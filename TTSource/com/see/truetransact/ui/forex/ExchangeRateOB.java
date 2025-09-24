/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition. 
 * 
 * ExchangeRateOB.java
 *
 * Created on January 12, 2004, 3:11 PM
 */

package com.see.truetransact.ui.forex;

import java.util.Observable;
import com.see.truetransact.clientproxy.ProxyFactory;
import com.see.truetransact.clientutil.ComboBoxModel;
import com.see.truetransact.commonutil.CommonConstants;
import com.see.truetransact.clientutil.ClientUtil;
import com.see.truetransact.clientutil.ClientConstants;
import com.see.truetransact.commonutil.DateUtil;
import com.see.truetransact.commonutil.CommonUtil;
import com.see.truetransact.commonutil.TTException;
import com.see.truetransact.clientexception.ClientParseException;
import com.see.truetransact.clientutil.EnhancedTableModel;

import com.see.truetransact.transferobject.forex.ExchangeRateTO;

import java.util.HashMap;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
/**
 *
 * @author  amathan
 */
public class ExchangeRateOB extends Observable {
    private String txtExchangeId = "";
    private String tdtValueDate = "";
    private String cboTransCurrency = "";
    private String cboConversionCurrency = "";
    private String cboCustomerType = "";
    private boolean chkPreferred = false;
    private String txtCommission = "";
    private String txtRemarks = "";
    private String txtMiddleRate = "";
//    private String cboBuyingRate = "";
//    private String cboSellingRate = "";
    private String txtBuyingValue = "";
    private String txtSellingValue = "";
    
    private String txtCurrBuying = "";
    private String txtCurrSelling = "";
    private String cboBuySellType = "";
    private String txtNotionalRate = "";
    private String txtTTBuying = "";
    private String txtTTSelling = "";
    private String txtBillBuying = "";
    private String txtBillSelling = "";
    private String txtDDBuying = "";
    private String txtDDSelling = "";
    private String txtTCBuying = "";
    private String txtTCSelling = "";
    private String txtTTBuyingSlab = "";
    private String txtTTBuyingComm = "";
    private String txtTCBuyingComm = "";
    private String txtTCBuyingSlab = "";
    private String txtBillBuyingSlab = "";
    private String txtBillBuyingComm = "";
    private String txtDDBuyingSlab = "";
    private String txtDDBuyingComm = "";
    private String txtCurrBuyingSlab = "";
    private String txtCurrBuyingComm = "";
    
//   private EnhancedTableModel tblCurrency;
    private final String AMOUNT = "Amount";
    private final String PERCENTAGE = "Percentage";
    private HashMap lookUpHash;
    private HashMap keyValue;
    private ArrayList key;
    private ArrayList value;
    
    private ComboBoxModel cbmTransCurrency;
    private ComboBoxModel cbmConversionCurrency;
    private ComboBoxModel cbmCustomerType;
//    private ComboBoxModel cbmBuyingRate;
//    private ComboBoxModel cbmSellingRate;
    private EnhancedTableModel tblCurrency;
    
    private ComboBoxModel cbmBuySellType;
    
    private int actionType;
    private int result;
    
    private String lblStatus = ClientConstants.ACTION_STATUS[ClientConstants.ACTIONTYPE_CANCEL];
    
    private HashMap operationMap;
    private ProxyFactory proxy;
    private static ExchangeRateOB objExchangeRateOB; // singleton object    
    
    private final static Logger log = Logger.getLogger(ExchangeRateOB.class);  
    private final static ClientParseException parseException = ClientParseException.getInstance();
        
    static {
        try {
            log.info("Creating ExchangeRateOB...");
            objExchangeRateOB = new ExchangeRateOB();
        } catch(Exception e) {
            parseException.logException(e,true);
        }
    }
    
    /** Creates a new instance of ExchangeRateOB */
    private ExchangeRateOB() throws Exception{        
        proxy = ProxyFactory.createProxy();
        setOperationMap();
        fillDropdown();
    }
    
    
    /* Sets the HashMap required to set JNDI,Home and Remote*/
    private void setOperationMap() throws Exception{      
        operationMap = new HashMap();
        operationMap.put(CommonConstants.JNDI, "ExchangeRateJNDI");
        operationMap.put(CommonConstants.HOME, "forex.ExchangeRateHome");
        operationMap.put(CommonConstants.REMOTE, "forex.ExchangeRate");
    }
    
