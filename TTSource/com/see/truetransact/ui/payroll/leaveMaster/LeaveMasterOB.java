/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition.
 * 
 *
 * LeaveMasterOB.java
 *
 *
 */
package com.see.truetransact.ui.payroll.leaveMaster;

/*
 *
 * @author anjuanand
 */
import com.see.truetransact.clientexception.ClientParseException;
import com.see.truetransact.clientproxy.ProxyFactory;
import com.see.truetransact.clientutil.ClientConstants;
import com.see.truetransact.clientutil.ClientUtil;
import com.see.truetransact.clientutil.ComboBoxModel;
import com.see.truetransact.commonutil.CommonConstants;
import com.see.truetransact.commonutil.CommonUtil;
import com.see.truetransact.transferobject.payroll.leaveDetails.LeaveDetailsTO;
import com.see.truetransact.uicomponent.CObservable;
import java.util.*;
import org.apache.log4j.Logger;

/**
 *
 * @author anjuanand
 */
public class LeaveMasterOB extends CObservable {

    Date curDate = ClientUtil.getCurrentDate();
    private final static ClientParseException parseException = ClientParseException.getInstance();
    private final static Logger log = Logger.getLogger(LeaveMasterOB.class);
    private ProxyFactory proxy = null;
    private int actionType;
    private int result;
    private ArrayList key, value;
    private static LeaveMasterOB objLeaveDetailsOB;//Singleton Object
    private String lblStatus = ClientConstants.ACTION_STATUS[ClientConstants.ACTIONTYPE_CANCEL];
    private HashMap operationMap;
    private HashMap lookUpHash;
    private HashMap keyValue;
    private String txtLeaveId = "";
    private String cboLeaveDescription = "";
    private ComboBoxModel cbmLeaveDescription;
    private boolean isDeduction = false;
    private boolean isHalfPay = false;
    private boolean isFullPay = false;
    private final double halfPay = 0.5;
    private final double fullPay = 1;
    private final double no_Dedu = 0;

    public boolean isIsDeduction() {
        return isDeduction;
    }

    public void setIsDeduction(boolean isDeduction) {
        this.isDeduction = isDeduction;
    }

    public boolean isIsFullPay() {
        return isFullPay;
    }

    public void setIsFullPay(boolean isFullPay) {
        this.isFullPay = isFullPay;
    }

    public boolean isIsHalfPay() {
        return isHalfPay;
    }

    public void setIsHalfPay(boolean isHalfPay) {
        this.isHalfPay = isHalfPay;
    }

    public ComboBoxModel getCbmLeaveDescription() {
        return cbmLeaveDescription;
    }

    public void setCbmLeaveDescription(ComboBoxModel cbmLeaveDescription) {
        this.cbmLeaveDescription = cbmLeaveDescription;
    }

    public String getCboLeaveDescription() {
        return cboLeaveDescription;
    }

    public void setCboLeaveDescription(String cboLeaveDescription) {
        this.cboLeaveDescription = cboLeaveDescription;
    }

    public String getTxtLeaveId() {
        return txtLeaveId;
    }

    public void setTxtLeaveId(String txtLeaveId) {
        this.txtLeaveId = txtLeaveId;
    }

    public int getActionType() {
        return actionType;
    }

    public void setActionType(int actionType) {
        this.actionType = actionType;
        setChanged();
    }

    public int getResult() {
        return result;
    }

    public void setResult(int result) {
        this.result = result;
        setChanged();
    }

    public void setStatus() {
        this.setLblStatus(ClientConstants.ACTION_STATUS[this.getActionType()]);
        ttNotifyObservers();
    }

    public void setResultStatus() {
        this.setLblStatus(ClientConstants.RESULT_STATUS[this.getResult()]);
        ttNotifyObservers();
    }

    /**
     * Creates a new instance of LeaveMasterOB
     */
    private LeaveMasterOB() throws Exception {
        operationMap = new HashMap();
        operationMap.put(CommonConstants.JNDI, "LeaveMasterJNDI");
        operationMap.put(CommonConstants.HOME, "payroll.leaveMaster.LeaveMasterHome");
        operationMap.put(CommonConstants.REMOTE, "payroll.leaveMaster.LeaveMaster");
        try {
            proxy = ProxyFactory.createProxy();
        } catch (Exception e) {
            parseException.logException(e, true);
        }
        fillDropdown();// To Fill all the Combo Boxes
    }

    static {
        try {
            log.info("Creating LeaveDetailsOB...");
            objLeaveDetailsOB = new LeaveMasterOB();
        } catch (Exception e) {
            parseException.logException(e, true);
        }
    }

    private void getKeyValue(HashMap keyValue) throws Exception {
        log.info("getKeyValue");
        key = (ArrayList) keyValue.get(CommonConstants.KEY);
        value = (ArrayList) keyValue.get(CommonConstants.VALUE);
    }

    public void fillDropdown() throws Exception {
        log.info("fillDropdown");
        lookUpHash = new HashMap();
        lookUpHash.put(CommonConstants.MAP_NAME, null);
        final ArrayList lookup_keys = new ArrayList();
        lookup_keys.add("LEAVE_TYPE");
        lookUpHash.put(CommonConstants.PARAMFORQUERY, lookup_keys);
        keyValue = ClientUtil.populateLookupData(lookUpHash);
        getKeyValue((HashMap) keyValue.get("LEAVE_TYPE"));
        cbmLeaveDescription = new ComboBoxModel(key, value);
//        keyValue = ClientUtil.populateLookupData(lookUpHash);
    }

