/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition. 
 * 
 * RecoveryParametersOB.java
 *
 * Created on February 25, 2004, 2:48 PM
 */

package com.see.truetransact.ui.salaryrecovery;

import com.see.truetransact.clientutil.ComboBoxModel;
import com.see.truetransact.clientutil.ClientUtil;
import com.see.truetransact.clientutil.ClientConstants;
import com.see.truetransact.commonutil.CommonConstants;
import com.see.truetransact.commonutil.DateUtil;
import com.see.truetransact.commonutil.CommonUtil;
import com.see.truetransact.commonutil.TTException;
import com.see.truetransact.clientexception.ClientParseException;
import com.see.truetransact.clientproxy.ProxyFactory;
import com.see.truetransact.clientproxy.ProxyParameters;
import com.see.truetransact.uicomponent.CInternalFrame;
import com.see.truetransact.uicomponent.CObservable;
import com.see.truetransact.clientutil.EnhancedTableModel;
import com.see.truetransact.ui.TrueTransactMain;
//import com.see.truetransact.ui.common.viewall.TableDialogUI;
import com.see.truetransact.transferobject.salaryrecovery.RecoveryParametersTO;
import com.see.truetransact.transferobject.transaction.reconciliation.ReconciliationTO;
//import java.math.BigDecimal;
//import java.util.Observable;
import java.util.HashMap;
import java.util.ArrayList;
import java.util.List;
import java.util.Date;
import java.util.Map;

import org.apache.log4j.Logger;

//import com.see.truetransact.serverside.tds.tdscalc;
/**
 *
 * @author  rahul
 */
public class RecoveryParametersOB extends CObservable{
    
    private HashMap lookUpHash;
    private HashMap keyValue;
    private HashMap operationMap;
    private ProxyFactory proxy;
    HashMap map =null;
    private int actionType;
    private int result;
    private HashMap _authorizeMap;
    private Date curDate = null;
    private ArrayList key;
    private ArrayList value;
    private static CInternalFrame frame;
    private static RecoveryParametersOB recoveryParametersOB;
    private HashMap procChargeHash = new HashMap();
    private ComboBoxModel cbmProdType;
    private ComboBoxModel cbmProdId;
    private ComboBoxModel cbmInputCurrency;
    private ComboBoxModel cbmInstrumentType;
    private ArrayList lockTableTitle;
    private boolean recovery=false;
    private HashMap tdsCalc =new HashMap();
    private String txtFirstDay="";
    private String txtLastDay="";
    private String txtSalarySuspenseProdId="";
    private String txtMDSSuspenseProdId="";
    private String txtRDSuspenseProdId="";
    private String lblStatus = ClientConstants.ACTION_STATUS[ClientConstants.ACTIONTYPE_CANCEL];
    private final static ClientParseException parseException = ClientParseException.getInstance();
    
    private final static Logger log = Logger.getLogger(SalaryDeductionMappingUI.class);
    
    
    static {
        try {
            
            recoveryParametersOB = new RecoveryParametersOB();
        } catch(Exception e) {
            log.info("Error in SalaryDeductionMappingOB Declaration");
        }
    }
    
    public static RecoveryParametersOB getInstance(CInternalFrame frm) {
        frame = frm;
        return recoveryParametersOB;
    }
    
    /** Creates a new instance of CashTransactionOB */
    public RecoveryParametersOB() throws Exception {
        curDate = ClientUtil.getCurrentDate();
        initianSetup();
        setOperationMap();
        
    }
    
    public Date getCurrentDate() {
        return (Date)curDate.clone();
    }
    private void initianSetup() throws Exception{
        log.info("In initianSetup()");
        
        try {
            proxy = ProxyFactory.createProxy();
        } catch (Exception e) {
            log.info(" Error In initianSetup()");
            e.printStackTrace();
        }
        fillDropdown();// To Fill all the Combo Boxes
    }
    
    // Set the value of JNDI and the Session Bean...
    private void setOperationMap() throws Exception{
        log.info("In setOperationMap()");
        
        operationMap = new HashMap();
        operationMap.put(CommonConstants.JNDI, "RecoveryParametersJNDI");
        operationMap.put(CommonConstants.HOME, "salaryrecovery.RecoveryParametersHome");
        operationMap.put(CommonConstants.REMOTE, "salaryrecovery.RecoveryParameters");
    }
    public void setTableReset(){
        
    }
    
    
    
    // To Fill the Combo boxes in the UI
    public void fillDropdown() throws Exception{
        
        
    }
    
    // Get the value from the Hash Map depending on the Value of Key...
    private void getKeyValue(HashMap keyValue)  throws Exception{
        log.info("In getKeyValue()");
        
        key = (ArrayList)keyValue.get(CommonConstants.KEY);
        value = (ArrayList)keyValue.get(CommonConstants.VALUE);
    }
    
    // To enter the Data into the Database...Called from doActionPerform()...
    public RecoveryParametersTO setRecoveryParametersTO() {
        final RecoveryParametersTO objRecoveryParametersTO = new RecoveryParametersTO();
        try{
            objRecoveryParametersTO.setBranchId(getSelectedBranchID());
            objRecoveryParametersTO.setStatusBy(ProxyParameters.USER_ID);
            objRecoveryParametersTO.setStatus("CREATED");
            objRecoveryParametersTO.setStatusDt(curDate);
            objRecoveryParametersTO.setCreatedBy(ProxyParameters.USER_ID);
            objRecoveryParametersTO.setCreatedDt(curDate);
            objRecoveryParametersTO.setFirstDay(getFirstDay());
            objRecoveryParametersTO.setLastDay(getLastDay());
            objRecoveryParametersTO.setSalarySuspenseProdId(getTxtSalarySuspenseProdId());
            objRecoveryParametersTO.setMDSSuspenseProdId(getTxtMDSSuspenseProdId());
            objRecoveryParametersTO.setRDSuspenseProdId(getTxtRDSuspenseProdId());
            
            //            Double tokenNo = getTokenDouble(getTxtTokenNo());
            
        }catch(Exception e){
            e.printStackTrace();
        }
        return objRecoveryParametersTO;
    }
    
