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

package com.see.truetransact.ui.product.investments;

import com.see.truetransact.uicomponent.CObservable;
import com.see.truetransact.clientexception.ClientParseException;
import com.see.truetransact.commonutil.CommonConstants;
import com.see.truetransact.clientutil.ClientConstants;
import com.see.truetransact.clientproxy.ProxyFactory;
import com.see.truetransact.clientproxy.ProxyParameters;
import com.see.truetransact.clientutil.ComboBoxModel;
import com.see.truetransact.clientutil.ClientUtil;
import com.see.truetransact.transferobject.product.investments.InvestmentsProductTO;
import com.see.truetransact.ui.TrueTransactMain;
import com.see.truetransact.commonutil.CommonUtil;

import org.apache.log4j.Logger;

import java.util.HashMap;
import java.util.ArrayList;
import java.util.List;
import java.util.Date;
import java.util.ResourceBundle;

/**
 *
 * @author Ashok Vijayakumar
 */

public class InvestmentsProductOB extends CObservable{
    
    Date curDate = null;
    private final static ClientParseException parseException = ClientParseException.getInstance();
    private final static Logger _log = Logger.getLogger(InvestmentsProductOB.class);
    private ResourceBundle resourceBundle = java.util.ResourceBundle.getBundle("com.see.truetransact.ui.product.investments.InvestmentsProductRB", ProxyParameters.LANGUAGE);
    private ProxyFactory proxy = null;
    private ComboBoxModel  cbmInvestmentBehaves;
    private HashMap map,lookUpHash,keyValue;
    private int _result,_actionType;
    private ArrayList key,value;
    private static InvestmentsProductOB objInvestmentsProductOB;//Singleton Object
    private String lblStatus = ClientConstants.ACTION_STATUS[ClientConstants.ACTIONTYPE_CANCEL];
    private String cboInvestmentBehaves = "";
    private String txtProductID="";
    private String txtDesc="";
    private String txtInvestmentAcHead="";
    private String txtIntReceivedAcHead="";
    private String txtIntPaidAcHead="";
    private String txtPremiumPaidAcHead="";
    private String txtPremiumDepreciationAcHead="";
    private String txtBrokerCommissionAcHead="";
    private String txtDividentReceivedAcHead="";
    private String txtChargeAcHead="";
    private String txtInterestReceivableAcHead="";
    private String txtServiceTaxAcHead="";
    private String txtDividentPaidAcHead="";
    private String txtPremiumReceivedAcHead="";
    private String txtTDSAcHead="";
    private String chkRenewalWithoutTransaction = "N";
    
    
    /** Creates a new instance of ShareProductOB */
    private InvestmentsProductOB() {
        curDate = ClientUtil.getCurrentDate();
        map = new HashMap();
        map.put(CommonConstants.JNDI, "InvestmentsProductJNDI");
        map.put(CommonConstants.HOME, "serverside.product.investments.InvestmentsProductHome");
        map.put(CommonConstants.REMOTE, "serverside.product.investments.InvestmentsProduct");
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
            _log.info("Creating InvestmentsProductOB...");
            objInvestmentsProductOB= new InvestmentsProductOB();
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
            lookup_keys.add("INVESTMENT");
            
            lookUpHash.put(CommonConstants.PARAMFORQUERY, lookup_keys);
            keyValue = ClientUtil.populateLookupData(lookUpHash);
            getKeyValue((HashMap)keyValue.get("INVESTMENT"));
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
    
    public static InvestmentsProductOB getInstance()throws Exception{
        return objInvestmentsProductOB;
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
    
    /** Returns an Instance of ShareProductTO */
    public InvestmentsProductTO getInvestmentsProductTO(String command){
        InvestmentsProductTO objgetInvestmentsProductTO = new InvestmentsProductTO();
        final String yes="Y";
        final String no="N";
        objgetInvestmentsProductTO.setCommand(command);
        if(objgetInvestmentsProductTO.getCommand().equalsIgnoreCase(CommonConstants.TOSTATUS_INSERT)){
            objgetInvestmentsProductTO.setStatus(CommonConstants.STATUS_CREATED);
        }else  if(objgetInvestmentsProductTO.getCommand().equalsIgnoreCase(CommonConstants.TOSTATUS_UPDATE)){
            objgetInvestmentsProductTO.setStatus(CommonConstants.STATUS_MODIFIED);
        }else if(objgetInvestmentsProductTO.getCommand().equalsIgnoreCase(CommonConstants.TOSTATUS_DELETE)){
            objgetInvestmentsProductTO.setStatus(CommonConstants.STATUS_DELETED);
        }
        objgetInvestmentsProductTO.setCboInvestmentBehaves(CommonUtil.convertObjToStr(cbmInvestmentBehaves.getKeyForSelected()));
        objgetInvestmentsProductTO.setTxtProductID(getTxtProductID());
        objgetInvestmentsProductTO.setRenewalWithoutTransaction(getChkRenewalWithoutTransaction());
        objgetInvestmentsProductTO.setTxtPremiumReceivedAcHead(getTxtPremiumReceivedAcHead());
        objgetInvestmentsProductTO.setTxtDesc(getTxtDesc());
        objgetInvestmentsProductTO.setTxtInvestmentAcHead(getTxtInvestmentAcHead());
        objgetInvestmentsProductTO.setTxtIntPaidAcHead(getTxtIntPaidAcHead());
        objgetInvestmentsProductTO.setTxtIntReceivedAcHead(getTxtIntReceivedAcHead());
        objgetInvestmentsProductTO.setTxtPremiumPaidAcHead(getTxtPremiumPaidAcHead());
        objgetInvestmentsProductTO.setTxtPremiumDepreciationAcHead(getTxtPremiumDepreciationAcHead());
        objgetInvestmentsProductTO.setTxtBrokerCommissionAcHead(getTxtBrokerCommissionAcHead());
        objgetInvestmentsProductTO.setTxtDividentReceivedAcHead(getTxtDividentReceivedAcHead());
        objgetInvestmentsProductTO.setTxtChargeAcHead(getTxtChargeAcHead());
        objgetInvestmentsProductTO.setTxtInterestReceivableAcHead(getTxtInterestReceivableAcHead());
        objgetInvestmentsProductTO.setTxtServiceTaxAcHead(getTxtServiceTaxAcHead());
        objgetInvestmentsProductTO.setTxtDividentPaidAcHead(getTxtDividentPaidAcHead());
        objgetInvestmentsProductTO.setStatusBy(TrueTransactMain.USER_ID);
        objgetInvestmentsProductTO.setStatusDt(curDate);
        objgetInvestmentsProductTO.setCommand(command);
        objgetInvestmentsProductTO.setTxtTDSAcHead(getTxtTDSAcHead());
        return objgetInvestmentsProductTO;
        
    }
    
    /** Sets all the ShareProduct values to the OB varibles  there by populatin the UI fields */
    private void setInvestmentsProductTO(InvestmentsProductTO objInvestmentsProductTO){
        
        setCboInvestmentBehaves(CommonUtil.convertObjToStr(getCbmInvestmentBehaves().getDataForKey(objInvestmentsProductTO.getCboInvestmentBehaves())));
        setTxtProductID(CommonUtil.convertObjToStr(objInvestmentsProductTO.getTxtProductID()));
        setChkRenewalWithoutTransaction(objInvestmentsProductTO.getRenewalWithoutTransaction());
        setTxtDesc(CommonUtil.convertObjToStr(objInvestmentsProductTO.getTxtDesc()));
        setTxtInvestmentAcHead(CommonUtil.convertObjToStr(objInvestmentsProductTO.getTxtInvestmentAcHead()));
        setTxtIntPaidAcHead(CommonUtil.convertObjToStr(objInvestmentsProductTO.getTxtIntPaidAcHead()));
        setTxtIntReceivedAcHead(CommonUtil.convertObjToStr(objInvestmentsProductTO.getTxtIntReceivedAcHead()));
        setTxtPremiumPaidAcHead(CommonUtil.convertObjToStr(objInvestmentsProductTO.getTxtPremiumPaidAcHead()));
        setTxtPremiumDepreciationAcHead(CommonUtil.convertObjToStr(objInvestmentsProductTO.getTxtPremiumDepreciationAcHead()));
        setTxtBrokerCommissionAcHead(CommonUtil.convertObjToStr(objInvestmentsProductTO.getTxtBrokerCommissionAcHead()));
        setTxtDividentReceivedAcHead(CommonUtil.convertObjToStr(objInvestmentsProductTO.getTxtDividentReceivedAcHead()));
        setTxtChargeAcHead(CommonUtil.convertObjToStr(objInvestmentsProductTO.getTxtChargeAcHead()));
        setTxtInterestReceivableAcHead(CommonUtil.convertObjToStr(objInvestmentsProductTO.getTxtInterestReceivableAcHead()));
        setTxtServiceTaxAcHead(CommonUtil.convertObjToStr(objInvestmentsProductTO.getTxtServiceTaxAcHead()));
        setTxtDividentPaidAcHead(CommonUtil.convertObjToStr(objInvestmentsProductTO.getTxtDividentPaidAcHead()));
        setTxtPremiumReceivedAcHead(CommonUtil.convertObjToStr(objInvestmentsProductTO.getTxtPremiumReceivedAcHead()));
        setTxtTDSAcHead(CommonUtil.convertObjToStr(objInvestmentsProductTO.getTxtTDSAcHead()));
        notifyObservers();
    }
    
    /* Executes Query using the TO object */
    public void execute(String command) {
        try {
            HashMap term = new HashMap();
            term.put(CommonConstants.BRANCH_ID, TrueTransactMain.BRANCH_ID);
            term.put("InvestmentsProductTO", getInvestmentsProductTO(command));
            
            term.put(CommonConstants.MODULE, getModule());
            term.put(CommonConstants.SCREEN, getScreen());
            term.put(CommonConstants.USER_ID, TrueTransactMain.USER_ID);
            
            int countRec=0;
            HashMap proxyResultMap=null;
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
        setCboInvestmentBehaves("");
        setTxtProductID("");
        setChkRenewalWithoutTransaction("N");
        setTxtDesc("");
        setTxtInvestmentAcHead("");
        setTxtIntPaidAcHead("");
        setTxtIntReceivedAcHead("");
        setTxtIntPaidAcHead("");
        setTxtPremiumPaidAcHead("");
        setTxtPremiumDepreciationAcHead("");
        setTxtBrokerCommissionAcHead("");
        setTxtDividentReceivedAcHead("");
        setTxtChargeAcHead("");
        setTxtInterestReceivableAcHead("");
        setTxtServiceTaxAcHead("");
        setTxtDividentPaidAcHead("");
        setTxtPremiumReceivedAcHead("");
        notifyObservers();
    }
    
    
    /** This checks whether user entered sharetype already exists if it exists
     * it returns true otherwise false */
    public boolean isInvsetMentTypeExists(String txtinvestProdIdSelected){
        boolean exists = false;
        ArrayList resultList =(ArrayList) ClientUtil.executeQuery("getSelectInvestmentProduct",null);
        if(resultList != null){
            for(int i=0; i<resultList.size(); i++){
                HashMap resultMap = (HashMap)resultList.get(i);
                String investProdType =CommonUtil.convertObjToStr(resultMap.get("INVESTMENT_PROD_ID"));
                if(investProdType.equalsIgnoreCase(txtinvestProdIdSelected)){
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
            
            InvestmentsProductTO objInvestmentsProductTO =
            (InvestmentsProductTO) ((List)((HashMap) mapData.get("InvestmentProductTO")).get("InvestmentsProductTO")).get(0);
            setInvestmentsProductTO(objInvestmentsProductTO);
            
        } catch( Exception e ) {
            setResult(ClientConstants.ACTIONTYPE_FAILED);
            e.printStackTrace();
            parseException.logException(e,true);
            
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
    
    public String callForBehaves(){
        return CommonUtil.convertObjToStr(cbmInvestmentBehaves.getKeyForSelected());
    }
    
    /**
     * Getter for property txtProductID.
     * @return Value of property txtProductID.
     */
    public java.lang.String getTxtProductID() {
        return txtProductID;
    }
    
    /**
     * Setter for property txtProductID.
     * @param txtProductID New value of property txtProductID.
     */
    public void setTxtProductID(java.lang.String txtProductID) {
        this.txtProductID = txtProductID;
    }
    
    /**
     * Getter for property txtDesc.
     * @return Value of property txtDesc.
     */
    public java.lang.String getTxtDesc() {
        return txtDesc;
    }
    
    /**
     * Setter for property txtDesc.
     * @param txtDesc New value of property txtDesc.
     */
    public void setTxtDesc(java.lang.String txtDesc) {
        this.txtDesc = txtDesc;
    }
    
    /**
     * Getter for property txtInvestmentAcHead.
     * @return Value of property txtInvestmentAcHead.
     */
    public java.lang.String getTxtInvestmentAcHead() {
        return txtInvestmentAcHead;
    }
    
    /**
     * Setter for property txtInvestmentAcHead.
     * @param txtInvestmentAcHead New value of property txtInvestmentAcHead.
     */
    public void setTxtInvestmentAcHead(java.lang.String txtInvestmentAcHead) {
        this.txtInvestmentAcHead = txtInvestmentAcHead;
    }
    
    /**
     * Getter for property txtIntReceivedAcHead.
     * @return Value of property txtIntReceivedAcHead.
     */
    public java.lang.String getTxtIntReceivedAcHead() {
        return txtIntReceivedAcHead;
    }
    
    /**
     * Setter for property txtIntReceivedAcHead.
     * @param txtIntReceivedAcHead New value of property txtIntReceivedAcHead.
     */
    public void setTxtIntReceivedAcHead(java.lang.String txtIntReceivedAcHead) {
        this.txtIntReceivedAcHead = txtIntReceivedAcHead;
    }
    
    /**
     * Getter for property txtIntPaidAcHead.
     * @return Value of property txtIntPaidAcHead.
     */
    public java.lang.String getTxtIntPaidAcHead() {
        return txtIntPaidAcHead;
    }
    
    /**
     * Setter for property txtIntPaidAcHead.
     * @param txtIntPaidAcHead New value of property txtIntPaidAcHead.
     */
    public void setTxtIntPaidAcHead(java.lang.String txtIntPaidAcHead) {
        this.txtIntPaidAcHead = txtIntPaidAcHead;
    }
    
    /**
     * Getter for property txtPremiumPaidAcHead.
     * @return Value of property txtPremiumPaidAcHead.
     */
    public java.lang.String getTxtPremiumPaidAcHead() {
        return txtPremiumPaidAcHead;
    }
    
    /**
     * Setter for property txtPremiumPaidAcHead.
     * @param txtPremiumPaidAcHead New value of property txtPremiumPaidAcHead.
     */
    public void setTxtPremiumPaidAcHead(java.lang.String txtPremiumPaidAcHead) {
        this.txtPremiumPaidAcHead = txtPremiumPaidAcHead;
    }
    
    /**
     * Getter for property txtPremiumDepreciationAcHead.
     * @return Value of property txtPremiumDepreciationAcHead.
     */
    public java.lang.String getTxtPremiumDepreciationAcHead() {
        return txtPremiumDepreciationAcHead;
    }
    
    /**
     * Setter for property txtPremiumDepreciationAcHead.
     * @param txtPremiumDepreciationAcHead New value of property txtPremiumDepreciationAcHead.
     */
    public void setTxtPremiumDepreciationAcHead(java.lang.String txtPremiumDepreciationAcHead) {
        this.txtPremiumDepreciationAcHead = txtPremiumDepreciationAcHead;
    }
    
    /**
     * Getter for property txtBrokerCommissionAcHead.
     * @return Value of property txtBrokerCommissionAcHead.
     */
    public java.lang.String getTxtBrokerCommissionAcHead() {
        return txtBrokerCommissionAcHead;
    }
    
    /**
     * Setter for property txtBrokerCommissionAcHead.
     * @param txtBrokerCommissionAcHead New value of property txtBrokerCommissionAcHead.
     */
    public void setTxtBrokerCommissionAcHead(java.lang.String txtBrokerCommissionAcHead) {
        this.txtBrokerCommissionAcHead = txtBrokerCommissionAcHead;
    }
    
    /**
     * Getter for property txtDividentReceivedAcHead.
     * @return Value of property txtDividentReceivedAcHead.
     */
    public java.lang.String getTxtDividentReceivedAcHead() {
        return txtDividentReceivedAcHead;
    }
    
    /**
     * Setter for property txtDividentReceivedAcHead.
     * @param txtDividentReceivedAcHead New value of property txtDividentReceivedAcHead.
     */
    public void setTxtDividentReceivedAcHead(java.lang.String txtDividentReceivedAcHead) {
        this.txtDividentReceivedAcHead = txtDividentReceivedAcHead;
    }
    
    /**
     * Getter for property txtServiceTaxAcHead.
     * @return Value of property txtServiceTaxAcHead.
     */
    public java.lang.String getTxtServiceTaxAcHead() {
        return txtServiceTaxAcHead;
    }
    
    /**
     * Setter for property txtServiceTaxAcHead.
     * @param txtServiceTaxAcHead New value of property txtServiceTaxAcHead.
     */
    public void setTxtServiceTaxAcHead(java.lang.String txtServiceTaxAcHead) {
        this.txtServiceTaxAcHead = txtServiceTaxAcHead;
    }
    
    
    
    
    /**
     * Getter for property txtDividentPaidAcHead.
     * @return Value of property txtDividentPaidAcHead.
     */
    public java.lang.String getTxtDividentPaidAcHead() {
        return txtDividentPaidAcHead;
    }
    
    /**
     * Setter for property txtDividentPaidAcHead.
     * @param txtDividentPaidAcHead New value of property txtDividentPaidAcHead.
     */
    public void setTxtDividentPaidAcHead(java.lang.String txtDividentPaidAcHead) {
        this.txtDividentPaidAcHead = txtDividentPaidAcHead;
    }
    
    /**
     * Getter for property txtPremiumReceivedAcHead.
     * @return Value of property txtPremiumReceivedAcHead.
     */
    public java.lang.String getTxtPremiumReceivedAcHead() {
        return txtPremiumReceivedAcHead;
    }
    
    /**
     * Setter for property txtPremiumReceivedAcHead.
     * @param txtPremiumReceivedAcHead New value of property txtPremiumReceivedAcHead.
     */
    public void setTxtPremiumReceivedAcHead(java.lang.String txtPremiumReceivedAcHead) {
        this.txtPremiumReceivedAcHead = txtPremiumReceivedAcHead;
    }
    
    /**
     * Getter for property txtChargeAcHead.
     * @return Value of property txtChargeAcHead.
     */
    public java.lang.String getTxtChargeAcHead() {
        return txtChargeAcHead;
    }
    
    /**
     * Setter for property txtChargeAcHead.
     * @param txtChargeAcHead New value of property txtChargeAcHead.
     */
    public void setTxtChargeAcHead(java.lang.String txtChargeAcHead) {
        this.txtChargeAcHead = txtChargeAcHead;
    }
    
    /**
     * Getter for property txtInterestReceivableAcHead.
     * @return Value of property txtInterestReceivableAcHead.
     */
    public java.lang.String getTxtInterestReceivableAcHead() {
        return txtInterestReceivableAcHead;
    }
    
    /**
     * Setter for property txtInterestReceivableAcHead.
     * @param txtInterestReceivableAcHead New value of property txtInterestReceivableAcHead.
     */
    public void setTxtInterestReceivableAcHead(java.lang.String txtInterestReceivableAcHead) {
        this.txtInterestReceivableAcHead = txtInterestReceivableAcHead;
    }

    public String getTxtTDSAcHead() {
        return txtTDSAcHead;
    }

    public void setTxtTDSAcHead(String txtTDSAcHead) {
        this.txtTDSAcHead = txtTDSAcHead;
    }    

    public String getChkRenewalWithoutTransaction() {
        return chkRenewalWithoutTransaction;
    }

    public void setChkRenewalWithoutTransaction(String chkRenewalWithoutTransaction) {
        this.chkRenewalWithoutTransaction = chkRenewalWithoutTransaction;
    }
    
}