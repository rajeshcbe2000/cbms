/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition.
 * 
 *
 * IncrementOB.java
 *
 * Created on Fri Nov 14 10:00:00 IST 2014
 */
package com.see.truetransact.ui.payroll.increment;

import com.see.truetransact.clientutil.ClientConstants;
import com.see.truetransact.clientutil.ClientUtil;
import com.see.truetransact.commonutil.CommonConstants;
import com.see.truetransact.commonutil.DateUtil;
import com.see.truetransact.commonutil.CommonUtil;
import com.see.truetransact.clientexception.ClientParseException;
import com.see.truetransact.clientproxy.ProxyFactory;
import com.see.truetransact.uicomponent.CObservable;
import com.see.truetransact.clientutil.EnhancedTableModel;
import com.see.truetransact.clientutil.ComboBoxModel;
import com.see.truetransact.clientproxy.ProxyParameters;
import com.see.truetransact.transferobject.payroll.increment.IncrementTO;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import org.apache.log4j.Logger;

/**
 *
 * @author shihad
 */
public class IncrementOB extends CObservable {

    private HashMap operationMap;
    private HashMap lookUpHash;
    private HashMap keyValue;
    private ArrayList key;
    private ArrayList value;
    private ComboBoxModel cbmDesig;
    private String txtEmpId = "", txtPresBasicSalary = "", txtLastIncrDate = "", cboDesig = "", txtNewBasicSalary = "",
            txtEmpName = "", txtDesig = "", txtNumberIncr = "", tdtNewIncrDate = "", txtNextIncrDate = "", scaleId = "",
            incrId = "",versionNo = "";
    private int actionType;
    private int result;
    private String lblStatus = ClientConstants.ACTION_STATUS[ClientConstants.ACTIONTYPE_CANCEL];
    private final static ClientParseException parseException = ClientParseException.getInstance();
    private final static Logger log = Logger.getLogger(IncrementUI.class);
    private ProxyFactory proxy = null;
    private Date curDate = null;
    IncrementTO objIncrementTO;
    private boolean rdoIncrement = false, rdoPromotion = false;
    java.util.ResourceBundle IncrementRB = java.util.ResourceBundle.getBundle("com.see.truetransact.ui.payroll.increment.IncrementRB", ProxyParameters.LANGUAGE);
    private static IncrementOB IncrementOB;

    static {
        try {
            log.info("In IncrementOB Declaration");
            IncrementOB = new IncrementOB();
        } catch (Exception e) {
            parseException.logException(e, true);
        }
    }

    public static IncrementOB getInstance() {
        return IncrementOB;
    }

    /** Creates a new instance of InwardClearingOB */
    public IncrementOB() throws Exception {
        objIncrementTO = new IncrementTO();
        curDate = ClientUtil.getCurrentDate();
        cbmDesig = new ComboBoxModel();
        initianSetup();
        fillDropdown();

    }

