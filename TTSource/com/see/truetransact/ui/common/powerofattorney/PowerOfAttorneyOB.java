/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition. 
 * 
 * PowerOfAttorneyOB.java
 *
 * Created on December 23, 2004, 4:47 PM
 */

package com.see.truetransact.ui.common.powerofattorney;

import com.see.truetransact.clientproxy.ProxyParameters;
import com.see.truetransact.clientutil.ClientConstants;
import com.see.truetransact.clientutil.ClientUtil;
import com.see.truetransact.clientutil.ComboBoxModel;
import com.see.truetransact.clientutil.EnhancedTableModel;
import com.see.truetransact.clientutil.TableUtil;
import com.see.truetransact.clientexception.ClientParseException;
import com.see.truetransact.commonutil.CommonConstants;
import com.see.truetransact.commonutil.DateUtil;
import com.see.truetransact.commonutil.CommonUtil;
import com.see.truetransact.commonutil.TTException;
import com.see.truetransact.transferobject.common.powerofattorney.PowerAttorneyTO;
import com.see.truetransact.ui.TrueTransactMain;
import com.see.truetransact.uicomponent.COptionPane;
import com.see.truetransact.uicomponent.CObservable;

import org.apache.log4j.Logger;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Date;
/**
 *
 * @author  152713
 */
public class PowerOfAttorneyOB extends CObservable{
    
    /** Creates a new instance of PowerOfAttorneyOB */
    public PowerOfAttorneyOB(String strModule){
        powerOfAttorneyOB(strModule);
    }
    
    private       static PowerOfAttorneyOB powerOfAttorneyOB;
    private final static Logger log = Logger.getLogger(PowerOfAttorneyOB.class);
    private final static ClientParseException parseException = ClientParseException.getInstance();
    
    private final   java.util.ResourceBundle objPowerOfAttorneyRB = java.util.ResourceBundle.getBundle("com.see.truetransact.ui.common.powerofattorney.PowerOfAttorneyRB", ProxyParameters.LANGUAGE);
//    private final   PowerOfAttorneyRB objPowerOfAttorneyRB = new PowerOfAttorneyRB();
    
    public          String  strMaxDelSlNoMapName = "";
    private final   String  ADDR_TYPE = "ADDR_TYPE";
    private final   String  ALL_VALUES = "ALL_VALUES";
    private final   String  AREA = "AREA";
    private final   String  CITY = "CITY";
    private final   String  COMMAND = "COMMAND";
    private final   String  COUNTRY = "COUNTRY";
    private final   String  COUNTRY_CODE = "COUNTRY_CODE";
    private final   String  ONBEHALFOF = "ONBEHALFOF";
    private final   String  CUSTOMER_ID = "CUSTOMER_ID";
    private final   String  FROM = "FROM";
    private final   String  HOLDERNAME = "HOLDERNAME";
    private final   String  INSERT = "INSERT";
    private final   String  OPTION = "OPTION";
    private final   String  PHONE = "PHONE";
    private final   String  PIN = "PIN";
    private final   String  PIN_CODE = "PIN_CODE";
    private final   String  POANO = "POANO";
    private final   String  REMARK = "REMARK";
    private final   String  STATE = "STATE";
    private final   String  STREET = "STREET";
    private final   String  TABLE_VALUES = "TABLE_VALUES";
    private final   String  TO = "TO";
    private final   String  UPDATE = "UPDATE";
    
    private final   ArrayList poaTabTitle = new ArrayList();
    private ArrayList poaEachTabRecords;
    private ArrayList poaAllTabRecords;                                 // ArrayList of ArrayList to display in PoA
    
    private LinkedHashMap poaAllRecords = new LinkedHashMap();          // Both displayed and hidden values in the table
    
    private ArrayList key;
    private ArrayList value;
    private HashMap lookUpHash;
    private HashMap poaEachRecords;
    private HashMap keyValue;
    
    private EnhancedTableModel tblPoATab;
    
    private ComboBoxModel cbmCity_PowerAttroney;
    private ComboBoxModel cbmState_PowerAttroney;
    private ComboBoxModel cbmCountry_PowerAttroney;
    private ComboBoxModel cbmPoACust;
    private ComboBoxModel cbmAddrType_PoA;
    
    private TableUtil tableUtilPoA = new TableUtil();
    
    private String borrowerNo = "";
    private String txtCustID_PoA = "";
    private String cboPoACust = "";
    private String cboAddrType_PoA = "";
    private String txtPoANo = "";
    private String txtPoaHolderName = "";
    private String txtStreet_PowerAttroney = "";
    private String txtArea_PowerAttroney = "";
    private String cboCity_PowerAttroney = "";
    private String cboState_PowerAttroney = "";
    private String cboCountry_PowerAttroney = "";
    private String txtPin_PowerAttroney = "";
    private String txtPhone_PowerAttroney = "";
    private String tdtPeriodFrom_PowerAttroney = "";
    private String tdtPeriodTo_PowerAttroney = "";
    private String txtRemark_PowerAttroney = "";
    private Date curDate = null;
    
    private void powerOfAttorneyOB(String strModule){
        try{
            curDate = ClientUtil.getCurrentDate();
            fillDropdown();
            setPoATabTitle();
            tableUtilPoA.setAttributeKey(POANO);
            tblPoATab = new EnhancedTableModel(null, poaTabTitle);
            setStrMaxDelSlNoMapName(strModule);
        }catch(Exception e){
            System.out.println("Exception in powerOfAttorneyOB()..."+e);
            parseException.logException(e,true);
        }
    }
    
    private void setPoATabTitle() throws Exception{
        try {
            poaTabTitle.add(objPowerOfAttorneyRB.getString("tblColumnPoA1"));
            poaTabTitle.add(objPowerOfAttorneyRB.getString("tblColumnPoA2"));
            poaTabTitle.add(objPowerOfAttorneyRB.getString("tblColumnPoA3"));
            poaTabTitle.add(objPowerOfAttorneyRB.getString("tblColumnPoA4"));
        }catch(Exception e) {
            System.out.println("Exception in setPoATabTitle()..."+e);
            parseException.logException(e,true);
        }
    }
    
