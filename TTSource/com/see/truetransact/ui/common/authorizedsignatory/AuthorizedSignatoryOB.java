/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition. 
 * 
 * AuthorizedSignatoryOB.java
 *
 * Created on December 23, 2004, 10:57 AM
 */

package com.see.truetransact.ui.common.authorizedsignatory;

/**
 *
 * @author  152713
 */

import com.see.truetransact.clientproxy.ProxyParameters;
import com.see.truetransact.clientutil.ClientUtil;
import com.see.truetransact.clientutil.ComboBoxModel;
import com.see.truetransact.clientutil.EnhancedTableModel;
import com.see.truetransact.clientutil.TableUtil;
import com.see.truetransact.clientexception.ClientParseException;
import com.see.truetransact.commonutil.CommonConstants;
import com.see.truetransact.commonutil.DateUtil;
import com.see.truetransact.commonutil.CommonUtil;
import com.see.truetransact.commonutil.TTException;
import com.see.truetransact.transferobject.common.authorizedsignatory.AuthorizedSignatoryTO;
import com.see.truetransact.transferobject.termloan.TermLoanAuthorizedSignatoryTO;
import com.see.truetransact.ui.TrueTransactMain;
import com.see.truetransact.uicomponent.COptionPane;
import com.see.truetransact.uicomponent.CObservable;

import org.apache.log4j.Logger;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Date;
public class AuthorizedSignatoryOB extends CObservable{
    
    /** Creates a new instance of TermLoanAuthorizedSignatory */
    public AuthorizedSignatoryOB(String strModule){
        authorizedSignatoryOB(strModule);
    }
    
    private AuthorizedSignatoryOB authorizedSignatoryOB;
    
    private final static Logger log = Logger.getLogger(AuthorizedSignatoryOB.class);
    private final static ClientParseException parseException = ClientParseException.getInstance();
    
    //    private final   AuthorizedSignatoryRB authSignRB = new AuthorizedSignatoryRB();
    private final   java.util.ResourceBundle authSignRB = java.util.ResourceBundle.getBundle("com.see.truetransact.ui.common.authorizedsignatory.AuthorizedSignatoryRB", ProxyParameters.LANGUAGE);
    
    public          String  strMaxDelSlNoMapName = "";
    public          int no_AuthorizedSignatory;
    private final   String  AREA_CODE = "AREA_CODE";
    private final   String  ADDR_COMM = "ADDR_COMM";
    private final   String  ADDR_TYPE = "ADDR_TYPE";
    private final   String  ALL_VALUES = "ALL_VALUES";
    private final   String  AREA = "AREA";
    private final   String  AUTHORIZE_NO = "AUTHORIZE_NO";
    private final   String  BUSINESS = "BUSINESS";
    private final   String  BUSINESS_FAX = "BUSINESS_FAX";
    private final   String  BUSINESS_PHONE = "BUSINESS_PHONE";
    private final   String  CITY = "CITY";
    private final   String  COMMAND = "COMMAND";
    private final   String  COUNTRY = "COUNTRY";
    private final   String  COUNTRY_CODE = "COUNTRY_CODE";
    private final   String  CUSTID = "CUSTID";
    private final   String  DESIGNATION = "DESIGNATION";
    private final   String  EMAILID = "EMAILID";
    private final   String  FAX = "FAX";
    private final   String  HOME = "HOME";
    private final   String  HOME_FAX = "HOME_FAX";
    private final   String  HOME_PHONE = "HOME_PHONE";
    private final   String  INSERT = "INSERT";
    private final   String  LIMITS = "LIMITS";
    private final   String  MOBILE = "MOBILE";
    private final   String  NAME = "NAME";
    private final   String  ACCT_NUM="ACCT_NUM";
    private final   String  OPTION = "OPTION";
    private final   String  PAGER = "PAGER";
    private final   String  PHONE_NUMBER = "PHONE_NUMBER";
    private final   String  PIN = "PIN";
    private final   String  PIN_CODE = "PIN_CODE";
    private final   String  SLNO = "SLNO";
    private final   String  STATE = "STATE";
    private final   String  STREET = "STREET";
    private final   String  TABLE_VALUES = "TABLE_VALUES";
    private final   String  UPDATE = "UPDATE";
    
    private final   ArrayList authorizedTabTitle = new ArrayList();     //  Table Title of Authorized Signatory
    private         ArrayList authorizedTabRow;                         //  Single Record of Authorized Table
    private         ArrayList authorizeTableList;                       //  ArrayList to display in Authorized Table
    
    private ArrayList key;
    private ArrayList value;
    
    private HashMap lookUpHash;
    private HashMap keyValue;
    
    private HashMap authorizedRec;                                      //  Single Record equivalent to the Authorised Table
    private LinkedHashMap authorizedAll = new LinkedHashMap();          //  List which contains all the values of the Authorized Table
    
    private EnhancedTableModel tblAuthorizedTab;
    
    private ComboBoxModel cbmAddrCommunication_AuthorizedSignatory;
    private ComboBoxModel cbmCity_AuthorizedSignatory;
    private ComboBoxModel cbmState_AuthorizedSignatory;
    private ComboBoxModel cbmCountry_AuthorizedSignatory;
    
    private TableUtil tableUtilAuthorize = new TableUtil();
    
    private String borrowerNo = "";
    private String acctNum="";
    private String txtNumberAuthSignatory = "0";
    private String txtCustomerID = "";
    private String txtLimits = "";
    private String txtName_AuthorizedSignatory = "";
    private String txtDesig_AuthorizedSignatory = "";
    private String cboAddrCommunication_AuthorizedSignatory = "";
    private String txtStreet_AuthorizedSignatory = "";
    private String txtArea_AuthorizedSignatory = "";
    private String cboCity_AuthorizedSignatory = "";
    private String cboState_AuthorizedSignatory = "";
    private String cboCountry_AuthorizedSignatory = "";
    private String txtPin_AuthorizedSignatory = "";
    private String txtHomePhone_AuthorizedSignatory = "";
    private String txtBusinessPhone_AuthorizedSignatory = "";
    private String txtHomeFax_AuthorizedSignatory = "";
    private String txtBusinessFax_AuthorizedSignatory = "";
    private String txtPager_AuthorizedSignatory = "";
    private String txtMobile_AuthorizedSignatory = "";
    private String txtEmailId_AuthorizedSignatory = "";
    private Date currDt = null;
    private void authorizedSignatoryOB(String strModule){
        try{
            currDt = ClientUtil.getCurrentDate();
            fillDropdown();
            setAutorizedTabTitle();
            tableUtilAuthorize.setAttributeKey(SLNO);
            tblAuthorizedTab = new EnhancedTableModel(null, authorizedTabTitle);
            no_AuthorizedSignatory = getTblAuthorized().getRowCount();
            setStrMaxDelSlNoMapName(strModule);
            ttNotifyObservers();
        }catch(Exception e){
            log.info("Exception in authorizedSignatoryOB()..."+e);
            parseException.logException(e,true);
        }
    }
    
