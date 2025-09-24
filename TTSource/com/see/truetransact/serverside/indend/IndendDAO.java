/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition.
 **
 *
 * BorrwingDAO.java
 *
 * Created on Thu Jan 20 17:19:05 IST 2005
 */
package com.see.truetransact.serverside.indend;

import java.util.List;
import java.util.ArrayList;


import com.ibatis.db.sqlmap.SqlMap;
import com.see.truetransact.clientutil.ClientUtil;
import com.see.truetransact.serverutil.ServerUtil;
import java.util.LinkedHashMap;
import com.see.truetransact.commonutil.CommonConstants;
import com.see.truetransact.commonutil.NoCommandException;
import com.see.truetransact.commonutil.CommonUtil;
import com.see.truetransact.commonutil.DateUtil;
import com.see.truetransact.serverside.TTDAO;
import com.see.truetransact.serverutil.ServerConstants;
import com.see.truetransact.servicelocator.ServiceLocator;
import com.see.truetransact.serverexception.TransRollbackException;
import com.see.truetransact.serverexception.ServiceLocatorException;
import com.see.truetransact.transferobject.common.transaction.TransactionTO;
import com.see.truetransact.serverside.transaction.cash.CashTransactionDAO;
import com.see.truetransact.serverside.common.transaction.TransactionDAO;
import com.see.truetransact.commonutil.TTException;
import com.see.truetransact.serverside.common.idgenerate.IDGenerateDAO;
import com.see.truetransact.serverside.common.transaction.TransferTrans;
import com.see.truetransact.serverside.transaction.cash.CashTransaction;
import com.see.truetransact.serverside.transaction.common.TransactionFactory;
//import com.see.truetransact.serverside.common.idgenerate.IDGenerateDAO;
//import com.see.truetransact.serverside.common.log.LogDAO;
//import com.see.truetransact.transferobject.common.log.LogTO;
import com.see.truetransact.transferobject.common.log.LogTO;
import com.see.truetransact.serverside.common.log.LogDAO;
import com.see.truetransact.transferobject.transaction.cash.CashTransactionTO;

import com.see.truetransact.transferobject.indend.IndendTO;
import java.util.HashMap;
import java.util.Date;
import java.util.Iterator;
//import org.jfree.xml.ParseException;

/**
 * TokenConfig DAO.
 *
 */
public class IndendDAO extends TTDAO {

    private static SqlMap sqlMap = null;
    private IndendTO objTO;
    private TransactionDAO transactionDAO = null;
    private LogDAO logDAO;
    private LogTO logTO;
    private Date currDt;
    private Iterator processLstIterator;
    HashMap returnMap = null;
    private String irno = "";
    private int debitCreditFlag1 = 0;
    private int debitCreditFlag2 = 0;
    private int creditFlag1 = 0;
    ArrayList cashList = new ArrayList();
    HashMap tranMap = new HashMap();

    /**
     * Creates a new instance of TokenConfigDAO
     */
    public IndendDAO() throws ServiceLocatorException {
        ServiceLocator locate = ServiceLocator.getInstance();
        sqlMap = (SqlMap) locate.getDAOSqlMap();
    }

    private HashMap getData(HashMap map) throws Exception {
        _branchCode = (String) map.get(CommonConstants.BRANCH_ID);
        currDt = ServerUtil.getCurrentDate(_branchCode);
        returnMap = new HashMap();
        HashMap where = (HashMap) map.get(CommonConstants.MAP_WHERE);
        List list = (List) sqlMap.executeQueryForList("Indend.getSelectIndend", where);
        returnMap.put("IndendTO", list);

        //Tranaction
        if (where.containsKey("IRID")) {
            HashMap getRemitTransMap = new HashMap();
            getRemitTransMap.put("TRANS_ID", where.get("IRID"));
            System.out.println("_branchCode??##11@@>>>>" + _branchCode);
            System.out.println("ServerUtil.getCurrentDate(_branchCode)??>###>>>>" + ServerUtil.getCurrentDate(_branchCode));
            getRemitTransMap.put("TRANS_DT", getProperDateFormat(currDt.clone()));
            getRemitTransMap.put("BRANCH_CODE", _branchCode);
            System.out.println("@#%$#@%#$%getRemitTransMap:" + getRemitTransMap);
            list = (List) sqlMap.executeQueryForList("getSelectRemittanceIssueTransactionTODate", getRemitTransMap);
            //            list = sqlMap.executeQueryForList("getSelectRemittanceIssueTransactionTO", where.get("DISBURSAL_NO"));
            if (list != null && list.size() > 0) {
                returnMap.put("TRANSACTION_LIST", list);
            }
        }
        //


        return returnMap;
    }

    private String getIRNo() throws Exception {
        //  final IDGenerateDAO dao = new IDGenerateDAO();
        final HashMap where = new HashMap();
        where.put(CommonConstants.MAP_WHERE, "IRNO");
        // return "";
        HashMap map = generateID();
        //System.out.println("MAP IN DAOOOOOO=========="+map);
        return (String) map.get(CommonConstants.DATA);
        //  return (String) (dao.executeQuery(where)).get(CommonConstants.DATA);
    }