    public void setStrMaxDelSlNoMapName(String module){
        this.strMaxDelSlNoMapName = "getSelectPowerAttorneyMaxSLNO" + module;
    }
    
    private String getStrMaxDelSlNoMapName(){
        return this.strMaxDelSlNoMapName;
    }
    
    /**
     * To populate the appropriate keys and values of all the combo
     * boxes in the screen at the time of TermLoanOB instance creation
     * @throws Exception will throw it to the TermLoanOB constructor
     */
    public void fillDropdown() throws Exception{
        lookUpHash = new HashMap();
        lookUpHash.put(CommonConstants.MAP_NAME,null);
        
        ArrayList lookup_keys = new ArrayList();
        lookup_keys.add("CUSTOMER.ADDRTYPE");
        lookup_keys.add("CUSTOMER.CITY");
        lookup_keys.add("CUSTOMER.STATE");
        lookup_keys.add("CUSTOMER.COUNTRY");
        
        lookUpHash.put(CommonConstants.PARAMFORQUERY, lookup_keys);
        
        keyValue = ClientUtil.populateLookupData(lookUpHash);
        getKeyValue((HashMap)keyValue.get("CUSTOMER.ADDRTYPE"));
        setCbmAddrType_PoA(new ComboBoxModel(key, value));
        
        getKeyValue((HashMap)keyValue.get("CUSTOMER.CITY"));
        setCbmCity_PowerAttroney(new ComboBoxModel(key,value));
        
        getKeyValue((HashMap)keyValue.get("CUSTOMER.STATE"));
        setCbmState_PowerAttroney(new ComboBoxModel(key,value));
        
        getKeyValue((HashMap)keyValue.get("CUSTOMER.COUNTRY"));
        setCbmCountry_PowerAttroney(new ComboBoxModel(key,value));
        
        setBlankKeyValue();
        setCbmPoACust(new ComboBoxModel(key, value));
        
        lookup_keys = null;
    }
    
    private void getKeyValue(HashMap keyValue)  throws Exception{
        key = (ArrayList)keyValue.get(CommonConstants.KEY);
        value = (ArrayList)keyValue.get(CommonConstants.VALUE);
    }
    
    private void setBlankKeyValue(){
        key = new ArrayList();
        key.add("");
        value = new ArrayList();
        value.add("");
    }
    
    private void resetPowerOfAttorneyTable(){
        tblPoATab = new EnhancedTableModel(null, poaTabTitle);
        ttNotifyObservers();
    }
    
    public void resetAllFieldsInPoA(){
        resetPoAForm();
        destroyObjects();
        createObject();
        clearCboPoACust_ID();
        resetPowerOfAttorneyTable();
    }
    
    public void resetPoAForm(){
        setCboPoACust("");
        setCboAddrType_PoA("");
        setTxtPoANo("");
        setCboCountry_PowerAttroney("");
        setCboCity_PowerAttroney("");
        setTdtPeriodFrom_PowerAttroney("");
        setTdtPeriodTo_PowerAttroney("");
        setTxtArea_PowerAttroney("");
        setTxtPhone_PowerAttroney("");
        setTxtPin_PowerAttroney("");
        setTxtPoaHolderName("");
        setTxtRemark_PowerAttroney("");
        setCboState_PowerAttroney("");
        setTxtStreet_PowerAttroney("");
        setTxtCustID_PoA("");
    }
    
    public void setTermLoanPowerAttorneyTO(ArrayList objPoATOList, String borrowNo){
        try{
            setBorrowerNo(borrowNo);
            PowerAttorneyTO obj;
            ArrayList removedValues = new ArrayList();
            poaAllTabRecords = new ArrayList();
            ArrayList tabRecord;
            HashMap record;
            poaAllRecords.clear();
            
            // To retrieve Power of Attorney records one by one from the Database
            for (int i = objPoATOList.size() - 1,j = 0;i >= 0;--i,++j){
                obj = (PowerAttorneyTO) objPoATOList.get(j);
                
                tabRecord = new ArrayList();
                
                tabRecord.add(CommonUtil.convertObjToStr(obj.getPoaNo()));
                tabRecord.add(CommonUtil.convertObjToStr(obj.getPoaHolderName()));
                tabRecord.add(DateUtil.getStringDate(obj.getPeriodFrom()));
                tabRecord.add(DateUtil.getStringDate(obj.getPeriodTo()));
                
                poaAllTabRecords.add(tabRecord);
                
                tabRecord = null;
                record = new HashMap();
                record.put(POANO, CommonUtil.convertObjToStr(obj.getPoaNo()));
                record.put(HOLDERNAME, obj.getPoaHolderName());
                record.put(ONBEHALFOF, obj.getToWhom());
                record.put(CUSTOMER_ID, obj.getCustId());
                record.put(AREA, obj.getArea());
                record.put(STREET, obj.getStreet());
                record.put(CITY, obj.getCity());
                record.put(STATE, obj.getState());
                record.put(COUNTRY, obj.getCountryCode());
                record.put(PIN, obj.getPincode());
                record.put(PHONE, obj.getPhone());
                record.put(ADDR_TYPE, obj.getAddrType());
                record.put(FROM, DateUtil.getStringDate(obj.getPeriodFrom()));
                record.put(TO, DateUtil.getStringDate(obj.getPeriodTo()));
                record.put(REMARK, obj.getRemarks());
                record.put(COMMAND, UPDATE);
                
                poaAllRecords.put(CommonUtil.convertObjToStr(obj.getPoaNo()),record);
                
                record = null;
            }
            this.tblPoATab.setDataArrayList(poaAllTabRecords,poaTabTitle);
            tableUtilPoA.setTableValues(poaAllTabRecords);
            tableUtilPoA.setAllValues(poaAllRecords);
            setMax_Del_PoA_No(borrowNo);
            record = null;
            tabRecord = null;
            ttNotifyObservers();
        }catch(Exception e){
            System.out.println("Error In setTermLoanPowerAttorneyTO..."+e);
            parseException.logException(e,true);
        }
    }
    
