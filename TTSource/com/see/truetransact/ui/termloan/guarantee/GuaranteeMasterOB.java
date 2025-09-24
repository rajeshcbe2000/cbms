/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition.
 * 
 *
 * ShareProductOB.java
 *
 * Created on Fri Jan 07 12:01:51 IST 2005
 */

package com.see.truetransact.ui.termloan.guarantee;


import com.see.truetransact.uicomponent.CObservable;
import com.see.truetransact.clientexception.ClientParseException;
import com.see.truetransact.transferobject.termloan.guarantee.GuaranteeTransTO;
import com.see.truetransact.commonutil.CommonConstants;
import com.see.truetransact.clientutil.ClientConstants;
import com.see.truetransact.commonutil.DateUtil;
import com.see.truetransact.clientproxy.ProxyFactory;
import com.see.truetransact.clientproxy.ProxyParameters;
import com.see.truetransact.clientutil.ComboBoxModel;
import com.see.truetransact.clientutil.ClientUtil;
import com.see.truetransact.transferobject.investments.InvestmentsMasterTO;
import com.see.truetransact.ui.TrueTransactMain;
import com.see.truetransact.commonutil.CommonUtil;
import com.see.truetransact.ui.common.transaction.TransactionOB;
import com.see.truetransact.transferobject.termloan.guarantee.GuaranteeMasterTO;
import com.see.truetransact.clientutil.EnhancedTableModel;
import org.apache.log4j.Logger;

import java.util.HashMap;
import java.util.ArrayList;
import java.util.List;
import java.util.Date;
import java.util.ResourceBundle;
import java.util.LinkedHashMap;

/**
 *
 * @author Ashok Vijayakumar
 */

public class GuaranteeMasterOB extends CObservable{
    
    Date curDate = ClientUtil.getCurrentDate();
    private final static ClientParseException parseException = ClientParseException.getInstance();
    private final static Logger _log = Logger.getLogger(GuaranteeMasterOB.class);
    private ResourceBundle resourceBundle = java.util.ResourceBundle.getBundle("com.see.truetransact.ui.investments.InvestmentsMasterRB", ProxyParameters.LANGUAGE);
    private ProxyFactory proxy = null;
    private ComboBoxModel cbmPli,cbmPliBranch,cbmRepaymentfrequency,cbmGuranteeSanctionBy;
    private EnhancedTableModel tblInvestmentTransDet;
    private LinkedHashMap allowedTransactionDetailsTO ,transactionDetailsTO;
    private final String NOT_DELETED_TRANS_TOs = "NOT_DELETED_TRANS_TOs";
    private HashMap map,lookUpHash,keyValue ,oldAmountMap;
    private int _result,_actionType;
    private ArrayList key,value,tblnvestmentTransDetColTitle;
    private HashMap _authorizeMap;
    private static GuaranteeMasterOB objGuaranteeMasterOB;//Singleton Object
    private String lblStatus = ClientConstants.ACTION_STATUS[ClientConstants.ACTIONTYPE_CANCEL];
    private String cboPli;
    private String cbopliBranch;
    private String cboRepaymentFrequency;
    private String cboGuranteeSanctionBy;
    private TransactionOB transactionOB;
    private String custId;
    private String sanctionNo;
    private Date sanctionDt;
    private String loanNo ;
    private Date loanDt;
    private String sanctionAmt;
    private String holidayPeriod;
    private String noOfInst;
    private String intRate;
    private String guaranteeNo;
    private Date guaranteeDt;
    private String guaranteeSanctionNo;
    private String guranteAmt;
    private String guranteFeepayBy;
    private String guranteFeePer;
    private String guranteFee;
    
    
    
    
    /** Creates a new instance of ShareProductOB */
    private GuaranteeMasterOB() {
        map = new HashMap();
        map.put(CommonConstants.JNDI, "GuaranteeMasterJNDI");
        map.put(CommonConstants.HOME, "serverside.termloan.guarantee.GuaranteeMasterHome");
        map.put(CommonConstants.REMOTE, "serverside.termloan.guarantee.GuaranteeMaster");
        try {
            proxy = ProxyFactory.createProxy();
            initUIComboBoxModel();
            fillDropdown();
              settblnvestmentTransDetColTitleCol();
             tblInvestmentTransDet = new EnhancedTableModel(null, tblnvestmentTransDetColTitle);
           
            
        } catch (Exception e) {
            parseException.logException(e,true);
        }
    }
    
