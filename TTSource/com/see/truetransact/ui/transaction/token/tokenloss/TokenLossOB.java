/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition.
 * 
 *
 * TokenLossOB.java
 *
 * Created on Tue Jan 25 16:57:15 IST 2005
 */

package com.see.truetransact.ui.transaction.token.tokenloss;

import java.util.Observable;
import com.see.truetransact.clientproxy.ProxyFactory;
import com.see.truetransact.clientutil.ComboBoxModel;
import com.see.truetransact.clientutil.ClientUtil;
import com.see.truetransact.clientutil.ClientConstants;
import com.see.truetransact.clientexception.ClientParseException;
import com.see.truetransact.commonutil.CommonConstants;
import com.see.truetransact.commonutil.CommonUtil;
import com.see.truetransact.uicomponent.CObservable;
import com.see.truetransact.ui.TrueTransactMain;
import com.see.truetransact.uicomponent.CInternalFrame;
import com.see.truetransact.commonutil.DateUtil;
import com.see.truetransact.transferobject.transaction.token.tokenloss.TokenLossTO;
import org.apache.log4j.Logger;
import java.util.HashMap;
import java.util.ArrayList;
import java.util.List;
import java.util.Date;
import java.awt.Dimension;
import java.math.BigDecimal;
import com.see.truetransact.clientproxy.ProxyParameters;

/**
 *
 * @author AshokVijayakumar
 */

public class TokenLossOB extends CObservable{
    
    private String cboTokenType = "";
    private String tdtDateOfLoss = "";
    private String txtRemarks = "";
    private String cboSeriesNo = "";
    private String txtTokenNo = "";
    private String txtTokenLostId = "";
    private boolean chkTokenRecovered = false;
    private String tdtRecoveredDate = "";
    private ProxyFactory proxy;
    private HashMap map,lookUpHash,keyValue;
    private ArrayList key,value;
    private ComboBoxModel cbmTokenType,cbmSeriesNo;
    private int _result,_actionType;
    private static TokenLossOB objTokenLossOB;
    private final static Logger log = Logger.getLogger(TokenLossOB.class);//Creating Instace of Log
    private final static ClientParseException parseException = ClientParseException.getInstance();
    private String lblStatus = ClientConstants.ACTION_STATUS[ClientConstants.ACTIONTYPE_CANCEL];
    private String tokenStatus = "";
    Date curDate = null;
    
    
    /** Consturctor Declaration  for  TokenConfigOB */
    private TokenLossOB() {
        try {
            curDate = ClientUtil.getCurrentDate();
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
            objTokenLossOB = new TokenLossOB();
        } catch(Exception e) {
            parseException.logException(e,true);
        }
    }
    
    // Sets the HashMap required to set JNDI,Home and Remote
    private void setOperationMap() throws Exception{
        map = new HashMap();
        map.put(CommonConstants.JNDI, "TokenLossJNDI");
        map.put(CommonConstants.HOME, "transaction.token.tokenloss.TokenLossHome");
        map.put(CommonConstants.REMOTE, "tramsaction.token.tokenloss.TokenLoss");
    }
    
    /** Creating instance for ComboboxModel cbmTokenType and cbmSeriesNo*/
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
            lookUpHash.put(CommonConstants.MAP_NAME,"getSelectTokenIssueSeries");
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
     * Returns an instance of TokenLossOB.
     * @return  TokenLossOB
     */
    
    public static TokenLossOB getInstance()throws Exception{
        return objTokenLossOB;
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
        setChanged();
    }
    
    // Setter method for tdtDateOfLoss
    void setTdtDateOfLoss(String tdtDateOfLoss){
        this.tdtDateOfLoss = tdtDateOfLoss;
        setChanged();
    }
    // Getter method for tdtDateOfLoss
    String getTdtDateOfLoss(){
        return this.tdtDateOfLoss;
    }
    
    // Setter method for txtRemarks
    void setTxtRemarks(String txtRemarks){
        this.txtRemarks = txtRemarks;
        setChanged();
    }
    // Getter method for txtRemarks
    String getTxtRemarks(){
        return this.txtRemarks;
    }
    
    // Setter method for txtRemarks
    void setTxtTokenNo(String txtTokenNo){
        this.txtTokenNo = txtTokenNo;
        setChanged();
    }
    // Getter method for txtRemarks
    String getTxtTokenNo(){
        return this.txtTokenNo;
    }
    
