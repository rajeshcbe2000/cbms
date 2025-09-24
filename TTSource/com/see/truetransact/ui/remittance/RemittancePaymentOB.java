/* Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition.
 * 
 *
 * RemittancePaymentOB.java
 *
 * Created on December 31, 2003, 3:26 PM
 */
/* Modified on 6 May 2004
 * method compareSerialNoAndVariableNo is added
 *
 */
package com.see.truetransact.ui.remittance;

import java.util.List;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.ArrayList;
import java.util.Observable;
import com.see.truetransact.commonutil.DateUtil;
import com.see.truetransact.commonutil.CommonConstants;
import com.see.truetransact.commonutil.TTException;
import com.see.truetransact.commonutil.CommonUtil;
import com.see.truetransact.clientutil.ComboBoxModel;
import com.see.truetransact.clientproxy.ProxyFactory;
import com.see.truetransact.clientutil.ClientConstants;
import com.see.truetransact.clientutil.ClientUtil;
import com.see.truetransact.uicomponent.COptionPane;
import com.see.truetransact.transferobject.remittance.RemittancePaymentTO;
import com.see.truetransact.clientexception.ClientParseException;
import com.see.truetransact.ui.TrueTransactMain;
import com.see.truetransact.uicomponent.CObservable;
import com.see.truetransact.uicomponent.CInternalFrame;
import com.see.truetransact.ui.common.transaction.TransactionOB;

/**
 *
 * @author  Lohith R.
 */
public class RemittancePaymentOB extends CObservable{
    private ComboBoxModel cbmInstrumentType;
    private ComboBoxModel cbmPayStatus;
    private String lblStatus = ClientConstants.ACTION_STATUS[ClientConstants.ACTIONTYPE_CANCEL];
    private final static ClientParseException parseException = ClientParseException.getInstance();
    private static RemittancePaymentOB remittancePaymentOB; // singleton object
    private TransactionOB transactionOB;
    final RemittancePaymentRB objRemittancePaymentRB = new RemittancePaymentRB();
    private String cboInstrumentType = "";
    private String txtSerialNumber = "";
    private String cboPayStatus = "";
    private String txtCharges = "";
    private String txtServiceTax = "";
    private String txtPayAmount = "";
    private String lblPayableAmount = "" ;
    private String txtRemarks = "";
    private String txtAddress = "";
    private String lblRemitPayId = "";
    private String PaymentDt = "";
    
    private String lblDateIssue = "";
    private String lblFavouring = "" ;
    private String lblIssueBank = "" ;
    private String lblBranchCode = "" ;
    
    private String txtNumber1 = "";
    private String txtNumber2 = "";
    private int actionType;
    private int result;
    private HashMap operationMap;
    private ProxyFactory proxy;
    private HashMap lookUpHash;
    private HashMap keyValue;
    private ArrayList key;
    private ArrayList value;
    private String warningSerialNo ="";
    private String checkSerialNo ="";
    private String duplicateDt = "";
    private String lblDupRevValue = "";
    private String lblAccHeadBalDisplay = "";
    private String lblAccHeadProdIdDisplay = "";
    private HashMap _authorizeMap;
    private HashMap oldTransDetMap = null;
    
    private LinkedHashMap allowedIssueTO = null;
    private LinkedHashMap issueDetailsTO = null;
    private LinkedHashMap deletedIssueTO = null;
    private LinkedHashMap allowedTransactionDetailsTO = null;
    private LinkedHashMap transactionDetailsTO = null;
    private LinkedHashMap deletedTransactionDetailsTO = null;
    
    private final String DELETED_TRANS_TOs = "DELETED_TRANS_TOs";
    private final String NOT_DELETED_TRANS_TOs = "NOT_DELETED_TRANS_TOs";
     private final String DELETED_ISSUE_TOs = "DELETED_ISSUE_TOs";
    private final String NOT_DELETED_ISSUE_TOs = "NOT_DELETED_ISSUE_TOs";
    private String payableAt = "" ;
    
    
    
    static {
        try {
            remittancePaymentOB = new RemittancePaymentOB();
        } catch(Exception e) {
            parseException.logException(e,true);
        }
    }
    
    public RemittancePaymentOB()throws Exception {
        proxy = ProxyFactory.createProxy();
        setOperationMap();
        fillDropdown();
    }
    
    /** Creates a new instance of RemittancePaymentOB */
    public static RemittancePaymentOB getInstance() {
        return remittancePaymentOB;
    }
    
    /* Sets the HashMap required to set JNDI,Home and Remote*/
    private void setOperationMap() throws Exception{
        operationMap = new HashMap();
        operationMap.put(CommonConstants.JNDI, "RemittancePaymentJNDI");
        operationMap.put(CommonConstants.HOME, "remittance.RemittancePaymentHome");
        operationMap.put(CommonConstants.REMOTE, "remittance.RemittancePayment");
    }
    
