/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition.
 **
 *
 * RecoveryListTallyOB.java
 *
 *
 */

package com.see.truetransact.ui.salaryrecovery;

import java.util.*;
import java.util.List;
import java.util.HashMap;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import org.apache.log4j.Logger;
import com.see.truetransact.ui.TrueTransactMain;
import com.see.truetransact.commonutil.DateUtil;
import com.see.truetransact.commonutil.CommonUtil;
import com.see.truetransact.clientutil.ClientUtil;
import com.see.truetransact.commonutil.TTException;
import com.see.truetransact.uicomponent.CObservable;
import com.see.truetransact.clientproxy.ProxyFactory;
import com.see.truetransact.clientutil.ComboBoxModel;
import com.see.truetransact.clientutil.ClientConstants;
import com.see.truetransact.commonutil.CommonConstants;
import com.see.truetransact.clientutil.EnhancedTableModel;
import com.see.truetransact.ui.common.transaction.TransactionOB;
import com.see.truetransact.clientexception.ClientParseException;
import java.text.DecimalFormat;

/**
 *
 * @author Suresh
 */
public class RecoveryListTallyOB extends CObservable {

    final ArrayList tableTitle = new ArrayList();
    final ArrayList tableTitle1 = new ArrayList();
    private EnhancedTableModel tblSalaryRecoveryList;
    private EnhancedTableModel tblproduct;
    private ProxyFactory proxy;
    private HashMap map;
    private final static Logger log = Logger.getLogger(RecoveryListTallyOB.class);
    private final static ClientParseException parseException = ClientParseException.getInstance();
    private int result;
    private int actionType;
    private Date currDate = null;
    private List finalList = null;
    private HashMap finalMap = new HashMap();
    private String tdtCalcIntUpto = "";
    private ComboBoxModel cbmProdType;
    private String cboProdType = "";
    private ComboBoxModel cbmProdId;
    private HashMap lookUpHash;
    private HashMap keyValue;
    private ArrayList key;
    private ArrayList value;
    private String txtAccountNo = "";
    private TransactionOB transactionOB = null;
    private LinkedHashMap allowedTransactionDetailsTO = null;
    private LinkedHashMap transactionDetailsTO = null;
    private LinkedHashMap deletedTransactionDetailsTO = null;
    private final String DELETED_TRANS_TOs = "DELETED_TRANS_TOs";
    private final String NOT_DELETED_TRANS_TOs = "NOT_DELETED_TRANS_TOs";
    DecimalFormat df = new DecimalFormat("##.00");
    private String emp_type = "";
    int principal, penal, penalInt, charges;

    public RecoveryListTallyOB() {
        try {
            proxy = ProxyFactory.createProxy();
            map = new HashMap();
            map.put(CommonConstants.JNDI, "RecoveryListTallyJNDI");
            map.put(CommonConstants.HOME, "salaryrecovery.RecoveryListTallyHome");
            map.put(CommonConstants.REMOTE, "salaryrecovery.RecoveryListTally");
            fillDropdown();
            setDepositInterestTableTitle();
//             setDepositInterestTableTitle1();

            tblSalaryRecoveryList = new EnhancedTableModel(null, tableTitle);
            tblproduct = new EnhancedTableModel(null, tableTitle1);
            // tblUnrecovery = new EnhancedTableModel(null, tableTitle);
        } catch (Exception e) {
            parseException.logException(e, true);
        }
    }

    /**
     * A method to set the combo box values
     */
    private void fillDropdown() throws Exception {
        try {
            lookUpHash = new HashMap();
            lookUpHash.put(CommonConstants.MAP_NAME, null);
            final ArrayList lookup_keys = new ArrayList();
            lookup_keys.add("PRODUCTTYPE");
            currDate = ClientUtil.getCurrentDate();
            lookUpHash.put(CommonConstants.PARAMFORQUERY, lookup_keys);
            keyValue = com.see.truetransact.clientutil.ClientUtil.populateLookupData(lookUpHash);
            getKeyValue((HashMap) keyValue.get("PRODUCTTYPE"));
            cbmProdType = new ComboBoxModel(key, value);
            cbmProdType.removeKeyAndElement("TD");
            cbmProdType.removeKeyAndElement("TL");
            cbmProdId = new ComboBoxModel();
            keyValue = null;
        } catch (Exception e) {
            parseException.logException(e, true);
        }
    }

    private void getKeyValue(HashMap keyValue) throws Exception {
        key = (ArrayList) keyValue.get(CommonConstants.KEY);
        value = (ArrayList) keyValue.get(CommonConstants.VALUE);
    }

