/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition. 
 * 
 * AgriTermLoanPeakSanctionOB.java
 *
 * Created on February 17, 2009, 12:04 PM
 */

package com.see.truetransact.ui.termloan.agritermloan;
import com.see.truetransact.clientproxy.ProxyParameters;
import com.see.truetransact.transferobject.termloan.agritermloan.AgriTermLoanPeakSanctionTO;
import com.see.truetransact.clientutil.ClientUtil;
import com.see.truetransact.clientutil.EnhancedTableModel;
import com.see.truetransact.clientexception.ClientParseException;
import com.see.truetransact.commonutil.CommonConstants;
import com.see.truetransact.commonutil.DateUtil;
import com.see.truetransact.commonutil.CommonUtil;
import com.see.truetransact.commonutil.TTException;
import com.see.truetransact.transferobject.termloan.agritermloan.AgriTermLoanDocumentTO;
import com.see.truetransact.ui.TrueTransactMain;
import com.see.truetransact.uicomponent.COptionPane;
import com.see.truetransact.uicomponent.CObservable;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import org.apache.log4j.Logger;




/**
 *
 * @author  Administrator
 */
public class AgriTermLoanPeakSanctionOB extends  CObservable {
    
    /** Creates a new instance of TermLoanPeakSanctionOB */
    public AgriTermLoanPeakSanctionOB() {
           curDate = ClientUtil.getCurrentDate();
        termLoanPeakSanctionOB();
    }
    
   
    
    private       static AgriTermLoanPeakSanctionOB termLoanPeakSanctionOB;
    
    private final static Logger log = Logger.getLogger(AgriTermLoanPeakSanctionOB.class);
    private final static ClientParseException parseException = ClientParseException.getInstance();
    
//    private final   TermLoanRB objTermLoanRB = new TermLoanRB();
    
    private final   java.util.ResourceBundle objTermLoanRB = java.util.ResourceBundle.getBundle("com.see.truetransact.ui.termloan.agritermloan.AgriTermLoanRB", ProxyParameters.LANGUAGE);
    
    private final   String  ACCT_NO = "ACCT_NO";
    private final   String  ALL_VALUES = "ALL_VALUES";
    private final   String  BORROW_NO = "BORROW_NO";
    private final   String  COMMAND = "COMMAND";
    private final   String  DOC_DESC = "DOC_DESC";
    private final   String  DOC_EXEC_DT = "DOC_EXEC_DT";
    private final   String  DOC_EXP_DT = "DOC_EXP_DT";
    private final   String  DOC_FORM_NO = "DOC_FORM_NO";
    private final   String  DOC_TYPE = "DOC_TYPE";
    private final   String  INSERT = "INSERT";
    private final   String  IS_EXECUTED = "IS_EXECUTED";
    private final   String  IS_MANDATORY = "IS_MANDATORY";
    private final   String  IS_SUBMITTED = "IS_SUBMITTED";
    private final   String  NO = "N";
    private final   String  OPTION = "OPTION";
    private final   String  PRODUCT_ID = "PRODUCT_ID";
    private final   String  REMARKS = "REMARKS";
    private final   String  SUBMITTED_DT = "SUBMITTED_DT";
    private final   String  TABLE_VALUES = "TABLE_VALUES";
    private final   String  UPDATE = "UPDATE";
    private final   String  YES = "Y";
    
    private final   ArrayList peakSanctionTabTitle = new ArrayList();       //  Table Title of document
    private ArrayList documentEachTabRecord;
    private ArrayList PeakSanctionTabValues = new ArrayList();             // ArrayList to display in Guarantor Table
    
    private EnhancedTableModel tblPeakSanctionTab;
    
    private LinkedHashMap peakSanctionAll = new LinkedHashMap();           // Both displayed and hidden values in the table
    private HashMap documentEachRecord;
    private String PeakSanctionFromDate=null;
    private String PeakSanctionToDate=null;
    private String PeakSanctionAmt=null;
    
    Date curDate = null;
    
    static {
        try {
            termLoanPeakSanctionOB = new AgriTermLoanPeakSanctionOB();
        } catch(Exception e) {
            log.info("try: " + e);
            parseException.logException(e,true);
            e.printStackTrace();
        }
    }
    