    private void setMax_Del_PoA_No(String borrowNo){
        try{
            HashMap transactionMap = new HashMap();
            HashMap retrieve = new HashMap();
            transactionMap.put("borrowNo", borrowNo);
            List resultList = ClientUtil.executeQuery(getStrMaxDelSlNoMapName(), transactionMap);
            if (resultList.size() > 0){
                retrieve = (HashMap) resultList.get(0);
                tableUtilPoA.setStr_MAX_DEL_SL_NO(CommonUtil.convertObjToStr(retrieve.get("MAX_POA_NO")));
            }
            retrieve = null;
            transactionMap = null;
            resultList = null;
        }catch(Exception e){
            System.out.println("Error In setMax_Del_PoA_No: "+e);
            parseException.logException(e,true);
        }
    }
    
    public HashMap setTermLoanPowerAttorney(){
        PowerAttorneyTO objTermLoanPowerAttorneyTO = new PowerAttorneyTO();
        HashMap poaTOList = new HashMap();
        HashMap poaEachRecord;
        try {
            java.util.Set keySet =  poaAllRecords.keySet();
            Object[] objKeySet = (Object[]) keySet.toArray();
            // To set the values for Power of Attorney Transfer Object
            if(poaAllRecords!=null)
            for (int i = poaAllRecords.size() - 1, j = 0;i >= 0;--i,++j){
                poaEachRecord = (HashMap) poaAllRecords.get(objKeySet[j]);
                objTermLoanPowerAttorneyTO = new PowerAttorneyTO();
                objTermLoanPowerAttorneyTO.setArea(CommonUtil.convertObjToStr( poaEachRecord.get(AREA)));
                objTermLoanPowerAttorneyTO.setBorrowNo(borrowerNo);
                objTermLoanPowerAttorneyTO.setCustId(CommonUtil.convertObjToStr(poaEachRecord.get(CUSTOMER_ID)));
                objTermLoanPowerAttorneyTO.setCity(CommonUtil.convertObjToStr( poaEachRecord.get(CITY)));
                objTermLoanPowerAttorneyTO.setCountryCode(CommonUtil.convertObjToStr( poaEachRecord.get(COUNTRY)));
                
                Date IsDt = DateUtil.getDateMMDDYYYY(CommonUtil.convertObjToStr( poaEachRecord.get(FROM)));
                if(IsDt != null){
                Date isDate = (Date)curDate.clone();
                isDate.setDate(IsDt.getDate());
                isDate.setMonth(IsDt.getMonth());
                isDate.setYear(IsDt.getYear());
                objTermLoanPowerAttorneyTO.setPeriodFrom(isDate);  
                }else{
                  objTermLoanPowerAttorneyTO.setPeriodFrom(DateUtil.getDateMMDDYYYY(CommonUtil.convertObjToStr( poaEachRecord.get(FROM))));  
                }
                
                Date ToDt = DateUtil.getDateMMDDYYYY(CommonUtil.convertObjToStr( poaEachRecord.get(TO)));
                if(ToDt != null){
                Date toDate = (Date)curDate.clone();
                toDate.setDate(ToDt.getDate());
                toDate.setMonth(ToDt.getMonth());
                toDate.setYear(ToDt.getYear());
                objTermLoanPowerAttorneyTO.setPeriodTo(toDate);  
                }else{
                  objTermLoanPowerAttorneyTO.setPeriodTo(DateUtil.getDateMMDDYYYY(CommonUtil.convertObjToStr( poaEachRecord.get(TO))));
                }
                
                
//                objTermLoanPowerAttorneyTO.setPeriodFrom((Date)poaEachRecord.get(FROM));
//                objTermLoanPowerAttorneyTO.setPeriodTo((Date)poaEachRecord.get(TO));
                objTermLoanPowerAttorneyTO.setPhone(CommonUtil.convertObjToStr( poaEachRecord.get(PHONE)));
                objTermLoanPowerAttorneyTO.setPincode(CommonUtil.convertObjToStr( poaEachRecord.get(PIN)));
                objTermLoanPowerAttorneyTO.setAddrType(CommonUtil.convertObjToStr(poaEachRecord.get(ADDR_TYPE)));
                objTermLoanPowerAttorneyTO.setPoaHolderName(CommonUtil.convertObjToStr( poaEachRecord.get(HOLDERNAME)));
                objTermLoanPowerAttorneyTO.setState(CommonUtil.convertObjToStr( poaEachRecord.get(STATE)));
                objTermLoanPowerAttorneyTO.setStreet(CommonUtil.convertObjToStr( poaEachRecord.get(STREET)));
                objTermLoanPowerAttorneyTO.setPoaNo(CommonUtil.convertObjToDouble( poaEachRecord.get(POANO)));
                objTermLoanPowerAttorneyTO.setRemarks(CommonUtil.convertObjToStr( poaEachRecord.get(REMARK)));
                objTermLoanPowerAttorneyTO.setToWhom(CommonUtil.convertObjToStr( poaEachRecord.get(ONBEHALFOF)));
                objTermLoanPowerAttorneyTO.setCommand(CommonUtil.convertObjToStr( poaEachRecord.get(COMMAND)));
                if (poaEachRecord.get(COMMAND).equals(INSERT)){
                    objTermLoanPowerAttorneyTO.setStatus(CommonConstants.STATUS_CREATED);
                }else if (poaEachRecord.get(COMMAND).equals(UPDATE)){
                    objTermLoanPowerAttorneyTO.setStatus(CommonConstants.STATUS_MODIFIED);
                }
                objTermLoanPowerAttorneyTO.setStatusBy(TrueTransactMain.USER_ID);
                objTermLoanPowerAttorneyTO.setStatusDt(curDate);
                poaTOList.put(objTermLoanPowerAttorneyTO.getPoaNo(), objTermLoanPowerAttorneyTO);
                poaEachRecord = null;
                objTermLoanPowerAttorneyTO = null;
            }
            // To set the values for Power of Attorney Transfer Object
            // where as the existing records in Database are deleted in client side
            // useful for updating the status
            for (int i = tableUtilPoA.getRemovedValues().size() - 1,j = 0;i >= 0;--i,++j){
                poaEachRecord = (HashMap) tableUtilPoA.getRemovedValues().get(j);
                objTermLoanPowerAttorneyTO = new PowerAttorneyTO();
                objTermLoanPowerAttorneyTO.setArea(CommonUtil.convertObjToStr( poaEachRecord.get(AREA)));
                objTermLoanPowerAttorneyTO.setBorrowNo(borrowerNo);
                objTermLoanPowerAttorneyTO.setCustId(CommonUtil.convertObjToStr(poaEachRecord.get(CUSTOMER_ID)));
                objTermLoanPowerAttorneyTO.setCity(CommonUtil.convertObjToStr( poaEachRecord.get(CITY)));
                objTermLoanPowerAttorneyTO.setCountryCode(CommonUtil.convertObjToStr( poaEachRecord.get(COUNTRY)));
//                objTermLoanPowerAttorneyTO.setPeriodFrom(DateUtil.getDateMMDDYYYY(CommonUtil.convertObjToStr( poaEachRecord.get(FROM))));
//                objTermLoanPowerAttorneyTO.setPeriodTo(DateUtil.getDateMMDDYYYY(CommonUtil.convertObjToStr( poaEachRecord.get(TO))));
                
                 objTermLoanPowerAttorneyTO.setPeriodFrom((Date)poaEachRecord.get(FROM));
                objTermLoanPowerAttorneyTO.setPeriodTo((Date)poaEachRecord.get(TO));
                objTermLoanPowerAttorneyTO.setPhone(CommonUtil.convertObjToStr( poaEachRecord.get(PHONE)));
                objTermLoanPowerAttorneyTO.setPincode(CommonUtil.convertObjToStr( poaEachRecord.get(PIN)));
                objTermLoanPowerAttorneyTO.setAddrType(CommonUtil.convertObjToStr(poaEachRecord.get(ADDR_TYPE)));
                objTermLoanPowerAttorneyTO.setPoaHolderName(CommonUtil.convertObjToStr( poaEachRecord.get(HOLDERNAME)));
                objTermLoanPowerAttorneyTO.setState(CommonUtil.convertObjToStr( poaEachRecord.get(STATE)));
                objTermLoanPowerAttorneyTO.setStreet(CommonUtil.convertObjToStr( poaEachRecord.get(STREET)));
                objTermLoanPowerAttorneyTO.setPoaNo(CommonUtil.convertObjToDouble( poaEachRecord.get(POANO)));
                objTermLoanPowerAttorneyTO.setRemarks(CommonUtil.convertObjToStr( poaEachRecord.get(REMARK)));
                objTermLoanPowerAttorneyTO.setToWhom(CommonUtil.convertObjToStr( poaEachRecord.get(ONBEHALFOF)));
                objTermLoanPowerAttorneyTO.setCommand(CommonUtil.convertObjToStr( poaEachRecord.get(COMMAND)));
                objTermLoanPowerAttorneyTO.setStatus(CommonConstants.STATUS_DELETED);
                objTermLoanPowerAttorneyTO.setStatusBy(TrueTransactMain.USER_ID);
                objTermLoanPowerAttorneyTO.setStatusDt(curDate);
                poaTOList.put(objTermLoanPowerAttorneyTO.getPoaNo(), objTermLoanPowerAttorneyTO);
                poaEachRecord = null;
                objTermLoanPowerAttorneyTO = null;
            }
        }catch(Exception e){
            System.out.println("Error In setTermLoanPowerAttorney..."+e);
            parseException.logException(e,true);
        }
        objTermLoanPowerAttorneyTO = null;
        return poaTOList;
    }
    
