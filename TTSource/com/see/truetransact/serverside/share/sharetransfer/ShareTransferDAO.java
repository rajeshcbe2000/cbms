/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition.
 * 
 *
 * ShareTransferDAO.java
 *
 * Created on Thu Feb 03 16:24:31 IST 2005
 */
package com.see.truetransact.serverside.share.sharetransfer;

import java.util.List;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Date;


import com.ibatis.db.sqlmap.SqlMap;
import com.see.truetransact.serverutil.ServerUtil;

import com.see.truetransact.commonutil.CommonConstants;
import com.see.truetransact.commonutil.NoCommandException;
import com.see.truetransact.commonutil.CommonUtil;
import com.see.truetransact.commonutil.DateUtil;
import com.see.truetransact.serverside.TTDAO;
import com.see.truetransact.serverutil.ServerConstants;
import com.see.truetransact.servicelocator.ServiceLocator;
import com.see.truetransact.serverexception.TransRollbackException;
import com.see.truetransact.serverexception.ServiceLocatorException;

import com.see.truetransact.transferobject.share.sharetransfer.ShareTransferTO;
import com.see.truetransact.serverside.common.idgenerate.IDGenerateDAO;

import com.see.truetransact.serverside.common.log.LogDAO;
import com.see.truetransact.transferobject.common.log.LogTO;


//__ Transfer object for the ShareAcctDetails...
import com.see.truetransact.transferobject.share.ShareAcctDetailsTO;
import com.see.truetransact.transferobject.common.transaction.TransactionTO;
import com.see.truetransact.serverside.common.transaction.TransactionDAO;
import com.see.truetransact.serverside.common.transaction.TransferTrans;
import com.see.truetransact.transferobject.batchprocess.share.DividendBatchTO;
import com.see.truetransact.serverside.transaction.transfer.TransferDAO;

import com.see.truetransact.serverside.transaction.cash.CashTransactionDAO;
import com.see.truetransact.transferobject.transaction.cash.CashTransactionTO;

import com.see.truetransact.transferobject.transaction.transfer.TxTransferTO;
import java.util.Date;
/**
 * ShareTransfer DAO.
 *
 */
public class ShareTransferDAO extends TTDAO {

    private static SqlMap sqlMap = null;
    private ShareTransferTO objShareTransferTO;
    private String userID = "";
    private TransactionTO objTransactionTO;
    private TransactionDAO transactionDAO = null;
    private DividendBatchTO objDividendBatchTO;
    private HashMap loanDataMap = new HashMap();
    private String payMode = "";
    private String linkBatchId = "";
    private HashMap returmMap = new HashMap();
    private Date currDt = null;
    /**
     * Creates a new instance of ShareTransferDAO
     */
    public ShareTransferDAO() throws ServiceLocatorException {
        ServiceLocator locate = ServiceLocator.getInstance();
        sqlMap = (SqlMap) locate.getDAOSqlMap();
    }

    private HashMap getData(HashMap map) throws Exception {
        HashMap returnMap = new HashMap();
        String where = (String) map.get(CommonConstants.MAP_WHERE);
        List list = (List) sqlMap.executeQueryForList("getSelectShareTransferTO", where);
        returnMap.put("ShareTransferTO", list);
        return returnMap;
    }

    private void insertData(LogDAO objLogDAO, LogTO objLogTO, HashMap map) throws Exception {
        try {
            sqlMap.startTransaction();
            if (!map.containsKey("DIVIDEND")) {
                objShareTransferTO.setShareTransId(getShareTransID());
                objShareTransferTO.setCreatedBy(userID);
                objShareTransferTO.setCreatedDt(currDt);
                objShareTransferTO.setStatus(CommonConstants.STATUS_CREATED);
                objShareTransferTO.setStatusBy(userID);
                objShareTransferTO.setStatusDt(currDt);
                sqlMap.executeUpdate("insertShareTransferTO", objShareTransferTO);
                objLogTO.setData(objShareTransferTO.toString());
                objLogTO.setPrimaryKey(objShareTransferTO.getKeyData());
                objLogDAO.addToLog(objLogTO);
            } else {
                doDividendPayment(map, objLogTO);
                objDividendBatchTO.setDivPayFlag(payMode);
                objDividendBatchTO.setBatchID(CommonUtil.convertObjToStr(loanDataMap.get("TRANS_ID")));
                returmMap.put("TRANS_ID", CommonUtil.convertObjToStr(loanDataMap.get("TRANS_ID")));
                sqlMap.executeUpdate("insertDividendBatchTO", objDividendBatchTO);
                HashMap mapShareAcctDet = new HashMap();
                double dividend = CommonUtil.convertObjToDouble(objDividendBatchTO.getDividendAmt()).doubleValue();
                mapShareAcctDet.put("DIVIDEND_AMOUNT", new Double(dividend * -1));
                Date uptoDate = objDividendBatchTO.getDividendDt();
                mapShareAcctDet.put("UPTO_DIVIDEND_APPLDT", uptoDate);
                mapShareAcctDet.put("UPTO_DIVIDEND_PAID_DATE", uptoDate);
                mapShareAcctDet.put("SHARE_ACCT_NO", CommonUtil.convertObjToStr(objDividendBatchTO.getShareAcctNo()));
                //                sqlMap.executeUpdate("upDateDividendDateAndAmountpaid", mapShareAcctDet);
                objLogTO.setData(objDividendBatchTO.toString());
                objLogTO.setPrimaryKey(objDividendBatchTO.getKeyData());
                objLogDAO.addToLog(objLogTO);
            }
            sqlMap.commitTransaction();
        } catch (Exception e) {
            sqlMap.rollbackTransaction();
            e.printStackTrace();
            throw new TransRollbackException(e);
        }
    }

