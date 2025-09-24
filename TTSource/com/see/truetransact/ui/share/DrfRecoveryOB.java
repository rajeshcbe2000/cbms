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

import java.util.Observable;
import com.see.truetransact.transferobject.share.DrfTransactionTO;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Iterator;
import com.see.truetransact.transferobject.deposit.JointAccntTO;
import com.see.truetransact.uicomponent.CObservable;
import com.see.truetransact.clientproxy.*;
import com.see.truetransact.clientutil.ClientConstants;
import com.see.truetransact.commonutil.CommonConstants;
import com.see.truetransact.commonutil.DateUtil;
import com.see.truetransact.commonutil.CommonUtil;
import com.see.truetransact.clientutil.ClientUtil;
import com.see.truetransact.clientutil.TableUtil;
import com.see.truetransact.clientutil.ComboBoxModel;
import com.see.truetransact.clientutil.TableModel;
import com.see.truetransact.clientutil.EnhancedTableModel;
import com.see.truetransact.clientexception.ClientParseException;
import com.see.truetransact.ui.common.introducer.*;
import com.see.truetransact.ui.TrueTransactMain;
import com.see.truetransact.ui.deposit.CommonRB;
import com.see.truetransact.uicomponent.COptionPane;
import com.see.truetransact.clientutil.JointAcctHolderManipulation;
import com.see.truetransact.ui.common.transaction.TransactionOB;
import com.see.truetransact.ui.share.*;
import java.util.Date;

import org.apache.log4j.Logger;

import java.io.OutputStream;
import java.io.InputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.sql.*;
import oracle.sql.*;
import oracle.jdbc.driver.*;
import com.see.truetransact.commonutil.Dummy;
import java.util.Set;
import com.see.truetransact.transferobject.share.DrfRecoveryTO;

/**
 *
 * @author
 */

public class DrfRecoveryOB extends CObservable{
    private int screenCustType;
    private Date curDate = null;
    private ProxyFactory proxy;
    private final static Logger log = Logger.getLogger(DrfTransactionOB.class);
    private final static ClientParseException parseException = ClientParseException.getInstance();
    private HashMap map;
    private TransactionOB transactionOB;
    private int actionType;
    private String lblStatus = ClientConstants.ACTION_STATUS[ClientConstants.ACTIONTYPE_CANCEL];
    private int result;
    private LinkedHashMap allowedTransactionDetailsTO = null;
    private EnhancedTableModel tblDrfTransaction;
    private LinkedHashMap drfTransMap;
    private DrfTransactionTO objDrfTransactionTO = new DrfTransactionTO();
    HashMap data = new HashMap();
    private ArrayList drfTransList;
    private String txtDrfTransMemberNo = "";
    private String txtDrfTransName = "";
    private String txtDrfTransAmount = "";
    private String cboDrfTransProdID = "";
    private String drfProductAmount = "";
    private String drfProdPaymentAmt = "";
    private HashMap _authorizeMap;
    private String lblDrfTransAddressCont = "";
    private String rdoDrfTransaction = "";
    private String chkDueAmtPayment = "";
    DrfTransactionRB objDrfTransactionRB = new DrfTransactionRB();
    double totCurrent=0;
    double totSaleAmount=0;
    Date currDt = null;
    private LinkedHashMap transactionDetailsTO = null;
    private final String NOT_DELETED_TRANS_TOs = "NOT_DELETED_TRANS_TOs";
    private ComboBoxModel cbmDrfTransProdID;
    private String drfTransID = "";
    private ArrayList tblList= null;
    
