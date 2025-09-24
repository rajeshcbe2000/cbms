/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition.
 * 
 *
 * TDSConfigOB.java
 *
 * Created on Mon Jan 31 16:05:07 IST 2005
 */

package com.see.truetransact.ui.tds.tdsconfig;

import java.util.Observable;
import org.apache.log4j.Logger;
import com.see.truetransact.clientproxy.ProxyFactory;
import com.see.truetransact.clientexception.ClientParseException;
import com.see.truetransact.commonutil.DateUtil;
import com.see.truetransact.commonutil.CommonConstants;
import com.see.truetransact.commonutil.CommonUtil;
import com.see.truetransact.clientutil.ComboBoxModel;
import com.see.truetransact.clientutil.ClientUtil;
import com.see.truetransact.clientutil.ClientConstants;
import com.see.truetransact.uicomponent.CObservable;
import com.see.truetransact.transferobject.tds.tdsconfig.TDSConfigTO;
import com.see.truetransact.ui.TrueTransactMain;
import java.util.HashMap;
import java.util.ArrayList;
import java.util.List;
import java.util.Date;

/**
 *
 * @author Ashok Vijayakumar
 */

public class TDSConfigOB extends CObservable{
    
    Date curDate = null;
    private String tdtStartDate = "";
    private String tdtEndDate = "";
    private String txtTdsId = "";
    private String txtCutOfAmount = "";
    private String cboScope = "";
    private String cboCustType= "";
    private String tdsCrAcHdId="";
    private ComboBoxModel cbmScope;
    private ComboBoxModel cbmCustType;
    private String txtPercentage = "";
    private boolean rdoCutOff_Yes = false;
    private boolean rdoCutOff_No = false;
    private ArrayList key,value;
    private ProxyFactory proxy;
    private static TDSConfigOB objTDSConfigOB;
    private HashMap map,keyValue,lookUpHash;
    private final static Logger log = Logger.getLogger(TDSConfigOB.class);
    private final static ClientParseException parseException = ClientParseException.getInstance();
    private int _result,_actionType;
    private String lblStatus = ClientConstants.ACTION_STATUS[ClientConstants.ACTIONTYPE_CANCEL];
    private final String YES = "Y";
    private final String NO = "N";
    private String txtWithoutPANPercentage = ""; //17-02-2020
//    private String tdsCeAchdId="";
    
    /** Consturctor Declaration  for  TDSConfigOB */
    private TDSConfigOB() {
        try {
            curDate = ClientUtil.getCurrentDate();
            proxy = ProxyFactory.createProxy();
            setOperationMap();
            initUIComboBoxModel();
            fillDropdown();
        }catch(Exception e){
            e.printStackTrace();
            parseException.logException(e,true);
        }
    }
    
    static {
        try {
            log.info("Creating ParameterOB...");
            objTDSConfigOB = new TDSConfigOB();
        } catch(Exception e) {
            parseException.logException(e,true);
        }
    }
    
    // Sets the HashMap required to set JNDI,Home and Remote
    private void setOperationMap() throws Exception{
        map = new HashMap();
        map.put(CommonConstants.JNDI, "TDSConfigJNDI");
        map.put(CommonConstants.HOME, "tds.tdsconfig.TDSConfigHome");
        map.put(CommonConstants.REMOTE, "tds.tdsconfig.TDSConfig");
    }
    
    /** Creating instance for ComboboxModel cbmTokenType */
    private void initUIComboBoxModel(){
        cbmScope = new ComboBoxModel();
        cbmCustType = new ComboBoxModel();
    }
    