    /**
     * Returns an instance of LeaveMasterOB.
     *
     * @return LeaveMasterOB
     */
    public static LeaveMasterOB getInstance() throws Exception {
        return objLeaveDetailsOB;
    }

    public void ttNotifyObservers() {
        setChanged();
        notifyObservers();
    }

    /**
     * Getter for property lblStatus.
     *
     * @return Value of property lblStatus.
     *
     */
    public java.lang.String getLblStatus() {
        return lblStatus;
    }

    /**
     * Setter for property lblStatus.
     *
     * @param lblStatus New value of property lblStatus.
     *
     */
    public void setLblStatus(java.lang.String lblStatus) {
        this.lblStatus = lblStatus;
        setChanged();
    }

    public void resetStatus() {
        this.setLblStatus(ClientConstants.ACTION_STATUS[0]);
    }

    /**
     * Resetting all the Fields of UI
     */
    public void resetForm() {
        setCboLeaveDescription("");
        isFullPay = false;
        isHalfPay = false;
        isDeduction = false;
        setChanged();
        notifyObservers();
    }

    public void doAction() {
        log.info("In doAction()");
        try {
            //If actionType such as NEW, EDIT, DELETE, then proceed
            if (actionType != ClientConstants.ACTIONTYPE_CANCEL) {
                //If actionType has got propervalue then doActionPerform, else throw error
                if (getCommand() != null) {
                    doActionPerform();
                }
            } else {
                log.info("Action Type Not Defined In setInwardClearingTO()");
            }
        } catch (Exception e) {
            log.info("Error In doAction()");
            setResult(ClientConstants.ACTIONTYPE_FAILED);
        }
    }

    private String getCommand() {
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

    private void doActionPerform() {
        log.info("In doActionPerform()");
        try {
            final LeaveDetailsTO objLeaveDetailsTO = setLeaveMasterDetails();
            objLeaveDetailsTO.setCommand(getCommand());
            final HashMap data = new HashMap();
            data.put("LeaveMasterTO", objLeaveDetailsTO);
            HashMap proxyResultMap = proxy.execute(data, operationMap);
            setProxyReturnMap(proxyResultMap);
            setResult(actionType);
            resetForm();
        } catch (Exception e) {
            log.info("Error In doActionPerform()");
            ClientUtil.showMessageWindow(e.getMessage());
            setResult(ClientConstants.ACTIONTYPE_FAILED);
        }
    }

    private LeaveDetailsTO setLeaveMasterDetails() {
        log.info("In setEmployeeDetails()");
        final LeaveDetailsTO objLeaveDetailsTO = new LeaveDetailsTO();
        objLeaveDetailsTO.setLeaveDescription(getCboLeaveDescription());
        objLeaveDetailsTO.setLeaveID(getTxtLeaveId());
        if (isDeduction == false) {
            objLeaveDetailsTO.setLeaveDedPerDay(CommonUtil.convertObjToDouble(no_Dedu));
        } else if (isDeduction == true && isHalfPay == true) {
            objLeaveDetailsTO.setLeaveDedPerDay(CommonUtil.convertObjToDouble(halfPay));
        } else if (isDeduction == true && isFullPay == true) {
            objLeaveDetailsTO.setLeaveDedPerDay(CommonUtil.convertObjToDouble(fullPay));
        }
        try {
        } catch (Exception e) {
            log.info("Error In setEmployeeDetails()");
            e.printStackTrace();
        }
        return objLeaveDetailsTO;
    }

    public boolean checkLeaveExists(HashMap dataMap) {
        boolean flag = false;
        List leaveId = ClientUtil.executeQuery("checkLeaveExists", dataMap);
        if (leaveId != null && leaveId.size() > 0) {
            flag = true;
        } else {
            flag = false;
        }
        return flag;
    }

    public void populateOB(HashMap dataMap) throws Exception {
        final List listData;
        HashMap data = new HashMap();
        data.put("LEAVEID", dataMap.get("LEAVE_ID"));
        listData = ClientUtil.executeQuery("getAllLeaveTypes", data);
        if (listData != null && listData.size() > 0) {
            HashMap hash = new HashMap();
            hash = (HashMap) listData.get(0);
            setCboLeaveDescription(CommonUtil.convertObjToStr(hash.get("LEAVE_DESC")));
            setTxtLeaveId(CommonUtil.convertObjToStr(hash.get("LEAVE_ID")));
            if (CommonUtil.convertObjToDouble(hash.get("LEAVE_DED_PER_DAY")) == no_Dedu) {
                setIsDeduction(false);
                setIsFullPay(false);
                setIsHalfPay(false);
            } else if (CommonUtil.convertObjToDouble(hash.get("LEAVE_DED_PER_DAY")) == halfPay) {
                setIsDeduction(true);
                setIsFullPay(false);
                setIsHalfPay(true);
            } else if (CommonUtil.convertObjToDouble(hash.get("LEAVE_DED_PER_DAY")) == fullPay) {
                setIsDeduction(true);
                setIsFullPay(true);
                setIsHalfPay(false);
            }
        }
    }
}