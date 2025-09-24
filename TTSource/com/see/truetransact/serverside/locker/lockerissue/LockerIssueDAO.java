/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition.
 **
 *
 * AccountsDAO.java
 *
 * Created on September 08, 2003, 1:00 PM
 */
package com.see.truetransact.serverside.locker.lockerissue;

import com.ibatis.db.sqlmap.SqlMap;
import com.see.truetransact.serverutil.ServerUtil;
import com.see.truetransact.commonutil.CommonConstants;
import com.see.truetransact.commonutil.CommonUtil;
import com.see.truetransact.commonutil.DateUtil;
import com.see.truetransact.commonutil.NoCommandException;
import com.see.truetransact.serverexception.ServiceLocatorException;
import com.see.truetransact.serverexception.TransRollbackException;
import com.see.truetransact.serverside.TTDAO;
import com.see.truetransact.serverside.common.idgenerate.IDGenerateDAO;
import com.see.truetransact.servicelocator.ServiceLocator;
import com.see.truetransact.transferobject.operativeaccount.*;


import com.see.truetransact.serverside.common.nominee.NomineeDAO;
import com.see.truetransact.serverside.common.powerofattorney.PowerOfAttorneyDAO;
import com.see.truetransact.serverside.common.authorizedsignatory.AuthorizedSignatoryDAO;

import com.see.truetransact.serverside.common.log.LogDAO;
import com.see.truetransact.transferobject.common.log.LogTO;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;

import org.apache.log4j.Logger;

import com.see.truetransact.serverside.transaction.common.TransactionFactory;
import com.see.truetransact.transferobject.transaction.chargesServiceTax.ChargesServiceTaxTO;
import com.see.truetransact.serverside.common.transaction.TransactionDAO;
import com.see.truetransact.serverside.common.transaction.TransferTrans;
import com.see.truetransact.transferobject.common.transaction.TransactionTO;

import com.see.truetransact.serverside.transaction.chargesServiceTax.ChargesServiceTaxDAO;

import com.see.truetransact.transferobject.locker.lockerissue.*;

/**
 * This is used for Accounts Data Access.
 *
 * @author 152721
 */
public class LockerIssueDAO extends TTDAO {

    private String screen = "";
    static SqlMap sqlMap = null;
    final String SCREEN = "OA";
    String userID = "";
    String transID = "";
    double rent = 0.0;
    double servTax = 0.0;
    private LockerIssueTO lockerTO;
    private TransactionDAO transactionDAO = null;
    private ArrayList lockerChargeTabTO;
    private ArrayList lockerPwdTabTO;
    private String lockerIssId = "";
    private LockerIssueChargesTO objLockerIssueChargesTO;
    private LockerPwdDetailsTO objLockerPwdDetailsTO;
    private final static Logger log = Logger.getLogger(LockerIssueDAO.class);
    private LinkedHashMap mapJointAccntTO;
    private String accountNo;
    public String prod_id;
    private java.util.Date currDt = null;
    HashMap returnMap;
    static {
        try {
            ServiceLocator locate = ServiceLocator.getInstance();
            sqlMap = (SqlMap) locate.getDAOSqlMap();
        } catch (ServiceLocatorException se) {
            System.err.println(se);
            log.error(se);
        }
    }

//    /** Creates a new instance of AccountsDAO */
//    public LockerIssueDAO() {
//    }
    /*
     * this method will get the data against any query with some comdition
     */
    public Object getMiscData(String map, String where) {
        List list = null;
        try {
            list = (List) sqlMap.executeQueryForList(map, where);
        } catch (Exception e) {
            log.error(e);
            throw new RuntimeException("Error initializing SqlConfig class. Cause: " + e);
        }
        return (Object) list;
    }

    private String getLockerIssueId() throws Exception {
        final IDGenerateDAO dao = new IDGenerateDAO();
        final HashMap where = new HashMap();
        where.put(CommonConstants.MAP_WHERE, "LOCKER_ISSUE_ID");
        return (String) (dao.executeQuery(where)).get(CommonConstants.DATA);
    }

