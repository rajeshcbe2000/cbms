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

package com.see.truetransact.ui.transaction.token.tokenconfig;

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
import com.see.truetransact.transferobject.transaction.token.tokenconfig.TokenConfigTO;
import com.see.truetransact.ui.TrueTransactMain;

/**
 *
 * @author Ashok Vijayakumar
 */

public class TokenConfigOB extends CObservable{
    
    /** Variables Declaration - Corresponding each Variable is in TokenConfigUI*/
    private String txtTokenConfigId = "";
    private String cboTokenType = "";
    private ComboBoxModel cbmTokenType;//Model for ui combobox cboTokenType
    private String txtSeriesNo = "";
    private String txtStartingTokenNo = "";
    private String txtEndingTokenNo = "";
    private String txtNoOfTokens = "";
    /** Other Varibales Declartion */
    private ProxyFactory proxy;
    private HashMap map;//HashMap for setting key,values for Jndi,home,remote
    private HashMap lookUpHash;//HashMap used for puting a mapname with a key which is to be used for filling ComboBoxModel
    private static TokenConfigOB objTokenConfigOB;//Singleton Object Reference
    private final static Logger log = Logger.getLogger(TokenConfigOB.class);//Creating Instace of Log
    private ArrayList key,value;//ArrayList for populating the ComboBoxModel key-Storing keys value-for Storing Values
    private HashMap keyValue;//Which holds the resultset values as key,value pairs for populatine ComboBoxModel
    private final static ClientParseException parseException = ClientParseException.getInstance();
    private  TokenConfigTO objTokenConfigTO;//Reference for the EntityBean TokenConfigTO
    private int _actionType;//This vairable is used to type of action 1-New 2-Edit 3-Delete
    private int _result;
    private String lblStatus = ClientConstants.ACTION_STATUS[ClientConstants.ACTIONTYPE_CANCEL];
    
    /** Consturctor Declaration  for  TokenConfigOB */
    private TokenConfigOB() {
        try {
            proxy = ProxyFactory.createProxy();
            setOperationMap();
            initUIComboBoxModel();
            fillDropdown();
        }catch(Exception e){
            parseException.logException(e,true);
        }
    }
    
    static {
        try {
            log.info("Creating ParameterOB...");
            objTokenConfigOB = new TokenConfigOB();
        } catch(Exception e) {
            parseException.logException(e,true);
        }
    }
    
    // Sets the HashMap required to set JNDI,Home and Remote
    private void setOperationMap() throws Exception{
        map = new HashMap();
        map.put(CommonConstants.JNDI, "TokenConfigJNDI");
        map.put(CommonConstants.HOME, "transaction.token.tokenconfig.TokenConfigHomee");
        map.put(CommonConstants.REMOTE, "tramsaction.token.tokenconfig.TokenConfig");
    }
    
