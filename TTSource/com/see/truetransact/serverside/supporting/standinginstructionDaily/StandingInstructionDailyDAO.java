/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition.
 * 
 *
 * StandingInstructionDAO.java
 *
 * Created on Tue Feb 03 14:30:43 IST 2004
 */
package com.see.truetransact.serverside.supporting.standinginstructionDaily;

import com.see.truetransact.serverside.supporting.standinginstruction.*;

import java.util.HashMap;
import java.util.List;
import java.util.ArrayList;
import java.util.LinkedHashMap;


import com.ibatis.db.sqlmap.SqlMap;
import com.see.truetransact.clientproxy.ProxyParameters;
import com.see.truetransact.serverutil.ServerUtil;

import com.see.truetransact.commonutil.CommonConstants;
import com.see.truetransact.commonutil.NoCommandException;
import com.see.truetransact.serverside.TTDAO;
import com.see.truetransact.serverutil.ServerConstants;
import com.see.truetransact.servicelocator.ServiceLocator;
import com.see.truetransact.serverexception.TransRollbackException;
import com.see.truetransact.serverexception.ServiceLocatorException;
import com.see.truetransact.transferobject.supporting.standinginstruction.*;
import com.see.truetransact.serverside.common.idgenerate.IDGenerateDAO;
import com.see.truetransact.serverside.common.log.LogDAO;
import com.see.truetransact.transferobject.common.log.LogTO;
import com.see.truetransact.commonutil.CommonUtil;
import com.see.truetransact.transferobject.transaction.transfer.TxTransferTO;
import java.util.Date;
import com.see.truetransact.commonutil.DateUtil;
import com.see.truetransact.commonutil.TTException;
import com.see.truetransact.commonutil.servicetax.ServiceTaxCalculation;
import com.see.truetransact.serverside.batchprocess.task.TaskHeader;
import com.see.truetransact.serverside.batchprocess.task.authorizechk.InterestCalculationTask;
import com.see.truetransact.serverside.common.transaction.TransferTrans;
import com.see.truetransact.serverside.remittance.RemittanceIssueDAO;
import com.see.truetransact.serverside.transaction.common.TransactionFactory;
import com.see.truetransact.serverside.transaction.transfer.TransferDAO;
import com.see.truetransact.transferobject.remittance.RemittanceIssueTO;
import com.see.truetransact.transferobject.common.transaction.TransactionTO;
import com.see.truetransact.transferobject.operativeaccount.AccountTO;
import com.see.truetransact.transferobject.servicetax.servicetaxdetails.ServiceTaxDetailsTO;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.Date;
/**
 * StandingInstruction DAO.
 *
 */
public class StandingInstructionDailyDAO extends TTDAO {

    private static SqlMap sqlMap = null;
    private StandingInstructionTO objStandingInstructionTO;
    private List objStandingInstructionCreditTO;
    private List objStandingInstructionDebitTO;
    private LogDAO logDAO;
    private LogTO logTO;
    private HashMap map = new HashMap();
    private String lbl = "";
    private String lblClose = "";
    private String command = "";
    private final String AC_HD_ID = "AC_HD_ID";
    private final String ACT_NUM = "ACT_NUM";
    private String branchID = "";
    private boolean isVariable = false;
    private double debitAmt = 0;
    private Date curDate = null;
    private double installment = 0;
    private boolean depValidate = true;
    private String prodType = "";
//    private final String BRANCH_CODE = "BRANCH_CODE";
//    private final String COMMAND = "COMMAND";
    private final String BASE_CURRENCY = "BASE_CURRENCY";
//    private final String EXEC_DT = "EXEC_DT";
//    private final String FREQUENCY = "FREQUENCY";
//    private final String GRACE_DAYS = "GRACE_DAYS";
//    private final String INSTALLMENT = "INSTALLMENT";
//    private final String MODULE = "Supporting";
//    private final String NEXT_INSTALL = "NEXT_INSTALL";
    private final String PROD_ID = "PROD_ID";
//    private final String SCREEN = "StandingInstruction";
//    private final String SI_ID = "SI_ID";
//    private final String SI_START_DT = "SI_START_DT";
//    private final String SI_END_DT = "SI_END_DT";
    private HashMap transReturnMap = new HashMap();
    //private HashMap returnMap = new HashMap();
    TransferTrans transfer = new TransferTrans();
    String singleTransId = "";
    HashMap data = new HashMap();
    private Date currDt = null;
    private Date lastIntCalcDtForSalRecovery = null; // Added by nithya on 03-03-2017 for 5763 [ RBI ]
    private boolean isTransaction = false;
    private String debitProdType = "";
    private String creditAct = "";
    private boolean isDepTrans = false;
    /**
     * Creates a new instance of StandingInstructionDAO
     */
    public StandingInstructionDailyDAO() throws ServiceLocatorException {
        ServiceLocator locate = ServiceLocator.getInstance();
        sqlMap = (SqlMap) locate.getDAOSqlMap();
    }

    private HashMap getData(HashMap map) throws Exception {
        System.out.println("MAP#######" + map);
        HashMap returnMap = new HashMap();
        HashMap where = (HashMap) map.get(CommonConstants.MAP_WHERE);
        where.put("SAL_REC_MODULE", CommonUtil.convertObjToStr(CommonConstants.SAL_REC_MODULE));

        if (map.containsKey(CommonConstants.MAP_NAME)) {
            String mapName = (String) map.get(CommonConstants.MAP_NAME);
            List list = (List) sqlMap.executeQueryForList(mapName, where);
            returnMap.put(mapName, list);
            list = null;
            return returnMap;
        }

        List list = (List) sqlMap.executeQueryForList("getSelectStandingInstructionTO", where);
        returnMap.put("StandingInstructionTO", (StandingInstructionTO) list.get(0));

        where.put("ASONDATE", currDt.clone());
        list = (List) sqlMap.executeQueryForList("getSelectStandingInstructionCreditTO", where);
        returnMap.put("StandingInstructionCreditTO", list);

        list = (List) sqlMap.executeQueryForList("getSelectStandingInstructionDebitTO", where);
        returnMap.put("StandingInstructionDebitTO", list);
        list = null;
        return returnMap;
    }

    private void insertData() throws Exception {
        try {
            sqlMap.startTransaction();
            String siID = getSIID();
            objStandingInstructionTO.setSiId(siID);
            logTO.setData(objStandingInstructionTO.toString());
            logTO.setPrimaryKey(objStandingInstructionTO.getKeyData());
            logTO.setStatus(objStandingInstructionTO.getCommand());
            objStandingInstructionTO.setStatus(CommonConstants.STATUS_CREATED);
            sqlMap.executeUpdate("insertStandingInstructionTO", objStandingInstructionTO);
            logDAO.addToLog(logTO);
            if (objStandingInstructionCreditTO != null && objStandingInstructionCreditTO.size() > 0) {
                int size = objStandingInstructionCreditTO.size();
                int i = 0;
                StandingInstructionCreditTO csi;
                while (i < size) {
                    csi = (StandingInstructionCreditTO) objStandingInstructionCreditTO.get(i);
                    csi.setSiId(siID);
                    csi.setStatus(CommonConstants.STATUS_CREATED);
                    logTO.setData(csi.toString());
                    logTO.setPrimaryKey(csi.getKeyData());
                    logTO.setStatus(csi.getCommand());
                    sqlMap.executeUpdate("insertStandingInstructionCreditTO", csi);
                    logDAO.addToLog(logTO);
                    i++;
                }
            }

            if (objStandingInstructionDebitTO != null && objStandingInstructionDebitTO.size() > 0) {
                int size = objStandingInstructionDebitTO.size();
                int i = 0;
                StandingInstructionDebitTO dsi;
                while (i < size) {
                    dsi = (StandingInstructionDebitTO) objStandingInstructionDebitTO.get(i);
                    dsi.setSiId(siID);
                    dsi.setStatus(CommonConstants.STATUS_CREATED);
                    logTO.setData(dsi.toString());
                    logTO.setPrimaryKey(dsi.getKeyData());
                    logTO.setStatus(dsi.getCommand());
                    sqlMap.executeUpdate("insertStandingInstructionDebitTO", (StandingInstructionDebitTO) objStandingInstructionDebitTO.get(i));
                    logDAO.addToLog(logTO);
                    i++;
                }

            }
            siID = null;
            sqlMap.commitTransaction();
        } catch (Exception e) {
            sqlMap.rollbackTransaction();
            e.printStackTrace();
            throw new TransRollbackException(e);
        }
    }

    private void updateData() throws Exception {
        try {
            System.out.println("####mapupdate" + map);
            sqlMap.startTransaction();
            logTO.setData(objStandingInstructionTO.toString());
            logTO.setPrimaryKey(objStandingInstructionTO.getKeyData());
            logTO.setStatus(objStandingInstructionTO.getCommand());
            objStandingInstructionTO.setStatus(CommonConstants.STATUS_MODIFIED);
//            HashMap map1= new HashMap();
            if (lbl.equals("Y")) {
                objStandingInstructionTO.setStatus("SUSPENDED");
            }
            if (lblClose.equals("Y")) {
                objStandingInstructionTO.setStatus("CLOSED");
            }
            sqlMap.executeUpdate("updateStandingInstructionTO", objStandingInstructionTO);
            logDAO.addToLog(logTO);
            if (objStandingInstructionCreditTO != null && objStandingInstructionCreditTO.size() > 0) {
                logTO.setData(((StandingInstructionCreditTO) objStandingInstructionCreditTO.get(0)).toString());
                logTO.setPrimaryKey(((StandingInstructionCreditTO) objStandingInstructionCreditTO.get(0)).getKeyData());
                logTO.setStatus(((StandingInstructionCreditTO) objStandingInstructionCreditTO.get(0)).getCommand());
//                changed by nikhil
//                ((StandingInstructionCreditTO)objStandingInstructionCreditTO.get(0)).setStatus(CommonConstants.STATUS_MODIFIED);
                StandingInstructionCreditTO objStandingCreditTO = (StandingInstructionCreditTO) objStandingInstructionCreditTO.get(0);
                objStandingCreditTO.setStatus(objStandingInstructionTO.getStatus());
                System.out.println("@#!#!@#!@#!@!#@objStandingCreditTO:" + objStandingCreditTO);
//                sqlMap.executeUpdate("updateStandingInstructionCreditTO", (StandingInstructionCreditTO)objStandingInstructionCreditTO.get(0));
                sqlMap.executeUpdate("updateStandingInstructionCreditTO", objStandingCreditTO);
                logDAO.addToLog(logTO);
                int size = objStandingInstructionCreditTO.size();
                System.out.println("size**********************" + size);
                int i = 1;
                while (i < size) {
                    logTO.setData(((StandingInstructionCreditTO) objStandingInstructionCreditTO.get(i)).toString());
                    logTO.setPrimaryKey(((StandingInstructionCreditTO) objStandingInstructionCreditTO.get(i)).getKeyData());
                    logTO.setStatus(((StandingInstructionCreditTO) objStandingInstructionCreditTO.get(i)).getCommand());
                    ((StandingInstructionCreditTO) objStandingInstructionCreditTO.get(i)).setStatus(CommonConstants.STATUS_CREATED);
                    sqlMap.executeUpdate("insertStandingInstructionCreditTO", (StandingInstructionCreditTO) objStandingInstructionCreditTO.get(i));
                    logDAO.addToLog(logTO);
                    i++;
                }
            } else {
                StandingInstructionCreditTO objCSI = new StandingInstructionCreditTO();
                objCSI.setSiId(objStandingInstructionTO.getSiId());
                objCSI.setStatus(CommonConstants.STATUS_DELETED);
                logTO.setData(objCSI.toString());
                logTO.setPrimaryKey(objCSI.getKeyData());
                logTO.setStatus(objCSI.getCommand());
                sqlMap.executeUpdate("deleteStandingInstructionCreditTO", objCSI);
                logDAO.addToLog(logTO);
            }

            if (objStandingInstructionDebitTO != null && objStandingInstructionDebitTO.size() > 0) {
                logTO.setData(((StandingInstructionDebitTO) objStandingInstructionDebitTO.get(0)).toString());
                logTO.setPrimaryKey(((StandingInstructionDebitTO) objStandingInstructionDebitTO.get(0)).getKeyData());
                logTO.setStatus(((StandingInstructionDebitTO) objStandingInstructionDebitTO.get(0)).getCommand());
//                changed by nikhil
//                ((StandingInstructionDebitTO)objStandingInstructionDebitTO.get(0)).setStatus(CommonConstants.STATUS_MODIFIED);
                StandingInstructionDebitTO objStandingDebitTO = (StandingInstructionDebitTO) objStandingInstructionDebitTO.get(0);
//                sqlMap.executeUpdate("updateDeletedStandingInstructionDebitTO", (StandingInstructionDebitTO)objStandingInstructionDebitTO.get(0));
                objStandingDebitTO.setStatus(objStandingInstructionTO.getStatus());
                System.out.println("@#!#!@#!@#!@!#@objStandingInstructionTO:" + objStandingDebitTO);
                sqlMap.executeUpdate("updateDeletedStandingInstructionDebitTO", objStandingDebitTO);
                logDAO.addToLog(logTO);

                int size = objStandingInstructionDebitTO.size();
                System.out.println("size**********************" + size);
                int i = 1;
                while (i < size) {
                    logTO.setData(((StandingInstructionDebitTO) objStandingInstructionDebitTO.get(i)).toString());
                    logTO.setPrimaryKey(((StandingInstructionDebitTO) objStandingInstructionDebitTO.get(i)).getKeyData());
                    logTO.setStatus(((StandingInstructionDebitTO) objStandingInstructionDebitTO.get(i)).getCommand());
                    ((StandingInstructionDebitTO) objStandingInstructionDebitTO.get(i)).setStatus(CommonConstants.STATUS_CREATED);
                    sqlMap.executeUpdate("insertStandingInstructionDebitTO", (StandingInstructionDebitTO) objStandingInstructionDebitTO.get(i));
                    logDAO.addToLog(logTO);
                    i++;
                }
            } else {
                StandingInstructionDebitTO objDSI = new StandingInstructionDebitTO();
                objDSI.setSiId(objStandingInstructionTO.getSiId());
                objDSI.setStatus(CommonConstants.STATUS_DELETED);
                logTO.setData(objDSI.toString());
                logTO.setPrimaryKey(objDSI.getKeyData());
                logTO.setStatus(objDSI.getCommand());
                sqlMap.executeUpdate("deleteStandingInstructionDebitTO", objDSI);
                logDAO.addToLog(logTO);
            }

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
            logTO.setData(objStandingInstructionTO.toString());
            logTO.setPrimaryKey(objStandingInstructionTO.getKeyData());
            logTO.setStatus(objStandingInstructionTO.getCommand());
            objStandingInstructionTO.setStatus(CommonConstants.STATUS_DELETED);
            sqlMap.executeUpdate("updateDeletedStandingInstructionTO", objStandingInstructionTO);
            logDAO.addToLog(logTO);

            if (objStandingInstructionCreditTO != null && objStandingInstructionCreditTO.size() > 0) {
                int size = objStandingInstructionCreditTO.size();
                StandingInstructionCreditTO objCSI = new StandingInstructionCreditTO();
                objCSI.setSiId(objStandingInstructionTO.getSiId());
                objCSI.setStatus(CommonConstants.STATUS_DELETED);
                HashMap standingMap = new HashMap();
                standingMap.put("SI_ID", objStandingInstructionTO.getSiId());
                List lst = sqlMap.executeQueryForList("getStandingInstrnDetailsAuthNotNull", standingMap);
                if (lst != null && lst.size() > 0) {
                    standingMap = (HashMap) lst.get(0);
                    String depositNo = CommonUtil.convertObjToStr(standingMap.get("ACCT_NO"));
                    if (depositNo.lastIndexOf("_") != -1) {
                        depositNo = depositNo.substring(0, depositNo.lastIndexOf("_"));
                    }
                    HashMap behavesMap = new HashMap();
                    behavesMap.put("ACT_NUM", depositNo);
                    List lstBehaves = sqlMap.executeQueryForList("getBehavesLikeForDepositNo", behavesMap);
                    if (lstBehaves != null && lstBehaves.size() > 0) {
                        behavesMap = (HashMap) lstBehaves.get(0);
                        behavesMap.put("DEPOSIT_NO", depositNo);
                        behavesMap.put("STANDING_INSTRUCT", "N");
                        if (behavesMap.get("BEHAVES_LIKE").equals("RECURRING")) {
                            sqlMap.executeUpdate("updateStandingInstnNo", behavesMap);
                        }
                    }
                }
                sqlMap.executeUpdate("deleteStandingInstructionCreditTO", objCSI);
            }

            if (objStandingInstructionDebitTO != null && objStandingInstructionDebitTO.size() > 0) {
                int size = objStandingInstructionDebitTO.size();
                StandingInstructionDebitTO objDSI = new StandingInstructionDebitTO();
                objDSI.setSiId(objStandingInstructionTO.getSiId());
                objDSI.setStatus(CommonConstants.STATUS_DELETED);
                sqlMap.executeUpdate("deleteStandingInstructionDebitTO", objDSI);
            }

            sqlMap.commitTransaction();
        } catch (Exception e) {
            sqlMap.rollbackTransaction();
            e.printStackTrace();
            throw new TransRollbackException(e);
        }
    }

