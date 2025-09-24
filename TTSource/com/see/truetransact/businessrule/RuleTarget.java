/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition. 
 * 
 * RuleTarget.java
 *
 * Created on September 29, 2003, 4:53 PM
 */
package com.see.truetransact.businessrule;

import java.util.HashMap;
/**
 * @author  balachandar
 */
public class RuleTarget {
    HashMap hash = new HashMap();
    
    /** Creates a new instance of RuleTarget */
    public RuleTarget() {
        hash.put("MINBALANCE", "1000");
        hash.put("MAXBALANCE", "5000");
    }
    
    public HashMap getTarget() {
        return hash;
    }
    
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        System.out.println ((new RuleTarget()).getTarget());
    }
}