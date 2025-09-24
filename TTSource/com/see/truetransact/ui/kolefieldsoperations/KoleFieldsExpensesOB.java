/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition.
 * 
 *
 * EmpTransferOB.java
 *
 * Created on june , 2010, 4:30 PM Swaroop
 */

package com.see.truetransact.ui.kolefieldsoperations;

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
import com.see.truetransact.transferobject.kolefieldsoperations.KoleFieldsExpensesTO;
import com.see.truetransact.ui.common.transaction.TransactionOB;

/**
 *
 * @author  
 *
 */
public class KoleFieldsExpensesOB extends CObservable {
   /** private String txtEmpTransferID = "";
    private String txtEmpID = "";
    private String txtCurrBran = "";
    private String txtLastWorkingDay = "";
    private String txtDoj = "";
    private String txtStatusBy = "";
    private boolean rdoApp_Yes = false;
    private boolean rdoOff_Yes = false;
    private String txtStatus = "";
    private String CreatedDt="";
    private String empName="";
    private String currBranName="";
    */
   //------------------------------------------------------------------------------------------------------------------------------------------ 
    private String cboBoMember="";
    private Double txtSittingFeeAmount=null;
    private String txtdirectorBoardNo="";
    private String txtD1="";
    private String txtD2="";
    private int actionType;
    private String txtStatus = "";
    private int result;
    private HashMap _authorizeMap;
     private  KoleFieldsExpensesTO objKoleFieldsExpensesTO ;
     private String CreatedDt="";
     private String lblStatus = ClientConstants.ACTION_STATUS[ClientConstants.ACTIONTYPE_CANCEL];
     private String txtStatusBy = "";
     private HashMap map;
     private ProxyFactory proxy;
     private final static Logger log = Logger.getLogger(DirectorBoardOB.class);
     private ComboBoxModel cbmSuspenseProduct;
     private HashMap lookupMap;
    private HashMap param;
    private HashMap keyValue;
    private ArrayList key;
    private ArrayList value;
    private int _actionType;
    private TransactionOB transactionOB;
    private LinkedHashMap allowedTransactionDetailsTO = null;  
    private final static ClientParseException parseException = ClientParseException.getInstance();
    
    
    // Update OB Feilds
    private String rdoreceipt = "";
    private String rdoPayment = "";
    private String cboSuspenseProduct = "";
    private String txtAccountNo = "";
    private String tdtLastIntCalcDt = "";
    private String txtIntAmount = "";
    private String txtTransAmount = "";
    private String transType = "";
    
    // End
    
    
    /** Creates a new instance of TDS MiantenanceOB */
    public KoleFieldsExpensesOB() {
        try {
            proxy = ProxyFactory.createProxy();
            
            map = new HashMap();
            map.put(CommonConstants.JNDI, "KoleFieldsExpensesJNDI");
            map.put(CommonConstants.HOME, "KoleFieldExpensesHome");
            map.put(CommonConstants.REMOTE, "KoleFieldExpenses");
           fillDropdown();
            
        } catch (Exception e) {
            parseException.logException(e,true);
        }
    }
    
    /** To fill the comboboxes */
    private void fillDropdown() throws Exception{
      lookupMap = new HashMap();
            lookupMap.put(CommonConstants.JNDI, "LookUpJNDI");            
            lookupMap.put(CommonConstants.HOME, "common.lookup.LookUpHome");
            lookupMap.put(CommonConstants.REMOTE, "common.lookup.LookUp");
            
            HashMap param = new HashMap();
            param.put(CommonConstants.MAP_NAME,"getAllSuspenseProdBehavesLikeLoan");
            HashMap where = new HashMap();
            where.put("BRANCH_ID", ProxyParameters.BRANCH_ID);
            param.put(CommonConstants.PARAMFORQUERY, where);
            where = null;
            keyValue = ClientUtil.populateLookupData(param);
            fillData((HashMap)keyValue.get(CommonConstants.DATA));
            cbmSuspenseProduct = new ComboBoxModel(key,value);      
    }
    
    
    
    
     private void fillData(HashMap keyValue)  throws Exception{
        key = (ArrayList)keyValue.get("KEY");
        value = (ArrayList)keyValue.get("VALUE");
    }
     

  
    public void ttNotifyObservers(){
        notifyObservers();
    }
    
