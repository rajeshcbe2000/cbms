/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition.
 * 
 *
 * ShareTransferOB.java
 *
 * Created on Thu Feb 03 15:35:09 IST 2005
 */

package com.see.truetransact.ui.share.sharetransfer;

import com.see.truetransact.clientutil.ClientConstants;
import com.see.truetransact.clientutil.ClientUtil;
import com.see.truetransact.commonutil.CommonConstants;
import com.see.truetransact.commonutil.DateUtil;
import com.see.truetransact.commonutil.CommonUtil;
import com.see.truetransact.commonutil.TTException;
import com.see.truetransact.clientexception.ClientParseException;
import com.see.truetransact.clientproxy.ProxyFactory;
import com.see.truetransact.transferobject.share.sharetransfer.ShareTransferTO;
import com.see.truetransact.uicomponent.CObservable;
import com.see.truetransact.clientutil.EnhancedTableModel;
import com.see.truetransact.clientproxy.ProxyParameters;
import com.see.truetransact.ui.common.transaction.TransactionOB ;
import com.see.truetransact.transferobject.batchprocess.share.DividendBatchTO;
import com.see.truetransact.ui.TrueTransactMain;

import java.util.Observable;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.ArrayList;
import java.util.List;
import java.util.Date;
import com.see.truetransact.clientutil.ComboBoxModel;

import org.apache.log4j.Logger;
/**
 *
 * @author
 */

public class ShareTransferOB extends CObservable{
    private String cboShareType = "";
    private ComboBoxModel cbmShareType;
    private HashMap operationMap;
    private HashMap authorizeMap;
    private TransactionOB transactionOB;
    private EnhancedTableModel tblShareTransTab;
    private Date currDt;
    private HashMap lookUpHash;
    private HashMap keyValue;
    private ArrayList key;
    private ArrayList value;
    private final String SHARE_TYPE = "getShareType";
    private String divAcno="";
    private Date divUptoDate=null;
    private Double divAmt=null;
    private LinkedHashMap allowedTransactionDetailsTO ;
    private LinkedHashMap transactionDetailsTO ;
    private final String NOT_DELETED_TRANS_TOs = "NOT_DELETED_TRANS_TOs";
    private String batchID="";
    
    //__ ArrayLists for the ShareTrans Table...
    ArrayList shareTransTabTitle = new ArrayList();
    private ArrayList shareTransTabRow;
    
    // To get the Value of Column Title and Dialogue Box...
    //    final ShareTransferRB objShareTransferRB = new ShareTransferRB();
    java.util.ResourceBundle objShareTransferRB = java.util.ResourceBundle.getBundle("com.see.truetransact.ui.share.sharetransfer.ShareTransferRB", ProxyParameters.LANGUAGE);
    
    private int actionType;
    private int result;
    private String lblStatus = ClientConstants.ACTION_STATUS[ClientConstants.ACTIONTYPE_CANCEL];
    private final static ClientParseException parseException = ClientParseException.getInstance();
    private final static Logger log = Logger.getLogger(ShareTransferUI.class);
    
    private ProxyFactory proxy = null;
    
    
    private static ShareTransferOB shareTransferOB;
    static {
        try {
            log.info("In ShareTransferOB Declaration");
            shareTransferOB = new ShareTransferOB();
        } catch(Exception e) {
            parseException.logException(e,true);
        }
    }
    
    public static ShareTransferOB getInstance() {
        return shareTransferOB;
    }
    
    /** Creates a new instance of InwardClearingOB */
    public ShareTransferOB() throws Exception {
        initianSetup();
        fillDropdown();
        currDt = ClientUtil.getCurrentDate();
    }
    
    private void initianSetup() throws Exception{
        log.info("In initianSetup()");
        setOperationMap();
        try {
            proxy = ProxyFactory.createProxy();
        } catch (Exception e) {
            parseException.logException(e,true);
            //e.printStackTrace();
        }
        
        setShareTransTabTitle();   //__ To set the Title of Table in Agent Tab...
        tblShareTransTab = new EnhancedTableModel(null, shareTransTabTitle);
    }
    
