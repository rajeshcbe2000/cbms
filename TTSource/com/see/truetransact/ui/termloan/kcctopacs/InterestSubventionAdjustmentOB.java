/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition.
 * 
 *
 * InterestSubventionAdjustmentOB.java
 *
 * Created on Tue Oct 18 12:40:45 IST 2011
 */
package com.see.truetransact.ui.termloan.kcctopacs;

import java.util.Observable;
import java.util.HashMap;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.ArrayList;
import java.util.List;
import org.apache.log4j.Logger;
import com.see.truetransact.commonutil.DateUtil;
import com.see.truetransact.clientutil.ComboBoxModel;
import com.see.truetransact.commonutil.CommonConstants;
import com.see.truetransact.clientproxy.ProxyFactory;
import com.see.truetransact.commonutil.CommonUtil;
import com.see.truetransact.clientutil.ClientConstants;
import com.see.truetransact.clientexception.ClientParseException;
import com.see.truetransact.clientutil.ClientUtil;
import com.see.truetransact.ui.TrueTransactMain;
import com.see.truetransact.uicomponent.CObservable;
import com.see.truetransact.clientproxy.ProxyParameters;
import com.see.truetransact.transferobject.termloan.kcctopacs.InterestSubventionAdjustmentTO;
import com.see.truetransact.clientutil.EnhancedTableModel;
import com.see.truetransact.ui.common.transaction.TransactionOB;

/**
 *
 * @author Suresh R
 */
public class InterestSubventionAdjustmentOB extends CObservable {

    private String lblStatus = ClientConstants.ACTION_STATUS[ClientConstants.ACTIONTYPE_CANCEL];
    private int actionType;
    private int result;
    private HashMap oldTransDetMap = new HashMap();
    private TransactionOB transactionOB;
    private LinkedHashMap allowedTransactionDetailsTO = null;
    private LinkedHashMap transactionDetailsTO = null;
    private LinkedHashMap deletedTransactionDetailsTO = null;
    private final String DELETED_TRANS_TOs = "DELETED_TRANS_TOs";
    private final String NOT_DELETED_TRANS_TOs = "NOT_DELETED_TRANS_TOs";
    private ArrayList IncVal = new ArrayList();
    private HashMap _authorizeMap;
    private ProxyFactory proxy;
    private HashMap map;
    private List finalList = null;
    private final static Logger log = Logger.getLogger(InterestSubventionAdjustmentOB.class);
    private final static ClientParseException parseException = ClientParseException.getInstance();
    private HashMap lookupMap;
    private HashMap param;
    private HashMap keyValue;
    private ArrayList key;
    private ArrayList value;
    private ComboBoxModel cbmAgencyName;
    private InterestSubventionAdjustmentTO objInterestSubventionAdjustmentTO;
    private String txtReleaseRefNo = "";
    private String tdtReleaseDate = "";
    private String txtFinancialYear = "";
    private String txtFinancialYearEnd = "";
    private String txtClaimedAmount = "";
    private String txtReceivedAmount = "";
    private String txtCustomerID = "";
    private String subventionAdjustNo = "";
    private Date currDt = null;

    /**
     * Creates a new instance of TDS MiantenanceOB
     */
    public InterestSubventionAdjustmentOB() {
        try {
            currDt = ClientUtil.getCurrentDate();
            proxy = ProxyFactory.createProxy();
            map = new HashMap();
            map.put(CommonConstants.JNDI, "InterestSubventionAdjustmentJNDI");
            map.put(CommonConstants.HOME, "InterestSubventionAdjustmentJNDIHome");
            map.put(CommonConstants.REMOTE, "InterestSubventionAdjustment");
            fillDropdown();
        } catch (Exception e) {
            parseException.logException(e, true);
        }
    }

