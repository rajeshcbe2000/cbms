/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition. 
 * 
 * OutwardClearingOB.java
 *
 * Created on January 12, 2004, 1:01 PM
 */

package com.see.truetransact.ui.transaction.clearing.outward;

/**
 *
 * @author  Hemant
 */
import java.util.HashMap;
import java.util.ArrayList;
import java.util.List;
//import java.util.Observable;
import java.util.Date;
import java.util.Set;
import java.util.TreeSet;
import java.util.Comparator;

import com.see.truetransact.clientproxy.ProxyFactory;
import com.see.truetransact.commonutil.CommonUtil;
import com.see.truetransact.commonutil.CommonConstants;
import com.see.truetransact.clientutil.ComboBoxModel;
import com.see.truetransact.transferobject.transaction.clearing.outward.*;
import com.see.truetransact.commonutil.DateUtil;
import com.see.truetransact.clientutil.EnhancedTableModel;
import com.see.truetransact.clientutil.ClientUtil;
import com.see.truetransact.clientutil.ClientConstants;
import com.see.truetransact.clientproxy.ProxyParameters;
import com.see.truetransact.clientexception.ClientParseException;
import com.see.truetransact.uicomponent.CObservable;
import com.see.truetransact.ui.TrueTransactMain;
import com.see.truetransact.transferobject.clearing.ParameterTO;
import com.see.truetransact.commonutil.TTException;

public class OutwardClearingOB extends CObservable{
    private OutwardClearingRB resourceBundle;
    
    private String txtBatchIdOC = "";
    private String cboClearingTypeID = "";
    private String cboInstrumentTypeID = "";
    private String cboInstrDetailsCurrency = "";
    private String cboScheduleNo="";
    private String dtdInstrumentDtID = "";
    private String dtclearingDtID="";
    private String txtInstrumentNo1ID = "";
    private String txtInstrumentNo2ID = "";
    private String txtAmountID = "";
    private String txtDrawerAccNoID = "";
    private String txtDrawerNameID = "";
    private String txtPayeeNameID = "";
    private String cboBankCodeID = "";
    private String cboBranchCodeID = "";
    private String txtRemarksID = "";
    private String cboProdIdPISD = "";
    private String txtAccNoPISD = "";
    private String txtAmountPISD = "";
    private String txtBankCode="";
    private String txtBranchCode="";
    private String txtConvAmt = "";
    private String txtRemarksPISD = "";
    private String outwardID;
    private String payInSlipID;
    private String lblTotalAmountValueID;
    private String lblTotalAmountValuePISD;
    private String lblAccHolderNameValuePISD;
    private String lblValProductCurrency;
    private String lblValScheduleNo;
    private String lblValAmount;
    private String lblValNoInstrBooked;
    private Date outwardIDDate;
    private Date payInSlipDate;
    private HashMap _authorizeMap;
    private EnhancedTableModel tbmID;
    private EnhancedTableModel tbmPISD;
    private String cboProductType="";
    private int  lotSize=0;
    String bookedamt=null;
    Date curDate = null;
    
    private List listID;
    private List listPISD;
    
    private ComboBoxModel cbmClearingTypeID;
    private ComboBoxModel cbmInstrumentTypeID;
    private ComboBoxModel cbmProdIdPISD;
//    private ComboBoxModel cbmBankCodeID;
//    private ComboBoxModel cbmBranchCodeID;
    private ComboBoxModel cbmProdType;
    private ComboBoxModel cbmScheduleNo;
    
    private ProxyFactory proxy;
    private HashMap lookUpHash;
    private HashMap keyValue;
    private ArrayList key;
    private ArrayList value;
    private HashMap map;
    
    private HashMap oldAmountMap;
    
    private int result,operation;
    private String lblStatus="";
    private String lblAccHead="";
    
    private String lblAccHeadDesc = "";
    private String createdBy="";
    
    private String lblAvailableBalance = "";
    private String lblUnclearBalance = "";
    private String prodCurrency;
    private Object[] objBouncingReasons;
    
    private ArrayList deletedListID,deletedListPSID;
    ParameterTO parmTO = null;
    
    private final static ClientParseException parseException = ClientParseException.getInstance();
    
    /** Creates a new instance of OutwardClearingOB */
    public OutwardClearingOB() {
        curDate = ClientUtil.getCurrentDate();
        map = new HashMap();
        map.put(CommonConstants.JNDI, "OutwardClearingJNDI");
        map.put(CommonConstants.HOME, "com.see.truetransact.serverside.transaction.clearing.outward.OutwardClearingHome");
        map.put(CommonConstants.REMOTE, "com.see.truetransact.serverside.transaction.clearing.outward.OutwardClearing");
        /**/
        try {
            proxy = ProxyFactory.createProxy();
            initUIComboBoxModel();

            fillDropdown();
            initTableModel();
            oldAmountMap = new HashMap();

            deletedListID = new ArrayList();
            deletedListPSID = new ArrayList();
        } catch (Exception e) {
            System.err.println( "Exception " + e + "Caught" );
            e.printStackTrace();
        }
    }    
    void setTxtBatchIdOC(String txtBatchIdOC){
        this.txtBatchIdOC = txtBatchIdOC;
        setChanged();
    }
    String getTxtBatchIdOC(){
        return this.txtBatchIdOC;
    }    
    void setCboClearingTypeID(String cboClearingTypeID){
        this.cboClearingTypeID = cboClearingTypeID;
        setChanged();
    }
    String getCboClearingTypeID(){
        return this.cboClearingTypeID;
    }    
    void setCboInstrumentTypeID(String cboInstrumentTypeID){
        this.cboInstrumentTypeID = cboInstrumentTypeID;
        setChanged();
    }
    String getCboInstrumentTypeID(){
        return this.cboInstrumentTypeID;
    }
    
    void setCboInstrDetailsCurrency(String cboInstrDetailsCurrency){
        this.cboInstrDetailsCurrency = cboInstrDetailsCurrency;
        setChanged();
    }
    
    String getCboInstrDetailsCurrency(){
        return this.cboInstrDetailsCurrency;
    }
    
    void setDtdInstrumentDtID(String dtdInstrumentDtID){
        this.dtdInstrumentDtID = dtdInstrumentDtID;
        setChanged();
    }
    String getDtdInstrumentDtID(){
        return this.dtdInstrumentDtID;
    }    
    void setTxtInstrumentNo1ID(String txtInstrumentNo1ID){
        this.txtInstrumentNo1ID = txtInstrumentNo1ID;
        setChanged();
    }
    String getTxtInstrumentNo1ID(){
        return this.txtInstrumentNo1ID;
    }
    
    void setTxtInstrumentNo2ID(String txtInstrumentNo2ID){
        this.txtInstrumentNo2ID = txtInstrumentNo2ID;
        setChanged();
    }
    String getTxtInstrumentNo2ID(){
        return this.txtInstrumentNo2ID;
    }
    void setTxtAmountID(String txtAmountID){
        this.txtAmountID = txtAmountID;
        setChanged();
    }
    String getTxtAmountID(){
        return this.txtAmountID;
    }
    
    void setTxtDrawerAccNoID(String txtDrawerAccNoID){
        this.txtDrawerAccNoID = txtDrawerAccNoID;
        setChanged();
    }
    String getTxtDrawerAccNoID(){
        return this.txtDrawerAccNoID;
    }
    
    void setTxtDrawerNameID(String txtDrawerNameID){
        this.txtDrawerNameID = txtDrawerNameID;
        setChanged();
    }
    String getTxtDrawerNameID(){
        return this.txtDrawerNameID;
    }
    
    void setTxtPayeeNameID(String txtPayeeNameID){
        this.txtPayeeNameID = txtPayeeNameID;
        setChanged();
    }
    String getTxtPayeeNameID(){
        return this.txtPayeeNameID;
    }
    
    void setCboBankCodeID(String cboBankCodeID){
        this.cboBankCodeID = cboBankCodeID;
        setChanged();
    }
    String getCboBankCodeID(){
        return this.cboBankCodeID;
    }
    
    void setCboBranchCodeID(String cboBranchCodeID){
        this.cboBranchCodeID = cboBranchCodeID;
        setChanged();
    }
    String getCboBranchCodeID(){
        return this.cboBranchCodeID;
    }
    
    void setTxtRemarksID(String txtRemarksID){
        this.txtRemarksID = txtRemarksID;
        setChanged();
    }
    String getTxtRemarksID(){
        return this.txtRemarksID;
    }
    
