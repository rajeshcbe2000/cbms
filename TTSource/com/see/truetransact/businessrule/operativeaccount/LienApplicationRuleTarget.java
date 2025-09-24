/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition. 
 * 
 * LienApplicationRuleTarget.java
 *
 * Created on September 29, 2003, 4:53 PM
 */
package com.see.truetransact.businessrule.operativeaccount;

import java.util.HashMap;

import java.util.List;
/*import java.util.ArrayList;
import java.util.HashMap;

import java.util.Date;
import java.rmi.RemoteException;*/

import com.ibatis.db.sqlmap.SqlMap;
import com.see.truetransact.servicelocator.ServiceLocator;
import com.see.truetransact.serverexception.ServiceLocatorException;
//import com.see.truetransact.clientutil.DateUtil;
import com.see.truetransact.transferobject.operativeaccount.AccountTO;
/*import com.see.truetransact.transferobject.transaction.cash.TxCashTO;
import com.see.truetransact.transferobject.transaction.transfer.TxTransferTO;
import com.see.truetransact.transferobject.TOHeader;
import com.see.truetransact.serverside.TTDAO;
import com.see.truetransact.commonutil.TTException;
import com.see.truetransact.commonutil.CommonConstants;
import com.see.truetransact.commonutil.NoCommandException;
import com.see.truetransact.serverexception.TransRollbackException;*/

/**
 * @author  karthik
 */
public class LienApplicationRuleTarget {
    private HashMap targetMap;
    private SqlMap sqlMap;
    
    /** Creates a new instance of RuleTarget */
    public LienApplicationRuleTarget() throws Exception{
        final ServiceLocator locate = ServiceLocator.getInstance();
        sqlMap = (SqlMap) locate.getDAOSqlMap();
        targetMap  = new HashMap(); 
    }
    
    public HashMap getTarget(HashMap inputMap) throws Exception{
        List list = (List) sqlMap.executeQueryForList("lienApplicationRule", (AccountTO)inputMap.get("ACCOUNTTO"));
        HashMap resultMap = (HashMap)list.get(0);
        if( resultMap.get("ACT_STATUS_ID").equals("PARTIALFREEZE") ){
            targetMap.put("FREEZE", new Boolean(true));
        }
        else
        {
            targetMap.put("FREEZE", new Boolean(false));
        }
        return targetMap;
    }
    
   /* public void getAccountStatus(AccountTO accountTO) throws Exception{
        List list = (List) sqlMap.executeQueryForList("lienApplicationRule", accountTO);
        HashMap resultMap = (HashMap)list.get(0);
        if( resultMap.get("ACT_STATUS_ID").equals("PARTIALFREEZE") || resultMap.get("ACT_STATUS_ID").equals("COMPLETEFREEZE") ){
            targetMap.put("FREEZE", new Boolean(true));
        }
        else
        {
            targetMap.put("FREEZE", new Boolean(false));
        }
    }*/
    
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws Exception{
        /*LienApplicationRuleTarget lrt = new LienApplicationRuleTarget();
        AccountTO accountTO = new AccountTO();
        accountTO.setActNum("OA00002010");
        lrt.getAccountStatus(accountTO);
        System.out.println (lrt.getTarget());*/
    }
}