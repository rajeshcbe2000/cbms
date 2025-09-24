/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition.
 * 
 *
 * LeaveManagementOB.java
 *
 * Created on Fri Jan 07 12:01:51 IST 2005
 */

package com.see.truetransact.ui.sysadmin.leavemanagement;

import com.see.truetransact.uicomponent.CObservable;
import com.see.truetransact.clientexception.ClientParseException;
import com.see.truetransact.commonutil.CommonConstants;
import com.see.truetransact.clientutil.ClientConstants;
import com.see.truetransact.commonutil.DateUtil;
import com.see.truetransact.clientproxy.ProxyFactory;
import com.see.truetransact.clientproxy.ProxyParameters;

import com.see.truetransact.clientutil.ComboBoxModel;
import com.see.truetransact.clientutil.ClientUtil;
import com.see.truetransact.clientutil.EnhancedTableModel;
import com.see.truetransact.transferobject.sysadmin.leavemanagement.LeaveManagementTO;

import com.see.truetransact.ui.TrueTransactMain;
import com.see.truetransact.commonutil.CommonUtil;

import org.apache.log4j.Logger;

import java.util.HashMap;
import java.util.ArrayList;
import java.util.List;
import java.util.Date;
import java.util.ResourceBundle;

/**
 *
 * @author Swaroop
 */

public class LeaveManagementOB extends CObservable{
    
    Date curDate = ClientUtil.getCurrentDate();
    private final static ClientParseException parseException = ClientParseException.getInstance();
    private final static Logger _log = Logger.getLogger(LeaveManagementOB.class);
    private ResourceBundle resourceBundle = java.util.ResourceBundle.getBundle("com.see.truetransact.ui.sysadmin.leavemanagement.LeaveManagementRB", ProxyParameters.LANGUAGE);
    private ProxyFactory proxy = null;
    private ComboBoxModel  cbmDivCalcFrequency,cbmDivAppFrequency,cbmUnclaimedDivPeriod,cbmShareType,cbmNomineePeriod,cbmRefundPeriod,cbmAdditionalShareRefund,cbmLoanType;
    private HashMap map,lookUpHash,keyValue;
    private int _result,_actionType;
    private ArrayList key,value;
    private static LeaveManagementOB objLeaveManagementOB;//Singleton Object
    private String lblStatus = ClientConstants.ACTION_STATUS[ClientConstants.ACTIONTYPE_CANCEL];
//    private Date lstRunDate=null;
    private ArrayList headings;
    private EnhancedTableModel tbmLoanType;
    private HashMap loanDataMap;
    private HashMap deletedMap;
    
    private String typeOfLeave="";
    private String desc="";
    private String cboToBeCredited="";
    private ComboBoxModel cbmToBeCredited;
    private String carryOver="";
    private String txtAcc="";
    private String cboAcc="";
    private ComboBoxModel cbmAcc;
    private String cboParForLeave="";
    private ComboBoxModel cbmParForLeave;
    private String txtFixedPar1="";
    private String txtFixedPar="";
    private String txtPro1="";
    private String txtPro2="";
    private String cboParFixed="";
    private ComboBoxModel cbmParFixed;
    private String txtMaxLeaves="";
    private String txtMaxEncashment="";
    private String txtMaternityCountLimit = "";
    private String leaveID="";
    
    private boolean rdoIntroReq_Yes1 = false;
    private boolean rdoIntroReq_No1 = false;
    private boolean rdoAcc_Yes = false;
    private boolean rdoAcc_No = false;
    private boolean rdoEncash_Yes1 = false;
    private boolean rdoEncash_No1 = false;
    private boolean rdoLtc = false;
    private boolean rdoLfc = false;
    private final String YES = "Y";
    private final String NO = "N";
    private boolean chkLtc = false;
    private boolean chkMaternityLeave = false;
    private boolean rdoFull = false;
    private boolean rdoHalf = false;
   
    
    
    
    
    /** Creates a new instance of LeaveManagementOB */
    private LeaveManagementOB() {
        map = new HashMap();
        map.put(CommonConstants.JNDI, "LeaveManagementJNDI");
        map.put(CommonConstants.HOME, "serverside.product.share.ShareProductHome");
        map.put(CommonConstants.REMOTE, "serverside.proudct.share.ShareProduct");
        try {
            proxy = ProxyFactory.createProxy();
            initUIComboBoxModel();
            fillDropdown();
        } catch (Exception e) {
            parseException.logException(e,true);
        }
    }
    
