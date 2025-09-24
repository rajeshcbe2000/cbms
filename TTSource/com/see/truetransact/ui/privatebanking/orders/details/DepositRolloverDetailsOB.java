/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition. 
 * 
 * DepositRolloverDetailsOB.java
 *
 * Created on July 7, 2004, 3:53 PM
 */

package com.see.truetransact.ui.privatebanking.orders.details;

import com.see.truetransact.transferobject.privatebanking.orders.details.DepositRolloverDetailsTO;
import com.see.truetransact.clientproxy.ProxyFactory;
import com.see.truetransact.commonutil.CommonConstants;
import com.see.truetransact.clientexception.ClientParseException;
import com.see.truetransact.clientutil.ComboBoxModel;
import com.see.truetransact.clientutil.ClientUtil;
import com.see.truetransact.commonutil.CommonUtil;
import com.see.truetransact.clientutil.ClientConstants;
import com.see.truetransact.commonutil.DateUtil;
import com.see.truetransact.commonutil.TTException;
import com.see.truetransact.ui.TrueTransactMain;
import com.see.truetransact.uicomponent.CObservable;

import java.util.Observable;
import java.util.HashMap;
import java.util.ArrayList;
import java.util.List;
import java.util.Date;

/**
 *
 * @author  Lohith R.
 */
public class DepositRolloverDetailsOB extends CObservable {
    
    private static DepositRolloverDetailsOB objDepositRolloverDetailsOB; // singleton object
    private final static ClientParseException parseException = ClientParseException.getInstance();
    private String lblStatus = ClientConstants.ACTION_STATUS[ClientConstants.ACTIONTYPE_CANCEL];
    
    private int actionType;
    private int result;
    private HashMap lookUpHash;
    private HashMap keyValue;
    private ArrayList key;
    private ArrayList value;
    private ProxyFactory proxy;
    private HashMap operationMap;
    
    private String txtEntitlementGroup = "";
    private String txtDepositReferenceNumber = "";
    private String txtPortfolioLocation = "";
    private String txtAssetSubClass = "";
    private String txtAccount = "";
    private String txtPrincipal = "";
    private String txtInterestEarned = "";
    private String txtRollover = "";
    private String txtCSPMemoAvailableBalance = "";
    private String dateStartDate = "";
    private boolean rdoPhoneOrder_Yes = false;
    private boolean rdoPhoneOrder_No = false;
    private String txtTenorDays = "";
    private String txtSpread = "";
    private String dateMaturityDate = "";
    private String txtRolloverAmount = "";
    private String cboTenor = "";
    private String cboRolloverType = "";
    private String cboCurrency = "";
    
    private ComboBoxModel cbmTenor;
    private ComboBoxModel cbmRolloverType;
    private ComboBoxModel cbmCurrency;
    Date curDate = null;
    
    
    static {
        try {
            objDepositRolloverDetailsOB = new DepositRolloverDetailsOB();
        } catch(Exception e) {
            parseException.logException(e,true);
        }
    }
    
    /** Creates a new instance of DepositRolloverDetailsOB */
    public DepositRolloverDetailsOB()throws Exception {
        curDate = ClientUtil.getCurrentDate();
        proxy = ProxyFactory.createProxy();
        setOperationMap();
        fillDropdown();
        
    }
    
    /** Creates a new instance of RemittancePaymentOB */
    public static DepositRolloverDetailsOB getInstance() {
        return objDepositRolloverDetailsOB;
    }
    
    /** Sets the HashMap required to set JNDI,Home and Remote*/
    private void setOperationMap() throws Exception{
        operationMap = new HashMap();
        operationMap.put(CommonConstants.JNDI, "DepositRolloverDetailsJNDI");
        operationMap.put(CommonConstants.HOME, "privatebanking.orders.details.DepositRolloverDetailsHome");
        operationMap.put(CommonConstants.REMOTE, "privatebanking.orders.details.DepositRolloverDetails");
    }
    
