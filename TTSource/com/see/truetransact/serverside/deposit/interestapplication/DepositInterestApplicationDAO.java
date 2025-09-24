/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition.
 * 
 *
 * DepositInterestApplicationDAO.java
 * 
 * Created on Tue Oct 11 13:18:08 IST 2011
 */
package com.see.truetransact.serverside.deposit.interestapplication;

import java.util.List;
import java.util.Date;
import java.util.Map;
import java.util.HashMap;
import java.util.ArrayList;
import com.ibatis.db.sqlmap.SqlMap;
import com.see.truetransact.serverside.TTDAO;
import com.see.truetransact.commonutil.DateUtil;
import com.see.truetransact.serverutil.ServerUtil;
import com.see.truetransact.commonutil.CommonUtil;
import com.see.truetransact.commonutil.CommonConstants;
import com.see.truetransact.servicelocator.ServiceLocator;
import com.see.truetransact.serverexception.ServiceLocatorException;
import com.see.truetransact.serverside.common.transaction.TransferTrans;
import com.see.truetransact.serverside.transaction.transfer.TransferDAO;
import com.see.truetransact.serverside.common.transaction.TransactionDAO;
import com.see.truetransact.transferobject.common.transaction.TransactionTO;
import com.see.truetransact.transferobject.transaction.transfer.TxTransferTO;
import com.see.truetransact.serverside.transaction.common.TransactionFactory;
import com.see.truetransact.serverside.batchprocess.task.TaskHeader;
import com.see.truetransact.serverside.batchprocess.task.operativeaccount.interest.DepositIntTask;
import com.see.truetransact.serverside.common.sms.SmsConfigDAO;

/**
 * DepositInterestApplication DAO.
 *
 */
public class DepositInterestApplicationDAO extends TTDAO {

    private static SqlMap sqlMap = null;
    private Date currDt = null;
    private Map returnMap = null;

    /**
     * Creates a new instance of OperativeAcctProductDAO
     */
    public DepositInterestApplicationDAO() throws ServiceLocatorException {
        ServiceLocator locate = ServiceLocator.getInstance();
        sqlMap = (SqlMap) locate.getDAOSqlMap();
    }