    public DrfRecoveryOB(int param) {
        screenCustType = param;
        try{
            curDate = ClientUtil.getCurrentDate();
            proxy = ProxyFactory.createProxy();
            map = new HashMap();
            map.put(CommonConstants.JNDI, "DrfRecoveryJNDI");
            map.put(CommonConstants.HOME, "serverside.share.DrfRecoveryHome");
            map.put(CommonConstants.REMOTE, "serverside.share.DrfRecovery");
            createDrfTransTable();
            fillDropdown();
            
        }catch(Exception e){
            parseException.logException(e,true);
        }
    }
    private void fillDropdown() {
        HashMap param = new HashMap();
        param.put(CommonConstants.MAP_NAME,"getDrfProductLookUp");
        param.put(CommonConstants.PARAMFORQUERY, null);
        HashMap where = new HashMap();
        where = null;
        HashMap keyValue = (HashMap) ClientUtil.populateLookupData(param).get(CommonConstants.DATA);
        cbmDrfTransProdID=new ComboBoxModel((ArrayList)keyValue.get(CommonConstants.KEY),(ArrayList)keyValue.get(CommonConstants.VALUE));
    }
    public void resetDrfTransDetails() {
        resetForm();
        setChanged();
        ttNotifyObservers();
    }
    public void resetForm(){
        drfTransList = null;
        drfTransMap = null;
        setTxtDrfTransAmount("");
        setTxtDrfTransMemberNo("");
        setTxtDrfTransName("");
        setRdoDrfTransaction("");
        setChkDueAmtPayment("");
        setCboDrfTransProdID("");
        setDrfProdPaymentAmt("");
        setDrfProductAmount("");
        setLblDrfTransAddressCont("");
        setDrfTransID("");
        resetDrfTransListTable();
        makeToNull();
        setChanged();
        ttNotifyObservers();
    }
    private void createDrfTransTable() throws Exception{
        final ArrayList drfTransColoumn = new ArrayList();
        drfTransColoumn.add("Select");
        drfTransColoumn.add("DRFTrans Id");
        drfTransColoumn.add("Pay Date");
        drfTransColoumn.add("Amount Due");
        tblDrfTransaction = new EnhancedTableModel(null, drfTransColoumn);
        
    }
    public void resetDrfTransListTable(){
        for(int i = tblDrfTransaction.getRowCount(); i > 0; i--){
            tblDrfTransaction.removeRow(0);
        }
    }
    
    public void populateData(HashMap whereMap) {
        log.info("In populateData()");
        HashMap mapData=null;
        try {
            mapData = proxy.executeQuery(whereMap,map);
            DrfRecoveryTO objTO =(DrfRecoveryTO) ((List) mapData.get("DRFREC_LIST")).get(0);
            setDrfRecTO(objTO);
            
            if(mapData.containsKey("TRANSACTION_LIST")){
                List list = (List) mapData.get("TRANSACTION_LIST");
                if (!list.isEmpty()) {
                    transactionOB.setDetails(list);
                    transactionOB.setTxtTransactionAmt(String.valueOf(objTO.getTxtDrfTransAmount()));
                }
            }
            ttNotifyObservers();
        } catch( Exception e ) {
            parseException.logException(e,true);
        }
    }
    
    private void setDrfRecTO(DrfRecoveryTO objTO) {
        
        setDrfTransID(objTO.getDrfTransID());
        setCboDrfTransProdID(objTO.getCboDrfTransProdID());
        setTxtDrfTransMemberNo(objTO.getTxtDrfTransMemberNo());
        setTxtDrfTransAmount(objTO.getTxtDrfTransAmount());
        setTxtDrfTransName(objTO.getTxtDrfTransName());
        setLblDrfTransAddressCont(objTO.getLblDrfTransAddressCont());
        setChanged();
        notifyObservers();
        
        
    }
    public void populateDrfTransData(String drfTransID,int panEditDelete) {
        
        log.info("in table");
        HashMap whereMap = new HashMap();
        LinkedHashMap dataMap = null;
        List rowList = new ArrayList();
        List depreciationList = new ArrayList();
        String mapNameDT = "";
        String mapNameED = "";
        String mapNameGA = "";
        whereMap.put(CommonConstants.BRANCH_ID,TrueTransactMain.BRANCH_ID);
        whereMap.put("DRF_TRANS_ID",drfTransID);
        
        HashMap drfRecTableMap = new HashMap();
        if(getActionType() != ClientConstants.ACTIONTYPE_NEW) {
            log.info("in tablenew");
            mapNameDT = "getSelectDrfRecAuthList";
            //mapNameED = "getSelectPromotionEditTO";
            List list = ClientUtil.executeQuery(mapNameDT,whereMap);
            for (int i = 0;i<list.size();i++) {
                
                ArrayList technicalTabRow = new ArrayList();
                drfRecTableMap = (HashMap)list.get(i);
                technicalTabRow = new ArrayList();
                technicalTabRow.add(new Boolean(true));
                technicalTabRow.add(CommonUtil.convertObjToStr(drfRecTableMap.get("DRF_TRANSID")));
                technicalTabRow.add(CommonUtil.convertObjToStr(drfRecTableMap.get("DRF_PAID_DATE")));
                technicalTabRow.add(CommonUtil.convertObjToStr(drfRecTableMap.get("RECOVERY_AMOUNT")));
                tblDrfTransaction.insertRow(tblDrfTransaction.getRowCount(),technicalTabRow);
            }
        }
        whereMap = null;
    }
    public void populateDrfTransTable(List drfTransList) {
        HashMap drfTransTableMap = new HashMap();
        int length = drfTransList.size();
        if(tblDrfTransaction.getRowCount()>0) {
            int x=tblDrfTransaction.getRowCount();
            for(int i=0;i<(tblDrfTransaction.getRowCount());i++) {
                tblDrfTransaction.removeRow(i);
            }
        }
        for(int i=0; i<length; i++){
            ArrayList technicalTabRow = new ArrayList();
            drfTransTableMap = (HashMap)drfTransList.get(i);
            technicalTabRow = new ArrayList();
            technicalTabRow.add(new Boolean(false));
            technicalTabRow.add(CommonUtil.convertObjToStr(drfTransTableMap.get("DRF_TRANSID")));
            technicalTabRow.add(CommonUtil.convertObjToStr(drfTransTableMap.get("DRF_PAID_DATE")));
            technicalTabRow.add(CommonUtil.convertObjToStr(drfTransTableMap.get("RECOVERY_AMOUNT")));
            tblDrfTransaction.insertRow(tblDrfTransaction.getRowCount(),technicalTabRow);
            
        }
        
    }
    private void insertData() throws Exception{
        setDrfTransData();
    }
    private void updateData() throws Exception{
        setDrfTransData();
    }
    private void deleteData() throws Exception{
        setDrfTransData();
    }
    
