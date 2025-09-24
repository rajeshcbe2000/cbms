/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition. 
 * 
 * IPValidation.java
 *
 * Created on February 6, 2004, 2:15 PM
 */

package com.see.truetransact.uivalidation;

/**
 *
 * @author  Hemant
 */
import java.awt.event.KeyEvent;

import com.see.truetransact.uivalidation.ValidationRB;
import com.see.truetransact.uicomponent.CTextField;

public class IPValidation extends UIComponentValidation {
    
    private String errorMessage;
    
    /** Creates a new instance of IPValidation */
    public IPValidation() {
    }
    
    /** 
     * @return
     */    
    public boolean validate() {
       return true;
    }
    
    /** To restrict the user to enter only numbers 
     * @param keyEvent
     * @return
     */    
    public boolean validateEvent(java.awt.event.KeyEvent keyEvent){
        char keyChar = keyEvent.getKeyChar();
        if ( checkInValidCharactor(keyChar)) {
            keyEvent.consume();
            
        }
        return true;
    }
    
    private boolean checkInValidCharactor(char keyChar){
        if (!( checkValidOctateDigit(keyChar) ||
        checkValidSeparator(keyChar)||
        (keyChar == KeyEvent.VK_BACK_SPACE) ||
        (keyChar == KeyEvent.VK_DELETE)) || keyChar== KeyEvent.VK_KP_LEFT) {
            return true;
        }
        return false;
    }
    
    private boolean checkValidOctateDigit(char keyChar){
        boolean flag = false;
        try{
            if(Character.isDigit(keyChar)){
                String str = ((CTextField)getComponent()).getText();
                int octate = 0;
                //String octateStr = str.substring(str.lastIndexOf(".")+1);
                String octateStr = getOctate(keyChar);
                System.out.println("In Invaild digit"+octateStr);
                int len = octateStr.length();
                if(len<4){
                    if(len > 0){
                        octate = Integer.parseInt(octateStr);
                    }
                        //octate = octate*10 + Character.getNumericValue(keyChar);
                        if( octate > 255)
                            flag = false;
                        else
                            flag = true;
                     
                }   
            }    
        }catch(Exception e){
            e.printStackTrace();
        }
        return flag;
    }
    
    private boolean checkValidSeparator(char keyChar){
        boolean flag = false;
        if(keyChar == '.'){
            CTextField txtField =  (CTextField)getComponent();
            String str = txtField.getText();
            int len = str.length();
            int cp = txtField.getCaretPosition();
            if(len == 0 || cp==0){
                return flag;
            }
            
            if(cp < len){
                if(str.charAt(cp-1)=='.' || str.charAt(cp)=='.')
                return flag;
            }else if(str.charAt(len-1)=='.')
                return flag;
            
            if(separatorFreq(str) < 3)
                flag = true;
        }   
        return flag;
    }
    
    private int separatorFreq(String str){
        int freq = 0;
        if(str != null){
            int len = str.length();
            int i=0;
            while(i<len){
                if(str.charAt(i)=='.')
                    freq++;
                i++;
            }
        }
        System.out.println("Frequency :"+freq);
        return freq;
    }  
    
    public String getErrorMessage() {
        return this.errorMessage;
    }
    
    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }
    
    private String getOctate(char keyChar){
        CTextField txtField = (CTextField)getComponent();
        String str = txtField.getText();
        String octateStr = "";
        int cp = txtField.getCaretPosition();
        System.out.println("CP:"+cp);
        int len = str.length();
        if(cp < len){
            str = str.substring(0,cp)+Character.toString(keyChar)+str.substring(cp);
            System.out.println(str);
            len++;
            int end = cp;
            while(end < len){
                if(str.charAt(end)=='.'){
                    break;
                }    
                end++;
            }
            
            int begins = cp;
            while(begins>=0){
                if(str.charAt(begins)=='.'){
                    break;
                }    
                begins--;
            }
            
            if(begins!=0)
                begins++;
            octateStr = str.substring(begins,end);
            return octateStr;
        }
        octateStr = str.substring(str.lastIndexOf(".")+1)+keyChar;    
        return octateStr;    
    }
}