    void setCboProdIdPISD(String cboProdIdPISD){
        this.cboProdIdPISD = cboProdIdPISD;
        setChanged();
    }
    String getCboProdIdPISD(){
        return this.cboProdIdPISD;
    }
    
    void setTxtAccNoPISD(String txtAccNoPISD){
        this.txtAccNoPISD = txtAccNoPISD;
        setChanged();
    }
    String getTxtAccNoPISD(){
        return this.txtAccNoPISD;
    }
    
    void setLblValScheduleNo(String lblValScheduleNo){
        this.lblValScheduleNo = lblValScheduleNo;
        setChanged();
    }
    String getLblValScheduleNo(){
        return this.lblValScheduleNo;
    }
    
    void setTxtAmountPISD(String txtAmountPISD){
        this.txtAmountPISD = txtAmountPISD;
        setChanged();
    }
    String getTxtAmountPISD(){
        return this.txtAmountPISD;
    }
    
    void setTxtConvAmt(String txtConvAmt){
        this.txtConvAmt = txtConvAmt;
        setChanged();
    }
    String getTxtConvAmt(){
        return this.txtConvAmt;
    }
    
    void setTxtRemarksPISD(String txtRemarksPISD){
        this.txtRemarksPISD = txtRemarksPISD;
        setChanged();
    }
    String getTxtRemarksPISD(){
        return this.txtRemarksPISD;
    }
    
    public void initUIComboBoxModel(){
        cbmClearingTypeID = new ComboBoxModel();
        cbmInstrumentTypeID= new ComboBoxModel();
        cbmProdIdPISD= new ComboBoxModel();
//        cbmBankCodeID = new ComboBoxModel();
        cbmProdType = new ComboBoxModel();
        cbmScheduleNo=new ComboBoxModel();
    }
    
    private void getKeyValue(HashMap keyValue)  throws Exception{
        key = (ArrayList)keyValue.get(CommonConstants.KEY);
        value = (ArrayList)keyValue.get(CommonConstants.VALUE);
    }
    
    public void fillDropdown(){
        try{
            lookUpHash = new HashMap();
            lookUpHash.put(CommonConstants.MAP_NAME,null);
            final ArrayList lookup_keys = new ArrayList();
            
            lookup_keys.add("CASH.INST_TYPE");
            lookup_keys.add("PRODUCTTYPE");
            
            lookUpHash.put(CommonConstants.PARAMFORQUERY, lookup_keys);
            keyValue = com.see.truetransact.clientutil.ClientUtil.populateLookupData(lookUpHash);

            //__ Data for the ClearingType Combo-Box...
            lookUpHash.put(CommonConstants.MAP_NAME,"InwardClearing.getInwardClearingType");
            lookUpHash.put(CommonConstants.PARAMFORQUERY, ProxyParameters.BRANCH_ID);
            keyValue = ClientUtil.populateLookupData(lookUpHash);
            getKeyValue((HashMap)keyValue.get(CommonConstants.DATA));
            cbmClearingTypeID = new ComboBoxModel(key,value);
            
            getKeyValue((HashMap)keyValue.get("CASH.INST_TYPE"));
            cbmInstrumentTypeID= new ComboBoxModel(key,value);
            
            getKeyValue((HashMap)keyValue.get("PRODUCTTYPE"));
            this.cbmProdType= new ComboBoxModel(key,value);
            
            lookUpHash.put(CommonConstants.MAP_NAME,"InwardClearing.getBank");
            lookUpHash.put(CommonConstants.PARAMFORQUERY, null);
            keyValue = ClientUtil.populateLookupData(lookUpHash);
            getKeyValue((HashMap)keyValue.get(CommonConstants.DATA));
//            cbmBankCodeID = new ComboBoxModel(key,value);
            
        }catch(NullPointerException e){
            e.printStackTrace();
        }catch(Exception e){
            e.printStackTrace();
        }
    }
    
    /** Getter for property cbmClearingTypeID.
     * @return Value of property cbmClearingTypeID.
     *
     */
    public com.see.truetransact.clientutil.ComboBoxModel getCbmClearingTypeID() {
        return cbmClearingTypeID;
    }
    
    /** Setter for property cbmClearingTypeID.
     * @param cbmClearingTypeID New value of property cbmClearingTypeID.
     *
     */
    public void setCbmClearingTypeID(com.see.truetransact.clientutil.ComboBoxModel cbmClearingTypeID) {
        this.cbmClearingTypeID = cbmClearingTypeID;
        setChanged();
    }
    
    /** Getter for property cbmInstrumentTypeID.
     * @return Value of property cbmInstrumentTypeID.
     *
     */
    public com.see.truetransact.clientutil.ComboBoxModel getCbmInstrumentTypeID() {
        return cbmInstrumentTypeID;
    }
    
    /** Setter for property cbmInstrumentTypeID.
     * @param cbmInstrumentTypeID New value of property cbmInstrumentTypeID.
     *
     */
    public void setCbmInstrumentTypeID(com.see.truetransact.clientutil.ComboBoxModel cbmInstrumentTypeID) {
        this.cbmInstrumentTypeID = cbmInstrumentTypeID;
        setChanged();
    }
    
    /** Getter for property cbmProdIdPISD.
     * @return Value of property cbmProdIdPISD.
     *
     */
    public com.see.truetransact.clientutil.ComboBoxModel getCbmProdIdPISD() {
        return cbmProdIdPISD;
    }
    
//    public com.see.truetransact.clientutil.ComboBoxModel getCbmBankCodeID() {
//        return cbmBankCodeID;
//    }
//    
//    public void setCbmBankCodeID(com.see.truetransact.clientutil.ComboBoxModel cbmBankCodeID) {
//        this.cbmBankCodeID = cbmBankCodeID;
//        setChanged();
//    }
//    
//    public com.see.truetransact.clientutil.ComboBoxModel getCbmBranchCodeID() {
//        return cbmBranchCodeID;
//    }
//    
//    public void setCbmBranchCodeID(com.see.truetransact.clientutil.ComboBoxModel cbmBranchCodeID) {
//        this.cbmBranchCodeID = cbmBranchCodeID;
//        setChanged();
//    }
    /** Getter for property outwardID.
     * @return Value of property outwardID.
     *
     */
    public java.lang.String getOutwardID() {
        return outwardID;
    }
    
    /** Setter for property outwardID.
     * @param outwardID New value of property outwardID.
     *
     */
    public void setOutwardID(java.lang.String outwardID) {
        this.outwardID = outwardID;
        setChanged();
    }
    
    /** Getter for property payInSlipID.
     * @return Value of property payInSlipID.
     *
     */
    public java.lang.String getPayInSlipID() {
        return payInSlipID;
    }
    
    /** Setter for property payInSlipID.
     * @param payInSlipID New value of property payInSlipID.
     *
     */
    public void setPayInSlipID(java.lang.String payInSlipID) {
        this.payInSlipID = payInSlipID;
        setChanged();
    }
    
    /** Getter for property outwardIDDate.
     * @return Value of property outwardIDDate.
     *
     */
    public java.util.Date getOutwardIDDate() {
        return outwardIDDate;
    }
    
    /** Setter for property outwardIDDate.
     * @param outwardIDDate New value of property outwardIDDate.
     *
     */
    public void setOutwardIDDate(java.util.Date outwardIDDate) {
        this.outwardIDDate = outwardIDDate;
        setChanged();
    }
    
    /** Getter for property payInSlipDate.
     * @return Value of property payInSlipDate.
     *
     */
    public java.util.Date getPayInSlipDate() {
        return payInSlipDate;
    }
    
    /** Setter for property payInSlipDate.
     * @param payInSlipDate New value of property payInSlipDate.
     *
     */
    public void setPayInSlipDate(java.util.Date payInSlipDate) {
        this.payInSlipDate = payInSlipDate;
        setChanged();
    }
    
    /** Getter for property lblTotalAmountValueID.
     * @return Value of property lblTotalAmountValueID.
     *
     */
    public java.lang.String getLblTotalAmountValueID() {
        return lblTotalAmountValueID;
    }
    
    /** Setter for property lblTotalAmountValueID.
     * @param lblTotalAmountValueID New value of property lblTotalAmountValueID.
     *
     */
    public void setLblTotalAmountValueID(java.lang.String lblTotalAmountValueID) {
        this.lblTotalAmountValueID = lblTotalAmountValueID;
        setChanged();
    }
    
