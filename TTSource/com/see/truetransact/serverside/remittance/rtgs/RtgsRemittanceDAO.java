/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition.
 * 
 *
 * RtgsRemittanceDAO.java
 *
 * Created on Wed Feb 04 17:36:32 IST 2015
 */
package com.see.truetransact.serverside.remittance.rtgs;

import java.util.List;
import java.util.Date;
import java.util.HashMap;
import java.util.ArrayList;
import org.apache.log4j.Logger;
import java.util.LinkedHashMap;
import com.ibatis.db.sqlmap.SqlMap;
import com.see.truetransact.clientproxy.ProxyParameters;
import com.see.truetransact.serverside.TTDAO;
import com.see.truetransact.serverutil.ServerUtil;
import com.see.truetransact.commonutil.CommonUtil;
import com.see.truetransact.commonutil.CommonConstants;
import com.see.truetransact.commonutil.DateUtil;
import com.see.truetransact.commonutil.TTException;
import com.see.truetransact.servicelocator.ServiceLocator;
import com.see.truetransact.serverexception.TransRollbackException;
import com.see.truetransact.serverexception.ServiceLocatorException;
import com.see.truetransact.serverside.common.idgenerate.IDGenerateDAO;
import com.see.truetransact.serverside.common.transaction.TransferTrans;
import com.see.truetransact.serverside.transaction.transfer.TransferDAO;
import com.see.truetransact.serverside.common.transaction.TransactionDAO;
import com.see.truetransact.transferobject.remittance.rtgs.RtgsRemittanceTO;
import com.see.truetransact.transferobject.common.transaction.TransactionTO;
import com.see.truetransact.transferobject.transaction.transfer.TxTransferTO;
import com.see.truetransact.serverside.transaction.common.TransactionFactory;
import java.io.File;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * @author Suresh R
 *
 */
public class RtgsRemittanceDAO extends TTDAO {

    private static SqlMap sqlMap = null;
    private TransactionDAO transactionDAO = null;
    TransferDAO transferDAO = new TransferDAO();
    private Date currDt = null;
    HashMap returnMap = new HashMap();
    HashMap utrNumberMap = new HashMap();
    private final static Logger log = Logger.getLogger(RtgsRemittanceDAO.class);
    private int yearTobeAdded = 1900;
    TransactionTO transactionTO = new TransactionTO();
    private LinkedHashMap RTGSMap = null;
    private int constants = 2;

    /**
     * Creates a new instance of RemittanceIssueDAO
     */
    public RtgsRemittanceDAO() throws ServiceLocatorException {
        ServiceLocator locate = ServiceLocator.getInstance();
        sqlMap = (SqlMap) locate.getDAOSqlMap();
    }

    private HashMap getData(HashMap map) throws Exception {
        System.out.println("#### GetData map : " + map);
        returnMap = new HashMap();
        List RTGSlist = (List) sqlMap.executeQueryForList("getRTGSRemittanceTO", map);
        if (RTGSlist != null && RTGSlist.size() > 0) {
            LinkedHashMap ParMap = new LinkedHashMap();
            for (int i = RTGSlist.size(), j = 0; i > 0; i--, j++) {
                String st = ((RtgsRemittanceTO) RTGSlist.get(j)).getRtgs_ID();
                ParMap.put(j + 1, RTGSlist.get(j));
            }
            returnMap.put("RTGS_DATA", ParMap);
        }
        RTGSlist = null;

        String where = (String) map.get("RTGS_ID");
        List transList = (List) sqlMap.executeQueryForList("getSelectRemittanceIssueTransactionTO", where);
        if (transList != null && transList.size() > 0) {
            returnMap.put("TRANSACTION_LIST", transList);
            transList = null;
        }
        //getTransDetails(CommonUtil.convertObjToStr(map.get("RTGS_ID")));
        System.out.println("########## returnMap : " + returnMap);
        return returnMap;
    }

    private void getTransDetails(String batchId) throws Exception {
        HashMap getTransMap = new HashMap();
        getTransMap.put("BATCH_ID", batchId);
        getTransMap.put("TRANS_DT", currDt);
        getTransMap.put(CommonConstants.BRANCH_ID, _branchCode);
        List transList = (List) sqlMap.executeQueryForList("getTransferDetailsWithAccHeadDesc", getTransMap);
        if (transList != null && transList.size() > 0) {
            returnMap.put("TRANSFER_TRANS_LIST", transList);
        }
        getTransMap.clear();
        getTransMap = null;
        transList = null;
    }

    public HashMap executeQuery(HashMap obj) throws Exception {
        _branchCode = (String) obj.get(CommonConstants.BRANCH_ID);
        currDt = ServerUtil.getCurrentDate(_branchCode);
        return getData(obj);
    }

