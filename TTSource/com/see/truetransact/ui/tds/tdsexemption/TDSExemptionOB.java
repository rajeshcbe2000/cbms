/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition.
 * 
 *
 * TDSExemptionOB.java
 *
 * Created on Tue Feb 01 17:34:53 IST 2005
 */

package com.see.truetransact.ui.tds.tdsexemption;

import java.util.Observable;
import org.apache.log4j.Logger;
import java.util.HashMap;
import java.util.List;
import java.util.ArrayList;
import java.util.Date;

import com.see.truetransact.clientproxy.ProxyFactory;
import com.see.truetransact.clientutil.ClientUtil;
import com.see.truetransact.clientutil.ClientConstants;
import com.see.truetransact.clientexception.ClientParseException;
import com.see.truetransact.commonutil.CommonConstants;
import com.see.truetransact.commonutil.CommonUtil;
import com.see.truetransact.commonutil.DateUtil;
import com.see.truetransact.clientproxy.ProxyParameters;
import com.see.truetransact.uicomponent.CObservable;
import com.see.truetransact.transferobject.tds.tdsexemption.TDSExemptionTO;
import com.see.truetransact.ui.TrueTransactMain;
import com.see.truetransact.uicomponent.CObservable;



/**
 *
 * @author  Ashok Vijayakumar
 */

public class TDSExemptionOB extends CObservable{
    
    Date curDate = null;
    private String txtCustomerId = "";
    private String txtRefNo = "";
    private String tdtStartDate = "";
    private String tdtEndDate = "";
    private String tdtSubmitDate = "";
    private String txtExemptId = "";
    private String txtPanNo = "";
    private String txtRemarks = "";
    private ProxyFactory proxy;
    private HashMap map;//HashMap for setting key,values for Jndi,home,remote
    private HashMap lookUpHash;//HashMap used for puting a mapname with a key which is to be used for filling ComboBoxModel
    private static TDSExemptionOB objTDSExemptionOB;//Singleton Object Reference
    private final static Logger log = Logger.getLogger(TDSExemptionOB.class);//Creating Instace of Log
    private final static ClientParseException parseException = ClientParseException.getInstance();
    private int _actionType;//This vairable is used to type of action 1-New 2-Edit 3-Delete
    private int _result;
    private String lblStatus = ClientConstants.ACTION_STATUS[ClientConstants.ACTIONTYPE_CANCEL];
    
    /** Consturctor Declaration  for  TDSExemptionOB */
    private TDSExemptionOB() {
        try {
            curDate = ClientUtil.getCurrentDate();
            proxy = ProxyFactory.createProxy();
            setOperationMap();
        }catch(Exception e){
            parseException.logException(e,true);
        }
    }
    
    static {
        try {
            log.info("Creating ParameterOB...");
            objTDSExemptionOB = new TDSExemptionOB();
        } catch(Exception e) {
            parseException.logException(e,true);
        }
    }
    
    // Sets the HashMap required to set JNDI,Home and Remote
    private void setOperationMap() throws Exception{
        map = new HashMap();
        map.put(CommonConstants.JNDI, "TDSExemptionJNDI");
        map.put(CommonConstants.HOME, "tds.tdsexemption.TDSExemptionHome");
        map.put(CommonConstants.REMOTE, "tds.tdsexemption.TDSExemption");
    }
    
    /**
     * Returns an instance of TDSExemptionOB.
     * @return  TDSExemptionOB
     */
    
    public static TDSExemptionOB getInstance()throws Exception{
        return objTDSExemptionOB;
    }
    
    // Setter method for txtCustomerId
    void setTxtCustomerId(String txtCustomerId){
        this.txtCustomerId = txtCustomerId;
        setChanged();
    }
    // Getter method for txtCustomerId
    String getTxtCustomerId(){
        return this.txtCustomerId;
    }
    
    // Setter method for txtRefNo
    void setTxtRefNo(String txtRefNo){
        this.txtRefNo = txtRefNo;
        setChanged();
    }
    // Getter method for txtRefNo
    String getTxtRefNo(){
        return this.txtRefNo;
    }
    
    // Setter method for tdtStartDate
    void setTdtStartDate(String tdtStartDate){
        this.tdtStartDate = tdtStartDate;
        setChanged();
    }
    // Getter method for tdtStartDate
    String getTdtStartDate(){
        return this.tdtStartDate;
    }
    