    public void changeStatusPoA(int resultType){
        try{
            if (resultType != 2){
                //If the Main Save Button pressed
                tableUtilPoA.getRemovedValues().clear();
            }
            java.util.Set keySet =  poaAllRecords.keySet();
            Object[] objKeySet = (Object[]) keySet.toArray();
            HashMap oneRecord;
            // To change the Insert command to Update after Save Buttone Pressed
            // Power of Attorney Details
            for (int i = keySet.size() - 1,j = 0;i >= 0;--i,++j){
                oneRecord = (HashMap) poaAllRecords.get(objKeySet[j]);
                if (oneRecord.get(COMMAND).equals(INSERT)){
                    // If the status is in Insert Mode then change the mode to Update
                    oneRecord.put(COMMAND, UPDATE);
                    poaAllRecords.put(objKeySet[j], oneRecord);
                }
                oneRecord = null;
            }
            
            tableUtilPoA.setAllValues(poaAllRecords);
            keySet = null;
            objKeySet = null;
        }catch(Exception e){
            System.out.println("Error In changeStatusPoA..."+e);
            parseException.logException(e,true);
        }
    }
    
    public void setBorrowerNo(String borrowerNo){
        this.borrowerNo = borrowerNo;
        setChanged();
    }
    
    public String getBorrowerNo(){
        return this.borrowerNo;
    }
    
    public void setTblPoA(EnhancedTableModel tblPoATab){
        this.tblPoATab = tblPoATab;
        setChanged();
    }
    
    public EnhancedTableModel getTblPoA(){
        return this.tblPoATab;
    }
    
    void setCboPoACust(String cboPoACust){
        this.cboPoACust = cboPoACust;
        setChanged();
    }
    String getCboPoACust(){
        return this.cboPoACust;
    }
    