    /*
     * this method will insert the data in the database against the Accounts
     * screen, it will insert the record for AccountTO, *IntroTO,
     * AccountParamTO, nomineeTOList and poaTOList
     */
    private void insertData(HashMap map, LogDAO objLogDAO, LogTO objLogTO, String command) throws Exception {
        try {
            /**
             * Using the PowerOfAttorneyDAO, IntroducerDAO and NomineeDAO to
             * insert the Data in the Inroducer and Nominee Table...
             */
            boolean isNoTrans =  false;
            //Added by nithya on 18-02-2019 for KD 419 - 0020292: Could not issue Locker without transaction.
            if(map.containsKey("LOCKER_ISSUE_WITH_NO_TRANSACTION")&& map.get("LOCKER_ISSUE_WITH_NO_TRANSACTION") != null && CommonUtil.convertObjToStr(map.get("LOCKER_ISSUE_WITH_NO_TRANSACTION")).equalsIgnoreCase("LOCKER_ISSUE_WITH_NO_TRANSACTION")){
                isNoTrans = true;
            }
            PowerOfAttorneyDAO objPowerOfAttorneyDAO = new PowerOfAttorneyDAO(SCREEN);
            NomineeDAO objNomineeDAO = new NomineeDAO();
            AuthorizedSignatoryDAO objAuthSignDAO = new AuthorizedSignatoryDAO(SCREEN);

            LockerIssueTO lockerTO = (LockerIssueTO) map.get("LockerTO");
//            AccountTO accountTO = (AccountTO) map.get("AccountTO");
//            prod_id = accountTO.getProdId();
            // this is used to generate the account number on the server side
//            final String accountNumber = getAccountNumber();
            //__ To return to the OB...
//            transID = accountNumber;

//            accountTO.setActNum(accountNumber);
            lockerTO.setCreatedBy(userID);
            lockerTO.setCreateDt(currDt);
            lockerTO.setStatusBy(userID);
            lockerTO.setStatus(CommonConstants.STATUS_CREATED);
            lockerTO.setStatusDt(currDt);
            // insert AccountTO
            lockerTO.setBranchCode(_branchCode);
            lockerTO.setInitiatedBranch(_branchCode);
            setLockerIssId(getLockerIssueId());
            lockerTO.setRemarks(getLockerIssId()); 
            lockerTO.setActNum(getLockerIssId());
            sqlMap.executeUpdate("insertLockerMasterTO", lockerTO);
            if (!lockerTO.getFreezeStatus().equals("") && (lockerTO.getFreezeStatus().equals("Y") || lockerTO.getFreezeStatus().equals("N"))) {
                lockerTO = updatingSLNOinFreezeTable();
                sqlMap.executeUpdate("insertLockerFreezeTO", lockerTO);
            }
            // Inserting into Customer History Table
//            CustomerHistoryTO objCustomerHistoryTO = new CustomerHistoryTO ();
//            objCustomerHistoryTO.setAcctNo(accountNumber);
//            objCustomerHistoryTO.setCustId(accountTO.getCustId());
//            objCustomerHistoryTO.setProductType(TransactionFactory.OPERATIVE);
//            objCustomerHistoryTO.setProdId(accountTO.getProdId());
//            objCustomerHistoryTO.setRelationship(AcctStatusConstants.ACCT_HOLDER);
//            objCustomerHistoryTO.setFromDt(currDt);
//            CustomerHistoryDAO.insertToHistory(objCustomerHistoryTO);
//            objCustomerHistoryTO = null;

            objLogTO.setData(lockerTO.toString());
            objLogTO.setPrimaryKey(lockerTO.getKeyData());
            objLogDAO.addToLog(objLogTO);

            //__ Global Variable...
            accountNo = getLockerIssId();

//            AccountParamTO paramTO = (AccountParamTO) map.get("AccountParamTO");
//            paramTO.setActNum(lockerTO.getLocNum());
//            paramTO.setStatus(CommonConstants.STATUS_CREATED);
//            paramTO.setStatusBy(userID);
//            paramTO.setStatusDt(currDt);            

            //--- Inserts the Joint Account Holder  Details data into the database if the Constitution is Joint_Account.
            if (lockerTO.getLocCatId().equals("JOINT")) {
                insertJointAccntDetails(objLogDAO, objLogTO, accountNo);
            }

            // insert AccountParamTO
//            sqlMap.executeUpdate("insertAccountParamTO", paramTO);
//            objLogTO.setData(paramTO.toString());
//            objLogTO.setPrimaryKey(paramTO.getKeyData());
//            objLogDAO.addToLog(objLogTO);

            //__ To Add the Data Related to Nomine(s)...
            ArrayList nomineeTOList = (ArrayList) map.get("AccountNomineeTO");
            objNomineeDAO.insertData(nomineeTOList, accountNo, false, userID, SCREEN, objLogTO, objLogDAO);

            //__ To enter the Data in the POA...
            objPowerOfAttorneyDAO.setPoAMap((HashMap) map.get("PowerAttorneyTO"));
            objPowerOfAttorneyDAO.setBorrower_No(accountNo);
            objPowerOfAttorneyDAO.setScreen(getScreen());
            objPowerOfAttorneyDAO.executePoATabQuery(objLogTO, objLogDAO, sqlMap);

            objAuthSignDAO.setAuthorizeMap((HashMap) map.get("AuthorizedSignatoryTO"));
            objAuthSignDAO.setAuthorizedInstructionMap((HashMap) map.get("AuthorizedSignatoryInstructionTO"));
            objAuthSignDAO.setBorrower_No(accountNo);
            objAuthSignDAO.setScreen(getScreen());
            objAuthSignDAO.executeAuthorizedTabQuery(objLogTO, objLogDAO, sqlMap);


            for (int i = 0; i < lockerChargeTabTO.size(); i++) {
                objLockerIssueChargesTO = (LockerIssueChargesTO) lockerChargeTabTO.get(i);
                if (objLockerIssueChargesTO.getChargeType().equals("RENT_CHARGES")) {
                    rent = CommonUtil.convertObjToDouble(objLockerIssueChargesTO.getCommision()).doubleValue();
                    servTax = CommonUtil.convertObjToDouble(objLockerIssueChargesTO.getServiceTax()).doubleValue();
                }
                objLockerIssueChargesTO.setRemarks(getLockerIssId());
                objLockerIssueChargesTO.setBranID(_branchCode);
                if(isNoTrans){
                    objLockerIssueChargesTO.setCommision(0.0);
                    objLockerIssueChargesTO.setServiceTax(0.0);
                }
                sqlMap.executeUpdate("insertLockerIssueChrgsTO", objLockerIssueChargesTO);
            }
            for (int i = 0; i < lockerPwdTabTO.size(); i++) {
                objLockerPwdDetailsTO = (LockerPwdDetailsTO) lockerPwdTabTO.get(i);
//                objLockerPwdDetailsTO.setProdID(objTO.getProdId());
//                objLockerPwdDetailsTO.setStatus(objTO.getStatus());
                objLockerPwdDetailsTO.setBranID(_branchCode);
                objLockerPwdDetailsTO.setStatusDt(lockerTO.getStatusDt());
                objLockerPwdDetailsTO.setStatus(lockerTO.getStatus());
                objLockerPwdDetailsTO.setRemarks(getLockerIssId());
                sqlMap.executeUpdate("insertLockerPwdTO", objLockerPwdDetailsTO);
            }

//            transactionDAO.setBatchId(getLockerIssId());
//            transactionDAO.setBatchDate(currDt);
//            transactionDAO.setLinkBatchID(getLockerIssId()); 
//            System.out.println("##########transactionDAO"+transactionDAO);
            if (!isNoTrans) {
                insertAccHead(map, command);
            }

            nomineeTOList = null;
//            paramTO=null;
            objLockerPwdDetailsTO = null;
            objLockerIssueChargesTO = null;
            lockerTO = null;

            objNomineeDAO = null;
            objPowerOfAttorneyDAO = null;
            objAuthSignDAO = null;
        } catch (Exception e) {
            // if something goes wrong, rollback the transaction
            sqlMap.rollbackTransaction();
            log.error(e);
            throw e;
//            throw new TransRollbackException(e);
        }
    }

