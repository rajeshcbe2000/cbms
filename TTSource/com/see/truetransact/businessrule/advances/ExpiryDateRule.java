/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition. 
 * 
 * ExpiryDateRule.java
 *
 * Created on February 4, 2008, 5:07 PM
 */

package com.see.truetransact.businessrule.advances;
import java.util.HashMap;
import java.util.List;

import com.ibatis.db.sqlmap.SqlMap;
import com.see.truetransact.commonutil.DateUtil;
import com.see.truetransact.businessrule.validateexception.ValidationRuleException;
import com.see.truetransact.businessrule.ValidationRule;
import com.see.truetransact.commonutil.CommonConstants;
import com.see.truetransact.commonutil.CommonUtil;
import com.see.truetransact.serverutil.ServerUtil;
import com.see.truetransact.commonutil.exceptionconstants.advances.AdvancesConstants;
import com.see.truetransact.serverside.transaction.common.TransactionDAOConstants;
import com.see.truetransact.servicelocator.ServiceLocator;
import com.see.truetransact.serverexception.ServiceLocatorException;
/**
 *
 * @author  Administrator
 */
public class ExpiryDateRule extends ValidationRule {
    private SqlMap sqlMap = null;
    /** Creates a new instance of ExpiryDateRule */
    public ExpiryDateRule() throws Exception {
        
        final ServiceLocator locate=ServiceLocator.getInstance();
        sqlMap=locate.getDAOSqlMap();
        
    }
    public void validate(java.util.HashMap inputMap) throws Exception {
        getTarget(inputMap);
    }
    private void getTarget(HashMap inputMap)throws Exception{
        super._branchCode=CommonUtil.convertObjToStr(inputMap.get(TransactionDAOConstants.BRANCH_CODE));
        System.out.println("inputmap###4444#"+inputMap);
        boolean tocheck=true;
        List expiryList=sqlMap.executeQueryForList("getTLBalance",inputMap);
       List getProd=sqlMap.executeQueryForList("getProductFromAcnt",inputMap);
         HashMap aMap=new HashMap();
         String PROD_ID="";
       if(getProd!=null && getProd.size()>0)
        {
            aMap=(HashMap)getProd.get(0);
            if(aMap!=null && aMap.containsKey("PRODUCT_ID") && aMap.get("PRODUCT_ID")!=null && !aMap.get("PRODUCT_ID").toString().equals("") )
            {
                PROD_ID=aMap.get("PRODUCT_ID").toString();
            }
            
        }
        System.out.println("PROD_ID.."+PROD_ID);
         aMap=new HashMap();
         aMap.put("PROD_ID",PROD_ID);
        List wetherCheck=sqlMap.executeQueryForList("getCheckingCondition", aMap);
        if(wetherCheck!=null && wetherCheck.size()>0)
        { aMap=new HashMap();
          
            aMap=(HashMap)wetherCheck.get(0);
            if(aMap!=null && aMap.containsKey("CALCULATE_EXPIRY") && aMap.get("CALCULATE_EXPIRY")!=null && aMap.get("CALCULATE_EXPIRY").toString().equals("Y") )
            {
                tocheck=false;
                
            }
            else
            {
                 tocheck=true;
            }
        }
        else
        {
            tocheck=true;
        }
        System.out.println("tocheck...."+tocheck);
        if(tocheck)
        {
        if(expiryList !=null && expiryList.size()>0){
            HashMap expiryMap=(HashMap)expiryList.get(0);
            if(DateUtil.dateDiff(DateUtil.getDateWithoutMinitues(ServerUtil.getCurrentDate(_branchCode)),
               DateUtil.getDateMMDDYYYY(CommonUtil.convertObjToStr(expiryMap.get("EXPIRY_DT"))))<0){
                 throw new ValidationRuleException(AdvancesConstants.ADVANCES_EXPIRY_DATE);
                 
            }
        }
    }
    }
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        // TODO code application logic here
    }
    
    
    
}
