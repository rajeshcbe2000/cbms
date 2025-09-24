/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition.
 * 
 *
 * PFTransferOB.java
 *
 * 
 */
package com.see.truetransact.ui.payroll.pftransfer;

import com.see.truetransact.clientexception.ClientParseException;
import com.see.truetransact.clientproxy.ProxyFactory;
import com.see.truetransact.clientutil.ClientConstants;
import com.see.truetransact.clientutil.ClientUtil;
import com.see.truetransact.commonutil.CommonConstants;
import com.see.truetransact.commonutil.CommonUtil;
import com.see.truetransact.transferobject.payroll.PFTransferTO;
import com.see.truetransact.transferobject.payroll.pfMaster.PFMasterTO;
import com.see.truetransact.ui.TrueTransactMain;
import com.see.truetransact.ui.common.transaction.TransactionOB;
import com.see.truetransact.uicomponent.CObservable;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import org.apache.log4j.Logger;
import java.util.Date;
/**
 *
 * @author anjuanand
 */
public class PFTransferOB extends CObservable {

    private HashMap operationMap;
    private TransactionOB transactionOB;
    private String batchID = "";
    private Double amount = null;
    private boolean rdoTransactionType_Debit = false;
    private boolean rdoTransactionType_Credit = false;
    private LinkedHashMap transactionDetailsTO = null; //trans details
    private LinkedHashMap deletedTransactionDetailsTO = null; //trans details
    private LinkedHashMap allowedTransactionDetailsTO = null; //trans details
    private final String DELETED_TRANS_TOs = "DELETED_TRANS_TOs";
    private final String NOT_DELETED_TRANS_TOs = "NOT_DELETED_TRANS_TOs";
    private final String AUTHORIZE = "AUTHORIZE";
    final PFTransferRB objPFTransferRB = new PFTransferRB();
    private int actionType;
    private int result;
    private String lblStatus = ClientConstants.ACTION_STATUS[ClientConstants.ACTIONTYPE_CANCEL];
    private final static ClientParseException parseException = ClientParseException.getInstance();
    private final static Logger log = Logger.getLogger(PFTransferUI.class);
    private ProxyFactory proxy = null;
    private String txtPfNo = "";
    private Double txtBalance = null;
    private Double txtAmount = null;
    final String DEBIT = "DEBIT";
    final String CREDIT = "CREDIT";
    private String lblTransactionId;
    private String lblTransDate;
    private HashMap authMap = new HashMap();
    private HashMap authorizeMap;
    private String transType;
    private String pfTransType;
    private String transMode;
    private String pfId = "";
    private String empId = "";
    private static Date currDt = null;
    
    public String getEmpId() {
        return empId;
    }

    public void setEmpId(String empId) {
        this.empId = empId;
    }

    public String getPfId() {
        return pfId;
    }

    public void setPfId(String pfId) {
        this.pfId = pfId;
    }
   
    public String getTransMode() {
        return transMode;
    }

    public void setTransMode(String transMode) {
        this.transMode = transMode;
    }

    public String getPfTransType() {
        return pfTransType;
    }

    public void setPfTransType(String pfTransType) {
        this.pfTransType = pfTransType;
    }

    public String getTransType() {
        return transType;
    }

    public void setTransType(String transType) {
        this.transType = transType;
    }

    public HashMap getAuthorizeMap() {
        return authorizeMap;
    }

    public void setAuthorizeMap(HashMap authorizeMap) {
        this.authorizeMap = authorizeMap;
    }

    public HashMap getAuthMap() {
        return authMap;
    }

    public void setAuthMap(HashMap authMap) {
        this.authMap = authMap;
    }

    public String getLblTransactionId() {
        return lblTransactionId;
    }

    public void setLblTransactionId(String lblTransactionId) {
        this.lblTransactionId = lblTransactionId;
    }

    public String getLblTransDate() {
        return lblTransDate;
    }

