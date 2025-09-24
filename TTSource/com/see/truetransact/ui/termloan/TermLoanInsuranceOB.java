/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition.
 * 
 *
 * TermLoanInsuranceOB.java
 *
 * Created on July 6, 2004, 11:29 AM
 */

package com.see.truetransact.ui.termloan;

/**
 *
 * @author  shanmuga
 *
 */

import com.see.truetransact.clientutil.*;
import com.see.truetransact.clientexception.ClientParseException;
import com.see.truetransact.commonutil.CommonConstants;
import com.see.truetransact.commonutil.DateUtil;
import com.see.truetransact.commonutil.CommonUtil;
import com.see.truetransact.commonutil.TTException;
import com.see.truetransact.transferobject.termloan.TermLoanInsuranceTO;
import com.see.truetransact.ui.TrueTransactMain;
import com.see.truetransact.uicomponent.CObservable;
import com.see.truetransact.uicomponent.COptionPane;

import org.apache.log4j.Logger;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Date;


public class TermLoanInsuranceOB extends CObservable{
    
    /** Creates a new instance of TermLoanInsuranceOB */
    private TermLoanInsuranceOB() throws Exception{
        curDate = ClientUtil.getCurrentDate();
        termLoanInsuranceOB();
    }
    
    private       static TermLoanInsuranceOB termLoanInsuranceOB;
    
    private final static Logger log = Logger.getLogger(TermLoanInsuranceOB.class);
    private final static ClientParseException parseException = ClientParseException.getInstance();
    
    private final   String  ACCOUNT_NO = "ACCOUNT_NO";
    private final   String  ALL_VALUES = "ALL_VALUES";
    private final   String  COMMAND = "COMMAND";
    private final   String  EXPIRY_DATE = "EXPIRY_DATE";
    private final   String  INSERT = "INSERT";
    private final   String  INSURANCE_COMPANY = "INSURANCE_COMPANY";
    private final   String  NATURE_RISK = "NATURE_RISK";
    private final   String  OPTION = "OPTION";
    private final   String  POLICY_AMT = "POLICY_AMT";
    private final   String  POLICY_DATE = "POLICY_DATE";
    private final   String  POLICY_NO = "POLICY_NO";
    private final   String  PREMIUM_AMT = "PREMIUM_AMT";
//    private final   String  PRODUCT_ID = "PRODUCT_ID";
    private final   String  REMARK = "REMARK";
    private final   String  SECURITY_NO = "SECURITY_NO";
    private final   String  SLNO = "SLNO";
    private final   String  TABLE_VALUES = "TABLE_VALUES";
    private final   String  UPDATE = "UPDATE";
    
    private final   ArrayList insuranceTabTitle = new ArrayList();      //  Table Title of Insurance
    private ArrayList insuranceTabValues = new ArrayList();
    private ArrayList insuranceEachTabRecord;
    
    private EnhancedTableModel tblInsuranceTab;
    
    private HashMap insuranceEachRecord;
    
    private LinkedHashMap insuranceAll = new LinkedHashMap();          // Both displayed and hidden values in the table
    
    private ComboBoxModel cbmSecurityNo_Insurance;
    private ComboBoxModel cbmNatureRisk;
    
    private final   TermLoanRB objTermLoanRB = new TermLoanRB();
    
    private TableUtil tableUtilInsurance = new TableUtil();
    
    private String borrowerNo = "";
    private String txtInsureCompany = "";
    private String txtPolicyNumber = "";
    private String txtPolicyAmt = "";
    private String tdtPolicyDate = "";
    private String txtPremiumAmt = "";
    private String tdtExpityDate = "";
    private String cboNatureRisk = "";
    private String cboSecurityNo_Insurance = "";
    private String txtRemark_Insurance = "";
    private String strACNumber = "";
    private String strProd_ID = "";
    Date curDate = null;
    
    
    static {
        try {
            termLoanInsuranceOB = new TermLoanInsuranceOB();
        } catch(Exception e) {
            log.info("try: " + e);
            parseException.logException(e,true);
        }
    }
    
