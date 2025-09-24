/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition. 
 * 
 * TradingDAO.java
 *
 * Created on 16 September, 2011, 4:52 PM
 */

package com.see.truetransact.serverside.trading;
import java.util.List;
import java.util.ArrayList;

import java.util.HashMap;
import java.util.LinkedHashMap;
import com.ibatis.db.sqlmap.SqlMap;
import com.see.truetransact.serverutil.ServerUtil;
//import com.see.truetransact.transferobject.TOHeader;
import com.see.truetransact.commonutil.CommonConstants;
import com.see.truetransact.commonutil.NoCommandException;
import com.see.truetransact.serverside.TTDAO;
import com.see.truetransact.serverutil.ServerConstants;
import com.see.truetransact.servicelocator.ServiceLocator;
import com.see.truetransact.serverexception.TransRollbackException;
import com.see.truetransact.serverexception.ServiceLocatorException;
import com.see.truetransact.commonutil.CommonUtil;
import com.see.truetransact.commonutil.DateUtil;
import com.see.truetransact.serverutil.ServerUtil;
import com.see.truetransact.serverside.common.log.LogDAO;
import com.see.truetransact.serverside.common.transaction.TransferTrans;
import com.see.truetransact.serverside.transaction.cash.CashTransaction;
import com.see.truetransact.commonutil.TTException;
import com.see.truetransact.transferobject.common.transaction.TransactionTO;
import com.see.truetransact.serverside.transaction.cash.CashTransactionDAO;
import com.see.truetransact.serverside.common.transaction.TransactionDAO;
import com.see.truetransact.transferobject.transaction.cash.CashTransactionTO;
import com.see.truetransact.serverside.transaction.common.TransactionFactory;
import com.see.truetransact.transferobject.common.log.LogTO;
import com.see.truetransact.serverside.common.log.LogDAO;
import java.util.Date;
import com.see.truetransact.transferobject.trading.TradingTO;
/**
 *
 * @author  aravind
 */
public class TradingDAO  extends TTDAO{
    
    private static SqlMap sqlMap = null;
    private java.util.Properties dataBaseProperties = new java.util.Properties();
    private String DB_DRIVER_NAME = "";
    private TradingTO objTO;
    public HashMap mapTrans=new HashMap();
    private LogDAO logDAO;
    private LogTO logTO;
    private Date currDt;
    private ArrayList tradingList;
    
    /** Creates a new instance of TradingDAO */
    public TradingDAO() throws ServiceLocatorException {
       // ServiceLocator locate = ServiceLocator.getInstance();
       // sqlMap = (SqlMap) locate.getDAOSqlMap();
  
      //  System.out.println("--------- TradingDAO");
        ServiceLocator locate = ServiceLocator.getInstance();
        sqlMap = (SqlMap) locate.getDAOSqlMap();
        try {
            dataBaseProperties.load(this.getClass().getResourceAsStream("/com/see/truetransact/serverutil/SqlMapConfig.properties"));
            DB_DRIVER_NAME = dataBaseProperties.getProperty("driver");
        } catch (Exception ex) {
            
        }        
      //  System.out.println("#$#$#$TradingDAO() sqlMap.getCurrentDataSourceName() : "+DB_DRIVER_NAME);
  
    
    }
    
    
    
