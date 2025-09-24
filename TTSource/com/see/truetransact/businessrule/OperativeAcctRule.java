/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition. 
 * 
 * OperativeAcctRule.java
 *
 * Created on September 30, 2003, 11:36 AM
 */

package com.see.truetransact.businessrule;

/**
 * @author  balachandar
 */
import java.util.HashMap;

import com.see.truetransact.businessrule.validateexception.ValidationRuleException;

public class OperativeAcctRule extends ValidationRule {
    HashMap tar = null;
    /** Creates a new instance of RuleEngine */
    public OperativeAcctRule() {
        tar = new RuleTarget().getTarget();
    }
    
   // public OperativeAcctRule() {
     //   tar = new RuleTarget().getTarget();
    //}
    
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        try {
            new OperativeAcctRule().validate(new RuleSource().getSoruce());
        } catch (Exception e) {
            System.out.println (e.toString());
            //e.printStackTrace();
        }
    }

    public void validate(HashMap sour) throws com.see.truetransact.businessrule.validateexception.ValidationRuleException {

        int balance = Integer.parseInt((String)sour.get("BALANCE"));
        int min = Integer.parseInt((String)tar.get("MINBALANCE"));
        int max = Integer.parseInt((String)tar.get("MAXBALANCE"));
        
        if (balance < min || balance > max) 
            throw new ValidationRuleException("OperativeAcctRule");
    }
}