    public void setCurrencyTable(){
        final ArrayList colHeader = new ArrayList();
        final ArrayList allData = new ArrayList();
        ArrayList oneCol;
        final List headList = ClientUtil.executeQuery("forex.getTblTitle",null);
        colHeader.add(((HashMap)headList.get(0)).get("TITLE"));
        tblCurrency = new EnhancedTableModel(null,colHeader);
        final List transCurrData = ClientUtil.executeQuery("getTblCurrencyData",null);
        final int dataSize = transCurrData.size();
        for (int i=0;i<dataSize;i++){
            oneCol = new ArrayList();
            oneCol.add(((HashMap)transCurrData.get(i)).get("TRANS_CURR"));
            allData.add(oneCol);
        }        
        tblCurrency.setDataArrayList(allData,colHeader);
        setTblCurrency(tblCurrency);
        ttNotifyObservers();
        oneCol = null;
    }
        
    public void fillDropdown() throws Exception{       
        
        lookUpHash = new HashMap();
        lookUpHash.put(CommonConstants.MAP_NAME,null);
        final ArrayList lookup_keys = new ArrayList();
        
        lookup_keys.add("FOREX.CURRENCY");        
        lookup_keys.add("CUSTOMER.CUSTOMERTYPE");      
        lookup_keys.add("FOREX.RATE_TYPE");
        
        lookUpHash.put(CommonConstants.PARAMFORQUERY, lookup_keys);
        
        keyValue = ClientUtil.populateLookupData(lookUpHash);
        getKeyValue((HashMap)keyValue.get("FOREX.CURRENCY"));
        cbmTransCurrency = new ComboBoxModel(key,value);
        cbmConversionCurrency = new ComboBoxModel(key,value);
        
//        keyValue = ClientUtil.populateLookupData(lookUpHash);
        getKeyValue((HashMap)keyValue.get("CUSTOMER.CUSTOMERTYPE"));
        cbmCustomerType = new ComboBoxModel(key,value);
        
//        keyValue = ClientUtil.populateLookupData(lookUpHash);
//        getKeyValue((HashMap)keyValue.get("FOREX.RATE_TYPE"));
//        cbmBuyingRate = new ComboBoxModel(key,value);
//        cbmSellingRate = new ComboBoxModel(key,value);  
     }
    
    private void getKeyValue(HashMap keyValue)  throws Exception{
        key = (ArrayList)keyValue.get(CommonConstants.KEY);
        value = (ArrayList)keyValue.get(CommonConstants.VALUE);
    }
    
    public void resetForm(){
        //setTxtExchangeId ("");
        setTdtValueDate ("");
        setCboTransCurrency ("");
        setCboConversionCurrency ("");
        setCboCustomerType ("");
        setTxtMiddleRate ("");
//        setCboSellingRate ("");
        setTxtSellingValue ("");
//        setCboBuyingRate ("");
        setTxtBuyingValue ("");
        setChkPreferred (false);
        setTxtCommission ("");
        setTxtRemarks ("");
        
        setTxtTTBuying("");
        setTxtTTBuyingComm("");
        setTxtTTBuyingSlab("");
        setTxtTTSelling("");
        setTxtBillBuying("");
        setTxtBillBuyingComm("");
        setTxtBillBuyingSlab("");
        setTxtBillSelling("");
        setTxtDDBuying("");
        setTxtDDBuyingComm("");
        setTxtDDBuyingSlab("");
        setTxtDDSelling("");
        setTxtCurrBuying("");
        setTxtCurrBuyingComm("");
        setTxtCurrBuyingSlab("");
        setTxtCurrSelling("");
        setTxtTCBuying("");
        setTxtTCBuyingComm("");
        setTxtTCBuyingSlab("");        
        setTxtTCSelling("");
        setTxtNotionalRate("");
        ttNotifyObservers();
    }
    
    public void resetStatus(){
        this.setLblStatus(ClientConstants.ACTION_STATUS[0]);
    }
    
    /** To set the status based on ActionType, either New, Edit, etc., */
    public void setStatus(){
        this.setLblStatus( ClientConstants.ACTION_STATUS[this.getActionType()]);
        ttNotifyObservers();
    }
    
    /** To update the Status based on result performed by doAction() method */
    public void setResultStatus(){
        this.setLblStatus(ClientConstants.RESULT_STATUS[this.getResult()]);
        ttNotifyObservers();
    }
    
    /** To perform the appropriate operation */
    public void doAction() {
        try {
            log.info("Inside doAction()");
            //If actionType such as NEW, EDIT, DELETE, then proceed
            if( actionType != ClientConstants.ACTIONTYPE_CANCEL ){
                //If actionType has got propervalue then doActionPerform, else throw error
                if( getCommand() != null ){
                    doActionPerform();
                }
                else{
                    final ExchangeRateRB objExchangeRateRB = new ExchangeRateRB();
                    throw new TTException(objExchangeRateRB.getString("TOCommandError"));
                }
            }
        } catch (Exception e) {
            setResult(ClientConstants.ACTIONTYPE_FAILED);
            parseException.logException(e,true);
        }
    }
    
