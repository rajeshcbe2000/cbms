/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition.
 * 
 *
 * TermLoanGuarantorOB.java
 *
 * Created on July 5, 2004, 12:57 PM
 */

package com.see.truetransact.ui.termloan.agritermloan;

/**
 *
 * @author  shanmuga
 *
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
import com.see.truetransact.transferobject.termloan.agritermloan.AgriTermLoanGuarantorTO;
import com.see.truetransact.ui.TrueTransactMain;
import com.see.truetransact.uicomponent.CObservable;
import com.see.truetransact.uicomponent.COptionPane;
import java.util.Date;

import org.apache.log4j.Logger;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;

public class AgriTermLoanGuarantorOB extends CObservable{
    
    /** Creates a new instance of TermLoanGuarantorOB */
    private AgriTermLoanGuarantorOB() throws Exception{
        curDate = ClientUtil.getCurrentDate();
        termLoanGuarantorOB();
    }
    
    private       static AgriTermLoanGuarantorOB termLoanGuarantorOB;
    
    private final static Logger log = Logger.getLogger(AgriTermLoanGuarantorOB.class);
    private final static ClientParseException parseException = ClientParseException.getInstance();
    
    private final   String  ACCOUNT_NO = "ACCOUNT_NO";
    private final   String  SECURITY_TYPE="SECURITY TYPE";
    private final   String  ADD_SIS = "ADD_SIS";
    private final   String  ALL_VALUES = "ALL_VALUES";
    private final   String  AREA = "AREA";
    private final   String  ASON = "ASON";
    private final   String  CITY = "CITY";
    private final   String  COMMAND = "COMMAND";
    private final   String  CONSTITUTION = "CONSTITUTION";
    private final   String  COUNTRY = "COUNTRY";
    private final   String  COUNTRY_CODE = "COUNTRY_CODE";
    private final   String  CUSTID = "CUSTID";
    private final   String  DOB = "DOB";
    private final   String  G_ACC_NO = "G_ACC_NO";
    private final   String  G_NAME = "G_NAME";
    private final   String  G_NET_WORTH = "G_NET_WORTH";
    private final   String  G_PROD_ID = "G_PROD_ID";
    private final   String  G_PROD_TYPE = "G_PROD_TYPE";
    private final   String  INSERT = "INSERT";
    private final   String  OPTION = "OPTION";
    private final   String  PHONE = "PHONE";
    private final   String  PIN = "PIN";
    private final   String  PIN_CODE = "PIN_CODE";
    //    private final   String  PRODUCT_ID = "PRODUCT_ID";
    private final   String  SLNO = "SLNO";
    private final   String  STATE = "STATE";
    private final   String  STREET = "STREET";
    private final   String  TABLE_VALUES = "TABLE_VALUES";
    private final   String  UPDATE = "UPDATE";
    
    private final   java.util.ResourceBundle objTermLoanRB = java.util.ResourceBundle.getBundle("com.see.truetransact.ui.termloan.TermLoanRB", ProxyParameters.LANGUAGE);
    
    //    private final   TermLoanRB objTermLoanRB = new TermLoanRB();
    
    private final   ArrayList guarantorTabTitle = new ArrayList();      //  Table Title of Guarantor
    private ArrayList guarantorTabValues = new ArrayList();            // ArrayList to display in Guarantor Table
    private ArrayList guarantorEachTabRecord;
    
    private LinkedHashMap guarantorAll = new LinkedHashMap();          // Both displayed and hidden values in the table
    
    private HashMap guarantorEachRecord;
    
    private EnhancedTableModel tblGuarantorTab;
    
    private TableUtil tableUtilGuarantor = new TableUtil();
    
    private ComboBoxModel cbmCity_GD;
    private ComboBoxModel cbmState_GD;
    private ComboBoxModel cbmCountry_GD;
    private ComboBoxModel cbmConstitution_GD;
    private ComboBoxModel cbmProdId;
    private ComboBoxModel cbmProdType;
    private ComboBoxModel cbmSecurityType;
    private String borrowerNo = "";
    private String lblProdID_CD_Disp = "";
    private String txtCustomerID_GD = "";
    private String txtGuaranAccNo = "";
    private String txtGuaranName = "";
    private String txtStreet_GD = "";
    private String txtArea_GD = "";
    private String cboCity_GD = "";
    private String txtPin_GD = "";
    private String tdtDOB_GD = "";
    private String cboProdId = "";
    private String cboProdType = "";
    private String cboState_GD = "";
    private String cboCountry_GD = "";
    private String cboSecurityType="";
    private String txtPhone_GD = "";
    private String cboConstitution_GD = "";
    private String txtGuarantorNetWorth = "";
    private String tdtAsOn_GD = "";
    private String lblAccHead_GD_2 = "";
    private String lblAccNo_GD_2 = "";
    private String txtGuarantorNo = "";
    Date curDate = null;
    
    static {
        try {
            termLoanGuarantorOB = new AgriTermLoanGuarantorOB();
        } catch(Exception e) {
            log.info("try: " + e);
            parseException.logException(e,true);
        }
    }
    
    private void termLoanGuarantorOB() throws Exception{
        setGuarantorTabTitle();
        tableUtilGuarantor.setAttributeKey(SLNO);
        tblGuarantorTab = new EnhancedTableModel(null, guarantorTabTitle);
    }
    
    public static AgriTermLoanGuarantorOB getInstance() {
        return termLoanGuarantorOB;
    }
    
    private void setGuarantorTabTitle() throws Exception{
        try{
            guarantorTabTitle.add(objTermLoanRB.getString("tblColumnGuarantorNo"));
            guarantorTabTitle.add(objTermLoanRB.getString("tblColumnGuarantorCustID"));
            guarantorTabTitle.add(objTermLoanRB.getString("tblColumnGuarantorName"));
            guarantorTabTitle.add(objTermLoanRB.getString("tblColumnGuarantorAcc_No"));
        }catch(Exception e){
            log.info("Exception in setGuarantorTabTitle(): "+e);
            parseException.logException(e,true);
        }
    }
    
    public ArrayList getGuarantorTabTitle(){
        return this.guarantorTabTitle;
    }
    
    public void changeStatusGuarantor(int resultType){
        try{
            // Guarantor Details
            HashMap oneRecord;
            if (resultType != 2){
                //If the Main Save Button pressed
                tableUtilGuarantor.getRemovedValues().clear();
            }
            java.util.Set keySet =  guarantorAll.keySet();
            Object[] objKeySet = (Object[]) keySet.toArray();
            // To change the Insert command to Update after Save Buttone Pressed
            // Guarantor Details
            for (int i = keySet.size() - 1,j = 0;i >= 0;--i,++j){
                oneRecord = (HashMap) guarantorAll.get(objKeySet[j]);
                if (oneRecord.get(COMMAND).equals(INSERT)){
                    // If the status is in Insert Mode then change the mode to Update
                    oneRecord.put(COMMAND, UPDATE);
                    guarantorAll.put(objKeySet[j], oneRecord);
                }
                oneRecord = null;
            }
            keySet = null;
            objKeySet = null;
        }catch(Exception e){
            log.info("Exception in changeStatusGuarantor(): "+e);
            parseException.logException(e,true);
        }
    }
    
    public void resetAllGuarantorDetails(){
        resetGuarantorDetails();
        setLblAccHead_GD_2("");
        setLblAccNo_GD_2("");
        setLblProdID_GD_Disp("");
    }
    
    public void resetGuarantorDetails(){
        setTxtGuarantorNo("");
        setTxtCustomerID_GD("");
        setCboProdId("");
        setCboProdType("");
        setTdtDOB_GD("");
        setTxtGuaranAccNo("");
        setTxtGuaranName("");
        setTxtStreet_GD("");
        setTxtArea_GD("");
        setCboCity_GD("");
        setCboState_GD("");
        setTxtPin_GD("");
        setTxtPhone_GD("");
        setCboConstitution_GD("");
        setTxtGuarantorNetWorth("");
        setTdtAsOn_GD("");
        setCboCountry_GD("");
        setCboSecurityType("");
    }
    
    public void resetGuarantorCTable(){
        tblGuarantorTab.setDataArrayList(null, guarantorTabTitle);
        tableUtilGuarantor = new TableUtil();
        tableUtilGuarantor.setAttributeKey(SLNO);
    }
    
    public void removeAllProdID(){
        setCbmProdId("");
    }
    
    public void setTermLoanGuarantorTO(ArrayList guarantorList, String acctNum){
        try{
            AgriTermLoanGuarantorTO termLoanGuarantorTO;
            HashMap guarantorRecordMap;
            LinkedHashMap allGuarantorRecords = new LinkedHashMap();
            ArrayList removedValues = new ArrayList();
            ArrayList guarantorRecordList;
            ArrayList tabGuarantorRecords = new ArrayList();
            // To retrieve the Guarantor Details from the Serverside
            for (int i = guarantorList.size() - 1,j = 0;i >= 0;--i,++j){
                termLoanGuarantorTO = (AgriTermLoanGuarantorTO) guarantorList.get(j);
                guarantorRecordMap = new HashMap();
                guarantorRecordList = new ArrayList();
                
                guarantorRecordList.add(CommonUtil.convertObjToStr(termLoanGuarantorTO.getSlno()));
                guarantorRecordList.add(CommonUtil.convertObjToStr(termLoanGuarantorTO.getCustId()));
                guarantorRecordList.add(CommonUtil.convertObjToStr(termLoanGuarantorTO.getName()));
                guarantorRecordList.add(CommonUtil.convertObjToStr(termLoanGuarantorTO.getGuarantorAcNo()));
                
                tabGuarantorRecords.add(guarantorRecordList);
                
                guarantorRecordMap.put(SLNO, CommonUtil.convertObjToStr(termLoanGuarantorTO.getSlno()));
                guarantorRecordMap.put(CUSTID, CommonUtil.convertObjToStr(termLoanGuarantorTO.getCustId()));
                guarantorRecordMap.put(G_ACC_NO, CommonUtil.convertObjToStr(termLoanGuarantorTO.getGuarantorAcNo()));
                guarantorRecordMap.put(G_NAME, CommonUtil.convertObjToStr(termLoanGuarantorTO.getName()));
                guarantorRecordMap.put(G_PROD_ID, CommonUtil.convertObjToStr(termLoanGuarantorTO.getGuarantorProdId()));
                guarantorRecordMap.put(G_PROD_TYPE, CommonUtil.convertObjToStr(termLoanGuarantorTO.getGuarantorProdType()));
                guarantorRecordMap.put(DOB, DateUtil.getStringDate(termLoanGuarantorTO.getGuarantorNetworthOn()));
                guarantorRecordMap.put(STREET, CommonUtil.convertObjToStr(termLoanGuarantorTO.getStreet()));
                guarantorRecordMap.put(AREA, CommonUtil.convertObjToStr(termLoanGuarantorTO.getArea()));
                guarantorRecordMap.put(CITY, CommonUtil.convertObjToStr(termLoanGuarantorTO.getCity()));
                guarantorRecordMap.put(STATE, CommonUtil.convertObjToStr(termLoanGuarantorTO.getState()));
                guarantorRecordMap.put(COUNTRY, CommonUtil.convertObjToStr(termLoanGuarantorTO.getCountryCode()));
                guarantorRecordMap.put(PIN, CommonUtil.convertObjToStr(termLoanGuarantorTO.getPincode()));
                guarantorRecordMap.put(PHONE, CommonUtil.convertObjToStr(termLoanGuarantorTO.getPhone()));
                guarantorRecordMap.put(CONSTITUTION, CommonUtil.convertObjToStr(termLoanGuarantorTO.getConstitution()));
                guarantorRecordMap.put(G_NET_WORTH, CommonUtil.convertObjToStr(termLoanGuarantorTO.getGuarantorNetWorth()));
                guarantorRecordMap.put(ASON, DateUtil.getStringDate(termLoanGuarantorTO.getGuarantorNetworthOn()));
                guarantorRecordMap.put(ACCOUNT_NO, CommonUtil.convertObjToStr(termLoanGuarantorTO.getAcctNum()));
                guarantorRecordMap.put(SECURITY_TYPE, CommonUtil.convertObjToStr(termLoanGuarantorTO.getSecurityType()));
                
                guarantorRecordMap.put(COMMAND, UPDATE);
                
                allGuarantorRecords.put(CommonUtil.convertObjToStr(termLoanGuarantorTO.getSlno()), guarantorRecordMap);
                
                guarantorRecordList = null;
                guarantorRecordMap = null;
            }
            guarantorAll.clear();
            guarantorTabValues.clear();
            
            guarantorAll = allGuarantorRecords;
            guarantorTabValues = tabGuarantorRecords;
            
            tblGuarantorTab.setDataArrayList(guarantorTabValues, guarantorTabTitle);
            tableUtilGuarantor.setRemovedValues(removedValues);
            tableUtilGuarantor.setAllValues(guarantorAll);
            tableUtilGuarantor.setTableValues(guarantorTabValues);
            setMax_Del_Guarantor_No(acctNum);
            tabGuarantorRecords = null;
            allGuarantorRecords = null;
        }catch(Exception e){
            log.info("Error in setTermLoanGuarantorTO()..."+e);
            parseException.logException(e,true);
        }
    }
    
    private void setMax_Del_Guarantor_No(String acctNum){
        try{
            HashMap transactionMap = new HashMap();
            HashMap retrieve = new HashMap();
            transactionMap.put("acctNum", acctNum);
            List resultList = ClientUtil.executeQuery("getSelectAgriTermLoanGuarantorMaxSLNO", transactionMap);
            if (resultList.size() > 0){
                retrieve = (HashMap) resultList.get(0);
                tableUtilGuarantor.setStr_MAX_DEL_SL_NO(CommonUtil.convertObjToStr(retrieve.get("MAX_SL_NO")));
            }
            retrieve = null;
            transactionMap = null;
            resultList = null;
        }catch(Exception e){
            log.info("Error In setMax_Del_Guarantor_No: "+e);
            parseException.logException(e,true);
        }
    }
    
    public HashMap setTermLoanGuarantor(){
        HashMap guarantorMap = new HashMap();
        try{
            AgriTermLoanGuarantorTO objTermLoanGuarantorTO;
            java.util.Set keySet =  guarantorAll.keySet();
            Object[] objKeySet = (Object[]) keySet.toArray();
            HashMap oneRecord;
            // To set the values for Guarantor Transfer Object
            for (int i = keySet.size() - 1,j = 0;i >= 0;--i,++j){
                oneRecord = (HashMap) guarantorAll.get(objKeySet[j]);
                objTermLoanGuarantorTO = new AgriTermLoanGuarantorTO();
                objTermLoanGuarantorTO.setSlno(CommonUtil.convertObjToDouble( oneRecord.get(SLNO)));
                //                objTermLoanGuarantorTO.setBorrowNo(getBorrowerNo());
                //                objTermLoanGuarantorTO.setProdId(CommonUtil.convertObjToStr(oneRecord.get(PRODUCT_ID)));
                objTermLoanGuarantorTO.setAcctNum(CommonUtil.convertObjToStr(oneRecord.get(ACCOUNT_NO)));
                objTermLoanGuarantorTO.setCustId(CommonUtil.convertObjToStr( oneRecord.get(CUSTID)));
                objTermLoanGuarantorTO.setGuarantorAcNo(CommonUtil.convertObjToStr( oneRecord.get(G_ACC_NO)));
                objTermLoanGuarantorTO.setName(CommonUtil.convertObjToStr( oneRecord.get(G_NAME)));
                objTermLoanGuarantorTO.setStreet(CommonUtil.convertObjToStr(oneRecord.get(STREET)));
                objTermLoanGuarantorTO.setArea(CommonUtil.convertObjToStr(oneRecord.get(AREA)));
                objTermLoanGuarantorTO.setCity(CommonUtil.convertObjToStr(oneRecord.get(CITY)));
                objTermLoanGuarantorTO.setState(CommonUtil.convertObjToStr(oneRecord.get(STATE)));
                objTermLoanGuarantorTO.setCountryCode(CommonUtil.convertObjToStr(oneRecord.get(COUNTRY)));
                objTermLoanGuarantorTO.setPincode(CommonUtil.convertObjToStr(oneRecord.get(PIN)));
                objTermLoanGuarantorTO.setPhone(CommonUtil.convertObjToStr(oneRecord.get(PHONE)));
                
                //                objTermLoanGuarantorTO.setDob((Date)oneRecord.get(DOB));
                objTermLoanGuarantorTO.setDob(DateUtil.getDateMMDDYYYY(CommonUtil.convertObjToStr(oneRecord.get(DOB))));
                objTermLoanGuarantorTO.setGuarantorProdId(CommonUtil.convertObjToStr(oneRecord.get(G_PROD_ID)));
                objTermLoanGuarantorTO.setGuarantorProdType(CommonUtil.convertObjToStr(oneRecord.get(G_PROD_TYPE)));
                objTermLoanGuarantorTO.setConstitution(CommonUtil.convertObjToStr(oneRecord.get(CONSTITUTION)));
                objTermLoanGuarantorTO.setGuarantorNetWorth(CommonUtil.convertObjToDouble(oneRecord.get(G_NET_WORTH)));
                objTermLoanGuarantorTO.setSecurityType(CommonUtil.convertObjToStr(oneRecord.get(SECURITY_TYPE)));
                
                //                objTermLoanGuarantorTO.setGuarantorNetworthOn((Date)oneRecord.get(ASON));
                objTermLoanGuarantorTO.setGuarantorNetworthOn(DateUtil.getDateMMDDYYYY(CommonUtil.convertObjToStr(oneRecord.get(ASON))));
                objTermLoanGuarantorTO.setCommand(CommonUtil.convertObjToStr(oneRecord.get(COMMAND)));
                objTermLoanGuarantorTO.setStatusBy(TrueTransactMain.USER_ID);
                objTermLoanGuarantorTO.setStatusDt(curDate);
                if (oneRecord.get(COMMAND).equals(INSERT)){
                    objTermLoanGuarantorTO.setStatus(CommonConstants.STATUS_CREATED);
                }else if (oneRecord.get(COMMAND).equals(UPDATE)){
                    objTermLoanGuarantorTO.setStatus(CommonConstants.STATUS_MODIFIED);
                }
                guarantorMap.put(String.valueOf(j+1), objTermLoanGuarantorTO);
                
                oneRecord = null;
                objTermLoanGuarantorTO = null;
            }
            
            
            // To set the values for Guarantor Transfer Object
            // where as the existing records in Database are deleted in client side
            // useful for updating the status
            for (int i = tableUtilGuarantor.getRemovedValues().size() - 1,j = 0;i >= 0;--i,++j){
                oneRecord = (HashMap) tableUtilGuarantor.getRemovedValues().get(j);
                objTermLoanGuarantorTO = new AgriTermLoanGuarantorTO();
                objTermLoanGuarantorTO.setSlno(CommonUtil.convertObjToDouble( oneRecord.get(SLNO)));
                //                objTermLoanGuarantorTO.setBorrowNo(getBorrowerNo());
                //                objTermLoanGuarantorTO.setProdId(CommonUtil.convertObjToStr(oneRecord.get(PRODUCT_ID)));
                objTermLoanGuarantorTO.setAcctNum(CommonUtil.convertObjToStr(oneRecord.get(ACCOUNT_NO)));
                objTermLoanGuarantorTO.setCustId(CommonUtil.convertObjToStr( oneRecord.get(CUSTID)));
                objTermLoanGuarantorTO.setGuarantorAcNo(CommonUtil.convertObjToStr( oneRecord.get(G_ACC_NO)));
                objTermLoanGuarantorTO.setName(CommonUtil.convertObjToStr( oneRecord.get(G_NAME)));
                objTermLoanGuarantorTO.setStreet(CommonUtil.convertObjToStr(oneRecord.get(STREET)));
                objTermLoanGuarantorTO.setArea(CommonUtil.convertObjToStr(oneRecord.get(AREA)));
                objTermLoanGuarantorTO.setCity(CommonUtil.convertObjToStr(oneRecord.get(CITY)));
                objTermLoanGuarantorTO.setState(CommonUtil.convertObjToStr(oneRecord.get(STATE)));
                objTermLoanGuarantorTO.setCountryCode(CommonUtil.convertObjToStr(oneRecord.get(COUNTRY)));
                objTermLoanGuarantorTO.setPincode(CommonUtil.convertObjToStr(oneRecord.get(PIN)));
                objTermLoanGuarantorTO.setPhone(CommonUtil.convertObjToStr(oneRecord.get(PHONE)));
                objTermLoanGuarantorTO.setSecurityType(CommonUtil.convertObjToStr(oneRecord.get(SECURITY_TYPE)));
                //                objTermLoanGuarantorTO.setDob((Date)oneRecord.get(DOB));
                objTermLoanGuarantorTO.setDob(DateUtil.getDateMMDDYYYY(CommonUtil.convertObjToStr(oneRecord.get(DOB))));
                objTermLoanGuarantorTO.setGuarantorProdId(CommonUtil.convertObjToStr(oneRecord.get(G_PROD_ID)));
                objTermLoanGuarantorTO.setGuarantorProdType(CommonUtil.convertObjToStr(oneRecord.get(G_PROD_TYPE)));
                objTermLoanGuarantorTO.setConstitution(CommonUtil.convertObjToStr(oneRecord.get(CONSTITUTION)));
                objTermLoanGuarantorTO.setGuarantorNetWorth(CommonUtil.convertObjToDouble(oneRecord.get(G_NET_WORTH)));
                
                //                objTermLoanGuarantorTO.setGuarantorNetworthOn((Date)oneRecord.get(ASON));
                objTermLoanGuarantorTO.setGuarantorNetworthOn(DateUtil.getDateMMDDYYYY(CommonUtil.convertObjToStr(oneRecord.get(ASON))));
                objTermLoanGuarantorTO.setCommand(CommonUtil.convertObjToStr(oneRecord.get(COMMAND)));
                objTermLoanGuarantorTO.setStatus(CommonConstants.STATUS_DELETED);
                objTermLoanGuarantorTO.setStatusBy(TrueTransactMain.USER_ID);
                objTermLoanGuarantorTO.setStatusDt(curDate);
                guarantorMap.put(String.valueOf(guarantorMap.size()+1), objTermLoanGuarantorTO);
                
                oneRecord = null;
                objTermLoanGuarantorTO = null;
            }
        }catch(Exception e){
            log.info("Error In setTermLoanGuarantor() "+e);
            parseException.logException(e,true);
        }
        return guarantorMap;
    }
    
    public void setBorrowerNo(String borrowerNo){
        this.borrowerNo = borrowerNo;
        setChanged();
    }
    
    public String getBorrowerNo(){
        return this.borrowerNo;
    }
    
    void setTblGuarantorTab(EnhancedTableModel tblGuarantorTab){
        log.info("In setTblGuarantorTab()...");
        
        this.tblGuarantorTab = tblGuarantorTab;
        setChanged();
    }
    
    public EnhancedTableModel getTblGuarantorTab(){
        return this.tblGuarantorTab;
    }
    
    public void setLblAccHead_GD_2(String lblAccHead_GD_2){
        this.lblAccHead_GD_2 = lblAccHead_GD_2;
        setChanged();
    }
    
    public String getLblAccHead_GD_2(){
        return this.lblAccHead_GD_2;
    }
    
    void setLblProdID_GD_Disp(String lblProdID_CD_Disp){
        this.lblProdID_CD_Disp = lblProdID_CD_Disp;
        setChanged();
    }
    String getLblProdID_GD_Disp(){
        return this.lblProdID_CD_Disp;
    }
    
    public void setLblAccNo_GD_2(String lblAccNo_GD_2){
        this.lblAccNo_GD_2 = lblAccNo_GD_2;
        setChanged();
    }
    
    public String getLblAccNo_GD_2(){
        return this.lblAccNo_GD_2;
    }
    
    void setTxtCustomerID_GD(String txtCustomerID_GD){
        this.txtCustomerID_GD = txtCustomerID_GD;
        setChanged();
    }
    String getTxtCustomerID_GD(){
        return this.txtCustomerID_GD;
    }
    
    void setTxtGuaranAccNo(String txtGuaranAccNo){
        this.txtGuaranAccNo = txtGuaranAccNo;
        setChanged();
    }
    String getTxtGuaranAccNo(){
        return this.txtGuaranAccNo;
    }
    
    void setTxtGuaranName(String txtGuaranName){
        this.txtGuaranName = txtGuaranName;
        setChanged();
    }
    String getTxtGuaranName(){
        return this.txtGuaranName;
    }
    
    void setTxtGuarantorNo(String txtGuarantorNo){
        this.txtGuarantorNo = txtGuarantorNo;
        setChanged();
    }
    String getTxtGuarantorNo(){
        return this.txtGuarantorNo;
    }
    
    void setTxtStreet_GD(String txtStreet_GD){
        this.txtStreet_GD = txtStreet_GD;
        setChanged();
    }
    String getTxtStreet_GD(){
        return this.txtStreet_GD;
    }
    
    void setTxtArea_GD(String txtArea_GD){
        this.txtArea_GD = txtArea_GD;
        setChanged();
    }
    String getTxtArea_GD(){
        return this.txtArea_GD;
    }
    
    public void setCbmCity_GD(ComboBoxModel cbmCity_GD){
        this.cbmCity_GD = cbmCity_GD;
        setChanged();
    }
    
    ComboBoxModel getCbmCity_GD(){
        return cbmCity_GD;
    }
    
    void setCboCity_GD(String cboCity_GD){
        this.cboCity_GD = cboCity_GD;
        setChanged();
    }
    String getCboCity_GD(){
        return this.cboCity_GD;
    }
    
    void setTxtPin_GD(String txtPin_GD){
        this.txtPin_GD = txtPin_GD;
        setChanged();
    }
    String getTxtPin_GD(){
        return this.txtPin_GD;
    }
    
    void setCbmState_GD(ComboBoxModel cbmState_GD){
        this.cbmState_GD = cbmState_GD;
        setChanged();
    }
    
    ComboBoxModel getCbmState_GD(){
        return this.cbmState_GD;
    }
    
    void setCboState_GD(String cboState_GD){
        this.cboState_GD = cboState_GD;
        setChanged();
    }
    String getCboState_GD(){
        return this.cboState_GD;
    }
    
    public void setCbmCountry_GD(ComboBoxModel cbmCountry_GD){
        this.cbmCountry_GD = cbmCountry_GD;
        setChanged();
    }
    
    ComboBoxModel getCbmCountry_GD(){
        return cbmCountry_GD;
    }
    
    void setCboCountry_GD(String cboCountry_GD){
        this.cboCountry_GD = cboCountry_GD;
        setChanged();
    }
    String getCboCountry_GD(){
        return this.cboCountry_GD;
    }
    
    void setTxtPhone_GD(String txtPhone_GD){
        this.txtPhone_GD = txtPhone_GD;
        setChanged();
    }
    String getTxtPhone_GD(){
        return this.txtPhone_GD;
    }
    
    public void setCbmConstitution_GD(ComboBoxModel cbmConstitution_GD){
        this.cbmConstitution_GD = cbmConstitution_GD;
        setChanged();
    }
    
    ComboBoxModel getCbmConstitution_GD(){
        return cbmConstitution_GD;
    }
    
    
    void setCboConstitution_GD(String cboConstitution_GD){
        this.cboConstitution_GD = cboConstitution_GD;
        setChanged();
    }
    String getCboConstitution_GD(){
        return this.cboConstitution_GD;
    }
    
    void setTxtGuarantorNetWorth(String txtGuarantorNetWorth){
        this.txtGuarantorNetWorth = txtGuarantorNetWorth;
        setChanged();
    }
    String getTxtGuarantorNetWorth(){
        return this.txtGuarantorNetWorth;
    }
    
    void setTdtAsOn_GD(String tdtAsOn_GD){
        this.tdtAsOn_GD = tdtAsOn_GD;
        setChanged();
    }
    String getTdtAsOn_GD(){
        return this.tdtAsOn_GD;
    }
    
    public void ttNotifyObservers(){
        notifyObservers();
    }
    
    public int addGuarantorDetails(int recordPosition, boolean update){
        int option = -1;
        try{
            log.info("Add Guarantor Details...");
            guarantorEachTabRecord = new ArrayList();
            guarantorEachRecord = new HashMap();
            HashMap result = new HashMap();
            ArrayList data = tblGuarantorTab.getDataArrayList();
            tblGuarantorTab.setDataArrayList(data, guarantorTabTitle);
            final int dataSize = data.size();
            insertGuarantor(dataSize+1);
            if (!update) {
                // If the table is not in Edit Mode
                result = tableUtilGuarantor.insertTableValues(guarantorEachTabRecord, guarantorEachRecord);
                
                guarantorTabValues = (ArrayList) result.get(TABLE_VALUES);
                guarantorAll = (LinkedHashMap) result.get(ALL_VALUES);
                option = CommonUtil.convertObjToInt(result.get(OPTION));
                
                tblGuarantorTab.setDataArrayList(guarantorTabValues, guarantorTabTitle);
            }else{
                option = updateGuarantorTab(recordPosition);
            }
            
            setChanged();
            
            guarantorEachTabRecord = null;
            guarantorEachRecord = null;
            result = null;
            data = null;
        }catch(Exception e){
            log.info("Error in addGuarantorDetails()..."+e);
            parseException.logException(e,true);
        }
        return option;
    }
    
    private void insertGuarantor(int recordPosition){
        guarantorEachTabRecord.add(String.valueOf(recordPosition));
        guarantorEachTabRecord.add(getTxtCustomerID_GD());
        guarantorEachTabRecord.add(getTxtGuaranName());
        guarantorEachTabRecord.add(getTxtGuaranAccNo());
        
        guarantorEachRecord.put(SLNO, String.valueOf(recordPosition));
        guarantorEachRecord.put(CUSTID, getTxtCustomerID_GD());
        guarantorEachRecord.put(G_ACC_NO, getTxtGuaranAccNo());
        guarantorEachRecord.put(G_NAME, getTxtGuaranName());
        guarantorEachRecord.put(STREET, getTxtStreet_GD());
        guarantorEachRecord.put(AREA, getTxtArea_GD());
        guarantorEachRecord.put(CITY, CommonUtil.convertObjToStr(getCbmCity_GD().getKeyForSelected()));
        guarantorEachRecord.put(STATE, CommonUtil.convertObjToStr(getCbmState_GD().getKeyForSelected()));
        guarantorEachRecord.put(COUNTRY, CommonUtil.convertObjToStr(getCbmCountry_GD().getKeyForSelected()));
        guarantorEachRecord.put(PIN, getTxtPin_GD());
        guarantorEachRecord.put(PHONE, getTxtPhone_GD());
        guarantorEachRecord.put(DOB, getTdtDOB_GD());
        guarantorEachRecord.put(G_PROD_ID, CommonUtil.convertObjToStr(getCbmProdId().getKeyForSelected()));
        guarantorEachRecord.put(G_PROD_TYPE, CommonUtil.convertObjToStr(getCbmProdType().getKeyForSelected()));
        guarantorEachRecord.put(CONSTITUTION, CommonUtil.convertObjToStr(getCbmConstitution_GD().getKeyForSelected()));
        guarantorEachRecord.put(G_NET_WORTH, getTxtGuarantorNetWorth());
        guarantorEachRecord.put(SECURITY_TYPE, CommonUtil.convertObjToStr(getCbmSecurityType().getKeyForSelected()));
        guarantorEachRecord.put(ASON, getTdtAsOn_GD());
        guarantorEachRecord.put(ACCOUNT_NO, getLblAccNo_GD_2());
        //        guarantorEachRecord.put(PRODUCT_ID, getLblProdID_GD_Disp());
        guarantorEachRecord.put(COMMAND, "");
    }
    
    private int updateGuarantorTab(int recordPosition){
        int option = -1;
        HashMap result = new HashMap();
        try{
            result = tableUtilGuarantor.updateTableValues(guarantorEachTabRecord, guarantorEachRecord, recordPosition);
            
            guarantorTabValues = (ArrayList) result.get(TABLE_VALUES);
            guarantorAll = (LinkedHashMap) result.get(ALL_VALUES);
            option = CommonUtil.convertObjToInt(result.get(OPTION));
            
            tblGuarantorTab.setDataArrayList(guarantorTabValues, guarantorTabTitle);
            
        }catch(Exception e){
            log.info("Error in updateGuarantorTab()..."+e);
            parseException.logException(e,true);
        }
        result = null;
        return option;
    }
    
    public void populateGuarantorDetails(int recordPosition){
        try{
            HashMap eachRecs;
            java.util.Set keySet =  guarantorAll.keySet();
            Object[] objKeySet = (Object[]) keySet.toArray();
            String strRecordKey = (String) ((ArrayList) (tblGuarantorTab.getDataArrayList().get(recordPosition))).get(0);
            
            // To populate the corresponding record from the Guarantor Details
            for (int i = keySet.size() - 1, j = 0;i >= 0;--i, ++j){
                if (((String) ((HashMap) guarantorAll.get(objKeySet[j])).get(SLNO)).equals(strRecordKey)){
                    // To populate the Corresponding record from CTable
                    eachRecs = (HashMap) guarantorAll.get(objKeySet[j]);
                    setTxtGuarantorNo(CommonUtil.convertObjToStr(eachRecs.get(SLNO)));
                    setTxtCustomerID_GD(CommonUtil.convertObjToStr(eachRecs.get(CUSTID)));
                    setTxtGuaranAccNo(CommonUtil.convertObjToStr(eachRecs.get(G_ACC_NO)));
                    setTxtGuaranName(CommonUtil.convertObjToStr(eachRecs.get(G_NAME)));
                    setTxtStreet_GD(CommonUtil.convertObjToStr(eachRecs.get(STREET)));
                    setTxtArea_GD(CommonUtil.convertObjToStr(eachRecs.get(AREA)));
                    setCboCity_GD(CommonUtil.convertObjToStr(getCbmCity_GD().getDataForKey(CommonUtil.convertObjToStr(eachRecs.get(CITY)))));
                    setCboSecurityType(CommonUtil.convertObjToStr(getCbmSecurityType().getDataForKey(CommonUtil.convertObjToStr(eachRecs.get(SECURITY_TYPE)))));
                    setCboState_GD(CommonUtil.convertObjToStr(getCbmState_GD().getDataForKey(CommonUtil.convertObjToStr(eachRecs.get(STATE)))));
                    setTxtPin_GD(CommonUtil.convertObjToStr(eachRecs.get(PIN)));
                    setTxtPhone_GD(CommonUtil.convertObjToStr(eachRecs.get(PHONE)));
                    setCboConstitution_GD(CommonUtil.convertObjToStr(getCbmConstitution_GD().getDataForKey(CommonUtil.convertObjToStr(eachRecs.get(CONSTITUTION)))));
                    setTxtGuarantorNetWorth(CommonUtil.convertObjToStr(eachRecs.get(G_NET_WORTH)));
                    setCboCountry_GD(CommonUtil.convertObjToStr(getCbmCountry_GD().getDataForKey(CommonUtil.convertObjToStr(eachRecs.get(COUNTRY)))));
                    setTdtAsOn_GD(CommonUtil.convertObjToStr(eachRecs.get(ASON)));
                    setTdtDOB_GD(CommonUtil.convertObjToStr(eachRecs.get(DOB)));
                    setCboProdType(CommonUtil.convertObjToStr(getCbmProdType().getDataForKey(CommonUtil.convertObjToStr(eachRecs.get(G_PROD_TYPE)))));
                    setCbmProdId(CommonUtil.convertObjToStr(eachRecs.get(G_PROD_TYPE)));
                    setCboProdId(CommonUtil.convertObjToStr(getCbmProdId().getDataForKey(CommonUtil.convertObjToStr(eachRecs.get(G_PROD_ID)))));
                    
                    break;
                }
                eachRecs = null;
            }
            keySet = null;
            
            objKeySet = null;
            setChanged();
            ttNotifyObservers();
        }catch(Exception e){
            log.info("Error in populateGuarantorDetails()..."+e);
            parseException.logException(e,true);
        }
    }
    
    public void deleteGuarantorTabRecord(int recordPosition){
        HashMap result = new HashMap();
        try{
            result = tableUtilGuarantor.deleteTableValues(recordPosition);
            
            guarantorTabValues = (ArrayList) result.get(TABLE_VALUES);
            guarantorAll = (LinkedHashMap) result.get(ALL_VALUES);
            
            tblGuarantorTab.setDataArrayList(guarantorTabValues, guarantorTabTitle);
            
        }catch(Exception e){
            log.info("Error in deleteGuarantorTabRecord()..."+e);
            parseException.logException(e,true);
        }
        result = null;
    }
    
    public void setGuarantorCustName(String CUSTID){
        try {
            HashMap transactionMap = new HashMap();
            HashMap retrieve = new HashMap();
            transactionMap.put("custId",CUSTID);
            List resultList1 = (List) ClientUtil.executeQuery("getSelectCustomerOpenDate", transactionMap);
            if (resultList1.size() > 0){
                // If atleast one Record should exist
                retrieve = (HashMap) resultList1.get(0);
                if (retrieve.get("CUST_TYPE").equals("CORPORATE")){// If it is the Corporate Customer
                    setTxtGuaranName(CommonUtil.convertObjToStr(retrieve.get("COMP_NAME")));
                }else{
                    setTxtGuaranName(CommonUtil.convertObjToStr(retrieve.get("CUSTOMER NAME")));
                }
                if (CommonUtil.convertObjToStr(retrieve.get("NETWORTH_AS_ON")).length() > 0){
                    setTdtAsOn_GD(DateUtil.getStringDate((java.util.Date) retrieve.get("NETWORTH_AS_ON")));
                }
            }
            retrieve = null;
            transactionMap = null;
            resultList1 = null;
        }catch(Exception e){
            log.info("Exception caught in setGuarantorCustName: "+e);
            parseException.logException(e,true);
        }
    }
    
    public void setGuarantorCustOtherAccounts(String CUSTID){
        try {
            HashMap transactionMap = new HashMap();
            HashMap retrieve = new HashMap();
            transactionMap.put("CUST_ID",CUSTID);
            List resultList2 = ClientUtil.executeQuery("getSelectCustomerOtherAccounts", transactionMap);
            if (resultList2.size() > 0){
                // Atleast one Record should exist
                retrieve = (HashMap) resultList2.get(0);
                
                setCboProdType(CommonUtil.convertObjToStr(getCbmProdType().getDataForKey(CommonUtil.convertObjToStr(retrieve.get("PRODUCT_TYPE")))));
                setCbmProdId(CommonUtil.convertObjToStr(retrieve.get("PRODUCT_TYPE")));
                setCboProdId(CommonUtil.convertObjToStr(getCbmProdId().getDataForKey(CommonUtil.convertObjToStr(retrieve.get("PROD_ID")))));
                setTxtGuaranAccNo(CommonUtil.convertObjToStr(retrieve.get("ACCT_NO")));
            }
            retrieve = null;
            transactionMap = null;
            resultList2 = null;
        }catch(Exception e){
            log.info("Exception caught in setGuarantorCustAddr: "+e);
            parseException.logException(e,true);
        }
    }
    
    public void setGuarantorCustAddr(String CUSTID){
        try {
            HashMap transactionMap = new HashMap();
            HashMap retrieve = new HashMap();
            transactionMap.put("custId",CUSTID);
            List resultList2 = ClientUtil.executeQuery("getSelectCustomerAddress", transactionMap);
            if (resultList2.size() > 0){
                // Atleast one Record should exist
                retrieve = (HashMap) resultList2.get(0);
                
                setTxtArea_GD(CommonUtil.convertObjToStr(retrieve.get(AREA)));
                setTxtStreet_GD(CommonUtil.convertObjToStr(retrieve.get(STREET)));
                setCboCity_GD(CommonUtil.convertObjToStr(retrieve.get(CITY)));
                setCboState_GD(CommonUtil.convertObjToStr(retrieve.get(STATE)));
                setCboCountry_GD(CommonUtil.convertObjToStr(retrieve.get(COUNTRY)));
                setTxtPin_GD(CommonUtil.convertObjToStr(retrieve.get(PIN_CODE)));
            }
            retrieve = null;
            transactionMap = null;
            resultList2 = null;
        }catch(Exception e){
            log.info("Exception caught in setGuarantorCustAddr: "+e);
            parseException.logException(e,true);
        }
    }
    
    public void setGuarantorCustPhone(String CUSTID){
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
                setTxtPhone_GD(CommonUtil.convertObjToStr(stbPhoneNo));
            }
            transactionMap = null;
            resultList2 = null;
            retrieve = null;
            stbPhoneNo = null;
        }catch(Exception e){
            log.info("Exception caught in setGuarantorCustPhone: "+e);
            parseException.logException(e,true);
        }
    }
    
    // To create objects
    public void createObject(){
        guarantorTabValues = new ArrayList();
        guarantorAll = new LinkedHashMap();
        tblGuarantorTab.setDataArrayList(null, guarantorTabTitle);
        tableUtilGuarantor = new TableUtil();
        tableUtilGuarantor.setAttributeKey(SLNO);
    }
    
    // To destroy Objects
    public void destroyObjects(){
        guarantorTabValues = null;
        guarantorAll = null;
        tableUtilGuarantor = null;
    }
    
    /**
     * Getter for property tdtDOB_GD.
     * @return Value of property tdtDOB_GD.
     */
    public java.lang.String getTdtDOB_GD() {
        return tdtDOB_GD;
    }
    
    /**
     * Setter for property tdtDOB_GD.
     * @param tdtDOB_GD New value of property tdtDOB_GD.
     */
    public void setTdtDOB_GD(java.lang.String tdtDOB_GD) {
        this.tdtDOB_GD = tdtDOB_GD;
    }
    
    /**
     * Getter for property cboProdId.
     * @return Value of property cboProdId.
     */
    public java.lang.String getCboProdId() {
        return cboProdId;
    }
    
    /**
     * Setter for property cboProdId.
     * @param cboProdId New value of property cboProdId.
     */
    public void setCboProdId(java.lang.String cboProdId) {
        this.cboProdId = cboProdId;
    }
    
    /**
     * Getter for property cboProdType.
     * @return Value of property cboProdType.
     */
    public java.lang.String getCboProdType() {
        return cboProdType;
    }
    
    /**
     * Setter for property cboProdType.
     * @param cboProdType New value of property cboProdType.
     */
    public void setCboProdType(java.lang.String cboProdType) {
        this.cboProdType = cboProdType;
    }
    
    /**
     * Getter for property cbmProdId.
     * @return Value of property cbmProdId.
     */
    public com.see.truetransact.clientutil.ComboBoxModel getCbmProdId() {
        return cbmProdId;
    }
    
    /**
     * Setter for property cbmProdId.
     * @param cbmProdId New value of property cbmProdId.
     */
    public void setCbmProdId(String prodType) {
        try {
            if (prodType.length() > 0){
                HashMap lookUpHash = new HashMap();
                lookUpHash.put(CommonConstants.MAP_NAME,"Cash.getAccProduct" + prodType);
                lookUpHash.put(CommonConstants.PARAMFORQUERY, null);
                HashMap keyValue = ClientUtil.populateLookupData(lookUpHash);
                keyValue = (HashMap) keyValue.get(CommonConstants.DATA);
                
                ArrayList key = (ArrayList)keyValue.get(CommonConstants.KEY);
                ArrayList value = (ArrayList)keyValue.get(CommonConstants.VALUE);
                
                cbmProdId = new ComboBoxModel(key,value);
                this.cbmProdId = cbmProdId;
                
                key = null;
                value = null;
                lookUpHash = null;
                keyValue = null;
            }else{
                ArrayList key = new ArrayList();
                ArrayList val = new ArrayList();
                key.add("");
                val.add("");
                this.cbmProdId = new ComboBoxModel(key, val);
                
                key = null;
                val = null;
            }
            setChanged();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
    
    // Get the value from the Hash Map depending on the Value of Key...
    private void getKeyValue(HashMap keyValue)  throws Exception{
        log.info("In getKeyValue()");
        
    }
    /**
     * Getter for property cbmProdType.
     * @return Value of property cbmProdType.
     */
    public com.see.truetransact.clientutil.ComboBoxModel getCbmProdType() {
        return cbmProdType;
    }
    
    /**
     * Setter for property cbmProdType.
     * @param cbmProdType New value of property cbmProdType.
     */
    public void setCbmProdType(com.see.truetransact.clientutil.ComboBoxModel cbmProdType) {
        this.cbmProdType = cbmProdType;
    }
    
    /**
     * Getter for property cbmSecurityType.
     * @return Value of property cbmSecurityType.
     */
    public com.see.truetransact.clientutil.ComboBoxModel getCbmSecurityType() {
        return cbmSecurityType;
    }
    
    /**
     * Setter for property cbmSecurityType.
     * @param cbmSecurityType New value of property cbmSecurityType.
     */
    public void setCbmSecurityType(com.see.truetransact.clientutil.ComboBoxModel cbmSecurityType) {
        this.cbmSecurityType = cbmSecurityType;
    }
    
    /**
     * Getter for property cboSecurityType.
     * @return Value of property cboSecurityType.
     */
    public java.lang.String getCboSecurityType() {
        return cboSecurityType;
    }
    
    /**
     * Setter for property cboSecurityType.
     * @param cboSecurityType New value of property cboSecurityType.
     */
    public void setCboSecurityType(java.lang.String cboSecurityType) {
        this.cboSecurityType = cboSecurityType;
    }
    
}
