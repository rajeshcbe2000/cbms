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

package com.see.truetransact.ui.transaction.token.tokenissue;

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
import com.see.truetransact.transferobject.transaction.token.tokenissue.TokenIssueTO;
import com.see.truetransact.clientutil.EnhancedTableModel;
import com.see.truetransact.ui.TrueTransactMain;
import java.util.Date;
/**
 *
 * @author Ashok Vijayakumar
 */

public class TokenIssueOB extends CObservable{
    
    /** Variables Declaration - Corresponding each Variable is in TokenConfigUI*/
    private String txtTokenIssueId = "";
    private String cboTokenType = "";
    private ComboBoxModel cbmTokenType;//Model for ui combobox cboTokenType
    private String cboSeriesNo = "";
    private ComboBoxModel cbmSeriesNo;//Model for ui combobox cboSeriesNO
    private String txtStartingTokenNo = "";
    private String txtEndingTokenNo = "";
    private String txtNoOfTokens = "";
    private String txtReceiverId = "";
    private String id="";
    /** Other Varibales Declartion */
    private ProxyFactory proxy;
    private HashMap map;//HashMap for setting key,values for Jndi,home,remote
    private HashMap lookUpHash;//HashMap used for puting a mapname with a key which is to be used for filling ComboBoxModel
    private static TokenIssueOB objTokenIssueOB;//Singleton Object Reference
    private final static Logger log = Logger.getLogger(TokenIssueOB.class);//Creating Instace of Log
    private ArrayList key,value;//ArrayList for populating the ComboBoxModel key-Storing keys value-for Storing Values
    private HashMap keyValue;//Which holds the resultset values as key,value pairs for populatine ComboBoxModel
    private final static ClientParseException parseException = ClientParseException.getInstance();
    private  TokenIssueTO objTokenIssueTO;//Reference for the EntityBean TokenConfigTO
    private int _actionType;//This vairable is used to type of action 1-New 2-Edit 3-Delete
    private int _result;
    private TokenIssueRB objTokenIssueRB = new TokenIssueRB();
    private ArrayList tblTitle = new ArrayList();
    private ArrayList tblRowList;
    private String lblStatus = ClientConstants.ACTION_STATUS[ClientConstants.ACTIONTYPE_CANCEL];
    private EnhancedTableModel tbmTokenLost;
    private Date currDt = null;
    /** Consturctor Declaration  for  TokenConfigOB */
    private TokenIssueOB() {
        try {
            currDt = ClientUtil.getCurrentDate();
            proxy = ProxyFactory.createProxy();
            setOperationMap();
            initUIComboBoxModel();
            setTblTokenLostTitle();
            tbmTokenLost = new EnhancedTableModel(null, tblTitle);
            removeTblTokenLostRow();
            fillDropdown();
        }catch(Exception e){
            parseException.logException(e,true);
        }
    }
    
    static {
        try {
            log.info("Creating ParameterOB...");
            objTokenIssueOB = new TokenIssueOB();
        } catch(Exception e) {
            parseException.logException(e,true);
        }
    }
    
    // Sets the HashMap required to set JNDI,Home and Remote
    private void setOperationMap() throws Exception{
        map = new HashMap();
        map.put(CommonConstants.JNDI, "TokenIssueJNDI");
        map.put(CommonConstants.HOME, "transaction.token.tokenissue.TokenIssueHome");
        map.put(CommonConstants.REMOTE, "tramsaction.token.tokenissue.TokenIssue");
    }
    
    /** Creating instance for ComboboxModel cbmTokenType */
    private void initUIComboBoxModel(){
        cbmTokenType = new ComboBoxModel();
        cbmSeriesNo = new ComboBoxModel();
    }
    
