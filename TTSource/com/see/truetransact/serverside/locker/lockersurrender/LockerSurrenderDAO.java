/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition.
 **
 *
 * TokenConfigDAO.java
 *
 * Created on Thu Jan 20 17:19:05 IST 2005
 */
package com.see.truetransact.serverside.locker.lockersurrender;

import java.util.List;
import java.util.ArrayList;
import com.see.truetransact.commonutil.DateUtil;
import java.util.Date;

import com.ibatis.db.sqlmap.SqlMap;
import com.see.truetransact.serverutil.ServerUtil;

import com.see.truetransact.commonutil.CommonConstants;
import com.see.truetransact.commonutil.NoCommandException;
import com.see.truetransact.commonutil.CommonUtil;
import com.see.truetransact.commonutil.TTException;
import com.see.truetransact.serverside.TTDAO;
import com.see.truetransact.servicelocator.ServiceLocator;
import com.see.truetransact.serverexception.TransRollbackException;
import com.see.truetransact.serverexception.ServiceLocatorException;
import com.see.truetransact.serverside.common.idgenerate.IDGenerateDAO;
import com.see.truetransact.serverside.common.log.LogDAO;
import com.see.truetransact.transferobject.common.log.LogTO;
import com.see.truetransact.transferobject.transaction.transfer.TxTransferTO;
import com.see.truetransact.serverside.transaction.transfer.TransferDAO;
import com.see.truetransact.serverside.transaction.cash.CashTransactionDAO;
import com.see.truetransact.transferobject.transaction.chargesServiceTax.ChargesServiceTaxTO;
import com.see.truetransact.serverside.common.transaction.TransactionDAO;
import com.see.truetransact.serverside.common.transaction.TransferTrans;
import com.see.truetransact.serverside.transaction.common.TransactionFactory;
import com.see.truetransact.transferobject.common.transaction.TransactionTO;
import com.see.truetransact.serverside.transaction.chargesServiceTax.ChargesServiceTaxDAO;
import com.see.truetransact.transferobject.locker.lockersurrender.LockerSurrenderTO;
import com.see.truetransact.transferobject.transaction.cash.CashTransactionTO;

import java.util.HashMap;
import java.util.LinkedHashMap;

/**
 * TokenConfig DAO.
 *
 */
public class LockerSurrenderDAO extends TTDAO {

    private static SqlMap sqlMap = null;
    private LockerSurrenderTO objTO;
    private LogDAO logDAO;
    private LogTO logTO;
    private ChargesServiceTaxDAO chargesServiceTaxDAO = new ChargesServiceTaxDAO();
//    private ChargesServiceTaxTO chargesServiceTaxTO= new ChargesServiceTaxTO();
    private TransactionDAO transactionDAO = null;
    private TransactionTO objTransactionTO;
    private String lockerIssId = "";
    private java.util.Date currDt = null;
    TransactionTO transactionTO = new TransactionTO();
    private String batchid = "";
    HashMap returnData = null;
    //   private TransactionDAO transactionDAO = null ;

    /**
     * Creates a new instance of TokenConfigDAO
     */
    public LockerSurrenderDAO() throws ServiceLocatorException {
        ServiceLocator locate = ServiceLocator.getInstance();
        sqlMap = (SqlMap) locate.getDAOSqlMap();
    }

    private HashMap getData(HashMap map) throws Exception {
        HashMap returnMap = new HashMap();
        if (!CommonUtil.convertObjToStr(map.get("VIEW_TYPE")).equals("LockerNo") && !CommonUtil.convertObjToStr(map.get("VIEW_TYPE")).equals("LockerListClosednDue")) {
            transactionDAO = new TransactionDAO(CommonConstants.CREDIT);
            HashMap where = new HashMap();
            //         String where = (String) map.get(CommonConstants.MAP_WHERE);
            TransactionTO dataTO = new TransactionTO();
            LockerSurrenderTO objLockerSurrenderTO;
            HashMap dataMap = new HashMap();
            where.put("LOCKER_NUM", CommonUtil.convertObjToStr(map.get("LOCKER_NUM")));
            where.put("CUST_ID", CommonUtil.convertObjToStr(map.get("CUST_ID")));
            where.put("BRANCH_CODE", CommonUtil.convertObjToStr(map.get("BRANCH_CODE")));
            where.put("SURRENDER_ID", CommonUtil.convertObjToStr(map.get("SURRENDER_ID")));
            System.out.println("#####where" + where);
            List list = (List) sqlMap.executeQueryForList("getSelectLockerSurTO", where);
            objLockerSurrenderTO = (LockerSurrenderTO) list.get(0);
            returnMap.put("LockerSurTO", list);
            list = (List) sqlMap.executeQueryForList("getSelectLockerTO", map.get("ISSUE_ID"));
            returnMap.put("LockerTO", list);
            list = null;
            currDt = (Date) currDt.clone();
//            map.put("TRANS_DT",currDt);
//            map.put("TRANS_ID",CommonUtil.convertObjToStr(map.get("LOCKER_NUM")));
//            map.put("BRANCH_CODE",CommonUtil.convertObjToStr(map.get("BRANCH_CODE")));
//            map.put(CommonConstants.MAP_WHERE, "getSelectRemittanceIssueTransactionTODate");
//            list = transactionDAO.getData(map);

            HashMap getRemitTransMap = new HashMap();
            getRemitTransMap.put("TRANS_ID", CommonUtil.convertObjToStr(map.get("SURRENDER_ID")));
            getRemitTransMap.put("TRANS_DT", currDt);
            getRemitTransMap.put("BRANCH_CODE", CommonUtil.convertObjToStr(map.get("BRANCH_CODE")));
            System.out.println("@#%$#@%#$%getRemitTransMap:" + getRemitTransMap);
            list = sqlMap.executeQueryForList("getSelectRemittanceIssueTransactionTODate", getRemitTransMap);
            if (list != null && list.size() > 0) {
                dataTO = (TransactionTO) list.get(0);
                if (dataTO.getTransType().equals("CASH")) {
                    if (returnMap != null) {
                        if (returnMap.size() > 0) {
                            HashMap acHeads = (HashMap) sqlMap.executeQueryForObject("getLockerAccountHeads", objLockerSurrenderTO.getProdId());
                            HashMap getTransMap = new HashMap();
                            dataMap.put("AMOUNT", objLockerSurrenderTO.getCharges());
                            double amount = CommonUtil.convertObjToDouble(dataMap.get("AMOUNT")).doubleValue();
                            //System.out.println("@@@@@@@amount"+amount);
                            getTransMap.put("LINK_BATCH_ID", objLockerSurrenderTO.getLocNum());
                            getTransMap.put("TODAY_DT", currDt);
                            getTransMap.put("INITIATED_BRANCH", CommonUtil.convertObjToStr(_branchCode));
                            if (amount > 0) {
                                getTransMap.put("AC_HD_ID", acHeads.get("LOC_RENT_AC_HD"));
                                getTransMap.put("AMOUNT", dataMap.get("AMOUNT"));
                                List lst = (List) sqlMap.executeQueryForList("getCashTransBatchID", getTransMap);
                                if (lst != null) {
                                    if (lst.size() > 0) {
                                        returnMap.put("AMT_CASH_TRANS_DETAILS", lst.get(0));
                                    }
                                }
                                lst = null;
                            }
                            getTransMap = null;
                            acHeads = null;
                        }
                    }
                }
                if (dataTO.getTransType().equals("TRANSFER")) {
                    if (returnMap != null) {
                        if (returnMap.size() > 0) {
                            HashMap acHeads = (HashMap) sqlMap.executeQueryForObject("getLockerAccountHeads", objLockerSurrenderTO.getProdId());
                            HashMap getTransMap = new HashMap();
                            dataMap.put("AMOUNT", objLockerSurrenderTO.getCharges());
                            double amount = CommonUtil.convertObjToDouble(dataMap.get("AMOUNT")).doubleValue();
                            //System.out.println("@@@@@@@amount"+amount);
                            getTransMap.put("LINK_BATCH_ID", objLockerSurrenderTO.getLocNum());
                            getTransMap.put("TODAY_DT", currDt);
                            getTransMap.put("INITIATED_BRANCH", _branchCode);
                            if (amount > 0) {
                                getTransMap.put("AC_HD_ID", acHeads.get("LOC_RENT_AC_HD"));
                                getTransMap.put("AMOUNT", dataMap.get("AMOUNT"));
                                List lst = (List) sqlMap.executeQueryForList("getTransferTransBatchID", getTransMap);
                                if (lst != null) {
                                    if (lst.size() > 0) {

                                        returnMap.put("AMT_TRANSFER_TRANS_DETAILS", lst.get(0));
                                    }
                                }
                                lst = null;
                            }
                            getTransMap = null;
                            acHeads = null;
                        }
                    }
                }
                returnMap.put("TransactionTO", list);
                list = null;

            }
            objLockerSurrenderTO = null;
            dataTO = null;
            dataMap = null;
            where = null;
            transactionDAO = null;
        } else {
            List lst = (List) sqlMap.executeQueryForList("getSurrenderAuthorizeStatus", map);
            if (lst != null) {
                if (lst.size() > 0) {
                    returnMap.put("SURRENDER_AUTHORIZATION_STATUS", lst.get(0));
                }
            }
            lst = null;
        }
        System.out.println("@@@@@@@@@@@@@@@@@returnMap" + returnMap);
        return returnMap;
    }

