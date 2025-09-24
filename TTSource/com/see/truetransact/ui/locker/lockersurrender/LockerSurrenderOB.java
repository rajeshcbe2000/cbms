/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition.
 * 
 *
 * TokenConfigOB.java
 *
 * Created on Thu Jan 20 15:43:27 IST 2005
 */

package com.see.truetransact.ui.locker.lockersurrender;

import org.apache.log4j.Logger;
import java.util.HashMap;
import java.util.ArrayList;
import java.util.List;

import com.see.truetransact.uicomponent.COptionPane;
import com.see.truetransact.clientproxy.ProxyFactory;
import com.see.truetransact.clientutil.ComboBoxModel;
import com.see.truetransact.clientutil.ClientUtil;
import com.see.truetransact.clientutil.ClientConstants;
import com.see.truetransact.clientutil.EnhancedTableModel;
import com.see.truetransact.clientexception.ClientParseException;
import com.see.truetransact.commonutil.CommonConstants;
import com.see.truetransact.commonutil.CommonUtil;
import com.see.truetransact.uicomponent.CObservable;
import com.see.truetransact.ui.TrueTransactMain;
import com.see.truetransact.clientproxy.ProxyParameters;
import com.see.truetransact.ui.common.transaction.TransactionOB;
import java.util.LinkedHashMap;
import com.see.truetransact.transferobject.locker.lockersurrender.LockerSurrenderTO;
import com.see.truetransact.commonutil.DateUtil;
import com.see.truetransact.transferobject.servicetax.servicetaxdetails.ServiceTaxDetailsTO;
import com.see.truetransact.ui.common.servicetax.ServiceTaxCalculation;
import java.util.Date;
/**
 *
 * @author Ashok Vijayakumar
 */

public class LockerSurrenderOB extends CObservable{
    
    /** Variables Declaration - Corresponding each Variable is in TokenConfigUI*/
//    private String txtTokenConfigId = "";
//    private String cboTokenType = "";
//    private ComboBoxModel cbmTokenType;//Model for ui combobox cboTokenType
//    private String txtSeriesNo = "";
//    private String txtStartingTokenNo = "";
//    private String txtEndingTokenNo = "";
//    private String txtNoOfTokens = "";
     private TransactionOB transactionOB;
    java.util.ResourceBundle resourceBundle = java.util.ResourceBundle.getBundle("com.see.truetransact.ui.locker.lockeroperation.LockerOperationRB", ProxyParameters.LANGUAGE);
    
    private String optID = "";
    private String lblDateVal = "";
    private String lblLockerOutDtVal = "";
    private String lblCustNameVal = "";
    private String lblCustomerIdVal = "";
    private String lblModeOfOpVal = "";
    private String txtLockerNo = "";
    private String txtCharges = "";
    private String txtCustId = "";
    private String txtName = "";
    private String txtPassword = "";
    private HashMap _authorizeMap;
    private String cboProdID = "";
    private ComboBoxModel cbmProdID;
    private HashMap lookupMap;
    private String serviceTax = "";
    private String prodID="";
    private String txtRefund="";
    private String breakOpenDt="";
    private String breakOpenRemarks="";
    private LinkedHashMap allowedTransactionDetailsTO = null;
    private LinkedHashMap transactionDetailsTO = null;
    private LinkedHashMap deletedTransactionDetailsTO = null;
    
    private final String DELETED_TRANS_TOs = "DELETED_TRANS_TOs";
    private final String NOT_DELETED_TRANS_TOs = "NOT_DELETED_TRANS_TOs";
    
    private EnhancedTableModel tbmInstructions2;
    private ArrayList tblHeadings3;
    private boolean deletingExists = false;
    private boolean cbCustomer;
    private boolean chkBforeExpDt = false;
    private boolean cbRefundYes;
    private ArrayList existingData;
    private ArrayList newInstructionRow = null;
    private ArrayList newData = new ArrayList();
    private String collectRentMM="";
    private String   collectRentYYYY="";
    private String lblCurrentDateVal="";
    private String callingApplicantName="";
    private  String lblNewExpDateVal="";
     //private  String txtSurDate="";
    private String lblNewExpDate="";
    /** Other Varibales Declartion */
    private ProxyFactory proxy;
    private HashMap map;//HashMap for setting key,values for Jndi,home,remote
    private static LockerSurrenderOB objLockerSurrenderOB;//Singleton Object Reference
    private final static Logger log = Logger.getLogger(LockerSurrenderOB.class);//Creating Instace of Log
    private ArrayList key,value;//ArrayList for populating the ComboBoxModel key-Storing keys value-for Storing Values
    private HashMap keyValue;//Which holds the resultset values as key,value pairs for populatine ComboBoxModel
    private final static ClientParseException parseException = ClientParseException.getInstance();
    private int _actionType;//This vairable is used to type of action 1-New 2-Edit 3-Delete
    private int _result;
    private String lblStatus = ClientConstants.ACTION_STATUS[ClientConstants.ACTIONTYPE_CANCEL];
    private boolean renew = false;
    private boolean surrender = false;
    private boolean breakOpen=false;
    private boolean rentCollection=false;
    private String remarks = "";
    private String date="";
    private String txtPenalAmt = "";
    private String txtActNum = "";
    private String chkDefaulter = "";
    private String chkNoTrans = "";
    private static Date currDt = null;
    private String lblServiceTaxval = "";
    private HashMap serviceTax_Map = null;

    public String getLblServiceTaxval() {
        return lblServiceTaxval;
    }

    public void setLblServiceTaxval(String lblServiceTaxval) {
        this.lblServiceTaxval = lblServiceTaxval;
    }

    public HashMap getServiceTax_Map() {
        return serviceTax_Map;
    }

    public void setServiceTax_Map(HashMap serviceTax_Map) {
        this.serviceTax_Map = serviceTax_Map;
    }
    
    public String getTxtPenalAmt() {
        return txtPenalAmt;
    }
    public void setTxtPenalAmt(String txtPenalAmt) {
        this.txtPenalAmt = txtPenalAmt;
    }

    public boolean isChkBforeExpDt() {
        return chkBforeExpDt;
    }

    public void setChkBforeExpDt(boolean chkBforeExpDt) {
        this.chkBforeExpDt = chkBforeExpDt;
    }
     
    /** Consturctor Declaration  for  TokenConfigOB */
    private LockerSurrenderOB() {
        try {
            proxy = ProxyFactory.createProxy();
            setOperationMap();
            initUIComboBoxModel();
            createTblHeadings();
            createTbmHead();
            fillDropdown();
        }catch(Exception e){
            parseException.logException(e,true);
        }
    }
    