    void setCbmPoACust(ComboBoxModel cbmPoACust){
        this.cbmPoACust = cbmPoACust;
        setChanged();
    }
    
    public ComboBoxModel getCbmPoACust(){
        return this.cbmPoACust;
    }
    
    void setCboAddrType_PoA(String cboAddrType_PoA){
        this.cboAddrType_PoA = cboAddrType_PoA;
        setChanged();
    }
    String getCboAddrType_PoA(){
        return this.cboAddrType_PoA;
    }
    
    void setCbmAddrType_PoA(ComboBoxModel cbmAddrType_PoA){
        this.cbmAddrType_PoA = cbmAddrType_PoA;
        setChanged();
    }
    
    ComboBoxModel getCbmAddrType_PoA(){
        return this.cbmAddrType_PoA;
    }
    
    void setTxtPoANo(String txtPoANo){
        this.txtPoANo = txtPoANo;
        setChanged();
    }
    
    String getTxtPoANo(){
        return this.txtPoANo;
    }
    
    void setTxtPoaHolderName(String txtPoaHolderName){
        this.txtPoaHolderName = txtPoaHolderName;
        setChanged();
    }
    String getTxtPoaHolderName(){
        return this.txtPoaHolderName;
    }
    
    void setTxtStreet_PowerAttroney(String txtStreet_PowerAttroney){
        this.txtStreet_PowerAttroney = txtStreet_PowerAttroney;
        setChanged();
    }
    String getTxtStreet_PowerAttroney(){
        return this.txtStreet_PowerAttroney;
    }
    
    void setTxtArea_PowerAttroney(String txtArea_PowerAttroney){
        this.txtArea_PowerAttroney = txtArea_PowerAttroney;
        setChanged();
    }
    String getTxtArea_PowerAttroney(){
        return this.txtArea_PowerAttroney;
    }
    
    void setCbmCity_PowerAttroney(ComboBoxModel cbmCity_PowerAttroney){
        this.cbmCity_PowerAttroney = cbmCity_PowerAttroney;
        setChanged();
    }
    
    ComboBoxModel getCbmCity_PowerAttroney(){
        return this.cbmCity_PowerAttroney;
    }
    
    void setCboCity_PowerAttroney(String cboCity_PowerAttroney){
        this.cboCity_PowerAttroney = cboCity_PowerAttroney;
        setChanged();
    }
    String getCboCity_PowerAttroney(){
        return this.cboCity_PowerAttroney;
    }
    
    void setCbmState_PowerAttroney(ComboBoxModel cbmState_PowerAttroney){
        this.cbmState_PowerAttroney = cbmState_PowerAttroney;
        setChanged();
    }
    
    ComboBoxModel getCbmState_PowerAttroney(){
        return this.cbmState_PowerAttroney;
    }
    
    void setCboState_PowerAttroney(String cboState_PowerAttroney){
        this.cboState_PowerAttroney = cboState_PowerAttroney;
        setChanged();
    }
    String getCboState_PowerAttroney(){
        return this.cboState_PowerAttroney;
    }
    
    void setCbmCountry_PowerAttroney(ComboBoxModel cbmCountry_PowerAttroney){
        this.cbmCountry_PowerAttroney = cbmCountry_PowerAttroney;
        setChanged();
    }
    
    ComboBoxModel getCbmCountry_PowerAttroney(){
        return this.cbmCountry_PowerAttroney;
    }
    
    void setCboCountry_PowerAttroney(String cboCountry_PowerAttroney){
        this.cboCountry_PowerAttroney = cboCountry_PowerAttroney;
        setChanged();
    }
    String getCboCountry_PowerAttroney(){
        return this.cboCountry_PowerAttroney;
    }
    
    void setTxtPin_PowerAttroney(String txtPin_PowerAttroney){
        this.txtPin_PowerAttroney = txtPin_PowerAttroney;
        setChanged();
    }
    String getTxtPin_PowerAttroney(){
        return this.txtPin_PowerAttroney;
    }
    
    void setTxtPhone_PowerAttroney(String txtPhone_PowerAttroney){
        this.txtPhone_PowerAttroney = txtPhone_PowerAttroney;
        setChanged();
    }
    String getTxtPhone_PowerAttroney(){
        return this.txtPhone_PowerAttroney;
    }
    
    void setTdtPeriodFrom_PowerAttroney(String tdtPeriodFrom_PowerAttroney){
        this.tdtPeriodFrom_PowerAttroney = tdtPeriodFrom_PowerAttroney;
        setChanged();
    }
    String getTdtPeriodFrom_PowerAttroney(){
        return this.tdtPeriodFrom_PowerAttroney;
    }
    
    void setTdtPeriodTo_PowerAttroney(String tdtPeriodTo_PowerAttroney){
        this.tdtPeriodTo_PowerAttroney = tdtPeriodTo_PowerAttroney;
        setChanged();
    }
    String getTdtPeriodTo_PowerAttroney(){
        return this.tdtPeriodTo_PowerAttroney;
    }
    
    void setTxtRemark_PowerAttroney(String txtRemark_PowerAttroney){
        this.txtRemark_PowerAttroney = txtRemark_PowerAttroney;
        setChanged();
    }
    String getTxtRemark_PowerAttroney(){
        return this.txtRemark_PowerAttroney;
    }
    
    public void ttNotifyObservers(){
        this.notifyObservers();
    }
    
