/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition.
 * 
 *
 * DeathReliefMasterOB.java
 *
 * Created on Fri Aug 05 13:20:23 GMT+05:30 2011
 */

package com.see.truetransact.ui.share;

import com.see.truetransact.transferobject.share.ShareDividendCalculationTO;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.ArrayList;
import java.util.List;
import com.see.truetransact.uicomponent.CObservable;
import com.see.truetransact.clientproxy.*;
import com.see.truetransact.clientutil.ClientConstants;
import com.see.truetransact.commonutil.CommonConstants;
import com.see.truetransact.commonutil.DateUtil;
import com.see.truetransact.commonutil.CommonUtil;
import com.see.truetransact.clientutil.ClientUtil;
import com.see.truetransact.clientutil.ComboBoxModel;
import com.see.truetransact.clientutil.EnhancedTableModel;
import com.see.truetransact.clientexception.ClientParseException;
import com.see.truetransact.ui.common.introducer.*;
import com.see.truetransact.ui.TrueTransactMain;
import com.see.truetransact.uicomponent.COptionPane;
import com.see.truetransact.ui.common.transaction.TransactionOB;
import com.see.truetransact.ui.share.*;
import java.util.Date;
import com.see.truetransact.uivalidation.CurrencyValidation;
import org.apache.log4j.Logger;
import java.sql.*;
import oracle.sql.*;
/**
 *
 * @author Nikhil
 */

public class ShareDividendPaymentOB extends CObservable{
    private int screenCustType;
    private Date curDate = null;
    private ProxyFactory proxy;
    private final static Logger log = Logger.getLogger(ShareDividendPaymentOB.class);
    private final static ClientParseException parseException = ClientParseException.getInstance();
    private HashMap map;
    private HashMap getShareDetailsMap;
    private TransactionOB transactionOB;
    private int actionType;
    private String lblStatus = ClientConstants.ACTION_STATUS[ClientConstants.ACTIONTYPE_CANCEL];
    private int result;
    private LinkedHashMap allowedTransactionDetailsTO = null;
    private EnhancedTableModel tblShareDividendPayment;
    private LinkedHashMap drfTransMap;
    private ShareDividendCalculationTO objShareDividendCalculationTO = new ShareDividendCalculationTO();
    HashMap data = new HashMap();
    private ArrayList drfTransList;
    private List finalList = null;
    private String cboShareClass = "";
    private String txtMemberNo = "";
    private String txtDebitGl = "";
    private String txtMemberName = "";
    private String txtPayableGl = "";
    private String txtTotalAmount = "";
    private String tdtFromPeriod = "";
    private String tdtToPeriod = "";
    private String txtDividendPercent = "";
    private String txtResolutionNo = "";
    private String tdtResolutionDate = "";
    private String txtRemarks = "";
    private String dividendID = "";
    private ArrayList dividendCalcColoumn = new ArrayList();
    private String txtDrfTransAmount = "";
    private String cboDrfTransProdID = "";
    private String savingMode = "";
    private ArrayList key,value;
    private HashMap lookupMap;
    private HashMap _authorizeMap;
    private String lblDrfTransAddressCont = "";
    private String rdoShareDividendCalculation = "";
    private String chkDueAmtPayment = "";
    ShareDividendPaymentRB objShareDividendPaymentRB = new ShareDividendPaymentRB();
    double totCurrent=0;
    double totSaleAmount=0;
    Date currDt = null;
    private LinkedHashMap transactionDetailsTO = null;
    private final String NOT_DELETED_TRANS_TOs = "NOT_DELETED_TRANS_TOs";
    private ComboBoxModel cbmDrfTransProdID;
    private ComboBoxModel cbmShareClass;
    private String drfTransID = "";
    private String txtStreet = "";
    private String txtArea = "";
    private String txtPinCode = "";
    private String txtPaymentBranch="";

    public String getTxtPaymentBranch() {
        return txtPaymentBranch;
    }

    public void setTxtPaymentBranch(String txtPaymentBranch) {
        this.txtPaymentBranch = txtPaymentBranch;
    }
    
