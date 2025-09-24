/**
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition. 
 * 
 * Author   : Chithra
 * Location : Thrissur
 * Date of Completion : 1-08-2015
 */
package com.see.truetransact.ui.payroll.leaveSurrender;

import com.see.truetransact.ui.directorboardmeeting.*;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.ArrayList;
import java.util.List;
import java.util.Iterator;
import java.util.Date;

import com.see.truetransact.clientutil.ComboBoxModel;
import com.see.truetransact.clientproxy.ProxyFactory;
import com.see.truetransact.commonutil.CommonConstants;
import com.see.truetransact.commonutil.CommonUtil;
import com.see.truetransact.transferobject.sysadmin.emptransfer.EmpTransferTO;
import com.see.truetransact.clientutil.ClientConstants;
import com.see.truetransact.commonutil.TTException;
import org.apache.log4j.Logger;
import com.see.truetransact.clientutil.ClientUtil;
import com.see.truetransact.clientexception.ClientParseException;
import com.see.truetransact.commonutil.CommonUtil;
import com.see.truetransact.ui.TrueTransactMain;
import com.see.truetransact.uicomponent.COptionPane;
import com.see.truetransact.ui.common.CommonRB;
import com.see.truetransact.commonutil.DateUtil;
import com.see.truetransact.clientproxy.ProxyParameters;
import com.see.truetransact.uicomponent.CObservable;
import com.see.truetransact.transferobject.directorboardmeeting.DirectorBoardTO;
import com.see.truetransact.transferobject.payroll.leavesurrender.LeaveSurrenderTO;
import com.see.truetransact.ui.common.transaction.TransactionOB;

/**
 *
 * @author
 *
 */
public class LeaveSurrenderOB extends CObservable {

    private String txtEmpId = "";
    private Double txtLeaveAmount = null;
    private String txtMemberName = "";
    private int txtLeaveNo = 0;
    private int actionType;
    private String txtStatus = "";
    private int result;
    private HashMap _authorizeMap;
    private String CreatedDt = "";
    private String lblStatus = ClientConstants.ACTION_STATUS[ClientConstants.ACTIONTYPE_CANCEL];
    private String txtStatusBy = "";
    private HashMap map;
    private ProxyFactory proxy;
    private final static Logger log = Logger.getLogger(LeaveSurrenderOB.class);
    private ComboBoxModel cbmBoardMember;
    private HashMap lookupMap;
    private HashMap param;
    private HashMap keyValue;
    private ArrayList key;
    private ArrayList value;
    private int _actionType;
    private TransactionOB transactionOB;
    private LinkedHashMap allowedTransactionDetailsTO = null;
    private final static ClientParseException parseException = ClientParseException.getInstance();
    private Date curDate = null;
    public LeaveSurrenderOB() {
        try {
            proxy = ProxyFactory.createProxy();
            curDate = ClientUtil.getCurrentDate();
            transactionOB = new TransactionOB();
            map = new HashMap();
            map.put(CommonConstants.JNDI, "LeaveSurrenderJNDI");
            map.put(CommonConstants.HOME, "LeaveSurrenderHome");
            map.put(CommonConstants.REMOTE, "LeaveSurrender");
            fillDropdown();

        } catch (Exception e) {
            parseException.logException(e, true);
        }
    }

    /**
     * To fill the comboboxes
     */
    private void fillDropdown() throws Exception {
        lookupMap = new HashMap();
        lookupMap.put(CommonConstants.JNDI, "LookUpJNDI");
        lookupMap.put(CommonConstants.HOME, "common.lookup.LookUpHome");
        lookupMap.put(CommonConstants.REMOTE, "common.lookup.LookUp");

        HashMap param = new HashMap();
        param.put(CommonConstants.MAP_NAME, "getAllBoardMem");
        HashMap where = new HashMap();
        where.put("BRANCH_ID", ProxyParameters.BRANCH_ID);
        param.put(CommonConstants.PARAMFORQUERY, where);
        where = null;
        keyValue = ClientUtil.populateLookupData(param);
        fillData((HashMap) keyValue.get(CommonConstants.DATA));
        cbmBoardMember = new ComboBoxModel(key, value);



    }