    private HashMap executeSi(HashMap map) throws Exception {
        HashMap paramMap = new HashMap();
        singleTransId = this.generateLinkID();
        try {
            sqlMap.startTransaction();
            StandingInstructionTO siTO;
            debitProdType = "";
            //ArrayList batchList = new ArrayList();
            List siLst = (List) map.get("EXECUTE_LIST");
            if (siLst != null && siLst.size() > 0) {
                for (int i = 0; i < siLst.size(); i++) {
                    ArrayList batchList = new ArrayList();
                    HashMap siIdMap = (HashMap) siLst.get(i);
                    depValidate = true;
                    List list = sqlMap.executeQueryForList("standingBatchRunManual", siIdMap);
                    if (list != null && list.size() > 0) {                                                //IF STANDING INSTRUCTION IS FIXED
                        for (int j = 0; j < list.size(); j++) {
                            siTO = (StandingInstructionTO) list.get(j);
                            HashMap siId = new HashMap();
                            siId.put("SAL_REC_MODULE", CommonUtil.convertObjToStr(CommonConstants.SAL_REC_MODULE));
                            siId.put("SI_ID", siTO.getSiId());
                            siId.put("CURRDATE", curDate);
                            siId.put("ASONDATE", lastIntCalcDtForSalRecovery);// Added by nithya on 03-03-2017 for 5763 [ RBI ]
                            List creditList = sqlMap.executeQueryForList("getSelectStandingInstructionCreditTO", siId);
                            List debitList = sqlMap.executeQueryForList("getSelectStandingInstructionDebitTO", siId);
                            if (debitList != null && debitList.size() > 0) {
                                for (int x = 0, y = debitList.size(); x < y; x++) {
                                    HashMap where = new HashMap();
                                    where.put("PROD_TYPE", ((StandingInstructionDebitTO) debitList.get(x)).getProdType());
                                    where.put("ACT_NUM", ((StandingInstructionDebitTO) debitList.get(x)).getAcctNo());
                                    debitProdType = CommonUtil.convertObjToStr(where.get("PROD_TYPE")); //Added By Kannan AR
                                    if (!(where.get("PROD_TYPE").equals("RM"))) {
                                        StandingInstructionDebitTO standingInstructionDebitTO = (StandingInstructionDebitTO) debitList.get(x);
                                        if (!standingInstructionDebitTO.getProdType().equals("GL")) {
                                            HashMap branchidMap = (HashMap) sqlMap.executeQueryForObject("getBranchCode_ACT_MASTER"
                                                    + standingInstructionDebitTO.getProdType(), where);
                                            String branch = _branchCode;
                                            isVariable=false;
                                            batchList.add(getDebitTransferTO(standingInstructionDebitTO, (StandingInstructionTO) siTO, branch));
                                            branchidMap = null;
                                        }
                                    } else {
                                    }
                                }
                            }
                            TransferTrans trans = new TransferTrans();
                            TransferDAO transferDAO = new TransferDAO();
                            if (creditList != null && creditList.size() > 0) {
                                for (int x = 0, z = creditList.size(); x < z; x++) {
                                    HashMap where = new HashMap();
                                    where.put("ACT_NUM", ((StandingInstructionCreditTO) creditList.get(x)).getAcctNo());
                                    where.put("PROD_ID", ((StandingInstructionCreditTO) creditList.get(x)).getProdId());
                                    where.put("PROD_TYPE", ((StandingInstructionCreditTO) creditList.get(x)).getProdType());
                                    prodType = CommonUtil.convertObjToStr(where.get("PROD_TYPE"));
                                    if (!(where.get("PROD_TYPE").equals("RM"))) {
                                        String mapName = "getDetailsSIAcNumCredit" + ((StandingInstructionCreditTO) creditList.get(x)).getProdType();
                                        StandingInstructionCreditTO standingInstructionCreditTO = (StandingInstructionCreditTO) creditList.get(x);
                                        if (!standingInstructionCreditTO.getProdType().equals("GL")) {
                                            HashMap branchidMap = (HashMap) sqlMap.executeQueryForObject("getBranchCode_ACT_MASTER"
                                                    + standingInstructionCreditTO.getProdType(), where);
                                            String branch = _branchCode;
                                            isVariable=false;
                                            batchList.add(getCreditTransferTO(standingInstructionCreditTO, (StandingInstructionTO) siTO, branch));
                                            if (CommonUtil.convertObjToDouble(siTO.getSiCharges()).doubleValue() > 0) {
                                                batchList.add(getSiChargeCreditTransferTO(standingInstructionCreditTO, (StandingInstructionTO) siTO, branch));
                                            }
                                            if (CommonUtil.convertObjToDouble(siTO.getExecCharge()).doubleValue() > 0) {
                                                batchList.add(getSiChargeSTCreditTransferTO(standingInstructionCreditTO, (StandingInstructionTO) siTO, branch));
                                            }
                                            branchidMap = null;
                                        }
                                        if ((where.get("PROD_TYPE").equals("TD")) && (where.get("PROD_ID").equals("RD"))) {
                                            String actNum = CommonUtil.convertObjToStr(where.get("ACT_NUM"));
                                            actNum = actNum.substring(0, actNum.lastIndexOf("_"));
                                            where.put("ACT_NUM", actNum);
                                            List depVal = sqlMap.executeQueryForList("getDepValidateData", where);
                                            where = null;
                                            if ((depVal != null) && (depVal.size() > 0)) {
                                                where = (HashMap) depVal.get(0);
                                                String actStatus = CommonUtil.convertObjToStr(where.get("ACCT_STATUS"));
                                                Date matDate = (Date) where.get("MATURITY_DT");
                                                Date todayDate = (Date) curDate.clone();
                                                if ((actStatus.equalsIgnoreCase("CLOSED"))
                                                        || (DateUtil.dateDiff(matDate, todayDate) > 0)) {
                                                    depValidate = false;
                                                }
                                            }
                                        }
                                        if (where.get("PROD_TYPE").equals("TL") || where.get("PROD_TYPE").equals("AD")) {
                                            HashMap loanMap = new HashMap();
                                            loanMap = interestCalculationTLAD(CommonUtil.convertObjToStr(where.get("ACT_NUM")),
                                                    CommonUtil.convertObjToStr(where.get("PROD_ID")), CommonUtil.convertObjToStr(where.get("PROD_TYPE")), standingInstructionCreditTO.getAmount(), siTO);
                                            if (where.get("PROD_TYPE").equals(TransactionFactory.LOANS)) {
                                                HashMap whereMap = new HashMap();
                                                siId.put("ASONDATE", lastIntCalcDtForSalRecovery);
                                                siId.put("SAL_REC_MODULE", CommonUtil.convertObjToStr(CommonConstants.SAL_REC_MODULE));
                                                List debitLists = sqlMap.executeQueryForList("getSelectStandingInstructionDebitTO", siId);
                                                if (debitLists != null && debitLists.size() > 0) {
                                                    String loanActNumber = "";
                                                    String depositActNumber = "";
                                                    String sid = "";
                                                    depositActNumber = CommonUtil.convertObjToStr(((StandingInstructionDebitTO) debitLists.get(x)).getAcctNo());
                                                    loanActNumber = CommonUtil.convertObjToStr(where.get("ACT_NUM"));
                                                    sid = CommonUtil.convertObjToStr(((StandingInstructionDebitTO) debitLists.get(x)).getSiId());
                                                    // String debPrdType = CommonUtil.convertObjToStr(((StandingInstructionDebitTO) debitLists.get(x)).getProdType());
                                                    closeLoanAccount(loanActNumber, depositActNumber, sid, "");
                                                }
                                            }
                                            List waiveList = sqlMap.executeQueryForList("getwaiveoffDetails", where);
                                            if (waiveList != null && waiveList.size() > 0) {
                                                HashMap waiveMap = (HashMap) waiveList.get(0);
                                                loanMap.put("PENAL_WAIVE_OFF", waiveMap.get("PENAL_WAIVER"));
                                            }
                                            trans.setAllAmountMap(loanMap);
                                        }
                                    } else {
                                    }
                                }
                            }
                            if (depValidate) {
                                try {
                                    data = new HashMap(); // KDSA - 778
                                    if (!prodType.equalsIgnoreCase("RM")) {
                                        trans.setInitiatedBranch(_branchCode);
                                        trans.setLoanDebitInt("DP");
                                        if (siTO.getAutomaticPosting().equalsIgnoreCase("Y")) {
                                            HashMap authorizeMap = new HashMap();
                                            authorizeMap.put("BATCH_ID", null);
                                            authorizeMap.put("USER_ID", logTO.getUserId());
                                            authorizeMap.put(CommonConstants.AUTHORIZESTATUS, CommonConstants.STATUS_AUTHORIZED);
                                            data.put(CommonConstants.AUTHORIZEMAP, authorizeMap);
                                            data.put("ALL_AMOUNT", trans.getAllAmountMap());                                            
                                            //Added By Kannan AR ADV_TRANS_DETAILS table record should update properly KDSA:228
                                            if(debitProdType.equalsIgnoreCase("AD")){
                                                data.put("DEBIT_LOAN_TYPE", "DP");                                                
                                            }
                                            transReturnMap = doTransaction(batchList, _branchCode);
                                        } else {
                                            data.put("ALL_AMOUNT", trans.getAllAmountMap());
                                            transReturnMap = doTransaction(batchList, _branchCode);
                                            trans.setLoanDebitInt("");
                                        }
                                    } else {
                                        if (debitList != null && debitList.size() > 0) {
                                            for (int x = 0, y = debitList.size(); x < y; x++) {
                                                HashMap where = new HashMap();
                                                where.put("PROD_TYPE", ((StandingInstructionCreditTO) creditList.get(x)).getProdType());
                                                if ((where.get("PROD_TYPE").equals("RM"))) {
                                                    StandingInstructionDebitTO standingInstructionDebitTO = (StandingInstructionDebitTO) debitList.get(x);
                                                    final HashMap accountNameMap = new HashMap();
                                                    String custName = "";
                                                    accountNameMap.put("ACC_NUM", standingInstructionDebitTO.getAcctNo());
                                                    final List resultList = sqlMap.executeQueryForList("getAccountNumberName" + standingInstructionDebitTO.getProdType(), accountNameMap);
                                                    if (resultList != null) {
                                                        if (resultList.size() != 0) {
                                                            final HashMap resultMap = (HashMap) resultList.get(0);
                                                            if (resultMap != null) {
                                                                custName = resultMap.get("CUSTOMER_NAME").toString();
                                                            }
                                                        }
                                                    }
                                                    Date todayDt = (Date) curDate.clone();
                                                    HashMap dtatMap = new HashMap();
                                                    LinkedHashMap notDelMap = new LinkedHashMap();
                                                    LinkedHashMap notDelRemMap = new LinkedHashMap();
                                                    dtatMap.put("MODE", "INSERT");
                                                    TransactionTO transfer = new TransactionTO();
                                                    transfer.setTransType("TRANSFER");
                                                    transfer.setTransAmt(new Double(CommonUtil.convertObjToDouble(standingInstructionDebitTO.getAmount()).doubleValue()
                                                            + CommonUtil.convertObjToDouble(siTO.getRemitCharges()).doubleValue()
                                                            + CommonUtil.convertObjToDouble(siTO.getAcceptanceCharge()).doubleValue()
                                                            + CommonUtil.convertObjToDouble(siTO.getServiceTax()).doubleValue()));
                                                    transfer.setProductId(CommonUtil.convertObjToStr(standingInstructionDebitTO.getProdId()));
                                                    transfer.setProductType(CommonUtil.convertObjToStr(standingInstructionDebitTO.getProdType()));
                                                    transfer.setDebitAcctNo(CommonUtil.convertObjToStr(standingInstructionDebitTO.getAcctNo()));
                                                    transfer.setApplName(CommonUtil.convertObjToStr(custName));  // Added by Rajesh
                                                    //                transfer.setDebitAcctNo(CommonUtil.convertObjToStr(paramMap.get("ACT_NUM")));
                                                    transfer.setInstType("VOUCHER");
                                                    transfer.setChequeDt(todayDt);
                                                    LinkedHashMap transMap = new LinkedHashMap();
                                                    LinkedHashMap remMap = new LinkedHashMap();
                                                    transfer.setCommand("INSERT");
                                                    notDelMap.put(String.valueOf(1), transfer);//"NOT_DELETED_TRANS_TOs"
                                                    transMap.put("NOT_DELETED_TRANS_TOs", notDelMap);
                                                    dtatMap.put("TransactionTO", transMap);
                                                    dtatMap.put(CommonConstants.BRANCH_ID, _branchCode);
                                                    dtatMap.put("OPERATION_MODE", "ISSUE");
                                                    dtatMap.put("AUTHORIZEMAP", null);
                                                    RemittanceIssueTO remt = new RemittanceIssueTO();
                                                    remt.setDraweeBranchCode(_branchCode);
                                                    remt.setAmount(standingInstructionDebitTO.getAmount());
                                                    remt.setCategory("GENERAL_CATEGORY");
                                                    remt.setRemarks(siTO.getSiId());
                                                    // The following block added by Rajesh
                                                    HashMap behavesMap = new HashMap();
                                                    behavesMap.put("BEHAVES_LIKE", "PO");
                                                    List lstRemit = sqlMap.executeQueryForList("selectRemitProductId", behavesMap);
                                                    if (lstRemit != null && lstRemit.size() > 0) {
                                                        behavesMap = (HashMap) lstRemit.get(0);
                                                        remt.setProdId(CommonUtil.convertObjToStr(behavesMap.get("PROD_ID")));
                                                    }
                                                    HashMap draweeMap = new HashMap();
                                                    if (behavesMap.get("PAY_ISSUE_BRANCH").equals("ISSU_BRANCH")) {
                                                        lstRemit = sqlMap.executeQueryForList("getSelectBankTOList", null);
                                                        if (lstRemit != null && lstRemit.size() > 0) {
                                                            draweeMap = (HashMap) lstRemit.get(0);
                                                        }
                                                    }
                                                    //                remt.setProdId(CommonUtil.convertObjToStr(acctDtlMap.get("INT_PAY_PROD_ID")));  // Commented by Rajesh
                                                    remt.setFavouring(((StandingInstructionCreditTO) creditList.get(x)).getAcctNo());
                                                    //                remt.setDraweeBank(branch);   // Commented by Rajesh
                                                    remt.setDraweeBank(CommonUtil.convertObjToStr(draweeMap.get("BANK_CODE")));
                                                    remt.setBranchId(_branchCode);
                                                    remt.setCity("560");
                                                    remt.setInstrumentNo1("PO");
                                                    remt.setCommand("INSERT");
                                                    remt.setExchange(CommonUtil.convertObjToDouble(siTO.getRemitCharges()));
                                                    remt.setPostage(CommonUtil.convertObjToDouble(siTO.getAcceptanceCharge()));
                                                    remt.setOtherCharges(CommonUtil.convertObjToDouble(siTO.getServiceTax()));
                                                    remt.setTotalAmt(new Double(CommonUtil.convertObjToDouble(standingInstructionDebitTO.getAmount()).doubleValue()
                                                            + CommonUtil.convertObjToDouble(siTO.getRemitCharges()).doubleValue()
                                                            + CommonUtil.convertObjToDouble(siTO.getAcceptanceCharge()).doubleValue()
                                                            + CommonUtil.convertObjToDouble(siTO.getServiceTax()).doubleValue()));
                                                    remt.setStatusDt(todayDt);
                                                    remt.setStatusBy("TTSYSTEM");
                                                    notDelRemMap.put(String.valueOf(1), remt);
                                                    remMap.put("NOT_DELETED_ISSUE_TOs", notDelRemMap);
                                                    dtatMap.put("RemittanceIssueTO", remMap);
                                                    RemittanceIssueDAO remDao = new RemittanceIssueDAO();
                                                    HashMap resulMap = new HashMap();
                                                    remDao.setFromotherDAo(false);
                                                    resulMap = remDao.execute(dtatMap);
                                                    behavesMap = null;
                                                    draweeMap = null;
                                                    lstRemit = null;
                                                }
                                            }
                                        }
                                        //si charges
                                        batchList = new ArrayList();
                                        if (debitList != null && debitList.size() > 0) {
                                            for (int x = 0, y = debitList.size(); x < y; x++) {
                                                HashMap where = new HashMap();
                                                where.put("PROD_TYPE", ((StandingInstructionDebitTO) debitList.get(x)).getProdType());
                                                where.put("ACT_NUM", ((StandingInstructionDebitTO) debitList.get(x)).getAcctNo());
                                                if (!(where.get("PROD_TYPE").equals("RM"))) {
                                                    StandingInstructionDebitTO standingInstructionDebitTO = (StandingInstructionDebitTO) debitList.get(x);
                                                    if (!standingInstructionDebitTO.getProdType().equals("GL")) {
                                                        HashMap branchidMap = (HashMap) sqlMap.executeQueryForObject("getBranchCode_ACT_MASTER"
                                                                + standingInstructionDebitTO.getProdType(), where);
                                                        String branch = _branchCode;
                                                        batchList.add(getDebitSiChargesTransferTO(standingInstructionDebitTO, (StandingInstructionTO) siTO, branch));
                                                        branchidMap = null;
                                                    }
                                                } else {
                                                }
                                            }
                                        }
                                        if (creditList != null && creditList.size() > 0) {
                                            for (int x = 0, z = creditList.size(); x < z; x++) {
                                                HashMap where = new HashMap();
                                                where.put("ACT_NUM", ((StandingInstructionCreditTO) creditList.get(x)).getAcctNo());
                                                where.put("PROD_ID", ((StandingInstructionCreditTO) creditList.get(x)).getProdId());
                                                where.put("PROD_TYPE", ((StandingInstructionCreditTO) creditList.get(x)).getProdType());
                                                prodType = CommonUtil.convertObjToStr(where.get("PROD_TYPE"));
                                                StandingInstructionCreditTO standingInstructionCreditTO = (StandingInstructionCreditTO) creditList.get(x);
                                                String branch = _branchCode;
                                                if (CommonUtil.convertObjToDouble(siTO.getSiCharges()).doubleValue() > 0) {
                                                    batchList.add(getSiChargeCreditTransferTO(standingInstructionCreditTO, (StandingInstructionTO) siTO, branch));
                                                }
                                                if (CommonUtil.convertObjToDouble(siTO.getExecCharge()).doubleValue() > 0) {
                                                    batchList.add(getSiChargeSTCreditTransferTO(standingInstructionCreditTO, (StandingInstructionTO) siTO, branch));
                                                }
                                            }
                                        }
                                        trans = new TransferTrans();
                                        trans.setInitiatedBranch(_branchCode);
                                        trans.setLoanDebitInt("DP");
                                        if (siTO.getAutomaticPosting().equalsIgnoreCase("Y")) {
                                            HashMap authorizeMap = new HashMap();
                                            authorizeMap.put("BATCH_ID", null);
                                            authorizeMap.put("USER_ID", logTO.getUserId());
                                            authorizeMap.put(CommonConstants.AUTHORIZESTATUS, CommonConstants.STATUS_AUTHORIZED);
                                            data.put(CommonConstants.AUTHORIZEMAP, authorizeMap);
                                            data.put("ALL_AMOUNT", trans.getAllAmountMap());
                                            transReturnMap = doTransaction(batchList, _branchCode);
                                        } else {
                                            data.put("ALL_AMOUNT", trans.getAllAmountMap());
                                            transReturnMap = doTransaction(batchList, _branchCode);
                                        }
                                        trans.setLoanDebitInt("");
                                    }
                                    paramMap = new HashMap();
                                    paramMap.put("SI_ID", siTO.getSiId());
                                    paramMap.put("EXEC_DT", curDate);
                                    sqlMap.executeUpdate("updateStandingInstrManual", paramMap);
                                    sqlMap.executeUpdate("updateStandingInstrBatchManual", paramMap);
                                    updateStandingInstruction(siTO);
                                } catch (Exception transError) {
                                    sqlMap.rollbackTransaction();
                                    transError.printStackTrace();
                                }
                            }
                        }
                    }
                }
            }
            sqlMap.commitTransaction();
        } catch (Exception e) {
            sqlMap.rollbackTransaction();
            e.printStackTrace();
            throw new TransRollbackException(e);
        }
        System.out.println("transReturnMap############" + transReturnMap);
        return transReturnMap;
    }//trans.doDebitCredit(batchList,_branchCode)
    
    
    private HashMap executeFixedRecurringSi(HashMap map) throws Exception {
        HashMap paramMap = new HashMap();
        singleTransId = this.generateLinkID();
        try {
            sqlMap.startTransaction();
            StandingInstructionTO siTO;
            debitProdType = "";
            //ArrayList batchList = new ArrayList();
            List siLst = (List) map.get("EXECUTE_LIST");
            if (siLst != null && siLst.size() > 0) {
                for (int i = 0; i < siLst.size(); i++) {
                    ArrayList batchList = new ArrayList();
                    HashMap siIdMap = (HashMap) siLst.get(i);
                    depValidate = true;
                    List list = sqlMap.executeQueryForList("standingBatchRunManual", siIdMap);
                    if (list != null && list.size() > 0) {                                                //IF STANDING INSTRUCTION IS FIXED
                        for (int j = 0; j < list.size(); j++) {
                            siTO = (StandingInstructionTO) list.get(j);
                            HashMap siId = new HashMap();
                            creditAct = "";
                            isDepTrans = false;
                            siId.put("SAL_REC_MODULE", CommonUtil.convertObjToStr(CommonConstants.SAL_REC_MODULE));
                            siId.put("SI_ID", siTO.getSiId());
                            siId.put("CURRDATE", curDate);
                            siId.put("ASONDATE", lastIntCalcDtForSalRecovery);// Added by nithya on 03-03-2017 for 5763 [ RBI ]
                            List creditList = sqlMap.executeQueryForList("getSelectStandingInstructionCreditTO", siId);
                            List debitList = sqlMap.executeQueryForList("getSelectStandingInstructionDebitTO", siId);
                            if (debitList != null && debitList.size() > 0) {
                                for (int x = 0, y = debitList.size(); x < y; x++) {
                                    HashMap where = new HashMap();
                                    where.put("PROD_TYPE", ((StandingInstructionDebitTO) debitList.get(x)).getProdType());
                                    where.put("ACT_NUM", ((StandingInstructionDebitTO) debitList.get(x)).getAcctNo());
                                    debitProdType = CommonUtil.convertObjToStr(where.get("PROD_TYPE")); //Added By Kannan AR
                                    if (!(where.get("PROD_TYPE").equals("RM"))) {
                                        StandingInstructionDebitTO standingInstructionDebitTO = (StandingInstructionDebitTO) debitList.get(x);
                                        if (!standingInstructionDebitTO.getProdType().equals("GL")) {
                                            HashMap branchidMap = (HashMap) sqlMap.executeQueryForObject("getBranchCode_ACT_MASTER"
                                                    + standingInstructionDebitTO.getProdType(), where);
                                            String branch = _branchCode;
                                            isVariable=false;
                                            batchList.add(getDebitTransferTO(standingInstructionDebitTO, (StandingInstructionTO) siTO, branch));
                                            branchidMap = null;
                                        }
                                    } else {
                                    }
                                }
                            }
                            TransferTrans trans = new TransferTrans();
                            if (creditList != null && creditList.size() > 0) {
                                for (int x = 0, z = creditList.size(); x < z; x++) {
                                    HashMap where = new HashMap();
                                    where.put("ACT_NUM", ((StandingInstructionCreditTO) creditList.get(x)).getAcctNo());
                                    where.put("PROD_ID", ((StandingInstructionCreditTO) creditList.get(x)).getProdId());
                                    where.put("PROD_TYPE", ((StandingInstructionCreditTO) creditList.get(x)).getProdType());
                                    prodType = CommonUtil.convertObjToStr(where.get("PROD_TYPE"));
                                    creditAct = ((StandingInstructionCreditTO) creditList.get(x)).getAcctNo();
                                    if (!(where.get("PROD_TYPE").equals("RM"))) {
                                        String mapName = "getDetailsSIAcNumCredit" + ((StandingInstructionCreditTO) creditList.get(x)).getProdType();
                                        StandingInstructionCreditTO standingInstructionCreditTO = (StandingInstructionCreditTO) creditList.get(x);
                                        if (!standingInstructionCreditTO.getProdType().equals("GL")) {
                                            HashMap branchidMap = (HashMap) sqlMap.executeQueryForObject("getBranchCode_ACT_MASTER"
                                                    + standingInstructionCreditTO.getProdType(), where);
                                            String branch = _branchCode;
                                            isVariable=false;
                                            batchList.add(getCreditTransferTO(standingInstructionCreditTO, (StandingInstructionTO) siTO, branch));
                                            if (CommonUtil.convertObjToDouble(siTO.getSiCharges()).doubleValue() > 0) {
                                                batchList.add(getSiChargeCreditTransferTO(standingInstructionCreditTO, (StandingInstructionTO) siTO, branch));
                                            }
                                            if (CommonUtil.convertObjToDouble(siTO.getExecCharge()).doubleValue() > 0) {
                                                batchList.add(getSiChargeSTCreditTransferTO(standingInstructionCreditTO, (StandingInstructionTO) siTO, branch));
                                            }
                                            branchidMap = null;
                                        }
                                        if ((where.get("PROD_TYPE").equals("TD")) && (where.get("PROD_ID").equals("RD"))) {
                                            String actNum = CommonUtil.convertObjToStr(where.get("ACT_NUM"));
                                            actNum = actNum.substring(0, actNum.lastIndexOf("_"));
                                            where.put("ACT_NUM", actNum);
                                            List depVal = sqlMap.executeQueryForList("getDepValidateData", where);
                                            where = null;
                                            if ((depVal != null) && (depVal.size() > 0)) {
                                                where = (HashMap) depVal.get(0);
                                                String actStatus = CommonUtil.convertObjToStr(where.get("ACCT_STATUS"));
                                                Date matDate = (Date) where.get("MATURITY_DT");
                                                Date todayDate = (Date) curDate.clone();
                                                if ((actStatus.equalsIgnoreCase("CLOSED"))
                                                        || (DateUtil.dateDiff(matDate, todayDate) > 0)) {
                                                    depValidate = false;
                                                }
                                            }
                                        }
                                    } else {
                                    }
                                }
                            }
                            HashMap depMap = new HashMap();
                            depMap.put("CURRDATE", curDate.clone());
                            depMap.put("DEPOSIT_NO", CommonUtil.convertObjToStr(creditAct));
                            List curInstLst = sqlMap.executeQueryForList("getCheckCurrentInstallmentRD", depMap);
                            if (curInstLst != null && curInstLst.size() > 0) {
                                isDepTrans = true;
                            }
                            transReturnMap = new HashMap();
                            data = new HashMap(); // KDSA - 778
                            if (depValidate && isDepTrans) {
                                try {
                                    if (!prodType.equalsIgnoreCase("RM")) {
                                        trans.setInitiatedBranch(_branchCode);
                                        trans.setLoanDebitInt("DP");
                                        if (siTO.getAutomaticPosting().equalsIgnoreCase("Y")) {
                                            HashMap authorizeMap = new HashMap();
                                            authorizeMap.put("BATCH_ID", null);
                                            authorizeMap.put("USER_ID", logTO.getUserId());
                                            authorizeMap.put(CommonConstants.AUTHORIZESTATUS, CommonConstants.STATUS_AUTHORIZED);
                                            data.put(CommonConstants.AUTHORIZEMAP, authorizeMap);
                                            //data.put("ALL_AMOUNT", trans.getAllAmountMap());                                            
                                            //Added By Kannan AR ADV_TRANS_DETAILS table record should update properly KDSA:228
                                            if(debitProdType.equalsIgnoreCase("AD")){
                                                data.put("DEBIT_LOAN_TYPE", "DP");                                                
                                            }
                                            transReturnMap = doTransaction(batchList, _branchCode);
                                        } else {
                                            //data.put("ALL_AMOUNT", trans.getAllAmountMap());
                                            transReturnMap = doTransaction(batchList, _branchCode);
                                            trans.setLoanDebitInt("");
                                        }
                                    } else {
                                        if (debitList != null && debitList.size() > 0) {
                                            for (int x = 0, y = debitList.size(); x < y; x++) {
                                                HashMap where = new HashMap();
                                                where.put("PROD_TYPE", ((StandingInstructionCreditTO) creditList.get(x)).getProdType());
                                                if ((where.get("PROD_TYPE").equals("RM"))) {
                                                    StandingInstructionDebitTO standingInstructionDebitTO = (StandingInstructionDebitTO) debitList.get(x);
                                                    final HashMap accountNameMap = new HashMap();
                                                    String custName = "";
                                                    accountNameMap.put("ACC_NUM", standingInstructionDebitTO.getAcctNo());
                                                    final List resultList = sqlMap.executeQueryForList("getAccountNumberName" + standingInstructionDebitTO.getProdType(), accountNameMap);
                                                    if (resultList != null) {
                                                        if (resultList.size() != 0) {
                                                            final HashMap resultMap = (HashMap) resultList.get(0);
                                                            if (resultMap != null) {
                                                                custName = resultMap.get("CUSTOMER_NAME").toString();
                                                            }
                                                        }
                                                    }
                                                    Date todayDt = (Date) curDate.clone();
                                                    HashMap dtatMap = new HashMap();
                                                    LinkedHashMap notDelMap = new LinkedHashMap();
                                                    LinkedHashMap notDelRemMap = new LinkedHashMap();
                                                    dtatMap.put("MODE", "INSERT");
                                                    TransactionTO transfer = new TransactionTO();
                                                    transfer.setTransType("TRANSFER");
                                                    transfer.setTransAmt(new Double(CommonUtil.convertObjToDouble(standingInstructionDebitTO.getAmount()).doubleValue()
                                                            + CommonUtil.convertObjToDouble(siTO.getRemitCharges()).doubleValue()
                                                            + CommonUtil.convertObjToDouble(siTO.getAcceptanceCharge()).doubleValue()
                                                            + CommonUtil.convertObjToDouble(siTO.getServiceTax()).doubleValue()));
                                                    transfer.setProductId(CommonUtil.convertObjToStr(standingInstructionDebitTO.getProdId()));
                                                    transfer.setProductType(CommonUtil.convertObjToStr(standingInstructionDebitTO.getProdType()));
                                                    transfer.setDebitAcctNo(CommonUtil.convertObjToStr(standingInstructionDebitTO.getAcctNo()));
                                                    transfer.setApplName(CommonUtil.convertObjToStr(custName));  // Added by Rajesh
                                                    //                transfer.setDebitAcctNo(CommonUtil.convertObjToStr(paramMap.get("ACT_NUM")));
                                                    transfer.setInstType("VOUCHER");
                                                    transfer.setChequeDt(todayDt);
                                                    LinkedHashMap transMap = new LinkedHashMap();
                                                    LinkedHashMap remMap = new LinkedHashMap();
                                                    transfer.setCommand("INSERT");
                                                    notDelMap.put(String.valueOf(1), transfer);//"NOT_DELETED_TRANS_TOs"
                                                    transMap.put("NOT_DELETED_TRANS_TOs", notDelMap);
                                                    dtatMap.put("TransactionTO", transMap);
                                                    dtatMap.put(CommonConstants.BRANCH_ID, _branchCode);
                                                    dtatMap.put("OPERATION_MODE", "ISSUE");
                                                    dtatMap.put("AUTHORIZEMAP", null);
                                                    RemittanceIssueTO remt = new RemittanceIssueTO();
                                                    remt.setDraweeBranchCode(_branchCode);
                                                    remt.setAmount(standingInstructionDebitTO.getAmount());
                                                    remt.setCategory("GENERAL_CATEGORY");
                                                    remt.setRemarks(siTO.getSiId());
                                                    // The following block added by Rajesh
                                                    HashMap behavesMap = new HashMap();
                                                    behavesMap.put("BEHAVES_LIKE", "PO");
                                                    List lstRemit = sqlMap.executeQueryForList("selectRemitProductId", behavesMap);
                                                    if (lstRemit != null && lstRemit.size() > 0) {
                                                        behavesMap = (HashMap) lstRemit.get(0);
                                                        remt.setProdId(CommonUtil.convertObjToStr(behavesMap.get("PROD_ID")));
                                                    }
                                                    HashMap draweeMap = new HashMap();
                                                    if (behavesMap.get("PAY_ISSUE_BRANCH").equals("ISSU_BRANCH")) {
                                                        lstRemit = sqlMap.executeQueryForList("getSelectBankTOList", null);
                                                        if (lstRemit != null && lstRemit.size() > 0) {
                                                            draweeMap = (HashMap) lstRemit.get(0);
                                                        }
                                                    }
                                                    //                remt.setProdId(CommonUtil.convertObjToStr(acctDtlMap.get("INT_PAY_PROD_ID")));  // Commented by Rajesh
                                                    remt.setFavouring(((StandingInstructionCreditTO) creditList.get(x)).getAcctNo());
                                                    //                remt.setDraweeBank(branch);   // Commented by Rajesh
                                                    remt.setDraweeBank(CommonUtil.convertObjToStr(draweeMap.get("BANK_CODE")));
                                                    remt.setBranchId(_branchCode);
                                                    remt.setCity("560");
                                                    remt.setInstrumentNo1("PO");
                                                    remt.setCommand("INSERT");
                                                    remt.setExchange(CommonUtil.convertObjToDouble(siTO.getRemitCharges()));
                                                    remt.setPostage(CommonUtil.convertObjToDouble(siTO.getAcceptanceCharge()));
                                                    remt.setOtherCharges(CommonUtil.convertObjToDouble(siTO.getServiceTax()));
                                                    remt.setTotalAmt(new Double(CommonUtil.convertObjToDouble(standingInstructionDebitTO.getAmount()).doubleValue()
                                                            + CommonUtil.convertObjToDouble(siTO.getRemitCharges()).doubleValue()
                                                            + CommonUtil.convertObjToDouble(siTO.getAcceptanceCharge()).doubleValue()
                                                            + CommonUtil.convertObjToDouble(siTO.getServiceTax()).doubleValue()));
                                                    remt.setStatusDt(todayDt);
                                                    remt.setStatusBy("TTSYSTEM");
                                                    notDelRemMap.put(String.valueOf(1), remt);
                                                    remMap.put("NOT_DELETED_ISSUE_TOs", notDelRemMap);
                                                    dtatMap.put("RemittanceIssueTO", remMap);
                                                    RemittanceIssueDAO remDao = new RemittanceIssueDAO();
                                                    HashMap resulMap = new HashMap();
                                                    remDao.setFromotherDAo(false);
                                                    resulMap = remDao.execute(dtatMap);
                                                    behavesMap = null;
                                                    draweeMap = null;
                                                    lstRemit = null;
                                                }
                                            }
                                        }
                                        //si charges
                                        batchList = new ArrayList();
                                        if (debitList != null && debitList.size() > 0) {
                                            for (int x = 0, y = debitList.size(); x < y; x++) {
                                                HashMap where = new HashMap();
                                                where.put("PROD_TYPE", ((StandingInstructionDebitTO) debitList.get(x)).getProdType());
                                                where.put("ACT_NUM", ((StandingInstructionDebitTO) debitList.get(x)).getAcctNo());
                                                if (!(where.get("PROD_TYPE").equals("RM"))) {
                                                    StandingInstructionDebitTO standingInstructionDebitTO = (StandingInstructionDebitTO) debitList.get(x);
                                                    if (!standingInstructionDebitTO.getProdType().equals("GL")) {
                                                        HashMap branchidMap = (HashMap) sqlMap.executeQueryForObject("getBranchCode_ACT_MASTER"
                                                                + standingInstructionDebitTO.getProdType(), where);
                                                        String branch = _branchCode;
                                                        batchList.add(getDebitSiChargesTransferTO(standingInstructionDebitTO, (StandingInstructionTO) siTO, branch));
                                                        branchidMap = null;
                                                    }
                                                } else {
                                                }
                                            }
                                        }
                                        if (creditList != null && creditList.size() > 0) {
                                            for (int x = 0, z = creditList.size(); x < z; x++) {
                                                HashMap where = new HashMap();
                                                where.put("ACT_NUM", ((StandingInstructionCreditTO) creditList.get(x)).getAcctNo());
                                                where.put("PROD_ID", ((StandingInstructionCreditTO) creditList.get(x)).getProdId());
                                                where.put("PROD_TYPE", ((StandingInstructionCreditTO) creditList.get(x)).getProdType());
                                                prodType = CommonUtil.convertObjToStr(where.get("PROD_TYPE"));
                                                StandingInstructionCreditTO standingInstructionCreditTO = (StandingInstructionCreditTO) creditList.get(x);
                                                String branch = _branchCode;
                                                if (CommonUtil.convertObjToDouble(siTO.getSiCharges()).doubleValue() > 0) {
                                                    batchList.add(getSiChargeCreditTransferTO(standingInstructionCreditTO, (StandingInstructionTO) siTO, branch));
                                                }
                                                if (CommonUtil.convertObjToDouble(siTO.getExecCharge()).doubleValue() > 0) {
                                                    batchList.add(getSiChargeSTCreditTransferTO(standingInstructionCreditTO, (StandingInstructionTO) siTO, branch));
                                                }
                                            }
                                        }
                                        trans = new TransferTrans();
                                        trans.setInitiatedBranch(_branchCode);
                                        trans.setLoanDebitInt("DP");
                                        if (siTO.getAutomaticPosting().equalsIgnoreCase("Y")) {
                                            HashMap authorizeMap = new HashMap();
                                            authorizeMap.put("BATCH_ID", null);
                                            authorizeMap.put("USER_ID", logTO.getUserId());
                                            authorizeMap.put(CommonConstants.AUTHORIZESTATUS, CommonConstants.STATUS_AUTHORIZED);
                                            data.put(CommonConstants.AUTHORIZEMAP, authorizeMap);
                                            data.put("ALL_AMOUNT", trans.getAllAmountMap());
                                            transReturnMap = doTransaction(batchList, _branchCode);
                                        } else {
                                            data.put("ALL_AMOUNT", trans.getAllAmountMap());
                                            transReturnMap = doTransaction(batchList, _branchCode);
                                        }
                                        trans.setLoanDebitInt("");
                                    }
                                    paramMap = new HashMap();
                                    paramMap.put("SI_ID", siTO.getSiId());
                                    paramMap.put("EXEC_DT", curDate);
                                    sqlMap.executeUpdate("updateStandingInstrManual", paramMap);
                                    sqlMap.executeUpdate("updateStandingInstrBatchManual", paramMap);
                                    updateStandingInstruction(siTO);
                                } catch (Exception transError) {
                                    sqlMap.rollbackTransaction();
                                    transError.printStackTrace();
                                }
                            }
                        }
                    }
                }
            }
            sqlMap.commitTransaction();
        } catch (Exception e) {
            sqlMap.rollbackTransaction();
            e.printStackTrace();
            throw new TransRollbackException(e);
        }
        System.out.println("transReturnMap############" + transReturnMap);
        return transReturnMap;
    }//trans.doDebitCredit(batchList,_branchCode)
    
    
    private Date setProperDtFormat(Date dt) { //Added By Suresh R  14-Jul-2017
        Date tempDt = (Date) currDt.clone();
        if (dt != null) {
            tempDt.setDate(dt.getDate());
            tempDt.setMonth(dt.getMonth());
            tempDt.setYear(dt.getYear());
            return tempDt;
        }
        return null;
    }
    public long getNearest(long number, long roundingFactor) {
        long roundingFactorOdd = roundingFactor;
        if ((roundingFactor % 2) != 0) {
            roundingFactorOdd += 1;
        }
        long mod = number % roundingFactor;
        if ((mod <= (roundingFactor / 2)) || (mod <= (roundingFactorOdd / 2))) {
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
    
    //Added By Suresh R     13-Jul-2017     Ref By Mr.Srinath
    private HashMap executeVaribleRecurringSi(HashMap map) throws Exception { //Added By Suresh R     13-Jul-2017     Ref By Mr.Srinath
        HashMap paramMap = new HashMap();
        singleTransId = this.generateLinkID(); // commented by nithya on 25-09-2019 for KD 622 - STANDING INSTRUCTION ISSUE
        try {
            sqlMap.startTransaction();
            isVariable = true;
            depValidate = true;
            double ROI = 0.0;
            double PENAL = 0.0;
            double totalGst = 0.0; // Added by nithya on 20-12-2018 for KD 364 - Gst amount calculation issue at Rd standing
            HashMap serviceTax_Map = new HashMap();
            int delayMonth = 0;
            int pendingInst = 0;
            double depAmt = 0.0;
            double chargeAmt = 0;
            Date depDate = null;
            int gracePeriod = 0;
            double calcAmt = 0.0;
            int pendingInstl = 0;
            String depositNo = "";
            StandingInstructionTO siTO;
            double tot_Inst_paid = 0.0;
            double debitAvailabeBal = 0.0;
            HashMap siId = new HashMap();
            HashMap where = new HashMap();
            HashMap calcMap = new HashMap();
            HashMap delayMap = new HashMap();
            HashMap returnMap = new HashMap();
            HashMap balanceMap = new HashMap();
            HashMap accountMap = new HashMap();
            HashMap penaltyMap = new HashMap();
            HashMap recurringMap = new HashMap();
            HashMap depositPenalMap = new HashMap();
            HashMap multiDepositPenalMap = new HashMap();
            ArrayList batchList = new ArrayList();
            TransferTrans trans = new TransferTrans();
            TransferDAO transferDAO = new TransferDAO();
            List siLst = (List) map.get("EXECUTE_VARIABLE_LIST");
            boolean isTransaction = false; //Added By Kannan AR
            if (siLst != null && siLst.size() > 0) {
                for (int i = 0; i < siLst.size(); i++) {
                    isTransaction = false; //Added By Kannan AR
                    batchList = new ArrayList();;
                    HashMap siIdMap = (HashMap) siLst.get(i);
                    debitAvailabeBal = 0.0;
                    tot_Inst_paid = 0.0;
                    depValidate = true;
                    isVariable = true;
                    debitAmt = 0.0;
                    calcAmt = 0.0;
                    siIdMap.put("CURRDATE", curDate);
                    List list = sqlMap.executeQueryForList("standingBatchRunVariableManual", siIdMap);
                    if (list != null && list.size() > 0) {
                        balanceMap = new HashMap();
                        trans = new TransferTrans();
                        transferDAO = new TransferDAO();
                        depositPenalMap = new HashMap();
                        multiDepositPenalMap = new HashMap();
                        siTO = (StandingInstructionTO) list.get(0);
                        siId = new HashMap();
                        siId.put("SI_ID", siTO.getSiId());
                        siId.put("CURRDATE", curDate);
                        List creditList = sqlMap.executeQueryForList("getSelectStandingInstructionVariableCreditTO", siId);
                        List debitList = sqlMap.executeQueryForList("getSelectStandingInstructionVariableDebitTO", siId);
                        if ((creditList != null && creditList.size() > 0) && (debitList != null && debitList.size() > 0)) {
                            //CREDIT LIST
                            isVariable = true;
                            where = new HashMap();
                            where.put("ACT_NUM", ((StandingInstructionCreditTO) creditList.get(0)).getAcctNo());
                            where.put("PROD_ID", ((StandingInstructionCreditTO) creditList.get(0)).getProdId());
                            where.put("PROD_TYPE", ((StandingInstructionCreditTO) creditList.get(0)).getProdType());
                            prodType = CommonUtil.convertObjToStr(where.get("PROD_TYPE"));
                            // Start of changes done by nithya [ to set SI from Daily deposit to RD ]
                            // Commenting the below two lins and rewriting the code to set standing from daily deposit it RD
                            // Currently RD standing can set from SB only
                            //balanceMap.put("ACT_NUM", ((StandingInstructionDebitTO) debitList.get(0)).getAcctNo());
                            //List balanceDrList = sqlMap.executeQueryForList("getLatestSBODLimitForAccNum", balanceMap);
                            String debitAccountNo = CommonUtil.convertObjToStr(((StandingInstructionDebitTO) debitList.get(0)).getAcctNo());
                            if(debitAccountNo.indexOf("_1") != -1){
                                debitAccountNo = debitAccountNo.replaceAll("_1", "");
                            }
                            balanceMap.put("ACT_NUM", debitAccountNo);
                            List balanceDrList = sqlMap.executeQueryForList("getAccountBalanceForStandingInstruction", balanceMap);
                            // End of changes done by nithya [ to set SI from Daily deposit to RD ]
                            if(balanceDrList != null && balanceDrList.size() > 0) {
                                balanceMap = (HashMap) balanceDrList.get(0);
                                //debitAvailabeBal = CommonUtil.convertObjToDouble(balanceMap.get("AVAILABLE_BALANCE"));
                                debitAvailabeBal = CommonUtil.convertObjToDouble(balanceMap.get("ACCOUNT_BALANCE"));
                                //System.out.println("########### debitAvailabeBal : "+debitAvailabeBal);
                                if (debitAvailabeBal > 0) {
                                    StandingInstructionCreditTO standingInstructionCreditTO = (StandingInstructionCreditTO) creditList.get(0);
                                    String branch = _branchCode;
                                    depositNo = CommonUtil.convertObjToStr(standingInstructionCreditTO.getAcctNo());
//                                    System.out.println("###### DepositNo : " + depositNo);
                                    if (depositNo.lastIndexOf("_") != -1) {
                                        depositNo = depositNo.substring(0, depositNo.lastIndexOf("_"));
                                    }
//                                    System.out.println("###### Final DepositNo : " + depositNo);
                                    accountMap = new HashMap();
                                    accountMap.put("DEPOSIT_NO", depositNo);
                                    accountMap.put("BRANCH_ID", _branchCode);
                                    List depositAccLst = sqlMap.executeQueryForList("getProductIdForDeposits", accountMap);
                                    if (depositAccLst != null && depositAccLst.size() > 0) {
                                        accountMap = (HashMap) depositAccLst.get(0);
//                                        System.out.println("########## accountMap : " + accountMap);
                                        if (CommonUtil.convertObjToDouble(siTO.getSiAgentComm()).doubleValue() > 0) {
                                            siTO.setSiAgentComm(CommonUtil.convertObjToDouble(0));
                                        }
                                        depAmt = CommonUtil.convertObjToDouble(accountMap.get("DEPOSIT_AMT")).doubleValue();
                                        recurringMap = new HashMap();
                                        recurringMap.put("PROD_ID", ((StandingInstructionCreditTO) creditList.get(0)).getProdId());
                                        List recurringLst = sqlMap.executeQueryForList("getRecurringDepositDetails", recurringMap);
                                        if (recurringLst != null && recurringLst.size() > 0) {
                                            recurringMap = (HashMap) recurringLst.get(0);
                                        }
                                        HashMap lastMap = new HashMap();
                                        lastMap.put("DEPOSIT_NO", depositNo);
                                        List lst = sqlMap.executeQueryForList("getInterestDeptIntTable", lastMap);
                                        if (lst != null && lst.size() > 0) {
                                            lastMap = (HashMap) lst.get(0);
                                            tot_Inst_paid = CommonUtil.convertObjToDouble(lastMap.get("TOTAL_INSTALL_PAID")).doubleValue();
                                            depDate = DateUtil.getDateMMDDYYYY(CommonUtil.convertObjToStr(lastMap.get("DEPOSIT_DT")));
                                            gracePeriod = CommonUtil.convertObjToInt(recurringMap.get("GRACE_PERIOD"));
                                            //System.out.println("########## gracePeriod  : " + gracePeriod);
                                            depDate = DateUtil.addDays(depDate, gracePeriod);
                                            //System.out.println("########## After Grace Period DepDate  : " + depDate);
                                            chargeAmt = depAmt / 100;
                                            //System.out.println("####### chargeAmt : " + chargeAmt);
                                            penaltyMap = new HashMap();
                                            HashMap penalInstMap = new HashMap();
                                            penaltyMap.put("DEPOSIT_DT", setProperDtFormat(depDate));
                                            penaltyMap.put("CURR_DT", setProperDtFormat((Date) currDt.clone()));
                                            penaltyMap.put("FREQ", 1);
                                            penaltyMap.put("PAID_INST", tot_Inst_paid); // Penal Calculation for RD
                                            List penaltyList = sqlMap.executeQueryForList("getPenaltyForDeposit", penaltyMap);     //Based On Query Penaty Calculation
                                            if(penaltyList != null && penaltyList.size() > 0){
                                                penalInstMap = (HashMap)penaltyList.get(0);
                                                int penalInstallments = CommonUtil.convertObjToInt(penalInstMap.get("NO_OF_PENDING_INST"));
                                                penalInstMap = new HashMap();
                                                for(int p=1; p<=penalInstallments; p++){// Added by nithya on 07-05-2020 for KD-1724
                                                    penalInstMap.put(p,p);
                                                }
                                            }
                                            //System.out.println("penal Inst Map ::" + penalInstMap);
                                            depDate = DateUtil.getDateMMDDYYYY(CommonUtil.convertObjToStr(lastMap.get("DEPOSIT_DT")));
                                            penaltyMap.put("DEPOSIT_DT", setProperDtFormat(depDate));
                                            penaltyList = sqlMap.executeQueryForList("getPenaltyForDeposit", penaltyMap);
                                            if (penaltyList != null && penaltyList.size() > 0) {
                                                penaltyMap = (HashMap) penaltyList.get(0);
                                                if (CommonUtil.convertObjToInt(penaltyMap.get("NO_OF_PENDING_INST")) > 0) {
                                                    pendingInst = CommonUtil.convertObjToInt(penaltyMap.get("NO_OF_PENDING_INST"));
                                                    pendingInstl = pendingInst;
                                                    //System.out.println("############ NO_OF_PENDING_INST : " + pendingInst);
                                                    calcMap = new HashMap();
                                                    for (int j = 1; j <= pendingInst; j++) {
                                                        calcMap.put(j, pendingInstl);
                                                        pendingInstl = pendingInstl - 1;
                                                    }
                                                    delayMap = new HashMap();
                                                    delayMap.put("PROD_ID", accountMap.get("PROD_ID"));
                                                    delayMap.put("DEPOSIT_AMT", accountMap.get("DEPOSIT_AMT"));
                                                    lst = sqlMap.executeQueryForList("getSelectDelayedRate", delayMap);
                                                    if (lst != null && lst.size() > 0) {
                                                        delayMap = (HashMap) lst.get(0);
                                                        //System.out.println("####### delayMap : " + delayMap);
                                                        ROI = CommonUtil.convertObjToDouble(delayMap.get("ROI")).doubleValue();
                                                        calcAmt = ROI * chargeAmt;
                                                       //System.out.println("######## calcAmt " + calcAmt);
                                                    }
                                                   //System.out.println("######## CALC_Map " + calcMap);
                                                    if (calcMap != null && calcMap.size() > 0) {
                                                        delayMonth = 0;
                                                        for (int z = 1; z <= calcMap.size(); z++) {
                                                            PENAL = 0.0;
                                                            totalGst = 0.0; // Added by nithya on 20-12-2018 for KD 364 - Gst amount calculation issue at Rd standing
                                                            debitAmt = 0.0;
                                                            data = new HashMap();
                                                            debitProdType = "";
                                                            trans = new TransferTrans();
                                                            batchList = new ArrayList();
                                                            transferDAO = new TransferDAO();
                                                            depositPenalMap = new HashMap();
                                                            multiDepositPenalMap = new HashMap();
                                                            delayMonth = CommonUtil.convertObjToInt(calcMap.get(z));
                                                            //System.out.println("############ DELAY_MONTH : " + delayMonth);
                                                            if (delayMonth > 0) {
                                                                if(penalInstMap.containsKey(z)){ // Added by nithya on 07-05-2020 for KD-1724
                                                                PENAL = calcAmt * CommonUtil.convertObjToDouble(delayMonth);
                                                                PENAL = (double) getNearest((long) (PENAL * 100), 100) / 100;
                                                                }else{
                                                                    PENAL = 0.0;
                                                                }
                                                               //System.out.println("####### PENAL : " + PENAL);
                                                                debitAmt = depAmt;
                                                                if (PENAL > 0) {
                                                                    if(map.containsKey("SERVIC_TAX_REQ") && map.get("SERVIC_TAX_REQ") != null && CommonUtil.convertObjToStr(map.get("SERVIC_TAX_REQ")).equalsIgnoreCase("Y")){ // Added by nithya on 20-12-2018 for KD 364 - Gst amount calculation issue at Rd standing
                                                                        serviceTax_Map = new HashMap();
                                                                        serviceTax_Map = calculateServiceTax(PENAL,standingInstructionCreditTO.getProdId());
                                                                        //serviceTax_Map.put("ACT_NUM",standingInstructionCreditTO.getAcctNo());// Commented by nithya for KD-404
                                                                       // System.out.println("serviceTax_Map :: " + serviceTax_Map);
                                                                        if(serviceTax_Map != null && serviceTax_Map.containsKey("TOT_TAX_AMT") && serviceTax_Map.get("TOT_TAX_AMT") != null && CommonUtil.convertObjToDouble(serviceTax_Map.get("TOT_TAX_AMT")) > 0){
                                                                            serviceTax_Map.put("ACT_NUM",standingInstructionCreditTO.getAcctNo());// Added by nithya for KD-404
                                                                            totalGst = CommonUtil.convertObjToDouble(serviceTax_Map.get("TOT_TAX_AMT"));
                                                                        }
                                                                    }
                                                                    debitAmt = depAmt + PENAL;
                                                                    depositPenalMap.put("DEPOSIT_PENAL_AMT", String.valueOf(PENAL));
                                                                    depositPenalMap.put("DEPOSIT_PENAL_MONTH", String.valueOf(delayMonth));
                                                                    multiDepositPenalMap.put(CommonUtil.convertObjToStr(standingInstructionCreditTO.getAcctNo()), depositPenalMap);
                                                                }
                                                                if (debitAmt > 0 && debitAvailabeBal >= debitAmt) {
                                                                    isVariable = true;
                                                                    batchList.add(getCreditTransferTO(standingInstructionCreditTO, (StandingInstructionTO) siTO, branch));
                                                                    if ((where.get("PROD_TYPE").equals("TD")) && (where.get("PROD_ID").equals("RD"))) {
                                                                        String actNum = CommonUtil.convertObjToStr(where.get("ACT_NUM"));
                                                                        actNum = actNum.substring(0, actNum.lastIndexOf("_"));
                                                                        where.put("ACT_NUM", actNum);
                                                                        List depVal = sqlMap.executeQueryForList("getDepValidateData", where);
                                                                        where = null;
                                                                        if ((depVal != null) && (depVal.size() > 0)) {
                                                                            where = (HashMap) depVal.get(0);
                                                                            String actStatus = CommonUtil.convertObjToStr(where.get("ACCT_STATUS"));
                                                                            Date matDate = (Date) where.get("MATURITY_DT");
                                                                            Date todayDate = (Date) curDate.clone();
                                                                            if ((actStatus.equalsIgnoreCase("CLOSED"))
                                                                                    || (DateUtil.dateDiff(matDate, todayDate) > 0)) {
                                                                                depValidate = false;
//                                                                                System.out.println("##### Stop RD Transaction");
                                                                            }
                                                                        }
                                                                    }
                                                                    debitAmt = debitAmt + totalGst;
                                                                    //DEBIT LIST
                                                                    if (debitList != null && debitList.size() > 0) {
                                                                        where = new HashMap();
                                                                        where.put("PROD_TYPE", ((StandingInstructionDebitTO) debitList.get(0)).getProdType());
                                                                        where.put("ACT_NUM", ((StandingInstructionDebitTO) debitList.get(0)).getAcctNo());
                                                                        where.put("PROD_ID", ((StandingInstructionDebitTO) debitList.get(0)).getProdId());//// Added by nithya on 02-03-2018[ to set SI from Daily deposit to RD ]
                                                                        debitProdType = CommonUtil.convertObjToStr(where.get("PROD_TYPE"));
                                                                        if (!(where.get("PROD_TYPE").equals("RM"))) {
                                                                            StandingInstructionDebitTO standingInstructionDebitTO = (StandingInstructionDebitTO) debitList.get(0);
                                                                            if (!standingInstructionDebitTO.getProdType().equals("GL")) {
                                                                                branch = _branchCode;
                                                                                if (CommonUtil.convertObjToDouble(siTO.getSiCharges()).doubleValue() > 0) {
                                                                                    siTO.setSiCharges(CommonUtil.convertObjToDouble(0));
                                                                                }
                                                                                if (CommonUtil.convertObjToDouble(siTO.getExecCharge()).doubleValue() > 0) {
                                                                                    siTO.setExecCharge(CommonUtil.convertObjToDouble(0));
                                                                                }
                                                                                isVariable = true;
                                                                                batchList.add(getDebitTransferTO(standingInstructionDebitTO, (StandingInstructionTO) siTO, branch));
                                                                            }
                                                                        }
                                                                    }
                                                                    if (depValidate) {
                                                                        try {
                                                                            trans.setInitiatedBranch(_branchCode);
                                                                            returnMap = new HashMap();
                                                                            data = new HashMap();
                                                                            data.put("TxTransferTO", batchList);
                                                                            data.put("COMMAND", CommonConstants.TOSTATUS_INSERT);
                                                                            data.put("MODE", CommonConstants.TOSTATUS_INSERT);
                                                                            data.put(CommonConstants.BRANCH_ID, _branchCode);
                                                                            data.put(CommonConstants.SCREEN, "Standing Instruction");
                                                                            if (multiDepositPenalMap != null && multiDepositPenalMap.size() > 0) {
                                                                                data.put("MULTIPLE_DEPOSIT_PENAL", multiDepositPenalMap);
                                                                            }
                                                                            if(serviceTax_Map != null && serviceTax_Map.size() > 0 ){ // Added by nithya on 20-12-2018 for KD 364 - Gst amount calculation issue at Rd standing
                                                                                ArrayList servicTaxDetList = new ArrayList();                                                                                
                                                                                data.put("SERVICE_TAX_MAP", serviceTax_Map);//SERVICE_TAX_DETAILS
                                                                                ServiceTaxDetailsTO objServiceTaxDetailsTO = setServiceTaxDetails(serviceTax_Map);
                                                                                servicTaxDetList.add(objServiceTaxDetailsTO);
                                                                                data.put("SERVICE_TAX_DETAILS", servicTaxDetList);
                                                                            }
                                                                            //AUTHORIZE_MAP
                                                                            if (CommonUtil.convertObjToStr(siTO.getAutomaticPosting()).equals("Y")) {
                                                                                HashMap authorizeMap = new HashMap();
                                                                                authorizeMap.put("BATCH_ID", null);
                                                                                authorizeMap.put("USER_ID", logTO.getUserId());
                                                                                authorizeMap.put(CommonConstants.AUTHORIZESTATUS, CommonConstants.STATUS_AUTHORIZED);
                                                                                data.put(CommonConstants.AUTHORIZEMAP, authorizeMap);
                                                                                //Added By Kannan AR ADV_TRANS_DETAILS table record should update properly KDSA:228
                                                                                if (debitProdType.equalsIgnoreCase("AD")) {
                                                                                    data.put("DEBIT_LOAN_TYPE", "DP");
                                                                                }
                                                                            }
                                                                            try {
                                                                                debitAvailabeBal = debitAvailabeBal-debitAmt;
                                                                                isTransaction = true; //Added By Kannan AR
//                                                                                System.out.println("############## Before Calling Transfer DAO : " + data);
                                                                                transReturnMap = transferDAO.execute(data, false);
                                                                            } catch (Exception e) {
                                                                                System.out.println("################ Error :" + e);
                                                                                returnMap.put(data.get("SI_ID"), e);
                                                                                sqlMap.rollbackTransaction();
                                                                            }
                                                                        } catch (Exception transError) {
                                                                            sqlMap.rollbackTransaction();
                                                                            transError.printStackTrace();
                                                                        }
                                                                    }
                                                                }
                                                            }
                                                        }//END FOR LOOP
                                                        //To Check Current Installment
//                                                        System.out.println("##################### PENDING AND CURRENT INSTALLMENT TO CHECK: " + depositNo);
                                                        HashMap depMap = new HashMap();
                                                        depMap.put("CURRDATE", curDate.clone());
                                                        depMap.put("DEPOSIT_NO", CommonUtil.convertObjToStr(standingInstructionCreditTO.getAcctNo()));
                                                        List curInstLst = sqlMap.executeQueryForList("getCheckCurrentInstForRD", depMap);
                                                        if (curInstLst != null && curInstLst.size() > 0) {
                                                            debitAmt = 0.0;
                                                            data = new HashMap();
                                                            trans = new TransferTrans();
                                                            batchList = new ArrayList();
                                                            transferDAO = new TransferDAO();
                                                            debitAmt = depAmt;
                                                            if (debitAmt > 0 && debitAvailabeBal>debitAmt) {
                                                                isVariable = true;
                                                                batchList.add(getCreditTransferTO(standingInstructionCreditTO, (StandingInstructionTO) siTO, branch));
                                                                if ((where.get("PROD_TYPE").equals("TD")) && (where.get("PROD_ID").equals("RD"))) {
                                                                    String actNum = CommonUtil.convertObjToStr(where.get("ACT_NUM"));
                                                                    actNum = actNum.substring(0, actNum.lastIndexOf("_"));
                                                                    where.put("ACT_NUM", actNum);
                                                                    List depVal = sqlMap.executeQueryForList("getDepValidateData", where);
                                                                    where = null;
                                                                    if ((depVal != null) && (depVal.size() > 0)) {
                                                                        where = (HashMap) depVal.get(0);
                                                                        String actStatus = CommonUtil.convertObjToStr(where.get("ACCT_STATUS"));
                                                                        Date matDate = (Date) where.get("MATURITY_DT");
                                                                        Date todayDate = (Date) curDate.clone();
                                                                        if ((actStatus.equalsIgnoreCase("CLOSED"))
                                                                                || (DateUtil.dateDiff(matDate, todayDate) > 0)) {
                                                                            depValidate = false;
//                                                                            System.out.println("##### Stop RD Transaction");
                                                                        }
                                                                    }
                                                                }
                                                                //DEBIT LIST
                                                                if (debitList != null && debitList.size() > 0) {
                                                                    where = new HashMap();
                                                                    where.put("PROD_TYPE", ((StandingInstructionDebitTO) debitList.get(0)).getProdType());
                                                                    where.put("ACT_NUM", ((StandingInstructionDebitTO) debitList.get(0)).getAcctNo());
                                                                    debitProdType = CommonUtil.convertObjToStr(where.get("PROD_TYPE"));
                                                                    if (!(where.get("PROD_TYPE").equals("RM"))) {
                                                                        StandingInstructionDebitTO standingInstructionDebitTO = (StandingInstructionDebitTO) debitList.get(0);
                                                                        if (!standingInstructionDebitTO.getProdType().equals("GL")) {
                                                                            branch = _branchCode;
                                                                            if (CommonUtil.convertObjToDouble(siTO.getSiCharges()).doubleValue() > 0) {
                                                                                siTO.setSiCharges(CommonUtil.convertObjToDouble(0));
                                                                            }
                                                                            if (CommonUtil.convertObjToDouble(siTO.getExecCharge()).doubleValue() > 0) {
                                                                                siTO.setExecCharge(CommonUtil.convertObjToDouble(0));
                                                                            }
                                                                            isVariable = true;
                                                                            batchList.add(getDebitTransferTO(standingInstructionDebitTO, (StandingInstructionTO) siTO, branch));
                                                                        }
                                                                    }
                                                                }
                                                                if (depValidate) {
                                                                    try {
                                                                        trans.setInitiatedBranch(_branchCode);
                                                                        returnMap = new HashMap();
                                                                        data = new HashMap();
                                                                        data.put("TxTransferTO", batchList);
                                                                        data.put("COMMAND", CommonConstants.TOSTATUS_INSERT);
                                                                        data.put("MODE", CommonConstants.TOSTATUS_INSERT);
                                                                        data.put(CommonConstants.BRANCH_ID, _branchCode);
                                                                        data.put(CommonConstants.SCREEN, "Standing Instruction");
                                                                        //AUTHORIZE_MAP
                                                                        if (CommonUtil.convertObjToStr(siTO.getAutomaticPosting()).equals("Y")) {
                                                                            HashMap authorizeMap = new HashMap();
                                                                            authorizeMap.put("BATCH_ID", null);
                                                                            authorizeMap.put("USER_ID", logTO.getUserId());
                                                                            authorizeMap.put(CommonConstants.AUTHORIZESTATUS, CommonConstants.STATUS_AUTHORIZED);
                                                                            data.put(CommonConstants.AUTHORIZEMAP, authorizeMap);
                                                                            //Added By Kannan AR ADV_TRANS_DETAILS table record should update properly KDSA:228
                                                                            if (debitProdType.equalsIgnoreCase("AD")) {
                                                                                data.put("DEBIT_LOAN_TYPE", "DP");
                                                                            }
                                                                        }
                                                                        try {
                                                                            debitAvailabeBal = debitAvailabeBal-debitAmt;
                                                                            isTransaction = true; //Added By Kannan AR
//                                                                            System.out.println("############## Before Calling Transfer DAO : " + data);
                                                                            transReturnMap = transferDAO.execute(data, false);
                                                                        } catch (Exception e) {
                                                                            System.out.println("################ Error :" + e);
                                                                            returnMap.put(data.get("SI_ID"), e);
                                                                        }
                                                                    } catch (Exception transError) {
                                                                        sqlMap.rollbackTransaction();
                                                                        transError.printStackTrace();
                                                                    }
                                                                }
                                                            }
                                                        }

                                                    }
                                                } else {
//                                                    System.out.println("##################### NO PENDING & CHECK CURRENT INSTALLMENT: " + depositNo);
                                                    HashMap depMap = new HashMap();
                                                    depMap.put("CURRDATE", curDate.clone());
                                                    depMap.put("DEPOSIT_NO", CommonUtil.convertObjToStr(standingInstructionCreditTO.getAcctNo()));
                                                    List curInstLst = sqlMap.executeQueryForList("getCheckCurrentInstallmentRD", depMap);
                                                    if (curInstLst != null && curInstLst.size() > 0) {
                                                        debitAmt = 0.0;
                                                        data = new HashMap();
                                                        trans = new TransferTrans();
                                                        batchList = new ArrayList();
                                                        transferDAO = new TransferDAO();
                                                        debitAmt = depAmt;
                                                        if (debitAmt > 0 && debitAvailabeBal >= debitAmt) {
                                                            isVariable = true;
                                                            batchList.add(getCreditTransferTO(standingInstructionCreditTO, (StandingInstructionTO) siTO, branch));
                                                            if ((where.get("PROD_TYPE").equals("TD")) && (where.get("PROD_ID").equals("RD"))) {
                                                                String actNum = CommonUtil.convertObjToStr(where.get("ACT_NUM"));
                                                                actNum = actNum.substring(0, actNum.lastIndexOf("_"));
                                                                where.put("ACT_NUM", actNum);
                                                                List depVal = sqlMap.executeQueryForList("getDepValidateData", where);
                                                                where = null;
                                                                if ((depVal != null) && (depVal.size() > 0)) {
                                                                    where = (HashMap) depVal.get(0);
                                                                    String actStatus = CommonUtil.convertObjToStr(where.get("ACCT_STATUS"));
                                                                    Date matDate = (Date) where.get("MATURITY_DT");
                                                                    Date todayDate = (Date) curDate.clone();
                                                                    if ((actStatus.equalsIgnoreCase("CLOSED"))
                                                                            || (DateUtil.dateDiff(matDate, todayDate) > 0)) {
                                                                        depValidate = false;
//                                                                        System.out.println("##### Stop RD Transaction");
                                                                    }
                                                                }
                                                            }
                                                            //DEBIT LIST
                                                            if (debitList != null && debitList.size() > 0) {
                                                                where = new HashMap();
                                                                where.put("PROD_TYPE", ((StandingInstructionDebitTO) debitList.get(0)).getProdType());
                                                                where.put("ACT_NUM", ((StandingInstructionDebitTO) debitList.get(0)).getAcctNo());
                                                                debitProdType = CommonUtil.convertObjToStr(where.get("PROD_TYPE"));
                                                               if (!(where.get("PROD_TYPE").equals("RM"))) {
                                                                    StandingInstructionDebitTO standingInstructionDebitTO = (StandingInstructionDebitTO) debitList.get(0);
                                                                    if (!standingInstructionDebitTO.getProdType().equals("GL")) {
                                                                        branch = _branchCode;
                                                                        if (CommonUtil.convertObjToDouble(siTO.getSiCharges()).doubleValue() > 0) {
                                                                            siTO.setSiCharges(CommonUtil.convertObjToDouble(0));
                                                                        }
                                                                        if (CommonUtil.convertObjToDouble(siTO.getExecCharge()).doubleValue() > 0) {
                                                                            siTO.setExecCharge(CommonUtil.convertObjToDouble(0));
                                                                        }
                                                                        isVariable = true;
                                                                        batchList.add(getDebitTransferTO(standingInstructionDebitTO, (StandingInstructionTO) siTO, branch));
                                                                    }
                                                                }
                                                            }
                                                            if (depValidate) {
                                                                try {
                                                                    trans.setInitiatedBranch(_branchCode);
                                                                    returnMap = new HashMap();
                                                                    data = new HashMap();
                                                                    data.put("TxTransferTO", batchList);
                                                                    data.put("COMMAND", CommonConstants.TOSTATUS_INSERT);
                                                                    data.put("MODE", CommonConstants.TOSTATUS_INSERT);
                                                                    data.put(CommonConstants.BRANCH_ID, _branchCode);
                                                                    data.put(CommonConstants.SCREEN, "Standing Instruction");
                                                                    //AUTHORIZE_MAP
                                                                    if (CommonUtil.convertObjToStr(siTO.getAutomaticPosting()).equals("Y")) {
                                                                        HashMap authorizeMap = new HashMap();
                                                                        authorizeMap.put("BATCH_ID", null);
                                                                        authorizeMap.put("USER_ID", logTO.getUserId());
                                                                        authorizeMap.put(CommonConstants.AUTHORIZESTATUS, CommonConstants.STATUS_AUTHORIZED);
                                                                        data.put(CommonConstants.AUTHORIZEMAP, authorizeMap);
                                                                        //Added By Kannan AR ADV_TRANS_DETAILS table record should update properly KDSA:228
                                                                        if (debitProdType.equalsIgnoreCase("AD")) {
                                                                            data.put("DEBIT_LOAN_TYPE", "DP");
                                                                        }
                                                                    }
                                                                    try {
                                                                        debitAvailabeBal = debitAvailabeBal-debitAmt;
                                                                        isTransaction = true; //Added By Kannan AR
//                                                                        System.out.println("############## Before Calling Transfer DAO : " + data);
                                                                        transReturnMap = transferDAO.execute(data, false);
                                                                    } catch (Exception e) {
                                                                        System.out.println("################ Error :" + e);
                                                                        returnMap.put(data.get("SI_ID"), e);
                                                                    }
                                                                } catch (Exception transError) {
                                                                    sqlMap.rollbackTransaction();
                                                                    transError.printStackTrace();
                                                                }
                                                            }
                                                        }
                                                    }
                                                }
                                            }
                                        }
                                    }
                                    if (isTransaction) {//code changes done By Kannan AR 29-Aug-2017
                                        updateNextRunDtSI(siTO);
                                        /*paramMap = new HashMap();
                                        paramMap.put("SI_ID", siTO.getSiId());
                                        paramMap.put("EXEC_DT", curDate);                                        
                                        sqlMap.executeUpdate("updateStandingInstrManual", paramMap);
                                        sqlMap.executeUpdate("updateStandingInstrBatchManual", paramMap);                                        
                                        updateStandingInstruction(siTO);*/
                                    }
                                }
                            }
                        }
                    }
                }
            }
            HashMap whereMap = new HashMap();
            whereMap.put("BATCH_ID", singleTransId);
            whereMap.put("BRANCH_ID", _branchCode);
            whereMap.put("TRANS_DT", curDate);
            whereMap.put("STATUS", "CREAETD");
            sqlMap.executeUpdate("insertStandingTLProcessed", whereMap);
            sqlMap.commitTransaction();
        } catch (Exception e) {
            sqlMap.rollbackTransaction();
            e.printStackTrace();
            throw new TransRollbackException(e);
        }
        return transReturnMap;
    }

    //added for varible standing by rishad
    private HashMap executeVaribleSi(HashMap map) throws Exception {
        HashMap paramMap = new HashMap();
        singleTransId = this.generateLinkID();
        try {
            sqlMap.startTransaction();
            StandingInstructionTO siTO;
             String depositNo="";
            List siLst = (List) map.get("EXECUTE_VARIABLE_LIST");
            if (siLst != null && siLst.size() > 0) {
                for (int i = 0; i < siLst.size(); i++) {
                    depValidate=true;
                    ArrayList batchList = new ArrayList();;
                    HashMap siIdMap = (HashMap) siLst.get(i);
                    siIdMap.put("CURRDATE", curDate);
                    List list = sqlMap.executeQueryForList("standingBatchRunVariableManual", siIdMap);
                    if (list != null && list.size() > 0) {                                                //IF STANDING INSTRUCTION IS FIXED
                        for (int j = 0; j < list.size(); j++) {
                            siTO = (StandingInstructionTO) list.get(j);
                            HashMap siId = new HashMap();
                            siId.put("SI_ID", siTO.getSiId());
                            siId.put("CURRDATE", curDate);
                            List creditList = sqlMap.executeQueryForList("getSelectStandingInstructionVariableCreditTO", siId);
                            List debitList = sqlMap.executeQueryForList("getSelectStandingInstructionVariableDebitTO", siId);
                            if (debitList != null && debitList.size() > 0) {
                                for (int x = 0, y = debitList.size(); x < y; x++) {
                                    HashMap where = new HashMap();
                                    where.put("PROD_TYPE", ((StandingInstructionDebitTO) debitList.get(x)).getProdType());
                                    depositNo=((StandingInstructionDebitTO) debitList.get(x)).getAcctNo();
                                    where.put("ACT_NUM", ((StandingInstructionDebitTO) debitList.get(x)).getAcctNo());
                                    if (!(where.get("PROD_TYPE").equals("RM"))) {
                                        StandingInstructionDebitTO standingInstructionDebitTO = (StandingInstructionDebitTO) debitList.get(x);
                                        if (!standingInstructionDebitTO.getProdType().equals("GL")) {
                                            HashMap branchidMap = (HashMap) sqlMap.executeQueryForObject("getBranchCode_ACT_MASTER"
                                                    + standingInstructionDebitTO.getProdType(), where);
                                            String branch = _branchCode;
                                            batchList.add(getDebitTransferTO(standingInstructionDebitTO, (StandingInstructionTO) siTO, branch));
                                            branchidMap = null;
                                        }
                                    } else {
                                    }
                                }
                            }
                            TransferTrans trans = new TransferTrans();
                            TransferDAO transferDAO = new TransferDAO();

                            if (creditList != null && creditList.size() > 0) {
                                for (int x = 0, z = creditList.size(); x < z; x++) {
                                    HashMap where = new HashMap();
                                    where.put("ACT_NUM", ((StandingInstructionCreditTO) creditList.get(x)).getAcctNo());
                                    where.put("PROD_ID", ((StandingInstructionCreditTO) creditList.get(x)).getProdId());
                                    where.put("PROD_TYPE", ((StandingInstructionCreditTO) creditList.get(x)).getProdType());
                                    prodType = CommonUtil.convertObjToStr(where.get("PROD_TYPE"));
                                    if (!(where.get("PROD_TYPE").equals("RM"))) {
                                        String mapName = "getDetailsSIAcNumCredit" + ((StandingInstructionCreditTO) creditList.get(x)).getProdType();
                                        StandingInstructionCreditTO standingInstructionCreditTO = (StandingInstructionCreditTO) creditList.get(x);
                                        if (!standingInstructionCreditTO.getProdType().equals("GL")) {
                                            HashMap branchidMap = (HashMap) sqlMap.executeQueryForObject("getBranchCode_ACT_MASTER"
                                                    + standingInstructionCreditTO.getProdType(), where);
                                            String branch = _branchCode;
                                            batchList.add(getCreditTransferTO(standingInstructionCreditTO, (StandingInstructionTO) siTO, branch));
                                            if (CommonUtil.convertObjToDouble(siTO.getSiAgentComm()).doubleValue() > 0) {
                                                batchList.add(getAgentCommitionCreditTransferTO(standingInstructionCreditTO, (StandingInstructionTO) siTO, branch,depositNo));
                                            }
                                            if (CommonUtil.convertObjToDouble(siTO.getSiCharges()).doubleValue() > 0) {
                                                batchList.add(getSiChargeCreditTransferTO(standingInstructionCreditTO, (StandingInstructionTO) siTO, branch));
                                            }
                                            if (CommonUtil.convertObjToDouble(siTO.getExecCharge()).doubleValue() > 0) {
                                                batchList.add(getSiChargeSTCreditTransferTO(standingInstructionCreditTO, (StandingInstructionTO) siTO, branch));
                                            }
                                            branchidMap = null;
                                        }

                                        if ((where.get("PROD_TYPE").equals("TD")) && (where.get("PROD_ID").equals("RD"))) {
                                            String actNum = CommonUtil.convertObjToStr(where.get("ACT_NUM"));
                                            actNum = actNum.substring(0, actNum.lastIndexOf("_"));
                                            where.put("ACT_NUM", actNum);
                                            List depVal = sqlMap.executeQueryForList("getDepValidateData", where);
                                            where = null;
                                            if ((depVal != null) && (depVal.size() > 0)) {
                                                where = (HashMap) depVal.get(0);
                                                String actStatus = CommonUtil.convertObjToStr(where.get("ACCT_STATUS"));
                                                Date matDate = (Date) where.get("MATURITY_DT");
                                                Date todayDate = (Date) curDate.clone();
                                                if ((actStatus.equalsIgnoreCase("CLOSED"))
                                                        || (DateUtil.dateDiff(matDate, todayDate) > 0)) {
                                                    depValidate = false;
                                                }
                                            }
                                        }
                                        if (where.get("PROD_TYPE").equals("TL") || where.get("PROD_TYPE").equals("AD")) {
                                            HashMap loanMap = new HashMap();
                                            loanMap = interestCalculationTLAD(CommonUtil.convertObjToStr(where.get("ACT_NUM")),
                                                    CommonUtil.convertObjToStr(where.get("PROD_ID")),CommonUtil.convertObjToStr(where.get("PROD_TYPE")),standingInstructionCreditTO.getAmount(),siTO);
                                            if (where.get("PROD_TYPE").equals(TransactionFactory.LOANS)&&loanMap!=null&&loanMap.size()>0) {
                                                
                                                HashMap whereMap = new HashMap();
                                                siId.put("ASONDATE", lastIntCalcDtForSalRecovery);
                                                 siId.put("SAL_REC_MODULE", CommonUtil.convertObjToStr(CommonConstants.SAL_REC_MODULE));
                                                List debitLists = sqlMap.executeQueryForList("getSelectStandingInstructionDebitTO", siId);
                                                if (debitLists != null && debitLists.size() > 0) {
                                                    String loanActNumber = "";
                                                    String depositActNumber = "";
                                                    String sid = "";
                                                    depositActNumber = CommonUtil.convertObjToStr(((StandingInstructionDebitTO) debitLists.get(x)).getAcctNo());
                                                    sid = CommonUtil.convertObjToStr(((StandingInstructionDebitTO) debitLists.get(x)).getSiId());
                                                    loanActNumber = CommonUtil.convertObjToStr(where.get("ACT_NUM"));
                                                    String debPrdType = CommonUtil.convertObjToStr(((StandingInstructionDebitTO) debitLists.get(x)).getProdType());
                                                    HashMap whrmap = new HashMap();
                                                    HashMap hash = null;
                                                    whrmap.put("SI_ID", sid);
                                                    whrmap.put("BRANCH_CODE", _branchCode);
                                                    List lst11 = sqlMap.executeQueryForList("getStandingInstalmentNumber", whrmap);
                                                    boolean chkflag = true;
                                                    if (lst11 != null && lst11.size() > 0) {
                                                        hash = (HashMap) lst11.get(0);
                                                        if (hash != null && hash.containsKey("INSTALMENT_YN") && CommonUtil.convertObjToStr(hash.get("INSTALMENT_YN")).equals("Y")) {
                                                            chkflag = false;
                                                        }
                                                    }
                                                    if(loanMap.containsKey("EMI_IN_SIMPLEINTREST") && loanMap.get("EMI_IN_SIMPLEINTREST") != null && loanMap.get("EMI_IN_SIMPLEINTREST").equals("Y")){
                                                        chkflag = false;
                                                    }
                                                    if (chkflag) {
                                                        closeLoanAccount(loanActNumber, depositActNumber, sid, debPrdType);
                                                    }
                                                }
                                            }
                                            if(loanMap!=null&&loanMap.size()>0){
                                            List waiveList = sqlMap.executeQueryForList("getwaiveoffDetails", where);
                                            if (waiveList != null && waiveList.size() > 0) {
                                                HashMap waiveMap = (HashMap) waiveList.get(0);
                                                loanMap.put("PENAL_WAIVE_OFF", waiveMap.get("PENAL_WAIVER"));
                                            }
                                             if (loanMap.containsKey("OTHER CHARGES")) {//Other charges code added by Kannan AR on 18-Sep-2017
                                                HashMap otherChgMap = new HashMap();                                                
                                                otherChgMap.put("OTHER CHARGES", loanMap.get("OTHER CHARGES"));
                                                loanMap.remove("OTHER CHARGES");
                                                loanMap.put("OTHER_CHARGES", otherChgMap);                                                
                                                trans.setAllAmountMap(loanMap);                                                
                                            }   }  
                                         
                                            trans.setAllAmountMap(loanMap);
                                        }
                                    } else {
                                    }
                                }
                            }
                            if (depValidate) {
                                try {
//                         sqlMap.startTransaction();
                                    data = new HashMap(); // KDSA - 778
                                    
                                    if (!prodType.equalsIgnoreCase("RM")) {
                                        trans.setInitiatedBranch(_branchCode);
                                        trans.setLoanDebitInt("DP");
                                        //modified by rishad 05/09/2018
                                        //System.out.println("trans.getAllAmountMap() :: " + trans.getAllAmountMap());
                                        data.put("ALL_AMOUNT", trans.getAllAmountMap());
                                        if (trans.getAllAmountMap().containsKey("REPAYMENT_TYPE") && trans.getAllAmountMap().get("REPAYMENT_TYPE")!=null && trans.getAllAmountMap().get("REPAYMENT_TYPE").equals("EMI") &&(siTO.getChkPendingInstalment().equalsIgnoreCase("Y") || siTO.getChkInstalment().equalsIgnoreCase("Y"))) {
                                            if(trans.getAllAmountMap().containsKey("EMI_IN_SIMPLEINTREST") && trans.getAllAmountMap().get("EMI_IN_SIMPLEINTREST") != null && trans.getAllAmountMap().get("EMI_IN_SIMPLEINTREST").equals("Y")){
                                              transReturnMap = doTransactionforEMIInSimpleInterest(batchList, _branchCode, siTO);  
                                            }else{
                                              transReturnMap = doTransactionforPendingInstallMent(batchList, _branchCode, siTO);
                                            }
                                        } else {
                                            transReturnMap = doAuthTransaction(batchList, _branchCode, siTO);
                                        }
                                        if (siTO.getAutomaticPosting().equalsIgnoreCase("N")) {
                                            trans.setLoanDebitInt("");
                                        }
                                    } else {
                                        if (debitList != null && debitList.size() > 0) {
                                            for (int x = 0, y = debitList.size(); x < y; x++) {
                                                HashMap where = new HashMap();
                                                where.put("PROD_TYPE", ((StandingInstructionCreditTO) creditList.get(x)).getProdType());
                                                if ((where.get("PROD_TYPE").equals("RM"))) {
                                                    StandingInstructionDebitTO standingInstructionDebitTO = (StandingInstructionDebitTO) debitList.get(x);
                                                    final HashMap accountNameMap = new HashMap();
                                                    String custName = "";
                                                    accountNameMap.put("ACC_NUM", standingInstructionDebitTO.getAcctNo());
                                                    final List resultList = sqlMap.executeQueryForList("getAccountNumberName" + standingInstructionDebitTO.getProdType(), accountNameMap);
                                                    if (resultList != null) {
                                                        if (resultList.size() != 0) {
                                                            final HashMap resultMap = (HashMap) resultList.get(0);
                                                            if (resultMap != null) {
                                                                custName = resultMap.get("CUSTOMER_NAME").toString();
                                                            }
                                                        }
                                                    }
                                                    Date todayDt = (Date) curDate.clone();
                                                    HashMap dtatMap = new HashMap();
                                                    LinkedHashMap notDelMap = new LinkedHashMap();
                                                    LinkedHashMap notDelRemMap = new LinkedHashMap();
                                                    dtatMap.put("MODE", "INSERT");
                                                    TransactionTO transfer = new TransactionTO();
                                                    transfer.setTransType("TRANSFER");
                                                    transfer.setTransAmt(new Double(CommonUtil.convertObjToDouble(standingInstructionDebitTO.getAmount()).doubleValue()
                                                            + CommonUtil.convertObjToDouble(siTO.getRemitCharges()).doubleValue()
                                                            + CommonUtil.convertObjToDouble(siTO.getAcceptanceCharge()).doubleValue()
                                                            + CommonUtil.convertObjToDouble(siTO.getServiceTax()).doubleValue()));
                                                    transfer.setProductId(CommonUtil.convertObjToStr(standingInstructionDebitTO.getProdId()));
                                                    transfer.setProductType(CommonUtil.convertObjToStr(standingInstructionDebitTO.getProdType()));
                                                    transfer.setDebitAcctNo(CommonUtil.convertObjToStr(standingInstructionDebitTO.getAcctNo()));
                                                    transfer.setApplName(CommonUtil.convertObjToStr(custName));  // Added by Rajesh
                                                    //                transfer.setDebitAcctNo(CommonUtil.convertObjToStr(paramMap.get("ACT_NUM")));
                                                    transfer.setInstType("VOUCHER");
                                                    transfer.setChequeDt(todayDt);
                                                    LinkedHashMap transMap = new LinkedHashMap();
                                                    LinkedHashMap remMap = new LinkedHashMap();
                                                    transfer.setCommand("INSERT");
                                                    notDelMap.put(String.valueOf(1), transfer);//"NOT_DELETED_TRANS_TOs"
                                                    transMap.put("NOT_DELETED_TRANS_TOs", notDelMap);
                                                    dtatMap.put("TransactionTO", transMap);
                                                    dtatMap.put(CommonConstants.BRANCH_ID, _branchCode);
                                                    dtatMap.put("OPERATION_MODE", "ISSUE");
                                                    dtatMap.put("AUTHORIZEMAP", null);
                                                    RemittanceIssueTO remt = new RemittanceIssueTO();
                                                    remt.setDraweeBranchCode(_branchCode);
                                                    remt.setAmount(standingInstructionDebitTO.getAmount());
                                                    remt.setCategory("GENERAL_CATEGORY");
                                                    remt.setRemarks(siTO.getSiId());
                                                    HashMap behavesMap = new HashMap();
                                                    behavesMap.put("BEHAVES_LIKE", "PO");
                                                    List lstRemit = sqlMap.executeQueryForList("selectRemitProductId", behavesMap);
                                                    if (lstRemit != null && lstRemit.size() > 0) {
                                                        behavesMap = (HashMap) lstRemit.get(0);
                                                        remt.setProdId(CommonUtil.convertObjToStr(behavesMap.get("PROD_ID")));
                                                    }
                                                    HashMap draweeMap = new HashMap();
                                                    if (behavesMap.get("PAY_ISSUE_BRANCH").equals("ISSU_BRANCH")) {
                                                        lstRemit = sqlMap.executeQueryForList("getSelectBankTOList", null);
                                                        if (lstRemit != null && lstRemit.size() > 0) {
                                                            draweeMap = (HashMap) lstRemit.get(0);
                                                        }
                                                    }
                                                    remt.setFavouring(((StandingInstructionCreditTO) creditList.get(x)).getAcctNo());
                                                    remt.setDraweeBank(CommonUtil.convertObjToStr(draweeMap.get("BANK_CODE")));
                                                    remt.setBranchId(_branchCode);
                                                    remt.setCity("560");
                                                    remt.setInstrumentNo1("PO");
                                                    remt.setCommand("INSERT");
                                                    remt.setExchange(CommonUtil.convertObjToDouble(siTO.getRemitCharges()));
                                                    remt.setPostage(CommonUtil.convertObjToDouble(siTO.getAcceptanceCharge()));
                                                    remt.setOtherCharges(CommonUtil.convertObjToDouble(siTO.getServiceTax()));
                                                    remt.setTotalAmt(new Double(CommonUtil.convertObjToDouble(standingInstructionDebitTO.getAmount()).doubleValue()
                                                            + CommonUtil.convertObjToDouble(siTO.getRemitCharges()).doubleValue()
                                                            + CommonUtil.convertObjToDouble(siTO.getAcceptanceCharge()).doubleValue()
                                                            + CommonUtil.convertObjToDouble(siTO.getServiceTax()).doubleValue()));
                                                    remt.setStatusDt(todayDt);
                                                    remt.setStatusBy("TTSYSTEM");
                                                    notDelRemMap.put(String.valueOf(1), remt);
                                                    remMap.put("NOT_DELETED_ISSUE_TOs", notDelRemMap);
                                                    dtatMap.put("RemittanceIssueTO", remMap);
                                                    RemittanceIssueDAO remDao = new RemittanceIssueDAO();
                                                    HashMap resulMap = new HashMap();
                                                    remDao.setFromotherDAo(false);
                                                    resulMap = remDao.execute(dtatMap);
                                                    behavesMap = null;
                                                    draweeMap = null;
                                                    lstRemit = null;
                                                }
                                            }
                                        }

                                        //si charges
                                        batchList = new ArrayList();
                                        if (debitList != null && debitList.size() > 0) {
                                            for (int x = 0, y = debitList.size(); x < y; x++) {
                                                HashMap where = new HashMap();
                                                where.put("PROD_TYPE", ((StandingInstructionDebitTO) debitList.get(x)).getProdType());
                                                where.put("ACT_NUM", ((StandingInstructionDebitTO) debitList.get(x)).getAcctNo());
                                                if (!(where.get("PROD_TYPE").equals("RM"))) {
                                                    StandingInstructionDebitTO standingInstructionDebitTO = (StandingInstructionDebitTO) debitList.get(x);
                                                    if (!standingInstructionDebitTO.getProdType().equals("GL")) {
                                                        HashMap branchidMap = (HashMap) sqlMap.executeQueryForObject("getBranchCode_ACT_MASTER"
                                                                + standingInstructionDebitTO.getProdType(), where);
                                                        String branch = _branchCode;
                                                        batchList.add(getDebitSiChargesTransferTO(standingInstructionDebitTO, (StandingInstructionTO) siTO, branch));
                                                        branchidMap = null;
                                                    }
                                                } else {
                                                }
                                            }
                                        }
                                        if (creditList != null && creditList.size() > 0) {
                                            for (int x = 0, z = creditList.size(); x < z; x++) {
                                                HashMap where = new HashMap();
                                                where.put("ACT_NUM", ((StandingInstructionCreditTO) creditList.get(x)).getAcctNo());
                                                where.put("PROD_ID", ((StandingInstructionCreditTO) creditList.get(x)).getProdId());
                                                where.put("PROD_TYPE", ((StandingInstructionCreditTO) creditList.get(x)).getProdType());
                                                prodType = CommonUtil.convertObjToStr(where.get("PROD_TYPE"));
                                                StandingInstructionCreditTO standingInstructionCreditTO = (StandingInstructionCreditTO) creditList.get(x);
                                                String branch = _branchCode;                                                if (CommonUtil.convertObjToDouble(siTO.getSiCharges()).doubleValue() > 0) {
                                                    batchList.add(getSiChargeCreditTransferTO(standingInstructionCreditTO, (StandingInstructionTO) siTO, branch));
                                                }
                                                if (CommonUtil.convertObjToDouble(siTO.getExecCharge()).doubleValue() > 0) {
                                                    batchList.add(getSiChargeSTCreditTransferTO(standingInstructionCreditTO, (StandingInstructionTO) siTO, branch));
                                                }
                                            }
                                        }
                                        trans = new TransferTrans();
                                        trans.setInitiatedBranch(_branchCode);
                                        trans.setLoanDebitInt("DP");
//                                        if (siTO.getAutomaticPosting().equalsIgnoreCase("Y")) {
//                                            data.put("ALL_AMOUNT", trans.getAllAmountMap());
//                                            transReturnMap = doAuthTransaction(batchList, _branchCode,siTO.getSiId());
//                                        } else {
//                                            data.put("ALL_AMOUNT", trans.getAllAmountMap());
//                                            transReturnMap = doAuthTransaction(batchList, _branchCode, siTO.getSiId());
//                                        }
                                          data.put("ALL_AMOUNT", trans.getAllAmountMap());
                                          transReturnMap = doAuthTransaction(batchList, _branchCode, siTO); 
                                         trans.setLoanDebitInt("");
                                    }
                                    if (isTransaction) {
                                        paramMap = new HashMap();
                                        paramMap.put("SI_ID", siTO.getSiId());
                                        paramMap.put("EXEC_DT", curDate);
                                        sqlMap.executeUpdate("updateStandingInstrManual", paramMap);
                                        sqlMap.executeUpdate("updateStandingInstrBatchManual", paramMap);
                                        updateStandingInstruction(siTO);
                                    }
                                } catch (Exception transError) {
                                    sqlMap.rollbackTransaction();
                                    transError.printStackTrace();
                                }
                            }
                        }
                    }
                }
            }
            HashMap whereMap = new HashMap();
            whereMap.put("BATCH_ID", singleTransId);
            whereMap.put("BRANCH_ID", _branchCode);
            whereMap.put("TRANS_DT", curDate);
            whereMap.put("STATUS", "CREAETD");
            sqlMap.executeUpdate("insertStandingTLProcessed", whereMap);
            sqlMap.commitTransaction();
        } catch (Exception e) {
            sqlMap.rollbackTransaction();
            e.printStackTrace();
            throw new TransRollbackException(e);
        }
        return transReturnMap;
    }

    public void updateStandingInstruction(StandingInstructionTO siTO) throws Exception {
        System.out.println("StandingInstructionTO############" + siTO);
        HashMap paramMap = new HashMap();
        paramMap.put("SI_ID", siTO.getSiId());
        paramMap.put("RUN_DT", siTO.getLastRunDt());
        paramMap.put("CURR_DT", curDate.clone());
        // Added by nithya on 03-03-2017 for 5763 [ RBI ]
        if (CommonUtil.convertObjToStr(CommonConstants.SAL_REC_MODULE).equals("Y")) {             
            paramMap.put("CURR_DT", lastIntCalcDtForSalRecovery);
        }
        if (paramMap.get("RUN_DT") != null) {
            sqlMap.executeUpdate("updateStandingInstrManual2", paramMap);
        } else {
            sqlMap.executeUpdate("updateStandingInstrManual1", paramMap);
        }
    }
    
    public void updateNextRunDtSI(StandingInstructionTO siTO) throws Exception { //Added By Kannan AR
        System.out.println("StandingInstructionTO############" + siTO);
        if (!CommonUtil.convertObjToStr(siTO.getSiStartDt()).equals("")) {
            HashMap paramMap = new HashMap();
            paramMap.put("SI_ID", siTO.getSiId());
            paramMap.put("CURR_DT", curDate.clone());
            Date startDt = DateUtil.getDateMMDDYYYY(CommonUtil.convertObjToStr(siTO.getSiStartDt()));
            Date nextRunDt = (Date) curDate.clone();
            nextRunDt.setDate(startDt.getDate());
            if (CommonUtil.convertObjToInt(startDt.getDate()) <= CommonUtil.convertObjToInt(curDate.getDate())) {
                nextRunDt = DateUtil.addDays(nextRunDt, CommonUtil.convertObjToInt(siTO.getFrequency()));
            }
            paramMap.put("NEXT_RUN_DT", nextRunDt);
            sqlMap.executeUpdate("updateNextRunDtSI", paramMap);
        }
    }

    public HashMap doTransaction(ArrayList list, String branch) throws Exception {
        //System.out.println("transfer###"+trans.getAllAmountMap());
        //List returnlist=new ArrayList();    
        TransferDAO transferDAO = new TransferDAO();
        HashMap returnMap = new HashMap();
        data.put("TxTransferTO", list);
        data.put("COMMAND", CommonConstants.TOSTATUS_INSERT);
        data.put("MODE", CommonConstants.TOSTATUS_INSERT);
        data.put(CommonConstants.BRANCH_ID, branch);
        data.put(CommonConstants.SCREEN, "Standing Instruction");
        try {
            returnMap = transferDAO.execute(data, false);
        } catch (Exception e) {
            System.out.println("#$#$ Error :" + e);
            returnMap.put(data.get("SI_ID"), e);

        }
        return returnMap;
        //return transReturnMap;
    }

    public ArrayList adjustTransactionAmount(ArrayList list, int inst_no,String instType) {
        String debAccNo = "";
        double totalBalAmount = 0, instalment_amt = 0, miscamt = 0,paidinstalment_amt =0;
        ArrayList retList = new ArrayList();
        int pendingInstalments = 1;
        try {
            for (int i = 0; i < list.size(); i++) {
                TxTransferTO objTxTransferTO = (TxTransferTO) list.get(i);
                if (objTxTransferTO.getTransType() != null && objTxTransferTO.getTransType().equalsIgnoreCase(CommonConstants.DEBIT)) {
                    debAccNo = objTxTransferTO.getActNum();
                    HashMap whereMap = new HashMap();
                    whereMap.put("ACT_NUM", debAccNo);
                    Date currntDate = (Date) currDt.clone();
                    whereMap.put("CURRDATE", currntDate);
                    whereMap.put("ACCT_NUM", debAccNo);
                    List totBalList = null;
                    if(objTxTransferTO.getProdType()!=null && objTxTransferTO.getProdType().equals("TD")){
                        totBalList = sqlMap.executeQueryForList("getBalanceTD", whereMap);
                    }else if(objTxTransferTO.getProdType()!=null && objTxTransferTO.getProdType().equals("OA")){
                         totBalList = sqlMap.executeQueryForList("getBalanceOA", whereMap);
                    }else if(objTxTransferTO.getProdType()!=null && objTxTransferTO.getProdType().equals("SA")){
                         totBalList = sqlMap.executeQueryForList("getNegativeAmtCheckForSA", whereMap);
                    }
                    HashMap totBalMap = new HashMap();
                    if (totBalList != null && totBalList.size() > 0) {
                        totBalMap = (HashMap) totBalList.get(0);
                        if (totBalMap.containsKey("TOTAL_BALANCE") && totBalMap.get("TOTAL_BALANCE") != null) {
                            totalBalAmount = CommonUtil.convertObjToDouble(totBalMap.get("TOTAL_BALANCE"));
                        }
                    }
                }
                if (objTxTransferTO.getTransType() != null && objTxTransferTO.getTransType().equalsIgnoreCase(CommonConstants.CREDIT)) {
                    String crAcNum = objTxTransferTO.getActNum();
                    if (crAcNum != null && crAcNum.length() > 0 && objTxTransferTO.getProdType() != null && objTxTransferTO.getProdType().equals("TL")) {
                        HashMap whereMap = new HashMap();
                        whereMap.put("ACT_NUM", crAcNum);
                        //Commented by nithya on 01-Aug-2024 for KD-3811
//                        Date currntDate = DateUtil.addDays(curDate, -1);
//                        whereMap.put("CURR_DATE", CommonUtil.getProperDate(currDt, currntDate));
                          whereMap.put("CURR_DATE", currDt.clone());
                        List totLoanBalList = sqlMap.executeQueryForList("getPrincipalDueDetails", whereMap);
                         List totLoanpaidList = sqlMap.executeQueryForList("getPrincipalforStanding", whereMap);
                        if (totLoanBalList != null && totLoanBalList.size() > 0) {
                            pendingInstalments = totLoanBalList.size();
                            HashMap loanMap = (HashMap) totLoanBalList.get(0);
                            if (loanMap != null && loanMap.containsKey("PRINCIPAL_AMT")) {
                                instalment_amt = CommonUtil.convertObjToDouble(loanMap.get("PRINCIPAL_AMT"));
                            }
                        }
                        if (totLoanpaidList != null && totLoanpaidList.size() > 0) {
                           HashMap loanMap = (HashMap) totLoanpaidList.get(0);
                            if (loanMap != null && loanMap.containsKey("PRINCIPLE")) {
                                paidinstalment_amt = CommonUtil.convertObjToDouble(loanMap.get("PRINCIPLE"));
                            } 
                        }
                    } else {
                        miscamt = miscamt + objTxTransferTO.getAmount();
                    }
                }
            }
            HashMap loanAmtmap = (HashMap) data.get("ALL_AMOUNT");
            double curPrinc = CommonUtil.convertObjToDouble(loanAmtmap.get("CURR_MONTH_INT"));
            double curPenal = CommonUtil.convertObjToDouble(loanAmtmap.get("PENAL_INT"));
            double totDebAmt = instalment_amt + curPrinc + curPenal + miscamt;
             if (instType != null && instType.equals("PENDING_INST_YN")) {
                inst_no = pendingInstalments;
            }
            if (inst_no == 0) {
                inst_no = 1;
            }
            int curInstNo = inst_no;
            for (int i = inst_no; i > 0; i--) {
                if (instType != null && instType.equals("PENDING_INST_YN")) {
                    totDebAmt = ((instalment_amt * inst_no) - paidinstalment_amt) + curPrinc + curPenal + miscamt;
                } else {
                    totDebAmt = ((instalment_amt * inst_no)) + curPrinc + curPenal + miscamt;
                }
                if (totalBalAmount <= totDebAmt) {
                    curInstNo = i - 1;
                    break;
                }
            }
            if (curInstNo == 0) {
                curInstNo = 1;
            }
            if (instType != null && instType.equals("PENDING_INST_YN")) {
                totDebAmt = ((instalment_amt * curInstNo) - paidinstalment_amt) + curPrinc + curPenal + miscamt;
            } else {
                totDebAmt = ((instalment_amt * curInstNo)) + curPrinc + curPenal + miscamt;
            }
            if (totDebAmt > 0 && totalBalAmount >= totDebAmt) {
                isTransaction = true;
                for (int i = 0; i < list.size(); i++) {
                    TxTransferTO objTxTransferTO = (TxTransferTO) list.get(i);
                    if (objTxTransferTO.getTransType() != null && objTxTransferTO.getTransType().equalsIgnoreCase(CommonConstants.DEBIT)) {
                        objTxTransferTO.setAmount(totDebAmt);
                        objTxTransferTO.setInpAmount(totDebAmt);
                    }
                    if (objTxTransferTO.getTransType() != null && objTxTransferTO.getTransType().equalsIgnoreCase(CommonConstants.CREDIT)) {
                        String crAcNum = objTxTransferTO.getActNum();
                        if (crAcNum != null && crAcNum.length() > 0 && objTxTransferTO.getProdType() != null && objTxTransferTO.getProdType().equals("TL")) {
                            objTxTransferTO.setAmount(totDebAmt - miscamt);
                            objTxTransferTO.setInpAmount(totDebAmt - miscamt);
                        }
                    }
                    retList.add(objTxTransferTO);
                }
            } else {
                isTransaction = false;
                retList = list;
            }

        } catch (SQLException ex) {
            Logger.getLogger(StandingInstructionDailyDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
        return retList;
    }
    public HashMap doAuthTransaction(ArrayList list, String branch ,StandingInstructionTO siTO) throws Exception {   
        TransferDAO transferDAO = new TransferDAO();
        String siCreditProdType = "";
        HashMap returnMap = new HashMap();
        HashMap whrmap = new HashMap();
        HashMap hash = null;
        
        try {
            whrmap.put("SI_ID", siTO.getSiId());
            whrmap.put("BRANCH_CODE", branch);
            // Added by nithya on 16-05-2018 for 0009100: Advance standing instruction not processing correctly
            List prodTypeLst = sqlMap.executeQueryForList("getProductTypeForSIID", whrmap);
            if(prodTypeLst != null && prodTypeLst.size() > 0){
                HashMap prodTypeMap = (HashMap)prodTypeLst.get(0);
                siCreditProdType = CommonUtil.convertObjToStr(prodTypeMap.get("PROD_TYPE"));
            }
              // Standing instruction Instalment settings--
            List lst = sqlMap.executeQueryForList("getStandingInstalmentNumber", whrmap);
            if (lst != null && lst.size() > 0) {
                hash = (HashMap) lst.get(0);
                if (hash != null && hash.containsKey("INSTALMENT_YN") && CommonUtil.convertObjToStr(hash.get("INSTALMENT_YN")).equals("Y")) {
                    int inst_no = CommonUtil.convertObjToInt(hash.get("INSTALMENT_NO"));
                    data.put("TxTransferTO", adjustTransactionAmount(list, inst_no, "INSTALMENT_YN"));   // Standing instruction Instalment settings--
                } else if (hash != null && hash.containsKey("PENDING_INST_YN") && CommonUtil.convertObjToStr(hash.get("PENDING_INST_YN")).equals("Y")) {
                    data.put("TxTransferTO", adjustTransactionAmount(list, 0, "PENDING_INST_YN"));
                } else {
                    if (siCreditProdType.equalsIgnoreCase("AD")) {    // Added by nithya on 16-05-2018 for 0009100: Advance standing instruction not processing correctly                    
                        data.put("TxTransferTO", adjustAdvanceTransactionAmount(list, 0));
                    } else {
                        // this block only for  one to one mapping ( it will transfer remaining full balance to credit account)
                        isTransaction = true;
                        data.put("TxTransferTO", list);
                    }
                }
            } else {
                if (siCreditProdType.equalsIgnoreCase("AD")) {      // Added by nithya on 16-05-2018 for 0009100: Advance standing instruction not processing correctly              
                    data.put("TxTransferTO", adjustAdvanceTransactionAmount(list,0));
                } else {
                    isTransaction = true;
                    data.put("TxTransferTO", list);
                }
            }
        }  catch (Exception e) {
            e.printStackTrace();
        }
        if (isTransaction) {
            data.put("COMMAND", CommonConstants.TOSTATUS_INSERT);
            data.put("MODE", CommonConstants.TOSTATUS_INSERT);
            data.put(CommonConstants.BRANCH_ID, branch);
            data.put(CommonConstants.SCREEN, " Standing Instruction");
            //AUTHORIZE_MAP
            if (siTO.getAutomaticPosting().equalsIgnoreCase("Y")) {
                HashMap authorizeMap = new HashMap();
                authorizeMap.put("BATCH_ID", null);
                authorizeMap.put("USER_ID", logTO.getUserId());
                authorizeMap.put(CommonConstants.AUTHORIZESTATUS, CommonConstants.STATUS_AUTHORIZED);
                data.put(CommonConstants.AUTHORIZEMAP, authorizeMap);
            }
            try {
                returnMap = transferDAO.execute(data, false);
            } catch (Exception e) {
                System.out.println("#$#$ Error :" + e);
                returnMap.put(data.get("SI_ID"), e);

            }
        }
        return returnMap;
    }
    
     public HashMap doTransactionforPendingInstallMent(ArrayList list, String branch, StandingInstructionTO siTo) throws Exception {
        TransferDAO transferDAO = new TransferDAO();      
        //HashMap returnMap = new HashMap();
        try {
            data.put("TxTransferTO", setBatchList(list));
        } catch (Exception e) {
            e.printStackTrace();
        }
        if(isTransaction){
        data.put("COMMAND", CommonConstants.TOSTATUS_INSERT);
        data.put("MODE", CommonConstants.TOSTATUS_INSERT);
        data.put(CommonConstants.BRANCH_ID, branch);
        data.put(CommonConstants.SCREEN, "Standing Instruction");
        //AUTHORIZE_MAP
        if (siTo.getAutomaticPosting().equalsIgnoreCase("Y")) {
            HashMap authorizeMap = new HashMap();
            authorizeMap.put("BATCH_ID", null);
            authorizeMap.put("USER_ID", logTO.getUserId());
            authorizeMap.put(CommonConstants.AUTHORIZESTATUS, CommonConstants.STATUS_AUTHORIZED);
            data.put(CommonConstants.AUTHORIZEMAP, authorizeMap);
        }
        try {
            transReturnMap = transferDAO.execute(data, false);
        } catch (Exception e) {
            System.out.println("#$#$ Error :" + e);
            transReturnMap.put(data.get("SI_ID"), e);
        }
        }
        return transReturnMap;
    }
    public ArrayList setBatchList(ArrayList list) {
        String debAccNo = "";
        double totalBalAmount = 0, instalment_amt = 0, shadowDebit = 0, totCharge = 0.0;
        ArrayList retList = new ArrayList();
        int pendingInstalments = 1;
        try {
            for (int i = 0; i < list.size(); i++) {
                TxTransferTO objTxTransferTO = (TxTransferTO) list.get(i);
                if (objTxTransferTO.getTransType() != null && objTxTransferTO.getTransType().equalsIgnoreCase(CommonConstants.DEBIT)) {
                    debAccNo = objTxTransferTO.getActNum();
                    HashMap whereMap = new HashMap();
                    whereMap.put("ACT_NUM", debAccNo);
                    Date currntDate = (Date) currDt.clone();
                    whereMap.put("CURRDATE", currntDate);
                    whereMap.put("ACCT_NUM", debAccNo);
                    List totBalList = null;
                    if (objTxTransferTO.getProdType() != null && objTxTransferTO.getProdType().equals("TD")) {
                        totBalList = sqlMap.executeQueryForList("getBalanceTD", whereMap);
                    } else if (objTxTransferTO.getProdType() != null && objTxTransferTO.getProdType().equals("OA")) {
                        totBalList = sqlMap.executeQueryForList("getBalanceOA", whereMap);
                    } else if (objTxTransferTO.getProdType() != null && objTxTransferTO.getProdType().equals("SA")) {
                        totBalList = sqlMap.executeQueryForList("getNegativeAmtCheckForSA", whereMap);
                    }
                    HashMap totBalMap = new HashMap();
                    if (totBalList != null && totBalList.size() > 0) {
                        totBalMap = (HashMap) totBalList.get(0);
                        if (totBalMap.containsKey("TOTAL_BALANCE") && totBalMap.get("TOTAL_BALANCE") != null) {
                            totalBalAmount = CommonUtil.convertObjToDouble(totBalMap.get("TOTAL_BALANCE"));
                            shadowDebit = CommonUtil.convertObjToDouble(totBalMap.get("SHADOW_DEBIT"));
                            if (shadowDebit < 0) {
                                shadowDebit = shadowDebit * -1;
                            }
                            totalBalAmount = totalBalAmount - shadowDebit;
                        }
                    }
                }
            }
            System.out.println(" ALL_AMOUNT : " + data.get("ALL_AMOUNT"));
            HashMap loanAmtmap = (HashMap) data.get("ALL_AMOUNT");
            double currInterest = CommonUtil.convertObjToDouble(loanAmtmap.get("CURR_MONTH_INT"));
            double currPenal = CommonUtil.convertObjToDouble(loanAmtmap.get("PENAL_INT"));
            double currPrinceple = CommonUtil.convertObjToDouble(loanAmtmap.get("CURR_MONTH_PRINCEPLE"));
            if (loanAmtmap.containsKey("TOTAL_CHARGE")) {
                totCharge = CommonUtil.convertObjToDouble(loanAmtmap.get("TOTAL_CHARGE"));
            }           
            double totDebAmt = currPrinceple + currInterest + currPenal + totCharge;
            if (totalBalAmount < totDebAmt) {
                totDebAmt = 0.0;
            }
            for (int i = 0; i < list.size(); i++) {
                TxTransferTO objTxTransferTO = (TxTransferTO) list.get(i);
                if (objTxTransferTO.getTransType() != null && objTxTransferTO.getTransType().equalsIgnoreCase(CommonConstants.DEBIT)) {
                    objTxTransferTO.setAmount(totDebAmt);
                    objTxTransferTO.setInpAmount(totDebAmt);
                }
                if (objTxTransferTO.getTransType() != null && objTxTransferTO.getTransType().equalsIgnoreCase(CommonConstants.CREDIT)) {
                    String crAcNum = objTxTransferTO.getActNum();
                    if (crAcNum != null && crAcNum.length() > 0 && objTxTransferTO.getProdType() != null && objTxTransferTO.getProdType().equals("TL")) {
                        objTxTransferTO.setAmount(totDebAmt);
                        objTxTransferTO.setInpAmount(totDebAmt);
                    }
                }
                retList.add(objTxTransferTO);
            }
            if (totDebAmt > 0) {
                isTransaction = true;
            } else {
                isTransaction = false;
            }            
        } catch (SQLException ex) {
            Logger.getLogger(StandingInstructionDailyDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
        System.out.println("@#@#@#@#@# Return List : " + retList);
        return retList;
    }
    private HashMap interestCalculationTLAD(String accountNo, String prodID,String ProdType,double debitAmount, StandingInstructionTO SiTo) {
        HashMap map = new HashMap();
        String emiInSimpleInterest = "N";
        HashMap insertPenal = new HashMap();
        double localDebitAmt=debitAmount;
        HashMap hash = null;
        try {
            map.put("ACT_NUM", accountNo);
            map.put("PROD_ID", prodID);
            List lst = null;
            if (ProdType != null && ProdType.equals("AD")) {
                lst = sqlMap.executeQueryForList("IntCalculationDetailAD", map);
            } else {
                lst = sqlMap.executeQueryForList("IntCalculationDetail", map);
            }
            if (lst != null && lst.size() > 0) {
                hash = (HashMap) lst.get(0);
                insertPenal.put("AS_CUSTOMER_COMES", hash.get("AS_CUSTOMER_COMES"));
                if (hash.get("AS_CUSTOMER_COMES") != null && hash.get("AS_CUSTOMER_COMES").equals("N")) {
                    if (!ProdType.equals("AD")) {// Added by nithya on 26-4-2019 for KD 494 - standing issue(sb to advance)
                        depValidate = false;
                    }
                    hash = new HashMap();
                    return hash;
                }
                map.put("BRANCH_ID", branchID);
                map.put(CommonConstants.BRANCH_ID, branchID);
                map.putAll(hash);
                map.put("LOAN_ACCOUNT_CLOSING", "LOAN_ACCOUNT_CLOSING");
                map.put("CURR_DATE", curDate);
                if (SiTo.getChkInstalment().equalsIgnoreCase("Y")) {
                    map.put("NO_OF_INSTALLMENT", SiTo.getNoOfInstalments());
                }
                TaskHeader header = new TaskHeader();
                header.setBranchID(_branchCode);
                // Added by nithya on 03-03-2017 for 5763 [ RBI ]
                if (CommonUtil.convertObjToStr(CommonConstants.SAL_REC_MODULE).equals("Y")) {
                    if (lastIntCalcDtForSalRecovery != null) {
                        map.put("LAST_CALC_DT_FOR_SI", lastIntCalcDtForSalRecovery);
                    }
                }
                // total charge amount required before loan calculation 
                   List chargeList = sqlMap.executeQueryForList("getChargeDetails", map);
                if (chargeList != null && chargeList.size() > 0) {
                    double totCharge = 0.0;
                    for (int i = 0; i < chargeList.size(); i++) {
                        HashMap chargeMap = (HashMap) chargeList.get(i);
                        double chargeAmt = CommonUtil.convertObjToDouble(chargeMap.get("CHARGE_AMT")).doubleValue();
                        totCharge = totCharge + chargeAmt;

                        chargeMap = null;
                    }
                    debitAmount -= totCharge;
                }
                if (SiTo.getChkPendingInstalment().equalsIgnoreCase("Y")) {
                    map.put("EMI_DEBIT_BALANCE", debitAmount);
                }
                InterestCalculationTask interestcalTask = new InterestCalculationTask(header);
                hash = interestcalTask.interestCalcTermLoanAD(map);
                if (hash == null) {
                    hash = new HashMap();
                    depValidate = false;
                } else if (hash != null && hash.size() > 0) {
                    double total_transaction_amt = 0.0;
                    // Added by nithya on 29-05-2020 for KD-380
                    if (hash.containsKey("EMI_IN_SIMPLEINTREST") && hash.get("EMI_IN_SIMPLEINTREST") != null) {
                        emiInSimpleInterest = CommonUtil.convertObjToStr(hash.get("EMI_IN_SIMPLEINTREST"));
                    }
                    // End
                    insertPenal.put("REPAYMENT_TYPE", hash.get("REPAYMENT_TYPE"));
                    if (!(hash.containsKey("REPAYMENT_TYPE") && hash.get("REPAYMENT_TYPE") != null && hash.get("REPAYMENT_TYPE").equals("EMI"))) {
                        double interest = CommonUtil.convertObjToDouble(hash.get("INTEREST")).doubleValue();
                        double penal = CommonUtil.convertObjToDouble(hash.get("LOAN_CLOSING_PENAL_INT")).doubleValue();
                        hash.put("ACT_NUM", accountNo);
                        hash.put("FROM_DT", hash.get("LAST_INT_CALC_DT"));
                        if (hash.get("FROM_DT") != null && !hash.get("FROM_DT").equals("")) {
                            hash.put("FROM_DT", DateUtil.addDays(((Date) hash.get("FROM_DT")), 2));
                        }
                        hash.put("TO_DATE", map.get("CURR_DATE"));
                        List facilitylst = sqlMap.executeQueryForList("getPaidPrinciple", hash);
                        if (facilitylst != null && facilitylst.size() > 0) {
                            hash = (HashMap) facilitylst.get(0);
                            interest -= CommonUtil.convertObjToDouble(hash.get("INTEREST")).doubleValue();
                            penal -= CommonUtil.convertObjToDouble(hash.get("PENAL")).doubleValue();
                        }
                        if (interest > 0) {
                            insertPenal.put("CURR_MONTH_INT", new Double(interest));
                        } else {
                            insertPenal.put("CURR_MONTH_INT", new Double(0));
                        }
                        if (penal > 0) {
                            insertPenal.put("PENAL_INT", new Double(penal));
                        } else {
                            insertPenal.put("PENAL_INT", new Double(0));
                        }
                    }
                    chargeList = sqlMap.executeQueryForList("getChargeDetails", map);
                    if (chargeList != null && chargeList.size() > 0) {
                          double totCharge = 0.0;
                        for (int i = 0; i < chargeList.size(); i++) {
                            HashMap chargeMap = (HashMap) chargeList.get(i);
                            double chargeAmt = CommonUtil.convertObjToDouble(chargeMap.get("CHARGE_AMT")).doubleValue();
                             totCharge = totCharge + chargeAmt;
                            if (chargeMap.get("CHARGE_TYPE").equals("POSTAGE CHARGES") && chargeAmt > 0) {
                                insertPenal.put("POSTAGE CHARGES", chargeMap.get("CHARGE_AMT"));
                            }
                            if (chargeMap.get("CHARGE_TYPE").equals("MISCELLANEOUS CHARGES") && chargeAmt > 0) {
                                insertPenal.put("MISCELLANEOUS CHARGES", chargeMap.get("CHARGE_AMT"));
                            }
                            if (chargeMap.get("CHARGE_TYPE").equals("LEGAL CHARGES") && chargeAmt > 0) {
                                insertPenal.put("LEGAL CHARGES", chargeMap.get("CHARGE_AMT"));
                            }
                            if (chargeMap.get("CHARGE_TYPE").equals("INSURANCE CHARGES") && chargeAmt > 0) {
                                insertPenal.put("INSURANCE CHARGES", chargeMap.get("CHARGE_AMT"));
                            }
                            if (chargeMap.get("CHARGE_TYPE").equals("EXECUTION DECREE CHARGES") && chargeAmt > 0) {
                                insertPenal.put("EXECUTION DECREE CHARGES", chargeMap.get("CHARGE_AMT"));
                            }
                            if (chargeMap.get("CHARGE_TYPE").equals("ARBITRARY CHARGES") && chargeAmt > 0) {
                                insertPenal.put("ARBITRARY CHARGES", chargeMap.get("CHARGE_AMT"));
                            }
                            if (chargeMap.containsKey("CHARGE_TYPE") && chargeMap.get("CHARGE_TYPE") != null && chargeMap.get("CHARGE_TYPE").equals("NOTICE CHARGES") && chargeAmt > 0) {
                                insertPenal.put("NOTICE CHARGES", chargeMap.get("CHARGE_AMT"));
                            }
                            if (chargeMap.containsKey("CHARGE_TYPE") && chargeMap.get("CHARGE_TYPE") != null && chargeMap.get("CHARGE_TYPE").equals("OTHER CHARGES") && chargeAmt > 0) {
                                insertPenal.put("OTHER CHARGES", chargeMap.get("CHARGE_AMT"));
                            }
                            if (chargeMap.containsKey("CHARGE_TYPE") && chargeMap.get("CHARGE_TYPE") != null && chargeMap.get("CHARGE_TYPE").equals("EP_COST") && chargeAmt > 0) {
                                insertPenal.put("EP_COST", chargeMap.get("CHARGE_AMT"));
                            }
                            if (chargeMap.containsKey("CHARGE_TYPE") && chargeMap.get("CHARGE_TYPE") != null && chargeMap.get("CHARGE_TYPE").equals("ARC_COST") && chargeAmt > 0) {
                                insertPenal.put("ARC_COST", chargeMap.get("CHARGE_AMT"));
                            }
//                            if (chargeMap.containsKey("CHARGE_TYPE") && chargeMap.get("CHARGE_TYPE") != null && chargeMap.get("CHARGE_TYPE").equals("EXECUTION DECREE CHARGES") && chargeAmt > 0) {
//                                insertPenal.put("EP_COST", chargeMap.get("CHARGE_AMT"));
//                            }
                            if (chargeMap.containsKey("CHARGE_TYPE") && chargeMap.get("CHARGE_TYPE").equals("RECOVERY CHARGES") && chargeAmt > 0) {
                                insertPenal.put("RECOVERY CHARGES", chargeMap.get("CHARGE_AMT"));
                            }
                            if (chargeMap.containsKey("CHARGE_TYPE") && chargeMap.get("CHARGE_TYPE").equals("MEASUREMENT CHARGES") && chargeAmt > 0) {
                                insertPenal.put("MEASUREMENT CHARGES", chargeMap.get("CHARGE_AMT"));
                            }
                            chargeMap = null;
                        }
                        if (totCharge > 0) {
                            insertPenal.put("TOTAL_CHARGE", totCharge);
                        }
                    }
                    if (hash.containsKey("REPAYMENT_TYPE") && hash.get("REPAYMENT_TYPE") != null && hash.get("REPAYMENT_TYPE").equals("EMI") && emiInSimpleInterest.equalsIgnoreCase("N")) {
                        if (hash.containsKey("NO_OF_INSTALLMENT") && hash.get("NO_OF_INSTALLMENT") != null
                                && hash.containsKey("TOTAL_DUE") && hash.get("TOTAL_DUE") != null) {
                            insertPenal.put("INTEREST", hash.get("INTEREST"));
                            insertPenal.put("NO_OF_INSTALLMENT", hash.get("NO_OF_INSTALLMENT"));
                            Double total_amount = CommonUtil.convertObjToDouble(hash.get("TOTAL_DUE")) + CommonUtil.convertObjToDouble(hash.get("LOAN_CLOSING_PENAL_INT"));
                            insertPenal.put("CURR_MONTH_PRINCEPLE", CommonUtil.convertObjToDouble(hash.get("PRINCIPAL_DUE")));
                            insertPenal.put("CURR_MONTH_INT", Math.round(CommonUtil.convertObjToDouble(hash.get("TOT_INTEREST")).doubleValue()));
                            if (hash.containsKey("LOAN_CLOSING_PENAL_INT") && hash.get("LOAN_CLOSING_PENAL_INT") != null) {
                                insertPenal.put("PENAL_INT", CommonUtil.convertObjToDouble(hash.get("LOAN_CLOSING_PENAL_INT")));
                            }
                            insertPenal.put("EMI","EMI");
                        } else {
                            depValidate = false;
                            hash = new HashMap();
                            return hash;
                        }
                        map.remove("EMI_DEBIT_BALANCE");
                    }else if (hash.containsKey("REPAYMENT_TYPE") && hash.get("REPAYMENT_TYPE") != null && hash.get("REPAYMENT_TYPE").equals("EMI") && emiInSimpleInterest.equalsIgnoreCase("Y")) {
                        double interest = CommonUtil.convertObjToDouble(hash.get("INTEREST")).doubleValue();
                        double penal = CommonUtil.convertObjToDouble(hash.get("LOAN_CLOSING_PENAL_INT")).doubleValue();
                        hash.put("ACT_NUM", accountNo);
                        insertPenal.put("CURR_MONTH_PRINCEPLE", CommonUtil.convertObjToDouble(hash.get("PRINCIPAL_DUE")));
                        hash.put("FROM_DT", hash.get("LAST_INT_CALC_DT"));
                        if (hash.get("FROM_DT") != null && !hash.get("FROM_DT").equals("")) {
                            hash.put("FROM_DT", DateUtil.addDays(((Date) hash.get("FROM_DT")), 2));
                        }
                        hash.put("TO_DATE", map.get("CURR_DATE"));
                        List facilitylst = sqlMap.executeQueryForList("getPaidPrinciple", hash);
                        if (facilitylst != null && facilitylst.size() > 0) {
                            hash = (HashMap) facilitylst.get(0);
                            interest -= CommonUtil.convertObjToDouble(hash.get("INTEREST")).doubleValue(); 
                            penal -= CommonUtil.convertObjToDouble(hash.get("PENAL")).doubleValue();
                        }
                        if (interest > 0) {
                            insertPenal.put("CURR_MONTH_INT", new Double(interest));
                        } else {
                            insertPenal.put("CURR_MONTH_INT", new Double(0));
                        }
                        if (penal > 0) {
                            insertPenal.put("PENAL_INT", new Double(penal));
                        } else {
                            insertPenal.put("PENAL_INT", new Double(0));
                        }
                        insertPenal.put("EMI_IN_SIMPLEINTREST", "Y");
                        insertPenal.put("REPAYMENT_TYPE", "EMI");                        
                        map.remove("EMI_DEBIT_BALANCE");
                    }
                    chargeList = null;
                }
                interestcalTask = null;
            }
        } catch (Exception e) {
            depValidate = false;
            e.printStackTrace();
        }
        map = null;
        hash = null;
        return insertPenal;
    }

    private TxTransferTO getDebitTransferTO(StandingInstructionDebitTO standingInstructionDebitTO, StandingInstructionTO siTO, String drBranchCode) {
        final TxTransferTO objTxTransferTO = new TxTransferTO();
        try {
            objTxTransferTO.setBatchId(null);

            HashMap prodID = new HashMap();
            HashMap actNum = new HashMap();
            prodID.put(PROD_ID, standingInstructionDebitTO.getProdId());

            if (!standingInstructionDebitTO.getProdType().equals("GL")) {
                HashMap acHeadMap = (HashMap) sqlMap.executeQueryForObject("getProdID_OP_AC_PRODUCT" + standingInstructionDebitTO.getProdType(), prodID);
                actNum.put(ACT_NUM, standingInstructionDebitTO.getAcctNo());
                // The following line by Rajesh

                objTxTransferTO.setAcHdId(CommonUtil.convertObjToStr(acHeadMap.get(AC_HD_ID)));
                objTxTransferTO.setProdId(standingInstructionDebitTO.getProdId());
                objTxTransferTO.setActNum(standingInstructionDebitTO.getAcctNo());
               // objTxTransferTO.setBranchId(drBranchCode);  // This line added & commented the following line by Rajesh
                 objTxTransferTO.setBranchId(standingInstructionDebitTO.getBranchId());
                objTxTransferTO.setInpCurr(CommonUtil.convertObjToStr(acHeadMap.get(BASE_CURRENCY)));
                acHeadMap = null;
                // The following line by Rajesh

            } else {
                objTxTransferTO.setAcHdId(standingInstructionDebitTO.getAcHdId());
                objTxTransferTO.setBranchId(_branchCode);
                objTxTransferTO.setInpCurr("INR");
            }
            objTxTransferTO.setProdType(standingInstructionDebitTO.getProdType());
            if (isVariable) {
                objTxTransferTO.setInpAmount(new Double(CommonUtil.convertObjToDouble(String.valueOf(debitAmt)).doubleValue()
                        + CommonUtil.convertObjToDouble(siTO.getSiCharges()).doubleValue()
                        + CommonUtil.convertObjToDouble(siTO.getExecCharge()).doubleValue()));
                objTxTransferTO.setAmount(new Double(CommonUtil.convertObjToDouble(String.valueOf(debitAmt)).doubleValue()
                        + CommonUtil.convertObjToDouble(siTO.getSiCharges()).doubleValue()
                        + CommonUtil.convertObjToDouble(siTO.getExecCharge()).doubleValue()));
//                System.out.println("######objTxTransfer1233333" + objTxTransferTO.getInpAmount() + "@@@####" + objTxTransferTO.getAmount());
            } else {
                objTxTransferTO.setInpAmount(new Double(CommonUtil.convertObjToDouble(standingInstructionDebitTO.getAmount()).doubleValue()
                        + CommonUtil.convertObjToDouble(siTO.getSiCharges()).doubleValue()
                        + CommonUtil.convertObjToDouble(siTO.getExecCharge()).doubleValue()));
                objTxTransferTO.setAmount(new Double(CommonUtil.convertObjToDouble(standingInstructionDebitTO.getAmount()).doubleValue()
                        + CommonUtil.convertObjToDouble(siTO.getSiCharges()).doubleValue()
                        + CommonUtil.convertObjToDouble(siTO.getExecCharge()).doubleValue()));
            }
            objTxTransferTO.setTransType(CommonConstants.DEBIT);
            objTxTransferTO.setTransDt(curDate);
            objTxTransferTO.setInstType("VOUCHER");
            objTxTransferTO.setInstrumentNo1("1");
            objTxTransferTO.setInstrumentNo2("2");
            objTxTransferTO.setInstDt(curDate);
            objTxTransferTO.setInitChannType("CASHIER");
            objTxTransferTO.setParticulars("To-SI-" + standingInstructionDebitTO.getParticulars() + " " + siTO.getSiId());
            objTxTransferTO.setTransMode("TRANSFER");
            objTxTransferTO.setStatus(CommonConstants.STATUS_CREATED);
            objTxTransferTO.setTransModType(standingInstructionDebitTO.getProdType());
            if (siTO.getAutomaticPosting().equalsIgnoreCase("Y")) {
                objTxTransferTO.setStatusBy(logTO.getUserId());
                objTxTransferTO.setInitTransId(CommonConstants.TTSYSTEM);
            } else {
                objTxTransferTO.setStatusBy(logTO.getUserId());
                objTxTransferTO.setInitTransId(String.valueOf("SITTSYSTEM"));
            }
            objTxTransferTO.setStatusDt(curDate);
            objTxTransferTO.setInitiatedBranch(_branchCode);
            objTxTransferTO.setSingleTransId(singleTransId);
            if (standingInstructionDebitTO.getProdType().equalsIgnoreCase("AD")) {
                objTxTransferTO.setAuthorizeRemarks("DP");
            }
            objTxTransferTO.setScreenName("Standing Instruction");
            objTxTransferTO.setTransAllId(siTO.getSiId());
            installment += standingInstructionDebitTO.getAmount().doubleValue();
//            System.out.println("######objTxTransferTODDDDD" + objTxTransferTO.getInitiatedBranch());
//            System.out.println("######objTxTransfer#######Debit" + objTxTransferTO);


            prodID = null;
            actNum = null;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return objTxTransferTO;
    }

    private TxTransferTO getCreditTransferTO(StandingInstructionCreditTO standingInstructionCreditTO, StandingInstructionTO siTO, String crBranchCode) {
        final TxTransferTO objTxTransferTO = new TxTransferTO();
        try {
            objTxTransferTO.setBatchId("");
            objTxTransferTO.setTransId("");
            HashMap prodID = new HashMap();
            HashMap actNum = new HashMap();
            String prod = standingInstructionCreditTO.getProdType();
            if (!standingInstructionCreditTO.getProdType().equals("GL")) {
                prodID.put(PROD_ID, standingInstructionCreditTO.getProdId());
                HashMap acHeadMap = (HashMap) sqlMap.executeQueryForObject("getProdID_OP_AC_PRODUCT" + standingInstructionCreditTO.getProdType(), prodID);
                String act = "";
                act = standingInstructionCreditTO.getAcctNo();
                if (standingInstructionCreditTO.getProdType().equals("TL")) {
                    act = standingInstructionCreditTO.getAcctNo();
                    if (act.lastIndexOf("_") != -1) {
                        act = act.substring(0, act.lastIndexOf("_"));
                    }
                }
                actNum.put(ACT_NUM, act);
                objTxTransferTO.setAcHdId(CommonUtil.convertObjToStr(acHeadMap.get(AC_HD_ID)));
                objTxTransferTO.setProdId(standingInstructionCreditTO.getProdId());
                objTxTransferTO.setActNum(standingInstructionCreditTO.getAcctNo());
                //objTxTransferTO.setBranchId(crBranchCode);  // This line added & commented the following line by Rajesh
                objTxTransferTO.setBranchId(standingInstructionCreditTO.getBranchId());
                objTxTransferTO.setInpCurr(CommonUtil.convertObjToStr(acHeadMap.get(BASE_CURRENCY)));
                acHeadMap = null;
                // The following line by Rajesh
            } else {
                objTxTransferTO.setAcHdId(standingInstructionCreditTO.getAcHdId());
                objTxTransferTO.setBranchId(_branchCode);
                objTxTransferTO.setInpCurr("INR");
            }
            objTxTransferTO.setProdType(standingInstructionCreditTO.getProdType());
            if (isVariable) {
                objTxTransferTO.setInpAmount(CommonUtil.convertObjToDouble(String.valueOf(debitAmt)) - CommonUtil.convertObjToDouble(siTO.getSiAgentComm()));
                objTxTransferTO.setAmount(CommonUtil.convertObjToDouble(String.valueOf(debitAmt)) - CommonUtil.convertObjToDouble(siTO.getSiAgentComm()));
                System.out.println("######objTxTransfer1233333" + objTxTransferTO.getInpAmount() + "@@@####" + objTxTransferTO.getAmount());
                isVariable = false;
            } else {
                objTxTransferTO.setInpAmount(standingInstructionCreditTO.getAmount() - CommonUtil.convertObjToDouble(siTO.getSiAgentComm()));
                objTxTransferTO.setAmount(standingInstructionCreditTO.getAmount() - CommonUtil.convertObjToDouble(siTO.getSiAgentComm()));
            }
            objTxTransferTO.setTransType(CommonConstants.CREDIT);
            objTxTransferTO.setTransDt(curDate);
            objTxTransferTO.setInstType(null);
            objTxTransferTO.setInstrumentNo1(null);
            objTxTransferTO.setInstrumentNo2(null);
            objTxTransferTO.setInstDt(null);
            // Added by nithya on 28-08-2017
            if (CommonConstants.SAL_REC_MODULE != null && !CommonConstants.SAL_REC_MODULE.equals("")
                && CommonConstants.SAL_REC_MODULE.equalsIgnoreCase("Y")) {         
                if (standingInstructionCreditTO.getProdType().equals("TL")) {
                    if (lastIntCalcDtForSalRecovery != null) {
                       objTxTransferTO.setInstDt(lastIntCalcDtForSalRecovery); 
                       objTxTransferTO.setInstrumentNo1("FUTURE_DT_SI_PROCESS");
                    }
                }
            }
            objTxTransferTO.setInitChannType("CASHIER");
            objTxTransferTO.setParticulars("By-SI-" + standingInstructionCreditTO.getParticulars() + " " + siTO.getSiId());
            objTxTransferTO.setTransMode("TRANSFER");
            objTxTransferTO.setTransModType(standingInstructionCreditTO.getProdType());
            objTxTransferTO.setStatus(CommonConstants.STATUS_CREATED);
            if (siTO.getAutomaticPosting().equalsIgnoreCase("Y")) {
                objTxTransferTO.setStatusBy(logTO.getUserId());
                objTxTransferTO.setInitTransId(CommonConstants.TTSYSTEM);
            } else {
                objTxTransferTO.setStatusBy(logTO.getUserId());
                objTxTransferTO.setInitTransId(String.valueOf("SITTSYSTEM"));
            }
            objTxTransferTO.setStatusDt(curDate);
            objTxTransferTO.setInitiatedBranch(_branchCode);
            //objTxTransferTO.setLinkBatchId();
            objTxTransferTO.setSingleTransId(singleTransId);
            objTxTransferTO.setScreenName("Standing Instruction");
            objTxTransferTO.setTransAllId(siTO.getSiId());
            data.put("LINK_BATCH_ID", standingInstructionCreditTO.getAcctNo());
//            System.out.println("######objTxTransferTO" + objTxTransferTO.getInitiatedBranch());
//            System.out.println("######objTxTransferTO####credit" + objTxTransferTO);
            prodID = null;
            actNum = null;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return objTxTransferTO;
    }

    private TxTransferTO getDebitSiChargesTransferTO(StandingInstructionDebitTO standingInstructionDebitTO, StandingInstructionTO siTO, String drBranchCode) {
        final TxTransferTO objTxTransferTO = new TxTransferTO();
        try {
            objTxTransferTO.setBatchId(null);

            HashMap prodID = new HashMap();
            HashMap actNum = new HashMap();
            prodID.put(PROD_ID, standingInstructionDebitTO.getProdId());

            if (!standingInstructionDebitTO.getProdType().equals("GL")) {
                HashMap acHeadMap = (HashMap) sqlMap.executeQueryForObject("getProdID_OP_AC_PRODUCT" + standingInstructionDebitTO.getProdType(), prodID);
                actNum.put(ACT_NUM, standingInstructionDebitTO.getAcctNo());
                // The following line by Rajesh
//                HashMap branchidMap = (HashMap)sqlMap.executeQueryForObject("getBranchCode_ACT_MASTER" + standingInstructionDebitTO.getProdType(), actNum);
                objTxTransferTO.setAcHdId(CommonUtil.convertObjToStr(acHeadMap.get(AC_HD_ID)));
                objTxTransferTO.setProdId(standingInstructionDebitTO.getProdId());
                objTxTransferTO.setActNum(standingInstructionDebitTO.getAcctNo());
                objTxTransferTO.setBranchId(drBranchCode);  // This line added & commented the following line by Rajesh
//                objTxTransferTO.setBranchId(CommonUtil.convertObjToStr(branchidMap.get(BRANCH_CODE)));
                objTxTransferTO.setInpCurr(CommonUtil.convertObjToStr(acHeadMap.get(BASE_CURRENCY)));
                acHeadMap = null;
                // The following line by Rajesh
//                branchidMap = null;
            } else {
                objTxTransferTO.setAcHdId(standingInstructionDebitTO.getAcHdId());
                objTxTransferTO.setBranchId(_branchCode);
                objTxTransferTO.setInpCurr("INR");
            }
            objTxTransferTO.setProdType(standingInstructionDebitTO.getProdType());
//            if(isVariable){
//                objTxTransferTO.setInpAmount(CommonUtil.convertObjToDouble(String.valueOf(debitAmt)));
//                objTxTransferTO.setAmount(CommonUtil.convertObjToDouble(String.valueOf(debitAmt)));
//                System.out.println("######objTxTransfer1233333"+objTxTransferTO.getInpAmount()+"@@@####"+objTxTransferTO.getAmount());
//            }else{
            objTxTransferTO.setInpAmount(new Double(CommonUtil.convertObjToDouble(siTO.getSiCharges()).doubleValue()
                    + CommonUtil.convertObjToDouble(siTO.getExecCharge()).doubleValue()));
//                objTxTransferTO.setAmount(standingInstructionDebitTO.getAmount());
            objTxTransferTO.setAmount(new Double(CommonUtil.convertObjToDouble(siTO.getSiCharges()).doubleValue()
                    + CommonUtil.convertObjToDouble(siTO.getExecCharge()).doubleValue()));
//            }
            objTxTransferTO.setTransType(CommonConstants.DEBIT);
            objTxTransferTO.setTransDt(curDate);
            objTxTransferTO.setInstType("VOUCHER");
            objTxTransferTO.setInstrumentNo1("1");
            objTxTransferTO.setInstrumentNo2("2");
            objTxTransferTO.setInstDt(curDate);
            objTxTransferTO.setInitChannType("CASHIER");
            objTxTransferTO.setParticulars("To-SI-SI Charges " + siTO.getSiId());
            objTxTransferTO.setTransMode("TRANSFER");
            objTxTransferTO.setTransModType(standingInstructionDebitTO.getProdType());
            objTxTransferTO.setStatus(CommonConstants.STATUS_CREATED);
            if (siTO.getAutomaticPosting().equalsIgnoreCase("Y")) {
                objTxTransferTO.setStatusBy(logTO.getUserId());
                objTxTransferTO.setInitTransId(CommonConstants.TTSYSTEM);
            } else {
                objTxTransferTO.setStatusBy(logTO.getUserId());
                objTxTransferTO.setInitTransId(String.valueOf("SITTSYSTEM"));
            }
            objTxTransferTO.setStatusDt(curDate);
            objTxTransferTO.setInitiatedBranch(super._branchCode);
            objTxTransferTO.setSingleTransId(singleTransId);
            objTxTransferTO.setScreenName("Standing Instruction");
            objTxTransferTO.setTransAllId(siTO.getSiId());
            installment += standingInstructionDebitTO.getAmount().doubleValue();
//            System.out.println("######FailureCharges" + objTxTransferTO);
            prodID = null;
            actNum = null;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return objTxTransferTO;
    }

    private TxTransferTO getAgentCommitionCreditTransferTO(StandingInstructionCreditTO standingInstructionCreditTO, StandingInstructionTO siTO, String crBranchCode,String deposit) {
        final TxTransferTO objAgentCommitionTxTransferTO = new TxTransferTO();
        try {
            objAgentCommitionTxTransferTO.setBatchId("");
            objAgentCommitionTxTransferTO.setTransId("");
            HashMap prodID = new HashMap();
            HashMap actNum = new HashMap();
            //HashMap acHeadMap = (HashMap) sqlMap.executeQueryForObject("getAgentCommisionAcHd", null); commented by sreekrishnan
            objAgentCommitionTxTransferTO.setAcHdId(siTO.getCommisionAcHd());
            objAgentCommitionTxTransferTO.setBranchId(_branchCode);
            objAgentCommitionTxTransferTO.setInpCurr("INR");
            //acHeadMap = null;
            objAgentCommitionTxTransferTO.setProdType("GL");
            if (isVariable) {
                objAgentCommitionTxTransferTO.setInpAmount(siTO.getSiAgentComm());
                objAgentCommitionTxTransferTO.setAmount(siTO.getSiAgentComm());
                isVariable = false;
            } else {
                objAgentCommitionTxTransferTO.setInpAmount(siTO.getSiAgentComm());
                objAgentCommitionTxTransferTO.setAmount(siTO.getSiAgentComm());
            }
            objAgentCommitionTxTransferTO.setTransType(CommonConstants.CREDIT);
            objAgentCommitionTxTransferTO.setTransDt(curDate);
            objAgentCommitionTxTransferTO.setInstType(null);
            objAgentCommitionTxTransferTO.setInstrumentNo1(null);
            objAgentCommitionTxTransferTO.setInstrumentNo2(null);
            objAgentCommitionTxTransferTO.setInstDt(null);
            objAgentCommitionTxTransferTO.setInitChannType("CASHIER");
            objAgentCommitionTxTransferTO.setParticulars("By-SI-SI Commission " + siTO.getSiId());
            objAgentCommitionTxTransferTO.setTransModType("GL"); // Added by nithya for 4409 on 07-05-2016
            objAgentCommitionTxTransferTO.setTransMode("TRANSFER");
            objAgentCommitionTxTransferTO.setStatus(CommonConstants.STATUS_CREATED);
            if (siTO.getAutomaticPosting().equalsIgnoreCase("Y")) {
                objAgentCommitionTxTransferTO.setStatusBy(logTO.getUserId());
                objAgentCommitionTxTransferTO.setInitTransId(CommonConstants.TTSYSTEM);
            } else {
                objAgentCommitionTxTransferTO.setStatusBy(logTO.getUserId());
                objAgentCommitionTxTransferTO.setInitTransId(String.valueOf("SITTSYSTEM"));
            }
            objAgentCommitionTxTransferTO.setStatusDt(curDate);
            objAgentCommitionTxTransferTO.setInitiatedBranch(_branchCode);
            objAgentCommitionTxTransferTO.setSingleTransId(singleTransId);
            objAgentCommitionTxTransferTO.setLinkBatchId(deposit);
            objAgentCommitionTxTransferTO.setAuthorizeRemarks("NOT_INCLUDEDINTL");
            objAgentCommitionTxTransferTO.setGlTransActNum(deposit);
            objAgentCommitionTxTransferTO.setScreenName("Standing Instruction");
            objAgentCommitionTxTransferTO.setTransAllId(siTO.getSiId()); 
           prodID = null;
            actNum = null;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return objAgentCommitionTxTransferTO;
    }

    private TxTransferTO getSiChargeCreditTransferTO(StandingInstructionCreditTO standingInstructionCreditTO, StandingInstructionTO siTO, String crBranchCode) {
        final TxTransferTO objSiChargeTxTransferTO = new TxTransferTO();
        try {
            objSiChargeTxTransferTO.setBatchId("");
            objSiChargeTxTransferTO.setTransId("");
            HashMap prodID = new HashMap();
            HashMap actNum = new HashMap();
//            HashMap acHeadMap = new HashMap();
//            prodID.put(PROD_ID, standingInstructionCreditTO.getProdId());    
//                List lst = (List)sqlMap.executeQueryForList("getSelectSIChargesHeadTO", null);
            HashMap acHeadMap = (HashMap) sqlMap.executeQueryForObject("getServiceTaxSiCharges", null);
//                if(lst != null && lst.size()>0){
//                    acHeadMap = (HashMap)lst.get(0);
//                }
//            System.out.println("#####dd#acHeadMap" + acHeadMap);
//            System.out.println("######acHeadMap" + (CommonUtil.convertObjToStr(acHeadMap.get("SI_COM_HD"))));
            objSiChargeTxTransferTO.setAcHdId(CommonUtil.convertObjToStr(acHeadMap.get("siComHd")));
          //  objSiChargeTxTransferTO.setBranchId(_branchCode);
            objSiChargeTxTransferTO.setBranchId(standingInstructionCreditTO.getBranchId());
            //objSiChargeTxTransferTO.setBranchId(_branchCode);
            objSiChargeTxTransferTO.setInpCurr("INR");
            acHeadMap = null;
            objSiChargeTxTransferTO.setProdType("GL");
            if (isVariable) {
                objSiChargeTxTransferTO.setInpAmount(siTO.getSiCharges());
                objSiChargeTxTransferTO.setAmount(siTO.getSiCharges());
//                System.out.println("######objTxTransfer1233333" + objSiChargeTxTransferTO.getInpAmount() + "@@@####" + objSiChargeTxTransferTO.getAmount());
                isVariable = false;
            } else {
                objSiChargeTxTransferTO.setInpAmount(siTO.getSiCharges());
                objSiChargeTxTransferTO.setAmount(siTO.getSiCharges());
            }
            objSiChargeTxTransferTO.setTransType(CommonConstants.CREDIT);
            objSiChargeTxTransferTO.setTransDt(curDate);
            objSiChargeTxTransferTO.setInstType(null);
            objSiChargeTxTransferTO.setInstrumentNo1(null);
            objSiChargeTxTransferTO.setInstrumentNo2(null);
            objSiChargeTxTransferTO.setInstDt(null);
            objSiChargeTxTransferTO.setInitChannType("CASHIER");
            objSiChargeTxTransferTO.setParticulars("By-SI-SI Charges " + siTO.getSiId());
            objSiChargeTxTransferTO.setTransMode("TRANSFER");
            objSiChargeTxTransferTO.setStatus(CommonConstants.STATUS_CREATED);
            objSiChargeTxTransferTO.setTransModType(TransactionFactory.GL);
            if (siTO.getAutomaticPosting().equalsIgnoreCase("Y")) {
                objSiChargeTxTransferTO.setStatusBy(logTO.getUserId());
                objSiChargeTxTransferTO.setInitTransId(CommonConstants.TTSYSTEM);
            } else {
                objSiChargeTxTransferTO.setStatusBy(logTO.getUserId());
                objSiChargeTxTransferTO.setInitTransId(String.valueOf("SITTSYSTEM"));
            }
            objSiChargeTxTransferTO.setStatusDt(curDate);
            objSiChargeTxTransferTO.setInitiatedBranch(_branchCode);
            objSiChargeTxTransferTO.setSingleTransId(singleTransId);
            objSiChargeTxTransferTO.setScreenName("Standing Instruction");
            objSiChargeTxTransferTO.setTransAllId(siTO.getSiId());
//            System.out.println("######objSiChargeTxTransferTO" + objSiChargeTxTransferTO.getInitiatedBranch());

            prodID = null;
            actNum = null;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return objSiChargeTxTransferTO;
    }

    private TxTransferTO getSiChargeSTCreditTransferTO(StandingInstructionCreditTO standingInstructionCreditTO, StandingInstructionTO siTO, String crBranchCode) {
        final TxTransferTO objSiChargeSTTxTransferTO = new TxTransferTO();
        try {
            objSiChargeSTTxTransferTO.setBatchId("");
            objSiChargeSTTxTransferTO.setTransId("");
            HashMap prodID = new HashMap();
            HashMap actNum = new HashMap();
//            prodID.put(PROD_ID, standingInstructionCreditTO.getProdId());    
//                HashMap acHeadMap = (HashMap)sqlMap.executeQueryForList("getServiceTaxSiCharges", prodID);
//            HashMap acHeadMap = new HashMap();
//            prodID.put(PROD_ID, standingInstructionCreditTO.getProdId());    
//                List lst = (List)sqlMap.executeQueryForList("getSelectSIChargesHeadTO", null);
//                if(lst != null && lst.size()>0){
//                    acHeadMap = (HashMap)lst.get(0);
//                }
            HashMap acHeadMap = (HashMap) sqlMap.executeQueryForObject("getServiceTaxSiCharges", null);
//            System.out.println("######acHeadMap" + (CommonUtil.convertObjToStr(acHeadMap.get("SERVICE_TAX_HD"))));
            objSiChargeSTTxTransferTO.setAcHdId(CommonUtil.convertObjToStr(acHeadMap.get("serviceTaxHd")));
//            System.out.println("######objSiChargeSTTxTransferTO" + objSiChargeSTTxTransferTO);
            objSiChargeSTTxTransferTO.setBranchId(_branchCode);
            objSiChargeSTTxTransferTO.setInpCurr("INR");
            acHeadMap = null;
            objSiChargeSTTxTransferTO.setProdType("GL");
            if (isVariable) {
                objSiChargeSTTxTransferTO.setInpAmount(siTO.getExecCharge());
                objSiChargeSTTxTransferTO.setAmount(siTO.getExecCharge());
//                System.out.println("######objTxTransfer1233333" + objSiChargeSTTxTransferTO.getInpAmount() + "@@@####" + objSiChargeSTTxTransferTO.getAmount());
                isVariable = false;
            } else {
                objSiChargeSTTxTransferTO.setInpAmount(siTO.getExecCharge());
                objSiChargeSTTxTransferTO.setAmount(siTO.getExecCharge());
            }
            objSiChargeSTTxTransferTO.setTransType(CommonConstants.CREDIT);
            objSiChargeSTTxTransferTO.setTransDt(curDate);
            objSiChargeSTTxTransferTO.setInstType(null);
            objSiChargeSTTxTransferTO.setInstrumentNo1(null);
            objSiChargeSTTxTransferTO.setInstrumentNo2(null);
            objSiChargeSTTxTransferTO.setInstDt(null);
            objSiChargeSTTxTransferTO.setInitChannType("CASHIER");
            objSiChargeSTTxTransferTO.setParticulars("By-SI-SI Charges ST " + siTO.getSiId());
            objSiChargeSTTxTransferTO.setTransMode("TRANSFER");
            objSiChargeSTTxTransferTO.setStatus(CommonConstants.STATUS_CREATED);
            if (siTO.getAutomaticPosting().equalsIgnoreCase("Y")) {
                objSiChargeSTTxTransferTO.setStatusBy(logTO.getUserId());
                objSiChargeSTTxTransferTO.setInitTransId(CommonConstants.TTSYSTEM);
            } else {
                objSiChargeSTTxTransferTO.setStatusBy(logTO.getUserId());
                objSiChargeSTTxTransferTO.setInitTransId(String.valueOf("SITTSYSTEM"));
            }
            objSiChargeSTTxTransferTO.setTransModType(TransactionFactory.GL);
            objSiChargeSTTxTransferTO.setStatusDt(curDate);
            objSiChargeSTTxTransferTO.setInitiatedBranch(_branchCode);
            //  objSiChargeSTTxTransferTO.setBranchId(_branchCode);
            objSiChargeSTTxTransferTO.setBranchId(standingInstructionCreditTO.getBranchId());
            objSiChargeSTTxTransferTO.setSingleTransId(singleTransId);
            objSiChargeSTTxTransferTO.setScreenName("Standing Instruction");
            objSiChargeSTTxTransferTO.setTransAllId(siTO.getSiId());
//            System.out.println("######objSiChargeSTTxTransferTO" + objSiChargeSTTxTransferTO.getInitiatedBranch());

            prodID = null;
            actNum = null;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return objSiChargeSTTxTransferTO;
    }

    public static void main(String str[]) {
        try {
            StandingInstructionDailyDAO dao = new StandingInstructionDailyDAO();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public HashMap execute(HashMap map) throws Exception {
        System.out.println("@@@@execute: " + map);
        _branchCode = (String) map.get(CommonConstants.BRANCH_ID);
        currDt = ServerUtil.getCurrentDate(_branchCode);
        lbl = "";
        lblClose = "";
        if (map.containsKey("SUSPEND")) {
            lbl = CommonUtil.convertObjToStr(map.get("SUSPEND"));
            System.out.println("@@@@lbl" + lbl);
        }
        if (map.containsKey("CLOSE")) {
            lblClose = CommonUtil.convertObjToStr(map.get("CLOSE"));
        }
        curDate = (Date) currDt.clone();
        HashMap siIdCreation = new HashMap();
        if (map.containsKey("StandingInstructionTO")) {
            objStandingInstructionTO = (StandingInstructionTO) map.get("StandingInstructionTO");
            command = objStandingInstructionTO.getCommand();
        }
        if (map.containsKey("StandingInstructionCreditTO")) {
            objStandingInstructionCreditTO = (List) map.get("StandingInstructionCreditTO");
        }
        if (map.containsKey("StandingInstructionDebitTO")) {
            objStandingInstructionDebitTO = (List) map.get("StandingInstructionDebitTO");
        }
        if (map.containsKey("EXECUTE_LIST")) {
            command = "EXECUTE_SI";
        }
        if (map.containsKey("EXECUTE_VARIABLE_LIST")) {
            command = "EXECUTE_VARIABLE_LIST";
        }

        HashMap returnMap = null;
        logDAO = new LogDAO();
        logTO = new LogTO();
        logTO.setUserId(CommonUtil.convertObjToStr(map.get(CommonConstants.USER_ID)));
        logTO.setBranchId(CommonUtil.convertObjToStr(map.get(CommonConstants.BRANCH_ID)));
        logTO.setSelectedBranchId(CommonUtil.convertObjToStr(map.get(CommonConstants.SELECTED_BRANCH_ID)));
        logTO.setIpAddr(CommonUtil.convertObjToStr(map.get(CommonConstants.IP_ADDR)));
        logTO.setModule(CommonUtil.convertObjToStr(map.get(CommonConstants.MODULE)));
        logTO.setScreen(CommonUtil.convertObjToStr(map.get(CommonConstants.SCREEN)));
//        final String command = objStandingInstructionTO.getCommand();

        // Added by nithya on 03-03-2017 for 5763 [ RBI ]
        if (CommonUtil.convertObjToStr(CommonConstants.SAL_REC_MODULE).equals("Y")) {
            if (map.containsKey("LAST_CALC_DT_FOR_SI") && map.get("LAST_CALC_DT_FOR_SI") != null) {
                lastIntCalcDtForSalRecovery = CommonUtil.getProperDate(currDt, DateUtil.getDateMMDDYYYY(CommonUtil.convertObjToStr(map.get("LAST_CALC_DT_FOR_SI"))));
            } else {
                lastIntCalcDtForSalRecovery = (Date) currDt.clone();
            }
        } else {
            lastIntCalcDtForSalRecovery = (Date) currDt.clone();
        }
        if (command.equals(CommonConstants.TOSTATUS_INSERT)) {
            insertData();
//           returnMap = new HashMap();
//           returnMap.put("SI_ID",objStandingInstructionTO.getSiId());

        } else if (command.equals(CommonConstants.TOSTATUS_UPDATE)) {
            updateData();
        } else if (command.equals(CommonConstants.TOSTATUS_DELETE)) {
            deleteData();
        } else if (command.equals("EXECUTE_SI")) {
            if (CommonUtil.convertObjToStr(map.get("TYPE_OF_SI")).equals("RECURRING_FIXED")) {//Added By Kannan AR On 05Nov2019
                siIdCreation = executeFixedRecurringSi(map);
            } else {
                siIdCreation = executeSi(map);
            }
        } else if (command.equals("EXECUTE_VARIABLE_LIST")) {
            if(CommonUtil.convertObjToStr(map.get("TYPE_OF_SI")).equals("RECURRING_VARIABLE")){
                siIdCreation = executeVaribleRecurringSi(map);      //Added By Suresh R 14-Jul-2017
            }else {
                siIdCreation = executeVaribleSi(map);
            }
        } else {
            throw new NoCommandException();
        }
        if (command.equals(CommonConstants.TOSTATUS_INSERT)) {
            siIdCreation.put(CommonConstants.TRANS_ID, objStandingInstructionTO.getSiId());
        }
        destroyObjects();
        System.out.println("@@@@@" + siIdCreation);
        return siIdCreation;

    }
    private void closeLoanAccount(String loanAccountNo, String depositAccNo,String sid,String debPrdType) throws SQLException {
        double totalBalAmount = 0.0;
        StandingInstructionTO stdTo =null;
        StandingInstructionDebitTO stdDebitTo = null;
        StandingInstructionCreditTO stdCreditTo = null;
        double totalLoanBalAmount = 0.0;
        HashMap whereMap = new HashMap();
        HashMap totBalMap = new HashMap();
        AccountTO accountTO = new AccountTO();
        accountTO.setActNum(loanAccountNo);
        accountTO.setActStatusId(CommonConstants.CLOSED);
        accountTO.setClosedDt(currDt);
        accountTO.setActStatusDt(currDt);
        Date currntDate = (Date) currDt.clone();
        whereMap.put("ACT_NUM", depositAccNo);
        whereMap.put("CURRDATE", currntDate);
        whereMap.put("ACCT_NUM", depositAccNo);
        
        List totBalList = null;
        if (debPrdType != null && debPrdType.equals("TD")) {
            totBalList = sqlMap.executeQueryForList("getBalanceTD", whereMap);
        } else if (debPrdType != null && debPrdType.equals("SA")) {
            totBalList = sqlMap.executeQueryForList("getNegativeAmtCheckForSA", whereMap);
        } else if (debPrdType != null && debPrdType.equals("OA")) {
            //totBalList = sqlMap.executeQueryForList("getBalanceOA", whereMap);// Changed map by nithya on 13-07-2020 for KD-2016
            totBalList = sqlMap.executeQueryForList("getOABalanceForStanding", whereMap);
        } else {
            totBalList = sqlMap.executeQueryForList("getBalanceTD", whereMap);
        }
        if (totBalList != null && totBalList.size() > 0) {
            totBalMap = (HashMap) totBalList.get(0);
            if (totBalMap.containsKey("TOTAL_BALANCE") && totBalMap.get("TOTAL_BALANCE") != null) {
                totalBalAmount = CommonUtil.convertObjToDouble(totBalMap.get("TOTAL_BALANCE"));
            }
            whereMap.put("ACT_NUM", loanAccountNo);
            List creditList = sqlMap.executeQueryForList("getTotalLoanBalance", whereMap);
            if (creditList != null && creditList.size() > 0) {
                totBalMap = (HashMap) creditList.get(0);
                if (totBalMap.containsKey("TOTAL_LOAN_BALANCE") && totBalMap.get("TOTAL_LOAN_BALANCE") != null) {
                    totalLoanBalAmount = CommonUtil.convertObjToDouble(totBalMap.get("TOTAL_LOAN_BALANCE"));
                }
            }
            if (totalBalAmount >= totalLoanBalAmount) {
                stdTo = new StandingInstructionTO();
                stdDebitTo = new StandingInstructionDebitTO();
                stdCreditTo = new StandingInstructionCreditTO();
                stdDebitTo.setStatus(CommonConstants.CLOSED);
                stdDebitTo.setSiId(sid);
                stdCreditTo.setStatus(CommonConstants.CLOSED);
                stdCreditTo.setSiId(sid);
                stdTo.setSiId(sid);
                stdTo.setStatus(CommonConstants.CLOSED);
                stdTo.setStatusBy(logTO.getUserId());
                stdTo.setStatusDt(curDate);
                stdTo.setBranchCode(_branchCode);
                System.out.println("accountTO@@@@@@@@2B Nidhin" + accountTO);
                sqlMap.executeUpdate("deleteStandingInstructionDebitTO", stdDebitTo);//for status updation  
                sqlMap.executeUpdate("deleteStandingInstructionCreditTO", stdCreditTo);//for status updation  
                sqlMap.executeUpdate("updateDeletedStandingInstructionTO", stdTo);//for status updation  
                sqlMap.executeUpdate("updateLoanActClosingDetail", accountTO);//for status updation  
            }

        }
    }
    public HashMap executeQuery(HashMap obj) throws Exception {
        _branchCode = (String) obj.get(CommonConstants.BRANCH_ID);
        currDt = ServerUtil.getCurrentDate(_branchCode);
        return getData(obj);
    }

    private String getSIID() throws Exception {
        try {
            final IDGenerateDAO dao = new IDGenerateDAO();
            final HashMap where = new HashMap();
            // where.put("WHERE", "STANDING_INSTRUCTION");
            where.put(CommonConstants.MAP_WHERE, "STANDING_INSTRUCTION");
            return (String) (dao.executeQuery(where)).get(CommonConstants.DATA);
        } catch (Exception e) {
            sqlMap.rollbackTransaction();
            e.printStackTrace();
            throw new TransRollbackException(e);
        }

    }

    private void destroyObjects() {
        objStandingInstructionTO = null;
        objStandingInstructionCreditTO = null;
        objStandingInstructionDebitTO = null;
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
    // Added by nithya on 16-05-2018 for 0009100: Advance standing instruction not processing correctly
    public ArrayList adjustAdvanceTransactionAmount(ArrayList list, int inst_no) {
        String debAccNo = "";
        double totalBalAmount = 0;
        ArrayList retList = new ArrayList();        
        try {
            for (int i = 0; i < list.size(); i++) {
                TxTransferTO objTxTransferTO = (TxTransferTO) list.get(i);
                System.out.println("objTxTransferTO.  :" + objTxTransferTO.getTransType());
                if (objTxTransferTO.getTransType() != null && objTxTransferTO.getTransType().equalsIgnoreCase(CommonConstants.DEBIT)) {
                    debAccNo = objTxTransferTO.getActNum();
                    System.out.println("debAccNo   :" + debAccNo + " amt :" + objTxTransferTO.getAmount());
                    HashMap whereMap = new HashMap();
                    whereMap.put("ACT_NUM", debAccNo);
                    Date currntDate = (Date) currDt.clone();
                    whereMap.put("CURRDATE", currntDate);
                     whereMap.put("ACCT_NUM", debAccNo);
                    List totBalList = null;
                    if(objTxTransferTO.getProdType()!=null && objTxTransferTO.getProdType().equals("TD")){
                        totBalList = sqlMap.executeQueryForList("getBalanceTD", whereMap);
                    }else if(objTxTransferTO.getProdType()!=null && objTxTransferTO.getProdType().equals("OA")){
                         totBalList = sqlMap.executeQueryForList("getOATotalBalanceWithoutMinBal", whereMap);
                    }else if(objTxTransferTO.getProdType()!=null && objTxTransferTO.getProdType().equals("SA")){
                         totBalList = sqlMap.executeQueryForList("getNegativeAmtCheckForSA", whereMap);
                    }
                    HashMap totBalMap = new HashMap();
                    if (totBalList != null && totBalList.size() > 0) {
                        totBalMap = (HashMap) totBalList.get(0);
                        if (totBalMap.containsKey("TOTAL_BALANCE") && totBalMap.get("TOTAL_BALANCE") != null) {
                            totalBalAmount = CommonUtil.convertObjToDouble(totBalMap.get("TOTAL_BALANCE"));
                        }
                    }
                }
            }           
            if (totalBalAmount >= 0) {
                isTransaction=true;
                for (int i = 0; i < list.size(); i++) {
                    TxTransferTO objTxTransferTO = (TxTransferTO) list.get(i);
                    if (objTxTransferTO.getTransType() != null && objTxTransferTO.getTransType().equalsIgnoreCase(CommonConstants.DEBIT)) {
                        objTxTransferTO.setAmount(totalBalAmount);
                        objTxTransferTO.setInpAmount(totalBalAmount);
                    }
                    if (objTxTransferTO.getTransType() != null && objTxTransferTO.getTransType().equalsIgnoreCase(CommonConstants.CREDIT)) {                  
                            objTxTransferTO.setAmount(totalBalAmount);
                            objTxTransferTO.setInpAmount(totalBalAmount);                        
                    }
                    retList.add(objTxTransferTO);
                }
            } else {
                isTransaction=false;
                retList = list;
            }

        } catch (SQLException ex) {
            Logger.getLogger(StandingInstructionDailyDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
        return retList;
    }
    
    private HashMap calculateServiceTax(double amount, String prodId) throws Exception {
        ServiceTaxCalculation objServiceTax;
        HashMap serviceTax_Map = new HashMap();
        HashMap prodMap = new HashMap();
        String taxApplicable = "";
        HashMap taxMap;
        double taxAmt = amount;
        prodMap.put("PROD_ID", prodId);
        List depositClosingHeadLst = sqlMap.executeQueryForList("getDepositClosingHeads", prodMap);
        if (depositClosingHeadLst != null && depositClosingHeadLst.size() > 0) {
            HashMap depositClosingHeadMap = (HashMap) depositClosingHeadLst.get(0);
            List taxSettingsList = new ArrayList();
            String achd = CommonUtil.convertObjToStr(depositClosingHeadMap.get("DELAYED_ACHD"));
            HashMap whereMap = new HashMap();
            whereMap.put("AC_HD_ID", achd);
            List temp = sqlMap.executeQueryForList("getCheckServiceTaxApplicableForShare", whereMap);
             if (temp != null && temp.size() > 0) {
                    HashMap value = (HashMap) temp.get(0);
                    if (value != null && value.containsKey("SERVICE_TAX_APPLICABLE")) {
                        taxApplicable = CommonUtil.convertObjToStr(value.get("SERVICE_TAX_APPLICABLE"));
                        if (taxApplicable != null && taxApplicable.equals("Y") && taxAmt > 0) {
                            if (value.containsKey("SERVICE_TAX_ID") && value.get("SERVICE_TAX_ID") != null && CommonUtil.convertObjToStr(value.get("SERVICE_TAX_ID")).length() > 0) {
                                taxMap = new HashMap();
                                taxMap.put("SETTINGS_ID", value.get("SERVICE_TAX_ID"));
                                taxMap.put(ServiceTaxCalculation.TOT_AMOUNT, CommonUtil.convertObjToStr(taxAmt));
                                taxSettingsList.add(taxMap);
                            }
                        }
                    }
                }
             if (taxSettingsList != null && taxSettingsList.size() > 0) {
                    HashMap ser_Tax_Val = new HashMap();
                    ser_Tax_Val.put(ServiceTaxCalculation.CURR_DT, currDt.clone());                   
                    ser_Tax_Val.put("SERVICE_TAX_DATA", taxSettingsList);
                    try {
                        objServiceTax = new ServiceTaxCalculation();                    
                        serviceTax_Map = objServiceTax.calculateServiceTax(ser_Tax_Val);
                        if (serviceTax_Map != null && serviceTax_Map.containsKey(ServiceTaxCalculation.TOT_TAX_AMT)) {
                            String amt = CommonUtil.convertObjToStr(serviceTax_Map.get(ServiceTaxCalculation.TOT_TAX_AMT));                            
                            //serviceTax_Map.put(ServiceTaxCalculation.TOT_TAX_AMT, objServiceTax.roundOffAmt(amt, "NEAREST_VALUE"));// Commented and added the code by nithya on 05-08-2019 for KD 282 - Flood cess issue Rd satnding and loan standing instruction
                            serviceTax_Map.put(ServiceTaxCalculation.TOT_TAX_AMT, amt);
                        } else {
                            serviceTax_Map = null;
                        }
                    } catch (Exception ex) {
                        ex.printStackTrace();
                    }
                }else{
                 serviceTax_Map = null;
             }
        }
        return serviceTax_Map;
    }
    
    public ServiceTaxDetailsTO setServiceTaxDetails(HashMap serviceTax_Map) {
        final ServiceTaxDetailsTO objservicetaxDetTo = new ServiceTaxDetailsTO();
        try {
            objservicetaxDetTo.setCommand("INSERT");           
            objservicetaxDetTo.setStatus(CommonConstants.STATUS_CREATED);          
            objservicetaxDetTo.setStatusBy("TTSYSTEM");
            objservicetaxDetTo.setAcct_Num(CommonUtil.convertObjToStr(serviceTax_Map.get("ACT_NUM")));
            objservicetaxDetTo.setParticulars("RD Standing");
            objservicetaxDetTo.setBranchID(_branchCode);
            objservicetaxDetTo.setTrans_type("C");
            if (serviceTax_Map != null && serviceTax_Map.containsKey("SERVICE_TAX")) {
                objservicetaxDetTo.setServiceTaxAmt(CommonUtil.convertObjToDouble(serviceTax_Map.get("SERVICE_TAX")));
            }
            if (serviceTax_Map != null && serviceTax_Map.containsKey("EDUCATION_CESS")) {
                objservicetaxDetTo.setEducationCess(CommonUtil.convertObjToDouble(serviceTax_Map.get("EDUCATION_CESS")));
            }
            if (serviceTax_Map != null && serviceTax_Map.containsKey("HIGHER_EDU_CESS")) {
                objservicetaxDetTo.setHigherCess(CommonUtil.convertObjToDouble(serviceTax_Map.get("HIGHER_EDU_CESS")));
            }
            if (serviceTax_Map != null && serviceTax_Map.containsKey(ServiceTaxCalculation.SWACHH_CESS)) {
                objservicetaxDetTo.setSwachhCess(CommonUtil.convertObjToDouble(serviceTax_Map.get(ServiceTaxCalculation.SWACHH_CESS)));
            }
            if (serviceTax_Map != null && serviceTax_Map.containsKey(ServiceTaxCalculation.KRISHIKALYAN_CESS)) {
                objservicetaxDetTo.setKrishiKalyan(CommonUtil.convertObjToDouble(serviceTax_Map.get(ServiceTaxCalculation.KRISHIKALYAN_CESS)));
            }
            if (serviceTax_Map != null && serviceTax_Map.containsKey("TOT_TAX_AMT")) {
                objservicetaxDetTo.setTotalTaxAmt(CommonUtil.convertObjToDouble(serviceTax_Map.get("TOT_TAX_AMT")));
            }
            double roudVal = objservicetaxDetTo.getTotalTaxAmt() - (objservicetaxDetTo.getServiceTaxAmt() + objservicetaxDetTo.getEducationCess() + objservicetaxDetTo.getHigherCess() + objservicetaxDetTo.getSwachhCess() + objservicetaxDetTo.getKrishiKalyan());
            ServiceTaxCalculation serviceTaxObj = new ServiceTaxCalculation();
            objservicetaxDetTo.setRoundVal(CommonUtil.convertObjToStr(serviceTaxObj.roundOffAmtForRoundVal(roudVal)));
            objservicetaxDetTo.setStatusDt(curDate);
            objservicetaxDetTo.setCreatedBy("TTSYSTEM");
            objservicetaxDetTo.setCreatedDt(curDate);

        } catch (Exception e) {
            e.printStackTrace();
        }
        return objservicetaxDetTo;

    }
    
    
    public HashMap doTransactionforEMIInSimpleInterest(ArrayList list, String branch, StandingInstructionTO siTO) throws Exception {
        System.out.println("inside doTransactionforEMIInSimpleInterest");
        TransferDAO transferDAO = new TransferDAO();
        HashMap returnMap = new HashMap();
        HashMap whrmap = new HashMap();
        HashMap hash = null;
        try {
            whrmap.put("SI_ID", siTO.getSiId());
            whrmap.put("BRANCH_CODE", branch);
            List lst = sqlMap.executeQueryForList("getStandingInstalmentNumber", whrmap);
            if (lst != null && lst.size() > 0) {
                System.out.println("inside here 1");
                hash = (HashMap) lst.get(0);
                if (hash != null && hash.containsKey("INSTALMENT_YN") && CommonUtil.convertObjToStr(hash.get("INSTALMENT_YN")).equals("Y")) {
                    System.out.println("inside here 2");
                    int inst_no = CommonUtil.convertObjToInt(hash.get("INSTALMENT_NO"));
                    data.put("TxTransferTO", adjustTransactionAmountForEMIInSimpleInterest_installment(list, inst_no, "INSTALMENT_YN"));
                } else if (hash != null && hash.containsKey("PENDING_INST_YN") && CommonUtil.convertObjToStr(hash.get("PENDING_INST_YN")).equals("Y")) {
                    System.out.println("inside here 3");
                    data.put("TxTransferTO", adjustTransactionAmountForEMIInSimpleInterest_pendinIinstallment(list) );
                } else {
                    System.out.println("inside here 4");
                    isTransaction = true;
                    data.put("TxTransferTO", list);
                }
            } else {
                System.out.println("inside here 5");
                isTransaction = true;
                data.put("TxTransferTO", list);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (isTransaction) {
            data.put("COMMAND", CommonConstants.TOSTATUS_INSERT);
            data.put("MODE", CommonConstants.TOSTATUS_INSERT);
            data.put(CommonConstants.BRANCH_ID, branch);
            data.put(CommonConstants.SCREEN, " Standing Instruction");
            if (siTO.getAutomaticPosting().equalsIgnoreCase("Y")) {
                HashMap authorizeMap = new HashMap();
                authorizeMap.put("BATCH_ID", null);
                authorizeMap.put("USER_ID", logTO.getUserId());
                authorizeMap.put(CommonConstants.AUTHORIZESTATUS, CommonConstants.STATUS_AUTHORIZED);
                data.put(CommonConstants.AUTHORIZEMAP, authorizeMap);
            }
            try {
                returnMap = transferDAO.execute(data, false);
            } catch (Exception e) {
                System.out.println("#$#$ Error :" + e);
                returnMap.put(data.get("SI_ID"), e);

            }
        }
        return returnMap;
    }
    
    public ArrayList adjustTransactionAmountForEMIInSimpleInterest_installment(ArrayList list, int inst_no,String instType) {
        System.out.println("Inside adjustTransactionAmountForEMIInSimpleInterest");
        String debAccNo = "";
        double totalBalAmount = 0, instalment_amt = 0, miscamt = 0,paidinstalment_amt =0;
        ArrayList retList = new ArrayList();
        int pendingInstalments = 1;
        try {
            for (int i = 0; i < list.size(); i++) {
                TxTransferTO objTxTransferTO = (TxTransferTO) list.get(i);
                if (objTxTransferTO.getTransType() != null && objTxTransferTO.getTransType().equalsIgnoreCase(CommonConstants.DEBIT)) {
                    debAccNo = objTxTransferTO.getActNum();
                    HashMap whereMap = new HashMap();
                    whereMap.put("ACT_NUM", debAccNo);
                    Date currntDate = (Date) currDt.clone();
                    whereMap.put("CURRDATE", currntDate);
                    whereMap.put("ACCT_NUM", debAccNo);
                    List totBalList = null;
                    if(objTxTransferTO.getProdType()!=null && objTxTransferTO.getProdType().equals("TD")){
                        totBalList = sqlMap.executeQueryForList("getBalanceTD", whereMap);
                    }else if(objTxTransferTO.getProdType()!=null && objTxTransferTO.getProdType().equals("OA")){
                         totBalList = sqlMap.executeQueryForList("getBalanceOA", whereMap);
                    }else if(objTxTransferTO.getProdType()!=null && objTxTransferTO.getProdType().equals("SA")){
                         totBalList = sqlMap.executeQueryForList("getNegativeAmtCheckForSA", whereMap);
                    }
                    HashMap totBalMap = new HashMap();
                    if (totBalList != null && totBalList.size() > 0) {
                        totBalMap = (HashMap) totBalList.get(0);
                        if (totBalMap.containsKey("TOTAL_BALANCE") && totBalMap.get("TOTAL_BALANCE") != null) {
                            totalBalAmount = CommonUtil.convertObjToDouble(totBalMap.get("TOTAL_BALANCE"));
                        }
                    }
                }
                if (objTxTransferTO.getTransType() != null && objTxTransferTO.getTransType().equalsIgnoreCase(CommonConstants.CREDIT)) {
                    String crAcNum = objTxTransferTO.getActNum();
                    if (crAcNum != null && crAcNum.length() > 0 && objTxTransferTO.getProdType() != null && objTxTransferTO.getProdType().equals("TL")) {
                        HashMap whereMap = new HashMap();
                        whereMap.put("ACT_NUM", crAcNum);
                        whereMap.put("CURR_DATE", currDt.clone());
                        List totLoanBalList = sqlMap.executeQueryForList("getPrincipalDueDetails", whereMap);
                         List totLoanpaidList = sqlMap.executeQueryForList("getPrincipalforStanding", whereMap);
                        if (totLoanBalList != null && totLoanBalList.size() > 0) {
                            pendingInstalments = totLoanBalList.size();
                            HashMap loanMap = (HashMap) totLoanBalList.get(0);
                            if (loanMap != null && loanMap.containsKey("PRINCIPAL_AMT")) {
                                instalment_amt = CommonUtil.convertObjToDouble(loanMap.get("PRINCIPAL_AMT"));
                            }
                        }
                        if (totLoanpaidList != null && totLoanpaidList.size() > 0) {
                           HashMap loanMap = (HashMap) totLoanpaidList.get(0);
                            if (loanMap != null && loanMap.containsKey("PRINCIPLE")) {
                                paidinstalment_amt = CommonUtil.convertObjToDouble(loanMap.get("PRINCIPLE"));
                            } 
                        }
                    } else {
                        miscamt = miscamt + objTxTransferTO.getAmount();
                    }
                }
            }
            HashMap loanAmtmap = (HashMap) data.get("ALL_AMOUNT");
            double curPrinc = CommonUtil.convertObjToDouble(loanAmtmap.get("CURR_MONTH_INT"));
            double curPenal = CommonUtil.convertObjToDouble(loanAmtmap.get("PENAL_INT"));
            double totDebAmt = instalment_amt + curPrinc + curPenal + miscamt;            
            if (inst_no == 0) {
                inst_no = 1;
            }
            int curInstNo = inst_no;
            for (int i = inst_no; i > 0; i--) {               
                    totDebAmt = ((instalment_amt * inst_no)) + curPrinc + curPenal + miscamt;                
                if (totalBalAmount <= totDebAmt) {
                    curInstNo = i - 1;
                    break;
                }
            }
            if (curInstNo == 0) {
                curInstNo = 1;
            }            
            totDebAmt = ((instalment_amt * curInstNo)) + curPrinc + curPenal + miscamt;            
            if (totDebAmt > 0 && totalBalAmount >= totDebAmt) {
                isTransaction = true;
                for (int i = 0; i < list.size(); i++) {
                    TxTransferTO objTxTransferTO = (TxTransferTO) list.get(i);
                    if (objTxTransferTO.getTransType() != null && objTxTransferTO.getTransType().equalsIgnoreCase(CommonConstants.DEBIT)) {
                        objTxTransferTO.setAmount(totDebAmt);
                        objTxTransferTO.setInpAmount(totDebAmt);
                    }
                    if (objTxTransferTO.getTransType() != null && objTxTransferTO.getTransType().equalsIgnoreCase(CommonConstants.CREDIT)) {
                        String crAcNum = objTxTransferTO.getActNum();
                        if (crAcNum != null && crAcNum.length() > 0 && objTxTransferTO.getProdType() != null && objTxTransferTO.getProdType().equals("TL")) {
                            objTxTransferTO.setAmount(totDebAmt - miscamt);
                            objTxTransferTO.setInpAmount(totDebAmt - miscamt);
                        }
                    }
                    retList.add(objTxTransferTO);
                }
            } else {
                isTransaction = false;
                retList = list;
            }

        } catch (SQLException ex) {
            Logger.getLogger(StandingInstructionDailyDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
        return retList;
    }
    
    public ArrayList adjustTransactionAmountForEMIInSimpleInterest_pendinIinstallment(ArrayList list) {
        String debAccNo = "";
        double totalBalAmount = 0, instalment_amt = 0, shadowDebit = 0, totCharge = 0.0;
        ArrayList retList = new ArrayList();
        int pendingInstalments = 1;
        try {
            for (int i = 0; i < list.size(); i++) {
                TxTransferTO objTxTransferTO = (TxTransferTO) list.get(i);
                if (objTxTransferTO.getTransType() != null && objTxTransferTO.getTransType().equalsIgnoreCase(CommonConstants.DEBIT)) {
                    debAccNo = objTxTransferTO.getActNum();
                    HashMap whereMap = new HashMap();
                    whereMap.put("ACT_NUM", debAccNo);
                    Date currntDate = (Date) currDt.clone();
                    whereMap.put("CURRDATE", currntDate);
                    whereMap.put("ACCT_NUM", debAccNo);
                    List totBalList = null;
                    if (objTxTransferTO.getProdType() != null && objTxTransferTO.getProdType().equals("TD")) {
                        totBalList = sqlMap.executeQueryForList("getBalanceTD", whereMap);
                    } else if (objTxTransferTO.getProdType() != null && objTxTransferTO.getProdType().equals("OA")) {
                        totBalList = sqlMap.executeQueryForList("getBalanceOA", whereMap);
                    } else if (objTxTransferTO.getProdType() != null && objTxTransferTO.getProdType().equals("SA")) {
                        totBalList = sqlMap.executeQueryForList("getNegativeAmtCheckForSA", whereMap);
                    }
                    HashMap totBalMap = new HashMap();
                    if (totBalList != null && totBalList.size() > 0) {
                        totBalMap = (HashMap) totBalList.get(0);
                        if (totBalMap.containsKey("TOTAL_BALANCE") && totBalMap.get("TOTAL_BALANCE") != null) {
                            totalBalAmount = CommonUtil.convertObjToDouble(totBalMap.get("TOTAL_BALANCE"));
                            shadowDebit = CommonUtil.convertObjToDouble(totBalMap.get("SHADOW_DEBIT"));
                            if (shadowDebit < 0) {
                                shadowDebit = shadowDebit * -1;
                            }
                            totalBalAmount = totalBalAmount - shadowDebit;
                        }
                    }
                }
            }
            //System.out.println(" ALL_AMOUNT : " + data.get("ALL_AMOUNT"));
            HashMap loanAmtmap = (HashMap) data.get("ALL_AMOUNT");
            double currInterest = CommonUtil.convertObjToDouble(loanAmtmap.get("CURR_MONTH_INT"));
            double currPenal = CommonUtil.convertObjToDouble(loanAmtmap.get("PENAL_INT"));
            double currPrinceple = CommonUtil.convertObjToDouble(loanAmtmap.get("CURR_MONTH_PRINCEPLE"));
            if (loanAmtmap.containsKey("TOTAL_CHARGE")) {
                totCharge = CommonUtil.convertObjToDouble(loanAmtmap.get("TOTAL_CHARGE"));
            }           
            double totDebAmt = currPrinceple + currInterest + currPenal + totCharge;
            if (totalBalAmount < totDebAmt) {
                totDebAmt = totalBalAmount;
            }
            for (int i = 0; i < list.size(); i++) {
                TxTransferTO objTxTransferTO = (TxTransferTO) list.get(i);
                if (objTxTransferTO.getTransType() != null && objTxTransferTO.getTransType().equalsIgnoreCase(CommonConstants.DEBIT)) {
                    objTxTransferTO.setAmount(totDebAmt);
                    objTxTransferTO.setInpAmount(totDebAmt);
                }
                if (objTxTransferTO.getTransType() != null && objTxTransferTO.getTransType().equalsIgnoreCase(CommonConstants.CREDIT)) {
                    String crAcNum = objTxTransferTO.getActNum();
                    if (crAcNum != null && crAcNum.length() > 0 && objTxTransferTO.getProdType() != null && objTxTransferTO.getProdType().equals("TL")) {
                        objTxTransferTO.setAmount(totDebAmt);
                        objTxTransferTO.setInpAmount(totDebAmt);
                    }
                }
                retList.add(objTxTransferTO);
            }
            if (totDebAmt > 0) {
                isTransaction = true;
            } else {
                isTransaction = false;
            }            
        } catch (SQLException ex) {
            Logger.getLogger(StandingInstructionDailyDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
        //System.out.println("@#@#@#@#@# Return List : " + retList);
        return retList;
    }
    
    
}
