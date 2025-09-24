/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition.
 * 
 *
 * TermLoanSecurityOB.java
 *
 * Created on July 6, 2004, 12:23 PM
 */

package com.see.truetransact.ui.bills;

/**
 *
 * @author  shanmuga
 *
 */

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
import com.see.truetransact.transferobject.bills.TermLoanSecurityTO;
import com.see.truetransact.ui.TrueTransactMain;
import com.see.truetransact.uicomponent.CObservable;
import com.see.truetransact.uicomponent.COptionPane;

import org.apache.log4j.Logger;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Date;
import javax.swing.table.TableModel;

public class TermLoanSecurityOB extends CObservable{
    
    /** Creates a new instance of TermLoanSecurityOB */
    private TermLoanSecurityOB() throws Exception{
        curDate = ClientUtil.getCurrentDate();
        termLoanSecurityOB();
    }
    
    private       static TermLoanSecurityOB termLoanSecurityOB;
    private       static TermLoanOB termLoanOB;
    
    private final static Logger log = Logger.getLogger(TermLoanSecurityOB.class);
    private final static ClientParseException parseException = ClientParseException.getInstance();
    
    private double limitAmount = 0.0;
    private double totalSecurityValue = 0.0;
    private double avgSecurityValue = 0.0;
    
    private final   String  ACCOUNT_NO = "ACCOUNT_NO";
    private final   String  ALL = "ALL";
    private final   String  ALL_VALUES = "ALL_VALUES";
    private final   String  ASON = "ASON";
    private final   String  COLLATERAL = "COLLATERAL";
    private final   String  COMMAND = "COMMAND";
    private final   String  COMMODITY_ITEM = "COMMODITY_ITEM";
    private final   String  CUSTOMER_ID = "CUSTOMER ID";
    private final   String  CUSTOMER_NAME = "CUSTOMER NAME";
    private final   String  DATE_CHARGE = "DATE_CHARGE";
    private final   String  DATE_INSPECTION = "DATE_INSPECTION";
    private final   String  ELIGIBLE_LOAN = "ELIGIBLE_LOAN";
    private final   String  FROM_DATE = "FROM_DATE";
    private final   String  INSERT = "INSERT";
    private final   String  LOANS_AGAINST_DEPOSITS = "LOANS_AGAINST_DEPOSITS";
    private final   String  MARGIN = "MARGIN";
    private final   String  MILL_INDUS = "MILL_INDUS";
    private final   String  NATURE_CHARGE = "NATURE_CHARGE";
    private final   String  NO = "N";
    private final   String  OPTION = "OPTION";
    private final   String  PARTICULARS = "PARTICULARS";
    private final   String  PRIMARY = "PRIMARY";
    //    private final   String  PRODUCT_ID = "PRODUCT_ID";
    private final   String  SECURITY_CATEGORY = "SECURITY_CATEGORY";
    private final   String  SECURITY_DETAILS = "SECURITY_DETAILS";
    private final   String  SECURITY_NO = "SECURITY_NO";
    private final   String  SECURITY_TYPE = "SECURITY_TYPE";
    private final   String  SECURITY_VALUE = "SECURITY_VALUE";
    private final   String  SLNO = "SLNO";
    private final   String  STOCK_STATE_FREQ = "STOCK_STATE_FREQ";
    private final   String  TABLE_VALUES = "TABLE_VALUES";
    private final   String  TO_DATE = "TO_DATE";
    private final   String  UPDATE = "UPDATE";
    private final   String  YES = "Y";
    
    private final   ArrayList securityTabTitle = new ArrayList();       //  Table Title of Security
    private final   ArrayList depositSecurityTabTitle = new ArrayList();
    private ArrayList securityTabValues = new ArrayList();
    private ArrayList securityEachTabRecord;
    
    private LinkedHashMap securityAll = new LinkedHashMap();           // Both displayed and hidden values in the table
    private HashMap securityEachRecord;
    private HashMap oldEligibleLoanAmtMap;
    
    private EnhancedTableModel tblSecurityTab;
    
//    private final   TermLoanRB objTermLoanRB = new TermLoanRB();
    
    private final   java.util.ResourceBundle objTermLoanRB = java.util.ResourceBundle.getBundle("com.see.truetransact.ui.bills.TermLoanRB", ProxyParameters.LANGUAGE);
    
    private TableUtil tableUtilSecurity = new TableUtil();
    
