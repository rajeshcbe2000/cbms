/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition.
 **
 *
 * ShareProductOB.java
 *
 * Created on Fri Jan 07 12:01:51 IST 2005
 */

package com.see.truetransact.ui.investments;

import com.see.truetransact.uicomponent.CObservable;
import com.see.truetransact.clientexception.ClientParseException;
import com.see.truetransact.commonutil.CommonConstants;
import com.see.truetransact.clientutil.ClientConstants;
import com.see.truetransact.commonutil.DateUtil;
import com.see.truetransact.clientproxy.ProxyFactory;
import com.see.truetransact.clientproxy.ProxyParameters;
import com.see.truetransact.clientutil.ComboBoxModel;
import com.see.truetransact.clientutil.ClientUtil;
import com.see.truetransact.transferobject.investments.InvestmentsMasterTO;
import com.see.truetransact.transferobject.investments.InvestmentsTransTO;
import com.see.truetransact.transferobject.investments.InvestmentsAmortizationTO;
import com.see.truetransact.transferobject.investments.InvestmentsAmortizationCalculationTO;
import com.see.truetransact.ui.TrueTransactMain;
import com.see.truetransact.commonutil.CommonUtil;
import com.see.truetransact.ui.common.transaction.TransactionOB;
import com.see.truetransact.transferobject.transaction.transfer.TxTransferTO;

import org.apache.log4j.Logger;

import java.util.HashMap;
import java.util.ArrayList;
import java.util.List;
import java.util.Date;
import java.util.ResourceBundle;
import java.util.LinkedHashMap;
import com.see.truetransact.clientutil.EnhancedTableModel;
import com.see.truetransact.ui.deposit.TableManipulation;
/**
 *
 * @author Ashok Vijayakumar
 */

public class InvestmentsAmortizationOB extends CObservable{
    
    Date curDate = ClientUtil.getCurrentDate();
    private final static ClientParseException parseException = ClientParseException.getInstance();
    private final static Logger _log = Logger.getLogger(InvestmentsMasterOB.class);
    private ResourceBundle resourceBundle = java.util.ResourceBundle.getBundle("com.see.truetransact.ui.investments.InvestmentsAmortizationRB", ProxyParameters.LANGUAGE);
    private ProxyFactory proxy = null;
    private ComboBoxModel  cbmInvestmentBehaves,cbmIntPayFreq;
    private HashMap map,lookUpHash,keyValue,oldAmountMap;
    private int _result,_actionType;
    private ArrayList key,value ,tblnvestmentAmortizationDetColTitle ;
    private final String NOT_DELETED_TRANS_TOs = "NOT_DELETED_TRANS_TOs";
    private static InvestmentsAmortizationOB objInvestmentsAmortizationOB;//Singleton Object
    //     private static InvestmentsMasterOB objInvestmentsMasterOB;
    private TransactionOB transactionOB;
    private String lblStatus = ClientConstants.ACTION_STATUS[ClientConstants.ACTIONTYPE_CANCEL];
    private EnhancedTableModel tblInvestmentAmortizationDet;
    TableManipulation objTableManipulation = new TableManipulation();
    private LinkedHashMap transactionDetailsTO ;
    private LinkedHashMap deletedTransactionDetailsTO;
    private LinkedHashMap allowedTransactionDetailsTO ;
    private String cboInvestmentBehaves = "";
    private String cboIntPayFreq="";
    private String  investmentID="";
    private String investmentName="";
    private Date IssueDt=null;
    private Double years=null;
    private Double months=null;
    private Double days=null;
    private Date maturityDate=null;
    private Double faceValue=null;
    private Double couponRate=null;
    private String SLR="";
    private String callOption="";
    private String putOption="";
    private String setUpOption="";
    private Double availableNoOfUnits=null ;
    private Date lastIntPaidDate=null;
    private Double totalPremiumPaid=null;
    private Double totalPremiumCollected=null;
    private Double totalInterestPaid=null;
    private Double totalInterestCollected=null;
    private String batch_Id="";
    private String trans_Id="";
    private Date trans_Dt=null;
    private String trans_type="";
    private String tran_Code="";
    private Date purchas_Date=null;
    private String purchase_Mode="" ;
    private String purchse_Through="";
    private String broker_Name="";
    private Double purchase_Rate=null;
    private Double no_Of_Units=null;
    private Double investment_amount=null;
    private Double discount_Amount=null;
    private Double premium_Amount=null;
    private Double broken_Period_Interest=null;
    private Double broken_Commession=null;
    private HashMap authorizeMap;
    private Date initiatedDate=null;
    private Double outstandingAmount=null;
    private Double maturityAmount=null;
    private String classification="";
    private Date shiftingDate=null;
    private Double valuationRate=null;
    private String oldClassfication="";
    private String newClassfication="";
    
    
    
    
    /** Creates a new instance of ShareProductOB */
    private InvestmentsAmortizationOB()    {
        map = new HashMap();
        map.put(CommonConstants.JNDI, "InvestmentsAmortizationJNDI");
        map.put(CommonConstants.HOME, "serverside.investments.InvestmentsAmortizationHome");
        map.put(CommonConstants.REMOTE, "serverside.investments.InvestmentsAmortization");
        try {
            proxy = ProxyFactory.createProxy();
            initUIComboBoxModel();
            fillDropdown();
            settblnvestmentAmortizationDetColTitleCol();
            tblInvestmentAmortizationDet = new EnhancedTableModel(null, tblnvestmentAmortizationDetColTitle);
            
        } catch (Exception e) {
            parseException.logException(e,true);
        }
    }
    
    static {
        try {
            _log.info("Creating InvestmentsMasterOB...");
            objInvestmentsAmortizationOB= new InvestmentsAmortizationOB();
        } catch(Exception e) {
            parseException.logException(e,true);
        }
    }
    
    /** Creating Instances of ComboBoxModel */
    private void initUIComboBoxModel(){
        cbmInvestmentBehaves=new ComboBoxModel();
    }
    
    /* Filling up the the ComboBoxModel with key, value */
    private void fillDropdown() throws Exception{
        try{
            _log.info("Inside FillDropDown");
            lookUpHash = new HashMap();
            lookUpHash.put(CommonConstants.MAP_NAME, null);
            final ArrayList lookup_keys = new ArrayList();
            //            lookup_keys.add("INVESTMENT");
            lookup_keys.add("DEPOSITSPRODUCT.DEPOSITPERIOD");
            
            lookUpHash.put(CommonConstants.PARAMFORQUERY, lookup_keys);
            keyValue = ClientUtil.populateLookupData(lookUpHash);
            
            //            getKeyValue((HashMap)keyValue.get("INVESTMENT"));
            getKeyValue((HashMap)keyValue.get("DEPOSITSPRODUCT.DEPOSITPERIOD"));
            cbmIntPayFreq=new ComboBoxModel(key,value);
            key = new ArrayList();
            value= new ArrayList();
            key.add("");
            value.add("");
            key.add("BONDS");
            value.add("Bonds");
            cbmInvestmentBehaves = new ComboBoxModel(key,value);            
        }catch(NullPointerException e){
            parseException.logException(e,true);
        }catch(Exception e){
            parseException.logException(e,true);
        }
    }
    