    /**
     * To create the singleton object TermLoanDocumentDetailsOB
     * @return the instance of TermLoanDocumentDetailsOB
     */
    public static AgriTermLoanPeakSanctionOB getInstance() {
        return termLoanPeakSanctionOB;
    }
    
    private void termLoanPeakSanctionOB(){
        setPeakSanctionTabTitle();
        tblPeakSanctionTab = new EnhancedTableModel(null, peakSanctionTabTitle);
    }
    
    private void setPeakSanctionTabTitle(){
        try{
            
            peakSanctionTabTitle.add(objTermLoanRB.getString("tblColumnPeakSanFromDate"));
            peakSanctionTabTitle.add(objTermLoanRB.getString("tblColumnPeakToDate"));
            peakSanctionTabTitle.add(objTermLoanRB.getString("tblColumnPeakAmt"));
        }catch(Exception e){
            log.info("Exception in setDocumentTabTitle(): "+e);
            parseException.logException(e,true);
        }
    }
    
    public ArrayList getPeakSanctionTabTitle(){
        return this.peakSanctionTabTitle;
    }
    
    public void setTermLoanPeakSanctionTO(ArrayList documentList){
        try{
            HashMap termLoanDocumentTO;
            HashMap documentRecordMap;
            LinkedHashMap allPeakSanctionRecords = new LinkedHashMap();
            ArrayList removedValues = new ArrayList();
            ArrayList documentRecordList;
            ArrayList tabDocumentRecords = new ArrayList();
            // To retrieve the Guarantor Details from the Serverside
            for (int i = documentList.size() - 1,j = 0;i >= 0;--i,++j){
                termLoanDocumentTO = (HashMap) documentList.get(j);
                documentRecordMap = new HashMap();
                documentRecordList = new ArrayList();
                
                documentRecordList.add(CommonUtil.convertObjToStr(termLoanDocumentTO.get(DOC_TYPE)));
                documentRecordList.add(CommonUtil.convertObjToStr(termLoanDocumentTO.get(DOC_FORM_NO)));
                documentRecordList.add(CommonUtil.convertObjToStr(termLoanDocumentTO.get(DOC_DESC)));
                
                
                // If there is no account number then the record is retrieved from the Product Table
                if (CommonUtil.convertObjToStr(termLoanDocumentTO.get(ACCT_NO)).length() == 0){
//                    documentRecordMap.put(ACCT_NO, getStrACNumber());
//                    documentRecordMap.put(BORROW_NO, getBorrowerNo());
                    documentRecordMap.put(IS_SUBMITTED, NO);
//                    documentRecordMap.put(IS_MANDATORY, NO);
//                    documentRecordMap.put(IS_EXECUTED, NO);
//                    documentRecordMap.put(DOC_EXEC_DT, "");
//                    documentRecordMap.put(DOC_EXP_DT, "");
                    // CTable Value
                    documentRecordList.add(NO);
                    documentRecordMap.put(COMMAND, "");
                }else{
                    documentRecordMap.put(ACCT_NO, termLoanDocumentTO.get(ACCT_NO));
//                    documentRecordMap.put(BORROW_NO, termLoanDocumentTO.get(BORROW_NO));
                    documentRecordMap.put(IS_SUBMITTED, CommonUtil.convertObjToStr(termLoanDocumentTO.get(IS_SUBMITTED)));
                    // CTable Value
                    documentRecordList.add(CommonUtil.convertObjToStr(termLoanDocumentTO.get(IS_SUBMITTED)));
                    documentRecordMap.put(COMMAND, UPDATE);
                }
                
                tabDocumentRecords.add(documentRecordList);
                
                documentRecordMap.put(DOC_TYPE, CommonUtil.convertObjToStr(termLoanDocumentTO.get(DOC_TYPE)));
                documentRecordMap.put(DOC_FORM_NO, CommonUtil.convertObjToStr(termLoanDocumentTO.get(DOC_FORM_NO)));
                documentRecordMap.put(DOC_DESC, CommonUtil.convertObjToStr(termLoanDocumentTO.get(DOC_DESC)));
                documentRecordMap.put(REMARKS, CommonUtil.convertObjToStr(termLoanDocumentTO.get(REMARKS)));
                documentRecordMap.put(SUBMITTED_DT, DateUtil.getStringDate((Date) termLoanDocumentTO.get(SUBMITTED_DT)));
                documentRecordMap.put(IS_MANDATORY, CommonUtil.convertObjToStr(termLoanDocumentTO.get(IS_MANDATORY)));
                documentRecordMap.put(IS_EXECUTED, CommonUtil.convertObjToStr(termLoanDocumentTO.get(IS_EXECUTED)));
                documentRecordMap.put(DOC_EXEC_DT, DateUtil.getStringDate((Date) termLoanDocumentTO.get(DOC_EXEC_DT)));
                documentRecordMap.put(DOC_EXP_DT, DateUtil.getStringDate((Date) termLoanDocumentTO.get(DOC_EXP_DT)));
                
                allPeakSanctionRecords.put(getDocumentKey(CommonUtil.convertObjToStr(termLoanDocumentTO.get(DOC_TYPE)), CommonUtil.convertObjToStr(termLoanDocumentTO.get(DOC_FORM_NO))), documentRecordMap);
                
                documentRecordList = null;
                documentRecordMap = null;
            }
            peakSanctionAll.clear();
            PeakSanctionTabValues.clear();
            peakSanctionAll = allPeakSanctionRecords;
            PeakSanctionTabValues = tabDocumentRecords;
            tblPeakSanctionTab.setDataArrayList(PeakSanctionTabValues, getPeakSanctionTabTitle());
            tabDocumentRecords = null;
            allPeakSanctionRecords = null;
        }catch(Exception e){
            log.info("Exception in setDocumentTabTitle(): "+e);
            parseException.logException(e,true);
        }
    }
    