    /** To perform the necessary action */
    private void doActionPerform() throws Exception{
        final ExchangeRateTO objExchangeRateTO = setExchangeRateData();
        objExchangeRateTO.setCommand(getCommand());
        final HashMap data = new HashMap();
        data.put("EXCHANGE_RATE",objExchangeRateTO);
        log.info("objExchangeRateTO:"+objExchangeRateTO);
        HashMap proxyResultMap = proxy.execute(data, operationMap);
        setResult(actionType);
        actionType = ClientConstants.ACTIONTYPE_CANCEL;
        resetForm();        
    }
    
    private ExchangeRateTO setExchangeRateData(){
        
        ExchangeRateTO objExchangeRateTO = new ExchangeRateTO();
        objExchangeRateTO.setExchangeId (getTxtExchangeId());
//        objExchangeRateTO.setValueDate (DateUtil.getDateMMDDYYYY (getTdtValueDate()));
        objExchangeRateTO.setTransCurrency ((String)getCbmTransCurrency().getKeyForSelected());
        objExchangeRateTO.setConversionCurrency ((String)getCbmConversionCurrency().getKeyForSelected());
        objExchangeRateTO.setCustomerType ((String)getCbmCustomerType().getKeyForSelected());
//        objExchangeRateTO.setMiddleRate (CommonUtil.convertObjToDouble(getTxtMiddleRate()));
//        if (getCboSellingRate().equals(AMOUNT)){
//            objExchangeRateTO.setSellingPrice (CommonUtil.convertObjToDouble(getTxtSellingValue()));
//        }else{
//            objExchangeRateTO.setSellingPer (CommonUtil.convertObjToDouble(getTxtSellingValue()));
//        }
        
//        if (getCboBuyingRate().equals(AMOUNT)){
//            objExchangeRateTO.setBuyingPrice (CommonUtil.convertObjToDouble(getTxtBuyingValue()));
//        }else{
//            objExchangeRateTO.setBuyingPer (CommonUtil.convertObjToDouble(getTxtBuyingValue()));
//        }
        
        if (getChkPreferred() == true) {
            objExchangeRateTO.setPreferred ("Y");
        }else{
            objExchangeRateTO.setPreferred ("N");
        }
        
//        objExchangeRateTO.setCommission (CommonUtil.convertObjToDouble(getTxtCommission()));
        objExchangeRateTO.setRemarks (getTxtRemarks());
        
        objExchangeRateTO.setTtBuying(CommonUtil.convertObjToDouble(getTxtTTBuying()));
        objExchangeRateTO.setTtSelling(CommonUtil.convertObjToDouble(getTxtTTSelling()));
        objExchangeRateTO.setBillBuying(CommonUtil.convertObjToDouble(getTxtBillBuying()));
        objExchangeRateTO.setBillSelling(CommonUtil.convertObjToDouble(getTxtBillSelling()));
        objExchangeRateTO.setDdBuying(CommonUtil.convertObjToDouble(getTxtDDBuying()));
        objExchangeRateTO.setDdSelling(CommonUtil.convertObjToDouble(getTxtDDSelling()));
        objExchangeRateTO.setSellingPrice(CommonUtil.convertObjToDouble(getTxtCurrSelling()));
        objExchangeRateTO.setBuyingPrice(CommonUtil.convertObjToDouble(getTxtCurrBuying()));
        objExchangeRateTO.setTcBuying(CommonUtil.convertObjToDouble(getTxtTCBuying()));
        objExchangeRateTO.setTcSelling(CommonUtil.convertObjToDouble(getTxtTCSelling()));
        
        objExchangeRateTO.setCommTtSlab(CommonUtil.convertObjToDouble(getTxtTTBuyingSlab()));
        objExchangeRateTO.setCommTtPer(CommonUtil.convertObjToDouble(getTxtTTBuyingComm()));
        objExchangeRateTO.setCommBillSlab(CommonUtil.convertObjToDouble(getTxtBillBuyingSlab()));
        objExchangeRateTO.setCommBillPer(CommonUtil.convertObjToDouble(getTxtBillBuyingComm()));
        objExchangeRateTO.setCommDdSlab(CommonUtil.convertObjToDouble(getTxtDDBuyingSlab()));
        objExchangeRateTO.setCommDdPer(CommonUtil.convertObjToDouble(getTxtDDBuyingComm()));
        objExchangeRateTO.setCommission(CommonUtil.convertObjToDouble(getTxtCurrBuyingSlab()));
        objExchangeRateTO.setCommCurrPer(CommonUtil.convertObjToDouble(getTxtCurrBuyingComm()));
        objExchangeRateTO.setCommTcSlab(CommonUtil.convertObjToDouble(getTxtTCBuyingSlab()));
        objExchangeRateTO.setCommTcPer(CommonUtil.convertObjToDouble(getTxtTCBuyingComm()));
        
        objExchangeRateTO.setNotionalRate(CommonUtil.convertObjToDouble(getTxtNotionalRate()));        
        
        return objExchangeRateTO;
    }
    