    private DrfTransactionTO setDrfTransDataTO(){
        objDrfTransactionTO = new DrfTransactionTO();
        objDrfTransactionTO.setTxtDrfTransAmount(CommonUtil.convertObjToDouble(getTxtDrfTransAmount()));
        objDrfTransactionTO.setCboDrfTransProdID(getCboDrfTransProdID());
        objDrfTransactionTO.setTxtDrfTransMemberNo(getTxtDrfTransMemberNo());
        objDrfTransactionTO.setTxtDrfTransName(getTxtDrfTransName());
        objDrfTransactionTO.setDrfProdPaymentAmt(getDrfProdPaymentAmt());
        objDrfTransactionTO.setDrfProductAmount(getDrfProductAmount());
        objDrfTransactionTO.setDrfTransID(getDrfTransID());
        if(getRdoDrfTransaction().equals("RECIEPT")){
            objDrfTransactionTO.setRdoDrfTransaction("RECIEPT");
        }else if(getRdoDrfTransaction().equals("PAYMENT")){
            objDrfTransactionTO.setRdoDrfTransaction("PAYMENT");
        }
        objDrfTransactionTO.setChkDueAmtPayment(getChkDueAmtPayment());
        objDrfTransactionTO.setStatus(getAction());
        objDrfTransactionTO.setStatusBy(ProxyParameters.USER_ID);
        objDrfTransactionTO.setStatusDate(curDate);
        objDrfTransactionTO.setCommand(getCommand());
        return objDrfTransactionTO;
    }
    private void setDrfTransData(){
        objDrfTransactionTO = new DrfTransactionTO();
        objDrfTransactionTO = setDrfTransDataTO();
        if(objDrfTransactionTO != null){
            data.put("DrfTransactionTO",objDrfTransactionTO);
        }
        if(allowedTransactionDetailsTO!=null && allowedTransactionDetailsTO.size()>0) {
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
        return command;
    }
    
    private String getAction(){
        String action = null;
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
    
    public HashMap doAction() {
        HashMap proxyResultMap = new HashMap();
        try {
            if(data ==null) {
                data = new HashMap();
            }
            
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
                        // throw new ActionNotFoundException();
                }
                
                if( actionType != ClientConstants.ACTIONTYPE_CANCEL || get_authorizeMap() != null) {
                    log.info("dfffffffffffffffffffffffffff");
                    if( get_authorizeMap() != null){
                        data.put(CommonConstants.AUTHORIZEMAP, get_authorizeMap());
                    }
                    if(allowedTransactionDetailsTO!=null && allowedTransactionDetailsTO.size()>0) {
                        if (transactionDetailsTO == null) {
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
                data.put("tblData",getTblList());
                proxyResultMap = proxy.execute(data, map);
                setProxyReturnMap(proxyResultMap);
                data = null;
                setResult(actionType);
                if(proxyResultMap!=null && proxyResultMap.containsKey("DRF_TRAN_ID")){
                    ClientUtil.showMessageWindow("DRF Tran Id: " + CommonUtil.convertObjToStr(proxyResultMap.get("DRF_TRAN_ID")));
                }
                actionType = ClientConstants.ACTIONTYPE_CANCEL;
            }
        }
        catch(Exception e) {
            setResult(ClientConstants.ACTIONTYPE_FAILED);
            parseException.logException(e,true);
        }
        return proxyResultMap;
    }
    protected void makeToNull(){
        objDrfTransactionTO = null;
        drfTransMap = null;
        
    }
    public int showAlertWindow(String amtLimit) {
        int option = 1;
        try{
            String[] options = {objDrfTransactionRB.getString("cDialogOK")};
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
     * Getter for property txtDrfTransMemberNo.
     * @return Value of property txtDrfTransMemberNo.
     */
    public java.lang.String getTxtDrfTransMemberNo() {
        return txtDrfTransMemberNo;
    }
    
    /**
     * Setter for property txtDrfTransMemberNo.
     * @param txtDrfTransMemberNo New value of property txtDrfTransMemberNo.
     */
    public void setTxtDrfTransMemberNo(java.lang.String txtDrfTransMemberNo) {
        this.txtDrfTransMemberNo = txtDrfTransMemberNo;
    }
    
    /**
     * Getter for property txtDrfTransName.
     * @return Value of property txtDrfTransName.
     */
    public java.lang.String getTxtDrfTransName() {
        return txtDrfTransName;
    }
    
    /**
     * Setter for property txtDrfTransName.
     * @param txtDrfTransName New value of property txtDrfTransName.
     */
    public void setTxtDrfTransName(java.lang.String txtDrfTransName) {
        this.txtDrfTransName = txtDrfTransName;
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
     * Getter for property rdoDrfTransaction.
     * @return Value of property rdoDrfTransaction.
     */
    public java.lang.String getRdoDrfTransaction() {
        return rdoDrfTransaction;
    }
    
    /**
     * Setter for property rdoDrfTransaction.
     * @param rdoDrfTransaction New value of property rdoDrfTransaction.
     */
    public void setRdoDrfTransaction(java.lang.String rdoDrfTransaction) {
        this.rdoDrfTransaction = rdoDrfTransaction;
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
     * Getter for property drfProductAmount.
     * @return Value of property drfProductAmount.
     */
    public java.lang.String getDrfProductAmount() {
        return drfProductAmount;
    }
    
    /**
     * Setter for property drfProductAmount.
     * @param drfProductAmount New value of property drfProductAmount.
     */
    public void setDrfProductAmount(java.lang.String drfProductAmount) {
        this.drfProductAmount = drfProductAmount;
    }
    
    /**
     * Getter for property drfProdPaymentAmt.
     * @return Value of property drfProdPaymentAmt.
     */
    public java.lang.String getDrfProdPaymentAmt() {
        return drfProdPaymentAmt;
    }
    
    /**
     * Setter for property drfProdPaymentAmt.
     * @param drfProdPaymentAmt New value of property drfProdPaymentAmt.
     */
    public void setDrfProdPaymentAmt(java.lang.String drfProdPaymentAmt) {
        this.drfProdPaymentAmt = drfProdPaymentAmt;
    }
    
    /**
     * Getter for property tblDrfTransaction.
     * @return Value of property tblDrfTransaction.
     */
    public com.see.truetransact.clientutil.EnhancedTableModel getTblDrfTransaction() {
        return tblDrfTransaction;
    }
    
    /**
     * Setter for property tblDrfTransaction.
     * @param tblDrfTransaction New value of property tblDrfTransaction.
     */
    public void setTblDrfTransaction(com.see.truetransact.clientutil.EnhancedTableModel tblDrfTransaction) {
        this.tblDrfTransaction = tblDrfTransaction;
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
     * Getter for property tblList.
     * @return Value of property tblList.
     */
    public ArrayList getTblList() {
        return tblList;
    }
    
    /**
     * Setter for property tblList.
     * @param tblList New value of property tblList.
     */
    public void setTblList(ArrayList tblList) {
        this.tblList = tblList;
    }
    
}