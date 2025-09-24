/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition.
 **
 *
 * ShareProductDAO.java
 *
 * Created on Wed Nov 24 16:51:38 GMT+05:30 2004
 */
package com.see.truetransact.serverside.termloan.guarantee;

import java.util.List;
import java.util.HashMap;
import java.util.ArrayList;


import com.ibatis.db.sqlmap.SqlMap;
import com.see.truetransact.serverutil.ServerUtil;
import com.see.truetransact.serverside.transaction.common.TransactionFactory;
import com.see.truetransact.serverside.common.transaction.TransactionDAO;
//import com.see.truetransact.serverside.transaction.common;

import com.see.truetransact.commonutil.CommonConstants;
import com.see.truetransact.commonutil.NoCommandException;
import com.see.truetransact.serverside.common.idgenerate.IDGenerateDAO;
import com.see.truetransact.serverside.common.transaction.TransferTrans;
import com.see.truetransact.commonutil.CommonUtil;
import com.see.truetransact.serverside.TTDAO;
import com.see.truetransact.serverutil.ServerConstants;
import com.see.truetransact.servicelocator.ServiceLocator;
import com.see.truetransact.serverexception.TransRollbackException;
import com.see.truetransact.serverexception.ServiceLocatorException;
import com.see.truetransact.serverside.common.log.LogDAO;
import com.see.truetransact.transferobject.common.log.LogTO;

import com.see.truetransact.transferobject.termloan.guarantee.GuaranteeTransTO;
import com.see.truetransact.transferobject.termloan.guarantee.GuaranteeMasterTO;
import com.see.truetransact.transferobject.product.share.ShareProductLoanTO;
import com.see.truetransact.transferobject.common.transaction.TransactionTO;
import com.see.truetransact.transferobject.common.transaction.TransactionTO;
import com.see.truetransact.transferobject.transaction.cash.CashTransactionTO;
import com.see.truetransact.serverside.transaction.cash.CashTransactionDAO;
import com.see.truetransact.serverside.transaction.transfer.TransferDAO;
import com.see.truetransact.serverside.transaction.common.TransactionDAOConstants;
import java.util.Date;
import java.util.LinkedHashMap;

/**
 * ShareProduct DAO.
 *
 */
public class GuaranteeMasterDAO extends TTDAO {

    private static SqlMap sqlMap = null;
    private GuaranteeMasterTO objTO;
    private ShareProductLoanTO objLoanTO;
    private LogDAO logDAO;
    private LogTO logTO;
    private HashMap loanDataMap = new HashMap();
    private HashMap deletedLoanDataMap;
    private String where = "";
    private Date curDate = null;
    private TransactionTO objTransactionTO = null;
    private TransactionDAO transactionDAO = null;

    /**
     * Creates a new instance of ShareProductDAO
     */
    public GuaranteeMasterDAO() throws ServiceLocatorException {
        ServiceLocator locate = ServiceLocator.getInstance();
        sqlMap = (SqlMap) locate.getDAOSqlMap();
    }

    /**
     * This method execute a Query and returns the resultset in HashMap object
     */
    private HashMap getGuaranteeMasterData() throws Exception {
        HashMap returnMap = new HashMap();
        List list = (List) sqlMap.executeQueryForList("getSelectGuaranteeMasterTO", where);
        returnMap.put("GuaranteeMasterTO", list);
        list = null;
        return returnMap;
    }

    private HashMap getGuaranteeTransData() throws Exception {
        HashMap returnMap = new HashMap();
        List list = (List) sqlMap.executeQueryForList("getSelectGuaranteeTransTO", where);
        returnMap.put("GuaranteeTransTO", list);
        list = null;
        return returnMap;
    }

    //    private HashMap getShareProductLoanData()throws Exception{
    //        HashMap dataMap = new HashMap();
    //        List list = (List) sqlMap.executeQueryForList("getSelectShareProductLoan", where);
    //        if(list.size() != 0){
    //            for(int i=0;i<list.size();i++){
    //                dataMap.put(((ShareProductLoanTO)list.get(i)).getLoanType(),(ShareProductLoanTO)list.get(i));
    //            }
    //        }
    //        return dataMap;
    //    }
    /**
     * This method is used to insertnew datat into the Table
     */
    private void insertData(HashMap map) throws Exception {
        logTO.setData(objTO.toString());
        logTO.setPrimaryKey(objTO.getKeyData());
        logTO.setStatus(objTO.getCommand());
        objTO.setGuaranteeNo(getTransID());
        //        objTO.setCbopliBranch(objTO.getCboPli());
        objTO.setStatusBy(logTO.getUserId());
        objTO.setStatusDt(curDate);
        logDAO.addToLog(logTO);
        sqlMap.executeUpdate("insertGuaranteeMasterTO", objTO);
        doTransaction(map);
        //        if(map.get("TransactionTO")!=null)
        logDAO.addToLog(logTO);
        //        addLoanData();
    }