    private String borrowerNo = "";
    private String txtSecurityNo = "";
    private String txtCustID_Security = "";
    private String txtSecurityValue = "";
    private String tdtFromDate = "";
    private String tdtToDate = "";
    private String txtMargin = "";
    private String txtEligibleLoan = "";
    private String lblProdId_Disp = "";
    private String lblAccHeadSec_2 = "";
    private String lblAccNoSec_2 = "";
    private String lblCustName_Security_Display = "";
    private String strACNumber = "";
    Date curDate = null;
    
    static {
        try {
            termLoanSecurityOB = new TermLoanSecurityOB();
        } catch(Exception e) {
            log.info("try: " + e);
            parseException.logException(e,true);
        }
    }
    
    private void termLoanSecurityOB()  throws Exception{
        setSecurityTabTitle();
        setDepositSecurityTabTitle();
        tableUtilSecurity.setAttributeKey(SLNO);
        tblSecurityTab = new EnhancedTableModel(null, securityTabTitle);
        oldEligibleLoanAmtMap = new HashMap();
    }
    
    public static TermLoanSecurityOB getInstance() {
        return termLoanSecurityOB;
    }
    
    private void setSecurityTabTitle() throws Exception{
        try{
            securityTabTitle.add(objTermLoanRB.getString("tblColumnSecuritySLNO"));
            securityTabTitle.add(objTermLoanRB.getString("tblColumnSecurityCustID"));
            securityTabTitle.add(objTermLoanRB.getString("tblColumnSecurityNo"));
            securityTabTitle.add(objTermLoanRB.getString("tblColumnSecurityEligible"));
            securityTabTitle.add(objTermLoanRB.getString("tblColumnSecurityMargin"));
            securityTabTitle.add(objTermLoanRB.getString("tblColumnSecurityValue"));
        }catch(Exception e){
            log.info("Exception in setSecurityTabTitle(): "+e);
            parseException.logException(e,true);
        }
    }
    
    public ArrayList getSecurityTabTitle(){
        return this.securityTabTitle;
    }
    
    private void setDepositSecurityTabTitle() throws Exception{
        try{
            depositSecurityTabTitle.add(objTermLoanRB.getString("tblColumnDepositNo"));
            depositSecurityTabTitle.add(objTermLoanRB.getString("tblColumnDepositSubNo"));
            depositSecurityTabTitle.add(objTermLoanRB.getString("tblColumnLienNo"));
            depositSecurityTabTitle.add(objTermLoanRB.getString("tblColumnSecurityValue"));
            depositSecurityTabTitle.add(objTermLoanRB.getString("tblColumnLienDt"));
        }catch(Exception e){
            log.info("Exception caught in setDepositSecurityTabTitle: "+e);
            parseException.logException(e,true);
        }
    }
    
    public ArrayList getDepositSecurityTabTitle(){
        return this.depositSecurityTabTitle;
    }
    
    
    /**
     * Setter for property tblSecurityLoanTab.
     *
     */
    public void setTblDepositSecurityTable() {
        tblSecurityTab = new EnhancedTableModel(null, getDepositSecurityTabTitle());
    }
    
    /**
     * Setter for property tblSecurityLoanTab.
     *
     */
    public void setTblSecurityTable() {
        tblSecurityTab = new EnhancedTableModel(null, getSecurityTabTitle());
    }
    
    public void resetAllSecurityDetails(){
        resetSecurityDetails();
        setLblProdId_Disp("");
        setLblAccHeadSec_2("");
        setLblAccNoSec_2("");
        oldEligibleLoanAmtMap = null;
        oldEligibleLoanAmtMap = new HashMap();
    }
    
    public void resetSecurityDetails(){
        setTxtCustID_Security("");
        setLblCustName_Security_Display("");
        setTxtSecurityNo("");
        setTxtSecurityValue("");
        setTdtFromDate("");
        setTdtToDate("");
        setTxtMargin("");
        setTxtEligibleLoan("");
    }
    
    public void resetSecurityTableUtil(){
        tableUtilSecurity = new TableUtil();
        tableUtilSecurity.setAttributeKey(SLNO);
    }
    
