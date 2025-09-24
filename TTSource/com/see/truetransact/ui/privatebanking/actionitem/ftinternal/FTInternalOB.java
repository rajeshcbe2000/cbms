/**
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition.
 * 
 *
 * ActionItemOB.java
 *
 * Created on June 18, 2004, 4:48 PM
 */

package com.see.truetransact.ui.privatebanking.actionitem.ftinternal;

/**
 *
 * @author  Ashok
 */

import java.util.Observable;
import java.util.HashMap;
import java.util.List;
import java.util.Date;

import com.see.truetransact.clientproxy.ProxyFactory;
import com.see.truetransact.clientexception.ClientParseException;
import com.see.truetransact.clientutil.ClientConstants;
import com.see.truetransact.clientutil.ClientUtil;
import com.see.truetransact.transferobject.privatebanking.actionitem.ftinternal.FTInternalTO;
import com.see.truetransact.commonutil.DateUtil;
import com.see.truetransact.commonutil.CommonConstants;
import com.see.truetransact.commonutil.CommonUtil;
import org.apache.log4j.Logger;
import com.see.truetransact.ui.TrueTransactMain;
import com.see.truetransact.uicomponent.CObservable;

public class FTInternalOB extends CObservable {
    
    private String txtMember = "";
    private String lblReferenceNumber = "";
    private String txtCreditEntitlementGroup = "";
    private String txtCreditPortfolioLocation = "";
    private String txtCreditAssetSubClass = "";
    private String txtCreditAccount = "";
    private String tdtExecutionDate = "";
    private String tdtValueDate = "";
    private String txtDebitAmount = "";
    private String txtCreditAmount = "";
    private String txtBankOfficeInstruction = "";
    private String txtTraderDealerInst = "";
    private String txtClientAdvices = "";
    private String txtCreditNotes = "";
    private String txtDebitEntitlementGroup = "";
    private String txtDebitPortfolioLocation = "";
    private String txtDebitAssetSubClass = "";
    private String txtDebitAccount = "";
    private int _result,_actionType;
    private HashMap map;
    private String lblStatus = ClientConstants.ACTION_STATUS[ClientConstants.ACTIONTYPE_CANCEL];
    private ProxyFactory proxy;
    private final static ClientParseException parseException = ClientParseException.getInstance();
    private final static Logger _log = Logger.getLogger(FTInternalOB.class);
    private static FTInternalOB objFTInternalOB;
    Date curDate = null;
    
    /** Creates a new instance of ActionItemOB */
    private FTInternalOB() {
        curDate = ClientUtil.getCurrentDate();
        map = new HashMap();
        map.put(CommonConstants.JNDI, "FTInternalJNDI");
        map.put(CommonConstants.HOME, "serverside.privatebanking.actionitem.ftinternal.FTInternalHome");
        map.put(CommonConstants.REMOTE, "serverside.privatebanking.actionitem.ftinternal.FTInternal");
        try {
            proxy = ProxyFactory.createProxy();
        } catch (Exception e) {
            parseException.logException(e,true);
        }
    }
    
    static {
        try {
            _log.info("Creating BillsOB...");
            objFTInternalOB= new FTInternalOB();
        } catch(Exception e) {
            parseException.logException(e,true);
        }
    }
    
