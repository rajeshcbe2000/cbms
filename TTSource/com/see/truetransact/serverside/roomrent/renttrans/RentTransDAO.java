/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition.
 **
 *
 * RentTransDAO.java
 *
 * Created on Thu Jan 20 17:19:05 IST 2005
 */
package com.see.truetransact.serverside.roomrent.renttrans;

import com.ibatis.db.sqlmap.SqlMap;
import com.see.truetransact.serverutil.ServerUtil;

import com.see.truetransact.commonutil.CommonConstants;
import com.see.truetransact.commonutil.NoCommandException;
import com.see.truetransact.commonutil.CommonUtil;
import com.see.truetransact.commonutil.DateUtil;
import com.see.truetransact.commonutil.TTException;
import com.see.truetransact.serverside.TTDAO;
import com.see.truetransact.serverutil.ServerConstants;
import com.see.truetransact.servicelocator.ServiceLocator;
import com.see.truetransact.serverexception.TransRollbackException;
import com.see.truetransact.serverexception.ServiceLocatorException;
import com.see.truetransact.serverside.common.idgenerate.IDGenerateDAO;
import com.see.truetransact.serverside.common.log.LogDAO;
import com.see.truetransact.serverside.common.transaction.TransferTrans;
import com.see.truetransact.serverside.transaction.cash.CashTransaction;
import com.see.truetransact.serverside.transaction.common.TransactionFactory;
import com.see.truetransact.transferobject.common.log.LogTO;
import com.see.truetransact.transferobject.common.transaction.TransactionTO;
import com.see.truetransact.serverside.transaction.cash.CashTransactionDAO;
import com.see.truetransact.serverside.common.transaction.TransactionDAO;
import com.see.truetransact.transferobject.transaction.cash.CashTransactionTO;
import com.see.truetransact.transferobject.roomrent.renttrans.RentTransTO;
import com.see.truetransact.transferobject.servicetax.servicetaxdetails.ServiceTaxDetailsTO;
import com.see.truetransact.ui.common.servicetax.ServiceTaxCalculation;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.ArrayList;
import java.util.Date;

/**
 * RentTransDAO DAO.
 *
 */
public class RentTransDAO extends TTDAO {

    private static SqlMap sqlMap = null;
    private RentTransTO objTO;
    private TransactionDAO transactionDAO = null;
    private LogDAO logDAO;
    private LogTO logTO;
    private Date currDt;
    HashMap returnMap;
    HashMap tranMap = new HashMap();
    private String generateSingleTransId = "";
    boolean insuffBal = false;
    ArrayList cashList = new ArrayList();
    boolean rentTransSucc = false;
    /**
     * Creates a new instance of TokenConfigDAO
     */
    public RentTransDAO() throws ServiceLocatorException {
        ServiceLocator locate = ServiceLocator.getInstance();
        sqlMap = (SqlMap) locate.getDAOSqlMap();
    }

    private HashMap getData(HashMap map) throws Exception {
        HashMap returnMap = new HashMap();
        HashMap where = (HashMap) map.get(CommonConstants.MAP_WHERE);
        List list = null;
        if (where.containsKey("RTID")) {
            list = (List) sqlMap.executeQueryForList("RentTrans.getSelectRentTrans", where);
        }
        returnMap.put("RentTransTO", list);
        if (where.containsKey("RTID")) {
            HashMap getRemitTransMap = new HashMap();
            getRemitTransMap.put("TRANS_ID", where.get("RTID"));
            getRemitTransMap.put("TRANS_DT", currDt.clone());
            getRemitTransMap.put("BRANCH_CODE", _branchCode);
            System.out.println("@#%$#@%#$%getRemitTransMap:" + getRemitTransMap);
            list = (List) sqlMap.executeQueryForList("getSelectRemittanceIssueTransactionTODate", getRemitTransMap);
            if (list != null && list.size() > 0) {
                returnMap.put("TRANSACTION_LIST", list);
            }
        }
        System.out.println("returnMap 00========" + returnMap);
        return returnMap;
    }

    private String getRrId() throws Exception {
        System.out.println("inside id gen");
        final IDGenerateDAO dao = new IDGenerateDAO();
        final HashMap where = new HashMap();
        where.put(CommonConstants.MAP_WHERE, "RTID");
        System.out.println("id here "+(String) (dao.executeQuery(where)).get(CommonConstants.DATA));
        return (String) (dao.executeQuery(where)).get(CommonConstants.DATA);
    }

    private void insertData(HashMap map) throws Exception {
        try {
            sqlMap.startTransaction();
            //objTO.setRrId(getRrId());
            objTO.setStatus(CommonConstants.STATUS_CREATED);
            doRentTransTransactions(map);
            if (!insuffBal) {
                System.out.println("inside not insufficient balance");
                logTO.setData(objTO.toString());
                logTO.setPrimaryKey(objTO.getKeyData());
                logTO.setStatus(objTO.getCommand());
                if(rentTransSucc)
                {    
                System.out.println("inside not defaul");
                sqlMap.executeUpdate("insertRentTransTO", objTO);
                }
                sqlMap.executeUpdate("insertRentTransRentDetails", objTO);
                sqlMap.executeUpdate("UpdateRentRegisterAccStatusUpdate", objTO);
                if (map.containsKey("serviceTaxDetailsTO")) {
                ServiceTaxDetailsTO objserTax = (ServiceTaxDetailsTO) map.get("serviceTaxDetailsTO");
                //objserTax.setAcct_Num(objTO.getRmNumber());
                insertServiceTaxDetails(objserTax);
               }
            //    System.out.println("amtBorrowedMaster in DAPO==="+objTO.getAmtBorrowedMaster());
                //     System.out.println("avalbalBorrowedMaster in DAPO==="+objTO.getAvalbalBorrowedMaster());
                // sqlMap.executeUpdate("amtborrowedupdated11", objTO);
                //  sqlMap.executeUpdate("avalbalupdated11", objTO);
                //     logDAO.addToLog(logTO);

            }
            sqlMap.commitTransaction();
        } catch (Exception e) {
            sqlMap.rollbackTransaction();
            e.printStackTrace();
            throw new TransRollbackException(e);
        }
    }
    
