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

package com.see.truetransact.ui.termloan;

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
import com.see.truetransact.transferobject.termloan.TermLoanGuarantorTO;
import com.see.truetransact.transferobject.termloan.TermLoanInstitGuarantorTO;
import com.see.truetransact.ui.TrueTransactMain;
import com.see.truetransact.uicomponent.CObservable;
import com.see.truetransact.uicomponent.COptionPane;
import java.util.Date;

import org.apache.log4j.Logger;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;

public class TermLoanGuarantorOB extends CObservable{
    
    /** Creates a new instance of TermLoanGuarantorOB */
    private TermLoanGuarantorOB() throws Exception{
        curDate = ClientUtil.getCurrentDate();
        termLoanGuarantorOB();
    }
    
    private       static TermLoanGuarantorOB termLoanGuarantorOB;
    
    private final static Logger log = Logger.getLogger(TermLoanGuarantorOB.class);
    private final static ClientParseException parseException = ClientParseException.getInstance();
    
    private final   String  ACCOUNT_NO = "ACCOUNT_NO";
    private final   String  ADD_SIS = "ADD_SIS";
    private final   String  ALL_VALUES = "ALL_VALUES";
    private final   String  AREA = "AREA";
    private final   String  ASON = "ASON";
    private final   String  CITY = "CITY";
    private final   String  COMMAND = "COMMAND";
    private final   String  CONSTITUTION = "CONSTITUTION";
    private final   String  GUARANT_CONSTITUTION="GUARANT_CONSTITUTION";
     private final  String  GUARANT_INSTIT_CONSTITUTION="GUARANT_INSTIT_CONSTITUTION";
    private final   String  COUNTRY = "COUNTRY";
    private final   String  COUNTRY_CODE = "COUNTRY_CODE";
    private final   String  CUSTID = "CUSTID";
    
    private final   String  PLINAME="PLINAME";
    private final   String  PLIBRANCH="PLIBRANCH";
    private final   String  GUARANT_NO="GUARANT_NO";
    private final   String  GUARANT_DT="GUARANT_DT";
    private final   String  GUARANT_PERIOD_FROM="GUARANT_PERIOD_FROM";
    private final   String  GUARANT_PERIOD_TO="GUARANT_PERIOD_TO";
    private final   String  GUARANT_COMMISSION="GUARANT_COMMISSION";
    private final   String  GUARANT_STATUS="GUARANT_STATUS";
    private final   String  STATUS_DT="STATUS_DT";
    private final   String  STATUS_REMARKS="STATUS_REMARKS";
    
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
    private final   String  MEMBERNO = "MEMBERNO";
    
    private final   java.util.ResourceBundle objTermLoanRB = java.util.ResourceBundle.getBundle("com.see.truetransact.ui.termloan.TermLoanRB", ProxyParameters.LANGUAGE);
    
//    private final   TermLoanRB objTermLoanRB = new TermLoanRB();
    
    private final   ArrayList guarantorTabTitle = new ArrayList();      //  Table Title of Guarantor
     private final   ArrayList institGuarantorTabTitle = new ArrayList(); 
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
    private ComboBoxModel cbmPLIName;
    private ComboBoxModel cbmPLIBranch;
    private ComboBoxModel cbmGuaranStatus;
    
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
    private String txtPhone_GD = "";
    private String cboConstitution_GD = "";
    private String txtGuarantorNetWorth = "";
    private String tdtAsOn_GD = "";
    private String lblAccHead_GD_2 = "";
    private String lblAccNo_GD_2 = "";
    private String txtGuarantorNo = "";
    private boolean rdoGuarnIndividual=false;
    private boolean rdoGuarnInsititutional=false;
    private String memberNo = "";
    
private String  cboPLIName="";
private String cboPLIBranch="";
private String txtGuaratNo="";
private String tdtGuaranDate="";
private String tdtGuaranPeriodFrom="";
private String tdtGuaranPeriodTo="";
private String txtGuaranCommision="";
private String cboGuaranStatus="";
private String tdtStatusDate="";
private String txtGuarnRemarks="";



    Date curDate = null;
    
    static {
        try {
            termLoanGuarantorOB = new TermLoanGuarantorOB();
        } catch(Exception e) {
            log.info("try: " + e);
            parseException.logException(e,true);
        }
    }
    
    private void termLoanGuarantorOB() throws Exception{
        setGuarantorTabTitle();
        setInstitGuarantorTabTitle();
        tableUtilGuarantor.setAttributeKey(SLNO);
        tblGuarantorTab = new EnhancedTableModel(null, guarantorTabTitle);
    }
    
