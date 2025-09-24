/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition. 
 * 
 * BankOB.java
 *
 * Created on February 5, 2004, 4:39 PM
 */

package com.see.truetransact.ui.sysadmin.bank;

/**
 *
 * @author  Hemant
 */

import java.util.HashMap;
import java.util.ArrayList;
import java.util.List;

import com.see.truetransact.commonutil.CommonConstants;
import com.see.truetransact.commonutil.CommonUtil;
import com.see.truetransact.clientproxy.ProxyFactory;
import com.see.truetransact.clientutil.ComboBoxModel;
import com.see.truetransact.clientutil.ClientConstants;
import com.see.truetransact.clientutil.ClientUtil;
import com.see.truetransact.commonutil.DateUtil;
import com.see.truetransact.transferobject.sysadmin.bank.BankTO;
import com.see.truetransact.ui.TrueTransactMain;
import com.see.truetransact.uicomponent.CObservable;
import java.util.Date;

public class BankOB extends CObservable {
    
    private String tdtBankOpeningDate = "";
    private String txtBankCode = "";
    private String txtBankName = "";
    private String txtWebsite = "";
    private String txtSiteIP = "";
    private String txtDataIP = "";
    private String txtCashLimit = "";
    private String cboConversion = "";
    private String cboTranPosting = "";
    private boolean rdoT2T_Yes = false;
    private boolean rdoT2T_No = false;
    private boolean rdoB2B_Yes = false;
    private boolean rdoB2B_No = false;
    private String cboHours = "";
    private String cboMins = "";
    private String cboBaseCurrency="";
    private String txtSupportEmail = "";
    
    private ComboBoxModel cbmConversion;
    private ComboBoxModel cbmTranPosting;
    private ComboBoxModel cbmHours;
    private ComboBoxModel cbmMins;
    private ComboBoxModel cbmBaseCurrency;
    
    private HashMap map;
    private ProxyFactory proxy;
    private HashMap lookUpHash;
    private HashMap keyValue;
    private ArrayList key;
    private ArrayList value;
    private HashMap authorizeMap;
    
    //--- To find whetehr the status is INSERT or UPDATE
    public String INSERT = "Insert";
    public String UPDATE = "Update";
    public String statusInsorUpd = INSERT;
    
    private int actionType;
    private int resultStatus;
    private Date curDate = null;
    
    private String lblStatus = ClientConstants.ACTION_STATUS[ClientConstants.ACTIONTYPE_EDIT];
    
    private final static com.see.truetransact.clientexception.ClientParseException parseException = com.see.truetransact.clientexception.ClientParseException.getInstance();
    
    private static BankOB bankOB;
    static {
        try {
            //log.info("Creating AccountCreationOB...");
            bankOB = new BankOB();
        } catch(Exception e) {
            parseException.logException(e,true);
        }
    }
    
    public BankOB()throws Exception{
        curDate = ClientUtil.getCurrentDate();
        map = new HashMap();
        map.put(CommonConstants.JNDI, "BankJNDI");
        map.put(CommonConstants.HOME, "com.see.truetransact.serverside.sysadmin.bank.BankHome");
        map.put(CommonConstants.REMOTE, "com.see.truetransact.serverside.sysadmin.bank.Bank");
        /**/
        
        try {
            proxy = ProxyFactory.createProxy();
        } catch (Exception e) {
            System.err.println( "Exception " + e + "Caught" );
            e.printStackTrace();
        }
        initUIComboBoxModel();
        
        fillDropdown();
    }
    
    public static BankOB getInstance() {
        return bankOB;
    }
    
    void setTxtBankCode(String txtBankCode){
        this.txtBankCode = txtBankCode;
        setChanged();
    }
    String getTxtBankCode(){
        return this.txtBankCode;
    }
    
    void setTxtBankName(String txtBankName){
        this.txtBankName = txtBankName;
        setChanged();
    }
    String getTxtBankName(){
        return this.txtBankName;
    }
    
    void setTxtWebsite(String txtWebsite){
        this.txtWebsite = txtWebsite;
        setChanged();
    }
    String getTxtWebsite(){
        return this.txtWebsite;
    }
    
    void setTxtSiteIP(String txtSiteIP){
        this.txtSiteIP = txtSiteIP;
        setChanged();
    }
    String getTxtSiteIP(){
        return this.txtSiteIP;
    }
    