    // Setter method for tdtEndDate
    void setTdtEndDate(String tdtEndDate){
        this.tdtEndDate = tdtEndDate;
        setChanged();
    }
    // Getter method for tdtEndDate
    String getTdtEndDate(){
        return this.tdtEndDate;
    }
    
    // Setter method for tdtSubmitDate
    void setTdtSubmitDate(String tdtSubmitDate){
        this.tdtSubmitDate = tdtSubmitDate;
        setChanged();
    }
    // Getter method for tdtSubmitDate
    String getTdtSubmitDate(){
        return this.tdtSubmitDate;
    }
    
    // Setter method for txtExemptId
    void setTxtExemptId(String txtExemptId){
        this.txtExemptId = txtExemptId;
        setChanged();
    }
    // Getter method for txtExemptId
    String getTxtExemptId(){
        return this.txtExemptId;
    }
    
    /**
     * Getter for property panNo.
     * @return Value of property panNo.
     */
    public java.lang.String getTxtPanNo() {
        return txtPanNo;
    }
    
    /**
     * Setter for property panNo.
     * @param panNo New value of property panNo.
     */
    public void setTxtPanNo(java.lang.String txtPanNo) {
        this.txtPanNo = txtPanNo;
        setChanged();
    }
    
    /**
     * Getter for property remarks.
     * @return Value of property remarks.
     */
    public java.lang.String getTxtRemarks() {
        return txtRemarks;
    }
    
    /**
     * Setter for property remarks.
     * @param remarks New value of property remarks.
     */
    public void setTxtRemarks(java.lang.String txtRemarks) {
        this.txtRemarks = txtRemarks;
        setChanged();
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
    }
    
    public void resetStatus(){
        this.setLblStatus(ClientConstants.ACTION_STATUS[0]);
    }
    
    public int getResult(){
        return _result;
    }
    
    /** Returns an Instance of TDSExemptionTO **/
    private TDSExemptionTO getTDSExemptionTO(String command){
        TDSExemptionTO objTDSExemptionTO = new TDSExemptionTO();
        objTDSExemptionTO.setCommand(command);
        objTDSExemptionTO.setCustId(getTxtCustomerId());
        
        Date SubDt = DateUtil.getDateMMDDYYYY(getTdtSubmitDate());
        if(SubDt != null){
        Date subDate = (Date)curDate.clone();
        subDate.setDate(SubDt.getDate());
        subDate.setMonth(SubDt.getMonth());
        subDate.setYear(SubDt.getYear());
//        objTDSExemptionTO.setExemRecDt(DateUtil.getDateMMDDYYYY(getTdtSubmitDate()));
        objTDSExemptionTO.setExemRecDt(subDate);
        }else{
            objTDSExemptionTO.setExemRecDt(DateUtil.getDateMMDDYYYY(getTdtSubmitDate()));
        }
        
        objTDSExemptionTO.setCertRefNo(getTxtRefNo());
        
//        objTDSExemptionTO.setExemStartDt(DateUtil.getDateMMDDYYYY(getTdtStartDate()));
//        objTDSExemptionTO.setExemEndDt(DateUtil.getDateMMDDYYYY(getTdtEndDate()));
        Date StDt = DateUtil.getDateMMDDYYYY(getTdtStartDate());
        if(StDt != null){
        Date stDate = (Date)curDate.clone();
        stDate.setDate(StDt.getDate());
        stDate.setMonth(StDt.getMonth());
        stDate.setYear(StDt.getYear());
        objTDSExemptionTO.setExemStartDt(stDate);
        }else{
            objTDSExemptionTO.setExemStartDt(DateUtil.getDateMMDDYYYY(getTdtStartDate()));
        }
        
        Date EndDt = DateUtil.getDateMMDDYYYY(getTdtEndDate());
        if(EndDt != null){
        Date endDate = (Date)curDate.clone();
        endDate.setDate(EndDt.getDate());
        endDate.setMonth(EndDt.getMonth());
        endDate.setYear(EndDt.getYear());
        objTDSExemptionTO.setExemEndDt(endDate);
        }else{
           objTDSExemptionTO.setExemEndDt(DateUtil.getDateMMDDYYYY(getTdtEndDate())); 
        }
        
        objTDSExemptionTO.setExemId(getTxtExemptId());
        objTDSExemptionTO.setBranchId(getSelectedBranchID());
        objTDSExemptionTO.setPanNo(getTxtPanNo());
        objTDSExemptionTO.setRemarks(getTxtRemarks());
        objTDSExemptionTO.setStatusBy(TrueTransactMain.USER_ID);
        objTDSExemptionTO.setStatusDt(curDate);
        objTDSExemptionTO.setInitiatedBranch(ProxyParameters.BRANCH_ID);
        return objTDSExemptionTO;
    }
    