    /* Return the key,value(Array List) to be used up in ComboBoxModel */
    private void getKeyValue(HashMap keyValue)  throws Exception{
        key = (ArrayList)keyValue.get(CommonConstants.KEY);
        value = (ArrayList)keyValue.get(CommonConstants.VALUE);
    }
    /**
     * Returns an instance of ShareProductOB.
     * @return  ShareProductOB
     */
    
    public static InvestmentsAmortizationOB getInstance()throws Exception{
        return objInvestmentsAmortizationOB;
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
        setChanged();
    }
    
    public void resetStatus(){
        this.setLblStatus(ClientConstants.ACTION_STATUS[0]);
    }
    
    public int getResult(){
        return _result;
    }
    
    /** Returns an Instance of InvestmentMaster */
    public InvestmentsMasterTO getInvestmentsMasterTO(String command){
        InvestmentsMasterTO objgetInvestmentsMasterTO= new InvestmentsMasterTO();
        final String yes="Y";
        final String no="N";
        objgetInvestmentsMasterTO.setCommand(command);
        if(objgetInvestmentsMasterTO.getCommand().equalsIgnoreCase(CommonConstants.TOSTATUS_INSERT)){
            objgetInvestmentsMasterTO.setStatus(CommonConstants.STATUS_CREATED);
        }else  if(objgetInvestmentsMasterTO.getCommand().equalsIgnoreCase(CommonConstants.TOSTATUS_UPDATE)){
            objgetInvestmentsMasterTO.setStatus(CommonConstants.STATUS_MODIFIED);
        }else if(objgetInvestmentsMasterTO.getCommand().equalsIgnoreCase(CommonConstants.TOSTATUS_DELETE)){
            objgetInvestmentsMasterTO.setStatus(CommonConstants.STATUS_DELETED);
        }
        objgetInvestmentsMasterTO.setCboInvestmentBehaves(CommonUtil.convertObjToStr(cbmInvestmentBehaves.getKeyForSelected()));
        objgetInvestmentsMasterTO.setCboIntPayFreq(CommonUtil.convertObjToStr(cbmIntPayFreq.getKeyForSelected()));
        objgetInvestmentsMasterTO.setInvestmentID(CommonUtil.convertObjToStr(getInvestmentID()));
        objgetInvestmentsMasterTO.setInvestmentName(CommonUtil.convertObjToStr(getInvestmentName()));
        objgetInvestmentsMasterTO.setInvestmentName(CommonUtil.convertObjToStr(getInvestmentName()));
        objgetInvestmentsMasterTO.setIssueDt(getIssueDt());
        objgetInvestmentsMasterTO.setYears(getYears());
        objgetInvestmentsMasterTO.setMonths(getMonths());
        objgetInvestmentsMasterTO.setDays(getDays());
        objgetInvestmentsMasterTO.setMaturityDate(getMaturityDate());
        objgetInvestmentsMasterTO.setFaceValue(getFaceValue());
        objgetInvestmentsMasterTO.setCouponRate(getCouponRate());
        objgetInvestmentsMasterTO.setSLR(CommonUtil.convertObjToStr(getSLR()));
        objgetInvestmentsMasterTO.setCallOption(CommonUtil.convertObjToStr(getCallOption()));
        objgetInvestmentsMasterTO.setPutOption(CommonUtil.convertObjToStr(getPutOption()));
        objgetInvestmentsMasterTO.setSetUpOption(CommonUtil.convertObjToStr(getSetUpOption()));
        objgetInvestmentsMasterTO.setStatusBy(TrueTransactMain.USER_ID);
        objgetInvestmentsMasterTO.setStatusDt(curDate);
        objgetInvestmentsMasterTO.setLastIntPaidDate(getLastIntPaidDate());
        objgetInvestmentsMasterTO.setAvailableNoOfUnits(getAvailableNoOfUnits());
        objgetInvestmentsMasterTO.setTotalPremiumCollected(getTotalPremiumCollected());
        objgetInvestmentsMasterTO.setTotalPremiumPaid(getTotalPremiumPaid());
        objgetInvestmentsMasterTO.setTotalInterestPaid(getTotalInterestPaid());
        objgetInvestmentsMasterTO.setTotalInterestCollected(getTotalInterestCollected());
        objgetInvestmentsMasterTO.setOutstandingAmount(getOutstandingAmount());
        objgetInvestmentsMasterTO.setMaturityAmount(getMaturityAmount());
        objgetInvestmentsMasterTO.setInitiatedDate(getInitiatedDate());
        objgetInvestmentsMasterTO.setClassification(getClassification());
        return objgetInvestmentsMasterTO;
        
    }
    