    public void insertAccHead(HashMap obj, String command) throws Exception {
        if (objLockerIssueChargesTO.getCommision().doubleValue() > 0) {
            ChargesServiceTaxDAO chargesServiceTaxDAO = new ChargesServiceTaxDAO();
            HashMap acHeads = (HashMap) sqlMap.executeQueryForObject("getLockerAccountHeads", objLockerIssueChargesTO.getProdID());
            ChargesServiceTaxTO chargesServiceTaxTO = new ChargesServiceTaxTO();
            chargesServiceTaxTO.setProd_id(objLockerIssueChargesTO.getProdID());
            chargesServiceTaxTO.setParticulars("Locker Issue : " + accountNo);
            chargesServiceTaxTO.setAmount(objLockerIssueChargesTO.getCommision());
            chargesServiceTaxTO.setService_tax_amt(objLockerIssueChargesTO.getServiceTax());
            double com = objLockerIssueChargesTO.getCommision().doubleValue();
            double tax = com + objLockerIssueChargesTO.getServiceTax().doubleValue();
            chargesServiceTaxTO.setTotal_amt(new Double(tax));
            chargesServiceTaxTO.setTrans_id(objLockerIssueChargesTO.getLocNum());
            chargesServiceTaxTO.setTrans_dt(objLockerIssueChargesTO.getStatusDt());
            chargesServiceTaxTO.setProd_type("GL");
            chargesServiceTaxTO.setCreated_by(CommonUtil.convertObjToStr(obj.get(CommonConstants.USER_ID)));
            chargesServiceTaxTO.setStatus("CREATED");
            chargesServiceTaxTO.setCommand(command);
            chargesServiceTaxTO.setAc_Head(CommonUtil.convertObjToStr(acHeads.get("LOC_RENT_AC_HD")));
            obj.put("ChargesServiceTaxTO", chargesServiceTaxTO);
            obj.put("LOCKER_SURRENDER_DAO", "LOCKER_SURRENDER_DAO");
            obj.put("LOCKER_SURRENDER_ID", accountNo);
            chargesServiceTaxDAO.execute(obj, false);
        }
//        String branchID = lockerTO.getBranchCode();
//        HashMap acHeads = (HashMap)sqlMap.executeQueryForObject("getLockerAccountHeads", lockerTO.getProdId());
//        HashMap txMap = new HashMap();
//        txMap = createMap(txMap, branchID);
//        //TransactionTO objTransactionTO;
//        TxTransferTO transferTo = null ;
//        LinkedHashMap TransactionDetailsMap = (LinkedHashMap) obj.get("TransactionTO");
//        if (TransactionDetailsMap.size()>0)
//            if(TransactionDetailsMap.containsKey("NOT_DELETED_TRANS_TOs")) {
//                LinkedHashMap allowedTransDetailsTO = (LinkedHashMap)TransactionDetailsMap.get("NOT_DELETED_TRANS_TOs");
//                for (int i = 1, j=allowedTransDetailsTO.size(); i<= j ;i++){
//                    objTransactionTO = (TransactionTO)allowedTransDetailsTO.get(String.valueOf(i));
//                    
//                    Double tempAmt = objTransactionTO.getTransAmt();
//                     if (objTransactionTO.getTransType().equals("TRANSFER")) {
//                     if(objTransactionTO.getTransAmt() != null && CommonUtil.convertObjToDouble(objTransactionTO.getTransAmt()).doubleValue() != 0){
////                         transactionDAO.setCommandMode(CommonConstants.TOSTATUS_INSERT);
//                        System.out.println("######getchargenewmode");
//                        System.out.println("######iside new mode");
//                        double totDebit = CommonUtil.convertObjToDouble(objTransactionTO.getTransAmt()).doubleValue();
//                        double calculatedServTax = totDebit-rent;
////                        rent = rent+calculatedServTax;
////                        System.out.println("######calculatedServTax"+calculatedServTax+"######rent"+rent);
//                        TxTransferTO txTransferTO = null;
//                        txMap = new HashMap();
//                        ArrayList transferList = new ArrayList(); // for local transfer
//                        
//                         if(!objTransactionTO.getProductType().equals("GL")){
//                        txMap.put(TransferTrans.DR_AC_HD, null);
//                        txMap.put(TransferTrans.DR_ACT_NUM, objTransactionTO.getDebitAcctNo());
//                        txMap.put(TransferTrans.DR_PROD_ID, objTransactionTO.getProductId());
//                        }else{
//                            txMap.put(TransferTrans.DR_AC_HD, objTransactionTO.getDebitAcctNo());
//                        }
//                        txMap.put(TransferTrans.DR_PROD_TYPE, objTransactionTO.getProductType());
//                        txMap.put(TransferTrans.DR_BRANCH, branchID);
//                        txMap.put(TransferTrans.PARTICULARS, lockerTO.getLocNum()+" Chrg");
//                        transferTo =  transactionDAO.addTransferDebitLocal(txMap, totDebit);
//                        transferList.add(transferTo);
//                        
//                        txMap.put(TransferTrans.CR_AC_HD, (String)acHeads.get("LOC_RENT_AC_HD"));
//                        txMap.put(TransferTrans.CR_BRANCH, branchID);
//                        txMap.put(TransferTrans.CURRENCY, "INR");
//                        txMap.put(TransferTrans.CR_PROD_TYPE, TransactionFactory.GL);
////                         txMap.put(TransferTrans.PARTICULARS, objTO.getInstrumentType());
//                        txMap.put(TransferTrans.PARTICULARS, lockerTO.getLocNum()+"Rent Chrg");
//                        transferTo =  transactionDAO.addTransferCreditLocal(txMap, rent) ;
//                        transferList.add(transferTo);
//                        
//                        txMap.put(TransferTrans.CR_AC_HD, (String)acHeads.get("SERV_TAX_AC_HD"));
//                        txMap.put(TransferTrans.CR_BRANCH, branchID);
//                        txMap.put(TransferTrans.CURRENCY, "INR");
//                        txMap.put(TransferTrans.CR_PROD_TYPE, TransactionFactory.GL);
////                         txMap.put(TransferTrans.PARTICULARS, objTO.getInstrumentType());
//                        txMap.put(TransferTrans.PARTICULARS, lockerTO.getLocNum()+" ServTax Chrg");
////                        if(servTax != 0)
//                          
//                        transferTo =  transactionDAO.addTransferCreditLocal(txMap, calculatedServTax) ;
//                        transferList.add(transferTo);
//                        
//                        transactionDAO.doTransferLocal(transferList, branchID);
//                    }
////                    objTO.setCharges(tempAmt);
//                     }else if (objTransactionTO.getTransType().equals("CASH")){
////                         TransactionTO transTO = new TransactionTO();
////                         ArrayList cashList = new ArrayList();
////                         double totDebit = CommonUtil.convertObjToDouble(objTransactionTO.getTransAmt()).doubleValue();
////                        double calculatedServTax = totDebit-rent;
////                                transactionDAO.setCommandMode(CommonConstants.TOSTATUS_INSERT);
////                            txMap.put(TransferTrans.CR_AC_HD, (String)acHeads.get("LOC_RENT_AC_HD"));  // credit to Exchange Charge account head......
//////                            transAmt = CommonUtil.convertObjToDouble(objRemittanceIssueTO.getAmount()).doubleValue();
////                            
////                            txMap.put(TransferTrans.PARTICULARS, "Rent ");
////                            transactionDAO.addTransferCredit(txMap, rent) ;
////                            transactionDAO.deleteTxList();
////                            transTO.setCommand(CommonConstants.TOSTATUS_INSERT);
////                            transTO.setTransType("CASH");
////                            transTO.setBatchId(lockerTO.getLocNum());
////                            transTO.setTransAmt(new Double(rent));
////                            cashList.add(transTO);
////                            transactionDAO.addCashList(cashList);
////                            transactionDAO.doTransfer();
////                            
////                            transactionDAO.setCommandMode(command);
////                             txMap.put(TransferTrans.CR_AC_HD, (String)acHeads.get("SERV_TAX_AC_HD"));  // credit to Exchange Charge account head......
//////                            transAmt = CommonUtil.convertObjToDouble(objRemittanceIssueTO.getAmount()).doubleValue();
////                            txMap.put(TransferTrans.PARTICULARS, "ServTax ");
////                            transactionDAO.addTransferCredit(txMap, calculatedServTax) ;
////                            transactionDAO.deleteTxList();
////                            transTO.setCommand(CommonConstants.TOSTATUS_INSERT);
////                            transTO.setTransType("CASH");
////                            transTO.setBatchId(lockerTO.getLocNum());
////                            transTO.setTransAmt(new Double(calculatedServTax));
////                            cashList.add(transTO);
////                            transactionDAO.addCashList(cashList);
////                            transactionDAO.doTransfer();
////                            transactionDAO.setCommandMode(command);
////                         double 
//                             TransactionTO transTO = new TransactionTO();
//                          ArrayList cashList = new ArrayList();
//                         double transAmt = CommonUtil.convertObjToDouble(objTransactionTO.getTransAmt()).doubleValue();
//                         transactionDAO.setCommandMode(CommonConstants.TOSTATUS_INSERT);
//                    txMap.put(TransferTrans.CR_AC_HD, (String)acHeads.get("LOC_RENT_AC_HD"));  // credit to Exchange Charge account head......
////                    transAmt = CommonUtil.convertObjToDouble(objRemittanceIssueTO.getAmount()).doubleValue();
//                    txMap.put(TransferTrans.PARTICULARS, "rent");
////                    transactionDAO.setTransType(CommonConstants.CREDIT);
//                    transactionDAO.addTransferCredit(txMap, transAmt) ;
//                    transactionDAO.deleteTxList();
//                    transTO.setCommand(CommonConstants.TOSTATUS_INSERT);
//                    transTO.setTransType("CASH");
//                    transTO.setBatchId(getLockerIssId());
//                    transTO.setTransAmt(new Double(transAmt));
//                    cashList.add(transTO);
//                    transactionDAO.addCashList(cashList);
//                    transactionDAO.doTransfer();
//                }
//                     }
//                }
    }
    //--- Inserts the data Record by record in AccountJointTO Table