    // Set the value of JNDI and the Session Bean...
    private void setOperationMap() throws Exception{
        log.info("In setOperationMap()");
        
        operationMap = new HashMap();
        operationMap.put(CommonConstants.JNDI, "ShareTransferJNDI");
        operationMap.put(CommonConstants.HOME, "share.sharetransfer.ShareTransferHome");
        operationMap.put(CommonConstants.REMOTE, "share.sharetransfer.ShareTransfer");
    }
    
    // To set the Column title in Table...
    private void setShareTransTabTitle() throws Exception{
        log.info("In setAgentTabTitle...");
        
        shareTransTabTitle.add(objShareTransferRB.getString("tblColumn1"));
        shareTransTabTitle.add(objShareTransferRB.getString("tblColumn2"));
        shareTransTabTitle.add(objShareTransferRB.getString("tblColumn3"));
        shareTransTabTitle.add(objShareTransferRB.getString("tblColumn4"));
    }
    
    public void fillDropdown() throws Exception{
        lookUpHash = new HashMap();
        lookUpHash.put(CommonConstants.MAP_NAME,null);
        final ArrayList lookup_keys = new ArrayList();
        lookup_keys.add(SHARE_TYPE);
        lookUpHash.put(CommonConstants.MAP_NAME,SHARE_TYPE);
        lookUpHash.put(CommonConstants.PARAMFORQUERY,null);
        keyValue = ClientUtil.populateLookupData(lookUpHash);
        getKeyValue((HashMap)keyValue.get(CommonConstants.DATA));
        cbmShareType = new ComboBoxModel(key,value);
        
        makeNull();
    }
    //Do display the Data from the Database, in UI
    public void populateData(HashMap whereMap) {
        log.info("In populateData()");
        final HashMap mapData;
        try {
            mapData = proxy.executeQuery(whereMap, operationMap);
            if(!whereMap.containsKey("DIVIDEND")){
                populateOB(mapData);
            }else{
                setDivAcno(CommonUtil.convertObjToStr(whereMap.get("SHARE_ACCT_NO")));
                setDivAmt(CommonUtil.convertObjToDouble(whereMap.get("DIVIDEND_AMT")));
                setDivUptoDate(DateUtil.getDateMMDDYYYY(CommonUtil.convertObjToStr(whereMap.get("DIVIDEND_DT"))));
                setCboShareType(CommonUtil.convertObjToStr(getCbmShareType().getDataForKey(CommonUtil.convertObjToStr(whereMap.get("SHARE_TYPE")))));
                String where = (String) whereMap.get("BATCH_ID");
                HashMap getRemitTransMap = new HashMap();
                getRemitTransMap.put("TRANS_ID",whereMap.get("BATCH_ID"));
                getRemitTransMap.put("TRANS_DT",currDt);
                getRemitTransMap.put("BRANCH_CODE",ProxyParameters.BRANCH_ID);
                System.out.println("@#%$#@%#$%getRemitTransMap:"+getRemitTransMap);
                List list = (List) ClientUtil.executeQuery("getSelectRemittanceIssueTransactionTODate", whereMap);
                
                if (!list.isEmpty()) {
                    transactionOB.setDetails(list);
                }
            }
        } catch( Exception e ) {
            System.out.println("Error In populateData()");
            parseException.logException(e,true);
        }
    }
    
    private void populateOB(HashMap mapData) throws Exception{
        ShareTransferTO objShareTransferTO = null;
        //Taking the Value of Prod_Id from each Table...
        objShareTransferTO = (ShareTransferTO) ((List) mapData.get("ShareTransferTO")).get(0);
        setShareTransferTO(objShareTransferTO);
        
        //        ttNotifyObservers();
    }
    