    public InvestmentsTransTO getInvestmentsTransTO(String command){
        InvestmentsTransTO objgetInvestmentsTransTO= new InvestmentsTransTO();
        final String yes="Y";
        final String no="N";
        objgetInvestmentsTransTO.setCommand(command);
        if(objgetInvestmentsTransTO.getCommand().equalsIgnoreCase(CommonConstants.TOSTATUS_INSERT)){
            objgetInvestmentsTransTO.setStatus(CommonConstants.STATUS_CREATED);
        }else  if(objgetInvestmentsTransTO.getCommand().equalsIgnoreCase(CommonConstants.TOSTATUS_UPDATE)){
            objgetInvestmentsTransTO.setStatus(CommonConstants.STATUS_MODIFIED);
        }else if(objgetInvestmentsTransTO.getCommand().equalsIgnoreCase(CommonConstants.TOSTATUS_DELETE)){
            objgetInvestmentsTransTO.setStatus(CommonConstants.STATUS_DELETED);
        }
        objgetInvestmentsTransTO.setInvestmentBehaves(CommonUtil.convertObjToStr(cbmInvestmentBehaves.getKeyForSelected()));
        //        objgetInvestmentsTransTO.setInvestmentBehaves(CommonUtil.convertObjToStr(getCboInvestmentBehaves()));
        objgetInvestmentsTransTO.setInvestmentID(CommonUtil.convertObjToStr(getInvestmentID()));
        objgetInvestmentsTransTO.setInvestmentName(CommonUtil.convertObjToStr(getInvestmentName()));
        objgetInvestmentsTransTO.setBatchID(CommonUtil.convertObjToStr(getBatch_Id()));
        objgetInvestmentsTransTO.setTransDT(getTrans_Dt());
        objgetInvestmentsTransTO.setTransType(CommonUtil.convertObjToStr(getTrans_type()));
        objgetInvestmentsTransTO.setTrnCode(getTran_Code());//CommonUtil.convertObjToStr(
        objgetInvestmentsTransTO.setAmount(new Double(0.0));
        objgetInvestmentsTransTO.setPurchaseDt(getPurchas_Date());
        objgetInvestmentsTransTO.setPurchaseMode(CommonUtil.convertObjToStr(getPurchase_Mode()));
        objgetInvestmentsTransTO.setPurchaseThrough(CommonUtil.convertObjToStr(getPurchse_Through()));
        objgetInvestmentsTransTO.setBrokerName(CommonUtil.convertObjToStr(getBroker_Name()));
        objgetInvestmentsTransTO.setPurchaseRate(getPurchase_Rate());
        objgetInvestmentsTransTO.setNoOfUnits(getNo_Of_Units());
        objgetInvestmentsTransTO.setInvestmentAmount(getInvestment_amount());
        objgetInvestmentsTransTO.setDiscountAmount(getDiscount_Amount());
        objgetInvestmentsTransTO.setPremiumAmount(getPremium_Amount());
        objgetInvestmentsTransTO.setBrokenPeriodInterest(getBroken_Period_Interest());
        objgetInvestmentsTransTO.setBrokerCommession(getBroken_Commession());
        objgetInvestmentsTransTO.setStatusBy(TrueTransactMain.USER_ID);
        objgetInvestmentsTransTO.setStatusDt(curDate);
        objgetInvestmentsTransTO.setDividendAmount(new Double(0));
        return objgetInvestmentsTransTO;
        
        
    }
    /** Sets all the InvsetmentMaster values to the OB varibles  there by populatin the UI fields */
    private void setInvestmentsMasterTO(InvestmentsMasterTO objInvestmentsMasterTO){
        setCboInvestmentBehaves(CommonUtil.convertObjToStr(getCbmInvestmentBehaves().getDataForKey(objInvestmentsMasterTO.getCboInvestmentBehaves())));
        setCboIntPayFreq(CommonUtil.convertObjToStr(getCbmIntPayFreq().getDataForKey(objInvestmentsMasterTO.getCboIntPayFreq())));
        setInvestmentID(CommonUtil.convertObjToStr(objInvestmentsMasterTO.getInvestmentID()));
        setInvestmentName(CommonUtil.convertObjToStr(objInvestmentsMasterTO.getInvestmentName()));
        setIssueDt(objInvestmentsMasterTO.getIssueDt());
        setYears(objInvestmentsMasterTO.getYears());
        setMonths(objInvestmentsMasterTO.getMonths());
        setDays(objInvestmentsMasterTO.getDays());
        setMaturityDate(objInvestmentsMasterTO.getMaturityDate());
        setFaceValue(objInvestmentsMasterTO.getFaceValue());
        setCouponRate(objInvestmentsMasterTO.getCouponRate());
        setSLR(CommonUtil.convertObjToStr(objInvestmentsMasterTO.getSLR()));
        setCallOption(CommonUtil.convertObjToStr(objInvestmentsMasterTO.getCallOption()));
        setPutOption(CommonUtil.convertObjToStr(objInvestmentsMasterTO.getPutOption()));
        setSetUpOption(CommonUtil.convertObjToStr(objInvestmentsMasterTO.getSetUpOption()));
        setLastIntPaidDate(objInvestmentsMasterTO.getLastIntPaidDate());
        setAvailableNoOfUnits(CommonUtil.convertObjToDouble(objInvestmentsMasterTO.getAvailableNoOfUnits()));
        setTotalInterestCollected(CommonUtil.convertObjToDouble(objInvestmentsMasterTO.getTotalInterestCollected()));
        setTotalInterestPaid(CommonUtil.convertObjToDouble(objInvestmentsMasterTO.getTotalInterestPaid()));
        setTotalPremiumPaid(CommonUtil.convertObjToDouble(objInvestmentsMasterTO.getTotalPremiumPaid()));
        setTotalPremiumCollected(CommonUtil.convertObjToDouble(objInvestmentsMasterTO.getTotalPremiumCollected()));
        setMaturityAmount(objInvestmentsMasterTO.getMaturityAmount());
        setOutstandingAmount(objInvestmentsMasterTO.getOutstandingAmount());
        setClassification(objInvestmentsMasterTO.getClassification());
        setChanged();
        notifyObservers();
    }
    
    private void setInvestmentsTransTO(InvestmentsTransTO objInvestmentsTransTO){
        setPurchas_Date(DateUtil.getDateMMDDYYYY(DateUtil.getStringDate(objInvestmentsTransTO.getPurchaseDt())));
        setPurchase_Mode(CommonUtil.convertObjToStr(objInvestmentsTransTO.getPurchaseMode()));
        setPurchse_Through(CommonUtil.convertObjToStr(objInvestmentsTransTO.getPurchaseThrough()));
        setBatch_Id(CommonUtil.convertObjToStr(objInvestmentsTransTO.getBatchID()));
        setBroker_Name(CommonUtil.convertObjToStr(objInvestmentsTransTO.getBrokerName()));
        setPurchase_Rate(CommonUtil.convertObjToDouble(objInvestmentsTransTO.getPurchaseRate()));
        setNo_Of_Units(CommonUtil.convertObjToDouble(objInvestmentsTransTO.getNoOfUnits()));
        setDiscount_Amount(CommonUtil.convertObjToDouble(objInvestmentsTransTO.getDiscountAmount()));
        setPremium_Amount(CommonUtil.convertObjToDouble(objInvestmentsTransTO.getPremiumAmount()));
        setBroken_Period_Interest(CommonUtil.convertObjToDouble(objInvestmentsTransTO.getBrokenPeriodInterest()));
        setBroken_Commession(CommonUtil.convertObjToDouble(objInvestmentsTransTO.getBrokerCommession()));
        setInvestment_amount(objInvestmentsTransTO.getInvestmentAmount());
        setTran_Code(CommonUtil.convertObjToStr(objInvestmentsTransTO.getTrnCode()));
        
        setChanged();
        notifyObservers();
    }
    
    private void setInvestmentsAmortizationTO(InvestmentsAmortizationTO  objInvestmentsAmortizationTO){
        setShiftingDate(objInvestmentsAmortizationTO.getShiftingDate());
        setValuationRate(objInvestmentsAmortizationTO.getValuationRate());
        setNewClassfication(objInvestmentsAmortizationTO.getNewClassfication());
        setChanged();
        notifyObservers();
    }
    