    private void fillDropdown() throws Exception {
        try {
            lookupMap = new HashMap();
            lookupMap.put(CommonConstants.JNDI, "LookUpJNDI");
            lookupMap.put(CommonConstants.HOME, "common.lookup.LookUpHome");
            lookupMap.put(CommonConstants.REMOTE, "common.lookup.LookUp");
            lookupMap.put(CommonConstants.MAP_NAME, null);
            final ArrayList lookup_keys = new ArrayList();
            lookup_keys.add("SUBSIDY.INSTITUTIONS");
            lookupMap.put(CommonConstants.PARAMFORQUERY, lookup_keys);
            keyValue = com.see.truetransact.clientutil.ClientUtil.populateLookupData(lookupMap);
            getKeyValue((HashMap) keyValue.get("SUBSIDY.INSTITUTIONS"));
            cbmAgencyName = new ComboBoxModel(key, value);
            makeNull();
        } catch (NullPointerException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * To get data for comboboxes
     */
    private HashMap populateData(HashMap obj) throws Exception {
        keyValue = proxy.executeQuery(obj, lookupMap);
        log.info("Got HashMap");
        return keyValue;
    }

    private void fillData(HashMap keyValue) throws Exception {
        key = (ArrayList) keyValue.get("KEY");
        value = (ArrayList) keyValue.get("VALUE");
    }

    private void getKeyValue(HashMap keyValue) throws Exception {
        key = (ArrayList) keyValue.get(CommonConstants.KEY);
        value = (ArrayList) keyValue.get(CommonConstants.VALUE);
    }

    private void makeNull() {
        key = null;
        value = null;
    }

    private String getCommand() {
        String command = null;
        System.out.println("actionType : " + actionType);
        switch (actionType) {
            case ClientConstants.ACTIONTYPE_NEW:
                command = CommonConstants.TOSTATUS_INSERT;
                break;
            case ClientConstants.ACTIONTYPE_EDIT:
                command = CommonConstants.TOSTATUS_UPDATE;
                break;
            case ClientConstants.ACTIONTYPE_DELETE:
                command = CommonConstants.TOSTATUS_DELETE;
                break;
            case ClientConstants.ACTIONTYPE_AUTHORIZE:
                command = CommonConstants.STATUS_AUTHORIZED;
                break;
            case ClientConstants.ACTIONTYPE_REJECT:
                command = CommonConstants.STATUS_REJECTED;
                break;
            case ClientConstants.ACTIONTYPE_EXCEPTION:
                command = CommonConstants.STATUS_EXCEPTION;
            default:
        }
        return command;
    }

    private String getAction() {
        String action = null;
        switch (actionType) {
            case ClientConstants.ACTIONTYPE_NEW:
                action = CommonConstants.STATUS_CREATED;
                break;
            case ClientConstants.ACTIONTYPE_EDIT:
                action = CommonConstants.STATUS_MODIFIED;
                break;
            case ClientConstants.ACTIONTYPE_DELETE:
                action = CommonConstants.STATUS_DELETED;
                break;
            case ClientConstants.ACTIONTYPE_AUTHORIZE:
                action = CommonConstants.STATUS_AUTHORIZED;
                break;
            case ClientConstants.ACTIONTYPE_REJECT:
                action = CommonConstants.STATUS_REJECTED;
                break;
            case ClientConstants.ACTIONTYPE_EXCEPTION:
                action = CommonConstants.STATUS_EXCEPTION;
                break;
            default:
        }
        return action;
    }

    public ComboBoxModel getCbmAgencyName() {
        return cbmAgencyName;
    }

    public void setCbmAgencyName(ComboBoxModel cbmAgencyName) {
        this.cbmAgencyName = cbmAgencyName;
    }
    private String cboAgencyName = "";

    public String getCboAgencyName() {
        return cboAgencyName;
    }

    public void setCboAgencyName(String cboAgencyName) {
        this.cboAgencyName = cboAgencyName;
    }

    public String getSubventionAdjustNo() {
        return subventionAdjustNo;
    }

    public void setSubventionAdjustNo(String subventionAdjustNo) {
        this.subventionAdjustNo = subventionAdjustNo;
    }

    public String getTdtReleaseDate() {
        return tdtReleaseDate;
    }

    public void setTdtReleaseDate(String tdtReleaseDate) {
        this.tdtReleaseDate = tdtReleaseDate;
    }

    public String getTxtClaimedAmount() {
        return txtClaimedAmount;
    }

    public void setTxtClaimedAmount(String txtClaimedAmount) {
        this.txtClaimedAmount = txtClaimedAmount;
    }

    public String getTxtCustomerID() {
        return txtCustomerID;
    }

    public void setTxtCustomerID(String txtCustomerID) {
        this.txtCustomerID = txtCustomerID;
    }

    public String getTxtFinancialYear() {
        return txtFinancialYear;
    }

    public void setTxtFinancialYear(String txtFinancialYear) {
        this.txtFinancialYear = txtFinancialYear;
    }

    public String getTxtFinancialYearEnd() {
        return txtFinancialYearEnd;
    }

    public void setTxtFinancialYearEnd(String txtFinancialYearEnd) {
        this.txtFinancialYearEnd = txtFinancialYearEnd;
    }

    public String getTxtReceivedAmount() {
        return txtReceivedAmount;
    }

    public void setTxtReceivedAmount(String txtReceivedAmount) {
        this.txtReceivedAmount = txtReceivedAmount;
    }

    public String getTxtReleaseRefNo() {
        return txtReleaseRefNo;
    }

    public void setTxtReleaseRefNo(String txtReleaseRefNo) {
        this.txtReleaseRefNo = txtReleaseRefNo;
    }

    private void populateIntSubventionDetails(InterestSubventionAdjustmentTO objInterestSubventionAdjustmentTO) throws Exception {
        setSubventionAdjustNo(CommonUtil.convertObjToStr(objInterestSubventionAdjustmentTO.getAdjustmentNo()));
        setCboAgencyName(CommonUtil.convertObjToStr(objInterestSubventionAdjustmentTO.getAgencyName()));
        setTxtCustomerID(CommonUtil.convertObjToStr(objInterestSubventionAdjustmentTO.getCustId()));
        setTxtReleaseRefNo(CommonUtil.convertObjToStr(objInterestSubventionAdjustmentTO.getReleaseRefNo()));
        setTdtReleaseDate(CommonUtil.convertObjToStr(objInterestSubventionAdjustmentTO.getReleaseDate()));
        setTxtFinancialYear(CommonUtil.convertObjToStr(objInterestSubventionAdjustmentTO.getStartFinYear()));
        setTxtFinancialYearEnd(CommonUtil.convertObjToStr(objInterestSubventionAdjustmentTO.getEndFinYear()));
        setTxtClaimedAmount(CommonUtil.convertObjToStr(objInterestSubventionAdjustmentTO.getClaimedAmount()));
        setTxtReceivedAmount(CommonUtil.convertObjToStr(objInterestSubventionAdjustmentTO.getReceivedAmount()));
    }

    public void getData(HashMap whereMap) {
        try {
            final HashMap data = (HashMap) proxy.executeQuery(whereMap, map);
            System.out.println("#### After DAO Data : " + data);
            HashMap resultMap = new HashMap();
            if (data.containsKey("TRANSFER_TRANS_LIST")) {
                List transList = (List) data.get("TRANSFER_TRANS_LIST");
                resultMap.put("TRANSFER_TRANS_LIST", transList);
                setProxyReturnMap(resultMap);
            }
            if (data.containsKey("TRANSACTION_LIST")) {
                List list = (List) data.get("TRANSACTION_LIST");
                if (!list.isEmpty()) {
                    transactionOB.setDetails(list);
                }
            }
            if (data.containsKey("SubventionInterestData")) {
                objInterestSubventionAdjustmentTO = (InterestSubventionAdjustmentTO) ((List) data.get("SubventionInterestData")).get(0);
                populateIntSubventionDetails(objInterestSubventionAdjustmentTO);
            }
        } catch (Exception e) {
            parseException.logException(e, true);
            e.printStackTrace();
        }
    }

    /**
     * To perform the appropriate operation
     */
    public void doAction() {
        try {
            doActionPerform();
        } catch (Exception e) {
            setResult(ClientConstants.ACTIONTYPE_FAILED);
            parseException.logException(e, true);
            e.printStackTrace();
        }
    }

    /**
     * To perform the necessary action
     */
    private void doActionPerform() throws Exception {
        final HashMap data = new HashMap();
        data.put("COMMAND", getCommand());
        if (getActionType() == ClientConstants.ACTIONTYPE_AUTHORIZE || getActionType() == ClientConstants.ACTIONTYPE_REJECT) {
            data.put(CommonConstants.AUTHORIZEMAP, get_authorizeMap());
        }
        data.put("SubventionInterestData", setSubventionInterestData());
        if (allowedTransactionDetailsTO != null && allowedTransactionDetailsTO.size() > 0) {
            if (transactionDetailsTO == null) {
                transactionDetailsTO = new LinkedHashMap();
            }
            transactionDetailsTO.put(NOT_DELETED_TRANS_TOs, allowedTransactionDetailsTO);
            data.put("TransactionTO", transactionDetailsTO);
            allowedTransactionDetailsTO = null;
        }
        if (deletedTransactionDetailsTO != null) {
            transactionDetailsTO.put(DELETED_TRANS_TOs, deletedTransactionDetailsTO);
            deletedTransactionDetailsTO = null;
        }
        data.put("BRANCH_CODE", TrueTransactMain.BRANCH_ID);
        System.out.println("#$########## data : " + data);
        HashMap proxyResultMap = proxy.execute(data, map);
        setProxyReturnMap(proxyResultMap);
        setResult(actionType);
        _authorizeMap = null;
    }

    public InterestSubventionAdjustmentTO setSubventionInterestData() {
        final InterestSubventionAdjustmentTO objInterestSubventionAdjustmentTO = new InterestSubventionAdjustmentTO();
        try {
            objInterestSubventionAdjustmentTO.setAdjustmentNo(getSubventionAdjustNo());
            objInterestSubventionAdjustmentTO.setAgencyName(getCboAgencyName());
            objInterestSubventionAdjustmentTO.setCustId(getTxtCustomerID());
            objInterestSubventionAdjustmentTO.setReleaseRefNo(getTxtReleaseRefNo());
            objInterestSubventionAdjustmentTO.setReleaseDate(DateUtil.getDateMMDDYYYY(getTdtReleaseDate()));
            objInterestSubventionAdjustmentTO.setStartFinYear(CommonUtil.convertObjToDouble(getTxtFinancialYear()));
            objInterestSubventionAdjustmentTO.setEndFinYear(CommonUtil.convertObjToDouble(getTxtFinancialYearEnd()));
            objInterestSubventionAdjustmentTO.setClaimedAmount(CommonUtil.convertObjToDouble(getTxtClaimedAmount()));
            objInterestSubventionAdjustmentTO.setReceivedAmount(CommonUtil.convertObjToDouble(getTxtReceivedAmount()));
            objInterestSubventionAdjustmentTO.setStatus(getAction());
            objInterestSubventionAdjustmentTO.setStatusBy(TrueTransactMain.USER_ID);
            objInterestSubventionAdjustmentTO.setStatusDt(ClientUtil.getCurrentDateWithTime());
        } catch (Exception e) {
            log.info("Error In setSubventionInterestData()");
            e.printStackTrace();
        }
        return objInterestSubventionAdjustmentTO;
    }

    public void resetForm() {
        setSubventionAdjustNo("");
        setCboAgencyName("");
        setTxtCustomerID("");
        setTxtReleaseRefNo("");
        setTdtReleaseDate("");
        setTxtFinancialYear("");
        setTxtFinancialYearEnd("");
        setTxtClaimedAmount("");
        setTxtReceivedAmount("");
        setChanged();
    }

    /**
     * Getter for property lblStatus.
     *
     * @return Value of property lblStatus.
     */
    public java.lang.String getLblStatus() {
        return lblStatus;
    }

    /**
     * Setter for property lblStatus.
     *
     * @param lblStatus New value of property lblStatus.
     */
    public void setLblStatus(java.lang.String lblStatus) {
        this.lblStatus = lblStatus;
    }

    /**
     * Getter for property actionType.
     *
     * @return Value of property actionType.
     */
    public int getActionType() {
        return actionType;
    }

    /**
     * Setter for property actionType.
     *
     * @param actionType New value of property actionType.
     */
    public void setActionType(int actionType) {
        this.actionType = actionType;
    }

    /**
     * Getter for property result.
     *
     * @return Value of property result.
     */
    public int getResult() {
        return result;
    }

    /**
     * Setter for property result.
     *
     * @param result New value of property result.
     */
    public void setResult(int result) {
        this.result = result;
    }

    /**
     * Getter for property _authorizeMap.
     *
     * @return Value of property _authorizeMap.
     */
    public java.util.HashMap get_authorizeMap() {
        return _authorizeMap;
    }

    /**
     * Setter for property _authorizeMap.
     *
     * @param _authorizeMap New value of property _authorizeMap.
     */
    public void set_authorizeMap(java.util.HashMap _authorizeMap) {
        this._authorizeMap = _authorizeMap;
    }

    /**
     * To set the status based on ActionType, either New, Edit, etc.,
     */
    public void setStatus() {
        this.setLblStatus(ClientConstants.ACTION_STATUS[this.getActionType()]);
    }

    /**
     * To update the Status based on result performed by doAction() method
     */
    public void setResultStatus() {
        this.setLblStatus(ClientConstants.RESULT_STATUS[this.getResult()]);
    }

    /**
     * Getter for property allowedTransactionDetailsTO.
     *
     * @return Value of property allowedTransactionDetailsTO.
     */
    public java.util.LinkedHashMap getAllowedTransactionDetailsTO() {
        return allowedTransactionDetailsTO;
    }

    /**
     * Setter for property allowedTransactionDetailsTO.
     *
     * @param allowedTransactionDetailsTO New value of property
     * allowedTransactionDetailsTO.
     */
    public void setAllowedTransactionDetailsTO(java.util.LinkedHashMap allowedTransactionDetailsTO) {
        this.allowedTransactionDetailsTO = allowedTransactionDetailsTO;
    }

    /**
     * Getter for property oldTransDetMap.
     *
     * @return Value of property oldTransDetMap.
     */
    public java.util.HashMap getOldTransDetMap() {
        return oldTransDetMap;
    }

    /**
     * Setter for property oldTransDetMap.
     *
     * @param oldTransDetMap New value of property oldTransDetMap.
     */
    public void setOldTransDetMap(java.util.HashMap oldTransDetMap) {
        this.oldTransDetMap = oldTransDetMap;
    }

    /**
     * Getter for property transactionDetailsTO.
     *
     * @return Value of property transactionDetailsTO.
     */
    public java.util.LinkedHashMap getTransactionDetailsTO() {
        return transactionDetailsTO;
    }

    /**
     * Setter for property transactionDetailsTO.
     *
     * @param transactionDetailsTO New value of property transactionDetailsTO.
     */
    public void setTransactionDetailsTO(java.util.LinkedHashMap transactionDetailsTO) {
        this.transactionDetailsTO = transactionDetailsTO;
    }

    /**
     * Getter for property deletedTransactionDetailsTO.
     *
     * @return Value of property deletedTransactionDetailsTO.
     */
    public java.util.LinkedHashMap getDeletedTransactionDetailsTO() {
        return deletedTransactionDetailsTO;
    }

    /**
     * Setter for property deletedTransactionDetailsTO.
     *
     * @param deletedTransactionDetailsTO New value of property
     * deletedTransactionDetailsTO.
     */
    public void setDeletedTransactionDetailsTO(java.util.LinkedHashMap deletedTransactionDetailsTO) {
        this.deletedTransactionDetailsTO = deletedTransactionDetailsTO;
    }

    /**
     * Getter for property transactionOB.
     *
     * @return Value of property transactionOB.
     */
    public com.see.truetransact.ui.common.transaction.TransactionOB getTransactionOB() {
        return transactionOB;
    }

    /**
     * Setter for property transactionOB.
     *
     * @param transactionOB New value of property transactionOB.
     */
    public void setTransactionOB(com.see.truetransact.ui.common.transaction.TransactionOB transactionOB) {
        this.transactionOB = transactionOB;
    }
}