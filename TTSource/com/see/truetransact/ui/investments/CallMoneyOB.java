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
import com.see.truetransact.transferobject.investments.CallMoneyTO;
import com.see.truetransact.ui.TrueTransactMain;
import com.see.truetransact.commonutil.CommonUtil;
import com.see.truetransact.ui.common.transaction.TransactionOB;
import com.see.truetransact.transferobject.transaction.transfer.TxTransferTO;
import com.see.truetransact.clientutil.EnhancedTableModel;
import com.see.truetransact.ui.deposit.TableManipulation;

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

public class CallMoneyOB extends CObservable{
    
    Date curDate = ClientUtil.getCurrentDate();
    private final static ClientParseException parseException = ClientParseException.getInstance();
    private final static Logger _log = Logger.getLogger(InvestmentsMasterOB.class);
    private ResourceBundle resourceBundle = java.util.ResourceBundle.getBundle("com.see.truetransact.ui.investments.CallMoneyRB", ProxyParameters.LANGUAGE);
    private ProxyFactory proxy = null;
    private ComboBoxModel  cbmCallMoneyCommunication,cbmCallMoneyInstituation;
    private HashMap map,lookUpHash,keyValue,oldAmountMap,authorizeMap;
    private int _result,_actionType;
    private ArrayList key,value,tblCallMoneysDetColTitle;
    private EnhancedTableModel tblCallMoneyDet;
    private final String NOT_DELETED_TRANS_TOs = "NOT_DELETED_TRANS_TOs";
    private static CallMoneyOB objCallMoneyOB;//Singleton Object
    //     private static InvestmentsMasterOB objInvestmentsMasterOB;
    private TransactionOB transactionOB;
    private String lblStatus = ClientConstants.ACTION_STATUS[ClientConstants.ACTIONTYPE_CANCEL];
    
    private LinkedHashMap transactionDetailsTO ;
    private LinkedHashMap deletedTransactionDetailsTO;
    private LinkedHashMap allowedTransactionDetailsTO ;
    private Date callMoneydate=null;
    private String callMoneyInstituation="";
    private String callMoneyCommunication="";
    private Double noOfDays=null;
    private Double interestRate=null;
    private Double callMoneyAmount=null;
    private Double interestAmt=null;
    private String transMode="";
    private String transType="";
    private Date TransDate=null;
    private String particulars="";
    private String batchID="";
    private Double totTransAmt=null;
    private String callMoneyType="";
    private String reconcileStatus="";
    private String reconcileBatchId="";
    private String txtCallMoneyInstId="";
    private String initBran="";
    
    /** Creates a new instance of ShareProductOB */
    private CallMoneyOB()    {
        map = new HashMap();
        map.put(CommonConstants.JNDI, "CallMoneyJNDI");
        map.put(CommonConstants.HOME, "serverside.investments.CallMoneyHome");
        map.put(CommonConstants.REMOTE, "serverside.investments.CallMoney");
        try {
            proxy = ProxyFactory.createProxy();
            initUIComboBoxModel();
            fillDropdown();
            settblnvestmentTransDetColTitleCol();
            tblCallMoneyDet = new EnhancedTableModel(null, tblCallMoneysDetColTitle);
            
        } catch (Exception e) {
            parseException.logException(e,true);
        }
    }
    
    static {
        try {
            _log.info("Creating InvestmentsMasterOB...");
            objCallMoneyOB= new CallMoneyOB();
        } catch(Exception e) {
            parseException.logException(e,true);
        }
    }
    
    private void initUIComboBoxModel(){
        cbmCallMoneyCommunication=new ComboBoxModel();
        cbmCallMoneyInstituation=new ComboBoxModel();
    }
    
    /* Filling up the the ComboBoxModel with key, value */
    private void fillDropdown() throws Exception{
        try{
            _log.info("Inside FillDropDown");
            lookUpHash = new HashMap();
            lookUpHash.put(CommonConstants.MAP_NAME, null);
            final ArrayList lookup_keys = new ArrayList();
            lookup_keys.add("INVESTMENT");
            lookup_keys.add("TERM_LOAN.SANCTION_MODE");
            
            lookUpHash.put(CommonConstants.PARAMFORQUERY, lookup_keys);
            keyValue = ClientUtil.populateLookupData(lookUpHash);
            getKeyValue((HashMap)keyValue.get("INVESTMENT"));
            cbmCallMoneyInstituation = new ComboBoxModel(key,value);
            getKeyValue((HashMap)keyValue.get("TERM_LOAN.SANCTION_MODE"));
            cbmCallMoneyCommunication=new ComboBoxModel(key,value);
            
        }catch(NullPointerException e){
            parseException.logException(e,true);
        }catch(Exception e){
            parseException.logException(e,true);
        }
    }
    
