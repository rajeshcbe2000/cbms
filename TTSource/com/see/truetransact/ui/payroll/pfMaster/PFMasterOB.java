/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition.
 * 
 *
 * PFMasterOB.java
 *
 * 
 */
package com.see.truetransact.ui.payroll.pfMaster;

import com.see.truetransact.clientexception.ClientParseException;
import com.see.truetransact.clientproxy.ProxyFactory;
import com.see.truetransact.clientutil.ClientConstants;
import com.see.truetransact.clientutil.ClientUtil;
import com.see.truetransact.commonutil.CommonConstants;
import com.see.truetransact.commonutil.CommonUtil;
import com.see.truetransact.commonutil.DateUtil;
import com.see.truetransact.transferobject.payroll.pfMaster.PFMasterTO;
import com.see.truetransact.ui.TrueTransactMain;
import com.see.truetransact.uicomponent.CObservable;
import java.sql.SQLException;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import org.apache.log4j.Logger;

/**
 *
 * @author anjuanand
 */
public class PFMasterOB extends CObservable {

    private HashMap operationMap;
    private int actionType;
    private int result;
    private final static ClientParseException parseException = ClientParseException.getInstance();
    private final static Logger log = Logger.getLogger(com.see.truetransact.ui.payroll.pfMaster.PFMasterUI.class);
    private ProxyFactory proxy = null;
    private String lblStatus = ClientConstants.ACTION_STATUS[ClientConstants.ACTIONTYPE_CANCEL];
    private Date curDate = null;
    private String txtEmployeeId = "";
    private String txtPfAccountNo = "";
    private Date tdtPfDate = null;
    private Date tdtPfOpeningDate = null;
    private String txtOpeningBalance = "";
    private String txtPfRateOfInterest = "";
    private Date tdtLastInterestDate = null;
    private String txtPfNomineeName = "";
    private String txtPfNomineeRelation = "";
    private String txtEmployerContribution = "";
    private static Date currDt = null;

    public Date getTdtLastInterestDate() {
        return tdtLastInterestDate;
    }

    public void setTdtLastInterestDate(Date tdtLastInterestDate) {
        this.tdtLastInterestDate = tdtLastInterestDate;
    }

    public Date getTdtPfDate() {
        return tdtPfDate;
    }

    public void setTdtPfDate(Date tdtPfDate) {
        this.tdtPfDate = tdtPfDate;
    }

    public Date getTdtPfOpeningDate() {
        return tdtPfOpeningDate;
    }

    public void setTdtPfOpeningDate(Date tdtPfOpeningDate) {
        this.tdtPfOpeningDate = tdtPfOpeningDate;
    }
   
    public String getTxtEmployeeId() {
        return txtEmployeeId;
    }

    public void setTxtEmployeeId(String txtEmployeeId) {
        this.txtEmployeeId = txtEmployeeId;
    }

    public String getTxtEmployerContribution() {
        return txtEmployerContribution;
    }

    public void setTxtEmployerContribution(String txtEmployerContribution) {
        this.txtEmployerContribution = txtEmployerContribution;
    }

    public String getTxtOpeningBalance() {
        return txtOpeningBalance;
    }

    public void setTxtOpeningBalance(String txtOpeningBalance) {
        this.txtOpeningBalance = txtOpeningBalance;
    }

    public String getTxtPfAccountNo() {
        return txtPfAccountNo;
    }

    public void setTxtPfAccountNo(String txtPfAccountNo) {
        this.txtPfAccountNo = txtPfAccountNo;
    }

    public String getTxtPfNomineeName() {
        return txtPfNomineeName;
    }

    public void setTxtPfNomineeName(String txtPfNomineeName) {
        this.txtPfNomineeName = txtPfNomineeName;
    }

    public String getTxtPfNomineeRelation() {
        return txtPfNomineeRelation;
    }

    public void setTxtPfNomineeRelation(String txtPfNomineeRelation) {
        this.txtPfNomineeRelation = txtPfNomineeRelation;
    }

    public String getTxtPfRateOfInterest() {
        return txtPfRateOfInterest;
    }

