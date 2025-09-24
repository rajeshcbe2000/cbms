/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition.
 * 
 *
 * chargesServiceTaxOB.java
 *
 * Created on August 13, 2003, 4:30 PM
 */

package com.see.truetransact.ui.transaction.chargesServiceTax;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.ArrayList;
import java.util.List;
import java.util.Iterator;
import java.util.Date;
import com.see.truetransact.ui.deposit.lien.DepositLienUI;
import com.see.truetransact.clientutil.ComboBoxModel;
import com.see.truetransact.clientproxy.ProxyFactory;
import com.see.truetransact.commonutil.CommonConstants;
import com.see.truetransact.commonutil.CommonUtil;
import com.see.truetransact.transferobject.transaction.chargesServiceTax.ChargesServiceTaxTO;
import com.see.truetransact.ui.common.viewall.TableDialogUI;
import com.see.truetransact.transferobject.TOHeader;
import com.see.truetransact.clientutil.ClientConstants;
import com.see.truetransact.commonutil.TTException;
import org.apache.log4j.Logger;
import com.see.truetransact.clientutil.ClientUtil;
import com.see.truetransact.clientexception.ClientParseException;
import com.see.truetransact.commonutil.DateUtil;
import com.see.truetransact.ui.TrueTransactMain;
import com.see.truetransact.ui.common.transaction.TransactionOB;
import com.see.truetransact.uicomponent.COptionPane;
import com.see.truetransact.ui.common.CommonRB;
import com.see.truetransact.uicomponent.CObservable;
import java.util.Date;
/**
 *
 * @author  Administrator
 * Modified by Karthik
 */
public class ChargesServiceTaxOB extends CObservable {
    private String cboProductID = "";
    private String cboProductType= "";
    private String txtAccountNumber = "";
    private String MainProductTypeValue = "";
    private ComboBoxModel cbmProductID;
    private ComboBoxModel cbmProductType;
    private final String AUTHORIZE="AUTHORIZE";
    private String accountHeadDesc;
    private String accountHeadId;
    private String customerName;
    private String amount = null;
    private String service_tax_amt = null;
    private String total_amt = null;
    private String particulars = "";
    private int actionType;
    private int result;
    private HashMap linkMap=new HashMap();
    
    private String lblStatus = ClientConstants.ACTION_STATUS[ClientConstants.ACTIONTYPE_CANCEL];    
    private final String DELETED_TRANS_TOs = "DELETED_TRANS_TOs";
    private final String NOT_DELETED_TRANS_TOs = "NOT_DELETED_TRANS_TOs";
    
    private final static Logger log = Logger.getLogger(ChargesServiceTaxOB.class);
    private final static ClientParseException parseException = ClientParseException.getInstance();
    private String prodType="";
    private HashMap map;
    private ProxyFactory proxy;
    private HashMap totalLoanAmount;
    //for filling dropdown
    private HashMap lookupMap;
    private HashMap param;
    private HashMap keyValue;
    private ArrayList key;
    private ArrayList value;
    private TransactionOB transactionOB;
    private LinkedHashMap allowedTransactionDetailsTO = null;
    private LinkedHashMap transactionDetailsTO = null;
    private LinkedHashMap deletedTransactionDetailsTO = null;
    ChargesServiceTaxRB objChargesServiceTaxRB = new ChargesServiceTaxRB();
    private static final CommonRB objCommonRB = new CommonRB();
    private HashMap _authorizeMap ;
    private HashMap oldTransDetMap = null;
    private Date currDt = null;
    /** Creates a new instance of chargesServiceTaxOB */
    public ChargesServiceTaxOB() {
        try {
            currDt = ClientUtil.getCurrentDate();

            proxy = ProxyFactory.createProxy();
            
            map = new HashMap();
            map.put(CommonConstants.JNDI, "ChargesServiceTaxJNDI");
            map.put(CommonConstants.HOME, "transaction.chargesServiceTax.ChargesServiceTaxHome");
            map.put(CommonConstants.REMOTE, "transaction.chargesServiceTax.ChargesServiceTaxRemote");
            
            
        } catch (Exception e) {
            parseException.logException(e,true);
        }
    }
    