    static {
        try {
            log.info("Creating ParameterOB...");
            currDt = ClientUtil.getCurrentDate();
            objLockerSurrenderOB = new LockerSurrenderOB();
        } catch(Exception e) {
            parseException.logException(e,true);
        }
    }
     public void setTransactionOB(TransactionOB transactionOB) {
        this.transactionOB = transactionOB;
    }
    // Sets the HashMap required to set JNDI,Home and Remote
    private void setOperationMap() throws Exception{
        map = new HashMap();
        map.put(CommonConstants.JNDI, "LockerSurrenderJNDI");
        map.put(CommonConstants.HOME, "locker.lockersurrender.LockerSurrenderHome");
        map.put(CommonConstants.REMOTE, "locker.lockersurrender.LockerSurrender");
    }
    
    private void createTblHeadings(){
        tblHeadings3 = new ArrayList();
        tblHeadings3.add(resourceBundle.getString("tblHeading1"));
        tblHeadings3.add(resourceBundle.getString("tblHeading2"));
        tblHeadings3.add(resourceBundle.getString("tblHeading3"));
//        tblHeadings3.add(resourceBundle.getString("tblHeadingLock4"));
//        tblHeadings3.add(resourceBundle.getString("tblHeadingLock5"));
//        tblHeadings1.add(resourceBundle.getString("tblHeading4"));
    }
    private void createTbmHead(){
        tbmInstructions2 = new EnhancedTableModel(null, tblHeadings3);
    }
    /** Creating instance for ComboboxModel cbmTokenType */
    private void initUIComboBoxModel(){
        cbmProdID = new ComboBoxModel();
    }
    
    /* Filling up the the ComboBox in the UI*/
    private void fillDropdown() throws Exception{
//        try{
//            log.info("Inside FillDropDown");
//            lookUpHash = new HashMap();
//            lookUpHash.put(CommonConstants.MAP_NAME, null);
//            final ArrayList lookup_keys = new ArrayList();
//            lookup_keys.add("TOKEN_TYPE");
//            lookUpHash.put(CommonConstants.PARAMFORQUERY, lookup_keys);
//            keyValue = ClientUtil.populateLookupData(lookUpHash);
//            getKeyValue((HashMap)keyValue.get("TOKEN_TYPE"));
//            cbmProdID = new ComboBoxModel(key,value);
//        }catch(NullPointerException e){
//            parseException.logException(e,true);
//        }catch(Exception e){
//            parseException.logException(e,true);
//        }
        lookupMap = new HashMap();
        lookupMap.put(CommonConstants.JNDI, "LookUpJNDI");
        lookupMap.put(CommonConstants.HOME, "common.lookup.LookUpHome");
        lookupMap.put(CommonConstants.REMOTE, "common.lookup.LookUp");
        
        final HashMap param = new HashMap();
        
        param.put(CommonConstants.MAP_NAME, "getLockerProducts");
        param.put(CommonConstants.PARAMFORQUERY, null);
        HashMap lookupValues = ClientUtil.populateLookupData(param);
        fillData((HashMap)lookupValues.get(CommonConstants.DATA));
        int j=value.size();
        for(int i=1;i<j;i++){
            value.set(i, (String)value.get(i)+" ("+(String)key.get(i)+")");
        }
        cbmProdID = new ComboBoxModel(key,value);
        
        param.put(CommonConstants.MAP_NAME,null);
    }
    private void fillData(HashMap keyValue)  throws Exception{
        key = (ArrayList)keyValue.get(CommonConstants.KEY);
        value = (ArrayList)keyValue.get(CommonConstants.VALUE);
    }
    
    private HashMap populateDataLocal(HashMap obj)  throws Exception{
        keyValue = proxy.executeQuery(obj,lookupMap);
        
        return keyValue;
    }
    
    /**
     * Returns an instance of TokenConfigOB.
     * @return  TokenConfigOB
     */
    
    public static LockerSurrenderOB getInstance()throws Exception{
        return objLockerSurrenderOB;
    }
    
//    public void populateTblActData(HashMap mapData){
//                   HashMap param = new HashMap();
//          param.put("CUST_ID", mapData.get("CUST_ID"));
//          param.put("LOCKER_NUM", mapData.get("LOCKER_NUM"));
//           param.put("ISSUE_ID", mapData.get("ISSUE_ID"));
//          List lstData = (List) ClientUtil.executeQuery("selectLockerActOptTO", param);
//          param = null;
//          if(lstData != null && lstData.size() > 0){
//              for(int i=0; i < lstData.size(); i++){
//              param = (HashMap) lstData.get(i);
//              existData = new ArrayList();
//              existData.add(param.get("CUST_ID"));
//              existData.add(param.get("NAME"));
//              existData.add(param.get(""));
////              existData.add(param.get("END_DT"));
////              existData.add(param.get("COMMISION"));
////              existData.add(param.get("SERVICE_TAX"));
//              tbmInstructions2.insertRow(tbmInstructions2.getRowCount(), existData);
//
//    }
//          }
//    }
    
//     public void doAction(String command) {
//        try {
//            if( actionType != ClientConstants.ACTIONTYPE_CANCEL ){
//                //If actionType has got propervalue then doActionPerform, else throw error
//                if( getCommand() != null || getAuthorizeMap() != null){
//                    doActionPerform();
//                }
//                else{
//                    final RemittancePaymentRB objRemittancePaymentRB = new RemittancePaymentRB();
//                    throw new TTException(objRemittancePaymentRB.getString("TOCommandError"));
//                }
//            }
//        } catch (Exception e) {
//            setResult(ClientConstants.ACTIONTYPE_FAILED);
//            parseException.logException(e,true);
//        }
//    }
     
    /**
     * Getter for property txtTokenConfigId.
     * @return Value of property txtTokenConfigId.
     */
//    public java.lang.String getTxtTokenConfigId() {
//        return txtTokenConfigId;
//    }
    