    private void insertJointAccntDetails(LogDAO objLogDAO, LogTO objLogTO, String prodID) throws Exception {
        LockerIssueJointTO accountJointTO;
//        CustomerHistoryTO objCustomerHistoryTO;
        int jointAccntSize = mapJointAccntTO.size();
        for (int i = 0; i < jointAccntSize; i++) {
            try {
                accountJointTO = (LockerIssueJointTO) mapJointAccntTO.get(String.valueOf(i));
                accountJointTO.setLocNum(accountNo);
                accountJointTO.setStatus(CommonConstants.STATUS_CREATED);
                sqlMap.executeUpdate("insertLockerIssueJointTO", accountJointTO);

                // Inserting into Customer History Table
//                objCustomerHistoryTO = new CustomerHistoryTO ();
//                objCustomerHistoryTO.setAcctNo(accountNo);
//                objCustomerHistoryTO.setCustId(accountJointTO.getCustId());
//                objCustomerHistoryTO.setProductType(TransactionFactory.OPERATIVE);
//                objCustomerHistoryTO.setProdId(prodID);
//                objCustomerHistoryTO.setRelationship(AcctStatusConstants.JOINT);
//                objCustomerHistoryTO.setFromDt(currDt);
//                CustomerHistoryDAO.insertToHistory(objCustomerHistoryTO);
//                objCustomerHistoryTO = null;

                objLogTO.setData(accountJointTO.toString());
                objLogTO.setPrimaryKey(accountJointTO.getKeyData());
                objLogDAO.addToLog(objLogTO);
            } catch (Exception e) {
                sqlMap.rollbackTransaction();
                throw new TransRollbackException(e);
            }
        }
        mapJointAccntTO = null;// Added by nithya on 29-11-2017 for 8293
    }
    /*
     * this method will update the data on screen for a particular account
     * number
     */

