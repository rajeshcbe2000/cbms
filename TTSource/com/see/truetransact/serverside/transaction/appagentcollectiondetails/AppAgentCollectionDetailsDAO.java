/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition.
 * 
 *
 * AppAgentCollectionDetailsDAO.java
 * 
 * Created on Wed Feb 02 13:11:28 IST 2005
 */
package com.see.truetransact.serverside.transaction.appagentcollectiondetails;

import com.see.truetransact.serverside.payroll.arrear.*;

import com.see.truetransact.serverside.generalledger.GLOpeningUpdate.*;
import com.see.truetransact.serverside.supporting.balanceupdate.*;

import java.util.List;
import java.util.HashMap;
import java.util.ArrayList;
import com.ibatis.db.sqlmap.SqlMap;
import com.see.truetransact.commonutil.CommonConstants;
import com.see.truetransact.commonutil.CommonUtil;
import com.see.truetransact.commonutil.DateUtil;
import com.see.truetransact.commonutil.TTException;
import com.see.truetransact.serverside.TTDAO;
import com.see.truetransact.servicelocator.ServiceLocator;
import com.see.truetransact.serverexception.TransRollbackException;
import com.see.truetransact.serverexception.ServiceLocatorException;
import com.see.truetransact.serverside.common.transaction.TransactionDAO;
import com.see.truetransact.serverside.common.log.LogDAO;
import com.see.truetransact.serverside.common.transaction.TransferTrans;
import com.see.truetransact.serverside.transaction.common.TransactionFactory;
import com.see.truetransact.serverside.transaction.transfer.TransferDAO;
import com.see.truetransact.serverutil.ServerUtil;
import com.see.truetransact.transferobject.common.log.LogTO;
import com.see.truetransact.transferobject.supporting.balanceupdate.BalanceUpdateTO;
import com.see.truetransact.transferobject.generalledger.GLOpeningUpdateTO;
import com.see.truetransact.transferobject.payroll.arrear.ArrearTO;
import com.see.truetransact.transferobject.transaction.dailyDepositTrans.DailyAccountTransTO;
//import com.see.truetransact.serverside.transaction.transfer.TransferDAO;
import java.util.Date;
import com.see.truetransact.serverside.batchprocess.task.TaskHeader;
import com.see.truetransact.serverside.batchprocess.task.authorizechk.InterestCalculationTask;
import com.see.truetransact.transferobject.transaction.dailyDepositTrans.DailyDepositTransTO;
import com.see.truetransact.transferobject.transaction.transfer.TxTransferTO;

public class AppAgentCollectionDetailsDAO extends TTDAO {

    private static SqlMap sqlMap = null;
    private LogTO logTO;
    private LogDAO logDAO;
    private BalanceUpdate objBalanceUpdateTO;
    private String userID = "";
    private String branchCode = "";
    private ArrayList selectedArrayList;
    private ArrayList deletedArrayList;
    private int totalCount = 0;
    private BalanceUpdateTO objTO;
    BalanceUpdateTO objTO1 = new BalanceUpdateTO();
    Date currDt = null;
    int key = 1;
    private HashMap returnDataMap = new HashMap();
    TransactionDAO transactionDAO = null;

    public AppAgentCollectionDetailsDAO() throws ServiceLocatorException {
        ServiceLocator locate = ServiceLocator.getInstance();
        sqlMap = (SqlMap) locate.getDAOSqlMap();
    }

    private Date setProperDtFormat(Date dt) {
        Date tempDt = (Date) currDt.clone();
        if (dt != null) {
            tempDt.setDate(dt.getDate());
            tempDt.setMonth(dt.getMonth());
            tempDt.setYear(dt.getYear());
            return tempDt;
        }
        return null;
    }

    public Date getProperDateFormat(Object obj) {
        Date currDate = null;
        if (obj != null && obj.toString().length() > 0) {
            Date tempDt = DateUtil.getDateMMDDYYYY(CommonUtil.convertObjToStr(obj));
            currDate = (Date) currDt.clone();
            currDate.setDate(tempDt.getDate());
            currDate.setMonth(tempDt.getMonth());
            currDate.setYear(tempDt.getYear());
        }
        return currDate;
    }