    private void doDividendPayment(HashMap map, LogTO objLogTO) throws Exception {
        HashMap acHeads = new HashMap();
        acHeads.put("SHARE_TYPE", CommonUtil.convertObjToStr(objDividendBatchTO.getShareType()));
        List lstacHeads = (List) sqlMap.executeQueryForList("getSelectDividendCalProd", acHeads);
        if (lstacHeads != null && lstacHeads.size() > 0) {
            acHeads = new HashMap();
            acHeads = (HashMap) lstacHeads.get(0);
            LinkedHashMap TransactionDetailsMap = (LinkedHashMap) map.get("TransactionTO");
            TransferTrans objTransferTrans = new TransferTrans();
            System.out.println("objDividendBatchTO.getShareAcctNo()------------>" + objDividendBatchTO.getShareAcctNo());
            objTransferTrans.setLinkBatchId(CommonUtil.convertObjToStr(objDividendBatchTO.getShareAcctNo()));
            linkBatchId = CommonUtil.convertObjToStr(objDividendBatchTO.getShareAcctNo());
            if (TransactionDetailsMap.size() > 0) {
                if (TransactionDetailsMap.containsKey("NOT_DELETED_TRANS_TOs")) {
                    ArrayList transferList = new ArrayList();
                    LinkedHashMap allowedTransDetailsTO = (LinkedHashMap) TransactionDetailsMap.get("NOT_DELETED_TRANS_TOs");
                    HashMap txMap = new HashMap();
                    txMap.put(TransferTrans.AUTHORIZE_STATUS_2, "ENTERED_AMOUNT");
                    txMap.put(TransferTrans.PARTICULARS, "Dividend Of " + CommonUtil.convertObjToStr(objDividendBatchTO.getShareAcctNo()));
                    txMap.put("DR_INST_TYPE", "WITHDRAW_SLIP");
                    txMap.put(TransferTrans.DR_AC_HD, (String) acHeads.get("DIVIDEND_PAYABLE_ACHD"));
                    txMap.put(TransferTrans.DR_PROD_TYPE, "GL");
                    txMap.put(TransferTrans.DR_BRANCH, _branchCode);
                    txMap.put(CommonConstants.USER_ID, objDividendBatchTO.getStatusBy());

                    transferList.add(objTransferTrans.getDebitTransferTO(txMap, CommonUtil.convertObjToDouble(objDividendBatchTO.getDividendAmt()).doubleValue()));
                    for (int J = 1; J <= allowedTransDetailsTO.size(); J++) {
                        objTransactionTO = (TransactionTO) allowedTransDetailsTO.get(String.valueOf(J));
                        payMode = CommonUtil.convertObjToStr(objTransactionTO.getTransType());
                        if (objTransactionTO.getTransType().equals("TRANSFER")) {
                            txMap.put(TransferTrans.CR_PROD_TYPE, CommonUtil.convertObjToStr(objTransactionTO.getProductType()));
                            txMap.put(TransferTrans.CR_ACT_NUM, CommonUtil.convertObjToStr(objTransactionTO.getDebitAcctNo()));
                            txMap.put(TransferTrans.CR_PROD_ID, CommonUtil.convertObjToStr(objTransactionTO.getProductId()));
                            if (CommonUtil.convertObjToStr(objTransactionTO.getProductType()).equals("GL")) {
                                txMap.put(TransferTrans.CR_AC_HD, CommonUtil.convertObjToStr(objTransactionTO.getDebitAcctNo()));
                            }
                            txMap.put(TransferTrans.CR_BRANCH, _branchCode);

                            transferList.add(objTransferTrans.getCreditTransferTO(txMap, CommonUtil.convertObjToDouble(objTransactionTO.getTransAmt()).doubleValue()));
                            doDebitCredit(transferList, _branchCode, false);
                        } else {

                            if (CommonUtil.convertObjToDouble(objTransactionTO.getTransAmt()).doubleValue() > 0) {

                                doCashTransfer(objTransactionTO, objLogTO, acHeads, linkBatchId);
                            }
                        }
                        objTransactionTO.setBatchDt(currDt);
                        System.out.println("loanDataMap------------>" + loanDataMap);
                        objTransactionTO.setBatchId(CommonUtil.convertObjToStr(loanDataMap.get("TRANS_ID")));
                        objTransactionTO.setTransId(objDividendBatchTO.getShareAcctNo());
                        objTransactionTO.setBranchId(_branchCode);
                        System.out.println("objTransactionTO------------------->" + objTransactionTO);
                        sqlMap.executeUpdate("insertRemittanceIssueTransactionTO", objTransactionTO);



                    }

                }
            }
        }
    }

    private void doDebitCredit(ArrayList batchList, String branchCode, boolean isAutoAuthorize) throws Exception {
        TransferDAO transferDAO = new TransferDAO();
        HashMap data = new HashMap();
        data.put("TxTransferTO", batchList);
        data.put("COMMAND", objDividendBatchTO.getCommand());
        data.put("INITIATED_BRANCH", branchCode);
        data.put("LINK_BATCH_ID", linkBatchId);
        data.put(CommonConstants.BRANCH_ID, branchCode);
        data.put(CommonConstants.USER_ID, userID);
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
        System.out.println("loanDataMap-------->" + loanDataMap);
    }