    void setTxtDataIP(String txtDataIP){
        this.txtDataIP = txtDataIP;
        setChanged();
    }
    String getTxtDataIP(){
        return this.txtDataIP;
    }
    
    void setTxtCashLimit(String txtCashLimit){
        this.txtCashLimit = txtCashLimit;
        setChanged();
    }
    String getTxtCashLimit(){
        return this.txtCashLimit;
    }
    
    void setCboConversion(String cboConversion){
        this.cboConversion = cboConversion;
        setChanged();
    }
    String getCboConversion(){
        return this.cboConversion;
    }
    
    void setCboTranPosting(String cboTranPosting){
        this.cboTranPosting = cboTranPosting;
        setChanged();
    }
    String getCboTranPosting(){
        return this.cboTranPosting;
    }
    
    void setRdoT2T_Yes(boolean rdoT2T_Yes){
        this.rdoT2T_Yes = rdoT2T_Yes;
        setChanged();
    }
    boolean getRdoT2T_Yes(){
        return this.rdoT2T_Yes;
    }
    
    void setRdoT2T_No(boolean rdoT2T_No){
        this.rdoT2T_No = rdoT2T_No;
        setChanged();
    }
    boolean getRdoT2T_No(){
        return this.rdoT2T_No;
    }
    
    void setRdoB2B_Yes(boolean rdoB2B_Yes){
        this.rdoB2B_Yes = rdoB2B_Yes;
        setChanged();
    }
    boolean getRdoB2B_Yes(){
        return this.rdoB2B_Yes;
    }
    
    void setRdoB2B_No(boolean rdoB2B_No){
        this.rdoB2B_No = rdoB2B_No;
        setChanged();
    }
    boolean getRdoB2B_No(){
        return this.rdoB2B_No;
    }
    
    void setCboHours(String cboHours){
        this.cboHours = cboHours;
        setChanged();
    }
    String getCboHours(){
        return this.cboHours;
    }
    
    void setCboMins(String cboMins){
        this.cboMins = cboMins;
        setChanged();
    }
    String getCboMins(){
        return this.cboMins;
    }
    
    /** Getter for property cboBaseCurrency.
     * @return Value of property cboBaseCurrency.
     *
     */
    public java.lang.String getCboBaseCurrency() {
        return cboBaseCurrency;
    }
    
    /** Setter for property cboBaseCurrency.
     * @param cboBaseCurrency New value of property cboBaseCurrency.
     *
     */
    public void setCboBaseCurrency(java.lang.String cboBaseCurrency) {
        this.cboBaseCurrency = cboBaseCurrency;
        setChanged();
    }
    
    private void initUIComboBoxModel(){
        cbmConversion = new ComboBoxModel();
        cbmTranPosting = new ComboBoxModel();
        cbmHours = new ComboBoxModel();
        cbmMins = new ComboBoxModel();
        cbmBaseCurrency = new ComboBoxModel();
    }
    
    private void fillDropdown() throws Exception{
        try{
            //log.info("Inside FillDropDown");
            lookUpHash = new HashMap();
            lookUpHash.put(CommonConstants.MAP_NAME,null);
            final ArrayList lookup_keys = new ArrayList();
            
            lookup_keys.add("BANK.CURREANYCONV");
            lookup_keys.add("ACCTHEADMAIN.POSTMODE");
            lookup_keys.add("FOREX.CURRENCY");
            
            lookUpHash.put(CommonConstants.PARAMFORQUERY, lookup_keys);
            keyValue = com.see.truetransact.clientutil.ClientUtil.populateLookupData(lookUpHash);
            
            getKeyValue((HashMap)keyValue.get("BANK.CURREANYCONV"));
            cbmConversion = new ComboBoxModel(key,value);
            
            getKeyValue((HashMap)keyValue.get("ACCTHEADMAIN.POSTMODE"));
            cbmTranPosting = new ComboBoxModel(key,value);
            
            getKeyValue((HashMap)keyValue.get("FOREX.CURRENCY"));
            cbmBaseCurrency = new ComboBoxModel(key,value);
            
            setCbmHours();
            setCbmMins();
            
        }catch(NullPointerException e){
            e.printStackTrace();
        }catch(Exception e){
            e.printStackTrace();
        }
    }
    
    private void getKeyValue(HashMap keyValue)  throws Exception{
        key = (ArrayList)keyValue.get(CommonConstants.KEY);
        value = (ArrayList)keyValue.get(CommonConstants.VALUE);
    }
    