    /**
     * This method is used to Edit the already existing data in the table
     */
    private void updateData(HashMap map) throws Exception {
        logTO.setData(objTO.toString());
        logTO.setPrimaryKey(objTO.getKeyData());
        logTO.setStatus(objTO.getCommand());
        logDAO.addToLog(logTO);
        objTO.setStatusBy(logTO.getUserId());
        sqlMap.executeUpdate("updateGuaranteetMasterTO", objTO);
        doTransaction(map);
        //        addLoanData();
    }

    /*
     * This method is used to update the already existing data by making its
     * status to be deleted
     */
    private void deleteData() throws Exception {
        logTO.setData(objTO.toString());
        logTO.setPrimaryKey(objTO.getKeyData());
        logTO.setStatus(objTO.getCommand());
        objTO.setStatusBy(logTO.getUserId());
        logDAO.addToLog(logTO);
        sqlMap.executeUpdate("deletegGuaranteeMasterTO", objTO);
        //        addLoanData();
    }

    public static void main(String str[]) {
        try {
            GuaranteeMasterDAO dao = new GuaranteeMasterDAO();
        } catch (Exception ex) {
        }
    }

    /**
     * This method is called to do desired operations in the Table
     */
    public HashMap execute(HashMap map) throws Exception {
        _branchCode = (String) map.get(CommonConstants.BRANCH_ID);
        curDate = ServerUtil.getCurrentDate(_branchCode);
        try {
            sqlMap.startTransaction();

            System.out.println("Map in Dao ------------>" + map);

            objTO = (GuaranteeMasterTO) map.get("GuaranteeMasterTO");
            //            objTransactionTO=
            System.out.println("objTO---------------->" + objTO);
            logDAO = new LogDAO();
            logTO = new LogTO();
            transactionDAO = new TransactionDAO(CommonConstants.DEBIT);
            logTO.setUserId(CommonUtil.convertObjToStr(map.get(CommonConstants.USER_ID)));
            logTO.setBranchId(CommonUtil.convertObjToStr(map.get(CommonConstants.BRANCH_ID)));
            logTO.setIpAddr(CommonUtil.convertObjToStr(map.get(CommonConstants.IP_ADDR)));
            logTO.setModule(CommonUtil.convertObjToStr(map.get(CommonConstants.MODULE)));
            logTO.setScreen(CommonUtil.convertObjToStr(map.get(CommonConstants.SCREEN)));
            if (!map.containsKey(CommonConstants.AUTHORIZEMAP)) {

                final String command = objTO.getCommand();
                if (command.equals(CommonConstants.TOSTATUS_INSERT)) {

                    insertData(map);
                } else if (command.equals(CommonConstants.TOSTATUS_UPDATE)) {
                    updateData(map);
                } else if (command.equals(CommonConstants.TOSTATUS_DELETE)) {
                    deleteData();
                }
            } else if (map.containsKey(CommonConstants.AUTHORIZEMAP)) {
                System.out.println("map:" + map);

                HashMap authMap = (HashMap) map.get(CommonConstants.AUTHORIZEMAP);
                authMap.put(CommonConstants.BRANCH_ID, (String) map.get(CommonConstants.BRANCH_ID));
                System.out.println("authMap:" + authMap);
                if (authMap != null) {
                    objTO = (GuaranteeMasterTO) map.get("GuaranteeMasterTO");
                    authorize(authMap, map);




                }
                authMap = null;
            } else {
                throw new NoCommandException();
            }
            sqlMap.commitTransaction();
            destroyObjects();
            return null;
        } catch (Exception e) {
            sqlMap.rollbackTransaction();
            throw new TransRollbackException(e);
        }
    }

