/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition.
 * 
 *
 * SecurityOB.java
 *
 * Created on July 6, 2004, 12:23 PM
 */

package com.see.truetransact.ui.customer.security;

/**
 *
 * @author  shanmuga
 *
 */

import com.see.truetransact.clientexception.ClientParseException;
import com.see.truetransact.clientproxy.ProxyFactory;
import com.see.truetransact.clientproxy.ProxyParameters;
import com.see.truetransact.clientutil.ClientConstants;
import com.see.truetransact.clientutil.ClientUtil;
import com.see.truetransact.clientutil.ComboBoxModel;
import com.see.truetransact.clientutil.EnhancedTableModel;
import com.see.truetransact.clientutil.TableUtil;
import com.see.truetransact.commonutil.CommonConstants;
import com.see.truetransact.commonutil.DateUtil;
import com.see.truetransact.commonutil.CommonUtil;
import com.see.truetransact.commonutil.TTException;
import com.see.truetransact.transferobject.customer.security.SecurityTO;
import com.see.truetransact.ui.TrueTransactMain;
import com.see.truetransact.uicomponent.CObservable;
import com.see.truetransact.uicomponent.COptionPane;

import org.apache.log4j.Logger;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Date;

public class SecurityOB extends CObservable{
    
    /** Creates a new instance of SecurityOB */
    private SecurityOB() throws Exception{
        curDate = ClientUtil.getCurrentDate();
        setOperationMap();
        try {
            proxy = ProxyFactory.createProxy();
        } catch (Exception e) {
            parseException.logException(e,true);
            e.printStackTrace();
            log.info("SecurityInsuranceOB: "+e);
        }
        securityOB();
    }
    
    private int actionType;
    private int resultStatus;
    
    private String lblStatus = ClientConstants.ACTION_STATUS[ClientConstants.ACTIONTYPE_CANCEL];
    
    private       static SecurityOB securityOB;
    private       static InsuranceOB insuranceOB;
    
    private final static Logger log = Logger.getLogger(SecurityOB.class);
    private final static ClientParseException parseException = ClientParseException.getInstance();
    
    private double totalSecurityValue = 0;
    private double securityValue = 0;
    
    private ProxyFactory proxy = null;
    
    private final   SecurityInsuranceRB objSecurityInsuranceRB = new SecurityInsuranceRB();
    
    private ArrayList key;
    private ArrayList value;
    
    private final   String  ALL = "ALL";
    private final   String  ALL_VALUES = "ALL_VALUES";
    private final   String  ASON = "ASON";
    private final   String  AUTHORIZE_BY = "AUTHORIZE_BY";
    private final   String  AUTHORIZE_DT = "AUTHORIZE_DT";
    private final   String  AUTHORIZE_STATUS = "AUTHORIZE_STATUS";
    private final   String  AUTHORIZE_REMARKS = "AUTHORIZE_REMARKS";
    private final   String  AVAL_SEC_VAL = "AVAL_SEC_VAL";
    private final   String  CITY = "CITY";
    private final   String  COLLATERAL = "COLLATERAL";
    private final   String  COMMAND = "COMMAND";
    private final   String  COMMODITY_ITEM = "COMMODITY_ITEM";
    private final   String  CUSTOMER_ID = "CUSTOMER_ID";
    private final   String  DATE_CHARGE = "DATE_CHARGE";
    private final   String  DATE_INSPECTION = "DATE_INSPECTION";
    private final   String  EMAIL_ID = "EMAIL_ID";
    private final   String  FROM_DATE = "FROM_DATE";
    private final   String  INSERT = "INSERT";
    private final   String  INSURANCE_NO = "INSURANCE_NO";
    private final   String  MILL_INDUS = "MILL_INDUS";
    private final   String  NATURE_CHARGE = "NATURE_CHARGE";
    private final   String  NO = "N";
    private final   String  OPTION = "OPTION";
    private final   String  PARTICULARS = "PARTICULARS";
    private final   String  PHONE_NUMBER = "PHONE_NUMBER";
    private final   String  PIN = "PIN";
    private final   String  PIN_CODE = "PIN_CODE";
    private final   String  POLICY_AMT = "POLICY_AMT";
    private final   String  PRIMARY = "PRIMARY";
    private final   String  SECURITY_CATEGORY = "SECURITY_CATEGORY";
    private final   String  SECURITY_DETAILS = "SECURITY_DETAILS";
    private final   String  SECURITY_NO = "SECURITY_NO";
    private final   String  SECURITY_TYPE = "SECURITY_TYPE";
    private final   String  SECURITY_VALUE = "SECURITY_VALUE";
    private final   String  SLNO = "SLNO";
    private final   String  STATE = "STATE";
    private final   String  STREET = "STREET";
    private final   String  STOCK_STATE_FREQ = "STOCK_STATE_FREQ";
    private final   String  TABLE_VALUES = "TABLE_VALUES";
    private final   String  TO_DATE = "TO_DATE";
    private final   String  UPDATE = "UPDATE";
    private final   String  WEIGHT = "WEIGHT";
    private final   String  GROSSWEIGHT="GROSSWEIGHT";
    private final   String  YES = "Y";
    private final   String  STATUS_BY = "STATUS_BY";
    private final   ArrayList securityTabTitle = new ArrayList();       //  Table Title of Security
    private final   ArrayList securityLoanTabTitle = new ArrayList();
    
    private ArrayList insuranceNoSelectedNSecurityLevel;
    
    private ArrayList securityTabValues = new ArrayList();
    private ArrayList securityEachTabRecord;
    
    private LinkedHashMap securityAll = new LinkedHashMap();           // Both displayed and hidden values in the table
    private HashMap securityEachRecord;
    
    private HashMap lookUpHash;
    private HashMap operationMap;
    private HashMap keyValue;
    private HashMap authorizeMap;
    
    private EnhancedTableModel tblSecurityTab;
    private EnhancedTableModel tblSecurityLoanTab;
    
    private ComboBoxModel cbmSecurityCate;
    private ComboBoxModel cbmNatureCharge;
    private ComboBoxModel cbmForMillIndus;
    private ComboBoxModel cbmStockStateFreq;
    private ComboBoxModel cbmCity;
    private ComboBoxModel cbmInsuranceNo;
    
    private TableUtil tableUtilSecurity = new TableUtil();
    
    private String strStoredInsuranceNo = "";
    private String cboInsuranceNo = "";
    private String txtSecurityNo = "";
    private String cboSecurityCate = "";
    private boolean rdoSecurityType_Primary = false;
    private boolean rdoSecurityType_Collateral = false;
    private String txtSecurityValue = "";
    private String tdtAson = "";
    private String txtParticulars = "";
    private String cboNatureCharge = "";
    private String tdtDateCharge = "";
    private boolean chkSelCommodityItem = false;
    private String cboForMillIndus = "";
    private String tdtDateInspection = "";
    private String cboStockStateFreq = "";
    private String tdtFromDate = "";
    private String tdtToDate = "";
    private String txtAvalSecVal = "";
    private String txtWeight = "";
    private String txtGrossWeight="";
    private String txtTotalSecurity_Value = "";
    private String lblCustID_Disp = "";
    private String lblCustName_Disp = "";
    private String lblCustEmail_ID_Disp = "";
    private String lblCustCity_Disp = "";
    private String lblCustStreet_Disp = "";
    private String lblCustPin_Disp = "";
    private String statusBy = ""; 
     private String authorizeStatus1 = ""; 
     Date curDate = null;
    static {
        try {
            securityOB = new SecurityOB();
        } catch(Exception e) {
            log.info("try: " + e);
            parseException.logException(e,true);
        }
    }
    
    private void securityOB()  throws Exception{
        insuranceOB = InsuranceOB.getInstance();
        insuranceNoSelectedNSecurityLevel = new ArrayList();
        fillDropdown();
        setSecurityTabTitle();
        setSecurityLoanTabTitle();
        tableUtilSecurity.setAttributeKey(SLNO);
        tblSecurityTab = new EnhancedTableModel(null, securityTabTitle);
        tblSecurityLoanTab = new EnhancedTableModel(null, securityLoanTabTitle);
    }
    
    public static SecurityOB getInstance() {
        return securityOB;
    }
    public void fillDropdown() throws Exception{
        lookUpHash = new HashMap();
        lookUpHash.put(CommonConstants.MAP_NAME,null);
        
        ArrayList lookup_keys = new ArrayList();
        lookup_keys.add("TERM_LOAN.RISK_NATURE");
        lookup_keys.add("FREQUENCY");
        lookup_keys.add("CUSTOMER.CITY");
        lookup_keys.add("TERM_LOAN.SECURITY_CATEGORY");
        lookup_keys.add("TERM_LOAN.CHARGE_NATURE");
        lookup_keys.add("TERM_LOAN.MILLS_INDUSTRIAL_USERS");
        
        lookUpHash.put(CommonConstants.PARAMFORQUERY, lookup_keys);
        
        keyValue = ClientUtil.populateLookupData(lookUpHash);
        
        getKeyValue((HashMap)keyValue.get("FREQUENCY"));
        setCbmStockStateFreq(new ComboBoxModel(key,value));
        
        getKeyValue((HashMap)keyValue.get("TERM_LOAN.SECURITY_CATEGORY"));
        setCbmSecurityCate(new ComboBoxModel(key,value));
        
        getKeyValue((HashMap)keyValue.get("TERM_LOAN.CHARGE_NATURE"));
        setCbmNatureCharge(new ComboBoxModel(key,value));
        
        getKeyValue((HashMap)keyValue.get("TERM_LOAN.MILLS_INDUSTRIAL_USERS"));
        setCbmForMillIndus(new ComboBoxModel(key,value));
        
        getKeyValue((HashMap)keyValue.get("TERM_LOAN.RISK_NATURE"));
        insuranceOB.setCbmNatureRisk(new ComboBoxModel(key,value));
        
        getKeyValue((HashMap)keyValue.get("CUSTOMER.CITY"));
        setCbmCity(new ComboBoxModel(key,value));
        
        setBlankKeyValue();
        setCbmInsuranceNo(new ComboBoxModel(key, value));
        
        setBlankKeyValue();
        insuranceOB.setCbmSecurityNo_Insurance(new ComboBoxModel(key,value));
        
        lookup_keys = null;
    }
    
    private void setOperationMap() throws Exception{
        operationMap = new HashMap();
        operationMap.put(CommonConstants.JNDI, "SecurityInsuranceJNDI");
        operationMap.put(CommonConstants.HOME, "SecurityInsuranceHome");
        operationMap.put(CommonConstants.REMOTE, "SecurityInsurance");
    }
    