    private void insertServiceTaxDetails(ServiceTaxDetailsTO objserviceTaxDetailsTO) {
        try {
            objserviceTaxDetailsTO.setServiceTaxDet_Id(getServiceTaxNo());
            objserviceTaxDetailsTO.setParticulars("Romm Rent");
            sqlMap.executeUpdate("insertServiceTaxDetailsTO", objserviceTaxDetailsTO);
        } catch (Exception ex) {
            ex.printStackTrace();
        }        
    }
    
    private String getServiceTaxNo() throws Exception {
        final IDGenerateDAO dao = new IDGenerateDAO();
        final HashMap where = new HashMap();
        where.put(CommonConstants.MAP_WHERE, "SERVICETAX_DET_ID");
        return (String) (dao.executeQuery(where)).get(CommonConstants.DATA);
    }

    private void doRentTransTransactions(HashMap map) throws Exception, Exception {
        try {
            double swachhCess = 0.0;
            double krishikalyanCess = 0.0;
            double serTaxAmt = 0.0;
            double normalServiceTax = 0.0;
            objTO.setRrId(getRrId());    
            String trans_type = "";
            System.out.println("!@#$@# Borrowings to :" + objTO);
            if (tranMap == null) {
                tranMap = new HashMap();
            }
            if (objTO.getCommand() != null) {
                if (objTO.getCommand().equals(CommonConstants.TOSTATUS_INSERT)) {
                    // Added by nithya for gst
                    HashMap serviceTaxMap = new HashMap();
                    double service_taxAmt = 0;
                    if (map.containsKey("serviceTaxDetails")) {
                        serviceTaxMap = (HashMap) map.get("serviceTaxDetails");
                        if (serviceTaxMap.containsKey("TOT_TAX_AMT") && serviceTaxMap.get("TOT_TAX_AMT") != null) {
                            service_taxAmt = CommonUtil.convertObjToDouble(serviceTaxMap.get("TOT_TAX_AMT"));
                        }
                    }
                    if (serviceTaxMap != null && serviceTaxMap.size() > 0) {                        
                        if (serviceTaxMap.containsKey("TOT_TAX_AMT") && serviceTaxMap.get("TOT_TAX_AMT") != null) {
                            serTaxAmt = CommonUtil.convertObjToDouble(serviceTaxMap.get("TOT_TAX_AMT"));
                        }
                        if (serviceTaxMap.containsKey(ServiceTaxCalculation.SWACHH_CESS) && serviceTaxMap.get(ServiceTaxCalculation.SWACHH_CESS) != null) {
                            swachhCess = CommonUtil.convertObjToDouble(serviceTaxMap.get(ServiceTaxCalculation.SWACHH_CESS));
                        }
                        if (serviceTaxMap.containsKey(ServiceTaxCalculation.KRISHIKALYAN_CESS) && serviceTaxMap.get(ServiceTaxCalculation.KRISHIKALYAN_CESS) != null) {
                            krishikalyanCess = CommonUtil.convertObjToDouble(serviceTaxMap.get(ServiceTaxCalculation.KRISHIKALYAN_CESS));
                        }
                           if (serviceTaxMap.containsKey(ServiceTaxCalculation.SERVICE_TAX) && serviceTaxMap.get(ServiceTaxCalculation.SERVICE_TAX) != null) {
                                normalServiceTax = CommonUtil.convertObjToDouble(serviceTaxMap.get(ServiceTaxCalculation.SERVICE_TAX));
                            }
                       // serTaxAmt -= (swachhCess + krishikalyanCess);
                    }                
                    // End
                    System.out.println("!@#!@#@# !ID GENERATED:" + objTO.getRrId());
                    Double rentAmt = objTO.getRentAmt();//CommonUtil.convertObjToDouble(objTO.getRentAmt()).doubleValue() ;
                    Double penelAmt = objTO.getPenelAmt();
                    Double noticeAmt = objTO.getNoticeAmt();
                    Double legalAmt = objTO.getLegalAmt();
                    Double arbAmt = objTO.getArbAmt();
                    Double courtAmt = objTO.getCourtAmt();
                    Double exeAmt = objTO.getExeAmt();
                    System.out.println("rentAmt=" + rentAmt + " penelAmt=" + penelAmt + " noticeAmt==" + noticeAmt + " legalAmt=" + legalAmt + " arbAmt==" + arbAmt + " courtAmt==" + courtAmt + " exeAmt=" + exeAmt);
                    HashMap txMap;
                    HashMap whereMap = new HashMap();
                    whereMap.put("BUILDING_NO", objTO.getBuildingNo());
                    System.out.println("B NOO============" + objTO.getBuildingNo());
                    HashMap acHeads = (HashMap) sqlMap.executeQueryForObject("RentTrans.getAcHeads", whereMap);

                    if (acHeads == null || acHeads.size() == 0) {
                        throw new TTException("Account heads not set properly...");
                    }
                    TransferTrans objTransferTrans = new TransferTrans();

                    objTransferTrans.setInitiatedBranch(_branchCode);

                    objTransferTrans.setLinkBatchId(objTO.getRrId());
                    txMap = new HashMap();
                    ArrayList transferList = new ArrayList();

                    LinkedHashMap TransactionDetailsMap = (LinkedHashMap) map.get("TransactionTO");
                    //                            System.out.println("TransactionDetailsMap---->"+TransactionDetailsMap);
                    if (TransactionDetailsMap.size() > 0) {
                        if (TransactionDetailsMap.containsKey("NOT_DELETED_TRANS_TOs")) {
                            LinkedHashMap allowedTransDetailsTO = (LinkedHashMap) TransactionDetailsMap.get("NOT_DELETED_TRANS_TOs");
                            if (allowedTransDetailsTO !=null && allowedTransDetailsTO.size()>0) {
                            TransactionTO objTransactionTO = null;
                            System.out.println("@#$@#$#$allowedTransDetailsTO" + allowedTransDetailsTO);
                            for (int J = 1; J <= allowedTransDetailsTO.size(); J++) {
                                objTransactionTO = (TransactionTO) allowedTransDetailsTO.get(String.valueOf(J));
                                double debitAmt = CommonUtil.convertObjToDouble(objTransactionTO.getTransAmt()).doubleValue();
                                trans_type = objTransactionTO.getTransType();
                                System.out.println("product type here 234 " + objTransactionTO.getProductType());
                                if (objTransactionTO.getTransType().equals("TRANSFER")) {
                                    if (objTransactionTO.getProductType().equals(TransactionFactory.OPERATIVE)) {
                                        HashMap inputMap = new HashMap();
                                        inputMap.put("ACCOUNTNO", CommonUtil.convertObjToStr(objTransactionTO.getDebitAcctNo()));
                                        List list = (List) sqlMap.executeQueryForList("getOABalance", inputMap);
                                        if (list != null && list.size() > 0) {
                                            inputMap = (HashMap) list.get(0);
                                            double availableBalance = CommonUtil.convertObjToDouble(inputMap.get("AVAILABLE_BALANCE")).doubleValue();
                                            System.out.println("available balance here" + availableBalance);
                                            double totAmount = objTransactionTO.getTransAmt();
                                            System.out.println("transaction amount here" + totAmount);
                                            if (availableBalance < totAmount) {
                                                System.out.println("inside no balance");
                                                returnMap = new HashMap();
                                                returnMap.put("INSUFFICIENT_BALANCE", "INSUFFICIENT_BALANCE");
                                                insuffBal = true;
                                                return;
                                            }
                                        }
                                    }
                                    //                                            txMap.put(TransferTrans.DR_AC_HD, (String)acHeads.get("SHARE_ACHD"));
                                    txMap.put(TransferTrans.PARTICULARS, "To " + objTO.getRoomNo() + " Trans");
                                    txMap.put(CommonConstants.USER_ID, logTO.getUserId());
                                    txMap.put("DR_INSTRUMENT_2", "DEPOSIT_TRANS");
                                    txMap.put("DR_INST_TYPE", "WITHDRAW_SLIP");
                                    HashMap interBranchCodeMap = new HashMap();
                                    interBranchCodeMap.put("ACT_NUM", CommonUtil.convertObjToStr(objTransactionTO.getDebitAcctNo()));
                                    List interBranchCodeList = sqlMap.executeQueryForList("getSelectInterBranchCode", interBranchCodeMap);
                                    if (interBranchCodeList != null && interBranchCodeList.size() > 0) {
                                        interBranchCodeMap = (HashMap) interBranchCodeList.get(0);
                                        System.out.println("interBranchCodeMap : " + interBranchCodeMap);
                                        txMap.put(TransferTrans.DR_BRANCH, CommonUtil.convertObjToStr(interBranchCodeMap.get("BRANCH_CODE")));
                                    } else {
                                        txMap.put(TransferTrans.DR_BRANCH, _branchCode);
                                    }
                                    txMap.put(TransferTrans.CR_BRANCH, _branchCode);
                                    txMap.put(TransferTrans.CR_PROD_TYPE, TransactionFactory.GL);
                                    txMap.put(TransferTrans.AUTHORIZE_STATUS_2, "ENTERED_AMOUNT");
                                    if (objTransactionTO.getProductType().equals("GL")) {
                                        txMap.put(TransferTrans.DR_AC_HD, CommonUtil.convertObjToStr(objTransactionTO.getDebitAcctNo()));
                                        txMap.put(TransferTrans.DR_BRANCH, _branchCode);
                                    } else {
                                        txMap.put(TransferTrans.DR_ACT_NUM, CommonUtil.convertObjToStr(objTransactionTO.getDebitAcctNo()));
                                        txMap.put(TransferTrans.DR_PROD_ID, CommonUtil.convertObjToStr(objTransactionTO.getProductId()));
                                    }
                                    txMap.put(TransferTrans.DR_PROD_TYPE, CommonUtil.convertObjToStr(objTransactionTO.getProductType()));
                                    if (rentAmt.doubleValue() > 0.0) {
                                        if (objTransactionTO.getProductType().equals("OA")) {
                                            txMap.put("TRANS_MOD_TYPE", "OA");
                                        } else if (objTransactionTO.getProductType().equals("AB")) {
                                            txMap.put("TRANS_MOD_TYPE", "AB");
                                        } else if (objTransactionTO.getProductType().equals("SA")) {
                                            txMap.put("TRANS_MOD_TYPE", "SA");
                                        } else if (objTransactionTO.getProductType().equals("TL")) {
                                            txMap.put("TRANS_MOD_TYPE", "TL");
                                        } else if (objTransactionTO.getProductType().equals("AD")) {
                                            txMap.put("TRANS_MOD_TYPE", "AD");
                                        } else {
                                            txMap.put("TRANS_MOD_TYPE", "GL");
                                        }
                                        transferList.add(objTransferTrans.getDebitTransferTO(txMap, rentAmt.doubleValue()));
                                        txMap.put(TransferTrans.CR_AC_HD, (String) acHeads.get("RENT_AC_HD_ID"));
                                        txMap.put("AUTHORIZEREMARKS", "RENT_AC_HD_ID");
                                        txMap.put("TRANS_MOD_TYPE", "RT");
                                        transferList.add(objTransferTrans.getCreditTransferTO(txMap, rentAmt.doubleValue()));

                                    }
                                    if (penelAmt.doubleValue() > 0.0) {
                                        if (objTransactionTO.getProductType().equals("OA")) {
                                            txMap.put("TRANS_MOD_TYPE", "OA");
                                        } else if (objTransactionTO.getProductType().equals("AB")) {
                                            txMap.put("TRANS_MOD_TYPE", "AB");
                                        } else if (objTransactionTO.getProductType().equals("SA")) {
                                            txMap.put("TRANS_MOD_TYPE", "SA");
                                        } else if (objTransactionTO.getProductType().equals("TL")) {
                                            txMap.put("TRANS_MOD_TYPE", "TL");
                                        } else if (objTransactionTO.getProductType().equals("AD")) {
                                            txMap.put("TRANS_MOD_TYPE", "AD");
                                        } else {
                                            txMap.put("TRANS_MOD_TYPE", "GL");
                                        }
                                        transferList.add(objTransferTrans.getDebitTransferTO(txMap, penelAmt.doubleValue()));
                                        txMap.put(TransferTrans.CR_AC_HD, (String) acHeads.get("PENEL_AC_HD_ID"));
                                        txMap.put("AUTHORIZEREMARKS", "PENEL_AC_HD_ID");
                                        txMap.put("TRANS_MOD_TYPE", "RT");
                                        transferList.add(objTransferTrans.getCreditTransferTO(txMap, penelAmt.doubleValue()));
                                    }
                                    if (noticeAmt.doubleValue() > 0.0) {
                                        if (objTransactionTO.getProductType().equals("OA")) {
                                            txMap.put("TRANS_MOD_TYPE", "OA");
                                        } else if (objTransactionTO.getProductType().equals("AB")) {
                                            txMap.put("TRANS_MOD_TYPE", "AB");
                                        } else if (objTransactionTO.getProductType().equals("SA")) {
                                            txMap.put("TRANS_MOD_TYPE", "SA");
                                        } else if (objTransactionTO.getProductType().equals("TL")) {
                                            txMap.put("TRANS_MOD_TYPE", "TL");
                                        } else if (objTransactionTO.getProductType().equals("AD")) {
                                            txMap.put("TRANS_MOD_TYPE", "AD");
                                        } else {
                                            txMap.put("TRANS_MOD_TYPE", "GL");
                                        }
                                        transferList.add(objTransferTrans.getDebitTransferTO(txMap, noticeAmt.doubleValue()));
                                        txMap.put(TransferTrans.CR_AC_HD, (String) acHeads.get("NOTICE_AC_HD_ID"));
                                        txMap.put("AUTHORIZEREMARKS", "NOTICE_AC_HD_ID");
                                        txMap.put("TRANS_MOD_TYPE", "RT");
                                        transferList.add(objTransferTrans.getCreditTransferTO(txMap, noticeAmt.doubleValue()));
                                    }
                                    if (legalAmt.doubleValue() > 0.0) {
                                        if (objTransactionTO.getProductType().equals("OA")) {
                                            txMap.put("TRANS_MOD_TYPE", "OA");
                                        } else if (objTransactionTO.getProductType().equals("AB")) {
                                            txMap.put("TRANS_MOD_TYPE", "AB");
                                        } else if (objTransactionTO.getProductType().equals("SA")) {
                                            txMap.put("TRANS_MOD_TYPE", "SA");
                                        } else if (objTransactionTO.getProductType().equals("TL")) {
                                            txMap.put("TRANS_MOD_TYPE", "TL");
                                        } else if (objTransactionTO.getProductType().equals("AD")) {
                                            txMap.put("TRANS_MOD_TYPE", "AD");
                                        } else {
                                            txMap.put("TRANS_MOD_TYPE", "GL");
                                        }
                                        transferList.add(objTransferTrans.getDebitTransferTO(txMap, legalAmt.doubleValue()));
                                        txMap.put(TransferTrans.CR_AC_HD, (String) acHeads.get("LEGAL_AC_HD_ID"));
                                        txMap.put("AUTHORIZEREMARKS", "LEGAL_AC_HD_ID");
                                        txMap.put("TRANS_MOD_TYPE", "RT");
                                        transferList.add(objTransferTrans.getCreditTransferTO(txMap, legalAmt.doubleValue()));
                                    }
                                    if (arbAmt.doubleValue() > 0.0) {
                                        if (objTransactionTO.getProductType().equals("OA")) {
                                            txMap.put("TRANS_MOD_TYPE", "OA");
                                        } else if (objTransactionTO.getProductType().equals("AB")) {
                                            txMap.put("TRANS_MOD_TYPE", "AB");
                                        } else if (objTransactionTO.getProductType().equals("SA")) {
                                            txMap.put("TRANS_MOD_TYPE", "SA");
                                        } else if (objTransactionTO.getProductType().equals("TL")) {
                                            txMap.put("TRANS_MOD_TYPE", "TL");
                                        } else if (objTransactionTO.getProductType().equals("AD")) {
                                            txMap.put("TRANS_MOD_TYPE", "AD");
                                        } else {
                                            txMap.put("TRANS_MOD_TYPE", "GL");
                                        }
                                        transferList.add(objTransferTrans.getDebitTransferTO(txMap, arbAmt.doubleValue()));
                                        txMap.put(TransferTrans.CR_AC_HD, (String) acHeads.get("ARB_AC_HD_ID"));
                                        txMap.put("AUTHORIZEREMARKS", "ARB_AC_HD_ID");
                                        txMap.put("TRANS_MOD_TYPE", "RT");
                                        transferList.add(objTransferTrans.getCreditTransferTO(txMap, arbAmt.doubleValue()));
                                    }
                                    if (courtAmt.doubleValue() > 0.0) {
                                        if (objTransactionTO.getProductType().equals("OA")) {
                                            txMap.put("TRANS_MOD_TYPE", "OA");
                                        } else if (objTransactionTO.getProductType().equals("AB")) {
                                            txMap.put("TRANS_MOD_TYPE", "AB");
                                        } else if (objTransactionTO.getProductType().equals("SA")) {
                                            txMap.put("TRANS_MOD_TYPE", "SA");
                                        } else if (objTransactionTO.getProductType().equals("TL")) {
                                            txMap.put("TRANS_MOD_TYPE", "TL");
                                        } else if (objTransactionTO.getProductType().equals("AD")) {
                                            txMap.put("TRANS_MOD_TYPE", "AD");
                                        } else {
                                            txMap.put("TRANS_MOD_TYPE", "GL");
                                        }
                                        transferList.add(objTransferTrans.getDebitTransferTO(txMap, courtAmt.doubleValue()));

                                        txMap.put(TransferTrans.CR_AC_HD, (String) acHeads.get("COURT_AC_HD_ID"));
                                        txMap.put("AUTHORIZEREMARKS", "COURT_AC_HD_ID");
                                        txMap.put("TRANS_MOD_TYPE", "RT");
                                        transferList.add(objTransferTrans.getCreditTransferTO(txMap, courtAmt.doubleValue()));
                                    }
                                    if (exeAmt.doubleValue() > 0.0) {
                                        if (objTransactionTO.getProductType().equals("OA")) {
                                            txMap.put("TRANS_MOD_TYPE", "OA");
                                        } else if (objTransactionTO.getProductType().equals("AB")) {
                                            txMap.put("TRANS_MOD_TYPE", "AB");
                                        } else if (objTransactionTO.getProductType().equals("SA")) {
                                            txMap.put("TRANS_MOD_TYPE", "SA");
                                        } else if (objTransactionTO.getProductType().equals("TL")) {
                                            txMap.put("TRANS_MOD_TYPE", "TL");
                                        } else if (objTransactionTO.getProductType().equals("AD")) {
                                            txMap.put("TRANS_MOD_TYPE", "AD");
                                        } else {
                                            txMap.put("TRANS_MOD_TYPE", "GL");
                                        }
                                        transferList.add(objTransferTrans.getDebitTransferTO(txMap, exeAmt.doubleValue()));
                                        txMap.put(TransferTrans.CR_AC_HD, (String) acHeads.get("EXE_AC_HD_ID"));
                                        txMap.put("AUTHORIZEREMARKS", "EXE_AC_HD_ID");
                                        txMap.put("TRANS_MOD_TYPE", "RT");
                                        transferList.add(objTransferTrans.getCreditTransferTO(txMap, exeAmt.doubleValue()));
                                    }
//                                    TOT_TAX_AMT=19, SWACHH_CESS=9.0, TAX_HEAD_ID=1006001004, SWACHH_HEAD_ID=1006001004, EDUCATION_CESS=0.0, KRISHIKALYAN_HEAD_ID=1006001006, 
//                                    KRISHI_KALYAN_CESS=10.0, HIGHER_EDU_CESS=0.0, SERVICE_TAX=0.0}, SESSION_ID=null, MODULE=Room Rent, BRANCH_CODE=0001, 
                                    if(swachhCess > 0) {
                                        if (objTransactionTO.getProductType().equals("OA")) {
                                            txMap.put("TRANS_MOD_TYPE", "OA");
                                        } else if (objTransactionTO.getProductType().equals("AB")) {
                                            txMap.put("TRANS_MOD_TYPE", "AB");
                                        } else if (objTransactionTO.getProductType().equals("SA")) {
                                            txMap.put("TRANS_MOD_TYPE", "SA");
                                        } else if (objTransactionTO.getProductType().equals("TL")) {
                                            txMap.put("TRANS_MOD_TYPE", "TL");
                                        } else if (objTransactionTO.getProductType().equals("AD")) {
                                            txMap.put("TRANS_MOD_TYPE", "AD");
                                        } else {
                                            txMap.put("TRANS_MOD_TYPE", "GL");
                                        }
                                        transferList.add(objTransferTrans.getDebitTransferTO(txMap, swachhCess));
                                        txMap.put(TransferTrans.CR_AC_HD, (String) serviceTaxMap.get("SWACHH_HEAD_ID"));
                                        txMap.put("AUTHORIZEREMARKS", "SWACHH_HEAD_ID");
                                        txMap.put("TRANS_MOD_TYPE", "RT");
                                        txMap.put(TransferTrans.PARTICULARS,"CGST " + objTO.getRoomNo() + " Trans");
                                        transferList.add(objTransferTrans.getCreditTransferTO(txMap, swachhCess));
                                       // serTaxAmt -= swachhCess;
                                    }
                                    if(krishikalyanCess > 0){
                                        if (objTransactionTO.getProductType().equals("OA")) {
                                            txMap.put("TRANS_MOD_TYPE", "OA");
                                        } else if (objTransactionTO.getProductType().equals("AB")) {
                                            txMap.put("TRANS_MOD_TYPE", "AB");
                                        } else if (objTransactionTO.getProductType().equals("SA")) {
                                            txMap.put("TRANS_MOD_TYPE", "SA");
                                        } else if (objTransactionTO.getProductType().equals("TL")) {
                                            txMap.put("TRANS_MOD_TYPE", "TL");
                                        } else if (objTransactionTO.getProductType().equals("AD")) {
                                            txMap.put("TRANS_MOD_TYPE", "AD");
                                        } else {
                                            txMap.put("TRANS_MOD_TYPE", "GL");
                                        }
                                        transferList.add(objTransferTrans.getDebitTransferTO(txMap, krishikalyanCess));
                                        txMap.put(TransferTrans.CR_AC_HD, (String) serviceTaxMap.get("KRISHIKALYAN_HEAD_ID"));
                                        txMap.put("AUTHORIZEREMARKS", "KRISHIKALYAN_HEAD_ID");
                                        txMap.put("TRANS_MOD_TYPE", "RT");
                                        txMap.put(TransferTrans.PARTICULARS,"SGST " + objTO.getRoomNo() + " Trans");
                                        transferList.add(objTransferTrans.getCreditTransferTO(txMap, krishikalyanCess));
                                       // serTaxAmt -= krishikalyanCess;
                                    }
                                    if(normalServiceTax > 0){
                                         if (objTransactionTO.getProductType().equals("OA")) {
                                            txMap.put("TRANS_MOD_TYPE", "OA");
                                        } else if (objTransactionTO.getProductType().equals("AB")) {
                                            txMap.put("TRANS_MOD_TYPE", "AB");
                                        } else if (objTransactionTO.getProductType().equals("SA")) {
                                            txMap.put("TRANS_MOD_TYPE", "SA");
                                        } else if (objTransactionTO.getProductType().equals("TL")) {
                                            txMap.put("TRANS_MOD_TYPE", "TL");
                                        } else if (objTransactionTO.getProductType().equals("AD")) {
                                            txMap.put("TRANS_MOD_TYPE", "AD");
                                        } else {
                                            txMap.put("TRANS_MOD_TYPE", "GL");
                                        }
                                        transferList.add(objTransferTrans.getDebitTransferTO(txMap, normalServiceTax));
                                        txMap.put(TransferTrans.CR_AC_HD, (String) serviceTaxMap.get("TAX_HEAD_ID"));
                                        txMap.put("AUTHORIZEREMARKS", "TAX_HEAD_ID");
                                        txMap.put("TRANS_MOD_TYPE", "RT");
                                        txMap.put(TransferTrans.PARTICULARS,"SERVICE_TAX " + objTO.getRoomNo() + " Trans");
                                        transferList.add(objTransferTrans.getCreditTransferTO(txMap, normalServiceTax));
                                    }
                                    objTransferTrans.doDebitCredit(transferList, _branchCode, false);
                                } else {
                                    cashList = new ArrayList();
                                    String particular = "";
                                    String fromDate = CommonUtil.convertObjToStr(objTO.getRentPFrm());
                                    String toDate = CommonUtil.convertObjToStr(objTO.getRentPto());
                                    if (rentAmt.doubleValue() > 0.0) {
                                        particular="By " + CommonUtil.convertObjToStr(objTO.getRoomNo()) + " Rent trans, From "+fromDate+"-To "+toDate;
                                        cashTransSave(rentAmt, (String) acHeads.get("RENT_AC_HD_ID"), objTransactionTO, objTransferTrans, map, particular);
                                    }
                                    if (penelAmt.doubleValue() > 0.0) {
                                        particular ="By " + CommonUtil.convertObjToStr(objTO.getRoomNo()) + " Penal";
                                        cashTransSave(penelAmt, (String) acHeads.get("PENEL_AC_HD_ID"), objTransactionTO, objTransferTrans, map, particular);
                                    }
                                    if (noticeAmt.doubleValue() > 0.0) {
                                        particular ="By " + CommonUtil.convertObjToStr(objTO.getRoomNo()) + " Notice";
                                        cashTransSave(noticeAmt, (String) acHeads.get("NOTICE_AC_HD_ID"), objTransactionTO, objTransferTrans, map, particular);
                                    }
                                    if (legalAmt.doubleValue() > 0.0) {
                                        particular ="By " + CommonUtil.convertObjToStr(objTO.getRoomNo()) + " Legal";
                                        cashTransSave(legalAmt, (String) acHeads.get("LEGAL_AC_HD_ID"), objTransactionTO, objTransferTrans, map, particular);
                                    }
                                    if (arbAmt.doubleValue() > 0.0) {
                                        particular ="By " + CommonUtil.convertObjToStr(objTO.getRoomNo()) + " Arbitration";
                                        cashTransSave(arbAmt, (String) acHeads.get("ARB_AC_HD_ID"), objTransactionTO, objTransferTrans, map, particular);
                                    }
                                    if (courtAmt.doubleValue() > 0.0) {
                                        particular ="By " + CommonUtil.convertObjToStr(objTO.getRoomNo()) + " Court";
                                        cashTransSave(courtAmt, (String) acHeads.get("COURT_AC_HD_ID"), objTransactionTO, objTransferTrans, map, particular);
                                    }
                                    if (exeAmt.doubleValue() > 0.0) {
                                        particular ="By " + CommonUtil.convertObjToStr(objTO.getRoomNo()) + " Execution";
                                        cashTransSave(exeAmt, (String) acHeads.get("EXE_AC_HD_ID"), objTransactionTO, objTransferTrans, map, particular);
                                    }
                                    if (swachhCess > 0) {
                                        particular ="CGST "+ objTO.getRoomNo() + " Trans";
                                        cashTransSave(swachhCess, (String) serviceTaxMap.get("SWACHH_HEAD_ID"), objTransactionTO, objTransferTrans, map, particular);
                                        serTaxAmt -= swachhCess;
                                    }
                                    if(krishikalyanCess > 0){
                                        particular ="SGST " + objTO.getRoomNo() + " Trans";
                                        cashTransSave(krishikalyanCess, (String) serviceTaxMap.get("KRISHIKALYAN_HEAD_ID"), objTransactionTO, objTransferTrans, map, particular);
                                        serTaxAmt -= krishikalyanCess;
                                    }
                                    if(normalServiceTax > 0){
                                       particular ="SERVICE_TAX " + objTO.getRoomNo() + " Trans";
                                       cashTransSave(normalServiceTax, (String) serviceTaxMap.get("TAX_HEAD_ID"), objTransactionTO, objTransferTrans, map, particular); 
                                    }
                                }
                                //End cash
                                objTransactionTO.setStatus(CommonConstants.STATUS_CREATED);
                                objTransactionTO.setBatchId(objTO.getRrId());
                                objTransactionTO.setBatchDt(currDt);
                                objTransactionTO.setTransId(objTransferTrans.getLinkBatchId());
                                objTransactionTO.setBranchId(_branchCode);
                                System.out.println("objTransactionTO------------------->" + objTransactionTO);
                                sqlMap.executeUpdate("insertRemittanceIssueTransactionTO", objTransactionTO);
                            }
                           }
                        }
                    }
                    if (trans_type.equals("CASH")) {
                        CashTransactionDAO cashDao;
                        cashDao = new CashTransactionDAO();
                        System.out.println("tranMap ==========" + tranMap);
                        tranMap = cashDao.execute(tranMap, false);
                        cashDao = null;
                        tranMap = null;     
                        cashList = null;
                    }
                    objTransferTrans = null;
                    transferList = null;
                    txMap = null;
                    // Code End
                    getTransDetails(objTO.getRrId());
                }
            }         
    }catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
    }

