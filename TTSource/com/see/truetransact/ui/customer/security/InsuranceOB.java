/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition. 
 * 
 * InsuranceOB.java
 *
 * Created on January 13, 2005, 3:06 PM
 */

package com.see.truetransact.ui.customer.security;

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
import com.see.truetransact.transferobject.customer.security.InsuranceTO;
import com.see.truetransact.ui.TrueTransactMain;
import com.see.truetransact.uicomponent.CObservable;
import com.see.truetransact.uicomponent.COptionPane;

import org.apache.log4j.Logger;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Date;


public class InsuranceOB extends CObservable{
    
    /** Creates a new instance of InsuranceOB */
    private InsuranceOB() throws Exception{
        insuranceOB();
    }
    
    private       static InsuranceOB insuranceOB;
    
    private final static Logger log = Logger.getLogger(InsuranceOB.class);
    private final static ClientParseException parseException = ClientParseException.getInstance();
    
    private final   String  ALL_VALUES = "ALL_VALUES";
    private final   String  COMMAND = "COMMAND";
    private final   String  CUSTOMER_ID = "CUSTOMER_ID";
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
    
    private ArrayList securityNoSelectedNInsuranceLevel;
    private EnhancedTableModel tblInsuranceTab;
    
    private HashMap insuranceEachRecord;
    
    private LinkedHashMap insuranceAll = new LinkedHashMap();          // Both displayed and hidden values in the table
    
    private ComboBoxModel cbmSecurityNo_Insurance;
    private ComboBoxModel cbmNatureRisk;
    
    private final   SecurityInsuranceRB objSecurityInsuranceRB = new SecurityInsuranceRB();
    
    private TableUtil tableUtilInsurance = new TableUtil();
    
    private String strStoredSecurityNo = "";
    private String txtInsuranceNo = "";
    private String txtInsureCompany = "";
    private String txtPolicyNumber = "";
    private String txtPolicyAmt = "";
    private String tdtPolicyDate = "";
    private String txtPremiumAmt = "";
    private String tdtExpityDate = "";
    private String cboNatureRisk = "";
    private String cboSecurityNo_Insurance = "";
    private String txtRemark_Insurance = "";
    private String lblCustID_Disp = "";
    private String lblCustName_Disp = "";
    private String lblCustEmail_ID_Disp = "";
    private String lblCustCity_Disp = "";
    private String lblCustStreet_Disp = "";
    private String lblCustPin_Disp = "";
    Date curDate = null;
    
    
    static {
        try {
            insuranceOB = new InsuranceOB();
        } catch(Exception e) {
            log.info("try: " + e);
            parseException.logException(e,true);
        }
    }
    
    private void insuranceOB()  throws Exception{
        curDate = ClientUtil.getCurrentDate();
        setInsuranceTabTitle();
        securityNoSelectedNInsuranceLevel = new ArrayList();
        tableUtilInsurance.setAttributeKey(SLNO);
        tblInsuranceTab = new EnhancedTableModel(null, insuranceTabTitle);
    }
    
    public static InsuranceOB getInstance() {
        return insuranceOB;
    }
    
    private void setInsuranceTabTitle() throws Exception{
        try{
            insuranceTabTitle.add(objSecurityInsuranceRB.getString("tblColumnInsuranceNo"));
            insuranceTabTitle.add(objSecurityInsuranceRB.getString("tblColumnInsuranceSecrityNo"));
            insuranceTabTitle.add(objSecurityInsuranceRB.getString("tblColumnInsuranceCompany"));
        }catch(Exception e){
            log.info("Exception in setInsuranceTabTitle(): "+e);
            parseException.logException(e,true);
        }
    }
    
    public ArrayList getInsuranceTabTitle(){
        return this.insuranceTabTitle;
    }
    
    public void resetAllInsuranceDetails(){
        removeAllSecurityNo();
        resetInsuranceCTable();
        resetInsuranceDetails();
    }
    
    public void resetsecurityNoSelectedNInsuranceLevelList(){
        securityNoSelectedNInsuranceLevel = null;
        securityNoSelectedNInsuranceLevel = new ArrayList();
    }
    