    private void fillData(HashMap keyValue) throws Exception {
        key = (ArrayList) keyValue.get("KEY");
        value = (ArrayList) keyValue.get("VALUE");
    }

    public void ttNotifyObservers() {
        notifyObservers();
    }

    /**
     * To perform the necessary operation
     */
    public void doAction() {
        try {

            doActionPerform();
        } catch (Exception e) {
            setResult(ClientConstants.ACTIONTYPE_FAILED);
        }
    }

    /**
     * To perform the necessary action
     */
    private void doActionPerform() throws Exception {
         LeaveSurrenderTO objLeaveSurrenderTO = new LeaveSurrenderTO();
        final HashMap data = new HashMap();
        data.put("COMMAND", getCommand());
        data.put("USER", ProxyParameters.USER_ID);
        data.put("BRANCH_ID", ProxyParameters.BRANCH_ID);
        if (get_authorizeMap() == null) {
            objLeaveSurrenderTO = setLeaveSurrenderTODet();
            data.put("LeaveSurrenderTO",setLeaveSurrenderTODet());
            data.put("TransactionTo", allowedTransactionDetailsTO);
        } else {
            data.put(CommonConstants.AUTHORIZEMAP, get_authorizeMap());
        }
        System.out.println("data in DirectorBoard OB : " + data);
        HashMap proxyResultMap = proxy.execute(data, map);
        setProxyReturnMap(proxyResultMap);
        if (proxyResultMap != null && getCommand() != null && getCommand().equalsIgnoreCase("INSERT")) {
            ClientUtil.showMessageWindow("Transaction No. : " + CommonUtil.convertObjToStr(proxyResultMap.get("LEAVE_SURRENDER_ID")));
        }
        setResult(getActionType());
        setResult(actionType);
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
        // System.out.println("command : " + command);
        return command;
    }

    private String getAction() {
        String action = null;
        // System.out.println("actionType : " + actionType);
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
        // System.out.println("command : " + command);
        return action;
    }

    /**
     * To retrieve a particular customer's accountclosing record
     */
    public void getData(HashMap whereMap) {
        try {
            final HashMap data = (HashMap) proxy.executeQuery(whereMap, map);
            List list = (List) data.get("TransactionTO");
            transactionOB.setDetails(list);
           LeaveSurrenderTO objLeaveSurrenderTO = (LeaveSurrenderTO)((List) data.get("LeaveSurrenderTO")).get(0);
             populateLeaveSurrenderData(objLeaveSurrenderTO);
            ttNotifyObservers();
        } catch (Exception e) {
//            parseException.logException(e,true);
        }
    }
    public LeaveSurrenderTO setLeaveSurrenderTODet() {
        LeaveSurrenderTO objLeaveSurrenderTO = new LeaveSurrenderTO();
        objLeaveSurrenderTO.setCommand(getCommand());
        objLeaveSurrenderTO.setEmpId(getTxtEmpId());
        objLeaveSurrenderTO.setLeaveNo(getTxtLeaveNo());
        objLeaveSurrenderTO.setLeaveAmt(getTxtLeaveAmount());
        objLeaveSurrenderTO.setCreatedBy(ProxyParameters.USER_ID);
        objLeaveSurrenderTO.setStatusBy(ProxyParameters.USER_ID);
        objLeaveSurrenderTO.setStatus(CommonConstants.STATUS_CREATED);
        objLeaveSurrenderTO.setStatusDt((Date) curDate.clone());
        objLeaveSurrenderTO.setCreatedDt((Date) curDate.clone());
        objLeaveSurrenderTO.setBranchId(ProxyParameters.BRANCH_ID);
        return objLeaveSurrenderTO;
    }
    