    // Setter method for txtRemarks
    void setTxtTokenLostId(String txtTokenLostId){
        this.txtTokenLostId = txtTokenLostId;
        setChanged();
    }
    // Getter method for txtRemarks
    String getTxtTokenLostId(){
        return this.txtTokenLostId;
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
    
    // Setter method for chkTokenRecovered
    void setChkTokenRecovered(boolean chkTokenRecovered){
        this.chkTokenRecovered = chkTokenRecovered;
        setChanged();
    }
    // Getter method for chkTokenRecovered
    boolean getChkTokenRecovered(){
        return this.chkTokenRecovered;
    }
    
    // Setter method for tdtRecoveredDate
    void setTdtRecoveredDate(String tdtRecoveredDate){
        this.tdtRecoveredDate = tdtRecoveredDate;
        setChanged();
    }
    // Getter method for tdtRecoveredDate
    String getTdtRecoveredDate(){
        return this.tdtRecoveredDate;
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
    
    /** Returns an instance of TokenLossTo object by setting up all its variables **/
    private TokenLossTO getTokenLossTO(String command){
        TokenLossTO objTokenLossTO = new TokenLossTO();
        objTokenLossTO.setCommand(command);
        objTokenLossTO.setTokenLostId(getTxtTokenLostId());
        objTokenLossTO.setTokenType(CommonUtil.convertObjToStr(getCbmTokenType().getKeyForSelected()));
        Date LosDt = DateUtil.getDateMMDDYYYY(getTdtDateOfLoss());
        if(LosDt != null){
        Date losDate = (Date)curDate.clone();
        losDate.setDate(LosDt.getDate());
        losDate.setMonth(LosDt.getMonth());
        losDate.setYear(LosDt.getYear());
        objTokenLossTO.setLostDt(losDate);
        }else{
            objTokenLossTO.setLostDt(DateUtil.getDateMMDDYYYY(getTdtDateOfLoss()));
        }
//        objTokenLossTO.setLostDt(DateUtil.getDateMMDDYYYY(getTdtDateOfLoss()));
        objTokenLossTO.setSeriesNo(getCboSeriesNo());
        objTokenLossTO.setTokenNo(getTxtTokenNo());
        objTokenLossTO.setRemarks(getTxtRemarks());
        objTokenLossTO.setBranchId(TrueTransactMain.BRANCH_ID);
        objTokenLossTO.setStatusBy(TrueTransactMain.USER_ID);
        objTokenLossTO.setStatusDt(curDate);
        objTokenLossTO.setTokenStatus(getTokenStatus());
        return objTokenLossTO;
    }
    
    /** Sets the TokenLossTO variables, to the OB variables thereby setting the same to UI fields **/
    private void setTokenLossTO(TokenLossTO objTokenLossTO){
        setCboTokenType(CommonUtil.convertObjToStr(getCbmTokenType().getDataForKey(objTokenLossTO.getTokenType())));
        setTdtDateOfLoss(DateUtil.getStringDate(objTokenLossTO.getLostDt()));
        setCboSeriesNo(objTokenLossTO.getSeriesNo());
        setTxtTokenNo(objTokenLossTO.getTokenNo());
        setTxtTokenLostId(objTokenLossTO.getTokenLostId());
        setTxtRemarks(objTokenLossTO.getRemarks());
        setStatusBy(objTokenLossTO.getStatusBy());
        setAuthorizeStatus(objTokenLossTO.getAuthorizeStatus());
        notifyObservers();
        
    }
    
    /** Clear up all the Fields in the UI **/
    public void resetForm(){
        setCboTokenType("");
        setTdtDateOfLoss("");
        setCboSeriesNo("");
        setTxtTokenNo("");
        setTxtRemarks("");
        setTxtTokenLostId("");
        setChkTokenRecovered(false);
        setTdtRecoveredDate("");
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
            term.put("TokenLossTO", getTokenLossTO(command));
            HashMap proxyReturnMap = proxy.execute(term, map);
            setProxyReturnMap(proxyReturnMap);
            HashMap where = new HashMap();
            if(getChkTokenRecovered()){       
                where.put("TOKEN_STATUS", CommonConstants.REVOKED);
                Date RecDt = DateUtil.getDateMMDDYYYY(getTdtRecoveredDate());
                if(RecDt != null){
                Date recDate = (Date)curDate.clone();
                recDate.setDate(RecDt.getDate());
                recDate.setMonth(RecDt.getMonth());
                recDate.setYear(RecDt.getYear());
                where.put("RECOVER_DT", recDate);
                }else{
                    where.put("RECOVER_DT", DateUtil.getDateMMDDYYYY(getTdtRecoveredDate()));
                }
//                where.put("RECOVER_DT", DateUtil.getDateMMDDYYYY(getTdtRecoveredDate()));
                where.put("TOKEN_LOST_ID", getTxtTokenLostId());
                where.put("BRANCH_ID", TrueTransactMain.BRANCH_ID);
                where.put("AUTHORIZE_STATUS",  CommonConstants.STATUS_AUTHORIZED);
                where.put("AUTHORIZE_BY", TrueTransactMain.USER_ID);
                ClientUtil.execute("updateTokenStatus", where);
                resetForm();
                where = null;
            }
            setResult(getActionType());
        } catch (Exception e) {
            setResult(ClientConstants.ACTIONTYPE_FAILED);
            parseException.logException(e,true);
        }
    }
    
    /* Populates the TO object by executing a Query */
    public void populateData(CInternalFrame frame, HashMap whereMap){
        HashMap mapData=null;
        TokenLossUI ui = (TokenLossUI)frame;
        try {
            mapData = proxy.executeQuery(whereMap, map);
            TokenLossTO objTokenLossTO =
            (TokenLossTO) ((List) mapData.get("TokenLossTO")).get(0);
            if(objTokenLossTO.getAuthorizeStatus() != null){
                if(objTokenLossTO.getAuthorizeStatus().equals("AUTHORIZED")){
                    ui.setEnableDisable(false);
                    ui.makePanelVisible(true);
                }else{
                    ui.setEnableDisable(true);
                    ui.makePanelVisible(false);
                }
            }
            if(objTokenLossTO.getTokenStatus() != null){
                if(objTokenLossTO.getTokenStatus().equals(CommonConstants.REVOKED)){
                    ui.setEnableDisable(true);
                    ui.makePanelVisible(false);
                }
            }
            setTokenLossTO(objTokenLossTO);
        } catch( Exception e ) {
            setResult(ClientConstants.ACTIONTYPE_FAILED);
            parseException.logException(e,true);
            
        }
    }
    
    public boolean isTokenIssued(String seriesNo, String tokenNo){
        boolean exists = false;
        HashMap where = new HashMap();
        where.put("SERIES_NO", seriesNo);
        Date LsDt = DateUtil.getDateMMDDYYYY(getTdtDateOfLoss());
        if(LsDt != null){
        Date lsDate = (Date)curDate.clone();
        lsDate.setDate(LsDt.getDate());
        lsDate.setMonth(LsDt.getMonth());
        lsDate.setYear(LsDt.getYear());
        where.put("CURRENT_DT", lsDate);
        }else{
            where.put("CURRENT_DT",DateUtil.getDateMMDDYYYY(getTdtDateOfLoss()));
        }
//        where.put("CURRENT_DT",DateUtil.getDateMMDDYYYY(getTdtDateOfLoss()));
        ArrayList list = (ArrayList) ClientUtil.executeQuery("getSelectIssuedTokens", where);
        where = null;
        if(list!=null){
            if(list.size()!=0){
                for(int i=0; i<list.size();i++)
                {
                HashMap resultMap =(HashMap)list.get(i);
                double startNo = ((BigDecimal) resultMap.get("TOKEN_START_NO")).doubleValue();
                double endNo = ((BigDecimal) resultMap.get("TOKEN_END_NO")).doubleValue();
                double temp =0;
                if(endNo<startNo){
                    temp = endNo;
                    endNo = startNo;
                    startNo = temp;
                }
                double number = Double.parseDouble(tokenNo);
                if(number>=startNo && number<=endNo){
                    exists = true;
                    return exists;
                }
            }
        }
        }   
        return exists;
       
    }
    
    /** Checks for the Duplication of TokenNO if any **/
    public  boolean isTokenNoExists(String seriesNo, String tokenNo){
        boolean exists = false;
        HashMap where = new HashMap();
        where.put("SERIES_NO", seriesNo);
        where.put("BRANCH_ID", TrueTransactMain.BRANCH_ID);
        ArrayList list =(ArrayList) ClientUtil.executeQuery("getSelectTokenNumber", where);
        where = null;
        if(list != null){
            if(list.size() != 0){
                for(int i=0; i<list.size(); i++){
                    HashMap resultMap = (HashMap) list.get(i);
                    String dbTokenNo = CommonUtil.convertObjToStr(resultMap.get("TOKEN_NO"));
                    if(dbTokenNo.equals(tokenNo)){
                        exists = true;
                        break;
                    }
                }
            }
        }
        return exists;
    }
    
    /**
     * Getter for property tokenStatus.
     * @return Value of property tokenStatus.
     */
    public java.lang.String getTokenStatus() {
        return tokenStatus;
    }
    
    /**
     * Setter for property tokenStatus.
     * @param tokenStatus New value of property tokenStatus.
     */
    public void setTokenStatus(java.lang.String tokenStatus) {
        this.tokenStatus = tokenStatus;
    }
    
}