    public void resetInsuranceCTable(){
        tblInsuranceTab.setDataArrayList(null, insuranceTabTitle);
        tableUtilInsurance = new TableUtil();
        tableUtilInsurance.setAttributeKey(SLNO);
    }
    
    public void resetInsuranceDetails(){
        setCboSecurityNo_Insurance("");
        setStrStoredSecurityNo("");
        setTxtInsureCompany("");
        setTxtPolicyNumber("");
        setTxtPolicyAmt("");
        setTdtPolicyDate("");
        setTxtPremiumAmt("");
        setTdtExpityDate("");
        setCboNatureRisk("");
        setTxtRemark_Insurance("");
        setTxtInsuranceNo("");
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
    
    public void setInsuranceTO(ArrayList insuranceList){
        try{
            InsuranceTO insuranceTO;
            HashMap insuranceRecordMap;
            String strCustID = "";
            ArrayList removedValues = new ArrayList();
            LinkedHashMap allInsuranceRecords = new LinkedHashMap();
            ArrayList insuranceRecordList;
            ArrayList tabInsuranceRecords = new ArrayList();
            SecurityOB objSecurityOB = SecurityOB.getInstance();
            String strInsuranceSlNo;
            
            // To retrieve the Insurance Details from the Serverside
            for (int i = insuranceList.size() - 1,j = 0;i >= 0;--i,++j){
                insuranceTO = (InsuranceTO) insuranceList.get(j);
                insuranceRecordMap = new HashMap();
                insuranceRecordList = new ArrayList();
                
                insuranceRecordList.add(CommonUtil.convertObjToStr(insuranceTO.getSlno()));
                insuranceRecordList.add(CommonUtil.convertObjToStr(getCbmSecurityNo_Insurance().getDataForKey(CommonUtil.convertObjToStr(insuranceTO.getSecurityNo()))));
                insuranceRecordList.add(CommonUtil.convertObjToStr(insuranceTO.getInsuranceCompany()));

                tabInsuranceRecords.add(insuranceRecordList);
                
                strCustID = insuranceTO.getCustId();
                
                insuranceRecordMap.put(SLNO, CommonUtil.convertObjToStr(insuranceTO.getSlno()));
                
                strInsuranceSlNo = CommonUtil.convertObjToStr(insuranceTO.getSlno());
                objSecurityOB.getCbmInsuranceNo().addKeyAndElement(strInsuranceSlNo, strInsuranceSlNo);
                
                if (CommonUtil.convertObjToStr(insuranceTO.getSecurityNo()).length() > 0 && !(CommonUtil.convertObjToStr(insuranceTO.getSecurityNo()).equals("0"))){
                    getSecurityNoSelectedNInsuranceLevel().add(CommonUtil.convertObjToStr(insuranceTO.getSecurityNo()));
                    insuranceRecordMap.put(SECURITY_NO, CommonUtil.convertObjToStr(insuranceTO.getSecurityNo()));
                }else{
                    insuranceRecordMap.put(SECURITY_NO, "");
                }
//                insuranceRecordMap.put(PRODUCT_ID, CommonUtil.convertObjToStr(insuranceTO.getProdId()));
                insuranceRecordMap.put(EXPIRY_DATE, DateUtil.getStringDate(insuranceTO.getExpiryDt()));
                insuranceRecordMap.put(INSURANCE_COMPANY, CommonUtil.convertObjToStr(insuranceTO.getInsuranceCompany()));
                insuranceRecordMap.put(POLICY_AMT, CommonUtil.convertObjToStr(insuranceTO.getPolicyAmt()));
                insuranceRecordMap.put(POLICY_DATE, DateUtil.getStringDate(insuranceTO.getPolicyDt()));
                insuranceRecordMap.put(POLICY_NO, CommonUtil.convertObjToStr(insuranceTO.getPolicyNo()));
                insuranceRecordMap.put(PREMIUM_AMT, CommonUtil.convertObjToStr(insuranceTO.getPremiumAmt()));
                insuranceRecordMap.put(CUSTOMER_ID, CommonUtil.convertObjToStr(insuranceTO.getCustId()));
                insuranceRecordMap.put(NATURE_RISK, CommonUtil.convertObjToStr(insuranceTO.getRiskNature()));
                insuranceRecordMap.put(REMARK, CommonUtil.convertObjToStr(insuranceTO.getRemarks()));
                
                insuranceRecordMap.put(COMMAND, UPDATE);
                
                allInsuranceRecords.put(CommonUtil.convertObjToStr(insuranceTO.getSlno()), insuranceRecordMap);
                
                insuranceRecordList = null;
                insuranceRecordMap = null;
                strInsuranceSlNo = null;
            }
            insuranceAll.clear();
            insuranceTabValues.clear();
            
            insuranceAll = allInsuranceRecords;
            insuranceTabValues = tabInsuranceRecords;
            
            tblInsuranceTab.setDataArrayList(insuranceTabValues, insuranceTabTitle);
            
            tableUtilInsurance.setRemovedValues(removedValues);
            tableUtilInsurance.setAllValues(insuranceAll);
            tableUtilInsurance.setTableValues(insuranceTabValues);
            setMax_Del_Insurance_No(strCustID);
            tabInsuranceRecords = null;
            allInsuranceRecords = null;
            removedValues = null;
            objSecurityOB = null;
        }catch(Exception e){
            log.info("Error in setInsuranceTO()..."+e);
            parseException.logException(e,true);
        }
    }
    
    private void setMax_Del_Insurance_No(String strCustID){
        try{
            HashMap transactionMap = new HashMap();
            HashMap retrieve = new HashMap();
            transactionMap.put("custId", strCustID);
            List resultList = ClientUtil.executeQuery("getSelectInsuranceMaxSLNO", transactionMap);
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
    
    public HashMap setInsurance(String strCommand){
        HashMap insuranceMap = new HashMap();
        try{
            InsuranceTO insuranceTO;
            java.util.Set keySet =  insuranceAll.keySet();
            Object[] objKeySet = (Object[]) keySet.toArray();
            HashMap oneRecord;
            // To set the values for Insurance Transfer Object
            for (int i = keySet.size() - 1,j = 0;i >= 0;--i,++j){
                oneRecord = (HashMap) insuranceAll.get(objKeySet[j]);
                insuranceTO = new InsuranceTO();
                
                insuranceTO.setCustId(CommonUtil.convertObjToStr(oneRecord.get(CUSTOMER_ID)));
                insuranceTO.setSlno(CommonUtil.convertObjToDouble(oneRecord.get(SLNO)));
                insuranceTO.setSecurityNo(CommonUtil.convertObjToDouble(oneRecord.get(SECURITY_NO)));
                Date IsDt = DateUtil.getDateMMDDYYYY(CommonUtil.convertObjToStr(oneRecord.get(EXPIRY_DATE)));
                if(IsDt != null){
                Date isDate = (Date)curDate.clone();
                isDate.setDate(IsDt.getDate());
                isDate.setMonth(IsDt.getMonth());
                isDate.setYear(IsDt.getYear());
                insuranceTO.setExpiryDt(isDate);
                }else{
                    insuranceTO.setExpiryDt(DateUtil.getDateMMDDYYYY(CommonUtil.convertObjToStr(oneRecord.get(EXPIRY_DATE))));
                }
//                insuranceTO.setExpiryDt(DateUtil.getDateMMDDYYYY(CommonUtil.convertObjToStr(oneRecord.get(EXPIRY_DATE))));
                
                insuranceTO.setInsuranceCompany(CommonUtil.convertObjToStr(oneRecord.get(INSURANCE_COMPANY)));
                insuranceTO.setPolicyAmt(CommonUtil.convertObjToDouble(oneRecord.get(POLICY_AMT)));
                
                Date polDt = DateUtil.getDateMMDDYYYY(CommonUtil.convertObjToStr(oneRecord.get(POLICY_DATE)));
                  if(polDt != null){
                Date polDate = (Date)curDate.clone();
                polDate.setDate(polDt.getDate());
                polDate.setMonth(polDt.getMonth());
                polDate.setYear(polDt.getYear());
                insuranceTO.setPolicyDt(polDate);
                }else{
                    insuranceTO.setPolicyDt(DateUtil.getDateMMDDYYYY(CommonUtil.convertObjToStr(oneRecord.get(POLICY_DATE))));
                }
//                insuranceTO.setPolicyDt((Date)oneRecord.get(POLICY_DATE));
//                insuranceTO.setPolicyDt(DateUtil.getDateMMDDYYYY(CommonUtil.convertObjToStr(oneRecord.get(POLICY_DATE))));
                
                insuranceTO.setPolicyNo(CommonUtil.convertObjToStr(oneRecord.get(POLICY_NO)));
                insuranceTO.setPremiumAmt(CommonUtil.convertObjToDouble(oneRecord.get(PREMIUM_AMT)));
//                insuranceTO.setProdId(CommonUtil.convertObjToStr(oneRecord.get(PRODUCT_ID)));
                insuranceTO.setRiskNature(CommonUtil.convertObjToStr(oneRecord.get(NATURE_RISK)));
                insuranceTO.setRemarks(CommonUtil.convertObjToStr(oneRecord.get(REMARK)));
                insuranceTO.setStatusBy(TrueTransactMain.USER_ID);
                insuranceTO.setBranchCode(getSelectedBranchID());
                insuranceTO.setStatusDt(curDate);
                if (oneRecord.get(COMMAND).equals(INSERT)){
                    insuranceTO.setStatus(CommonConstants.STATUS_CREATED);
                }else if (oneRecord.get(COMMAND).equals(UPDATE)){
                    insuranceTO.setStatus(CommonConstants.STATUS_MODIFIED);
                }
                if (strCommand.equals(CommonConstants.TOSTATUS_DELETE)){
                    insuranceTO.setStatus(CommonConstants.STATUS_DELETED);
                    insuranceTO.setCommand(CommonConstants.TOSTATUS_DELETE);
                }else{
                    insuranceTO.setCommand(CommonUtil.convertObjToStr(oneRecord.get(COMMAND)));
                }
                insuranceMap.put(String.valueOf(j+1), insuranceTO);
                
                oneRecord = null;
                insuranceTO = null;
            }
            
            // To set the values for Insurance Transfer Object
            // where as the existing records in Database are deleted in client side
            // useful for updating the status
            for (int i = tableUtilInsurance.getRemovedValues().size() - 1,j = 0;i >= 0;--i,++j){
                oneRecord = (HashMap) tableUtilInsurance.getRemovedValues().get(j);
                insuranceTO = new InsuranceTO();
                
                insuranceTO.setCustId(CommonUtil.convertObjToStr(oneRecord.get(CUSTOMER_ID)));
                insuranceTO.setSlno(CommonUtil.convertObjToDouble(oneRecord.get(SLNO)));
                insuranceTO.setSecurityNo(CommonUtil.convertObjToDouble(oneRecord.get(SECURITY_NO)));
                insuranceTO.setCommand(CommonUtil.convertObjToStr(oneRecord.get(COMMAND)));
                
                Date ExpDt = DateUtil.getDateMMDDYYYY(CommonUtil.convertObjToStr(oneRecord.get(EXPIRY_DATE)));
                if(ExpDt != null){
                Date expDt = (Date)curDate.clone();
                expDt.setDate(ExpDt.getDate());
                expDt.setMonth(ExpDt.getMonth());
                expDt.setYear(ExpDt.getYear());
                insuranceTO.setExpiryDt(expDt);
                }else{
                    insuranceTO.setExpiryDt(DateUtil.getDateMMDDYYYY(CommonUtil.convertObjToStr(oneRecord.get(EXPIRY_DATE))));
                }
//                insuranceTO.setExpiryDt((Date)oneRecord.get(EXPIRY_DATE));
//                insuranceTO.setExpiryDt(DateUtil.getDateMMDDYYYY(CommonUtil.convertObjToStr(oneRecord.get(EXPIRY_DATE))));
                
                insuranceTO.setInsuranceCompany(CommonUtil.convertObjToStr(oneRecord.get(INSURANCE_COMPANY)));
                insuranceTO.setPolicyAmt(CommonUtil.convertObjToDouble(oneRecord.get(POLICY_AMT)));
                
                 Date PoDt = DateUtil.getDateMMDDYYYY(CommonUtil.convertObjToStr(oneRecord.get(POLICY_DATE)));
                if(PoDt != null){
                Date poDt = (Date)curDate.clone();
                poDt.setDate(PoDt.getDate());
                poDt.setMonth(PoDt.getMonth());
                poDt.setYear(PoDt.getYear());
                insuranceTO.setPolicyDt(poDt);
                }else{
                    insuranceTO.setPolicyDt(DateUtil.getDateMMDDYYYY(CommonUtil.convertObjToStr(oneRecord.get(POLICY_DATE))));
                }
//                insuranceTO.setPolicyDt((Date)oneRecord.get(POLICY_DATE));
//                insuranceTO.setPolicyDt(DateUtil.getDateMMDDYYYY(CommonUtil.convertObjToStr(oneRecord.get(POLICY_DATE))));
                
                insuranceTO.setPolicyNo(CommonUtil.convertObjToStr(oneRecord.get(POLICY_NO)));
                insuranceTO.setPremiumAmt(CommonUtil.convertObjToDouble(oneRecord.get(PREMIUM_AMT)));
//                insuranceTO.setProdId(CommonUtil.convertObjToStr(oneRecord.get(PRODUCT_ID)));
                insuranceTO.setRiskNature(CommonUtil.convertObjToStr(oneRecord.get(NATURE_RISK)));
                insuranceTO.setRemarks(CommonUtil.convertObjToStr(oneRecord.get(REMARK)));
                insuranceTO.setStatus(CommonConstants.STATUS_DELETED);
                insuranceTO.setStatusBy(TrueTransactMain.USER_ID);
                insuranceTO.setBranchCode(getSelectedBranchID());
                insuranceTO.setStatusDt(curDate);
                insuranceMap.put(String.valueOf(insuranceMap.size()+1), insuranceTO);
                
                oneRecord = null;
                insuranceTO = null;
            }
        }catch(Exception e){
            log.info("Error In setInsurance() "+e);
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
    }
    
    public void ttNotifyObservers(){
        notifyObservers();
    }
    
    public void removeAllSecurityNo(){
        try{
            // Remove all keys and values before add
            for (int i = getCbmSecurityNo_Insurance().getSize() - 1;i >= 0;--i){
                getCbmSecurityNo_Insurance().removeKeyAndElement(getCbmSecurityNo_Insurance().getKey(i));
            }
            // To add the Sanction Nos. in ComboBoxModel in Insurance Details
            getCbmSecurityNo_Insurance().addKeyAndElement("", "");
            resetsecurityNoSelectedNInsuranceLevelList();
        }catch(Exception e){
            log.info("Error in removeAllSecurityNo..."+e);
            parseException.logException(e,true);
        }
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
            String strSecNoSelectedKey = CommonUtil.convertObjToStr(getCbmSecurityNo_Insurance().getKeyForSelected());
            if (!update) {
                // If the table is not in Edit Mode
                result = tableUtilInsurance.insertTableValues(insuranceEachTabRecord, insuranceEachRecord);
                
                insuranceTabValues = (ArrayList) result.get(TABLE_VALUES);
                insuranceAll = (LinkedHashMap) result.get(ALL_VALUES);
                option = CommonUtil.convertObjToInt(result.get(OPTION));
                
                tblInsuranceTab.setDataArrayList(insuranceTabValues, insuranceTabTitle);
                addInsuranceNoInSecurityComboBox();
                if (strSecNoSelectedKey.length() > 0){
                    getSecurityNoSelectedNInsuranceLevel().add(strSecNoSelectedKey);
                }
            }else{
                option = updateInsuranceTab(recordPosition);
                
                if (strSecNoSelectedKey.length() > 0 && !(getStrStoredSecurityNo().equals(strSecNoSelectedKey))){
                    // If the old value(Security No) is not equal to the selected one then
                    // delete the old value and add the new value
                    getSecurityNoSelectedNInsuranceLevel().remove(getStrStoredSecurityNo());
                    getSecurityNoSelectedNInsuranceLevel().add(strSecNoSelectedKey);
                }else if ((getStrStoredSecurityNo().length() > 0 && strSecNoSelectedKey.length() == 0)){
                    // The Security No is selected in the original record
                    // and if it is not selected now then remove the value from the List
                    getSecurityNoSelectedNInsuranceLevel().remove(getStrStoredSecurityNo());
                }else if ((getStrStoredSecurityNo().length() == 0 && strSecNoSelectedKey.length() > 0)){
                    // The Security No is not selected in the original record
                    // and if it is selected now then add the value from the List
                    getSecurityNoSelectedNInsuranceLevel().add(strSecNoSelectedKey);
                }
            }
            
            setChanged();
            
            strSecNoSelectedKey = null;
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
    
    private void addInsuranceNoInSecurityComboBox(){
        SecurityOB objSecurityOB = SecurityOB.getInstance();
        String strInsuranceSlNo = CommonUtil.convertObjToStr(((ArrayList)insuranceTabValues.get(insuranceTabValues.size() - 1)).get(0));
        objSecurityOB.getCbmInsuranceNo().addKeyAndElement(strInsuranceSlNo, strInsuranceSlNo);
        objSecurityOB = null;
        strInsuranceSlNo = null;
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
        insuranceEachRecord.put(CUSTOMER_ID, getLblCustID_Disp());
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
                    
                    setTxtInsuranceNo(CommonUtil.convertObjToStr(eachRecs.get(SLNO)));
                    setCboSecurityNo_Insurance(CommonUtil.convertObjToStr(getCbmSecurityNo_Insurance().getDataForKey(eachRecs.get(SECURITY_NO))));
                    setStrStoredSecurityNo(CommonUtil.convertObjToStr(eachRecs.get(SECURITY_NO)));
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
            deleteInsuranceNoInSecurityComboBox(recordPosition);
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
    
    private void deleteInsuranceNoInSecurityComboBox(int recordPosition){
        String strRecordKey = CommonUtil.convertObjToStr(((ArrayList) insuranceTabValues.get(recordPosition)).get(0));
        SecurityOB objSecurityOB = SecurityOB.getInstance();
        if (objSecurityOB.getCbmInsuranceNo().containsElement(strRecordKey)){
            objSecurityOB.getCbmInsuranceNo().removeKeyAndElement(strRecordKey);
        }
        if (CommonUtil.convertObjToStr(getCbmSecurityNo_Insurance().getKeyForSelected()).length() > 0){
                    getSecurityNoSelectedNInsuranceLevel().remove(CommonUtil.convertObjToStr(getCbmSecurityNo_Insurance().getKeyForSelected()));
        }
        objSecurityOB = null;
        strRecordKey = null;
    }
    
    public String securityExistWarning(int selectedRow, String strSecurityNo){
        // To check whether the Insurance No. has Security Details or not
        StringBuffer stbWarnMsg = new StringBuffer("");
        try{
            ArrayList insuranceData = tblInsuranceTab.getDataArrayList();
            String insuranceNo = CommonUtil.convertObjToStr(((ArrayList) insuranceData.get(selectedRow)).get(0));
            ArrayList securityData = SecurityOB.getInstance().getInsuranceNoSelectedNSecurityLevel();
            
            if (securityData.contains(insuranceNo) || strSecurityNo.length() > 0){
                // This Insurance No. has Security Details
                stbWarnMsg.append("\n");
                stbWarnMsg.append(objSecurityInsuranceRB.getString("lblInsuranceNo_Display"));
                stbWarnMsg.append(insuranceNo);
                stbWarnMsg.append(objSecurityInsuranceRB.getString("hasSecurityWarning"));
            }
        }catch(Exception e){
            log.info("Exception caught in securityExistWarning: "+e);
            parseException.logException(e,true);
        }
        return stbWarnMsg.toString();
    }
    
    public String isSecurityNoSelectedInInsuranceDetails(String strSecNo, Object objInsuNoSelected){
        StringBuffer stbWarnMsg = new StringBuffer("");
        try{
            String strInsuNoSelected = CommonUtil.convertObjToStr(objInsuNoSelected);
            if (strInsuNoSelected.length() > 0){
                HashMap eachRecs;
                java.util.Set keySet =  insuranceAll.keySet();
                Object[] objKeySet = (Object[]) keySet.toArray();
                // To get the corresponding record from the Insurance Details
                for (int i = keySet.size() - 1, j = 0;i >= 0;--i, ++j){
                    if ((CommonUtil.convertObjToStr(((HashMap) insuranceAll.get(objKeySet[j])).get(SLNO))).equals(strInsuNoSelected)){
                        eachRecs = (HashMap) insuranceAll.get(objKeySet[j]);
                        if (CommonUtil.convertObjToStr(eachRecs.get(SECURITY_NO)).length() > 0){
                            stbWarnMsg.append("\n");
                            stbWarnMsg.append(objSecurityInsuranceRB.getString("lblInsuranceNo_Disp"));
                            stbWarnMsg.append(strInsuNoSelected);
                            stbWarnMsg.append(objSecurityInsuranceRB.getString("insuranceNoSelectedWarning"));
                            break;
                        }
                        eachRecs = null;
                    }
                }
                keySet = null;
                objKeySet = null;
            }
            if (strSecNo.length() > 0 && stbWarnMsg.length() <= 0 && getSecurityNoSelectedNInsuranceLevel().contains(strSecNo)){
                stbWarnMsg.append("\n");
                stbWarnMsg.append(objSecurityInsuranceRB.getString("lblSecurityNo_Insurance"));
                stbWarnMsg.append(strSecNo);
                stbWarnMsg.append(objSecurityInsuranceRB.getString("securityNoSelectedWarning"));
            }
        }catch(Exception e){
            log.info("Exception caught in isSecurityNoSelectedInInsuranceDetails: "+e);
            parseException.logException(e, true);
        }
        return stbWarnMsg.toString();
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
    
    /**
     * Getter for property txtInsuranceNo.
     * @return Value of property txtInsuranceNo.
     */
    public java.lang.String getTxtInsuranceNo() {
        return txtInsuranceNo;
    }
    
    /**
     * Setter for property txtInsuranceNo.
     * @param txtInsuranceNo New value of property txtInsuranceNo.
     */
    public void setTxtInsuranceNo(java.lang.String txtInsuranceNo) {
        this.txtInsuranceNo = txtInsuranceNo;
    }
    
    /**
     * Getter for property securityNoSelectedNInsuranceLevel.
     * @return Value of property securityNoSelectedNInsuranceLevel.
     */
    public java.util.ArrayList getSecurityNoSelectedNInsuranceLevel() {
        return securityNoSelectedNInsuranceLevel;
    }
    
    /**
     * Setter for property securityNoSelectedNInsuranceLevel.
     * @param securityNoSelectedNInsuranceLevel New value of property securityNoSelectedNInsuranceLevel.
     */
    public void setSecurityNoSelectedNInsuranceLevel(java.util.ArrayList securityNoSelectedNInsuranceLevel) {
        this.securityNoSelectedNInsuranceLevel = securityNoSelectedNInsuranceLevel;
    }
    
    /**
     * Getter for property strStoredSecurityNo.
     * @return Value of property strStoredSecurityNo.
     */
    public java.lang.String getStrStoredSecurityNo() {
        return strStoredSecurityNo;
    }
    
    /**
     * Setter for property strStoredSecurityNo.
     * @param strStoredSecurityNo New value of property strStoredSecurityNo.
     */
    public void setStrStoredSecurityNo(java.lang.String strStoredSecurityNo) {
        this.strStoredSecurityNo = strStoredSecurityNo;
    }
    
    /**
     * Getter for property insuranceTabValues.
     * @return Value of property insuranceTabValues.
     */
    public java.util.ArrayList getInsuranceTabValues() {
        return insuranceTabValues;
    }
    
    /**
     * Setter for property insuranceTabValues.
     * @param insuranceTabValues New value of property insuranceTabValues.
     */
    public void setInsuranceTabValues(java.util.ArrayList insuranceTabValues) {
        this.insuranceTabValues = insuranceTabValues;
    }
    
    /**
     * Getter for property insuranceAll.
     * @return Value of property insuranceAll.
     */
    public java.util.LinkedHashMap getInsuranceAll() {
        return insuranceAll;
    }
    
    /**
     * Setter for property insuranceAll.
     * @param insuranceAll New value of property insuranceAll.
     */
    public void setInsuranceAll(java.util.LinkedHashMap insuranceAll) {
        this.insuranceAll = insuranceAll;
    }
    
}