    public void clearCboPoACust_ID(){
        // Remove all keys and values before add
        try{
            for (int i = cbmPoACust.getSize() - 1;i >= 0;--i){
                cbmPoACust.removeKeyAndElement(cbmPoACust.getKey(i));
            }
            cbmPoACust.addKeyAndElement("", "");
            ttNotifyObservers();
        }catch(Exception e){
            log.info("Exception Caught in clearCboPoACust_ID: "+e);
            parseException.logException(e,true);
        }
    }
    
    
    public void resetPoACustID(String Cust_ID){
        try{
            // Remove all keys and values before add
            for (int i = cbmPoACust.getSize() - 1;i >= 0;--i){
                cbmPoACust.removeKeyAndElement(cbmPoACust.getKey(i));
            }
            // To add the Customer ID in ComboBoxModel in PoA Details
            if (!cbmPoACust.containsElement("")){
                cbmPoACust.addKeyAndElement("", "");
            }
            if (!cbmPoACust.containsElement(getCustName(Cust_ID))){
                cbmPoACust.addKeyAndElement(Cust_ID, getCustName(Cust_ID));
            }
            ttNotifyObservers();
        }catch(Exception e){
            System.out.println("Exception Caught in resetPoACustID: "+e);
            parseException.logException(e,true);
        }
    }
    
    public String getCustName(String custID){
        //        getSelectAccInfoTblDisplay
        String returnValue = "";
        try{
            HashMap idTransactionMap = new HashMap();
            HashMap idRetrieve;
            idTransactionMap.put("CUST_ID", custID);
            List idResultList = ClientUtil.executeQuery("getSelectAccInfoTblDisplay", idTransactionMap);
            if (idResultList.size() > 0){
                // If Product Account Head exist in Database
                idRetrieve = (HashMap) idResultList.get(0);
                returnValue = CommonUtil.convertObjToStr(idRetrieve.get("NAME"));
                returnValue = returnValue+"("+custID+")";
            }
            idRetrieve = null;
            idTransactionMap = null;
            idResultList = null;
        }catch(Exception e){
            System.out.println("Exception caught in getCustName: "+e);
            parseException.logException(e,true);
        }
        return returnValue;
    }
    
    //  Power of Attorney Details.
    public int addPoATab(int row, boolean update){ // update is the flag to update the records
        String temp = new String();
        int option = -1;
        try{
            poaEachTabRecords = new ArrayList();
            poaEachRecords = new HashMap();
            HashMap result = new HashMap();
            ArrayList data = tblPoATab.getDataArrayList();
            tblPoATab.setDataArrayList(data,poaTabTitle);
            final int dataSize = data.size();
            boolean exist = false;
            boolean found = false;
            insertPoA(dataSize+1);
            if (!update) {
                // If the table is not in Edit Mode
                result = tableUtilPoA.insertTableValues(poaEachTabRecords,poaEachRecords);
                
                poaAllTabRecords = (ArrayList) result.get(TABLE_VALUES);
                poaAllRecords = (LinkedHashMap) result.get(ALL_VALUES);
                option = CommonUtil.convertObjToInt(result.get(OPTION));
                
                tblPoATab.setDataArrayList(poaAllTabRecords, poaTabTitle);
            }else{
                option = updatePoATable(row);
            }
            
            setChanged();
            result = null;
            data = null;
            poaEachTabRecords = null;
            poaEachRecords = null;
        }catch(Exception e){
            System.out.println("The error in addPoATab()"+e);
            parseException.logException(e,true);
        }
        return option;
    }
    
    // Details to be inserted or updated in PoA table
    private void insertPoA(int slno){
        
        poaEachTabRecords.add(String.valueOf(slno));
        poaEachTabRecords.add(txtPoaHolderName);
        poaEachTabRecords.add(tdtPeriodFrom_PowerAttroney);
        poaEachTabRecords.add(tdtPeriodTo_PowerAttroney);
        
        poaEachRecords.put(POANO,String.valueOf(slno));
        poaEachRecords.put(CUSTOMER_ID, getTxtCustID_PoA());
        poaEachRecords.put(ONBEHALFOF, CommonUtil.convertObjToStr(cbmPoACust.getKeyForSelected()));
        poaEachRecords.put(HOLDERNAME,txtPoaHolderName);
        poaEachRecords.put(AREA,txtArea_PowerAttroney);
        poaEachRecords.put(STREET,txtStreet_PowerAttroney);
        poaEachRecords.put(ADDR_TYPE, CommonUtil.convertObjToStr(cbmAddrType_PoA.getKeyForSelected()));
        poaEachRecords.put(CITY,CommonUtil.convertObjToStr(cbmCity_PowerAttroney.getKeyForSelected()));
        poaEachRecords.put(STATE,CommonUtil.convertObjToStr(cbmState_PowerAttroney.getKeyForSelected()));
        poaEachRecords.put(COUNTRY,CommonUtil.convertObjToStr(cbmCountry_PowerAttroney.getKeyForSelected()));
        poaEachRecords.put(PIN,txtPin_PowerAttroney);
        poaEachRecords.put(PHONE,txtPhone_PowerAttroney);
        poaEachRecords.put(FROM,tdtPeriodFrom_PowerAttroney);
        poaEachRecords.put(TO,tdtPeriodTo_PowerAttroney);
        poaEachRecords.put(REMARK,txtRemark_PowerAttroney);
        poaEachRecords.put(COMMAND, "");
    }
    
