/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition.
 **
 *
 * UIComponentValidation.java
 *
 * Created on July 18, 2003, 9:41 AM
 */

package com.see.truetransact.uivalidation;
import java.awt.Component;
/** This is the super class for all UI Component Validations.
 * @author karthik
 */
public abstract class UIComponentValidation {
    
    private Component component;
    /** To check the validity of the entry
     * @return
     */    
    public boolean validate(){
        return true;
    }
    /** To set the ErrorMessage if the entered value is invalid. This message can be
     * used to display the user understandable error
     * @param errorMessage
     */    
    public abstract void setErrorMessage(String errorMessage);
    /** To get the ErrorMessage */    
    public abstract String getErrorMessage();
    
    /** To validate the individual keys typed. It can be used to consume the event, if
     * the event violates the given condition
     * @param keyEvent
     * @return
     */    
    public boolean validateEvent(java.awt.event.KeyEvent keyEvent){
        return true;
    }
    
    public void setComponent(Component component){
        this.component = component;
    }
    
    public Component getComponent(){
        return component;
    }
    
}
 