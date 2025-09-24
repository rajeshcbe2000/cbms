/*
 *Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition.
 * 
 *
 * ReturnOfInstrumentsOB.java
 *
 * Created on April 5, 2004, 3:59 PM
 */

package com.see.truetransact.ui.clearing.returns;

/**
 *
 * @author  Ashok
 */

import java.util.HashMap;
import java.util.ArrayList;
import java.util.List;
import java.util.Date;

import com.see.truetransact.commonutil.CommonConstants;
import com.see.truetransact.clientutil.ClientConstants;
import com.see.truetransact.commonutil.CommonUtil;
import com.see.truetransact.clientproxy.ProxyFactory;
import com.see.truetransact.clientproxy.ProxyParameters;
import org.apache.log4j.Logger;
import com.see.truetransact.clientexception.ClientParseException;
import com.see.truetransact.clientutil.ComboBoxModel;
import com.see.truetransact.clientutil.ClientUtil;
import com.see.truetransact.transferobject.clearing.returns.ReturnOfInstrumentsTO;
import com.see.truetransact.commonutil.DateUtil;
import com.see.truetransact.uicomponent.CObservable;
import com.see.truetransact.ui.TrueTransactMain;

public class ReturnOfInstrumentsOB extends CObservable{
    
    private String txtInstrumentNo1 = "";
    private String txtInstrumentNo2 = "";
    private String lblReturnId="";
    private boolean chkYes = false;
    private String cboReturnType = "";
    private String cboClearingType = "";
    private String tdtClearingDate = "";
    private String txtBatchId = "";
    private String lblStatus = ClientConstants.ACTION_STATUS[ClientConstants.ACTIONTYPE_CANCEL];
    private int _result;
    private int _actionType;
    private HashMap map;
    private ProxyFactory proxy;
    private final static Logger _log = Logger.getLogger(ReturnOfInstrumentsOB.class);
    private static ReturnOfInstrumentsOB objReturnOfInstrumentsOB;// singleton object
    private final static ClientParseException parseException = ClientParseException.getInstance();
    private ComboBoxModel returnTypeModel;
    private ComboBoxModel clearingTypeModel;
    private HashMap lookupMap,lookUpHash;
    private HashMap keyValue;
    private ArrayList key;
    private ArrayList value;
    private HashMap lblMap;
    private static Date currDt = null;
    private HashMap authMap;
    