    private void setCbmHours(){
        key = new ArrayList();
        value = new ArrayList();
        key.add("");
        value.add("");
        int i = 1;
        while (i<25){
            key.add(Integer.toString(i));
            value.add(Integer.toString(i));
            i++;
        }
        cbmHours = new ComboBoxModel(key,value);
    }
    
    private void setCbmMins(){
        key = new ArrayList();
        value = new ArrayList();
        key.add("");
        value.add("");
        key.add("00");
        value.add("00");
        key.add("15");
        value.add("15");
        key.add("30");
        value.add("30");
        key.add("45");
        value.add("45");
        cbmMins = new ComboBoxModel(key,value);
    }
    
    /** Getter for property cbmConversion.
     * @return Value of property cbmConversion.
     *
     */
    public com.see.truetransact.clientutil.ComboBoxModel getCbmConversion() {
        return cbmConversion;
    }
    
    /** Setter for property cbmConversion.
     * @param cbmConversion New value of property cbmConversion.
     *
     */
    public void setCbmConversion(com.see.truetransact.clientutil.ComboBoxModel cbmConversion) {
        this.cbmConversion = cbmConversion;
    }
    
    /** Getter for property cbmTranPosting.
     * @return Value of property cbmTranPosting.
     *
     */
    public com.see.truetransact.clientutil.ComboBoxModel getCbmTranPosting() {
        return cbmTranPosting;
    }
    
    /** Setter for property cbmTranPosting.
     * @param cbmTranPosting New value of property cbmTranPosting.
     *
     */
    public void setCbmTranPosting(com.see.truetransact.clientutil.ComboBoxModel cbmTranPosting) {
        this.cbmTranPosting = cbmTranPosting;
    }
    
    /** Getter for property cbmHours.
     * @return Value of property cbmHours.
     *
     */
    public com.see.truetransact.clientutil.ComboBoxModel getCbmHours() {
        return cbmHours;
    }
    
    /** Setter for property cbmHours.
     * @param cbmHours New value of property cbmHours.
     *
     */
    public void setCbmHours(com.see.truetransact.clientutil.ComboBoxModel cbmHours) {
        this.cbmHours = cbmHours;
    }
    
    /** Getter for property cbmMins.
     * @return Value of property cbmMins.
     *
     */
    public com.see.truetransact.clientutil.ComboBoxModel getCbmMins() {
        return cbmMins;
    }
    
    /** Setter for property cbmMins.
     * @param cbmMins New value of property cbmMins.
     *
     */
    public void setCbmMins(com.see.truetransact.clientutil.ComboBoxModel cbmMins) {
        this.cbmMins = cbmMins;
    }
    
    /** Getter for property cbmBaseCurrency.
     * @return Value of property cbmBaseCurrency.
     *
     */
    public com.see.truetransact.clientutil.ComboBoxModel getCbmBaseCurrency() {
        return cbmBaseCurrency;
    }
    
    /** Setter for property cbmBaseCurrency.
     * @param cbmBaseCurrency New value of property cbmBaseCurrency.
     *
     */
    public void setCbmBaseCurrency(com.see.truetransact.clientutil.ComboBoxModel cbmBaseCurrency) {
        this.cbmBaseCurrency = cbmBaseCurrency;
    }
    
    private BankTO getBankTO(String command){
        BankTO objBankTO = new BankTO();
        objBankTO.setCommand(command);
        objBankTO.setBankCode(getTxtBankCode());
        objBankTO.setBankName(getTxtBankName());
        objBankTO.setWebSiteAddr(getTxtWebsite());
        objBankTO.setWebSiteIp(getTxtSiteIP());
        objBankTO.setSupportEmail(getTxtSupportEmail());
        objBankTO.setDataCenterIp(getTxtDataIP());
        objBankTO.setDayEndProcessTime(getCboHours()+":"+getCboMins());
        
        Date OpDt = DateUtil.getDateMMDDYYYY(getTdtBankOpeningDate());
        if(OpDt != null){
        Date opDate = (Date) curDate.clone();
        opDate.setDate(OpDt.getDate());
        opDate.setMonth(OpDt.getMonth());
        opDate.setYear(OpDt.getYear());
//        objBankTO.setOpeningDt(DateUtil.getDateMMDDYYYY(getTdtBankOpeningDate()));
        objBankTO.setOpeningDt(opDate);
        }else{
            objBankTO.setOpeningDt(DateUtil.getDateMMDDYYYY(getTdtBankOpeningDate()));
        }
        
        objBankTO.setLastModifiedBy(TrueTransactMain.USER_ID);
        objBankTO.setLastModifiedDt(curDate);
//        objBankTO.setAuthorizeStatus1(null);
//        objBankTO.setAuthorizeStatus2(null);
//        objBankTO.setAuthorizeBy1(null);
//        objBankTO.setAuthorizeBy2(null);
//        objBankTO.setAuthorizeDt1(null);
//        objBankTO.setAuthorizeDt2(null);
        //        objBankTO.setExchRateConv (getCboConversion());
        //        objBankTO.setTransPostingMethod (getCboTranPosting());
        //        objBankTO.setCashLimit (new Double (getTxtCashLimit()));
        //        objBankTO.setBaseCurrency(getCboBaseCurrency());
        //        if(getRdoT2T_Yes())
        //            objBankTO.setTellerTransAllowed ("Y");
        //        else
        //            objBankTO.setTellerTransAllowed ("N");
        //        if(getRdoB2B_Yes())
        //            objBankTO.setBranchTransAllowed ("Y");
        //        else
        //            objBankTO.setBranchTransAllowed ("N");
        
        return objBankTO;
        
    }
    
