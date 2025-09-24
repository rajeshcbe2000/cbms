/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition.
 * 
 *
 * HeadConsolidationOB.java
 *
 * Created on Wed Feb 02 12:57:50 IST 2015
 */
package com.see.truetransact.ui.payroll.voucherprocessing;

import com.see.truetransact.clientexception.ClientParseException;
import com.see.truetransact.clientproxy.ProxyFactory;
import com.see.truetransact.clientproxy.ProxyParameters;
import com.see.truetransact.clientutil.ClientConstants;
import com.see.truetransact.clientutil.ClientUtil;
import com.see.truetransact.clientutil.ComboBoxModel;
import com.see.truetransact.clientutil.EnhancedTableModel;
import com.see.truetransact.commonutil.CommonConstants;
import com.see.truetransact.commonutil.CommonUtil;
import com.see.truetransact.transferobject.payroll.voucherprocessing.PaymentVoucherTO;
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
public class HeadConsolidationOB extends CObservable {

    private HashMap operationMap;
    private HashMap lookUpHash;
    private HashMap keyValue;
    private ArrayList key;
    private ArrayList value;
    private ComboBoxModel cbmHeads;
    private int actionType;
    private int result;
    private String lblStatus = ClientConstants.ACTION_STATUS[ClientConstants.ACTIONTYPE_CANCEL];
    private final static ClientParseException parseException = ClientParseException.getInstance();
    private final static Logger log = Logger.getLogger(HeadConsolidationUI.class);
    private ProxyFactory proxy = null;
    private Date curDate = null;
    private ArrayList tblHeadList = new ArrayList();
    private EnhancedTableModel tbmPayHeads;
    private EnhancedTableModel tbmPayHeadsEdit;
    private EnhancedTableModel tbmPayHeadsDelete;
    private ArrayList tblTitle = new ArrayList();
    private String txtMapHead = "";
    private String accHead = "";
    private String payCode = "";
    PaymentVoucherTO objPaymentVoucherTO;
    java.util.ResourceBundle objHeadConsolidationRB = java.util.ResourceBundle.getBundle("com.see.truetransact.ui.payroll.voucherprocessing.HeadConsolidationRB", ProxyParameters.LANGUAGE);
    private static HeadConsolidationOB headConsolidationOB;

    static {
        try {
            log.info("In AgentOB Declaration");
            headConsolidationOB = new HeadConsolidationOB();
        } catch (Exception e) {
            parseException.logException(e, true);
        }
    }

    public static HeadConsolidationOB getInstance() {
        return headConsolidationOB;
    }

    /**
     * Creates a new instance of InwardClearingOB
     */
    public HeadConsolidationOB() throws Exception {
        objPaymentVoucherTO = new PaymentVoucherTO();
        curDate = ClientUtil.getCurrentDate();
        cbmHeads = new ComboBoxModel();
        initianSetup();
        fillDropdown();

    }

    private void setTblHeadTitile() throws Exception {
        try {
            tblTitle = new ArrayList();
            tblTitle.add("Account Head");
            tblTitle.add("Paycode");
        } catch (Exception e) {
            parseException.logException(e, true);
        }
    }

    private void initianSetup() throws Exception {
        log.info("In initianSetup()");
        setOperationMap();
        try {
            proxy = ProxyFactory.createProxy();
        } catch (Exception e) {
            parseException.logException(e, true);
        }
        setTblHeadTitile();
        tbmPayHeads = new EnhancedTableModel(null, tblTitle);
        tbmPayHeadsEdit = new EnhancedTableModel(null, tblTitle);
        tbmPayHeadsDelete = new EnhancedTableModel(null, tblTitle);
    }

    // Set the value of JNDI and the Session Bean...
    private void setOperationMap() throws Exception {
        log.info("In setOperationMap()");

        operationMap = new HashMap();
        operationMap.put(CommonConstants.JNDI, "HeadConsolidationJNDI");
        operationMap.put(CommonConstants.HOME, "payroll.voucherprocessing.HeadConsolidationHome");
        operationMap.put(CommonConstants.REMOTE, "payroll.voucherprocessing.HeadConsolidation");
    }

    //Do display the Data from the Database, in UI
    public void populateData(HashMap whereMap) {
        log.info("In populateData()");
        final HashMap mapData;
        try {
            mapData = proxy.executeQuery(whereMap, operationMap);
            populateOB(mapData);
        } catch (Exception e) {
            System.out.println("Error In populateData()");
            parseException.logException(e, true);
        }
    }

    private void populateOB(HashMap mapData) throws Exception {
        PaymentVoucherTO objPaymentVoucherTO = null;
        List headList = (List) mapData.get("PaymentVoucherTO");
        for (int i = 0; i < headList.size(); i++) {
            objPaymentVoucherTO = (PaymentVoucherTO) ((List) mapData.get("PaymentVoucherTO")).get(i);
            setHeadTO(objPaymentVoucherTO);
        }
        ttNotifyObservers();
    }