    public static TermLoanGuarantorOB getInstance() {
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
    
     private void setInstitGuarantorTabTitle() throws Exception{
        try{
            institGuarantorTabTitle.add(objTermLoanRB.getString("tblColumnGuarantorSlno"));
            institGuarantorTabTitle.add(objTermLoanRB.getString("tblColumnGuarantorPLIName"));
            institGuarantorTabTitle.add(objTermLoanRB.getString("tblColumnGuarantorPLIBranch"));
            institGuarantorTabTitle.add(objTermLoanRB.getString("tblColumnGuarantGuarNo"));
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
        resetInstitGuarantorDetails();
        setLblAccHead_GD_2("");
        setLblAccNo_GD_2("");
        setLblProdID_GD_Disp("");
       setRdoGuarnIndividual(false);
       setRdoGuarnInsititutional(false);
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
        setMemberNo("");
    }
     public void resetInstitGuarantorDetails(){
        setTxtGuaratNo("");
        setTxtGuaranCommision("");
        setCboPLIName("");
        setCboPLIBranch("");
        setTdtGuaranDate("");
        setTdtGuaranPeriodFrom("");
        setTdtGuaranPeriodTo("");
        setTdtStatusDate("");
        setCboGuaranStatus("");
        setTxtGuarnRemarks("");
       
    }
    
    public void resetGuarantorCTable(){
        tblGuarantorTab.setDataArrayList(null, guarantorTabTitle);
         tblGuarantorTab.setDataArrayList(null, institGuarantorTabTitle);
        tableUtilGuarantor = new TableUtil();
        tableUtilGuarantor.setAttributeKey(SLNO);
    }
    
    public void removeAllProdID(){
        setCbmProdId("");
    }
    /* from oracle database
     */
    public void setTermLoanGuarantorTO(ArrayList guarantorList, String acctNum){
        try{
            TermLoanGuarantorTO termLoanGuarantorTO;
            HashMap guarantorRecordMap;
            LinkedHashMap allGuarantorRecords = new LinkedHashMap();
            ArrayList removedValues = new ArrayList();
            ArrayList guarantorRecordList;
            ArrayList tabGuarantorRecords = new ArrayList();
            // To retrieve the Guarantor Details from the Serverside
            for (int i = guarantorList.size() - 1,j = 0;i >= 0;--i,++j){
                termLoanGuarantorTO = (TermLoanGuarantorTO) guarantorList.get(j);
                guarantorRecordMap = new HashMap();
                guarantorRecordList = new ArrayList();
                
                guarantorRecordList.add(CommonUtil.convertObjToStr(termLoanGuarantorTO.getSlno()));
                guarantorRecordList.add(CommonUtil.convertObjToStr(termLoanGuarantorTO.getCustId()));
                guarantorRecordList.add(CommonUtil.convertObjToStr(termLoanGuarantorTO.getName()));
                guarantorRecordList.add(CommonUtil.convertObjToStr(termLoanGuarantorTO.getGuarantorAcNo()));
                
                tabGuarantorRecords.add(guarantorRecordList);
                
                guarantorRecordMap.put(SLNO, CommonUtil.convertObjToStr(termLoanGuarantorTO.getSlno()));
                guarantorRecordMap.put(MEMBERNO, CommonUtil.convertObjToStr(termLoanGuarantorTO.getBorrowNo()));
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
                guarantorRecordMap.put(GUARANT_CONSTITUTION,  CommonUtil.convertObjToStr(termLoanGuarantorTO.getGuarntConstitution()));
                if(CommonUtil.convertObjToStr(termLoanGuarantorTO.getGuarntConstitution()).equals("Y")){
                    setRdoGuarnIndividual(true);
                    setRdoGuarnInsititutional(false);
                }else{
                     setRdoGuarnIndividual(false);
                    setRdoGuarnInsititutional(true);
                }
                guarantorRecordMap.put(G_NET_WORTH, CommonUtil.convertObjToStr(termLoanGuarantorTO.getGuarantorNetWorth()));
                guarantorRecordMap.put(ASON, DateUtil.getStringDate(termLoanGuarantorTO.getGuarantorNetworthOn()));
                guarantorRecordMap.put(ACCOUNT_NO, CommonUtil.convertObjToStr(termLoanGuarantorTO.getAcctNum()));
                
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
    
 /* from oracle database
     */
    public void setTermLoanInstitGuarantorTO(ArrayList guarantorList, String acctNum){
        try{
            TermLoanInstitGuarantorTO termLoanInstitGuarantorTO;
            HashMap guarantorRecordMap;
            LinkedHashMap allGuarantorRecords = new LinkedHashMap();
            ArrayList removedValues = new ArrayList();
            ArrayList guarantorRecordList;
            ArrayList tabGuarantorRecords = new ArrayList();
            // To retrieve the Guarantor Details from the Serverside
            if(guarantorList==null || guarantorList.isEmpty())
                return;
            for (int i = guarantorList.size() - 1,j = 0;i >= 0;--i,++j){
                termLoanInstitGuarantorTO = (TermLoanInstitGuarantorTO) guarantorList.get(j);
                guarantorRecordMap = new HashMap();
                guarantorRecordList = new ArrayList();
                  
                guarantorRecordList.add(CommonUtil.convertObjToStr(termLoanInstitGuarantorTO.getSlno()));
                guarantorRecordList.add(CommonUtil.convertObjToStr(termLoanInstitGuarantorTO.getPliName()));
                guarantorRecordList.add(CommonUtil.convertObjToStr(termLoanInstitGuarantorTO.getPliBranch()));
                guarantorRecordList.add(CommonUtil.convertObjToStr(termLoanInstitGuarantorTO.getGuaratNo()));
                
                tabGuarantorRecords.add(guarantorRecordList);
                
            
                
                guarantorRecordMap.put(SLNO, CommonUtil.convertObjToStr(termLoanInstitGuarantorTO.getSlno()));
                guarantorRecordMap.put(ACCOUNT_NO, CommonUtil.convertObjToStr(termLoanInstitGuarantorTO.getAcctNum()));
                guarantorRecordMap.put(PLINAME, CommonUtil.convertObjToStr(termLoanInstitGuarantorTO.getPliName()));
                guarantorRecordMap.put(PLIBRANCH, CommonUtil.convertObjToStr(termLoanInstitGuarantorTO.getPliBranch()));
                guarantorRecordMap.put(GUARANT_NO, CommonUtil.convertObjToStr(termLoanInstitGuarantorTO.getGuaratNo()));
                guarantorRecordMap.put(GUARANT_DT, CommonUtil.convertObjToStr(termLoanInstitGuarantorTO.getGuaranDate()));
                guarantorRecordMap.put(GUARANT_PERIOD_FROM, DateUtil.getStringDate(termLoanInstitGuarantorTO.getGuaranPeriodFrom()));
                guarantorRecordMap.put(GUARANT_PERIOD_TO, CommonUtil.convertObjToStr(termLoanInstitGuarantorTO.getGuaranPeriodTo()));
                guarantorRecordMap.put(GUARANT_COMMISSION, CommonUtil.convertObjToStr(termLoanInstitGuarantorTO.getGuaranCommision()));
                guarantorRecordMap.put(GUARANT_STATUS, CommonUtil.convertObjToStr(termLoanInstitGuarantorTO.getGuaranStatus()));
                guarantorRecordMap.put(STATUS_DT, CommonUtil.convertObjToStr(termLoanInstitGuarantorTO.getStatusDate()));
                guarantorRecordMap.put(STATUS_REMARKS, CommonUtil.convertObjToStr(termLoanInstitGuarantorTO.getGuarnRemarks()));
                guarantorRecordMap.put(GUARANT_INSTIT_CONSTITUTION, CommonUtil.convertObjToStr(termLoanInstitGuarantorTO.getGuarntConstitution()));
             
                if(CommonUtil.convertObjToStr(termLoanInstitGuarantorTO.getGuarntConstitution()).equals("Y")){
                    setRdoGuarnIndividual(false);
                    setRdoGuarnInsititutional(true);
                }else{
                     setRdoGuarnIndividual(true);
                    setRdoGuarnInsititutional(false);
                }
               
                
                guarantorRecordMap.put(COMMAND, UPDATE);
                
                allGuarantorRecords.put(CommonUtil.convertObjToStr(termLoanInstitGuarantorTO.getSlno()), guarantorRecordMap);
                
                guarantorRecordList = null;
                guarantorRecordMap = null;
            }
            guarantorAll.clear();
            guarantorTabValues.clear();
            
            guarantorAll = allGuarantorRecords;
            guarantorTabValues = tabGuarantorRecords;
            
            tblGuarantorTab.setDataArrayList(guarantorTabValues, institGuarantorTabTitle);
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
            List resultList = ClientUtil.executeQuery("getSelectTermLoanGuarantorMaxSLNO", transactionMap);
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
            TermLoanGuarantorTO objTermLoanGuarantorTO;
            java.util.Set keySet =  guarantorAll.keySet();
            Object[] objKeySet = (Object[]) keySet.toArray();
            HashMap oneRecord;
            // To set the values for Guarantor Transfer Object
            for (int i = keySet.size() - 1,j = 0;i >= 0;--i,++j){
                oneRecord = (HashMap) guarantorAll.get(objKeySet[j]);
                objTermLoanGuarantorTO = new TermLoanGuarantorTO();
                
                objTermLoanGuarantorTO.setSlno(CommonUtil.convertObjToDouble( oneRecord.get(SLNO)));
//                objTermLoanGuarantorTO.setBorrowNo(getBorrowerNo());
                //                objTermLoanGuarantorTO.setProdId(CommonUtil.convertObjToStr(oneRecord.get(PRODUCT_ID)));
                objTermLoanGuarantorTO.setBorrowNo(CommonUtil.convertObjToStr( oneRecord.get(MEMBERNO)));
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
               objTermLoanGuarantorTO.setGuarntConstitution(CommonUtil.convertObjToStr(oneRecord.get(GUARANT_CONSTITUTION)));
                objTermLoanGuarantorTO.setConstitution(CommonUtil.convertObjToStr(oneRecord.get(CONSTITUTION)));
                objTermLoanGuarantorTO.setGuarantorNetWorth(CommonUtil.convertObjToDouble(oneRecord.get(G_NET_WORTH)));
                
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
                if(CommonUtil.convertObjToStr(oneRecord.get(GUARANT_CONSTITUTION)).equals("Y"))
                    guarantorMap.put(String.valueOf(j+1), objTermLoanGuarantorTO);
                
                oneRecord = null;
                objTermLoanGuarantorTO = null;
            }
            
            
            // To set the values for Guarantor Transfer Object
            // where as the existing records in Database are deleted in client side
            // useful for updating the status
            for (int i = tableUtilGuarantor.getRemovedValues().size() - 1,j = 0;i >= 0;--i,++j){
                oneRecord = (HashMap) tableUtilGuarantor.getRemovedValues().get(j);
                objTermLoanGuarantorTO = new TermLoanGuarantorTO();
                objTermLoanGuarantorTO.setSlno(CommonUtil.convertObjToDouble( oneRecord.get(SLNO)));
//                objTermLoanGuarantorTO.setBorrowNo(getBorrowerNo());
                //                objTermLoanGuarantorTO.setProdId(CommonUtil.convertObjToStr(oneRecord.get(PRODUCT_ID)));
                objTermLoanGuarantorTO.setAcctNum(CommonUtil.convertObjToStr(oneRecord.get(ACCOUNT_NO)));
                objTermLoanGuarantorTO.setBorrowNo(CommonUtil.convertObjToStr( oneRecord.get(MEMBERNO)));
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
                objTermLoanGuarantorTO.setGuarntConstitution(CommonUtil.convertObjToStr(oneRecord.get(GUARANT_CONSTITUTION)));
//                objTermLoanGuarantorTO.setGuarantorNetworthOn((Date)oneRecord.get(ASON));
                objTermLoanGuarantorTO.setGuarantorNetworthOn(DateUtil.getDateMMDDYYYY(CommonUtil.convertObjToStr(oneRecord.get(ASON))));
                objTermLoanGuarantorTO.setCommand(CommonUtil.convertObjToStr(oneRecord.get(COMMAND)));
                objTermLoanGuarantorTO.setStatus(CommonConstants.STATUS_DELETED);
                objTermLoanGuarantorTO.setStatusBy(TrueTransactMain.USER_ID);
                objTermLoanGuarantorTO.setStatusDt(curDate);
                 if(CommonUtil.convertObjToStr(oneRecord.get(GUARANT_CONSTITUTION)).equals("Y"))
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
    
     public HashMap setTermInsititLoanGuarantor(){
        HashMap guarantorMap = new HashMap();
        try{
            TermLoanInstitGuarantorTO objTermLoanInstitGuarantorTO;
            java.util.Set keySet =  guarantorAll.keySet();
            Object[] objKeySet = (Object[]) keySet.toArray();
            HashMap oneRecord;
            // To set the values for Guarantor Transfer Object
            for (int i = keySet.size() - 1,j = 0;i >= 0;--i,++j){
                oneRecord = (HashMap) guarantorAll.get(objKeySet[j]);
                objTermLoanInstitGuarantorTO = new TermLoanInstitGuarantorTO();
                
                objTermLoanInstitGuarantorTO.setSlno(CommonUtil.convertObjToStr(oneRecord.get(SLNO)));
//                objTermLoanGuarantorTO.setBorrowNo(getBorrowerNo());
                //                objTermLoanGuarantorTO.setProdId(CommonUtil.convertObjToStr(oneRecord.get(PRODUCT_ID)));
                objTermLoanInstitGuarantorTO.setAcctNum(CommonUtil.convertObjToStr(oneRecord.get(ACCOUNT_NO)));
               
                objTermLoanInstitGuarantorTO.setPliName(CommonUtil.convertObjToStr( oneRecord.get(PLINAME)));
                objTermLoanInstitGuarantorTO.setPliBranch(CommonUtil.convertObjToStr( oneRecord.get(PLIBRANCH)));
                objTermLoanInstitGuarantorTO.setGuaratNo(CommonUtil.convertObjToStr(oneRecord.get(GUARANT_NO)));
                objTermLoanInstitGuarantorTO.setGuaranDate(DateUtil.getDateMMDDYYYY(CommonUtil.convertObjToStr(oneRecord.get(GUARANT_DT))));
                objTermLoanInstitGuarantorTO.setGuaranPeriodFrom(DateUtil.getDateMMDDYYYY(CommonUtil.convertObjToStr(oneRecord.get(GUARANT_PERIOD_FROM))));
                objTermLoanInstitGuarantorTO.setGuaranPeriodTo(DateUtil.getDateMMDDYYYY(CommonUtil.convertObjToStr(oneRecord.get(GUARANT_PERIOD_TO))));
                objTermLoanInstitGuarantorTO.setGuaranCommision(CommonUtil.convertObjToStr(oneRecord.get(GUARANT_COMMISSION)));
                objTermLoanInstitGuarantorTO.setGuaranStatus(CommonUtil.convertObjToStr(oneRecord.get(GUARANT_STATUS)));
                objTermLoanInstitGuarantorTO.setStatusDate(DateUtil.getDateMMDDYYYY(CommonUtil.convertObjToStr(oneRecord.get(STATUS_DT))));
                objTermLoanInstitGuarantorTO.setGuarnRemarks(CommonUtil.convertObjToStr(oneRecord.get(STATUS_REMARKS)));
                objTermLoanInstitGuarantorTO.setGuarntConstitution(CommonUtil.convertObjToStr(oneRecord.get(GUARANT_INSTIT_CONSTITUTION)));
//    
                objTermLoanInstitGuarantorTO.setCommand(CommonUtil.convertObjToStr(oneRecord.get(COMMAND)));
//                objTermLoanGuarantorTO.setStatusBy(TrueTransactMain.USER_ID);
//                objTermLoanGuarantorTO.setStatusDt(curDate);
                if (oneRecord.get(COMMAND).equals(INSERT)){
                    objTermLoanInstitGuarantorTO.setStatus(CommonConstants.STATUS_CREATED);
                }else if (oneRecord.get(COMMAND).equals(UPDATE)){
                    objTermLoanInstitGuarantorTO.setStatus(CommonConstants.STATUS_MODIFIED);
                }
                if(CommonUtil.convertObjToStr(oneRecord.get(GUARANT_INSTIT_CONSTITUTION)).equals("Y"))
                    guarantorMap.put(String.valueOf(j+1), objTermLoanInstitGuarantorTO);
                
                oneRecord = null;
                objTermLoanInstitGuarantorTO = null;
            }
            
            
            // To set the values for Guarantor Transfer Object
            // where as the existing records in Database are deleted in client side
            // useful for updating the status
            for (int i = tableUtilGuarantor.getRemovedValues().size() - 1,j = 0;i >= 0;--i,++j){
                oneRecord = (HashMap) tableUtilGuarantor.getRemovedValues().get(j);

                    objTermLoanInstitGuarantorTO = new TermLoanInstitGuarantorTO();

                    objTermLoanInstitGuarantorTO.setSlno(CommonUtil.convertObjToStr(oneRecord.get(SLNO)));
                    //                objTermLoanGuarantorTO.setBorrowNo(getBorrowerNo());
                    //                objTermLoanGuarantorTO.setProdId(CommonUtil.convertObjToStr(oneRecord.get(PRODUCT_ID)));
                    objTermLoanInstitGuarantorTO.setAcctNum(CommonUtil.convertObjToStr(oneRecord.get(ACCOUNT_NO)));

                    objTermLoanInstitGuarantorTO.setPliName(CommonUtil.convertObjToStr( oneRecord.get(PLINAME)));
                    objTermLoanInstitGuarantorTO.setPliBranch(CommonUtil.convertObjToStr( oneRecord.get(PLIBRANCH)));
                    objTermLoanInstitGuarantorTO.setGuaratNo(CommonUtil.convertObjToStr(oneRecord.get(GUARANT_NO)));
                    objTermLoanInstitGuarantorTO.setGuaranDate((Date)oneRecord.get(GUARANT_DT));
                    objTermLoanInstitGuarantorTO.setGuaranPeriodFrom((Date)oneRecord.get(GUARANT_PERIOD_FROM));
                    objTermLoanInstitGuarantorTO.setGuaranPeriodTo((Date)oneRecord.get(GUARANT_PERIOD_TO));
                    objTermLoanInstitGuarantorTO.setGuaranCommision(CommonUtil.convertObjToStr(oneRecord.get(GUARANT_COMMISSION)));
                    objTermLoanInstitGuarantorTO.setGuaranStatus(CommonUtil.convertObjToStr(oneRecord.get(GUARANT_STATUS)));
                    objTermLoanInstitGuarantorTO.setStatusDate(DateUtil.getDateMMDDYYYY(CommonUtil.convertObjToStr(oneRecord.get(STATUS_DT))));
                    objTermLoanInstitGuarantorTO.setGuarnRemarks(CommonUtil.convertObjToStr(oneRecord.get(STATUS_REMARKS)));                
//                objTermLoanGuarantorTO = new TermLoanGuarantorTO();
//                objTermLoanGuarantorTO.setSlno(CommonUtil.convertObjToDouble( oneRecord.get(SLNO)));
////                objTermLoanGuarantorTO.setBorrowNo(getBorrowerNo());
//                //                objTermLoanGuarantorTO.setProdId(CommonUtil.convertObjToStr(oneRecord.get(PRODUCT_ID)));
//                objTermLoanGuarantorTO.setAcctNum(CommonUtil.convertObjToStr(oneRecord.get(ACCOUNT_NO)));
//                objTermLoanGuarantorTO.setCustId(CommonUtil.convertObjToStr( oneRecord.get(CUSTID)));
//                objTermLoanGuarantorTO.setGuarantorAcNo(CommonUtil.convertObjToStr( oneRecord.get(G_ACC_NO)));
//                objTermLoanGuarantorTO.setName(CommonUtil.convertObjToStr( oneRecord.get(G_NAME)));
//                objTermLoanGuarantorTO.setStreet(CommonUtil.convertObjToStr(oneRecord.get(STREET)));
//                objTermLoanGuarantorTO.setArea(CommonUtil.convertObjToStr(oneRecord.get(AREA)));
//                objTermLoanGuarantorTO.setCity(CommonUtil.convertObjToStr(oneRecord.get(CITY)));
//                objTermLoanGuarantorTO.setState(CommonUtil.convertObjToStr(oneRecord.get(STATE)));
//                objTermLoanGuarantorTO.setCountryCode(CommonUtil.convertObjToStr(oneRecord.get(COUNTRY)));
//                objTermLoanGuarantorTO.setPincode(CommonUtil.convertObjToStr(oneRecord.get(PIN)));
//                objTermLoanGuarantorTO.setPhone(CommonUtil.convertObjToStr(oneRecord.get(PHONE)));
//                
////                objTermLoanGuarantorTO.setDob((Date)oneRecord.get(DOB));
//                objTermLoanGuarantorTO.setDob(DateUtil.getDateMMDDYYYY(CommonUtil.convertObjToStr(oneRecord.get(DOB))));
//                objTermLoanGuarantorTO.setGuarantorProdId(CommonUtil.convertObjToStr(oneRecord.get(G_PROD_ID)));
//                objTermLoanGuarantorTO.setGuarantorProdType(CommonUtil.convertObjToStr(oneRecord.get(G_PROD_TYPE)));
//                objTermLoanGuarantorTO.setConstitution(CommonUtil.convertObjToStr(oneRecord.get(CONSTITUTION)));
//                objTermLoanGuarantorTO.setGuarantorNetWorth(CommonUtil.convertObjToDouble(oneRecord.get(G_NET_WORTH)));
//                objTermLoanGuarantorTO.setGuarntConstitution(CommonUtil.convertObjToStr(oneRecord.get(GUARANT_CONSTITUTION)));
////                objTermLoanGuarantorTO.setGuarantorNetworthOn((Date)oneRecord.get(ASON));
//                objTermLoanGuarantorTO.setGuarantorNetworthOn(DateUtil.getDateMMDDYYYY(CommonUtil.convertObjToStr(oneRecord.get(ASON))));
                objTermLoanInstitGuarantorTO.setCommand(CommonUtil.convertObjToStr(oneRecord.get(COMMAND)));
                objTermLoanInstitGuarantorTO.setStatus(CommonConstants.STATUS_DELETED);
                 if(CommonUtil.convertObjToStr(oneRecord.get(GUARANT_INSTIT_CONSTITUTION)).equals("Y"))
                    guarantorMap.put(String.valueOf(guarantorMap.size()+1), objTermLoanInstitGuarantorTO);
                
                oneRecord = null;
                objTermLoanInstitGuarantorTO = null;
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
    
    EnhancedTableModel getTblGuarantorTab(){
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
    
     public int addInstitGuarantorDetails(int recordPosition, boolean update){
        int option = -1;
        try{
            log.info("Add Guarantor Details...");
            guarantorEachTabRecord = new ArrayList();
            guarantorEachRecord = new HashMap();
            HashMap result = new HashMap();
            ArrayList data = tblGuarantorTab.getDataArrayList();
            tblGuarantorTab.setDataArrayList(data, institGuarantorTabTitle);
            final int dataSize = data.size();
            insertInstitGuarantor(dataSize+1);
            if (!update) {
                // If the table is not in Edit Mode
                result = tableUtilGuarantor.insertTableValues(guarantorEachTabRecord, guarantorEachRecord);
                
                guarantorTabValues = (ArrayList) result.get(TABLE_VALUES);
                guarantorAll = (LinkedHashMap) result.get(ALL_VALUES);
                option = CommonUtil.convertObjToInt(result.get(OPTION));
                
                tblGuarantorTab.setDataArrayList(guarantorTabValues, institGuarantorTabTitle);
            }else{
                option = updateInstitGuarantorTab(recordPosition);
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
        guarantorEachRecord.put(MEMBERNO, getMemberNo());
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
         if(isRdoGuarnIndividual())
            guarantorEachRecord.put(GUARANT_CONSTITUTION, "Y");
        else
             guarantorEachRecord.put(GUARANT_CONSTITUTION, "N");
        guarantorEachRecord.put(G_NET_WORTH, getTxtGuarantorNetWorth());
        guarantorEachRecord.put(ASON, getTdtAsOn_GD());
        guarantorEachRecord.put(ACCOUNT_NO, getLblAccNo_GD_2());
        //        guarantorEachRecord.put(PRODUCT_ID, getLblProdID_GD_Disp());
        guarantorEachRecord.put(COMMAND, "");
    }
    
    
     private void insertInstitGuarantor(int recordPosition){
        guarantorEachTabRecord.add(String.valueOf(recordPosition));
        guarantorEachTabRecord.add(CommonUtil.convertObjToStr(getCbmPLIName().getKeyForSelected()));
        guarantorEachTabRecord.add(CommonUtil.convertObjToStr(getCbmPLIBranch().getKeyForSelected()));
        guarantorEachTabRecord.add(getTxtGuaratNo());
        
        guarantorEachRecord.put(SLNO, String.valueOf(recordPosition));
        guarantorEachRecord.put(PLINAME, CommonUtil.convertObjToStr(getCbmPLIName().getKeyForSelected()));
        guarantorEachRecord.put(PLIBRANCH, CommonUtil.convertObjToStr(getCbmPLIBranch().getKeyForSelected()));
        guarantorEachRecord.put(GUARANT_NO, getTxtGuaratNo());
        guarantorEachRecord.put(GUARANT_DT, getTdtGuaranDate());
        guarantorEachRecord.put(GUARANT_PERIOD_FROM,getTdtGuaranPeriodFrom());
        guarantorEachRecord.put(GUARANT_PERIOD_TO, getTdtGuaranPeriodTo());
        guarantorEachRecord.put(GUARANT_COMMISSION,  getTxtGuaranCommision());
        guarantorEachRecord.put(GUARANT_STATUS, CommonUtil.convertObjToStr(getCbmGuaranStatus().getKeyForSelected()));
        guarantorEachRecord.put(STATUS_DT, getTdtStatusDate());
        guarantorEachRecord.put(STATUS_REMARKS, getTxtGuarnRemarks());
        guarantorEachRecord.put(ACCOUNT_NO, getLblAccNo_GD_2());
     
        if(isRdoGuarnInsititutional())
            guarantorEachRecord.put(GUARANT_INSTIT_CONSTITUTION, "Y");
        else
             guarantorEachRecord.put(GUARANT_INSTIT_CONSTITUTION, "N");
   
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
    
    private int updateInstitGuarantorTab(int recordPosition){
        int option = -1;
        HashMap result = new HashMap();
        try{
            result = tableUtilGuarantor.updateTableValues(guarantorEachTabRecord, guarantorEachRecord, recordPosition);
            
            guarantorTabValues = (ArrayList) result.get(TABLE_VALUES);
            guarantorAll = (LinkedHashMap) result.get(ALL_VALUES);
            option = CommonUtil.convertObjToInt(result.get(OPTION));
            
            tblGuarantorTab.setDataArrayList(guarantorTabValues, institGuarantorTabTitle);
            
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
                    setMemberNo(CommonUtil.convertObjToStr(eachRecs.get(MEMBERNO)));
                    setTxtCustomerID_GD(CommonUtil.convertObjToStr(eachRecs.get(CUSTID)));
                    setTxtGuaranAccNo(CommonUtil.convertObjToStr(eachRecs.get(G_ACC_NO)));
                    setTxtGuaranName(CommonUtil.convertObjToStr(eachRecs.get(G_NAME)));
                    setTxtStreet_GD(CommonUtil.convertObjToStr(eachRecs.get(STREET)));
                    setTxtArea_GD(CommonUtil.convertObjToStr(eachRecs.get(AREA)));
                    setCboCity_GD(CommonUtil.convertObjToStr(getCbmCity_GD().getDataForKey(CommonUtil.convertObjToStr(eachRecs.get(CITY)))));
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
    
     public void populateInstitGuarantorDetails(int recordPosition){
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
                    setTxtGuaratNo(CommonUtil.convertObjToStr(eachRecs.get(SLNO)));
                    setTxtGuaranCommision(CommonUtil.convertObjToStr(eachRecs.get(GUARANT_COMMISSION)));
                    setTdtGuaranDate(CommonUtil.convertObjToStr(eachRecs.get(GUARANT_DT)));
                    setTdtGuaranPeriodFrom(CommonUtil.convertObjToStr(eachRecs.get(GUARANT_PERIOD_FROM)));
                    setTdtGuaranPeriodTo(CommonUtil.convertObjToStr(eachRecs.get(GUARANT_PERIOD_TO)));
                    
                    setCboPLIName(CommonUtil.convertObjToStr(getCbmPLIName().getDataForKey(CommonUtil.convertObjToStr(eachRecs.get(PLINAME)))));
                    getPliBranch(CommonUtil.convertObjToStr(getCbmPLIName().getDataForKey(CommonUtil.convertObjToStr(eachRecs.get(PLINAME)))));
                    setCboPLIBranch(CommonUtil.convertObjToStr(getCbmPLIBranch().getDataForKey(CommonUtil.convertObjToStr(eachRecs.get(PLIBRANCH)))));
                    setCboGuaranStatus(CommonUtil.convertObjToStr(getCbmGuaranStatus().getDataForKey(CommonUtil.convertObjToStr(eachRecs.get(GUARANT_STATUS)))));
                    setTxtGuarnRemarks(CommonUtil.convertObjToStr(eachRecs.get(STATUS_REMARKS)));
                    setTdtStatusDate(CommonUtil.convertObjToStr(eachRecs.get(STATUS_DT)));
                    setRdoGuarnInsititutional(true);
//                    setCboConstitution_GD(CommonUtil.convertObjToStr(getCbmConstitution_GD().getDataForKey(CommonUtil.convertObjToStr(eachRecs.get(CONSTITUTION)))));
//                    setTxtGuarantorNetWorth(CommonUtil.convertObjToStr(eachRecs.get(G_NET_WORTH)));
//                    setCboCountry_GD(CommonUtil.convertObjToStr(getCbmCountry_GD().getDataForKey(CommonUtil.convertObjToStr(eachRecs.get(COUNTRY)))));
//                    setTdtAsOn_GD(CommonUtil.convertObjToStr(eachRecs.get(ASON)));
//                    setTdtDOB_GD(CommonUtil.convertObjToStr(eachRecs.get(DOB)));
//                    setCboProdType(CommonUtil.convertObjToStr(getCbmProdType().getDataForKey(CommonUtil.convertObjToStr(eachRecs.get(G_PROD_TYPE)))));
//                    setCbmProdId(CommonUtil.convertObjToStr(eachRecs.get(G_PROD_TYPE)));
//                    setCboProdId(CommonUtil.convertObjToStr(getCbmProdId().getDataForKey(CommonUtil.convertObjToStr(eachRecs.get(G_PROD_ID)))));
                    
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
        tblGuarantorTab.setDataArrayList(null, institGuarantorTabTitle);
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
    
    
    public void getPliBranch(String pli){
        try{
            if(pli.length()>0){
                HashMap lookUpHash=new HashMap();
                lookUpHash.put(CommonConstants.MAP_NAME, "getOtherBankBranchs2");
                lookUpHash.put(CommonConstants.PARAMFORQUERY, pli);
                HashMap keyValue = ClientUtil.populateLookupData(lookUpHash);
                keyValue = (HashMap) keyValue.get(CommonConstants.DATA);
                ArrayList key = (ArrayList)keyValue.get(CommonConstants.KEY);
                ArrayList value = (ArrayList)keyValue.get(CommonConstants.VALUE);
                cbmPLIBranch = new ComboBoxModel(key,value);
                key = null;
                value = null;
                lookUpHash = null;
                keyValue = null;
            }else{
                ArrayList key = new ArrayList();
                ArrayList val = new ArrayList();
                key.add("");
                val.add("");
                this.cbmPLIBranch = new ComboBoxModel(key, val);
                key = null;
                val = null;
            }
            setChanged();
        }catch(NullPointerException e){
            parseException.logException(e,true);
        }catch(Exception e){
            parseException.logException(e,true);
        }
        
    }
    
    /**
     * Getter for property cbmPLIName.
     * @return Value of property cbmPLIName.
     */
    public com.see.truetransact.clientutil.ComboBoxModel getCbmPLIName() {
        return cbmPLIName;
    }
    
    /**
     * Setter for property cbmPLIName.
     * @param cbmPLIName New value of property cbmPLIName.
     */
    public void setCbmPLIName(com.see.truetransact.clientutil.ComboBoxModel cbmPLIName) {
        this.cbmPLIName = cbmPLIName;
    }
    
    /**
     * Getter for property cbmPLIBranch.
     * @return Value of property cbmPLIBranch.
     */
    public com.see.truetransact.clientutil.ComboBoxModel getCbmPLIBranch() {
        return cbmPLIBranch;
    }
    
    /**
     * Setter for property cbmPLIBranch.
     * @param cbmPLIBranch New value of property cbmPLIBranch.
     */
    public void setCbmPLIBranch(com.see.truetransact.clientutil.ComboBoxModel cbmPLIBranch) {
        this.cbmPLIBranch = cbmPLIBranch;
    }
    
    /**
     * Getter for property cbmGuaranStatus.
     * @return Value of property cbmGuaranStatus.
     */
    public com.see.truetransact.clientutil.ComboBoxModel getCbmGuaranStatus() {
        return cbmGuaranStatus;
    }
    
    /**
     * Setter for property cbmGuaranStatus.
     * @param cbmGuaranStatus New value of property cbmGuaranStatus.
     */
    public void setCbmGuaranStatus(com.see.truetransact.clientutil.ComboBoxModel cbmGuaranStatus) {
        this.cbmGuaranStatus = cbmGuaranStatus;
    }
    
    /**
     * Getter for property rdoGuarnIndividual.
     * @return Value of property rdoGuarnIndividual.
     */
    public boolean isRdoGuarnIndividual() {
        return rdoGuarnIndividual;
    }
    
    /**
     * Setter for property rdoGuarnIndividual.
     * @param rdoGuarnIndividual New value of property rdoGuarnIndividual.
     */
    public void setRdoGuarnIndividual(boolean rdoGuarnIndividual) {
        this.rdoGuarnIndividual = rdoGuarnIndividual;
    }
    
    /**
     * Getter for property rdoGuarnInsititutional.
     * @return Value of property rdoGuarnInsititutional.
     */
    public boolean isRdoGuarnInsititutional() {
        return rdoGuarnInsititutional;
    }
    
    /**
     * Setter for property rdoGuarnInsititutional.
     * @param rdoGuarnInsititutional New value of property rdoGuarnInsititutional.
     */
    public void setRdoGuarnInsititutional(boolean rdoGuarnInsititutional) {
        this.rdoGuarnInsititutional = rdoGuarnInsititutional;
    }
    
    /**
     * Getter for property cboPLIName.
     * @return Value of property cboPLIName.
     */
    public java.lang.String getCboPLIName() {
        return cboPLIName;
    }
    
    /**
     * Setter for property cboPLIName.
     * @param cboPLIName New value of property cboPLIName.
     */
    public void setCboPLIName(java.lang.String cboPLIName) {
        this.cboPLIName = cboPLIName;
    }
    
    /**
     * Getter for property cboPLIBranch.
     * @return Value of property cboPLIBranch.
     */
    public java.lang.String getCboPLIBranch() {
        return cboPLIBranch;
    }
    
    /**
     * Setter for property cboPLIBranch.
     * @param cboPLIBranch New value of property cboPLIBranch.
     */
    public void setCboPLIBranch(java.lang.String cboPLIBranch) {
        this.cboPLIBranch = cboPLIBranch;
    }
    
    /**
     * Getter for property txtGuaratNo.
     * @return Value of property txtGuaratNo.
     */
    public java.lang.String getTxtGuaratNo() {
        return txtGuaratNo;
    }
    
    /**
     * Setter for property txtGuaratNo.
     * @param txtGuaratNo New value of property txtGuaratNo.
     */
    public void setTxtGuaratNo(java.lang.String txtGuaratNo) {
        this.txtGuaratNo = txtGuaratNo;
    }
    
    /**
     * Getter for property tdtGuaranDate.
     * @return Value of property tdtGuaranDate.
     */
    public java.lang.String getTdtGuaranDate() {
        return tdtGuaranDate;
    }
    
    /**
     * Setter for property tdtGuaranDate.
     * @param tdtGuaranDate New value of property tdtGuaranDate.
     */
    public void setTdtGuaranDate(java.lang.String tdtGuaranDate) {
        this.tdtGuaranDate = tdtGuaranDate;
    }
    
    /**
     * Getter for property tdtGuaranPeriodFrom.
     * @return Value of property tdtGuaranPeriodFrom.
     */
    public java.lang.String getTdtGuaranPeriodFrom() {
        return tdtGuaranPeriodFrom;
    }
    
    /**
     * Setter for property tdtGuaranPeriodFrom.
     * @param tdtGuaranPeriodFrom New value of property tdtGuaranPeriodFrom.
     */
    public void setTdtGuaranPeriodFrom(java.lang.String tdtGuaranPeriodFrom) {
        this.tdtGuaranPeriodFrom = tdtGuaranPeriodFrom;
    }
    
    /**
     * Getter for property tdtGuaranPeriodTo.
     * @return Value of property tdtGuaranPeriodTo.
     */
    public java.lang.String getTdtGuaranPeriodTo() {
        return tdtGuaranPeriodTo;
    }
    
    /**
     * Setter for property tdtGuaranPeriodTo.
     * @param tdtGuaranPeriodTo New value of property tdtGuaranPeriodTo.
     */
    public void setTdtGuaranPeriodTo(java.lang.String tdtGuaranPeriodTo) {
        this.tdtGuaranPeriodTo = tdtGuaranPeriodTo;
    }
    
    /**
     * Getter for property txtGuaranCommision.
     * @return Value of property txtGuaranCommision.
     */
    public java.lang.String getTxtGuaranCommision() {
        return txtGuaranCommision;
    }
    
    /**
     * Setter for property txtGuaranCommision.
     * @param txtGuaranCommision New value of property txtGuaranCommision.
     */
    public void setTxtGuaranCommision(java.lang.String txtGuaranCommision) {
        this.txtGuaranCommision = txtGuaranCommision;
    }
    
    /**
     * Getter for property cboGuaranStatus.
     * @return Value of property cboGuaranStatus.
     */
    public java.lang.String getCboGuaranStatus() {
        return cboGuaranStatus;
    }
    
    /**
     * Setter for property cboGuaranStatus.
     * @param cboGuaranStatus New value of property cboGuaranStatus.
     */
    public void setCboGuaranStatus(java.lang.String cboGuaranStatus) {
        this.cboGuaranStatus = cboGuaranStatus;
    }
    
    /**
     * Getter for property tdtStatusDate.
     * @return Value of property tdtStatusDate.
     */
    public java.lang.String getTdtStatusDate() {
        return tdtStatusDate;
    }
    
    /**
     * Setter for property tdtStatusDate.
     * @param tdtStatusDate New value of property tdtStatusDate.
     */
    public void setTdtStatusDate(java.lang.String tdtStatusDate) {
        this.tdtStatusDate = tdtStatusDate;
    }
    
    /**
     * Getter for property txtGuarnRemarks.
     * @return Value of property txtGuarnRemarks.
     */
    public java.lang.String getTxtGuarnRemarks() {
        return txtGuarnRemarks;
    }
    
    /**
     * Setter for property txtGuarnRemarks.
     * @param txtGuarnRemarks New value of property txtGuarnRemarks.
     */
    public void setTxtGuarnRemarks(java.lang.String txtGuarnRemarks) {
        this.txtGuarnRemarks = txtGuarnRemarks;
    }
    
    /**
     * Getter for property institGuarantorTabTitle.
     * @return Value of property institGuarantorTabTitle.
     */
    public java.util.ArrayList getInstitGuarantorTabTitle() {
        return institGuarantorTabTitle;
    }
    
    /**
     * Getter for property memberNo.
     * @return Value of property memberNo.
     */
    public java.lang.String getMemberNo() {
        return memberNo;
    }    
    
    /**
     * Setter for property memberNo.
     * @param memberNo New value of property memberNo.
     */
    public void setMemberNo(java.lang.String memberNo) {
        this.memberNo = memberNo;
        setChanged();
    }    
    
}