    /**
     * To populate the appropriate keys and values of all the combo
     * boxes in the screen at the time of AuthorizedSignatoryOB instance creation
     * @throws Exception will throw it to the AuthorizedSignatoryOB constructor
     */
    public void fillDropdown() throws Exception{
        lookUpHash = new HashMap();
        lookUpHash.put(CommonConstants.MAP_NAME,null);
        
        ArrayList lookup_keys = new ArrayList();
        lookup_keys.add("CORPORATE.ADDRESS_TYPE");
        lookup_keys.add("CUSTOMER.ADDRTYPE");
        lookup_keys.add("CUSTOMER.CITY");
        lookup_keys.add("CUSTOMER.STATE");
        lookup_keys.add("CUSTOMER.COUNTRY");
        lookUpHash.put(CommonConstants.PARAMFORQUERY, lookup_keys);
        
        keyValue = ClientUtil.populateLookupData(lookUpHash);
        
        getKeyValue((HashMap)keyValue.get("CUSTOMER.ADDRTYPE"));
        ArrayList addrTypeKeyList   = key;
        ArrayList addrTypeValueList = value;
        getKeyValue((HashMap)keyValue.get("CORPORATE.ADDRESS_TYPE"));
        // To add the two types of Address Type(Individual Customer & Corporate Customer
        for (int i = key.size() - 1, j = 0;i >= 0;--i,++j){
            if ((key.get(j).equals("")) || (key.get(j).equals("OTHERS"))){
                // To avoid the Duplication of OTHERS and <Blank Space>
                continue;
            }
            addrTypeKeyList.add(key.get(j));
            addrTypeValueList.add(value.get(j));
        }
        setCbmAddrCommunication_AuthorizedSignatory(new ComboBoxModel(addrTypeKeyList,addrTypeValueList));
        
        getKeyValue((HashMap)keyValue.get("CUSTOMER.CITY"));
        setCbmCity_AuthorizedSignatory(new ComboBoxModel(key,value));
        
        getKeyValue((HashMap)keyValue.get("CUSTOMER.STATE"));
        setCbmState_AuthorizedSignatory(new ComboBoxModel(key,value));
        
        getKeyValue((HashMap)keyValue.get("CUSTOMER.COUNTRY"));
        setCbmCountry_AuthorizedSignatory(new ComboBoxModel(key,value));
        
        addrTypeKeyList = null;
        addrTypeValueList = null;
        lookup_keys = null;
    }
    
    private void getKeyValue(HashMap keyValue)  throws Exception{
        key = (ArrayList)keyValue.get(CommonConstants.KEY);
        value = (ArrayList)keyValue.get(CommonConstants.VALUE);
    }
    
    private void setAutorizedTabTitle() throws Exception{
        try {
            authorizedTabTitle.add(authSignRB.getString("tblColumnAuth1"));
            authorizedTabTitle.add(authSignRB.getString("tblColumnAuth2"));
            authorizedTabTitle.add(authSignRB.getString("tblColumnAuth3"));
            authorizedTabTitle.add(authSignRB.getString("tblColumnAuth4"));
        }catch(Exception e) {
            log.info("Exception in AuthorizedTabTitle()..."+e);
            parseException.logException(e,true);
        }
        
    }
    
    public void setStrMaxDelSlNoMapName(String module){
        this.strMaxDelSlNoMapName = "getSelectAuthorizedSignatoryMaxSLNO" + module;
        setChanged();
    }
    
    private String getStrMaxDelSlNoMapName(){
        return this.strMaxDelSlNoMapName;
    }
    
    private void resetAuthorizeTable(){
        tblAuthorizedTab = new EnhancedTableModel(null, authorizedTabTitle);
        ttNotifyObservers();
    }
    
    private void resetNoAuthSign(){
        setTxtNumberAuthSignatory("0");
        no_AuthorizedSignatory = 0;
        ttNotifyObservers();
    }
    
    public void resetAllFieldsInAuthTab(){
        resetAuthorizedForm();
        resetAuthorizeTable();
        resetNoAuthSign();
        destroyObjects();
        createObject();
        ttNotifyObservers();
    }
    
    public void resetAuthorizedForm(){
        //authorized_signatory
        setCboAddrCommunication_AuthorizedSignatory("");
        setCboCity_AuthorizedSignatory("");
        setCboCountry_AuthorizedSignatory("");
        setTxtArea_AuthorizedSignatory("");
        setTxtStreet_AuthorizedSignatory("");
        setCboState_AuthorizedSignatory("");
        setTxtName_AuthorizedSignatory("");
        setTxtPin_AuthorizedSignatory("");
        setTxtBusinessFax_AuthorizedSignatory("");
        setTxtBusinessPhone_AuthorizedSignatory("");
        setTxtCustomerID("");
        setTxtLimits("");
        setTxtDesig_AuthorizedSignatory("");
        setTxtEmailId_AuthorizedSignatory("");
        setTxtHomeFax_AuthorizedSignatory("");
        setTxtHomePhone_AuthorizedSignatory("");
        setTxtMobile_AuthorizedSignatory("");
        setTxtPager_AuthorizedSignatory("");
        ttNotifyObservers();
    }
    