    public InvestmentsAmortizationTO getInvestmentsAmortizationTO(String command){
        InvestmentsAmortizationTO objInvestmentsAmortizationTO=new InvestmentsAmortizationTO();
        final String yes="Y";
        final String no="N";
        objInvestmentsAmortizationTO.setCommand(command);
        if(objInvestmentsAmortizationTO.getCommand().equalsIgnoreCase(CommonConstants.TOSTATUS_INSERT)){
            objInvestmentsAmortizationTO.setStatus(CommonConstants.STATUS_CREATED);
        }else  if(objInvestmentsAmortizationTO.getCommand().equalsIgnoreCase(CommonConstants.TOSTATUS_UPDATE)){
            objInvestmentsAmortizationTO.setStatus(CommonConstants.STATUS_MODIFIED);
        }else if(objInvestmentsAmortizationTO.getCommand().equalsIgnoreCase(CommonConstants.TOSTATUS_DELETE)){
            objInvestmentsAmortizationTO.setStatus(CommonConstants.STATUS_DELETED);
        }
        objInvestmentsAmortizationTO.setStatusBy(TrueTransactMain.USER_ID);
        objInvestmentsAmortizationTO.setStatusDt(curDate);
        objInvestmentsAmortizationTO.setInvestmentBehaves(CommonUtil.convertObjToStr(cbmInvestmentBehaves.getKeyForSelected()));
        //        objgetInvestmentsTransTO.setInvestmentBehaves(CommonUtil.convertObjToStr(getCboInvestmentBehaves()));
        objInvestmentsAmortizationTO.setInvestmentID(CommonUtil.convertObjToStr(getInvestmentID()));
        objInvestmentsAmortizationTO.setInvestmentName(CommonUtil.convertObjToStr(getInvestmentName()));
        objInvestmentsAmortizationTO.setValuationRate(getValuationRate());
        objInvestmentsAmortizationTO.setShiftingDate(getShiftingDate());
        objInvestmentsAmortizationTO.setBatchID(getBatch_Id());
        objInvestmentsAmortizationTO.setOldClassfication(getOldClassfication());
        objInvestmentsAmortizationTO.setNewClassfication(getNewClassfication());
        return objInvestmentsAmortizationTO;
    }
    /* Executes Query using the TO object */
    public void execute(String command) {
        try {
            HashMap term = new HashMap();
            int countRec=0;
            HashMap proxyResultMap=null;
            if(!command.equals("AUTHORIZE")){
                
                term.put(CommonConstants.BRANCH_ID, TrueTransactMain.BRANCH_ID);
                term.put(CommonConstants.MODULE, getModule());
                term.put(CommonConstants.SCREEN, getScreen());
                term.put(CommonConstants.USER_ID, TrueTransactMain.USER_ID);
                if(getTran_Code().equals("Shifting")){
                    term.put("SHIFTING","SHIFTING");
                    term.put("InvestmentsMasterTO", getInvestmentsMasterTO(command));
                    term.put("InvestmentsTransTO", getInvestmentsTransTO(command));
                    term.put("InvestmentsAmortizationTO",getInvestmentsAmortizationTO(command));
                    transactionDetailsTO=new LinkedHashMap();
                    transactionDetailsTO.put(NOT_DELETED_TRANS_TOs,allowedTransactionDetailsTO);
                    term.put("TransactionTO",transactionDetailsTO);
                    if(getActionType()==ClientConstants.ACTIONTYPE_EDIT){
                        term.put("OLDAMOUNT",oldAmountMap);
                    }
                }else if(getTran_Code().equals("Amortization")){
                    term.put("AMORTIZATION","AMORTIZATION");
                    term.put("InvestmentsAmortizationCalculationTO",getInvestmentsAmortizationCalculationTO(command));
                    term.put("InvestmentsTransTO", getInvestmentsTransTO(command));
                    
                }
            }else{
                
                term.put(CommonConstants.AUTHORIZEMAP,getAuthorizeMap());
                
            }
            if(countRec==0)
                
                proxyResultMap = proxy.execute(term, map);
            else
                ClientUtil.displayAlert("THIS PRODUCT HAVING ACCOUNT");
            setResult(getActionType());
            
        } catch (Exception e) {
            setResult(ClientConstants.ACTIONTYPE_FAILED);
            parseException.logException(e,true);
        }
    }
    public String callForBehaves(){
        return CommonUtil.convertObjToStr(cbmInvestmentBehaves.getKeyForSelected());
    }
    
    /** Resetting all tbe Fields of UI */
    public  void resetForm(){
        setCboInvestmentBehaves("");
        setCboIntPayFreq("");
        setInvestmentID("");
        setInvestmentName("");
        setIssueDt(null);
        setFaceValue(null);
        setMonths(null);
        setDays(null);
        setCouponRate(null);
        //        setFaceValue(new Double(0).doubleValue());
        setMaturityDate(null);
        setSLR("");
        setCallOption("");
        setSetUpOption("");
        setNo_Of_Units(null);
        setLastIntPaidDate(null);
        setCboIntPayFreq("");
        setTotalInterestCollected(null);
        setTotalInterestPaid(null);
        setTotalPremiumCollected(null);
        setTotalPremiumPaid(null);
        setPurchas_Date(null);
        setPurchase_Mode("");
        setPurchse_Through("");
        setAvailableNoOfUnits(null);
        setInvestment_amount(null);
        setDiscount_Amount(null);
        setBroken_Period_Interest(null);
        setBroken_Commession(null);
        setPremium_Amount(null);
        setTran_Code("");
        oldAmountMap=null;
        setOutstandingAmount(null);
        setPurchas_Date(DateUtil.getDateMMDDYYYY(""));
        setShiftingDate(DateUtil.getDateMMDDYYYY(""));
        setValuationRate(null);
        setClassification("");
        setNewClassfication("");
        setBatch_Id("");
        setChanged();
        notifyObservers();
        
        
    }
    
    private void  settblnvestmentAmortizationDetColTitleCol(){
        tblnvestmentAmortizationDetColTitle = new ArrayList();
        tblnvestmentAmortizationDetColTitle.add("Investment Type");
        tblnvestmentAmortizationDetColTitle.add("Investment ID");
        tblnvestmentAmortizationDetColTitle.add("Investment Name");
        tblnvestmentAmortizationDetColTitle.add("AmortizationUpTo");
        tblnvestmentAmortizationDetColTitle.add("Premium Amount");
        tblnvestmentAmortizationDetColTitle.add("AmortizationAmount");
    }
    /** This checks whether user entered sharetype already exists if it exists
     * it returns true otherwise false */
    public boolean isInvsetMentMasterTypeExists(String InvestmentName){
        boolean exists = false;
        
        ArrayList resultList =(ArrayList) ClientUtil.executeQuery("getSelectInvestmentShifting",null);
        if(resultList != null){
            for(int i=0; i<resultList.size(); i++){
                HashMap resultMap = (HashMap)resultList.get(i);
                String investProdType =CommonUtil.convertObjToStr(resultMap.get("INVESTMENT_NAME"));
                if(investProdType.equalsIgnoreCase(InvestmentName)){
                    exists = true;
                    break;
                }
            }
        }
        return exists;
    }
    
