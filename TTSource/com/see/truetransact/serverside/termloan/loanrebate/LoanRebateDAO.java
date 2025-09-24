/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition.
 **
 *
 * AgentDAO.java
 *
 * Created on Wed Feb 02 13:11:28 IST 2005
 */
package com.see.truetransact.serverside.termloan.loanrebate;

import java.util.List;
import java.util.HashMap;
import java.util.ArrayList;
import java.util.Date;

import com.ibatis.db.sqlmap.SqlMap;
import com.see.truetransact.serverutil.ServerUtil;
import com.see.truetransact.commonutil.CommonConstants;
import com.see.truetransact.commonutil.CommonUtil;
import com.see.truetransact.commonutil.DateUtil;
import com.see.truetransact.serverside.TTDAO;
import com.see.truetransact.servicelocator.ServiceLocator;
import com.see.truetransact.serverexception.ServiceLocatorException;
import com.see.truetransact.transferobject.termloan.chargesTo.TermLoanChargesTO;
import com.see.truetransact.serverside.batchprocess.task.TaskHeader;
import com.see.truetransact.serverside.batchprocess.task.authorizechk.InterestCalculationTask;
import com.see.truetransact.serverside.common.transaction.TransferTrans;
import com.see.truetransact.transferobject.transaction.transfer.TxTransferTO;
import com.see.truetransact.serverside.common.transaction.TransactionDAO;
import com.see.truetransact.transferobject.termloan.loanrebate.LoanRebateTO;
import com.see.truetransact.serverside.transaction.common.TransactionFactory;
import com.see.truetransact.serverside.common.idgenerate.IDGenerateDAO;
import com.see.truetransact.transferobject.suspenseaccount.SuspenseAccountMasterTO;
import com.see.truetransact.transferobject.suspenseaccount.SuspenseAccountProductTO;
import java.util.Arrays;

//import com.see.truetransact.commonutil.TTException;
//import com.see.truetransact.serverutil.ServerUtil;
//import com.see.truetransact.transferobject.agent.AgentTO;
//import com.see.truetransact.commonutil.NoCommandException;
//import com.see.truetransact.serverutil.ServerConstants;
//import com.see.truetransact.serverexception.TransRollbackException;
//import com.see.truetransact.transferobject.agent.AgentCommisonTO;
//import com.see.truetransact.serverside.transaction.transfer.TransferDAO;
//import com.see.truetransact.serverside.common.log.LogDAO;
//import com.see.truetransact.transferobject.common.log.LogTO;
/**
 * Agent DAO.
 *
 */
public class LoanRebateDAO extends TTDAO {

    private static SqlMap sqlMap = null;
    private double paid_interest = 0;
    private double paid_penal_int = 0;
    private double paid_principal = 0;
    private String id = "";
    private TransactionDAO transactionDAO;
    private HashMap prodMap = new HashMap();
    private Date currDt = null;
    private String user = "";

    /**
     * Creates a new instance of AgentDAO
     */
    public LoanRebateDAO() throws ServiceLocatorException {
        ServiceLocator locate = ServiceLocator.getInstance();
        sqlMap = (SqlMap) locate.getDAOSqlMap();
    }

