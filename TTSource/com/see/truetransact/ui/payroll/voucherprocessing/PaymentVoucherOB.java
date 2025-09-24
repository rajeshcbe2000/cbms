/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition.
 * 
 *
 * PaymentVoucherOB.java
 *
 * Created on Wed Feb 02 12:57:50 IST 2015
 */
package com.see.truetransact.ui.payroll.voucherprocessing;

import com.see.truetransact.clientexception.ClientParseException;
import com.see.truetransact.clientproxy.ProxyFactory;
import com.see.truetransact.clientproxy.ProxyParameters;
import com.see.truetransact.clientutil.ClientConstants;
import com.see.truetransact.clientutil.ClientUtil;
import com.see.truetransact.clientutil.EnhancedTableModel;
import com.see.truetransact.commonutil.CommonConstants;
import com.see.truetransact.commonutil.CommonUtil;
import com.see.truetransact.commonutil.DateUtil;
import com.see.truetransact.transferobject.payroll.voucherprocessing.PaymentVoucherTO;
import com.see.truetransact.ui.agent.AgentUI;
import com.see.truetransact.uicomponent.CObservable;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import org.apache.log4j.Logger;

/**
 *
 * @author shihad
 */
public class PaymentVoucherOB extends CObservable {

    private HashMap operationMap;
    private HashMap lookUpHash;
    private HashMap keyValue;
    private ArrayList key;
    private ArrayList value;
    private int actionType;
    private int result;
    private String lblStatus = ClientConstants.ACTION_STATUS[ClientConstants.ACTIONTYPE_CANCEL];
    private final static ClientParseException parseException = ClientParseException.getInstance();
    private final static Logger log = Logger.getLogger(AgentUI.class);
    private ProxyFactory proxy = null;
    private Date curDate = null;
    private Date monthYear = null;
    private ArrayList tblHeadList = new ArrayList();
    private EnhancedTableModel tbmHeadAmt;
    private ArrayList tblTitle = new ArrayList();
    PaymentVoucherTO objPaymentVoucherTO;
    java.util.ResourceBundle objPaymentVoucherRB = java.util.ResourceBundle.getBundle("com.see.truetransact.ui.payroll.voucherprocessing.PaymentVoucherRB", ProxyParameters.LANGUAGE);
    private static PaymentVoucherOB paymentVoucherProcessingOB;
    private String authStatus = "";

    static {
        try {
            log.info("In PaymentVoucherOB Declaration");
            paymentVoucherProcessingOB = new PaymentVoucherOB();
        } catch (Exception e) {
            e.printStackTrace();
            parseException.logException(e, true);
        }
    }

    public static PaymentVoucherOB getInstance() {
        return paymentVoucherProcessingOB;
    }

    /**
     * Creates a new instance of PaymentVoucherOB
     */
    public PaymentVoucherOB() throws Exception {
        objPaymentVoucherTO = new PaymentVoucherTO();
        curDate = ClientUtil.getCurrentDate();
        initianSetup();
        fillDropdown();
    }

    private void setTblHeadTitile() throws Exception {
        try {
            tblTitle = new ArrayList();
            tblTitle.add("Account Head");
            tblTitle.add("Amount");
            tblTitle.add("AcHd Name");
        } catch (Exception e) {
            e.printStackTrace();
            parseException.logException(e, true);
        }
    }

    private void initianSetup() throws Exception {
        log.info("In initianSetup()");
        setOperationMap();
        try {
            proxy = ProxyFactory.createProxy();
        } catch (Exception e) {
            e.printStackTrace();
            parseException.logException(e, true);
        }
        setTblHeadTitile();
        tbmHeadAmt = new EnhancedTableModel(null, tblTitle);
    }

    // Set the value of JNDI and the Session Bean...
    private void setOperationMap() throws Exception {
        log.info("In setOperationMap()");
        operationMap = new HashMap();
        operationMap.put(CommonConstants.JNDI, "PaymentVoucherJNDI");
        operationMap.put(CommonConstants.HOME, "sysadmin.voucherprocessing.PaymentVoucherHome");
        operationMap.put(CommonConstants.REMOTE, "sysadmin.voucherprocessing.PaymentVoucher");
    }

    //Do display the Data from the Database, in UI
    public void populateData(HashMap whereMap) {
        log.info("In populateData()");
    }

    private void populateOB(HashMap mapData) throws Exception {
    }

    private PaymentVoucherTO setPaymentTO() {
        log.info("In setAgent()");
        try {
            objPaymentVoucherTO.setMonthYr(getMonthYear());
            objPaymentVoucherTO.setBranchId(ProxyParameters.BRANCH_ID);
            objPaymentVoucherTO.setCreatedBy(ProxyParameters.USER_ID);
        } catch (Exception e) {
            e.printStackTrace();
            parseException.logException(e, true);
        }
        return objPaymentVoucherTO;
    }

    public void doAction(String command) {
        log.info("In doAction()---------");
        try {
            //If actionType such as NEW, EDIT, DELETE, then proceed
            //If actionType has got propervalue then doActionPerform, else throw error
            if (getCommand() != null) {
                doActionPerform();
            } //            }
            else {
                log.info("Action Type Not Defined In setChequeBookTO()");
            }
        } catch (Exception e) {
            log.info("Error In doAction()" + e);
            setResult(ClientConstants.ACTIONTYPE_FAILED);
            e.printStackTrace();
            parseException.logException(e, true);
        }
    }