    private void setBankTO(BankTO objBankTO){
        setTxtBankCode(objBankTO.getBankCode());
        setTxtBankName(objBankTO.getBankName());
        setTxtWebsite(objBankTO.getWebSiteAddr());
        setTxtSiteIP(objBankTO.getWebSiteIp());
        setTxtSupportEmail(objBankTO.getSupportEmail());
        setTxtDataIP(objBankTO.getDataCenterIp());
        setTdtBankOpeningDate(DateUtil.getStringDate(objBankTO.getOpeningDt()));
        timeBreak(objBankTO.getDayEndProcessTime());
        //        setCboBaseCurrency(objBankTO.getBaseCurrency());
        //        setCboConversion (objBankTO.getExchRateConv ());
        //        setCboTranPosting (objBankTO.getTransPostingMethod ());
        //        setTxtCashLimit (objBankTO.getCashLimit ().toString());
        //        if(objBankTO.getTellerTransAllowed ().equals("Y"))
        //            setRdoT2T_Yes(true);
        //        else
        //            setRdoT2T_No(true);
        
        //        if(objBankTO.getBranchTransAllowed ().equals("Y"))
        //            setRdoB2B_Yes(true);
        //        else
        //            setRdoB2B_No(true);
        notifyObservers();
    }
    
    void  timeBreak(String str){
        int len = str.length();
        String hrs = "";
        String mins = "";
        if(len > 0){
            int index = str.indexOf(":");
            if (index > 0 ){
                hrs = str.substring(0,index);
                mins = str.substring(index+1);
            }else{
                if(index==0){
                    mins = str.substring(index+1);
                }else{
                    hrs = str;
                }
            }
            
        }
        setCboHours(hrs);
        setCboMins(mins);
    }
    /** Getter for property txSupportEmail.
     * @return Value of property txSupportEmail.
     *
     */
    public java.lang.String getTxtSupportEmail() {
        return txtSupportEmail;
    }
    
    /** Setter for property txSupportEmail.
     * @param txSupportEmail New value of property txSupportEmail.
     *
     */
    public void setTxtSupportEmail(java.lang.String txtSupportEmail) {
        this.txtSupportEmail = txtSupportEmail;
    }
    
    private HashMap populateBean(String command) {
        HashMap rpBeans = new HashMap();
        rpBeans.put("BankTO", getBankTO(command));
        
        rpBeans.put(CommonConstants.MODULE, getModule());
        rpBeans.put(CommonConstants.SCREEN, getScreen());
        return rpBeans;
    }
    
    public void populateData(HashMap whereMap) throws Exception {
        HashMap mapData = null;
            HashMap hash = (HashMap)(ClientUtil.executeQuery("getBankCount", null).get(0));
            int count = CommonUtil.convertObjToInt(hash.get("COUNT"));
            //--- If there is already a value, then it is Update else it is Insert
            if(count !=0){
                statusInsorUpd=UPDATE;
            } else {
                statusInsorUpd=INSERT;
            }
            
            mapData = proxy.executeQuery(whereMap, map);
            populateOB(mapData);
    }
    
    public int insertData() throws Exception {
        int state = 0;
        HashMap dataMap = populateBean(CommonConstants.TOSTATUS_INSERT);
        System.out.println("EJB Call");
        HashMap proxyResultMap = proxy.execute(dataMap, map);
        state = 1;
        setResult(actionType);
        actionType = ClientConstants.ACTIONTYPE_CANCEL;
        return state;
    }
    
