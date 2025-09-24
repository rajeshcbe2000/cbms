/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition.
 * 
 *
 * TokenConfigOB.java
 *
 * Created on Thu Jan 20 15:43:27 IST 2005
 */

package com.see.truetransact.ui.remittance.printingdetails;

import org.apache.log4j.Logger;
import java.util.Observable;
import java.util.HashMap;
import java.util.ArrayList;
import java.util.List;

import com.see.truetransact.clientproxy.ProxyFactory;
import com.see.truetransact.clientutil.ComboBoxModel;
import com.see.truetransact.clientutil.ClientUtil;
import com.see.truetransact.clientutil.ClientConstants;
import com.see.truetransact.clientexception.ClientParseException;
import com.see.truetransact.commonutil.CommonConstants;
import com.see.truetransact.commonutil.CommonUtil;
import com.see.truetransact.commonutil.DateUtil;
import com.see.truetransact.uicomponent.CObservable;
import com.see.truetransact.transferobject.remittance.InstrumentAllotmentTO;
import com.see.truetransact.clientutil.EnhancedTableModel;
import com.see.truetransact.ui.TrueTransactMain;
import java.util.Date;
/**
 *
 * @author Ashok Vijayakumar
 */

public class InstrumentAllotmentOB extends CObservable{
    
    /** Variables Declaration - Corresponding each Variable is in TokenConfigUI*/
    private String txtInstrumentIssueId = "";
    private String cboTokenType = "";
    private ComboBoxModel cbmTokenType;//Model for ui combobox cboTokenType
    private String cboSeriesNo = "";
    private ComboBoxModel cbmSeriesNo;//Model for ui combobox cboSeriesNO
    private String txtStartingInstrumentNo = "";
    private String txtEndingInstrumentNo = "";
    private String txtNoOfInstruments = "";
    private String txtReceiverId = "";
    private String id="";
    /** Other Varibales Declartion */
    private ProxyFactory proxy;
    private HashMap map;//HashMap for setting key,values for Jndi,home,remote
    private HashMap lookUpHash;//HashMap used for puting a mapname with a key which is to be used for filling ComboBoxModel
    private static InstrumentAllotmentOB objInstrumentAllotmentOB;//Singleton Object Reference
//    private final static Logger log = Logger.getLogger(TokenIssueOB.class);//Creating Instace of Log
    private ArrayList key,value;//ArrayList for populating the ComboBoxModel key-Storing keys value-for Storing Values
    private HashMap keyValue;//Which holds the resultset values as key,value pairs for populatine ComboBoxModel
    private final static ClientParseException parseException = ClientParseException.getInstance();
    private  InstrumentAllotmentTO objInstrumentAllotmentTO;//Reference for the EntityBean TokenConfigTO
    private int _actionType;//This vairable is used to type of action 1-New 2-Edit 3-Delete
    private int _result;
//    private TokenIssueRB objTokenIssueRB = new TokenIssueRB();
    private ArrayList tblTitle = new ArrayList();
    private ArrayList tblRowList;
    private String lblStatus = ClientConstants.ACTION_STATUS[ClientConstants.ACTIONTYPE_CANCEL];
    private EnhancedTableModel tbmTokenLost;
    private Date currDt = null;
    /** Consturctor Declaration  for  TokenConfigOB */
    private InstrumentAllotmentOB() {
        try {
            currDt = ClientUtil.getCurrentDate();
            proxy = ProxyFactory.createProxy();
            setOperationMap();
            initUIComboBoxModel();
//            setTblTokenLostTitle();
            tbmTokenLost = new EnhancedTableModel(null, tblTitle);
            removeTblTokenLostRow();
            fillDropdown();
        }catch(Exception e){
            parseException.logException(e,true);
        }
    }
    
    static {
        try {
//            log.info("Creating ParameterOB...");
            objInstrumentAllotmentOB = new InstrumentAllotmentOB();
        } catch(Exception e) {
            parseException.logException(e,true);
        }
    }
    
    // Sets the HashMap required to set JNDI,Home and Remote
    private void setOperationMap() throws Exception{
        map = new HashMap();
        map.put(CommonConstants.JNDI, "InstrumentAllotmentJNDI");
        map.put(CommonConstants.HOME, "remittance.InstrumentAllotmentHome");
        map.put(CommonConstants.REMOTE, "remittance.InstrumentAllotment");
    }
    