    /* To populate the screen */
    public void populateData(HashMap whereMap) {
        try {
            final HashMap mapData = proxy.executeQuery(whereMap, operationMap);
            log.info("mapData:"+mapData);
            populateOB(mapData);
        } catch( Exception e ) {
            parseException.logException(e,true);
            //e.printStackTrace();
        }
    }
    
    
    private void populateOB(HashMap mapData) throws Exception{
        final ExchangeRateTO objExchangeRateTO = (ExchangeRateTO) ((List) mapData.get("ExchangeRateDetails")).get(0);
        log.info("exchangeRateMap:"+objExchangeRateTO);
        setTxtExchangeId (objExchangeRateTO.getExchangeId ());
//        setTdtValueDate (DateUtil.getStringDate (objExchangeRateTO.getValueDate ()));        
        setCboTransCurrency(CommonUtil.convertObjToStr(getCbmTransCurrency().getDataForKey(objExchangeRateTO.getTransCurrency())));    
//        setCboConversionCurrency(CommonUtil.convertObjToStr(getCbmConversionCurrency().getDataForKey(objExchangeRateTO.getConversionCurrency())));
        setCboCustomerType (objExchangeRateTO.getCustomerType ());
        setCboCustomerType(CommonUtil.convertObjToStr(getCbmCustomerType().getDataForKey(objExchangeRateTO.getCustomerType())));
        setTxtMiddleRate (CommonUtil.convertObjToStr(objExchangeRateTO.getMiddleRate ()));
        
        if (objExchangeRateTO.getSellingPrice () == null){
            setTxtSellingValue (CommonUtil.convertObjToStr(objExchangeRateTO.getSellingPer ()));
//            setCboSellingRate(PERCENTAGE);
        }else if (objExchangeRateTO.getSellingPer () == null){
            setTxtSellingValue (CommonUtil.convertObjToStr(objExchangeRateTO.getSellingPrice ()));
//            setCboSellingRate(AMOUNT);
        }        
            
        if (objExchangeRateTO.getBuyingPrice () == null){
            setTxtBuyingValue (CommonUtil.convertObjToStr(objExchangeRateTO.getBuyingPer ()));
//            setCboBuyingRate(PERCENTAGE);
        }else if (objExchangeRateTO.getBuyingPer () == null){
            setTxtBuyingValue (CommonUtil.convertObjToStr(objExchangeRateTO.getBuyingPrice ()));
//            setCboBuyingRate(AMOUNT);
        }        
        if ((objExchangeRateTO.getPreferred ()).equals("Y")){
            setChkPreferred (true);
        }else{
            setChkPreferred (false);
        }        
//        setTxtCommission (CommonUtil.convertObjToStr(objExchangeRateTO.getCommission ()));
        setTxtRemarks (objExchangeRateTO.getRemarks ()); 
        
        setTxtBillBuying (CommonUtil.convertObjToStr(objExchangeRateTO.getBillBuying()));
        setTxtBillSelling (CommonUtil.convertObjToStr(objExchangeRateTO.getBillSelling ()));
        setTxtDDBuying (CommonUtil.convertObjToStr(objExchangeRateTO.getDdBuying ()));
        setTxtDDSelling (CommonUtil.convertObjToStr(objExchangeRateTO.getDdSelling ()));
        setTxtTCBuying (CommonUtil.convertObjToStr(objExchangeRateTO.getTcBuying ()));
        setTxtTCSelling (CommonUtil.convertObjToStr(objExchangeRateTO.getTcSelling ()));
        setTxtNotionalRate (CommonUtil.convertObjToStr(objExchangeRateTO.getNotionalRate ()));
        setTxtTTBuying (CommonUtil.convertObjToStr(objExchangeRateTO.getTtBuying ()));
        setTxtTTSelling (CommonUtil.convertObjToStr(objExchangeRateTO.getTtSelling ()));
        setTxtBillBuyingSlab(CommonUtil.convertObjToStr(objExchangeRateTO.getCommBillSlab ()));
        setTxtBillBuyingComm(CommonUtil.convertObjToStr(objExchangeRateTO.getCommBillPer ()));
        setTxtDDBuyingSlab(CommonUtil.convertObjToStr(objExchangeRateTO.getCommDdSlab ()));
        setTxtDDBuyingComm(CommonUtil.convertObjToStr(objExchangeRateTO.getCommDdPer ()));
        setTxtTCBuyingSlab(CommonUtil.convertObjToStr(objExchangeRateTO.getCommTcSlab ()));
        setTxtTCBuyingComm(CommonUtil.convertObjToStr(objExchangeRateTO.getCommTcPer ()));
        setTxtTTBuyingSlab(CommonUtil.convertObjToStr(objExchangeRateTO.getCommTtSlab ()));
        setTxtTTBuyingComm(CommonUtil.convertObjToStr(objExchangeRateTO.getCommTtPer ()));        
        setTxtCurrBuying (CommonUtil.convertObjToStr(objExchangeRateTO.getBuyingPrice()));
        setTxtCurrSelling (CommonUtil.convertObjToStr(objExchangeRateTO.getSellingPrice()));        
        setTxtCurrBuyingSlab(CommonUtil.convertObjToStr(objExchangeRateTO.getCommission()));
        setTxtCurrBuyingComm(CommonUtil.convertObjToStr(objExchangeRateTO.getCommCurrPer()));
        
        ttNotifyObservers();
    }
    