    /* Populates the TO object by executing a Query */
    public void populateData(HashMap whereMap) {
        HashMap mapData=null;
        try {
            
            mapData = proxy.executeQuery(whereMap, map);
            System.out.println("mapData------------->"+mapData);
            if(!whereMap.containsKey("AMORTIZATIONCALC")){
                InvestmentsMasterTO objInvestmentsMasterTO =
                (InvestmentsMasterTO) ((List)((HashMap) mapData.get("InvestmentsMasterTO")).get("InvestmentsMasterTO")).get(0);
                setInvestmentsMasterTO(objInvestmentsMasterTO);
                if(!whereMap.containsKey("MASTER")){
                    
                    InvestmentsAmortizationTO objInvestmentsAmortizationTO=
                    (InvestmentsAmortizationTO) ((List)((HashMap) mapData.get("InvestmentsAmortizationTO")).get("InvestmentsAmortizationTO")).get(0);
                    
                    setInvestmentsAmortizationTO(objInvestmentsAmortizationTO);
                    if(objInvestmentsAmortizationTO.getBatchID()!=null && !objInvestmentsAmortizationTO.getBatchID().equals("")){
                        InvestmentsTransTO objInvestmentsTransTO =
                        (InvestmentsTransTO) ((List)((HashMap) mapData.get("InvestmentsTransTO")).get("InvestmentsTransTO")).get(0);
                        setInvestmentsTransTO(objInvestmentsTransTO);
                        
                    }
                }
            }else{
                ArrayList invAmrDetList=new ArrayList();
                invAmrDetList=(ArrayList)mapData.get("InvestmentsAmortizationCalculationTO");
                if(invAmrDetList!=null && invAmrDetList.size()>0)
                    setInvestmentAmortizationTable(invAmrDetList);
                
                
            }
            
        } catch( Exception e ) {
            setResult(ClientConstants.ACTIONTYPE_FAILED);
            e.printStackTrace();
            parseException.logException(e,true);
            
        }
    }
    private void setInvestmentAmortizationTable(ArrayList invAmrDetList){
        tblInvestmentAmortizationDet=new EnhancedTableModel();
        ArrayList dataList = new ArrayList();
        for(int i=0 ;i<invAmrDetList.size();i++){
            ArrayList invAmrDetRow=new ArrayList();
            InvestmentsAmortizationCalculationTO objInvestmentsAmortizationCalculationTO=new InvestmentsAmortizationCalculationTO();
            objInvestmentsAmortizationCalculationTO=(InvestmentsAmortizationCalculationTO)invAmrDetList.get(i);
            invAmrDetRow.add(0, objInvestmentsAmortizationCalculationTO.getInvestmentBehaves());
            invAmrDetRow.add(1, objInvestmentsAmortizationCalculationTO.getInvestmentID());
            invAmrDetRow.add(2, objInvestmentsAmortizationCalculationTO.getInvestmentName());
            invAmrDetRow.add(3, CommonUtil.convertObjToStr(objInvestmentsAmortizationCalculationTO.getUptoDate()));
            invAmrDetRow.add(4,String.valueOf(objInvestmentsAmortizationCalculationTO.getPremium()));
            invAmrDetRow.add(5, String.valueOf(objInvestmentsAmortizationCalculationTO.getAmortizationAmount()));
            dataList.add(invAmrDetRow);
            invAmrDetRow=null;
        }
        tblInvestmentAmortizationDet.setDataArrayList(dataList, tblnvestmentAmortizationDetColTitle);
        setChanged();
        notifyObservers();
    }
    
    public void resetTable(){
        try{
            ArrayList data = tblInvestmentAmortizationDet.getDataArrayList();
            for(int i=data.size(); i>0; i--)
                tblInvestmentAmortizationDet.removeRow(i-1);
        }catch (Exception e) {
            setResult(ClientConstants.ACTIONTYPE_FAILED);
            //            log.info("Error in resetTable():");
        }
    }
    
    /**
     * Getter for property cbmInvestmentBehaves.
     * @return Value of property cbmInvestmentBehaves.
     */
    public com.see.truetransact.clientutil.ComboBoxModel getCbmInvestmentBehaves() {
        return cbmInvestmentBehaves;
    }
    
    /**
     * Setter for property cbmInvestmentBehaves.
     * @param cbmInvestmentBehaves New value of property cbmInvestmentBehaves.
     */
    public void setCbmInvestmentBehaves(com.see.truetransact.clientutil.ComboBoxModel cbmInvestmentBehaves) {
        this.cbmInvestmentBehaves = cbmInvestmentBehaves;
    }
    
    /**
     * Getter for property cboInvestmentBehaves.
     * @return Value of property cboInvestmentBehaves.
     */
    public java.lang.String getCboInvestmentBehaves() {
        return cboInvestmentBehaves;
    }
    
    /**
     * Setter for property cboInvestmentBehaves.
     * @param cboInvestmentBehaves New value of property cboInvestmentBehaves.
     */
    public void setCboInvestmentBehaves(java.lang.String cboInvestmentBehaves) {
        this.cboInvestmentBehaves = cboInvestmentBehaves;
    }
    
    /**
     * Getter for property cbmIntPayFreq.
     * @return Value of property cbmIntPayFreq.
     */
    public com.see.truetransact.clientutil.ComboBoxModel getCbmIntPayFreq() {
        return cbmIntPayFreq;
    }
    
    /**
     * Setter for property cbmIntPayFreq.
     * @param cbmIntPayFreq New value of property cbmIntPayFreq.
     */
    public void setCbmIntPayFreq(com.see.truetransact.clientutil.ComboBoxModel cbmIntPayFreq) {
        this.cbmIntPayFreq = cbmIntPayFreq;
    }
    
    /**
     * Getter for property IntPayFreq.
     * @return Value of property IntPayFreq.
     */
    
    
    /**
     * Getter for property investmentID.
     * @return Value of property investmentID.
     */
    public java.lang.String getInvestmentID() {
        return investmentID;
    }
    
    /**
     * Setter for property investmentID.
     * @param investmentID New value of property investmentID.
     */
    public void setInvestmentID(java.lang.String investmentID) {
        this.investmentID = investmentID;
    }
    
    /**
     * Getter for property cboIntPayFreq.
     * @return Value of property cboIntPayFreq.
     */
    public java.lang.String getCboIntPayFreq() {
        return cboIntPayFreq;
    }
    
    /**
     * Setter for property cboIntPayFreq.
     * @param cboIntPayFreq New value of property cboIntPayFreq.
     */
    public void setCboIntPayFreq(java.lang.String cboIntPayFreq) {
        this.cboIntPayFreq = cboIntPayFreq;
    }
    
    /**
     * Getter for property investmentName.
     * @return Value of property investmentName.
     */
    public java.lang.String getInvestmentName() {
        return investmentName;
    }
    
    /**
     * Setter for property investmentName.
     * @param investmentName New value of property investmentName.
     */
    public void setInvestmentName(java.lang.String investmentName) {
        this.investmentName = investmentName;
    }
    
    /**
     * Getter for property IssueDt.
     * @return Value of property IssueDt.
     */
    public java.util.Date getIssueDt() {
        return IssueDt;
    }
    
    /**
     * Setter for property IssueDt.
     * @param IssueDt New value of property IssueDt.
     */
    public void setIssueDt(java.util.Date IssueDt) {
        this.IssueDt = IssueDt;
    }
    
    
    