    public int updateData() throws Exception {
        int state = 0;
        HashMap proxyResultMap = proxy.execute(populateBean(CommonConstants.TOSTATUS_UPDATE), map);
        state = 1;
        setResult(actionType);
        actionType = ClientConstants.ACTIONTYPE_CANCEL;
        return state;
    }
    
    public int deleteData() throws Exception {
        int state = 0;
        HashMap proxyResultMap = proxy.execute(populateBean(CommonConstants.TOSTATUS_DELETE), map);
        state = 1;
        setResult(actionType);
        actionType = ClientConstants.ACTIONTYPE_CANCEL;
        return state;
    }
    
    private void populateOB(HashMap mapData) {
        BankTO bankTO = new BankTO();
        
        if(((List)mapData.get("BankTO")).size() != 0){
            bankTO = (BankTO) ((List) mapData.get("BankTO")).get(0);
            setBankTO(bankTO);
        }
    }
    
    void doAction(String command){
        try{
            if(command.equals("Insert")){
                insertData();
            }else if(command.equals("Update")){
                updateData();
            }else if(command.equals("Delete")){
                deleteData();
            }else if(command.equals("FetchData")){
                HashMap where = new HashMap();
                where.put(CommonConstants.MAP_WHERE, getTxtBankCode());
                populateData(where);
                return;
            }
            resetOBFields();
        }catch(Exception e){
            setResult(ClientConstants.ACTIONTYPE_FAILED);
            e.printStackTrace();
        }
    }
    
    void resetOBFields(){
        //        setTxtBankCode("");
        //        setTxtBankName("");
        setTxtWebsite("");
        setTxtSiteIP("");
        setTxtSupportEmail("");
        setTxtDataIP("");
        setCboConversion("");
        setCboBaseCurrency("");
        setCboTranPosting("");
        setCboHours("");
        setCboMins("");
        setTxtCashLimit("");
        setTdtBankOpeningDate("");
        setRdoT2T_Yes(true);
        setRdoB2B_Yes(true);
        notifyObservers();
    }
    
    /**
     * Getter for property tdtBankOpeningDate.
     * @return Value of property tdtBankOpeningDate.
     */
    public java.lang.String getTdtBankOpeningDate() {
        return tdtBankOpeningDate;
    }
    
    /**
     * Setter for property tdtBankOpeningDate.
     * @param tdtBankOpeningDate New value of property tdtBankOpeningDate.
     */
    public void setTdtBankOpeningDate(java.lang.String tdtBankOpeningDate) {
        this.tdtBankOpeningDate = tdtBankOpeningDate;
    }
    
    
    /**
     * will return the current status of the process
     * @return the current status of the process
     */
    public String getLblStatus(){
        return lblStatus;
    }
    
    /**
     * will set the current status of the process
     * @param lblStatus is the current status of the process
     */
    public void setLblStatus(String lblStatus) {
        this.lblStatus = lblStatus;
        setChanged();
    }
    /**
     * will reset the current status to Cancel mode
     */
    public void resetStatus(){
        this.setLblStatus(ClientConstants.ACTION_STATUS[0]);
    }
    
    /** To set the status based on ActionType, either New, Edit, etc., */
    public void setStatus(){
        this.setLblStatus( ClientConstants.ACTION_STATUS[this.getActionType()]);
        notifyObservers();
    }
    
    /** To update the Status based on result performed by doAction() method */
    public void setResultStatus(){
        this.setLblStatus(ClientConstants.RESULT_STATUS[this.getResult()]);
        notifyObservers();
    }
    /**
     * will return the action type
     * @return the appropriate action taken
     */
    public int getActionType(){
        return actionType;
    }
    
    /**
     * will set the action type whether it is new or edit or delete
     * @param actionType is new or edit or delete
     */
    public void setActionType(int actionType) {
        this.actionType = actionType;
        setChanged();
    }
    
    /**
     * It will set the result of the query executed
     * @param resultStatus is the integer value which gives whether the data are inserted or updated or
     * deleted or the execution is failed
     */
    public void setResult(int resultStatus) {
        this.resultStatus = resultStatus;
        setChanged();
    }
    
    /**
     * Return the result of the executed query
     * @return the result of the executed query
     */
    public int getResult(){
        return resultStatus;
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
    
}