    /** To fill the comboboxes */
    private void fillDropdown() throws Exception{
        lookupMap = new HashMap();
        lookupMap.put(CommonConstants.JNDI, "LookUpJNDI");
        lookupMap.put(CommonConstants.HOME, "common.lookup.LookUpHome");
        lookupMap.put(CommonConstants.REMOTE, "common.lookup.LookUp");
        
        param = new java.util.HashMap();
        key=new ArrayList();
        value=new ArrayList();
        key.add("");
        value.add("");
        cbmProductID = new ComboBoxModel(key,value);

        final ArrayList lookupKey = new ArrayList();
        lookupKey.add("PRODUCTTYPE");
        param.put(CommonConstants.MAP_NAME, null);
        param.put(CommonConstants.PARAMFORQUERY, lookupKey);

        final HashMap lookupValues = ClientUtil.populateLookupData(param);

        fillData((HashMap)lookupValues.get("PRODUCTTYPE"));
        cbmProductType = new ComboBoxModel(key,value);
    }
    
    /** To set the key & value for comboboxes */
    private void fillData(HashMap keyValue)  throws Exception{
        key = (ArrayList)keyValue.get("KEY");
        value = (ArrayList)keyValue.get("VALUE");
    }
    
    
    public void getProducts(){
        List list=null;
        ArrayList key=new ArrayList();
        ArrayList value=new ArrayList();
        key.add("");
        value.add("");
        HashMap data;
        list=ClientUtil.executeQuery("Transfer.getCreditProduct"+getMainProductTypeValue(),null);
        if(list!=null && list.size()>0){
            int size=list.size();
            for(int i=0;i<size;i++){
                data=(HashMap)list.get(i);
                key.add(data.get("PRODID"));
                value.add(data.get("PRODDESC"));
            }
        }        
        data = null;        
        cbmProductID = new ComboBoxModel(key,value);
        setChanged();
    }

    /** To get data for comboboxes */
//    private HashMap populateData(HashMap obj)  throws Exception{
//        // System.out.println("obj in OB : " + obj);
//        obj.put(CommonConstants.MAP_WHERE, getTxtAccountNumber());
//        System.out.println("getTransProdId :"+getTransProdId());
//        if(prodType.equals("TermLoan"))
//            if(getTransProdId()!=null && getTransProdId().length()>0)
//                obj.put("PROD_ID",getTransProdId());
//            else
//                obj.put("PROD_ID",getCbmProductID().getKeyForSelected());
//        obj.put("PROD_TYPE",prodType);
//        
//        obj.put("CURR_DATE", ClientUtil.getCurrentDateProperFormat());
//        System.out.println("map in OB :MIDDLE " + obj);
//        HashMap where = proxy.executeQuery(obj, map) ;
//        
//        // System.out.println("where : " + where);
//        //        keyValue = (HashMap)where.get("AccountDetailsTO");
//        //        transactionOB.setDetails((List)where.get("TransactionTO"));
//        //        log.info("Got HashMap");
//        return where;
//    }
    
    void setCboProductID(String cboProductID){
        this.cboProductID = cboProductID;
        setChanged();
    }
    String getCboProductID(){
        return this.cboProductID;
    }
    
    void setTxtAccountNumber(String txtAccountNumber){
        this.txtAccountNumber = txtAccountNumber;
        setChanged();
    }
    String getTxtAccountNumber(){
        return this.txtAccountNumber;
    }
    
    void setCbmProductID(ComboBoxModel cbmProductID){
        this.cbmProductID = cbmProductID;
        setChanged();
    }
    ComboBoxModel getCbmProductID(){
        return this.cbmProductID;
    }
    
    public String getAccountHeadDesc() {
        return accountHeadDesc;
    }
    