    /** Getter for property lblTotalAmountValuePISD.
     * @return Value of property lblTotalAmountValuePISD.
     *
     */
    public java.lang.String getLblTotalAmountValuePISD() {
        return lblTotalAmountValuePISD;
    }
    
    /** Setter for property lblTotalAmountValuePISD.
     * @param lblTotalAmountValuePISD New value of property lblTotalAmountValuePISD.
     *
     */
    public void setLblTotalAmountValuePISD(java.lang.String lblTotalAmountValuePISD) {
        this.lblTotalAmountValuePISD = lblTotalAmountValuePISD;
        setChanged();
    }
    
    /** Getter for property lblAccHolderNameValuePISD.
     * @return Value of property lblAccHolderNameValuePISD.
     *
     */
    public java.lang.String getLblAccHolderNameValuePISD() {
        return lblAccHolderNameValuePISD;
    }
    
    /** Setter for property lblAccHolderNameValuePISD.
     * @param lblAccHolderNameValuePISD New value of property lblAccHolderNameValuePISD.
     *
     */
    public void setLblAccHolderNameValuePISD(java.lang.String lblAccHolderNameValuePISD) {
        this.lblAccHolderNameValuePISD = lblAccHolderNameValuePISD;
        setChanged();
    }
    
    /** Getter for property tbmID.
     * @return Value of property tbmID.
     *
     */
    public com.see.truetransact.clientutil.EnhancedTableModel getTbmID() {
        return tbmID;
    }
    
    /** Setter for property tbmID.
     * @param tbmID New value of property tbmID.
     *
     */
    public void setTbmID(com.see.truetransact.clientutil.EnhancedTableModel tbmID) {
        this.tbmID = tbmID;
        setChanged();
    }
    
    /** Getter for property tbmPISD.
     * @return Value of property tbmPISD.
     *
     */
    public com.see.truetransact.clientutil.EnhancedTableModel getTbmPISD() {
        return tbmPISD;
    }
    
    /** Setter for property tbmPISD.
     * @param tbmPISD New value of property tbmPISD.
     *
     */
    public void setTbmPISD(com.see.truetransact.clientutil.EnhancedTableModel tbmPISD) {
        this.tbmPISD = tbmPISD;
        setChanged();
    }
    
    public OutwardClearingTO  getOutwardClearingTO(){
        OutwardClearingTO objOutwardClearingTO = new OutwardClearingTO();
        objOutwardClearingTO.setBatchId(getTxtBatchIdOC());
        
        Date ClrDt = DateUtil.getDateMMDDYYYY(getDtclearingDtID());
        if(ClrDt != null){
            Date clrDate = (Date)curDate.clone();
            clrDate.setDate(ClrDt.getDate());
            clrDate.setMonth(ClrDt.getMonth());
            clrDate.setYear(ClrDt.getYear());
        objOutwardClearingTO.setOutwardDt(clrDate); 
        }else{
                objOutwardClearingTO.setOutwardDt(DateUtil.getDateMMDDYYYY(getDtclearingDtID())); //Current Date   getOutwardIDDate()            //Current Date   getOutwardIDDate()
        }

        //        objOutwardClearingTO.setOutwardDt(DateUtil.getDateMMDDYYYY(getDtclearingDtID())); //Current Date   getOutwardIDDate()
        
        objOutwardClearingTO.setClearingType(getCboClearingTypeID());
        objOutwardClearingTO.setInstrumentType(getCboInstrumentTypeID());
        objOutwardClearingTO.setInstrumentNo1(getTxtInstrumentNo1ID());
        objOutwardClearingTO.setInstrumentNo2(getTxtInstrumentNo2ID());
        
        Date IsDt = DateUtil.getDateMMDDYYYY(getDtdInstrumentDtID());
        if(IsDt != null){
            Date isDate = (Date)curDate.clone();
            isDate.setDate(IsDt.getDate());
            isDate.setMonth(IsDt.getMonth());
            isDate.setYear(IsDt.getYear());
        objOutwardClearingTO.setInstrumentDt(isDate);
        }else{
            objOutwardClearingTO.setInstrumentDt(DateUtil.getDateMMDDYYYY(getDtdInstrumentDtID()));
        }
//        objOutwardClearingTO.setInstrumentDt(DateUtil.getDateMMDDYYYY(getDtdInstrumentDtID()));
        
        objOutwardClearingTO.setAmount(CommonUtil.convertObjToDouble(getTxtAmountID()));
        objOutwardClearingTO.setPayeeName(getTxtPayeeNameID());
        objOutwardClearingTO.setDrawer(getTxtDrawerNameID());
        objOutwardClearingTO.setDrawerAcctNo(getTxtDrawerAccNoID());
//        objOutwardClearingTO.setBankCode(getCboBankCodeID());
//        objOutwardClearingTO.setBranchCode(getCboBranchCodeID());
          objOutwardClearingTO.setBankCode(getTxtBankCode());
        objOutwardClearingTO.setBranchCode(getTxtBranchCode());
        objOutwardClearingTO.setRemarks(getTxtRemarksID());
        objOutwardClearingTO.setScheduleNo(getCboScheduleNo());       
        objOutwardClearingTO.setCreatedDt(curDate);
        objOutwardClearingTO.setStatusDt(curDate);
        objOutwardClearingTO.setInitiatedBranch(com.see.truetransact.ui.TrueTransactMain.BRANCH_ID);
        return objOutwardClearingTO;
    }
    /**/
    
    public void  setOutwardClearingTO(OutwardClearingTO objOutwardClearingTO){
        setStatusBy(objOutwardClearingTO.getStatusBy());
        setTxtBatchIdOC(objOutwardClearingTO.getBatchId());
        setOutwardID(objOutwardClearingTO.getOutwardId());
        setOutwardIDDate(objOutwardClearingTO.getOutwardDt());
        setCboClearingTypeID(objOutwardClearingTO.getClearingType());
        if (getOperation()==ClientConstants.ACTIONTYPE_REALIZE) {
            key=new ArrayList();
            value=new ArrayList();
            key.add(objOutwardClearingTO.getScheduleNo());
            value.add(objOutwardClearingTO.getScheduleNo());
            cbmScheduleNo=new ComboBoxModel(key,value);
        }
        setCboScheduleNo(objOutwardClearingTO.getScheduleNo());
        setCboInstrumentTypeID(objOutwardClearingTO.getInstrumentType());
        setTxtInstrumentNo1ID(objOutwardClearingTO.getInstrumentNo1());
        setTxtInstrumentNo2ID(objOutwardClearingTO.getInstrumentNo2());
        setDtdInstrumentDtID(DateUtil.getStringDate(objOutwardClearingTO.getInstrumentDt()));
        setTxtAmountID(CommonUtil.convertObjToStr(objOutwardClearingTO.getAmount()));
        setTxtPayeeNameID(objOutwardClearingTO.getPayeeName());
        setTxtDrawerNameID(objOutwardClearingTO.getDrawer());
        setTxtDrawerAccNoID(objOutwardClearingTO.getDrawerAcctNo());
//        setCboBankCodeID(objOutwardClearingTO.getBankCode());
         setTxtBankCode(objOutwardClearingTO.getBankCode());
         setTxtBranchCode(objOutwardClearingTO.getBranchCode());
         /*
          * Combo box model for the Branch Code is to be added because its not Called in the
          * fillDropdown() method; as its dependent on the Value of Bank Code.
          */
        getBranchData();
//        setCboBranchCodeID(objOutwardClearingTO.getBranchCode());
        setTxtRemarksID(objOutwardClearingTO.getRemarks());        
        
        setCreatedBy(objOutwardClearingTO.getCreatedBy());
    }
    
    public OutwardClearingPISTO getOutwardClearingPISTO(){
        OutwardClearingPISTO objOutwardClearingPISTO = new OutwardClearingPISTO();
        objOutwardClearingPISTO.setBatchId(getTxtBatchIdOC());
        objOutwardClearingPISTO.setPayInSlipDt(getPayInSlipDate());
        objOutwardClearingPISTO.setProdId(CommonUtil.convertObjToStr(cbmProdIdPISD.getKeyForSelected()));
        objOutwardClearingPISTO.setAcctNo(getTxtAccNoPISD());
        objOutwardClearingPISTO.setAmount(CommonUtil.convertObjToDouble(getTxtAmountPISD()));
        objOutwardClearingPISTO.setRemarks(getTxtRemarksPISD());
        objOutwardClearingPISTO.setAcHdId(this.getLblAccHead());
        objOutwardClearingPISTO.setProdType(this.getCboProductType());
        return objOutwardClearingPISTO;
    }
    
