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
import org.apache.log4j.Logger;
import java.sql.*;
import oracle.sql.*;

/**
 *
 * @author nikhil
 */

public class ShareDividendCalculationOB extends CObservable{
    private int screenCustType;
    private Date curDate = null;
    private ProxyFactory proxy;
    private final static Logger log = Logger.getLogger(ShareDividendCalculationOB.class);
    private final static ClientParseException parseException = ClientParseException.getInstance();
    private HashMap map;
    private HashMap getShareDetailsMap;
    private TransactionOB transactionOB;
    private int actionType;
    private String lblStatus = ClientConstants.ACTION_STATUS[ClientConstants.ACTIONTYPE_CANCEL];
    private int result;
    private LinkedHashMap allowedTransactionDetailsTO = null;
    private EnhancedTableModel tblShareDividendCalculation;
    private LinkedHashMap drfTransMap;
    private ShareDividendCalculationTO objShareDividendCalculationTO = new ShareDividendCalculationTO();
    HashMap data = new HashMap();
    private ArrayList drfTransList;
   
    private String cboShareClass = "";
    private String txtDebitGl = "";
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
    ShareDividendCalculationRB objShareDividendCalculationRB = new ShareDividendCalculationRB();
    double totCurrent=0;
    double totSaleAmount=0;
    Date currDt = null;
    private LinkedHashMap transactionDetailsTO = null;
    private final String NOT_DELETED_TRANS_TOs = "NOT_DELETED_TRANS_TOs";
    private ComboBoxModel cbmDrfTransProdID;
    private ComboBoxModel cbmShareClass;
    private String drfTransID = "";
    private String chkClosedMem ="N";
    