    static {
        try {
            _log.info("Creating LeaveManagementOB...");
            objLeaveManagementOB= new LeaveManagementOB();
        } catch(Exception e) {
            parseException.logException(e,true);
        }
    }
    
    /** Creating Instances of ComboBoxModel */
    private void initUIComboBoxModel(){
        cbmDivCalcFrequency = new  ComboBoxModel();
        cbmDivAppFrequency = new  ComboBoxModel();
        cbmUnclaimedDivPeriod = new  ComboBoxModel();
        cbmShareType = new  ComboBoxModel();
        cbmNomineePeriod = new  ComboBoxModel();
        cbmRefundPeriod = new  ComboBoxModel();
        cbmAdditionalShareRefund = new  ComboBoxModel();
        cbmLoanType = new  ComboBoxModel();
        
    }
    
    /* Filling up the the ComboBoxModel with key, value */
    private void fillDropdown() throws Exception{
        try{
            _log.info("Inside FillDropDown");
            lookUpHash = new HashMap();
            lookUpHash.put(CommonConstants.MAP_NAME, null);
            final ArrayList lookup_keys = new ArrayList();
            lookup_keys.add("LEAVE_MANGEMENT_CREDIT_TYPE");
            lookup_keys.add("LEAVE_MANGEMENT_LEAVE_PAR");
            lookup_keys.add("LEAVE_MANGEMENT_PERIOD");
            lookUpHash.put(CommonConstants.PARAMFORQUERY, lookup_keys);
            keyValue = ClientUtil.populateLookupData(lookUpHash);
            getKeyValue((HashMap)keyValue.get("LEAVE_MANGEMENT_CREDIT_TYPE"));
            cbmToBeCredited  = new ComboBoxModel(key,value);
            getKeyValue((HashMap) keyValue.get("LEAVE_MANGEMENT_LEAVE_PAR"));
            cbmParForLeave = new ComboBoxModel(key,value);
            getKeyValue((HashMap) keyValue.get("LEAVE_MANGEMENT_PERIOD"));
            cbmAcc= new ComboBoxModel(key,value);
            cbmParFixed= new ComboBoxModel(key,value);
        }catch(NullPointerException e){
            parseException.logException(e,true);
        }catch(Exception e){
            parseException.logException(e,true);
        }
    }
    
    /* Return the key,value(Array List) to be used up in ComboBoxModel */
    private void getKeyValue(HashMap keyValue)  throws Exception{
        key = (ArrayList)keyValue.get(CommonConstants.KEY);
        value = (ArrayList)keyValue.get(CommonConstants.VALUE);
    }
   
    /**
     * Returns an instance of LeaveManagementOB.
     * @return  LeaveManagementOB
     */
    
    public static LeaveManagementOB getInstance()throws Exception{
        return objLeaveManagementOB;
    }
     
    public void setResult(int result) {
        _result = result;
        setResultStatus();
        setChanged();
    }
    
    public int getActionType(){
        return _actionType;
    }
    
    public void setActionType(int actionType) {
        _actionType = actionType;
        setStatus();
        setChanged();
    }
    
    /** To set the status based on ActionType, either New, Edit, etc., */
    public void setStatus(){
        this.setLblStatus( ClientConstants.ACTION_STATUS[this.getActionType()]);
    }
    
    /** To update the Status based on result performed by doAction() method */
    public void setResultStatus(){
        this.setLblStatus(ClientConstants.RESULT_STATUS[this.getResult()]);
    }
    
    /** Getter for property lblStatus.
     * @return Value of property lblStatus.
     *
     */
    public java.lang.String getLblStatus() {
        return lblStatus;
    }
    
    /** Setter for property lblStatus.
     * @param lblStatus New value of property lblStatus.
     *
     */
    public void setLblStatus(java.lang.String lblStatus) {
        this.lblStatus = lblStatus;
        setChanged();
    }
    
    public void resetStatus(){
        this.setLblStatus(ClientConstants.ACTION_STATUS[0]);
    }
    