    private void getTransDetails(String batchId) throws Exception {
        HashMap getTransMap = new HashMap();
        getTransMap.put("BATCH_ID", batchId);
        getTransMap.put("TRANS_DT", currDt);
        getTransMap.put(CommonConstants.BRANCH_ID, _branchCode);
        returnDataMap = new HashMap();
        List transList = (List) sqlMap.executeQueryForList("getTransferDetailsPayRoll", getTransMap);
        if (transList != null && transList.size() > 0) {
            returnDataMap.put("TRANSFER_TRANS_LIST", transList);
        }
        List cashList = (List) sqlMap.executeQueryForList("getCashDetailsPayRoll", getTransMap);
        if (cashList != null && cashList.size() > 0) {
            returnDataMap.put("CASH_TRANS_LIST", cashList);
        }
        getTransMap.clear();
        getTransMap = null;
        transList = null;
        cashList = null;
    }

    public void authorizeTransaction(HashMap returnDataMap) throws Exception {
        try {
            if (returnDataMap != null && returnDataMap.get("TRANS_ID") != null && !returnDataMap.get("TRANS_ID").equals("")) {
                System.out.println("TRANSFER TRANS_ID :" + returnDataMap.get("TRANS_ID"));
                String authorizeStatus = CommonUtil.convertObjToStr(CommonConstants.STATUS_AUTHORIZED);
                String linkBatchId = CommonUtil.convertObjToStr(returnDataMap.get("TRANS_ID"));
                HashMap cashAuthMap = new HashMap();
                cashAuthMap.put(CommonConstants.BRANCH_ID, _branchCode);
                cashAuthMap.put(CommonConstants.USER_ID, "admin");
                TransactionDAO transactionDAO = new TransactionDAO(CommonConstants.DEBIT);
                TransactionDAO.authorizeCashAndTransferWithoutRemitIssueDet(linkBatchId, authorizeStatus, cashAuthMap);
                cashAuthMap = null;
                //transMap = null;
            }
        } catch (Exception e) {
            sqlMap.rollbackTransaction();
            e.printStackTrace();
            throw e;
        }
    }