    /* Filling up the the ComboBox in the UI*/
    private void fillDropdown() throws Exception{
        try{
            log.info("Inside FillDropDown");
            lookUpHash = new HashMap();
            lookUpHash.put(CommonConstants.MAP_NAME, null);
            final ArrayList lookup_keys = new ArrayList();
            lookup_keys.add("TDS_CONFIG_SCOPE");
            lookup_keys.add("CUSTOMER.TYPE");
            lookUpHash.put(CommonConstants.PARAMFORQUERY, lookup_keys);
            keyValue = ClientUtil.populateLookupData(lookUpHash);
            getKeyValue((HashMap)keyValue.get("TDS_CONFIG_SCOPE"));
            cbmScope = new ComboBoxModel(key,value);
            getKeyValue((HashMap)keyValue.get("CUSTOMER.TYPE"));
            cbmCustType = new ComboBoxModel(key,value);
        }catch(NullPointerException e){
            parseException.logException(e,true);
        }catch(Exception e){
            parseException.logException(e,true);
        }
    }
    
    /** Populates two ArrayList key,value */
    private void getKeyValue(HashMap keyValue)  throws Exception{
        key = (ArrayList)keyValue.get(CommonConstants.KEY);
        value = (ArrayList)keyValue.get(CommonConstants.VALUE);
    }
    
    /**
     * Returns an instance of TokenConfigOB.
     * @return  TokenConfigOB
     */
    