    public int getResult(){
        return _result;
    }
    
    /** Returns an Instance of LeaveManagementTO */
    public LeaveManagementTO getLeaveManagementTO(String command){
        LeaveManagementTO objLeaveManagementTO = new LeaveManagementTO();
        objLeaveManagementTO.setTypeOfLeave(getTypeOfLeave());
        objLeaveManagementTO.setDesc(getDesc());
        objLeaveManagementTO.setCboToBeCredited(CommonUtil.convertObjToStr(getCbmToBeCredited().getKeyForSelected()));
        if(CommonUtil.convertObjToStr(getCbmToBeCredited().getKeyForSelected()).equalsIgnoreCase("CALENDAR_YR")){
            Date dt=(Date) curDate.clone();
            dt.setDate(01);
            dt.setMonth(0);
            dt.setYear(2000);
            objLeaveManagementTO.setDateOfCrediting(dt); 
        }
        else if(CommonUtil.convertObjToStr(getCbmToBeCredited().getKeyForSelected()).equalsIgnoreCase("FINANCIAL_YR_END")){
             Date dt=(Date) curDate.clone();
             dt.setDate(01);
             dt.setMonth(3);
             dt.setYear(2000);
             objLeaveManagementTO.setDateOfCrediting(dt);
        }
        if (isRdoAcc_Yes() == true) objLeaveManagementTO.setAcc(YES);
        else objLeaveManagementTO.setAcc(NO);
        if (isRdoEncash_Yes1() == true) objLeaveManagementTO.setEncash(YES);
        else objLeaveManagementTO.setEncash(NO);
        if (isRdoIntroReq_Yes1() == true) objLeaveManagementTO.setIntroReq(YES);
        else objLeaveManagementTO.setIntroReq(NO);
//        if (isRdoLtc() == true) objLeaveManagementTO.setLeaveEncashmentType("LTC");
//        else objLeaveManagementTO.setLeaveEncashmentType("LFC");
        if(isChkLtc()) objLeaveManagementTO.setLeaveEncashmentType("Y");
        else objLeaveManagementTO.setLeaveEncashmentType("N");
        
        if(isChkMaternityLeave()){
            objLeaveManagementTO.setChkMaternityLeave("Y");
        }else{
            objLeaveManagementTO.setChkMaternityLeave("N");
        }
      
        objLeaveManagementTO.setTxtAcc(getTxtAcc());
        objLeaveManagementTO.setCboAcc(CommonUtil.convertObjToStr(getCbmAcc().getKeyForSelected()));
        objLeaveManagementTO.setCboParForLeave(CommonUtil.convertObjToStr(getCbmParForLeave().getKeyForSelected()));
        objLeaveManagementTO.setCboParFixed(CommonUtil.convertObjToStr(getCbmParFixed().getKeyForSelected()));
        objLeaveManagementTO.setTxtFixedPar(getTxtFixedPar());
        objLeaveManagementTO.setTxtFixedPar1(getTxtFixedPar1());
        objLeaveManagementTO.setTxtPro1(getTxtPro1());
        objLeaveManagementTO.setTxtPro2(getTxtPro2());
        objLeaveManagementTO.setTxtMaxEncashment(getTxtMaxEncashment());
        objLeaveManagementTO.setTxtMaternityCountLimit(getTxtMaternityCountLimit());
        objLeaveManagementTO.setCommand(command);
        objLeaveManagementTO.setCarryOver(getCarryOver());
        objLeaveManagementTO.setStatusBy(TrueTransactMain.USER_ID);
        objLeaveManagementTO.setStatusDt(curDate);
        objLeaveManagementTO.setBranch(TrueTransactMain.BRANCH_ID);
        objLeaveManagementTO.setTxtMaxLeaves(getTxtMaxLeaves());
        if(command.equalsIgnoreCase("INSERT")){
           objLeaveManagementTO.setCreatedBy(TrueTransactMain.USER_ID);
           objLeaveManagementTO.setCreatedDt(curDate);
        }
        if(command.equalsIgnoreCase("UPDATE")||command.equalsIgnoreCase("DELETE")){
            objLeaveManagementTO.setLeaveID(getLeaveID());
        }
        if (isRdoFull() == true) {
            objLeaveManagementTO.setPaymentType("FULL_PAY");
        }
        else{
            objLeaveManagementTO.setPaymentType("HALF_PAY");
        }
        return objLeaveManagementTO;
        
    }
    
