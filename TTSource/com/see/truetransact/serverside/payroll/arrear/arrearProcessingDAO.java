/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition.  
 * 
 * arrearProcessingDAO.java
 */

package com.see.truetransact.serverside.payroll.arrear;

import com.see.truetransact.serverside.generalledger.GLOpeningUpdate.*;
import com.see.truetransact.serverside.supporting.balanceupdate.*;

import java.util.List;
import java.util.HashMap;
import java.util.ArrayList;
import com.ibatis.db.sqlmap.SqlMap;
import com.see.truetransact.commonutil.CommonConstants;
import com.see.truetransact.commonutil.CommonUtil;
import com.see.truetransact.commonutil.DateUtil;
import com.see.truetransact.commonutil.TTException;
import com.see.truetransact.serverside.TTDAO;
import com.see.truetransact.servicelocator.ServiceLocator;
import com.see.truetransact.serverexception.TransRollbackException;
import com.see.truetransact.serverexception.ServiceLocatorException;
import com.see.truetransact.serverside.common.idgenerate.IDGenerateDAO;
import com.see.truetransact.serverside.common.transaction.TransactionDAO;
import com.see.truetransact.serverside.common.log.LogDAO;
import com.see.truetransact.serverside.common.transaction.TransferTrans;
import com.see.truetransact.serverside.transaction.common.TransactionFactory;
import com.see.truetransact.serverside.transaction.transfer.TransferDAO;
import com.see.truetransact.serverutil.ServerUtil;
import com.see.truetransact.transferobject.common.log.LogTO;
import com.see.truetransact.transferobject.supporting.balanceupdate.BalanceUpdateTO;
import com.see.truetransact.transferobject.generalledger.GLOpeningUpdateTO;
import com.see.truetransact.transferobject.payroll.arrear.ArrearTO;
import java.util.Date;

public class arrearProcessingDAO extends TTDAO {

    private static SqlMap sqlMap = null;
    private LogTO logTO;
    private LogDAO logDAO;
    private BalanceUpdate objBalanceUpdateTO;
    private String userID = "";
    private String branchCode = "";
    private ArrayList selectedArrayList;
    private ArrayList deletedArrayList;
    private int totalCount = 0;
    private BalanceUpdateTO objTO;
    BalanceUpdateTO objTO1 = new BalanceUpdateTO();
    Date currDt = null;
    int key = 1;
    private HashMap returnDataMap = new HashMap();
     String generateSingleTransId="";

    public arrearProcessingDAO() throws ServiceLocatorException {
        ServiceLocator locate = ServiceLocator.getInstance();
        sqlMap = (SqlMap) locate.getDAOSqlMap();
    }

    private Date setProperDtFormat(Date dt) {
        Date tempDt = (Date) currDt.clone();
        if (dt != null) {
            tempDt.setDate(dt.getDate());
            tempDt.setMonth(dt.getMonth());
            tempDt.setYear(dt.getYear());
            return tempDt;
        }
        return null;
    }

    public Date getProperDateFormat(Object obj) {
        Date currDate = null;
        if (obj != null && obj.toString().length() > 0) {
            Date tempDt = DateUtil.getDateMMDDYYYY(CommonUtil.convertObjToStr(obj));
            currDate = (Date) currDt.clone();
            currDate.setDate(tempDt.getDate());
            currDate.setMonth(tempDt.getMonth());
            currDate.setYear(tempDt.getYear());
        }
        return currDate;
    }