    /**
     * Getter for property months.
     * @return Value of property months.
     */
    public Double getMonths() {
        return months;
    }
    
    /**
     * Setter for property months.
     * @param months New value of property months.
     */
    public void setMonths(Double months) {
        this.months = months;
    }
    
    /**
     * Getter for property days.
     * @return Value of property days.
     */
    public Double getDays() {
        return days;
    }
    
    /**
     * Setter for property days.
     * @param days New value of property days.
     */
    public void setDays(Double days) {
        this.days = days;
    }
    
    /**
     * Getter for property maturityDate.
     * @return Value of property maturityDate.
     */
    public java.util.Date getMaturityDate() {
        return maturityDate;
    }
    
    /**
     * Setter for property maturityDate.
     * @param maturityDate New value of property maturityDate.
     */
    public void setMaturityDate(java.util.Date maturityDate) {
        this.maturityDate = maturityDate;
    }
    
    /**
     * Getter for property faceValue.
     * @return Value of property faceValue.
     */
    public Double getFaceValue() {
        return faceValue;
    }
    
    /**
     * Setter for property faceValue.
     * @param faceValue New value of property faceValue.
     */
    public void setFaceValue(Double faceValue) {
        this.faceValue = faceValue;
    }
    
    /**
     * Getter for property couponRate.
     * @return Value of property couponRate.
     */
    public Double getCouponRate() {
        return couponRate;
    }
    
    /**
     * Setter for property couponRate.
     * @param couponRate New value of property couponRate.
     */
    public void setCouponRate(Double couponRate) {
        this.couponRate = couponRate;
    }
    
    /**
     * Getter for property SLR.
     * @return Value of property SLR.
     */
    public java.lang.String getSLR() {
        return SLR;
    }
    
    /**
     * Setter for property SLR.
     * @param SLR New value of property SLR.
     */
    public void setSLR(java.lang.String SLR) {
        this.SLR = SLR;
    }
    
    /**
     * Getter for property callOption.
     * @return Value of property callOption.
     */
    public java.lang.String getCallOption() {
        return callOption;
    }
    
    /**
     * Setter for property callOption.
     * @param callOption New value of property callOption.
     */
    public void setCallOption(java.lang.String callOption) {
        this.callOption = callOption;
    }
    
    /**
     * Getter for property putOption.
     * @return Value of property putOption.
     */
    public java.lang.String getPutOption() {
        return putOption;
    }
    
    /**
     * Setter for property putOption.
     * @param putOption New value of property putOption.
     */
    public void setPutOption(java.lang.String putOption) {
        this.putOption = putOption;
    }
    
    /**
     * Getter for property setUpOption.
     * @return Value of property setUpOption.
     */
    public java.lang.String getSetUpOption() {
        return setUpOption;
    }
    
    /**
     * Setter for property setUpOption.
     * @param setUpOption New value of property setUpOption.
     */
    public void setSetUpOption(java.lang.String setUpOption) {
        this.setUpOption = setUpOption;
    }
    
    /**
     * Getter for property transactionOB.
     * @return Value of property transactionOB.
     */
    
    public void setTransactionOB(TransactionOB transactionOB) {
        this.transactionOB = transactionOB;
    }
    
    /**
     * Getter for property availableNoOfUnits.
     * @return Value of property availableNoOfUnits.
     */
    public Double getAvailableNoOfUnits() {
        return availableNoOfUnits;
    }
    
    /**
     * Setter for property availableNoOfUnits.
     * @param availableNoOfUnits New value of property availableNoOfUnits.
     */
    public void setAvailableNoOfUnits(Double availableNoOfUnits) {
        this.availableNoOfUnits = availableNoOfUnits;
    }
    
    /**
     * Getter for property lastIntPaidDate.
     * @return Value of property lastIntPaidDate.
     */
    public java.util.Date getLastIntPaidDate() {
        return lastIntPaidDate;
    }
    
    /**
     * Setter for property lastIntPaidDate.
     * @param lastIntPaidDate New value of property lastIntPaidDate.
     */
    public void setLastIntPaidDate(java.util.Date lastIntPaidDate) {
        this.lastIntPaidDate = lastIntPaidDate;
    }
    
    /**
     * Getter for property totalPremiumPaid.
     * @return Value of property totalPremiumPaid.
     */
    public Double getTotalPremiumPaid() {
        return totalPremiumPaid;
    }
    
    /**
     * Setter for property totalPremiumPaid.
     * @param totalPremiumPaid New value of property totalPremiumPaid.
     */
    public void setTotalPremiumPaid(Double totalPremiumPaid) {
        this.totalPremiumPaid = totalPremiumPaid;
    }
    
    /**
     * Getter for property totalPremiumCollected.
     * @return Value of property totalPremiumCollected.
     */
    public Double getTotalPremiumCollected() {
        return totalPremiumCollected;
    }
    
    /**
     * Setter for property totalPremiumCollected.
     * @param totalPremiumCollected New value of property totalPremiumCollected.
     */
    public void setTotalPremiumCollected(Double totalPremiumCollected) {
        this.totalPremiumCollected = totalPremiumCollected;
    }
    
    /**
     * Getter for property totalInterestPaid.
     * @return Value of property totalInterestPaid.
     */
    public Double getTotalInterestPaid() {
        return totalInterestPaid;
    }
    
    /**
     * Setter for property totalInterestPaid.
     * @param totalInterestPaid New value of property totalInterestPaid.
     */
    public void setTotalInterestPaid(Double totalInterestPaid) {
        this.totalInterestPaid = totalInterestPaid;
    }
    
    /**
     * Getter for property totalInterestCollected.
     * @return Value of property totalInterestCollected.
     */
    public Double getTotalInterestCollected() {
        return totalInterestCollected;
    }
    
    /**
     * Setter for property totalInterestCollected.
     * @param totalInterestCollected New value of property totalInterestCollected.
     */
    public void setTotalInterestCollected(Double totalInterestCollected) {
        this.totalInterestCollected = totalInterestCollected;
    }
    
    /**
     * Getter for property batch_Id.
     * @return Value of property batch_Id.
     */
    public java.lang.String getBatch_Id() {
        return batch_Id;
    }
    
    /**
     * Setter for property batch_Id.
     * @param batch_Id New value of property batch_Id.
     */
    public void setBatch_Id(java.lang.String batch_Id) {
        this.batch_Id = batch_Id;
    }
    
    /**
     * Getter for property trans_Id.
     * @return Value of property trans_Id.
     */
    public java.lang.String getTrans_Id() {
        return trans_Id;
    }
    
    /**
     * Setter for property trans_Id.
     * @param trans_Id New value of property trans_Id.
     */
    public void setTrans_Id(java.lang.String trans_Id) {
        this.trans_Id = trans_Id;
    }
    
    /**
     * Getter for property trans_Dt.
     * @return Value of property trans_Dt.
     */
    public java.util.Date getTrans_Dt() {
        return trans_Dt;
    }
    