    /** Sets all the LeaveManagement values to the OB varibles  there by populatin the UI fields */
    private void setLeaveManagementTO(LeaveManagementTO objLeaveManagementTO){
        setTypeOfLeave(objLeaveManagementTO.getTypeOfLeave());
        setDesc(objLeaveManagementTO.getDesc());
        if (objLeaveManagementTO.getAcc() != null) {
            if (objLeaveManagementTO.getAcc().equals(YES)) {
                setRdoAcc_Yes(true);
                 setRdoAcc_No(false);
            }
            else setRdoAcc_No(true);
        }
        if (objLeaveManagementTO.getIntroReq() != null) {
            if (objLeaveManagementTO.getIntroReq().equals(YES)) {
                setRdoIntroReq_Yes1(true);
                setRdoIntroReq_No1(false);
            }
            else setRdoIntroReq_No1(true);
        }
        if (objLeaveManagementTO.getEncash() != null) {
            if (objLeaveManagementTO.getEncash().equals(YES)) {
                setRdoEncash_Yes1(true);
                setRdoEncash_No1(false);
            }
            else  setRdoEncash_No1(true);
        }
        if (objLeaveManagementTO.getLeaveEncashmentType() != null) {
            if (objLeaveManagementTO.getLeaveEncashmentType().equals("Y")) setChkLtc(true);
            else setChkLtc(false);
        }
        
        if (objLeaveManagementTO.getChkMaternityLeave() != null) {
            if (objLeaveManagementTO.getChkMaternityLeave().equals("Y")) setChkMaternityLeave(true);
            else setChkMaternityLeave(false);
        }
        setCarryOver(objLeaveManagementTO.getCarryOver());
        setTxtAcc(objLeaveManagementTO.getTxtAcc());
        getCbmToBeCredited().setKeyForSelected(CommonUtil.convertObjToStr(objLeaveManagementTO.getCboToBeCredited())); 
        getCbmAcc().setKeyForSelected(CommonUtil.convertObjToStr(objLeaveManagementTO.getCboAcc())); 
        getCbmParFixed().setKeyForSelected(CommonUtil.convertObjToStr(objLeaveManagementTO.getCboParFixed())); 
        getCbmParForLeave().setKeyForSelected(CommonUtil.convertObjToStr(objLeaveManagementTO.getCboParForLeave())); 
        setTxtFixedPar(objLeaveManagementTO.getTxtFixedPar());
        setTxtFixedPar1(objLeaveManagementTO.getTxtFixedPar1());
        setTxtPro1(objLeaveManagementTO.getTxtPro1());
        setTxtPro2(objLeaveManagementTO.getTxtPro2());
        setTxtMaxEncashment(objLeaveManagementTO.getTxtMaxEncashment());
        setTxtMaternityCountLimit(objLeaveManagementTO.getTxtMaternityCountLimit());
        setLeaveID(objLeaveManagementTO.getLeaveID());
        setTxtMaxLeaves(objLeaveManagementTO.getTxtMaxLeaves());
        setStatusBy(objLeaveManagementTO.getStatusBy());
        if (objLeaveManagementTO.getPaymentType() != null) {
            if (objLeaveManagementTO.getPaymentType().equalsIgnoreCase("HALF_PAY")) {
                setRdoHalf(true);
                setRdoFull(false);
            }
            else  {
                setRdoFull(true);
            }
        }
        notifyObservers();
    }
    
    
    /* Executes Query using the TO object */
    public void execute(String command) {
        try {
            HashMap term = new HashMap();
            term.put(CommonConstants.BRANCH_ID, TrueTransactMain.BRANCH_ID);
            term.put("LeaveManagementTO", getLeaveManagementTO(command));
            term.put(CommonConstants.MODULE, getModule());
            term.put(CommonConstants.SCREEN, getScreen());
            term.put(CommonConstants.USER_ID, TrueTransactMain.USER_ID);
            HashMap proxyResultMap=null;
           proxyResultMap = proxy.execute(term, map);
           System.out.println("proxyResultMap"+proxyResultMap);
           setProxyReturnMap(proxyResultMap);
        if (proxyResultMap != null  && command=="INSERT"){
                ClientUtil.showMessageWindow("Transaction No. : " + CommonUtil.convertObjToStr(proxyResultMap.get("LEAVE_ID")));
            }
            setResult(getActionType());
        } catch (Exception e) {
            setResult(ClientConstants.ACTIONTYPE_FAILED);
            parseException.logException(e,true);
        }
    }
     
    
    /** Resetting all tbe Fields of UI */
    public  void resetForm(){
        setTypeOfLeave("");
        setDesc("");
        rdoIntroReq_No1 = false;
        rdoIntroReq_Yes1 = true;
        rdoAcc_No = true;
        rdoAcc_Yes = false;
        rdoEncash_No1 = true;
        rdoEncash_Yes1 = false;
        rdoHalf = false;
        rdoFull = true;
        setCarryOver("");
        setTxtAcc("");
        cboAcc ="";
        cboParFixed="";
        cboParForLeave="";
        cboToBeCredited="";
        setTxtFixedPar("");
        setTxtFixedPar1("");
        setTxtPro1("");
        setTxtPro2("");
        setTxtMaxLeaves("");
        setTxtMaxEncashment("");
        setTxtMaternityCountLimit("");
        setLeaveID("");
        setChanged();
        notifyObservers();
    }
    

 
    