    public ShareDividendPaymentOB(int param) {
        screenCustType = param;
        try{
            curDate = ClientUtil.getCurrentDate();
            proxy = ProxyFactory.createProxy();
            map = new HashMap();
            map.put(CommonConstants.JNDI, "ShareDividendPaymentJNDI");
            map.put(CommonConstants.HOME, "serverside.share.ShareDividendPaymentHome");
            map.put(CommonConstants.REMOTE, "serverside.share.ShareDividendPayment");
            createDividendCalcTransTable();
            fillDropdown();
            
        }catch(Exception e){
            parseException.logException(e,true);
        }
    }
    private void fillDropdown() throws Exception{
        lookupMap = new HashMap();
        lookupMap.put(CommonConstants.JNDI, "IncrementsPromotionsJNDI");
        lookupMap.put(CommonConstants.HOME, "serverside.common.IncrementsPromotionsHome");
        lookupMap.put(CommonConstants.REMOTE, "serverside.common.IncrementsPromotions");
        HashMap param = new HashMap();
        param.put(CommonConstants.MAP_NAME, null);
        final ArrayList lookupKey = new ArrayList();
        lookupKey.add("SHARE_TYPE");
        param.put(CommonConstants.PARAMFORQUERY, lookupKey);
        HashMap lookupValues = ClientUtil.populateLookupData(param);
        key =  new ArrayList();
        value = new ArrayList();
        fillData((HashMap)lookupValues.get("SHARE_TYPE"));
        this.cbmShareClass = new ComboBoxModel(key,value);
        param = null;
        lookupValues = null;
        key =  new ArrayList();
        value = new ArrayList();
        key = null;
        value = null;
    }
    
    private void fillData(HashMap keyValue)  throws Exception{
        key = (ArrayList)keyValue.get(CommonConstants.KEY);
        value = (ArrayList)keyValue.get(CommonConstants.VALUE);
    }
    
    public void resetDividendCalcDetails() {
        resetForm();
        //        setChanged();
        ttNotifyObservers();
    }
    public void resetForm(){
        drfTransList = null;
        drfTransMap = null;
        setTxtDebitGl("");
        setTxtDividendPercent("");
        setTxtPayableGl("");
        setTxtTotalAmount("");
        setTxtRemarks("");
        setTxtResolutionNo("");
        setCboShareClass("");
        setTdtResolutionDate("");
        setTdtFromPeriod("");
        setTdtToPeriod("");
        setDividendID("");
        setTxtArea("");
        setTxtStreet("");
        setTxtPinCode("");
        
        resetDividendCalcListTable();
        makeToNull();
        //        setChanged();
        ttNotifyObservers();
    }
    private void createDividendCalcTransTable() throws Exception{
        dividendCalcColoumn = new ArrayList();
        dividendCalcColoumn.add("Select");
        dividendCalcColoumn.add("Dividend ID");
        dividendCalcColoumn.add("From Period");
        dividendCalcColoumn.add("To Period");
        dividendCalcColoumn.add("Dividend Amt");
        tblShareDividendPayment = new EnhancedTableModel(null, dividendCalcColoumn);
        
    }
    public void resetDividendCalcListTable(){
        for(int i = tblShareDividendPayment.getRowCount(); i > 0; i--){
            tblShareDividendPayment.removeRow(0);
        }
    }
    
    public void populateData(HashMap whereMap) {
        log.info("In populateData()");
        final HashMap mapData;
        try {
            setCboDrfTransProdID(CommonUtil.convertObjToStr(getCbmDrfTransProdID().getDataForKey(CommonUtil.convertObjToStr(whereMap.get("DRF_PROD_ID")))));
            mapData = proxy.executeQuery(whereMap,map);
            if(mapData.containsKey("TRANSACTION_LIST")){
                List list = (List) mapData.get("TRANSACTION_LIST");
                if (!list.isEmpty()) {
                    transactionOB.setDetails(list);
                }
            }
            
        } catch( Exception e ) {
            System.out.println("Error In populateData()");
            parseException.logException(e,true);
        }
    }
    
    private void insertData() throws Exception{
        setDividendCalcData();
    }
    private void updateData() throws Exception{
        setDividendCalcData();
    }
    private void deleteData() throws Exception{
        setDividendCalcData();
    }
    
