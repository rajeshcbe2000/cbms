/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition.
 * 
 *
 * AccountMaintenanceOB.java
 *
 * Created on August 13, 2003, 2:49 PM
 */

package com.see.truetransact.ui.generalledger;

/**
 *
 * @author  Bala
 * @modified by Annamalai
 */
import java.util.List;
import java.util.HashMap;
import java.util.ArrayList;
import com.see.truetransact.uicomponent.CObservable;
import com.see.truetransact.transferobject.generalledger.AccountCreationTO;
import com.see.truetransact.clientutil.ComboBoxModel;
import com.see.truetransact.clientutil.ClientUtil;
import com.see.truetransact.clientutil.ClientConstants;
import com.see.truetransact.commonutil.DateUtil;
import com.see.truetransact.commonutil.CommonUtil;
import com.see.truetransact.clientproxy.ProxyFactory;
import com.see.truetransact.commonutil.CommonConstants;
import com.see.truetransact.transferobject.generalledger.AccountMaintenanceTO;
import com.see.truetransact.transferobject.TOHeader;
import com.see.truetransact.commonutil.TTException;
import com.see.truetransact.clientexception.ClientParseException;
import com.see.truetransact.clientproxy.ProxyParameters;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.DefaultMutableTreeNode;
import org.apache.log4j.Logger;
import java.util.Enumeration;
public class AccountMaintenanceOB extends CObservable {
    private String _txtAccountHead = "";
    //    private boolean _rdoStatus_Implemented = false;
    //    private boolean _rdoStatus_NonImplemented = false;
    private String _cboContraHead = "";
    /*private boolean _chkCreditClearing = true;
    private boolean _chkCreditTransfer = true;
    private boolean _chkCreditCash = true;
    private boolean _chkDebitClearing = true;
    private boolean _chkDebitTransfer = true;
    private boolean _chkDebitCash = true;*/
    
    final String NO = "N";
    final String YES = "Y";
    private HashMap _lookUpHash;
    private boolean _chkCreditClearing = false;
    private boolean _chkCreditTransfer = false;
    private boolean _chkCreditCash = false;
    private boolean _chkDebitClearing = false;
    private boolean _chkDebitTransfer = false;
    private boolean _chkDebitCash = false;
    
    private String _cboTransactionPosting = "";
    private String _cboPostingMode = "";
    private String _cboGLBalanceType = "";
    private boolean _chkReconcilliationAllowed = false;
    private String _txtBalanceInGL = "";
    private String _txtReconcillationAcHd = "";
    private boolean _rdoFloatAccount_Yes = false;
    private boolean _rdoFloatAccount_No = false;
    private boolean _chkHdOfficeAc = false;
    
    private boolean rdoNegValue_No = false;
    private boolean rdoNegValue_Yes = false;
    
    private String _lblAccountOpenedOnDisplay = "";
    private String _lblAccountClosedOnDisplay = "";
    private String _lblLastTransactionDateDisplay = "";
    private String _lblFirstTransactionDateDisplay = "";
    private String _lblAccountTypeDisplay = "";
    private String _lblMajorHeadDisplay = "";
    private String _lblSubHeadDisplay = "";
    private String _lblAccountHeadCodeDisplay = "";
    private String _lblAccountHeadDescDisplay = "";
    private String _lblStatus = ClientConstants.ACTION_STATUS[ClientConstants.ACTIONTYPE_CANCEL];
    private boolean chkBalCheck = false;
    private boolean chkAllowCustomerEntry=false;
    private boolean cbServiceTax=false;
    private ComboBoxModel _cbmAccountType;
    private ComboBoxModel _cbmMajorHead;
    private ComboBoxModel _cbmSubHead;
    private String _cboAccountType = "";
    private String _cboMajorHead = "";
    private String _cboSubHead = "";
    private String _txtAccountHeadCode = "";
    private String _txtAccountHeadDesc = "";
    private String rdoReceiveDayBookDetail = "";
    private String rdoPayDayBookDetail = "";
    private String txtAccountHeadOrder = "";
    private HashMap _operationMap;
    private ProxyFactory _proxy;
     
    private HashMap _keyValue;
    private ArrayList _key;
    private ArrayList _value;
    
    private int _actionType;
    private int _result;
    private final String STARTING_NUMBER = "001";
//    final AccountMaintenanceRB objAccountMaintenanceRB = new AccountMaintenanceRB();
    java.util.ResourceBundle objAccountMaintenanceRB = java.util.ResourceBundle.getBundle("com.see.truetransact.ui.generalledger.AccountMaintenanceRB", ProxyParameters.LANGUAGE);
    private static HashMap _map;
    private ComboBoxModel _cbmContraHead;
    private ComboBoxModel _cbmTransactionPosting;
    private ComboBoxModel _cbmPostingMode;
    private ComboBoxModel _cbmGLBalanceType;
    private ComboBoxModel _cbmStatus;
    
      
    private static AccountMaintenanceOB _objAccountMaintenanceOB; // singleton object
    
    private final static Logger _log = Logger.getLogger(AccountMaintenanceOB.class);
    private final static ClientParseException _parseException = ClientParseException.getInstance();
    private String txtserviceTaxId = ""; // Added by nithya on 12-01-2018 for 7013
    
    
    static {
        try {
            _log.info("Creating AccountMaintenanceOB...");
            _objAccountMaintenanceOB = new AccountMaintenanceOB();
        } catch(Exception e) {
            _parseException.logException(e,true);
        }
    }
    
    /** Creates a new instance of AccountMaintenanceOB */
    private AccountMaintenanceOB() throws Exception{
        _proxy = ProxyFactory.createProxy();
        _map = new HashMap();
        _map.put(CommonConstants.JNDI, "AccountMaintenanceJNDI");
        _map.put(CommonConstants.HOME, "generalledger.AccountMaintenanceHome");
        _map.put(CommonConstants.REMOTE, "generalledger.AccountMaintenance");
        fillDropdown();
         _cbmMajorHead = new ComboBoxModel();
        _cbmSubHead = new ComboBoxModel();
        _cbmContraHead = new ComboBoxModel();
        makeNull();
        setOperationMap();
        
    }
     private void setOperationMap() throws Exception{
        _proxy = ProxyFactory.createProxy();
        _operationMap = new HashMap();
        _operationMap.put(CommonConstants.JNDI, "AccountCreationJNDI");
        _operationMap.put(CommonConstants.HOME, "generalledger.AcctCreationHome");
        _operationMap.put(CommonConstants.REMOTE, "generalledger.AcctCreation");
    }
    /**
     * Returns an instance of AccountMaintenanceOB.
     * @return  AccountMaintenanceOB
     */
    public static AccountMaintenanceOB getInstance() {
        return _objAccountMaintenanceOB;
    }
    