    // Populate Authorized Signatory Tab
    public void setAuthorizedSignatoryTO(ArrayList authList, String borrowNo){
        log.info("In setAuthorizedSignatoryTO...");
        try{
            setBorrowerNo(borrowNo);
//            setAcctNum(acct_num);
            ArrayList arrayListRecord;
            ArrayList removedValues = new ArrayList();
            HashMap hashMapRecord;
            LinkedHashMap authorizeAllMap = new LinkedHashMap();
            authorizeTableList = new ArrayList();
            AuthorizedSignatoryTO obj = null;
            // To retrieve Authorized signatory records one by one from the Database
            if(authList !=null)
            for (int i = authList.size() - 1,k = 0;i >= 0;--i,++k){
                arrayListRecord = new ArrayList();
                obj = (AuthorizedSignatoryTO) authList.get(k);
                
                arrayListRecord.add(CommonUtil.convertObjToStr(obj.getSlNo()));
                if (!(obj.getCustId() == null)){ // To put the value if it is not null
                    arrayListRecord.add(CommonUtil.convertObjToStr(obj.getCustId()));
                }else{
                    arrayListRecord.add("");
                }
                arrayListRecord.add(CommonUtil.convertObjToStr(obj.getCustName()));
                arrayListRecord.add(CommonUtil.convertObjToStr(obj.getLimits()));
                
                authorizeTableList.add(arrayListRecord);  //  List to be displayed in the Table
                arrayListRecord = null;
                hashMapRecord = new HashMap();
                
                hashMapRecord.put(SLNO, CommonUtil.convertObjToStr(obj.getSlNo()));
                hashMapRecord.put(CUSTID,obj.getCustId());
                hashMapRecord.put(NAME,obj.getCustName());
//                hashMapRecord.put(ACCT_NUM,obj.getACustName());
                hashMapRecord.put(LIMITS,CommonUtil.convertObjToStr(obj.getLimits()));
                hashMapRecord.put(AUTHORIZE_NO,obj.getAuthrizeNo());
                hashMapRecord.put(DESIGNATION,obj.getDesignation());
                hashMapRecord.put(ADDR_COMM,obj.getCommAddr());
                hashMapRecord.put(STREET,obj.getStreet());
                hashMapRecord.put(AREA,obj.getArea());
                hashMapRecord.put(CITY,obj.getCity());
                hashMapRecord.put(STATE,obj.getState());
                hashMapRecord.put(COUNTRY,obj.getCountryCode());
                hashMapRecord.put(PIN,obj.getPincode());
                hashMapRecord.put(HOME_PHONE,obj.getHomePhone());
                hashMapRecord.put(BUSINESS_PHONE,obj.getBusinessPhone());
                hashMapRecord.put(HOME_FAX,obj.getHomeFax());
                hashMapRecord.put(BUSINESS_FAX,obj.getBusinessFax());
                hashMapRecord.put(PAGER,obj.getPager());
                hashMapRecord.put(MOBILE,obj.getMobile());
                hashMapRecord.put(EMAILID,obj.getEmailId());
                hashMapRecord.put(COMMAND, UPDATE);
                
                authorizeAllMap.put(CommonUtil.convertObjToStr(obj.getSlNo()), hashMapRecord);  //List of values corresponding to the table
                
                obj = null;
                hashMapRecord = null;
            }
            no_AuthorizedSignatory = authorizeAllMap.size();
            setTxtNumberAuthSignatory(CommonUtil.convertObjToStr(String.valueOf(no_AuthorizedSignatory)));  // To display no of Authorized Signatory
            tblAuthorizedTab.setDataArrayList(authorizeTableList, authorizedTabTitle);
            
            authorizedAll.clear();
            authorizedAll = authorizeAllMap;
            setMax_Del_AuthSign_No(borrowNo);
            arrayListRecord = null;
            hashMapRecord = null;
            authorizeAllMap = null;
            removedValues = null;
            tableUtilAuthorize.setTableValues(authorizeTableList);
            tableUtilAuthorize.setAllValues(authorizedAll);
            ttNotifyObservers();
        }catch(Exception e){
            log.info("Error In setAuthorizedSignatoryTO..."+e);
            parseException.logException(e,true);
        }
    }
    
    
     // Populate Authorized Signatory Tab
    public void setTermLoanAuthorizedSignatoryTO(ArrayList authList, String borrowNo,String acct_num){
        log.info("In setAuthorizedSignatoryTO...");
        try{
            setBorrowerNo(borrowNo);
            setAcctNum(acct_num);
            ArrayList arrayListRecord;
            ArrayList removedValues = new ArrayList();
            HashMap hashMapRecord;
            LinkedHashMap authorizeAllMap = new LinkedHashMap();
            authorizeTableList = new ArrayList();
            TermLoanAuthorizedSignatoryTO obj = null;
            // To retrieve Authorized signatory records one by one from the Database
            for (int i = authList.size() - 1,k = 0;i >= 0;--i,++k){
                arrayListRecord = new ArrayList();
                obj = (TermLoanAuthorizedSignatoryTO) authList.get(k);
                
                arrayListRecord.add(CommonUtil.convertObjToStr(obj.getSlNo()));
                if (!(obj.getCustId() == null)){ // To put the value if it is not null
                    arrayListRecord.add(CommonUtil.convertObjToStr(obj.getCustId()));
                }else{
                    arrayListRecord.add("");
                }
                arrayListRecord.add(CommonUtil.convertObjToStr(obj.getCustName()));
                arrayListRecord.add(CommonUtil.convertObjToStr(obj.getLimits()));
                
                authorizeTableList.add(arrayListRecord);  //  List to be displayed in the Table
                arrayListRecord = null;
                hashMapRecord = new HashMap();
                
                hashMapRecord.put(SLNO, CommonUtil.convertObjToStr(obj.getSlNo()));
                hashMapRecord.put(CUSTID,obj.getCustId());
                hashMapRecord.put(NAME,obj.getCustName());
                hashMapRecord.put(ACCT_NUM,obj.getAcctNum());
                hashMapRecord.put(LIMITS,CommonUtil.convertObjToStr(obj.getLimits()));
                hashMapRecord.put(AUTHORIZE_NO,obj.getAuthrizeNo());
                hashMapRecord.put(DESIGNATION,obj.getDesignation());
                hashMapRecord.put(ADDR_COMM,obj.getCommAddr());
                hashMapRecord.put(STREET,obj.getStreet());
                hashMapRecord.put(AREA,obj.getArea());
                hashMapRecord.put(CITY,obj.getCity());
                hashMapRecord.put(STATE,obj.getState());
                hashMapRecord.put(COUNTRY,obj.getCountryCode());
                hashMapRecord.put(PIN,obj.getPincode());
                hashMapRecord.put(HOME_PHONE,obj.getHomePhone());
                hashMapRecord.put(BUSINESS_PHONE,obj.getBusinessPhone());
                hashMapRecord.put(HOME_FAX,obj.getHomeFax());
                hashMapRecord.put(BUSINESS_FAX,obj.getBusinessFax());
                hashMapRecord.put(PAGER,obj.getPager());
                hashMapRecord.put(MOBILE,obj.getMobile());
                hashMapRecord.put(EMAILID,obj.getEmailId());
                hashMapRecord.put(COMMAND, UPDATE);
                
                authorizeAllMap.put(CommonUtil.convertObjToStr(obj.getSlNo()), hashMapRecord);  //List of values corresponding to the table
                
                obj = null;
                hashMapRecord = null;
            }
            no_AuthorizedSignatory = authorizeAllMap.size();
            setTxtNumberAuthSignatory(CommonUtil.convertObjToStr(String.valueOf(no_AuthorizedSignatory)));  // To display no of Authorized Signatory
            tblAuthorizedTab.setDataArrayList(authorizeTableList, authorizedTabTitle);
            
            authorizedAll.clear();
            authorizedAll = authorizeAllMap;
            setMax_Del_AuthSign_No(borrowNo);
            arrayListRecord = null;
            hashMapRecord = null;
            authorizeAllMap = null;
            removedValues = null;
            tableUtilAuthorize.setTableValues(authorizeTableList);
            tableUtilAuthorize.setAllValues(authorizedAll);
            ttNotifyObservers();
        }catch(Exception e){
            log.info("Error In setAuthorizedSignatoryTO..."+e);
            parseException.logException(e,true);
        }
    }
    