    private String getLockerIssueId() throws Exception {
        final IDGenerateDAO dao = new IDGenerateDAO();
        final HashMap where = new HashMap();
        where.put(CommonConstants.MAP_WHERE, "LOCKER_ISSUE_ID");
        return (String) (dao.executeQuery(where)).get(CommonConstants.DATA);
    }

    private void updateData() throws Exception {
        try {
//            sqlMap.startTransaction();
            objTO.setStatus(CommonConstants.STATUS_MODIFIED);
            logTO.setData(objTO.toString());
            logTO.setPrimaryKey(objTO.getKeyData());
            logTO.setStatus(objTO.getCommand());
//            objTO.setLockerOutDt(ServerUtil.getCurrentDateWithTime(_branchCode));
            sqlMap.executeUpdate("updateLockerSurTO", objTO);
            logDAO.addToLog(logTO);
//            sqlMap.commitTransaction();
        } catch (Exception e) {
            sqlMap.rollbackTransaction();
            e.printStackTrace();
            throw new TTException(e);
        }
    }

    private void deleteData(HashMap map) throws Exception {
        try {
//            sqlMap.startTransaction();
            objTO.setStatus(CommonConstants.STATUS_DELETED);
            logTO.setData(objTO.toString());
            logTO.setPrimaryKey(objTO.getKeyData());
            logTO.setStatus(objTO.getCommand());
            sqlMap.executeUpdate("updateLockerSurTO", objTO);
            logDAO.addToLog(logTO);

            // if(objTO.getSurRenew().equalsIgnoreCase("RENEW")){
            HashMap transMap = new HashMap();
            transMap.put("LINK_BATCH_ID", objTO.getRemarks());
            transMap.put("TODAY_DT", currDt);
            List transId = sqlMap.executeQueryForList("getTransIDForLockerSurrenderTrans", transMap);
            String serviceTransID = "";
            if (transId != null && transId.size() > 0) {
                transMap = (HashMap) transId.get(0);
                serviceTransID = CommonUtil.convertObjToStr(transMap.get("TRANS_ID"));
            } else {
                transId = sqlMap.executeQueryForList("getTransIDForLockerSurrender", transMap);
                if (transId != null && transId.size() > 0) {
                    transMap = (HashMap) transId.get(0);
                    serviceTransID = CommonUtil.convertObjToStr(transMap.get("TRANS_ID"));
                }
            }
            transMap.clear();
            transMap.put("BATCH_ID", serviceTransID);
            transMap.put("TRANS_ID", serviceTransID);
            double rent = CommonUtil.convertObjToDouble(objTO.getCharges()).doubleValue();
            double servTax = CommonUtil.convertObjToDouble(objTO.getServiceTax()).doubleValue();
            transMap.put("AMOUNT", new Double(rent + servTax));
            map.put("TOTAL_AMT_TRANSACTION", transMap);
            map.put("AMT_TRANSACTION", transMap);

            doServiceTaxTrans(map, CommonConstants.TOSTATUS_DELETE);
            transMap = null;
            transId = null;


//            sqlMap.commitTransaction();
        } catch (Exception e) {
            sqlMap.rollbackTransaction();
            e.printStackTrace();
            throw new TTException(e);
        }
    }