    /** A method to set the combo box values */
    private void fillDropdown() throws Exception{
        lookUpHash = new HashMap();
        // The data to be show in Combo Box from LOOKUP_MASTER table
        lookUpHash.put(CommonConstants.MAP_NAME,null);
        final ArrayList lookup_keys = new ArrayList();
        
        lookup_keys.add("PVT_ORD_DET.TENOR");
        lookup_keys.add("PVT_ORD_DET.ROLLOVER_TYPE");
        lookup_keys.add("FOREX.CURRENCY");
        
        lookUpHash.put(CommonConstants.PARAMFORQUERY, lookup_keys);
        keyValue = ClientUtil.populateLookupData(lookUpHash);
        
        getKeyValue((HashMap)keyValue.get("PVT_ORD_DET.TENOR"));
        cbmTenor = new ComboBoxModel(key,value);
        
        getKeyValue((HashMap)keyValue.get("PVT_ORD_DET.ROLLOVER_TYPE"));
        cbmRolloverType = new ComboBoxModel(key,value);
        
        getKeyValue((HashMap)keyValue.get("FOREX.CURRENCY"));
        cbmCurrency = new ComboBoxModel(key,value);
        
        makeNull();
    }
    
    private void getKeyValue(HashMap keyValue)  throws Exception{
        key = (ArrayList)keyValue.get(CommonConstants.KEY);
        value = (ArrayList)keyValue.get(CommonConstants.VALUE);
    }
    
    private void makeNull(){
        key = null;
        value = null;
    }
    
    /** Setter method for txtEntitlementGroup */
    void setTxtEntitlementGroup(String txtEntitlementGroup){
        this.txtEntitlementGroup = txtEntitlementGroup;
        setChanged();
    }
    /** Getter method for txtEntitlementGroup */
    String getTxtEntitlementGroup(){
        return this.txtEntitlementGroup;
    }
    
    /** Setter method for txtDepositReferenceNumber */
    void setTxtDepositReferenceNumber(String txtDepositReferenceNumber){
        this.txtDepositReferenceNumber = txtDepositReferenceNumber;
        setChanged();
    }
    /** Getter method for txtDepositReferenceNumber */
    String getTxtDepositReferenceNumber(){
        return this.txtDepositReferenceNumber;
    }
    
    /** Setter method for txtPortfolioLocation */
    void setTxtPortfolioLocation(String txtPortfolioLocation){
        this.txtPortfolioLocation = txtPortfolioLocation;
        setChanged();
    }
    /** Getter method for txtPortfolioLocation */
    String getTxtPortfolioLocation(){
        return this.txtPortfolioLocation;
    }
    
    /** Setter method for txtAssetSubClass */
    void setTxtAssetSubClass(String txtAssetSubClass){
        this.txtAssetSubClass = txtAssetSubClass;
        setChanged();
    }
    /** Getter method for txtAssetSubClass */
    String getTxtAssetSubClass(){
        return this.txtAssetSubClass;
    }
    
    /** Setter method for txtAccount */
    void setTxtAccount(String txtAccount){
        this.txtAccount = txtAccount;
        setChanged();
    }
    /** Getter method for txtAccount */
    String getTxtAccount(){
        return this.txtAccount;
    }
    
    /** Setter method for txtPrincipal */
    void setTxtPrincipal(String txtPrincipal){
        this.txtPrincipal = txtPrincipal;
        setChanged();
    }
    /** Getter method for txtPrincipal */
    String getTxtPrincipal(){
        return this.txtPrincipal;
    }
    
    /** Setter method for txtInterestEarned */
    void setTxtInterestEarned(String txtInterestEarned){
        this.txtInterestEarned = txtInterestEarned;
        setChanged();
    }
    /** Getter method for txtInterestEarned */
    String getTxtInterestEarned(){
        return this.txtInterestEarned;
    }
    
    /** Setter method for txtRollover */
    void setTxtRollover(String txtRollover){
        this.txtRollover = txtRollover;
        setChanged();
    }
    /** Getter method for txtRollover */
    String getTxtRollover(){
        return this.txtRollover;
    }
    
    /** Setter method for txtCSPMemoAvailableBalance */
    void setTxtCSPMemoAvailableBalance(String txtCSPMemoAvailableBalance){
        this.txtCSPMemoAvailableBalance = txtCSPMemoAvailableBalance;
        setChanged();
    }
    /** Getter method for txtCSPMemoAvailableBalance */
    String getTxtCSPMemoAvailableBalance(){
        return this.txtCSPMemoAvailableBalance;
    }
    