    private void setBlankKeyValue(){
        key = new ArrayList();
        key.add("");
        value = new ArrayList();
        value.add("");
    }
    
    private void getKeyValue(HashMap keyValue)  throws Exception{
        key = (ArrayList)keyValue.get(CommonConstants.KEY);
        value = (ArrayList)keyValue.get(CommonConstants.VALUE);
    }
    
    private void setSecurityLoanTabTitle() throws Exception{
        try{
            securityLoanTabTitle.add(objSecurityInsuranceRB.getString("tblColumnLoanBorrowNo"));
            securityLoanTabTitle.add(objSecurityInsuranceRB.getString("tblColumnLoanAcctNo"));
            securityLoanTabTitle.add(objSecurityInsuranceRB.getString("tblColumnLoanFromDt"));
            securityLoanTabTitle.add(objSecurityInsuranceRB.getString("tblColumnLoanToDt"));
            securityLoanTabTitle.add(objSecurityInsuranceRB.getString("tblColumnLoanMargin"));
            securityLoanTabTitle.add(objSecurityInsuranceRB.getString("tblColumnLoanAmt"));
        }catch(Exception e){
            log.info("Exception in setSecurityLoanTabTitle(): "+e);
            parseException.logException(e,true);
        }
    }
    
    private void setSecurityTabTitle() throws Exception{
        try{
            securityTabTitle.add(objSecurityInsuranceRB.getString("tblColumnSecurityNo"));
            securityTabTitle.add(objSecurityInsuranceRB.getString("tblColumnSecurityCategory"));
            securityTabTitle.add(objSecurityInsuranceRB.getString("tblColumnSecurityValue"));
            securityTabTitle.add(objSecurityInsuranceRB.getString("tblColumnSecurityAson"));
        }catch(Exception e){
            log.info("Exception in setSecurityTabTitle(): "+e);
            parseException.logException(e,true);
        }
    }
    
    public ArrayList getSecurityTabTitle(){
        return this.securityTabTitle;
    }
    
    public void resetForm(){
        resetAllSecurityDetails();
        removeAllInsuranceNo();
        resetInsuranceNoSelectedNSecurityLevelList();
        insuranceOB.resetAllInsuranceDetails();
        resetSecurityCTable();
        setTotalSecurityValue(0);
        displayTotalSecurityValue();
        ttNotifyObservers();
    }
    
    public void resetInsuranceNoSelectedNSecurityLevelList(){
        insuranceNoSelectedNSecurityLevel = null;
        insuranceNoSelectedNSecurityLevel = new ArrayList();
    }
    public void resetSecurityLoanTab(){
        tblSecurityLoanTab = new EnhancedTableModel(null, securityLoanTabTitle);
    }
    public void resetAllSecurityDetails(){
        resetSecurityDetails();
        setSecurityValue(0);
        setTxtTotalSecurity_Value("");
        setLblCustCity_Disp("");
        setLblCustID_Disp("");
        setLblCustName_Disp("");
        setLblCustEmail_ID_Disp("");
        setLblCustPin_Disp("");
        setLblCustStreet_Disp("");
        resetSecurityLoanTab();
    }
    
    public void resetSecurityDetails(){
        setTxtSecurityNo("");
        setCboSecurityCate("");
        setRdoSecurityType_Primary(false);
        setRdoSecurityType_Collateral(false);
        setTxtSecurityValue("");
        setTdtAson("");
        setTxtParticulars("");
        setCboNatureCharge("");
        setTdtDateCharge("");
        setChkSelCommodityItem(false);
        setCboForMillIndus("");
        setTdtFromDate("");
        setTdtToDate("");
        setTxtAvalSecVal("");
        setCboStockStateFreq("");
        setCboInsuranceNo("");
        setStrStoredInsuranceNo("");
        setTdtDateInspection("");
        setTxtWeight("");
    }
    
    
    public void removeAllInsuranceNo(){
        try{
            // Remove all keys and values before add
            for (int i = getCbmInsuranceNo().getSize() - 1;i >= 0;--i){
                getCbmInsuranceNo().removeKeyAndElement(getCbmInsuranceNo().getKey(i));
            }
            // To add the Insurance Nos. in ComboBoxModel in Security Details
            getCbmInsuranceNo().addKeyAndElement("", "");
            resetInsuranceNoSelectedNSecurityLevelList();
        }catch(Exception e){
            log.info("Error in removeAllInsuranceNo..."+e);
            parseException.logException(e,true);
        }
    }
    
    
    public void resetSecurityCTable(){
        tblSecurityTab.setDataArrayList(null, securityTabTitle);
        tableUtilSecurity = new TableUtil();
        tableUtilSecurity.setAttributeKey(SLNO);
    }
    
    public void populateCustDetails(HashMap map){
        HashMap mapData = null;
        try{
            HashMap transactionMap = new HashMap();
            HashMap retrieve = new HashMap();
            String strCustID = CommonUtil.convertObjToStr(map.get("CUSTOMER ID"));
            String strCustName = CommonUtil.convertObjToStr(map.get("CUSTOMER NAME"));
            transactionMap.put("custId", strCustID);
            setLblCustID_Disp(strCustID);
            setLblCustName_Disp(strCustName);
            List resultList = ClientUtil.executeQuery("getSelectCustomerAddress", transactionMap);
            if (resultList.size() > 0){
                retrieve = (HashMap) resultList.get(0);
                setLblCustStreet_Disp(CommonUtil.convertObjToStr(retrieve.get(STREET)));
                setLblCustCity_Disp(CommonUtil.convertObjToStr(retrieve.get(CITY)));
                setLblCustPin_Disp(CommonUtil.convertObjToStr(retrieve.get(PIN_CODE)));
                setLblCustEmail_ID_Disp(CommonUtil.convertObjToStr(retrieve.get(EMAIL_ID)));
            }
            
            retrieve = null;
            transactionMap = null;
            resultList = null;
        }catch(Exception e){
            log.info("Exception caught in populateCustDetails: "+e);
            parseException.logException(e,true);
        }
    }
    public void populateData(HashMap whereMap) {
        log.info("In populateData..."+whereMap);
        HashMap mapData = null;
        try {
            mapData =  (HashMap) proxy.executeQuery(whereMap, operationMap);
            log.info("proxy.executeQuery is working fine"+mapData);
            populateOB(mapData);
            populateCustDetails(whereMap);
        } catch( Exception e ) {
            log.info("Exception caught in populateData"+e);
            parseException.logException(e,true);
        }
    }
    
    private void populateOB(HashMap mapData) {
        log.info("In populateOB...");
        resetSecurityDetails();
        getTblSecurityTab().setDataArrayList(null, getSecurityTabTitle());
        removeAllInsuranceNo();
        setSecurityTO((ArrayList) (mapData.get("SecurityTO")));
        
        insuranceOB.resetInsuranceDetails();
        insuranceOB.getTblInsuranceTab().setDataArrayList(null, insuranceOB.getInsuranceTabTitle());
        insuranceOB.setInsuranceTO((ArrayList) (mapData.get("InsuranceTO")));
        
        ttNotifyObservers();
    }
    