    public ShareDividendCalculationOB(int param) {
        screenCustType = param;
        try{
            curDate = ClientUtil.getCurrentDate();
            proxy = ProxyFactory.createProxy();
            map = new HashMap();
            map.put(CommonConstants.JNDI, "ShareDividendCalculationJNDI");
            map.put(CommonConstants.HOME, "serverside.share.ShareDividendCalculationHome");
            map.put(CommonConstants.REMOTE, "serverside.share.ShareDividendCalculation");
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
        setChanged();
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
        setChkClosedMem("N");
        
        resetDividendCalcListTable();
        makeToNull();
        setChanged();
        ttNotifyObservers();
    }
    private void createDividendCalcTransTable() throws Exception{
        dividendCalcColoumn = new ArrayList();
        dividendCalcColoumn.add("Member No");
        dividendCalcColoumn.add("Name");
        dividendCalcColoumn.add("Dividend Amount");
        dividendCalcColoumn.add("Product ID");
        dividendCalcColoumn.add("Product Type");
        dividendCalcColoumn.add("SI Account No");
        tblShareDividendCalculation = new EnhancedTableModel(null, dividendCalcColoumn);
    }
    public void resetDividendCalcListTable(){
        for(int i = tblShareDividendCalculation.getRowCount(); i > 0; i--){
            tblShareDividendCalculation.removeRow(0);
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
//        System.out.println("#@$@#$objShareDividendCalculationTO:"+objShareDividendCalculationTO);
        return objShareDividendCalculationTO;
    }
    private void setDividendCalcData(){
        objShareDividendCalculationTO = new ShareDividendCalculationTO();
        objShareDividendCalculationTO = setDividendCalcDataTO();
//        System.out.println("@#$@#$@#$objShareDividendCalculationTO:"+objShareDividendCalculationTO);
        if(objShareDividendCalculationTO != null){
            data.put("ShareDividendCalculationTO",objShareDividendCalculationTO);
//            System.out.println("$#%#$%#$%#$%savingMode:"+savingMode);
            if(savingMode.equals("LOAD")){
//                System.out.println("ShareDividendCalculationTO:"+objShareDividendCalculationTO);
                data.put("LOAD_DATA","LOAD_DATA");
                data.put("CLOSED_REQ",getChkClosedMem());
                if(getGetShareDetailsMap() != null && getGetShareDetailsMap().size() >0){
                    data.put("SHARE_PROD_DETAIL",getGetShareDetailsMap());
                }
            }else{
                if(getGetShareDetailsMap() != null && getGetShareDetailsMap().size() >0){
                    data.put("SHARE_PROD_DETAIL_SAVE",getGetShareDetailsMap());
                }
            }
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
                    }
                    _authorizeMap=null;
                }
                data.put(CommonConstants.MODULE, getModule());
                data.put(CommonConstants.SCREEN, getScreen());
                data.put(CommonConstants.BRANCH_ID, getSelectedBranchID());
                data.put("COMMAND", getCommand());
                data.put(CommonConstants.USER_ID, TrueTransactMain.USER_ID);
                data.put(CommonConstants.BRANCH_ID, ProxyParameters.BRANCH_ID);
                data.put(CommonConstants.SELECTED_BRANCH_ID, getSelectedBranchID());
//                System.out.println("!@#@@@####data"+data);
                proxyResultMap = proxy.execute(data, map);
                setProxyReturnMap(proxyResultMap);
                setGetShareDetailsMap(proxyResultMap);
//                System.out.println("######## proxyResultMap :"+proxyResultMap);
                if(proxyResultMap!=null && proxyResultMap.containsKey("TOTAL_EMPLOYEE_DETAILS") && saveMode.equals("LOAD")){
                    Date salaryForDate = (Date) curDate.clone();
                    ArrayList resultList =(ArrayList) proxyResultMap.get("TOTAL_EMPLOYEE_DETAILS");
                    populateSalaryDetails(resultList);
//                    System.out.println("#$%#$%#resultList:"+resultList);
                }
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
    
    public void populateSalaryDetails(ArrayList shareDividendList){
        resetDividendCalcListTable();
        HashMap whereMap = new HashMap();
        List tableLst = new ArrayList();
        if(true){
//            System.out.println("#@$@#$@#shareDividendList:"+shareDividendList);
            if(shareDividendList!= null && shareDividendList.size()>0){
                double totalDivAmount = 0.0;
                for(int i =0;i<shareDividendList.size();i++){
                    List rowLst = new ArrayList();
                    HashMap indEmpSalMap = (HashMap)shareDividendList.get(i);
                    rowLst.add(indEmpSalMap.get("MEMBERSHIP_NO"));
                    rowLst.add(indEmpSalMap.get("NAME"));
                    rowLst.add(indEmpSalMap.get("DIVIDEND_AMOUNT"));
                    rowLst.add(indEmpSalMap.get("DIVIDEND_CREDIT_PRODUCT_ID"));
                    rowLst.add(indEmpSalMap.get("DIVIDEND_CREDIT_PRODUCT"));
                    rowLst.add(indEmpSalMap.get("DIVIDEND_CREDIT_AC"));
                    totalDivAmount += CommonUtil.convertObjToDouble(indEmpSalMap.get("DIVIDEND_AMOUNT")).doubleValue();
                    tableLst.add(rowLst);
                    
                }
                
                tblShareDividendCalculation = new EnhancedTableModel((ArrayList)tableLst,dividendCalcColoumn);
//                System.out.println("@#$@$#@$@#$totalDivAmount:"+totalDivAmount);
                setTxtTotalAmount(String.valueOf(totalDivAmount));
            }
            else{
                ClientUtil.showMessageWindow("No Records found!!!");
            }
        }
        
    }
    
    protected void makeToNull(){
        objShareDividendCalculationTO = null;
        drfTransMap = null;
        
    }
    public int showAlertWindow(String amtLimit) {
        int option = 1;
        try{
            String[] options = {objShareDividendCalculationRB.getString("cDialogOK")};
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
     * Getter for property tblShareDividendCalculation.
     * @return Value of property tblShareDividendCalculation.
     */
    public com.see.truetransact.clientutil.EnhancedTableModel getTblShareDividendCalculation() {
        return tblShareDividendCalculation;
    }
    
    /**
     * Setter for property tblShareDividendCalculation.
     * @param tblShareDividendCalculation New value of property tblShareDividendCalculation.
     */
    public void setTblShareDividendCalculation(com.see.truetransact.clientutil.EnhancedTableModel tblShareDividendCalculation) {
        this.tblShareDividendCalculation = tblShareDividendCalculation;
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

    public String getChkClosedMem() {
        return chkClosedMem;
    }

    public void setChkClosedMem(String chkClosedMem) {
        this.chkClosedMem = chkClosedMem;
    }
    
}