    public void setAccountHeadDesc(String accountHeadDesc) {
        this.accountHeadDesc = accountHeadDesc;
        setChanged();
    }
    
    public String getAccountHeadId() {
        return this.accountHeadId;
    }
    
    public void setAccountHeadId(String accountHeadId) {
        this.accountHeadId = accountHeadId;
        setChanged();
    }
    
    public String getCustomerName() {
        return this.customerName;
    }
    
    public void setCustomerName(String customerName) {
        this.customerName = customerName;
        setChanged();
    }
    
    public int getActionType(){
        return this.actionType;
    }
    
    public void setActionType(int actionType) {
        this.actionType = actionType;
        setChanged();
    }
    
    public int getResult(){
        return this.result;
    }
    
    public void setResult(int result) {
        this.result = result;
        setChanged();
    }
    
    public String getLblStatus(){
        return this.lblStatus;
    }
    
    public void setLblStatus(String lblStatus) {
        this.lblStatus = lblStatus;
        setChanged();
    }
    
    /** set the value of Account head ID and description based on the product selected
     * in the UI
     */
    public void getAccountHeadForProduct() {
        
        /* based on the selection from the product combo box, one accound head
         * will be fetched from database and displayed on screen
         * same LookUp bean will be used for this purpose
         */
        param.put(CommonConstants.MAP_NAME,"getAccHead");
        param.put(CommonConstants.PARAMFORQUERY, getCboProductID());
        try {
            final HashMap lookupValues = ClientUtil.populateLookupData(param);
            fillData((HashMap)lookupValues.get("DATA"));
            //If proper value is returned, then the size will be more than 1, else do nothing
            if( value.size() > 1 ){
                setAccountHeadId((String)value.get(1));
                setAccountHeadDesc((String)key.get(1));
            }
            ttNotifyObservers();
        } catch (Exception e) {
            parseException.logException(e,true);
        }
        
    }
    
    /** To get customername & balance info */
    //    public void getCustomerNameForAccountNumber() {
    
        /* based on the selection from the product combo box, one accound head
         * will be fetched from database and displayed on screen
         * same LookUp bean will be used for this purpose
         
        param.put(CommonConstants.MAP_NAME,"getCustomerName");
        param.put(CommonConstants.PARAMFORQUERY, getTxtAccountNumber());
        try {
            HashMap lookupValues = ClientUtil.populateLookupData(param);
            fillData((HashMap)lookupValues.get("DATA"));
            setCustomerName((String)value.get(1));
            setAvailableBalance((String)key.get(1));
            param.put(CommonConstants.MAP_NAME,"getAccountClosingCharges");
            lookupValues = ClientUtil.populateLookupData(param);
            fillData((HashMap)lookupValues.get("DATA"));
            this.txtAccountClosingCharges=(String)(value.get(1));
            setTxtAccountClosingCharges((String)(value.get(1)));
            // System.out.println(this.getTxtAccountClosingCharges());
            ttNotifyObservers();
        } catch (Exception e) {
            parseException.logException(e,true);
//        } */
    //        getAccountClosingCharges();
    //    }
    
    
    
    public void ttNotifyObservers(){
        notifyObservers();
    }
    