    private void setMax_Del_AuthSign_No(String borrowNo){
        try{
            HashMap transactionMap = new HashMap();
            HashMap retrieve = new HashMap();
            transactionMap.put("borrowNo", borrowNo);
            List resultList = ClientUtil.executeQuery(getStrMaxDelSlNoMapName(), transactionMap);
            if (resultList.size() > 0){
                retrieve = (HashMap) resultList.get(0);
                tableUtilAuthorize.setStr_MAX_DEL_SL_NO(CommonUtil.convertObjToStr(retrieve.get("MAX_AUTHORIZE_NO")));
            }
            retrieve = null;
            transactionMap = null;
            resultList = null;
        }catch(Exception e){
            log.info("Error In setMax_Del_AuthSign_No: "+e);
            parseException.logException(e,true);
        }
    }
    public HashMap setAuthorizedSignatory(){
        AuthorizedSignatoryTO objTermLoanAuthorizedSignatoryTO;
        HashMap authorizedTOList = new HashMap();
        HashMap temp = new HashMap();
        try{
            java.util.Set keySet =  authorizedAll.keySet();
            Object[] objKeySet = (Object[]) keySet.toArray();
            // To set the values for Authorized Signatory Transfer Object
            for (int i = authorizedAll.size() - 1, j = 0;i >= 0;--i,++j){
                temp = (HashMap) authorizedAll.get(objKeySet[j]);
                objTermLoanAuthorizedSignatoryTO = new AuthorizedSignatoryTO();
                objTermLoanAuthorizedSignatoryTO.setSlNo(CommonUtil.convertObjToStr(temp.get(SLNO)));
                objTermLoanAuthorizedSignatoryTO.setCustId(CommonUtil.convertObjToStr(temp.get(CUSTID)));
                objTermLoanAuthorizedSignatoryTO.setAuthrizeNo(CommonUtil.convertObjToDouble(String.valueOf(authorizedAll.size())));
                objTermLoanAuthorizedSignatoryTO.setCustName(CommonUtil.convertObjToStr(temp.get(NAME)));
                objTermLoanAuthorizedSignatoryTO.setCommAddr(CommonUtil.convertObjToStr(temp.get(ADDR_COMM)));
                objTermLoanAuthorizedSignatoryTO.setStreet(CommonUtil.convertObjToStr(temp.get(STREET)));
                objTermLoanAuthorizedSignatoryTO.setArea(CommonUtil.convertObjToStr(temp.get(AREA)));
                objTermLoanAuthorizedSignatoryTO.setCity(CommonUtil.convertObjToStr(temp.get(CITY)));
                objTermLoanAuthorizedSignatoryTO.setState(CommonUtil.convertObjToStr(temp.get(STATE)));
                objTermLoanAuthorizedSignatoryTO.setCountryCode(CommonUtil.convertObjToStr(temp.get(COUNTRY)));
                objTermLoanAuthorizedSignatoryTO.setPincode(CommonUtil.convertObjToStr(temp.get(PIN)));
                objTermLoanAuthorizedSignatoryTO.setLimits(CommonUtil.convertObjToDouble(temp.get(LIMITS)));
                objTermLoanAuthorizedSignatoryTO.setDesignation(CommonUtil.convertObjToStr(temp.get(DESIGNATION)));
                objTermLoanAuthorizedSignatoryTO.setHomePhone(CommonUtil.convertObjToStr(temp.get(HOME_PHONE)));
                objTermLoanAuthorizedSignatoryTO.setHomeFax(CommonUtil.convertObjToStr(temp.get(HOME_FAX)));
                objTermLoanAuthorizedSignatoryTO.setPager(CommonUtil.convertObjToStr(temp.get(PAGER)));
                objTermLoanAuthorizedSignatoryTO.setEmailId(CommonUtil.convertObjToStr(temp.get(EMAILID)));
                objTermLoanAuthorizedSignatoryTO.setBusinessPhone(CommonUtil.convertObjToStr(temp.get(BUSINESS_PHONE)));
                objTermLoanAuthorizedSignatoryTO.setBusinessFax(CommonUtil.convertObjToStr(temp.get(BUSINESS_FAX)));
                objTermLoanAuthorizedSignatoryTO.setMobile(CommonUtil.convertObjToStr(temp.get(MOBILE)));
                objTermLoanAuthorizedSignatoryTO.setBorrowNo(borrowerNo);
                objTermLoanAuthorizedSignatoryTO.setCommand(CommonUtil.convertObjToStr(temp.get(COMMAND)));
                if (temp.get(COMMAND).equals(INSERT)){
                    objTermLoanAuthorizedSignatoryTO.setStatus(CommonConstants.STATUS_CREATED);
                }else if (temp.get(COMMAND).equals(UPDATE)){
                    objTermLoanAuthorizedSignatoryTO.setStatus(CommonConstants.STATUS_MODIFIED);
                }
                objTermLoanAuthorizedSignatoryTO.setStatusBy(TrueTransactMain.USER_ID);
                objTermLoanAuthorizedSignatoryTO.setStatusDt(currDt);
                authorizedTOList.put(temp.get(SLNO), objTermLoanAuthorizedSignatoryTO);
                temp = null;
                objTermLoanAuthorizedSignatoryTO = null;
            }
            // To set the values for Authorized Signatory Transfer Object
            // where as the existing records in Database are deleted in client side
            // useful for updating the status
            for (int i = tableUtilAuthorize.getRemovedValues().size() - 1,j = 0;i >= 0;--i,++j){
                temp = (HashMap) tableUtilAuthorize.getRemovedValues().get(j);
                objTermLoanAuthorizedSignatoryTO = new AuthorizedSignatoryTO();
                objTermLoanAuthorizedSignatoryTO.setSlNo(CommonUtil.convertObjToStr(temp.get(SLNO)));
                objTermLoanAuthorizedSignatoryTO.setCustId(CommonUtil.convertObjToStr( temp.get(CUSTID)));
                objTermLoanAuthorizedSignatoryTO.setAuthrizeNo(CommonUtil.convertObjToDouble( temp.get(AUTHORIZE_NO)));
                objTermLoanAuthorizedSignatoryTO.setCustName(CommonUtil.convertObjToStr( temp.get(NAME)));
                objTermLoanAuthorizedSignatoryTO.setCommAddr(CommonUtil.convertObjToStr( temp.get(ADDR_COMM)));
                objTermLoanAuthorizedSignatoryTO.setStreet(CommonUtil.convertObjToStr( temp.get(STREET)));
                objTermLoanAuthorizedSignatoryTO.setArea(CommonUtil.convertObjToStr( temp.get(AREA)));
                objTermLoanAuthorizedSignatoryTO.setCity(CommonUtil.convertObjToStr( temp.get(CITY)));
                objTermLoanAuthorizedSignatoryTO.setState(CommonUtil.convertObjToStr( temp.get(STATE)));
                objTermLoanAuthorizedSignatoryTO.setCountryCode(CommonUtil.convertObjToStr( temp.get(COUNTRY)));
                objTermLoanAuthorizedSignatoryTO.setPincode(CommonUtil.convertObjToStr( temp.get(PIN)));
                objTermLoanAuthorizedSignatoryTO.setLimits(CommonUtil.convertObjToDouble(temp.get(LIMITS)));
                objTermLoanAuthorizedSignatoryTO.setDesignation(CommonUtil.convertObjToStr( temp.get(DESIGNATION)));
                objTermLoanAuthorizedSignatoryTO.setHomePhone(CommonUtil.convertObjToStr( temp.get(HOME_PHONE)));
                objTermLoanAuthorizedSignatoryTO.setHomeFax(CommonUtil.convertObjToStr( temp.get(HOME_FAX)));
                objTermLoanAuthorizedSignatoryTO.setPager(CommonUtil.convertObjToStr( temp.get(PAGER)));
                objTermLoanAuthorizedSignatoryTO.setEmailId(CommonUtil.convertObjToStr( temp.get(EMAILID)));
                objTermLoanAuthorizedSignatoryTO.setBusinessPhone(CommonUtil.convertObjToStr( temp.get(BUSINESS_PHONE)));
                objTermLoanAuthorizedSignatoryTO.setBusinessFax(CommonUtil.convertObjToStr( temp.get(BUSINESS_FAX)));
                objTermLoanAuthorizedSignatoryTO.setMobile(CommonUtil.convertObjToStr( temp.get(MOBILE)));
                objTermLoanAuthorizedSignatoryTO.setBorrowNo(borrowerNo);
                objTermLoanAuthorizedSignatoryTO.setCommand(CommonUtil.convertObjToStr( temp.get(COMMAND)));
                objTermLoanAuthorizedSignatoryTO.setStatus(CommonConstants.STATUS_DELETED);
                objTermLoanAuthorizedSignatoryTO.setStatusBy(TrueTransactMain.USER_ID);
                objTermLoanAuthorizedSignatoryTO.setStatusDt(currDt);
                authorizedTOList.put(temp.get(SLNO), objTermLoanAuthorizedSignatoryTO);
                temp = null;
                objTermLoanAuthorizedSignatoryTO = null;
            }
            
        }catch(Exception e){
            log.info("Error In setAuthorizedSignatory..."+e);
            parseException.logException(e,true);
        }
        return authorizedTOList;
    }
    