    public void setOutwardClearingPISTO(OutwardClearingPISTO objOutwardClearingPISTO){
        setCboProductType(objOutwardClearingPISTO.getProdType());
//        setCbmProdIdPISD(objOutwardClearingPISTO.getProdType());
        setTxtBatchIdOC(objOutwardClearingPISTO.getBatchId());
        setPayInSlipID(objOutwardClearingPISTO.getPayInSlipId());
        setPayInSlipDate(objOutwardClearingPISTO.getPayInSlipDt());
        setCboProdIdPISD(objOutwardClearingPISTO.getProdId());
        setTxtAccNoPISD(objOutwardClearingPISTO.getAcctNo());
        if (objOutwardClearingPISTO.getProdType().equals("GL")) {
            setLblAccHead(objOutwardClearingPISTO.getAcHdId());
            HashMap accountNameMap = new HashMap();
            accountNameMap.put("ACC_NUM",getLblAccHead());
            List resultList = ClientUtil.executeQuery("getAccountNumberName"+this.getCboProductType(),accountNameMap);
            HashMap resultMap = (HashMap)resultList.get(0);
            setLblAccHeadDesc(CommonUtil.convertObjToStr(resultMap.get("CUSTOMER_NAME")));
        }
            
        setTxtAmountPISD(CommonUtil.convertObjToStr(objOutwardClearingPISTO.getAmount()));
        setTxtRemarksPISD(objOutwardClearingPISTO.getRemarks());
    }
    
    /*This method Fires a Query to the Database and 'll
    returm a HashMap which 'll contain the result Lists.
    /**/
    public HashMap populateData(HashMap whereMap) {
        HashMap mapData = null;
        try {
            if(getOperation()==ClientConstants.ACTIONTYPE_AUTHORIZE ||
            getOperation()==ClientConstants.ACTIONTYPE_EXCEPTION ||
            getOperation()==ClientConstants.ACTIONTYPE_REJECT)
                whereMap.put(CommonConstants.AUTHORIZEMAP, "Authorize");
            else if (getOperation()==ClientConstants.ACTIONTYPE_REALIZE)
                whereMap.put(CommonConstants.REALIZEMAP, "Realize");
            else if(getOperation()==0 )
                  whereMap.put("VIEW","view");
            mapData = proxy.executeQuery(whereMap, map);
        } catch( Exception e ) {
            System.err.println( "Exception " + e.toString() + "Caught" );
            e.printStackTrace();
        }
        return mapData;
    }
    