    public void changeStatusSecurity(int resultType){
        try{
            if (resultType != 2){
                //If the Main Save Button pressed
                tableUtilSecurity.getRemovedValues().clear();
            }
            java.util.Set keySet =  securityAll.keySet();
            Object[] objKeySet = (Object[]) keySet.toArray();
            HashMap oneRecord;
            // To change the Insert command to Update after Save Buttone Pressed
            // Security Details
            for (int i = keySet.size() - 1,j = 0;i >= 0;--i,++j){
                oneRecord = (HashMap) securityAll.get(objKeySet[j]);
                if (oneRecord.get(COMMAND).equals(INSERT)){
                    // If the status is in Insert Mode then change the mode to Update
                    oneRecord.put(COMMAND, UPDATE);
                    securityAll.put(objKeySet[j], oneRecord);
                }
                oneRecord = null;
            }
            keySet = null;
            objKeySet = null;
        }catch(Exception e){
            log.info("Exception in changeStatusSecurity(): "+e);
            parseException.logException(e,true);
        }
    }
    
    private String getEligibleLoanAmtKey(String custID, String strSecurityNo) throws Exception{
        return custID + "#" + strSecurityNo;
    }
    
    public void setTermLoanSecurityTO(ArrayList objSecurityTOList, String act_Num, String strFaciType){
        try{
            TermLoanSecurityTO objTermLoanSecurityTO;
            HashMap securityRecordMap;
            ArrayList securityRecordList;
            ArrayList securityCTableValues = new ArrayList();
            ArrayList removedValues = new ArrayList();
            LinkedHashMap allLocalRecs = new LinkedHashMap();
            oldEligibleLoanAmtMap = null;
            oldEligibleLoanAmtMap = new HashMap();
            String strKey = "";
            // To retrieve the Security Details from the Database
            for (int i = objSecurityTOList.size() - 1,j = 0;i >= 0;--i,++j){
                objTermLoanSecurityTO = (TermLoanSecurityTO) objSecurityTOList.get(j);
                securityRecordMap  = new HashMap();
                securityRecordList = new ArrayList();
                
                securityRecordList.add(CommonUtil.convertObjToStr(objTermLoanSecurityTO.getSlno()));
                securityRecordList.add(CommonUtil.convertObjToStr(objTermLoanSecurityTO.getCustId()));
                securityRecordList.add(CommonUtil.convertObjToStr(objTermLoanSecurityTO.getSecurityNo()));
                securityRecordList.add(CommonUtil.convertObjToStr(objTermLoanSecurityTO.getEligibleLoanAmt()));
                securityRecordList.add(CommonUtil.convertObjToStr(objTermLoanSecurityTO.getMargin()));
                securityRecordList.add(CommonUtil.convertObjToStr(objTermLoanSecurityTO.getSecurityAmt()));
                
                securityCTableValues.add(securityRecordList);
                
                securityRecordMap.put(SLNO, CommonUtil.convertObjToStr(objTermLoanSecurityTO.getSlno()));
                securityRecordMap.put(ELIGIBLE_LOAN, CommonUtil.convertObjToStr(objTermLoanSecurityTO.getEligibleLoanAmt()));
                securityRecordMap.put(MARGIN, CommonUtil.convertObjToStr(objTermLoanSecurityTO.getMargin()));
                securityRecordMap.put(SECURITY_NO, CommonUtil.convertObjToStr(objTermLoanSecurityTO.getSecurityNo()));
                securityRecordMap.put(CUSTOMER_ID, CommonUtil.convertObjToStr(objTermLoanSecurityTO.getCustId()));
                securityRecordMap.put(SECURITY_VALUE, CommonUtil.convertObjToStr(objTermLoanSecurityTO.getSecurityAmt()));
                securityRecordMap.put(TO_DATE, DateUtil.getStringDate(objTermLoanSecurityTO.getToDt()));
                securityRecordMap.put(FROM_DATE, DateUtil.getStringDate(objTermLoanSecurityTO.getFromDt()));
                securityRecordMap.put(ACCOUNT_NO, CommonUtil.convertObjToStr(objTermLoanSecurityTO.getAcctNum()));
                
                securityRecordMap.put(COMMAND, UPDATE);
                
                strKey = getEligibleLoanAmtKey(CommonUtil.convertObjToStr(objTermLoanSecurityTO.getCustId()), CommonUtil.convertObjToStr(objTermLoanSecurityTO.getSecurityNo()));
                oldEligibleLoanAmtMap.put(strKey, objTermLoanSecurityTO.getEligibleLoanAmt());
                allLocalRecs.put(CommonUtil.convertObjToStr(objTermLoanSecurityTO.getSlno()), securityRecordMap);  //List of values corresponding to the table
                
                strKey = null;
                securityRecordMap  = null;
                securityRecordList = null;
            }
            securityTabValues.clear();
            securityAll.clear();
            securityTabValues = securityCTableValues;
            securityAll = allLocalRecs;
            if (strFaciType.equals(LOANS_AGAINST_DEPOSITS)){
                setTblDepositSecurityTable();
            }else{
                tblSecurityTab.setDataArrayList(securityTabValues, getSecurityTabTitle());
                tableUtilSecurity.setRemovedValues(removedValues);
                tableUtilSecurity.setAllValues(securityAll);
                tableUtilSecurity.setTableValues(securityTabValues);
                setMax_Del_Security_No(act_Num);
            }
            securityCTableValues = null;
            allLocalRecs = null;
        }catch(Exception e){
            log.info("Error in setTermLoanSecurityTO()..."+e);
            parseException.logException(e,true);
        }
    }
    