    /*
     * This method is used to execute a query to get all the inserted datas in
     * the table and retrun the resultset as a HashMap *
     */
    public HashMap executeQuery(HashMap obj) throws Exception {
        System.out.println("obj------------------->" + obj);
        _branchCode = (String) obj.get(CommonConstants.BRANCH_ID);
        curDate = ServerUtil.getCurrentDate(_branchCode);
        where = (String) obj.get(CommonConstants.MAP_WHERE);
        HashMap guaranteeMap = getGuaranteeMasterData();
//        HashMap guaranteeTransMap =  getGuaranteeTransData();
        //        HashMap shareLoanMap = getShareProductLoanData();
        System.out.println("investmentMap------------------->" + guaranteeMap);
        HashMap data = new HashMap();
        data.put("GuaranteeMasterTO", guaranteeMap);

        List transList = (List) sqlMap.executeQueryForList("getSelectGuaranteeTransTO", where);

        data.put("getSelectGuaranteeTransTO", transList);


        obj.put(CommonConstants.MAP_WHERE, where);
        List list = (List) sqlMap.executeQueryForList("getSelectGuaranteeRemittanceIssueTransactionTO", where);
        data.put("TransactionTO", list);
        System.out.println("data_______________-->" + data.get("getSelectGuaranteeTransTO"));
        list = null;
        //            obj.put("BATCHID",obj.get("BATCH_ID"));
        //            list = (List) sqlMap.executeQueryForList("getBatchTxTransferTOsAuthorize", obj);
        //            data.put("transferTrans",list);
        list = null;

        //        data.put("ShareProductLoanTO", shareLoanMap);
        return data;
    }

    /*
     * This is used to free up the memory used by SharePrductTO object
     */
    private void destroyObjects() {
        objTO = null;
        loanDataMap = null;
        deletedLoanDataMap = null;
    }

    /**
     * This is used to Insert,Update,Delete the loan related detials in the
     * table *
     */
    //    private void addLoanData()throws Exception{
    //        if(loanDataMap!=null){
    //            ArrayList list = new ArrayList(loanDataMap.keySet());
    //            if(list.size() != 0){
    //                for(int i=0; i<list.size();i++){
    //                    objLoanTO = (ShareProductLoanTO)loanDataMap.get(CommonUtil.convertObjToStr(list.get(i)));
    //                    if(objLoanTO.getStatus().equals(CommonConstants.STATUS_CREATED)){
    //                        sqlMap.executeUpdate("insertShareProductLoanTO", objLoanTO);
    //                    }else if(objLoanTO.getStatus().equals(CommonConstants.STATUS_MODIFIED)){
    //                        sqlMap.executeUpdate("updateShareProductLoanTO", objLoanTO);
    //                    }
    //                }
    //            }
    //        }
    //        if(deletedLoanDataMap != null){
    //            ArrayList deletedList = new ArrayList(deletedLoanDataMap.keySet());
    //            if(deletedList.size() != 0){
    //                for(int j=0;j<deletedList.size();j++){
    //                    objLoanTO = (ShareProductLoanTO)deletedLoanDataMap.get(CommonUtil.convertObjToStr(deletedList.get(j)));
    //                    sqlMap.executeUpdate("deleteShareProductLoanTO", objLoanTO);
    //                }
    //            }
    //        }
    //    }
    //
    private String getTransID() throws Exception {
        final IDGenerateDAO dao = new IDGenerateDAO();
        final HashMap where = new HashMap();
        where.put(CommonConstants.MAP_WHERE, "PLI_GUARANTEE_SL_NO");
        return (String) (dao.executeQuery(where)).get(CommonConstants.DATA);
    }