    /* Populates the TO object by executing a Query */
    public void populateData(HashMap whereMap) {
        HashMap mapData=null;
        try {
            mapData = proxy.executeQuery(whereMap, map);
            LeaveManagementTO objLeaveManagementTO =
            (LeaveManagementTO) ((List) mapData.get("LeaveManagementTO")).get(0);
            setLeaveManagementTO(objLeaveManagementTO);
        } catch( Exception e ) {
            setResult(ClientConstants.ACTIONTYPE_FAILED);
            e.printStackTrace();
            parseException.logException(e,true);
            
        }
    }
    
    
    /** This is used to remove the rows in the tblLoanTyep **/
    public void resetTblLoanType(){
        for(int i = tbmLoanType.getRowCount(); i > 0; i--){
            tbmLoanType.removeRow(0);
        }
    }
    
    
    /**
     * Getter for property typeOfLeave.
     * @return Value of property typeOfLeave.
     */
    public java.lang.String getTypeOfLeave() {
        return typeOfLeave;
    }    
    
    /**
     * Setter for property typeOfLeave.
     * @param typeOfLeave New value of property typeOfLeave.
     */
    public void setTypeOfLeave(java.lang.String typeOfLeave) {
        this.typeOfLeave = typeOfLeave;
    }
    
    /**
     * Getter for property desc.
     * @return Value of property desc.
     */
    public java.lang.String getDesc() {
        return desc;
    }
    
    /**
     * Setter for property desc.
     * @param desc New value of property desc.
     */
    public void setDesc(java.lang.String desc) {
        this.desc = desc;
    }
    
    /**
     * Getter for property cboToBeCredited.
     * @return Value of property cboToBeCredited.
     */
    public java.lang.String getCboToBeCredited() {
        return cboToBeCredited;
    }
    
    /**
     * Setter for property cboToBeCredited.
     * @param cboToBeCredited New value of property cboToBeCredited.
     */
    public void setCboToBeCredited(java.lang.String cboToBeCredited) {
        this.cboToBeCredited = cboToBeCredited;
    }
    
    /**
     * Getter for property cbmToBeCredited.
     * @return Value of property cbmToBeCredited.
     */
    public com.see.truetransact.clientutil.ComboBoxModel getCbmToBeCredited() {
        return cbmToBeCredited;
    }
    
    /**
     * Setter for property cbmToBeCredited.
     * @param cbmToBeCredited New value of property cbmToBeCredited.
     */
    public void setCbmToBeCredited(com.see.truetransact.clientutil.ComboBoxModel cbmToBeCredited) {
        this.cbmToBeCredited = cbmToBeCredited;
    }
    
    /**
     * Getter for property carryOver.
     * @return Value of property carryOver.
     */
    public java.lang.String getCarryOver() {
        return carryOver;
    }
    