      private void insertData() throws Exception {
        try {
            sqlMap.startTransaction();
            
            //System.out.println("#$#$ tradingList:"+tradingList);
            if (tradingList!=null && tradingList.size()>0) {
                ArrayList transfersList = new ArrayList();
                for (int i=0;i<tradingList.size();i++) {
                    objTO = (TradingTO) tradingList.get(i);
                    if (objTO.getMode().equals("CASH")) {
                        doTradingCashTransaction(objTO);
                        tradingList.remove(i);
                        i = -1;
                    } else {
                        String batchId = objTO.getLinkBATCHID();
                        transfersList = getBatch(batchId, tradingList);
                        System.out.println("#$#$ Transfer list:"+transfersList);
                        doTradingTransferTranactions(transfersList);
                        i = 0;
                    }
                }
              //  System.out.println("#$#$ tradingList 2:"+tradingList);
            }
             System.out.println("---TradingDAO()-Going to exe insertTradingTO--"+objTO.getTransID()+":"+objTO.getAchdID());
            //            if(objTO.getCashList().size()>0) {
            //                for(int i=0;i<objTO.getCashList().size();i++){
            //                    System.out.println("arrrrLiiist in cash"+objTO.getMode());
            //                    objTO=((TradingTO)(objTO.getCashList().get(i)));
            //                    objTO.setTransID(generateID("CASH_TRANS_ID"));
            //                    objTO.setLinkBATCHID(generateID("CASH_TRANS_ID"));
            //                    System.out.println("cashAmount"+((TradingTO)(objTO.getCashList().get(i))).getAMOUNT());
            //                    doTradingTranactions(objTO);
            //                }
            //            }
            //
            //
            //            if(objTO.getTransList().size()>0) {
            //
            //                System.out.println("arrrrLiiist in transfer"+objTO.getTransList().size());
            //                for(int i=0;i<objTO.getTransList().size();i++){
            //                    objTO=((TradingTO)(objTO.getTransList().get(i)));
            //                    objTO.setTransID(generateID("TRANSFER_TRANS_ID"));
            //
            //                    if(!mapTrans.containsKey(objTO.getAchdID())) {
            //                        mapTrans.put(objTO.getAchdID(), generateID("TRANSFER.BATCH_ID"));
            //                    }
            //                    String value=mapTrans.get(objTO.getAchdID()).toString();
            //
            //                    objTO.setBatch_id(value);
            //
            //                }
            //
            //                doTradingTranactions(objTO);
            //            }
            //            HashMap map;
            //
            //            System.out.println("---TradingDAO()-After to exe insertTradingTO--"+objTO.getTransID());
        
            sqlMap.commitTransaction();
             System.out.println("---TradingDAO()-After commitTransaction--");
        } catch (Exception e) {
            sqlMap.rollbackTransaction();
            e.printStackTrace();
            throw new TransRollbackException(e);
        }
    }
    
    private static ArrayList getBatch(String batch, ArrayList transList) {
        String id="";
        ArrayList returnList = new ArrayList();
        TradingTO transferTO=null;
        for (int i=0; i<transList.size();) {
            transferTO = (TradingTO) transList.get(i);
            id=transferTO.getLinkBATCHID();
            
            if (batch.equals(id)) {
                returnList.add(transList.get(i));
                transList.remove(i);
                i=-1;
            }
            i=i+1;
        }
        return returnList;
        
    }
        
//     private String getTransId(String key) throws Exception{
//      //  final IDGenerateDAO dao = new IDGenerateDAO();
//        final HashMap where = new HashMap();
//        where.put(CommonConstants.MAP_WHERE,key);
//       // return "";
//        HashMap map=generateID();
//        return (String) map.get(CommonConstants.DATA); 
//    }
// private String getBatchId(String key) throws Exception{
//      //  final IDGenerateDAO dao = new IDGenerateDAO();
//        final HashMap where = new HashMap();
//        where.put(CommonConstants.MAP_WHERE,key);
//       // return "";
//        HashMap map=generateID();
//        return (String) map.get(CommonConstants.DATA); 
//    }
//     
//       private String getTradingNo() throws Exception{
//      //  final IDGenerateDAO dao = new IDGenerateDAO();
//        final HashMap where = new HashMap();
//        where.put(CommonConstants.MAP_WHERE, "TRADING_NO");
//       // return "";
//        HashMap map=generateID();
//        return (String) map.get(CommonConstants.DATA); 
//    }
//    private String getBatchNo() throws Exception{
//      //  final IDGenerateDAO dao = new IDGenerateDAO();
//        final HashMap where = new HashMap();
//        where.put(CommonConstants.MAP_WHERE, "TRANSFER.BATCH_ID");
//       // return "";
//        HashMap map=generateID();
//        return (String) map.get(CommonConstants.DATA); 
//    }
    
