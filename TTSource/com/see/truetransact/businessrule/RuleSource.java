/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition. 
 * 
 * RuleSource.java
 *
 * Created on September 29, 2003, 4:51 PM
 */

package com.see.truetransact.businessrule;

import java.util.HashMap;
/**
 *
 * @author  balachandar
 */
public class RuleSource {
    HashMap map = new HashMap();

    /** Creates a new instance of RuleSource */
    public RuleSource() {
        map.put("BALANCE", "8000");
    }

    public HashMap getSoruce() {
        return map;
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        System.out.println ((new RuleSource()).getSoruce());
    }
    
}
