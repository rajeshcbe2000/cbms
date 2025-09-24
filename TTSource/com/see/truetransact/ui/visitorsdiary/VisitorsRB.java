/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition.
 * 
 */
package com.see.truetransact.ui.visitorsdiary;
import java.util.ListResourceBundle;
public class VisitorsRB extends ListResourceBundle {
    public VisitorsRB(){
    }

    public Object[][] getContents() {
        return contents;
    }

    static final String[][] contents = {
        {"lblDateOfVisit", "Date of Visit"},
        {"lblInstiNameAddress", "Institution Name and  Address"},
        {"lblNameAddress", "Name and Address"},
        {"lblPurposeofVisit", "Purpose of Visit"},
        {"lblCommentsLeft", "Comments Left"}
    };
}