    /** A method to get the combo box values */
    private void fillDropdown() throws Exception{
        lookUpHash = new HashMap();
        lookUpHash.put(CommonConstants.MAP_NAME,null);
        final ArrayList lookup_keys = new ArrayList();
        
        lookup_keys.add("REMIT_PAY_STATUS");
        lookup_keys.add("REMITTANCE.BEHAVES");
        lookUpHash.put(CommonConstants.PARAMFORQUERY, lookup_keys);
        keyValue = ClientUtil.populateLookupData(lookUpHash);
        
        
        getKeyValue((HashMap)keyValue.get("REMIT_PAY_STATUS"));
        cbmPayStatus = new ComboBoxModel(key,value);
        makeNull();
        
        /* ProdId is taken from Remittance_product */
        lookUpHash.put(CommonConstants.MAP_NAME,"RemitIssuegetProdId");
        lookUpHash.put(CommonConstants.PARAMFORQUERY, null);
        
        keyValue = ClientUtil.populateLookupData(lookUpHash);
        getKeyValue((HashMap)keyValue.get(CommonConstants.DATA));
        cbmInstrumentType = new ComboBoxModel(key,value);
    }
    
    private void getKeyValue(HashMap keyValue)  throws Exception{
        key = (ArrayList)keyValue.get(CommonConstants.KEY);
        value = (ArrayList)keyValue.get(CommonConstants.VALUE);
    }
    
    private void makeNull(){
        key = null;
        value = null;
    }
    
     public void getAccountHeadForProduct(String productId) {
        try {
            final HashMap remitissueMap = new HashMap();
            remitissueMap.put("PROD_ID",productId);
            final List resultList = ClientUtil.executeQuery("getAccountHeadForProductId", remitissueMap);
            if( resultList.size() >0 && resultList != null){
                final HashMap resultMap = (HashMap)resultList.get(0);
                setLblAccHeadProdIdDisplay(CommonUtil.convertObjToStr(resultMap.get("PAY_HD")));
                HashMap dataMap = new HashMap();
                dataMap.put("AC_HD_ID", CommonUtil.convertObjToStr(resultMap.get("PAY_HD")));
                dataMap.put("BRANCH_CODE", TrueTransactMain.BRANCH_ID);
                final List lst = ClientUtil.executeQuery("getAccountHeadBalForProdId", dataMap);
                if(lst != null && lst.size() > 0){
                      dataMap = (HashMap)lst.get(0);
                      setLblAccHeadBalDisplay(CommonUtil.convertObjToStr(dataMap.get("CUR_BAL")));
                }else
                    setLblAccHeadBalDisplay("");
              
            }else{
                setLblAccHeadProdIdDisplay("");
            }
        }catch(Exception e){
            parseException.logException(e,true);
        }
    }
    
    void setCboInstrumentType(String cboInstrumentType){
        this.cboInstrumentType = cboInstrumentType;
        setChanged();
    }
    String getCboInstrumentType(){
        return this.cboInstrumentType;
    }
    
    public void setDuplicateDt(String duplicateDt){
        this.duplicateDt = duplicateDt;
    }
    
    String getDuplicateDt(){
        return this.duplicateDt;
    }
    
    void setCbmInstrumentType(ComboBoxModel cbmInstrumentType){
        this.cbmInstrumentType =(ComboBoxModel) cbmInstrumentType.getKeyForSelected();
        setChanged();
    }
    ComboBoxModel getCbmInstrumentType(){
        return cbmInstrumentType;
    }
    
    void setTxtSerialNumber(String txtSerialNumber){
        this.txtSerialNumber = txtSerialNumber;
        setChanged();
    }
    String getTxtSerialNumber(){
        return this.txtSerialNumber;
    }
    
    
    void setCboPayStatus(String cboPayStatus){
        this.cboPayStatus = cboPayStatus;
        setChanged();
    }
    String getCboPayStatus(){
        return this.cboPayStatus;
    }
    
    void setCbmPayStatus(ComboBoxModel cbmPayStatus){
        this.cbmPayStatus = cbmPayStatus;
        setChanged();
    }
    ComboBoxModel getCbmPayStatus(){
        return cbmPayStatus;
    }
    
    void setTxtCharges(String txtCharges){
        this.txtCharges = txtCharges;
        setChanged();
    }
    String getTxtCharges(){
        return this.txtCharges;
    }
    
    void setTxtAddress(String txtAddress){
        this.txtAddress = txtAddress;
        setChanged();
    }
    String getTxtAddress(){
        return this.txtAddress;
    }
    
    void setTxtPayAmount(String txtPayAmount){
        this.txtPayAmount = txtPayAmount;
        setChanged();
    }
    String getTxtPayAmount(){
        return this.txtPayAmount;
    }
    
    
    void setTxtRemarks(String txtRemarks){
        this.txtRemarks = txtRemarks;
        setChanged();
    }
    String getTxtRemarks(){
        return this.txtRemarks;
    }
    
