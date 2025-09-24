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
package com.see.truetransact.serverside.indend.suspenseIndend;

import com.see.truetransact.serverside.indend.*;

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
public class SuspneseIndendDAO extends TTDAO {

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
    public SuspneseIndendDAO() throws ServiceLocatorException {
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
            System.out.println("currDt??>###>>>>" + currDt.clone());
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
                doIndendTransactions(map);
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

    private void doIndendTransactions(HashMap map) throws Exception, Exception {
        try {
            if (objTO.getCommand() != null) {
                if (objTO.getCommand().equals(CommonConstants.TOSTATUS_INSERT)) {
                    String generateSingleTransId = generateLinkID();
                    TransferTrans objTransferTrans = new TransferTrans();
                    ArrayList transferList = new ArrayList();
                    HashMap txMap;
                    String key1 = "";
                    double creditTot = 0.0, debitTot = 0.0;
                    HashMap acHeads = null;
                    HashMap acHeadsMisc = null;
                    HashMap acHeadsVat = null;
                    HashMap acHeadsOthr = null;
                    HashMap acHeadsComm = null;
                    HashMap acHeadsDiscount = null;
                    boolean transflag = false;
                    HashMap whereMap = new HashMap();
                    whereMap.put("IRNO", objTO.getStrIRNo());
                    //whereMap.put("DEPID", objTO.getDepoId());
                    HashMap finalMap = (HashMap) map.get("finalMap");
                    System.out.println("ma###############p??>>>" + map);
                    System.out.println("finalMap??>>>" + finalMap);
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
                        if (objTO.getDiscountAmt() != null) {
                            acHeadsDiscount = (HashMap) sqlMap.executeQueryForObject("Indend.getAcHeadsPurchaseSuspenseDicount", whereMap);
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
                        
                        String billDate = DateUtil.getStringDate(DateUtil.getDateMMDDYYYY(CommonUtil.convertObjToStr(objTO.getBillDate())));
                        String saleDate = DateUtil.getStringDate(DateUtil.getDateMMDDYYYY(CommonUtil.convertObjToStr(objTO.getSalesDate())));
                        String particulars = "";
                        String transferParticulars = "";
                        if (objTO.getTransType().equals("Purchase")) {
                            particulars = getSupplierName(objTO.getSupplier()) + " " + " Bill No : " + objTO.getPurchBillNo() + " " + billDate;
                            transferParticulars = " " + " Bill No : " + objTO.getPurchBillNo() + " " + billDate;
                        }

                      if (finalMap.size() > 0) {
                            //Transfer Credit Transaction....
                          creditTot = 0.0;
                            if (objTO.getTransType() != null && objTO.getTransType().equals("Purchase")) {                              
                                if (objTO.getAmount() > 0) {
                                    txMap = new HashMap();
                                    txMap.put(TransferTrans.DR_AC_HD, acHeads.get("PURCHASE_AC_HD_ID"));
                                    txMap.put(CommonConstants.USER_ID, logTO.getUserId());
                                    txMap.put("DR_INSTRUMENT_2", "DEPOSIT_TRANS");
                                    txMap.put("DR_INST_TYPE", "WITHDRAW_SLIP");
                                    txMap.put(TransferTrans.DR_BRANCH, _branchCode);
                                    txMap.put(TransferTrans.DR_PROD_TYPE, TransactionFactory.GL);
                                    txMap.put(TransferTrans.AUTHORIZE_STATUS_2, "ENTERED_AMOUNT");
                                    txMap.put(TransferTrans.PARTICULARS, particulars);
                                    txMap.put("TRANS_MOD_TYPE", CommonConstants.TXN_PROD_TYPE_INDEND);
                                    System.out.println("txMap##################PURCHASE_AC_HD_ID" + txMap);
                                    transferList.add(objTransferTrans.getDebitTransferTO(txMap, objTO.getAmount()));
                                    creditTot = calculateDebitAmount(creditTot,objTO.getAmount());
                                }
                                if (objTO.getVatAmt() > 0) {
                                    txMap = new HashMap();
                                    txMap.put(TransferTrans.DR_AC_HD, acHeadsVat.get("PUR_VAT_AC_HD_ID"));
                                    txMap.put(CommonConstants.USER_ID, logTO.getUserId());
                                    txMap.put("DR_INSTRUMENT_2", "DEPOSIT_TRANS");
                                    txMap.put("DR_INST_TYPE", "WITHDRAW_SLIP");
                                    txMap.put(TransferTrans.DR_BRANCH, _branchCode);
                                    txMap.put(TransferTrans.DR_PROD_TYPE, TransactionFactory.GL);
                                    txMap.put(TransferTrans.AUTHORIZE_STATUS_2, "ENTERED_AMOUNT");
                                    txMap.put(TransferTrans.PARTICULARS, particulars);
                                    txMap.put("TRANS_MOD_TYPE", CommonConstants.TXN_PROD_TYPE_INDEND);
                                    System.out.println("txMap##################PUR_VAT_AC_HD_ID" + txMap);
                                    transferList.add(objTransferTrans.getDebitTransferTO(txMap, objTO.getVatAmt()));
                                    creditTot = calculateDebitAmount(creditTot,objTO.getVatAmt());
                                }
                                if (objTO.getOthrExpAmt() > 0) {
                                    txMap = new HashMap();
                                    txMap.put(TransferTrans.DR_AC_HD, acHeadsOthr.get("OTHER_EXP_HEAD"));
                                    txMap.put(CommonConstants.USER_ID, logTO.getUserId());
                                    txMap.put("DR_INSTRUMENT_2", "DEPOSIT_TRANS");
                                    txMap.put("DR_INST_TYPE", "WITHDRAW_SLIP");
                                    txMap.put(TransferTrans.DR_BRANCH, _branchCode);
                                    txMap.put(TransferTrans.DR_PROD_TYPE, TransactionFactory.GL);
                                    txMap.put(TransferTrans.AUTHORIZE_STATUS_2, "ENTERED_AMOUNT");
                                    txMap.put(TransferTrans.PARTICULARS, particulars);
                                    txMap.put("TRANS_MOD_TYPE", CommonConstants.TXN_PROD_TYPE_INDEND);
                                    System.out.println("txMap##################OTHER_EXP_HEAD" + txMap);
                                    transferList.add(objTransferTrans.getDebitTransferTO(txMap, objTO.getOthrExpAmt()));
                                    creditTot = calculateDebitAmount(creditTot,objTO.getOthrExpAmt());
                                }
                                if (objTO.getMiscAmt() > 0) {
                                    txMap = new HashMap();
                                    txMap.put(TransferTrans.CR_AC_HD, acHeadsMisc.get("MISC_INCOME_HEAD"));
                                    txMap.put(CommonConstants.USER_ID, logTO.getUserId());
                                    txMap.put("DR_INSTRUMENT_2", "DEPOSIT_TRANS");
                                    txMap.put("DR_INST_TYPE", "WITHDRAW_SLIP");
                                    txMap.put(TransferTrans.CR_BRANCH, _branchCode);
                                    txMap.put(TransferTrans.CR_PROD_TYPE, TransactionFactory.GL);
                                    txMap.put(TransferTrans.AUTHORIZE_STATUS_2, "ENTERED_AMOUNT");
                                    txMap.put(TransferTrans.PARTICULARS, particulars);
                                    txMap.put("TRANS_MOD_TYPE", CommonConstants.TXN_PROD_TYPE_INDEND);
                                    System.out.println("txMap##################MISC_INCOME_HEAD" + txMap);
                                    transferList.add(objTransferTrans.getCreditTransferTO(txMap, objTO.getMiscAmt()));
                                    creditTot = calculateCreditAmount(creditTot,objTO.getMiscAmt());
                                }
                                if (objTO.getCommRecvdAmt() > 0) {
                                    txMap = new HashMap();
                                    txMap.put(TransferTrans.CR_AC_HD, acHeadsComm.get("COMM_RECVD_HEAD"));
                                    txMap.put(CommonConstants.USER_ID, logTO.getUserId());
                                    txMap.put("DR_INSTRUMENT_2", "DEPOSIT_TRANS");
                                    txMap.put("DR_INST_TYPE", "WITHDRAW_SLIP");
                                    txMap.put(TransferTrans.CR_BRANCH, _branchCode);
                                    txMap.put(TransferTrans.CR_PROD_TYPE, TransactionFactory.GL);
                                    txMap.put(TransferTrans.PARTICULARS, particulars);
                                    txMap.put(TransferTrans.AUTHORIZE_STATUS_2, "ENTERED_AMOUNT");
                                    txMap.put("TRANS_MOD_TYPE", CommonConstants.TXN_PROD_TYPE_INDEND);
                                    System.out.println("txMap##################COMM_RECVD_HEAD" + txMap);
                                    transferList.add(objTransferTrans.getCreditTransferTO(txMap, objTO.getCommRecvdAmt()));
                                    creditTot = calculateCreditAmount(creditTot,objTO.getCommRecvdAmt());
                                }
                                
                                if (objTO.getDiscountAmt() > 0) {
                                    txMap = new HashMap();
                                    txMap.put(TransferTrans.CR_AC_HD, acHeadsDiscount.get("SUSPENSE_DISCOUNT_ACHD"));
                                    txMap.put(CommonConstants.USER_ID, logTO.getUserId());
                                    txMap.put("DR_INSTRUMENT_2", "DEPOSIT_TRANS");
                                    txMap.put("DR_INST_TYPE", "WITHDRAW_SLIP");
                                    txMap.put(TransferTrans.CR_BRANCH, _branchCode);
                                    txMap.put(TransferTrans.CR_PROD_TYPE, TransactionFactory.GL);
                                    txMap.put(TransferTrans.PARTICULARS, particulars);
                                    txMap.put(TransferTrans.AUTHORIZE_STATUS_2, "ENTERED_AMOUNT");
                                    txMap.put("TRANS_MOD_TYPE", CommonConstants.TXN_PROD_TYPE_INDEND);
                                    System.out.println("txMap##################DISCOUNT_AC_HD_ID" + txMap);
                                    transferList.add(objTransferTrans.getCreditTransferTO(txMap, objTO.getDiscountAmt()));
                                    creditTot = calculateCreditAmount(creditTot,objTO.getDiscountAmt());
                                }
                                
                                
                                if (creditTot > 0) {
                                    txMap = new HashMap();
                                    txMap.put(TransferTrans.CR_AC_HD, CommonUtil.convertObjToStr(CommonUtil.convertObjToStr(objTO.getSuspenseAcHd())));
                                    txMap.put(TransferTrans.CR_PROD_TYPE, TransactionFactory.GL);
                                    txMap.put(CommonConstants.USER_ID, logTO.getUserId());
                                    txMap.put("DR_INSTRUMENT_2", "DEPOSIT_TRANS");
                                    txMap.put(TransferTrans.CR_BRANCH, _branchCode);
                                    txMap.put(TransferTrans.AUTHORIZE_STATUS_2, "ENTERED_AMOUNT");
                                    txMap.put("TRANS_MOD_TYPE", CommonConstants.TXN_PROD_TYPE_GL);
                                    txMap.put(TransferTrans.PARTICULARS, transferParticulars);
                                    transferList.add(objTransferTrans.getCreditTransferTO(txMap, creditTot));
                                }
                            }


                        }
                    }
                    //To Transfer........
                    if (transferList != null && transferList.size() > 0) {
                        objTransferTrans.setInitiatedBranch(_branchCode);
                        objTransferTrans.setLinkBatchId(objTO.getStrIRNo());
                        objTransferTrans.doDebitCredit(transferList, _branchCode, false);
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

    public String getSupplierName(String supplierId) throws SQLException {
        HashMap supMap = new HashMap();
        String supplierName = "";
        supMap.put("SUPPLY_ID", supplierId);
        supMap = (HashMap) (sqlMap.executeQueryForList("getIndendSupplier", supMap)).get(0);
        if (supMap != null && supMap.size() > 0) {
            supplierName = CommonUtil.convertObjToStr(supMap.get("NAME"));
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

    private double calculateCreditAmount(double Amount, double AddingAmount) {
        double returnTotalAmt = 0.0;
        try {
            Amount -= AddingAmount;
            long dr = roundOff((long) (Amount * 1000));
            Amount = dr / 100.0;
            returnTotalAmt = new Double(Amount).doubleValue();
            System.out.println("returnTotalAmt%%%%%%%%%%%" + returnTotalAmt);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return returnTotalAmt;
    }
    
    private double calculateDebitAmount(double Amount, double AddingAmount) {
        double returnTotalAmt = 0.0;
        try {
            Amount += AddingAmount;
            long dr = roundOff((long) (Amount * 1000));
            Amount = dr / 100.0;
            returnTotalAmt = new Double(Amount).doubleValue();
            System.out.println("returnTotalAmt%%%%%%%%%%%" + returnTotalAmt);
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
            SuspneseIndendDAO dao = new SuspneseIndendDAO();
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

        System.out.println("*&$@#$@#$ suspense indend DAO " + map);

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
                authorize(authMap);
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
            System.out.println("@#$@zvbvxcvzx#$map:" + map);
            cashAuthMap.put(CommonConstants.BRANCH_ID, map.get(CommonConstants.BRANCH_ID));
            cashAuthMap.put(CommonConstants.USER_ID, map.get(CommonConstants.USER_ID));
            cashAuthMap.put("INITIATED_BRANCH", map.get(CommonConstants.BRANCH_ID));
            //cashAuthMap.put("DAILY", "DAILY");
            //cashAuthMap.put("INDEND_TRANSACTION", "INDEND_TRANSACTION");
            cashAuthMap.put("CURR_DATE", currDt);
            cashAuthMap.put("TODAY_DT", currDt);
            System.out.println("map:" + map);
            System.out.println("cashAuthMap:" + cashAuthMap);
            System.out.println("#$%#$%#$%xcvlinkBatchId" + linkBatchId);
            HashMap tempAuthMap = new HashMap();
            tempAuthMap.put("LINK_BATCH_ID", linkBatchId);
            cashAuthMap.put("LINK_BATCH_ID", linkBatchId);
            System.out.println("map.get(AUTHORIZEDT??>>>" + map.get("AUTHORIZEDT"));
            tempAuthMap.put("TRANS_DT", getProperDateFormat(map.get("AUTHORIZEDT")));
            tempAuthMap.put("INITIATED_BRANCH", cashAuthMap.get("BRANCH_CODE"));
            System.out.println("tempAuthMap>>>>>" + tempAuthMap);
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
            System.out.println("linkBatchId or TRANS_ID" + cashAuthMap.get("TRANS_ID"));
            TransactionDAO.authorizeCashAndTransfer(linkBatchId, status, cashAuthMap);

            HashMap transAuthMap = new HashMap();
            transAuthMap.put("LINK_BATCH_ID", linkBatchId);
            // tempAuthMap.put("TRANS_DT",map.get("AUTHORIZEDT"));
            transAuthMap.put("INITIATED_BRANCH", cashAuthMap.get("BRANCH_CODE"));
            System.out.println("transAuthMap>>>>>" + transAuthMap);
            List transAuthList;
            transAuthList = sqlMap.executeQueryForList("getSelectTransferTransForauthorize", transAuthMap);
            System.out.println("linkBatchId222@@22>>>>>" + linkBatchId);
            System.out.println("status222@@22>>>>>" + transAuthList);
            System.out.println("cashAuthMap222@@22>>>>>" + transAuthList);
            System.out.println("transAuthList>>>>>" + transAuthList);
            if (transAuthList != null && transAuthList.size() > 0) {
                for (int i = 0; i <= transAuthList.size(); i++) {
                    TransactionDAO.authorizeCashAndTransfer(linkBatchId, status, cashAuthMap);
                }
            }
            //  }
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
        _branchCode = (String) obj.get(CommonConstants.BRANCH_ID);
        currDt = ServerUtil.getCurrentDate(_branchCode);
        return getData(obj);
    }

    private void destroyObjects() {
        objTO = null;
    }
}