    /*To set data for all dropdowns*/
    private void fillDropdown() throws Exception{
        final HashMap lookUpHash = new HashMap();
        final HashMap keyValue;
        
        lookUpHash.put(CommonConstants.MAP_NAME,null);
        
        final ArrayList lookup_keys = new ArrayList();
        lookup_keys.add("ACCTHEADMAIN.TRANSPOST");
        lookup_keys.add("ACCTHEADMAIN.POSTMODE");
        lookup_keys.add("ACCTHEADMAIN.BALTYPE");
         lookup_keys.add("ACCOUNTTYPE");
        lookUpHash.put(CommonConstants.PARAMFORQUERY, lookup_keys);
        
        keyValue = ClientUtil.populateLookupData(lookUpHash);
        
        getKeyValue((HashMap)keyValue.get("ACCTHEADMAIN.TRANSPOST"));
        _cbmTransactionPosting = new ComboBoxModel(_key,_value);
        getKeyValue((HashMap)keyValue.get("ACCTHEADMAIN.POSTMODE"));
        _cbmPostingMode = new ComboBoxModel(_key,_value);
        getKeyValue((HashMap)keyValue.get("ACCTHEADMAIN.BALTYPE"));
        _cbmGLBalanceType = new ComboBoxModel(_key,_value);
      
        getKeyValue((HashMap)keyValue.get("ACCOUNTTYPE"));
        _cbmAccountType = new ComboBoxModel(_key,_value);
        
        makeNull();
    }
    public void populateMajorHead(String accountType){
        try{
            _lookUpHash = new HashMap();
            _lookUpHash.put(CommonConstants.MAP_NAME,"getMajorHead");
            _lookUpHash.put(CommonConstants.PARAMFORQUERY, accountType);
            _keyValue = ClientUtil.populateLookupData(_lookUpHash);
            getKeyValue((HashMap)_keyValue.get(CommonConstants.DATA));
            _cbmMajorHead = new ComboBoxModel(_key,_value);
            setCbmMajorHead(_cbmMajorHead);
            makeNull();
        }catch(Exception e){
            //e.printStackTrace();
            _parseException.logException(e,true);
        }
    }
    
     public void populateSubHead(String majorHead){
        try{
            _lookUpHash = new HashMap();
            _lookUpHash.put(CommonConstants.MAP_NAME,"getSubHead");
            _lookUpHash.put(CommonConstants.PARAMFORQUERY, majorHead);
            _keyValue = ClientUtil.populateLookupData(_lookUpHash);
            getKeyValue((HashMap)_keyValue.get(CommonConstants.DATA));
            _cbmSubHead = new ComboBoxModel(_key,_value);
            setCbmSubHead(_cbmSubHead);
            makeNull();
        }catch(Exception e){
            //e.printStackTrace();
            _parseException.logException(e,true);
        }
    }
    /* To make the class variables null*/
    private void makeNull(){
        _key = null;
        _value = null;
         _lookUpHash = null;
        _keyValue = null;
    }
    
    /* Splits the keyValue HashMap into _key and _value arraylists*/
    private void getKeyValue(HashMap keyValue) throws Exception{
        _key = (ArrayList)keyValue.get(CommonConstants.KEY);
        _value = (ArrayList)keyValue.get(CommonConstants.VALUE);
    }
    
    /**To set combo box with the Contra heads
     *This method should not be used and this method's usage should be replaced by ClientUtil.getComboboxModel() method
     *This replacement should be done after the ClientUtil's methods are tested
     **/
    
    public void populateContraHead(String accountType){
        _log.info("accountType:"+accountType);
        try{
            final HashMap lookUpHash = new HashMap();
            lookUpHash.put(CommonConstants.MAP_NAME,"getContraHead");
            if (accountType.equalsIgnoreCase("CONTRAASSETS")){
                lookUpHash.put(CommonConstants.PARAMFORQUERY, "CONTRALIABILITY");
            }else{
                lookUpHash.put(CommonConstants.PARAMFORQUERY, "CONTRAASSETS");
            }
            //lookUpHash.put("paramforquery", accountType);
            final HashMap keyValue = ClientUtil.populateLookupData(lookUpHash);
            final HashMap oneMap = ((HashMap)keyValue.get(CommonConstants.DATA));
            final ArrayList key = (ArrayList)oneMap.get(CommonConstants.KEY);
            final ArrayList value = (ArrayList)oneMap.get(CommonConstants.VALUE);
            _log.info("key:"+key);
            _log.info("value:"+value);
            _cbmContraHead = new ComboBoxModel(key,value);
            setCbmContraHead(_cbmContraHead);
            _log.info("_cbmContraHead.getKeys():"+_cbmContraHead.getKeys());
        }catch(Exception e){
            //_log.error(e);
            _parseException.logException(e,true);
        }
    }
    
    /** To perform the appropriate operation */
    public void doAction() {
        try {
            //If actionType such as NEW, EDIT, DELETE, then proceed
            if( _actionType != ClientConstants.ACTIONTYPE_CANCEL ){
                //If actionType has got propervalue then doActionPerform, else throw error
                if( getCommand() != null ){
                    doActionPerform();
                }
                else{
                    
                    throw new TTException(objAccountMaintenanceRB.getString("TOCommandError"));
                }
            }
        } catch (Exception e) {
            setResult(ClientConstants.ACTIONTYPE_FAILED);
            //_log.error(e);
            _parseException.logException(e,true);
        }
    }
    
