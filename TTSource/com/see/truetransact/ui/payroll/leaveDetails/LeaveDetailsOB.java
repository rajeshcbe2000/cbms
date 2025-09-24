/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition.
 * 
 *
 * LeaveDetailsOB.java
 *
 *
 */
package com.see.truetransact.ui.payroll.leaveDetails;

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
import com.see.truetransact.commonutil.DateUtil;
import com.see.truetransact.transferobject.payroll.leaveDetails.LeaveDetailsTO;
import com.see.truetransact.uicomponent.CObservable;
import java.util.*;
import org.apache.log4j.Logger;

/**
 *
 * @author anjuanand
 */
public class LeaveDetailsOB extends CObservable {

    Date curDate = ClientUtil.getCurrentDate();
    private final static ClientParseException parseException = ClientParseException.getInstance();
    private final static Logger log = Logger.getLogger(LeaveDetailsOB.class);
    private ProxyFactory proxy = null;
    private int actionType;
    private int result;
    private ArrayList key, value;
    private static LeaveDetailsOB objLeaveDetailsOB;//Singleton Object
    private String lblStatus = ClientConstants.ACTION_STATUS[ClientConstants.ACTIONTYPE_CANCEL];
    private HashMap operationMap;
    private String txtLeaveId = "";
    private String cboLeaveDescription = "";
    private String txtEmployeeId = "";
    private String tdtLeaveDate = "";
    private String tdtLeaveToDate="";
    private String txtRemarks = "";
    private ComboBoxModel cbmLeaveDescription;
    private String txtLeaveDetailsId = "";

    public String getTdtLeaveToDate() {
        return tdtLeaveToDate;
    }

    public void setTdtLeaveToDate(String tdtLeaveToDate) {
        this.tdtLeaveToDate = tdtLeaveToDate;
    }
   
    public String getTxtLeaveDetailsId() {
        return txtLeaveDetailsId;
    }