    private void termLoanInsuranceOB()  throws Exception{
        setInsuranceTabTitle();
        tableUtilInsurance.setAttributeKey(SLNO);
        tblInsuranceTab = new EnhancedTableModel(null, insuranceTabTitle);
    }
    
    public static TermLoanInsuranceOB getInstance() {
        return termLoanInsuranceOB;
    }
    
    private void setInsuranceTabTitle() throws Exception{
        try{
            insuranceTabTitle.add(objTermLoanRB.getString("tblColumnInsuranceNo"));
            insuranceTabTitle.add(objTermLoanRB.getString("tblColumnInsuranceSecrityNo"));
            insuranceTabTitle.add(objTermLoanRB.getString("tblColumnInsuranceCompany"));
        }catch(Exception e){
            log.info("Exception in setInsuranceTabTitle(): "+e);
            parseException.logException(e,true);
        }
    }
    
    public ArrayList getInsuranceTabTitle(){
        return this.insuranceTabTitle;
    }
    
    public void resetInsuranceCTable(){
        tblInsuranceTab.setDataArrayList(null, insuranceTabTitle);
        tableUtilInsurance = new TableUtil();
        tableUtilInsurance.setAttributeKey(SLNO);
    }
    
    public void resetInsuranceDetails(){
        setCboSecurityNo_Insurance("");
        setTxtInsureCompany("");
        setTxtPolicyNumber("");
        setTxtPolicyAmt("");
        setTdtPolicyDate("");
        setTxtPremiumAmt("");
        setTdtExpityDate("");
        setCboNatureRisk("");
        setTxtRemark_Insurance("");
    }
    
    public void changeStatusInsurance(int resultType){
        try{
            if (resultType != 2){
                //If the Main Save Button pressed
                tableUtilInsurance.getRemovedValues().clear();
            }
            java.util.Set keySet =  insuranceAll.keySet();
            Object[] objKeySet = (Object[]) keySet.toArray();
            HashMap oneRecord;
            // To change the Insert command to Update after Save Buttone Pressed
            // Insurance Details
            for (int i = keySet.size() - 1,j = 0;i >= 0;--i,++j){
                oneRecord = (HashMap) insuranceAll.get(objKeySet[j]);
                if (oneRecord.get(COMMAND).equals(INSERT)){
                    // If the status is in Insert Mode then change the mode to Update
                    oneRecord.put(COMMAND, UPDATE);
                    insuranceAll.put(objKeySet[j], oneRecord);
                }
                oneRecord = null;
            }
            keySet = null;
            objKeySet = null;
        }catch(Exception e){
            log.info("Exception in changeStatusInsurance(): "+e);
            parseException.logException(e,true);
        }
    }
    
