/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition. 
 * 
 * RuleEngine.java
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
import com.see.truetransact.commonutil.TTException;

public class RuleEngine {
    
    /** Creates a new instance of RuleEngine */
    public RuleEngine() {
    }
    
    /**
     * returns Size of the Rules ArrayList
     *
     * return type is integer
     */
    public Object validateAll(RuleContext context, HashMap obj) throws ValidationRuleException {
        int size = context.size();
        ArrayList errsList = null;
        for (int i=0; i < size; i++) {
            try {
                context.getRule(i).validate(obj);
            } catch (Exception e) {
                if (errsList == null) {
                    errsList = new ArrayList();
                }
                if (e instanceof TTException && ((TTException) e).getExceptionHashMap() != null ) {
                    errsList.add(((TTException) e).getExceptionHashMap());
                } else {
                    errsList.add(e.getMessage());
                }
                System.out.println (e.getMessage());
                e.printStackTrace();
            }
        }
        return errsList;
    }
    
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        try {
            /*RuleContext context = new RuleContext();
            context.addRule(new OperativeAcctRule());
            context.addRule(new OperativeAcctRule());
            
            RuleEngine engine = new RuleEngine();
            System.out.println (engine.validateAll(context, 
                    new RuleSource().getSoruce()));
            System.out.println ("size:" + context.size());
            System.out.println ("getRule:" + context.getRule(0));*/
            
             /*RuleContext context = new RuleContext();
             context.addRule(new com.see.truetransact.businessrule.operativeaccount.LienApplicationRule());
             RuleEngine engine = new RuleEngine();
             HashMap inputMap = new HashMap();
             com.see.truetransact.transferobject.operativeaccount.AccountTO accountTO = new com.see.truetransact.transferobject.operativeaccount.AccountTO();
             accountTO.setActNum("OA00002010");
             inputMap.put("ACCOUNTTO", accountTO);
             System.out.println(engine.validateAll(context, inputMap));*/
              
        } catch (Exception e) {
            System.out.println (e.toString());
        }
    }    
}