    // To Enter the values in the UI fields, from the database...
    private void setHeadTO(PaymentVoucherTO objPaymentVoucherTO) throws Exception {
        log.info("In setAgentTO()");
        setTxtMapHead(CommonUtil.convertObjToStr(objPaymentVoucherTO.getMapHead()));
        tblHeadList = new ArrayList();
        ArrayList tempList = new ArrayList();
        tempList.add(CommonUtil.convertObjToStr(objPaymentVoucherTO.getAccHead()));
        tempList.add(CommonUtil.convertObjToStr(objPaymentVoucherTO.getPayCode()));
        tblHeadList.addAll(tempList);
        tbmPayHeads.insertRow(tbmPayHeads.getRowCount(), tempList);
    }

    public void getOperativeProdId(String OAaccountNo) {

    }

    public void getDepositProdId(String depositNo) {
    }

    private PaymentVoucherTO setMapHead() {
        log.info("In setAgent()");
        try {
            objPaymentVoucherTO.setMapHead(getTxtMapHead());
        } catch (Exception e) {
            parseException.logException(e, true);
        }
        return objPaymentVoucherTO;
    }

    public void doAction(String command) {
        log.info("In doAction()---------");
        try {
            if (getCommand() != null) {
                doActionPerform();
            } //            }
            else {
                log.info("Action Type Not Defined In setChequeBookTO()");
            }
        } catch (Exception e) {
            log.info("Error In doAction()" + e);
            setResult(ClientConstants.ACTIONTYPE_FAILED);
            parseException.logException(e, true);
        }
    }

    /**
     * To perform the necessary action
     */
    private void doActionPerform() throws Exception {
        log.info("In doActionPerform()");
        final HashMap data = new HashMap();
        objPaymentVoucherTO.setCommand(getCommand());
        data.put("PayAccTO", setMapHead());
        ArrayList heads = getTbmPayHeads().getDataArrayList();
        ArrayList headsDelete = getTbmPayHeadsDelete().getDataArrayList();
        ArrayList headsEdit = getTbmPayHeadsEdit().getDataArrayList();
        data.put("mappingHeads", heads);
        data.put("mappingHeadsDelete", headsDelete);
        data.put("mappingHeadsEdit", headsEdit);
        HashMap proxyResultMap = proxy.execute(data, operationMap);
        setResult(actionType);
        setProxyReturnMap(proxyResultMap);
        resetForm();
    }

    // to decide which action Should be performed...
    private String getCommand() throws Exception {
        log.info("In getCommand()");
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
        setTdtApptDate("");
        setTxtRemarks("");
        setLblName("");
        //__ reset Auth Status...
        setAuthStatus("");
        setTxtMapHead("");

    }
    private String tdtApptDate = "";
    private String txtRemarks = "";
    private String lblName = "";
    private String authStatus = "";

    // Setter method for tdtApptDate
    void setTdtApptDate(String tdtApptDate) {
        this.tdtApptDate = tdtApptDate;
        setChanged();
    }
    // Getter method for tdtApptDate

    String getTdtApptDate() {
        return this.tdtApptDate;
    }

    // Setter method for txtRemarks
    void setTxtRemarks(String txtRemarks) {
        this.txtRemarks = txtRemarks;
        setChanged();
    }
    // Getter method for txtRemarks

    String getTxtRemarks() {
        return this.txtRemarks;
    }

    // Setter method for lblName
    void setLblName(String lblName) {
        this.lblName = lblName;
        setChanged();
    }
    // Getter method for lblName

    String getLblName() {
        return this.lblName;
    }

    public void setAgentTabData() {
    }

