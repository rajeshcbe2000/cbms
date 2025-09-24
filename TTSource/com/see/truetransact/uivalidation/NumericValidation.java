/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition.
 **
 *
 * NumericValidation.java
 *
 * Created on July 18, 2003, 9:56 AM
 */

package com.see.truetransact.uivalidation;

import java.awt.event.KeyEvent;
import com.see.truetransact.uivalidation.ValidationRB;
import com.see.truetransact.uicomponent.CTextField;
import java.text.DecimalFormat;

/** To Validate the text field for numbers only 
 * @author karthik, Bala
 */
public class NumericValidation extends UIComponentValidation{
    private String errorMessage;
    
    private int _nums=0;
    private int _decimals=0;
    
    /** Creates a new instance of NumericValidation
     */
    public NumericValidation() {
    }
    
    /** Creates a new instance of NumericValidation
     */
    public NumericValidation(int nums, int decimals) {
        _nums = nums;
        _decimals = decimals;
    }
    
    /**
     * @return
     */
    public boolean validate() {
        boolean returnValue = true;
        CTextField textField = (CTextField)getComponent();
        String content = textField.getText();
        if (content.length() != 0) {
            try {
                String textValue = textField.getText();
                if ((!textField.isAllowAll()) && textField.isAllowNumber()) {
                    returnValue = validString(textValue);
                    if (!returnValue) textField.setText("");
                } else
                    textField.setText( getFormattedText(textValue) );
            } catch (NumberFormatException e) {
                ValidationRB validationResourceBundle = new ValidationRB();
                setErrorMessage(validationResourceBundle.getString("numeric_warning"));
                textField.setText("");
                validationResourceBundle = null;
                returnValue = false;
            }
        }
        return returnValue;
    }
    
    private boolean validString(String str) {
        String strFind = "";
        boolean result = true;
        char arr[] = null;
        char prevChar=0, currChar;
        int ascii;
        for (int i=0, j=str.length();i<j;i++) {
            currChar = str.charAt(i);
            ascii = (int) currChar;
            if (!(ascii >= 48 && ascii <= 57)) {
                ValidationRB validationResourceBundle = new ValidationRB();
                setErrorMessage(validationResourceBundle.getString("numeric_warning"));
                validationResourceBundle = null;
                result = false;
                break;
            }
        }
        return result;
    }
    
    public String getFormattedText(String textValue){
        if( 0 != textValue.length() ) {
            char lastChar = textValue.charAt(textValue.length()-1);
            
            if (!Character.isDigit(lastChar)) {
                textValue = textValue.substring(0, textValue.length()-1);
                textValue = String.valueOf
                    (
                        Double.parseDouble(textValue) * Double.parseDouble
                        (CurrencyValidation.SHORTCUT_NUMS.get(String.valueOf(lastChar)).toString())
                     );
            }
            
            DecimalFormat numberFormat = new DecimalFormat();
            StringBuffer strB = new StringBuffer("##############");
            if (_decimals > 0) {
                strB.append(".");
                
                for (int i=0; i < _decimals; i++) {
                    strB.append("#");
                }
            }
            numberFormat.applyPattern(strB.toString());
            
            return numberFormat.format(Double.parseDouble(textValue) );
        }
        return textValue;
    }
    
    /** To restrict the user to enter only numbers
     * @param keyEvent
     * @return
     */
    public boolean validateEvent(java.awt.event.KeyEvent keyEvent){
        char keyChar = keyEvent.getKeyChar();
        if (!CurrencyValidation.validShortcuts(keyChar) &&  checkInValidCharactor(keyChar)) {
            keyEvent.consume();
        }
        return true;
    }
    
    private boolean checkInValidCharactor(char keyChar){        
        if(this._nums==0 && this._decimals==0){            
            if (keyChar == '.') 
                return true;
        }     
        if (!( Character.isDigit(keyChar) ||       
        (keyChar == '.' && !isDotPresent())||
        (keyChar == KeyEvent.VK_BACK_SPACE) ||
        (keyChar == KeyEvent.VK_DELETE) )) {
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
    private boolean checkNumDecimalLength(char keyChar){
        CTextField txtField =  (CTextField)getComponent();
        String str = txtField.getText();            
        int index=str.indexOf(".");
        int cp=txtField.getCaretPosition();        
        if(index>-1){
            String str1=str.substring(0,index);
            String str2=str.substring(index+1,str.length());
            if((str1.length()>=this._nums && cp<=index)||(str2.length()>=this._decimals && cp>index))
                return false;
        }else{
            if(str.length()>=this._nums && keyChar!='.')
                return false;
        }       
        if(this._decimals==0 && keyChar=='.')
                return false;        
        return true;
    }
    private boolean isDotPresent(){
        boolean flag = false;
        CTextField txtField =  (CTextField)getComponent();
        String str = txtField.getText();
        if (str.indexOf(".") > -1) {
            flag = true;
        }
        return flag;
    }
    public String getErrorMessage() {
        return this.errorMessage;
    }
    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }
}