    public HashMap setTermLoanDocument(){
        HashMap docMap = new HashMap();
        try{
            AgriTermLoanDocumentTO objTermLoanDocumentTO;
            java.util.Set keySet =  peakSanctionAll.keySet();
            Object[] objKeySet = (Object[]) keySet.toArray();
            HashMap oneRecord;
            log.info(peakSanctionAll);
            log.info(peakSanctionAll.keySet());
            // To set the values for Security Transfer Object
            for (int i = keySet.size() - 1,j = 0;i >= 0;--i,++j){
                oneRecord = (HashMap) peakSanctionAll.get(objKeySet[j]);
                
                if (CommonUtil.convertObjToStr(oneRecord.get(COMMAND)).length() != 0){
                    log.info("Command"+oneRecord.get(COMMAND));
                    objTermLoanDocumentTO = new AgriTermLoanDocumentTO();
                    objTermLoanDocumentTO.setAcctNo(CommonUtil.convertObjToStr(oneRecord.get(ACCT_NO)));
//                    objTermLoanDocumentTO.setBorrowNo(CommonUtil.convertObjToStr(oneRecord.get(BORROW_NO)));
                    objTermLoanDocumentTO.setCommand(CommonUtil.convertObjToStr(oneRecord.get(COMMAND)));
                    objTermLoanDocumentTO.setDocFormNo(CommonUtil.convertObjToStr(oneRecord.get(DOC_FORM_NO)));
                    objTermLoanDocumentTO.setDocType(CommonUtil.convertObjToStr(oneRecord.get(DOC_TYPE)));
                    objTermLoanDocumentTO.setIsSubmitted(CommonUtil.convertObjToStr(oneRecord.get(IS_SUBMITTED)));
                    objTermLoanDocumentTO.setRemarks(CommonUtil.convertObjToStr(oneRecord.get(REMARKS)));
                    
//                    objTermLoanDocumentTO.setSubmittedDt((Date)oneRecord.get(SUBMITTED_DT));
                    objTermLoanDocumentTO.setSubmittedDt(DateUtil.getDateMMDDYYYY(CommonUtil.convertObjToStr(oneRecord.get(SUBMITTED_DT))));
                    
//                    objTermLoanDocumentTO.setDocExecDt((Date)oneRecord.get(DOC_EXEC_DT));
                    objTermLoanDocumentTO.setDocExecDt(DateUtil.getDateMMDDYYYY(CommonUtil.convertObjToStr(oneRecord.get(DOC_EXEC_DT))));
                    
//                    objTermLoanDocumentTO.setDocExpDt((Date)oneRecord.get(DOC_EXP_DT));
                    objTermLoanDocumentTO.setDocExpDt(DateUtil.getDateMMDDYYYY(CommonUtil.convertObjToStr(oneRecord.get(DOC_EXP_DT))));
                    objTermLoanDocumentTO.setIsExecuted(CommonUtil.convertObjToStr(oneRecord.get(IS_EXECUTED)));
                    objTermLoanDocumentTO.setIsMandatory(CommonUtil.convertObjToStr(oneRecord.get(IS_MANDATORY)));
                    objTermLoanDocumentTO.setStatusBy(TrueTransactMain.USER_ID);
                    objTermLoanDocumentTO.setStatusDt(curDate);
                    docMap.put(getDocumentKey(objTermLoanDocumentTO.getDocType(), objTermLoanDocumentTO.getDocFormNo()), objTermLoanDocumentTO);
                }
                oneRecord = null;
                objTermLoanDocumentTO = null;
            }
            log.info(docMap.keySet());
        }catch(Exception e){
            log.info("Exception in setTermLoanDocument(): "+e);
            parseException.logException(e,true);
        }
        return docMap;
    }
    