    private ShareDividendCalculationTO setDividendCalcDataTO(){
        objShareDividendCalculationTO = new ShareDividendCalculationTO();
        objShareDividendCalculationTO.setTxtDebitGl(getTxtDebitGl());
        objShareDividendCalculationTO.setTxtDividendPercent(getTxtDividendPercent());
        objShareDividendCalculationTO.setTxtPayableGl(getTxtPayableGl());
        objShareDividendCalculationTO.setTxtTotalAmount(getTxtTotalAmount());
        objShareDividendCalculationTO.setTxtRemarks(getTxtRemarks());
        objShareDividendCalculationTO.setTxtResolutionNo(getTxtResolutionNo());
        objShareDividendCalculationTO.setTdtFromPeriod(DateUtil.getDateMMDDYYYY(getTdtFromPeriod()));
        objShareDividendCalculationTO.setTdtResolutionDate(DateUtil.getDateMMDDYYYY(getTdtResolutionDate()));
        objShareDividendCalculationTO.setTdtToPeriod(DateUtil.getDateMMDDYYYY(getTdtToPeriod()));
        objShareDividendCalculationTO.setCboShareClass(getCboShareClass());
        objShareDividendCalculationTO.setStatus(getAction());
        objShareDividendCalculationTO.setStatusBy(ProxyParameters.USER_ID);
        objShareDividendCalculationTO.setStatusDate(curDate);
        objShareDividendCalculationTO.setCommand(getCommand());
        System.out.println("#@$@#$objShareDividendCalculationTO:"+objShareDividendCalculationTO);
        return objShareDividendCalculationTO;
    }
    
