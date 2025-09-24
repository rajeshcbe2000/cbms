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

import java.sql.SQLException;
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
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.Date;
import java.util.Iterator;
//import org.jfree.xml.ParseException;

/**
 * TokenConfig DAO.
 *
 */
public class IndendMultipleDAO extends TTDAO {

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
    public IndendMultipleDAO() throws ServiceLocatorException {
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
            System.out.println("irno11@@@!!!###>>>>" + irno);
//            objTO.setStrIRNo(getIRNo());
//            objTO.setStatus(CommonConstants.STATUS_CREATED);
            System.out.println("objTO.getGenTrans()>>>>>" + objTO.getGenTrans());
            if (objTO.getGenTrans() != null && objTO.getGenTrans().equalsIgnoreCase("YES")) {
                if(!(map.containsKey("INDEND_LIABILITY"))){
                    doIndendTransactions(map);
                }
            }
            //  logTO.setData(objTO.toString());
            //  logTO.setPrimaryKey(objTO.getKeyData());
            //   logTO.setStatus(objTO.getCommand());
            HashMap finalMap = (HashMap) map.get("finalMap");
            processLstIterator = finalMap.keySet().iterator();
            String key1 = "";
            System.out.println("fmap...." + finalMap.size());
            for (int i = 0; i < finalMap.size(); i++) {
                key1 = (String) processLstIterator.next();
                objTO = (IndendTO) finalMap.get(key1);
                System.out.println("irno22@@@!!!###>>>>" + irno);
                objTO.setStrIRNo(irno);
                objTO.setStatus(CommonConstants.STATUS_CREATED);
                objTO.setBranch(_branchCode);
                // objTO.setStatusBy("");
                System.out.println("!@#!@#@# !objTO111@@@>>>" + objTO);
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
            System.out.println("objTO.getAmount()------------------->" + objTO.getAmount());
            System.out.println("generateSingleTransId------------------->" + generateSingleTransId);
            System.out.println("map------------------->" + map);
            System.out.println("acHeads------------------->" + accHd);
            System.out.println("objTO------------------->" + objTO);
            CashTransactionTO objCashTO = new CashTransactionTO();
//            CashTransactionDAO cashDao = new CashTransactionDAO();       
            ArrayList cashList = new ArrayList();
            HashMap tranMap = new HashMap();
            objCashTO.setInitTransId(logTO.getUserId());
            objCashTO.setBranchId(_branchCode);
            objCashTO.setStatusBy(logTO.getUserId());
            objCashTO.setStatusDt(currDt);
            String billDate = DateUtil.getStringDate(DateUtil.getDateMMDDYYYY(CommonUtil.convertObjToStr(objTO.getBillDate())));
            String saleDate = DateUtil.getStringDate(DateUtil.getDateMMDDYYYY(CommonUtil.convertObjToStr(objTO.getSalesDate())));               
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
            if (objTO.getTransType().equals("Sales") || objTO.getTransType().equals("Sales Return")) {
                objCashTO.setParticulars("Sale dated : " + saleDate + " ");
            }else if(objTO.getTransType().equals("Purchase") || objTO.getTransType().equals("Purchase Return")) {
                String particulars = getSupplierName(objTO.getSupplier()) +" "+" Bill No : " + objTO.getPurchBillNo()+ " "+ billDate;
                objCashTO.setParticulars("By"+" "+ particulars+" ");
            }
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
            objCashTO.setTransModType(CommonConstants.TXN_PROD_TYPE_INDEND);
            objCashTO.setSingleTransId(generateSingleTransId);
            objCashTO.setScreenName("Indend Transaction");
            objCashTO.setInstrumentNo2(objTO.getDepoId());// Added by nithya on 08-05-2019 for KD 451 - 0020678: Indend transaction(purchase and purchase return) issue
            // Added by nithya on 26-09-2018 for KD 262 - GST Indend Transaction - Needed to implement GST in Indent Transaction 
            if(map.containsKey("GST_PARTICULARS") && map.get("GST_PARTICULARS")!=null && !map.get("GST_PARTICULARS").equals("")){
                objCashTO.setInstrumentNo1(CommonUtil.convertObjToStr(map.get("GST_PARTICULARS")));
            }
            // End
            System.out.println("objCashTO in setCashTransDetails>>>>" + objCashTO);
            cashList.add(objCashTO);
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
            if (objTO.getCommand() != null) {
                if (objTO.getCommand().equals(CommonConstants.TOSTATUS_INSERT)) {
                    String generateSingleTransId = generateLinkID();
                    TransferTrans objTransferTrans = new TransferTrans();
                    ArrayList transferList = new ArrayList();
                    cashList = new ArrayList();
                    CashTransactionDAO cashDao;
                    HashMap txMap;
                    String key1 = "";
                    double creditTot = 0.0,debitTot = 0.0;
                    HashMap acHeads = null;
                    HashMap acHeadsMisc = null;
                    HashMap acHeadsVat = null;
                    HashMap acHeadsOthr = null;
                    HashMap acHeadsComm = null;                    
                    HashMap roundOffAcHeads = null;
                    String sgstAcHd = "";
                    String cgstAcHd = "";
                    String roundOffHead = "";
                    TransactionTO objTransactionTO = null;
                    boolean transflag  = false;
                    String debitorcredit = CommonUtil.convertObjToStr(map.get("DEBITORCREDIT"));
                    HashMap whereMap = new HashMap();
                    whereMap.put("IRNO", objTO.getStrIRNo());
                    //whereMap.put("DEPID", objTO.getDepoId());
                    HashMap finalMap = (HashMap) map.get("finalMap");
                    System.out.println("ma###############p??>>>" + map);
                    processLstIterator = finalMap.keySet().iterator();
                    for (int i = 0; i < finalMap.size(); i++) {
                        key1 = (String) processLstIterator.next();
                        objTO = (IndendTO) finalMap.get(key1);
                        objTO.setStrIRNo(irno);
                        objTO.setStatus(CommonConstants.STATUS_CREATED);
                        whereMap.put("DEPID", objTO.getDepoId());
                        if (objTO.getMiscAmt() != null) {
                            acHeadsMisc = (HashMap) sqlMap.executeQueryForObject("Indend.getAcHeadsMisc", whereMap);
                        }
                        if (objTO.getOthrExpAmt() != null) {
                            acHeadsOthr = (HashMap) sqlMap.executeQueryForObject("Indend.getAcHeadsOthrExpnse", whereMap);
                        }
                        if (objTO.getCommRecvdAmt() != null) {
                            acHeadsComm = (HashMap) sqlMap.executeQueryForObject("Indend.getAcHeadsComm", whereMap);
                        }
                        
                        // Added by nithya on 27-09-2018 for KD 262 - GST Indend Transaction - Needed to implement GST in Indent Transaction
                        if(objTO.getCgstAmt() > 0 && objTO.getSgstAmt() > 0){
                           HashMap acHeadsGST = (HashMap) sqlMap.executeQueryForObject("Indend.getGSTActHeads", whereMap);
                            if (objTO.getTransType() != null && objTO.getTransType().equals("Sales")) {
                                if (acHeadsGST != null && acHeadsGST.containsKey("SALES_CGST_AC_HD_ID") && acHeadsGST.get("SALES_CGST_AC_HD_ID") != null && !acHeadsGST.get("SALES_CGST_AC_HD_ID").equals("")) {
                                    cgstAcHd = CommonUtil.convertObjToStr(acHeadsGST.get("SALES_CGST_AC_HD_ID"));
                                } else {
                                    throw new TTException("CGST Account head not set properly...");
                                }
                                if (acHeadsGST != null && acHeadsGST.containsKey("SALES_SGST_AC_HD_ID") && acHeadsGST.get("SALES_SGST_AC_HD_ID") != null && !acHeadsGST.get("SALES_SGST_AC_HD_ID").equals("")) {
                                    sgstAcHd = CommonUtil.convertObjToStr(acHeadsGST.get("SALES_SGST_AC_HD_ID"));
                                } else {
                                    throw new TTException("SGST Account head not set properly...");
                                }
                            } else {
                           if(acHeadsGST != null && acHeadsGST.containsKey("CGST_AC_HD_ID") && acHeadsGST.get("CGST_AC_HD_ID") != null && !acHeadsGST.get("CGST_AC_HD_ID").equals("")){
                             cgstAcHd = CommonUtil.convertObjToStr(acHeadsGST.get("CGST_AC_HD_ID"));  
                           }else{
                             throw new TTException("CGST Account head not set properly...");  
                           }
                           if(acHeadsGST != null && acHeadsGST.containsKey("SGST_AC_HD_ID") && acHeadsGST.get("SGST_AC_HD_ID") != null && !acHeadsGST.get("SGST_AC_HD_ID").equals("")){
                             sgstAcHd = CommonUtil.convertObjToStr(acHeadsGST.get("SGST_AC_HD_ID"));  
                           }else{
                             throw new TTException("SGST Account head not set properly...");  
                           }
                        }                                                 
                        }                                                
                        // END
                        
                        
                        //Round off Heads                        
                        if (objTO.getRoundOffAmt() > 0) {
                            roundOffAcHeads = (HashMap) sqlMap.executeQueryForObject("Indend.getRoundOffActHeads", whereMap);                            
                            if (roundOffAcHeads != null && roundOffAcHeads.containsKey("ROUND_OFF_AC_HD_ID") && roundOffAcHeads.get("ROUND_OFF_AC_HD_ID") != null) {
                                roundOffHead = CommonUtil.convertObjToStr(roundOffAcHeads.get("ROUND_OFF_AC_HD_ID"));
                            }else{
                                throw new TTException("Round Off Account head not set properly...");
                            }                            
                        }
                        //End                        
                        
                        
                        //Sale heads...
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
                        //Purchase Heads.....
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
                        //Sale Return Heads......
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
                        //Purchase Return Heads...
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
                        //Damage Heads...
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
                        
                        //Discount Heads...
                        if (objTO.getTransType() != null && objTO.getTransType().equals("Discount")) { // Added by nithya on 04-04-2020 for KD-1732
                            acHeads = (HashMap) sqlMap.executeQueryForObject("Indend.getAcHeadsDicount", whereMap);                            
                            if (acHeads != null && acHeads.containsKey("DISCOUNT_AC_HD_ID") && acHeads.get("DISCOUNT_AC_HD_ID") == null) {
                                throw new TTException("Discount Account head not set properly...");
                            }
                            if (acHeads != null && acHeads.containsKey("DISCOUNT_VAT_AC_HD_ID") && acHeads.get("DISCOUNT_VAT_AC_HD_ID") == null) {
                                throw new TTException("Discount Vat Account head not set properly...");
                            }
                        }
                        
                        //Deficite Heads...
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
                        String billDate = DateUtil.getStringDate(DateUtil.getDateMMDDYYYY(CommonUtil.convertObjToStr(objTO.getBillDate())));
                        String saleDate = DateUtil.getStringDate(DateUtil.getDateMMDDYYYY(CommonUtil.convertObjToStr(objTO.getSalesDate())));
                        String particulars = "";
                        if(objTO.getTransType().equals("Purchase") || objTO.getTransType().equals("Purchase Return")) {
                            particulars = getSupplierName(objTO.getSupplier()) +" "+" Bill No : " + objTO.getPurchBillNo()+ " "+ billDate;    
                        }
                        objTransferTrans.setInitiatedBranch(_branchCode);
                        objTransferTrans.setLinkBatchId(objTO.getStrIRNo());
                        txMap = new HashMap();
                        LinkedHashMap TransactionDetailsMap = (LinkedHashMap) map.get("TransactionTO");
                        if (TransactionDetailsMap.size() > 0) {
                            if (TransactionDetailsMap.containsKey("NOT_DELETED_TRANS_TOs")) {
                                LinkedHashMap allowedTransDetailsTO = (LinkedHashMap) TransactionDetailsMap.get("NOT_DELETED_TRANS_TOs");
                                if(!transflag){
                                for (int J = 1; J <= allowedTransDetailsTO.size(); J++) {
                                    transflag = true;
                                    objTransactionTO = (TransactionTO) allowedTransDetailsTO.get(String.valueOf(J));
                                    if (objTransactionTO.getTransType().equals("TRANSFER")) {
                                        //Transfer Debit Transaction....
                                        //SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");                                                                             
                                        if (objTO.getTransType() != null && (objTO.getTransType().equals("Sales") || objTO.getTransType().equals("Sales Return"))) {
                                            txMap.put(TransferTrans.PARTICULARS, "Sale dated : " + saleDate);
                                        }
                                        if (objTO.getTransType() != null && (objTO.getTransType().equals("Purchase") || objTO.getTransType().equals("Purchase Return"))) {
                                            txMap.put(TransferTrans.PARTICULARS, particulars);
                                        }
                                        if (objTO.getTransType() != null && (objTO.getTransType().equals("Sales") || objTO.getTransType().equals("Purchase Return"))|| 
                                                objTO.getTransType().equals("Damage")||objTO.getTransType().equals("Deficite") || objTO.getTransType().equals("Discount")) {
                                            txMap = new HashMap();
                                            if (objTransactionTO.getProductType().equals("GL")) {
                                                txMap.put(TransferTrans.DR_AC_HD, CommonUtil.convertObjToStr(CommonUtil.convertObjToStr(objTransactionTO.getDebitAcctNo())));
                                            } else {
                                                txMap.put(TransferTrans.DR_ACT_NUM, CommonUtil.convertObjToStr(CommonUtil.convertObjToStr(objTransactionTO.getDebitAcctNo())));
                                                txMap.put(TransferTrans.DR_PROD_ID, CommonUtil.convertObjToStr(objTransactionTO.getProductId()));
                                            }
                                            txMap.put(TransferTrans.DR_PROD_TYPE, CommonUtil.convertObjToStr(objTransactionTO.getProductType()));
                                            txMap.put(CommonConstants.USER_ID, logTO.getUserId());
                                            //txMap.put("DR_INSTRUMENT_2", "DEPOSIT_TRANS");
                                            txMap.put("DR_INSTRUMENT_2", objTO.getDepoId());// Added by nithya on 08-05-2019 for KD 451 - 0020678: Indend transaction(purchase and purchase return) issue
                                            txMap.put("DR_INST_TYPE", "WITHDRAW_SLIP");
                                            txMap.put(TransferTrans.DR_BRANCH, _branchCode);
                                            txMap.put(TransferTrans.AUTHORIZE_STATUS_2, "ENTERED_AMOUNT");
                                            if (objTransactionTO.getProductType().equals("OA")) {
                                                txMap.put("TRANS_MOD_TYPE", CommonConstants.TXN_PROD_TYPE_OPERATIVE);
                                            } else if (objTransactionTO.getProductType().equals("AB")) {
                                                txMap.put("TRANS_MOD_TYPE", CommonConstants.TXN_PROD_TYPE_OTHERBANKACTS);
                                            } else if (objTransactionTO.getProductType().equals("SA")) {
                                                txMap.put("TRANS_MOD_TYPE", CommonConstants.TXN_PROD_TYPE_SUSPENSE);
                                            } else if (objTransactionTO.getProductType().equals("TL")) {
                                                txMap.put("TRANS_MOD_TYPE", CommonConstants.TXN_PROD_TYPE_LOANS);
                                            } else if (objTransactionTO.getProductType().equals("AD")) {
                                                txMap.put("TRANS_MOD_TYPE", CommonConstants.TXN_PROD_TYPE_ADVANCES);
                                            } else {
                                                txMap.put("TRANS_MOD_TYPE", CommonConstants.TXN_PROD_TYPE_GL);
                                            }
                                            txMap.put("SCREEN_NAME","Indend Transaction");
                                            txMap.put(TransferTrans.PARTICULARS, objTransactionTO.getParticulars()); 
                                            //added by rishad 26/12/2019 for jira KD-1078
                                            txMap.put("GL_TRANS_ACT_NUM",CommonUtil.convertObjToStr(CommonUtil.convertObjToStr(objTransactionTO.getDebitAcctNo())));
                                            transferList.add(objTransferTrans.getDebitTransferTO(txMap, objTransactionTO.getTransAmt()));
                                            //debitTot = debitTot + objTransactionTO.getTransAmt();
                                            debitTot = calculateTotalAmount(debitTot,objTransactionTO.getTransAmt());
                                        }
                                        //Transfer Credit Transaction....
                                        if (objTO.getTransType() != null && (objTO.getTransType().equals("Sales Return") || objTO.getTransType().equals("Purchase"))) {
                                            txMap = new HashMap();
                                            if (objTransactionTO.getProductType().equals("GL")) {                                            
                                                txMap.put(TransferTrans.CR_AC_HD, CommonUtil.convertObjToStr(CommonUtil.convertObjToStr(objTransactionTO.getDebitAcctNo())));
                                            } else {
                                                txMap.put(TransferTrans.CR_ACT_NUM, CommonUtil.convertObjToStr(CommonUtil.convertObjToStr(objTransactionTO.getDebitAcctNo())));
                                                txMap.put(TransferTrans.CR_PROD_ID, CommonUtil.convertObjToStr(objTransactionTO.getProductId()));
                                            }
                                            txMap.put(TransferTrans.CR_PROD_TYPE, CommonUtil.convertObjToStr(objTransactionTO.getProductType()));
                                            txMap.put(CommonConstants.USER_ID, logTO.getUserId());
                                            //txMap.put("DR_INSTRUMENT_2", "DEPOSIT_TRANS");     
                                            txMap.put("DR_INSTRUMENT_2", objTO.getDepoId());// Added by nithya on 08-05-2019 for KD 451 - 0020678: Indend transaction(purchase and purchase return) issue
                                            txMap.put(TransferTrans.CR_BRANCH, _branchCode);
                                            txMap.put(TransferTrans.AUTHORIZE_STATUS_2, "ENTERED_AMOUNT");
                                            if (objTransactionTO.getProductType().equals("OA")) {
                                                txMap.put("TRANS_MOD_TYPE", CommonConstants.TXN_PROD_TYPE_OPERATIVE);
                                            } else if (objTransactionTO.getProductType().equals("AB")) {
                                                txMap.put("TRANS_MOD_TYPE", CommonConstants.TXN_PROD_TYPE_OTHERBANKACTS);
                                            } else if (objTransactionTO.getProductType().equals("SA")) {
                                                txMap.put("TRANS_MOD_TYPE", CommonConstants.TXN_PROD_TYPE_SUSPENSE);
                                            } else if (objTransactionTO.getProductType().equals("TL")) {
                                                txMap.put("TRANS_MOD_TYPE", CommonConstants.TXN_PROD_TYPE_LOANS);
                                            } else if (objTransactionTO.getProductType().equals("AD")) {
                                                txMap.put("TRANS_MOD_TYPE", CommonConstants.TXN_PROD_TYPE_ADVANCES);
                                            } else {
                                                txMap.put("TRANS_MOD_TYPE", CommonConstants.TXN_PROD_TYPE_GL);
                                            }
                                            txMap.put(TransferTrans.PARTICULARS, objTransactionTO.getParticulars()); 
                                            txMap.put("SCREEN_NAME","Indend Transaction");
                                           //added by rishad 26/12/2019 for jira KD-1078
                                            txMap.put("GL_TRANS_ACT_NUM",CommonUtil.convertObjToStr(CommonUtil.convertObjToStr(objTransactionTO.getDebitAcctNo())));
                                            transferList.add(objTransferTrans.getCreditTransferTO(txMap, objTransactionTO.getTransAmt()));
                                            //creditTot = creditTot + objTransactionTO.getTransAmt();
                                            creditTot = calculateTotalAmount(creditTot,objTransactionTO.getTransAmt());
                                        }
                                        //To Remitt issue...
                                        objTransactionTO.setStatus(CommonConstants.STATUS_CREATED);
                                        objTransactionTO.setBatchId(objTO.getStrIRNo());
                                        objTransactionTO.setBatchDt(currDt);
                                        objTransactionTO.setTransId(objTransferTrans.getLinkBatchId());
                                        objTransactionTO.setBranchId(_branchCode);
                                        System.out.println("objTransactionTO------------------->" + objTransactionTO);
                                        sqlMap.executeUpdate("insertRemittanceIssueTransactionTO", objTransactionTO);
                                    }
                                }
                                System.out.println("transflag##############"+transflag);
                            }
                                if (objTransactionTO.getTransType().equals("TRANSFER")) {
                                    //Indend Sale Transaction.........
                                    if (objTO.getTransType() != null && objTO.getTransType().equals("Sales")) {
                                        if(objTO.getAmount()>0){
                                            txMap = new HashMap();
                                            txMap.put(TransferTrans.CR_AC_HD, acHeads.get("SALES_AC_HD_ID"));                                        
                                            txMap.put(CommonConstants.USER_ID, logTO.getUserId());
                                            //txMap.put("DR_INSTRUMENT_2", "DEPOSIT_TRANS");
                                            txMap.put("DR_INSTRUMENT_2", objTO.getDepoId());// Added by nithya on 08-05-2019 for KD 451 - 0020678: Indend transaction(purchase and purchase return) issue
                                            txMap.put("DR_INST_TYPE", "WITHDRAW_SLIP");
                                            txMap.put(TransferTrans.CR_BRANCH, _branchCode);
                                            txMap.put(TransferTrans.CR_PROD_TYPE, TransactionFactory.GL);
                                            txMap.put(TransferTrans.AUTHORIZE_STATUS_2, "ENTERED_AMOUNT");
                                            txMap.put(TransferTrans.PARTICULARS, "Sale dated : " + saleDate + " ");
                                            txMap.put("TRANS_MOD_TYPE", CommonConstants.TXN_PROD_TYPE_INDEND);
                                            txMap.put("SCREEN_NAME","Indend Transaction");
                                            transferList.add(objTransferTrans.getCreditTransferTO(txMap, objTO.getAmount()));
                                            //creditTot = creditTot + objTO.getAmount();
                                            creditTot = calculateTotalAmount(creditTot,objTO.getAmount());
                                        }
                                        if(objTO.getVatAmt()>0){
                                            txMap = new HashMap();
                                            txMap.put(TransferTrans.CR_AC_HD, acHeadsVat.get("SALE_VAT_AC_HD_ID"));                                        
                                            txMap.put(CommonConstants.USER_ID, logTO.getUserId());
                                            //txMap.put("DR_INSTRUMENT_2", "DEPOSIT_TRANS");
                                            txMap.put("DR_INSTRUMENT_2", objTO.getDepoId());// Added by nithya on 08-05-2019 for KD 451 - 0020678: Indend transaction(purchase and purchase return) issue
                                            txMap.put("DR_INST_TYPE", "WITHDRAW_SLIP");
                                            txMap.put(TransferTrans.CR_BRANCH, _branchCode);
                                            txMap.put(TransferTrans.CR_PROD_TYPE, TransactionFactory.GL);
                                            txMap.put(TransferTrans.AUTHORIZE_STATUS_2, "ENTERED_AMOUNT");
                                            txMap.put(TransferTrans.PARTICULARS, "Sale dated : " + saleDate + " ");
                                            txMap.put("TRANS_MOD_TYPE", CommonConstants.TXN_PROD_TYPE_INDEND);
                                            txMap.put("SCREEN_NAME","Indend Transaction");
                                            transferList.add(objTransferTrans.getCreditTransferTO(txMap, objTO.getVatAmt()));
                                            //creditTot = creditTot + objTO.getVatAmt();
                                            creditTot = calculateTotalAmount(creditTot,objTO.getVatAmt());
                                        }
                                        if(objTO.getMiscAmt()>0){
                                            txMap = new HashMap();
                                            txMap.put(TransferTrans.CR_AC_HD, acHeadsMisc.get("MISC_INCOME_HEAD"));                                        
                                            txMap.put(CommonConstants.USER_ID, logTO.getUserId());
                                            //txMap.put("DR_INSTRUMENT_2", "DEPOSIT_TRANS");
                                            txMap.put("DR_INSTRUMENT_2", objTO.getDepoId());// Added by nithya on 08-05-2019 for KD 451 - 0020678: Indend transaction(purchase and purchase return) issue
                                            txMap.put("DR_INST_TYPE", "WITHDRAW_SLIP");
                                            txMap.put(TransferTrans.CR_BRANCH, _branchCode);
                                            txMap.put(TransferTrans.CR_PROD_TYPE, TransactionFactory.GL);
                                            txMap.put(TransferTrans.AUTHORIZE_STATUS_2, "ENTERED_AMOUNT");
                                            txMap.put(TransferTrans.PARTICULARS, "Sale dated : " + saleDate + " ");
                                            txMap.put("TRANS_MOD_TYPE", CommonConstants.TXN_PROD_TYPE_INDEND);
                                            txMap.put("SCREEN_NAME","Indend Transaction");
                                            transferList.add(objTransferTrans.getCreditTransferTO(txMap, objTO.getMiscAmt()));
                                            //creditTot = creditTot + objTO.getMiscAmt();
                                            creditTot = calculateTotalAmount(creditTot,objTO.getMiscAmt());
                                        }
                                        if(objTO.getOtherIncome()>0){
                                            txMap = new HashMap();
                                            txMap.put(TransferTrans.CR_AC_HD, acHeadsMisc.get("OTHR_INC_AC_HD_ID"));                                        
                                            txMap.put(CommonConstants.USER_ID, logTO.getUserId());
                                            //txMap.put("DR_INSTRUMENT_2", "DEPOSIT_TRANS");
                                            txMap.put("DR_INSTRUMENT_2", objTO.getDepoId());// Added by nithya on 08-05-2019 for KD 451 - 0020678: Indend transaction(purchase and purchase return) issue
                                            txMap.put("DR_INST_TYPE", "WITHDRAW_SLIP");
                                            txMap.put(TransferTrans.CR_BRANCH, _branchCode);
                                            txMap.put(TransferTrans.CR_PROD_TYPE, TransactionFactory.GL);
                                            txMap.put(TransferTrans.AUTHORIZE_STATUS_2, "ENTERED_AMOUNT");
                                            txMap.put(TransferTrans.PARTICULARS, "Sale dated : " + saleDate + " ");
                                            txMap.put("TRANS_MOD_TYPE", CommonConstants.TXN_PROD_TYPE_INDEND);
                                            txMap.put("SCREEN_NAME","Indend Transaction");
                                            transferList.add(objTransferTrans.getCreditTransferTO(txMap, objTO.getOtherIncome()));
                                            //creditTot = creditTot + objTO.getMiscAmt();
                                            creditTot = calculateTotalAmount(creditTot,objTO.getOtherIncome());
                                        }
                                       // Added by nithya on 07-04-2017 for 6161
                                        if(objTO.getOthrExpAmt()>0){
                                            txMap = new HashMap();
                                            txMap.put(TransferTrans.DR_AC_HD, acHeadsOthr.get("OTHER_EXP_HEAD"));                                        
                                            txMap.put(CommonConstants.USER_ID, logTO.getUserId());
                                            //txMap.put("DR_INSTRUMENT_2", "DEPOSIT_TRANS");
                                            txMap.put("DR_INSTRUMENT_2", objTO.getDepoId());// Added by nithya on 08-05-2019 for KD 451 - 0020678: Indend transaction(purchase and purchase return) issue
                                            txMap.put("DR_INST_TYPE", "WITHDRAW_SLIP");
                                            txMap.put(TransferTrans.DR_BRANCH, _branchCode);
                                            txMap.put(TransferTrans.DR_PROD_TYPE, TransactionFactory.GL);
                                            txMap.put(TransferTrans.AUTHORIZE_STATUS_2, "ENTERED_AMOUNT");
                                            txMap.put(TransferTrans.PARTICULARS, particulars);
                                            txMap.put("TRANS_MOD_TYPE", CommonConstants.TXN_PROD_TYPE_INDEND);
                                            txMap.put("SCREEN_NAME","Indend Transaction");
                                            System.out.println("txMap##################OTHER_EXP_HEAD"+txMap);
                                            transferList.add(objTransferTrans.getDebitTransferTO(txMap, objTO.getOthrExpAmt()));
                                            //debitTot = debitTot + objTO.getOthrExpAmt();
                                            debitTot = calculateTotalAmount(debitTot,objTO.getOthrExpAmt());
                                        }
                                        
                                        // Added by nithya on 26-09-2018 for KD 262 - GST Indend Transaction - Needed to implement GST in Indent Transaction
                                        if(objTO.getCgstAmt() >0){
                                            txMap = new HashMap();
                                            txMap.put(TransferTrans.CR_AC_HD, cgstAcHd);                                        
                                            txMap.put(CommonConstants.USER_ID, logTO.getUserId());
                                            //txMap.put("DR_INSTRUMENT_2", "DEPOSIT_TRANS");
                                            txMap.put("DR_INSTRUMENT_2", objTO.getDepoId());// Added by nithya on 08-05-2019 for KD 451 - 0020678: Indend transaction(purchase and purchase return) issue
                                            txMap.put("DR_INST_TYPE", "WITHDRAW_SLIP");
                                            txMap.put(TransferTrans.CR_BRANCH, _branchCode);
                                            txMap.put(TransferTrans.CR_PROD_TYPE, TransactionFactory.GL);
                                            txMap.put(TransferTrans.AUTHORIZE_STATUS_2, "ENTERED_AMOUNT");
                                            txMap.put(TransferTrans.PARTICULARS, "Sale dated : " + saleDate + " ");
                                            txMap.put("TRANS_MOD_TYPE", CommonConstants.TXN_PROD_TYPE_INDEND);
                                            txMap.put("DR_INSTRUMENT_1", "CGST");
                                            txMap.put("SCREEN_NAME","Indend Transaction");
                                            transferList.add(objTransferTrans.getCreditTransferTO(txMap, objTO.getCgstAmt()));
                                            //creditTot = creditTot + objTO.getVatAmt();
                                            creditTot = calculateTotalAmount(creditTot,objTO.getCgstAmt());
                                        }
                                        if(objTO.getSgstAmt() >0){
                                            txMap = new HashMap();
                                            txMap.put(TransferTrans.CR_AC_HD, sgstAcHd);                                        
                                            txMap.put(CommonConstants.USER_ID, logTO.getUserId());
                                            //txMap.put("DR_INSTRUMENT_2", "DEPOSIT_TRANS");
                                            txMap.put("DR_INSTRUMENT_2", objTO.getDepoId());// Added by nithya on 08-05-2019 for KD 451 - 0020678: Indend transaction(purchase and purchase return) issue
                                            txMap.put("DR_INST_TYPE", "WITHDRAW_SLIP");
                                            txMap.put(TransferTrans.CR_BRANCH, _branchCode);
                                            txMap.put(TransferTrans.CR_PROD_TYPE, TransactionFactory.GL);
                                            txMap.put(TransferTrans.AUTHORIZE_STATUS_2, "ENTERED_AMOUNT");
                                            txMap.put(TransferTrans.PARTICULARS, "Sale dated : " + saleDate + " ");
                                            txMap.put("TRANS_MOD_TYPE", CommonConstants.TXN_PROD_TYPE_INDEND);
                                            txMap.put("DR_INSTRUMENT_1", "SGST");
                                            txMap.put("SCREEN_NAME","Indend Transaction");
                                            transferList.add(objTransferTrans.getCreditTransferTO(txMap, objTO.getSgstAmt()));
                                            //creditTot = creditTot + objTO.getVatAmt();
                                            creditTot = calculateTotalAmount(creditTot,objTO.getSgstAmt());
                                        }
                                        // End
                                        
                                          if(objTO.getRoundOffAmt() >0){
                                            txMap = new HashMap();
                                            txMap.put(TransferTrans.CR_AC_HD, roundOffHead);                                        
                                            txMap.put(CommonConstants.USER_ID, logTO.getUserId());
                                            //txMap.put("DR_INSTRUMENT_2", "DEPOSIT_TRANS");
                                            txMap.put("DR_INSTRUMENT_2", objTO.getDepoId());// Added by nithya on 08-05-2019 for KD 451 - 0020678: Indend transaction(purchase and purchase return) issue
                                            txMap.put("DR_INST_TYPE", "WITHDRAW_SLIP");
                                            txMap.put(TransferTrans.CR_BRANCH, _branchCode);
                                            txMap.put(TransferTrans.CR_PROD_TYPE, TransactionFactory.GL);
                                            txMap.put(TransferTrans.AUTHORIZE_STATUS_2, "ENTERED_AMOUNT");
                                            txMap.put(TransferTrans.PARTICULARS, "Sale dated : " + saleDate + " ");
                                            txMap.put("TRANS_MOD_TYPE", CommonConstants.TXN_PROD_TYPE_INDEND);
                                            txMap.put("DR_INSTRUMENT_1", "SGST");
                                            txMap.put("SCREEN_NAME","Indend Transaction");
                                            transferList.add(objTransferTrans.getCreditTransferTO(txMap, objTO.getRoundOffAmt()));
                                            //creditTot = creditTot + objTO.getVatAmt();
                                            creditTot = calculateTotalAmount(creditTot,objTO.getRoundOffAmt());
                                        }
                                        
                                    }
                                    //Indend Purchase Transaction.........    
                                    if (objTO.getTransType() != null && objTO.getTransType().equals("Purchase")) {
                                        if(objTO.getAmount()>0){
                                            txMap = new HashMap();
                                            txMap.put(TransferTrans.DR_AC_HD, acHeads.get("PURCHASE_AC_HD_ID"));                                        
                                            txMap.put(CommonConstants.USER_ID, logTO.getUserId());
                                            //txMap.put("DR_INSTRUMENT_2", "DEPOSIT_TRANS");
                                            txMap.put("DR_INSTRUMENT_2", objTO.getDepoId());// Added by nithya on 08-05-2019 for KD 451 - 0020678: Indend transaction(purchase and purchase return) issue
                                            txMap.put("DR_INST_TYPE", "WITHDRAW_SLIP");
                                            txMap.put(TransferTrans.DR_BRANCH, _branchCode);
                                            txMap.put(TransferTrans.DR_PROD_TYPE, TransactionFactory.GL);
                                            txMap.put(TransferTrans.AUTHORIZE_STATUS_2, "ENTERED_AMOUNT");
                                            txMap.put(TransferTrans.PARTICULARS, particulars); 
                                            txMap.put("TRANS_MOD_TYPE", CommonConstants.TXN_PROD_TYPE_INDEND);
                                            txMap.put("SCREEN_NAME","Indend Transaction");
                                            transferList.add(objTransferTrans.getDebitTransferTO(txMap, objTO.getAmount()));
                                            //debitTot = debitTot + objTO.getAmount();
                                            debitTot = calculateTotalAmount(debitTot,objTO.getAmount());
                                        }
                                        if(objTO.getVatAmt()>0){
                                            txMap = new HashMap();
                                            txMap.put(TransferTrans.DR_AC_HD, acHeadsVat.get("PUR_VAT_AC_HD_ID"));                                        
                                            txMap.put(CommonConstants.USER_ID, logTO.getUserId());
                                            //txMap.put("DR_INSTRUMENT_2", "DEPOSIT_TRANS");
                                            txMap.put("DR_INSTRUMENT_2", objTO.getDepoId());// Added by nithya on 08-05-2019 for KD 451 - 0020678: Indend transaction(purchase and purchase return) issue
                                            txMap.put("DR_INST_TYPE", "WITHDRAW_SLIP");
                                            txMap.put(TransferTrans.DR_BRANCH, _branchCode);
                                            txMap.put(TransferTrans.DR_PROD_TYPE, TransactionFactory.GL);
                                            txMap.put(TransferTrans.AUTHORIZE_STATUS_2, "ENTERED_AMOUNT");
                                            txMap.put(TransferTrans.PARTICULARS, particulars); 
                                            txMap.put("TRANS_MOD_TYPE", CommonConstants.TXN_PROD_TYPE_INDEND);
                                            txMap.put("SCREEN_NAME","Indend Transaction");
                                            transferList.add(objTransferTrans.getDebitTransferTO(txMap, objTO.getVatAmt()));
                                            //debitTot = debitTot + objTO.getVatAmt();
                                            debitTot = calculateTotalAmount(debitTot,objTO.getVatAmt());
                                        }
                                        if(objTO.getOthrExpAmt()>0){
                                            txMap = new HashMap();
                                            txMap.put(TransferTrans.DR_AC_HD, acHeadsOthr.get("OTHER_EXP_HEAD"));                                        
                                            txMap.put(CommonConstants.USER_ID, logTO.getUserId());
                                            //txMap.put("DR_INSTRUMENT_2", "DEPOSIT_TRANS");
                                            txMap.put("DR_INSTRUMENT_2", objTO.getDepoId());// Added by nithya on 08-05-2019 for KD 451 - 0020678: Indend transaction(purchase and purchase return) issue
                                            txMap.put("DR_INST_TYPE", "WITHDRAW_SLIP");
                                            txMap.put(TransferTrans.DR_BRANCH, _branchCode);
                                            txMap.put(TransferTrans.DR_PROD_TYPE, TransactionFactory.GL);
                                            txMap.put(TransferTrans.AUTHORIZE_STATUS_2, "ENTERED_AMOUNT");
                                            txMap.put(TransferTrans.PARTICULARS, particulars);
                                            txMap.put("TRANS_MOD_TYPE", CommonConstants.TXN_PROD_TYPE_INDEND);
                                            txMap.put("SCREEN_NAME","Indend Transaction");
                                            transferList.add(objTransferTrans.getDebitTransferTO(txMap, objTO.getOthrExpAmt()));
                                            //debitTot = debitTot + objTO.getOthrExpAmt();
                                            debitTot = calculateTotalAmount(debitTot,objTO.getOthrExpAmt());
                                        }
                                        if(objTO.getMiscAmt()>0){
                                            txMap = new HashMap();
                                            txMap.put(TransferTrans.CR_AC_HD, acHeadsMisc.get("MISC_INCOME_HEAD"));                                        
                                            txMap.put(CommonConstants.USER_ID, logTO.getUserId());
                                            //txMap.put("DR_INSTRUMENT_2", "DEPOSIT_TRANS");
                                            txMap.put("DR_INSTRUMENT_2", objTO.getDepoId());// Added by nithya on 08-05-2019 for KD 451 - 0020678: Indend transaction(purchase and purchase return) issue
                                            txMap.put("DR_INST_TYPE", "WITHDRAW_SLIP");
                                            txMap.put(TransferTrans.CR_BRANCH, _branchCode);
                                            txMap.put(TransferTrans.CR_PROD_TYPE, TransactionFactory.GL);
                                            txMap.put(TransferTrans.AUTHORIZE_STATUS_2, "ENTERED_AMOUNT");
                                            txMap.put(TransferTrans.PARTICULARS, particulars);
                                            txMap.put("TRANS_MOD_TYPE", CommonConstants.TXN_PROD_TYPE_INDEND);
                                            txMap.put("SCREEN_NAME","Indend Transaction");
                                            transferList.add(objTransferTrans.getCreditTransferTO(txMap, objTO.getMiscAmt()));
                                            //creditTot = creditTot + objTO.getMiscAmt();
                                            creditTot = calculateTotalAmount(creditTot,objTO.getMiscAmt());
                                        }
                                        if(objTO.getOtherIncome()>0){
                                            txMap = new HashMap();
                                            txMap.put(TransferTrans.CR_AC_HD, acHeadsMisc.get("OTHR_INC_AC_HD_ID"));                                        
                                            txMap.put(CommonConstants.USER_ID, logTO.getUserId());
                                            //txMap.put("DR_INSTRUMENT_2", "DEPOSIT_TRANS");
                                            txMap.put("DR_INSTRUMENT_2", objTO.getDepoId());// Added by nithya on 08-05-2019 for KD 451 - 0020678: Indend transaction(purchase and purchase return) issue
                                            txMap.put("DR_INST_TYPE", "WITHDRAW_SLIP");
                                            txMap.put(TransferTrans.CR_BRANCH, _branchCode);
                                            txMap.put(TransferTrans.CR_PROD_TYPE, TransactionFactory.GL);
                                            txMap.put(TransferTrans.AUTHORIZE_STATUS_2, "ENTERED_AMOUNT");
                                            txMap.put(TransferTrans.PARTICULARS, particulars);
                                            txMap.put("TRANS_MOD_TYPE", CommonConstants.TXN_PROD_TYPE_INDEND);
                                            txMap.put("SCREEN_NAME","Indend Transaction");
                                            transferList.add(objTransferTrans.getCreditTransferTO(txMap, objTO.getOtherIncome()));
                                            //creditTot = creditTot + objTO.getMiscAmt();
                                            creditTot = calculateTotalAmount(creditTot,objTO.getOtherIncome());
                                        }
                                        if(objTO.getCommRecvdAmt()>0){
                                            txMap = new HashMap();
                                            txMap.put(TransferTrans.CR_AC_HD, acHeadsComm.get("COMM_RECVD_HEAD"));                                        
                                            txMap.put(CommonConstants.USER_ID, logTO.getUserId());
                                            //txMap.put("DR_INSTRUMENT_2", "DEPOSIT_TRANS");
                                            txMap.put("DR_INSTRUMENT_2", objTO.getDepoId());// Added by nithya on 08-05-2019 for KD 451 - 0020678: Indend transaction(purchase and purchase return) issue
                                            txMap.put("DR_INST_TYPE", "WITHDRAW_SLIP");
                                            txMap.put(TransferTrans.CR_BRANCH, _branchCode);
                                            txMap.put(TransferTrans.CR_PROD_TYPE, TransactionFactory.GL);
                                            txMap.put(TransferTrans.PARTICULARS, particulars);
                                            txMap.put(TransferTrans.AUTHORIZE_STATUS_2, "ENTERED_AMOUNT");
                                            txMap.put("TRANS_MOD_TYPE", CommonConstants.TXN_PROD_TYPE_INDEND);
                                            txMap.put("SCREEN_NAME","Indend Transaction");
                                            transferList.add(objTransferTrans.getCreditTransferTO(txMap, objTO.getCommRecvdAmt()));
                                            //creditTot = creditTot + objTO.getCommRecvdAmt();
                                            creditTot = calculateTotalAmount(creditTot,objTO.getCommRecvdAmt());
                                        }
                                        
                                        // Added by nithya on 27-09-2018 for KD 262 - GST Indend Transaction - Needed to implement GST in Indent Transaction
                                        if(objTO.getCgstAmt() > 0){
                                            txMap = new HashMap();
                                            txMap.put(TransferTrans.DR_AC_HD, cgstAcHd);                                        
                                            txMap.put(CommonConstants.USER_ID, logTO.getUserId());
                                            //txMap.put("DR_INSTRUMENT_2", "DEPOSIT_TRANS");
                                            txMap.put("DR_INSTRUMENT_2", objTO.getDepoId());// Added by nithya on 08-05-2019 for KD 451 - 0020678: Indend transaction(purchase and purchase return) issue
                                            txMap.put("DR_INST_TYPE", "WITHDRAW_SLIP");
                                            txMap.put(TransferTrans.DR_BRANCH, _branchCode);
                                            txMap.put(TransferTrans.DR_PROD_TYPE, TransactionFactory.GL);
                                            txMap.put(TransferTrans.AUTHORIZE_STATUS_2, "ENTERED_AMOUNT");
                                            txMap.put(TransferTrans.PARTICULARS, particulars); 
                                            txMap.put("TRANS_MOD_TYPE", CommonConstants.TXN_PROD_TYPE_INDEND);
                                            txMap.put("DR_INSTRUMENT_1", "CGST");
                                            txMap.put("SCREEN_NAME","Indend Transaction");
                                            transferList.add(objTransferTrans.getDebitTransferTO(txMap, objTO.getCgstAmt()));
                                            //debitTot = debitTot + objTO.getVatAmt();
                                            debitTot = calculateTotalAmount(debitTot,objTO.getCgstAmt());
                                        }
                                        if(objTO.getSgstAmt() > 0){
                                            txMap = new HashMap();
                                            txMap.put(TransferTrans.DR_AC_HD, sgstAcHd);                                        
                                            txMap.put(CommonConstants.USER_ID, logTO.getUserId());
                                            //txMap.put("DR_INSTRUMENT_2", "DEPOSIT_TRANS");
                                            txMap.put("DR_INSTRUMENT_2", objTO.getDepoId());// Added by nithya on 08-05-2019 for KD 451 - 0020678: Indend transaction(purchase and purchase return) issue
                                            txMap.put("DR_INST_TYPE", "WITHDRAW_SLIP");
                                            txMap.put(TransferTrans.DR_BRANCH, _branchCode);
                                            txMap.put(TransferTrans.DR_PROD_TYPE, TransactionFactory.GL);
                                            txMap.put(TransferTrans.AUTHORIZE_STATUS_2, "ENTERED_AMOUNT");
                                            txMap.put(TransferTrans.PARTICULARS, particulars); 
                                            txMap.put("TRANS_MOD_TYPE", CommonConstants.TXN_PROD_TYPE_INDEND);
                                            txMap.put("DR_INSTRUMENT_1", "SGST");
                                            txMap.put("SCREEN_NAME","Indend Transaction");
                                            transferList.add(objTransferTrans.getDebitTransferTO(txMap, objTO.getSgstAmt()));
                                            //debitTot = debitTot + objTO.getVatAmt();
                                            debitTot = calculateTotalAmount(debitTot,objTO.getSgstAmt());
                                        }                                     
                                        
                                        if(objTO.getRoundOffAmt() > 0){
                                            txMap = new HashMap();
                                            txMap.put(TransferTrans.DR_AC_HD, roundOffHead);                                        
                                            txMap.put(CommonConstants.USER_ID, logTO.getUserId());
                                            //txMap.put("DR_INSTRUMENT_2", "DEPOSIT_TRANS");
                                            txMap.put("DR_INSTRUMENT_2", objTO.getDepoId());// Added by nithya on 08-05-2019 for KD 451 - 0020678: Indend transaction(purchase and purchase return) issue
                                            txMap.put("DR_INST_TYPE", "WITHDRAW_SLIP");
                                            txMap.put(TransferTrans.DR_BRANCH, _branchCode);
                                            txMap.put(TransferTrans.DR_PROD_TYPE, TransactionFactory.GL);
                                            txMap.put(TransferTrans.AUTHORIZE_STATUS_2, "ENTERED_AMOUNT");
                                            txMap.put(TransferTrans.PARTICULARS, particulars); 
                                            txMap.put("TRANS_MOD_TYPE", CommonConstants.TXN_PROD_TYPE_INDEND);
                                            txMap.put("DR_INSTRUMENT_1", "SGST");
                                            txMap.put("SCREEN_NAME","Indend Transaction");
                                            transferList.add(objTransferTrans.getDebitTransferTO(txMap, objTO.getRoundOffAmt()));
                                            //debitTot = debitTot + objTO.getVatAmt();
                                            debitTot = calculateTotalAmount(debitTot,objTO.getRoundOffAmt());
                                        } 
                                        
                                        // End
                                    } 
                                    //Indend Purchase Return Transaction.........    
                                    if (objTO.getTransType() != null && objTO.getTransType().equals("Purchase Return")) {
                                        if(objTO.getAmount()>0){
                                            txMap = new HashMap();
                                            txMap.put(TransferTrans.CR_AC_HD, acHeads.get("PUR_RET_AC_HD_ID"));                                        
                                            txMap.put(CommonConstants.USER_ID, logTO.getUserId());
                                            //txMap.put("DR_INSTRUMENT_2", "DEPOSIT_TRANS");
                                            txMap.put("DR_INSTRUMENT_2", objTO.getDepoId());// Added by nithya on 08-05-2019 for KD 451 - 0020678: Indend transaction(purchase and purchase return) issue
                                            txMap.put("DR_INST_TYPE", "WITHDRAW_SLIP");
                                            txMap.put(TransferTrans.CR_BRANCH, _branchCode);
                                            txMap.put(TransferTrans.CR_PROD_TYPE, TransactionFactory.GL);
                                            txMap.put(TransferTrans.PARTICULARS, particulars);
                                            txMap.put("TRANS_MOD_TYPE", CommonConstants.TXN_PROD_TYPE_INDEND);
                                            txMap.put("SCREEN_NAME","Indend Transaction");
                                            transferList.add(objTransferTrans.getCreditTransferTO(txMap, objTO.getAmount()));
                                            //creditTot = creditTot + objTO.getAmount();
                                            creditTot = calculateTotalAmount(creditTot,objTO.getAmount());
                                        }
                                        if(objTO.getVatAmt()>0){
                                            txMap = new HashMap();
                                            txMap.put(TransferTrans.CR_AC_HD, acHeadsVat.get("PUR_RTN_VAT_AC_HD_ID"));                                        
                                            txMap.put(CommonConstants.USER_ID, logTO.getUserId());
                                            //txMap.put("DR_INSTRUMENT_2", "DEPOSIT_TRANS");
                                            txMap.put("DR_INSTRUMENT_2", objTO.getDepoId());// Added by nithya on 08-05-2019 for KD 451 - 0020678: Indend transaction(purchase and purchase return) issue
                                            txMap.put("DR_INST_TYPE", "WITHDRAW_SLIP");
                                            txMap.put(TransferTrans.CR_BRANCH, _branchCode);
                                            txMap.put(TransferTrans.CR_PROD_TYPE, TransactionFactory.GL);
                                            txMap.put(TransferTrans.PARTICULARS, particulars);
                                            txMap.put("TRANS_MOD_TYPE", CommonConstants.TXN_PROD_TYPE_INDEND);
                                            txMap.put("SCREEN_NAME","Indend Transaction");
                                            transferList.add(objTransferTrans.getCreditTransferTO(txMap, objTO.getVatAmt()));
                                            //creditTot = creditTot + objTO.getVatAmt();
                                            creditTot = calculateTotalAmount(creditTot,objTO.getVatAmt());
                                        }
                                        if(objTO.getMiscAmt()>0){
                                            txMap = new HashMap();
                                            txMap.put(TransferTrans.CR_AC_HD, acHeadsMisc.get("MISC_INCOME_HEAD"));                                        
                                            txMap.put(CommonConstants.USER_ID, logTO.getUserId());
                                            //txMap.put("DR_INSTRUMENT_2", "DEPOSIT_TRANS");
                                            txMap.put("DR_INSTRUMENT_2", objTO.getDepoId());// Added by nithya on 08-05-2019 for KD 451 - 0020678: Indend transaction(purchase and purchase return) issue
                                            txMap.put("DR_INST_TYPE", "WITHDRAW_SLIP");
                                            txMap.put(TransferTrans.CR_BRANCH, _branchCode);
                                            txMap.put(TransferTrans.CR_PROD_TYPE, TransactionFactory.GL);
                                            txMap.put(TransferTrans.PARTICULARS, particulars);
                                            txMap.put("TRANS_MOD_TYPE", CommonConstants.TXN_PROD_TYPE_INDEND);
                                            txMap.put("SCREEN_NAME","Indend Transaction");
                                            transferList.add(objTransferTrans.getCreditTransferTO(txMap, objTO.getMiscAmt()));
                                            //creditTot = creditTot + objTO.getMiscAmt();
                                            creditTot = calculateTotalAmount(creditTot,objTO.getMiscAmt());
                                        }
                                        if(objTO.getOtherIncome()>0){
                                            txMap = new HashMap();
                                            txMap.put(TransferTrans.CR_AC_HD, acHeadsMisc.get("OTHR_INC_AC_HD_ID"));                                        
                                            txMap.put(CommonConstants.USER_ID, logTO.getUserId());
                                            //txMap.put("DR_INSTRUMENT_2", "DEPOSIT_TRANS");
                                            txMap.put("DR_INSTRUMENT_2", objTO.getDepoId());// Added by nithya on 08-05-2019 for KD 451 - 0020678: Indend transaction(purchase and purchase return) issue
                                            txMap.put("DR_INST_TYPE", "WITHDRAW_SLIP");
                                            txMap.put(TransferTrans.CR_BRANCH, _branchCode);
                                            txMap.put(TransferTrans.CR_PROD_TYPE, TransactionFactory.GL);
                                            txMap.put(TransferTrans.PARTICULARS, particulars);
                                            txMap.put("TRANS_MOD_TYPE", CommonConstants.TXN_PROD_TYPE_INDEND);
                                            txMap.put("SCREEN_NAME","Indend Transaction");
                                            transferList.add(objTransferTrans.getCreditTransferTO(txMap, objTO.getOtherIncome()));
                                            //creditTot = creditTot + objTO.getMiscAmt();
                                            creditTot = calculateTotalAmount(creditTot,objTO.getOtherIncome());
                                        }     
                                        // Added by nithya on 05-02-2018 for 0009023: Requirement in indend
                                        if(objTO.getOthrExpAmt()>0){
                                            txMap = new HashMap();
                                            txMap.put(TransferTrans.DR_AC_HD, acHeadsOthr.get("OTHER_EXP_HEAD"));                                        
                                            txMap.put(CommonConstants.USER_ID, logTO.getUserId());
                                            //txMap.put("DR_INSTRUMENT_2", "DEPOSIT_TRANS");
                                            txMap.put("DR_INSTRUMENT_2", objTO.getDepoId());// Added by nithya on 08-05-2019 for KD 451 - 0020678: Indend transaction(purchase and purchase return) issue
                                            txMap.put("DR_INST_TYPE", "WITHDRAW_SLIP");
                                            txMap.put(TransferTrans.DR_BRANCH, _branchCode);
                                            txMap.put(TransferTrans.DR_PROD_TYPE, TransactionFactory.GL);
                                            txMap.put(TransferTrans.AUTHORIZE_STATUS_2, "ENTERED_AMOUNT");
                                            txMap.put(TransferTrans.PARTICULARS, particulars);
                                            txMap.put("TRANS_MOD_TYPE", CommonConstants.TXN_PROD_TYPE_INDEND);
                                            txMap.put("SCREEN_NAME","Indend Transaction");
                                            transferList.add(objTransferTrans.getDebitTransferTO(txMap, objTO.getOthrExpAmt()));
                                            //debitTot = debitTot + objTO.getOthrExpAmt();
                                            debitTot = calculateTotalAmount(debitTot,objTO.getOthrExpAmt());
                                        }
                                        
                                        // Added by nithya on 27-09-2018 for KD 262 - GST Indend Transaction - Needed to implement GST in Indent Transaction
                                        if(objTO.getCgstAmt() > 0){
                                            txMap = new HashMap();
                                            txMap.put(TransferTrans.CR_AC_HD, cgstAcHd);                                        
                                            txMap.put(CommonConstants.USER_ID, logTO.getUserId());
                                            //txMap.put("DR_INSTRUMENT_2", "DEPOSIT_TRANS");
                                            txMap.put("DR_INSTRUMENT_2", objTO.getDepoId());// Added by nithya on 08-05-2019 for KD 451 - 0020678: Indend transaction(purchase and purchase return) issue
                                            txMap.put("DR_INST_TYPE", "WITHDRAW_SLIP");
                                            txMap.put(TransferTrans.CR_BRANCH, _branchCode);
                                            txMap.put(TransferTrans.CR_PROD_TYPE, TransactionFactory.GL);
                                            txMap.put(TransferTrans.PARTICULARS, particulars);
                                            txMap.put("TRANS_MOD_TYPE", CommonConstants.TXN_PROD_TYPE_INDEND);
                                            txMap.put("DR_INSTRUMENT_1", "CGST");
                                            txMap.put("SCREEN_NAME","Indend Transaction");
                                            transferList.add(objTransferTrans.getCreditTransferTO(txMap, objTO.getCgstAmt()));
                                            //creditTot = creditTot + objTO.getVatAmt();
                                            creditTot = calculateTotalAmount(creditTot,objTO.getCgstAmt());
                                        }                                        
                                        if(objTO.getSgstAmt() > 0){
                                            txMap = new HashMap();
                                            txMap.put(TransferTrans.CR_AC_HD, sgstAcHd);                                        
                                            txMap.put(CommonConstants.USER_ID, logTO.getUserId());
                                            //txMap.put("DR_INSTRUMENT_2", "DEPOSIT_TRANS");
                                            txMap.put("DR_INSTRUMENT_2", objTO.getDepoId());// Added by nithya on 08-05-2019 for KD 451 - 0020678: Indend transaction(purchase and purchase return) issue
                                            txMap.put("DR_INST_TYPE", "WITHDRAW_SLIP");
                                            txMap.put(TransferTrans.CR_BRANCH, _branchCode);
                                            txMap.put(TransferTrans.CR_PROD_TYPE, TransactionFactory.GL);
                                            txMap.put(TransferTrans.PARTICULARS, particulars);
                                            txMap.put("TRANS_MOD_TYPE", CommonConstants.TXN_PROD_TYPE_INDEND);
                                            txMap.put("DR_INSTRUMENT_1", "SGST");
                                            txMap.put("SCREEN_NAME","Indend Transaction");
                                            transferList.add(objTransferTrans.getCreditTransferTO(txMap, objTO.getSgstAmt()));
                                            //creditTot = creditTot + objTO.getVatAmt();
                                            creditTot = calculateTotalAmount(creditTot,objTO.getSgstAmt());
                                        }                                       
                                        
                                         if(objTO.getRoundOffAmt()> 0){
                                            txMap = new HashMap();
                                            txMap.put(TransferTrans.CR_AC_HD, roundOffHead);                                        
                                            txMap.put(CommonConstants.USER_ID, logTO.getUserId());
                                            //txMap.put("DR_INSTRUMENT_2", "DEPOSIT_TRANS");
                                            txMap.put("DR_INSTRUMENT_2", objTO.getDepoId());// Added by nithya on 08-05-2019 for KD 451 - 0020678: Indend transaction(purchase and purchase return) issue
                                            txMap.put("DR_INST_TYPE", "WITHDRAW_SLIP");
                                            txMap.put(TransferTrans.CR_BRANCH, _branchCode);
                                            txMap.put(TransferTrans.CR_PROD_TYPE, TransactionFactory.GL);
                                            txMap.put(TransferTrans.PARTICULARS, particulars);
                                            txMap.put("TRANS_MOD_TYPE", CommonConstants.TXN_PROD_TYPE_INDEND);
                                            txMap.put("DR_INSTRUMENT_1", "SGST");
                                            txMap.put("SCREEN_NAME","Indend Transaction");
                                            transferList.add(objTransferTrans.getCreditTransferTO(txMap, objTO.getRoundOffAmt()));
                                            //creditTot = creditTot + objTO.getVatAmt();
                                            creditTot = calculateTotalAmount(creditTot,objTO.getRoundOffAmt());
                                        } 
                                        
                                        
                                        
                                        // End                                       
                                    }
                                    //Indend Sales Return Transaction.........    
                                   if (objTO.getTransType() != null && objTO.getTransType().equals("Sales Return")) {
                                        if(objTO.getAmount()>0){
                                            txMap = new HashMap();
                                            txMap.put(TransferTrans.DR_AC_HD, acHeads.get("SALES_RET_AC_HD_ID"));                                        
                                            txMap.put(CommonConstants.USER_ID, logTO.getUserId());
                                            //txMap.put("DR_INSTRUMENT_2", "DEPOSIT_TRANS");
                                            txMap.put("DR_INSTRUMENT_2", objTO.getDepoId());// Added by nithya on 08-05-2019 for KD 451 - 0020678: Indend transaction(purchase and purchase return) issue
                                            txMap.put("DR_INST_TYPE", "WITHDRAW_SLIP");
                                            txMap.put(TransferTrans.DR_BRANCH, _branchCode);
                                            txMap.put(TransferTrans.PARTICULARS, "Sale dated : " + saleDate + " ");
                                            txMap.put(TransferTrans.DR_PROD_TYPE, TransactionFactory.GL);       
                                            txMap.put("TRANS_MOD_TYPE", CommonConstants.TXN_PROD_TYPE_INDEND);
                                            txMap.put("SCREEN_NAME","Indend Transaction");
                                            transferList.add(objTransferTrans.getDebitTransferTO(txMap, objTO.getAmount()));
                                            //debitTot = debitTot + objTO.getAmount();
                                            debitTot = calculateTotalAmount(debitTot,objTO.getAmount());
                                        }
                                        if(objTO.getVatAmt()>0){
                                            txMap = new HashMap();
                                            txMap.put(TransferTrans.DR_AC_HD, acHeadsVat.get("SALE_RTN_VAT_AC_HD_ID"));                                        
                                            txMap.put(CommonConstants.USER_ID, logTO.getUserId());
                                            //txMap.put("DR_INSTRUMENT_2", "DEPOSIT_TRANS");
                                            txMap.put("DR_INSTRUMENT_2", objTO.getDepoId());// Added by nithya on 08-05-2019 for KD 451 - 0020678: Indend transaction(purchase and purchase return) issue
                                            txMap.put("DR_INST_TYPE", "WITHDRAW_SLIP");
                                            txMap.put(TransferTrans.DR_BRANCH, _branchCode);
                                            txMap.put(TransferTrans.DR_PROD_TYPE, TransactionFactory.GL);
                                            txMap.put(TransferTrans.PARTICULARS, "Sale dated : " + saleDate + " ");
                                            txMap.put("TRANS_MOD_TYPE", CommonConstants.TXN_PROD_TYPE_INDEND);
                                            txMap.put("SCREEN_NAME","Indend Transaction");
                                            transferList.add(objTransferTrans.getDebitTransferTO(txMap, objTO.getVatAmt()));
                                            //debitTot = debitTot + objTO.getVatAmt();
                                            debitTot = calculateTotalAmount(debitTot,objTO.getVatAmt());
                                        }
                                        if(objTO.getOthrExpAmt()>0){
                                            txMap = new HashMap();
                                            txMap.put(TransferTrans.DR_AC_HD, acHeadsOthr.get("OTHER_EXP_HEAD"));                                        
                                            txMap.put(CommonConstants.USER_ID, logTO.getUserId());
                                            //txMap.put("DR_INSTRUMENT_2", "DEPOSIT_TRANS");
                                            txMap.put("DR_INSTRUMENT_2", objTO.getDepoId());// Added by nithya on 08-05-2019 for KD 451 - 0020678: Indend transaction(purchase and purchase return) issue
                                            txMap.put("DR_INST_TYPE", "WITHDRAW_SLIP");
                                            txMap.put(TransferTrans.DR_BRANCH, _branchCode);
                                            txMap.put(TransferTrans.DR_PROD_TYPE, TransactionFactory.GL);
                                            txMap.put(TransferTrans.PARTICULARS, "Sale dated : " + saleDate + " ");
                                            txMap.put("TRANS_MOD_TYPE", CommonConstants.TXN_PROD_TYPE_INDEND);
                                            txMap.put("SCREEN_NAME","Indend Transaction");
                                            transferList.add(objTransferTrans.getDebitTransferTO(txMap, objTO.getOthrExpAmt()));
                                            //debitTot = debitTot + objTO.getOthrExpAmt();
                                            debitTot = calculateTotalAmount(debitTot,objTO.getOthrExpAmt());
                                        }
                                        if(objTO.getMiscAmt()>0){
                                            txMap = new HashMap();
                                            txMap.put(TransferTrans.CR_AC_HD, acHeadsMisc.get("MISC_INCOME_HEAD"));                                        
                                            txMap.put(CommonConstants.USER_ID, logTO.getUserId());
                                            //txMap.put("DR_INSTRUMENT_2", "DEPOSIT_TRANS");
                                            txMap.put("DR_INSTRUMENT_2", objTO.getDepoId());// Added by nithya on 08-05-2019 for KD 451 - 0020678: Indend transaction(purchase and purchase return) issue
                                            txMap.put("DR_INST_TYPE", "WITHDRAW_SLIP");
                                            txMap.put(TransferTrans.CR_BRANCH, _branchCode);
                                            txMap.put(TransferTrans.CR_PROD_TYPE, TransactionFactory.GL);
                                            txMap.put(TransferTrans.PARTICULARS, "Sale dated : " + saleDate + " ");
                                            txMap.put("TRANS_MOD_TYPE", CommonConstants.TXN_PROD_TYPE_INDEND);
                                            txMap.put("SCREEN_NAME","Indend Transaction");
//                                            transferList.add(objTransferTrans.getDebitTransferTO(txMap, objTO.getMiscAmt()));
//                                            //debitTot = debitTot + objTO.getMiscAmt();
//                                            debitTot = calculateTotalAmount(debitTot,objTO.getMiscAmt());
                                            transferList.add(objTransferTrans.getCreditTransferTO(txMap, objTO.getMiscAmt())); //KD-3423
                                            creditTot = calculateTotalAmount(creditTot,objTO.getMiscAmt());
                                        }
                                        if(objTO.getOtherIncome()>0){
                                            txMap = new HashMap();
                                            txMap.put(TransferTrans.CR_AC_HD, acHeadsMisc.get("OTHR_INC_AC_HD_ID"));                                        
                                            txMap.put(CommonConstants.USER_ID, logTO.getUserId());
                                            //txMap.put("DR_INSTRUMENT_2", "DEPOSIT_TRANS");
                                            txMap.put("DR_INSTRUMENT_2", objTO.getDepoId());// Added by nithya on 08-05-2019 for KD 451 - 0020678: Indend transaction(purchase and purchase return) issue
                                            txMap.put("DR_INST_TYPE", "WITHDRAW_SLIP");
                                            txMap.put(TransferTrans.CR_BRANCH, _branchCode);
                                            txMap.put(TransferTrans.CR_PROD_TYPE, TransactionFactory.GL);
                                            txMap.put(TransferTrans.PARTICULARS, "Sale dated : " + saleDate + " ");
                                            txMap.put("TRANS_MOD_TYPE", CommonConstants.TXN_PROD_TYPE_INDEND);
                                            txMap.put("SCREEN_NAME","Indend Transaction");
                                            transferList.add(objTransferTrans.getDebitTransferTO(txMap, objTO.getOtherIncome()));
                                            //debitTot = debitTot + objTO.getMiscAmt();
                                            debitTot = calculateTotalAmount(debitTot,objTO.getOtherIncome());
                                        }
                                        if(objTO.getCommRecvdAmt()>0){
                                            txMap = new HashMap();
                                            txMap.put(TransferTrans.CR_AC_HD, acHeadsComm.get("COMM_RECVD_HEAD"));                                        
                                            txMap.put(CommonConstants.USER_ID, logTO.getUserId());
                                            //txMap.put("DR_INSTRUMENT_2", "DEPOSIT_TRANS");
                                            txMap.put("DR_INSTRUMENT_2", objTO.getDepoId());// Added by nithya on 08-05-2019 for KD 451 - 0020678: Indend transaction(purchase and purchase return) issue
                                            txMap.put("DR_INST_TYPE", "WITHDRAW_SLIP");
                                            txMap.put(TransferTrans.CR_BRANCH, _branchCode);
                                            txMap.put(TransferTrans.CR_PROD_TYPE, TransactionFactory.GL);
                                            txMap.put(TransferTrans.PARTICULARS, "Sale dated : " + saleDate + " ");
                                            txMap.put("TRANS_MOD_TYPE", CommonConstants.TXN_PROD_TYPE_INDEND);
                                            txMap.put("SCREEN_NAME","Indend Transaction");
                                            transferList.add(objTransferTrans.getDebitTransferTO(txMap, objTO.getCommRecvdAmt()));
                                            //debitTot = debitTot + objTO.getCommRecvdAmt();
                                            debitTot = calculateTotalAmount(debitTot, objTO.getCommRecvdAmt());
                                        }
                                        // Added by nithya on 27-09-2018 for KD 262 - GST Indend Transaction - Needed to implement GST in Indent Transaction
                                        if(objTO.getCgstAmt() > 0){
                                            txMap = new HashMap();
                                            txMap.put(TransferTrans.DR_AC_HD, cgstAcHd);                                        
                                            txMap.put(CommonConstants.USER_ID, logTO.getUserId());
                                            //txMap.put("DR_INSTRUMENT_2", "DEPOSIT_TRANS");
                                            txMap.put("DR_INSTRUMENT_2", objTO.getDepoId());// Added by nithya on 08-05-2019 for KD 451 - 0020678: Indend transaction(purchase and purchase return) issue
                                            txMap.put("DR_INST_TYPE", "WITHDRAW_SLIP");
                                            txMap.put(TransferTrans.DR_BRANCH, _branchCode);
                                            txMap.put(TransferTrans.DR_PROD_TYPE, TransactionFactory.GL);
                                            txMap.put(TransferTrans.PARTICULARS, "Sale dated : " + saleDate + " ");
                                            txMap.put("TRANS_MOD_TYPE", CommonConstants.TXN_PROD_TYPE_INDEND);
                                            txMap.put("DR_INSTRUMENT_1", "CGST");
                                            txMap.put("SCREEN_NAME","Indend Transaction");
                                            transferList.add(objTransferTrans.getDebitTransferTO(txMap, objTO.getCgstAmt()));
                                            //debitTot = debitTot + objTO.getVatAmt();
                                            debitTot = calculateTotalAmount(debitTot,objTO.getCgstAmt());
                                        }
                                        if(objTO.getSgstAmt() > 0){
                                            txMap = new HashMap();
                                            txMap.put(TransferTrans.DR_AC_HD, sgstAcHd);                                        
                                            txMap.put(CommonConstants.USER_ID, logTO.getUserId());
                                            //txMap.put("DR_INSTRUMENT_2", "DEPOSIT_TRANS");
                                            txMap.put("DR_INSTRUMENT_2", objTO.getDepoId());// Added by nithya on 08-05-2019 for KD 451 - 0020678: Indend transaction(purchase and purchase return) issue
                                            txMap.put("DR_INST_TYPE", "WITHDRAW_SLIP");
                                            txMap.put(TransferTrans.DR_BRANCH, _branchCode);
                                            txMap.put(TransferTrans.DR_PROD_TYPE, TransactionFactory.GL);
                                            txMap.put(TransferTrans.PARTICULARS, "Sale dated : " + saleDate + " ");
                                            txMap.put("TRANS_MOD_TYPE", CommonConstants.TXN_PROD_TYPE_INDEND);
                                            txMap.put("DR_INSTRUMENT_1", "SGST");
                                            txMap.put("SCREEN_NAME","Indend Transaction");
                                            transferList.add(objTransferTrans.getDebitTransferTO(txMap, objTO.getSgstAmt()));
                                            //debitTot = debitTot + objTO.getVatAmt();
                                            debitTot = calculateTotalAmount(debitTot,objTO.getSgstAmt());
                                        }
                                        
                                         if(objTO.getRoundOffAmt() > 0){
                                            txMap = new HashMap();
                                            txMap.put(TransferTrans.DR_AC_HD, roundOffHead);                                        
                                            txMap.put(CommonConstants.USER_ID, logTO.getUserId());
                                            //txMap.put("DR_INSTRUMENT_2", "DEPOSIT_TRANS");
                                            txMap.put("DR_INSTRUMENT_2", objTO.getDepoId());// Added by nithya on 08-05-2019 for KD 451 - 0020678: Indend transaction(purchase and purchase return) issue
                                            txMap.put("DR_INST_TYPE", "WITHDRAW_SLIP");
                                            txMap.put(TransferTrans.DR_BRANCH, _branchCode);
                                            txMap.put(TransferTrans.DR_PROD_TYPE, TransactionFactory.GL);
                                            txMap.put(TransferTrans.PARTICULARS, "Sale dated : " + saleDate + " ");
                                            txMap.put("TRANS_MOD_TYPE", CommonConstants.TXN_PROD_TYPE_INDEND);
                                            txMap.put("DR_INSTRUMENT_1", "SGST");
                                            txMap.put("SCREEN_NAME","Indend Transaction");
                                            transferList.add(objTransferTrans.getDebitTransferTO(txMap, objTO.getRoundOffAmt()));
                                            //debitTot = debitTot + objTO.getVatAmt();
                                            debitTot = calculateTotalAmount(debitTot,objTO.getRoundOffAmt());
                                        }
                                        
                                        // End
                                    } 
                                    //Indend Damage ransaction......... 
                                    if (objTO.getTransType() != null && objTO.getTransType().equals("Damage")) {
                                        if(objTO.getAmount()>0){
                                            txMap = new HashMap();
                                            txMap.put(TransferTrans.CR_AC_HD, acHeads.get("DAMAGE_AC_HD_ID"));                                        
                                            txMap.put(CommonConstants.USER_ID, logTO.getUserId());
                                            //txMap.put("DR_INSTRUMENT_2", "DEPOSIT_TRANS");
                                            txMap.put("DR_INSTRUMENT_2", objTO.getDepoId());// Added by nithya on 08-05-2019 for KD 451 - 0020678: Indend transaction(purchase and purchase return) issue
                                            txMap.put("DR_INST_TYPE", "WITHDRAW_SLIP");
                                            txMap.put(TransferTrans.CR_BRANCH, _branchCode);
                                            txMap.put(TransferTrans.CR_PROD_TYPE, TransactionFactory.GL);
                                            txMap.put(TransferTrans.PARTICULARS, "To " + objTO.getNarration() + " ");
                                            txMap.put("TRANS_MOD_TYPE", CommonConstants.TXN_PROD_TYPE_INDEND);
                                            txMap.put("SCREEN_NAME","Indend Transaction");
                                            transferList.add(objTransferTrans.getCreditTransferTO(txMap, objTO.getAmount()));
                                            //creditTot = creditTot + objTO.getAmount();
                                            creditTot = calculateTotalAmount(creditTot, objTO.getAmount());
                                        }
                                        if(objTO.getVatAmt()>0){
                                            txMap = new HashMap();
                                            txMap.put(TransferTrans.CR_AC_HD, acHeadsVat.get("DAM_VAT_AC_HD_ID"));                                        
                                            txMap.put(CommonConstants.USER_ID, logTO.getUserId());
                                            //txMap.put("DR_INSTRUMENT_2", "DEPOSIT_TRANS");
                                            txMap.put("DR_INSTRUMENT_2", objTO.getDepoId());// Added by nithya on 08-05-2019 for KD 451 - 0020678: Indend transaction(purchase and purchase return) issue
                                            txMap.put("DR_INST_TYPE", "WITHDRAW_SLIP");
                                            txMap.put(TransferTrans.CR_BRANCH, _branchCode);
                                            txMap.put(TransferTrans.CR_PROD_TYPE, TransactionFactory.GL);
                                            txMap.put(TransferTrans.PARTICULARS, "To " + objTO.getNarration() + " ");
                                            txMap.put("TRANS_MOD_TYPE", CommonConstants.TXN_PROD_TYPE_INDEND);
                                            txMap.put("SCREEN_NAME","Indend Transaction");
                                            transferList.add(objTransferTrans.getCreditTransferTO(txMap, objTO.getVatAmt()));
                                            //creditTot = creditTot + objTO.getVatAmt();
                                            creditTot = calculateTotalAmount(creditTot, objTO.getVatAmt());
                                        }
                                        if(objTO.getMiscAmt()>0){
                                            txMap = new HashMap();
                                            txMap.put(TransferTrans.CR_AC_HD, acHeadsMisc.get("MISC_INCOME_HEAD"));                                        
                                            txMap.put(CommonConstants.USER_ID, logTO.getUserId());
                                            //txMap.put("DR_INSTRUMENT_2", "DEPOSIT_TRANS");
                                            txMap.put("DR_INSTRUMENT_2", objTO.getDepoId());// Added by nithya on 08-05-2019 for KD 451 - 0020678: Indend transaction(purchase and purchase return) issue
                                            txMap.put("DR_INST_TYPE", "WITHDRAW_SLIP");
                                            txMap.put(TransferTrans.CR_BRANCH, _branchCode);
                                            txMap.put(TransferTrans.CR_PROD_TYPE, TransactionFactory.GL);
                                            txMap.put(TransferTrans.PARTICULARS, "To " + objTO.getNarration() + " ");
                                            txMap.put("TRANS_MOD_TYPE", CommonConstants.TXN_PROD_TYPE_INDEND);
                                            txMap.put("SCREEN_NAME","Indend Transaction");
                                            transferList.add(objTransferTrans.getCreditTransferTO(txMap, objTO.getMiscAmt()));
                                            //creditTot = creditTot + objTO.getMiscAmt();
                                            creditTot = calculateTotalAmount(creditTot, objTO.getMiscAmt());
                                        }  
                                        if(objTO.getOtherIncome()>0){
                                            txMap = new HashMap();
                                            txMap.put(TransferTrans.CR_AC_HD, acHeadsMisc.get("OTHR_INC_AC_HD_ID"));                                        
                                            txMap.put(CommonConstants.USER_ID, logTO.getUserId());
                                            //txMap.put("DR_INSTRUMENT_2", "DEPOSIT_TRANS");
                                            txMap.put("DR_INSTRUMENT_2", objTO.getDepoId());// Added by nithya on 08-05-2019 for KD 451 - 0020678: Indend transaction(purchase and purchase return) issue
                                            txMap.put("DR_INST_TYPE", "WITHDRAW_SLIP");
                                            txMap.put(TransferTrans.CR_BRANCH, _branchCode);
                                            txMap.put(TransferTrans.CR_PROD_TYPE, TransactionFactory.GL);
                                            txMap.put(TransferTrans.PARTICULARS, "Sale dated : " + saleDate + " ");
                                            txMap.put("TRANS_MOD_TYPE", CommonConstants.TXN_PROD_TYPE_INDEND);
                                            txMap.put("SCREEN_NAME","Indend Transaction");
                                            transferList.add(objTransferTrans.getDebitTransferTO(txMap, objTO.getOtherIncome()));
                                            //debitTot = debitTot + objTO.getMiscAmt();
                                            debitTot = calculateTotalAmount(debitTot,objTO.getOtherIncome());
                                        }
                                        // Added by nithya on 27-09-2018 for KD 262 - GST Indend Transaction - Needed to implement GST in Indent Transaction
                                        if(objTO.getCgstAmt() > 0){
                                            txMap = new HashMap();
                                            txMap.put(TransferTrans.CR_AC_HD, cgstAcHd);                                        
                                            txMap.put(CommonConstants.USER_ID, logTO.getUserId());
                                            //txMap.put("DR_INSTRUMENT_2", "DEPOSIT_TRANS");
                                            txMap.put("DR_INSTRUMENT_2", objTO.getDepoId());// Added by nithya on 08-05-2019 for KD 451 - 0020678: Indend transaction(purchase and purchase return) issue
                                            txMap.put("DR_INST_TYPE", "WITHDRAW_SLIP");
                                            txMap.put(TransferTrans.CR_BRANCH, _branchCode);
                                            txMap.put(TransferTrans.CR_PROD_TYPE, TransactionFactory.GL);
                                            txMap.put(TransferTrans.PARTICULARS, "To " + objTO.getNarration() + " ");
                                            txMap.put("TRANS_MOD_TYPE", CommonConstants.TXN_PROD_TYPE_INDEND);
                                            txMap.put("DR_INSTRUMENT_1", "CGST");
                                            txMap.put("SCREEN_NAME","Indend Transaction");
                                            transferList.add(objTransferTrans.getCreditTransferTO(txMap, objTO.getCgstAmt()));
                                            //creditTot = creditTot + objTO.getVatAmt();
                                            creditTot = calculateTotalAmount(creditTot, objTO.getCgstAmt());
                                        }
                                        if(objTO.getSgstAmt() > 0){
                                            txMap = new HashMap();
                                            txMap.put(TransferTrans.CR_AC_HD, sgstAcHd);                                        
                                            txMap.put(CommonConstants.USER_ID, logTO.getUserId());
                                            //txMap.put("DR_INSTRUMENT_2", "DEPOSIT_TRANS");
                                            txMap.put("DR_INSTRUMENT_2", objTO.getDepoId());// Added by nithya on 08-05-2019 for KD 451 - 0020678: Indend transaction(purchase and purchase return) issue
                                            txMap.put("DR_INST_TYPE", "WITHDRAW_SLIP");
                                            txMap.put(TransferTrans.CR_BRANCH, _branchCode);
                                            txMap.put(TransferTrans.CR_PROD_TYPE, TransactionFactory.GL);
                                            txMap.put(TransferTrans.PARTICULARS, "To " + objTO.getNarration() + " ");
                                            txMap.put("TRANS_MOD_TYPE", CommonConstants.TXN_PROD_TYPE_INDEND);
                                            txMap.put("DR_INSTRUMENT_1", "SGST");
                                            txMap.put("SCREEN_NAME","Indend Transaction");
                                            transferList.add(objTransferTrans.getCreditTransferTO(txMap, objTO.getSgstAmt()));
                                            //creditTot = creditTot + objTO.getVatAmt();
                                            creditTot = calculateTotalAmount(creditTot, objTO.getSgstAmt());
                                        }
                                        
                                        if(objTO.getRoundOffAmt() > 0){
                                            txMap = new HashMap();
                                            txMap.put(TransferTrans.CR_AC_HD, roundOffHead);                                        
                                            txMap.put(CommonConstants.USER_ID, logTO.getUserId());
                                            //txMap.put("DR_INSTRUMENT_2", "DEPOSIT_TRANS");
                                            txMap.put("DR_INSTRUMENT_2", objTO.getDepoId());// Added by nithya on 08-05-2019 for KD 451 - 0020678: Indend transaction(purchase and purchase return) issue
                                            txMap.put("DR_INST_TYPE", "WITHDRAW_SLIP");
                                            txMap.put(TransferTrans.CR_BRANCH, _branchCode);
                                            txMap.put(TransferTrans.CR_PROD_TYPE, TransactionFactory.GL);
                                            txMap.put(TransferTrans.PARTICULARS, "To " + objTO.getNarration() + " ");
                                            txMap.put("TRANS_MOD_TYPE", CommonConstants.TXN_PROD_TYPE_INDEND);
                                            txMap.put("DR_INSTRUMENT_1", "SGST");
                                            txMap.put("SCREEN_NAME","Indend Transaction");
                                            transferList.add(objTransferTrans.getCreditTransferTO(txMap, objTO.getRoundOffAmt()));
                                            //creditTot = creditTot + objTO.getVatAmt();
                                            creditTot = calculateTotalAmount(creditTot, objTO.getRoundOffAmt());
                                        }
                                        // End
                                        
                                    }
                                    
                                    // Indend discount transaction
                                     if (objTO.getTransType() != null && objTO.getTransType().equals("Discount")) { // Added by nithya on 04-04-2020 for KD-1732
                                        if(objTO.getAmount()>0){
                                            txMap = new HashMap();
                                            txMap.put(TransferTrans.CR_AC_HD, acHeads.get("DISCOUNT_AC_HD_ID"));                                        
                                            txMap.put(CommonConstants.USER_ID, logTO.getUserId());
                                            //txMap.put("DR_INSTRUMENT_2", "DEPOSIT_TRANS");
                                            txMap.put("DR_INSTRUMENT_2", objTO.getDepoId());// Added by nithya on 08-05-2019 for KD 451 - 0020678: Indend transaction(purchase and purchase return) issue
                                            txMap.put("DR_INST_TYPE", "WITHDRAW_SLIP");
                                            txMap.put(TransferTrans.CR_BRANCH, _branchCode);
                                            txMap.put(TransferTrans.CR_PROD_TYPE, TransactionFactory.GL);
                                            txMap.put(TransferTrans.PARTICULARS, "To " + objTO.getNarration() + " ");
                                            txMap.put("TRANS_MOD_TYPE", CommonConstants.TXN_PROD_TYPE_INDEND);
                                            txMap.put(TransferTrans.AUTHORIZE_STATUS_2, "ENTERED_AMOUNT");
                                            txMap.put("SCREEN_NAME","Indend Transaction");
                                            transferList.add(objTransferTrans.getCreditTransferTO(txMap, objTO.getAmount()));
                                            //creditTot = creditTot + objTO.getAmount();
                                            creditTot = calculateTotalAmount(creditTot, objTO.getAmount());
                                        }
                                        if(objTO.getVatAmt()>0){
                                            txMap = new HashMap();
                                            txMap.put(TransferTrans.CR_AC_HD, acHeads.get("DISCOUNT_VAT_AC_HD_ID"));                                        
                                            txMap.put(CommonConstants.USER_ID, logTO.getUserId());
                                            //txMap.put("DR_INSTRUMENT_2", "DEPOSIT_TRANS");
                                            txMap.put("DR_INSTRUMENT_2", objTO.getDepoId());// Added by nithya on 08-05-2019 for KD 451 - 0020678: Indend transaction(purchase and purchase return) issue
                                            txMap.put("DR_INST_TYPE", "WITHDRAW_SLIP");
                                            txMap.put(TransferTrans.CR_BRANCH, _branchCode);
                                            txMap.put(TransferTrans.CR_PROD_TYPE, TransactionFactory.GL);
                                            txMap.put(TransferTrans.PARTICULARS, "To " + objTO.getNarration() + " ");
                                            txMap.put("TRANS_MOD_TYPE", CommonConstants.TXN_PROD_TYPE_INDEND);
                                            txMap.put("SCREEN_NAME","Indend Transaction");
                                            txMap.put(TransferTrans.AUTHORIZE_STATUS_2, "ENTERED_AMOUNT");
                                            transferList.add(objTransferTrans.getCreditTransferTO(txMap, objTO.getVatAmt()));
                                            //creditTot = creditTot + objTO.getVatAmt();
                                            creditTot = calculateTotalAmount(creditTot, objTO.getVatAmt());
                                        }
                                        if(objTO.getMiscAmt()>0){
                                            txMap = new HashMap();
                                            txMap.put(TransferTrans.CR_AC_HD, acHeadsMisc.get("MISC_INCOME_HEAD"));                                        
                                            txMap.put(CommonConstants.USER_ID, logTO.getUserId());
                                            //txMap.put("DR_INSTRUMENT_2", "DEPOSIT_TRANS");
                                            txMap.put("DR_INSTRUMENT_2", objTO.getDepoId());// Added by nithya on 08-05-2019 for KD 451 - 0020678: Indend transaction(purchase and purchase return) issue
                                            txMap.put("DR_INST_TYPE", "WITHDRAW_SLIP");
                                            txMap.put(TransferTrans.CR_BRANCH, _branchCode);
                                            txMap.put(TransferTrans.CR_PROD_TYPE, TransactionFactory.GL);
                                            txMap.put(TransferTrans.PARTICULARS, "To " + objTO.getNarration() + " ");
                                            txMap.put("TRANS_MOD_TYPE", CommonConstants.TXN_PROD_TYPE_INDEND);
                                            txMap.put("SCREEN_NAME","Indend Transaction");
                                            txMap.put(TransferTrans.AUTHORIZE_STATUS_2, "ENTERED_AMOUNT");
                                            transferList.add(objTransferTrans.getCreditTransferTO(txMap, objTO.getMiscAmt()));
                                            //creditTot = creditTot + objTO.getMiscAmt();
                                            creditTot = calculateTotalAmount(creditTot, objTO.getMiscAmt());
                                        }  
                                        if(objTO.getOtherIncome()>0){
                                            txMap = new HashMap();
                                            txMap.put(TransferTrans.CR_AC_HD, acHeadsMisc.get("OTHR_INC_AC_HD_ID"));                                        
                                            txMap.put(CommonConstants.USER_ID, logTO.getUserId());
                                            //txMap.put("DR_INSTRUMENT_2", "DEPOSIT_TRANS");
                                            txMap.put("DR_INSTRUMENT_2", objTO.getDepoId());// Added by nithya on 08-05-2019 for KD 451 - 0020678: Indend transaction(purchase and purchase return) issue
                                            txMap.put("DR_INST_TYPE", "WITHDRAW_SLIP");
                                            txMap.put(TransferTrans.CR_BRANCH, _branchCode);
                                            txMap.put(TransferTrans.CR_PROD_TYPE, TransactionFactory.GL);
                                            txMap.put(TransferTrans.PARTICULARS, "Sale dated : " + saleDate + " ");
                                            txMap.put("TRANS_MOD_TYPE", CommonConstants.TXN_PROD_TYPE_INDEND);
                                            txMap.put("SCREEN_NAME","Indend Transaction");
                                            txMap.put(TransferTrans.AUTHORIZE_STATUS_2, "ENTERED_AMOUNT");
                                            transferList.add(objTransferTrans.getDebitTransferTO(txMap, objTO.getOtherIncome()));
                                            //debitTot = debitTot + objTO.getMiscAmt();
                                            debitTot = calculateTotalAmount(debitTot,objTO.getOtherIncome());
                                        }
                                        // Added by nithya on 27-09-2018 for KD 262 - GST Indend Transaction - Needed to implement GST in Indent Transaction
                                        if(objTO.getCgstAmt() > 0){
                                            txMap = new HashMap();
                                            txMap.put(TransferTrans.CR_AC_HD, cgstAcHd);                                        
                                            txMap.put(CommonConstants.USER_ID, logTO.getUserId());
                                            //txMap.put("DR_INSTRUMENT_2", "DEPOSIT_TRANS");
                                            txMap.put("DR_INSTRUMENT_2", objTO.getDepoId());// Added by nithya on 08-05-2019 for KD 451 - 0020678: Indend transaction(purchase and purchase return) issue
                                            txMap.put("DR_INST_TYPE", "WITHDRAW_SLIP");
                                            txMap.put(TransferTrans.CR_BRANCH, _branchCode);
                                            txMap.put(TransferTrans.CR_PROD_TYPE, TransactionFactory.GL);
                                            txMap.put(TransferTrans.PARTICULARS, "To " + objTO.getNarration() + " ");
                                            txMap.put("TRANS_MOD_TYPE", CommonConstants.TXN_PROD_TYPE_INDEND);
                                            txMap.put("DR_INSTRUMENT_1", "CGST");
                                            txMap.put("SCREEN_NAME","Indend Transaction");
                                            txMap.put(TransferTrans.AUTHORIZE_STATUS_2, "ENTERED_AMOUNT");
                                            transferList.add(objTransferTrans.getCreditTransferTO(txMap, objTO.getCgstAmt()));
                                            //creditTot = creditTot + objTO.getVatAmt();
                                            creditTot = calculateTotalAmount(creditTot, objTO.getCgstAmt());
                                        }
                                        if(objTO.getSgstAmt() > 0){
                                            txMap = new HashMap();
                                            txMap.put(TransferTrans.CR_AC_HD, sgstAcHd);                                        
                                            txMap.put(CommonConstants.USER_ID, logTO.getUserId());
                                            //txMap.put("DR_INSTRUMENT_2", "DEPOSIT_TRANS");
                                            txMap.put("DR_INSTRUMENT_2", objTO.getDepoId());// Added by nithya on 08-05-2019 for KD 451 - 0020678: Indend transaction(purchase and purchase return) issue
                                            txMap.put("DR_INST_TYPE", "WITHDRAW_SLIP");
                                            txMap.put(TransferTrans.CR_BRANCH, _branchCode);
                                            txMap.put(TransferTrans.CR_PROD_TYPE, TransactionFactory.GL);
                                            txMap.put(TransferTrans.PARTICULARS, "To " + objTO.getNarration() + " ");
                                            txMap.put("TRANS_MOD_TYPE", CommonConstants.TXN_PROD_TYPE_INDEND);
                                            txMap.put("DR_INSTRUMENT_1", "SGST");
                                            txMap.put("SCREEN_NAME","Indend Transaction");
                                            txMap.put(TransferTrans.AUTHORIZE_STATUS_2, "ENTERED_AMOUNT");
                                            transferList.add(objTransferTrans.getCreditTransferTO(txMap, objTO.getSgstAmt()));
                                            //creditTot = creditTot + objTO.getVatAmt();
                                            creditTot = calculateTotalAmount(creditTot, objTO.getSgstAmt());
                                        }
                                        
                                        if(objTO.getRoundOffAmt() > 0){
                                            txMap = new HashMap();
                                            txMap.put(TransferTrans.CR_AC_HD, roundOffHead);                                        
                                            txMap.put(CommonConstants.USER_ID, logTO.getUserId());
                                            //txMap.put("DR_INSTRUMENT_2", "DEPOSIT_TRANS");
                                            txMap.put("DR_INSTRUMENT_2", objTO.getDepoId());// Added by nithya on 08-05-2019 for KD 451 - 0020678: Indend transaction(purchase and purchase return) issue
                                            txMap.put("DR_INST_TYPE", "WITHDRAW_SLIP");
                                            txMap.put(TransferTrans.CR_BRANCH, _branchCode);
                                            txMap.put(TransferTrans.CR_PROD_TYPE, TransactionFactory.GL);
                                            txMap.put(TransferTrans.PARTICULARS, "To " + objTO.getNarration() + " ");
                                            txMap.put("TRANS_MOD_TYPE", CommonConstants.TXN_PROD_TYPE_INDEND);
                                            txMap.put("DR_INSTRUMENT_1", "SGST");
                                            txMap.put("SCREEN_NAME","Indend Transaction");
                                            txMap.put(TransferTrans.AUTHORIZE_STATUS_2, "ENTERED_AMOUNT");
                                            transferList.add(objTransferTrans.getCreditTransferTO(txMap, objTO.getRoundOffAmt()));
                                            //creditTot = creditTot + objTO.getVatAmt();
                                            creditTot = calculateTotalAmount(creditTot, objTO.getRoundOffAmt());
                                        }
                                        
                                        // End
                                        
                                    }
                                    
                                    // end
                                    
                                    
                                    //Indend Deficite ransaction......... 
                                    if (objTO.getTransType() != null && objTO.getTransType().equals("Deficite")) {
                                        if(objTO.getAmount()>0){
                                            txMap = new HashMap();
                                            txMap.put(TransferTrans.CR_AC_HD, acHeads.get("DEFICIATE_HC_HD_ID"));                                        
                                            txMap.put(CommonConstants.USER_ID, logTO.getUserId());
                                            //txMap.put("DR_INSTRUMENT_2", "DEPOSIT_TRANS");
                                            txMap.put("DR_INSTRUMENT_2", objTO.getDepoId());// Added by nithya on 08-05-2019 for KD 451 - 0020678: Indend transaction(purchase and purchase return) issue
                                            txMap.put("DR_INST_TYPE", "WITHDRAW_SLIP");
                                            txMap.put(TransferTrans.CR_BRANCH, _branchCode);
                                            txMap.put(TransferTrans.CR_PROD_TYPE, TransactionFactory.GL);
                                            txMap.put(TransferTrans.PARTICULARS, "To " + objTO.getNarration() + " ");
                                            txMap.put("TRANS_MOD_TYPE", CommonConstants.TXN_PROD_TYPE_INDEND);
                                            txMap.put("SCREEN_NAME","Indend Transaction");
                                            transferList.add(objTransferTrans.getCreditTransferTO(txMap, objTO.getAmount()));
                                            //creditTot = creditTot + objTO.getAmount();
                                            creditTot = calculateTotalAmount(creditTot, objTO.getAmount());
                                        }
                                        if(objTO.getVatAmt()>0){
                                            txMap = new HashMap();
                                            txMap.put(TransferTrans.CR_AC_HD, acHeadsVat.get("DEF_VAT_AC_HD_ID"));                                        
                                            txMap.put(CommonConstants.USER_ID, logTO.getUserId());
                                            //txMap.put("DR_INSTRUMENT_2", "DEPOSIT_TRANS");
                                            txMap.put("DR_INSTRUMENT_2", objTO.getDepoId());// Added by nithya on 08-05-2019 for KD 451 - 0020678: Indend transaction(purchase and purchase return) issue
                                            txMap.put("DR_INST_TYPE", "WITHDRAW_SLIP");
                                            txMap.put(TransferTrans.CR_BRANCH, _branchCode);
                                            txMap.put(TransferTrans.CR_PROD_TYPE, TransactionFactory.GL);
                                            txMap.put(TransferTrans.PARTICULARS, "To " + objTO.getNarration() + " ");
                                            txMap.put("TRANS_MOD_TYPE", CommonConstants.TXN_PROD_TYPE_INDEND);
                                            txMap.put("SCREEN_NAME","Indend Transaction");
                                            transferList.add(objTransferTrans.getCreditTransferTO(txMap, objTO.getVatAmt()));
                                            //creditTot = creditTot + objTO.getVatAmt();
                                            creditTot = calculateTotalAmount(creditTot, objTO.getVatAmt());
                                        }
                                        if(objTO.getMiscAmt()>0){
                                            txMap = new HashMap();
                                            txMap.put(TransferTrans.CR_AC_HD, acHeadsMisc.get("MISC_INCOME_HEAD"));                                        
                                            txMap.put(CommonConstants.USER_ID, logTO.getUserId());
                                            //txMap.put("DR_INSTRUMENT_2", "DEPOSIT_TRANS");
                                            txMap.put("DR_INSTRUMENT_2", objTO.getDepoId());// Added by nithya on 08-05-2019 for KD 451 - 0020678: Indend transaction(purchase and purchase return) issue
                                            txMap.put("DR_INST_TYPE", "WITHDRAW_SLIP");
                                            txMap.put(TransferTrans.CR_BRANCH, _branchCode);
                                            txMap.put(TransferTrans.CR_PROD_TYPE, TransactionFactory.GL);
                                            txMap.put(TransferTrans.PARTICULARS, "To " + objTO.getNarration() + " ");
                                            txMap.put("TRANS_MOD_TYPE", CommonConstants.TXN_PROD_TYPE_INDEND);
                                            txMap.put("SCREEN_NAME","Indend Transaction");
                                            transferList.add(objTransferTrans.getCreditTransferTO(txMap, objTO.getMiscAmt()));
                                            //creditTot = creditTot + objTO.getMiscAmt();
                                            creditTot = calculateTotalAmount(creditTot, objTO.getMiscAmt());
                                        }
                                        if(objTO.getOtherIncome()>0){
                                            txMap = new HashMap();
                                            txMap.put(TransferTrans.CR_AC_HD, acHeadsMisc.get("OTHR_INC_AC_HD_ID"));                                        
                                            txMap.put(CommonConstants.USER_ID, logTO.getUserId());
                                            //txMap.put("DR_INSTRUMENT_2", "DEPOSIT_TRANS");
                                            txMap.put("DR_INSTRUMENT_2", objTO.getDepoId());// Added by nithya on 08-05-2019 for KD 451 - 0020678: Indend transaction(purchase and purchase return) issue
                                            txMap.put("DR_INST_TYPE", "WITHDRAW_SLIP");
                                            txMap.put(TransferTrans.CR_BRANCH, _branchCode);
                                            txMap.put(TransferTrans.CR_PROD_TYPE, TransactionFactory.GL);
                                            txMap.put(TransferTrans.PARTICULARS, "Sale dated : " + saleDate + " ");
                                            txMap.put("TRANS_MOD_TYPE", CommonConstants.TXN_PROD_TYPE_INDEND);
                                            txMap.put("SCREEN_NAME","Indend Transaction");
                                            transferList.add(objTransferTrans.getDebitTransferTO(txMap, objTO.getOtherIncome()));
                                            //debitTot = debitTot + objTO.getMiscAmt();
                                            debitTot = calculateTotalAmount(debitTot,objTO.getOtherIncome());
                                        }
                                        // Added by nithya on 27-09-2018 for KD 262 - GST Indend Transaction - Needed to implement GST in Indent Transaction
                                        if(objTO.getCgstAmt() > 0){
                                            txMap = new HashMap();
                                            txMap.put(TransferTrans.CR_AC_HD, cgstAcHd);                                        
                                            txMap.put(CommonConstants.USER_ID, logTO.getUserId());
                                            //txMap.put("DR_INSTRUMENT_2", "DEPOSIT_TRANS");
                                            txMap.put("DR_INSTRUMENT_2", objTO.getDepoId());// Added by nithya on 08-05-2019 for KD 451 - 0020678: Indend transaction(purchase and purchase return) issue
                                            txMap.put("DR_INST_TYPE", "WITHDRAW_SLIP");
                                            txMap.put(TransferTrans.CR_BRANCH, _branchCode);
                                            txMap.put(TransferTrans.CR_PROD_TYPE, TransactionFactory.GL);
                                            txMap.put(TransferTrans.PARTICULARS, "To " + objTO.getNarration() + " ");
                                            txMap.put("TRANS_MOD_TYPE", CommonConstants.TXN_PROD_TYPE_INDEND);
                                            txMap.put("DR_INSTRUMENT_1", "CGST");
                                            txMap.put("SCREEN_NAME","Indend Transaction");
                                            transferList.add(objTransferTrans.getCreditTransferTO(txMap, objTO.getCgstAmt()));
                                            //creditTot = creditTot + objTO.getVatAmt();
                                            creditTot = calculateTotalAmount(creditTot, objTO.getCgstAmt());
                                        }
                                        if(objTO.getSgstAmt() > 0){
                                            txMap = new HashMap();
                                            txMap.put(TransferTrans.CR_AC_HD, sgstAcHd);                                        
                                            txMap.put(CommonConstants.USER_ID, logTO.getUserId());
                                            //txMap.put("DR_INSTRUMENT_2", "DEPOSIT_TRANS");
                                            txMap.put("DR_INSTRUMENT_2", objTO.getDepoId());// Added by nithya on 08-05-2019 for KD 451 - 0020678: Indend transaction(purchase and purchase return) issue
                                            txMap.put("DR_INST_TYPE", "WITHDRAW_SLIP");
                                            txMap.put(TransferTrans.CR_BRANCH, _branchCode);
                                            txMap.put(TransferTrans.CR_PROD_TYPE, TransactionFactory.GL);
                                            txMap.put(TransferTrans.PARTICULARS, "To " + objTO.getNarration() + " ");
                                            txMap.put("TRANS_MOD_TYPE", CommonConstants.TXN_PROD_TYPE_INDEND);
                                            txMap.put("DR_INSTRUMENT_1", "SGST");
                                            txMap.put("SCREEN_NAME","Indend Transaction");
                                            transferList.add(objTransferTrans.getCreditTransferTO(txMap, objTO.getSgstAmt()));
                                            //creditTot = creditTot + objTO.getVatAmt();
                                            creditTot = calculateTotalAmount(creditTot, objTO.getSgstAmt());
                                        }
                                        
                                        if(objTO.getRoundOffAmt() > 0){
                                            txMap = new HashMap();
                                            txMap.put(TransferTrans.CR_AC_HD, roundOffHead);                                        
                                            txMap.put(CommonConstants.USER_ID, logTO.getUserId());
                                            //txMap.put("DR_INSTRUMENT_2", "DEPOSIT_TRANS");
                                            txMap.put("DR_INSTRUMENT_2", objTO.getDepoId());// Added by nithya on 08-05-2019 for KD 451 - 0020678: Indend transaction(purchase and purchase return) issue
                                            txMap.put("DR_INST_TYPE", "WITHDRAW_SLIP");
                                            txMap.put(TransferTrans.CR_BRANCH, _branchCode);
                                            txMap.put(TransferTrans.CR_PROD_TYPE, TransactionFactory.GL);
                                            txMap.put(TransferTrans.PARTICULARS, "To " + objTO.getNarration() + " ");
                                            txMap.put("TRANS_MOD_TYPE", CommonConstants.TXN_PROD_TYPE_INDEND);
                                            txMap.put("DR_INSTRUMENT_1", "SGST");
                                            txMap.put("SCREEN_NAME","Indend Transaction");
                                            transferList.add(objTransferTrans.getCreditTransferTO(txMap, objTO.getRoundOffAmt()));
                                            //creditTot = creditTot + objTO.getVatAmt();
                                            creditTot = calculateTotalAmount(creditTot, objTO.getRoundOffAmt());
                                        }
                                        
                                        
                                        // End
                                    }                                    
                                                                   
                                }//Transfer Ends...
                                //Cash Starts...
                                else {                                                                 
                                    if (objTO.getTransType() != null && objTO.getTransType().equals("Sales")) {
                                        if(objTO.getAmount()>0){ 
                                           setCashTransDetails(objTO.getAmount(), acHeads.get("SALES_AC_HD_ID").toString(), CommonConstants.CREDIT , map, generateSingleTransId);
                                        }
                                        if(objTO.getVatAmt()>0){
                                            setCashTransDetails(objTO.getVatAmt(), acHeadsVat.get("SALE_VAT_AC_HD_ID").toString(), CommonConstants.CREDIT, map, generateSingleTransId);
                                        }
                                        if(objTO.getMiscAmt()>0){
                                            setCashTransDetails(objTO.getMiscAmt(), acHeadsMisc.get("MISC_INCOME_HEAD").toString(), CommonConstants.CREDIT, map, generateSingleTransId);
                                        }
                                        if(objTO.getOtherIncome()>0){
                                            setCashTransDetails(objTO.getOtherIncome(), acHeadsMisc.get("OTHR_INC_AC_HD_ID").toString(), CommonConstants.CREDIT, map, generateSingleTransId);
                                        }
                                        // Added by nithya on 07-04-2017 for 6161
                                        if(objTO.getOthrExpAmt()>0){
                                            setCashTransDetails(objTO.getOthrExpAmt(), acHeadsOthr.get("OTHER_EXP_HEAD").toString(), CommonConstants.DEBIT, map, generateSingleTransId);
                                        }
                                        // Added by nithya on 26-09-2018 for KD 262 - GST Indend Transaction - Needed to implement GST in Indent Transaction
                                        if(objTO.getCgstAmt() > 0){
                                            map.put("GST_PARTICULARS","CGST");
                                            setCashTransDetails(objTO.getCgstAmt(), cgstAcHd, CommonConstants.CREDIT, map, generateSingleTransId);
                                        }                                        
                                        if(objTO.getSgstAmt() > 0){
                                            map.put("GST_PARTICULARS","SGST");
                                            setCashTransDetails(objTO.getSgstAmt(), sgstAcHd, CommonConstants.CREDIT, map, generateSingleTransId);
                                        }
                                        // End
                                        
                                         if(objTO.getRoundOffAmt()>0){
                                            setCashTransDetails(objTO.getRoundOffAmt(), roundOffAcHeads.get("ROUND_OFF_AC_HD_ID").toString(), CommonConstants.CREDIT, map, generateSingleTransId);
                                        }
                                        
                                    }
                                    if (objTO.getTransType() != null && objTO.getTransType().equals("Deficite")) {
                                        if(objTO.getAmount()>0){
                                            setCashTransDetails(objTO.getAmount(), acHeads.get("DEFICIATE_HC_HD_ID").toString(), CommonConstants.CREDIT, map, generateSingleTransId);
                                        }
                                        if(objTO.getVatAmt()>0){
                                            setCashTransDetails(objTO.getVatAmt(), acHeadsVat.get("DEF_VAT_AC_HD_ID").toString(), CommonConstants.CREDIT, map, generateSingleTransId);
                                        }
                                        if(objTO.getMiscAmt()>0){
                                            setCashTransDetails(objTO.getMiscAmt(), acHeadsMisc.get("MISC_INCOME_HEAD").toString(), CommonConstants.CREDIT, map, generateSingleTransId);
                                        }
                                        if(objTO.getOtherIncome()>0){
                                            setCashTransDetails(objTO.getOtherIncome(), acHeadsMisc.get("OTHR_INC_AC_HD_ID").toString(), CommonConstants.CREDIT, map, generateSingleTransId);
                                        }
                                        // Added by nithya on 26-09-2018 for KD 262 - GST Indend Transaction - Needed to implement GST in Indent Transaction
                                        if(objTO.getCgstAmt() > 0){
                                            map.put("GST_PARTICULARS","CGST");
                                            setCashTransDetails(objTO.getCgstAmt(), cgstAcHd, CommonConstants.CREDIT, map, generateSingleTransId);
                                        }                                        
                                        if(objTO.getSgstAmt() > 0){
                                            map.put("GST_PARTICULARS","SGST");
                                            setCashTransDetails(objTO.getSgstAmt(), sgstAcHd, CommonConstants.CREDIT, map, generateSingleTransId);
                                        }
                                        // End
                                         if(objTO.getRoundOffAmt()>0){
                                            setCashTransDetails(objTO.getRoundOffAmt(), roundOffAcHeads.get("ROUND_OFF_AC_HD_ID").toString(), CommonConstants.CREDIT, map, generateSingleTransId);
                                        }
                                    }
                                    if (objTO.getTransType() != null && objTO.getTransType().equals("Damage")) {
                                        if(objTO.getAmount()>0){
                                            setCashTransDetails(objTO.getAmount(), acHeads.get("DAMAGE_AC_HD_ID").toString(), CommonConstants.CREDIT, map, generateSingleTransId);
                                        }
                                        if(objTO.getVatAmt()>0){
                                            setCashTransDetails(objTO.getVatAmt(), acHeadsVat.get("DAM_VAT_AC_HD_ID").toString(), CommonConstants.CREDIT, map, generateSingleTransId);
                                        }
                                        if(objTO.getMiscAmt()>0){
                                            setCashTransDetails(objTO.getMiscAmt(), acHeadsMisc.get("MISC_INCOME_HEAD").toString(), CommonConstants.CREDIT, map, generateSingleTransId);
                                        }
                                        if(objTO.getOtherIncome()>0){
                                            setCashTransDetails(objTO.getOtherIncome(), acHeadsMisc.get("OTHR_INC_AC_HD_ID").toString(), CommonConstants.CREDIT, map, generateSingleTransId);
                                        }
                                        
                                        // Added by nithya on 26-09-2018 for KD 262 - GST Indend Transaction - Needed to implement GST in Indent Transaction                                       
                                        if(objTO.getCgstAmt() > 0){
                                            map.put("GST_PARTICULARS","CGST");
                                            setCashTransDetails(objTO.getCgstAmt(), cgstAcHd, CommonConstants.CREDIT, map, generateSingleTransId);
                                        }                                        
                                        if(objTO.getSgstAmt() > 0){
                                            map.put("GST_PARTICULARS","SGST");
                                            setCashTransDetails(objTO.getSgstAmt(), sgstAcHd, CommonConstants.CREDIT, map, generateSingleTransId);
                                        }                                        
                                        //  End                                       
                                         if(objTO.getRoundOffAmt()>0){
                                            setCashTransDetails(objTO.getRoundOffAmt(), roundOffAcHeads.get("ROUND_OFF_AC_HD_ID").toString(), CommonConstants.CREDIT, map, generateSingleTransId);
                                        }
                                    }
                                    
                                    // Cash indend discount transaction - // Added by nithya on 04-04-2020 for KD-1732
                                    
                                    if (objTO.getTransType() != null && objTO.getTransType().equals("Discount")) {
                                        if(objTO.getAmount()>0){
                                            setCashTransDetails(objTO.getAmount(), acHeads.get("DISCOUNT_AC_HD_ID").toString(), CommonConstants.CREDIT, map, generateSingleTransId);
                                        }
                                        if(objTO.getVatAmt()>0){
                                            setCashTransDetails(objTO.getVatAmt(), acHeads.get("DISCOUNT_VAT_AC_HD_ID").toString(), CommonConstants.CREDIT, map, generateSingleTransId);
                                        }
                                        if(objTO.getMiscAmt()>0){
                                            setCashTransDetails(objTO.getMiscAmt(), acHeadsMisc.get("MISC_INCOME_HEAD").toString(), CommonConstants.CREDIT, map, generateSingleTransId);
                                        }
                                        if(objTO.getOtherIncome()>0){
                                            setCashTransDetails(objTO.getOtherIncome(), acHeadsMisc.get("OTHR_INC_AC_HD_ID").toString(), CommonConstants.CREDIT, map, generateSingleTransId);
                                        }
                                        
                                        // Added by nithya on 26-09-2018 for KD 262 - GST Indend Transaction - Needed to implement GST in Indent Transaction                                       
                                        if(objTO.getCgstAmt() > 0){
                                            map.put("GST_PARTICULARS","CGST");
                                            setCashTransDetails(objTO.getCgstAmt(), cgstAcHd, CommonConstants.CREDIT, map, generateSingleTransId);
                                        }                                        
                                        if(objTO.getSgstAmt() > 0){
                                            map.put("GST_PARTICULARS","SGST");
                                            setCashTransDetails(objTO.getSgstAmt(), sgstAcHd, CommonConstants.CREDIT, map, generateSingleTransId);
                                        }                                        
                                        //  End                                       
                                         if(objTO.getRoundOffAmt()>0){
                                            setCashTransDetails(objTO.getRoundOffAmt(), roundOffAcHeads.get("ROUND_OFF_AC_HD_ID").toString(), CommonConstants.CREDIT, map, generateSingleTransId);
                                        }
                                    }
                                    
                                    // End
                                    
                                    if (objTO.getTransType() != null && objTO.getTransType().equals("Purchase Return")) {
                                        if(objTO.getAmount()>0){
                                            setCashTransDetails(objTO.getAmount(), acHeads.get("PUR_RET_AC_HD_ID").toString(), CommonConstants.CREDIT, map, generateSingleTransId);
                                        }
                                        if(objTO.getVatAmt()>0){
                                            setCashTransDetails(objTO.getVatAmt(), acHeadsVat.get("PUR_RTN_VAT_AC_HD_ID").toString(), CommonConstants.CREDIT, map, generateSingleTransId);
                                        }
                                        if(objTO.getMiscAmt()>0){
                                            setCashTransDetails(objTO.getMiscAmt(), acHeadsMisc.get("MISC_INCOME_HEAD").toString(), CommonConstants.CREDIT, map, generateSingleTransId);
                                        }
                                        if(objTO.getOtherIncome()>0){
                                            setCashTransDetails(objTO.getOtherIncome(), acHeadsMisc.get("OTHR_INC_AC_HD_ID").toString(), CommonConstants.CREDIT, map, generateSingleTransId);
                                        }
                                         // Added by nithya on 05-02-2018 for 0009023: Requirement in indend
                                        if(objTO.getOthrExpAmt()>0){
                                            setCashTransDetails(objTO.getOthrExpAmt(), acHeadsOthr.get("OTHER_EXP_HEAD").toString(), CommonConstants.DEBIT, map, generateSingleTransId);
                                        }                                       
                                        // Added by nithya on 26-09-2018 for KD 262 - GST Indend Transaction - Needed to implement GST in Indent Transaction                                      
                                        if(objTO.getCgstAmt() > 0){
                                            map.put("GST_PARTICULARS","CGST");
                                            setCashTransDetails(objTO.getCgstAmt(), cgstAcHd, CommonConstants.CREDIT, map, generateSingleTransId);
                                        }
                                        if(objTO.getSgstAmt() > 0){
                                            map.put("GST_PARTICULARS","SGST");
                                            setCashTransDetails(objTO.getSgstAmt(), sgstAcHd, CommonConstants.CREDIT, map, generateSingleTransId);
                                        }                                        
                                        // End
                                         if(objTO.getRoundOffAmt()>0){
                                            setCashTransDetails(objTO.getRoundOffAmt(), roundOffAcHeads.get("ROUND_OFF_AC_HD_ID").toString(), CommonConstants.CREDIT, map, generateSingleTransId);
                                        }
                                    }
                                    if (objTO.getTransType() != null && objTO.getTransType().equals("Sales Return")) {
                                        if(objTO.getAmount()>0){
                                            setCashTransDetails(objTO.getAmount(), acHeads.get("SALES_RET_AC_HD_ID").toString(), CommonConstants.DEBIT, map, generateSingleTransId);
                                        }
                                        if(objTO.getVatAmt()>0){
                                            setCashTransDetails(objTO.getVatAmt(), acHeadsVat.get("SALE_RTN_VAT_AC_HD_ID").toString(), CommonConstants.DEBIT, map, generateSingleTransId);
                                        }
                                        if(objTO.getOthrExpAmt()>0){
                                            setCashTransDetails(objTO.getOthrExpAmt(), acHeadsOthr.get("OTHER_EXP_HEAD").toString(), CommonConstants.DEBIT, map, generateSingleTransId);
                                        }
                                        if(objTO.getMiscAmt()>0){
                                            setCashTransDetails(objTO.getMiscAmt(), acHeadsMisc.get("MISC_INCOME_HEAD").toString(), CommonConstants.CREDIT, map, generateSingleTransId);
                                        }
                                        if(objTO.getCommRecvdAmt()>0){
                                            setCashTransDetails(objTO.getCommRecvdAmt(), acHeadsComm.get("COMM_RECVD_HEAD").toString(), CommonConstants.CREDIT, map, generateSingleTransId);
                                        }
                                        if(objTO.getOtherIncome()>0){
                                            setCashTransDetails(objTO.getOtherIncome(), acHeadsMisc.get("OTHR_INC_AC_HD_ID").toString(), CommonConstants.CREDIT, map, generateSingleTransId);
                                        }
                                        // Added by nithya on 26-09-2018 for KD 262 - GST Indend Transaction - Needed to implement GST in Indent Transaction
                                        if(objTO.getCgstAmt() > 0){
                                            map.put("GST_PARTICULARS","CGST");
                                            setCashTransDetails(objTO.getCgstAmt(), cgstAcHd, CommonConstants.DEBIT, map, generateSingleTransId);
                                        }
                                        if(objTO.getSgstAmt() > 0){
                                            map.put("GST_PARTICULARS","SGST");
                                            setCashTransDetails(objTO.getSgstAmt(), sgstAcHd , CommonConstants.DEBIT, map, generateSingleTransId);
                                        }
                                        // End
                                         if(objTO.getRoundOffAmt()>0){
                                            setCashTransDetails(objTO.getRoundOffAmt(), roundOffAcHeads.get("ROUND_OFF_AC_HD_ID").toString(), CommonConstants.DEBIT, map, generateSingleTransId);
                                        }
                                    }
                                    if (objTO.getTransType() != null && objTO.getTransType().equals("Purchase")) {
                                        if(objTO.getAmount()>0){
                                            setCashTransDetails(objTO.getAmount(), acHeads.get("PURCHASE_AC_HD_ID").toString(), CommonConstants.DEBIT, map, generateSingleTransId);
                                        }
                                        if(objTO.getVatAmt()>0){
                                            setCashTransDetails(objTO.getVatAmt(), acHeadsVat.get("PUR_VAT_AC_HD_ID").toString(), CommonConstants.DEBIT, map, generateSingleTransId);
                                        }
                                        if(objTO.getOthrExpAmt()>0){
                                            setCashTransDetails(objTO.getOthrExpAmt(), acHeadsOthr.get("OTHER_EXP_HEAD").toString(), CommonConstants.DEBIT, map, generateSingleTransId);
                                        }
                                        if(objTO.getMiscAmt()>0){
                                            setCashTransDetails(objTO.getMiscAmt(), acHeadsMisc.get("MISC_INCOME_HEAD").toString(), CommonConstants.CREDIT, map, generateSingleTransId);
                                        }
                                        if(objTO.getCommRecvdAmt()>0){
                                            setCashTransDetails(objTO.getCommRecvdAmt(), acHeadsComm.get("COMM_RECVD_HEAD").toString(), CommonConstants.CREDIT, map, generateSingleTransId);
                                        }
                                        if(objTO.getOtherIncome()>0){
                                            setCashTransDetails(objTO.getOtherIncome(), acHeadsMisc.get("OTHR_INC_AC_HD_ID").toString(), CommonConstants.CREDIT, map, generateSingleTransId);
                                        }
                                        // Added by nithya on 26-09-2018 for KD 262 - GST Indend Transaction - Needed to implement GST in Indent Transaction                                       
                                        if(objTO.getCgstAmt() > 0){
                                            map.put("GST_PARTICULARS","CGST");
                                            setCashTransDetails(objTO.getCgstAmt(), cgstAcHd, CommonConstants.DEBIT, map, generateSingleTransId);
                                        }
                                        if(objTO.getSgstAmt() > 0){
                                            map.put("GST_PARTICULARS","SGST");
                                            setCashTransDetails(objTO.getSgstAmt(), sgstAcHd, CommonConstants.DEBIT, map, generateSingleTransId);
                                        }                                        
                                        // End
                                        
                                         if(objTO.getRoundOffAmt()>0){
                                            setCashTransDetails(objTO.getRoundOffAmt(), roundOffAcHeads.get("ROUND_OFF_AC_HD_ID").toString(), CommonConstants.DEBIT, map, generateSingleTransId);
                                        }
                                        
                                    }
                                    //To Remitt issue...
                                    objTransactionTO.setStatus(CommonConstants.STATUS_CREATED);
                                    objTransactionTO.setBatchId(objTO.getStrIRNo());
                                    objTransactionTO.setBatchDt(currDt);
                                    objTransactionTO.setTransId(objTransferTrans.getLinkBatchId());
                                    objTransactionTO.setBranchId(_branchCode);
                                    sqlMap.executeUpdate("insertRemittanceIssueTransactionTO", objTransactionTO);
                                }
                            }                            
                            // Code End
                            //getTransDetails(objTO.getStrIRNo());
                        }
                    }
                     //To Transfer........
                     if (transferList != null && transferList.size() > 0) {
                        if(creditTot == debitTot){
                            objTransferTrans.doDebitCredit(transferList, _branchCode, false);
                        }else{
                            throw new TTException("Batch Not Tallied...credit = "+creditTot+"  and  debit = "+debitTot); 
                        }
                     }  
                     getTransDetails(objTO.getStrIRNo());
                }
            }

        } catch (Exception e) {
            sqlMap.rollbackTransaction();
            e.printStackTrace();
            throw new TransRollbackException(e);
        }
    }
    