    /** To perform the necessary action */
    private void doActionPerform() throws Exception{
        
        final AccountCreationTO objAccountCreationTO = setAcctCreationData();
        final HashMap data = new HashMap();
        objAccountCreationTO.setCommand(getCommand());
       // final HashMap data = new HashMap();
        data.put("ACTCREATION",objAccountCreationTO);
        _log.info("objAccountCreationTO:"+objAccountCreationTO);
        data.put(CommonConstants.MODULE, getModule());
        data.put(CommonConstants.SCREEN, getScreen());
        HashMap proxyResultMap = _proxy.execute(data, _operationMap);
        
//        setResult(_actionType);
//        _actionType = ClientConstants.ACTIONTYPE_CANCEL;
//        
        final AccountMaintenanceTO objAccountMaintenanceTO = getAccountMaintenanceTO();
        System.out.println("objAccountMaintenanceTO"+objAccountMaintenanceTO);
        objAccountMaintenanceTO.setCommand(getCommand());
        data.put("AccountMaintenanceTO",objAccountMaintenanceTO);
        data.put(CommonConstants.MODULE, getModule());
        data.put(CommonConstants.SCREEN, getScreen());
        proxyResultMap = _proxy.execute(data, _map);
        
        setResult(_actionType);
        _actionType = ClientConstants.ACTIONTYPE_CANCEL;
        
        //resetForm();
    }
    public AccountCreationTO setAcctCreationData() {
        final AccountCreationTO objAccountCreationTO = new AccountCreationTO();
        try{
            objAccountCreationTO.setMjrAcHdId((String)_cbmMajorHead.getKeyForSelected());
            objAccountCreationTO.setSubAcHdId((String)_cbmSubHead.getKeyForSelected());
            objAccountCreationTO.setAcHdCode(_txtAccountHeadCode);
            objAccountCreationTO.setAcHdDesc(_txtAccountHeadDesc);
            objAccountCreationTO.setAcHdId(_txtAccountHead);
            objAccountCreationTO.setRecDayBook(getRdoReceiveDayBookDetail());
            objAccountCreationTO.setPayDayBook(getRdoPayDayBookDetail());
            objAccountCreationTO.setAcHeadOrder(CommonUtil.convertObjToDouble(getTxtAccountHeadOrder()));
        }catch(Exception e){
            //e.printStackTrace();
            _parseException.logException(e,true);
        }
        return objAccountCreationTO;
    }
    
    public ComboBoxModel getCbmTransactionPosting(){
        return this._cbmTransactionPosting;
    }
    public ComboBoxModel getCbmPostingMode(){
        return this._cbmPostingMode;
    }
    public ComboBoxModel getCbmGLBalanceType(){
        return this._cbmGLBalanceType;
    }
    
    public void populateData(HashMap whereMap) {
         try {
            HashMap mapData1 = _proxy.executeQuery(whereMap, _map);
            _log.info("mapData:1"+mapData1);
            populateOB(mapData1);
        } catch( Exception e ) {
            //_log.error(e);
            _parseException.logException(e,true);
        }
         try {
            HashMap mapData1 = _proxy.executeQuery(whereMap, _operationMap);
            _log.info("mapData:"+mapData1);
            populateOB1(mapData1);
        } catch( Exception e ) {
            _parseException.logException(e,true);
            //e.printStackTrace();
        }
    }
    
    private HashMap populateBean() throws Exception{
        final HashMap acctMain = new HashMap();
        acctMain.put("AccountMaintenanceTO", getAccountMaintenanceTO());
        return acctMain;
    }
    
    private void populateOB(HashMap mapData) throws Exception{
        resetOBFields();
        AccountMaintenanceTO objAccountMaintenanceTO = null;
        objAccountMaintenanceTO = (AccountMaintenanceTO) ((List) mapData.get("AccountMaintenanceTO")).get(0);
        setAccountMaintenanceTO(objAccountMaintenanceTO);
        System.out.println("Populate OB");
//         HashMap accountCreationMap = (HashMap) ((List) mapData.get("AccountCreationDetails")).get(0);
//        System.out.println("accountCreationMap:"+accountCreationMap);
//        populateMajorHead((String)accountCreationMap.get("MJR_AC_HD_TYPE"));
//        populateSubHead((String)accountCreationMap.get("MJR_AC_HD_ID"));
//        setCboAccountType(CommonUtil.convertObjToStr(getCbmAccountType().getDataForKey(accountCreationMap.get("MJR_AC_HD_TYPE"))));
//        setCboMajorHead(CommonUtil.convertObjToStr(getCbmMajorHead().getDataForKey(accountCreationMap.get("MJR_AC_HD_ID"))));
//        setCboSubHead(CommonUtil.convertObjToStr(getCbmSubHead().getDataForKey(accountCreationMap.get("SUB_AC_HD_ID"))));
//        setTxtAccountHeadCode(CommonUtil.convertObjToStr(accountCreationMap.get("AC_HD_CODE")));
//        setTxtAccountHeadDesc(CommonUtil.convertObjToStr(accountCreationMap.get("AC_HD_DESC")));
//        setTxtAccountHead(CommonUtil.convertObjToStr(accountCreationMap.get("AC_HD_ID")));
//        setRdoReceiveDayBookDetail(CommonUtil.convertObjToStr(accountCreationMap.get("REC_DETAILED_IN_DAYBOOK")));
//        setRdoPayDayBookDetail(CommonUtil.convertObjToStr(accountCreationMap.get("PAY_DETAILED_IN_DAYBOOK")));
//        setTxtAccountHeadOrder(CommonUtil.convertObjToStr(accountCreationMap.get("AC_HD_ORDER")));
//        ttNotifyObservers();
    }
      private void populateOB1(HashMap mapData1) throws Exception{
        HashMap accountCreationMap = (HashMap) ((List) mapData1.get("AccountCreationDetails")).get(0);
        System.out.println("accountCreationMap:"+accountCreationMap);
        populateMajorHead((String)accountCreationMap.get("MJR_AC_HD_TYPE"));
        populateSubHead((String)accountCreationMap.get("MJR_AC_HD_ID"));
        setCboAccountType(CommonUtil.convertObjToStr(getCbmAccountType().getDataForKey(accountCreationMap.get("MJR_AC_HD_TYPE"))));
        setCboMajorHead(CommonUtil.convertObjToStr(getCbmMajorHead().getDataForKey(accountCreationMap.get("MJR_AC_HD_ID"))));
        setCboSubHead(CommonUtil.convertObjToStr(getCbmSubHead().getDataForKey(accountCreationMap.get("SUB_AC_HD_ID"))));
        setTxtAccountHeadCode(CommonUtil.convertObjToStr(accountCreationMap.get("AC_HD_CODE")));
        setTxtAccountHeadDesc(CommonUtil.convertObjToStr(accountCreationMap.get("AC_HD_DESC")));
        setTxtAccountHead(CommonUtil.convertObjToStr(accountCreationMap.get("AC_HD_ID")));
        setRdoReceiveDayBookDetail(CommonUtil.convertObjToStr(accountCreationMap.get("REC_DETAILED_IN_DAYBOOK")));
        setRdoPayDayBookDetail(CommonUtil.convertObjToStr(accountCreationMap.get("PAY_DETAILED_IN_DAYBOOK")));
        setTxtAccountHeadOrder(CommonUtil.convertObjToStr(accountCreationMap.get("AC_HD_ORDER")));
        ttNotifyObservers();   
      }
    private AccountMaintenanceTO getAccountMaintenanceTO() throws Exception{
        AccountMaintenanceTO objAccountMaintenanceTO = new AccountMaintenanceTO();
        objAccountMaintenanceTO.setAcHdId(getTxtAccountHead());
        
        
        if (getChkCreditCash() == true) {
            objAccountMaintenanceTO.setCrCash("Y");
        }else{
            objAccountMaintenanceTO.setCrCash("N");
        }
        
        if (getChkCreditClearing() == true) {
            objAccountMaintenanceTO.setCrClr("Y");
        }else{
            objAccountMaintenanceTO.setCrClr("N");
        }
        
        if (getChkCreditTransfer() == true){
            objAccountMaintenanceTO.setCrTrans("Y");
        }else{
            objAccountMaintenanceTO.setCrTrans("N");
        }
        
        if (getChkDebitCash() == true) {
            objAccountMaintenanceTO.setDrCash("Y");
        }else{
            objAccountMaintenanceTO.setDrCash("N");
        }
        
        if (getChkDebitClearing() == true){
            objAccountMaintenanceTO.setDrClr("Y");
        }else{
            objAccountMaintenanceTO.setDrClr("N");
        }
        
        if (getChkDebitTransfer() == true){
            objAccountMaintenanceTO.setDrTrans("Y");
        }else {
            objAccountMaintenanceTO.setDrTrans("N");
        }
        
        
        if (getRdoFloatAccount_Yes() == true) {
            objAccountMaintenanceTO.setFloatAct("Y");
        }else{
            objAccountMaintenanceTO.setFloatAct("N");
        }
        
        /*objAccountMaintenanceTO.setFTransDt(DateUtil.getDateMMDDYYYY("1/1/2003"));
        objAccountMaintenanceTO.setLTransDt(DateUtil.getDateMMDDYYYY("1/1/2003"));
        objAccountMaintenanceTO.setAcOpenDt(DateUtil.getDateMMDDYYYY("1/1/2003"));
        objAccountMaintenanceTO.setAcCloseDt(DateUtil.getDateMMDDYYYY("1/1/2003"));*/
        
        if (getChkReconcilliationAllowed() == true){
            objAccountMaintenanceTO.setRecons("Y");
        }else {
            objAccountMaintenanceTO.setRecons("N");
        }
        
        objAccountMaintenanceTO.setTranspost(getCboTransactionPosting());
        objAccountMaintenanceTO.setPostmode(getCboPostingMode());
        objAccountMaintenanceTO.setBalancetype(getCboGLBalanceType());
        objAccountMaintenanceTO.setContraAct(getCboContraHead());
        objAccountMaintenanceTO.setGlbalance(CommonUtil.convertObjToDouble(getTxtBalanceInGL()));
        
        if (getRdoNegValue_Yes() == true) {
            objAccountMaintenanceTO.setNegativeAllowed(YES);
        }else{
            objAccountMaintenanceTO.setNegativeAllowed(NO);
        }
        
        objAccountMaintenanceTO.setCommand(CommonConstants.TOSTATUS_INSERT);
        
        if(getChkHdOfficeAc()==true){
            objAccountMaintenanceTO.setHoAcct(YES);
        }else{
            objAccountMaintenanceTO.setHoAcct(NO);
        }
         if(getChkBalCheck()==true){
            objAccountMaintenanceTO.setDayEndZeroCheck(YES);
        }else{
            objAccountMaintenanceTO.setDayEndZeroCheck(NO);
        }
        if(isChkAllowCustomerEntry()==true){
            objAccountMaintenanceTO.setChkCustomerAllow(YES);
        }else{
             objAccountMaintenanceTO.setChkCustomerAllow(NO);
        }
        if(isCbServiceTax()==true){
            objAccountMaintenanceTO.setCbServiceTax(YES);
        }else{
            objAccountMaintenanceTO.setCbServiceTax(NO);
        }
        objAccountMaintenanceTO.setReconsAcHdId(getTxtReconcillationAcHd());
        
        objAccountMaintenanceTO.setServiceTaxId(getTxtserviceTaxId()); // Added by nithya on 12-01-2018 for 7013
        
        return objAccountMaintenanceTO;
    }
    
