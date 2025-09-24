/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition.
 **
 *
 * CurrencyValidation.java
 *
 * Created on July 18, 2003, 9:56 AM
 */

package com.see.truetransact.uivalidation;

import java.awt.event.KeyEvent;
import java.text.NumberFormat;
import java.text.DecimalFormat;
import java.util.Currency;
import java.util.Locale;
import java.util.HashMap;
import com.see.truetransact.uivalidation.ValidationRB;
import com.see.truetransact.uicomponent.CTextField;

/** To Validate the text field for currency only entries
 * @author karthik
 */
public class CurrencyValidation extends UIComponentValidation{
    
    public static final HashMap SHORTCUT_NUMS = new HashMap();
    
    static {
        // Thousand
        SHORTCUT_NUMS.put("T", "1000");
        SHORTCUT_NUMS.put("t", "1000");
        SHORTCUT_NUMS.put("K", "1000");
        SHORTCUT_NUMS.put("k", "1000");
        // Lakh 1,00,000    5 zeros
        SHORTCUT_NUMS.put("L", "100000");
        SHORTCUT_NUMS.put("l", "100000");
        // Crore 1,00,00,000    7 zeros
        SHORTCUT_NUMS.put("C", "10000000");
        SHORTCUT_NUMS.put("c", "10000000");
        // Million 1,000,000    6 zeros
        SHORTCUT_NUMS.put("M", "1000000");
        SHORTCUT_NUMS.put("m", "1000000");
        // Billion 1,000,000,000    9 zeros (Clarify)
        SHORTCUT_NUMS.put("B", "1000000000");
        SHORTCUT_NUMS.put("b", "1000000000");
    }
    
    private String languageCode;
    private String countryCode;
    private String integerSeperator;
    private char decimalSeperatorChar;
    private String errorMessage;
    
    private int _nums=0;
    private int _decimals=0;
    
    private void initializeCurrencyParameters(){
        ValidationRB validationResourceBundle = new ValidationRB();
        languageCode = validationResourceBundle.getString("language_code");
        countryCode = validationResourceBundle.getString("country_code");
        integerSeperator = validationResourceBundle.getString("integer_seperator");
        decimalSeperatorChar = validationResourceBundle.getString("decimal_seperator").charAt(0);
        validationResourceBundle = null;
    }
    /** Creates a new instance of CurrencyValidation & initializes variables for getting
     * the Locale & validation
     */
    public CurrencyValidation() {  
        initializeCurrencyParameters();
    }
    public CurrencyValidation(int num,int decimal){
        initializeCurrencyParameters();
        this._nums=num;
        this._decimals=decimal;
        
    }
    
    /** To validate whether the given text is of Currency Format or not. If not set the
     * proper error message.
     * If the entered amount is placed with improper Integer Seperator, it is
     * automatically handled & the value is set in the following code snippet
     *
     * textValue = textValue.replaceAll(integerSeperator,"");
     * textValue = textValue.replaceAll(",",".");
     * textField.setText( numberFormat.format(Double.parseDouble(textValue) ) );
     * @return
     */    
    public boolean validate() {
        boolean returnValue = true;
        CTextField textField = (CTextField)getComponent();
        String content = textField.getText();
        if (content.length() != 0) {
            try {
                String textValue = textField.getText();
                textField.setText( getFormattedText(textValue) );
            } catch (NumberFormatException e) {
                ValidationRB validationResourceBundle = new ValidationRB();
                setErrorMessage(validationResourceBundle.getString("currency_warning"));
                textField.setText("");
                validationResourceBundle = null;
                returnValue = false;
            }
        }
        return returnValue;
    }
    
    public String getFormattedText(String textValue){
        if( 0 != textValue.length() ) {
            char lastChar;
            double currData;
            // 10,00,000,00,00,000
            textValue = textValue.replaceAll(integerSeperator,"");
            textValue = textValue.replaceAll(",",".");
            
            lastChar = textValue.charAt(textValue.length()-1);
            
            if (!Character.isDigit(lastChar) && lastChar!='.') {
                textValue = textValue.substring(0, textValue.length()-1);
                textValue = String.valueOf
                    (
                        Double.parseDouble(textValue) * Double.parseDouble
                        (SHORTCUT_NUMS.get(String.valueOf(lastChar)).toString())
                     );
            }
            
//            DecimalFormat numberFormat = new DecimalFormat();
//            numberFormat.applyPattern("##,##,###,##,##,###.00");
//            NumberFormat numberFormat = NumberFormat.getInstance(); //new Locale(languageCode,countryCode));
            
            currData = Double.parseDouble(textValue);
            if (currData <= -1) {
                currData = 0;
            }
            
            return formatCrore ( String.valueOf( currData ) );
        }
        return textValue;
    }
    