    public void changeStatusAuthorizedSignatory(int resultType){
        try{
            if (resultType != 2){
                //If the Main Save Button pressed
                tableUtilAuthorize.getRemovedValues().clear();
            }
            java.util.Set keySet =  authorizedAll.keySet();
            Object[] objKeySet = (Object[]) keySet.toArray();
            HashMap oneRecord;
            // To change the Insert command to Update after Save Buttone Pressed
            // For Authorized Details
            for (int i = keySet.size() - 1,j = 0;i >= 0;--i,++j){
                oneRecord = (HashMap) authorizedAll.get(objKeySet[j]);
                if (oneRecord.get(COMMAND).equals(INSERT)){
                    // If the status is in Insert Mode then change the mode to Update
                    oneRecord.put(COMMAND, UPDATE);
                    authorizedAll.put(objKeySet[j], oneRecord);
                }
                oneRecord = null;
            }
            
            tableUtilAuthorize.setAllValues(authorizedAll);
            keySet = null;
            objKeySet = null;
            ttNotifyObservers();
        }catch(Exception e){
            log.info("Error In changeStatusAuthorizedSignatory..."+e);
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
    
    void setTxtNumberAuthSignatory(String txtNumberAuthSignatory){
        this.txtNumberAuthSignatory = txtNumberAuthSignatory;
        setChanged();
    }
    String getTxtNumberAuthSignatory(){
        return this.txtNumberAuthSignatory;
    }
    
    void setTxtCustomerID(String txtCustomerID){
        this.txtCustomerID = txtCustomerID;
        setChanged();
    }
    public String getTxtCustomerID(){
        return this.txtCustomerID;
    }
    
    void setTxtLimits(String txtLimits){
        this.txtLimits = txtLimits;
        setChanged();
    }
    String getTxtLimits(){
        return this.txtLimits;
    }
    
    void setTxtName_AuthorizedSignatory(String txtName_AuthorizedSignatory){
        this.txtName_AuthorizedSignatory = txtName_AuthorizedSignatory;
        setChanged();
    }
    String getTxtName_AuthorizedSignatory(){
        return this.txtName_AuthorizedSignatory;
    }
    
    void setTxtDesig_AuthorizedSignatory(String txtDesig_AuthorizedSignatory){
        this.txtDesig_AuthorizedSignatory = txtDesig_AuthorizedSignatory;
        setChanged();
    }
    String getTxtDesig_AuthorizedSignatory(){
        return this.txtDesig_AuthorizedSignatory;
    }
    
    void setCbmAddrCommunication_AuthorizedSignatory(ComboBoxModel cbmAddrCommunication_AuthorizedSignatory){
        this.cbmAddrCommunication_AuthorizedSignatory = cbmAddrCommunication_AuthorizedSignatory;
        setChanged();
    }
    
    ComboBoxModel getCbmAddrCommunication_AuthorizedSignatory(){
        return this.cbmAddrCommunication_AuthorizedSignatory;
    }
    
    void setCboAddrCommunication_AuthorizedSignatory(String cboAddrCommunication_AuthorizedSignatory){
        this.cboAddrCommunication_AuthorizedSignatory = cboAddrCommunication_AuthorizedSignatory;
        setChanged();
    }
    
    String getCboAddrCommunication_AuthorizedSignatory(){
        return this.cboAddrCommunication_AuthorizedSignatory;
    }
    
    void setTxtStreet_AuthorizedSignatory(String txtStreet_AuthorizedSignatory){
        this.txtStreet_AuthorizedSignatory = txtStreet_AuthorizedSignatory;
        setChanged();
    }
    String getTxtStreet_AuthorizedSignatory(){
        return this.txtStreet_AuthorizedSignatory;
    }
    
    void setTxtArea_AuthorizedSignatory(String txtArea_AuthorizedSignatory){
        this.txtArea_AuthorizedSignatory = txtArea_AuthorizedSignatory;
        setChanged();
    }
    String getTxtArea_AuthorizedSignatory(){
        return this.txtArea_AuthorizedSignatory;
    }
    
    void setCbmCity_AuthorizedSignatory(ComboBoxModel cbmCity_AuthorizedSignatory){
        this.cbmCity_AuthorizedSignatory = cbmCity_AuthorizedSignatory;
        setChanged();
    }
    
    public ComboBoxModel getCbmCity_AuthorizedSignatory(){
        return this.cbmCity_AuthorizedSignatory;
    }
    
    void setCboCity_AuthorizedSignatory(String cboCity_AuthorizedSignatory){
        this.cboCity_AuthorizedSignatory = cboCity_AuthorizedSignatory;
        setChanged();
    }
    String getCboCity_AuthorizedSignatory(){
        return this.cboCity_AuthorizedSignatory;
    }
    
    
    void setCbmState_AuthorizedSignatory(ComboBoxModel cbmState_AuthorizedSignatory){
        this.cbmState_AuthorizedSignatory = cbmState_AuthorizedSignatory;
        setChanged();
    }
    
    public ComboBoxModel getCbmState_AuthorizedSignatory(){
        return this.cbmState_AuthorizedSignatory;
    }
    
    void setCboState_AuthorizedSignatory(String cboState_AuthorizedSignatory){
        this.cboState_AuthorizedSignatory = cboState_AuthorizedSignatory;
        setChanged();
    }
    String getCboState_AuthorizedSignatory(){
        return this.cboState_AuthorizedSignatory;
    }
    
    void setCbmCountry_AuthorizedSignatory(ComboBoxModel cbmCountry_AuthorizedSignatory){
        this.cbmCountry_AuthorizedSignatory = cbmCountry_AuthorizedSignatory;
        setChanged();
    }
    
    ComboBoxModel getCbmCountry_AuthorizedSignatory(){
        return this.cbmCountry_AuthorizedSignatory;
    }
    
    void setCboCountry_AuthorizedSignatory(String cboCountry_AuthorizedSignatory){
        this.cboCountry_AuthorizedSignatory = cboCountry_AuthorizedSignatory;
        setChanged();
    }
    String getCboCountry_AuthorizedSignatory(){
        return this.cboCountry_AuthorizedSignatory;
    }
    
    void setTxtPin_AuthorizedSignatory(String txtPin_AuthorizedSignatory){
        this.txtPin_AuthorizedSignatory = txtPin_AuthorizedSignatory;
        setChanged();
    }
    String getTxtPin_AuthorizedSignatory(){
        return this.txtPin_AuthorizedSignatory;
    }
    
    void setTxtHomePhone_AuthorizedSignatory(String txtHomePhone_AuthorizedSignatory){
        this.txtHomePhone_AuthorizedSignatory = txtHomePhone_AuthorizedSignatory;
        setChanged();
    }
    String getTxtHomePhone_AuthorizedSignatory(){
        return this.txtHomePhone_AuthorizedSignatory;
    }
    
    void setTxtBusinessPhone_AuthorizedSignatory(String txtBusinessPhone_AuthorizedSignatory){
        this.txtBusinessPhone_AuthorizedSignatory = txtBusinessPhone_AuthorizedSignatory;
        setChanged();
    }
    String getTxtBusinessPhone_AuthorizedSignatory(){
        return this.txtBusinessPhone_AuthorizedSignatory;
    }
    
    void setTxtHomeFax_AuthorizedSignatory(String txtHomeFax_AuthorizedSignatory){
        this.txtHomeFax_AuthorizedSignatory = txtHomeFax_AuthorizedSignatory;
        setChanged();
    }
    String getTxtHomeFax_AuthorizedSignatory(){
        return this.txtHomeFax_AuthorizedSignatory;
    }
    
    void setTxtBusinessFax_AuthorizedSignatory(String txtBusinessFax_AuthorizedSignatory){
        this.txtBusinessFax_AuthorizedSignatory = txtBusinessFax_AuthorizedSignatory;
        setChanged();
    }
    String getTxtBusinessFax_AuthorizedSignatory(){
        return this.txtBusinessFax_AuthorizedSignatory;
    }
    
    void setTxtPager_AuthorizedSignatory(String txtPager_AuthorizedSignatory){
        this.txtPager_AuthorizedSignatory = txtPager_AuthorizedSignatory;
        setChanged();
    }
    String getTxtPager_AuthorizedSignatory(){
        return this.txtPager_AuthorizedSignatory;
    }
    
    void setTxtMobile_AuthorizedSignatory(String txtMobile_AuthorizedSignatory){
        this.txtMobile_AuthorizedSignatory = txtMobile_AuthorizedSignatory;
        setChanged();
    }
    String getTxtMobile_AuthorizedSignatory(){
        return this.txtMobile_AuthorizedSignatory;
    }
    
    void setTxtEmailId_AuthorizedSignatory(String txtEmailId_AuthorizedSignatory){
        this.txtEmailId_AuthorizedSignatory = txtEmailId_AuthorizedSignatory;
        setChanged();
    }
    String getTxtEmailId_AuthorizedSignatory(){
        return this.txtEmailId_AuthorizedSignatory;
    }
    
    public void setTblAuthorized(EnhancedTableModel tblAuthorizedTab){
        log.info("In setTblAuthorized...");
        
        this.tblAuthorizedTab = tblAuthorizedTab;
        setChanged();
    }
    
   public  EnhancedTableModel getTblAuthorized(){
        return this.tblAuthorizedTab;
    }
    
    public void ttNotifyObservers(){
        setChanged();
        notifyObservers();
    }
    
    
    public void setAuthCustName(String CUSTID){
        try {
            HashMap transactionMap = new HashMap();
            HashMap retrieve = new HashMap();
            transactionMap.put("custId",CUSTID);
            List resultList1 = (List) ClientUtil.executeQuery("getSelectCustomerOpenDate", transactionMap);
            if (resultList1.size() > 0){
                // If atleast one Record should exist
                retrieve = (HashMap) resultList1.get(0);
                if (retrieve.get("CUST_TYPE").equals("CORPORATE")){// If it is the Corporate Customer
                    setTxtName_AuthorizedSignatory(CommonUtil.convertObjToStr(retrieve.get("COMP_NAME")));
                }else{
                    setTxtName_AuthorizedSignatory(CommonUtil.convertObjToStr(retrieve.get("CUSTOMER NAME")));
                }
                setTxtEmailId_AuthorizedSignatory(CommonUtil.convertObjToStr(retrieve.get("EMAIL_ID")));
                setTxtDesig_AuthorizedSignatory(CommonUtil.convertObjToStr(retrieve.get("DESIGNATION")));
            }
            retrieve = null;
            transactionMap = null;
            resultList1 = null;
        }catch(Exception e){
            log.info("Exception caught in setAuthCustName..."+e);
            parseException.logException(e,true);
        }
    }
    
    public void setAuthCustAddr(String CUSTID){
        try {
            HashMap transactionMap = new HashMap();
            HashMap retrieve = new HashMap();
            transactionMap.put("custId",CUSTID);
            List resultList2 = ClientUtil.executeQuery("getSelectCustomerAddress", transactionMap);
            if (resultList2.size() > 0){
                // Atleast one Record should exist
                retrieve = (HashMap) resultList2.get(0);
                
                setTxtArea_AuthorizedSignatory(CommonUtil.convertObjToStr(retrieve.get(AREA)));
                setTxtStreet_AuthorizedSignatory(CommonUtil.convertObjToStr(retrieve.get(STREET)));
                setCboCity_AuthorizedSignatory(CommonUtil.convertObjToStr(retrieve.get(CITY)));
                setCboState_AuthorizedSignatory(CommonUtil.convertObjToStr(retrieve.get(STATE)));
                setCboCountry_AuthorizedSignatory(CommonUtil.convertObjToStr(retrieve.get(COUNTRY)));
                setTxtPin_AuthorizedSignatory(CommonUtil.convertObjToStr(retrieve.get(PIN_CODE)));
                setCboAddrCommunication_AuthorizedSignatory(CommonUtil.convertObjToStr(getCbmAddrCommunication_AuthorizedSignatory().getDataForKey(CommonUtil.convertObjToStr(retrieve.get(ADDR_TYPE)))));
            }
            retrieve = null;
            transactionMap = null;
            resultList2 = null;
        }catch(Exception e){
            log.info("Exception caught in setAuthCustAddr..."+e);
            parseException.logException(e,true);
        }
    }
    
    public void setAuthCustPhone(String CUSTID){
        try {
            HashMap transactionMap = new HashMap();
            HashMap retrieve;
            transactionMap.put("custId",CUSTID);
            int businessPhoneCount = 0;
            int businessFaxCount   = 0;
            int homePhoneCount     = 0;
            int homeFaxCount       = 0;
            int mobileCount        = 0;
            int pagerCount         = 0;
            StringBuffer stbHomePhone      = new StringBuffer("");
            StringBuffer stbHomeFax        = new StringBuffer("");
            StringBuffer stbBusinessPhone  = new StringBuffer("");
            StringBuffer stbBusinessFax    = new StringBuffer("");
            StringBuffer stbMobile         = new StringBuffer("");
            StringBuffer stbPager          = new StringBuffer("");
            List resultList2         = ClientUtil.executeQuery("getSelectCustomerPhone", transactionMap);
            // To retrieve the Contact phone numbers of the Customer
            for (int i = resultList2.size() - 1,j = 0;i >= 0;--i,++j){
                retrieve = new HashMap();
                retrieve = (HashMap) resultList2.get(j);
                String phoneType = new String();
                String addrType = new String();
                
                phoneType = CommonUtil.convertObjToStr(retrieve.get("PHONE_TYPE_ID"));
                addrType = CommonUtil.convertObjToStr(retrieve.get(ADDR_TYPE));
                if (addrType.equals(HOME) && phoneType.equals("LAND LINE")){
                    // If Address type is Home and Phone type is Land Line
                    if (homePhoneCount != 0){
                        // To append comma except at the end
                        stbHomePhone.append(", ");
                    }
                    stbHomePhone.append(CommonUtil.convertObjToStr(retrieve.get(AREA_CODE)));
                    stbHomePhone.append(CommonUtil.convertObjToStr(retrieve.get(PHONE_NUMBER)));
                    homePhoneCount++;
                }
                if (addrType.equals(HOME) && phoneType.equals(FAX)){
                    // If Address type is home and Phone type is Fax
                    if (homeFaxCount != 0){
                        // To append comma except at the end
                        stbHomeFax.append(", ");
                    }
                    stbHomeFax.append(CommonUtil.convertObjToStr(retrieve.get(AREA_CODE)));
                    stbHomeFax.append(CommonUtil.convertObjToStr(retrieve.get(PHONE_NUMBER)));
                    homeFaxCount++;
                }
                if (addrType.equals(BUSINESS) && phoneType.equals("LAND LINE")){
                    // If Address type is business and Phone type is Land Line
                    if (businessPhoneCount != 0){
                        // To append comma except at the end
                        stbBusinessPhone.append(", ");
                    }
                    stbBusinessPhone.append(CommonUtil.convertObjToStr(retrieve.get(AREA_CODE)));
                    stbBusinessPhone.append(CommonUtil.convertObjToStr(retrieve.get(PHONE_NUMBER)));
                    businessPhoneCount++;
                }
                if (addrType.equals(BUSINESS) && phoneType.equals(FAX)){
                    // If Address type is business and Phone type is Fax
                    if (businessFaxCount != 0){
                        // To append comma except at the end
                        stbBusinessFax.append(", ");
                    }
                    stbBusinessFax.append(CommonUtil.convertObjToStr(retrieve.get(AREA_CODE)));
                    stbBusinessFax.append(CommonUtil.convertObjToStr(retrieve.get(PHONE_NUMBER)));
                    businessFaxCount++;
                }
                if (phoneType.equals(MOBILE)){
                    // If Phone type is Mobile
                    if (mobileCount != 0){
                        // To append comma except at the end
                        stbMobile.append(", ");
                    }
                    stbMobile.append(CommonUtil.convertObjToStr(retrieve.get(PHONE_NUMBER)));
                    mobileCount++;
                }
                if (phoneType.equals(PAGER)){
                    // If Phone type is Pager
                    if (pagerCount != 0){
                        // To append comma except at the end
                        stbPager.append(", ");
                    }
                    stbPager.append(CommonUtil.convertObjToStr(retrieve.get(PHONE_NUMBER)));
                    pagerCount++;
                }
                retrieve = null;
            }
            
            setTxtHomePhone_AuthorizedSignatory(stbHomePhone.toString());
            setTxtHomeFax_AuthorizedSignatory(stbHomeFax.toString());
            setTxtBusinessPhone_AuthorizedSignatory(stbBusinessPhone.toString());
            setTxtBusinessFax_AuthorizedSignatory(stbBusinessFax.toString());
            setTxtMobile_AuthorizedSignatory(stbMobile.toString());
            setTxtPager_AuthorizedSignatory(stbPager.toString());
            transactionMap = null;
            resultList2 = null;
        }catch(Exception e){
            log.info("Exception caught in setAuthCustPhone()..."+e);
            parseException.logException(e,true);
        }
    }
    
    
    // ADD, UPDATE Records in Authorized Table
    public int addAuthorizedTab(int row, boolean update){ // mode is used as flag as well as serial number
        log.info("In addAuthorizedTab...");
        //        setTxtNumberAuthSignatory(String.valueOf(no_AuthorizedSignatory));
        int option = -1;
        try{
            authorizedTabRow = new ArrayList();
            authorizedRec = new HashMap();
            HashMap result = new HashMap();
            ArrayList data = tblAuthorizedTab.getDataArrayList();
            tblAuthorizedTab.setDataArrayList(data,authorizedTabTitle);
            final int dataSize = data.size();
            boolean exist = false;
            boolean found = false;
            
            insertAuthorizedRecord(dataSize+1);
            if (!update){
                // If the table is not in Edit Mode
                result = tableUtilAuthorize.insertTableValues(authorizedTabRow, authorizedRec);
                authorizeTableList = (ArrayList) result.get(TABLE_VALUES);
                authorizedAll = (LinkedHashMap) result.get(ALL_VALUES);
                option = CommonUtil.convertObjToInt(result.get(OPTION));
                tblAuthorizedTab.setDataArrayList(authorizeTableList,authorizedTabTitle);
            }else{
                option = updateAuthorizedTab(row);
            }
            
            authorizedRec = null;
            authorizedTabRow = null;
            data = null;
            result = null;
            ttNotifyObservers();
        }catch(Exception e){
            log.info("in addAuthorizedTab..."+e);
            parseException.logException(e,true);
        }
        return option;
    }
    
    // Details to be inserted or updated in Authorized Signatory Table
    private void insertAuthorizedRecord(int slno){
        //TO INSERT RECORD FOR AUTHORIZED SIGNATORY TABLE
        try{
            authorizedTabRow.add(String.valueOf(slno));
            authorizedTabRow.add(txtCustomerID);
            authorizedTabRow.add(txtName_AuthorizedSignatory);
            authorizedTabRow.add(CommonUtil.convertObjToStr(txtLimits));
            
            authorizedRec.put(CUSTID,txtCustomerID);
            authorizedRec.put(NAME,txtName_AuthorizedSignatory);
            authorizedRec.put(LIMITS,CommonUtil.convertObjToStr(txtLimits));
            authorizedRec.put(SLNO,String.valueOf(slno));
            authorizedRec.put(DESIGNATION,txtDesig_AuthorizedSignatory);
            authorizedRec.put(ADDR_COMM,CommonUtil.convertObjToStr(cbmAddrCommunication_AuthorizedSignatory.getKeyForSelected()));
            authorizedRec.put(STREET,txtStreet_AuthorizedSignatory);
            authorizedRec.put(AREA,txtArea_AuthorizedSignatory);
            authorizedRec.put(CITY,CommonUtil.convertObjToStr(cbmCity_AuthorizedSignatory.getKeyForSelected()));
            authorizedRec.put(STATE,CommonUtil.convertObjToStr(cbmState_AuthorizedSignatory.getKeyForSelected()));
            authorizedRec.put(COUNTRY,CommonUtil.convertObjToStr(cbmCountry_AuthorizedSignatory.getKeyForSelected()));
            authorizedRec.put(PIN,txtPin_AuthorizedSignatory);
            authorizedRec.put(HOME_PHONE,txtHomePhone_AuthorizedSignatory);
            authorizedRec.put(BUSINESS_PHONE,txtBusinessPhone_AuthorizedSignatory);
            authorizedRec.put(HOME_FAX,txtHomeFax_AuthorizedSignatory);
            authorizedRec.put(BUSINESS_FAX,txtBusinessFax_AuthorizedSignatory);
            authorizedRec.put(PAGER,txtPager_AuthorizedSignatory);
            authorizedRec.put(MOBILE,txtMobile_AuthorizedSignatory);
            authorizedRec.put(EMAILID,txtEmailId_AuthorizedSignatory);
            authorizedRec.put(AUTHORIZE_NO,"0");
            authorizedRec.put(COMMAND,"");
        }catch(Exception e){
            log.info("in insertAuthorizedRecord()..."+e);
            parseException.logException(e,true);
        }
    }
    
    // TO POPULATE CORRESPONDING RECORDS IN UI
    public void populateAuthorizeTab(int row){
        log.info("In populateAuthorizeTab...");
        try{
            ArrayList authorizedTableValue = (ArrayList)tblAuthorizedTab.getDataArrayList().get(row);
            HashMap authorizedOneRecord = new HashMap();
            //            no_AuthorizedSignatory = tblAuthorizedTab.getRowCount();
            java.util.Set keySet =  authorizedAll.keySet();
            Object[] objKeySet = (Object[]) keySet.toArray();
            // To populate the corresponding record from the Authorized Signatory HashMap
            for (int i = authorizedAll.size() - 1,j = 0;i >= 0 ;i--,++j){
                authorizedOneRecord = (HashMap)authorizedAll.get(objKeySet[j]);
                if ((CommonUtil.convertObjToStr(authorizedTableValue.get(0))).equals(CommonUtil.convertObjToStr(authorizedOneRecord.get(SLNO)))){
                    // To populate the Corresponding record from CTable
                    setTxtCustomerID(CommonUtil.convertObjToStr(authorizedOneRecord.get(CUSTID)));
                    setTxtName_AuthorizedSignatory(CommonUtil.convertObjToStr(authorizedOneRecord.get(NAME)));
                    setTxtLimits(CommonUtil.convertObjToStr(authorizedOneRecord.get(LIMITS)));
                    setTxtDesig_AuthorizedSignatory(CommonUtil.convertObjToStr(authorizedOneRecord.get(DESIGNATION)));
                    setCboAddrCommunication_AuthorizedSignatory(CommonUtil.convertObjToStr(getCbmAddrCommunication_AuthorizedSignatory().getDataForKey(authorizedOneRecord.get(ADDR_COMM))));
                    setTxtStreet_AuthorizedSignatory(CommonUtil.convertObjToStr(authorizedOneRecord.get(STREET)));
                    setTxtArea_AuthorizedSignatory(CommonUtil.convertObjToStr(authorizedOneRecord.get(AREA)));
                    setCboCity_AuthorizedSignatory(CommonUtil.convertObjToStr(getCbmCity_AuthorizedSignatory().getDataForKey(authorizedOneRecord.get(CITY))));
                    setCboState_AuthorizedSignatory(CommonUtil.convertObjToStr(getCbmState_AuthorizedSignatory().getDataForKey(authorizedOneRecord.get(STATE))));
                    setCboCountry_AuthorizedSignatory(CommonUtil.convertObjToStr(getCbmCountry_AuthorizedSignatory().getDataForKey(authorizedOneRecord.get(COUNTRY))));
                    setTxtPin_AuthorizedSignatory(CommonUtil.convertObjToStr(authorizedOneRecord.get(PIN)));
                    setTxtHomePhone_AuthorizedSignatory(CommonUtil.convertObjToStr(authorizedOneRecord.get(HOME_PHONE)));
                    setTxtBusinessPhone_AuthorizedSignatory(CommonUtil.convertObjToStr(authorizedOneRecord.get(BUSINESS_PHONE)));
                    setTxtHomeFax_AuthorizedSignatory(CommonUtil.convertObjToStr(authorizedOneRecord.get(HOME_FAX)));
                    setTxtBusinessFax_AuthorizedSignatory(CommonUtil.convertObjToStr(authorizedOneRecord.get(BUSINESS_FAX)));
                    setTxtPager_AuthorizedSignatory(CommonUtil.convertObjToStr(authorizedOneRecord.get(PAGER)));
                    setTxtMobile_AuthorizedSignatory(CommonUtil.convertObjToStr(authorizedOneRecord.get(MOBILE)));
                    setTxtEmailId_AuthorizedSignatory(CommonUtil.convertObjToStr(authorizedOneRecord.get(EMAILID)));
                    setChanged();
                    ttNotifyObservers();
                }
            }
            authorizedOneRecord = null;
            authorizedTableValue = null;
            keySet = null;
            objKeySet = null;
        }catch(Exception e){
            log.info("in populateAuthorizeTab()..."+e);
            parseException.logException(e,true);
        }
    }
    
    // TO UPDATE AUTHORIZED SIGNATORY TABLE (row is the corresponding Serial Number)
    private int updateAuthorizedTab(int row){
        log.info("In updateAuthorizedTab...");
        
        int option = -1;
        HashMap result = new HashMap();
        
        result = tableUtilAuthorize.updateTableValues(authorizedTabRow, authorizedRec, row);
        
        
        authorizeTableList = (ArrayList) result.get(TABLE_VALUES);
        authorizedAll = (LinkedHashMap) result.get(ALL_VALUES);
        option = CommonUtil.convertObjToInt(result.get(OPTION));
        
        tblAuthorizedTab.setDataArrayList(authorizeTableList, authorizedTabTitle);
        
        result = null;
        ttNotifyObservers();
        return option;
    }
    
    // Delete for the Table Authorized Signatory. (r is the corresponding Serial Number)
    public void deleteAuthorizedTab(int row){
        log.info("In deleteAuthorizedTab...");
        
        try{
            ArrayList data = tblAuthorizedTab.getDataArrayList();
            ArrayList temp = new ArrayList();
            final int dataSize = data.size();
            HashMap result = new HashMap();
            
            result = tableUtilAuthorize.deleteTableValues(row);
            authorizeTableList = (ArrayList) result.get(TABLE_VALUES);
            tblAuthorizedTab.setDataArrayList(authorizeTableList,authorizedTabTitle);
            authorizedAll = (LinkedHashMap) result.get(ALL_VALUES);
            
            temp = null;
            data = null;
            result = null;
            ttNotifyObservers();
        }catch(Exception e){
            log.info("Exception caught in deleteAuthorizedTab..."+e);
            parseException.logException(e,true);
        }
    }
    
    // To create objects
    public void createObject(){
        authorizedAll = new LinkedHashMap();
        authorizeTableList = new ArrayList();
        
        tblAuthorizedTab = new EnhancedTableModel(null, authorizedTabTitle);
        tableUtilAuthorize = new TableUtil();
        tableUtilAuthorize.setAttributeKey(SLNO);
        
    }
    
    // To destroy Objects
    public void destroyObjects(){
        tblAuthorizedTab = null;
        authorizedTabRow = null;
        authorizedRec = null;
        authorizedAll = null;
        authorizeTableList = null;
        tableUtilAuthorize = null;
    }
    
    public void authorizedSignatoryList(String Id) {
        HashMap where = new HashMap();
        HashMap map = new HashMap();
        HashMap maps = new HashMap();
        where.put("CUST_ID",Id);
        System.out.println("where@@@"+where);
        List lst = ClientUtil.executeQuery("getAuthorizedCustDetails", where);
        System.out.println("List@@@"+lst);
        if(lst.size()>0) {
            for(int i= 0; i<lst.size(); i++) {
                map = (HashMap)lst.get(i);
                maps.put("C_ID",CommonUtil.convertObjToStr(map.get("ACD")));
                System.out.println("maps"+maps);
                List list = ClientUtil.executeQuery("getAuthorizedCustDetailsForNew",maps);
                if(list.size() >0 && list !=null){
                    map=null;
                    map = (HashMap)list.get(0);
                    insertData(map, i);
                    //             tableUtilAuthorize.insertTableValues(authorizedTabRow, authorizedRec);
                    HashMap result = new HashMap();
                    result = tableUtilAuthorize.insertTableValues(authorizedTabRow, authorizedRec);
                    authorizeTableList = (ArrayList) result.get(TABLE_VALUES);
                    authorizedAll = (LinkedHashMap) result.get(ALL_VALUES);
                    tblAuthorizedTab.setDataArrayList(authorizeTableList,authorizedTabTitle);
                }
            }
        }
    }
    private void insertData(HashMap where, int slno) {
        authorizedTabRow = new ArrayList();
        authorizedRec = new HashMap();
        try{
            authorizedTabRow.add(String.valueOf(slno));
            authorizedTabRow.add(where.get("CUST_ID"));
            //authorizedTabRow.add(where.get("Name"));
            authorizedTabRow.add(where.get("NAME"));
            authorizedTabRow.add(CommonUtil.convertObjToStr(txtLimits));
            
            authorizedRec.put(CUSTID,where.get("CUST_ID"));
            authorizedRec.put(NAME,where.get("Name"));
            authorizedRec.put(LIMITS,CommonUtil.convertObjToStr(txtLimits));
            authorizedRec.put(SLNO,String.valueOf(slno));
            authorizedRec.put(DESIGNATION,where.get("DESIGNATION"));
            authorizedRec.put(ADDR_COMM,where.get("ADDR_TYPE"));
            authorizedRec.put(STREET,where.get("STREET"));
            authorizedRec.put(AREA,where.get("AREA"));
            authorizedRec.put(CITY,where.get("CITY"));
            authorizedRec.put(STATE,where.get("STATE"));
            authorizedRec.put(COUNTRY,where.get("COUNTRY"));
            authorizedRec.put(PIN,where.get("PIN_CODE"));
            authorizedRec.put(HOME_PHONE,where.get("PHONE_NUMBER"));
            authorizedRec.put(BUSINESS_PHONE,txtBusinessPhone_AuthorizedSignatory);
            authorizedRec.put(HOME_FAX,txtHomeFax_AuthorizedSignatory);
            authorizedRec.put(BUSINESS_FAX,txtBusinessFax_AuthorizedSignatory);
            authorizedRec.put(PAGER,txtPager_AuthorizedSignatory);
            authorizedRec.put(MOBILE,txtMobile_AuthorizedSignatory);
            authorizedRec.put(EMAILID,where.get("EMAIL_ID"));
            authorizedRec.put(AUTHORIZE_NO,String.valueOf(slno));
            authorizedRec.put(COMMAND,"");
        }catch(Exception e){
            log.info("in insertAuthorizedRecord()..."+e);
            parseException.logException(e,true);
        }
    }
    public boolean CheckForLimit() {
        if(tblAuthorizedTab.getRowCount()> 0)
            for(int i=0; i<tblAuthorizedTab.getRowCount();i++) {
                String data= CommonUtil.convertObjToStr(tblAuthorizedTab.getValueAt(i,3));
                if (data.equals("")||data.equals("0")) {
                    return true;
                }
            }
        return false;
    }
    
    /**
     * Getter for property acctNum.
     * @return Value of property acctNum.
     */
    public java.lang.String getAcctNum() {
        return acctNum;
    }
    
    /**
     * Setter for property acctNum.
     * @param acctNum New value of property acctNum.
     */
    public void setAcctNum(java.lang.String acctNum) {
        this.acctNum = acctNum;
    }
    
}