    /**
     * Setter for property carryOver.
     * @param carryOver New value of property carryOver.
     */
    public void setCarryOver(java.lang.String carryOver) {
        this.carryOver = carryOver;
    }
    
    /**
     * Getter for property txtAcc.
     * @return Value of property txtAcc.
     */
    public java.lang.String getTxtAcc() {
        return txtAcc;
    }
    
    /**
     * Setter for property txtAcc.
     * @param txtAcc New value of property txtAcc.
     */
    public void setTxtAcc(java.lang.String txtAcc) {
        this.txtAcc = txtAcc;
    }
    
    /**
     * Getter for property cboAcc.
     * @return Value of property cboAcc.
     */
    public java.lang.String getCboAcc() {
        return cboAcc;
    }
    
    /**
     * Setter for property cboAcc.
     * @param cboAcc New value of property cboAcc.
     */
    public void setCboAcc(java.lang.String cboAcc) {
        this.cboAcc = cboAcc;
    }
    
    /**
     * Getter for property cbmAcc.
     * @return Value of property cbmAcc.
     */
    public com.see.truetransact.clientutil.ComboBoxModel getCbmAcc() {
        return cbmAcc;
    }
    
    /**
     * Setter for property cbmAcc.
     * @param cbmAcc New value of property cbmAcc.
     */
    public void setCbmAcc(com.see.truetransact.clientutil.ComboBoxModel cbmAcc) {
        this.cbmAcc = cbmAcc;
    }
    
    /**
     * Getter for property cboParForLeave.
     * @return Value of property cboParForLeave.
     */
    public java.lang.String getCboParForLeave() {
        return cboParForLeave;
    }
    
    /**
     * Setter for property cboParForLeave.
     * @param cboParForLeave New value of property cboParForLeave.
     */
    public void setCboParForLeave(java.lang.String cboParForLeave) {
        this.cboParForLeave = cboParForLeave;
    }
    
    /**
     * Getter for property cbmParForLeave.
     * @return Value of property cbmParForLeave.
     */
    public com.see.truetransact.clientutil.ComboBoxModel getCbmParForLeave() {
        return cbmParForLeave;
    }
    
    /**
     * Setter for property cbmParForLeave.
     * @param cbmParForLeave New value of property cbmParForLeave.
     */
    public void setCbmParForLeave(com.see.truetransact.clientutil.ComboBoxModel cbmParForLeave) {
        this.cbmParForLeave = cbmParForLeave;
    }
    
    /**
     * Getter for property txtFixedPar1.
     * @return Value of property txtFixedPar1.
     */
    public java.lang.String getTxtFixedPar1() {
        return txtFixedPar1;
    }
    
    /**
     * Setter for property txtFixedPar1.
     * @param txtFixedPar1 New value of property txtFixedPar1.
     */
    public void setTxtFixedPar1(java.lang.String txtFixedPar1) {
        this.txtFixedPar1 = txtFixedPar1;
    }
    
    /**
     * Getter for property txtFixedPar.
     * @return Value of property txtFixedPar.
     */
    public java.lang.String getTxtFixedPar() {
        return txtFixedPar;
    }
    
    /**
     * Setter for property txtFixedPar.
     * @param txtFixedPar New value of property txtFixedPar.
     */
    public void setTxtFixedPar(java.lang.String txtFixedPar) {
        this.txtFixedPar = txtFixedPar;
    }
    
    /**
     * Getter for property txtPro1.
     * @return Value of property txtPro1.
     */
    public java.lang.String getTxtPro1() {
        return txtPro1;
    }
    
    /**
     * Setter for property txtPro1.
     * @param txtPro1 New value of property txtPro1.
     */
    public void setTxtPro1(java.lang.String txtPro1) {
        this.txtPro1 = txtPro1;
    }
    
    /**
     * Getter for property txtPro2.
     * @return Value of property txtPro2.
     */
    public java.lang.String getTxtPro2() {
        return txtPro2;
    }
    
    /**
     * Setter for property txtPro2.
     * @param txtPro2 New value of property txtPro2.
     */
    public void setTxtPro2(java.lang.String txtPro2) {
        this.txtPro2 = txtPro2;
    }
    