    /** To perform the necessary operation */
    public void doAction() {
        try {
            // System.out.println("Coming here ...........");
            //If actionType such as NEW, EDIT, DELETE is not 0, then proceed
            if( actionType != ClientConstants.ACTIONTYPE_CANCEL ){
                //If actionType has got propervalue then doActionPerform, else throw error
                //                if( getCommand() != null ){
                doActionPerform();
                //                }
                //                else{
                //                    final AccountClosingRB accountClosingRB = new AccountClosingRB();
                //                    throw new TTException(accountClosingRB.getString("TOCommandError"));
                //                }
            }
        } catch (Exception e) {
            // System.out.println("Error in doAction of A/c Closing OB....");
            setResult(ClientConstants.ACTIONTYPE_FAILED);
            parseException.logException(e,true);
        }
    }
    
    
    /** To perform the necessary action */
    private void doActionPerform() throws Exception{
        final ChargesServiceTaxTO objChargesServiceTaxTO = setAccountClosingData();
        objChargesServiceTaxTO.setCommand(getCommand());
        if(!getCommand().equals("") && getCommand().equals("INSERT")){
            objChargesServiceTaxTO.setStatus(getAction());
            objChargesServiceTaxTO.setCreated_by(TrueTransactMain.USER_ID);
            objChargesServiceTaxTO.setCreated_dt(currDt);
            objChargesServiceTaxTO.setBranchCode(TrueTransactMain.BRANCH_ID);
        }
        final HashMap data = new HashMap();
        
        if (transactionDetailsTO == null)
            transactionDetailsTO = new LinkedHashMap();
        
        if (deletedTransactionDetailsTO != null) {
            transactionDetailsTO.put(DELETED_TRANS_TOs,deletedTransactionDetailsTO);
            deletedTransactionDetailsTO = null;
        }
        
        transactionDetailsTO.put(NOT_DELETED_TRANS_TOs,allowedTransactionDetailsTO);
        allowedTransactionDetailsTO = null;
        
        data.put("TransactionTO",transactionDetailsTO);
        
        data.put("ChargesServiceTaxTO",objChargesServiceTaxTO);     
//        if(getCommand().equals("AUTHORIZE"))
         if(getActionType() == ClientConstants.ACTIONTYPE_AUTHORIZE ||
         getActionType() == ClientConstants.ACTIONTYPE_REJECT)
            data.put(CommonConstants.AUTHORIZEMAP, getAuthorizeMap()); //For Authorization added 28 Apr 2005
        data.put("MODE",getCommand());
        System.out.println("#### oldTransDetMap : "+oldTransDetMap);
        
        if (oldTransDetMap != null && oldTransDetMap.size() > 0){
            if (oldTransDetMap.containsKey("AMT_TRANSACTION"))
                data.put("AMT_TRANSACTION", oldTransDetMap.get("AMT_TRANSACTION"));
            if (oldTransDetMap.containsKey("SERVICE_TAX_AMT_TRANSACTION"))
                data.put("SERVICE_TAX_AMT_TRANSACTION", oldTransDetMap.get("SERVICE_TAX_AMT_TRANSACTION"));
            if (oldTransDetMap.containsKey("TOTAL_AMT_TRANSACTION"))
                data.put("TOTAL_AMT_TRANSACTION", oldTransDetMap.get("TOTAL_AMT_TRANSACTION"));
//            if (oldTransDetMap.containsKey("TRANSFER_TRANS_DETAILS"))
//                data.put("TRANSFER_TRANS_DETAILS", oldTransDetMap.get("TRANSFER_TRANS_DETAILS"));
        }
        data.put(CommonConstants.MODULE, getModule());
        data.put(CommonConstants.SCREEN, getScreen());
        data.put(CommonConstants.SELECTED_BRANCH_ID, getSelectedBranchID());
        data.put(CommonConstants.BRANCH_ID, TrueTransactMain.BRANCH_ID);
        System.out.println("data in A/c Closing OB : " + data);
        HashMap proxyResultMap = proxy.execute(data, map);
        if (proxyResultMap != null && proxyResultMap.containsKey(CommonConstants.TRANS_ID)) {
            //                if(arrayList.size()==1)
            ClientUtil.showMessageWindow("Transaction No. : " + CommonUtil.convertObjToStr(proxyResultMap.get(CommonConstants.TRANS_ID)));
            //                else{
//            tranIdList+=proxyResultMap.get(CommonConstants.TRANS_ID)+"\n";
            //                }
        }
        oldTransDetMap = null;
        setProxyReturnMap(proxyResultMap);
        setResult(actionType);
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
                command=AUTHORIZE;
                break;
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
            default:
        }
        // System.out.println("command : " + command);
        return action;
    }
    
    private ChargesServiceTaxTO setAccountClosingData()  throws Exception{
        final ChargesServiceTaxTO objChargesServiceTaxTO = new ChargesServiceTaxTO();
        objChargesServiceTaxTO.setProd_type(CommonUtil.convertObjToStr(getCboProductType()));
        if(getCboProductType().equals("GL")){
            objChargesServiceTaxTO.setAc_Head(CommonUtil.convertObjToStr(getAccountHeadId()));
            objChargesServiceTaxTO.setAcct_num("");
        }else{
            HashMap acHeadMap = new HashMap();
            acHeadMap.put("PROD_ID",getCboProductID());
            List lst = ClientUtil.executeQuery("", acHeadMap);
            if(lst!=null && lst.size()>0){
                acHeadMap = (HashMap)lst.get(0);
                objChargesServiceTaxTO.setAc_Head(CommonUtil.convertObjToStr(acHeadMap.get("AC_HEAD")));                
            }
            objChargesServiceTaxTO.setAcct_num(CommonUtil.convertObjToStr(getTxtAccountNumber()));            
        }
        objChargesServiceTaxTO.setProd_id(CommonUtil.convertObjToStr(getCboProductID()));
        objChargesServiceTaxTO.setParticulars(CommonUtil.convertObjToStr(getParticulars()));
        objChargesServiceTaxTO.setAmount(CommonUtil.convertObjToDouble(getAmount()));
        objChargesServiceTaxTO.setService_tax_amt(CommonUtil.convertObjToDouble(getService_tax_amt()));
        objChargesServiceTaxTO.setTotal_amt(CommonUtil.convertObjToDouble(getTotal_amt()));
        System.out.println("####accountto"+objChargesServiceTaxTO);
        return objChargesServiceTaxTO;
    }
    
    /** To retrieve a particular customer's accountclosing record */
    public void getData(HashMap whereMap) {
        try{
            System.out.println("map in getData OB : " + whereMap);
            final HashMap data = (HashMap)proxy.executeQuery(whereMap,map);
            System.out.println(">>>>> data in OBSIDE##### : " + data);
            populateAccountClosingData(data);
            List list = (List) data.get("TransactionTO");
            System.out.println("transaction@@@####"+list);
            transactionOB.setDetails(list);
            allowedTransactionDetailsTO=transactionOB.getAllowedTransactionDetailsTO();
        }catch(Exception e){
            parseException.logException(e,true);
            e.printStackTrace();
        }
    }
    
    /** To populate data into the screen */
    private void populateAccountClosingData(HashMap data) throws Exception{
        ChargesServiceTaxTO objChargesServiceTaxTO = new ChargesServiceTaxTO();
        System.out.println("data in populateAccountClosingData : " + data);
        objChargesServiceTaxTO = (ChargesServiceTaxTO) ((List) data.get("ChargesServiceTaxTO")).get(0);

        setCboProductID(CommonUtil.convertObjToStr(objChargesServiceTaxTO.getProd_id()));
        setCboProductType(CommonUtil.convertObjToStr(objChargesServiceTaxTO.getProd_type()));
        setAccountHeadId(CommonUtil.convertObjToStr(objChargesServiceTaxTO.getAc_Head()));
        setTotal_amt(CommonUtil.convertObjToStr(objChargesServiceTaxTO.getTotal_amt()));
        setAmount(CommonUtil.convertObjToStr(objChargesServiceTaxTO.getAmount()));
        setService_tax_amt(CommonUtil.convertObjToStr(objChargesServiceTaxTO.getService_tax_amt()));
        setParticulars(CommonUtil.convertObjToStr(objChargesServiceTaxTO.getParticulars()));
        oldTransDetMap = new HashMap();
        if (data.containsKey("AMT_TRANSACTION"))
            oldTransDetMap.put("AMT_TRANSACTION", data.get("AMT_TRANSACTION"));
        if (data.containsKey("SERVICE_TAX_AMT_TRANSACTION"))
            oldTransDetMap.put("SERVICE_TAX_AMT_TRANSACTION", data.get("SERVICE_TAX_AMT_TRANSACTION"));
        if (data.containsKey("TOTAL_AMT_TRANSACTION"))
            oldTransDetMap.put("TOTAL_AMT_TRANSACTION", data.get("TOTAL_AMT_TRANSACTION"));
    }
    
    public void resetForm(){
//        setCboProductID((String)cbmProductID.getElementAt(0));
        setAccountHeadId("");
        setAccountHeadDesc("");
        setTxtAccountNumber("");
        setCustomerName("");
        setCboProductID("");
        setCboProductType("");
        setParticulars("");
        setTotal_amt("");
        setService_tax_amt("");
        setAmount("");
        setOldTransDetMap(null);
        setChanged();
        ttNotifyObservers();
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
    
    public void updateAuthorizeStatus() {
        HashMap hash = null;
        String status = null;
        // System.out.println("Records'll be updated... ");
        if(getActionType() == ClientConstants.ACTIONTYPE_AUTHORIZE) {
            status = CommonConstants.STATUS_AUTHORIZED;
        } else if(getActionType() == ClientConstants.ACTIONTYPE_REJECT) {
            status = CommonConstants.STATUS_REJECTED;
        } else if(getActionType() == ClientConstants.ACTIONTYPE_EXCEPTION) {
            status = CommonConstants.STATUS_EXCEPTION;
        }
        
        hash = new HashMap();
        hash.put("ACCOUNTNO",txtAccountNumber);
        hash.put("STATUS", status);
        hash.put("USER_ID",TrueTransactMain.USER_ID);
        hash.put("ACCOUNT_STATUS","CLOSED");
        ClientUtil.execute("authorizeUpdateAccountCloseTO", hash);
        ClientUtil.execute("authorizeAcctStatus", hash);
        ClientUtil.execute("authorizeAcctStatusTL", hash);
        // System.out.println("Record Updated : " +hash);
        
    }
    
    public int getUnPaidCheques(String acctNo) {
        HashMap chequesMap = new HashMap();
        int issued = 0, returned = 0, used = 0;
        
        chequesMap.put("ACCTNO",acctNo);
        // System.out.println("chequesMap: "+ chequesMap);
        final List resultList = ClientUtil.executeQuery("getNoChequesIssued", chequesMap);
        if (resultList != null && resultList.size() > 0){
            final HashMap resultMap = (HashMap)resultList.get(0);
            issued = CommonUtil.convertObjToInt(resultMap.get("ISSUED_LEAVES"));
        }
        
        final List returnedList = ClientUtil.executeQuery("getNoChequesUsed", chequesMap);
        if (returnedList != null && returnedList.size() > 0){
            int size = returnedList.size();
            for (int i = 0; i< size; i++){
                HashMap returnedMap = (HashMap)returnedList.get(i);
                used = used + CommonUtil.convertObjToInt(returnedMap.get("COUNT"));
            }
        }
        
        returned = issued - used;
        // System.out.println("returned: "+ returned);
        // System.out.println("issued: " + issued);
        // System.out.println("used: " +used);
        return returned;
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
        // System.out.println("In OB of RemIssue : " + allowedTransactionDetailsTO);
        this.allowedTransactionDetailsTO = allowedTransactionDetailsTO;
    }
    
    public int showAlertWindow(String amtLimit) {
        int option = 1;
        try{
            String[] options = {objCommonRB.getString("cDialogOK")};
            option = COptionPane.showOptionDialog(null,amtLimit, CommonConstants.WARNINGTITLE,
            COptionPane.OK_OPTION, COptionPane.WARNING_MESSAGE,
            null, options, options[0]);
        }catch (Exception e){
            parseException.logException(e,true);
        }
        return option;
    }
    
    /**
     * Getter for property _authorizeMap.
     * @return Value of property _authorizeMap.
     */
    public java.util.HashMap getAuthorizeMap() {
        return _authorizeMap;
    }
    
    /**
     * Setter for property _authorizeMap.
     * @param _authorizeMap New value of property _authorizeMap.
     */
    public void setAuthorizeMap(java.util.HashMap authorizeMap) {
        this._authorizeMap = authorizeMap;
    }
    
    /**
     * Getter for property totalLoanAmount.
     * @return Value of property totalLoanAmount.
     */
    public java.util.HashMap getTotalLoanAmount() {
        return totalLoanAmount;
    }
    
    /**
     * Setter for property totalLoanAmount.
     * @param totalLoanAmount New value of property totalLoanAmount.
     */
    public void setTotalLoanAmount(java.util.HashMap totalLoanAmount) {
        this.totalLoanAmount = totalLoanAmount;
    }
    
    /**
     * Getter for property prodType.
     * @return Value of property prodType.
     */
    public java.lang.String getProdType() {
        return prodType;
    }
    
    /**
     * Setter for property prodType.
     * @param prodType New value of property prodType.
     */
    public void setProdType(java.lang.String prodType) {
        this.prodType = prodType;
        try{
            fillDropdown();
        }
        catch(Exception e) {
            e.printStackTrace();
        }
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
     * Getter for property linkMap.
     * @return Value of property linkMap.
     */
    public java.util.HashMap getLinkMap() {
        return linkMap;
    }
    
    /**
     * Setter for property linkMap.
     * @param linkMap New value of property linkMap.
     */
    public void setLinkMap(java.util.HashMap linkMap) {
        this.linkMap = linkMap;
    }
    
    /**
     * Getter for property cbmProductType.
     * @return Value of property cbmProductType.
     */
    public com.see.truetransact.clientutil.ComboBoxModel getCbmProductType() {
        return cbmProductType;
    }
    
    /**
     * Setter for property cbmProductType.
     * @param cbmProductType New value of property cbmProductType.
     */
    public void setCbmProductType(com.see.truetransact.clientutil.ComboBoxModel cbmProductType) {
        this.cbmProductType = cbmProductType;
    }
    
    /**
     * Getter for property cboProductType.
     * @return Value of property cboProductType.
     */
    public java.lang.String getCboProductType() {
        return cboProductType;
    }
    
    /**
     * Setter for property cboProductType.
     * @param cboProductType New value of property cboProductType.
     */
    public void setCboProductType(java.lang.String cboProductType) {
        this.cboProductType = cboProductType;
    }
    
    /**
     * Getter for property MainProductTypeValue.
     * @return Value of property MainProductTypeValue.
     */
    public java.lang.String getMainProductTypeValue() {
        return MainProductTypeValue;
    }
    
    /**
     * Setter for property MainProductTypeValue.
     * @param MainProductTypeValue New value of property MainProductTypeValue.
     */
    public void setMainProductTypeValue(java.lang.String MainProductTypeValue) {
        this.MainProductTypeValue = MainProductTypeValue;
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
     * Getter for property amount.
     * @return Value of property amount.
     */
    public java.lang.String getAmount() {
        return amount;
    }
    
    /**
     * Setter for property amount.
     * @param amount New value of property amount.
     */
    public void setAmount(java.lang.String amount) {
        this.amount = amount;
    }
    
    /**
     * Getter for property service_tax_amt.
     * @return Value of property service_tax_amt.
     */
    public java.lang.String getService_tax_amt() {
        return service_tax_amt;
    }
    
    /**
     * Setter for property service_tax_amt.
     * @param service_tax_amt New value of property service_tax_amt.
     */
    public void setService_tax_amt(java.lang.String service_tax_amt) {
        this.service_tax_amt = service_tax_amt;
    }
    
    /**
     * Getter for property total_amt.
     * @return Value of property total_amt.
     */
    public java.lang.String getTotal_amt() {
        return total_amt;
    }
    
    /**
     * Setter for property total_amt.
     * @param total_amt New value of property total_amt.
     */
    public void setTotal_amt(java.lang.String total_amt) {
        this.total_amt = total_amt;
    }
    
}