    /** Setter method for dateStartDate */
    void setDateStartDate(String dateStartDate){
        this.dateStartDate = dateStartDate;
        setChanged();
    }
    /** Getter method for dateStartDate */
    String getDateStartDate(){
        return this.dateStartDate;
    }
    
    /** Setter method for rdoPhoneOrder_Yes */
    void setRdoPhoneOrder_Yes(boolean rdoPhoneOrder_Yes){
        this.rdoPhoneOrder_Yes = rdoPhoneOrder_Yes;
        setChanged();
    }
    /** Getter method for rdoPhoneOrder_Yes */
    boolean getRdoPhoneOrder_Yes(){
        return this.rdoPhoneOrder_Yes;
    }
    
    /** Setter method for rdoPhoneOrder_No */
    void setRdoPhoneOrder_No(boolean rdoPhoneOrder_No){
        this.rdoPhoneOrder_No = rdoPhoneOrder_No;
        setChanged();
    }
    /** Getter method for rdoPhoneOrder_No */
    boolean getRdoPhoneOrder_No(){
        return this.rdoPhoneOrder_No;
    }
    
    /** Setter method for cboTenor */
    void setCboTenor(String cboTenor){
        this.cboTenor = cboTenor;
        setChanged();
    }
    /** Getter method for cboTenor */
    String getCboTenor(){
        return this.cboTenor;
    }
    
    /** Setter method for cbmTenor */
    void setCbmTenor(ComboBoxModel cbmTenor){
        this.cbmTenor = cbmTenor;
        setChanged();
    }
    /** Getter method for cbmContactMode */
    ComboBoxModel getCbmTenor(){
        return cbmTenor;
    }
    
    /** Setter method for txtTenorDays */
    void setTxtTenorDays(String txtTenorDays){
        this.txtTenorDays = txtTenorDays;
        setChanged();
    }
    /** Getter method for txtTenorDays */
    String getTxtTenorDays(){
        return this.txtTenorDays;
    }
    
    /** Setter method for cboRolloverType */
    void setCboRolloverType(String cboRolloverType){
        this.cboRolloverType = cboRolloverType;
        setChanged();
    }
    /** Getter method for cboRolloverType */
    String getCboRolloverType(){
        return this.cboRolloverType;
    }
    
    /** Setter method for cbmRolloverType */
    void setCbmRolloverType(ComboBoxModel cbmRolloverType){
        this.cbmRolloverType = cbmRolloverType;
        setChanged();
    }
    /** Getter method for cbmRolloverType */
    ComboBoxModel getCbmRolloverType(){
        return cbmRolloverType;
    }
    
    /** Setter method for cboCurrency */
    void setCboCurrency(String cboCurrency){
        this.cboCurrency = cboCurrency;
        setChanged();
    }
    /** Getter method for cboCurrency */
    String getCboCurrency(){
        return this.cboCurrency;
    }
    
    /** Setter method for cbmCurrency */
    void setCbmCurrency(ComboBoxModel cbmCurrency){
        this.cbmCurrency = cbmCurrency;
        setChanged();
    }
    /** Getter method for cbmCurrency */
    ComboBoxModel getCbmCurrency(){
        return cbmCurrency;
    }
    
    /** Setter method for txtSpread */
    void setTxtSpread(String txtSpread){
        this.txtSpread = txtSpread;
        setChanged();
    }
    /** Getter method for txtSpread */
    String getTxtSpread(){
        return this.txtSpread;
    }
    
    /** Setter method for dateMaturityDate */
    void setDateMaturityDate(String dateMaturityDate){
        this.dateMaturityDate = dateMaturityDate;
        setChanged();
    }
    /** Getter method for dateMaturityDate */
    String getDateMaturityDate(){
        return this.dateMaturityDate;
    }
    
    /** Setter method for txtRolloverAmount */
    void setTxtRolloverAmount(String txtRolloverAmount){
        this.txtRolloverAmount = txtRolloverAmount;
        setChanged();
    }
    /** Getter method for txtRolloverAmount */
    String getTxtRolloverAmount(){
        return this.txtRolloverAmount;
    }
    