    static {
        try {
            _log.info("Creating InvestmentsMasterOB...");
            objGuaranteeMasterOB= new GuaranteeMasterOB();
        } catch(Exception e) {
            parseException.logException(e,true);
        }
    }
    
    /** Creating Instances of ComboBoxModel */
    private void initUIComboBoxModel(){
        cbmRepaymentfrequency=new ComboBoxModel();
        cbmGuranteeSanctionBy=new ComboBoxModel();
        cbmPli=new ComboBoxModel();
        cbmPliBranch=new ComboBoxModel();
    }
    
    /* Filling up the the ComboBoxModel with key, value */
    private void fillDropdown() throws Exception{
        try{
            _log.info("Inside FillDropDown");
            lookUpHash = new HashMap();
            lookUpHash.put(CommonConstants.MAP_NAME, null);
            final ArrayList lookup_keys = new ArrayList();
            lookup_keys.add("TERM_LOAN.REPAYMENT_CODE");
            lookup_keys.add("TERM_LOAN.SANCTIONING_AUTHORITY");
            
            //            lookup_keys.add("DEPOSITSPRODUCT.DEPOSITPERIOD");
            
            lookUpHash.put(CommonConstants.PARAMFORQUERY, lookup_keys);
            keyValue = ClientUtil.populateLookupData(lookUpHash);
            getKeyValue((HashMap)keyValue.get("TERM_LOAN.REPAYMENT_CODE"));
            cbmRepaymentfrequency = new ComboBoxModel(key,value);
            
            getKeyValue((HashMap)keyValue.get("TERM_LOAN.SANCTIONING_AUTHORITY"));
            cbmGuranteeSanctionBy=new ComboBoxModel(key,value);
            lookUpHash=new HashMap();
            lookUpHash.put(CommonConstants.MAP_NAME, "InwardClearing.getBank");
            keyValue = ClientUtil.populateLookupData(lookUpHash);
            getKeyValue((HashMap)keyValue.get("DATA"));
            cbmPli = new ComboBoxModel(key,value);
            
        }catch(NullPointerException e){
            parseException.logException(e,true);
        }catch(Exception e){
            parseException.logException(e,true);
        }
    }
    