    /*public void populateCurrency(int row){        
        ArrayList selectedCurrency = (ArrayList)tblCurrency.getDataArrayList().get(row);
        setCboTransCurrency((String)selectedCurrency.get(0));
        setCboConversionCurrency((String)selectedCurrency.get(1));
        ttNotifyObservers();
        
    }*/
    
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
    
    
    /**
     * Returns an instance of ExchangeRateOB.
     * @return  ExchangeRateOB
     */
    public static ExchangeRateOB getInstance() {
        return objExchangeRateOB;
    }
    
    public int getActionType(){
        return actionType;
    }
    
    public void setActionType(int actionType) {
        this.actionType = actionType;
        setChanged();
    }
    
    public int getResult(){
        return this.result;
    }
    
    public void setResult(int result) {
        this.result = result;
        setChanged();
    }
    
    public String getLblStatus(){
        return this.lblStatus;
    }
    
    public void setLblStatus(String lblStatus) {
        this.lblStatus = lblStatus;
        setChanged();
    }
    
    

   void setCbmTransCurrency(ComboBoxModel cbmTransCurrency){
        this.cbmTransCurrency = cbmTransCurrency;
        setChanged();
   }

    ComboBoxModel getCbmTransCurrency(){
        return cbmTransCurrency;
    }
    
   void setCbmConversionCurrency(ComboBoxModel cbmConversionCurrency){
        this.cbmConversionCurrency = cbmConversionCurrency;
        setChanged();
   }

    ComboBoxModel getCbmConversionCurrency(){
        return cbmConversionCurrency;
    }
    
   void setCbmCustomerType(ComboBoxModel cbmCustomerType){
        this.cbmCustomerType = cbmCustomerType;
        setChanged();
   }

    ComboBoxModel getCbmCustomerType(){
        return cbmCustomerType;
    }
    
    void setTxtExchangeId(String txtExchangeId){
        this.txtExchangeId = txtExchangeId;
        setChanged();
    }
    String getTxtExchangeId(){
        return this.txtExchangeId;
    }
    
    void setTdtValueDate(String tdtValueDate){
        this.tdtValueDate = tdtValueDate;
        setChanged();
    }
    String getTdtValueDate(){
        return this.tdtValueDate;
    }
    
    void setCboTransCurrency(String cboTransCurrency){
        this.cboTransCurrency = cboTransCurrency;
        setChanged();
    }
    String getCboTransCurrency(){
        return this.cboTransCurrency;
    }
    
    void setCboConversionCurrency(String cboConversionCurrency){
        this.cboConversionCurrency = cboConversionCurrency;
        setChanged();
    }
    String getCboConversionCurrency(){
        return this.cboConversionCurrency;
    }
    
    void setCboCustomerType(String cboCustomerType){
        this.cboCustomerType = cboCustomerType;
        setChanged();
    }
    String getCboCustomerType(){
        return this.cboCustomerType;
    }
    
    void setChkPreferred(boolean chkPreferred){
        this.chkPreferred = chkPreferred;
        setChanged();
    }
    boolean getChkPreferred(){
        return this.chkPreferred;
    }
    