    public void setTermLoanInsuranceTO(ArrayList insuranceList, String acctNum){
        try{
            TermLoanInsuranceTO termLoanInsuranceTO;
            HashMap insuranceRecordMap;
            ArrayList removedValues = new ArrayList();
            LinkedHashMap allInsuranceRecords = new LinkedHashMap();
            ArrayList insuranceRecordList;
            ArrayList tabInsuranceRecords = new ArrayList();
            
            // To retrieve the Insurance Details from the Serverside
            for (int i = insuranceList.size() - 1,j = 0;i >= 0;--i,++j){
                termLoanInsuranceTO = (TermLoanInsuranceTO) insuranceList.get(j);
                insuranceRecordMap = new HashMap();
                insuranceRecordList = new ArrayList();
                
                insuranceRecordList.add(CommonUtil.convertObjToStr(termLoanInsuranceTO.getSlno()));
                insuranceRecordList.add(CommonUtil.convertObjToStr(getCbmSecurityNo_Insurance().getDataForKey(termLoanInsuranceTO.getSecurityNo())));
                insuranceRecordList.add(CommonUtil.convertObjToStr(termLoanInsuranceTO.getInsuranceCompany()));

                tabInsuranceRecords.add(insuranceRecordList);
                
                insuranceRecordMap.put(SLNO, CommonUtil.convertObjToStr(termLoanInsuranceTO.getSlno()));
//                insuranceRecordMap.put(PRODUCT_ID, CommonUtil.convertObjToStr(termLoanInsuranceTO.getProdId()));
                insuranceRecordMap.put(SECURITY_NO, CommonUtil.convertObjToStr(termLoanInsuranceTO.getSecurityNo()));
                insuranceRecordMap.put(EXPIRY_DATE, DateUtil.getStringDate(termLoanInsuranceTO.getExpiryDt()));
                insuranceRecordMap.put(INSURANCE_COMPANY, CommonUtil.convertObjToStr(termLoanInsuranceTO.getInsuranceCompany()));
                insuranceRecordMap.put(POLICY_AMT, CommonUtil.convertObjToStr(termLoanInsuranceTO.getPolicyAmt()));
                insuranceRecordMap.put(POLICY_DATE, DateUtil.getStringDate(termLoanInsuranceTO.getPolicyDt()));
                insuranceRecordMap.put(POLICY_NO, CommonUtil.convertObjToStr(termLoanInsuranceTO.getPolicyNo()));
                insuranceRecordMap.put(PREMIUM_AMT, CommonUtil.convertObjToStr(termLoanInsuranceTO.getPremiumAmt()));
                insuranceRecordMap.put(ACCOUNT_NO, CommonUtil.convertObjToStr(termLoanInsuranceTO.getAcctNum()));
                insuranceRecordMap.put(NATURE_RISK, CommonUtil.convertObjToStr(termLoanInsuranceTO.getRiskNature()));
                insuranceRecordMap.put(REMARK, CommonUtil.convertObjToStr(termLoanInsuranceTO.getRemarks()));
                
                insuranceRecordMap.put(COMMAND, UPDATE);
                
                allInsuranceRecords.put(termLoanInsuranceTO.getSlno(), insuranceRecordMap);
                
                insuranceRecordList = null;
                insuranceRecordMap = null;
            }
            insuranceAll.clear();
            insuranceTabValues.clear();
            
            insuranceAll = allInsuranceRecords;
            insuranceTabValues = tabInsuranceRecords;
            
            tblInsuranceTab.setDataArrayList(insuranceTabValues, insuranceTabTitle);
            
            tableUtilInsurance.setRemovedValues(removedValues);
            tableUtilInsurance.setAllValues(insuranceAll);
            tableUtilInsurance.setTableValues(insuranceTabValues);
            setMax_Del_Insurance_No(acctNum);
            tabInsuranceRecords = null;
            allInsuranceRecords = null;
            removedValues = null;
        }catch(Exception e){
            log.info("Error in setTermLoanInsuranceTO()..."+e);
            parseException.logException(e,true);
        }
    }
    
    private void setMax_Del_Insurance_No(String acctNum){
        try{
            HashMap transactionMap = new HashMap();
            HashMap retrieve = new HashMap();
            transactionMap.put("acctNum", acctNum);
            List resultList = ClientUtil.executeQuery("getSelectTermLoanInsuranceMaxSLNO", transactionMap);
            if (resultList.size() > 0){
                retrieve = (HashMap) resultList.get(0);
                tableUtilInsurance.setStr_MAX_DEL_SL_NO(CommonUtil.convertObjToStr(retrieve.get("MAX_SL_NO")));
            }
            retrieve = null;
            transactionMap = null;
            resultList = null;
        }catch(Exception e){
            log.info("Error In setMax_Del_Insurance_No: "+e);
            parseException.logException(e,true);
        }
    }
    
