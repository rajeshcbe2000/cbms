/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition.
 **
 *
 * ViewPhotoSignRB.java
 * 
 * Created on Tue Aug 10 16:15:58 GMT+05:30 2004
 */

package com.see.truetransact.ui.common.viewphotosign;

import java.util.ListResourceBundle;

public class ViewPhotoSignRB extends ListResourceBundle {
    public ViewPhotoSignRB(){
    }

    public Object[][] getContents() {
        return contents;
    }

    static final String[][] contents = {
        {"lblSign", ""},
        {"panPhoto", "Photograph"},
        {"panSign", "Signature"},
        {"lblPhoto", ""} 

   };

}