    public String generateID(String key) {
           
            HashMap hash = null,result=null;
            String genID = "";
        try {
            String mapName = "getCurrentID";
            HashMap where = new HashMap();
            where.put("ID_KEY", key); //Here u have to pass BORROW_ID or something else
            List list = null;
            
            //sqlMap.startTransaction();
            sqlMap.executeUpdate("updateIDGenerated", where);  // This update statement just updates curr_value=curr_value+1
            list = (List) sqlMap.executeQueryForList(mapName, where);  // This will get u the updated curr_value, prefix and length
            //sqlMap.commitTransaction();

            if (list.size() > 0) {
                hash = (HashMap) list.get(0);
                String strPrefix="", strLen="";

                // Prefix for the ID.
                if (hash.containsKey("PREFIX")) {
                    strPrefix = (String) hash.get("PREFIX");
                    if (strPrefix == null || strPrefix.trim().length() == 0) strPrefix = "";
                }

                // Maximum Length for the ID
                int len=10;
                if (hash.containsKey("ID_LENGTH")) {
                    strLen = String.valueOf(hash.get("ID_LENGTH"));
                    if (strLen == null || strLen.trim().length() == 0) len = 10;
                    else len = Integer.parseInt(strLen.trim());
                }

                int numFrom = strPrefix.trim().length();

                String newID = String.valueOf(hash.get("CURR_VALUE"));

                // Number Part of the String and incrementing 1 (ex. only 00085 from OGGOT00085)
                result = new HashMap();
                genID = strPrefix.toUpperCase() + CommonUtil.lpad(newID, len - numFrom, '0');
                result.put(CommonConstants.DATA, genID);
            }
            }
        catch(Exception e) {
                e.printStackTrace();
            }
            return genID;
       }
    