    public void changeStatusDocument(int resultType){
        try{
            if (resultType != 2){
                //If the Main Save Button pressed
                //                tableUtilDocument.getRemovedValues().clear();
            }
            java.util.Set keySet =  peakSanctionAll.keySet();
            Object[] objKeySet = (Object[]) keySet.toArray();
            HashMap oneRecord;
            // To change the Insert command to Update after Save Buttone Pressed
            // Document Details
            for (int i = keySet.size() - 1,j = 0;i >= 0;--i,++j){
                oneRecord = (HashMap) peakSanctionAll.get(objKeySet[j]);
                if (oneRecord.get(COMMAND).equals(INSERT)){
                    // If the status is in Insert Mode then change the mode to Update
                    oneRecord.put(COMMAND, UPDATE);
                    peakSanctionAll.put(objKeySet[j], oneRecord);
                }
                oneRecord = null;
            }
            keySet = null;
            objKeySet = null;
        }catch(Exception e){
            log.info("Exception in changeStatusDocument(): "+e);
            parseException.logException(e,true);
        }
    }
    
    private String getDocumentKey(String docType, String docNo){
        String strFacilityKey = "";
        StringBuffer strbufKey = new StringBuffer();
        try{
            strbufKey.append(docType);
            strbufKey.append("#");
            strbufKey.append(docNo);
            strFacilityKey = new String(strbufKey);
        }catch(Exception e){
            log.info("Exception caught in getDocumentKey: "+e);
            parseException.logException(e,true);
        }
        strbufKey = null;
        return strFacilityKey;
    }
    
    public boolean isDocumentCompleted(){
        boolean isCompleted = false;
        try{
            java.util.Set keySet =  peakSanctionAll.keySet();
            Object[] objKeySet = (Object[]) keySet.toArray();
            HashMap oneRecord;
            int i = keySet.size() - 1, j = 0;
            /*
             * To check mandatory records are executed
             */
            for (;i >= 0;--i,++j){
                oneRecord = null;
                oneRecord = (HashMap) peakSanctionAll.get(objKeySet[j]);
                if (CommonUtil.convertObjToStr(oneRecord.get(IS_MANDATORY)).equals(YES) && CommonUtil.convertObjToStr(oneRecord.get(IS_EXECUTED)).equals(YES)){
                    // If the document is mandatory then it should be executed
                    continue;
                }else if (CommonUtil.convertObjToStr(oneRecord.get(IS_MANDATORY)).equals(NO)){
                    // If the document is not mandatory then continue to next record
                    continue;
                }else if (CommonUtil.convertObjToStr(oneRecord.get(IS_MANDATORY)).equals(YES)&& (CommonUtil.convertObjToStr(oneRecord.get(IS_EXECUTED)).equals(NO) || CommonUtil.convertObjToStr(oneRecord.get(IS_EXECUTED)).length() <= 0)){
                    break;
                }
            }
            
            if (j != 0 && j == keySet.size()){
                // Atleast one document should be there and all the mandatory documents should be executed
                isCompleted = true;
            }
            oneRecord = null;
            keySet = null;
            objKeySet = null;
        }catch(Exception e){
            log.info("Exception caught in isDocumentCompleted: "+e);
            parseException.logException(e,true);
        }
        return isCompleted;
    }
  
    
   
    
  
    
    public void resetAllDocumentDetails(){
//        resetDocumentDetails();
    }
    