    private void setAccountMaintenanceTO(AccountMaintenanceTO objAccountMaintenanceTO) throws Exception{
        setTxtAccountHead(objAccountMaintenanceTO.getAcHdId());
        setTxtBalanceInGL(CommonUtil.convertObjToStr(objAccountMaintenanceTO.getGlbalance()));
        if (objAccountMaintenanceTO.getContraAct() != null){
            setCboContraHead(CommonUtil.convertObjToStr(getCbmContraHead().getDataForKey(objAccountMaintenanceTO.getContraAct())));
        }
        setCboGLBalanceType(CommonUtil.convertObjToStr(getCbmGLBalanceType().getDataForKey(objAccountMaintenanceTO.getBalancetype())));
        setCboPostingMode(CommonUtil.convertObjToStr(getCbmPostingMode().getDataForKey(objAccountMaintenanceTO.getPostmode())));
        setCboTransactionPosting(CommonUtil.convertObjToStr(getCbmTransactionPosting().getDataForKey(objAccountMaintenanceTO.getTranspost())));
        
        if (objAccountMaintenanceTO.getCrCash() != null && objAccountMaintenanceTO.getCrCash().equals("Y")){
            setChkCreditCash(true);
        }else {
            setChkCreditCash(false);
        }
        
        if (objAccountMaintenanceTO.getCrClr() != null&& objAccountMaintenanceTO.getCrClr().equals("Y")) {
            setChkCreditClearing(true);
        }else {
            setChkCreditClearing(false);
        }
        
        if (objAccountMaintenanceTO.getCrTrans() != null && objAccountMaintenanceTO.getCrTrans().equals("Y")){
            setChkCreditTransfer(true);
        }else{
            setChkCreditTransfer(false);
        }
        
        if (objAccountMaintenanceTO.getDrCash() != null && objAccountMaintenanceTO.getDrCash().equals("Y")){
            setChkDebitCash(true);
            
        }else{
            setChkDebitCash(false);
        }
        
        if (objAccountMaintenanceTO.getDrClr() != null && objAccountMaintenanceTO.getDrClr().equals("Y")){
            setChkDebitClearing(true);
        }else{
            setChkDebitClearing(false);
        }
        
        if (objAccountMaintenanceTO.getDrTrans() != null && objAccountMaintenanceTO.getDrTrans().equals("Y")){
            setChkDebitTransfer(true);
        }else{
            setChkDebitTransfer(false);
        }
        
        if (objAccountMaintenanceTO.getRecons() != null && objAccountMaintenanceTO.getRecons().equals("Y")) {
            setChkReconcilliationAllowed(true);
        }else{
            setChkReconcilliationAllowed(false);
        }
         if (objAccountMaintenanceTO.getDayEndZeroCheck() != null && objAccountMaintenanceTO.getDayEndZeroCheck().equals(YES)) {
            setChkBalCheck(true);
        }else{
            setChkBalCheck(false);
        }
        
         if (objAccountMaintenanceTO.getChkCustomerAllow() != null && objAccountMaintenanceTO.getChkCustomerAllow().equals(YES)) {
            setChkAllowCustomerEntry(true);
        }else{
            setChkAllowCustomerEntry(false);
        }
          if (objAccountMaintenanceTO.getCbServiceTax() != null && objAccountMaintenanceTO.getCbServiceTax().equals(YES)) {
            setCbServiceTax(true);
        }else{
            setCbServiceTax(false);
        }
        
        if (objAccountMaintenanceTO.getFloatAct() != null && objAccountMaintenanceTO.getFloatAct().equals("Y")){
            setRdoFloatAccount_Yes(true);
        }else{
            setRdoFloatAccount_No(true);
        }
        setLblFirstTransactionDateDisplay(DateUtil.getStringDate(objAccountMaintenanceTO.getFTransDt()));
        setLblLastTransactionDateDisplay(DateUtil.getStringDate(objAccountMaintenanceTO.getLTransDt()));
        setLblAccountOpenedOnDisplay(DateUtil.getStringDate(objAccountMaintenanceTO.getAcOpenDt()));
        setLblAccountClosedOnDisplay(DateUtil.getStringDate(objAccountMaintenanceTO.getAcCloseDt()));
        /*objAccountMaintenanceTO.getFTransDt();
        objAccountMaintenanceTO.getLTransDt();*/
        System.out.println("AccountMaintanaceTo");
        
        if (CommonUtil.convertObjToStr(objAccountMaintenanceTO.getNegativeAllowed()).equalsIgnoreCase(YES)){
            setRdoNegValue_Yes(true);
        }else{
            setRdoNegValue_No(true);
        }
         if (objAccountMaintenanceTO.getHoAcct() != null && objAccountMaintenanceTO.getHoAcct().equals("Y")){
            setChkHdOfficeAc(true);
        }else{
            setChkHdOfficeAc(false);
        }
        setTxtReconcillationAcHd(objAccountMaintenanceTO.getReconsAcHdId());
        
        setTxtserviceTaxId(objAccountMaintenanceTO.getServiceTaxId());// Added by nithya on 12-01-2018 for 7013

    }
    