    private void  settblnvestmentTransDetColTitleCol(){
        tblCallMoneysDetColTitle = new ArrayList();
        tblCallMoneysDetColTitle.add("Trans_Date");
        tblCallMoneysDetColTitle.add("Call Money Date");
        tblCallMoneysDetColTitle.add("Communication");
        tblCallMoneysDetColTitle.add("Instituation");
        tblCallMoneysDetColTitle.add("No Of Days");
        tblCallMoneysDetColTitle.add("Trans Type");
        tblCallMoneysDetColTitle.add("Batch ID");
        tblCallMoneysDetColTitle.add("CallMoney Type");
        tblCallMoneysDetColTitle.add("Interest Rate");
        tblCallMoneysDetColTitle.add("CallMoney Amount");
        tblCallMoneysDetColTitle.add("Interest Amount");
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
    
    public static CallMoneyOB getInstance()throws Exception{
        return objCallMoneyOB;
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
    
    
    /* Executes Query using the TO object */
    public void execute(String command) {
        try {
            HashMap term = new HashMap();
            HashMap proxyResultMap=null;
            term.put(CommonConstants.USER_ID, TrueTransactMain.USER_ID);
            term.put(CommonConstants.MODULE, getModule());
            term.put(CommonConstants.SCREEN, getScreen());
            term.put(CommonConstants.BRANCH_ID, TrueTransactMain.BRANCH_ID);
            if(!command.equals("AUTHORIZE")){
                term.put(CommonConstants.BRANCH_ID, TrueTransactMain.BRANCH_ID);
                term.put("CallMoneyTO", getCallMoneyTO(command));
                transactionDetailsTO=new LinkedHashMap();
                transactionDetailsTO.put(NOT_DELETED_TRANS_TOs,allowedTransactionDetailsTO);
                term.put("TransactionTO",transactionDetailsTO);
                if(getActionType()==ClientConstants.ACTIONTYPE_EDIT){
                    term.put("OLDAMOUNT",oldAmountMap);
                }
            }
            else{
                term.put(CommonConstants.AUTHORIZEMAP,getAuthorizeMap());
            }
            proxyResultMap = proxy.execute(term, map);
            setResult(getActionType());
            
        } catch (Exception e) {
            setResult(ClientConstants.ACTIONTYPE_FAILED);
            parseException.logException(e,true);
        }
    }
    
    
    /** Resetting all tbe Fields of UI */
    public  void resetForm(){
        setTransMode("");
        setTransDate(null);
        setCallMoneydate(null);
        setTransType("");
        setNoOfDays(null);
        setCallMoneyCommunication("");
        setCallMoneyInstituation("");
        setInterestAmt(null);
        setInterestRate(null);
        setCallMoneyAmount(null);
        setParticulars("");
        setTotTransAmt(null);
        setBatchID("");
        resetTable();
        setChanged();
        setCallMoneyType("");
        setTxtCallMoneyInstId("");
        notifyObservers();
        
        
    }
    
    public void setCallMoneyTo(CallMoneyTO objCallMoneyTO ){
        setTransDate(objCallMoneyTO.getTransDT());
        setCallMoneydate(objCallMoneyTO.getCallMoneydate());
        
        setCallMoneyCommunication(CommonUtil.convertObjToStr(getCbmCallMoneyCommunication().getDataForKey(objCallMoneyTO.getCallMoneyCommunication())));
        setCallMoneyInstituation(CommonUtil.convertObjToStr(getCbmCallMoneyInstituation().getDataForKey(objCallMoneyTO.getCallMoneyInstituation())));
        setNoOfDays(objCallMoneyTO.getNoOfDays());
        setBatchID(objCallMoneyTO.getBatchID());
        setInterestRate(objCallMoneyTO.getInterestRate());
        setCallMoneyAmount(objCallMoneyTO.getCallMoneyAmount());
        setInterestAmt(objCallMoneyTO.getInterestAmt());
        setParticulars(objCallMoneyTO.getParticulars());
        setTransType(objCallMoneyTO.getTransType());
        setTransMode(objCallMoneyTO.getTransMode());
        setCallMoneyType(objCallMoneyTO.getCallMoneyType());
        setReconcileBatchId(objCallMoneyTO.getReconcileBatchId());
        setReconcileStatus(objCallMoneyTO.getReconcileStatus());
        setTxtCallMoneyInstId(objCallMoneyTO.getTxtCallMoneyInstId());
        
        setChanged();
        notifyObservers();
        
        
    }
    
    
    
    public CallMoneyTO getCallMoneyTO(String command){
        CallMoneyTO objgetCallMoneyTO= new CallMoneyTO();
        final String yes="Y";
        final String no="N";
        objgetCallMoneyTO.setCommand(command);
        if(objgetCallMoneyTO.getCommand().equalsIgnoreCase(CommonConstants.TOSTATUS_INSERT)){
            objgetCallMoneyTO.setStatus(CommonConstants.STATUS_CREATED);
        }else  if(objgetCallMoneyTO.getCommand().equalsIgnoreCase(CommonConstants.TOSTATUS_UPDATE)){
            objgetCallMoneyTO.setStatus(CommonConstants.STATUS_MODIFIED);
        }else if(objgetCallMoneyTO.getCommand().equalsIgnoreCase(CommonConstants.TOSTATUS_DELETE)){
            objgetCallMoneyTO.setStatus(CommonConstants.STATUS_DELETED);
        }
        objgetCallMoneyTO.setCallMoneyCommunication(CommonUtil.convertObjToStr(cbmCallMoneyCommunication.getKeyForSelected()));
        objgetCallMoneyTO.setCallMoneyInstituation(CommonUtil.convertObjToStr(cbmCallMoneyInstituation.getKeyForSelected()));
        objgetCallMoneyTO.setCallMoneydate(getCallMoneydate());
        objgetCallMoneyTO.setNoOfDays(getNoOfDays());
        objgetCallMoneyTO.setInterestRate(getInterestRate());
        objgetCallMoneyTO.setInterestAmt(getInterestAmt());
        objgetCallMoneyTO.setCallMoneyAmount(getCallMoneyAmount());
        objgetCallMoneyTO.setParticulars(getParticulars());
        objgetCallMoneyTO.setTransMode(getTransMode());
        objgetCallMoneyTO.setTransType(getTransType());
        objgetCallMoneyTO.setTransDT(getTransDate());
        objgetCallMoneyTO.setBatchID(getBatchID());
        objgetCallMoneyTO.setStatusBy(TrueTransactMain.USER_ID);
        objgetCallMoneyTO.setCallMoneyType(getCallMoneyType());
        objgetCallMoneyTO.setReconcileBatchId(getReconcileBatchId());
        objgetCallMoneyTO.setReconcileStatus(getReconcileStatus());
        objgetCallMoneyTO.setTxtCallMoneyInstId(getTxtCallMoneyInstId());
        objgetCallMoneyTO.setInitBran(getInitBran());
        return objgetCallMoneyTO;
    }
    
    
    /** This checks whether user entered sharetype already exists if it exists
     * it returns true otherwise false */
    public boolean isInvsetMentMasterTypeExists(String InvestmentName){
        boolean exists = false;
        ArrayList resultList =(ArrayList) ClientUtil.executeQuery("getSelectInvestmentMaster",null);
        if(resultList != null){
            for(int i=0; i<resultList.size(); i++){
                HashMap resultMap = (HashMap)resultList.get(i);
                String investProdType =CommonUtil.convertObjToStr(resultMap.get("INVSETMENT_NAME"));
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
            if(!whereMap.containsKey("TRANSACTION")){
                System.out.println("mapData------------->"+mapData);
                CallMoneyTO objCallMoneyTO=
                (CallMoneyTO) ((List)((HashMap) mapData.get("CallMoneyTO")).get("CallMoneyTO")).get(0);
                setCallMoneyTo(objCallMoneyTO);
                List  list = (List) mapData.get("TransactionTO");
                if (!list.isEmpty()) {
                    transactionOB.setDetails(list);
                }
                //            if(getActionType()==ClientConstants.ACTIONTYPE_EDIT){
                //                oldAmountMap=new HashMap();
                //                TxTransferTO txTransferTO=new TxTransferTO();
                //                List oldLst=(List) mapData.get("transferTrans");
                //                if(!oldLst.isEmpty()){
                //                    for(int i=0;i<oldLst.size();i++){
                //                        txTransferTO=new TxTransferTO();
                //                        txTransferTO=(TxTransferTO)oldLst.get(i) ;
                //                        oldAmountMap.put(txTransferTO.getTransId(), txTransferTO.getAmount());
                //
                //                    }
                //                }
                //                txTransferTO=null;
                //            }
                
                
            }else  if(whereMap.containsKey("TRANSACTION")){
                ArrayList transList=(ArrayList) mapData.get("getSelectCallMoneyDetailsTO");
                if(transList!=null && transList.size()>0){
                    setCallMoneyTransDetTable(transList);
                }else{
                    ClientUtil.displayAlert("Records Not Their In This Period");
                }
            }
            
        } catch( Exception e ) {
            setResult(ClientConstants.ACTIONTYPE_FAILED);
            e.printStackTrace();
            parseException.logException(e,true);
            
        }
    }
    
    public void  callForCboForReconcile(HashMap map){
        setCallMoneyCommunication(CommonUtil.convertObjToStr(getCbmCallMoneyCommunication().getDataForKey( map.get("CALLMONEY_COMMUNICATION"))));
        setCallMoneyInstituation(CommonUtil.convertObjToStr(getCbmCallMoneyInstituation().getDataForKey(map.get("CALLMONEY_INSTITUATION"))));
    }
    private void setCallMoneyTransDetTable(ArrayList transList){
        tblCallMoneyDet=new EnhancedTableModel();
        ArrayList dataList = new ArrayList();
        for(int i=0 ;i<transList.size();i++){
            ArrayList invAmrDetRow=new ArrayList();
            CallMoneyTO objCallMoneyTO=new CallMoneyTO();
            objCallMoneyTO=(CallMoneyTO)transList.get(i);
            invAmrDetRow.add(0, CommonUtil.convertObjToStr(objCallMoneyTO.getTransDT()));
            invAmrDetRow.add(1, CommonUtil.convertObjToStr(objCallMoneyTO.getCallMoneydate()));
            invAmrDetRow.add(2, objCallMoneyTO.getCallMoneyCommunication());
            invAmrDetRow.add(3, objCallMoneyTO.getCallMoneyInstituation());
            invAmrDetRow.add(4, String.valueOf(objCallMoneyTO.getNoOfDays()));
            invAmrDetRow.add(5, String.valueOf(objCallMoneyTO.getTransType()));
            invAmrDetRow.add(6,String.valueOf(objCallMoneyTO.getBatchID()));
            invAmrDetRow.add(7,String.valueOf(objCallMoneyTO.getCallMoneyType()));
            invAmrDetRow.add(8,String.valueOf(objCallMoneyTO.getInterestRate()));
            invAmrDetRow.add(9,CommonUtil.convertObjToStr(objCallMoneyTO.getCallMoneyAmount()));
            invAmrDetRow.add(10,String.valueOf(objCallMoneyTO.getInterestAmt()));
            dataList.add(invAmrDetRow);
            invAmrDetRow=null;
        }
        tblCallMoneyDet.setDataArrayList(dataList, tblCallMoneysDetColTitle);
        setChanged();
        notifyObservers();
        
    }
    
    public void resetTable(){
        try{
            ArrayList data = tblCallMoneyDet.getDataArrayList();
            for(int i=data.size(); i>0; i--)
                tblCallMoneyDet.removeRow(i-1);
        }catch (Exception e) {
            setResult(ClientConstants.ACTIONTYPE_FAILED);
            //            log.info("Error in resetTable():");
        }
    }
    
    
    public void setTransactionOB(TransactionOB transactionOB) {
        this.transactionOB = transactionOB;
    }
    
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
    
    /**
     * Getter for property initiatedDate.
     * @return Value of property initiatedDate.
     */
    
    
    //    public void setTblInvestmentTransDet(com.see.truetransact.clientutil.EnhancedTableModel tblInvestmentTransDet) {
    //        this.tblInvestmentTransDet = tblInvestmentTransDet;
    //    }
    
    /**
     * Getter for property cbmCallMoneyCommunication.
     * @return Value of property cbmCallMoneyCommunication.
     */
    public com.see.truetransact.clientutil.ComboBoxModel getCbmCallMoneyCommunication() {
        return cbmCallMoneyCommunication;
    }
    
    /**
     * Setter for property cbmCallMoneyCommunication.
     * @param cbmCallMoneyCommunication New value of property cbmCallMoneyCommunication.
     */
    public void setCbmCallMoneyCommunication(com.see.truetransact.clientutil.ComboBoxModel cbmCallMoneyCommunication) {
        this.cbmCallMoneyCommunication = cbmCallMoneyCommunication;
    }
    
    /**
     * Getter for property cbmCallMoneyInstituation.
     * @return Value of property cbmCallMoneyInstituation.
     */
    public com.see.truetransact.clientutil.ComboBoxModel getCbmCallMoneyInstituation() {
        return cbmCallMoneyInstituation;
    }
    
    /**
     * Setter for property cbmCallMoneyInstituation.
     * @param cbmCallMoneyInstituation New value of property cbmCallMoneyInstituation.
     */
    public void setCbmCallMoneyInstituation(com.see.truetransact.clientutil.ComboBoxModel cbmCallMoneyInstituation) {
        this.cbmCallMoneyInstituation = cbmCallMoneyInstituation;
    }
    
    /**
     * Getter for property callMoneydate.
     * @return Value of property callMoneydate.
     */
    public java.util.Date getCallMoneydate() {
        return callMoneydate;
    }
    
    /**
     * Setter for property callMoneydate.
     * @param callMoneydate New value of property callMoneydate.
     */
    public void setCallMoneydate(java.util.Date callMoneydate) {
        this.callMoneydate = callMoneydate;
    }
    
    /**
     * Getter for property callMoneyInstituation.
     * @return Value of property callMoneyInstituation.
     */
    public java.lang.String getCallMoneyInstituation() {
        return callMoneyInstituation;
    }
    
    /**
     * Setter for property callMoneyInstituation.
     * @param callMoneyInstituation New value of property callMoneyInstituation.
     */
    public void setCallMoneyInstituation(java.lang.String callMoneyInstituation) {
        this.callMoneyInstituation = callMoneyInstituation;
    }
    
    /**
     * Getter for property noOfDays.
     * @return Value of property noOfDays.
     */
    public java.lang.Double getNoOfDays() {
        return noOfDays;
    }
    
    /**
     * Setter for property noOfDays.
     * @param noOfDays New value of property noOfDays.
     */
    public void setNoOfDays(java.lang.Double noOfDays) {
        this.noOfDays = noOfDays;
    }
    
    /**
     * Getter for property interestRate.
     * @return Value of property interestRate.
     */
    public java.lang.Double getInterestRate() {
        return interestRate;
    }
    
    /**
     * Setter for property interestRate.
     * @param interestRate New value of property interestRate.
     */
    public void setInterestRate(java.lang.Double interestRate) {
        this.interestRate = interestRate;
    }
    
    /**
     * Getter for property callMoneyAmount.
     * @return Value of property callMoneyAmount.
     */
    public java.lang.Double getCallMoneyAmount() {
        return callMoneyAmount;
    }
    
    /**
     * Setter for property callMoneyAmount.
     * @param callMoneyAmount New value of property callMoneyAmount.
     */
    public void setCallMoneyAmount(java.lang.Double callMoneyAmount) {
        this.callMoneyAmount = callMoneyAmount;
    }
    
    /**
     * Getter for property interestAmt.
     * @return Value of property interestAmt.
     */
    public java.lang.Double getInterestAmt() {
        return interestAmt;
    }
    
    /**
     * Setter for property interestAmt.
     * @param interestAmt New value of property interestAmt.
     */
    public void setInterestAmt(java.lang.Double interestAmt) {
        this.interestAmt = interestAmt;
    }
    
    /**
     * Getter for property TransDate.
     * @return Value of property TransDate.
     */
    public java.util.Date getTransDate() {
        return TransDate;
    }
    
    /**
     * Setter for property TransDate.
     * @param TransDate New value of property TransDate.
     */
    public void setTransDate(java.util.Date TransDate) {
        this.TransDate = TransDate;
    }
    
    /**
     * Getter for property particulars.
     * @return Value of property particulars.
     */
    public java.lang.String getParticulars() {
        return particulars;
    }
    
    /**
     * Setter for property particulars.
     * @param particulars New value of property particulars.
     */
    public void setParticulars(java.lang.String particulars) {
        this.particulars = particulars;
    }
    
    /**
     * Getter for property transMode.
     * @return Value of property transMode.
     */
    public java.lang.String getTransMode() {
        return transMode;
    }
    
    /**
     * Setter for property transMode.
     * @param transMode New value of property transMode.
     */
    public void setTransMode(java.lang.String transMode) {
        this.transMode = transMode;
    }
    
    /**
     * Getter for property transType.
     * @return Value of property transType.
     */
    public java.lang.String getTransType() {
        return transType;
    }
    
    /**
     * Setter for property transType.
     * @param transType New value of property transType.
     */
    public void setTransType(java.lang.String transType) {
        this.transType = transType;
    }
    
    /**
     * Getter for property callMoneyCommunication.
     * @return Value of property callMoneyCommunication.
     */
    public java.lang.String getCallMoneyCommunication() {
        return callMoneyCommunication;
    }
    
    /**
     * Setter for property callMoneyCommunication.
     * @param callMoneyCommunication New value of property callMoneyCommunication.
     */
    public void setCallMoneyCommunication(java.lang.String callMoneyCommunication) {
        this.callMoneyCommunication = callMoneyCommunication;
    }
    
    /**
     * Getter for property batchID.
     * @return Value of property batchID.
     */
    public java.lang.String getBatchID() {
        return batchID;
    }
    
    /**
     * Setter for property batchID.
     * @param batchID New value of property batchID.
     */
    public void setBatchID(java.lang.String batchID) {
        this.batchID = batchID;
    }
    
    /**
     * Getter for property totTransAmt.
     * @return Value of property totTransAmt.
     */
    public java.lang.Double getTotTransAmt() {
        return totTransAmt;
    }
    
    /**
     * Setter for property totTransAmt.
     * @param totTransAmt New value of property totTransAmt.
     */
    public void setTotTransAmt(java.lang.Double totTransAmt) {
        this.totTransAmt = totTransAmt;
    }
    
    /**
     * Getter for property tblCallMoneyDet.
     * @return Value of property tblCallMoneyDet.
     */
    public com.see.truetransact.clientutil.EnhancedTableModel getTblCallMoneyDet() {
        return tblCallMoneyDet;
    }
    
    /**
     * Setter for property tblCallMoneyDet.
     * @param tblCallMoneyDet New value of property tblCallMoneyDet.
     */
    public void setTblCallMoneyDet(com.see.truetransact.clientutil.EnhancedTableModel tblCallMoneyDet) {
        this.tblCallMoneyDet = tblCallMoneyDet;
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
     * Getter for property callMoneyType.
     * @return Value of property callMoneyType.
     */
    public java.lang.String getCallMoneyType() {
        return callMoneyType;
    }
    
    /**
     * Setter for property callMoneyType.
     * @param callMoneyType New value of property callMoneyType.
     */
    public void setCallMoneyType(java.lang.String callMoneyType) {
        this.callMoneyType = callMoneyType;
    }
    
    /**
     * Getter for property reconcileStatus.
     * @return Value of property reconcileStatus.
     */
    public java.lang.String getReconcileStatus() {
        return reconcileStatus;
    }
    
    /**
     * Setter for property reconcileStatus.
     * @param reconcileStatus New value of property reconcileStatus.
     */
    public void setReconcileStatus(java.lang.String reconcileStatus) {
        this.reconcileStatus = reconcileStatus;
    }
    
    /**
     * Getter for property reconcileBatchId.
     * @return Value of property reconcileBatchId.
     */
    public java.lang.String getReconcileBatchId() {
        return reconcileBatchId;
    }
    
    /**
     * Setter for property reconcileBatchId.
     * @param reconcileBatchId New value of property reconcileBatchId.
     */
    public void setReconcileBatchId(java.lang.String reconcileBatchId) {
        this.reconcileBatchId = reconcileBatchId;
    }
    
    /**
     * Getter for property txtCallMoneyInstId.
     * @return Value of property txtCallMoneyInstId.
     */
    public java.lang.String getTxtCallMoneyInstId() {
        return txtCallMoneyInstId;
    }
    
    /**
     * Setter for property txtCallMoneyInstId.
     * @param txtCallMoneyInstId New value of property txtCallMoneyInstId.
     */
    public void setTxtCallMoneyInstId(java.lang.String txtCallMoneyInstId) {
        this.txtCallMoneyInstId = txtCallMoneyInstId;
    }
    
    /**
     * Getter for property initBran.
     * @return Value of property initBran.
     */
    public java.lang.String getInitBran() {
        return initBran;
    }
    
    /**
     * Setter for property initBran.
     * @param initBran New value of property initBran.
     */
    public void setInitBran(java.lang.String initBran) {
        this.initBran = initBran;
    }
    
}