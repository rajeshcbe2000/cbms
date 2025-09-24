/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition.
 **
 *
 * RentRegisterDAO.java
 *
 * Created on Thu Jan 20 17:19:05 IST 2005
 */
package com.see.truetransact.serverside.roomrent.rentregister;

import java.util.List;
import java.util.ArrayList;

import com.ibatis.db.sqlmap.SqlMap;
import com.see.truetransact.serverutil.ServerUtil;
import java.util.LinkedHashMap;
import com.see.truetransact.commonutil.CommonConstants;
import com.see.truetransact.commonutil.NoCommandException;
import com.see.truetransact.commonutil.CommonUtil;
import com.see.truetransact.serverside.TTDAO;
import com.see.truetransact.serverutil.ServerConstants;
import com.see.truetransact.servicelocator.ServiceLocator;
import com.see.truetransact.serverexception.TransRollbackException;
import com.see.truetransact.serverexception.ServiceLocatorException;
import com.see.truetransact.transferobject.common.transaction.TransactionTO;
import com.see.truetransact.serverside.transaction.cash.CashTransactionDAO;
import com.see.truetransact.serverside.common.transaction.TransactionDAO;
import com.see.truetransact.commonutil.TTException;
import com.see.truetransact.serverside.common.transaction.TransferTrans;
import com.see.truetransact.serverside.transaction.cash.CashTransaction;
import com.see.truetransact.serverside.transaction.common.TransactionFactory;

//import com.see.truetransact.serverside.common.idgenerate.IDGenerateDAO;
//import com.see.truetransact.serverside.common.log.LogDAO;
//import com.see.truetransact.transferobject.common.log.LogTO;
import com.see.truetransact.transferobject.common.log.LogTO;
import com.see.truetransact.serverside.common.log.LogDAO;
import com.see.truetransact.transferobject.transaction.cash.CashTransactionTO;

import com.see.truetransact.transferobject.roomrent.rentregister.RentRegisterTO;
import com.see.truetransact.transferobject.roomrent.renttrans.RentTransTO;
import com.see.truetransact.serverside.common.idgenerate.IDGenerateDAO;
//import com.see.truetransact.ui.deposit.CommonMethod;
import java.util.HashMap;
import java.util.Date;
import com.see.truetransact.commonutil.DateUtil;
import java.util.Calendar;
import java.text.SimpleDateFormat;

/**
 * RentRegisterDAO.
 *
 */
public class RentRegisterDAO extends TTDAO {
    private static SqlMap sqlMap = null;
    private RentRegisterTO objTO;
    private TransactionDAO transactionDAO = null;
    private LogDAO logDAO;
    private LogTO logTO;
    private Date currDt;
    HashMap returnMap;
    boolean insuffBal = false;

    /**
     * Creates a new instance of TokenConfigDAO
     */
    public RentRegisterDAO() throws ServiceLocatorException {
        ServiceLocator locate = ServiceLocator.getInstance();
        sqlMap = (SqlMap) locate.getDAOSqlMap();
    }

    private HashMap getData(HashMap map) throws Exception {
        HashMap returnMap = new HashMap();
        HashMap where = (HashMap) map.get(CommonConstants.MAP_WHERE);
        System.out.println("where===================" + where);
        List list = (List) sqlMap.executeQueryForList("RentRegister.getSelectRentRegister", where);
        returnMap.put("RentRegisterTO", list);
        //Tranaction
        if (where.containsKey("RRID")) {
            HashMap getRemitTransMap = new HashMap();
            getRemitTransMap.put("TRANS_ID", where.get("RRID"));
            getRemitTransMap.put("TRANS_DT", currDt.clone());
            getRemitTransMap.put("BRANCH_CODE", _branchCode);
            System.out.println("@#%$#@%#$%getRemitTransMap:" + getRemitTransMap);
            list = (List) sqlMap.executeQueryForList("getSelectRemittanceIssueTransactionTODate", getRemitTransMap);
            //            list = sqlMap.executeQueryForList("getSelectRemittanceIssueTransactionTO", where.get("DISBURSAL_NO"));
            if (list != null && list.size() > 0) {
                returnMap.put("TRANSACTION_LIST", list);
            }
        }
        return returnMap;
    }

