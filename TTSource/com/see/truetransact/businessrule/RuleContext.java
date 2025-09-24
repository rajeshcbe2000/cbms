/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition. 
 * 
 * RuleContext.java
 *
 * Created on September 30, 2003, 11:36 AM
 */
package com.see.truetransact.businessrule;

/**
 * @author  balachandar
 */
import java.util.HashMap;
import java.util.ArrayList;

import com.see.truetransact.businessrule.validateexception.ValidationRuleException;

public class RuleContext {
    ArrayList rulesList;
    
    /** Creates a new instance of RuleContext */
    public RuleContext() {
        rulesList = new ArrayList();
    }
    
    /**
     * addRule is used to adding rules into the RuleContext
     * It takes parameter as ValidationRule
     * and adding into ArrayList for the collection.
     *
     * @param  rule (ValidationRule)
     */
    public void addRule(ValidationRule rule) {
        rulesList.add(rule);
    }
    
    /**
     * getRule returns ValidationRule and takes index as an argument
     * If the index exceeds the ArrayList size then it will throw
     * IndexOutOfBoundsException.
     * 
     * return type is ValidationRule
     *
     * @param index (int)
     */
    public ValidationRule getRule(int index) throws IndexOutOfBoundsException {
        if (index >= rulesList.size())
            throw new IndexOutOfBoundsException("No Rules pending");
        
        return (ValidationRule) rulesList.get(index);
    }
    
    /**
     * returns Size of the Rules ArrayList
     *
     * return type is integer
     */
    public int size() {
        return rulesList.size();
    }
    
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        try {
           /* RuleContext context = new RuleContext();
            context.addRule(new OperativeAcctRule());
            context.addRule(new OperativeAcctRule());
            context.addRule(new OperativeAcctRule());

            System.out.println ("size:" + context.size());
            System.out.println ("getRule:" + context.getRule(0));*/
        } catch (Exception e) {
            System.out.println (e.toString());
        }
    }    
}