    private void doCashTransfer(TransactionTO objTxTransferTO, LogTO objLogTO, HashMap acHeads, String linkBatchId) throws Exception {

        HashMap cashMap;
        CashTransactionDAO cashDao;
        CashTransactionTO cashTO;
        cashDao = new CashTransactionDAO();
        cashTO = new CashTransactionTO();
        cashMap = new HashMap();
        cashMap.put(CommonConstants.PRODUCT_TYPE, "GL");
        cashMap.put(CommonConstants.USER_ID, userID);
        cashMap.put(CommonConstants.BRANCH_ID, objLogTO.getBranchId());
        cashMap.put(CommonConstants.IP_ADDR, objLogTO.getIpAddr());
        cashMap.put(CommonConstants.MODULE, objLogTO.getModule());
        cashMap.put(CommonConstants.SCREEN, objLogTO.getScreen());

        cashTO.setAmount(objTxTransferTO.getTransAmt());
        cashTO.setAcHdId(CommonUtil.convertObjToStr(acHeads.get("DIVIDEND_PAYABLE_ACHD")));
        cashTO.setProdType("GL");
        cashTO.setInstType(objTxTransferTO.getInstType());
        cashTO.setInstrumentNo1(objTxTransferTO.getChequeNo());
        cashTO.setAuthorizeStatus_2("ENTERED_AMOUNT");
        cashTO.setInstDt(objTxTransferTO.getChequeDt());
        cashTO.setParticulars(objDividendBatchTO.getShareAcctNo() + "Div Payment");
        cashTO.setStatus(objTxTransferTO.getStatus());
        cashTO.setStatusBy(userID);
        cashTO.setTransDt(currDt);
        cashTO.setTransId(objTxTransferTO.getTransId());
        cashTO.setTransType("DEBIT");
        cashTO.setBranchId(_branchCode);
        cashTO.setInitChannType(CommonConstants.CASHIER);
        cashTO.setInitTransId("CASHIER");
        cashTO.setInpAmount(objTxTransferTO.getTransAmt());
        cashTO.setInpCurr("INR");
        cashTO.setLinkBatchId(linkBatchId);
        cashTO.setInitiatedBranch(_branchCode);
        cashTO.setCommand(objDividendBatchTO.getCommand());
        cashMap.put("CashTransactionTO", cashTO);
        loanDataMap = cashDao.execute(cashMap, false);
        cashTO = null;
        cashDao = null;
        cashMap = null;
        objTxTransferTO = null;
    }

    private String getShareTransID() throws Exception {
        final IDGenerateDAO dao = new IDGenerateDAO();
        final HashMap where = new HashMap();
        where.put(CommonConstants.MAP_WHERE, "SHARE_TRANS_ID");
        return (String) (dao.executeQuery(where)).get(CommonConstants.DATA);
    }

    private void updateData(LogDAO objLogDAO, LogTO objLogTO) throws Exception {
        try {
            sqlMap.startTransaction();
            objShareTransferTO.setStatus(CommonConstants.STATUS_MODIFIED);
            objShareTransferTO.setStatusBy(userID);
            objShareTransferTO.setStatusDt(currDt);

            sqlMap.executeUpdate("updateShareTransferTO", objShareTransferTO);

            objLogTO.setData(objShareTransferTO.toString());
            objLogTO.setPrimaryKey(objShareTransferTO.getKeyData());
            objLogDAO.addToLog(objLogTO);

            sqlMap.commitTransaction();
        } catch (Exception e) {
            sqlMap.rollbackTransaction();
            e.printStackTrace();
            throw new TransRollbackException(e);
        }
    }

    private void deleteData(LogDAO objLogDAO, LogTO objLogTO, HashMap map) throws Exception {
        try {
            sqlMap.startTransaction();
            if (!map.containsKey("DIVIDEND")) {
                objShareTransferTO.setStatus(CommonConstants.STATUS_DELETED);
                objShareTransferTO.setStatusBy(userID);
                objShareTransferTO.setStatusDt(currDt);
                sqlMap.executeUpdate("deleteShareTransferTO", objShareTransferTO);

                objLogTO.setData(objShareTransferTO.toString());
                objLogTO.setPrimaryKey(objShareTransferTO.getKeyData());
                objLogDAO.addToLog(objLogTO);
            } else {
                objDividendBatchTO.setStatus(CommonConstants.STATUS_DELETED);
                objDividendBatchTO.setStatusBy(userID);
                objDividendBatchTO.setStatusDt(currDt);
                dodeleteOrUpadate(map);
                objDividendBatchTO.setInitiatedBranch(_branchCode);
                sqlMap.executeUpdate("deleteShareDividend", objDividendBatchTO);
            }

            sqlMap.commitTransaction();
        } catch (Exception e) {
            sqlMap.rollbackTransaction();
            e.printStackTrace();
            throw new TransRollbackException(e);
        }
    }