    private void setMax_Del_Security_No(String act_Num){
        try{
            HashMap transactionMap = new HashMap();
            HashMap retrieve = new HashMap();
            transactionMap.put("acctNum", act_Num);
            List resultList = ClientUtil.executeQuery("getSelectBillsSecurityMaxSLNO", transactionMap);
            if (resultList.size() > 0){
                retrieve = (HashMap) resultList.get(0);
                tableUtilSecurity.setStr_MAX_DEL_SL_NO(CommonUtil.convertObjToStr(retrieve.get("MAX_NO")));
            }
            retrieve = null;
            transactionMap = null;
            resultList = null;
        }catch(Exception e){
            log.info("Error In setMax_Del_Security_No: "+e);
            parseException.logException(e,true);
        }
    }
    
    public HashMap setTermLoanSecurity(){
        HashMap securityMap = new HashMap();
        try{
            TermLoanSecurityTO objTermLoanSecurityTO;
            java.util.Set keySet =  securityAll.keySet();
            Object[] objKeySet = (Object[]) keySet.toArray();
            HashMap oneRecord;
            
            // To set the values for Security Transfer Object
            for (int i = keySet.size() - 1,j = 0;i >= 0;--i,++j){
                oneRecord = (HashMap) securityAll.get(objKeySet[j]);
                objTermLoanSecurityTO = new TermLoanSecurityTO();
                objTermLoanSecurityTO.setAcctNum(CommonUtil.convertObjToStr(oneRecord.get(ACCOUNT_NO)));
//                objTermLoanSecurityTO.setBorrowNo(getBorrowerNo());
                objTermLoanSecurityTO.setCustId(CommonUtil.convertObjToStr(oneRecord.get(CUSTOMER_ID)));
                objTermLoanSecurityTO.setCommand(CommonUtil.convertObjToStr(oneRecord.get(COMMAND)));
//                objTermLoanSecurityTO.setFromDt((Date)oneRecord.get(FROM_DATE));
                objTermLoanSecurityTO.setFromDt(DateUtil.getDateMMDDYYYY(CommonUtil.convertObjToStr(oneRecord.get(FROM_DATE))));
                objTermLoanSecurityTO.setSecurityNo(CommonUtil.convertObjToDouble(oneRecord.get(SECURITY_NO)));
                objTermLoanSecurityTO.setSlno(CommonUtil.convertObjToDouble(oneRecord.get(SLNO)));
                objTermLoanSecurityTO.setSecurityAmt(CommonUtil.convertObjToDouble(oneRecord.get(SECURITY_VALUE)));
                objTermLoanSecurityTO.setStatus(CommonUtil.convertObjToStr(oneRecord.get(COMMAND)));
//                objTermLoanSecurityTO.setToDt((Date)oneRecord.get(TO_DATE));
                objTermLoanSecurityTO.setToDt(DateUtil.getDateMMDDYYYY(CommonUtil.convertObjToStr(oneRecord.get(TO_DATE))));
                objTermLoanSecurityTO.setMargin(CommonUtil.convertObjToDouble(oneRecord.get(MARGIN)));
                objTermLoanSecurityTO.setEligibleLoanAmt(CommonUtil.convertObjToDouble(oneRecord.get(ELIGIBLE_LOAN)));
                if (oneRecord.get(COMMAND).equals(INSERT)){
                    objTermLoanSecurityTO.setStatus(CommonConstants.STATUS_CREATED);
                }else if (oneRecord.get(COMMAND).equals(UPDATE)){
                    objTermLoanSecurityTO.setStatus(CommonConstants.STATUS_MODIFIED);
                }
                objTermLoanSecurityTO.setStatusBy(TrueTransactMain.USER_ID);
                objTermLoanSecurityTO.setStatusDt(curDate);
                securityMap.put(String.valueOf(j+1), objTermLoanSecurityTO);
                oneRecord = null;
                objTermLoanSecurityTO = null;
            }
            
            // To set the values for Security Transfer Object
            // where as the existing records in Database are deleted in client side
            // useful for updating the status
            for (int i = tableUtilSecurity.getRemovedValues().size() - 1,j = 0;i >= 0;--i,++j){
                oneRecord = (HashMap) tableUtilSecurity.getRemovedValues().get(j);
                objTermLoanSecurityTO = new TermLoanSecurityTO();
                objTermLoanSecurityTO.setAcctNum(CommonUtil.convertObjToStr(oneRecord.get(ACCOUNT_NO)));
//                objTermLoanSecurityTO.setBorrowNo(getBorrowerNo());
                objTermLoanSecurityTO.setCustId(CommonUtil.convertObjToStr(oneRecord.get(CUSTOMER_ID)));
                objTermLoanSecurityTO.setCommand(CommonUtil.convertObjToStr(oneRecord.get(COMMAND)));
//                objTermLoanSecurityTO.setFromDt((Date)oneRecord.get(FROM_DATE));
                objTermLoanSecurityTO.setFromDt(DateUtil.getDateMMDDYYYY(CommonUtil.convertObjToStr(oneRecord.get(FROM_DATE))));
                objTermLoanSecurityTO.setSecurityNo(CommonUtil.convertObjToDouble(oneRecord.get(SECURITY_NO)));
                objTermLoanSecurityTO.setSlno(CommonUtil.convertObjToDouble(oneRecord.get(SLNO)));
                objTermLoanSecurityTO.setSecurityAmt(CommonUtil.convertObjToDouble(oneRecord.get(SECURITY_VALUE)));
                objTermLoanSecurityTO.setStatus(CommonUtil.convertObjToStr(oneRecord.get(COMMAND)));
//                objTermLoanSecurityTO.setToDt((Date)oneRecord.get(TO_DATE));
                objTermLoanSecurityTO.setToDt(DateUtil.getDateMMDDYYYY(CommonUtil.convertObjToStr(oneRecord.get(TO_DATE))));
                objTermLoanSecurityTO.setMargin(CommonUtil.convertObjToDouble(oneRecord.get(MARGIN)));
                objTermLoanSecurityTO.setEligibleLoanAmt(CommonUtil.convertObjToDouble(oneRecord.get(ELIGIBLE_LOAN)));
                objTermLoanSecurityTO.setStatus(CommonConstants.STATUS_DELETED);
                objTermLoanSecurityTO.setStatusBy(TrueTransactMain.USER_ID);
                objTermLoanSecurityTO.setStatusDt(curDate);
                securityMap.put(String.valueOf(securityMap.size()+1), objTermLoanSecurityTO);
                oneRecord = null;
                objTermLoanSecurityTO = null;
            }
            
            securityMap.put("OLD_ELIGIBLE_LOAN_AMT", oldEligibleLoanAmtMap);
        }catch(Exception e){
            log.info("Error In setTermLoanSecurity()..."+e);
            parseException.logException(e,true);
        }
        return securityMap;
    }
    