    /**
     * Setter for property trans_Dt.
     * @param trans_Dt New value of property trans_Dt.
     */
    public void setTrans_Dt(java.util.Date trans_Dt) {
        this.trans_Dt = trans_Dt;
    }
    
    /**
     * Getter for property trans_type.
     * @return Value of property trans_type.
     */
    public java.lang.String getTrans_type() {
        return trans_type;
    }
    
    /**
     * Setter for property trans_type.
     * @param trans_type New value of property trans_type.
     */
    public void setTrans_type(java.lang.String trans_type) {
        this.trans_type = trans_type;
    }
    
    /**
     * Getter for property tran_Code.
     * @return Value of property tran_Code.
     */
    public java.lang.String getTran_Code() {
        return tran_Code;
    }
    
    /**
     * Setter for property tran_Code.
     * @param tran_Code New value of property tran_Code.
     */
    public void setTran_Code(java.lang.String tran_Code) {
        this.tran_Code = tran_Code;
    }
    
    /**
     * Getter for property purchas_Date.
     * @return Value of property purchas_Date.
     */
    public java.util.Date getPurchas_Date() {
        return purchas_Date;
    }
    
    /**
     * Setter for property purchas_Date.
     * @param purchas_Date New value of property purchas_Date.
     */
    public void setPurchas_Date(java.util.Date purchas_Date) {
        this.purchas_Date = purchas_Date;
    }
    
    /**
     * Getter for property purchase_Mode.
     * @return Value of property purchase_Mode.
     */
    public java.lang.String getPurchase_Mode() {
        return purchase_Mode;
    }
    
    /**
     * Setter for property purchase_Mode.
     * @param purchase_Mode New value of property purchase_Mode.
     */
    public void setPurchase_Mode(java.lang.String purchase_Mode) {
        this.purchase_Mode = purchase_Mode;
    }
    
    /**
     * Getter for property purchse_Through.
     * @return Value of property purchse_Through.
     */
    public java.lang.String getPurchse_Through() {
        return purchse_Through;
    }
    
    /**
     * Setter for property purchse_Through.
     * @param purchse_Through New value of property purchse_Through.
     */
    public void setPurchse_Through(java.lang.String purchse_Through) {
        this.purchse_Through = purchse_Through;
    }
    
    /**
     * Getter for property broker_Name.
     * @return Value of property broker_Name.
     */
    public java.lang.String getBroker_Name() {
        return broker_Name;
    }
    
    /**
     * Setter for property broker_Name.
     * @param broker_Name New value of property broker_Name.
     */
    public void setBroker_Name(java.lang.String broker_Name) {
        this.broker_Name = broker_Name;
    }
    
    /**
     * Getter for property no_Of_Units.
     * @return Value of property no_Of_Units.
     */
    public Double getNo_Of_Units() {
        return no_Of_Units;
    }
    
    /**
     * Setter for property no_Of_Units.
     * @param no_Of_Units New value of property no_Of_Units.
     */
    public void setNo_Of_Units(Double no_Of_Units) {
        this.no_Of_Units = no_Of_Units;
    }
    
    /**
     * Getter for property investment_amount.
     * @return Value of property investment_amount.
     */
    public Double getInvestment_amount() {
        return investment_amount;
    }
    
    /**
     * Setter for property investment_amount.
     * @param investment_amount New value of property investment_amount.
     */
    public void setInvestment_amount(Double investment_amount) {
        this.investment_amount = investment_amount;
    }
    
    /**
     * Getter for property discount_Amount.
     * @return Value of property discount_Amount.
     */
    public Double getDiscount_Amount() {
        return discount_Amount;
    }
    
    /**
     * Setter for property discount_Amount.
     * @param discount_Amount New value of property discount_Amount.
     */
    public void setDiscount_Amount(Double discount_Amount) {
        this.discount_Amount = discount_Amount;
    }
    
    /**
     * Getter for property premium_Amount.
     * @return Value of property premium_Amount.
     */
    public Double getPremium_Amount() {
        return premium_Amount;
    }
    
    /**
     * Setter for property premium_Amount.
     * @param premium_Amount New value of property premium_Amount.
     */
    public void setPremium_Amount(Double premium_Amount) {
        this.premium_Amount = premium_Amount;
    }
    
    /**
     * Getter for property broken_Period_Interest.
     * @return Value of property broken_Period_Interest.
     */
    public Double getBroken_Period_Interest() {
        return broken_Period_Interest;
    }
    
    /**
     * Setter for property broken_Period_Interest.
     * @param broken_Period_Interest New value of property broken_Period_Interest.
     */
    public void setBroken_Period_Interest(Double broken_Period_Interest) {
        this.broken_Period_Interest = broken_Period_Interest;
    }
    
    /**
     * Getter for property broken_Commession.
     * @return Value of property broken_Commession.
     */
    public Double getBroken_Commession() {
        return broken_Commession;
    }
    
    /**
     * Setter for property broken_Commession.
     * @param broken_Commession New value of property broken_Commession.
     */
    public void setBroken_Commession(Double broken_Commession) {
        this.broken_Commession = broken_Commession;
    }
    
    /**
     * Getter for property purchase_Rate.
     * @return Value of property purchase_Rate.
     */
    public Double getPurchase_Rate() {
        return purchase_Rate;
    }
    
    /**
     * Setter for property purchase_Rate.
     * @param purchase_Rate New value of property purchase_Rate.
     */
    public void setPurchase_Rate(Double purchase_Rate) {
        this.purchase_Rate = purchase_Rate;
    }
    
    /**
     * Getter for property years.
     * @return Value of property years.
     */
    public java.lang.Double getYears() {
        return years;
    }
    
    /**
     * Setter for property years.
     * @param years New value of property years.
     */
    public void setYears(java.lang.Double years) {
        this.years = years;
    }
    
    /**
     * Getter for property transactionDetailsTO.
     * @return Value of property transactionDetailsTO.
     */
    public java.util.LinkedHashMap getTransactionDetailsTO() {
        return transactionDetailsTO;
    }
    
    /**
     * Setter for property transactionDetailsTO.
     * @param transactionDetailsTO New value of property transactionDetailsTO.
     */
    public void setTransactionDetailsTO(java.util.LinkedHashMap transactionDetailsTO) {
        this.transactionDetailsTO = transactionDetailsTO;
    }
    
    /**
     * Getter for property deletedTransactionDetailsTO.
     * @return Value of property deletedTransactionDetailsTO.
     */
    public java.util.LinkedHashMap getDeletedTransactionDetailsTO() {
        return deletedTransactionDetailsTO;
    }
    
    /**
     * Setter for property deletedTransactionDetailsTO.
     * @param deletedTransactionDetailsTO New value of property deletedTransactionDetailsTO.
     */
    public void setDeletedTransactionDetailsTO(java.util.LinkedHashMap deletedTransactionDetailsTO) {
        this.deletedTransactionDetailsTO = deletedTransactionDetailsTO;
    }
    