    public HashMap setTermLoanInsurance(){
        HashMap insuranceMap = new HashMap();
        try{
            TermLoanInsuranceTO termLoanInsuranceTO;
            java.util.Set keySet =  insuranceAll.keySet();
            Object[] objKeySet = (Object[]) keySet.toArray();
            HashMap oneRecord;
            // To set the values for Insurance Transfer Object
            for (int i = keySet.size() - 1,j = 0;i >= 0;--i,++j){
                oneRecord = (HashMap) insuranceAll.get(objKeySet[j]);
                termLoanInsuranceTO = new TermLoanInsuranceTO();
                termLoanInsuranceTO.setAcctNum(getStrACNumber());
                termLoanInsuranceTO.setBorrowNo(getBorrowerNo());
                termLoanInsuranceTO.setSlno(CommonUtil.convertObjToStr(oneRecord.get(SLNO)));
                termLoanInsuranceTO.setSecurityNo(CommonUtil.convertObjToStr(oneRecord.get(SECURITY_NO)));
                termLoanInsuranceTO.setCommand(CommonUtil.convertObjToStr(oneRecord.get(COMMAND)));
                
//                termLoanInsuranceTO.setExpiryDt((Date)oneRecord.get(EXPIRY_DATE));
                termLoanInsuranceTO.setExpiryDt(DateUtil.getDateMMDDYYYY(CommonUtil.convertObjToStr(oneRecord.get(EXPIRY_DATE))));
                termLoanInsuranceTO.setInsuranceCompany(CommonUtil.convertObjToStr(oneRecord.get(INSURANCE_COMPANY)));
                termLoanInsuranceTO.setPolicyAmt(CommonUtil.convertObjToDouble(oneRecord.get(POLICY_AMT)));
                
//                termLoanInsuranceTO.setPolicyDt((Date)oneRecord.get(POLICY_DATE));
                termLoanInsuranceTO.setPolicyDt(DateUtil.getDateMMDDYYYY(CommonUtil.convertObjToStr(oneRecord.get(POLICY_DATE))));
                termLoanInsuranceTO.setPolicyNo(CommonUtil.convertObjToStr(oneRecord.get(POLICY_NO)));
                termLoanInsuranceTO.setPremiumAmt(CommonUtil.convertObjToDouble(oneRecord.get(PREMIUM_AMT)));
//                termLoanInsuranceTO.setProdId(CommonUtil.convertObjToStr(oneRecord.get(PRODUCT_ID)));
                termLoanInsuranceTO.setRiskNature(CommonUtil.convertObjToStr(oneRecord.get(NATURE_RISK)));
                termLoanInsuranceTO.setRemarks(CommonUtil.convertObjToStr(oneRecord.get(REMARK)));
                termLoanInsuranceTO.setStatusBy(TrueTransactMain.USER_ID);
                termLoanInsuranceTO.setStatusDt(curDate);
                if (oneRecord.get(COMMAND).equals(INSERT)){
                    termLoanInsuranceTO.setStatus(CommonConstants.STATUS_CREATED);
                }else if (oneRecord.get(COMMAND).equals(UPDATE)){
                    termLoanInsuranceTO.setStatus(CommonConstants.STATUS_MODIFIED);
                }
                insuranceMap.put(String.valueOf(j+1), termLoanInsuranceTO);
                
                oneRecord = null;
                termLoanInsuranceTO = null;
            }
            
            // To set the values for Insurance Transfer Object
            // where as the existing records in Database are deleted in client side
            // useful for updating the status
            for (int i = tableUtilInsurance.getRemovedValues().size() - 1,j = 0;i >= 0;--i,++j){
                oneRecord = (HashMap) tableUtilInsurance.getRemovedValues().get(j);
                termLoanInsuranceTO = new TermLoanInsuranceTO();
                termLoanInsuranceTO.setAcctNum(getStrACNumber());
                termLoanInsuranceTO.setBorrowNo(getBorrowerNo());
                termLoanInsuranceTO.setSlno(CommonUtil.convertObjToStr(oneRecord.get(SLNO)));
                termLoanInsuranceTO.setSecurityNo(CommonUtil.convertObjToStr(oneRecord.get(SECURITY_NO)));
                termLoanInsuranceTO.setCommand(CommonUtil.convertObjToStr(oneRecord.get(COMMAND)));
                
//                termLoanInsuranceTO.setExpiryDt((Date)oneRecord.get(EXPIRY_DATE));
                termLoanInsuranceTO.setExpiryDt(DateUtil.getDateMMDDYYYY(CommonUtil.convertObjToStr(oneRecord.get(EXPIRY_DATE))));
                termLoanInsuranceTO.setInsuranceCompany(CommonUtil.convertObjToStr(oneRecord.get(INSURANCE_COMPANY)));
                termLoanInsuranceTO.setPolicyAmt(CommonUtil.convertObjToDouble(oneRecord.get(POLICY_AMT)));
                
//                termLoanInsuranceTO.setPolicyDt((Date)oneRecord.get(POLICY_DATE));
                termLoanInsuranceTO.setPolicyDt(DateUtil.getDateMMDDYYYY(CommonUtil.convertObjToStr(oneRecord.get(POLICY_DATE))));
                termLoanInsuranceTO.setPolicyNo(CommonUtil.convertObjToStr(oneRecord.get(POLICY_NO)));
                termLoanInsuranceTO.setPremiumAmt(CommonUtil.convertObjToDouble(oneRecord.get(PREMIUM_AMT)));
//                termLoanInsuranceTO.setProdId(CommonUtil.convertObjToStr(oneRecord.get(PRODUCT_ID)));
                termLoanInsuranceTO.setRiskNature(CommonUtil.convertObjToStr(oneRecord.get(NATURE_RISK)));
                termLoanInsuranceTO.setRemarks(CommonUtil.convertObjToStr(oneRecord.get(REMARK)));
                termLoanInsuranceTO.setStatus(CommonConstants.STATUS_DELETED);
                termLoanInsuranceTO.setStatusBy(TrueTransactMain.USER_ID);
                termLoanInsuranceTO.setStatusDt(curDate);
                insuranceMap.put(String.valueOf(insuranceMap.size()+1), termLoanInsuranceTO);
                
                oneRecord = null;
                termLoanInsuranceTO = null;
            }
            
        }catch(Exception e){
            log.info("Error In setTermLoanInsurance() "+e);
            parseException.logException(e,true);
        }
        return insuranceMap;
    }
    