    /** Setter for property lblStatus.
     * @param lblStatus New value of property lblStatus.
     *
     */
    public void setLblStatus(String lblStatus) {
        this.lblStatus = lblStatus;
        setChanged();
    }
    public String getLblStatus(){
        return lblStatus;
    }
    
    public void resetStatus(){
        this.setLblStatus(ClientConstants.ACTION_STATUS[0]);
    }
    public void setResultStatus(){
        this.setLblStatus(ClientConstants.RESULT_STATUS[this.getResult()]);
        notifyObservers();
    }
    public void setStatus(){
        this.setLblStatus( ClientConstants.ACTION_STATUS[this.getActionType()]);
        notifyObservers();
    }
    
    public void setResult(int result) {
        this.result = result;
        setChanged();
    }
    public int getResult(){
        return result;
    }
    
    public void setActionType(int action) {
        actionType = action;
        setChanged();
    }
    public int getActionType(){
        return actionType;
    }
    
    /** To perform the appropriate operation */
    public void doAction() {
        try {
            if( actionType != ClientConstants.ACTIONTYPE_CANCEL ){
                if( getCommand() != null ){
                    doActionPerform();
                }
                else{
                    final DepositRolloverDetailsRB objDepositRolloverDetailsRB = new DepositRolloverDetailsRB();
                    throw new TTException(objDepositRolloverDetailsRB.getString("TOCommandError"));
                }
            }
        } catch (Exception e) {
            setResult(ClientConstants.ACTIONTYPE_FAILED);
        }
    }
    