    /* Filling up the the ComboBox in the UI*/
    private void fillDropdown() throws Exception{
        try{
            log.info("Inside FillDropDown");
            lookUpHash = new HashMap();
            lookUpHash.put(CommonConstants.MAP_NAME, null);
            final ArrayList lookup_keys = new ArrayList();
            lookup_keys.add("TOKEN_TYPE");
            lookUpHash.put(CommonConstants.PARAMFORQUERY, lookup_keys);
            keyValue = ClientUtil.populateLookupData(lookUpHash);
            getKeyValue((HashMap)keyValue.get("TOKEN_TYPE"));
            cbmTokenType = new ComboBoxModel(key,value);
            makeComboBoxKeyValuesNull();
            
            lookUpHash = new HashMap();
            lookUpHash.put(CommonConstants.MAP_NAME,"getSelectTokenConfigSeries");
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
    
    public static TokenIssueOB getInstance()throws Exception{
        return objTokenIssueOB;
    }
    
    /** This method sets the Title for the tblTokenLost Table in the UI **/
    public void setTblTokenLostTitle() throws Exception{
        tblTitle.add(objTokenIssueRB.getString("lblSeriesNo"));
        tblTitle.add(objTokenIssueRB.getString("lblTokenNo"));
        tblTitle.add(objTokenIssueRB.getString("lblTokenStatus"));
        tblTitle.add(objTokenIssueRB.getString("lblLostDate"));
    }
    
    // This method removes the row from the tbmTokenLost in UI
    public void removeTblTokenLostRow(){
        int i = tbmTokenLost.getRowCount() - 1;
        while(i >= 0){
            tbmTokenLost.removeRow(i);
            i-=1;
        }
    }
    /**
     * Getter for property txtTokenConfigId.
     * @return Value of property txtTokenConfigId.
     */
    public java.lang.String getTxtTokenIssueId() {
        return txtTokenIssueId;
    }
    
    /**
     * Setter for property txtTokenConfigId.
     * @param txtTokenConfigId New value of property txtTokenConfigId.
     */
    public void setTxtTokenIssueId(java.lang.String txtTokenIssueId) {
        this.txtTokenIssueId = txtTokenIssueId;
        setChanged();
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
    // Setter method for txtStartingTokenNo
    void setTxtStartingTokenNo(String txtStartingTokenNo){
        this.txtStartingTokenNo = txtStartingTokenNo;
        setChanged();
    }
    // Getter method for txtStartingTokenNo
    String getTxtStartingTokenNo(){
        return this.txtStartingTokenNo;
    }
    
    // Setter method for txtEndingTokenNo
    void setTxtEndingTokenNo(String txtEndingTokenNo){
        this.txtEndingTokenNo = txtEndingTokenNo;
        setChanged();
    }
    // Getter method for txtEndingTokenNo
    String getTxtEndingTokenNo(){
        return this.txtEndingTokenNo;
    }
    
    // Setter method for txtNoOfTokens
    void setTxtNoOfTokens(String txtNoOfTokens){
        this.txtNoOfTokens = txtNoOfTokens;
        setChanged();
    }
    // Getter method for txtNoOfTokens
    String getTxtNoOfTokens(){
        return this.txtNoOfTokens;
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
    private TokenIssueTO getTokenIssueTO(String command){
        TokenIssueTO objTokenIssueTO = new TokenIssueTO();
        objTokenIssueTO.setCommand(command);
        objTokenIssueTO.setIssueDt(currDt);
        objTokenIssueTO.setIssueId(getTxtTokenIssueId());
        objTokenIssueTO.setTokenType(CommonUtil.convertObjToStr(getCbmTokenType().getKeyForSelected()));
        objTokenIssueTO.setSeriesNo(CommonUtil.convertObjToStr(getCbmSeriesNo().getKeyForSelected()));
        objTokenIssueTO.setTokenStartNo(new Double(getTxtStartingTokenNo()));
        objTokenIssueTO.setTokenEndNo(new Double(getTxtEndingTokenNo()));
        objTokenIssueTO.setNoOfTokens(new Double(getTxtNoOfTokens()));
        objTokenIssueTO.setBranchId(TrueTransactMain.BRANCH_ID);
        objTokenIssueTO.setStatusBy(TrueTransactMain.USER_ID);
        objTokenIssueTO.setStatusDt(currDt);
        objTokenIssueTO.setReceivedBy(getTxtReceiverId());
        if(command.equals(CommonConstants.TOSTATUS_INSERT)){
             objTokenIssueTO.setCreatedBy(TrueTransactMain.USER_ID);
             objTokenIssueTO.setCreatedDt(currDt);
        }
        return objTokenIssueTO;
    }
    
    private void setTokenIssueTO(TokenIssueTO objTokenIssueTO){
        setTxtTokenIssueId(objTokenIssueTO.getIssueId());
        cbmTokenType.setKeyForSelected(objTokenIssueTO.getTokenType());
        setCboTokenType(CommonUtil.convertObjToStr(getCbmTokenType().getDataForKey(objTokenIssueTO.getTokenType())));
        setCboSeriesNo(CommonUtil.convertObjToStr(getCbmSeriesNo().getDataForKey(objTokenIssueTO.getSeriesNo())));
        setTxtStartingTokenNo(CommonUtil.convertObjToStr(objTokenIssueTO.getTokenStartNo()));
        setTxtEndingTokenNo(CommonUtil.convertObjToStr(objTokenIssueTO.getTokenEndNo()));
        setTxtReceiverId(objTokenIssueTO.getReceivedBy());
        setTxtNoOfTokens(CommonUtil.convertObjToStr(objTokenIssueTO.getNoOfTokens()));
        setStatusBy(objTokenIssueTO.getStatusBy());
        setAuthorizeStatus(objTokenIssueTO.getAuthorizeStatus());
        notifyObservers();
    }
    
    /** Resets all the UI Fields */
    public void resetForm(){
        setTxtTokenIssueId("");
        setCboTokenType("");
        setCboSeriesNo("");
        setTxtStartingTokenNo("");
        setTxtEndingTokenNo("");
        setTxtReceiverId("");
        setTxtNoOfTokens("");
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
            term.put("TokenIssueTO", getTokenIssueTO(command));
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
            TokenIssueTO objTokenIssueTO =
            (TokenIssueTO) ((List) mapData.get("TokenIssueTO")).get(0);
            setTokenIssueTO(objTokenIssueTO);
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
    
   }