    public void setBorrowerNo(String borrowerNo){
        this.borrowerNo = borrowerNo;
        setChanged();
    }
    
    public String getBorrowerNo(){
        return this.borrowerNo;
    }
    
    public void setLblAccHeadSec_2(String lblAccHeadSec_2){
        this.lblAccHeadSec_2 = lblAccHeadSec_2;
        setChanged();
    }
    
    public String getLblAccHeadSec_2(){
        return this.lblAccHeadSec_2;
    }
    
    public void setLblAccNoSec_2(String lblAccNoSec_2){
        this.lblAccNoSec_2 = lblAccNoSec_2;
        setChanged();
    }
    
    public String getLblAccNoSec_2(){
        return this.lblAccNoSec_2;
    }
    
    void setTblSecurityTab(EnhancedTableModel tblSecurityTab){
        log.info("In setTblSecurityTab()...");
        
        this.tblSecurityTab = tblSecurityTab;
        setChanged();
    }
    
    EnhancedTableModel getTblSecurityTab(){
        return this.tblSecurityTab;
    }
    
    void setLblProdId_Disp(String lblProdId_Disp){
        this.lblProdId_Disp = lblProdId_Disp;
        setChanged();
    }
    String getLblProdId_Disp(){
        return this.lblProdId_Disp;
    }
    