    public void ttNotifyObservers(){
        notifyObservers();
    }
    
    public void fillAccountHeadInfo(String callingCode) {
        final HashMap callingCodeMap = new HashMap();
        _log.info("callingCode:"+callingCode);
        callingCodeMap.put("CALLINGCODE",callingCode);
        try {
            final List resultList = ClientUtil.executeQuery("getAccountHeadInfo", callingCodeMap);
            final HashMap resultMap = (HashMap)resultList.get(0);
            StringBuffer temp;
            setLblAccountTypeDisplay(CommonUtil.convertObjToStr(resultMap.get("LOOKUP_DESC")));
            System.out.println("Fill Account Head Info");
            temp = new StringBuffer();
            temp.append(resultMap.get("MJR_AC_HD_DESC")).append(" (").append(resultMap.get("MJR_AC_HD_ID")).append(")");
            setLblMajorHeadDisplay(temp.toString());
            
            temp = new StringBuffer();
            temp.append(resultMap.get("SUB_AC_HD_DESC")).append(" (").append(resultMap.get("SUB_AC_HD_ID")).append(")");
            setLblSubHeadDisplay(temp.toString());
            
            temp = new StringBuffer();
            temp.append(CommonUtil.convertObjToStr(resultMap.get("AC_HD_CODE")));
            setLblAccountHeadCodeDisplay(temp.toString());
            temp = null;

            temp = new StringBuffer();
            temp.append(resultMap.get("AC_HD_DESC")).append(" (").append(resultMap.get("AC_HD_ID")).append(")");
            setLblAccountHeadDescDisplay(temp.toString());
            temp = null;
        } catch (Exception e) {
            _parseException.logException(e,true);
        }
    }
    
    public void insertData() {
        try {
            HashMap proxyResultMap = _proxy.execute(populateBean(), _map);
        } catch (Exception e) {
            //_log.error(e);
            _parseException.logException(e,true);
        }
    }
    
    public int updateData() {
        int state = 0;
        try {
            HashMap proxyResultMap = _proxy.execute(populateBean(), _map);
            state = 1;
        } catch (Exception e) {
            //_log.error(e);
            _parseException.logException(e,true);
        }
        return state;
    }
    
    public int deleteData() {
        int state = 0;
        try {
            HashMap proxyResultMap = _proxy.execute(populateBean(), _map);
            state = 1;
        } catch (Exception e) {
            //_log.error(e);
            _parseException.logException(e,true);
        }
        return state;
    }
    