    void setLblRemitPayId(String lblRemitPayId){
        this.lblRemitPayId = lblRemitPayId;
        setChanged();
    }
    String getLblRemitPayId(){
        return this.lblRemitPayId;
    }
    
    /**
     * Getter for property txtNumber1.
     * @return Value of property txtNumber1.
     */
    public java.lang.String getTxtNumber1() {
        return txtNumber1;
    }
    
    /**
     * Setter for property txtNumber1.
     * @param txtNumber1 New value of property txtNumber1.
     */
    public void setTxtNumber1(java.lang.String txtNumber1) {
        this.txtNumber1 = txtNumber1;
        setChanged();
    }
    
    /**
     * Getter for property txtNumber2.
     * @return Value of property txtNumber2.
     */
    public java.lang.String getTxtNumber2() {
        return txtNumber2;
    }
    
    /**
     * Setter for property txtNumber2.
     * @param txtNumber2 New value of property txtNumber2.
     */
    public void setTxtNumber2(java.lang.String txtNumber2) {
        this.txtNumber2 = txtNumber2;
        setChanged();
    }
    
    // Setter for Authorization.
    public void setAuthorizeMap(HashMap authorizeMap) {
        _authorizeMap = authorizeMap;
    }
    
    // Getter for Authorization.
    public HashMap getAuthorizeMap() {
        return _authorizeMap;
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
    public void setStatus(){
        this.setLblStatus( ClientConstants.ACTION_STATUS[this.getActionType()]);
        notifyObservers();
    }
    public void setResultStatus(){
        this.setLblStatus(ClientConstants.RESULT_STATUS[this.getResult()]);
        notifyObservers();
    }
    public int getResult(){
        return result;
    }
    public void setActionType(int action) {
        this.actionType = action;
        setChanged();
    }
    
    public int getActionType(){
        return this.actionType;
    }
    
    /** To perform the appropriate operation */
    public void doAction() {
        try {
            if( actionType != ClientConstants.ACTIONTYPE_CANCEL ){
                //If actionType has got propervalue then doActionPerform, else throw error
                if( getCommand() != null || getAuthorizeMap() != null){
                    doActionPerform();
                }
                else{
                    final RemittancePaymentRB objRemittancePaymentRB = new RemittancePaymentRB();
                    throw new TTException(objRemittancePaymentRB.getString("TOCommandError"));
                }
            }
        } catch (Exception e) {
            setResult(ClientConstants.ACTIONTYPE_FAILED);
            parseException.logException(e,true);
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
        final HashMap data = new HashMap();
        data.put("MODE", getCommand());
        if(getActionType() == ClientConstants.ACTIONTYPE_AUTHORIZE || getActionType() == ClientConstants.ACTIONTYPE_REJECT
        || getActionType() == ClientConstants.ACTIONTYPE_EXCEPTION){
            data.put(CommonConstants.AUTHORIZEMAP, getAuthorizeMap());
            data.put(CommonConstants.USER_ID, _authorizeMap.get(CommonConstants.USER_ID));
        }else {
            final RemittancePaymentTO objRemittancePaymentTO = setRemittancePaymentData();
            objRemittancePaymentTO.setCommand(getCommand());
            if (transactionDetailsTO == null)
                transactionDetailsTO = new LinkedHashMap();
            
            if (deletedTransactionDetailsTO != null) {
                transactionDetailsTO.put(DELETED_TRANS_TOs,deletedTransactionDetailsTO);
                deletedTransactionDetailsTO = null;
            }
            
            transactionDetailsTO.put(NOT_DELETED_TRANS_TOs,allowedTransactionDetailsTO);
            allowedTransactionDetailsTO = null;
            
            data.put("TransactionTO",transactionDetailsTO);
            
            data.put("RemittancePaymentTO",objRemittancePaymentTO);
            data.put(CommonConstants.MODULE, getModule());
            data.put(CommonConstants.SCREEN, getScreen());
            System.out.println("DATA## : " + data);
        }
        if (oldTransDetMap != null)
        if (oldTransDetMap.size() > 0) {

        if (oldTransDetMap.containsKey("AMT_CASH_TRANS_DETAILS"))
            data.put("AMT_CASH_TRANS_DETAILS", oldTransDetMap.get("AMT_CASH_TRANS_DETAILS"));
        if (oldTransDetMap.containsKey("AMT_TRANSFER_TRANS_DETAILS"))
            data.put("AMT_TRANSFER_TRANS_DETAILS", oldTransDetMap.get("AMT_TRANSFER_TRANS_DETAILS"));
        if (oldTransDetMap.containsKey("AMT_TRANSFER_TRANS"))
            data.put("AMT_TRANSFER_TRANS", oldTransDetMap.get("AMT_TRANSFER_TRANS"));
        if (oldTransDetMap.containsKey("CHARGES_TRANSFER_TRANS_DETAILS"))
            data.put("CHARGES_TRANSFER_TRANS_DETAILS", oldTransDetMap.get("CHARGES_TRANSFER_TRANS_DETAILS"));
        if (oldTransDetMap.containsKey("TRANSCHRG_TRANSFER_TRANS_DETAILS"))
            data.put("TRANSCHRG_TRANSFER_TRANS_DETAILS", oldTransDetMap.get("TRANSCHRG_TRANSFER_TRANS_DETAILS"));
        if (oldTransDetMap.containsKey("SERVICE_TRANSFER_TRANS_DETAILS"))
            data.put("SERVICE_TRANSFER_TRANS_DETAILS", oldTransDetMap.get("SERVICE_TRANSFER_TRANS_DETAILS"));
        if (oldTransDetMap.containsKey("TRANSERVICE_TRANSFER_TRANS_DETAILS"))
            data.put("TRANSERVICE_TRANSFER_TRANS_DETAILS", oldTransDetMap.get("TRANSERVICE_TRANSFER_TRANS_DETAILS"));
        
        }
        
        oldTransDetMap = null;
        System.out.println("DATA###### : " + data);
        
        HashMap proxyResultMap = proxy.execute(data, operationMap);
        setProxyReturnMap(proxyResultMap);
        setResult(actionType);
         if (proxyResultMap != null && proxyResultMap.containsKey("REMIT_PAY_ID"))  {
            ClientUtil.showMessageWindow("REMIT "+CommonUtil.convertObjToStr(proxyResultMap.get("PAY_STATUS"))+" ID ."+CommonUtil.convertObjToStr(proxyResultMap.get("REMIT_PAY_ID"))+"\n"
            /* +"Instrument No. : "+CommonUtil.convertObjToStr(proxyResultMap.get("INST_NO1")) + "-" + CommonUtil.convertObjToStr(proxyResultMap.get("INST_NO2"))*/);
        }
//        actionType = ClientConstants.ACTIONTYPE_NEW;
//        resetForm();
    }
    
    public void setResult(int result) {
        this.result = result;
        setChanged();
    }
    
    public String getCancellationCharge(HashMap params){
        List charge = ClientUtil.executeQuery("getPaymentCancelCharge", params) ;
        String chargeVal = "" ;
        if(charge != null && charge.size()!=0)
            chargeVal = (String)(charge.get(0));
        return chargeVal ;
        
    }
    
    /** To retrieve a particular customer's accountclosing record */
    public void getData(HashMap whereMap) {
        try{
            final HashMap data = (HashMap)proxy.executeQuery(whereMap,operationMap);
            populateOB(data);
            List list = (List) data.get("TransactionTO");
            transactionOB.setDetails(list);
            ttNotifyObservers();
        }catch(Exception e){
            parseException.logException(e,true);
        }
    }
    
    public void ttNotifyObservers(){
        notifyObservers();
    }
    
    /* To set common data in the Transfer Object*/
    public RemittancePaymentTO setRemittancePaymentData() {
        final RemittancePaymentTO objRemittancePaymentTO = new RemittancePaymentTO();
        try{
            if(getCommand().equals(CommonConstants.TOSTATUS_INSERT)){
                objRemittancePaymentTO.setCreatedBy(TrueTransactMain.USER_ID);
            }else{
                objRemittancePaymentTO.setStatusBy(TrueTransactMain.USER_ID);
            }
            objRemittancePaymentTO.setRemitPayId(getLblRemitPayId());
            objRemittancePaymentTO.setInstrumentType((String)cbmInstrumentType.getKeyForSelected());
            objRemittancePaymentTO.setPayStatus((String)cbmPayStatus.getKeyForSelected());
            objRemittancePaymentTO.setBranchId(TrueTransactMain.BRANCH_ID);
            objRemittancePaymentTO.setSerialNo(getTxtSerialNumber());
            objRemittancePaymentTO.setCharges(CommonUtil.convertObjToDouble(getTxtCharges()));
            objRemittancePaymentTO.setServiceTax(CommonUtil.convertObjToDouble(getTxtServiceTax()));
            objRemittancePaymentTO.setPayAmt(CommonUtil.convertObjToDouble(getLblPayableAmount()));
            objRemittancePaymentTO.setRemarks(getTxtRemarks());
            objRemittancePaymentTO.setAddress(getTxtAddress());
            objRemittancePaymentTO.setInstrumentNo1(getTxtNumber1());
            objRemittancePaymentTO.setInstrumentNo2(getTxtNumber2());
        }catch(Exception e){
            parseException.logException(e,true);
        }
        return objRemittancePaymentTO;
    }
    
    /* To populate the screen */
    public void populateData(HashMap whereMap) {
        try {
            HashMap mapData = proxy.executeQuery(whereMap, operationMap);
            System.out.println("mapData : " + mapData);
            populateOB(mapData);
            transactionOB.setDetails((List)mapData.get("TransactionTO"));
        } catch( Exception e ) {
            e.printStackTrace();
            parseException.logException(e,true);
        }
    }
    
    private void populateOB(HashMap mapData) throws Exception{
        RemittancePaymentTO objRemittancePaymentTO;
        objRemittancePaymentTO = (RemittancePaymentTO) ((List)mapData.get("RemittancePaymentTO")).get(0);
        setLblRemitPayId(objRemittancePaymentTO.getRemitPayId());
        setCboInstrumentType(CommonUtil.convertObjToStr(getCbmInstrumentType().getDataForKey(objRemittancePaymentTO.getInstrumentType())));
        setTxtSerialNumber(objRemittancePaymentTO.getSerialNo());
        setCboPayStatus(CommonUtil.convertObjToStr(getCbmPayStatus().getDataForKey(objRemittancePaymentTO.getPayStatus())));
        setTxtCharges(CommonUtil.convertObjToStr(objRemittancePaymentTO.getCharges()));
        setTxtServiceTax(CommonUtil.convertObjToStr(objRemittancePaymentTO.getServiceTax()));
        double payable = CommonUtil.convertObjToDouble(objRemittancePaymentTO.getPayAmt()).doubleValue() + 
        CommonUtil.convertObjToDouble(objRemittancePaymentTO.getCharges()).doubleValue() +
        CommonUtil.convertObjToDouble(objRemittancePaymentTO.getServiceTax()).doubleValue();
        setTxtPayAmount(String.valueOf(payable));  
        setLblPayableAmount(CommonUtil.convertObjToStr(objRemittancePaymentTO.getPayAmt()));
        setTxtRemarks(objRemittancePaymentTO.getRemarks());
        setTxtAddress(objRemittancePaymentTO.getAddress());
        setTxtNumber1(objRemittancePaymentTO.getInstrumentNo1());
        setTxtNumber2(objRemittancePaymentTO.getInstrumentNo2());
        setLblFavouring(objRemittancePaymentTO.getFavouring());
        setLblIssueBank(objRemittancePaymentTO.getDraweeBank());
        setLblBranchCode(objRemittancePaymentTO.getDraweeBranch());
        setLblDateIssue(objRemittancePaymentTO.getIssueDt());
        setStatusBy(objRemittancePaymentTO.getStatusBy());
        setAuthorizeStatus(objRemittancePaymentTO.getAuthorizeStatus());
        setPaymentDt(CommonUtil.convertObjToStr(objRemittancePaymentTO.getRemitPayDt()));
        oldTransDetMap = new HashMap();
        
        if (mapData.containsKey("AMT_CASH_TRANS_DETAILS"))
            oldTransDetMap.put("AMT_CASH_TRANS_DETAILS", mapData.get("AMT_CASH_TRANS_DETAILS"));
        if (mapData.containsKey("AMT_TRANSFER_TRANS_DETAILS"))
            oldTransDetMap.put("AMT_TRANSFER_TRANS_DETAILS", mapData.get("AMT_TRANSFER_TRANS_DETAILS"));
        if (mapData.containsKey("AMT_TRANSFER_TRANS"))
            oldTransDetMap.put("AMT_TRANSFER_TRANS", mapData.get("AMT_TRANSFER_TRANS"));
        if (mapData.containsKey("CHARGES_TRANSFER_TRANS_DETAILS"))
            oldTransDetMap.put("CHARGES_TRANSFER_TRANS_DETAILS", mapData.get("CHARGES_TRANSFER_TRANS_DETAILS"));
        if (mapData.containsKey("TRANSCHRG_TRANSFER_TRANS_DETAILS"))
            oldTransDetMap.put("TRANSCHRG_TRANSFER_TRANS_DETAILS", mapData.get("TRANSCHRG_TRANSFER_TRANS_DETAILS"));
        if (mapData.containsKey("SERVICE_TRANSFER_TRANS_DETAILS"))
            oldTransDetMap.put("SERVICE_TRANSFER_TRANS_DETAILS", mapData.get("SERVICE_TRANSFER_TRANS_DETAILS"));
         if (mapData.containsKey("TRANSERVICE_TRANSFER_TRANS_DETAILS"))
            oldTransDetMap.put("TRANSERVICE_TRANSFER_TRANS_DETAILS", mapData.get("TRANSERVICE_TRANSFER_TRANS_DETAILS"));
        
        
       System.out.println("oldTransDetMap%%%%%% : " + oldTransDetMap);
        notifyObservers();
    }
    
    /** To reset the txt fields to null */
    public void resetForm(){
        _authorizeMap= null;
        setLblRemitPayId("");
        setCboInstrumentType("");
        setCboPayStatus("");
        setTxtSerialNumber("");
        setTxtCharges("");
        setTxtServiceTax("");
        setTxtPayAmount("");
        setLblPayableAmount("");
        setTxtRemarks("");
        setTxtAddress("");
        setTxtNumber1("");
        setTxtNumber2("");
        setLblFavouring("") ;
        setLblIssueBank("") ;
        setLblBranchCode("") ;
        setLblDateIssue("");
        setLblDupRevValue("");
        setLblAccHeadBalDisplay("");
        setLblAccHeadProdIdDisplay("");
        setPaymentDt("");
        setRdoCancel(false);
        setRdoPayment(false);
        notifyObservers();
    }
    private boolean rdoPayment = false;
    private boolean rdoCancel = false;
    // To check whether the Serial No in Remittance payment equasl
    // Variable No in Remittance issue
    public boolean checkSerialNoAndVariableNo(String stmtName, String fieldName) {
        boolean exists = false;
        if(getTxtSerialNumber().length() > 0 && getCboInstrumentType().length() > 0){
            HashMap where = new HashMap();
            where.put("PROD_ID", cbmInstrumentType.getKeyForSelected());
            where.put("VARIABLE_NO", getTxtSerialNumber());
            ArrayList resultList = (ArrayList) ClientUtil.executeQuery(stmtName, where);
            if(resultList.size() > 0){
                int cnt = CommonUtil.convertObjToInt(resultList.get(0));
                if (cnt > 0) {
                    exists = true;
                }
            }
        }
        return exists;
    }
     public String executeQueryForCharge(String productId, String category, String amount, String chargeType, String city, String bankCode, String branchCode){
        double inputAmt = CommonUtil.convertObjToDouble(amount).doubleValue() ;
        double calculatedCharge = 0.0;
        HashMap where = new HashMap();
        where.put("PROD_ID", productId);
        where.put("CATEGORY", category);
        where.put("AMOUNT", amount);
        where.put("CHARGE_TYPE", chargeType);
        getPayableAtBranch();
        where.put("PAYABLE", getPayableAt());
        where.put("BANK_CODE", bankCode);
        where.put("BRANCH_CODE", branchCode);
        List outList = executeQuery("getExchange", where);
        System.out.println("where:" + where);
        System.out.println("outList:" + outList);
        where = null ;
        if(outList.size() > 0){ //If a charge is configured. else return default zero
//            for(int i = 0;i < outList.size(); i++){
            HashMap outputMap  = (HashMap)outList.get(0);
            
            if(outputMap != null){
                double toAmt = CommonUtil.convertObjToDouble(outputMap.get("TO_AMT")).doubleValue() ;
                double fixedRate = CommonUtil.convertObjToDouble(outputMap.get("CHARGE")).doubleValue() ;
                double percentage = CommonUtil.convertObjToDouble(outputMap.get("PERCENTAGE")).doubleValue() ;

                double forEveryAmt = CommonUtil.convertObjToDouble(outputMap.get("FOR_EVERY_AMT")).doubleValue() ;
                double forEveryRate = CommonUtil.convertObjToDouble(outputMap.get("FOR_EVERY_RATE")).doubleValue() ;
                String forEveryType = CommonUtil.convertObjToStr(outputMap.get("FOR_EVERY_TYPE")) ;
                outputMap = null ;
                if(percentage != 0)
                    calculatedCharge += (inputAmt * percentage) / 100;
                if(fixedRate != 0)
                    calculatedCharge += fixedRate ;
                
                if (inputAmt > toAmt) {
                    if(forEveryAmt != 0){
                        double remainder = inputAmt - toAmt ;
                        if(forEveryType.toUpperCase().equals("AMOUNT")) //Value from Lookup Table
                            calculatedCharge += (remainder / forEveryAmt) * forEveryRate ;
                        else if(forEveryType.toUpperCase().equals("PERCENTAGE"))//Value from Lookup Table
                            calculatedCharge += ((remainder / forEveryAmt) * percentage)/100 ;
                    }
                }
            }
//        }
    }
        System.out.println("===========>>>>>>>>calculatedCharge : " + calculatedCharge);
        calculatedCharge = (double)getNearest((long)(calculatedCharge *100),100)/100;
        return String.valueOf(calculatedCharge) ;
        
    }
    public String calServiceTax(String exchange,String productId,String category,String amount, String chargeType, String city, String bankCode, String branchCode){
        double inputAmt = CommonUtil.convertObjToDouble(amount).doubleValue() ;
        double exchangeAmt = CommonUtil.convertObjToDouble(exchange).doubleValue() ;
        double calculatedCharge = 0.0; 
        if(!bankCode.equalsIgnoreCase("")){
            HashMap where = new HashMap();
            where.put("PROD_ID", productId);
            where.put("CATEGORY", category);
            where.put("AMOUNT", amount);
            where.put("CHARGE_TYPE", chargeType);
            getPayableAtBranch();
            where.put("PAYABLE", getPayableAt());
            where.put("BANK_CODE", bankCode);
            where.put("BRANCH_CODE", branchCode);
            List outList = executeQuery("getServiceTax", where);
            System.out.println("where:" + where);
            System.out.println("outList:" + outList);
            where = null ;
            if(outList.size() > 0){ //If a charge is configured. else return default zero
                HashMap outputMap  = (HashMap)outList.get(0);

                if(outputMap != null){
                    double serviceTax = CommonUtil.convertObjToDouble(outputMap.get("SERVICE_TAX")).doubleValue() ;
                    if(serviceTax != 0)
                        calculatedCharge = (exchangeAmt * serviceTax) / 100;
                }
            }
            System.out.println("===========>>>>>>>>calculatedCharge : " + calculatedCharge);
            calculatedCharge = (double)getNearest((long)(calculatedCharge *100),100)/100;
           }
        return String.valueOf(calculatedCharge) ; 
     
    }
    public long getNearest(long number,long roundingFactor)  {
        long roundingFactorOdd = roundingFactor;
        if ((roundingFactor%2) != 0)
            roundingFactorOdd +=1;
        long mod = number%roundingFactor;
        if ((mod < (roundingFactor/2)) || (mod < (roundingFactorOdd/2)))
            return lower(number,roundingFactor);
        else
            return higher(number,roundingFactor);
    }
    public long lower(long number,long roundingFactor) {
        long mod = number%roundingFactor;
        return number-mod;
    }
    
    public long higher(long number,long roundingFactor) {
        long mod = number%roundingFactor;
        if ( mod == 0)
            return number;
        return (number-mod) + roundingFactor ;
    }
     private void getPayableAtBranch(){
        String prodId = (String)cbmInstrumentType.getKeyForSelected();
        String payableAt = "" ;
        HashMap map = new HashMap();
        map.put("PRODUCT_ID", prodId) ;
        List tempList = ClientUtil.executeQuery("getPayableAt", map);
        System.out.println("map = "+map);
        System.out.println("in getPayableAtBranch() tempList.size() : "+tempList.size());
        if(tempList.size() > 0){
            setPayableAt(CommonUtil.convertObjToStr(tempList.get(0))) ;
        System.out.println("tempList.get(0) : "+tempList.get(0));}
    }
    /**
     * Executes the query
     */
    private List executeQuery(String mapName, HashMap where){
        List returnList = null;
        try{
            returnList = (List) ClientUtil.executeQuery(mapName, where);
        }catch(Exception e){
            parseException.logException(e,true);
        }
        return returnList;
    }
    // To set the Authorize Status as PAID
    public void setAuthorizeStatusAsPaid(){
        if(getTxtSerialNumber().length()>0 ){
            HashMap where = new HashMap();
            where.put("VARIABLE_NO",getTxtSerialNumber());
            ClientUtil.execute("setAuthorizeStatusPaid",where);
            where = null;
        }
    }
    
    public HashMap getResultMap(String stmtName, String variableNo){
        ArrayList resultList = new ArrayList();
        HashMap resultMap = null;
        try{
            HashMap where = new HashMap();
            where.put("VARIABLE_NO", variableNo);
            resultList = (ArrayList) ClientUtil.executeQuery(stmtName, where);
            if(resultList.size() > 0){
                resultMap = (HashMap) resultList.get(0);
            }
        }catch(Exception e){
            
        }
        return resultMap;
    }
    
    /**
     * Setter for property transactionOB.
     * @param transactionOB New value of property transactionOB.
     */
    public void setTransactionOB(TransactionOB transactionOB) {
        this.transactionOB = transactionOB;
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
        //System.out.println("In OB of RemPayment : " + allowedTransactionDetailsTO);
        this.allowedTransactionDetailsTO = allowedTransactionDetailsTO;
    }
    
    /**
     * Getter for property lblPayableAmount.
     * @return Value of property lblPayableAmount.
     */
    public java.lang.String getLblPayableAmount() {
        return lblPayableAmount;
    }
    
    /**
     * Setter for property lblPayableAmount.
     * @param lblPayableAmount New value of property lblPayableAmount.
     */
    public void setLblPayableAmount(java.lang.String lblPayableAmount) {
        this.lblPayableAmount = lblPayableAmount;
        setChanged();
    }
    
    /**
     * Getter for property lblFavouring.
     * @return Value of property lblFavouring.
     */
    public java.lang.String getLblFavouring() {
        return lblFavouring;
    }
    
    /**
     * Setter for property lblFavouring.
     * @param lblFavouring New value of property lblFavouring.
     */
    public void setLblFavouring(java.lang.String lblFavouring) {
        this.lblFavouring = lblFavouring;
    }
    
    /**
     * Getter for property lblIssueBank.
     * @return Value of property lblIssueBank.
     */
    public java.lang.String getLblIssueBank() {
        return lblIssueBank;
    }
    
    /**
     * Setter for property lblIssueBank.
     * @param lblIssueBank New value of property lblIssueBank.
     */
    public void setLblIssueBank(java.lang.String lblIssueBank) {
        this.lblIssueBank = lblIssueBank;
    }
    
    /**
     * Getter for property lblBranchCode.
     * @return Value of property lblBranchCode.
     */
    public java.lang.String getLblBranchCode() {
        return lblBranchCode;
    }
    
    /**
     * Setter for property lblBranchCode.
     * @param lblBranchCode New value of property lblBranchCode.
     */
    public void setLblBranchCode(java.lang.String lblBranchCode) {
        this.lblBranchCode = lblBranchCode;
    }
    
    /**
     * Getter for property lblDateIssue.
     * @return Value of property lblDateIssue.
     */
    public java.lang.String getLblDateIssue() {
        return lblDateIssue;
    }
    
    /**
     * Setter for property lblDateIssue.
     * @param lblDateIssue New value of property lblDateIssue.
     */
    public void setLblDateIssue(java.lang.String lblDateIssue) {
        this.lblDateIssue = lblDateIssue;
    }
    
    /**
     * Getter for property oldTransDetMap.
     * @return Value of property oldTransDetMap.
     */
    public java.util.HashMap getOldTransDetMap() {
        return oldTransDetMap;
    }
    
    /**
     * Setter for property oldTransDetMap.
     * @param oldTransDetMap New value of property oldTransDetMap.
     */
    public void setOldTransDetMap(java.util.HashMap oldTransDetMap) {
        this.oldTransDetMap = oldTransDetMap;
    }
    
    /**
     * Getter for property lblAccHeadBalDisplay.
     * @return Value of property lblAccHeadBalDisplay.
     */
    public java.lang.String getLblAccHeadBalDisplay() {
        return lblAccHeadBalDisplay;
    }
    
    /**
     * Setter for property lblAccHeadBalDisplay.
     * @param lblAccHeadBalDisplay New value of property lblAccHeadBalDisplay.
     */
    public void setLblAccHeadBalDisplay(java.lang.String lblAccHeadBalDisplay) {
        this.lblAccHeadBalDisplay = lblAccHeadBalDisplay;
    }
    
    /**
     * Getter for property lblAccHeadProdIdDisplay.
     * @return Value of property lblAccHeadProdIdDisplay.
     */
    public java.lang.String getLblAccHeadProdIdDisplay() {
        return lblAccHeadProdIdDisplay;
    }
    
    /**
     * Setter for property lblAccHeadProdIdDisplay.
     * @param lblAccHeadProdIdDisplay New value of property lblAccHeadProdIdDisplay.
     */
    public void setLblAccHeadProdIdDisplay(java.lang.String lblAccHeadProdIdDisplay) {
        this.lblAccHeadProdIdDisplay = lblAccHeadProdIdDisplay;
    }
    
    /**
     * Getter for property PaymentDt.
     * @return Value of property PaymentDt.
     */
    public java.lang.String getPaymentDt() {
        return PaymentDt;
    }
    
    /**
     * Setter for property PaymentDt.
     * @param PaymentDt New value of property PaymentDt.
     */
    public void setPaymentDt(java.lang.String PaymentDt) {
        this.PaymentDt = PaymentDt;
    }
    
    /**
     * Getter for property txtServiceTax.
     * @return Value of property txtServiceTax.
     */
    public java.lang.String getTxtServiceTax() {
        return txtServiceTax;
    }
    
    /**
     * Setter for property txtServiceTax.
     * @param txtServiceTax New value of property txtServiceTax.
     */
    public void setTxtServiceTax(java.lang.String txtServiceTax) {
        this.txtServiceTax = txtServiceTax;
    }
    
    /**
     * Getter for property payableAt.
     * @return Value of property payableAt.
     */
    public java.lang.String getPayableAt() {
        return payableAt;
    }
    
    /**
     * Setter for property payableAt.
     * @param payableAt New value of property payableAt.
     */
    public void setPayableAt(java.lang.String payableAt) {
        this.payableAt = payableAt;
    }
    
    /**
     * Getter for property lblDupRevValue.
     * @return Value of property lblDupRevValue.
     */
    public java.lang.String getLblDupRevValue() {
        return lblDupRevValue;
    }
    
    /**
     * Setter for property lblDupRevValue.
     * @param lblDupRevValue New value of property lblDupRevValue.
     */
    public void setLblDupRevValue(java.lang.String lblDupRevValue) {
        this.lblDupRevValue = lblDupRevValue;
    }
    
    /**
     * Getter for property rdoPayment.
     * @return Value of property rdoPayment.
     */
    public boolean isRdoPayment() {
        return rdoPayment;
    }
    
    /**
     * Setter for property rdoPayment.
     * @param rdoPayment New value of property rdoPayment.
     */
    public void setRdoPayment(boolean rdoPayment) {
        this.rdoPayment = rdoPayment;
        setChanged();
    }
    
    /**
     * Getter for property rdoCancel.
     * @return Value of property rdoCancel.
     */
    public boolean isRdoCancel() {
        return rdoCancel;
    }
    
    /**
     * Setter for property rdoCancel.
     * @param rdoCancel New value of property rdoCancel.
     */
    public void setRdoCancel(boolean rdoCancel) {
        this.rdoCancel = rdoCancel;
        setChanged();
    }
    
}