    private void initianSetup() throws Exception {
        log.info("In initianSetup()");
        setOperationMap();
        try {
            proxy = ProxyFactory.createProxy();
        } catch (Exception e) {
            parseException.logException(e, true);
            //e.printStackTrace();
        }
    }
    // Set the value of JNDI and the Session Bean...
    private void setOperationMap() throws Exception {
        log.info("In setOperationMap()");

        operationMap = new HashMap();
        operationMap.put(CommonConstants.JNDI, "IncrementJNDI");
        operationMap.put(CommonConstants.HOME, "payroll.increment.IncrementHome");
        operationMap.put(CommonConstants.REMOTE, "payroll.increment.Increment");
    }
    private IncrementTO setIncrement() {
        log.info("In setIncrement()");
        try {
            objIncrementTO.setEmployeeId(getTxtEmpId());
            objIncrementTO.setName(getTxtEmpName());
            objIncrementTO.setPresentBasic(CommonUtil.convertObjToDouble(getTxtPresBasicSalary()));
            objIncrementTO.setLastIncrDate(DateUtil.getDateMMDDYYYY(getTxtLastIncrDate()));
            objIncrementTO.setNewDesig(getCboDesig());
            objIncrementTO.setNewBasicSal(CommonUtil.convertObjToDouble(getTxtNewBasicSalary()));
            objIncrementTO.setNextIncrDate(DateUtil.getDateMMDDYYYY(getTxtNextIncrDate()));
            objIncrementTO.setNumOfIncr(CommonUtil.convertObjToInt(getTxtNumberIncr()));
            objIncrementTO.setEmployeeId(getTxtEmpId());
            objIncrementTO.setCommand(getCommand());
            objIncrementTO.setNewIncrDate(DateUtil.getDateMMDDYYYY(getTdtNewIncrDate()));
            objIncrementTO.setScaleId(getScaleId());
            objIncrementTO.setIncrID(getIncrId());
            objIncrementTO.setVersionNo(getVersionNo());
            
            if (isRdoIncrement()) {
                objIncrementTO.setIncrType("Increment");
                objIncrementTO.setNewDesig(getTxtDesig());
                objIncrementTO.setDesignation(getTxtDesig());
            }
            if (isRdoPromotion()) {
                objIncrementTO.setIncrType("Promotion");
                objIncrementTO.setDesignation(getTxtDesig());
                objIncrementTO.setNewDesig(getCboDesig());
            }
        } catch (Exception e) {
            parseException.logException(e, true);
            e.printStackTrace();
        }
        return objIncrementTO;
    }

    public void doAction(String command) {
        log.info("In doAction()---------");
        try {
            if (getCommand() != null) {
                doActionPerform();
            } 
            else {
                log.info("Action Type Not Defined In setChequeBookTO()");
            }
        } catch (Exception e) {
            log.info("Error In doAction()" + e);
            setResult(ClientConstants.ACTIONTYPE_FAILED);
            parseException.logException(e, true);
        }
    }