    // To Enter the values in the UI fields, from the database...
    private void setShareTransferTO(ShareTransferTO objShareTransferTO) throws Exception{
        log.info("In setShareTransferTO()");
        
        setLblShareTransID(objShareTransferTO.getShareTransId());
        setTxtAcctFrom(objShareTransferTO.getTransferFrom());
        setTxtAcctTo(objShareTransferTO.getTransferTo());
        setTxtShareFrom(objShareTransferTO.getShareNoFrom());
        setTxtShareTo(objShareTransferTO.getShareNoTo());
        setTxtRemarks(objShareTransferTO.getRemarks());
        setStatusBy(objShareTransferTO.getStatusBy());
    }
    
    private ShareTransferTO setShareTransfer() {
        log.info("In setAgent()");
        
        final ShareTransferTO objShareTransferTO = new ShareTransferTO();
        try{
            objShareTransferTO.setShareTransId(getLblShareTransID());
            objShareTransferTO.setTransferFrom(getTxtAcctFrom());
            objShareTransferTO.setTransferTo(getTxtAcctTo());
            objShareTransferTO.setShareNoFrom(getTxtShareFrom());
            objShareTransferTO.setShareNoTo(getTxtShareTo());
            objShareTransferTO.setRemarks(getTxtRemarks());
        }catch(Exception e){
            parseException.logException(e,true);
            //e.printStackTrace();
        }
        return objShareTransferTO;
    }
    
    public void doAction() {
        log.info("In doAction()");
        try {
            //If actionType such as NEW, EDIT, DELETE, then proceed
            if( actionType != ClientConstants.ACTIONTYPE_CANCEL ){
                //If actionType has got propervalue then doActionPerform, else throw error
                if( getCommand() != null || getAuthorizeMap() != null){
                    doActionPerform();
                }
            }
            else
                log.info("Action Type Not Defined In setChequeBookTO()");
        } catch (Exception e) {
            log.info("Error In doAction()");
            setResult(ClientConstants.ACTIONTYPE_FAILED);
            parseException.logException(e,true);
        }
    }
    