    private void updateData(HashMap map, LogDAO objLogDAO, LogTO objLogTO, String command) throws Exception {
        try {
            NomineeDAO objNomineeDAO = new NomineeDAO();
            PowerOfAttorneyDAO objPowerOfAttorneyDAO = new PowerOfAttorneyDAO(SCREEN);
            AuthorizedSignatoryDAO objAuthSignDAO = new AuthorizedSignatoryDAO(SCREEN);

            /*
             * get the varoius objects, in this scenario, there are 6 objects
             * which are to be gathered from the "obj".
             */
//            LockerIssueTO accountTO = (LockerIssueTO) map.get("LockerTO");
            accountNo = CommonUtil.convertObjToStr(lockerTO.getRemarks());
//            
            if (command.equals(CommonConstants.TOSTATUS_UPDATE)) {
                lockerTO.setStatus(CommonConstants.STATUS_MODIFIED);
            }
            if (command.equals(CommonConstants.TOSTATUS_DELETE)) {
                lockerTO.setStatus(CommonConstants.STATUS_DELETED);
            }
            lockerTO.setStatusBy(userID);
//            accountTO.setStatusDt(currDt);
            lockerTO.setBranchCode(_branchCode);
            List lst = sqlMap.executeQueryForList("getLockerFreezeDetails", lockerTO);
            HashMap hmap = new HashMap();
            if (lockerTO.getFreezeStatus().equals("Y") || lockerTO.getUnFreezeStatus().equals("Y")) {
                hmap = (HashMap) lst.get(lst.size() - 1);
                String freezestaus = CommonUtil.convertObjToStr(hmap.get("FREEZE_STATUS"));
                String unFreezestatus = CommonUtil.convertObjToStr(hmap.get("UNFREEZE_STATUS"));
                if (freezestaus.equals("Y") && unFreezestatus.equals("Y")) {
                    if (lockerTO.getUnFreezeStatus().equals("Y")) {
                        sqlMap.executeUpdate("updateLockerUnFreezeTO", lockerTO);
                    } else {
                        lockerTO = updatingSLNOinFreezeTable();
                        sqlMap.executeUpdate("insertLockerFreezeTO", lockerTO);
                    }
                } else {
                    if (lockerTO.getFreezeStatus().equals("Y")) {
                        sqlMap.executeUpdate("updateLockerFreezeTO", lockerTO);
                    } else if (lockerTO.getUnFreezeStatus().equals("Y")) {
                        sqlMap.executeUpdate("updateLockerUnFreezeTO", lockerTO);
                    }
                }
            }
            sqlMap.executeUpdate("updateLockerMasterTO", lockerTO);

            objLogTO.setData(lockerTO.toString());
            objLogTO.setPrimaryKey(lockerTO.getKeyData());
            objLogDAO.addToLog(objLogTO);


            //--- Updates the Joint Account Holder  Details data into the database if the Constitution is Joint.
            if (lockerTO.getLocCatId().equals("JOINT")) {
                updateJointAccountDetails(objLogDAO, objLogTO, lockerTO.getStatus());
            }


//            AccountParamTO paramTO = (AccountParamTO) map.get("AccountParamTO");
//            paramTO.setStatus(CommonConstants.STATUS_MODIFIED);
//            paramTO.setStatusBy(userID);
//            paramTO.setStatusDt(currDt);  
//            
//            sqlMap.executeUpdate("updateAccountParamTO", paramTO);
//            objLogTO.setData(paramTO.toString());
//            objLogTO.setPrimaryKey(paramTO.getKeyData());
//            objLogDAO.addToLog(objLogTO);

            ArrayList nomineeTOList = (ArrayList) map.get("AccountNomineeTO");
            ArrayList nomineeDeleteList = (ArrayList) map.get("AccountNomineeDeleteTO");

            // Update the data regarding the NomineeTable...
            if (nomineeTOList != null) {
                if (nomineeTOList.size() > 0) {
                    System.out.println("Nominee List " + nomineeTOList);
                    objNomineeDAO.deleteData(accountNo, SCREEN);
                    if (!command.equals(CommonConstants.TOSTATUS_DELETE)) {
                        objNomineeDAO.insertData(nomineeTOList, accountNo, false, userID, SCREEN, objLogTO, objLogDAO);
                    }
                }
            }

            //__ Data for Nominee...
            if (nomineeDeleteList != null) {
                if (nomineeDeleteList.size() > 0) {
                    System.out.println("Deleted Nominee List " + nomineeDeleteList);
//                    objNomineeDAO.insertData(nomineeDeleteList, accountNo, true, userID, SCREEN, objLogTO, objLogDAO);
                    objNomineeDAO.deleteData(accountNo, SCREEN);
                }
            }

            //__ date for the POA...
            objPowerOfAttorneyDAO.setPoAMap((HashMap) map.get("PowerAttorneyTO"));
            objPowerOfAttorneyDAO.setBorrower_No(accountNo);
            objPowerOfAttorneyDAO.setCommand(command);
            objPowerOfAttorneyDAO.executePoATabQuery(objLogTO, objLogDAO, sqlMap);

            objAuthSignDAO.setAuthorizeMap((HashMap) map.get("AuthorizedSignatoryTO"));
            objAuthSignDAO.setAuthorizedInstructionMap((HashMap) map.get("AuthorizedSignatoryInstructionTO"));
            objAuthSignDAO.setBorrower_No(accountNo);
            objAuthSignDAO.setCommand(command);
            objAuthSignDAO.executeAuthorizedTabQuery(objLogTO, objLogDAO, sqlMap);

            HashMap acctNo = new HashMap();
            acctNo.put("ACT_NUM", accountNo);
            List list = null;
            boolean pwdListExists = false;
            if (!command.equals(CommonConstants.TOSTATUS_DELETE)) {
                list = sqlMap.executeQueryForList("getCountLockerPwdDetails", acctNo);
                if (list != null && list.size() > 0) {
                    pwdListExists = true;
                }
            }
            System.out.println("lockerPwdTabTO######" + lockerPwdTabTO);
            for (int i = 0; i < lockerPwdTabTO.size(); i++) {

                objLockerPwdDetailsTO = (LockerPwdDetailsTO) lockerPwdTabTO.get(i);
                objLockerPwdDetailsTO.setRemarks(lockerTO.getRemarks());
//                objLockerPwdDetailsTO.setStatus(objTO.getStatus());
                objLockerPwdDetailsTO.setBranID(_branchCode);
                objLockerPwdDetailsTO.setStatusDt(lockerTO.getStatusDt());
                objLockerPwdDetailsTO.setStatus(lockerTO.getStatus());
                boolean pwdExists = false;
                if (pwdListExists) {
                    for (int j = 0; j < list.size(); j++) {
                        if (objLockerPwdDetailsTO.getCustID().equals(CommonUtil.convertObjToStr(list.get(j)))) {
                            pwdExists = true;
                            break;
                        }
                    }
                    if (pwdExists) {
                        objLockerPwdDetailsTO.setStatus(CommonConstants.STATUS_MODIFIED);
                    } else {
                        objLockerPwdDetailsTO.setStatus(CommonConstants.STATUS_CREATED);
                    }
                }
                System.out.println("objLockerPwdDetailsTO######" + objLockerPwdDetailsTO);
                if (objLockerPwdDetailsTO.getStatus().equals(CommonConstants.STATUS_CREATED)) {
                    sqlMap.executeUpdate("insertLockerPwdTO", objLockerPwdDetailsTO);
                } else {
                    sqlMap.executeUpdate("updateLockerPwdTO", objLockerPwdDetailsTO);
                }
                pwdExists = false;
            }
            if (command.equals(CommonConstants.TOSTATUS_DELETE)) {
                System.out.println("lockerChargeTabTO######" + lockerChargeTabTO);
                if (lockerChargeTabTO != null && lockerChargeTabTO.size() > 0) {
                    objLockerIssueChargesTO = (LockerIssueChargesTO) lockerChargeTabTO.get(0);
                    if (objLockerIssueChargesTO.getChargeType().equals("RENT_CHARGES")) {
                        rent = CommonUtil.convertObjToDouble(objLockerIssueChargesTO.getCommision()).doubleValue();
                        servTax = CommonUtil.convertObjToDouble(objLockerIssueChargesTO.getServiceTax()).doubleValue();
                    }
                    System.out.println("objLockerIssueChargesTO######" + objLockerIssueChargesTO);
                    objLockerIssueChargesTO.setRemarks(lockerTO.getRemarks());
                    objLockerIssueChargesTO.setStatus(lockerTO.getStatus());
                    objLockerIssueChargesTO.setBranID(_branchCode);
                    sqlMap.executeUpdate("updateLockerIssueChrgsTO", objLockerIssueChargesTO);
                    HashMap transMap = new HashMap();
                    transMap.put("LINK_BATCH_ID", accountNo);
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
                    transMap.put("AMOUNT", new Double(rent + servTax));
                    map.put("TOTAL_AMT_TRANSACTION", transMap);
                    map.put("AMT_TRANSACTION", transMap);
                    insertAccHead(map, command);
                    transMap = null;
                }
            }
            objLockerIssueChargesTO = null;
            nomineeTOList = null;
            nomineeDeleteList = null;
//            paramTO=null;
//            accountTO=null;
            objNomineeDAO = null;
            objPowerOfAttorneyDAO = null;
            objAuthSignDAO = null;
        } catch (Exception e) {
            // if something goes wrong, rollback the transaction
            sqlMap.rollbackTransaction();
            log.error(e);
            throw new TransRollbackException(e);
        }

    }

