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

package com.see.truetransact.ui.sysadmin.emptransfer;

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

/**
 *
 * @author  
 *
 */
public class EmpTransferOB extends CObservable {
    private String txtEmpTransferID = "";
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
    
    private ComboBoxModel cbmTransferBran;
    private String cboTransferBran="";
    private ComboBoxModel cbmRoleInCurrBran;
    private String cboRoleInCurrBran="";
    private ComboBoxModel cbmRoleInTranBran;
    private String cboRoleInTranBran="";
    private HashMap _authorizeMap;
    private int result;
    private String lblStatus = ClientConstants.ACTION_STATUS[ClientConstants.ACTIONTYPE_CANCEL];
    private String txtNewAcctName = "";
    
    private final static Logger log = Logger.getLogger(EmpTransferOB.class);
    private final static ClientParseException parseException = ClientParseException.getInstance();
    private HashMap map;
    private ProxyFactory proxy;
    //for filling dropdown
    private HashMap lookupMap;
    private HashMap param;
    private HashMap keyValue;
    private ArrayList key;
    private ArrayList value;
    private int actionType;
    private EmpTransferOB empTransferOB;
    EmpTransferRB objEmpTransferRB = new EmpTransferRB();
    private static final CommonRB objCommonRB = new CommonRB();
    private EmpTransferTO objEmpTransferTO;
    private Date currDt = null;
    /** Creates a new instance of TDS MiantenanceOB */
    public EmpTransferOB() {
        try {
            currDt = ClientUtil.getCurrentDate();
            proxy = ProxyFactory.createProxy();
            
            map = new HashMap();
            map.put(CommonConstants.JNDI, "EmpTransferJNDI");
            map.put(CommonConstants.HOME, "EmpTransferHome");
            map.put(CommonConstants.REMOTE, "EmpTransfer");
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
            param.put(CommonConstants.MAP_NAME,"getAllBran");
            HashMap where = new HashMap();
            where.put("BRANCH_ID", ProxyParameters.BRANCH_ID);
            param.put(CommonConstants.PARAMFORQUERY, where);
            where = null;
            keyValue = ClientUtil.populateLookupData(param);
            fillData((HashMap)keyValue.get(CommonConstants.DATA));
            cbmTransferBran = new ComboBoxModel(key,value);
            
            param.put(CommonConstants.MAP_NAME,"getRoleEmp");
            HashMap wheres = new HashMap();
            param.put(CommonConstants.PARAMFORQUERY, wheres);
            wheres = null;
            keyValue = ClientUtil.populateLookupData(param);
            fillData((HashMap)keyValue.get(CommonConstants.DATA));
            cbmRoleInTranBran = new ComboBoxModel(key,value);
            cbmRoleInCurrBran = new ComboBoxModel(key,value);
     
    }
     private void fillData(HashMap keyValue)  throws Exception{
        key = (ArrayList)keyValue.get("KEY");
        value = (ArrayList)keyValue.get("VALUE");
    }
    
    /** To get data for comboboxes */
    private HashMap populateData(HashMap obj)  throws Exception{
        keyValue = proxy.executeQuery(obj,lookupMap);
        log.info("Got HashMap");
        return keyValue;
   }
    
  
   
  
    public void ttNotifyObservers(){
        notifyObservers();
    }
    
    /** To perform the necessary operation */
    public void doAction() {
        try {
          
            if( actionType != ClientConstants.ACTIONTYPE_CANCEL || get_authorizeMap() != null){
                doActionPerform();
            }
        } catch (Exception e) {
            setResult(ClientConstants.ACTIONTYPE_FAILED);
            parseException.logException(e,true);
        }
    }
    
        
    /** To perform the necessary action */
    private void doActionPerform() throws Exception{
        final EmpTransferTO objEmpTransferTO = new EmpTransferTO();
        final HashMap data = new HashMap();
         data.put("COMMAND",getCommand());
        if(get_authorizeMap() == null){
         data.put("EmpTransfer",setEmpTranferData());
        }
        else{
        data.put(CommonConstants.AUTHORIZEMAP, get_authorizeMap());
        }
        System.out.println("data in EmpTransfer OB : " + data);
        HashMap proxyResultMap = proxy.execute(data, map);
        setProxyReturnMap(proxyResultMap);
        if (proxyResultMap != null  &&  getCommand()!=null && getCommand().equalsIgnoreCase("INSERT")){
                ClientUtil.showMessageWindow("Transaction No. : " + CommonUtil.convertObjToStr(proxyResultMap.get("LEAVE_ID")));
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
        // System.out.println("command : " + command);
        return action;
    }
    
   
    
    /** To retrieve a particular customer's accountclosing record */
    public void getData(HashMap whereMap) {
         try{
            final HashMap data = (HashMap)proxy.executeQuery(whereMap,map);
            objEmpTransferTO = (EmpTransferTO) ((List) data.get("EmpTransferTO")).get(0);
            populateEmpTransferData(objEmpTransferTO);
            ttNotifyObservers();
        }catch(Exception e){
            parseException.logException(e,true);
        }
    }
    
    /** To populate data into the screen */
    public EmpTransferTO setEmpTranferData() {
        
        final EmpTransferTO objEmpTransferTO = new EmpTransferTO();
        try{
           if (isRdoApp_Yes() == true) objEmpTransferTO.setApplType("APPLICATION");
           else objEmpTransferTO.setApplType("OFFICIAL");
            objEmpTransferTO.setBranCode(TrueTransactMain.BRANCH_ID);
            objEmpTransferTO.setStatusBy(ProxyParameters.USER_ID);
            objEmpTransferTO.setCurrBran(getTxtCurrBran());
         
           objEmpTransferTO.setDoj(DateUtil.getDateMMDDYYYY(getTxtDoj()));
           objEmpTransferTO.setEmpID(getTxtEmpID());
           objEmpTransferTO.setLastWorkingDay(DateUtil.getDateMMDDYYYY(getTxtLastWorkingDay()));
           objEmpTransferTO.setCommand(getCommand());
           objEmpTransferTO.setStatus(getAction());
           objEmpTransferTO.setStatusBy(TrueTransactMain.USER_ID);
           objEmpTransferTO.setStatusDt(ClientUtil.getCurrentDateWithTime());
           objEmpTransferTO.setRoleInCurrBran(CommonUtil.convertObjToStr(getCbmRoleInCurrBran().getKeyForSelected()));
           objEmpTransferTO.setRoleInTransferBran(CommonUtil.convertObjToStr(getCbmRoleInTranBran().getKeyForSelected()));
           objEmpTransferTO.setTransferBran(CommonUtil.convertObjToStr(getCbmTransferBran().getKeyForSelected()));
           objEmpTransferTO.setEmpName(getEmpName());
           objEmpTransferTO.setCurrBranName(getCurrBranName());
           if(getCommand().equalsIgnoreCase("INSERT")){
           objEmpTransferTO.setCreatedBy(TrueTransactMain.USER_ID);
           objEmpTransferTO.setCreatedDt(currDt);
           }
            if(getCommand().equalsIgnoreCase("UPDATE")||getCommand().equalsIgnoreCase("DELETE")){
              objEmpTransferTO.setEmpTransferID(getTxtEmpTransferID());  
            }
        }catch(Exception e){
            log.info("Error In setEmpTranferData()");
            e.printStackTrace();
        }
        return objEmpTransferTO;
    }
    
    private void populateEmpTransferData(EmpTransferTO objEmpTransferTO) throws Exception{
        this.setTxtEmpTransferID(CommonUtil.convertObjToStr(objEmpTransferTO.getEmpTransferID()));
        this.setTxtEmpID(CommonUtil.convertObjToStr(objEmpTransferTO.getEmpID()));
        this.setTxtStatusBy(CommonUtil.convertObjToStr(objEmpTransferTO.getStatusBy()));
        getCbmRoleInCurrBran().setKeyForSelected(CommonUtil.convertObjToStr(objEmpTransferTO.getRoleInCurrBran()));
        getCbmRoleInTranBran().setKeyForSelected(CommonUtil.convertObjToStr(objEmpTransferTO.getRoleInTransferBran()));
        getCbmTransferBran().setKeyForSelected(CommonUtil.convertObjToStr(objEmpTransferTO.getTransferBran()));

        this.setCreatedDt(CommonUtil.convertObjToStr(objEmpTransferTO.getCreatedDt()));
        this.setTxtDoj(CommonUtil.convertObjToStr(objEmpTransferTO.getDoj()));
        this.setTxtCurrBran(CommonUtil.convertObjToStr(objEmpTransferTO.getCurrBran()));
        this.setTxtLastWorkingDay(CommonUtil.convertObjToStr(objEmpTransferTO.getLastWorkingDay()));
        if(objEmpTransferTO.getApplType()!=null)
            if(objEmpTransferTO.getApplType().equalsIgnoreCase("APPLICATION")){
                setRdoApp_Yes(true);
                setRdoOff_Yes(false);
            }
            else{
                setRdoOff_Yes(true);
               setRdoApp_Yes(false);
            }
        this.setEmpName(CommonUtil.convertObjToStr(objEmpTransferTO.getEmpName()));
        this.setCurrBranName(CommonUtil.convertObjToStr(objEmpTransferTO.getCurrBranName()));
        this.setStatusBy(CommonUtil.convertObjToStr(objEmpTransferTO.getStatusBy()));
        setChanged();
        notifyObservers();
    }
    
    
    public void resetForm(){
       setTxtCurrBran("");
       setCboRoleInCurrBran("");
       setCboRoleInTranBran("");
       setCboTransferBran("");
       cboRoleInCurrBran="";
       cboRoleInTranBran="";
       cboTransferBran="";
       setTxtEmpID("");
       setTxtDoj("");
       setTxtEmpTransferID("");
       setTxtLastWorkingDay("");
       setRdoOff_Yes(true);
       setRdoApp_Yes(false);
       setEmpName("");
       setCurrBranName("");
       setChanged();
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
   
    
    /**
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
     * Getter for property txtEmpTransferID.
     * @return Value of property txtEmpTransferID.
     */
    public java.lang.String getTxtEmpTransferID() {
        return txtEmpTransferID;
    }
    
    /**
     * Setter for property txtEmpTransferID.
     * @param txtEmpTransferID New value of property txtEmpTransferID.
     */
    public void setTxtEmpTransferID(java.lang.String txtEmpTransferID) {
        this.txtEmpTransferID = txtEmpTransferID;
    }
    
    /**
     * Getter for property txtEmpID.
     * @return Value of property txtEmpID.
     */
    public java.lang.String getTxtEmpID() {
        return txtEmpID;
    }
    
    /**
     * Setter for property txtEmpID.
     * @param txtEmpID New value of property txtEmpID.
     */
    public void setTxtEmpID(java.lang.String txtEmpID) {
        this.txtEmpID = txtEmpID;
    }
    
    /**
     * Getter for property txtCurrBran.
     * @return Value of property txtCurrBran.
     */
    public java.lang.String getTxtCurrBran() {
        return txtCurrBran;
    }
    
    /**
     * Setter for property txtCurrBran.
     * @param txtCurrBran New value of property txtCurrBran.
     */
    public void setTxtCurrBran(java.lang.String txtCurrBran) {
        this.txtCurrBran = txtCurrBran;
    }
    
    /**
     * Getter for property txtLastWorkingDay.
     * @return Value of property txtLastWorkingDay.
     */
    public java.lang.String getTxtLastWorkingDay() {
        return txtLastWorkingDay;
    }
    
    /**
     * Setter for property txtLastWorkingDay.
     * @param txtLastWorkingDay New value of property txtLastWorkingDay.
     */
    public void setTxtLastWorkingDay(java.lang.String txtLastWorkingDay) {
        this.txtLastWorkingDay = txtLastWorkingDay;
    }
    
    /**
     * Getter for property txtDoj.
     * @return Value of property txtDoj.
     */
    public java.lang.String getTxtDoj() {
        return txtDoj;
    }
    
    /**
     * Setter for property txtDoj.
     * @param txtDoj New value of property txtDoj.
     */
    public void setTxtDoj(java.lang.String txtDoj) {
        this.txtDoj = txtDoj;
    }
    
    /**
     * Getter for property rdoApp_Yes.
     * @return Value of property rdoApp_Yes.
     */
    public boolean isRdoApp_Yes() {
        return rdoApp_Yes;
    }
    
    /**
     * Setter for property rdoApp_Yes.
     * @param rdoApp_Yes New value of property rdoApp_Yes.
     */
    public void setRdoApp_Yes(boolean rdoApp_Yes) {
        this.rdoApp_Yes = rdoApp_Yes;
    }
    
    /**
     * Getter for property rdoOff_Yes.
     * @return Value of property rdoOff_Yes.
     */
    public boolean isRdoOff_Yes() {
        return rdoOff_Yes;
    }
    
    /**
     * Setter for property rdoOff_Yes.
     * @param rdoOff_Yes New value of property rdoOff_Yes.
     */
    public void setRdoOff_Yes(boolean rdoOff_Yes) {
        this.rdoOff_Yes = rdoOff_Yes;
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
    
    /**
     * Getter for property cbmTransferBran.
     * @return Value of property cbmTransferBran.
     */
    public com.see.truetransact.clientutil.ComboBoxModel getCbmTransferBran() {
        return cbmTransferBran;
    }
    
    /**
     * Setter for property cbmTransferBran.
     * @param cbmTransferBran New value of property cbmTransferBran.
     */
    public void setCbmTransferBran(com.see.truetransact.clientutil.ComboBoxModel cbmTransferBran) {
        this.cbmTransferBran = cbmTransferBran;
    }
    
    /**
     * Getter for property cboTransferBran.
     * @return Value of property cboTransferBran.
     */
    public java.lang.String getCboTransferBran() {
        return cboTransferBran;
    }
    
    /**
     * Setter for property cboTransferBran.
     * @param cboTransferBran New value of property cboTransferBran.
     */
    public void setCboTransferBran(java.lang.String cboTransferBran) {
        this.cboTransferBran = cboTransferBran;
    }
    
    /**
     * Getter for property cbmRoleInCurrBran.
     * @return Value of property cbmRoleInCurrBran.
     */
    public com.see.truetransact.clientutil.ComboBoxModel getCbmRoleInCurrBran() {
        return cbmRoleInCurrBran;
    }
    
    /**
     * Setter for property cbmRoleInCurrBran.
     * @param cbmRoleInCurrBran New value of property cbmRoleInCurrBran.
     */
    public void setCbmRoleInCurrBran(com.see.truetransact.clientutil.ComboBoxModel cbmRoleInCurrBran) {
        this.cbmRoleInCurrBran = cbmRoleInCurrBran;
    }
    
    /**
     * Getter for property cboRoleInCurrBran.
     * @return Value of property cboRoleInCurrBran.
     */
    public java.lang.String getCboRoleInCurrBran() {
        return cboRoleInCurrBran;
    }
    
    /**
     * Setter for property cboRoleInCurrBran.
     * @param cboRoleInCurrBran New value of property cboRoleInCurrBran.
     */
    public void setCboRoleInCurrBran(java.lang.String cboRoleInCurrBran) {
        this.cboRoleInCurrBran = cboRoleInCurrBran;
    }
    
    /**
     * Getter for property cbmRoleInTranBran.
     * @return Value of property cbmRoleInTranBran.
     */
    public com.see.truetransact.clientutil.ComboBoxModel getCbmRoleInTranBran() {
        return cbmRoleInTranBran;
    }
    
    /**
     * Setter for property cbmRoleInTranBran.
     * @param cbmRoleInTranBran New value of property cbmRoleInTranBran.
     */
    public void setCbmRoleInTranBran(com.see.truetransact.clientutil.ComboBoxModel cbmRoleInTranBran) {
        this.cbmRoleInTranBran = cbmRoleInTranBran;
    }
    
    /**
     * Getter for property cboRoleInTranBran.
     * @return Value of property cboRoleInTranBran.
     */
    public java.lang.String getCboRoleInTranBran() {
        return cboRoleInTranBran;
    }
    
    /**
     * Setter for property cboRoleInTranBran.
     * @param cboRoleInTranBran New value of property cboRoleInTranBran.
     */
    public void setCboRoleInTranBran(java.lang.String cboRoleInTranBran) {
        this.cboRoleInTranBran = cboRoleInTranBran;
    }
    
    /**
     * Getter for property empName.
     * @return Value of property empName.
     */
    public java.lang.String getEmpName() {
        return empName;
    }
    
    /**
     * Setter for property empName.
     * @param empName New value of property empName.
     */
    public void setEmpName(java.lang.String empName) {
        this.empName = empName;
    }
    
    /**
     * Getter for property currBranName.
     * @return Value of property currBranName.
     */
    public java.lang.String getCurrBranName() {
        return currBranName;
    }
    
    /**
     * Setter for property currBranName.
     * @param currBranName New value of property currBranName.
     */
    public void setCurrBranName(java.lang.String currBranName) {
        this.currBranName = currBranName;
    }
    
    }
    
  
    
   