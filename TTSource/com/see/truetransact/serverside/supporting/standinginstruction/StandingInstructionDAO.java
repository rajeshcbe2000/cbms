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
package com.see.truetransact.serverside.supporting.standinginstruction;

import java.util.HashMap;
import java.util.List;
import java.util.ArrayList;
import java.util.LinkedHashMap;


import com.ibatis.db.sqlmap.SqlMap;
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
import com.see.truetransact.serverside.common.transaction.TransferTrans;
import com.see.truetransact.serverside.remittance.RemittanceIssueDAO;
import com.see.truetransact.transferobject.remittance.RemittanceIssueTO;
import com.see.truetransact.transferobject.common.transaction.TransactionTO;

/**
 * StandingInstruction DAO.
 *
 */
public class StandingInstructionDAO extends TTDAO {

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

    /**
     * Creates a new instance of StandingInstructionDAO
     */
    public StandingInstructionDAO() throws ServiceLocatorException {
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

        where.put("ASONDATE", curDate.clone()); // Added by nithya on 03-03-2017 for 5763 [ RBI ]
        list = (List) sqlMap.executeQueryForList("getSelectStandingInstructionCreditTO", where);
        returnMap.put("StandingInstructionCreditTO", list);

        list = (List) sqlMap.executeQueryForList("getSelectStandingInstructionDebitTO", where);
        returnMap.put("StandingInstructionDebitTO", list);
        
        list = (List)sqlMap.executeQueryForList("getSelectStandingDebitMasterDetails", where);
        returnMap.put("SI_DEBIT_MASTER",list);
        
        list = (List)sqlMap.executeQueryForList("getSelectStandingCreditMasterDetails", where);
        returnMap.put("SI_CREDIT_MASTER",list);
        
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
        try {
            sqlMap.startTransaction();
            StandingInstructionTO siTO;
            ArrayList batchList = new ArrayList();
            List siLst = (List) map.get("EXECUTE_LIST");
            System.out.println("@@#@#@#@#siLst" + siLst);
            if (siLst != null && siLst.size() > 0) {
                for (int i = 0; i < siLst.size(); i++) {
                    HashMap siIdMap = (HashMap) siLst.get(i);
                    List list = sqlMap.executeQueryForList("standingBatchRunManual", siIdMap);
                    System.out.println("inside ####### runBatch List :" + list);
                    if (list != null && list.size() > 0) {                                                //IF STANDING INSTRUCTION IS FIXED
                        for (int j = 0; j < list.size(); j++) {
                            siTO = (StandingInstructionTO) list.get(j);
                            HashMap siId = new HashMap();
                            siId.put("SAL_REC_MODULE", CommonUtil.convertObjToStr(CommonConstants.SAL_REC_MODULE));
                            siId.put("SI_ID", siTO.getSiId());
                            siId.put("ASONDATE", curDate.clone()); // Added by nithya on 03-03-2017 for 5763 [ RBI ]
                            List creditList = sqlMap.executeQueryForList("getSelectStandingInstructionCreditTO", siId);
                            List debitList = sqlMap.executeQueryForList("getSelectStandingInstructionDebitTO", siId);
                            if (debitList != null && debitList.size() > 0) {
                                for (int x = 0, y = debitList.size(); x < y; x++) {
                                    HashMap where = new HashMap();
                                    where.put("PROD_TYPE", ((StandingInstructionCreditTO) creditList.get(x)).getProdType());
                                    where.put("ACT_NUM", ((StandingInstructionDebitTO) debitList.get(x)).getAcctNo());
                                    if (!(where.get("PROD_TYPE").equals("RM"))) {
                                        StandingInstructionDebitTO standingInstructionDebitTO = (StandingInstructionDebitTO) debitList.get(x);
                                        if (!standingInstructionDebitTO.getProdType().equals("GL")) {
                                            HashMap branchidMap = (HashMap) sqlMap.executeQueryForObject("getBranchCode_ACT_MASTER"
                                                    + standingInstructionDebitTO.getProdType(), where);
//                            if (branchList!=null && branchList.size()>0) {
//                                for (int b=0;b<branchList.size();b++) {
//                                    HashMap branchMap = (HashMap) branchList.get(b);
                                            String branch = _branchCode;
//                                    if(CommonUtil.convertObjToStr(branchidMap.get(BRANCH_CODE)).equals(branch)) {
                                            batchList.add(getDebitTransferTO(standingInstructionDebitTO, (StandingInstructionTO) siTO, branch));
//                                    }
//                                    branchMap = null;
//                                }
//                            }
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
                                    if (!(where.get("PROD_TYPE").equals("RM"))) {
                                        String mapName = "getDetailsSIAcNumCredit" + ((StandingInstructionCreditTO) creditList.get(x)).getProdType();

                                        StandingInstructionCreditTO standingInstructionCreditTO = (StandingInstructionCreditTO) creditList.get(x);
                                        if (!standingInstructionCreditTO.getProdType().equals("GL")) {
                                            HashMap branchidMap = (HashMap) sqlMap.executeQueryForObject("getBranchCode_ACT_MASTER"
                                                    + standingInstructionCreditTO.getProdType(), where);
//                                    if (branchList!=null && branchList.size()>0) {
//                                        for (int b=0;b<branchList.size();b++) {
//                                            HashMap branchMap = (HashMap) branchList.get(b);
                                            String branch = _branchCode;
//                                            if(CommonUtil.convertObjToStr(branchidMap.get(BRANCH_CODE)).equals(branch)) {
                                            batchList.add(getCreditTransferTO(standingInstructionCreditTO, (StandingInstructionTO) siTO, branch));
                                            if (CommonUtil.convertObjToDouble(siTO.getSiCharges()).doubleValue() > 0) {
                                                batchList.add(getSiChargeCreditTransferTO(standingInstructionCreditTO, (StandingInstructionTO) siTO, branch));
                                            }
                                            if (CommonUtil.convertObjToDouble(siTO.getExecCharge()).doubleValue() > 0) {
                                                batchList.add(getSiChargeSTCreditTransferTO(standingInstructionCreditTO, (StandingInstructionTO) siTO, branch));
                                            }
//                                            }
//                                            branchMap = null;
//                                        }
//                                    }
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
                            System.out.println("@@#@#@#@#depValidate" + depValidate);
                            if (depValidate) {
                                try {
//                         sqlMap.startTransaction();
                                    if (!prodType.equalsIgnoreCase("RM")) {
                                        TransferTrans trans = new TransferTrans();
                                        trans.setInitiatedBranch(_branchCode);
                                        trans.setLoanDebitInt("DP");
                                        if (siTO.getAutomaticPosting().equalsIgnoreCase("Y")) {
                                            trans.doDebitCredit(batchList, _branchCode);
                                        } else {
                                            trans.doDebitCredit(batchList, _branchCode, false);
                                        }
                                        trans.setLoanDebitInt("");
                                    } else {
                                        System.out.println("@@#depValidate" + depValidate);
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
                                                    System.out.println("inside Rm");
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
//                                remt.setTotalAmt(standingInstructionDebitTO.getAmount());
//                                remt.setExchange(new Double(0.0));
//                                remt.setPostage(new Double(0.0));
//                                remt.setOtherCharges(new Double(0.0));
                                                    remt.setExchange(CommonUtil.convertObjToDouble(siTO.getRemitCharges()));
                                                    remt.setPostage(CommonUtil.convertObjToDouble(siTO.getAcceptanceCharge()));
                                                    remt.setOtherCharges(CommonUtil.convertObjToDouble(siTO.getServiceTax()));
                                                    remt.setTotalAmt(new Double(CommonUtil.convertObjToDouble(standingInstructionDebitTO.getAmount()).doubleValue()
                                                            + CommonUtil.convertObjToDouble(siTO.getRemitCharges()).doubleValue()
                                                            + CommonUtil.convertObjToDouble(siTO.getAcceptanceCharge()).doubleValue()
                                                            + CommonUtil.convertObjToDouble(siTO.getServiceTax()).doubleValue()));
                                                    System.out.println("remt%%%%" + remt);
                                                    remt.setStatusDt(todayDt);
                                                    remt.setStatusBy("TTSYSTEM");
                                                    notDelRemMap.put(String.valueOf(1), remt);
                                                    remMap.put("NOT_DELETED_ISSUE_TOs", notDelRemMap);
                                                    dtatMap.put("RemittanceIssueTO", remMap);
                                                    System.out.println("dtatMap$$$$$$$$$$$$$$$" + dtatMap);
                                                    RemittanceIssueDAO remDao = new RemittanceIssueDAO();
                                                    HashMap resulMap = new HashMap();
                                                    remDao.setFromotherDAo(false);
                                                    resulMap = remDao.execute(dtatMap);
                                                    System.out.println("resulMap$$$$$$$$$$$$$$$" + resulMap);
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
//                                        if (branchList!=null && branchList.size()>0) {
//                                            for (int b=0;b<branchList.size();b++) {
//                                                HashMap branchMap = (HashMap) branchList.get(b);
                                                        String branch = _branchCode;
//                                                if(CommonUtil.convertObjToStr(branchidMap.get(BRANCH_CODE)).equals(branch)) {
                                                        batchList.add(getDebitSiChargesTransferTO(standingInstructionDebitTO, (StandingInstructionTO) siTO, branch));
//                                                }
//                                                branchMap = null;
//                                            }
//                                        }
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
//                            if(!(where.get("PROD_TYPE").equals("RM"))){
//                                String mapName = "getDetailsSIAcNumCredit"+((StandingInstructionCreditTO) creditList.get(x)).getProdType();

                                                StandingInstructionCreditTO standingInstructionCreditTO = (StandingInstructionCreditTO) creditList.get(x);
//                                if(!standingInstructionCreditTO.getProdType().equals("GL")){
//                                    HashMap branchidMap = (HashMap)sqlMap.executeQueryForObject("getBranchCode_ACT_MASTER" + 
//                                        standingInstructionCreditTO.getProdType(), where);
//                                    if (branchList!=null && branchList.size()>0) {
//                                        for (int b=0;b<branchList.size();b++) {
//                                            HashMap branchMap = (HashMap) branchList.get(b);
                                                String branch = _branchCode;
//                                            if(CommonUtil.convertObjToStr(branchidMap.get(BRANCH_CODE)).equals(branch)) {
//                                                batchList.add(getCreditTransferTO(standingInstructionCreditTO,(StandingInstructionTO) siTO, branch));
                                                if (CommonUtil.convertObjToDouble(siTO.getSiCharges()).doubleValue() > 0) {
                                                    batchList.add(getSiChargeCreditTransferTO(standingInstructionCreditTO, (StandingInstructionTO) siTO, branch));
                                                }
                                                if (CommonUtil.convertObjToDouble(siTO.getExecCharge()).doubleValue() > 0) {
                                                    batchList.add(getSiChargeSTCreditTransferTO(standingInstructionCreditTO, (StandingInstructionTO) siTO, branch));
                                                }
//                                            }
//                                            branchMap = null;
//                                        }
//                                    }
//                                    branchidMap = null;
//                                }
//                            }else{
//                               
//               
//                            }
                                            }
                                        }
//                            Date startDate;
//                            double count = siTO.getCount().doubleValue();
//                            double fwdCount = siTO.getCarriedForwardCount().doubleValue();
//                            System.out.println("count%%%"+count);
//                            System.out.println("trying insert second time");
//                            sqlMap.startTransaction();
                                        TransferTrans trans = new TransferTrans();
                                        trans.setInitiatedBranch(_branchCode);
                                        trans.setLoanDebitInt("DP");
                                        if (siTO.getAutomaticPosting().equalsIgnoreCase("Y")) {
                                            trans.doDebitCredit(batchList, _branchCode);
                                        } else {
                                            trans.doDebitCredit(batchList, _branchCode, false);
                                        }
                                        trans.setLoanDebitInt("");
//                        sqlMap.commitTransaction();

                                    }
                                    paramMap = new HashMap();
                                    paramMap.put("SI_ID", siTO.getSiId());
                                    paramMap.put("EXEC_DT", curDate);
                                    sqlMap.executeUpdate("updateStandingInstrManual", paramMap);
                                    sqlMap.executeUpdate("updateStandingInstrBatchManual", paramMap);                                    
            						sqlMap.commitTransaction();
//                        paramMap=null;
//                        paramMap.put("SUCESS", siTO.getSiId());
                                } catch (Exception transError) {
//                        System.out.println("insert failed, first attempt");
//                        transStatus = false ;
                                    sqlMap.rollbackTransaction();
                                    transError.printStackTrace();
                                    throw new TransRollbackException(transError);
//                        System.out.println("Error in transaction 1 ....");
                                }
                            }
                        }
                    }
                }
            }
        } catch (Exception e) {
            sqlMap.rollbackTransaction();
            e.printStackTrace();
            throw new TransRollbackException(e);
        }
        return paramMap;
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
                objTxTransferTO.setBranchId(drBranchCode);  // This line added & commented the following line by Rajesh

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
                System.out.println("######objTxTransfer1233333" + objTxTransferTO.getInpAmount() + "@@@####" + objTxTransferTO.getAmount());
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
            if (siTO.getAutomaticPosting().equalsIgnoreCase("Y")) {
                objTxTransferTO.setStatusBy(CommonConstants.TTSYSTEM);
                objTxTransferTO.setInitTransId(CommonConstants.TTSYSTEM);
            } else {
                objTxTransferTO.setStatusBy(String.valueOf("SITTSYSTEM"));
                objTxTransferTO.setInitTransId(String.valueOf("SITTSYSTEM"));
            }
            objTxTransferTO.setStatusDt(curDate);
            objTxTransferTO.setInitiatedBranch(_branchCode);
            installment += standingInstructionDebitTO.getAmount().doubleValue();
            System.out.println("######objTxTransferTODDDDD" + objTxTransferTO.getInitiatedBranch());


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
                objTxTransferTO.setBranchId(crBranchCode);  // This line added & commented the following line by Rajesh

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
                objTxTransferTO.setInpAmount(CommonUtil.convertObjToDouble(String.valueOf(debitAmt)));
                objTxTransferTO.setAmount(CommonUtil.convertObjToDouble(String.valueOf(debitAmt)));
                System.out.println("######objTxTransfer1233333" + objTxTransferTO.getInpAmount() + "@@@####" + objTxTransferTO.getAmount());
                isVariable = false;
            } else {
                objTxTransferTO.setInpAmount(standingInstructionCreditTO.getAmount());
                objTxTransferTO.setAmount(standingInstructionCreditTO.getAmount());
            }
            objTxTransferTO.setTransType(CommonConstants.CREDIT);
            objTxTransferTO.setTransDt(curDate);
            objTxTransferTO.setInstType(null);
            objTxTransferTO.setInstrumentNo1(null);
            objTxTransferTO.setInstrumentNo2(null);
            objTxTransferTO.setInstDt(null);
            objTxTransferTO.setInitChannType("CASHIER");
            objTxTransferTO.setParticulars("By-SI-" + standingInstructionCreditTO.getParticulars() + " " + siTO.getSiId());
            objTxTransferTO.setTransMode("TRANSFER");
            objTxTransferTO.setStatus(CommonConstants.STATUS_CREATED);
            if (siTO.getAutomaticPosting().equalsIgnoreCase("Y")) {
                objTxTransferTO.setStatusBy(CommonConstants.TTSYSTEM);
                objTxTransferTO.setInitTransId(CommonConstants.TTSYSTEM);
            } else {
                objTxTransferTO.setStatusBy(String.valueOf("SITTSYSTEM"));
                objTxTransferTO.setInitTransId(String.valueOf("SITTSYSTEM"));
            }
            objTxTransferTO.setStatusDt(curDate);
            objTxTransferTO.setInitiatedBranch(_branchCode);
            objTxTransferTO.setBatchId(_branchCode);
            System.out.println("######objTxTransferTO" + objTxTransferTO.getInitiatedBranch());

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
            objTxTransferTO.setStatus(CommonConstants.STATUS_CREATED);
            if (siTO.getAutomaticPosting().equalsIgnoreCase("Y")) {
                objTxTransferTO.setStatusBy(CommonConstants.TTSYSTEM);
                objTxTransferTO.setInitTransId(CommonConstants.TTSYSTEM);
            } else {
                objTxTransferTO.setStatusBy(String.valueOf("SITTSYSTEM"));
                objTxTransferTO.setInitTransId(String.valueOf("SITTSYSTEM"));
            }
            objTxTransferTO.setStatusDt(curDate);
            objTxTransferTO.setInitiatedBranch(super._branchCode);
            installment += standingInstructionDebitTO.getAmount().doubleValue();
            System.out.println("######FailureCharges" + objTxTransferTO);


            prodID = null;
            actNum = null;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return objTxTransferTO;
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
            System.out.println("#####dd#acHeadMap" + acHeadMap);
            System.out.println("######acHeadMap" + (CommonUtil.convertObjToStr(acHeadMap.get("SI_COM_HD"))));
            objSiChargeTxTransferTO.setAcHdId(CommonUtil.convertObjToStr(acHeadMap.get("siComHd")));
            objSiChargeTxTransferTO.setBranchId(_branchCode);
            objSiChargeTxTransferTO.setInpCurr("INR");
            acHeadMap = null;
            objSiChargeTxTransferTO.setProdType("GL");
            if (isVariable) {
                objSiChargeTxTransferTO.setInpAmount(siTO.getSiCharges());
                objSiChargeTxTransferTO.setAmount(siTO.getSiCharges());
                System.out.println("######objTxTransfer1233333" + objSiChargeTxTransferTO.getInpAmount() + "@@@####" + objSiChargeTxTransferTO.getAmount());
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
            if (siTO.getAutomaticPosting().equalsIgnoreCase("Y")) {
                objSiChargeTxTransferTO.setStatusBy(CommonConstants.TTSYSTEM);
                objSiChargeTxTransferTO.setInitTransId(CommonConstants.TTSYSTEM);
            } else {
                objSiChargeTxTransferTO.setStatusBy(String.valueOf("SITTSYSTEM"));
                objSiChargeTxTransferTO.setInitTransId(String.valueOf("SITTSYSTEM"));
            }
            objSiChargeTxTransferTO.setStatusDt(curDate);
            objSiChargeTxTransferTO.setInitiatedBranch(_branchCode);
            objSiChargeTxTransferTO.setBatchId(_branchCode);
            System.out.println("######objSiChargeTxTransferTO" + objSiChargeTxTransferTO.getInitiatedBranch());

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
            System.out.println("######acHeadMap" + (CommonUtil.convertObjToStr(acHeadMap.get("SERVICE_TAX_HD"))));
            objSiChargeSTTxTransferTO.setAcHdId(CommonUtil.convertObjToStr(acHeadMap.get("serviceTaxHd")));
            System.out.println("######objSiChargeSTTxTransferTO" + objSiChargeSTTxTransferTO);
            objSiChargeSTTxTransferTO.setBranchId(_branchCode);
            objSiChargeSTTxTransferTO.setInpCurr("INR");
            acHeadMap = null;
            objSiChargeSTTxTransferTO.setProdType("GL");
            if (isVariable) {
                objSiChargeSTTxTransferTO.setInpAmount(siTO.getExecCharge());
                objSiChargeSTTxTransferTO.setAmount(siTO.getExecCharge());
                System.out.println("######objTxTransfer1233333" + objSiChargeSTTxTransferTO.getInpAmount() + "@@@####" + objSiChargeSTTxTransferTO.getAmount());
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
                objSiChargeSTTxTransferTO.setStatusBy(CommonConstants.TTSYSTEM);
                objSiChargeSTTxTransferTO.setInitTransId(CommonConstants.TTSYSTEM);
            } else {
                objSiChargeSTTxTransferTO.setStatusBy(String.valueOf("SITTSYSTEM"));
                objSiChargeSTTxTransferTO.setInitTransId(String.valueOf("SITTSYSTEM"));
            }
            objSiChargeSTTxTransferTO.setStatusDt(curDate);
            objSiChargeSTTxTransferTO.setInitiatedBranch(_branchCode);
            objSiChargeSTTxTransferTO.setBranchId(_branchCode);
            System.out.println("######objSiChargeSTTxTransferTO" + objSiChargeSTTxTransferTO.getInitiatedBranch());

            prodID = null;
            actNum = null;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return objSiChargeSTTxTransferTO;
    }

    public static void main(String str[]) {
        try {
            StandingInstructionDAO dao = new StandingInstructionDAO();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public HashMap execute(HashMap map) throws Exception {
        System.out.println("@@@@execute: " + map);
        lbl = "";
        lblClose = "";
        if (map.containsKey("SUSPEND")) {
            lbl = CommonUtil.convertObjToStr(map.get("SUSPEND"));
            System.out.println("@@@@lbl" + lbl);
        }
        if (map.containsKey("CLOSE")) {
            lblClose = CommonUtil.convertObjToStr(map.get("CLOSE"));
            System.out.println("@@@@lblCLOSE" + lblClose);
        }
        _branchCode = (String) map.get(CommonConstants.BRANCH_ID);
        curDate = ServerUtil.getCurrentDate(_branchCode);
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
            System.out.println("@@#@#@#@#command" + command);
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

        if (command.equals(CommonConstants.TOSTATUS_INSERT)) {
            insertData();
//           returnMap = new HashMap();
//           returnMap.put("SI_ID",objStandingInstructionTO.getSiId());

        } else if (command.equals(CommonConstants.TOSTATUS_UPDATE)) {
            updateData();
        } else if (command.equals(CommonConstants.TOSTATUS_DELETE)) {
            deleteData();
        } else if (command.equals("EXECUTE_SI")) {
            siIdCreation = executeSi(map);
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

    public HashMap executeQuery(HashMap obj) throws Exception {
        _branchCode = (String) obj.get(CommonConstants.BRANCH_ID);
        curDate = ServerUtil.getCurrentDate(_branchCode);
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
}