    void setTxtSecurityNo(String txtSecurityNo){
        this.txtSecurityNo = txtSecurityNo;
        setChanged();
    }
    String getTxtSecurityNo(){
        return this.txtSecurityNo;
    }
    
    void setTxtSecurityValue(String txtSecurityValue){
        this.txtSecurityValue = txtSecurityValue;
        setChanged();
    }
    String getTxtSecurityValue(){
        return this.txtSecurityValue;
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
    
    public void ttNotifyObservers(){
        notifyObservers();
    }
    
    
    public int addSecurityDetails(int recordPosition, boolean update){
        int option = -1;
        try{
            double tempTotSecVal = 0.0;
            termLoanOB = TermLoanOB.getInstance();
            
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
                
            }else{
                option = updateSecurityTab(recordPosition);
            }
            
            setChanged();
            
            securityEachTabRecord = null;
            securityEachRecord = null;
            result = null;
            data = null;
            termLoanOB = null;
        }catch(Exception e){
            log.info("Error in addSecurityDetails()..."+e);
            parseException.logException(e,true);
        }
        return option;
    }
    
    private void insertSecurity(int recordPosition){
        securityEachTabRecord.add(String.valueOf(recordPosition));
        securityEachTabRecord.add(getTxtCustID_Security());
        securityEachTabRecord.add(getTxtSecurityNo());
        securityEachTabRecord.add(getTxtEligibleLoan());
        securityEachTabRecord.add(getTxtMargin());
        securityEachTabRecord.add(getTxtSecurityValue());
        
        securityEachRecord.put(SLNO, "");
        securityEachRecord.put(CUSTOMER_ID, getTxtCustID_Security());
        securityEachRecord.put(SECURITY_NO, getTxtSecurityNo());
        securityEachRecord.put(SECURITY_VALUE, getTxtSecurityValue());
        securityEachRecord.put(TO_DATE, getTdtToDate());
        securityEachRecord.put(FROM_DATE, getTdtFromDate());
        securityEachRecord.put(MARGIN, getTxtMargin());
        securityEachRecord.put(ELIGIBLE_LOAN, getTxtEligibleLoan());
        securityEachRecord.put(ACCOUNT_NO, getLblAccNoSec_2());
        securityEachRecord.put(COMMAND, "");
    }
    
    private int updateSecurityTab(int recordPosition){
        int option = -1;
        HashMap result = new HashMap();
        try{
            double secValueBeforeUpdate = CommonUtil.convertObjToDouble(((ArrayList) securityTabValues.get(recordPosition)).get(2)).doubleValue();
            result = tableUtilSecurity.updateTableValues(securityEachTabRecord, securityEachRecord, recordPosition);
            
            securityTabValues = (ArrayList) result.get(TABLE_VALUES);
            securityAll = (LinkedHashMap) result.get(ALL_VALUES);
            option = CommonUtil.convertObjToInt(result.get(OPTION));
            
            tblSecurityTab.setDataArrayList(securityTabValues, securityTabTitle);
            
        }catch(Exception e){
            log.info("Error in updateSecurityTab()..."+e);
            parseException.logException(e,true);
        }
        result = null;
        return option;
    }
    
