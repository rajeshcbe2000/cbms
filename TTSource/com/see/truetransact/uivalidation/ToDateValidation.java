/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition. 
 * 
 * ToDateValidation.java
 *
 * Created on March 31, 2004, 4:17 PM
 */

package com.see.truetransact.uivalidation;

import com.see.truetransact.commonutil.DateUtil;
import com.see.truetransact.uicomponent.CDateField;
import com.see.truetransact.uivalidation.UIComponentValidation;
import java.util.Calendar;
import java.util.Date;

/**
 *
 * @author  Pinky
 * @modified  Jay
 */

public class ToDateValidation extends UIComponentValidation{
    String errorMessage;
    Date startDate;
    int minorYr;
    boolean before = false;
    /** Creates a new instance of ToDateValidation */
    public ToDateValidation(Date startDate) {
        this.startDate=startDate;
    }
    
    /** Creates a new instance of ToDateValidation */
    public ToDateValidation(Date startDate, boolean before) {
        this.startDate=startDate;
        this.before = before;
    }
    
    /** Creates a new instance of ToDateValidation */
    public ToDateValidation(Date startDate, boolean before, String minor) {
        this.startDate=startDate;
        this.before = before;
        this.minorYr = Integer.parseInt(minor);
    }
    
    public boolean validate() {
        boolean validDate = false;
        CDateField dateField = (CDateField)getComponent();
        String date = dateField.getDateValue();
        Date toDate = DateUtil.getDateMMDDYYYY(date);
        Calendar calFrom=Calendar.getInstance();
        Calendar calTo=Calendar.getInstance();
        calFrom.setTime(startDate);
        calTo.setTime(toDate);
        
        if (before) {
            validDate = calTo.before(calFrom) | calTo.equals(calFrom);
            if (validDate == false)
                setErrorMessage("Not valid Date");            
        } else {
            validDate = calTo.after(calFrom) | calTo.equals(calFrom);
            if (validDate == false)
                setErrorMessage("Not valid Date");            
        }
        
        //-- To check If it is a Major, Minor or Minor Under Wards
        if (validDate == true){
            if(minorYr > 0){
            	calTo.add(Calendar.YEAR, minorYr);
                if(calTo.before(calFrom)){
                    validDate=false;
                    setErrorMessage("Not valid Date");
                }
            }
        }
        return validDate;
    }
    
    /**
     * @param args the command line arguments
     */
    
    public String getErrorMessage() {
        return this.errorMessage;
    }
    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }    
}
