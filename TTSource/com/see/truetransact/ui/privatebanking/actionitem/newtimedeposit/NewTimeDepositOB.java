/**
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition.
 * 
 *
 * NewTimeDepositOB.java
 *
 * Created on July 12, 2004, 4:12 PM
 */

package com.see.truetransact.ui.privatebanking.actionitem.newtimedeposit;

/**
 *
 * @author  Ashok
 */

import java.util.Observable;
import java.util.HashMap;
import java.util.ArrayList;
import java.util.List;
import java.util.Date;

import com.see.truetransact.clientproxy.ProxyFactory;
import com.see.truetransact.clientexception.ClientParseException;
import com.see.truetransact.clientutil.ClientConstants;
import com.see.truetransact.clientutil.ClientUtil;
import com.see.truetransact.commonutil.DateUtil;
import com.see.truetransact.commonutil.CommonConstants;
import com.see.truetransact.commonutil.CommonUtil;
import com.see.truetransact.clientutil.ComboBoxModel;
import com.see.truetransact.transferobject.privatebanking.actionitem.newtimedeposit.NewTimeDepositTO;
import com.see.truetransact.clientutil.ComboBoxModel;
import org.apache.log4j.Logger;
import com.see.truetransact.ui.TrueTransactMain;
import com.see.truetransact.uicomponent.CObservable;

public class NewTimeDepositOB extends CObservable {
    
    private String txtMember = "";
    private String txtEntitlementGroup = "";
    private String txtPortfolioLocation = "";
    private String txtPortfolioAssetSubClass = "";
    private String txtPortfolioAccount = "";
    private String txtSettlementAssetSubClass = "";
    private String txtSettlementAccount = "";
    private String txtAssetSubClass = "";
    private String txtAccount = "";
    private String txtPrincipalAssetSubClass = "";
    private String txtPrincipalAccount = "";
    private String tdtExecutionDate = "";
    private String tdtStartDate = "";
    private String txtOrderAmount = "";
    private String txtSpread = "";
    private String cboProductType = "";
    private String cboSettlementType = "";
    private String txtTenor1 = "";
    private String txtTenor2 = "";
    private String tdtMaturityDate = "";
    private String txtAutorollInd = "";
    private boolean rdoPhoneOrder_Yes = false;
    private boolean rdoPhoneOrder_No = false;
    private String txtClientRate = "";
    private String txtBankOfficeInstruction = "";
    private String txtTraderDealerInst = "";
    private String txtCreditNotes = "";
    private String txtClientAdvices = "";
    private String lblReferenceNumber = "";
    private final static ClientParseException parseException = ClientParseException.getInstance();
    private String lblStatus = ClientConstants.ACTION_STATUS[ClientConstants.ACTIONTYPE_CANCEL];
    private final static Logger _log = Logger.getLogger(NewTimeDepositOB.class);
    private int _result,_actionType;
    private ProxyFactory proxy;
    private HashMap lookUpHash;
    private HashMap keyValue,map;
    private ArrayList key;
    private ArrayList value;
    private static NewTimeDepositOB objNewTimeDepositOB;
    private final String YES = "Y",NO = "N";
    private ComboBoxModel cbmProductType,cbmSettlementType;
    Date curDate = null;
    