    private void updateJointAccountDetails(LogDAO objLogDAO, LogTO objLogTO, String status) throws Exception {
        LockerIssueJointTO accountJointTO;
        HashMap acctNo = new HashMap();
        acctNo.put("ACT_NUM", accountNo);
        List list = (List) sqlMap.executeQueryForList("getCountLockerJointHolders", acctNo);
        HashMap jntMapData = new HashMap();
        jntMapData.put("JointAccountHoldersData", list);
        HashMap JointAccntMap;
        JointAccntMap = (HashMap) ((List) jntMapData.get("JointAccountHoldersData")).get(0);
        int isMore = (int) Integer.parseInt(JointAccntMap.get("COUNT").toString());
        int jntAccntHolderSize = mapJointAccntTO.size();
        for (int i = 0; i < jntAccntHolderSize; i++) {
            try {
                accountJointTO = (LockerIssueJointTO) mapJointAccntTO.get(String.valueOf(i));
                accountJointTO.setLocNum(accountNo);
                if (accountJointTO.getStatus().equals(CommonConstants.STATUS_DELETED)) {
                    accountJointTO.setStatus(CommonConstants.STATUS_DELETED);
                    sqlMap.executeUpdate("updateLockerIssueJointTO", accountJointTO);
                } else {
                    if (i < isMore) { //--- If the data is exisiting , update the data
                        accountJointTO.setStatus(CommonConstants.STATUS_MODIFIED);
                        sqlMap.executeUpdate("updateLockerIssueJointTO", accountJointTO);

                    } else { //--- Else insert the data.
                        accountJointTO.setStatus(CommonConstants.STATUS_CREATED);
                        sqlMap.executeUpdate("insertLockerIssueJointTO", accountJointTO);
                    }
                }
                objLogTO.setData(accountJointTO.toString());
                objLogTO.setPrimaryKey(accountJointTO.getKeyData());
                objLogDAO.addToLog(objLogTO);
            } catch (Exception e) {
                sqlMap.rollbackTransaction();
                throw new TransRollbackException(e);
            }
        }
    }