    public String getSupplierName(String supplierId) throws SQLException{
        HashMap supMap = new HashMap();
        String supplierName = "";
        supMap.put("SUPPLY_ID", supplierId);
//        supMap = (HashMap) (sqlMap.executeQueryForList("getIndendSupplier", supMap)).get(0);
//            if (supMap != null && supMap.size() > 0) {
//                supplierName = CommonUtil.convertObjToStr(supMap.get("NAME"));
//            }

        List supllierLst = sqlMap.executeQueryForList("getIndendSupplier", supMap);
        if (supllierLst != null && supllierLst.size() > 0) {
            supMap = (HashMap) supllierLst.get(0);
            if (supMap != null && supMap.size() > 0) {
                supplierName = CommonUtil.convertObjToStr(supMap.get("NAME"));
            }
        }

  
        return supplierName;
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

    private double calculateTotalAmount(double Amount,double AddingAmount) {        
        double returnTotalAmt = 0.0;
        try {            
            Amount += AddingAmount;                
            long dr = roundOff((long) (Amount * 1000));                
            Amount = dr / 100.0; 
            returnTotalAmt = new Double(Amount).doubleValue();
            System.out.println("returnTotalAmt%%%%%%%%%%%"+returnTotalAmt);            
        } catch (Exception e) {
           e.printStackTrace();
        }
        
        return returnTotalAmt;
    }
    
    private long roundOff(long amt) {
        long amount = amt / 10;
//        int lastDigit = (int)amt%10;
        int lastDigit = (int) (amt % 10);  //() brackets added because sometimes returns 8 if 0 also.
        if (lastDigit > 5) {
            amount++;
        }
        return amount;
    }
    
    public static void main(String str[]) {
        try {
            IndendMultipleDAO dao = new IndendMultipleDAO();
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

        System.out.println("*&$@#$@#$map:" + map);

        if (!map.containsKey(CommonConstants.AUTHORIZEMAP)) {
            objTO = (IndendTO) map.get("IndendTO");
            final String command = objTO.getCommand();
            if (command.equals(CommonConstants.TOSTATUS_INSERT)) {
                insertData(map);
                //            returnMap = new HashMap();
                System.out.println("objTO.getStrIRNo()objTO.getStrIRNo()objTO.getStrIRNo()====:" + objTO);
                System.out.println("objTO.getStrIRNo()====:" + objTO.getStrIRNo());
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
            System.out.println("map:" + map);
            HashMap authMap = (HashMap) map.get(CommonConstants.AUTHORIZEMAP);
            authMap.put(CommonConstants.BRANCH_ID, (String) map.get(CommonConstants.BRANCH_ID));
            authMap.put(CommonConstants.USER_ID, (String) map.get(CommonConstants.USER_ID));
            System.out.println("authMap:" + authMap);
            if (authMap != null) {
                if(map.containsKey("INDEND_LIABILITY")){
                    map.put(CommonConstants.STATUS, CommonUtil.convertObjToStr(authMap.get("STATUS")));
                    map.put(CommonConstants.USER_ID, authMap.get(CommonConstants.USER_ID));
                    map.put("CURR_DATE", currDt);
                    map.putAll(authMap);
                    sqlMap.executeUpdate("authorizeIndend", map);
                }else{
                    authorize(authMap);
                }
            }
        }

        destroyObjects();
        System.out.println("@#@#@# returnMap:" + returnMap);
        return returnMap;
    }

    private void authorize(HashMap map) throws Exception {
        System.out.println("authorize#########" + map);
        String status = (String) map.get("STATUS");
        String linkBatchId = null;
        String disbursalNo = null;
        HashMap cashAuthMap;
        TransactionTO objTransactionTO = null;
        try {
            sqlMap.startTransaction();
            transactionDAO = new TransactionDAO(CommonConstants.DEBIT);
            System.out.println("map111111@@@1111:" + map);
            disbursalNo = CommonUtil.convertObjToStr(map.get("IRID"));
            map.put(CommonConstants.STATUS, status);
            map.put(CommonConstants.USER_ID, map.get(CommonConstants.USER_ID));
            map.put("CURR_DATE", currDt);
            System.out.println("status------------>" + status);
            sqlMap.executeUpdate("authorizeIndend", map);  //authorizeIndendDisbursal not there in map
            linkBatchId = CommonUtil.convertObjToStr(map.get("IRID"));//Transaction Batch Id
            System.out.println("linkBatchId11111>>>>>>" + linkBatchId);
            //Separation of Authorization for Cash and Transfer
            //Call this in all places that need Authorization for Transaction
            cashAuthMap = new HashMap();
            cashAuthMap.put(CommonConstants.BRANCH_ID, map.get(CommonConstants.BRANCH_ID));
            cashAuthMap.put(CommonConstants.USER_ID, map.get(CommonConstants.USER_ID));
            cashAuthMap.put("INITIATED_BRANCH", map.get(CommonConstants.BRANCH_ID));
            //cashAuthMap.put("DAILY", "DAILY");
            //cashAuthMap.put("INDEND_TRANSACTION", "INDEND_TRANSACTION");
            cashAuthMap.put("CURR_DATE", currDt);
            cashAuthMap.put("TODAY_DT", currDt);
            cashAuthMap.put("DEBIT_LOAN_TYPE", "DP");//Added By Revathi.L reff by Mr.Abi
            HashMap tempAuthMap = new HashMap();
            tempAuthMap.put("LINK_BATCH_ID", linkBatchId);
            cashAuthMap.put("LINK_BATCH_ID", linkBatchId);
            tempAuthMap.put("TRANS_DT", getProperDateFormat(map.get("AUTHORIZEDT")));
            tempAuthMap.put("INITIATED_BRANCH", cashAuthMap.get("BRANCH_CODE"));
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
            TransactionDAO.authorizeCashAndTransfer(linkBatchId, status, cashAuthMap);
            HashMap transAuthMap = new HashMap();
            transAuthMap.put("LINK_BATCH_ID", linkBatchId);
            // tempAuthMap.put("TRANS_DT",map.get("AUTHORIZEDT"));
            transAuthMap.put("INITIATED_BRANCH", cashAuthMap.get("BRANCH_CODE"));
            List transAuthList;
            transAuthList = sqlMap.executeQueryForList("getSelectTransferTransForauthorize", transAuthMap);
            if (transAuthList != null && transAuthList.size() > 0) {
                for (int i = 0; i <= transAuthList.size(); i++) {
                    TransactionDAO.authorizeCashAndTransfer(linkBatchId, status, cashAuthMap);
                }
            }
            //  }
            HashMap transMap = new HashMap();
            transMap.put("LINK_BATCH_ID", linkBatchId);
            sqlMap.executeUpdate("updateInstrumentNO1Transfer", transMap);
            sqlMap.executeUpdate("updateInstrumentNO1Cash", transMap);
            transMap = null;
            objTransactionTO = new TransactionTO();
            objTransactionTO.setBatchId(CommonUtil.convertObjToStr(disbursalNo));
            objTransactionTO.setTransId(CommonUtil.convertObjToStr(linkBatchId));
            objTransactionTO.setBranchId(_branchCode);
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