    void setTblInsuranceTab(EnhancedTableModel tblInsuranceTab){
        log.info("In setTblInsuranceTab()...");
        
        this.tblInsuranceTab = tblInsuranceTab;
        setChanged();
    }
    
    EnhancedTableModel getTblInsuranceTab(){
        return this.tblInsuranceTab;
    }
    
    public void setBorrowerNo(String borrowerNo){
        this.borrowerNo = borrowerNo;
        setChanged();
    }
    
    public String getBorrowerNo(){
        return this.borrowerNo;
    }
    
    void setCbmSecurityNo_Insurance(ComboBoxModel cbmSecurityNo_Insurance){
        this.cbmSecurityNo_Insurance = cbmSecurityNo_Insurance;
        setChanged();
    }
    
    ComboBoxModel getCbmSecurityNo_Insurance(){
        return this.cbmSecurityNo_Insurance;
    }
    
    void setCboSecurityNo_Insurance(String cboSecurityNo_Insurance){
        this.cboSecurityNo_Insurance = cboSecurityNo_Insurance;
        setChanged();
    }
    String getCboSecurityNo_Insurance(){
        return this.cboSecurityNo_Insurance;
    }
    
    void setTxtRemark_Insurance(String txtRemark_Insurance){
        this.txtRemark_Insurance = txtRemark_Insurance;
        setChanged();
    }
    String getTxtRemark_Insurance(){
        return this.txtRemark_Insurance;
    }
    
    void setTxtInsureCompany(String txtInsureCompany){
        this.txtInsureCompany = txtInsureCompany;
        setChanged();
    }
    String getTxtInsureCompany(){
        return this.txtInsureCompany;
    }
    
    void setTxtPolicyNumber(String txtPolicyNumber){
        this.txtPolicyNumber = txtPolicyNumber;
        setChanged();
    }
    String getTxtPolicyNumber(){
        return this.txtPolicyNumber;
    }
    
    void setTxtPolicyAmt(String txtPolicyAmt){
        this.txtPolicyAmt = txtPolicyAmt;
        setChanged();
    }
    String getTxtPolicyAmt(){
        return this.txtPolicyAmt;
    }
    
    void setTdtPolicyDate(String tdtPolicyDate){
        this.tdtPolicyDate = tdtPolicyDate;
        setChanged();
    }
    String getTdtPolicyDate(){
        return this.tdtPolicyDate;
    }
    
    void setTxtPremiumAmt(String txtPremiumAmt){
        this.txtPremiumAmt = txtPremiumAmt;
        setChanged();
    }
    String getTxtPremiumAmt(){
        return this.txtPremiumAmt;
    }
    