    public void populateSecurityDetails(int recordPosition){
        try{
            log.info(securityAll);
            HashMap eachRecs;
            java.util.Set keySet =  securityAll.keySet();
            Object[] objKeySet = (Object[]) keySet.toArray();
            String strRecordKey = (String) ((ArrayList) (tblSecurityTab.getDataArrayList().get(recordPosition))).get(0);
            // To populate the corresponding record from the Security Details
            for (int i = keySet.size() - 1, j = 0;i >= 0;--i, ++j){
                if (((String) ((HashMap) securityAll.get(objKeySet[j])).get(SLNO)).equals(strRecordKey)){
                    // To populate the Corresponding record from CTable
                    eachRecs = (HashMap) securityAll.get(objKeySet[j]);
                    setTxtSecurityNo(CommonUtil.convertObjToStr(eachRecs.get(SECURITY_NO)));
                    setTxtCustID_Security(CommonUtil.convertObjToStr(eachRecs.get(CUSTOMER_ID)));
                    setLblCustName_Security_Display(getCustomerName(getTxtCustID_Security()));
                    setTxtSecurityValue(CommonUtil.convertObjToStr(eachRecs.get(SECURITY_VALUE)));
                    setTdtToDate(CommonUtil.convertObjToStr(eachRecs.get(TO_DATE)));
                    setTdtFromDate(CommonUtil.convertObjToStr(eachRecs.get(FROM_DATE)));
                    setTxtMargin(CommonUtil.convertObjToStr(eachRecs.get(MARGIN)));
                    setTxtEligibleLoan(CommonUtil.convertObjToStr(eachRecs.get(ELIGIBLE_LOAN)));
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
            
        }catch(Exception e){
            log.info("Error in deleteSecurityTabRecord()..."+e);
            parseException.logException(e,true);
        }
        result = null;
    }
    
    private String getCustomerName(String strCustID){
        String strCustName = "";
        try{
            HashMap transactionMap = new HashMap();
            HashMap retrieve = new HashMap();
            transactionMap.put("CUST_ID", strCustID);
            List resultList = ClientUtil.executeQuery("getSelectSecurityCustName", transactionMap);
            if (resultList.size() > 0){
                retrieve = (HashMap) resultList.get(0);
                strCustName = CommonUtil.convertObjToStr(retrieve.get(CUSTOMER_NAME));
            }
            retrieve = null;
            transactionMap = null;
            resultList = null;
        }catch(Exception e){
            log.info("Exception caught in getCustomerName: "+e);
            parseException.logException(e,true);
        }
        return strCustName;
    }
    
    public void populateCustDetails(HashMap map){
        try{
            setTxtCustID_Security(CommonUtil.convertObjToStr(map.get(CUSTOMER_ID)));
            setLblCustName_Security_Display(CommonUtil.convertObjToStr(map.get(CUSTOMER_NAME)));
        }catch(Exception e){
            log.info("Exception caught in populateCustDetails: "+e);
            parseException.logException(e,true);
        }
    }
    
    public String populateEligibleLoanAgainstSecurity(String strSecAmt, String strMargin, String strLoanAmt){
        String strEligibleLoan = "";
        try{
            double realSecurityValue = CommonUtil.convertObjToDouble(strSecAmt).doubleValue();
            double margin = CommonUtil.convertObjToDouble(strMargin).doubleValue();
            double marginAmt = calculateMarginValue(realSecurityValue, margin);
            strEligibleLoan = String.valueOf(marginAmt);
        }catch(Exception e){
            log.info("Exception caught in populateEligibleLoanAgainstSecurity: "+e);
            parseException.logException(e,true);
        }
        return strEligibleLoan;
    }
    
    private double calculateMarginValue(double realSecurityValue, double margin){
        double securityAmt = 0.0;
        try{
             securityAmt = (realSecurityValue * ((100.0 - margin) / 100.0));
        }catch(Exception e){
            log.info("Exception caught in calculateMarginValue: "+e);
            parseException.logException(e,true);
        }
        return securityAmt;
    }
    
    public void populateSecurityID_Value(HashMap hash, String strLoanAmt){
        try{
            setTxtSecurityNo(CommonUtil.convertObjToStr(hash.get(SECURITY_NO)));
            double realSecurityValue = CommonUtil.convertObjToDouble(hash.get("AVAILABLE_SECURITY_VALUE")).doubleValue();
            double loanAmt = CommonUtil.convertObjToDouble(strLoanAmt).doubleValue();
            if (loanAmt > realSecurityValue){
                int option = -1;
                String[] options = {objTermLoanRB.getString("cDialogOk")};
                option = COptionPane.showOptionDialog(null, objTermLoanRB.getString("securityValueMsg"), CommonConstants.WARNINGTITLE,
                COptionPane.OK_OPTION, COptionPane.WARNING_MESSAGE, null, options, options[0]);
            }
            setTxtSecurityValue(CommonUtil.convertObjToStr(hash.get("AVAILABLE_SECURITY_VALUE")));
            setTxtEligibleLoan("");
        }catch(Exception e){
            log.info("Exception caught in populateSecurityID_Value: "+e);
            parseException.logException(e,true);
        }
    }
    
    public boolean chkForSecValLessThanLimiVal(String strLimitAmt){
        return chkForSecValLessThanLimiVal(strLimitAmt, null);
    }
    
    public boolean chkForSecValLessThanLimiVal(String strLimitAmt, TableModel tblSecTab){
        try{
            double limitAmt = CommonUtil.convertObjToDouble(strLimitAmt).doubleValue();
            double secAmt = 0.0;
            if (tblSecTab == null){
                ArrayList secValues = tableUtilSecurity.getTableValues();
                ArrayList oneRec;
                for (int i = secValues.size() - 1, j = 0;i >= 0;--i,++j){
                    oneRec = (ArrayList) secValues.get(j);
                    secAmt += CommonUtil.convertObjToDouble(oneRec.get(3)).doubleValue();
                    oneRec = null;
                }
                secValues = null;
            }else{
                int noOfRecords = tblSecTab.getRowCount();
                for (int i = noOfRecords - 1, j = 0;i >= 0;--i,++j){
                    secAmt += CommonUtil.convertObjToDouble(tblSecTab.getValueAt(j, 3)).doubleValue();
                }
            }
            if (secAmt < limitAmt){
                return true;
            }
        }catch(Exception e){
            log.info("Exception caught in chkForSecurityVal: "+e);
            parseException.logException(e,true);
        }
        return false;
    }
    
    // To create objects
    public void createObject(){
        securityTabValues = new ArrayList();
        securityAll = new LinkedHashMap();
        tableUtilSecurity = new TableUtil();
        tableUtilSecurity.setAttributeKey(SLNO);
        oldEligibleLoanAmtMap = new HashMap();
        tblSecurityTab = new EnhancedTableModel(null, getSecurityTabTitle());
    }
    
    // To destroy Objects
    public void destroyObjects(){
        securityTabValues = null;
        securityAll = null;
        tableUtilSecurity = null;
        oldEligibleLoanAmtMap = null;
        tblSecurityTab = null;
    }
    
    /**
     * Getter for property txtCustID_Security.
     * @return Value of property txtCustID_Security.
     */
    public java.lang.String getTxtCustID_Security() {
        return txtCustID_Security;
    }
    
    /**
     * Setter for property txtCustID_Security.
     * @param txtCustID_Security New value of property txtCustID_Security.
     */
    public void setTxtCustID_Security(java.lang.String txtCustID_Security) {
        this.txtCustID_Security = txtCustID_Security;
    }
    
    /**
     * Getter for property lblCustName_Security_Display.
     * @return Value of property lblCustName_Security_Display.
     */
    public java.lang.String getLblCustName_Security_Display() {
        return lblCustName_Security_Display;
    }
    
    /**
     * Setter for property lblCustName_Security_Display.
     * @param lblCustName_Security_Display New value of property lblCustName_Security_Display.
     */
    public void setLblCustName_Security_Display(java.lang.String lblCustName_Security_Display) {
        this.lblCustName_Security_Display = lblCustName_Security_Display;
    }
    
    /**
     * Getter for property strACNumber.
     * @return Value of property strACNumber.
     */
    public java.lang.String getStrACNumber() {
        return strACNumber;
    }
    
    /**
     * Setter for property strACNumber.
     * @param strACNumber New value of property strACNumber.
     */
    public void setStrACNumber(java.lang.String strACNumber) {
        this.strACNumber = strACNumber;
    }
    
    /**
     * Getter for property txtMargin.
     * @return Value of property txtMargin.
     */
    public java.lang.String getTxtMargin() {
        return txtMargin;
    }
    
    /**
     * Setter for property txtMargin.
     * @param txtMargin New value of property txtMargin.
     */
    public void setTxtMargin(java.lang.String txtMargin) {
        this.txtMargin = txtMargin;
    }
    
    /**
     * Getter for property txtEligibleLoan.
     * @return Value of property txtEligibleLoan.
     */
    public java.lang.String getTxtEligibleLoan() {
        return txtEligibleLoan;
    }
    
    /**
     * Setter for property txtEligibleLoan.
     * @param txtEligibleLoan New value of property txtEligibleLoan.
     */
    public void setTxtEligibleLoan(java.lang.String txtEligibleLoan) {
        this.txtEligibleLoan = txtEligibleLoan;
    }
    
}