    /**
     * Getter for property allowedTransactionDetailsTO.
     * @return Value of property allowedTransactionDetailsTO.
     */
    public java.util.LinkedHashMap getAllowedTransactionDetailsTO() {
        return allowedTransactionDetailsTO;
    }
    
    /**
     * Setter for property allowedTransactionDetailsTO.
     * @param allowedTransactionDetailsTO New value of property allowedTransactionDetailsTO.
     */
    public void setAllowedTransactionDetailsTO(java.util.LinkedHashMap allowedTransactionDetailsTO) {
        this.allowedTransactionDetailsTO = allowedTransactionDetailsTO;
    }
    
    /**
     * Getter for property authorizeMap.
     * @return Value of property authorizeMap.
     */
    public java.util.HashMap getAuthorizeMap() {
        return authorizeMap;
    }
    
    /**
     * Setter for property authorizeMap.
     * @param authorizeMap New value of property authorizeMap.
     */
    public void setAuthorizeMap(java.util.HashMap authorizeMap) {
        this.authorizeMap = authorizeMap;
    }
    
    /**
     * Getter for property initiatedDate.
     * @return Value of property initiatedDate.
     */
    public java.util.Date getInitiatedDate() {
        return initiatedDate;
    }
    
    /**
     * Setter for property initiatedDate.
     * @param initiatedDate New value of property initiatedDate.
     */
    public void setInitiatedDate(java.util.Date initiatedDate) {
        this.initiatedDate = initiatedDate;
    }
    
    /**
     * Getter for property outstandingAmount.
     * @return Value of property outstandingAmount.
     */
    public java.lang.Double getOutstandingAmount() {
        return outstandingAmount;
    }
    
    /**
     * Setter for property outstandingAmount.
     * @param outstandingAmount New value of property outstandingAmount.
     */
    public void setOutstandingAmount(java.lang.Double outstandingAmount) {
        this.outstandingAmount = outstandingAmount;
    }
    
    /**
     * Getter for property maturityAmount.
     * @return Value of property maturityAmount.
     */
    public java.lang.Double getMaturityAmount() {
        return maturityAmount;
    }
    
    /**
     * Setter for property maturityAmount.
     * @param maturityAmount New value of property maturityAmount.
     */
    public void setMaturityAmount(java.lang.Double maturityAmount) {
        this.maturityAmount = maturityAmount;
    }
    
    /**
     * Getter for property classification.
     * @return Value of property classification.
     */
    public java.lang.String getClassification() {
        return classification;
    }
    
    /**
     * Setter for property classification.
     * @param classification New value of property classification.
     */
    public void setClassification(java.lang.String classification) {
        this.classification = classification;
    }
    
    /**
     * Getter for property shiftingDate.
     * @return Value of property shiftingDate.
     */
    public java.util.Date getShiftingDate() {
        return shiftingDate;
    }
    
    /**
     * Setter for property shiftingDate.
     * @param shiftingDate New value of property shiftingDate.
     */
    public void setShiftingDate(java.util.Date shiftingDate) {
        this.shiftingDate = shiftingDate;
    }
    
    /**
     * Getter for property valuationRate.
     * @return Value of property valuationRate.
     */
    public java.lang.Double getValuationRate() {
        return valuationRate;
    }
    
    /**
     * Setter for property valuationRate.
     * @param valuationRate New value of property valuationRate.
     */
    public void setValuationRate(java.lang.Double valuationRate) {
        this.valuationRate = valuationRate;
    }
    
    /**
     * Getter for property oldClassfication.
     * @return Value of property oldClassfication.
     */
    public java.lang.String getOldClassfication() {
        return oldClassfication;
    }
    
    /**
     * Setter for property oldClassfication.
     * @param oldClassfication New value of property oldClassfication.
     */
    public void setOldClassfication(java.lang.String oldClassfication) {
        this.oldClassfication = oldClassfication;
    }
    
    /**
     * Getter for property newClassfication.
     * @return Value of property newClassfication.
     */
    public java.lang.String getNewClassfication() {
        return newClassfication;
    }
    
    /**
     * Setter for property newClassfication.
     * @param newClassfication New value of property newClassfication.
     */
    public void setNewClassfication(java.lang.String newClassfication) {
        this.newClassfication = newClassfication;
    }
    
    /**
     * Getter for property tblInvestmentAmortizationDet.
     * @return Value of property tblInvestmentAmortizationDet.
     */
    public com.see.truetransact.clientutil.EnhancedTableModel getTblInvestmentAmortizationDet() {
        return tblInvestmentAmortizationDet;
    }
    
    /**
     * Setter for property tblInvestmentAmortizationDet.
     * @param tblInvestmentAmortizationDet New value of property tblInvestmentAmortizationDet.
     */
    public void setTblInvestmentAmortizationDet(com.see.truetransact.clientutil.EnhancedTableModel tblInvestmentAmortizationDet) {
        this.tblInvestmentAmortizationDet = tblInvestmentAmortizationDet;
        setChanged();
    }
    
    private ArrayList getInvestmentsAmortizationCalculationTO(String commond){
        InvestmentsAmortizationCalculationTO objInvestmentsAmortizationCalculationTO =new  InvestmentsAmortizationCalculationTO();
        ArrayList data = new ArrayList();
        for (int i=0;i<tblInvestmentAmortizationDet.getRowCount();i++){
            objInvestmentsAmortizationCalculationTO =new  InvestmentsAmortizationCalculationTO();
            objInvestmentsAmortizationCalculationTO.setStatus(CommonConstants.STATUS_CREATED);
            objInvestmentsAmortizationCalculationTO.setStatusBy(TrueTransactMain.USER_ID);
            objInvestmentsAmortizationCalculationTO.setInvestmentBehaves(CommonUtil.convertObjToStr(tblInvestmentAmortizationDet.getValueAt(i,0)));
            objInvestmentsAmortizationCalculationTO.setInvestmentID(CommonUtil.convertObjToStr(tblInvestmentAmortizationDet.getValueAt(i,1)));
            objInvestmentsAmortizationCalculationTO.setInvestmentName(CommonUtil.convertObjToStr(tblInvestmentAmortizationDet.getValueAt(i,2)));
            objInvestmentsAmortizationCalculationTO.setUptoDate(DateUtil.getDateMMDDYYYY(CommonUtil.convertObjToStr(tblInvestmentAmortizationDet.getValueAt(i,3))));
            objInvestmentsAmortizationCalculationTO.setPremium(CommonUtil.convertObjToDouble(tblInvestmentAmortizationDet.getValueAt(i,4)));
            objInvestmentsAmortizationCalculationTO.setAmortizationAmount(CommonUtil.convertObjToDouble(tblInvestmentAmortizationDet.getValueAt(i,5)));
            objInvestmentsAmortizationCalculationTO.setTransDate(curDate);
            data.add(objInvestmentsAmortizationCalculationTO);
            //            objInvestmentsAmortizationCalculationTO
        }
        return data;
    }
}