    /** This method Returns an instance of this class **/
    public static FTInternalOB getInstance()throws Exception{
        return objFTInternalOB;
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
    
    /**
     * Getter for property txtMember.
     * @return Value of property txtMember.
     */
    public java.lang.String getTxtMember() {
        return txtMember;
    }
    
    /**
     * Setter for property txtMember.
     * @param txtMember New value of property txtMember.
     */
    public void setTxtMember(java.lang.String txtMember) {
        this.txtMember = txtMember;
    }
    
    // Setter method for txtCreditEntitlementGroup
    void setTxtCreditEntitlementGroup(String txtCreditEntitlementGroup){
        this.txtCreditEntitlementGroup = txtCreditEntitlementGroup;
        setChanged();
    }
    // Getter method for txtCreditEntitlementGroup
    String getTxtCreditEntitlementGroup(){
        return this.txtCreditEntitlementGroup;
    }
    
    // Setter method for txtCreditPortfolioLocation
    void setTxtCreditPortfolioLocation(String txtCreditPortfolioLocation){
        this.txtCreditPortfolioLocation = txtCreditPortfolioLocation;
        setChanged();
    }
    // Getter method for txtCreditPortfolioLocation
    String getTxtCreditPortfolioLocation(){
        return this.txtCreditPortfolioLocation;
    }
    
    // Setter method for txtCreditAssetSubClass
    void setTxtCreditAssetSubClass(String txtCreditAssetSubClass){
        this.txtCreditAssetSubClass = txtCreditAssetSubClass;
        setChanged();
    }
    // Getter method for txtCreditAssetSubClass
    String getTxtCreditAssetSubClass(){
        return this.txtCreditAssetSubClass;
    }
    
    // Setter method for txtCreditAccount
    void setTxtCreditAccount(String txtCreditAccount){
        this.txtCreditAccount = txtCreditAccount;
        setChanged();
    }
    // Getter method for txtCreditAccount
    String getTxtCreditAccount(){
        return this.txtCreditAccount;
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
    
    // Setter method for tdtValueDate
    void setTdtValueDate(String tdtValueDate){
        this.tdtValueDate = tdtValueDate;
        setChanged();
    }
    // Getter method for tdtValueDate
    String getTdtValueDate(){
        return this.tdtValueDate;
    }
    
    // Setter method for txtDebitAmount
    void setTxtDebitAmount(String txtDebitAmount){
        this.txtDebitAmount = txtDebitAmount;
        setChanged();
    }
    // Getter method for txtDebitAmount
    String getTxtDebitAmount(){
        return this.txtDebitAmount;
    }
    
    // Setter method for txtCreditAmount
    void setTxtCreditAmount(String txtCreditAmount){
        this.txtCreditAmount = txtCreditAmount;
        setChanged();
    }
    // Getter method for txtCreditAmount
    String getTxtCreditAmount(){
        return this.txtCreditAmount;
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
    
    // Setter method for txtClientAdvices
    void setTxtClientAdvices(String txtClientAdvices){
        this.txtClientAdvices = txtClientAdvices;
        setChanged();
    }
    // Getter method for txtClientAdvices
    String getTxtClientAdvices(){
        return this.txtClientAdvices;
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
    
    // Setter method for txtDebitEntitlementGroup
    void setTxtDebitEntitlementGroup(String txtDebitEntitlementGroup){
        this.txtDebitEntitlementGroup = txtDebitEntitlementGroup;
        setChanged();
    }
    // Getter method for txtDebitEntitlementGroup
    String getTxtDebitEntitlementGroup(){
        return this.txtDebitEntitlementGroup;
    }
    
    // Setter method for txtDebtiPortfolioLocation
    void setTxtDebitPortfolioLocation(String txtDebitPortfolioLocation){
        this.txtDebitPortfolioLocation = txtDebitPortfolioLocation;
        setChanged();
    }
    // Getter method for txtDebtiPortfolioLocation
    String getTxtDebitPortfolioLocation(){
        return this.txtDebitPortfolioLocation;
    }
    
    // Setter method for txtDebitAssetSubClass
    void setTxtDebitAssetSubClass(String txtDebitAssetSubClass){
        this.txtDebitAssetSubClass = txtDebitAssetSubClass;
        setChanged();
    }
    // Getter method for txtDebitAssetSubClass
    String getTxtDebitAssetSubClass(){
        return this.txtDebitAssetSubClass;
    }
    
    // Setter method for txtDebitAccount
    void setTxtDebitAccount(String txtDebitAccount){
        this.txtDebitAccount = txtDebitAccount;
        setChanged();
    }
    // Getter method for txtDebitAccount
    String getTxtDebitAccount(){
        return this.txtDebitAccount;
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
    
    /** Resets the ui Fields after each operation in the ui */
    public void resetForm(){
        setTxtMember("");
        setTxtCreditEntitlementGroup("");
        setTxtCreditPortfolioLocation("");
        setTxtCreditAssetSubClass("");
        setTxtCreditAccount("");
        setTdtExecutionDate("");
        setTdtValueDate("");
        setTxtDebitAmount("");
        setTxtCreditAmount("");
        setTxtBankOfficeInstruction("");
        setTxtTraderDealerInst("");
        setTxtClientAdvices("");
        setTxtCreditNotes("");
        setTxtDebitEntitlementGroup("");
        setTxtDebitPortfolioLocation("");
        setTxtDebitAssetSubClass("");
        setTxtDebitAccount("");
        notifyObservers();
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
    
    /** Returns an Instance of FTInternalTO whose fields r filled up using the OB methods */
    private FTInternalTO getFTInternalTO(String command){
        FTInternalTO objFTInternalTO = new FTInternalTO();
        objFTInternalTO.setCommand(command);
        if(objFTInternalTO.getCommand().equals(CommonConstants.TOSTATUS_INSERT)){
            objFTInternalTO.setStatus(CommonConstants.STATUS_CREATED);
        }else{
            objFTInternalTO.setStatusBy(TrueTransactMain.USER_ID);
        }
        objFTInternalTO.setRefNo(getLblReferenceNumber());
        objFTInternalTO.setDrEntitleGrp(getTxtDebitEntitlementGroup());
        objFTInternalTO.setDrPortfolioLoc(getTxtDebitPortfolioLocation());
        objFTInternalTO.setDrAssetSubclass(getTxtDebitAssetSubClass());
        objFTInternalTO.setDrAcct(getTxtDebitAccount());
        objFTInternalTO.setCrEntitleGrp(getTxtCreditEntitlementGroup());
        objFTInternalTO.setCrPortfolioLoc(getTxtCreditPortfolioLocation());
        objFTInternalTO.setCrAssetSubclass(getTxtCreditAssetSubClass());
        objFTInternalTO.setCrAcct(getTxtCreditAccount());
        
        Date ExDt = DateUtil.getDateMMDDYYYY(getTdtExecutionDate());
        if(ExDt != null){
        Date exDate = (Date) curDate.clone();
        exDate.setDate(ExDt.getDate());
        exDate.setMonth(ExDt.getMonth());
        exDate.setYear(ExDt.getYear());
//        objFTInternalTO.setOrdExecDt(DateUtil.getDateMMDDYYYY(getTdtExecutionDate()));
        objFTInternalTO.setOrdExecDt(exDate);
        }else{
            
        }
       
        Date ValDt = DateUtil.getDateMMDDYYYY(getTdtValueDate());
        if(ValDt != null){
        Date valDate = (Date) curDate.clone();
        valDate.setDate(ValDt.getDate());
        valDate.setMonth(ValDt.getMonth());
        valDate.setYear(ValDt.getYear());
//        objFTInternalTO.setOrdValueDt(DateUtil.getDateMMDDYYYY(getTdtValueDate()));
        objFTInternalTO.setOrdValueDt(valDate);
        }else{
            
        }
        
        objFTInternalTO.setDrAmount(CommonUtil.convertObjToDouble(getTxtDebitAmount()));
        objFTInternalTO.setCrAmount(CommonUtil.convertObjToDouble(getTxtCreditAmount()));
        objFTInternalTO.setBankOfficeInstruct(getTxtBankOfficeInstruction());
        objFTInternalTO.setTraderInstruct(getTxtTraderDealerInst());
        objFTInternalTO.setClientAdvcies(getTxtClientAdvices());
        objFTInternalTO.setCreditNotes(getTxtCreditNotes());
        objFTInternalTO.setMemberId(getTxtMember());
        return objFTInternalTO;
    }
    
    /** This method fills up the ob fields using the getters of TO */
    private void setFTInternalTO(FTInternalTO objFTInternalTO){
        setLblReferenceNumber(objFTInternalTO.getRefNo());
        setTxtDebitEntitlementGroup(objFTInternalTO.getDrEntitleGrp());
        setTxtDebitPortfolioLocation(objFTInternalTO.getDrPortfolioLoc());
        setTxtDebitAssetSubClass(objFTInternalTO.getDrAssetSubclass());
        setTxtDebitAccount(objFTInternalTO.getDrAcct());
        setTxtCreditEntitlementGroup(objFTInternalTO.getCrEntitleGrp());
        setTxtCreditPortfolioLocation(objFTInternalTO.getCrPortfolioLoc());
        setTxtCreditAssetSubClass(objFTInternalTO.getCrAssetSubclass());
        setTxtCreditAccount(objFTInternalTO.getCrAcct());
        setTdtExecutionDate(DateUtil.getStringDate(objFTInternalTO.getOrdExecDt()));
        setTdtValueDate(DateUtil.getStringDate(objFTInternalTO.getOrdValueDt()));
        setTxtDebitAmount(CommonUtil.convertObjToStr(objFTInternalTO.getDrAmount()));
        setTxtCreditAmount(CommonUtil.convertObjToStr(objFTInternalTO.getCrAmount()));
        setTxtBankOfficeInstruction(objFTInternalTO.getBankOfficeInstruct());
        setTxtTraderDealerInst(objFTInternalTO.getTraderInstruct());
        setTxtClientAdvices(objFTInternalTO.getClientAdvcies());
        setTxtCreditNotes(objFTInternalTO.getCreditNotes());
        setTxtMember(objFTInternalTO.getMemberId());
        notifyObservers();
        
    }
    /* Executes Query using the TO object */
    public void execute(String command) {
        try {
            HashMap term = new HashMap();
            term.put("FTInternalTO", getFTInternalTO(command));
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
            FTInternalTO objFTInternalTO =
            (FTInternalTO) ((List) mapData.get("FTInternalTO")).get(0);
            setFTInternalTO(objFTInternalTO);
        } catch( Exception e ) {
            setResult(ClientConstants.ACTIONTYPE_FAILED);
            parseException.logException(e,true);
            
        }
    }
    
    
    
}