    /**
     * Getter for property cboParFixed.
     * @return Value of property cboParFixed.
     */
    public java.lang.String getCboParFixed() {
        return cboParFixed;
    }
    
    /**
     * Setter for property cboParFixed.
     * @param cboParFixed New value of property cboParFixed.
     */
    public void setCboParFixed(java.lang.String cboParFixed) {
        this.cboParFixed = cboParFixed;
    }
    
    /**
     * Getter for property cbmParFixed.
     * @return Value of property cbmParFixed.
     */
    public com.see.truetransact.clientutil.ComboBoxModel getCbmParFixed() {
        return cbmParFixed;
    }
    
    /**
     * Setter for property cbmParFixed.
     * @param cbmParFixed New value of property cbmParFixed.
     */
    public void setCbmParFixed(com.see.truetransact.clientutil.ComboBoxModel cbmParFixed) {
        this.cbmParFixed = cbmParFixed;
    }
    
    /**
     * Getter for property txtMaxLeaves.
     * @return Value of property txtMaxLeaves.
     */
    public java.lang.String getTxtMaxLeaves() {
        return txtMaxLeaves;
    }
    
    /**
     * Setter for property txtMaxLeaves.
     * @param txtMaxLeaves New value of property txtMaxLeaves.
     */
    public void setTxtMaxLeaves(java.lang.String txtMaxLeaves) {
        this.txtMaxLeaves = txtMaxLeaves;
    }
    
    /**
     * Getter for property txtMaxEncashment.
     * @return Value of property txtMaxEncashment.
     */
    public java.lang.String getTxtMaxEncashment() {
        return txtMaxEncashment;
    }
    
    /**
     * Setter for property txtMaxEncashment.
     * @param txtMaxEncashment New value of property txtMaxEncashment.
     */
    public void setTxtMaxEncashment(java.lang.String txtMaxEncashment) {
        this.txtMaxEncashment = txtMaxEncashment;
    }
    
    /**
     * Getter for property rdoIntroReq_Yes1.
     * @return Value of property rdoIntroReq_Yes1.
     */
    public boolean isRdoIntroReq_Yes1() {
        return rdoIntroReq_Yes1;
    }
    
    /**
     * Setter for property rdoIntroReq_Yes1.
     * @param rdoIntroReq_Yes1 New value of property rdoIntroReq_Yes1.
     */
    public void setRdoIntroReq_Yes1(boolean rdoIntroReq_Yes1) {
        this.rdoIntroReq_Yes1 = rdoIntroReq_Yes1;
    }
    
    /**
     * Getter for property rdoIntroReq_No1.
     * @return Value of property rdoIntroReq_No1.
     */
    public boolean isRdoIntroReq_No1() {
        return rdoIntroReq_No1;
    }
    
    /**
     * Setter for property rdoIntroReq_No1.
     * @param rdoIntroReq_No1 New value of property rdoIntroReq_No1.
     */
    public void setRdoIntroReq_No1(boolean rdoIntroReq_No1) {
        this.rdoIntroReq_No1 = rdoIntroReq_No1;
    }
    
    /**
     * Getter for property rdoAcc_Yes.
     * @return Value of property rdoAcc_Yes.
     */
    public boolean isRdoAcc_Yes() {
        return rdoAcc_Yes;
    }
    
    /**
     * Setter for property rdoAcc_Yes.
     * @param rdoAcc_Yes New value of property rdoAcc_Yes.
     */
    public void setRdoAcc_Yes(boolean rdoAcc_Yes) {
        this.rdoAcc_Yes = rdoAcc_Yes;
    }
    
    /**
     * Getter for property rdoAcc_No.
     * @return Value of property rdoAcc_No.
     */
    public boolean isRdoAcc_No() {
        return rdoAcc_No;
    }
    
    /**
     * Setter for property rdoAcc_No.
     * @param rdoAcc_No New value of property rdoAcc_No.
     */
    public void setRdoAcc_No(boolean rdoAcc_No) {
        this.rdoAcc_No = rdoAcc_No;
    }
    
    /**
     * Getter for property rdoEncash_Yes1.
     * @return Value of property rdoEncash_Yes1.
     */
    public boolean isRdoEncash_Yes1() {
        return rdoEncash_Yes1;
    }
    