    private String getRRId() throws Exception {
        IDGenerateDAO dao = new IDGenerateDAO();
        final HashMap where = new HashMap();
        where.put(CommonConstants.MAP_WHERE, "RRID");
        HashMap map = generateID();
        return (String) map.get(CommonConstants.DATA);
    }

    public HashMap generateID() {
        HashMap hash = null, result = null;
        try {
            String mapName = "getCurrentID";
            HashMap where = new HashMap();
            where.put("ID_KEY", "RRID"); //Here u have to pass BORROW_ID or something else
            //where.put("BRANCH_CODE",_branchCode);
            List list = null;
            sqlMap.executeUpdate("updateIDGenerated", where);  // This update statement just updates curr_value=curr_value+1
            list = (List) sqlMap.executeQueryForList(mapName, where);  // This will get u the updated curr_value, prefix and length
            if (list != null && list.size() > 0) {
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
                int brCode = _branchCode.trim().length();
                String newID = String.valueOf(hash.get("CURR_VALUE"));
                // Number Part of the String and incrementing 1 (ex. only 00085 from OGGOT00085)
                result = new HashMap();
                String genID = strPrefix.toUpperCase() +_branchCode+ CommonUtil.lpad(newID, len - numFrom - brCode, '0');
                result.put(CommonConstants.DATA, genID);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    private void insertData(HashMap map) throws Exception {
        try {
            System.out.println("map==============================" + map);
            sqlMap.startTransaction();
            objTO.setRrId(getRRId());
            objTO.setStatus(CommonConstants.STATUS_CREATED);
            objTO.setBranchCode(CommonUtil.convertObjToStr(map.get("BRANCH_CODE")));
            if (map.containsKey("TransactionTO")) {
                LinkedHashMap TransactionDetailsMap = (LinkedHashMap) map.get("TransactionTO");
                if (TransactionDetailsMap != null && TransactionDetailsMap.size() > 0) {
                    doRentRegisterTransactions(map);
                }
            }
            if (!insuffBal) {
                System.out.println("inside not insufficient");
                logTO.setData(objTO.toString());
                logTO.setPrimaryKey(objTO.getKeyData());
                logTO.setStatus(objTO.getCommand());
                sqlMap.executeUpdate("insertRentRegisterTO", objTO);
                String rentDetails = "REGISTERED";
                System.out.println("rentDetails==========================" + rentDetails);
                //  objTO.setRentDetails(rentDetails);
                objTO.setRentDetails(rentDetails);
                
                sqlMap.executeUpdate("insertRentDetails", objTO);
                RentTransTO objRentTransTO = new RentTransTO();
                objRentTransTO.setRrId(getRrId());
                objRentTransTO.setRoomNo(objTO.getRoomNo());
                objRentTransTO.setBuildingNo(objTO.getBuidingNo());
                objRentTransTO.setRentDate(objTO.getApplDate());
                objRentTransTO.setTransDate(objTO.getApplDate());
                objRentTransTO.setRentCDate(objTO.getApplDate());
//        objRentTransTO.setRentPFrm(DateUtil.getDateMMDDYYYY(objTO.getApplDate()));
                objRentTransTO.setRentPFrm(objTO.getApplDate());
                String appDate = CommonUtil.convertObjToStr(objTO.getApplDate());
                System.out.println("appDate==========" + appDate);
            //cm=new CommonMethod();

                String incrdDate = null;
                SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
                Calendar cal = Calendar.getInstance();
                cal.setTime(sdf.parse(appDate));
                cal.add(Calendar.MONTH, 1);
                incrdDate = sdf.format(cal.getTime());
                //incrUtildt1 = sdf.parse(incrdDate);

                System.out.println("incrdDate==========" + incrdDate);

                objRentTransTO.setRentPto(DateUtil.getDateMMDDYYYY(incrdDate));

                //objRentTransTO.setRentPto(DateUtil.getDateMMDDYYYY(getAddMonth(appDate)));
                objRentTransTO.setDueAmt(Double.valueOf(0.0));
                objRentTransTO.setRentAmt(objTO.getRentAmt());
                objRentTransTO.setPenelAmt(Double.valueOf(0.0));
                objRentTransTO.setNoticeAmt(Double.valueOf(0.0));
                objRentTransTO.setLegalAmt(Double.valueOf(0.0));
                objRentTransTO.setArbAmt(Double.valueOf(0.0));
                objRentTransTO.setCourtAmt(Double.valueOf(0.0));
                objRentTransTO.setExeAmt(Double.valueOf(0.0));
                objRentTransTO.setClosure("NO");
                objRentTransTO.setClosedDate(null);
                objRentTransTO.setAuthorizeDte(null);
                objRentTransTO.setAuthorizeStatus(null);
                objRentTransTO.setAuthorizeBy(null);
                objRentTransTO.setStatus(CommonConstants.STATUS_CREATED);
                objRentTransTO.setAccNumber(objTO.getRrId());
                objRentTransTO.setRentTotal(objTO.getRentAmt());
                objRentTransTO.setStatusBy(CommonUtil.convertObjToStr(map.get(CommonConstants.USER_ID)));
                objRentTransTO.setStatusDate((Date)currDt.clone());
                System.out.println("objRentTransTO==========================" + objRentTransTO);
                if (map.containsKey("TransactionTO")) {
                LinkedHashMap TransactionDetailsMap = (LinkedHashMap) map.get("TransactionTO");
                if (TransactionDetailsMap != null && TransactionDetailsMap.size() > 0) {
                  sqlMap.executeUpdate("insertRentTransTO", objRentTransTO);
                }
            }
                //  logDAO.addToLog(logTO);          
            }
            sqlMap.commitTransaction();
        } catch (Exception e) {
            sqlMap.rollbackTransaction();
            e.printStackTrace();
            throw new TransRollbackException(e);
        }
    }
//    private void insertRentTrans()
//    {
//        try
//        {
//            System.out.println("hhhhhhhhh");
//        RentTransTO objRentTransTO= new RentTransTO();
//        objRentTransTO.setRrId(getRrId());
//        objRentTransTO.setRoomNo(objTO.getRoomNo());
//        objRentTransTO.setBuildingNo(objTO.getBuidingNo());
//        objRentTransTO.setRentDate(DateUtil.getDateMMDDYYYY(objTO.getApplDate()));
//        objRentTransTO.setTransDate(objTO.getApplDate());
//        objRentTransTO.setRentPFrm(DateUtil.getDateMMDDYYYY(objTO.getApplDate()));
//        String appDate=objTO.getApplDate();
//        //cm=new CommonMethod();
//        
//        
//         //String dDate=getDateddMMyyyy(due_date);
//                String incrdDate=null;
//                SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
//                Calendar cal = Calendar.getInstance();
//                cal.setTime(sdf.parse(appDate));
//                cal.add(Calendar.MONTH, 1);
//                incrdDate = sdf.format(cal.getTime());
//                //incrUtildt1 = sdf.parse(incrdDate);
//        
//        
//        
//        objRentTransTO.setRentPto(DateUtil.getDateMMDDYYYY(incrdDate));
//        objRentTransTO.setDueAmt(Double.valueOf(0.0));
//        objRentTransTO.setRentAmt(objTO.getRentAmt());
//        objRentTransTO.setPenelAmt(Double.valueOf(0.0));
//        objRentTransTO.setNoticeAmt(Double.valueOf(0.0));
//        objRentTransTO.setLegalAmt(Double.valueOf(0.0));
//        objRentTransTO.setArbAmt(Double.valueOf(0.0));
//        objRentTransTO.setCourtAmt(Double.valueOf(0.0));
//        objRentTransTO.setExeAmt(Double.valueOf(0.0));
//        objRentTransTO.setClosure("NO");
//        objRentTransTO.setClosedDate(null);
//        objRentTransTO.setAuthorizeDte(null);
//        objRentTransTO.setAuthorizeStatus(null);
//        objRentTransTO.setAuthorizeBy(null);
//        objRentTransTO.setStatus("CREATED");
//        objRentTransTO.setRentTotal(objTO.getRentAmt());
//         System.out.println("objRentTransTO=========================="+objRentTransTO);
//        sqlMap.executeUpdate("insertRentTransTO", objRentTransTO);
//        }
//        catch(Exception e) {
//            //sqlMap.rollbackTransaction();
//            e.printStackTrace();
//           // throw new TransRollbackException(e);
//        }
//        
//    }

//    public static String getAddMonth(String day)
//   {
//       
//          String dDate=getDateddMMyyyy(due_date);
//                SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
//                Calendar cal = Calendar.getInstance();
//                cal.setTime(sdf.parse(dDate));
//                cal.add(Calendar.YEAR, 1);
//                incrdDate = sdf.format(cal.getTime());
//                incrUtildt1 = sdf.parse(incrdDate);
//       
//       
//       
//       
//         //create Calendar instance
//    Calendar now = Calendar.getInstance();
//   
//    System.out.println("Current date : " + (now.get(Calendar.MONTH) + 1)
//                        + "-"
//                        + now.get(Calendar.DATE)
//                        + "-"
//                        + now.get(Calendar.YEAR));
//   
//    //add months to current date using Calendar.add method
//    now.add(Calendar.MONTH,1);
// 
//    System.out.println("date after 1 months : " + (now.get(Calendar.MONTH) + 1)
//                        + "-"
//                        + now.get(Calendar.DATE)
//                        + "-"
//                        + now.get(Calendar.YEAR));
//          return day+"/"+(now.get(Calendar.MONTH) + 1)+"/"+now.get(Calendar.YEAR);
//   }
//        public String getDateddMMyyyy(java.util.Date strDate1)
//{
//    ////////////////////////////
//     DateFormat df = new SimpleDateFormat("MM/dd/yyyy");
//         
//        // Get the date today using Calendar object.
//       // Date today = Calendar.getInstance().getTime();       
//        // Using DateFormat format method we can create a string
//        // representation of a date with the defined format.
//        String strDate = df.format(strDate1);
//    //////////////////////////////
//    
//    
//    
//    
//    //String strDate=(String)strDate1;
//    SimpleDateFormat dateFormat =null;
//    java.util.Date varDate=null;
//    try
//    {
//        //String strDate="23-Mar-2011";
//    dateFormat = new SimpleDateFormat("MM/dd/yyyy");//
//
//    varDate=(java.util.Date) dateFormat.parse(strDate);
//    dateFormat=new SimpleDateFormat("dd-MM-yyyy");
//    //System.out.println("Date :"+dateFormat.format(varDate));
//
//    }
//    catch(Exception e)
//    {
//        e.printStackTrace();;
//    }
//    return dateFormat.format(varDate);
//}
    private String getRrId() throws Exception {
        IDGenerateDAO dao = new IDGenerateDAO();
        final HashMap where = new HashMap();
        where.put(CommonConstants.MAP_WHERE, "RTID");
        // return "";
//        HashMap map=generateID();
        //System.out.println("MAP IN DAOOOOOO=========="+map);
//        return (String) map.get(CommonConstants.DATA);
        return (String) (dao.executeQuery(where)).get(CommonConstants.DATA);
    }

    private void doRentRegisterTransactions(HashMap map) throws Exception, Exception {
        try {
            System.out.println("!@#$@# Borrowings to :" + objTO);
            if (objTO.getCommand() != null) {
                if (objTO.getCommand().equals(CommonConstants.TOSTATUS_INSERT)) {
                    System.out.println("!@#!@#@# !ID GENERATED:" + objTO.getRrId());
                    double amtIndend = CommonUtil.convertObjToDouble(objTO.getRentAmt()).doubleValue();
                    double advAmount = CommonUtil.convertObjToDouble(objTO.getAdvAmt()).doubleValue();
                    //   System.out.println("@#$ amtBorrowed :"+amtBorrowed);
                    HashMap txMap;
                    HashMap whereMap = new HashMap();
                    whereMap.put("BUILDING_NUM", objTO.getBuidingNo());
                    HashMap acHeads = null;
                    System.out.println("IN&%%%%%%%%%%%%%%%%%%%%%%%%%=====" + objTO.getBuidingNo());

                    acHeads = (HashMap) sqlMap.executeQueryForObject("RentRegister.getAcHeads", whereMap);

                    if (acHeads == null || acHeads.size() == 0) {
                        throw new TTException("Account heads not set properly...");
                    }
                    TransferTrans objTransferTrans = new TransferTrans();

                    objTransferTrans.setInitiatedBranch(_branchCode);

                    objTransferTrans.setLinkBatchId(objTO.getRrId());
                    txMap = new HashMap();
                    ArrayList transferList = new ArrayList();

                    LinkedHashMap TransactionDetailsMap = (LinkedHashMap) map.get("TransactionTO");
                    System.out.println("TransactionDetailsMap---68687687687667868767866->" + TransactionDetailsMap);
                    if (TransactionDetailsMap!=null && TransactionDetailsMap.size() > 0) {
                        if (TransactionDetailsMap.containsKey("NOT_DELETED_TRANS_TOs")) {
                            LinkedHashMap allowedTransDetailsTO = (LinkedHashMap) TransactionDetailsMap.get("NOT_DELETED_TRANS_TOs");
                            TransactionTO objTransactionTO = null;
                            System.out.println("@#$@#$#$allowedTransDetailsTO" + allowedTransDetailsTO);
                            for (int J = 1; J <= allowedTransDetailsTO.size(); J++) {
                                objTransactionTO = (TransactionTO) allowedTransDetailsTO.get(String.valueOf(J));
                                //                                   System.out.println("objTransactionTO---->"+objTransactionTO);

                                double debitAmt = CommonUtil.convertObjToDouble(objTransactionTO.getTransAmt()).doubleValue();
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
                                                returnMap = new HashMap();
                                                returnMap.put("INSUFFICIENT_BALANCE", "INSUFFICIENT_BALANCE");
                                                insuffBal = true;
                                                return;
                                            }
                                        }
                                    }

                                    System.out.println("@#$@#$#$allowedTransDetailsTOoooooooooooooooooooooooooooooooooooooooooooooooooo");
                                    txMap.put(TransferTrans.PARTICULARS, "To " + objTO.getRoomNo() + " ");
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
                                    }else{
                                        txMap.put(TransferTrans.DR_BRANCH, _branchCode);
                                    }
                                    txMap.put(TransferTrans.CR_BRANCH, _branchCode);
                                    txMap.put(TransferTrans.CR_PROD_TYPE, TransactionFactory.GL);
                                    if (objTransactionTO.getProductType().equals("GL")) {
                                        txMap.put(TransferTrans.DR_AC_HD, CommonUtil.convertObjToStr(objTransactionTO.getDebitAcctNo()));
                                        txMap.put(TransferTrans.DR_BRANCH, _branchCode);
                                    } else {
                                        txMap.put(TransferTrans.DR_ACT_NUM, CommonUtil.convertObjToStr(objTransactionTO.getDebitAcctNo()));
                                        txMap.put(TransferTrans.DR_PROD_ID, CommonUtil.convertObjToStr(objTransactionTO.getProductId()));
                                    }
                                    txMap.put(TransferTrans.DR_PROD_TYPE, CommonUtil.convertObjToStr(objTransactionTO.getProductType()));
                                    transferList.add(objTransferTrans.getDebitTransferTO(txMap, debitAmt));
                                    System.out.println("CREDIT AC_HD IN vamtIndend================" + amtIndend + "acHeads=" + acHeads);
                                    if (amtIndend > 0.0) {
                                        txMap.put(TransferTrans.CR_AC_HD, (String) acHeads.get("RENT_AC_HD_ID"));
                                        txMap.put("AUTHORIZEREMARKS", "RENT_AC_HD_ID");
                                        transferList.add(objTransferTrans.getCreditTransferTO(txMap, amtIndend));
                                    }
                                    if (advAmount > 0.0) {
                                        txMap.put(TransferTrans.CR_AC_HD, (String) acHeads.get("ADV_RENT_ACT_HD_ID"));
                                        txMap.put("AUTHORIZEREMARKS", "ADV_RENT_ACT_HD_ID");
                                        transferList.add(objTransferTrans.getCreditTransferTO(txMap, advAmount));
                                    }
                                    System.out.println("_branchCode======================" + _branchCode);
                                    System.out.println("transferList===================" + transferList);
                                    objTransferTrans.doDebitCredit(transferList, _branchCode, false);
                                } else {
                                    double transAmt;
                                    //  TransactionTO transTO = new TransactionTO();
                                    CashTransactionTO objCashTO = new CashTransactionTO();
                                    ArrayList cashList = new ArrayList();
                                    if (CommonUtil.convertObjToDouble(objTO.getRentAmt()).doubleValue() > 0) {
                                        String grpHead = "";
                                        grpHead = (String) acHeads.get("RENT_AC_HD_ID");
                                        objCashTO.setTransId("");
                                        objCashTO.setProdType(TransactionFactory.GL);
                                        objCashTO.setTransType(CommonConstants.CREDIT);
                                        objCashTO.setInitTransId(logTO.getUserId());
                                        objCashTO.setBranchId(_branchCode);
                                        objCashTO.setStatusBy(logTO.getUserId());
                                        objCashTO.setStatusDt(currDt);
//                                        objCashTO.setTokenNo(CommonUtil.convertObjToStr(paramMap.get("TOKEN_NO")));
                                        objCashTO.setInstrumentNo1("ENTERED_AMOUNT");
                                        objCashTO.setParticulars("By " + objTO.getRoomNo() + " RentRegister");
                                        objCashTO.setInitiatedBranch(_branchCode);
                                        objCashTO.setInitChannType(CommonConstants.CASHIER);
                                        objCashTO.setCommand(CommonConstants.TOSTATUS_INSERT);
                                        objCashTO.setAcHdId(grpHead);
                                        objCashTO.setInpAmount(objTO.getRentAmt());
                                        objCashTO.setAmount(objTO.getRentAmt());
                                        objCashTO.setLinkBatchId(objTO.getRrId());
                                        objCashTO.setTransModType("RT");
                                        System.out.println("line no 465^^^^^^^");
                                        txMap = new HashMap();
                                        txMap.put(CommonConstants.BRANCH_ID, _branchCode);
                                        txMap.put(CommonConstants.PRODUCT_ID, TransactionFactory.GL);
                                        //     txMap.put(CommonConstants.USER_ID, objDrfTransactionTO.getStatusBy());
                                        txMap.put("TRANS_TYPE", CommonConstants.CREDIT);
                                        //if(drfAmt>0.0){
                                        System.out.println("debitAmt IOOOOOOOOOOOOOOOOO^^^^^^=" + debitAmt + " acHeads===" + acHeads);
                                        if (debitAmt > 0.0) {
                                            //txMap.put(CommonConstants.AC_HD_ID, (String)acHeads.get("LIABILITY_HEAD"));
                                            // txMap.put("AUTHORIZEREMARKS","LIABILITY_HEAD");
                                            txMap.put(CommonConstants.AC_HD_ID, grpHead);
                                            txMap.put("AUTHORIZEREMARKS", "RENT_AC_HD_ID");
                                            txMap.put("AMOUNT", new Double(debitAmt));
                                            // txMap.put("LINK_BATCH_ID", objTransferTrans.getLinkBatchId());
                                            txMap.put("LINK_BATCH_ID", objTO.getRrId());
                                        }
                                        cashList.add(objCashTO);
                                        if (CommonUtil.convertObjToDouble(objTO.getAdvAmt()) > 0) {
                                            String gpHead = "";
                                            gpHead = (String) acHeads.get("ADV_RENT_ACT_HD_ID");
                                            objCashTO = new CashTransactionTO();
                                            objCashTO.setTransId("");
                                            objCashTO.setProdType(TransactionFactory.GL);
                                            objCashTO.setTransType(CommonConstants.CREDIT);
                                            objCashTO.setInitTransId(logTO.getUserId());
                                            objCashTO.setBranchId(_branchCode);
                                            objCashTO.setStatusBy(logTO.getUserId());
                                            objCashTO.setStatusDt(currDt);
                                            objCashTO.setInstrumentNo1("ENTERED_AMOUNT");
                                            objCashTO.setParticulars("By " + objTO.getRoomNo() + " RentRegister");
                                            objCashTO.setInitiatedBranch(_branchCode);
                                            objCashTO.setInitChannType(CommonConstants.CASHIER);
                                            objCashTO.setCommand(CommonConstants.TOSTATUS_INSERT);
                                            objCashTO.setAcHdId(gpHead);
                                            objCashTO.setInpAmount(objTO.getAdvAmt());
                                            objCashTO.setAmount(objTO.getAdvAmt());
                                            objCashTO.setLinkBatchId(objTO.getRrId());
                                            System.out.println("Advnc amount here" + objTO.getAdvAmt());
                                            System.out.println("line no 465^^^^^^^");
                                            txMap = new HashMap();
                                            txMap.put(CommonConstants.BRANCH_ID, _branchCode);
                                            txMap.put(CommonConstants.PRODUCT_ID, TransactionFactory.GL);
                                            txMap.put("TRANS_TYPE", CommonConstants.CREDIT);
                                            System.out.println("debitAmt IOOOOOOOOOOOOOOOOO^^^^^^=" + debitAmt + " acHeads===" + acHeads);
                                            if (debitAmt > 0.0) {
                                                txMap.put(CommonConstants.AC_HD_ID, gpHead);
                                                txMap.put("AUTHORIZEREMARKS", "ADV_RENT_ACT_HD_ID");
                                                txMap.put("AMOUNT", new Double(debitAmt));
                                                // txMap.put("LINK_BATCH_ID", objTransferTrans.getLinkBatchId());
                                                txMap.put("LINK_BATCH_ID", objTO.getRrId());
                                            }
                                            cashList.add(objCashTO);
                                        }
                                        System.out.println("cashList---------------->" + cashList);
                                        HashMap tranMap = new HashMap();
                                        tranMap.put(CommonConstants.BRANCH_ID, CommonUtil.convertObjToStr(map.get("BRANCH_CODE")));
                                        tranMap.put(CommonConstants.USER_ID, CommonUtil.convertObjToStr(map.get("USER_ID")));
                                        tranMap.put(CommonConstants.IP_ADDR, CommonUtil.convertObjToStr(map.get("IP_ADDR")));
                                        tranMap.put(CommonConstants.MODULE, CommonUtil.convertObjToStr(map.get("MODULE")));
                                        tranMap.put(CommonConstants.SCREEN, CommonUtil.convertObjToStr(map.get("SCREEN")));
                                        tranMap.put("MODE", CommonConstants.TOSTATUS_INSERT);
                                        tranMap.put("DAILYDEPOSITTRANSTO", cashList);
                                        CashTransactionDAO cashDao;
                                        cashDao = new CashTransactionDAO();
                                        System.out.println("tranMap BEFOREE EXECUTE---------------->" + tranMap);
                                        tranMap = cashDao.execute(tranMap, false);
                                        System.out.println("tranMap AFTER EXECUTE---------------->" + tranMap);
                                        cashDao = null;
                                        tranMap = null;
                                    }
                                }
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
                    amtIndend = 0.0;
                    objTransferTrans = null;
                    transferList = null;
                    txMap = null;
                    // Code End
                    getTransDetails(objTO.getRrId());
                } else {
                    HashMap shareAcctNoMap = new HashMap();
                    double amtIndend = CommonUtil.convertObjToDouble(objTO.getRentAmt()).doubleValue();
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

    private void updateData() throws Exception {
        try {
            sqlMap.startTransaction();
            objTO.setStatus(CommonConstants.STATUS_MODIFIED);
            //logTO.setData(objTO.toString());
            //  logTO.setPrimaryKey(objTO.getKeyData());
            // logTO.setStatus(objTO.getCommand());
            //sqlMap.executeUpdate("updateIndendTO", objTO);
            sqlMap.executeUpdate("updateRentRegisterTO", objTO);
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
//            sqlMap.executeUpdate("deleteIndendTO", objTO);
            sqlMap.executeUpdate("deleteRentRegisterTO", objTO);
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
            RentRegisterDAO dao = new RentRegisterDAO();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public HashMap execute(HashMap map) throws Exception {
        insuffBal = false;
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
        objTO = (RentRegisterTO) map.get("RentRegisterTO");
        if (!map.containsKey(CommonConstants.AUTHORIZEMAP)) {
//            objTO = (RentRegisterTO) map.get("RentRegisterTO");
            final String command = objTO.getCommand();
            if (command.equals(CommonConstants.TOSTATUS_INSERT)) {
                insertData(map);
                //            returnMap = new HashMap();
                System.out.println("objTO.getStrIRNo()objTO.getStrIRNo()objTO.getStrIRNo()====:" + objTO);
                returnMap.put("RRID", objTO.getRrId());
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
            if (authMap != null) {
                authorize(authMap);
            }
        }

        destroyObjects();
        System.out.println("@#@#@# returnMap:" + returnMap);
        return returnMap;
    }

    private void authorize(HashMap map) throws Exception {
        String status = (String) map.get("STATUS");
        String linkBatchId = null;
        String disbursalNo = null;
        HashMap cashAuthMap;
        TransactionTO objTransactionTO = null;
        try {
            sqlMap.startTransaction();
            transactionDAO = new TransactionDAO(CommonConstants.DEBIT);
            System.out.println("map:" + map);
            disbursalNo = CommonUtil.convertObjToStr(map.get("IRNO"));
            map.put(CommonConstants.STATUS, status);
            map.put(CommonConstants.USER_ID, map.get(CommonConstants.USER_ID));
            map.put("CURR_DATE", currDt);
            System.out.println("status------------>" + status);
            sqlMap.executeUpdate("authorizeRentRegister", map);  
            linkBatchId = CommonUtil.convertObjToStr(map.get("RRID"));//Transaction Batch Id
            sqlMap.executeUpdate("authorizeRentTrans", map);  
            if (status.equals(CommonConstants.STATUS_REJECTED)) {
                objTO.setRentDetails("Vacant");
                sqlMap.executeUpdate("insertRentDetails", objTO);
            }
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
//            HashMap transMap = new HashMap();
//            transMap.put("LINK_BATCH_ID", linkBatchId);
//            System.out.println("transMap----------------->" + transMap);
//            sqlMap.executeUpdate("updateInstrumentNO1Transfer", transMap);
//            sqlMap.executeUpdate("updateInstrumentNO1Cash", transMap);
//            transMap = null;
//            System.out.println("disbursalNo----------------->" + disbursalNo);
//            objTransactionTO = new TransactionTO();
//            objTransactionTO.setBatchId(CommonUtil.convertObjToStr(disbursalNo));
//            objTransactionTO.setTransId(CommonUtil.convertObjToStr(linkBatchId));
//            objTransactionTO.setBranchId(_branchCode);
//            System.out.println("objTransactionTO----------------->" + objTransactionTO);
//            sqlMap.executeUpdate("deleteRemittanceIssueTransactionTO", objTransactionTO);
//            if (!status.equals("REJECTED")) {
//            }
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