    public static void main(String str[]) {
        try {
            LockerSurrenderDAO dao = new LockerSurrenderDAO();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public HashMap execute(HashMap map) throws Exception {
        _branchCode = (String) map.get(CommonConstants.BRANCH_ID);
        currDt = ServerUtil.getCurrentDate(_branchCode);
        System.out.println("Map in locker surr DAO : " + map);
        if (!map.containsKey("LOCKER_DEPOSIT_SI_DAO")) {
            sqlMap.startTransaction();
        }

        returnData = new HashMap();
        try {
            if (map.containsKey("LockerSurrenderTO")) {
                transactionDAO = new TransactionDAO(CommonConstants.DEBIT);
                transactionDAO.setInitiatedBranch(_branchCode);
                transactionDAO.setInitialValuesForLogTO(map);
                objTO = (LockerSurrenderTO) map.get("LockerSurrenderTO");
                objTO.setStatusBy(CommonUtil.convertObjToStr(map.get(CommonConstants.USER_ID)));
                logDAO = new LogDAO();
                logTO = new LogTO();
                logTO.setUserId(CommonUtil.convertObjToStr(map.get(CommonConstants.USER_ID)));
                logTO.setBranchId(CommonUtil.convertObjToStr(map.get(CommonConstants.BRANCH_ID)));
                logTO.setIpAddr(CommonUtil.convertObjToStr(map.get(CommonConstants.IP_ADDR)));
                logTO.setModule(CommonUtil.convertObjToStr(map.get(CommonConstants.MODULE)));
                logTO.setScreen(CommonUtil.convertObjToStr(map.get(CommonConstants.SCREEN)));
                if (objTO != null) {
                    final String command = objTO.getCommand();

                    if (command != null) {
                        if (command.equals(CommonConstants.TOSTATUS_INSERT)) {
                            insertData(map, command);
                            
                            returnData.put("TRANS_ID", objTO.getRemarks());
//                            if(objTO.getSurRenew().equalsIgnoreCase("RENEW")){
                            double refundAmount = 0.0;;
                            //    if(objTO.getSurRenew().equalsIgnoreCase("RENEW") ||objTO.getSurRenew().equalsIgnoreCase("SURRENDER") ||objTO.getSurRenew().equalsIgnoreCase("BREAKOPEN"))
                            // { 
                            double chargeAmount = objTO.getCharges().doubleValue();
                            double serviceTax = CommonUtil.convertObjToDouble(objTO.getServiceTax());
                            if (objTO.getTxtRefund() != null) {
                                refundAmount = objTO.getTxtRefund().doubleValue();
                            }
                            double penalAmount = objTO.getPenalAmount().doubleValue();
                            System.out.println("chargeAmount" + chargeAmount);
                            System.out.println("penalAmount" + penalAmount);
                            if ((objTO.getChkDefaluterYes().equals("") && objTO.getChkDefaluterYes().length() <= 0) && (!CommonUtil.convertObjToStr(objTO.getChkNoTrans()).equals("NOTRANS"))) {
                                if (refundAmount > 0.0) {
                                    refundAmount = objTO.getTxtRefund().doubleValue();
                                    doRefundTrans(map, command);
                                    System.out.println("do refund is executing");
                                }
                                if (chargeAmount > 0.0 || penalAmount > 0 || serviceTax > 0) {
                                    doServiceTaxTrans(map, command);
                                    System.out.println("do doservicetax is executing");
                                }
                            }
                            // doServiceTaxTrans(map, command);
                        } else if (command.equals(CommonConstants.TOSTATUS_UPDATE)) {
                            updateData();
                            transactionDAO.execute(map);
                        } else if (command.equals(CommonConstants.TOSTATUS_DELETE)) {
                            deleteData(map);
//                            transactionDAO.execute(map);
//                            if(objTO.getSurRenew().equalsIgnoreCase("RENEW")){
//                                doServiceTaxTrans(map, command);
//                            }
                        } else {
                            throw new NoCommandException();
                        }
                    }
                }
            } else {
                HashMap authMap = (HashMap) map.get(CommonConstants.AUTHORIZEMAP);
                if (authMap != null) {
                    authorize(authMap, map);
                }
            }
            if (!map.containsKey("LOCKER_DEPOSIT_SI_DAO")) {
                sqlMap.commitTransaction();
            }
  
        } catch (Exception e) {
            destroyObjects();
            sqlMap.rollbackTransaction();
            e.printStackTrace();
            throw e;
        }
        destroyObjects();
        map = null;
        return returnData;
    }

    private void doServiceTaxTrans(HashMap map, String command) throws Exception {
        HashMap acHeads = (HashMap) sqlMap.executeQueryForObject("getLockerAccountHeads", objTO.getProdId());
        ChargesServiceTaxTO chargesServiceTaxTO = new ChargesServiceTaxTO();
        chargesServiceTaxTO.setProd_id(objTO.getProdId());
        if (objTO.getSurRenew().equalsIgnoreCase("RENEW")) {
            chargesServiceTaxTO.setParticulars("Locker Renewal-" + objTO.getLocNum());
        } else if (objTO.getSurRenew().equalsIgnoreCase("RENTCOLLECTION")) {
            chargesServiceTaxTO.setParticulars("Locker Rent Collection-" + objTO.getLocNum());
        }else if (objTO.getSurRenew().equalsIgnoreCase("SURRENDER")) {
            chargesServiceTaxTO.setParticulars("Locker Surrender- " + objTO.getLocNum());
        } else {
            chargesServiceTaxTO.setParticulars("Locker BreakOpen- " + objTO.getLocNum());
        }
        double chargesAmount = CommonUtil.convertObjToDouble(objTO.getCharges()).doubleValue();
        double penalAmount = CommonUtil.convertObjToDouble(objTO.getPenalAmount()).doubleValue();
        double serviceTax = CommonUtil.convertObjToDouble(objTO.getServiceTax());
        if (penalAmount > 0) {
            chargesServiceTaxTO.setPenalAmount(objTO.getPenalAmount());
            chargesServiceTaxTO.setProd_type("GL");
            chargesServiceTaxTO.setCreated_by(CommonUtil.convertObjToStr(map.get(CommonConstants.USER_ID)));
            chargesServiceTaxTO.setStatus("CREATED");
            chargesServiceTaxTO.setCommand(command);
            if (objTO.getSurRenew().equalsIgnoreCase("RENEW") || objTO.getSurRenew().equalsIgnoreCase("SURRENDER") || objTO.getSurRenew().equalsIgnoreCase("RENTCOLLECTION")) {
                chargesServiceTaxTO.setPenalAcctHead(CommonUtil.convertObjToStr(acHeads.get("PENAL_INTEREST_AC_HEAD")));
            } else {
                chargesServiceTaxTO.setPenalAcctHead(CommonUtil.convertObjToStr(acHeads.get("LOC_BRK_AC_HD_YN")));
            }   //SERV_TAX_AC_HD
        }
        if (chargesAmount > 0.0) {
            chargesServiceTaxTO.setAmount(objTO.getCharges());
            chargesServiceTaxTO.setService_tax_amt(objTO.getServiceTax());
            double com = CommonUtil.convertObjToDouble(objTO.getCharges()).doubleValue();
            double tax = com + objTO.getServiceTax().doubleValue();
            chargesServiceTaxTO.setTotal_amt(new Double(tax));
        } else {
            double d = 0.0;
            chargesServiceTaxTO.setService_tax_amt(new Double(d));
            double com = CommonUtil.convertObjToDouble(objTO.getTxtRefund()).doubleValue();
            double tax = com + objTO.getServiceTax().doubleValue();
            chargesServiceTaxTO.setAmount(objTO.getTxtRefund());
            map.put("loccredit", objTO.getTxtRefund());
            chargesServiceTaxTO.setTotal_amt(new Double(tax));
            System.out.println("the amount is" + chargesServiceTaxTO.getTotal_amt());
        }
        chargesServiceTaxTO.setTrans_id(objTO.getLocNum());
        chargesServiceTaxTO.setTrans_dt(objTO.getStatusDt());
        chargesServiceTaxTO.setProd_type("GL");
        chargesServiceTaxTO.setCreated_by(CommonUtil.convertObjToStr(map.get(CommonConstants.USER_ID)));
        chargesServiceTaxTO.setStatus("CREATED");
        chargesServiceTaxTO.setCommand(command);
        if (objTO.getSurRenew().equalsIgnoreCase("RENEW") || objTO.getSurRenew().equalsIgnoreCase("SURRENDER") ||objTO.getSurRenew().equalsIgnoreCase("RENTCOLLECTION")) {

            chargesServiceTaxTO.setAc_Head(CommonUtil.convertObjToStr(acHeads.get("LOC_RENT_AC_HD")));
        } else {
            chargesServiceTaxTO.setAc_Head(CommonUtil.convertObjToStr(acHeads.get("LOC_BRK_AC_HD_YN")));
            System.out.println("account head is" + acHeads.get("LOC_BRK_AC_HD_YN"));
        }
        map.put("ChargesServiceTaxTO", chargesServiceTaxTO);
        map.put("LOCKER_SURRENDER_DAO", "LOCKER_SURRENDER_DAO");
        if(map.containsKey("LOCKER_DEPOSIT_SI_DAO")){//KD-3961
            map.put("FROM_LOCKER_SI_SCREEN", "FROM_LOCKER_SI_SCREEN");
        }
        map.put("LOCKER_SURRENDER_ID", objTO.getRemarks());
        map.put("loccredit", objTO.getTxtRefund());
        HashMap batchMap = chargesServiceTaxDAO.execute(map, false);
        System.out.println("batchMap" + batchMap);
        if (batchMap.containsKey("TRANS_ID")) {
            returnData.put("TRANS_ID", objTO.getRemarks());
            returnData.put("BATCH_ID", batchMap.get("TRANS_ID"));
        }
        if (batchMap.containsKey("SINGLE_TRANS_ID")) {
            returnData.put("SINGLE_TRANS_ID", batchMap.get("SINGLE_TRANS_ID"));
        }
        //System.out.println("returnDatareturnDatareturnData" + returnData);
    }

    private void doRefundTrans(HashMap chargesMap, String command) throws Exception {
        ArrayList transferList = new ArrayList();
        TransferTrans transferTrans = new TransferTrans();
        TxTransferTO transferTo = new TxTransferTO();
        ChargesServiceTaxTO chargesServiceTaxTO = new ChargesServiceTaxTO();
        ArrayList TxTransferTO = new ArrayList();
        HashMap txMap = new HashMap();
        chargesMap.put("LOCKER_SURRENDER_ID", objTO.getRemarks());
        chargesMap.put("LOCKER_SURRENDER_DAO", "LOCKER_SURRENDER_DAO");
        HashMap acHeads = (HashMap) sqlMap.executeQueryForObject("getLockerAccountHeads", objTO.getProdId());
        chargesServiceTaxTO.setProd_id(objTO.getProdId());

        TransferDAO transferDAO;
        ChargesServiceTaxTO objChargesServiceTaxTO = new ChargesServiceTaxTO();
        System.out.println("do refund is executing");
        System.out.println("the charges map is" + chargesMap);
        double d = 0.0;
        objChargesServiceTaxTO.setService_tax_amt(new Double(d));
        objChargesServiceTaxTO.setParticulars("Locker Surrender-" + objTO.getLocNum());
        double com = CommonUtil.convertObjToDouble(objTO.getTxtRefund()).doubleValue();
        double tax = com + objTO.getServiceTax().doubleValue();
        objChargesServiceTaxTO.setAmount(objTO.getTxtRefund());

        // map.put("loccredit",objTO.getTxtRefund());
        objChargesServiceTaxTO.setTotal_amt(new Double(tax));
        System.out.println("the amount is" + chargesServiceTaxTO.getTotal_amt());
        //TransactionTO transactionTO=new TransactionTO();
        // objChargesServiceTaxTO = (ChargesServiceTaxTO)chargesMap.get("ChargesServiceTaxTO");


        String BRANCH_ID = CommonUtil.convertObjToStr(chargesMap.get("BRANCH_CODE"));
        double totalAmt = CommonUtil.convertObjToDouble(objChargesServiceTaxTO.getTotal_amt()).doubleValue();
        double serviceAmt = CommonUtil.convertObjToDouble(objChargesServiceTaxTO.getService_tax_amt()).doubleValue();
        double amount = CommonUtil.convertObjToDouble(objChargesServiceTaxTO.getAmount()).doubleValue();
        objChargesServiceTaxTO.setProd_type("GL");
        objChargesServiceTaxTO.setCreated_by(CommonUtil.convertObjToStr(chargesMap.get(CommonConstants.USER_ID)));
        objChargesServiceTaxTO.setStatus("CREATED");
        objChargesServiceTaxTO.setCommand(command);
        objChargesServiceTaxTO.setProd_id(objTO.getProdId());


        //objChragesServiceTAxTO.setBranchID(BRANCH_ID);
        objChargesServiceTaxTO.setInitiatedBranch(BRANCH_ID);
        transferTrans.setInitiatedBranch(BRANCH_ID);


        transactionDAO = new TransactionDAO(CommonConstants.DEBIT);

        transactionDAO.setInitiatedBranch(_branchCode);
        transactionDAO.setCommandMode(CommonConstants.TOSTATUS_INSERT);
        transactionDAO.setTransactionType(transactionDAO.TRANSFER);
        if (chargesMap.containsKey("TransactionTO")) {
            HashMap transactionDetailsMap = (LinkedHashMap) chargesMap.get("TransactionTO");
            System.out.println("*********" + transactionDetailsMap);
            LinkedHashMap allowedTransDetailsTO = new LinkedHashMap();
            if (transactionDetailsMap.size() > 0 && transactionDetailsMap.containsKey("NOT_DELETED_TRANS_TOs")) {
                allowedTransDetailsTO = (LinkedHashMap) transactionDetailsMap.get("NOT_DELETED_TRANS_TOs");
            }
            transactionTO = (TransactionTO) allowedTransDetailsTO.get(String.valueOf(1));

            chargesMap.put("ChargesServiceTaxTO", objChargesServiceTaxTO);

            System.out.println("objChargesServiceTaxTO " + chargesMap);
            if (transactionTO.getTransType().equals("TRANSFER")) {
                transferTo.setInstrumentNo2("ENTERED_AMOUNT");
                String linkBatchId = CommonUtil.convertObjToStr(transactionTO.getDebitAcctNo());

                transactionDAO.setLinkBatchID(CommonUtil.convertObjToStr(transactionTO.getDebitAcctNo()));
                if (totalAmt > 0) {
                    txMap = new HashMap();
                    if (!transactionTO.getProductType().equals("") && transactionTO.getProductType().equals("GL") && transactionTO.getProductId().equals("")) {
                        txMap.put(TransferTrans.CR_AC_HD, (String) transactionTO.getDebitAcctNo());
                        txMap.put(TransferTrans.CR_PROD_TYPE, TransactionFactory.GL);
                    } else {
                        txMap.put(TransferTrans.CR_ACT_NUM, transactionTO.getDebitAcctNo());
                        txMap.put(TransferTrans.CR_PROD_ID, transactionTO.getProductId());
                        txMap.put(TransferTrans.CR_PROD_TYPE, transactionTO.getProductType());
                    }
                    txMap.put(TransferTrans.CR_BRANCH, BRANCH_ID);
                    txMap.put(TransferTrans.PARTICULARS, objChargesServiceTaxTO.getParticulars());
                    if (transactionTO.getProductType().equals("OA")) {
                        txMap.put("TRANS_MOD_TYPE", "OA");
                    } else if (transactionTO.getProductType().equals("AB")) {
                        txMap.put("TRANS_MOD_TYPE", "AB");
                    } else if (transactionTO.getProductType().equals("SA")) {
                        txMap.put("TRANS_MOD_TYPE", "SA");
                    } else if (transactionTO.getProductType().equals("TL")) {
                        txMap.put("TRANS_MOD_TYPE", "TL");
                    } else if (transactionTO.getProductType().equals("AD")) {
                        txMap.put("TRANS_MOD_TYPE", "AD");
                    } else {
                        txMap.put("TRANS_MOD_TYPE", "GL");
                    }
                     //txMap.put("TRANS_MOD_TYPE", CommonConstants.LOCKER_TRANSMODE_TYPE);
                    if (chargesMap.containsKey("generateSingleTransId")) {
                        txMap.put("generateSingleTransId", chargesMap.get("generateSingleTransId"));
                    }
                    System.out.println("txMap : " + txMap + "totalAmt :" + totalAmt);
                    transferTo = transactionDAO.addTransferCreditLocal(txMap, amount);

                    transferTo.setTransId("-");
                    transferTo.setBatchId("-");
                    transferTo.setInitiatedBranch(BRANCH_ID);
                    transferTo.setStatusBy(CommonConstants.TTSYSTEM);
                    transferTo.setInitTransId(CommonUtil.convertObjToStr(chargesMap.get("USER_ID")));
                    if (!transactionTO.getProductType().equals("") && !transactionTO.getProductType().equals("GL") && !transactionTO.getProductId().equals("")) {
                        transferTo.setLinkBatchId(transactionTO.getDebitAcctNo());
                    }
                    transferTo.setScreenName((String) chargesMap.get(CommonConstants.SCREEN)); // 23-08-2016
                    System.out.println("transferTo List 1 : " + transferTo);
                    TxTransferTO.add(transferTo);
                }

                if (amount > 0) {
                    txMap = new HashMap();
                    transferTo = new TxTransferTO();
                    objChargesServiceTaxTO.setAc_Head(CommonUtil.convertObjToStr(acHeads.get("LOC_RENT_AC_HD")));
                    if (!objChargesServiceTaxTO.getProd_type().equals("") && objChargesServiceTaxTO.getProd_type().equals("GL")) {
                        txMap.put(TransferTrans.DR_PROD_TYPE, TransactionFactory.GL);
                    } else {
                        txMap.put(TransferTrans.DR_PROD_ID, objChargesServiceTaxTO.getProd_id());
                        txMap.put(TransferTrans.DR_ACT_NUM, objChargesServiceTaxTO.getAcct_num());
                        txMap.put(TransferTrans.DR_PROD_TYPE, TransactionFactory.DEPOSITS);
                    }
                    txMap.put(TransferTrans.DR_AC_HD, (String) objChargesServiceTaxTO.getAc_Head());
                    txMap.put(TransferTrans.PARTICULARS, objChargesServiceTaxTO.getParticulars());
                    txMap.put(TransferTrans.DR_BRANCH, BRANCH_ID);
//                    if (transactionTO.getProductType().equals("OA")) {
//                        txMap.put("TRANS_MOD_TYPE", "OA");
//                    } else if (transactionTO.getProductType().equals("AB")) {
//                        txMap.put("TRANS_MOD_TYPE", "AB");
//                    } else if (transactionTO.getProductType().equals("SA")) {
//                        txMap.put("TRANS_MOD_TYPE", "SA");
//                    } else if (transactionTO.getProductType().equals("TL")) {
//                        txMap.put("TRANS_MOD_TYPE", "TL");
//                    } else if (transactionTO.getProductType().equals("AD")) {
//                        txMap.put("TRANS_MOD_TYPE", "AD");
//                    } else {
//                        txMap.put("TRANS_MOD_TYPE", "GL");
//                    }
                    txMap.put("TRANS_MOD_TYPE", CommonConstants.LOCKER_TRANSMODE_TYPE);
                    if (chargesMap.containsKey("generateSingleTransId")) {
                        txMap.put("generateSingleTransId", chargesMap.get("generateSingleTransId"));
                    }
                    System.out.println("txMap : " + txMap + "amount :" + amount);
                    transferTo = transactionDAO.addTransferDebitLocal(txMap, amount);

                    transferTo.setTransId("-");
                    transferTo.setBatchId("-");
                    transferTo.setInitiatedBranch(BRANCH_ID);
                    transferTo.setStatusBy(CommonConstants.TTSYSTEM);
                    transferTo.setInitTransId(CommonUtil.convertObjToStr(chargesMap.get("USER_ID")));
                    if (!transactionTO.getProductType().equals("") && !transactionTO.getProductType().equals("GL") && !transactionTO.getProductId().equals("")) {
                        transferTo.setLinkBatchId(transactionTO.getDebitAcctNo());
                    }
                    transferTo.setScreenName((String) chargesMap.get(CommonConstants.SCREEN)); // 23-08-2016
                    System.out.println("transferTo List 3 : " + transferTo);
                    TxTransferTO.add(transferTo);
                }
                transferDAO = new TransferDAO();
                chargesMap.put("COMMAND", chargesMap.get("MODE"));
                chargesMap.put("TxTransferTO", TxTransferTO);
                System.out.println("^^&&&&&&" + chargesMap);
                HashMap transMap = new HashMap();
//                if (transactionTO.getProductType().equals("OA")) {
//                    chargesMap.put("TRANS_MOD_TYPE", "OA");
//                } else if (transactionTO.getProductType().equals("AB")) {
//                    chargesMap.put("TRANS_MOD_TYPE", "AB");
//                } else if (transactionTO.getProductType().equals("SA")) {
//                    chargesMap.put("TRANS_MOD_TYPE", "SA");
//                } else if (transactionTO.getProductType().equals("TL")) {
//                    chargesMap.put("TRANS_MOD_TYPE", "TL");
//                } else if (transactionTO.getProductType().equals("AD")) {
//                    chargesMap.put("TRANS_MOD_TYPE", "AD");
//                } else {
//                    chargesMap.put("TRANS_MOD_TYPE", "GL");
//                }
//              chargesMap.put("TRANS_MOD_TYPE", CommonConstants.LOCKER_TRANSMODE_TYPE);
                transMap = transferDAO.execute(chargesMap, false);
                System.out.println("transMap AFTER : " + transMap + "totalAmt :" + totalAmt);
                objChargesServiceTaxTO.setTrans_id(CommonUtil.convertObjToStr(transMap.get("TRANS_ID")));
                transactionTO.setChequeNo("SERVICE_TAX");
                transactionDAO.setBatchId(getLockerIssId());
                transactionDAO.setBatchDate(currDt);
                transactionDAO.execute(chargesMap);
//                HashMap remitMap = new HashMap();
//              List  lst = sqlMap.executeQueryForList("getSelectMaxTransIdForCash",null);
//                if(lst!=null && lst.size()>0){
//                    remitMap = (HashMap)lst.get(0);
//                      remitMap.put("BATCH_ID", chargesMap.get("LOCKER_SURRENDER_ID"));
//                      remitMap.put("BATCH_DT", currDt);
//                    //                    remitMap.put("TRANS_DT",currDt);
//                    //                    remitMap.put("INITIATED_BRANCH",BRANCH_ID);
//                    System.out.println("remitMap : " + remitMap);
//                    sqlMap.executeUpdate("updateRemitIssueTableCashBatchId", remitMap);
//                }
                HashMap linkBatchMap = new HashMap();
                linkBatchMap.put("LINK_BATCH_ID", chargesMap.get("LOCKER_SURRENDER_ID"));
                linkBatchMap.put("BATCH_ID", transMap.get("TRANS_ID"));
                linkBatchMap.put("TRANS_DT", currDt.clone());
                linkBatchMap.put("INITIATED_BRANCH", _branchCode);
                sqlMap.executeUpdate("updateLinkBatchIdTransfer", linkBatchMap);
                linkBatchMap = null;
                transMap = null;


            } else if (transactionTO.getTransType().equals("CASH")) {
                transactionTO.setChequeNo("SERVICE_TAX");
//           chargesMap.put("CHARGES_SERVICE_TAX",new Double(totalAmt));
                //          chargesMap.put("CHARGES_SERVICE_TAX_SCREEN","CHARGES_SERVICE_TAX_SCREEN");
                objChargesServiceTaxTO.setAc_Head(CommonUtil.convertObjToStr(acHeads.get("LOC_RENT_AC_HD")));
                //transactionDAO.setLinkBatchID(CommonUtil.convertObjToStr(transactionTO.getDebitAcctNo()));
                chargesMap.put("CashTransactionTO", getCashTransTO(chargesMap));
                chargesMap.put("SELECTED_BRANCH_ID", BRANCH_ID);
                CashTransactionDAO cashTransactionDAO = new CashTransactionDAO();
                //chargesMap.put("TRANS_MOD_TYPE", CommonConstants.LOCKER_TRANSMODE_TYPE);
                if (chargesMap.containsKey("generateSingleTransId")) {
                    chargesMap.put("SINGLE_TRANS_ID", chargesMap.get("generateSingleTransId"));
                }
                HashMap cashMap = cashTransactionDAO.execute(chargesMap, false);
                System.out.println("cashMap :" + cashMap);
                command = CommonUtil.convertObjToStr(objChargesServiceTaxTO.getCommand());
                transactionTO.setBatchId(CommonUtil.convertObjToStr(cashMap.get("TRANS_ID")));
                objChargesServiceTaxTO.setTrans_id(CommonUtil.convertObjToStr(cashMap.get("TRANS_ID")));
                transactionTO.setBatchDt(objChargesServiceTaxTO.getCreated_dt());
                System.out.println("the locker surrender id is" + chargesMap.get("LOCKER_SURRENDER_ID"));
                transactionDAO.setLinkBatchID(CommonUtil.convertObjToStr(chargesMap.get("LOCKER_SURRENDER_ID")));
                transactionDAO.setBatchId(getLockerIssId());
                transactionDAO.setBatchDate(currDt);
                transactionDAO.execute(chargesMap);
//                HashMap debitMap = new HashMap();
//                List lst = sqlMap.executeQueryForList("getSelectMaxTransIdForCash",null);
//                if(lst!=null && lst.size()>0){
//                    debitMap = (HashMap)lst.get(0);
//                    if(!chargesMap.containsKey("LOCKER_SURRENDER_DAO")){
//                        debitMap.put("BATCH_ID", cashMap.get("TRANS_ID"));
//                    System.out.println("if block");
//                    }
//                    else
//                        debitMap.put("BATCH_ID", chargesMap.get("LOCKER_SURRENDER_ID"));
//                    debitMap.put("BATCH_DT", currDt);
//                    debitMap.put("TRANS_DT",currDt);
//                    debitMap.put("INITIATED_BRANCH",BRANCH_ID);
//                    sqlMap.executeUpdate("updateRemitIssueTableCashBatchId", debitMap);
//                    debitMap = null;
//                }
                HashMap linkBatchMap = new HashMap();
                //                linkBatchMap.put("LINK_BATCH_ID",cashMap.get("TRANS_ID"));
                if (!chargesMap.containsKey("LOCKER_SURRENDER_DAO")) {
                    linkBatchMap.put("LINK_BATCH_ID", cashMap.get("TRANS_ID"));

                } else {
                    linkBatchMap.put("LINK_BATCH_ID", chargesMap.get("LOCKER_SURRENDER_ID"));
                }
                System.out.println("the locker surrender id is" + chargesMap.get("LOCKER_SURRENDER_ID"));
                linkBatchMap.put("TRANS_ID", cashMap.get("TRANS_ID"));
                linkBatchMap.put("TRANS_DT", currDt);
                linkBatchMap.put("INITIATED_BRANCH", BRANCH_ID);
                sqlMap.executeUpdate("updateLinkBatchIdCash", linkBatchMap);
                linkBatchMap = null;
            }
            objChargesServiceTaxTO.setCreated_dt(currDt);
            objChargesServiceTaxTO.setTrans_dt(currDt);
            objChargesServiceTaxTO.setParticulars(objChargesServiceTaxTO.getParticulars());
            objChargesServiceTaxTO.setBranchCode(BRANCH_ID);
            sqlMap.executeUpdate("insertChargesServiceTaxTO", objChargesServiceTaxTO);
        }
    }

    private CashTransactionTO getCashTransTO(HashMap chargesMap) {
        System.out.println("chargesMap :" + chargesMap);
        CashTransactionTO objCashTO = null;
        ChargesServiceTaxTO objChargesServiceTaxTO = new ChargesServiceTaxTO();
        objChargesServiceTaxTO = (ChargesServiceTaxTO) chargesMap.get("ChargesServiceTaxTO");
        System.out.println("objCashTransactionTO :" + objChargesServiceTaxTO);
        if (objChargesServiceTaxTO.getCommand().equals(CommonConstants.TOSTATUS_INSERT)) {
            objCashTO = new CashTransactionTO();
            objCashTO.setTransId("");
            objCashTO.setAcHdId(objChargesServiceTaxTO.getAc_Head());
            objCashTO.setProdId(objChargesServiceTaxTO.getProd_id());
            objCashTO.setProdType(objChargesServiceTaxTO.getProd_type());
            objCashTO.setInpAmount(objChargesServiceTaxTO.getAmount());
            objCashTO.setAmount(objChargesServiceTaxTO.getAmount());
            objCashTO.setTransType(CommonConstants.DEBIT);
            objCashTO.setInitTransId(CommonUtil.convertObjToStr(chargesMap.get("USER_ID")));
            objCashTO.setBranchId(objTO.getBranchID());
            objCashTO.setStatusBy(CommonUtil.convertObjToStr(chargesMap.get("USER_ID")));
            objCashTO.setStatusDt(objChargesServiceTaxTO.getCreated_dt());
            objCashTO.setInstrumentNo2("ENTERED_AMOUNT");
            objCashTO.setParticulars(objChargesServiceTaxTO.getParticulars());
            objCashTO.setInitiatedBranch(objTO.getBranchID());
            objCashTO.setInitChannType(CommonConstants.CASHIER);
            objCashTO.setCommand(objChargesServiceTaxTO.getCommand());
            objCashTO.setTransModType(CommonConstants.LOCKER_TRANSMODE_TYPE);
            objCashTO.setScreenName((String) chargesMap.get(CommonConstants.SCREEN)); // 23-08-2016
            System.out.println("objCashTO 1st one:" + objCashTO);
//            System.out.println("objCashTO transaction :"+objCashTO);

        }
        objChargesServiceTaxTO = null;
        return objCashTO;
    }

    private void authorize(HashMap map, HashMap map2) throws Exception {

        String status = (String) map.get(CommonConstants.AUTHORIZESTATUS);
        ArrayList selectedList = (ArrayList) map.get(CommonConstants.AUTHORIZEDATA);
        System.out.println("the map is" + map);
        String rentyear = CommonUtil.convertObjToStr(map.get("year"));        
        java.util.Date date = DateUtil.getDateMMDDYYYY(rentyear);
        java.util.Date exp = (java.util.Date) currDt.clone();
        exp.setDate(date.getDate());
        exp.setMonth(date.getMonth());
        exp.setYear(date.getYear());

        System.out.println("the expir year is" + rentyear);
        HashMap dataMap;
        String lockerNo;
        String issueId;
        String ServiceTransID = "";
        String collectRentMonth = "";
        String collectRentYear = "";
        for (int i = 0; i < selectedList.size(); i++) {
            dataMap = (HashMap) selectedList.get(i);
            //System.out.println("dataMap###"+dataMap);
            lockerNo = CommonUtil.convertObjToStr(dataMap.get("LOCKER_NUM"));
            issueId = CommonUtil.convertObjToStr(dataMap.get("ISSUE_ID"));

            //System.out.println("remitPayId###"+remitPayId);
            dataMap.put(CommonConstants.STATUS, status);
            dataMap.put(CommonConstants.USER_ID, map.get(CommonConstants.USER_ID));
            dataMap.put(CommonConstants.BRANCH_ID, _branchCode);
            dataMap.put("TODAY_DT", currDt);
            // dataMap.put("year",map.get("year").toString());
            //System.out.println("datamap %%^^^^^^"+dataMap);
            System.out.println("datamap %%^^^^^^" + dataMap);
            sqlMap.executeUpdate("authorizeLockerSur", dataMap);
//             if(status.equalsIgnoreCase(CommonConstants.STATUS_AUTHORIZED)){

//                 if(dataMap.get("SUR_OR_RENEW").equals("RENEW")) {

            if (map2.containsKey("TransactionTO")) {
                HashMap transactionDetailsMap = (LinkedHashMap) map2.get("TransactionTO");
                LinkedHashMap allowedTransDetailsTO = new LinkedHashMap();
                if (transactionDetailsMap.size() > 0 && transactionDetailsMap.containsKey("NOT_DELETED_TRANS_TOs")) {
                    allowedTransDetailsTO = (LinkedHashMap) transactionDetailsMap.get("NOT_DELETED_TRANS_TOs");

                    if (allowedTransDetailsTO != null) {

                        TransactionTO transactionTO = (TransactionTO) allowedTransDetailsTO.get(String.valueOf(1));
                        if (transactionTO.getTransType().equals("TRANSFER")) {
                            System.out.println("transactionTO.getTransType() :" + transactionTO.getTransType());
                            HashMap authorizeMap = (HashMap) map2.get("AUTHORIZEMAP");
//                             authorizeMap =(HashMap) map2.get("AUTHORIZEMAP");
                            System.out.println("map2 :" + map2);
                            HashMap cashAuthMap = new HashMap();
                            String authorizeStatus = CommonUtil.convertObjToStr(authorizeMap.get("AUTHORIZESTATUS"));
                            HashMap transMap = new HashMap();
                            transMap.put("LINK_BATCH_ID", transactionTO.getBatchId());
                            transMap.put("TODAY_DT", currDt);
                            List transId = sqlMap.executeQueryForList("getTransIDForLockerSurrenderTrans", transMap);
                            if (transId != null && transId.size() > 0) {
//                                 transMap= null;
                                transMap = (HashMap) transId.get(0);
                                ServiceTransID = CommonUtil.convertObjToStr(transMap.get("TRANS_ID"));
                            }
//                             String linkBatchId = "";
//                             if(!transactionTO.getProductType().equals("") && !transactionTO.getProductType().equals("GL"))
//                                 linkBatchId = CommonUtil.convertObjToStr(transactionTO.getDebitAcctNo());
//                             else
                            String linkBatchId = CommonUtil.convertObjToStr(transactionTO.getBatchId());
                            cashAuthMap.put(CommonConstants.BRANCH_ID, map2.get(CommonConstants.BRANCH_ID));
                            cashAuthMap.put(CommonConstants.USER_ID, map2.get(CommonConstants.USER_ID));
                            if (map2.containsKey("DEBIT_LOAN_TYPE")) {
                                cashAuthMap.put("DEBIT_LOAN_TYPE", "DP");
                            }
                            if(transactionTO.getProductType().equals("AD")){//Added By Revathi.L
                                cashAuthMap.put("DEBIT_LOAN_TYPE", "DP");
                            }
                            transactionDAO = new TransactionDAO(CommonConstants.DEBIT);
                            System.out.println(" cashAuthMap :" + cashAuthMap + "linkBatchId :" + linkBatchId + "authorizeStatus :" + authorizeStatus);
                            TransactionDAO.authorizeCashAndTransferWithoutRemitIssueDet(linkBatchId, authorizeStatus, cashAuthMap);
                            System.out.println("transactionTO.getTransType() :" + transactionTO.getTransType());
                            authorizeMap = null;
                            cashAuthMap = null;
                        } else {
                            System.out.println("transactionTO.getTransType() :" + transactionTO.getTransType());
                            CashTransactionDAO cashTransactionDAO = new CashTransactionDAO();
                            ArrayList arrList = new ArrayList();
                            HashMap singleAuthorizeMap = new HashMap();;
                            singleAuthorizeMap.put("STATUS", status);
                            HashMap transMap = new HashMap();
                            transMap.put("LINK_BATCH_ID", transactionTO.getBatchId());
                            transMap.put("TODAY_DT", currDt);
                            List transId = sqlMap.executeQueryForList("getTransIDForLockerSurrender", transMap);
                            if (transId != null && transId.size() > 0) {
//                                 transMap= null;
                                transMap = (HashMap) transId.get(0);
                                singleAuthorizeMap.put("TRANS_ID", transMap.get("TRANS_ID"));
                                ServiceTransID = CommonUtil.convertObjToStr(transMap.get("TRANS_ID"));
                                //                singleAuthorizeMap.put("TRANS_ID", CommonUtil.convertObjToStr(transactionTO.getBatchId()));
                                singleAuthorizeMap.put("USER_ID", map2.get("USER_ID"));
                                arrList.add(singleAuthorizeMap);
                                String branchCode = CommonUtil.convertObjToStr(map2.get("BRANCH_CODE"));
                                String userId = CommonUtil.convertObjToStr(map2.get("USER_ID"));
                                System.out.println("before making new DAO map2 :" + map2);
                                map2 = new HashMap();
                                map2.put("SCREEN", "Cash");
                                map2.put("USER_ID", userId);
                                map2.put("SELECTED_BRANCH_ID", branchCode);
                                map2.put("BRANCH_CODE", branchCode);
                                map2.put("MODULE", "Transaction");
                                HashMap data1Map = new HashMap();
                                data1Map.put(CommonConstants.AUTHORIZESTATUS, status);
                                data1Map.put(CommonConstants.AUTHORIZEDATA, arrList);
                                data1Map.put("DAILY", "DAILY");
                                map2.put(CommonConstants.AUTHORIZEMAP, data1Map);                                
                                System.out.println("before entering DAO map2 :" + map2);
                                cashTransactionDAO.execute(map2, false);
                                cashTransactionDAO = null;
                                map2 = null;
                                data1Map = null;
                            }
                        }
                    }
                }
            }

            HashMap paramMap = new HashMap();
            paramMap.put("SERVICE_ID", ServiceTransID);
            paramMap.put("SERVICE_AUTH_STATUS", status);
            paramMap.put("SERVICE_AUTH_BY", dataMap.get(CommonConstants.USER_ID));
            paramMap.put("SERVICE_AUTH_DT", currDt);
            sqlMap.executeUpdate("AuthSerTaxFromLockerSurrender", paramMap);
//                 }

            if (status.equalsIgnoreCase(CommonConstants.STATUS_AUTHORIZED)) {
                HashMap param = new HashMap();
                if (dataMap.get("SUR_OR_RENEW").equals("SURRENDER")) {
                    param.put("LOC_NO", dataMap.get("LOCKER_NUM"));
                    param.put("CUST_ID", dataMap.get("CUST_ID"));
                    param.put("PROD_ID", dataMap.get("PROD_ID"));
                    param.put("CLOSED_BY", dataMap.get(CommonConstants.USER_ID));
                    param.put("CLOSED_DT", dataMap.get("TODAY_DT"));
                    param.put("LOCKER_STATUS", "AVAILABLE");
                    if (CommonUtil.convertObjToStr(dataMap.get("DEFAULTER")).equals("DEFAULTER")) {
                        param.put("LOCKER_STATUS_ID", "DEFAULTER");
                    } else {
                        param.put("LOCKER_STATUS_ID", "CLOSED");
                    }
                    if(dataMap.get("SUR_OR_RENEW").equals("DEFAULTER")){
                    	param.put("LOCKER_STATUS_ID", "DEFAULTER");    
                    }
                    sqlMap.executeUpdate("updateLockerStatus", param);
                    sqlMap.executeUpdate("updateLockerMasterStatus", param);
                } else if (dataMap.get("SUR_OR_RENEW").equals("RENEW")) {
                    // Added by nithya for 5378 on 22-11-2016
                    if(map.containsKey("COLLECT_RENT_MONTH") && map.get("COLLECT_RENT_MONTH") != null){
                       collectRentMonth = CommonUtil.convertObjToStr(map.get("COLLECT_RENT_MONTH"));  
                    }
                    if(map.containsKey("COLLECT_RENT_MONTH") && map.get("COLLECT_RENT_MONTH") != null){
                       collectRentYear = CommonUtil.convertObjToStr(map.get("COLLECT_RENT_YEAR"));  
                    }                      
                    // End
                    param.put("LOC_NO", dataMap.get("LOCKER_NUM"));
                    param.put("CUST_ID", dataMap.get("CUST_ID"));
                    param.put("PROD_ID", dataMap.get("PROD_ID"));
                    List expList = sqlMap.executeQueryForList("getExpiryDtLockerMaster", param);
                    if (expList != null && expList.size() > 0) {
                        HashMap expMap = new HashMap();
                        expMap = (HashMap) expList.get(0);
                        java.util.Date dt = (java.util.Date) expMap.get("EXP_DT");
                        // java.util.Date date=(java.util.Date)expMap.get("EXP_DT");
                        //int day=date.getDay();
                        //int mon=date.getMonth();
                        //java.util.Date dat=(java.util.Date)currDt.clone();
                        // dat.setDate(date.getDate());
                        // dat.setMonth(date.getMonth());
                        // dat.setYear(rentyear-1900);
                        // param.put("EXP_DT", DateUtil.addDays(dt,365);

                        param.put("EXP_DT", exp);
                        param.put("COLLECT_RENT_YEAR", collectRentYear);
                        param.put("COLLECT_RENT_MONTH", collectRentMonth);
                        System.out.println("the exp is " + exp);
                        System.out.println("the param is" + param);
                    }
                    sqlMap.executeUpdate("updateLockerMasterStatusRenew", param);
                } else if (dataMap.get("SUR_OR_RENEW").equals("BREAKOPEN")) {
                    param.put("LOC_NO", dataMap.get("LOCKER_NUM"));
                    param.put("CUST_ID", dataMap.get("CUST_ID"));
                    param.put("PROD_ID", dataMap.get("PROD_ID"));
                    param.put("CLOSED_BY", dataMap.get(CommonConstants.USER_ID));
                    param.put("CLOSED_DT", dataMap.get("TODAY_DT"));
                    param.put("LOCKER_STATUS", "BREAKOPEN");
                    param.put("LOCKER_STATUS_ID", "CLOSED");

                    sqlMap.executeUpdate("updateLockerStatus", param);
                    sqlMap.executeUpdate("updateLockerMasterStatus", param);
                }else if (dataMap.containsKey("SUR_OR_RENEW") && dataMap.get("SUR_OR_RENEW").equals("RENTCOLLECTION")) {   // Added by nithya on 25-02-2020 for KD-1496               
                    if(map.containsKey("COLLECT_RENT_MONTH") && map.get("COLLECT_RENT_MONTH") != null){
                       collectRentMonth = CommonUtil.convertObjToStr(map.get("COLLECT_RENT_MONTH"));  
                    }
                    if(map.containsKey("COLLECT_RENT_MONTH") && map.get("COLLECT_RENT_MONTH") != null){
                       collectRentYear = CommonUtil.convertObjToStr(map.get("COLLECT_RENT_YEAR"));  
                    }
                    param.put("LOC_NO", dataMap.get("LOCKER_NUM"));
                    param.put("CUST_ID", dataMap.get("CUST_ID"));
                    param.put("PROD_ID", dataMap.get("PROD_ID")); 
                    param.put("EXP_DT", exp);
                    param.put("COLLECT_RENT_YEAR", collectRentYear);
                    param.put("COLLECT_RENT_MONTH", collectRentMonth);
                    sqlMap.executeUpdate("updateLockerMasterStatusRenew", param);                        
                }
                // authorize service tax table
                if (map.containsKey("SER_TAX_AUTH")) {
                    HashMap serMapAuth = (HashMap) map.get("SER_TAX_AUTH");
                    sqlMap.executeUpdate("authorizeServiceTaxDetails", serMapAuth);
                }
                // end
                param = null;
            }
        }
    }

    private void insertData(HashMap obj, String command) throws Exception {
        objTO.setStatus(CommonConstants.STATUS_CREATED);
//        objTO.setOptMode(getLockerSurrenderId());
//        objTO.setRemitPayDt(currDt);
//        String Remit_Pay_ID = getRemitPayID();
//        objTO.setRemitPayId(Remit_Pay_ID);
//        Remit_Pay_ID = null;
        logTO.setData(objTO.toString());
        logTO.setPrimaryKey(objTO.getKeyData());
        logTO.setStatus(objTO.getCommand());
        //        objTO.setCreatedDt(currDt);
        setLockerIssId(getLockerSurrenderId());
        objTO.setRemarks(getLockerIssId());
        String s = "BREAKOPEN";


        if (objTO.getSurRenew().equals(s)) {

            objTO.setBreakOpenDt(objTO.getSurDt());

        }
        // Added by nithya on 13-12-2016
        if(objTO.getSurRenew().equalsIgnoreCase("SURRENDER")){
            objTO.setLblNewExpDateVal(currDt);
        }
        Date newLckrExpDate = DateUtil.getDateMMDDYYYY(CommonUtil.convertObjToStr(objTO.getLblNewExpDateVal()));
        objTO.setLblNewExpDateVal(setProperDtFormat(newLckrExpDate));// for solving date time stamp cast issue
        // End
        sqlMap.executeUpdate("insertLockerSurTO", objTO);
        transactionDAO.setBatchId(getLockerIssId());
        transactionDAO.setBatchDate(currDt);
        transactionDAO.setLinkBatchID(getLockerIssId());
        System.out.println("##########transactionDAO" + transactionDAO);
//        insertAccHead(obj,command);
        logDAO.addToLog(logTO);
    }

    private String getLockerSurrenderId() throws Exception {
        final IDGenerateDAO dao = new IDGenerateDAO();
        final HashMap where = new HashMap();
        where.put(CommonConstants.MAP_WHERE, "LOCKER_SURRENDER_ID");
        return (String) (dao.executeQuery(where)).get(CommonConstants.DATA);
    }

    private HashMap createMap(HashMap map, String branchID) {
        map.put(TransferTrans.CR_PROD_TYPE, TransactionFactory.GL);
        map.put(TransferTrans.CR_BRANCH, branchID);
        map.put(TransferTrans.CURRENCY, "INR");
        return map;
    }

    public void insertAccHead(HashMap obj, String command) throws Exception {
        System.out.println("######iside insertAccHead");
        String branchID = objTO.getBranchID();
        HashMap acHeads = (HashMap) sqlMap.executeQueryForObject("getLockerAccountHeads", objTO.getProdId());
        HashMap txMap = new HashMap();
        txMap = createMap(txMap, branchID);
        //TransactionTO objTransactionTO;
        TxTransferTO transferTo = null;
        LinkedHashMap TransactionDetailsMap = (LinkedHashMap) obj.get("TransactionTO");
        if (TransactionDetailsMap.size() > 0) {
            if (TransactionDetailsMap.containsKey("NOT_DELETED_TRANS_TOs")) {
                LinkedHashMap allowedTransDetailsTO = (LinkedHashMap) TransactionDetailsMap.get("NOT_DELETED_TRANS_TOs");
                for (int i = 1, j = allowedTransDetailsTO.size(); i <= j; i++) {
                    objTransactionTO = (TransactionTO) allowedTransDetailsTO.get(String.valueOf(i));

                    Double tempAmt = objTO.getCharges();
                    if (objTransactionTO.getTransType().equals("TRANSFER")) {
                        if (objTO.getCharges() != null && CommonUtil.convertObjToDouble(objTO.getCharges()).doubleValue() != 0) {
                            System.out.println("######getchargenewmode");
                            System.out.println("######iside new mode");
                            txMap = new HashMap();
                            ArrayList transferList = new ArrayList(); // for local transfer
                            //                        txMap.put(TransferTrans.DR_AC_HD, (String)acHeads.get("LOC_RENT_AC_HD"));
                            //                        txMap.put(TransferTrans.DR_BRANCH, branchID);
                            //                        txMap.put(TransferTrans.CURRENCY, "INR");
                            //                        txMap.put(TransferTrans.DR_PROD_TYPE, TransactionFactory.GL);

                            if (!objTransactionTO.getProductType().equals("GL")) {
                                txMap.put(TransferTrans.DR_AC_HD, null);
                                txMap.put(TransferTrans.DR_ACT_NUM, objTransactionTO.getDebitAcctNo());
                                txMap.put(TransferTrans.DR_PROD_ID, objTransactionTO.getProductId());
                            } else {
                                txMap.put(TransferTrans.DR_AC_HD, objTransactionTO.getDebitAcctNo());
                            }
                            txMap.put(TransferTrans.DR_PROD_TYPE, objTransactionTO.getProductType());
                            txMap.put(TransferTrans.DR_BRANCH, branchID);

                            txMap.put(TransferTrans.CR_AC_HD, (String) acHeads.get("LOC_RENT_AC_HD"));
                            txMap.put(TransferTrans.CR_BRANCH, branchID);
                            txMap.put(TransferTrans.CURRENCY, "INR");
                            txMap.put(TransferTrans.CR_PROD_TYPE, TransactionFactory.GL);

                            txMap.put(TransferTrans.PARTICULARS, objTO.getLocNum() + " Chrg");
                            transferTo = transactionDAO.addTransferDebitLocal(txMap, CommonUtil.convertObjToDouble(objTO.getCharges()).doubleValue());
                            transferList.add(transferTo);
                            //                         txMap.put(TransferTrans.PARTICULARS, objTO.getInstrumentType());
                            txMap.put(TransferTrans.PARTICULARS, objTO.getLocNum() + " Chrg");
                            transferTo = transactionDAO.addTransferCreditLocal(txMap, CommonUtil.convertObjToDouble(objTO.getCharges()).doubleValue());
                            transferList.add(transferTo);
                            transactionDAO.doTransferLocal(transferList, branchID);
                        }
                        objTO.setCharges(tempAmt);
                    } else if (objTransactionTO.getTransType().equals("CASH")) {
                        TransactionTO transTO = new TransactionTO();
                        ArrayList cashList = new ArrayList();
                        double transAmt = CommonUtil.convertObjToDouble(objTO.getCharges()).doubleValue();
                        transactionDAO.setCommandMode(CommonConstants.TOSTATUS_INSERT);
                        txMap.put(TransferTrans.CR_AC_HD, (String) acHeads.get("LOC_RENT_AC_HD"));  // credit to Exchange Charge account head......
                        //                    transAmt = CommonUtil.convertObjToDouble(objRemittanceIssueTO.getAmount()).doubleValue();
                        txMap.put(TransferTrans.PARTICULARS, "rent");
                        //                    transactionDAO.setTransType(CommonConstants.CREDIT);
                        transactionDAO.addTransferCredit(txMap, transAmt);
                        transactionDAO.deleteTxList();
                        transTO.setCommand(CommonConstants.TOSTATUS_INSERT);
                        transTO.setTransType("CASH");
                        transTO.setBatchId(getLockerIssId());
                        transTO.setTransAmt(new Double(transAmt));
                        cashList.add(transTO);
                        transactionDAO.addCashList(cashList);
                        transactionDAO.doTransfer();
                    }
                }
            }
        }
    }

    public HashMap executeQuery(HashMap obj) throws Exception {
        _branchCode = (String) obj.get(CommonConstants.BRANCH_ID);
        currDt = ServerUtil.getCurrentDate(_branchCode);
        return getData(obj);
    }

    private void destroyObjects() {
        objTO = null;
        logDAO = null;
        logTO = null;
    }

    /**
     * Getter for property lockerIssId.
     *
     * @return Value of property lockerIssId.
     */
    public java.lang.String getLockerIssId() {
        return lockerIssId;
    }

    /**
     * Setter for property lockerIssId.
     *
     * @param lockerIssId New value of property lockerIssId.
     */
    public void setLockerIssId(java.lang.String lockerIssId) {
        this.lockerIssId = lockerIssId;
    }
    
    // Added by nithya
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
}