    public void setSecurityTO(ArrayList objSecurityTOList){
        try{
            SecurityTO objSecurityTO;
            HashMap securityRecordMap;
            String strCustID = "";
            ArrayList securityRecordList;
            ArrayList securityCTableValues = new ArrayList();
            ArrayList removedValues = new ArrayList();
            LinkedHashMap allLocalRecs = new LinkedHashMap();
            // To set the Total Security Value as ZERO
            setTotalSecurityValue(0);
            
            // Remove all keys and values before add
            for (int i = insuranceOB.getCbmSecurityNo_Insurance().getSize() - 1;i >= 0;--i){
                insuranceOB.getCbmSecurityNo_Insurance().removeKeyAndElement(insuranceOB.getCbmSecurityNo_Insurance().getKey(i));
            }
            // To add the Sanction Nos. in ComboBoxModel in Insurance Details
            insuranceOB.getCbmSecurityNo_Insurance().addKeyAndElement("", "");
//            insuranceOB.getCbmSecurityNo_Insurance().addKeyAndElement(ALL, "All");
            // To retrieve the Security Details from the Database
            for (int i = objSecurityTOList.size() - 1,j = 0;i >= 0;--i,++j){
                objSecurityTO = (SecurityTO) objSecurityTOList.get(j);
                securityRecordMap  = new HashMap();
                securityRecordList = new ArrayList();
                
                securityRecordList.add(CommonUtil.convertObjToStr(objSecurityTO.getSecurityNo()));
                securityRecordList.add(CommonUtil.convertObjToStr(getCbmSecurityCate().getDataForKey(objSecurityTO.getSecurityCategory())));
                securityRecordList.add(CommonUtil.convertObjToStr(objSecurityTO.getSecurityValue()));
                securityRecordList.add(DateUtil.getStringDate(objSecurityTO.getSecurityValueOn()));
                
                // To add the Sanction Nos. in ComboBoxModel in Insurance Details
                insuranceOB.getCbmSecurityNo_Insurance().addKeyAndElement(CommonUtil.convertObjToStr(objSecurityTO.getSecurityNo()), CommonUtil.convertObjToStr(objSecurityTO.getSecurityNo()));
                securityCTableValues.add(securityRecordList);
                
                if (CommonUtil.convertObjToStr(objSecurityTO.getInsuranceNo()).length() > 0 && !(CommonUtil.convertObjToStr(objSecurityTO.getInsuranceNo()).equals("0"))){
                    getInsuranceNoSelectedNSecurityLevel().add(CommonUtil.convertObjToStr(objSecurityTO.getInsuranceNo()));
                    securityRecordMap.put(INSURANCE_NO, CommonUtil.convertObjToStr(objSecurityTO.getInsuranceNo()));
                }else{
                    securityRecordMap.put(INSURANCE_NO, "");
                }
                securityRecordMap.put(SLNO, CommonUtil.convertObjToStr(objSecurityTO.getSecurityNo()));
                securityRecordMap.put(SECURITY_TYPE, CommonUtil.convertObjToStr(objSecurityTO.getSecurityType()));
                securityRecordMap.put(SECURITY_VALUE, CommonUtil.convertObjToStr(objSecurityTO.getSecurityValue()));
                securityRecordMap.put(WEIGHT, CommonUtil.convertObjToStr(objSecurityTO.getWeight()));
                securityRecordMap.put(GROSSWEIGHT, CommonUtil.convertObjToStr(objSecurityTO.getGrossWeight()));
                // Add Security Value to find the average security value
                calculateSecurityValue(CommonUtil.convertObjToDouble(objSecurityTO.getAvailableSecurityValue()).doubleValue());
                addSecurityValue(getSecurityValue());
                
                securityRecordMap.put(CUSTOMER_ID, objSecurityTO.getCustId());
                strCustID = objSecurityTO.getCustId();
                securityRecordMap.put(ASON, DateUtil.getStringDate(objSecurityTO.getSecurityValueOn()));
                securityRecordMap.put(PARTICULARS, objSecurityTO.getParticulars());
                securityRecordMap.put(SECURITY_CATEGORY, CommonUtil.convertObjToStr(objSecurityTO.getSecurityCategory()));
                securityRecordMap.put(TO_DATE, DateUtil.getStringDate(objSecurityTO.getToDt()));
                securityRecordMap.put(FROM_DATE, DateUtil.getStringDate(objSecurityTO.getFromDt()));
                securityRecordMap.put(AVAL_SEC_VAL, CommonUtil.convertObjToStr(objSecurityTO.getAvailableSecurityValue()));
                securityRecordMap.put(NATURE_CHARGE, CommonUtil.convertObjToStr(objSecurityTO.getChargeNature()));
                securityRecordMap.put(DATE_CHARGE, DateUtil.getStringDate(objSecurityTO.getChargeDt()));
                securityRecordMap.put(COMMODITY_ITEM, CommonUtil.convertObjToStr(objSecurityTO.getSelectiveCommodity()));
                securityRecordMap.put(MILL_INDUS, CommonUtil.convertObjToStr(objSecurityTO.getIndustrialUsers()));
                securityRecordMap.put(DATE_INSPECTION, DateUtil.getStringDate(objSecurityTO.getInspectionDt()));
                securityRecordMap.put(STOCK_STATE_FREQ, CommonUtil.convertObjToStr(objSecurityTO.getStockStatFreq()));
                securityRecordMap.put(AUTHORIZE_BY, CommonUtil.convertObjToStr(objSecurityTO.getAuthorizeBy()));
                securityRecordMap.put(AUTHORIZE_DT, DateUtil.getStringDate(objSecurityTO.getAuthorizeDt()));
                securityRecordMap.put(AUTHORIZE_STATUS, CommonUtil.convertObjToStr(objSecurityTO.getAuthorizeStatus()));
                securityRecordMap.put(AUTHORIZE_REMARKS, CommonUtil.convertObjToStr(objSecurityTO.getAuthorizeRemarks()));
                securityRecordMap.put(STATUS_BY,CommonUtil.convertObjToStr(objSecurityTO.getStatusBy()));
                System.out.println("$$MAP"+securityRecordMap);
                securityRecordMap.put(COMMAND, UPDATE);
                
                allLocalRecs.put(CommonUtil.convertObjToStr(objSecurityTO.getSecurityNo()), securityRecordMap);  //List of values corresponding to the table
                setStatusBy(objSecurityTO.getStatusBy());
                setAuthorizeStatus1(objSecurityTO.getAuthorizeStatus());
                securityRecordMap  = null;
                securityRecordList = null;
                objSecurityTO = null;
            }
            securityTabValues.clear();
            securityAll.clear();
            securityTabValues = securityCTableValues;
            securityAll = allLocalRecs;
            tblSecurityTab.setDataArrayList(securityTabValues, securityTabTitle);
            displayTotalSecurityValue();
            tableUtilSecurity.setRemovedValues(removedValues);
            tableUtilSecurity.setAllValues(securityAll);
            tableUtilSecurity.setTableValues(securityTabValues);
            setMax_Del_Security_No(strCustID);
            securityCTableValues = null;
            allLocalRecs = null;
        }catch(Exception e){
            log.info("Error in setSecurityTO()..."+e);
            parseException.logException(e,true);
        }
    }
    
    private void setMax_Del_Security_No(String strCustID){
        try{
            HashMap transactionMap = new HashMap();
            HashMap retrieve = new HashMap();
            transactionMap.put("custId", strCustID);
            List resultList = ClientUtil.executeQuery("getSelectSecurityMaxSLNO", transactionMap);
            if (resultList.size() > 0){
                retrieve = (HashMap) resultList.get(0);
                tableUtilSecurity.setStr_MAX_DEL_SL_NO(CommonUtil.convertObjToStr(retrieve.get("MAX_SECURITY_NO")));
            }
            retrieve = null;
            transactionMap = null;
            resultList = null;
        }catch(Exception e){
            log.info("Error In setMax_Del_Security_No: "+e);
            parseException.logException(e,true);
        }
    }
    