    public HashMap execute(HashMap map) throws Exception {
        try {
            System.out.println("######## RTGS REMITTANCE MAP DAO : " + map);
            returnMap = new HashMap();
            utrNumberMap = new HashMap();
            _branchCode = (String) map.get(CommonConstants.BRANCH_ID);
            currDt = ServerUtil.getCurrentDate(_branchCode);
            final String command = CommonUtil.convertObjToStr(map.get("COMMAND"));
            if (map.containsKey("RTGS_DATA")) {
                RTGSMap = (LinkedHashMap) map.get("RTGS_DATA");
            }
            if (command.equals(CommonConstants.TOSTATUS_INSERT)) {
                if ((RTGSMap != null && RTGSMap.size() > 0) && map.containsKey("TransactionTO")) {
                    ArrayList addList = new ArrayList(RTGSMap.keySet());
                    double allAmount = 0.0;
                    double totalAmount = 0.0;
                    for (int i = 0; i < addList.size(); i++) {
                        allAmount = 0.0;
                        totalAmount = 0.0;
                        RtgsRemittanceTO objRtgsRemittanceTO = (RtgsRemittanceTO) RTGSMap.get(addList.get(i));
                        allAmount = CommonUtil.convertObjToDouble(objRtgsRemittanceTO.getAmount())
                                + CommonUtil.convertObjToDouble(objRtgsRemittanceTO.getEx_Calculated())
                                + CommonUtil.convertObjToDouble(objRtgsRemittanceTO.getEx_Collected())
                                + CommonUtil.convertObjToDouble(objRtgsRemittanceTO.getService_Tax())
                                + CommonUtil.convertObjToDouble(objRtgsRemittanceTO.getCharges());
                        totalAmount = CommonUtil.convertObjToDouble(objRtgsRemittanceTO.getTotalAmt());
                        if (allAmount > totalAmount || allAmount < totalAmount) {
                            String ErrorMessage = "Total Amount is Wrong, Please Re-Enter the Amount ("
                                    + CommonUtil.convertObjToStr(objRtgsRemittanceTO.getBeneficiary_Name()) + " - "
                                    + CommonUtil.convertObjToStr(objRtgsRemittanceTO.getAccount_No() + ")");
                            System.out.println("########### allAmount   : " + allAmount);
                            System.out.println("########### totalAmount : " + totalAmount + "   ###### : " + CommonUtil.convertObjToStr(objRtgsRemittanceTO.getBeneficiary_Name()));
                            throw new TTException(ErrorMessage);
                        }
                    }
                    insertData(map);
                }
            } else if (command.equals(CommonConstants.TOSTATUS_UPDATE)) {
                ArrayList addList = new ArrayList(RTGSMap.keySet());
                double allAmount = 0.0;
                double totalAmount = 0.0;
                for (int i = 0; i < addList.size(); i++) {
                    RtgsRemittanceTO objRtgsRemittanceTO = (RtgsRemittanceTO) RTGSMap.get(addList.get(i));
                    allAmount = CommonUtil.convertObjToDouble(objRtgsRemittanceTO.getAmount())
                            + CommonUtil.convertObjToDouble(objRtgsRemittanceTO.getEx_Calculated())
                            + CommonUtil.convertObjToDouble(objRtgsRemittanceTO.getEx_Collected())
                            + CommonUtil.convertObjToDouble(objRtgsRemittanceTO.getService_Tax())
                            + CommonUtil.convertObjToDouble(objRtgsRemittanceTO.getCharges());
                    totalAmount = CommonUtil.convertObjToDouble(objRtgsRemittanceTO.getTotalAmt());
                    if (allAmount > totalAmount || allAmount < totalAmount) {
                        String ErrorMessage = "Total Amount is Wrong, Please Re-Enter the Amount ("
                                + CommonUtil.convertObjToStr(objRtgsRemittanceTO.getBeneficiary_Name()) + " - "
                                + CommonUtil.convertObjToStr(objRtgsRemittanceTO.getAccount_No() + ")");
                        System.out.println("########### allAmount   : " + allAmount);
                        System.out.println("########### totalAmount : " + totalAmount + "   ###### : " + CommonUtil.convertObjToStr(objRtgsRemittanceTO.getBeneficiary_Name()));
                        throw new TTException(ErrorMessage);
                    }
                }
                updateData(map);
            } else if (command.equals(CommonConstants.TOSTATUS_DELETE)) {
                //deleteData(map);
            } else if (map.containsKey(CommonConstants.AUTHORIZEMAP)) {
                HashMap authMap = (HashMap) map.get(CommonConstants.AUTHORIZEMAP);
                if (authMap != null && (RTGSMap != null && RTGSMap.size() > 0)) {
                    authorize(authMap, map);
                }
            }
            map = null;
            destroyObjects();
            System.out.println("######## returnMap DAO : " + returnMap);
            return returnMap;
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
    }

    private void updateData(HashMap map) throws Exception {
        try {
            sqlMap.startTransaction();
            String RTGS_ID = "";
            RTGS_ID = CommonUtil.convertObjToStr(map.get("RTGS_ID"));
            returnMap.put("RTGS_ID", RTGS_ID);
            returnMap.put("BATCH_ID", RTGS_ID);
            if (RTGS_ID != null && RTGS_ID.length() > 0) {
                sqlMap.executeUpdate("updateRTGSRemittance", returnMap);
                sqlMap.executeUpdate("deleteRemittanceIssueTrans", returnMap);
                insertRTGSdetails(map, RTGS_ID);
            }
            sqlMap.commitTransaction();
            //getTransDetails(RTGS_ID);
        } catch (Exception e) {
            sqlMap.rollbackTransaction();
            e.printStackTrace();
            throw e;
        }
        map = null;
    }

    private void insertData(HashMap map) throws Exception {
        try {
            if(!map.containsKey("otherBankTransaction")){
                sqlMap.startTransaction();
            }
            String RTGS_ID = "";
            RTGS_ID = getRTGS_ID();
            returnMap.put("RTGS_ID", RTGS_ID);
            //Transaction Start
            if (RTGS_ID != null && RTGS_ID.length() > 0) {
                //transactionPart(map, RTGS_ID);    //Commentted By Suresh R    After discussed with Rajesh/Ravi/Srinath Sir.
                insertRTGSdetails(map, RTGS_ID);
            }
            if(!map.containsKey("otherBankTransaction")){
                sqlMap.commitTransaction();
            }
            //getTransDetails(RTGS_ID);
        } catch (Exception e) {
            sqlMap.rollbackTransaction();
            e.printStackTrace();
            throw e;
        }
        map = null;
    }

    public void transactionPart(HashMap map, String RTGS_ID, RtgsRemittanceTO rtgsRemitTo) throws Exception {
        System.out.println("########### TRANSACTION PART STARTED ####### ");
        RtgsRemittanceTO objRtgsRemittanceTO = null;
        String BRANCH_ID = CommonUtil.convertObjToStr(map.get("BRANCH_CODE"));
        String remarks = "";
        HashMap txMap = new HashMap();
        HashMap transMap = new HashMap();
        HashMap debitMap = new HashMap();
        double transAmt = 0.0;
        List creditList = null;
        List creditRTGSList = null;
        HashMap creditMap = new HashMap();
        HashMap creditRTGSMap = new HashMap();
        ArrayList transferList = new ArrayList();
        HashMap tansactionMap = new HashMap();
        TxTransferTO transferTo = new TxTransferTO();
        ArrayList TxTransferTO = new ArrayList();
        TransferTrans transferTrans = new TransferTrans();
        transferTrans.setInitiatedBranch(BRANCH_ID);
        TransferTrans objTransferTrans = new TransferTrans();
        objTransferTrans.setInitiatedBranch(_branchCode);
        transactionDAO = new TransactionDAO(CommonConstants.DEBIT);
        transactionDAO.setInitiatedBranch(_branchCode);
        transactionDAO.setCommandMode(CommonConstants.TOSTATUS_INSERT);
        transactionDAO.setTransactionType(transactionDAO.TRANSFER);
        if (RTGSMap != null && RTGSMap.size() > 0) {
            ArrayList addList = new ArrayList(RTGSMap.keySet());
            for (int i = 0; i < addList.size(); i++) {
                objRtgsRemittanceTO = (RtgsRemittanceTO) RTGSMap.get(addList.get(i));
                creditMap.put("PROD_ID", objRtgsRemittanceTO.getProdId());
                creditRTGSMap.put("PROD_ID", objRtgsRemittanceTO.getProdId());
                if (i == 0) {
                    creditList = (List) sqlMap.executeQueryForList("getRTGSHeadDetails", creditMap);
                    creditRTGSList = (List) sqlMap.executeQueryForList("getRTGSAccountHeads", creditRTGSMap);
                }
                if (creditList != null && creditList.size() > 0) {
                    creditMap = (HashMap) creditList.get(0);
                    if (creditRTGSList != null && creditRTGSList.size() > 0) {
                        creditRTGSMap = (HashMap) creditRTGSList.get(0);
                        HashMap transactionDetailsMap = (LinkedHashMap) map.get("TransactionTO");
                        LinkedHashMap allowedTransDetailsTO = new LinkedHashMap();
                        if (transactionDetailsMap.size() > 0 && transactionDetailsMap.containsKey("NOT_DELETED_TRANS_TOs")) {
                            allowedTransDetailsTO = (LinkedHashMap) transactionDetailsMap.get("NOT_DELETED_TRANS_TOs");
                            transactionTO = (TransactionTO) allowedTransDetailsTO.get(String.valueOf(1));
                            transAmt = CommonUtil.convertObjToDouble(transactionTO.getTransAmt()).doubleValue();
                            if (transAmt > 0) {
                                //DEBIT PART
                                if (i == 0) {
                                    if (!transactionTO.getProductType().equals("") && transactionTO.getProductType().equals("GL") && transactionTO.getProductId().equals("")) {
                                        txMap.put(TransferTrans.DR_AC_HD, (String) transactionTO.getDebitAcctNo());
                                        txMap.put(TransferTrans.DR_PROD_TYPE, TransactionFactory.GL);
                                        txMap.put("TRANS_MOD_TYPE", TransactionFactory.GL);
                                    } else {
                                        txMap.put(TransferTrans.DR_PROD_TYPE, CommonUtil.convertObjToStr(transactionTO.getProductType()));
                                        txMap.put(TransferTrans.DR_ACT_NUM, CommonUtil.convertObjToStr(transactionTO.getDebitAcctNo()));
                                        txMap.put(TransferTrans.DR_PROD_ID, CommonUtil.convertObjToStr(transactionTO.getProductId()));
                                        txMap.put("TRANS_MOD_TYPE", transactionTO.getProductType());
                                    }
                                    if (CommonUtil.convertObjToStr(transactionTO.getProductType()).equals("AD")) {//Advanes basic validation need to check abi 29-02-2016
                                        //map.put("DEBIT_LOAN_TYPE", CommonUtil.convertObjToStr(transactionTO.getLoanDebitType()));
                                        //txMap.put(TransferTrans.PARTICULARS, CommonUtil.convertObjToStr(transactionTO.getLoanDebitType()));
                                    }
                                    if (CommonUtil.convertObjToStr(rtgsRemitTo.getRemarks()).length() > 10) {
                                        remarks = CommonUtil.convertObjToStr(rtgsRemitTo.getRemarks()).substring(0, 9);
                                    } else {
                                        remarks = CommonUtil.convertObjToStr(rtgsRemitTo.getRemarks());
                                    }
                                    txMap.put(TransferTrans.PARTICULARS, rtgsRemitTo.getProdId() + " " + rtgsRemitTo.getBeneficiary_Name() + " " + remarks + " " + rtgsRemitTo.getUtrNumber() + " " + rtgsRemitTo.getBeneficiary_IFSC_Code());//Changed By Revathi.L reff by Mr.Srinath 06/06/2017
                                    //txMap.put("AUTHORIZEREMARKS", "RTGS/NEFT");
                                    txMap.put(TransferTrans.AUTHORIZE_STATUS_2, "ENTERED_AMOUNT");
                                    txMap.put(TransferTrans.DR_INST_TYPE, transactionTO.getInstType());
                                    txMap.put(TransferTrans.DR_INSTRUMENT_1, transactionTO.getChequeNo());
                                    txMap.put(TransferTrans.DR_INSTRUMENT_2, transactionTO.getChequeNo2());
                                    txMap.put(TransferTrans.DR_BRANCH, _branchCode);
                                    txMap.put(CommonConstants.USER_ID, map.get("USER_ID"));
                                    txMap.put("SCREEN_NAME","RTGS/NEFT");
                                    transferList.add(objTransferTrans.getDebitTransferTO(txMap, transAmt-objRtgsRemittanceTO.getTotalGstAmt()));
                                }
                                //CREDIT CHARGES
                                if (CommonUtil.convertObjToDouble(objRtgsRemittanceTO.getCharges()) > 0) {      //If Charges Amount>0
                                    txMap = new HashMap();
                                    txMap.put(TransferTrans.CR_AC_HD, creditRTGSMap.get("POSTAGE_HD"));
                                    txMap.put("AUTHORIZEREMARKS", "RTGS/NEFT CHARGES");
                                    txMap.put(TransferTrans.CR_BRANCH, BRANCH_ID);//creditMap.get("BANK_CODE"));
                                    txMap.put(TransferTrans.CR_PROD_TYPE, TransactionFactory.GL);
                                    txMap.put(TransferTrans.PARTICULARS, "RTGS/NEFT CHARGES");
                                    txMap.put(TransferTrans.AUTHORIZE_STATUS_2, "ENTERED_AMOUNT");
                                    txMap.put(CommonConstants.USER_ID, map.get("USER_ID"));
                                    txMap.put("SCREEN_NAME","RTGS/NEFT");
                                    txMap.put("TRANS_MOD_TYPE", TransactionFactory.GL);
                                    transferList.add(objTransferTrans.getCreditTransferTO(txMap, CommonUtil.convertObjToDouble(objRtgsRemittanceTO.getCharges())));
                                }
                                //CREDIT SERVICE_TAX
                                if (CommonUtil.convertObjToDouble(objRtgsRemittanceTO.getService_Tax()) > 0) {      //If ServiceTax Amount>0
                                    txMap = new HashMap();
                                    txMap.put(TransferTrans.CR_AC_HD, creditRTGSMap.get("OTHER_CHRG_HD"));
                                    txMap.put("AUTHORIZEREMARKS", "RTGS/NEFT SERVICE_TAX");
                                    txMap.put(TransferTrans.CR_BRANCH, BRANCH_ID);//creditMap.get("BANK_CODE"));
                                    txMap.put(TransferTrans.CR_PROD_TYPE, TransactionFactory.GL);
                                    txMap.put(TransferTrans.PARTICULARS, "RTGS/NEFT SERVICE_TAX");
                                    txMap.put(TransferTrans.AUTHORIZE_STATUS_2, "ENTERED_AMOUNT");
                                    txMap.put(CommonConstants.USER_ID, map.get("USER_ID"));
                                    txMap.put("SCREEN_NAME","RTGS/NEFT");
                                    txMap.put("TRANS_MOD_TYPE", TransactionFactory.GL);
                                    transferList.add(objTransferTrans.getCreditTransferTO(txMap, CommonUtil.convertObjToDouble(objRtgsRemittanceTO.getService_Tax())));
                                }
                                //GST                                
                                /*if (CommonUtil.convertObjToDouble(objRtgsRemittanceTO.getSgstAmt()) > 0) {      //If ServiceTax Amount>0
                                    txMap = new HashMap();
                                    txMap.put(TransferTrans.CR_AC_HD, creditRTGSMap.get("SGST_HD"));
                                    txMap.put("AUTHORIZEREMARKS", "RTGS/NEFT SGST");
                                    txMap.put(TransferTrans.CR_BRANCH, BRANCH_ID);//creditMap.get("BANK_CODE"));
                                    txMap.put(TransferTrans.CR_PROD_TYPE, TransactionFactory.GL);
                                    txMap.put(TransferTrans.PARTICULARS, "RTGS/NEFT SGST");
                                    txMap.put(TransferTrans.AUTHORIZE_STATUS_2, "ENTERED_AMOUNT");
                                    txMap.put(CommonConstants.USER_ID, map.get("USER_ID"));
                                    txMap.put("SCREEN_NAME","RTGS/NEFT");
                                    txMap.put("TRANS_MOD_TYPE", TransactionFactory.GL);
                                    transferList.add(objTransferTrans.getCreditTransferTO(txMap, CommonUtil.convertObjToDouble(objRtgsRemittanceTO.getSgstAmt())));
                                }
                                 if (CommonUtil.convertObjToDouble(objRtgsRemittanceTO.getCgstAmt()) > 0) {      //If ServiceTax Amount>0
                                    txMap = new HashMap();
                                    txMap.put(TransferTrans.CR_AC_HD, creditRTGSMap.get("CGST_HD"));
                                    txMap.put("AUTHORIZEREMARKS", "RTGS/NEFT CGST");
                                    txMap.put(TransferTrans.CR_BRANCH, BRANCH_ID);//creditMap.get("BANK_CODE"));
                                    txMap.put(TransferTrans.CR_PROD_TYPE, TransactionFactory.GL);
                                    txMap.put(TransferTrans.PARTICULARS, "RTGS/NEFT CGST");
                                    txMap.put(TransferTrans.AUTHORIZE_STATUS_2, "ENTERED_AMOUNT");
                                    txMap.put(CommonConstants.USER_ID, map.get("USER_ID"));
                                    txMap.put("SCREEN_NAME","RTGS/NEFT");
                                    txMap.put("TRANS_MOD_TYPE", TransactionFactory.GL);
                                    transferList.add(objTransferTrans.getCreditTransferTO(txMap, CommonUtil.convertObjToDouble(objRtgsRemittanceTO.getCgstAmt())));
                                }*/
                                if (CommonUtil.convertObjToDouble(objRtgsRemittanceTO.getTotalGstAmt()) > 0) {      //If ServiceTax Amount>0
                                    txMap = new HashMap();
                                    txMap.put(TransferTrans.DR_AC_HD, creditRTGSMap.get("SGST_HD"));
                                    txMap.put("AUTHORIZEREMARKS", "RTGS/NEFT GST");
                                    txMap.put(TransferTrans.DR_BRANCH, BRANCH_ID);//creditMap.get("BANK_CODE"));
                                    txMap.put(TransferTrans.DR_PROD_TYPE, TransactionFactory.GL);
                                    txMap.put(TransferTrans.PARTICULARS, "RTGS/NEFT GST");
                                    txMap.put(TransferTrans.AUTHORIZE_STATUS_2, "ENTERED_AMOUNT");
                                    txMap.put(CommonConstants.USER_ID, map.get("USER_ID"));
                                    txMap.put("SCREEN_NAME","RTGS/NEFT");
                                    txMap.put("TRANS_MOD_TYPE", TransactionFactory.GL);
                                    transferList.add(objTransferTrans.getDebitTransferTO(txMap, CommonUtil.convertObjToDouble(objRtgsRemittanceTO.getTotalGstAmt())));
                                }
                                //END
                                //CREDIT RTGS_AMOUNT
                                if (CommonUtil.convertObjToDouble(objRtgsRemittanceTO.getAmount()) > 0) { //RTGS Amount Cr to RTGS_GL
                                    txMap = new HashMap();
                                    if(CommonUtil.convertObjToStr(creditRTGSMap.get("RTGS_NEFT_GL_TYPE")).equals("Y")){
                                        txMap.put(TransferTrans.CR_AC_HD, creditRTGSMap.get("ISSUE_HD"));
                                        txMap.put(TransferTrans.CR_PROD_TYPE, TransactionFactory.GL);
                                        txMap.put(TransferTrans.CR_BRANCH, creditMap.get("BANK_CODE"));
                                        txMap.put("TRANS_MOD_TYPE", TransactionFactory.GL);
                                    }else if(CommonUtil.convertObjToStr(creditRTGSMap.get("RTGS_NEFT_GL_TYPE")).equals("N")){
                                        txMap.put(TransferTrans.CR_ACT_NUM, creditRTGSMap.get("RTGS_NEFT_ACT_NUM"));
                                        txMap.put(TransferTrans.CR_PROD_ID, creditRTGSMap.get("RTGS_NEFT_PROD_ID"));
                                        txMap.put(TransferTrans.CR_PROD_TYPE, TransactionFactory.OTHERBANKACTS);
                                        txMap.put(TransferTrans.CR_BRANCH, creditMap.get("BANK_CODE"));
                                        txMap.put("TRANS_MOD_TYPE", TransactionFactory.OTHERBANKACTS);
                                    }
                                    txMap.put("AUTHORIZEREMARKS", "RTGS/NEFT");
                                    txMap.put(TransferTrans.PARTICULARS, "RTGS/NEFT");
                                    txMap.put(TransferTrans.AUTHORIZE_STATUS_2, "ENTERED_AMOUNT");
                                    txMap.put(CommonConstants.USER_ID, map.get("USER_ID"));
                                    txMap.put("SCREEN_NAME","RTGS/NEFT");
                                    transferList.add(objTransferTrans.getCreditTransferTO(txMap, CommonUtil.convertObjToDouble(objRtgsRemittanceTO.getAmount())));
                                }
                            }
                        }
                    }
                }
            }
            //Calling Transfer DAO Method
            if (transferList != null && transferList.size() > 0) {
                System.out.println("########### transferList         : " + transferList);
                doDebitCredit(transferList, _branchCode, false, map, RTGS_ID);
            }
        }
    }

    private void doDebitCredit(ArrayList batchList, String branchCode, boolean isAutoAuthorize, HashMap map, String RTGS_ID) throws Exception {
        TransferDAO transferDAO = new TransferDAO();
        HashMap data = new HashMap();
        data.put("TxTransferTO", batchList);
        data.put("COMMAND", map.get("COMMAND"));
        data.put("INITIATED_BRANCH", _branchCode);
        data.put(CommonConstants.BRANCH_ID, branchCode);
        data.put(CommonConstants.USER_ID, map.get("USER_ID"));
        //Module & screen code changed by Kannan
        data.put(CommonConstants.MODULE, map.get(CommonConstants.MODULE));
        data.put(CommonConstants.SCREEN, map.get(CommonConstants.SCREEN));
        data.put(CommonConstants.IP_ADDR, map.get(CommonConstants.IP_ADDR));
        data.put("MODE", "MODE");
        data.put("LINK_BATCH_ID", RTGS_ID);
        if (CommonUtil.convertObjToStr(map.get("DEBIT_LOAN_TYPE")).equals("DP")) { // need to check validation authorize time also
            data.put("DEBIT_LOAN_TYPE", "DP");
        }
        HashMap loanDataMap = new HashMap();
        loanDataMap = transferDAO.execute(data, false);
        System.out.println("############# loanDataMap : " + loanDataMap);
        if (loanDataMap != null && loanDataMap.containsKey("TRANS_ID")) {
            loanDataMap.put("RTGS_ID", RTGS_ID);
            sqlMap.executeUpdate("updateRTGSBatchID", loanDataMap);
        }
        //Insert Remit Issue Trans Table
        /*if (transactionTO == null) {
         transactionTO = new TransactionTO();
         }
         transactionTO.setBatchId(RTGS_ID);
         transactionTO.setBatchDt(currDt);
         transactionTO.setTransId(RTGS_ID);
         transactionTO.setStatus(CommonUtil.convertObjToStr(map.get("COMMAND")));
         transactionTO.setBranchId(_branchCode);
         sqlMap.executeUpdate("insertRemittanceIssueTransactionTO", transactionTO);
         //Insert RTGS_REMIT_ISSUE  table
         if (RTGSMap != null && RTGSMap.size() > 0) {
         ArrayList addList = new ArrayList(RTGSMap.keySet());
         String RTGS_UNIQUE_ID = "";
         for (int i = 0; i < addList.size(); i++) {
         RTGS_UNIQUE_ID = getRTGS_Unique_ID();
         RtgsRemittanceTO objRtgsRemittanceTO = (RtgsRemittanceTO) RTGSMap.get(addList.get(i));
         objRtgsRemittanceTO.setRtgs_ID(RTGS_ID);
         objRtgsRemittanceTO.setRtgs_Unique_ID(RTGS_UNIQUE_ID);
         objRtgsRemittanceTO.setBatchId(CommonUtil.convertObjToStr(loanDataMap.get("TRANS_ID")));
         objRtgsRemittanceTO.setTrans_type("OUTWARD");
         objRtgsRemittanceTO.setBatchDt(currDt);
         objRtgsRemittanceTO.setFileStatus("DELIVERED");
         objRtgsRemittanceTO.setN06processDt(currDt);
         sqlMap.executeUpdate("insertRTGSRemittanceTO", objRtgsRemittanceTO);
         objRtgsRemittanceTO = null;
         }
         }*/
    }

    private void insertRTGSdetails(HashMap map, String RTGS_ID) throws Exception {
        //Insert Remit Issue Trans Table
        if (map.containsKey("TransactionTO")) {
            transactionTO = new TransactionTO();
            HashMap transactionDetailsMap = (LinkedHashMap) map.get("TransactionTO");
            LinkedHashMap allowedTransDetailsTO = new LinkedHashMap();
            if (transactionDetailsMap.size() > 0 && transactionDetailsMap.containsKey("NOT_DELETED_TRANS_TOs")) {
                allowedTransDetailsTO = (LinkedHashMap) transactionDetailsMap.get("NOT_DELETED_TRANS_TOs");
                transactionTO = (TransactionTO) allowedTransDetailsTO.get(String.valueOf(1));
                transactionTO.setBatchId(RTGS_ID);
                transactionTO.setBatchDt(currDt);
                transactionTO.setTransId(RTGS_ID);
                transactionTO.setStatus(CommonUtil.convertObjToStr(map.get("COMMAND")));
                transactionTO.setBranchId(_branchCode);
                sqlMap.executeUpdate("insertRemittanceIssueTransactionTO", transactionTO);
                if (RTGSMap != null && RTGSMap.size() > 0) {                    //Insert RTGS_REMIT_ISSUE  table
                    ArrayList addList = new ArrayList(RTGSMap.keySet());
                    String RTGS_UNIQUE_ID = "";
                    for (int i = 0; i < addList.size(); i++) {
                        RTGS_UNIQUE_ID = getRTGS_Unique_ID();
                        RtgsRemittanceTO objRtgsRemittanceTO = (RtgsRemittanceTO) RTGSMap.get(addList.get(i));
                        objRtgsRemittanceTO.setRtgs_ID(RTGS_ID);
                        objRtgsRemittanceTO.setRtgs_Unique_ID(RTGS_UNIQUE_ID);
                        objRtgsRemittanceTO.setBatchId("");
                        objRtgsRemittanceTO.setTrans_type("OUTWARD");
                        objRtgsRemittanceTO.setBatchDt(currDt);
                        objRtgsRemittanceTO.setFileStatus("PROGRESS");
                        objRtgsRemittanceTO.setN06processDt(currDt);
                        objRtgsRemittanceTO.setInitiatedBranch(_branchCode);
                        sqlMap.executeUpdate("insertRTGSRemittanceTO", objRtgsRemittanceTO);
                        objRtgsRemittanceTO = null;
                    }
                }
            }
        }
    }

    private String getRTGS_ID() throws Exception {
        final IDGenerateDAO dao = new IDGenerateDAO();
        final HashMap where = new HashMap();
        where.put(CommonConstants.MAP_WHERE, "RTGS_ID");
        String rtgs_ID = (String) (dao.executeQuery(where)).get(CommonConstants.DATA);
        return rtgs_ID;
    }

    private String getRTGS_Unique_ID() throws Exception {
        final IDGenerateDAO dao = new IDGenerateDAO();
        final HashMap where = new HashMap();
        where.put(CommonConstants.MAP_WHERE, "RTGS_UNIQUE_ID");
        String rtgs_ID = (String) (dao.executeQuery(where)).get(CommonConstants.DATA);
        return rtgs_ID;
    }

    private String getCEDGEuNIQUE_NEFT_ID(String type) throws Exception {
        final IDGenerateDAO dao = new IDGenerateDAO();
        final HashMap where = new HashMap();
        if (type.equals("NEFT")) {
            where.put(CommonConstants.MAP_WHERE, "CEDGE_ID");
        } else {
            where.put(CommonConstants.MAP_WHERE, "CEDGE_ID_RTGS");
        }
        String rtgs_ID = (String) (dao.executeQuery(where)).get(CommonConstants.DATA);
        return rtgs_ID;
    }

    private void authorize(HashMap map, HashMap transmap) throws Exception {
        ArrayList selectedList = (ArrayList) map.get(CommonConstants.AUTHORIZEDATA);
        HashMap AuthMap = new HashMap();
        AuthMap = (HashMap) selectedList.get(0);
        AuthMap.put("FILTERED_LIST", CommonUtil.convertObjToStr(map.get("FILTERED_LIST")));
        System.out.println("########## AuthMap : " + AuthMap);
        try {
            if(!transmap.containsKey("otherBankTransaction")){
                sqlMap.startTransaction();
            }
            doTransaction(AuthMap, transmap);
            if(!transmap.containsKey("otherBankTransaction")){
                sqlMap.commitTransaction();
            }
        } catch (Exception e) {
            sqlMap.rollbackTransaction();
            e.printStackTrace();
            throw e; // return exception to UI
        }
    }

    public void doTransaction(HashMap AuthMap, HashMap map) throws Exception {
        String filePath = "";
        try {
            RtgsRemittanceTO resultTo = new RtgsRemittanceTO();
            RtgsRemittanceTO resultTofirstRec = new RtgsRemittanceTO();
            List utraddedList = new ArrayList();
            DateFormat years2digit = new SimpleDateFormat("yy");
            StringBuffer dayInYearbuffer = new StringBuffer();
            Calendar calender = Calendar.getInstance();
            String dayinyearStr = "";
            int daysInYear = 0;
            if (AuthMap != null && AuthMap.get("RTGS_ID") != null && !AuthMap.get("RTGS_ID").equals("")) {
                String authorizeStatus = CommonUtil.convertObjToStr(AuthMap.get("STATUS"));
                String linkBatchId = CommonUtil.convertObjToStr(AuthMap.get("RTGS_ID"));
                if (authorizeStatus.equals("AUTHORIZED")) {
                    List RTGSlist = (List) sqlMap.executeQueryForList("getRTGSRemittanceTODetails", AuthMap);
                    if (RTGSlist != null && RTGSlist.size() > 0) {
                        for (int i = 0; i < RTGSlist.size(); i++) {//Added By Suresh 07-Dec-2016  To Create Multiple File
                            resultTo = (RtgsRemittanceTO) RTGSlist.get(i);
                            String neftgenId = getCEDGEuNIQUE_NEFT_ID(CommonUtil.convertObjToStr(resultTo.getProdId()));
                            Calendar localCalendar = Calendar.getInstance(TimeZone.getDefault());
                            daysInYear = localCalendar.get(Calendar.DAY_OF_YEAR);
                            dayinyearStr = String.valueOf(daysInYear);
                            dayInYearbuffer = new StringBuffer();
                            System.out.println("dayinyearStr#######" + dayinyearStr + "daysInYear$$$$$" + daysInYear + "dayinyearStr.length()" + dayinyearStr.length());
                            if (dayinyearStr.length() == 2) {
                                dayInYearbuffer.append("0" + dayinyearStr);
                            } else if (dayinyearStr.length() == 1) {
                                dayInYearbuffer.append("00" + dayinyearStr);
                            } else {
                                dayInYearbuffer.append(dayinyearStr);
                            }
                            System.out.println("UTR Number : "+neftgenId);
                            if (CommonUtil.convertObjToStr(resultTo.getProdId()).equals("NEFT")) {
                                neftgenId = CommonConstants.RTGS_NEFT_CLIENT_CODE + years2digit.format(calender.getTime()) + dayInYearbuffer + neftgenId.substring(2, 8);
                            } else {
                                neftgenId = CommonConstants.RTGS_NEFT_CLIENT_CODE + years2digit.format(calender.getTime()) + dayInYearbuffer + "0" + neftgenId.substring(2, 8); // TRANSACTION REF NO SHOULD BE IN 16 DIGIT
                            }
                            System.out.println("UTR Number : "+neftgenId+" RTGS_NEFT_CLIENT_CODE : "+CommonConstants.RTGS_NEFT_CLIENT_CODE+" years2digit : "+years2digit.format(calender.getTime())+" dayInYearbuffer : "+dayInYearbuffer+" neftgenId substring : "+neftgenId.substring(2, 8));
                            resultTo.setUtrNumber(neftgenId);
                            if (i == 0) {
                                resultTofirstRec = (RtgsRemittanceTO) resultTo;
                            }
                            utraddedList.add(resultTo);
                        }
                        if (utrNumberMap != null && utrNumberMap.size() > 0) {        //Return UTR Numbers Added By Suresh R
                            returnMap.put("UTR_NUMBER", utrNumberMap);
                        }
                    }
                }
                if (authorizeStatus.equals("AUTHORIZED")) {
                    returnMap.put("RTGS_ID", linkBatchId);
                    System.out.println("resultTo.getUtrNumber()" + resultTo.getUtrNumber());
                    transactionPart(map, linkBatchId, resultTofirstRec);                          //INSERT TRANSACTIONS
                    HashMap transAuthMap = new HashMap();
                    transAuthMap.put(CommonConstants.BRANCH_ID, _branchCode);
                    transAuthMap.put(CommonConstants.USER_ID, map.get(CommonConstants.USER_ID));
                    //Added by Kannan
                    transAuthMap.put(CommonConstants.IP_ADDR, CommonUtil.convertObjToStr(map.get(CommonConstants.IP_ADDR)));
                    transAuthMap.put(CommonConstants.MODULE, CommonUtil.convertObjToStr(map.get(CommonConstants.MODULE)));
                    transAuthMap.put(CommonConstants.SCREEN, CommonUtil.convertObjToStr(map.get(CommonConstants.SCREEN)));
                    transactionDAO = new TransactionDAO(CommonConstants.DEBIT);
                    transAuthMap.put("DEBIT_LOAN_TYPE", "DP");
                    TransactionDAO.authorizeCashAndTransferWithoutRemitIssueDet(linkBatchId, authorizeStatus, transAuthMap);
                    transAuthMap = null;
                }

                if (authorizeStatus.equals("AUTHORIZED")) {//Added By Suresh 07-Dec-2016  To Create Multiple File
                    if (utraddedList != null && utraddedList.size() > 0) {
                        for (int i = 0; i < utraddedList.size(); i++) {//Added By Suresh 07-Dec-2016  To Create Multiple File
                            resultTo = (RtgsRemittanceTO) utraddedList.get(i);
                            updateAuthorizeStatus(AuthMap, map, resultTo);
                            if (i == 0) {
                                resultTofirstRec = (RtgsRemittanceTO) resultTo;
                            }
                        }
                        if (utrNumberMap != null && utrNumberMap.size() > 0) {        //Return UTR Numbers Added By Suresh R
                            returnMap.put("UTR_NUMBER", utrNumberMap);
                        }
                    }
                } else {
                    AuthMap.put("SEQUNCE_NO", null);
                    AuthMap.put("UTR_NUMBER", null);
                    sqlMap.executeUpdate("rejectRTGSDetails", AuthMap);
                }
            }
        } catch (Exception e) {
            sqlMap.rollbackTransaction();
            e.printStackTrace();
            throw e;
        }
    }

    private void updateAuthorizeStatus(HashMap authMap, HashMap map, RtgsRemittanceTO resultTo) throws Exception {       //This Method Added By Abi
        HashMap singleUTRMap = new HashMap();
        String neftUniqueId = "", neftgenId = "", sequnceNum = "";
        if (resultTo != null) {     // Added By Suresh R  07-Dec-2016
            try {
                System.out.println("UTR Number : "+resultTo.getUtrNumber()+"UTR Number length : "+resultTo.getUtrNumber().length());
                //neftgenId,sequnceNum code changes done by Kannan AR after discuss with Mr.Sathya, Mr.Suresh
                //neftgenId = CommonUtil.convertObjToStr(resultTo.getUtrNumber()).substring(8, resultTo.getUtrNumber().length());
                int length = CommonUtil.convertObjToInt(CommonConstants.RTGS_NEFT_CLIENT_CODE.length());
                neftgenId = CommonUtil.convertObjToStr(resultTo.getUtrNumber()).substring(length, resultTo.getUtrNumber().length());
                neftUniqueId = CommonUtil.convertObjToStr(resultTo.getUtrNumber());
                if (neftgenId != null && neftgenId.length() > 0) {
                    if (CommonUtil.convertObjToStr(resultTo.getProdId()).equals("NEFT")) {
                        //sequnceNum = "000" + CommonUtil.convertObjToStr(neftgenId.substring(2, 8));
                        sequnceNum = CommonUtil.convertObjToStr(neftgenId);                        
                        System.out.println(" neft started +neftUniqueId" + neftgenId + "neftgenId.substring(2, 8)" + neftgenId.substring(2, 8));
                        neftUniqueId = CommonUtil.convertObjToStr(resultTo.getUtrNumber());
                    }
                    authMap.put("SEQUNCE_NO", sequnceNum);
                    authMap.put("UTR_NUMBER", neftUniqueId);
                    resultTo.setUtrNumber(neftUniqueId);
                    authMap.put("RTGS_UNIQUE_ID", CommonUtil.convertObjToStr(resultTo.getRtgs_Unique_ID()));
                    singleUTRMap.put("UTR_NUMBER", neftUniqueId);   //Added By Suresh R     Return UTR Numbers.
                    singleUTRMap.put("RTGS_ID", authMap.get("RTGS_ID"));
                    utrNumberMap.put(neftUniqueId, singleUTRMap);
                    authMap.put("FILE_STATUS", "DELIVERED");
                    sqlMap.executeUpdate("authorizeRTGSDetails", authMap);
                }
            } catch (Exception e) {
                e.printStackTrace();
                throw new TTException(e);
            }
        }
    }

    private void destroyObjects() {
        RTGSMap = null;
        transactionDAO = null;

    }
}