    private void setDividendCalcData(){
        if(finalList != null && finalList.size() >0){
            data.put("FINAL_LIST",finalList);
        }
        if(allowedTransactionDetailsTO!=null && allowedTransactionDetailsTO.size()>0){
            if (transactionDetailsTO == null){
                transactionDetailsTO = new LinkedHashMap();
            }
            transactionDetailsTO.put(NOT_DELETED_TRANS_TOs,allowedTransactionDetailsTO);
            data.put("TransactionTO",transactionDetailsTO);
            allowedTransactionDetailsTO = null;
        }
    }
    
    
    private String getCommand(){
        String command = null;
        System.out.println("actionType : " + actionType);
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
            case ClientConstants.ACTIONTYPE_AUTHORIZE:
                command = CommonConstants.STATUS_AUTHORIZED;
                break;
            case ClientConstants.ACTIONTYPE_REJECT:
                command = CommonConstants.STATUS_REJECTED;
                break;
            case ClientConstants.ACTIONTYPE_EXCEPTION:
                command = CommonConstants.STATUS_EXCEPTION;
            default:
        }
        // System.out.println("command : " + command);
        return command;
    }
    
    private String getAction(){
        String action = null;
        // System.out.println("actionType : " + actionType);
        switch (actionType) {
            case ClientConstants.ACTIONTYPE_NEW:
                action = CommonConstants.STATUS_CREATED;
                break;
            case ClientConstants.ACTIONTYPE_EDIT:
                action = CommonConstants.STATUS_MODIFIED;
                break;
            case ClientConstants.ACTIONTYPE_DELETE:
                action = CommonConstants.STATUS_DELETED;
                break;
            case ClientConstants.ACTIONTYPE_AUTHORIZE:
                action = CommonConstants.STATUS_AUTHORIZED;
                break;
            case ClientConstants.ACTIONTYPE_REJECT:
                action = CommonConstants.STATUS_REJECTED;
                break;
            case ClientConstants.ACTIONTYPE_EXCEPTION:
                action = CommonConstants.STATUS_EXCEPTION;
                break;
            default:
        }
        return action;
    }
    public HashMap doAction(String saveMode){
        HashMap proxyResultMap = new HashMap();
        try{
            if(data ==null){
                data = new HashMap();
            }
            savingMode = saveMode;
            if( actionType != ClientConstants.ACTIONTYPE_CANCEL ){
                switch(actionType) {
                    case ClientConstants.ACTIONTYPE_NEW:
                        insertData();
                        break;
                    case ClientConstants.ACTIONTYPE_EDIT:
                        updateData();
                        break;
                    case ClientConstants.ACTIONTYPE_DELETE:
                        deleteData();
                        break;
                    default:
                }
                
                 if( actionType != ClientConstants.ACTIONTYPE_CANCEL || get_authorizeMap() != null){
                    if( get_authorizeMap() != null){
                        data.put(CommonConstants.AUTHORIZEMAP, get_authorizeMap());
                    }
                    if(allowedTransactionDetailsTO!=null && allowedTransactionDetailsTO.size()>0){
                        if (transactionDetailsTO == null){
                            transactionDetailsTO = new LinkedHashMap();
                        }
                        transactionDetailsTO.put(NOT_DELETED_TRANS_TOs,allowedTransactionDetailsTO);
                        data.put("TransactionTO",transactionDetailsTO);
                        allowedTransactionDetailsTO = null;
                        //                        data.put("SALE_LIST",drfTransList);
                    }
                    _authorizeMap=null;
                }
                data.put("SHARE_PAYMENT_BRANCH", getTxtPaymentBranch());
                data.put(CommonConstants.MODULE, getModule());
                data.put(CommonConstants.SCREEN, getScreen());
                data.put(CommonConstants.BRANCH_ID, getSelectedBranchID());
                data.put("COMMAND", getCommand());
                data.put(CommonConstants.USER_ID, TrueTransactMain.USER_ID);
                data.put(CommonConstants.BRANCH_ID, ProxyParameters.BRANCH_ID);
                data.put(CommonConstants.SELECTED_BRANCH_ID, TrueTransactMain.selBranch);
                System.out.println("!@#@@@####data"+data);
                proxyResultMap = proxy.execute(data, map);
                setProxyReturnMap(proxyResultMap);
                setGetShareDetailsMap(proxyResultMap);
                System.out.println("######## proxyResultMap :"+proxyResultMap);
               
                data = null;
                setResult(actionType);
                actionType = ClientConstants.ACTIONTYPE_CANCEL;
            }
        }catch(Exception e){
            setResult(ClientConstants.ACTIONTYPE_FAILED);
            parseException.logException(e,true);
        }
        return proxyResultMap;
    }
    
    public HashMap populateDataForAuth(HashMap authMap){
        try{
        System.out.println("@#$@#$@#$authMap:"+authMap);
        HashMap mapData = new HashMap();
        mapData = proxy.executeQuery(authMap,map);
        System.out.println("#$%#$%#$%mapData:"+mapData);
        if(mapData.containsKey("TRANSACTION_LIST")){
            List list = (List) mapData.get("TRANSACTION_LIST");
            if (!list.isEmpty()) {
                transactionOB.setDetails(list);
            }
        }
        if(mapData.containsKey("SHARE_DETAILS_LIST")){
                ArrayList list = (ArrayList) mapData.get("SHARE_DETAILS_LIST");
                if (!list.isEmpty()) {
                    populateSalaryDetails(list);
                }
            }
        }catch( Exception e ) {
            System.out.println("Error In populateData()");
            parseException.logException(e,true);
        }
        return authMap;
    }
    public void populateSalaryDetails(ArrayList shareDividendList){
        resetDividendCalcListTable();
        HashMap whereMap = new HashMap();
        List tableLst = new ArrayList();
//            if(getActionType() == ClientConstants.ACTIONTYPE_AUTHORIZE){
//                System.out.println("#@$@$#@#$inside auth:"+shareDividendList);
//                System.out.println("#@$@#$@#shareDividendList:"+shareDividendList);
//                if(shareDividendList!= null && shareDividendList.size()>0){
//                    finalList = shareDividendList;
//                    double totalDivAmount = 0.0;
//                    String memberName = "";
//                    for(int i =0;i<shareDividendList.size();i++){
//                        List rowLst = new ArrayList();
//                        HashMap indEmpSalMap = (HashMap)shareDividendList.get(i);
//                        rowLst.add(new Boolean(true));
//                        rowLst.add(indEmpSalMap.get("DIVIDEND_CALC_ID"));
//                        String fromDt = CommonUtil.convertObjToStr(indEmpSalMap.get("FROM_PERIOD"));
//                        String toDt = CommonUtil.convertObjToStr(indEmpSalMap.get("TO_PERIOD"));
//                        System.out.println("@#$@#$fromDt:"+fromDt);
//                        rowLst.add(fromDt);
//                        rowLst.add(toDt);
//                        rowLst.add(indEmpSalMap.get("DIV_AMOUNT"));
//                        totalDivAmount += CommonUtil.convertObjToDouble(indEmpSalMap.get("DIV_AMOUNT")).doubleValue();
//                        tableLst.add(rowLst);
//                        memberName = CommonUtil.convertObjToStr(indEmpSalMap.get("NAME"));
//                    }
//                    
//                    tblShareDividendPayment = new EnhancedTableModel((ArrayList)tableLst,dividendCalcColoumn);
//                    System.out.println("@#$@$#@$@#$totalDivAmount:"+totalDivAmount);
//                    setTxtTotalAmount(String.valueOf(totalDivAmount));
//                    setTxtMemberName(memberName);
//                }
//            }else{
                System.out.println("#@$@#$@#shareDividendList:"+shareDividendList);
                if(shareDividendList!= null && shareDividendList.size()>0){
                    finalList = shareDividendList;
                    double totalDivAmount = 0.0;
                    String memberName = "";
                    String street = "";
                    String pinCode = "";
                    String area = "";
                    String paymentBranch=""; 
                    for(int i =0;i<shareDividendList.size();i++){
                        List rowLst = new ArrayList();
                        HashMap indEmpSalMap = (HashMap)shareDividendList.get(i);
                        rowLst.add(new Boolean(true));
                        rowLst.add(indEmpSalMap.get("DIVIDEND_CALC_ID"));
                        String fromDt = CommonUtil.convertObjToStr(indEmpSalMap.get("FROM_PERIOD"));
                        String toDt = CommonUtil.convertObjToStr(indEmpSalMap.get("TO_PERIOD"));
                        System.out.println("@#$@#$fromDt:"+fromDt);
                        rowLst.add(fromDt);
                        rowLst.add(toDt);
                        rowLst.add(CurrencyValidation.formatCrore(CommonUtil.convertObjToStr(indEmpSalMap.get("DIV_AMOUNT"))));
//                        added by nikhil
                        String divAmtStr =  CommonUtil.convertObjToStr(indEmpSalMap.get("DIV_AMOUNT")).replaceAll(",", "");
                        totalDivAmount += CommonUtil.convertObjToDouble(divAmtStr).doubleValue();
                        tableLst.add(rowLst);
                        memberName = CommonUtil.convertObjToStr(indEmpSalMap.get("NAME"));
                        area = CommonUtil.convertObjToStr(indEmpSalMap.get("AREA"));
                        street = CommonUtil.convertObjToStr(indEmpSalMap.get("STREET"));
                        pinCode = CommonUtil.convertObjToStr(indEmpSalMap.get("PIN_CODE"));
                        paymentBranch=CommonUtil.convertObjToStr(indEmpSalMap.get("BRANCH_CODE"));
                    }
                    tblShareDividendPayment = new EnhancedTableModel((ArrayList)tableLst,dividendCalcColoumn);
                    System.out.println("@#$@$#@$@#$totalDivAmount:"+totalDivAmount);
                    setTxtTotalAmount(String.valueOf(totalDivAmount));
                    setTxtMemberName(memberName);
                    setTxtArea(area);
                    setTxtStreet(street);
                    setTxtPinCode(pinCode);
                    setTxtPaymentBranch(paymentBranch);
                }
                else{
                    ClientUtil.showMessageWindow("No Records found!!!");
                }
//            }
    }
    
    protected void makeToNull(){
        objShareDividendCalculationTO = null;
        drfTransMap = null;
        
    }
    public int showAlertWindow(String amtLimit) {
        int option = 1;
        try{
            String[] options = {objShareDividendPaymentRB.getString("cDialogOK")};
            option = COptionPane.showOptionDialog(null,amtLimit, CommonConstants.WARNINGTITLE,
            COptionPane.OK_OPTION, COptionPane.WARNING_MESSAGE,
            null, options, options[0]);
        }catch (Exception e){
            parseException.logException(e,true);
        }
        return option;
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
     * Getter for property actionType.
     * @return Value of property actionType.
     */
    public int getActionType() {
        return actionType;
    }
    
    /**
     * Setter for property actionType.
     * @param actionType New value of property actionType.
     */
    public void setActionType(int actionType) {
        this.actionType = actionType;
    }
    
    public void ttNotifyObservers(){
        notifyObservers();
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
    
    /**
     * Getter for property lblStatus.
     * @return Value of property lblStatus.
     */
    public java.lang.String getLblStatus() {
        return lblStatus;
    }
    
    /**
     * Setter for property lblStatus.
     * @param lblStatus New value of property lblStatus.
     */
    public void setLblStatus(java.lang.String lblStatus) {
        this.lblStatus = lblStatus;
    }
    
    /**
     * Getter for property result.
     * @return Value of property result.
     */
    public int getResult() {
        return result;
    }
    
    /**
     * Setter for property result.
     * @param result New value of property result.
     */
    public void setResult(int result) {
        this.result = result;
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
     * Getter for property txtDrfTransAmount.
     * @return Value of property txtDrfTransAmount.
     */
    public java.lang.String getTxtDrfTransAmount() {
        return txtDrfTransAmount;
    }
    
    /**
     * Setter for property txtDrfTransAmount.
     * @param txtDrfTransAmount New value of property txtDrfTransAmount.
     */
    public void setTxtDrfTransAmount(java.lang.String txtDrfTransAmount) {
        this.txtDrfTransAmount = txtDrfTransAmount;
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
     * Getter for property lblDrfTransAddressCont.
     * @return Value of property lblDrfTransAddressCont.
     */
    public java.lang.String getLblDrfTransAddressCont() {
        return lblDrfTransAddressCont;
    }
    
    /**
     * Setter for property lblDrfTransAddressCont.
     * @param lblDrfTransAddressCont New value of property lblDrfTransAddressCont.
     */
    public void setLblDrfTransAddressCont(java.lang.String lblDrfTransAddressCont) {
        this.lblDrfTransAddressCont = lblDrfTransAddressCont;
    }
    
    /**
     * Getter for property rdoShareDividendCalculation.
     * @return Value of property rdoShareDividendCalculation.
     */
    public java.lang.String getRdoShareDividendCalculation() {
        return rdoShareDividendCalculation;
    }
    
    /**
     * Setter for property rdoShareDividendCalculation.
     * @param rdoShareDividendCalculation New value of property rdoShareDividendCalculation.
     */
    public void setRdoShareDividendCalculation(java.lang.String rdoShareDividendCalculation) {
        this.rdoShareDividendCalculation = rdoShareDividendCalculation;
    }
    
    /**
     * Getter for property chkDueAmtPayment.
     * @return Value of property chkDueAmtPayment.
     */
    public java.lang.String getChkDueAmtPayment() {
        return chkDueAmtPayment;
    }
    
    /**
     * Setter for property chkDueAmtPayment.
     * @param chkDueAmtPayment New value of property chkDueAmtPayment.
     */
    public void setChkDueAmtPayment(java.lang.String chkDueAmtPayment) {
        this.chkDueAmtPayment = chkDueAmtPayment;
    }
    
    /**
     * Getter for property cbmDrfTransProdID.
     * @return Value of property cbmDrfTransProdID.
     */
    public com.see.truetransact.clientutil.ComboBoxModel getCbmDrfTransProdID() {
        return cbmDrfTransProdID;
    }
    
    /**
     * Setter for property cbmDrfTransProdID.
     * @param cbmDrfTransProdID New value of property cbmDrfTransProdID.
     */
    public void setCbmDrfTransProdID(com.see.truetransact.clientutil.ComboBoxModel cbmDrfTransProdID) {
        this.cbmDrfTransProdID = cbmDrfTransProdID;
    }
    
    /**
     * Getter for property cboDrfTransProdID.
     * @return Value of property cboDrfTransProdID.
     */
    public java.lang.String getCboDrfTransProdID() {
        return cboDrfTransProdID;
    }
    
    /**
     * Setter for property cboDrfTransProdID.
     * @param cboDrfTransProdID New value of property cboDrfTransProdID.
     */
    public void setCboDrfTransProdID(java.lang.String cboDrfTransProdID) {
        this.cboDrfTransProdID = cboDrfTransProdID;
    }
    
    
    
    /**
     * Getter for property drfTransID.
     * @return Value of property drfTransID.
     */
    public java.lang.String getDrfTransID() {
        return drfTransID;
    }
    
    /**
     * Setter for property drfTransID.
     * @param drfTransID New value of property drfTransID.
     */
    public void setDrfTransID(java.lang.String drfTransID) {
        this.drfTransID = drfTransID;
    }
    
    /**
     * Getter for property cboShareClass.
     * @return Value of property cboShareClass.
     */
    public java.lang.String getCboShareClass() {
        return cboShareClass;
    }
    
    /**
     * Setter for property cboShareClass.
     * @param cboShareClass New value of property cboShareClass.
     */
    public void setCboShareClass(java.lang.String cboShareClass) {
        this.cboShareClass = cboShareClass;
    }
    
    /**
     * Getter for property txtDebitGl.
     * @return Value of property txtDebitGl.
     */
    public java.lang.String getTxtDebitGl() {
        return txtDebitGl;
    }
    
    /**
     * Setter for property txtDebitGl.
     * @param txtDebitGl New value of property txtDebitGl.
     */
    public void setTxtDebitGl(java.lang.String txtDebitGl) {
        this.txtDebitGl = txtDebitGl;
    }
    
    /**
     * Getter for property txtPayableGl.
     * @return Value of property txtPayableGl.
     */
    public java.lang.String getTxtPayableGl() {
        return txtPayableGl;
    }
    
    /**
     * Setter for property txtPayableGl.
     * @param txtPayableGl New value of property txtPayableGl.
     */
    public void setTxtPayableGl(java.lang.String txtPayableGl) {
        this.txtPayableGl = txtPayableGl;
    }
    
    /**
     * Getter for property tdtFromPeriod.
     * @return Value of property tdtFromPeriod.
     */
    public java.lang.String getTdtFromPeriod() {
        return tdtFromPeriod;
    }
    
    /**
     * Setter for property tdtFromPeriod.
     * @param tdtFromPeriod New value of property tdtFromPeriod.
     */
    public void setTdtFromPeriod(java.lang.String tdtFromPeriod) {
        this.tdtFromPeriod = tdtFromPeriod;
    }
    
    /**
     * Getter for property tdtToPeriod.
     * @return Value of property tdtToPeriod.
     */
    public java.lang.String getTdtToPeriod() {
        return tdtToPeriod;
    }
    
    /**
     * Setter for property tdtToPeriod.
     * @param tdtToPeriod New value of property tdtToPeriod.
     */
    public void setTdtToPeriod(java.lang.String tdtToPeriod) {
        this.tdtToPeriod = tdtToPeriod;
    }
    
    /**
     * Getter for property txtDividendPercent.
     * @return Value of property txtDividendPercent.
     */
    public java.lang.String getTxtDividendPercent() {
        return txtDividendPercent;
    }
    
    /**
     * Setter for property txtDividendPercent.
     * @param txtDividendPercent New value of property txtDividendPercent.
     */
    public void setTxtDividendPercent(java.lang.String txtDividendPercent) {
        this.txtDividendPercent = txtDividendPercent;
    }
    
    /**
     * Getter for property txtResolutionNo.
     * @return Value of property txtResolutionNo.
     */
    public java.lang.String getTxtResolutionNo() {
        return txtResolutionNo;
    }
    
    /**
     * Setter for property txtResolutionNo.
     * @param txtResolutionNo New value of property txtResolutionNo.
     */
    public void setTxtResolutionNo(java.lang.String txtResolutionNo) {
        this.txtResolutionNo = txtResolutionNo;
    }
    
    /**
     * Getter for property tdtResolutionDate.
     * @return Value of property tdtResolutionDate.
     */
    public java.lang.String getTdtResolutionDate() {
        return tdtResolutionDate;
    }
    
    /**
     * Setter for property tdtResolutionDate.
     * @param tdtResolutionDate New value of property tdtResolutionDate.
     */
    public void setTdtResolutionDate(java.lang.String tdtResolutionDate) {
        this.tdtResolutionDate = tdtResolutionDate;
    }
    
    /**
     * Getter for property txtRemarks.
     * @return Value of property txtRemarks.
     */
    public java.lang.String getTxtRemarks() {
        return txtRemarks;
    }
    
    /**
     * Setter for property txtRemarks.
     * @param txtRemarks New value of property txtRemarks.
     */
    public void setTxtRemarks(java.lang.String txtRemarks) {
        this.txtRemarks = txtRemarks;
    }
    
    /**
     * Getter for property dividendID.
     * @return Value of property dividendID.
     */
    public java.lang.String getDividendID() {
        return dividendID;
    }
    
    /**
     * Setter for property dividendID.
     * @param dividendID New value of property dividendID.
     */
    public void setDividendID(java.lang.String dividendID) {
        this.dividendID = dividendID;
    }
    
    /**
     * Getter for property cbmShareClass.
     * @return Value of property cbmShareClass.
     */
    public com.see.truetransact.clientutil.ComboBoxModel getCbmShareClass() {
        return cbmShareClass;
    }
    
    /**
     * Setter for property cbmShareClass.
     * @param cbmShareClass New value of property cbmShareClass.
     */
    public void setCbmShareClass(com.see.truetransact.clientutil.ComboBoxModel cbmShareClass) {
        this.cbmShareClass = cbmShareClass;
    }
    
    /**
     * Getter for property getShareDetailsMap.
     * @return Value of property getShareDetailsMap.
     */
    public java.util.HashMap getGetShareDetailsMap() {
        return getShareDetailsMap;
    }
    
    /**
     * Setter for property getShareDetailsMap.
     * @param getShareDetailsMap New value of property getShareDetailsMap.
     */
    public void setGetShareDetailsMap(java.util.HashMap getShareDetailsMap) {
        this.getShareDetailsMap = getShareDetailsMap;
    }
    
    /**
     * Getter for property txtTotalAmount.
     * @return Value of property txtTotalAmount.
     */
    public java.lang.String getTxtTotalAmount() {
        return txtTotalAmount;
    }
    
    /**
     * Setter for property txtTotalAmount.
     * @param txtTotalAmount New value of property txtTotalAmount.
     */
    public void setTxtTotalAmount(java.lang.String txtTotalAmount) {
        this.txtTotalAmount = txtTotalAmount;
    }
    
    /**
     * Getter for property tblShareDividendPayment.
     * @return Value of property tblShareDividendPayment.
     */
    public com.see.truetransact.clientutil.EnhancedTableModel getTblShareDividendPayment() {
        return tblShareDividendPayment;
    }
    
    /**
     * Setter for property tblShareDividendPayment.
     * @param tblShareDividendPayment New value of property tblShareDividendPayment.
     */
    public void setTblShareDividendPayment(com.see.truetransact.clientutil.EnhancedTableModel tblShareDividendPayment) {
        this.tblShareDividendPayment = tblShareDividendPayment;
    }
    
    /**
     * Getter for property txtMemberName.
     * @return Value of property txtMemberName.
     */
    public java.lang.String getTxtMemberName() {
        return txtMemberName;
    }
    
    /**
     * Setter for property txtMemberName.
     * @param txtMemberName New value of property txtMemberName.
     */
    public void setTxtMemberName(java.lang.String txtMemberName) {
        this.txtMemberName = txtMemberName;
    }
    
    /**
     * Getter for property finalList.
     * @return Value of property finalList.
     */
    public java.util.List getFinalList() {
        return finalList;
    }
    
    /**
     * Setter for property finalList.
     * @param finalList New value of property finalList.
     */
    public void setFinalList(java.util.List finalList) {
        this.finalList = finalList;
    }
    
    /**
     * Getter for property txtMemberNo.
     * @return Value of property txtMemberNo.
     */
    public java.lang.String getTxtMemberNo() {
        return txtMemberNo;
    }
    
    /**
     * Setter for property txtMemberNo.
     * @param txtMemberNo New value of property txtMemberNo.
     */
    public void setTxtMemberNo(java.lang.String txtMemberNo) {
        this.txtMemberNo = txtMemberNo;
    }
    
    /**
     * Getter for property txtStreet.
     * @return Value of property txtStreet.
     */
    public java.lang.String getTxtStreet() {
        return txtStreet;
    }
    
    /**
     * Setter for property txtStreet.
     * @param txtStreet New value of property txtStreet.
     */
    public void setTxtStreet(java.lang.String txtStreet) {
        this.txtStreet = txtStreet;
    }
    
    /**
     * Getter for property txtArea.
     * @return Value of property txtArea.
     */
    public java.lang.String getTxtArea() {
        return txtArea;
    }
    
    /**
     * Setter for property txtArea.
     * @param txtArea New value of property txtArea.
     */
    public void setTxtArea(java.lang.String txtArea) {
        this.txtArea = txtArea;
    }
    
    /**
     * Getter for property txtPinCode.
     * @return Value of property txtPinCode.
     */
    public java.lang.String getTxtPinCode() {
        return txtPinCode;
    }
    
    /**
     * Setter for property txtPinCode.
     * @param txtPinCode New value of property txtPinCode.
     */
    public void setTxtPinCode(java.lang.String txtPinCode) {
        this.txtPinCode = txtPinCode;
    }
    public boolean setAccountName(String shareNum){
        boolean result = false;
        final HashMap accountNameMap = new HashMap();
        accountNameMap.put("SHARE_ACCT_NO",shareNum);
        accountNameMap.put("CLOSECHECK",shareNum);
        final java.util.List resultList = ClientUtil.executeQuery("getSelectDividendUnclaimedTransferList",accountNameMap);
        if(resultList != null && resultList.size()>0){
            final HashMap resultMap = (HashMap)resultList.get(0);
            result = true;
            return result;
        } else {
            ClientUtil.displayAlert("Share Number does not exists");
            result = false;
            return result;
        }
        
        
        
    }
    
}