    public void setTxtLeaveDetailsId(String txtLeaveDetailsId) {
        this.txtLeaveDetailsId = txtLeaveDetailsId;
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

    public String getTxtLeaveId() {
        return txtLeaveId;
    }

    public void setTxtLeaveId(String txtLeaveId) {
        this.txtLeaveId = txtLeaveId;
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

    public String getTdtLeaveDate() {
        return tdtLeaveDate;
    }

    public void setTdtLeaveDate(String tdtLeaveDate) {
        this.tdtLeaveDate = tdtLeaveDate;
    }

    public String getTxtEmployeeId() {
        return txtEmployeeId;
    }

    public void setTxtEmployeeId(String txtEmployeeId) {
        this.txtEmployeeId = txtEmployeeId;
    }

    public String getTxtRemarks() {
        return txtRemarks;
    }

    public void setTxtRemarks(String txtRemarks) {
        this.txtRemarks = txtRemarks;
    }

    /**
     * Creates a new instance of LeaveDetailsOB
     */
    private LeaveDetailsOB() {
        operationMap = new HashMap();
        operationMap.put(CommonConstants.JNDI, "LeaveDetailsJNDI");
        operationMap.put(CommonConstants.HOME, "payroll.leaveDetails.LeaveDetailsHome");
        operationMap.put(CommonConstants.REMOTE, "payroll.leaveDetails.LeaveDetails");
        try {
            proxy = ProxyFactory.createProxy();
        } catch (Exception e) {
            parseException.logException(e, true);
        }
    }

    static {
        try {
            log.info("Creating LeaveDetailsOB...");
            objLeaveDetailsOB = new LeaveDetailsOB();
        } catch (Exception e) {
            parseException.logException(e, true);
        }
    }

    public List setLeaveDesc() {
        List lst = (List) ClientUtil.executeQuery("selectLeaveDesc", null);
        if (lst != null && lst.size() > 0) {
            getMap(lst);
            cbmLeaveDescription = new ComboBoxModel(key, value);
            setCbmLeaveDescription(cbmLeaveDescription);
        }
        return lst;
    }

    private void getMap(List list) {
        if (list != null && list.size() > 0) {
            key = new ArrayList();
            value = new ArrayList();
            key.add("");
            value.add("");
            for (int i = 0, j = list.size(); i < j; i++) {
                key.add(((HashMap) list.get(i)).get("KEY"));
                value.add(((HashMap) list.get(i)).get("VALUE"));
            }
        }
    }

    public List getLeaveId(HashMap dataMap) {
        List leaveIdList = ClientUtil.executeQuery("getLeaveId", dataMap);
        if (leaveIdList != null && leaveIdList.size() > 0) {
            return leaveIdList;
        }
        return null;
    }

    public boolean chkLeaveExists(HashMap dataMap) {
        boolean flag = false;
        List leaveId = ClientUtil.executeQuery("chkLeaveExists", dataMap);
        if (leaveId != null && leaveId.size() > 0) {
            flag = true;
        } else {
            flag = false;
        }
        return flag;
    }

    public boolean chkDateExists(HashMap dataMap) {
        boolean flag = false;
        List leaveId = ClientUtil.executeQuery("chkDateExists", dataMap);
        if (leaveId != null && leaveId.size() > 0) {
            flag = true;
        } else {
            flag = false;
        }
        return flag;
    }
    
    /**
     * Returns an instance of LeaveDetailsOB.
     *
     * @return LeaveDetailsOB
     */
    public static LeaveDetailsOB getInstance() throws Exception {
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
        setTxtEmployeeId("");
        setTdtLeaveDate(null);
        setTdtLeaveToDate(null);
        setTxtRemarks("");
        setCboLeaveDescription("");
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
            final LeaveDetailsTO objLeaveDetailsTO = setLeaveDetails();
            objLeaveDetailsTO.setCommand(getCommand());
            final HashMap data = new HashMap();
            data.put("LeaveDetailsTO", objLeaveDetailsTO);
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

    private LeaveDetailsTO setLeaveDetails() {
        log.info("In setEmployeeDetails()");
        final LeaveDetailsTO objLeaveDetailsTO = new LeaveDetailsTO();
        try {
            objLeaveDetailsTO.setLeaveID(getTxtLeaveId());
            objLeaveDetailsTO.setEmployeeId(getTxtEmployeeId());
            objLeaveDetailsTO.setRemarks(getTxtRemarks());
            objLeaveDetailsTO.setLeaveDescription(getCboLeaveDescription());
            objLeaveDetailsTO.setLeaveDate(DateUtil.getDateMMDDYYYY(getTdtLeaveDate()));
            objLeaveDetailsTO.setLeaveToDate(DateUtil.getDateMMDDYYYY(getTdtLeaveToDate()));
            objLeaveDetailsTO.setLeaveDetailsId(getTxtLeaveDetailsId());
        } catch (Exception e) {
            log.info("Error In setEmployeeDetails()");
            e.printStackTrace();
        }
        return objLeaveDetailsTO;
    }
    
    
     public void populateOB(HashMap dataMap) throws Exception {
        System.out.println("datamap in ob: "+dataMap);
        final List listData;
        HashMap data = new HashMap();
        data.put("LEAVE_DETAILS_ID", dataMap.get("LEAVE_DETAILS_ID"));
        listData = ClientUtil.executeQuery("getAllLeaveDetails", data);
        if (listData != null && listData.size() > 0) {
            HashMap hash = new HashMap();
            hash = (HashMap) listData.get(0);
            setTxtEmployeeId(CommonUtil.convertObjToStr(hash.get("EMP_ID")));
            setTdtLeaveDate(CommonUtil.convertObjToStr(hash.get("LEAVE_DATE")));
            setTxtRemarks(CommonUtil.convertObjToStr(hash.get("LEAVE_REMARKS")));
            setCboLeaveDescription(CommonUtil.convertObjToStr(hash.get("LEAVE_DESC")));
            setTxtLeaveId(CommonUtil.convertObjToStr(hash.get("LEAVE_ID")));
            setTxtLeaveDetailsId(CommonUtil.convertObjToStr(hash.get("LEAVE_DETAILS_ID")));
        }
     }
     private Date setproperDate(Date roiDt) {
        if (roiDt != null) {
            Date roiCurrDt = (Date) curDate.clone();
            roiCurrDt.setDate(roiDt.getDate());
            roiCurrDt.setMonth(roiDt.getMonth());
            roiCurrDt.setYear(roiDt.getYear());
            return (roiCurrDt);
        }
        return null;
    }
}