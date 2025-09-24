/*

 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition. 
 * 
 * ScheduleTaskRB.java
 *
 * Created on October 7, 2003, 10:28 AM
 */

package com.see.truetransact.ui.scheduler;

/**
 *
 * @author  Senthil
 */
public class ScheduleTaskRB extends java.util.ListResourceBundle {
    
    static final String[][] contents = {
	{"frameTitle","Schedule Task"},    
        {"jLabelRepName","Report Name"},
        {"jLabelFromDate","From Date"},    
        {"jLabelToDate","To Date"},    
        {"jLabelFieldFromTree","Field From Tree"},   
        {"jLabelSelectUser","Select User"},   
        {"jRadioButtonDaily","Daily"},   
        {"jRadioButtonWeekly","Weekly"},   
        {"jRadioButtonMonthly","Monthly"},   
        {"Sunday","Sunday"},
        {"Monday","Monday"},   
        {"Tuesday","Tuesday"},   
        {"Wednesday","Wednesday"},   
        {"Thursday","Thursday"},   
        {"Friday","Friday"},
        {"Saturday","Saturday"},
        {"jRadi0ButtonReportOn","Report On"},
        {"jLabelHour","Hour"},
        {"jLabelMin","Minutes"},
	{"jButtonScheduleReq","Show Report Request List"},
        {"jButtonScheduleJobUSER","Report Request"},
	{"jButtonScheduleJobADMIN","Schedule Job"},
        {"jButtonCancel","Close"},
        {"msgNoItemsInReportRequestList","No Items available in Report Request List."},
        {"msgPlSelectUsers","Plese select user(s) from the User List."}        
    };    
    
    /** Creates a new instance of ScheduleTaskRB */
    public ScheduleTaskRB() {
    }
    
    /**
     * The method is used to get contents of the ResourceBundle.
     * @return java.lang.Object
     */      
    protected Object[][] getContents() {
        return contents;
    }    
}