    private void populateLeaveSurrenderData(LeaveSurrenderTO objLeaveSurrenderTO) throws Exception {
        setTxtEmpId(objLeaveSurrenderTO.getEmpId());
        setTxtLeaveAmount(objLeaveSurrenderTO.getLeaveAmt());
        setTxtLeaveNo(objLeaveSurrenderTO.getLeaveNo());

        setChanged();
        notifyObservers();
    }

    public void resetForm() {
        setTxtEmpId("");
        setTxtLeaveAmount(null);
        setTxtLeaveNo(0);
        setTxtMemberName("");
        setTransactionOB(null);
        allowedTransactionDetailsTO = null;
        setChanged();
        set_authorizeMap(null);
        ttNotifyObservers();
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

    /**
     * Getter for property txtStatusBy.
     *
     * @return Value of property txtStatusBy.
     */
    public java.lang.String getTxtStatusBy() {
        return txtStatusBy;
    }

    /**
     * Setter for property txtStatusBy.
     *
     * @param txtStatusBy New value of property txtStatusBy.
     */
    public void setTxtStatusBy(java.lang.String txtStatusBy) {
        this.txtStatusBy = txtStatusBy;
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
     *
     *
     * /**
     * Getter for property CreatedDt.
     *
     * @return Value of property CreatedDt.
     */
    public java.lang.String getCreatedDt() {
        return CreatedDt;
    }

    /**
     * Setter for property CreatedDt.
     *
     * @param CreatedDt New value of property CreatedDt.
     */
    public void setCreatedDt(java.lang.String CreatedDt) {
        this.CreatedDt = CreatedDt;
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
     * Getter for property txtStatus.
     *
     * @return Value of property txtStatus.
     */
    public java.lang.String getTxtStatus() {
        return txtStatus;
    }

    /**
     * Setter for property txtStatus.
     *
     * @param txtStatus New value of property txtStatus.
     */
    public void setTxtStatus(java.lang.String txtStatus) {
        this.txtStatus = txtStatus;
    }

    //-------------------------------------------------------
    public ComboBoxModel getCbmBoardMember() {
        return cbmBoardMember;
    }

    /**
     * Setter for property cbmBoardMember.
     *
     * @param cbmBoardMember New value of property cbmBoardMember.
     */
    public void setCbmBoardMember(ComboBoxModel cbmBoardMember) {
        this.cbmBoardMember = cbmBoardMember;
    }

    /**
     * Getter for property _actionType.
     *
     * @return Value of property _actionType.
     */
    public int get_actionType() {
        return _actionType;
    }

    /**
     * Setter for property _actionType.
     *
     * @param _actionType New value of property _actionType.
     */
    public void set_actionType(int _actionType) {
        this._actionType = _actionType;
        setStatus();
        setChanged();
    }

    public TransactionOB getTransactionOB() {
        return transactionOB;
    }

    public void setTransactionOB(TransactionOB transactionOB) {
        this.transactionOB = transactionOB;
    }

    public LinkedHashMap getAllowedTransactionDetailsTO() {
        return allowedTransactionDetailsTO;
    }

    public void setAllowedTransactionDetailsTO(LinkedHashMap allowedTransactionDetailsTO) {
        this.allowedTransactionDetailsTO = allowedTransactionDetailsTO;
    }

    public String getTxtEmpId() {
        return txtEmpId;
    }

    public void setTxtEmpId(String txtEmpId) {
        this.txtEmpId = txtEmpId;
    }

    public Double getTxtLeaveAmount() {
        return txtLeaveAmount;
    }

    public void setTxtLeaveAmount(Double txtLeaveAmount) {
        this.txtLeaveAmount = txtLeaveAmount;
    }

    public int getTxtLeaveNo() {
        return txtLeaveNo;
    }

    public void setTxtLeaveNo(int txtLeaveNo) {
        this.txtLeaveNo = txtLeaveNo;
    }

    public String getTxtMemberName() {
        return txtMemberName;
    }

    public void setTxtMemberName(String txtMemberName) {
        this.txtMemberName = txtMemberName;
    }
}