    public void setCbmProdId(String prodType) {
        if (prodType.equals("GL")) {
            key = new ArrayList();
            value = new ArrayList();
        } else {
            try {
                lookUpHash = new HashMap();
                lookUpHash.put(CommonConstants.MAP_NAME, "Cash.getAccProduct" + prodType);
                lookUpHash.put(CommonConstants.PARAMFORQUERY, null);
                keyValue = ClientUtil.populateLookupData(lookUpHash);
                getKeyValue((HashMap) keyValue.get(CommonConstants.DATA));
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
        cbmProdId = new ComboBoxModel(key, value);
        this.cbmProdId = cbmProdId;
        setChanged();
    }

    public void setDepositInterestTableTitle() {
        tableTitle.add("SNo.");
        tableTitle.add("Emp Ref.No.");
        tableTitle.add("Member Name");
        tableTitle.add("Total Amount");
        tableTitle.add("Recovered Amount");
    }

    public void insertTableData(boolean newMode) {
        try {
            ArrayList rowList = new ArrayList();
            ArrayList tableList = new ArrayList();
            HashMap whereMap = new HashMap();
            List recoveryList = null;
            int sno = 1;
            whereMap.put("EMP_TYPE", getEmp_type());
            if (newMode) {
                recoveryList = ClientUtil.executeQuery("getRecoveryTallyData", whereMap);
            } else {
                whereMap.put("INT_CALC_UPTO_DT", setProperDtFormat(DateUtil.getDateMMDDYYYY(getTdtCalcIntUpto())));
                recoveryList = ClientUtil.executeQuery("getRecoveryTallyDataEdit", whereMap);
            }
            if (recoveryList != null && recoveryList.size() > 0) {
                for (int i = 0; i < recoveryList.size(); i++) {
                    whereMap = (HashMap) recoveryList.get(i);
                    rowList = new ArrayList();
                    rowList.add(String.valueOf(sno++));
                    if (i == 0) {
                        setTdtCalcIntUpto(CommonUtil.convertObjToStr(whereMap.get("INT_CALC_UPTO_DT")));
                    }
                    rowList.add(CommonUtil.convertObjToStr(whereMap.get("EMP_REF_NO")));
                    rowList.add(CommonUtil.convertObjToStr(whereMap.get("MEMBER_NAME")));
                    rowList.add(String.valueOf(df.format(CommonUtil.convertObjToDouble(whereMap.get("AMOUNT")).doubleValue())));
                    if (newMode) {
                        rowList.add("");
                    } else {
                        rowList.add(String.valueOf(df.format(CommonUtil.convertObjToDouble(whereMap.get("RECOVERED_AMOUNT")).doubleValue())));
                    }
                    tableList.add(rowList);
                }
            }
            tblSalaryRecoveryList = new EnhancedTableModel((ArrayList) tableList, tableTitle);
        } catch (Exception e) {
            e.printStackTrace();
            parseException.logException(e, true);
        }
    }

    private Date setProperDtFormat(Date dt) {
        Date tempDt = (Date) currDate.clone();
        if (dt != null) {
            tempDt.setDate(dt.getDate());
            tempDt.setMonth(dt.getMonth());
            tempDt.setYear(dt.getYear());
            return tempDt;
        }
        return null;
    }

    /**
     * To perform the necessary operation
     */
    public void doAction(boolean newMode) {
        TTException exception = null;
        log.info("In doAction()");
        try {
            final HashMap data = new HashMap();
            System.out.println("EMP_TYPE" + getEmp_type());
            data.put("EMP_TYPE", getEmp_type());
            data.put("CALC_INT_UPTO_DT", DateUtil.getDateMMDDYYYY(getTdtCalcIntUpto()));
            data.put("IMPORT_RECOVERY_LIST", "IMPORT_RECOVERY_LIST");
            if (newMode) {
                data.put("NEW_MODE", "NEW_MODE");
            } else {
                data.put("EDIT_MODE", "EDIT_MODE");
            }
            data.put(CommonConstants.USER_ID, TrueTransactMain.USER_ID);
            System.out.println("Data in RecoveryList Generation OB : " + data);
            HashMap proxyResultMap = proxy.execute(data, map);
            System.out.println("##################### proxyResultMap : " + proxyResultMap);
            setProxyReturnMap(proxyResultMap);
            setResult(getActionType());
        } catch (Exception e) {
            System.out.println("##$$$##$#$#$#$# Exception e : " + e);
            log.info("Error In doAction()");
            setResult(ClientConstants.ACTIONTYPE_FAILED);
            e.printStackTrace();
            if (e instanceof TTException) {
                exception = (TTException) e;
            }
        }
        if (exception != null) {
            parseException.logException(exception, true);
            setResult(actionType);
        }
    }

    /**
     * To perform the necessary action
     */
    public void doActionPerform() throws Exception {
        final HashMap data = new HashMap();
        if (getFinalMap() != null && getFinalMap().size() > 0) {
            data.put("RECOVERY_PROCESS_LIST", getFinalMap());
        }
        if (transactionDetailsTO == null) {
            transactionDetailsTO = new LinkedHashMap();
        }
        if (deletedTransactionDetailsTO != null) {
            transactionDetailsTO.put(DELETED_TRANS_TOs, deletedTransactionDetailsTO);
            deletedTransactionDetailsTO = null;
        }
        transactionDetailsTO.put(NOT_DELETED_TRANS_TOs, allowedTransactionDetailsTO);
        allowedTransactionDetailsTO = null;
        data.put("TransactionTO", transactionDetailsTO);
        data.put("BRANCH_CODE", TrueTransactMain.BRANCH_ID);
        data.put("CALC_INT_UPTO_DT", DateUtil.getDateMMDDYYYY(getTdtCalcIntUpto()));
        data.put(CommonConstants.USER_ID, TrueTransactMain.USER_ID);
        System.out.println("Data in RecoveryList Generation OB : " + data);
        HashMap proxyResultMap = proxy.execute(data, map);
        finalMap = null;
        System.out.println("##################### proxyResultMap : " + proxyResultMap);
        setProxyReturnMap(proxyResultMap);
        setResult(getActionType());
    }

    public void resetForm() {
        setTdtCalcIntUpto("");
        finalMap = null;
        resetTableValues();
        setChanged();
    }

    public void resetTableValues() {
        tblSalaryRecoveryList.setDataArrayList(null, tableTitle);
    }

    public java.util.List getFinalList() {
        return finalList;
    }

    public void setFinalList(java.util.List finalList) {
        this.finalList = finalList;
    }

    public int getResult() {
        return result;
    }

    public void setResult(int result) {
        this.result = result;
    }

    public int getActionType() {
        return actionType;
    }

    public void setActionType(int actionType) {
        this.actionType = actionType;
    }

    public java.util.ArrayList getTableTitle() {
        return tableTitle;
    }

    public com.see.truetransact.clientutil.EnhancedTableModel getTblSalaryRecoveryList() {
        return tblSalaryRecoveryList;
    }

    public void setTblSalaryRecoveryList(com.see.truetransact.clientutil.EnhancedTableModel tblSalaryRecoveryList) {
        this.tblSalaryRecoveryList = tblSalaryRecoveryList;
    }

    public EnhancedTableModel getTblproduct() {
        return tblproduct;
    }

    public void setTblproduct(EnhancedTableModel tblproduct) {
        this.tblproduct = tblproduct;
    }

    public java.lang.String getTdtCalcIntUpto() {
        return tdtCalcIntUpto;
    }

    public void setTdtCalcIntUpto(java.lang.String tdtCalcIntUpto) {
        this.tdtCalcIntUpto = tdtCalcIntUpto;
    }

    public java.util.HashMap getFinalMap() {
        return finalMap;
    }

    public void setFinalMap(java.util.HashMap finalMap) {
        this.finalMap = finalMap;
    }

    public java.lang.String getCboProdType() {
        return cboProdType;
    }

    public void setCboProdType(java.lang.String cboProdType) {
        this.cboProdType = cboProdType;
    }

    public com.see.truetransact.clientutil.ComboBoxModel getCbmProdId() {
        return cbmProdId;
    }

    public void setCbmProdId(com.see.truetransact.clientutil.ComboBoxModel cbmProdId) {
        this.cbmProdId = cbmProdId;
    }

    public com.see.truetransact.clientutil.ComboBoxModel getCbmProdType() {
        return cbmProdType;
    }

    public void setCbmProdType(com.see.truetransact.clientutil.ComboBoxModel cbmProdType) {
        this.cbmProdType = cbmProdType;
    }

    public java.lang.String getTxtAccountNo() {
        return txtAccountNo;
    }

    public void setTxtAccountNo(java.lang.String txtAccountNo) {
        this.txtAccountNo = txtAccountNo;
    }

    public com.see.truetransact.ui.common.transaction.TransactionOB getTransactionOB() {
        return transactionOB;
    }

    public void setTransactionOB(com.see.truetransact.ui.common.transaction.TransactionOB transactionOB) {
        this.transactionOB = transactionOB;
    }

    public java.util.LinkedHashMap getAllowedTransactionDetailsTO() {
        return allowedTransactionDetailsTO;
    }

    public void setAllowedTransactionDetailsTO(java.util.LinkedHashMap allowedTransactionDetailsTO) {
        this.allowedTransactionDetailsTO = allowedTransactionDetailsTO;
    }

    public String getEmp_type() {
        return emp_type;
    }

    public int getCharges() {
        return charges;
    }

    public void setCharges(int charges) {
        this.charges = charges;
    }

    public int getPenal() {
        return penal;
    }

    public void setPenal(int penal) {
        this.penal = penal;
    }

    public int getPenalInt() {
        return penalInt;
    }

    public void setPenalInt(int penalInt) {
        this.penalInt = penalInt;
    }

    public int getPrincipal() {
        return principal;
    }

    public void setPrincipal(int principal) {
        this.principal = principal;
    }

    public void setEmp_type(String emp_type) {
        this.emp_type = emp_type;
    }
}