    /** Creates a new instance of ReturnOfInstrumentsOB */
    private ReturnOfInstrumentsOB() {
        map = new HashMap();
        map.put(CommonConstants.JNDI, "ReturnOfInstrumentsJNDI");
        map.put(CommonConstants.HOME, "serverside.clearing.returns.ReturnOfInstrumentsHome");
        map.put(CommonConstants.REMOTE, "serverside.clearing.returns.ReturnOfInstruments");
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
            _log.info("Creating BillsOB...");
            currDt = ClientUtil.getCurrentDate();
            objReturnOfInstrumentsOB= new ReturnOfInstrumentsOB();
        } catch(Exception e) {
            parseException.logException(e,true);
        }
    }
    
    /**
     * Returns an instance of ReturnOfInstrumentsOB.
     * @return  ReturnOfInstrumentsOB
     */
    
    public static ReturnOfInstrumentsOB getInstance()throws Exception{
        return objReturnOfInstrumentsOB;
    }
    
    /* Initialsing ComboBox model */
    private void initUIComboBoxModel(){
        returnTypeModel=new ComboBoxModel();
        clearingTypeModel=new ComboBoxModel();
    }
    
    /* Filling up the ComboBox */
    private void fillDropdown(){
        try{
            final HashMap param = new HashMap();
            final ArrayList lookupKey = new ArrayList();
            lookupKey.add("CLEARING.RETURN_TYPE");
            lookupKey.add("INWARDCLEARING.CLEARINGTYPE");
            param.put(CommonConstants.MAP_NAME, null);
            param.put(CommonConstants.PARAMFORQUERY, lookupKey);
            HashMap lookupValues =ClientUtil.populateLookupData(param);
            getKeyValue((HashMap)lookupValues.get("CLEARING.RETURN_TYPE"));
            returnTypeModel = new ComboBoxModel(key,value);
            lookUpHash = new HashMap();
            lookUpHash.put(CommonConstants.MAP_NAME,"getSelectClearingType");
            lookUpHash.put(CommonConstants.PARAMFORQUERY, TrueTransactMain.BRANCH_ID);
            keyValue = com.see.truetransact.clientutil.ClientUtil.populateLookupData(lookUpHash);
            getKeyValue((HashMap)keyValue.get(CommonConstants.DATA));
            clearingTypeModel = new ComboBoxModel(key,value);
        }catch(NullPointerException e){
            parseException.logException(e,true);
        }catch(Exception e){
            parseException.logException(e,true);
        }
    }
    
    private void getKeyValue(HashMap keyValue)  throws Exception{
        key = (ArrayList)keyValue.get(CommonConstants.KEY);
        value = (ArrayList)keyValue.get(CommonConstants.VALUE);
    }
    
    void setTxtInstrumentNo1(String txtInstrumentNo1){
        this.txtInstrumentNo1 = txtInstrumentNo1;
        setChanged();
    }
    
    String getTxtInstrumentNo1(){
        return this.txtInstrumentNo1;
    }
    
    void setTxtInstrumentNo2(String txtInstrumentNo2){
        this.txtInstrumentNo2 = txtInstrumentNo2;
        setChanged();
    }
    
    String getTxtInstrumentNo2(){
        return this.txtInstrumentNo2;
    }
    
    void setTxtBatchId(String txtBatchId){
        this.txtBatchId = txtBatchId;
        setChanged();
    }
    
    String getTxtBatchId(){
        return this.txtBatchId;
    }
    
    void setLblReturnId(String lblReturnId){
        this.lblReturnId = lblReturnId;
        setChanged();
    }
    
    String getLblReturnId(){
        return this.lblReturnId;
    }
    
    void setChkPresentAgain(boolean chkYes){
        this.chkYes = chkYes;
        setChanged();
    }
    
    boolean getChkPresentAgain(){
        return this.chkYes;
    }
    
    /** Setter for property returnTypeModel.
     * @param cbmTransitPeriod New value of property returnTypeModel.
     *
     */
    public void setReturnTypeModel(ComboBoxModel _instrumentTypeModel) {
        returnTypeModel = _instrumentTypeModel;
    }
    
    /** Getter for property returnTypeModel
     * @return Value of property returnTypeModel
     *
     */
    public ComboBoxModel getReturnTypeModel() {
        return returnTypeModel;
    }
    
    /** Setter for property clearingTypeModel.
     * @param clearingTypeModel New value of property cbmTransitPeriod.
     *
     */
    public void setClearingypeModel(ComboBoxModel _clearingTypeModel) {
        returnTypeModel = _clearingTypeModel;
    }
    
    /** Getter for property clearingTypeModel
     * @return Value of property clearingTypeModel
     *
     */
    public ComboBoxModel getClearingTypeModel() {
        return clearingTypeModel;
    }
    
    void setCboReturnType(String cboReturnType){
        this.cboReturnType = cboReturnType;
        setChanged();
    }
    
    String getCboReturnType(){
        return this.cboReturnType;
    }
    
    void setCboClearingType(String cboClearingType){
        this.cboClearingType = cboClearingType;
        setChanged();
    }
    
    String getCboClearingType(){
        return this.cboClearingType;
    }
    
    void setTdtClearingDate(String tdtClearingDate){
        this.tdtClearingDate = tdtClearingDate;
        setChanged();
    }
    
    String getTdtClearingDate(){
        return this.tdtClearingDate;
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
    
    /** To update the Status based on result performed */
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
    
    public void ttNotifyObservers(){
        notifyObservers();
    }
    
    /** Empties all the Fields in the UI */
    public void resetForm(){
        setLblReturnId("");
        setTxtBatchId("");
        setCboReturnType("");
        setTxtInstrumentNo1("");
        setTxtInstrumentNo2("");
        setChkPresentAgain(false);
        setCboClearingType("");
        setTdtClearingDate("");
        ttNotifyObservers();
    }
    
    /** Returns the ReturnOfInstrumentsTO object by setting its fields */
    public ReturnOfInstrumentsTO getReturnOfInstrumentsTO(String command){
        ReturnOfInstrumentsTO objReturnOfInstrumentsTO = new ReturnOfInstrumentsTO();
        final String yes="Y";
        final String no="N";
        objReturnOfInstrumentsTO.setBranchId(getSelectedBranchID());
        if( getActionType() != ClientConstants.ACTIONTYPE_NEW){
            objReturnOfInstrumentsTO.setStatusBy(TrueTransactMain.USER_ID);
        }
        objReturnOfInstrumentsTO.setCommand(command);
        objReturnOfInstrumentsTO.setReturnType(CommonUtil.convertObjToStr(getReturnTypeModel().getKeyForSelected()));
        objReturnOfInstrumentsTO.setReturnId(getLblReturnId());
        objReturnOfInstrumentsTO.setInstrumentNo1(getTxtInstrumentNo1());
        objReturnOfInstrumentsTO.setInstrumentNo2(getTxtInstrumentNo2());
        objReturnOfInstrumentsTO.setBatchId(getTxtBatchId());
        
        if(getChkPresentAgain())
            objReturnOfInstrumentsTO.setPresentAgain(yes);
        else
            objReturnOfInstrumentsTO.setPresentAgain(no);
        
        objReturnOfInstrumentsTO.setClearingType(CommonUtil.convertObjToStr(getClearingTypeModel().getKeyForSelected()));
        Date IsDt = DateUtil.getDateMMDDYYYY(getTdtClearingDate());
        if(IsDt != null){
        Date isDate = (Date) currDt.clone();
        isDate.setDate(IsDt.getDate());
        isDate.setMonth(IsDt.getMonth());
        isDate.setYear(IsDt.getYear());
        objReturnOfInstrumentsTO.setClearingDate(isDate);
        }else{
            objReturnOfInstrumentsTO.setClearingDate(DateUtil.getDateMMDDYYYY(getTdtClearingDate()));
        }
//        objReturnOfInstrumentsTO.setClearingDate(DateUtil.getDateMMDDYYYY(getTdtClearingDate()));
        objReturnOfInstrumentsTO.setStatus(CommonConstants.STATUS_CREATED);
        objReturnOfInstrumentsTO.setInitiatedBranch(ProxyParameters.BRANCH_ID);
        return objReturnOfInstrumentsTO;
    }
    
    /** Setting all the observable fields using the ReturnOfInstrumentsTO fields */
    public void setReturnOfInstrumentsTO(ReturnOfInstrumentsTO objReturnOfInstrumentsTO){
        setCboReturnType((String) getReturnTypeModel().getDataForKey(CommonUtil.convertObjToStr(objReturnOfInstrumentsTO.getReturnType())));
        setTxtInstrumentNo1(objReturnOfInstrumentsTO.getInstrumentNo1());
        setTxtInstrumentNo2(objReturnOfInstrumentsTO.getInstrumentNo2());
        setLblReturnId(objReturnOfInstrumentsTO.getReturnId());
        setTxtBatchId(objReturnOfInstrumentsTO.getBatchId());
        if(objReturnOfInstrumentsTO.getPresentAgain().equals("Y")){
            setChkPresentAgain(true);
        }
        else{
            setChkPresentAgain(false);
        }
        
        setCboClearingType((String)getClearingTypeModel().getDataForKey(CommonUtil.convertObjToStr(objReturnOfInstrumentsTO.getClearingType())));
        setTdtClearingDate(DateUtil.getStringDate(objReturnOfInstrumentsTO.getClearingDate()));
        
        ttNotifyObservers();
    }
    
    /** Fills up the labels by setting it with the texts */
    public HashMap getResultMap(HashMap map){
        ArrayList resultList = new ArrayList();
        HashMap lblMap = null;
        // EDITDATA
        System.out.println("Ins 1" + getTxtInstrumentNo1());
        System.out.println("Ins 2" + getTxtInstrumentNo2());
        
        //__ if the record is not the One Selected...
        if(!getTxtInstrumentNo1().equalsIgnoreCase(CommonUtil.convertObjToStr(map.get("INSRU_NO1")))
           || !getTxtInstrumentNo2().equalsIgnoreCase(CommonUtil.convertObjToStr(map.get("INSRU_NO2")))){
            map.put("NEWDATA", "");
        }
        map.put("INITIATED_BRANCH", ProxyParameters.BRANCH_ID);
        map.put("TRANS_DT", currDt.clone());
        resultList = (ArrayList)ClientUtil.executeQuery("getOutwardClearingData", map);
        if(resultList.size()>0){
            lblMap = (HashMap) resultList.get(0);
        }
        return lblMap;
    }
    
    
    /** Populates the TO object when savebutton is clicked for  updation or deletion of table */
    public void populateData(HashMap whereMap) {
        HashMap mapData=null;
        try {
            mapData = proxy.executeQuery(whereMap, map);
            ReturnOfInstrumentsTO objReturnOfInstrumentsTO =
            (ReturnOfInstrumentsTO) ((List) mapData.get("ReturnOfInstrumentsTO")).get(0);
            setReturnOfInstrumentsTO(objReturnOfInstrumentsTO);
        } catch( Exception e ) {
            setResult(ClientConstants.ACTIONTYPE_FAILED);
            parseException.logException(e,true);
            
        }
    }
    
    /** Executes the Query for insertion, updation or deletion based on the command */
    public void execute(String command) {
        try {
            HashMap term = new HashMap();
            if(getAuthMap() == null){
                term.put("ReturnOfInstrumentsTO", getReturnOfInstrumentsTO(command));
            }
            
            term.put(CommonConstants.MODULE, getModule());
            term.put(CommonConstants.SCREEN, getScreen());
            term.put(CommonConstants.SELECTED_BRANCH_ID, getSelectedBranchID());
            term.put(CommonConstants.AUTHORIZEMAP, getAuthMap());
            
            HashMap proxyResultMap = proxy.execute(term, map);
            setResult(getActionType());
        } catch (Exception e) {
            setResult(ClientConstants.ACTIONTYPE_FAILED);
            parseException.logException(e,true);
        }
    }
    
    
    
    public HashMap getAuthMap() {
        return authMap;
    }
    
    
    public void setAuthMap(HashMap authMap) {
        this.authMap = authMap;
    }
    
}