    /** To get the value of action performed */
    private String getCommand() throws Exception{
        String command = null;
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
            default:
        }
        return command;
    }
    
    /** To perform the necessary action */
    private void doActionPerform() throws Exception{
        final DepositRolloverDetailsTO objTO = setDepositRolloverDetailsTOData();
        objTO.setCommand(getCommand());
        final HashMap data = new HashMap();
        data.put(CommonConstants.MODULE, getModule());
        data.put(CommonConstants.SCREEN, getScreen());
        data.put("DepositRolloverDetailsTO",objTO);
        HashMap proxyResultMap = proxy.execute(data, operationMap);
        setResult(actionType);
    }
    
    /** Gets the datas from the Fields and sets to DepositRollover TO */
    public DepositRolloverDetailsTO setDepositRolloverDetailsTOData() {
        final DepositRolloverDetailsTO objTO = new DepositRolloverDetailsTO();
        try{
            objTO.setOrdId(getTxtDepositReferenceNumber());
            objTO.setPortfolioLoc(getTxtPortfolioLocation());
            objTO.setAssetSubClass(getTxtAssetSubClass());
            objTO.setAccount(getTxtAccount());
            objTO.setPrincipal(CommonUtil.convertObjToDouble(getTxtPrincipal()));
            objTO.setRollover(getTxtRollover());
            objTO.setIntEarned(CommonUtil.convertObjToDouble(getTxtInterestEarned()));
            objTO.setCspMemoBalance(CommonUtil.convertObjToDouble(getTxtCSPMemoAvailableBalance()));
            
            Date StrDt = DateUtil.getDateMMDDYYYY(getDateStartDate());
            if(StrDt != null){
            Date strDate = (Date) curDate.clone();
            strDate.setDate(StrDt.getDate());
            strDate.setMonth(StrDt.getMonth());
            strDate.setYear(StrDt.getYear());
//            objTO.setStartDt(DateUtil.getDateMMDDYYYY(getDateStartDate()));
            objTO.setStartDt(strDate);
            }else{
                objTO.setStartDt(DateUtil.getDateMMDDYYYY(getDateStartDate()));
            }
            
            Date MatDt = DateUtil.getDateMMDDYYYY(getDateMaturityDate());
            if(MatDt != null){
            Date matDate = (Date) curDate.clone();
            matDate.setDate(MatDt.getDate());
            matDate.setMonth(MatDt.getMonth());
            matDate.setYear(MatDt.getYear());
//            objTO.setMaturityDate(DateUtil.getDateMMDDYYYY(getDateMaturityDate()));
            objTO.setMaturityDate(matDate);
            }else{
                objTO.setMaturityDate(DateUtil.getDateMMDDYYYY(getDateMaturityDate()));
            }
            
            objTO.setRolloverAmount(CommonUtil.convertObjToDouble(getTxtRolloverAmount()));
            objTO.setTenorDays(CommonUtil.convertObjToDouble(getTxtTenorDays()));
            objTO.setSpread(CommonUtil.convertObjToDouble(getTxtSpread()));
            objTO.setTenorPeriodType(CommonUtil.convertObjToStr(cbmTenor.getKeyForSelected()));
            objTO.setRolloverType(CommonUtil.convertObjToStr(cbmRolloverType.getKeyForSelected()));
            objTO.setCurrency(CommonUtil.convertObjToStr(cbmCurrency.getKeyForSelected()));
            objTO.setStatusBy(TrueTransactMain.USER_ID);
            if(getRdoPhoneOrder_Yes()){
                objTO.setPhoneOrder("Y");
            }else{
                objTO.setPhoneOrder("N");
            }
        }catch(Exception e){
            parseException.logException(e,true);
        }
        return objTO;
    }
    
    /** To populate to the screen */
    public void populateData(HashMap whereMap) {
        HashMap mapData = new HashMap() ;
        try {
            mapData = proxy.executeQuery(whereMap, operationMap);
            populateOB(mapData);
        } catch( Exception e ) {
            parseException.logException(e,true);
        }
    }
    
    private void populateOB(HashMap mapData) {
        DepositRolloverDetailsTO objTO;
        objTO = (DepositRolloverDetailsTO) ((List) mapData.get("DepositRolloverDetailsTO")).get(0);
        getDepositRolloverDetailsTOData(objTO);
        objTO = null;
        notifyObservers();
    }
    
    /** Gets datas from DepositRollover TO and sets to Fields*/
    public void getDepositRolloverDetailsTOData(DepositRolloverDetailsTO objTO){
        setTxtDepositReferenceNumber(objTO.getOrdId());
        setTxtPortfolioLocation(objTO.getPortfolioLoc());
        setTxtAssetSubClass(objTO.getAssetSubClass());
        setTxtAccount(objTO.getAccount());
        setTxtPrincipal(CommonUtil.convertObjToStr(objTO.getPrincipal()));
        setTxtRollover(objTO.getRollover());
        setTxtInterestEarned(CommonUtil.convertObjToStr(objTO.getIntEarned()));
        setTxtCSPMemoAvailableBalance(CommonUtil.convertObjToStr(objTO.getCspMemoBalance()));
        setDateStartDate(DateUtil.getStringDate(objTO.getStartDt()));
        setDateMaturityDate(DateUtil.getStringDate(objTO.getMaturityDate()));
        setTxtRolloverAmount(CommonUtil.convertObjToStr(objTO.getRolloverAmount()));
        setTxtTenorDays(CommonUtil.convertObjToStr(objTO.getTenorDays()));
        setTxtSpread(CommonUtil.convertObjToStr(objTO.getSpread()));
        setCboTenor(CommonUtil.convertObjToStr(getCbmTenor().getDataForKey(objTO.getTenorPeriodType())));
        setCboRolloverType(CommonUtil.convertObjToStr(getCbmRolloverType().getDataForKey(objTO.getRolloverType())));
        setCboCurrency(CommonUtil.convertObjToStr(getCbmCurrency().getDataForKey(objTO.getCurrency())));
        if(objTO.getPhoneOrder().equals("Y")){
            setRdoPhoneOrder_Yes(true);
        }else{
            setRdoPhoneOrder_No(true);
        }
    }
    
    /** Resets all the Fields to Null  */
    public void resetFields(){
        setTxtEntitlementGroup("");
        setTxtDepositReferenceNumber("");
        setTxtPortfolioLocation("");
        setTxtAssetSubClass("");
        setTxtAccount("");
        setTxtPrincipal("");
        setTxtRollover("");
        setTxtInterestEarned("");
        setTxtCSPMemoAvailableBalance("");
        setDateStartDate("");
        setDateMaturityDate("");
        setTxtRolloverAmount("");
        setTxtTenorDays("");
        setTxtSpread("");
        setCboTenor("");
        setCboRolloverType("");
        setCboCurrency("");
        setRdoPhoneOrder_Yes(false);
        setRdoPhoneOrder_No(false);
        notifyObservers();
    }
}