    /** To perform the necessary operation */
    public void doAction() {
        try {
                doActionPerform();
        } catch (Exception e) {
            setResult(ClientConstants.ACTIONTYPE_FAILED);
        }
    }
    

        
    /** To perform the necessary action */
    private void doActionPerform() throws Exception{
        final HashMap data = new HashMap();
        data.put("COMMAND", getCommand());
        data.put("USER", ProxyParameters.USER_ID);
        data.put("SCREEN",getScreen());
        if (get_authorizeMap() == null) {
            data.put("KoleFieldExpensesTO", setKoleFieldsExpensesTO());
            data.put("TransactionTo", allowedTransactionDetailsTO);
        } else {
            data.put(CommonConstants.AUTHORIZEMAP, get_authorizeMap());
        }
        System.out.println("data in KoleField Expenses OB : " + data);
        HashMap proxyResultMap = proxy.execute(data, map);
        setProxyReturnMap(proxyResultMap);
        if (proxyResultMap != null && getCommand() != null && getCommand().equalsIgnoreCase("INSERT")) {
            ClientUtil.showMessageWindow("Transaction No. : " + CommonUtil.convertObjToStr(proxyResultMap.get("KOLE_FIELD_EXPENSE_ID")));
        }
        setResult(getActionType());
        setResult(actionType);
    }
       