    /** To perform the necessary action */
    private void doActionPerform() throws Exception{
        log.info("In doActionPerform()");
        final HashMap data = new HashMap();
        
        if(getAuthorizeMap() == null){
            if(getDivAmt()==null && getDivAmt().doubleValue()==0.0){
                final ShareTransferTO objShareTransferTO = setShareTransfer();
                objShareTransferTO.setCommand(getCommand());
                data.put("ShareTransferTO",objShareTransferTO);
            } else   if(getDivAmt()!=null && getDivAmt().doubleValue()>0.0){
                if (transactionDetailsTO == null)
                    transactionDetailsTO = new LinkedHashMap();
                data.put("DividendTO",setDividenBatchTo());
                transactionDetailsTO.put(NOT_DELETED_TRANS_TOs,allowedTransactionDetailsTO);
                data.put("TransactionTO",transactionDetailsTO);
                data.put("DIVIDEND","DIVIDEND");
            }
        }
        data.put(CommonConstants.AUTHORIZEMAP, getAuthorizeMap());
        
        
        HashMap proxyResultMap = proxy.execute(data,operationMap);
//        System.out.println("proxyResultMap---------->"+proxyResultMap);
           if (proxyResultMap != null && proxyResultMap.containsKey(CommonConstants.TRANS_ID)) {
            ClientUtil.showMessageWindow("Transaction No. :" + CommonUtil.convertObjToStr(proxyResultMap.get(CommonConstants.TRANS_ID)));
            
        }
        setResult(actionType);
        resetForm();
    }
    private DividendBatchTO setDividenBatchTo(){
        
        DividendBatchTO divPayTo=new DividendBatchTO();
        if(getActionType()==ClientConstants.ACTIONTYPE_NEW){
            divPayTo.setCommand(CommonConstants.TOSTATUS_INSERT);
            divPayTo.setStatus(CommonConstants.STATUS_CREATED);
        }else  if(getActionType()==ClientConstants.ACTIONTYPE_EDIT){
            divPayTo.setCommand(CommonConstants.TOSTATUS_UPDATE);
            divPayTo.setStatus(CommonConstants.STATUS_MODIFIED);
        }else  if(getActionType()==ClientConstants.ACTIONTYPE_DELETE){
            divPayTo.setCommand(CommonConstants.TOSTATUS_DELETE);
            divPayTo.setStatus(CommonConstants.STATUS_DELETED);
        }
        
        divPayTo.setShareAcctNo(getDivAcno());
        divPayTo.setDividendAmt(getDivAmt());
        divPayTo.setShareType(CommonUtil.convertObjToStr(getCbmShareType().getKeyForSelected()));
        divPayTo.setDividendUpTo(getDivUptoDate());
        divPayTo.setDividendDt(getDivUptoDate());
        divPayTo.setShareAcctDetailNo("DEBIT");
        divPayTo.setStatusBy(TrueTransactMain.USER_ID);
        divPayTo.setStatusDt(currDt);
        return divPayTo;
    }
    private void  getDividenBatchTo(DividendBatchTO divPayTo){
        setDivAcno(divPayTo.getShareAcctNo());
        setDivAmt(divPayTo.getDividendAmt());
        setDivUptoDate(divPayTo.getDividendDt());
    }
    // to decide which action Should be performed...
    private String getCommand() throws Exception{
        log.info("In getCommand()");
        
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
    
    // Returns the Current Value of Action type...
    public int getActionType(){
        return actionType;
    }
    
    // Sets the value of the Action Type to the poperation we want to execute...
    public void setActionType(int actionType) {
        this.actionType = actionType;
        setChanged();
    }
    
    // To set and change the Status of the lable STATUS
    public void setResult(int result) {
        this.result = result;
        setChanged();
    }
    public int getResult(){
        return this.result;
    }
    
    // To set the Value of the lblStatus...
    public void setLblStatus(String lblStatus) {
        this.lblStatus = lblStatus;
        setChanged();
    }
    public String getLblStatus(){
        return lblStatus;
    }
    
    //To reset the Value of lblStatus after each save action...
    public void resetStatus(){
        this.setLblStatus(ClientConstants.ACTION_STATUS[0]);
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
    
    public void ttNotifyObservers(){
        setChanged();
        notifyObservers();
    }
    
    public void resetForm(){
        setLblShareTransID("");
        
        setTxtAcctFrom("");
        setTxtAcctTo("");
        setTxtShareFrom("");
        setTxtShareTo("");
        setTxtRemarks("");
        setCboShareType("");
        setDivAcno("");
        setDivUptoDate(null);
        setDivAmt(null);
    }
    
    private String txtAcctFrom = "";
    private String txtAcctTo = "";
    private String txtShareFrom = "";
    private String txtShareTo = "";
    private String txtRemarks = "";
    private String lblShareTransID = "";
    
    // Setter method for txtAcctFrom
    void setTxtAcctFrom(String txtAcctFrom){
        this.txtAcctFrom = txtAcctFrom;
        setChanged();
    }
    // Getter method for txtAcctFrom
    String getTxtAcctFrom(){
        return this.txtAcctFrom;
    }
    
    // Setter method for txtAcctTo
    void setTxtAcctTo(String txtAcctTo){
        this.txtAcctTo = txtAcctTo;
        setChanged();
    }
    // Getter method for txtAcctTo
    String getTxtAcctTo(){
        return this.txtAcctTo;
    }
    
    // Setter method for txtShareFrom
    void setTxtShareFrom(String txtShareFrom){
        this.txtShareFrom = txtShareFrom;
        setChanged();
    }
    // Getter method for txtShareFrom
    String getTxtShareFrom(){
        return this.txtShareFrom;
    }
    
    // Setter method for txtShareTo
    void setTxtShareTo(String txtShareTo){
        this.txtShareTo = txtShareTo;
        setChanged();
    }
    // Getter method for txtShareTo
    String getTxtShareTo(){
        return this.txtShareTo;
    }
    
    // Setter method for txtRemarks
    void setTxtRemarks(String txtRemarks){
        this.txtRemarks = txtRemarks;
        setChanged();
    }
    // Getter method for txtRemarks
    String getTxtRemarks(){
        return this.txtRemarks;
    }
    
    // Setter method for lblShareTransID
    void setLblShareTransID(String lblShareTransID){
        this.lblShareTransID = lblShareTransID;
        setChanged();
    }
    // Getter method for lblShareTransID
    String getLblShareTransID(){
        return this.lblShareTransID;
    }
    
    //__ To CHeck if the Account Holder Actually got the Specified Shares or not...
    //    public boolean checkShareTrans(HashMap dataMap){
    //        boolean isIssued = false;
    //        final List shareDataList = ClientUtil.executeQuery("validateShareData", dataMap);
    //        if(shareDataList.size() > 0){
    //            isIssued = true;
    //        }else{
    //            isIssued = false;
    //        }
    //
    //        return isIssued;
    //    }
    
    
    public void setAuthorizeMap(HashMap authorizeMap) {
        this.authorizeMap = authorizeMap;
    }
    
    public HashMap getAuthorizeMap() {
        return this.authorizeMap;
    }
    
    /* Set and GET METHODS FOR THE tABLE...*/
    void setTblShareTrans(EnhancedTableModel tblShareTransTab){
        this.tblShareTransTab = tblShareTransTab;
        setChanged();
    }
    
    EnhancedTableModel getTblShareTrans(){
        return this.tblShareTransTab;
    }
    
    public void setShareTransTabData(){
        final HashMap dataMap = new HashMap();
        dataMap.put("SHAREACCTNO",getTxtAcctFrom());
        List chekLoanAct=ClientUtil.executeQuery("ShareTransfer.getLoanActStatus", dataMap);
        if(chekLoanAct !=null){
            final List shareDataList = ClientUtil.executeQuery("getShareAcctData", dataMap);
            int size = shareDataList.size();
            
            //__ If the Data Exists...
            if(size > 0){
                for (int i=0 ; i < size ; i++){
                    shareTransTabRow = new ArrayList();
                    final HashMap resultMap = (HashMap)shareDataList.get(i);
                    
                    shareTransTabRow.add(CommonUtil.convertObjToStr(resultMap.get("SHARE_ACCT_DET_NO")));
                    shareTransTabRow.add(CommonUtil.convertObjToStr(resultMap.get("SHARE_NO_FROM")));
                    shareTransTabRow.add(CommonUtil.convertObjToStr(resultMap.get("SHARE_NO_TO")));
                    shareTransTabRow.add(CommonUtil.convertObjToStr(resultMap.get("NO_OF_SHARES")));
                    
                    tblShareTransTab.addRow(shareTransTabRow);
                    shareTransTabRow = null;
                }
            }
        }
        else
            ClientUtil.displayAlert("This Share account Holder Having Loan Account");
    }
    
    //__  To Reset the ShareTrans Table...
    public void resetTable(){
        try{
            ArrayList data = tblShareTransTab.getDataArrayList();
            for(int i=data.size(); i>0; i--)
                tblShareTransTab.removeRow(i-1);
        }catch (Exception e) {
            setResult(ClientConstants.ACTIONTYPE_FAILED);
            log.info("Error in resetTable():");
        }
    }
    
    public boolean validateShareSelection(HashMap dataMap){
        final String SHAREFROM = CommonUtil.convertObjToStr(dataMap.get("SHAREFROM"));
        final String SHARETO = CommonUtil.convertObjToStr(dataMap.get("SHARETO"));
        
        //        final String SHAREFROM = "22";
        //        final String SHARETO = "2222";
        boolean isExist = false;
        ArrayList shareTransList = tblShareTransTab.getDataArrayList();
        int size = shareTransList.size();
        
        for(int i = 0; i < size; i++){
            String fromShare = CommonUtil.convertObjToStr(((ArrayList)shareTransList.get(i)).get(1));
            String toShare = CommonUtil.convertObjToStr(((ArrayList)shareTransList.get(i)).get(2));
            
            //            if((fromShare.compareTo(SHAREFROM) <= 0) && (toShare.compareTo(SHAREFROM) > 0)
            //            && (fromShare.compareTo(SHARETO) < 0) && (toShare.compareTo(SHARETO) >= 0)){
            if((SHAREFROM.compareTo(fromShare) >= 0) && (SHAREFROM.compareTo(toShare) <= 0)
            && (SHARETO.compareTo(fromShare) >= 0) && (SHARETO.compareTo(toShare) <= 0)){
                isExist  = true;
                break;
            }
        }
        return isExist;
    }
    public String callForBehaves(){
        return CommonUtil.convertObjToStr(cbmShareType.getKeyForSelected());
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
    private void makeNull(){
        key = null;
        value = null;
        lookUpHash = null;
        keyValue = null;
        
    }
    /* Splits the keyValue HashMap into key and value arraylists*/
    private void getKeyValue(HashMap keyValue)  throws Exception{
        key = (ArrayList)keyValue.get(CommonConstants.KEY);
        value = (ArrayList)keyValue.get(CommonConstants.VALUE);
    }
    
    /**
     * Getter for property cboShareType.
     * @return Value of property cboShareType.
     */
    public java.lang.String getCboShareType() {
        return cboShareType;
    }
    
    /**
     * Setter for property cboShareType.
     * @param cboShareType New value of property cboShareType.
     */
    public void setCboShareType(java.lang.String cboShareType) {
        this.cboShareType = cboShareType;
    }
    
    /**
     * Getter for property cbmShareType.
     * @return Value of property cbmShareType.
     */
    public ComboBoxModel getCbmShareType() {
        return cbmShareType;
    }
    
    /**
     * Setter for property cbmShareType.
     * @param cbmShareType New value of property cbmShareType.
     */
    public void setCbmShareType(ComboBoxModel cbmShareType) {
        this.cbmShareType = cbmShareType;
    }
    
    /**
     * Getter for property divAcno.
     * @return Value of property divAcno.
     */
    public java.lang.String getDivAcno() {
        return divAcno;
    }
    
    /**
     * Setter for property divAcno.
     * @param divAcno New value of property divAcno.
     */
    public void setDivAcno(java.lang.String divAcno) {
        this.divAcno = divAcno;
    }
    
    /**
     * Getter for property divUptoDate.
     * @return Value of property divUptoDate.
     */
    public java.util.Date getDivUptoDate() {
        return divUptoDate;
    }
    
    /**
     * Setter for property divUptoDate.
     * @param divUptoDate New value of property divUptoDate.
     */
    public void setDivUptoDate(java.util.Date divUptoDate) {
        this.divUptoDate = divUptoDate;
    }
    
    /**
     * Getter for property divAmt.
     * @return Value of property divAmt.
     */
    public java.lang.Double getDivAmt() {
        return divAmt;
    }
    
    /**
     * Setter for property divAmt.
     * @param divAmt New value of property divAmt.
     */
    public void setDivAmt(java.lang.Double divAmt) {
        this.divAmt = divAmt;
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
     * Getter for property keyValue.
     * @return Value of property keyValue.
     */
    //    public java.util.HashMap getKeyValue() {
    //        return keyValue;
    //    }
    
    /**
     * Setter for property keyValue.
     * @param keyValue New value of property keyValue.
     */
    //    public void setKeyValue(java.util.HashMap keyValue) {
    //        this.keyValue = keyValue;
    //    }
    
    public HashMap setAccountName(String shareNum){
        
        final HashMap accountNameMap = new HashMap();
        accountNameMap.put("SHARE_ACCT_NO",shareNum);
        accountNameMap.put("CLOSECHECK",shareNum);
        accountNameMap.put("DIVIDEND_PAID_STATUS","DIVIDEND_PAID_STATUS");
        final java.util.List resultList = ClientUtil.executeQuery("getSelectDividendUnclaimedTransferList",accountNameMap);
        if(resultList != null && resultList.size()>0){
            final HashMap resultMap = (HashMap)resultList.get(0);
            return resultMap;
        } else {
            ClientUtil.displayAlert("share No Not Exists");
            return null;
        }
        
        
        
    }
    
}