    void setTxtCommission(String txtCommission){
        this.txtCommission = txtCommission;
        setChanged();
    }
    String getTxtCommission(){
        return this.txtCommission;
    }
    
    void setTxtRemarks(String txtRemarks){
        this.txtRemarks = txtRemarks;
        setChanged();
    }
    String getTxtRemarks(){
        return this.txtRemarks;
    }
    
    void setTxtMiddleRate(String txtMiddleRate){
        this.txtMiddleRate = txtMiddleRate;
        setChanged();
    }
    String getTxtMiddleRate(){
        return this.txtMiddleRate;    
    }
    
//    void setCboBuyingRate(String cboBuyingRate){
//        this.cboBuyingRate = cboBuyingRate;
//        setChanged();
//    }
//    String getCboBuyingRate(){
//        return this.cboBuyingRate;
//    }
//    
//    void setCboSellingRate(String cboSellingRate){
//        this.cboSellingRate = cboSellingRate;
//        setChanged();
//    }
//    String getCboSellingRate(){
//        return this.cboSellingRate;
//    }
    
    void setTxtBuyingValue(String txtBuyingValue){
        this.txtBuyingValue = txtBuyingValue;
        setChanged();
    }
    String getTxtBuyingValue(){
        return this.txtBuyingValue;
    }
    
    void setTxtSellingValue(String txtSellingValue){
        this.txtSellingValue = txtSellingValue;
        setChanged();
    }
    String getTxtSellingValue(){
        return this.txtSellingValue;
    }
    
    
    
    void setTxtCurrBuying(String txtCurrBuying){
        this.txtCurrBuying = txtCurrBuying;
        setChanged();
    }
    String getTxtCurrBuying(){
        return this.txtCurrBuying;
    }
    
    void setTxtCurrSelling(String txtCurrSelling){
        this.txtCurrSelling = txtCurrSelling;
        setChanged();
    }
    String getTxtCurrSelling(){
        return this.txtCurrSelling;
    }
    
    void setCboBuySellType(String cboBuySellType){
        this.cboBuySellType = cboBuySellType;
        setChanged();
    }
    String getCboBuySellType(){
        return this.cboBuySellType;
    }
    
    void setCbmBuySellType(ComboBoxModel cbmBuySellType){
        this.cbmBuySellType = cbmBuySellType;
        setChanged();
    }
    ComboBoxModel getCbmBuySellType(){
        return this.cbmBuySellType;
    }
    
    void setTxtNotionalRate(String txtNotionalRate){
        this.txtNotionalRate = txtNotionalRate;
        setChanged();
    }
    String getTxtNotionalRate(){
        return this.txtNotionalRate;
    }
    
    void setTxtTTBuying(String txtTTBuying){
        this.txtTTBuying = txtTTBuying;
        setChanged();
    }
    String getTxtTTBuying(){
        return this.txtTTBuying;
    }
    
    void setTxtTTSelling(String txtTTSelling){
        this.txtTTSelling = txtTTSelling;
        setChanged();
    }
    String getTxtTTSelling(){
        return this.txtTTSelling;
    }
    
    void setTxtBillBuying(String txtBillBuying){
        this.txtBillBuying = txtBillBuying;
        setChanged();
    }
    String getTxtBillBuying(){
        return this.txtBillBuying;
    }
    
    void setTxtBillSelling(String txtBillSelling){
        this.txtBillSelling = txtBillSelling;
        setChanged();
    }
    String getTxtBillSelling(){
        return this.txtBillSelling;
    }
    
    void setTxtDDBuying(String txtDDBuying){
        this.txtDDBuying = txtDDBuying;
        setChanged();
    }
    String getTxtDDBuying(){
        return this.txtDDBuying;
    }
    
    void setTxtDDSelling(String txtDDSelling){
        this.txtDDSelling = txtDDSelling;
        setChanged();
    }
    String getTxtDDSelling(){
        return this.txtDDSelling;
    }
    
    void setTxtTCBuying(String txtTCBuying){
        this.txtTCBuying = txtTCBuying;
        setChanged();
    }
    String getTxtTCBuying(){
        return this.txtTCBuying;
    }
    
    void setTxtTCSelling(String txtTCSelling){
        this.txtTCSelling = txtTCSelling;
        setChanged();
    }
    String getTxtTCSelling(){
        return this.txtTCSelling;
    }
    
    void setTxtTTBuyingSlab(String txtTTBuyingSlab){
        this.txtTTBuyingSlab = txtTTBuyingSlab;
        setChanged();
    }
    String getTxtTTBuyingSlab(){
        return this.txtTTBuyingSlab;
    }
    