    /**
     * TO RESET THE TABLE...
     */
    public void resetTable() {
        try {
            int count = tbmPayHeads.getRowCount();
            for (int i = count; i > 0; i--) {
                tbmPayHeads.removeRow(i - 1);
            }
            int countDelete = tbmPayHeadsEdit.getRowCount();
            for (int i = countDelete; i > 0; i--) {
                tbmPayHeadsEdit.removeRow(i - 1);
            }
        } catch (Exception e) {
            setResult(ClientConstants.ACTIONTYPE_FAILED);
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
        try {
            HashMap map = new HashMap();

            lookUpHash = new HashMap();
            lookUpHash.put(CommonConstants.JNDI, "LookUpJNDI");
            lookUpHash.put(CommonConstants.HOME, "common.lookup.LookUpHome");
            lookUpHash.put(CommonConstants.REMOTE, "common.lookup.LookUp");

            HashMap where = new HashMap();
            where = null;
            map.put(CommonConstants.MAP_NAME, "getPayAcc");
            map.put(CommonConstants.PARAMFORQUERY, where);
            keyValue = ClientUtil.populateLookupData(map);
            getKeyValue((HashMap) keyValue.get(CommonConstants.DATA));
            cbmHeads = new ComboBoxModel(key, value);

        } catch (NullPointerException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void getKeyValue(HashMap keyValue) throws Exception {
        key = (ArrayList) keyValue.get(CommonConstants.KEY);
        value = (ArrayList) keyValue.get(CommonConstants.VALUE);
    }




    public com.see.truetransact.clientutil.ComboBoxModel getCbmHeads() {
        return cbmHeads;
    }

    public void setCbmHeads(com.see.truetransact.clientutil.ComboBoxModel cbmHeads) {
        this.cbmHeads = cbmHeads;
    }

    public void populateTblActData(HashMap mapData) {
        HashMap where = new HashMap();
        where.put("PAY_ACC_NO", CommonUtil.convertObjToStr(mapData.get("PAY_ACC_NO")));
        List count = ClientUtil.executeQuery("checkAlreadyMappedAccNumber", where);
        where = new HashMap();
        if (count != null && count.size() > 0) {
            where = (HashMap) count.get(0);
            int num = CommonUtil.convertObjToInt(where.get("COUNT"));
            if (num > 0) {
                ClientUtil.showAlertWindow("The account Number already used for mapping You can edit the data");
                return;
            } else {
                List payCode = ClientUtil.executeQuery("getPayCodesDed", mapData);
                HashMap tblPaycodes = (HashMap) payCode.get(0);
                tblHeadList = new ArrayList();
                ArrayList tempList = new ArrayList();
                ArrayList tblTempList = new ArrayList();
                tempList.add(CommonUtil.convertObjToStr(mapData.get("PAY_ACC_NO")));
                tempList.add(CommonUtil.convertObjToStr(tblPaycodes.get("PAY_CODE")));
                tblHeadList.addAll(tempList);
                if (getActionType() == ClientConstants.ACTIONTYPE_EDIT) {
                    tbmPayHeadsEdit.insertRow(tbmPayHeadsEdit.getRowCount(), tempList);
                }
                tbmPayHeads.insertRow(tbmPayHeads.getRowCount(), tempList);
            }
        }
    }

    public void deleteRowFromTbmPayHeads(HashMap mapData) {
        if (getActionType() == ClientConstants.ACTIONTYPE_EDIT) {
            tblHeadList = new ArrayList();
            ArrayList tempList = new ArrayList();
            ArrayList tblTempList = new ArrayList();
            tempList.add(CommonUtil.convertObjToStr(mapData.get("PAY_ACC_NO")));
            tempList.add(CommonUtil.convertObjToStr(mapData.get("PAY_CODE")));
            tblHeadList.addAll(tempList);
            tbmPayHeadsDelete.insertRow(tbmPayHeadsDelete.getRowCount(), tempList);
        }
        tbmPayHeads.removeRow(CommonUtil.convertObjToInt(mapData.get("ROW_SELECTED")));
    }

    public void deletedRowEditTbmPayHeads(HashMap mapData) {
        //tbmPayHeads.removeRow(CommonUtil.convertObjToInt(mapData.get("ROW_SELECTED")));
        System.out.println("row delete" + CommonUtil.convertObjToInt(mapData.get("ROW_SELECTED")));
    }

    public EnhancedTableModel getTbmPayHeads() {
        return tbmPayHeads;
    }

    public void setTbmPayHeads(EnhancedTableModel tbmPayHeads) {
        this.tbmPayHeads = tbmPayHeads;
        setChanged();
    }

    public String getTxtMapHead() {
        return txtMapHead;
    }

    public void setTxtMapHead(String txtMapHead) {
        this.txtMapHead = txtMapHead;
    }

    public String getAccHead() {
        return accHead;
    }

    public void setAccHead(String accHead) {
        this.accHead = accHead;
    }

    public String getPayCode() {
        return payCode;
    }

    public void setPayCode(String payCode) {
        this.payCode = payCode;
    }

    public EnhancedTableModel getTbmPayHeadsEdit() {
        return tbmPayHeadsEdit;
    }

    public void setTbmPayHeadsEdit(EnhancedTableModel tbmPayHeadsEdit) {
        this.tbmPayHeadsEdit = tbmPayHeadsEdit;
    }

    public EnhancedTableModel getTbmPayHeadsDelete() {
        return tbmPayHeadsDelete;
    }

    public void setTbmPayHeadsDelete(EnhancedTableModel tbmPayHeadsDelete) {
        this.tbmPayHeadsDelete = tbmPayHeadsDelete;
    }
}