    public void resetPeakSanctionDetails(){
       setPeakSanctionAmt(null);
       setPeakSanctionFromDate(null);
       setPeakSanctionToDate(null);
    }
    
    public void resetPeakSanctionCTable(){
        tblPeakSanctionTab.setDataArrayList(null, peakSanctionTabTitle);
    }
    
    public int saveDocumentTab(int recordPosition){
        int option = -1;
        HashMap result = new HashMap();
        try{
            AgriTermLoanPeakSanctionTO termLoanPeakSanctionTO=new AgriTermLoanPeakSanctionTO();
            ArrayList dataList=new ArrayList();
            ArrayList newData =new ArrayList();
            termLoanPeakSanctionTO.setPeakSancFromDt(DateUtil.getDateMMDDYYYY(getPeakSanctionFromDate()));
            termLoanPeakSanctionTO.setPeakSancToDt(DateUtil.getDateMMDDYYYY(getPeakSanctionToDate()));
            termLoanPeakSanctionTO.setPeakSanctionAmt(CommonUtil.convertObjToDouble(getPeakSanctionAmt()));
            
//           newData.add(getPeakSanctionAmt());
//           newData.add(getPeakSanctionFromDate());
//           newData.add(getPeakSanctionToDate());
           dataList.add(termLoanPeakSanctionTO);

            ArrayList data = tblPeakSanctionTab.getDataArrayList();
            int datasize=data.size();
//            String strDocType = CommonUtil.convertObjToStr(((ArrayList) (tblPeakSanctionTab.getDataArrayList().get(recordPosition))).get(0));
//            String strDocNo = CommonUtil.convertObjToStr(((ArrayList) (tblPeakSanctionTab.getDataArrayList().get(recordPosition))).get(1));
            String strRecordKey = getDocumentKey(String.valueOf(datasize), "&");
            HashMap oneRec;
            if (peakSanctionAll.containsKey(strRecordKey)){
                oneRec = (HashMap) peakSanctionAll.get(strRecordKey);
                if (CommonUtil.convertObjToStr(oneRec.get(COMMAND)).length() == 0){
                    oneRec.put(COMMAND, INSERT);
                }
     
                peakSanctionAll.put(strRecordKey, oneRec);
            }
            tblPeakSanctionTab.setDataArrayList(dataList, peakSanctionTabTitle);
        }catch(Exception e){
            log.info("Error in saveDocumentTab(): "+e);
            parseException.logException(e,true);
        }
        result = null;
        return option;
    }
    
    // To create objects
    public void createObject(){
        PeakSanctionTabValues = new ArrayList();
        tblPeakSanctionTab.setDataArrayList(null, peakSanctionTabTitle);
        peakSanctionAll = new LinkedHashMap();
    }
    