    public static void main(String str[]) {
        try {
            LoanRebateDAO dao = new LoanRebateDAO();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public HashMap execute(HashMap map) throws Exception {
        System.out.println("LoanRebate Map Dao : " + map);

        _branchCode = (String) map.get(CommonConstants.BRANCH_ID);
        currDt = ServerUtil.getCurrentDate(_branchCode);
        user = CommonUtil.convertObjToStr(map.get(CommonConstants.USER_ID));
        try {
            sqlMap.startTransaction();
            actionPerform(map);
            sqlMap.commitTransaction();
        } catch (Exception e) {
            e.printStackTrace();
            sqlMap.rollbackTransaction();
        }
        ServiceLocator.flushCache(sqlMap);
        System.gc();
        System.out.println("id is@@@@@" + id);
        map.put("REBATEID", id);
        System.out.println("map is@@@@@" + map);
        return (map);
    }

    private void interestRebateTransaction(LoanRebateTO loanRebateTO) {
        HashMap dataMap = new HashMap();
        HashMap rebateMap = new HashMap();
        HashMap suspenseMap = new HashMap();
        try {
            //        List waiveOffList=(List)sqlMap.executeQueryForList("getSelectTermLoanWaiveOffTO",loanRebateTO.getRebateId());
            //        if(waiveOffList !=null && waiveOffList.size()>0){
            //            TermLoanPenalWaiveOffTO obj =new TermLoanPenalWaiveOffTO();
            //            obj =(TermLoanPenalWaiveOffTO)waiveOffList.get(0);
            dataMap.put("ACCT_NUM", loanRebateTO.getAccNo());
            List lst = (List) sqlMap.executeQueryForList("getInterestAndPenalIntActHeadForRebate", dataMap);
            List lst1 = (List) sqlMap.executeQueryForList("getProdType", dataMap);
            List rebateList = (List) sqlMap.executeQueryForList("getLoanRebateSettings", dataMap);
            if (lst != null && lst.size() > 0) {
                HashMap acHeads = (HashMap) lst.get(0);
                HashMap prodTypeMap = (HashMap) lst1.get(0);
                System.out.println("#####loanRebateTO.getNormalInterest()" + loanRebateTO.getNormalInterest());
                System.out.println("#####loanRebateTO.getIntAmount()" + loanRebateTO.getIntAmount());
                double normalint = CommonUtil.convertObjToDouble(loanRebateTO.getNormalInterest()).doubleValue();
                double rebateint = CommonUtil.convertObjToDouble(loanRebateTO.getIntAmount()).doubleValue();
                String prodtype = CommonUtil.convertObjToStr(prodTypeMap.get("PROD_TYPE"));
                String prodId = CommonUtil.convertObjToStr(prodTypeMap.get("PROD_ID"));
                HashMap txMap = new HashMap();

                txMap = new HashMap();
                ArrayList transferList = new ArrayList(); // for local transfer
                TransferTrans trans = new TransferTrans();
                trans.setTransMode(CommonConstants.TX_TRANSFER);
                if (rebateList != null && rebateList.size() > 0) {
                    rebateMap = (HashMap) rebateList.get(0);
                    dataMap.put("PROD_ID", rebateMap.get("SUSPENSE_PROD_ID"));
                    dataMap.put("BRANCH_ID", _branchCode);
                    //Debit
                    txMap.put(TransferTrans.DR_AC_HD, (String) acHeads.get("REBATE_INTEREST_ACHD"));
                    txMap.put(TransferTrans.DR_PROD_TYPE, TransactionFactory.GL);
                    txMap.put(TransferTrans.DR_BRANCH, _branchCode);
                    txMap.put(TransferTrans.PARTICULARS, "RebateInterest for " + loanRebateTO.getAccNo());
                    txMap.put("TRANS_MOD_TYPE", CommonConstants.TXN_PROD_TYPE_GL);
                    transferList.add(trans.getDebitTransferTO(txMap, CommonUtil.convertObjToDouble(loanRebateTO.getIntAmount()).doubleValue()));

                    //If credit is suspense
                    if (rebateMap.containsKey("SUSPENSE_TRANS") && rebateMap.get("SUSPENSE_TRANS").equals("Y")) {
                        suspenseMap = getSuspenseProduct(dataMap);
                        txMap.put(TransferTrans.CR_AC_HD, suspenseMap.get("AC_HD"));
                        txMap.put(TransferTrans.CR_ACT_NUM, suspenseMap.get("ACCT_NUM"));
                        txMap.put(TransferTrans.CR_PROD_ID, suspenseMap.get("PROD_ID"));
                        txMap.put(TransferTrans.CR_BRANCH, _branchCode);
                        txMap.put(TransferTrans.CURRENCY, "INR");
                        txMap.put(TransferTrans.CR_PROD_TYPE, TransactionFactory.SUSPENSE);
                        txMap.put(TransferTrans.PARTICULARS, "RebateInterest for " + loanRebateTO.getAccNo());
                        txMap.put("AUTHORIZEREMARKS", "INTEREST");
                        txMap.put("TRANS_MOD_TYPE", CommonConstants.TXN_PROD_TYPE_SUSPENSE);
                        trans.setInitiatedBranch(_branchCode);
                        trans.setLinkBatchId(CommonUtil.convertObjToStr(loanRebateTO.getAccNo()));
                        transferList.add(trans.getCreditTransferTO(txMap, CommonUtil.convertObjToDouble(loanRebateTO.getIntAmount()).doubleValue()));
                        trans.doDebitCredit(transferList, _branchCode, true);
                    } else {
                        //if closed loan credit to suspense
                        if (rebateMap.containsKey("CLOSED_SUSPENSE_TRANS") && rebateMap.get("CLOSED_SUSPENSE_TRANS").equals("Y") &&
                                checkClosedLoan(CommonUtil.convertObjToStr(loanRebateTO.getAccNo()))) {
                                suspenseMap = getSuspenseProduct(dataMap);
                            //if (checkClosedLoan(CommonUtil.convertObjToStr(loanRebateTO.getAccNo()))) {
                                txMap.put(TransferTrans.CR_AC_HD, suspenseMap.get("AC_HD"));
                                txMap.put(TransferTrans.CR_ACT_NUM, suspenseMap.get("ACCT_NUM"));
                                txMap.put(TransferTrans.CR_PROD_ID, suspenseMap.get("PROD_ID"));
                                txMap.put(TransferTrans.CR_BRANCH, _branchCode);
                                txMap.put(TransferTrans.CURRENCY, "INR");
                                txMap.put(TransferTrans.CR_PROD_TYPE, TransactionFactory.SUSPENSE);
                                txMap.put(TransferTrans.PARTICULARS, "RebateInterest for " + loanRebateTO.getAccNo());
                                txMap.put("AUTHORIZEREMARKS", "INTEREST");
                                txMap.put("TRANS_MOD_TYPE", CommonConstants.TXN_PROD_TYPE_SUSPENSE);
                                trans.setInitiatedBranch(_branchCode);
                                trans.setLinkBatchId(CommonUtil.convertObjToStr(loanRebateTO.getAccNo()));
                                transferList.add(trans.getCreditTransferTO(txMap, CommonUtil.convertObjToDouble(loanRebateTO.getIntAmount()).doubleValue()));
                                trans.doDebitCredit(transferList, _branchCode, true);
                           // }
                        } else {
                            //Credit to loan
                            if (normalint > 0.0) {
                                double remainAmount = rebateint - normalint;
                                System.out.println("@@@remainAmount" + remainAmount);
                                if (remainAmount > 0.0) {
                                    txMap.put(TransferTrans.CR_AC_HD, (String) acHeads.get("ACCT_HEAD"));
                                    txMap.put(TransferTrans.CR_ACT_NUM, loanRebateTO.getAccNo());
                                    txMap.put(TransferTrans.CR_PROD_ID, prodId);
                                    txMap.put(TransferTrans.CR_BRANCH, _branchCode);
                                    txMap.put(TransferTrans.CURRENCY, "INR");
                                    txMap.put(TransferTrans.CR_PROD_TYPE, prodtype);
                                    txMap.put(TransferTrans.PARTICULARS, "RebateInterest");
                                    txMap.put("TRANS_MOD_TYPE", CommonConstants.TXN_PROD_TYPE_LOANS);
                                    trans.setInitiatedBranch(_branchCode);
                                    trans.setLinkBatchId(CommonUtil.convertObjToStr(loanRebateTO.getAccNo()));

                                    //                transferList.add(trans.getCreditTransferTO(txMap,CommonUtil.convertObjToDouble(loanRebateTO.getIntAmount()).doubleValue()));
                                    transferList.add(trans.getCreditTransferTO(txMap, remainAmount));
                                    trans.setBreakLoanHierachy("Y");
                                    //trans.doDebitCredit(transferList, _branchCode, true);


                                    txMap.put(TransferTrans.CR_AC_HD, (String) acHeads.get("AC_DEBIT_INT"));
                                    txMap.put(TransferTrans.CR_BRANCH, _branchCode);
                                    txMap.put(TransferTrans.CURRENCY, "INR");
                                    txMap.put(TransferTrans.CR_PROD_TYPE, TransactionFactory.GL);
                                    txMap.put(TransferTrans.PARTICULARS, "RebateInterest for " + loanRebateTO.getAccNo());
                                    txMap.put("AUTHORIZEREMARKS", "INTEREST");
                                    txMap.put("TRANS_MOD_TYPE", CommonConstants.TXN_PROD_TYPE_LOANS);
                                    System.out.println("####### normalint  " + normalint);

                                    System.out.println("####### rebateint  " + rebateint);
                                    trans.setInitiatedBranch(_branchCode);
                                    trans.setLinkBatchId(CommonUtil.convertObjToStr(loanRebateTO.getAccNo()));

                                    //                transferList.add(trans.getCreditTransferTO(txMap,CommonUtil.convertObjToDouble(loanRebateTO.getIntAmount()).doubleValue()));
                                    transferList.add(trans.getCreditTransferTO(txMap, normalint));
                                    trans.doDebitCredit(transferList, _branchCode, true);
                                } else {
                                    txMap.put(TransferTrans.CR_AC_HD, (String) acHeads.get("AC_DEBIT_INT"));
                                    txMap.put(TransferTrans.CR_BRANCH, _branchCode);
                                    txMap.put(TransferTrans.CURRENCY, "INR");
                                    txMap.put(TransferTrans.CR_PROD_TYPE, TransactionFactory.GL);
                                    txMap.put(TransferTrans.PARTICULARS, "RebateInterest for " + loanRebateTO.getAccNo());
                                    txMap.put("AUTHORIZEREMARKS", "INTEREST");
                                    txMap.put("TRANS_MOD_TYPE", CommonConstants.TXN_PROD_TYPE_LOANS);
                                    System.out.println("####### normalint  " + normalint);

                                    System.out.println("####### rebateint  " + rebateint);
                                    trans.setInitiatedBranch(_branchCode);
                                    trans.setLinkBatchId(CommonUtil.convertObjToStr(loanRebateTO.getAccNo()));

                                    //                transferList.add(trans.getCreditTransferTO(txMap,CommonUtil.convertObjToDouble(loanRebateTO.getIntAmount()).doubleValue()));
                                    transferList.add(trans.getCreditTransferTO(txMap, rebateint));
                                    trans.doDebitCredit(transferList, _branchCode, true);
                                }
                            } else {
                                txMap.put(TransferTrans.CR_AC_HD, (String) acHeads.get("ACCT_HEAD"));
                                txMap.put(TransferTrans.CR_ACT_NUM, loanRebateTO.getAccNo());
                                txMap.put(TransferTrans.CR_PROD_ID, prodId);
                                txMap.put(TransferTrans.CR_BRANCH, _branchCode);
                                txMap.put(TransferTrans.CURRENCY, "INR");
                                //txMap.put(TransferTrans., "INR");
                                txMap.put(TransferTrans.CR_PROD_TYPE, prodtype);
                                txMap.put(TransferTrans.PARTICULARS, "RebateInterest ");
                                txMap.put("TRANS_MOD_TYPE", CommonConstants.TXN_PROD_TYPE_LOANS);
                                System.out.println("####### insertAccHead txMap " + txMap);

                                trans.setBreakLoanHierachy("Y");
                                trans.setInitiatedBranch(_branchCode);
                                trans.setLinkBatchId(CommonUtil.convertObjToStr(loanRebateTO.getAccNo()));

                                //                transferList.add(trans.getCreditTransferTO(txMap,CommonUtil.convertObjToDouble(loanRebateTO.getIntAmount()).doubleValue()));
                                transferList.add(trans.getCreditTransferTO(txMap, rebateint));
                                trans.doDebitCredit(transferList, _branchCode, true);
                            }
                        }
                    }
                        loanRebateTO.setAuthorizeStatus(CommonConstants.STATUS_AUTHORIZED);
                        loanRebateTO.setAuthorizeBy(user);
                        loanRebateTO.setAuthorizeDt(currDt);
                        dataMap.put("CURR_DT", currDt);
                        dataMap.put("ACCT_NUM", loanRebateTO.getAccNo());
                        dataMap.put("INTEREST_TRANS_AMT", loanRebateTO.getIntAmount());
                        dataMap.put("AUTHORIZE_DT", currDt);
                        dataMap.put("AUTHORIZE_BY", user);
                        dataMap.put("AUTHORIZE_STATUS", "AUTHORIZED");
                        dataMap.put("REBATE_ID", loanRebateTO.getRebateId());
                        dataMap.put("AMOUNT", loanRebateTO.getIntAmount());
                        System.out.println("dataMap####" + dataMap);
                        sqlMap.executeUpdate("authorizeLoanRebate", dataMap);
                        sqlMap.executeUpdate("updateLoanFacilityDetails", dataMap);
                        sqlMap.executeUpdate("authorizeLoanRebateMaster", dataMap);



                        transferList = null;
                        trans = null;
                        acHeads = null;
                        lst = null;

                        txMap = null;
                    //}
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    protected void updateNEwAccountNo(HashMap map) {
        boolean isUpdated = false;
        HashMap where = new HashMap();
        int dif = 0;
        try{
        where.put("PROD_ID", map.get("PROD_ID"));
        where.put("PRODUCT_ID", map.get("PROD_ID"));
        where.put("BRANCH_ID", _branchCode);
        where.put(CommonConstants.BRANCH_ID, _branchCode);
        List lsts = (List) sqlMap.executeQueryForList("getSelectNextAccNo", where);
        String strPrefix = "";
        int len = 13;
        String genID = null;
        if (lsts != null && lsts.size() > 0) {
            String accountClosingNo = CommonUtil.convertObjToStr((lsts.get(0)));
            accountClosingNo = accountClosingNo.substring(10, accountClosingNo.length());
            int nxtIDs = CommonUtil.convertObjToInt(accountClosingNo)+1;
            System.out.println("accountClosingNo"+accountClosingNo);
            String nxtID = CommonUtil.convertObjToStr(CommonUtil.convertObjToInt(accountClosingNo));
            dif = nxtIDs - CommonUtil.convertObjToInt(nxtID);
            where.put("VALUE", CommonUtil.convertObjToStr(nxtIDs));
            sqlMap.executeUpdate("updateCoreBankNextActNum", where);
        }
        }catch(Exception e){
            e.printStackTrace();
        }
    }
    
    public boolean checkClosedLoan(String accountNo) {
        HashMap checkMap = new HashMap();
        boolean closeFlag = false;
        try {
            checkMap.put("ACCT_NUM", accountNo);
            List checkList = (List) sqlMap.executeQueryForList("getClosedLoanList", checkMap);
            if (checkList != null && checkList.size() > 0) {
                closeFlag = true;
            } else {
                closeFlag = false;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return closeFlag;
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
    
    private void actionPerform(HashMap map) throws Exception {
        System.out.println("map#####" + map);

        ArrayList list = (ArrayList) map.get("ACCOUNTLIST");
        String cmd = CommonUtil.convertObjToStr(map.get("COMMAND"));
        ArrayList masterlist = (ArrayList) map.get("MASTERLIST");
        ArrayList singleRecord = null;
        if (list != null) {
            if (cmd.equals("AUTHORIZE")) {
                Date reabteUpToDate = null;
                String rebateId = "";
                for (int i = 0; i < list.size(); i++) {
                    LoanRebateTO loanRebateTO = (LoanRebateTO) list.get(i);
                    reabteUpToDate  = loanRebateTO.getRebateUpto();
                    rebateId = loanRebateTO.getRebateId();
                    interestRebateTransaction(loanRebateTO);
                }
                //added by sreekrshnan for Penal date updation
                HashMap penalUpdateMap = new HashMap();
                penalUpdateMap.put("TO_DATE",getProperDateFormat(reabteUpToDate));
                penalUpdateMap.put("BRANCH_CODE",_branchCode);
                penalUpdateMap.put("REBATE_ID",rebateId);
                sqlMap.executeUpdate("updateLoanFacilityWithPenalDt", penalUpdateMap);
                //Updation ends..
            } else if (cmd.equals(CommonConstants.TOSTATUS_INSERT)) {
                id = getRebateID();
                for (int i = 0; i < list.size(); i++) {
                    LoanRebateTO loanRebateTO = (LoanRebateTO) list.get(i);
                    loanRebateTO.setRebateId(id);
                    sqlMap.executeUpdate("insertLoanRebate", loanRebateTO);
                }
                LoanRebateTO loanRebateTO = (LoanRebateTO) masterlist.get(0);
                loanRebateTO.setRebateId(id);
                sqlMap.executeUpdate("insertLoanRebateMaster", loanRebateTO);
            } else if (cmd.equals(CommonConstants.TOSTATUS_UPDATE)) {
                Date rebateUpTo = null;
                for (int i = 0; i < list.size(); i++) {
                    LoanRebateTO loanRebateTO = (LoanRebateTO) list.get(i);
                    String select = loanRebateTO.getSelect();
                    rebateUpTo = loanRebateTO.getRebateUpto();
                    System.out.println("select is" + select);
                    if (select.equals("true")) {
                        sqlMap.executeUpdate("updateLoanRebate", loanRebateTO);
                    } else {
                        sqlMap.executeUpdate("updateLoanRebateAsDelete", loanRebateTO);
                    }
                }                
            } else if (cmd.equals(CommonConstants.TOSTATUS_DELETE)) {
                for (int i = 0; i < list.size(); i++) {
                    LoanRebateTO loanRebateTO = (LoanRebateTO) list.get(i);
                    sqlMap.executeUpdate("deleteLoanRebate", loanRebateTO);
                }
                LoanRebateTO loanRebateTO = (LoanRebateTO) masterlist.get(0);
                sqlMap.executeUpdate("deleteLoanRebateMaster", loanRebateTO);
            }
        }


    }

    private String getRebateID() throws Exception {
        final IDGenerateDAO dao = new IDGenerateDAO();
        final HashMap where = new HashMap();
        where.put("WHERE", "REBATE_ID");
        return (String) (dao.executeQuery(where)).get(CommonConstants.DATA);
    }

    public void insertNoticeCharges(HashMap inputMap) throws Exception {
        System.out.println("map in insertCharges : " + inputMap);
        //Added BY Suresh
        String prod_type = "";
        if (inputMap.containsKey("PROD_TYPE")) {
            prod_type = CommonUtil.convertObjToStr(inputMap.get("PROD_TYPE"));
            System.out.println("###### PRODUCT_TYPE : " + prod_type);
        }
        HashMap map = (HashMap) inputMap.get("NOTICE_CHARGES");
        boolean onlyChargeDetails = ((Boolean) inputMap.get("ONLY_CHARGE_DETAILS")).booleanValue();
        if (prod_type.equals("") && !prod_type.equals("MDS")) {
            List prodList = sqlMap.executeQueryForList("getProductsForLoanNotice", new HashMap());
            HashMap tempMap = null;
            for (int i = 0; i < prodList.size(); i++) {
                tempMap = (HashMap) prodList.get(i);
                prodMap.put(tempMap.get("PROD_DESC"), tempMap.get("PROD_ID"));
            }
            System.out.println("prodMap in insertCharges : " + prodMap);
        }
        if (map != null && map.size() > 0) {
            Object[] accountList = map.keySet().toArray();
            ArrayList tempList = null;
            for (int i = 0; i < accountList.length; i++) {
                tempList = (ArrayList) map.get(accountList[i]);
                ArrayList rowList = null;
                for (int j = 0; j < tempList.size(); j++) {
                    rowList = (ArrayList) tempList.get(j);
                    insertNoticeChargesTO(rowList, onlyChargeDetails, prod_type);
                }
            }
        }
    }

    public void insertNoticeChargesTO(ArrayList chargeRow, boolean onlyChargeDetails, String prod_type) throws Exception {
        System.out.println("$#@$#@$@@ chargeRow:" + chargeRow);
        HashMap chargeDetMap = null;
        TermLoanChargesTO objChargeTO = null;
        //Changed By Suresh
        String tempAmount = "";
        if (!prod_type.equals("") && prod_type.equals("MDS")) {
            tempAmount = CommonUtil.convertObjToStr(chargeRow.get(8));
        } else {
            tempAmount = CommonUtil.convertObjToStr(chargeRow.get(14));
        }
        System.out.println("##### tempAmount:" + tempAmount);
        if (((Boolean) chargeRow.get(0)).booleanValue() && tempAmount.lastIndexOf("+") != -1) {
            chargeDetMap = new HashMap();
            objChargeTO = new TermLoanChargesTO();
            if (!prod_type.equals("") && prod_type.equals("MDS")) {
                objChargeTO.setAct_num(CommonUtil.convertObjToStr(chargeRow.get(2)));
                chargeDetMap.put("PROD_TYPE", "");
                chargeDetMap.put("PROD_ID", "");
                chargeDetMap.put("CUST_ID", "");
                chargeDetMap.put("ACT_NUM", chargeRow.get(2));
                chargeDetMap.put("MEMBER_NO", chargeRow.get(3));
                chargeDetMap.put("CUST_NAME", chargeRow.get(4));
                chargeDetMap.put("CUST_TYPE", chargeRow.get(9));
            } else {
                String prod_Id = CommonUtil.convertObjToStr(chargeRow.get(5));
                chargeDetMap.put("PROD_TYPE", "TL");
                prod_Id = CommonUtil.convertObjToStr(prodMap.get(prod_Id));
                chargeDetMap.put("PROD_ID", prod_Id);
                System.out.println("$#@$#@$@@prod_id" + prod_Id);
                objChargeTO.setProd_Id(prod_Id);
                objChargeTO.setProd_Type("TL");
                objChargeTO.setAct_num(CommonUtil.convertObjToStr(chargeRow.get(1)));
                chargeDetMap.put("ACT_NUM", chargeRow.get(1));
                chargeDetMap.put("MEMBER_NO", chargeRow.get(3));
                String name = CommonUtil.convertObjToStr(chargeRow.get(2));
                name = name.length() > 128 ? name.substring(0, 127) : name;
                chargeDetMap.put("CUST_ID", name);
                chargeDetMap.put("CUST_NAME", chargeRow.get(4));
                chargeDetMap.put("CUST_TYPE", chargeRow.get(15));
            }
            objChargeTO.setChargeDt(currDt);
            objChargeTO.setStatus_Dt(currDt);
            objChargeTO.setStatus_By(user);
            objChargeTO.setAuthorize_Dt(currDt);
            objChargeTO.setAuthorize_by(user);
            objChargeTO.setAuthorize_Status("AUTHORIZED");
            chargeDetMap.put("SENT_BY", user);
            chargeDetMap.put("SENT_DT", currDt);

            String amount[] = tempAmount.replace("+", ",").split(",");
            Double noticeCharge = new Double(0);
            Double postageCharge = new Double(0);
            noticeCharge = CommonUtil.convertObjToDouble(amount[0]);
            postageCharge = CommonUtil.convertObjToDouble(amount[1]);

            if (!onlyChargeDetails) {
                chargeDetMap.put("NOTICE_CHARGE", noticeCharge);
                chargeDetMap.put("POSTAGE_CHARGE", noticeCharge);

                if (noticeCharge.doubleValue() > 0) {
                    objChargeTO.setCharge_Type("NOTICE CHARGES");
                    objChargeTO.setAmount(noticeCharge);
                    List noticeChargeList = (List) sqlMap.executeQueryForList("getSelectTermLoanChargeList", objChargeTO);
                    System.out.println("$#@$#@$@@ noticeChargeList : " + noticeChargeList);
                    if (noticeChargeList != null && noticeChargeList.size() > 0) {
                        HashMap noticeChargeMap = (HashMap) noticeChargeList.get(0);
                        noticeCharge = new Double(noticeCharge.doubleValue()
                                + CommonUtil.convertObjToDouble(noticeChargeMap.get("AMOUNT")).doubleValue());
                        objChargeTO.setAmount(noticeCharge);
                        String chargeNo = CommonUtil.convertObjToStr(noticeChargeMap.get("CHARGE_NO"));
                        objChargeTO.setStatus(CommonConstants.STATUS_MODIFIED);
                        objChargeTO.setChargeGenerateNo(new Long(CommonUtil.convertObjToLong(chargeNo)));
                        sqlMap.executeUpdate("updateTermLoanChargeTO", objChargeTO);
                    } else {
                        objChargeTO.setStatus(CommonConstants.STATUS_CREATED);
                        sqlMap.executeUpdate("insertTermLoanChargeTO", objChargeTO);
                    }
                }

                if (postageCharge.doubleValue() > 0) {
                    objChargeTO.setCharge_Type("POSTAGE CHARGES");
                    objChargeTO.setAmount(postageCharge);
                    List postageChargeList = (List) sqlMap.executeQueryForList("getSelectTermLoanChargeList", objChargeTO);
                    System.out.println("$#@$#@$@@postageChargeList : " + postageChargeList);
                    if (postageChargeList != null && postageChargeList.size() > 0) {
                        HashMap postageChargeMap = (HashMap) postageChargeList.get(0);
                        postageCharge = new Double(postageCharge.doubleValue()
                                + CommonUtil.convertObjToDouble(postageChargeMap.get("AMOUNT")).doubleValue());
                        objChargeTO.setAmount(noticeCharge);
                        String chargeNo = CommonUtil.convertObjToStr(postageChargeMap.get("CHARGE_NO"));
                        objChargeTO.setStatus(CommonConstants.STATUS_MODIFIED);
                        objChargeTO.setChargeGenerateNo(new Long(CommonUtil.convertObjToLong(chargeNo)));
                        sqlMap.executeUpdate("updateTermLoanChargeTO", objChargeTO);
                    } else {
                        objChargeTO.setStatus(CommonConstants.STATUS_CREATED);
                        sqlMap.executeUpdate("insertTermLoanChargeTO", objChargeTO);
                    }
                }
            } else {
                chargeDetMap.put("NOTICE_CHARGE", new Double(0));
                chargeDetMap.put("POSTAGE_CHARGE", new Double(0));
            }
            sqlMap.executeUpdate("insertNoticeChargeDet", chargeDetMap);
        }
    }

    public HashMap executeQuery(HashMap obj) throws Exception {
        _branchCode = (String) obj.get(CommonConstants.BRANCH_ID);
        currDt = ServerUtil.getCurrentDate(_branchCode);
        return getData(obj);
    }

    public HashMap getData(HashMap obj) throws Exception {
        System.out.println("obj####" + obj);
        TaskHeader header = new TaskHeader();
        header.setBranchID(_branchCode);
        HashMap getDateMap = new HashMap();
        HashMap returnMap = new HashMap();
        obj.put("WHERE", obj.get("ACT_NUM"));
        InterestCalculationTask interestCalTask = new InterestCalculationTask(header);


        //            List lst=(List)sqlMap.executeQueryForList("getLastIntCalDate",obj);
        //            getDateMap=(HashMap)lst.get(0);
        //            prod_id=  CommonUtil.convertObjToStr(getDateMap.get("PROD_ID"));
        //            map.put("PROD_ID",prod_id);

        HashMap behaveLike = (HashMap) (sqlMap.executeQueryForList("getLoanBehaves", obj).get(0));
        Date CURR_DATE = new java.util.Date();
        CURR_DATE = ServerUtil.getCurrentDateProperFormat(_branchCode);
        System.out.println("curr_date###1" + CURR_DATE);
        CURR_DATE = (Date) ServerUtil.getCurrentDate(_branchCode);
        String mapNameForLastIntCalcDt = "getLastIntCalDate";
        if (CommonUtil.convertObjToStr(behaveLike.get("BEHAVES_LIKE")).equals("OD")) {
            mapNameForLastIntCalcDt = "getLastIntCalDateAD";
        }
        List lst = (List) sqlMap.executeQueryForList(mapNameForLastIntCalcDt, obj);
        getDateMap = (HashMap) lst.get(0);
        Date lastPayDate = (Date) getDateMap.get("LAST_INT_CALC_DT");
        double interest = 0;
        double penalInt = 0;
        if (CommonUtil.convertObjToStr(behaveLike.get("BEHAVES_LIKE")).equals("OD")) {
            //                lst=(List)sqlMap.executeQueryForList("getLastIntCalDate",map);
            //                getDateMap=(HashMap)lst.get(0);
            //                lastPayDate=(Date)getDateMap.get("LAST_INTCALC_DTDEBIT");
            //                noOfDays=DateUtil.dateDiff(lastPayDate, currDt);
            //                Date CURR_DATE=currDt.clone();
            //                    getDateMap.put("CURR_DATE",CURR_DATE);
            obj.put("BRANCH_ID", _branchCode);
            obj.put("ACT_NUM", obj.get(CommonConstants.MAP_WHERE));
            obj.put("LAST_INT_CALC_DT", lastPayDate);
            obj.put("PROD_ID", getDateMap.get("PROD_ID"));
            obj.put("BEHAVES_LIKE", behaveLike.get("BEHAVES_LIKE"));
            obj.put("LOAN_ACCOUNT_CLOSING", "LOAN_ACCOUNT_CLOSING");
            System.out.println("before od interest calculation####" + obj);
            getDateMap = interestCalTask.interestCalcTermLoanAD(obj); // we need same used for TL also

            if (getDateMap.containsKey("LOAN_CLOSING_PENAL_INT")) {
                penalInt = CommonUtil.convertObjToDouble(getDateMap.get("LOAN_CLOSING_PENAL_INT")).doubleValue();
            }
            interest = CommonUtil.convertObjToDouble(getDateMap.get("INTEREST")).doubleValue();
            //                lst=sqlMap.executeQueryForList("getSumProductOD",getDateMap);
            //                getDateMap=(HashMap)lst.get(0);
            System.out.println(lastPayDate + "OD  #####!" + getDateMap);
            //                    returnMap.put("AccountInterest",getDateMap.get("INTEREST"));
            returnMap.put("AccountInterest", new Double(interest));
            returnMap.put("AccountPenalInterest", new Double(penalInt));
        } else {
            HashMap hash = new HashMap();
            //                    map.put("CURR_DATE",CURR_DATE);
            obj.put("BRANCH_ID", obj.get("BRANCH_CODE"));
            obj.put("ACT_NUM", obj.get("WHERE"));
            obj.put("BEHAVES_LIKE", behaveLike.get("BEHAVES_LIKE"));
            obj.put("LAST_INT_CALC_DT", lastPayDate);
            obj.put("PROD_ID", getDateMap.get("PROD_ID"));
            obj.put("LOAN_ACCOUNT_CLOSING", "LOAN_ACCOUNT_CLOSING");
            System.out.println("before od interest calculation####" + obj);
            //                hash=interestCalTask.calculateInterestNonBatchAndbatch(map);  //already calculating interest now using dayend balance comment
            hash = interestCalTask.interestCalcTermLoanAD(obj);
            System.out.println("totInterest#####" + hash);
            if (hash != null) {
                //                    returnMap.put("AccountInterest",hash.get("TOT_INT"));
                if (obj.containsKey("DEPOSIT_PREMATURE_CLOSER")) {
                    returnMap.put("AccountInterest", hash.get("FINAL_INT"));
                    //                             returnMap.put("AccountPenalInterest",hash.get("LOAN_INT"));
                } else {
                    returnMap.put("AccountInterest", hash.get("INTEREST"));

                    if (hash.containsKey("LOAN_CLOSING_PENAL_INT")) {
                        penalInt = CommonUtil.convertObjToDouble(hash.get("LOAN_CLOSING_PENAL_INT")).doubleValue();
                    }
                    interest = CommonUtil.convertObjToDouble(hash.get("INTEREST")).doubleValue();

                    returnMap.put("AccountInterest", new Double(interest));
                    returnMap.put("AccountPenalInterest", new Double(penalInt));
                    returnMap.put("LOANBALANCE", hash.get("PRINCIPAL_BAL"));
                    returnMap.put("ASSET_STATUS", hash.get("ASSET_STATUS"));
                    returnMap.put("ROI", hash.get("ROI"));
                    returnMap.put("REPAYMENT_TYPE", hash.get("REPAYMENT_TYPE"));
                    returnMap.put("PRINCIPAL_DUE", hash.get("PRINCIPAL_DUE"));
                    returnMap.put("SHOW_INSTALLMENT_NO", hash.get("SHOW_INSTALLMENT_NO"));

                    if (hash.containsKey("UPDATE_RET_APP_DT") && hash.get("UPDATE_RET_APP_DT") != null) {
                        returnMap.put("UPDATE_RET_APP_DT", hash.get("UPDATE_RET_APP_DT"));
                    }


                }
            }
            hash = null;
        }
        returnMap.putAll(getDateMap);
        HashMap principalMap = new HashMap();
        System.out.println("returnMap  ##" + returnMap);
        if (!(returnMap != null && returnMap.containsKey("REPAYMENT_TYPE") && returnMap.get("REPAYMENT_TYPE") != null && returnMap.get("REPAYMENT_TYPE").equals("EMI"))) {
            principalMap = getPrincipalDue(obj);
            returnMap.put("AccountInterest", new Double(interest - paid_interest));
            returnMap.put("AccountPenalInterest", new Double(penalInt - paid_penal_int));

        } else {
            List installLst = sqlMap.executeQueryForList("getAllLoanInstallment", obj);
            if (installLst != null && installLst.size() > 0) {
                returnMap.put("NO_OF_INSTALLMENT", new Long(installLst.size()));
            }
            HashMap dueMap = (HashMap) installLst.get(0);
            returnMap.put("INSTALLMENT AMT", dueMap.get("TOTAL_AMT"));
        }
        //        if(obj.containsKey("MODE") && obj.get("MODE")!=null && obj.get("MODE").equals("UPDATE"))
        //        {
        System.out.println("obj###" + obj);
        lst = sqlMap.executeQueryForList("getSelectTermLoanChargeDetailsTO", obj.get("ACT_NUM"));
        returnMap.put("TermLoanChargesTO", lst);
        //        }
        System.out.println("returnMap####   " + returnMap);
        returnMap.putAll(principalMap);
        System.out.println("returnMap####" + returnMap);
        ServiceLocator.flushCache(sqlMap);
        System.gc();
        return returnMap;
    }
    //as an when customer get principal due

    private HashMap getSuspenseProduct(HashMap map) {
        HashMap updateMap = new HashMap();
        HashMap returnMap = new HashMap();
        try {
            List susAcHdList = sqlMap.executeQueryForList("getSelectSuspenseProductTO", map);
            if (susAcHdList != null && susAcHdList.size() > 0) {
                SuspenseAccountProductTO suspenseProdTo = (SuspenseAccountProductTO) susAcHdList.get(0);
                if (suspenseProdTo.getTxtSuspenseProductHead() != null && !suspenseProdTo.getTxtSuspenseProductHead().equals("")) {
                }
                map.put("PRODUCT_ID", map.get("PROD_ID"));
                List susAccnoList = sqlMap.executeQueryForList("getSelectNextAccNo", map);
                if (susAccnoList != null && susAccnoList.size() > 0) {
                    String accountClosingNo = CommonUtil.convertObjToStr((susAccnoList.get(0)));
                    if (!accountClosingNo.equals("") && accountClosingNo != null) {
                        SuspenseAccountMasterTO suspenseAcctMaserTo = new SuspenseAccountMasterTO();
                        suspenseAcctMaserTo.setCboSuspenseProdID(suspenseProdTo.getTxtSuspenseProdName());
                        suspenseAcctMaserTo.setTxtSuspenseActNum(accountClosingNo);
                        suspenseAcctMaserTo.setTxtSuspenseProdDescription(suspenseProdTo.getTxtSuspenseProdID());
                        suspenseAcctMaserTo.setTxtCustomerId("");
                        suspenseAcctMaserTo.setTxtName("");
                        suspenseAcctMaserTo.setTxtPrefix("");
                        suspenseAcctMaserTo.setStatusBy("");
                        suspenseAcctMaserTo.setTdtSuspenseOpenDate(currDt);
                        suspenseAcctMaserTo.setStatusDt(currDt);
                        suspenseAcctMaserTo.setAuthorizeStatus(CommonConstants.STATUS_AUTHORIZED);
                        suspenseAcctMaserTo.setStatus("CREATED");
                        suspenseAcctMaserTo.setAuthorizeBy("");
                        suspenseAcctMaserTo.setAuthorizeDt(currDt);
                        suspenseAcctMaserTo.setCboAgentID("");
                        suspenseAcctMaserTo.setTxtAccRefNo("");
                        suspenseAcctMaserTo.setIsAuction("N");
                        suspenseAcctMaserTo.setBranchCode(_branchCode);
                        sqlMap.executeUpdate("insertSuspenseAccountMaster", suspenseAcctMaserTo);
                        updateMap.put("STATUS", CommonUtil.convertObjToStr(suspenseAcctMaserTo.getAuthorizeStatus()));
                        updateMap.put("USER_ID", CommonUtil.convertObjToStr(suspenseAcctMaserTo.getAuthorizeBy()));
                        updateMap.put("AUTHORIZEDT", DateUtil.getDateWithoutMinitues(suspenseAcctMaserTo.getAuthorizeDt()));
                        updateMap.put("SUSPENSE_ACCT_NUM", CommonUtil.convertObjToStr(suspenseAcctMaserTo.getTxtSuspenseActNum()));
                        sqlMap.executeUpdate("authorizeSuspenseAccountMaster", updateMap);
                        updateNEwAccountNo(map);
                        returnMap.put("ACCT_NUM", suspenseAcctMaserTo.getTxtSuspenseActNum());
                        returnMap.put("PROD_ID", suspenseAcctMaserTo.getTxtSuspenseProdDescription());
                        returnMap.put("AC_HD", suspenseProdTo.getTxtSuspenseProductHead());
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        System.out.println("returnMap^%^%^%^%^%" + returnMap);
        return returnMap;
    }

    public HashMap getPrincipalDue(HashMap map) throws Exception {
        String prodType = CommonUtil.convertObjToStr(map.get("PROD_TYPE"));
        HashMap insertPenal = null;
        HashMap returnMap = new HashMap();
        paid_interest = 0;
        paid_penal_int = 0;
        paid_principal = 0;
        Date curr_dt = (Date) currDt.clone();
        if (prodType != null && prodType.equals("TL")) {

            HashMap InstalDate = new HashMap();
            List getInstallDate = null;

            Date cDate = ServerUtil.getCurrentDateProperFormat(_branchCode);
            HashMap allInstallmentMap = null;
            double principleAmt = 0;
            double intAmt = 0;
            double clearBalance = 0;
            List facilitylst = sqlMap.executeQueryForList("LoneFacilityDetailAD", map);
            if (facilitylst != null && facilitylst.size() > 0) {
                HashMap hash = (HashMap) facilitylst.get(0);
                clearBalance = CommonUtil.convertObjToDouble(hash.get("CLEAR_BALANCE")).doubleValue();
                clearBalance = clearBalance * -1;
            }
            List paidAmt = sqlMap.executeQueryForList("getPaidPrinciple", map);
            allInstallmentMap = (HashMap) paidAmt.get(0);
            double totPrinciple = CommonUtil.convertObjToDouble(allInstallmentMap.get("PRINCIPLE")).doubleValue();
            paidAmt = sqlMap.executeQueryForList("getIntDetails", map);
            if (paidAmt != null && paidAmt.size() > 0) {
                allInstallmentMap = (HashMap) paidAmt.get(0);
            }
            double pbal = CommonUtil.convertObjToDouble(allInstallmentMap.get("EXCESS_AMT")).doubleValue();
            //            totPrinciple+=totExcessAmt;
            List lst = sqlMap.executeQueryForList("getAllLoanInstallment", map);
            Date inst_dt = null;
            double instalAmt = 0;
            for (int i = 0; i < lst.size(); i++) {
                allInstallmentMap = (HashMap) lst.get(i);
                instalAmt = CommonUtil.convertObjToDouble(allInstallmentMap.get("PRINCIPAL_AMT")).doubleValue();
                if (instalAmt <= totPrinciple) {
                    totPrinciple -= instalAmt;
                    //                        if(lst.size()==1){
                    inst_dt = new Date();
                    String in_date = CommonUtil.convertObjToStr(allInstallmentMap.get("INSTALLMENT_DT"));
                    inst_dt = DateUtil.getDateMMDDYYYY(in_date);
                    //                        }
                    if (DateUtil.dateDiff(curr_dt, inst_dt) >= 0) {
                        paid_principal += instalAmt;
                    }

                } else {
                    inst_dt = new Date();
                    String in_date = CommonUtil.convertObjToStr(allInstallmentMap.get("INSTALLMENT_DT"));
                    inst_dt = DateUtil.getDateMMDDYYYY(in_date);
                    System.out.println("paid_principal" + paid_principal + "totprincipal###" + totPrinciple);
                    if (DateUtil.dateDiff(curr_dt, inst_dt) >= 0) {
                        paid_principal += totPrinciple;
                    }
                    System.out.println("paid_principal" + paid_principal + "totprincipal###" + totPrinciple);

                    totPrinciple = CommonUtil.convertObjToDouble(allInstallmentMap.get("PRINCIPAL_AMT")).doubleValue() - totPrinciple;

                    break;
                }

            }
            returnMap.put("INSTALLMENT AMT", new Double(instalAmt));
            returnMap.put("NO_OF_INSTALLMENT", new Long(lst.size()));
            returnMap.put("PAID_PRINCIPAL", new Double(paid_principal));
            Date from_Dt = DateUtil.addDays(inst_dt, 1);
//            Date currDt = currDt.clone();
            currDt.setDate(from_Dt.getDate());
            currDt.setMonth(from_Dt.getMonth());
            currDt.setYear(from_Dt.getYear());
            map.put("FROM_DATE", currDt);//DateUtil.addDays(inst_dt,1));
            map.put("TO_DATE", cDate);
            System.out.println("getTotalamount#####" + map);
            List lst1 = null;
            if (inst_dt != null && (totPrinciple > 0)) {
                lst1 = sqlMap.executeQueryForList("getTotalAmountOverDue", map);
                System.out.println("listsize####" + lst1);
            }
            double principle = 0;
            if (lst1 != null && lst1.size() > 0) {
                HashMap overDuemap = (HashMap) lst1.get(0);
                principle = CommonUtil.convertObjToDouble(overDuemap.get("PRINCIPAL_AMOUNT")).doubleValue();
            }
            totPrinciple += principle;
            insertPenal = new HashMap();
            if (inst_dt != null) {
                if (DateUtil.dateDiff(currDt, inst_dt) <= 0 && clearBalance >= totPrinciple) {
                    returnMap.put("PRINCIPAL_DUE", new Double(totPrinciple));
                    insertPenal.put("CURR_MONTH_PRINCEPLE", new Double(totPrinciple));
                } else if (DateUtil.dateDiff(currDt, inst_dt) <= 0 && clearBalance < totPrinciple) {
                    returnMap.put("PRINCIPAL_DUE", new Double(clearBalance));
                    insertPenal.put("CURR_MONTH_PRINCEPLE", new Double(clearBalance));
                } else {
                    returnMap.put("PRINCIPAL_DUE", new Double(0));
                    insertPenal.put("CURR_MONTH_PRINCEPLE", new Double(0));
                }
            }
            insertPenal.put("INSTALL_DT", inst_dt);
            map.put("FROM_DT", map.get("LAST_INT_CALC_DT"));
            map.put("FROM_DT", DateUtil.addDays(((Date) map.get("FROM_DT")), 2));//
            map.put("TO_DATE", cDate);
            System.out.println("map#####@@@@" + map);
            List getIntDetails = sqlMap.executeQueryForList("getPaidPrinciple", map);//from_dt,to_date,act_num
            HashMap hash = null;
            if (getIntDetails != null) {
                for (int i = 0; i < getIntDetails.size(); i++) {
                    hash = (HashMap) getIntDetails.get(i);
                    returnMap.put("PAID_INTEREST", hash.get("INTEREST"));
                    returnMap.put("PAID_PENAL_INTEREST", hash.get("PENAL"));
                    paid_interest = CommonUtil.convertObjToDouble(hash.get("INTEREST")).doubleValue();
                    paid_penal_int = CommonUtil.convertObjToDouble(hash.get("PENAL")).doubleValue();
                }
            }
        } else {
            map.put("FROM_DT", map.get("LAST_INT_CALC_DT"));
            map.put("FROM_DT", DateUtil.addDays(((Date) map.get("FROM_DT")), 2));//2
            map.put("TO_DATE", ServerUtil.getCurrentDateProperFormat(_branchCode));
            System.out.println("map$$$^^^^" + map);
            List lst = sqlMap.executeQueryForList("getPaidPrincipleAD", map);
            HashMap hash = new HashMap();
            int i = 0;
            if (lst != null) {
                hash = (HashMap) lst.get(i);
            }
            returnMap.put("PAID_INTEREST", hash.get("INTEREST"));
            returnMap.put("PAID_PENAL_INTEREST", hash.get("PENAL"));
            paid_interest = CommonUtil.convertObjToDouble(hash.get("INTEREST")).doubleValue();
            paid_penal_int = CommonUtil.convertObjToDouble(hash.get("PENAL")).doubleValue();
        }
        System.out.println("returnMap####" + returnMap);
        return returnMap;
    }

    /*
     * more interest already collected from customer now banker want to return
     * back
     */
    private void exccessCollectedTransaction(HashMap map) throws Exception {
        ArrayList transList = null;
        HashMap notDeleted = new HashMap();
        HashMap acHeads = new HashMap();
        transactionDAO = new TransactionDAO(CommonConstants.DEBIT);
        //        transactionDAO.setTransType(CommonConstants.DEBIT);
        String branchID = CommonUtil.convertObjToStr(map.get(CommonConstants.BRANCH_ID));
        HashMap totAmt = new HashMap();
        if (map.containsKey("ALL_AMOUNT")) {
            totAmt = (HashMap) map.get("ALL_AMOUNT");
        }
        String act_num = CommonUtil.convertObjToStr(map.get("ACT_NUM"));
        acHeads = (HashMap) sqlMap.executeQueryForObject("getLoanAccountClosingHeads", act_num);
        HashMap txMap = new HashMap();
        transactionDAO.setInitiatedBranch(_branchCode);
        transactionDAO.setLinkBatchID(act_num);
        System.out.println("acHeads:" + acHeads);
        TxTransferTO transferTo = null;
        String behaves = "";
        Double loanTempAmt = new Double(0);
        // New mode
        double reverseInt = CommonUtil.convertObjToDouble(totAmt.get("TRANSACTION_AMT")).doubleValue();
        System.out.println("reverseInt  ###" + reverseInt);
        //                    reverseInt=reverseInt*(-1);
        System.out.println("reverseInt  ###" + reverseInt);
        txMap = new HashMap();
        txMap.put(TransferTrans.DR_PROD_TYPE, TransactionFactory.GL);
        txMap.put(TransferTrans.DR_AC_HD, (String) acHeads.get("AC_DEBIT_INT"));
        txMap.put(TransferTrans.DR_BRANCH, branchID);
        txMap.put(TransferTrans.CURRENCY, "INR");
        txMap.put("AUTHORIZEREMARKS", "ACT_CLOSE");

        txMap.put(TransferTrans.CR_PROD_ID, (String) acHeads.get("PROD_ID"));
        txMap.put(TransferTrans.CR_AC_HD, (String) acHeads.get("ACCT_HEAD"));
        txMap.put(TransferTrans.CR_ACT_NUM, act_num);
        txMap.put(TransferTrans.CR_BRANCH, branchID);
        txMap.put(TransferTrans.CURRENCY, "INR");
        //                    if(behaves.equals("OD"))
        //                        txMap.put(TransferTrans.CR_PROD_TYPE, TransactionFactory.ADVANCES);
        //                    else
        txMap.put(TransferTrans.CR_PROD_TYPE, TransactionFactory.LOANS);
        txMap.put("AUTHORIZEREMARKS", "ACT_CLOSE");
        System.out.println("txmap  ####" + txMap);
        txMap.put(TransferTrans.PARTICULARS, "Excess Interest");
        transferTo = transactionDAO.addTransferCreditLocal(txMap, reverseInt);
        ArrayList transferList = new ArrayList();
        transferTo.setStatusBy(CommonUtil.convertObjToStr(map.get(CommonConstants.USER_ID)));
        transferList.add(transferTo);
        txMap.put(TransferTrans.PARTICULARS, act_num);
        transferTo = transactionDAO.addTransferDebitLocal(txMap, reverseInt);
        transferTo.setStatusBy(CommonUtil.convertObjToStr(map.get(CommonConstants.USER_ID)));
        transferList.add(transferTo);
        // //System.out.println("transferList:" + transferList);
        transactionDAO.setCommandMode(CommonConstants.TOSTATUS_INSERT);
        if (totAmt != null && totAmt.size() > 0) {
            transactionDAO.setLoanAmtMap(totAmt);
        }

        transactionDAO.doTransferLocal(transferList, branchID);

    }

    private void destroyObjects() {
        transactionDAO.setLoanAmtMap(new HashMap());
    }
}