    // This Methode 'll initilize TableModel for the Tables used in UI.
    private void initTableModel(){
        OutwardClearingRB rb = new OutwardClearingRB();
        
        tbmID = new EnhancedTableModel(
        new Object [][] {
            
        },
        new String [] {
            rb.getString("tblLblClID"), rb.getString("tblLblDAN"), rb.getString("tblLblAmt")
        }
        ) {
            Class[] types = new Class [] {
                java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class
            };
            boolean[] canEdit = new boolean [] {
                false, false,false, false, false, false
            };
            
            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }
            
            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        };
        
        
        tbmPISD = new EnhancedTableModel(
        new Object [][] {
            
        },
        new String [] {
            rb.getString("tblLblPISID"), rb.getString("tblLblPID"), rb.getString("tblLblAN"), rb.getString("tblLblAmt")
        }
        ) {
            Class[] types = new Class [] {
                java.lang.Object.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class
            };
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false
            };
            
            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }
            
            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        };
    }
    
    private void setDates(){
        outwardIDDate = (Date)curDate.clone();
        payInSlipDate = (Date)curDate.clone();
    }
    
    //This'll computes the sum of amount of all the rows in the Outward Clearing table.
    //And sets the total amountb label.
    private void setTotalAmountTbmID(){
        double sum = 0;
        int rows = tbmID.getRowCount();
        int i=0;
        while(i<rows){
            sum = sum + CommonUtil.convertObjToDouble(tbmID.getValueAt(i,2)).doubleValue();
            i++;
        }
        setLblTotalAmountValueID(Double.toString(sum));
    }
    
    //This'll computes the sum of amount of all the rows in the Pay In Slip table.
    //And sets the total amountb label.
    private void setTotalAmountTbmPISD(){
        double sum = 0;
        int rows = tbmPISD.getRowCount();
        int i=0;
        while(i<rows){
            sum = sum + CommonUtil.convertObjToDouble(tbmPISD.getValueAt(i,3)).doubleValue();
            i++;
        }
        setLblTotalAmountValuePISD(Double.toString(sum));
    }
    
    //This methode 'll resets the Serial no of tables.
    //This 'll be called whenever a row is deleted from table.
    public void rearrangeSN(EnhancedTableModel tbm){
        int rows = tbm.getRowCount();
        int i = 0;
        while(i<rows){
            tbm.setValueAt(Integer.toString(i+1),i,0);
            i++;
        }
    }
    
    //It 'll set null value to corresponding the UI Components for Outward detail.
    private void resetIDComponents(){
        
        if(listID!=null && listID.size()>0){
            setCboClearingTypeID(((OutwardClearingTO)listID.get(0)).getClearingType());
        }else
            setCboClearingTypeID("");
        setCboScheduleNo("");
        setLblValScheduleNo("");
        setCboInstrumentTypeID("");
        setCboInstrDetailsCurrency("");
        setTxtInstrumentNo1ID("");
        setTxtInstrumentNo2ID("");
        setDtdInstrumentDtID("");
        setTxtAmountID("");
        setTxtPayeeNameID("");
        setTxtDrawerNameID("");
        setTxtDrawerAccNoID("");
        setCboBankCodeID("");
        setCboBranchCodeID("");
        setTxtRemarksID("");
        setTxtBankCode("");
        setTxtBranchCode("");
    }
    public  void  getClearingDate(){
        try{
            HashMap schNo=new HashMap();
            schNo.put("CLEARING_TYPE", getCbmClearingTypeID().getKeyForSelected());
            schNo.put("SCHEDULE_NO",getCbmScheduleNo().getKeyForSelected());
            schNo.put("BRANCH_ID", ProxyParameters.BRANCH_ID);
            List resultList=null;
            if(schNo.get("SCHEDULE_NO")!=null)
                resultList = ClientUtil.executeQuery("getScheduleNo", schNo);
            if(resultList !=null && resultList.size()>0){
                schNo=(HashMap)resultList.get(0);
                setDtclearingDtID(CommonUtil.convertObjToStr(schNo.get("CLEARING_DT")));
                getClearingDetails();
                
            }
            
            notifyObservers();
        }
       catch(Exception e){
           e.printStackTrace();
       }
    }
    
    public  void  getViewClearingDate(){
        try{
            HashMap schNo=new HashMap();
            schNo.put("CLEARING_TYPE", getCbmClearingTypeID().getKeyForSelected());
            schNo.put("SCHEDULE_NO",getCbmScheduleNo().getKeyForSelected());
            schNo.put("BRANCH_ID", ProxyParameters.BRANCH_ID);
            List resultList=null;
            if(schNo.get("SCHEDULE_NO")!=null)
                resultList = ClientUtil.executeQuery("getViewScheduleNo", schNo);
            if(resultList !=null && resultList.size()>0){
                schNo=(HashMap)resultList.get(0);
                setDtclearingDtID(CommonUtil.convertObjToStr(schNo.get("CLEARING_DT")));
                getClearingDetails();
                
            }
            
            notifyObservers();
        }
       catch(Exception e){
           e.printStackTrace();
       }
    }
    
    /** To retrive Schedule No based on Clearing Type*/
    public void getScheduleNoForProd() {
        try {
            HashMap schedNoMap = new HashMap();
            schedNoMap.put("CLEARING_TYPE", getCbmClearingTypeID().getKeyForSelected());
            schedNoMap.put("BRANCH_ID", ProxyParameters.BRANCH_ID);

            List resultList = ClientUtil.executeQuery("getScheduleNo", schedNoMap);
            //--- If the selected Clearing Type have the record, then set the Schedule No
            //--- appropriate to that.
            key=new ArrayList();
            value=new ArrayList();
            key.add("");
            value.add("");
            if(resultList !=null && resultList.size()>0){
                for(int i=0;i<resultList.size();i++){
                    HashMap resultMap = (HashMap)resultList.get(i);
                    key.add(resultMap.get("SCHEDULE_NO"));
                    value.add(resultMap.get("SCHEDULE_NO"));
                    
//                setLblValScheduleNo(CommonUtil.convertObjToStr(resultMap.get("SCHEDULE_NO")));
                }
                cbmScheduleNo=new ComboBoxModel(key,value);
		} else if(resultList == null || resultList.size()==0){ //--- Else if Clearing Type does not have the
                setLblValScheduleNo("");         //--- record, reset the Schedule No.
                cbmScheduleNo=new ComboBoxModel();
            }
            setChanged();
//            if((remarks.equalsIgnoreCase("Proceeds Received")))
                notifyObservers();
        }catch(Exception e){
            parseException.logException(e,true);
        }
    }
    
    
    //It 'll set null value to corresponding the UI Components for Pay In Slip detail.
    private void resetPISDComponents(){
        setCboProductType("");
        setCboProdIdPISD("");
        setCbmProdIdPISD("");
        setTxtAccNoPISD("");
        setTxtAmountPISD("");
        setTxtConvAmt("");
        setTxtRemarksPISD("");
        setLblValProductCurrency("");
        setLblAccHolderNameValuePISD("");
        setLblValProductCurrency("");
        setLblAccHead("");
        setLblAccHeadDesc("");
    }
    
    //This methode delete all the rows of Outward detail Table and Pay In Slip Detail Table.
    public void resetTbm(){
        initTableModel();
    }
    
    public void resetOB(){
        listID=null;
        listPISD=null;
        resetTbm();
        setTxtBatchIdOC("");
        setLblTotalAmountValueID("");
        setLblTotalAmountValuePISD("");
        oldAmountMap.clear();
        this.deletedListID.clear();
        this.deletedListPSID.clear();
        setChanged();
        setDtclearingDtID("");
        setLblValNoInstrBooked("");
        setLblValAmount("");
        setCreatedBy("");
        notifyObservers();
        
    }
    
    //This method corresponds to the EndInstruments Button's Action Event.
    public void btnEndPISDAction(){
        try{
            resetIDComponents();
            resetPISDComponents();
            setChanged();
            notifyObservers();
        }catch(Exception e){
            e.printStackTrace();
        }
    }
    
    //This method corresponds to Pay In SLip Details Table Select Event.
    public int rowSelectedPISDAction(int index){
        try{
            OutwardClearingPISTO ocPISTO = (OutwardClearingPISTO)listPISD.get(index);
            setOutwardClearingPISTO(ocPISTO);
            notifyObservers();
            return 1;
        }catch(Exception e){
            e.printStackTrace();
        }
        return 0;
    }
    
    //This method corresponds to Pay In SLip Details Delete Button Event.
    public void btnDeletePISDAction(int index){
        try{
            if(index>=0) {
                OutwardClearingPISTO ocPISTO = (OutwardClearingPISTO)listPISD.get(index);
                if(ocPISTO.getPayInSlipId()!=null && ocPISTO.getPayInSlipId().length()>0) {
                    resourceBundle = new OutwardClearingRB();
                    ocPISTO.setStatus(CommonConstants.STATUS_DELETED);
                    ocPISTO.setAmount(CommonUtil.convertObjToDouble(oldAmountMap.get(ocPISTO.getKeyData())));
                    
                    ocPISTO.setAuthorizeBy(ProxyParameters.USER_ID);
                    ocPISTO.setAuthorizeDt(curDate);
                    ocPISTO.setAuthorizeRemarks(resourceBundle.getString("LOCAL_DELETE"));
                    ocPISTO.setAuthorizeStatus(CommonConstants.STATUS_AUTHORIZED);
                    
                    this.deletedListPSID.add(ocPISTO);
                }
                tbmPISD.removeRow(index);
                listPISD.remove(index);
                setTotalAmountTbmPISD();
                rearrangeSN(tbmPISD);
                resetPISDComponents();
                notifyObservers();
            }
        }catch(Exception e){
            e.printStackTrace();
        }
    }
    
    //This method corresponds to Pay In Slip Details Save Button Event.
    public void btnSavePISDAction(int rowSelected){
        try{
            if(listPISD == null){
                listPISD = new ArrayList();
            }

            OutwardClearingPISTO ocPISTO = getOutwardClearingPISTO();
            List tableRow = new ArrayList();
            tableRow.add(String.valueOf(rowSelected+1));
            tableRow.add(ocPISTO.getProdId());
            tableRow.add(ocPISTO.getAcctNo());
            tableRow.add(ocPISTO.getAmount().toString());
            
            if(rowSelected == -1) {
                tableRow.set(0,Integer.toString(tbmPISD.getRowCount()+1));
                listPISD.add(ocPISTO);
                tbmPISD.addRow((ArrayList)tableRow);
            }else {
                tbmPISD.removeRow(rowSelected);
                tbmPISD.insertRow(rowSelected,(ArrayList)tableRow);
                ocPISTO.setPayInSlipId(((OutwardClearingPISTO)listPISD.get(rowSelected)).getPayInSlipId());
                ocPISTO.setStatus(((OutwardClearingPISTO)listPISD.get(rowSelected)).getStatus());
                listPISD.set(rowSelected,ocPISTO);
            }
            setTotalAmountTbmPISD();
            resetPISDComponents();
            tableRow=null;
            notifyObservers();
        }catch(Exception e){
            e.printStackTrace();
        }
    }
    
    //This method corresponds to Outward Details Delete Button Event.
    public void btnDeleteIDAction(int index){
        try{
            if(index>=0){
                OutwardClearingTO ocTO = (OutwardClearingTO)listID.get(index);
                if(ocTO.getOutwardId()!=null && ocTO.getOutwardId().length()>0) {
                    resourceBundle = new OutwardClearingRB();
                    ocTO.setStatus(CommonConstants.STATUS_DELETED);
                    
                    ocTO.setAuthorizeStatus(CommonConstants.STATUS_AUTHORIZED);
                    ocTO.setAuthorizeBy(ProxyParameters.USER_ID);
                    ocTO.setAuthorizeDt(curDate);
                    ocTO.setAuthorizeRemarks(resourceBundle.getString("LOCAL_DELETE"));
                    
                    ocTO.setStatusBy(ProxyParameters.USER_ID);
                    ocTO.setAmount((Double)oldAmountMap.get(ocTO.getKeyData()));
                    this.deletedListID.add(ocTO);
                }
                tbmID.removeRow(index);
                listID.remove(index);
                setTotalAmountTbmID();
                rearrangeSN(tbmID);
                resetIDComponents();
                notifyObservers();
            }
        }catch(Exception e){
            e.printStackTrace();
        }
    }
    
    //This method corresponds to Outward Details Table Selection Event.
    public int rowSelectedIDAction(int index){
        int state = 0;
        try{
            OutwardClearingTO ocTO;
            if(listID != null){
                ocTO = (OutwardClearingTO)listID.get(index);
                setOutwardClearingTO(ocTO);
                System.out.println("ocTO : " + ocTO);
                notifyObservers();
                state = 1;
            }
        }catch(Exception e){
            e.printStackTrace();
        }
        return state;
    }
    
    //This method corresponds to Outward Details Save Button Event.
    public void btnSaveIDAction(int rowSelected){
        try{
            if(listID == null){
                listID = new ArrayList();
            }

            OutwardClearingTO ocTO = getOutwardClearingTO();
            List tableRow = new ArrayList();
            tableRow.add(String.valueOf(rowSelected+1));
            tableRow.add(ocTO.getInstrumentNo2());
            tableRow.add(ocTO.getAmount().toString());
            if(rowSelected == -1) {
                tableRow.set(0,Integer.toString(tbmID.getRowCount()+1));
                listID.add(ocTO);
                tbmID.addRow((ArrayList)tableRow);
            }else {
                tbmID.removeRow(rowSelected);
                tbmID.insertRow(rowSelected,(ArrayList)tableRow);
                ocTO.setOutwardId(((OutwardClearingTO)listID.get(rowSelected)).getOutwardId());
                ocTO.setStatus(((OutwardClearingTO)listID.get(rowSelected)).getStatus());
                listID.set(rowSelected,ocTO);
            }
            System.out.println("list ID : " + listID);
            setTotalAmountTbmID();
            resetIDComponents();
            tableRow=null;
            notifyObservers();
        }catch(Exception e){
            e.printStackTrace();
        }
    }
    
    //This method corresponds to Pay In Slip Details New Button Action Event.
    public void btnNewPISDAction(){
        try{
            resetPISDComponents();
            setPayInSlipID(null);
            setDates();
            setChanged();
            notifyObservers();
        }catch(Exception e){
            e.printStackTrace();
        }
    }
    
    //This method corresponds to Outward Details New Button Action Event.
    public void btnNewIDAction(){
        try{
            resetIDComponents();
            setOutwardID(null);
            setDates();
            setChanged();
            notifyObservers();
        }catch(Exception e){
            e.printStackTrace();
        }
    }
    
    public java.lang.String getLblValProductCurrency() {
        return lblValProductCurrency;
    }
    
    public void setLblValProductCurrency(java.lang.String lblValProductCurrency) {
        this.lblValProductCurrency = lblValProductCurrency;
        setChanged();
    }
    
    public void cboProdIdPISDChanged(String valProdId){
        String keyProdId = CommonUtil.convertObjToStr(getCbmProdIdPISD().getKeyForSelected());
        try{
            setCboProdIdPISD(keyProdId);
            HashMap whereMap = new HashMap();
            whereMap.put(CommonConstants.MAP_NAME,"getAccountHeadID4PISD"+this.getCboProductType());
            whereMap.put(CommonConstants.MAP_WHERE,keyProdId);
            HashMap returnHash = populateData(whereMap);
            if(returnHash != null){
                List list =  (List)returnHash.get("getAccountHeadID4PISD"+this.getCboProductType());
                if(list.size()!=0){
                    whereMap = (HashMap)list.get(0);
                    setLblAccHead(CommonUtil.convertObjToStr(whereMap.get("accHeadID")));
                    setLblAccHeadDesc(CommonUtil.convertObjToStr(whereMap.get("accHeadDesc")));
                }
            }            
        }catch(Exception e){
            e.printStackTrace();
        }
    }

    public void setProdCurrency(String prodCurr) {
        this.prodCurrency = prodCurr;
        setChanged();
    }
    
    public String getProdCurrency() {
        return this.prodCurrency;
    }
    
    int doAction(String command){
        int state = 0;
        try{
            
            // The following block added by Rajesh to avoid Save operation after Authorization.
            // If one person opened a transaction for Edit and another person opened the same 
            // transaction for Authorization, the system is allowing to save after Authorization also.  
            // So, after authorization again the GL gets updated and a/c level shadow credit/debit goes negative.
            // In this case the should not allow to save or some error message should display.  
            if ((!getTxtBatchIdOC().equals("")) && getOperation()!=ClientConstants.ACTIONTYPE_AUTHORIZE && getOperation()!=ClientConstants.ACTIONTYPE_REJECT && getOperation()!=ClientConstants.ACTIONTYPE_REALIZE) {
                HashMap whereMap = new HashMap();
                whereMap.put("BATCH_ID", getTxtBatchIdOC());
                whereMap.put("INITIATED_BRANCH", ProxyParameters.BRANCH_ID);
                whereMap.put("TRANS_DT", curDate.clone());
                List lst = ClientUtil.executeQuery("getOutwardClearingAuthorizeStatus", whereMap);
                if (lst!=null && lst.size()>0) {
                    whereMap = (HashMap) lst.get(0);
                    String authStatus = CommonUtil.convertObjToStr(whereMap.get("AUTHORIZE_STATUS"));
                    String authBy = CommonUtil.convertObjToStr(whereMap.get("AUTHORIZE_BY"));
                    if (!authStatus.equals("")) {
                        setOperation(ClientConstants.ACTIONTYPE_FAILED);
                        throw new TTException("This transaction already "+authStatus.toLowerCase()+" by "+authBy);
                    }
                }
            }
            // End
        
            HashMap dataMap = new HashMap();
            
            if(listID != null && listID.size() > 0 ){
                listID.addAll(this.deletedListID);
                dataMap.put("OutwardClearingTO",listID);
            }
            
            if(listPISD != null && listPISD.size() > 0 ){
                listPISD.addAll(this.deletedListPSID);
                dataMap.put("OutwardClearingPISTO",listPISD);
            }
            dataMap.put("Command",command);
            dataMap.put(CommonConstants.REALIZEMAP, getAuthorizeMap());
            dataMap.put("OLDAMOUNT", this.getOldAmountMap());
            HashMap proxyReturnMap = proxy.execute(dataMap, map);
            if (proxyReturnMap != null && proxyReturnMap.containsKey(CommonConstants.TRANS_ID)) {
                setProxyReturnMap(proxyReturnMap);            
                ClientUtil.showMessageWindow ("Transaction Batch No. : " + CommonUtil.convertObjToStr(proxyReturnMap.get(CommonConstants.TRANS_ID)));
            }
            state = 1;
            setResult(getOperation());
        }catch(Exception e) {
            setResult(ClientConstants.ACTIONTYPE_FAILED);
            parseException.logException(e,true);
            System.err.println( "Exception " + e + "Caught" );
            e.printStackTrace();
        }
        return state;
    }
    
    void btnEndIDAction(){
        resetIDComponents();
        notifyObservers();
    }
    
    public void setAccountName(String AccountNo){
        try {
            HashMap accountNameMap = new HashMap();
            accountNameMap.put("ACC_NUM",AccountNo);
            List resultList = ClientUtil.executeQuery("getAccountNumberName"+this.getCboProductType(),accountNameMap);
            if(resultList !=null && resultList.size()>0) {
            HashMap resultMap = (HashMap)resultList.get(0);
            String pID =  getCbmProdIdPISD().getKeyForSelected().toString() ;
             HashMap dataMap = new HashMap();
            accountNameMap.put("BRANCH_ID", TrueTransactMain.BRANCH_ID);
            List lst = (List) ClientUtil.executeQuery("getProdIdForActNo"+this.getCboProductType(),accountNameMap);
            if(lst!=null && lst.size()>0){
               dataMap=(HashMap)lst.get(0);
               if(dataMap.get("PROD_ID").equals(pID)){
                 setLblAccHolderNameValuePISD(CommonUtil.convertObjToStr(resultMap.get("CUSTOMER_NAME")));
                 setTxtAccNoPISD(AccountNo);
                }else{
                       setLblAccHolderNameValuePISD("");
                       setTxtAccNoPISD("");
                         }
                     }else{
                         setLblAccHolderNameValuePISD("");
                         setTxtAccNoPISD("");
                     }
//             setTxtAccNoPISD(AccountNo);
//            setLblAccHolderNameValuePISD(CommonUtil.convertObjToStr(resultMap.get("CUSTOMER_NAME")));
            notifyObservers();
             }
        }catch(Exception e){
            e.printStackTrace();
        }
    }
        
    
    void fetchData(HashMap where){
        try{
            OutwardClearingTO ocTO;
            OutwardClearingPISTO ocPISTO;
            HashMap result = populateData(where);
            if(result!=null){
                listID = (List) result.get("OutwardClearingTO");
                sortListID();
                if(listID != null && listID.size()>0 ){
                    int size = listID.size();
                    int i=0;
                    while(i<size){
                        ocTO = (OutwardClearingTO) listID.get(i);
                        
                        addToOldAmountMap(ocTO);
                        
                        List tableRow = new ArrayList();
                        tableRow.add(Integer.toString(i+1));
                        tableRow.add(ocTO.getInstrumentNo2());
                        tableRow.add(ocTO.getAmount().toString());
                        tbmID.addRow((ArrayList)tableRow);
                        i++;
                    }
                    setTxtBatchIdOC(((OutwardClearingTO)listID.get(size-1)).getBatchId());
                    
                }
                setTotalAmountTbmID();
                
                listPISD = (List) result.get("OutwardClearingPISTO");
                sortListPISD();
                if(listPISD != null && listPISD.size()>0 ){
                    int size = listPISD.size();
                    int i=0;
                    while(i<size){
                        ocPISTO = (OutwardClearingPISTO) listPISD.get(i);
                        addToOldAmountMap(ocPISTO);
                        List tableRow = new ArrayList();
                        tableRow.add(Integer.toString(i+1));
                        tableRow.add(ocPISTO.getProdId());
                        tableRow.add(ocPISTO.getAcctNo());
                        tableRow.add(ocPISTO.getAmount().toString());
                        tbmPISD.addRow((ArrayList)tableRow);
                        i++;
                    }
                }
                setTotalAmountTbmPISD();
                
                setChanged();
                notifyObservers();
            }
            
        }catch(Exception e){
            e.printStackTrace();
        }
    }
    private void addToOldAmountMap(OutwardClearingPISTO obj){
        this.oldAmountMap.put(obj.getKeyData(),obj.getAmount());
    }
    private void addToOldAmountMap(OutwardClearingTO obj){
        this.oldAmountMap.put(obj.getKeyData(),obj.getAmount());
    }
    private void sortListID(){
        Set sortedList = new TreeSet(
        new Comparator(){
            public int compare(Object a, Object b){
                return (((OutwardClearingTO)a).getOutwardId().compareTo(((OutwardClearingTO)b).getOutwardId()));
            }
        });
        
        sortedList.addAll(listID);
        listID.removeAll(listID);
        listID.addAll(sortedList);
        sortedList=null;
    }
    
    private void sortListPISD(){
        Set sortedList = new TreeSet(
        new Comparator(){
            public int compare(Object a, Object b){
                return (((OutwardClearingPISTO)a).getPayInSlipId().compareTo( ((OutwardClearingPISTO)b).getPayInSlipId()));
            }
        });
        
        sortedList.addAll(listPISD);
        listPISD.removeAll(listPISD);
        listPISD.addAll(sortedList);
        sortedList=null;
    }
    
    public void setCbmProdIdPISD(String prodType) {
        if (CommonUtil.convertObjToStr(prodType).length() > 0 && !CommonUtil.convertObjToStr(prodType).equals("GL")) {
            System.out.println("prodType : setCbmProdIdPISD " + prodType);
            try {
                lookUpHash = new HashMap();
                lookUpHash.put(CommonConstants.MAP_NAME,"Cash.getAccProduct" + prodType);
                keyValue = ClientUtil.populateLookupData(lookUpHash);
                getKeyValue((HashMap)keyValue.get(CommonConstants.DATA));
                cbmProdIdPISD = new ComboBoxModel(key,value);
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        } else {
            cbmProdIdPISD = new ComboBoxModel();
        }
        setChanged();
   }
    
    public boolean checkAcNoWithoutProdType(String actNum) {
        HashMap mapData=new HashMap();
        boolean isExists = false;
        try{//dont delete chck selectalldao
            mapData.put("ACT_NUM", actNum);
            List mapDataList = ClientUtil.executeQuery("getActNumFromAllProducts", mapData); 
            System.out.println("#### mapDataList :"+mapDataList);
            if (mapDataList!=null && mapDataList.size()>0) {
                mapData=(HashMap)mapDataList.get(0);
                setTxtAccNoPISD(CommonUtil.convertObjToStr(mapData.get("ACT_NUM")));
                cbmProdType.setKeyForSelected(CommonUtil.convertObjToStr(mapData.get("PROD_TYPE")));
                setCbmProdIdPISD(CommonUtil.convertObjToStr(mapData.get("PROD_TYPE")));
                cbmProdIdPISD.setKeyForSelected(CommonUtil.convertObjToStr(mapData.get("PROD_ID")));
                isExists = true;
            } else {
//                ArrayList key=new ArrayList();
//                ArrayList value=new ArrayList();
//                key.add("");
//                value.add("");   
                setCbmProdIdPISD("");
                isExists = false;
//                key = null;
//                value = null;
                isExists = false;
            }
            mapDataList = null;
        }catch(Exception e){
            e.printStackTrace();
        }
        mapData = null;
        return isExists;
    }
    
    /**
     * To get the Value of Branch Code, Depending on the Bank Code
     */
    public void getBranchData(){
        try {
            lookUpHash = new HashMap();
            String bankCode = getCboBankCodeID();
            System.out.println("bankCode : " + bankCode);
            if (bankCode.length() > 0) {
                lookUpHash.put(CommonConstants.MAP_NAME,"InwardClearing.getBranch");
                lookUpHash.put(CommonConstants.PARAMFORQUERY, bankCode);
                keyValue = ClientUtil.populateLookupData(lookUpHash);
                getKeyValue((HashMap)keyValue.get(CommonConstants.DATA));
//                cbmBranchCodeID = new ComboBoxModel(key,value);
            }
        }catch(Exception e){
            parseException.logException(e,true);
        }
    }
    
    // Setter for Authorization.
    public void setAuthorizeMap(HashMap authorizeMap) {
        _authorizeMap = authorizeMap;
        setChanged();
    }
    
    // Getter for Authorization.
    public HashMap getAuthorizeMap() {
        return _authorizeMap;
    }
    
    /** Getter for property oldAmountMap.
     * @return Value of property oldAmountMap.
     *
     */
    public java.util.HashMap getOldAmountMap() {
        return oldAmountMap;
    }
    
    /** Setter for property oldAmountMap.
     * @param oldAmountMap New value of property oldAmountMap.
     *
     */
    public void setOldAmountMap(java.util.HashMap oldAmountMap) {
        this.oldAmountMap = oldAmountMap;
        setChanged();
    }
    
    /** Getter for property result.
     * @return Value of property result.
     *
     */
    public int getResult() {
        return result;
    }
    
    /** Setter for property result.
     * @param result New value of property result.
     *
     */
    public void setResult(int result) {
        this.result = result;
        setChanged();
    }
    
    /** Getter for property operation.
     * @return Value of property operation.
     *
     */
    public int getOperation() {
        return operation;
    }
    
    /** Setter for property operation.
     * @param operation New value of property operation.
     *
     */
    public void setOperation(int operation) {
        this.operation = operation;
        setChanged();
    }
    
    public String getLblStatus(){
        return this.lblStatus;
    }
    
    public void setLblStatus(String lblStatus) {
        this.lblStatus = lblStatus;
        setChanged();
    }
    
    /** To set the status based on ActionType, either New, Edit, etc., */
    public void setStatus(){
        this.setLblStatus( ClientConstants.ACTION_STATUS[this.getOperation()]);
    }
    
    /** To update the Status based on result performed by doAction() method */
    public void setResultStatus(){
        this.setLblStatus(ClientConstants.RESULT_STATUS[this.getResult()]);
        setChanged();
    }
    
    /** Getter for property lblAccHead.
     * @return Value of property lblAccHead.
     *
     */
    public java.lang.String getLblAccHead() {
        return lblAccHead;
    }
    
    /** Setter for property lblAccHead.
     * @param lblAccHead New value of property lblAccHead.
     *
     */
    public void setLblAccHead(java.lang.String lblAccHead) {
        this.lblAccHead = lblAccHead;
        setChanged();
    }    
    
    /** Getter for property lblAccHeadDesc.
     * @return Value of property lblAccHeadDesc.
     *
     */
    public java.lang.String getLblAccHeadDesc() {
        return lblAccHeadDesc;
    }
    
    /** Setter for property lblAccHeadDesc.
     * @param lblAccHeadDesc New value of property lblAccHeadDesc.
     *
     */
    public void setLblAccHeadDesc(java.lang.String lblAccHeadDesc) {
        this.lblAccHeadDesc = lblAccHeadDesc;
        setChanged();
    }
    
    /** Getter for property cbmProdType.
     * @return Value of property cbmProdType.
     *
     */
    public com.see.truetransact.clientutil.ComboBoxModel getCbmProdType() {
        return cbmProdType;
    }
    
    /** Setter for property cbmProdType.
     * @param cbmProdType New value of property cbmProdType.
     *
     */
    public void setCbmProdType(com.see.truetransact.clientutil.ComboBoxModel cbmProdType) {
        this.cbmProdType = cbmProdType;
        setChanged();
    }
    
    /** Getter for property cboProductType.
     * @return Value of property cboProductType.
     *
     */
    public java.lang.String getCboProductType() {
        return cboProductType;
    }
    
    /** Setter for property cboProductType.
     * @param cboProductType New value of property cboProductType.
     *
     */
    public void setCboProductType(java.lang.String cboProductType) {
        this.cboProductType = cboProductType;
        setChanged();
    }
    
    /** Getter for property createdBy.
     * @return Value of property createdBy.
     *
     */
    public java.lang.String getCreatedBy() {
        return createdBy;
    }
    
    /** Setter for property createdBy.
     * @param createdBy New value of property createdBy.
     *
     */
    public void setCreatedBy(java.lang.String createdBy) {
        this.createdBy = createdBy;
        setChanged();
    }
    
    /**
     * Getter for property cboScheduleNo.
     * @return Value of property cboScheduleNo.
     */
    public java.lang.String getCboScheduleNo() {
        return cboScheduleNo;
    }
    
    /**
     * Setter for property cboScheduleNo.
     * @param cboScheduleNo New value of property cboScheduleNo.
     */
    public void setCboScheduleNo(java.lang.String cboScheduleNo) {
        this.cboScheduleNo = cboScheduleNo;
    }
    
    /**
     * Getter for property dtclearingDtID.
     * @return Value of property dtclearingDtID.
     */
    public java.lang.String getDtclearingDtID() {
        return dtclearingDtID;
    }
    
    /**
     * Setter for property dtclearingDtID.
     * @param dtclearingDtID New value of property dtclearingDtID.
     */
    public void setDtclearingDtID(java.lang.String dtclearingDtID) {
        this.dtclearingDtID = dtclearingDtID;
    }
    
    /**
     * Getter for property cbmScheduleNo.
     * @return Value of property cbmScheduleNo.
     */
    public com.see.truetransact.clientutil.ComboBoxModel getCbmScheduleNo() {
        return cbmScheduleNo;
    }
    
    /**
     * Setter for property cbmScheduleNo.
     * @param cbmScheduleNo New value of property cbmScheduleNo.
     */
    public void setCbmScheduleNo(com.see.truetransact.clientutil.ComboBoxModel cbmScheduleNo) {
        this.cbmScheduleNo = cbmScheduleNo;
    }
    
    /**
     * Getter for property txtBankCode.
     * @return Value of property txtBankCode.
     */
    public java.lang.String getTxtBankCode() {
        return txtBankCode;
    }
    
    /**
     * Setter for property txtBankCode.
     * @param txtBankCode New value of property txtBankCode.
     */
    public void setTxtBankCode(java.lang.String txtBankCode) {
        this.txtBankCode = txtBankCode;
    }
    
    /**
     * Getter for property txtBranchCode.
     * @return Value of property txtBranchCode.
     */
    public java.lang.String getTxtBranchCode() {
        return txtBranchCode;
    }
    
    /**
     * Setter for property txtBranchCode.
     * @param txtBranchCode New value of property txtBranchCode.
     */
    public void setTxtBranchCode(java.lang.String txtBranchCode) {
        this.txtBranchCode = txtBranchCode;
    }
    
    public void populateClearingType() {
        HashMap where = new HashMap();
        HashMap mapwhere=new HashMap();
       
        mapwhere.put("BRANCH_ID", TrueTransactMain.BRANCH_ID);
        System.out.print("BRANCH###"+TrueTransactMain.BRANCH_ID);
        where.put(CommonConstants.MAP_NAME,"OutwardClearing.getBounceClearingType");
        where.put(CommonConstants.PARAMFORQUERY, TrueTransactMain.BRANCH_ID);
        keyValue = ClientUtil.populateLookupData(where);
        try {
        getKeyValue((HashMap)keyValue.get(CommonConstants.DATA));
        }catch(Exception e){}
        setBouncingClearingType(value.toArray());     
        
    }

    /**
     * Getter for property objBouncingReasons.
     * @return Value of property objBouncingReasons.
     */
   public Object[] getBouncingClearingType() {
        return objBouncingReasons;
    }
    
    public void setBouncingClearingType(Object[] objBouncingReasons) {
        this.objBouncingReasons = objBouncingReasons;
    }
    
    public void getClearingDetails(){
        if (getCboScheduleNo()!=null && getCboScheduleNo().length()>0) {
            List lst=null;
            String bookedinst=null;
            String bookedamt=null;
            HashMap  where =new HashMap();
            where.put("BRANCH_ID",TrueTransactMain.BRANCH_ID);
            where.put("SCHEDULE_NO",getCboScheduleNo());
            HashMap detailMap=new HashMap();
            
            lst =ClientUtil.executeQuery("outWardclearingDetails",where);
            if( lst!=null && lst.size()>0 ){
                detailMap=(HashMap)lst.get(0);
                bookedamt=CommonUtil.convertObjToStr(detailMap.get("BOOKEDAMT"));
                bookedinst=CommonUtil.convertObjToStr(detailMap.get("BOOKEDINST"));
            }
            setLblValAmount(bookedamt);
            setLblValNoInstrBooked(bookedinst);
            
            where =null;
        }
    }
    
    /**
     * Getter for property lblValAmount.
     * @return Value of property lblValAmount.
     */
    public java.lang.String getLblValAmount() {
        return lblValAmount;
    }
    
    /**
     * Setter for property lblValAmount.
     * @param lblValAmount New value of property lblValAmount.
     */
    public void setLblValAmount(java.lang.String lblValAmount) {
        this.lblValAmount = lblValAmount;
    }
    
    /**
     * Getter for property lblValNoInstrBooked.
     * @return Value of property lblValNoInstrBooked.
     */
    public java.lang.String getLblValNoInstrBooked() {
        return lblValNoInstrBooked;
    }
    
    /**
     * Setter for property lblValNoInstrBooked.
     * @param lblValNoInstrBooked New value of property lblValNoInstrBooked.
     */
    public void setLblValNoInstrBooked(java.lang.String lblValNoInstrBooked) {
        this.lblValNoInstrBooked = lblValNoInstrBooked;
    }
    
    public boolean checkForLotSize(){
        HashMap where =new HashMap();
        where.put("CLEARING_TYPE",cboClearingTypeID);
        where.put("BRANCH_CODE",TrueTransactMain.BRANCH_ID);
        parmTO = new ParameterTO();
        parmTO=(ParameterTO)(ClientUtil.executeQuery("getSelectParameterTO", where).get(0));
          lotSize=CommonUtil.convertObjToInt(parmTO.getLotsizeMicrClearing());
        if(parmTO.getLotsizeMicrClearing().doubleValue() <= CommonUtil.convertObjToDouble(lblValNoInstrBooked).doubleValue() ||
           parmTO.getLotsizeMicrClearing().doubleValue()==0){
            return true;
        }
        return false;
    }
    
    public boolean checkForHighValue(){
        if (parmTO!=null)
        if (parmTO.getHighValAppl().equals("Y") && 
            CommonUtil.convertObjToDouble(txtAmountID).doubleValue() < CommonUtil.convertObjToDouble(parmTO.getHighValCheque()).doubleValue()){
            return true;
        }
        return false;
    }    
    
    public void getViewScheduleNoForProd() {
        try {
            HashMap schedNoMap = new HashMap();
            schedNoMap.put("CLEARING_TYPE", getCbmClearingTypeID().getKeyForSelected());
            schedNoMap.put("BRANCH_ID", ProxyParameters.BRANCH_ID);

            List resultList = ClientUtil.executeQuery("getViewScheduleNo", schedNoMap);
            //--- If the selected Clearing Type have the record, then set the Schedule No
            //--- appropriate to that.
            key=new ArrayList();
            value=new ArrayList();
            key.add("");
            value.add("");
            if(resultList !=null && resultList.size()>0){
                for(int i=0;i<resultList.size();i++){
                    HashMap resultMap = (HashMap)resultList.get(i);
                    key.add(resultMap.get("SCHEDULE_NO"));
                    value.add(resultMap.get("SCHEDULE_NO"));
                    
//                setLblValScheduleNo(CommonUtil.convertObjToStr(resultMap.get("SCHEDULE_NO")));
                }
                cbmScheduleNo=new ComboBoxModel(key,value);
		} else if(resultList == null || resultList.size()==0){ //--- Else if Clearing Type does not have the
                setLblValScheduleNo("");         //--- record, reset the Schedule No.
                cbmScheduleNo=new ComboBoxModel();
            }
            setChanged();
            notifyObservers();
        }catch(Exception e){
            parseException.logException(e,true);
        }
    }
    
    /**
     * Getter for property lotSize.
     * @return Value of property lotSize.
     */
    public int getLotSize() {
        return lotSize;
    }
    
    /**

     * Setter for property lotSize.
     * @param lotSize New value of property lotSize.
     */
    public void setLotSize(int lotSize) {
        this.lotSize = lotSize;
    }
    
    /**
     * Getter for property bookedamt.
     * @return Value of property bookedamt.
     */
    public java.lang.String getBookedamt() {
        return bookedamt;
    }
    
    /**
     * Setter for property bookedamt.
     * @param bookedamt New value of property bookedamt.
     */
    public void setBookedamt(java.lang.String bookedamt) {
        this.bookedamt = bookedamt;
    }
    
}