    private void cashTransSave(Double transAmt1, String acHeads, TransactionTO objTransactionTO, TransferTrans objTransferTrans, HashMap map, String particulars) {
        try {
            // D transAmt;
            CashTransactionTO objCashTO = new CashTransactionTO();
            if (transAmt1.doubleValue() > 0) {
                objCashTO.setTransId("");
                objCashTO.setProdType(TransactionFactory.GL);
                objCashTO.setTransType(CommonConstants.CREDIT);
                objCashTO.setInitTransId(logTO.getUserId());
                objCashTO.setBranchId(_branchCode);
                objCashTO.setStatusBy(logTO.getUserId());
                objCashTO.setStatusDt(currDt);
                objCashTO.setInstrumentNo1("ENTERED_AMOUNT");
                objCashTO.setParticulars(particulars);
                //objCashTO.setParticulars("By " + CommonUtil.convertObjToStr(objTO.getRoomNo()) + " Rent trans, From "+objTO.getRentPFrm()+"-To "+objTO.getRentPto());
                objCashTO.setInitiatedBranch(_branchCode);
                objCashTO.setInitChannType(CommonConstants.CASHIER);
                objCashTO.setCommand(CommonConstants.TOSTATUS_INSERT);
                objCashTO.setAcHdId(acHeads);
                objCashTO.setAuthorizeStatus_2("ENTERED_AMOUNT");
                String amt = String.valueOf(transAmt1);
                double v1 = CommonUtil.convertObjToDouble(amt).doubleValue();
                objCashTO.setInpAmount(transAmt1);//objTransactionTO.getTransAmt());
                objCashTO.setAmount(transAmt1);//objTransactionTO.getTransAmt());
                objCashTO.setLinkBatchId(objTO.getRrId());
                objCashTO.setTransModType("RT");
                objCashTO.setSingleTransId(generateSingleTransId);
                //   System.out.println("objCashTO^^^^^^^"+objCashTO);
                HashMap txMap = new HashMap();
                txMap.put(CommonConstants.BRANCH_ID, _branchCode);
                txMap.put(CommonConstants.PRODUCT_ID, TransactionFactory.GL);
                txMap.put("TRANS_TYPE", CommonConstants.CREDIT);
                txMap.put(TransferTrans.AUTHORIZE_STATUS_2, "ENTERED_AMOUNT");
                if (transAmt1.doubleValue() > 0.0) {
                    txMap.put(CommonConstants.AC_HD_ID, acHeads);
                    txMap.put("AUTHORIZEREMARKS", "PRINCIPAL_GRP_HEAD");
                    txMap.put("AMOUNT", transAmt1);
                    txMap.put("LINK_BATCH_ID", objTransferTrans.getLinkBatchId());
                }
                cashList.add(objCashTO);
                System.out.println("cashList---------------->" + cashList);

                tranMap.put(CommonConstants.BRANCH_ID, CommonUtil.convertObjToStr(map.get("BRANCH_CODE")));
                tranMap.put(CommonConstants.USER_ID, CommonUtil.convertObjToStr(map.get("USER_ID")));
                tranMap.put(CommonConstants.IP_ADDR, CommonUtil.convertObjToStr(map.get("IP_ADDR")));
                tranMap.put(CommonConstants.MODULE, CommonUtil.convertObjToStr(map.get("MODULE")));
                tranMap.put(CommonConstants.SCREEN, CommonUtil.convertObjToStr(map.get("SCREEN")));
                tranMap.put("MODE", CommonConstants.TOSTATUS_INSERT);
                tranMap.put("DAILYDEPOSITTRANSTO", cashList);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void getTransDetails(String batchId) throws Exception {
        HashMap getTransMap = new HashMap();
        getTransMap.put("BATCH_ID", batchId);
        getTransMap.put("TRANS_DT", currDt);
        getTransMap.put(CommonConstants.BRANCH_ID, _branchCode);
        returnMap = new HashMap();
        List transList = (List) sqlMap.executeQueryForList("getTransferDetails", getTransMap);
        if (transList != null && transList.size() > 0) {
            returnMap.put("TRANSFER_TRANS_LIST", transList);
            rentTransSucc = true;
        }
        List cashList = (List) sqlMap.executeQueryForList("getCashDetails", getTransMap);
        if (cashList != null && cashList.size() > 0) {
            returnMap.put("CASH_TRANS_LIST", cashList);
            rentTransSucc = true;
        }
        returnMap.put("RTID", objTO.getRrId());
        getTransMap.clear();
        getTransMap = null;
        transList = null;
        cashList = null;
    }

    private void updateData() throws Exception {
        try {
            sqlMap.startTransaction();
            System.out.println("CommonConstants.STATUS_MODIFIED ======" + CommonConstants.STATUS_MODIFIED + " objTO==" + objTO);
            //  System.out.println("%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%============="+ objTO.getDisbursalNo());
            objTO.setStatus(CommonConstants.STATUS_MODIFIED);
            //logTO.setData(objTO.toString());
            //  logTO.setPrimaryKey(objTO.getKeyData());
            // logTO.setStatus(objTO.getCommand());
            sqlMap.executeUpdate("updateRentTransTO", objTO);
            //  sqlMap.executeUpdate("amtborrowedupdated", objTO);
            // sqlMap.executeUpdate("avalbalupdated", objTO);
            //  logDAO.addToLog(logTO);
            sqlMap.commitTransaction();
        } catch (Exception e) {
            sqlMap.rollbackTransaction();
            e.printStackTrace();
            throw new TransRollbackException(e);
        }
    }

    private void deleteData() throws Exception {
        try {
            sqlMap.startTransaction();
            objTO.setStatus(CommonConstants.STATUS_DELETED);
            System.out.println("CommonConstants.STATUS_DELETED--------" + CommonConstants.STATUS_DELETED);
            //logTO.setData(objTO.toString());
            // logTO.setPrimaryKey(objTO.getKeyData());
            // logTO.setStatus(objTO.getCommand());
            sqlMap.executeUpdate("deleteRentTransTO", objTO);
            System.out.println("CommonConsobjTOobjTO-------" + objTO);
            sqlMap.commitTransaction();
        } catch (Exception e) {
            sqlMap.rollbackTransaction();
            e.printStackTrace();
            throw new TransRollbackException(e);
        }
    }

    public static void main(String str[]) {
        try {
            RentTransDAO dao = new RentTransDAO();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public HashMap execute(HashMap map) throws Exception {
        insuffBal = false;
        _branchCode = (String) map.get(CommonConstants.BRANCH_ID);
        generateSingleTransId = generateLinkID();
        currDt = ServerUtil.getCurrentDate(_branchCode);
        returnMap = null;
        logDAO = new LogDAO();
        logTO = new LogTO();
        logTO.setUserId(CommonUtil.convertObjToStr(map.get(CommonConstants.USER_ID)));
        logTO.setBranchId(CommonUtil.convertObjToStr(map.get(CommonConstants.BRANCH_ID)));
        logTO.setIpAddr(CommonUtil.convertObjToStr(map.get(CommonConstants.IP_ADDR)));
        logTO.setModule(CommonUtil.convertObjToStr(map.get(CommonConstants.MODULE)));
        logTO.setScreen(CommonUtil.convertObjToStr(map.get(CommonConstants.SCREEN)));

        System.out.println("*&$@#$@#$map:" + map);

        if (!map.containsKey(CommonConstants.AUTHORIZEMAP)) {
            objTO = (RentTransTO) map.get("RentTransTO");
            final String command = objTO.getCommand();
            if (command.equals(CommonConstants.TOSTATUS_INSERT)) {
                insertData(map);
                //            returnMap = new HashMap();
                //returnMap.put("RTID", CommonUtil.convertObjToStr(objTO.getRrId()));
            } else if (command.equals(CommonConstants.TOSTATUS_UPDATE)) {
                updateData();
            } else if (command.equals(CommonConstants.TOSTATUS_DELETE)) {
                deleteData();
            } else {
                throw new NoCommandException();
            }
        } else {
            System.out.println("map:" + map);
            HashMap authMap = (HashMap) map.get(CommonConstants.AUTHORIZEMAP);
            authMap.put(CommonConstants.BRANCH_ID, (String) map.get(CommonConstants.BRANCH_ID));
            authMap.put(CommonConstants.USER_ID, (String) map.get(CommonConstants.USER_ID));
            System.out.println("authMap:" + authMap);
            RentTransTO objRentTransTO = new RentTransTO();
            if(map.containsKey("RentTransTO")){
               objRentTransTO = (RentTransTO) map.get("RentTransTO"); 
            }
            if (authMap != null) {
                authorize(authMap,objRentTransTO);
            }
        }

        destroyObjects();
        System.out.println("@#@#@# returnMap:" + returnMap);
        rentTransSucc = false;
        return returnMap;
    }

    private void authorize(HashMap map, RentTransTO objRentTransTO) throws Exception {
        String status = (String) map.get("STATUS");
        String linkBatchId = null;
        String disbursalNo = null;
        HashMap cashAuthMap;
        TransactionTO objTransactionTO = null;
        try {
            sqlMap.startTransaction();
            transactionDAO = new TransactionDAO(CommonConstants.DEBIT);
            System.out.println("map:" + map);
            disbursalNo = CommonUtil.convertObjToStr(map.get("RTID"));
            map.put(CommonConstants.STATUS, status);
            map.put(CommonConstants.USER_ID, "0001");
            map.put("CURR_DATE", currDt);
            System.out.println("status------------>" + status);
            sqlMap.executeUpdate("authorizeRentTrans", map);
            linkBatchId = CommonUtil.convertObjToStr(map.get("RTID"));//Transaction Batch Id
            //Separation of Authorization for Cash and Transfer
            //Call this in all places that need Authorization for Transaction
            cashAuthMap = new HashMap();
            System.out.println("@#$@zvbvxcvzx#$map:" + map);
            cashAuthMap.put(CommonConstants.BRANCH_ID, map.get(CommonConstants.BRANCH_ID));
            cashAuthMap.put(CommonConstants.USER_ID, map.get(CommonConstants.USER_ID));
            cashAuthMap.put("DAILY", "DAILY");
            System.out.println("map:" + map);
            System.out.println("cashAuthMap:" + cashAuthMap);
            System.out.println("#$%#$%#$%xcvlinkBatchId" + linkBatchId);
            TransactionDAO.authorizeCashAndTransfer(linkBatchId, status, cashAuthMap);
            HashMap transMap = new HashMap();
            transMap.put("LINK_BATCH_ID", linkBatchId);
            System.out.println("transMap----------------->" + transMap);
            sqlMap.executeUpdate("updateInstrumentNO1Transfer", transMap);
            sqlMap.executeUpdate("updateInstrumentNO1Cash", transMap);
            transMap = null;
            System.out.println("disbursalNo----------------->" + disbursalNo);
            objTransactionTO = new TransactionTO();
            objTransactionTO.setBatchId(CommonUtil.convertObjToStr(disbursalNo));
            objTransactionTO.setTransId(CommonUtil.convertObjToStr(linkBatchId));
            objTransactionTO.setBranchId(_branchCode);
            System.out.println("objTransactionTO----------------->" + objTransactionTO);
            sqlMap.executeUpdate("deleteRemittanceIssueTransactionTO", objTransactionTO);
            if (map.containsKey("SER_TAX_AUTH")) {
                HashMap serMapAuth = (HashMap) map.get("SER_TAX_AUTH");
                sqlMap.executeUpdate("authorizeServiceTaxDetails", serMapAuth);
            }
            if (!status.equals("REJECTED")) {
                System.out.println("objRentTransTO :: " + objRentTransTO);
                if (objRentTransTO.getArbAmt() > 0) {
                    HashMap chargeUpdateMap = new HashMap();
                    chargeUpdateMap.put("PAID_AMT", objRentTransTO.getArbAmt());
                    chargeUpdateMap.put("CHARGE_TYPE", "ARC_COST");
                    chargeUpdateMap.put("ACT_NUM", objRentTransTO.getRoomNo());
                    sqlMap.executeUpdate("updateArcCostRoomChargeDetails", chargeUpdateMap);
                }
            }
            map = null;
            sqlMap.commitTransaction();
        } catch (Exception e) {
            sqlMap.rollbackTransaction();
            e.printStackTrace();
            throw e;
        }

    }

    public HashMap executeQuery(HashMap obj) throws Exception {
        _branchCode = (String) obj.get(CommonConstants.BRANCH_ID);
        currDt = ServerUtil.getCurrentDate(_branchCode);
        return getData(obj);
    }

    private void destroyObjects() {
        objTO = null;
    }

    private String generateLinkID() throws Exception {
        IDGenerateDAO dao = new IDGenerateDAO();
        final HashMap where = new HashMap();
        where.put(CommonConstants.MAP_WHERE, "GENERATE_LINK_ID");
        where.put("INIT_TRANS_ID", "INIT_TRANS_ID");//for trans_batch_id resetting, branch code
        where.put(CommonConstants.BRANCH_ID, _branchCode);
        String batchID = (String) (dao.executeQuery(where)).get(CommonConstants.DATA);
        dao = null;
        return batchID;
    }
}