    private HashMap doTransaction(HashMap map) throws Exception {
        System.out.println("In Side The DoTransaction");
        HashMap achdMap = new HashMap();
        List list = (List) sqlMap.executeQueryForList("getSelectTransParams", achdMap);
        if (list != null && list.size() > 0) {
            achdMap = new HashMap();
            achdMap = (HashMap) list.get(0);

        }

        //        System.out.println("achdMap before query ------------>"+achdMap);
        //        List achdLst=ServerUtil.executeQuery("getSelectinvestmentAccountHead", achdMap);
        //        if(achdLst!=null && achdLst.size()>0){

        ArrayList transferList = new ArrayList();
        //            achdMap=(HashMap)achdLst.get(0);
        //            System.out.println("achdMap--------------->"+achdMap);
        TransferTrans objTransferTrans = new TransferTrans();
        objTransferTrans.setInitiatedBranch(_branchCode);
        objTransferTrans.setLinkBatchId(objTO.getGuaranteeNo());

        HashMap txMap = new HashMap();
        if (map.containsKey("TransactionTO")) {
            LinkedHashMap TransactionDetailsMap = (LinkedHashMap) map.get("TransactionTO");
            System.out.println("transferList total ------------->" + TransactionDetailsMap);

            if (TransactionDetailsMap.size() > 0) {

                //                    if(objTO.getGuaranteeNo().equals("CUSTOMER")){
                if (TransactionDetailsMap.containsKey("NOT_DELETED_TRANS_TOs")) {
                    LinkedHashMap allowedTransDetailsTO = (LinkedHashMap) TransactionDetailsMap.get("NOT_DELETED_TRANS_TOs");
                    System.out.println("transferList total ------------->" + allowedTransDetailsTO);
                    for (int J = 1; J <= allowedTransDetailsTO.size(); J++) {
                        double tranAmt = 0.0;
                        objTransactionTO = (TransactionTO) allowedTransDetailsTO.get(String.valueOf(J));
                        tranAmt = CommonUtil.convertObjToDouble(objTransactionTO.getTransAmt()).doubleValue();
                        if (objTransactionTO.getTransType().equals("TRANSFER")) {
                            txMap.put(TransferTrans.CR_AC_HD, CommonUtil.convertObjToStr(achdMap.get("IBR_AC_HD")));
                            txMap.put(TransferTrans.CR_BRANCH, _branchCode);
                            txMap.put(TransferTrans.CR_PROD_TYPE, TransactionFactory.GL);
                            txMap.put("AUTHORIZEREMARKS", "PREMIUM_PAID_AC_HD");
                            txMap.put(TransferTrans.PARTICULARS, objTO.getGuaranteeNo());
                            txMap.put(CommonConstants.USER_ID, objTO.getStatusBy());
                            transferList.add(objTransferTrans.getCreditTransferTO(txMap, tranAmt));

                            txMap.put(TransferTrans.DR_PROD_TYPE, CommonUtil.convertObjToStr(objTransactionTO.getProductType()));
                            txMap.put(TransferTrans.DR_ACT_NUM, CommonUtil.convertObjToStr(objTransactionTO.getDebitAcctNo()));
                            txMap.put(TransferTrans.DR_PROD_ID, CommonUtil.convertObjToStr(objTransactionTO.getProductId()));
                            txMap.put(TransferTrans.PARTICULARS, objTO.getGuaranteeNo());
                            txMap.put("AUTHORIZEREMARKS", "TRANSACTIONAMOUNT");
                            if (CommonUtil.convertObjToStr(objTransactionTO.getProductType()).equals("GL")) {
                                txMap.put(TransferTrans.DR_AC_HD, CommonUtil.convertObjToStr(objTransactionTO.getDebitAcctNo()));
                            }
                            txMap.put(TransferTrans.DR_BRANCH, _branchCode);

                            transferList.add(objTransferTrans.getDebitTransferTO(txMap, tranAmt));
                            doDebitCredit(transferList, _branchCode, false);
                        } else if (objTransactionTO.getTransType().equals("CASH")) {
                            TransactionTO transTO = new TransactionTO();
                            ArrayList cashList = new ArrayList();
                            //                            transactionDAO=new TransactionDAO();
                            transactionDAO.setTransType(CommonConstants.DEBIT);
                            transactionDAO.setCommandMode(CommonConstants.TOSTATUS_INSERT);
                            txMap.put(TransferTrans.CR_AC_HD, (String) achdMap.get("IBR_AC_HD"));
                            tranAmt = CommonUtil.convertObjToDouble(objTransactionTO.getTransAmt()).doubleValue();
                            txMap.put(TransferTrans.PARTICULARS, objTransactionTO.getProductId());
                            txMap.put(TransferTrans.CR_PROD_TYPE, "GL");
                            txMap.put("PARTICULARS", objTransferTrans.getLinkBatchId());
                            txMap.put(CommonConstants.BRANCH_ID, CommonUtil.convertObjToStr(map.get("BRANCH_CODE")));
                            txMap.put("USER_ID", CommonUtil.convertObjToStr(map.get("USER_ID")));
                            txMap.put("IP_ADDR", CommonUtil.convertObjToStr(map.get("IP_ADDR")));
                            txMap.put("MODULE", CommonUtil.convertObjToStr(map.get("MODULE")));
                            txMap.put("SCREEN", CommonUtil.convertObjToStr(map.get("SCREEN")));
                            txMap.put("MODE", CommonConstants.TOSTATUS_INSERT);
                            HashMap returnMap = new HashMap();
                            returnMap = (HashMap) doCashTransfer(txMap, objTransactionTO);
                            System.out.println("returnMap------------------->" + returnMap);
                            System.out.println("objTransactionTO------------------->" + objTransactionTO);
                            objTransactionTO.setBatchId(CommonUtil.convertObjToStr(returnMap.get("TRANS_ID")));
                            objTransactionTO.setBatchDt(curDate);
                            System.out.println("objTO.getInvestmentName()---------------->" + objTO.getGuaranteeNo());
                            String val = (String) objTO.getGuaranteeNo();
                            if (val.length() > 15) {
                                val = (String) objTO.getGuaranteeNo().substring(0, 15);
                            }
                            //        objTransactionTO.setTransId(String.valueOf(objTO.getInvestmentName().split(objTO.getInvestmentName(),16)));
                            objTransactionTO.setTransId(val);
                            System.out.println("objTransactionTO.getTransId()---------------->" + val);
                            objTransactionTO.setStatus(objTO.getStatus());
                            objTransactionTO.setBranchId(_branchCode);
                            sqlMap.executeUpdate("insertRemittanceIssueTransactionTO", objTransactionTO);


                        }
                    }
                }
                //                    }

                transferList = null;
            } else {
                throw new Exception("investment  Transaction is not proper");
            }
        }

        //        }else{
        //            throw new Exception("investment  Config Date is Not set...");
        //        }
        return null;
    }