    public static void main(String str[]) {
        try {
            ShareTransferDAO dao = new ShareTransferDAO();
            ShareAcctDetailsTO objShareAcctDetailsTO = new ShareAcctDetailsTO();
            objShareAcctDetailsTO.setShareAcctNo("SH001014");
            objShareAcctDetailsTO.setShareAcctDetNo("1");
            objShareAcctDetailsTO.setAuthorize("AUTHORIZED");
            objShareAcctDetailsTO.setAuthorizeBy("sysadmin");
            //            objShareAcctDetailsTO.setAuthorizeDt(currDt);
            //            //            objShareAcctDetailsTO.setNoOfShares(new Double(10));
            //            objShareAcctDetailsTO.setShareCertIssueDt(currDt);
            objShareAcctDetailsTO.setShareNoFrom("SC001201");
            objShareAcctDetailsTO.setShareNoTo("SC001300");
            objShareAcctDetailsTO.setStatus(CommonConstants.STATUS_CREATED);
            objShareAcctDetailsTO.setStatusBy("sysadmin");
            //            objShareAcctDetailsTO.setStatusDt(currDt);


            //            dao.diffStartDiffEnd(objShareAcctDetailsTO, "SC001241", "SC001260", "SH001015");
            System.out.println("Over...");
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public HashMap execute(HashMap map) throws Exception {
        returmMap = new HashMap();
        _branchCode = (String) map.get(CommonConstants.BRANCH_ID);
        currDt = ServerUtil.getCurrentDate(_branchCode);
        // Log DAO
        LogDAO objLogDAO = new LogDAO();

        // Log Transfer Object
        LogTO objLogTO = new LogTO();
        objLogTO.setUserId((String) map.get(CommonConstants.USER_ID));
        objLogTO.setBranchId((String) map.get(CommonConstants.BRANCH_ID));
        objLogTO.setIpAddr((String) map.get(CommonConstants.IP_ADDR));
        objLogTO.setModule((String) map.get(CommonConstants.MODULE));
        objLogTO.setScreen((String) map.get(CommonConstants.SCREEN));
        System.out.println("Map----------->" + map);

        userID = CommonUtil.convertObjToStr(map.get(CommonConstants.USER_ID));

        //__ If the Map contains the ShareTransferTO Key...
        if (map.containsKey("ShareTransferTO") || map.containsKey("DividendTO")) {
            objShareTransferTO = (ShareTransferTO) map.get("ShareTransferTO");
            objDividendBatchTO = (DividendBatchTO) map.get("DividendTO");
            String commond1 = "";
            if (map.containsKey("DIVIDEND")) {
                System.out.println("objDividendBatchTO---------->" + objDividendBatchTO + "objDividendBatchTO.getCommand()" + objDividendBatchTO.getCommand());
                commond1 = CommonUtil.convertObjToStr(objDividendBatchTO.getCommand());
                System.out.println("commond1--------------->" + commond1);
            } else {
                commond1 = objShareTransferTO.getCommand();
            }

            final String command = commond1;


            if (command.equals(CommonConstants.TOSTATUS_INSERT)) {
                insertData(objLogDAO, objLogTO, map);
            } else if (command.equals(CommonConstants.TOSTATUS_UPDATE)) {
                updateData(objLogDAO, objLogTO);
            } else if (command.equals(CommonConstants.TOSTATUS_DELETE)) {
                deleteData(objLogDAO, objLogTO, map);
            } else {
                throw new NoCommandException();
            }
        }

        //__ If the Map Contains the Authorization Map...
        if (map.containsKey(CommonConstants.AUTHORIZEMAP)) {
            HashMap authMap = (HashMap) map.get(CommonConstants.AUTHORIZEMAP);
            if (authMap != null) {
                authorize(authMap, objLogDAO, objLogTO);
            }
        }


        objLogDAO = null;
        objLogTO = null;
        destroyObjects();

        return returmMap;
    }

    //__ To Authorize the data...
    private void authorize(HashMap map, LogDAO objLogDAO, LogTO objLogTO) throws Exception {
        String status = (String) map.get(CommonConstants.AUTHORIZESTATUS);
        ArrayList selectedList = (ArrayList) map.get(CommonConstants.AUTHORIZEDATA);
        //__ Start the Group of the Transaction...
        try {
            sqlMap.startTransaction();
            if (!map.containsKey("DIVIDENDAUTHORIZE")) {
                int size = selectedList.size();
                HashMap dataMap;

                for (int i = 0; i < size; i++) {
                    dataMap = (HashMap) selectedList.get(i);
                    dataMap.put(CommonConstants.STATUS, status);
                    dataMap.put(CommonConstants.USER_ID, objLogTO.getUserId());
                    System.out.println("DataMap in ShareTrans DAO: " + dataMap);

                    final String ACCTFROM = CommonUtil.convertObjToStr(dataMap.get(("TRANSFER FROM")));
                    final String ACCTTO = CommonUtil.convertObjToStr(dataMap.get(("TRANSFER TO")));

                    //__ Authorize the Data in the Share Transfer...
                    sqlMap.executeUpdate("authorizeShareTransferData", dataMap);

                    // if the AuthorizeStatus is Authorized
                    if (status.equalsIgnoreCase(CommonConstants.STATUS_AUTHORIZED)) {
                        ShareAcctDetailsTO objShareAcctDetailsTO = new ShareAcctDetailsTO();
                        //__ To get the Particular row containing the Range From which
                        //__ the Share(s) have been Transfered...
                        List list = (List) sqlMap.executeQueryForList("getShareAcctRowData", dataMap);
                        if (list.size() > 0) {
                            objShareAcctDetailsTO = (ShareAcctDetailsTO) list.get(0);

                            //__ Data from the Share Transfer...
                            String fromTrans = CommonUtil.convertObjToStr(dataMap.get(("SHARE NO FROM")));
                            String toTrans = CommonUtil.convertObjToStr(dataMap.get(("SHARE NO TO")));

                            //__ Data from the Share Acct Details...
                            String fromRange = CommonUtil.convertObjToStr(objShareAcctDetailsTO.getShareNoFrom());
                            String toRange = CommonUtil.convertObjToStr(objShareAcctDetailsTO.getShareNoTo());

                            if (fromTrans.equalsIgnoreCase(fromRange) && toTrans.equalsIgnoreCase(toRange)) {
                                //__ Both Strating and Ending are Same...
                                sameStartAndEnd(objShareAcctDetailsTO, ACCTTO, objLogDAO, objLogTO);

                            } else if (fromTrans.equalsIgnoreCase(fromRange) && toTrans.compareTo(toRange) < 0) {
                                //__ Starting is Same, but Different Ending...
                                sameStartDiffEnd(objShareAcctDetailsTO, toTrans, ACCTTO, objLogDAO, objLogTO);

                            } else if (fromTrans.compareTo(fromRange) != 0 && toTrans.equalsIgnoreCase(toRange)) {
                                //__ different Starting, but Same Ending...
                                diffStartSameEnd(objShareAcctDetailsTO, fromTrans, ACCTTO, objLogDAO, objLogTO);

                            } else {
                                //__ Both the Starting and Ending are Different...
                                diffStartDiffEnd(objShareAcctDetailsTO, fromTrans, toTrans, ACCTTO, objLogDAO, objLogTO);

                            }
                        }
                    }
                }
            } else {


                System.out.println("Inside Authorize Map------------------------>" + map);

                map.put("UPTO_DIVIDEND_PAID_DATE", currDt.clone());


                HashMap cashAuthMap = new HashMap();
                cashAuthMap.put(CommonConstants.BRANCH_ID, _branchCode);
                cashAuthMap.put(CommonConstants.USER_ID, CommonUtil.convertObjToStr(objLogTO.getUserId()));
//                cashAuthMap.put("PRODUCT", "SHARE");
                String linkBatchId = CommonUtil.convertObjToStr(map.get("SHARE_ACCT_NO"));


                new TransactionDAO("DEBIT").authorizeCashAndTransfer(linkBatchId, status, cashAuthMap);
                HashMap transMap = new HashMap();
                transMap.put("LINK_BATCH_ID", linkBatchId);
                sqlMap.executeUpdate("updateInstrumentNO1Transfer", transMap);
                sqlMap.executeUpdate("updateInstrumentNO1Cash", transMap);
                transMap = null;
                objTransactionTO = new TransactionTO();
                objTransactionTO.setBatchId(CommonUtil.convertObjToStr(map.get("BATCH_ID")));
                objTransactionTO.setTransId(CommonUtil.convertObjToStr(CommonUtil.convertObjToStr(map.get("SHARE_ACCT_NO"))));
                objTransactionTO.setBranchId(_branchCode);
                System.out.println("objTransactionTO----------------->" + objTransactionTO);
                sqlMap.executeUpdate("deleteRemittanceIssueTransactionTO", objTransactionTO);
                cashAuthMap = null;
                map.put("AUTHORIZE_STATUS", status);
                if (status.equals("AUTHORIZED")) {
                    sqlMap.executeUpdate("upDateDividendDateAndAmountpaid", map);
                }

                sqlMap.executeUpdate("authorizeShareDividend", map);

                selectedList = null;


            }
            sqlMap.commitTransaction();
        } catch (Exception e) {
            sqlMap.rollbackTransaction();
            e.printStackTrace();
            throw e;
        }
    }

    //__ Both Strating and Ending are Same...
    private void sameStartAndEnd(ShareAcctDetailsTO objShareAcctDetailsTO, String ACCTTO, LogDAO objLogDAO, LogTO objLogTO) {
        try {
            //__ Delete the Particular record from the ShareAcctDetails...
            objShareAcctDetailsTO.setStatus(CommonConstants.STATUS_DELETED);
            objShareAcctDetailsTO.setStatusBy(userID);
            objShareAcctDetailsTO.setStatusDt(currDt);
            sqlMap.executeUpdate("deleteShareAcctDetailsTO", objShareAcctDetailsTO);

            objLogTO.setData(objShareAcctDetailsTO.toString());
            objLogTO.setPrimaryKey(objShareAcctDetailsTO.getKeyData());
            objLogDAO.addToLog(objLogTO);

            //__ Insert it again to the Transfered Account as  a new Record...
            int maxSerialNo = getMaxSerialNo(ACCTTO);

            objShareAcctDetailsTO.setShareAcctNo(ACCTTO);
            objShareAcctDetailsTO.setStatus(CommonConstants.STATUS_CREATED);
            objShareAcctDetailsTO.setStatusDt(currDt);
            objShareAcctDetailsTO.setShareAcctDetNo(String.valueOf(maxSerialNo + 1));
            objShareAcctDetailsTO.setShareCertIssueDt(objShareAcctDetailsTO.getStatusDt());

            sqlMap.executeUpdate("insertShareAcctDetailsTO", objShareAcctDetailsTO);

            objLogTO.setData(objShareAcctDetailsTO.toString());
            objLogTO.setPrimaryKey(objShareAcctDetailsTO.getKeyData());
            objLogDAO.addToLog(objLogTO);

        } catch (Exception e) {
            System.out.println("Error in rowTransfer()");
            e.printStackTrace();
        }
    }

    //__ Starting is Same, but Different Ending...
    private void sameStartDiffEnd(ShareAcctDetailsTO objShareAcctDetailsTO, String rangeTO, String ACCTTO, LogDAO objLogDAO, LogTO objLogTO) throws Exception {
        //__ ShareAcctNo...
        final String FROMACCT = CommonUtil.convertObjToStr(objShareAcctDetailsTO.getShareAcctNo());

        //__ Ranges...
        final String FROMRANGE = CommonUtil.convertObjToStr(objShareAcctDetailsTO.getShareNoFrom());
        final String TORANGE = CommonUtil.convertObjToStr(objShareAcctDetailsTO.getShareNoTo());


        //__ Split the Range From and To into Sttring and Integer parts...
        final HashMap rangeFromMap = getShareNo(FROMRANGE);
        final HashMap rangeToMap = getShareNo(rangeTO);
        final HashMap endToMap = getShareNo(TORANGE);


        final String START = CommonUtil.convertObjToStr(rangeToMap.get("rangeFromStart"));
        final int LENGTH = CommonUtil.convertObjToInt(rangeToMap.get("rangeFromLen"));
        int toStart = CommonUtil.convertObjToInt(rangeToMap.get("rangeFromEnd"));

        //__ To get the Full String for the ShareAcctNo...
        String rangeToStart = CommonUtil.lpad(String.valueOf(toStart + 1), LENGTH, '0');

        //__ Update part...
        int start = CommonUtil.convertObjToInt(rangeToMap.get("rangeFromEnd"));
        int end = CommonUtil.convertObjToInt(endToMap.get("rangeFromEnd"));
        int noOfShares = end - start;

        //__ Update part...
        objShareAcctDetailsTO.setShareNoFrom(START + rangeToStart);
        objShareAcctDetailsTO.setShareNoTo(TORANGE);
        objShareAcctDetailsTO.setNoOfShares(CommonUtil.convertObjToInt(noOfShares));
        objShareAcctDetailsTO.setStatus(CommonConstants.STATUS_MODIFIED);
        objShareAcctDetailsTO.setStatusBy(userID);
        objShareAcctDetailsTO.setStatusDt(currDt);
        //__ Update the First Half....
        sqlMap.executeUpdate("updateShareAcctDetailsTO", objShareAcctDetailsTO);

        objLogTO.setData(objShareAcctDetailsTO.toString());
        objLogTO.setPrimaryKey(objShareAcctDetailsTO.getKeyData());
        objLogDAO.addToLog(objLogTO);


        //__ Insert Part...
        int fromEnd = CommonUtil.convertObjToInt(rangeFromMap.get("rangeFromEnd"));

        noOfShares = (toStart - fromEnd) + 1;

        objShareAcctDetailsTO.setShareAcctNo(ACCTTO);
        objShareAcctDetailsTO.setNoOfShares(CommonUtil.convertObjToInt(noOfShares));
        objShareAcctDetailsTO.setShareNoFrom(FROMRANGE);
        objShareAcctDetailsTO.setShareNoTo(CommonUtil.convertObjToStr(rangeTO));

        int maxSerialNo = getMaxSerialNo(ACCTTO);
        objShareAcctDetailsTO.setShareAcctDetNo(String.valueOf(maxSerialNo + 1));
        objShareAcctDetailsTO.setStatus(CommonConstants.STATUS_CREATED);
        objShareAcctDetailsTO.setStatusBy(userID);
        objShareAcctDetailsTO.setStatusDt(currDt);
        objShareAcctDetailsTO.setShareCertIssueDt(objShareAcctDetailsTO.getStatusDt());
        //__ Insert The Other half in ShareAcctDetails...
        sqlMap.executeUpdate("insertShareAcctDetailsTO", objShareAcctDetailsTO);

        objLogTO.setData(objShareAcctDetailsTO.toString());
        objLogTO.setPrimaryKey(objShareAcctDetailsTO.getKeyData());
        objLogDAO.addToLog(objLogTO);
    }

    //__ Starting is Different, but Same Ending...
    private void diffStartSameEnd(ShareAcctDetailsTO objShareAcctDetailsTO, String rangeFrom, String ACCTTO, LogDAO objLogDAO, LogTO objLogTO) throws Exception {
        //__ ShareAcctNo...
        final String FROMACCT = CommonUtil.convertObjToStr(objShareAcctDetailsTO.getShareAcctNo());

        //__ Ranges...
        final String FROMRANGE = CommonUtil.convertObjToStr(objShareAcctDetailsTO.getShareNoFrom());
        final String TORANGE = CommonUtil.convertObjToStr(objShareAcctDetailsTO.getShareNoTo());


        //__ Split the Range From and To into Sttring and Integer parts...
        final HashMap rangeFromMap = getShareNo(FROMRANGE);
        final HashMap rangeToMap = getShareNo(rangeFrom);
        final HashMap endToMap = getShareNo(TORANGE);

        final String START = CommonUtil.convertObjToStr(rangeToMap.get("rangeFromStart"));
        final int LENGTH = CommonUtil.convertObjToInt(rangeToMap.get("rangeFromLen"));
        int toStart = CommonUtil.convertObjToInt(rangeToMap.get("rangeFromEnd"));

        //__ To get the Full String for the ShareAcctNo...
        String rangeEnd = CommonUtil.lpad(String.valueOf(toStart - 1), LENGTH, '0');

        int start = CommonUtil.convertObjToInt(rangeFromMap.get("rangeFromEnd"));
        int end = CommonUtil.convertObjToInt(rangeToMap.get("rangeFromEnd"));
        int noOfShares = end - start;

        //__ Update part...
        objShareAcctDetailsTO.setShareNoFrom(FROMRANGE);
        objShareAcctDetailsTO.setShareNoTo(CommonUtil.convertObjToStr(START + rangeEnd));
        objShareAcctDetailsTO.setNoOfShares(CommonUtil.convertObjToInt(noOfShares));
        objShareAcctDetailsTO.setStatus(CommonConstants.STATUS_MODIFIED);
        objShareAcctDetailsTO.setStatusBy(userID);
        objShareAcctDetailsTO.setStatusDt(currDt);
        //__ Update the First Half....
        sqlMap.executeUpdate("updateShareAcctDetailsTO", objShareAcctDetailsTO);

        objLogTO.setData(objShareAcctDetailsTO.toString());
        objLogTO.setPrimaryKey(objShareAcctDetailsTO.getKeyData());
        objLogDAO.addToLog(objLogTO);


        //__ Insert Part...

        start = CommonUtil.convertObjToInt(rangeToMap.get("rangeFromEnd"));
        end = CommonUtil.convertObjToInt(endToMap.get("rangeFromEnd"));
        noOfShares = (end - start) + 1;


        objShareAcctDetailsTO.setShareAcctNo(ACCTTO);
        objShareAcctDetailsTO.setNoOfShares(CommonUtil.convertObjToInt(noOfShares));

        objShareAcctDetailsTO.setShareNoFrom(rangeFrom);
        objShareAcctDetailsTO.setShareNoTo(TORANGE);

        int maxSerialNo = getMaxSerialNo(ACCTTO);
        objShareAcctDetailsTO.setShareAcctDetNo(String.valueOf(maxSerialNo + 1));
        objShareAcctDetailsTO.setStatus(CommonConstants.STATUS_CREATED);
        objShareAcctDetailsTO.setStatusBy(userID);
        objShareAcctDetailsTO.setStatusDt(currDt);
        objShareAcctDetailsTO.setShareCertIssueDt(objShareAcctDetailsTO.getStatusDt());
        //__ Insert The Other half in ShareAcctDetails...
        sqlMap.executeUpdate("insertShareAcctDetailsTO", objShareAcctDetailsTO);

        objLogTO.setData(objShareAcctDetailsTO.toString());
        objLogTO.setPrimaryKey(objShareAcctDetailsTO.getKeyData());
        objLogDAO.addToLog(objLogTO);
    }

    //__ Starting is Different, but Same Ending...
    private void diffStartDiffEnd(ShareAcctDetailsTO objShareAcctDetailsTO, String rangeFrom, String rangeTo, String ACCTTO, LogDAO objLogDAO, LogTO objLogTO) throws Exception {
        //__ ShareAcctNo...
        final String FROMACCT = CommonUtil.convertObjToStr(objShareAcctDetailsTO.getShareAcctNo());

        //__ Ranges...
        final String FROMRANGE = CommonUtil.convertObjToStr(objShareAcctDetailsTO.getShareNoFrom());
        final String TORANGE = CommonUtil.convertObjToStr(objShareAcctDetailsTO.getShareNoTo());

        //__ Split the Range From and To into Sttring and Integer parts...
        final HashMap rangeStartMap = getShareNo(FROMRANGE);
        final HashMap rangeFromMap = getShareNo(rangeFrom);
        final HashMap rangeToMap = getShareNo(rangeTo);
        final HashMap rangeEndMap = getShareNo(TORANGE);


        final String START = CommonUtil.convertObjToStr(rangeStartMap.get("rangeFromStart"));
        final int LENGTH = CommonUtil.convertObjToInt(rangeFromMap.get("rangeFromLen"));
        int toStart = CommonUtil.convertObjToInt(rangeFromMap.get("rangeFromEnd"));

        //__ To get the Full String for the ShareAcctNo...
        final String rangeEnd = CommonUtil.lpad(String.valueOf(toStart - 1), LENGTH, '0');

        int start = CommonUtil.convertObjToInt(rangeStartMap.get("rangeFromEnd"));
        int end = CommonUtil.convertObjToInt(rangeFromMap.get("rangeFromEnd"));
        int noOfShares = end - start;

        //__ Update part...
        objShareAcctDetailsTO.setShareNoFrom(FROMRANGE);
        objShareAcctDetailsTO.setShareNoTo(START + rangeEnd);
        objShareAcctDetailsTO.setNoOfShares(CommonUtil.convertObjToInt(noOfShares));
        objShareAcctDetailsTO.setStatus(CommonConstants.STATUS_MODIFIED);
        objShareAcctDetailsTO.setStatusBy(userID);
        objShareAcctDetailsTO.setStatusDt(currDt);
        //__ Update the First Half....
        sqlMap.executeUpdate("updateShareAcctDetailsTO", objShareAcctDetailsTO);

        objLogTO.setData(objShareAcctDetailsTO.toString());
        objLogTO.setPrimaryKey(objShareAcctDetailsTO.getKeyData());
        objLogDAO.addToLog(objLogTO);


        //__ Insert Part (Selected Range)...
        start = CommonUtil.convertObjToInt(rangeFromMap.get("rangeFromEnd"));
        end = CommonUtil.convertObjToInt(rangeToMap.get("rangeFromEnd"));
        noOfShares = end - start + 1;
        int maxSerialNo = getMaxSerialNo(ACCTTO);
        java.util.Date certDt = objShareAcctDetailsTO.getShareCertIssueDt();
        objShareAcctDetailsTO.setShareAcctNo(ACCTTO);
        objShareAcctDetailsTO.setShareNoFrom(rangeFrom);
        objShareAcctDetailsTO.setShareNoTo(rangeTo);
        objShareAcctDetailsTO.setNoOfShares(CommonUtil.convertObjToInt(noOfShares));
        objShareAcctDetailsTO.setShareAcctDetNo(String.valueOf(maxSerialNo + 1));
        objShareAcctDetailsTO.setStatus(CommonConstants.STATUS_CREATED);
        objShareAcctDetailsTO.setStatusBy(userID);
        objShareAcctDetailsTO.setStatusDt(currDt);
        objShareAcctDetailsTO.setShareCertIssueDt(objShareAcctDetailsTO.getStatusDt());
        //__ Insert The Other half in ShareAcctDetails...
        sqlMap.executeUpdate("insertShareAcctDetailsTO", objShareAcctDetailsTO);

        objLogTO.setData(objShareAcctDetailsTO.toString());
        objLogTO.setPrimaryKey(objShareAcctDetailsTO.getKeyData());
        objLogDAO.addToLog(objLogTO);


        //__ Insert Part (Remaining Part)...
        toStart = CommonUtil.convertObjToInt(rangeToMap.get("rangeFromEnd"));

        //__ To get the Full String for the ShareAcctNo...
        final String rangeStart = CommonUtil.lpad(String.valueOf(toStart + 1), LENGTH, '0');

        start = CommonUtil.convertObjToInt(rangeToMap.get("rangeFromEnd"));
        end = CommonUtil.convertObjToInt(rangeEndMap.get("rangeFromEnd"));
        noOfShares = (end - start) + 1;
        maxSerialNo = getMaxSerialNo(FROMACCT);

        objShareAcctDetailsTO.setShareAcctNo(FROMACCT);
        objShareAcctDetailsTO.setShareNoFrom(START + rangeStart);
        objShareAcctDetailsTO.setShareNoTo(TORANGE);
        objShareAcctDetailsTO.setNoOfShares(CommonUtil.convertObjToInt(noOfShares));
        objShareAcctDetailsTO.setShareAcctDetNo(String.valueOf(maxSerialNo + 1));
        objShareAcctDetailsTO.setStatus(CommonConstants.STATUS_CREATED);
        objShareAcctDetailsTO.setStatusBy(userID);
        objShareAcctDetailsTO.setStatusDt(currDt);
        objShareAcctDetailsTO.setShareCertIssueDt(certDt);
        //__ Insert The Other half in ShareAcctDetails...
        sqlMap.executeUpdate("insertShareAcctDetailsTO", objShareAcctDetailsTO);

        objLogTO.setData(objShareAcctDetailsTO.toString());
        objLogTO.setPrimaryKey(objShareAcctDetailsTO.getKeyData());
        objLogDAO.addToLog(objLogTO);
    }

    //__ To get the Maximum of the Serial no for the Particular ShareAcctNo...
    private int getMaxSerialNo(String shareAcctNo) {
        int serialNo = 0;
        try {
            List list = (List) sqlMap.executeQueryForList("ShareTransfer.getMaxSerialNo", shareAcctNo);
            if (list.size() > 0) {
                final HashMap dataMap = (HashMap) list.get(0);
                serialNo = CommonUtil.convertObjToInt(dataMap.get("SERIAL_NO"));
            }
        } catch (Exception E) {
            System.out.println("Error in getMaxSerialNo()");
            E.printStackTrace();
        }
        return serialNo;
    }

    //__ To Calculate the Range distribution for the Transfered Shares...
    private HashMap getShareNo(String rangeFrom) {
        HashMap dataMap = new HashMap();
        //
        //__ To get the Lenth of the String ...
        int fromLen = rangeFrom.length();
        int strLen = 0;
        //__ To get the length of the Numerical part....
        for (int i = fromLen - 1; i >= 0; i--) {
            char lastChar = rangeFrom.charAt(i);
            if (Character.isDigit(lastChar)) {
                strLen++;
            } else {
                break;
            }
        }
        String rangeFromStart = rangeFrom.substring(0, (fromLen - strLen));
        int rangeFromEnd = Integer.parseInt(rangeFrom.substring(fromLen - strLen));

        dataMap.put("rangeFromStart", rangeFromStart);
        dataMap.put("rangeFromEnd", String.valueOf(rangeFromEnd));
        dataMap.put("rangeFromLen", String.valueOf(strLen));

        System.out.println("Returned dataMap: " + dataMap);
        return dataMap;
    }

    public HashMap executeQuery(HashMap obj) throws Exception {
        _branchCode = (String) obj.get(CommonConstants.BRANCH_ID);
        currDt = ServerUtil.getCurrentDate(_branchCode);
        return getData(obj);
    }

    private void destroyObjects() {
        objShareTransferTO = null;
    }

    private void dodeleteOrUpadate(HashMap map) throws Exception {
        String transType = "";
        String BatchId = "";
        objTransactionTO = new TransactionTO();
        LinkedHashMap TransactionDetailsMap = (LinkedHashMap) map.get("TransactionTO");
        System.out.println("TransactionDetailsMap---->" + TransactionDetailsMap);
        if (TransactionDetailsMap.size() > 0) {
            if (TransactionDetailsMap.containsKey("NOT_DELETED_TRANS_TOs")) {
                LinkedHashMap allowedTransDetailsTO = (LinkedHashMap) TransactionDetailsMap.get("NOT_DELETED_TRANS_TOs");
                for (int J = 1; J <= allowedTransDetailsTO.size(); J++) {
                    objTransactionTO = (TransactionTO) allowedTransDetailsTO.get(String.valueOf(J));
                    System.out.println("TransactionDetailsMap---->" + objTransactionTO);
                    transType = objTransactionTO.getTransType();
                    BatchId = objTransactionTO.getBatchId();
                    System.out.println("transType---->" + transType + "BatchId---------------------->" + BatchId);

                }
            }
        }
        objDividendBatchTO.setBatchID(BatchId);
        double oldAmount = 0;
        HashMap oldAmountMap = new HashMap();


        if (transType.equals("TRANSFER")) {
            HashMap shareAcctNoMap = new HashMap();
            shareAcctNoMap.put("BATCHID", BatchId);
            //            System.out.println("objDividendBatchTO.getBatchID());"+objDividendBatchTO.getBatchID());
            List lst = sqlMap.executeQueryForList("getBatchTxTransferTOsAuthorize", shareAcctNoMap);
            TxTransferTO txTransferTO = null;

            ArrayList transferList = new ArrayList();
            if (lst != null && lst.size() > 0) {
                for (int j = 0; j < lst.size(); j++) {
                    txTransferTO = (TxTransferTO) lst.get(j);
                    oldAmount = txTransferTO.getAmount().doubleValue();
                    txTransferTO.setInpAmount(new Double(oldAmount));
                    txTransferTO.setAmount(new Double(oldAmount));
                    txTransferTO.setStatus(CommonConstants.STATUS_DELETED);
                    txTransferTO.setStatusDt(currDt);
                    txTransferTO.setTransMode(CommonConstants.TX_TRANSFER);
                    oldAmountMap.put(txTransferTO.getTransId(), new Double(oldAmount));
                    transferList.add(txTransferTO);
                }
            } else {
                throw new Exception("Transfer Records Not Found");
            }
            TransferTrans transferTrans = new TransferTrans();
            transferTrans.doDebitCredit(transferList, _branchCode, false, objDividendBatchTO.getCommand());
            transferTrans = null;
            txTransferTO = null;
        } else {

            HashMap tempMap = new HashMap();
            List cLst1 = sqlMap.executeQueryForList("getSelectCashTransactionTO", objDividendBatchTO.getBatchID());
            if (cLst1 != null && cLst1.size() > 0) {
                CashTransactionTO txTransferTO1 = null;
                txTransferTO1 = (CashTransactionTO) cLst1.get(0);
                oldAmount = CommonUtil.convertObjToDouble(txTransferTO1.getAmount()).doubleValue();
                txTransferTO1.setInpAmount(new Double(oldAmount));
                txTransferTO1.setAmount(new Double(oldAmount));
                txTransferTO1.setCommand(objDividendBatchTO.getCommand());
                txTransferTO1.setStatus(CommonConstants.STATUS_DELETED);
                txTransferTO1.setStatusDt(currDt);

                map.put("PRODUCTTYPE", "GL");
                map.put("OLDAMOUNT", new Double(oldAmount));
                map.put("CashTransactionTO", txTransferTO1);
                CashTransactionDAO cashTransDAO = new CashTransactionDAO();
                cashTransDAO.execute(map, false);
            } else {
                throw new Exception("Cash Records Not Found");
            }
            cLst1 = null;

            oldAmountMap = null;
        }



    }
}