    public void setTxtPfRateOfInterest(String txtPfRateOfInterest) {
        this.txtPfRateOfInterest = txtPfRateOfInterest;
    }
    private static PFMasterOB pfMasterOB;

    static {
        try {
            log.info("In PFMasterOB Declaration");
            currDt = ClientUtil.getCurrentDate();
            pfMasterOB = new PFMasterOB();
        } catch (Exception e) {
            parseException.logException(e, true);
        }
    }

    public static PFMasterOB getInstance() {
        return pfMasterOB;
    }

    /**
     * Creates a new instance of PFMasterOB
     */
    public PFMasterOB() {
        try {
            initianSetup();
        } catch (Exception e) {
        }
    }

    private void initianSetup() throws Exception {
        setOperationMap();
        try {
            proxy = ProxyFactory.createProxy();
        } catch (Exception e) {
            parseException.logException(e, true);
        }
    }

    // Set the value of JNDI and the Session Bean...
    private void setOperationMap() throws Exception {
        log.info("In setOperationMap()");
        proxy = ProxyFactory.createProxy();
        operationMap = new HashMap();
        operationMap.put(CommonConstants.JNDI, "PFMasterJNDI");
        operationMap.put(CommonConstants.HOME, "payroll.pfMaster.PFMasterHome");
        operationMap.put(CommonConstants.REMOTE, "payroll.pfMaster.PFMaster");
    }

    public Date getCurrentDate() {
        return (Date) curDate.clone();
    }

    private Date setProperDtFormat(Date dt) {
        Date tempDt = (Date) curDate.clone();
        if (dt != null) {
            tempDt.setDate(dt.getDate());
            tempDt.setMonth(dt.getMonth());
            tempDt.setYear(dt.getYear());
            return tempDt;
        }
        return null;
    }

    public boolean chkPFActNoExists(String pfAccNo) {
        boolean flag = false;
        HashMap dataMap = new HashMap();
        dataMap.put("PF_ACT_NO", pfAccNo);
        List pfActList = ClientUtil.executeQuery("checkPFActNo", dataMap);
        if (pfActList != null && pfActList.size() > 0) {
            flag = true;
        } else {
            flag = false;
        }
        return flag;
    }

    public boolean chkEmpIdExists(String empId) {
        boolean flag = false;
        HashMap dataMap = new HashMap();
        dataMap.put("EMPID", empId);
        List empIdList = ClientUtil.executeQuery("chkEmpId", dataMap);
        if (empIdList != null && empIdList.size() > 0) {
            flag = true;
        } else {
            flag = false;
        }
        return flag;
    }

    public void populateOB(HashMap dataMap) throws Exception {
        final List listData;
        HashMap data = new HashMap();
        data.put("EMPLOYEEID", dataMap.get("EMP_ID"));
        listData = ClientUtil.executeQuery("getPFEmployeeDetails", data);
        if (listData != null && listData.size() > 0) {
            HashMap hash = new HashMap();
            hash = (HashMap) listData.get(0);
            setTxtEmployeeId(CommonUtil.convertObjToStr(dataMap.get("EMP_ID")));
            setTxtEmployerContribution(CommonUtil.convertObjToStr(hash.get("EMPLOYER_CONTRI")));
            setTxtOpeningBalance(CommonUtil.convertObjToStr(hash.get("PF_OP_BAL")));
            setTxtPfRateOfInterest(CommonUtil.convertObjToStr(hash.get("PF_ROI")));
            setTxtPfAccountNo(CommonUtil.convertObjToStr(hash.get("PF_ACT_NO")));
            setTxtPfNomineeName(CommonUtil.convertObjToStr(hash.get("PF_NOM")));
            setTxtPfNomineeRelation(CommonUtil.convertObjToStr(hash.get("PF_NOM_RL")));
            setTdtLastInterestDate(DateUtil.getDateMMDDYYYY(CommonUtil.convertObjToStr(hash.get("PF_INT_DT"))));
            setTdtPfDate(DateUtil.getDateMMDDYYYY(CommonUtil.convertObjToStr(hash.get("PF_DT"))));
            setTdtPfOpeningDate(DateUtil.getDateMMDDYYYY(CommonUtil.convertObjToStr(hash.get("PF_OP_DT"))));
        }
    }