    private void doDebitCredit(ArrayList batchList, String branchCode, boolean isAutoAuthorize) throws Exception {
        TransferDAO transferDAO = new TransferDAO();
        HashMap data = new HashMap();
        data.put("TxTransferTO", batchList);
        data.put("COMMAND", objTO.getCommand());
        data.put("INITIATED_BRANCH", branchCode);
        data.put(CommonConstants.BRANCH_ID, branchCode);
        data.put(CommonConstants.USER_ID, logTO.getUserId());
        data.put(CommonConstants.MODULE, "Transaction");
        data.put(CommonConstants.SCREEN, "");
        data.put("MODE", "MODE");
        if (isAutoAuthorize) {
            HashMap authorizeMap = new HashMap();
            authorizeMap.put("BATCH_ID", null);
            authorizeMap.put(CommonConstants.AUTHORIZESTATUS, CommonConstants.STATUS_AUTHORIZED);
            data.put(CommonConstants.AUTHORIZEMAP, authorizeMap);
        }
        loanDataMap = transferDAO.execute(data, false);
        if (isAutoAuthorize != true) {
            System.out.println("loanDataMap---------------->" + loanDataMap);
            objTransactionTO.setBatchId(CommonUtil.convertObjToStr(loanDataMap.get("TRANS_ID")));
            objTransactionTO.setBatchDt(curDate);
            System.out.println("objTO.getInvestmentName()---------------->" + objTO.getGuaranteeNo());
            String val = (String) objTO.getGuaranteeNo();
            if (val.length() > 15) {
                val = (String) objTO.getGuaranteeNo().substring(0, 15);
            }
            //        objTransactionTO.setTransId(String.valueOf(objTO.getInvestmentName().split(objTO.getInvestmentName(),16)));
            objTransactionTO.setTransId(val);
            System.out.println("objTransactionTO.getTransId()---------------->" + val);
            objTransactionTO.setStatus(objTO.getStatus());
            objTransactionTO.setBranchId(_branchCode);
            System.out.println("objTransactionTO------------------->" + objTransactionTO);
            sqlMap.executeUpdate("insertRemittanceIssueTransactionTO", objTransactionTO);
        }
    }