    /**
     * To perform the necessary action
     */
    private void doActionPerform() throws Exception {
        log.info("In doActionPerform()");
        final HashMap data = new HashMap();
        ArrayList headsAmt = new ArrayList();
        objPaymentVoucherTO.setCommand(getCommand());
        data.put("AccHeadsTO", setPaymentTO());
        headsAmt = getTbmHeadAmt().getDataArrayList();
        data.put("AccHeadsAmtList", headsAmt);
        HashMap proxyResultMap = proxy.execute(data, operationMap);
        setResult(actionType);
        setProxyReturnMap(proxyResultMap);
        resetForm();
    }

    // to decide which action Should be performed...
    private String getCommand() throws Exception {
        log.info("In getCommand()");
        System.out.println("action type " + actionType);
        String command = null;
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
            default:
        }
        return command;
    }

    // Returns the Current Value of Action type...
    public int getActionType() {
        return actionType;
    }

    // Sets the value of the Action Type to the poperation we want to execute...
    public void setActionType(int actionType) {
        this.actionType = actionType;
        setChanged();
    }

    // To set and change the Status of the lable STATUS
    public void setResult(int result) {
        this.result = result;
        setChanged();
    }

    public int getResult() {
        return this.result;
    }

    // To set the Value of the lblStatus...
    public void setLblStatus(String lblStatus) {
        this.lblStatus = lblStatus;
        setChanged();
    }

    public String getLblStatus() {
        return lblStatus;
    }

    //To reset the Value of lblStatus after each save action...
    public void resetStatus() {
        this.setLblStatus(ClientConstants.ACTION_STATUS[0]);
    }

    /**
     * To set the status based on ActionType, either New, Edit, etc.,
     */
    public void setStatus() {
        this.setLblStatus(ClientConstants.ACTION_STATUS[this.getActionType()]);
        ttNotifyObservers();
    }

    /**
     * To update the Status based on result performed by doAction() method
     */
    public void setResultStatus() {
        this.setLblStatus(ClientConstants.RESULT_STATUS[this.getResult()]);
        ttNotifyObservers();
    }

    public void ttNotifyObservers() {
        setChanged();
        notifyObservers();
    }

    public void resetForm() {
        //__ reset Auth Status...
        setAuthStatus("");
        setMonthYear(null);

    }

    /**
     * TO RESET THE TABLE...
     */
    public void resetTable() {
        try {
            int count = tbmHeadAmt.getRowCount();
            for (int i = count; i > 0; i--) {
                tbmHeadAmt.removeRow(i - 1);
            }
        } catch (Exception e) {
            setResult(ClientConstants.ACTIONTYPE_FAILED);
            e.printStackTrace();
            log.info("Error in resetTable():");
        }
    }

    // Setter method for authStatus
    void setAuthStatus(String authStatus) {
        this.authStatus = authStatus;
        setChanged();
    }
    // Getter method for authStatus

    String getAuthStatus() {
        return this.authStatus;
    }

    public void fillDropdown() {
    }

    private void getKeyValue(HashMap keyValue) throws Exception {
        key = (ArrayList) keyValue.get(CommonConstants.KEY);
        value = (ArrayList) keyValue.get(CommonConstants.VALUE);
    }

    public void populateTblActAmtData(HashMap mapData) {
        HashMap where = new HashMap();
        where.put("MONTH_YEAR", DateUtil.getDateMMDDYYYY(CommonUtil.convertObjToStr(mapData.get("MONTH_YEAR"))));
        List payVoucher = ClientUtil.executeQuery("getPaymentVouchers", where);
        if (payVoucher != null && payVoucher.size() > 0) {
            for (int i = 0; i < payVoucher.size(); i++) {
                where = new HashMap();
                where = (HashMap) payVoucher.get(i);
                System.out.println("where here" + where);
                tblHeadList = new ArrayList();
                ArrayList tempList = new ArrayList();
                tempList.add(CommonUtil.convertObjToStr(where.get("PAY_ACC_NO")));
                tempList.add(CommonUtil.convertObjToStr(where.get("TOTAL")));
                tempList.add(CommonUtil.convertObjToStr(where.get("AC_NAME")));
                System.out.println("templist here" + tempList);
                tblHeadList.addAll(tempList);
                tbmHeadAmt.insertRow(tbmHeadAmt.getRowCount(), tempList);
            }
        }
    }

    public EnhancedTableModel getTbmPayHeads() {
        return tbmHeadAmt;
    }

    public void setTbmPayHeads(EnhancedTableModel tbmPayHeads) {
        this.tbmHeadAmt = tbmPayHeads;
        setChanged();
    }

    public EnhancedTableModel getTbmHeadAmt() {
        return tbmHeadAmt;
    }

    public void setTbmHeadAmt(EnhancedTableModel tbmHeadAmt) {
        this.tbmHeadAmt = tbmHeadAmt;
    }

    public Date getMonthYear() {
        return monthYear;
    }

    public void setMonthYear(Date monthYear) {
        this.monthYear = monthYear;
    }
}