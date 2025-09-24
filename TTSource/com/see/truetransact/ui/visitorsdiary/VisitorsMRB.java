/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition.
 * 
 *
 * ShareProductMRB.java
 * 
 * Created on Mon Apr 11 12:14:57 IST 2005
 */

package com.see.truetransact.ui.visitorsdiary;

import java.util.ListResourceBundle;

public class VisitorsMRB extends ListResourceBundle {
    public VisitorsMRB(){
    }

    public Object[][] getContents() {
        return contents;
    }

    static final String[][] contents = {
        {"tdtDateofVisit", "Date of Visit should be a proper value!!!\n"},
        {"txaInstiNameAddress", "Instituition Name & Address should be a proper value!!!\n"},
        {"txaNameAddress", "Name & Address should be a proper value!!!\n"},
        {"txaPurposeofVisit", "Purpose of Visit should be a proper value!!!\n"},
        {"txaCommentsLeft", "Comments Left should be a proper value!!!\n"}
   };

}