    private String getCommand(){
        String command = null;
        System.out.println("action type here"+_actionType);
        switch (_actionType) {
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
    
    public void resetOBFields() {
        _txtAccountHead = "";
        _txtReconcillationAcHd = "";
        _cboContraHead = "";
        _chkCreditClearing = true;
        _chkCreditTransfer = true;
        _chkCreditCash = true;
        _chkDebitClearing = true;
        _chkDebitTransfer = true;
        _chkDebitCash = true;
        _cboTransactionPosting = "";
        _cboPostingMode = "";
        _chkReconcilliationAllowed = false;
        _chkHdOfficeAc = false;
        chkBalCheck = false;
        chkAllowCustomerEntry=false;
        _txtBalanceInGL = "";
        _rdoFloatAccount_Yes = false;
        _rdoFloatAccount_No = false;
        
        rdoNegValue_No = false;
        rdoNegValue_Yes = false;
        cbServiceTax=false;
        setCboAccountType("");
        setCboMajorHead("");
        setCboSubHead("");
        setTxtAccountHead("");
        setTxtAccountHeadCode("");
        setTxtAccountHeadDesc("");
        setRdoReceiveDayBookDetail("");
        setRdoPayDayBookDetail("");
        setTxtAccountHeadOrder("");
        resetAcHdRelated();
        setTxtserviceTaxId("");// Added by nithya on 12-01-2018 for 7013
        ttNotifyObservers();
    }
    
    public void resetAcHdRelated(){
        _lblAccountOpenedOnDisplay = "";
        _lblAccountClosedOnDisplay = "";
        _lblLastTransactionDateDisplay = "";
        _lblFirstTransactionDateDisplay = "";
        _lblAccountTypeDisplay = "";
        _lblMajorHeadDisplay = "";
        _lblSubHeadDisplay = "";
        _lblAccountHeadCodeDisplay = "";
        _cboGLBalanceType = "";
        setChanged();
    }

    public ComboBoxModel getCbmAccountType() {
        return _cbmAccountType;
    }

    public void setCbmAccountType(ComboBoxModel _cbmAccountType) {
        this._cbmAccountType = _cbmAccountType;
    }

    public ComboBoxModel getCbmMajorHead() {
        return _cbmMajorHead;
    }

    public void setCbmMajorHead(ComboBoxModel _cbmMajorHead) {
        this._cbmMajorHead = _cbmMajorHead;
    }

    public ComboBoxModel getCbmSubHead() {
        return _cbmSubHead;
    }

    public void setCbmSubHead(ComboBoxModel _cbmSubHead) {
        this._cbmSubHead = _cbmSubHead;
    }

    public String getCboAccountType() {
        return _cboAccountType;
    }

    public void setCboAccountType(String _cboAccountType) {
        this._cboAccountType = _cboAccountType;
    }

    public String getCboMajorHead() {
        return _cboMajorHead;
    }

    public void setCboMajorHead(String _cboMajorHead) {
        this._cboMajorHead = _cboMajorHead;
    }

    public String getCboSubHead() {
        return _cboSubHead;
    }

    public void setCboSubHead(String _cboSubHead) {
        this._cboSubHead = _cboSubHead;
    }

    public String getTxtAccountHeadCode() {
        return _txtAccountHeadCode;
    }

    public void setTxtAccountHeadCode(String _txtAccountHeadCode) {
        this._txtAccountHeadCode = _txtAccountHeadCode;
    }

    public String getTxtAccountHeadDesc() {
        return _txtAccountHeadDesc;
    }

    public void setTxtAccountHeadDesc(String _txtAccountHeadDesc) {
        this._txtAccountHeadDesc = _txtAccountHeadDesc;
    }

    public String getRdoPayDayBookDetail() {
        return rdoPayDayBookDetail;
    }

    public void setRdoPayDayBookDetail(String rdoPayDayBookDetail) {
        this.rdoPayDayBookDetail = rdoPayDayBookDetail;
    }

    public String getRdoReceiveDayBookDetail() {
        return rdoReceiveDayBookDetail;
    }

    public void setRdoReceiveDayBookDetail(String rdoReceiveDayBookDetail) {
        this.rdoReceiveDayBookDetail = rdoReceiveDayBookDetail;
    }

    public String getTxtAccountHeadOrder() {
        return txtAccountHeadOrder;
    }

    public void setTxtAccountHeadOrder(String txtAccountHeadOrder) {
        this.txtAccountHeadOrder = txtAccountHeadOrder;
    }
    
    public void resetStatus(){
        this.setLblStatus(ClientConstants.ACTION_STATUS[0]);
    }
    
    void setLblAccountOpenedOnDisplay(String lblAccountOpenedOnDisplay){
        this._lblAccountOpenedOnDisplay = lblAccountOpenedOnDisplay;
        setChanged();
    }
    String getLblAccountOpenedOnDisplay(){
        return this._lblAccountOpenedOnDisplay;
    }
    
    void setLblAccountClosedOnDisplay(String lblAccountClosedOnDisplay){
        this._lblAccountClosedOnDisplay = lblAccountClosedOnDisplay;
        setChanged();
    }
    String getLblAccountClosedOnDisplay(){
        return this._lblAccountClosedOnDisplay;
    }
    
    void setLblLastTransactionDateDisplay(String lblLastTransactionDateDisplay){
        this._lblLastTransactionDateDisplay = lblLastTransactionDateDisplay;
        setChanged();
    }
    String getLblLastTransactionDateDisplay(){
        return this._lblLastTransactionDateDisplay;
    }
    
    void setLblFirstTransactionDateDisplay(String lblFirstTransactionDateDisplay){
        this._lblFirstTransactionDateDisplay = lblFirstTransactionDateDisplay;
        setChanged();
    }
    String getLblFirstTransactionDateDisplay(){
        return this._lblFirstTransactionDateDisplay;
    }
    
    void setLblAccountTypeDisplay(String lblAccountTypeDisplay){
        this._lblAccountTypeDisplay = lblAccountTypeDisplay;
        setChanged();
    }
    String getLblAccountTypeDisplay(){
        return this._lblAccountTypeDisplay;
    }
    
    void setLblMajorHeadDisplay(String lblMajorHeadDisplay){
        this._lblMajorHeadDisplay = lblMajorHeadDisplay;
        setChanged();
    }
    String getLblMajorHeadDisplay(){
        return this._lblMajorHeadDisplay;
    }
    
    void setLblSubHeadDisplay(String lblSubHeadDisplay){
        this._lblSubHeadDisplay = lblSubHeadDisplay;
        setChanged();
    }
    String getLblSubHeadDisplay(){
        return this._lblSubHeadDisplay;
    }
    
    void setLblAccountHeadCodeDisplay(String lblAccountHeadCodeDisplay){
        this._lblAccountHeadCodeDisplay = lblAccountHeadCodeDisplay;
        setChanged();
    }
    String getLblAccountHeadCodeDisplay(){
        return this._lblAccountHeadCodeDisplay;
    }
    
    void setTxtAccountHead(String txtAccountHead){
        this._txtAccountHead = txtAccountHead;
        setChanged();
    }
    String getTxtAccountHead(){
        return this._txtAccountHead;
    }
    
    void setCboContraHead(String cboContraHead){
        this._cboContraHead = cboContraHead;
        setChanged();
    }
    String getCboContraHead(){
        return this._cboContraHead;
    }
    
    void setChkCreditClearing(boolean chkCreditClearing){
        this._chkCreditClearing = chkCreditClearing;
        setChanged();
    }
    boolean getChkCreditClearing(){
        return this._chkCreditClearing;
    }
    
    void setChkCreditTransfer(boolean chkCreditTransfer){
        this._chkCreditTransfer = chkCreditTransfer;
        setChanged();
    }
    boolean getChkCreditTransfer(){
        return this._chkCreditTransfer;
    }
    
    void setChkCreditCash(boolean chkCreditCash){
        this._chkCreditCash = chkCreditCash;
        setChanged();
    }
    boolean getChkCreditCash(){
        return this._chkCreditCash;
    }
    
    void setChkDebitClearing(boolean chkDebitClearing){
        this._chkDebitClearing = chkDebitClearing;
        setChanged();
    }
    boolean getChkDebitClearing(){
        return this._chkDebitClearing;
    }
    
    void setChkDebitTransfer(boolean chkDebitTransfer){
        this._chkDebitTransfer = chkDebitTransfer;
        setChanged();
    }
    boolean getChkDebitTransfer(){
        return this._chkDebitTransfer;
    }
    
    void setChkDebitCash(boolean chkDebitCash){
        this._chkDebitCash = chkDebitCash;
        setChanged();
    }
    boolean getChkDebitCash(){
        return this._chkDebitCash;
    }
    
    void setCboTransactionPosting(String cboTransactionPosting){
        this._cboTransactionPosting = cboTransactionPosting;
        setChanged();
    }
    String getCboTransactionPosting(){
        return this._cboTransactionPosting;
    }
    
    void setCboPostingMode(String cboPostingMode){
        this._cboPostingMode = cboPostingMode;
        setChanged();
    }
    String getCboPostingMode(){
        return this._cboPostingMode;
    }
    
    void setCboGLBalanceType(String cboGLBalanceType){
        this._cboGLBalanceType = cboGLBalanceType;
        setChanged();
    }
    String getCboGLBalanceType(){
        return this._cboGLBalanceType;
    }
    
    void setChkReconcilliationAllowed(boolean chkReconcilliationAllowed){
        this._chkReconcilliationAllowed = chkReconcilliationAllowed;
        setChanged();
    }
    boolean getChkReconcilliationAllowed(){
        return this._chkReconcilliationAllowed;
    }
    
    void setTxtBalanceInGL(String txtBalanceInGL){
        this._txtBalanceInGL = txtBalanceInGL;
        setChanged();
    }
    String getTxtBalanceInGL(){
        return this._txtBalanceInGL;
    }
    
    void setRdoFloatAccount_Yes(boolean rdoFloatAccount_Yes){
        this._rdoFloatAccount_Yes = rdoFloatAccount_Yes;
        setChanged();
    }
    boolean getRdoFloatAccount_Yes(){
        return this._rdoFloatAccount_Yes;
    }
    
    void setRdoFloatAccount_No(boolean rdoFloatAccount_No){
        this._rdoFloatAccount_No = rdoFloatAccount_No;
        setChanged();
    }
    boolean getRdoFloatAccount_No(){
        return this._rdoFloatAccount_No;
    }
    
    void setCbmContraHead(ComboBoxModel cbmContraHead){
        this._cbmContraHead = cbmContraHead;
        setChanged();
        
    }
    ComboBoxModel getCbmContraHead(){
        return this._cbmContraHead;
    }
    
    void setCbmStatus(ComboBoxModel cbmStatus){
        this._cbmStatus = cbmStatus;
        setChanged();
        
    }
    ComboBoxModel getCbmStatus(){
        return this._cbmStatus;
    }
    
    
    void setRdoNegValue_No(boolean rdoNegValue_No){
        this.rdoNegValue_No = rdoNegValue_No;
        setChanged();
    }
    boolean getRdoNegValue_No(){
        return this.rdoNegValue_No;
    }
    
    void setRdoNegValue_Yes(boolean rdoNegValue_Yes){
        this.rdoNegValue_Yes = rdoNegValue_Yes;
        setChanged();
    }
    boolean getRdoNegValue_Yes(){
        return this.rdoNegValue_Yes;
    }
    
     /**
     * Getter for property _chkHdOfficeAc.
     * @return Value of property _chkHdOfficeAc.
     */
    public boolean getChkHdOfficeAc() {
        return this._chkHdOfficeAc;
    }
    
    /**
     * Setter for property _chkHdOfficeAc.
     * @param _chkHdOfficeAc New value of property _chkHdOfficeAc.
     */
    public void setChkHdOfficeAc(boolean _chkHdOfficeAc) {
        this._chkHdOfficeAc = _chkHdOfficeAc;
        setChanged();
    }
    
    /**
     * Getter for property _txtReconcillationAcHd.
     * @return Value of property _txtReconcillationAcHd.
     */
    public String getTxtReconcillationAcHd() {
        return this._txtReconcillationAcHd;
    }
    
    /**
     * Setter for property _txtReconcillationAcHd.
     * @param _txtReconcillationAcHd New value of property _txtReconcillationAcHd.
     */
    public void setTxtReconcillationAcHd(String _txtReconcillationAcHd) {
        this._txtReconcillationAcHd = _txtReconcillationAcHd;
        setChanged();
    }
    
      /**
     * Getter for property chkBalCheck.
     * @return Value of property chkBalCheck.
     */
    public boolean getChkBalCheck() {
        return chkBalCheck;
    }    
   
    /**
     * Setter for property chkBalCheck.
     * @param chkBalCheck New value of property chkBalCheck.
     */
    public void setChkBalCheck(boolean chkBalCheck) {
        this.chkBalCheck = chkBalCheck;
        setChanged();
    }    
    
    public String getLblStatus(){
        return _lblStatus;
    }
    
    public void setLblStatus(String lblStatus) {
        _lblStatus = lblStatus;
        setChanged();
    }
    
    
    public int getActionType(){
        return _actionType;
    }
    
    public void setActionType(int actionType) {
        _actionType = actionType;
        setChanged();
    }
    
    public int getResult(){
        return _result;
    }
    
    public void setResult(int result) {
        _result = result;
        setChanged();
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
     public String getMaxAcHdCode(){
        String maxAcHdCode = null;
        try{
            final HashMap acHdMap = new HashMap();
            acHdMap.put("AC_HD_CODE", (String)getCbmMajorHead().getKeyForSelected()+getCbmSubHead().getKeyForSelected());
            //final String acHdCode = CommonUtil.convertObjToStr(((HashMap)ClientUtil.executeQuery("getMaxAcHdCode", acHdMap).get(0)).get("MAX(AC_HD_CODE)"));
            final String acHdCode = CommonUtil.convertObjToStr(((HashMap)ClientUtil.executeQuery("getMaxAcHdCode", acHdMap).get(0)).get("AC_HD_CODE"));
            if (acHdCode.length() == 0 ){
                maxAcHdCode = STARTING_NUMBER;
            }else{
                // If the data is wrong i.e the major Head and Sub Head are not of specified length, error can occur in the following line of code
                int codeVal = 0;
                codeVal = CommonUtil.convertObjToInt(acHdCode.substring(7));
                maxAcHdCode = CommonUtil.lpad(String.valueOf(codeVal+1),3,'0');
            }
        } catch (Exception e) {
            _parseException.logException(e,true);
        }
        return maxAcHdCode;
    }
     
      public DefaultTreeModel getAcHdTree(){        
	DefaultMutableTreeNode root = new DefaultMutableTreeNode(objAccountMaintenanceRB.getString("treeHeading"));
        DefaultMutableTreeNode accTypeNode = null;
        DefaultMutableTreeNode mjrHdNode = null;
        DefaultMutableTreeNode subHdNode = null;
        DefaultMutableTreeNode acHdNode = null;
        
        final List objList = ClientUtil.executeQuery("acHdCreation.acHdTree", null);        
        final int objListSize = objList.size();
        
        for (int i = 0;i<objListSize;i++){
            accTypeNode = addNode(root,accTypeNode,((HashMap)objList.get(i)).get("MJR_AC_HD_TYPE"));
            mjrHdNode = addNode(accTypeNode,mjrHdNode,((HashMap)objList.get(i)).get("MJR_AC_HD"));
            subHdNode = addNode(mjrHdNode,subHdNode,((HashMap)objList.get(i)).get("SUB_AC_HD"));
            acHdNode = addNode(subHdNode,acHdNode,((HashMap)objList.get(i)).get("AC_HD_ID"));
        }
        final DefaultTreeModel treemodel = new DefaultTreeModel(root);
        
        root = null;
        accTypeNode = null;
        mjrHdNode = null;
        subHdNode = null;
        acHdNode = null;  
        
        return treemodel;
    }
      private DefaultMutableTreeNode addNode(DefaultMutableTreeNode parent,DefaultMutableTreeNode child,Object obj){ 
        if (!chkExistance(parent,obj.toString())){
            child = new DefaultMutableTreeNode(obj);
        }            
        if (!parent.isNodeChild(child)){
            parent.add(child);
        }      
        return child;
    }
    
    private boolean chkExistance(DefaultMutableTreeNode parent,String chkStr){
        final Enumeration objEnumeration = parent.children();
        while (objEnumeration.hasMoreElements()){
            if (objEnumeration.nextElement().toString().equals(chkStr)){
                return true;
            }
        }
        return false;
    }
    
    /**
     * To set the 'Type of Balance in GL' Combobox based on the Account Type
     **/
    public void setGLBalType(String acctType){
        if (acctType.equals(GLConstants.INCOME) || acctType.equals(GLConstants.ASSETS) || acctType.equals(GLConstants.CONTRAASSETS)){
            setCboGLBalanceType(CommonUtil.convertObjToStr(getCbmGLBalanceType().getDataForKey(GLConstants.CREDIT)));
        }else{
            setCboGLBalanceType(CommonUtil.convertObjToStr(getCbmGLBalanceType().getDataForKey(GLConstants.DEBIT)));
        }
    }
    
    /**
     * Getter for property _lblAccountHeadDescDisplay.
     * @return Value of property _lblAccountHeadDescDisplay.
     */
    public java.lang.String getLblAccountHeadDescDisplay() {
        return _lblAccountHeadDescDisplay;
    }    
  
    /**
     * Setter for property _lblAccountHeadDescDisplay.
     * @param _lblAccountHeadDescDisplay New value of property _lblAccountHeadDescDisplay.
     */
    public void setLblAccountHeadDescDisplay(java.lang.String lblAccountHeadDescDisplay) {
        this._lblAccountHeadDescDisplay = lblAccountHeadDescDisplay;
    }    
    
    /**
     * Getter for property chkAllowCustomerEntry.
     * @return Value of property chkAllowCustomerEntry.
     */
    public boolean isChkAllowCustomerEntry() {
        return chkAllowCustomerEntry;
    }
    
    /**
     * Setter for property chkAllowCustomerEntry.
     * @param chkAllowCustomerEntry New value of property chkAllowCustomerEntry.
     */
    public void setChkAllowCustomerEntry(boolean chkAllowCustomerEntry) {
        this.chkAllowCustomerEntry = chkAllowCustomerEntry;
    }    
    
    /*public static void main(String str[]) {
        TOHeader toh = new TOHeader();
        toh.setCommand("Insert");
     
        AccountMaintenanceTO objAccountMaintenanceTO = new AccountMaintenanceTO();
        objAccountMaintenanceTO.setTOHeader(toh);
        objAccountMaintenanceTO.setAcHdId("58");
        objAccountMaintenanceTO.setAcHdStatusId("Behavior");
        objAccountMaintenanceTO.setBalancetype("DR");
        //objAccountMaintenanceTO.setContraHead("51");
        objAccountMaintenanceTO.setCrCash("Y");
        objAccountMaintenanceTO.setCrClr("Y");
        objAccountMaintenanceTO.setCrTrans("Y");
        objAccountMaintenanceTO.setDrCash("Y");
        objAccountMaintenanceTO.setDrClr("Y");
        objAccountMaintenanceTO.setDrTrans("Y");
        objAccountMaintenanceTO.setFTransDt(new java.util.Date());
        objAccountMaintenanceTO.setFloatAct("Y");
        objAccountMaintenanceTO.setGlbalance(new Double("10.0"));
        objAccountMaintenanceTO.setLTransDt(new java.util.Date());
        objAccountMaintenanceTO.setPostmode("t");
        objAccountMaintenanceTO.setRecons("Y");
        objAccountMaintenanceTO.setTranspost("Tran");
        objAccountMaintenanceTO.setAcOpenDt(new java.util.Date());
        objAccountMaintenanceTO.setAcCloseDt(new java.util.Date());
     
        HashMap map1 = new HashMap();
        //map.put("WHERE", "P001");
        //HashMap map1 = (HashMap) dao.getData(map);
        //List lst = (List) map1.get("OperativeAcctProductTO");
     
        HashMap map2 = new HashMap();
        map2.put(CommonConstants.JNDI, "AccountMaintenanceJNDI");
        map2.put(CommonConstants.HOME, "generalledger.AccountMaintenanceHome");
        map2.put(CommonConstants.REMOTE, "generalledger.AccountMaintenance");
     
        try {
            _proxy = ProxyFactory.createProxy();
        } catch (Exception e) {
            //_log.error(e);
            _parseException.logException(e,true);
        }
     
        map1.put("AccountMaintenanceTO", objAccountMaintenanceTO);
        AccountMaintenanceTO obj = (AccountMaintenanceTO) map1.get("AccountMaintenanceTO");
        try {
            _HashMap proxyResultMap = proxy.execute(map1, map2);
        } catch (Exception e) {
            //_log.error(e);
            _parseException.logException(e,true);
        }
    }*/

    public boolean isCbServiceTax() {
        return cbServiceTax;
    }

    public void setCbServiceTax(boolean cbServiceTax) {
        this.cbServiceTax = cbServiceTax;
    }
   
    // Added by nithya on 12-01-2018 for 7013
    public String getTxtserviceTaxId() {
        return txtserviceTaxId;
    }

    public void setTxtserviceTaxId(String txtserviceTaxId) {
        this.txtserviceTaxId = txtserviceTaxId;
    }
    
    
    
}