    public static TDSConfigOB getInstance()throws Exception{
        return objTDSConfigOB;
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
    
    // Setter method for txtTdsId
    void setTxtTdsId(String txtTdsId){
        this.txtTdsId = txtTdsId;
        setChanged();
    }
    // Getter method for txtTdsId
    String getTxtTdsId(){
        return this.txtTdsId;
    }
    
    // Setter method for txtCutOfAmount
    void setTxtCutOfAmount(String txtCutOfAmount){
        this.txtCutOfAmount = txtCutOfAmount;
        setChanged();
    }
    // Getter method for txtCutOfAmount
    String getTxtCutOfAmount(){
        return this.txtCutOfAmount;
    }
    
    // Setter method for cboScope
    void setCboScope(String cboScope){
        this.cboScope = cboScope;
        setChanged();
    }
    // Getter method for cboScope
    String getCboScope(){
        return this.cboScope;
    }
    
    /**
     * Getter for property cbmScope.
     * @return Value of property cbmScope.
     */
    public com.see.truetransact.clientutil.ComboBoxModel getCbmScope() {
        return cbmScope;
    }
    
    /**
     * Setter for property cbmScope.
     * @param cbmScope New value of property cbmScope.
     */
    public void setCbmScope(com.see.truetransact.clientutil.ComboBoxModel cbmScope) {
        this.cbmScope = cbmScope;
    }
    
    // Setter method for txtPercentage
    void setTxtPercentage(String txtPercentage){
        this.txtPercentage = txtPercentage;
        setChanged();
    }
    // Getter method for txtPercentage
    String getTxtPercentage(){
        return this.txtPercentage;
    }
    
    /**
     * Getter for property rdoConfig_Yes.
     * @return Value of property rdoConfig_Yes.
     */
    public boolean getRdoCutOff_Yes() {
        return rdoCutOff_Yes;
    }
    
    /**
     * Setter for property rdoConfig_Yes.
     * @param rdoConfig_Yes New value of property rdoConfig_Yes.
     */
    public void setRdoCutOff_Yes(boolean rdoCutOff_Yes) {
        this.rdoCutOff_Yes = rdoCutOff_Yes;
        setChanged();
    }
    
    /**
     * Getter for property rdoConfig_No.
     * @return Value of property rdoConfig_No.
     */
    public boolean getRdoCutOff_No() {
        return rdoCutOff_No;
    }
    
    /**
     * Setter for property rdoConfig_No.
     * @param rdoConfig_No New value of property rdoConfig_No.
     */
    public void setRdoCutOff_No(boolean rdoCutOff_No) {
        this.rdoCutOff_No = rdoCutOff_No;
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
    
    /** Returns an instance of TDSConfigTO by setting all the varibales of it **/
    private TDSConfigTO getTDSConfigTO(String command){
        TDSConfigTO objTDSConfigTO = new TDSConfigTO();
        objTDSConfigTO.setCommand(command);
        objTDSConfigTO.setTdsId(getTxtTdsId());
//        objTDSConfigTO.setFinStartDt(DateUtil.getDateMMDDYYYY(getTdtStartDate()));
//        objTDSConfigTO.setFinEndDt(DateUtil.getDateMMDDYYYY(getTdtEndDate()));
        Date StDt = DateUtil.getDateMMDDYYYY(getTdtStartDate());
        if(StDt != null){
        Date stDate = (Date)curDate.clone();
        stDate.setDate(StDt.getDate());
        stDate.setMonth(StDt.getMonth());
        stDate.setYear(StDt.getYear());
        objTDSConfigTO.setFinStartDt(stDate);
        }else{
            objTDSConfigTO.setFinStartDt(DateUtil.getDateMMDDYYYY(getTdtStartDate()));
        }
        
        Date IsDt = DateUtil.getDateMMDDYYYY(getTdtEndDate());
        if(IsDt != null){
        Date isDate = (Date)curDate.clone();
        isDate.setDate(IsDt.getDate());
        isDate.setMonth(IsDt.getMonth());
        isDate.setYear(IsDt.getYear());
        objTDSConfigTO.setFinEndDt(isDate);
        }else{
            objTDSConfigTO.setFinEndDt(DateUtil.getDateMMDDYYYY(getTdtEndDate()));
        }
        
        objTDSConfigTO.setTdsScope(CommonUtil.convertObjToStr(getCbmScope().getKeyForSelected()));
        objTDSConfigTO.setCustType(CommonUtil.convertObjToStr(getCbmCustType().getKeyForSelected()));
        objTDSConfigTO.setCutOfAmt(new Double(getTxtCutOfAmount()));
        objTDSConfigTO.setTdsPercentage(new Double(getTxtPercentage()));
        if(getRdoCutOff_Yes()){
            objTDSConfigTO.setIncludeCutof(YES);
        }else{
            objTDSConfigTO.setIncludeCutof(NO);
        }
        objTDSConfigTO.setStatusBy(TrueTransactMain.USER_ID);
        objTDSConfigTO.setStatusDt(curDate);
        objTDSConfigTO.setTdsCrAchdId(getTdsCrAcHdId());
        objTDSConfigTO.setWithOutPANPercentage(CommonUtil.convertObjToDouble(getTxtWithoutPANPercentage()));//17-02-2020
        return objTDSConfigTO;
    }
    
    /** sets the values of TDSConfigTo variables to TDSConfigOB Variables **/
    private void setTDSConfigTO(TDSConfigTO objTDSConfigTO){
        System.out.println("TDSConfigTO "+ objTDSConfigTO);
        setTxtTdsId(objTDSConfigTO.getTdsId());
        setTdtStartDate(DateUtil.getStringDate(objTDSConfigTO.getFinStartDt()));
        setTdtEndDate(DateUtil.getStringDate(objTDSConfigTO.getFinEndDt()));
        setCboScope(CommonUtil.convertObjToStr(getCbmScope().getDataForKey(objTDSConfigTO.getTdsScope())));
        setCboCustType(CommonUtil.convertObjToStr(getCbmCustType().getDataForKey(objTDSConfigTO.getCustType())));
        setTxtCutOfAmount(CommonUtil.convertObjToStr(objTDSConfigTO.getCutOfAmt()));
        setTxtPercentage(CommonUtil.convertObjToStr(objTDSConfigTO.getTdsPercentage()));
        setTdsCrAcHdId(CommonUtil.convertObjToStr(objTDSConfigTO.getTdsCrAchdId()));
        if(objTDSConfigTO.getIncludeCutof()!=null){
        if(objTDSConfigTO.getIncludeCutof().equals(YES)){
            setRdoCutOff_Yes(true);
        }else{
            setRdoCutOff_No(true);
        }
        }else{
            setRdoCutOff_No(true);
        }
        setTxtWithoutPANPercentage(CommonUtil.convertObjToStr(objTDSConfigTO.getWithOutPANPercentage())); // 17-02-2020
        
        notifyObservers();
        
    }
    
    /** Clear up all the Fields of UI thru OB **/
    public void resetForm(){
        setTxtTdsId("");
        setTdtStartDate("");
        setTdtEndDate("");
        setCboScope("");
        setCboCustType("");
        setTxtCutOfAmount("");
        setTxtPercentage("");
        setRdoCutOff_Yes(false);
        setTdsCrAcHdId("");
        setTxtWithoutPANPercentage(""); // 17-02-2020
        notifyObservers();
        
    }
    
    /* Populates the TO object by executing a Query */
    public void populateData(HashMap whereMap) {
        HashMap mapData=null;
        try {
            mapData = proxy.executeQuery(whereMap, map);
            TDSConfigTO objTDSConfigTO =
            (TDSConfigTO) ((List) mapData.get("TDSConfigTO")).get(0);
            setTDSConfigTO(objTDSConfigTO);
        } catch( Exception e ) {
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
            term.put(CommonConstants.BRANCH_ID, TrueTransactMain.BRANCH_ID);
            term.put("TDSConfigTO", getTDSConfigTO(command));
            HashMap proxyResultMap = proxy.execute(term, map);
            setResult(getActionType());
        } catch (Exception e) {
            setResult(ClientConstants.ACTIONTYPE_FAILED);
            parseException.logException(e,true);
        }
    }
    
    /** Return an ArrayList by executing Query **/
    public ArrayList getResultList(){
        ArrayList list = (ArrayList) ClientUtil.executeQuery("getSelectTDSDates", null);
        return list;
    }
    
    /**
     * Getter for property tdsCrAcHdId.
     * @return Value of property tdsCrAcHdId.
     */
    public java.lang.String getTdsCrAcHdId() {
        return tdsCrAcHdId;
    }    
    
    /**
     * Setter for property tdsCrAcHdId.
     * @param tdsCrAcHdId New value of property tdsCrAcHdId.
     */
    public void setTdsCrAcHdId(java.lang.String tdsCrAcHdId) {
        this.tdsCrAcHdId = tdsCrAcHdId;
    }
    
    /**
     * Getter for property cbmCustType.
     * @return Value of property cbmCustType.
     */
    public com.see.truetransact.clientutil.ComboBoxModel getCbmCustType() {
        return cbmCustType;
    }
    
    /**
     * Setter for property cbmCustType.
     * @param cbmCustType New value of property cbmCustType.
     */
    public void setCbmCustType(com.see.truetransact.clientutil.ComboBoxModel cbmCustType) {
        this.cbmCustType = cbmCustType;
    }
    
    /**
     * Getter for property cboCustType.
     * @return Value of property cboCustType.
     */
    public java.lang.String getCboCustType() {
        return cboCustType;
    }
    
    /**
     * Setter for property cboCustType.
     * @param cboCustType New value of property cboCustType.
     */
    public void setCboCustType(java.lang.String cboCustType) {
        this.cboCustType = cboCustType;
    }

    public String getTxtWithoutPANPercentage() {
        return txtWithoutPANPercentage;
    }

    public void setTxtWithoutPANPercentage(String txtWithoutPANPercentage) {
        this.txtWithoutPANPercentage = txtWithoutPANPercentage;
    }
    
    /**
     * Getter for property tdsCeAchdId.
     * @return Value of property tdsCeAchdId.
     */
//    public java.lang.String getTdsCeAchdId() {
//        return tdsCeAchdId;
//    }
//    
//    /**
//     * Setter for property tdsCeAchdId.
//     * @param tdsCeAchdId New value of property tdsCeAchdId.
//     */
//    public void setTdsCeAchdId(java.lang.String tdsCeAchdId) {
//        this.tdsCeAchdId = tdsCeAchdId;
//    }
        
}