    public static void main(String str[]) {
        try {
            DepositInterestApplicationDAO dao = new DepositInterestApplicationDAO();
            HashMap inputMap = new HashMap();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public HashMap execute(HashMap map) throws Exception {
        _branchCode = (String) map.get(CommonConstants.BRANCH_ID);
        currDt = ServerUtil.getCurrentDate(_branchCode);
        System.out.println("DepositInterestApplicationDAO execute map : "+map+" currDt : "+currDt.clone());
        if (map.containsKey("ACCOUNT_LIST") || map.containsKey("CAL_FREQ_ACCOUNT_LIST")) {
            try {
                returnMap = new HashMap();
                DepositIntTask task = new DepositIntTask(map);
               returnMap.put("INTEREST_DATA", task.getInterestTableDataKSSB("SAVE"));
              // returnMap.put("INTEREST_DATA", task.getInterestTableData("SAVE"));
            } catch (Exception e) {
                sqlMap.rollbackTransaction();
                e.printStackTrace();
                throw e;
            }
            destroyObjects();
        }else if (map.containsKey("SMS_ACCOUNT_LIST")) {
            List finalTableList = null;
            try {
                if (map.containsKey("SMS_ACCOUNT_LIST") && map.containsKey("DEPOSIT_INTEREST_SCREEN")) {
                    finalTableList = (List) map.get("SMS_ACCOUNT_LIST");
                    SmsConfigDAO smsConfigDAO = new SmsConfigDAO();
                    if (finalTableList != null && finalTableList.size() > 0) {
                        for (int i = 0; i < finalTableList.size(); i++) {
                            double totalCount = 0;
                            HashMap interestSB = new HashMap();
                            HashMap multipletoSingleMap = (HashMap) finalTableList.get(i);
                            System.out.println("multipletoSingleMap : " + multipletoSingleMap);
                            String custId = CommonUtil.convertObjToStr(multipletoSingleMap.get("CUST_ID"));
                            int count = CommonUtil.convertObjToInt(multipletoSingleMap.get("TOTAL_COUNT"));
                            totalCount = CommonUtil.convertObjToDouble(multipletoSingleMap.get("TOTAL_COUNT")).doubleValue();
                            List lst = sqlMap.executeQueryForList("getMobileNoinSMSSubscriptionTable", multipletoSingleMap);
                            if (lst != null && lst.size() > 0) {
                                multipletoSingleMap = (HashMap) lst.get(0);
//                                multipletoSingleMap.put("CUST_ID", CommonUtil.convertObjToStr(depMap.get("ACT_NUM")));
                                multipletoSingleMap.put("CUST_ID", custId);
                                boolean OAActAvailable = false;
                                multipletoSingleMap.put("SMS_MODULE", "DepositInterestApplication");
                                if (map.containsKey("SB_BALANCE")) {
                                    interestSB.put("CUST_ID", custId);
                                    interestSB.put("PROD_TYPE", TransactionFactory.OPERATIVE);
                                    List lst2 = sqlMap.executeQueryForList("getSelectActBalance", interestSB);
                                    if (lst2 != null && lst2.size() > 0) {
                                        OAActAvailable = true;
                                        interestSB = (HashMap) lst2.get(0);
                                        multipletoSingleMap.put("OA_BALANCE", CommonUtil.convertObjToStr(interestSB.get("AVAILABLE_BALANCE")));
                                        multipletoSingleMap.put("PROD_TYPE", TransactionFactory.OPERATIVE);
                                    }
                                }
                                HashMap intMap = new HashMap();
                                intMap.put("CUST_ID", custId);
                                intMap.put("TRANS_DT", currDt.clone());
                                intMap.put("DEPOSIT_INTEREST_SCREEN", "DEPOSIT_INTEREST_SCREEN");
                                List DepList = null;                          
                                if(count>1){
                                    DepList = sqlMap.executeQueryForList("getMultipleCustomerIntAmt", intMap);
                                    if (DepList != null && DepList.size() > 0) {
                                        intMap = (HashMap) DepList.get(0);
                                        multipletoSingleMap.put("INT_AMT", CommonUtil.convertObjToStr(intMap.get("INT_AMT")));
                                    }  
                                    multipletoSingleMap.put("SMS_TEMPLATE", "MultipleTOSingleCusomerWiseMsg");
                                }else{
                                    DepList = sqlMap.executeQueryForList("getSingleCustomerIntAmt", intMap);
                                    if (DepList != null && DepList.size() > 0) {
                                        intMap = (HashMap) DepList.get(0);
                                        multipletoSingleMap.put("INT_AMT", CommonUtil.convertObjToStr(intMap.get("INT_AMT")));
                                    } 
                                    multipletoSingleMap.put("SMS_TEMPLATE", "SingleTOSingleCusomerWiseMsg");
                                }
                                if (map.containsKey("SB_BALANCE") && OAActAvailable) {
                                    multipletoSingleMap.put("SMS_TEMPLATE", "MultipleTOSingleCusomerWiseWithSBBalMsg");
                                }else{
                                    multipletoSingleMap.put("SMS_TEMPLATE", "SingleTOSingleCusomerWiseMsg");
                                }
                                multipletoSingleMap.put("PHONE_NUMBER", multipletoSingleMap.get("MOBILE_NO"));
                                multipletoSingleMap.put("TRANS_DT", getProperFormatDate(map.get("TRANS_DT")));
                                multipletoSingleMap.put("BRANCH_ID", map.get("BRANCH_CODE"));
                                System.out.println("multipletoSingleMap : " + multipletoSingleMap);
                                smsConfigDAO.MultipletoSingleMSGForCustomerWise(multipletoSingleMap);
                                multipletoSingleMap.put("CUST_ID", custId);
                                sqlMap.executeUpdate("updateMultipletoSingleMSGDeliver", multipletoSingleMap);
                            }
                        }
                    }
                } else if (map.containsKey("SMS_ACCOUNT_LIST") && map.containsKey("MDS_PRIZED_MONEY_DETAILS_SCREEN")) {
                    HashMap smsMap = new HashMap();
                    HashMap smsDataMap = new HashMap();
                    SmsConfigDAO smsConfigDAO = new SmsConfigDAO();
                    finalTableList = (List) map.get("SMS_ACCOUNT_LIST");
                    if (finalTableList != null && finalTableList.size() > 0) {
                        for (int i = 0; i < finalTableList.size(); i++) {
                            HashMap multipletoSingleMap = (HashMap) finalTableList.get(i);
                            multipletoSingleMap.put("SCHEME_NAME", map.get("SCHEME_NAME"));
                            multipletoSingleMap.put("DRAW_AUCTION_DATE", getProperFormatDate(map.get("TRANS_DT")));
//                            System.out.println("multipletoSingleMap : "+multipletoSingleMap);
                            List smsList = sqlMap.executeQueryForList("getDetailsForPrizedDetailsSMS", multipletoSingleMap);
                            if (smsList != null && smsList.size() > 0) {
                                for (int j = 0; j < smsList.size(); j++) {
                                    smsMap = (HashMap) smsList.get(j);
                                    String chittalNo = CommonUtil.convertObjToStr(smsMap.get("CHITTAL_NO"));
                                    smsDataMap.put(chittalNo, smsMap);
                                }
                                System.out.println("smsMap#%#%#%#%#%" + smsMap);
                                if (smsDataMap.size() > 0) {
                                    HashMap smsData = new HashMap();
                                    smsData.put("SMS", smsDataMap);
                                    smsData.put("MDS_PRIZED_REMIDER", "");
                                    smsData.put(CommonConstants.BRANCH_ID, _branchCode);
                                    smsConfigDAO.MdsSmsConfiguration(smsData);
                                }
                            }
                        }
                    }
                }else if (map.containsKey("SMS_ACCOUNT_LIST") && map.containsKey("DEPOSIT_MULTIPLE_RENEWAL")) {
                    finalTableList = (List) map.get("SMS_ACCOUNT_LIST");
                    SmsConfigDAO smsConfigDAO = new SmsConfigDAO();
                    if (finalTableList != null && finalTableList.size() > 0) {
                        HashMap multipletoSingleMap = new HashMap();
                        HashMap SingleMap = new HashMap();
                        HashMap depMap = new HashMap();
                        HashMap intMap = new HashMap();
                        double totalCount = 0;
                        for (int i = 0; i < finalTableList.size(); i++) {
                            multipletoSingleMap = (HashMap) finalTableList.get(i);
                            System.out.println("multipletoSingleMap : " + multipletoSingleMap);
                            String custId = CommonUtil.convertObjToStr(multipletoSingleMap.get("CUST_ID"));
                            totalCount = CommonUtil.convertObjToDouble(multipletoSingleMap.get("TOTAL_COUNT")).doubleValue();
                            List lst = sqlMap.executeQueryForList("getMobileNoinSMSSubscriptionTable", multipletoSingleMap);
                            if (lst != null && lst.size() > 0) {
                                multipletoSingleMap = (HashMap) lst.get(0);
                                multipletoSingleMap.put("CUST_ID", custId);
                                multipletoSingleMap.put("SMS_MODULE", "DepositMultipleRenewal");
                                if (map.containsKey("SB_BALANCE")) {
                                    SingleMap.put("CUST_ID", custId);
                                    SingleMap.put("PROD_TYPE", TransactionFactory.OPERATIVE);
                                    List lst1 = sqlMap.executeQueryForList("getSelectActBalance", SingleMap);
                                    if (lst1 != null && lst1.size() > 0) {
                                        SingleMap = (HashMap) lst1.get(0);
                                        multipletoSingleMap.put("OA_BALANCE", CommonUtil.convertObjToStr(SingleMap.get("AVAILABLE_BALANCE")));
                                        multipletoSingleMap.put("PROD_TYPE", TransactionFactory.OPERATIVE);                                        
                                    }
                                }
                                intMap = new HashMap();
                                intMap.put("CUST_ID", custId);
                                intMap.put("TRANS_DT", currDt.clone());
                                intMap.put("DEPOSIT_MULTIPLE_RENEWAL", "DEPOSIT_MULTIPLE_RENEWAL");
                                List DepList = null;
                                if(totalCount == 1){
                                    DepList = sqlMap.executeQueryForList("getSingleCustomerIntAmt", intMap);
                                    if (DepList != null && DepList.size() > 0) {
                                        intMap = (HashMap) DepList.get(0);
                                        multipletoSingleMap.put("INT_AMT", CommonUtil.convertObjToStr(intMap.get("INT_AMT")));
                                    }else{
                                        multipletoSingleMap.put("INT_AMT", "");
                                    }     
                                    multipletoSingleMap.put("SMS_TEMPLATE", "SingleTOSingleCusomerWiseRenewalMsg");
                                }else{
                                    DepList = sqlMap.executeQueryForList("getMultipleCustomerIntAmt", intMap);
                                    if (DepList != null && DepList.size() > 0) {
                                        intMap = (HashMap) DepList.get(0);
                                        multipletoSingleMap.put("INT_AMT", CommonUtil.convertObjToStr(intMap.get("INT_AMT")));
                                    }else{
                                        multipletoSingleMap.put("INT_AMT", "");
                                    }
                                    multipletoSingleMap.put("SMS_TEMPLATE", "MultipleTOSingleCusomerWiseRenewalMsg");
                                }
                                if (map.containsKey("SB_BALANCE")) {
                                    multipletoSingleMap.put("SMS_TEMPLATE", "MultipleTOSingleCusomerWiseRenewalWithSBBalMsg");
                                }
                                multipletoSingleMap.put("PHONE_NUMBER", multipletoSingleMap.get("MOBILE_NO"));
                                multipletoSingleMap.put("TRANS_DT", getProperFormatDate(map.get("TRANS_DT")));
                                multipletoSingleMap.put("BRANCH_ID", map.get("BRANCH_CODE"));
                                multipletoSingleMap.put("CUST_ID", custId);
                                //System.out.println("multipletoSingleMap : " + multipletoSingleMap);
                                smsConfigDAO.MultipletoSingleMSGForCustomerWise(multipletoSingleMap);
                                sqlMap.executeUpdate("updateMultipletoSingleMSGDeliver", multipletoSingleMap);
                            }
                        }
                    }
                }
            } catch (Exception e) {
                sqlMap.rollbackTransaction();
                e.printStackTrace();
                throw e;
            }
            destroyObjects();
        }
        return (HashMap) returnMap;
    }

    public HashMap executeQuery(HashMap obj) throws Exception {
        if(obj.containsKey("BRANCH_ID")){
            obj.put(CommonConstants.BRANCH_ID,obj.get("BRANCH_ID"));
        }
        System.out.println("DepositInterestApplicationDAO executeQuery "+obj);
        _branchCode = (String) obj.get(CommonConstants.BRANCH_ID);
        currDt = ServerUtil.getCurrentDate(_branchCode);
        HashMap returnMap = new HashMap();
        DepositIntTask task = new DepositIntTask(obj);
       returnMap.put("INTEREST_DATA", task.getInterestTableDataKSSB("SELECT"));
     //  returnMap.put("INTEREST_DATA", task.getInterestTableData("SELECT"));
        returnMap.put("RD_DATA", task.getAccountsList());
       
        return returnMap;
    }

    private void destroyObjects() {
//        standingLst = null;
    }
    
    public Date getProperFormatDate(Object obj) {
        Date curDt = null;
        // currDt = properFormatDate;
        if (obj != null && obj.toString().length() > 0) {
            Date tempDt = DateUtil.getDateMMDDYYYY(CommonUtil.convertObjToStr(obj));
            curDt = (Date) currDt.clone();
            curDt.setDate(tempDt.getDate());
            curDt.setMonth(tempDt.getMonth());
            curDt.setYear(tempDt.getYear());
        }
        return curDt;
    }
}