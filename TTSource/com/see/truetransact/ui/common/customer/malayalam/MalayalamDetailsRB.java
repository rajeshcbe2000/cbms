/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved..
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition.
 **
 *
 * CustDetailsRB.java
 * 
 * Created on Fri Dec 24 10:13:55 IST 2004
 */

package com.see.truetransact.ui.common.customer.malayalam;

import java.util.ListResourceBundle;

public class MalayalamDetailsRB extends ListResourceBundle {
    public MalayalamDetailsRB(){
    }

    public Object[][] getContents() {
        return contents;
    }

    static final String[][] contents = {
        {"lblMemNo", "Member No:"} ,
        {"lblRegName", "Name"} ,
        {"lblRegHname", "House Name"} ,
        {"lblRegPlace", "Place"} ,
        {"lblRegVillage", "Village"} ,
        {"lblRegAmsam", "Amsam"} ,
        {"lblRegDesam", "Desam"} ,
        {"lblRegGardName", "Guardian Name"},
   };

}
