/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition.
 * 
 *
 * LodgementDAO.java
 *
 * Created on Mon Feb 07 15:08:33 IST 2005
 */
package com.see.truetransact.serverside.bills.lodgement;

import java.sql.SQLException;
import java.util.Date;
import java.util.List;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import com.ibatis.db.sqlmap.SqlMap;
import com.see.truetransact.businessrule.transaction.ChequeInstrumentRule;
import com.see.truetransact.businessrule.transaction.DateCheckingRule;
import com.see.truetransact.businessrule.validateexception.ValidationRuleException;
import com.see.truetransact.commonutil.TTException;
import com.see.truetransact.serverutil.ServerUtil;
import com.see.truetransact.commonutil.CommonConstants;
import com.see.truetransact.commonutil.NoCommandException;
import com.see.truetransact.commonutil.CommonUtil;
import com.see.truetransact.commonutil.exceptionconstants.transaction.TransactionConstants;
import com.see.truetransact.serverside.TTDAO;
import com.see.truetransact.serverside.investments.InvestmentsTransDAO;
import com.see.truetransact.transferobject.investments.InvestmentsTransTO;
import com.see.truetransact.servicelocator.ServiceLocator;
import com.see.truetransact.serverexception.TransRollbackException;
import com.see.truetransact.serverexception.ServiceLocatorException;
import com.see.truetransact.serverside.transaction.transfer.TransferDAO;
import com.see.truetransact.transferobject.bills.lodgement.LodgementMasterTO;
import com.see.truetransact.transferobject.bills.lodgement.LodgementChequeTO;
import com.see.truetransact.transferobject.bills.lodgement.LodgementHundiTO;
import com.see.truetransact.transferobject.bills.lodgement.LodgementInstructionsTO;
import com.see.truetransact.transferobject.bills.lodgement.LodgementInstrTO;
import com.see.truetransact.transferobject.bills.lodgement.LodgementRemitTO;
import com.see.truetransact.transferobject.bills.lodgement.LodgementBillRatesTO;
import com.see.truetransact.serverside.common.idgenerate.IDGenerateDAO;
import com.see.truetransact.serverside.common.log.LogDAO;
import com.see.truetransact.transferobject.common.log.LogTO;
import com.see.truetransact.transferobject.common.transaction.TransactionTO;
import com.see.truetransact.serverside.common.transaction.TransferTrans;
import com.see.truetransact.transferobject.transaction.transfer.TxTransferTO;
import com.see.truetransact.serverside.common.transaction.TransactionDAO;
import com.see.truetransact.serverside.transaction.common.TransactionFactory;
//import com.see.truetransact.serverside.transaction.common.TransactionDAOConstants;
import com.see.truetransact.serverside.remittance.RemittanceIssueDAO;
import com.see.truetransact.serverside.transaction.cash.CashTransactionDAO;
import com.see.truetransact.serverside.transaction.common.TransactionDAOConstants;
import com.see.truetransact.serverside.transaction.rollback.RollBack;
import com.see.truetransact.transferobject.bills.lodgement.MultipleLodgementMasterTO;
import com.see.truetransact.transferobject.remittance.RemittanceIssueTO;
import com.see.truetransact.transferobject.servicetax.servicetaxdetails.ServiceTaxDetailsTO;
import com.see.truetransact.transferobject.transaction.cash.CashTransactionTO;
import com.see.truetransact.ui.common.servicetax.ServiceTaxCalculation;

/**
 * Lodgement DAO.
 *
 */
public class LodgementDAO extends TTDAO {

    private static SqlMap sqlMap = null;
    private LodgementMasterTO objLodgementMasterTO;
    private LodgementChequeTO objLodgementChequeTO;
    private LodgementChequeTO tempObjLodgementChequeTO;
    private LodgementHundiTO objLodgementHundiTO;
    private LodgementHundiTO tempObjLodgementHundiTO;
    private LodgementRemitTO objLodgementRemitTO;
    private LodgementBillRatesTO objLodgementBillRatesTO;
    private ArrayList instructionList;
    private ArrayList instrList;
    private LodgementInstructionsTO objLodgementInstructionsTO;
    private LodgementInstrTO objLodgementInstrTO;
    private LodgementInstructionsTO objLodgementInstTO;
    private String lodgementId = "";
    private String command = "";
    private String batchId = "";
    private LogDAO logDAO;
    private LogTO logTO;
    private TransactionDAO transactionDAO = null;
    private Date batchDate;
    private String branchID = "";
    private String subRegType = "";
    private RemittanceIssueDAO remittanceIssueDAO;
    private RemittanceIssueTO remittanceIssueTO;
    TransferDAO transferDAO = new TransferDAO();
    private Date currDt = null;
    HashMap acHeadMap = new HashMap();
    HashMap returnAuthMap = new HashMap();
    private MultipleLodgementMasterTO objMultipleLodgementMasterTO;
    private ArrayList multipleLodgementMasterTOList;
    /**
     * Creates a new instance of LodgementDAO
     */
    public LodgementDAO() throws ServiceLocatorException {
        ServiceLocator locate = ServiceLocator.getInstance();
        sqlMap = (SqlMap) locate.getDAOSqlMap();
    }
    
    private HashMap getData(HashMap map) throws Exception {
        HashMap returnMap = new HashMap();
        String where = (String) map.get(CommonConstants.MAP_WHERE);
        HashMap newMap = new HashMap();
        newMap.put("BILL_STATUS", map.get("BILL_STATUS"));
        newMap.put("LODGEMENT_ID", map.get("LODGEMENT_ID"));
        if (map.containsKey("HISTORY")) {
            List lodgementMasterHistoryList = (List) sqlMap.executeQueryForList("getSelectLodgementMasterHistoryTO", newMap);
            returnMap.put("LodgementMasterTO", lodgementMasterHistoryList);
        } else {
            List lodgementMasterList = (List) sqlMap.executeQueryForList("getSelectLodgementMasterTO", where);
            returnMap.put("LodgementMasterTO", lodgementMasterList);
        }
        //        returnMap.put("LodgementMasterTO", lodgementMasterList);
        List lodgementChequeList = (List) sqlMap.executeQueryForList("getSelectLodgementChequeTO", where);
        if (lodgementChequeList != null && lodgementChequeList.size() != 0) {
            returnMap.put("LodgementChequeTO", lodgementChequeList);
        }
        List lodgementHundiList = (List) sqlMap.executeQueryForList("getSelectLodgementHundiTO", where);
        if (lodgementHundiList != null && lodgementHundiList.size() != 0) {
            returnMap.put("LodgementHundiTO", lodgementHundiList);
        }
        List lodgementInstList = (List) sqlMap.executeQueryForList("getSelectLodgementInstructions", where);
        if (lodgementInstList != null && lodgementInstList.size() != 0) {
            returnMap.put("LodgementInstructionsTO", lodgementInstList);
        }
        List lodgementRemitList = (List) sqlMap.executeQueryForList("getSelectLodgementRemitTO", newMap);
        //         newMap = null;
        if (lodgementRemitList != null && lodgementRemitList.size() != 0) {
            returnMap.put("LodgementRemitTO", lodgementRemitList);
        }
        List lodgementInstruList = (List) sqlMap.executeQueryForList("getSelectLodgementInstrTO", newMap);
        if (lodgementInstruList != null && lodgementInstruList.size() != 0) {
            returnMap.put("LodgementInstrTO", lodgementInstruList);
        }
        
        // Added by nithya for multiple lodgement
        if(map.containsKey("MULTIPLE_LODGEMENT")){
            List multipleLodgementMasterList = (List) sqlMap.executeQueryForList("getSelectMultipleLodgementMasterTO", newMap);
            if (multipleLodgementMasterList != null && multipleLodgementMasterList.size() > 0) {
                returnMap.put("MultipleLodgementMasterTO", multipleLodgementMasterList);
            }
        }
        // End
        newMap = null;
        return returnMap;
    }

    private String getLodgement_BranchAcInfo(String billsAcNo) throws Exception {
        HashMap where = new HashMap();
        String billsType = objLodgementMasterTO.getBillsType();
        where.put("BILLS_TYPE", billsType);
        where.put("BRANCH_ID", branchID);
        String billsNo = "";
        /*List accNolist = sqlMap.executeQueryForList("getBillsId", where);
        if (accNolist != null && accNolist.size() > 0) {
            HashMap hash = (HashMap) accNolist.get(0);
            String prefix = CommonUtil.convertObjToStr(hash.get("PREFIX"));
            int suffix = CommonUtil.convertObjToInt(hash.get("NEXT_AC_NO"));
            String old_suf = CommonUtil.convertObjToStr(new Integer(suffix));
            String sufixnum = CommonUtil.convertObjToStr(hash.get("LAST_AC_NO"));
            String newsufix = CommonUtil.convertObjToStr(hash.get("NEXT_AC_NO"));
            suffix = suffix + 1;
            System.out.println("Suffix$$$$" + suffix);
            String suf = CommonUtil.convertObjToStr(new Integer(suffix));
            billsNo = prefix + newsufix;// sufixnum;
            where.put("OLD_SUFFIX", old_suf);
            where.put("NEW_SUFFIX", suf);
            String currAcNo = prefix + newsufix;
            if (billsAcNo.equalsIgnoreCase(currAcNo) || billsAcNo == currAcNo) {
                System.out.println("billsNo@@111111111111@@" + billsNo);
                sqlMap.executeUpdate("updateBillsBranchNo", where);
            }
        }*/
        //Modified by sreekrishnan
        billsNo = CommonUtil.convertObjToStr(sqlMap.executeQueryForObject("geUpdatedBillsId", where));
        System.out.println("billsNo$%#%#"+billsNo);
        return billsNo;
    }

    private String getLodgementId() throws Exception {
        final IDGenerateDAO dao = new IDGenerateDAO();
        final HashMap where = new HashMap();
        where.put(CommonConstants.MAP_WHERE, "LODGEMENT_ID");
        return (String) (dao.executeQuery(where)).get(CommonConstants.DATA);
    }
    public HashMap erroMap = new HashMap();

    public HashMap checkValidation() throws SQLException {

        HashMap chqMap = new HashMap();
        HashMap inputMap = new HashMap();
        inputMap.put("ACCOUNTNO", objLodgementMasterTO.getBorrowAcctNum());
        inputMap.put("INSTRUMENT1", objLodgementChequeTO.getInstrPrefix());
        inputMap.put("INSTRUMENT2", objLodgementChequeTO.getInstrumentNo());
        try {
            sqlMap.startTransaction();
            List chequeIssued = (List) sqlMap.executeQueryForList("getChequeIssueNoRule", inputMap);
            System.out.println("chq size------" + chequeIssued.size());
            if (chequeIssued != null && chequeIssued.size() <= 0) {
                chqMap.put("ERRORLIST", "This Instrument is not issued/valid. A/c No:" + objLodgementMasterTO.getBorrowAcctNum());
            }
            double months = 0;
            HashMap instMap = new HashMap();
            Date dueDate = (Date) objLodgementChequeTO.getInstrumentDt();
            Date currDt = ServerUtil.getCurrentDate(super._branchCode);
            currDt.setDate(dueDate.getDate());
            currDt.setMonth(dueDate.getMonth());
            currDt.setYear(dueDate.getYear());
            instMap.put("INST_DATE", currDt);
            instMap.put("CURR_DATE", currDt);
            System.out.println("instMap : " + instMap);
            java.util.List lst = sqlMap.executeQueryForList("getInstrumentMonthDiff", instMap);
            if (lst != null && lst.size() > 0) {
                instMap = (HashMap) lst.get(0);
                System.out.println("instMap : " + instMap);
                months = CommonUtil.convertObjToDouble(instMap.get("MONTH_DIFF")).doubleValue();
                System.out.println("Months: " + months);
            }
            if (months > 3) {
                chqMap.put("ERRORLIST", "Stale Cheque");
            }
            inputMap.put("INSTRU_TYPE", "CHEQUE");
            inputMap.put("TRANS_DT", currDt);
            inputMap.put("INITIATED_BRANCH", _branchCode);
            inputMap.put("BRANCH_CODE", _branchCode);
            List instrumentCleared = (List) sqlMap.executeQueryForList("getInstrumentClearedRule", inputMap);

            if (instrumentCleared != null && instrumentCleared.size() > 0) {
                HashMap clearMap = (HashMap) instrumentCleared.get(0);
                if (CommonUtil.convertObjToStr(clearMap.get("AUTHORIZE_STATUS")).equalsIgnoreCase(CommonConstants.STATUS_AUTHORIZED)) {
                    System.out.println("Instrument Cleared, Authorized1111111...");
                } else {
                    System.out.println("Instrument sent for Clearing, Pending for Authorization...");
                    chqMap.put("ERRORLIST", "This sent for Clearing, Pending for Authorization..." + objLodgementMasterTO.getBorrowAcctNum());
                }
            }

            inputMap.put("DATE", objLodgementChequeTO.getInstrumentDt());
            double amt = 0.0;
            double amount = 0.0;
            amount = CommonUtil.convertObjToDouble(objLodgementChequeTO.getInstrumentAmount());
            amt = checkForMinAmount(inputMap);
            List list = (List) sqlMap.executeQueryForList("getOABalance", inputMap);
            inputMap = (HashMap) list.get(0);
            double availableBalance = CommonUtil.convertObjToDouble(inputMap.get("AVAILABLE_BALANCE")).doubleValue();
            double chqNewAmt = availableBalance - amt;
            if (amount > chqNewAmt) {
                chqMap.put("ERRORLIST", "Insufficient balance.Ac No:" + objLodgementMasterTO.getBorrowAcctNum());
            }
            sqlMap.commitTransaction();
        } catch (Exception e) {
            //   e.printStackTrace();
            sqlMap.rollbackTransaction();
        }
        return chqMap;
    }

    private double checkForMinAmount(HashMap inputMap) {
        double amt = 0.0;
        try {
            List minMap = sqlMap.executeQueryForList("getMinBalance", inputMap);
            if(minMap!=null && minMap.size()>0){
                inputMap = (HashMap) minMap.get(0);
                if(inputMap!=null && inputMap.containsKey("CHQ_BOOK")){
                    double withoutChq = CommonUtil.convertObjToDouble(inputMap.get("MIN_WITHOUT_CHQ")).doubleValue();
                    double withChq = CommonUtil.convertObjToDouble(inputMap.get("MIN_WITH_CHQ")).doubleValue();
                    String chqBk = CommonUtil.convertObjToStr(inputMap.get("CHQ_BOOK")).toUpperCase();
                    System.out.println("Inside checkForMinAmount :");
                    if (chqBk.equals("Y")) {
                        amt = withoutChq;
                    } else {
                        amt = withChq;
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return amt;
    }

    private void insertData(HashMap map) throws Exception {
        try {
            System.out.println("Will nottttttttt");

            sqlMap.startTransaction();

            //getLodgement_BranchAcInfo(objLodgementMasterTO.getBillsNo());//Commented by sreekrishnan
            //lodgementId = objLodgementMasterTO.getBillsNo();
            
            if(map.containsKey("MULTIPLE_LODGEMENT")){
                lodgementId = CommonUtil.convertObjToStr(map.get("LODGEMENT_ID"));
            }else{
                lodgementId= getLodgement_BranchAcInfo(objLodgementMasterTO.getBillsNo());
            }
            
            System.out.println("lodgementId innnn====" + lodgementId);
            objLodgementMasterTO.setLodgementId(lodgementId);
            objLodgementMasterTO.setStatus(CommonConstants.STATUS_CREATED);
            objLodgementMasterTO.setCreatedDt(currDt);
            //objLodgementMasterTO.setBranchCode(_branchCode);
            objLodgementMasterTO.setBranchCode(CommonUtil.convertObjToStr(map.get("SAVE_BRANCH_CODE")));
            logTO.setData(objLodgementMasterTO.toString());
            logTO.setPrimaryKey(objLodgementMasterTO.getKeyData());
            logTO.setStatus(objLodgementMasterTO.getCommand());
            if(map.containsKey(("MULTIPLE_LODGEMENT"))){
                if(map.containsKey("FINAL_RECORD")){
                   sqlMap.executeUpdate("insertLodgementMasterTO", objLodgementMasterTO);
                   sqlMap.executeUpdate("insertLodgementMasterHistoryTO", objLodgementMasterTO);
                }
            }else{
                   sqlMap.executeUpdate("insertLodgementMasterTO", objLodgementMasterTO);
                   sqlMap.executeUpdate("insertLodgementMasterHistoryTO", objLodgementMasterTO);
            }
            
            logDAO.addToLog(logTO);
            if (objLodgementChequeTO != null) {
                objLodgementChequeTO.setLodgementId(lodgementId);
                objLodgementChequeTO.setStatus(CommonConstants.STATUS_CREATED);
                logTO.setData(objLodgementChequeTO.toString());
                logTO.setPrimaryKey(objLodgementChequeTO.getKeyData());
                logTO.setStatus(objLodgementChequeTO.getCommand());
                if (map.containsKey(("MULTIPLE_LODGEMENT"))) {
                    if (map.containsKey("FINAL_RECORD")) {
                        //TOTAL_CHEQUE_AMT
                        tempObjLodgementChequeTO = objLodgementChequeTO ;
                        tempObjLodgementChequeTO.setInstrumentAmount(CommonUtil.convertObjToDouble(map.get("TOTAL_CHEQUE_AMT")));
                        sqlMap.executeUpdate("insertLodgementChequeTO", tempObjLodgementChequeTO);
                    }
                }else{                    
                        sqlMap.executeUpdate("insertLodgementChequeTO", objLodgementChequeTO); 
                }
                logDAO.addToLog(logTO);
            }
            if (objLodgementHundiTO != null) {
                objLodgementHundiTO.setLodgementId(lodgementId);
                objLodgementHundiTO.setStatus(CommonConstants.STATUS_CREATED);
                logTO.setData(objLodgementHundiTO.toString());
                logTO.setPrimaryKey(objLodgementHundiTO.getKeyData());
                logTO.setStatus(objLodgementHundiTO.getCommand());
                if (map.containsKey(("MULTIPLE_LODGEMENT"))) {
                    if (map.containsKey("FINAL_RECORD")) {
                        tempObjLodgementHundiTO = objLodgementHundiTO;
                        tempObjLodgementHundiTO.setHundiAmount(CommonUtil.convertObjToDouble(map.get("TOTAL_CHEQUE_AMT")));
                        sqlMap.executeUpdate("insertLodgementHundiTO", tempObjLodgementHundiTO);
                    }
                }else{
                    sqlMap.executeUpdate("insertLodgementHundiTO", objLodgementHundiTO);
                }                
                logDAO.addToLog(logTO);
            }
            if (objLodgementBillRatesTO != null) {
                objLodgementBillRatesTO.setLodgeID(lodgementId);
                objLodgementBillRatesTO.setStatus(CommonConstants.STATUS_CREATED);
                logTO.setData(objLodgementBillRatesTO.toString());
                logTO.setPrimaryKey(objLodgementBillRatesTO.getKeyData());
                logTO.setStatus(objLodgementBillRatesTO.getCommand());
                sqlMap.executeUpdate("insertLodgementBillRatesTO", objLodgementBillRatesTO);
                logDAO.addToLog(logTO);
            }
            // Added by nithya for multiple bills lodgement
            if(map.containsKey(("MULTIPLE_LODGEMENT"))){
                if(map.containsKey("FINAL_RECORD")){
                    if(map.containsKey("MULTIPLE_LODGEMENT_MASTER")){
                        if(multipleLodgementMasterTOList != null && multipleLodgementMasterTOList.size() > 0 ){
                            for (int i = 0; i < multipleLodgementMasterTOList.size(); i++) {
                            objMultipleLodgementMasterTO = (MultipleLodgementMasterTO) multipleLodgementMasterTOList.get(i);
                            objMultipleLodgementMasterTO.setLodgementId(lodgementId);
                            objMultipleLodgementMasterTO.setStatus(CommonConstants.STATUS_CREATED);
                            logTO.setData(objMultipleLodgementMasterTO.toString());
                            logTO.setPrimaryKey(objMultipleLodgementMasterTO.getKeyData()); 
                            logTO.setStatus(objMultipleLodgementMasterTO.getCommand());
                            sqlMap.executeUpdate("insertMultipleLodgementMasterTO", objMultipleLodgementMasterTO);
                            logDAO.addToLog(logTO);  
                        }
                    }
                }                
              }
            }     
            // End
            updateLodgementInstructions();
            updateLodgementInstr();
            System.out.println("tempObjLodgementChequeTO :: " + tempObjLodgementChequeTO);
            System.out.println("ObjLodgementChequeTO :: " +objLodgementChequeTO);
            if (((getSubRegType().equals("ICC")) || (getSubRegType().equals("CPD"))) && (objLodgementMasterTO.getBillsStatus().equals("LODGEMENT"))) {
                HashMap txMap = new HashMap();
                doAccountHeadCredit(map, command, txMap, branchID);
            }
            if (CommonUtil.convertObjToDouble(objLodgementChequeTO.getInstrumentAmount()).doubleValue() > 0 && (objLodgementMasterTO.getBillsStatus().equals("LODGEMENT"))) {
                System.out.println("######### Instrument Amount : " + CommonUtil.convertObjToDouble(objLodgementChequeTO.getInstrumentAmount()).doubleValue());
                System.out.println("######### Activities        : " + objLodgementMasterTO.getBillsStatus());

            }

            sqlMap.commitTransaction();
        } catch (Exception e) {
            sqlMap.rollbackTransaction();
            e.printStackTrace();
            //            throw new TransRollbackException(e);
            throw e;
        }
    }

    private void updateData(HashMap map) throws Exception {
        try {
            System.out.println("Will complete....");
            sqlMap.startTransaction();
            if (objLodgementMasterTO.getBillsStatus().equals("LODGEMENT")) {
                System.out.println("objLodgementMasterTO.getBillsStatus()++++***+++" + objLodgementMasterTO.getBillsStatus());
                
                if (objLodgementMasterTO != null) {
                    objLodgementMasterTO.setStatus(CommonConstants.STATUS_MODIFIED);
                    logTO.setData(objLodgementMasterTO.toString());
                    logTO.setPrimaryKey(objLodgementMasterTO.getKeyData());
                    logTO.setStatus(objLodgementMasterTO.getCommand());
                    if(map.containsKey(("MULTIPLE_LODGEMENT"))){
                      if(map.containsKey("FINAL_RECORD")){
                         sqlMap.executeUpdate("updateLodgementMasterTO", objLodgementMasterTO);
                         sqlMap.executeUpdate("updateLodgementMasterHistoryTO", objLodgementMasterTO);
                      }
                    }else{
                        sqlMap.executeUpdate("updateLodgementMasterTO", objLodgementMasterTO);
                        sqlMap.executeUpdate("updateLodgementMasterHistoryTO", objLodgementMasterTO);
                    }
                    
                    logDAO.addToLog(logTO);
                }
                if (objLodgementChequeTO != null) {
                    objLodgementChequeTO.setStatus(CommonConstants.STATUS_MODIFIED);
                    logTO.setData(objLodgementChequeTO.toString());
                    logTO.setPrimaryKey(objLodgementChequeTO.getKeyData());
                    logTO.setStatus(objLodgementChequeTO.getCommand());
                    if (map.containsKey(("MULTIPLE_LODGEMENT"))) {
                        if (map.containsKey("FINAL_RECORD")) {
                            //TOTAL_CHEQUE_AMT
                            tempObjLodgementChequeTO = objLodgementChequeTO;
                            tempObjLodgementChequeTO.setInstrumentAmount(CommonUtil.convertObjToDouble(map.get("TOTAL_CHEQUE_AMT")));
                            sqlMap.executeUpdate("updateLodgementChequeTO", tempObjLodgementChequeTO);
                        }
                    }else{
                         sqlMap.executeUpdate("updateLodgementChequeTO", objLodgementChequeTO);
                    }     
                    logDAO.addToLog(logTO);
                }
                if (objLodgementHundiTO != null) { // tempObjLodgementHundiTO
                    objLodgementHundiTO.setStatus(CommonConstants.STATUS_MODIFIED);
                    logTO.setData(objLodgementHundiTO.toString());
                    logTO.setPrimaryKey(objLodgementHundiTO.getKeyData());
                    logTO.setStatus(objLodgementHundiTO.getCommand());
                    if (map.containsKey(("MULTIPLE_LODGEMENT"))) {
                        if (map.containsKey("FINAL_RECORD")) {
                            tempObjLodgementHundiTO = objLodgementHundiTO;
                            tempObjLodgementHundiTO.setHundiAmount(CommonUtil.convertObjToDouble(map.get("TOTAL_CHEQUE_AMT")));
                            sqlMap.executeUpdate("updateLodgementHundiTO", tempObjLodgementHundiTO);
                        }
                    }else{
                        sqlMap.executeUpdate("updateLodgementHundiTO", objLodgementHundiTO);
                    }      
                    logDAO.addToLog(logTO);
                }
                if (objLodgementBillRatesTO != null) {
                    //                objLodgementBillRatesTO.setLodgeID(lodgementId);
                    objLodgementBillRatesTO.setStatus(CommonConstants.STATUS_MODIFIED);
                    logTO.setData(objLodgementBillRatesTO.toString());
                    logTO.setPrimaryKey(objLodgementBillRatesTO.getKeyData());
                    logTO.setStatus(objLodgementBillRatesTO.getCommand());
                    sqlMap.executeUpdate("updateLodgementBillRatesTO", objLodgementBillRatesTO);
                    logDAO.addToLog(logTO);
                }
                
                // Added by nithya for multiple bills lodgement
                if (map.containsKey(("MULTIPLE_LODGEMENT"))) {
                    if (map.containsKey("FINAL_RECORD")) {
                        if (map.containsKey("MULTIPLE_LODGEMENT_MASTER")) {        
                            HashMap updateMap = new HashMap();
                            updateMap.put("LODGEMENT_ID",map.get("LODGEMENT_ID"));
                            sqlMap.executeUpdate("updateMultipleLodgementInstructions", updateMap);
                            if (multipleLodgementMasterTOList != null && multipleLodgementMasterTOList.size() > 0) {
                                for (int i = 0; i < multipleLodgementMasterTOList.size(); i++) {
                                    objMultipleLodgementMasterTO = (MultipleLodgementMasterTO) multipleLodgementMasterTOList.get(i);
                                    //objMultipleLodgementMasterTO.setLodgementId(lodgementId);
                                    objMultipleLodgementMasterTO.setStatus(CommonConstants.STATUS_MODIFIED);
                                    logTO.setData(objMultipleLodgementMasterTO.toString());
                                    logTO.setPrimaryKey(objMultipleLodgementMasterTO.getKeyData());
                                    logTO.setStatus(objMultipleLodgementMasterTO.getCommand());
                                    sqlMap.executeUpdate("insertMultipleLodgementMasterTO", objMultipleLodgementMasterTO);
                                    logDAO.addToLog(logTO);                                    
                                }
                            }
                            updateMap = null;
                        }
                    }
                }  
            // End
                updateLodgementInstructions();
                updateLodgementInstr();
            } else if (objLodgementMasterTO.getBillsStatus().equals("REALIZE")) {
                if ((!getSubRegType().equals("ICC")) && (!getSubRegType().equals("CPD"))) {
                    System.out.println("objLodgementMasterTO.getBillsStatus()+++++++" + objLodgementMasterTO.getBillsStatus());
                    boolean isTran = false;
                    HashMap txMap = new HashMap();
                    objLodgementInstTO = new LodgementInstructionsTO();
                    HashMap paramMap = new HashMap();
                    paramMap.put("LODGEMENT_ID", objLodgementMasterTO.getLodgementId());
                    paramMap.put("BILL_STATUS", objLodgementMasterTO.getBillsStatus());
                    List tranLst = sqlMap.executeQueryForList("getAllBillsTransactions", paramMap);
                    paramMap = null;
                    if (tranLst != null && tranLst.size() > 0) {
                        for (int i = 0; i < tranLst.size(); i++) {
                            paramMap = (HashMap) tranLst.get(i);
                            String authStatus = CommonUtil.convertObjToStr(paramMap.get("AUTHORIZE_STATUS"));
                            String status = CommonUtil.convertObjToStr(paramMap.get("STATUS"));
                            if ((authStatus.equals("REJECTED")) || (status.equals("DELETED"))) {
                                isTran = true;
                                authStatus = "";
                                status = "";
                            } else {
                                isTran = false;
                                if(map.containsKey("MULTIPLE_LODGEMENT")){
                                    isTran = true;
                                }
                            }
                        }
                    } else {
                        isTran = true;
                    }
                    if (isTran) {
                        System.out.println("objLodgementInstTO+++++++" + objLodgementInstTO);
                        if(map.containsKey("OMIT_OTHER_BANK_CHARGE")){
                          doAccountHeadCreditWithoutBankCharges(map, command, txMap, branchID);
                        }else{
                          doAccountHeadCredit(map, command, txMap, branchID);
                        }
                        objLodgementMasterTO.setCreatedDt(currDt);
                        objLodgementMasterTO.setStatus(CommonConstants.STATUS_MODIFIED);
                        sqlMap.executeUpdate("insertLodgementMasterHistoryTO", objLodgementMasterTO);
                        isTran = false;
                    }
                    if (objLodgementMasterTO != null) {
                        objLodgementMasterTO.setStatus(CommonConstants.STATUS_MODIFIED);
                        logTO.setData(objLodgementMasterTO.toString());
                        logTO.setPrimaryKey(objLodgementMasterTO.getKeyData());
                        logTO.setStatus(objLodgementMasterTO.getCommand());
                        sqlMap.executeUpdate("updateLodgementMasterTO", objLodgementMasterTO);
                        sqlMap.executeUpdate("updateLodgementMasterHistoryTO", objLodgementMasterTO);
                        logDAO.addToLog(logTO);
                    }
                    paramMap = null;
                } else {
                    boolean isTran = false;
                    HashMap txMap = new HashMap();
                    objLodgementInstTO = new LodgementInstructionsTO();
                    HashMap paramMap = new HashMap();
                    paramMap.put("LODGEMENT_ID", objLodgementMasterTO.getLodgementId());
                    paramMap.put("BILL_STATUS", objLodgementMasterTO.getBillsStatus());
                    List tranLst = sqlMap.executeQueryForList("getAllBillsTransactions", paramMap);
                    paramMap = null;
                    if (tranLst != null && tranLst.size() > 0) {
                        for (int i = 0; i < tranLst.size(); i++) {
                            paramMap = (HashMap) tranLst.get(i);
                            String authStatus = CommonUtil.convertObjToStr(paramMap.get("AUTHORIZE_STATUS"));
                            String status = CommonUtil.convertObjToStr(paramMap.get("STATUS"));
                            if ((authStatus.equals("REJECTED")) || (status.equals("DELETED"))) {
                                isTran = true;
                                authStatus = "";
                                status = "";
                            } else {
                                isTran = false;
                                if(map.containsKey("MULTIPLE_LODGEMENT")){
                                    isTran = true;
                                }
                            }
                        }
                    } else {
                        isTran = true;
                    }
                    //                HashMap txMap = new HashMap();
                    if (isTran) {
                        System.out.println("objLodgem+++++++" + objLodgementMasterTO.getBillsStatus());
                        if(map.containsKey("OMIT_OTHER_BANK_CHARGE")){
                          doAccountHeadCreditWithoutBankCharges(map, command, txMap, branchID);
                        }else{
                          doAccountHeadCredit(map, command, txMap, branchID);
                        }  
                        objLodgementMasterTO.setCreatedDt(currDt);
                        objLodgementMasterTO.setStatus(CommonConstants.STATUS_MODIFIED);
                        sqlMap.executeUpdate("insertLodgementMasterHistoryTO", objLodgementMasterTO);
                        isTran = false;
                    }
                    if (objLodgementMasterTO != null) {
                        objLodgementMasterTO.setStatus(CommonConstants.STATUS_MODIFIED);
                        logTO.setData(objLodgementMasterTO.toString());
                        logTO.setPrimaryKey(objLodgementMasterTO.getKeyData());
                        logTO.setStatus(objLodgementMasterTO.getCommand());
                        sqlMap.executeUpdate("updateLodgementMasterTO", objLodgementMasterTO);
                        sqlMap.executeUpdate("updateLodgementMasterHistoryTO", objLodgementMasterTO);
                        logDAO.addToLog(logTO);
                    }
                }
                updateLodgementInstructions();
                updateLodgementInstr();
            } else if (objLodgementMasterTO.getBillsStatus().equals("DISHONOUR")) {
                System.out.println("In dishonour..........");
                if ((!getSubRegType().equals("ICC")) && (!getSubRegType().equals("CPD"))) {
                    System.out.println("objLodgementMasterTO.getBillsStatus()+++++++" + objLodgementMasterTO.getBillsStatus());
                    boolean isTran = false;
                    HashMap txMap = new HashMap();
                    //objLodgementInstTO = new LodgementInstructionsTO();
                    HashMap paramMap = new HashMap();
                    paramMap.put("LODGEMENT_ID", objLodgementMasterTO.getLodgementId());
                    paramMap.put("BILL_STATUS", objLodgementMasterTO.getBillsStatus());
                    List tranLst = sqlMap.executeQueryForList("getAllBillsTransactions1", paramMap);
                    paramMap = null;
                    System.out.println("jjjjjjjjjjjjjjj..................");
                    System.out.println("tranLst.size()=====" + tranLst.size());
                    if (tranLst != null && tranLst.size() > 0) {
                        for (int i = 0; i < tranLst.size(); i++) {
                            System.out.println("tranLst.size()" + tranLst.size());
                            paramMap = (HashMap) tranLst.get(i);
                            System.out.println("paramMap.........." + paramMap);
                            if ((paramMap.get("AUTHORIZE_STATUS").equals("REJECTED")) || (paramMap.get("STATUS").equals("DELETED"))) {
                                isTran = true;
                            } else {
                                isTran = false;
                            }
                        }
                    } else {
                        isTran = true;
                    }
                    System.out.println("isTran......." + isTran);
                    if (isTran) {//bbbb
                        System.out.println("objLodgementInstTO+++1111++++" + objLodgementInstTO);
                        doAccountHeadCredit(map, command, txMap, branchID);
                        objLodgementMasterTO.setCreatedDt(currDt);
                        objLodgementMasterTO.setStatus(CommonConstants.STATUS_MODIFIED);
                        sqlMap.executeUpdate("insertLodgementMasterHistoryTO", objLodgementMasterTO);
                        isTran = false;
                    }
                    if (objLodgementMasterTO != null) {
                        System.out.println("nnnnnnnnnnnnnnn...........");
                        System.out.println("here........." + objLodgementMasterTO);
                        objLodgementMasterTO.setStatus(CommonConstants.STATUS_MODIFIED);
                        System.out.println("objLodgementMasterTO.toString()" + objLodgementMasterTO.toString());
                        logTO.setData(objLodgementMasterTO.toString());
                        logTO.setPrimaryKey(objLodgementMasterTO.getKeyData());
                        logTO.setStatus(objLodgementMasterTO.getCommand());
                        sqlMap.executeUpdate("updateLodgementMasterTO", objLodgementMasterTO);
                        sqlMap.executeUpdate("updateLodgementMasterHistoryTO", objLodgementMasterTO);
                        logDAO.addToLog(logTO);
                    }
                } else {
                    boolean isTran = false;
                    HashMap txMap = new HashMap();
                    //  objLodgementInstTO = new LodgementInstructionsTO();
                    HashMap paramMap = new HashMap();
                    paramMap.put("LODGEMENT_ID", objLodgementMasterTO.getLodgementId());
                    paramMap.put("BILL_STATUS", objLodgementMasterTO.getBillsStatus());
                    List tranLst = sqlMap.executeQueryForList("getAllBillsTransactions", paramMap);
                    paramMap = null;
                    if (tranLst != null && tranLst.size() > 0) {
                        for (int i = 0; i < tranLst.size(); i++) {
                            paramMap = (HashMap) tranLst.get(i);
                            String authStatus = CommonUtil.convertObjToStr(paramMap.get("AUTHORIZE_STATUS"));
                            String status = CommonUtil.convertObjToStr(paramMap.get("STATUS"));
                            if ((authStatus.equals("REJECTED")) || (status.equals("DELETED"))) {
                                isTran = true;
                                authStatus = "";
                                status = "";
                            } else {
                                isTran = false;
                            }
                        }
                    } else {
                        isTran = true;
                    }
                    //                HashMap txMap = new HashMap();
                    if (isTran) {
                        System.out.println("objLodgem++++2222222222+++" + objLodgementMasterTO.getBillsStatus());
                        doAccountHeadCredit(map, command, txMap, branchID);
                        objLodgementMasterTO.setCreatedDt(currDt);
                        objLodgementMasterTO.setStatus(CommonConstants.STATUS_MODIFIED);
                        sqlMap.executeUpdate("insertLodgementMasterHistoryTO", objLodgementMasterTO);
                        isTran = false;
                    }
                    if (objLodgementMasterTO != null) {
                        objLodgementMasterTO.setStatus(CommonConstants.STATUS_MODIFIED);
                        logTO.setData(objLodgementMasterTO.toString());
                        logTO.setPrimaryKey(objLodgementMasterTO.getKeyData());
                        logTO.setStatus(objLodgementMasterTO.getCommand());
                        sqlMap.executeUpdate("updateLodgementMasterTO", objLodgementMasterTO);
                        sqlMap.executeUpdate("updateLodgementMasterHistoryTO", objLodgementMasterTO);
                        logDAO.addToLog(logTO);
                    }
                }
                updateLodgementInstructions();
                updateLodgementInstr();

            } else if (objLodgementMasterTO.getBillsStatus().equals("PROCEEDS_RECEIVED")) {
                if (objLodgementMasterTO != null) {
                    objLodgementMasterTO.setStatus(CommonConstants.STATUS_MODIFIED);
                    logTO.setData(objLodgementMasterTO.toString());
                    logTO.setPrimaryKey(objLodgementMasterTO.getKeyData());
                    logTO.setStatus(objLodgementMasterTO.getCommand());
                    sqlMap.executeUpdate("updateLodgementMasterTO", objLodgementMasterTO);
                    HashMap paramMap = new HashMap();
                    paramMap.put("LODGEMENT_ID", objLodgementMasterTO.getLodgementId());
                    paramMap.put("BILL_STATUS", objLodgementMasterTO.getBillsStatus());
                    List lst = sqlMap.executeQueryForList("checkBillsLodgementHistory", paramMap);
                    paramMap = null;
                    if (lst != null && lst.size() > 0) {
                        sqlMap.executeUpdate("updateLodgementMasterHistoryTO", objLodgementMasterTO);
                    } else {
                        objLodgementMasterTO.setCreatedDt(currDt);
                        sqlMap.executeUpdate("insertLodgementMasterHistoryTO", objLodgementMasterTO);
                    }
                    lst = null;
                    logDAO.addToLog(logTO);
                }
            } else if (objLodgementMasterTO.getBillsStatus().equals("CLOSURE")) {
                if (objLodgementMasterTO != null) {
                    if ((!getSubRegType().equals("ICC")) && (!getSubRegType().equals("CPD"))) {
                        System.out.println("objLodgementMaster++++++" + objLodgementMasterTO.getBillsStatus());
                        boolean isTran = false;
                        HashMap txMap = new HashMap();
                        objLodgementInstTO = new LodgementInstructionsTO();
                        HashMap paramMap = new HashMap();
                        paramMap.put("LODGEMENT_ID", objLodgementMasterTO.getLodgementId());
                        paramMap.put("BILL_STATUS", objLodgementMasterTO.getBillsStatus());
                        List tranLst = sqlMap.executeQueryForList("getAllBillsTransactions", paramMap);
                        paramMap = null;
                        if (tranLst != null && tranLst.size() > 0) {
                            for (int i = 0; i < tranLst.size(); i++) {
                                paramMap = (HashMap) tranLst.get(i);
                                if ((paramMap.get("AUTHORIZE_STATUS").equals("REJECTED")) || (paramMap.get("STATUS").equals("DELETED"))) {
                                    isTran = true;
                                } else {
                                    isTran = false;
                                }
                            }
                        } else {
                            isTran = true;
                        }
                        HashMap paramMap1 = new HashMap();
                        paramMap1.put("LODGEMENT_ID", objLodgementMasterTO.getLodgementId());
                        paramMap1.put("BILL_STATUS", objLodgementMasterTO.getBillsStatus());
                        List lst = sqlMap.executeQueryForList("checkBillsLodgementHistory", paramMap1);
                        paramMap1 = null;
                        if (lst != null && lst.size() > 0) {
                            isTran = false;
                            //                            sqlMap.executeUpdate("updateLodgementMasterHistoryTO", objLodgementMasterTO);
                        } else {
                            isTran = true;
                            //                            objLodgementMasterTO.setCreatedDt(currDt);
                            //                            sqlMap.executeUpdate("insertLodgementMasterHistoryTO", objLodgementMasterTO);
                        }
                        lst = null;
                        if (isTran) {
                            System.out.println("objLodgementInstTO++" + objLodgementInstTO);
                            doAccountHeadCredit(map, command, txMap, branchID);
                            objLodgementMasterTO.setCreatedDt(currDt);
                            objLodgementMasterTO.setStatus(CommonConstants.STATUS_MODIFIED);
                            sqlMap.executeUpdate("insertLodgementMasterHistoryTO", objLodgementMasterTO);
                            isTran = false;
                        }
                        if (objLodgementMasterTO != null) {
                            objLodgementMasterTO.setStatus(CommonConstants.STATUS_MODIFIED);
                            logTO.setData(objLodgementMasterTO.toString());
                            logTO.setPrimaryKey(objLodgementMasterTO.getKeyData());
                            logTO.setStatus(objLodgementMasterTO.getCommand());
                            sqlMap.executeUpdate("updateLodgementMasterTO", objLodgementMasterTO);
                            sqlMap.executeUpdate("updateLodgementMasterHistoryTO", objLodgementMasterTO);
                            logDAO.addToLog(logTO);
                        }
                    } else {
                        boolean isTran = false;
                        HashMap txMap = new HashMap();
                        objLodgementInstTO = new LodgementInstructionsTO();
                        HashMap paramMap = new HashMap();
                        paramMap.put("LODGEMENT_ID", objLodgementMasterTO.getLodgementId());
                        paramMap.put("BILL_STATUS", objLodgementMasterTO.getBillsStatus());
                        List tranLst = sqlMap.executeQueryForList("getAllBillsTransactions", paramMap);
                        paramMap = null;
                        if (tranLst != null && tranLst.size() > 0) {
                            for (int i = 0; i < tranLst.size(); i++) {
                                paramMap = (HashMap) tranLst.get(i);
                                String authStatus = CommonUtil.convertObjToStr(paramMap.get("AUTHORIZE_STATUS"));
                                String status = CommonUtil.convertObjToStr(paramMap.get("STATUS"));
                                if ((authStatus.equals("REJECTED")) || (status.equals("DELETED"))) {
                                    isTran = true;
                                    authStatus = "";
                                    status = "";
                                } else {
                                    isTran = false;
                                }
                            }
                        } else {
                            isTran = true;
                        }
                        HashMap paramMap1 = new HashMap();
                        paramMap1.put("LODGEMENT_ID", objLodgementMasterTO.getLodgementId());
                        paramMap1.put("BILL_STATUS", objLodgementMasterTO.getBillsStatus());
                        List lst = sqlMap.executeQueryForList("checkBillsLodgementHistory", paramMap1);
                        paramMap1 = null;
                        if (lst != null && lst.size() > 0) {
                            isTran = false;
                            //                            sqlMap.executeUpdate("updateLodgementMasterHistoryTO", objLodgementMasterTO);
                        } else {
                            isTran = true;
                            //                            objLodgementMasterTO.setCreatedDt(currDt);
                            //                            sqlMap.executeUpdate("insertLodgementMasterHistoryTO", objLodgementMasterTO);
                        }
                        lst = null;
                        //                        HashMap txMap = new HashMap();
                        if (isTran) {
                            System.out.println("objLodgem+++++++" + objLodgementMasterTO.getBillsStatus());
                            doAccountHeadCredit(map, command, txMap, branchID);
                            objLodgementMasterTO.setCreatedDt(currDt);
                            objLodgementMasterTO.setStatus(CommonConstants.STATUS_MODIFIED);
                            sqlMap.executeUpdate("insertLodgementMasterHistoryTO", objLodgementMasterTO);
                            isTran = false;
                        }
                        if (objLodgementMasterTO != null) {
                            objLodgementMasterTO.setStatus(CommonConstants.STATUS_MODIFIED);
                            logTO.setData(objLodgementMasterTO.toString());
                            logTO.setPrimaryKey(objLodgementMasterTO.getKeyData());
                            logTO.setStatus(objLodgementMasterTO.getCommand());
                            sqlMap.executeUpdate("updateLodgementMasterTO", objLodgementMasterTO);
                            sqlMap.executeUpdate("updateLodgementMasterHistoryTO", objLodgementMasterTO);
                            logDAO.addToLog(logTO);
                        }
                    }
                }
            }
            if (objLodgementRemitTO != null) {
                if ((objLodgementRemitTO.getRemitStatus() == null) || (objLodgementRemitTO.getRemitStatus().equals(""))) {
                    objLodgementRemitTO.setRemitStatus(CommonConstants.STATUS_CREATED);
                    logTO.setData(objLodgementRemitTO.toString());
                    logTO.setPrimaryKey(objLodgementRemitTO.getKeyData());
                    logTO.setStatus(objLodgementRemitTO.getCommand());
                    sqlMap.executeUpdate("insertLodgementRemitTO", objLodgementRemitTO);
                    logDAO.addToLog(logTO);
                } else {
                    HashMap newMap = new HashMap();
                    newMap.put("BILL_STATUS", objLodgementMasterTO.getBillsStatus());
                    newMap.put("LODGEMENT_ID", objLodgementMasterTO.getLodgementId());
                    List remitList = (List) sqlMap.executeQueryForList("getSelectLodgementRemitTO", newMap);
                    newMap = null;
                    if (remitList != null && remitList.size() > 0) {
                        objLodgementRemitTO.setRemitStatus(CommonConstants.STATUS_MODIFIED);
                        sqlMap.executeUpdate("updateLodgementRemitTO", objLodgementRemitTO);
                    } else {
                        objLodgementRemitTO.setRemitStatus(CommonConstants.STATUS_CREATED);
                        sqlMap.executeUpdate("insertLodgementRemitTO", objLodgementRemitTO);
                    }

                }
            }
            sqlMap.commitTransaction();
        } catch (Exception e) {
            sqlMap.rollbackTransaction();
            e.printStackTrace();
            //            throw new TransRollbackException(e);
            throw e;
        }
    }

    private void deleteData() throws Exception {
        try {
            System.out.println("Will dellllllllllllll");
            sqlMap.startTransaction();
            if (objLodgementMasterTO != null) {
                objLodgementMasterTO.setStatus(CommonConstants.STATUS_DELETED);
                logTO.setData(objLodgementMasterTO.toString());
                logTO.setPrimaryKey(objLodgementMasterTO.getKeyData());
                logTO.setStatus(objLodgementMasterTO.getCommand());
                sqlMap.executeUpdate("deleteLodgementMasterTO", objLodgementMasterTO);
                sqlMap.executeUpdate("deleteLodgementMasterHistoryTO", objLodgementMasterTO);
                logDAO.addToLog(logTO);
            }
            if (objLodgementChequeTO != null) {
                objLodgementChequeTO.setStatus(CommonConstants.STATUS_DELETED);
                logTO.setData(objLodgementChequeTO.toString());
                logTO.setPrimaryKey(objLodgementChequeTO.getKeyData());
                logTO.setStatus(objLodgementChequeTO.getCommand());
                sqlMap.executeUpdate("deleteLodgementChequeTO", objLodgementChequeTO);
                logDAO.addToLog(logTO);
            }
            if (objLodgementHundiTO != null) {
                objLodgementHundiTO.setStatus(CommonConstants.STATUS_DELETED);
                logTO.setData(objLodgementHundiTO.toString());
                logTO.setPrimaryKey(objLodgementHundiTO.getKeyData());
                logTO.setStatus(objLodgementHundiTO.getCommand());
                sqlMap.executeUpdate("deleteLodgementHundiTO", objLodgementHundiTO);
                logDAO.addToLog(logTO);
            }
            if (objLodgementBillRatesTO != null) {
                //                objLodgementBillRatesTO.setLodgeID(lodgementId);
                objLodgementBillRatesTO.setStatus(CommonConstants.STATUS_DELETED);
                logTO.setData(objLodgementBillRatesTO.toString());
                logTO.setPrimaryKey(objLodgementBillRatesTO.getKeyData());
                logTO.setStatus(objLodgementBillRatesTO.getCommand());
                sqlMap.executeUpdate("deleteLodgementBillRatesTO", objLodgementBillRatesTO);
                logDAO.addToLog(logTO);
            }
            updateLodgementInstructions();
            updateLodgementInstr();
            sqlMap.commitTransaction();
        } catch (Exception e) {
            sqlMap.rollbackTransaction();
            e.printStackTrace();
            //            throw new TransRollbackException(e);
            throw e;
        }
    }

    /**
     * Generates BatchId
     */
    private String getBatchId() throws Exception {
        final IDGenerateDAO dao = new IDGenerateDAO();
        final HashMap where = new HashMap();
        where.put(CommonConstants.MAP_WHERE, "BATCH_ID");
        return (String) (dao.executeQuery(where)).get(CommonConstants.DATA);
    }

    public static void main(String str[]) {
        try {
            LodgementDAO dao = new LodgementDAO();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
    HashMap returnData = null;

    public HashMap execute(HashMap map) throws Exception {

        _branchCode = (String) map.get(CommonConstants.BRANCH_ID);
        currDt = ServerUtil.getCurrentDate(_branchCode);
        setSubRegType(CommonUtil.convertObjToStr(map.get("SUB_REG_TYPE")));
        System.out.println("subRegType^&&&&&&&&&&" + subRegType);
        System.out.println("map^&&&&&&&&&&" + map);

        transactionDAO = new TransactionDAO(CommonConstants.DEBIT);
        transactionDAO.setInitiatedBranch(_branchCode);
        transactionDAO.setInitialValuesForLogTO(map);
        logDAO = new LogDAO();
        logTO = new LogTO();

        logTO.setUserId(CommonUtil.convertObjToStr(map.get(CommonConstants.USER_ID)));
        logTO.setBranchId(CommonUtil.convertObjToStr(map.get(CommonConstants.BRANCH_ID)));
        logTO.setIpAddr(CommonUtil.convertObjToStr(map.get(CommonConstants.IP_ADDR)));
        logTO.setModule(CommonUtil.convertObjToStr(map.get(CommonConstants.MODULE)));
        logTO.setScreen(CommonUtil.convertObjToStr(map.get(CommonConstants.SCREEN)));

        branchID = CommonUtil.convertObjToStr(map.get(CommonConstants.BRANCH_ID));
        objLodgementMasterTO = (LodgementMasterTO) map.get("LodgementMasterTO");
        System.out.println("objLodgementMasterTO$$$$$$$$$$$$$$&" + objLodgementMasterTO);
        if (map.containsKey("LodgementChequeTO")) {
            objLodgementChequeTO = (LodgementChequeTO) map.get("LodgementChequeTO");
        }
        if (map.containsKey("LodgementHundiTO")) {
            objLodgementHundiTO = (LodgementHundiTO) map.get("LodgementHundiTO");
        }
        if (map.containsKey("LodgementInstructionsTO")) {
            instructionList = (ArrayList) map.get("LodgementInstructionsTO");
            System.out.println("instructionList((((((((((&&&&&&&&&&&&&&&&" + instructionList);
        }
        if (map.containsKey("LodgementInstrTO")) {
            instrList = (ArrayList) map.get("LodgementInstrTO");
            System.out.println("instrList((((((&&&&&&&&&" + instrList);
        }
        if (map.containsKey("LodgementRemitTO")) {
            objLodgementRemitTO = (LodgementRemitTO) map.get("LodgementRemitTO");
        }
        if (map.containsKey("LodgementBillRatesTO")) {
            objLodgementBillRatesTO = (LodgementBillRatesTO) map.get("LodgementBillRatesTO");
        }
         
        if (map.containsKey(("MULTIPLE_LODGEMENT"))) {
            if(map.containsKey("FINAL_RECORD")){
                if(map.containsKey("MULTIPLE_LODGEMENT_MASTER")){
                   multipleLodgementMasterTOList = (ArrayList) map.get("MULTIPLE_LODGEMENT_MASTER");
                }
            }           
        }    

        command = objLodgementMasterTO.getCommand();

        if (command.equals(CommonConstants.TOSTATUS_INSERT)) {
            String billActivity1 = objLodgementMasterTO.getBillsStatus();
            String prodType1 = CommonUtil.convertObjToStr(objLodgementMasterTO.getProdType());
            System.out.println("billActivity==" + billActivity1 + "prodType1 === " + prodType1);
            HashMap hashData = new HashMap();
            System.out.println("objcttt..." + objLodgementMasterTO);
            hashData.put("PROD_ID", objLodgementMasterTO.getBillsType());
            System.out.println("prod iddd " + objLodgementMasterTO.getBillsType());
            List lstData = sqlMap.executeQueryForList("getOperatingType", hashData);
            hashData = null;
            if (lstData != null && lstData.size() > 0) {
                System.out.println("dfxsdfsdf ");
                hashData = (HashMap) lstData.get(0);
            }
            if (prodType1.equals("OA") && hashData.get("OPERATES_LIKE").equals("INWARD") && !billActivity1.equals("LODGEMENT")) {
                erroMap = checkValidation();
                if (erroMap != null && erroMap.containsKey("ERRORLIST")) {
                    return erroMap;
                }
            }
            insertData(map);
            returnData = new HashMap();
            returnData.put("LODGEMENT_ID", objLodgementMasterTO.getLodgementId());
            System.out.println("@@@@@@@@@@@@@2returnData" + returnData);
        } else if (command.equals(CommonConstants.TOSTATUS_UPDATE)) {
            updateData(map);
        } else if (command.equals(CommonConstants.TOSTATUS_DELETE)) {
            deleteData();
        } else if (map.containsKey("AUTHORIZEMAP") && map.get("AUTHORIZEMAP") != null) {
            System.out.println("In Authorize .... ");
            returnData = new HashMap();
            doAuthorize(map);
            //System.out.println("@@@@@@@@@@@@@returnAuthMap" + returnAuthMap);
            //System.out.println("returnData########"+returnAuthMap.get("SINGLE_TRANS_ID"));
            returnData.put("SINGLE_TRANS_ID", returnAuthMap.get("SINGLE_TRANS_ID"));
        } else {
            throw new NoCommandException();
        }

        destroyObjects();
        System.out.println("returnData########"+returnData.get("SINGLE_TRANS_ID"));
        return returnData;
        
        
    }

    // Added by nithya for multiple bills lodgement
    private void updateMultiLodgementInstructions() throws Exception{
        System.out.println("instructionList............." + instructionList);
        if (instructionList != null) {
            if (instructionList.size() != 0) {
                for (int i = 0; i < instructionList.size(); i++) {
                    LodgementInstructionsTO objLodgementInstructionsTO = (LodgementInstructionsTO) instructionList.get(i);
                    if (objLodgementInstructionsTO.getStatus().equals(CommonConstants.STATUS_DELETED)) {
                        MultipleLodgementMasterTO objMultipleLodgementMasterTO = new MultipleLodgementMasterTO();
                        logTO.setData(objLodgementInstructionsTO.toString());
                        logTO.setPrimaryKey(objLodgementInstructionsTO.getKeyData());
                        logTO.setStatus(objLodgementInstructionsTO.getCommand());
                        objMultipleLodgementMasterTO.setLodgementId(objLodgementInstructionsTO.getLodgementId());
                        objMultipleLodgementMasterTO.setStatus(objLodgementInstructionsTO.getStatus());
                        objMultipleLodgementMasterTO.setInstruction(objLodgementInstructionsTO.getInstruction());
                        objMultipleLodgementMasterTO.setInstAmount(objLodgementInstructionsTO.getAmount());
                        objMultipleLodgementMasterTO.setServiceTax(objLodgementInstructionsTO.getServiceTx());
                        sqlMap.executeUpdate("updateMultipleLodgementInstructions", objMultipleLodgementMasterTO);
                        logDAO.addToLog(logTO);
                    }
                }
            }
        }    
    }
    
    // End
    
    private void updateLodgementInstructions() throws Exception {
        Boolean flag = true;
        System.out.println("instructionList............." + instructionList);
        if (instructionList != null) {
            if (instructionList.size() != 0) {
                for (int i = 0; i < instructionList.size(); i++) {
                    LodgementInstructionsTO objLodgementInstructionsTO = (LodgementInstructionsTO) instructionList.get(i);
                    System.out.println("In objLodgementInstructionsTO$$$$$$$$$$$$$$ .... " + objLodgementInstructionsTO);
                    if (objLodgementInstructionsTO.getStatus().equals(CommonConstants.STATUS_CREATED)) {
                        if (command.equals(CommonConstants.TOSTATUS_INSERT)) {
                            objLodgementInstructionsTO.setLodgementId(lodgementId);
                        }
                        logTO.setData(objLodgementInstructionsTO.toString());
                        logTO.setPrimaryKey(objLodgementInstructionsTO.getKeyData());
                        logTO.setStatus(objLodgementInstructionsTO.getCommand());
                        sqlMap.executeUpdate("insertLodgementInstructionsTO", objLodgementInstructionsTO);
                        logDAO.addToLog(logTO);
                    } else if (objLodgementInstructionsTO.getStatus().equals(CommonConstants.STATUS_MODIFIED)) {
                        System.out.println("In objLodgementInstructionsTO$$$$$$$$$$$$$$ .... " + objLodgementInstructionsTO);

                        logTO.setData(objLodgementInstructionsTO.toString());
                        logTO.setPrimaryKey(objLodgementInstructionsTO.getKeyData());
                        logTO.setStatus(objLodgementInstructionsTO.getCommand());
                        //INSERT INSTRUCTION
                        HashMap instMap = new HashMap();
                        instMap.put("LODGEMENT_ID", objLodgementMasterTO.getLodgementId());
                        instMap.put("INSTRUCTION", objLodgementInstructionsTO.getInstruction());
                        List lstInst = sqlMap.executeQueryForList("getBillsExtInst", instMap);
                        instMap = null;
                        //comm and added by jjjj
//                        if(lstInst != null && lstInst.size() > 0){
//                            sqlMap.executeUpdate("updateLodgementInstructionsTO", objLodgementInstructionsTO);
//                        }else{
//                            sqlMap.executeUpdate("insertLodgementInstructionsTO", objLodgementInstructionsTO);
//                        }
                        if (flag) {
                            sqlMap.executeUpdate("delLodgementInstructionsTOj", objLodgementInstructionsTO);
                            flag = false;
                        }
                        sqlMap.executeUpdate("insertLodgementInstructionsTO", objLodgementInstructionsTO);

                        //////////////////////////
                        lstInst = null;
                        //                        sqlMap.executeUpdate("updateLodgementInstructionsTO", objLodgementInstructionsTO);
                        logDAO.addToLog(logTO);
                    } else if (objLodgementInstructionsTO.getStatus().equals(CommonConstants.STATUS_DELETED)) {
                        logTO.setData(objLodgementInstructionsTO.toString());
                        logTO.setPrimaryKey(objLodgementInstructionsTO.getKeyData());
                        logTO.setStatus(objLodgementInstructionsTO.getCommand());
                        //                        sqlMap.executeUpdate("deleteLodgementInstructionsTO", objLodgementInstructionsTO);
                        sqlMap.executeUpdate("updateLodgementInstructionsTO", objLodgementInstructionsTO);
                        //updateMultiLodgementInstructions();
                        logDAO.addToLog(logTO);
                    }
                }
            }
        }
    }

    private void updateLodgementInstr() throws Exception {
        System.out.println("instrList......" + instrList);
        boolean flag = true;
        if (instrList != null) {
            if (instrList.size() != 0) {
                for (int i = 0; i < instrList.size(); i++) {
                    LodgementInstrTO objLodgementInstrTO = (LodgementInstrTO) instrList.get(i);
                    System.out.println("In objLodgementInstructionsTO$$$$$$$$$$$$$$ .... " + objLodgementInstrTO);
                    if (objLodgementInstrTO.getInstStatus().equals(CommonConstants.STATUS_CREATED)) {
                        if (command.equals(CommonConstants.TOSTATUS_INSERT)) {
                            objLodgementInstrTO.setInstLodgementId(lodgementId);
                        }
                        logTO.setData(objLodgementInstrTO.toString());
                        logTO.setPrimaryKey(objLodgementInstrTO.getKeyData());
                        logTO.setStatus(objLodgementInstrTO.getCommand());
                        sqlMap.executeUpdate("insertLodgementInstrTO", objLodgementInstrTO);
                        logDAO.addToLog(logTO);
                    } else if (objLodgementInstrTO.getInstStatus().equals(CommonConstants.STATUS_MODIFIED)) {
                        System.out.println("In objLodgementInstrTO .... " + objLodgementInstrTO);
                        logTO.setData(objLodgementInstrTO.toString());
                        logTO.setPrimaryKey(objLodgementInstrTO.getKeyData());
                        logTO.setStatus(objLodgementInstrTO.getCommand());
                        if (flag) {
                            sqlMap.executeUpdate("delLodgementInstrTOj", objLodgementInstrTO);
                            flag = false;
                        }
                        sqlMap.executeUpdate("insertLodgementInstrTO", objLodgementInstrTO);
                        logDAO.addToLog(logTO);
                    } else if (objLodgementInstrTO.getInstStatus().equals(CommonConstants.STATUS_DELETED)) {
                        logTO.setData(objLodgementInstrTO.toString());
                        logTO.setPrimaryKey(objLodgementInstrTO.getKeyData());
                        logTO.setStatus(objLodgementInstrTO.getCommand());
                        sqlMap.executeUpdate("deleteLodgementInstrTO", objLodgementInstrTO);
                        logDAO.addToLog(logTO);
                    }
                }
            }
        }
    }

    public HashMap executeQuery(HashMap obj) throws Exception {
        _branchCode = (String) obj.get(CommonConstants.BRANCH_ID);
        currDt = ServerUtil.getCurrentDate(_branchCode);
        return getData(obj);
    }

    //Added By Suresh
    public InvestmentsTransTO getInvestmentsTransTO(String cmd, double intTrfAm, HashMap acctDtlMap, HashMap map) {
        HashMap whereMap = new HashMap();
        InvestmentsTransTO objgetInvestmentsTransTO = new InvestmentsTransTO();
        acctDtlMap.put("INT_PAY_ACC_NO", CommonUtil.convertObjToStr(acctDtlMap.get("INVESTMENT_ACC_NO")));
        List invList = ServerUtil.executeQuery("getInvestmentDetails", acctDtlMap);
        if (invList != null && invList.size() > 0) {
            whereMap = (HashMap) invList.get(0);
            objgetInvestmentsTransTO.setCommand(cmd);
            objgetInvestmentsTransTO.setStatus(CommonConstants.STATUS_CREATED);
            objgetInvestmentsTransTO.setInvestmentBehaves(CommonUtil.convertObjToStr(whereMap.get("INVESTMENT_TYPE")));
            objgetInvestmentsTransTO.setInvestmentID(CommonUtil.convertObjToStr(whereMap.get("INVESTMENT_PROD_ID")));
            objgetInvestmentsTransTO.setInvestment_internal_Id(CommonUtil.convertObjToStr(whereMap.get("INVESTMENT_ID")));
            objgetInvestmentsTransTO.setInvestment_Ref_No(CommonUtil.convertObjToStr(whereMap.get("INVESTMENT_REF_NO")));
            objgetInvestmentsTransTO.setInvestmentName(CommonUtil.convertObjToStr(whereMap.get("INVESTMENT_PROD_DESC")));
            objgetInvestmentsTransTO.setTransDT(currDt);
            objgetInvestmentsTransTO.setTransType("DEBIT");
            objgetInvestmentsTransTO.setTrnCode("Deposit");
            objgetInvestmentsTransTO.setAmount(new Double(0.0));
            objgetInvestmentsTransTO.setPurchaseDt(currDt);
            objgetInvestmentsTransTO.setInvestmentAmount(new Double(intTrfAm));
            objgetInvestmentsTransTO.setStatusBy(CommonUtil.convertObjToStr(map.get("USER_ID")));
            objgetInvestmentsTransTO.setStatusDt(currDt);
            objgetInvestmentsTransTO.setDividendAmount(new Double(0));
            objgetInvestmentsTransTO.setLastIntPaidDate(currDt);
            objgetInvestmentsTransTO.setInitiatedBranch(branchID);
            objgetInvestmentsTransTO.setPurchaseMode("SHARE_PAYMENT");
            if (acctDtlMap.containsKey("BATCH_ID")) {
                objgetInvestmentsTransTO.setBatchID(CommonUtil.convertObjToStr(acctDtlMap.get("BATCH_ID")));
            }
        }
        return objgetInvestmentsTransTO;
    }

    //Added By Suresh
    public InvestmentsTransTO getInvestmentsCreditTransTO(String cmd, double intTrfAm, HashMap acctDtlMap, HashMap map) {
        HashMap whereMap = new HashMap();
        InvestmentsTransTO objgetInvestmentsTransTO = new InvestmentsTransTO();
        acctDtlMap.put("INT_PAY_ACC_NO", CommonUtil.convertObjToStr(acctDtlMap.get("INVESTMENT_ACC_NO")));
        List invList = ServerUtil.executeQuery("getInvestmentDetails", acctDtlMap);
        if (invList != null && invList.size() > 0) {
            whereMap = (HashMap) invList.get(0);
            objgetInvestmentsTransTO.setCommand(cmd);
            objgetInvestmentsTransTO.setStatus(CommonConstants.STATUS_CREATED);
            objgetInvestmentsTransTO.setInvestmentBehaves(CommonUtil.convertObjToStr(whereMap.get("INVESTMENT_TYPE")));
            objgetInvestmentsTransTO.setInvestmentID(CommonUtil.convertObjToStr(whereMap.get("INVESTMENT_PROD_ID")));
            objgetInvestmentsTransTO.setInvestment_internal_Id(CommonUtil.convertObjToStr(whereMap.get("INVESTMENT_ID")));
            objgetInvestmentsTransTO.setInvestment_Ref_No(CommonUtil.convertObjToStr(whereMap.get("INVESTMENT_REF_NO")));
            objgetInvestmentsTransTO.setInvestmentName(CommonUtil.convertObjToStr(whereMap.get("INVESTMENT_PROD_DESC")));
            objgetInvestmentsTransTO.setTransDT(currDt);
            objgetInvestmentsTransTO.setTransType("CREDIT");
            objgetInvestmentsTransTO.setTrnCode("Withdrawal");
            objgetInvestmentsTransTO.setAmount(new Double(0.0));
            objgetInvestmentsTransTO.setPurchaseDt(currDt);
            objgetInvestmentsTransTO.setInvestmentAmount(new Double(intTrfAm));
            objgetInvestmentsTransTO.setStatusBy(CommonUtil.convertObjToStr(map.get("USER_ID")));
            objgetInvestmentsTransTO.setStatusDt(currDt);
            objgetInvestmentsTransTO.setDividendAmount(new Double(0));
            objgetInvestmentsTransTO.setLastIntPaidDate(currDt);
            objgetInvestmentsTransTO.setInitiatedBranch(branchID);
            objgetInvestmentsTransTO.setPurchaseMode("SHARE_PAYMENT");
            if (acctDtlMap.containsKey("BATCH_ID")) {
                objgetInvestmentsTransTO.setBatchID(CommonUtil.convertObjToStr(acctDtlMap.get("BATCH_ID")));
            }
        }
        return objgetInvestmentsTransTO;
    }
	//added by babu 19/01/2015 for inward charge transaction
private double insertInwardChargeTrans(ArrayList instructionList,HashMap txMap,ArrayList transferList,HashMap acHeads,
        double transAmt,HashMap obj){
    double totChargeAmt=0;
    try{
         //babu added for inward charge transaction
                             double temp = 0;
                             TxTransferTO transferTo=null;
                            for (int i = 0; i < instructionList.size(); i++) {
                                System.out.println("instructionList.size()**inwaards******" + instructionList.size());
                                objLodgementInstTO = (LodgementInstructionsTO) instructionList.get(i);
                                System.out.println("objLodgementInstTO***inwartd*****" + objLodgementInstTO);
                                String status = CommonUtil.convertObjToStr(objLodgementInstTO.getStatus());
                                if (!status.equals("DELETED")) {
                                   // if (objLodgementInstTO.getInstruction().equals("DISHONOUR_CHARGES") || objLodgementInstTO.getInstruction().equals("BANK_CHARGES")) {

                                        transactionDAO.setCommandMode(CommonConstants.TOSTATUS_INSERT);
                                        temp = temp + ((CommonUtil.convertObjToDouble(objLodgementInstTO.getAmount()).doubleValue())
                                                + (CommonUtil.convertObjToDouble(objLodgementInstTO.getServiceTx()).doubleValue()));
                                        if (!objLodgementMasterTO.getProdType().equals("GL")) {
                                            txMap.put(TransferTrans.DR_AC_HD, null);
                                            txMap.put(TransferTrans.DR_ACT_NUM, objLodgementMasterTO.getBorrowAcctNum());
                                            txMap.put(TransferTrans.DR_PROD_ID, objLodgementMasterTO.getBorrowProdId());
                                            txMap.put("TRANS_MOD_TYPE", objLodgementMasterTO.getProdType());
                                        } else {
                                            txMap.put(TransferTrans.DR_AC_HD, objLodgementMasterTO.getBorrowAcctNum());
                                            txMap.put("TRANS_MOD_TYPE", "GL");
                                        }
                                        txMap.put(TransferTrans.DR_INST_TYPE, "VOUCHER");
                                        txMap.put(TransferTrans.DR_INSTRUMENT_1, "");
                                        txMap.put(TransferTrans.DR_INSTRUMENT_2, "");
                                        txMap.put(TransferTrans.DR_INST_DATE, objLodgementChequeTO.getInstrumentDt());        
                                        txMap.put(TransferTrans.DR_BRANCH, CommonUtil.convertObjToStr(obj.get("INTER_BRANCH_ID")));
                                        txMap.put(TransferTrans.CURRENCY, "INR");
                                        txMap.put(TransferTrans.DR_PROD_TYPE, objLodgementMasterTO.getProdType());
                                        txMap.put(TransferTrans.CR_BRANCH, branchID);
                                        //                                txMap.put(TransferTrans.DR_INST_TYPE, objLodgementMasterTO.getBillsStatus());
                                        txMap.put(TransferTrans.PARTICULARS, "Chrg LodgeID : " + objLodgementMasterTO.getLodgementId() + " Dishonour");
                                        txMap.put(TransferTrans.AUTHORIZE_STATUS_2, "ENTERED_AMOUNT");
                                        transAmt = temp;
                                        totChargeAmt=totChargeAmt+transAmt;
                                        transactionDAO.setTransType(CommonConstants.CREDIT);
                                        transferTo = transactionDAO.addTransferDebitLocal(txMap, transAmt);
                                        transferTo.setStatusBy(CommonUtil.convertObjToStr(obj.get("USER_ID")));
                                        //transferTo.setInstType(objLodgementMasterTO.getBillsStatus());
                                        transferList.add(transferTo);
                                        if ((CommonUtil.convertObjToDouble(objLodgementInstTO.getAmount()).doubleValue()) > 0) {
                                            txMap.put(TransferTrans.CR_AC_HD, (String) acHeads.get("CHRG_AC_HD"));
                                            txMap.put(TransferTrans.CR_PROD_TYPE, TransactionFactory.GL);
                                            //                                    txMap.put(TransferTrans.DR_INST_TYPE, objLodgementMasterTO.getBillsStatus());
                                            txMap.put(TransferTrans.PARTICULARS, "Chrg LodgeID : " + objLodgementMasterTO.getLodgementId() + " Dishonour");
                                            txMap.put(TransferTrans.AUTHORIZE_STATUS_2, "ENTERED_AMOUNT");
                                            txMap.put("TRANS_MOD_TYPE", "GL");
                                            transAmt = CommonUtil.convertObjToDouble(objLodgementInstTO.getAmount()).doubleValue();
                                            transactionDAO.setTransType(CommonConstants.DEBIT);
                                            transferTo = transactionDAO.addTransferCreditLocal(txMap, transAmt);
                                            transferTo.setInstType(objLodgementMasterTO.getBillsStatus());
                                            transferTo.setStatusBy(CommonUtil.convertObjToStr(obj.get("USER_ID")));
                                            transferList.add(transferTo);
                                            transAmt = 0;
                                        }
                                        if ((CommonUtil.convertObjToDouble(objLodgementInstTO.getServiceTx()).doubleValue()) > 0) {
                                            txMap.put(TransferTrans.CR_AC_HD, (String) acHeads.get("SERVICE_TAX_HD"));
                                            txMap.put(TransferTrans.CR_PROD_TYPE, TransactionFactory.GL);
                                            //                                    txMap.put(TransferTrans.DR_INST_TYPE, objLodgementMasterTO.getBillsStatus());
                                            txMap.put(TransferTrans.PARTICULARS, "ServiceTax LodgeID : " + objLodgementMasterTO.getLodgementId() + " Dishonour");
                                            txMap.put(TransferTrans.AUTHORIZE_STATUS_2, "ENTERED_AMOUNT");
                                            txMap.put("TRANS_MOD_TYPE", "GL");
                                            transAmt = CommonUtil.convertObjToDouble(objLodgementInstTO.getServiceTx()).doubleValue();
                                            transactionDAO.setTransType(CommonConstants.DEBIT);
                                            transferTo = transactionDAO.addTransferCreditLocal(txMap, transAmt);
                                            transferTo.setInstType(objLodgementMasterTO.getBillsStatus());
                                            transferTo.setStatusBy(CommonUtil.convertObjToStr(obj.get("USER_ID")));
                                            transferList.add(transferTo);
                                            transAmt = 0;
                                        }
                                        transactionDAO.setTransactionType(transactionDAO.TRANSFER);
                                        transactionDAO.doTransferLocal(transferList, branchID);

                                        //INSERT INSTRUCTION
                                        HashMap instMap = new HashMap();
                                        instMap.put("LODGEMENT_ID", objLodgementMasterTO.getLodgementId());
                                        instMap.put("INSTRUCTION", objLodgementInstTO.getInstruction());
                                        List lstInst = sqlMap.executeQueryForList("getBillsExtInst", instMap);
                                        instMap = null;
                                        if (lstInst != null && lstInst.size() > 0) {
                                            sqlMap.executeUpdate("updateLodgementInstructionsTO", objLodgementInstTO);
                                        } else {
                                            sqlMap.executeUpdate("insertLodgementInstructionsTO", objLodgementInstTO);
                                        }
                                        lstInst = null;
                                   // }
                                    status = null;
                                }
                            }
                            //end
    }
    catch(Exception e){
        e.printStackTrace();
    }
    return totChargeAmt;
}
    private void doAccountHeadCredit(HashMap obj, String command, HashMap txMap, String branchID) throws Exception {
        HashMap prodIdTypMap = new HashMap();
        prodIdTypMap.put("BILLS_TYP", objLodgementMasterTO.getBillsType());
        prodIdTypMap.put("LODGE_ID", objLodgementMasterTO.getLodgementId());
        List lstProdIdTyp = sqlMap.executeQueryForList("getProdIdTyp", prodIdTypMap);
        if (lstProdIdTyp != null && lstProdIdTyp.size() > 0) {
            prodIdTypMap = new HashMap();
            prodIdTypMap = (HashMap) lstProdIdTyp.get(0);
        }
        //changes made by vinay
        HashMap acHeads = (HashMap) sqlMap.executeQueryForObject("getBillsAccountHeads", objLodgementMasterTO.getBillsType());
        // HashMap otherHeads=(HashMap)sqlMap.executeQueryForList("getotherBankAcntHeads", objLodgementMasterTO.getBillsType());
        if (!prodIdTypMap.get("PROD_TYPE").equals("GL")) {
            acHeadMap = new HashMap();
            acHeadMap.put("PROD_ID", prodIdTypMap.get("PROD_ID"));
            List lst = sqlMap.executeQueryForList("getAccountHeadProd" + prodIdTypMap.get("PROD_TYPE"), acHeadMap);
            if (lst != null && lst.size() > 0) {
                acHeadMap = (HashMap) lst.get(0);
                System.out.println("acHeadMapacHeadMap" + acHeadMap);
            }
        }
        HashMap prodIdTypMap1 = new HashMap();
        prodIdTypMap1.put("BILLS_TYP", objLodgementMasterTO.getBillsType());
        prodIdTypMap1.put("LODGE_ID", objLodgementMasterTO.getLodgementId());
        List lstProdIdTyp1 = sqlMap.executeQueryForList("getProdIdTyp1", prodIdTypMap1);        
        HashMap acHeadMap1 = new HashMap();
        if (lstProdIdTyp1 != null && lstProdIdTyp1.size() > 0) {
            prodIdTypMap1 = new HashMap();
            prodIdTypMap1 = (HashMap) lstProdIdTyp1.get(0);
            if (prodIdTypMap1 != null && prodIdTypMap1.containsKey("REC_OTHER_BANK") && prodIdTypMap1.get("REC_OTHER_BANK") != null) {
                acHeadMap1 = new HashMap();
                acHeadMap1.put("REC_OTHER_BANK", prodIdTypMap1.get("REC_OTHER_BANK"));
                //acHeadMap1.put("PROD_TYPE",prodIdTypMap1.get("PROD_TYPE"));
                List lst1 = sqlMap.executeQueryForList("getAccountHeadProd1", acHeadMap1);
                if (lst1 != null && lst1.size() > 0) {
                    acHeadMap1 = (HashMap) lst1.get(0);
                    System.out.println("acHeadMapacHeadMap" + acHeadMap1);
                }
            }
        }
        System.out.println("Ac heads in doAccountHeadCredit : " + acHeads);
        System.out.println("LODGEMENT  : " + objLodgementMasterTO.getTranType());
        String coll_Obc_From_Cust = "";
        String creditObcHd = "";
        coll_Obc_From_Cust = CommonUtil.convertObjToStr(acHeads.get("COLL_OBC_FROM_CUST"));
        creditObcHd = CommonUtil.convertObjToStr(acHeads.get("CREDIT_OBC_TO"));        
        txMap = createMap(txMap, objLodgementMasterTO, branchID);
        transactionDAO.setBatchDate(batchDate = currDt);
        transactionDAO.setLinkBatchID(objLodgementMasterTO.getLodgementId());
        double transAmt = 0.0;
        TxTransferTO transferTo;
        ArrayList transferList = new ArrayList(); // for local transfer
        if ((!getSubRegType().equals("ICC")) && (!getSubRegType().equals("CPD"))) {
            //new mode            
            if (CommonUtil.convertObjToDouble(objLodgementChequeTO.getInstrumentAmount()).doubleValue() > 0) {
                transactionDAO.setCommandMode(CommonConstants.TOSTATUS_INSERT);
                HashMap hashData = new HashMap();                
                hashData.put("PROD_ID", objLodgementMasterTO.getBillsType());                
                List lstData = sqlMap.executeQueryForList("getOperatingType", hashData);
                hashData = null;
                if (lstData != null && lstData.size() > 0) {                    
                    hashData = (HashMap) lstData.get(0);
                }
                if (hashData.get("OPERATES_LIKE").equals("INWARD")) {                    
                    if (objLodgementMasterTO.getBillsStatus().equals("REALIZE")) {
                        if (objLodgementMasterTO.getTranType().equals("TRANSFER")) {
                            txMap = new HashMap();
                            if (!objLodgementMasterTO.getProdType().equals("GL")) {
                                txMap.put(TransferTrans.DR_AC_HD, null);
                                txMap.put(TransferTrans.DR_ACT_NUM, objLodgementMasterTO.getBorrowAcctNum());
                                txMap.put(TransferTrans.DR_PROD_ID, objLodgementMasterTO.getBorrowProdId());
                                txMap.put("TRANS_MOD_TYPE", objLodgementMasterTO.getProdType());
                            } else {
                                txMap.put(TransferTrans.DR_AC_HD, objLodgementMasterTO.getBorrowAcctNum());
                                txMap.put("TRANS_MOD_TYPE", "GL");
                            }
                            txMap.put(TransferTrans.DR_BRANCH, CommonUtil.convertObjToStr(obj.get("INTER_BRANCH_ID")));
                            txMap.put(TransferTrans.DR_INST_TYPE, "CHEQUE");
                            txMap.put(TransferTrans.DR_INSTRUMENT_1, objLodgementChequeTO.getInstrPrefix());
                            txMap.put(TransferTrans.DR_INSTRUMENT_2, objLodgementChequeTO.getInstrumentNo());
                            txMap.put(TransferTrans.DR_INST_DATE, objLodgementChequeTO.getInstrumentDt());
                            txMap.put(TransferTrans.CURRENCY, "INR");
                            txMap.put(TransferTrans.DR_PROD_TYPE, objLodgementMasterTO.getProdType());
                            txMap.put(TransferTrans.CR_PROD_TYPE, TransactionFactory.GL);
                            txMap.put(TransferTrans.CR_BRANCH, branchID);                            
                            transAmt = CommonUtil.convertObjToDouble(objLodgementChequeTO.getInstrumentAmount()).doubleValue();
                            txMap.put(TransferTrans.PARTICULARS, "LodgeID : " + objLodgementMasterTO.getLodgementId() + " Realize ");
                            txMap.put(TransferTrans.AUTHORIZE_STATUS_2, "ENTERED_AMOUNT");
                            txMap.put("SCREEN_NAME",(String) obj.get(CommonConstants.SCREEN));// Added by nithya on 23-09-2016 for inserting screen name for transactions
                            transferTo = transactionDAO.addTransferDebitLocal(txMap, transAmt);
                            transferTo.setInstType("CHEQUE");
                            transferTo.setStatusBy(CommonUtil.convertObjToStr(obj.get("USER_ID")));
                            transferList.add(transferTo);
                            txMap=new HashMap();
                            if (!objLodgementMasterTO.getProdType().equals("GL")) {
                                txMap.put(TransferTrans.DR_AC_HD, null);
                                txMap.put(TransferTrans.DR_ACT_NUM, objLodgementMasterTO.getBorrowAcctNum());
                                txMap.put(TransferTrans.DR_PROD_ID, objLodgementMasterTO.getBorrowProdId());
                                txMap.put("TRANS_MOD_TYPE", objLodgementMasterTO.getProdType());
                            } else {
                                txMap.put(TransferTrans.DR_AC_HD, objLodgementMasterTO.getBorrowAcctNum());
                                txMap.put("TRANS_MOD_TYPE", "GL");
                            }
                            txMap.put(TransferTrans.DR_BRANCH, CommonUtil.convertObjToStr(obj.get("INTER_BRANCH_ID")));
                            txMap.put(TransferTrans.CURRENCY, "INR");
                            txMap.put(TransferTrans.DR_PROD_TYPE, objLodgementMasterTO.getProdType());
                            txMap.put(TransferTrans.CR_PROD_TYPE, TransactionFactory.GL);
                            txMap.put(TransferTrans.CR_BRANCH, branchID);
                            txMap.put(TransferTrans.PARTICULARS, "LodgeID : " + objLodgementMasterTO.getLodgementId() + " Realize");
                            txMap.put(TransferTrans.AUTHORIZE_STATUS_2, "ENTERED_AMOUNT");
                            txMap.put(TransferTrans.PARTICULARS, "A/c No-" + " " + objLodgementMasterTO.getBorrowAcctNum());
                            double tempIn = 0;
                            tempIn = CommonUtil.convertObjToDouble(objLodgementChequeTO.getInstrumentAmount()).doubleValue();
                            for (int i = 0; i < instructionList.size(); i++) {
                                objLodgementInstTO = (LodgementInstructionsTO) instructionList.get(i);
                                String status = CommonUtil.convertObjToStr(objLodgementInstTO.getStatus());
                                if (!status.equals("DELETED")) {
                                    transAmt = CommonUtil.convertObjToDouble(objLodgementInstTO.getAmount()).doubleValue();
                                    txMap.put(TransferTrans.CR_AC_HD, (String) acHeads.get("CHRG_AC_HD"));  // credit to Postage Charge account head......
                                    //                    txMap.put(TransferTrans.DR_INST_TYPE, objLodgementMasterTO.getBillsStatus());
                                    txMap.put(TransferTrans.PARTICULARS, "BankCharges LodgeID : " + objLodgementMasterTO.getLodgementId() + " Realize");
                                    txMap.put("SCREEN_NAME",(String) obj.get(CommonConstants.SCREEN));// Added by nithya on 23-09-2016 for inserting screen name for transactions
                                    transferTo = transactionDAO.addTransferCreditLocal(txMap, transAmt);
                                    transferTo.setInstType(objLodgementMasterTO.getBillsStatus());
                                    transferList.add(transferTo);
                                    if (CommonUtil.convertObjToDouble(objLodgementInstTO.getServiceTx()).doubleValue() > 0) {
                                        transAmt = CommonUtil.convertObjToDouble(objLodgementInstTO.getServiceTx()).doubleValue();
                                        txMap.put(TransferTrans.CR_AC_HD, (String) acHeads.get("SERVICE_TAX_HD"));  // credit to Postage Charge account head......
                                        txMap.put(TransferTrans.PARTICULARS, "ServiceTax LodgeID : " + objLodgementMasterTO.getLodgementId() + " Realize");
                                        txMap.put(TransferTrans.AUTHORIZE_STATUS_2, "ENTERED_AMOUNT");
                                        txMap.put("TRANS_MOD_TYPE", "GL");
                                        txMap.put("SCREEN_NAME",(String) obj.get(CommonConstants.SCREEN));// Added by nithya on 23-09-2016 for inserting screen name for transactions
                                        transferTo = transactionDAO.addTransferCreditLocal(txMap, transAmt);
                                        transferTo.setInstType(objLodgementMasterTO.getBillsStatus());
                                          transferTo.setStatusBy(CommonUtil.convertObjToStr(obj.get("USER_ID")));
                                        transferList.add(transferTo);
                                    }
                                    tempIn = tempIn - (CommonUtil.convertObjToDouble(objLodgementInstTO.getAmount()).doubleValue())
                                            - (CommonUtil.convertObjToDouble(objLodgementInstTO.getServiceTx()).doubleValue());
                                    //INSERT INSTRUCTION
                                    HashMap instMap = new HashMap();
                                    instMap.put("LODGEMENT_ID", objLodgementMasterTO.getLodgementId());
                                    instMap.put("INSTRUCTION", objLodgementInstTO.getInstruction());
                                    List lstInst = sqlMap.executeQueryForList("getBillsExtInst", instMap);
                                    instMap = null;
                                    if (lstInst != null && lstInst.size() > 0) {
                                        sqlMap.executeUpdate("updateLodgementInstructionsTO", objLodgementInstTO);
                                    } else {
                                        sqlMap.executeUpdate("insertLodgementInstructionsTO", objLodgementInstTO);
                                    }
                                    lstInst = null;
                                    status = null;
                                }
                            }                           
                            if (objLodgementRemitTO.getRemitProdId().equalsIgnoreCase("PO")) {
                                txMap.put(TransferTrans.CR_AC_HD, (String) acHeads.get("OTHER_HD")); // credit to Remittance Issue account head......
                            } else if (objLodgementRemitTO.getRemitProdId().equalsIgnoreCase("IBR")) {
                                txMap.put(TransferTrans.CR_AC_HD, (String) acHeads.get("IBR_AC_HD"));
                            } else {
                                // Added by nithya on 13-07-2016 for 4706    
                                if (obj.get("OTHER_BANK_PROD_TYPE") != null && obj.get("OTHER_BANK_PROD_TYPE").equals("AB")) {
                                    obj.put("INVESTMENT_ACC_NO", CommonUtil.convertObjToStr(obj.get("OTHER_BANK_HEAD")));
                                    HashMap achdMap = new HashMap();
                                    List achdLst = ServerUtil.executeQuery("getSelectOthrBankAcHd", obj);
                                    if (achdLst != null && achdLst.size() > 0) {
                                        achdMap = new HashMap();
                                        achdMap = (HashMap) achdLst.get(0);
                                        txMap.put(TransferTrans.CR_ACT_NUM, achdMap.get("ACT_MASTER_ID"));
                                        txMap.put(TransferTrans.CR_PROD_TYPE, TransactionFactory.OTHERBANKACTS);
                                        txMap.put(TransferTrans.CR_PROD_ID, CommonUtil.convertObjToStr(objLodgementMasterTO.getRecOtherBank()));
                                        txMap.put(TransferTrans.DR_PROD_TYPE, objLodgementMasterTO.getProdType());
                                        txMap.put(TransferTrans.CR_AC_HD, (String) achdMap.get("PRINCIPAL_AC_HD"));
                                    }else{
                                        txMap.put(TransferTrans.CR_AC_HD, (String) acHeads.get("DD_AC_HD"));
                                    }
                                }else{
                                    txMap.put(TransferTrans.CR_AC_HD, (String) acHeads.get("DD_AC_HD"));  
                                }                               
                            }
                            transAmt = tempIn;
                            txMap.put(TransferTrans.PARTICULARS, "LodgeID : " + objLodgementMasterTO.getLodgementId() + " Realize");
                            txMap.put(TransferTrans.AUTHORIZE_STATUS_2, "ENTERED_AMOUNT");
                            txMap.put("TRANS_MOD_TYPE", "GL");
                            txMap.put("SCREEN_NAME",(String) obj.get(CommonConstants.SCREEN));// Added by nithya on 23-09-2016 for inserting screen name for transactions
                            transferTo = transactionDAO.addTransferCreditLocal(txMap, transAmt);
                            transferTo.setInstType(objLodgementMasterTO.getBillsStatus());
                              transferTo.setStatusBy(CommonUtil.convertObjToStr(obj.get("USER_ID")));
                            transferList.add(transferTo);
                            transAmt = 0;
                            transactionDAO.setCommandMode(command);
                            transactionDAO.setTransactionType(transactionDAO.TRANSFER);
                            transactionDAO.doTransferLocal(transferList, branchID);
                            if ((objLodgementRemitTO.getRemitProdId().equals("PO")) || (objLodgementRemitTO.getRemitProdId().equals("DD"))) {
                                TransactionTO transactionTODebit = new TransactionTO();
                                LinkedHashMap notDeleteMap = new LinkedHashMap();
                                LinkedHashMap transferMap = new LinkedHashMap();
                                HashMap remittanceMap = new HashMap();
                                remittanceIssueDAO = new RemittanceIssueDAO();
                                remittanceIssueTO = new RemittanceIssueTO();
                                String favouringName = objLodgementRemitTO.getRemitFavouringIn();
                                transactionTODebit.setApplName(favouringName);
                                transactionTODebit.setTransAmt(new Double(tempIn));
                                transactionTODebit.setProductId(objLodgementMasterTO.getBorrowProdId());
                                transactionTODebit.setProductType(objLodgementMasterTO.getProdType());
                                transactionTODebit.setDebitAcctNo(objLodgementMasterTO.getBorrowAcctNum());
                                transactionTODebit.setTransType("TRANSFER");
                                transactionTODebit.setInstType(objLodgementMasterTO.getInstrumentType());
                                transactionTODebit.setChequeNo(objLodgementMasterTO.getProdId());
                                transactionTODebit.setChequeNo2(objLodgementChequeTO.getInstrumentNo());
                                transactionTODebit.setChequeDt(objLodgementChequeTO.getInstrumentDt());
                                remittanceIssueDAO.setFromotherDAo(false);
                                notDeleteMap.put(String.valueOf(1), transactionTODebit);
                                transferMap.put("NOT_DELETED_TRANS_TOs", notDeleteMap);
                                remittanceMap.put("TransactionTO", transferMap);
                                remittanceMap.put(CommonConstants.BRANCH_ID, branchID);
                                remittanceMap.put("OPERATION_MODE", "ISSUE");
                                remittanceMap.put("AUTHORIZEMAP", null);
                                remittanceMap.put("USER_ID", obj.get("USER_ID"));
                                remittanceMap.put("MODULE", "Remittance");
                                remittanceMap.put("MODE", "INSERT");
                                remittanceMap.put("SCREEN", "Issue");
                                remittanceIssueTO.setDraweeBranchCode(objLodgementRemitTO.getRemitDraweeBranchCode());
                                remittanceIssueTO.setDraweeBank(objLodgementRemitTO.getRemitDraweeBank());
                                remittanceIssueTO.setAmount(new Double(tempIn));
                                remittanceIssueTO.setCategory("GENERAL_CATEGORY");
                                remittanceIssueTO.setCity(objLodgementRemitTO.getRemitCity());
                                remittanceIssueTO.setProdId(objLodgementRemitTO.getRemitProdId());
                                remittanceIssueTO.setFavouring(favouringName);
                                remittanceIssueTO.setBranchId(branchID);
                                remittanceIssueTO.setCommand("INSERT");
                                remittanceIssueTO.setTotalAmt(new Double(tempIn));
                                remittanceIssueTO.setExchange(new Double(0.0));
                                remittanceIssueTO.setPostage(new Double(0.0));
                                remittanceIssueTO.setOtherCharges(new Double(0.0));
                                remittanceIssueTO.setAuthorizeRemark("FROM_BILLS_MODULE");
                                remittanceIssueTO.setRemarks(objLodgementMasterTO.getLodgementId());
                                remittanceIssueTO.setStatusDt(ServerUtil.getCurrentDate(branchID));
                                remittanceIssueTO.setStatusBy(CommonUtil.convertObjToStr(obj.get("USER_ID")));
                                remittanceIssueTO.setInstrumentNo1("PO");
                                remittanceIssueTO.setInstrumentNo2("");
                                LinkedHashMap remitMap = new LinkedHashMap();
                                LinkedHashMap remMap = new LinkedHashMap();
                                remMap.put(String.valueOf(1), remittanceIssueTO);
                                remitMap.put("NOT_DELETED_ISSUE_TOs", remMap);
                                remittanceMap.put("RemittanceIssueTO", remitMap);
                                remittanceIssueDAO.execute(remittanceMap);
                                remMap = null;
                                remitMap = null;
                                remittanceMap = null;
                                notDeleteMap = null;
                                transferMap = null;
                                favouringName = null;
                            }
                            tempIn = 0;                            
                        }
                        if (objLodgementMasterTO.getTranType().equals("CASH")) {
                            HashMap acHeadMap = new HashMap();
                            acHeadMap.put("PROD_ID", objLodgementMasterTO.getProdId());
                            if(objLodgementMasterTO.getProdType()!=null && !objLodgementMasterTO.getProdType().equals("GL")){
                                List lst = sqlMap.executeQueryForList("getAccountHeadProd" + objLodgementMasterTO.getProdType(), acHeadMap);
                                if (lst != null && lst.size() > 0) {
                                    acHeadMap = (HashMap) lst.get(0);
                                }
                            }
                            double totChargeAmt=insertInwardChargeTrans(instructionList,txMap,transferList,acHeads,transAmt,obj);
                            CashTransactionTO objCashTO = new CashTransactionTO();
                            ArrayList cashList = new ArrayList();
                            double charge = CommonUtil.convertObjToDouble(objLodgementRemitTO.getRemitInstAmt()).doubleValue();
                            double amt = charge-totChargeAmt;
                            if (amt > 0) {
                                objCashTO.setTransId("");
                                objCashTO.setProdType(objLodgementMasterTO.getProdType());
                                objCashTO.setTransType(CommonConstants.DEBIT);
                                objCashTO.setInitTransId(logTO.getUserId());
                                objCashTO.setBranchId(CommonUtil.convertObjToStr(obj.get("INTER_BRANCH_ID")));
                                objCashTO.setInitiatedBranch(_branchCode);
                                objCashTO.setStatusBy(logTO.getUserId());
                                objCashTO.setStatusDt(currDt);
                                objCashTO.setAuthorizeStatus_2("");
                                objCashTO.setParticulars("By " + "Realize cash" + " "+objLodgementMasterTO.getLodgementId());
                                objCashTO.setInitiatedBranch(_branchCode);
                                objCashTO.setInitChannType(CommonConstants.CASHIER);
                                objCashTO.setCommand(CommonConstants.TOSTATUS_INSERT);
                                objCashTO.setAcHdId(acHeadMap.get("AC_HEAD").toString());//achd set                                
                                objCashTO.setInpAmount(CommonUtil.convertObjToDouble(amt));//TOTAL_DEMAND
                                objCashTO.setAmount((CommonUtil.convertObjToDouble(amt)));//TOTAL_DEMAND
                                objCashTO.setLinkBatchId(objLodgementMasterTO.getLodgementId());
                                objCashTO.setActNum(objLodgementMasterTO.getBorrowAcctNum());
                                objCashTO.setProdId(objLodgementMasterTO.getBorrowProdId());
                                objCashTO.setInstType("CHEQUE");
                                objCashTO.setInstrumentNo1(objLodgementChequeTO.getInstrPrefix());
                                objCashTO.setInstrumentNo2(objLodgementChequeTO.getInstrumentNo());
                                objCashTO.setInstDt(objLodgementChequeTO.getInstrumentDt());
                                objCashTO.setTransModType("GL");                                                             
                                HashMap tranMap = new HashMap();
                                tranMap.put(CommonConstants.BRANCH_ID, CommonUtil.convertObjToStr(_branchCode));
                                tranMap.put(CommonConstants.USER_ID, CommonUtil.convertObjToStr(logTO.getUserId()));
                                tranMap.put(CommonConstants.IP_ADDR, CommonUtil.convertObjToStr(""));
                                tranMap.put(CommonConstants.MODULE, "Transaction");//CommonUtil.convertObjToStr(map.get("MODULE")));
                                tranMap.put(CommonConstants.SCREEN, "Cash");//CommonUtil.convertObjToStr(map.get("SCREEN")));
                                tranMap.put("DENOMINATION_LIST", null);
                                tranMap.put("MODE", CommonConstants.TOSTATUS_INSERT);
                                tranMap.put("PRODUCTTYPE", TransactionFactory.SUSPENSE);
                                tranMap.put("SELECTED_BRANCH_ID", CommonUtil.convertObjToStr(_branchCode));
                                tranMap.put("CashTransactionTO", objCashTO);
                                CashTransactionDAO cashDao;
                                cashDao = new CashTransactionDAO();
                                tranMap = cashDao.execute(tranMap, false);
                                cashDao = null;
                                tranMap = null;
                            }
                        }
                    }
                    if (objLodgementMasterTO.getBillsStatus().equals("DISHONOUR")) {                        
                        if (objLodgementMasterTO.getTranType().equals("TRANSFER")) {  
                            System.out.println("LODGEMENT 1 : " + objLodgementMasterTO.getTranType());
                            if (CommonUtil.convertObjToDouble(objLodgementRemitTO.getRemitFavouring()).doubleValue() > 0) {                                
                                txMap = new HashMap();
                                transactionDAO.setCommandMode(CommonConstants.TOSTATUS_INSERT);                                
                                if (!objLodgementMasterTO.getProdType().equals("GL")) {//bbbb1
                                    txMap.put(TransferTrans.DR_AC_HD, null);
                                    txMap.put(TransferTrans.DR_ACT_NUM, objLodgementMasterTO.getBorrowAcctNum());
                                    txMap.put(TransferTrans.DR_PROD_ID, objLodgementMasterTO.getBorrowProdId());
                                    txMap.put("TRANS_MOD_TYPE", objLodgementMasterTO.getProdType());
                                } else {
                                    txMap.put(TransferTrans.DR_AC_HD, objLodgementMasterTO.getBorrowAcctNum());
                                    txMap.put("TRANS_MOD_TYPE", "GL");
                                }
                                txMap.put(TransferTrans.DR_BRANCH, CommonUtil.convertObjToStr(obj.get("INTER_BRANCH_ID")));
                                txMap.put(TransferTrans.DR_INST_TYPE, "VOUCHER");
                                txMap.put(TransferTrans.DR_INSTRUMENT_1, "");
                                txMap.put(TransferTrans.DR_INSTRUMENT_2, "");
                                txMap.put(TransferTrans.DR_INST_DATE, objLodgementChequeTO.getInstrumentDt());
                                txMap.put(TransferTrans.CURRENCY, "INR");
                                txMap.put(TransferTrans.DR_PROD_TYPE, objLodgementMasterTO.getProdType());
                                System.out.println("@#@#@#@#@#@#@ achead : "+acHeadMap1);
                                txMap.put(TransferTrans.CR_AC_HD, (String) acHeadMap1.get("MISC_INC_BNK_CHRG_CR_HD"));//(String)acHeads.get("POSTAGE_AC_HD")); // credit to Remittance Issue account head......
                                txMap.put(TransferTrans.CR_BRANCH, branchID);
                                txMap.put(TransferTrans.CR_PROD_TYPE, TransactionFactory.GL);
                                transAmt = CommonUtil.convertObjToDouble(objLodgementRemitTO.getRemitFavouring()).doubleValue();
                                //                            txMap.put(TransferTrans.PARTICULARS, objLodgementMasterTO.getBorrowProdId());
                                txMap.put(TransferTrans.PARTICULARS, "VPL Chrg LodgeID : " + objLodgementMasterTO.getLodgementId() + " Dishonour");
                                txMap.put(TransferTrans.AUTHORIZE_STATUS_2, "ENTERED_AMOUNT");
                                transactionDAO.setTransType(CommonConstants.CREDIT);
                                //                            txMap.put(TransferTrans.DR_INST_TYE, objLodgementMasterTO.getBillsStatus());   
                                txMap.put("SCREEN_NAME",(String) obj.get(CommonConstants.SCREEN));// Added by nithya on 23-09-2016 for inserting screen name for transactions
                                transferTo = transactionDAO.addTransferDebitLocal(txMap, transAmt);
//                                transAmt=0;
                                transferTo.setInstType("CHEQUE");
                                transferList.add(transferTo);
                                //
                                txMap = new HashMap();
                                if (!objLodgementMasterTO.getProdType().equals("GL")) {//bbbb1
                                    txMap.put(TransferTrans.DR_AC_HD, null);
                                    txMap.put(TransferTrans.DR_ACT_NUM, objLodgementMasterTO.getBorrowAcctNum());
                                    txMap.put(TransferTrans.DR_PROD_ID, objLodgementMasterTO.getBorrowProdId());
                                    txMap.put("TRANS_MOD_TYPE", objLodgementMasterTO.getProdType());
                                } else {
                                    txMap.put(TransferTrans.DR_AC_HD, objLodgementMasterTO.getBorrowAcctNum());
                                    txMap.put("TRANS_MOD_TYPE", "GL");
                                }
                                txMap.put(TransferTrans.DR_BRANCH,CommonUtil.convertObjToStr(obj.get("INTER_BRANCH_ID")));
                                txMap.put(TransferTrans.CURRENCY, "INR");
                                txMap.put(TransferTrans.DR_PROD_TYPE, objLodgementMasterTO.getProdType());
                                System.out.println("@#@#@#@#@#@#@ achead 1: "+acHeadMap1);
                                txMap.put(TransferTrans.CR_AC_HD, (String) acHeadMap1.get("MISC_INC_BNK_CHRG_CR_HD"));//(String)acHeads.get("POSTAGE_AC_HD")); // credit to Remittance Issue account head......
                                txMap.put(TransferTrans.CR_BRANCH, branchID);
                                txMap.put(TransferTrans.CR_PROD_TYPE, TransactionFactory.GL);
                                transAmt = CommonUtil.convertObjToDouble(objLodgementRemitTO.getRemitFavouring()).doubleValue();
                                //                            txMap.put(TransferTrans.PARTICULARS, objLodgementMasterTO.getBorrowProdId());
                                txMap.put(TransferTrans.PARTICULARS, "VPL Chrg LodgeID : " + objLodgementMasterTO.getLodgementId() + " Dishonour");
                                txMap.put(TransferTrans.AUTHORIZE_STATUS_2, "ENTERED_AMOUNT");
                                txMap.put(TransferTrans.PARTICULARS, "VPL Chrg LodgeID : " + objLodgementMasterTO.getLodgementId() + " Dishonour");
                                txMap.put(TransferTrans.AUTHORIZE_STATUS_2, "ENTERED_AMOUNT");
                                transAmt = CommonUtil.convertObjToDouble(objLodgementRemitTO.getRemitFavouring()).doubleValue();
                                transactionDAO.setTransType(CommonConstants.DEBIT);
                                txMap.put("SCREEN_NAME",(String) obj.get(CommonConstants.SCREEN));// Added by nithya on 23-09-2016 for inserting screen name for transactions
                                transferTo = transactionDAO.addTransferCreditLocal(txMap, transAmt);
                                transferTo.setInstType(objLodgementMasterTO.getBillsStatus());
                                transferList.add(transferTo);
                                transAmt = 0;
                                transactionDAO.setTransactionType(transactionDAO.TRANSFER);
                                transactionDAO.doTransferLocal(transferList, branchID);
                                //cash tran
                                transAmt = CommonUtil.convertObjToDouble(objLodgementRemitTO.getRemitFavouring()).doubleValue();
                                txMap = new HashMap();
                                TransactionTO transTO = new TransactionTO();
                                ArrayList cashList = new ArrayList();
                                transactionDAO.setCommandMode(CommonConstants.TOSTATUS_INSERT);
                                txMap.put(TransferTrans.DR_AC_HD, (String) acHeads.get("POSTAGE_AC_HD"));
                                txMap.put(TransferTrans.DR_BRANCH, branchID);
                                txMap.put(TransferTrans.CURRENCY, "INR");
                                txMap.put(TransferTrans.DR_PROD_TYPE, TransactionFactory.GL);
                                txMap.put("TRANS_MOD_TYPE", "GL");
                                txMap.put(TransferTrans.PARTICULARS, "VPL Chrg LodgeID : " + objLodgementMasterTO.getLodgementId() + " Dishonour");
                                txMap.put(TransferTrans.PARTICULARS, "VPL Chrg LodgeID : " + objLodgementMasterTO.getLodgementId() + " Dishonour");
                                txMap.put(TransferTrans.AUTHORIZE_STATUS_2, "ENTERED_AMOUNT");
                                transactionDAO.setTransType(CommonConstants.CREDIT);
                                txMap.put("SCREEN_NAME",(String) obj.get(CommonConstants.SCREEN));// Added by nithya on 23-09-2016 for inserting screen name for transactions
                                transactionDAO.addTransferDebit(txMap, transAmt);
                                transactionDAO.deleteTxList();
                                transTO.setCommand(CommonConstants.TOSTATUS_INSERT);
                                transTO.setTransType("CASH");
                                transTO.setTransAmt(new Double(transAmt));
                                transTO.setBatchId(objLodgementMasterTO.getLodgementId());
                                transTO.setInstType(objLodgementMasterTO.getBillsStatus());
                                cashList.add(transTO);                                
                                transactionDAO.addCashList(cashList);
                                transactionDAO.doTransfer();
                            }
                            double temp = 0;
                            for (int i = 0; i < instructionList.size(); i++) {
                                objLodgementInstTO = (LodgementInstructionsTO) instructionList.get(i);
                                String status = CommonUtil.convertObjToStr(objLodgementInstTO.getStatus());
                                if (!status.equals("DELETED")) {
                                    if (objLodgementInstTO.getInstruction().equals("DISHONOUR_CHARGES") || objLodgementInstTO.getInstruction().equals("BANK_CHARGES")) {
                                        transactionDAO.setCommandMode(CommonConstants.TOSTATUS_INSERT);
                                        temp = temp + ((CommonUtil.convertObjToDouble(objLodgementInstTO.getAmount()).doubleValue())
                                                + (CommonUtil.convertObjToDouble(objLodgementInstTO.getServiceTx()).doubleValue()));
                                        if (!objLodgementMasterTO.getProdType().equals("GL")) {
                                            txMap.put(TransferTrans.DR_AC_HD, null);
                                            txMap.put(TransferTrans.DR_ACT_NUM, objLodgementMasterTO.getBorrowAcctNum());
                                            txMap.put(TransferTrans.DR_PROD_ID, objLodgementMasterTO.getBorrowProdId());
                                            txMap.put("TRANS_MOD_TYPE", objLodgementMasterTO.getProdType());
                                        } else {
                                            txMap.put(TransferTrans.DR_AC_HD, objLodgementMasterTO.getBorrowAcctNum());
                                            txMap.put("TRANS_MOD_TYPE", "GL");
                                        }
                                        txMap.put(TransferTrans.DR_INST_TYPE, "VOUCHER");                                        
                                        txMap.put(TransferTrans.DR_BRANCH, CommonUtil.convertObjToStr(obj.get("INTER_BRANCH_ID")));
                                        txMap.put(TransferTrans.CURRENCY, "INR");
                                        txMap.put(TransferTrans.DR_PROD_TYPE, objLodgementMasterTO.getProdType());
                                        txMap.put(TransferTrans.CR_BRANCH, branchID);
                                        //                                txMap.put(TransferTrans.DR_INST_TYPE, objLodgementMasterTO.getBillsStatus());
                                        txMap.put(TransferTrans.PARTICULARS, "Dishonour Chrg LodgeID : " + objLodgementMasterTO.getLodgementId() + " "+
                                                    objLodgementChequeTO.getInstrPrefix() +" "+objLodgementChequeTO.getInstrumentNo());
                                        txMap.put(TransferTrans.AUTHORIZE_STATUS_2, "ENTERED_AMOUNT");
                                        transAmt = temp;
                                        transactionDAO.setTransType(CommonConstants.CREDIT);
                                        txMap.put("SCREEN_NAME",(String) obj.get(CommonConstants.SCREEN));// Added by nithya on 23-09-2016 for inserting screen name for transactions
                                        transferTo = transactionDAO.addTransferDebitLocal(txMap, transAmt);
                                         transferTo.setStatusBy(CommonUtil.convertObjToStr(obj.get("USER_ID")));
                                        //transferTo.setInstType(objLodgementMasterTO.getBillsStatus());
                                        transferList.add(transferTo);
                                        if ((CommonUtil.convertObjToDouble(objLodgementInstTO.getAmount()).doubleValue()) > 0) {
                                            txMap.put(TransferTrans.CR_AC_HD, (String) acHeads.get("CHRG_AC_HD"));
                                            txMap.put(TransferTrans.CR_PROD_TYPE, TransactionFactory.GL);
                                            //                                    txMap.put(TransferTrans.DR_INST_TYPE, objLodgementMasterTO.getBillsStatus());
                                            System.out.println("@#@#@#@#@#@#@ achead 2: "+acHeads);
                                            txMap.put(TransferTrans.PARTICULARS, "Dishonour Chrg LodgeID : " + objLodgementMasterTO.getLodgementId() + "");
                                            txMap.put(TransferTrans.AUTHORIZE_STATUS_2, "ENTERED_AMOUNT");
                                            txMap.put("TRANS_MOD_TYPE", "GL");
                                            txMap.put(TransferTrans.DR_INST_TYPE, "");                                           
                                            transAmt = CommonUtil.convertObjToDouble(objLodgementInstTO.getAmount()).doubleValue();
                                            transactionDAO.setTransType(CommonConstants.DEBIT);
                                            txMap.put("SCREEN_NAME",(String) obj.get(CommonConstants.SCREEN));// Added by nithya on 23-09-2016 for inserting screen name for transactions
                                            transferTo = transactionDAO.addTransferCreditLocal(txMap, transAmt);
                                            transferTo.setStatusBy(CommonUtil.convertObjToStr(obj.get("USER_ID")));
                                            transferTo.setInstType(objLodgementMasterTO.getBillsStatus());
                                            transferList.add(transferTo);
                                            transAmt = 0;
                                        }
                                        if ((CommonUtil.convertObjToDouble(objLodgementInstTO.getServiceTx()).doubleValue()) > 0) {
                                            txMap.put(TransferTrans.CR_AC_HD, (String) acHeads.get("SERVICE_TAX_HD"));
                                            txMap.put(TransferTrans.CR_PROD_TYPE, TransactionFactory.GL);
                                            txMap.put(TransferTrans.PARTICULARS, "ServiceTax LodgeID : " + objLodgementMasterTO.getLodgementId() + " Dishonour");
                                            txMap.put(TransferTrans.AUTHORIZE_STATUS_2, "ENTERED_AMOUNT");
                                            txMap.put("TRANS_MOD_TYPE", "GL");
                                            transAmt = CommonUtil.convertObjToDouble(objLodgementInstTO.getServiceTx()).doubleValue();
                                            transactionDAO.setTransType(CommonConstants.DEBIT);
                                            txMap.put("SCREEN_NAME",(String) obj.get(CommonConstants.SCREEN));// Added by nithya on 23-09-2016 for inserting screen name for transactions
                                            transferTo = transactionDAO.addTransferCreditLocal(txMap, transAmt);
                                            transferTo.setInstType(objLodgementMasterTO.getBillsStatus());
                                            transferTo.setStatusBy(CommonUtil.convertObjToStr(obj.get("USER_ID")));
                                            transferList.add(transferTo);
                                            transAmt = 0;
                                        }
                                        transactionDAO.setTransactionType(transactionDAO.TRANSFER);
                                        transactionDAO.doTransferLocal(transferList, branchID);
                                        //INSERT INSTRUCTION
                                        HashMap instMap = new HashMap();
                                        instMap.put("LODGEMENT_ID", objLodgementMasterTO.getLodgementId());
                                        instMap.put("INSTRUCTION", objLodgementInstTO.getInstruction());
                                        List lstInst = sqlMap.executeQueryForList("getBillsExtInst", instMap);
                                        instMap = null;
                                        if (lstInst != null && lstInst.size() > 0) {
                                            sqlMap.executeUpdate("updateLodgementInstructionsTO", objLodgementInstTO);
                                        } else {
                                            sqlMap.executeUpdate("insertLodgementInstructionsTO", objLodgementInstTO);
                                        }
                                        lstInst = null;
                                    }
                                    status = null;
                                }
                            }
                        }
                        if (objLodgementMasterTO.getTranType().equals("CASH")) {
                            System.out.println("In DISHONOUR CASH");
                            if (CommonUtil.convertObjToDouble(objLodgementRemitTO.getRemitFavouring()).doubleValue() > 0) {
                                System.out.println("cash tran credit");
                                //cash tran credit
                                transAmt = CommonUtil.convertObjToDouble(objLodgementRemitTO.getRemitFavouring()).doubleValue();
                                txMap = new HashMap();
                                TransactionTO transTO = new TransactionTO();
                                ArrayList cashList = new ArrayList();
                                transactionDAO.setCommandMode(CommonConstants.TOSTATUS_INSERT);
                                txMap.put(TransferTrans.CR_AC_HD, (String) acHeads.get("POSTAGE_AC_HD"));
                                txMap.put(TransferTrans.CR_BRANCH, branchID);
                                txMap.put(TransferTrans.CURRENCY, "INR");
                                txMap.put(TransferTrans.CR_PROD_TYPE, TransactionFactory.GL);
                                //                                txMap.put(TransferTrans.DR_INST_TYPE, objLodgementMasterTO.getBillsStatus());
                                txMap.put(TransferTrans.PARTICULARS, "VPL Chrg LodgeID : " + objLodgementMasterTO.getLodgementId() + " Dishonour");
                                txMap.put(TransferTrans.AUTHORIZE_STATUS_2, "ENTERED_AMOUNT");
                                txMap.put("TRANS_MOD_TYPE", "GL");
                                transactionDAO.setTransType(CommonConstants.DEBIT);
                                txMap.put("SCREEN_NAME",(String) obj.get(CommonConstants.SCREEN));// Added by nithya on 23-09-2016 for inserting screen name for transactions
                                transactionDAO.addTransferCredit(txMap, transAmt);
                                transactionDAO.deleteTxList();
                                transTO.setCommand(CommonConstants.TOSTATUS_INSERT);
                                transTO.setTransType("CASH");
                                transTO.setTransAmt(new Double(transAmt));
                                transTO.setBatchId(objLodgementMasterTO.getLodgementId());
                                transTO.setInstType(objLodgementMasterTO.getBillsStatus());
                                cashList.add(transTO);
                                System.out.println("@@@@@@f" + cashList);
                                transactionDAO.addCashList(cashList);
                                transactionDAO.doTransfer();
                                //cash tran debit
                                transAmt = CommonUtil.convertObjToDouble(objLodgementRemitTO.getRemitFavouring()).doubleValue();
                                txMap = new HashMap();
                                transTO = new TransactionTO();
                                cashList = new ArrayList();
                                transactionDAO.setCommandMode(CommonConstants.TOSTATUS_INSERT);
                                txMap.put(TransferTrans.DR_AC_HD, (String) acHeads.get("POSTAGE_AC_HD"));
                                txMap.put(TransferTrans.DR_BRANCH, branchID);
                                txMap.put(TransferTrans.CURRENCY, "INR");
                                txMap.put(TransferTrans.DR_PROD_TYPE, TransactionFactory.GL);
                                //                                txMap.put(TransferTrans.DR_INST_TYPE, objLodgementMasterTO.getBillsStatus());
                                txMap.put(TransferTrans.PARTICULARS, "VPL Chrg LodgeID : " + objLodgementMasterTO.getLodgementId() + " Dishonour");
                                txMap.put(TransferTrans.AUTHORIZE_STATUS_2, "ENTERED_AMOUNT");
                                transactionDAO.setTransType(CommonConstants.CREDIT);
                                txMap.put("SCREEN_NAME",(String) obj.get(CommonConstants.SCREEN));// Added by nithya on 23-09-2016 for inserting screen name for transactions
                                transactionDAO.addTransferDebit(txMap, transAmt);
                                transactionDAO.deleteTxList();
                                transTO.setCommand(CommonConstants.TOSTATUS_INSERT);
                                transTO.setTransType("CASH");
                                transTO.setTransAmt(new Double(transAmt));
                                transTO.setBatchId(objLodgementMasterTO.getLodgementId());
                                transTO.setInstType(objLodgementMasterTO.getBillsStatus());
                                cashList.add(transTO);
                                System.out.println("@@@@@@f" + cashList);
                                transactionDAO.addCashList(cashList);
                                transactionDAO.doTransfer();
                            }
                            //TRANSFER TRAN
                            double temp = 0;
                            for (int i = 0; i < instructionList.size(); i++) {                                
                                objLodgementInstTO = (LodgementInstructionsTO) instructionList.get(i);                                
                                String status = CommonUtil.convertObjToStr(objLodgementInstTO.getStatus());
                                if (!status.equals("DELETED")) {
                                  //  if (objLodgementInstTO.getInstruction().equals("DISHONOUR_CHARGES")) {
                                        transAmt = CommonUtil.convertObjToDouble(objLodgementInstTO.getAmount()).doubleValue();
                                        if (transAmt > 0) {
                                            txMap = new HashMap();
                                            TransactionTO transTO = new TransactionTO();
                                            ArrayList cashList = new ArrayList();
                                            transactionDAO.setCommandMode(CommonConstants.TOSTATUS_INSERT);
                                            txMap.put(TransferTrans.CR_AC_HD, (String) acHeads.get("CHRG_AC_HD"));
                                            txMap.put(TransferTrans.CR_BRANCH, branchID);
                                            txMap.put(TransferTrans.CURRENCY, "INR");
                                            txMap.put(TransferTrans.CR_PROD_TYPE, TransactionFactory.GL);
                                            //                                        txMap.put(TransferTrans.DR_INST_TYPE, objLodgementMasterTO.getBillsStatus());
                                            txMap.put(TransferTrans.PARTICULARS, "VPL Chrg LodgeID : " + objLodgementMasterTO.getLodgementId() + " Dishonour");
                                            txMap.put(TransferTrans.AUTHORIZE_STATUS_2, "ENTERED_AMOUNT");
                                            txMap.put("TRANS_MOD_TYPE", "GL");
                                            txMap.put("SCREEN_NAME",(String) obj.get(CommonConstants.SCREEN));// Added by nithya on 23-09-2016 for inserting screen name for transactions
                                            transactionDAO.setTransType(CommonConstants.DEBIT);
                                            transactionDAO.addTransferCredit(txMap, transAmt);
                                            transactionDAO.deleteTxList();
                                            transTO.setCommand(CommonConstants.TOSTATUS_INSERT);
                                            transTO.setTransType("CASH");
                                            transTO.setTransAmt(new Double(transAmt));
                                            transTO.setBatchId(objLodgementMasterTO.getLodgementId());
                                            transTO.setInstType(objLodgementMasterTO.getBillsStatus());
                                            cashList.add(transTO);
                                            System.out.println("@@@@@@f" + cashList);
                                            transactionDAO.addCashList(cashList);
                                            transactionDAO.doTransfer();
                                            transAmt = 0;
                                        }
                                        transAmt = CommonUtil.convertObjToDouble(objLodgementInstTO.getServiceTx()).doubleValue();
                                        if (transAmt > 0) {
                                            txMap = new HashMap();
                                            TransactionTO transTO = new TransactionTO();
                                            ArrayList cashList = new ArrayList();
                                            transactionDAO.setCommandMode(CommonConstants.TOSTATUS_INSERT);
                                            txMap.put(TransferTrans.CR_AC_HD, (String) acHeads.get("COMM_AC_HD"));
                                            txMap.put(TransferTrans.CR_BRANCH, branchID);
                                            txMap.put(TransferTrans.CURRENCY, "INR");
                                            txMap.put(TransferTrans.CR_PROD_TYPE, TransactionFactory.GL);
                                            //                                        txMap.put(TransferTrans.DR_INST_TYPE, objLodgementMasterTO.getBillsStatus());
                                            txMap.put(TransferTrans.PARTICULARS, "VPL Chrg LodgeID : " + objLodgementMasterTO.getLodgementId() + " Dishonour");
                                            txMap.put(TransferTrans.AUTHORIZE_STATUS_2, "ENTERED_AMOUNT");
                                            txMap.put("TRANS_MOD_TYPE", "GL");
                                            txMap.put("SCREEN_NAME",(String) obj.get(CommonConstants.SCREEN));// Added by nithya on 23-09-2016 for inserting screen name for transactions
                                            transactionDAO.setTransType(CommonConstants.DEBIT);
                                            transactionDAO.addTransferCredit(txMap, transAmt);
                                            transactionDAO.deleteTxList();
                                            transTO.setCommand(CommonConstants.TOSTATUS_INSERT);
                                            transTO.setTransType("CASH");
                                            transTO.setTransAmt(new Double(transAmt));
                                            transTO.setBatchId(objLodgementMasterTO.getLodgementId());
                                            transTO.setInstType(objLodgementMasterTO.getBillsStatus());
                                            cashList.add(transTO);                                            
                                            transactionDAO.addCashList(cashList);
                                            transactionDAO.doTransfer();
                                            transAmt = 0;
                                        }
                                        //INSERT INSTRUCTION
                                        HashMap instMap = new HashMap();
                                        instMap.put("LODGEMENT_ID", objLodgementMasterTO.getLodgementId());
                                        instMap.put("INSTRUCTION", objLodgementInstTO.getInstruction());
                                        List lstInst = sqlMap.executeQueryForList("getBillsExtInst", instMap);
                                        instMap = null;
                                        if (lstInst != null && lstInst.size() > 0) {
                                            sqlMap.executeUpdate("updateLodgementInstructionsTO", objLodgementInstTO);
                                        } else {
                                            sqlMap.executeUpdate("insertLodgementInstructionsTO", objLodgementInstTO);
                                        }
                                        lstInst = null;
                                   // }
                                    status = null;
                                }
                            }
                        }
                    }
                } else if (hashData.get("OPERATES_LIKE").equals("OUTWARD")) {
                    String otherBankBranch = ""; // Added by nithya on 10-10-2019 for KD-653
                    if (objLodgementMasterTO.getBillsStatus().equals("REALIZE")) {
                        if (objLodgementMasterTO.getTranType().equals("TRANSFER")) {                            
                            if ((objLodgementRemitTO.getRemitProdId().equals("PO")) || (objLodgementRemitTO.getRemitProdId().equals("DD"))
                                    || (objLodgementRemitTO.getRemitProdId().equals("IBR")) || (objLodgementRemitTO.getRemitProdId().equals("OBADV"))) {
                                if (CommonUtil.convertObjToDouble(objLodgementRemitTO.getRemitInstAmt()).doubleValue() > 0) {
                                    if (obj.containsKey("LODGE_LOAN_DET")) {                                        
                                        double temp = 0;
                                        if (coll_Obc_From_Cust.equals("Y")) {
                                                transAmt = CommonUtil.convertObjToDouble(objLodgementChequeTO.getInstrumentAmount()).doubleValue();
                                            } else {
                                                transAmt = CommonUtil.convertObjToDouble(objLodgementRemitTO.getRemitInstAmt()).doubleValue();
                                            }
                                        if ((objLodgementRemitTO.getRemitProdId().equals("PO")) || (objLodgementRemitTO.getRemitProdId().equals("DD"))) {
                                            txMap.put(TransferTrans.DR_AC_HD, (String) acHeads.get("BILLS_REALISED_HD"));
                                        } else if (objLodgementRemitTO.getRemitProdId().equals("IBR")) {
                                            System.out.println("bbb acHeads.get(\"IBR_AC_HD\")" + acHeads.get("IBR_AC_HD"));
                                            txMap.put(TransferTrans.DR_AC_HD, (String) acHeads.get("IBR_AC_HD"));
                                        } else if (objLodgementRemitTO.getRemitProdId().equals("OBADV")) {
                                            if (obj.containsKey("OTHER_BANK_HEAD")) {                                                
                                                txMap.put(TransferTrans.DR_AC_HD, obj.get("OTHER_BANK_HEAD"));                                                
                                                if (obj.get("OTHER_BANK_PROD_TYPE") != null && obj.get("OTHER_BANK_PROD_TYPE").equals("INV")) {
                                                    InvestmentsTransTO objTO = new InvestmentsTransTO();
                                                    obj.put("INVESTMENT_ACC_NO", CommonUtil.convertObjToStr(obj.get("OTHER_BANK_HEAD")));
                                                    objTO = getInvestmentsTransTO(CommonConstants.TOSTATUS_INSERT, transAmt, obj, obj);
                                                    HashMap achdMap = new HashMap();
                                                    achdMap.put("INVESTMENT_PROD_ID", CommonUtil.convertObjToStr(objTO.getInvestmentID()));
                                                    List achdLst = ServerUtil.executeQuery("getSelectinvestmentAccountHead", achdMap);
                                                    if (achdLst != null && achdLst.size() > 0) {
                                                        achdMap = new HashMap();
                                                        achdMap = (HashMap) achdLst.get(0);
                                                        txMap.put(TransferTrans.DR_AC_HD, achdMap.get("IINVESTMENT_AC_HD"));
                                                        acHeads.put("IINVESTMENT_AC_HD", achdMap.get("IINVESTMENT_AC_HD"));
                                                        txMap.put("AUTHORIZEREMARKS", "IINVESTMENT_DEPOSIT");                                                        
                                                        txMap.put(TransferTrans.AUTHORIZE_STATUS_2, "ENTERED_AMOUNT");
                                                        txMap.put(TransferTrans.DR_ACT_NUM, objLodgementMasterTO.getBorrowAcctNum());
                                                        objTO.setBatchID(CommonUtil.convertObjToStr(objLodgementMasterTO.getLodgementId()));
                                                        sqlMap.executeUpdate("insertInvestmentTransTO", objTO);
                                                    } else {
                                                        throw new TTException("Account heads not set properly...");
                                                    }
                                                }                                                
                                                if (obj.get("OTHER_BANK_PROD_TYPE") != null && obj.get("OTHER_BANK_PROD_TYPE").equals("AB")) {
                                                    InvestmentsTransTO objTO = new InvestmentsTransTO();
                                                    obj.put("INVESTMENT_ACC_NO", CommonUtil.convertObjToStr(obj.get("OTHER_BANK_HEAD")));
                                                    HashMap achdMap = new HashMap();
                                                    List achdLst = ServerUtil.executeQuery("getSelectOthrBankAcHd", obj);
                                                    if (achdLst != null && achdLst.size() > 0) {
                                                        achdMap = new HashMap();
                                                        achdMap = (HashMap) achdLst.get(0);
                                                        otherBankBranch = CommonUtil.convertObjToStr(achdMap.get("BRANCH_ID")); // Added by nithya on 10-10-2019 for KD-653                                                        
                                                        txMap.put(TransferTrans.DR_AC_HD, achdMap.get("PRINCIPAL_AC_HD"));
                                                        acHeads.put("IINVESTMENT_AC_HD", achdMap.get("PRINCIPAL_AC_HD"));
                                                        txMap.put("AUTHORIZEREMARKS", "PRINCIPAL_AC_HD");                                                        
                                                        txMap.put(TransferTrans.DR_ACT_NUM, CommonUtil.convertObjToStr(obj.get("OTHER_BANK_HEAD")));
                                                        txMap.put(TransferTrans.AUTHORIZE_STATUS_2, "ENTERED_AMOUNT");                                                                                                               
                                                    } else {
                                                        throw new TTException("Account heads not set properly...");
                                                    }
                                                }
                                            } else {
                                                txMap.put(TransferTrans.DR_AC_HD, (String) acHeads.get("BILLS_REALISED_HD"));
                                            }
                                        }
                                        txMap.put(TransferTrans.DR_BRANCH, branchID);
                                        txMap.put(TransferTrans.CURRENCY, "INR");
                                        if (obj.get("OTHER_BANK_PROD_TYPE") != null && obj.get("OTHER_BANK_PROD_TYPE").equals("AB")) {
                                            txMap.put(TransferTrans.DR_PROD_TYPE, TransactionFactory.OTHERBANKACTS);
                                            txMap.put("TRANS_MOD_TYPE", "AB");
                                            txMap.put(TransferTrans.DR_BRANCH, otherBankBranch); // Added by nithya on 10-10-2019 for KD-653
                                            if (!(otherBankBranch.equalsIgnoreCase(branchID))) {
                                                txMap.put("BILLS_INTER_BRANCH_TRANS", "BILLS_INTER_BRANCH_TRANS");
                                                txMap.put("INITIATED_BRANCH", _branchCode);
                                            }
                                        } else {
                                            txMap.put(TransferTrans.DR_PROD_TYPE, TransactionFactory.GL);
                                            txMap.put("TRANS_MOD_TYPE","GL");
                                        }
                                        txMap.put(TransferTrans.PARTICULARS, "LodgeID : " + objLodgementMasterTO.getLodgementId() + " Realize");
                                        txMap.put(TransferTrans.AUTHORIZE_STATUS_2, "ENTERED_AMOUNT");
                                        txMap.put("SCREEN_NAME",(String) obj.get(CommonConstants.SCREEN));// Added by nithya on 23-09-2016 for inserting screen name for transactions
                                        transferTo = transactionDAO.addTransferDebitLocal(txMap, transAmt);
                                        transferTo.setInstType(objLodgementMasterTO.getBillsStatus());
                                        transferTo.setStatusBy(CommonUtil.convertObjToStr(obj.get("USER_ID")));
                                        transferTo.setInitTransId(CommonUtil.convertObjToStr(obj.get("USER_ID")));
                                        transferTo.setInitiatedBranch(branchID);
                                        transferList.add(transferTo);
                                        //System.out.println("transferList ::  realize ::" + transferList);
                                        if (coll_Obc_From_Cust.equals("Y")) {
                                            temp = CommonUtil.convertObjToDouble(objLodgementChequeTO.getInstrumentAmount()).doubleValue();
                                        } else {
                                            temp = CommonUtil.convertObjToDouble(objLodgementRemitTO.getRemitInstAmt()).doubleValue();
                                        }
                                        double commTot = 0;
                                        for (int i = 0; i < instructionList.size(); i++) {
                                            objLodgementInstTO = (LodgementInstructionsTO) instructionList.get(i);
                                            String status = CommonUtil.convertObjToStr(objLodgementInstTO.getStatus());
                                            if (!status.equals("DELETED")) {
                                                transAmt = CommonUtil.convertObjToDouble(objLodgementInstTO.getAmount()).doubleValue();
                                                commTot += transAmt;
                                                if (CommonUtil.convertObjToDouble(objLodgementInstTO.getServiceTx()).doubleValue() > 0) {
                                                    transAmt = CommonUtil.convertObjToDouble(objLodgementInstTO.getServiceTx()).doubleValue();
                                                    commTot += transAmt;
                                                    txMap.put(TransferTrans.CR_AC_HD, (String) acHeads.get("SERVICE_TAX_HD"));  // credit to Postage Charge account head......
                                                    txMap.put(TransferTrans.PARTICULARS, "ServiceTax LodgeID : " + objLodgementMasterTO.getLodgementId() + " Realize");
                                                    txMap.put("TRANS_MOD_TYPE", "GL");
                                                    txMap.put("SCREEN_NAME",(String) obj.get(CommonConstants.SCREEN));// Added by nithya on 23-09-2016 for inserting screen name for transactions
                                                    transferTo = transactionDAO.addTransferCreditLocal(txMap, transAmt);
                                                    transferTo.setInstType(objLodgementMasterTO.getBillsStatus());
                                                    transferTo.setInitiatedBranch(branchID);
                                                    transferList.add(transferTo);
                                                }
                                                temp = temp - (CommonUtil.convertObjToDouble(objLodgementInstTO.getAmount()).doubleValue())
                                                        - (CommonUtil.convertObjToDouble(objLodgementInstTO.getServiceTx()).doubleValue());
                                                HashMap instMap = new HashMap();
                                                instMap.put("LODGEMENT_ID", objLodgementMasterTO.getLodgementId());
                                                instMap.put("INSTRUCTION", objLodgementInstTO.getInstruction());
                                                List lstInst = sqlMap.executeQueryForList("getBillsExtInst", instMap);
                                                instMap = null;
                                                if (lstInst != null && lstInst.size() > 0) {
                                                    sqlMap.executeUpdate("updateLodgementInstructionsTO", objLodgementInstTO);
                                                } else {
                                                    sqlMap.executeUpdate("insertLodgementInstructionsTO", objLodgementInstTO);
                                                }
                                                lstInst = null;
                                                status = null;
                                            }
                                        }
                                        //rish
                                        if (coll_Obc_From_Cust.equals("Y")) {
                                            double otherBankCharge = 0.0;
                                            double totalCommisionCharge = 0.0;
                                            otherBankCharge = CommonUtil.convertObjToDouble(objLodgementRemitTO.getRemitFavouring()).doubleValue();  //Only Charge Amount
                                            totalCommisionCharge = otherBankCharge + commTot;  // commTot -> Toatal Commision Amount
                                            if (totalCommisionCharge > 0) {
                                                txMap.put(TransferTrans.CR_AC_HD, (String) acHeads.get("MISC_INC_BNK_CHRG_CR_HD"));  // credit to Postage Charge account head......
                                                txMap.put(TransferTrans.PARTICULARS, "BankCharges LodgeID : " + objLodgementMasterTO.getLodgementId() + " Realize");
                                                System.out.println("transAmt 2 cr" + totalCommisionCharge);
                                                if (obj.get("OTHER_BANK_PROD_TYPE") != null && obj.get("OTHER_BANK_PROD_TYPE").equals("AB")) {
                                                    txMap.put(TransferTrans.CR_ACT_NUM, null);
                                                }
                                                txMap.put(TransferTrans.AUTHORIZE_STATUS_2, "ENTERED_AMOUNT");
                                                txMap.put("TRANS_MOD_TYPE", "GL");
                                                txMap.put("SCREEN_NAME",(String) obj.get(CommonConstants.SCREEN));// Added by nithya on 23-09-2016 for inserting screen name for transactions
                                                transferTo = transactionDAO.addTransferCreditLocal(txMap, totalCommisionCharge);                                                
                                                transferTo.setInstType(objLodgementMasterTO.getBillsStatus());
                                                transferTo.setStatusBy(CommonUtil.convertObjToStr(obj.get("USER_ID")));
                                                transferTo.setInitTransId(CommonUtil.convertObjToStr(obj.get("USER_ID")));
                                                transferTo.setInitiatedBranch(branchID);
                                                transferList.add(transferTo);
                                            }
                                        }
                                        if (coll_Obc_From_Cust.equals("Y")) {
                                            temp = CommonUtil.convertObjToDouble(objLodgementChequeTO.getInstrumentAmount()).doubleValue();
                                        }
                                        if (temp > 0) {
                                            transAmt = temp;
                                            txMap.put(TransferTrans.CR_AC_HD, null);
                                            txMap.put(TransferTrans.CR_PROD_TYPE, objLodgementMasterTO.getProdType());
                                            txMap.put(TransferTrans.CR_PROD_ID, objLodgementMasterTO.getBorrowProdId());
                                            txMap.put(TransferTrans.CR_ACT_NUM, objLodgementMasterTO.getBorrowAcctNum());
                                            txMap.put(TransferTrans.CR_BRANCH, CommonUtil.convertObjToStr(obj.get("SELECTED_BRANCH_ID")));
                                            if (!objLodgementMasterTO.getProdType().equals("") && objLodgementMasterTO.getProdType().equals("GL") && objLodgementMasterTO.getBorrowProdId().equals("")) {
                                                txMap.put(TransferTrans.CR_AC_HD, (String) objLodgementMasterTO.getBorrowAcctNum());
                                                txMap.put(TransferTrans.CR_PROD_TYPE, TransactionFactory.GL);
                                                txMap.put("TRANS_MOD_TYPE", "GL");
                                            }
                                            if(objLodgementMasterTO.getProdType().equals("AB")){
                                                txMap.put(TransferTrans.CR_BRANCH, otherBankBranch);// Added by nithya on 10-10-2019 for KD-653
                                                if (!(otherBankBranch.equalsIgnoreCase(branchID))) {
                                                    txMap.put("BILLS_INTER_BRANCH_TRANS", "BILLS_INTER_BRANCH_TRANS");
                                                    txMap.put("INITIATED_BRANCH", _branchCode);
                                                }
                                            }
                                            txMap.put(TransferTrans.PARTICULARS, objLodgementMasterTO.getLodgementId() + " Realize / Comm-"
                                                    + commTot + " / OBC-" + objLodgementRemitTO.getRemitFavouring());
                                            txMap.put(TransferTrans.AUTHORIZE_STATUS_2, "ENTERED_AMOUNT");
                                            txMap.put("SCREEN_NAME",(String) obj.get(CommonConstants.SCREEN));// Added by nithya on 23-09-2016 for inserting screen name for transactions
                                            transferTo = transactionDAO.addTransferCreditLocal(txMap, transAmt);
                                            transferTo.setInstType(objLodgementMasterTO.getBillsStatus());
                                            transferTo.setStatusBy(CommonUtil.convertObjToStr(obj.get("USER_ID")));
                                            transferTo.setInitTransId(CommonUtil.convertObjToStr(obj.get("USER_ID")));
                                            transferTo.setInitiatedBranch(branchID);
                                            transferTo.setBranchId(CommonUtil.convertObjToStr(obj.get("SELECTED_BRANCH_ID")));
                                            transferList.add(transferTo);
                                            temp = 0;
                                        }                                        
                                        if (coll_Obc_From_Cust.equals("Y")) {
                                            double otherBankCharge = 0.0;
                                            double totalCommisionCharge = 0.0;
                                            otherBankCharge = CommonUtil.convertObjToDouble(objLodgementRemitTO.getRemitFavouring()).doubleValue();  //Only Charge Amount
                                            totalCommisionCharge = otherBankCharge + commTot;  // commTot -> Toatal Commision Amount
                                            if (totalCommisionCharge > 0) {
                                                if (!objLodgementMasterTO.getProdType().equals("GL")) {
                                                    txMap.put(TransferTrans.DR_AC_HD, (String) acHeadMap.get("AC_HEAD"));
                                                    txMap.put("TRANS_MOD_TYPE", "GL");
                                                } else {
                                                    txMap.put(TransferTrans.DR_AC_HD, objLodgementMasterTO.getBorrowAcctNum());
                                                    txMap.put("TRANS_MOD_TYPE", objLodgementMasterTO.getProdType());
                                                }
                                                txMap.put(TransferTrans.DR_ACT_NUM, objLodgementMasterTO.getBorrowAcctNum());
                                                txMap.put(TransferTrans.DR_BRANCH, branchID);
                                                txMap.put(TransferTrans.CURRENCY, "INR");
                                                txMap.put(TransferTrans.DR_PROD_TYPE, objLodgementMasterTO.getProdType());
                                                txMap.put(TransferTrans.DR_PROD_ID, objLodgementMasterTO.getBorrowProdId());
                                                txMap.put(TransferTrans.PARTICULARS, "LodgeID : " + objLodgementMasterTO.getLodgementId() + " Realize");
                                                txMap.put(TransferTrans.AUTHORIZE_STATUS_2, "ENTERED_AMOUNT");
                                                System.out.println("totalCommisionCharge====" + totalCommisionCharge);
                                                txMap.put("TRANS_MOD_TYPE", objLodgementMasterTO.getProdType());
                                                txMap.put("SCREEN_NAME",(String) obj.get(CommonConstants.SCREEN));// Added by nithya on 23-09-2016 for inserting screen name for transactions
                                                transferTo = transactionDAO.addTransferDebitLocal(txMap, totalCommisionCharge);                                                
                                                transferTo.setInstType(objLodgementMasterTO.getBillsStatus());
                                                transferTo.setStatusBy(CommonUtil.convertObjToStr(obj.get("USER_ID")));
                                                transferTo.setInitTransId(CommonUtil.convertObjToStr(obj.get("USER_ID")));
                                                transferTo.setInitiatedBranch(branchID);
                                                transferList.add(transferTo);                                                
                                            }
                                        }
                                        transAmt = 0;
                                        transactionDAO.setTransactionType(transactionDAO.TRANSFER);                                        
                                        transferDAO = new TransferDAO();
                                        HashMap tansactionMap = new HashMap();
                                        HashMap map1 = new HashMap();
                                        map1 = (HashMap) obj.get("LODGE_LOAN_DET");                                        
                                        tansactionMap.put("ALL_AMOUNT", (HashMap) map1.get("ALL_AMOUNT"));
                                        tansactionMap.put("TxTransferTO", transferList);
                                        tansactionMap.put("COMMAND", "INSERT");
                                        tansactionMap.put(CommonConstants.BRANCH_ID, branchID);
                                        transferDAO.execute(tansactionMap, false);//end babu
                                    } else {                                        
                                        double temp = 0;
                                        //babu comm 09-06-2014 for obc cust Y OR N set amount as instrument amount.
                                        //if (coll_Obc_From_Cust.equals("Y")) {
                                              transAmt = CommonUtil.convertObjToDouble(objLodgementChequeTO.getInstrumentAmount()).doubleValue();
                                       // } else {
                                       //       transAmt = CommonUtil.convertObjToDouble(objLodgementRemitTO.getRemitInstAmt()).doubleValue();
                                       /// }                                      
                                        if ((objLodgementRemitTO.getRemitProdId().equals("PO")) || (objLodgementRemitTO.getRemitProdId().equals("DD"))) {
                                            txMap.put(TransferTrans.DR_AC_HD, (String) acHeads.get("BILLS_REALISED_HD"));
                                        } else if (objLodgementRemitTO.getRemitProdId().equals("IBR")) {
                                            System.out.println("acHeads.get(\"IBR_AC_HD\")" + acHeads.get("IBR_AC_HD"));
                                            txMap.put(TransferTrans.DR_AC_HD, (String) acHeads.get("IBR_AC_HD"));
                                        } else if (objLodgementRemitTO.getRemitProdId().equals("OBADV")) {
                                            if (obj.containsKey("OTHER_BANK_HEAD")) {                                                
                                                txMap.put(TransferTrans.DR_AC_HD, obj.get("OTHER_BANK_HEAD"));
                                                //Added By Suresh                                                  
                                                if (obj.get("OTHER_BANK_PROD_TYPE") != null && obj.get("OTHER_BANK_PROD_TYPE").equals("INV")) {
                                                    InvestmentsTransTO objTO = new InvestmentsTransTO();
                                                    obj.put("INVESTMENT_ACC_NO", CommonUtil.convertObjToStr(obj.get("OTHER_BANK_HEAD")));
                                                    objTO = getInvestmentsTransTO(CommonConstants.TOSTATUS_INSERT, transAmt, obj, obj);
                                                    HashMap achdMap = new HashMap();
                                                    achdMap.put("INVESTMENT_PROD_ID", CommonUtil.convertObjToStr(objTO.getInvestmentID()));
                                                    List achdLst = ServerUtil.executeQuery("getSelectinvestmentAccountHead", achdMap);
                                                    if (achdLst != null && achdLst.size() > 0) {
                                                        achdMap = new HashMap();
                                                        achdMap = (HashMap) achdLst.get(0);
                                                        txMap.put(TransferTrans.DR_AC_HD, achdMap.get("IINVESTMENT_AC_HD"));
                                                        acHeads.put("IINVESTMENT_AC_HD", achdMap.get("IINVESTMENT_AC_HD"));
                                                        txMap.put("AUTHORIZEREMARKS", "IINVESTMENT_DEPOSIT");
                                                        //Set Trans ID                                                        
                                                        txMap.put(TransferTrans.AUTHORIZE_STATUS_2, "ENTERED_AMOUNT");
                                                        txMap.put(TransferTrans.DR_ACT_NUM, objLodgementMasterTO.getBorrowAcctNum());
                                                        objTO.setBatchID(CommonUtil.convertObjToStr(objLodgementMasterTO.getLodgementId()));
                                                        sqlMap.executeUpdate("insertInvestmentTransTO", objTO);
                                                    } else {
                                                        throw new TTException("Account heads not set properly...");
                                                    }
                                                }
                                                System.out.println("obj.get(\"OTHER_BANK_PROD_TYPE\")" + obj.get("OTHER_BANK_PROD_TYPE"));
                                                if (obj.get("OTHER_BANK_PROD_TYPE") != null && obj.get("OTHER_BANK_PROD_TYPE").equals("AB")) {
                                                    InvestmentsTransTO objTO = new InvestmentsTransTO();
                                                    obj.put("INVESTMENT_ACC_NO", CommonUtil.convertObjToStr(obj.get("OTHER_BANK_HEAD")));
                                                    //objTO = getInvestmentsTransTO(CommonConstants.TOSTATUS_INSERT, transAmt, obj, obj);
                                                    HashMap achdMap = new HashMap();
                                                    List achdLst = ServerUtil.executeQuery("getSelectOthrBankAcHd", obj);
                                                    if (achdLst != null && achdLst.size() > 0) {
                                                        achdMap = new HashMap();
                                                        achdMap = (HashMap) achdLst.get(0);
                                                        otherBankBranch = CommonUtil.convertObjToStr(achdMap.get("BRANCH_ID"));// Added by nithya on 10-10-2019 for KD-653
                                                        txMap.put(TransferTrans.DR_AC_HD, achdMap.get("PRINCIPAL_AC_HD"));
                                                        acHeads.put("IINVESTMENT_AC_HD", achdMap.get("PRINCIPAL_AC_HD"));
                                                        txMap.put("AUTHORIZEREMARKS", "PRINCIPAL_AC_HD");
                                                        txMap.put(TransferTrans.DR_ACT_NUM, CommonUtil.convertObjToStr(obj.get("OTHER_BANK_HEAD")));
                                                        txMap.put(TransferTrans.AUTHORIZE_STATUS_2, "ENTERED_AMOUNT");
                                                    } else {
                                                        throw new TTException("Account heads not set properly...");
                                                    }
                                                }
                                            } else {
                                                txMap.put(TransferTrans.DR_AC_HD, (String) acHeads.get("BILLS_REALISED_HD"));
                                            }
                                        }
                                        txMap.put(TransferTrans.DR_BRANCH, branchID);
                                        txMap.put(TransferTrans.CURRENCY, "INR");                                        
                                        //bbb1
                                        if (obj.get("OTHER_BANK_PROD_TYPE") != null && obj.get("OTHER_BANK_PROD_TYPE").equals("AB")) {
                                            txMap.put(TransferTrans.DR_PROD_TYPE, TransactionFactory.OTHERBANKACTS);
                                            txMap.put("TRANS_MOD_TYPE", "AB");
                                            txMap.put(TransferTrans.DR_PROD_ID, CommonUtil.convertObjToStr(objLodgementMasterTO.getRecOtherBank()));
                                            txMap.put(TransferTrans.DR_BRANCH, otherBankBranch);// Added by nithya on 10-10-2019 for KD-653
                                            if (!(otherBankBranch.equalsIgnoreCase(branchID))) {
                                                txMap.put("BILLS_INTER_BRANCH_TRANS", "BILLS_INTER_BRANCH_TRANS");
                                                txMap.put("INITIATED_BRANCH", _branchCode);
                                            }
                                        } else {
                                            txMap.put(TransferTrans.DR_PROD_TYPE, TransactionFactory.GL);
                                            txMap.put("TRANS_MOD_TYPE", "GL");                     
                                        }
                                        txMap.put(TransferTrans.PARTICULARS, "LodgeID : " + objLodgementMasterTO.getLodgementId() + " Realize");
                                        txMap.put(TransferTrans.AUTHORIZE_STATUS_2, "ENTERED_AMOUNT");
                                        txMap.put("SCREEN_NAME",(String) obj.get(CommonConstants.SCREEN));// Added by nithya on 23-09-2016 for inserting screen name for transactions
                                        transferTo = transactionDAO.addTransferDebitLocal(txMap, transAmt);                                        
                                        transferTo.setInstType(objLodgementMasterTO.getBillsStatus());
                                        transferTo.setStatusBy(CommonUtil.convertObjToStr(obj.get("USER_ID")));
                                        transferTo.setInitTransId(CommonUtil.convertObjToStr(obj.get("USER_ID")));
                                        transferList.add(transferTo);
                                        txMap.put(TransferTrans.DR_PROD_ID,"");                                        
                                        ////for 8 dr
                                        Double otherBankCharge1 = 0.0;
                                        otherBankCharge1 = CommonUtil.convertObjToDouble(objLodgementRemitTO.getRemitFavouring()).doubleValue();
                                        if (otherBankCharge1 > 0) {
                                            if ((objLodgementRemitTO.getRemitProdId().equals("PO")) || (objLodgementRemitTO.getRemitProdId().equals("DD"))) {
                                                txMap.put(TransferTrans.DR_AC_HD, (String) acHeads.get("BILLS_REALISED_HD"));
                                            } else if (objLodgementRemitTO.getRemitProdId().equals("IBR")) {
                                                System.out.println("acHeads.get(\"IBR_AC_HD\")" + acHeads.get("IBR_AC_HD"));
                                                txMap.put(TransferTrans.DR_AC_HD, (String) acHeads.get("IBR_AC_HD"));
                                            } else if (objLodgementRemitTO.getRemitProdId().equals("OBADV")) {
                                                if (obj.containsKey("OTHER_BANK_HEAD")) {                                                    
                                                    txMap.put(TransferTrans.DR_AC_HD, obj.get("OTHER_BANK_HEAD"));
                                                    //Added By Suresh                                                    
                                                    if (obj.get("OTHER_BANK_PROD_TYPE") != null && obj.get("OTHER_BANK_PROD_TYPE").equals("INV")) {
                                                        InvestmentsTransTO objTO = new InvestmentsTransTO();
                                                        obj.put("INVESTMENT_ACC_NO", CommonUtil.convertObjToStr(obj.get("OTHER_BANK_HEAD")));
                                                        objTO = getInvestmentsTransTO(CommonConstants.TOSTATUS_INSERT, transAmt, obj, obj);
                                                        HashMap achdMap = new HashMap();
                                                        achdMap.put("INVESTMENT_PROD_ID", CommonUtil.convertObjToStr(objTO.getInvestmentID()));
                                                        List achdLst = ServerUtil.executeQuery("getSelectinvestmentAccountHead", achdMap);
                                                        if (achdLst != null && achdLst.size() > 0) {
                                                            achdMap = new HashMap();
                                                            achdMap = (HashMap) achdLst.get(0);
                                                            txMap.put(TransferTrans.DR_AC_HD, acHeads.get("CONT_EXP_BNK_CHRG_DR_HD"));
                                                            acHeads.put("IINVESTMENT_AC_HD", achdMap.get("IINVESTMENT_AC_HD"));
                                                            txMap.put("AUTHORIZEREMARKS", "IINVESTMENT_DEPOSIT");
                                                            //Set Trans ID
                                                            objTO.setBatchID(CommonUtil.convertObjToStr(objLodgementMasterTO.getLodgementId()));
                                                            sqlMap.executeUpdate("insertInvestmentTransTO", objTO);
                                                        } else {
                                                            throw new TTException("Account heads not set properly...");
                                                        }
                                                    }
                                                    System.out.println("obj.get(\"OTHER_BANK_PROD_TYPE\")" + obj.get("OTHER_BANK_PROD_TYPE"));
                                                    if (obj.get("OTHER_BANK_PROD_TYPE") != null && obj.get("OTHER_BANK_PROD_TYPE").equals("AB")) {
                                                        InvestmentsTransTO objTO = new InvestmentsTransTO();
                                                        obj.put("INVESTMENT_ACC_NO", CommonUtil.convertObjToStr(obj.get("OTHER_BANK_HEAD")));
                                                        HashMap achdMap = new HashMap();
                                                        if (coll_Obc_From_Cust!=null && (coll_Obc_From_Cust.equals("N") || coll_Obc_From_Cust.equals("Y"))) {
                                                            String bankChargeHead=CommonUtil.convertObjToStr(acHeads.get("DEBIT_BANK_CHRG_HEAD"));
                                                            if(bankChargeHead!=null && !bankChargeHead.equals("")){
                                                                txMap.put(TransferTrans.DR_AC_HD,bankChargeHead);
                                                                txMap.put("AUTHORIZEREMARKS", "BANK_CHARGE_HEAD");
                                                            }
                                                            else {
                                                                throw new TTException("Account heads not set properly...");
                                                            }
                                                        }
                                                        else{
                                                            List achdLst = ServerUtil.executeQuery("getSelectOthrBankAcHd", obj);
                                                            if (achdLst != null && achdLst.size() > 0) {
                                                                achdMap = new HashMap();
                                                                achdMap = (HashMap) achdLst.get(0);
                                                                txMap.put(TransferTrans.DR_AC_HD, acHeads.get("CONT_EXP_BNK_CHRG_DR_HD"));
                                                                acHeads.put("IINVESTMENT_AC_HD", achdMap.get("PRINCIPAL_AC_HD"));
                                                                txMap.put("AUTHORIZEREMARKS", "PRINCIPAL_AC_HD");
                                                                txMap.put(TransferTrans.DR_ACT_NUM, CommonUtil.convertObjToStr(obj.get("OTHER_BANK_HEAD")));//babu comm 
                                                            } else {
                                                                throw new TTException("Account heads not set properly...");
                                                            }
                                                        }
                                                    }
                                                } else {
                                                    txMap.put(TransferTrans.DR_AC_HD, (String) acHeads.get("BILLS_REALISED_HD"));
                                                }
                                            }
                                            txMap.put(TransferTrans.DR_BRANCH, branchID);
                                            txMap.put(TransferTrans.CURRENCY, "INR");                                            
                                            //bbb1
                                             if(coll_Obc_From_Cust.equals("N")){
                                                    txMap.put(TransferTrans.DR_PROD_TYPE, TransactionFactory.GL);
                                                    txMap.put("TRANS_MOD_TYPE", "GL");
                                             }
                                             else{                                    
                                                    txMap.put(TransferTrans.DR_PROD_TYPE, TransactionFactory.GL);
                                                    txMap.put("TRANS_MOD_TYPE", "GL");
                                                    txMap.put(TransferTrans.DR_ACT_NUM, "");
                                             }
                                            txMap.put(TransferTrans.PARTICULARS, "LodgeID : " + objLodgementMasterTO.getLodgementId() + " Realize");
                                            txMap.put(TransferTrans.AUTHORIZE_STATUS_2, "ENTERED_AMOUNT");
                                            txMap.put("SCREEN_NAME",(String) obj.get(CommonConstants.SCREEN));// Added by nithya on 23-09-2016 for inserting screen name for transactions
                                            transferTo = transactionDAO.addTransferDebitLocal(txMap, otherBankCharge1);                                            
                                            transferTo.setInstType(objLodgementMasterTO.getBillsStatus());
                                            transferTo.setStatusBy(CommonUtil.convertObjToStr(obj.get("USER_ID")));
                                            transferTo.setInitTransId(CommonUtil.convertObjToStr(obj.get("USER_ID")));
                                            transferList.add(transferTo);                                            
                                            if ((objLodgementRemitTO.getRemitProdId().equals("PO")) || (objLodgementRemitTO.getRemitProdId().equals("DD"))) {
                                                txMap.put(TransferTrans.CR_AC_HD, (String) acHeads.get("BILLS_REALISED_HD"));
                                            } else if (objLodgementRemitTO.getRemitProdId().equals("IBR")) {
                                                txMap.put(TransferTrans.CR_AC_HD, (String) acHeads.get("IBR_AC_HD"));
                                            } else if (objLodgementRemitTO.getRemitProdId().equals("OBADV")) {
                                                if (obj.containsKey("OTHER_BANK_HEAD")) {
                                                    System.out.println("OTHER_BANK_HEAD in new");
                                                    txMap.put(TransferTrans.CR_AC_HD, obj.get("OTHER_BANK_HEAD"));
                                                    if ((obj.get("OTHER_BANK_PROD_TYPE") != null && obj.get("OTHER_BANK_PROD_TYPE").equals("INV")) && creditObcHd.equals("Investment A/c Hd")) {
                                                        InvestmentsTransTO objTO = new InvestmentsTransTO();
                                                        obj.put("INVESTMENT_ACC_NO", CommonUtil.convertObjToStr(obj.get("OTHER_BANK_HEAD")));
                                                        objTO = getInvestmentsCreditTransTO(CommonConstants.TOSTATUS_INSERT, otherBankCharge1, obj, obj);
                                                        objTO.setBatchID(CommonUtil.convertObjToStr(objLodgementMasterTO.getLodgementId()));
                                                        sqlMap.executeUpdate("insertInvestmentTransTO", objTO);
                                                        txMap.put(TransferTrans.CR_AC_HD, (String) acHeads.get("IINVESTMENT_AC_HD"));
                                                    } else if (creditObcHd.equals("Gl A/c Hd")) {
                                                        txMap.put(TransferTrans.CR_AC_HD, (String) acHeads.get("OBC_AC_HD"));
                                                    }
                                                    if (obj.get("OTHER_BANK_PROD_TYPE") != null && obj.get("OTHER_BANK_PROD_TYPE").equals("AB")) {
                                                        InvestmentsTransTO objTO = new InvestmentsTransTO();
                                                        obj.put("INVESTMENT_ACC_NO", CommonUtil.convertObjToStr(obj.get("OTHER_BANK_HEAD")));
                                                        HashMap achdMap = new HashMap();
                                                        List achdLst = ServerUtil.executeQuery("getSelectOthrBankAcHd", obj);
                                                        if (achdLst != null && achdLst.size() > 0) {
                                                            achdMap = new HashMap();
                                                            achdMap = (HashMap) achdLst.get(0);
                                                            txMap.put(TransferTrans.CR_AC_HD, achdMap.get("PRINCIPAL_AC_HD"));
                                                            acHeads.put("IINVESTMENT_AC_HD", achdMap.get("PRINCIPAL_AC_HD"));
                                                            txMap.put("AUTHORIZEREMARKS", "PRINCIPAL_AC_HD");
                                                            System.out.println("achdMap get act_master_id" + achdMap.get("ACT_MASTER_ID"));
                                                            txMap.put(TransferTrans.CR_ACT_NUM, achdMap.get("ACT_MASTER_ID"));
                                                        }//                                                                 
                                                    }
                                                } else {
                                                    txMap.put(TransferTrans.CR_AC_HD, (String) acHeads.get("BILLS_REALISED_HD"));
                                                }
                                            }
                                             if(coll_Obc_From_Cust.equals("N")){
                                                 if(obj.get("OTHER_BANK_PROD_TYPE")!=null && obj.get("OTHER_BANK_PROD_TYPE").equals("AB")){
                                                      txMap.put(TransferTrans.CR_PROD_TYPE, TransactionFactory.OTHERBANKACTS);  
                                                       txMap.put("TRANS_MOD_TYPE", "AB");
                                                 }
                                            }
                                            else{
                                                 if (obj.get("OTHER_BANK_PROD_TYPE") != null && obj.get("OTHER_BANK_PROD_TYPE").equals("AB")) {
                                                    txMap.put(TransferTrans.CR_PROD_TYPE, TransactionFactory.OTHERBANKACTS);
                                                    txMap.put("TRANS_MOD_TYPE", "AB");
                                                    txMap.put(TransferTrans.CR_ACT_NUM, CommonUtil.convertObjToStr(obj.get("OTHER_BANK_HEAD")));
                                                    txMap.put(TransferTrans.CR_PROD_ID, CommonUtil.convertObjToStr(objLodgementMasterTO.getRecOtherBank()));                                                    
                                                } else {
                                                txMap.put(TransferTrans.CR_PROD_TYPE, TransactionFactory.GL);
                                                txMap.put("TRANS_MOD_TYPE", "GL");
                                                 }
                                             }                                        
                                            txMap.put(TransferTrans.CR_BRANCH, branchID);
                                            if (obj.get("OTHER_BANK_PROD_TYPE") != null && obj.get("OTHER_BANK_PROD_TYPE").equals("AB")) {
                                              txMap.put(TransferTrans.CR_BRANCH, otherBankBranch);  // Added by nithya on 10-10-2019 for KD-653
                                                if (!(otherBankBranch.equalsIgnoreCase(branchID))) {
                                                    txMap.put("BILLS_INTER_BRANCH_TRANS", "BILLS_INTER_BRANCH_TRANS");
                                                    txMap.put("INITIATED_BRANCH", _branchCode);
                                                }
                                            }
                                            txMap.put(TransferTrans.PARTICULARS, objLodgementMasterTO.getLodgementId() + " Realize / Comm-"
                                                    + " / OBC-" + objLodgementRemitTO.getRemitFavouring());
                                            txMap.put(TransferTrans.AUTHORIZE_STATUS_2, "ENTERED_AMOUNT");
                                            txMap.put("SCREEN_NAME",(String) obj.get(CommonConstants.SCREEN));// Added by nithya on 23-09-2016 for inserting screen name for transactions
                                            transferTo = transactionDAO.addTransferCreditLocal(txMap, otherBankCharge1);
                                            transferTo.setInstType(objLodgementMasterTO.getBillsStatus());
                                            transferTo.setStatusBy(CommonUtil.convertObjToStr(obj.get("USER_ID")));
                                            transferTo.setInitTransId(CommonUtil.convertObjToStr(obj.get("USER_ID")));
                                            transferList.add(transferTo);                                            
                                            txMap.put(TransferTrans.CR_PROD_ID,"");   
                                        }
                                        //////////////
                                        //////////3 cr changed to 11
                                        if (coll_Obc_From_Cust.equals("Y")) {
                                            temp = CommonUtil.convertObjToDouble(objLodgementChequeTO.getInstrumentAmount()).doubleValue();
                                        } else {
                                            temp = CommonUtil.convertObjToDouble(objLodgementRemitTO.getRemitInstAmt()).doubleValue();
                                        }
                                        double commTot = 0;
                                        for (int i = 0; i < instructionList.size(); i++) {
                                            objLodgementInstTO = (LodgementInstructionsTO) instructionList.get(i);
                                            String status = CommonUtil.convertObjToStr(objLodgementInstTO.getStatus());
                                            if (!status.equals("DELETED")) {
                                                transAmt = CommonUtil.convertObjToDouble(objLodgementInstTO.getAmount()).doubleValue();
                                                commTot += transAmt;
                                                if (CommonUtil.convertObjToDouble(objLodgementInstTO.getServiceTx()).doubleValue() > 0) {
                                                    transAmt = CommonUtil.convertObjToDouble(objLodgementInstTO.getServiceTx()).doubleValue();
                                                    commTot += transAmt;
                                                    txMap.put(TransferTrans.CR_AC_HD, (String) acHeads.get("SERVICE_TAX_HD"));  // credit to Postage Charge account head......
                                                    txMap.put(TransferTrans.PARTICULARS, "ServiceTax LodgeID : " + objLodgementMasterTO.getLodgementId() + " Realize");
                                                    txMap.put("TRANS_MOD_TYPE", "GL");
                                                    txMap.put("SCREEN_NAME",(String) obj.get(CommonConstants.SCREEN));// Added by nithya on 23-09-2016 for inserting screen name for transactions
                                                    if(coll_Obc_From_Cust.equals("Y")){
                                                         transferTo = transactionDAO.addTransferCreditLocal(txMap, transAmt);
                                                    }
                                                    transferTo.setInstType(objLodgementMasterTO.getBillsStatus());
                                                    transferList.add(transferTo);
                                                }
                                                double serTaxAmtTemp = 0;
                                                if (coll_Obc_From_Cust.equals("N")) {
                                                    if (obj.containsKey("serviceTaxDetails")) {
                                                        HashMap serTaxMap = (HashMap) obj.get("serviceTaxDetails");
                                                        serTaxAmtTemp = CommonUtil.convertObjToDouble(serTaxMap.get("TOT_TAX_AMT"));
                                                    }
                                                }
                                                temp = temp - (CommonUtil.convertObjToDouble(objLodgementInstTO.getAmount()).doubleValue())
                                                        - (CommonUtil.convertObjToDouble(objLodgementInstTO.getServiceTx()).doubleValue())
                                                        - serTaxAmtTemp;
                                                //INSERT INSTRUCTION
                                                HashMap instMap = new HashMap();
                                                instMap.put("LODGEMENT_ID", objLodgementMasterTO.getLodgementId());
                                                instMap.put("INSTRUCTION", objLodgementInstTO.getInstruction());
                                                List lstInst = sqlMap.executeQueryForList("getBillsExtInst", instMap);
                                                instMap = null;
                                                if (lstInst != null && lstInst.size() > 0) {
                                                    sqlMap.executeUpdate("updateLodgementInstructionsTO", objLodgementInstTO);
                                                } else {
                                                    sqlMap.executeUpdate("insertLodgementInstructionsTO", objLodgementInstTO);
                                                }
                                                lstInst = null;
                                                status = null;
                                            }
                                        }
                                        if(coll_Obc_From_Cust.equals("N")){
                                            txMap.put(TransferTrans.CR_AC_HD, (String) acHeads.get("COMM_AC_HD"));  // credit to bank comm account head......
                                            txMap.put(TransferTrans.PARTICULARS, "Total  charges: " + objLodgementMasterTO.getLodgementId() + " Realize");
                                            txMap.put("TRANS_MOD_TYPE", "GL");
                                            txMap.put(TransferTrans.CR_BRANCH, branchID);
                                            txMap.put(TransferTrans.CR_PROD_TYPE, TransactionFactory.GL);
                                            txMap.put("SCREEN_NAME",(String) obj.get(CommonConstants.SCREEN));// Added by nithya on 23-09-2016 for inserting screen name for transactions
                                            transferTo = transactionDAO.addTransferCreditLocal(txMap, commTot+otherBankCharge1);
                                            transferTo.setInstType(objLodgementMasterTO.getBillsStatus());
                                            transferList.add(transferTo);        
                                        }  
                                        ///////////////////11 cr
                                       //Added by chithra for servicetax // Modifying by nithya for GST
                                          if (obj.containsKey("serviceTaxDetails")) {
                                              double swachhCess = 0.0;
                                              double krishikalyanCess = 0.0;
                                              double normalServiceTax = 0.0;
                                              double serTaxAmt = 0.0;
                                              HashMap serTaxMap = (HashMap) obj.get("serviceTaxDetails");
                                              if (serTaxMap != null && serTaxMap.size() > 0) {
                                                  if (serTaxMap.containsKey("TOT_TAX_AMT") && serTaxMap.get("TOT_TAX_AMT") != null) {
                                                      serTaxAmt = CommonUtil.convertObjToDouble(serTaxMap.get("TOT_TAX_AMT"));
                                                  }
                                                  if (serTaxMap.containsKey(ServiceTaxCalculation.SWACHH_CESS) && serTaxMap.get(ServiceTaxCalculation.SWACHH_CESS) != null) {
                                                      swachhCess = CommonUtil.convertObjToDouble(serTaxMap.get(ServiceTaxCalculation.SWACHH_CESS));
                                                  }
                                                  if (serTaxMap.containsKey(ServiceTaxCalculation.KRISHIKALYAN_CESS) && serTaxMap.get(ServiceTaxCalculation.KRISHIKALYAN_CESS) != null) {
                                                      krishikalyanCess = CommonUtil.convertObjToDouble(serTaxMap.get(ServiceTaxCalculation.KRISHIKALYAN_CESS));
                                                  }
                                                  if (serTaxMap.containsKey(ServiceTaxCalculation.SERVICE_TAX) && serTaxMap.get(ServiceTaxCalculation.SERVICE_TAX) != null) {
                                                      normalServiceTax = CommonUtil.convertObjToDouble(serTaxMap.get(ServiceTaxCalculation.SERVICE_TAX));
                                                  }
                                                 // normalServiceTax -= (swachhCess + krishikalyanCess);
                                              }
                                              txMap.put(TransferTrans.CR_BRANCH, branchID);// Added by nithya on 10-10-2019 for KD-653
                                              if (swachhCess > 0) {
                                                  txMap.put(TransferTrans.CR_AC_HD, CommonUtil.convertObjToStr(serTaxMap.get("SWACHH_HEAD_ID")));  // credit to Postage Charge account head......
                                                  txMap.put(TransferTrans.PARTICULARS, "CGST : " + objLodgementMasterTO.getLodgementId() + " Realize");
                                                  if (obj.get("OTHER_BANK_PROD_TYPE") != null && obj.get("OTHER_BANK_PROD_TYPE").equals(TransactionFactory.OTHERBANKACTS)) {
                                                      txMap.put(TransferTrans.CR_ACT_NUM, null);
                                                  }
                                                  txMap.put(TransferTrans.AUTHORIZE_STATUS_2, "ENTERED_AMOUNT");
                                                  txMap.put("TRANS_MOD_TYPE", TransactionFactory.GL);
                                                  txMap.put(TransferTrans.CR_PROD_TYPE, TransactionFactory.GL);
                                                  txMap.put("SCREEN_NAME", (String) obj.get(CommonConstants.SCREEN));// Added by nithya on 23-09-2016 for inserting screen name for transactions
                                                  transferTo = transactionDAO.addTransferCreditLocal(txMap, swachhCess);
                                                  transferTo.setInstType(objLodgementMasterTO.getBillsStatus());
                                                  transferTo.setStatusBy(CommonUtil.convertObjToStr(obj.get("USER_ID")));
                                                  transferTo.setInitTransId(CommonUtil.convertObjToStr(obj.get("USER_ID")));
                                                  transferList.add(transferTo);
                                                  //normalServiceTax -= swachhCess;
                                              }
                                              if (krishikalyanCess > 0) {
                                                  txMap.put(TransferTrans.CR_AC_HD, CommonUtil.convertObjToStr(serTaxMap.get("KRISHIKALYAN_HEAD_ID")));  // credit to Postage Charge account head......
                                                  txMap.put(TransferTrans.PARTICULARS, "SGST : " + objLodgementMasterTO.getLodgementId() + " Realize");
                                                  if (obj.get("OTHER_BANK_PROD_TYPE") != null && obj.get("OTHER_BANK_PROD_TYPE").equals(TransactionFactory.OTHERBANKACTS)) {
                                                      txMap.put(TransferTrans.CR_ACT_NUM, null);
                                                  }
                                                  txMap.put(TransferTrans.AUTHORIZE_STATUS_2, "ENTERED_AMOUNT");
                                                  txMap.put("TRANS_MOD_TYPE", TransactionFactory.GL);
                                                  txMap.put(TransferTrans.CR_PROD_TYPE, TransactionFactory.GL);
                                                  txMap.put("SCREEN_NAME", (String) obj.get(CommonConstants.SCREEN));// Added by nithya on 23-09-2016 for inserting screen name for transactions
                                                  transferTo = transactionDAO.addTransferCreditLocal(txMap, krishikalyanCess);
                                                  transferTo.setInstType(objLodgementMasterTO.getBillsStatus());
                                                  transferTo.setStatusBy(CommonUtil.convertObjToStr(obj.get("USER_ID")));
                                                  transferTo.setInitTransId(CommonUtil.convertObjToStr(obj.get("USER_ID")));
                                                  transferList.add(transferTo);
                                                  //normalServiceTax -= krishikalyanCess;
                                              }
                                              if (normalServiceTax > 0) {
                                                  txMap.put(TransferTrans.CR_AC_HD, CommonUtil.convertObjToStr(serTaxMap.get("TAX_HEAD_ID")));  // credit to Postage Charge account head......
                                                  txMap.put(TransferTrans.PARTICULARS, "SERVICE TAX for BankCharges LodgeID : " + objLodgementMasterTO.getLodgementId() + " Realize");
                                                  if (obj.get("OTHER_BANK_PROD_TYPE") != null && obj.get("OTHER_BANK_PROD_TYPE").equals(TransactionFactory.OTHERBANKACTS)) {
                                                      txMap.put(TransferTrans.CR_ACT_NUM, null);
                                                  }
                                                  txMap.put(TransferTrans.AUTHORIZE_STATUS_2, "ENTERED_AMOUNT");
                                                  txMap.put("TRANS_MOD_TYPE", TransactionFactory.GL);
                                                  txMap.put(TransferTrans.CR_PROD_TYPE, TransactionFactory.GL);
                                                  txMap.put("SCREEN_NAME", (String) obj.get(CommonConstants.SCREEN));// Added by nithya on 23-09-2016 for inserting screen name for transactions
                                                  transferTo = transactionDAO.addTransferCreditLocal(txMap, normalServiceTax);
                                                  transferTo.setInstType(objLodgementMasterTO.getBillsStatus());
                                                  transferTo.setStatusBy(CommonUtil.convertObjToStr(obj.get("USER_ID")));
                                                  transferTo.setInitTransId(CommonUtil.convertObjToStr(obj.get("USER_ID")));
                                                  transferList.add(transferTo);
                                              }
                                          }
                                              if (coll_Obc_From_Cust.equals("Y")) {
                                            double otherBankCharge = 0.0;
                                            double totalCommisionCharge = 0.0;
                                            otherBankCharge = CommonUtil.convertObjToDouble(objLodgementRemitTO.getRemitFavouring()).doubleValue();  //Only Charge Amount
                                            totalCommisionCharge = otherBankCharge + commTot;  // commTot -> Toatal Commision Amount
                                            if (totalCommisionCharge > 0) {
                                                txMap.put(TransferTrans.CR_BRANCH, branchID);// Added by nithya on 10-10-2019 for KD-653
                                                txMap.put(TransferTrans.CR_AC_HD, (String) acHeads.get("MISC_INC_BNK_CHRG_CR_HD"));  // credit to Postage Charge account head......
                                                txMap.put(TransferTrans.PARTICULARS, "BankCharges LodgeID : " + objLodgementMasterTO.getLodgementId() + " Realize");
                                                System.out.println("transAmt 2 cr" + totalCommisionCharge);
                                                if (obj.get("OTHER_BANK_PROD_TYPE") != null && obj.get("OTHER_BANK_PROD_TYPE").equals("AB")) {
                                                    txMap.put(TransferTrans.CR_ACT_NUM, null);
                                                }
                                                txMap.put(TransferTrans.AUTHORIZE_STATUS_2, "ENTERED_AMOUNT");
                                                txMap.put("TRANS_MOD_TYPE", "GL");
                                                txMap.put(TransferTrans.CR_PROD_TYPE, TransactionFactory.GL);
                                                txMap.put("SCREEN_NAME",(String) obj.get(CommonConstants.SCREEN));// Added by nithya on 23-09-2016 for inserting screen name for transactions
                                                transferTo = transactionDAO.addTransferCreditLocal(txMap, totalCommisionCharge);
                                                transferTo.setInstType(objLodgementMasterTO.getBillsStatus());
                                                transferTo.setStatusBy(CommonUtil.convertObjToStr(obj.get("USER_ID")));
                                                transferTo.setInitTransId(CommonUtil.convertObjToStr(obj.get("USER_ID")));
                                                transferList.add(transferTo);
                                            }                                           
                                        }
                                        //////////////////////////
                                        ///////////////////////////500 cr
                                        if (coll_Obc_From_Cust.equals("Y")) {
                                            temp = CommonUtil.convertObjToDouble(objLodgementChequeTO.getInstrumentAmount()).doubleValue();
                                        }
                                        if (temp > 0) {
                                            transAmt = temp;
                                            txMap.put(TransferTrans.CR_AC_HD, null);
                                            txMap.put(TransferTrans.CR_PROD_TYPE, objLodgementMasterTO.getProdType());
                                            txMap.put(TransferTrans.CR_PROD_ID, objLodgementMasterTO.getBorrowProdId());
                                            txMap.put(TransferTrans.CR_ACT_NUM, objLodgementMasterTO.getBorrowAcctNum());
                                            txMap.put(TransferTrans.CR_BRANCH, branchID);
                                            txMap.put("TRANS_MOD_TYPE", objLodgementMasterTO.getProdType());
                                            if (!objLodgementMasterTO.getProdType().equals("") && objLodgementMasterTO.getProdType().equals("GL") && objLodgementMasterTO.getBorrowProdId().equals("")) {
                                                txMap.put(TransferTrans.CR_AC_HD, (String) objLodgementMasterTO.getBorrowAcctNum());
                                                txMap.put(TransferTrans.CR_PROD_TYPE, TransactionFactory.GL);
                                            }
                                            txMap.put(TransferTrans.PARTICULARS, objLodgementMasterTO.getLodgementId() + " Realize / Comm-"
                                                    + commTot + " / OBC-" + objLodgementRemitTO.getRemitFavouring());
                                            txMap.put(TransferTrans.AUTHORIZE_STATUS_2, "ENTERED_AMOUNT");
                                            txMap.put("SCREEN_NAME",(String) obj.get(CommonConstants.SCREEN));// Added by nithya on 23-09-2016 for inserting screen name for transactions
                                            transferTo = transactionDAO.addTransferCreditLocal(txMap, transAmt);
                                            transferTo.setInstType(objLodgementMasterTO.getBillsStatus());
                                            transferTo.setStatusBy(CommonUtil.convertObjToStr(obj.get("USER_ID")));
                                            transferTo.setInitTransId(CommonUtil.convertObjToStr(obj.get("USER_ID")));
                                            transferTo.setBranchId(CommonUtil.convertObjToStr(obj.get("SELECTED_BRANCH_ID")));
                                            transferList.add(transferTo);
                                            temp = 0;
                                        }                                        
                                        ///////////////////////////////
                                        ////////////////Added By Suresh  11 dr
                                        if (coll_Obc_From_Cust.equals("Y")) {
                                            double otherBankCharge = 0.0;
                                            double totalCommisionCharge = 0.0;
                                            double serTaxAmt =0.0;
                                            if (obj.containsKey("serviceTaxDetails")) {
                                                HashMap serTaxMap = (HashMap) obj.get("serviceTaxDetails");
                                                 serTaxAmt = CommonUtil.convertObjToDouble(serTaxMap.get("TOT_TAX_AMT"));
                                            }
                                            otherBankCharge = CommonUtil.convertObjToDouble(objLodgementRemitTO.getRemitFavouring()).doubleValue();  //Only Charge Amount
                                            totalCommisionCharge = otherBankCharge + commTot+serTaxAmt;  // commTot -> Toatal Commision Amount
                                            if (totalCommisionCharge > 0) {                                              
                                                if (!objLodgementMasterTO.getProdType().equals("GL")) {
                                                    txMap.put(TransferTrans.DR_AC_HD, (String) acHeadMap.get("AC_HEAD"));
                                                } else {
                                                    txMap.put(TransferTrans.DR_AC_HD, objLodgementMasterTO.getBorrowAcctNum());
                                                }
                                                txMap.put(TransferTrans.DR_ACT_NUM, objLodgementMasterTO.getBorrowAcctNum());
                                                txMap.put(TransferTrans.DR_BRANCH, branchID);
                                                txMap.put(TransferTrans.CURRENCY, "INR");
                                                txMap.put(TransferTrans.DR_PROD_TYPE, objLodgementMasterTO.getProdType());
                                                txMap.put("TRANS_MOD_TYPE", objLodgementMasterTO.getProdType());
                                                txMap.put(TransferTrans.DR_PROD_ID, objLodgementMasterTO.getBorrowProdId());
                                                txMap.put(TransferTrans.PARTICULARS, "LodgeID : " + objLodgementMasterTO.getLodgementId() + " Realize");
                                                txMap.put(TransferTrans.AUTHORIZE_STATUS_2, "ENTERED_AMOUNT");
                                                txMap.put("SCREEN_NAME",(String) obj.get(CommonConstants.SCREEN));// Added by nithya on 23-09-2016 for inserting screen name for transactions
                                                transferTo = transactionDAO.addTransferDebitLocal(txMap, totalCommisionCharge);
                                                transferTo.setInstType(objLodgementMasterTO.getBillsStatus());
                                                transferTo.setStatusBy(CommonUtil.convertObjToStr(obj.get("USER_ID")));
                                                transferTo.setInitTransId(CommonUtil.convertObjToStr(obj.get("USER_ID")));
                                                transferList.add(transferTo);
                                            }
                                        }
                                        transAmt = 0;
                                        transactionDAO.setTransactionType(transactionDAO.TRANSFER);
                                        transactionDAO.doTransferLocal(transferList, branchID);
                                    }
                                }
                            }
                            //OVERDUE INTEREST CODING
                            if ((CommonUtil.convertObjToDouble(objLodgementMasterTO.getRateForDelay()).doubleValue()) > 0) {
                                HashMap overDue = new HashMap();
                                overDue.put("LODGEMENT_ID", CommonUtil.convertObjToStr(objLodgementMasterTO.getLodgementId()));
                                overDue.put("CUR_DATE", currDt.clone());
                                List dueLst = sqlMap.executeQueryForList("getBillsOverDueIntDetails", overDue);
                                overDue = null;
                                if (dueLst != null && dueLst.size() > 0) {
                                    overDue = (HashMap) dueLst.get(0);
                                    double amount = CommonUtil.convertObjToDouble(objLodgementChequeTO.getInstrumentAmount()).doubleValue();
                                    double rateOfInterest = CommonUtil.convertObjToDouble(objLodgementMasterTO.getRateForDelay()).doubleValue();
                                    double period = CommonUtil.convertObjToDouble(overDue.get("INT_PERIOD")).doubleValue();
                                    double yearDays = 36500;
                                    double intAmount = (amount * rateOfInterest * period) / yearDays;  
                                    intAmount = (double) getNearest((long) (intAmount * 100), 100) / 100;
                                    if (intAmount > 0) {
                                        //transaction Coding
                                        txMap = new HashMap();
                                        txMap.put(TransferTrans.DR_AC_HD, (String) overDue.get("INT_AC_HD"));
                                        txMap.put(TransferTrans.DR_BRANCH, branchID);
                                        txMap.put(TransferTrans.CURRENCY, "INR");
                                        txMap.put(TransferTrans.DR_PROD_TYPE, TransactionFactory.GL);
                                        //                                txMap.put(TransferTrans.DR_INST_TYPE, objLodgementMasterTO.getBillsStatus());
                                        txMap.put(TransferTrans.PARTICULARS, "Int Col(Delay) LodgeID : " + objLodgementMasterTO.getLodgementId() + " Realize");
                                        txMap.put(TransferTrans.AUTHORIZE_STATUS_2, "ENTERED_AMOUNT");
                                        txMap.put("TRANS_MOD_TYPE", "GL");
                                        transAmt = intAmount;     
                                        txMap.put("SCREEN_NAME",(String) obj.get(CommonConstants.SCREEN));// Added by nithya on 23-09-2016 for inserting screen name for transactions
                                        transferTo = transactionDAO.addTransferDebitLocal(txMap, transAmt);                                        
                                        transferTo.setInstType(objLodgementMasterTO.getBillsStatus());
                                        transferList.add(transferTo);                                        
                                        txMap.put(TransferTrans.CR_AC_HD, null);
                                        txMap.put(TransferTrans.CR_PROD_TYPE, objLodgementMasterTO.getProdType());
                                        txMap.put("TRANS_MOD_TYPE", objLodgementMasterTO.getProdType());
                                        txMap.put(TransferTrans.CR_PROD_ID, objLodgementMasterTO.getBorrowProdId());
                                        txMap.put(TransferTrans.CR_ACT_NUM, objLodgementMasterTO.getBorrowAcctNum());
                                        txMap.put(TransferTrans.CR_BRANCH, branchID);
                                        txMap.put(TransferTrans.PARTICULARS, "Int Col(Delay) LodgeID : " + objLodgementMasterTO.getLodgementId() + " Realize");
                                        txMap.put(TransferTrans.AUTHORIZE_STATUS_2, "ENTERED_AMOUNT");
                                        txMap.put("SCREEN_NAME",(String) obj.get(CommonConstants.SCREEN));// Added by nithya on 23-09-2016 for inserting screen name for transactions
                                        transferTo = transactionDAO.addTransferCreditLocal(txMap, transAmt);
                                        transferTo.setInstType(objLodgementMasterTO.getBillsStatus());
                                        transferList.add(transferTo);
                                        transactionDAO.setTransactionType(transactionDAO.TRANSFER);
                                        transactionDAO.doTransferLocal(transferList, branchID);
                                    }
                                }
                            }
                        }//
//                      cashtransaction();
                   if (objLodgementMasterTO.getTranType().equals("CASH")) {                            
                            if ((objLodgementRemitTO.getRemitProdId().equals("PO")) || (objLodgementRemitTO.getRemitProdId().equals("DD"))
                                    || (objLodgementRemitTO.getRemitProdId().equals("IBR")) || (objLodgementRemitTO.getRemitProdId().equals("OBADV"))) {
                                double temp = 0;
                                temp = CommonUtil.convertObjToDouble(objLodgementRemitTO.getRemitInstAmt()).doubleValue();
                                for (int i = 0; i < instructionList.size(); i++) {
                                    objLodgementInstTO = (LodgementInstructionsTO) instructionList.get(i);
                                    String status = CommonUtil.convertObjToStr(objLodgementInstTO.getStatus());
                                    if (!status.equals("DELETED")) {
                                        transAmt = CommonUtil.convertObjToDouble(objLodgementInstTO.getAmount()).doubleValue();
                                        if (CommonUtil.convertObjToDouble(objLodgementInstTO.getServiceTx()).doubleValue() > 0) {
                                            transAmt = CommonUtil.convertObjToDouble(objLodgementInstTO.getServiceTx()).doubleValue();
                                        }
                                        temp = temp - (CommonUtil.convertObjToDouble(objLodgementInstTO.getAmount()).doubleValue())
                                                - (CommonUtil.convertObjToDouble(objLodgementInstTO.getServiceTx()).doubleValue());
                                        status = null;
                                    }
                                }
                                double charge = 0;
                                charge = CommonUtil.convertObjToDouble(objLodgementRemitTO.getRemitInstAmt()).doubleValue()
                                        - temp;                              
                                if ((objLodgementRemitTO.getRemitProdId().equals("PO")) || (objLodgementRemitTO.getRemitProdId().equals("DD"))) {
                                    txMap.put(TransferTrans.DR_AC_HD, (String) acHeads.get("BILLS_REALISED_HD"));
                                } else if (objLodgementRemitTO.getRemitProdId().equals("IBR")) {
                                    txMap.put(TransferTrans.DR_AC_HD, (String) acHeads.get("IBR_AC_HD"));
                          //added by rishad 20/01/2015
                                }  else if (objLodgementRemitTO.getRemitProdId().equals("OBADV")) {
                                                if (obj.containsKey("OTHER_BANK_HEAD")) {                                                    
                                                    txMap.put(TransferTrans.DR_AC_HD, obj.get("OTHER_BANK_HEAD"));
                                                    //Added By Suresh                                                    
                                                    if (obj.get("OTHER_BANK_PROD_TYPE") != null && obj.get("OTHER_BANK_PROD_TYPE").equals("INV")) {
                                                        InvestmentsTransTO objTO = new InvestmentsTransTO();
                                                        obj.put("INVESTMENT_ACC_NO", CommonUtil.convertObjToStr(obj.get("OTHER_BANK_HEAD")));
                                                        objTO = getInvestmentsTransTO(CommonConstants.TOSTATUS_INSERT, transAmt, obj, obj);
                                                        HashMap achdMap = new HashMap();
                                                        achdMap.put("INVESTMENT_PROD_ID", CommonUtil.convertObjToStr(objTO.getInvestmentID()));
                                                        List achdLst = ServerUtil.executeQuery("getSelectinvestmentAccountHead", achdMap);
                                                        if (achdLst != null && achdLst.size() > 0) {
                                                            achdMap = new HashMap();
                                                            achdMap = (HashMap) achdLst.get(0);
                                                            txMap.put(TransferTrans.DR_AC_HD, acHeads.get("CONT_EXP_BNK_CHRG_DR_HD"));
                                                            acHeads.put("IINVESTMENT_AC_HD", achdMap.get("IINVESTMENT_AC_HD"));
                                                            txMap.put("AUTHORIZEREMARKS", "IINVESTMENT_DEPOSIT");
                                                            //Set Trans ID
                                                            objTO.setBatchID(CommonUtil.convertObjToStr(objLodgementMasterTO.getLodgementId()));
                                                            sqlMap.executeUpdate("insertInvestmentTransTO", objTO);
                                                        } else {
                                                            throw new TTException("Account heads not set properly...");
                                                        }
                                                    }                                                    
                                                    if (obj.get("OTHER_BANK_PROD_TYPE") != null && obj.get("OTHER_BANK_PROD_TYPE").equals("AB")) {
                                                        InvestmentsTransTO objTO = new InvestmentsTransTO();
                                                        obj.put("INVESTMENT_ACC_NO", CommonUtil.convertObjToStr(obj.get("OTHER_BANK_HEAD")));
                                                        //objTO = getInvestmentsTransTO(CommonConstants.TOSTATUS_INSERT, transAmt, obj, obj);
                                                        HashMap achdMap = new HashMap();
//                                                achdMap.put("INVESTMENT_PROD_ID", CommonUtil.convertObjToStr(objTO.getInvestmentID()));
//                                                List achdLst=ServerUtil.executeQuery("getSelectOthrBankAcHd1", obj);
                                                        if (coll_Obc_From_Cust!=null && (coll_Obc_From_Cust.equals("N") || coll_Obc_From_Cust.equals("Y"))) {
                                                            //String bankChargeHead=CommonUtil.convertObjToStr(acHeads.get("BANK_CHARGE_HEAD"));
                                                            String bankChargeHead=CommonUtil.convertObjToStr(acHeads.get("DEBIT_BANK_CHRG_HEAD"));
                                                            if(bankChargeHead!=null && !bankChargeHead.equals("")){
                                                                txMap.put(TransferTrans.DR_AC_HD,bankChargeHead);
                                                                txMap.put("AUTHORIZEREMARKS", "BANK_CHARGE_HEAD");
                                                            }
                                                            else {
                                                                throw new TTException("Account heads not set properly...");
                                                            }
                                                        }
                                                        else{
                                                            List achdLst = ServerUtil.executeQuery("getSelectOthrBankAcHd", obj);
                                                            if (achdLst != null && achdLst.size() > 0) {
                                                                achdMap = new HashMap();
                                                                achdMap = (HashMap) achdLst.get(0);
                                                                txMap.put(TransferTrans.DR_AC_HD, acHeads.get("CONT_EXP_BNK_CHRG_DR_HD"));
                                                                acHeads.put("IINVESTMENT_AC_HD", achdMap.get("PRINCIPAL_AC_HD"));
                                                                txMap.put("AUTHORIZEREMARKS", "PRINCIPAL_AC_HD");
                                                                //Set Trans ID
    //                                                    		objTO.setBatchID(CommonUtil.convertObjToStr(objLodgementMasterTO.getLodgementId()));
    //                                                    		sqlMap.executeUpdate("insertInvestmentTransTO", objTO);
    //                                                   		txMap.put(TransferTrans.DR_ACT_NUM,CommonUtil.convertObjToStr(obj.get("OTHER_BANK_HEAD")));
                                                                txMap.put(TransferTrans.DR_ACT_NUM, CommonUtil.convertObjToStr(obj.get("OTHER_BANK_HEAD")));//babu comm 
                                                                //  txMap.put(TransferTrans.DR_ACT_NUM,objLodgementMasterTO.getBorrowAcctNum());
                                                            } else {
                                                                throw new TTException("Account heads not set properly...");
                                                            }
                                                        }
                                                    }
                                                } else {
                                                    txMap.put(TransferTrans.DR_AC_HD, (String) acHeads.get("BILLS_REALISED_HD"));
                                                }
                                            }
                                    //end
                                txMap.put(TransferTrans.DR_BRANCH, branchID);
                                txMap.put(TransferTrans.CURRENCY, "INR");
                                txMap.put(TransferTrans.DR_PROD_TYPE, TransactionFactory.GL);
                                txMap.put("TRANS_MOD_TYPE", "GL");
                                //                            txMap.put(TransferTrans.DR_INST_TYPE, objLodgementMasterTO.getBillsStatus());
                                transAmt = charge;
                                txMap.put(TransferTrans.AUTHORIZE_STATUS_2, "ENTERED_AMOUNT");
//                                transferTo = transactionDAO.addTransferDebitLocal(txMap, transAmt);
//                                transferTo.setInstType(objLodgementMasterTO.getBillsStatus());
                               // transferList.add(transferTo);
                                    double commTot = 0;
                                for (int i = 0; i < instructionList.size(); i++) {
                                    objLodgementInstTO = (LodgementInstructionsTO) instructionList.get(i);
                                    String status = CommonUtil.convertObjToStr(objLodgementInstTO.getStatus());
                                    if (!status.equals("DELETED")) {
                                        transAmt = CommonUtil.convertObjToDouble(objLodgementInstTO.getAmount()).doubleValue();
                                        commTot+=transAmt;
//                                        txMap.put(TransferTrans.CR_AC_HD, (String) acHeads.get("CHRG_AC_HD"));  // credit to Postage Charge account head......
//                                        //                                txMap.put(TransferTrans.DR_INST_TYPE, objLodgementMasterTO.getBillsStatus());
//                                        txMap.put(TransferTrans.AUTHORIZE_STATUS_2, "ENTERED_AMOUNT");
//                                        txMap.put("TRANS_MOD_TYPE", "GL");
//                                        transferTo = transactionDAO.addTransferCreditLocal(txMap, transAmt);
//                                        transferTo.setInstType(objLodgementMasterTO.getBillsStatus());
//                                        transferList.add(transferTo);
//                                        if (CommonUtil.convertObjToDouble(objLodgementInstTO.getServiceTx()).doubleValue() > 0) {
//                                            transAmt = CommonUtil.convertObjToDouble(objLodgementInstTO.getServiceTx()).doubleValue();
//                                            txMap.put(TransferTrans.CR_AC_HD, (String) acHeads.get("SERVICE_TAX_HD"));  // credit to Postage Charge account head......
//                                                                                txMap.put(TransferTrans.DR_INST_TYPE, objLodgementMasterTO.getBillsStatus());
//                                            txMap.put(TransferTrans.AUTHORIZE_STATUS_2, "ENTERED_AMOUNT");
//                                            txMap.put("TRANS_MOD_TYPE", "GL");
//                                            transferTo = transactionDAO.addTransferCreditLocal(txMap, transAmt);
//                                            transferTo.setInstType(objLodgementMasterTO.getBillsStatus());
//                                            transferList.add(transferTo);
//                                        }

                                        //INSERT INSTRUCTION
                                        HashMap instMap = new HashMap();
                                        instMap.put("LODGEMENT_ID", objLodgementMasterTO.getLodgementId());
                                        instMap.put("INSTRUCTION", objLodgementInstTO.getInstruction());
                                        List lstInst = sqlMap.executeQueryForList("getBillsExtInst", instMap);
                                        instMap = null;
                                        if (lstInst != null && lstInst.size() > 0) {
                                            sqlMap.executeUpdate("updateLodgementInstructionsTO", objLodgementInstTO);
                                        } else {
                                            sqlMap.executeUpdate("insertLodgementInstructionsTO", objLodgementInstTO);
                                        }
                                        lstInst = null;
                                        status = null;
                                    }
                                }
                                //added by rishad 14/01/2015 for transfer transaction of charge                                   
                                 Double otherBankCharge1 = 0.0;
                                        otherBankCharge1 = CommonUtil.convertObjToDouble(objLodgementRemitTO.getRemitFavouring()).doubleValue();
                                        if (otherBankCharge1 > 0) {
                                            if ((objLodgementRemitTO.getRemitProdId().equals("PO")) || (objLodgementRemitTO.getRemitProdId().equals("DD"))) {
                                                txMap.put(TransferTrans.DR_AC_HD, (String) acHeads.get("BILLS_REALISED_HD"));
                                            } else if (objLodgementRemitTO.getRemitProdId().equals("IBR")) {
                                                System.out.println("acHeads.get(\"IBR_AC_HD\")" + acHeads.get("IBR_AC_HD"));
                                                txMap.put(TransferTrans.DR_AC_HD, (String) acHeads.get("IBR_AC_HD"));
                                            } else if (objLodgementRemitTO.getRemitProdId().equals("OBADV")) {
                                                if (obj.containsKey("OTHER_BANK_HEAD")) {                                                    
                                                    txMap.put(TransferTrans.DR_AC_HD, obj.get("OTHER_BANK_HEAD"));
                                                    //Added By Suresh                                                    
                                                    if (obj.get("OTHER_BANK_PROD_TYPE") != null && obj.get("OTHER_BANK_PROD_TYPE").equals("INV")) {
                                                        InvestmentsTransTO objTO = new InvestmentsTransTO();
                                                        obj.put("INVESTMENT_ACC_NO", CommonUtil.convertObjToStr(obj.get("OTHER_BANK_HEAD")));
                                                        objTO = getInvestmentsTransTO(CommonConstants.TOSTATUS_INSERT, transAmt, obj, obj);
                                                        HashMap achdMap = new HashMap();
                                                        achdMap.put("INVESTMENT_PROD_ID", CommonUtil.convertObjToStr(objTO.getInvestmentID()));
                                                        List achdLst = ServerUtil.executeQuery("getSelectinvestmentAccountHead", achdMap);
                                                        if (achdLst != null && achdLst.size() > 0) {
                                                            achdMap = new HashMap();
                                                            achdMap = (HashMap) achdLst.get(0);
                                                            txMap.put(TransferTrans.DR_AC_HD, acHeads.get("CONT_EXP_BNK_CHRG_DR_HD"));
                                                            acHeads.put("IINVESTMENT_AC_HD", achdMap.get("IINVESTMENT_AC_HD"));
                                                            txMap.put("AUTHORIZEREMARKS", "IINVESTMENT_DEPOSIT");
                                                            //Set Trans ID
                                                            objTO.setBatchID(CommonUtil.convertObjToStr(objLodgementMasterTO.getLodgementId()));
                                                            sqlMap.executeUpdate("insertInvestmentTransTO", objTO);
                                                        } else {
                                                            throw new TTException("Account heads not set properly...");
                                                        }
                                                    }
                                                    System.out.println("obj.get(\"OTHER_BANK_PROD_TYPE\")" + obj.get("OTHER_BANK_PROD_TYPE"));
                                                    if (obj.get("OTHER_BANK_PROD_TYPE") != null && obj.get("OTHER_BANK_PROD_TYPE").equals("AB")) {
                                                        InvestmentsTransTO objTO = new InvestmentsTransTO();
                                                        obj.put("INVESTMENT_ACC_NO", CommonUtil.convertObjToStr(obj.get("OTHER_BANK_HEAD")));
                                                        //objTO = getInvestmentsTransTO(CommonConstants.TOSTATUS_INSERT, transAmt, obj, obj);
                                                        HashMap achdMap = new HashMap();
//                                                achdMap.put("INVESTMENT_PROD_ID", CommonUtil.convertObjToStr(objTO.getInvestmentID()));
//                                                List achdLst=ServerUtil.executeQuery("getSelectOthrBankAcHd1", obj);
                                                        if (coll_Obc_From_Cust!=null && (coll_Obc_From_Cust.equals("N") || coll_Obc_From_Cust.equals("Y"))) {
                                                            //String bankChargeHead=CommonUtil.convertObjToStr(acHeads.get("BANK_CHARGE_HEAD"));
                                                            String bankChargeHead=CommonUtil.convertObjToStr(acHeads.get("DEBIT_BANK_CHRG_HEAD"));
                                                            if(bankChargeHead!=null && !bankChargeHead.equals("")){
                                                                txMap.put(TransferTrans.DR_AC_HD,bankChargeHead);
                                                                txMap.put("AUTHORIZEREMARKS", "BANK_CHARGE_HEAD");
                                                            }
                                                            else {
                                                                throw new TTException("Account heads not set properly...");
                                                            }
                                                        }
                                                        else{
                                                            List achdLst = ServerUtil.executeQuery("getSelectOthrBankAcHd", obj);
                                                            if (achdLst != null && achdLst.size() > 0) {
                                                                achdMap = new HashMap();
                                                                achdMap = (HashMap) achdLst.get(0);
                                                                txMap.put(TransferTrans.DR_AC_HD, acHeads.get("CONT_EXP_BNK_CHRG_DR_HD"));
                                                                acHeads.put("IINVESTMENT_AC_HD", achdMap.get("PRINCIPAL_AC_HD"));
                                                                txMap.put("AUTHORIZEREMARKS", "PRINCIPAL_AC_HD");
                                                                //Set Trans ID
    //                                                    		objTO.setBatchID(CommonUtil.convertObjToStr(objLodgementMasterTO.getLodgementId()));
    //                                                    		sqlMap.executeUpdate("insertInvestmentTransTO", objTO);
    //                                                   		txMap.put(TransferTrans.DR_ACT_NUM,CommonUtil.convertObjToStr(obj.get("OTHER_BANK_HEAD")));
                                                                txMap.put(TransferTrans.DR_ACT_NUM, CommonUtil.convertObjToStr(obj.get("OTHER_BANK_HEAD")));//babu comm 
                                                                //  txMap.put(TransferTrans.DR_ACT_NUM,objLodgementMasterTO.getBorrowAcctNum());
                                                            } else {
                                                                throw new TTException("Account heads not set properly...");
                                                            }
                                                        }
                                                    }
                                                } else {
                                                    txMap.put(TransferTrans.DR_AC_HD, (String) acHeads.get("BILLS_REALISED_HD"));
                                                }
                                            }
                                            txMap.put(TransferTrans.DR_BRANCH, branchID);
                                            txMap.put(TransferTrans.CURRENCY, "INR");                                           
                                            //bbb1
                                             if(coll_Obc_From_Cust.equals("N")){
                                                    txMap.put(TransferTrans.DR_PROD_TYPE, TransactionFactory.GL);
                                                    txMap.put("TRANS_MOD_TYPE", "GL");
                                             }
                                             else{
                                                    txMap.put(TransferTrans.DR_PROD_TYPE, TransactionFactory.GL);
                                                    txMap.put("TRANS_MOD_TYPE", "GL");
                                                    txMap.put(TransferTrans.DR_ACT_NUM, "");
                                              //  }
                                             }
                                            txMap.put(TransferTrans.PARTICULARS, "LodgeID : " + objLodgementMasterTO.getLodgementId() + " Realize");
                                            txMap.put(TransferTrans.AUTHORIZE_STATUS_2, "ENTERED_AMOUNT");     
                                            txMap.put("SCREEN_NAME",(String) obj.get(CommonConstants.SCREEN));// Added by nithya on 23-09-2016 for inserting screen name for transactions
                                            transferTo = transactionDAO.addTransferDebitLocal(txMap, otherBankCharge1);                                           
                                            transferTo.setInstType(objLodgementMasterTO.getBillsStatus());
                                            transferTo.setStatusBy(CommonUtil.convertObjToStr(obj.get("USER_ID")));
                                            transferTo.setInitTransId(CommonUtil.convertObjToStr(obj.get("USER_ID")));
                                            transferList.add(transferTo);
                                            
                                            if ((objLodgementRemitTO.getRemitProdId().equals("PO")) || (objLodgementRemitTO.getRemitProdId().equals("DD"))) {
                                                txMap.put(TransferTrans.CR_AC_HD, (String) acHeads.get("BILLS_REALISED_HD"));
                                            } else if (objLodgementRemitTO.getRemitProdId().equals("IBR")) {
                                                txMap.put(TransferTrans.CR_AC_HD, (String) acHeads.get("IBR_AC_HD"));
                                            } else if (objLodgementRemitTO.getRemitProdId().equals("OBADV")) {
                                                if (obj.containsKey("OTHER_BANK_HEAD")) {                                                    
                                                    txMap.put(TransferTrans.CR_AC_HD, obj.get("OTHER_BANK_HEAD"));
                                                    if ((obj.get("OTHER_BANK_PROD_TYPE") != null && obj.get("OTHER_BANK_PROD_TYPE").equals("INV")) && creditObcHd.equals("Investment A/c Hd")) {
                                                        InvestmentsTransTO objTO = new InvestmentsTransTO();
                                                        obj.put("INVESTMENT_ACC_NO", CommonUtil.convertObjToStr(obj.get("OTHER_BANK_HEAD")));
                                                        objTO = getInvestmentsCreditTransTO(CommonConstants.TOSTATUS_INSERT, otherBankCharge1, obj, obj);
                                                        objTO.setBatchID(CommonUtil.convertObjToStr(objLodgementMasterTO.getLodgementId()));
                                                        sqlMap.executeUpdate("insertInvestmentTransTO", objTO);
                                                        txMap.put(TransferTrans.CR_AC_HD, (String) acHeads.get("IINVESTMENT_AC_HD"));                                                        
                                                    } else if (creditObcHd.equals("Gl A/c Hd")) {
                                                        txMap.put(TransferTrans.CR_AC_HD, (String) acHeads.get("OBC_AC_HD"));
                                                    }
                                                    if (obj.get("OTHER_BANK_PROD_TYPE") != null && obj.get("OTHER_BANK_PROD_TYPE").equals("AB")) {
                                                        InvestmentsTransTO objTO = new InvestmentsTransTO();
                                                        obj.put("INVESTMENT_ACC_NO", CommonUtil.convertObjToStr(obj.get("OTHER_BANK_HEAD")));
                                                        HashMap achdMap = new HashMap();
                                                        List achdLst = ServerUtil.executeQuery("getSelectOthrBankAcHd", obj);
                                                        if (achdLst != null && achdLst.size() > 0) {
                                                            achdMap = new HashMap();
                                                            achdMap = (HashMap) achdLst.get(0);
                                                            txMap.put(TransferTrans.CR_AC_HD, achdMap.get("PRINCIPAL_AC_HD"));
                                                            acHeads.put("IINVESTMENT_AC_HD", achdMap.get("PRINCIPAL_AC_HD"));
                                                            txMap.put("AUTHORIZEREMARKS", "PRINCIPAL_AC_HD");
                                                            System.out.println("achdMap get act_master_id" + achdMap.get("ACT_MASTER_ID"));
                                                            txMap.put(TransferTrans.CR_ACT_NUM, achdMap.get("ACT_MASTER_ID"));
                                                        }                
                                                    }
                                                } else {
                                                    txMap.put(TransferTrans.CR_AC_HD, (String) acHeads.get("BILLS_REALISED_HD"));
                                                }
                                            }
                                             if(coll_Obc_From_Cust.equals("N")){
                                                 if(obj.get("OTHER_BANK_PROD_TYPE")!=null && obj.get("OTHER_BANK_PROD_TYPE").equals("AB")){
                                                      txMap.put(TransferTrans.CR_PROD_TYPE, TransactionFactory.OTHERBANKACTS);  
                                                       txMap.put("TRANS_MOD_TYPE", "AB");
                                                 }
                                            }
                                            else{
                                                 if (obj.get("OTHER_BANK_PROD_TYPE") != null && obj.get("OTHER_BANK_PROD_TYPE").equals("AB")) {
                                                    txMap.put(TransferTrans.CR_PROD_TYPE, TransactionFactory.OTHERBANKACTS);
                                                    txMap.put("TRANS_MOD_TYPE", "AB");
                                                    txMap.put(TransferTrans.CR_ACT_NUM, CommonUtil.convertObjToStr(obj.get("OTHER_BANK_HEAD")));
                                                    txMap.put(TransferTrans.CR_PROD_ID, CommonUtil.convertObjToStr(objLodgementMasterTO.getRecOtherBank()));                                                    
                                                } else {
                                                txMap.put(TransferTrans.CR_PROD_TYPE, TransactionFactory.GL);
                                                txMap.put("TRANS_MOD_TYPE", "GL");
                                                 }
                                             }                                        
                                            txMap.put(TransferTrans.CR_BRANCH, branchID);
                                            txMap.put(TransferTrans.PARTICULARS, objLodgementMasterTO.getLodgementId() + " Realize / Comm-"  + " / OBC-" + objLodgementRemitTO.getRemitFavouring());
                                            txMap.put(TransferTrans.AUTHORIZE_STATUS_2, "ENTERED_AMOUNT");//                  
                                            txMap.put("SCREEN_NAME",(String) obj.get(CommonConstants.SCREEN));// Added by nithya on 23-09-2016 for inserting screen name for transactions
                                            transferTo = transactionDAO.addTransferCreditLocal(txMap, otherBankCharge1);
                                            transferTo.setInstType(objLodgementMasterTO.getBillsStatus());
                                            transferTo.setStatusBy(CommonUtil.convertObjToStr(obj.get("USER_ID")));
                                            transferTo.setInitTransId(CommonUtil.convertObjToStr(obj.get("USER_ID")));
                                            transferList.add(transferTo);
                                        }
                                           if (coll_Obc_From_Cust.equals("Y")) {
                                            double otherBankCharge = 0.0;
                                            double totalCommisionCharge = 0.0;
                                            otherBankCharge = CommonUtil.convertObjToDouble(objLodgementRemitTO.getRemitFavouring()).doubleValue();  //Only Charge Amount
                                            totalCommisionCharge = otherBankCharge+ commTot ;  // commTot -> Toatal Commision Amount
                                            if (totalCommisionCharge > 0) {
                                                txMap.put(TransferTrans.CR_AC_HD, (String) acHeads.get("MISC_INC_BNK_CHRG_CR_HD"));  // credit to Postage Charge account head......
                                                txMap.put(TransferTrans.PARTICULARS, "BankCharges LodgeID : " + objLodgementMasterTO.getLodgementId() + " Realize");
                                                System.out.println("transAmt 2 cr" + totalCommisionCharge);
                                                if (obj.get("OTHER_BANK_PROD_TYPE") != null && obj.get("OTHER_BANK_PROD_TYPE").equals("AB")) {
                                                    txMap.put(TransferTrans.CR_ACT_NUM, null);
                                                }
                                                txMap.put(TransferTrans.AUTHORIZE_STATUS_2, "ENTERED_AMOUNT");
                                                txMap.put("TRANS_MOD_TYPE", "GL");
                                                txMap.put(TransferTrans.CR_PROD_TYPE, TransactionFactory.GL);
                                                txMap.put("SCREEN_NAME",(String) obj.get(CommonConstants.SCREEN));// Added by nithya on 23-09-2016 for inserting screen name for transactions
                                                transferTo = transactionDAO.addTransferCreditLocal(txMap, totalCommisionCharge);
                                                transferTo.setInstType(objLodgementMasterTO.getBillsStatus());
                                                transferTo.setStatusBy(CommonUtil.convertObjToStr(obj.get("USER_ID")));
                                                transferTo.setInitTransId(CommonUtil.convertObjToStr(obj.get("USER_ID")));
                                                transferList.add(transferTo);
                                            }                                           
                                        }
                                        if (coll_Obc_From_Cust.equals("Y")) {
                                            double otherBankCharge = 0.0;
                                            double totalCommisionCharge = 0.0;
                                            double serTaxAmt =0.0;
                                            if (obj.containsKey("serviceTaxDetails")) {
                                                HashMap serTaxMap = (HashMap) obj.get("serviceTaxDetails");
                                                 serTaxAmt = CommonUtil.convertObjToDouble(serTaxMap.get("TOT_TAX_AMT"));
                                            }
                                            otherBankCharge = CommonUtil.convertObjToDouble(objLodgementRemitTO.getRemitFavouring()).doubleValue();  //Only Charge Amount
                                            totalCommisionCharge = otherBankCharge + commTot+serTaxAmt;  // commTot -> Toatal Commision Amount
                                            if (totalCommisionCharge > 0) {                                              
                                                if (!objLodgementMasterTO.getProdType().equals("GL")) {
                                                    txMap.put(TransferTrans.DR_AC_HD, (String) acHeadMap.get("AC_HEAD"));
                                                } else {
                                                    txMap.put(TransferTrans.DR_AC_HD, objLodgementMasterTO.getBorrowAcctNum());
                                                }
                                                txMap.put(TransferTrans.DR_ACT_NUM, objLodgementMasterTO.getBorrowAcctNum());
                                                txMap.put(TransferTrans.DR_BRANCH, branchID);
                                                txMap.put(TransferTrans.CURRENCY, "INR");
                                                txMap.put(TransferTrans.DR_PROD_TYPE, objLodgementMasterTO.getProdType());
                                                txMap.put("TRANS_MOD_TYPE", objLodgementMasterTO.getProdType());
                                                txMap.put(TransferTrans.DR_PROD_ID, objLodgementMasterTO.getBorrowProdId());
                                                txMap.put(TransferTrans.PARTICULARS, "LodgeID : " + objLodgementMasterTO.getLodgementId() + " Realize");
                                                txMap.put(TransferTrans.AUTHORIZE_STATUS_2, "ENTERED_AMOUNT");
                                                txMap.put("SCREEN_NAME",(String) obj.get(CommonConstants.SCREEN));// Added by nithya on 23-09-2016 for inserting screen name for transactions
                                                transferTo = transactionDAO.addTransferDebitLocal(txMap, totalCommisionCharge);
                                                transferTo.setInstType(objLodgementMasterTO.getBillsStatus());
                                                transferTo.setStatusBy(CommonUtil.convertObjToStr(obj.get("USER_ID")));
                                                transferTo.setInitTransId(CommonUtil.convertObjToStr(obj.get("USER_ID")));
                                                transferList.add(transferTo);
                                                System.out.println("4 Debit");
                                            }
                                        }     
                                //end
                                transAmt = 0;
                                transactionDAO.setTransactionType(transactionDAO.TRANSFER);
                                transactionDAO.doTransferLocal(transferList, branchID);
                                if (temp > 0) {
                                    transAmt = temp;
                                    txMap = new HashMap();
                                    TransactionTO transTO = new TransactionTO();
                                    ArrayList cashList = new ArrayList();
                                    transactionDAO.setCommandMode(CommonConstants.TOSTATUS_INSERT);
                                    //added by rishad 15/01/2015
                                     if ((objLodgementRemitTO.getRemitProdId().equals("PO")) || (objLodgementRemitTO.getRemitProdId().equals("DD"))) {
                                            txMap.put(TransferTrans.DR_AC_HD, (String) acHeads.get("BILLS_REALISED_HD"));
                                        } else if (objLodgementRemitTO.getRemitProdId().equals("IBR")) {
                                            txMap.put(TransferTrans.DR_AC_HD, (String) acHeads.get("IBR_AC_HD"));
                                        } else if (objLodgementRemitTO.getRemitProdId().equals("OBADV")) {
                                            if (obj.containsKey("OTHER_BANK_HEAD")) {
                                                txMap.put(TransferTrans.DR_AC_HD, obj.get("OTHER_BANK_HEAD"));
                                                if (obj.get("OTHER_BANK_PROD_TYPE") != null && obj.get("OTHER_BANK_PROD_TYPE").equals("INV")) {
                                                    InvestmentsTransTO objTO = new InvestmentsTransTO();
                                                    obj.put("INVESTMENT_ACC_NO", CommonUtil.convertObjToStr(obj.get("OTHER_BANK_HEAD")));
                                                    objTO = getInvestmentsTransTO(CommonConstants.TOSTATUS_INSERT, transAmt, obj, obj);
                                                    HashMap achdMap = new HashMap();
                                                    achdMap.put("INVESTMENT_PROD_ID", CommonUtil.convertObjToStr(objTO.getInvestmentID()));
                                                    List achdLst = ServerUtil.executeQuery("getSelectinvestmentAccountHead", achdMap);
                                                    if (achdLst != null && achdLst.size() > 0) {
                                                        achdMap = new HashMap();
                                                        achdMap = (HashMap) achdLst.get(0);
                                                        txMap.put(TransferTrans.DR_AC_HD, achdMap.get("IINVESTMENT_AC_HD"));
                                                        acHeads.put("IINVESTMENT_AC_HD", achdMap.get("IINVESTMENT_AC_HD"));
                                                        txMap.put("AUTHORIZEREMARKS", "IINVESTMENT_DEPOSIT");
                                                        //Set Trans ID
                                                        txMap.put(TransferTrans.AUTHORIZE_STATUS_2, "ENTERED_AMOUNT");
                                                        txMap.put(TransferTrans.DR_ACT_NUM, objLodgementMasterTO.getBorrowAcctNum());
                                                        objTO.setBatchID(CommonUtil.convertObjToStr(objLodgementMasterTO.getLodgementId()));
                                                        sqlMap.executeUpdate("insertInvestmentTransTO", objTO);
                                                    } else {
                                                        throw new TTException("Account heads not set properly...");
                                                    }
                                                }
                                                if (obj.get("OTHER_BANK_PROD_TYPE") != null && obj.get("OTHER_BANK_PROD_TYPE").equals("AB")) {
                                                    InvestmentsTransTO objTO = new InvestmentsTransTO();
                                                    obj.put("INVESTMENT_ACC_NO", CommonUtil.convertObjToStr(obj.get("OTHER_BANK_HEAD")));
                                                    HashMap achdMap = new HashMap();
                                                    List achdLst = ServerUtil.executeQuery("getSelectOthrBankAcHd", obj);
                                                    if (achdLst != null && achdLst.size() > 0) {
                                                        achdMap = new HashMap();
                                                        achdMap = (HashMap) achdLst.get(0);
                                                        txMap.put(TransferTrans.DR_AC_HD, achdMap.get("PRINCIPAL_AC_HD"));
                                                        acHeads.put("IINVESTMENT_AC_HD", achdMap.get("PRINCIPAL_AC_HD"));
                                                        txMap.put("AUTHORIZEREMARKS", "PRINCIPAL_AC_HD");
                                                        txMap.put(TransferTrans.DR_ACT_NUM, CommonUtil.convertObjToStr(obj.get("OTHER_BANK_HEAD")));
                                                        txMap.put(TransferTrans.AUTHORIZE_STATUS_2, "ENTERED_AMOUNT");
                                                    } else {
                                                        throw new TTException("Account heads not set properly...");
                                                    }
                                                }
                                            } else {
                                                txMap.put(TransferTrans.DR_AC_HD, (String) acHeads.get("BILLS_REALISED_HD"));
                                            }
                                        }
                                        txMap.put(TransferTrans.DR_BRANCH, branchID);
                                        txMap.put(TransferTrans.CURRENCY, "INR");
                                        //bbb1
                                        if (obj.get("OTHER_BANK_PROD_TYPE") != null && obj.get("OTHER_BANK_PROD_TYPE").equals("AB")) {
                                            txMap.put(TransferTrans.DR_PROD_TYPE, TransactionFactory.OTHERBANKACTS);
                                            txMap.put("TRANS_MOD_TYPE", "AB");
                                            txMap.put(TransferTrans.DR_PROD_ID, CommonUtil.convertObjToStr(objLodgementMasterTO.getRecOtherBank()));
                                        } else {
                                            txMap.put(TransferTrans.DR_PROD_TYPE, TransactionFactory.GL);
                                            txMap.put("TRANS_MOD_TYPE", "GL");
                                        }
                                    //end
                                    txMap.put(TransferTrans.DR_BRANCH, branchID);
                                    txMap.put(TransferTrans.CURRENCY, "INR");
                                    //txMap.put(TransferTrans.DR_PROD_TYPE, TransactionFactory.GL);
                                    txMap.put(TransferTrans.PARTICULARS, "VPL Chrg LodgeID : " + objLodgementMasterTO.getLodgementId() + " Dishonour");
                                    txMap.put(TransferTrans.AUTHORIZE_STATUS_2, "ENTERED_AMOUNT");
                                    transactionDAO.setTransType(CommonConstants.CREDIT);
                                    otherBankCharge1=CommonUtil.convertObjToDouble(objLodgementRemitTO.getRemitFavouring());
                                   // transAmt+=otherBankCharge1;
                                  //  transAmt+=totalCommisionCharge;
                                    double transAmt1=CommonUtil.convertObjToDouble(objLodgementRemitTO.getRemitInstAmt())+otherBankCharge1;
                                    txMap.put("SCREEN_NAME",(String) obj.get(CommonConstants.SCREEN));// Added by nithya on 23-09-2016 for inserting screen name for transactions
                                    transactionDAO.addTransferDebit(txMap, transAmt1);
                                    transactionDAO.deleteTxList();
                                    transTO.setCommand(CommonConstants.TOSTATUS_INSERT);
                                    transTO.setTransType("CASH");
                                    transTO.setTransAmt(new Double(transAmt1));
                                    transTO.setBatchId(objLodgementMasterTO.getLodgementId());
                                    transTO.setInstType(objLodgementMasterTO.getBillsStatus());
                                    cashList.add(transTO);                                   
                                    transactionDAO.addCashList(cashList);
                                    transactionDAO.doTransfer();
                                    temp = 0;
                                }
                                //OVERDUE INTEREST CODING
                                if ((CommonUtil.convertObjToDouble(objLodgementMasterTO.getRateForDelay()).doubleValue()) > 0) {
                                    HashMap overDue = new HashMap();
                                    overDue.put("LODGEMENT_ID", CommonUtil.convertObjToStr(objLodgementMasterTO.getLodgementId()));
                                    overDue.put("CUR_DATE", currDt.clone());
                                    List dueLst = sqlMap.executeQueryForList("getBillsOverDueIntDetails", overDue);
                                    overDue = null;
                                    if (dueLst != null && dueLst.size() > 0) {
                                        overDue = (HashMap) dueLst.get(0);
                                        double amount = CommonUtil.convertObjToDouble(objLodgementChequeTO.getInstrumentAmount()).doubleValue();
                                        double rateOfInterest = CommonUtil.convertObjToDouble(objLodgementMasterTO.getRateForDelay()).doubleValue();
                                        double period = CommonUtil.convertObjToDouble(overDue.get("INT_PERIOD")).doubleValue();
                                        double yearDays = 36500;
                                        double intAmount = (amount * rateOfInterest * period) / yearDays;
                                        System.out.println("!!!!!!!!amount" + amount + "#####rateOfInterest" + rateOfInterest + "!!!!!!!!!period" + period + "####intAmount" + intAmount);
                                        intAmount = (double) getNearest((long) (intAmount * 100), 100) / 100;
                                        System.out.println("####intAmount" + intAmount);
                                        if (intAmount > 0) {
                                            //transaction Coding
                                            transAmt = intAmount;
                                            txMap = new HashMap();
                                            TransactionTO transTO = new TransactionTO();
                                            ArrayList cashList = new ArrayList();
                                            transactionDAO.setCommandMode(CommonConstants.TOSTATUS_INSERT);
                                            txMap.put(TransferTrans.DR_AC_HD, (String) acHeads.get("INT_AC_HD"));
                                            txMap.put(TransferTrans.DR_BRANCH, branchID);
                                            txMap.put(TransferTrans.CURRENCY, "INR");
                                            txMap.put(TransferTrans.DR_PROD_TYPE, TransactionFactory.GL);
                                            txMap.put("TRANS_MOD_TYPE","GL");
                                            txMap.put(TransferTrans.PARTICULARS, "VPL Chrg LodgeID : " + objLodgementMasterTO.getLodgementId() + " Dishonour");
                                            txMap.put(TransferTrans.AUTHORIZE_STATUS_2, "ENTERED_AMOUNT");
                                            txMap.put("SCREEN_NAME",(String) obj.get(CommonConstants.SCREEN));// Added by nithya on 23-09-2016 for inserting screen name for transactions
                                            transactionDAO.setTransType(CommonConstants.CREDIT);
                                            transactionDAO.addTransferDebit(txMap, transAmt);
                                            transactionDAO.deleteTxList();
                                            transTO.setCommand(CommonConstants.TOSTATUS_INSERT);
                                            transTO.setTransType("CASH");
                                            transTO.setTransAmt(new Double(transAmt));
                                            transTO.setBatchId(objLodgementMasterTO.getLodgementId());
                                            transTO.setInstType(objLodgementMasterTO.getBillsStatus());
                                            cashList.add(transTO);                                            
                                            transactionDAO.addCashList(cashList);
                                            transactionDAO.doTransfer();
                                        }
                                    }
                                }
                            }
                        }
                    }
                            //start
                    else if (objLodgementMasterTO.getBillsStatus().equals("DISHONOUR")) {
                        if (objLodgementMasterTO.getTranType().equals("TRANSFER")) {
                            if (CommonUtil.convertObjToDouble(objLodgementRemitTO.getRemitFavouring()).doubleValue() > 0) {
                                txMap = new HashMap();
                                transactionDAO.setCommandMode(CommonConstants.TOSTATUS_INSERT);  
                                String brnchName = objLodgementMasterTO.getDraweeName();
                                List list = sqlMap.executeQueryForList("getOtherBankBranchDetailsForDishonour", brnchName);
                                String obcProdType = "";
                                if (list != null && list.size() > 0) {
                                    HashMap hmap = (HashMap) list.get(0);
                                    obcProdType = CommonUtil.convertObjToStr(hmap.get("PROD_TYPE"));
                                }
                                if (obcProdType.equals("INV")) {
                                    if (obj.containsKey("OTHER_BANK_HEAD")) {
                                        txMap.put(TransferTrans.DR_AC_HD, obj.get("OTHER_BANK_HEAD"));
                                        transAmt = CommonUtil.convertObjToDouble(objLodgementRemitTO.getRemitFavouring()).doubleValue();
                                        if (transAmt > 0) {
                                            if (obj.get("OTHER_BANK_PROD_TYPE") != null && obj.get("OTHER_BANK_PROD_TYPE").equals("INV")) {
                                                InvestmentsTransTO objTO = new InvestmentsTransTO();
                                                obj.put("INVESTMENT_ACC_NO", CommonUtil.convertObjToStr(obj.get("OTHER_BANK_HEAD")));
                                                objTO = getInvestmentsCreditTransTO(CommonConstants.TOSTATUS_INSERT, transAmt, obj, obj);
                                                HashMap achdMap = new HashMap();
                                                achdMap.put("INVESTMENT_PROD_ID", CommonUtil.convertObjToStr(objTO.getInvestmentID()));
                                                List achdLst = ServerUtil.executeQuery("getSelectinvestmentAccountHead", achdMap);
                                                if (achdLst != null && achdLst.size() > 0) {
                                                    achdMap = new HashMap();
                                                    achdMap = (HashMap) achdLst.get(0);
                                                    txMap.put(TransferTrans.DR_AC_HD, achdMap.get("IINVESTMENT_AC_HD"));
                                                    acHeads.put("IINVESTMENT_AC_HD", achdMap.get("IINVESTMENT_AC_HD"));
                                                    txMap.put("AUTHORIZEREMARKS", "IINVESTMENT_DEPOSIT");
                                                    //Set Trans ID
                                                    objTO.setBatchID(CommonUtil.convertObjToStr(objLodgementMasterTO.getLodgementId()));
                                                    sqlMap.executeUpdate("insertInvestmentTransTO", objTO);
                                                } else {
                                                    throw new TTException("Account heads not set properly...");
                                                }
                                            }
                                            //added by jjjj for other bank charges in dishonour
                                        }
                                    }
                                    ////56 dr and cr
                                    if (!objLodgementMasterTO.getProdType().equals("GL")) {
                                        System.out.println("Not GL");
                                        txMap.put(TransferTrans.DR_AC_HD, (String) acHeads.get("CONT_EXP_BNK_CHRG_DR_HD"));
                                        txMap.put(TransferTrans.DR_ACT_NUM, "");
                                        txMap.put(TransferTrans.DR_PROD_ID, "");
                                    } else {
                                        System.out.println("else if Gl");
                                        txMap.put(TransferTrans.DR_AC_HD, objLodgementMasterTO.getBorrowAcctNum());
                                    }

                                    ////for othr bank chrg
                                    txMap.put(TransferTrans.DR_BRANCH, branchID);
                                    txMap.put(TransferTrans.CURRENCY, "INR");
                                    txMap.put(TransferTrans.DR_PROD_TYPE, TransactionFactory.GL);
                                    txMap.put("TRANS_MOD_TYPE", "GL");
                                    txMap.put(TransferTrans.PARTICULARS, "VPL Chrg LodgeID : " + objLodgementMasterTO.getLodgementId() + " Dishonour");
                                    txMap.put(TransferTrans.CR_AC_HD, (String) acHeadMap1.get("PRINCIPAL_AC_HD"));
                                    txMap.put(TransferTrans.CR_BRANCH, branchID);
                                    txMap.put(TransferTrans.CR_PROD_TYPE, TransactionFactory.GL);
                                    txMap.put("TRANS_MOD_TYPE", "GL");
                                    txMap.put(TransferTrans.AUTHORIZE_STATUS_2, "ENTERED_AMOUNT");
                                    transAmt = CommonUtil.convertObjToDouble(objLodgementRemitTO.getRemitFavouring()).doubleValue();
                                    System.out.println("transAmt" + transAmt + "nnnn" + acHeads.get("CONT_EXP_BNK_CHRG_DR_HD"));
                                    txMap.put(TransferTrans.PARTICULARS, objLodgementMasterTO.getBorrowProdId());
                                    txMap.put("SCREEN_NAME",(String) obj.get(CommonConstants.SCREEN));// Added by nithya on 23-09-2016 for inserting screen name for transactions
                                    transferTo = transactionDAO.addTransferDebitLocal(txMap, transAmt);
                                    transferTo.setInstType(objLodgementMasterTO.getBillsStatus());
                                    transferList.add(transferTo);
                                    txMap.put(TransferTrans.PARTICULARS, "A/c No-" + " " + objLodgementMasterTO.getBorrowAcctNum());
                                    txMap.put(TransferTrans.PARTICULARS, objLodgementMasterTO.getLodgementId() + " Dishonour / Comm-" + " / OBC-" + objLodgementRemitTO.getRemitFavouring());
                                    transAmt = CommonUtil.convertObjToDouble(objLodgementRemitTO.getRemitFavouring()).doubleValue();
                                    txMap.put(TransferTrans.AUTHORIZE_STATUS_2, "ENTERED_AMOUNT");
                                    txMap.put("SCREEN_NAME",(String) obj.get(CommonConstants.SCREEN));// Added by nithya on 23-09-2016 for inserting screen name for transactions
                                    transferTo = transactionDAO.addTransferCreditLocal(txMap, transAmt);
                                    transferTo.setInstType(objLodgementMasterTO.getBillsStatus());
                                    transferList.add(transferTo);
                                    transAmt = 0;
                                    ///66 dr and cr
                                    if (!objLodgementMasterTO.getProdType().equals("GL")) {
                                        System.out.println("Not GL");
                                        txMap.put(TransferTrans.DR_AC_HD, (String) acHeadMap.get("AC_HEAD"));
                                        txMap.put(TransferTrans.DR_ACT_NUM, objLodgementMasterTO.getBorrowAcctNum());
                                        txMap.put(TransferTrans.DR_PROD_ID, objLodgementMasterTO.getBorrowProdId());
                                    } else {
                                        txMap.put(TransferTrans.DR_AC_HD, objLodgementMasterTO.getBorrowAcctNum());
                                    }
                                    txMap.put(TransferTrans.DR_PROD_TYPE, objLodgementMasterTO.getProdType());
                                    txMap.put("TRANS_MOD_TYPE", objLodgementMasterTO.getProdType());
                                    txMap.put(TransferTrans.PARTICULARS, "VPL Chrg LodgeID : " + objLodgementMasterTO.getLodgementId() + " Dishonour");   
                                    txMap.put(TransferTrans.DR_BRANCH, branchID);
                                    double totAmountInst = 0.0;
                                    if (instructionList != null) {
                                        if (instructionList.size() != 0) {
                                            for (int i = 0; i < instructionList.size(); i++) {
                                                LodgementInstructionsTO objLodgementInstructionsTO = (LodgementInstructionsTO) instructionList.get(i);
                                                if (objLodgementInstructionsTO.getAmount() != null) {
                                                    totAmountInst = totAmountInst + (objLodgementInstructionsTO.getAmount());
                                                }
                                            }
                                        }
                                    }
                                    transAmt = CommonUtil.convertObjToDouble(objLodgementRemitTO.getRemitFavouring()).doubleValue() + totAmountInst;
                                    double swachhCess = 0.0;
                                    double krishikalyanCess = 0.0;
                                    double normalServiceTax = 0.0;                                             
                                    double serTaxAmt = 0;
                                    double transDebAmt = transAmt;
                                    HashMap serTaxMap = new HashMap();
                                    if (obj.containsKey("serviceTaxDetails")) {
                                        serTaxMap = (HashMap) obj.get("serviceTaxDetails");
                                        if (serTaxMap != null && serTaxMap.size() > 0) {
                                            if (serTaxMap.containsKey("TOT_TAX_AMT") && serTaxMap.get("TOT_TAX_AMT") != null) {
                                                serTaxAmt = CommonUtil.convertObjToDouble(serTaxMap.get("TOT_TAX_AMT"));
                                            }
                                            if (serTaxMap.containsKey(ServiceTaxCalculation.SWACHH_CESS) && serTaxMap.get(ServiceTaxCalculation.SWACHH_CESS) != null) {
                                                swachhCess = CommonUtil.convertObjToDouble(serTaxMap.get(ServiceTaxCalculation.SWACHH_CESS));
                                            }
                                            if (serTaxMap.containsKey(ServiceTaxCalculation.KRISHIKALYAN_CESS) && serTaxMap.get(ServiceTaxCalculation.KRISHIKALYAN_CESS) != null) {
                                                krishikalyanCess = CommonUtil.convertObjToDouble(serTaxMap.get(ServiceTaxCalculation.KRISHIKALYAN_CESS));
                                            }
                                            if (serTaxMap.containsKey(ServiceTaxCalculation.SERVICE_TAX) && serTaxMap.get(ServiceTaxCalculation.SERVICE_TAX) != null) {
                                                      normalServiceTax = CommonUtil.convertObjToDouble(serTaxMap.get(ServiceTaxCalculation.SERVICE_TAX));
                                            }
                                          //  normalServiceTax -= (swachhCess + krishikalyanCess);
                                        }                                        
                                        transDebAmt = transDebAmt + serTaxAmt;
                                    }
                                    txMap.put(TransferTrans.AUTHORIZE_STATUS_2, "ENTERED_AMOUNT");
                                    txMap.put(TransferTrans.PARTICULARS, objLodgementMasterTO.getBorrowProdId());
                                    txMap.put("SCREEN_NAME",(String) obj.get(CommonConstants.SCREEN));// Added by nithya on 23-09-2016 for inserting screen name for transactions
                                    transferTo = transactionDAO.addTransferDebitLocal(txMap, transDebAmt);
                                    transferTo.setInstType(objLodgementMasterTO.getBillsStatus());
                                    transferList.add(transferTo);
                                    txMap.put(TransferTrans.PARTICULARS, "A/c No-" + " " + objLodgementMasterTO.getBorrowAcctNum());
                                    txMap.put(TransferTrans.AUTHORIZE_STATUS_2, "ENTERED_AMOUNT");
                                    txMap.put(TransferTrans.PARTICULARS, "VPL Chrg LodgeID : " + objLodgementMasterTO.getLodgementId() + " Dishonour");
                                    txMap.put(TransferTrans.CR_AC_HD, (String) acHeads.get("MISC_INC_BNK_CHRG_CR_HD"));
                                    transAmt = CommonUtil.convertObjToDouble(objLodgementRemitTO.getRemitFavouring()).doubleValue() + totAmountInst;
                                    txMap.put(TransferTrans.CR_PROD_TYPE, TransactionFactory.GL);
                                    txMap.put("TRANS_MOD_TYPE", "GL");
                                    transactionDAO.setTransType(CommonConstants.DEBIT);
                                    txMap.put("SCREEN_NAME",(String) obj.get(CommonConstants.SCREEN));// Added by nithya on 23-09-2016 for inserting screen name for transactions
                                    transferTo = transactionDAO.addTransferCreditLocal(txMap, transAmt);
                                    transferTo.setInstType(objLodgementMasterTO.getBillsStatus());
                                    transferList.add(transferTo);
                                    transAmt = 0;
                                    if (swachhCess > 0) { 
                                        txMap.put(TransferTrans.CR_AC_HD, CommonUtil.convertObjToStr(serTaxMap.get("SWACHH_HEAD_ID")));  // credit to Postage Charge account head......
                                        txMap.put(TransferTrans.PARTICULARS, "CGST : " + objLodgementMasterTO.getLodgementId() + " Realize");
                                        if (obj.get("OTHER_BANK_PROD_TYPE") != null && obj.get("OTHER_BANK_PROD_TYPE").equals(TransactionFactory.OTHERBANKACTS)) {
                                            txMap.put(TransferTrans.CR_ACT_NUM, null);
                                        }
                                        txMap.put(TransferTrans.AUTHORIZE_STATUS_2, "ENTERED_AMOUNT");
                                        txMap.put("TRANS_MOD_TYPE", TransactionFactory.GL);
                                        txMap.put(TransferTrans.CR_PROD_TYPE, TransactionFactory.GL);
                                        txMap.put("SCREEN_NAME", (String) obj.get(CommonConstants.SCREEN));// Added by nithya on 23-09-2016 for inserting screen name for transactions
                                        transferTo = transactionDAO.addTransferCreditLocal(txMap, swachhCess);
                                        transferTo.setInstType(objLodgementMasterTO.getBillsStatus());
                                        transferTo.setStatusBy(CommonUtil.convertObjToStr(obj.get("USER_ID")));
                                        transferTo.setInitTransId(CommonUtil.convertObjToStr(obj.get("USER_ID")));
                                        transferList.add(transferTo);
                                        //normalServiceTax -= swachhCess;
                                    }
                                    if (krishikalyanCess > 0) {
                                        txMap.put(TransferTrans.CR_AC_HD, CommonUtil.convertObjToStr(serTaxMap.get("KRISHIKALYAN_HEAD_ID")));  // credit to Postage Charge account head......
                                        txMap.put(TransferTrans.PARTICULARS, "SGST : " + objLodgementMasterTO.getLodgementId() + " Realize");
                                        if (obj.get("OTHER_BANK_PROD_TYPE") != null && obj.get("OTHER_BANK_PROD_TYPE").equals(TransactionFactory.OTHERBANKACTS)) {
                                            txMap.put(TransferTrans.CR_ACT_NUM, null);
                                        }
                                        txMap.put(TransferTrans.AUTHORIZE_STATUS_2, "ENTERED_AMOUNT");
                                        txMap.put("TRANS_MOD_TYPE", TransactionFactory.GL);
                                        txMap.put(TransferTrans.CR_PROD_TYPE, TransactionFactory.GL);
                                        txMap.put("SCREEN_NAME", (String) obj.get(CommonConstants.SCREEN));// Added by nithya on 23-09-2016 for inserting screen name for transactions
                                        transferTo = transactionDAO.addTransferCreditLocal(txMap, krishikalyanCess);
                                        transferTo.setInstType(objLodgementMasterTO.getBillsStatus());
                                        transferTo.setStatusBy(CommonUtil.convertObjToStr(obj.get("USER_ID")));
                                        transferTo.setInitTransId(CommonUtil.convertObjToStr(obj.get("USER_ID")));
                                        transferList.add(transferTo);
                                       // normalServiceTax -= krishikalyanCess;
                                    }
                                    if (normalServiceTax > 0) {
                                        txMap.put(TransferTrans.CR_AC_HD, CommonUtil.convertObjToStr(serTaxMap.get("TAX_HEAD_ID")));  // credit to Postage Charge account head......
                                        txMap.put(TransferTrans.PARTICULARS, "SERVICE TAX for BankCharges LodgeID : " + objLodgementMasterTO.getLodgementId() + " Realize");
                                        if (obj.get("OTHER_BANK_PROD_TYPE") != null && obj.get("OTHER_BANK_PROD_TYPE").equals(TransactionFactory.OTHERBANKACTS)) {
                                            txMap.put(TransferTrans.CR_ACT_NUM, null);
                                        }
                                        txMap.put(TransferTrans.AUTHORIZE_STATUS_2, "ENTERED_AMOUNT");
                                        txMap.put("TRANS_MOD_TYPE", TransactionFactory.GL);
                                        txMap.put(TransferTrans.CR_PROD_TYPE, TransactionFactory.GL);
                                        txMap.put("SCREEN_NAME",(String) obj.get(CommonConstants.SCREEN));// Added by nithya on 23-09-2016 for inserting screen name for transactions
                                        transferTo = transactionDAO.addTransferCreditLocal(txMap, normalServiceTax);
                                        transferTo.setInstType(objLodgementMasterTO.getBillsStatus());
                                        transferTo.setStatusBy(CommonUtil.convertObjToStr(obj.get("USER_ID")));
                                        transferTo.setInitTransId(CommonUtil.convertObjToStr(obj.get("USER_ID")));
                                        transferList.add(transferTo);
                                    }
                                    /////////
                                    transactionDAO.setTransactionType(transactionDAO.TRANSFER);
                                    transactionDAO.doTransferLocal(transferList, branchID);
                                    transactionDAO.doTransfer();
                                    //  System.out.println("vvvv");
                                } else {
                                    //for OTHER BANKS
                                    if (!objLodgementMasterTO.getProdType().equals("GL")) {
                                        txMap.put(TransferTrans.DR_AC_HD, (String) acHeads.get("CONT_EXP_BNK_CHRG_DR_HD"));
                                        txMap.put(TransferTrans.DR_ACT_NUM, "");
                                        txMap.put(TransferTrans.DR_PROD_ID, "");
                                    } else {
                                        txMap.put(TransferTrans.DR_AC_HD, objLodgementMasterTO.getBorrowAcctNum());
                                    }
                                    txMap.put(TransferTrans.DR_BRANCH, branchID);
                                    txMap.put(TransferTrans.CURRENCY, "INR");
                                    txMap.put(TransferTrans.DR_PROD_TYPE,TransactionFactory.GL);
                                    txMap.put("TRANS_MOD_TYPE", "GL");
                                    txMap.put(TransferTrans.PARTICULARS, "VPL Chrg LodgeID : " + objLodgementMasterTO.getLodgementId() + " Dishonour");
                                    txMap.put(TransferTrans.AUTHORIZE_STATUS_2, "ENTERED_AMOUNT");
                                    if (obcProdType.equals("AB")) {
                                        obj.put("INVESTMENT_ACC_NO", CommonUtil.convertObjToStr(obj.get("OTHER_BANK_HEAD")));
                                        HashMap achdMap = new HashMap();
                                        List achdLst = ServerUtil.executeQuery("getSelectOthrBankAcHd", obj);                                        
                                        if (achdLst != null && achdLst.size() > 0) {
                                            achdMap = new HashMap();
                                            achdMap = (HashMap) achdLst.get(0);
                                            txMap.put("AUTHORIZEREMARKS", "DISHONUR");
                                            txMap.put(TransferTrans.CR_ACT_NUM, CommonUtil.convertObjToStr(obj.get("OTHER_BANK_HEAD")));
                                            txMap.put(TransferTrans.CR_PROD_ID, "AB");
                                        }
                                    }
                                    txMap.put(TransferTrans.CR_AC_HD, (String) acHeadMap1.get("PRINCIPAL_AC_HD"));
                                    txMap.put(TransferTrans.CR_BRANCH, branchID);
                                    txMap.put(TransferTrans.CR_PROD_TYPE, TransactionFactory.OTHERBANKACTS);
                                    //txMap.put("TRANS_MOD_TYPE", "AB");
                                    txMap.put("TRANS_MOD_TYPE", TransactionFactory.GL); //07-01-2020 for KD-2589
                                    transAmt = CommonUtil.convertObjToDouble(objLodgementRemitTO.getRemitFavouring()).doubleValue();
                                    System.out.println("transAmttransAmt" + transAmt);
                                    txMap.put(TransferTrans.PARTICULARS, objLodgementMasterTO.getBorrowProdId());
                                    txMap.put("SCREEN_NAME",(String) obj.get(CommonConstants.SCREEN));// Added by nithya on 23-09-2016 for inserting screen name for transactions
                                    transferTo = transactionDAO.addTransferDebitLocal(txMap, transAmt);
                                    transferTo.setInstType(objLodgementMasterTO.getBillsStatus());
                                    transferList.add(transferTo);
                                    txMap.put(TransferTrans.PARTICULARS, "A/c No-" + " " + objLodgementMasterTO.getBorrowAcctNum());
                                    txMap.put(TransferTrans.PARTICULARS, "VPL Chrg LodgeID : " + objLodgementMasterTO.getLodgementId() + " Dishonour");
                                    transAmt = CommonUtil.convertObjToDouble(objLodgementRemitTO.getRemitFavouring()).doubleValue();
                                    txMap.put(TransferTrans.AUTHORIZE_STATUS_2, "ENTERED_AMOUNT");
                                    txMap.put("SCREEN_NAME",(String) obj.get(CommonConstants.SCREEN));// Added by nithya on 23-09-2016 for inserting screen name for transactions
                                    txMap.put("TRANS_MOD_TYPE", "AB"); //07-01-2020 for KD-2589
                                    transferTo = transactionDAO.addTransferCreditLocal(txMap, transAmt);
                                    transferTo.setInstType(objLodgementMasterTO.getBillsStatus());
                                    transferList.add(transferTo);
                                    transAmt = 0;                                   
                                    double totAmountInst = 0.0;
                                    System.out.println("llll" + instructionList);
                                    if (instructionList != null) {
                                        if (instructionList.size() != 0) {
                                            for (int i = 0; i < instructionList.size(); i++) {
                                                LodgementInstructionsTO objLodgementInstructionsTO = (LodgementInstructionsTO) instructionList.get(i);
                                                if (objLodgementInstructionsTO.getAmount() != null) {
                                                    totAmountInst = totAmountInst + (objLodgementInstructionsTO.getAmount());
                                                }
                                            }
                                        }
                                    }
                                    if (!objLodgementMasterTO.getProdType().equals("GL")) {                    
                                        if (obcProdType.equals("AB")) {
                                            obj.put("INVESTMENT_ACC_NO", CommonUtil.convertObjToStr(obj.get("OTHER_BANK_HEAD")));
                                            HashMap achdMap = new HashMap();
                                            List achdLst = ServerUtil.executeQuery("getSelectOthrBankAcHd", obj);
                                            if (achdLst != null && achdLst.size() > 0) {
                                                achdMap = new HashMap();
                                                achdMap = (HashMap) achdLst.get(0);
                                                txMap.put(TransferTrans.DR_AC_HD, (String) acHeadMap.get("AC_HEAD"));
                                                txMap.put(TransferTrans.DR_ACT_NUM, objLodgementMasterTO.getBorrowAcctNum());
                                                txMap.put(TransferTrans.DR_PROD_ID, objLodgementMasterTO.getBorrowProdId());
                                            }
                                        } else {
                                            txMap.put(TransferTrans.DR_AC_HD, (String) acHeadMap.get("AC_HEAD"));
                                            txMap.put(TransferTrans.DR_ACT_NUM, objLodgementMasterTO.getBorrowAcctNum());
                                            txMap.put(TransferTrans.DR_PROD_ID, objLodgementMasterTO.getBorrowProdId());
                                        }
                                    } else {
                                        txMap.put(TransferTrans.DR_AC_HD, objLodgementMasterTO.getBorrowAcctNum());
                                    }
                                    txMap.put(TransferTrans.DR_PROD_TYPE, objLodgementMasterTO.getProdType());
                                    txMap.put(TransferTrans.PARTICULARS, "VPL Chrg LodgeID : " + objLodgementMasterTO.getLodgementId() + " Dishonour");
                                    txMap.put(TransferTrans.DR_BRANCH, branchID);
                                    txMap.put(TransferTrans.CR_PROD_TYPE, TransactionFactory.GL);
                                    txMap.put("TRANS_MOD_TYPE", "GL");
                                    txMap.put(TransferTrans.AUTHORIZE_STATUS_2, "ENTERED_AMOUNT");
                                    transAmt = CommonUtil.convertObjToDouble(objLodgementRemitTO.getRemitFavouring()).doubleValue() + totAmountInst;
                                    txMap.put(TransferTrans.PARTICULARS, objLodgementMasterTO.getBorrowProdId());
                                    double serTaxAmt = 0;
                                    double swachhCess = 0.0;
                                    double krishikalyanCess = 0.0;
                                    double normalServiceTax = 0.0;
                                    double transDebAmt = transAmt;
                                    HashMap serTaxMap = new HashMap();
                                    if (obj.containsKey("serviceTaxDetails")) {
                                        serTaxMap = (HashMap) obj.get("serviceTaxDetails");
                                        if (serTaxMap != null && serTaxMap.size() > 0) {
                                            if (serTaxMap.containsKey("TOT_TAX_AMT") && serTaxMap.get("TOT_TAX_AMT") != null) {
                                                serTaxAmt = CommonUtil.convertObjToDouble(serTaxMap.get("TOT_TAX_AMT"));
                                            }
                                            if (serTaxMap.containsKey(ServiceTaxCalculation.SWACHH_CESS) && serTaxMap.get(ServiceTaxCalculation.SWACHH_CESS) != null) {
                                                swachhCess = CommonUtil.convertObjToDouble(serTaxMap.get(ServiceTaxCalculation.SWACHH_CESS));
                                            }
                                            if (serTaxMap.containsKey(ServiceTaxCalculation.KRISHIKALYAN_CESS) && serTaxMap.get(ServiceTaxCalculation.KRISHIKALYAN_CESS) != null) {
                                                krishikalyanCess = CommonUtil.convertObjToDouble(serTaxMap.get(ServiceTaxCalculation.KRISHIKALYAN_CESS));
                                            }
                                            if (serTaxMap.containsKey(ServiceTaxCalculation.SERVICE_TAX) && serTaxMap.get(ServiceTaxCalculation.SERVICE_TAX) != null) {
                                                normalServiceTax = CommonUtil.convertObjToDouble(serTaxMap.get(ServiceTaxCalculation.SERVICE_TAX));
                                            }
                                          //  normalServiceTax -= (swachhCess + krishikalyanCess);
                                        }                                        
                                        transDebAmt = transDebAmt + serTaxAmt;
                                    }
                                    txMap.put("SCREEN_NAME",(String) obj.get(CommonConstants.SCREEN));// Added by nithya on 23-09-2016 for inserting screen name for transactions
                                    transferTo = transactionDAO.addTransferDebitLocal(txMap, transDebAmt);
                                    transferTo.setInstType(objLodgementMasterTO.getBillsStatus());
                                    transferList.add(transferTo);
                                    txMap.put(TransferTrans.PARTICULARS, "A/c No-" + " " + objLodgementMasterTO.getBorrowAcctNum());
                                    txMap.put(TransferTrans.PARTICULARS, "VPL Chrg LodgeID : " + objLodgementMasterTO.getLodgementId() + " Dishonour");
                                    txMap.put(TransferTrans.CR_AC_HD, (String) acHeads.get("MISC_INC_BNK_CHRG_CR_HD"));
                                    txMap.put(TransferTrans.CR_BRANCH, branchID);
                                    txMap.put(TransferTrans.CR_PROD_TYPE, TransactionFactory.GL);
                                    txMap.put("TRANS_MOD_TYPE", "GL");
                                    txMap.put("AUTHORIZEREMARKS", "DISHONUR");
                                    transAmt = CommonUtil.convertObjToDouble(objLodgementRemitTO.getRemitFavouring()).doubleValue() + totAmountInst;
                                    txMap.put(TransferTrans.AUTHORIZE_STATUS_2, "ENTERED_AMOUNT");
                                    txMap.put("SCREEN_NAME",(String) obj.get(CommonConstants.SCREEN));// Added by nithya on 23-09-2016 for inserting screen name for transactions
                                    transferTo = transactionDAO.addTransferCreditLocal(txMap, transAmt);
                                    transferTo.setInstType(objLodgementMasterTO.getBillsStatus());
                                    transferList.add(transferTo);
                                    //transAmt = 0;
                                    transAmt = 0;
                                    if (swachhCess > 0) {
                                        txMap.put(TransferTrans.CR_AC_HD, CommonUtil.convertObjToStr(serTaxMap.get("SWACHH_HEAD_ID")));  // credit to Postage Charge account head......
                                        txMap.put(TransferTrans.PARTICULARS, "CGST : " + objLodgementMasterTO.getLodgementId() + " Realize");
                                        if (obj.get("OTHER_BANK_PROD_TYPE") != null && obj.get("OTHER_BANK_PROD_TYPE").equals(TransactionFactory.OTHERBANKACTS)) {
                                            txMap.put(TransferTrans.CR_ACT_NUM, null);
                                        }
                                        txMap.put(TransferTrans.AUTHORIZE_STATUS_2, "ENTERED_AMOUNT");
                                        txMap.put("TRANS_MOD_TYPE", TransactionFactory.GL);
                                        txMap.put(TransferTrans.CR_PROD_TYPE, TransactionFactory.GL);
                                        txMap.put("SCREEN_NAME", (String) obj.get(CommonConstants.SCREEN));// Added by nithya on 23-09-2016 for inserting screen name for transactions
                                        transferTo = transactionDAO.addTransferCreditLocal(txMap, swachhCess);
                                        transferTo.setInstType(objLodgementMasterTO.getBillsStatus());
                                        transferTo.setStatusBy(CommonUtil.convertObjToStr(obj.get("USER_ID")));
                                        transferTo.setInitTransId(CommonUtil.convertObjToStr(obj.get("USER_ID")));
                                        transferList.add(transferTo);
                                       // normalServiceTax -= swachhCess;
                                    }
                                    if (krishikalyanCess > 0) {
                                        txMap.put(TransferTrans.CR_AC_HD, CommonUtil.convertObjToStr(serTaxMap.get("KRISHIKALYAN_HEAD_ID")));  // credit to Postage Charge account head......
                                        txMap.put(TransferTrans.PARTICULARS, "SGST : " + objLodgementMasterTO.getLodgementId() + " Realize");
                                        if (obj.get("OTHER_BANK_PROD_TYPE") != null && obj.get("OTHER_BANK_PROD_TYPE").equals(TransactionFactory.OTHERBANKACTS)) {
                                            txMap.put(TransferTrans.CR_ACT_NUM, null);
                                        }
                                        txMap.put(TransferTrans.AUTHORIZE_STATUS_2, "ENTERED_AMOUNT");
                                        txMap.put("TRANS_MOD_TYPE", TransactionFactory.GL);
                                        txMap.put(TransferTrans.CR_PROD_TYPE, TransactionFactory.GL);
                                        txMap.put("SCREEN_NAME", (String) obj.get(CommonConstants.SCREEN));// Added by nithya on 23-09-2016 for inserting screen name for transactions
                                        transferTo = transactionDAO.addTransferCreditLocal(txMap, krishikalyanCess);
                                        transferTo.setInstType(objLodgementMasterTO.getBillsStatus());
                                        transferTo.setStatusBy(CommonUtil.convertObjToStr(obj.get("USER_ID")));
                                        transferTo.setInitTransId(CommonUtil.convertObjToStr(obj.get("USER_ID")));
                                        transferList.add(transferTo);
                                      //  normalServiceTax -= krishikalyanCess;
                                    }
                                    if (normalServiceTax > 0) {
                                        txMap.put(TransferTrans.CR_AC_HD, CommonUtil.convertObjToStr(serTaxMap.get("TAX_HEAD_ID")));  // credit to Postage Charge account head......
                                        txMap.put(TransferTrans.PARTICULARS, "SERVICE TAX for BankCharges LodgeID : " + objLodgementMasterTO.getLodgementId() + " Realize");
                                        if (obj.get("OTHER_BANK_PROD_TYPE") != null && obj.get("OTHER_BANK_PROD_TYPE").equals(TransactionFactory.OTHERBANKACTS)) {
                                            txMap.put(TransferTrans.CR_ACT_NUM, null);
                                        }
                                        txMap.put(TransferTrans.AUTHORIZE_STATUS_2, "ENTERED_AMOUNT");
                                        txMap.put("TRANS_MOD_TYPE", TransactionFactory.GL);
                                        txMap.put(TransferTrans.CR_PROD_TYPE, TransactionFactory.GL);
                                        txMap.put("SCREEN_NAME",(String) obj.get(CommonConstants.SCREEN));// Added by nithya on 23-09-2016 for inserting screen name for transactions
                                        transferTo = transactionDAO.addTransferCreditLocal(txMap, normalServiceTax);
                                        transferTo.setInstType(objLodgementMasterTO.getBillsStatus());
                                        transferTo.setStatusBy(CommonUtil.convertObjToStr(obj.get("USER_ID")));
                                        transferTo.setInitTransId(CommonUtil.convertObjToStr(obj.get("USER_ID")));
                                        transferList.add(transferTo);
                                    }
                                    transactionDAO.setTransactionType(transactionDAO.TRANSFER);
                                    transactionDAO.doTransferLocal(transferList, branchID);
                                    //cash tran
                                    transAmt = CommonUtil.convertObjToDouble(objLodgementRemitTO.getRemitFavouring()).doubleValue();
                                    txMap = new HashMap();
                                    TransactionTO transTO = new TransactionTO();
                                    ArrayList cashList = new ArrayList();
                                    transactionDAO.setCommandMode(CommonConstants.TOSTATUS_INSERT);
                                    txMap.put(TransferTrans.DR_AC_HD, (String) acHeads.get("POSTAGE_AC_HD"));
                                    txMap.put(TransferTrans.DR_BRANCH, branchID);
                                    txMap.put(TransferTrans.CURRENCY, "INR");
                                    txMap.put(TransferTrans.DR_PROD_TYPE, TransactionFactory.GL);
                                    txMap.put("TRANS_MOD_TYPE", "GL");
                                    txMap.put(TransferTrans.PARTICULARS, "VPL Chrg LodgeID : " + objLodgementMasterTO.getLodgementId() + " Dishonour");
                                    transactionDAO.setTransType(CommonConstants.CREDIT);
                                    txMap.put(TransferTrans.AUTHORIZE_STATUS_2, "ENTERED_AMOUNT");
                                    txMap.put("SCREEN_NAME",(String) obj.get(CommonConstants.SCREEN));// Added by nithya on 23-09-2016 for inserting screen name for transactions
                                    transactionDAO.addTransferDebit(txMap, transAmt);
                                    transactionDAO.deleteTxList();
                                    transTO.setCommand(CommonConstants.TOSTATUS_INSERT);
                                    transTO.setTransType("CASH");
                                    transTO.setTransAmt(new Double(transAmt));
                                    transTO.setBatchId(objLodgementMasterTO.getLodgementId());
                                    transTO.setInstType(objLodgementMasterTO.getBillsStatus());
                                    cashList.add(transTO);
                                    transactionDAO.doTransfer();
                                }                                
                            }//babu1 dis
                            double temp = 0;
                            for (int i = 0; i < instructionList.size(); i++) {
                                objLodgementInstTO = (LodgementInstructionsTO) instructionList.get(i);
                                String status = CommonUtil.convertObjToStr(objLodgementInstTO.getStatus());
                                if (!status.equals("DELETED")) {
                                    if (objLodgementInstTO.getInstruction().equals("DISHONOUR_CHARGES") || 
                                            (CommonUtil.convertObjToDouble(objLodgementRemitTO.getRemitFavouring())<=0 && objLodgementInstTO.getInstruction().equals("BANK_CHARGES"))) {
                                        transactionDAO.setCommandMode(CommonConstants.TOSTATUS_INSERT);
                                        temp = temp + ((CommonUtil.convertObjToDouble(objLodgementInstTO.getAmount()).doubleValue())
                                                + (CommonUtil.convertObjToDouble(objLodgementInstTO.getServiceTx()).doubleValue()));
                                        if (!objLodgementMasterTO.getProdType().equals("GL")) {
                                            txMap.put(TransferTrans.DR_AC_HD, null);
                                            txMap.put(TransferTrans.DR_ACT_NUM, objLodgementMasterTO.getBorrowAcctNum());
                                            txMap.put(TransferTrans.DR_PROD_ID, objLodgementMasterTO.getBorrowProdId());
                                        } else {
                                            txMap.put(TransferTrans.DR_AC_HD, objLodgementMasterTO.getBorrowAcctNum());
                                        }
                                        txMap.put(TransferTrans.DR_BRANCH, branchID);
                                        txMap.put(TransferTrans.CURRENCY, "INR");
                                        txMap.put(TransferTrans.DR_PROD_TYPE, objLodgementMasterTO.getProdType());
                                        txMap.put("TRANS_MOD_TYPE", objLodgementMasterTO.getProdType());
                                        txMap.put(TransferTrans.CR_BRANCH, branchID);
                                        txMap.put(TransferTrans.PARTICULARS, "Dishonour Chrg LodgeID : " + objLodgementMasterTO.getLodgementId() + " Dishonour");
                                        transAmt = temp;
                                        txMap.put(TransferTrans.AUTHORIZE_STATUS_2, "ENTERED_AMOUNT");
                                        transactionDAO.setTransType(CommonConstants.CREDIT);
                                        txMap.put("SCREEN_NAME",(String) obj.get(CommonConstants.SCREEN));// Added by nithya on 23-09-2016 for inserting screen name for transactions
                                        transferTo = transactionDAO.addTransferDebitLocal(txMap, transAmt);
                                        transferTo.setInstType(objLodgementMasterTO.getBillsStatus());
                                        transferList.add(transferTo);
                                        if ((CommonUtil.convertObjToDouble(objLodgementInstTO.getAmount()).doubleValue()) > 0) {
                                            txMap.put(TransferTrans.CR_AC_HD, (String) acHeads.get("CHRG_AC_HD"));
                                            txMap.put(TransferTrans.CR_PROD_TYPE, TransactionFactory.GL);
                                            txMap.put("TRANS_MOD_TYPE", "GL");
                                            txMap.put(TransferTrans.PARTICULARS, "Dishonour Chrg LodgeID : " + objLodgementMasterTO.getLodgementId() + " Dishonour");
                                            transAmt = CommonUtil.convertObjToDouble(objLodgementInstTO.getAmount()).doubleValue();
                                            txMap.put(TransferTrans.AUTHORIZE_STATUS_2, "ENTERED_AMOUNT");
                                            transactionDAO.setTransType(CommonConstants.DEBIT);
                                            txMap.put("SCREEN_NAME",(String) obj.get(CommonConstants.SCREEN));// Added by nithya on 23-09-2016 for inserting screen name for transactions
                                            transferTo = transactionDAO.addTransferCreditLocal(txMap, transAmt);
                                            transferTo.setInstType(objLodgementMasterTO.getBillsStatus());
                                            transferList.add(transferTo);
                                            transAmt = 0;
                                        }
                                        if ((CommonUtil.convertObjToDouble(objLodgementInstTO.getServiceTx()).doubleValue()) > 0) {
                                            txMap.put(TransferTrans.CR_AC_HD, (String) acHeads.get("SERVICE_TAX_HD"));
                                            txMap.put(TransferTrans.CR_PROD_TYPE, TransactionFactory.GL);
                                            txMap.put("TRANS_MOD_TYPE", "GL");
                                            txMap.put(TransferTrans.PARTICULARS, "ServiceTax LodgeID : " + objLodgementMasterTO.getLodgementId() + " Dishonour");
                                            transAmt = CommonUtil.convertObjToDouble(objLodgementInstTO.getServiceTx()).doubleValue();
                                            transactionDAO.setTransType(CommonConstants.DEBIT);
                                            txMap.put(TransferTrans.AUTHORIZE_STATUS_2, "ENTERED_AMOUNT");
                                            txMap.put("SCREEN_NAME",(String) obj.get(CommonConstants.SCREEN));// Added by nithya on 23-09-2016 for inserting screen name for transactions
                                            transferTo = transactionDAO.addTransferCreditLocal(txMap, transAmt);
                                            transferTo.setInstType(objLodgementMasterTO.getBillsStatus());
                                            transferList.add(transferTo);
                                            transAmt = 0;
                                        }
                                        transactionDAO.setTransactionType(transactionDAO.TRANSFER);
                                        transactionDAO.doTransferLocal(transferList, branchID);
                                        //INSERT INSTRUCTION
                                        HashMap instMap = new HashMap();
                                        instMap.put("LODGEMENT_ID", objLodgementMasterTO.getLodgementId());
                                        instMap.put("INSTRUCTION", objLodgementInstTO.getInstruction());
                                        List lstInst = sqlMap.executeQueryForList("getBillsExtInst", instMap);
                                        instMap = null;
                                        if (lstInst != null && lstInst.size() > 0) {
                                            sqlMap.executeUpdate("updateLodgementInstructionsTO", objLodgementInstTO);
                                        } else {
                                            sqlMap.executeUpdate("insertLodgementInstructionsTO", objLodgementInstTO);
                                        }
                                        lstInst = null;
                                    }
                                    status = null;
                                }
                            }
                        }
                        if (objLodgementMasterTO.getTranType().equals("CASH")) {                            
                            if (CommonUtil.convertObjToDouble(objLodgementRemitTO.getRemitFavouring()).doubleValue() > 0) {
                                //cash tran credit
                                transAmt = CommonUtil.convertObjToDouble(objLodgementRemitTO.getRemitFavouring()).doubleValue();
                                txMap = new HashMap();
                                TransactionTO transTO = new TransactionTO();
                                ArrayList cashList = new ArrayList();
                                transactionDAO.setCommandMode(CommonConstants.TOSTATUS_INSERT);
                                txMap.put(TransferTrans.CR_AC_HD, (String) acHeads.get("POSTAGE_AC_HD"));
                                txMap.put(TransferTrans.CR_BRANCH, branchID);
                                txMap.put(TransferTrans.CURRENCY, "INR");
                                txMap.put(TransferTrans.CR_PROD_TYPE, TransactionFactory.GL);
                                //                                txMap.put(TransferTrans.DR_INST_TYPE, objLodgementMasterTO.getBillsStatus());
                                txMap.put(TransferTrans.PARTICULARS, "VPL Chrg LodgeID : " + objLodgementMasterTO.getLodgementId() + " Dishonour");
                                txMap.put(TransferTrans.AUTHORIZE_STATUS_2, "ENTERED_AMOUNT");
                                txMap.put("TRANS_MOD_TYPE", "GL");
                                transactionDAO.setTransType(CommonConstants.DEBIT);
                                txMap.put("SCREEN_NAME",(String) obj.get(CommonConstants.SCREEN));// Added by nithya on 23-09-2016 for inserting screen name for transactions
                                transactionDAO.addTransferCredit(txMap, transAmt);
                                transactionDAO.deleteTxList();
                                transTO.setCommand(CommonConstants.TOSTATUS_INSERT);
                                transTO.setTransType("CASH");
                                transTO.setTransAmt(new Double(transAmt));
                                transTO.setBatchId(objLodgementMasterTO.getLodgementId());
                                transTO.setInstType(objLodgementMasterTO.getBillsStatus());
                                cashList.add(transTO);
                                System.out.println("@@@@@@f" + cashList);
                                transactionDAO.addCashList(cashList);
                                transactionDAO.doTransfer();
                                //cash tran debit
                                transAmt = CommonUtil.convertObjToDouble(objLodgementRemitTO.getRemitFavouring()).doubleValue();
                                txMap = new HashMap();
                                transTO = new TransactionTO();
                                cashList = new ArrayList();
                                transactionDAO.setCommandMode(CommonConstants.TOSTATUS_INSERT);
                                txMap.put(TransferTrans.DR_AC_HD, (String) acHeads.get("POSTAGE_AC_HD"));
                                txMap.put(TransferTrans.DR_BRANCH, branchID);
                                txMap.put(TransferTrans.CURRENCY, "INR");
                                txMap.put(TransferTrans.DR_PROD_TYPE, TransactionFactory.GL);
                                txMap.put("TRANS_MOD_TYPE", "GL");
                                //                                txMap.put(TransferTrans.DR_INST_TYPE, objLodgementMasterTO.getBillsStatus());
                                txMap.put(TransferTrans.PARTICULARS, "VPL Chrg LodgeID : " + objLodgementMasterTO.getLodgementId() + " Dishonour");
                                txMap.put(TransferTrans.AUTHORIZE_STATUS_2, "ENTERED_AMOUNT");
                                txMap.put("SCREEN_NAME",(String) obj.get(CommonConstants.SCREEN));// Added by nithya on 23-09-2016 for inserting screen name for transactions
                                transactionDAO.setTransType(CommonConstants.CREDIT);
                                transactionDAO.addTransferDebit(txMap, transAmt);
                                transactionDAO.deleteTxList();
                                transTO.setCommand(CommonConstants.TOSTATUS_INSERT);
                                transTO.setTransType("CASH");
                                transTO.setTransAmt(new Double(transAmt));
                                transTO.setBatchId(objLodgementMasterTO.getLodgementId());
                                transTO.setInstType(objLodgementMasterTO.getBillsStatus());
                                cashList.add(transTO);
                                System.out.println("@@@@@@f" + cashList);
                                transactionDAO.addCashList(cashList);
                                transactionDAO.doTransfer();
                            }
                            //TRANSFER TRAN
                            double temp = 0;
                            for (int i = 0; i < instructionList.size(); i++) {
                                objLodgementInstTO = (LodgementInstructionsTO) instructionList.get(i);
                                String status = CommonUtil.convertObjToStr(objLodgementInstTO.getStatus());
                                if (!status.equals("DELETED")) {
                                    if (objLodgementInstTO.getInstruction().equals("DISHONOUR_CHARGES") || objLodgementInstTO.getInstruction().equals("BANK_CHARGES")) {
                                        transAmt = CommonUtil.convertObjToDouble(objLodgementInstTO.getAmount()).doubleValue();
                                        if (transAmt > 0) {
                                            txMap = new HashMap();
                                            TransactionTO transTO = new TransactionTO();
                                            ArrayList cashList = new ArrayList();
                                            transactionDAO.setCommandMode(CommonConstants.TOSTATUS_INSERT);
                                            txMap.put(TransferTrans.CR_AC_HD, (String) acHeads.get("CHRG_AC_HD"));
                                            txMap.put(TransferTrans.CR_BRANCH, branchID);
                                            txMap.put(TransferTrans.CURRENCY, "INR");
                                            txMap.put(TransferTrans.CR_PROD_TYPE, TransactionFactory.GL);
                                            txMap.put("TRANS_MOD_TYPE", "GL");
                                            txMap.put(TransferTrans.PARTICULARS, "VPL Chrg LodgeID : " + objLodgementMasterTO.getLodgementId() + " Dishonour");
                                            transactionDAO.setTransType(CommonConstants.DEBIT);
                                            txMap.put(TransferTrans.AUTHORIZE_STATUS_2, "ENTERED_AMOUNT");
                                            txMap.put("SCREEN_NAME",(String) obj.get(CommonConstants.SCREEN));// Added by nithya on 23-09-2016 for inserting screen name for transactions
                                            transactionDAO.addTransferCredit(txMap, transAmt);
                                            transactionDAO.deleteTxList();
                                            transTO.setCommand(CommonConstants.TOSTATUS_INSERT);
                                            transTO.setTransType("CASH");
                                            transTO.setTransAmt(new Double(transAmt));
                                            transTO.setBatchId(objLodgementMasterTO.getLodgementId());
                                            transTO.setInstType(objLodgementMasterTO.getBillsStatus());
                                            cashList.add(transTO);                                           
                                            transactionDAO.addCashList(cashList);
                                            transactionDAO.doTransfer();
                                            transAmt = 0;
                                        }
                                        transAmt = CommonUtil.convertObjToDouble(objLodgementInstTO.getServiceTx()).doubleValue();
                                        if (transAmt > 0) {
                                            txMap = new HashMap();
                                            TransactionTO transTO = new TransactionTO();
                                            ArrayList cashList = new ArrayList();
                                            transactionDAO.setCommandMode(CommonConstants.TOSTATUS_INSERT);
                                            txMap.put(TransferTrans.CR_AC_HD, (String) acHeads.get("COMM_AC_HD"));
                                            txMap.put(TransferTrans.CR_BRANCH, branchID);
                                            txMap.put(TransferTrans.CURRENCY, "INR");
                                            txMap.put(TransferTrans.CR_PROD_TYPE, TransactionFactory.GL);
                                            txMap.put("TRANS_MOD_TYPE", "GL");
                                            txMap.put(TransferTrans.PARTICULARS, "VPL Chrg LodgeID : " + objLodgementMasterTO.getLodgementId() + " Dishonour");
                                            txMap.put(TransferTrans.AUTHORIZE_STATUS_2, "ENTERED_AMOUNT");
                                            txMap.put("SCREEN_NAME",(String) obj.get(CommonConstants.SCREEN));// Added by nithya on 23-09-2016 for inserting screen name for transactions
                                            transactionDAO.setTransType(CommonConstants.DEBIT);
                                            transactionDAO.addTransferCredit(txMap, transAmt);
                                            transactionDAO.deleteTxList();
                                            transTO.setCommand(CommonConstants.TOSTATUS_INSERT);
                                            transTO.setTransType("CASH");
                                            transTO.setTransAmt(new Double(transAmt));
                                            transTO.setBatchId(objLodgementMasterTO.getLodgementId());
                                            transTO.setInstType(objLodgementMasterTO.getBillsStatus());
                                            cashList.add(transTO);
                                            System.out.println("@@@@@@f" + cashList);
                                            transactionDAO.addCashList(cashList);
                                            transactionDAO.doTransfer();
                                            transAmt = 0;
                                        }
                                        //INSERT INSTRUCTION
                                        HashMap instMap = new HashMap();
                                        instMap.put("LODGEMENT_ID", objLodgementMasterTO.getLodgementId());
                                        instMap.put("INSTRUCTION", objLodgementInstTO.getInstruction());
                                        List lstInst = sqlMap.executeQueryForList("getBillsExtInst", instMap);
                                        instMap = null;
                                        if (lstInst != null && lstInst.size() > 0) {
                                            sqlMap.executeUpdate("updateLodgementInstructionsTO", objLodgementInstTO);
                                        } else {
                                            sqlMap.executeUpdate("insertLodgementInstructionsTO", objLodgementInstTO);
                                        }
                                        lstInst = null;
                                    }
                                    status = null;
                                }
                            }

                        }
                    } else if (objLodgementMasterTO.getBillsStatus().equals("CLOSURE")) {
                        if (objLodgementMasterTO.getTranType().equals("TRANSFER")) {
                            if (CommonUtil.convertObjToDouble(objLodgementRemitTO.getRemitFavouring()).doubleValue() > 0) {
                                txMap = new HashMap();
                                transactionDAO.setCommandMode(CommonConstants.TOSTATUS_INSERT);
                                if (!objLodgementMasterTO.getProdType().equals("GL")) {
                                    txMap.put(TransferTrans.DR_AC_HD, null);
                                    txMap.put(TransferTrans.DR_ACT_NUM, objLodgementMasterTO.getBorrowAcctNum());
                                    txMap.put(TransferTrans.DR_PROD_ID, objLodgementMasterTO.getBorrowProdId());
                                    txMap.put("TRANS_MOD_TYPE", objLodgementMasterTO.getProdType());
                                } else {
                                    txMap.put(TransferTrans.DR_AC_HD, objLodgementMasterTO.getBorrowAcctNum());
                                    txMap.put("TRANS_MOD_TYPE", "GL");
                                }
                                txMap.put(TransferTrans.DR_BRANCH, branchID);
                                txMap.put(TransferTrans.CURRENCY, "INR");
                                txMap.put(TransferTrans.DR_PROD_TYPE, objLodgementMasterTO.getProdType());
                                txMap.put(TransferTrans.PARTICULARS, "VPL Chrg LodgeID : " + objLodgementMasterTO.getLodgementId() + " Closure");
                                txMap.put(TransferTrans.CR_AC_HD, (String) acHeads.get("POSTAGE_AC_HD")); // credit to Remittance Issue account head......
                                txMap.put(TransferTrans.CR_BRANCH, branchID);
                                txMap.put(TransferTrans.CR_PROD_TYPE, TransactionFactory.GL);
                                txMap.put("TRANS_MOD_TYPE", "GL");
                                transAmt = CommonUtil.convertObjToDouble(objLodgementRemitTO.getRemitFavouring()).doubleValue();
                                txMap.put(TransferTrans.PARTICULARS, objLodgementMasterTO.getBorrowProdId());
                                txMap.put(TransferTrans.AUTHORIZE_STATUS_2, "ENTERED_AMOUNT");
                                //                            txMap.put(TransferTrans.DR_INST_TYPE, objLodgementMasterTO.getBillsStatus());
                                transactionDAO.setTransType(CommonConstants.CREDIT);
                                txMap.put("SCREEN_NAME",(String) obj.get(CommonConstants.SCREEN));// Added by nithya on 23-09-2016 for inserting screen name for transactions
                                transferTo = transactionDAO.addTransferDebitLocal(txMap, transAmt);
                                transferTo.setInstType(objLodgementMasterTO.getBillsStatus());
                                transferList.add(transferTo);
                                txMap.put(TransferTrans.PARTICULARS, "A/c No-" + " " + objLodgementMasterTO.getBorrowAcctNum());
                                //                            txMap.put(TransferTrans.DR_INST_TYPE, objLodgementMasterTO.getBillsStatus());
                                txMap.put(TransferTrans.PARTICULARS, "VPL Chrg LodgeID : " + objLodgementMasterTO.getLodgementId() + " Closure");
                                transAmt = CommonUtil.convertObjToDouble(objLodgementRemitTO.getRemitFavouring()).doubleValue();
                                transactionDAO.setTransType(CommonConstants.DEBIT);
                                transferTo = transactionDAO.addTransferCreditLocal(txMap, transAmt);
                                transferTo.setInstType(objLodgementMasterTO.getBillsStatus());
                                transferList.add(transferTo);
                                transAmt = 0;
                                transactionDAO.setTransactionType(transactionDAO.TRANSFER);
                                transactionDAO.doTransferLocal(transferList, branchID);
                                //cash tran
                                transAmt = CommonUtil.convertObjToDouble(objLodgementRemitTO.getRemitFavouring()).doubleValue();
                                txMap = new HashMap();
                                TransactionTO transTO = new TransactionTO();
                                ArrayList cashList = new ArrayList();
                                transactionDAO.setCommandMode(CommonConstants.TOSTATUS_INSERT);
                                txMap.put(TransferTrans.DR_AC_HD, (String) acHeads.get("POSTAGE_AC_HD"));
                                txMap.put(TransferTrans.DR_BRANCH, branchID);
                                txMap.put(TransferTrans.CURRENCY, "INR");
                                txMap.put(TransferTrans.DR_PROD_TYPE, TransactionFactory.GL);
                                txMap.put("TRANS_MOD_TYPE", "GL");
                                txMap.put(TransferTrans.AUTHORIZE_STATUS_2, "ENTERED_AMOUNT");
                                txMap.put(TransferTrans.PARTICULARS, "VPL Chrg LodgeID : " + objLodgementMasterTO.getLodgementId() + " Closure");
                                txMap.put("SCREEN_NAME",(String) obj.get(CommonConstants.SCREEN));// Added by nithya on 23-09-2016 for inserting screen name for transactions
                                transactionDAO.setTransType(CommonConstants.CREDIT);
                                transactionDAO.addTransferDebit(txMap, transAmt);
                                transactionDAO.deleteTxList();
                                transTO.setCommand(CommonConstants.TOSTATUS_INSERT);
                                transTO.setTransType("CASH");
                                transTO.setTransAmt(new Double(transAmt));
                                transTO.setBatchId(objLodgementMasterTO.getLodgementId());
                                transTO.setInstType(objLodgementMasterTO.getBillsStatus());
                                cashList.add(transTO);
                                System.out.println("@@@@@@f" + cashList);
                                transactionDAO.addCashList(cashList);
                                transactionDAO.doTransfer();
                            }
                            double temp = 0;
                            for (int i = 0; i < instructionList.size(); i++) {
                                objLodgementInstTO = (LodgementInstructionsTO) instructionList.get(i);
                                String status = CommonUtil.convertObjToStr(objLodgementInstTO.getStatus());
                                if (!status.equals("DELETED")) {
                                    if (objLodgementInstTO.getInstruction().equals("CLOSURE_CHARGES")) {
                                        transactionDAO.setCommandMode(CommonConstants.TOSTATUS_INSERT);
                                        temp = temp + ((CommonUtil.convertObjToDouble(objLodgementInstTO.getAmount()).doubleValue())
                                                + (CommonUtil.convertObjToDouble(objLodgementInstTO.getServiceTx()).doubleValue()));
                                        if (!objLodgementMasterTO.getProdType().equals("GL")) {
                                            txMap.put(TransferTrans.DR_AC_HD, null);
                                            txMap.put(TransferTrans.DR_ACT_NUM, objLodgementMasterTO.getBorrowAcctNum());
                                            txMap.put(TransferTrans.DR_PROD_ID, objLodgementMasterTO.getBorrowProdId());
                                        } else {
                                            txMap.put(TransferTrans.DR_AC_HD, objLodgementMasterTO.getBorrowAcctNum());
                                        }
                                        txMap.put(TransferTrans.DR_BRANCH, branchID);
                                        txMap.put(TransferTrans.CURRENCY, "INR");
                                        txMap.put(TransferTrans.DR_PROD_TYPE, objLodgementMasterTO.getProdType());
                                        txMap.put("TRANS_MOD_TYPE", objLodgementMasterTO.getProdType());
                                        txMap.put(TransferTrans.CR_BRANCH, branchID);
                                        txMap.put(TransferTrans.PARTICULARS, "Closure Chrg LodgeID : " + objLodgementMasterTO.getLodgementId() + " Closure");
                                        txMap.put(TransferTrans.AUTHORIZE_STATUS_2, "ENTERED_AMOUNT");
                                        transAmt = temp;
                                        transactionDAO.setTransType(CommonConstants.CREDIT);
                                        txMap.put("SCREEN_NAME",(String) obj.get(CommonConstants.SCREEN));// Added by nithya on 23-09-2016 for inserting screen name for transactions
                                        transferTo = transactionDAO.addTransferDebitLocal(txMap, transAmt);
                                        transferTo.setInstType(objLodgementMasterTO.getBillsStatus());
                                        transferList.add(transferTo);
                                        if ((CommonUtil.convertObjToDouble(objLodgementInstTO.getAmount()).doubleValue()) > 0) {
                                            txMap.put(TransferTrans.CR_AC_HD, (String) acHeads.get("CHRG_AC_HD"));
                                            txMap.put(TransferTrans.CR_PROD_TYPE, TransactionFactory.GL);
                                            txMap.put("TRANS_MOD_TYPE","GL");
                                            txMap.put(TransferTrans.PARTICULARS, "Closure Chrg LodgeID : " + objLodgementMasterTO.getLodgementId() + " Closure");
                                            txMap.put(TransferTrans.AUTHORIZE_STATUS_2, "ENTERED_AMOUNT");
                                            transAmt = CommonUtil.convertObjToDouble(objLodgementInstTO.getAmount()).doubleValue();
                                            transactionDAO.setTransType(CommonConstants.DEBIT);
                                            txMap.put("SCREEN_NAME",(String) obj.get(CommonConstants.SCREEN));// Added by nithya on 23-09-2016 for inserting screen name for transactions
                                            transferTo = transactionDAO.addTransferCreditLocal(txMap, transAmt);
                                            transferTo.setInstType(objLodgementMasterTO.getBillsStatus());
                                            transferList.add(transferTo);
                                            transAmt = 0;
                                        }
                                        if ((CommonUtil.convertObjToDouble(objLodgementInstTO.getServiceTx()).doubleValue()) > 0) {
                                            txMap.put(TransferTrans.CR_AC_HD, (String) acHeads.get("SERVICE_TAX_HD"));
                                            txMap.put(TransferTrans.CR_PROD_TYPE, TransactionFactory.GL);
                                            txMap.put("TRANS_MOD_TYPE", "GL");
                                            txMap.put(TransferTrans.PARTICULARS, "ServiceTax LodgeID : " + objLodgementMasterTO.getLodgementId() + " Closure");
                                            txMap.put(TransferTrans.AUTHORIZE_STATUS_2, "ENTERED_AMOUNT");
                                            transAmt = CommonUtil.convertObjToDouble(objLodgementInstTO.getServiceTx()).doubleValue();
                                            transactionDAO.setTransType(CommonConstants.DEBIT);
                                            txMap.put("SCREEN_NAME",(String) obj.get(CommonConstants.SCREEN));// Added by nithya on 23-09-2016 for inserting screen name for transactions
                                            transferTo = transactionDAO.addTransferCreditLocal(txMap, transAmt);
                                            transferTo.setInstType(objLodgementMasterTO.getBillsStatus());
                                            transferList.add(transferTo);
                                            transAmt = 0;
                                        }
                                        transactionDAO.setTransactionType(transactionDAO.TRANSFER);
                                        transactionDAO.doTransferLocal(transferList, branchID);

                                        //INSERT INSTRUCTION
                                        HashMap instMap = new HashMap();
                                        instMap.put("LODGEMENT_ID", objLodgementMasterTO.getLodgementId());
                                        instMap.put("INSTRUCTION", objLodgementInstTO.getInstruction());
                                        List lstInst = sqlMap.executeQueryForList("getBillsExtInst", instMap);
                                        instMap = null;
                                        if (lstInst != null && lstInst.size() > 0) {
                                            sqlMap.executeUpdate("updateLodgementInstructionsTO", objLodgementInstTO);
                                        } else {
                                            sqlMap.executeUpdate("insertLodgementInstructionsTO", objLodgementInstTO);
                                        }
                                        lstInst = null;
                                    }
                                    status = null;
                                }
                            }
                        }
                        if (objLodgementMasterTO.getTranType().equals("CASH")) {
                            if (CommonUtil.convertObjToDouble(objLodgementRemitTO.getRemitFavouring()).doubleValue() > 0) {
                                //cash tran credit
                                transAmt = CommonUtil.convertObjToDouble(objLodgementRemitTO.getRemitFavouring()).doubleValue();
                                txMap = new HashMap();
                                TransactionTO transTO = new TransactionTO();
                                ArrayList cashList = new ArrayList();
                                transactionDAO.setCommandMode(CommonConstants.TOSTATUS_INSERT);
                                txMap.put(TransferTrans.CR_AC_HD, (String) acHeads.get("POSTAGE_AC_HD"));
                                txMap.put(TransferTrans.CR_BRANCH, branchID);
                                txMap.put(TransferTrans.CURRENCY, "INR");
                                txMap.put(TransferTrans.CR_PROD_TYPE, TransactionFactory.GL);
                                //                                txMap.put(TransferTrans.DR_INST_TYPE, objLodgementMasterTO.getBillsStatus());
                                txMap.put(TransferTrans.PARTICULARS, "VPL Chrg LodgeID : " + objLodgementMasterTO.getLodgementId() + " Closure");
                                txMap.put(TransferTrans.AUTHORIZE_STATUS_2, "ENTERED_AMOUNT");
                                txMap.put("TRANS_MOD_TYPE", "GL");
                                txMap.put("SCREEN_NAME",(String) obj.get(CommonConstants.SCREEN));// Added by nithya on 23-09-2016 for inserting screen name for transactions
                                transactionDAO.setTransType(CommonConstants.DEBIT);
                                transactionDAO.addTransferCredit(txMap, transAmt);
                                transactionDAO.deleteTxList();
                                transTO.setCommand(CommonConstants.TOSTATUS_INSERT);
                                transTO.setTransType("CASH");
                                transTO.setTransAmt(new Double(transAmt));
                                transTO.setBatchId(objLodgementMasterTO.getLodgementId());
                                transTO.setInstType(objLodgementMasterTO.getBillsStatus());
                                cashList.add(transTO);
                                System.out.println("@@@@@@f" + cashList);
                                transactionDAO.addCashList(cashList);
                                transactionDAO.doTransfer();
                                //cash tran debit
                                transAmt = CommonUtil.convertObjToDouble(objLodgementRemitTO.getRemitFavouring()).doubleValue();
                                txMap = new HashMap();
                                transTO = new TransactionTO();
                                cashList = new ArrayList();
                                transactionDAO.setCommandMode(CommonConstants.TOSTATUS_INSERT);
                                txMap.put(TransferTrans.DR_AC_HD, (String) acHeads.get("POSTAGE_AC_HD"));
                                txMap.put(TransferTrans.DR_BRANCH, branchID);
                                txMap.put(TransferTrans.CURRENCY, "INR");
                                txMap.put(TransferTrans.DR_PROD_TYPE, TransactionFactory.GL);
                                txMap.put("TRANS_MOD_TYPE", "GL");
                                //                                txMap.put(TransferTrans.DR_INST_TYPE, objLodgementMasterTO.getBillsStatus());
                                txMap.put(TransferTrans.PARTICULARS, "VPL Chrg LodgeID : " + objLodgementMasterTO.getLodgementId() + " Closure");
                                txMap.put(TransferTrans.AUTHORIZE_STATUS_2, "ENTERED_AMOUNT");
                                txMap.put("SCREEN_NAME",(String) obj.get(CommonConstants.SCREEN));// Added by nithya on 23-09-2016 for inserting screen name for transactions
                                transactionDAO.setTransType(CommonConstants.CREDIT);
                                transactionDAO.addTransferDebit(txMap, transAmt);
                                transactionDAO.deleteTxList();
                                transTO.setCommand(CommonConstants.TOSTATUS_INSERT);
                                transTO.setTransType("CASH");
                                transTO.setTransAmt(new Double(transAmt));
                                transTO.setBatchId(objLodgementMasterTO.getLodgementId());
                                transTO.setInstType(objLodgementMasterTO.getBillsStatus());
                                cashList.add(transTO);                                
                                transactionDAO.addCashList(cashList);
                                transactionDAO.doTransfer();
                            }
                            //TRANSFER TRAN
                            double temp = 0;
                            for (int i = 0; i < instructionList.size(); i++) {
                                objLodgementInstTO = (LodgementInstructionsTO) instructionList.get(i);
                                String status = CommonUtil.convertObjToStr(objLodgementInstTO.getStatus());
                                if (!status.equals("DELETED")) {
                                    if (objLodgementInstTO.getInstruction().equals("CLOSURE_CHARGES")) {
                                        transAmt = CommonUtil.convertObjToDouble(objLodgementInstTO.getAmount()).doubleValue();
                                        if (transAmt > 0) {
                                            txMap = new HashMap();
                                            TransactionTO transTO = new TransactionTO();
                                            ArrayList cashList = new ArrayList();
                                            transactionDAO.setCommandMode(CommonConstants.TOSTATUS_INSERT);
                                            txMap.put(TransferTrans.CR_AC_HD, (String) acHeads.get("CHRG_AC_HD"));
                                            txMap.put(TransferTrans.CR_BRANCH, branchID);
                                            txMap.put(TransferTrans.CURRENCY, "INR");
                                            txMap.put(TransferTrans.CR_PROD_TYPE, TransactionFactory.GL);
                                            txMap.put("TRANS_MOD_TYPE", "GL");
                                            txMap.put(TransferTrans.AUTHORIZE_STATUS_2, "ENTERED_AMOUNT");
                                            txMap.put(TransferTrans.PARTICULARS, "VPL Chrg LodgeID : " + objLodgementMasterTO.getLodgementId() + " Closure");
                                            txMap.put("SCREEN_NAME",(String) obj.get(CommonConstants.SCREEN));// Added by nithya on 23-09-2016 for inserting screen name for transactions
                                            transactionDAO.setTransType(CommonConstants.DEBIT);
                                            transactionDAO.addTransferCredit(txMap, transAmt);
                                            transactionDAO.deleteTxList();
                                            transTO.setCommand(CommonConstants.TOSTATUS_INSERT);
                                            transTO.setTransType("CASH");
                                            transTO.setTransAmt(new Double(transAmt));
                                            transTO.setBatchId(objLodgementMasterTO.getLodgementId());
                                            transTO.setInstType(objLodgementMasterTO.getBillsStatus());
                                            cashList.add(transTO);
                                            System.out.println("@@@@@@f" + cashList);
                                            transactionDAO.addCashList(cashList);
                                            transactionDAO.doTransfer();
                                            transAmt = 0;
                                        }
                                        transAmt = CommonUtil.convertObjToDouble(objLodgementInstTO.getServiceTx()).doubleValue();
                                        if (transAmt > 0) {
                                            txMap = new HashMap();
                                            TransactionTO transTO = new TransactionTO();
                                            ArrayList cashList = new ArrayList();
                                            transactionDAO.setCommandMode(CommonConstants.TOSTATUS_INSERT);
                                            txMap.put(TransferTrans.CR_AC_HD, (String) acHeads.get("COMM_AC_HD"));
                                            txMap.put(TransferTrans.CR_BRANCH, branchID);
                                            txMap.put(TransferTrans.CURRENCY, "INR");
                                            txMap.put(TransferTrans.CR_PROD_TYPE, TransactionFactory.GL);
                                            txMap.put(TransferTrans.AUTHORIZE_STATUS_2, "ENTERED_AMOUNT");
                                            txMap.put("TRANS_MOD_TYPE", "GL");
                                            txMap.put(TransferTrans.PARTICULARS, "VPL Chrg LodgeID : " + objLodgementMasterTO.getLodgementId() + " Closure");
                                            //                                        txMap.put(TransferTrans.DR_INST_TYPE, objLodgementMasterTO.getBillsStatus());
                                            txMap.put("SCREEN_NAME",(String) obj.get(CommonConstants.SCREEN));// Added by nithya on 23-09-2016 for inserting screen name for transactions
                                            transactionDAO.setTransType(CommonConstants.DEBIT);
                                            transactionDAO.addTransferCredit(txMap, transAmt);
                                            transactionDAO.deleteTxList();
                                            transTO.setCommand(CommonConstants.TOSTATUS_INSERT);
                                            transTO.setTransType("CASH");
                                            transTO.setTransAmt(new Double(transAmt));
                                            transTO.setBatchId(objLodgementMasterTO.getLodgementId());
                                            transTO.setInstType(objLodgementMasterTO.getBillsStatus());
                                            cashList.add(transTO);                                            
                                            transactionDAO.addCashList(cashList);
                                            transactionDAO.doTransfer();
                                            transAmt = 0;
                                        }
                                        //INSERT INSTRUCTION
                                        HashMap instMap = new HashMap();
                                        instMap.put("LODGEMENT_ID", objLodgementMasterTO.getLodgementId());
                                        instMap.put("INSTRUCTION", objLodgementInstTO.getInstruction());
                                        List lstInst = sqlMap.executeQueryForList("getBillsExtInst", instMap);
                                        instMap = null;
                                        if (lstInst != null && lstInst.size() > 0) {
                                            sqlMap.executeUpdate("updateLodgementInstructionsTO", objLodgementInstTO);
                                        } else {
                                            sqlMap.executeUpdate("insertLodgementInstructionsTO", objLodgementInstTO);
                                        }
                                        lstInst = null;
                                    }
                                    status = null;
                                }
                            }
                        }
                    }
                }
            }
        } else if (getSubRegType().equals("ICC")) {            
            HashMap hashData = new HashMap();
            hashData.put("PROD_ID", objLodgementMasterTO.getBillsType());
            List lstData = sqlMap.executeQueryForList("getOperatingType", hashData);
            hashData = null;
            if (lstData != null && lstData.size() > 0) {
                hashData = (HashMap) lstData.get(0);
            }
            if (objLodgementMasterTO.getBillsStatus().equals("LODGEMENT")) {
                if (hashData.get("OPERATES_LIKE").equals("OUTWARD")) {
                    if (CommonUtil.convertObjToDouble(objLodgementChequeTO.getInstrumentAmount()).doubleValue() > 0) {
                        double otherBankAmt = 0;
                        for (int i = 0; i < instructionList.size(); i++) {
                            objLodgementInstTO = (LodgementInstructionsTO) instructionList.get(i);
                            String status = CommonUtil.convertObjToStr(objLodgementInstTO.getStatus());
                            if (!status.equals("DELETED")) {
                                if (objLodgementInstTO.getInstruction().equals("OTHER_BANK_CHARGES")) {
                                    otherBankAmt = CommonUtil.convertObjToDouble(objLodgementInstTO.getAmount()).doubleValue();
                                }
                                status = null;
                            }
                        }
                        double temp = 0;
                        txMap.put(TransferTrans.DR_AC_HD, (String) acHeads.get("GL_AC_HD"));
                        txMap.put(TransferTrans.DR_BRANCH, branchID);
                        txMap.put(TransferTrans.CURRENCY, "INR");
                        txMap.put(TransferTrans.DR_PROD_TYPE, TransactionFactory.GL);
                        txMap.put("TRANS_MOD_TYPE", "GL");
                        txMap.put(TransferTrans.PARTICULARS, "LodgeID : " + objLodgementMasterTO.getLodgementId() + " Lodgement");
                        txMap.put(TransferTrans.AUTHORIZE_STATUS_2, "ENTERED_AMOUNT");
                        txMap.put("SCREEN_NAME",(String) obj.get(CommonConstants.SCREEN));// Added by nithya on 23-09-2016 for inserting screen name for transactions
                        transAmt = ((CommonUtil.convertObjToDouble(objLodgementChequeTO.getInstrumentAmount()).doubleValue()) - otherBankAmt);
                        transferTo = transactionDAO.addTransferDebitLocal(txMap, transAmt);
                        transferTo.setInstType(objLodgementMasterTO.getBillsStatus());
                        transferList.add(transferTo);
                        temp = transAmt;
                        for (int i = 0; i < instructionList.size(); i++) {
                            objLodgementInstTO = (LodgementInstructionsTO) instructionList.get(i);
                            String status = CommonUtil.convertObjToStr(objLodgementInstTO.getStatus());
                            if (!status.equals("DELETED")) {
                                if (!objLodgementInstTO.getInstruction().equals("OTHER_BANK_CHARGES")) {
                                    transAmt = CommonUtil.convertObjToDouble(objLodgementInstTO.getAmount()).doubleValue();
                                    txMap.put(TransferTrans.CR_AC_HD, (String) acHeads.get("CHRG_AC_HD"));  // credit to Postage Charge account head......
                                    txMap.put(TransferTrans.PARTICULARS, "BankCharges LodgeID : " + objLodgementMasterTO.getLodgementId());
                                    txMap.put(TransferTrans.AUTHORIZE_STATUS_2, "ENTERED_AMOUNT");
                                    txMap.put("TRANS_MOD_TYPE", "GL");
                                    transferTo = transactionDAO.addTransferCreditLocal(txMap, transAmt);
                                    transferTo.setInstType(objLodgementMasterTO.getBillsStatus());
                                    transferList.add(transferTo);
                                    if (CommonUtil.convertObjToDouble(objLodgementInstTO.getServiceTx()).doubleValue() > 0) {
                                        transAmt = CommonUtil.convertObjToDouble(objLodgementInstTO.getServiceTx()).doubleValue();
                                        txMap.put(TransferTrans.CR_AC_HD, (String) acHeads.get("SERVICE_TAX_HD"));  // credit to Postage Charge account head......
                                        txMap.put(TransferTrans.PARTICULARS, "ServiceTax LodgeID : " + objLodgementMasterTO.getLodgementId() + " Lodgement");
                                        txMap.put(TransferTrans.AUTHORIZE_STATUS_2, "ENTERED_AMOUNT");
                                        txMap.put("TRANS_MOD_TYPE", "GL");
                                        txMap.put("SCREEN_NAME",(String) obj.get(CommonConstants.SCREEN));// Added by nithya on 23-09-2016 for inserting screen name for transactions
                                        transferTo = transactionDAO.addTransferCreditLocal(txMap, transAmt);
                                        transferTo.setInstType(objLodgementMasterTO.getBillsStatus());
                                        transferList.add(transferTo);
                                    }
                                    temp = temp - (CommonUtil.convertObjToDouble(objLodgementInstTO.getAmount()).doubleValue())
                                            - (CommonUtil.convertObjToDouble(objLodgementInstTO.getServiceTx()).doubleValue());

                                    //INSERT INSTRUCTION
                                    HashMap instMap = new HashMap();
                                    instMap.put("LODGEMENT_ID", objLodgementMasterTO.getLodgementId());
                                    instMap.put("INSTRUCTION", objLodgementInstTO.getInstruction());
                                    List lstInst = sqlMap.executeQueryForList("getBillsExtInst", instMap);
                                    instMap = null;
                                    if (lstInst != null && lstInst.size() > 0) {
                                        sqlMap.executeUpdate("updateLodgementInstructionsTO", objLodgementInstTO);
                                    } else {
                                        sqlMap.executeUpdate("insertLodgementInstructionsTO", objLodgementInstTO);
                                    }
                                    lstInst = null;
                                }
                                status = null;
                            }
                        }
                        if (temp > 0) {
                            transAmt = temp;
                            txMap.put(TransferTrans.CR_AC_HD, null);
                            txMap.put(TransferTrans.CR_PROD_TYPE, objLodgementMasterTO.getProdType());
                            txMap.put("TRANS_MOD_TYPE", objLodgementMasterTO.getProdType());
                            txMap.put(TransferTrans.CR_PROD_ID, objLodgementMasterTO.getBorrowProdId());
                            txMap.put(TransferTrans.CR_ACT_NUM, objLodgementMasterTO.getBorrowAcctNum());
                            txMap.put(TransferTrans.CR_BRANCH, branchID);
                            txMap.put(TransferTrans.PARTICULARS, "LodgeID : " + objLodgementMasterTO.getLodgementId() + " Lodgement");
                            txMap.put(TransferTrans.AUTHORIZE_STATUS_2, "ENTERED_AMOUNT");
                            txMap.put("SCREEN_NAME",(String) obj.get(CommonConstants.SCREEN));// Added by nithya on 23-09-2016 for inserting screen name for transactions
                            transferTo = transactionDAO.addTransferCreditLocal(txMap, transAmt);
                            transferTo.setInstType(objLodgementMasterTO.getBillsStatus());
                            transferList.add(transferTo);
                            temp = 0;
                        }
                        transAmt = 0;
                        transactionDAO.setTransactionType(transactionDAO.TRANSFER);
                        transactionDAO.doTransferLocal(transferList, branchID);
                        //OVERDUE INTEREST CODING
                        if ((CommonUtil.convertObjToDouble(objLodgementMasterTO.getRateForDelay()).doubleValue()) > 0) {
                            HashMap overDue = new HashMap();
                            overDue.put("LODGEMENT_ID", CommonUtil.convertObjToStr(objLodgementMasterTO.getLodgementId()));
                            //                        overDue.put("CUR_DATE", currDt);
                            List dueLst = sqlMap.executeQueryForList("getBillsTransPrd", overDue);
                            overDue = null;
                            if (dueLst != null && dueLst.size() > 0) {
                                overDue = (HashMap) dueLst.get(0);
                                double amount = CommonUtil.convertObjToDouble(objLodgementChequeTO.getInstrumentAmount()).doubleValue();
                                double rateOfInterest = CommonUtil.convertObjToDouble(objLodgementMasterTO.getRateForDelay()).doubleValue();
                                double period = CommonUtil.convertObjToDouble(overDue.get("TRANS_PERIOD")).doubleValue();
                                double yearDays = 36500;
                                double intAmount = (amount * rateOfInterest * period) / yearDays;
                                System.out.println("!!!amount" + amount + "##rateOfInterest" + rateOfInterest + "!!!!period" + period + "####intAmount" + intAmount);
                                intAmount = (double) getNearest((long) (intAmount * 100), 100) / 100;
                                System.out.println("####intAmount" + intAmount);
                                if (intAmount > 0) {
                                    //transaction Coding
                                    txMap = new HashMap();
                                    txMap.put(TransferTrans.DR_AC_HD, null);
                                    txMap.put(TransferTrans.DR_PROD_TYPE, objLodgementMasterTO.getProdType());
                                    txMap.put("TRANS_MOD_TYPE", objLodgementMasterTO.getProdType());
                                    txMap.put(TransferTrans.DR_PROD_ID, objLodgementMasterTO.getBorrowProdId());
                                    txMap.put(TransferTrans.DR_ACT_NUM, objLodgementMasterTO.getBorrowAcctNum());
                                    txMap.put(TransferTrans.DR_BRANCH, branchID);
                                    //                                txMap.put(TransferTrans.DR_INST_TYPE, objLodgementMasterTO.getBillsStatus());
                                    txMap.put(TransferTrans.CURRENCY, "INR");
                                    txMap.put(TransferTrans.PARTICULARS, "Int Coll LodgeID : " + objLodgementMasterTO.getLodgementId() + " Lodgement");
                                    txMap.put(TransferTrans.AUTHORIZE_STATUS_2, "ENTERED_AMOUNT");
                                    txMap.put("SCREEN_NAME",(String) obj.get(CommonConstants.SCREEN));// Added by nithya on 23-09-2016 for inserting screen name for transactions
                                    transAmt = intAmount;
                                    transferTo = transactionDAO.addTransferDebitLocal(txMap, transAmt);
                                    //                                transferTo.setAuthorizeRemarks(objLodgementMasterTO.getBillsStatus());
                                    transferTo.setInstType(objLodgementMasterTO.getBillsStatus());
                                    transferList.add(transferTo);
                                    txMap.put(TransferTrans.CR_AC_HD, (String) overDue.get("INT_AC_HD"));
                                    txMap.put(TransferTrans.CR_BRANCH, branchID);
                                    txMap.put(TransferTrans.CURRENCY, "INR");
                                    txMap.put(TransferTrans.CR_PROD_TYPE, TransactionFactory.GL);
                                    txMap.put("TRANS_MOD_TYPE", "GL");
                                    txMap.put(TransferTrans.PARTICULARS, "Int Coll LodgeID : " + objLodgementMasterTO.getLodgementId() + " Lodgement");
                                    txMap.put(TransferTrans.AUTHORIZE_STATUS_2, "ENTERED_AMOUNT");
                                    txMap.put("SCREEN_NAME",(String) obj.get(CommonConstants.SCREEN));// Added by nithya on 23-09-2016 for inserting screen name for transactions
                                    transferTo = transactionDAO.addTransferCreditLocal(txMap, transAmt);
                                    //                                transferTo.setAuthorizeRemarks(objLodgementMasterTO.getBillsStatus());
                                    transferTo.setInstType(objLodgementMasterTO.getBillsStatus());
                                    transferList.add(transferTo);
                                    transactionDAO.setTransactionType(transactionDAO.TRANSFER);
                                    transactionDAO.setForLodgeICC(true);
                                    transactionDAO.doTransferLocal(transferList, branchID);
                                    transactionDAO.setForLodgeICC(false);
                                }
                            }
                        }
                    }
                    //                trans.setForLodgementICC(false);
                }
            }
            if (objLodgementMasterTO.getBillsStatus().equals("REALIZE")) {
                if (hashData.get("OPERATES_LIKE").equals("OUTWARD")) {
                    if (CommonUtil.convertObjToDouble(objLodgementRemitTO.getRemitInstAmt()).doubleValue() > 0) {
                        double temp = 0;
                        double otherBank = 0;
                        double diffAmt = 0;
                        double remitAmt = 0;
                        double cheqAmt = 0;
                        txMap = new HashMap();
                        remitAmt = CommonUtil.convertObjToDouble(objLodgementRemitTO.getRemitInstAmt()).doubleValue();
                        cheqAmt = CommonUtil.convertObjToDouble(objLodgementChequeTO.getInstrumentAmount()).doubleValue();
                        temp = CommonUtil.convertObjToDouble(objLodgementRemitTO.getRemitFavouring()).doubleValue();
                        if ((objLodgementRemitTO.getRemitProdId().equals("PO")) || (objLodgementRemitTO.getRemitProdId().equals("DD"))) {
                            txMap.put(TransferTrans.DR_AC_HD, (String) acHeads.get("BILLS_REALISED_HD"));
                        } else if (objLodgementRemitTO.getRemitProdId().equals("IBR")) {
                            txMap.put(TransferTrans.DR_AC_HD, (String) acHeads.get("IBR_AC_HD"));
                        } else if (objLodgementRemitTO.getRemitProdId().equals("OBADV")) {
                            if (obj.containsKey("OTHER_BANK_HEAD")) {
                                txMap.put(TransferTrans.DR_AC_HD, obj.get("OTHER_BANK_HEAD"));
                            } else {
                                txMap.put(TransferTrans.DR_AC_HD, (String) acHeads.get("BILLS_REALISED_HD"));
                            }
                        }
                        txMap.put(TransferTrans.DR_BRANCH, branchID);
                        txMap.put(TransferTrans.CURRENCY, "INR");
                        txMap.put(TransferTrans.DR_PROD_TYPE, TransactionFactory.GL);
                        txMap.put("TRANS_MOD_TYPE", "GL");
                        //                txMap.put(TransferTrans.DR_INST_TYPE, objLodgementMasterTO.getBillsStatus());
                        txMap.put(TransferTrans.PARTICULARS, "LodgeID : " + objLodgementMasterTO.getLodgementId() + " Realize");
                        txMap.put(TransferTrans.AUTHORIZE_STATUS_2, "ENTERED_AMOUNT");
                        transAmt = CommonUtil.convertObjToDouble(objLodgementRemitTO.getRemitInstAmt()).doubleValue();
                        txMap.put("SCREEN_NAME",(String) obj.get(CommonConstants.SCREEN));// Added by nithya on 23-09-2016 for inserting screen name for transactions
                        transferTo = transactionDAO.addTransferDebitLocal(txMap, transAmt);
                        transferTo.setInstType(objLodgementMasterTO.getBillsStatus());
                        transferList.add(transferTo);
                        txMap.put(TransferTrans.CR_AC_HD, (String) acHeads.get("GL_AC_HD"));
                        txMap.put(TransferTrans.CR_BRANCH, branchID);
                        txMap.put(TransferTrans.CURRENCY, "INR");
                        txMap.put(TransferTrans.CR_PROD_TYPE, TransactionFactory.GL);
                        txMap.put("TRANS_MOD_TYPE", "GL");
                        txMap.put(TransferTrans.PARTICULARS, "LodgeID : " + objLodgementMasterTO.getLodgementId() + " Realize");
                        txMap.put(TransferTrans.AUTHORIZE_STATUS_2, "ENTERED_AMOUNT");
                        txMap.put("SCREEN_NAME",(String) obj.get(CommonConstants.SCREEN));// Added by nithya on 23-09-2016 for inserting screen name for transactions
                        transferTo = transactionDAO.addTransferCreditLocal(txMap, transAmt);
                        transferTo.setInstType(objLodgementMasterTO.getBillsStatus());
                        transferList.add(transferTo);
                        transactionDAO.setTransactionType(transactionDAO.TRANSFER);
                        transactionDAO.doTransferLocal(transferList, branchID);
                        for (int i = 0; i < instructionList.size(); i++) {
                            objLodgementInstTO = (LodgementInstructionsTO) instructionList.get(i);
                            String status = CommonUtil.convertObjToStr(objLodgementInstTO.getStatus());
                            if (!status.equals("DELETED")) {
                                if (objLodgementInstTO.getInstruction().equals("OTHER_BANK_CHARGES")) {
                                    objLodgementInstTO = (LodgementInstructionsTO) instructionList.get(i);
                                    otherBank = CommonUtil.convertObjToDouble(objLodgementInstTO.getAmount()).doubleValue();
                                    if (temp != otherBank) {
                                        diffAmt = otherBank - temp;
                                    }
                                }
                                status = null;
                            }
                        }
                        if (diffAmt == 0 && temp > 0) {
                            diffAmt = remitAmt - cheqAmt;
                        }
                        //EXCESS SHORT IN OTHERBANK CHARGES
                        if (diffAmt < 0) {
                            txMap = new HashMap();
                            txMap.put(TransferTrans.DR_AC_HD, null);
                            txMap.put(TransferTrans.DR_PROD_TYPE, objLodgementMasterTO.getProdType());
                            txMap.put("TRANS_MOD_TYPE", objLodgementMasterTO.getProdType());
                            txMap.put(TransferTrans.DR_PROD_ID, objLodgementMasterTO.getBorrowProdId());
                            txMap.put(TransferTrans.DR_ACT_NUM, objLodgementMasterTO.getBorrowAcctNum());
                            txMap.put(TransferTrans.DR_BRANCH, branchID);
                            txMap.put(TransferTrans.CURRENCY, "INR");
                            txMap.put(TransferTrans.PARTICULARS, "Short OtherBank Chrg LodgeID : " + objLodgementMasterTO.getLodgementId() + " Realize");
                            txMap.put(TransferTrans.AUTHORIZE_STATUS_2, "ENTERED_AMOUNT");
                            txMap.put("SCREEN_NAME",(String) obj.get(CommonConstants.SCREEN));// Added by nithya on 23-09-2016 for inserting screen name for transactions
                            transAmt = diffAmt * -1;
                            transferTo = transactionDAO.addTransferDebitLocal(txMap, transAmt);
                            transferTo.setInstType(objLodgementMasterTO.getBillsStatus());
                            transferList.add(transferTo);
                            txMap.put(TransferTrans.CR_AC_HD, (String) acHeads.get("GL_AC_HD"));
                            txMap.put(TransferTrans.CR_BRANCH, branchID);
                            txMap.put(TransferTrans.CURRENCY, "INR");
                            txMap.put(TransferTrans.CR_PROD_TYPE, TransactionFactory.GL);
                            txMap.put("TRANS_MOD_TYPE", "GL");
                            txMap.put(TransferTrans.PARTICULARS, "Short OtherBank Chrg LodgeID : " + objLodgementMasterTO.getLodgementId() + " Realize");
                            txMap.put(TransferTrans.AUTHORIZE_STATUS_2, "ENTERED_AMOUNT");
                            txMap.put("SCREEN_NAME",(String) obj.get(CommonConstants.SCREEN));// Added by nithya on 23-09-2016 for inserting screen name for transactions
                            transferTo = transactionDAO.addTransferCreditLocal(txMap, transAmt);                        
                            transferTo.setInstType(objLodgementMasterTO.getBillsStatus());
                            transferList.add(transferTo);
                            transactionDAO.setTransactionType(transactionDAO.TRANSFER);
                            transactionDAO.doTransferLocal(transferList, branchID);
                        } else if (diffAmt > 0) {
                            txMap = new HashMap();                        
                            txMap.put(TransferTrans.DR_AC_HD, (String) acHeads.get("GL_AC_HD"));                            //                            }
                            txMap.put(TransferTrans.DR_BRANCH, branchID);
                            txMap.put(TransferTrans.CURRENCY, "INR");
                            txMap.put(TransferTrans.DR_PROD_TYPE, TransactionFactory.GL);
                            txMap.put("TRANS_MOD_TYPE", "GL");                          
                            txMap.put(TransferTrans.PARTICULARS, "Excess OtherBank Chrg LodgeID : " + objLodgementMasterTO.getLodgementId() + " Realize");
                            txMap.put(TransferTrans.AUTHORIZE_STATUS_2, "ENTERED_AMOUNT");
                            txMap.put("SCREEN_NAME",(String) obj.get(CommonConstants.SCREEN));// Added by nithya on 23-09-2016 for inserting screen name for transactions
                            transAmt = diffAmt;
                            transferTo = transactionDAO.addTransferDebitLocal(txMap, transAmt);
                            transferTo.setInstType(objLodgementMasterTO.getBillsStatus());
                            transferList.add(transferTo);
                            txMap.put(TransferTrans.CR_AC_HD, null);
                            txMap.put(TransferTrans.CR_PROD_TYPE, objLodgementMasterTO.getProdType());
                            txMap.put("TRANS_MOD_TYPE", objLodgementMasterTO.getProdType());
                            txMap.put(TransferTrans.CR_PROD_ID, objLodgementMasterTO.getBorrowProdId());
                            txMap.put(TransferTrans.CR_ACT_NUM, objLodgementMasterTO.getBorrowAcctNum());
                            txMap.put(TransferTrans.CR_BRANCH, branchID);
                            txMap.put(TransferTrans.PARTICULARS, "Excess OtherBank Chrg LodgeID : " + objLodgementMasterTO.getLodgementId() + " Realize");
                            txMap.put(TransferTrans.AUTHORIZE_STATUS_2, "ENTERED_AMOUNT");
                            txMap.put("SCREEN_NAME",(String) obj.get(CommonConstants.SCREEN));// Added by nithya on 23-09-2016 for inserting screen name for transactions
                            transferTo = transactionDAO.addTransferCreditLocal(txMap, transAmt);
                            transferTo.setInstType(objLodgementMasterTO.getBillsStatus());
                            transferList.add(transferTo);
                            transactionDAO.setTransactionType(transactionDAO.TRANSFER);
                            transactionDAO.doTransferLocal(transferList, branchID);
                        } else {
                        }
                        //OVERDUE INTEREST CODING
                        if ((CommonUtil.convertObjToDouble(objLodgementMasterTO.getRateForDelay()).doubleValue()) > 0) {
                            HashMap overDue = new HashMap();
                            overDue.put("LODGEMENT_ID", CommonUtil.convertObjToStr(objLodgementMasterTO.getLodgementId()));
                            overDue.put("CUR_DATE", currDt.clone());
                            List dueLst = sqlMap.executeQueryForList("getBillsOverDueIntDetails", overDue);
                            overDue = null;
                            if (dueLst != null && dueLst.size() > 0) {
                                overDue = (HashMap) dueLst.get(0);
                                double amount = CommonUtil.convertObjToDouble(objLodgementChequeTO.getInstrumentAmount()).doubleValue();
                                double rateOfInterest = CommonUtil.convertObjToDouble(objLodgementMasterTO.getRateForDelay()).doubleValue();
                                double period = CommonUtil.convertObjToDouble(overDue.get("INT_PERIOD")).doubleValue();
                                double yearDays = 36500;
                                double intAmount = (amount * rateOfInterest * period) / yearDays;                                
                                intAmount = (double) getNearest((long) (intAmount * 100), 100) / 100;                                
                                if (intAmount > 0) {
                                    //transaction Coding
                                    txMap = new HashMap();
                                    txMap.put(TransferTrans.DR_AC_HD, null);
                                    txMap.put(TransferTrans.DR_PROD_TYPE, objLodgementMasterTO.getProdType());
                                    txMap.put("TRANS_MOD_TYPE", objLodgementMasterTO.getProdType());
                                    txMap.put(TransferTrans.DR_PROD_ID, objLodgementMasterTO.getBorrowProdId());
                                    txMap.put(TransferTrans.DR_ACT_NUM, objLodgementMasterTO.getBorrowAcctNum());
                                    txMap.put(TransferTrans.DR_BRANCH, branchID);
                                    txMap.put(TransferTrans.CURRENCY, "INR");
                                    txMap.put(TransferTrans.PARTICULARS, "Int Col(Delay) LodgeID : " + objLodgementMasterTO.getLodgementId() + " Realize");
                                    txMap.put(TransferTrans.AUTHORIZE_STATUS_2, "ENTERED_AMOUNT");
                                    txMap.put("SCREEN_NAME",(String) obj.get(CommonConstants.SCREEN));// Added by nithya on 23-09-2016 for inserting screen name for transactions
                                    transAmt = intAmount;
                                    transferTo = transactionDAO.addTransferDebitLocal(txMap, transAmt);
                                    transferTo.setInstType(objLodgementMasterTO.getBillsStatus());
                                    transferList.add(transferTo);
                                    txMap.put(TransferTrans.CR_AC_HD, (String) overDue.get("INT_AC_HD"));
                                    txMap.put(TransferTrans.CR_BRANCH, branchID);
                                    txMap.put(TransferTrans.CURRENCY, "INR");
                                    txMap.put(TransferTrans.CR_PROD_TYPE, TransactionFactory.GL);
                                    txMap.put("TRANS_MOD_TYPE", "GL");
                                    txMap.put(TransferTrans.PARTICULARS, "Int Col(Delay) LodgeID : " + objLodgementMasterTO.getLodgementId() + " Realize");
                                    txMap.put(TransferTrans.AUTHORIZE_STATUS_2, "ENTERED_AMOUNT");
                                    txMap.put("SCREEN_NAME",(String) obj.get(CommonConstants.SCREEN));// Added by nithya on 23-09-2016 for inserting screen name for transactions
                                    transferTo = transactionDAO.addTransferCreditLocal(txMap, transAmt);
                                    transferTo.setInstType(objLodgementMasterTO.getBillsStatus());
                                    transferList.add(transferTo);
                                    transactionDAO.setTransactionType(transactionDAO.TRANSFER);
                                    transactionDAO.doTransferLocal(transferList, branchID);
                                }
                            }
                        }

                    }
                }
                if (hashData.get("OPERATES_LIKE").equals("INWARD")) {
                    txMap = new HashMap();
                    if (!objLodgementMasterTO.getProdType().equals("GL")) {
                        txMap.put(TransferTrans.DR_AC_HD, null);
                        txMap.put(TransferTrans.DR_ACT_NUM, objLodgementMasterTO.getBorrowAcctNum());
                        txMap.put(TransferTrans.DR_PROD_ID, objLodgementMasterTO.getBorrowProdId());
                    } else {
                        txMap.put(TransferTrans.DR_AC_HD, objLodgementMasterTO.getBorrowAcctNum());
                    }
                    txMap.put(TransferTrans.DR_BRANCH, branchID);
                    txMap.put(TransferTrans.CURRENCY, "INR");
                    txMap.put(TransferTrans.DR_PROD_TYPE, objLodgementMasterTO.getProdType());
                    txMap.put("TRANS_MOD_TYPE", objLodgementMasterTO.getProdType());
                    txMap.put(TransferTrans.CR_PROD_TYPE, TransactionFactory.GL);
                    txMap.put(TransferTrans.CR_BRANCH, branchID);
                    transAmt = CommonUtil.convertObjToDouble(objLodgementChequeTO.getInstrumentAmount()).doubleValue();
                    txMap.put(TransferTrans.PARTICULARS, objLodgementMasterTO.getProdId());
                    txMap.put(TransferTrans.AUTHORIZE_STATUS_2, "ENTERED_AMOUNT");
                    txMap.put(TransferTrans.PARTICULARS, "LodgeID : " + objLodgementMasterTO.getLodgementId() + " Realize");
                    txMap.put("SCREEN_NAME",(String) obj.get(CommonConstants.SCREEN));// Added by nithya on 23-09-2016 for inserting screen name for transactions
                    transferTo = transactionDAO.addTransferDebitLocal(txMap, transAmt);
                    transferTo.setInstType(objLodgementMasterTO.getBillsStatus());
                    transferList.add(transferTo);
                    double tempIn = 0;
                    tempIn = CommonUtil.convertObjToDouble(objLodgementChequeTO.getInstrumentAmount()).doubleValue();
                    for (int i = 0; i < instructionList.size(); i++) {
                        objLodgementInstTO = (LodgementInstructionsTO) instructionList.get(i);
                        String status = CommonUtil.convertObjToStr(objLodgementInstTO.getStatus());
                        if (!status.equals("DELETED")) {
                            transAmt = CommonUtil.convertObjToDouble(objLodgementInstTO.getAmount()).doubleValue();
                            txMap.put(TransferTrans.CR_AC_HD, (String) acHeads.get("CHRG_AC_HD"));  // credit to Postage Charge account head......
                            txMap.put(TransferTrans.PARTICULARS, "BankCharges LodgeID : " + objLodgementMasterTO.getLodgementId() + " Realize");
                            txMap.put("TRANS_MOD_TYPE", objLodgementMasterTO.getProdType());
                            txMap.put("SCREEN_NAME",(String) obj.get(CommonConstants.SCREEN));// Added by nithya on 23-09-2016 for inserting screen name for transactions
                            transferTo = transactionDAO.addTransferCreditLocal(txMap, transAmt);
                            transferTo.setInstType(objLodgementMasterTO.getBillsStatus());
                            transferList.add(transferTo);
                            if (CommonUtil.convertObjToDouble(objLodgementInstTO.getServiceTx()).doubleValue() > 0) {
                                transAmt = CommonUtil.convertObjToDouble(objLodgementInstTO.getServiceTx()).doubleValue();
                                txMap.put(TransferTrans.CR_AC_HD, (String) acHeads.get("SERVICE_TAX_HD"));  // credit to Postage Charge account head......
                                txMap.put(TransferTrans.PARTICULARS, "ServiceTax LodgeID : " + objLodgementMasterTO.getLodgementId() + " Realize");
                                txMap.put(TransferTrans.AUTHORIZE_STATUS_2, "ENTERED_AMOUNT");
                                txMap.put("TRANS_MOD_TYPE", objLodgementMasterTO.getProdType());
                                txMap.put("SCREEN_NAME",(String) obj.get(CommonConstants.SCREEN));// Added by nithya on 23-09-2016 for inserting screen name for transactions
                                transferTo = transactionDAO.addTransferCreditLocal(txMap, transAmt);
                                transferTo.setInstType(objLodgementMasterTO.getBillsStatus());
                                transferList.add(transferTo);
                            }
                            tempIn = tempIn - (CommonUtil.convertObjToDouble(objLodgementInstTO.getAmount()).doubleValue())
                                    - (CommonUtil.convertObjToDouble(objLodgementInstTO.getServiceTx()).doubleValue());
                            //INSERT INSTRUCTION
                            HashMap instMap = new HashMap();
                            instMap.put("LODGEMENT_ID", objLodgementMasterTO.getLodgementId());
                            instMap.put("INSTRUCTION", objLodgementInstTO.getInstruction());
                            List lstInst = sqlMap.executeQueryForList("getBillsExtInst", instMap);
                            instMap = null;
                            if (lstInst != null && lstInst.size() > 0) {
                                sqlMap.executeUpdate("updateLodgementInstructionsTO", objLodgementInstTO);
                            } else {
                                sqlMap.executeUpdate("insertLodgementInstructionsTO", objLodgementInstTO);
                            }
                            lstInst = null;
                            status = null;
                        }
                    }
                    if (objLodgementRemitTO.getRemitProdId().equalsIgnoreCase("PO")) {
                        txMap.put(TransferTrans.CR_AC_HD, (String) acHeads.get("OTHER_HD")); // credit to Remittance Issue account head......
                    } else if (objLodgementRemitTO.getRemitProdId().equalsIgnoreCase("IBR")) {
                        txMap.put(TransferTrans.CR_AC_HD, (String) acHeads.get("IBR_AC_HD"));
                    } else {
                        txMap.put(TransferTrans.CR_AC_HD, (String) acHeads.get("DD_AC_HD"));
                    }
                    transAmt = tempIn;
                    txMap.put(TransferTrans.PARTICULARS, "LodgeID : " + objLodgementMasterTO.getLodgementId() + " Realize");
                    txMap.put(TransferTrans.AUTHORIZE_STATUS_2, "ENTERED_AMOUNT");
                    txMap.put("SCREEN_NAME",(String) obj.get(CommonConstants.SCREEN));// Added by nithya on 23-09-2016 for inserting screen name for transactions
                    transferTo = transactionDAO.addTransferCreditLocal(txMap, transAmt);
                    transferTo.setInstType(objLodgementMasterTO.getBillsStatus());
                    transferList.add(transferTo);
                    transAmt = 0;
                    transactionDAO.setCommandMode(command);
                    transactionDAO.setTransactionType(transactionDAO.TRANSFER);
                    transactionDAO.doTransferLocal(transferList, branchID);
                    if ((objLodgementRemitTO.getRemitProdId().equals("PO")) || (objLodgementRemitTO.getRemitProdId().equals("DD"))) {
                        TransactionTO transactionTODebit = new TransactionTO();
                        LinkedHashMap notDeleteMap = new LinkedHashMap();
                        LinkedHashMap transferMap = new LinkedHashMap();
                        HashMap remittanceMap = new HashMap();
                        remittanceIssueDAO = new RemittanceIssueDAO();
                        remittanceIssueTO = new RemittanceIssueTO();
                        String favouringName = objLodgementRemitTO.getRemitFavouringIn();
                        transactionTODebit.setApplName(favouringName);
                        transactionTODebit.setTransAmt(new Double(tempIn));
                        transactionTODebit.setProductId(objLodgementMasterTO.getBorrowProdId());
                        transactionTODebit.setProductType(objLodgementMasterTO.getProdType());
                        transactionTODebit.setDebitAcctNo(objLodgementMasterTO.getBorrowAcctNum());
                        transactionTODebit.setTransType("TRANSFER");
                        remittanceIssueDAO.setFromotherDAo(false);
                        notDeleteMap.put(String.valueOf(1), transactionTODebit);
                        transferMap.put("NOT_DELETED_TRANS_TOs", notDeleteMap);
                        remittanceMap.put("TransactionTO", transferMap);
                        remittanceMap.put(CommonConstants.BRANCH_ID, branchID);
                        remittanceMap.put("OPERATION_MODE$$$$$$$$$$$$$$$4", "ISSUE");
                        remittanceMap.put("AUTHORIZEMAP", null);
                        remittanceMap.put("USER_ID", obj.get("USER_ID"));
                        remittanceMap.put("MODULE", "Remittance");
                        remittanceMap.put("MODE", "INSERT");
                        remittanceMap.put("SCREEN", "Issue");
                        remittanceIssueTO.setDraweeBranchCode(objLodgementRemitTO.getRemitDraweeBranchCode());
                        remittanceIssueTO.setDraweeBank(objLodgementRemitTO.getRemitDraweeBank());
                        remittanceIssueTO.setAmount(new Double(tempIn));
                        remittanceIssueTO.setCategory("GENERAL_CATEGORY");
                        remittanceIssueTO.setCity(objLodgementRemitTO.getRemitCity());
                        remittanceIssueTO.setProdId(objLodgementRemitTO.getRemitProdId());
                        remittanceIssueTO.setFavouring(favouringName);
                        remittanceIssueTO.setBranchId(branchID);
                        remittanceIssueTO.setCommand("INSERT");
                        remittanceIssueTO.setTotalAmt(new Double(tempIn));
                        remittanceIssueTO.setExchange(new Double(0.0));
                        remittanceIssueTO.setPostage(new Double(0.0));
                        remittanceIssueTO.setOtherCharges(new Double(0.0));
                        remittanceIssueTO.setRemarks("FROM_BILLS_MODULE");
                        remittanceIssueTO.setStatusDt(ServerUtil.getCurrentDate(branchID));
                        remittanceIssueTO.setStatusBy(CommonUtil.convertObjToStr(obj.get("USER_ID")));
                        remittanceIssueTO.setInstrumentNo1(objLodgementRemitTO.getRemitProdId());
                        remittanceIssueTO.setInstrumentNo2("");
                        LinkedHashMap remitMap = new LinkedHashMap();
                        LinkedHashMap remMap = new LinkedHashMap();
                        remMap.put(String.valueOf(1), remittanceIssueTO);
                        remitMap.put("NOT_DELETED_ISSUE_TOs", remMap);
                        remittanceMap.put("RemittanceIssueTO", remitMap);
                        remittanceIssueDAO.execute(remittanceMap);
                        remMap = null;
                        remitMap = null;
                        remittanceMap = null;
                        notDeleteMap = null;
                        transferMap = null;
                        favouringName = null;
                    }
                    tempIn = 0;
                    objLodgementMasterTO.setTranType("TRANSFER");
                }
            }
            if (objLodgementMasterTO.getBillsStatus().equals("DISHONOUR")) {
                if (hashData.get("OPERATES_LIKE").equals("OUTWARD")) {
                    if (objLodgementMasterTO.getTranType().equals("TRANSFER")) {
                        //DEBIT 950
                        double temp = 0;
                        double otherBank = 0;
                        double diffAmt = 0;
                        txMap = new HashMap();
                        temp = CommonUtil.convertObjToDouble(objLodgementChequeTO.getInstrumentAmount()).doubleValue();
                        for (int i = 0; i < instructionList.size(); i++) {
                            objLodgementInstTO = (LodgementInstructionsTO) instructionList.get(i);
                            String status = CommonUtil.convertObjToStr(objLodgementInstTO.getStatus());
                            if (!status.equals("DELETED")) {
                                if (objLodgementInstTO.getInstruction().equals("OTHER_BANK_CHARGES")) {
                                    objLodgementInstTO = (LodgementInstructionsTO) instructionList.get(i);
                                    otherBank = otherBank + CommonUtil.convertObjToDouble(objLodgementInstTO.getAmount()).doubleValue();
                                }
                                status = null;
                            }
                        }
                        diffAmt = temp - otherBank;
                        txMap = new HashMap();
                        txMap.put(TransferTrans.DR_AC_HD, null);
                        txMap.put(TransferTrans.DR_PROD_TYPE, objLodgementMasterTO.getProdType());
                        txMap.put("TRANS_MOD_TYPE", objLodgementMasterTO.getProdType());
                        txMap.put(TransferTrans.DR_PROD_ID, objLodgementMasterTO.getBorrowProdId());
                        txMap.put(TransferTrans.DR_ACT_NUM, objLodgementMasterTO.getBorrowAcctNum());
                        txMap.put(TransferTrans.DR_BRANCH, branchID);
                        txMap.put(TransferTrans.CURRENCY, "INR");
                        txMap.put(TransferTrans.PARTICULARS, "LodgeID : " + objLodgementMasterTO.getLodgementId() + " Dishonour");
                        txMap.put(TransferTrans.AUTHORIZE_STATUS_2, "ENTERED_AMOUNT");
                        txMap.put("SCREEN_NAME",(String) obj.get(CommonConstants.SCREEN));// Added by nithya on 23-09-2016 for inserting screen name for transactions
                        transAmt = diffAmt;
                        transferTo = transactionDAO.addTransferDebitLocal(txMap, transAmt);
                        transferTo.setInstType(objLodgementMasterTO.getBillsStatus());
                        transferList.add(transferTo);
                        txMap.put(TransferTrans.CR_AC_HD, (String) acHeads.get("GL_AC_HD"));
                        txMap.put(TransferTrans.CR_BRANCH, branchID);
                        txMap.put(TransferTrans.CURRENCY, "INR");
                        txMap.put(TransferTrans.CR_PROD_TYPE, TransactionFactory.GL);
                        txMap.put(TransferTrans.PARTICULARS, "LodgeID : " + objLodgementMasterTO.getLodgementId() + " Dishonour");
                        txMap.put(TransferTrans.AUTHORIZE_STATUS_2, "ENTERED_AMOUNT");
                        txMap.put("TRANS_MOD_TYPE", "GL");
                        txMap.put("SCREEN_NAME",(String) obj.get(CommonConstants.SCREEN));// Added by nithya on 23-09-2016 for inserting screen name for transactions
                        transferTo = transactionDAO.addTransferCreditLocal(txMap, transAmt);
                        transferTo.setInstType(objLodgementMasterTO.getBillsStatus());
                        transferList.add(transferTo);
                        transactionDAO.setTransactionType(transactionDAO.TRANSFER);
                        transactionDAO.doTransferLocal(transferList, branchID);
                        //-------------------------------------------
                        if (CommonUtil.convertObjToDouble(objLodgementRemitTO.getRemitFavouring()).doubleValue() > 0) {
                            txMap = new HashMap();
                            transactionDAO.setCommandMode(CommonConstants.TOSTATUS_INSERT);                           
                            if (!objLodgementMasterTO.getProdType().equals("GL")) {
                                txMap.put(TransferTrans.DR_AC_HD, null);
                                txMap.put(TransferTrans.DR_ACT_NUM, objLodgementMasterTO.getBorrowAcctNum());
                                txMap.put(TransferTrans.DR_PROD_ID, objLodgementMasterTO.getBorrowProdId());
                            } else {
                                txMap.put(TransferTrans.DR_AC_HD, objLodgementMasterTO.getBorrowAcctNum());
                            }
                            txMap.put(TransferTrans.DR_BRANCH, branchID);
                            txMap.put(TransferTrans.CURRENCY, "INR");
                            txMap.put(TransferTrans.DR_PROD_TYPE, objLodgementMasterTO.getProdType());
                            txMap.put("TRANS_MOD_TYPE", objLodgementMasterTO.getProdType());
                            txMap.put(TransferTrans.CR_AC_HD, (String) acHeads.get("POSTAGE_AC_HD")); // credit to Remittance Issue account head......
                            txMap.put(TransferTrans.CR_BRANCH, branchID);
                            txMap.put(TransferTrans.CR_PROD_TYPE, TransactionFactory.GL);
                            transAmt = CommonUtil.convertObjToDouble(objLodgementRemitTO.getRemitFavouring()).doubleValue();
                            txMap.put(TransferTrans.PARTICULARS, objLodgementMasterTO.getBorrowProdId());
                            txMap.put(TransferTrans.PARTICULARS, "VPL Chrg LodgeID : " + objLodgementMasterTO.getLodgementId() + " Dishonour");
                            txMap.put(TransferTrans.AUTHORIZE_STATUS_2, "ENTERED_AMOUNT");
                            txMap.put("SCREEN_NAME",(String) obj.get(CommonConstants.SCREEN));// Added by nithya on 23-09-2016 for inserting screen name for transactions
                            transactionDAO.setTransType(CommonConstants.CREDIT);
                            transferTo = transactionDAO.addTransferDebitLocal(txMap, transAmt);
                            transferTo.setInstType(objLodgementMasterTO.getBillsStatus());
                            transferList.add(transferTo);
                            txMap.put(TransferTrans.PARTICULARS, "VPL Chrg LodgeID : " + objLodgementMasterTO.getLodgementId() + " Dishonour");
                            txMap.put(TransferTrans.AUTHORIZE_STATUS_2, "ENTERED_AMOUNT");
                            txMap.put("TRANS_MOD_TYPE","GL");
                            txMap.put("SCREEN_NAME",(String) obj.get(CommonConstants.SCREEN));// Added by nithya on 23-09-2016 for inserting screen name for transactions
                            transAmt = CommonUtil.convertObjToDouble(objLodgementRemitTO.getRemitFavouring()).doubleValue();
                            transactionDAO.setTransType(CommonConstants.DEBIT);
                            transferTo = transactionDAO.addTransferCreditLocal(txMap, transAmt);
                            transferTo.setInstType(objLodgementMasterTO.getBillsStatus());
                            transferList.add(transferTo);
                            transAmt = 0;
                            transactionDAO.setTransactionType(transactionDAO.TRANSFER);
                            transactionDAO.doTransferLocal(transferList, branchID);
                            //cash tran
                            transAmt = CommonUtil.convertObjToDouble(objLodgementRemitTO.getRemitFavouring()).doubleValue();
                            txMap = new HashMap();
                            TransactionTO transTO = new TransactionTO();
                            ArrayList cashList = new ArrayList();
                            transactionDAO.setCommandMode(CommonConstants.TOSTATUS_INSERT);
                            txMap.put(TransferTrans.DR_AC_HD, (String) acHeads.get("POSTAGE_AC_HD"));
                            //                            txMap.put(TransferTrans.DR_INST_TYPE, objLodgementMasterTO.getBillsStatus());
                            txMap.put(TransferTrans.DR_BRANCH, branchID);
                            txMap.put(TransferTrans.CURRENCY, "INR");
                            txMap.put(TransferTrans.DR_PROD_TYPE, TransactionFactory.GL);
                            txMap.put(TransferTrans.PARTICULARS, "LodgeID : " + objLodgementMasterTO.getLodgementId() + " Dishonour");
                            txMap.put(TransferTrans.AUTHORIZE_STATUS_2, "ENTERED_AMOUNT");
                            txMap.put("SCREEN_NAME",(String) obj.get(CommonConstants.SCREEN));// Added by nithya on 23-09-2016 for inserting screen name for transactions
                            transactionDAO.setTransType(CommonConstants.CREDIT);
                            transactionDAO.addTransferDebit(txMap, transAmt);
                            transactionDAO.deleteTxList();
                            transTO.setCommand(CommonConstants.TOSTATUS_INSERT);
                            transTO.setTransType("CASH");
                            transTO.setTransAmt(new Double(transAmt));
                            transTO.setBatchId(objLodgementMasterTO.getLodgementId());
                            transTO.setInstType(objLodgementMasterTO.getBillsStatus());
                            cashList.add(transTO);                            
                            transactionDAO.addCashList(cashList);
                            transactionDAO.doTransfer();
                        }
                        temp = 0;
                        for (int i = 0; i < instructionList.size(); i++) {
                            objLodgementInstTO = (LodgementInstructionsTO) instructionList.get(i);
                            String status = CommonUtil.convertObjToStr(objLodgementInstTO.getStatus());
                            if (!status.equals("DELETED")) {
                                if (objLodgementInstTO.getInstruction().equals("DISHONOUR_CHARGES")) {
                                    transactionDAO.setCommandMode(CommonConstants.TOSTATUS_INSERT);
                                    temp = temp + ((CommonUtil.convertObjToDouble(objLodgementInstTO.getAmount()).doubleValue())
                                            + (CommonUtil.convertObjToDouble(objLodgementInstTO.getServiceTx()).doubleValue()));
                                    if (!objLodgementMasterTO.getProdType().equals("GL")) {
                                        txMap.put(TransferTrans.DR_AC_HD, null);
                                        txMap.put(TransferTrans.DR_ACT_NUM, objLodgementMasterTO.getBorrowAcctNum());
                                        txMap.put(TransferTrans.DR_PROD_ID, objLodgementMasterTO.getBorrowProdId());
                                    } else {
                                        txMap.put(TransferTrans.DR_AC_HD, objLodgementMasterTO.getBorrowAcctNum());
                                    }
                                    txMap.put(TransferTrans.DR_BRANCH, branchID);
                                    txMap.put(TransferTrans.CURRENCY, "INR");
                                    txMap.put("TRANS_MOD_TYPE", objLodgementMasterTO.getProdType());
                                    txMap.put(TransferTrans.DR_PROD_TYPE, objLodgementMasterTO.getProdType());
                                    txMap.put(TransferTrans.CR_BRANCH, branchID);
                                    txMap.put(TransferTrans.PARTICULARS, "Dishonour Chrg LodgeID : " + objLodgementMasterTO.getLodgementId() + " Dishonour");
                                    txMap.put(TransferTrans.AUTHORIZE_STATUS_2, "ENTERED_AMOUNT");
                                    transAmt = temp;
                                    transactionDAO.setTransType(CommonConstants.CREDIT);
                                    txMap.put("SCREEN_NAME",(String) obj.get(CommonConstants.SCREEN));// Added by nithya on 23-09-2016 for inserting screen name for transactions
                                    transferTo = transactionDAO.addTransferDebitLocal(txMap, transAmt);
                                    transferTo.setInstType(objLodgementMasterTO.getBillsStatus());
                                    transferList.add(transferTo);
                                    if ((CommonUtil.convertObjToDouble(objLodgementInstTO.getAmount()).doubleValue()) > 0) {
                                        txMap.put(TransferTrans.CR_AC_HD, (String) acHeads.get("CHRG_AC_HD"));
                                        txMap.put(TransferTrans.CR_PROD_TYPE, TransactionFactory.GL);
                                        txMap.put(TransferTrans.PARTICULARS, "Dishonour Chrg LodgeID : " + objLodgementMasterTO.getLodgementId() + " Dishonour");
                                        txMap.put(TransferTrans.AUTHORIZE_STATUS_2, "ENTERED_AMOUNT");
                                        transAmt = CommonUtil.convertObjToDouble(objLodgementInstTO.getAmount()).doubleValue();
                                        transactionDAO.setTransType(CommonConstants.DEBIT);
                                        txMap.put("TRANS_MOD_TYPE","GL");
                                        txMap.put("SCREEN_NAME",(String) obj.get(CommonConstants.SCREEN));// Added by nithya on 23-09-2016 for inserting screen name for transactions
                                        transferTo = transactionDAO.addTransferCreditLocal(txMap, transAmt);
                                        transferTo.setInstType(objLodgementMasterTO.getBillsStatus());
                                        transferList.add(transferTo);
                                        transAmt = 0;
                                    }
                                    if ((CommonUtil.convertObjToDouble(objLodgementInstTO.getServiceTx()).doubleValue()) > 0) {
                                        txMap.put(TransferTrans.CR_AC_HD, (String) acHeads.get("SERVICE_TAX_HD"));
                                        txMap.put(TransferTrans.CR_PROD_TYPE, TransactionFactory.GL);
                                        txMap.put(TransferTrans.PARTICULARS, "ServiceTax LodgeID : " + objLodgementMasterTO.getLodgementId() + " Dishonour");
                                        txMap.put(TransferTrans.AUTHORIZE_STATUS_2, "ENTERED_AMOUNT");
                                        transAmt = CommonUtil.convertObjToDouble(objLodgementInstTO.getServiceTx()).doubleValue();
                                        transactionDAO.setTransType(CommonConstants.DEBIT);
                                        txMap.put("TRANS_MOD_TYPE", "GL");
                                        txMap.put("SCREEN_NAME",(String) obj.get(CommonConstants.SCREEN));// Added by nithya on 23-09-2016 for inserting screen name for transactions
                                        transferTo = transactionDAO.addTransferCreditLocal(txMap, transAmt);
                                        transferTo.setInstType(objLodgementMasterTO.getBillsStatus());
                                        transferList.add(transferTo);
                                        transAmt = 0;
                                    }
                                    transactionDAO.setTransactionType(transactionDAO.TRANSFER);
                                    transactionDAO.doTransferLocal(transferList, branchID);
                                    //INSERT INSTRUCTION
                                    HashMap instMap = new HashMap();
                                    instMap.put("LODGEMENT_ID", objLodgementMasterTO.getLodgementId());
                                    instMap.put("INSTRUCTION", objLodgementInstTO.getInstruction());
                                    List lstInst = sqlMap.executeQueryForList("getBillsExtInst", instMap);
                                    instMap = null;
                                    if (lstInst != null && lstInst.size() > 0) {
                                        sqlMap.executeUpdate("updateLodgementInstructionsTO", objLodgementInstTO);
                                    } else {
                                        sqlMap.executeUpdate("insertLodgementInstructionsTO", objLodgementInstTO);
                                    }
                                    lstInst = null;
                                }
                                status = null;
                            }
                        }
                        //OVERDUE INTEREST CODING
                        if ((CommonUtil.convertObjToDouble(objLodgementMasterTO.getRateForDelay()).doubleValue()) > 0) {
                            if (objLodgementBillRatesTO.getIntIcc().equals("Y")) {
                                HashMap overDue = new HashMap();
                                overDue.put("LODGEMENT_ID", CommonUtil.convertObjToStr(objLodgementMasterTO.getLodgementId()));
                                overDue.put("CUR_DATE", currDt.clone());
                                List dueLst = sqlMap.executeQueryForList("getBillsOverDueIntDetails", overDue);
                                overDue = null;
                                if (dueLst != null && dueLst.size() > 0) {
                                    overDue = (HashMap) dueLst.get(0);
                                    double amount = CommonUtil.convertObjToDouble(objLodgementChequeTO.getInstrumentAmount()).doubleValue();
                                    double rateOfInterest = CommonUtil.convertObjToDouble(objLodgementMasterTO.getRateForDelay()).doubleValue();
                                    double period = CommonUtil.convertObjToDouble(overDue.get("INT_PERIOD")).doubleValue();
                                    double yearDays = 36500;
                                    double intAmount = (amount * rateOfInterest * period) / yearDays;
                                    System.out.println("!!!!!!!!amount" + amount + "#####rateOfInterest" + rateOfInterest + "!!!!!!!!!period" + period + "####intAmount" + intAmount);
                                    intAmount = (double) getNearest((long) (intAmount * 100), 100) / 100;                                    
                                    if (intAmount > 0) {
                                        //transaction Coding
                                        txMap = new HashMap();
                                        txMap.put(TransferTrans.DR_AC_HD, null);
                                        txMap.put(TransferTrans.DR_PROD_TYPE, objLodgementMasterTO.getProdType());
                                        txMap.put(TransferTrans.DR_PROD_ID, objLodgementMasterTO.getBorrowProdId());
                                        txMap.put(TransferTrans.DR_ACT_NUM, objLodgementMasterTO.getBorrowAcctNum());
                                        txMap.put(TransferTrans.DR_BRANCH, branchID);
                                        txMap.put(TransferTrans.CURRENCY, "INR");
                                        txMap.put(TransferTrans.PARTICULARS, "Int Coll(Delayed) LodgeID : " + objLodgementMasterTO.getLodgementId() + " Dishonour");
                                        txMap.put(TransferTrans.AUTHORIZE_STATUS_2, "ENTERED_AMOUNT");
                                        transAmt = intAmount;
                                        txMap.put("TRANS_MOD_TYPE", objLodgementMasterTO.getProdType());
                                        txMap.put("SCREEN_NAME",(String) obj.get(CommonConstants.SCREEN));// Added by nithya on 23-09-2016 for inserting screen name for transactions
                                        transferTo = transactionDAO.addTransferDebitLocal(txMap, transAmt);
                                        transferTo.setInstType(objLodgementMasterTO.getBillsStatus());
                                        transferList.add(transferTo);
                                        txMap.put(TransferTrans.CR_AC_HD, (String) overDue.get("INT_AC_HD"));
                                        txMap.put(TransferTrans.CR_BRANCH, branchID);
                                        txMap.put(TransferTrans.CURRENCY, "INR");
                                        txMap.put(TransferTrans.CR_PROD_TYPE, TransactionFactory.GL);
                                        txMap.put("TRANS_MOD_TYPE", "GL");
                                        txMap.put(TransferTrans.PARTICULARS, "Int Coll(Delayed) LodgeID : " + objLodgementMasterTO.getLodgementId() + " Dishonour");
                                        txMap.put(TransferTrans.AUTHORIZE_STATUS_2, "ENTERED_AMOUNT");
                                        txMap.put("SCREEN_NAME",(String) obj.get(CommonConstants.SCREEN));// Added by nithya on 23-09-2016 for inserting screen name for transactions
                                        transferTo = transactionDAO.addTransferCreditLocal(txMap, transAmt);
                                        transferTo.setInstType(objLodgementMasterTO.getBillsStatus());
                                        transferList.add(transferTo);
                                        transactionDAO.setTransactionType(transactionDAO.TRANSFER);
                                        transactionDAO.doTransferLocal(transferList, branchID);
                                    }
                                }
                            } else {
                                HashMap overDue = new HashMap();
                                overDue.put("LODGEMENT_ID", CommonUtil.convertObjToStr(objLodgementMasterTO.getLodgementId()));
                                overDue.put("CUR_DATE", currDt.clone());
                                List dueLst = sqlMap.executeQueryForList("getBillsOverDueIntDetailsForICC", overDue);
                                overDue = null;
                                if (dueLst != null && dueLst.size() > 0) {
                                    overDue = (HashMap) dueLst.get(0);
                                    double amount = CommonUtil.convertObjToDouble(objLodgementChequeTO.getInstrumentAmount()).doubleValue();
                                    double rateOfInterest = CommonUtil.convertObjToDouble(objLodgementMasterTO.getRateForDelay()).doubleValue();
                                    double period = CommonUtil.convertObjToDouble(overDue.get("DIFF_DATE")).doubleValue();
                                    double yearDays = 36500;
                                    double intAmount = (amount * rateOfInterest * period) / yearDays;
                                    intAmount = (double) getNearest((long) (intAmount * 100), 100) / 100;

                                    if (intAmount > 0) {
                                        //transaction Coding
                                        txMap = new HashMap();
                                        txMap.put(TransferTrans.DR_AC_HD, null);
                                        txMap.put(TransferTrans.DR_PROD_TYPE, objLodgementMasterTO.getProdType());
                                        txMap.put(TransferTrans.DR_PROD_ID, objLodgementMasterTO.getBorrowProdId());
                                        txMap.put(TransferTrans.DR_ACT_NUM, objLodgementMasterTO.getBorrowAcctNum());
                                        txMap.put(TransferTrans.DR_BRANCH, branchID);
                                        txMap.put(TransferTrans.CURRENCY, "INR");
                                        //                                txMap.put(TransferTrans.DR_INST_TYPE, objLodgementMasterTO.getBillsStatus());
                                        txMap.put(TransferTrans.PARTICULARS, "Int Coll(Delayed) LodgeID : " + objLodgementMasterTO.getLodgementId() + " Dishonour");
                                        txMap.put(TransferTrans.AUTHORIZE_STATUS_2, "ENTERED_AMOUNT");
                                        transAmt = intAmount;
                                        txMap.put("TRANS_MOD_TYPE", objLodgementMasterTO.getProdType());
                                        txMap.put("SCREEN_NAME",(String) obj.get(CommonConstants.SCREEN));// Added by nithya on 23-09-2016 for inserting screen name for transactions
                                        transferTo = transactionDAO.addTransferDebitLocal(txMap, transAmt);
                                        transferTo.setInstType(objLodgementMasterTO.getBillsStatus());
                                        transferList.add(transferTo);
                                        txMap.put(TransferTrans.CR_AC_HD, (String) overDue.get("INT_AC_HD"));
                                        txMap.put(TransferTrans.CR_BRANCH, branchID);
                                        txMap.put(TransferTrans.CURRENCY, "INR");
                                        txMap.put(TransferTrans.CR_PROD_TYPE, TransactionFactory.GL);
                                        //                                txMap.put(TransferTrans.DR_INST_TYPE, objLodgementMasterTO.getBillsStatus());
                                        txMap.put(TransferTrans.PARTICULARS, "Int Coll(Delayed) LodgeID : " + objLodgementMasterTO.getLodgementId() + " Dishonour");
                                        txMap.put(TransferTrans.AUTHORIZE_STATUS_2, "ENTERED_AMOUNT");
                                        txMap.put("TRANS_MOD_TYPE", "GL");
                                        txMap.put("SCREEN_NAME",(String) obj.get(CommonConstants.SCREEN));// Added by nithya on 23-09-2016 for inserting screen name for transactions
                                        transferTo = transactionDAO.addTransferCreditLocal(txMap, transAmt);
                                        transferTo.setInstType(objLodgementMasterTO.getBillsStatus());
                                        transferList.add(transferTo);
                                        transactionDAO.setTransactionType(transactionDAO.TRANSFER);
                                        transactionDAO.doTransferLocal(transferList, branchID);
                                    }
                                }
                            }
                        }
                    }
                }
                if (hashData.get("OPERATES_LIKE").equals("INWARD")) {
                    //            if(objLodgementMasterTO.getBillsStatus().equals("DISHONOUR")){
                    if (objLodgementMasterTO.getTranType().equals("TRANSFER")) {
                        if (CommonUtil.convertObjToDouble(objLodgementRemitTO.getRemitFavouring()).doubleValue() > 0) {
                            txMap = new HashMap();
                            transactionDAO.setCommandMode(CommonConstants.TOSTATUS_INSERT);   
                            if (!objLodgementMasterTO.getProdType().equals("GL")) {
                                txMap.put(TransferTrans.DR_AC_HD, null);
                                txMap.put(TransferTrans.DR_ACT_NUM, objLodgementMasterTO.getBorrowAcctNum());
                                txMap.put(TransferTrans.DR_PROD_ID, objLodgementMasterTO.getBorrowProdId());
                            } else {
                                txMap.put(TransferTrans.DR_AC_HD, objLodgementMasterTO.getBorrowAcctNum());
                            }
                            txMap.put(TransferTrans.DR_BRANCH, branchID);
                            txMap.put(TransferTrans.CURRENCY, "INR");
                            txMap.put(TransferTrans.DR_PROD_TYPE, objLodgementMasterTO.getProdType());
                            txMap.put(TransferTrans.CR_AC_HD, (String) acHeads.get("POSTAGE_AC_HD")); // credit to Remittance Issue account head......
                            txMap.put(TransferTrans.CR_BRANCH, branchID);
                            txMap.put(TransferTrans.CR_PROD_TYPE, TransactionFactory.GL);
                            txMap.put(TransferTrans.PARTICULARS, "LodgeID : " + objLodgementMasterTO.getLodgementId() + " Dishonour");
                            txMap.put(TransferTrans.AUTHORIZE_STATUS_2, "ENTERED_AMOUNT");
                            transAmt = CommonUtil.convertObjToDouble(objLodgementRemitTO.getRemitFavouring()).doubleValue();
                            txMap.put(TransferTrans.PARTICULARS, objLodgementMasterTO.getBorrowProdId());
                            transactionDAO.setTransType(CommonConstants.CREDIT);
                            txMap.put("TRANS_MOD_TYPE", objLodgementMasterTO.getProdType());
                            txMap.put("SCREEN_NAME",(String) obj.get(CommonConstants.SCREEN));// Added by nithya on 23-09-2016 for inserting screen name for transactions
                            //                            txMap.put(TransferTrans.DR_INST_TYPE, objLodgementMasterTO.getBillsStatus());
                            transferTo = transactionDAO.addTransferDebitLocal(txMap, transAmt);
                            transferTo.setInstType(objLodgementMasterTO.getBillsStatus());
                            transferList.add(transferTo);
                            txMap.put(TransferTrans.PARTICULARS, "LodgeID : " + objLodgementMasterTO.getLodgementId() + " Dishonour");
                            txMap.put(TransferTrans.AUTHORIZE_STATUS_2, "ENTERED_AMOUNT");
                            transAmt = CommonUtil.convertObjToDouble(objLodgementRemitTO.getRemitFavouring()).doubleValue();
                            transactionDAO.setTransType(CommonConstants.DEBIT);
                            txMap.put("TRANS_MOD_TYPE","GL");
                            txMap.put("SCREEN_NAME",(String) obj.get(CommonConstants.SCREEN));// Added by nithya on 23-09-2016 for inserting screen name for transactions
                            //                            txMap.put(TransferTrans.DR_INST_TYPE, objLodgementMasterTO.getBillsStatus());
                            transferTo = transactionDAO.addTransferCreditLocal(txMap, transAmt);
                            transferTo.setInstType(objLodgementMasterTO.getBillsStatus());
                            transferList.add(transferTo);
                            transAmt = 0;
                            transactionDAO.setTransactionType(transactionDAO.TRANSFER);
                            transactionDAO.doTransferLocal(transferList, branchID);
                            //cash tran
                            transAmt = CommonUtil.convertObjToDouble(objLodgementRemitTO.getRemitFavouring()).doubleValue();
                            txMap = new HashMap();
                            TransactionTO transTO = new TransactionTO();
                            ArrayList cashList = new ArrayList();
                            transactionDAO.setCommandMode(CommonConstants.TOSTATUS_INSERT);
                            txMap.put(TransferTrans.DR_AC_HD, (String) acHeads.get("POSTAGE_AC_HD"));
                            txMap.put(TransferTrans.DR_BRANCH, branchID);
                            txMap.put(TransferTrans.CURRENCY, "INR");
                            txMap.put(TransferTrans.DR_PROD_TYPE, TransactionFactory.GL);
                            txMap.put("TRANS_MOD_TYPE", "GL");
                            txMap.put(TransferTrans.PARTICULARS, "LodgeID : " + objLodgementMasterTO.getLodgementId() + " Dishonour");
                            txMap.put(TransferTrans.AUTHORIZE_STATUS_2, "ENTERED_AMOUNT");
                            txMap.put("SCREEN_NAME",(String) obj.get(CommonConstants.SCREEN));// Added by nithya on 23-09-2016 for inserting screen name for transactions
                            //                            txMap.put(TransferTrans.DR_INST_TYPE, objLodgementMasterTO.getBillsStatus());
                            transactionDAO.setTransType(CommonConstants.CREDIT);
                            transactionDAO.addTransferDebit(txMap, transAmt);
                            transactionDAO.deleteTxList();
                            transTO.setCommand(CommonConstants.TOSTATUS_INSERT);
                            transTO.setTransType("CASH");
                            transTO.setTransAmt(new Double(transAmt));
                            transTO.setBatchId(objLodgementMasterTO.getLodgementId());
                            transTO.setInstType(objLodgementMasterTO.getBillsStatus());
                            cashList.add(transTO);                            
                            transactionDAO.addCashList(cashList);
                            transactionDAO.doTransfer();
                        }
                        double temp = 0;
                        for (int i = 0; i < instructionList.size(); i++) {                            
                            objLodgementInstTO = (LodgementInstructionsTO) instructionList.get(i);
                            String status = CommonUtil.convertObjToStr(objLodgementInstTO.getStatus());
                            if (!status.equals("DELETED")) {                                
                                if (objLodgementInstTO.getInstruction().equals("DISHONOUR_CHARGES")) {

                                    transactionDAO.setCommandMode(CommonConstants.TOSTATUS_INSERT);
                                    temp = temp + ((CommonUtil.convertObjToDouble(objLodgementInstTO.getAmount()).doubleValue())
                                            + (CommonUtil.convertObjToDouble(objLodgementInstTO.getServiceTx()).doubleValue()));
                                    if (!objLodgementMasterTO.getProdType().equals("GL")) {
                                        txMap.put(TransferTrans.DR_AC_HD, null);
                                        txMap.put(TransferTrans.DR_ACT_NUM, objLodgementMasterTO.getBorrowAcctNum());
                                        txMap.put(TransferTrans.DR_PROD_ID, objLodgementMasterTO.getBorrowProdId());
                                    } else {
                                        txMap.put(TransferTrans.DR_AC_HD, objLodgementMasterTO.getBorrowAcctNum());
                                    }
                                    txMap.put(TransferTrans.DR_BRANCH, branchID);
                                    txMap.put(TransferTrans.CURRENCY, "INR");
                                    txMap.put(TransferTrans.DR_PROD_TYPE, objLodgementMasterTO.getProdType());
                                    txMap.put("TRANS_MOD_TYPE", objLodgementMasterTO.getProdType());
                                    txMap.put(TransferTrans.CR_BRANCH, branchID);
                                    //                                txMap.put(TransferTrans.DR_INST_TYPE, objLodgementMasterTO.getBillsStatus());
                                    txMap.put(TransferTrans.PARTICULARS, "Dishonour Chrg LodgeID : " + objLodgementMasterTO.getLodgementId() + " Dishonour");
                                    txMap.put(TransferTrans.AUTHORIZE_STATUS_2, "ENTERED_AMOUNT");
                                    transAmt = temp;
                                    transactionDAO.setTransType(CommonConstants.CREDIT);
                                    txMap.put("SCREEN_NAME",(String) obj.get(CommonConstants.SCREEN));// Added by nithya on 23-09-2016 for inserting screen name for transactions
                                    transferTo = transactionDAO.addTransferDebitLocal(txMap, transAmt);
                                    transferTo.setInstType(objLodgementMasterTO.getBillsStatus());
                                    transferList.add(transferTo);
                                    if ((CommonUtil.convertObjToDouble(objLodgementInstTO.getAmount()).doubleValue()) > 0) {
                                        txMap.put(TransferTrans.CR_AC_HD, (String) acHeads.get("CHRG_AC_HD"));
                                        txMap.put(TransferTrans.CR_PROD_TYPE, TransactionFactory.GL);
                                        //                                    txMap.put(TransferTrans.DR_INST_TYPE, objLodgementMasterTO.getBillsStatus());
                                        txMap.put(TransferTrans.PARTICULARS, "Dishonour Chrg LodgeID : " + objLodgementMasterTO.getLodgementId() + " Dishonour");
                                        txMap.put(TransferTrans.AUTHORIZE_STATUS_2, "ENTERED_AMOUNT");
                                        transAmt = CommonUtil.convertObjToDouble(objLodgementInstTO.getAmount()).doubleValue();
                                        transactionDAO.setTransType(CommonConstants.DEBIT);
                                        txMap.put("TRANS_MOD_TYPE", "GL");
                                        txMap.put("SCREEN_NAME",(String) obj.get(CommonConstants.SCREEN));// Added by nithya on 23-09-2016 for inserting screen name for transactions
                                        transferTo = transactionDAO.addTransferCreditLocal(txMap, transAmt);
                                        transferTo.setInstType(objLodgementMasterTO.getBillsStatus());
                                        transferList.add(transferTo);
                                        transAmt = 0;
                                    }
                                    if ((CommonUtil.convertObjToDouble(objLodgementInstTO.getServiceTx()).doubleValue()) > 0) {
                                        txMap.put(TransferTrans.CR_AC_HD, (String) acHeads.get("SERVICE_TAX_HD"));
                                        txMap.put(TransferTrans.CR_PROD_TYPE, TransactionFactory.GL);
                                        //                                    txMap.put(TransferTrans.DR_INST_TYPE, objLodgementMasterTO.getBillsStatus());
                                        txMap.put(TransferTrans.PARTICULARS, "ServiceTax LodgeID : " + objLodgementMasterTO.getLodgementId() + " Dishonour");
                                        txMap.put(TransferTrans.AUTHORIZE_STATUS_2, "ENTERED_AMOUNT");
                                        transAmt = CommonUtil.convertObjToDouble(objLodgementInstTO.getServiceTx()).doubleValue();
                                        transactionDAO.setTransType(CommonConstants.DEBIT);
                                        txMap.put("TRANS_MOD_TYPE", "GL");
                                        txMap.put("SCREEN_NAME",(String) obj.get(CommonConstants.SCREEN));// Added by nithya on 23-09-2016 for inserting screen name for transactions
                                        transferTo = transactionDAO.addTransferCreditLocal(txMap, transAmt);
                                        transferTo.setInstType(objLodgementMasterTO.getBillsStatus());
                                        transferList.add(transferTo);
                                        transAmt = 0;
                                    }
                                    transactionDAO.setTransactionType(transactionDAO.TRANSFER);
                                    transactionDAO.doTransferLocal(transferList, branchID);

                                    //INSERT INSTRUCTION
                                    HashMap instMap = new HashMap();
                                    instMap.put("LODGEMENT_ID", objLodgementMasterTO.getLodgementId());
                                    instMap.put("INSTRUCTION", objLodgementInstTO.getInstruction());
                                    List lstInst = sqlMap.executeQueryForList("getBillsExtInst", instMap);
                                    instMap = null;
                                    if (lstInst != null && lstInst.size() > 0) {
                                        sqlMap.executeUpdate("updateLodgementInstructionsTO", objLodgementInstTO);
                                    } else {
                                        sqlMap.executeUpdate("insertLodgementInstructionsTO", objLodgementInstTO);
                                    }
                                    lstInst = null;
                                }
                                status = null;
                            }
                        }
                    }
                    if (objLodgementMasterTO.getTranType().equals("CASH")) {
                        if (CommonUtil.convertObjToDouble(objLodgementRemitTO.getRemitFavouring()).doubleValue() > 0) {
                            //cash tran credit
                            transAmt = CommonUtil.convertObjToDouble(objLodgementRemitTO.getRemitFavouring()).doubleValue();
                            txMap = new HashMap();
                            TransactionTO transTO = new TransactionTO();
                            ArrayList cashList = new ArrayList();
                            transactionDAO.setCommandMode(CommonConstants.TOSTATUS_INSERT);
                            txMap.put(TransferTrans.CR_AC_HD, (String) acHeads.get("POSTAGE_AC_HD"));
                            txMap.put(TransferTrans.CR_BRANCH, branchID);
                            txMap.put(TransferTrans.CURRENCY, "INR");
                            txMap.put(TransferTrans.CR_PROD_TYPE, TransactionFactory.GL);
                            txMap.put("TRANS_MOD_TYPE", "GL");
                            //                                txMap.put(TransferTrans.DR_INST_TYPE, objLodgementMasterTO.getBillsStatus());
                            txMap.put(TransferTrans.PARTICULARS, "LodgeID : " + objLodgementMasterTO.getLodgementId() + " Dishonour");
                            txMap.put(TransferTrans.AUTHORIZE_STATUS_2, "ENTERED_AMOUNT");
                            txMap.put("SCREEN_NAME",(String) obj.get(CommonConstants.SCREEN));// Added by nithya on 23-09-2016 for inserting screen name for transactions
                            transactionDAO.setTransType(CommonConstants.DEBIT);
                            transactionDAO.addTransferCredit(txMap, transAmt);
                            transactionDAO.deleteTxList();
                            transTO.setCommand(CommonConstants.TOSTATUS_INSERT);
                            transTO.setTransType("CASH");
                            transTO.setTransAmt(new Double(transAmt));
                            transTO.setBatchId(objLodgementMasterTO.getLodgementId());
                            transTO.setInstType(objLodgementMasterTO.getBillsStatus());
                            cashList.add(transTO);                            
                            transactionDAO.addCashList(cashList);
                            transactionDAO.doTransfer();
                            //cash tran debit
                            transAmt = CommonUtil.convertObjToDouble(objLodgementRemitTO.getRemitFavouring()).doubleValue();
                            txMap = new HashMap();
                            transTO = new TransactionTO();
                            cashList = new ArrayList();
                            transactionDAO.setCommandMode(CommonConstants.TOSTATUS_INSERT);
                            txMap.put(TransferTrans.DR_AC_HD, (String) acHeads.get("POSTAGE_AC_HD"));
                            txMap.put(TransferTrans.DR_BRANCH, branchID);
                            txMap.put(TransferTrans.CURRENCY, "INR");
                            txMap.put(TransferTrans.DR_PROD_TYPE, TransactionFactory.GL);
                            txMap.put("TRANS_MOD_TYPE", "GL");
                            //                                txMap.put(TransferTrans.DR_INST_TYPE, objLodgementMasterTO.getBillsStatus());
                            txMap.put(TransferTrans.PARTICULARS, "LodgeID : " + objLodgementMasterTO.getLodgementId() + " Dishonour");
                            txMap.put(TransferTrans.AUTHORIZE_STATUS_2, "ENTERED_AMOUNT");
                            txMap.put("SCREEN_NAME",(String) obj.get(CommonConstants.SCREEN));// Added by nithya on 23-09-2016 for inserting screen name for transactions
                            transactionDAO.setTransType(CommonConstants.CREDIT);
                            transactionDAO.addTransferDebit(txMap, transAmt);
                            transactionDAO.deleteTxList();
                            transTO.setCommand(CommonConstants.TOSTATUS_INSERT);
                            transTO.setTransType("CASH");
                            transTO.setTransAmt(new Double(transAmt));
                            transTO.setBatchId(objLodgementMasterTO.getLodgementId());
                            transTO.setInstType(objLodgementMasterTO.getBillsStatus());
                            cashList.add(transTO);                            
                            transactionDAO.addCashList(cashList);
                            transactionDAO.doTransfer();
                        }

                        //TRANSFER TRAN
                        double temp = 0;
                        for (int i = 0; i < instructionList.size(); i++) {
                            objLodgementInstTO = (LodgementInstructionsTO) instructionList.get(i);
                            String status = CommonUtil.convertObjToStr(objLodgementInstTO.getStatus());
                            if (!status.equals("DELETED")) {
                                if (objLodgementInstTO.getInstruction().equals("DISHONOUR_CHARGES")) {
                                    transAmt = CommonUtil.convertObjToDouble(objLodgementInstTO.getAmount()).doubleValue();
                                    if (transAmt > 0) {
                                        txMap = new HashMap();
                                        TransactionTO transTO = new TransactionTO();
                                        ArrayList cashList = new ArrayList();
                                        transactionDAO.setCommandMode(CommonConstants.TOSTATUS_INSERT);
                                        txMap.put(TransferTrans.CR_AC_HD, (String) acHeads.get("CHRG_AC_HD"));
                                        txMap.put(TransferTrans.CR_BRANCH, branchID);
                                        txMap.put(TransferTrans.CURRENCY, "INR");
                                        txMap.put(TransferTrans.CR_PROD_TYPE, TransactionFactory.GL);
                                        txMap.put("TRANS_MOD_TYPE", "GL");
                                        //                                        txMap.put(TransferTrans.DR_INST_TYPE, objLodgementMasterTO.getBillsStatus());
                                        txMap.put(TransferTrans.PARTICULARS, "LodgeID : " + objLodgementMasterTO.getLodgementId() + " Dishonour");
                                        txMap.put(TransferTrans.AUTHORIZE_STATUS_2, "ENTERED_AMOUNT");
                                        txMap.put("SCREEN_NAME",(String) obj.get(CommonConstants.SCREEN));// Added by nithya on 23-09-2016 for inserting screen name for transactions
                                        transactionDAO.setTransType(CommonConstants.DEBIT);
                                        transactionDAO.addTransferCredit(txMap, transAmt);
                                        transactionDAO.deleteTxList();
                                        transTO.setCommand(CommonConstants.TOSTATUS_INSERT);
                                        transTO.setTransType("CASH");
                                        transTO.setTransAmt(new Double(transAmt));
                                        transTO.setBatchId(objLodgementMasterTO.getLodgementId());
                                        transTO.setInstType(objLodgementMasterTO.getBillsStatus());
                                        cashList.add(transTO);
                                        System.out.println("@@@@@@f" + cashList);
                                        transactionDAO.addCashList(cashList);
                                        transactionDAO.doTransfer();
                                        transAmt = 0;
                                    }
                                    transAmt = CommonUtil.convertObjToDouble(objLodgementInstTO.getServiceTx()).doubleValue();
                                    if (transAmt > 0) {
                                        txMap = new HashMap();
                                        TransactionTO transTO = new TransactionTO();
                                        ArrayList cashList = new ArrayList();
                                        transactionDAO.setCommandMode(CommonConstants.TOSTATUS_INSERT);
                                        txMap.put(TransferTrans.CR_AC_HD, (String) acHeads.get("COMM_AC_HD"));
                                        txMap.put(TransferTrans.CR_BRANCH, branchID);
                                        txMap.put(TransferTrans.CURRENCY, "INR");
                                        txMap.put(TransferTrans.CR_PROD_TYPE, TransactionFactory.GL);
                                        txMap.put("TRANS_MOD_TYPE", "GL");
                                        //                                        txMap.put(TransferTrans.DR_INST_TYPE, objLodgementMasterTO.getBillsStatus());
                                        txMap.put(TransferTrans.PARTICULARS, "LodgeID : " + objLodgementMasterTO.getLodgementId() + " Dishonour");
                                        txMap.put(TransferTrans.AUTHORIZE_STATUS_2, "ENTERED_AMOUNT");
                                        txMap.put("SCREEN_NAME",(String) obj.get(CommonConstants.SCREEN));// Added by nithya on 23-09-2016 for inserting screen name for transactions
                                        transactionDAO.setTransType(CommonConstants.DEBIT);
                                        transactionDAO.addTransferCredit(txMap, transAmt);
                                        transactionDAO.deleteTxList();
                                        transTO.setCommand(CommonConstants.TOSTATUS_INSERT);
                                        transTO.setTransType("CASH");
                                        transTO.setTransAmt(new Double(transAmt));
                                        transTO.setBatchId(objLodgementMasterTO.getLodgementId());
                                        transTO.setInstType(objLodgementMasterTO.getBillsStatus());
                                        cashList.add(transTO);
                                        System.out.println("@@@@@@f" + cashList);
                                        transactionDAO.addCashList(cashList);
                                        transactionDAO.doTransfer();
                                        transAmt = 0;
                                    }
                                    //INSERT INSTRUCTION
                                    HashMap instMap = new HashMap();
                                    instMap.put("LODGEMENT_ID", objLodgementMasterTO.getLodgementId());
                                    instMap.put("INSTRUCTION", objLodgementInstTO.getInstruction());
                                    List lstInst = sqlMap.executeQueryForList("getBillsExtInst", instMap);
                                    instMap = null;
                                    if (lstInst != null && lstInst.size() > 0) {
                                        sqlMap.executeUpdate("updateLodgementInstructionsTO", objLodgementInstTO);
                                    } else {
                                        sqlMap.executeUpdate("insertLodgementInstructionsTO", objLodgementInstTO);
                                    }
                                    lstInst = null;
                                }
                                status = null;
                            }
                        }
                    }
                    //                }
                }
            }
            if (objLodgementMasterTO.getBillsStatus().equals("CLOSURE")) {
                if (hashData.get("OPERATES_LIKE").equals("OUTWARD")) {
                    if (objLodgementMasterTO.getTranType().equals("TRANSFER")) {
                        //DEBIT 950
                        double temp = 0;
                        double otherBank = 0;
                        double diffAmt = 0;
                        txMap = new HashMap();
                        temp = CommonUtil.convertObjToDouble(objLodgementChequeTO.getInstrumentAmount()).doubleValue();
                        for (int i = 0; i < instructionList.size(); i++) {
                            objLodgementInstTO = (LodgementInstructionsTO) instructionList.get(i);
                            String status = CommonUtil.convertObjToStr(objLodgementInstTO.getStatus());
                            if (!status.equals("DELETED")) {
                                if (objLodgementInstTO.getInstruction().equals("OTHER_BANK_CHARGES")) {
                                    objLodgementInstTO = (LodgementInstructionsTO) instructionList.get(i);
                                    otherBank = otherBank + CommonUtil.convertObjToDouble(objLodgementInstTO.getAmount()).doubleValue();
                                }
                                status = null;
                            }
                        }
                        diffAmt = temp - otherBank;
                        txMap = new HashMap();
                        txMap.put(TransferTrans.DR_AC_HD, null);
                        txMap.put(TransferTrans.DR_PROD_TYPE, objLodgementMasterTO.getProdType());
                        txMap.put(TransferTrans.DR_PROD_ID, objLodgementMasterTO.getBorrowProdId());
                        txMap.put(TransferTrans.DR_ACT_NUM, objLodgementMasterTO.getBorrowAcctNum());
                        txMap.put(TransferTrans.DR_BRANCH, branchID);
                        txMap.put(TransferTrans.CURRENCY, "INR");
                        //                                txMap.put(TransferTrans.DR_INST_TYPE, objLodgementMasterTO.getBillsStatus());
                        txMap.put(TransferTrans.PARTICULARS, "LodgeID : " + objLodgementMasterTO.getLodgementId() + " Closure");
                        txMap.put(TransferTrans.AUTHORIZE_STATUS_2, "ENTERED_AMOUNT");
                        transAmt = diffAmt;
                        txMap.put("TRANS_MOD_TYPE", objLodgementMasterTO.getProdType());
                        txMap.put("SCREEN_NAME",(String) obj.get(CommonConstants.SCREEN));// Added by nithya on 23-09-2016 for inserting screen name for transactions
                        transferTo = transactionDAO.addTransferDebitLocal(txMap, transAmt);
                        transferTo.setInstType(objLodgementMasterTO.getBillsStatus());
                        transferList.add(transferTo);
                        txMap.put(TransferTrans.CR_AC_HD, (String) acHeads.get("GL_AC_HD"));
                        txMap.put(TransferTrans.CR_BRANCH, branchID);
                        txMap.put(TransferTrans.CURRENCY, "INR");
                        txMap.put(TransferTrans.CR_PROD_TYPE, TransactionFactory.GL);
                        txMap.put(TransferTrans.PARTICULARS, "LodgeID : " + objLodgementMasterTO.getLodgementId() + " Closure");
                        txMap.put(TransferTrans.AUTHORIZE_STATUS_2, "ENTERED_AMOUNT");
                        txMap.put("TRANS_MOD_TYPE", "GL");
                        txMap.put("SCREEN_NAME",(String) obj.get(CommonConstants.SCREEN));// Added by nithya on 23-09-2016 for inserting screen name for transactions
                        transferTo = transactionDAO.addTransferCreditLocal(txMap, transAmt);
                        transferTo.setInstType(objLodgementMasterTO.getBillsStatus());
                        transferList.add(transferTo);
                        transactionDAO.setTransactionType(transactionDAO.TRANSFER);
                        transactionDAO.doTransferLocal(transferList, branchID);
                        //-------------------------------------------
                        if (CommonUtil.convertObjToDouble(objLodgementRemitTO.getRemitFavouring()).doubleValue() > 0) {
                            txMap = new HashMap();
                            transactionDAO.setCommandMode(CommonConstants.TOSTATUS_INSERT);
                            if (!objLodgementMasterTO.getProdType().equals("GL")) {
                                txMap.put(TransferTrans.DR_AC_HD, null);
                                txMap.put(TransferTrans.DR_ACT_NUM, objLodgementMasterTO.getBorrowAcctNum());
                                txMap.put(TransferTrans.DR_PROD_ID, objLodgementMasterTO.getBorrowProdId());
                            } else {
                                txMap.put(TransferTrans.DR_AC_HD, objLodgementMasterTO.getBorrowAcctNum());
                            }
                            txMap.put(TransferTrans.DR_BRANCH, branchID);
                            txMap.put(TransferTrans.CURRENCY, "INR");
                            txMap.put(TransferTrans.DR_PROD_TYPE, objLodgementMasterTO.getProdType());
                            txMap.put("TRANS_MOD_TYPE", objLodgementMasterTO.getProdType());
                            txMap.put(TransferTrans.CR_AC_HD, (String) acHeads.get("POSTAGE_AC_HD")); // credit to Remittance Issue account head......
                            txMap.put(TransferTrans.CR_BRANCH, branchID);
                            txMap.put(TransferTrans.CR_PROD_TYPE, TransactionFactory.GL);
                            transAmt = CommonUtil.convertObjToDouble(objLodgementRemitTO.getRemitFavouring()).doubleValue();
                            txMap.put(TransferTrans.PARTICULARS, objLodgementMasterTO.getBorrowProdId());
                            txMap.put(TransferTrans.PARTICULARS, "VPL Chrg LodgeID : " + objLodgementMasterTO.getLodgementId() + " Closure");
                            txMap.put(TransferTrans.AUTHORIZE_STATUS_2, "ENTERED_AMOUNT");
                            transactionDAO.setTransType(CommonConstants.CREDIT);
                            txMap.put("SCREEN_NAME",(String) obj.get(CommonConstants.SCREEN));// Added by nithya on 23-09-2016 for inserting screen name for transactions
                            transferTo = transactionDAO.addTransferDebitLocal(txMap, transAmt);
                            transferTo.setInstType(objLodgementMasterTO.getBillsStatus());
                            transferList.add(transferTo);
                            txMap.put(TransferTrans.PARTICULARS, "VPL Chrg LodgeID : " + objLodgementMasterTO.getLodgementId() + " Closure");
                            txMap.put(TransferTrans.AUTHORIZE_STATUS_2, "ENTERED_AMOUNT");
                            transAmt = CommonUtil.convertObjToDouble(objLodgementRemitTO.getRemitFavouring()).doubleValue();
                            transactionDAO.setTransType(CommonConstants.DEBIT);
                            txMap.put("TRANS_MOD_TYPE", "GL");
                            txMap.put("SCREEN_NAME",(String) obj.get(CommonConstants.SCREEN));// Added by nithya on 23-09-2016 for inserting screen name for transactions
                            transferTo = transactionDAO.addTransferCreditLocal(txMap, transAmt);
                            transferTo.setInstType(objLodgementMasterTO.getBillsStatus());
                            transferList.add(transferTo);
                            transAmt = 0;
                            transactionDAO.setTransactionType(transactionDAO.TRANSFER);
                            transactionDAO.doTransferLocal(transferList, branchID);
                            //cash tran
                            transAmt = CommonUtil.convertObjToDouble(objLodgementRemitTO.getRemitFavouring()).doubleValue();
                            txMap = new HashMap();
                            TransactionTO transTO = new TransactionTO();
                            ArrayList cashList = new ArrayList();
                            transactionDAO.setCommandMode(CommonConstants.TOSTATUS_INSERT);
                            txMap.put(TransferTrans.DR_AC_HD, (String) acHeads.get("POSTAGE_AC_HD"));
                            txMap.put(TransferTrans.DR_BRANCH, branchID);
                            txMap.put(TransferTrans.CURRENCY, "INR");
                            txMap.put(TransferTrans.DR_PROD_TYPE, TransactionFactory.GL);
                            txMap.put("TRANS_MOD_TYPE", "GL");
                            txMap.put(TransferTrans.PARTICULARS, "LodgeID : " + objLodgementMasterTO.getLodgementId() + " Closure");
                            txMap.put(TransferTrans.AUTHORIZE_STATUS_2, "ENTERED_AMOUNT");
                            txMap.put("SCREEN_NAME",(String) obj.get(CommonConstants.SCREEN));// Added by nithya on 23-09-2016 for inserting screen name for transactions
                            transactionDAO.setTransType(CommonConstants.CREDIT);
                            transactionDAO.addTransferDebit(txMap, transAmt);
                            transactionDAO.deleteTxList();
                            transTO.setCommand(CommonConstants.TOSTATUS_INSERT);
                            transTO.setTransType("CASH");
                            transTO.setTransAmt(new Double(transAmt));
                            transTO.setBatchId(objLodgementMasterTO.getLodgementId());
                            transTO.setInstType(objLodgementMasterTO.getBillsStatus());
                            cashList.add(transTO);                            
                            transactionDAO.addCashList(cashList);
                            transactionDAO.doTransfer();
                        }
                        temp = 0;
                        for (int i = 0; i < instructionList.size(); i++) {
                            objLodgementInstTO = (LodgementInstructionsTO) instructionList.get(i);
                            String status = CommonUtil.convertObjToStr(objLodgementInstTO.getStatus());
                            if (!status.equals("DELETED")) {
                                if (objLodgementInstTO.getInstruction().equals("CLOSURE_CHARGES")) {
                                    transactionDAO.setCommandMode(CommonConstants.TOSTATUS_INSERT);
                                    temp = temp + ((CommonUtil.convertObjToDouble(objLodgementInstTO.getAmount()).doubleValue())
                                            + (CommonUtil.convertObjToDouble(objLodgementInstTO.getServiceTx()).doubleValue()));
                                    if (!objLodgementMasterTO.getProdType().equals("GL")) {
                                        txMap.put(TransferTrans.DR_AC_HD, null);
                                        txMap.put(TransferTrans.DR_ACT_NUM, objLodgementMasterTO.getBorrowAcctNum());
                                        txMap.put(TransferTrans.DR_PROD_ID, objLodgementMasterTO.getBorrowProdId());
                                    } else {
                                        txMap.put(TransferTrans.DR_AC_HD, objLodgementMasterTO.getBorrowAcctNum());
                                    }
                                    txMap.put(TransferTrans.DR_BRANCH, branchID);
                                    txMap.put(TransferTrans.CURRENCY, "INR");
                                    txMap.put(TransferTrans.DR_PROD_TYPE, objLodgementMasterTO.getProdType());
                                    txMap.put(TransferTrans.CR_BRANCH, branchID);
                                    txMap.put(TransferTrans.PARTICULARS, "Closure Chrg LodgeID : " + objLodgementMasterTO.getLodgementId() + " Closure");
                                    txMap.put(TransferTrans.AUTHORIZE_STATUS_2, "ENTERED_AMOUNT");
                                    transAmt = temp;
                                    txMap.put("TRANS_MOD_TYPE", objLodgementMasterTO.getProdType());
                                    transactionDAO.setTransType(CommonConstants.CREDIT);
                                    txMap.put("SCREEN_NAME",(String) obj.get(CommonConstants.SCREEN));// Added by nithya on 23-09-2016 for inserting screen name for transactions
                                    transferTo = transactionDAO.addTransferDebitLocal(txMap, transAmt);
                                    transferTo.setInstType(objLodgementMasterTO.getBillsStatus());
                                    transferList.add(transferTo);
                                    if ((CommonUtil.convertObjToDouble(objLodgementInstTO.getAmount()).doubleValue()) > 0) {
                                        txMap.put(TransferTrans.CR_AC_HD, (String) acHeads.get("CHRG_AC_HD"));
                                        txMap.put(TransferTrans.CR_PROD_TYPE, TransactionFactory.GL);
                                        txMap.put(TransferTrans.PARTICULARS, "Closure Chrg LodgeID : " + objLodgementMasterTO.getLodgementId() + " Closure");
                                        txMap.put(TransferTrans.AUTHORIZE_STATUS_2, "ENTERED_AMOUNT");
                                        transAmt = CommonUtil.convertObjToDouble(objLodgementInstTO.getAmount()).doubleValue();
                                        transactionDAO.setTransType(CommonConstants.DEBIT);
                                        txMap.put("TRANS_MOD_TYPE", "GL");
                                        txMap.put("SCREEN_NAME",(String) obj.get(CommonConstants.SCREEN));// Added by nithya on 23-09-2016 for inserting screen name for transactions
                                        transferTo = transactionDAO.addTransferCreditLocal(txMap, transAmt);
                                        transferTo.setInstType(objLodgementMasterTO.getBillsStatus());
                                        transferList.add(transferTo);
                                        transAmt = 0;
                                    }
                                    if ((CommonUtil.convertObjToDouble(objLodgementInstTO.getServiceTx()).doubleValue()) > 0) {
                                        txMap.put(TransferTrans.CR_AC_HD, (String) acHeads.get("SERVICE_TAX_HD"));
                                        txMap.put(TransferTrans.CR_PROD_TYPE, TransactionFactory.GL);
                                        txMap.put(TransferTrans.PARTICULARS, "ServiceTax LodgeID : " + objLodgementMasterTO.getLodgementId() + " Closure");
                                        txMap.put(TransferTrans.AUTHORIZE_STATUS_2, "ENTERED_AMOUNT");
                                        transAmt = CommonUtil.convertObjToDouble(objLodgementInstTO.getServiceTx()).doubleValue();
                                        transactionDAO.setTransType(CommonConstants.DEBIT);
                                        txMap.put("TRANS_MOD_TYPE", "GL");
                                        txMap.put("SCREEN_NAME",(String) obj.get(CommonConstants.SCREEN));// Added by nithya on 23-09-2016 for inserting screen name for transactions
                                        transferTo = transactionDAO.addTransferCreditLocal(txMap, transAmt);
                                        transferTo.setInstType(objLodgementMasterTO.getBillsStatus());
                                        transferList.add(transferTo);
                                        transAmt = 0;
                                    }
                                    transactionDAO.setTransactionType(transactionDAO.TRANSFER);
                                    transactionDAO.doTransferLocal(transferList, branchID);

                                    //INSERT INSTRUCTION
                                    HashMap instMap = new HashMap();
                                    instMap.put("LODGEMENT_ID", objLodgementMasterTO.getLodgementId());
                                    instMap.put("INSTRUCTION", objLodgementInstTO.getInstruction());
                                    List lstInst = sqlMap.executeQueryForList("getBillsExtInst", instMap);
                                    instMap = null;
                                    if (lstInst != null && lstInst.size() > 0) {
                                        sqlMap.executeUpdate("updateLodgementInstructionsTO", objLodgementInstTO);
                                    } else {
                                        sqlMap.executeUpdate("insertLodgementInstructionsTO", objLodgementInstTO);
                                    }
                                    lstInst = null;
                                }
                                status = null;
                            }
                        }
                        //OVERDUE INTEREST CODING
                        if ((CommonUtil.convertObjToDouble(objLodgementMasterTO.getRateForDelay()).doubleValue()) > 0) {
                            HashMap overDue = new HashMap();
                            overDue.put("LODGEMENT_ID", CommonUtil.convertObjToStr(objLodgementMasterTO.getLodgementId()));
                            overDue.put("CUR_DATE", currDt.clone());
                            List dueLst = sqlMap.executeQueryForList("getBillsOverDueIntDetails", overDue);
                            overDue = null;
                            if (dueLst != null && dueLst.size() > 0) {
                                overDue = (HashMap) dueLst.get(0);
                                double amount = CommonUtil.convertObjToDouble(objLodgementChequeTO.getInstrumentAmount()).doubleValue();
                                double rateOfInterest = CommonUtil.convertObjToDouble(objLodgementMasterTO.getRateForDelay()).doubleValue();
                                double period = CommonUtil.convertObjToDouble(overDue.get("INT_PERIOD")).doubleValue();
                                double yearDays = 36500;
                                double intAmount = (amount * rateOfInterest * period) / yearDays;                              
                                intAmount = (double) getNearest((long) (intAmount * 100), 100) / 100;
                                if (intAmount > 0) {
                                    //transaction Coding
                                    txMap = new HashMap();
                                    txMap.put(TransferTrans.DR_AC_HD, null);
                                    txMap.put(TransferTrans.DR_PROD_TYPE, objLodgementMasterTO.getProdType());
                                    txMap.put(TransferTrans.DR_PROD_ID, objLodgementMasterTO.getBorrowProdId());
                                    txMap.put(TransferTrans.DR_ACT_NUM, objLodgementMasterTO.getBorrowAcctNum());
                                    txMap.put(TransferTrans.DR_BRANCH, branchID);
                                    txMap.put(TransferTrans.CURRENCY, "INR");
                                    txMap.put(TransferTrans.PARTICULARS, "Int Coll(Delayed) LodgeID : " + objLodgementMasterTO.getLodgementId() + " Closure");
                                    txMap.put(TransferTrans.AUTHORIZE_STATUS_2, "ENTERED_AMOUNT");
                                    transAmt = intAmount;
                                    txMap.put("TRANS_MOD_TYPE", objLodgementMasterTO.getProdType());
                                    txMap.put("SCREEN_NAME",(String) obj.get(CommonConstants.SCREEN));// Added by nithya on 23-09-2016 for inserting screen name for transactions
                                    transferTo = transactionDAO.addTransferDebitLocal(txMap, transAmt);
                                    transferTo.setInstType(objLodgementMasterTO.getBillsStatus());
                                    transferList.add(transferTo);
                                    txMap.put(TransferTrans.CR_AC_HD, (String) overDue.get("INT_AC_HD"));
                                    txMap.put(TransferTrans.CR_BRANCH, branchID);
                                    txMap.put(TransferTrans.CURRENCY, "INR");
                                    txMap.put(TransferTrans.CR_PROD_TYPE, TransactionFactory.GL);
                                    txMap.put("TRANS_MOD_TYPE", "GL");
                                    txMap.put(TransferTrans.PARTICULARS, "Int Coll(Delayed) LodgeID : " + objLodgementMasterTO.getLodgementId() + " Closure");
                                    txMap.put(TransferTrans.AUTHORIZE_STATUS_2, "ENTERED_AMOUNT");
                                    txMap.put("SCREEN_NAME",(String) obj.get(CommonConstants.SCREEN));// Added by nithya on 23-09-2016 for inserting screen name for transactions
                                    transferTo = transactionDAO.addTransferCreditLocal(txMap, transAmt);
                                    transferTo.setInstType(objLodgementMasterTO.getBillsStatus());
                                    transferList.add(transferTo);
                                    transactionDAO.setTransactionType(transactionDAO.TRANSFER);
                                    transactionDAO.doTransferLocal(transferList, branchID);
                                }
                            }
                        }
                    }
                }
            }
        } else if (getSubRegType().equals("CPD")) {
            // CPD code rewritten ---- nithya 
            doAccountHeadCreditForCPD(obj,command,txMap,branchID);
        }
        //Added by chithra
        if (objLodgementMasterTO.getBillsStatus().equals("REALIZE") || objLodgementMasterTO.getBillsStatus().equals("DISHONOUR")) {
            if (obj.containsKey("serviceTaxDetailsTO")) {
                ServiceTaxDetailsTO objserviceTaxDetailsTO = (ServiceTaxDetailsTO) obj.get("serviceTaxDetailsTO");
                HashMap wherMap = new HashMap();
                wherMap.put("ACCT_NUM", objLodgementMasterTO.getLodgementId());
                List rsltList = sqlMap.executeQueryForList("getServiceTaxDetailsForUpdation", wherMap);
                if (rsltList != null && rsltList.size() > 0) {
                    HashMap serMapUpdate = new HashMap();
                    serMapUpdate.put("SERVICE_TAX_AMT", objserviceTaxDetailsTO.getServiceTaxAmt());
                    serMapUpdate.put("HIGHER_EDU_CESS", objserviceTaxDetailsTO.getHigherCess());
                    serMapUpdate.put("EDUCATION_CESS", objserviceTaxDetailsTO.getEducationCess());
                    serMapUpdate.put("TOTAL_TAX_AMOUNT", objserviceTaxDetailsTO.getTotalTaxAmt());
                    serMapUpdate.put("ROUND_VAL", objserviceTaxDetailsTO.getRoundVal());
                    serMapUpdate.put("STATUS", "MODIFIED");
                    serMapUpdate.put("AUTHORIZED_STATUS", "");
                    serMapUpdate.put("ACCT_NUM", objLodgementMasterTO.getLodgementId());
                    sqlMap.executeUpdate("updateServiceTaxDetails", serMapUpdate);
                } else {
                    objserviceTaxDetailsTO.setAcct_Num(objLodgementMasterTO.getLodgementId());
                    insertServiceTaxDetails(objserviceTaxDetailsTO);
                }
            }
        }
        //End....
    }
    
    private void creditacHdForDishonur(HashMap obj, String command, HashMap txMap, String branchID) throws Exception        
    {
        
        HashMap prodIdTypMap = new HashMap();
        prodIdTypMap.put("BILLS_TYP", objLodgementMasterTO.getBillsType());
        prodIdTypMap.put("LODGE_ID", objLodgementMasterTO.getLodgementId());
        List lstProdIdTyp = sqlMap.executeQueryForList("getProdIdTyp", prodIdTypMap);
        if (lstProdIdTyp != null && lstProdIdTyp.size() > 0) {
            prodIdTypMap = new HashMap();
            prodIdTypMap = (HashMap) lstProdIdTyp.get(0);
        }
        HashMap acHeads = (HashMap) sqlMap.executeQueryForObject("getBillsAccountHeads", objLodgementMasterTO.getBillsType());
        // HashMap otherHeads=(HashMap)sqlMap.executeQueryForList("getotherBankAcntHeads", objLodgementMasterTO.getBillsType());
        if (!prodIdTypMap.get("PROD_TYPE").equals("GL")) {
            acHeadMap = new HashMap();
            acHeadMap.put("PROD_ID", prodIdTypMap.get("PROD_ID"));
            List lst = sqlMap.executeQueryForList("getAccountHeadProd" + prodIdTypMap.get("PROD_TYPE"), acHeadMap);
            if (lst != null && lst.size() > 0) {
                acHeadMap = (HashMap) lst.get(0);
                System.out.println("acHeadMapacHeadMap" + acHeadMap);
            }
        }
        HashMap prodIdTypMap1 = new HashMap();
        prodIdTypMap1.put("BILLS_TYP", objLodgementMasterTO.getBillsType());
        prodIdTypMap1.put("LODGE_ID", objLodgementMasterTO.getLodgementId());
        List lstProdIdTyp1 = sqlMap.executeQueryForList("getProdIdTyp1", prodIdTypMap1);


        HashMap acHeadMap1 = new HashMap();
        if (lstProdIdTyp1 != null && lstProdIdTyp1.size() > 0) {
            prodIdTypMap1 = new HashMap();
            prodIdTypMap1 = (HashMap) lstProdIdTyp1.get(0);
            if (prodIdTypMap1 != null && prodIdTypMap1.containsKey("REC_OTHER_BANK") && prodIdTypMap1.get("REC_OTHER_BANK") != null) {

                acHeadMap1 = new HashMap();
                acHeadMap1.put("REC_OTHER_BANK", prodIdTypMap1.get("REC_OTHER_BANK"));
                //acHeadMap1.put("PROD_TYPE",prodIdTypMap1.get("PROD_TYPE"));
                List lst1 = sqlMap.executeQueryForList("getAccountHeadProd1", acHeadMap1);
                if (lst1 != null && lst1.size() > 0) {
                    acHeadMap1 = (HashMap) lst1.get(0);
                    System.out.println("acHeadMapacHeadMap" + acHeadMap1);
                }
            }
        }
        System.out.println("Ac heads in doAccountHeadCredit : " + acHeads);
        String coll_Obc_From_Cust = "";
        String creditObcHd = "";
        coll_Obc_From_Cust = CommonUtil.convertObjToStr(acHeads.get("COLL_OBC_FROM_CUST"));
        creditObcHd = CommonUtil.convertObjToStr(acHeads.get("CREDIT_OBC_TO"));
        txMap = createMap(txMap, objLodgementMasterTO, branchID);
        transactionDAO.setBatchDate(batchDate = currDt);
        transactionDAO.setLinkBatchID(objLodgementMasterTO.getLodgementId());
        double transAmt = 0.0;
        TxTransferTO transferTo;
        ArrayList transferList = new ArrayList(); // for local transfer
        
        //start
        
        if (objLodgementMasterTO.getBillsStatus().equals("DISHONOUR")) {
                        System.out.println("DISHONOURDISHONOURDISHONOUR");
                        if (objLodgementMasterTO.getTranType().equals("TRANSFER")) {
                            System.out.println("TRANSFER in dishonour" + objLodgementRemitTO);//babu1 dis
                            if (CommonUtil.convertObjToDouble(objLodgementChequeTO.getInstrumentAmount()) > 0) {
                                txMap = new HashMap();
                                transactionDAO.setCommandMode(CommonConstants.TOSTATUS_INSERT);
                                String brnchName = objLodgementMasterTO.getDraweeName();
                                List list = sqlMap.executeQueryForList("getOtherBankBranchDetailsForDishonour", brnchName);
                                String obcProdType = "";
                                if (list != null && list.size() > 0) {
                                    HashMap hmap = (HashMap) list.get(0);
                                    obcProdType = CommonUtil.convertObjToStr(hmap.get("PROD_TYPE"));
                                }
                                if (obcProdType.equals("INV")) {
                                    if (obj.containsKey("OTHER_BANK_HEAD")) {
                                        System.out.println("innnnnnnnnnnnnnnn OTHER_BANK_HEAD");
                                        txMap.put(TransferTrans.DR_AC_HD, obj.get("OTHER_BANK_HEAD"));
                                        transAmt = CommonUtil.convertObjToDouble(objLodgementChequeTO.getInstrumentAmount());
                                        if (transAmt > 0) {
                                            if (obj.get("OTHER_BANK_PROD_TYPE") != null && obj.get("OTHER_BANK_PROD_TYPE").equals("INV")) {
                                                InvestmentsTransTO objTO = new InvestmentsTransTO();
                                                obj.put("INVESTMENT_ACC_NO", CommonUtil.convertObjToStr(obj.get("OTHER_BANK_HEAD")));
                                                objTO = getInvestmentsCreditTransTO(CommonConstants.TOSTATUS_INSERT, transAmt, obj, obj);
                                                HashMap achdMap = new HashMap();
                                                achdMap.put("INVESTMENT_PROD_ID", CommonUtil.convertObjToStr(objTO.getInvestmentID()));
                                                List achdLst = ServerUtil.executeQuery("getSelectinvestmentAccountHead", achdMap);
                                                if (achdLst != null && achdLst.size() > 0) {
                                                    achdMap = new HashMap();
                                                    achdMap = (HashMap) achdLst.get(0);
                                                    txMap.put(TransferTrans.DR_AC_HD, achdMap.get("IINVESTMENT_AC_HD"));
                                                    acHeads.put("IINVESTMENT_AC_HD", achdMap.get("IINVESTMENT_AC_HD"));
                                                    txMap.put("AUTHORIZEREMARKS", "IINVESTMENT_DEPOSIT");
                                                    //Set Trans ID
                                                    objTO.setBatchID(CommonUtil.convertObjToStr(objLodgementMasterTO.getLodgementId()));
                                                    sqlMap.executeUpdate("insertInvestmentTransTO", objTO);
                                                } else {
                                                    throw new TTException("Account heads not set properly...");
                                                }
                                            }
                                        }
                                    }
                                    ////56 dr and cr
                                    if (!objLodgementMasterTO.getProdType().equals("GL")) {
                                        System.out.println("Not GL");
                                        txMap.put(TransferTrans.DR_AC_HD, (String) acHeads.get("CONT_EXP_BNK_CHRG_DR_HD"));
                                        // txMap.put(TransferTrans.DR_ACT_NUM, objLodgementMasterTO.getBorrowAcctNum());
                                        txMap.put(TransferTrans.DR_ACT_NUM, "");
                                        // txMap.put(TransferTrans.DR_PROD_ID, objLodgementMasterTO.getBorrowProdId());
                                        txMap.put(TransferTrans.DR_PROD_ID, "");

                                    } else {
                                        System.out.println("else if Gl");
                                        txMap.put(TransferTrans.DR_AC_HD, objLodgementMasterTO.getBorrowAcctNum());
                                    }

                                    ////for othr bank chrg
                                    txMap.put(TransferTrans.DR_BRANCH, branchID);
                                    txMap.put(TransferTrans.CURRENCY, "INR");
                                    //txMap.put(TransferTrans.DR_PROD_TYPE,objLodgementMasterTO.getProdType());
                                    txMap.put(TransferTrans.DR_PROD_TYPE, TransactionFactory.GL);
                                    txMap.put("TRANS_MOD_TYPE", "GL");
                                    txMap.put(TransferTrans.PARTICULARS, "VPL Chrg LodgeID : " + objLodgementMasterTO.getLodgementId() + " Dishonour");
//                                txMap.put(TransferTrans.DR_AC_HD, (String)acHeads.get("IINVESTMENT_AC_HD")); // credit to Remittance Issue account head......
                                    txMap.put(TransferTrans.CR_AC_HD, (String) acHeadMap1.get("PRINCIPAL_AC_HD"));
                                    txMap.put(TransferTrans.CR_BRANCH, branchID);
                                    txMap.put(TransferTrans.CR_PROD_TYPE, TransactionFactory.GL);
                                    txMap.put("TRANS_MOD_TYPE", "GL");
                                    txMap.put(TransferTrans.AUTHORIZE_STATUS_2, "ENTERED_AMOUNT");
                                    transAmt = CommonUtil.convertObjToDouble(objLodgementRemitTO.getRemitFavouring()).doubleValue();
                                    System.out.println("transAmt" + transAmt + "nnnn" + acHeads.get("CONT_EXP_BNK_CHRG_DR_HD"));
                                    txMap.put(TransferTrans.PARTICULARS, objLodgementMasterTO.getBorrowProdId());
                                    //                            txMap.put(TransferTrans.DR_INST_TYPE, objLodgementMasterTO.getBillsStatus());
                                    //transactionDAO.setTransType(CommonConstants.CREDIT);
                                    transferTo = transactionDAO.addTransferDebitLocal(txMap, transAmt);
                                    transferTo.setInstType(objLodgementMasterTO.getBillsStatus());
                                    transferTo.setInitiatedBranch(CommonUtil.convertObjToStr(obj.get(CommonConstants.BRANCH_ID)));
                                    transferList.add(transferTo);
                                    txMap.put(TransferTrans.PARTICULARS, "A/c No-" + " " + objLodgementMasterTO.getBorrowAcctNum());
                                    //                            txMap.put(TransferTrans.DR_INST_TYPE, objLodgementMasterTO.getBillsStatus());
                                    txMap.put(TransferTrans.PARTICULARS, objLodgementMasterTO.getLodgementId() + " Dishonour / Comm-" + " / OBC-" + objLodgementRemitTO.getRemitFavouring());
                                    transAmt = CommonUtil.convertObjToDouble(objLodgementRemitTO.getRemitFavouring()).doubleValue();
                                    txMap.put(TransferTrans.AUTHORIZE_STATUS_2, "ENTERED_AMOUNT");
                                    transferTo = transactionDAO.addTransferCreditLocal(txMap, transAmt);
                                    transferTo.setInstType(objLodgementMasterTO.getBillsStatus());
                                    transferTo.setInitiatedBranch(CommonUtil.convertObjToStr(obj.get(CommonConstants.BRANCH_ID)));
                                    transAmt = 0;
                                    if (!objLodgementMasterTO.getProdType().equals("GL")) {
                                        System.out.println("Not GL");
                                        txMap.put(TransferTrans.DR_AC_HD, (String) acHeadMap.get("AC_HEAD"));
                                        txMap.put(TransferTrans.DR_ACT_NUM, objLodgementMasterTO.getBorrowAcctNum());
//                                    txMap.put(TransferTrans.DR_PROD_ID, "");
                                        txMap.put(TransferTrans.DR_PROD_ID, objLodgementMasterTO.getBorrowProdId());
                                    } else {
                                        System.out.println("else if Gl");
//                                    txMap.put(TransferTrans.DR_AC_HD, (String)acHeadMap.get("AC_HEAD"));
                                        txMap.put(TransferTrans.DR_AC_HD, objLodgementMasterTO.getBorrowAcctNum());
                                    }
                                    System.out.println("objLodgementMasterTO.getProdType()" + objLodgementMasterTO.getProdType());
                                    txMap.put(TransferTrans.DR_PROD_TYPE, objLodgementMasterTO.getProdType());
                                    txMap.put("TRANS_MOD_TYPE", objLodgementMasterTO.getProdType());
                                    System.out.println("objLodgementMasterTO.getLodgementId()" + objLodgementMasterTO.getLodgementId());
                                    txMap.put(TransferTrans.PARTICULARS, "VPL Chrg LodgeID : " + objLodgementMasterTO.getLodgementId() + " Dishonour");
                                    
                                    txMap.put(TransferTrans.DR_BRANCH, branchID);
                                   
                                    double totAmountInst = 0.0;
                                    System.out.println("llll" + instructionList);
                                    if (instructionList != null) {
                                        if (instructionList.size() != 0) {
                                            for (int i = 0; i < instructionList.size(); i++) {
                                                LodgementInstructionsTO objLodgementInstructionsTO = (LodgementInstructionsTO) instructionList.get(i);
                                                if (objLodgementInstructionsTO.getAmount() != null) {
                                                    System.out.println("objLodgementInstructionsTO=====jj" + objLodgementInstructionsTO);
                                                    System.out.println("objLodgementInstructionsTO.getAmount()" + objLodgementInstructionsTO.getAmount());
                                                    totAmountInst = totAmountInst + (objLodgementInstructionsTO.getAmount());
                                                    System.out.println("totAmountInst" + totAmountInst);
                                                }
                                            }
                                        }
                                    }
                                   
                                    transAmt = CommonUtil.convertObjToDouble(objLodgementChequeTO.getInstrumentAmount());
                                    System.out.println("transAmttransAmt" + transAmt);
                                    System.out.println("transferList" + transferList);
                                    transactionDAO.setTransactionType(transactionDAO.TRANSFER);
                                    transactionDAO.doTransferLocal(transferList, branchID);
                                    transactionDAO.doTransfer();
                                    //  System.out.println("vvvv");
                                } else {
                                    //for OTHER BANKS
                                    if (!objLodgementMasterTO.getProdType().equals("GL")) {
                                        txMap.put(TransferTrans.CR_AC_HD, (String) acHeads.get("CONT_EXP_BNK_CHRG_DR_HD"));
                                        txMap.put(TransferTrans.CR_ACT_NUM, "");
                                        txMap.put(TransferTrans.CR_PROD_ID, "");
                                    } else {
                                        txMap.put(TransferTrans.CR_AC_HD, objLodgementMasterTO.getBorrowAcctNum());
                                    }

                                    System.out.println("works.............");
                                    txMap.put(TransferTrans.CR_BRANCH, branchID);
                                    txMap.put(TransferTrans.CURRENCY, "INR");
                                    txMap.put(TransferTrans.CR_PROD_TYPE,TransactionFactory.GL);
                                    txMap.put("TRANS_MOD_TYPE", "GL");
                                    txMap.put(TransferTrans.PARTICULARS, "VPL Chrg LodgeID : " + objLodgementMasterTO.getLodgementId() + " Dishonour");
                                    //txMap.put(TransferTrans.CR_AC_HD, (String)acHeads.get("POSTAGE_AC_HD")); // credit to Remittance Issue account head......
                                    txMap.put(TransferTrans.AUTHORIZE_STATUS_2, "ENTERED_AMOUNT");
                                    if (obcProdType.equals("AB")) {
                                        obj.put("INVESTMENT_ACC_NO", CommonUtil.convertObjToStr(obj.get("OTHER_BANK_HEAD")));
                                        System.out.println("CommonUtil.convertObjToStr(obj.get(\"OTHER_BANK_HEAD\"))" + CommonUtil.convertObjToStr(obj.get("OTHER_BANK_HEAD")));
                                        HashMap achdMap = new HashMap();
                                        List achdLst = ServerUtil.executeQuery("getSelectOthrBankAcHd", obj);
                                        System.out.println("obj================" + obj);
                                        if (achdLst != null && achdLst.size() > 0) {
                                            achdMap = new HashMap();
                                            achdMap = (HashMap) achdLst.get(0);
                                            txMap.put("AUTHORIZEREMARKS", "INVESTMENT_CONTRA");
                                            txMap.put(TransferTrans.DR_ACT_NUM, CommonUtil.convertObjToStr(obj.get("OTHER_BANK_HEAD")));
                                            txMap.put(TransferTrans.DR_PROD_ID, "AB");
                                        }
                                    }
                                    txMap.put(TransferTrans.DR_AC_HD, (String) acHeadMap1.get("PRINCIPAL_AC_HD"));
                                    txMap.put(TransferTrans.DR_BRANCH, branchID);
                                    txMap.put(TransferTrans.DR_PROD_TYPE, TransactionFactory.OTHERBANKACTS);
                                    txMap.put("TRANS_MOD_TYPE", "AB");
                                    transAmt = CommonUtil.convertObjToDouble(objLodgementChequeTO.getInstrumentAmount());
                                    System.out.println("transAmttransAmt" + transAmt);
                                    txMap.put(TransferTrans.PARTICULARS, objLodgementMasterTO.getBorrowProdId());
                                    transferTo = transactionDAO.addTransferDebitLocal(txMap, transAmt);
                                    transferTo.setInitiatedBranch(CommonUtil.convertObjToStr(obj.get(CommonConstants.BRANCH_ID)));
                                    transferList.add(transferTo);
                                    transferTo.setInstType(objLodgementMasterTO.getBillsStatus());

                                    txMap.put(TransferTrans.PARTICULARS, "A/c No-" + " " + objLodgementMasterTO.getBorrowAcctNum());
                                    //                            txMap.put(TransferTrans.DR_INST_TYPE, objLodgementMasterTO.getBillsStatus());
                                    txMap.put(TransferTrans.PARTICULARS, "VPL Chrg LodgeID : " + objLodgementMasterTO.getLodgementId() + " Dishonour");
                                    //txMap.put(TransferTrans.CR_AC_HD, (String)acHeads.get("CONT_EXP_BNK_CHRG_DR_HD"));
                                    transAmt = CommonUtil.convertObjToDouble(objLodgementChequeTO.getInstrumentAmount());
                                    txMap.put(TransferTrans.AUTHORIZE_STATUS_2, "ENTERED_AMOUNT");
                                    txMap.put("TRANS_MOD_TYPE", "GL");
                                    transferTo = transactionDAO.addTransferCreditLocal(txMap, transAmt);
                                    transferTo.setInstType(objLodgementMasterTO.getBillsStatus());
                                    transferTo.setInitiatedBranch(CommonUtil.convertObjToStr(obj.get(CommonConstants.BRANCH_ID)));
                                    transferList.add(transferTo);
                                    
                                    //other side
                                    if (!objLodgementMasterTO.getProdType().equals("GL")) {
                                        System.out.println("objLodgementMasterTO.getProdType()====" + objLodgementMasterTO.getProdType());
                                        System.out.println("obcProdTypeobcProdType=====" + obcProdType);
                                        if (obcProdType.equals("AB")) {
                                            obj.put("INVESTMENT_ACC_NO", CommonUtil.convertObjToStr(obj.get("OTHER_BANK_HEAD")));
                                            //objTO = getInvestmentsTransTO(CommonConstants.TOSTATUS_INSERT, transAmt, obj, obj);
                                            HashMap achdMap = new HashMap();
//                                                achdMap.put("INVESTMENT_PROD_ID", CommonUtil.convertObjToStr(objTO.getInvestmentID()));
//                                                List achdLst=ServerUtil.executeQuery("getSelectOthrBankAcHd1", obj);
                                            List achdLst = ServerUtil.executeQuery("getSelectOthrBankAcHd", obj);
                                            if (achdLst != null && achdLst.size() > 0) {
                                                achdMap = new HashMap();
                                                achdMap = (HashMap) achdLst.get(0);
                                                txMap.put(TransferTrans.CR_AC_HD, (String) acHeadMap.get("AC_HEAD"));
//                                                    acHeads.put("IINVESTMENT_AC_HD",achdMap.get("PRINCIPAL_AC_HD"));
                                                //txMap.put("AUTHORIZEREMARKS","PRINCIPAL_AC_HD");
                                                txMap.put(TransferTrans.CR_ACT_NUM, objLodgementMasterTO.getBorrowAcctNum());
                                                txMap.put(TransferTrans.CR_PROD_ID, objLodgementMasterTO.getBorrowProdId());
                                            }
                                        } else {
                                            txMap.put(TransferTrans.CR_AC_HD, (String) acHeadMap.get("AC_HEAD"));
                                            txMap.put(TransferTrans.CR_ACT_NUM, objLodgementMasterTO.getBorrowAcctNum());
                                            txMap.put(TransferTrans.CR_PROD_ID, objLodgementMasterTO.getBorrowProdId());
                                        }
                                    } else {
                                        txMap.put(TransferTrans.CR_AC_HD, objLodgementMasterTO.getBorrowAcctNum());
                                    }
                                    txMap.put(TransferTrans.CR_PROD_TYPE, objLodgementMasterTO.getProdType());
                                    txMap.put(TransferTrans.PARTICULARS, "VPL  LodgeID : " + objLodgementMasterTO.getLodgementId() + " Dishonour");
                                    //txMap.put(TransferTrans.DR_AC_HD, (String)acHeadMap.get("AC_HEAD")); // credit to Remittance Issue account head......
                                    txMap.put(TransferTrans.DR_BRANCH, branchID);
                                    txMap.put(TransferTrans.DR_PROD_TYPE, TransactionFactory.GL);
                                    txMap.put("TRANS_MOD_TYPE", "GL");
                                    txMap.put(TransferTrans.AUTHORIZE_STATUS_2, "ENTERED_AMOUNT");
                                    //System.out.println("objLodgementRemitTO.getRemitFavouring()======"+objLodgementRemitTO.getRemitFavouring());
                                    //                  System.out.println("objLodgementInstTO.getAmount()======"+objLodgementInstTO.getAmount());
                                    transAmt = CommonUtil.convertObjToDouble(objLodgementChequeTO.getInstrumentAmount());
                                    System.out.println("transAmttransAmt 22222222222" + transAmt);
                                    txMap.put(TransferTrans.PARTICULARS, objLodgementMasterTO.getBorrowProdId());
                                    //                            txMap.put(TransferTrans.DR_INST_TYPE, objLodgementMasterTO.getBillsStatus());
                                    //transactionDAO.setTransType(CommonConstants.CREDIT);
                                   
                                    transferTo = transactionDAO.addTransferCreditLocal(txMap, transAmt);
                                    transferTo.setInstType(objLodgementMasterTO.getBillsStatus());
                                    transferTo.setInitiatedBranch(CommonUtil.convertObjToStr(obj.get(CommonConstants.BRANCH_ID)));
                                    transferList.add(transferTo);
                                    txMap.put(TransferTrans.PARTICULARS, "A/c No-" + " " + objLodgementMasterTO.getBorrowAcctNum());
                                    //                            txMap.put(TransferTrans.DR_INST_TYPE, objLodgementMasterTO.getBillsStatus());
                                    txMap.put(TransferTrans.PARTICULARS, "VPL Chrg LodgeID : " + objLodgementMasterTO.getLodgementId() + " Dishonour");
                                    txMap.put(TransferTrans.DR_AC_HD, (String) acHeads.get("MISC_INC_BNK_CHRG_CR_HD"));
                                    System.out.println("6666666666666662222222222==" + (String) acHeads.get("MISC_INC_BNK_CHRG_CR_HD"));
                                    txMap.put(TransferTrans.DR_BRANCH, branchID);
                                    txMap.put(TransferTrans.DR_PROD_TYPE, TransactionFactory.GL);
                                    txMap.put("TRANS_MOD_TYPE", "GL");
                                    txMap.put("AUTHORIZEREMARKS", "INVESTMENT_CONTRA");
                                    transAmt = CommonUtil.convertObjToDouble(objLodgementChequeTO.getInstrumentAmount());
                                    System.out.println("transAmthjtyt" + transAmt);
                                    txMap.put(TransferTrans.AUTHORIZE_STATUS_2, "ENTERED_AMOUNT");
                                    transferTo = transactionDAO.addTransferDebitLocal(txMap, transAmt);
                                    transferTo.setInstType(objLodgementMasterTO.getBillsStatus());
                                    transferTo.setInitiatedBranch(CommonUtil.convertObjToStr(obj.get(CommonConstants.BRANCH_ID)));
                                    transferList.add(transferTo);
                                    
                                    
                                    //end
                                    System.out.println("transferListtransferList" + transferList);
                                     transactionDAO.setLinkBatchID(objLodgementChequeTO.getLodgementId());
                                    transactionDAO.setTransactionType(transactionDAO.TRANSFER);
                                     transferDAO = new TransferDAO();
                        obj.put("MODE", CommonConstants.TOSTATUS_INSERT);
                        obj.put("COMMAND", CommonConstants.TOSTATUS_INSERT);
                        obj.put("LINK_BATCH_ID", objLodgementChequeTO.getLodgementId());
                        obj.put("INVESTMENT_CONTRA", "INVESTMENT_CONTRA");
                        obj.put("TxTransferTO",  transferList);
                        try {
                            HashMap transMap = new HashMap();
                            transMap = transferDAO.execute(obj,false);
                              String authorizeStatus = CommonUtil.convertObjToStr(CommonConstants.STATUS_AUTHORIZED);
                String linkBatchId = objLodgementChequeTO.getLodgementId();
                HashMap cashAuthMap = new HashMap();
                cashAuthMap.put(CommonConstants.BRANCH_ID, obj.get(CommonConstants.BRANCH_ID));
                cashAuthMap.put(CommonConstants.USER_ID, obj.get(CommonConstants.USER_ID));
                transactionDAO = new TransactionDAO(CommonConstants.DEBIT);
                TransactionDAO.authorizeCashAndTransferWithoutRemitIssueDet(linkBatchId, authorizeStatus, cashAuthMap);
                cashAuthMap = null;
                transMap = null;
                            System.out.println("`rish............."+transMap);
                            //System.out.println("transMap#########"+transMap);
                        //    returnAuthMap.put("SINGLE_TRANS_ID", transMap.get("SINGLE_TRANS_ID"));
                            //System.out.println("returnAuthMap#########"+returnAuthMap);
                        //  authorizeTransaction(transMap, obj);
                        } catch (Exception e) {
                            System.out.println("gggggggg" + e);
                            sqlMap.rollbackTransaction();
                            e.printStackTrace();
                            throw e;
                        }
                                //  transactionDAO.doTransferLocal(transferList, branchID);
                                    
                                }
                                System.out.println("chipssss");
                            }//babu1 dis
                            double temp = 0;
                            System.out.println("instructionList ------------- " + instructionList);
                        }                  
                    }                
    }
    
    private void insertServiceTaxDetails(ServiceTaxDetailsTO objserviceTaxDetailsTO) {
        try {
            objserviceTaxDetailsTO.setServiceTaxDet_Id(getServiceTaxNo());
            objserviceTaxDetailsTO.setParticulars("Lodgement");
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
    public HashMap getInterestDetails(HashMap intDetails) throws Exception {
        try {
            HashMap intMap = new HashMap();
            List tempList = sqlMap.executeQueryForList("getIntRateBills", intDetails);
            if (tempList != null && tempList.size() > 0) {
                intMap = (HashMap) tempList.get(0);
                System.out.println("#####intMap : " + intMap);
            }
            return intMap;
        } catch (Exception e) {
            sqlMap.rollbackTransaction();
            e.printStackTrace();
            throw new TransRollbackException(e);
        }
    }

    public double roundInterest(double intAmt) {
        double intamt = (double) getNearest((long) (intAmt * 100), 100) / 100;
        return intamt;
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

    private void doAuthorize(HashMap map) throws Exception {
        try {
            HashMap singleAuthorizeMap;
            HashMap cashAuthMap = new HashMap();
            String authorizeStatus = null;
            String linkBatchId = null;
            String billStatus = null;
            HashMap authMap = (HashMap) map.get(CommonConstants.AUTHORIZEMAP);
            System.out.println("DAO #### AUTHMAP" + authMap);
            List authData = (List) authMap.get(CommonConstants.AUTHORIZEDATA);
            System.out.println("DAO #### AUTHlist" + authData);

            sqlMap.startTransaction();
            System.out.println("transaction started");
            for (int i = 0, j = authData.size(); i < j; i++) {
                linkBatchId = CommonUtil.convertObjToStr(((HashMap) authData.get(i)).get("LODGEMENT_ID"));//Transaction Batch Id
                System.out.println("linkBatchIdlinkBatchId" + linkBatchId);
                billStatus = CommonUtil.convertObjToStr(((HashMap) authData.get(i)).get("BILL_STATUS"));//Transaction Batch Id
                singleAuthorizeMap = new HashMap();
                authorizeStatus = CommonUtil.convertObjToStr(authMap.get(CommonConstants.AUTHORIZESTATUS));
                if (authorizeStatus.equals("AUTHORIZED")) {
                    singleAuthorizeMap.put("STATUS", authMap.get(CommonConstants.AUTHORIZESTATUS));
                    singleAuthorizeMap.put("LODGEMENT_ID", linkBatchId);
                    singleAuthorizeMap.put("BILL_STATUS", billStatus);
                    singleAuthorizeMap.put("AUTHORIZEDT", ServerUtil.getCurrentDate(CommonUtil.convertObjToStr(map.get(CommonConstants.BRANCH_ID))));
                    singleAuthorizeMap.put("USER_ID", map.get(CommonConstants.USER_ID));
                    sqlMap.executeUpdate("authorizeLodgementMaster", singleAuthorizeMap);
                    sqlMap.executeUpdate("authorizeLodgementMasterHistory", singleAuthorizeMap);
                    singleAuthorizeMap = null;
                    //Added By Suresh
                    if (map.containsKey("OTHER_BANK_HEAD") && map.containsKey("OTHER_BANK_PROD_TYPE") && (billStatus.equals("REALIZE") || billStatus.equals("DISHONOUR"))) {
                        if (map.get("OTHER_BANK_PROD_TYPE").equals("INV")) {
                            HashMap whereMap = new HashMap();
                            String investmentBatchId = "";
                            whereMap.put("BATCH_ID", linkBatchId);
                            whereMap.put("TRANS_DT", currDt);
                            whereMap.put("INITIATED_BRANCH", _branchCode);
                            whereMap.put("AUTHORIZE_STATUS", "AUTHORIZE_STATUS");
                            whereMap.put(CommonConstants.BRANCH_ID, _branchCode);
                            InvestmentsTransTO objgetInvestmentsTransTO;
                            List transList = (List) sqlMap.executeQueryForList("getTransferDetails", whereMap);
                            if (transList != null && transList.size() > 0) {
                                HashMap transhashMap = (HashMap) transList.get(0);
                                investmentBatchId = CommonUtil.convertObjToStr(transhashMap.get("BATCH_ID"));

                                List investmentList = (List) sqlMap.executeQueryForList("getSelectInvestmentTransTO", whereMap);

                                if (investmentList != null && investmentList.size() > 0) {
                                    for (int m = 0; m < investmentList.size(); m++) {
                                        objgetInvestmentsTransTO = new InvestmentsTransTO();
                                        objgetInvestmentsTransTO = (InvestmentsTransTO) investmentList.get(m);
                                        System.out.println("%%%%objgetInvestmentsTransTO" + objgetInvestmentsTransTO);
//                                investmentBatchId = CommonUtil.convertObjToStr(whereMap.get("BATCH_ID"));
                                        objgetInvestmentsTransTO.setBatchID(investmentBatchId);
                                        double dividendAmount = 0.0;
                                        whereMap = new HashMap();
                                        dividendAmount = CommonUtil.convertObjToDouble(objLodgementRemitTO.getRemitInstAmt()).doubleValue();
                                        System.out.println("####Investment Transaction");
                                        //Authorization
                                        whereMap.put("INVESTMENT_ACC_NO", map.get("OTHER_BANK_HEAD"));
                                        whereMap.put("BATCH_ID", investmentBatchId);
                                        map.put("FROM_INTEREST_TASK", "FROM_INTEREST_TASK");
                                        ArrayList arrList = new ArrayList();
                                        HashMap authDataMap = new HashMap();
                                        singleAuthorizeMap = new HashMap();
                                        if (m == 1) {
                                            singleAuthorizeMap.put("NOT_ALLOW_TRANS", "NOT_ALLOW_TRANS");
                                        }
                                        authDataMap.put(CommonConstants.USER_ID, CommonUtil.convertObjToStr(map.get("USER_ID")));
                                        authDataMap.put("BATCH_ID", objgetInvestmentsTransTO.getBatchID());
                                        arrList.add(authDataMap);
                                        singleAuthorizeMap.put(CommonConstants.AUTHORIZESTATUS, authorizeStatus);
                                        singleAuthorizeMap.put(CommonConstants.AUTHORIZEDATA, arrList);
                                        //                                singleAuthorizeMap.put("InvestmentsTransTO", getInvestmentsTransTO(authorizeStatus, dividendAmount, whereMap, map));
                                        objgetInvestmentsTransTO.setBatchID(linkBatchId);
                                        singleAuthorizeMap.put("InvestmentsTransTO", objgetInvestmentsTransTO);
                                        singleAuthorizeMap.put(CommonConstants.USER_ID, CommonUtil.convertObjToStr(map.get("USER_ID")));
                                        map.put(CommonConstants.AUTHORIZEMAP, singleAuthorizeMap);
                                        // Set TransactionTO
                                        LinkedHashMap notDelMap = new LinkedHashMap();
                                        LinkedHashMap transMap = new LinkedHashMap();
                                        TransactionTO transfer = new TransactionTO();
                                        transfer = new TransactionTO();
                                        transfer.setTransType("TRANSFER");
                                        transfer.setTransAmt(new Double(dividendAmount));
                                        transfer.setProductType("GL");
                                        transfer.setInstType("VOUCHER");
                                        transfer.setBatchDt(currDt);
                                        notDelMap.put(String.valueOf(1), transfer);//"NOT_DELETED_TRANS_TOs"
                                        transMap.put("NOT_DELETED_TRANS_TOs", notDelMap);
                                        map.put("TransactionTO", transMap);
                                        map.put("BILLS_LINK_BATCH_ID", linkBatchId);
                                        InvestmentsTransDAO investmentDAO = new InvestmentsTransDAO();
                                        whereMap = investmentDAO.execute(map);
                                    }
                                }
                            }
                        }
                        cashAuthMap = new HashMap();
                        cashAuthMap.put(CommonConstants.BRANCH_ID, map.get(CommonConstants.BRANCH_ID));
                        cashAuthMap.put(CommonConstants.USER_ID, map.get(CommonConstants.USER_ID));
                        System.out.println("IN1111111111");
                        if (objLodgementMasterTO.getProdType() != null && billStatus.equals("REALIZE") && objLodgementMasterTO.getProdType().equals("TL")) {
                            linkBatchId = objLodgementMasterTO.getBorrowAcctNum();
                        }
                        System.out.println("2222222222222" + linkBatchId);
                        TransactionDAO.authorizeCashAndTransfer(linkBatchId, authorizeStatus, cashAuthMap);
                    } else {
                        cashAuthMap = new HashMap();
                        cashAuthMap.put(CommonConstants.BRANCH_ID, map.get(CommonConstants.BRANCH_ID));
                        cashAuthMap.put(CommonConstants.USER_ID, map.get(CommonConstants.USER_ID));
                        System.out.println("INgg222222" + billStatus + "  hhhh====" + objLodgementMasterTO.getProdType() + "");
                        if (objLodgementMasterTO.getProdType() != null && billStatus.equals("REALIZE") && objLodgementMasterTO.getProdType().equals("TL")) {
                            linkBatchId = objLodgementMasterTO.getBorrowAcctNum();
                            System.out.println("hhhhhhhhhhhhhhhhhhhhhhhhhhh===" + objLodgementMasterTO.getBorrowAcctNum());
                        }
                        System.out.println("3333333" + linkBatchId);
                        TransactionDAO.authorizeCashAndTransfer(linkBatchId, authorizeStatus, cashAuthMap);
                    }
                }

                if (authorizeStatus.equals("REJECTED")) {
                    HashMap where = new HashMap();
                    where.put("LODGEMENT_ID", linkBatchId);
                    where.put("BILL_STATUS", billStatus);
                    if (billStatus.equalsIgnoreCase("LODGEMENT") && (objLodgementMasterTO.getAuthorizeStatus() == null || objLodgementMasterTO.getAuthorizeStatus().equals(""))) {
                        singleAuthorizeMap.put("STATUS", authMap.get(CommonConstants.AUTHORIZESTATUS));
                        singleAuthorizeMap.put("LODGEMENT_ID", linkBatchId);
                        singleAuthorizeMap.put("BILL_STATUS", billStatus);
                        singleAuthorizeMap.put("AUTHORIZEDT", ServerUtil.getCurrentDate(CommonUtil.convertObjToStr(map.get(CommonConstants.BRANCH_ID))));
                        singleAuthorizeMap.put("USER_ID", map.get(CommonConstants.USER_ID));
                        sqlMap.executeUpdate("authorizeLodgementMaster", singleAuthorizeMap);
                        sqlMap.executeUpdate("authorizeLodgementMasterHistory", singleAuthorizeMap);
                        singleAuthorizeMap = null;
                        //Separation of Authorization for Cash and Transfer
                        //Call this in all places that need Authorization for Transaction
                        cashAuthMap = new HashMap();
                        cashAuthMap.put(CommonConstants.BRANCH_ID, map.get(CommonConstants.BRANCH_ID));
                        cashAuthMap.put(CommonConstants.USER_ID, map.get(CommonConstants.USER_ID));
                        TransactionDAO.authorizeCashAndTransfer(linkBatchId, authorizeStatus, cashAuthMap);
                    } else {
                        if (billStatus.equalsIgnoreCase("REALIZE") && (objLodgementMasterTO.getAuthorizeStatus() == null || objLodgementMasterTO.getAuthorizeStatus().equals(""))) {
                            cashAuthMap = new HashMap();
                            cashAuthMap.put(CommonConstants.BRANCH_ID, CommonUtil.convertObjToStr(map.get(CommonConstants.BRANCH_ID)));
                            cashAuthMap.put(CommonConstants.USER_ID, CommonUtil.convertObjToStr(map.get(CommonConstants.USER_ID)));
                            TransactionDAO.authorizeCashAndTransfer(linkBatchId, authorizeStatus, cashAuthMap);
                        }
                        where = (HashMap) sqlMap.executeQueryForObject("getBillStatus", where);
                        if (where != null && where.size() > 0) {
                            System.out.println("@@@Now Status " + where);
                            where.put("CUR_STATUS", billStatus);
                            sqlMap.executeUpdate("updateBillStatus", where);
                            where.put("BILL_STATUS", billStatus);
                            sqlMap.executeUpdate("deleteLodgementRemit", where);
                            singleAuthorizeMap.put("STATUS", authMap.get(CommonConstants.AUTHORIZESTATUS));
                            singleAuthorizeMap.put("LODGEMENT_ID", linkBatchId);
                            singleAuthorizeMap.put("AUTHORIZEDT", ServerUtil.getCurrentDate(CommonUtil.convertObjToStr(map.get(CommonConstants.BRANCH_ID))));
                            singleAuthorizeMap.put("USER_ID", map.get(CommonConstants.USER_ID));
                            singleAuthorizeMap.put("BILL_STATUS", billStatus);
                            sqlMap.executeUpdate("authorizeLodgementMasterHistory", singleAuthorizeMap);
                            List lst = (List) sqlMap.executeQueryForList("checkPayInSlip", singleAuthorizeMap);
                            if (lst.size() > 0 && lst != null) {
                                HashMap mapData = new HashMap();
                                HashMap batchMap = new HashMap();
                                mapData = (HashMap) lst.get(0);
                                batchMap.put("BATCH_ID", CommonUtil.convertObjToStr(mapData.get("BATCH_ID")));
                                batchMap.put("STATUS", authMap.get(CommonConstants.AUTHORIZESTATUS));
                                batchMap.put("STATUS_BY", map.get(CommonConstants.USER_ID));
                                batchMap.put("INITIATED_BRANCH", _branchCode);
                                batchMap.put("OUTWARD_DT", currDt.clone());
                                sqlMap.executeUpdate("updateOutwardClearing", batchMap);
                                sqlMap.executeUpdate("updatePIS", batchMap);
                                lst = null;
                                batchMap = null;
                                mapData = null;
                            }

                            //Added By Suresh
                            if (map.containsKey("OTHER_BANK_HEAD") && map.containsKey("OTHER_BANK_PROD_TYPE") && (billStatus.equals("REALIZE") || billStatus.equals("DISHONOUR"))) {
                                if (map.get("OTHER_BANK_PROD_TYPE").equals("INV")) {
                                    HashMap whereMap = new HashMap();
                                    String investmentBatchId = "";
                                    whereMap.put("BATCH_ID", linkBatchId);
                                    whereMap.put("TRANS_DT", currDt);
                                    whereMap.put(CommonConstants.BRANCH_ID, _branchCode);
                                    whereMap.put("INITIATED_BRANCH", _branchCode);
                                    whereMap.put("AUTHORIZE_STATUS", "AUTHORIZE_STATUS");
                                    InvestmentsTransTO objgetInvestmentsTransTO;
                                    List transList = (List) sqlMap.executeQueryForList("getTransferDetails", whereMap);
                                    if (transList != null && transList.size() > 0) {
                                        HashMap transhashMap = (HashMap) transList.get(0);
                                        investmentBatchId = CommonUtil.convertObjToStr(transhashMap.get("BATCH_ID"));
                                        List investmentList = (List) sqlMap.executeQueryForList("getSelectInvestmentTransTO", whereMap);
                                        if (investmentList != null && investmentList.size() > 0) {
                                            for (int m = 0; m < investmentList.size(); m++) {
                                                objgetInvestmentsTransTO = new InvestmentsTransTO();
                                                objgetInvestmentsTransTO = (InvestmentsTransTO) investmentList.get(m);
                                                System.out.println("%%%%objgetInvestmentsTransTO" + objgetInvestmentsTransTO);
                                                //                                investmentBatchId = CommonUtil.convertObjToStr(whereMap.get("BATCH_ID"));
                                                objgetInvestmentsTransTO.setBatchID(investmentBatchId);

                                                double dividendAmount = 0.0;
                                                whereMap = new HashMap();
                                                dividendAmount = CommonUtil.convertObjToDouble(objLodgementRemitTO.getRemitInstAmt()).doubleValue();
                                                System.out.println("####Investment Transaction");
                                                //Authorization
                                                whereMap.put("INVESTMENT_ACC_NO", map.get("OTHER_BANK_HEAD"));
                                                whereMap.put("BATCH_ID", investmentBatchId);
                                                map.put("FROM_INTEREST_TASK", "FROM_INTEREST_TASK");
                                                ArrayList arrList = new ArrayList();
                                                HashMap authDataMap = new HashMap();
                                                singleAuthorizeMap = new HashMap();
                                                if (m == 1) {
                                                    singleAuthorizeMap.put("NOT_ALLOW_TRANS", "NOT_ALLOW_TRANS");
                                                }
                                                authDataMap.put(CommonConstants.USER_ID, CommonUtil.convertObjToStr(map.get("USER_ID")));
                                                authDataMap.put("BATCH_ID", investmentBatchId);
                                                arrList.add(authDataMap);
                                                singleAuthorizeMap.put(CommonConstants.AUTHORIZESTATUS, authorizeStatus);
                                                singleAuthorizeMap.put(CommonConstants.AUTHORIZEDATA, arrList);
//                                            singleAuthorizeMap.put("InvestmentsTransTO", getInvestmentsTransTO(authorizeStatus, dividendAmount, whereMap, map));
                                                singleAuthorizeMap.put("InvestmentsTransTO", objgetInvestmentsTransTO);
                                                singleAuthorizeMap.put(CommonConstants.USER_ID, CommonUtil.convertObjToStr(map.get("USER_ID")));
                                                map.put(CommonConstants.AUTHORIZEMAP, singleAuthorizeMap);
                                                // Set TransactionTO
                                                LinkedHashMap notDelMap = new LinkedHashMap();
                                                LinkedHashMap transMap = new LinkedHashMap();
                                                TransactionTO transfer = new TransactionTO();
                                                transfer = new TransactionTO();
                                                transfer.setTransType("TRANSFER");
                                                transfer.setTransAmt(new Double(dividendAmount));
                                                transfer.setProductType("GL");
                                                transfer.setInstType("VOUCHER");
                                                transfer.setBatchDt(currDt);
                                                notDelMap.put(String.valueOf(1), transfer);//"NOT_DELETED_TRANS_TOs"
                                                transMap.put("NOT_DELETED_TRANS_TOs", notDelMap);
                                                map.put("TransactionTO", transMap);
                                                InvestmentsTransDAO investmentDAO = new InvestmentsTransDAO();
                                                whereMap = investmentDAO.execute(map);
                                                HashMap transferTransParam = new HashMap();
                                                transferTransParam.put("BATCH_ID", linkBatchId);
                                                transferTransParam.put("STATUS", "REJECTED");
                                                transferTransParam.put("USER_ID", CommonUtil.convertObjToStr(map.get("USER_ID")));
                                                transferTransParam.put(CommonConstants.AUTHORIZEDT, currDt);
                                                transferTransParam.put("TRANS_DT", currDt);
                                                transferTransParam.put("INITIATED_BRANCH", _branchCode);
                                                sqlMap.executeUpdate("authorizeInvestmentTrans", transferTransParam);

                                            }
                                        }
                                    }
                                }
                            } else {
                                cashAuthMap = new HashMap();
                                cashAuthMap.put(CommonConstants.BRANCH_ID, map.get(CommonConstants.BRANCH_ID));
                                cashAuthMap.put(CommonConstants.USER_ID, map.get(CommonConstants.USER_ID));
                                System.out.println("IN1333333");
                                if (objLodgementMasterTO.getProdType() != null && billStatus.equals("REALIZE") && objLodgementMasterTO.getProdType().equals("TL")) {
                                    linkBatchId = objLodgementMasterTO.getBorrowAcctNum();
                                }
                                System.out.println("444444" + linkBatchId);
                                TransactionDAO.authorizeCashAndTransfer(linkBatchId, authorizeStatus, cashAuthMap);
                            }
                        }
                    }
                    where = null;
                }
            }
//        HashMap m=new HashMap();
//        m.put("LINK",linkBatchId);
//      List countList=sqlMap.executeQueryForList("getCountTransferTrans", m);
//      m=(HashMap)countList.get(0);
//      
//      int count=Integer.parseInt(m.get("NUM").toString());
//        // added By Suresh
//        if(count==0)
//        {

            authorizeStatus = CommonUtil.convertObjToStr(authMap.get(CommonConstants.AUTHORIZESTATUS));
            boolean chk = true;
            if (objLodgementMasterTO.getBillsStatus().equals("LODGEMENT")) {
                HashMap chkMap = new HashMap();
                chkMap.put("LINK_BATCHID", objLodgementChequeTO.getLodgementId());
                List chkList = sqlMap.executeQueryForList("chkalreadyLodged", chkMap);
                if (chkList != null && chkList.size() > 0) {
                    chkMap = (HashMap) chkList.get(0);
                    int no = CommonUtil.convertObjToInt(chkMap.get("NUM"));
                    // System.out.println("chkMap"+no);
                    if (no == 0) {
                        chk = true;
                    } else {
                        chk = false;
                    }
                    chkMap = null;
                }
            }
            // System.out.println("chk"+chk);
           
            if (authorizeStatus.equals("AUTHORIZED") && chk) {
                //  System.out.println("chk4545"+chk);
                ArrayList transferList = new ArrayList();
                TransferTrans transferTrans = new TransferTrans();
                TxTransferTO transferTo = new TxTransferTO();
                ArrayList TxTransferTO = new ArrayList();
                TransactionTO transactionTO = new TransactionTO();
                HashMap txMap = new HashMap();
                HashMap transactionListMap = new HashMap();
                HashMap transMap = new HashMap();
               
                System.out.println("objLodgementMasterTO     " + objLodgementMasterTO);
                System.out.println("objLodgementMasterTO.getBillsType()    " + objLodgementMasterTO.getBillsType());
                HashMap hashData = new HashMap();
                hashData.put("PROD_ID", objLodgementMasterTO.getBillsType());
                List lstData = sqlMap.executeQueryForList("getOperatingType", hashData);
                if (lstData != null && lstData.size() > 0) {
                    System.out.println("hgjgjg");
                    hashData = (HashMap) lstData.get(0);
                }
                if (hashData.get("OPERATES_LIKE").equals("OUTWARD") && hashData.get("CONTRA_AC_HD_YN").equals("Y")) {
                    if (CommonUtil.convertObjToDouble(objLodgementChequeTO.getInstrumentAmount()).doubleValue() > 0
                            && ((objLodgementMasterTO.getBillsStatus().equals("LODGEMENT")) || (objLodgementMasterTO.getBillsStatus().equals("REALIZE"))
                            || (objLodgementMasterTO.getBillsStatus().equals("DISHONOUR")) || (objLodgementMasterTO.getBillsStatus().equals("CLOSURE")))) {
                        System.out.println("######### Instrument Amount 11: " + CommonUtil.convertObjToDouble(objLodgementChequeTO.getInstrumentAmount()).doubleValue());
                        System.out.println("######### Activities       111 : " + objLodgementMasterTO.getBillsStatus());
                        //DEBIT
                        if (CommonUtil.convertObjToDouble(objLodgementChequeTO.getInstrumentAmount()).doubleValue() > 0) {
                            System.out.println("Transfer Started debit : ");
                            transferTo.setInstrumentNo2("APPL_GL_TRANS");
                            if (objLodgementMasterTO.getBillsStatus().equals("LODGEMENT")) {
                                txMap.put(TransferTrans.DR_AC_HD, CommonUtil.convertObjToStr(hashData.get("CONTRA_DR_AC_HD")));
                            } else {
                                txMap.put(TransferTrans.DR_AC_HD, CommonUtil.convertObjToStr(hashData.get("CONTRA_CR_AC_HD")));
                            }
                            txMap.put(TransferTrans.DR_PROD_TYPE, TransactionFactory.GL);
                            txMap.put(TransferTrans.PARTICULARS, objLodgementChequeTO.getLodgementId() + "/" + currDt);
                            txMap.put(TransferTrans.DR_BRANCH, map.get(CommonConstants.BRANCH_ID));
                            txMap.put(TransferTrans.AUTHORIZE_STATUS_2, "ENTERED_AMOUNT");
                            txMap.put("TRANS_MOD_TYPE", "GL");
                            System.out.println("txMap1 : " + txMap + "Debit Instrument Amount :" + CommonUtil.convertObjToDouble(objLodgementChequeTO.getInstrumentAmount()).doubleValue());
                            transferTo = transactionDAO.addTransferDebitLocal(txMap, CommonUtil.convertObjToDouble(objLodgementChequeTO.getInstrumentAmount()).doubleValue());
                            transferTo.setTransId("-");
                            transferTo.setBatchId("-");
                            transferTo.setTransDt(currDt);
                            transferTo.setBranchId(CommonUtil.convertObjToStr(map.get(CommonConstants.BRANCH_ID)));
                            transferTo.setInitiatedBranch(CommonUtil.convertObjToStr(map.get(CommonConstants.BRANCH_ID)));
                            transferTo.setStatusBy((String) map.get(CommonConstants.USER_ID));
                            transferTo.setInitTransId(CommonUtil.convertObjToStr(map.get("USER_ID")));
                            transactionDAO.setLinkBatchID(objLodgementChequeTO.getLodgementId());
                            TxTransferTO.add(transferTo);
                        }
                        //CREDIT
                        if (CommonUtil.convertObjToDouble(objLodgementChequeTO.getInstrumentAmount()).doubleValue() > 0) {
                            System.out.println("Transfer Started credit : ");
                            transferTo = new TxTransferTO();
                            txMap = new HashMap();
                            transferTo.setInstrumentNo2("APPL_GL_TRANS");
                            if (objLodgementMasterTO.getBillsStatus().equals("LODGEMENT")) {
                                txMap.put(TransferTrans.CR_AC_HD, CommonUtil.convertObjToStr(hashData.get("CONTRA_CR_AC_HD")));
                            } else {
                                txMap.put(TransferTrans.CR_AC_HD, CommonUtil.convertObjToStr(hashData.get("CONTRA_DR_AC_HD")));
                            }
                            txMap.put(TransferTrans.CR_PROD_TYPE, TransactionFactory.GL);
                            txMap.put(TransferTrans.CR_BRANCH, map.get(CommonConstants.BRANCH_ID));
                            txMap.put(TransferTrans.PARTICULARS, objLodgementChequeTO.getLodgementId() + "/" + currDt);
                            txMap.put(TransferTrans.AUTHORIZE_STATUS_2, "ENTERED_AMOUNT");
                            txMap.put("TRANS_MOD_TYPE", "GL");
                            System.out.println("txMap3 : " + txMap + "Credit Instrument Amount : :" + CommonUtil.convertObjToDouble(objLodgementChequeTO.getInstrumentAmount()).doubleValue());
                            transferTo = transactionDAO.addTransferCreditLocal(txMap, CommonUtil.convertObjToDouble(objLodgementChequeTO.getInstrumentAmount()).doubleValue());
                            System.out.println("jjjjjjjjjjjjtxMaptxMaptxMaptxMap=========" + txMap);
                            transferTo.setTransId("-");
                            transferTo.setBatchId("-");
                            transferTo.setTransDt(currDt);
                            transferTo.setBranchId(CommonUtil.convertObjToStr(map.get(CommonConstants.BRANCH_ID)));
                            transferTo.setInitiatedBranch(CommonUtil.convertObjToStr(map.get(CommonConstants.BRANCH_ID)));
                            transferTo.setStatusBy(CommonUtil.convertObjToStr(map.get("USER_ID")));
                            transferTo.setInitTransId(CommonUtil.convertObjToStr(map.get("USER_ID")));
                            transactionDAO.setLinkBatchID(objLodgementChequeTO.getLodgementId());
                            TxTransferTO.add(transferTo);
                        }
                        System.out.println("TxTransferTO List 3 : " + TxTransferTO);
                        HashMap applnMap = new HashMap();
                        transferDAO = new TransferDAO();
                        map.put("MODE", CommonConstants.TOSTATUS_INSERT);
                        map.put("COMMAND", CommonConstants.TOSTATUS_INSERT);
                        map.put("LINK_BATCH_ID", objLodgementChequeTO.getLodgementId());
                        map.put("INVESTMENT_CONTRA", "INVESTMENT_CONTRA");
                        map.put("TxTransferTO", TxTransferTO);
                        try {
                            HashMap m1 = new HashMap();
                            transMap = transferDAO.execute(map, false);
                            //System.out.println("transMap#########"+transMap);
                            returnAuthMap.put("SINGLE_TRANS_ID", transMap.get("SINGLE_TRANS_ID"));
                            //System.out.println("returnAuthMap#########"+returnAuthMap);
                            authorizeTransaction(transMap, map);
                        } catch (Exception e) {
                            System.out.println("gggggggg" + e);
                            sqlMap.rollbackTransaction();
                            e.printStackTrace();
                            throw e;
                        }
                    }
                }
            }
            
//            //added by rishad 12/01/2015 for  0010138: cheque return or Bills collecton ReJection
//            
//              if (hashData.get("OPERATES_LIKE").equals("OUTWARD") && authorizeStatus.equals("AUTHORIZED") && chk) {
//             creditacHdForDishonur(map, command, authMap, branchID);
//            }
//            
//            //end
            if (authMap.containsKey("SERVICE_TAX_AUTH")) {
                HashMap serMapAuth = new HashMap();
                serMapAuth.put("STATUS", authorizeStatus);
                serMapAuth.put("USER_ID", CommonUtil.convertObjToStr(map.get("USER_ID")));
                serMapAuth.put("AUTHORIZEDT", currDt);
                serMapAuth.put("ACCT_NUM", objLodgementChequeTO.getLodgementId());
                sqlMap.executeUpdate("authorizeServiceTaxDetails", serMapAuth);

            }
            // }
            sqlMap.commitTransaction();
            cashAuthMap = null;
            linkBatchId = "";
            billStatus = "";

        } catch (Exception e) {
            System.out.println("exceptionllkkkkkyyyyyll" + e);
            sqlMap.rollbackTransaction();

        }
    }
private void cashtransaction()
{

  
}
    public void authorizeTransaction(HashMap transMap, HashMap map) throws Exception {
        try {
            if (transMap != null && transMap.get("TRANS_ID") != null && !transMap.get("TRANS_ID").equals("")) {
                System.out.println("TRANSFER TRANS_ID :" + transMap.get("TRANS_ID"));
                String authorizeStatus = CommonUtil.convertObjToStr(CommonConstants.STATUS_AUTHORIZED);
                String linkBatchId = CommonUtil.convertObjToStr(transMap.get("TRANS_ID"));
                HashMap cashAuthMap = new HashMap();
                cashAuthMap.put(CommonConstants.BRANCH_ID, map.get(CommonConstants.BRANCH_ID));
                cashAuthMap.put(CommonConstants.USER_ID, map.get(CommonConstants.USER_ID));
                transactionDAO = new TransactionDAO(CommonConstants.DEBIT);
                TransactionDAO.authorizeCashAndTransferWithoutRemitIssueDet(linkBatchId, authorizeStatus, cashAuthMap);
                cashAuthMap = null;
                transMap = null;
            }
        } catch (Exception e) {
            sqlMap.rollbackTransaction();
            e.printStackTrace();
            throw e;
        }
    }

    private HashMap createMap(HashMap map, LodgementMasterTO obj, String branchID) {
        map.put(TransferTrans.CR_PROD_TYPE, TransactionFactory.GL);
        map.put(TransferTrans.CR_BRANCH, branchID);
        map.put(TransferTrans.CURRENCY, "INR");
        return map;
    }

    private void destroyObjects() {
        objLodgementInstructionsTO = null;
        objLodgementMasterTO = null;
        objLodgementChequeTO = null;
        objLodgementHundiTO = null;
        instructionList = null;
        multipleLodgementMasterTOList = null;
        objMultipleLodgementMasterTO = null;  
        tempObjLodgementChequeTO = null;
        tempObjLodgementHundiTO = null;
    }

    /**
     * Getter for property subRegType.
     *
     * @return Value of property subRegType.
     */
    public java.lang.String getSubRegType() {
        return subRegType;
    }

    /**
     * Setter for property subRegType.
     *
     * @param subRegType New value of property subRegType.
     */
    public void setSubRegType(java.lang.String subRegType) {
        this.subRegType = subRegType;
    }
    
    private void doAccountHeadCreditWithoutBankCharges(HashMap obj, String command, HashMap txMap, String branchID) throws Exception {
        HashMap prodIdTypMap = new HashMap();
        prodIdTypMap.put("BILLS_TYP", objLodgementMasterTO.getBillsType());
        prodIdTypMap.put("LODGE_ID", objLodgementMasterTO.getLodgementId());
        List lstProdIdTyp = sqlMap.executeQueryForList("getProdIdTyp", prodIdTypMap);
        if (lstProdIdTyp != null && lstProdIdTyp.size() > 0) {
            prodIdTypMap = new HashMap();
            prodIdTypMap = (HashMap) lstProdIdTyp.get(0);
        }
        //changes made by vinay

        HashMap acHeads = (HashMap) sqlMap.executeQueryForObject("getBillsAccountHeads", objLodgementMasterTO.getBillsType());
        // HashMap otherHeads=(HashMap)sqlMap.executeQueryForList("getotherBankAcntHeads", objLodgementMasterTO.getBillsType());
        if (!prodIdTypMap.get("PROD_TYPE").equals("GL")) {
            acHeadMap = new HashMap();
            acHeadMap.put("PROD_ID", prodIdTypMap.get("PROD_ID"));
            List lst = sqlMap.executeQueryForList("getAccountHeadProd" + prodIdTypMap.get("PROD_TYPE"), acHeadMap);
            if (lst != null && lst.size() > 0) {
                acHeadMap = (HashMap) lst.get(0);
                System.out.println("acHeadMapacHeadMap" + acHeadMap);
            }
        }
        HashMap prodIdTypMap1 = new HashMap();
        prodIdTypMap1.put("BILLS_TYP", objLodgementMasterTO.getBillsType());
        prodIdTypMap1.put("LODGE_ID", objLodgementMasterTO.getLodgementId());
        List lstProdIdTyp1 = sqlMap.executeQueryForList("getProdIdTyp1", prodIdTypMap1);

        HashMap acHeadMap1 = new HashMap();
        if (lstProdIdTyp1 != null && lstProdIdTyp1.size() > 0) {
            prodIdTypMap1 = new HashMap();
            prodIdTypMap1 = (HashMap) lstProdIdTyp1.get(0);
            if (prodIdTypMap1 != null && prodIdTypMap1.containsKey("REC_OTHER_BANK") && prodIdTypMap1.get("REC_OTHER_BANK") != null) {

                acHeadMap1 = new HashMap();
                acHeadMap1.put("REC_OTHER_BANK", prodIdTypMap1.get("REC_OTHER_BANK"));
                //acHeadMap1.put("PROD_TYPE",prodIdTypMap1.get("PROD_TYPE"));
                List lst1 = sqlMap.executeQueryForList("getAccountHeadProd1", acHeadMap1);
                if (lst1 != null && lst1.size() > 0) {
                    acHeadMap1 = (HashMap) lst1.get(0);
                    
                }
            }
        }
        
        String coll_Obc_From_Cust = "";
        String creditObcHd = "";
        coll_Obc_From_Cust = CommonUtil.convertObjToStr(acHeads.get("COLL_OBC_FROM_CUST"));
        creditObcHd = CommonUtil.convertObjToStr(acHeads.get("CREDIT_OBC_TO"));        
        txMap = createMap(txMap, objLodgementMasterTO, branchID);
        transactionDAO.setBatchDate(batchDate = currDt);
        transactionDAO.setLinkBatchID(objLodgementMasterTO.getLodgementId());
        double transAmt = 0.0;
        TxTransferTO transferTo;
        ArrayList transferList = new ArrayList(); // for local transfer
        if ((!getSubRegType().equals("ICC")) && (!getSubRegType().equals("CPD"))) {
            //new mode            
            if (CommonUtil.convertObjToDouble(objLodgementChequeTO.getInstrumentAmount()).doubleValue() > 0) {

                transactionDAO.setCommandMode(CommonConstants.TOSTATUS_INSERT);
                HashMap hashData = new HashMap();                
                hashData.put("PROD_ID", objLodgementMasterTO.getBillsType());                
                List lstData = sqlMap.executeQueryForList("getOperatingType", hashData);
                hashData = null;
                if (lstData != null && lstData.size() > 0) {
                    System.out.println("dfxsdfsdf ");
                    hashData = (HashMap) lstData.get(0);
                }
                if (hashData.get("OPERATES_LIKE").equals("OUTWARD")) {                    
                    if (objLodgementMasterTO.getBillsStatus().equals("REALIZE")) {
                        if (objLodgementMasterTO.getTranType().equals("TRANSFER")) {                            
                            if ((objLodgementRemitTO.getRemitProdId().equals("PO")) || (objLodgementRemitTO.getRemitProdId().equals("DD"))
                                    || (objLodgementRemitTO.getRemitProdId().equals("IBR")) || (objLodgementRemitTO.getRemitProdId().equals("OBADV"))) {
                                if (CommonUtil.convertObjToDouble(objLodgementRemitTO.getRemitInstAmt()).doubleValue() > 0) {

                                    // nithya
                                    System.out.println("objLodgementRemitTO.getRemitProdId()!!!!!!" + objLodgementRemitTO.getRemitProdId());
                                    double temp = 0;
                                    transAmt = CommonUtil.convertObjToDouble(objLodgementChequeTO.getInstrumentAmount()).doubleValue();
                                    transAmt = transAmt - CommonUtil.convertObjToDouble(objLodgementRemitTO.getRemitFavouring()).doubleValue();
                                    if ((objLodgementRemitTO.getRemitProdId().equals("PO")) || (objLodgementRemitTO.getRemitProdId().equals("DD"))) {
                                        txMap.put(TransferTrans.DR_AC_HD, (String) acHeads.get("BILLS_REALISED_HD"));
                                    } else if (objLodgementRemitTO.getRemitProdId().equals("IBR")) {
                                        System.out.println("acHeads.get(\"IBR_AC_HD\")" + acHeads.get("IBR_AC_HD"));
                                        txMap.put(TransferTrans.DR_AC_HD, (String) acHeads.get("IBR_AC_HD"));
                                    } else if (objLodgementRemitTO.getRemitProdId().equals("OBADV")) {
                                        if (obj.containsKey("OTHER_BANK_HEAD")) {                                            
                                            txMap.put(TransferTrans.DR_AC_HD, obj.get("OTHER_BANK_HEAD"));                                            
                                            if (obj.get("OTHER_BANK_PROD_TYPE") != null && obj.get("OTHER_BANK_PROD_TYPE").equals("INV")) {
                                                InvestmentsTransTO objTO = new InvestmentsTransTO();
                                                obj.put("INVESTMENT_ACC_NO", CommonUtil.convertObjToStr(obj.get("OTHER_BANK_HEAD")));
                                                objTO = getInvestmentsTransTO(CommonConstants.TOSTATUS_INSERT, transAmt, obj, obj);
                                                HashMap achdMap = new HashMap();
                                                achdMap.put("INVESTMENT_PROD_ID", CommonUtil.convertObjToStr(objTO.getInvestmentID()));
                                                List achdLst = ServerUtil.executeQuery("getSelectinvestmentAccountHead", achdMap);
                                                if (achdLst != null && achdLst.size() > 0) {
                                                    achdMap = new HashMap();
                                                    achdMap = (HashMap) achdLst.get(0);
                                                    txMap.put(TransferTrans.DR_AC_HD, achdMap.get("IINVESTMENT_AC_HD"));
                                                    acHeads.put("IINVESTMENT_AC_HD", achdMap.get("IINVESTMENT_AC_HD"));
                                                    txMap.put("AUTHORIZEREMARKS", "IINVESTMENT_DEPOSIT");
                                                    //Set Trans ID                                                    
                                                    txMap.put(TransferTrans.AUTHORIZE_STATUS_2, "ENTERED_AMOUNT");
                                                    txMap.put(TransferTrans.DR_ACT_NUM, objLodgementMasterTO.getBorrowAcctNum());
                                                    objTO.setBatchID(CommonUtil.convertObjToStr(objLodgementMasterTO.getLodgementId()));
                                                    sqlMap.executeUpdate("insertInvestmentTransTO", objTO);
                                                } else {
                                                    throw new TTException("Account heads not set properly...");
                                                }
                                            }
                                            
                                            if (obj.get("OTHER_BANK_PROD_TYPE") != null && obj.get("OTHER_BANK_PROD_TYPE").equals("AB")) {
                                                InvestmentsTransTO objTO = new InvestmentsTransTO();
                                                obj.put("INVESTMENT_ACC_NO", CommonUtil.convertObjToStr(obj.get("OTHER_BANK_HEAD")));
                                                //objTO = getInvestmentsTransTO(CommonConstants.TOSTATUS_INSERT, transAmt, obj, obj);
                                                HashMap achdMap = new HashMap();
                                                List achdLst = ServerUtil.executeQuery("getSelectOthrBankAcHd", obj);
                                                if (achdLst != null && achdLst.size() > 0) {
                                                    achdMap = new HashMap();
                                                    achdMap = (HashMap) achdLst.get(0);
                                                    txMap.put(TransferTrans.DR_AC_HD, achdMap.get("PRINCIPAL_AC_HD"));
                                                    acHeads.put("IINVESTMENT_AC_HD", achdMap.get("PRINCIPAL_AC_HD"));
                                                    txMap.put("AUTHORIZEREMARKS", "PRINCIPAL_AC_HD");
                                                    txMap.put(TransferTrans.DR_ACT_NUM, CommonUtil.convertObjToStr(obj.get("OTHER_BANK_HEAD")));
                                                    txMap.put(TransferTrans.AUTHORIZE_STATUS_2, "ENTERED_AMOUNT");
                                                } else {
                                                    throw new TTException("Account heads not set properly...");
                                                }
                                            }
                                        } else {
                                            txMap.put(TransferTrans.DR_AC_HD, (String) acHeads.get("BILLS_REALISED_HD"));
                                        }
                                    }
                                    txMap.put(TransferTrans.DR_BRANCH, branchID);
                                    txMap.put(TransferTrans.CURRENCY, "INR");                                    
                                    //bbb1
                                    if (obj.get("OTHER_BANK_PROD_TYPE") != null && obj.get("OTHER_BANK_PROD_TYPE").equals("AB")) {
                                        txMap.put(TransferTrans.DR_PROD_TYPE, TransactionFactory.OTHERBANKACTS);
                                        txMap.put("TRANS_MOD_TYPE", "AB");
                                        txMap.put(TransferTrans.DR_PROD_ID, CommonUtil.convertObjToStr(objLodgementMasterTO.getRecOtherBank()));
                                    } else {
                                        txMap.put(TransferTrans.DR_PROD_TYPE, TransactionFactory.GL);
                                        txMap.put("TRANS_MOD_TYPE", "GL");
                                    }

                                    txMap.put(TransferTrans.PARTICULARS, "LodgeID : " + objLodgementMasterTO.getLodgementId() + " Realize");
                                    txMap.put(TransferTrans.AUTHORIZE_STATUS_2, "ENTERED_AMOUNT");
                                    txMap.put("SCREEN_NAME",(String) obj.get(CommonConstants.SCREEN));// Added by nithya on 23-09-2016 for inserting screen name for transactions
                                    transferTo = transactionDAO.addTransferDebitLocal(txMap, transAmt);
                                    System.out.println("1 Debit 450");
                                    transferTo.setInstType(objLodgementMasterTO.getBillsStatus());
                                    transferTo.setStatusBy(CommonUtil.convertObjToStr(obj.get("USER_ID")));
                                    transferTo.setInitTransId(CommonUtil.convertObjToStr(obj.get("USER_ID")));
                                    transferList.add(transferTo);
                                    txMap.put(TransferTrans.DR_PROD_ID, "");

                                    // transaction 1 debit 2000

                                    double commTot = 0;
                                    double otherBankCharge1 = 0;
                                    for (int i = 0; i < instructionList.size(); i++) {
                                        objLodgementInstTO = (LodgementInstructionsTO) instructionList.get(i);
                                        String status = CommonUtil.convertObjToStr(objLodgementInstTO.getStatus());
                                        if (!status.equals("DELETED")) {
                                            transAmt = CommonUtil.convertObjToDouble(objLodgementInstTO.getAmount()).doubleValue();
                                            commTot += transAmt;
                                            if (CommonUtil.convertObjToDouble(objLodgementInstTO.getServiceTx()).doubleValue() > 0) {
                                                transAmt = CommonUtil.convertObjToDouble(objLodgementInstTO.getServiceTx()).doubleValue();
                                                commTot += transAmt;
                                                txMap.put(TransferTrans.CR_AC_HD, (String) acHeads.get("SERVICE_TAX_HD"));  // credit to Postage Charge account head......
                                                txMap.put(TransferTrans.PARTICULARS, "ServiceTax LodgeID : " + objLodgementMasterTO.getLodgementId() + " Realize");
                                                txMap.put("TRANS_MOD_TYPE", "GL");
                                                if (coll_Obc_From_Cust.equals("Y")) {
                                                    txMap.put("SCREEN_NAME",(String) obj.get(CommonConstants.SCREEN));// Added by nithya on 23-09-2016 for inserting screen name for transactions
                                                    transferTo = transactionDAO.addTransferCreditLocal(txMap, transAmt);
                                                }
                                                transferTo.setInstType(objLodgementMasterTO.getBillsStatus());
                                                transferList.add(transferTo);
                                            }
                                            double serTaxAmtTemp = 0;
                                            if (coll_Obc_From_Cust.equals("N")) {
                                                if (obj.containsKey("serviceTaxDetails")) {
                                                    HashMap serTaxMap = (HashMap) obj.get("serviceTaxDetails");
                                                    serTaxAmtTemp = CommonUtil.convertObjToDouble(serTaxMap.get("TOT_TAX_AMT"));
                                                }
                                            }
                                            temp = temp - (CommonUtil.convertObjToDouble(objLodgementInstTO.getAmount()).doubleValue())
                                                    - (CommonUtil.convertObjToDouble(objLodgementInstTO.getServiceTx()).doubleValue())
                                                    - serTaxAmtTemp;

                                            //INSERT INSTRUCTION
                                            HashMap instMap = new HashMap();
                                            instMap.put("LODGEMENT_ID", objLodgementMasterTO.getLodgementId());
                                            instMap.put("INSTRUCTION", objLodgementInstTO.getInstruction());
                                            List lstInst = sqlMap.executeQueryForList("getBillsExtInst", instMap);
                                            instMap = null;
                                            if (lstInst != null && lstInst.size() > 0) {
                                                sqlMap.executeUpdate("updateLodgementInstructionsTO", objLodgementInstTO);
                                            } else {
                                                sqlMap.executeUpdate("insertLodgementInstructionsTO", objLodgementInstTO);
                                            }
                                            lstInst = null;
                                            status = null;
                                        }

                                    }
                                    if (coll_Obc_From_Cust.equals("N")) {
                                        txMap.put(TransferTrans.CR_AC_HD, (String) acHeads.get("COMM_AC_HD"));  // credit to bank comm account head......
                                        txMap.put(TransferTrans.PARTICULARS, "Total  charges: " + objLodgementMasterTO.getLodgementId() + " Realize");
                                        txMap.put("TRANS_MOD_TYPE", "GL");
                                        txMap.put(TransferTrans.CR_BRANCH, branchID);
                                        txMap.put(TransferTrans.CR_PROD_TYPE, TransactionFactory.GL);
                                        txMap.put("SCREEN_NAME",(String) obj.get(CommonConstants.SCREEN));// Added by nithya on 23-09-2016 for inserting screen name for transactions
                                        transferTo = transactionDAO.addTransferCreditLocal(txMap, commTot + otherBankCharge1);
                                        transferTo.setInstType(objLodgementMasterTO.getBillsStatus());
                                        transferList.add(transferTo);
                                    }
                                    //Added by chithra for servicetax
                                    if (obj.containsKey("serviceTaxDetails")) {
                                        double swachhCess = 0.0;
                                        double krishikalyanCess = 0.0;
                                        double normalServiceTax = 0.0;
                                        double serTaxAmt = 0.0;
                                        HashMap serTaxMap = (HashMap) obj.get("serviceTaxDetails");
                                        if (serTaxMap != null && serTaxMap.size() > 0) {
                                            if (serTaxMap.containsKey("TOT_TAX_AMT") && serTaxMap.get("TOT_TAX_AMT") != null) {
                                                serTaxAmt = CommonUtil.convertObjToDouble(serTaxMap.get("TOT_TAX_AMT"));
                                            }
                                            if (serTaxMap.containsKey(ServiceTaxCalculation.SWACHH_CESS) && serTaxMap.get(ServiceTaxCalculation.SWACHH_CESS) != null) {
                                                swachhCess = CommonUtil.convertObjToDouble(serTaxMap.get(ServiceTaxCalculation.SWACHH_CESS));
                                            }
                                            if (serTaxMap.containsKey(ServiceTaxCalculation.KRISHIKALYAN_CESS) && serTaxMap.get(ServiceTaxCalculation.KRISHIKALYAN_CESS) != null) {
                                                krishikalyanCess = CommonUtil.convertObjToDouble(serTaxMap.get(ServiceTaxCalculation.KRISHIKALYAN_CESS));
                                            }
                                            if (serTaxMap.containsKey(ServiceTaxCalculation.SERVICE_TAX) && serTaxMap.get(ServiceTaxCalculation.SERVICE_TAX) != null) {
                                                      normalServiceTax = CommonUtil.convertObjToDouble(serTaxMap.get(ServiceTaxCalculation.SERVICE_TAX));
                                            }
                                         //   normalServiceTax -= (swachhCess + krishikalyanCess);
                                        }
                                        if (swachhCess > 0) {
                                            txMap.put(TransferTrans.CR_AC_HD, CommonUtil.convertObjToStr(serTaxMap.get("SWACHH_HEAD_ID")));  // credit to Postage Charge account head......
                                            txMap.put(TransferTrans.PARTICULARS, "CGST : " + objLodgementMasterTO.getLodgementId() + " Realize");
                                            if (obj.get("OTHER_BANK_PROD_TYPE") != null && obj.get("OTHER_BANK_PROD_TYPE").equals(TransactionFactory.OTHERBANKACTS)) {
                                                txMap.put(TransferTrans.CR_ACT_NUM, null);
                                            }
                                            txMap.put(TransferTrans.AUTHORIZE_STATUS_2, "ENTERED_AMOUNT");
                                            txMap.put("TRANS_MOD_TYPE", TransactionFactory.GL);
                                            txMap.put(TransferTrans.CR_PROD_TYPE, TransactionFactory.GL);
                                            txMap.put("SCREEN_NAME", (String) obj.get(CommonConstants.SCREEN));// Added by nithya on 23-09-2016 for inserting screen name for transactions
                                            transferTo = transactionDAO.addTransferCreditLocal(txMap, swachhCess);
                                            transferTo.setInstType(objLodgementMasterTO.getBillsStatus());
                                            transferTo.setStatusBy(CommonUtil.convertObjToStr(obj.get("USER_ID")));
                                            transferTo.setInitTransId(CommonUtil.convertObjToStr(obj.get("USER_ID")));
                                            transferList.add(transferTo);
                                           // normalServiceTax -= swachhCess;
                                        }
                                        if (krishikalyanCess > 0) {
                                            txMap.put(TransferTrans.CR_AC_HD, CommonUtil.convertObjToStr(serTaxMap.get("KRISHIKALYAN_HEAD_ID")));  // credit to Postage Charge account head......
                                            txMap.put(TransferTrans.PARTICULARS, "SGST : " + objLodgementMasterTO.getLodgementId() + " Realize");
                                            if (obj.get("OTHER_BANK_PROD_TYPE") != null && obj.get("OTHER_BANK_PROD_TYPE").equals(TransactionFactory.OTHERBANKACTS)) {
                                                txMap.put(TransferTrans.CR_ACT_NUM, null);
                                            }
                                            txMap.put(TransferTrans.AUTHORIZE_STATUS_2, "ENTERED_AMOUNT");
                                            txMap.put("TRANS_MOD_TYPE", TransactionFactory.GL);
                                            txMap.put(TransferTrans.CR_PROD_TYPE, TransactionFactory.GL);
                                            txMap.put("SCREEN_NAME", (String) obj.get(CommonConstants.SCREEN));// Added by nithya on 23-09-2016 for inserting screen name for transactions
                                            transferTo = transactionDAO.addTransferCreditLocal(txMap, krishikalyanCess);
                                            transferTo.setInstType(objLodgementMasterTO.getBillsStatus());
                                            transferTo.setStatusBy(CommonUtil.convertObjToStr(obj.get("USER_ID")));
                                            transferTo.setInitTransId(CommonUtil.convertObjToStr(obj.get("USER_ID")));
                                            transferList.add(transferTo);
                                           // normalServiceTax -= krishikalyanCess;
                                        }
                                        if (normalServiceTax > 0) {
                                            txMap.put(TransferTrans.CR_AC_HD, CommonUtil.convertObjToStr(serTaxMap.get("TAX_HEAD_ID")));  // credit to Postage Charge account head......
                                            txMap.put(TransferTrans.PARTICULARS, "SERVICE TAX for BankCharges LodgeID : " + objLodgementMasterTO.getLodgementId() + " Realize");
                                            if (obj.get("OTHER_BANK_PROD_TYPE") != null && obj.get("OTHER_BANK_PROD_TYPE").equals(TransactionFactory.OTHERBANKACTS)) {
                                                txMap.put(TransferTrans.CR_ACT_NUM, null);
                                            }
                                            txMap.put(TransferTrans.AUTHORIZE_STATUS_2, "ENTERED_AMOUNT");
                                            txMap.put("TRANS_MOD_TYPE", TransactionFactory.GL);
                                            txMap.put(TransferTrans.CR_PROD_TYPE, TransactionFactory.GL);
                                            txMap.put("SCREEN_NAME", (String) obj.get(CommonConstants.SCREEN));// Added by nithya on 23-09-2016 for inserting screen name for transactions
                                            transferTo = transactionDAO.addTransferCreditLocal(txMap, normalServiceTax);
                                            transferTo.setInstType(objLodgementMasterTO.getBillsStatus());
                                            transferTo.setStatusBy(CommonUtil.convertObjToStr(obj.get("USER_ID")));
                                            transferTo.setInitTransId(CommonUtil.convertObjToStr(obj.get("USER_ID")));
                                            transferList.add(transferTo);
                                        }
                                    }

                                    // Transaction 2 credit 

                                    if (coll_Obc_From_Cust.equals("Y")) {
                                        double otherBankCharge = 0.0;
                                        double totalCommisionCharge = 0.0;
                                        otherBankCharge = CommonUtil.convertObjToDouble(objLodgementRemitTO.getRemitFavouring()).doubleValue();  //Only Charge Amount
                                        totalCommisionCharge = commTot;  // commTot -> Toatal Commision Amount
                                        if (totalCommisionCharge > 0) {
                                            txMap.put(TransferTrans.CR_AC_HD, (String) acHeads.get("MISC_INC_BNK_CHRG_CR_HD"));  // credit to Postage Charge account head......
                                            txMap.put(TransferTrans.PARTICULARS, "BankCharges LodgeID : " + objLodgementMasterTO.getLodgementId() + " Realize");
                                            System.out.println("transAmt 2 cr" + totalCommisionCharge);
                                            if (obj.get("OTHER_BANK_PROD_TYPE") != null && obj.get("OTHER_BANK_PROD_TYPE").equals("AB")) {
                                                txMap.put(TransferTrans.CR_ACT_NUM, null);
                                            }
                                            txMap.put(TransferTrans.AUTHORIZE_STATUS_2, "ENTERED_AMOUNT");
                                            txMap.put("TRANS_MOD_TYPE", "GL");
                                            txMap.put(TransferTrans.CR_PROD_TYPE, TransactionFactory.GL);
                                            txMap.put("SCREEN_NAME",(String) obj.get(CommonConstants.SCREEN));// Added by nithya on 23-09-2016 for inserting screen name for transactions
                                            transferTo = transactionDAO.addTransferCreditLocal(txMap, totalCommisionCharge);
                                            transferTo.setInstType(objLodgementMasterTO.getBillsStatus());
                                            transferTo.setStatusBy(CommonUtil.convertObjToStr(obj.get("USER_ID")));
                                            transferTo.setInitTransId(CommonUtil.convertObjToStr(obj.get("USER_ID")));
                                            transferList.add(transferTo);
                                        }

                                    }
                                    //////////////////////////
                                    ///////////////////////////500 cr
                                    if (coll_Obc_From_Cust.equals("Y")) {
                                        temp = CommonUtil.convertObjToDouble(objLodgementChequeTO.getInstrumentAmount()).doubleValue();
                                        temp = temp - CommonUtil.convertObjToDouble(objLodgementRemitTO.getRemitFavouring()).doubleValue();
                                    }
                                    if (temp > 0) {
                                        transAmt = temp;
                                        txMap.put(TransferTrans.CR_AC_HD, null);
                                        txMap.put(TransferTrans.CR_PROD_TYPE, objLodgementMasterTO.getProdType());
                                        txMap.put(TransferTrans.CR_PROD_ID, objLodgementMasterTO.getBorrowProdId());
                                        txMap.put(TransferTrans.CR_ACT_NUM, objLodgementMasterTO.getBorrowAcctNum());
                                        txMap.put(TransferTrans.CR_BRANCH, branchID);
                                        txMap.put("TRANS_MOD_TYPE", objLodgementMasterTO.getProdType());
                                        if (!objLodgementMasterTO.getProdType().equals("") && objLodgementMasterTO.getProdType().equals("GL") && objLodgementMasterTO.getBorrowProdId().equals("")) {
                                            txMap.put(TransferTrans.CR_AC_HD, (String) objLodgementMasterTO.getBorrowAcctNum());
                                            txMap.put(TransferTrans.CR_PROD_TYPE, TransactionFactory.GL);
                                        }
                                        txMap.put(TransferTrans.PARTICULARS, objLodgementMasterTO.getLodgementId() + " Realize / Comm-"
                                                + commTot + " / OBC-" + objLodgementRemitTO.getRemitFavouring());
                                        txMap.put(TransferTrans.AUTHORIZE_STATUS_2, "ENTERED_AMOUNT");
                                        txMap.put("SCREEN_NAME",(String) obj.get(CommonConstants.SCREEN));// Added by nithya on 23-09-2016 for inserting screen name for transactions
                                        transferTo = transactionDAO.addTransferCreditLocal(txMap, transAmt);
                                        transferTo.setInstType(objLodgementMasterTO.getBillsStatus());
                                        transferTo.setStatusBy(CommonUtil.convertObjToStr(obj.get("USER_ID")));
                                        transferTo.setInitTransId(CommonUtil.convertObjToStr(obj.get("USER_ID")));
                                        transferTo.setBranchId(CommonUtil.convertObjToStr(obj.get("SELECTED_BRANCH_ID")));
                                        transferList.add(transferTo);
                                        temp = 0;
                                    }
                                    System.out.println("3  Credit");
                                    ///////////////////////////////

                                    ////////////////Added By Suresh  11 dr
                                    if (coll_Obc_From_Cust.equals("Y")) {
                                        double otherBankCharge = 0.0;
                                        double totalCommisionCharge = 0.0;
                                        double serTaxAmt = 0.0;
                                        if (obj.containsKey("serviceTaxDetails")) {
                                            HashMap serTaxMap = (HashMap) obj.get("serviceTaxDetails");
                                            serTaxAmt = CommonUtil.convertObjToDouble(serTaxMap.get("TOT_TAX_AMT"));
                                        }
                                        otherBankCharge = CommonUtil.convertObjToDouble(objLodgementRemitTO.getRemitFavouring()).doubleValue();  //Only Charge Amount
                                        totalCommisionCharge = commTot + serTaxAmt;  // commTot -> Toatal Commision Amount
                                        if (totalCommisionCharge > 0) {

                                            if (!objLodgementMasterTO.getProdType().equals("GL")) {
                                                txMap.put(TransferTrans.DR_AC_HD, (String) acHeadMap.get("AC_HEAD"));
                                            } else {
                                                txMap.put(TransferTrans.DR_AC_HD, objLodgementMasterTO.getBorrowAcctNum());
                                            }
                                            txMap.put(TransferTrans.DR_ACT_NUM, objLodgementMasterTO.getBorrowAcctNum());
                                            txMap.put(TransferTrans.DR_BRANCH, branchID);
                                            txMap.put(TransferTrans.CURRENCY, "INR");
                                            txMap.put(TransferTrans.DR_PROD_TYPE, objLodgementMasterTO.getProdType());
                                            txMap.put("TRANS_MOD_TYPE", objLodgementMasterTO.getProdType());
                                            txMap.put(TransferTrans.DR_PROD_ID, objLodgementMasterTO.getBorrowProdId());
                                            txMap.put(TransferTrans.PARTICULARS, "LodgeID : " + objLodgementMasterTO.getLodgementId() + " Realize");
                                            txMap.put(TransferTrans.AUTHORIZE_STATUS_2, "ENTERED_AMOUNT");
                                            txMap.put("SCREEN_NAME",(String) obj.get(CommonConstants.SCREEN));// Added by nithya on 23-09-2016 for inserting screen name for transactions
                                            transferTo = transactionDAO.addTransferDebitLocal(txMap, totalCommisionCharge);
                                            transferTo.setInstType(objLodgementMasterTO.getBillsStatus());
                                            transferTo.setStatusBy(CommonUtil.convertObjToStr(obj.get("USER_ID")));
                                            transferTo.setInitTransId(CommonUtil.convertObjToStr(obj.get("USER_ID")));
                                            transferList.add(transferTo);
                                            System.out.println("4 Debit");

                                            ////////////////////////////////////////
                                        }
                                    }
                                    transAmt = 0;
                                    transactionDAO.setTransactionType(transactionDAO.TRANSFER);
                                    transactionDAO.doTransferLocal(transferList, branchID);

                                    // nithya


                                }
                            }

                            if ((CommonUtil.convertObjToDouble(objLodgementMasterTO.getRateForDelay()).doubleValue()) > 0) {
                                HashMap overDue = new HashMap();
                                overDue.put("LODGEMENT_ID", CommonUtil.convertObjToStr(objLodgementMasterTO.getLodgementId()));
                                overDue.put("CUR_DATE", currDt.clone());
                                List dueLst = sqlMap.executeQueryForList("getBillsOverDueIntDetails", overDue);
                                overDue = null;
                                if (dueLst != null && dueLst.size() > 0) {
                                    overDue = (HashMap) dueLst.get(0);
                                    double amount = CommonUtil.convertObjToDouble(objLodgementChequeTO.getInstrumentAmount()).doubleValue();
                                    double rateOfInterest = CommonUtil.convertObjToDouble(objLodgementMasterTO.getRateForDelay()).doubleValue();
                                    double period = CommonUtil.convertObjToDouble(overDue.get("INT_PERIOD")).doubleValue();
                                    double yearDays = 36500;
                                    double intAmount = (amount * rateOfInterest * period) / yearDays;
                                    intAmount = (double) getNearest((long) (intAmount * 100), 100) / 100;
                                    if (intAmount > 0) {
                                        //transaction Coding
                                        txMap = new HashMap();
                                        txMap.put(TransferTrans.DR_AC_HD, (String) overDue.get("INT_AC_HD"));
                                        txMap.put(TransferTrans.DR_BRANCH, branchID);
                                        txMap.put(TransferTrans.CURRENCY, "INR");
                                        txMap.put(TransferTrans.DR_PROD_TYPE, TransactionFactory.GL);
                                        //                                txMap.put(TransferTrans.DR_INST_TYPE, objLodgementMasterTO.getBillsStatus());
                                        txMap.put(TransferTrans.PARTICULARS, "Int Col(Delay) LodgeID : " + objLodgementMasterTO.getLodgementId() + " Realize");
                                        txMap.put(TransferTrans.AUTHORIZE_STATUS_2, "ENTERED_AMOUNT");
                                        txMap.put("TRANS_MOD_TYPE", "GL");
                                        txMap.put("SCREEN_NAME",(String) obj.get(CommonConstants.SCREEN));// Added by nithya on 23-09-2016 for inserting screen name for transactions
                                        transAmt = intAmount;                                        
                                        transferTo = transactionDAO.addTransferDebitLocal(txMap, transAmt);
                                        System.out.println("vivivivivivi");
                                        transferTo.setInstType(objLodgementMasterTO.getBillsStatus());
                                        transferList.add(transferTo);
                                        System.out.println("6 Debit");
                                        txMap.put(TransferTrans.CR_AC_HD, null);
                                        txMap.put(TransferTrans.CR_PROD_TYPE, objLodgementMasterTO.getProdType());
                                        txMap.put("TRANS_MOD_TYPE", objLodgementMasterTO.getProdType());
                                        txMap.put(TransferTrans.CR_PROD_ID, objLodgementMasterTO.getBorrowProdId());
                                        txMap.put(TransferTrans.CR_ACT_NUM, objLodgementMasterTO.getBorrowAcctNum());
                                        txMap.put(TransferTrans.CR_BRANCH, branchID);
                                        txMap.put(TransferTrans.PARTICULARS, "Int Col(Delay) LodgeID : " + objLodgementMasterTO.getLodgementId() + " Realize");
                                        txMap.put(TransferTrans.AUTHORIZE_STATUS_2, "ENTERED_AMOUNT");
                                        txMap.put("SCREEN_NAME",(String) obj.get(CommonConstants.SCREEN));// Added by nithya on 23-09-2016 for inserting screen name for transactions
                                        transferTo = transactionDAO.addTransferCreditLocal(txMap, transAmt);
                                        transferTo.setInstType(objLodgementMasterTO.getBillsStatus());
                                        transferList.add(transferTo);
                                        transactionDAO.setTransactionType(transactionDAO.TRANSFER);
                                        transactionDAO.doTransferLocal(transferList, branchID);
                                    }
                                }

                            }
                        }

                    }
                }
            }
        }
    }	
    
    private void doAccountHeadCreditForCPD(HashMap obj, String command, HashMap txMap, String branchID) throws Exception {
        HashMap prodIdTypMap = new HashMap();
        prodIdTypMap.put("BILLS_TYP", objLodgementMasterTO.getBillsType());
        prodIdTypMap.put("LODGE_ID", objLodgementMasterTO.getLodgementId());
        List lstProdIdTyp = sqlMap.executeQueryForList("getProdIdTyp", prodIdTypMap);
        if (lstProdIdTyp != null && lstProdIdTyp.size() > 0) {
            prodIdTypMap = new HashMap();
            prodIdTypMap = (HashMap) lstProdIdTyp.get(0);
        }
        //changes made by vinay
        HashMap acHeads = (HashMap) sqlMap.executeQueryForObject("getBillsAccountHeads", objLodgementMasterTO.getBillsType());
        // HashMap otherHeads=(HashMap)sqlMap.executeQueryForList("getotherBankAcntHeads", objLodgementMasterTO.getBillsType());
        if (!prodIdTypMap.get("PROD_TYPE").equals("GL")) {
            acHeadMap = new HashMap();
            acHeadMap.put("PROD_ID", prodIdTypMap.get("PROD_ID"));
            List lst = sqlMap.executeQueryForList("getAccountHeadProd" + prodIdTypMap.get("PROD_TYPE"), acHeadMap);
            if (lst != null && lst.size() > 0) {
                acHeadMap = (HashMap) lst.get(0);
                System.out.println("acHeadMapacHeadMap" + acHeadMap);
            }
        }
        HashMap prodIdTypMap1 = new HashMap();
        prodIdTypMap1.put("BILLS_TYP", objLodgementMasterTO.getBillsType());
        prodIdTypMap1.put("LODGE_ID", objLodgementMasterTO.getLodgementId());
        List lstProdIdTyp1 = sqlMap.executeQueryForList("getProdIdTyp1", prodIdTypMap1);
        HashMap acHeadMap1 = new HashMap();
        if (lstProdIdTyp1 != null && lstProdIdTyp1.size() > 0) {
            prodIdTypMap1 = new HashMap();
            prodIdTypMap1 = (HashMap) lstProdIdTyp1.get(0);
            if (prodIdTypMap1 != null && prodIdTypMap1.containsKey("REC_OTHER_BANK") && prodIdTypMap1.get("REC_OTHER_BANK") != null) {
                acHeadMap1 = new HashMap();
                acHeadMap1.put("REC_OTHER_BANK", prodIdTypMap1.get("REC_OTHER_BANK"));
                //acHeadMap1.put("PROD_TYPE",prodIdTypMap1.get("PROD_TYPE"));
                List lst1 = sqlMap.executeQueryForList("getAccountHeadProd1", acHeadMap1);
                if (lst1 != null && lst1.size() > 0) {
                    acHeadMap1 = (HashMap) lst1.get(0);
                    System.out.println("acHeadMapacHeadMap" + acHeadMap1);
                }
            }
        }
        System.out.println("Ac heads in doAccountHeadCredit : " + acHeads);
        String coll_Obc_From_Cust = "";
        String creditObcHd = "";
        coll_Obc_From_Cust = CommonUtil.convertObjToStr(acHeads.get("COLL_OBC_FROM_CUST"));
        creditObcHd = CommonUtil.convertObjToStr(acHeads.get("CREDIT_OBC_TO"));
        txMap = createMap(txMap, objLodgementMasterTO, branchID);
        transactionDAO.setBatchDate(batchDate = currDt);
        transactionDAO.setLinkBatchID(objLodgementMasterTO.getLodgementId());
        double transAmt = 0.0;
        TxTransferTO transferTo;
        ArrayList transferList = new ArrayList(); // for local transfer

        HashMap hashData = new HashMap();
        hashData.put("PROD_ID", objLodgementMasterTO.getBillsType());
        List lstData = sqlMap.executeQueryForList("getOperatingType", hashData);
        hashData = null;
        if (lstData != null && lstData.size() > 0) {
            hashData = (HashMap) lstData.get(0);
        }
        if (objLodgementMasterTO.getBillsStatus().equals("LODGEMENT")) {
            if (hashData.get("OPERATES_LIKE").equals("OUTWARD")) {
                if (CommonUtil.convertObjToDouble(objLodgementChequeTO.getInstrumentAmount()).doubleValue() > 0) {
                    double otherBankAmt = 0;
                    double ourBankAmt = 0;
                    for (int i = 0; i < instructionList.size(); i++) {
                        objLodgementInstTO = (LodgementInstructionsTO) instructionList.get(i);
                        String status = CommonUtil.convertObjToStr(objLodgementInstTO.getStatus());
                        if (!status.equals("DELETED")) {
                            if (objLodgementInstTO.getInstruction().equals("OTHER_BANK_CHARGES")) {
                                otherBankAmt = CommonUtil.convertObjToDouble(objLodgementInstTO.getAmount()).doubleValue();
                                otherBankAmt = otherBankAmt + CommonUtil.convertObjToDouble(objLodgementInstTO.getServiceTx()).doubleValue();
                            } else {
                                ourBankAmt = CommonUtil.convertObjToDouble(objLodgementInstTO.getAmount()).doubleValue();
                                ourBankAmt = ourBankAmt + CommonUtil.convertObjToDouble(objLodgementInstTO.getServiceTx()).doubleValue();
                            }
                            status = null;
                        }
                    }
                    double temp = 0;
                    txMap.put(TransferTrans.DR_AC_HD, acHeads.get("GL_AC_HD"));
                    txMap.put(TransferTrans.DR_BRANCH, branchID);
                    txMap.put(TransferTrans.CURRENCY, "INR");
                    txMap.put(TransferTrans.DR_PROD_TYPE, TransactionFactory.GL);
                    txMap.put(TransferTrans.DR_PROD_ID, objLodgementMasterTO.getCbpProdID());
                    txMap.put(TransferTrans.DR_ACT_NUM, objLodgementMasterTO.getCbpActNum());
                    txMap.put(TransferTrans.PARTICULARS, "LodgeID : " + objLodgementMasterTO.getLodgementId() + " Lodgement");
                    txMap.put(TransferTrans.AUTHORIZE_STATUS_2, "ENTERED_AMOUNT");
                    //calculations
                    double margin = CommonUtil.convertObjToDouble(objLodgementBillRatesTO.getRateForDelay()).doubleValue();
                    long intDays = CommonUtil.convertObjToLong(objLodgementBillRatesTO.getNoOfIntDays());
                    double cheqAmt = CommonUtil.convertObjToDouble(objLodgementChequeTO.getInstrumentAmount()).doubleValue();
                    double marginAmt = 0.0;
                    double rateOfInt = 0.0;
                    if (margin > 0) {
                        marginAmt = (cheqAmt * margin) / 100;
                        marginAmt = roundInterest(marginAmt);
                    }
                    double intAmt = 0.0;
                    if (intDays > 0) {
                        HashMap getInterestMap = new HashMap();
                        getInterestMap.put("LIMIT", String.valueOf((cheqAmt - marginAmt)));
                        getInterestMap.put("CATEGORY", CommonUtil.convertObjToStr(objLodgementMasterTO.getCustCategory()));
                        getInterestMap.put("PROD_ID", CommonUtil.convertObjToStr(objLodgementMasterTO.getCbpProdID()));
                        getInterestMap.put("ACT_NUM", CommonUtil.convertObjToStr(objLodgementMasterTO.getCbpActNum()));
                        getInterestMap.put("DEPOSIT_DT", currDt.clone());
                        HashMap intMap = new HashMap();
                        intMap = getInterestDetails(getInterestMap);
                        getInterestMap = null;
                        rateOfInt = CommonUtil.convertObjToDouble(intMap.get("INTEREST")).doubleValue();
                        intMap = null;
                        double yearDays = 36500;
                        intAmt = ((cheqAmt - marginAmt) * rateOfInt * intDays) / yearDays;
                        intAmt = roundInterest(intAmt);
                    }
                    double charges = otherBankAmt + ourBankAmt;
                    double custCredit = ((cheqAmt) - (marginAmt + intAmt + charges));
                    custCredit = roundInterest(custCredit);
                    double debitAmt = (cheqAmt - (marginAmt + otherBankAmt));
                    transAmt = debitAmt;
                    txMap.put("TRANS_MOD_TYPE", "GL");
                    txMap.put("SCREEN_NAME", (String) obj.get(CommonConstants.SCREEN));// Added by nithya on 23-09-2016 for inserting screen name for transactions
                    transferTo = transactionDAO.addTransferDebitLocal(txMap, transAmt);
                    transferTo.setAuthorizeRemarks("FROM_BILLS_MODULE");
                    transferTo.setInstType(objLodgementMasterTO.getBillsStatus());
                    transferList.add(transferTo);
                    temp = transAmt;
                    for (int i = 0; i < instructionList.size(); i++) {
                        objLodgementInstTO = (LodgementInstructionsTO) instructionList.get(i);
                        String status = CommonUtil.convertObjToStr(objLodgementInstTO.getStatus());
                        if (!status.equals("DELETED")) {
                            if (!objLodgementInstTO.getInstruction().equals("OTHER_BANK_CHARGES")) {
                                transAmt = CommonUtil.convertObjToDouble(objLodgementInstTO.getAmount()).doubleValue();
                                txMap.put(TransferTrans.CR_AC_HD, (String) acHeads.get("CHRG_AC_HD"));  // credit to Postage Charge account head......
                                txMap.put(TransferTrans.PARTICULARS, "BankCharges LodgeID : " + objLodgementMasterTO.getLodgementId());
                                txMap.put(TransferTrans.AUTHORIZE_STATUS_2, "ENTERED_AMOUNT");
                                txMap.put("SCREEN_NAME", (String) obj.get(CommonConstants.SCREEN));// Added by nithya on 23-09-2016 for inserting screen name for transactions
                                transferTo = transactionDAO.addTransferCreditLocal(txMap, transAmt);
                                transferTo.setAuthorizeRemarks("FROM_BILLS_MODULE");
                                transferTo.setInstType(objLodgementMasterTO.getBillsStatus());
                                transferList.add(transferTo);
                                if (CommonUtil.convertObjToDouble(objLodgementInstTO.getServiceTx()).doubleValue() > 0) {
                                    transAmt = CommonUtil.convertObjToDouble(objLodgementInstTO.getServiceTx()).doubleValue();
                                    txMap.put(TransferTrans.CR_AC_HD, (String) acHeads.get("SERVICE_TAX_HD"));  // credit to Postage Charge account head......
                                    txMap.put(TransferTrans.PARTICULARS, "ServiceTax LodgeID : " + objLodgementMasterTO.getLodgementId() + " Lodgement");
                                    txMap.put(TransferTrans.AUTHORIZE_STATUS_2, "ENTERED_AMOUNT");
                                    txMap.put("SCREEN_NAME", (String) obj.get(CommonConstants.SCREEN));// Added by nithya on 23-09-2016 for inserting screen name for transactions
                                    transferTo = transactionDAO.addTransferCreditLocal(txMap, transAmt);
                                    transferTo.setAuthorizeRemarks("FROM_BILLS_MODULE");
                                    transferTo.setInstType(objLodgementMasterTO.getBillsStatus());
                                    transferList.add(transferTo);
                                }
                                temp = temp - (CommonUtil.convertObjToDouble(objLodgementInstTO.getAmount()).doubleValue())
                                        - (CommonUtil.convertObjToDouble(objLodgementInstTO.getServiceTx()).doubleValue());

                                //INSERT INSTRUCTION
                                HashMap instMap = new HashMap();
                                instMap.put("LODGEMENT_ID", objLodgementMasterTO.getLodgementId());
                                instMap.put("INSTRUCTION", objLodgementInstTO.getInstruction());
                                List lstInst = sqlMap.executeQueryForList("getBillsExtInst", instMap);
                                instMap = null;
                                if (lstInst != null && lstInst.size() > 0) {
                                    sqlMap.executeUpdate("updateLodgementInstructionsTO", objLodgementInstTO);
                                } else {
                                    sqlMap.executeUpdate("insertLodgementInstructionsTO", objLodgementInstTO);
                                }
                                lstInst = null;
                            }
                            status = null;
                        }
                    }

                    if (intAmt > 0) {
                        HashMap overDue = new HashMap();
                        overDue.put("LODGEMENT_ID", CommonUtil.convertObjToStr(objLodgementMasterTO.getLodgementId()));
                        List dueLst = sqlMap.executeQueryForList("getBillsTransPrd", overDue);
                        overDue = null;
                        if (dueLst != null && dueLst.size() > 0) {
                            overDue = (HashMap) dueLst.get(0);
                            txMap.put(TransferTrans.CR_AC_HD, (String) overDue.get("INT_AC_HD"));
                            txMap.put(TransferTrans.CR_BRANCH, branchID);
                            txMap.put(TransferTrans.CURRENCY, "INR");
                            txMap.put(TransferTrans.CR_PROD_TYPE, TransactionFactory.GL);
                            txMap.put("TRANS_MOD_TYPE", "GL");
                            txMap.put(TransferTrans.PARTICULARS, "Int Coll LodgeID : " + objLodgementMasterTO.getLodgementId() + " Lodgement");
                            txMap.put(TransferTrans.AUTHORIZE_STATUS_2, "ENTERED_AMOUNT");
                            txMap.put("SCREEN_NAME", (String) obj.get(CommonConstants.SCREEN));// Added by nithya on 23-09-2016 for inserting screen name for transactions
                            transferTo = transactionDAO.addTransferCreditLocal(txMap, intAmt);
                            transferTo.setAuthorizeRemarks("FROM_BILLS_MODULE");
                            transferTo.setInstType(objLodgementMasterTO.getBillsStatus());
                            transferList.add(transferTo);
                        }
                    }
                    if (custCredit > 0) {
                        transAmt = custCredit;
                        txMap.put(TransferTrans.CR_AC_HD, null);
                        txMap.put(TransferTrans.CR_PROD_TYPE, objLodgementMasterTO.getProdType());
                        txMap.put("TRANS_MOD_TYPE", objLodgementMasterTO.getProdType());
                        txMap.put(TransferTrans.CR_PROD_ID, objLodgementMasterTO.getBorrowProdId());
                        txMap.put(TransferTrans.CR_ACT_NUM, objLodgementMasterTO.getBorrowAcctNum());
                        txMap.put(TransferTrans.CR_BRANCH, branchID);
                        txMap.put(TransferTrans.PARTICULARS, "LodgeID : " + objLodgementMasterTO.getLodgementId() + " Lodgement");
                        txMap.put(TransferTrans.AUTHORIZE_STATUS_2, "ENTERED_AMOUNT");
                        txMap.put("SCREEN_NAME", (String) obj.get(CommonConstants.SCREEN));// Added by nithya on 23-09-2016 for inserting screen name for transactions
                        transferTo = transactionDAO.addTransferCreditLocal(txMap, transAmt);
                        transferTo.setAuthorizeRemarks("FROM_BILLS_MODULE");
                        transferTo.setInstType(objLodgementMasterTO.getBillsStatus());
                        transferList.add(transferTo);
                        temp = 0;
                    }
                    transAmt = 0;
                    transactionDAO.setTransactionType(transactionDAO.TRANSFER);
                    transactionDAO.doTransferLocal(transferList, branchID);
                }
            }
        }
        if (objLodgementMasterTO.getBillsStatus().equals("REALIZE")) {
            if (hashData.get("OPERATES_LIKE").equals("OUTWARD")) {
                if (CommonUtil.convertObjToDouble(objLodgementRemitTO.getRemitInstAmt()).doubleValue() > 0) {
                    double temp = 0;
                    double otherBank = 0;
                    double diffAmt = 0;
                    txMap = new HashMap();
                    double remitAmt = 0;
                    double chequeAmt = 0;
                    remitAmt = CommonUtil.convertObjToDouble(objLodgementRemitTO.getRemitInstAmt()).doubleValue();
                    chequeAmt = CommonUtil.convertObjToDouble(objLodgementChequeTO.getInstrumentAmount()).doubleValue();
                    temp = CommonUtil.convertObjToDouble(objLodgementRemitTO.getRemitFavouring()).doubleValue();
                    if ((objLodgementRemitTO.getRemitProdId().equals("PO")) || (objLodgementRemitTO.getRemitProdId().equals("DD"))) {
                        txMap.put(TransferTrans.DR_AC_HD, (String) acHeads.get("BILLS_REALISED_HD"));
                    } else if (objLodgementRemitTO.getRemitProdId().equals("IBR")) {
                        txMap.put(TransferTrans.DR_AC_HD, (String) acHeads.get("IBR_AC_HD"));
                    } else if (objLodgementRemitTO.getRemitProdId().equals("OBADV")) {
                        if (obj.containsKey("OTHER_BANK_HEAD")) {
                            txMap.put(TransferTrans.DR_AC_HD, obj.get("OTHER_BANK_HEAD"));
                            if (obj.get("OTHER_BANK_PROD_TYPE") != null && obj.get("OTHER_BANK_PROD_TYPE").equals("INV")) {
                                InvestmentsTransTO objTO = new InvestmentsTransTO();
                                obj.put("INVESTMENT_ACC_NO", CommonUtil.convertObjToStr(obj.get("OTHER_BANK_HEAD")));
                                objTO = getInvestmentsTransTO(CommonConstants.TOSTATUS_INSERT, transAmt, obj, obj);
                                HashMap achdMap = new HashMap();
                                achdMap.put("INVESTMENT_PROD_ID", CommonUtil.convertObjToStr(objTO.getInvestmentID()));
                                List achdLst = ServerUtil.executeQuery("getSelectinvestmentAccountHead", achdMap);
                                if (achdLst != null && achdLst.size() > 0) {
                                    achdMap = new HashMap();
                                    achdMap = (HashMap) achdLst.get(0);
                                    txMap.put(TransferTrans.DR_AC_HD, achdMap.get("IINVESTMENT_AC_HD"));
                                    acHeads.put("IINVESTMENT_AC_HD", achdMap.get("IINVESTMENT_AC_HD"));
                                    txMap.put("AUTHORIZEREMARKS", "IINVESTMENT_DEPOSIT");
                                    txMap.put(TransferTrans.AUTHORIZE_STATUS_2, "ENTERED_AMOUNT");
                                    txMap.put(TransferTrans.DR_ACT_NUM, objLodgementMasterTO.getBorrowAcctNum());
                                    objTO.setBatchID(CommonUtil.convertObjToStr(objLodgementMasterTO.getLodgementId()));
                                    sqlMap.executeUpdate("insertInvestmentTransTO", objTO);
                                } else {
                                    throw new TTException("Account heads not set properly...");
                                }
                            }
                            if (obj.get("OTHER_BANK_PROD_TYPE") != null && obj.get("OTHER_BANK_PROD_TYPE").equals("AB")) {
                                InvestmentsTransTO objTO = new InvestmentsTransTO();
                                obj.put("INVESTMENT_ACC_NO", CommonUtil.convertObjToStr(obj.get("OTHER_BANK_HEAD")));
                                HashMap achdMap = new HashMap();
                                List achdLst = ServerUtil.executeQuery("getSelectOthrBankAcHd", obj);
                                if (achdLst != null && achdLst.size() > 0) {
                                    achdMap = new HashMap();
                                    achdMap = (HashMap) achdLst.get(0);
                                    txMap.put(TransferTrans.DR_AC_HD, achdMap.get("PRINCIPAL_AC_HD"));
                                    acHeads.put("IINVESTMENT_AC_HD", achdMap.get("PRINCIPAL_AC_HD"));
                                    txMap.put("AUTHORIZEREMARKS", "PRINCIPAL_AC_HD");
                                    txMap.put(TransferTrans.DR_ACT_NUM, CommonUtil.convertObjToStr(obj.get("OTHER_BANK_HEAD")));
                                    txMap.put(TransferTrans.AUTHORIZE_STATUS_2, "ENTERED_AMOUNT");
                                } else {
                                    throw new TTException("Account heads not set properly...");
                                }
                            }
                        } else {
                            txMap.put(TransferTrans.DR_AC_HD, (String) acHeads.get("BILLS_REALISED_HD"));
                        }
                    }
                    txMap.put(TransferTrans.DR_BRANCH, branchID);
                    txMap.put(TransferTrans.CURRENCY, "INR");
                    txMap.put(TransferTrans.DR_PROD_TYPE, TransactionFactory.GL);
                    txMap.put("TRANS_MOD_TYPE", "GL");
                    //                txMap.put(TransferTrans.DR_INST_TYPE, objLodgementMasterTO.getBillsStatus());
                    txMap.put(TransferTrans.PARTICULARS, "LodgeID : " + objLodgementMasterTO.getLodgementId() + " Realize");
                    txMap.put(TransferTrans.AUTHORIZE_STATUS_2, "ENTERED_AMOUNT");
                    transAmt = CommonUtil.convertObjToDouble(objLodgementRemitTO.getRemitInstAmt()).doubleValue();
                    txMap.put("SCREEN_NAME", (String) obj.get(CommonConstants.SCREEN));// Added by nithya on 23-09-2016 for inserting screen name for transactions
                    transferTo = transactionDAO.addTransferDebitLocal(txMap, transAmt);
                    transferTo.setAuthorizeRemarks("FROM_BILLS_MODULE");
                    transferTo.setInstType(objLodgementMasterTO.getBillsStatus());
                    transferList.add(transferTo);
                    HashMap chkMap = new HashMap();
                    double debitAmt = 0.0;
                    chkMap.put("LINK_BATCH_ID", CommonUtil.convertObjToStr(objLodgementMasterTO.getLodgementId()));
                    chkMap.put("ACT_NUM", CommonUtil.convertObjToStr(objLodgementMasterTO.getCbpActNum()));
                    List chkLst = sqlMap.executeQueryForList("getDebitAmountCPD", chkMap);
                    chkMap = null;
                    if (chkLst != null && chkLst.size() > 0) {
                        chkMap = (HashMap) chkLst.get(0);
                        debitAmt = CommonUtil.convertObjToDouble(chkMap.get("AMOUNT")).doubleValue();
                    }
                    txMap.put(TransferTrans.CR_AC_HD, null);
                    txMap.put(TransferTrans.CR_PROD_TYPE, TransactionFactory.ADVANCES);
                    txMap.put("TRANS_MOD_TYPE", "AD");
                    txMap.put(TransferTrans.CR_PROD_ID, objLodgementMasterTO.getCbpProdID());
                    txMap.put(TransferTrans.CR_ACT_NUM, objLodgementMasterTO.getCbpActNum());
                    txMap.put(TransferTrans.CR_BRANCH, branchID);
                    txMap.put(TransferTrans.PARTICULARS, "LodgeID : " + objLodgementMasterTO.getLodgementId() + " Realize");
                    txMap.put(TransferTrans.AUTHORIZE_STATUS_2, "ENTERED_AMOUNT");
                    txMap.put("SCREEN_NAME", (String) obj.get(CommonConstants.SCREEN));// Added by nithya on 23-09-2016 for inserting screen name for transactions
                    //commented 14-may-09
                    double margin = CommonUtil.convertObjToDouble(objLodgementBillRatesTO.getRateForDelay()).doubleValue();
                    double cheqAmt = CommonUtil.convertObjToDouble(objLodgementChequeTO.getInstrumentAmount()).doubleValue();
                    double marginAmt = 0.0;
                    marginAmt = (cheqAmt * margin) / 100;
                    marginAmt = roundInterest(marginAmt);
                    if (transAmt >= debitAmt) //                transferTo =  transactionDAO.addTransferCreditLocal(txMap, transAmt-marginAmt);
                    {
                        transferTo = transactionDAO.addTransferCreditLocal(txMap, debitAmt);
                    } else {
                        transferTo = transactionDAO.addTransferCreditLocal(txMap, transAmt);
                    }

                    //                transferTo.setAuthorizeRemarks(objLodgementMasterTO.getBillsStatus());
                    transferTo.setAuthorizeRemarks("FROM_BILLS_MODULE");
                    transferTo.setInstType(objLodgementMasterTO.getBillsStatus());
                    transferList.add(transferTo);
                    double marginAmount = transAmt - debitAmt;
                    //                 if(margin > 0){
                    if (marginAmount > 0) {
                        transAmt = marginAmount;
                        txMap.put(TransferTrans.CR_AC_HD, acHeads.get("BILLS_REALISED_HD"));
                        txMap.put(TransferTrans.CR_PROD_TYPE, TransactionFactory.GL);
                        txMap.put("TRANS_MOD_TYPE", "GL");
                        txMap.put(TransferTrans.CR_PROD_ID, objLodgementMasterTO.getBorrowProdId());
                        txMap.put(TransferTrans.CR_ACT_NUM, objLodgementMasterTO.getBorrowAcctNum());
                        txMap.put(TransferTrans.CR_BRANCH, branchID);
                        txMap.put(TransferTrans.PARTICULARS, "Margin LodgeID : " + objLodgementMasterTO.getLodgementId() + " Realize");
                        txMap.put(TransferTrans.AUTHORIZE_STATUS_2, "ENTERED_AMOUNT");
                        txMap.put("SCREEN_NAME", (String) obj.get(CommonConstants.SCREEN));// Added by nithya on 23-09-2016 for inserting screen name for transactions
                        transferTo = transactionDAO.addTransferCreditLocal(txMap, transAmt);
                        transferTo.setAuthorizeRemarks("FROM_BILLS_MODULE");
                        transferTo.setInstType(objLodgementMasterTO.getBillsStatus());
                        transferList.add(transferTo);
                    }
                    transactionDAO.setTransactionType(transactionDAO.TRANSFER);
                    transactionDAO.doTransferLocal(transferList, branchID);
                    for (int i = 0; i < instructionList.size(); i++) {
                        objLodgementInstTO = (LodgementInstructionsTO) instructionList.get(i);
                        String status = CommonUtil.convertObjToStr(objLodgementInstTO.getStatus());
                        if (!status.equals("DELETED")) {
                            if (objLodgementInstTO.getInstruction().equals("OTHER_BANK_CHARGES")) {
                                objLodgementInstTO = (LodgementInstructionsTO) instructionList.get(i);
                                otherBank = CommonUtil.convertObjToDouble(objLodgementInstTO.getAmount()).doubleValue();
                                if (temp != otherBank) {
                                    diffAmt = otherBank - temp;
                                }
                            }
                            status = null;
                        }
                    }
                    if (diffAmt == 0 && temp > 0) {
                        //                    diffAmt = remitAmt-chequeAmt; 14-may-09 commented
                        diffAmt = remitAmt - debitAmt;
                    }
                    //EXCESS SHORT IN OTHERBANK CHARGES
                    if (diffAmt < 0) {
                        txMap = new HashMap();
                        txMap.put(TransferTrans.DR_AC_HD, null);
                        txMap.put(TransferTrans.DR_PROD_TYPE, objLodgementMasterTO.getProdType());
                        txMap.put("TRANS_MOD_TYPE", objLodgementMasterTO.getProdType());
                        txMap.put(TransferTrans.DR_PROD_ID, objLodgementMasterTO.getBorrowProdId());
                        txMap.put(TransferTrans.DR_ACT_NUM, objLodgementMasterTO.getBorrowAcctNum());
                        txMap.put(TransferTrans.DR_BRANCH, branchID);
                        //                    txMap.put(TransferTrans.DR_INST_TYPE, objLodgementMasterTO.getBillsStatus());
                        txMap.put(TransferTrans.CURRENCY, "INR");
                        txMap.put(TransferTrans.PARTICULARS, "Short OtherBank Chrg LodgeID : " + objLodgementMasterTO.getLodgementId() + " Realize");
                        txMap.put(TransferTrans.AUTHORIZE_STATUS_2, "ENTERED_AMOUNT");
                        transAmt = diffAmt * -1;
                        txMap.put("SCREEN_NAME", (String) obj.get(CommonConstants.SCREEN));// Added by nithya on 23-09-2016 for inserting screen name for transactions
                        transferTo = transactionDAO.addTransferDebitLocal(txMap, transAmt);
                        //                    transferTo.setAuthorizeRemarks(objLodgementMasterTO.getBillsStatus());
                        transferTo.setAuthorizeRemarks("FROM_BILLS_MODULE");
                        transferTo.setInstType(objLodgementMasterTO.getBillsStatus());
                        transferList.add(transferTo);
                        //                    txMap.put(TransferTrans.CR_AC_HD, (String)acHeads.get("GL_AC_HD")) ;
                        //                    txMap.put(TransferTrans.CR_BRANCH, branchID);
                        //                    txMap.put(TransferTrans.CURRENCY, "INR");
                        //                    txMap.put(TransferTrans.CR_PROD_TYPE, TransactionFactory.GL);
                        txMap.put(TransferTrans.CR_AC_HD, null);
                        txMap.put(TransferTrans.CR_PROD_TYPE, TransactionFactory.ADVANCES);
                        txMap.put("TRANS_MOD_TYPE", "AD");
                        txMap.put(TransferTrans.CR_PROD_ID, objLodgementMasterTO.getCbpProdID());
                        txMap.put(TransferTrans.CR_ACT_NUM, objLodgementMasterTO.getCbpActNum());
                        txMap.put(TransferTrans.CR_BRANCH, branchID);
                        txMap.put(TransferTrans.PARTICULARS, "Short OtherBank Chrg LodgeID : " + objLodgementMasterTO.getLodgementId() + " Realize");
                        txMap.put(TransferTrans.AUTHORIZE_STATUS_2, "ENTERED_AMOUNT");
                        txMap.put("SCREEN_NAME", (String) obj.get(CommonConstants.SCREEN));// Added by nithya on 23-09-2016 for inserting screen name for transactions
                        transferTo = transactionDAO.addTransferCreditLocal(txMap, transAmt);
                        //                    transferTo.setAuthorizeRemarks(objLodgementMasterTO.getBillsStatus());
                        transferTo.setAuthorizeRemarks("FROM_BILLS_MODULE");
                        transferTo.setInstType(objLodgementMasterTO.getBillsStatus());
                        transferList.add(transferTo);
                        transactionDAO.setTransactionType(transactionDAO.TRANSFER);
                        transactionDAO.doTransferLocal(transferList, branchID);
                    }

                    //OVERDUE INTEREST CODING
                    if ((CommonUtil.convertObjToDouble(objLodgementMasterTO.getRateForDelay()).doubleValue()) > 0) {
                        HashMap overDue = new HashMap();
                        overDue.put("LODGEMENT_ID", CommonUtil.convertObjToStr(objLodgementMasterTO.getLodgementId()));
                        overDue.put("CUR_DATE", currDt.clone());
                        List dueLst = sqlMap.executeQueryForList("getBillsOverDueIntDetails", overDue);
                        overDue = null;
                        if (dueLst != null && dueLst.size() > 0) {
                            overDue = (HashMap) dueLst.get(0);
                            double amount = CommonUtil.convertObjToDouble(objLodgementChequeTO.getInstrumentAmount()).doubleValue()
                                    - marginAmt;
                            double rateOfInterest = CommonUtil.convertObjToDouble(objLodgementMasterTO.getRateForDelay()).doubleValue();
                            double period = CommonUtil.convertObjToDouble(overDue.get("INT_PERIOD")).doubleValue();
                            double yearDays = 36500;
                            double intAmount = (amount * rateOfInterest * period) / yearDays;
                            System.out.println("!!!!!!!!amount" + amount + "#####rateOfInterest" + rateOfInterest + "!!!!!!!!!period" + period + "####intAmount" + intAmount);
                            intAmount = (double) getNearest((long) (intAmount * 100), 100) / 100;
                            if (intAmount > 0) {
                                //transaction Coding
                                txMap = new HashMap();
                                txMap.put(TransferTrans.DR_AC_HD, null);
                                txMap.put(TransferTrans.DR_PROD_TYPE, objLodgementMasterTO.getProdType());
                                txMap.put("TRANS_MOD_TYPE", objLodgementMasterTO.getProdType());
                                txMap.put(TransferTrans.DR_PROD_ID, objLodgementMasterTO.getBorrowProdId());
                                txMap.put(TransferTrans.DR_ACT_NUM, objLodgementMasterTO.getBorrowAcctNum());
                                txMap.put(TransferTrans.DR_BRANCH, branchID);
                                txMap.put(TransferTrans.CURRENCY, "INR");
                                //                                txMap.put(TransferTrans.DR_INST_TYPE, objLodgementMasterTO.getBillsStatus());
                                txMap.put(TransferTrans.PARTICULARS, "Int Col(Delay) LodgeID : " + objLodgementMasterTO.getLodgementId() + " Realize");
                                txMap.put(TransferTrans.AUTHORIZE_STATUS_2, "ENTERED_AMOUNT");
                                transAmt = intAmount;
                                txMap.put("SCREEN_NAME", (String) obj.get(CommonConstants.SCREEN));// Added by nithya on 23-09-2016 for inserting screen name for transactions
                                transferTo = transactionDAO.addTransferDebitLocal(txMap, transAmt);
                                transferTo.setAuthorizeRemarks("FROM_BILLS_MODULE");
                                transferTo.setInstType(objLodgementMasterTO.getBillsStatus());
                                transferList.add(transferTo);
                                txMap.put(TransferTrans.CR_AC_HD, (String) overDue.get("INT_AC_HD"));
                                txMap.put(TransferTrans.CR_BRANCH, branchID);
                                txMap.put(TransferTrans.CURRENCY, "INR");
                                txMap.put(TransferTrans.CR_PROD_TYPE, TransactionFactory.GL);
                                txMap.put("TRANS_MOD_TYPE", "GL");
                                txMap.put(TransferTrans.PARTICULARS, "Int Coll(Delay) LodgeID : " + objLodgementMasterTO.getLodgementId() + " Realize");
                                txMap.put(TransferTrans.AUTHORIZE_STATUS_2, "ENTERED_AMOUNT");
                                txMap.put("SCREEN_NAME", (String) obj.get(CommonConstants.SCREEN));// Added by nithya on 23-09-2016 for inserting screen name for transactions
                                transferTo = transactionDAO.addTransferCreditLocal(txMap, transAmt);
                                transferTo.setAuthorizeRemarks("FROM_BILLS_MODULE");
                                transferTo.setInstType(objLodgementMasterTO.getBillsStatus());
                                transferList.add(transferTo);
                                transactionDAO.setTransactionType(transactionDAO.TRANSFER);
                                transactionDAO.doTransferLocal(transferList, branchID);
                            }
                        }
                    }
                }
            }
            if (hashData.get("OPERATES_LIKE").equals("INWARD")) {
                txMap = new HashMap();
                if (!objLodgementMasterTO.getProdType().equals("GL")) {
                    txMap.put(TransferTrans.DR_AC_HD, null);
                    txMap.put(TransferTrans.DR_ACT_NUM, objLodgementMasterTO.getBorrowAcctNum());
                    txMap.put(TransferTrans.DR_PROD_ID, objLodgementMasterTO.getBorrowProdId());
                } else {
                    txMap.put(TransferTrans.DR_AC_HD, objLodgementMasterTO.getBorrowAcctNum());
                }
                txMap.put(TransferTrans.DR_BRANCH, branchID);
                txMap.put(TransferTrans.CURRENCY, "INR");
                txMap.put(TransferTrans.DR_PROD_TYPE, objLodgementMasterTO.getProdType());
                txMap.put(TransferTrans.CR_PROD_TYPE, TransactionFactory.GL);
                txMap.put(TransferTrans.CR_BRANCH, branchID);
                transAmt = CommonUtil.convertObjToDouble(objLodgementChequeTO.getInstrumentAmount()).doubleValue();
                txMap.put(TransferTrans.PARTICULARS, objLodgementMasterTO.getProdId());
                txMap.put(TransferTrans.AUTHORIZE_STATUS_2, "ENTERED_AMOUNT");
                //                 txMap.put(TransferTrans.DR_INST_TYPE, objLodgementMasterTO.getBillsStatus());
                txMap.put(TransferTrans.PARTICULARS, "LodgeID : " + objLodgementMasterTO.getLodgementId() + " Realize");
                txMap.put("TRANS_MOD_TYPE", objLodgementMasterTO.getProdType());
                txMap.put("SCREEN_NAME", (String) obj.get(CommonConstants.SCREEN));// Added by nithya on 23-09-2016 for inserting screen name for transactions
                transferTo = transactionDAO.addTransferDebitLocal(txMap, transAmt);
                transferTo.setInstType(objLodgementMasterTO.getBillsStatus());
                transferList.add(transferTo);
                //                 txMap.put(TransferTrans.PARTICULARS,"A/c No-" + " " + objLodgementMasterTO.getBorrowAcctNum());
                double tempIn = 0;
                tempIn = CommonUtil.convertObjToDouble(objLodgementChequeTO.getInstrumentAmount()).doubleValue();
                for (int i = 0; i < instructionList.size(); i++) {
                    objLodgementInstTO = (LodgementInstructionsTO) instructionList.get(i);
                    String status = CommonUtil.convertObjToStr(objLodgementInstTO.getStatus());
                    if (!status.equals("DELETED")) {
                        transAmt = CommonUtil.convertObjToDouble(objLodgementInstTO.getAmount()).doubleValue();
                        txMap.put(TransferTrans.CR_AC_HD, (String) acHeads.get("CHRG_AC_HD"));  // credit to Postage Charge account head......
                        //                     txMap.put(TransferTrans.DR_INST_TYPE, objLodgementMasterTO.getBillsStatus());
                        txMap.put(TransferTrans.PARTICULARS, "BankCharges LodgeID : " + objLodgementMasterTO.getLodgementId() + " Realize");
                        txMap.put(TransferTrans.AUTHORIZE_STATUS_2, "ENTERED_AMOUNT");
                        txMap.put("TRANS_MOD_TYPE", "GL");
                        txMap.put("SCREEN_NAME", (String) obj.get(CommonConstants.SCREEN));// Added by nithya on 23-09-2016 for inserting screen name for transactions
                        transferTo = transactionDAO.addTransferCreditLocal(txMap, transAmt);
                        transferTo.setInstType(objLodgementMasterTO.getBillsStatus());
                        transferList.add(transferTo);
                        if (CommonUtil.convertObjToDouble(objLodgementInstTO.getServiceTx()).doubleValue() > 0) {
                            transAmt = CommonUtil.convertObjToDouble(objLodgementInstTO.getServiceTx()).doubleValue();
                            txMap.put(TransferTrans.CR_AC_HD, (String) acHeads.get("SERVICE_TAX_HD"));  // credit to Postage Charge account head......
                            //                         txMap.put(TransferTrans.DR_INST_TYPE, objLodgementMasterTO.getBillsStatus());
                            txMap.put(TransferTrans.PARTICULARS, "ServiceTax LodgeID : " + objLodgementMasterTO.getLodgementId() + " Realize");
                            txMap.put(TransferTrans.AUTHORIZE_STATUS_2, "ENTERED_AMOUNT");
                            txMap.put("TRANS_MOD_TYPE", "GL");
                            transferTo = transactionDAO.addTransferCreditLocal(txMap, transAmt);
                            transferTo.setInstType(objLodgementMasterTO.getBillsStatus());
                            transferList.add(transferTo);
                        }
                        tempIn = tempIn - (CommonUtil.convertObjToDouble(objLodgementInstTO.getAmount()).doubleValue())
                                - (CommonUtil.convertObjToDouble(objLodgementInstTO.getServiceTx()).doubleValue());
                        //INSERT INSTRUCTION
                        HashMap instMap = new HashMap();
                        instMap.put("LODGEMENT_ID", objLodgementMasterTO.getLodgementId());
                        instMap.put("INSTRUCTION", objLodgementInstTO.getInstruction());
                        List lstInst = sqlMap.executeQueryForList("getBillsExtInst", instMap);
                        instMap = null;
                        if (lstInst != null && lstInst.size() > 0) {
                            sqlMap.executeUpdate("updateLodgementInstructionsTO", objLodgementInstTO);
                        } else {
                            sqlMap.executeUpdate("insertLodgementInstructionsTO", objLodgementInstTO);
                        }
                        lstInst = null;
                        status = null;
                    }
                }
                txMap.put("SCREEN_NAME", (String) obj.get(CommonConstants.SCREEN));// Added by nithya on 23-09-2016 for inserting screen name for transactions
                if (objLodgementRemitTO.getRemitProdId().equalsIgnoreCase("PO")) {
                    txMap.put(TransferTrans.CR_AC_HD, (String) acHeads.get("OTHER_HD")); // credit to Remittance Issue account head......
                } else if (objLodgementRemitTO.getRemitProdId().equalsIgnoreCase("IBR")) {
                    txMap.put(TransferTrans.CR_AC_HD, (String) acHeads.get("IBR_AC_HD"));
                } else {
                    txMap.put(TransferTrans.CR_AC_HD, (String) acHeads.get("DD_AC_HD"));
                }
                transAmt = tempIn;
                //                 txMap.put(TransferTrans.DR_INST_TYPE, objLodgementMasterTO.getBillsStatus());
                txMap.put(TransferTrans.PARTICULARS, "LodgeID : " + objLodgementMasterTO.getLodgementId() + " Realize");
                txMap.put(TransferTrans.AUTHORIZE_STATUS_2, "ENTERED_AMOUNT");
                txMap.put("SCREEN_NAME", (String) obj.get(CommonConstants.SCREEN));// Added by nithya on 23-09-2016 for inserting screen name for transactions
                transferTo = transactionDAO.addTransferCreditLocal(txMap, transAmt);
                transferTo.setInstType(objLodgementMasterTO.getBillsStatus());
                transferList.add(transferTo);
                transAmt = 0;
                transactionDAO.setCommandMode(command);
                transactionDAO.setTransactionType(transactionDAO.TRANSFER);
                transactionDAO.doTransferLocal(transferList, branchID);
                if ((objLodgementRemitTO.getRemitProdId().equals("PO")) || (objLodgementRemitTO.getRemitProdId().equals("DD"))) {
                    TransactionTO transactionTODebit = new TransactionTO();
                    LinkedHashMap notDeleteMap = new LinkedHashMap();
                    LinkedHashMap transferMap = new LinkedHashMap();
                    HashMap remittanceMap = new HashMap();
                    remittanceIssueDAO = new RemittanceIssueDAO();
                    remittanceIssueTO = new RemittanceIssueTO();
                    String favouringName = objLodgementRemitTO.getRemitFavouringIn();
                    transactionTODebit.setApplName(favouringName);
                    transactionTODebit.setTransAmt(new Double(tempIn));
                    transactionTODebit.setProductId(objLodgementMasterTO.getBorrowProdId());
                    transactionTODebit.setProductType(objLodgementMasterTO.getProdType());
                    transactionTODebit.setDebitAcctNo(objLodgementMasterTO.getBorrowAcctNum());
                    transactionTODebit.setTransType("TRANSFER");
                    remittanceIssueDAO.setFromotherDAo(false);
                    notDeleteMap.put(String.valueOf(1), transactionTODebit);
                    transferMap.put("NOT_DELETED_TRANS_TOs", notDeleteMap);
                    remittanceMap.put("TransactionTO", transferMap);
                    remittanceMap.put(CommonConstants.BRANCH_ID, branchID);
                    remittanceMap.put("OPERATION_MODE", "ISSUE");
                    remittanceMap.put("AUTHORIZEMAP", null);
                    remittanceMap.put("USER_ID", obj.get("USER_ID"));
                    remittanceMap.put("MODULE", "Remittance");
                    remittanceMap.put("MODE", "INSERT");
                    remittanceMap.put("SCREEN", "Issue");
                    remittanceIssueTO.setDraweeBranchCode(objLodgementRemitTO.getRemitDraweeBranchCode());
                    remittanceIssueTO.setDraweeBank(objLodgementRemitTO.getRemitDraweeBank());
                    remittanceIssueTO.setAmount(new Double(tempIn));
                    remittanceIssueTO.setCategory("GENERAL_CATEGORY");
                    remittanceIssueTO.setCity(objLodgementRemitTO.getRemitCity());
                    remittanceIssueTO.setProdId(objLodgementRemitTO.getRemitProdId());
                    remittanceIssueTO.setFavouring(favouringName);
                    remittanceIssueTO.setBranchId(branchID);
                    remittanceIssueTO.setCommand("INSERT");
                    remittanceIssueTO.setTotalAmt(new Double(tempIn));
                    remittanceIssueTO.setExchange(new Double(0.0));
                    remittanceIssueTO.setPostage(new Double(0.0));
                    remittanceIssueTO.setOtherCharges(new Double(0.0));
                    remittanceIssueTO.setRemarks("FROM_BILLS_MODULE");
                    remittanceIssueTO.setStatusDt(ServerUtil.getCurrentDate(branchID));
                    remittanceIssueTO.setStatusBy(CommonUtil.convertObjToStr(obj.get("USER_ID")));
                    remittanceIssueTO.setInstrumentNo1(objLodgementRemitTO.getRemitProdId());
                    remittanceIssueTO.setInstrumentNo2("");
                    LinkedHashMap remitMap = new LinkedHashMap();
                    LinkedHashMap remMap = new LinkedHashMap();
                    remMap.put(String.valueOf(1), remittanceIssueTO);
                    remitMap.put("NOT_DELETED_ISSUE_TOs", remMap);
                    remittanceMap.put("RemittanceIssueTO", remitMap);
                    remittanceIssueDAO.execute(remittanceMap);
                    remMap = null;
                    remitMap = null;
                    remittanceMap = null;
                    notDeleteMap = null;
                    transferMap = null;
                    favouringName = null;
                }
                tempIn = 0;
                objLodgementMasterTO.setTranType("TRANSFER");
            }
        }
        if (objLodgementMasterTO.getBillsStatus().equals("DISHONOUR")) {
            if (hashData.get("OPERATES_LIKE").equals("OUTWARD")) {
                if (objLodgementMasterTO.getTranType().equals("TRANSFER")) {
                    //margin amt credit to customer
                    double margin = CommonUtil.convertObjToDouble(objLodgementBillRatesTO.getRateForDelay()).doubleValue();
                    double cheqAmt = CommonUtil.convertObjToDouble(objLodgementChequeTO.getInstrumentAmount()).doubleValue();
                    double marginAmt = 0.0;
                    marginAmt = (cheqAmt * margin) / 100;
                    marginAmt = roundInterest(marginAmt);
                    //DEBIT 950
                    double temp = 0;
                    double otherBank = 0;
                    double diffAmt = 0;
                    txMap = new HashMap();
                    temp = CommonUtil.convertObjToDouble(objLodgementChequeTO.getInstrumentAmount()).doubleValue() - marginAmt;
                    for (int i = 0; i < instructionList.size(); i++) {
                        objLodgementInstTO = (LodgementInstructionsTO) instructionList.get(i);
                        String status = CommonUtil.convertObjToStr(objLodgementInstTO.getStatus());
                        if (!status.equals("DELETED")) {
                            if (objLodgementInstTO.getInstruction().equals("OTHER_BANK_CHARGES")) {
                                objLodgementInstTO = (LodgementInstructionsTO) instructionList.get(i);
                                otherBank = otherBank + CommonUtil.convertObjToDouble(objLodgementInstTO.getAmount()).doubleValue();
                            }
                            status = null;
                        }
                    }
                    diffAmt = temp - otherBank;
                    txMap = new HashMap();
                    txMap.put(TransferTrans.DR_AC_HD, null);
                    txMap.put(TransferTrans.DR_PROD_TYPE, objLodgementMasterTO.getProdType());
                    txMap.put(TransferTrans.DR_PROD_ID, objLodgementMasterTO.getBorrowProdId());
                    txMap.put(TransferTrans.DR_ACT_NUM, objLodgementMasterTO.getBorrowAcctNum());
                    txMap.put(TransferTrans.DR_BRANCH, branchID);
                    txMap.put(TransferTrans.CURRENCY, "INR");
                    //                                txMap.put(TransferTrans.DR_INST_TYPE, objLodgementMasterTO.getBillsStatus());
                    txMap.put(TransferTrans.PARTICULARS, "LodgeID : " + objLodgementMasterTO.getLodgementId() + " Dishonour");
                    txMap.put(TransferTrans.AUTHORIZE_STATUS_2, "ENTERED_AMOUNT");
                    transAmt = diffAmt;
                    txMap.put("TRANS_MOD_TYPE", objLodgementMasterTO.getProdType());
                    txMap.put("SCREEN_NAME", (String) obj.get(CommonConstants.SCREEN));// Added by nithya on 23-09-2016 for inserting screen name for transactions
                    transferTo = transactionDAO.addTransferDebitLocal(txMap, transAmt);
                    transferTo.setAuthorizeRemarks("FROM_BILLS_MODULE");
                    transferTo.setInstType(objLodgementMasterTO.getBillsStatus());
                    transferList.add(transferTo);
                    txMap.put(TransferTrans.CR_AC_HD, acHeads.get("GL_AC_HD"));
                    txMap.put(TransferTrans.CR_PROD_TYPE, TransactionFactory.GL);
                    txMap.put("TRANS_MOD_TYPE", "GL");
                    txMap.put(TransferTrans.CR_PROD_ID, objLodgementMasterTO.getCbpProdID());
                    txMap.put(TransferTrans.CR_ACT_NUM, objLodgementMasterTO.getCbpActNum());
                    txMap.put(TransferTrans.CR_BRANCH, branchID);
                    txMap.put(TransferTrans.PARTICULARS, "LodgeID : " + objLodgementMasterTO.getLodgementId() + " Dishonour");
                    txMap.put(TransferTrans.AUTHORIZE_STATUS_2, "ENTERED_AMOUNT");
                    txMap.put(TransferTrans.CURRENCY, "INR");
                    txMap.put("SCREEN_NAME", (String) obj.get(CommonConstants.SCREEN));// Added by nithya on 23-09-2016 for inserting screen name for transactions
                    transferTo = transactionDAO.addTransferCreditLocal(txMap, transAmt);
                    transferTo.setAuthorizeRemarks("FROM_BILLS_MODULE");
                    transferTo.setInstType(objLodgementMasterTO.getBillsStatus());
                    transferList.add(transferTo);
                    transactionDAO.setTransactionType(transactionDAO.TRANSFER);
                    transactionDAO.doTransferLocal(transferList, branchID);
                    //-------------------------------------------
                    if (CommonUtil.convertObjToDouble(objLodgementRemitTO.getRemitFavouring()).doubleValue() > 0) {
                        txMap = new HashMap();
                        transactionDAO.setCommandMode(CommonConstants.TOSTATUS_INSERT);
                        //System.out.println("");
                        if (!objLodgementMasterTO.getProdType().equals("GL")) {
                            txMap.put(TransferTrans.DR_AC_HD, null);
                            txMap.put(TransferTrans.DR_ACT_NUM, objLodgementMasterTO.getBorrowAcctNum());
                            txMap.put(TransferTrans.DR_PROD_ID, objLodgementMasterTO.getBorrowProdId());
                        } else {
                            txMap.put(TransferTrans.DR_AC_HD, objLodgementMasterTO.getBorrowAcctNum());
                        }
                        //                            txMap.put(TransferTrans.DR_INST_TYPE, objLodgementMasterTO.getBillsStatus());
                        txMap.put(TransferTrans.DR_BRANCH, branchID);
                        txMap.put(TransferTrans.CURRENCY, "INR");
                        txMap.put(TransferTrans.DR_PROD_TYPE, objLodgementMasterTO.getProdType());
                        txMap.put("TRANS_MOD_TYPE", objLodgementMasterTO.getProdType());
                        if (((String) acHeads.get("OBC_AC_HD") != null) && (acHeads.get("CREDIT_OBC_TO").equals("Gl A/c Hd"))) {
                            txMap.put(TransferTrans.CR_AC_HD, (String) acHeads.get("OBC_AC_HD"));
                        }
//                            else
//                            txMap.put(TransferTrans.CR_AC_HD, (String)acHeads.get("POSTAGE_AC_HD")); // credit to Remittance Issue account head......
                        txMap.put(TransferTrans.CR_BRANCH, branchID);
                        txMap.put(TransferTrans.CR_PROD_TYPE, TransactionFactory.GL);
                        txMap.put(TransferTrans.PARTICULARS, "VPL Chrg LodgeID : " + objLodgementMasterTO.getLodgementId() + " Dishonour");
                        txMap.put(TransferTrans.AUTHORIZE_STATUS_2, "ENTERED_AMOUNT");
                        transAmt = CommonUtil.convertObjToDouble(objLodgementRemitTO.getRemitFavouring()).doubleValue();
                        txMap.put(TransferTrans.PARTICULARS, objLodgementMasterTO.getBorrowProdId());
                        transactionDAO.setTransType(CommonConstants.CREDIT);
                        txMap.put("SCREEN_NAME", (String) obj.get(CommonConstants.SCREEN));// Added by nithya on 23-09-2016 for inserting screen name for transactions
                        transferTo = transactionDAO.addTransferDebitLocal(txMap, transAmt);
                        txMap.put(TransferTrans.PARTICULARS, "VPL Chrg LodgeID : " + objLodgementMasterTO.getLodgementId() + " Dishonour");
                        txMap.put(TransferTrans.AUTHORIZE_STATUS_2, "ENTERED_AMOUNT");
                        transferTo.setAuthorizeRemarks("FROM_BILLS_MODULE");
                        transferTo.setInstType(objLodgementMasterTO.getBillsStatus());
                        transferList.add(transferTo);
                        //                            txMap.put(TransferTrans.PARTICULARS,"A/c No-" + " " + objLodgementMasterTO.getBorrowAcctNum());
                        transAmt = CommonUtil.convertObjToDouble(objLodgementRemitTO.getRemitFavouring()).doubleValue();
                        transactionDAO.setTransType(CommonConstants.DEBIT);
                        txMap.put("TRANS_MOD_TYPE", "GL");
                        txMap.put("SCREEN_NAME", (String) obj.get(CommonConstants.SCREEN));// Added by nithya on 23-09-2016 for inserting screen name for transactions
                        transferTo = transactionDAO.addTransferCreditLocal(txMap, transAmt);
                        transferTo.setAuthorizeRemarks("FROM_BILLS_MODULE");
                        transferTo.setInstType(objLodgementMasterTO.getBillsStatus());
                        transferList.add(transferTo);
                        transAmt = 0;
                        transactionDAO.setTransactionType(transactionDAO.TRANSFER);
                        transactionDAO.doTransferLocal(transferList, branchID);
                        //cash tran
                        transAmt = CommonUtil.convertObjToDouble(objLodgementRemitTO.getRemitFavouring()).doubleValue();
                        txMap = new HashMap();
                        TransactionTO transTO = new TransactionTO();
                        ArrayList cashList = new ArrayList();
                        transactionDAO.setCommandMode(CommonConstants.TOSTATUS_INSERT);

                        txMap.put(TransferTrans.DR_AC_HD, (String) acHeads.get("POSTAGE_AC_HD"));
                        //                            txMap.put(TransferTrans.DR_INST_TYPE, objLodgementMasterTO.getBillsStatus());
                        txMap.put(TransferTrans.DR_BRANCH, branchID);
                        txMap.put(TransferTrans.CURRENCY, "INR");
                        txMap.put(TransferTrans.DR_PROD_TYPE, TransactionFactory.GL);
                        txMap.put("TRANS_MOD_TYPE", "GL");
                        txMap.put(TransferTrans.PARTICULARS, "LodgeID : " + objLodgementMasterTO.getLodgementId() + " Dishonour");
                        txMap.put(TransferTrans.PARTICULARS, "VPL Chrg LodgeID : " + objLodgementMasterTO.getLodgementId() + " Dishonour");
                        txMap.put(TransferTrans.AUTHORIZE_STATUS_2, "ENTERED_AMOUNT");
                        transactionDAO.setTransType(CommonConstants.CREDIT);
                        txMap.put("SCREEN_NAME", (String) obj.get(CommonConstants.SCREEN));// Added by nithya on 23-09-2016 for inserting screen name for transactions
                        transactionDAO.addTransferDebit(txMap, transAmt);
                        transactionDAO.deleteTxList();
                        transTO.setCommand(CommonConstants.TOSTATUS_INSERT);
                        transTO.setTransType("CASH");
                        transTO.setTransAmt(new Double(transAmt));
                        transTO.setBatchId(objLodgementMasterTO.getLodgementId());
                        transTO.setInstType(objLodgementMasterTO.getBillsStatus());
                        cashList.add(transTO);
                        transactionDAO.addCashList(cashList);
                        transactionDAO.doTransfer();
                    }
                    temp = 0;
                    for (int i = 0; i < instructionList.size(); i++) {
                        objLodgementInstTO = (LodgementInstructionsTO) instructionList.get(i);
                        String status = CommonUtil.convertObjToStr(objLodgementInstTO.getStatus());
                        if (!status.equals("DELETED")) {
                            if (objLodgementInstTO.getInstruction().equals("DISHONOUR_CHARGES")) {
                                transactionDAO.setCommandMode(CommonConstants.TOSTATUS_INSERT);
                                temp = temp + ((CommonUtil.convertObjToDouble(objLodgementInstTO.getAmount()).doubleValue())
                                        + (CommonUtil.convertObjToDouble(objLodgementInstTO.getServiceTx()).doubleValue()));
                                if (!objLodgementMasterTO.getProdType().equals("GL")) {
                                    txMap.put(TransferTrans.DR_AC_HD, null);
                                    txMap.put(TransferTrans.DR_ACT_NUM, objLodgementMasterTO.getBorrowAcctNum());
                                    txMap.put(TransferTrans.DR_PROD_ID, objLodgementMasterTO.getBorrowProdId());
                                } else {
                                    txMap.put(TransferTrans.DR_AC_HD, objLodgementMasterTO.getBorrowAcctNum());
                                }
                                txMap.put(TransferTrans.DR_BRANCH, branchID);
                                txMap.put(TransferTrans.CURRENCY, "INR");
                                txMap.put(TransferTrans.DR_PROD_TYPE, objLodgementMasterTO.getProdType());
                                txMap.put(TransferTrans.PARTICULARS, "Dishonour Chrg LodgeID : " + objLodgementMasterTO.getLodgementId() + " Dishonour");
                                txMap.put(TransferTrans.AUTHORIZE_STATUS_2, "ENTERED_AMOUNT");
                                txMap.put(TransferTrans.CR_BRANCH, branchID);
                                transAmt = temp;
                                transactionDAO.setTransType(CommonConstants.CREDIT);
                                txMap.put("TRANS_MOD_TYPE", objLodgementMasterTO.getProdType());
                                txMap.put("SCREEN_NAME", (String) obj.get(CommonConstants.SCREEN));// Added by nithya on 23-09-2016 for inserting screen name for transactions
                                transferTo = transactionDAO.addTransferDebitLocal(txMap, transAmt);
                                transferTo.setAuthorizeRemarks("FROM_BILLS_MODULE");
                                transferTo.setInstType(objLodgementMasterTO.getBillsStatus());
                                transferList.add(transferTo);
                                if ((CommonUtil.convertObjToDouble(objLodgementInstTO.getAmount()).doubleValue()) > 0) {
                                    txMap.put(TransferTrans.CR_AC_HD, (String) acHeads.get("CHRG_AC_HD"));
                                    txMap.put(TransferTrans.CR_PROD_TYPE, TransactionFactory.GL);
                                    txMap.put("TRANS_MOD_TYPE", "GL");
                                    txMap.put(TransferTrans.PARTICULARS, "Dishonour Chrg LodgeID : " + objLodgementMasterTO.getLodgementId() + " Dishonour");
                                    txMap.put(TransferTrans.AUTHORIZE_STATUS_2, "ENTERED_AMOUNT");
                                    transAmt = CommonUtil.convertObjToDouble(objLodgementInstTO.getAmount()).doubleValue();
                                    transactionDAO.setTransType(CommonConstants.DEBIT);
                                    txMap.put("SCREEN_NAME", (String) obj.get(CommonConstants.SCREEN));// Added by nithya on 23-09-2016 for inserting screen name for transactions
                                    transferTo = transactionDAO.addTransferCreditLocal(txMap, transAmt);
                                    transferTo.setAuthorizeRemarks("FROM_BILLS_MODULE");
                                    transferTo.setInstType(objLodgementMasterTO.getBillsStatus());
                                    transferList.add(transferTo);
                                    transAmt = 0;
                                }
                                if ((CommonUtil.convertObjToDouble(objLodgementInstTO.getServiceTx()).doubleValue()) > 0) {
                                    txMap.put(TransferTrans.CR_AC_HD, (String) acHeads.get("SERVICE_TAX_HD"));
                                    txMap.put(TransferTrans.CR_PROD_TYPE, TransactionFactory.GL);
                                    txMap.put(TransferTrans.AUTHORIZE_STATUS_2, "ENTERED_AMOUNT");
                                    txMap.put(TransferTrans.PARTICULARS, "ServiceTax LodgeID : " + objLodgementMasterTO.getLodgementId() + " Dishonour");
                                    transAmt = CommonUtil.convertObjToDouble(objLodgementInstTO.getServiceTx()).doubleValue();
                                    transactionDAO.setTransType(CommonConstants.DEBIT);
                                    txMap.put("TRANS_MOD_TYPE", "GL");
                                    txMap.put("SCREEN_NAME", (String) obj.get(CommonConstants.SCREEN));// Added by nithya on 23-09-2016 for inserting screen name for transactions
                                    transferTo = transactionDAO.addTransferCreditLocal(txMap, transAmt);
                                    transferTo.setAuthorizeRemarks("FROM_BILLS_MODULE");
                                    transferTo.setInstType(objLodgementMasterTO.getBillsStatus());
                                    transferList.add(transferTo);
                                    transAmt = 0;
                                }
                                transactionDAO.setTransactionType(transactionDAO.TRANSFER);
                                transactionDAO.doTransferLocal(transferList, branchID);
                                //INSERT INSTRUCTION
                                HashMap instMap = new HashMap();
                                instMap.put("LODGEMENT_ID", objLodgementMasterTO.getLodgementId());
                                instMap.put("INSTRUCTION", objLodgementInstTO.getInstruction());
                                List lstInst = sqlMap.executeQueryForList("getBillsExtInst", instMap);
                                instMap = null;
                                if (lstInst != null && lstInst.size() > 0) {
                                    sqlMap.executeUpdate("updateLodgementInstructionsTO", objLodgementInstTO);
                                } else {
                                    sqlMap.executeUpdate("insertLodgementInstructionsTO", objLodgementInstTO);
                                }
                                lstInst = null;
                            }
                            status = null;
                        }
                    }
                    //OVERDUE INTEREST CODING
                    if ((CommonUtil.convertObjToDouble(objLodgementMasterTO.getRateForDelay()).doubleValue()) > 0) {
                        HashMap overDue = new HashMap();
                        overDue.put("LODGEMENT_ID", CommonUtil.convertObjToStr(objLodgementMasterTO.getLodgementId()));
                        overDue.put("CUR_DATE", currDt.clone());
                        List dueLst = sqlMap.executeQueryForList("getBillsOverDueIntDetails", overDue);
                        overDue = null;
                        if (dueLst != null && dueLst.size() > 0) {
                            overDue = (HashMap) dueLst.get(0);
                            double amount = CommonUtil.convertObjToDouble(objLodgementChequeTO.getInstrumentAmount()).doubleValue()
                                    - marginAmt;
                            double rateOfInterest = CommonUtil.convertObjToDouble(objLodgementMasterTO.getRateForDelay()).doubleValue();
                            double period = CommonUtil.convertObjToDouble(overDue.get("INT_PERIOD")).doubleValue();
                            double yearDays = 36500;
                            double intAmount = (amount * rateOfInterest * period) / yearDays;
                            System.out.println("!!!!!!!!amount" + amount + "#####rateOfInterest" + rateOfInterest + "!!!!!!!!!period" + period + "####intAmount" + intAmount);
                            intAmount = (double) getNearest((long) (intAmount * 100), 100) / 100;
                            System.out.println("####intAmount" + intAmount);
                            if (intAmount > 0) {
                                //transaction Coding
                                txMap = new HashMap();
                                txMap.put(TransferTrans.DR_AC_HD, null);
                                txMap.put(TransferTrans.DR_PROD_TYPE, objLodgementMasterTO.getProdType());
                                txMap.put(TransferTrans.DR_PROD_ID, objLodgementMasterTO.getBorrowProdId());
                                txMap.put(TransferTrans.DR_ACT_NUM, objLodgementMasterTO.getBorrowAcctNum());
                                txMap.put(TransferTrans.DR_BRANCH, branchID);
                                txMap.put(TransferTrans.CURRENCY, "INR");
                                //                                txMap.put(TransferTrans.DR_INST_TYPE, objLodgementMasterTO.getBillsStatus());
                                txMap.put(TransferTrans.PARTICULARS, "Int Coll(Delayed) LodgeID : " + objLodgementMasterTO.getLodgementId() + " Dishonour");
                                txMap.put(TransferTrans.AUTHORIZE_STATUS_2, "ENTERED_AMOUNT");
                                transAmt = intAmount;
                                txMap.put("TRANS_MOD_TYPE", objLodgementMasterTO.getProdType());
                                txMap.put("SCREEN_NAME", (String) obj.get(CommonConstants.SCREEN));// Added by nithya on 23-09-2016 for inserting screen name for transactions
                                transferTo = transactionDAO.addTransferDebitLocal(txMap, transAmt);
                                transferTo.setAuthorizeRemarks("FROM_BILLS_MODULE");
                                transferTo.setInstType(objLodgementMasterTO.getBillsStatus());
                                transferList.add(transferTo);
                                txMap.put(TransferTrans.CR_AC_HD, (String) overDue.get("INT_AC_HD"));
                                txMap.put(TransferTrans.CR_BRANCH, branchID);
                                txMap.put(TransferTrans.CURRENCY, "INR");
                                txMap.put(TransferTrans.CR_PROD_TYPE, TransactionFactory.GL);
                                txMap.put("TRANS_MOD_TYPE", "GL");
                                txMap.put(TransferTrans.PARTICULARS, "Int Coll(Delayed) LodgeID : " + objLodgementMasterTO.getLodgementId() + " Dishonour");
                                txMap.put(TransferTrans.AUTHORIZE_STATUS_2, "ENTERED_AMOUNT");
                                txMap.put("SCREEN_NAME", (String) obj.get(CommonConstants.SCREEN));// Added by nithya on 23-09-2016 for inserting screen name for transactions
                                transferTo = transactionDAO.addTransferCreditLocal(txMap, transAmt);
                                transferTo.setAuthorizeRemarks("FROM_BILLS_MODULE");
                                transferTo.setInstType(objLodgementMasterTO.getBillsStatus());
                                transferList.add(transferTo);
                                transactionDAO.setTransactionType(transactionDAO.TRANSFER);
                                transactionDAO.doTransferLocal(transferList, branchID);
                            }
                        }
                    }
                }
            }
            if (hashData.get("OPERATES_LIKE").equals("INWARD")) {
                if (objLodgementMasterTO.getTranType().equals("TRANSFER")) {
                    if (CommonUtil.convertObjToDouble(objLodgementRemitTO.getRemitFavouring()).doubleValue() > 0) {
                        txMap = new HashMap();
                        transactionDAO.setCommandMode(CommonConstants.TOSTATUS_INSERT);
                        if (!objLodgementMasterTO.getProdType().equals("GL")) {
                            txMap.put(TransferTrans.DR_AC_HD, null);
                            txMap.put(TransferTrans.DR_ACT_NUM, objLodgementMasterTO.getBorrowAcctNum());
                            txMap.put(TransferTrans.DR_PROD_ID, objLodgementMasterTO.getBorrowProdId());
                        } else {
                            txMap.put(TransferTrans.DR_AC_HD, objLodgementMasterTO.getBorrowAcctNum());
                        }
                        txMap.put(TransferTrans.DR_BRANCH, branchID);
                        txMap.put(TransferTrans.CURRENCY, "INR");
                        txMap.put(TransferTrans.DR_PROD_TYPE, objLodgementMasterTO.getProdType());
                        txMap.put(TransferTrans.CR_AC_HD, (String) acHeads.get("POSTAGE_AC_HD")); // credit to Remittance Issue account head......
                        txMap.put(TransferTrans.CR_BRANCH, branchID);
                        txMap.put(TransferTrans.CR_PROD_TYPE, TransactionFactory.GL);
                        txMap.put(TransferTrans.PARTICULARS, "LodgeID : " + objLodgementMasterTO.getLodgementId() + " Dishonour");
                        txMap.put(TransferTrans.AUTHORIZE_STATUS_2, "ENTERED_AMOUNT");
                        transAmt = CommonUtil.convertObjToDouble(objLodgementRemitTO.getRemitFavouring()).doubleValue();
                        // txMap.put(TransferTrans.PARTICULARS, objLodgementMasterTO.getBorrowProdId());
                        transactionDAO.setTransType(CommonConstants.CREDIT);
                        txMap.put("TRANS_MOD_TYPE", objLodgementMasterTO.getProdType());
                        //                            txMap.put(TransferTrans.DR_INST_TYPE, objLodgementMasterTO.getBillsStatus());
                        txMap.put("SCREEN_NAME", (String) obj.get(CommonConstants.SCREEN));// Added by nithya on 23-09-2016 for inserting screen name for transactions
                        transferTo = transactionDAO.addTransferDebitLocal(txMap, transAmt);
                        transferTo.setInstType(objLodgementMasterTO.getBillsStatus());
                        transferList.add(transferTo);
                        txMap.put(TransferTrans.PARTICULARS, "LodgeID : " + objLodgementMasterTO.getLodgementId() + " Dishonour");
                        txMap.put(TransferTrans.AUTHORIZE_STATUS_2, "ENTERED_AMOUNT");
                        transAmt = CommonUtil.convertObjToDouble(objLodgementRemitTO.getRemitFavouring()).doubleValue();
                        transactionDAO.setTransType(CommonConstants.DEBIT);
                        txMap.put("TRANS_MOD_TYPE", "GL");
                        txMap.put("SCREEN_NAME", (String) obj.get(CommonConstants.SCREEN));// Added by nithya on 23-09-2016 for inserting screen name for transactions
                        //                            txMap.put(TransferTrans.DR_INST_TYPE, objLodgementMasterTO.getBillsStatus());
                        transferTo = transactionDAO.addTransferCreditLocal(txMap, transAmt);
                        transferTo.setInstType(objLodgementMasterTO.getBillsStatus());
                        transferList.add(transferTo);
                        transAmt = 0;
                        transactionDAO.setTransactionType(transactionDAO.TRANSFER);
                        transactionDAO.doTransferLocal(transferList, branchID);
                        //cash tran
                        transAmt = CommonUtil.convertObjToDouble(objLodgementRemitTO.getRemitFavouring()).doubleValue();
                        txMap = new HashMap();
                        TransactionTO transTO = new TransactionTO();
                        ArrayList cashList = new ArrayList();
                        transactionDAO.setCommandMode(CommonConstants.TOSTATUS_INSERT);
                        txMap.put(TransferTrans.DR_AC_HD, (String) acHeads.get("POSTAGE_AC_HD"));
                        txMap.put(TransferTrans.DR_BRANCH, branchID);
                        txMap.put(TransferTrans.CURRENCY, "INR");
                        txMap.put(TransferTrans.DR_PROD_TYPE, TransactionFactory.GL);
                        txMap.put(TransferTrans.PARTICULARS, "LodgeID : " + objLodgementMasterTO.getLodgementId() + " Dishonour");
                        txMap.put(TransferTrans.AUTHORIZE_STATUS_2, "ENTERED_AMOUNT");
                        //                            txMap.put(TransferTrans.DR_INST_TYPE, objLodgementMasterTO.getBillsStatus());
                        transactionDAO.setTransType(CommonConstants.CREDIT);
                        txMap.put("TRANS_MOD_TYPE", "GL");
                        txMap.put("SCREEN_NAME", (String) obj.get(CommonConstants.SCREEN));// Added by nithya on 23-09-2016 for inserting screen name for transactions
                        transactionDAO.addTransferDebit(txMap, transAmt);
                        transactionDAO.deleteTxList();
                        transTO.setCommand(CommonConstants.TOSTATUS_INSERT);
                        transTO.setTransType("CASH");
                        transTO.setTransAmt(new Double(transAmt));
                        transTO.setBatchId(objLodgementMasterTO.getLodgementId());
                        transTO.setInstType(objLodgementMasterTO.getBillsStatus());
                        cashList.add(transTO);
                        transactionDAO.addCashList(cashList);
                        transactionDAO.doTransfer();

                    }
                    double temp = 0;
                    for (int i = 0; i < instructionList.size(); i++) {
                        objLodgementInstTO = (LodgementInstructionsTO) instructionList.get(i);
                        String status = CommonUtil.convertObjToStr(objLodgementInstTO.getStatus());
                        if (!status.equals("DELETED")) {
                            if (objLodgementInstTO.getInstruction().equals("DISHONOUR_CHARGES")) {
                                transactionDAO.setCommandMode(CommonConstants.TOSTATUS_INSERT);
                                temp = temp + ((CommonUtil.convertObjToDouble(objLodgementInstTO.getAmount()).doubleValue())
                                        + (CommonUtil.convertObjToDouble(objLodgementInstTO.getServiceTx()).doubleValue()));
                                if (!objLodgementMasterTO.getProdType().equals("GL")) {
                                    txMap.put(TransferTrans.DR_AC_HD, null);
                                    txMap.put(TransferTrans.DR_ACT_NUM, objLodgementMasterTO.getBorrowAcctNum());
                                    txMap.put(TransferTrans.DR_PROD_ID, objLodgementMasterTO.getBorrowProdId());
                                } else {
                                    txMap.put(TransferTrans.DR_AC_HD, objLodgementMasterTO.getBorrowAcctNum());
                                }
                                txMap.put(TransferTrans.DR_BRANCH, branchID);
                                txMap.put(TransferTrans.CURRENCY, "INR");
                                txMap.put(TransferTrans.DR_PROD_TYPE, objLodgementMasterTO.getProdType());
                                txMap.put(TransferTrans.CR_BRANCH, branchID);
                                //                                txMap.put(TransferTrans.DR_INST_TYPE, objLodgementMasterTO.getBillsStatus());
                                txMap.put(TransferTrans.PARTICULARS, "Dishonour Chrg LodgeID : " + objLodgementMasterTO.getLodgementId() + " Dishonour");
                                txMap.put(TransferTrans.AUTHORIZE_STATUS_2, "ENTERED_AMOUNT");
                                transAmt = temp;
                                txMap.put("TRANS_MOD_TYPE", objLodgementMasterTO.getProdType());
                                transactionDAO.setTransType(CommonConstants.CREDIT);
                                txMap.put("SCREEN_NAME", (String) obj.get(CommonConstants.SCREEN));// Added by nithya on 23-09-2016 for inserting screen name for transactions
                                transferTo = transactionDAO.addTransferDebitLocal(txMap, transAmt);
                                transferTo.setInstType(objLodgementMasterTO.getBillsStatus());
                                transferList.add(transferTo);
                                if ((CommonUtil.convertObjToDouble(objLodgementInstTO.getAmount()).doubleValue()) > 0) {
                                    txMap.put(TransferTrans.CR_AC_HD, (String) acHeads.get("CHRG_AC_HD"));
                                    txMap.put(TransferTrans.CR_PROD_TYPE, TransactionFactory.GL);
                                    //                                    txMap.put(TransferTrans.DR_INST_TYPE, objLodgementMasterTO.getBillsStatus());
                                    txMap.put(TransferTrans.PARTICULARS, "Dishonour Chrg LodgeID : " + objLodgementMasterTO.getLodgementId() + " Dishonour");
                                    txMap.put(TransferTrans.AUTHORIZE_STATUS_2, "ENTERED_AMOUNT");
                                    transAmt = CommonUtil.convertObjToDouble(objLodgementInstTO.getAmount()).doubleValue();
                                    transactionDAO.setTransType(CommonConstants.DEBIT);
                                    txMap.put("TRANS_MOD_TYPE", "GL");
                                    txMap.put("SCREEN_NAME", (String) obj.get(CommonConstants.SCREEN));// Added by nithya on 23-09-2016 for inserting screen name for transactions
                                    transferTo = transactionDAO.addTransferCreditLocal(txMap, transAmt);
                                    transferTo.setInstType(objLodgementMasterTO.getBillsStatus());
                                    transferList.add(transferTo);
                                    transAmt = 0;
                                }
                                if ((CommonUtil.convertObjToDouble(objLodgementInstTO.getServiceTx()).doubleValue()) > 0) {
                                    txMap.put(TransferTrans.CR_AC_HD, (String) acHeads.get("SERVICE_TAX_HD"));
                                    txMap.put(TransferTrans.CR_PROD_TYPE, TransactionFactory.GL);
                                    //                                    txMap.put(TransferTrans.DR_INST_TYPE, objLodgementMasterTO.getBillsStatus());
                                    txMap.put(TransferTrans.AUTHORIZE_STATUS_2, "ENTERED_AMOUNT");
                                    txMap.put(TransferTrans.PARTICULARS, "ServiceTax LodgeID : " + objLodgementMasterTO.getLodgementId() + " Dishonour");
                                    transAmt = CommonUtil.convertObjToDouble(objLodgementInstTO.getServiceTx()).doubleValue();
                                    transactionDAO.setTransType(CommonConstants.DEBIT);
                                    txMap.put("TRANS_MOD_TYPE", "GL");
                                    txMap.put("SCREEN_NAME", (String) obj.get(CommonConstants.SCREEN));// Added by nithya on 23-09-2016 for inserting screen name for transactions
                                    transferTo = transactionDAO.addTransferCreditLocal(txMap, transAmt);
                                    transferTo.setInstType(objLodgementMasterTO.getBillsStatus());
                                    transferList.add(transferTo);
                                    transAmt = 0;
                                }
                                transactionDAO.setTransactionType(transactionDAO.TRANSFER);
                                transactionDAO.doTransferLocal(transferList, branchID);
                                //INSERT INSTRUCTION
                                HashMap instMap = new HashMap();
                                instMap.put("LODGEMENT_ID", objLodgementMasterTO.getLodgementId());
                                instMap.put("INSTRUCTION", objLodgementInstTO.getInstruction());
                                List lstInst = sqlMap.executeQueryForList("getBillsExtInst", instMap);
                                instMap = null;
                                if (lstInst != null && lstInst.size() > 0) {
                                    sqlMap.executeUpdate("updateLodgementInstructionsTO", objLodgementInstTO);
                                } else {
                                    sqlMap.executeUpdate("insertLodgementInstructionsTO", objLodgementInstTO);
                                }
                                lstInst = null;
                            }
                            status = null;
                        }
                    }
                }
                if (objLodgementMasterTO.getTranType().equals("CASH")) {
                    if (CommonUtil.convertObjToDouble(objLodgementRemitTO.getRemitFavouring()).doubleValue() > 0) {
                        //cash tran credit
                        transAmt = CommonUtil.convertObjToDouble(objLodgementRemitTO.getRemitFavouring()).doubleValue();
                        txMap = new HashMap();
                        TransactionTO transTO = new TransactionTO();
                        ArrayList cashList = new ArrayList();
                        transactionDAO.setCommandMode(CommonConstants.TOSTATUS_INSERT);
                        txMap.put(TransferTrans.CR_AC_HD, (String) acHeads.get("POSTAGE_AC_HD"));
                        txMap.put(TransferTrans.CR_BRANCH, branchID);
                        txMap.put(TransferTrans.CURRENCY, "INR");
                        txMap.put(TransferTrans.CR_PROD_TYPE, TransactionFactory.GL);
                        //                                txMap.put(TransferTrans.DR_INST_TYPE, objLodgementMasterTO.getBillsStatus());
                        txMap.put(TransferTrans.PARTICULARS, "LodgeID : " + objLodgementMasterTO.getLodgementId() + " Dishonour");
                        txMap.put(TransferTrans.AUTHORIZE_STATUS_2, "ENTERED_AMOUNT");
                        txMap.put("TRANS_MOD_TYPE", "GL");
                        transactionDAO.setTransType(CommonConstants.DEBIT);
                        txMap.put("SCREEN_NAME", (String) obj.get(CommonConstants.SCREEN));// Added by nithya on 23-09-2016 for inserting screen name for transactions
                        transactionDAO.addTransferCredit(txMap, transAmt);
                        transactionDAO.deleteTxList();
                        transTO.setCommand(CommonConstants.TOSTATUS_INSERT);
                        transTO.setTransType("CASH");
                        transTO.setTransAmt(new Double(transAmt));
                        transTO.setBatchId(objLodgementMasterTO.getLodgementId());
                        transTO.setInstType(objLodgementMasterTO.getBillsStatus());
                        cashList.add(transTO);
                        System.out.println("@@@@@@f" + cashList);
                        transactionDAO.addCashList(cashList);
                        transactionDAO.doTransfer();
                        //cash tran debit
                        transAmt = CommonUtil.convertObjToDouble(objLodgementRemitTO.getRemitFavouring()).doubleValue();
                        txMap = new HashMap();
                        transTO = new TransactionTO();
                        cashList = new ArrayList();
                        transactionDAO.setCommandMode(CommonConstants.TOSTATUS_INSERT);
                        txMap.put(TransferTrans.DR_AC_HD, (String) acHeads.get("POSTAGE_AC_HD"));
                        txMap.put(TransferTrans.DR_BRANCH, branchID);
                        txMap.put(TransferTrans.CURRENCY, "INR");
                        txMap.put(TransferTrans.DR_PROD_TYPE, TransactionFactory.GL);
                        //                                txMap.put(TransferTrans.DR_INST_TYPE, objLodgementMasterTO.getBillsStatus());
                        txMap.put(TransferTrans.PARTICULARS, "LodgeID : " + objLodgementMasterTO.getLodgementId() + " Dishonour");
                        txMap.put(TransferTrans.AUTHORIZE_STATUS_2, "ENTERED_AMOUNT");
                        txMap.put("TRANS_MOD_TYPE", "GL");
                        txMap.put("SCREEN_NAME", (String) obj.get(CommonConstants.SCREEN));// Added by nithya on 23-09-2016 for inserting screen name for transactions
                        transactionDAO.setTransType(CommonConstants.CREDIT);
                        transactionDAO.addTransferDebit(txMap, transAmt);
                        transactionDAO.deleteTxList();
                        transTO.setCommand(CommonConstants.TOSTATUS_INSERT);
                        transTO.setTransType("CASH");
                        transTO.setTransAmt(new Double(transAmt));
                        transTO.setBatchId(objLodgementMasterTO.getLodgementId());
                        transTO.setInstType(objLodgementMasterTO.getBillsStatus());
                        cashList.add(transTO);
                        System.out.println("@@@@@@f" + cashList);
                        transactionDAO.addCashList(cashList);
                        transactionDAO.doTransfer();
                    }

                    //TRANSFER TRAN
                    double temp = 0;
                    for (int i = 0; i < instructionList.size(); i++) {
                        objLodgementInstTO = (LodgementInstructionsTO) instructionList.get(i);
                        String status = CommonUtil.convertObjToStr(objLodgementInstTO.getStatus());
                        if (!status.equals("DELETED")) {
                            if (objLodgementInstTO.getInstruction().equals("DISHONOUR_CHARGES")) {
                                transAmt = CommonUtil.convertObjToDouble(objLodgementInstTO.getAmount()).doubleValue();
                                if (transAmt > 0) {
                                    txMap = new HashMap();
                                    TransactionTO transTO = new TransactionTO();
                                    ArrayList cashList = new ArrayList();
                                    transactionDAO.setCommandMode(CommonConstants.TOSTATUS_INSERT);
                                    txMap.put(TransferTrans.CR_AC_HD, (String) acHeads.get("CHRG_AC_HD"));
                                    txMap.put(TransferTrans.CR_BRANCH, branchID);
                                    txMap.put(TransferTrans.CURRENCY, "INR");
                                    txMap.put(TransferTrans.CR_PROD_TYPE, TransactionFactory.GL);
                                    //                                        txMap.put(TransferTrans.DR_INST_TYPE, objLodgementMasterTO.getBillsStatus());
                                    txMap.put(TransferTrans.PARTICULARS, "LodgeID : " + objLodgementMasterTO.getLodgementId() + " Dishonour");
                                    txMap.put(TransferTrans.AUTHORIZE_STATUS_2, "ENTERED_AMOUNT");
                                    txMap.put("TRANS_MOD_TYPE", "GL");
                                    txMap.put("SCREEN_NAME", (String) obj.get(CommonConstants.SCREEN));// Added by nithya on 23-09-2016 for inserting screen name for transactions
                                    transactionDAO.setTransType(CommonConstants.DEBIT);
                                    transactionDAO.addTransferCredit(txMap, transAmt);
                                    transactionDAO.deleteTxList();
                                    transTO.setCommand(CommonConstants.TOSTATUS_INSERT);
                                    transTO.setTransType("CASH");
                                    transTO.setTransAmt(new Double(transAmt));
                                    transTO.setBatchId(objLodgementMasterTO.getLodgementId());
                                    transTO.setInstType(objLodgementMasterTO.getBillsStatus());
                                    cashList.add(transTO);
                                    transactionDAO.addCashList(cashList);
                                    transactionDAO.doTransfer();
                                    transAmt = 0;
                                }
                                transAmt = CommonUtil.convertObjToDouble(objLodgementInstTO.getServiceTx()).doubleValue();
                                if (transAmt > 0) {
                                    txMap = new HashMap();
                                    TransactionTO transTO = new TransactionTO();
                                    ArrayList cashList = new ArrayList();
                                    transactionDAO.setCommandMode(CommonConstants.TOSTATUS_INSERT);
                                    txMap.put(TransferTrans.CR_AC_HD, (String) acHeads.get("COMM_AC_HD"));
                                    txMap.put(TransferTrans.CR_BRANCH, branchID);
                                    txMap.put(TransferTrans.CURRENCY, "INR");
                                    txMap.put(TransferTrans.CR_PROD_TYPE, TransactionFactory.GL);
                                    txMap.put("TRANS_MOD_TYPE", "GL");
                                    //                                        txMap.put(TransferTrans.DR_INST_TYPE, objLodgementMasterTO.getBillsStatus());
                                    txMap.put(TransferTrans.PARTICULARS, "LodgeID : " + objLodgementMasterTO.getLodgementId() + " Dishonour");
                                    txMap.put(TransferTrans.AUTHORIZE_STATUS_2, "ENTERED_AMOUNT");
                                    txMap.put("SCREEN_NAME", (String) obj.get(CommonConstants.SCREEN));// Added by nithya on 23-09-2016 for inserting screen name for transactions
                                    transactionDAO.setTransType(CommonConstants.DEBIT);
                                    transactionDAO.addTransferCredit(txMap, transAmt);
                                    transactionDAO.deleteTxList();
                                    transTO.setCommand(CommonConstants.TOSTATUS_INSERT);
                                    transTO.setTransType("CASH");
                                    transTO.setTransAmt(new Double(transAmt));
                                    transTO.setBatchId(objLodgementMasterTO.getLodgementId());
                                    transTO.setInstType(objLodgementMasterTO.getBillsStatus());
                                    cashList.add(transTO);
                                    transactionDAO.addCashList(cashList);
                                    transactionDAO.doTransfer();
                                    transAmt = 0;
                                }
                                //INSERT INSTRUCTION
                                HashMap instMap = new HashMap();
                                instMap.put("LODGEMENT_ID", objLodgementMasterTO.getLodgementId());
                                instMap.put("INSTRUCTION", objLodgementInstTO.getInstruction());
                                List lstInst = sqlMap.executeQueryForList("getBillsExtInst", instMap);
                                instMap = null;
                                if (lstInst != null && lstInst.size() > 0) {
                                    sqlMap.executeUpdate("updateLodgementInstructionsTO", objLodgementInstTO);
                                } else {
                                    sqlMap.executeUpdate("insertLodgementInstructionsTO", objLodgementInstTO);
                                }
                                lstInst = null;
                            }
                            status = null;
                        }
                    }
                }
            }
        }
        if (objLodgementMasterTO.getBillsStatus().equals("CLOSURE")) {
            if (hashData.get("OPERATES_LIKE").equals("OUTWARD")) {
                if (objLodgementMasterTO.getTranType().equals("TRANSFER")) {
                    //margin amt credit to customer
                    double margin = CommonUtil.convertObjToDouble(objLodgementBillRatesTO.getRateForDelay()).doubleValue();
                    double cheqAmt = CommonUtil.convertObjToDouble(objLodgementChequeTO.getInstrumentAmount()).doubleValue();
                    double marginAmt = 0.0;
                    marginAmt = (cheqAmt * margin) / 100;
                    marginAmt = roundInterest(marginAmt);
                    //DEBIT 950
                    double temp = 0;
                    double otherBank = 0;
                    double diffAmt = 0;
                    txMap = new HashMap();
                    temp = CommonUtil.convertObjToDouble(objLodgementChequeTO.getInstrumentAmount()).doubleValue() - marginAmt;
                    for (int i = 0; i < instructionList.size(); i++) {
                        objLodgementInstTO = (LodgementInstructionsTO) instructionList.get(i);
                        String status = CommonUtil.convertObjToStr(objLodgementInstTO.getStatus());
                        if (!status.equals("DELETED")) {
                            if (objLodgementInstTO.getInstruction().equals("OTHER_BANK_CHARGES")) {
                                objLodgementInstTO = (LodgementInstructionsTO) instructionList.get(i);
                                otherBank = otherBank + CommonUtil.convertObjToDouble(objLodgementInstTO.getAmount()).doubleValue();
                            }
                            status = null;
                        }
                    }
                    diffAmt = temp - otherBank;
                    txMap = new HashMap();
                    txMap.put(TransferTrans.DR_AC_HD, null);
                    txMap.put(TransferTrans.DR_PROD_TYPE, objLodgementMasterTO.getProdType());
                    txMap.put(TransferTrans.DR_PROD_ID, objLodgementMasterTO.getBorrowProdId());
                    txMap.put(TransferTrans.DR_ACT_NUM, objLodgementMasterTO.getBorrowAcctNum());
                    txMap.put(TransferTrans.DR_BRANCH, branchID);
                    txMap.put(TransferTrans.CURRENCY, "INR");
                    //                                txMap.put(TransferTrans.DR_INST_TYPE, objLodgementMasterTO.getBillsStatus());
                    txMap.put(TransferTrans.PARTICULARS, "LodgeID : " + objLodgementMasterTO.getLodgementId() + " Closure");
                    txMap.put(TransferTrans.AUTHORIZE_STATUS_2, "ENTERED_AMOUNT");
                    transAmt = diffAmt;
                    txMap.put("TRANS_MOD_TYPE", objLodgementMasterTO.getProdType());
                    txMap.put("SCREEN_NAME", (String) obj.get(CommonConstants.SCREEN));// Added by nithya on 23-09-2016 for inserting screen name for transactions
                    transferTo = transactionDAO.addTransferDebitLocal(txMap, transAmt);
                    transferTo.setAuthorizeRemarks("FROM_BILLS_MODULE");
                    transferTo.setInstType(objLodgementMasterTO.getBillsStatus());
                    transferList.add(transferTo);

                    txMap.put(TransferTrans.CR_AC_HD, null);
                    txMap.put(TransferTrans.CR_PROD_TYPE, TransactionFactory.ADVANCES);
                    txMap.put("TRANS_MOD_TYPE", "AD");
                    txMap.put(TransferTrans.CR_PROD_ID, objLodgementMasterTO.getCbpProdID());
                    txMap.put(TransferTrans.CR_ACT_NUM, objLodgementMasterTO.getCbpActNum());
                    txMap.put(TransferTrans.CR_BRANCH, branchID);
                    txMap.put(TransferTrans.PARTICULARS, "LodgeID : " + objLodgementMasterTO.getLodgementId() + " Closure");
                    txMap.put(TransferTrans.AUTHORIZE_STATUS_2, "ENTERED_AMOUNT");

                    //                                txMap.put(TransferTrans.CR_AC_HD, (String)acHeads.get("GL_AC_HD")) ;
                    //                                txMap.put(TransferTrans.CR_BRANCH, branchID);
                    txMap.put(TransferTrans.CURRENCY, "INR");
                    txMap.put("SCREEN_NAME", (String) obj.get(CommonConstants.SCREEN));// Added by nithya on 23-09-2016 for inserting screen name for transactions
                    //                                txMap.put(TransferTrans.CR_PROD_TYPE, TransactionFactory.GL);
                    transferTo = transactionDAO.addTransferCreditLocal(txMap, transAmt);
                    transferTo.setAuthorizeRemarks("FROM_BILLS_MODULE");
                    transferTo.setInstType(objLodgementMasterTO.getBillsStatus());
                    transferList.add(transferTo);
                    transactionDAO.setTransactionType(transactionDAO.TRANSFER);
                    transactionDAO.doTransferLocal(transferList, branchID);
                    //-------------------------------------------
                    if (CommonUtil.convertObjToDouble(objLodgementRemitTO.getRemitFavouring()).doubleValue() > 0) {
                        txMap = new HashMap();
                        transactionDAO.setCommandMode(CommonConstants.TOSTATUS_INSERT);
                        if (!objLodgementMasterTO.getProdType().equals("GL")) {
                            txMap.put(TransferTrans.DR_AC_HD, null);
                            txMap.put(TransferTrans.DR_ACT_NUM, objLodgementMasterTO.getBorrowAcctNum());
                            txMap.put(TransferTrans.DR_PROD_ID, objLodgementMasterTO.getBorrowProdId());
                        } else {
                            txMap.put(TransferTrans.DR_AC_HD, objLodgementMasterTO.getBorrowAcctNum());
                        }
                        //                            txMap.put(TransferTrans.DR_INST_TYPE, objLodgementMasterTO.getBillsStatus());
                        txMap.put(TransferTrans.DR_BRANCH, branchID);
                        txMap.put(TransferTrans.CURRENCY, "INR");
                        txMap.put(TransferTrans.DR_PROD_TYPE, objLodgementMasterTO.getProdType());
                        txMap.put(TransferTrans.CR_AC_HD, (String) acHeads.get("POSTAGE_AC_HD")); // credit to Remittance Issue account head......
                        txMap.put(TransferTrans.CR_BRANCH, branchID);
                        txMap.put(TransferTrans.CR_PROD_TYPE, TransactionFactory.GL);
                        txMap.put(TransferTrans.PARTICULARS, "VPL Chrg LodgeID : " + objLodgementMasterTO.getLodgementId() + " Closure");
                        txMap.put(TransferTrans.AUTHORIZE_STATUS_2, "ENTERED_AMOUNT");
                        txMap.put("SCREEN_NAME", (String) obj.get(CommonConstants.SCREEN));// Added by nithya on 23-09-2016 for inserting screen name for transactions
                        transAmt = CommonUtil.convertObjToDouble(objLodgementRemitTO.getRemitFavouring()).doubleValue();
                        //txMap.put(TransferTrans.PARTICULARS, objLodgementMasterTO.getBorrowProdId());
                        transactionDAO.setTransType(CommonConstants.CREDIT);
                        txMap.put("TRANS_MOD_TYPE", objLodgementMasterTO.getProdType());
                        transferTo = transactionDAO.addTransferDebitLocal(txMap, transAmt);
                        txMap.put(TransferTrans.PARTICULARS, "VPL Chrg LodgeID : " + objLodgementMasterTO.getLodgementId() + " Closure");
                        txMap.put(TransferTrans.AUTHORIZE_STATUS_2, "ENTERED_AMOUNT");
                        transferTo.setAuthorizeRemarks("FROM_BILLS_MODULE");
                        transferTo.setInstType(objLodgementMasterTO.getBillsStatus());
                        transferList.add(transferTo);
                        //                            txMap.put(TransferTrans.PARTICULARS,"A/c No-" + " " + objLodgementMasterTO.getBorrowAcctNum());
                        transAmt = CommonUtil.convertObjToDouble(objLodgementRemitTO.getRemitFavouring()).doubleValue();
                        transactionDAO.setTransType(CommonConstants.DEBIT);
                        txMap.put("TRANS_MOD_TYPE", "GL");
                        txMap.put("SCREEN_NAME", (String) obj.get(CommonConstants.SCREEN));// Added by nithya on 23-09-2016 for inserting screen name for transactions
                        transferTo = transactionDAO.addTransferCreditLocal(txMap, transAmt);
                        transferTo.setAuthorizeRemarks("FROM_BILLS_MODULE");
                        transferTo.setInstType(objLodgementMasterTO.getBillsStatus());
                        transferList.add(transferTo);
                        transAmt = 0;
                        transactionDAO.setTransactionType(transactionDAO.TRANSFER);
                        transactionDAO.doTransferLocal(transferList, branchID);
                        //cash tran
                        transAmt = CommonUtil.convertObjToDouble(objLodgementRemitTO.getRemitFavouring()).doubleValue();
                        txMap = new HashMap();
                        TransactionTO transTO = new TransactionTO();
                        ArrayList cashList = new ArrayList();
                        transactionDAO.setCommandMode(CommonConstants.TOSTATUS_INSERT);

                        txMap.put(TransferTrans.DR_AC_HD, (String) acHeads.get("POSTAGE_AC_HD"));
                        //                            txMap.put(TransferTrans.DR_INST_TYPE, objLodgementMasterTO.getBillsStatus());
                        txMap.put(TransferTrans.DR_BRANCH, branchID);
                        txMap.put(TransferTrans.CURRENCY, "INR");
                        txMap.put(TransferTrans.DR_PROD_TYPE, TransactionFactory.GL);
                        txMap.put("TRANS_MOD_TYPE", "GL");
                        txMap.put(TransferTrans.PARTICULARS, "LodgeID : " + objLodgementMasterTO.getLodgementId() + " Closure");
                        txMap.put(TransferTrans.PARTICULARS, "VPL Chrg LodgeID : " + objLodgementMasterTO.getLodgementId() + " Closure");
                        txMap.put(TransferTrans.AUTHORIZE_STATUS_2, "ENTERED_AMOUNT");
                        transactionDAO.setTransType(CommonConstants.CREDIT);
                        txMap.put("SCREEN_NAME", (String) obj.get(CommonConstants.SCREEN));// Added by nithya on 23-09-2016 for inserting screen name for transactions
                        transactionDAO.addTransferDebit(txMap, transAmt);
                        transactionDAO.deleteTxList();
                        transTO.setCommand(CommonConstants.TOSTATUS_INSERT);
                        transTO.setTransType("CASH");
                        transTO.setTransAmt(new Double(transAmt));
                        transTO.setBatchId(objLodgementMasterTO.getLodgementId());
                        transTO.setInstType(objLodgementMasterTO.getBillsStatus());
                        cashList.add(transTO);
                        transactionDAO.addCashList(cashList);
                        transactionDAO.doTransfer();
                    }
                    temp = 0;
                    for (int i = 0; i < instructionList.size(); i++) {
                        objLodgementInstTO = (LodgementInstructionsTO) instructionList.get(i);
                        String status = CommonUtil.convertObjToStr(objLodgementInstTO.getStatus());
                        if (!status.equals("DELETED")) {
                            if (objLodgementInstTO.getInstruction().equals("CLOSURE_CHARGES")) {
                                transactionDAO.setCommandMode(CommonConstants.TOSTATUS_INSERT);
                                temp = temp + ((CommonUtil.convertObjToDouble(objLodgementInstTO.getAmount()).doubleValue())
                                        + (CommonUtil.convertObjToDouble(objLodgementInstTO.getServiceTx()).doubleValue()));
                                if (!objLodgementMasterTO.getProdType().equals("GL")) {
                                    txMap.put(TransferTrans.DR_AC_HD, null);
                                    txMap.put(TransferTrans.DR_ACT_NUM, objLodgementMasterTO.getBorrowAcctNum());
                                    txMap.put(TransferTrans.DR_PROD_ID, objLodgementMasterTO.getBorrowProdId());
                                } else {
                                    txMap.put(TransferTrans.DR_AC_HD, objLodgementMasterTO.getBorrowAcctNum());
                                }
                                txMap.put(TransferTrans.DR_BRANCH, branchID);
                                txMap.put(TransferTrans.CURRENCY, "INR");
                                txMap.put(TransferTrans.DR_PROD_TYPE, objLodgementMasterTO.getProdType());
                                txMap.put(TransferTrans.PARTICULARS, "Closure Chrg LodgeID : " + objLodgementMasterTO.getLodgementId() + " Closure");
                                txMap.put(TransferTrans.CR_BRANCH, branchID);
                                txMap.put(TransferTrans.AUTHORIZE_STATUS_2, "ENTERED_AMOUNT");
                                transAmt = temp;
                                transactionDAO.setTransType(CommonConstants.CREDIT);
                                txMap.put("TRANS_MOD_TYPE", objLodgementMasterTO.getProdType());
                                txMap.put("SCREEN_NAME", (String) obj.get(CommonConstants.SCREEN));// Added by nithya on 23-09-2016 for inserting screen name for transactions
                                transferTo = transactionDAO.addTransferDebitLocal(txMap, transAmt);
                                transferTo.setAuthorizeRemarks("FROM_BILLS_MODULE");
                                transferTo.setInstType(objLodgementMasterTO.getBillsStatus());
                                transferList.add(transferTo);
                                if ((CommonUtil.convertObjToDouble(objLodgementInstTO.getAmount()).doubleValue()) > 0) {
                                    txMap.put(TransferTrans.CR_AC_HD, (String) acHeads.get("CHRG_AC_HD"));
                                    txMap.put(TransferTrans.CR_PROD_TYPE, TransactionFactory.GL);
                                    txMap.put(TransferTrans.PARTICULARS, "Closure Chrg LodgeID : " + objLodgementMasterTO.getLodgementId() + " Closure");
                                    txMap.put(TransferTrans.AUTHORIZE_STATUS_2, "ENTERED_AMOUNT");
                                    transAmt = CommonUtil.convertObjToDouble(objLodgementInstTO.getAmount()).doubleValue();
                                    transactionDAO.setTransType(CommonConstants.DEBIT);
                                    txMap.put("TRANS_MOD_TYPE", "GL");
                                    txMap.put("SCREEN_NAME", (String) obj.get(CommonConstants.SCREEN));// Added by nithya on 23-09-2016 for inserting screen name for transactions
                                    transferTo = transactionDAO.addTransferCreditLocal(txMap, transAmt);
                                    transferTo.setAuthorizeRemarks("FROM_BILLS_MODULE");
                                    transferTo.setInstType(objLodgementMasterTO.getBillsStatus());
                                    transferList.add(transferTo);
                                    transAmt = 0;
                                }
                                if ((CommonUtil.convertObjToDouble(objLodgementInstTO.getServiceTx()).doubleValue()) > 0) {
                                    txMap.put(TransferTrans.CR_AC_HD, (String) acHeads.get("SERVICE_TAX_HD"));
                                    txMap.put(TransferTrans.CR_PROD_TYPE, TransactionFactory.GL);
                                    txMap.put("TRANS_MOD_TYPE", "GL");
                                    txMap.put(TransferTrans.PARTICULARS, "ServiceTax LodgeID : " + objLodgementMasterTO.getLodgementId() + " Closure");
                                    txMap.put(TransferTrans.AUTHORIZE_STATUS_2, "ENTERED_AMOUNT");
                                    transAmt = CommonUtil.convertObjToDouble(objLodgementInstTO.getServiceTx()).doubleValue();
                                    transactionDAO.setTransType(CommonConstants.DEBIT);
                                    txMap.put("SCREEN_NAME", (String) obj.get(CommonConstants.SCREEN));// Added by nithya on 23-09-2016 for inserting screen name for transactions
                                    transferTo = transactionDAO.addTransferCreditLocal(txMap, transAmt);
                                    transferTo.setAuthorizeRemarks("FROM_BILLS_MODULE");
                                    transferTo.setInstType(objLodgementMasterTO.getBillsStatus());
                                    transferList.add(transferTo);
                                    transAmt = 0;
                                }
                                transactionDAO.setTransactionType(transactionDAO.TRANSFER);
                                transactionDAO.doTransferLocal(transferList, branchID);
                                //INSERT INSTRUCTION
                                HashMap instMap = new HashMap();
                                instMap.put("LODGEMENT_ID", objLodgementMasterTO.getLodgementId());
                                instMap.put("INSTRUCTION", objLodgementInstTO.getInstruction());
                                List lstInst = sqlMap.executeQueryForList("getBillsExtInst", instMap);
                                instMap = null;
                                if (lstInst != null && lstInst.size() > 0) {
                                    sqlMap.executeUpdate("updateLodgementInstructionsTO", objLodgementInstTO);
                                } else {
                                    sqlMap.executeUpdate("insertLodgementInstructionsTO", objLodgementInstTO);
                                }
                                lstInst = null;
                            }
                            status = null;
                        }
                    }
                    //OVERDUE INTEREST CODING
                    if ((CommonUtil.convertObjToDouble(objLodgementMasterTO.getRateForDelay()).doubleValue()) > 0) {
                        HashMap overDue = new HashMap();
                        overDue.put("LODGEMENT_ID", CommonUtil.convertObjToStr(objLodgementMasterTO.getLodgementId()));
                        overDue.put("CUR_DATE", currDt.clone());
                        List dueLst = sqlMap.executeQueryForList("getBillsOverDueIntDetails", overDue);
                        overDue = null;
                        if (dueLst != null && dueLst.size() > 0) {
                            overDue = (HashMap) dueLst.get(0);
                            double amount = CommonUtil.convertObjToDouble(objLodgementChequeTO.getInstrumentAmount()).doubleValue()
                                    - marginAmt;
                            double rateOfInterest = CommonUtil.convertObjToDouble(objLodgementMasterTO.getRateForDelay()).doubleValue();
                            double period = CommonUtil.convertObjToDouble(overDue.get("INT_PERIOD")).doubleValue();
                            double yearDays = 36500;
                            double intAmount = (amount * rateOfInterest * period) / yearDays;
                            System.out.println("!!!!!!!!amount" + amount + "#####rateOfInterest" + rateOfInterest + "!!!!!!!!!period" + period + "####intAmount" + intAmount);
                            intAmount = (double) getNearest((long) (intAmount * 100), 100) / 100;
                            System.out.println("####intAmount" + intAmount);
                            if (intAmount > 0) {
                                //transaction Coding
                                txMap = new HashMap();
                                txMap.put(TransferTrans.DR_AC_HD, null);
                                txMap.put(TransferTrans.DR_PROD_TYPE, objLodgementMasterTO.getProdType());
                                txMap.put(TransferTrans.DR_PROD_ID, objLodgementMasterTO.getBorrowProdId());
                                txMap.put(TransferTrans.DR_ACT_NUM, objLodgementMasterTO.getBorrowAcctNum());
                                txMap.put(TransferTrans.DR_BRANCH, branchID);
                                txMap.put(TransferTrans.CURRENCY, "INR");
                                //                                txMap.put(TransferTrans.DR_INST_TYPE, objLodgementMasterTO.getBillsStatus());
                                txMap.put(TransferTrans.PARTICULARS, "Int Coll(Delayed) LodgeID : " + objLodgementMasterTO.getLodgementId() + " Closure");
                                txMap.put(TransferTrans.AUTHORIZE_STATUS_2, "ENTERED_AMOUNT");
                                transAmt = intAmount;
                                txMap.put("TRANS_MOD_TYPE", objLodgementMasterTO.getProdType());
                                txMap.put("SCREEN_NAME", (String) obj.get(CommonConstants.SCREEN));// Added by nithya on 23-09-2016 for inserting screen name for transactions
                                transferTo = transactionDAO.addTransferDebitLocal(txMap, transAmt);
                                transferTo.setAuthorizeRemarks("FROM_BILLS_MODULE");
                                transferTo.setInstType(objLodgementMasterTO.getBillsStatus());
                                transferList.add(transferTo);
                                txMap.put(TransferTrans.CR_AC_HD, (String) overDue.get("INT_AC_HD"));
                                txMap.put(TransferTrans.CR_BRANCH, branchID);
                                txMap.put(TransferTrans.CURRENCY, "INR");
                                txMap.put(TransferTrans.CR_PROD_TYPE, TransactionFactory.GL);
                                txMap.put("TRANS_MOD_TYPE", "GL");
                                txMap.put(TransferTrans.PARTICULARS, "Int Coll(Delayed) LodgeID : " + objLodgementMasterTO.getLodgementId() + " Closure");
                                txMap.put(TransferTrans.AUTHORIZE_STATUS_2, "ENTERED_AMOUNT");
                                txMap.put("SCREEN_NAME", (String) obj.get(CommonConstants.SCREEN));// Added by nithya on 23-09-2016 for inserting screen name for transactions
                                transferTo = transactionDAO.addTransferCreditLocal(txMap, transAmt);
                                transferTo.setAuthorizeRemarks("FROM_BILLS_MODULE");
                                transferTo.setInstType(objLodgementMasterTO.getBillsStatus());
                                transferList.add(transferTo);
                                transactionDAO.setTransactionType(transactionDAO.TRANSFER);
                                transactionDAO.doTransferLocal(transferList, branchID);
                            }
                        }
                    }
                }
            }
        }
    }
    
}