    public void setLblTransDate(String lblTransDate) {
        this.lblTransDate = lblTransDate;
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

    public java.util.LinkedHashMap getTransactionDetailsTO() {
        return transactionDetailsTO;
    }

    public void setTransactionDetailsTO(java.util.LinkedHashMap transactionDetailsTO) {
        this.transactionDetailsTO = transactionDetailsTO;
    }

    public LinkedHashMap getDeletedTransactionDetailsTO() {
        return deletedTransactionDetailsTO;
    }

    public void setDeletedTransactionDetailsTO(LinkedHashMap deletedTransactionDetailsTO) {
        this.deletedTransactionDetailsTO = deletedTransactionDetailsTO;
    }

    public boolean isRdoTransactionType_Credit() {
        return rdoTransactionType_Credit;
    }

    public void setRdoTransactionType_Credit(boolean rdoTransactionType_Credit) {
        this.rdoTransactionType_Credit = rdoTransactionType_Credit;
    }

    public boolean isRdoTransactionType_Debit() {
        return rdoTransactionType_Debit;
    }

    public void setRdoTransactionType_Debit(boolean rdoTransactionType_Debit) {
        this.rdoTransactionType_Debit = rdoTransactionType_Debit;
    }

    public Double getAmount() {
        return amount;
    }

    public void setAmount(Double amount) {
        this.amount = amount;
    }

    public String getBatchID() {
        return batchID;
    }

    public void setBatchID(String batchID) {
        this.batchID = batchID;
    }
    private static PFTransferOB pfTransferOB;

    static {
        try {
            log.info("In PFTransferOB Declaration");
            currDt = ClientUtil.getCurrentDate();
            pfTransferOB = new PFTransferOB();
        } catch (Exception e) {
            parseException.logException(e, true);
        }
    }

    public static PFTransferOB getInstance() {
        return pfTransferOB;
    }

    /**
     * Creates a new instance of PFTransferOB
     */
    public PFTransferOB() {

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
        operationMap.put(CommonConstants.JNDI, "PayrollJNDI");
        operationMap.put(CommonConstants.HOME, "payroll.PayrollHome");
        operationMap.put(CommonConstants.REMOTE, "payroll.Payroll");
    }

    //Do display the Data from the Database, in UI
    public void populateData(HashMap hashmap) throws Exception {
        log.info("In populateData()");
        setTxtPfNo(CommonUtil.convertObjToStr(hashmap.get("PF_NO")));
        setTxtBalance(CommonUtil.convertObjToDouble(hashmap.get("BALANCE")));
        setTxtAmount(CommonUtil.convertObjToDouble(hashmap.get("AMOUNT")));
        setPfTransType(CommonUtil.convertObjToStr(hashmap.get("PF_TRANS_TYPE")));
        setTransMode(CommonUtil.convertObjToStr(hashmap.get("TRANS_MODE")));
        setTransType(CommonUtil.convertObjToStr(hashmap.get("TRANS_TYPE")));
//        setTxtBalance(CommonUtil.convertObjToDouble(hashmap.get("OPEN_BAL")));
        if (CommonUtil.convertObjToStr(hashmap.get("TRANS_TYPE")).equals("DEBIT")) {
            setRdoTransactionType_Debit(true);
        } else if (CommonUtil.convertObjToStr(hashmap.get("TRANS_TYPE")).equals("CREDIT")) {
            setRdoTransactionType_Credit(true);
        }

        HashMap mapData = null;
        try {
            mapData = proxy.executeQuery(hashmap, operationMap);
            //trans details
            if (mapData.containsKey("TransactionTO")) {
                List list = (List) mapData.get("TransactionTO");
                if (!list.isEmpty()) {
                    transactionOB.setDetails(list);
                }
            }
        } catch (Exception e) {
            setResult(ClientConstants.ACTIONTYPE_FAILED);
            // parseException.logException(e,true);
            System.out.println("Error in populateData():" + e);
        }
        ttNotifyObservers();
    }
    
    public HashMap getPfDetails(HashMap dataMap) throws SQLException {
        HashMap resultMap = new HashMap();
        List pfIdList = ClientUtil.executeQuery("getPFId", dataMap);
        String pfId = "";
        String empId = "";
        if (pfIdList != null && pfIdList.size() > 0) {
            resultMap = (HashMap) pfIdList.get(0);
            pfId = CommonUtil.convertObjToStr(resultMap.get("PF_ID"));
            empId = CommonUtil.convertObjToStr(resultMap.get("EMP_ID"));
            return resultMap;
        }
        return null;
    }

    private PFTransferTO setPFTransferData() {
        log.info("In setPFTransferData()");
        final PFTransferTO objPFTransferTO = new PFTransferTO();
        try {
            objPFTransferTO.setPfId(getPfId());
            objPFTransferTO.setEmpId(getEmpId());
            objPFTransferTO.setPfNo(getTxtPfNo());
            objPFTransferTO.setBalance(getTxtBalance());
            objPFTransferTO.setAmount(getTxtAmount());
            if (isRdoTransactionType_Debit() == true) {
                objPFTransferTO.setTransType(DEBIT);
            } else if (isRdoTransactionType_Credit() == true) {
                objPFTransferTO.setTransType(CREDIT);
            }

        } catch (Exception e) {
            e.printStackTrace();
            parseException.logException(e, true);
            //e.printStackTrace();
        }
        return objPFTransferTO;
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
        PFTransferTO objPFTransferTo = null;
        objPFTransferTo = setPFTransferData();
        objPFTransferTo.setCommand(getCommand());
        objPFTransferTo.setStatus(getAction());
        objPFTransferTo.setStatusBy(TrueTransactMain.USER_ID);
        objPFTransferTo.setStatusDt(currDt);
        final HashMap data = new HashMap();
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
        data.put("pftransfer", objPFTransferTo);
        data.put(CommonConstants.MODULE, getModule());
        data.put(CommonConstants.SELECTED_BRANCH_ID, getSelectedBranchID());
        data.put(CommonConstants.BRANCH_ID, TrueTransactMain.BRANCH_ID);
        data.put("SCREEN_NAME", "PF_TRANSFER");
        if (actionType != ClientConstants.ACTIONTYPE_CANCEL || getAuthorizeMap() != null) {
            data.put(CommonConstants.AUTHORIZEMAP, getAuthorizeMap());
        }
        if (getAuthMap() != null && getAuthMap().size() > 0) {
            if (getAuthMap() != null) {
                data.put(CommonConstants.AUTHORIZEMAP, getAuthMap());
            }
            if (allowedTransactionDetailsTO != null && allowedTransactionDetailsTO.size() > 0) {
                if (transactionDetailsTO == null) {
                    transactionDetailsTO = new LinkedHashMap();
                }
                transactionDetailsTO.put(NOT_DELETED_TRANS_TOs, allowedTransactionDetailsTO);
                data.put("TransactionTO", transactionDetailsTO);
                allowedTransactionDetailsTO = null;
            }
            authMap = null;
        }
        HashMap proxyResultMap = proxy.execute(data, operationMap);
        setProxyReturnMap(proxyResultMap);
        setResult(actionType);
        resetForm();
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
            case ClientConstants.ACTIONTYPE_AUTHORIZE:
                command = AUTHORIZE;
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
        setTxtPfNo("");
        setTxtBalance(0.0);
        setTxtAmount(0.0);

    }

    public double getTxtAmount() {
        return txtAmount;
    }

    public void setTxtAmount(double txtAmount) {
        this.txtAmount = txtAmount;
    }

    public double getTxtBalance() {
        return txtBalance;
    }

    public void setTxtBalance(double txtBalance) {
        this.txtBalance = txtBalance;
    }

    public String getTxtPfNo() {
        return txtPfNo;
    }

    public void setTxtPfNo(String txtPfNo) {
        this.txtPfNo = txtPfNo;
    }
}