    //  Record to display in UI
    public void populatePoATable(int row){
        try{
            HashMap eachRecs = new HashMap();
            java.util.Set keySet =  poaAllRecords.keySet();
            Object[] objKeySet = (Object[]) keySet.toArray();
            ArrayList poaTableValue = (ArrayList)tblPoATab.getDataArrayList().get(row);
            // To populate the corresponding PoA record in UI
            for (int i = poaAllRecords.size() - 1,j = 0;i >= 0;--i,++j){
                if (((HashMap) poaAllRecords.get(objKeySet[j])).get(POANO).equals(poaTableValue.get(0))){
                    // To populate the Corresponding record from CTable
                    eachRecs = (HashMap) poaAllRecords.get(objKeySet[j]);
                    setTxtPoANo(CommonUtil.convertObjToStr(eachRecs.get(POANO)));
                    setCboPoACust(CommonUtil.convertObjToStr(getCbmPoACust().getDataForKey(eachRecs.get(ONBEHALFOF))));
                    setTxtCustID_PoA(CommonUtil.convertObjToStr(eachRecs.get(CUSTOMER_ID)));
                    setCboCity_PowerAttroney(CommonUtil.convertObjToStr(getCbmCity_PowerAttroney().getDataForKey(eachRecs.get(CITY))));
                    setCboCountry_PowerAttroney(CommonUtil.convertObjToStr(getCbmCountry_PowerAttroney().getDataForKey(eachRecs.get(COUNTRY))));
                    setTdtPeriodFrom_PowerAttroney(CommonUtil.convertObjToStr(eachRecs.get(FROM)));
                    setTdtPeriodTo_PowerAttroney(CommonUtil.convertObjToStr(eachRecs.get(TO)));
                    setTxtArea_PowerAttroney(CommonUtil.convertObjToStr(eachRecs.get(AREA)));
                    setTxtPhone_PowerAttroney(CommonUtil.convertObjToStr(eachRecs.get(PHONE)));
                    setTxtPin_PowerAttroney(CommonUtil.convertObjToStr(eachRecs.get(PIN)));
                    setTxtPoaHolderName(CommonUtil.convertObjToStr(eachRecs.get(HOLDERNAME)));
                    setTxtRemark_PowerAttroney(CommonUtil.convertObjToStr(eachRecs.get(REMARK)));
                    setCboAddrType_PoA(CommonUtil.convertObjToStr(getCbmAddrType_PoA().getDataForKey(eachRecs.get(ADDR_TYPE))));
                    setCboState_PowerAttroney(CommonUtil.convertObjToStr(getCbmState_PowerAttroney().getDataForKey(eachRecs.get(STATE))));
                    setTxtStreet_PowerAttroney(CommonUtil.convertObjToStr(eachRecs.get(STREET)));
                    setChanged();
                    ttNotifyObservers();
                    break;
                }
            }
            eachRecs = null;
            objKeySet = null;
            poaTableValue = null;
            keySet = null;
        }catch(Exception e){
            System.out.println("Error in populatePoATable..."+e);
            parseException.logException(e,true);
        }
    }
    
    // Update Power of Attorney Table where row is the Serial Number
    private int updatePoATable(int row){
        HashMap result = new HashMap();
        int option = -1;
        int count = 0;
        try{
            
            result = tableUtilPoA.updateTableValues(poaEachTabRecords, poaEachRecords, row);
            
            poaAllTabRecords = (ArrayList) result.get(TABLE_VALUES);
            poaAllRecords = (LinkedHashMap) result.get(ALL_VALUES);
            option = CommonUtil.convertObjToInt(result.get(OPTION));
            
            tblPoATab.setDataArrayList(poaAllTabRecords, poaTabTitle);
        }catch(Exception e){
            System.out.println("Error in updatePoA..."+e);
            parseException.logException(e,true);
        }
        result = null;
        return option;
    }
    
    // Delete Power of Attorney Table where r is the Serial Number
    public void deletePoATable(int row){
        try{
            ArrayList tempList1 = new ArrayList();
            HashMap result = new HashMap();
            
            result = tableUtilPoA.deleteTableValues(row);
            
            poaAllTabRecords = (ArrayList)result.get(TABLE_VALUES);
            poaAllRecords = (LinkedHashMap)result.get(ALL_VALUES);
            
            tblPoATab.setDataArrayList(poaAllTabRecords, poaTabTitle);
            result = null;
        }catch(Exception e){
            System.out.println("Exception caught in deletePoATable: "+e);
            parseException.logException(e,true);
        }
    }
    
    public boolean checkCustIDExistInJointAcctAndPoA(String CustID){
        java.util.Set keySet =  poaAllRecords.keySet();
        Object[] objKeySet = (Object[]) keySet.toArray();
        try{
            HashMap eachPoARec;
            for (int i = poaAllRecords.size() - 1,j = 0;i >= 0;--i,++j){
                eachPoARec = (HashMap) poaAllRecords.get(objKeySet[j]);
                if (CustID.equals(eachPoARec.get(ONBEHALFOF))){
                    int option = -1;
                    String[] options = {objPowerOfAttorneyRB.getString("cDialogOk")};
                    option = COptionPane.showOptionDialog(null, objPowerOfAttorneyRB.getString("existanceCustomerWarningPoA"), CommonConstants.WARNINGTITLE,
                    COptionPane.OK_OPTION, COptionPane.WARNING_MESSAGE, null, options, options[0]);
                    return false;
                }
                eachPoARec = null;
            }
            keySet = null;
            objKeySet = null;
        }catch(Exception e){
            System.out.println("Exception caught in checkCustIDExistInJointAcctAndPoA: "+e);
            parseException.logException(e,true);
        }
        return true;
    }
    
    public void setPoACustName(String CUSTID){
        try {
            HashMap transactionMap = new HashMap();
            HashMap retrieve = new HashMap();
            transactionMap.put("custId",CUSTID);
            List resultList1 = (List) ClientUtil.executeQuery("getSelectCustomerOpenDate", transactionMap);
            if (resultList1.size() > 0){
                // If atleast one Record should exist
                retrieve = (HashMap) resultList1.get(0);
                if (retrieve.get("CUST_TYPE").equals("CORPORATE")){// If it is the Corporate Customer
                    setTxtPoaHolderName(CommonUtil.convertObjToStr(retrieve.get("COMP_NAME")));
                }else{
                    setTxtPoaHolderName(CommonUtil.convertObjToStr(retrieve.get("CUSTOMER NAME")));
                }
            }
            retrieve = null;
            transactionMap = null;
            resultList1 = null;
        }catch(Exception e){
            log.info("Exception caught in setPoACustName: "+e);
            parseException.logException(e,true);
        }
    }
    
