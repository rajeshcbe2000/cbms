/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition. 
 * 
 * LienApplicationRule.java
 *
 * Created on November 24, 2003, 3:08 PM
 */

package com.see.truetransact.businessrule.operativeaccount;
import java.util.HashMap;
import com.see.truetransact.businessrule.ValidationRule;
import com.see.truetransact.businessrule.validateexception.ValidationRuleException;
import com.see.truetransact.transferobject.operativeaccount.AccountTO;
import com.see.truetransact.commonutil.exceptionconstants.operativeaccount.OperativeAccountConstants;
import com.ibatis.db.sqlmap.SqlMap;
import com.see.truetransact.servicelocator.ServiceLocator;
import com.see.truetransact.serverexception.ServiceLocatorException;
import com.see.truetransact.transferobject.operativeaccount.AccountTO;
import java.util.List;

import com.see.truetransact.commonutil.CommonUtil;
/**
 *
 * @author  karthik
 */
public class LienApplicationRule extends ValidationRule{
    //private HashMap tar;
    //private LienApplicationRuleTarget objLienApplicationRuleTarget;
    private SqlMap sqlMap;
    private String advances="";
    /** Creates a new instance of LienApplicationRule */
    public LienApplicationRule() throws Exception {
        final ServiceLocator locate = ServiceLocator.getInstance();
        sqlMap = (SqlMap) locate.getDAOSqlMap();
    }
    
    public void validate(HashMap inputMap) throws Exception {
        //        String operation=(String)inputMap.get("Operation");
        System.out.println("Before GetTarget...");
        System.out.println("inputMap: " + inputMap);
        getTarget(inputMap);
    }
    
    public void getTarget(HashMap inputMap) throws Exception{
        List list;
        HashMap resultMap;
        double amount =  Double.parseDouble(CommonUtil.convertObjToStr(inputMap.get("AMOUNT")));
        System.out.println("Before lienApplicationRule...");
        if(inputMap.containsKey("ADVANCES")){
            list = (List) sqlMap.executeQueryForList("lienApplicationRuleAD", inputMap);
            advances=(String)inputMap.get("ADVANCES");
        }else{
            list = (List) sqlMap.executeQueryForList("lienApplicationRule", (AccountTO)inputMap.get("ACCOUNTTO"));
            advances="";
        }
        resultMap = (HashMap)list.get(0);
        if( resultMap.get("ACT_STATUS_ID").equals("COMP_FREEZE")){
            System.out.println("Account is Completely Freezed");
            throw new ValidationRuleException(OperativeAccountConstants.FREEZE);
        }else{
            System.out.println("Account is Not Completely Freezed");
            //            limitCheck(amount, CommonUtil.convertObjToStr(((AccountTO)inputMap.get("ACCOUNTTO")).getActNum()),
            //            CommonUtil.convertObjToStr(((AccountTO)inputMap.get("BRANCH_CODE")).getBranchCode()));
            if(inputMap.containsKey("ADVANCES"))
                limitCheck(amount,CommonUtil.convertObjToStr(inputMap.get("ACCT_NUM")),
                CommonUtil.convertObjToStr(inputMap.get("BRANCH_CODE")));
            else{
                limitCheck(amount, CommonUtil.convertObjToStr(((AccountTO)inputMap.get("ACCOUNTTO")).getActNum()),
                CommonUtil.convertObjToStr(((AccountTO)inputMap.get("ACCOUNTTO")).getBranchCode()));
            }
        }
    }
    
    private void limitCheck(double amount, String accountNo,String branch_code) throws Exception{
        
        System.out.println("limitCheck()");
        System.out.println("accountNo: "+accountNo);
        System.out.println("amount: "+amount);
        System.out.println("branchId: "+branch_code);
        
        HashMap resultMap = new HashMap();
        List list=null;
        if(advances.equals("ADVANCES")){
            resultMap.put("ACCOUNTNO",accountNo);
            resultMap.put("BRANCH_CODE",branch_code);
            System.out.println("resultMap@@@"+resultMap);
            list = (List) sqlMap.executeQueryForList("Lein.getAvailableBalanceAD", resultMap);
        }
        else
            list = (List) sqlMap.executeQueryForList("Lein.getAvailableBalance", accountNo);
        resultMap = (HashMap)list.get(0);
        double availBalance = Double.parseDouble(CommonUtil.convertObjToStr(resultMap.get("AVAILABLE_BALANCE")));
        System.out.println("availBalance: " + availBalance);
        if(amount > availBalance){
            System.out.println("Amount greater than Available Balance");
            throw new ValidationRuleException(OperativeAccountConstants.FREEZE);
        }else{
            System.out.println("Amount Less than Available Balance");
            System.out.println("You Can Proceed");
        }
    }
    
    public static void main(String[] args) throws Exception{
        HashMap inputMap = new HashMap();
        
        AccountTO accountTO = new AccountTO();
        accountTO.setActNum("OA060888");
        //        accountTO.setActStatusId("LIENMARKED");
        
        inputMap.put("ACCOUNTTO", accountTO);
        //        inputMap.put("Operation", "FREEZE");
        inputMap.put("AMOUNT", "1500");
        
        LienApplicationRule lienRule = new LienApplicationRule();
        lienRule.validate(inputMap);
    }
}