    /** Ses all the TO values OB Varibales thereby setting it to the ui **/
    private void setTDSExemptionTO(TDSExemptionTO objTDSExemptionTO) {
        setTxtCustomerId(objTDSExemptionTO.getCustId());
        setTdtSubmitDate(DateUtil.getStringDate(objTDSExemptionTO.getExemRecDt()));
        setTxtRefNo(objTDSExemptionTO.getCertRefNo());
        setTdtStartDate(DateUtil.getStringDate(objTDSExemptionTO.getExemStartDt()));
        setTdtEndDate(DateUtil.getStringDate(objTDSExemptionTO.getExemEndDt()));
        setTxtExemptId(objTDSExemptionTO.getExemId());
        setTxtPanNo(objTDSExemptionTO.getPanNo());
        setTxtRemarks(objTDSExemptionTO.getRemarks());
        setStatusBy(objTDSExemptionTO.getStatusBy());
        setAuthorizeStatus(objTDSExemptionTO.getAuthorizeStatus());
        notifyObservers();
    }
    
    /** Resets all the OB variable to empty **/
    public void resetForm(){
        setTxtCustomerId("");
        setTdtSubmitDate("");
        setTxtRefNo("");
        setTdtStartDate("");
        setTdtEndDate("");
        setTxtExemptId("");
        setTxtPanNo("");
        setTxtRemarks("");
        notifyObservers();
    }
    
    /* Populates the TO object by executing a Query */
    public void populateData(HashMap whereMap) {
        HashMap mapData=null;
        try {
            mapData = proxy.executeQuery(whereMap, map);
            TDSExemptionTO objTDSExemptionTO =
            (TDSExemptionTO) ((List) mapData.get("TDSExemptionTO")).get(0);
            setTDSExemptionTO(objTDSExemptionTO);
        } catch( Exception e ) {
            e.printStackTrace();
            setResult(ClientConstants.ACTIONTYPE_FAILED);
            parseException.logException(e,true);
            
        }
    }
    
    /* Executes Query using the TO object */
    public void execute(String command) {
        try {
            HashMap term = new HashMap();
            term.put(CommonConstants.MODULE, getModule());
            term.put(CommonConstants.SCREEN, getScreen());
            term.put(CommonConstants.USER_ID, TrueTransactMain.USER_ID);
            term.put(CommonConstants.BRANCH_ID, ProxyParameters.BRANCH_ID);
            term.put(CommonConstants.SELECTED_BRANCH_ID, getSelectedBranchID());
            term.put("TDSExemptionTO", getTDSExemptionTO(command));
            HashMap proxyResultMap = proxy.execute(term, map);
            if(proxyResultMap !=null && proxyResultMap.containsKey(CommonConstants.TRANS_ID)) {
                ClientUtil.showMessageWindow("TDSExemption ID No."+CommonUtil.convertObjToStr(proxyResultMap.get(CommonConstants.TRANS_ID)));
            }
             setProxyReturnMap(proxyResultMap);
             System.out.println("prkk"+proxyResultMap);
            setResult(getActionType());
        } catch (Exception e) {
            setResult(ClientConstants.ACTIONTYPE_FAILED);
            parseException.logException(e,true);
        }
    }
    
    /** Returns the String CustomerName, by executing a Qurery **/
    public String getCustomerName(String custId){
        String customerName = "";
        HashMap where = new HashMap();
        where.put("CUST_ID", custId);
        ArrayList list = (ArrayList) ClientUtil.executeQuery("getCustomerDetail",where);
        where = null;
        if(list.size() >0){
            HashMap map = (HashMap) list.get(0);
            customerName = CommonUtil.convertObjToStr(map.get("CUSTOMERNAME"));
        }
        return customerName;
        
    }
    
}