    private void authorize(HashMap authMap, HashMap map) throws Exception {
        String batchid = "";
        if (map.containsKey("TransactionTO")) {
            LinkedHashMap TransactionDetailsMap = (LinkedHashMap) map.get("TransactionTO");
            if (TransactionDetailsMap.size() > 0) {
                if (TransactionDetailsMap.containsKey("NOT_DELETED_TRANS_TOs")) {
                    LinkedHashMap allowedTransDetailsTO = (LinkedHashMap) TransactionDetailsMap.get("NOT_DELETED_TRANS_TOs");
                    String status = (String) authMap.get(CommonConstants.AUTHORIZESTATUS);
                    String user_id = (String) authMap.get(CommonConstants.USER_ID);

                    for (int J = 1; J <= allowedTransDetailsTO.size(); J++) {
                        double tranAmt = 0.0;
                        objTransactionTO = (TransactionTO) allowedTransDetailsTO.get(String.valueOf(J));
                        batchid = CommonUtil.convertObjToStr(objTransactionTO.getBatchId());

                        TransactionTO transactionTO;
                        HashMap cashAuthMap, tempMap, transIdMap, transferTransParam;
                        ArrayList cashTransList, transferTransList, transactionList;
                        transactionList = new ArrayList();
                        transferTransParam = new HashMap();
                        transferTransParam.put(CommonConstants.BRANCH_ID, authMap.get(CommonConstants.BRANCH_ID));
                        transferTransParam.put(CommonConstants.USER_ID, authMap.get(CommonConstants.USER_ID));
                        transferTransParam.put("BATCHID", batchid);
                        transferTransParam.put("BATCH_ID", batchid);
                        transferTransParam.put(CommonConstants.AUTHORIZESTATUS, status);
                        if (objTransactionTO.getTransType().equals("TRANSFER")) {
                            transferTransList = (ArrayList) sqlMap.executeQueryForList("getBatchTxTransferTOsAuthorize", transferTransParam);
                            if (transferTransList != null && transferTransList.size() > 0) {
                                TransferTrans objTrans = new TransferTrans();
                                objTrans.doTransferAuthorize(transferTransList, transferTransParam);

                            }
                        }
                        if (objTransactionTO.getTransType().equals("CASH")) {
                            List lst = sqlMap.executeQueryForList("getCashTransactionTOForAuthorzationTransId", batchid);
                            tempMap = new HashMap();
                            cashAuthMap = new HashMap();
                            if (lst != null) {
                                if (lst.size() > 0) {
                                    tempMap.put(CommonConstants.AUTHORIZESTATUS, status);
                                    cashAuthMap.put(CommonConstants.BRANCH_ID, map.get(CommonConstants.BRANCH_ID));
                                    cashAuthMap.put(CommonConstants.USER_ID, map.get(CommonConstants.USER_ID));
                                    cashAuthMap.put(TransactionDAOConstants.OLDAMT, CommonUtil.convertObjToDouble("0.0"));
                                    if (map.containsKey("DAILY")) {
                                        tempMap.put("DAILY", "DAILY");
                                    }
                                    for (int i = 0; i < lst.size(); i++) {
                                        CashTransactionTO cashTO = new CashTransactionTO();
                                        cashTO = (CashTransactionTO) lst.get(i);
                                        if (cashTO != null) {
                                            transIdMap = new HashMap();
                                            transIdMap.put("TRANS_ID", cashTO.getTransId());
                                            cashTransList = new ArrayList();
                                            cashTransList.add(transIdMap);
                                            tempMap.put(CommonConstants.AUTHORIZEDATA, cashTransList);
                                            cashAuthMap.put(CommonConstants.AUTHORIZEMAP, tempMap);
                                            cashAuthMap.put("GL", cashTO.getProdType());
                                            if (map.containsKey("PRODUCT")) {
                                                cashAuthMap.put("PRODUCT", "SHARE");
                                            }
                                            if (cashTO.getAuthorizeRemarks() != null && cashTO.getAuthorizeRemarks().length() > 0) {
                                                cashAuthMap.put("DEBIT_LOAN_TYPE", cashTO.getAuthorizeRemarks());
                                            }
                                            CashTransactionDAO cDAO = new CashTransactionDAO();
                                            cDAO.execute(cashAuthMap, false);
                                            //                                        doCashAuthorize(cashAuthMap) ;
                                            if (map.containsKey("DAILY")) {
                                                i = lst.size();
                                            }
                                        }
                                    }
                                    transIdMap = null;
                                    lst = null;

                                }
                            }
                        }
                        authMap.put("AUTHORIZE_STATUS", status);
                        authMap.put("AUTHORIZE_BY", user_id);
                        authMap.put("AUTHORIZE_DT", curDate);
                        System.out.println("authMap----------------->" + authMap);
                        //                        authMap.put("GUARANTEE_NO",CommonUtil.convertObjToStr(objTO.getGuaranteeNo()));
                        sqlMap.executeUpdate("authorizeGuaranteeMaster", authMap);
                        objTransactionTO.setBranchId(_branchCode);
                        sqlMap.executeUpdate("deleteRemittanceIssueTransactionTO", objTransactionTO);
                        GuaranteeTransTO objTransTO = new GuaranteeTransTO();
                        objTransTO.setCboPli(objTO.getCboPli());
                        objTransTO.setGuaranteeNo(objTO.getGuaranteeNo());
                        objTransTO.setStatus("CREATED");
                        objTransTO.setStatusBy(user_id);
                        objTransTO.setStatusDt(curDate);
                        objTransTO.setAuthorizeBy(user_id);
                        objTransTO.setAuthorizeDt(curDate);
                        objTransTO.setAuthorizeStatus(status);
                        objTransTO.setTransDt(objTransactionTO.getBatchDt());
                        objTransTO.setTransID(objTransactionTO.getTransId());
                        objTransTO.setBatchID(objTransactionTO.getBatchId());
                        objTransTO.setTrnAmt(CommonUtil.convertObjToStr(objTransactionTO.getTransAmt()));
                        sqlMap.executeUpdate("insertGuaranteeTransTO", objTransTO);



                    }
                }
            }
        }

    }