    /** Creating instance for ComboboxModel cbmTokenType */
    private void initUIComboBoxModel(){
        cbmTokenType = new ComboBoxModel();
        cbmSeriesNo = new ComboBoxModel();
    }
     public static InstrumentAllotmentOB getInstance()throws Exception{
        return objInstrumentAllotmentOB;
    }
    
    /* Filling up the the ComboBox in the UI*/
    private void fillDropdown() throws Exception{
        try{
//            log.info("Inside FillDropDown");
            lookUpHash = new HashMap();
            lookUpHash.put(CommonConstants.MAP_NAME, null);
            final ArrayList lookup_keys = new ArrayList();
            makeComboBoxKeyValuesNull();
            lookUpHash = new HashMap();
            lookUpHash.put(CommonConstants.MAP_NAME,"getSelectInstrumentSeriesNo");
            HashMap where = new HashMap();
            where.put("BRANCH_ID", TrueTransactMain.BRANCH_ID);
            lookUpHash.put(CommonConstants.PARAMFORQUERY, where);
            keyValue = ClientUtil.populateLookupData(lookUpHash);
            getKeyValue((HashMap)keyValue.get(CommonConstants.DATA));
            cbmSeriesNo = new ComboBoxModel(key,value);
            makeComboBoxKeyValuesNull();
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
     * To make ComboBox key values Null
     */
    private void makeComboBoxKeyValuesNull(){
        key = null;
        value = null;
        lookUpHash = null;
        keyValue = null;
    }
    /**
     * Returns an instance of TokenConfigOB.
     * @return  TokenConfigOB
     */
    
//    public static TokenIssueOB getInstance()throws Exception{
////        return objTokenIssueOB;
//    }
    
    /** This method sets the Title for the tblTokenLost Table in the UI **/
//    public void setTblTokenLostTitle() throws Exception{
//        tblTitle.add(objTokenIssueRB.getString("lblSeriesNo"));
//        tblTitle.add(objTokenIssueRB.getString("lblTokenNo"));
//        tblTitle.add(objTokenIssueRB.getString("lblTokenStatus"));
//        tblTitle.add(objTokenIssueRB.getString("lblLostDate"));
//    }
    
    // This method removes the row from the tbmTokenLost in UI
    public void removeTblTokenLostRow(){
        int i = tbmTokenLost.getRowCount() - 1;
        while(i >= 0){
            tbmTokenLost.removeRow(i);
            i-=1;
        }
    }
   
    
    // Setter method for cboTokenType
    void setCboTokenType(String cboTokenType){
        this.cboTokenType = cboTokenType;
        setChanged();
    }
    // Getter method for cboTokenType
    String getCboTokenType(){
        return this.cboTokenType;
    }
    
    /**
     * Getter for property cbmTokenType.
     * @return Value of property cbmTokenType.
     */
    public com.see.truetransact.clientutil.ComboBoxModel getCbmTokenType() {
        return cbmTokenType;
    }
    
    /**
     * Setter for property cbmTokenType.
     * @param cbmTokenType New value of property cbmTokenType.
     */
    public void setCbmTokenType(com.see.truetransact.clientutil.ComboBoxModel cbmTokenType) {
        this.cbmTokenType = cbmTokenType;
    }
    
    // Setter method for txtSeriesNo
    void setCboSeriesNo(String cboSeriesNo){
        this.cboSeriesNo = cboSeriesNo;
        setChanged();
    }
    // Getter method for txtSeriesNo
    String getCboSeriesNo(){
        return this.cboSeriesNo;
    }
    
    /**
     * Getter for property cbmSeriesNo.
     * @return Value of property cbmSeriesNo.
     */
    public com.see.truetransact.clientutil.ComboBoxModel getCbmSeriesNo() {
        return cbmSeriesNo;
    }
    
    /**
     * Setter for property cbmSeriesNo.
     * @param cbmSeriesNo New value of property cbmSeriesNo.
     */
    public void setCbmSeriesNo(com.see.truetransact.clientutil.ComboBoxModel cbmSeriesNo) {
        this.cbmSeriesNo = cbmSeriesNo;
        setChanged();
    }
   
    
 
    
  
    
    /**
     * Getter for property txtReceiverId.
     * @return Value of property txtReceiverId.
     */
    public java.lang.String getTxtReceiverId() {
        return txtReceiverId;
    }
    
    /**
     * Setter for property txtReceiverId.
     * @param txtReceiverId New value of property txtReceiverId.
     */
    public void setTxtReceiverId(java.lang.String txtReceiverId) {
        this.txtReceiverId = txtReceiverId;
        setChanged();
    }
    
    /**
     * Getter for property tbmTokenLost.
     * @return Value of property tbmTokenLost.
     */
    public com.see.truetransact.clientutil.EnhancedTableModel getTbmTokenLost() {
        return tbmTokenLost;
    }
    
    /**
     * Setter for property tbmTokenLost.
     * @param tbmTokenLost New value of property tbmTokenLost.
     */
    public void setTbmTokenLost(com.see.truetransact.clientutil.EnhancedTableModel tbmTokenLost) {
        this.tbmTokenLost = tbmTokenLost;
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
    
    /** Creates an Instance of TokenConfigTO Bean and sets its variables with OBMethods */
    private InstrumentAllotmentTO  getInstrumentAllotmentTO(String command){
        InstrumentAllotmentTO objInstrumentAllotmentTO = new InstrumentAllotmentTO();
        objInstrumentAllotmentTO.setCommand(command);
        objInstrumentAllotmentTO.setIssueDt(currDt);
        objInstrumentAllotmentTO.setIssueId(getTxtInstrumentIssueId());
        objInstrumentAllotmentTO.setSeriesNo(CommonUtil.convertObjToStr(getCbmSeriesNo().getKeyForSelected()));
        objInstrumentAllotmentTO.setInstrumentStartNo(new Double(getTxtStartingInstrumentNo()));
        objInstrumentAllotmentTO.setInstrumentEndNo(CommonUtil.convertObjToDouble(getTxtEndingInstrumentNo()));
        objInstrumentAllotmentTO.setNoOfInstruments(CommonUtil.convertObjToDouble(getTxtNoOfInstruments()));
        objInstrumentAllotmentTO.setBranchId(TrueTransactMain.BRANCH_ID);
        objInstrumentAllotmentTO.setStatusBy(TrueTransactMain.USER_ID);
        objInstrumentAllotmentTO.setStatusDt(currDt);
        objInstrumentAllotmentTO.setReceivedBy(getTxtReceiverId());
        if(command.equals(CommonConstants.TOSTATUS_INSERT)){
             objInstrumentAllotmentTO.setCreatedBy(TrueTransactMain.USER_ID);
             objInstrumentAllotmentTO.setCreatedDt(currDt);
        }
        return objInstrumentAllotmentTO;
    }
    
    private void setInstrumentAllotmentTO(InstrumentAllotmentTO  objInstrumentAllotmentTO){
      
     setTxtInstrumentIssueId(CommonUtil.convertObjToStr(objInstrumentAllotmentTO.getIssueId()));
        setCboSeriesNo(CommonUtil.convertObjToStr(getCbmSeriesNo().getDataForKey(objInstrumentAllotmentTO.getSeriesNo())));
        setTxtStartingInstrumentNo(CommonUtil.convertObjToStr(objInstrumentAllotmentTO.getInstrumentStartNo()));
        setTxtEndingInstrumentNo(CommonUtil.convertObjToStr(objInstrumentAllotmentTO.getInstrumentEndNo()));
        setTxtReceiverId(objInstrumentAllotmentTO.getReceivedBy());
//        setTxtNoOfInstruments(CommonUtil.convertObjToStr(objInstrumentAllotmentTO.getNoOfInstruments()));
        setStatusBy(objInstrumentAllotmentTO.getStatusBy());
        setAuthorizeStatus(objInstrumentAllotmentTO.getAuthorizeStatus());
        setTxtNoOfInstruments(CommonUtil.convertObjToStr(objInstrumentAllotmentTO.getNoOfInstruments()));
        notifyObservers();
    }
    
    /** Resets all the UI Fields */
    public void resetForm(){
        setTxtInstrumentIssueId("");
        setCboTokenType("");
        setCboSeriesNo("");
        setTxtStartingInstrumentNo("");
        setTxtEndingInstrumentNo("");
        setTxtReceiverId("");
        setTxtNoOfInstruments("");
        removeTblTokenLostRow();
        notifyObservers();
    }
    
    /* Executes Query using the TO object */
    public void execute(String command) {
        try {
            HashMap term = new HashMap();
            term.put(CommonConstants.MODULE, getModule());
            term.put(CommonConstants.SCREEN, getScreen());
            term.put(CommonConstants.USER_ID, TrueTransactMain.USER_ID);
            term.put(CommonConstants.BRANCH_ID, TrueTransactMain.BRANCH_ID);
            term.put("InstrumentAllotmentTO", getInstrumentAllotmentTO(command));
            HashMap proxyReturnMap = proxy.execute(term, map);
            setProxyReturnMap(proxyReturnMap);
            setResult(getActionType());
        } catch (Exception e) {
            setResult(ClientConstants.ACTIONTYPE_FAILED);
            parseException.logException(e,true);
        }
    }
    
    /* Populates the TO object by executing a Query */
    public void populateData(HashMap whereMap) {
        HashMap mapData=null;
        try {
            mapData = proxy.executeQuery(whereMap, map);
            InstrumentAllotmentTO objInstrumentAllotmentTO =
            (InstrumentAllotmentTO) ((List) mapData.get("InstrumentAllotmentTO")).get(0);
            setInstrumentAllotmentTO(objInstrumentAllotmentTO);
        } catch( Exception e ) {
            setResult(ClientConstants.ACTIONTYPE_FAILED);
            parseException.logException(e,true);
            
        }
    }
    
    /** Returns ArrayList by executing a query thru ClientUtil's executeQuery method **/
    public ArrayList getResultList(HashMap map){
        HashMap resultMap = null;
        ArrayList resultList = null;
        String stmtName = CommonUtil.convertObjToStr(map.get(CommonConstants.MAP_NAME));
        HashMap where = (HashMap) map.get(CommonConstants.MAP_WHERE);
        resultList =(ArrayList) ClientUtil.executeQuery(stmtName, where);
        return resultList;
        
    }
    
    /** This will sets the all the elements of the ArrayList in to tbmTokenLost row **/
    public void setTbmTokenLost(ArrayList list){
        int size=list.size();
        for (int i=0;i<size;i++){
            tblRowList = new ArrayList();
            HashMap columnData =(HashMap) list.get(i);
            tblRowList.add(columnData.get("SERIES_NO"));
            tblRowList.add(columnData.get("TOKEN_NO"));
            tblRowList.add(columnData.get("TOKEN_STATUS"));
            tblRowList.add(columnData.get("LOST_DT"));
            tbmTokenLost.insertRow(0,tblRowList);
        }
        
    }
    
    /**
     * Getter for property id.
     * @return Value of property id.
     */
    public java.lang.String getId() {
        return id;
    }
    
    /**
     * Setter for property id.
     * @param id New value of property id.
     */
    public void setId(java.lang.String id) {
        this.id = id;
    }
    
    /**
     * Getter for property txtStartingInstrumentNo.
     * @return Value of property txtStartingInstrumentNo.
     */
    public java.lang.String getTxtStartingInstrumentNo() {
        return txtStartingInstrumentNo;
    }
    
    /**
     * Setter for property txtStartingInstrumentNo.
     * @param txtStartingInstrumentNo New value of property txtStartingInstrumentNo.
     */
    public void setTxtStartingInstrumentNo(java.lang.String txtStartingInstrumentNo) {
        this.txtStartingInstrumentNo = txtStartingInstrumentNo;
    }
    
    /**
     * Getter for property txtEndingInstrumentNo.
     * @return Value of property txtEndingInstrumentNo.
     */
    public java.lang.String getTxtEndingInstrumentNo() {
        return txtEndingInstrumentNo;
    }
    
    /**
     * Setter for property txtEndingInstrumentNo.
     * @param txtEndingInstrumentNo New value of property txtEndingInstrumentNo.
     */
    public void setTxtEndingInstrumentNo(java.lang.String txtEndingInstrumentNo) {
        this.txtEndingInstrumentNo = txtEndingInstrumentNo;
    }
    
    /**
     * Getter for property txtNoOfInstruments.
     * @return Value of property txtNoOfInstruments.
     */
    public java.lang.String getTxtNoOfInstruments() {
        return txtNoOfInstruments;
    }
    
    /**
     * Setter for property txtNoOfInstruments.
     * @param txtNoOfInstruments New value of property txtNoOfInstruments.
     */
    public void setTxtNoOfInstruments(java.lang.String txtNoOfInstruments) {
        this.txtNoOfInstruments = txtNoOfInstruments;
    }
    
    /**
     * Getter for property txtInstrumentIssueId.
     * @return Value of property txtInstrumentIssueId.
     */
    public java.lang.String getTxtInstrumentIssueId() {
        return txtInstrumentIssueId;
    }
    
    /**
     * Setter for property txtInstrumentIssueId.
     * @param txtInstrumentIssueId New value of property txtInstrumentIssueId.
     */
    public void setTxtInstrumentIssueId(java.lang.String txtInstrumentIssueId) {
        this.txtInstrumentIssueId = txtInstrumentIssueId;
    }
    
   }