    void setTxtTTBuyingComm(String txtTTBuyingComm){
        this.txtTTBuyingComm = txtTTBuyingComm;
        setChanged();
    }
    String getTxtTTBuyingComm(){
        return this.txtTTBuyingComm;
    }
    
    void setTxtTCBuyingComm(String txtTCBuyingComm){
        this.txtTCBuyingComm = txtTCBuyingComm;
        setChanged();
    }
    String getTxtTCBuyingComm(){
        return this.txtTCBuyingComm;
    }
    
    void setTxtTCBuyingSlab(String txtTCBuyingSlab){
        this.txtTCBuyingSlab = txtTCBuyingSlab;
        setChanged();
    }
    String getTxtTCBuyingSlab(){
        return this.txtTCBuyingSlab;
    }
    
    void setTxtBillBuyingSlab(String txtBillBuyingSlab){
        this.txtBillBuyingSlab = txtBillBuyingSlab;
        setChanged();
    }
    String getTxtBillBuyingSlab(){
        return this.txtBillBuyingSlab;
    }   
    
    void setTxtBillBuyingComm(String txtBillBuyingComm){
        this.txtBillBuyingComm = txtBillBuyingComm;
        setChanged();
    }
    String getTxtBillBuyingComm(){
        return this.txtBillBuyingComm;
    }  
    
    void setTxtDDBuyingSlab(String txtDDBuyingSlab){
        this.txtDDBuyingSlab = txtDDBuyingSlab;
        setChanged();
    }
    String getTxtDDBuyingSlab(){
        return this.txtDDBuyingSlab;
    }  	
    
    void setTxtDDBuyingComm(String txtDDBuyingComm){
        this.txtDDBuyingComm = txtDDBuyingComm;
        setChanged();
    }
    String getTxtDDBuyingComm(){
        return this.txtDDBuyingComm;
    }  
    
    void setTxtCurrBuyingSlab(String txtCurrBuyingSlab){
        this.txtCurrBuyingSlab = txtCurrBuyingSlab;
        setChanged();
    }
    String getTxtCurrBuyingSlab(){
        return this.txtCurrBuyingSlab;
    }  
    
    void setTxtCurrBuyingComm(String txtCurrBuyingComm){
        this.txtCurrBuyingComm = txtCurrBuyingComm;
        setChanged();
    }
    String getTxtCurrBuyingComm(){
        return this.txtCurrBuyingComm;
    }      
    
    void setTblCurrency(EnhancedTableModel tblCurrency){
        this.tblCurrency = tblCurrency;
        setChanged();
    }
    
    EnhancedTableModel getTblCurrency(){
        return this.tblCurrency;
    }
    
    public void ttNotifyObservers(){
        notifyObservers();
    }
    