    public void getPliBranch(String pli){
        try{
            lookUpHash=new HashMap();
            lookUpHash.put(CommonConstants.MAP_NAME, "getOtherBankBranchs2");
            lookUpHash.put(CommonConstants.PARAMFORQUERY, pli);
            keyValue = ClientUtil.populateLookupData(lookUpHash);
            getKeyValue((HashMap)keyValue.get("DATA"));
            cbmPliBranch = new ComboBoxModel(key,value);
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
    
    public static GuaranteeMasterOB getInstance()throws Exception{
        return objGuaranteeMasterOB;
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
    public GuaranteeMasterTO getGuaranteeMasterTO(String command){
        
        GuaranteeMasterTO objgetGuaranteeMasterTO= new GuaranteeMasterTO();
        
        objgetGuaranteeMasterTO.setCommand(command);
        if(objgetGuaranteeMasterTO.getCommand().equalsIgnoreCase(CommonConstants.TOSTATUS_INSERT)){
            objgetGuaranteeMasterTO.setStatus(CommonConstants.STATUS_CREATED);
        }else  if(objgetGuaranteeMasterTO.getCommand().equalsIgnoreCase(CommonConstants.TOSTATUS_UPDATE)){
            objgetGuaranteeMasterTO.setStatus(CommonConstants.STATUS_MODIFIED);
        }else if(objgetGuaranteeMasterTO.getCommand().equalsIgnoreCase(CommonConstants.TOSTATUS_DELETE)){
            objgetGuaranteeMasterTO.setStatus(CommonConstants.STATUS_DELETED);
        }
        objgetGuaranteeMasterTO.setCboPli(CommonUtil.convertObjToStr(getCboPli()));
        objgetGuaranteeMasterTO.setCbopliBranch(CommonUtil.convertObjToStr(getCbopliBranch()));
        objgetGuaranteeMasterTO.setCboRepaymentFrequency(CommonUtil.convertObjToStr(getCboRepaymentFrequency()));
        objgetGuaranteeMasterTO.setCboGuranteeSanctionBy(CommonUtil.convertObjToStr(getCboGuranteeSanctionBy()));
        objgetGuaranteeMasterTO.setCustId(CommonUtil.convertObjToStr(getCustId()));
        objgetGuaranteeMasterTO.setSanctionNo(CommonUtil.convertObjToStr(getSanctionNo()));
        objgetGuaranteeMasterTO.setSanctionDt(getSanctionDt());
        objgetGuaranteeMasterTO.setLoanNo(CommonUtil.convertObjToStr(getLoanNo()));
        objgetGuaranteeMasterTO.setLoanDt(getLoanDt());
        objgetGuaranteeMasterTO.setSanctionAmt(CommonUtil.convertObjToStr(getSanctionAmt()));
        objgetGuaranteeMasterTO.setHolidayPeriod(CommonUtil.convertObjToStr(getHolidayPeriod()));
        objgetGuaranteeMasterTO.setNoOfInst(CommonUtil.convertObjToStr(getNoOfInst()));
        objgetGuaranteeMasterTO.setIntRate(CommonUtil.convertObjToStr(getIntRate()));
        objgetGuaranteeMasterTO.setGuaranteeNo(CommonUtil.convertObjToStr(getGuaranteeNo()));
        objgetGuaranteeMasterTO.setGuaranteeDt(getGuaranteeDt());
        objgetGuaranteeMasterTO.setGuaranteeSanctionNo(getGuaranteeSanctionNo());
        objgetGuaranteeMasterTO.setGuranteAmt(getGuranteAmt());
        objgetGuaranteeMasterTO.setGuranteFeepayBy(getGuranteFeepayBy());
        objgetGuaranteeMasterTO.setGuranteFeePer(getGuranteFeePer());
        objgetGuaranteeMasterTO.setGuranteFee(getGuranteFee());
        return objgetGuaranteeMasterTO;
        //
    }
    //
    /** Sets all the InvsetmentMaster values to the OB varibles  there by populatin the UI fields */
    private void setGuaranteeMasterTO(GuaranteeMasterTO objGuaranteeMasterTO){
        //        setCboPli(CommonUtil.convertObjToStr(getCbmPli().getDataForKey(objGuaranteeMasterTO.getCboPli())));
        setCboPli((String)getCbmPli().getDataForKey(objGuaranteeMasterTO.getCboPli()));
        setCbopliBranch(CommonUtil.convertObjToStr(getCbmPliBranch().getDataForKey(objGuaranteeMasterTO.getCbopliBranch())));
        setCboRepaymentFrequency(CommonUtil.convertObjToStr(getCbmRepaymentfrequency().getDataForKey(objGuaranteeMasterTO.getCboRepaymentFrequency())));
        setCboGuranteeSanctionBy(CommonUtil.convertObjToStr(getCbmGuranteeSanctionBy().getDataForKey(objGuaranteeMasterTO.getCboGuranteeSanctionBy())));
        setCustId(CommonUtil.convertObjToStr(objGuaranteeMasterTO.getCustId()));
        setSanctionNo(CommonUtil.convertObjToStr(objGuaranteeMasterTO.getSanctionNo()));
        setSanctionDt(objGuaranteeMasterTO.getSanctionDt());
        setLoanNo(CommonUtil.convertObjToStr(objGuaranteeMasterTO.getLoanNo()));
        setLoanDt(objGuaranteeMasterTO.getLoanDt());
        setSanctionAmt(CommonUtil.convertObjToStr(objGuaranteeMasterTO.getSanctionAmt()));
        setHolidayPeriod(CommonUtil.convertObjToStr(objGuaranteeMasterTO.getHolidayPeriod()));
        setNoOfInst(CommonUtil.convertObjToStr(objGuaranteeMasterTO.getNoOfInst()));
        setIntRate(CommonUtil.convertObjToStr(objGuaranteeMasterTO.getIntRate()));
        setGuaranteeNo(CommonUtil.convertObjToStr(objGuaranteeMasterTO.getGuaranteeNo()));
        setGuaranteeDt(objGuaranteeMasterTO.getGuaranteeDt());
        setGuaranteeSanctionNo(CommonUtil.convertObjToStr(objGuaranteeMasterTO.getGuaranteeSanctionNo()));
        setGuranteAmt(CommonUtil.convertObjToStr(objGuaranteeMasterTO.getGuranteAmt()));
        setGuranteFeepayBy(CommonUtil.convertObjToStr(objGuaranteeMasterTO.getGuranteFeepayBy()));
        setGuranteFeePer(CommonUtil.convertObjToStr(objGuaranteeMasterTO.getGuranteFeePer()));
        setGuranteFee(CommonUtil.convertObjToStr(objGuaranteeMasterTO.getGuranteFee()));
        notifyObservers();
    }
    
    /* Executes Query using the TO object */
    public void execute(String command) {
        try {
            HashMap term = new HashMap();
            int countRec=0;
            HashMap proxyResultMap=null;
            term.put(CommonConstants.USER_ID, TrueTransactMain.USER_ID);
            term.put(CommonConstants.MODULE, getModule());
            term.put(CommonConstants.SCREEN, getScreen());
            term.put(CommonConstants.BRANCH_ID, TrueTransactMain.BRANCH_ID);
            if(!command.equals("AUTHORIZE")){
                term.put(CommonConstants.BRANCH_ID, TrueTransactMain.BRANCH_ID);
                term.put("GuaranteeMasterTO", getGuaranteeMasterTO(command));
                //                term.put("InvestmentsTransTO", getInvestmentsTransTO(command));
                transactionDetailsTO=new LinkedHashMap();
                transactionDetailsTO.put(NOT_DELETED_TRANS_TOs,allowedTransactionDetailsTO);
                term.put("TransactionTO",transactionDetailsTO);
                if(getActionType()==ClientConstants.ACTIONTYPE_EDIT){
                    term.put("OLDAMOUNT",oldAmountMap);
                }
            }else{
                term.put("GuaranteeMasterTO", getGuaranteeMasterTO(command));
                term.put(CommonConstants.AUTHORIZEMAP, get_authorizeMap());
                term.put(CommonConstants.USER_ID, TrueTransactMain.USER_ID);
                transactionDetailsTO=new LinkedHashMap();
                transactionDetailsTO.put(NOT_DELETED_TRANS_TOs,allowedTransactionDetailsTO);
                term.put("TransactionTO",transactionDetailsTO);
                
                
            }
            
            //        else{
            //                term.put(CommonConstants.AUTHORIZEMAP,getAuthorizeMap());
            //            }
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
    
    
    /** Resetting all tbe Fields of UI */
    public  void resetForm(){
        setCboPli("");
        setCbopliBranch("");
        setCustId("");
        setSanctionDt(null);
        setLoanNo("");
        setLoanDt(null);
        setSanctionAmt("");
        setHolidayPeriod("");
        setNoOfInst("");
        setCboRepaymentFrequency("");
        setIntRate("");
        setGuaranteeNo("");
        setGuaranteeDt(null);
        setCboGuranteeSanctionBy("");
        setGuaranteeSanctionNo("");
        setGuranteAmt("");
        setGuranteFeepayBy("");
        setGuranteFeePer("");
        setGuranteFee("");
        setGuaranteeNo("");
        setLoanNo("");
        resetTable();
        notifyObservers();
    }
    
    
    /** This checks whether user entered sharetype already exists if it exists
     * it returns true otherwise false */
    public boolean isInvsetMentMasterTypeExists(String InvestmentName){
        boolean exists = false;
        ArrayList resultList =(ArrayList) ClientUtil.executeQuery("getSelectInvestmentMaster",null);
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
            whereMap.put(CommonConstants.MAP_WHERE,whereMap.get("GUARANTEE_NO"));
            mapData = proxy.executeQuery(whereMap, map);
            
            GuaranteeMasterTO objGuaranteeMasterTO =
            (GuaranteeMasterTO) ((List)((HashMap) mapData.get("GuaranteeMasterTO")).get("GuaranteeMasterTO")).get(0);
            //             if((getActionType()==ClientConstants.ACTIONTYPE_DELETE || getActionType()==ClientConstants.ACTIONTYPE_REJECT)&&CommonUtil.convertObjToDouble(getOutstandingAmount()).doubleValue()!=0.0 ) {
            //                objInvestmentsMasterTO=null;
            //                ClientUtil.displayAlert("outstanding Amount is Greater Than Zero");
            //            }else{
            setGuaranteeMasterTO(objGuaranteeMasterTO);
            //            }
            //
            
            List  list = (List) mapData.get("TransactionTO");
            if (!list.isEmpty()) {
                transactionOB.setDetails(list);
            }
            
         
                ArrayList transList=(ArrayList) mapData.get("getSelectGuaranteeTransTO");
                if(transList!=null && transList.size()>0){
                    setInvestmentAmortizationTable(transList);
              
            }
            
            
        } catch( Exception e ) {
            setResult(ClientConstants.ACTIONTYPE_FAILED);
            e.printStackTrace();
            parseException.logException(e,true);
            
        }
    }
    
     public void resetTable(){
        try{
            ArrayList data = tblInvestmentTransDet.getDataArrayList();
            for(int i=data.size(); i>0; i--)
                tblInvestmentTransDet.removeRow(i-1);
        }catch (Exception e) {
            setResult(ClientConstants.ACTIONTYPE_FAILED);
            //            log.info("Error in resetTable():");
        }
    }
    
    private void setInvestmentAmortizationTable(ArrayList transList){
        tblInvestmentTransDet=new EnhancedTableModel();
        ArrayList dataList = new ArrayList();
        for(int i=0 ;i<transList.size();i++){
            ArrayList invAmrDetRow=new ArrayList();
            GuaranteeTransTO objGuaranteeTransTO=new GuaranteeTransTO();
            objGuaranteeTransTO=(GuaranteeTransTO)transList.get(i);
            invAmrDetRow.add(0, CommonUtil.convertObjToStr(objGuaranteeTransTO.getTransDt()));
            invAmrDetRow.add(1, objGuaranteeTransTO.getGuaranteeNo());
            invAmrDetRow.add(2, objGuaranteeTransTO.getCboPli());
            invAmrDetRow.add(3, CommonUtil.convertObjToStr(objGuaranteeTransTO.getTrnAmt()));
//            invAmrDetRow.add(4, CommonUtil.convertObjToStr(objInvestmentsTransTO.getInvestmentAmount()));
//            invAmrDetRow.add(5, CommonUtil.convertObjToStr(objInvestmentsTransTO.getPremiumAmount()));
//            invAmrDetRow.add(6,CommonUtil.convertObjToStr(objInvestmentsTransTO.getBrokenPeriodInterest()));
            
            dataList.add(invAmrDetRow);
            invAmrDetRow=null;
        }
        tblInvestmentTransDet.setDataArrayList(dataList, tblnvestmentTransDetColTitle);
        setChanged();
        notifyObservers();
    }

       private void  settblnvestmentTransDetColTitleCol(){
        tblnvestmentTransDetColTitle = new ArrayList();
        tblnvestmentTransDetColTitle.add("Trans_Date");
        tblnvestmentTransDetColTitle.add("Guarantee No");
        tblnvestmentTransDetColTitle.add("PLI");
        tblnvestmentTransDetColTitle.add("Amount");
//        tblnvestmentTransDetColTitle.add("Investment Amount");
//        tblnvestmentTransDetColTitle.add("Premium Amount");
//        tblnvestmentTransDetColTitle.add("Interest Amount");
        
        //        tblnvestmentTransDetColTitle.add("AmortizationAmount");
    }
    
    
    /**
     * Getter for property transactionOB.
     * @return Value of property transactionOB.
     */
    public com.see.truetransact.ui.common.transaction.TransactionOB getTransactionOB() {
        return transactionOB;
    }
    
    /**
     * Setter for property transactionOB.
     * @param transactionOB New value of property transactionOB.
     */
    public void setTransactionOB(com.see.truetransact.ui.common.transaction.TransactionOB transactionOB) {
        this.transactionOB = transactionOB;
    }
    
    /**
     * Getter for property cbmPli.
     * @return Value of property cbmPli.
     */
    public com.see.truetransact.clientutil.ComboBoxModel getCbmPli() {
        return cbmPli;
    }
    
    /**
     * Setter for property cbmPli.
     * @param cbmPli New value of property cbmPli.
     */
    public void setCbmPli(com.see.truetransact.clientutil.ComboBoxModel cbmPli) {
        this.cbmPli = cbmPli;
    }
    
    /**
     * Getter for property cbmPliBranch.
     * @return Value of property cbmPliBranch.
     */
    public com.see.truetransact.clientutil.ComboBoxModel getCbmPliBranch() {
        return cbmPliBranch;
    }
    
    /**
     * Setter for property cbmPliBranch.
     * @param cbmPliBranch New value of property cbmPliBranch.
     */
    public void setCbmPliBranch(com.see.truetransact.clientutil.ComboBoxModel cbmPliBranch) {
        this.cbmPliBranch = cbmPliBranch;
    }
    
    /**
     * Getter for property cbmRepaymentfrequency.
     * @return Value of property cbmRepaymentfrequency.
     */
    public com.see.truetransact.clientutil.ComboBoxModel getCbmRepaymentfrequency() {
        return cbmRepaymentfrequency;
    }
    
    /**
     * Setter for property cbmRepaymentfrequency.
     * @param cbmRepaymentfrequency New value of property cbmRepaymentfrequency.
     */
    public void setCbmRepaymentfrequency(com.see.truetransact.clientutil.ComboBoxModel cbmRepaymentfrequency) {
        this.cbmRepaymentfrequency = cbmRepaymentfrequency;
    }
    
    /**
     * Getter for property cboPli.
     * @return Value of property cboPli.
     */
    public java.lang.String getCboPli() {
        return cboPli;
        
    }
    
    /**
     * Setter for property cboPli.
     * @param cboPli New value of property cboPli.
     */
    public void setCboPli(java.lang.String cboPli) {
        this.cboPli = cboPli;
        setChanged();
    }
    
    /**
     * Getter for property cbopliBranch.
     * @return Value of property cbopliBranch.
     */
    public java.lang.String getCbopliBranch() {
        return cbopliBranch;
    }
    
    /**
     * Setter for property cbopliBranch.
     * @param cbopliBranch New value of property cbopliBranch.
     */
    public void setCbopliBranch(java.lang.String cbopliBranch) {
        this.cbopliBranch = cbopliBranch;
    }
    
    /**
     * Getter for property cboRepaymentFrequency.
     * @return Value of property cboRepaymentFrequency.
     */
    public java.lang.String getCboRepaymentFrequency() {
        return cboRepaymentFrequency;
    }
    
    /**
     * Setter for property cboRepaymentFrequency.
     * @param cboRepaymentFrequency New value of property cboRepaymentFrequency.
     */
    public void setCboRepaymentFrequency(java.lang.String cboRepaymentFrequency) {
        this.cboRepaymentFrequency = cboRepaymentFrequency;
    }
    
    
    
    /**
     * Getter for property sanctionDt.
     * @return Value of property sanctionDt.
     */
    public java.util.Date getSanctionDt() {
        return sanctionDt;
    }
    
    /**
     * Setter for property sanctionDt.
     * @param sanctionDt New value of property sanctionDt.
     */
    public void setSanctionDt(java.util.Date sanctionDt) {
        this.sanctionDt = sanctionDt;
    }
    
    /**
     * Getter for property loanNo.
     * @return Value of property loanNo.
     */
    public java.lang.String getLoanNo() {
        return loanNo;
    }
    
    /**
     * Setter for property loanNo.
     * @param loanNo New value of property loanNo.
     */
    public void setLoanNo(java.lang.String loanNo) {
        this.loanNo = loanNo;
    }
    
    /**
     * Getter for property loanDt.
     * @return Value of property loanDt.
     */
    public java.util.Date getLoanDt() {
        return loanDt;
    }
    
    /**
     * Setter for property loanDt.
     * @param loanDt New value of property loanDt.
     */
    public void setLoanDt(java.util.Date loanDt) {
        this.loanDt = loanDt;
    }
    
    /**
     * Getter for property sanctionAmt.
     * @return Value of property sanctionAmt.
     */
    public java.lang.String getSanctionAmt() {
        return sanctionAmt;
    }
    
    /**
     * Setter for property sanctionAmt.
     * @param sanctionAmt New value of property sanctionAmt.
     */
    public void setSanctionAmt(java.lang.String sanctionAmt) {
        this.sanctionAmt = sanctionAmt;
    }
    
    /**
     * Getter for property holidayPeriod.
     * @return Value of property holidayPeriod.
     */
    public java.lang.String getHolidayPeriod() {
        return holidayPeriod;
    }
    
    /**
     * Setter for property holidayPeriod.
     * @param holidayPeriod New value of property holidayPeriod.
     */
    public void setHolidayPeriod(java.lang.String holidayPeriod) {
        this.holidayPeriod = holidayPeriod;
    }
    
    /**
     * Getter for property noOfInst.
     * @return Value of property noOfInst.
     */
    public java.lang.String getNoOfInst() {
        return noOfInst;
    }
    
    /**
     * Setter for property noOfInst.
     * @param noOfInst New value of property noOfInst.
     */
    public void setNoOfInst(java.lang.String noOfInst) {
        this.noOfInst = noOfInst;
    }
    
    /**
     * Getter for property intRate.
     * @return Value of property intRate.
     */
    public java.lang.String getIntRate() {
        return intRate;
    }
    
    /**
     * Setter for property intRate.
     * @param intRate New value of property intRate.
     */
    public void setIntRate(java.lang.String intRate) {
        this.intRate = intRate;
    }
    
    /**
     * Getter for property guaranteeNo.
     * @return Value of property guaranteeNo.
     */
    public java.lang.String getGuaranteeNo() {
        return guaranteeNo;
    }
    
    /**
     * Setter for property guaranteeNo.
     * @param guaranteeNo New value of property guaranteeNo.
     */
    public void setGuaranteeNo(java.lang.String guaranteeNo) {
        this.guaranteeNo = guaranteeNo;
        setChanged();
    }
    
    /**
     * Getter for property guaranteeDt.
     * @return Value of property guaranteeDt.
     */
    public java.util.Date getGuaranteeDt() {
        return guaranteeDt;
    }
    
    /**
     * Setter for property guaranteeDt.
     * @param guaranteeDt New value of property guaranteeDt.
     */
    public void setGuaranteeDt(java.util.Date guaranteeDt) {
        this.guaranteeDt = guaranteeDt;
    }
    
    /**
     * Getter for property cbmGuranteeSanctionBy.
     * @return Value of property cbmGuranteeSanctionBy.
     */
    public com.see.truetransact.clientutil.ComboBoxModel getCbmGuranteeSanctionBy() {
        return cbmGuranteeSanctionBy;
    }
    
    /**
     * Setter for property cbmGuranteeSanctionBy.
     * @param cbmGuranteeSanctionBy New value of property cbmGuranteeSanctionBy.
     */
    public void setCbmGuranteeSanctionBy(com.see.truetransact.clientutil.ComboBoxModel cbmGuranteeSanctionBy) {
        this.cbmGuranteeSanctionBy = cbmGuranteeSanctionBy;
    }
    
    /**
     * Getter for property cboGuranteeSanctionBy.
     * @return Value of property cboGuranteeSanctionBy.
     */
    public java.lang.String getCboGuranteeSanctionBy() {
        return cboGuranteeSanctionBy;
    }
    
    /**
     * Setter for property cboGuranteeSanctionBy.
     * @param cboGuranteeSanctionBy New value of property cboGuranteeSanctionBy.
     */
    public void setCboGuranteeSanctionBy(java.lang.String cboGuranteeSanctionBy) {
        this.cboGuranteeSanctionBy = cboGuranteeSanctionBy;
    }
    
    /**
     * Getter for property guaranteeSanctionNo.
     * @return Value of property guaranteeSanctionNo.
     */
    public java.lang.String getGuaranteeSanctionNo() {
        return guaranteeSanctionNo;
    }
    
    /**
     * Setter for property guaranteeSanctionNo.
     * @param guaranteeSanctionNo New value of property guaranteeSanctionNo.
     */
    public void setGuaranteeSanctionNo(java.lang.String guaranteeSanctionNo) {
        this.guaranteeSanctionNo = guaranteeSanctionNo;
    }
    
    /**
     * Getter for property guranteAmt.
     * @return Value of property guranteAmt.
     */
    public java.lang.String getGuranteAmt() {
        return guranteAmt;
    }
    
    /**
     * Setter for property guranteAmt.
     * @param guranteAmt New value of property guranteAmt.
     */
    public void setGuranteAmt(java.lang.String guranteAmt) {
        this.guranteAmt = guranteAmt;
    }
    
    /**
     * Getter for property guranteFeepayBy.
     * @return Value of property guranteFeepayBy.
     */
    public java.lang.String getGuranteFeepayBy() {
        return guranteFeepayBy;
    }
    
    /**
     * Setter for property guranteFeepayBy.
     * @param guranteFeepayBy New value of property guranteFeepayBy.
     */
    public void setGuranteFeepayBy(java.lang.String guranteFeepayBy) {
        this.guranteFeepayBy = guranteFeepayBy;
    }
    
    /**
     * Getter for property guranteFeePer.
     * @return Value of property guranteFeePer.
     */
    public java.lang.String getGuranteFeePer() {
        return guranteFeePer;
    }
    
    /**
     * Setter for property guranteFeePer.
     * @param guranteFeePer New value of property guranteFeePer.
     */
    public void setGuranteFeePer(java.lang.String guranteFeePer) {
        this.guranteFeePer = guranteFeePer;
    }
    
    /**
     * Getter for property guranteFee.
     * @return Value of property guranteFee.
     */
    public java.lang.String getGuranteFee() {
        return guranteFee;
    }
    
    /**
     * Setter for property guranteFee.
     * @param guranteFee New value of property guranteFee.
     */
    public void setGuranteFee(java.lang.String guranteFee) {
        this.guranteFee = guranteFee;
    }
    
    /**
     * Getter for property sanctionNo.
     * @return Value of property sanctionNo.
     */
    public java.lang.String getSanctionNo() {
        return sanctionNo;
    }
    
    /**
     * Setter for property sanctionNo.
     * @param sanctionNo New value of property sanctionNo.
     */
    public void setSanctionNo(java.lang.String sanctionNo) {
        this.sanctionNo = sanctionNo;
        setChanged();
    }
    
    /**
     * Getter for property custId.
     * @return Value of property custId.
     */
    public java.lang.String getCustId() {
        return custId;
    }
    
    /**
     * Setter for property custId.
     * @param custId New value of property custId.
     */
    public void setCustId(java.lang.String custId) {
        this.custId = custId;
    }
    
    /**
     * Getter for property allowedTransactionDetailsTO.
     * @return Value of property allowedTransactionDetailsTO.
     */
    public LinkedHashMap getAllowedTransactionDetailsTO() {
        return allowedTransactionDetailsTO;
    }
    
    /**
     * Setter for property allowedTransactionDetailsTO.
     * @param allowedTransactionDetailsTO New value of property allowedTransactionDetailsTO.
     */
    public void setAllowedTransactionDetailsTO(LinkedHashMap allowedTransactionDetailsTO) {
        this.allowedTransactionDetailsTO = allowedTransactionDetailsTO;
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
     * Getter for property _authorizeMap.
     * @return Value of property _authorizeMap.
     */
    public java.util.HashMap get_authorizeMap() {
        return _authorizeMap;
    }
    
    /**
     * Setter for property _authorizeMap.
     * @param _authorizeMap New value of property _authorizeMap.
     */
    public void set_authorizeMap(java.util.HashMap _authorizeMap) {
        this._authorizeMap = _authorizeMap;
    }
    
    /**
     * Getter for property tblInvestmentTransDet.
     * @return Value of property tblInvestmentTransDet.
     */
    public EnhancedTableModel getTblInvestmentTransDet() {
        return tblInvestmentTransDet;
    }
    
    /**
     * Setter for property tblInvestmentTransDet.
     * @param tblInvestmentTransDet New value of property tblInvestmentTransDet.
     */
    public void setTblInvestmentTransDet(EnhancedTableModel tblInvestmentTransDet) {
        this.tblInvestmentTransDet = tblInvestmentTransDet;
    }
    
}