    /**
     * Setter for property rdoEncash_Yes1.
     * @param rdoEncash_Yes1 New value of property rdoEncash_Yes1.
     */
    public void setRdoEncash_Yes1(boolean rdoEncash_Yes1) {
        this.rdoEncash_Yes1 = rdoEncash_Yes1;
    }
    
    /**
     * Getter for property rdoEncash_No1.
     * @return Value of property rdoEncash_No1.
     */
    public boolean isRdoEncash_No1() {
        return rdoEncash_No1;
    }
    
    /**
     * Setter for property rdoEncash_No1.
     * @param rdoEncash_No1 New value of property rdoEncash_No1.
     */
    public void setRdoEncash_No1(boolean rdoEncash_No1) {
        this.rdoEncash_No1 = rdoEncash_No1;
    }
    
    /**
     * Getter for property rdoLtc.
     * @return Value of property rdoLtc.
     */
    public boolean isRdoLtc() {
        return rdoLtc;
    }
    
    /**
     * Setter for property rdoLtc.
     * @param rdoLtc New value of property rdoLtc.
     */
    public void setRdoLtc(boolean rdoLtc) {
        this.rdoLtc = rdoLtc;
    }
    
    /**
     * Getter for property rdoLfc.
     * @return Value of property rdoLfc.
     */
    public boolean isRdoLfc() {
        return rdoLfc;
    }
    
    /**
     * Setter for property rdoLfc.
     * @param rdoLfc New value of property rdoLfc.
     */
    public void setRdoLfc(boolean rdoLfc) {
        this.rdoLfc = rdoLfc;
    }
    
    /**
     * Getter for property leaveID.
     * @return Value of property leaveID.
     */
    public java.lang.String getLeaveID() {
        return leaveID;
    }
    
    /**
     * Setter for property leaveID.
     * @param leaveID New value of property leaveID.
     */
    public void setLeaveID(java.lang.String leaveID) {
        this.leaveID = leaveID;
    }
    
    /**
     * Getter for property chkLtc.
     * @return Value of property chkLtc.
     */
    public boolean isChkLtc() {
        return chkLtc;
    }
    
    /**
     * Setter for property chkLtc.
     * @param chkLtc New value of property chkLtc.
     */
    public void setChkLtc(boolean chkLtc) {
        this.chkLtc = chkLtc;
    }
    
    /**
     * Getter for property rdoFull.
     * @return Value of property rdoFull.
     */
    public boolean isRdoFull() {
        return rdoFull;
    }
    
    /**
     * Setter for property rdoFull.
     * @param rdoFull New value of property rdoFull.
     */
    public void setRdoFull(boolean rdoFull) {
        this.rdoFull = rdoFull;
    }
    
    /**
     * Getter for property rdoHalf.
     * @return Value of property rdoHalf.
     */
    public boolean isRdoHalf() {
        return rdoHalf;
    }
    
    /**
     * Setter for property rdoHalf.
     * @param rdoHalf New value of property rdoHalf.
     */
    public void setRdoHalf(boolean rdoHalf) {
        this.rdoHalf = rdoHalf;
    }
    
    /**
     * Getter for property chkMaternityLeave.
     * @return Value of property chkMaternityLeave.
     */
    public boolean isChkMaternityLeave() {
        return chkMaternityLeave;
    }
    
    /**
     * Setter for property chkMaternityLeave.
     * @param chkMaternityLeave New value of property chkMaternityLeave.
     */
    public void setChkMaternityLeave(boolean chkMaternityLeave) {
        this.chkMaternityLeave = chkMaternityLeave;
    }
    
    /**
     * Getter for property txtMaternityCountLimit.
     * @return Value of property txtMaternityCountLimit.
     */
    public java.lang.String getTxtMaternityCountLimit() {
        return txtMaternityCountLimit;
    }
    
    /**
     * Setter for property txtMaternityCountLimit.
     * @param txtMaternityCountLimit New value of property txtMaternityCountLimit.
     */
    public void setTxtMaternityCountLimit(java.lang.String txtMaternityCountLimit) {
        this.txtMaternityCountLimit = txtMaternityCountLimit;
    }
    
}