    void setTdtExpityDate(String tdtExpityDate){
        this.tdtExpityDate = tdtExpityDate;
        setChanged();
    }
    String getTdtExpityDate(){
        return this.tdtExpityDate;
    }
    
    void setCbmNatureRisk(ComboBoxModel cbmNatureRisk){
        this.cbmNatureRisk = cbmNatureRisk;
        setChanged();
    }
    ComboBoxModel getCbmNatureRisk(){
        return this.cbmNatureRisk;
    }
    
    void setCboNatureRisk(String cboNatureRisk){
        this.cboNatureRisk = cboNatureRisk;
        setChanged();
    }
    String getCboNatureRisk(){
        return this.cboNatureRisk;
    }
    
    public void setStrACNumber(String strACNumber){
        this.strACNumber = strACNumber;
        setChanged();
    }
    
    public String getStrACNumber(){
        return this.strACNumber;
    }
    
    public void setStrProd_ID(String strProd_ID){
        this.strProd_ID = strProd_ID;
        setChanged();
    }
    String getStrProd_ID(){
        return this.strProd_ID;
    }
    
    public void ttNotifyObservers(){
        notifyObservers();
    }
    
    public int addInsuranceDetails(int recordPosition, boolean update){
        int option = -1;
        try{
            log.info("Add Insurance Details...");
            insuranceEachTabRecord = new ArrayList();
            insuranceEachRecord = new HashMap();
            HashMap result = new HashMap();
            ArrayList data = tblInsuranceTab.getDataArrayList();
            tblInsuranceTab.setDataArrayList(data, insuranceTabTitle);
            final int dataSize = data.size();
            insertInsurance(dataSize+1);
            if (!update) {
                // If the table is not in Edit Mode
                result = tableUtilInsurance.insertTableValues(insuranceEachTabRecord, insuranceEachRecord);
                
                insuranceTabValues = (ArrayList) result.get(TABLE_VALUES);
                insuranceAll = (LinkedHashMap) result.get(ALL_VALUES);
                option = CommonUtil.convertObjToInt(result.get(OPTION));
                
                tblInsuranceTab.setDataArrayList(insuranceTabValues, insuranceTabTitle);
            }else{
                option = updateInsuranceTab(recordPosition);
            }
            
            setChanged();
            
            insuranceEachTabRecord = null;
            insuranceEachRecord = null;
            result = null;
            data = null;
        }catch(Exception e){
            log.info("Error in addInsuranceDetails..."+e);
            parseException.logException(e,true);
        }
        return option;
    }
    
    private void insertInsurance(int recordPosition){
        insuranceEachTabRecord.add(String.valueOf(recordPosition));
        insuranceEachTabRecord.add(getCboSecurityNo_Insurance());
        insuranceEachTabRecord.add(getTxtInsureCompany());
        
        insuranceEachRecord.put(SLNO, String.valueOf(recordPosition));
//        insuranceEachRecord.put(PRODUCT_ID, getStrProd_ID());
        insuranceEachRecord.put(SECURITY_NO, CommonUtil.convertObjToStr(getCbmSecurityNo_Insurance().getKeyForSelected()));
        insuranceEachRecord.put(EXPIRY_DATE, getTdtExpityDate());
        insuranceEachRecord.put(INSURANCE_COMPANY, getTxtInsureCompany());
        insuranceEachRecord.put(POLICY_AMT, getTxtPolicyAmt());
        insuranceEachRecord.put(POLICY_DATE, getTdtPolicyDate());
        insuranceEachRecord.put(POLICY_NO, getTxtPolicyNumber());
        insuranceEachRecord.put(PREMIUM_AMT, getTxtPremiumAmt());
        insuranceEachRecord.put(ACCOUNT_NO, getStrACNumber());
        insuranceEachRecord.put(NATURE_RISK, CommonUtil.convertObjToStr(getCbmNatureRisk().getKeyForSelected()));
        insuranceEachRecord.put(REMARK, getTxtRemark_Insurance());
        insuranceEachRecord.put(COMMAND, "");
    }
    