    /**
     * Setter for property txtTokenConfigId.
     * @param txtTokenConfigId New value of property txtTokenConfigId.
     */
//    public void setTxtTokenConfigId(java.lang.String txtTokenConfigId) {
//        this.txtTokenConfigId = txtTokenConfigId;
//        setChanged();
//    }
//    
//    // Setter method for cboTokenType
//    void setCboTokenType(String cboTokenType){
//        this.cboTokenType = cboTokenType;
//        setChanged();
//    }
//    // Getter method for cboTokenType
//    String getCboTokenType(){
//        return this.cboTokenType;
//    }
//    
//    /**
//     * Getter for property cbmTokenType.
//     * @return Value of property cbmTokenType.
//     */
//    public com.see.truetransact.clientutil.ComboBoxModel getCbmTokenType() {
//        return cbmTokenType;
//    }
//    
//    /**
//     * Setter for property cbmTokenType.
//     * @param cbmTokenType New value of property cbmTokenType.
//     */
//    public void setCbmTokenType(com.see.truetransact.clientutil.ComboBoxModel cbmTokenType) {
//        this.cbmTokenType = cbmTokenType;
//    }
//    
//    // Setter method for txtSeriesNo
//    void setTxtSeriesNo(String txtSeriesNo){
//        this.txtSeriesNo = txtSeriesNo;
//        setChanged();
//    }
//    // Getter method for txtSeriesNo
//    String getTxtSeriesNo(){
//        return this.txtSeriesNo;
//    }
//    
//    // Setter method for txtStartingTokenNo
//    void setTxtStartingTokenNo(String txtStartingTokenNo){
//        this.txtStartingTokenNo = txtStartingTokenNo;
//        setChanged();
//    }
//    // Getter method for txtStartingTokenNo
//    String getTxtStartingTokenNo(){
//        return this.txtStartingTokenNo;
//    }
//    
//    // Setter method for txtEndingTokenNo
//    void setTxtEndingTokenNo(String txtEndingTokenNo){
//        this.txtEndingTokenNo = txtEndingTokenNo;
//        setChanged();
//    }
    // Getter method for txtEndingTokenNo
//    String getTxtEndingTokenNo(){
//        return this.txtEndingTokenNo;
//    }
//    
//    // Setter method for txtNoOfTokens
//    void setTxtNoOfTokens(String txtNoOfTokens){
//        this.txtNoOfTokens = txtNoOfTokens;
//        setChanged();
//    }
//    // Getter method for txtNoOfTokens
//    String getTxtNoOfTokens(){
//        return this.txtNoOfTokens;
//    }
    
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
    }
    
    public void resetStatus(){
        this.setLblStatus(ClientConstants.ACTION_STATUS[0]);
    }
    
    public int getResult(){
        return _result;
    }
    
    /** Creates an Instance of TokenConfigTO Bean and sets its variables with OBMethods */
    private LockerSurrenderTO getLockerSurrenderTO(String command){
        LockerSurrenderTO objLockerSurrenderTO = new LockerSurrenderTO();
//        objTokenConfigTO.setCommand(command);
//        objTokenConfigTO.setConfigId(getTxtTokenConfigId());
//        objTokenConfigTO.setTokenType(CommonUtil.convertObjToStr(getCbmTokenType().getKeyForSelected()));
//        objTokenConfigTO.setSeriesNo(getTxtSeriesNo());
//        objTokenConfigTO.setTokenStartNo(new Double(getTxtStartingTokenNo()));
//        objTokenConfigTO.setTokenEndNo(new Double(getTxtEndingTokenNo()));
//        objTokenConfigTO.setBranchId(TrueTransactMain.BRANCH_ID);
//        if(command.equals(CommonConstants.TOSTATUS_INSERT)){
//            objTokenConfigTO.setCreatedBy(TrueTransactMain.USER_ID);
//            objTokenConfigTO.setCreatedDt(currDt);
//        }
//        objTokenConfigTO.setStatusBy(TrueTransactMain.USER_ID);
//        objTokenConfigTO.setStatusDt(currDt);
        objLockerSurrenderTO.setCommand(command);
        
        objLockerSurrenderTO.setLocNum(getTxtLockerNo());
        objLockerSurrenderTO.setCustId(getLblCustomerIdVal());
        objLockerSurrenderTO.setOptMode(getLblModeOfOpVal());
        objLockerSurrenderTO.setStatusBy(TrueTransactMain.USER_ID);
//        objLockerSurrenderTO.setStatusDt(ClientUtil.getCurrentDateWithTime());
        objLockerSurrenderTO.setBranchID(TrueTransactMain.BRANCH_ID);
        objLockerSurrenderTO.setCreatedBy(TrueTransactMain.USER_ID);
        objLockerSurrenderTO.setAcctName(getLblCustNameVal());
       // objLockerSurrenderTO.setCbCustomer(getCbCustomer());
         //objLockerSurrenderTO.setCbRefundYes(getCbRefundYes());
        objLockerSurrenderTO.setCharges(CommonUtil.convertObjToDouble(getTxtCharges()));
        
         objLockerSurrenderTO.setTxtRefund(CommonUtil.convertObjToDouble(getTxtRefund()));
         
        objLockerSurrenderTO.setProdId(CommonUtil.convertObjToStr(getCbmProdID().getKeyForSelected()));
        objLockerSurrenderTO.setSurDt(currDt);
        objLockerSurrenderTO.setServiceTax(CommonUtil.convertObjToDouble(getServiceTax()));
        //objLockerSurrenderTO.setProdId(CommonUtil.convertObjToStr())

         objLockerSurrenderTO.setLblNewExpDateVal(DateUtil.getDateMMDDYYYY(getLblNewExpDateVal()));
         // objLockerSurrenderTO.setTxtSurDate(CommonUtil.convertObjToStr(getTxtSurDate()));
        objLockerSurrenderTO.setCollectRentMM(getCollectRentMM());
        objLockerSurrenderTO.setCollectRentYYYY(getCollectRentYYYY());
         
          // objLockerSurrenderTO.setBreakOpenDt(getBreakOpenDt());
        //  objLockerSurrenderTO.setLblCurrentDateVal(getLblCurrentDateVal());
      //  objLockerSurrenderTO.setCallingApplicantName(getCallingApplicantName());
        objLockerSurrenderTO.setBforeExpDt("N");
        if(isRenew()){
            objLockerSurrenderTO.setSurRenew("RENEW");
            if(isChkBforeExpDt()){
                objLockerSurrenderTO.setBforeExpDt("Y");
            }
        }
        if(isRentCollection()){
	        objLockerSurrenderTO.setSurRenew("RENTCOLLECTION");
	        //objLockerSurrenderTO.setLblNewExpDateVal(null);
        }
        if (isSurrender()) {
            objLockerSurrenderTO.setSurRenew("SURRENDER");
        }
         if(isBreakOpen()){
            objLockerSurrenderTO.setSurRenew("BREAKOPEN");
             objLockerSurrenderTO.setBreakOpenRemarks(getBreakOpenRemarks());
            if (isCbCustomer() == true) {
          
              objLockerSurrenderTO.setCbCustomer("Y");
          
            } else {
              objLockerSurrenderTO.setCbCustomer("N"); 
            }
        }
        if (getActionType()!=ClientConstants.ACTIONTYPE_NEW) {
            objLockerSurrenderTO.setRemarks(getRemarks());
        }
        objLockerSurrenderTO.setPenalAmount(CommonUtil.convertObjToDouble(getTxtPenalAmt()));
        objLockerSurrenderTO.setActNum(getTxtActNum());
        objLockerSurrenderTO.setChkDefaluterYes(CommonUtil.convertObjToStr(getChkDefaulter())); 
        objLockerSurrenderTO.setChkNoTrans(CommonUtil.convertObjToStr(getChkNoTrans()));
        System.out.println("@@@@@@@@@@@@@objLockerSurrenderTO"+objLockerSurrenderTO);
        return objLockerSurrenderTO;
    }
    
    private void setLockerOptTO(LockerSurrenderTO objLockerSurrenderTO){
        setTxtLockerNo(objLockerSurrenderTO.getLocNum());
        setLblCustNameVal(objLockerSurrenderTO.getAcctName());
       // setCbCustomer(objLockerSurrenderTO.getCbCustomer());
        //  setCbRefundYes(objLockerSurrenderTO.getCbRefundYes());
        setLblCustomerIdVal(objLockerSurrenderTO.getCustId());
        setLblDateVal(CommonUtil.convertObjToStr(objLockerSurrenderTO.getSurDt()));
        setLblModeOfOpVal(objLockerSurrenderTO.getOptMode());
        setTxtCharges(CommonUtil.convertObjToStr(objLockerSurrenderTO.getCharges()));
        setTxtPenalAmt(CommonUtil.convertObjToStr(objLockerSurrenderTO.getPenalAmount()));
         setTxtRefund(CommonUtil.convertObjToStr(objLockerSurrenderTO.getTxtRefund()));
        setServiceTax(CommonUtil.convertObjToStr(objLockerSurrenderTO.getServiceTax()));
        //        setTxtTokenConfigId(objTokenConfigTO.getConfigId());
        setCboProdID(CommonUtil.convertObjToStr(getCbmProdID().getDataForKey(objLockerSurrenderTO.getProdId())));
        setCollectRentMM(CommonUtil.convertObjToStr(objLockerSurrenderTO.getCollectRentMM()));
        setCollectRentYYYY(CommonUtil.convertObjToStr(objLockerSurrenderTO.getcollectRentYYYY()));
        setProdID(CommonUtil.convertObjToStr(objLockerSurrenderTO.getProdId()));
        setLblNewExpDateVal(CommonUtil.convertObjToStr(objLockerSurrenderTO.getLblNewExpDateVal()));        
        if (objLockerSurrenderTO.getBforeExpDt() != null && objLockerSurrenderTO.getBforeExpDt().equals("Y")) {
            setChkBforeExpDt(true);
        } else {
            setChkBforeExpDt(false);
        }
    //  setTxtSurDate(CommonUtil.convertObjToStr(objLockerSurrenderTO.getTxtSurDate()));
       
          // setBreakOpenDt(CommonUtil.convertObjToStr(objLockerSurrenderTO.getBreakOpenDt()));
           //setLblNewDateExp(CommonUtil.convertObjToStr(objLockerSurrenderTO.getLblNewExpDate()));
        
        //setLblCurrentDateVal(CommonUtil.convertObjToStr(objLockerSurrenderTO.getLblCurrentDateVal()));
       // setCallingApplicantName(CommonUtil.convertObjToStr(objLockerSurrenderTO.getCallingApplicantName()));
        
//        setTxtSeriesNo(objTokenConfigTO.getSeriesNo());
//        setTxtStartingTokenNo(CommonUtil.convertObjToStr(objTokenConfigTO.getTokenStartNo()));
//        setTxtEndingTokenNo(CommonUtil.convertObjToStr(objTokenConfigTO.getTokenEndNo()));
//        setStatusBy(objTokenConfigTO.getStatusBy());
//         setAuthorizeStatus(objTokenConfigTO.getAuthorizeStatus());
        if(objLockerSurrenderTO.getSurRenew()!=null){
            if(objLockerSurrenderTO.getSurRenew().equalsIgnoreCase("RENEW")){
                setRenew(true);
                setSurrender(false);
                setBreakOpen(false);
                setRentCollection(false);
            }
            if(objLockerSurrenderTO.getSurRenew().equalsIgnoreCase("RENTCOLLECTION")){
                setRenew(false);
                setSurrender(false);
                setBreakOpen(false);
                setRentCollection(true);
            }
                 
          if(objLockerSurrenderTO.getSurRenew().equalsIgnoreCase("BREAKOPEN")){
               setRenew(false);
                setSurrender(false);
                setBreakOpen(true);
                setRentCollection(false);
               setBreakOpenRemarks(CommonUtil.convertObjToStr(objLockerSurrenderTO.getBreakOpenRemarks()));  
            }
            
  if(objLockerSurrenderTO.getSurRenew().equalsIgnoreCase("SURRENDER")){
                 setRenew(false);
                setSurrender(true);
                setBreakOpen(false);
                setRentCollection(false);
          }
        }
       
       if(objLockerSurrenderTO.getCbCustomer()!=null){
            if (objLockerSurrenderTO.getCbCustomer().equalsIgnoreCase("Y")) {
            setCbCustomer(true);
            
            } else {
              setCbCustomer(false);
            }
        
       
       }
       setTxtActNum(objLockerSurrenderTO.getActNum()); 
        System.out.println("$$$$$$$$$$$$"+getLblCustNameVal()+"&&&&&&&&&&"+getLblDateVal());
        notifyObservers();
    }
    
    /** Resets all the UI Fields */
    public void resetForm(){
        setLblCustNameVal("");
        setLblCustomerIdVal("");
        setTxtLockerNo("");
        setLblLockerOutDtVal("");
        setLblModeOfOpVal("");
        setLblDateVal("");
         setLblNewExpDateVal("");
 
        setRenew(false);
        setBreakOpen(false);
        setSurrender(false);
        setRentCollection(false);
        setTxtActNum("");
        setChkDefaulter("");
        setChkNoTrans("");
        setServiceTax_Map(null);
        setLblServiceTaxval("");
        notifyObservers();
    }
    
    /* Executes Query using the TO object */
    public void execute(String command) {
        try {
            HashMap term = new HashMap();
            term.put(CommonConstants.MODULE, getModule());
            term.put(CommonConstants.SCREEN, getScreen());
            term.put(CommonConstants.USER_ID, TrueTransactMain.USER_ID);
            term.put(CommonConstants.BRANCH_ID, TrueTransactMain.BRANCH_ID);
            term.put("LockerOperationTO", getLockerSurrenderTO(command));
            if (getActionType() == ClientConstants.ACTIONTYPE_NEW) {
                if (getServiceTax_Map() != null && getServiceTax_Map().size() > 0) {
                    term.put("serviceTaxDetails", getServiceTax_Map());
                    term.put("serviceTaxDetailsTO", setServiceTaxDetails());
                }
            }
            HashMap proxyReturnMap = proxy.execute(term, map);
            setProxyReturnMap(proxyReturnMap);
            setResult(getActionType());
            if (proxyReturnMap != null && proxyReturnMap.containsKey("LOCKER_OPT_ID"))  {
            ClientUtil.showMessageWindow("Operation ID : "+CommonUtil.convertObjToStr(proxyReturnMap.get("LOCKER_OPT_ID"))+"\n"
            /* +"Instrument No. : "+CommonUtil.convertObjToStr(proxyResultMap.get("INST_NO1")) + "-" + CommonUtil.convertObjToStr(proxyResultMap.get("INST_NO2"))*/);
        }
        } catch (Exception e) {
            setResult(ClientConstants.ACTIONTYPE_FAILED);
            parseException.logException(e,true);
        }
    }
    
    public void doAction(String command) {
        try {
//            if( actionType != ClientConstants.ACTIONTYPE_CANCEL ){
                //If actionType has got propervalue then doActionPerform, else throw error
//                if( getCommand() != null || getAuthorizeMap() != null){
                    doActionPerform(command);
//                }
//                else{
//                    final RemittancePaymentRB objRemittancePaymentRB = new RemittancePaymentRB();
//                    throw new TTException(objRemittancePaymentRB.getString("TOCommandError"));
//                }
//            }
        } catch (Exception e) {
            setResult(ClientConstants.ACTIONTYPE_FAILED);
            parseException.logException(e,true);
        }
    }
    private void doActionPerform(String command) throws Exception{
        final HashMap data = new HashMap();
        data.put("MODE", command);
        if(getActionType() == ClientConstants.ACTIONTYPE_AUTHORIZE || getActionType() == ClientConstants.ACTIONTYPE_REJECT
        || getActionType() == ClientConstants.ACTIONTYPE_EXCEPTION){
            data.put(CommonConstants.AUTHORIZEMAP, getAuthorizeMap());
            data.put(CommonConstants.USER_ID, _authorizeMap.get(CommonConstants.USER_ID));
            if (transactionDetailsTO == null) {
                transactionDetailsTO = new LinkedHashMap();
            }
            transactionDetailsTO.put(NOT_DELETED_TRANS_TOs,allowedTransactionDetailsTO);
            allowedTransactionDetailsTO = null;
            
            data.put("TransactionTO",transactionDetailsTO);
        }else {
            final LockerSurrenderTO objLockerSurrenderTO = getLockerSurrenderTO(command);
            //            objRemittancePaymentTO.setCommand(getCommand());
            if (transactionDetailsTO == null) {
                transactionDetailsTO = new LinkedHashMap();
            }
            
            if (deletedTransactionDetailsTO != null) {
                transactionDetailsTO.put(DELETED_TRANS_TOs,deletedTransactionDetailsTO);
                deletedTransactionDetailsTO = null;
            }
            
            transactionDetailsTO.put(NOT_DELETED_TRANS_TOs,allowedTransactionDetailsTO);
            allowedTransactionDetailsTO = null;
            
            data.put("TransactionTO",transactionDetailsTO);
            
            data.put("LockerSurrenderTO",objLockerSurrenderTO);
            if (getActionType() == ClientConstants.ACTIONTYPE_NEW) {
                if (getServiceTax_Map() != null && getServiceTax_Map().size() > 0) {
                    data.put("serviceTaxDetails", getServiceTax_Map());
                    data.put("serviceTaxDetailsTO", setServiceTaxDetails());
                }
            }
            data.put(CommonConstants.MODULE, getModule());
            data.put(CommonConstants.SCREEN, getScreen());
            System.out.println("DATA## : " + data);
        }
       
        System.out.println("DATA###### : " + data);
        HashMap proxyResultMap = proxy.execute(data, map);
        if (proxyResultMap != null && proxyResultMap.containsKey(CommonConstants.TRANS_ID)) {
            ClientUtil.showMessageWindow("Locker Transaction ID. : " + CommonUtil.convertObjToStr(proxyResultMap.get(CommonConstants.TRANS_ID)));
        }
        setProxyReturnMap(proxyResultMap);
        setResult(_actionType);
    }
    
    public void populateSelectedRowAct(int row){
        ArrayList data = (ArrayList)tbmInstructions2.getDataArrayList().get(row);
        //        return data;
        setTxtCustId(CommonUtil.convertObjToStr(data.get(0)));
        setTxtName(CommonUtil.convertObjToStr(data.get(1)));
        setTxtPassword(CommonUtil.convertObjToStr(data.get(2)));
//        ttNotifyObservers();
//        setTxtPassword(CommonUtil.convertObjToStr(data.get(1)));
        
    }
     
     public void setTableValueAt(int row){
         deletingExists = true;
        final ArrayList data = tbmInstructions2.getDataArrayList();
        if(existingData!=null){
            existingData.add(data.get(row));
        }
        //        if(!getCbmStdInstruction().getKeyForSelected().equals("OTHERS")){
        tbmInstructions2.setValueAt(getTxtCustId(),row,0);
        //            tbmInstructions.setValueAt(getCboStdInstruction(),row,1);
        tbmInstructions2.setValueAt(getTxtName(),row,1);
        tbmInstructions2.setValueAt(getTxtPassword(),row,2);
        //        }else{
        //            tbmInstructions.setValueAt(getTxtStdInstruction(),row,1);
        //        }
        //         setTxtTotalAmt(calculateTotalAmount(data));
        //         setTxttotalServTax(calculateTotalServTaxAmount(data));
      }

      public void resetInstTbl(){
        tbmInstructions2.setDataArrayList(null, tblHeadings3);
    }
      
      public int addTblInstructionData(){
          int optionSelected = -1;
          String columnData = new String();
          //        getCbmStdInstruction().setSelectedItem(getCboStdInstruction());
          //        if(!getCbmStdInstruction().getKeyForSelected().equals("OTHERS")){
          //            columnData = CommonUtil.convertObjToStr(getCbmStdInstruction().getKeyForSelected());
          //            columnData = CommonUtil.convertObjToStr(getCboStdInstruction());
          columnData = CommonUtil.convertObjToStr(getTxtCustId());
          columnData = CommonUtil.convertObjToStr(getTxtName());
          //        }else{
          //            columnData = getTxtStdInstruction();
          columnData = CommonUtil.convertObjToStr(getTxtPassword());
          //            columnData = CommonUtil.convertObjToStr(getTxtServiceTax());
          //        }
        try{
            if (newInstructionRow == null) {
                newInstructionRow = new ArrayList();
            }
            newInstructionRow.add(new Integer(tbmInstructions2.getRowCount()+1));
//            if(!getCbmStdInstruction().getKeyForSelected().equals("OTHERS")){
//////                newInstructionRow.add(getCbmStdInstruction().getKeyForSelected());
//                newInstructionRow.add(getCboStdInstruction());
                newInstructionRow.add(getTxtCustId());
//                newInstructionRow.add(getTxtServiceTax());
//            }else{
//                newInstructionRow.add(getTxtStdInstruction());
//                newInstructionRow.add(getTxtAmount());
//                newInstructionRow.add(getTxtServiceTax());
//            }
            ArrayList data = tbmInstructions2.getDataArrayList();
            final int dataSize = data.size();
            boolean exist = false;
            for (int i=0;i<dataSize;i++){
                if (CommonUtil.convertObjToStr(((ArrayList)data.get(i)).get(1)).equalsIgnoreCase(columnData)){
                    // Checking whether existing Data is equal new data entered by the user
                    exist = true;
                    String[] options = {resourceBundle.getString("cDialogYes"),resourceBundle.getString("cDialogNo"),resourceBundle.getString("cDialogCancel")};
                    optionSelected = COptionPane.showOptionDialog(null, resourceBundle.getString("existanceWarning"), CommonConstants.WARNINGTITLE,
                    COptionPane.YES_NO_CANCEL_OPTION, COptionPane.WARNING_MESSAGE, null, options, options[0]);
                    if (optionSelected == 0){
                        // Newly Entered data already exists and the user wants to modify it
                        updateTbmInstructions(i);
                        //doUpdateData(i);
                    }
                    break;
                }
                
            }
            if (!exist){
                //The condition that the Entered data is not in the table
                doNewData();
                insertNewData();
            }
//            setTxtTotalAmt(calculateTotalAmount(data));
//            setTxttotalServTax(calculateTotalServTaxAmount(data));
            newInstructionRow = null;
        }catch(Exception e){
            parseException.logException(e,true);
        }
        return optionSelected;
    }
      
       private void updateTbmInstructions(int row) throws Exception{
//        if(!getCbmStdInstruction().getKeyForSelected().equals("OTHERS")){
//            tbmInstructions.setValueAt(getCbmStdInstruction().getKeyForSelected(), row, 1);
//            tbmInstructions.setValueAt(getCboStdInstruction(), row, 1);
//        }else{
            tbmInstructions2.setValueAt(getTxtCustId(), row, 1);
//        }
        
    }
       
       private void doNewData(){
        newData.add(newInstructionRow);
    }
      private void insertNewData() throws Exception{
        //final TerminalTO objTerminalTO = new TerminalTO();
        int row = tbmInstructions2.getRowCount();
        tbmInstructions2.insertRow(row,newInstructionRow);
    }
      public void ttNotifyObservers(){
        this.notifyObservers();
        setChanged();
    }
    /*Populates the TO object by executing a Query */
      public void populateData(HashMap whereMap) {
          HashMap mapData=null;
          try {
              mapData = proxy.executeQuery(whereMap, map);
              System.out.println("the map data is"+mapData);
              if (!CommonUtil.convertObjToStr(whereMap.get("VIEW_TYPE")).equals("LockerNo")&& !CommonUtil.convertObjToStr(whereMap.get("VIEW_TYPE")).equals("LockerListClosednDue")) {
                  LockerSurrenderTO objLockerSurrenderTO =
                  (LockerSurrenderTO) ((List) mapData.get("LockerSurTO")).get(0);
                    System.out.println("the map data is"+mapData);
                  setLockerOptTO(objLockerSurrenderTO);
                  
                  
                  com.see.truetransact.transferobject.locker.lockerissue.LockerIssueTO objLockerIssueTO =
                (com.see.truetransact.transferobject.locker.lockerissue.LockerIssueTO) ((List) mapData.get("LockerTO")).get(0);
                //  whereMap.put("LockerTO",objLockerIssueTO);
                 String dat=CommonUtil.convertObjToStr( objLockerIssueTO.getExpDt());
                 setDate(dat);
                  
                  if(mapData.containsKey("TransactionTO")){
                      transactionOB.setDetails((List)mapData.get("TransactionTO"));
                      setAllowedTransactionDetailsTO(transactionOB.getAllowedTransactionDetailsTO());
                      
                     //panTransaction.setVisible(true);
                  }
              } else {
//                  System.out.println("#@$#$!@! mapData :"+mapData);
                  if (mapData.containsKey("SURRENDER_AUTHORIZATION_STATUS") && mapData.get("SURRENDER_AUTHORIZATION_STATUS")!=null) {
                      whereMap.put("SURRENDER_AUTHORIZATION_STATUS",mapData.get("SURRENDER_AUTHORIZATION_STATUS"));
                  }
              }
          } catch( Exception e ) {
              setResult(ClientConstants.ACTIONTYPE_FAILED);
              parseException.logException(e,true);
              
          }
      }
    
    /**
     * Getter for property lblDateVal.
     * @return Value of property lblDateVal.
     */
    public java.lang.String getLblDateVal() {
        return lblDateVal;
    }
    
    /**
     * Setter for property lblDateVal.
     * @param lblDateVal New value of property lblDateVal.
     */
    public void setLblDateVal(java.lang.String lblDateVal) {
        this.lblDateVal = lblDateVal;
    }
    
    /**
     * Getter for property lblLockerOutDtVal.
     * @return Value of property lblLockerOutDtVal.
     */
    public java.lang.String getLblLockerOutDtVal() {
        return lblLockerOutDtVal;
    }
    
    /**
     * Setter for property lblLockerOutDtVal.
     * @param lblLockerOutDtVal New value of property lblLockerOutDtVal.
     */
    public void setLblLockerOutDtVal(java.lang.String lblLockerOutDtVal) {
        this.lblLockerOutDtVal = lblLockerOutDtVal;
    }
    
    /**
     * Getter for property lblCustNameVal.
     * @return Value of property lblCustNameVal.
     */
    public java.lang.String getLblCustNameVal() {
        return lblCustNameVal;
    }
    
    /**
     * Setter for property lblCustNameVal.
     * @param lblCustNameVal New value of property lblCustNameVal.
     */
    public void setLblCustNameVal(java.lang.String lblCustNameVal) {
        this.lblCustNameVal = lblCustNameVal;
    }
      public java.lang.String getDate() {
        return date;
    }
    
    /**
     * Setter for property lblCustNameVal.
     * @param lblCustNameVal New value of property lblCustNameVal.
     */
    public void setDate(java.lang.String date) {
        this.date = date;
    }
     public boolean isCbCustomer() {
        return cbCustomer;
    }
    
    /**
     * Setter for property lblCustNameVal.
     * @param lblCustNameVal New value of property lblCustNameVal.
     */
    public void setCbCustomer(boolean cbCustomer) {
        this.cbCustomer = cbCustomer;
    }
    
    
     
    /**
     * Getter for property lblCustomerIdVal.
     * @return Value of property lblCustomerIdVal.
     */
    public java.lang.String getLblCustomerIdVal() {
        return lblCustomerIdVal;
    }
    
    /**
     * Setter for property lblCustomerIdVal.
     * @param lblCustomerIdVal New value of property lblCustomerIdVal.
     */
    public void setLblCustomerIdVal(java.lang.String lblCustomerIdVal) {
        this.lblCustomerIdVal = lblCustomerIdVal;
    }
    
    /**
     * Getter for property lblModeOfOpVal.
     * @return Value of property lblModeOfOpVal.
     */
    public java.lang.String getLblModeOfOpVal() {
        return lblModeOfOpVal;
    }
    
    /**
     * Setter for property lblModeOfOpVal.
     * @param lblModeOfOpVal New value of property lblModeOfOpVal.
     */
    public void setLblModeOfOpVal(java.lang.String lblModeOfOpVal) {
        this.lblModeOfOpVal = lblModeOfOpVal;
    }
    
    /**
     * Getter for property txtLockerNo.
     * @return Value of property txtLockerNo.
     */
    public java.lang.String getTxtLockerNo() {
        return txtLockerNo;
    }
    
    /**
     * Setter for property txtLockerNo.
     * @param txtLockerNo New value of property txtLockerNo.
     */
    public void setTxtLockerNo(java.lang.String txtLockerNo) {
        this.txtLockerNo = txtLockerNo;
    }
    
    /**
     * Getter for property txtCustId.
     * @return Value of property txtCustId.
     */
    public java.lang.String getTxtCustId() {
        return txtCustId;
    }
    
    /**
     * Setter for property txtCustId.
     * @param txtCustId New value of property txtCustId.
     */
    public void setTxtCustId(java.lang.String txtCustId) {
        this.txtCustId = txtCustId;
    }
    
    /**
     * Getter for property txtName.
     * @return Value of property txtName.
     */
    public java.lang.String getTxtName() {
        return txtName;
    }
    
    /**
     * Setter for property txtName.
     * @param txtName New value of property txtName.
     */
    public void setTxtName(java.lang.String txtName) {
        this.txtName = txtName;
    }
    
    /**
     * Getter for property txtPassword.
     * @return Value of property txtPassword.
     */
    public java.lang.String getTxtPassword() {
        return txtPassword;
    }
    
    /**
     * Setter for property txtPassword.
     * @param txtPassword New value of property txtPassword.
     */
    public void setTxtPassword(java.lang.String txtPassword) {
        this.txtPassword = txtPassword;
    }
    
    /**
     * Getter for property tbmInstructions2.
     * @return Value of property tbmInstructions2.
     */
    public com.see.truetransact.clientutil.EnhancedTableModel getTbmInstructions2() {
        return tbmInstructions2;
    }
    
    /**
     * Setter for property tbmInstructions2.
     * @param tbmInstructions2 New value of property tbmInstructions2.
     */
    public void setTbmInstructions2(com.see.truetransact.clientutil.EnhancedTableModel tbmInstructions2) {
        this.tbmInstructions2 = tbmInstructions2;
    }
    
    /**
     * Getter for property optID.
     * @return Value of property optID.
     */
    public java.lang.String getOptID() {
        return optID;
    }
    
    /**
     * Setter for property optID.
     * @param optID New value of property optID.
     */
    public void setOptID(java.lang.String optID) {
        this.optID = optID;
    }
    
    /**
     * Getter for property txtCharges.
     * @return Value of property txtCharges.
     */
    public java.lang.String getTxtCharges() {
        return txtCharges;
    }
    
    /**
     * Setter for property txtCharges.
     * @param txtCharges New value of property txtCharges.
     */
    public void setTxtCharges(java.lang.String txtCharges) {
        this.txtCharges = txtCharges;
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
    public void setAuthorizeMap(java.util.HashMap _authorizeMap) {
        this._authorizeMap = _authorizeMap;
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
     * Getter for property cboProdID.
     * @return Value of property cboProdID.
     */
    public java.lang.String getCboProdID() {
        return cboProdID;
    }
    
     void setCollectRentMM(String collectRentMM){
        this.collectRentMM = collectRentMM;
        setChanged();
    }
                String getCollectRentMM(){
        return collectRentMM;
    }
 void setCollectRentYYYY(String collectRentYYYY){
        this.collectRentYYYY = collectRentYYYY;
        setChanged();
    }
      String getCollectRentYYYY(){
        return collectRentYYYY ; 
    }
    
      
      
    /**
     * Setter for property cboProdID.
     * @param cboProdID New value of property cboProdID.
     */
    public void setCboProdID(java.lang.String cboProdID) {
        this.cboProdID = cboProdID;
    }
    
    /**
     * Getter for property cbmProdID.
     * @return Value of property cbmProdID.
     */
    public com.see.truetransact.clientutil.ComboBoxModel getCbmProdID() {
        return cbmProdID;
    }
    
    /**
     * Setter for property cbmProdID.
     * @param cbmProdID New value of property cbmProdID.
     */
    public void setCbmProdID(com.see.truetransact.clientutil.ComboBoxModel cbmProdID) {
        this.cbmProdID = cbmProdID;
    }
    
    /**
     * Getter for property renew.
     * @return Value of property renew.
     */
    public boolean isRenew() {
        return renew;
    }
    
    /**
     * Setter for property renew.
     * @param renew New value of property renew.
     */
    public void setRenew(boolean renew) {
        this.renew = renew;
    }
    
    /**
     * Getter for property surrender.
     * @return Value of property surrender.
     */
    public boolean isSurrender() {
        return surrender;
    }
    
    /**
     * Setter for property surrender.
     * @param surrender New value of property surrender.
     */
    public void setSurrender(boolean surrender) {
        this.surrender = surrender;
    }
    public boolean isBreakOpen() {
        return breakOpen;
    }
    
    /**
     * Setter for property surrender.
     * @param surrender New value of property surrender.
     */
    public void setBreakOpen(boolean breakOpen) {
        this.breakOpen = breakOpen;
    }
    
    /**
     * Getter for property serviceTax.
     * @return Value of property serviceTax.
     */
    public java.lang.String getServiceTax() {
        return serviceTax;
    }
    
    /**
     * Setter for property serviceTax.
     * @param serviceTax New value of property serviceTax.
     */
    public void setLblNewExpDateVal(java.lang.String lblNewExpDateVal) {
        this. lblNewExpDateVal= lblNewExpDateVal;
    }
    
      public java.lang.String getLblNewExpDateVal () {
        return  lblNewExpDateVal;
    }
     
      public void setLblNewExpDate(java.lang.String lblNewExpDate) {
        this. lblNewExpDate= lblNewExpDate;
    }
        public void setBreakOpenRemarks(java.lang.String breakOpenRemarks) {
        this. breakOpenRemarks= breakOpenRemarks;
    }
    
      public java.lang.String getBreakOpenRemarks () {
        return breakOpenRemarks;
    }
     
    
      public java.lang.String getLblNewExpDate () {
        return  lblNewExpDate;
    }
      
    /**
     * Setter for property serviceTax.
     * @param serviceTax New value of property serviceTax.
     */
    public void setServiceTax(java.lang.String serviceTax) {
        this.serviceTax = serviceTax;
    }
    
    /**
     * Getter for property remarks.
     * @return Value of property remarks.
     */
    public java.lang.String getRemarks() {
        return remarks;
    }
    
    /**
     * Setter for property remarks.
     * @param remarks New value of property remarks.
     */
    public void setRemarks(java.lang.String remarks) {
        this.remarks = remarks;
    }
       public java.lang.String getBreakOpenDt() {
        return breakOpenDt;
    }
    
    /**
     * Setter for property remarks.
     * @param remarks New value of property remarks.
     */
    public void setBreakOpenDt(java.lang.String breakOpenDt) {
        this.breakOpenDt=breakOpenDt;
    }
    
     public java.lang.String getProdID() {
        return prodID;
    }
    
    /** Setter for property prodID.
     * @param prodID New value of property prodID.
     *
     */
    public void setProdID(java.lang.String prodID) {
        this.prodID = prodID;
    }
    
      public java.lang.String getTxtRefund() {
        return txtRefund;
    }
    
    /** Setter for property prodID.
     * @param prodID New value of property prodID.
     *
     */
    public void setTxtRefund(java.lang.String txtRefund) {
        this.txtRefund = txtRefund;
    }
    
      public java.lang.String getCallingApplicantName() {
        return callingApplicantName;
    }
    
    /**
     * Setter for property callingApplicantName.
     * @param callingApplicantName New value of property callingApplicantName.
     */
    public void setCallingApplicantName(java.lang.String callingApplicantName) {
        this.callingApplicantName = callingApplicantName;
    }
    
    /*Checks for the duplication of TokenType if so retuns a boolean type vairable as true */
//    public boolean isTokenTypeExists(String tokenType){
//        boolean exists = false;
//        HashMap resultMap = null;
//        HashMap where = new HashMap();
//        where.put("BRANCH_ID", TrueTransactMain.BRANCH_ID);
//        ArrayList resultList =(ArrayList) ClientUtil.executeQuery("getSelectTokenType", where);
//        where = null;
//        if(resultList.size() > 0){
//            for(int i=0; i<resultList.size(); i++){
//                resultMap= (HashMap)resultList.get(i);
//                if(resultMap.get("TOKEN_TYPE").equals(tokenType)){
//                    exists = true;
//                    break;
//                }
//            }
//        }
//        return exists;
//    }
    
    /*Checks for the duplication of SeriesNo if so retuns a boolean type vairable as true */
//    public boolean isSeriesNoExists(String tokenType,String seriesNo){
//        boolean exists = false;
//        HashMap resultMap = null;
//        HashMap where = new HashMap();
//        where.put("TOKEN_TYPE", tokenType);
//        where.put("BRANCH_ID", TrueTransactMain.BRANCH_ID);
//        ArrayList resultList =(ArrayList) ClientUtil.executeQuery("getSelectTokenSeries", where);
//        where = null;
//        if(resultList.size() > 0){
//            for(int i=0; i<resultList.size(); i++){
//                resultMap= (HashMap)resultList.get(i);
//                if(resultMap.get("SERIES_NO").equals(seriesNo)){
//                    exists = true;
//                    break;
//                }
//            }
//        }
//        return exists;
//    }

    
	public boolean isRentCollection() {
        return rentCollection;
    }

    public void setRentCollection(boolean rentCollection) {
        this.rentCollection = rentCollection;
    }

    public String getTxtActNum() {
        return txtActNum;
    }

    public void setTxtActNum(String txtActNum) {
        this.txtActNum = txtActNum;
    }

    public String getChkDefaulter() {
        return chkDefaulter;
    }

    public void setChkDefaulter(String chkDefaulter) {
        this.chkDefaulter = chkDefaulter;
    }

    public String getChkNoTrans() {
        return chkNoTrans;
    }

    public void setChkNoTrans(String chkNoTrans) {
        this.chkNoTrans = chkNoTrans;
    }
    public HashMap checkServiceTaxApplicable(String accheadId) {
        String checkFlag = "N";
        HashMap checkForTaxMap = new HashMap();
        if (TrueTransactMain.SERVICE_TAX_REQ.equals("Y")) {
            HashMap whereMap = new HashMap();
            whereMap.put("AC_HD_ID", accheadId);
            List accHeadList = ClientUtil.executeQuery("getCheckServiceTaxApplicableForShare", whereMap);                  
            if (accHeadList != null && accHeadList.size() > 0) {
                HashMap accHeadMap = (HashMap) accHeadList.get(0);
                if (accHeadMap != null && accHeadMap.containsKey("SERVICE_TAX_APPLICABLE")&& accHeadMap.containsKey("SERVICE_TAX_ID")) {
                    checkFlag = CommonUtil.convertObjToStr(accHeadMap.get("SERVICE_TAX_APPLICABLE"));
                    checkForTaxMap.put("SERVICE_TAX_APPLICABLE",accHeadMap.get("SERVICE_TAX_APPLICABLE"));
                    checkForTaxMap.put("SERVICE_TAX_ID",accHeadMap.get("SERVICE_TAX_ID"));
                }
            }
        }
        return checkForTaxMap;
    }
    
    public ServiceTaxDetailsTO setServiceTaxDetails() {
        final ServiceTaxDetailsTO objservicetaxDetTo = new ServiceTaxDetailsTO();
        try {

            objservicetaxDetTo.setBranchID(ProxyParameters.BRANCH_ID);
            objservicetaxDetTo.setCommand("INSERT");
            objservicetaxDetTo.setStatus("CREATED");
            objservicetaxDetTo.setStatusBy(TrueTransactMain.USER_ID);
            objservicetaxDetTo.setAcct_Num(getTxtLockerNo());
            objservicetaxDetTo.setParticulars("Loan Closing");

            if (serviceTax_Map != null && serviceTax_Map.containsKey("SERVICE_TAX")) {
                objservicetaxDetTo.setServiceTaxAmt(CommonUtil.convertObjToDouble(serviceTax_Map.get("SERVICE_TAX")));
            }
            if (serviceTax_Map != null && serviceTax_Map.containsKey("EDUCATION_CESS")) {
                objservicetaxDetTo.setEducationCess(CommonUtil.convertObjToDouble(serviceTax_Map.get("EDUCATION_CESS")));
            }
            if (serviceTax_Map != null && serviceTax_Map.containsKey("HIGHER_EDU_CESS")) {
                objservicetaxDetTo.setHigherCess(CommonUtil.convertObjToDouble(serviceTax_Map.get("HIGHER_EDU_CESS")));
            }
            if (serviceTax_Map != null && serviceTax_Map.containsKey(ServiceTaxCalculation.SWACHH_CESS)) {
                objservicetaxDetTo.setSwachhCess(CommonUtil.convertObjToDouble(serviceTax_Map.get(ServiceTaxCalculation.SWACHH_CESS)));
            }
            if (serviceTax_Map != null && serviceTax_Map.containsKey(ServiceTaxCalculation.KRISHIKALYAN_CESS)) {
                objservicetaxDetTo.setKrishiKalyan(CommonUtil.convertObjToDouble(serviceTax_Map.get(ServiceTaxCalculation.KRISHIKALYAN_CESS)));
            }
            if (serviceTax_Map != null && serviceTax_Map.containsKey("TOT_TAX_AMT")) {
                objservicetaxDetTo.setTotalTaxAmt(CommonUtil.convertObjToDouble(serviceTax_Map.get("TOT_TAX_AMT")));
            }
            double roudVal = objservicetaxDetTo.getTotalTaxAmt() - (objservicetaxDetTo.getServiceTaxAmt() + objservicetaxDetTo.getEducationCess() + objservicetaxDetTo.getHigherCess() + objservicetaxDetTo.getSwachhCess() + objservicetaxDetTo.getKrishiKalyan());
            ServiceTaxCalculation serviceTaxObj = new ServiceTaxCalculation();
            objservicetaxDetTo.setRoundVal(CommonUtil.convertObjToStr(serviceTaxObj.roundOffAmtForRoundVal(roudVal)));
            objservicetaxDetTo.setStatusDt(currDt);
            objservicetaxDetTo.setTrans_type("C");
           
                objservicetaxDetTo.setCreatedBy(TrueTransactMain.USER_ID);
                objservicetaxDetTo.setCreatedDt(currDt);
            
        } catch (Exception e) {
            log.info("Error In setLoanApplicationData()");
            e.printStackTrace();
        }
        return objservicetaxDetTo;

    }
    
}