    private HashMap doCashTransfer(HashMap txMap, TransactionTO objTransactionTO) throws Exception {
        TransactionTO objTxTransferTO;
        HashMap cashMap;
        CashTransactionDAO cashDao;
        CashTransactionTO cashTO;

        cashDao = new CashTransactionDAO();
        cashTO = new CashTransactionTO();
        cashMap = new HashMap();
        //            String prodId = objCashTxTo.getProdId();

        //            if((!prodId.equals("")) && (prodId != null))
        cashMap.put(CommonConstants.PRODUCT_TYPE, "GL");
        cashMap.put(CommonConstants.USER_ID, txMap.get("USER_ID"));
        cashMap.put(CommonConstants.BRANCH_ID, txMap.get("BRANCH_CODE"));
        cashMap.put(CommonConstants.IP_ADDR, logTO.getIpAddr());
        cashMap.put(CommonConstants.MODULE, logTO.getModule());
        cashMap.put(CommonConstants.SCREEN, logTO.getScreen());
        cashMap.put("OLDAMOUNT", "0");


        cashTO.setAmount(objTransactionTO.getTransAmt());
        cashTO.setAcHdId((String) txMap.get(TransferTrans.CR_AC_HD));
        cashTO.setActNum("");
        cashTO.setProdType("GL");
        cashTO.setInstType(objTransactionTO.getInstType());
        cashTO.setInstrumentNo1(objTransactionTO.getChequeNo());
        cashTO.setInstrumentNo2(objTransactionTO.getChequeNo2());
        cashTO.setInstDt(objTransactionTO.getChequeDt());
        //            cashTO.setProdId(objTransactionTO.getProdId());
        //            cashTO.setParticulars(objTransactionTO.getParticulars());
        cashTO.setStatus(objTransactionTO.getStatus());
        cashTO.setStatusBy((String) txMap.get("USER_ID"));
        cashTO.setTransDt(curDate);
        cashTO.setTransId(objTransactionTO.getTransId());
        cashTO.setTransType(objTransactionTO.getTransType());

        cashTO.setBranchId((String) txMap.get("BRANCH_CODE"));

        cashTO.setInitChannType(CommonConstants.CASHIER);
        //            cashTO.setInitTransId(objTransactionTO.getInitTransId());
        cashTO.setInpAmount(objTransactionTO.getTransAmt());
        cashTO.setInpCurr("INR");
        //            cashTO.setAuthorizeRemarks(objTransactionTO.getAuthorizeRemarks());
        cashTO.setLinkBatchId("");
        cashTO.setInitiatedBranch(cashTO.getBranchId());
        cashTO.setCommand(CommonConstants.TOSTATUS_INSERT);
        cashTO.setStatus(CommonConstants.STATUS_CREATED);
        cashMap.put("CashTransactionTO", cashTO);
        //            if(getLoanAmtMap()!=null && getLoanAmtMap().size()>0)
        //                cashMap.put("ALL_AMOUNT",getLoanAmtMap());
        System.out.println("#### doCashTransfer() : cashMap : " + cashMap);
        txMap = new HashMap();
        txMap = cashDao.execute(cashMap, false);
        cashTO = null;
        cashDao = null;
        cashMap = null;
        objTxTransferTO = null;
        return txMap;

    }
}