    public void setPoACustAddr(String CUSTID){
        try {
            HashMap transactionMap = new HashMap();
            HashMap retrieve = new HashMap();
            transactionMap.put("custId",CUSTID);
            List resultList2 = ClientUtil.executeQuery("getSelectCustomerAddress", transactionMap);
            if (resultList2.size() > 0){
                // Atleast one Record should exist
                retrieve = (HashMap) resultList2.get(0);
                
                setTxtArea_PowerAttroney(CommonUtil.convertObjToStr(retrieve.get(AREA)));
                setTxtStreet_PowerAttroney(CommonUtil.convertObjToStr(retrieve.get(STREET)));
                setCboCity_PowerAttroney(CommonUtil.convertObjToStr(retrieve.get(CITY)));
                setCboState_PowerAttroney(CommonUtil.convertObjToStr(retrieve.get(STATE)));
                setCboCountry_PowerAttroney(CommonUtil.convertObjToStr(retrieve.get(COUNTRY)));
                setTxtPin_PowerAttroney(CommonUtil.convertObjToStr(retrieve.get(PIN_CODE)));
                setCboAddrType_PoA(CommonUtil.convertObjToStr(getCbmAddrType_PoA().getDataForKey(CommonUtil.convertObjToStr(retrieve.get(ADDR_TYPE)))));
            }
            retrieve = null;
            transactionMap = null;
            resultList2 = null;
        }catch(Exception e){
            log.info("Exception caught in setPoACustAddr: "+e);
            parseException.logException(e,true);
        }
    }
    
    public void setPoACustPhone(String CUSTID){
        try {
            HashMap transactionMap = new HashMap();
            HashMap retrieve;
            StringBuffer stbPhoneNo = new StringBuffer();
            transactionMap.put("custId",CUSTID);
            
            List resultList2         = ClientUtil.executeQuery("getSelectCustomerPhone", transactionMap);
            
            if (resultList2.size() > 0){
                retrieve = (HashMap) resultList2.get(0);
                stbPhoneNo.append(CommonUtil.convertObjToStr(retrieve.get("AREA_CODE")));
                stbPhoneNo.append(CommonUtil.convertObjToStr(retrieve.get("PHONE_NUMBER")));
                setTxtPhone_PowerAttroney(CommonUtil.convertObjToStr(stbPhoneNo));
            }
            else{
                 setTxtPhone_PowerAttroney("");
            }
            transactionMap = null;
            resultList2 = null;
            retrieve = null;
            stbPhoneNo = null;
        }catch(Exception e){
            log.info("Exception caught in setPoACustPhone: "+e);
            parseException.logException(e,true);
        }
    }
    
    public boolean isThisCustomerOnBehalfofOther(String strCustID, String strOnBehalfOf){
        boolean isThisCustomerOnBehalfofOther = true;
        try{
            if (strCustID.equals(strOnBehalfOf)){
                showWarningMsg(objPowerOfAttorneyRB.getString("onBehalfOfSameCustWarn"));
                isThisCustomerOnBehalfofOther = false;
            }
            if (isThisCustomerOnBehalfofOther){
                HashMap eachRecs = new HashMap();
                String strExistingCustID;
                String strExistingOnBehalfOf;
                java.util.Set keySet =  poaAllRecords.keySet();
                Object[] objKeySet = (Object[]) keySet.toArray();
                // To find this combination already exits or not
                for (int i = poaAllRecords.size() - 1,j = 0;i >= 0;--i,++j){
                        eachRecs = (HashMap) poaAllRecords.get(objKeySet[j]);
                        strExistingCustID = CommonUtil.convertObjToStr(eachRecs.get(CUSTOMER_ID));
                        strExistingOnBehalfOf = CommonUtil.convertObjToStr(eachRecs.get(ONBEHALFOF));
                        if (strExistingCustID.equals(strCustID) && strExistingOnBehalfOf.equals(strOnBehalfOf)){
                            showWarningMsg(strOnBehalfOf + objPowerOfAttorneyRB.getString("onBehalfOfCombinationExistWarn") + strCustID);
                            isThisCustomerOnBehalfofOther = false;
                            break;
                        }
                        strExistingCustID = null;
                        strExistingOnBehalfOf = null;
                        eachRecs = null;
                }
                eachRecs = null;
                keySet = null;
                objKeySet = null;
                strExistingCustID = null;
                strExistingOnBehalfOf = null;
            }
        }catch(Exception e){
            log.info("Exception caught in isThisCustomerOnBehalfofOther: "+e);
            parseException.logException(e,true);
        }
        return isThisCustomerOnBehalfofOther;
    }
    
    public int showWarningMsg(String strWarnMsg){
        int option = -1;
        String[] options = {objPowerOfAttorneyRB.getString("cDialogOk")};
        option = COptionPane.showOptionDialog(null, strWarnMsg, CommonConstants.WARNINGTITLE,
        COptionPane.OK_OPTION, COptionPane.WARNING_MESSAGE, null, options, options[0]);
        return option;
    }
    
    // To create objects
    public void createObject(){
        poaAllRecords = new LinkedHashMap();
        poaAllTabRecords = new ArrayList();
        tblPoATab = new EnhancedTableModel(null, poaTabTitle);
        tableUtilPoA = new TableUtil();
        tableUtilPoA.setAttributeKey(POANO);
    }
    
    // To destroy Objects
    public void destroyObjects(){
        poaAllRecords = null;
        poaAllTabRecords = null;
        tblPoATab = null;
        poaEachRecords= null;
        poaEachTabRecords= null;
        tableUtilPoA = null;
    }
    
    /**
     * Getter for property txtCustID_PoA.
     * @return Value of property txtCustID_PoA.
     */
    public java.lang.String getTxtCustID_PoA() {
        return txtCustID_PoA;
    }
    
    /**
     * Setter for property txtCustID_PoA.
     * @param txtCustID_PoA New value of property txtCustID_PoA.
     */
    public void setTxtCustID_PoA(java.lang.String txtCustID_PoA) {
        this.txtCustID_PoA = txtCustID_PoA;
    }
    
}