    private void doTradingCashTransaction(TradingTO objTO) throws Exception{
        
       // System.out.println("!@#$@# trading324324 to :"+objTO);
        try {
            //System.out.println("!@#$@# trading to :"+map);
            if (objTO.getCommand()!=null){
                if (objTO.getCommand().equals(CommonConstants.TOSTATUS_INSERT)){
                    double amt = CommonUtil.convertObjToDouble(objTO.getAMOUNT()).doubleValue() ;
                    HashMap whereMap = new HashMap();
                    double transAmt;
                    //  TransactionTO transTO = new TransactionTO();
                    CashTransactionTO objCashTO= new CashTransactionTO();
                    ArrayList cashList1 = new ArrayList();
                    if(CommonUtil.convertObjToDouble(objTO.getAMOUNT()).doubleValue() > 0){
                        objCashTO.setTransId(objTO.getTransID());
                        objCashTO.setProdType(objTO.getProdTYPE());
                        objCashTO.setTransType(CommonConstants.CREDIT);
                        objCashTO.setInitTransId(objTO.getStatusBY());
                        objCashTO.setBranchId(_branchCode);
                        objCashTO.setStatusBy(objTO.getStatusBY());
                        objCashTO.setStatusDt(objTO.getStatusDT());
                        //                                        objCashTO.setTokenNo(CommonUtil.convertObjToStr(paramMap.get("TOKEN_NO")));
                        objCashTO.setAuthorizeStatus_2("ENTERED_AMOUNT");
                        objCashTO.setParticulars("By "+objTO.getPARTICULARS());
                        objCashTO.setInitiatedBranch(_branchCode);
                        objCashTO.setInitChannType(CommonConstants.CASHIER);
                        objCashTO.setCommand(CommonConstants.TOSTATUS_INSERT);
                        objCashTO.setAcHdId(objTO.getAchdID());
                        objCashTO.setInpAmount(objTO.getAMOUNT());
                        objCashTO.setAmount(objTO.getAMOUNT());
                        objCashTO.setLinkBatchId(objTO.getLinkBATCHID());
                        objCashTO.setStatus(CommonConstants.STATUS_CREATED);
                      //  System.out.println("objCashTO^^^^^^^"+objCashTO);
                        cashList1.add(objCashTO);
                       // System.out.println("cashList1---------------->"+cashList1);
                        HashMap tranMap=new HashMap();
                        tranMap.put(CommonConstants.BRANCH_ID, _branchCode);
                        tranMap.put(CommonConstants.USER_ID,objTO.getStatusBY());
                        tranMap.put(CommonConstants.IP_ADDR,(""));
                        tranMap.put(CommonConstants.MODULE,(""));
                        tranMap.put(CommonConstants.SCREEN,(""));
                        
                        HashMap cashMap = new HashMap();
                        cashMap.putAll(tranMap);
                        tranMap.put("MODE",CommonConstants.TOSTATUS_INSERT);
                        tranMap.put("DAILYDEPOSITTRANSTO",cashList1);
                        tranMap.put(CommonConstants.AUTHORIZESTATUS, CommonConstants.STATUS_AUTHORIZED);
                        CashTransactionDAO cashDao ;
                        cashDao = new CashTransactionDAO() ;
                        tranMap.putAll(cashDao.execute(tranMap, false));
                        tranMap.put("INITIATED_BRANCH", _branchCode);
                        ArrayList arrList =new ArrayList();
                        arrList.add(tranMap);
                        HashMap authMap = new HashMap();
                        authMap.put(CommonConstants.AUTHORIZEDATA, arrList);
                        authMap.put(CommonConstants.AUTHORIZESTATUS, CommonConstants.STATUS_AUTHORIZED);
                        
                        cashMap.put(CommonConstants.AUTHORIZEMAP, authMap);
                        System.out.println("#!@ cashMap:"+cashMap);
                        cashDao.execute(cashMap, false);
                        cashDao = null ;
                        tranMap = null ;
                    }
                    //End cash
                    System.out.println("ssssss"+CommonConstants.STATUS_CREATED);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
    }
    
    private void doTradingTransferTranactions(ArrayList objTOs) throws Exception{
        
        try {
            //System.out.println("!@#$@# trading to :"+map);
            
            HashMap whereMap = new HashMap();
            TransferTrans objTransferTrans = new TransferTrans();
            
            objTransferTrans.setInitiatedBranch(_branchCode);
            
            objTransferTrans.setLinkBatchId(objTO.getLinkBATCHID());
            HashMap txMap;
            ArrayList transferList = new ArrayList();
            
            for (int i=0;i<objTOs.size(); i++) {
                objTO = (TradingTO) objTOs.get(i);
                System.out.println("!@#$@# trading324324 to :"+objTO);
                txMap = new HashMap();
                double amt= CommonUtil.convertObjToDouble(objTO.getAMOUNT()).doubleValue();
                
                if (amt>0) {
                    txMap.put(TransferTrans.PARTICULARS, "To "+objTO.getPARTICULARS());
                    txMap.put(CommonConstants.USER_ID, objTO.getStatusBY());
                    txMap.put(TransferTrans.AUTHORIZE_STATUS_2,"ENTERED_AMOUNT");
                    txMap.put("DR_INST_TYPE","VOUCHER");
                    
                    if(objTO.getTransTYPE().equals("DEBIT")){
                        if (objTO.getProdTYPE().equals("GL")) {
                            txMap.put(TransferTrans.DR_AC_HD,CommonUtil.convertObjToStr(objTO.getAchdID()));
                            
                        } else {
                            txMap.put(TransferTrans.DR_ACT_NUM,CommonUtil.convertObjToStr(objTO.getActNUM()));
                            txMap.put(TransferTrans.DR_PROD_ID, CommonUtil.convertObjToStr(objTO.getProdID()));
                        }
                        txMap.put(TransferTrans.DR_BRANCH, _branchCode);
                        txMap.put(TransferTrans.DR_PROD_TYPE, CommonUtil.convertObjToStr(objTO.getProdTYPE()));
                        transferList.add(objTransferTrans.getDebitTransferTO(txMap, amt));
                    }
                    
                    if(objTO.getTransTYPE().equals("CREDIT")){
                        if (objTO.getProdTYPE().equals("GL")) {
                            txMap.put(TransferTrans.CR_AC_HD,CommonUtil.convertObjToStr(objTO.getAchdID()));
                            
                        } else {
                            txMap.put(TransferTrans.CR_ACT_NUM,CommonUtil.convertObjToStr(objTO.getActNUM()));
                            txMap.put(TransferTrans.CR_PROD_ID, CommonUtil.convertObjToStr(objTO.getProdID()));
                        }
                        txMap.put(TransferTrans.CR_BRANCH, _branchCode);
                        txMap.put(TransferTrans.CR_PROD_TYPE, objTO.getProdTYPE());
                        txMap.put("AUTHORIZEREMARKS",objTO.getAchdID());
                        //                                                transferList.add(objTransferTrans.getDebitTransferTO(txMap, shareAmt));
                        transferList.add(objTransferTrans.getCreditTransferTO(txMap, amt));
                        //                                                objTransferTrans.doDebitCredit(transferList, _branchCode, false);
                    }
                }
            }
            System.out.println("Transfer List Final:"+transferList);
            objTransferTrans.doDebitCredit(transferList, _branchCode, false);
            
            //End cash
            System.out.println("ssssss"+CommonConstants.STATUS_CREATED);
            objTransferTrans = null;
            transferList = null;
            txMap = null;
            // Code End
            //getTransDetails(objTO.getDisbursalNo());
            
            
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
    }
    
    
    //copied from borrowings to update the gl table      //
    private void doTradingTranactions(TradingTO objTO) throws Exception{
        
        //System.out.println("!@#$@# trading324324 to :"+objTO);
        try {
            //System.out.println("!@#$@# trading to :"+map);
            if (objTO.getCommand()!=null){
                if (objTO.getCommand().equals(CommonConstants.TOSTATUS_INSERT)){
                    double amt = CommonUtil.convertObjToDouble(objTO.getAMOUNT()).doubleValue() ;
                    HashMap whereMap = new HashMap();
                    // whereMap.put("BORROWING_NO", objTO.getBorrowingNo());
                    //  HashMap acHeads = (HashMap)sqlMap.executeQueryForObject("Borrowings.getAcHeads", whereMap);
                    
                    //   if (acHeads==null || acHeads.size()==0) {
                    //       throw new TTException("Account heads not set properly...");
                    //   }
                    TransferTrans objTransferTrans = new TransferTrans();
                    
                    objTransferTrans.setInitiatedBranch(_branchCode);
                    
                    objTransferTrans.setLinkBatchId(objTO.getLinkBATCHID());
                    HashMap txMap;
                    txMap = new HashMap();
                    ArrayList transferList = new ArrayList();
                    
                    // LinkedHashMap TransactionDetailsMap = (LinkedHashMap) map.get("TransactionTO");
                    //                            System.out.println("TransactionDetailsMap---->"+TransactionDetailsMap);
                    // if (TransactionDetailsMap.size()>0) {
                    //if(TransactionDetailsMap.containsKey("NOT_DELETED_TRANS_TOs")) {
                    // LinkedHashMap allowedTransDetailsTO = (LinkedHashMap)TransactionDetailsMap.get("NOT_DELETED_TRANS_TOs");
                    TransactionTO objTransactionTO = null;
                    //System.out.println("@#$@#$#$allowedTransDetailsTO"+allowedTransDetailsTO);
                    // for (int J = 1;J <= allowedTransDetailsTO.size();J++){
                    // objTransactionTO = (TransactionTO) allowedTransDetailsTO.get(String.valueOf(J));
                    //                                   System.out.println("objTransactionTO---->"+objTransactionTO);
                    
                    
                    double debitAmt= CommonUtil.convertObjToDouble(objTO.getAMOUNT()).doubleValue();
                    if(objTO.getMode().equals("TRANSFER")){
                        //                                            txMap.put(TransferTrans.DR_AC_HD, (String)acHeads.get("SHARE_ACHD"));
                        txMap.put(TransferTrans.PARTICULARS, "To "+objTO.getPARTICULARS());
                        txMap.put(CommonConstants.USER_ID, objTO.getStatusBY());
                        txMap.put(TransferTrans.DR_BRANCH, _branchCode);
                        txMap.put(TransferTrans.AUTHORIZE_STATUS_2,"ENTERED_AMOUNT");
                        txMap.put("DR_INST_TYPE","VOUCHER");
                        txMap.put(TransferTrans.CR_BRANCH, _branchCode);
                        txMap.put(TransferTrans.CR_PROD_TYPE, objTO.getProdTYPE());
                        
                        if (objTO.getProdTYPE().equals("GL")) {
                            txMap.put(TransferTrans.DR_AC_HD,CommonUtil.convertObjToStr(objTO.getAchdID()));
                            
                        } else {
                            txMap.put(TransferTrans.DR_ACT_NUM,CommonUtil.convertObjToStr(objTO.getActNUM()));
                            txMap.put(TransferTrans.DR_PROD_ID, CommonUtil.convertObjToStr(objTO.getProdID()));
                        }
                        txMap.put(TransferTrans.DR_PROD_TYPE, CommonUtil.convertObjToStr(objTO.getProdTYPE()));
                        transferList.add(objTransferTrans.getDebitTransferTO(txMap, debitAmt));
                        //                                            transferList.add(objTransferTrans.getCreditTransferTO(txMap, shareAmt));
                        //                                            objTransferTrans.doDebitCredit(transferList, _branchCode, false);
                        if(amt>0.0){
                            txMap.put(TransferTrans.CR_AC_HD, objTO.getAchdID());
                            txMap.put("AUTHORIZEREMARKS",objTO.getAchdID());
                            //                                                transferList.add(objTransferTrans.getDebitTransferTO(txMap, shareAmt));
                            transferList.add(objTransferTrans.getCreditTransferTO(txMap, amt));
                            //                                                objTransferTrans.doDebitCredit(transferList, _branchCode, false);
                        }
                        objTransferTrans.doDebitCredit(transferList, _branchCode, false);
                    } else {
                        double transAmt;
                        //  TransactionTO transTO = new TransactionTO();
                        CashTransactionTO objCashTO= new CashTransactionTO();
                        ArrayList cashList1 = new ArrayList();
                        if(CommonUtil.convertObjToDouble(objTO.getAMOUNT()).doubleValue() > 0){
                            objCashTO.setTransId(objTO.getTransID());
                            objCashTO.setProdType(objTO.getProdTYPE());
                            objCashTO.setTransType(CommonConstants.CREDIT);
                            objCashTO.setInitTransId(objTO.getStatusBY());
                            objCashTO.setBranchId(_branchCode);
                            objCashTO.setStatusBy(objTO.getStatusBY());
                            objCashTO.setStatusDt(objTO.getStatusDT());
                            //                                        objCashTO.setTokenNo(CommonUtil.convertObjToStr(paramMap.get("TOKEN_NO")));
                            objCashTO.setAuthorizeStatus_2("ENTERED_AMOUNT");
                            objCashTO.setParticulars("By "+objTO.getPARTICULARS());
                            objCashTO.setInitiatedBranch(_branchCode);
                            objCashTO.setInitChannType(CommonConstants.CASHIER);
                            objCashTO.setCommand(CommonConstants.TOSTATUS_INSERT);
                            objCashTO.setAcHdId(objTO.getAchdID());
                            objCashTO.setInpAmount(objTO.getAMOUNT());
                            objCashTO.setAmount(objTO.getAMOUNT());
                            objCashTO.setLinkBatchId(objTO.getLinkBATCHID());
                            objCashTO.setStatus(CommonConstants.STATUS_CREATED);
                            System.out.println("objCashTO^^^^^^^"+objCashTO);
                            cashList1.add(objCashTO);
                            System.out.println("cashList1---------------->"+cashList1);
                            HashMap tranMap=new HashMap();
                            tranMap.put(CommonConstants.BRANCH_ID, _branchCode);
                            tranMap.put(CommonConstants.USER_ID,objTO.getStatusBY());
                            tranMap.put(CommonConstants.IP_ADDR,(""));
                            tranMap.put(CommonConstants.MODULE,(""));
                            tranMap.put(CommonConstants.SCREEN,(""));
                            
                            HashMap cashMap = new HashMap();
                            cashMap.putAll(tranMap);
                            tranMap.put("MODE",CommonConstants.TOSTATUS_INSERT);
                            tranMap.put("DAILYDEPOSITTRANSTO",cashList1);
                            tranMap.put(CommonConstants.AUTHORIZESTATUS, CommonConstants.STATUS_AUTHORIZED);
                            CashTransactionDAO cashDao ;
                            cashDao = new CashTransactionDAO() ;
                            tranMap.putAll(cashDao.execute(tranMap, false));
                            tranMap.put("INITIATED_BRANCH", _branchCode);
                            ArrayList arrList =new ArrayList();
                            arrList.add(tranMap);
                            HashMap authMap = new HashMap();
                            authMap.put(CommonConstants.AUTHORIZEDATA, arrList);
                            authMap.put(CommonConstants.AUTHORIZESTATUS, CommonConstants.STATUS_AUTHORIZED);
                            
                            cashMap.put(CommonConstants.AUTHORIZEMAP, authMap);
                            System.out.println("#!@ cashMap:"+cashMap);
                            cashDao.execute(cashMap, false);
                            cashDao = null ;
                            tranMap = null ;
                        }
                    }
                    
                    //End cash
                    System.out.println("ssssss"+CommonConstants.STATUS_CREATED);
                    //                                objTransactionTO.setStatus(CommonConstants.STATUS_CREATED);
                    //                                objTransactionTO.setBatchId(objTO.getBatch_id());
                    //                                objTransactionTO.setBatchDt(currDt);
                    //                                objTransactionTO.setTransId(objTO.getLinkBATCHID());
                    //                                objTransactionTO.setBranchId(_branchCode);
                    //                                System.out.println("objTransactionTO------------------->"+objTransactionTO);
                    //                                sqlMap.executeUpdate("insertRemittanceIssueTransactionTO", objTransactionTO);
                    //}
                    //  }
                    //  }
                    amt=0.0;
                    objTransferTrans = null;
                    transferList = null;
                    txMap = null;
                    // Code End
                    //getTransDetails(objTO.getDisbursalNo());
                }
                else {
                    HashMap shareAcctNoMap = new HashMap();
                    //                    shareAcctNoMap = (HashMap)sqlMap.executeQueryForObject("transferResolvedShare", shareAcctNoMap);
                    //                    sqlMap.executeUpdate("updateShareAcctDetailsTO", shareAcctDetailsTO);
                    double amt = CommonUtil.convertObjToDouble(objTO.getAMOUNT()).doubleValue() ;
                    if (objTO.getCommand().equals(CommonConstants.TOSTATUS_DELETE)) {
                        
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
    }
    //copied from borrowings to update the gl  table   //
       public HashMap execute(HashMap map)  throws Exception {
		_branchCode = (String) map.get(CommonConstants.BRANCH_ID);
      
        tradingList = (ArrayList) map.get("TradingTO");
     
        //        final String command = objTO.getCommand();
        HashMap returnMap = null;
        //            System.out.println("---TradingDAO()-Going to insert--:"+command+":"+objTO.getTransID());
       
        //        if (command.equals(CommonConstants.TOSTATUS_INSERT)){
       // System.out.println("---TradingDAO()- insert data--");
            insertData();
            returnMap = new HashMap();
            returnMap.put("TRANS_ID",objTO.getTransID());
        //        } else if (command.equals(CommonConstants.TOSTATUS_UPDATE)){
        //            // updateData();
        //        } else if (command.equals(CommonConstants.TOSTATUS_DELETE)){
        //            // deleteData();
        //        } else {
        //            throw new NoCommandException();
        //        }
        
        destroyObjects();
        return returnMap;
    }
    
     private void destroyObjects() {
        objTO = null;
    }
   public HashMap executeQuery(HashMap obj) throws Exception {
    //    System.out.println("---Call exeQueryTradingDAO()- ");
		_branchCode = (String) obj.get(CommonConstants.BRANCH_ID);
        return getData(obj);
    }
     private HashMap getData(HashMap map) throws Exception {
        HashMap returnMap = new HashMap();
        HashMap where = (HashMap) map.get(CommonConstants.MAP_WHERE);
        List list = (List) sqlMap.executeQueryForList("getTradingIdDetails", where);
        returnMap.put("TradingTO", list);
        return returnMap;
    }
    
    
    
}