    public HashMap generateID() {

        HashMap hash = null, result = null;
        try {
            String mapName = "getCurrentID";
            HashMap where = new HashMap();
            where.put("ID_KEY", "IRNO"); //Here u have to pass BORROW_ID or something else
            List list = null;

            //sqlMap.startTransaction();
            sqlMap.executeUpdate("updateIDGenerated", where);  // This update statement just updates curr_value=curr_value+1
            list = (List) sqlMap.executeQueryForList(mapName, where);  // This will get u the updated curr_value, prefix and length
            //sqlMap.commitTransaction();

            if (list.size() > 0) {
                hash = (HashMap) list.get(0);
                String strPrefix = "", strLen = "";

                // Prefix for the ID.
                if (hash.containsKey("PREFIX")) {
                    strPrefix = (String) hash.get("PREFIX");
                    if (strPrefix == null || strPrefix.trim().length() == 0) {
                        strPrefix = "";
                    }
                }

                // Maximum Length for the ID
                int len = 10;
                if (hash.containsKey("ID_LENGTH")) {
                    strLen = String.valueOf(hash.get("ID_LENGTH"));
                    if (strLen == null || strLen.trim().length() == 0) {
                        len = 10;
                    } else {
                        len = Integer.parseInt(strLen.trim());
                    }
                }

                int numFrom = strPrefix.trim().length();

                String newID = String.valueOf(hash.get("CURR_VALUE"));

                // Number Part of the String and incrementing 1 (ex. only 00085 from OGGOT00085)
                result = new HashMap();
                String genID = strPrefix.toUpperCase() + CommonUtil.lpad(newID, len - numFrom, '0');
                result.put(CommonConstants.DATA, genID);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    private void insertData(HashMap map) throws Exception {
        try {
            sqlMap.startTransaction();
            irno = getIRNo();
    //        System.out.println("irno11@@@!!!###>>>>" + irno);
//            objTO.setStrIRNo(getIRNo());
//            objTO.setStatus(CommonConstants.STATUS_CREATED);
      //      System.out.println("objTO.getGenTrans()>>>>>" + objTO.getGenTrans());
            System.out.println("@#%#%#$%#$%$%$%#$"+map.get("INDEND_LIABILITY"));
            if (objTO.getGenTrans() != null && objTO.getGenTrans().equalsIgnoreCase("YES")) {
                if(!(map.get("INDEND_LIABILITY")!=null && !map.get("INDEND_LIABILITY").equals(""))){
                    doIndendTransactions(map);
                }
            }
            //  logTO.setData(objTO.toString());
            //  logTO.setPrimaryKey(objTO.getKeyData());
            //   logTO.setStatus(objTO.getCommand());
            HashMap finalMap = (HashMap) map.get("finalMap");
            processLstIterator = finalMap.keySet().iterator();
            String key1 = "";
           // System.out.println("fmap...." + finalMap.size());
            for (int i = 0; i < finalMap.size(); i++) {
                key1 = (String) processLstIterator.next();
                objTO = (IndendTO) finalMap.get(key1);
             //   System.out.println("irno22@@@!!!###>>>>" + irno);
                objTO.setStrIRNo(irno);
                objTO.setStatus(CommonConstants.STATUS_CREATED);
                objTO.setBranch(_branchCode);
//                objTO.setBranchId(CommonConstants.BRANCH_ID);
                // objTO.setStatusBy("");
               // System.out.println("!@#!@#@# !objTO111@@@>>>" + objTO);
                sqlMap.executeUpdate("insertIndendTO", objTO);
            }
            //  logDAO.addToLog(logTO);
            sqlMap.commitTransaction();
        } catch (Exception e) {
            sqlMap.rollbackTransaction();
            e.printStackTrace();
            throw new TransRollbackException(e);
        }
    }

    public void setCashTransDetails(Double amt, String accHd, String dc, HashMap map, String generateSingleTransId) {
        try {
            System.out.println("in setCashTransDetails>>>>");
            CashTransactionTO objCashTO = new CashTransactionTO();
//            CashTransactionDAO cashDao = new CashTransactionDAO();       
            ArrayList cashList = new ArrayList();
            HashMap tranMap = new HashMap();
            objCashTO.setInitTransId(logTO.getUserId());
            objCashTO.setBranchId(_branchCode);
            objCashTO.setStatusBy(logTO.getUserId());
            objCashTO.setStatusDt(currDt);
//                                        objCashTO.setTokenNo(CommonUtil.convertObjToStr(paramMap.get("TOKEN_NO")));
            objCashTO.setInstrumentNo1("ENTERED_AMOUNT");
            objCashTO.setTransType(dc);
            //added by sreekrishnan for cashier
            List listData = ServerUtil.executeQuery("getCashierAuth", new HashMap());
            if (listData != null && listData.size() > 0) {
                HashMap map1 = (HashMap) listData.get(0);
                if (map1.get("CASHIER_AUTH_ALLOWED") != null && map1.get("CASHIER_AUTH_ALLOWED").toString().equals("Y")) {
                    objCashTO.setAuthorizeStatus_2("");
                } else {
                    objCashTO.setAuthorizeStatus_2("ENTERED_AMOUNT");
                }
            } else {
                objCashTO.setAuthorizeStatus_2("ENTERED_AMOUNT");
            }
            objCashTO.setParticulars("By " + objTO.getStrIRNo() + " Indend");
            objCashTO.setNarration(objTO.getNarration());
            objCashTO.setInitiatedBranch(_branchCode);
            objCashTO.setInitChannType(CommonConstants.CASHIER);
            objCashTO.setCommand(CommonConstants.TOSTATUS_INSERT);
            objCashTO.setAcHdId(accHd);
            objCashTO.setInpAmount(amt);
            objCashTO.setAmount(amt);
            objCashTO.setTransId("");
            objCashTO.setProdType(TransactionFactory.GL);

            objCashTO.setLinkBatchId(objTO.getStrIRNo());
            objCashTO.setTransModType("TR");
            objCashTO.setSingleTransId(generateSingleTransId);
            objCashTO.setScreenName("Indend Transaction");
            cashList.add(/*
                     * setCashTransaction(txMap)
                     */objCashTO);
            tranMap.put(CommonConstants.BRANCH_ID, CommonUtil.convertObjToStr(map.get("BRANCH_CODE")));
            tranMap.put(CommonConstants.USER_ID, CommonUtil.convertObjToStr(map.get("USER_ID")));
            tranMap.put(CommonConstants.IP_ADDR, CommonUtil.convertObjToStr(map.get("IP_ADDR")));
            tranMap.put(CommonConstants.MODULE, CommonUtil.convertObjToStr(map.get("MODULE")));
            tranMap.put(CommonConstants.SCREEN, CommonUtil.convertObjToStr(map.get("SCREEN")));
            tranMap.put("MODE", CommonConstants.TOSTATUS_INSERT);
            tranMap.put("DAILYDEPOSITTRANSTO", cashList);
            CashTransactionDAO cashDao;
            cashDao = new CashTransactionDAO();
            tranMap = cashDao.execute(tranMap, false);
            System.out.println("returnmap in setCashTransDetails>>>>" + tranMap);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void doIndendTransactions(HashMap map) throws Exception, Exception {


        try {
            String generateSingleTransId = generateLinkID();
    //        System.out.println("!@#$@# Borrowings to :" + objTO);
            if (objTO.getCommand() != null) {
                if (objTO.getCommand().equals(CommonConstants.TOSTATUS_INSERT)) {
                    double amtIndend = 0.0;
                    TransferTrans objTransferTrans = new TransferTrans();
                    ArrayList transferList = new ArrayList();
                    cashList = new ArrayList();
                    CashTransactionDAO cashDao;
                    HashMap txMap;
                    TransactionTO objTransactionTO = null;
                    String debitorcredit = CommonUtil.convertObjToStr(map.get("DEBITORCREDIT"));
                  //  System.out.println("debitorcredit??>>>" + debitorcredit);
                    HashMap finalMap = (HashMap) map.get("finalMap");
                    processLstIterator = finalMap.keySet().iterator();
                    String key1 = "";
                    //System.out.println("fmap for transaction...." + finalMap.size());
                    for (int i = 0; i < finalMap.size(); i++) {
                        key1 = (String) processLstIterator.next();
                        objTO = (IndendTO) finalMap.get(key1);
                        objTO.setStrIRNo(irno);
                        objTO.setStatus(CommonConstants.STATUS_CREATED);
//                        System.out.println("!@#!@#@# !irno222@@@>>>" + irno);
//                        System.out.println("!@#!@#@# !objTO222@@@>>>" + objTO);
                        amtIndend = CommonUtil.convertObjToDouble(objTO.getAmount()).doubleValue();
                        //   System.out.println("@#$ amtBorrowed :"+amtBorrowed);
//                        HashMap txMap;
                        HashMap whereMap = new HashMap();
                        whereMap.put("IRNO", objTO.getStrIRNo());
                        whereMap.put("DEPID", objTO.getDepoId());
                        HashMap acHeads = null;
                        HashMap acHeadsMisc = null;
                        HashMap acHeadsVat = null;
                        HashMap acHeadsOthr = null;
                        HashMap acHeadsComm = null;
                        if (objTO.getMiscAmt() != null) {
                            acHeadsMisc = (HashMap) sqlMap.executeQueryForObject("Indend.getAcHeadsMisc", whereMap);
                        }
                        //if (objTO.getVatAmt() != null) {
                        //     acHeadsVat = (HashMap) sqlMap.executeQueryForObject("Indend.getAcHeadsVat", whereMap);
                        /// }
                        if (objTO.getOthrExpAmt() != null) {
                            acHeadsOthr = (HashMap) sqlMap.executeQueryForObject("Indend.getAcHeadsOthrExpnse", whereMap);
                        }
                        if (objTO.getCommRecvdAmt() != null) {
                            acHeadsComm = (HashMap) sqlMap.executeQueryForObject("Indend.getAcHeadsComm", whereMap);
                        }


  //                      System.out.println("IN&%%%%%%%%%%%%%%%%%%%%%%%%%=====" + objTO.getTransType());
                        if (objTO.getTransType() != null && objTO.getTransType().equals("Purchase")) {
                            //  System.out.println("IwhereMapwhereMapwhereMap===="+whereMap );
                            acHeads = (HashMap) sqlMap.executeQueryForObject("Indend.getAcHeadsPurchase", whereMap);
                            acHeadsVat = (HashMap) sqlMap.executeQueryForObject("Indend.getAcHeadsVatPur", whereMap);
                            if (acHeads != null && acHeads.containsKey("PURCHASE_AC_HD_ID") && acHeads.get("PURCHASE_AC_HD_ID") == null) {
                                throw new TTException("Purchase Account head not set properly...");
                            }
                            if (acHeadsVat != null && acHeadsVat.containsKey("PUR_VAT_AC_HD_ID") && acHeadsVat.get("PUR_VAT_AC_HD_ID") == null) {
                                throw new TTException("Purchase Vat Account head not set properly...");
                            }
                        }
                        if (objTO.getTransType() != null && objTO.getTransType().equals("Sales")) {
                            acHeads = (HashMap) sqlMap.executeQueryForObject("Indend.getAcHeadsSale", whereMap);
                            acHeadsVat = (HashMap) sqlMap.executeQueryForObject("Indend.getAcHeadsVatSale", whereMap);
                            if (acHeads != null && acHeads.containsKey("SALES_AC_HD_ID") && acHeads.get("SALES_AC_HD_ID") == null) {
                                throw new TTException("Sales Account head not set properly...");
                            }
                            if (acHeadsVat != null && acHeadsVat.containsKey("SALE_VAT_AC_HD_ID") && acHeadsVat.get("SALE_VAT_AC_HD_ID") == null) {
                                throw new TTException("Sales Vat Account head not set properly...");
                            }
                        }
                        if (objTO.getTransType() != null && objTO.getTransType().equals("Purchase Return")) {
                            acHeads = (HashMap) sqlMap.executeQueryForObject("Indend.getAcHeadsPurchaseReturn", whereMap);
                            acHeadsVat = (HashMap) sqlMap.executeQueryForObject("Indend.getAcHeadsVatPurRtn", whereMap);
                            if (acHeads != null && acHeads.containsKey("PUR_RET_AC_HD_ID") && acHeads.get("PUR_RET_AC_HD_ID") == null) {
                                throw new TTException("Purchase Return Account head not set properly...");
                            }
                            if (acHeadsVat != null && acHeadsVat.containsKey("PUR_RTN_VAT_AC_HD_ID") && acHeadsVat.get("PUR_RTN_VAT_AC_HD_ID") == null) {
                                throw new TTException("Purchase Vat Return Account head not set properly...");
                            }
                        }
                        if (objTO.getTransType() != null && objTO.getTransType().equals("Sales Return")) {
                            acHeads = (HashMap) sqlMap.executeQueryForObject("Indend.getAcHeadsSalesReturn", whereMap);
                            acHeadsVat = (HashMap) sqlMap.executeQueryForObject("Indend.getAcHeadsVatSaleRtn", whereMap);
                            if (acHeads != null && acHeads.containsKey("SALES_RET_AC_HD_ID") && acHeads.get("SALES_RET_AC_HD_ID") == null) {
                                throw new TTException("Sales Return Account head not set properly...");
                            }
                            if (acHeads != null && acHeads.containsKey("SALES_RET_AC_HD_ID") && acHeads.get("SALES_RET_AC_HD_ID") == null) {
                                throw new TTException("Sales Return Vat Account head not set properly...");
                            }
                        }
                        if (objTO.getTransType() != null && objTO.getTransType().equals("Damage")) {
                            acHeads = (HashMap) sqlMap.executeQueryForObject("Indend.getAcHeadsDamage", whereMap);
                            acHeadsVat = (HashMap) sqlMap.executeQueryForObject("Indend.getAcHeadsVatDamage", whereMap);
                            if (acHeads != null && acHeads.containsKey("DAMAGE_AC_HD_ID") && acHeads.get("DAMAGE_AC_HD_ID") == null) {
                                throw new TTException("Damage Account head not set properly...");
                            }
                            if (acHeads != null && acHeads.containsKey("DAM_VAT_AC_HD_ID") && acHeads.get("DAM_VAT_AC_HD_ID") == null) {
                                throw new TTException("Damage Vat Account head not set properly...");
                            }
                        }
                        if (objTO.getTransType() != null && objTO.getTransType().equals("Deficite")) {
                            acHeads = (HashMap) sqlMap.executeQueryForObject("Indend.getAcHeadsDeficiate", whereMap);
                            acHeadsVat = (HashMap) sqlMap.executeQueryForObject("Indend.getAcHeadsVatDeficite", whereMap);
                            if (acHeads != null && acHeads.containsKey("DEFICIATE_HC_HD_ID") && acHeads.get("DEFICIATE_HC_HD_ID") == null) {
                                throw new TTException("Deficiate Account head not set properly...");
                            }
                            if (acHeadsVat != null && acHeadsVat.containsKey("DEF_VAT_AC_HD_ID") && acHeadsVat.get("DEF_VAT_AC_HD_ID") == null) {
                                throw new TTException("Deficiate Vat Account head not set properly...");
                            }
                        }
    //                    System.out.println("acHeads === " + acHeads);
                        if (acHeads == null || acHeads.isEmpty() || acHeads.size() == 0) {
                            throw new TTException("Account heads not set properly...");
                        }
                        if (acHeadsVat == null || acHeadsVat.isEmpty() || acHeadsVat.size() == 0) {
                            throw new TTException("Vat Account heads not set properly...");
                        }
//                        TransferTrans objTransferTrans = new TransferTrans();

                        objTransferTrans.setInitiatedBranch(_branchCode);

                        objTransferTrans.setLinkBatchId(objTO.getStrIRNo());
                        txMap = new HashMap();
//                        ArrayList transferList = new ArrayList();

                        LinkedHashMap TransactionDetailsMap = (LinkedHashMap) map.get("TransactionTO");
      //                  System.out.println("TransactionDetailsMap---68687687687667868767866->" + TransactionDetailsMap);
                        if (TransactionDetailsMap.size() > 0) {
                            if (TransactionDetailsMap.containsKey("NOT_DELETED_TRANS_TOs")) {
                                LinkedHashMap allowedTransDetailsTO = (LinkedHashMap) TransactionDetailsMap.get("NOT_DELETED_TRANS_TOs");
//                                TransactionTO objTransactionTO = null;
        //                        System.out.println("@#$@#$#$allowedTransDetailsTO" + allowedTransDetailsTO);
                                for (int J = 1; J <= allowedTransDetailsTO.size(); J++) {
                                    objTransactionTO = (TransactionTO) allowedTransDetailsTO.get(String.valueOf(J));
                                    //                                   System.out.println("objTransactionTO---->"+objTransactionTO);


                                    double debitAmt = CommonUtil.convertObjToDouble(objTransactionTO.getTransAmt()).doubleValue();
                                    if (objTransactionTO.getTransType().equals("TRANSFER")) {

                                        if (objTO.getTransType() != null && (objTO.getTransType().equals("Purchase") || objTO.getTransType().equals("Sales Return") || objTO.getTransType().equals("Purchase Return")
                                                || objTO.getTransType().equals("Sales") || objTO.getTransType().equals("Damage") || objTO.getTransType().equals("Deficite"))) {

                                            txMap.put(TransferTrans.PARTICULARS, "To " + objTO.getNarration() + " ");
                                            if (objTO.getTransType().equals("Sales") || objTO.getTransType().equals("Sales Return")) {
                                                txMap.put(TransferTrans.PARTICULARS, "To " + objTO.getSalesmanName() + " ");
                                            }
                                            txMap.put(CommonConstants.USER_ID, logTO.getUserId());
                                            txMap.put("DR_INSTRUMENT_2", "DEPOSIT_TRANS");
                                            txMap.put("DR_INST_TYPE", "WITHDRAW_SLIP");
                                            txMap.put(TransferTrans.DR_BRANCH, _branchCode);
                                            txMap.put(TransferTrans.CR_BRANCH, _branchCode);
                                            txMap.put(TransferTrans.CR_PROD_TYPE, TransactionFactory.GL);
                                            String hd = null;
                                            if (objTO.getTransType() != null && objTO.getTransType().equals("Purchase")) {
                                                hd = (String) acHeads.get("PURCHASE_AC_HD_ID");
                                            }
                                            if (objTO.getTransType() != null && objTO.getTransType().equals("Sales Return")) {
                                                hd = (String) acHeads.get("SALES_RET_AC_HD_ID");
                                            }
                                            if (objTransactionTO.getProductType().equals("GL")) {
                                                txMap.put(TransferTrans.DR_AC_HD, CommonUtil.convertObjToStr(CommonUtil.convertObjToStr(objTransactionTO.getDebitAcctNo())));
                                            } else {
                                                txMap.put(TransferTrans.DR_ACT_NUM, CommonUtil.convertObjToStr(CommonUtil.convertObjToStr(objTransactionTO.getDebitAcctNo())));
                                                txMap.put(TransferTrans.DR_PROD_ID, CommonUtil.convertObjToStr(objTransactionTO.getProductId()));
                                            }
                                            txMap.put(TransferTrans.DR_PROD_TYPE, CommonUtil.convertObjToStr(objTransactionTO.getProductType()));
                                            //System.out.println("debitAmt12345**>>>>" + debitAmt);
//                                        if(!objTO.getTransType().equals("Purchase") && !objTO.getTransType().equals("Sales Return")){
                                            if (debitCreditFlag1 == 0 && debitorcredit.equals("DEBIT")) {
                                              //  System.out.println("debitttttt#####################" + objTransactionTO.getProductType());
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
                                                txMap.put("SCREEN_NAME",(String) map.get(CommonConstants.SCREEN));
                                                transferList.add(objTransferTrans.getDebitTransferTO(txMap, debitAmt));
                                                debitCreditFlag1 = 1;
                                            }
//                                        }

                                            if (objTO.getTransType().equals("Purchase")) {
                                                txMap.put("SCREEN_NAME",(String) map.get(CommonConstants.SCREEN));
                                                txMap.put(TransferTrans.NARRATION, objTO.getPurchBillNo() + objTO.getBillDate());
                                                if ((String) acHeads.get("PURCHASE_AC_HD_ID") != null) {
                                                    txMap.put(TransferTrans.DR_AC_HD, (String) acHeads.get("PURCHASE_AC_HD_ID"));
                                                    txMap.put("TRANS_MOD_TYPE", "TR");
                                                    transferList.add(objTransferTrans.getDebitTransferTO(txMap, objTO.getAmount()));
                                                }

                                                if (acHeadsVat.get("PUR_VAT_AC_HD_ID") != null) {
                                                    txMap.put(TransferTrans.DR_AC_HD, CommonUtil.convertObjToStr(acHeadsVat.get("PUR_VAT_AC_HD_ID")));
                                                    txMap.put("TRANS_MOD_TYPE", "TR");
                                                    transferList.add(objTransferTrans.getDebitTransferTO(txMap, objTO.getVatAmt()));
                                                }

                                                if (acHeadsOthr.get("OTHER_EXP_HEAD") != null) {
                                                    txMap.put(TransferTrans.DR_AC_HD, CommonUtil.convertObjToStr(acHeadsOthr.get("OTHER_EXP_HEAD")));
                                                    txMap.put("TRANS_MOD_TYPE", "TR");
                                                    transferList.add(objTransferTrans.getDebitTransferTO(txMap, objTO.getOthrExpAmt()));
                                                }
                                            }

                                            if (objTO.getTransType().equals("Sales Return")) {
                                                 txMap.put("SCREEN_NAME",(String) map.get(CommonConstants.SCREEN));
                                                txMap.put(TransferTrans.NARRATION, objTO.getSalesDate() + objTO.getSalesmanName());
                                                if ((String) acHeads.get("SALES_RET_AC_HD_ID") != null) {
                                                    txMap.put(TransferTrans.DR_AC_HD, (String) acHeads.get("SALES_RET_AC_HD_ID"));
                                                    txMap.put("TRANS_MOD_TYPE", "TR");
                                                    transferList.add(objTransferTrans.getDebitTransferTO(txMap, objTO.getAmount()));
                                                }

                                                if (acHeadsVat.get("SALE_RTN_VAT_AC_HD_ID") != null) {
                                                    txMap.put(TransferTrans.DR_AC_HD, CommonUtil.convertObjToStr(acHeadsVat.get("SALE_RTN_VAT_AC_HD_ID")));
                                                    txMap.put("TRANS_MOD_TYPE", "TR");
                                                    transferList.add(objTransferTrans.getDebitTransferTO(txMap, objTO.getVatAmt()));
                                                }

                                                if (acHeadsOthr.get("OTHER_EXP_HEAD") != null) {
                                                    txMap.put(TransferTrans.DR_AC_HD, CommonUtil.convertObjToStr(acHeadsOthr.get("OTHER_EXP_HEAD")));
                                                    txMap.put("TRANS_MOD_TYPE", "TR");
                                                    transferList.add(objTransferTrans.getDebitTransferTO(txMap, objTO.getOthrExpAmt()));
                                                }
                                            }



                                            if (debitAmt > 0.0) {
                                                if (objTransactionTO.getProductType().equals("GL")) {
                                                    txMap.put(TransferTrans.CR_AC_HD, CommonUtil.convertObjToStr(objTransactionTO.getDebitAcctNo()));
                                                    if (objTO.getTransType() != null && objTO.getTransType().equals("Purchase")) {
                                                        txMap.put("TRANS_MOD_TYPE", "TR");
                                                        txMap.put("AUTHORIZEREMARKS", "PURCHASE_AC_HD_ID");
                                                    }
                                                    if (objTO.getTransType() != null && objTO.getTransType().equals("Sales Return")) {
                                                        txMap.put("AUTHORIZEREMARKS", "SALES_RET_AC_HD_ID");
                                                        txMap.put("TRANS_MOD_TYPE", "TR");
                                                    }
                                                } else {
                                                    txMap.put(TransferTrans.DR_ACT_NUM, CommonUtil.convertObjToStr(objTransactionTO.getDebitAcctNo()));
                                                    if (objTO.getTransType() != null && objTO.getTransType().equals("Purchase")) {
                                                        txMap.put("AUTHORIZEREMARKS", "PURCHASE_AC_HD_ID");
                                                        txMap.put("TRANS_MOD_TYPE", "TR");
                                                    }
                                                    if (objTO.getTransType() != null && objTO.getTransType().equals("Sales Return")) {

                                                        txMap.put("AUTHORIZEREMARKS", "SALES_RET_AC_HD_ID");
                                                        txMap.put("TRANS_MOD_TYPE", "TR");
                                                    }
                                                }
//                                                System.out.println("acHeads.get(PURCHASE_AC_HD_ID) for credit??>>>>"+acHeads.get("PURCHASE_AC_HD_ID"));
//                                                txMap.put(TransferTrans.CR_AC_HD, CommonUtil.convertObjToStr(acHeads.get("PURCHASE_AC_HD_ID")));
//                                            if(objTO.getTransType().equals("Sales Return") || objTO.getTransType().equals("Purchase")){

                                                if (objTransactionTO.getProductType().equals("GL")) {
                                                    txMap.put(TransferTrans.CR_AC_HD, CommonUtil.convertObjToStr(CommonUtil.convertObjToStr(objTransactionTO.getDebitAcctNo())));
                                                } else {
                                                    txMap.put(TransferTrans.CR_ACT_NUM, CommonUtil.convertObjToStr(CommonUtil.convertObjToStr(objTransactionTO.getDebitAcctNo())));
                                                    txMap.put(TransferTrans.CR_PROD_ID, CommonUtil.convertObjToStr(objTransactionTO.getProductId()));
                                                }
                                                txMap.put(TransferTrans.CR_PROD_TYPE, CommonUtil.convertObjToStr(objTransactionTO.getProductType()));
                                             //   System.out.println("debitAmt12345###@@@**>>>>" + debitAmt);

                                                if (creditFlag1 == 0 && debitorcredit.equals("CREDIT")) {
                                                    System.out.println("credittt#####################" + objTransactionTO.getProductType());
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
                                                    txMap.put("SCREEN_NAME",(String) map.get(CommonConstants.SCREEN));
                                                    transferList.add(objTransferTrans.getCreditTransferTO(txMap, debitAmt));
                                                    creditFlag1 = 1;
                                                }
//                                            }

                                                if (objTO.getTransType().equals("Purchase")) {
                                                    if (acHeadsComm.get("COMM_RECVD_HEAD") != null) {
                                                        txMap.put(TransferTrans.CR_AC_HD, CommonUtil.convertObjToStr(acHeadsComm.get("COMM_RECVD_HEAD")));
                                                        txMap.put("TRANS_MOD_TYPE", "TR");
                                                        txMap.put("SCREEN_NAME",(String) map.get(CommonConstants.SCREEN));
                                                        transferList.add(objTransferTrans.getCreditTransferTO(txMap, objTO.getCommRecvdAmt()));
                                                    }
                                                    if (acHeadsMisc.get("MISC_INCOME_HEAD") != null) {
                                                        txMap.put(TransferTrans.NARRATION, objTO.getNarration());
                                                        txMap.put(TransferTrans.CR_AC_HD, CommonUtil.convertObjToStr(acHeadsMisc.get("MISC_INCOME_HEAD")));
                                                        txMap.put("TRANS_MOD_TYPE", "TR");
                                                         txMap.put("SCREEN_NAME",(String) map.get(CommonConstants.SCREEN));
                                                        transferList.add(objTransferTrans.getCreditTransferTO(txMap, objTO.getMiscAmt()));
                                                    }
                                                }

                                                if (objTO.getTransType().equals("Sales Return")) {
                                                     txMap.put("SCREEN_NAME",(String) map.get(CommonConstants.SCREEN));
                                                    if (acHeadsComm.get("COMM_RECVD_HEAD") != null) {
                                                        txMap.put(TransferTrans.CR_AC_HD, CommonUtil.convertObjToStr(acHeadsComm.get("COMM_RECVD_HEAD")));
                                                        txMap.put("TRANS_MOD_TYPE", "TR");
                                                        transferList.add(objTransferTrans.getCreditTransferTO(txMap, objTO.getCommRecvdAmt()));
                                                    }
                                                    if (acHeadsMisc.get("MISC_INCOME_HEAD") != null) {
                                                        txMap.put(TransferTrans.NARRATION, objTO.getNarration());
                                                        txMap.put(TransferTrans.CR_AC_HD, CommonUtil.convertObjToStr(acHeadsMisc.get("MISC_INCOME_HEAD")));
                                                        txMap.put("TRANS_MOD_TYPE", "TR");
                                                        transferList.add(objTransferTrans.getCreditTransferTO(txMap, objTO.getMiscAmt()));
                                                    }
                                                }

                                                if (objTO.getTransType().equals("Sales")) {
                                                     txMap.put("SCREEN_NAME",(String) map.get(CommonConstants.SCREEN));
                                                    if ((String) acHeads.get("SALES_AC_HD_ID") != null) {
                                                        txMap.put(TransferTrans.CR_AC_HD, (String) acHeads.get("SALES_AC_HD_ID"));
                                                        txMap.put("TRANS_MOD_TYPE", "TR");
                                                        transferList.add(objTransferTrans.getCreditTransferTO(txMap, objTO.getAmount()));
                                                    }
                                                    if (acHeadsVat.get("SALE_VAT_AC_HD_ID") != null) {
                                                        txMap.put(TransferTrans.CR_AC_HD, CommonUtil.convertObjToStr(acHeadsVat.get("SALE_VAT_AC_HD_ID")));
                                                        txMap.put("TRANS_MOD_TYPE", "TR");
                                                        transferList.add(objTransferTrans.getCreditTransferTO(txMap, objTO.getVatAmt()));
                                                    }
                                                    if (acHeadsMisc.get("MISC_INCOME_HEAD") != null) {
                                                        txMap.put(TransferTrans.NARRATION, objTO.getNarration());
                                                        txMap.put(TransferTrans.CR_AC_HD, CommonUtil.convertObjToStr(acHeadsMisc.get("MISC_INCOME_HEAD")));
                                                        txMap.put("TRANS_MOD_TYPE", "TR");
                                                        transferList.add(objTransferTrans.getCreditTransferTO(txMap, objTO.getMiscAmt()));
                                                    }
                                                }

//                                                System.out.println("(String) acHeads.get(DAMAGE_AC_HD_ID)@@111111>>>>>>" + (String) acHeads.get("DAMAGE_AC_HD_ID"));
//                                                System.out.println("acHeadsVat.get(VAT_AC_HD_ID)22222>>>>>" + acHeadsVat.get("VAT_AC_HD_ID"));

                                                if (objTO.getTransType().equals("Damage")) {
                                                     txMap.put("SCREEN_NAME",(String) map.get(CommonConstants.SCREEN));
                                                    if ((String) acHeads.get("DAMAGE_AC_HD_ID") != null) {
                                                        System.out.println("(String) acHeads.get(DAMAGE_AC_HD_ID)@@11111>>>>>>" + (String) acHeads.get("DAMAGE_AC_HD_ID"));
                                                        txMap.put(TransferTrans.CR_AC_HD, (String) acHeads.get("DAMAGE_AC_HD_ID"));
                                                        txMap.put("TRANS_MOD_TYPE", "TR");
                                                        transferList.add(objTransferTrans.getCreditTransferTO(txMap, objTO.getAmount()));
                                                    }
                                                   // System.out.println("acHeadsVat.get(VAT_AC_HD_ID)22222>>>>>" + acHeadsVat.get("DAM_VAT_AC_HD_ID"));
                                                    if (acHeadsVat.get("DAM_VAT_AC_HD_ID") != null) {
                                                        txMap.put(TransferTrans.CR_AC_HD, CommonUtil.convertObjToStr(acHeadsVat.get("DAM_VAT_AC_HD_ID")));
                                                        txMap.put("TRANS_MOD_TYPE", "TR");
                                                        transferList.add(objTransferTrans.getCreditTransferTO(txMap, objTO.getVatAmt()));
                                                    }
                                                  //  System.out.println("acHeadsMisc.get(MISC_INCOME_HEAD)###@@@11>>>>>" + acHeadsMisc.get("MISC_INCOME_HEAD"));
                                                    if (acHeadsMisc.get("MISC_INCOME_HEAD") != null) {
                                                        txMap.put(TransferTrans.DR_AC_HD, CommonUtil.convertObjToStr(acHeadsMisc.get("MISC_INCOME_HEAD")));
                                                        txMap.put("TRANS_MOD_TYPE", "TR");
                                                        transferList.add(objTransferTrans.getDebitTransferTO(txMap, objTO.getMiscAmt()));
                                                    }
                                                }

                                             ///   System.out.println("(String) acHeads.get(DEFICIATE_HC_HD_ID)@@!!!>>>>> " + (String) acHeads.get("DEFICIATE_HC_HD_ID"));

                                                if (objTO.getTransType().equals("Deficite")) {
                                                     txMap.put("SCREEN_NAME",(String) map.get(CommonConstants.SCREEN));
                                                    if ((String) acHeads.get("DEFICIATE_HC_HD_ID") != null) {
                                                        txMap.put(TransferTrans.CR_AC_HD, (String) acHeads.get("DEFICIATE_HC_HD_ID"));
                                                        txMap.put("TRANS_MOD_TYPE", "TR");
                                                        transferList.add(objTransferTrans.getCreditTransferTO(txMap, objTO.getAmount()));
                                                    }
                                                    if (acHeadsVat.get("DEF_VAT_AC_HD_ID") != null) {
                                                        txMap.put(TransferTrans.CR_AC_HD, CommonUtil.convertObjToStr(acHeadsVat.get("DEF_VAT_AC_HD_ID")));
                                                        txMap.put("TRANS_MOD_TYPE", "TR");
                                                        transferList.add(objTransferTrans.getCreditTransferTO(txMap, objTO.getVatAmt()));
                                                    }
                                                  //  System.out.println("acHeadsMisc.get(MISC_INCOME_HEAD)###@@@22>>>>>" + acHeadsMisc.get("MISC_INCOME_HEAD"));
                                                    if (acHeadsMisc.get("MISC_INCOME_HEAD") != null) {
                                                        txMap.put(TransferTrans.DR_AC_HD, CommonUtil.convertObjToStr(acHeadsMisc.get("MISC_INCOME_HEAD")));
                                                        txMap.put("TRANS_MOD_TYPE", "TR");
                                                        transferList.add(objTransferTrans.getDebitTransferTO(txMap, objTO.getMiscAmt()));
                                                    }
                                                }

                                                if (objTO.getTransType().equals("Purchase Return")) {
                                                     txMap.put("SCREEN_NAME",(String) map.get(CommonConstants.SCREEN));
                                                    if (acHeadsVat.get("PUR_RTN_VAT_AC_HD_ID") != null) {
                                                        txMap.put(TransferTrans.CR_AC_HD, CommonUtil.convertObjToStr(acHeadsVat.get("PUR_RTN_VAT_AC_HD_ID")));
                                                        txMap.put("TRANS_MOD_TYPE", "TR");
                                                        transferList.add(objTransferTrans.getCreditTransferTO(txMap, objTO.getVatAmt()));
                                                    }
                                                    if ((String) acHeads.get("PUR_RET_AC_HD_ID") != null) {
                                                        txMap.put(TransferTrans.CR_AC_HD, (String) acHeads.get("PUR_RET_AC_HD_ID"));
                                                        txMap.put("TRANS_MOD_TYPE", "TR");
                                                        transferList.add(objTransferTrans.getCreditTransferTO(txMap, objTO.getAmount()));
                                                    }
                                                   // System.out.println("acHeadsMisc.get(MISC_INCOME_HEAD)###@@@33>>>>>" + acHeadsMisc.get("MISC_INCOME_HEAD"));
                                                    if (acHeadsMisc.get("MISC_INCOME_HEAD") != null) {
                                                        txMap.put(TransferTrans.DR_AC_HD, CommonUtil.convertObjToStr(acHeadsMisc.get("MISC_INCOME_HEAD")));
                                                        txMap.put("TRANS_MOD_TYPE", "TR");
                                                        transferList.add(objTransferTrans.getDebitTransferTO(txMap, objTO.getMiscAmt()));
                                                    }

                                                }

                                            }
//                                            objTransferTrans.doDebitCredit(transferList, _branchCode, false);
                                        } else {
                                            //                                            txMap.put(TransferTrans.DR_AC_HD, (String)acHeads.get("SHARE_ACHD"));
                                            txMap.put(TransferTrans.PARTICULARS, "To " + objTO.getNarration() + " ");
                                            txMap.put(CommonConstants.USER_ID, logTO.getUserId());
//                                    txMap.put("DR_INSTRUMENT_2","DEPOSIT_TRANS");
                                            txMap.put(TransferTrans.AUTHORIZE_STATUS_2, "ENTERED_AMOUNT");
                                            txMap.put("DR_INST_TYPE", "WITHDRAW_SLIP");
                                            txMap.put(TransferTrans.DR_BRANCH, _branchCode);
                                            txMap.put(TransferTrans.CR_BRANCH, _branchCode);
                                            txMap.put(TransferTrans.CR_PROD_TYPE, TransactionFactory.GL);

                                            if (objTransactionTO.getProductType().equals("GL")) {
                                                txMap.put(TransferTrans.DR_AC_HD, CommonUtil.convertObjToStr(objTransactionTO.getDebitAcctNo()));

                                            } else {
                                                txMap.put(TransferTrans.DR_ACT_NUM, CommonUtil.convertObjToStr(objTransactionTO.getDebitAcctNo()));
                                                txMap.put(TransferTrans.DR_PROD_ID, CommonUtil.convertObjToStr(objTransactionTO.getProductId()));
                                            }
                                            txMap.put(TransferTrans.DR_PROD_TYPE, CommonUtil.convertObjToStr(objTransactionTO.getProductType()));
                                            if (debitCreditFlag2 == 0 && debitorcredit.equals("DEBIT")) {
                                             //   System.out.println("debitttttt#####################" + objTransactionTO.getProductType());
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
                                                txMap.put("SCREEN_NAME",(String) map.get(CommonConstants.SCREEN));
                                                transferList.add(objTransferTrans.getDebitTransferTO(txMap, debitAmt));
                                                debitCreditFlag2 = 1;
                                            }
                                            //                                            transferList.add(objTransferTrans.getCreditTransferTO(txMap, shareAmt));
                                            //                                            objTransferTrans.doDebitCredit(transferList, _branchCode, false);
                                          //  System.out.println("CREDIT AC_HD IN vamtIndend================" + amtIndend + "acHeads=" + acHeads);
                                            if (amtIndend > 0.0) {
                                                if (objTO.getTransType() != null && objTO.getTransType().equals("Purchase")) {
                                                    txMap.put(TransferTrans.DR_AC_HD/*
                                                             * TransferTrans.CR_AC_HD
                                                             */, (String) acHeads.get("PURCHASE_AC_HD_ID"));
                                                    txMap.put("AUTHORIZEREMARKS", "PURCHASE_AC_HD_ID");
                                                    txMap.put("TRANS_MOD_TYPE", "TR");
                                                }
                                                if (objTO.getTransType() != null && objTO.getTransType().equals("Sales")) {
                                                    //  acHeads = (HashMap)sqlMap.executeQueryForObject("Indend.getAcHeadsSale", whereMap);
                                                    txMap.put(TransferTrans.CR_AC_HD, (String) acHeads.get("SALES_AC_HD_ID"));
                                                    txMap.put("AUTHORIZEREMARKS", "SALES_AC_HD_ID");
                                                    txMap.put("TRANS_MOD_TYPE", "TR");
                                                }
                                                if (objTO.getTransType() != null && objTO.getTransType().equals("Purchase Return")) {
                                                    txMap.put(TransferTrans.CR_AC_HD, (String) acHeads.get("PUR_RET_AC_HD_ID"));
                                                    txMap.put("AUTHORIZEREMARKS", "PUR_RET_AC_HD_ID");
                                                    txMap.put("TRANS_MOD_TYPE", "TR");
                                                }
                                                if (objTO.getTransType() != null && objTO.getTransType().equals("Sales Return")) {
                                                    txMap.put(TransferTrans.DR_AC_HD/*
                                                             * TransferTrans.CR_AC_HD
                                                             */, (String) acHeads.get("SALES_RET_AC_HD_ID"));
                                                    txMap.put("AUTHORIZEREMARKS", "SALES_RET_AC_HD_ID");
                                                    txMap.put("TRANS_MOD_TYPE", "TR");
                                                }
                                                if (objTO.getTransType() != null && objTO.getTransType().equals("Damage")) {
                                                    txMap.put(TransferTrans.CR_AC_HD, (String) acHeads.get("DAMAGE_AC_HD_ID"));
                                                    txMap.put("AUTHORIZEREMARKS", "DAMAGE_AC_HD_ID");
                                                    txMap.put("TRANS_MOD_TYPE", "TR");
                                                }
                                                if (objTO.getTransType() != null && objTO.getTransType().equals("Deficite")) {
                                                    txMap.put(TransferTrans.CR_AC_HD, (String) acHeads.get("DEFICIATE_HC_HD_ID"));
                                                    txMap.put("AUTHORIZEREMARKS", "DEFICIATE_HC_HD_ID");
                                                    txMap.put("TRANS_MOD_TYPE", "TR");
                                                }
                                                //                                                transferList.add(objTransferTrans.getDebitTransferTO(txMap, shareAmt));
                                                txMap.put("SCREEN_NAME",(String) map.get(CommonConstants.SCREEN));
                                                transferList.add(objTransferTrans.getCreditTransferTO(txMap, amtIndend));
                                                //                                                objTransferTrans.doDebitCredit(transferList, _branchCode, false);
                                            }
//                                            objTransferTrans.doDebitCredit(transferList, _branchCode, false);
                                        }//new 
                                    } else {

                                        double transAmt;
                                        //  TransactionTO transTO = new TransactionTO();
                                        CashTransactionTO objCashTO = new CashTransactionTO();
                                        // ArrayList cashList = new ArrayList();
                                        if (CommonUtil.convertObjToDouble(objTransactionTO.getTransAmt()).doubleValue() > 0) {
                                            String grpHead = "";
                                            if (objTO.getTransType() != null && objTO.getTransType().equals("Purchase")) {
                                                grpHead = (String) acHeads.get("PURCHASE_AC_HD_ID");
                                            }
                                            if (objTO.getTransType() != null && objTO.getTransType().equals("Sales")) {
                                                grpHead = (String) acHeads.get("SALES_AC_HD_ID");
                                            }
                                            if (objTO.getTransType() != null && objTO.getTransType().equals("Purchase Return")) {
                                                grpHead = (String) acHeads.get("PUR_RET_AC_HD_ID");
                                            }
                                            if (objTO.getTransType() != null && objTO.getTransType().equals("Sales Return")) {
                                                grpHead = (String) acHeads.get("SALES_RET_AC_HD_ID");
                                            }
                                            if (objTO.getTransType() != null && objTO.getTransType().equals("Damage")) {
                                                grpHead = (String) acHeads.get("DAMAGE_AC_HD_ID");
                                            }
                                            if (objTO.getTransType() != null && objTO.getTransType().equals("Deficite")) {
                                                grpHead = (String) acHeads.get("DEFICIATE_HC_HD_ID");
                                            }
                                            objCashTO.setTransId("");
                                            objCashTO.setProdType(TransactionFactory.GL);
                                            if (objTO.getTransType() != null && (objTO.getTransType().equals("Purchase") || objTO.getTransType().equals("Sales Return"))) {
                                                objCashTO.setTransType(CommonConstants.DEBIT);
                                            } else {
                                                objCashTO.setTransType(CommonConstants.CREDIT);
                                            }
                                            objCashTO.setInitTransId(logTO.getUserId());
                                            objCashTO.setBranchId(_branchCode);
                                            objCashTO.setStatusBy(logTO.getUserId());
                                            objCashTO.setStatusDt(currDt);
//                                        objCashTO.setTokenNo(CommonUtil.convertObjToStr(paramMap.get("TOKEN_NO")));
                                            objCashTO.setInstrumentNo1("ENTERED_AMOUNT");
                                            objCashTO.setAuthorizeStatus_2("ENTERED_AMOUNT");
                                            if (objTO.getTransType().equals("Sales") || objTO.getTransType().equals("Sales Return")) {
                                                objCashTO.setParticulars("By " + objTO.getSalesmanName() + " ");
                                            } else {
                                                objCashTO.setParticulars("By " + objTO.getNarration() + " ");
                                            }

                                            objCashTO.setInitiatedBranch(_branchCode);
                                            objCashTO.setInitChannType(CommonConstants.CASHIER);
                                            objCashTO.setCommand(CommonConstants.TOSTATUS_INSERT);
                                            objCashTO.setAcHdId(grpHead);
                                            objCashTO.setInpAmount(objTransactionTO.getTransAmt());
                                            objCashTO.setAmount(objTransactionTO.getTransAmt());
                                            objCashTO.setLinkBatchId(objTO.getStrIRNo());
                                            objCashTO.setTransModType("TR");
                                            objCashTO.setSingleTransId(generateSingleTransId);

                                            //System.out.println("line no 465^^^^^^^");
                                            txMap = new HashMap();
                                            txMap.put(CommonConstants.BRANCH_ID, _branchCode);
                                            txMap.put(CommonConstants.PRODUCT_ID, TransactionFactory.GL);
                                            //     txMap.put(CommonConstants.USER_ID, objDrfTransactionTO.getStatusBy());
                                            if (objTO.getTransType() != null && (objTO.getTransType().equals("Purchase") || objTO.getTransType().equals("Sales Return"))) {
                                                txMap.put("TRANS_TYPE", CommonConstants.DEBIT);
                                            } else {
                                                txMap.put("TRANS_TYPE", CommonConstants.CREDIT);
                                            }
                                            //if(drfAmt>0.0){
                                            //System.out.println("debitAmt IOOOOOOOOOOOOOOOOO^^^^^^=" + debitAmt + " acHeads===" + acHeads);
                                            if (debitAmt > 0.0) {
                                                //txMap.put(CommonConstants.AC_HD_ID, (String)acHeads.get("LIABILITY_HEAD"));
                                                // txMap.put("AUTHORIZEREMARKS","LIABILITY_HEAD");
                                                if (objTO.getTransType() != null && objTO.getTransType().equals("Purchase")) {

                                                    txMap.put(CommonConstants.AC_HD_ID, grpHead);
                                                    txMap.put("AUTHORIZEREMARKS", "PURCHASE_AC_HD_ID");

                                                }
                                                if (objTO.getTransType() != null && objTO.getTransType().equals("Sales")) {
                                                    //  acHeads = (HashMap)sqlMap.executeQueryForObject("Indend.getAcHeadsSale", whereMap);
                                                    txMap.put(CommonConstants.AC_HD_ID, grpHead);
                                                    txMap.put("AUTHORIZEREMARKS", "SALES_AC_HD_ID");
                                                }
                                                if (objTO.getTransType() != null && objTO.getTransType().equals("Purchase Return")) {
                                                    txMap.put(CommonConstants.AC_HD_ID, grpHead);
                                                    txMap.put("AUTHORIZEREMARKS", "PUR_RET_AC_HD_ID");
                                                }
                                                if (objTO.getTransType() != null && objTO.getTransType().equals("Sales Return")) {
                                                    txMap.put(CommonConstants.AC_HD_ID, grpHead);
                                                    txMap.put("AUTHORIZEREMARKS", "SALES_RET_AC_HD_ID");
                                                }
                                                if (objTO.getTransType() != null && objTO.getTransType().equals("Damage")) {
                                                    txMap.put(CommonConstants.AC_HD_ID, grpHead);
                                                    txMap.put("AUTHORIZEREMARKS", "DAMAGE_AC_HD_ID");
                                                }
                                                if (objTO.getTransType() != null && objTO.getTransType().equals("Deficiate")) {
                                                    txMap.put(CommonConstants.AC_HD_ID, grpHead);
                                                    txMap.put("AUTHORIZEREMARKS", "DEFICIATE_HC_HD_ID");
                                                }
                                                txMap.put("AMOUNT", new Double(debitAmt));
                                                // txMap.put("LINK_BATCH_ID", objTransferTrans.getLinkBatchId());
                                                txMap.put("LINK_BATCH_ID", objTO.getStrIRNo());
                                            }
                                            objCashTO.setScreenName((String) map.get(CommonConstants.SCREEN));
                                            cashList.add(/*
                                                     * setCashTransaction(txMap)
                                                     */objCashTO);
//                                            System.out.println("cashList---------------->" + cashList);
//                                            System.out.println("map##??###>>>" + map);
//                                            HashMap tranMap = new HashMap();
//                                            cashList = new ArrayList();
                                            tranMap = new HashMap();
                                            tranMap.put(CommonConstants.BRANCH_ID, CommonUtil.convertObjToStr(map.get("BRANCH_CODE")));
                                            tranMap.put(CommonConstants.USER_ID, CommonUtil.convertObjToStr(map.get("USER_ID")));
                                            tranMap.put(CommonConstants.IP_ADDR, CommonUtil.convertObjToStr(map.get("IP_ADDR")));
                                            tranMap.put(CommonConstants.MODULE, CommonUtil.convertObjToStr(map.get("MODULE")));
                                            tranMap.put(CommonConstants.SCREEN, CommonUtil.convertObjToStr(map.get("SCREEN")));
                                            tranMap.put("MODE", CommonConstants.TOSTATUS_INSERT);
                                            tranMap.put("DAILYDEPOSITTRANSTO", cashList);
//                                            CashTransactionDAO cashDao;
//                                            cashDao = new CashTransactionDAO();
                                          //  System.out.println("tranMap BEFOREE EXECUTE---@@@------------->" + tranMap);
//                                            HashMap tempTranMap = new HashMap();
                                            //  if (!objTO.getTransType().equals("Purchase") && !objTO.getTransType().equals("Sales") && !objTO.getTransType().equals("Purchase Return")) {
                                            // tempTranMap = cashDao.execute(tranMap, false);
                                            //  }
//                                            System.out.println("tranMap AFTER EXECUTE---------------->" + tranMap);


                                            if (objTO.getTransType().equals("Purchase")) {
//                                                System.out.println("PURCHASECASH1111111111acHeadsVat>>>" + acHeadsVat.get("VAT_AC_HD_ID"));
//                                                System.out.println("CASH1111111111objTO.getVatAmt()>>>" + objTO.getVatAmt());
//                                                System.out.println("CASH1111111111acHeadsOthr>>>" + acHeadsOthr.get("OTHER_EXP_HEAD"));
//                                                System.out.println("CASH1111111111objTO.getOthrExpAmt()>>>" + objTO.getOthrExpAmt());
//                                                System.out.println("CASH1111111111acHeads.get(\"PURCHASE_AC_HD_ID\")>>>" + acHeads.get("PURCHASE_AC_HD_ID"));
//                                                System.out.println("CASH1111111111objTO.getAmount()>>>" + objTO.getAmount());
//                                                System.out.println("CASH1111111111acHeadsMisc>>>" + acHeadsMisc.get("MISC_INCOME_HEAD"));
//                                                System.out.println("CASH1111111111objTO.getMiscAmt()>>>" + objTO.getMiscAmt());
//                                                System.out.println("CASH1111111111acHeadsComm>>>" + acHeadsComm.get("COMM_RECVD_HEAD"));
//                                                System.out.println("CASH1111111111objTO.getCommRecvdAmt()>>>" + objTO.getCommRecvdAmt());
                                                if (objTO.getVatAmt() > 0) {
                                                    if (acHeadsVat.get("PUR_VAT_AC_HD_ID") != null) {
                                                        setCashTransDetails(objTO.getVatAmt(), acHeadsVat.get("PUR_VAT_AC_HD_ID").toString(), CommonConstants.DEBIT, map, generateSingleTransId);
                                                        objCashTO.setAcHdId(acHeadsVat.get("PUR_VAT_AC_HD_ID").toString());
                                                        objCashTO.setAmount(objTO.getVatAmt());
                                                        objCashTO.setTransType(CommonConstants.DEBIT);
                                                        objCashTO.setScreenName((String) map.get(CommonConstants.SCREEN));
                                                        cashList.add(/*
                                                                 * setCashTransaction(txMap)
                                                                 */objCashTO);
                                                        tranMap.put("DAILYDEPOSITTRANSTO", cashList);
//                                                     tempTranMap = cashDao.execute(tranMap, false);
                                                    }
                                                }
                                                if (objTO.getOthrExpAmt() > 0) {
                                                    if (acHeadsOthr.get("OTHER_EXP_HEAD") != null) {
                                                        setCashTransDetails(objTO.getOthrExpAmt(), acHeadsOthr.get("OTHER_EXP_HEAD").toString(), CommonConstants.DEBIT, map, generateSingleTransId);
                                                        objCashTO.setAcHdId(acHeadsOthr.get("OTHER_EXP_HEAD").toString());
                                                        objCashTO.setAmount(objTO.getOthrExpAmt());
                                                        objCashTO.setTransType(CommonConstants.DEBIT);
                                                        objCashTO.setScreenName((String) map.get(CommonConstants.SCREEN));
                                                        cashList.add(/*
                                                                 * setCashTransaction(txMap)
                                                                 */objCashTO);
                                                        tranMap.put("DAILYDEPOSITTRANSTO", cashList);
//                                                tempTranMap = cashDao.execute(tranMap, false);
                                                    }
                                                }
                                               // System.out.println("acHeads.get(PURCHASE_AC_HD_ID)>>>" + acHeads.get("PURCHASE_AC_HD_ID").toString());
                                                if (objTO.getAmount() > 0) {
                                                    if (acHeads.get("PURCHASE_AC_HD_ID") != null) {
                                                        System.out.println("objTO.getAmount()>>>>" + objTO.getAmount());
                                                        setCashTransDetails(objTO.getAmount(), acHeads.get("PURCHASE_AC_HD_ID").toString(), CommonConstants.DEBIT, map, generateSingleTransId);
                                                        objCashTO.setAcHdId((String) acHeads.get("PURCHASE_AC_HD_ID"));
                                                        objCashTO.setAmount(objTO.getAmount());
                                                        objCashTO.setTransType(CommonConstants.DEBIT);
                                                        objCashTO.setScreenName((String) map.get(CommonConstants.SCREEN));
                                                        cashList.add(/*
                                                                 * setCashTransaction(txMap)
                                                                 */objCashTO);
                                                        tranMap.put("DAILYDEPOSITTRANSTO", cashList);
//                                                tempTranMap = cashDao.execute(tranMap, false);
                                                    }
                                                }
                                                if (objTO.getMiscAmt() > 0) {
                                                    if (acHeadsMisc.get("MISC_INCOME_HEAD") != null) {
                                                        setCashTransDetails(objTO.getMiscAmt(), acHeadsMisc.get("MISC_INCOME_HEAD").toString(), CommonConstants.CREDIT, map, generateSingleTransId);
                                                        objCashTO.setAcHdId(acHeadsMisc.get("MISC_INCOME_HEAD").toString());
                                                        objCashTO.setAmount(objTO.getMiscAmt());
                                                        objCashTO.setTransType(CommonConstants.CREDIT);
                                                        objCashTO.setScreenName((String) map.get(CommonConstants.SCREEN));
                                                        cashList.add(/*
                                                                 * setCashTransaction(txMap)
                                                                 */objCashTO);
                                                        tranMap.put("DAILYDEPOSITTRANSTO", cashList);
//                                                tempTranMap = cashDao.execute(tranMap, false);
                                                    }
                                                }
                                                if (objTO.getCommRecvdAmt() > 0) {
                                                    if (acHeadsComm.get("COMM_RECVD_HEAD") != null) {
                                                        setCashTransDetails(objTO.getCommRecvdAmt(), acHeadsComm.get("COMM_RECVD_HEAD").toString(), CommonConstants.CREDIT, map, generateSingleTransId);
                                                        objCashTO.setAcHdId(acHeadsComm.get("COMM_RECVD_HEAD").toString());
                                                        objCashTO.setAmount(objTO.getCommRecvdAmt());
                                                        objCashTO.setTransType(CommonConstants.CREDIT);
                                                        objCashTO.setScreenName((String) map.get(CommonConstants.SCREEN));
                                                        cashList.add(/*
                                                                 * setCashTransaction(txMap)
                                                                 */objCashTO);
                                                        tranMap.put("DAILYDEPOSITTRANSTO", cashList);
//                                                tempTranMap = cashDao.execute(tranMap, false);
                                                    }
                                                }
                                            }
//SALES_AC_HD_ID,PUR_RET_AC_HD_ID

                                            if (objTO.getTransType().equals("Sales Return")) {
//                                                System.out.println("PURCHASECASH1111111111acHeadsVat>>>" + acHeadsVat.get("SALE_RTN_VAT_AC_HD_ID"));
//                                                System.out.println("CASH1111111111objTO.getVatAmt()>>>" + objTO.getVatAmt());
//                                                System.out.println("CASH1111111111acHeadsOthr>>>" + acHeadsOthr.get("OTHER_EXP_HEAD"));
//                                                System.out.println("CASH1111111111objTO.getOthrExpAmt()>>>" + objTO.getOthrExpAmt());
//                                                System.out.println("CASH1111111111acHeads.get(\"PURCHASE_AC_HD_ID\")>>>" + acHeads.get("PURCHASE_AC_HD_ID"));
//                                                System.out.println("CASH1111111111objTO.getAmount()>>>" + objTO.getAmount());
//                                                System.out.println("CASH1111111111acHeadsMisc>>>" + acHeadsMisc.get("MISC_INCOME_HEAD"));
//                                                System.out.println("CASH1111111111objTO.getMiscAmt()>>>" + objTO.getMiscAmt());
//                                                System.out.println("CASH1111111111acHeadsComm>>>" + acHeadsComm.get("COMM_RECVD_HEAD"));
//                                                System.out.println("CASH1111111111objTO.getCommRecvdAmt()>>>" + objTO.getCommRecvdAmt());
                                                if (objTO.getVatAmt() > 0) {
                                                    if (acHeadsVat.get("SALE_RTN_VAT_AC_HD_ID") != null) {
                                                        setCashTransDetails(objTO.getVatAmt(), acHeadsVat.get("SALE_RTN_VAT_AC_HD_ID").toString(), CommonConstants.DEBIT, map, generateSingleTransId);
                                                        objCashTO.setAcHdId(acHeadsVat.get("SALE_RTN_VAT_AC_HD_ID").toString());
                                                        objCashTO.setAmount(objTO.getVatAmt());
                                                        objCashTO.setTransType(CommonConstants.DEBIT);
                                                        objCashTO.setScreenName((String) map.get(CommonConstants.SCREEN));
                                                        cashList.add(/*
                                                                 * setCashTransaction(txMap)
                                                                 */objCashTO);
                                                        tranMap.put("DAILYDEPOSITTRANSTO", cashList);
//                                                     tempTranMap = cashDao.execute(tranMap, false);
                                                    }
                                                }
                                                if (objTO.getOthrExpAmt() > 0) {
                                                    if (acHeadsOthr.get("OTHER_EXP_HEAD") != null) {
                                                        setCashTransDetails(objTO.getOthrExpAmt(), acHeadsOthr.get("OTHER_EXP_HEAD").toString(), CommonConstants.DEBIT, map, generateSingleTransId);
                                                        objCashTO.setAcHdId(acHeadsOthr.get("OTHER_EXP_HEAD").toString());
                                                        objCashTO.setAmount(objTO.getOthrExpAmt());
                                                        objCashTO.setTransType(CommonConstants.DEBIT);
                                                        objCashTO.setScreenName((String) map.get(CommonConstants.SCREEN));
                                                        cashList.add(/*
                                                                 * setCashTransaction(txMap)
                                                                 */objCashTO);
                                                        tranMap.put("DAILYDEPOSITTRANSTO", cashList);
//                                                tempTranMap = cashDao.execute(tranMap, false);
                                                    }
                                                }
                                                //System.out.println("acHeads.get(SALES_RET_AC_HD_ID)>>>" + acHeads.get("SALES_RET_AC_HD_ID").toString());
                                                if (objTO.getAmount() > 0) {
                                                    if (acHeads.get("SALES_RET_AC_HD_ID") != null) {
                                                    //    System.out.println("objTO.getAmount()>>>>" + objTO.getAmount());
                                                        setCashTransDetails(objTO.getAmount(), acHeads.get("SALES_RET_AC_HD_ID").toString(), CommonConstants.DEBIT, map, generateSingleTransId);
                                                        objCashTO.setAcHdId((String) acHeads.get("PURCHASE_AC_HD_ID"));
                                                        objCashTO.setAmount(objTO.getAmount());
                                                        objCashTO.setTransType(CommonConstants.DEBIT);
                                                        objCashTO.setScreenName((String) map.get(CommonConstants.SCREEN));
                                                        cashList.add(/*
                                                                 * setCashTransaction(txMap)
                                                                 */objCashTO);
                                                        tranMap.put("DAILYDEPOSITTRANSTO", cashList);
//                                                tempTranMap = cashDao.execute(tranMap, false);
                                                    }
                                                }
                                                if (objTO.getMiscAmt() > 0) {
                                                    if (acHeadsMisc.get("MISC_INCOME_HEAD") != null) {
                                                        setCashTransDetails(objTO.getMiscAmt(), acHeadsMisc.get("MISC_INCOME_HEAD").toString(), CommonConstants.CREDIT, map, generateSingleTransId);
                                                        objCashTO.setAcHdId(acHeadsMisc.get("MISC_INCOME_HEAD").toString());
                                                        objCashTO.setAmount(objTO.getMiscAmt());
                                                        objCashTO.setTransType(CommonConstants.CREDIT);
                                                        objCashTO.setScreenName((String) map.get(CommonConstants.SCREEN));
                                                        cashList.add(/*
                                                                 * setCashTransaction(txMap)
                                                                 */objCashTO);
                                                        tranMap.put("DAILYDEPOSITTRANSTO", cashList);
//                                                tempTranMap = cashDao.execute(tranMap, false);
                                                    }
                                                }
                                                if (objTO.getCommRecvdAmt() > 0) {
                                                    if (acHeadsComm.get("COMM_RECVD_HEAD") != null) {
                                                        setCashTransDetails(objTO.getCommRecvdAmt(), acHeadsComm.get("COMM_RECVD_HEAD").toString(), CommonConstants.CREDIT, map, generateSingleTransId);
                                                        objCashTO.setAcHdId(acHeadsComm.get("COMM_RECVD_HEAD").toString());
                                                        objCashTO.setAmount(objTO.getCommRecvdAmt());
                                                        objCashTO.setTransType(CommonConstants.CREDIT);
                                                        objCashTO.setScreenName((String) map.get(CommonConstants.SCREEN));
                                                        cashList.add(/*
                                                                 * setCashTransaction(txMap)
                                                                 */objCashTO);
                                                        tranMap.put("DAILYDEPOSITTRANSTO", cashList);
//                                                tempTranMap = cashDao.execute(tranMap, false);
                                                    }
                                                }
                                            }


                                            if (objTO.getTransType().equals("Sales")) {
//                                                System.out.println("SALECASH2222222acHeadsVat>>>" + acHeadsVat.get("SALE_VAT_AC_HD_ID"));
//                                                System.out.println("CASH22222222222objTO.getVatAmt()>>>" + objTO.getVatAmt());
//                                                System.out.println("CASH2222222222acHeads.get(SALES_AC_HD_ID)>>>" + acHeads.get("SALES_AC_HD_ID"));
//                                                System.out.println("CASH2222222222objTO.getAmount()>>>" + objTO.getAmount());
                                                if (objTO.getAmount() > 0) {
                                                    if (acHeads.get("SALES_AC_HD_ID") != null) {
                                                        setCashTransDetails(objTO.getAmount(), acHeads.get("SALES_AC_HD_ID").toString(), CommonConstants.CREDIT, map, generateSingleTransId);
                                                        objCashTO.setAcHdId((String) acHeads.get("PURCHASE_AC_HD_ID"));
                                                        objCashTO.setAmount(objTO.getAmount());
                                                        objCashTO.setTransType(CommonConstants.CREDIT);
                                                        objCashTO.setScreenName((String) map.get(CommonConstants.SCREEN));
                                                        cashList.add(/*
                                                                 * setCashTransaction(txMap)
                                                                 */objCashTO);
                                                        tranMap.put("DAILYDEPOSITTRANSTO", cashList);
//                                                tempTranMap = cashDao.execute(tranMap, false);
                                                    }
                                                }
                                                if (objTO.getVatAmt() > 0) {
                                                    if (acHeadsVat.get("SALE_VAT_AC_HD_ID") != null) {
                                                        setCashTransDetails(objTO.getVatAmt(), acHeadsVat.get("SALE_VAT_AC_HD_ID").toString(), CommonConstants.CREDIT, map, generateSingleTransId);
                                                        objCashTO.setAcHdId(acHeadsVat.get("SALE_VAT_AC_HD_ID").toString());
                                                        objCashTO.setAmount(objTO.getVatAmt());
                                                        objCashTO.setTransType(CommonConstants.CREDIT);
                                                        objCashTO.setScreenName((String) map.get(CommonConstants.SCREEN));
                                                        cashList.add(/*
                                                                 * setCashTransaction(txMap)
                                                                 */objCashTO);
                                                        tranMap.put("DAILYDEPOSITTRANSTO", cashList);
//                                                tempTranMap = cashDao.execute(tranMap, false);
                                                    }
                                                }
                                                if (objTO.getMiscAmt() > 0) {
                                                    if (acHeadsMisc.get("MISC_INCOME_HEAD") != null) {
                                                        setCashTransDetails(objTO.getMiscAmt(), acHeadsMisc.get("MISC_INCOME_HEAD").toString(), CommonConstants.CREDIT, map, generateSingleTransId);
                                                        objCashTO.setAcHdId(acHeadsMisc.get("MISC_INCOME_HEAD").toString());
                                                        objCashTO.setAmount(objTO.getMiscAmt());
                                                        objCashTO.setTransType(CommonConstants.CREDIT);
                                                        objCashTO.setScreenName((String) map.get(CommonConstants.SCREEN));
                                                        cashList.add(/*
                                                                 * setCashTransaction(txMap)
                                                                 */objCashTO);
                                                        tranMap.put("DAILYDEPOSITTRANSTO", cashList);
//                                                tempTranMap = cashDao.execute(tranMap, false);
                                                    }
                                                }

                                            }

                                            if (objTO.getTransType().equals("Damage")) {
//                                                System.out.println("SALECASH2222222acHeadsVat>>>" + acHeadsVat.get("VAT_AC_HD_ID"));
//                                                System.out.println("CASH22222222222objTO.getVatAmt()>>>" + objTO.getVatAmt());
//                                                System.out.println("CASH2222222222acHeads.get(DAMAGE_AC_HD_ID)>>>" + acHeads.get("DAMAGE_AC_HD_ID"));
//                                                System.out.println("CASH2222222222objTO.getAmount()>>>" + objTO.getAmount());
                                                if (objTO.getAmount() > 0) {
                                                    if (acHeads.get("DAMAGE_AC_HD_ID") != null) {
                                                        setCashTransDetails(objTO.getAmount(), acHeads.get("DAMAGE_AC_HD_ID").toString(), CommonConstants.CREDIT, map, generateSingleTransId);
                                                        objCashTO.setAcHdId((String) acHeads.get("PURCHASE_AC_HD_ID"));
                                                        objCashTO.setAmount(objTO.getAmount());
                                                        objCashTO.setTransType(CommonConstants.CREDIT);
                                                        objCashTO.setScreenName((String) map.get(CommonConstants.SCREEN));
                                                        cashList.add(/*
                                                                 * setCashTransaction(txMap)
                                                                 */objCashTO);
                                                        tranMap.put("DAILYDEPOSITTRANSTO", cashList);
//                                                tempTranMap = cashDao.execute(tranMap, false);
                                                    }
                                                }
                                                if (objTO.getVatAmt() > 0) {
                                                    if (acHeadsVat.get("DAM_VAT_AC_HD_ID") != null) {
                                                        setCashTransDetails(objTO.getVatAmt(), acHeadsVat.get("DAM_VAT_AC_HD_ID").toString(), CommonConstants.CREDIT, map, generateSingleTransId);
                                                        objCashTO.setAcHdId(acHeadsVat.get("DAM_VAT_AC_HD_ID").toString());
                                                        objCashTO.setAmount(objTO.getVatAmt());
                                                        objCashTO.setTransType(CommonConstants.CREDIT);
                                                        objCashTO.setScreenName((String) map.get(CommonConstants.SCREEN));
                                                        cashList.add(/*
                                                                 * setCashTransaction(txMap)
                                                                 */objCashTO);
                                                        tranMap.put("DAILYDEPOSITTRANSTO", cashList);
//                                                tempTranMap = cashDao.execute(tranMap, false);
                                                    }
                                                }
                                                if (objTO.getMiscAmt() > 0) {
                                                    if (acHeadsMisc.get("MISC_INCOME_HEAD") != null) {
                                                        setCashTransDetails(objTO.getMiscAmt(), acHeadsMisc.get("MISC_INCOME_HEAD").toString(), CommonConstants.CREDIT, map, generateSingleTransId);
                                                        objCashTO.setAcHdId(acHeadsMisc.get("MISC_INCOME_HEAD").toString());
                                                        objCashTO.setAmount(objTO.getMiscAmt());
                                                        objCashTO.setTransType(CommonConstants.CREDIT);
                                                        objCashTO.setScreenName((String) map.get(CommonConstants.SCREEN));
                                                        cashList.add(/*
                                                                 * setCashTransaction(txMap)
                                                                 */objCashTO);
                                                        tranMap.put("DAILYDEPOSITTRANSTO", cashList);
//                                                tempTranMap = cashDao.execute(tranMap, false);
                                                    }
                                                }

                                            }

                                            if (objTO.getTransType().equals("Deficite")) {
//                                                System.out.println("SALECASH2222222acHeadsVat>>>" + acHeadsVat.get("DEF_VAT_AC_HD_ID"));
//                                                System.out.println("CASH22222222222objTO.getVatAmt()>>>" + objTO.getVatAmt());
//                                                System.out.println("CASH2222222222acHeads.get(DEFICIATE_HC_HD_ID)>>>" + acHeads.get("DEFICIATE_HC_HD_ID"));
//                                                System.out.println("CASH2222222222objTO.getAmount()>>>" + objTO.getAmount());
                                                if (objTO.getAmount() > 0) {
                                                    if (acHeads.get("DEFICIATE_HC_HD_ID") != null) {
                                                        setCashTransDetails(objTO.getAmount(), acHeads.get("DEFICIATE_HC_HD_ID").toString(), CommonConstants.CREDIT, map, generateSingleTransId);
                                                        objCashTO.setAcHdId((String) acHeads.get("DEFICIATE_HC_HD_ID"));
                                                        objCashTO.setAmount(objTO.getAmount());
                                                        objCashTO.setTransType(CommonConstants.CREDIT);
                                                        objCashTO.setScreenName((String) map.get(CommonConstants.SCREEN));
                                                        cashList.add(/*
                                                                 * setCashTransaction(txMap)
                                                                 */objCashTO);
                                                        tranMap.put("DAILYDEPOSITTRANSTO", cashList);
//                                                tempTranMap = cashDao.execute(tranMap, false);
                                                    }
                                                }
                                                if (objTO.getVatAmt() > 0) {
                                                    if (acHeadsVat.get("DEF_VAT_AC_HD_ID") != null) {
                                                        setCashTransDetails(objTO.getVatAmt(), acHeadsVat.get("DEF_VAT_AC_HD_ID").toString(), CommonConstants.CREDIT, map, generateSingleTransId);
                                                        objCashTO.setAcHdId(acHeadsVat.get("DEF_VAT_AC_HD_ID").toString());
                                                        objCashTO.setAmount(objTO.getVatAmt());
                                                        objCashTO.setTransType(CommonConstants.CREDIT);
                                                        objCashTO.setScreenName((String) map.get(CommonConstants.SCREEN));
                                                        cashList.add(/*
                                                                 * setCashTransaction(txMap)
                                                                 */objCashTO);
                                                        tranMap.put("DAILYDEPOSITTRANSTO", cashList);
//                                                tempTranMap = cashDao.execute(tranMap, false);
                                                    }
                                                }
                                                if (objTO.getMiscAmt() > 0) {
                                                    if (acHeadsMisc.get("MISC_INCOME_HEAD") != null) {
                                                        setCashTransDetails(objTO.getMiscAmt(), acHeadsMisc.get("MISC_INCOME_HEAD").toString(), CommonConstants.CREDIT, map, generateSingleTransId);
                                                        objCashTO.setAcHdId(acHeadsMisc.get("MISC_INCOME_HEAD").toString());
                                                        objCashTO.setAmount(objTO.getMiscAmt());
                                                        objCashTO.setTransType(CommonConstants.CREDIT);
                                                        objCashTO.setScreenName((String) map.get(CommonConstants.SCREEN));
                                                        cashList.add(/*
                                                                 * setCashTransaction(txMap)
                                                                 */objCashTO);
                                                        tranMap.put("DAILYDEPOSITTRANSTO", cashList);
//                                                tempTranMap = cashDao.execute(tranMap, false);
                                                    }
                                                }

                                            }

                                            if (objTO.getTransType().equals("Purchase Return")) {
//                                                System.out.println("PURCHASE RETURN CASH33333333acHeadsVat>>>" + acHeadsVat);
//                                                System.out.println("CASH33333333objTO.getVatAmt()>>>" + objTO.getVatAmt());
//                                                System.out.println("CASH33333333acHeads.get(PUR_RET_AC_HD_ID)>>>" + acHeads.get("PUR_RET_AC_HD_ID"));
//                                                System.out.println("CASH33333333objTO.getAmount()>>>" + objTO.getAmount());
                                                if (objTO.getAmount() > 0) {
                                                    if (acHeads.get("PUR_RET_AC_HD_ID") != null) {
                                                        setCashTransDetails(objTO.getAmount(), acHeads.get("PUR_RET_AC_HD_ID").toString(), CommonConstants.CREDIT, map, generateSingleTransId);
                                                        objCashTO.setAcHdId((String) acHeads.get("PUR_RET_AC_HD_ID"));
                                                        objCashTO.setAmount(objTO.getAmount());
                                                        objCashTO.setTransType(CommonConstants.CREDIT);
                                                        objCashTO.setScreenName((String) map.get(CommonConstants.SCREEN));
                                                        cashList.add(/*
                                                                 * setCashTransaction(txMap)
                                                                 */objCashTO);
                                                        tranMap.put("DAILYDEPOSITTRANSTO", cashList);
//                                                tempTranMap = cashDao.execute(tranMap, false);
                                                    }
                                                }
                                                if (objTO.getVatAmt() > 0) {
                                                    if (acHeadsVat.get("PUR_RTN_VAT_AC_HD_ID") != null) {
                                                        setCashTransDetails(objTO.getVatAmt(), acHeadsVat.get("PUR_RTN_VAT_AC_HD_ID").toString(), CommonConstants.CREDIT, map, generateSingleTransId);
                                                        objCashTO.setAcHdId(acHeadsVat.get("PUR_RTN_VAT_AC_HD_ID").toString());
                                                        objCashTO.setAmount(objTO.getVatAmt());
                                                        objCashTO.setTransType(CommonConstants.CREDIT);
                                                        objCashTO.setScreenName((String) map.get(CommonConstants.SCREEN));
                                                        cashList.add(/*
                                                                 * setCashTransaction(txMap)
                                                                 */objCashTO);
                                                        tranMap.put("DAILYDEPOSITTRANSTO", cashList);
//                                                tempTranMap = cashDao.execute(tranMap, false);
                                                    }
                                                }
                                                if (objTO.getMiscAmt() > 0) {
                                                    if (acHeadsMisc.get("MISC_INCOME_HEAD") != null) {
                                                        setCashTransDetails(objTO.getMiscAmt(), acHeadsMisc.get("MISC_INCOME_HEAD").toString(), CommonConstants.CREDIT, map, generateSingleTransId);
                                                        objCashTO.setAcHdId(acHeadsMisc.get("MISC_INCOME_HEAD").toString());
                                                        objCashTO.setAmount(objTO.getMiscAmt());
                                                        objCashTO.setTransType(CommonConstants.CREDIT);
                                                        objCashTO.setScreenName((String) map.get(CommonConstants.SCREEN));
                                                        cashList.add(/*
                                                                 * setCashTransaction(txMap)
                                                                 */objCashTO);
                                                        tranMap.put("DAILYDEPOSITTRANSTO", cashList);
//                                                tempTranMap = cashDao.execute(tranMap, false);
                                                    }
                                                }
                                            }

                                            cashDao = null;
                                            tranMap = null;
//                                            tempTranMap = null;



                                        }


                                    }
                                    /*
                                     * else {
                                     *
                                     * // txMap.put(TransferTrans.DR_AC_HD,
                                     * (String)acHeads.get("SHARE_ACHD"));
                                     * txMap.put(CashTransaction.PARTICULARS,
                                     * "To "+objTO.getBorrowingNo()+"
                                     * Disbursement");
                                     * txMap.put(CommonConstants.USER_ID,
                                     * logTO.getUserId());
                                     * txMap.put("DR_INSTRUMENT_2","DEPOSIT_TRANS");
                                     * txMap.put("DR_INST_TYPE","WITHDRAW_SLIP");
                                     * txMap.put(TransferTrans.DR_BRANCH,
                                     * _branchCode);
                                     * txMap.put(TransferTrans.CR_BRANCH,
                                     * _branchCode);
                                     * txMap.put(TransferTrans.CR_PROD_TYPE,
                                     * TransactionFactory.GL); if
                                     * (objTransactionTO.getProductType().equals("GL"))
                                     * {
                                     * txMap.put(TransferTrans.DR_AC_HD,CommonUtil.convertObjToStr(objTransactionTO.getDebitAcctNo()));
                                     *
                                     * } else {
                                     * txMap.put(TransferTrans.DR_ACT_NUM,CommonUtil.convertObjToStr(objTransactionTO.getDebitAcctNo()));
                                     * txMap.put(TransferTrans.DR_PROD_ID,
                                     * CommonUtil.convertObjToStr(objTransactionTO.getProductId()));
                                     * } txMap.put(TransferTrans.DR_PROD_TYPE,
                                     * CommonUtil.convertObjToStr(objTransactionTO.getProductType()));
                                     * transferList.add(objTransferTrans.getDebitTransferTO(txMap,
                                     * debitAmt)); //
                                     * transferList.add(objTransferTrans.getCreditTransferTO(txMap,
                                     * shareAmt)); //
                                     * objTransferTrans.doDebitCredit(transferList,
                                     * _branchCode, false); if(amtBorrowed>0.0){
                                     * txMap.put(TransferTrans.CR_AC_HD,
                                     * (String)acHeads.get("PRINCIPAL_GRP_HEAD"));
                                     * txMap.put("AUTHORIZEREMARKS","PRINCIPAL_GRP_HEAD");
                                     * //
                                     * transferList.add(objTransferTrans.getDebitTransferTO(txMap,
                                     * shareAmt));
                                     * transferList.add(objTransferTrans.getCreditTransferTO(txMap,
                                     * amtBorrowed)); //
                                     * objTransferTrans.doDebitCredit(transferList,
                                     * _branchCode, false); }
                                     * objTransferTrans.doDebitCredit(transferList,
                                     * _branchCode, false);
                                    }
                                     */
                                    //End cash
//                                    objTransactionTO.setStatus(CommonConstants.STATUS_CREATED);
//                                    objTransactionTO.setBatchId(objTO.getStrIRNo());
//                                    objTransactionTO.setBatchDt(currDt);
//                                    objTransactionTO.setTransId(objTransferTrans.getLinkBatchId());
//                                    objTransactionTO.setBranchId(_branchCode);
//                                    System.out.println("objTransactionTO------------------->" + objTransactionTO);
//                                    sqlMap.executeUpdate("insertRemittanceIssueTransactionTO", objTransactionTO);
                                }
                            }
                        }
//                        amtIndend = 0.0;
//                        objTransferTrans = null;
//                        transferList = null;
//                        txMap = null;

                    }
                    if (objTransactionTO.getTransType().equals("TRANSFER")) {
                        objTransferTrans.doDebitCredit(transferList, _branchCode, false);
                    } else {
//                        tranMap.put(CommonConstants.BRANCH_ID, CommonUtil.convertObjToStr(map.get("BRANCH_CODE")));
//                        tranMap.put(CommonConstants.USER_ID, CommonUtil.convertObjToStr(map.get("USER_ID")));
//                        tranMap.put(CommonConstants.IP_ADDR, CommonUtil.convertObjToStr(map.get("IP_ADDR")));
//                        tranMap.put(CommonConstants.MODULE, CommonUtil.convertObjToStr(map.get("MODULE")));
//                        tranMap.put(CommonConstants.SCREEN, CommonUtil.convertObjToStr(map.get("SCREEN")));
//                        tranMap.put("MODE", CommonConstants.TOSTATUS_INSERT);
//                        tranMap.put("DAILYDEPOSITTRANSTO", cashList);
////                        CashTransactionDAO cashDao;
//                        cashDao = new CashTransactionDAO();
//                        tranMap = cashDao.execute(tranMap, false);
//                        System.out.println("returnmap in setCashTransDetails>>>>" + tranMap);
                    }
                    objTransactionTO.setStatus(CommonConstants.STATUS_CREATED);
                    objTransactionTO.setBatchId(objTO.getStrIRNo());
                    objTransactionTO.setBatchDt(currDt);
                    objTransactionTO.setTransId(objTransferTrans.getLinkBatchId());
                    objTransactionTO.setBranchId(_branchCode);
                   // System.out.println("objTransactionTO------------------->" + objTransactionTO);
                    sqlMap.executeUpdate("insertRemittanceIssueTransactionTO", objTransactionTO);

//                    cashDao = null;
//                    tranMap = null;
                    cashList = null;
                    amtIndend = 0.0;
                    objTransferTrans = null;
                    transferList = null;
                    txMap = null;
                    debitCreditFlag1 = 0;
                    debitCreditFlag2 = 0;
                    creditFlag1 = 0;
                    // Code End
                    getTransDetails(objTO.getStrIRNo());

                } else {
                    HashMap shareAcctNoMap = new HashMap();
                    //                    shareAcctNoMap = (HashMap)sqlMap.executeQueryForObject("transferResolvedShare", shareAcctNoMap);
                    //                    sqlMap.executeUpdate("updateShareAcctDetailsTO", shareAcctDetailsTO);
                    double amtIndend = CommonUtil.convertObjToDouble(objTO.getAmount()).doubleValue();
                    if (objTO.getCommand().equals(CommonConstants.TOSTATUS_DELETE)) {
//                        shareAcctNoMap = new HashMap();
//                        shareAcctNoMap.put("LINK_BATCH_ID",objTO.getBorrowingNo());
//                        List lst = sqlMap.executeQueryForList("getAuthBatchTxTransferTOs", shareAcctNoMap);
////                        TxTransferTO txTransferTO = null;
//                        double oldAmount = 0;
//                        HashMap oldAmountMap = new HashMap();
//                        ArrayList transferList = new ArrayList();
//                        if (lst!=null && lst.size()>0) {
//                            for (int j=0; j<lst.size(); j++) {
//                                txTransferTO = (TxTransferTO) lst.get(j);
//                                System.out.println("#@$@#$@#$lst:"+lst);
//                            }
//                            
//                        }else{
//                            System.out.println("In Cash Edit");
//                            LinkedHashMap TransactionDetailsMap = (LinkedHashMap) map.get("TransactionTO");
//                            if(TransactionDetailsMap.containsKey("NOT_DELETED_TRANS_TOs")) {
//                                LinkedHashMap allowedTransDetailsTO = (LinkedHashMap)TransactionDetailsMap.get("NOT_DELETED_TRANS_TOs");
//                                TransactionTO objTransactionTO = null;
//                                if(allowedTransDetailsTO!=null && allowedTransDetailsTO.size()>0){
//                                    for (int J = 1;J <= allowedTransDetailsTO.size();J++)  {
//                                        objTransactionTO = (TransactionTO) allowedTransDetailsTO.get(String.valueOf(J));
//                                        
//                                        //                                if ((oldAmount != newAmount) || command.equals(CommonConstants.TOSTATUS_DELETE)) {
//                                        HashMap tempMap=new HashMap();
//                                        //                                        if(!CommonUtil.convertObjToStr(shareAcctDetailsTO.getShareNoFrom()).equals("ADD")){
//                                        List cLst1 = sqlMap.executeQueryForList("getSelectShareCashTransactionTO",   CommonUtil.convertObjToStr(objTransactionTO.getTransId()));
//                                        if (cLst1!=null && cLst1.size()>0) {
//                                            CashTransactionTO txTransferTO1 = null;
//                                            txTransferTO1 = (CashTransactionTO) cLst1.get(0);
//                                            oldAmount= CommonUtil.convertObjToDouble(txTransferTO1.getAmount()).doubleValue();
//                                            double newAmount = CommonUtil.convertObjToDouble(objTransactionTO.getTransAmt()).doubleValue();
//                                            txTransferTO1.setInpAmount(new Double(newAmount));
//                                            txTransferTO1.setAmount(new Double(newAmount));
//                                            txTransferTO1.setCommand(command);
//                                            txTransferTO1.setStatus(CommonConstants.STATUS_DELETED);
//                                            txTransferTO1.setStatusDt(currDt);
//                                            
//                                            map.put("PRODUCTTYPE", TransactionFactory.GL);
//                                            map.put("OLDAMOUNT", new Double(oldAmount));
//                                            map.put("CashTransactionTO", txTransferTO1);
//                                            CashTransactionDAO cashTransDAO = new CashTransactionDAO();
//                                            cashTransDAO.execute(map,false);
//                                        }
//                                        cLst1 = null;
//                                        //                                        }
//                                        
//                                        //                                                         for (int J = 1;J <= allowedTransDetailsTO.size();J++){
//                                        
//                                        objTransactionTO.setStatus(CommonConstants.STATUS_DELETED);
//                                        objTransactionTO.setTransId(objDrfTransactionTO.getDrfTransID());
//                                        objTransactionTO.setBatchId(objDrfTransactionTO.getDrfTransID());
//                                        objTransactionTO.setBranchId(_branchCode);
//                                        sqlMap.executeUpdate("updateRemittanceIssueTransactionTO", objTransactionTO);
//                                        
//                                    }
//                                    
//                                }
//                                
//                                //                                }
//                                
//                                
//                            }
//                            lst = null;
//                            oldAmountMap = null;
//                            transferList = null;
//                            shareAcctNoMap = null;
//                            txTransferTO = null;
//                        }
                    }
                }
            }
        } catch (Exception e) {
            //                sqlMap.rollbackTransaction();
            e.printStackTrace();
            //                throw new TransRollbackException(e);
            throw e;
        }
    }

    public Date getProperDateFormat(Object obj) {
        Date currDate = null;
        if (obj != null && obj.toString().length() > 0) {
            Date tempDt = DateUtil.getDateMMDDYYYY(CommonUtil.convertObjToStr(obj));
            System.out.println("currDt in properdateformat>>>>>" + currDt);
            currDate = (Date) currDt.clone();
            currDate.setDate(tempDt.getDate());
            currDate.setMonth(tempDt.getMonth());
            currDate.setYear(tempDt.getYear());
        }
        return currDate;
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

    private void getTransDetails(String batchId) throws Exception {
        HashMap getTransMap = new HashMap();
        getTransMap.put("BATCH_ID", batchId);
        getTransMap.put("TRANS_DT", currDt);
        getTransMap.put(CommonConstants.BRANCH_ID, _branchCode);
        //        System.out.println("########getTransferTransBatchID returnMap depCloseAmt:"+resultMap);
//        returnMap = new HashMap();
        List transList = (List) sqlMap.executeQueryForList("getTransferDetails", getTransMap);
        if (transList != null && transList.size() > 0) {
            returnMap.put("TRANSFER_TRANS_LIST", transList);
        }
        List cashList = (List) sqlMap.executeQueryForList("getCashDetails", getTransMap);
        if (cashList != null && cashList.size() > 0) {
            returnMap.put("CASH_TRANS_LIST", cashList);
        }
        getTransMap.clear();
        getTransMap = null;
        transList = null;
        cashList = null;
    }

    private void updateData(HashMap map) throws Exception {
        try {
            sqlMap.startTransaction();
            HashMap finalMap = (HashMap) map.get("finalMap");
            processLstIterator = finalMap.keySet().iterator();
            String key1 = "";
            System.out.println("fmap...." + finalMap.size());
            for (int i = 0; i < finalMap.size(); i++) {
                key1 = (String) processLstIterator.next();
                objTO = (IndendTO) finalMap.get(key1);
                objTO.setStatus(CommonConstants.STATUS_MODIFIED);
                System.out.println("map.get(USER_ID)>>" + map.get("USER_ID"));
                objTO.setStatusBy(map.get("USER_ID").toString());
                //logTO.setData(objTO.toString());
                //  logTO.setPrimaryKey(objTO.getKeyData());
                // logTO.setStatus(objTO.getCommand());
                sqlMap.executeUpdate("updateIndendTO", objTO);
            }
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
            //logTO.setData(objTO.toString());
            // logTO.setPrimaryKey(objTO.getKeyData());
            // logTO.setStatus(objTO.getCommand());
            sqlMap.executeUpdate("deleteIndendTO", objTO);
            //   logDAO.addToLog(logTO);
            sqlMap.commitTransaction();
        } catch (Exception e) {
            sqlMap.rollbackTransaction();
            e.printStackTrace();
            throw new TransRollbackException(e);
        }
    }

    public static void main(String str[]) {
        try {
            IndendDAO dao = new IndendDAO();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public HashMap execute(HashMap map) throws Exception {
        _branchCode = (String) map.get(CommonConstants.BRANCH_ID);
        currDt = ServerUtil.getCurrentDate(_branchCode);
        returnMap = new HashMap();
        logDAO = new LogDAO();
        logTO = new LogTO();
        logTO.setUserId(CommonUtil.convertObjToStr(map.get(CommonConstants.USER_ID)));
        logTO.setBranchId(CommonUtil.convertObjToStr(map.get(CommonConstants.BRANCH_ID)));
        logTO.setIpAddr(CommonUtil.convertObjToStr(map.get(CommonConstants.IP_ADDR)));
        logTO.setModule(CommonUtil.convertObjToStr(map.get(CommonConstants.MODULE)));
        logTO.setScreen(CommonUtil.convertObjToStr(map.get(CommonConstants.SCREEN)));

        System.out.println("*&$@#$@#$map: in indend Dao" + map);

        if (!map.containsKey(CommonConstants.AUTHORIZEMAP)) {
            objTO = (IndendTO) map.get("IndendTO");
            final String command = objTO.getCommand();
            if (command.equals(CommonConstants.TOSTATUS_INSERT)) {
                insertData(map);
                //            returnMap = new HashMap();
            //    System.out.println("objTO.getStrIRNo()objTO.getStrIRNo()objTO.getStrIRNo()====:" + objTO);
                returnMap.put("IRNO", objTO.getStrIRNo());
            } else if (command.equals(CommonConstants.TOSTATUS_UPDATE)) {
                objTO.setStatusBy(map.get("USER_ID").toString());
                deleteData();
                HashMap irMap = new HashMap();
                irMap.put("IRID", objTO.getStrIRNo());
                ServerUtil.executeQuery("deleteCashtransForIndendEdit", irMap);
                ServerUtil.executeQuery("deleteTransfertransForIndendEdit", irMap);
                objTO.setCommand(CommonConstants.TOSTATUS_INSERT);
                insertData(map);
//                updateData(map);
//                getTransDetails(objTO.getStrIRNo());
                returnMap.put("IRNO", objTO.getStrIRNo());
            } else if (command.equals(CommonConstants.TOSTATUS_DELETE)) {
                deleteData();
                returnMap.put("IRNO", objTO.getStrIRNo());
            } else {
                throw new NoCommandException();
            }
        } else {
           // System.out.println("map:" + map);
            HashMap authMap = (HashMap) map.get(CommonConstants.AUTHORIZEMAP);
            authMap.put(CommonConstants.BRANCH_ID, (String) map.get(CommonConstants.BRANCH_ID));
            authMap.put(CommonConstants.USER_ID, (String) map.get(CommonConstants.USER_ID));
            System.out.println("authMap:" + authMap);
            if (authMap != null) {
                authorize(authMap);
            }
        }

        destroyObjects();
        System.out.println("@#@#@# returnMap:" + returnMap);
        return returnMap;
    }

    private void authorize(HashMap map) throws Exception {
      //  System.out.println("authorize#########" + map);
        String status = (String) map.get("STATUS");
        String linkBatchId = null;
        String disbursalNo = null;
        HashMap cashAuthMap;
        TransactionTO objTransactionTO = null;
        try {
            sqlMap.startTransaction();
            transactionDAO = new TransactionDAO(CommonConstants.DEBIT);
        //    System.out.println("map111111@@@1111:" + map);
            disbursalNo = CommonUtil.convertObjToStr(map.get("IRID"));
            map.put(CommonConstants.STATUS, status);
            map.put(CommonConstants.USER_ID, map.get(CommonConstants.USER_ID));
            map.put("CURR_DATE", currDt);
          //  System.out.println("status------------>" + status);
            sqlMap.executeUpdate("authorizeIndend", map);  //authorizeIndendDisbursal not there in map
            linkBatchId = CommonUtil.convertObjToStr(map.get("IRID"));//Transaction Batch Id
            //System.out.println("linkBatchId11111>>>>>>" + linkBatchId);
            //Separation of Authorization for Cash and Transfer
            //Call this in all places that need Authorization for Transaction
            cashAuthMap = new HashMap();
            //System.out.println("@#$@zvbvxcvzx#$map:" + map);
            cashAuthMap.put(CommonConstants.BRANCH_ID, map.get(CommonConstants.BRANCH_ID));
            cashAuthMap.put(CommonConstants.USER_ID, map.get(CommonConstants.USER_ID));
            cashAuthMap.put("INITIATED_BRANCH", map.get(CommonConstants.BRANCH_ID));
            //cashAuthMap.put("DAILY", "DAILY");
            //cashAuthMap.put("INDEND_TRANSACTION", "INDEND_TRANSACTION");
            cashAuthMap.put("CURR_DATE", currDt);
            cashAuthMap.put("TODAY_DT", currDt);
//            System.out.println("map:" + map);
//            System.out.println("cashAuthMap:" + cashAuthMap);
//            System.out.println("#$%#$%#$%xcvlinkBatchId" + linkBatchId);
            HashMap tempAuthMap = new HashMap();
            tempAuthMap.put("LINK_BATCH_ID", linkBatchId);
  //          System.out.println("map.get(AUTHORIZEDT??>>>" + map.get("AUTHORIZEDT"));
            tempAuthMap.put("TRANS_DT", getProperDateFormat(map.get("AUTHORIZEDT")));
            tempAuthMap.put("INITIATED_BRANCH", cashAuthMap.get("BRANCH_CODE"));
    //        System.out.println("tempAuthMap>>>>>" + tempAuthMap);
            //added by sreekrishnan for cashier
            List listData = ServerUtil.executeQuery("getCashierAuth", new HashMap());
            if (listData != null && listData.size() > 0) {
                HashMap map1 = (HashMap) listData.get(0);
                if (map1.get("CASHIER_AUTH_ALLOWED") != null && map1.get("CASHIER_AUTH_ALLOWED").toString().equals("Y")) {
                    List lst = sqlMap.executeQueryForList("getCashTransTransID", cashAuthMap);
                    if (lst != null && lst.size() > 0) {
                        HashMap obj = (HashMap) lst.get(0);
                        cashAuthMap.put("INDEND_TRANSACTION", "INDEND_TRANSACTION");
                    }
                }
            }
        //    System.out.println("linkBatchId or TRANS_ID" + cashAuthMap.get("TRANS_ID"));
            TransactionDAO.authorizeCashAndTransfer(linkBatchId, status, cashAuthMap);

            HashMap transAuthMap = new HashMap();
            transAuthMap.put("LINK_BATCH_ID", linkBatchId);
            // tempAuthMap.put("TRANS_DT",map.get("AUTHORIZEDT"));
            transAuthMap.put("INITIATED_BRANCH", cashAuthMap.get("BRANCH_CODE"));
          //  System.out.println("transAuthMap>>>>>" + transAuthMap);
            List transAuthList;
            transAuthList = sqlMap.executeQueryForList("getSelectTransferTransForauthorize", transAuthMap);
            //System.out.println("linkBatchId222@@22>>>>>" + linkBatchId);
//            System.out.println("status222@@22>>>>>" + transAuthList);
//            System.out.println("cashAuthMap222@@22>>>>>" + transAuthList);
//            System.out.println("transAuthList>>>>>" + transAuthList);
            if (transAuthList != null && transAuthList.size() > 0) {
                for (int i = 0; i <= transAuthList.size(); i++) {
                    TransactionDAO.authorizeCashAndTransfer(linkBatchId, status, cashAuthMap);
                }
            }
            //  }
            HashMap transMap = new HashMap();
            transMap.put("LINK_BATCH_ID", linkBatchId);
       //     System.out.println("transMap----------------->" + transMap);
            sqlMap.executeUpdate("updateInstrumentNO1Transfer", transMap);
            sqlMap.executeUpdate("updateInstrumentNO1Cash", transMap);
            transMap = null;
         //   System.out.println("disbursalNo----------------->" + disbursalNo);
            objTransactionTO = new TransactionTO();
            objTransactionTO.setBatchId(CommonUtil.convertObjToStr(disbursalNo));
            objTransactionTO.setTransId(CommonUtil.convertObjToStr(linkBatchId));
            objTransactionTO.setBranchId(_branchCode);
         //   System.out.println("objTransactionTO----------------->" + objTransactionTO);
            sqlMap.executeUpdate("deleteRemittanceIssueTransactionTO", objTransactionTO);
            if (!status.equals("REJECTED")) {
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
        return getData(obj);
    }

    private void destroyObjects() {
        objTO = null;
    }
}