    public void populateDocumentDetails(int recordPosition){
        try{
            HashMap eachRecs;
            java.util.Set keySet =  peakSanctionAll.keySet();
            Object[] objKeySet = (Object[]) keySet.toArray();
            String strDocType = CommonUtil.convertObjToStr(((ArrayList) (tblPeakSanctionTab.getDataArrayList().get(recordPosition))).get(0));
            String strDocNo = CommonUtil.convertObjToStr(((ArrayList) (tblPeakSanctionTab.getDataArrayList().get(recordPosition))).get(1));
            String strRecordKey = getDocumentKey(strDocType, strDocNo);
            // To populate the corresponding record from the Security Details
            for (int i = keySet.size() - 1, j = 0;i >= 0;--i, ++j){
                if (objKeySet[j].equals(strRecordKey)){
                    // To populate the Corresponding record from CTable
                    eachRecs = (HashMap) peakSanctionAll.get(objKeySet[j]);
                    
                 
                   
                eachRecs = null;
            }
            keySet = null;
            objKeySet = null;
            setChanged();
            ttNotifyObservers();
            }
        }catch(Exception e){
            log.info("Error in populateDocumentDetails(): "+e);
            parseException.logException(e,true);
        }
    
    }
    
    public void ttNotifyObservers(){
        notifyObservers();
    }
    
   
    
   
   
    
   /**
     * Getter for property PeakSanctionTabValues.
     * @return Value of property PeakSanctionTabValues.
     */
    public java.util.ArrayList getPeakSanctionTabValues() {
        return PeakSanctionTabValues;
    }
    
    /**
     * Setter for property PeakSanctionTabValues.
     * @param PeakSanctionTabValues New value of property PeakSanctionTabValues.
     */
    public void setPeakSanctionTabValues(java.util.ArrayList PeakSanctionTabValues) {
        this.PeakSanctionTabValues = PeakSanctionTabValues;
    }
    
    /**
     * Getter for property tblPeakSanctionTab.
     * @return Value of property tblPeakSanctionTab.
     */
    public com.see.truetransact.clientutil.EnhancedTableModel getTblPeakSanctionTab() {
        return tblPeakSanctionTab;
    }
    
    /**
     * Setter for property tblPeakSanctionTab.
     * @param tblPeakSanctionTab New value of property tblPeakSanctionTab.
     */
    public void setTblPeakSanctionTab(com.see.truetransact.clientutil.EnhancedTableModel tblPeakSanctionTab) {
        this.tblPeakSanctionTab = tblPeakSanctionTab;
    }
    
    /**
     * Getter for property PeakSanctionFromDate.
     * @return Value of property PeakSanctionFromDate.
     */
    public java.lang.String getPeakSanctionFromDate() {
        return PeakSanctionFromDate;
    }
    
    /**
     * Setter for property PeakSanctionFromDate.
     * @param PeakSanctionFromDate New value of property PeakSanctionFromDate.
     */
    public void setPeakSanctionFromDate(java.lang.String PeakSanctionFromDate) {
        this.PeakSanctionFromDate = PeakSanctionFromDate;
    }
    
    /**
     * Getter for property PeakSanctionToDate.
     * @return Value of property PeakSanctionToDate.
     */
    public java.lang.String getPeakSanctionToDate() {
        return PeakSanctionToDate;
    }
    
    /**
     * Setter for property PeakSanctionToDate.
     * @param PeakSanctionToDate New value of property PeakSanctionToDate.
     */
    public void setPeakSanctionToDate(java.lang.String PeakSanctionToDate) {
        this.PeakSanctionToDate = PeakSanctionToDate;
    }
    
    /**
     * Getter for property PeakSanctionAmt.
     * @return Value of property PeakSanctionAmt.
     */
    public java.lang.String getPeakSanctionAmt() {
        return PeakSanctionAmt;
    }
    
    /**
     * Setter for property PeakSanctionAmt.
     * @param PeakSanctionAmt New value of property PeakSanctionAmt.
     */
    public void setPeakSanctionAmt(java.lang.String PeakSanctionAmt) {
        this.PeakSanctionAmt = PeakSanctionAmt;
    }
    
    /**
     * Getter for property peakSanctionAll.
     * @return Value of property peakSanctionAll.
     */
    public java.util.LinkedHashMap getPeakSanctionAll() {
        return peakSanctionAll;
    }
    
    /**
     * Setter for property peakSanctionAll.
     * @param peakSanctionAll New value of property peakSanctionAll.
     */
    public void setPeakSanctionAll(java.util.LinkedHashMap peakSanctionAll) {
        this.peakSanctionAll = peakSanctionAll;
    }
    
}