    /** Creating instance for ComboboxModel cbmTokenType */
    private void initUIComboBoxModel(){
        cbmTokenType = new ComboBoxModel();
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
    
    public static TokenConfigOB getInstance()throws Exception{
        return objTokenConfigOB;
    }
    
    /**
     * Getter for property txtTokenConfigId.
     * @return Value of property txtTokenConfigId.
     */
    public java.lang.String getTxtTokenConfigId() {
        return txtTokenConfigId;
    }
    
    /**
     * Setter for property txtTokenConfigId.
     * @param txtTokenConfigId New value of property txtTokenConfigId.
     */
    public void setTxtTokenConfigId(java.lang.String txtTokenConfigId) {
        this.txtTokenConfigId = txtTokenConfigId;
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
    void setTxtSeriesNo(String txtSeriesNo){
        this.txtSeriesNo = txtSeriesNo;
        setChanged();
    }
    // Getter method for txtSeriesNo
    String getTxtSeriesNo(){
        return this.txtSeriesNo;
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
    private TokenConfigTO getTokenConfigTO(String command){
        TokenConfigTO objTokenConfigTO = new TokenConfigTO();
        objTokenConfigTO.setCommand(command);
        objTokenConfigTO.setConfigId(getTxtTokenConfigId());
        objTokenConfigTO.setTokenType(CommonUtil.convertObjToStr(getCbmTokenType().getKeyForSelected()));
        objTokenConfigTO.setSeriesNo(getTxtSeriesNo());
        objTokenConfigTO.setTokenStartNo(new Double(getTxtStartingTokenNo()));
        objTokenConfigTO.setTokenEndNo(new Double(getTxtEndingTokenNo()));
        objTokenConfigTO.setBranchId(TrueTransactMain.BRANCH_ID);
        if(command.equals(CommonConstants.TOSTATUS_INSERT)){
            objTokenConfigTO.setCreatedBy(TrueTransactMain.USER_ID);
            objTokenConfigTO.setCreatedDt(ClientUtil.getCurrentDate());
        }
        objTokenConfigTO.setStatusBy(TrueTransactMain.USER_ID);
        objTokenConfigTO.setStatusDt(ClientUtil.getCurrentDate());
        return objTokenConfigTO;
    }
    
    private void setTokenConfigTO(TokenConfigTO objTokenConfigTO){
        setTxtTokenConfigId(objTokenConfigTO.getConfigId());
        setCboTokenType(CommonUtil.convertObjToStr(getCbmTokenType().getDataForKey(objTokenConfigTO.getTokenType())));
        setTxtSeriesNo(objTokenConfigTO.getSeriesNo());
        setTxtStartingTokenNo(CommonUtil.convertObjToStr(objTokenConfigTO.getTokenStartNo()));
        setTxtEndingTokenNo(CommonUtil.convertObjToStr(objTokenConfigTO.getTokenEndNo()));
        setStatusBy(objTokenConfigTO.getStatusBy());
         setAuthorizeStatus(objTokenConfigTO.getAuthorizeStatus());
        notifyObservers();
    }
    
    /** Resets all the UI Fields */
    public void resetForm(){
        setTxtTokenConfigId("");
        setCboTokenType("");
        setTxtSeriesNo("");
        setTxtStartingTokenNo("");
        setTxtEndingTokenNo("");
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
            term.put("TokenConfigTO", getTokenConfigTO(command));
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
            TokenConfigTO objTokenConfigTO =
            (TokenConfigTO) ((List) mapData.get("TokenConfigTO")).get(0);
            setTokenConfigTO(objTokenConfigTO);
        } catch( Exception e ) {
            setResult(ClientConstants.ACTIONTYPE_FAILED);
            parseException.logException(e,true);
            
        }
    }
    
    /*Checks for the duplication of TokenType if so retuns a boolean type vairable as true */
    public boolean isTokenTypeExists(String tokenType){
        boolean exists = false;
        HashMap resultMap = null;
        HashMap where = new HashMap();
        where.put("BRANCH_ID", TrueTransactMain.BRANCH_ID);
        ArrayList resultList =(ArrayList) ClientUtil.executeQuery("getSelectTokenType", where);
        where = null;
        if(resultList.size() > 0){
            for(int i=0; i<resultList.size(); i++){
                resultMap= (HashMap)resultList.get(i);
                if(resultMap.get("TOKEN_TYPE").equals(tokenType)){
                    exists = true;
                    break;
                }
            }
        }
        return exists;
    }
    
    /*Checks for the duplication of SeriesNo if so retuns a boolean type vairable as true */
    public boolean isSeriesNoExists(String tokenType,String seriesNo){
        boolean exists = false;
        HashMap resultMap = null;
        HashMap where = new HashMap();
        where.put("TOKEN_TYPE", tokenType);
        where.put("BRANCH_ID", TrueTransactMain.BRANCH_ID);
        ArrayList resultList =(ArrayList) ClientUtil.executeQuery("getSelectTokenSeries", where);
        where = null;
        if(resultList.size() > 0){
            for(int i=0; i<resultList.size(); i++){
                resultMap= (HashMap)resultList.get(i);
                if(resultMap.get("SERIES_NO").equals(seriesNo)){
                    exists = true;
                    break;
                }
            }
        }
        return exists;
    }
    
}