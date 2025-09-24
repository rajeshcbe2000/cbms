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

package com.see.truetransact.ui.customer.goldsecurity;

/**
 *
 * @author  shanmuga
 *
 */

import com.see.truetransact.ui.customer.security.*;
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
import com.see.truetransact.transferobject.customer.goldsecurity.CustomerGoldSecurityTO;
import com.see.truetransact.ui.TrueTransactMain;
import com.see.truetransact.uicomponent.CObservable;
import com.see.truetransact.uicomponent.COptionPane;
import java.util.*;

import org.apache.log4j.Logger;


public class CustomerGoldSecurityOB extends CObservable{
    
    /** Creates a new instance of SecurityOB */
    private CustomerGoldSecurityOB() throws Exception{
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
    
    private       static CustomerGoldSecurityOB securityOB;
    //private       static InsuranceOB insuranceOB;
    
    private final static Logger log = Logger.getLogger(CustomerGoldSecurityOB.class);
    private final static ClientParseException parseException = ClientParseException.getInstance();
    
    private double totalSecurityValue = 0;
    private double securityValue = 0;
    
    private ProxyFactory proxy = null;
    
    private final   CustomerGoldSecurityRB objSecurityInsuranceRB = new CustomerGoldSecurityRB();
    
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
    private final   String  GOLD_SECURITY_ID = "GOLD_SECURITY_ID";
    
    private final   String  PURITY = "PURITY";
    private final   String  MARKET_RATE = "MARKET_RATE";
    private final   String  PLEDGE_AMT = "PLEDGE_AMT";
    private final   String  APPRAISER_ID = "APPRAISER_ID";
    
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
    private ComboBoxModel cbmPurity;
    
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
    private String cboPurity = "";
    
    private String txtNetWeight = "";
    private String txtMarketRate = "";
    private String tdtAsOnDt = "";
   
    private String txtPledgeAmount = "";
    private ComboBoxModel cbmAppraiserId;
    private String cboAppraiserId = "";
    private String txtCustId = "";
    private String txtGoldSecurityId = "";
    private String txtMemberNo = "";
    private String photoFile;
    private byte[] photoByteArray; 
    
    
    private LinkedHashMap goldItemMap = new LinkedHashMap();
    static {
        try {
            securityOB = new CustomerGoldSecurityOB();
        } catch(Exception e) {
            log.info("try: " + e);
            parseException.logException(e,true);
        }
    }
    
    private void securityOB()  throws Exception{
        //insuranceOB = InsuranceOB.getInstance();
        insuranceNoSelectedNSecurityLevel = new ArrayList();
        fillDropdown();
        setSecurityTabTitle();
        setSecurityLoanTabTitle();
        tableUtilSecurity.setAttributeKey(SLNO);
        tblSecurityTab = new EnhancedTableModel(null, securityTabTitle);
        tblSecurityLoanTab = new EnhancedTableModel(null, securityLoanTabTitle);
    }
    
    public static CustomerGoldSecurityOB getInstance() {
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
        lookup_keys.add("TERMLOAN.GOLDITEM");
        lookup_keys.add("GOLD_CONFIGURATION");
        
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
        
       
        
        getKeyValue((HashMap)keyValue.get("CUSTOMER.CITY"));
        setCbmCity(new ComboBoxModel(key,value));
        
        getKeyValue((HashMap)keyValue.get("GOLD_CONFIGURATION"));
        setCbmPurity(new ComboBoxModel(key,value));
        
        setBlankKeyValue();
        setCbmInsuranceNo(new ComboBoxModel(key, value));
        
        setBlankKeyValue();
        getKeyValue((HashMap) keyValue.get("TERMLOAN.GOLDITEM"));        
        ArrayList<String> itemValue = value;
        Collections.sort(itemValue);
        for (int i = 0; i < itemValue.size(); i++) {
            if (!itemValue.get(i).equals("")) {
                goldItemMap.put(itemValue.get(i).toUpperCase(), "1");
            }
        }
        
    
        //////
        HashMap param = new HashMap();
        param.put(CommonConstants.MAP_NAME, "getAppraiserCode");
        HashMap whereMap1 = new HashMap();
        whereMap1.put(CommonConstants.BRANCH_ID, ProxyParameters.BRANCH_ID);
        param.put(CommonConstants.PARAMFORQUERY, whereMap1);
        keyValue = ClientUtil.populateLookupData(param);
        fillData((HashMap) keyValue.get(CommonConstants.DATA));
        cbmAppraiserId = new ComboBoxModel(key, value);
        
        ////
        
        
        lookup_keys = null;
    }
    
     private void fillData(HashMap keyValue) throws Exception {
        key = (ArrayList) keyValue.get("KEY");
        value = (ArrayList) keyValue.get("VALUE");
    }
    
    private void setOperationMap() throws Exception{
        operationMap = new HashMap();
        operationMap.put(CommonConstants.JNDI, "CustomerGoldSecurityJNDI");
        operationMap.put(CommonConstants.HOME, "CustomerGoldSecurityHome");
        operationMap.put(CommonConstants.REMOTE, "CustomerGoldSecurity");
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
            securityLoanTabTitle.add("Prod Type");
            securityLoanTabTitle.add("Prod Id");
            securityLoanTabTitle.add("Acct No");
            securityLoanTabTitle.add("As On Dt");
            securityLoanTabTitle.add("Pledge Amt");
            securityLoanTabTitle.add("Balance Amt");
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
        //insuranceOB.resetAllInsuranceDetails();
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
        setTxtCustId("");
        setCboAppraiserId("");
        setTxtGoldSecurityId("");
        setTxtMemberNo("");
        setTdtAsOnDt("");
        setPhotoByteArray(null);      
    }
    
    public void resetSecurityDetails(){
        setTxtSecurityNo("");
        setCboPurity("");
        setTxtGrossWeight("");
        setTxtNetWeight("");
        setTxtMarketRate("");      
        setTxtSecurityValue("");
        setTdtAsOnDt("");
        setTxtParticulars("");
        setTxtPledgeAmount("");        
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
        //System.out.println("map :::" + map);
//        map :::{CUSTOMER=Soman T N  , ACCOUNT STATUS=PROVISIONAL, SHARE TYPE=AA, CUST_ID=C010003711, SHARE DETAIL NO=1,
//                CUSTOMER NAME=Soman T N  , CUSTOMER ID=C010003711, SHARE ACCOUNT NO=AA5390}
        
//        map :::{CARE_OF_NAME=Sasi, NAME=Shanil , DOB=10/05/1980, 
//        ADDRESS=Maliyekkal House Randamkallu, P O Chengaloor CHENGALOOR, CARE_OF=FATHER, STATUS=CREATED, CUST_TYPE=INDIVIDUAL, CUSTOMER NAME=null, CUSTOMER ID=C000092723, RETIREMENT_DT=2040-05-10 00:00:00.0, UNIQUE_ID=9571 8248 5405, AADHAR NO=null, COMMUNICATION ADDRESS=HOME, CUST_ID=C000092723, 
//        PHONE_NO=9605569914, BRANCH_CODE=0001, PAN_NO=null, PASSPORT_NO=null, MEMBER_NO=AA14750}
        
        try{
            HashMap transactionMap = new HashMap();
            HashMap retrieve = new HashMap();
            String strCustID = CommonUtil.convertObjToStr(map.get("CUSTOMER ID"));
            String strCustName = CommonUtil.convertObjToStr(map.get("CUSTOMER NAME"));
            transactionMap.put("custId", strCustID);
            setTxtCustId(strCustID);
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
            resultList = ClientUtil.executeQuery("getMemberNoOfCustomer", transactionMap);
             if (resultList.size() > 0){
                retrieve = (HashMap) resultList.get(0);
                setTxtMemberNo(CommonUtil.convertObjToStr(retrieve.get("MEMBER_NO")));
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
        //System.out.println("getTblSecurityTab() :: " + getTblSecurityTab().getDataArrayList());
        //removeAllInsuranceNo();
        setSecurityTO((ArrayList) (mapData.get("SecurityTO")));
          if (mapData.containsKey("STOCK_PHOTO_FILE") && mapData.get("STOCK_PHOTO_FILE") != null) {// Added by nithya on 29-10-2019 for KD-763	Need Gold ornaments photo saving option
            HashMap photoMap = (HashMap) mapData.get("STOCK_PHOTO_FILE");           
                if (photoMap.containsKey("PHOTO") && photoMap.get("PHOTO") != null) {
                    setPhotoByteArray((byte[]) photoMap.get("PHOTO"));
                }            
        }
       
        
        ttNotifyObservers();
    }
    
    public void setSecurityTO(ArrayList objSecurityTOList){
        try{
            CustomerGoldSecurityTO objSecurityTO;
            HashMap securityRecordMap;
            String strCustID = "";
            ArrayList securityRecordList;
            ArrayList securityCTableValues = new ArrayList();
            ArrayList removedValues = new ArrayList();
            LinkedHashMap allLocalRecs = new LinkedHashMap();
            // To set the Total Security Value as ZERO
            setTotalSecurityValue(0);
            
           
            for (int i = objSecurityTOList.size() - 1,j = 0;i >= 0;--i,++j){
                objSecurityTO = (CustomerGoldSecurityTO) objSecurityTOList.get(j);
                securityRecordMap  = new HashMap();
                securityRecordList = new ArrayList();                
                securityRecordList.add(CommonUtil.convertObjToStr(objSecurityTO.getSecurityNo()));
                securityRecordList.add("Gold");
                securityRecordList.add(CommonUtil.convertObjToStr(objSecurityTO.getSecurityValue()));
                securityRecordList.add(DateUtil.getStringDate(objSecurityTO.getAsondt()));
                securityCTableValues.add(securityRecordList);                
                securityRecordMap.put(SLNO, CommonUtil.convertObjToStr(objSecurityTO.getSecurityNo())); 
                securityRecordMap.put(GOLD_SECURITY_ID,CommonUtil.convertObjToStr(objSecurityTO.getGoldSecurityId()));
                securityRecordMap.put(SECURITY_VALUE, CommonUtil.convertObjToStr(objSecurityTO.getSecurityValue()));
                securityRecordMap.put(WEIGHT, CommonUtil.convertObjToStr(objSecurityTO.getNetWeight()));
                securityRecordMap.put(GROSSWEIGHT, CommonUtil.convertObjToStr(objSecurityTO.getGrossWeight()));                
                calculateSecurityValue(CommonUtil.convertObjToDouble(objSecurityTO.getAvailableSecurityValue()).doubleValue());
                addSecurityValue(getSecurityValue());                
                securityRecordMap.put(CUSTOMER_ID, objSecurityTO.getCustId());
                strCustID = objSecurityTO.getCustId();
                securityRecordMap.put(ASON, DateUtil.getStringDate(objSecurityTO.getAsondt()));
                securityRecordMap.put(PLEDGE_AMT, CommonUtil.convertObjToStr(objSecurityTO.getPledgeAmt()));
                securityRecordMap.put(MARKET_RATE, CommonUtil.convertObjToStr(objSecurityTO.getMarketRate()));
                securityRecordMap.put(APPRAISER_ID, CommonUtil.convertObjToStr(objSecurityTO.getAppraiserId()));
                securityRecordMap.put(PURITY, CommonUtil.convertObjToStr(objSecurityTO.getPurity()));
                securityRecordMap.put(PARTICULARS, objSecurityTO.getParticulars());
                securityRecordMap.put(SECURITY_CATEGORY, "Gold");                
                securityRecordMap.put(AUTHORIZE_BY, CommonUtil.convertObjToStr(objSecurityTO.getAuthorizeBy()));
                securityRecordMap.put(AUTHORIZE_DT, DateUtil.getStringDate(objSecurityTO.getAuthorizeDt()));
                securityRecordMap.put(AUTHORIZE_STATUS, CommonUtil.convertObjToStr(objSecurityTO.getAuthorizeStatus()));
                securityRecordMap.put(AUTHORIZE_REMARKS, CommonUtil.convertObjToStr(objSecurityTO.getAuthorizeRemarks()));
                securityRecordMap.put(STATUS_BY,CommonUtil.convertObjToStr(objSecurityTO.getStatusBy()));
                //System.out.println("$$MAP"+securityRecordMap);
                securityRecordMap.put(COMMAND, UPDATE);                
                allLocalRecs.put(CommonUtil.convertObjToStr(objSecurityTO.getSecurityNo()), securityRecordMap);  //List of values corresponding to the table
                setStatusBy(objSecurityTO.getStatusBy());
                setAuthorizeStatus1(objSecurityTO.getAuthorizeStatus());
                setTxtGoldSecurityId(objSecurityTO.getGoldSecurityId());
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
            List resultList = ClientUtil.executeQuery("getSelectCustomerGoldSecurityMaxSLNO", transactionMap);
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
        //HashMap objInsuranceTOMap = insuranceOB.setInsurance(getCommand());
        
        HashMap data = new HashMap();
        
        data.put("SecurityTO", objSecurityTOMap);
        data.put("PHOTO", this.photoByteArray);
        //data.put("InsuranceTO", objInsuranceTOMap);
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
        //objInsuranceTOMap = null;
        data = null;
    }
    
    public HashMap setSecurity(String strCommand){
        HashMap securityMap = new HashMap();
        //System.out.println("securityAll :: " + securityAll);
        //System.out.println("gold security :: " + getTxtGoldSecurityId());
        try{
            CustomerGoldSecurityTO objSecurityTO;
            java.util.Set keySet =  securityAll.keySet();
            Object[] objKeySet = (Object[]) keySet.toArray();
            HashMap oneRecord;
            
            // To set the values for Security Transfer Object
            for (int i = keySet.size() - 1,j = 0;i >= 0;--i,++j){
                oneRecord = (HashMap) securityAll.get(objKeySet[j]);
                objSecurityTO = new CustomerGoldSecurityTO();
                objSecurityTO.setCustId(CommonUtil.convertObjToStr(oneRecord.get(CUSTOMER_ID)));                             
                objSecurityTO.setCommand(CommonUtil.convertObjToStr(oneRecord.get(COMMAND)));
                objSecurityTO.setAvailableSecurityValue(CommonUtil.convertObjToDouble(oneRecord.get(AVAL_SEC_VAL)));
                objSecurityTO.setParticulars(CommonUtil.convertObjToStr(oneRecord.get(PARTICULARS)));
                objSecurityTO.setSecurityCategory("Gold");
                objSecurityTO.setSecurityNo(CommonUtil.convertObjToDouble(oneRecord.get(SLNO)));                
                objSecurityTO.setSecurityValue(CommonUtil.convertObjToDouble(oneRecord.get(SECURITY_VALUE)));
                objSecurityTO.setNetWeight(CommonUtil.convertObjToDouble(oneRecord.get(WEIGHT)));
                objSecurityTO.setGrossWeight(CommonUtil.convertObjToDouble(oneRecord.get(GROSSWEIGHT)));
                if(CommonUtil.convertObjToStr(oneRecord.get(COMMAND)).equalsIgnoreCase("UPDATE")){
                    objSecurityTO.setGoldSecurityId(CommonUtil.convertObjToStr(oneRecord.get(GOLD_SECURITY_ID)));
                }
//                
//                securityAll :: {1={ASON=25/01/2020, APPRAISER_ID=E11, PURITY=22, GROSSWEIGHT=20, CUSTOMER_ID=C000092837, SLNO=1, SECURITY_VALUE=100000.00, SECURITY_TYPE=Gold, PARTICULARS=GOLD PIECE-1
//JIMKY-1
//LOCKET-1
//, WEIGHT=20, AUTHORIZE_REMARKS=null, AUTHORIZE_DT=null, AUTHORIZE_BY=null, AUTHORIZE_STATUS=null, MARKET_RATE=3720.0, COMMAND=INSERT}}
                objSecurityTO.setMarketRate(CommonUtil.convertObjToStr(oneRecord.get(MARKET_RATE)));
                objSecurityTO.setPurity(CommonUtil.convertObjToStr(oneRecord.get(PURITY)));
                objSecurityTO.setPledgeAmt(CommonUtil.convertObjToDouble(oneRecord.get(PLEDGE_AMT))); 
                objSecurityTO.setStatus(CommonUtil.convertObjToStr(oneRecord.get(COMMAND)));  
                objSecurityTO.setAppraiserId(CommonUtil.convertObjToStr(oneRecord.get(APPRAISER_ID)));  
                objSecurityTO.setAuthorizeBy(CommonUtil.convertObjToStr(oneRecord.get(AUTHORIZE_BY)));
                objSecurityTO.setAsondt(DateUtil.getDateMMDDYYYY(CommonUtil.convertObjToStr(oneRecord.get(ASON))));           
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
                objSecurityTO.setGoldSecurityId(getTxtGoldSecurityId());
                securityMap.put(String.valueOf(j+1), objSecurityTO);
                oneRecord = null;
                objSecurityTO = null;
            }
            
            // To set the values for Security Transfer Object
            // where as the existing records in Database are deleted in client side
            // useful for updating the status
            for (int i = tableUtilSecurity.getRemovedValues().size() - 1,j = 0;i >= 0;--i,++j){
                oneRecord = (HashMap) tableUtilSecurity.getRemovedValues().get(j);
                objSecurityTO = new CustomerGoldSecurityTO();
                objSecurityTO.setCustId(CommonUtil.convertObjToStr(oneRecord.get(CUSTOMER_ID)));
                
                
                objSecurityTO.setCommand(CommonUtil.convertObjToStr(oneRecord.get(COMMAND)));
                
                
               
               
                objSecurityTO.setAvailableSecurityValue(CommonUtil.convertObjToDouble(oneRecord.get(AVAL_SEC_VAL)));
                objSecurityTO.setParticulars(CommonUtil.convertObjToStr(oneRecord.get(PARTICULARS)));
                objSecurityTO.setSecurityCategory(CommonUtil.convertObjToStr(oneRecord.get("")));
                objSecurityTO.setSecurityNo(CommonUtil.convertObjToDouble(oneRecord.get(SLNO)));
               
                objSecurityTO.setSecurityValue(CommonUtil.convertObjToDouble(oneRecord.get(SECURITY_VALUE)));
                objSecurityTO.setNetWeight(CommonUtil.convertObjToDouble(oneRecord.get(WEIGHT)));
                
                Date AsDt1 = DateUtil.getDateMMDDYYYY(CommonUtil.convertObjToStr(oneRecord.get(ASON)));
                if(AsDt1 != null){
                Date asDate1 = (Date) curDate.clone();
                asDate1.setDate(AsDt1.getDate());
                asDate1.setMonth(AsDt1.getMonth());
                asDate1.setYear(AsDt1.getYear());
                objSecurityTO.setAsondt(asDate1);
                }else{
                    objSecurityTO.setAsondt(DateUtil.getDateMMDDYYYY(CommonUtil.convertObjToStr(oneRecord.get(ASON))));
                }
                objSecurityTO.setStatus(CommonUtil.convertObjToStr(oneRecord.get(COMMAND)));
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
        //insuranceOB.ttNotifyObservers();
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
        //insuranceOB.setLblCustPin_Disp(lblCustPin_Disp);
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
        //insuranceOB.setLblCustStreet_Disp(lblCustStreet_Disp);
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
        //insuranceOB.setLblCustCity_Disp(lblCustCity_Disp);
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
        //insuranceOB.setLblCustEmail_ID_Disp(lblCustEmail_ID_Disp);
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
        //insuranceOB.setLblCustName_Disp(lblCustName_Disp);
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
        //insuranceOB.setLblCustID_Disp(lblCustID_Disp);
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
                //System.out.println("securityEachTabRecord :: " + securityEachTabRecord);
                //System.out.println("securityEachRecord :: " + securityEachRecord);
                result = tableUtilSecurity.insertTableValues(securityEachTabRecord, securityEachRecord);
                //System.out.println("result ::::" + result);
                securityTabValues = (ArrayList) result.get(TABLE_VALUES);
                securityAll = (LinkedHashMap) result.get(ALL_VALUES);
                option = CommonUtil.convertObjToInt(result.get(OPTION));
                //System.out.println("securityTabValues :: " + securityTabValues);
                //System.out.println("securityAll :: " + securityAll);
                tblSecurityTab.setDataArrayList(securityTabValues, securityTabTitle);
                //System.out.println("tblSecurityTab.getdataarraylist :: " + tblSecurityTab.getDataArrayList());
                String securityNo = ((String) ((ArrayList) tblSecurityTab.getDataArrayList().get(tblSecurityTab.getRowCount() - 1)).get(0));
              
                // To calculate Total Security value(Average)
                calculateSecurityValue(CommonUtil.convertObjToDouble(getTxtSecurityValue()).doubleValue());
                addSecurityValue(getSecurityValue());
                displayTotalSecurityValue();
               
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
        securityEachTabRecord.add("Gold");
        securityEachTabRecord.add(getTxtSecurityValue());
        securityEachTabRecord.add(getTdtAsOnDt());
        securityEachRecord.put(CUSTOMER_ID, getTxtCustId());
        securityEachRecord.put(SLNO, "");        
        securityEachRecord.put(SECURITY_TYPE, "Gold");        
        securityEachRecord.put(PURITY, getCboPurity());
        securityEachRecord.put(GROSSWEIGHT, getTxtGrossWeight());
        securityEachRecord.put(WEIGHT, getTxtNetWeight());
        securityEachRecord.put(SECURITY_VALUE, getTxtSecurityValue());
        securityEachRecord.put(MARKET_RATE, getTxtMarketRate());
        securityEachRecord.put(PARTICULARS, getTxtParticulars());   
        securityEachRecord.put(PLEDGE_AMT, getTxtPledgeAmount());   
        securityEachRecord.put(APPRAISER_ID, getCboAppraiserId());   
        securityEachRecord.put(ASON, getTdtAsOnDt());            
        securityEachRecord.put(COMMAND, "");
        securityEachRecord.put(AUTHORIZE_BY, "");
        securityEachRecord.put(AUTHORIZE_DT, null);
        securityEachRecord.put(AUTHORIZE_REMARKS, "");
        securityEachRecord.put(AUTHORIZE_STATUS, "");
        
        //System.out.println("********** securityEachRecord ::" + securityEachRecord);
        
    }
    
    private int updateSecurityTab(int recordPosition){
        int option = -1;
        HashMap result = new HashMap();
        try{
            
            //System.out.println("inside updateSecurityTab securityAll :: " +  securityAll);
            String strRecordKey = CommonUtil.convertObjToStr(((ArrayList) (tblSecurityTab.getDataArrayList().get(recordPosition))).get(0));
            HashMap eachRecs = (HashMap) securityAll.get(strRecordKey);
            double secValueBeforeUpdate = CommonUtil.convertObjToDouble(eachRecs.get(AVAL_SEC_VAL)).doubleValue();
            double secValue = CommonUtil.convertObjToDouble(getTxtAvalSecVal()).doubleValue();
            result = tableUtilSecurity.updateTableValues(securityEachTabRecord, securityEachRecord, recordPosition);
            //System.out.println("inside updateSecurityTab result :: " +  result);
            securityTabValues = (ArrayList) result.get(TABLE_VALUES);
            securityAll = (LinkedHashMap) result.get(ALL_VALUES);
            option = CommonUtil.convertObjToInt(result.get(OPTION));
            //System.out.println("inside updateSecurityTab securityTabValues :: " +  securityTabValues);
            //System.out.println("inside updateSecurityTab securityAll :: " +  securityAll);
            //System.out.println("inside updateSecurityTab option :: " +  option);
            
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
            //System.out.println("securityAll in populateSecurityDetails :: " + securityAll);
            for (int i = keySet.size() - 1, j = 0;i >= 0;--i, ++j){
                if ((CommonUtil.convertObjToStr(((HashMap) securityAll.get(objKeySet[j])).get(SLNO))).equals(strRecordKey)){
                    // To populate the Corresponding record from CTable
                    eachRecs = (HashMap) securityAll.get(objKeySet[j]);
                    setTxtSecurityNo(CommonUtil.convertObjToStr(eachRecs.get(SLNO)));
                    setTxtCustId(CommonUtil.convertObjToStr(eachRecs.get(CUSTOMER_ID)));
                    setTxtGoldSecurityId(CommonUtil.convertObjToStr(eachRecs.get(GOLD_SECURITY_ID)));
                    setTxtSecurityValue(CommonUtil.convertObjToStr(eachRecs.get(SECURITY_VALUE)));
                    setCboPurity(CommonUtil.convertObjToStr(getCbmPurity().getDataForKey(CommonUtil.convertObjToStr(eachRecs.get(PURITY)))));
                    setTxtGrossWeight(CommonUtil.convertObjToStr(eachRecs.get(GROSSWEIGHT)));
                    setTxtNetWeight(CommonUtil.convertObjToStr(eachRecs.get(WEIGHT)));
                    setTxtMarketRate(CommonUtil.convertObjToStr(eachRecs.get(MARKET_RATE)));                    
                    setTdtAsOnDt(CommonUtil.convertObjToStr(eachRecs.get(ASON)));
                    setTxtParticulars(CommonUtil.convertObjToStr(eachRecs.get(PARTICULARS)));
                    setTxtPledgeAmount(CommonUtil.convertObjToStr(eachRecs.get(PLEDGE_AMT)));
                    setCboAppraiserId(CommonUtil.convertObjToStr(getCbmAppraiserId().getDataForKey(CommonUtil.convertObjToStr(eachRecs.get(APPRAISER_ID)))));                   
                    setTxtAvalSecVal(CommonUtil.convertObjToStr(eachRecs.get(AVAL_SEC_VAL)));       
                    
                    break;
                }
                eachRecs = null;
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
//        try{
//            ArrayList securityData = tblSecurityTab.getDataArrayList();
//            String securityNo = CommonUtil.convertObjToStr(((ArrayList) securityData.get(selectedRow)).get(0));
//            ArrayList insuranceData = insuranceOB.getSecurityNoSelectedNInsuranceLevel();
//            
//            if (insuranceData.contains(securityNo) || strInsuranceNumber.length() > 0){
//                // This Security No. has Insurance Details
//                stbWarnMsg.append("\n");
//                stbWarnMsg.append(objSecurityInsuranceRB.getString("lblSecurityNo_Insurance"));
//                stbWarnMsg.append(securityNo);
//                stbWarnMsg.append(objSecurityInsuranceRB.getString("hasInsuranceWarning"));
//            }
////            if ((tblSecurityTab.getRowCount() == 1) && (insuranceOB.getTblInsuranceTab().getRowCount() > 0)){
////                String[] options = {objSecurityInsuranceRB.getString("cDialogOk")};
////                option = COptionPane.showOptionDialog(null, objSecurityInsuranceRB.getString("existanceSecurityNoWarning"), CommonConstants.WARNINGTITLE,
////                COptionPane.OK_OPTION, COptionPane.WARNING_MESSAGE, null, options, options[0]);
////                delete = false;
////                return delete;
////            }
//        }catch(Exception e){
//            log.info("Error in insuranceExistWarning..."+e);
//            parseException.logException(e,true);
//        }
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
//        try{
//            int insuranceRowCount = insuranceOB.getInsuranceTabValues().size();
//            ArrayList insuranceWithOutSecurityList = new ArrayList();
//            for (int i = insuranceRowCount - 1,j = 0;i >= 0;--i,++j){
//                if (CommonUtil.convertObjToStr(((ArrayList)insuranceOB.getInsuranceTabValues().get(j)).get(1)).length() <= 0){
//                    insuranceWithOutSecurityList.add(((ArrayList)insuranceOB.getInsuranceTabValues().get(j)).get(0));
//                }
//            }
//            for (int i = insuranceWithOutSecurityList.size() - 1;i >= 0;--i){
//                if (getInsuranceNoSelectedNSecurityLevel().contains(insuranceWithOutSecurityList.get(i))){
//                    insuranceWithOutSecurityList.remove(i);
//                }
//            }
//            if (insuranceWithOutSecurityList.size() > 0){
//                stbWarnMsg.append("\n");
//                stbWarnMsg.append(objSecurityInsuranceRB.getString("lblInsuranceNo_Disp"));
//                stbWarnMsg.append(insuranceWithOutSecurityList.toString());
//                stbWarnMsg.append(objSecurityInsuranceRB.getString("insuranceDoesntExistWarning"));
//            }
//        }catch(Exception e){
//            log.info("Exception caught in calculateRemainingSecurityVal: "+e);
//            parseException.logException(e,true);
//        }
        return stbWarnMsg.toString();
    }
    
    public String chkSecValueGTInsuAmt(){
        StringBuffer stbWarnMsg = new StringBuffer("");
//        try{
//            ArrayList secValLessThanInsAmtList = new ArrayList();
//            HashMap securityInsuranceVal = new HashMap();
//            LinkedHashMap insuranceMap = insuranceOB.getInsuranceAll();
//            int insuranceRowCount = insuranceOB.getInsuranceTabValues().size();
//            int securityRowCount = getSecurityAll().size();
//            java.util.Set keySet =  getSecurityAll().keySet();
//            Object[] objKeySet = (Object[]) keySet.toArray();
//            java.util.Set insuranceKeySet =  insuranceOB.getInsuranceAll().keySet();
//            Object[] objInsurKeySet = (Object[]) insuranceKeySet.toArray();
//            String strInsuranceNo = "";
//            
//            for (int i = securityRowCount - 1, j = 0;i >= 0;--i,++j){
//                strInsuranceNo = CommonUtil.convertObjToStr(((HashMap)getSecurityAll().get(objKeySet[j])).get(INSURANCE_NO));
//                if (strInsuranceNo.length() > 0){
//                    insuranceOB.getInsuranceAll().get(strInsuranceNo);
//                    securityInsuranceVal.put(objKeySet[j], CommonUtil.convertObjToDouble(((HashMap)insuranceOB.getInsuranceAll().get(strInsuranceNo)).get(POLICY_AMT)));
//                }
//                strInsuranceNo = null;
//            }
//            
//            String strSecurityNo = "";
//            double insuranceAmt = 0.0;
//            for (int i = insuranceRowCount - 1, j = 0;i >= 0;--i,++j){
//                strSecurityNo = CommonUtil.convertObjToStr(((HashMap)insuranceOB.getInsuranceAll().get(objInsurKeySet[j])).get(SECURITY_NO));
//                if (strSecurityNo.length() > 0){
//                    insuranceAmt = CommonUtil.convertObjToDouble(((HashMap)insuranceOB.getInsuranceAll().get(objInsurKeySet[j])).get(POLICY_AMT)).doubleValue();
//                    insuranceAmt += CommonUtil.convertObjToDouble(securityInsuranceVal.get(strSecurityNo)).doubleValue();
//                    securityInsuranceVal.put(strSecurityNo, String.valueOf(insuranceAmt));
//                }
//                strSecurityNo = null;
//            }
//            
//            java.util.Set secWithInsurancekeySet =  securityInsuranceVal.keySet();
//            Object[] objSecWithInsuranceKeySet = (Object[]) secWithInsurancekeySet.toArray();
//            double secAmt = 0.0;
//            for (int i = secWithInsurancekeySet.size() - 1, j = 0;i >= 0;--i,++j){
//                if (getSecurityAll().containsKey(objSecWithInsuranceKeySet[j])){
//                    secAmt = CommonUtil.convertObjToDouble(((HashMap) getSecurityAll().get(objSecWithInsuranceKeySet[j])).get(SECURITY_VALUE)).doubleValue();
//                    insuranceAmt = CommonUtil.convertObjToDouble(securityInsuranceVal.get(objSecWithInsuranceKeySet[j])).doubleValue();
//                    if (insuranceAmt < secAmt){
//                        secValLessThanInsAmtList.add(objSecWithInsuranceKeySet[j]);
//                    }
//                }
//            }
//            
//            if (secValLessThanInsAmtList.size() > 0){
//                stbWarnMsg.append("\n");
//                stbWarnMsg.append(objSecurityInsuranceRB.getString("insuranceAmtLessThanSecAmtWarning"));
//                stbWarnMsg.append(secValLessThanInsAmtList.toString());
//            }
//            
//            secWithInsurancekeySet = null;
//            objSecWithInsuranceKeySet = null;
//            keySet = null;
//            objKeySet = null;
//            insuranceKeySet = null;
//            objInsurKeySet = null;
//            strSecurityNo = null;
//            strInsuranceNo = null;
//            securityInsuranceVal = null;
//            insuranceMap = null;
//            secValLessThanInsAmtList = null;
//        }catch(Exception e){
//            log.info("Exception caught in chkSecValueGTInsuAmt: "+e);
//            parseException.logException(e,true);
//        }
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

    public LinkedHashMap getGoldItemMap() {
        return goldItemMap;
    }

    public void setGoldItemMap(LinkedHashMap goldItemMap) {
        this.goldItemMap = goldItemMap;
    }

    public ComboBoxModel getCbmPurity() {
        return cbmPurity;
    }

    public void setCbmPurity(ComboBoxModel cbmPurity) {
        this.cbmPurity = cbmPurity;
    }

    public String getCboPurity() {
        return cboPurity;
    }

    public void setCboPurity(String cboPurity) {
        this.cboPurity = cboPurity;
    }

    public String getTdtAsOnDt() {
        return tdtAsOnDt;
    }

    public void setTdtAsOnDt(String tdtAsOnDt) {
        this.tdtAsOnDt = tdtAsOnDt;
    }

    public String getTxtMarketRate() {
        return txtMarketRate;
    }

    public void setTxtMarketRate(String txtMarketRate) {
        this.txtMarketRate = txtMarketRate;
    }

    public String getTxtNetWeight() {
        return txtNetWeight;
    }

    public void setTxtNetWeight(String txtNetWeight) {
        this.txtNetWeight = txtNetWeight;
    }

    public String getTxtPledgeAmount() {
        return txtPledgeAmount;
    }

    public void setTxtPledgeAmount(String txtPledgeAmount) {
        this.txtPledgeAmount = txtPledgeAmount;
    }

    public ComboBoxModel getCbmAppraiserId() {
        return cbmAppraiserId;
    }

    public void setCbmAppraiserId(ComboBoxModel cbmAppraiserId) {
        this.cbmAppraiserId = cbmAppraiserId;
    }

    public String getCboAppraiserId() {
        return cboAppraiserId;
    }

    public void setCboAppraiserId(String cboAppraiserId) {
        this.cboAppraiserId = cboAppraiserId;
    }

    public String getTxtCustId() {
        return txtCustId;
    }

    public void setTxtCustId(String txtCustId) {
        this.txtCustId = txtCustId;
    }

    public String getTxtGoldSecurityId() {
        return txtGoldSecurityId;
    }

    public void setTxtGoldSecurityId(String txtGoldSecurityId) {
        this.txtGoldSecurityId = txtGoldSecurityId;
    }

    public String getTxtMemberNo() {
        return txtMemberNo;
    }

    public void setTxtMemberNo(String txtMemberNo) {
        this.txtMemberNo = txtMemberNo;
    }

    public byte[] getPhotoByteArray() {
        return photoByteArray;
    }

    public void setPhotoByteArray(byte[] photoByteArray) {
        this.photoByteArray = photoByteArray;
    }

    public String getPhotoFile() {
        return photoFile;
    }

    public void setPhotoFile(String photoFile) {
        this.photoFile = photoFile;
    }
    
}