    public static String formatCrore(String str) {
        java.text.DecimalFormat numberFormat = new java.text.DecimalFormat();
        numberFormat.applyPattern("########################0.00");
        
        double currData = Double.parseDouble(str.replaceAll(",",""));
        str = numberFormat.format( currData );
        
        String num = str.substring(0,str.lastIndexOf(".")).replaceAll(",","");
        String dec = str.substring(str.lastIndexOf("."));
        
        String sign = "";
        if (num.substring(0,1).equals("-")) {
            sign = num.substring(0,1);
            num = num.substring(1,num.length());
        }
        
        char[] chrArr = num.toCharArray();
        StringBuffer fmtStrB = new StringBuffer();
        
        for (int i=chrArr.length-1, j=0, k=0; i >= 0; i--) {
            if ((j==3 && k==3) || (j==2 && k==5) || (j==2 && k==7)) {
                fmtStrB.insert(0, ",");
                if (k==7) k = 0;
                j=0;
            }
            j++; k++;
            
            fmtStrB.insert(0, chrArr[i]);
        }
        fmtStrB.append (dec);
        
        str = fmtStrB.toString();
        
        str = sign+str;
        
        if (str.equals(".00")) str = "0";
        
        return str;
    }    
    
    public static boolean validShortcuts(char keyChar) {
        return SHORTCUT_NUMS.containsKey(String.valueOf(keyChar));
    }

    /** To restrict the user to enter only numbers & proper Integer & Decimal seperators
     * @param keyEvent
     * @return
     */    
    public boolean validateEvent(java.awt.event.KeyEvent keyEvent){
        char keyChar = keyEvent.getKeyChar();
        if ( !validShortcuts(keyChar) && checkInValidCharactor(keyChar) ) {
            keyEvent.consume();
        }        
        return true;
    }
    private boolean checkNumDecimalLength(char keyChar){
        CTextField txtField =  (CTextField)getComponent();
        String str = txtField.getText();            
        int index=str.indexOf(".");
        int cp=txtField.getCaretPosition();        
        if(index>-1){
            String str1=str.substring(0,index);
            String str2=str.substring(index+1,str.length());
            if((str1.length()>=this._nums && cp<=index && keyChar!=',')||(str2.length()>=this._decimals && cp>index))
                return false;
        }else{
            if(str.length()>=this._nums && keyChar!='.' && keyChar!=',')
                return false;
        }       
        if(this._decimals==0 && keyChar=='.')
                return false;        
        return true;
    }
    private boolean checkInValidCharactor(char keyChar){
         if(this._nums==0 && this._decimals==0){            
            if (keyChar == '.') 
                return true;
        }   
        if (!(Character.isDigit(keyChar) ||
        (keyChar == ',') ||
        (keyChar == '.' && !checkDecimalSeparator(keyChar)) ||
        (keyChar == KeyEvent.VK_BACK_SPACE) ||
        (keyChar == KeyEvent.VK_DELETE))) {
            return true;
        }
        if(!((keyChar == KeyEvent.VK_BACK_SPACE) ||                
                 (keyChar == KeyEvent.VK_DELETE))) {
            if(this._nums!=0 || this._decimals!=0){            
                if(!checkNumDecimalLength(keyChar))
                   return true;
            }      
        }
        
        return false;
    }
    
    private boolean checkDecimalSeparator(char keyChar){
        if(keyChar == decimalSeperatorChar && !decimalSeperator()){
            return true;
        }
        return false;
    }
    
    /** To check whether the decimal seperator already exists. If exists return false
     * @return
     */    
    private boolean decimalSeperator() {
        CTextField textField = (CTextField)getComponent();
        String textValue = textField.getText();
        int firstDecimalSeperatorIndex = textValue.indexOf(decimalSeperatorChar);
        
        if( checkDecimalSeperator(firstDecimalSeperatorIndex) ) {
            return true;
        }
        return false;
    }
    
    private boolean checkDecimalSeperator(int firstDecimalSeperatorIndex){
        if( firstDecimalSeperatorIndex < 0 ) {
            return true;
        }
        return false;
    }

    public String getErrorMessage() {
        return this.errorMessage;
    }
    
    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }
    
}