        /*
ExchangeRateTO objExchangeRateTO = new ExchangeRateTO();
objExchangeRateTO.setCommand (command);
objExchangeRateTO.setExchangeId (getTxtExchangeId());
objExchangeRateTO.setValueDate (DateUtil.getDateMMDDYYYY (getTxtValueDate()));
objExchangeRateTO.setTransCurrency (getTxtTransCurrency());
objExchangeRateTO.setConversionCurrency (getTxtConversionCurrency());
objExchangeRateTO.setCustomerType (getTxtCustomerType());
objExchangeRateTO.setMiddleRate (new Double (getTxtMiddleRate()));
objExchangeRateTO.setSellingPer (new Double (getTxtSellingPer()));
objExchangeRateTO.setSellingPrice (new Double (getTxtSellingPrice()));
objExchangeRateTO.setBuyingPer (new Double (getTxtBuyingPer()));
objExchangeRateTO.setBuyingPrice (new Double (getTxtBuyingPrice()));
objExchangeRateTO.setPreferred (getTxtPreferred());
objExchangeRateTO.setCommission (new Double (getTxtCommission()));
objExchangeRateTO.setRemarks (getTxtRemarks());
objExchangeRateTO.setCreatedBy (getTxtCreatedBy());
objExchangeRateTO.setCreatedDt (DateUtil.getDateMMDDYYYY (getTxtCreatedDt()));
objExchangeRateTO.setAuthorizedBy (getTxtAuthorizedBy());
objExchangeRateTO.setAuthorizedDt (DateUtil.getDateMMDDYYYY (getTxtAuthorizedDt()));
objExchangeRateTO.setStatus (getTxtStatus());
objExchangeRateTO.setAuthorizeStatus (getTxtAuthorizeStatus());
objExchangeRateTO.setRateType (getTxtRateType());
objExchangeRateTO.setBillBuying (new Double (getTxtBillBuying()));
objExchangeRateTO.setBillSelling (new Double (getTxtBillSelling()));
objExchangeRateTO.setDdBuying (new Double (getTxtDdBuying()));
objExchangeRateTO.setDdSelling (new Double (getTxtDdSelling()));
objExchangeRateTO.setTcBuying (new Double (getTxtTcBuying()));
objExchangeRateTO.setTcSelling (new Double (getTxtTcSelling()));
objExchangeRateTO.setNotionalRate (new Double (getTxtNotionalRate()));
objExchangeRateTO.setTtBuying (new Double (getTxtTtBuying()));
objExchangeRateTO.setTtSelling (new Double (getTxtTtSelling()));
objExchangeRateTO.setCommBillSlab (new Double (getTxtCommBillSlab()));
objExchangeRateTO.setCommBillPer (new Double (getTxtCommBillPer()));
objExchangeRateTO.setCommDdSlab (new Double (getTxtCommDdSlab()));
objExchangeRateTO.setCommDdPer (new Double (getTxtCommDdPer()));
objExchangeRateTO.setCommTcSlab (new Double (getTxtCommTcSlab()));
objExchangeRateTO.setCommTcPer (new Double (getTxtCommTcPer()));
objExchangeRateTO.setCommTtSlab (new Double (getTxtCommTtSlab()));
objExchangeRateTO.setCommTtPer (new Double (getTxtCommTtPer()));
objExchangeRateTO.setCommCurrPer (new Double (getTxtCommCurrPer()));


setTxtExchangeId (objExchangeRateTO.getExchangeId ());
setTxtValueDate (DateUtil.getStringDate (objExchangeRateTO.getValueDate ()));
setTxtTransCurrency (objExchangeRateTO.getTransCurrency ());
setTxtConversionCurrency (objExchangeRateTO.getConversionCurrency ());
setTxtCustomerType (objExchangeRateTO.getCustomerType ());
setTxtMiddleRate (objExchangeRateTO.getMiddleRate ());
setTxtSellingPer (objExchangeRateTO.getSellingPer ());
setTxtSellingPrice (objExchangeRateTO.getSellingPrice ());
setTxtBuyingPer (objExchangeRateTO.getBuyingPer ());
setTxtBuyingPrice (objExchangeRateTO.getBuyingPrice ());
setTxtPreferred (objExchangeRateTO.getPreferred ());
setTxtCommission (objExchangeRateTO.getCommission ());
setTxtRemarks (objExchangeRateTO.getRemarks ());
setTxtCreatedBy (objExchangeRateTO.getCreatedBy ());
setTxtCreatedDt (DateUtil.getStringDate (objExchangeRateTO.getCreatedDt ()));
setTxtAuthorizedBy (objExchangeRateTO.getAuthorizedBy ());
setTxtAuthorizedDt (DateUtil.getStringDate (objExchangeRateTO.getAuthorizedDt ()));
setTxtStatus (objExchangeRateTO.getStatus ());
setTxtAuthorizeStatus (objExchangeRateTO.getAuthorizeStatus ());
setTxtRateType (objExchangeRateTO.getRateType ());
setTxtBillBuying (objExchangeRateTO.getBillBuying ());
setTxtBillSelling (objExchangeRateTO.getBillSelling ());
setTxtDdBuying (objExchangeRateTO.getDdBuying ());
setTxtDdSelling (objExchangeRateTO.getDdSelling ());
setTxtTcBuying (objExchangeRateTO.getTcBuying ());
setTxtTcSelling (objExchangeRateTO.getTcSelling ());
setTxtNotionalRate (objExchangeRateTO.getNotionalRate ());
setTxtTtBuying (objExchangeRateTO.getTtBuying ());
setTxtTtSelling (objExchangeRateTO.getTtSelling ());
setTxtCommBillSlab (objExchangeRateTO.getCommBillSlab ());
setTxtCommBillPer (objExchangeRateTO.getCommBillPer ());
setTxtCommDdSlab (objExchangeRateTO.getCommDdSlab ());
setTxtCommDdPer (objExchangeRateTO.getCommDdPer ());
setTxtCommTcSlab (objExchangeRateTO.getCommTcSlab ());
setTxtCommTcPer (objExchangeRateTO.getCommTcPer ());
setTxtCommTtSlab (objExchangeRateTO.getCommTtSlab ());
setTxtCommTtPer (objExchangeRateTO.getCommTtPer ());
setTxtCommCurrPer (objExchangeRateTO.getCommCurrPer ());

         */
}