    //Do display the Data from the Database, in UI
    public void populateData(HashMap whereMap) {
        try{
            log.info("In populateData()");
            populateOB(whereMap);
        }catch(Exception e){
            e.printStackTrace();
        }
    }
    
    private void populateOB(HashMap mapData) throws Exception{
        ttNotifyObservers();
    }
    
    // To Enter the values in the UI fields, from the database...
    
    
    // To perform Appropriate operation... Insert, Update, Delete...
    public void doAction() {
        TTException exception = null;
        
        log.info("In doAction()");
        try {
            doActionPerform("");
        } catch (Exception e) {
            log.info("Error In doAction()");
            setResult(ClientConstants.ACTIONTYPE_FAILED);
            e.printStackTrace();
            if(e instanceof TTException) {
                exception = (TTException) e;
            }
        }
        
    }
    
    /** To perform the necessary action */
    private void doActionPerform(String parameter) throws Exception{
        HashMap proxyResultMap = new HashMap();
        final RecoveryParametersTO objRecoveryParametersTO = setRecoveryParametersTO();
        HashMap data=new HashMap();
        if(getRecovery()==true){
            objRecoveryParametersTO.setStatus("UPDATED");
            setRecovery(true);
        }else{
            objRecoveryParametersTO.setStatus("CREATED");
            setRecovery(true);
        }
        data.put("RecoveryParametersTO",objRecoveryParametersTO);
        proxyResultMap = proxy.execute(data, operationMap);
        ClientUtil.showMessageWindow("Process is completed");
    }
    
    
    public void setRecovery(boolean recovery){
        this.recovery=recovery;
    }
    public boolean getRecovery(){
        return recovery;
        
    }
    // To reset all the fields in the UI
    public void resetForm(){
        setFirstDay("");
        setLastDay("");
        setTxtMDSSuspenseProdId("");
        setTxtRDSuspenseProdId("");
        setTxtSalarySuspenseProdId("");
        ttNotifyObservers();
        
    }
    
    //To reset all the Lables in the UI...
    public void resetLable(){
        
        
    }
    
    // To set and change the Status of the lable STATUS
    public void setResult(int result) {
        this.result = result;
        setChanged();
    }
    public int getResult(){
        return this.result;
    }
    
    // To set the Value of the lblStatus...
    public void setLblStatus(String lblStatus) {
        this.lblStatus = lblStatus;
        setChanged();
    }
    public String getLblStatus(){
        return lblStatus;
    }
    
    //To reset the Value of lblStatus after each save action...
    public void resetStatus(){
        this.setLblStatus(ClientConstants.ACTION_STATUS[0]);
    }
    
    /** To set the status based on ActionType, either New, Edit, etc., */
    
    /** To update the Status based on result performed by doAction() method */
    public void setResultStatus(){
        this.setLblStatus(ClientConstants.RESULT_STATUS[this.getResult()]);
        ttNotifyObservers();
    }
    
    public void ttNotifyObservers(){
        notifyObservers();
    }
    public void setFirstDay(String txtFirstDay){
        this.txtFirstDay=txtFirstDay;
    }
    public String getFirstDay(){
        return txtFirstDay;
    }
    
    public void setLastDay(String txtLastDay){
        this.txtLastDay=txtLastDay;
    }
    public String getLastDay(){
        return txtLastDay;
    }
    
    /**
     * Getter for property txtSalarySuspenseProdId.
     * @return Value of property txtSalarySuspenseProdId.
     */
    public String getTxtSalarySuspenseProdId() {
        return txtSalarySuspenseProdId;
    }
    
    /**
     * Setter for property txtSalarySuspenseProdId.
     * @param txtSalarySuspenseProdId New value of property txtSalarySuspenseProdId.
     */
    public void setTxtSalarySuspenseProdId(String txtSalarySuspenseProdId) {
        this.txtSalarySuspenseProdId = txtSalarySuspenseProdId;
    }
    
    /**
     * Getter for property txtMDSSuspenseProdId.
     * @return Value of property txtMDSSuspenseProdId.
     */
    public String getTxtMDSSuspenseProdId() {
        return txtMDSSuspenseProdId;
    }
    
    /**
     * Setter for property txtMDSSuspenseProdId.
     * @param txtMDSSuspenseProdId New value of property txtMDSSuspenseProdId.
     */
    public void setTxtMDSSuspenseProdId(String txtMDSSuspenseProdId) {
        this.txtMDSSuspenseProdId = txtMDSSuspenseProdId;
    }
    
    /**
     * Getter for property txtRDSuspenseProdId.
     * @return Value of property txtRDSuspenseProdId.
     */
    public String getTxtRDSuspenseProdId() {
        return txtRDSuspenseProdId;
    }
    
    /**
     * Setter for property txtRDSuspenseProdId.
     * @param txtRDSuspenseProdId New value of property txtRDSuspenseProdId.
     */
    public void setTxtRDSuspenseProdId(String txtRDSuspenseProdId) {
        this.txtRDSuspenseProdId = txtRDSuspenseProdId;
    }
    
}