    public void doAction() {
        log.info("In doAction...");
        try {
            //If actionType such as NEW, EDIT, DELETE, then proceed
            if( actionType != ClientConstants.ACTIONTYPE_CANCEL ){
                //If actionType has got propervalue then doActionPerform, else throw error
                if( getCommand() != null || getAuthorizeMap() != null){
                    log.info("before doActionPerform...");
                    doActionPerform();
                }
                else{
                    log.info("In doAction()-->getCommand() is null:" );
                }
            }
            else
                log.info("In doAction()-->actionType is null:" );
        } catch (Exception e) {
            setResult(ClientConstants.ACTIONTYPE_FAILED);
            log.info("Error in doAction():"+e);
            parseException.logException(e,true);
        }
    }
    
    
    private String getCommand() throws Exception{
        log.info("getCommand");
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
    
    private void doActionPerform() throws Exception{
        log.info("doActionPerform");
        HashMap objSecurityTOMap = setSecurity(getCommand());
        HashMap objInsuranceTOMap = insuranceOB.setInsurance(getCommand());
        
        HashMap data = new HashMap();
        
        data.put("SecurityTO", objSecurityTOMap);
        data.put("InsuranceTO", objInsuranceTOMap);
        if (getAuthorizeMap() != null){
            data.put(CommonConstants.AUTHORIZEMAP, getAuthorizeMap());
        }
        data.put(CommonConstants.MODULE, getModule());
        data.put(CommonConstants.SCREEN, getScreen());
        
        HashMap proxyResultMap = proxy.execute(data,operationMap);
        
        log.info("doActionPerform is over...");
        setResult(actionType);
        
        actionType = ClientConstants.ACTIONTYPE_CANCEL;
        
        objSecurityTOMap = null;
        objInsuranceTOMap = null;
        data = null;
    }
    
    public HashMap setSecurity(String strCommand){
        HashMap securityMap = new HashMap();
        try{
            SecurityTO objSecurityTO;
            java.util.Set keySet =  securityAll.keySet();
            Object[] objKeySet = (Object[]) keySet.toArray();
            HashMap oneRecord;
            
            // To set the values for Security Transfer Object
            for (int i = keySet.size() - 1,j = 0;i >= 0;--i,++j){
                oneRecord = (HashMap) securityAll.get(objKeySet[j]);
                objSecurityTO = new SecurityTO();
                objSecurityTO.setCustId(CommonUtil.convertObjToStr(oneRecord.get(CUSTOMER_ID)));
                
                Date DcDt = DateUtil.getDateMMDDYYYY(CommonUtil.convertObjToStr(oneRecord.get(DATE_CHARGE)));
                if(DcDt != null){
                Date dcDate = (Date) curDate.clone();
                dcDate.setDate(DcDt.getDate());
                dcDate.setMonth(DcDt.getMonth());
                dcDate.setYear(DcDt.getYear());
                objSecurityTO.setChargeDt(dcDate);
                }else{
                    objSecurityTO.setChargeDt(DateUtil.getDateMMDDYYYY(CommonUtil.convertObjToStr(oneRecord.get(DATE_CHARGE))));
                }
                
                objSecurityTO.setChargeNature(CommonUtil.convertObjToStr(oneRecord.get(NATURE_CHARGE)));
                objSecurityTO.setCommand(CommonUtil.convertObjToStr(oneRecord.get(COMMAND)));
                
                Date FdDt = DateUtil.getDateMMDDYYYY(CommonUtil.convertObjToStr(oneRecord.get(FROM_DATE)));
                if(FdDt != null){
                Date fdDate = (Date) curDate.clone();
                fdDate.setDate(FdDt.getDate());
                fdDate.setMonth(FdDt.getMonth());
                fdDate.setYear(FdDt.getYear());
                objSecurityTO.setFromDt(fdDate);
                }else{
                    objSecurityTO.setFromDt(DateUtil.getDateMMDDYYYY(CommonUtil.convertObjToStr(oneRecord.get(FROM_DATE))));
                }
                objSecurityTO.setIndustrialUsers(CommonUtil.convertObjToStr(oneRecord.get(MILL_INDUS)));
                
                Date DiDt = DateUtil.getDateMMDDYYYY(CommonUtil.convertObjToStr(oneRecord.get(DATE_INSPECTION)));
                if(DiDt != null){
                Date diDate = (Date) curDate.clone();
                diDate.setDate(DiDt.getDate());
                diDate.setMonth(DiDt.getMonth());
                diDate.setYear(DiDt.getYear());
                objSecurityTO.setInspectionDt(diDate);
                }else{
                    objSecurityTO.setInspectionDt(DateUtil.getDateMMDDYYYY(CommonUtil.convertObjToStr(oneRecord.get(DATE_INSPECTION))));
                }
                objSecurityTO.setAvailableSecurityValue(CommonUtil.convertObjToDouble(oneRecord.get(AVAL_SEC_VAL)));
                objSecurityTO.setParticulars(CommonUtil.convertObjToStr(oneRecord.get(PARTICULARS)));
                objSecurityTO.setSecurityCategory(CommonUtil.convertObjToStr(oneRecord.get(SECURITY_CATEGORY)));
                objSecurityTO.setSecurityNo(CommonUtil.convertObjToDouble(oneRecord.get(SLNO)));
                objSecurityTO.setSecurityType(CommonUtil.convertObjToStr(oneRecord.get(SECURITY_TYPE)));
                objSecurityTO.setSecurityValue(CommonUtil.convertObjToDouble(oneRecord.get(SECURITY_VALUE)));
                objSecurityTO.setWeight(CommonUtil.convertObjToDouble(oneRecord.get(WEIGHT)));
                objSecurityTO.setGrossWeight(CommonUtil.convertObjToDouble(oneRecord.get(GROSSWEIGHT)));
                
                Date AsDt = DateUtil.getDateMMDDYYYY(CommonUtil.convertObjToStr(oneRecord.get(ASON)));
                if(AsDt != null){
                Date asDate = (Date) curDate.clone();
                asDate.setDate(AsDt.getDate());
                asDate.setMonth(AsDt.getMonth());
                asDate.setYear(AsDt.getYear());
                objSecurityTO.setSecurityValueOn(asDate);
                }else{
                    objSecurityTO.setSecurityValueOn(DateUtil.getDateMMDDYYYY(CommonUtil.convertObjToStr(oneRecord.get(ASON))));
                }
                objSecurityTO.setSelectiveCommodity(CommonUtil.convertObjToStr(oneRecord.get(COMMODITY_ITEM)));
                objSecurityTO.setStatus(CommonUtil.convertObjToStr(oneRecord.get(COMMAND)));
                objSecurityTO.setStockStatFreq(CommonUtil.convertObjToDouble(oneRecord.get(STOCK_STATE_FREQ)));
                objSecurityTO.setInsuranceNo(CommonUtil.convertObjToDouble(oneRecord.get(INSURANCE_NO)));
                objSecurityTO.setAuthorizeBy(CommonUtil.convertObjToStr(oneRecord.get(AUTHORIZE_BY)));
                           
                Date AdDt = DateUtil.getDateMMDDYYYY(CommonUtil.convertObjToStr(oneRecord.get(AUTHORIZE_DT)));
                if(AdDt != null){
                Date adDate = (Date) curDate.clone();
                adDate.setDate(AdDt.getDate());
                adDate.setMonth(AdDt.getMonth());
                adDate.setYear(AdDt.getYear());
                objSecurityTO.setAuthorizeDt(adDate);
                }else{
                    objSecurityTO.setAuthorizeDt(DateUtil.getDateMMDDYYYY(CommonUtil.convertObjToStr(oneRecord.get(AUTHORIZE_DT))));
                }
                objSecurityTO.setAuthorizeRemarks(CommonUtil.convertObjToStr(oneRecord.get(AUTHORIZE_REMARKS)));
                objSecurityTO.setAuthorizeStatus(CommonUtil.convertObjToStr(oneRecord.get(AUTHORIZE_STATUS)));
                
                Date ToDt = DateUtil.getDateMMDDYYYY(CommonUtil.convertObjToStr(oneRecord.get(TO_DATE)));
                if(ToDt != null){
                Date toDate = (Date) curDate.clone();
                toDate.setDate(ToDt.getDate());
                toDate.setMonth(ToDt.getMonth());
                toDate.setYear(ToDt.getYear());
                objSecurityTO.setToDt(toDate);
                }else{
                    objSecurityTO.setToDt(DateUtil.getDateMMDDYYYY(CommonUtil.convertObjToStr(oneRecord.get(TO_DATE))));
                }
                if (oneRecord.get(COMMAND).equals(INSERT)){
                    objSecurityTO.setStatus(CommonConstants.STATUS_CREATED);
                }else if (oneRecord.get(COMMAND).equals(UPDATE)){
                    objSecurityTO.setStatus(CommonConstants.STATUS_MODIFIED);
                }
                if (strCommand.equals(CommonConstants.TOSTATUS_DELETE)){
                    objSecurityTO.setStatus(CommonConstants.STATUS_DELETED);
                    objSecurityTO.setCommand(CommonConstants.TOSTATUS_DELETE);
                }
                objSecurityTO.setStatusBy(TrueTransactMain.USER_ID);
                objSecurityTO.setStatusDt(curDate);
                objSecurityTO.setBranchCode(getSelectedBranchID());
                objSecurityTO.setInitiatedBranch(ProxyParameters.BRANCH_ID);
                securityMap.put(String.valueOf(j+1), objSecurityTO);
                oneRecord = null;
                objSecurityTO = null;
            }
            
            // To set the values for Security Transfer Object
            // where as the existing records in Database are deleted in client side
            // useful for updating the status
            for (int i = tableUtilSecurity.getRemovedValues().size() - 1,j = 0;i >= 0;--i,++j){
                oneRecord = (HashMap) tableUtilSecurity.getRemovedValues().get(j);
                objSecurityTO = new SecurityTO();
                objSecurityTO.setCustId(CommonUtil.convertObjToStr(oneRecord.get(CUSTOMER_ID)));
                
                Date DcDt1 = DateUtil.getDateMMDDYYYY(CommonUtil.convertObjToStr(oneRecord.get(DATE_CHARGE)));
                if(DcDt1 != null){
                Date dcDate1 = (Date) curDate.clone();
                dcDate1.setDate(DcDt1.getDate());
                dcDate1.setMonth(DcDt1.getMonth());
                dcDate1.setYear(DcDt1.getYear());
                objSecurityTO.setChargeDt(dcDate1);
                }else{
                    objSecurityTO.setChargeDt(DateUtil.getDateMMDDYYYY(CommonUtil.convertObjToStr(oneRecord.get(DATE_CHARGE))));
                }
                objSecurityTO.setChargeNature(CommonUtil.convertObjToStr(oneRecord.get(NATURE_CHARGE)));
                objSecurityTO.setCommand(CommonUtil.convertObjToStr(oneRecord.get(COMMAND)));
                
                
                Date FdDt1 = DateUtil.getDateMMDDYYYY(CommonUtil.convertObjToStr(oneRecord.get(FROM_DATE)));
                if(FdDt1 != null){
                Date fdDate1 = (Date) curDate.clone();
                fdDate1.setDate(FdDt1.getDate());
                fdDate1.setMonth(FdDt1.getMonth());
                fdDate1.setYear(FdDt1.getYear());
                objSecurityTO.setFromDt(fdDate1);
                }else{
                    objSecurityTO.setFromDt(DateUtil.getDateMMDDYYYY(CommonUtil.convertObjToStr(oneRecord.get(FROM_DATE))));
                }
                objSecurityTO.setIndustrialUsers(CommonUtil.convertObjToStr(oneRecord.get(MILL_INDUS)));
                
                Date DiDt1 = DateUtil.getDateMMDDYYYY(CommonUtil.convertObjToStr(oneRecord.get(DATE_INSPECTION)));
                if(DiDt1 != null){
                Date diDate1 = (Date) curDate.clone();
                diDate1.setDate(DiDt1.getDate());
                diDate1.setMonth(DiDt1.getMonth());
                diDate1.setYear(DiDt1.getYear());
                objSecurityTO.setInspectionDt(diDate1);
                }else{
                    objSecurityTO.setInspectionDt(DateUtil.getDateMMDDYYYY(CommonUtil.convertObjToStr(oneRecord.get(DATE_INSPECTION))));
                }
                objSecurityTO.setAvailableSecurityValue(CommonUtil.convertObjToDouble(oneRecord.get(AVAL_SEC_VAL)));
                objSecurityTO.setParticulars(CommonUtil.convertObjToStr(oneRecord.get(PARTICULARS)));
                objSecurityTO.setSecurityCategory(CommonUtil.convertObjToStr(oneRecord.get("")));
                objSecurityTO.setSecurityNo(CommonUtil.convertObjToDouble(oneRecord.get(SLNO)));
                objSecurityTO.setSecurityType(CommonUtil.convertObjToStr(oneRecord.get(SECURITY_TYPE)));
                objSecurityTO.setSecurityValue(CommonUtil.convertObjToDouble(oneRecord.get(SECURITY_VALUE)));
                objSecurityTO.setWeight(CommonUtil.convertObjToDouble(oneRecord.get(WEIGHT)));
                
                Date AsDt1 = DateUtil.getDateMMDDYYYY(CommonUtil.convertObjToStr(oneRecord.get(ASON)));
                if(AsDt1 != null){
                Date asDate1 = (Date) curDate.clone();
                asDate1.setDate(AsDt1.getDate());
                asDate1.setMonth(AsDt1.getMonth());
                asDate1.setYear(AsDt1.getYear());
                objSecurityTO.setSecurityValueOn(asDate1);
                }else{
                    objSecurityTO.setSecurityValueOn(DateUtil.getDateMMDDYYYY(CommonUtil.convertObjToStr(oneRecord.get(ASON))));
                }
                objSecurityTO.setSelectiveCommodity(CommonUtil.convertObjToStr(oneRecord.get(COMMODITY_ITEM)));
                objSecurityTO.setStatus(CommonUtil.convertObjToStr(oneRecord.get(COMMAND)));
                objSecurityTO.setStockStatFreq(CommonUtil.convertObjToDouble(oneRecord.get(STOCK_STATE_FREQ)));
                objSecurityTO.setInsuranceNo(CommonUtil.convertObjToDouble(oneRecord.get(INSURANCE_NO)));
                
                Date TdDt1 = DateUtil.getDateMMDDYYYY(CommonUtil.convertObjToStr(oneRecord.get(TO_DATE)));
                if(TdDt1 != null){
                Date tdDate1 = (Date) curDate.clone();
                tdDate1.setDate(TdDt1.getDate());
                tdDate1.setMonth(TdDt1.getMonth());
                tdDate1.setYear(TdDt1.getYear());
                objSecurityTO.setToDt(tdDate1);
                }else{
                    objSecurityTO.setToDt(DateUtil.getDateMMDDYYYY(CommonUtil.convertObjToStr(oneRecord.get(TO_DATE))));
                }
                objSecurityTO.setStatus(CommonConstants.STATUS_DELETED);
                objSecurityTO.setStatusBy(TrueTransactMain.USER_ID);
                objSecurityTO.setBranchCode(getSelectedBranchID());
                objSecurityTO.setInitiatedBranch(ProxyParameters.BRANCH_ID);
                objSecurityTO.setStatusDt(curDate);
                securityMap.put(String.valueOf(securityMap.size()+1), objSecurityTO);
                oneRecord = null;
                objSecurityTO = null;
            }
        }catch(Exception e){
            log.info("Error In setSecurity()..."+e);
            parseException.logException(e,true);
        }
        return securityMap;
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
        ttNotifyObservers();
    }
    
    /** To update the Status based on result performed by doAction() method */
    public void setResultStatus(){
        this.setLblStatus(ClientConstants.RESULT_STATUS[this.getResult()]);
        ttNotifyObservers();
    }
    /**
     * will return the action type
     * @return the appropriate action taken
     */
    public int getActionType(){
        log.info("In getActionType...");
        return actionType;
    }
    
    /**
     * will set the action type whether it is new or edit or delete
     * @param actionType is new or edit or delete
     */
    public void setActionType(int actionType) {
        log.info("In setActionType: "+actionType);
        this.actionType = actionType;
        setChanged();
    }
    
    /**
     * It will set the result of the query executed
     * @param resultStatus is the integer value which gives whether the data are inserted or updated or
     * deleted or the execution is failed
     */
    public void setResult(int resultStatus) {
        log.info("In setResult...");
        this.resultStatus = resultStatus;
        setChanged();
    }
    
    /**
     * Return the result of the executed query
     * @return the result of the executed query
     */
    public int getResult(){
        log.info("In getResult...");
        return resultStatus;
    }
    
    /**
     * It will call the notifyObservers()
     */
    public void ttNotifyObservers(){
        notifyObservers();
        insuranceOB.ttNotifyObservers();
    }
    
    void setTxtTotalSecurity_Value(String txtTotalSecurity_Value){
        this.txtTotalSecurity_Value = txtTotalSecurity_Value;
    }
    
    String getTxtTotalSecurity_Value(){
        return this.txtTotalSecurity_Value;
    }
    
    private void calculateSecurityValue(double realSecurityValue){
        try{
            this.securityValue = realSecurityValue;
        }catch(Exception e){
            log.info("Exception caught in calculateSecurityValue: "+e);
            parseException.logException(e,true);
        }
    }
    
    private void addSecurityValue(double securityVal){
        try{
            this.totalSecurityValue = this.totalSecurityValue + securityVal;
        }catch(Exception e){
            log.info("Exception caught in addSecurityValue: "+e);
            parseException.logException(e,true);
        }
    }
    
    private void subtractSecurityValue(double securityVal){
        try{
            this.totalSecurityValue = this.totalSecurityValue - securityVal;
        }catch(Exception e){
            log.info("Exception caught in subtractSecurityValue: "+e);
            parseException.logException(e,true);
        }
    }
    
    private void displayTotalSecurityValue(){
        try{
            java.text.DecimalFormat objDecimalFormat = new java.text.DecimalFormat();
            objDecimalFormat.applyPattern("###############.#");
            objDecimalFormat.setDecimalSeparatorAlwaysShown(false);
            Double totSecurityVal = new Double(getTotalSecurityValue());
            setTxtTotalSecurity_Value(objDecimalFormat.format(totSecurityVal));
            objDecimalFormat = null;
            totSecurityVal = null;
        }catch(Exception e){
            log.info("Exception caught in displayTotalSecurityValue: "+e);
            parseException.logException(e,true);
        }
    }
    /**
     * Getter for property cbmCity.
     * @return Value of property cbmCity.
     */
    public com.see.truetransact.clientutil.ComboBoxModel getCbmCity() {
        return cbmCity;
    }    
    
    /**
     * Setter for property cbmCity.
     * @param cbmCity New value of property cbmCity.
     */
    public void setCbmCity(com.see.truetransact.clientutil.ComboBoxModel cbmCity) {
        this.cbmCity = cbmCity;
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
    
    /**
     * Getter for property securityValue.
     * @return Value of property securityValue.
     */
    public double getSecurityValue() {
        return securityValue;
    }
    
    /**
     * Setter for property securityValue.
     * @param securityValue New value of property securityValue.
     */
    public void setSecurityValue(double securityValue) {
        this.securityValue = securityValue;
    }
    
    /**
     * Getter for property totalSecurityValue.
     * @return Value of property totalSecurityValue.
     */
    public double getTotalSecurityValue() {
        return totalSecurityValue;
    }
    
    /**
     * Setter for property totalSecurityValue.
     * @param totalSecurityValue New value of property totalSecurityValue.
     */
    public void setTotalSecurityValue(double totalSecurityValue) {
        this.totalSecurityValue = totalSecurityValue;
    }
    
    void setTblSecurityTab(EnhancedTableModel tblSecurityTab){
        log.info("In setTblSecurityTab()...");
        
        this.tblSecurityTab = tblSecurityTab;
        setChanged();
    }
    
    EnhancedTableModel getTblSecurityTab(){
        return this.tblSecurityTab;
    }
    
    void setTxtSecurityNo(String txtSecurityNo){
        this.txtSecurityNo = txtSecurityNo;
        setChanged();
    }
    String getTxtSecurityNo(){
        return this.txtSecurityNo;
    }
    
    void setCbmSecurityCate(ComboBoxModel cbmSecurityCate){
        this.cbmSecurityCate = cbmSecurityCate;
        setChanged();
    }
    
    ComboBoxModel getCbmSecurityCate(){
        return this.cbmSecurityCate;
    }
    
    void setCboSecurityCate(String cboSecurityCate){
        this.cboSecurityCate = cboSecurityCate;
        setChanged();
    }
    String getCboSecurityCate(){
        return this.cboSecurityCate;
    }
    
    void setRdoSecurityType_Primary(boolean rdoSecurityType_Primary){
        this.rdoSecurityType_Primary = rdoSecurityType_Primary;
        setChanged();
    }
    boolean getRdoSecurityType_Primary(){
        return this.rdoSecurityType_Primary;
    }
    
    void setRdoSecurityType_Collateral(boolean rdoSecurityType_Collateral){
        this.rdoSecurityType_Collateral = rdoSecurityType_Collateral;
        setChanged();
    }
    boolean getRdoSecurityType_Collateral(){
        return this.rdoSecurityType_Collateral;
    }
    
    void setTxtSecurityValue(String txtSecurityValue){
        this.txtSecurityValue = txtSecurityValue;
        setChanged();
    }
    String getTxtSecurityValue(){
        return this.txtSecurityValue;
    }
    
    void setTdtAson(String tdtAson){
        this.tdtAson = tdtAson;
        setChanged();
    }
    String getTdtAson(){
        return this.tdtAson;
    }
    
    void setTxtParticulars(String txtParticulars){
        this.txtParticulars = txtParticulars;
        setChanged();
    }
    String getTxtParticulars(){
        return this.txtParticulars;
    }
    
    void setCbmNatureCharge(ComboBoxModel cbmNatureCharge){
        this.cbmNatureCharge = cbmNatureCharge;
        setChanged();
    }
    
    ComboBoxModel getCbmNatureCharge(){
        return this.cbmNatureCharge;
    }
    
    void setCboNatureCharge(String cboNatureCharge){
        this.cboNatureCharge = cboNatureCharge;
        setChanged();
    }
    String getCboNatureCharge(){
        return this.cboNatureCharge;
    }
    
    void setTdtDateCharge(String tdtDateCharge){
        this.tdtDateCharge = tdtDateCharge;
        setChanged();
    }
    String getTdtDateCharge(){
        return this.tdtDateCharge;
    }
    
    void setChkSelCommodityItem(boolean chkSelCommodityItem){
        this.chkSelCommodityItem = chkSelCommodityItem;
        setChanged();
    }
    boolean getChkSelCommodityItem(){
        return this.chkSelCommodityItem;
    }
    
    void setCbmForMillIndus(ComboBoxModel cbmForMillIndus){
        this.cbmForMillIndus = cbmForMillIndus;
        setChanged();
    }
    
    ComboBoxModel getCbmForMillIndus(){
        return this.cbmForMillIndus;
    }
    
    void setCboForMillIndus(String cboForMillIndus){
        this.cboForMillIndus = cboForMillIndus;
        setChanged();
    }
    String getCboForMillIndus(){
        return this.cboForMillIndus;
    }
    
    void setTdtDateInspection(String tdtDateInspection){
        this.tdtDateInspection = tdtDateInspection;
        setChanged();
    }
    String getTdtDateInspection(){
        return this.tdtDateInspection;
    }
    
    void setCbmStockStateFreq(ComboBoxModel cbmStockStateFreq){
        this.cbmStockStateFreq = cbmStockStateFreq;
        setChanged();
    }
    
    ComboBoxModel getCbmStockStateFreq(){
        return this.cbmStockStateFreq;
    }
    
    void setCboStockStateFreq(String cboStockStateFreq){
        this.cboStockStateFreq = cboStockStateFreq;
        setChanged();
    }
    String getCboStockStateFreq(){
        return this.cboStockStateFreq;
    }
    
    void setTdtFromDate(String tdtFromDate){
        this.tdtFromDate = tdtFromDate;
        setChanged();
    }
    String getTdtFromDate(){
        return this.tdtFromDate;
    }
    
    void setTdtToDate(String tdtToDate){
        this.tdtToDate = tdtToDate;
        setChanged();
    }
    String getTdtToDate(){
        return this.tdtToDate;
    }
    
    /**
     * Getter for property lblCustPin_Disp.
     * @return Value of property lblCustPin_Disp.
     */
    public java.lang.String getLblCustPin_Disp() {
        return lblCustPin_Disp;
    }
    
    /**
     * Setter for property lblCustPin_Disp.
     * @param lblCustPin_Disp New value of property lblCustPin_Disp.
     */
    public void setLblCustPin_Disp(java.lang.String lblCustPin_Disp) {
        this.lblCustPin_Disp = lblCustPin_Disp;
        insuranceOB.setLblCustPin_Disp(lblCustPin_Disp);
    }
    
    /**
     * Getter for property lblCustStreet_Disp.
     * @return Value of property lblCustStreet_Disp.
     */
    public java.lang.String getLblCustStreet_Disp() {
        return lblCustStreet_Disp;
    }
    
    /**
     * Setter for property lblCustStreet_Disp.
     * @param lblCustStreet_Disp New value of property lblCustStreet_Disp.
     */
    public void setLblCustStreet_Disp(java.lang.String lblCustStreet_Disp) {
        this.lblCustStreet_Disp = lblCustStreet_Disp;
        insuranceOB.setLblCustStreet_Disp(lblCustStreet_Disp);
    }
    
    /**
     * Getter for property lblCustCity_Disp.
     * @return Value of property lblCustCity_Disp.
     */
    public java.lang.String getLblCustCity_Disp() {
        return lblCustCity_Disp;
    }
    
    /**
     * Setter for property lblCustCity_Disp.
     * @param lblCustCity_Disp New value of property lblCustCity_Disp.
     */
    public void setLblCustCity_Disp(java.lang.String lblCustCity_Disp) {
        this.lblCustCity_Disp = lblCustCity_Disp;
        insuranceOB.setLblCustCity_Disp(lblCustCity_Disp);
    }
    
    /**
     * Getter for property lblCustEmail_ID_Disp.
     * @return Value of property lblCustEmail_ID_Disp.
     */
    public java.lang.String getLblCustEmail_ID_Disp() {
        return lblCustEmail_ID_Disp;
    }
    
    /**
     * Setter for property lblCustEmail_ID_Disp.
     * @param lblCustEmail_ID_Disp New value of property lblCustEmail_ID_Disp.
     */
    public void setLblCustEmail_ID_Disp(java.lang.String lblCustEmail_ID_Disp) {
        this.lblCustEmail_ID_Disp = lblCustEmail_ID_Disp;
        insuranceOB.setLblCustEmail_ID_Disp(lblCustEmail_ID_Disp);
    }
    
    /**
     * Getter for property lblCustName_Disp.
     * @return Value of property lblCustName_Disp.
     */
    public java.lang.String getLblCustName_Disp() {
        return lblCustName_Disp;
    }
    
    /**
     * Setter for property lblCustName_Disp.
     * @param lblCustName_Disp New value of property lblCustName_Disp.
     */
    public void setLblCustName_Disp(java.lang.String lblCustName_Disp) {
        this.lblCustName_Disp = lblCustName_Disp;
        insuranceOB.setLblCustName_Disp(lblCustName_Disp);
    }
    
    /**
     * Getter for property lblCustID_Disp.
     * @return Value of property lblCustID_Disp.
     */
    public java.lang.String getLblCustID_Disp() {
        return lblCustID_Disp;
    }
    
    /**
     * Setter for property lblCustID_Disp.
     * @param lblCustID_Disp New value of property lblCustID_Disp.
     */
    public void setLblCustID_Disp(java.lang.String lblCustID_Disp) {
        this.lblCustID_Disp = lblCustID_Disp;
        insuranceOB.setLblCustID_Disp(lblCustID_Disp);
    }
    
    public int addSecurityDetails(int recordPosition, boolean update){
        int option = -1;
        try{
            securityEachTabRecord = new ArrayList();
            securityEachRecord = new HashMap();
            HashMap result = new HashMap();
            ArrayList data = tblSecurityTab.getDataArrayList();
            tblSecurityTab.setDataArrayList(data, securityTabTitle);
            final int dataSize = data.size();
            insertSecurity(dataSize+1);
            if (!update) {
                // If the table is not in Edit Mode
                result = tableUtilSecurity.insertTableValues(securityEachTabRecord, securityEachRecord);
                
                securityTabValues = (ArrayList) result.get(TABLE_VALUES);
                securityAll = (LinkedHashMap) result.get(ALL_VALUES);
                option = CommonUtil.convertObjToInt(result.get(OPTION));
                
                tblSecurityTab.setDataArrayList(securityTabValues, securityTabTitle);
                String securityNo = ((String) ((ArrayList) tblSecurityTab.getDataArrayList().get(tblSecurityTab.getRowCount() - 1)).get(0));
                // To add the sanction no. in Insurance Detail's combobox when new Sanction Record inserted
//                if (insuranceOB.getCbmSecurityNo_Insurance().containsElement("All")){
//                    // If sanction number contains element "All" then append the sanction no. only
                insuranceOB.getCbmSecurityNo_Insurance().addKeyAndElement(securityNo, securityNo);
//                }else{
//                    // If the combobox doesn't contain element "All" then add "All" and then Sanction no.
//                    insuranceOB.getCbmSecurityNo_Insurance().addKeyAndElement(ALL, "All");
//                    insuranceOB.getCbmSecurityNo_Insurance().addKeyAndElement(securityNo, securityNo);
//                }
                
                // To calculate Total Security value(Average)
                calculateSecurityValue(CommonUtil.convertObjToDouble(getTxtAvalSecVal()).doubleValue());
                addSecurityValue(getSecurityValue());
                displayTotalSecurityValue();
                if (CommonUtil.convertObjToStr(getCbmInsuranceNo().getKeyForSelected()).length() > 0){
                    getInsuranceNoSelectedNSecurityLevel().add(CommonUtil.convertObjToStr(getCbmInsuranceNo().getKeyForSelected()));
                }
            }else{
                option = updateSecurityTab(recordPosition);
                
                if (CommonUtil.convertObjToStr(getCbmInsuranceNo().getKeyForSelected()).length() > 0 && !(getStrStoredInsuranceNo().equals(CommonUtil.convertObjToStr(getCbmInsuranceNo().getKeyForSelected())))){
                    // If the old value(Insurance No) is not equal to the selected one then
                    // delete the old value and add the new value
                    getInsuranceNoSelectedNSecurityLevel().remove(getStrStoredInsuranceNo());
                    getInsuranceNoSelectedNSecurityLevel().add(CommonUtil.convertObjToStr(getCbmInsuranceNo().getKeyForSelected()));
                }else if ((getStrStoredInsuranceNo().length() > 0 && CommonUtil.convertObjToStr(getCbmInsuranceNo().getKeyForSelected()).length() == 0)){
                    // The Insurance No is selected in the original record
                    // and if it is not selected now then remove the value from the List
                    getInsuranceNoSelectedNSecurityLevel().remove(getStrStoredInsuranceNo());
                }else if ((getStrStoredInsuranceNo().length() == 0 && CommonUtil.convertObjToStr(getCbmInsuranceNo().getKeyForSelected()).length() > 0)){
                    // The Insurance No is not selected in the original record
                    // and if it is selected now then add the value from the List
                    getInsuranceNoSelectedNSecurityLevel().add(CommonUtil.convertObjToStr(getCbmInsuranceNo().getKeyForSelected()));
                }
            }
            
            setChanged();
            
            securityEachTabRecord = null;
            securityEachRecord = null;
            result = null;
            data = null;
        }catch(Exception e){
            log.info("Error in addSecurityDetails()..."+e);
            parseException.logException(e,true);
        }
        return option;
    }
    
    private void insertSecurity(int recordPosition){
        securityEachTabRecord.add(String.valueOf(recordPosition));
        securityEachTabRecord.add(getCboSecurityCate());
        securityEachTabRecord.add(getTxtSecurityValue());
        securityEachTabRecord.add(getTdtAson());
        
        securityEachRecord.put(SLNO, "");
        if (getRdoSecurityType_Collateral()){
            securityEachRecord.put(SECURITY_TYPE, COLLATERAL);
        }else if (getRdoSecurityType_Primary()){
            securityEachRecord.put(SECURITY_TYPE, PRIMARY);
        }else{
            securityEachRecord.put(SECURITY_TYPE, "");
        }
        securityEachRecord.put(INSURANCE_NO, CommonUtil.convertObjToStr(getCbmInsuranceNo().getKeyForSelected()));
        securityEachRecord.put(CUSTOMER_ID, getLblCustID_Disp());
        securityEachRecord.put(SECURITY_VALUE, getTxtSecurityValue());
        securityEachRecord.put(WEIGHT, getTxtWeight());
        securityEachRecord.put(GROSSWEIGHT, getTxtGrossWeight());
        securityEachRecord.put(ASON, getTdtAson());
        securityEachRecord.put(PARTICULARS, getTxtParticulars());
        securityEachRecord.put(SECURITY_CATEGORY, CommonUtil.convertObjToStr(getCbmSecurityCate().getKeyForSelected()));
        securityEachRecord.put(TO_DATE, getTdtToDate());
        securityEachRecord.put(FROM_DATE, getTdtFromDate());
        securityEachRecord.put(AVAL_SEC_VAL, getTxtAvalSecVal());
        securityEachRecord.put(NATURE_CHARGE, CommonUtil.convertObjToStr(getCbmNatureCharge().getKeyForSelected()));
        securityEachRecord.put(DATE_CHARGE, getTdtDateCharge());
        if (getChkSelCommodityItem()){
            securityEachRecord.put(COMMODITY_ITEM, YES);
        }else{
            securityEachRecord.put(COMMODITY_ITEM, NO);
        }
        securityEachRecord.put(MILL_INDUS, CommonUtil.convertObjToStr(getCbmForMillIndus().getKeyForSelected()));
        securityEachRecord.put(DATE_INSPECTION, getTdtDateInspection());
        securityEachRecord.put(STOCK_STATE_FREQ, CommonUtil.convertObjToStr(getCbmStockStateFreq().getKeyForSelected()));
        securityEachRecord.put(COMMAND, "");
        securityEachRecord.put(AUTHORIZE_BY, "");
        securityEachRecord.put(AUTHORIZE_DT, null);
        securityEachRecord.put(AUTHORIZE_REMARKS, "");
        securityEachRecord.put(AUTHORIZE_STATUS, "");
    }
    
    private int updateSecurityTab(int recordPosition){
        int option = -1;
        HashMap result = new HashMap();
        try{
            String strRecordKey = CommonUtil.convertObjToStr(((ArrayList) (tblSecurityTab.getDataArrayList().get(recordPosition))).get(0));
            HashMap eachRecs = (HashMap) securityAll.get(strRecordKey);
            double secValueBeforeUpdate = CommonUtil.convertObjToDouble(eachRecs.get(AVAL_SEC_VAL)).doubleValue();
            double secValue = CommonUtil.convertObjToDouble(getTxtAvalSecVal()).doubleValue();
            result = tableUtilSecurity.updateTableValues(securityEachTabRecord, securityEachRecord, recordPosition);
            
            securityTabValues = (ArrayList) result.get(TABLE_VALUES);
            securityAll = (LinkedHashMap) result.get(ALL_VALUES);
            option = CommonUtil.convertObjToInt(result.get(OPTION));
            
            tblSecurityTab.setDataArrayList(securityTabValues, securityTabTitle);
            
            // To calculate Total Security value(Average)
            calculateSecurityValue(secValueBeforeUpdate);
            subtractSecurityValue(getSecurityValue());
            calculateSecurityValue(secValue);
            addSecurityValue(getSecurityValue());
            displayTotalSecurityValue();
        }catch(Exception e){
            log.info("Error in updateSecurityTab()..."+e);
            parseException.logException(e,true);
        }
        result = null;
        return option;
    }
    
    public int populateSecurityDetails(int recordPosition){
        int insuranceTabRowToPopulate = -1;
        try{
            HashMap eachRecs;
            java.util.Set keySet =  securityAll.keySet();
            Object[] objKeySet = (Object[]) keySet.toArray();
            String strRecordKey = (String) ((ArrayList) (tblSecurityTab.getDataArrayList().get(recordPosition))).get(0);
            // To populate the corresponding record from the Security Details
            for (int i = keySet.size() - 1, j = 0;i >= 0;--i, ++j){
                if ((CommonUtil.convertObjToStr(((HashMap) securityAll.get(objKeySet[j])).get(SLNO))).equals(strRecordKey)){
                    // To populate the Corresponding record from CTable
                    eachRecs = (HashMap) securityAll.get(objKeySet[j]);
                    setTxtSecurityNo(CommonUtil.convertObjToStr(eachRecs.get(SLNO)));
                    if (eachRecs.get(SECURITY_TYPE).equals(PRIMARY)){
                        setRdoSecurityType_Primary(true);
                        setRdoSecurityType_Collateral(false);
                    }else if (eachRecs.get(SECURITY_TYPE).equals(COLLATERAL)){
                        setRdoSecurityType_Collateral(true);
                        setRdoSecurityType_Primary(false);
                    }
                    setTxtSecurityValue(CommonUtil.convertObjToStr(eachRecs.get(SECURITY_VALUE)));
                    setTdtAson(CommonUtil.convertObjToStr(eachRecs.get(ASON)));
                    setTxtParticulars(CommonUtil.convertObjToStr(eachRecs.get(PARTICULARS)));
                    setCboSecurityCate(CommonUtil.convertObjToStr(getCbmSecurityCate().getDataForKey(CommonUtil.convertObjToStr(eachRecs.get(SECURITY_CATEGORY)))));
                    setTdtToDate(CommonUtil.convertObjToStr(eachRecs.get(TO_DATE)));
                    setTdtFromDate(CommonUtil.convertObjToStr(eachRecs.get(FROM_DATE)));
                    setTxtAvalSecVal(CommonUtil.convertObjToStr(eachRecs.get(AVAL_SEC_VAL)));
                    setCboNatureCharge(CommonUtil.convertObjToStr(getCbmNatureCharge().getDataForKey(CommonUtil.convertObjToStr(eachRecs.get(NATURE_CHARGE)))));
                    setTdtDateCharge(CommonUtil.convertObjToStr(eachRecs.get(DATE_CHARGE)));
                    if (eachRecs.get(COMMODITY_ITEM).equals(YES)){
                        setChkSelCommodityItem(true);
                    }else if (eachRecs.get(COMMODITY_ITEM).equals(NO)){
                        setChkSelCommodityItem(false);
                    }
                    setTxtWeight(CommonUtil.convertObjToStr(eachRecs.get(WEIGHT)));
                    setTxtGrossWeight(CommonUtil.convertObjToStr(eachRecs.get(GROSSWEIGHT)));
                    setCboForMillIndus(CommonUtil.convertObjToStr(getCbmForMillIndus().getDataForKey(CommonUtil.convertObjToStr(eachRecs.get(MILL_INDUS)))));
                    setTdtDateInspection(CommonUtil.convertObjToStr(eachRecs.get(DATE_INSPECTION)));
                    setCboStockStateFreq(CommonUtil.convertObjToStr(getCbmStockStateFreq().getDataForKey(CommonUtil.convertObjToStr(eachRecs.get(STOCK_STATE_FREQ)))));
                    setCboInsuranceNo(CommonUtil.convertObjToStr(getCbmInsuranceNo().getDataForKey(CommonUtil.convertObjToStr(eachRecs.get(INSURANCE_NO)))));
                    setStrStoredInsuranceNo(CommonUtil.convertObjToStr(eachRecs.get(INSURANCE_NO)));
                    break;
                }
                eachRecs = null;
            }
            
            if (getCboInsuranceNo().length() > 0){
                ArrayList insuranceList = insuranceOB.getInsuranceTabValues();
                int insuranceListSize = insuranceList.size();
                for (int i = insuranceListSize - 1,j = 0;i >= 0;--i,++j){
                    if (CommonUtil.convertObjToStr(((ArrayList)insuranceList.get(j)).get(0)).equals(getCboInsuranceNo())){
                        insuranceTabRowToPopulate = j;
                        break;
                    }
                }
            }
            keySet = null;
            objKeySet = null;
            setChanged();
            ttNotifyObservers();
        }catch(Exception e){
            log.info("Error in populateSecurityDetails()..."+e);
            parseException.logException(e,true);
        }
        return insuranceTabRowToPopulate;
    }
    
    public void deleteSecurityTabRecord(int recordPosition){
        HashMap result = new HashMap();
        try{
            // To remove the security No. from the combo box in Insurance Details
            String securityNo = (String) ((ArrayList) securityTabValues.get(recordPosition)).get(0);
            
            result = tableUtilSecurity.deleteTableValues(recordPosition);
            
            securityTabValues = (ArrayList) result.get(TABLE_VALUES);
            securityAll = (LinkedHashMap) result.get(ALL_VALUES);
            
            tblSecurityTab.setDataArrayList(securityTabValues, securityTabTitle);
            // Remove the Security number in Insurance Detail's comboboxmodel
            
            insuranceOB.getCbmSecurityNo_Insurance().removeKeyAndElement(securityNo);
            // Remove the element "All" in the comboboxmodel when all the security details are removed
//            if (securityTabValues.size() <= 0){
//                insuranceOB.getCbmSecurityNo_Insurance().removeKeyAndElement(ALL);
//            }
            
            // To calculate Total Security value(Average)
            calculateSecurityValue(CommonUtil.convertObjToDouble(getTxtAvalSecVal()).doubleValue());
            subtractSecurityValue(getSecurityValue());
            displayTotalSecurityValue();
            if (CommonUtil.convertObjToStr(getCbmInsuranceNo().getKeyForSelected()).length() > 0){
                    getInsuranceNoSelectedNSecurityLevel().remove(CommonUtil.convertObjToStr(getCbmInsuranceNo().getKeyForSelected()));
            }
        }catch(Exception e){
            log.info("Error in deleteSecurityTabRecord()..."+e);
            parseException.logException(e,true);
        }
        result = null;
    }
    
    public String insuranceExistWarning(int selectedRow, String strInsuranceNumber){
        // To check whether the Security No. has Insurance Details or not
        StringBuffer stbWarnMsg = new StringBuffer("");
        try{
            ArrayList securityData = tblSecurityTab.getDataArrayList();
            String securityNo = CommonUtil.convertObjToStr(((ArrayList) securityData.get(selectedRow)).get(0));
            ArrayList insuranceData = insuranceOB.getSecurityNoSelectedNInsuranceLevel();
            
            if (insuranceData.contains(securityNo) || strInsuranceNumber.length() > 0){
                // This Security No. has Insurance Details
                stbWarnMsg.append("\n");
                stbWarnMsg.append(objSecurityInsuranceRB.getString("lblSecurityNo_Insurance"));
                stbWarnMsg.append(securityNo);
                stbWarnMsg.append(objSecurityInsuranceRB.getString("hasInsuranceWarning"));
            }
//            if ((tblSecurityTab.getRowCount() == 1) && (insuranceOB.getTblInsuranceTab().getRowCount() > 0)){
//                String[] options = {objSecurityInsuranceRB.getString("cDialogOk")};
//                option = COptionPane.showOptionDialog(null, objSecurityInsuranceRB.getString("existanceSecurityNoWarning"), CommonConstants.WARNINGTITLE,
//                COptionPane.OK_OPTION, COptionPane.WARNING_MESSAGE, null, options, options[0]);
//                delete = false;
//                return delete;
//            }
        }catch(Exception e){
            log.info("Error in insuranceExistWarning..."+e);
            parseException.logException(e,true);
        }
        return stbWarnMsg.toString();
    }
    
    
    public String isInsuranceNoSelectedInSecurityDetails(String strInsuranceNo, Object objSecNoSelected){
        StringBuffer stbWarnMsg = new StringBuffer("");
        try{
            String strSecNoSelected = CommonUtil.convertObjToStr(objSecNoSelected);
            if (strSecNoSelected.length() > 0){

                HashMap eachRec;
                java.util.Set keySet =  securityAll.keySet();
                Object[] objKeySet = (Object[]) keySet.toArray();
                // To get the corresponding record from the Security Details
                for (int i = keySet.size() - 1, j = 0;i >= 0;--i, ++j){
                    if ((CommonUtil.convertObjToStr(((HashMap) securityAll.get(objKeySet[j])).get(SLNO))).equals(strSecNoSelected)){
                        eachRec = (HashMap) securityAll.get(objKeySet[j]);
                        if (CommonUtil.convertObjToStr(eachRec.get(INSURANCE_NO)).length() > 0){
                            stbWarnMsg.append("\n");
                            stbWarnMsg.append(objSecurityInsuranceRB.getString("lblSecurityNo_Insurance"));
                            stbWarnMsg.append(strSecNoSelected);
                            stbWarnMsg.append(objSecurityInsuranceRB.getString("securityNoSelectedWarning"));
                            break;
                        }
                        eachRec = null;
                    }
                }
                keySet = null;
                objKeySet = null;
            }
            if (strInsuranceNo.length() > 0 && stbWarnMsg.length() <= 0 && getInsuranceNoSelectedNSecurityLevel().contains(strInsuranceNo)){
                stbWarnMsg.append("\n");
                stbWarnMsg.append(objSecurityInsuranceRB.getString("lblInsuranceNo_Disp"));
                stbWarnMsg.append(strInsuranceNo);
                stbWarnMsg.append(objSecurityInsuranceRB.getString("insuranceNoSelectedWarning"));
            }
        }catch(Exception e){
            log.info("Exception caught in isInsuranceNoSelectedInSecurityDetails: "+e);
            parseException.logException(e, true);
        }
        return stbWarnMsg.toString();
    }
    
    public String chkAllInsuranceHavingSecurity(){
        StringBuffer stbWarnMsg = new StringBuffer("");
        try{
            int insuranceRowCount = insuranceOB.getInsuranceTabValues().size();
            ArrayList insuranceWithOutSecurityList = new ArrayList();
            for (int i = insuranceRowCount - 1,j = 0;i >= 0;--i,++j){
                if (CommonUtil.convertObjToStr(((ArrayList)insuranceOB.getInsuranceTabValues().get(j)).get(1)).length() <= 0){
                    insuranceWithOutSecurityList.add(((ArrayList)insuranceOB.getInsuranceTabValues().get(j)).get(0));
                }
            }
            for (int i = insuranceWithOutSecurityList.size() - 1;i >= 0;--i){
                if (getInsuranceNoSelectedNSecurityLevel().contains(insuranceWithOutSecurityList.get(i))){
                    insuranceWithOutSecurityList.remove(i);
                }
            }
            if (insuranceWithOutSecurityList.size() > 0){
                stbWarnMsg.append("\n");
                stbWarnMsg.append(objSecurityInsuranceRB.getString("lblInsuranceNo_Disp"));
                stbWarnMsg.append(insuranceWithOutSecurityList.toString());
                stbWarnMsg.append(objSecurityInsuranceRB.getString("insuranceDoesntExistWarning"));
            }
        }catch(Exception e){
            log.info("Exception caught in calculateRemainingSecurityVal: "+e);
            parseException.logException(e,true);
        }
        return stbWarnMsg.toString();
    }
    
    public String chkSecValueGTInsuAmt(){
        StringBuffer stbWarnMsg = new StringBuffer("");
        try{
            ArrayList secValLessThanInsAmtList = new ArrayList();
            HashMap securityInsuranceVal = new HashMap();
            LinkedHashMap insuranceMap = insuranceOB.getInsuranceAll();
            int insuranceRowCount = insuranceOB.getInsuranceTabValues().size();
            int securityRowCount = getSecurityAll().size();
            java.util.Set keySet =  getSecurityAll().keySet();
            Object[] objKeySet = (Object[]) keySet.toArray();
            java.util.Set insuranceKeySet =  insuranceOB.getInsuranceAll().keySet();
            Object[] objInsurKeySet = (Object[]) insuranceKeySet.toArray();
            String strInsuranceNo = "";
            
            for (int i = securityRowCount - 1, j = 0;i >= 0;--i,++j){
                strInsuranceNo = CommonUtil.convertObjToStr(((HashMap)getSecurityAll().get(objKeySet[j])).get(INSURANCE_NO));
                if (strInsuranceNo.length() > 0){
                    insuranceOB.getInsuranceAll().get(strInsuranceNo);
                    securityInsuranceVal.put(objKeySet[j], CommonUtil.convertObjToDouble(((HashMap)insuranceOB.getInsuranceAll().get(strInsuranceNo)).get(POLICY_AMT)));
                }
                strInsuranceNo = null;
            }
            
            String strSecurityNo = "";
            double insuranceAmt = 0.0;
            for (int i = insuranceRowCount - 1, j = 0;i >= 0;--i,++j){
                strSecurityNo = CommonUtil.convertObjToStr(((HashMap)insuranceOB.getInsuranceAll().get(objInsurKeySet[j])).get(SECURITY_NO));
                if (strSecurityNo.length() > 0){
                    insuranceAmt = CommonUtil.convertObjToDouble(((HashMap)insuranceOB.getInsuranceAll().get(objInsurKeySet[j])).get(POLICY_AMT)).doubleValue();
                    insuranceAmt += CommonUtil.convertObjToDouble(securityInsuranceVal.get(strSecurityNo)).doubleValue();
                    securityInsuranceVal.put(strSecurityNo, String.valueOf(insuranceAmt));
                }
                strSecurityNo = null;
            }
            
            java.util.Set secWithInsurancekeySet =  securityInsuranceVal.keySet();
            Object[] objSecWithInsuranceKeySet = (Object[]) secWithInsurancekeySet.toArray();
            double secAmt = 0.0;
            for (int i = secWithInsurancekeySet.size() - 1, j = 0;i >= 0;--i,++j){
                if (getSecurityAll().containsKey(objSecWithInsuranceKeySet[j])){
                    secAmt = CommonUtil.convertObjToDouble(((HashMap) getSecurityAll().get(objSecWithInsuranceKeySet[j])).get(SECURITY_VALUE)).doubleValue();
                    insuranceAmt = CommonUtil.convertObjToDouble(securityInsuranceVal.get(objSecWithInsuranceKeySet[j])).doubleValue();
                    if (insuranceAmt < secAmt){
                        secValLessThanInsAmtList.add(objSecWithInsuranceKeySet[j]);
                    }
                }
            }
            
            if (secValLessThanInsAmtList.size() > 0){
                stbWarnMsg.append("\n");
                stbWarnMsg.append(objSecurityInsuranceRB.getString("insuranceAmtLessThanSecAmtWarning"));
                stbWarnMsg.append(secValLessThanInsAmtList.toString());
            }
            
            secWithInsurancekeySet = null;
            objSecWithInsuranceKeySet = null;
            keySet = null;
            objKeySet = null;
            insuranceKeySet = null;
            objInsurKeySet = null;
            strSecurityNo = null;
            strInsuranceNo = null;
            securityInsuranceVal = null;
            insuranceMap = null;
            secValLessThanInsAmtList = null;
        }catch(Exception e){
            log.info("Exception caught in chkSecValueGTInsuAmt: "+e);
            parseException.logException(e,true);
        }
        return stbWarnMsg.toString();
    }
    
    public String calculateRemainingSecurityVal(String strCurrSecVal){
        String strRemSecVal = "";
        try{
            
        }catch(Exception e){
            log.info("Error in calculateRemainingSecurityVal: "+e);
            parseException.logException(e,true);
        }
        return strRemSecVal;
    }
           
    public void setDefaultValWhenSecurityNewBtnActionPerformed(){
        try{
            setTdtAson(DateUtil.getStringDate(curDate));
            setTdtDateCharge(DateUtil.getStringDate(curDate));
            setTdtDateInspection(DateUtil.getStringDate(curDate));
            setTdtFromDate(DateUtil.getStringDate(curDate));
        }catch(Exception e){
            log.info("Error in setDefaultValWhenSecurityNewBtnActionPerformed: "+e);
            parseException.logException(e,true);
        }
    }
    
    // To create objects
    public void createObject(){
        insuranceNoSelectedNSecurityLevel = new ArrayList();
        securityTabValues = new ArrayList();
        tblSecurityTab.setDataArrayList(null, securityTabTitle);
        securityAll = new LinkedHashMap();
        tableUtilSecurity = new TableUtil();
        tableUtilSecurity.setAttributeKey(SLNO);
    }
    
    // To destroy Objects
    public void destroyObjects(){
        insuranceNoSelectedNSecurityLevel = null;
        securityTabValues = null;
        securityAll = null;
        tableUtilSecurity = null;
    }
    
    /**
     * Getter for property txtAvalSecVal.
     * @return Value of property txtAvalSecVal.
     */
    public java.lang.String getTxtAvalSecVal() {
        return txtAvalSecVal;
    }    
    
    /**
     * Setter for property txtAvalSecVal.
     * @param txtAvalSecVal New value of property txtAvalSecVal.
     */
    public void setTxtAvalSecVal(java.lang.String txtAvalSecVal) {
        this.txtAvalSecVal = txtAvalSecVal;
    }
    
    /**
     * Getter for property tblSecurityLoanTab.
     * @return Value of property tblSecurityLoanTab.
     */
    public com.see.truetransact.clientutil.EnhancedTableModel getTblSecurityLoanTab() {
        return tblSecurityLoanTab;
    }
    
    /**
     * Setter for property tblSecurityLoanTab.
     * @param tblSecurityLoanTab New value of property tblSecurityLoanTab.
     */
    public void setTblSecurityLoanTab(com.see.truetransact.clientutil.EnhancedTableModel tblSecurityLoanTab) {
        this.tblSecurityLoanTab = tblSecurityLoanTab;
    }
    
    /**
     * Getter for property txtWeight.
     * @return Value of property txtWeight.
     */
    public java.lang.String getTxtWeight() {
        return txtWeight;
    }
    
    /**
     * Setter for property txtWeight.
     * @param txtWeight New value of property txtWeight.
     */
    public void setTxtWeight(java.lang.String txtWeight) {
        this.txtWeight = txtWeight;
    }
    
    /**
     * Getter for property cbmInsuranceNo.
     * @return Value of property cbmInsuranceNo.
     */
    public com.see.truetransact.clientutil.ComboBoxModel getCbmInsuranceNo() {
        return cbmInsuranceNo;
    }
    
    /**
     * Setter for property cbmInsuranceNo.
     * @param cbmInsuranceNo New value of property cbmInsuranceNo.
     */
    public void setCbmInsuranceNo(com.see.truetransact.clientutil.ComboBoxModel cbmInsuranceNo) {
        this.cbmInsuranceNo = cbmInsuranceNo;
    }
    
    /**
     * Getter for property cboInsuranceNo.
     * @return Value of property cboInsuranceNo.
     */
    public java.lang.String getCboInsuranceNo() {
        return cboInsuranceNo;
    }
    
    /**
     * Setter for property cboInsuranceNo.
     * @param cboInsuranceNo New value of property cboInsuranceNo.
     */
    public void setCboInsuranceNo(java.lang.String cboInsuranceNo) {
        this.cboInsuranceNo = cboInsuranceNo;
    }
    
    /**
     * Getter for property insuranceNoSelectedNSecurityLevel.
     * @return Value of property insuranceNoSelectedNSecurityLevel.
     */
    public java.util.ArrayList getInsuranceNoSelectedNSecurityLevel() {
        return insuranceNoSelectedNSecurityLevel;
    }
    
    /**
     * Setter for property insuranceNoSelectedNSecurityLevel.
     * @param insuranceNoSelectedNSecurityLevel New value of property insuranceNoSelectedNSecurityLevel.
     */
    public void setInsuranceNoSelectedNSecurityLevel(java.util.ArrayList insuranceNoSelectedNSecurityLevel) {
        this.insuranceNoSelectedNSecurityLevel = insuranceNoSelectedNSecurityLevel;
    }
    
    /**
     * Getter for property strStoredInsuranceNo.
     * @return Value of property strStoredInsuranceNo.
     */
    public java.lang.String getStrStoredInsuranceNo() {
        return strStoredInsuranceNo;
    }
    
    /**
     * Setter for property strStoredInsuranceNo.
     * @param strStoredInsuranceNo New value of property strStoredInsuranceNo.
     */
    public void setStrStoredInsuranceNo(java.lang.String strStoredInsuranceNo) {
        this.strStoredInsuranceNo = strStoredInsuranceNo;
    }
    
    /**
     * Getter for property securityAll.
     * @return Value of property securityAll.
     */
    public java.util.LinkedHashMap getSecurityAll() {
        return securityAll;
    }
    
    /**
     * Setter for property securityAll.
     * @param securityAll New value of property securityAll.
     */
    public void setSecurityAll(java.util.LinkedHashMap securityAll) {
        this.securityAll = securityAll;
    }
    
    /**
     * Getter for property statusBy.
     * @return Value of property statusBy.
     */
    public java.lang.String getStatusBy() {
        return statusBy;
    }    
   
    /**
     * Setter for property statusBy.
     * @param statusBy New value of property statusBy.
     */
    public void setStatusBy(java.lang.String statusBy) {
        this.statusBy = statusBy;
    }    
    
    /**
     * Getter for property authorizeStatus1.
     * @return Value of property authorizeStatus1.
     */
    public java.lang.String getAuthorizeStatus1() {
        return authorizeStatus1;
    }
    
    /**
     * Setter for property authorizeStatus1.
     * @param authorizeStatus1 New value of property authorizeStatus1.
     */
    public void setAuthorizeStatus1(java.lang.String authorizeStatus1) {
        this.authorizeStatus1 = authorizeStatus1;
    }
    
    /**
     * Getter for property txtGrossWeight.
     * @return Value of property txtGrossWeight.
     */
    public java.lang.String getTxtGrossWeight() {
        return txtGrossWeight;
    }
    
    /**
     * Setter for property txtGrossWeight.
     * @param txtGrossWeight New value of property txtGrossWeight.
     */
    public void setTxtGrossWeight(java.lang.String txtGrossWeight) {
        this.txtGrossWeight = txtGrossWeight;
    }
    
}