    private void insertPensionOnlyArrearCalculation(HashMap map) throws Exception {
        try {
            System.out.println("Map in arrear processing Transaction &&" + map);
            TransferTrans objTransferTrans = new TransferTrans();
            ArrayList transferList = new ArrayList();
            ArrayList ContraTransferList = new ArrayList();
            List employeeList = null;
            HashMap txMap = new HashMap();
            List lst = null;
            double tranAmt = 0.0;
            double totalDa = 0.0;
            double totalHra = 0.0;
            double totalContra = 0.0;
            double totalBasic = 0.0;
            HashMap BasicHeadMap = new HashMap();
            HashMap DaHeadMap = new HashMap();
            HashMap HraHeadMap = new HashMap();
            HashMap ContraHeadMap = new HashMap();
            HashMap employeeMap = new HashMap();
            if (map.containsKey("COMMAND") && map.get("COMMAND").equals("TRANSACTION") && map.get("EMPLOYEE_DATA") != null) {
                HashMap dataMap = new HashMap();
                HashMap eachMap = new HashMap();
                HashMap cRacHdMap = new HashMap();
                HashMap empDetailMap = new HashMap();
                employeeList = (List) map.get("EMPLOYEE_DATA");
                for (int i = 0; i < employeeList.size(); i++) {
                    map.put("EMPLOYEEID", employeeList.get(i));
                    if (map.containsKey("PENSION_ONLY") && map.get("PENSION_ONLY").equals("N")) {
                        map.put("PF_TYPE", "DEDUCTIONS");
                    }
                    List transList = ServerUtil.executeQuery("getNetPfArrearAmount", map);
                    if (transList != null && transList.size() > 0) {
                        //for (int i = 0; i < transList.size(); i++) {
                        dataMap = (HashMap) transList.get(0);
                        tranAmt = CommonUtil.convertObjToDouble(dataMap.get("NET_AMOUNT"));
                        //employee credit account
                        List creditList = ServerUtil.executeQuery("getemployeeSalAccount", dataMap);
                        if (creditList != null && creditList.size() > 0) {
                            empDetailMap = (HashMap) creditList.get(0);
                            //Credit account head
                            if (!empDetailMap.get("NET_SALARY_PROD_TYPE").equals("") && empDetailMap.get("NET_SALARY_PROD_TYPE").equals(TransactionFactory.ADVANCES)) {
                                dataMap.put("prodId", empDetailMap.get("NET_SALARY_PROD_ID"));
                                lst = sqlMap.executeQueryForList("getAccountHeadProdADHeadPayroll", dataMap);
                                if (lst != null && lst.size() > 0) {
                                    cRacHdMap = (HashMap) lst.get(0);
                                }
                            }
                            if (!empDetailMap.get("NET_SALARY_PROD_TYPE").equals("") && empDetailMap.get("NET_SALARY_PROD_TYPE").equals(TransactionFactory.OPERATIVE)) {
                                String value = CommonUtil.convertObjToStr(empDetailMap.get("NET_SALARY_ACC_NO"));
                                List lstOA = sqlMap.executeQueryForList("getAccountClosingHeads", value);
                                if (lstOA != null && lstOA.size() > 0) {
                                    cRacHdMap = (HashMap) lstOA.get(0);
                                }
                            }
                            if (!empDetailMap.get("NET_SALARY_PROD_TYPE").equals("") && empDetailMap.get("NET_SALARY_PROD_TYPE").equals(TransactionFactory.SUSPENSE)) {
                                dataMap.put("PROD_ID", empDetailMap.get("NET_SALARY_PROD_ID"));
                                lst = sqlMap.executeQueryForList("getAccountHeadProdSAHead", dataMap);
                                if (lst != null && lst.size() > 0) {
                                    cRacHdMap = (HashMap) lst.get(0);
                                }
                            }
                            if (!empDetailMap.get("NET_SALARY_PROD_TYPE").equals("") && empDetailMap.get("NET_SALARY_PROD_TYPE").equals(TransactionFactory.DEPOSITS)) {
                                dataMap.put("PROD_ID", empDetailMap.get("NET_SALARY_PROD_ID"));
                                lst = sqlMap.executeQueryForList("getDepositClosingHeadsPayroll", empDetailMap);
                                if (lst != null && lst.size() > 0) {
                                    cRacHdMap = (HashMap) lst.get(0);
                                }
                            }
                        }
                        eachMap.put("EMPLOYEEID", dataMap.get("EMPLOYEEID"));
                        eachMap.put("FROM_DT", map.get("FROM_DT"));
                        eachMap.put("TO_DT", map.get("TO_DT"));
                        eachMap.put("BASED_ON", map.get("BASED_ON"));
                        eachMap.put("TRANS_DT", map.get("TRANS_DT"));
                        objTransferTrans.setLinkBatchId(CommonUtil.convertObjToStr(dataMap.get("EMPLOYEEID")));
                        List eachList = ServerUtil.executeQuery("getArrearEmployeeWise", eachMap);
                        if (eachList != null && eachList.size() > 0) {
                            for (int j = 0; j < eachList.size(); j++) {
                                eachMap = (HashMap) eachList.get(j);
                                //Debit
                                if (CommonUtil.convertObjToDouble(eachMap.get("DIFFERENCE")) > 0) {
                                    if (eachMap.get("PAY_EARNDEDU").equals("DEDUCTIONS")) {
                                        if (map.containsKey("PENSION_ONLY") && map.get("PENSION_ONLY").equals("N")) {
                                            txMap = new HashMap();
                                            txMap.put(TransferTrans.CR_AC_HD, CommonUtil.convertObjToStr(eachMap.get("ACC_HD")));
                                            txMap.put(TransferTrans.CR_PROD_TYPE, TransactionFactory.GL);
                                            txMap.put(TransferTrans.CR_BRANCH, _branchCode);
                                            txMap.put("AUTHORIZEREMARKS", "ARREAR_PROCESSING");
                                            txMap.put(TransferTrans.AUTHORIZE_STATUS_2, "ENTERED_AMOUNT");
                                            txMap.put(TransferTrans.PARTICULARS, "" + dataMap.get("EMPLOYEEID") + " " + eachMap.get("PAY_DESCRI"));
                                            txMap.put("LINK_BATCH_ID", map.get("EMPLOYEEID"));
                                            txMap.put("SCREEN_NAME", "Arrear Process");
                                            txMap.put("TRANS_MOD_TYPE", TransactionFactory.GL);
                                            txMap.put("GL_TRANS_ACT_NUM", dataMap.get("EMPLOYEEID"));
                                            txMap.put("generateSingleTransId", generateSingleTransId);
                                            transferList.add(objTransferTrans.getCreditTransferTO(txMap, CommonUtil.convertObjToDouble(eachMap.get("DIFFERENCE"))));

                                            if (-1 * (tranAmt) > 0) {
                                                txMap = new HashMap();
                                                if (map.containsKey("DIFFERENT_ACCOUNT")) {
                                                    if (map.get("DEBIT_PRODUCT_TYPE").equals(TransactionFactory.GL)) {
                                                        txMap.put(TransferTrans.DR_AC_HD, CommonUtil.convertObjToStr(map.get("DEBIT_ACCOUNT_NO")));
                                                        txMap.put(TransferTrans.DR_PROD_TYPE, TransactionFactory.GL);
                                                        txMap.put("TRANS_MOD_TYPE",TransactionFactory.GL);
                                                    } else {
                                                        txMap.put(TransferTrans.DR_ACT_NUM, map.get("DEBIT_ACCOUNT_NO"));
                                                        txMap.put(TransferTrans.DR_PROD_TYPE, map.get("DEBIT_PRODUCT_TYPE"));
                                                        txMap.put(TransferTrans.DR_PROD_ID, map.get("DEBIT_PRODUCT_ID"));
                                                        txMap.put("TRANS_MOD_TYPE", map.get("DEBIT_PRODUCT_TYPE"));
                                                    }
                                                } else {
                                                    if (empDetailMap.get("NET_SALARY_PROD_TYPE").equals(TransactionFactory.GL)) {
                                                        txMap.put(TransferTrans.DR_AC_HD, CommonUtil.convertObjToStr(cRacHdMap.get("ACC_HD")));
                                                        txMap.put(TransferTrans.DR_PROD_TYPE, TransactionFactory.GL);
                                                        txMap.put("TRANS_MOD_TYPE", TransactionFactory.GL);
                                                    } else {
                                                        txMap.put(TransferTrans.DR_AC_HD, cRacHdMap.get("AC_HD_ID"));
                                                        txMap.put(TransferTrans.DR_ACT_NUM, empDetailMap.get("NET_SALARY_ACC_NO"));
                                                        txMap.put(TransferTrans.DR_PROD_TYPE, empDetailMap.get("NET_SALARY_PROD_TYPE"));
                                                        txMap.put(TransferTrans.DR_PROD_ID, empDetailMap.get("NET_SALARY_PROD_ID"));
                                                        txMap.put("TRANS_MOD_TYPE",  empDetailMap.get("NET_SALARY_PROD_TYPE"));
                                                    }
                                                }
                                                txMap.put(TransferTrans.DR_BRANCH, _branchCode);
                                                txMap.put("AUTHORIZEREMARKS", "ARREAR_PROCESSING");
                                                txMap.put(TransferTrans.AUTHORIZE_STATUS_2, "ENTERED_AMOUNT");
                                                txMap.put(TransferTrans.PARTICULARS, "" + dataMap.get("EMPLOYEEID"));
                                                txMap.put("LINK_BATCH_ID", map.get("EMPLOYEEID"));
                                                txMap.put("SCREEN_NAME","Arrear Process" );         
                                                txMap.put("GL_TRANS_ACT_NUM",dataMap.get("EMPLOYEEID"));
                                                txMap.put("generateSingleTransId", generateSingleTransId);
                                                transferList.add(objTransferTrans.getDebitTransferTO(txMap, CommonUtil.convertObjToDouble(eachMap.get("DIFFERENCE"))));
                                            }
                                        }
                                    } else if (eachMap.get("PAY_EARNDEDU").equals("CONTRA")) {
                                        if (CommonUtil.convertObjToStr(eachMap.get("ACC_TYPE")).equalsIgnoreCase("Debit")) {
                                            totalContra = totalContra + CommonUtil.convertObjToDouble(eachMap.get("DIFFERENCE"));
                                            ContraHeadMap.put("ACC_HD", CommonUtil.convertObjToStr(eachMap.get("ACC_HD")));
                                        } else {
                                            txMap = new HashMap();
                                            txMap.put(TransferTrans.CR_AC_HD, CommonUtil.convertObjToStr(eachMap.get("ACC_HD")));
                                            txMap.put(TransferTrans.CR_PROD_TYPE, TransactionFactory.GL);
                                            txMap.put(TransferTrans.CR_BRANCH, _branchCode);
                                            txMap.put("AUTHORIZEREMARKS", "ARREAR_PROCESSING");
                                            txMap.put(TransferTrans.AUTHORIZE_STATUS_2, "ENTERED_AMOUNT");
                                            txMap.put(TransferTrans.PARTICULARS, "" + dataMap.get("EMPLOYEEID") + " " + eachMap.get("PAY_DESCRI"));
                                            txMap.put("LINK_BATCH_ID", map.get("EMPLOYEEID"));
                                            txMap.put("SCREEN_NAME","Arrear Process" );         
                                            txMap.put("GL_TRANS_ACT_NUM",dataMap.get("EMPLOYEEID"));
                                            txMap.put("TRANS_MOD_TYPE", TransactionFactory.GL);
                                            txMap.put("generateSingleTransId", generateSingleTransId);
                                            transferList.add(objTransferTrans.getCreditTransferTO(txMap, CommonUtil.convertObjToDouble(eachMap.get("DIFFERENCE"))));
                                        }
                                    }
                                }
                            }
                            //consolidate amount transaction
                        }
                    }
                }
                if (totalContra > 0) {
                    txMap = new HashMap();
                    txMap.put(TransferTrans.DR_AC_HD, CommonUtil.convertObjToStr(ContraHeadMap.get("ACC_HD")));
                    txMap.put(TransferTrans.DR_PROD_TYPE, TransactionFactory.GL);
                    txMap.put(TransferTrans.DR_BRANCH, _branchCode);
                    txMap.put("AUTHORIZEREMARKS", "ARREAR_PROCESSING");
                    txMap.put(TransferTrans.AUTHORIZE_STATUS_2, "ENTERED_AMOUNT");
                    txMap.put(TransferTrans.PARTICULARS, " " + eachMap.get("PAY_DESCRI"));
                    //txMap.put("LINK_BATCH_ID", dataMap.get("EMPLOYEEID"));
                    txMap.put(TransferTrans.DR_INST_TYPE, "VOUCHER");
                    txMap.put("INITIATED_BRANCH", branchCode);
                    txMap.put("SCREEN_NAME","Arrear Process" );         
                    txMap.put("GL_TRANS_ACT_NUM",dataMap.get("EMPLOYEEID"));
                    txMap.put("TRANS_MOD_TYPE", TransactionFactory.GL);
                    txMap.put("generateSingleTransId", generateSingleTransId);
                    transferList.add(objTransferTrans.getDebitTransferTO(txMap, totalContra));
                }
                //To transfer
                if (transferList != null && transferList.size() > 0) {
                    doDebitCredit(transferList, false);
                } else {
                    throw new TTException("Transaction Failed!!!");
                }

                ContraTransferList = null;
                transferList = null;
                objTransferTrans = null;
                dataMap = null;
                eachMap = null;
                cRacHdMap = null;
                empDetailMap = null;
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw new TransRollbackException(e);
        }
    }

    private void getTransDetails(String batchId) throws Exception {
        HashMap getTransMap = new HashMap();
        getTransMap.put("BATCH_ID", batchId);
        getTransMap.put("TRANS_DT", currDt);
        getTransMap.put(CommonConstants.BRANCH_ID, _branchCode);
        returnDataMap = new HashMap();
        List transList = (List) sqlMap.executeQueryForList("getTransferDetailsPayRoll", getTransMap);
        if (transList != null && transList.size() > 0) {
            returnDataMap.put("TRANSFER_TRANS_LIST", transList);
        }
        List cashList = (List) sqlMap.executeQueryForList("getCashDetailsPayRoll", getTransMap);
        if (cashList != null && cashList.size() > 0) {
            returnDataMap.put("CASH_TRANS_LIST", cashList);
        }
        getTransMap.clear();
        getTransMap = null;
        transList = null;
        cashList = null;
    }
    
    private void insertArrearCalcluation(HashMap map) throws Exception {
        try {
            System.out.println("Map in ArrearCalculation &&" + map);
            TransferTrans objTransferTrans = new TransferTrans();
            ArrayList transferList = new ArrayList();
            ArrayList ContraTransferList = new ArrayList();
            HashMap txMap = new HashMap();
            List employeeList = null;
            List lst = null;
            double tranAmt = 0.0;
            double totalDa = 0.0;
            double totalHra = 0.0;
            double totalContra = 0.0;
            double totalBasic = 0.0;
            HashMap BasicHeadMap = new HashMap();
            HashMap DaHeadMap = new HashMap();
            HashMap HraHeadMap = new HashMap();
            HashMap ContraHeadMap = new HashMap();
            if (map.containsKey("COMMAND") && map.get("COMMAND").equals("TRANSACTION") && map.get("EMPLOYEE_DATA") != null) {
                HashMap dataMap = new HashMap();
                HashMap eachMap = new HashMap();
                HashMap cRacHdMap = new HashMap();
                HashMap empDetailMap = new HashMap();
                employeeList = (List) map.get("EMPLOYEE_DATA");
                for (int i = 0; i < employeeList.size(); i++) {
                    map.put("EMPLOYEEID", employeeList.get(i));
                    List transList = ServerUtil.executeQuery("getNetArrearAmount", map);
                    if (transList != null && transList.size() > 0) {
                        //for (int i = 0; i < transList.size(); i++) {
                        dataMap = (HashMap) transList.get(0);
                        tranAmt = CommonUtil.convertObjToDouble(dataMap.get("NET_AMOUNT"));
                        //employee credit account
                        List creditList = ServerUtil.executeQuery("getemployeeSalAccount", dataMap);
                        if (creditList != null && creditList.size() > 0) {
                            empDetailMap = (HashMap) creditList.get(0);
                            //Credit account head
                            if (!empDetailMap.get("NET_SALARY_PROD_TYPE").equals("") && empDetailMap.get("NET_SALARY_PROD_TYPE").equals(TransactionFactory.ADVANCES)) {
                                dataMap.put("prodId", empDetailMap.get("NET_SALARY_PROD_ID"));
                                lst = sqlMap.executeQueryForList("getAccountHeadProdADHeadPayroll", dataMap);
                                if (lst != null && lst.size() > 0) {
                                    cRacHdMap = (HashMap) lst.get(0);
                                }
                            }
                            if (!empDetailMap.get("NET_SALARY_PROD_TYPE").equals("") && empDetailMap.get("NET_SALARY_PROD_TYPE").equals(TransactionFactory.OPERATIVE)) {
                                String value = CommonUtil.convertObjToStr(empDetailMap.get("NET_SALARY_ACC_NO"));
                                List lstOA = sqlMap.executeQueryForList("getAccountClosingHeads", value);
                                if (lstOA != null && lstOA.size() > 0) {
                                    cRacHdMap = (HashMap) lstOA.get(0);
                                }
                            }
                            if (!empDetailMap.get("NET_SALARY_PROD_TYPE").equals("") && empDetailMap.get("NET_SALARY_PROD_TYPE").equals(TransactionFactory.SUSPENSE)) {
                                dataMap.put("PROD_ID", empDetailMap.get("NET_SALARY_PROD_ID"));
                                lst = sqlMap.executeQueryForList("getAccountHeadProdSAHead", dataMap);
                                if (lst != null && lst.size() > 0) {
                                    cRacHdMap = (HashMap) lst.get(0);
                                }
                            }
                            if (!empDetailMap.get("NET_SALARY_PROD_TYPE").equals("") && empDetailMap.get("NET_SALARY_PROD_TYPE").equals(TransactionFactory.DEPOSITS)) {
                                dataMap.put("PROD_ID", empDetailMap.get("NET_SALARY_PROD_ID"));
                                lst = sqlMap.executeQueryForList("getDepositClosingHeadsPayroll", empDetailMap);
                                if (lst != null && lst.size() > 0) {
                                    cRacHdMap = (HashMap) lst.get(0);
                                }
                            }
                        }
                        eachMap.put("EMPLOYEEID", dataMap.get("EMPLOYEEID"));
                        eachMap.put("FROM_DT", map.get("FROM_DT"));
                        eachMap.put("TO_DT", map.get("TO_DT"));
                        eachMap.put("BASED_ON", map.get("BASED_ON"));
                        eachMap.put("TRANS_DT", map.get("TRANS_DT"));
                        objTransferTrans.setLinkBatchId(CommonUtil.convertObjToStr(dataMap.get("EMPLOYEEID")));
                        List eachList = ServerUtil.executeQuery("getArrearEmployeeWise", eachMap);
                        if (eachList != null && eachList.size() > 0) {
                            for (int j = 0; j < eachList.size(); j++) {
                                eachMap = (HashMap) eachList.get(j);
                                //Debit
                                if (CommonUtil.convertObjToDouble(eachMap.get("DIFFERENCE")) > 0) {
                                    if (eachMap.get("PAY_EARNDEDU").equals("EARNINGS")) {
                                        if (eachMap.get("PAY_MODULE_TYPE").equals("DearNess Allowance")) {
                                            totalDa = totalDa + CommonUtil.convertObjToDouble(eachMap.get("DIFFERENCE"));
                                            DaHeadMap.put("ACC_HD", CommonUtil.convertObjToStr(eachMap.get("ACC_HD")));
                                        } else if (eachMap.get("PAY_MODULE_TYPE").equals("BasicPay")) {
                                            totalBasic = totalBasic + CommonUtil.convertObjToDouble(eachMap.get("DIFFERENCE"));
                                            BasicHeadMap.put("ACC_HD", CommonUtil.convertObjToStr(eachMap.get("ACC_HD")));
                                        } else {
                                            totalHra = totalHra + CommonUtil.convertObjToDouble(eachMap.get("DIFFERENCE"));
                                        }
                                        HraHeadMap.put("ACC_HD", CommonUtil.convertObjToStr(eachMap.get("ACC_HD")));
                                    } else if (eachMap.get("PAY_EARNDEDU").equals("DEDUCTIONS")) {
                                        txMap = new HashMap();
                                        txMap.put(TransferTrans.CR_AC_HD, CommonUtil.convertObjToStr(eachMap.get("ACC_HD")));
                                        txMap.put(TransferTrans.CR_PROD_TYPE, TransactionFactory.GL);
                                        txMap.put(TransferTrans.CR_BRANCH, _branchCode);
                                        txMap.put("AUTHORIZEREMARKS", "ARREAR_PROCESSING");
                                        txMap.put(TransferTrans.AUTHORIZE_STATUS_2, "ENTERED_AMOUNT");
                                        txMap.put(TransferTrans.PARTICULARS, "By" + "" + dataMap.get("EMPLOYEEID") + " " + eachMap.get("PAY_DESCRI"));
                                        txMap.put("LINK_BATCH_ID", dataMap.get("EMPLOYEEID"));
                                        txMap.put("SCREEN_NAME","Arrear Process" );
                                        txMap.put("TRANS_MOD_TYPE", TransactionFactory.GL);
                                        txMap.put("GL_TRANS_ACT_NUM",dataMap.get("EMPLOYEEID"));
                                        txMap.put("generateSingleTransId", generateSingleTransId);
                                        transferList.add(objTransferTrans.getCreditTransferTO(txMap, CommonUtil.convertObjToDouble(eachMap.get("DIFFERENCE"))));
                                    } else if (eachMap.get("PAY_EARNDEDU").equals("CONTRA")) {
                                        if (CommonUtil.convertObjToStr(eachMap.get("ACC_TYPE")).equalsIgnoreCase("Debit")) {
                                            totalContra = totalContra + CommonUtil.convertObjToDouble(eachMap.get("DIFFERENCE"));
                                            ContraHeadMap.put("ACC_HD", CommonUtil.convertObjToStr(eachMap.get("ACC_HD")));
                                        } else {
                                            txMap = new HashMap();
                                            txMap.put(TransferTrans.CR_AC_HD, CommonUtil.convertObjToStr(eachMap.get("ACC_HD")));
                                            txMap.put(TransferTrans.CR_PROD_TYPE, TransactionFactory.GL);
                                            txMap.put(TransferTrans.CR_BRANCH, _branchCode);
                                            txMap.put("AUTHORIZEREMARKS", "ARREAR_PROCESSING");
                                            txMap.put(TransferTrans.AUTHORIZE_STATUS_2, "ENTERED_AMOUNT");
                                            txMap.put(TransferTrans.PARTICULARS, "By" + "" + dataMap.get("EMPLOYEEID") + " " + eachMap.get("PAY_DESCRI"));
                                            txMap.put("LINK_BATCH_ID", dataMap.get("EMPLOYEEID"));
                                            txMap.put("SCREEN_NAME","Arrear Process" );
                                            txMap.put("TRANS_MOD_TYPE", TransactionFactory.GL);
                                            txMap.put("GL_TRANS_ACT_NUM",dataMap.get("EMPLOYEEID"));
                                            txMap.put("generateSingleTransId", generateSingleTransId);
                                            ContraTransferList.add(objTransferTrans.getCreditTransferTO(txMap, CommonUtil.convertObjToDouble(eachMap.get("DIFFERENCE"))));
                                        }
                                    }
                                }
                            }
                            //consolidate amount transaction
                            if (tranAmt > 0) {
                                txMap = new HashMap();
                                if (empDetailMap.get("NET_SALARY_PROD_TYPE").equals(TransactionFactory.GL)) {
                                    txMap.put(TransferTrans.CR_AC_HD, CommonUtil.convertObjToStr(cRacHdMap.get("ACC_HD")));
                                    txMap.put(TransferTrans.CR_PROD_TYPE, TransactionFactory.GL);
                                    txMap.put("TRANS_MOD_TYPE", empDetailMap.get("NET_SALARY_PROD_TYPE"));
                                } else {
                                    txMap.put(TransferTrans.CR_AC_HD, cRacHdMap.get("AC_HD_ID"));
                                    txMap.put(TransferTrans.CR_ACT_NUM, empDetailMap.get("NET_SALARY_ACC_NO"));
                                    txMap.put(TransferTrans.CR_PROD_TYPE, empDetailMap.get("NET_SALARY_PROD_TYPE"));
                                    txMap.put(TransferTrans.CR_PROD_ID, empDetailMap.get("NET_SALARY_PROD_ID"));
                                    txMap.put("TRANS_MOD_TYPE", empDetailMap.get("NET_SALARY_PROD_TYPE"));
                                }
                                txMap.put(TransferTrans.CR_BRANCH, _branchCode);
                                txMap.put("AUTHORIZEREMARKS", "ARREAR_PROCESSING");
                                txMap.put(TransferTrans.AUTHORIZE_STATUS_2, "ENTERED_AMOUNT");
                                txMap.put(TransferTrans.PARTICULARS, "By" + "" + dataMap.get("EMPLOYEEID"));
                                txMap.put("LINK_BATCH_ID", dataMap.get("EMPLOYEEID"));
                                txMap.put("SCREEN_NAME","Arrear Process" );
                                txMap.put("GL_TRANS_ACT_NUM",dataMap.get("EMPLOYEEID"));
                                txMap.put("generateSingleTransId", generateSingleTransId);
                                transferList.add(objTransferTrans.getCreditTransferTO(txMap, tranAmt));
                            }
                        }
                    }
                }
                if (totalDa > 0) {
                    txMap = new HashMap();
                    txMap.put(TransferTrans.DR_AC_HD, CommonUtil.convertObjToStr(DaHeadMap.get("ACC_HD")));
                    txMap.put(TransferTrans.DR_PROD_TYPE, TransactionFactory.GL);
                    txMap.put(TransferTrans.DR_BRANCH, _branchCode);
                    txMap.put("AUTHORIZEREMARKS", "ARREAR_PROCESSING");
                    txMap.put(TransferTrans.AUTHORIZE_STATUS_2, "ENTERED_AMOUNT");
                    txMap.put(TransferTrans.PARTICULARS, "By" + " " + dataMap.get("EMPLOYEEID") + " " + eachMap.get("PAY_DESCRI"));
                    //txMap.put("LINK_BATCH_ID", dataMap.get("EMPLOYEEID"));
                    txMap.put(TransferTrans.DR_INST_TYPE, "VOUCHER");
                    txMap.put("INITIATED_BRANCH", branchCode);
                    txMap.put("SCREEN_NAME","Arrear Process" );
                    txMap.put("TRANS_MOD_TYPE", TransactionFactory.GL);
                    txMap.put("GL_TRANS_ACT_NUM",dataMap.get("EMPLOYEEID"));
                    txMap.put("generateSingleTransId", generateSingleTransId);
                    transferList.add(objTransferTrans.getDebitTransferTO(txMap, totalDa));
                }
                if (totalHra > 0) {
                    txMap = new HashMap();
                    txMap.put(TransferTrans.DR_AC_HD, CommonUtil.convertObjToStr(HraHeadMap.get("ACC_HD")));
                    txMap.put(TransferTrans.DR_PROD_TYPE, TransactionFactory.GL);
                    txMap.put(TransferTrans.DR_BRANCH, _branchCode);
                    txMap.put("AUTHORIZEREMARKS", "ARREAR_PROCESSING");
                    txMap.put(TransferTrans.AUTHORIZE_STATUS_2, "ENTERED_AMOUNT");
                    txMap.put(TransferTrans.PARTICULARS, "By" + " " + dataMap.get("EMPLOYEEID") + " " + eachMap.get("PAY_DESCRI"));
                    //txMap.put("LINK_BATCH_ID", dataMap.get("EMPLOYEEID"));
                    txMap.put(TransferTrans.DR_INST_TYPE, "VOUCHER");
                    txMap.put("INITIATED_BRANCH", branchCode);
                    txMap.put("SCREEN_NAME","Arrear Process" );
                    txMap.put("TRANS_MOD_TYPE", TransactionFactory.GL);
                    txMap.put("GL_TRANS_ACT_NUM",dataMap.get("EMPLOYEEID"));
                    txMap.put("generateSingleTransId", generateSingleTransId);
                    transferList.add(objTransferTrans.getDebitTransferTO(txMap, totalHra));
                }
                if (totalContra > 0) {
                    txMap = new HashMap();
                    txMap.put(TransferTrans.DR_AC_HD, CommonUtil.convertObjToStr(ContraHeadMap.get("ACC_HD")));
                    txMap.put(TransferTrans.DR_PROD_TYPE, TransactionFactory.GL);
                    txMap.put(TransferTrans.DR_BRANCH, _branchCode);
                    txMap.put("AUTHORIZEREMARKS", "ARREAR_PROCESSING");
                    txMap.put(TransferTrans.AUTHORIZE_STATUS_2, "ENTERED_AMOUNT");
                    txMap.put(TransferTrans.PARTICULARS, "By" + " " + dataMap.get("EMPLOYEEID") + " " + eachMap.get("PAY_DESCRI"));
                    //txMap.put("LINK_BATCH_ID", dataMap.get("EMPLOYEEID"));
                    txMap.put(TransferTrans.DR_INST_TYPE, "VOUCHER");
                    txMap.put("INITIATED_BRANCH", branchCode);
                    txMap.put("SCREEN_NAME","Arrear Process" );
                    txMap.put("TRANS_MOD_TYPE", TransactionFactory.GL);
                    txMap.put("GL_TRANS_ACT_NUM",dataMap.get("EMPLOYEEID"));
                    txMap.put("generateSingleTransId", generateSingleTransId);
                    ContraTransferList.add(objTransferTrans.getDebitTransferTO(txMap, totalContra));
                }
                if (totalBasic > 0) {
                    txMap = new HashMap();
                    txMap.put(TransferTrans.DR_AC_HD, CommonUtil.convertObjToStr(BasicHeadMap.get("ACC_HD")));
                    txMap.put(TransferTrans.DR_PROD_TYPE, TransactionFactory.GL);
                    txMap.put(TransferTrans.DR_BRANCH, _branchCode);
                    txMap.put("AUTHORIZEREMARKS", "ARREAR_PROCESSING");
                    txMap.put(TransferTrans.AUTHORIZE_STATUS_2, "ENTERED_AMOUNT");
                    txMap.put(TransferTrans.PARTICULARS, "By" + " " + dataMap.get("EMPLOYEEID") + " " + eachMap.get("PAY_DESCRI"));
                    //txMap.put("LINK_BATCH_ID", dataMap.get("EMPLOYEEID"));
                    txMap.put(TransferTrans.DR_INST_TYPE, "VOUCHER");
                    txMap.put("INITIATED_BRANCH", branchCode);
                    txMap.put("SCREEN_NAME","Arrear Process" );
                    txMap.put("TRANS_MOD_TYPE", TransactionFactory.GL);
                    txMap.put("GL_TRANS_ACT_NUM",dataMap.get("EMPLOYEEID"));
                    txMap.put("generateSingleTransId", generateSingleTransId);
                    transferList.add(objTransferTrans.getDebitTransferTO(txMap, totalBasic));
                }

                //}
                if (transferList != null && transferList.size() > 0) {
                    //objTransferTrans.setInitiatedBranch(_branchCode);
                    //objTransferTrans.doDebitCredit(transferList, _branchCode);
                    doDebitCredit(transferList, false);
                    //getTransDetails(CommonUtil.convertObjToStr(returnDataMap.get("SINGLE_TRANS_ID")));
                } else {
                    throw new TTException("Transaction Failed!!!");
                }
                //Contra list
                if (ContraTransferList != null && ContraTransferList.size() > 0) {
                    //objTransferTrans.setInitiatedBranch(_branchCode);
                    //objTransferTrans.doDebitCredit(ContraTransferList, _branchCode);
                    doDebitCredit(ContraTransferList, false);
                    //getTransDetails(CommonUtil.convertObjToStr(returnDataMap.get("SINGLE_TRANS_ID")));
                } else {
                    throw new TTException("Transaction Failed!!!");
                }

                ContraTransferList = null;
                transferList = null;
                objTransferTrans = null;
                dataMap = null;
                eachMap = null;
                cRacHdMap = null;
                empDetailMap = null;
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw new TransRollbackException(e);
        }
    }

    private void doDebitCredit(ArrayList batchList,boolean isAutoAuthorize) throws Exception {
        TransferDAO transferDAO = new TransferDAO();
        HashMap data = new HashMap();
        data.put("TxTransferTO", batchList);
        data.put("COMMAND", CommonConstants.TOSTATUS_INSERT);
        data.put("INITIATED_BRANCH", _branchCode);
        data.put(CommonConstants.BRANCH_ID, _branchCode);
        data.put(CommonConstants.USER_ID, logTO.getUserId());
        data.put(CommonConstants.MODULE, "Payroll");
        data.put(CommonConstants.SCREEN, "Arrear Process");
        data.put("MODE", "MODE");
        HashMap authorizeMap = new HashMap();
        authorizeMap.put("BATCH_ID", null);
        authorizeMap.put(CommonConstants.AUTHORIZESTATUS, CommonConstants.STATUS_AUTHORIZED);
        data.put(CommonConstants.AUTHORIZEMAP, authorizeMap);
        returnDataMap = transferDAO.execute(data, false);
        System.out.println("returnDataMap^$^$^$^$^$^$^$"+returnDataMap);
        //if (isAutoAuthorize == true) {
        //    authorizeTransaction(returnDataMap);
        //}
    }

    public void authorizeTransaction(HashMap returnDataMap) throws Exception {
        try {
            if (returnDataMap != null && returnDataMap.get("TRANS_ID") != null && !returnDataMap.get("TRANS_ID").equals("")) {
                System.out.println("TRANSFER TRANS_ID :" + returnDataMap.get("TRANS_ID"));
                String authorizeStatus = CommonUtil.convertObjToStr(CommonConstants.STATUS_AUTHORIZED);
                String linkBatchId = CommonUtil.convertObjToStr(returnDataMap.get("TRANS_ID"));
                HashMap cashAuthMap = new HashMap();
                cashAuthMap.put(CommonConstants.BRANCH_ID, _branchCode);
                cashAuthMap.put(CommonConstants.USER_ID, "admin");
                TransactionDAO transactionDAO = new TransactionDAO(CommonConstants.DEBIT);
                TransactionDAO.authorizeCashAndTransferWithoutRemitIssueDet(linkBatchId, authorizeStatus, cashAuthMap);
                cashAuthMap = null;
                //transMap = null;
            }
        } catch (Exception e) {
            sqlMap.rollbackTransaction();
            e.printStackTrace();
            throw e;
        }
    }

    private void insertData(HashMap map) throws Exception {
        try {
            ArrearTO arrearTO = new ArrearTO();
            if (map.containsKey("ArrearTO")) {
                arrearTO = (ArrearTO) map.get("ArrearTO");
            }
            sqlMap.executeUpdate("insertArrearTO", arrearTO);
        } catch (Exception e) {
            e.printStackTrace();
            throw new TransRollbackException(e);
        }
    }

    public static void main(String str[]) {
        try {
            arrearProcessingDAO dao = new arrearProcessingDAO();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public HashMap execute(HashMap map) throws Exception {
        System.out.println("arrearProcessingDAO Execute Method : " + map);
        totalCount = 0;
        HashMap returnMap = new HashMap();
        _branchCode = (String) map.get(CommonConstants.BRANCH_ID);
        currDt = ServerUtil.getCurrentDate(_branchCode);
        logDAO = new LogDAO();
        logTO = new LogTO();
        logTO.setUserId(CommonUtil.convertObjToStr(map.get(CommonConstants.USER_ID)));
        final String command = CommonUtil.convertObjToStr(map.get("COMMAND"));
        if (command.equals(CommonConstants.TOSTATUS_INSERT)) {
            insertData(map);
        } else if (command.equals(CommonConstants.TOSTATUS_DELETE)) {
            System.out.println("ENTRY TO DELETE");
            deleteData(map);
        } else if (command.equals("TRANSACTION")) {
             generateSingleTransId = generateLinkID();
            if (map.containsKey("BASED_ON") && CommonUtil.convertObjToStr(map.get("BASED_ON")).equalsIgnoreCase("PF")) {
                insertPensionOnlyArrearCalculation(map);
            } else {
                insertArrearCalcluation(map);
            }
        }
        return returnDataMap;
    }

    @Override
    public HashMap executeQuery(HashMap obj) throws Exception {
        return getData(obj);
    }

    private void deleteData(HashMap deleteMap) throws Exception {
        try {
            sqlMap.startTransaction();
            if (deleteMap.containsKey("COMMAND") && deleteMap.get("COMMAND").equals(CommonConstants.TOSTATUS_DELETE)) {
                sqlMap.executeUpdate("deleteArrearProcessData", deleteMap);
            }
            sqlMap.commitTransaction();
        } catch (Exception e) {
            sqlMap.rollbackTransaction();
            e.printStackTrace();
            throw new TransRollbackException(e);
        }
    }

    private HashMap getData(HashMap map) throws Exception {
        return null;
    }

    private String generateLinkID() throws Exception {
        IDGenerateDAO dao = new IDGenerateDAO();
        final HashMap where = new HashMap();
        where.put(CommonConstants.MAP_WHERE, "GENERATE_LINK_ID");
        where.put("INIT_TRANS_ID", "INIT_TRANS_ID");//for trans_batch_id resetting, branch code
        where.put(CommonConstants.BRANCH_ID, _branchCode);
        String batchID = CommonUtil.convertObjToStr((dao.executeQuery(where)).get(CommonConstants.DATA));
        dao = null;
        return batchID;
    }

}