    /*
     * performing all operations based on the command parameter
     */
    public HashMap execute(HashMap hash) throws Exception {
        System.out.println("Locker DAO execute method : " + hash);
        setScreen(CommonUtil.convertObjToStr(hash.get("SCREEN")));
        HashMap execReturnMap = new HashMap();
        String command = "";
        // Log DAO
        LogDAO objLogDAO = new LogDAO();

        // Log Transfer Object
        LogTO objLogTO = new LogTO();
        objLogTO.setUserId((String) hash.get(CommonConstants.USER_ID));
        objLogTO.setBranchId((String) hash.get(CommonConstants.BRANCH_ID));
        objLogTO.setSelectedBranchId((String) hash.get(CommonConstants.SELECTED_BRANCH_ID));
        objLogTO.setIpAddr((String) hash.get(CommonConstants.IP_ADDR));
        objLogTO.setModule((String) hash.get(CommonConstants.MODULE));
        objLogTO.setScreen((String) hash.get(CommonConstants.SCREEN));

        _branchCode = CommonUtil.convertObjToStr(hash.get(CommonConstants.BRANCH_ID));
        userID = CommonUtil.convertObjToStr(hash.get(CommonConstants.USER_ID));

        currDt = ServerUtil.getCurrentDate(_branchCode);

        if (hash.containsKey("LockerTO")) {
            lockerTO = (LockerIssueTO) hash.get("LockerTO");
            command = lockerTO.getCommand();
        }
        mapJointAccntTO = (LinkedHashMap) hash.get("JointLockerTO");

        final String COMMAND = command;

        if (hash.containsKey("LockerPwdDetailsTO")) {
            lockerPwdTabTO = new ArrayList();
            lockerPwdTabTO = (ArrayList) hash.get("LockerPwdDetailsTO");
        }
        if (hash.containsKey("LockerIssueChargesTO")) {
            lockerChargeTabTO = new ArrayList();
            lockerChargeTabTO = (ArrayList) hash.get("LockerIssueChargesTO");
        }
        try {
            /*
             * start the transaction, if something goes wrong that condition is
             * handled in individual method
             */
            sqlMap.startTransaction();

            if (!(hash.get("MODE").equals("Authorize"))) {
                //To perform corresponding actions based on command object
                if (COMMAND.equals(CommonConstants.TOSTATUS_INSERT)) {
                    insertData(hash, objLogDAO, objLogTO, COMMAND);
					getTransDetails(accountNo);
                    //execReturnMap.put(CommonConstants.TRANS_ID, accountNo);
                } else if (COMMAND.equals(CommonConstants.TOSTATUS_UPDATE)) {
                    updateData(hash, objLogDAO, objLogTO, COMMAND);
                } else if (COMMAND.equals(CommonConstants.TOSTATUS_DELETE)) {
                    updateData(hash, objLogDAO, objLogTO, COMMAND);
                } else {
                    sqlMap.rollbackTransaction();
                    throw new NoCommandException();
                }
            } else {
                HashMap authMap = (HashMap) hash.get(CommonConstants.AUTHORIZEMAP);
                if (authMap != null) {
                    authorize(authMap);
                }
            }
            sqlMap.commitTransaction();
            hash = null;
        } catch (Exception e) {
            System.out.println("Exception throws in LockerIssueDAO execute : " + e);
            sqlMap.rollbackTransaction();
            throw e;
        }
        System.out.println("Locker DAO execute method return : " + execReturnMap);
        return returnMap;
    }
    private void getTransDetails(String batchId) throws Exception {
        HashMap getTransMap = new HashMap();
        getTransMap.put("BATCH_ID", batchId);
        getTransMap.put("TRANS_DT", currDt);
        getTransMap.put(CommonConstants.BRANCH_ID, _branchCode);
        //        System.out.println("########getTransferTransBatchID returnMap depCloseAmt:"+resultMap);
        returnMap = new HashMap();
        List transList = (List) sqlMap.executeQueryForList("getTransferDetails", getTransMap);
        if (transList != null && transList.size() > 0) {
            returnMap.put("TRANSFER_TRANS_LIST", transList);
        }
        List cashList = (List) sqlMap.executeQueryForList("getCashDetails", getTransMap);
        if (cashList != null && cashList.size() > 0) {
            returnMap.put("CASH_TRANS_LIST", cashList);
        }
        returnMap.put(CommonConstants.TRANS_ID, accountNo);
        getTransMap.clear();
        getTransMap = null;
        transList = null;
        cashList = null;
    }
    private void authorize(HashMap map) throws Exception {
        String status = (String) map.get(CommonConstants.AUTHORIZESTATUS);

        //String exp=(String)map.get(CommonConsta);
        ArrayList selectedList = (ArrayList) map.get(CommonConstants.AUTHORIZEDATA);
        HashMap dataMap;
        String lockerNo;
        String issueID;
        String expdt;
        int date = currDt.getDate();

        //java.util.Date odate=DateUtil.getDateMMDDYYYY(date);

        // int day=odate.getDate();
        int month = currDt.getMonth() + 1;
        int year = currDt.getYear() + 1900;
        for (int i = 0; i < selectedList.size(); i++) {
            dataMap = (HashMap) selectedList.get(i);
            lockerNo = CommonUtil.convertObjToStr(dataMap.get("LOCKER_NUM"));
            issueID = CommonUtil.convertObjToStr(dataMap.get("ISSUE_ID"));
            lockerNo = CommonUtil.convertObjToStr(dataMap.get("LOCKER_NUM"));
            String exp = CommonUtil.convertObjToStr(map.get("EXP_DT"));
            java.util.Date d = DateUtil.getDateMMDDYYYY(exp);
            //System.out.println("remitPayId###"+remitPayId);
            dataMap.put(CommonConstants.STATUS, status);
            dataMap.put(CommonConstants.USER_ID, map.get(CommonConstants.USER_ID));
            dataMap.put(CommonConstants.BRANCH_ID, _branchCode);
            dataMap.put("TODAY_DT", currDt);
            dataMap.put("EXP_DT", d);
            dataMap.put("DAILY", "DAILY"); // This is important for Cash authorization (multiple entries
            //System.out.println("datamap %%^^^^^^"+dataMap);
            TransactionDAO dao = new TransactionDAO(CommonConstants.DEBIT);
            TransactionDAO.authorizeCashAndTransfer(issueID, status, dataMap);
            dao = null;
            System.out.println("datamap %%^^^^^^" + dataMap);

            sqlMap.executeUpdate("authorizeLockerMaster", dataMap);
            if (status.equalsIgnoreCase(CommonConstants.STATUS_AUTHORIZED)) {
                HashMap paramMap = new HashMap();
                paramMap.put("LOCKER_STATUS", "NOT_AVAILABLE");
                paramMap.put("LOC_NO", lockerNo);
                sqlMap.executeUpdate("updateLockerStatus", paramMap);
                paramMap = null;
            }
            HashMap transMap = new HashMap();
            transMap.put("LINK_BATCH_ID", issueID);
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
            transMap.put("SERVICE_ID", serviceTransID);
            transMap.put("SERVICE_AUTH_STATUS", status);
            transMap.put("SERVICE_AUTH_BY", map.get(CommonConstants.USER_ID));
            transMap.put("SERVICE_AUTH_DT", currDt);
            sqlMap.executeUpdate("AuthSerTaxFromLockerSurrender", transMap);
            transId = null;
            transMap = null;
            dataMap = null;
        }
    }

    public long getNearest(long number, long roundingFactor) {
        long roundingFactorOdd = roundingFactor;
        if ((roundingFactor % 2) != 0) {
            roundingFactorOdd += 1;
        }
        long mod = number % roundingFactor;
        if ((mod < (roundingFactor / 2)) || (mod < (roundingFactorOdd / 2))) {
            return lower(number, roundingFactor);
        } else {
            return higher(number, roundingFactor);
        }
    }

    public long lower(long number, long roundingFactor) {
        long mod = number % roundingFactor;
        return number - mod;
    }

    public long higher(long number, long roundingFactor) {
        long mod = number % roundingFactor;
        if (mod == 0) {
            return number;
        }
        return (number - mod) + roundingFactor;
    }

    public static void main(String[] arg) {
        try {
            HashMap data = new HashMap();
            data.put("WHERE", "OA060893");
            new LockerIssueDAO().executeQuery(data);
            data = null;
        } catch (Exception e) {
            System.out.println(e);
        }
    }