    private int updateInsuranceTab(int recordPosition){
        int option = -1;
        HashMap result = new HashMap();
        try{
            result = tableUtilInsurance.updateTableValues(insuranceEachTabRecord, insuranceEachRecord, recordPosition);
            
            insuranceTabValues = (ArrayList) result.get(TABLE_VALUES);
            insuranceAll = (LinkedHashMap) result.get(ALL_VALUES);
            option = CommonUtil.convertObjToInt(result.get(OPTION));
            
            tblInsuranceTab.setDataArrayList(insuranceTabValues, insuranceTabTitle);
            
        }catch(Exception e){
            log.info("Error in updateInsuranceTab..."+e);
            parseException.logException(e,true);
        }
        result = null;
        return option;
    }
    
    public void populateInsuranceDetails(int recordPosition){
        try{
            HashMap eachRecs;
            java.util.Set keySet =  insuranceAll.keySet();
            Object[] objKeySet = (Object[]) keySet.toArray();
            String strRecordKey = (String) ((ArrayList) (tblInsuranceTab.getDataArrayList().get(recordPosition))).get(0);
            
            // To populate the corresponding record from the Insurance Details
            for (int i = keySet.size() - 1, j = 0;i >= 0;--i, ++j){
                if (((String) ((HashMap) insuranceAll.get(objKeySet[j])).get(SLNO)).equals(strRecordKey)){
                    // To populate the Corresponding record from CTable
                    eachRecs = (HashMap) insuranceAll.get(objKeySet[j]);
                    
                    setCboSecurityNo_Insurance(CommonUtil.convertObjToStr(getCbmSecurityNo_Insurance().getDataForKey(eachRecs.get(SECURITY_NO))));
                    setTxtInsureCompany(CommonUtil.convertObjToStr(eachRecs.get(INSURANCE_COMPANY)));
                    setTdtPolicyDate(CommonUtil.convertObjToStr(eachRecs.get(POLICY_DATE)));
                    setTxtPolicyNumber(CommonUtil.convertObjToStr(eachRecs.get(POLICY_NO)));
                    setTxtPolicyAmt(CommonUtil.convertObjToStr(eachRecs.get(POLICY_AMT)));
                    setTxtPremiumAmt(CommonUtil.convertObjToStr(eachRecs.get(PREMIUM_AMT)));
                    setTdtExpityDate(CommonUtil.convertObjToStr(eachRecs.get(EXPIRY_DATE)));
                    setCboNatureRisk(CommonUtil.convertObjToStr(getCbmNatureRisk().getDataForKey(eachRecs.get(NATURE_RISK))));
                    setTxtRemark_Insurance(CommonUtil.convertObjToStr(eachRecs.get(REMARK)));
                    
                    break;
                }
                eachRecs = null;
            }
            keySet = null;
            
            objKeySet = null;
            setChanged();
            ttNotifyObservers();
        }catch(Exception e){
            log.info("Error in populateInsuranceDetails..."+e);
            parseException.logException(e,true);
        }
    }
    
    public void deleteInsuranceTabRecord(int recordPosition){
        HashMap result = new HashMap();
        try{
            result = tableUtilInsurance.deleteTableValues(recordPosition);
            
            insuranceTabValues = (ArrayList) result.get(TABLE_VALUES);
            insuranceAll = (LinkedHashMap) result.get(ALL_VALUES);
            
            tblInsuranceTab.setDataArrayList(insuranceTabValues, insuranceTabTitle);
            
        }catch(Exception e){
            log.info("Error in deleteInsuranceTabRecord..."+e);
            parseException.logException(e,true);
        }
        result = null;
    }
    
    // To create objects
    public void createObject(){
        insuranceTabValues = new ArrayList();
        tblInsuranceTab.setDataArrayList(null, insuranceTabTitle);
        insuranceAll = new LinkedHashMap();
        tableUtilInsurance = new TableUtil();
        tableUtilInsurance.setAttributeKey(SLNO);
    }
    
    // To destroy Objects
    public void destroyObjects(){
        insuranceTabValues = null;
        insuranceAll = null;
        tableUtilInsurance = null;
    }
}