    /** Creates a new instance of NewTimeDepositOB */
    private NewTimeDepositOB() {
        curDate = ClientUtil.getCurrentDate();
        map = new HashMap();
        map.put(CommonConstants.JNDI, "NewTimeDepositJNDI");
        map.put(CommonConstants.HOME, "serverside.privatebanking.actionitem.newtimedeposit.NewTimeDepositHome");
        map.put(CommonConstants.REMOTE, "serverside.privatebanking.actionitem.newtimedeposit.NewTimeDeposit");
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
            objNewTimeDepositOB= new NewTimeDepositOB();
        } catch(Exception e) {
            parseException.logException(e,true);
        }
    }
    
    /** This method Returns an instance of this class **/
    public static NewTimeDepositOB getInstance()throws Exception{
        return objNewTimeDepositOB;
    }
    
    /**
     * Getter for property lblReferenceNumber.
     * @return Value of property lblReferenceNumber.
     */
    public java.lang.String getLblReferenceNumber() {
        return lblReferenceNumber;
    }
    
    /**
     * Setter for property lblReferenceNumber.
     * @param lblReferenceNumber New value of property lblReferenceNumber.
     */
    public void setLblReferenceNumber(java.lang.String lblReferenceNumber) {
        this.lblReferenceNumber = lblReferenceNumber;
    }
    
    // Setter method for txtMember
    void setTxtMember(String txtMember){
        this.txtMember = txtMember;
        setChanged();
    }
    // Getter method for txtMember
    String getTxtMember(){
        return this.txtMember;
    }
    
    // Setter method for txtEntitlementGroup
    void setTxtEntitlementGroup(String txtEntitlementGroup){
        this.txtEntitlementGroup = txtEntitlementGroup;
        setChanged();
    }
    // Getter method for txtEntitlementGroup
    String getTxtEntitlementGroup(){
        return this.txtEntitlementGroup;
    }
    
    // Setter method for txtPortfolioLocation
    void setTxtPortfolioLocation(String txtPortfolioLocation){
        this.txtPortfolioLocation = txtPortfolioLocation;
        setChanged();
    }
    // Getter method for txtPortfolioLocation
    String getTxtPortfolioLocation(){
        return this.txtPortfolioLocation;
    }
    
    // Setter method for txtPortfolioAssetSubClass
    void setTxtPortfolioAssetSubClass(String txtPortfolioAssetSubClass){
        this.txtPortfolioAssetSubClass = txtPortfolioAssetSubClass;
        setChanged();
    }
    // Getter method for txtPortfolioAssetSubClass
    String getTxtPortfolioAssetSubClass(){
        return this.txtPortfolioAssetSubClass;
    }
    
    // Setter method for txtPortfolioAccount
    void setTxtPortfolioAccount(String txtPortfolioAccount){
        this.txtPortfolioAccount = txtPortfolioAccount;
        setChanged();
    }
    // Getter method for txtPortfolioAccount
    String getTxtPortfolioAccount(){
        return this.txtPortfolioAccount;
    }
    
    // Setter method for txtSettlementAssetSubClass
    void setTxtSettlementAssetSubClass(String txtSettlementAssetSubClass){
        this.txtSettlementAssetSubClass = txtSettlementAssetSubClass;
        setChanged();
    }
    // Getter method for txtSettlementAssetSubClass
    String getTxtSettlementAssetSubClass(){
        return this.txtSettlementAssetSubClass;
    }
    
    // Setter method for txtSettlementAccount
    void setTxtSettlementAccount(String txtSettlementAccount){
        this.txtSettlementAccount = txtSettlementAccount;
        setChanged();
    }
    // Getter method for txtSettlementAccount
    String getTxtSettlementAccount(){
        return this.txtSettlementAccount;
    }
    
    // Setter method for txtAssetSubClass
    void setTxtAssetSubClass(String txtAssetSubClass){
        this.txtAssetSubClass = txtAssetSubClass;
        setChanged();
    }
    // Getter method for txtAssetSubClass
    String getTxtAssetSubClass(){
        return this.txtAssetSubClass;
    }
    
    // Setter method for txAccount
    void setTxtAccount(String txtAccount){
        this.txtAccount = txtAccount;
        setChanged();
    }
    // Getter method for txAccount
    String getTxtAccount(){
        return this.txtAccount;
    }
    
    // Setter method for txtPrincipalAssetSubClass
    void setTxtPrincipalAssetSubClass(String txtPrincipalAssetSubClass){
        this.txtPrincipalAssetSubClass = txtPrincipalAssetSubClass;
        setChanged();
    }
    // Getter method for txtPrincipalAssetSubClass
    String getTxtPrincipalAssetSubClass(){
        return this.txtPrincipalAssetSubClass;
    }
    
    // Setter method for txPrincipalAccount
    void setTxtPrincipalAccount(String txtPrincipalAccount){
        this.txtPrincipalAccount = txtPrincipalAccount;
        setChanged();
    }
    // Getter method for txPrincipalAccount
    String getTxtPrincipalAccount(){
        return this.txtPrincipalAccount;
    }
    
    // Setter method for tdtExecutionDate
    void setTdtExecutionDate(String tdtExecutionDate){
        this.tdtExecutionDate = tdtExecutionDate;
        setChanged();
    }
    // Getter method for tdtExecutionDate
    String getTdtExecutionDate(){
        return this.tdtExecutionDate;
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
    
    // Setter method for txtOrderAmount
    void setTxtOrderAmount(String txtOrderAmount){
        this.txtOrderAmount = txtOrderAmount;
        setChanged();
    }
    // Getter method for txtOrderAmount
    String getTxtOrderAmount(){
        return this.txtOrderAmount;
    }
    
    // Setter method for txtSpread
    void setTxtSpread(String txtSpread){
        this.txtSpread = txtSpread;
        setChanged();
    }
    // Getter method for txtSpread
    String getTxtSpread(){
        return this.txtSpread;
    }
    
    // Setter method for cboProductType
    void setCboProductType(String cboProductType){
        this.cboProductType = cboProductType;
        setChanged();
    }
    // Getter method for cboProductType
    String getCboProductType(){
        return this.cboProductType;
    }
    
    /**
     * Getter for property cbmProductType.
     * @return Value of property cbmProductType.
     */
    public com.see.truetransact.clientutil.ComboBoxModel getCbmProductType() {
        return cbmProductType;
    }
    
    /**
     * Setter for property cbmProductType.
     * @param cbmProductType New value of property cbmProductType.
     */
    public void setCbmProductType(com.see.truetransact.clientutil.ComboBoxModel cbmProductType) {
        this.cbmProductType = cbmProductType;
    }
    
    // Setter method for cboSettlementType
    void setCboSettlementType(String cboSettlementType){
        this.cboSettlementType = cboSettlementType;
        setChanged();
    }
    // Getter method for cboSettlementType
    String getCboSettlementType(){
        return this.cboSettlementType;
    }
    
    /**
     * Getter for property cbmSettlementType.
     * @return Value of property cbmSettlementType.
     */
    public com.see.truetransact.clientutil.ComboBoxModel getCbmSettlementType() {
        return cbmSettlementType;
    }
    
    /**
     * Setter for property cbmSettlementType.
     * @param cbmSettlementType New value of property cbmSettlementType.
     */
    public void setCbmSettlementType(com.see.truetransact.clientutil.ComboBoxModel cbmSettlementType) {
        this.cbmSettlementType = cbmSettlementType;
    }
    
    // Setter method for txtTenor1
    void setTxtTenor1(String txtTenor1){
        this.txtTenor1 = txtTenor1;
        setChanged();
    }
    // Getter method for txtTenor1
    String getTxtTenor1(){
        return this.txtTenor1;
    }
    
    // Setter method for txtTenor2
    void setTxtTenor2(String txtTenor2){
        this.txtTenor2 = txtTenor2;
        setChanged();
    }
    // Getter method for txtTenor
    String getTxtTenor2(){
        return this.txtTenor2;
    }
    
    // Setter method for tdtMaturityDate
    void setTdtMaturityDate(String tdtMaturityDate){
        this.tdtMaturityDate = tdtMaturityDate;
        setChanged();
    }
    // Getter method for tdtMaturityDate
    String getTdtMaturityDate(){
        return this.tdtMaturityDate;
    }
    
    // Setter method for txtAutorollInd
    void setTxtAutorollInd(String txtAutorollInd){
        this.txtAutorollInd = txtAutorollInd;
        setChanged();
    }
    // Getter method for txtAutorollInd
    String getTxtAutorollInd(){
        return this.txtAutorollInd;
    }
    
    // Setter method for rdoPhoneOrder_Yes
    void setRdoPhoneOrder_Yes(boolean rdoPhoneOrder_Yes){
        this.rdoPhoneOrder_Yes = rdoPhoneOrder_Yes;
        setChanged();
    }
    // Getter method for rdoPhoneOrder_Yes
    boolean getRdoPhoneOrder_Yes(){
        return this.rdoPhoneOrder_Yes;
    }
    
    // Setter method for rdoPhoneOrder_No
    void setRdoPhoneOrder_No(boolean rdoPhoneOrder_No){
        this.rdoPhoneOrder_No = rdoPhoneOrder_No;
        setChanged();
    }
    // Getter method for rdoPhoneOrder_No
    boolean getRdoPhoneOrder_No(){
        return this.rdoPhoneOrder_No;
    }
    
    // Setter method for txtClientRate
    void setTxtClientRate(String txtClientRate){
        this.txtClientRate = txtClientRate;
        setChanged();
    }
    // Getter method for txtClientRate
    String getTxtClientRate(){
        return this.txtClientRate;
    }
    
    // Setter method for txtBankOfficeInstruction
    void setTxtBankOfficeInstruction(String txtBankOfficeInstruction){
        this.txtBankOfficeInstruction = txtBankOfficeInstruction;
        setChanged();
    }
    // Getter method for txtBankOfficeInstruction
    String getTxtBankOfficeInstruction(){
        return this.txtBankOfficeInstruction;
    }
    
    // Setter method for txtTraderDealerInst
    void setTxtTraderDealerInst(String txtTraderDealerInst){
        this.txtTraderDealerInst = txtTraderDealerInst;
        setChanged();
    }
    // Getter method for txtTraderDealerInst
    String getTxtTraderDealerInst(){
        return this.txtTraderDealerInst;
    }
    
    // Setter method for txtCreditNotes
    void setTxtCreditNotes(String txtCreditNotes){
        this.txtCreditNotes = txtCreditNotes;
        setChanged();
    }
    // Getter method for txtCreditNotes
    String getTxtCreditNotes(){
        return this.txtCreditNotes;
    }
    
    // Setter method for txtClientAdvices
    void setTxtClientAdvices(String txtClientAdvices){
        this.txtClientAdvices = txtClientAdvices;
        setChanged();
    }
    // Getter method for txtClientAdvices
    String getTxtClientAdvices(){
        return this.txtClientAdvices;
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
    
    /** Creates the instances for the comboboxmodels */
    private void initUIComboBoxModel(){
        cbmProductType = new ComboBoxModel();
        cbmSettlementType = new ComboBoxModel();
    }
    
    /* Filling up the the ComboBox in the UI*/
    private void fillDropdown() throws Exception{
        try{
            _log.info("Inside FillDropDown");
            lookUpHash = new HashMap();
            lookUpHash.put(CommonConstants.MAP_NAME, null);
            final ArrayList lookup_keys = new ArrayList();
            lookup_keys.add("PVT_TD.PROD_TYPE");
            lookup_keys.add("PVT_TD.SETTLEMENT_TYPE");
            lookUpHash.put(CommonConstants.PARAMFORQUERY, lookup_keys);
            keyValue = ClientUtil.populateLookupData(lookUpHash);
            getKeyValue((HashMap)keyValue.get("PVT_TD.PROD_TYPE"));
            cbmProductType = new ComboBoxModel(key,value);
            getKeyValue((HashMap) keyValue.get("PVT_TD.SETTLEMENT_TYPE"));
            cbmSettlementType = new ComboBoxModel(key,value);
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
    
    /** Returns a map by executing a  query which can be used to fill up the labels in the ui */
    public HashMap getLabelMap(String orderId)throws Exception {
        HashMap resultMap = null;
        try {
            HashMap whereMap = new HashMap();
            whereMap.put("ORD_ID", orderId);
            List resultList = ClientUtil.executeQuery("SelectPvtOrderMaster", whereMap);
            if(resultList.size() > 0){
                resultMap = (HashMap)resultList.get(0);
            }
            resultList = null;
        }catch(Exception e){
        }
        return resultMap;
    }
    
    /** Return an Instance of NewTimeDepositTO */
    public NewTimeDepositTO getNewTimeDepositTO(String command){
        NewTimeDepositTO objNewTimeDepositTO = new NewTimeDepositTO();
        objNewTimeDepositTO.setCommand(command);
        if(objNewTimeDepositTO.getCommand().equals(CommonConstants.TOSTATUS_INSERT)){
            objNewTimeDepositTO.setStatus(CommonConstants.STATUS_CREATED);
        }else{
            objNewTimeDepositTO.setStatusBy(TrueTransactMain.USER_ID);
        }
        objNewTimeDepositTO.setRefId(getLblReferenceNumber());
        objNewTimeDepositTO.setMemberId(getTxtMember());
        objNewTimeDepositTO.setSafeEntitleGrp(getTxtEntitlementGroup());
        objNewTimeDepositTO.setPortfolioLoc(getTxtPortfolioLocation());
        objNewTimeDepositTO.setSafeAssetSubclass(getTxtPortfolioAssetSubClass());
        objNewTimeDepositTO.setSafeAcct(getTxtPortfolioAccount());
        objNewTimeDepositTO.setIntAssetSubclass1(getTxtSettlementAssetSubClass());
        objNewTimeDepositTO.setIntAssetSubclass2(getTxtAssetSubClass());
        objNewTimeDepositTO.setIntAcct1(getTxtSettlementAccount());
        objNewTimeDepositTO.setIntAcct2(getTxtAccount());
        objNewTimeDepositTO.setPrinAssetSubclass(getTxtPrincipalAssetSubClass());
        objNewTimeDepositTO.setPrinAcct(getTxtPrincipalAccount());
        
        Date ExDt = DateUtil.getDateMMDDYYYY(getTdtExecutionDate());
        if(ExDt != null){
        Date exDate = (Date)curDate.clone();
        exDate.setDate(ExDt.getDate());
        exDate.setMonth(ExDt.getMonth());
        exDate.setYear(ExDt.getYear());
//        objNewTimeDepositTO.setExecDt(DateUtil.getDateMMDDYYYY(getTdtExecutionDate()));
        objNewTimeDepositTO.setExecDt(exDate);
        }else{
            objNewTimeDepositTO.setExecDt(DateUtil.getDateMMDDYYYY(getTdtExecutionDate()));
        }
        
        Date StDt = DateUtil.getDateMMDDYYYY(getTdtStartDate());
        if(StDt != null){
        Date stDate = (Date)curDate.clone();
        stDate.setDate(StDt.getDate());
        stDate.setMonth(StDt.getMonth());
        stDate.setYear(StDt.getYear());
//        objNewTimeDepositTO.setStartDt(DateUtil.getDateMMDDYYYY(getTdtStartDate()));
        objNewTimeDepositTO.setStartDt(stDate);
        }else{
            objNewTimeDepositTO.setStartDt(DateUtil.getDateMMDDYYYY(getTdtStartDate()));
        }
        
        objNewTimeDepositTO.setAmt(CommonUtil.convertObjToDouble(getTxtOrderAmount()));
        objNewTimeDepositTO.setSpread(CommonUtil.convertObjToDouble(getTxtSpread()));
        objNewTimeDepositTO.setProdType(getCboProductType());
        objNewTimeDepositTO.setSettlementType(getCboSettlementType());
        objNewTimeDepositTO.setTenor1(getTxtTenor1());
        objNewTimeDepositTO.setTenor2(getTxtTenor2());
        
        Date MatDt = DateUtil.getDateMMDDYYYY(getTdtMaturityDate());
        if(MatDt != null){
        Date matDate = (Date)curDate.clone();
        matDate.setDate(MatDt.getDate());
        matDate.setMonth(MatDt.getMonth());
        matDate.setYear(MatDt.getYear());
//        objNewTimeDepositTO.setMaturityDt(DateUtil.getDateMMDDYYYY(getTdtMaturityDate()));
        objNewTimeDepositTO.setMaturityDt(matDate);
        }else{
            objNewTimeDepositTO.setMaturityDt(DateUtil.getDateMMDDYYYY(getTdtMaturityDate()));
        }
        
        objNewTimeDepositTO.setAutoroll(getTxtAutorollInd());
        if(getRdoPhoneOrder_Yes()){
            objNewTimeDepositTO.setPhoneOrder(YES);
        }else{
            objNewTimeDepositTO.setPhoneOrder(NO);
        }
        objNewTimeDepositTO.setClientRate(getTxtClientRate());
        objNewTimeDepositTO.setBankInstruct(getTxtBankOfficeInstruction());
        objNewTimeDepositTO.setTraderInstruct(getTxtTraderDealerInst());
        objNewTimeDepositTO.setCreditNotes(getTxtCreditNotes());
        objNewTimeDepositTO.setClientAdvices(getTxtClientAdvices());
        return objNewTimeDepositTO;
    }
    
    /**  Sets the OB Fields using TO */
    public void setNewTimeDepositTO(NewTimeDepositTO objNewTimeDepositTO){
        setLblReferenceNumber(objNewTimeDepositTO.getRefId());
        setTxtMember(objNewTimeDepositTO.getMemberId());
        setTxtEntitlementGroup(objNewTimeDepositTO.getSafeEntitleGrp());
        setTxtPortfolioLocation(objNewTimeDepositTO.getPortfolioLoc());
        setTxtPortfolioAssetSubClass(objNewTimeDepositTO.getSafeAssetSubclass());
        setTxtPortfolioAccount(objNewTimeDepositTO.getSafeAcct());
        setTxtSettlementAssetSubClass(objNewTimeDepositTO.getIntAssetSubclass1());
        setTxtAssetSubClass(objNewTimeDepositTO.getIntAssetSubclass2());
        setTxtSettlementAccount(objNewTimeDepositTO.getIntAcct1());
        setTxtAccount(objNewTimeDepositTO.getIntAcct2());
        setTxtPrincipalAssetSubClass(objNewTimeDepositTO.getPrinAssetSubclass());
        setTxtPrincipalAccount(objNewTimeDepositTO.getPrinAcct());
        setTdtExecutionDate(DateUtil.getStringDate(objNewTimeDepositTO.getExecDt()));
        setTdtStartDate(DateUtil.getStringDate(objNewTimeDepositTO.getStartDt()));
        setTxtOrderAmount(CommonUtil.convertObjToStr(objNewTimeDepositTO.getAmt()));
        setTxtSpread(CommonUtil.convertObjToStr(objNewTimeDepositTO.getSpread()));
        setCboProductType(objNewTimeDepositTO.getProdType());
        setCboSettlementType(objNewTimeDepositTO.getSettlementType());
        setTxtTenor1(objNewTimeDepositTO.getTenor1());
        setTxtTenor2(objNewTimeDepositTO.getTenor2());
        setTdtMaturityDate(DateUtil.getStringDate(objNewTimeDepositTO.getMaturityDt()));
        setTxtAutorollInd(objNewTimeDepositTO.getAutoroll());
        if(objNewTimeDepositTO.getPhoneOrder().equals(YES)){
            setRdoPhoneOrder_Yes(true);
        }else{
            setRdoPhoneOrder_No(true);
        }
        setTxtClientRate(objNewTimeDepositTO.getClientRate());
        setTxtBankOfficeInstruction(objNewTimeDepositTO.getBankInstruct());
        setTxtTraderDealerInst(objNewTimeDepositTO.getTraderInstruct());
        setTxtCreditNotes(objNewTimeDepositTO.getCreditNotes());
        setTxtClientAdvices(objNewTimeDepositTO.getClientAdvices());
        notifyObservers();
    }
    
    /* Executes Query using the TO object */
    public void execute(String command) {
        try {
            HashMap term = new HashMap();
            term.put("NewTimeDepositTO", getNewTimeDepositTO(command));
            term.put(CommonConstants.MODULE, getModule());
            term.put(CommonConstants.SCREEN, getScreen());
            HashMap proxyResultMap = proxy.execute(term, map);
            term = null;
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
            NewTimeDepositTO objNewTimeDepositTO =
            (NewTimeDepositTO) ((List) mapData.get("NewTimeDepositTO")).get(0);
            setNewTimeDepositTO(objNewTimeDepositTO);
        } catch( Exception e ) {
            setResult(ClientConstants.ACTIONTYPE_FAILED);
            parseException.logException(e,true);
            
        }
    }
    
    /** Resets the OB Fields */
    public void resetForm(){
        setTxtMember("");
        setTxtEntitlementGroup("");
        setTxtPortfolioLocation("");
        setTxtPortfolioAssetSubClass("");
        setTxtPortfolioAccount("");
        setTxtSettlementAssetSubClass("");
        setTxtSettlementAccount("");
        setTxtAssetSubClass("");
        setTxtAccount("");
        setTxtPrincipalAssetSubClass("");
        setTxtPrincipalAccount("");
        setTdtExecutionDate("");
        setTdtStartDate("");
        setTxtOrderAmount("");
        setTxtSpread("");
        setCboProductType("");
        setCboSettlementType("");
        setTxtTenor1("");
        setTxtTenor2("");
        setTdtMaturityDate("");
        setTxtAutorollInd("");
        setRdoPhoneOrder_Yes(false);
        setRdoPhoneOrder_No(false);
        setTxtClientRate("");
        setTxtBankOfficeInstruction("");
        setTxtTraderDealerInst("");
        setTxtCreditNotes("");
        setTxtClientAdvices("");
        notifyObservers();
        
    }
}