    public synchronized HashMap executeQuery(HashMap hash) throws Exception {
        _branchCode = CommonUtil.convertObjToStr(hash.get(CommonConstants.BRANCH_ID));
        currDt = ServerUtil.getCurrentDate(_branchCode);
        HashMap returnMap = null;
        NomineeDAO objNomineeDAO = new NomineeDAO();
        PowerOfAttorneyDAO objPowerOfAttorneyDAO = new PowerOfAttorneyDAO(SCREEN);
        AuthorizedSignatoryDAO objAuthSignDAO = new AuthorizedSignatoryDAO(SCREEN);
        try {
            String map = (String) hash.get("MAPNAME");
            String where = (String) hash.get("WHERE");
            returnMap = new HashMap();
            if (map == null) {
                List list = (List) sqlMap.executeQueryForList("getSelectLockerJointTO", where);
                returnMap.put("JointAcctDetails", list);
                List lst = sqlMap.executeQueryForList("getLockerFreeze", where);
                if (lst != null && lst.size() > 0) {
                    list = (List) sqlMap.executeQueryForList("getSelectLockerFreezeTO", where);
                    if (list != null && list.size() > 0) {
                        returnMap.put("AccountTO", list);
                    }
                } else {
                    list = (List) sqlMap.executeQueryForList("getSelectLockerTO", where);
                    returnMap.put("AccountTO", list);
                }
                List lockerPwdList = (List) sqlMap.executeQueryForList("getSelectLockerPwdDetailsTO", hash);
                if (lockerPwdList != null && lockerPwdList.size() != 0) {
                    returnMap.put("LockerPwdDetailsTO", lockerPwdList);
                }
                List lockerIssueChrgsList = (List) sqlMap.executeQueryForList("getSelectLockerIssueChrgsTO", hash);
                if (lockerIssueChrgsList != null && lockerIssueChrgsList.size() != 0) {
                    returnMap.put("LockerIssueChrgsTO", lockerIssueChrgsList);
                }
                returnMap.put("AccountNomineeList", (List) objNomineeDAO.getDataList(where, SCREEN));
                objPowerOfAttorneyDAO.getData(returnMap, where, sqlMap);
                objAuthSignDAO.getData(returnMap, where, sqlMap);
            } else if (map.equals("getCustomerDetails")) {
                returnMap.put("CustomerDetails", getMiscData(map, where));
            } else if (map.equals("getAccountDetails")) {
                returnMap.put("AccountDetails", getMiscData(map, where));
            } else if (map.equals("getProductInterests")) {
                returnMap.put("ProductInterests", getMiscData(map, where));
            } else if (map.equals("getBranchDetails")) {
                returnMap.put("BranchDetails", getMiscData(map, where));
            } else if (map.equals("getTransActDetails")) {
                returnMap.put("TransActDetails", getMiscData(map, where));
            } else if (map.equals("getPrevoisAccountDetails")) {
                returnMap.put("getPrevoisAccountDetails", getMiscData(map, where));
            }
            transactionDAO = new TransactionDAO(CommonConstants.CREDIT);
            //        HashMap returnMap = new HashMap();
            HashMap whereMap = new HashMap();
            //         String where = (String) map.get(CommonConstants.MAP_WHERE);
            TransactionTO dataTO = new TransactionTO();
            LockerIssueTO objLockerIssueTO;
            HashMap dataMap = new HashMap();
            whereMap.put("LOCKER_NUM", CommonUtil.convertObjToStr(hash.get("LOCKER_NUM")));
            whereMap.put("CUST_ID", CommonUtil.convertObjToStr(hash.get("CUST_ID")));
            whereMap.put("BRANCH_CODE", CommonUtil.convertObjToStr(hash.get("BRANCH_CODE")));
            System.out.println("#####whereMap" + whereMap);
            //        where = (String) hash.get("ISSUE_ID");
            List list = (List) sqlMap.executeQueryForList("getSelectLockerTO", where);
            objLockerIssueTO = (LockerIssueTO) list.get(0);
            //        returnMap.put("LockerSurTO", list);
            list = null;
            hash.put("WHERE", hash.get("ISSUE_ID"));
            list = transactionDAO.getData(hash);
            if (list != null && list.size() > 0) {
                dataTO = (TransactionTO) list.get(0);
                if (dataTO.getTransType().equals("CASH")) {
                    if (returnMap != null) {
                        if (returnMap.size() > 0) {
                            HashMap acHeads = (HashMap) sqlMap.executeQueryForObject("getLockerAccountHeads", objLockerIssueTO.getProdId());
                            HashMap getTransMap = new HashMap();
                            dataMap.put("AMOUNT", CommonUtil.convertObjToDouble(new Double(rent + servTax)));
                            double amount = CommonUtil.convertObjToDouble(dataMap.get("AMOUNT")).doubleValue();
                            //System.out.println("@@@@@@@amount"+amount);
                            getTransMap.put("LINK_BATCH_ID", hash.get("LOCKER_NUM"));
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
                        }
                    }
                }
                if (dataTO.getTransType().equals("TRANSFER")) {
                    if (returnMap != null) {
                        if (returnMap.size() > 0) {
                            HashMap acHeads = (HashMap) sqlMap.executeQueryForObject("getLockerAccountHeads", objLockerIssueTO.getProdId());
                            HashMap getTransMap = new HashMap();
                            dataMap.put("AMOUNT", CommonUtil.convertObjToDouble(new Double(rent + servTax)));
                            double amount = CommonUtil.convertObjToDouble(dataMap.get("AMOUNT")).doubleValue();
                            //System.out.println("@@@@@@@amount"+amount);
                            getTransMap.put("LINK_BATCH_ID", hash.get("LOCKER_NUM"));
                            getTransMap.put("TODAY_DT", ServerUtil.getCurrentDate(CommonUtil.convertObjToStr(_branchCode)));
                            getTransMap.put("INITIATED_BRANCH", CommonUtil.convertObjToStr(_branchCode));
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
                        }
                    }
                }
                returnMap.put("TransactionTO", list);
            }
            transactionDAO = null;
            //        return returnMap;

        } catch (Exception e) {
            e.printStackTrace();
            log.error(e);
            throw new RuntimeException("Error initializing SqlConfig class. Cause: " + e);
        }
        hash = null;
        System.out.println("@@@@@@@@@@@@@@@@@returnMap" + returnMap);
        return returnMap;
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

    /**
     * Getter for property screen.
     *
     * @return Value of property screen.
     */
    public java.lang.String getScreen() {
        return screen;
    }

    /**
     * Setter for property screen.
     *
     * @param screen New value of property screen.
     */
    public void setScreen(java.lang.String screen) {
        this.screen = screen;
    }

    private LockerIssueTO updatingSLNOinFreezeTable() throws Exception {
        List slList = sqlMap.executeQueryForList("getMaxFreezNo", lockerTO);
        int slNo = 0;
        HashMap lockerMap = new HashMap();
        if (slList != null && slList.size() > 0) {
            System.out.println("updatingSLNOinFreezeTable slList : " + slList);
            lockerMap = (HashMap) slList.get(0);
            slNo = CommonUtil.convertObjToInt(lockerMap.get("SLNO"));
            slNo = slNo + 1;
            lockerTO.setSlNo(CommonUtil.convertObjToStr(slNo));
        }
        return lockerTO;
    }
}