    /** To perform the necessary action */
    private void doActionPerform() throws Exception {
        log.info("In doActionPerform()");
        final HashMap data = new HashMap();       
        data.put("EmpIncrementTO", setIncrement());
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

    /** To set the status based on ActionType, either New, Edit, etc., */
    public void setStatus() {
        this.setLblStatus(ClientConstants.ACTION_STATUS[this.getActionType()]);
        ttNotifyObservers();
    }

    /** To update the Status based on result performed by doAction() method */
    public void setResultStatus() {
        this.setLblStatus(ClientConstants.RESULT_STATUS[this.getResult()]);
        ttNotifyObservers();
    }

    public void ttNotifyObservers() {
        setChanged();
        notifyObservers();
    }

    public void resetForm() {
        setTxtEmpId("");
        setTxtDesig("");
        setTxtEmpName("");
        setTxtLastIncrDate("");
        setTxtNewBasicSalary("");
        setTxtNextIncrDate("");
        setTxtNumberIncr("");
        setTxtPresBasicSalary("");
        setCboDesig("");
        setTdtNewIncrDate("");
        setScaleId("");
        setVersionNo("");
        scaleId = "";
    }

    /* Set and GET METHODS FOR THE tABLE...*/
    void setTblAgent(EnhancedTableModel tblAgentTab) {
    }

    public void fillDropdown() {
        try {
            HashMap map = new HashMap();

            lookUpHash = new HashMap();
            lookUpHash.put(CommonConstants.JNDI, "LookUpJNDI");
            lookUpHash.put(CommonConstants.HOME, "common.lookup.LookUpHome");
            lookUpHash.put(CommonConstants.REMOTE, "common.lookup.LookUp");
            final ArrayList lookup_keys = new ArrayList();
            lookup_keys.add("PAYROLL.DESIGNATION");
            lookUpHash.put(CommonConstants.PARAMFORQUERY, lookup_keys);
            keyValue = com.see.truetransact.clientutil.ClientUtil.populateLookupData(lookUpHash);            
            getKeyValue((HashMap) keyValue.get("PAYROLL.DESIGNATION"));
            this.cbmDesig = new ComboBoxModel(key, value);

        } catch (NullPointerException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
  public HashMap getPreBasicSal(HashMap dataMap) throws SQLException {
        HashMap resultMap = new HashMap();
        final List basicSal = ClientUtil.executeQuery("getBasicSal", dataMap);
        if (basicSal != null && basicSal.size() > 0) {
            resultMap = (HashMap) basicSal.get(0);
        }
        return resultMap;
    }
    public HashMap getIncrementStagCount(HashMap dataMap) throws SQLException {
        HashMap resultMap = new HashMap();
        final List incrCount = ClientUtil.executeQuery("getIncrStagCount", dataMap);
        if (incrCount != null && incrCount.size() > 0) {
            resultMap = (HashMap) incrCount.get(0);
        }
        return resultMap;
    }
    private void getKeyValue(HashMap keyValue) throws Exception {
        key = (ArrayList) keyValue.get(CommonConstants.KEY);
        System.out.println("key here" + key);
        System.out.println("value here" + value);
        value = (ArrayList) keyValue.get(CommonConstants.VALUE);
    }

    public ComboBoxModel getCbmDesig() {
        return cbmDesig;
    }

    public void setCbmDesig(ComboBoxModel cbmDesig) {
        this.cbmDesig = cbmDesig;
    }

    public String getCboDesig() {
        return cboDesig;
    }

    public void setCboDesig(String cboDesig) {
        this.cboDesig = cboDesig;
    }

    public String getTdtNewIncrDate() {
        return tdtNewIncrDate;
    }

    public void setTdtNewIncrDate(String tdtNewIncrDate) {
        this.tdtNewIncrDate = tdtNewIncrDate;
    }

    public String getTxtDesig() {
        return txtDesig;
    }

    public void setTxtDesig(String txtDesig) {
        this.txtDesig = txtDesig;
    }

    public String getTxtEmpId() {
        return txtEmpId;
    }

    public void setTxtEmpId(String txtEmpId) {
        this.txtEmpId = txtEmpId;
    }

    public String getTxtEmpName() {
        return txtEmpName;
    }

    public void setTxtEmpName(String txtEmpName) {
        this.txtEmpName = txtEmpName;
    }

    public String getTxtLastIncrDate() {
        return txtLastIncrDate;
    }

    public void setTxtLastIncrDate(String txtLastIncrDate) {
        this.txtLastIncrDate = txtLastIncrDate;
    }

    public String getTxtNewBasicSalary() {
        return txtNewBasicSalary;
    }

    public void setTxtNewBasicSalary(String txtNewBasicSalary) {
        this.txtNewBasicSalary = txtNewBasicSalary;
    }

    public String getTxtNumberIncr() {
        return txtNumberIncr;
    }

    public void setTxtNumberIncr(String txtNumberIncr) {
        this.txtNumberIncr = txtNumberIncr;
    }

    public String getTxtPresBasicSalary() {
        return txtPresBasicSalary;
    }

    public void setTxtPresBasicSalary(String txtPresBasicSalary) {
        this.txtPresBasicSalary = txtPresBasicSalary;
    }

    public boolean isRdoIncrement() {
        return rdoIncrement;
    }

    public void setRdoIncrement(boolean rdoIncrement) {
        this.rdoIncrement = rdoIncrement;
    }

    public boolean isRdoPromotion() {
        return rdoPromotion;
    }

    public void setRdoPromotion(boolean rdoPromotion) {
        this.rdoPromotion = rdoPromotion;
    }

    public String getTxtNextIncrDate() {
        return txtNextIncrDate;
    }

    public void setTxtNextIncrDate(String txtNextIncrDate) {
        this.txtNextIncrDate = txtNextIncrDate;
    }

    public String getScaleId() {
        return scaleId;
    }

    public void setScaleId(String scaleId) {
        this.scaleId = scaleId;
    }

    public String getIncrId() {
        return incrId;
    }

    public void setIncrId(String incrId) {
        this.incrId = incrId;
    }

    public String getVersionNo() {
        return versionNo;
    }

    public void setVersionNo(String versionNo) {
        this.versionNo = versionNo;
    }
    
}