    private String getCommand(){
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
    
    private String getAction(){
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
        return action;
    }
    
   
    
    /** To retrieve a particular customer's accountclosing record */
    public void getData(HashMap whereMap) {
         try{
            final HashMap data = (HashMap)proxy.executeQuery(whereMap,map);
           objKoleFieldsExpensesTO = (KoleFieldsExpensesTO)((List) data.get("KoleFieldsExpensesTO")).get(0);
             System.out.println("objKoleFieldsExpensesTO nithya :: " + objKoleFieldsExpensesTO);   
            populateKoleFieldExpenseData(objKoleFieldsExpensesTO);
            List list = (List) data.get("TransactionTO");
            System.out.println("list :: " + list);                 
            transactionOB.setDetails(list);
            ttNotifyObservers();
        }catch(Exception e){
//            parseException.logException(e,true);
        }
    }
    
    /** To populate data into the screen */
    //-----------------------------------------------------------------------------------------------------------------------------------------------

    
    
    
    
     public KoleFieldsExpensesTO setKoleFieldsExpensesTO() {
        
        final KoleFieldsExpensesTO objKoleFieldsExpensesTO = new KoleFieldsExpensesTO();
        try{
            objKoleFieldsExpensesTO.setStatusBy(ProxyParameters.USER_ID);
            objKoleFieldsExpensesTO.setAcctNum(getTxtAccountNo());
            objKoleFieldsExpensesTO.setTransType(getTransType());
            objKoleFieldsExpensesTO.setIntAmount(CommonUtil.convertObjToDouble(getTxtIntAmount()));
            objKoleFieldsExpensesTO.setTransAmount(CommonUtil.convertObjToDouble(getTxtTransAmount()));
            objKoleFieldsExpensesTO.setLastIntCalcDt(DateUtil.getDateMMDDYYYY(getTdtLastIntCalcDt()));
            objKoleFieldsExpensesTO.setCommand(getCommand());
            objKoleFieldsExpensesTO.setStatus(getAction());
            objKoleFieldsExpensesTO.setStatusBy(TrueTransactMain.USER_ID);
            objKoleFieldsExpensesTO.setStatusDt(ClientUtil.getCurrentDateWithTime());
            objKoleFieldsExpensesTO.setProdId(getCboSuspenseProduct());
       }catch(Exception e){           
            e.printStackTrace();
        }
        return objKoleFieldsExpensesTO;
    }
    
    private void populateKoleFieldExpenseData(KoleFieldsExpensesTO objKoleFieldsExpensesTO) throws Exception {
        System.out.println("objKoleFieldsExpensesTO here :: " + objKoleFieldsExpensesTO);
        this.setTxtAccountNo(objKoleFieldsExpensesTO.getAcctNum());
        this.setTxtTransAmount(CommonUtil.convertObjToStr(objKoleFieldsExpensesTO.getTransAmount()));
        this.setTdtLastIntCalcDt(CommonUtil.convertObjToStr(objKoleFieldsExpensesTO.getLastIntCalcDt()));
        this.setTxtIntAmount(CommonUtil.convertObjToStr(objKoleFieldsExpensesTO.getIntAmount()));
        this.setTransType(CommonUtil.convertObjToStr(objKoleFieldsExpensesTO.getTransType()));
        this.setCboSuspenseProduct(CommonUtil.convertObjToStr(objKoleFieldsExpensesTO.getProdId()));
        setChanged();
        notifyObservers();
    }
    
    
    public void resetForm(){
        setCboBoMember("");
        setTxtSittingFeeAmount(null);
        setTxtMeetingDate("");
        setTxtPaidDate("");
        setTxtIntAmount("");
        setTxtTransAmount("");
        setCboSuspenseProduct("");
        setTxtAccountNo("");
        setTransType("");
        setTdtLastIntCalcDt(null);
        //setTransactionOB(null);
        allowedTransactionDetailsTO = null;
        setChanged();
        set_authorizeMap(null);        
        ttNotifyObservers();
    }
    
    /** To set the status based on ActionType, either New, Edit, etc., */
    public void setStatus(){
       this.setLblStatus( ClientConstants.ACTION_STATUS[this.getActionType()]);
        ttNotifyObservers();
    }
    
    /** To update the Status based on result performed by doAction() method */
    public void setResultStatus(){
       this.setLblStatus(ClientConstants.RESULT_STATUS[this.getResult()]);
        ttNotifyObservers();
    }
    
    /**
     * Getter for property txtStatusBy.
     * @return Value of property txtStatusBy.
     */
    public java.lang.String getTxtStatusBy() {
        return txtStatusBy;
    }
    
    /**
     * Setter for property txtStatusBy.
     * @param txtStatusBy New value of property txtStatusBy.
     */
    public void setTxtStatusBy(java.lang.String txtStatusBy) {
        this.txtStatusBy = txtStatusBy;
    }
    
  
    /**
     * Getter for property actionType.
     * @return Value of property actionType.
     */
    public int getActionType() {
        return actionType;
    }
    
    /**
     * Setter for property actionType.
     * @param actionType New value of property actionType.
     */
    public void setActionType(int actionType) {
        this.actionType = actionType;
    }
    
    /**
     * Getter for property lblStatus.
     * @return Value of property lblStatus.
     */
    public java.lang.String getLblStatus() {
        return lblStatus;
    }
    
    /**
     * Setter for property lblStatus.
     * @param lblStatus New value of property lblStatus.
     */
    public void setLblStatus(java.lang.String lblStatus) {
        this.lblStatus = lblStatus;
    }
    
    /**
     * Getter for property _authorizeMap.
     * @return Value of property _authorizeMap.
     */
    public java.util.HashMap get_authorizeMap() {
        return _authorizeMap;
    }
    
    /**
     * Setter for property _authorizeMap.
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
     * @return Value of property CreatedDt.
     */
    public java.lang.String getCreatedDt() {
        return CreatedDt;
    }
    
    /**
     * Setter for property CreatedDt.
     * @param CreatedDt New value of property CreatedDt.
     */
    public void setCreatedDt(java.lang.String CreatedDt) {
        this.CreatedDt = CreatedDt;
    }

  
    
    /**
     * Getter for property result.
     * @return Value of property result.
     */
    public int getResult() {
        return result;
    }
    
    /**
     * Setter for property result.
     * @param result New value of property result.
     */
    public void setResult(int result) {
        this.result = result;
    }
    
    /**
     * Getter for property txtStatus.
     * @return Value of property txtStatus.
     */
    public java.lang.String getTxtStatus() {
        return txtStatus;
    }
    
    /**
     * Setter for property txtStatus.
     * @param txtStatus New value of property txtStatus.
     */
    public void setTxtStatus(java.lang.String txtStatus) {
        this.txtStatus = txtStatus;
    }
    
    
 
    
    public java.lang.Double getTxtSittingFeeAmount() {
        return txtSittingFeeAmount;
    }
    
 
    public void setTxtSittingFeeAmount(java.lang.Double txtSittingFeeAmount) {
        this.txtSittingFeeAmount = txtSittingFeeAmount;
    }
    
     public java.lang.String getTxtMeetingDate() {
        return txtD1;
    }
    
  
    public void setTxtMeetingDate(java.lang.String txtD1) {
        this.txtD1 = txtD1;
    }
    
     public java.lang.String getTxtPaidDate() {
        return txtD2;
    }
    
   
    public void setTxtPaidDate(java.lang.String txtD2) {
        this.txtD2 = txtD2;
    }
    
    /**
     * Getter for property cboBoMember.
     * @return Value of property cboBoMember.
     */
    public String getCboBoMember() {
        return cboBoMember;
    }    

    /**
     * Setter for property cboBoMember.
     * @param cboBoMember New value of property cboBoMember.
     */
    public void setCboBoMember(String cboBoMember) {
        this.cboBoMember = cboBoMember;
    }    
   
    /**
     * Getter for property txtdirectorBoardNo.
     * @return Value of property txtdirectorBoardNo.
     */
    public String getTxtdirectorBoardNo() {
        return txtdirectorBoardNo;
    }
    
    /**
     * Setter for property txtdirectorBoardNo.
     * @param txtdirectorBoardNo New value of property txtdirectorBoardNo.
     */
    public void setTxtdirectorBoardNo(String txtdirectorBoardNo) {
        this.txtdirectorBoardNo = txtdirectorBoardNo;
    }

    public ComboBoxModel getCbmSuspenseProduct() {
        return cbmSuspenseProduct;
    }

    public void setCbmSuspenseProduct(ComboBoxModel cbmSuspenseProduct) {
        this.cbmSuspenseProduct = cbmSuspenseProduct;
    }
    
  
    /**
     * Getter for property _actionType.
     * @return Value of property _actionType.
     */
    public int get_actionType() {
        return _actionType;
    }
    
    /**
     * Setter for property _actionType.
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

    public String getRdoreceipt() {
        return rdoreceipt;
    }

    public void setRdoreceipt(String rdoreceipt) {
        this.rdoreceipt = rdoreceipt;
    }

    public String getRdoPayment() {
        return rdoPayment;
    }

    public void setRdoPayment(String rdoPayment) {
        this.rdoPayment = rdoPayment;
    }

    public String getCboSuspenseProduct() {
        return cboSuspenseProduct;
    }

    public void setCboSuspenseProduct(String cboSuspenseProduct) {
        this.cboSuspenseProduct = cboSuspenseProduct;
    }

    public String getTxtAccountNo() {
        return txtAccountNo;
    }

    public void setTxtAccountNo(String txtAccountNo) {
        this.txtAccountNo = txtAccountNo;
    }

    public String getTdtLastIntCalcDt() {
        return tdtLastIntCalcDt;
    }

    public void setTdtLastIntCalcDt(String tdtLastIntCalcDt) {
        this.tdtLastIntCalcDt = tdtLastIntCalcDt;
    }

    public String getTxtIntAmount() {
        return txtIntAmount;
    }

    public void setTxtIntAmount(String txtIntAmount) {
        this.txtIntAmount = txtIntAmount;
    }

    public String getTxtTransAmount() {
        return txtTransAmount;
    }

    public void setTxtTransAmount(String txtTransAmount) {
        this.txtTransAmount = txtTransAmount;
    }

    public String getTransType() {
        return transType;
    }

    public void setTransType(String transType) {
        this.transType = transType;
    }
    
    
    
    
}
    //-----------------------------------------------------------
   
   
    
    