    public static void main(String str[]) {
        try {
            AppAgentCollectionDetailsDAO dao = new AppAgentCollectionDetailsDAO();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public HashMap execute(HashMap map) throws Exception {
        System.out.println("AppAgentCollectionDetailsDAO Execute Method : " + map);
        HashMap returnMap = new HashMap();
        try {
            _branchCode = (String) map.get(CommonConstants.BRANCH_ID);
            currDt = ServerUtil.getCurrentDate(_branchCode);
            final String command = CommonUtil.convertObjToStr(map.get("COMMAND"));
            sqlMap.startTransaction();
            if (command.equals(CommonConstants.TOSTATUS_INSERT)) {
                if (map.containsKey("TxTransferTO")) {
                    HashMap applicationMap = new HashMap();
                    applicationMap.put("BRANCH_CODE", map.get("BRANCH_CODE"));
                    applicationMap.put("USER_ID", CommonUtil.convertObjToStr(map.get(CommonConstants.USER_ID)));
                    TaskHeader header = new TaskHeader();
                    header.setBranchID(_branchCode);
                    TransferDAO transferDAO = new TransferDAO();
                    ArrayList applncashList = (ArrayList) map.get("TxTransferTO");
                    HashMap totalLoanMap = new HashMap();
                    if (applncashList != null && applncashList.size() > 0) {
                        for (int i = 0; i < applncashList.size(); i++) {
                            ArrayList newList = new ArrayList();
                            TxTransferTO objTxTransferTO = (TxTransferTO) applncashList.get(i);
                            System.out.println("applncashList   " + objTxTransferTO);
                            newList.add(objTxTransferTO);
                            if (objTxTransferTO != null && objTxTransferTO.getProdType().equals(TransactionFactory.LOANS)) {
                                HashMap singleLoanMap = interestCalculationTLAD(objTxTransferTO.getActNum(), objTxTransferTO.getProdId());
                                System.out.println("singleLoanMap : " + singleLoanMap);
                                totalLoanMap.put(objTxTransferTO.getActNum(), singleLoanMap);
                                System.out.println("totalLoanMap : " + totalLoanMap);
                                applicationMap.put("MULTIPLE_ALL_AMOUNT", totalLoanMap);
                            }
                        }
                        applicationMap.put("TxTransferTO", applncashList);
                        applicationMap.put("COMMAND", CommonConstants.TOSTATUS_INSERT);
                        applicationMap.put("INITIATED_BRANCH", _branchCode);
                        applicationMap.put(CommonConstants.BRANCH_ID, _branchCode);
                        applicationMap.put(CommonConstants.USER_ID, CommonUtil.convertObjToStr(map.get(CommonConstants.USER_ID)));
                        applicationMap.put(CommonConstants.MODULE, "Transaction");
                        applicationMap.put(CommonConstants.SCREEN, "");
                        applicationMap.put("MODE", "MODE");
                        HashMap transferMap = transferDAO.execute(applicationMap, false);
                        System.out.println("AppAgentCollectionDetailsDAO transferMap : " + transferMap);
//                        String cash_transid = CommonUtil.convertObjToStr(transferMap.get("TRANS_ID"));
//                        returnMap.put("CASH_TRANS_ID", cash_transid);

                        HashMap resultMap = new HashMap();
                        for (int i = 0; i < applncashList.size(); i++) {
                            ArrayList newList = new ArrayList();
                            TxTransferTO objTxTransferTO = (TxTransferTO) applncashList.get(i);
                            System.out.println("AppAgentCollectionDetailsDAO objTxTransferTO : " + objTxTransferTO);
                            if (objTxTransferTO.getInpCurr() != null && objTxTransferTO.getInpCurr().length() > 0) {
                                if (objTxTransferTO.getActNum() != null && objTxTransferTO.getActNum().length() > 0) {
                                    resultMap.put("ACT_NUM", objTxTransferTO.getActNum());
                                } else {
                                    resultMap.put("ACT_NUM", objTxTransferTO.getGlTransActNum());
                                }
                                if (objTxTransferTO.getProdType() != null && objTxTransferTO.getProdType().equals("TD")) {
                                    String deposit_No = CommonUtil.convertObjToStr(objTxTransferTO.getActNum());
                                    deposit_No = deposit_No.substring(0, deposit_No.lastIndexOf("_"));
                                    resultMap.put("ACT_NUM", deposit_No);
                                }
                                resultMap.put("BATCH_ID", transferMap.get("TRANS_ID"));
                                resultMap.put("APP_ID", objTxTransferTO.getInpCurr());
                                resultMap.put("TRANS_DT", objTxTransferTO.getTransDt());
                                resultMap.put("VALUE_DT", objTxTransferTO.getInstDt());
                                resultMap.put("USER_ID", CommonUtil.convertObjToStr(map.get(CommonConstants.USER_ID)));
                                sqlMap.executeUpdate("updateAppAgentCollectionDate", resultMap);
                                SetDailydepositTrans(objTxTransferTO, CommonUtil.convertObjToStr(map.get("COLLECTING_AGENT_ID")), CommonUtil.convertObjToStr(transferMap.get("TRANS_ID")));
                            }
                        }
                        returnDataMap.put("BATCH_ID", transferMap.get("TRANS_ID"));
                    }
                }
            }
            if (map.containsKey(CommonConstants.AUTHORIZEMAP)) {
                returnDataMap = new HashMap();
                HashMap authorizeMap = new HashMap();
                HashMap authMap = (HashMap) map.get(CommonConstants.AUTHORIZEMAP);
                String status = (String) authMap.get(CommonConstants.STATUS);
                String status1 = (String) map.get(CommonConstants.STATUS);
                String linkBatchId = CommonUtil.convertObjToStr(map.get("BATCH_ID"));
                map.put("AGENT_COLLECTION_DETAILS_DAO", "AGENT_COLLECTION_DETAILS_DAO");
                map.put("AGENT_BATCH_ID", linkBatchId);
                map.put("TRANS_DT", currDt.clone());
                map.put("INITIATED_BRANCH", _branchCode);
                TransactionDAO transactionDAO = new TransactionDAO(CommonConstants.DEBIT);
                transactionDAO = new TransactionDAO(CommonConstants.DEBIT);
                System.out.println("authMap : " + authMap + "authorize map : " + map + "linkBatchId : " + linkBatchId + "status : " + status + "status1 : " + status1);
                TransactionDAO.authorizeCashAndTransfer(linkBatchId, status, map);
                HashMap resultMap = new HashMap();
                ArrayList applncashList = (ArrayList) map.get("AUTHORIZEDATA");
                for (int i = 0; i < applncashList.size(); i++) {
                    ArrayList newList = new ArrayList();
                    TxTransferTO objTxTransferTO = (TxTransferTO) applncashList.get(i);
                    System.out.println("objTxTransferTO : " + objTxTransferTO);
                    if (objTxTransferTO.getInpCurr() != null && objTxTransferTO.getInpCurr().length() > 0) {
                        if (objTxTransferTO.getActNum() != null && objTxTransferTO.getActNum().length() > 0) {
                            resultMap.put("ACT_NUM", objTxTransferTO.getActNum());
                        } else {
                            resultMap.put("ACT_NUM", objTxTransferTO.getGlTransActNum());
                        }
                        if (objTxTransferTO.getProdType() != null && objTxTransferTO.getProdType().equals("TD")) {
                            String deposit_No = CommonUtil.convertObjToStr(objTxTransferTO.getActNum());
                            deposit_No = deposit_No.substring(0, deposit_No.lastIndexOf("_"));
                            System.out.println("deposit_No : " + deposit_No);
                            resultMap.put("ACT_NUM", deposit_No);
                        }
                        resultMap.put("BRANCH_ID", objTxTransferTO.getInitiatedBranch());
                        resultMap.put("TRANS_DT", objTxTransferTO.getTransDt());
                        resultMap.put("APP_ID", objTxTransferTO.getInpCurr());
                        resultMap.put("VALUE_DT", objTxTransferTO.getInstDt());
                        resultMap.put("AUTHORIZE_STATUS", status);
                        resultMap.put("AUTHORIZE_BY", CommonUtil.convertObjToStr(map.get(CommonConstants.USER_ID)));
                        resultMap.put("BATCH_ID", linkBatchId);
                        if (status.equals("REJECTED")) { //Added By Kannan AR ref. sathya reject entry should list again for transaction
                            resultMap.put("BRANCH_ID", objTxTransferTO.getBranchId());
                            sqlMap.executeUpdate("updateAppAgentReject", resultMap);
                        } else {
                            resultMap.put("BRANCH_ID", objTxTransferTO.getBranchId());
                            sqlMap.executeUpdate("updateAppAgentTxnFlag", resultMap);
                        }
                        sqlMap.executeUpdate("updateAppAgentTxnNull", resultMap);
                        if (objTxTransferTO.getProdType() != null && objTxTransferTO.getProdType().equals("TD")) {
                            resultMap.put("ACT_NUM", objTxTransferTO.getActNum());
                        }
                        sqlMap.executeUpdate("updateAppAgentAuthorizeStatus", resultMap);
                    }
                }
            }
            sqlMap.commitTransaction();
        } catch (Exception e) {
            e.printStackTrace();
            sqlMap.rollbackTransaction();
            if (e instanceof TTException) {
                throw e;
            } else {
                throw new TTException(e);
            }
    }
    return returnDataMap ;
}

public HashMap executeQuery(HashMap obj) throws Exception {
        _branchCode = (String) obj.get(CommonConstants.BRANCH_ID);
        currDt = ServerUtil.getCurrentDate(_branchCode);
        return getData(obj);
    }

    private HashMap getData(HashMap map) throws Exception {
        HashMap returnMap = new HashMap();
        List lstData = ServerUtil.executeQuery("getSelectCashTransactionTODAILY", map);
        DailyAccountTransTO dailyTo;
        if (lstData != null && lstData.size() > 0) {
            returnMap.put("DAILY_TRANS_DATA", lstData);
        }
        return returnMap;
    }
    
    private HashMap interestCalculationTLAD(String accountNo, String prodID) {
        HashMap map = new HashMap();
        HashMap insertPenal = new HashMap();
        HashMap hash = null;
        try {
            map.put("ACT_NUM", accountNo);
            map.put("PROD_ID", prodID);
            List lst = sqlMap.executeQueryForList("IntCalculationDetail", map);
            if (lst != null && lst.size() > 0) {
                hash = (HashMap) lst.get(0);
                if (hash.get("AS_CUSTOMER_COMES") != null && hash.get("AS_CUSTOMER_COMES").equals("N")) {
                    hash = new HashMap();
                    return hash;
                }
                map.put("BRANCH_ID", _branchCode);
                map.put(CommonConstants.BRANCH_ID, _branchCode);
                map.putAll(hash);
                map.put("LOAN_ACCOUNT_CLOSING", "LOAN_ACCOUNT_CLOSING");
                map.put("CURR_DATE", currDt.clone());
                TaskHeader header = new TaskHeader();
                header.setBranchID(_branchCode);
                InterestCalculationTask interestcalTask = new InterestCalculationTask(header);
                hash = interestcalTask.interestCalcTermLoanAD(map);
                if (hash == null) {
                    hash = new HashMap();
                } else if (hash != null && hash.size() > 0) {
                    double interest = CommonUtil.convertObjToDouble(hash.get("INTEREST")).doubleValue();
                    double penal = CommonUtil.convertObjToDouble(hash.get("LOAN_CLOSING_PENAL_INT")).doubleValue();
                    hash.put("ACT_NUM", accountNo);
                    hash.put("FROM_DT", hash.get("LAST_INT_CALC_DT"));
                    hash.put("FROM_DT", DateUtil.addDays(((Date) hash.get("FROM_DT")), 2));
                    hash.put("TO_DATE", map.get("CURR_DATE"));
                    List facilitylst = sqlMap.executeQueryForList("getPaidPrinciple", hash);
                    if (facilitylst != null && facilitylst.size() > 0) {
                        hash = (HashMap) facilitylst.get(0);
                        interest -= CommonUtil.convertObjToDouble(hash.get("INTEREST")).doubleValue();
                        penal -= CommonUtil.convertObjToDouble(hash.get("PENAL")).doubleValue();
                    }
                    if (interest > 0) {
                        insertPenal.put("CURR_MONTH_INT", new Double(interest));
                    } else {
                        insertPenal.put("CURR_MONTH_INT", new Double(0));
                    }
                    if (penal > 0) {
                        insertPenal.put("PENAL_INT", new Double(penal));
                    } else {
                        insertPenal.put("PENAL_INT", new Double(0));
                    }
                    List chargeList = sqlMap.executeQueryForList("getChargeDetails", map);
                    if (chargeList != null && chargeList.size() > 0) {
                        for (int i = 0; i < chargeList.size(); i++) {
                            HashMap chargeMap = (HashMap) chargeList.get(i);
                            double chargeAmt = CommonUtil.convertObjToDouble(chargeMap.get("CHARGE_AMT")).doubleValue();
                            if (chargeMap.get("CHARGE_TYPE").equals("POSTAGE CHARGES") && chargeAmt > 0) {
                                insertPenal.put("POSTAGE CHARGES", chargeMap.get("CHARGE_AMT"));
                            }
                            if (chargeMap.get("CHARGE_TYPE").equals("MISCELLANEOUS CHARGES") && chargeAmt > 0) {
                                insertPenal.put("MISCELLANEOUS CHARGES", chargeMap.get("CHARGE_AMT"));
                            }
                            if (chargeMap.get("CHARGE_TYPE").equals("LEGAL CHARGES") && chargeAmt > 0) {
                                insertPenal.put("LEGAL CHARGES", chargeMap.get("CHARGE_AMT"));
                            }
                            if (chargeMap.get("CHARGE_TYPE").equals("INSURANCE CHARGES") && chargeAmt > 0) {
                                insertPenal.put("INSURANCE CHARGES", chargeMap.get("CHARGE_AMT"));
                            }
                            if (chargeMap.get("CHARGE_TYPE").equals("EXECUTION DECREE CHARGES") && chargeAmt > 0) {
                                insertPenal.put("EXECUTION DECREE CHARGES", chargeMap.get("CHARGE_AMT"));
                            }
                            if (chargeMap.get("CHARGE_TYPE").equals("ARBITRARY CHARGES") && chargeAmt > 0) {
                                insertPenal.put("ARBITRARY CHARGES", chargeMap.get("CHARGE_AMT"));
                            }
                            if (chargeMap.get("CHARGE_TYPE").equals("ADVERTISE CHARGES") && chargeAmt > 0) {
                                insertPenal.put("ADVERTISE CHARGES", chargeMap.get("CHARGE_AMT"));
                            }
                            chargeMap = null;
                        }
                    }
                    chargeList = null;
                }
                interestcalTask = null;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        map = null;
        hash = null;
        return insertPenal;
    }    
    
    public void SetDailydepositTrans(TxTransferTO objTxTransferTO,String collectingAgentId,String batchId) {
        DailyDepositTransTO dailyDepositTransTO = new DailyDepositTransTO();
        try{            
            dailyDepositTransTO.setTrans_id(objTxTransferTO.getInpCurr());
            dailyDepositTransTO.setBatch_id(CommonUtil.convertObjToStr(batchId));        
            dailyDepositTransTO.setTrn_dt((Date)currDt.clone());        
            dailyDepositTransTO.setColl_dt((Date)currDt.clone());        
            dailyDepositTransTO.setAgent_no(CommonUtil.convertObjToStr(collectingAgentId));        
            if(objTxTransferTO.getProdType() != null && objTxTransferTO.getProdType().length()>0 && objTxTransferTO.getProdType().equals("TD")){
//                String deposit_No = CommonUtil.convertObjToStr(objTxTransferTO.getActNum());
//                deposit_No = deposit_No.substring(0, deposit_No.lastIndexOf("_"));
                dailyDepositTransTO.setAcct_num(objTxTransferTO.getActNum());        
            }else{
                if(objTxTransferTO.getActNum() != null && objTxTransferTO.getActNum().length()>0){
                    dailyDepositTransTO.setAcct_num(CommonUtil.convertObjToStr(objTxTransferTO.getActNum()));        
                }else{
                    dailyDepositTransTO.setAcct_num(CommonUtil.convertObjToStr(objTxTransferTO.getGlTransActNum()));        
                }                
            }
            dailyDepositTransTO.setTrans_mode(CommonConstants.TX_TRANSFER);
            dailyDepositTransTO.setTrans_type(objTxTransferTO.getTransType());
            dailyDepositTransTO.setAmount(CommonUtil.convertObjToDouble(objTxTransferTO.getAmount()));
            dailyDepositTransTO.setParticulars(objTxTransferTO.getInpCurr());
            dailyDepositTransTO.setCreated_by(objTxTransferTO.getStatusBy());
            dailyDepositTransTO.setCreated_dt((Date)currDt.clone());
            dailyDepositTransTO.setInitiatedBranch(CommonUtil.convertObjToStr(objTxTransferTO.getInitiatedBranch()));
            dailyDepositTransTO.setProd_Type(CommonUtil.convertObjToStr(objTxTransferTO.getProdType()));
            dailyDepositTransTO.setStatus(CommonConstants.STATUS_CREATED);
            dailyDepositTransTO.setScreenName("MOBILEAPP");
            sqlMap.executeUpdate("INSERTINTODAILYDEPOSIT", dailyDepositTransTO);
        }catch(Exception e){
            e.printStackTrace();
        }
    }
    
}