    public void doAction() {
        log.info("In doAction()");
        try {
            //If actionType such as NEW, EDIT, DELETE, then proceed
            if (actionType != ClientConstants.ACTIONTYPE_CANCEL) {
                //If actionType has got propervalue then doActionPerform, else throw error
                doActionPerform();
            }
        } catch (Exception e) {
            log.info("Error In doAction()");
            setResult(ClientConstants.ACTIONTYPE_FAILED);
            parseException.logException(e, true);
        }
    }

    /**
     * To perform the necessary action
     */
    private void doActionPerform() throws Exception {
        log.info("In doActionPerform()");
        try {
            final PFMasterTO objPfMasterTO = setPFMaster();
            final HashMap data = new HashMap();
            data.put("PF_MASTER_DATA", objPfMasterTO);
            objPfMasterTO.setCommand(getCommand());
            HashMap proxyResultMap = proxy.execute(data, operationMap);
            if (proxyResultMap != null && proxyResultMap.containsKey(CommonConstants.TRANS_ID) && proxyResultMap.get(CommonConstants.TRANS_ID) != null) {
                ClientUtil.showMessageWindow("Transaction No. : " + CommonUtil.convertObjToStr(proxyResultMap.get(CommonConstants.TRANS_ID)));
            }
            resetForm();
        } catch (SQLException ex) {
            log.info("Error In doActionPerform()");
            ex.printStackTrace();
            ClientUtil.showMessageWindow(ex.getMessage());
            setResult(ClientConstants.ACTIONTYPE_FAILED);
            parseException.logException(ex, true);

        } catch (Exception e) {
            log.info("Error In doActionPerform()");
            e.printStackTrace();
            ClientUtil.showMessageWindow(e.getMessage());
            setResult(ClientConstants.ACTIONTYPE_FAILED);
            parseException.logException(e, true);
        }
    }

    // to decide which action Should be performed...
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

    private String getAction() {
        log.info("In getAction()");
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
            default:
        }
        return action;
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
        setTdtLastInterestDate(null);
        setTdtPfDate(null);
        setTdtPfOpeningDate(null);
        setTxtEmployeeId("");
        setTxtEmployerContribution("");
        setTxtEmployerContribution("");
        setTxtOpeningBalance("");
        setTxtPfAccountNo("");
        setTxtPfNomineeName("");
        setTxtPfNomineeRelation("");
        setTxtPfRateOfInterest("");
    }

    private PFMasterTO setPFMaster() {
        log.info("in setPFMaster");
        final PFMasterTO objPFMasterTo = new PFMasterTO();
        objPFMasterTo.setEmpId(txtEmployeeId);
        objPFMasterTo.setEmployerContri(CommonUtil.convertObjToDouble(txtEmployerContribution));
        objPFMasterTo.setPfActNo(txtPfAccountNo);
        objPFMasterTo.setPfNominee(txtPfNomineeName);
        objPFMasterTo.setPfOpenBal(CommonUtil.convertObjToDouble(txtOpeningBalance));
        objPFMasterTo.setPrnBal(CommonUtil.convertObjToDouble(txtOpeningBalance));
        objPFMasterTo.setPfNomineeRelation(txtPfNomineeRelation);
        objPFMasterTo.setPfRate(CommonUtil.convertObjToDouble(txtPfRateOfInterest));
        objPFMasterTo.setCreatedBy(TrueTransactMain.USER_ID);
        objPFMasterTo.setStatusBy(TrueTransactMain.USER_ID);
        objPFMasterTo.setStatusDate(currDt);
        objPFMasterTo.setCreatedDate(currDt);
        objPFMasterTo.setPfDate(tdtPfDate);
        objPFMasterTo.setPfIntDate(tdtLastInterestDate);
        objPFMasterTo.setPfOpenDate(tdtPfOpeningDate);
        log.info("end of setPFMaster");
        return objPFMasterTo;
    }
}