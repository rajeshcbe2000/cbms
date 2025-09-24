/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition.  
 * 
 * TermLoanDocumentDetailsOB.java
 *
 * Created on December 30, 2004, 5:35 PM
 */

package com.see.truetransact.ui.bills;

import com.see.truetransact.clientproxy.ProxyParameters;
import com.see.truetransact.clientutil.ClientUtil;
import com.see.truetransact.clientutil.EnhancedTableModel;
import com.see.truetransact.clientexception.ClientParseException;
import com.see.truetransact.commonutil.CommonConstants;
import com.see.truetransact.commonutil.DateUtil;
import com.see.truetransact.commonutil.CommonUtil;
import com.see.truetransact.commonutil.TTException;
import com.see.truetransact.transferobject.bills.TermLoanDocumentTO;
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
 * @author  152713
 */
public class TermLoanDocumentDetailsOB extends CObservable {
    
    /** Creates a new instance of TermLoanDocumentDetailsOB */
    public TermLoanDocumentDetailsOB() {
        curDate = ClientUtil.getCurrentDate();
        termLoanDocumentDetailsOB();
    }
    
    private       static TermLoanDocumentDetailsOB termLoanDocumentDetailsOB;
    
    private final static Logger log = Logger.getLogger(TermLoanDocumentDetailsOB.class);
    private final static ClientParseException parseException = ClientParseException.getInstance();
    
//    private final   TermLoanRB objTermLoanRB = new TermLoanRB();
    
    private final   java.util.ResourceBundle objTermLoanRB = java.util.ResourceBundle.getBundle("com.see.truetransact.ui.bills.TermLoanRB", ProxyParameters.LANGUAGE);
    
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
    
    private final   ArrayList documentTabTitle = new ArrayList();       //  Table Title of document
    private ArrayList documentEachTabRecord;
    private ArrayList documentTabValues = new ArrayList();             // ArrayList to display in Guarantor Table
    
    private EnhancedTableModel tblDocumentTab;
    
    private LinkedHashMap documentAll = new LinkedHashMap();           // Both displayed and hidden values in the table
    private HashMap documentEachRecord;
    
    private String borrowerNo = "";
    private String strACNumber = "";
    private String tdtExpiryDate_DOC = "";
    private String tdtExecuteDate_DOC = "";
    private boolean rdoNo_Executed_DOC = false;
    private boolean rdoYes_Executed_DOC = false;
    private boolean rdoNo_Mandatory_DOC = false;
    private boolean rdoYes_Mandatory_DOC = false;
    private String lblProdID_Disp_DocumentDetails = "";
    private String lblAcctHead_Disp_DocumentDetails = "";
    private String lblAcctNo_Disp_DocumentDetails = "";
    private String lblDocDesc_Disp_DocumentDetails = "";
    private String lblDocNo_Disp_DocumentDetails = "";
    private String lblDocType_Disp_DocumentDetails = "";
    private String txtRemarks_DocumentDetails = "";
    private String tdtSubmitDate_DocumentDetails = "";
    private boolean rdoYes_DocumentDetails = false;
    private boolean rdoNo_DocumentDetails = false;
    Date curDate = null;
    
    static {
        try {
            termLoanDocumentDetailsOB = new TermLoanDocumentDetailsOB();
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
    public static TermLoanDocumentDetailsOB getInstance() {
        return termLoanDocumentDetailsOB;
    }
    
    private void termLoanDocumentDetailsOB(){
        setDocumentTabTitle();
        tblDocumentTab = new EnhancedTableModel(null, documentTabTitle);
    }
    
    private void setDocumentTabTitle(){
        try{
            documentTabTitle.add(objTermLoanRB.getString("tblColumnDocumentType"));
            documentTabTitle.add(objTermLoanRB.getString("tblColumnDocumentNo"));
            documentTabTitle.add(objTermLoanRB.getString("tblColumnDocumentDesc"));
            documentTabTitle.add(objTermLoanRB.getString("tblColumnDocumentSubmit"));
        }catch(Exception e){
            log.info("Exception in setDocumentTabTitle(): "+e);
            parseException.logException(e,true);
        }
    }
    
    public ArrayList getDocumentTabTitle(){
        return this.documentTabTitle;
    }
    
    public void setTermLoanDocumentTO(ArrayList documentList){
        try{
            HashMap termLoanDocumentTO;
            HashMap documentRecordMap;
            LinkedHashMap allDocumentRecords = new LinkedHashMap();
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
                    documentRecordMap.put(ACCT_NO, getStrACNumber());
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
                
                allDocumentRecords.put(getDocumentKey(CommonUtil.convertObjToStr(termLoanDocumentTO.get(DOC_TYPE)), CommonUtil.convertObjToStr(termLoanDocumentTO.get(DOC_FORM_NO))), documentRecordMap);
                
                documentRecordList = null;
                documentRecordMap = null;
            }
            documentAll.clear();
            documentTabValues.clear();
            documentAll = allDocumentRecords;
            documentTabValues = tabDocumentRecords;
            tblDocumentTab.setDataArrayList(documentTabValues, getDocumentTabTitle());
            tabDocumentRecords = null;
            allDocumentRecords = null;
        }catch(Exception e){
            log.info("Exception in setDocumentTabTitle(): "+e);
            parseException.logException(e,true);
        }
    }
    
    public HashMap setTermLoanDocument(){
        HashMap docMap = new HashMap();
        try{
            TermLoanDocumentTO objTermLoanDocumentTO;
            java.util.Set keySet =  documentAll.keySet();
            Object[] objKeySet = (Object[]) keySet.toArray();
            HashMap oneRecord;
            log.info(documentAll);
            log.info(documentAll.keySet());
            // To set the values for Security Transfer Object
            for (int i = keySet.size() - 1,j = 0;i >= 0;--i,++j){
                oneRecord = (HashMap) documentAll.get(objKeySet[j]);
                
                if (CommonUtil.convertObjToStr(oneRecord.get(COMMAND)).length() != 0){
                    log.info("Command"+oneRecord.get(COMMAND));
                    objTermLoanDocumentTO = new TermLoanDocumentTO();
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
            java.util.Set keySet =  documentAll.keySet();
            Object[] objKeySet = (Object[]) keySet.toArray();
            HashMap oneRecord;
            // To change the Insert command to Update after Save Buttone Pressed
            // Document Details
            for (int i = keySet.size() - 1,j = 0;i >= 0;--i,++j){
                oneRecord = (HashMap) documentAll.get(objKeySet[j]);
                if (oneRecord.get(COMMAND).equals(INSERT)){
                    // If the status is in Insert Mode then change the mode to Update
                    oneRecord.put(COMMAND, UPDATE);
                    documentAll.put(objKeySet[j], oneRecord);
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
            java.util.Set keySet =  documentAll.keySet();
            Object[] objKeySet = (Object[]) keySet.toArray();
            HashMap oneRecord;
            int i = keySet.size() - 1, j = 0;
            /*
             * To check mandatory records are executed
             */
            for (;i >= 0;--i,++j){
                oneRecord = null;
                oneRecord = (HashMap) documentAll.get(objKeySet[j]);
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
    
    public void setBorrowerNo(String borrowerNo){
        this.borrowerNo = borrowerNo;
        setChanged();
    }
    
    public String getBorrowerNo(){
        return this.borrowerNo;
    }
    
    public void setLblProdID_Disp_DocumentDetails(String lblProdID_Disp_DocumentDetails){
        this.lblProdID_Disp_DocumentDetails = lblProdID_Disp_DocumentDetails;
        setChanged();
    }
    
    public String getLblProdID_Disp_DocumentDetails(){
        return this.lblProdID_Disp_DocumentDetails;
    }
    
    public void setLblAcctHead_Disp_DocumentDetails(String lblAcctHead_Disp_DocumentDetails){
        this.lblAcctHead_Disp_DocumentDetails = lblAcctHead_Disp_DocumentDetails;
        setChanged();
    }
    
    public String getLblAcctHead_Disp_DocumentDetails(){
        return this.lblAcctHead_Disp_DocumentDetails;
    }
    
    public void setLblAcctNo_Disp_DocumentDetails(String lblAcctNo_Disp_DocumentDetails){
        this.lblAcctNo_Disp_DocumentDetails = lblAcctNo_Disp_DocumentDetails;
        setChanged();
    }
    
    public String getLblAcctNo_Disp_DocumentDetails(){
        return this.lblAcctNo_Disp_DocumentDetails;
    }
    
    public void setLblDocDesc_Disp_DocumentDetails(String lblDocDesc_Disp_DocumentDetails){
        this.lblDocDesc_Disp_DocumentDetails = lblDocDesc_Disp_DocumentDetails;
        setChanged();
    }
    
    public String getLblDocDesc_Disp_DocumentDetails(){
        return this.lblDocDesc_Disp_DocumentDetails;
    }
    
    public void setLblDocNo_Disp_DocumentDetails(String lblDocNo_Disp_DocumentDetails){
        this.lblDocNo_Disp_DocumentDetails = lblDocNo_Disp_DocumentDetails;
        setChanged();
    }
    
    public String getLblDocNo_Disp_DocumentDetails(){
        return this.lblDocNo_Disp_DocumentDetails;
    }
    
    public void setLblDocType_Disp_DocumentDetails(String lblDocType_Disp_DocumentDetails){
        this.lblDocType_Disp_DocumentDetails = lblDocType_Disp_DocumentDetails;
        setChanged();
    }
    
    public String getLblDocType_Disp_DocumentDetails(){
        return this.lblDocType_Disp_DocumentDetails;
    }
    
    public void setTxtRemarks_DocumentDetails(String txtRemarks_DocumentDetails){
        this.txtRemarks_DocumentDetails = txtRemarks_DocumentDetails;
        setChanged();
    }
    
    public String getTxtRemarks_DocumentDetails(){
        return this.txtRemarks_DocumentDetails;
    }
    
    public void setTdtSubmitDate_DocumentDetails(String tdtSubmitDate_DocumentDetails){
        this.tdtSubmitDate_DocumentDetails = tdtSubmitDate_DocumentDetails;
        setChanged();
    }
    
    public String getTdtSubmitDate_DocumentDetails(){
        return this.tdtSubmitDate_DocumentDetails;
    }
    
    public void setRdoYes_DocumentDetails(boolean rdoYes_DocumentDetails){
        this.rdoYes_DocumentDetails = rdoYes_DocumentDetails;
        setChanged();
    }
    
    public boolean getRdoYes_DocumentDetails(){
        return this.rdoYes_DocumentDetails;
    }
    
    public void setRdoNo_DocumentDetails(boolean rdoNo_DocumentDetails){
        this.rdoNo_DocumentDetails = rdoNo_DocumentDetails;
        setChanged();
    }
    
    public boolean getRdoNo_DocumentDetails(){
        return this.rdoNo_DocumentDetails;
    }
    
    public void setStrACNumber(String strACNumber){
        this.strACNumber = strACNumber;
        setChanged();
    }
    
    public String getStrACNumber(){
        return this.strACNumber;
    }
    
    void setTblDocumentTab(EnhancedTableModel tblDocumentTab){
        this.tblDocumentTab = tblDocumentTab;
        setChanged();
    }
    
    EnhancedTableModel getTblDocumentTab(){
        return this.tblDocumentTab;
    }
    
    public void resetAllDocumentDetails(){
        setLblAcctHead_Disp_DocumentDetails("");
        setLblAcctNo_Disp_DocumentDetails("");
        setLblProdID_Disp_DocumentDetails("");
        resetDocumentDetails();
    }
    
    public void resetDocumentDetails(){
        setLblDocDesc_Disp_DocumentDetails("");
        setLblDocNo_Disp_DocumentDetails("");
        setLblDocType_Disp_DocumentDetails("");
        setTxtRemarks_DocumentDetails("");
        setTdtSubmitDate_DocumentDetails("");
        setRdoNo_DocumentDetails(false);
        setRdoYes_DocumentDetails(false);
        setRdoNo_Executed_DOC(false);
        setRdoYes_Executed_DOC(false);
        setRdoNo_Mandatory_DOC(false);
        setRdoYes_Mandatory_DOC(false);
        setTdtExecuteDate_DOC("");
        setTdtExpiryDate_DOC("");
    }
    
    public void resetDocCTable(){
        tblDocumentTab.setDataArrayList(null, documentTabTitle);
    }
    
    public int saveDocumentTab(int recordPosition){
        int option = -1;
        HashMap result = new HashMap();
        try{
            ArrayList data = tblDocumentTab.getDataArrayList();
            String strDocType = CommonUtil.convertObjToStr(((ArrayList) (tblDocumentTab.getDataArrayList().get(recordPosition))).get(0));
            String strDocNo = CommonUtil.convertObjToStr(((ArrayList) (tblDocumentTab.getDataArrayList().get(recordPosition))).get(1));
            String strRecordKey = getDocumentKey(strDocType, strDocNo);
            HashMap oneRec;
            if (documentAll.containsKey(strRecordKey)){
                oneRec = (HashMap) documentAll.get(strRecordKey);
                if (CommonUtil.convertObjToStr(oneRec.get(COMMAND)).length() == 0){
                    oneRec.put(COMMAND, INSERT);
                }
                oneRec.put(REMARKS, getTxtRemarks_DocumentDetails());
                oneRec.put(SUBMITTED_DT, getTdtSubmitDate_DocumentDetails());
                if (getRdoNo_DocumentDetails()){
                    oneRec.put(IS_SUBMITTED, NO);
                    ((ArrayList) data.get(recordPosition)).set(3, NO);
                }else if (getRdoYes_DocumentDetails()){
                    oneRec.put(IS_SUBMITTED, YES);
                    ((ArrayList) data.get(recordPosition)).set(3, YES);
                }
                if (getRdoNo_Executed_DOC()){
                    oneRec.put(IS_EXECUTED, NO);
                    oneRec.put(DOC_EXEC_DT, "");
                }else if (getRdoYes_Executed_DOC()){
                    oneRec.put(IS_EXECUTED, YES);
                    oneRec.put(DOC_EXEC_DT, DateUtil.getDateMMDDYYYY(getTdtExecuteDate_DOC()));
                }
                if (getRdoNo_Mandatory_DOC()){
                    oneRec.put(IS_MANDATORY, NO);
                }else if (getRdoYes_Mandatory_DOC()){
                    oneRec.put(IS_MANDATORY, YES);
                }
                oneRec.put(DOC_EXP_DT, DateUtil.getDateMMDDYYYY(getTdtExpiryDate_DOC()));
                documentAll.put(strRecordKey, oneRec);
            }
            tblDocumentTab.setDataArrayList(data, documentTabTitle);
        }catch(Exception e){
            log.info("Error in saveDocumentTab(): "+e);
            parseException.logException(e,true);
        }
        result = null;
        return option;
    }
    
    // To create objects
    public void createObject(){
        documentTabValues = new ArrayList();
        tblDocumentTab.setDataArrayList(null, documentTabTitle);
        documentAll = new LinkedHashMap();
    }
    
    public void populateDocumentDetails(int recordPosition){
        try{
            HashMap eachRecs;
            java.util.Set keySet =  documentAll.keySet();
            Object[] objKeySet = (Object[]) keySet.toArray();
            String strDocType = CommonUtil.convertObjToStr(((ArrayList) (tblDocumentTab.getDataArrayList().get(recordPosition))).get(0));
            String strDocNo = CommonUtil.convertObjToStr(((ArrayList) (tblDocumentTab.getDataArrayList().get(recordPosition))).get(1));
            String strRecordKey = getDocumentKey(strDocType, strDocNo);
            // To populate the corresponding record from the Security Details
            for (int i = keySet.size() - 1, j = 0;i >= 0;--i, ++j){
                if (objKeySet[j].equals(strRecordKey)){
                    // To populate the Corresponding record from CTable
                    eachRecs = (HashMap) documentAll.get(objKeySet[j]);
                    
                    if (eachRecs.get(IS_SUBMITTED).equals(YES)){
                        setRdoYes_DocumentDetails(true);
                        setRdoNo_DocumentDetails(false);
                    }else{
                        setRdoNo_DocumentDetails(true);
                        setRdoYes_DocumentDetails(false);
                    }
                    if (eachRecs.get(IS_EXECUTED).equals(YES)){
                        setRdoYes_Executed_DOC(true);
                        setRdoNo_Executed_DOC(false);
                        setTdtExecuteDate_DOC(CommonUtil.convertObjToStr(eachRecs.get(DOC_EXEC_DT)));
                    }else{
                        setRdoYes_Executed_DOC(false);
                        setRdoNo_Executed_DOC(true);
                        setTdtExecuteDate_DOC("");
                    }
                    if (eachRecs.get(IS_MANDATORY).equals(YES)){
                        setRdoYes_Mandatory_DOC(true);
                        setRdoNo_Mandatory_DOC(false);
                    }else{
                        setRdoYes_Mandatory_DOC(false);
                        setRdoNo_Mandatory_DOC(true);
                    }
                    setTdtExpiryDate_DOC(CommonUtil.convertObjToStr(eachRecs.get(DOC_EXP_DT)));
                    setTxtRemarks_DocumentDetails(CommonUtil.convertObjToStr(eachRecs.get(REMARKS)));
                    setTdtSubmitDate_DocumentDetails(CommonUtil.convertObjToStr(eachRecs.get(SUBMITTED_DT)));
                    setLblDocType_Disp_DocumentDetails(CommonUtil.convertObjToStr(eachRecs.get(DOC_TYPE)));
                    setLblDocNo_Disp_DocumentDetails(CommonUtil.convertObjToStr(eachRecs.get(DOC_FORM_NO)));
                    setLblDocDesc_Disp_DocumentDetails(CommonUtil.convertObjToStr(eachRecs.get(DOC_DESC)));
                    
                    break;
                }
                eachRecs = null;
            }
            keySet = null;
            objKeySet = null;
            setChanged();
            ttNotifyObservers();
        }catch(Exception e){
            log.info("Error in populateDocumentDetails(): "+e);
            parseException.logException(e,true);
        }
    }
    
    public void ttNotifyObservers(){
        notifyObservers();
    }
    
    // To destroy Objects
    public void destroyObjects(){
        documentTabValues = null;
        documentAll = null;
    }
    
    /**
     * Getter for property tdtExpiryDate_DOC.
     * @return Value of property tdtExpiryDate_DOC.
     */
    public java.lang.String getTdtExpiryDate_DOC() {
        return tdtExpiryDate_DOC;
    }
    
    /**
     * Setter for property tdtExpiryDate_DOC.
     * @param tdtExpiryDate_DOC New value of property tdtExpiryDate_DOC.
     */
    public void setTdtExpiryDate_DOC(java.lang.String tdtExpiryDate_DOC) {
        this.tdtExpiryDate_DOC = tdtExpiryDate_DOC;
    }
    
    /**
     * Getter for property tdtExecuteDate_DOC.
     * @return Value of property tdtExecuteDate_DOC.
     */
    public java.lang.String getTdtExecuteDate_DOC() {
        return tdtExecuteDate_DOC;
    }
    
    /**
     * Setter for property tdtExecuteDate_DOC.
     * @param tdtExecuteDate_DOC New value of property tdtExecuteDate_DOC.
     */
    public void setTdtExecuteDate_DOC(java.lang.String tdtExecuteDate_DOC) {
        this.tdtExecuteDate_DOC = tdtExecuteDate_DOC;
    }
    
    /**
     * Getter for property rdoNo_Executed_DOC.
     * @return Value of property rdoNo_Executed_DOC.
     */
    public boolean getRdoNo_Executed_DOC() {
        return rdoNo_Executed_DOC;
    }
    
    /**
     * Setter for property rdoNo_Executed_DOC.
     * @param rdoNo_Executed_DOC New value of property rdoNo_Executed_DOC.
     */
    public void setRdoNo_Executed_DOC(boolean rdoNo_Executed_DOC) {
        this.rdoNo_Executed_DOC = rdoNo_Executed_DOC;
    }
    
    /**
     * Getter for property rdoYes_Executed_DOC.
     * @return Value of property rdoYes_Executed_DOC.
     */
    public boolean getRdoYes_Executed_DOC() {
        return rdoYes_Executed_DOC;
    }
    
    /**
     * Setter for property rdoYes_Executed_DOC.
     * @param rdoYes_Executed_DOC New value of property rdoYes_Executed_DOC.
     */
    public void setRdoYes_Executed_DOC(boolean rdoYes_Executed_DOC) {
        this.rdoYes_Executed_DOC = rdoYes_Executed_DOC;
    }
    
    /**
     * Getter for property rdoNo_Mandatory_DOC.
     * @return Value of property rdoNo_Mandatory_DOC.
     */
    public boolean getRdoNo_Mandatory_DOC() {
        return rdoNo_Mandatory_DOC;
    }
    
    /**
     * Setter for property rdoNo_Mandatory_DOC.
     * @param rdoNo_Mandatory_DOC New value of property rdoNo_Mandatory_DOC.
     */
    public void setRdoNo_Mandatory_DOC(boolean rdoNo_Mandatory_DOC) {
        this.rdoNo_Mandatory_DOC = rdoNo_Mandatory_DOC;
    }
    
    /**
     * Getter for property rdoYes_Mandatory_DOC.
     * @return Value of property rdoYes_Mandatory_DOC.
     */
    public boolean getRdoYes_Mandatory_DOC() {
        return rdoYes_Mandatory_DOC;
    }
    
    /**
     * Setter for property rdoYes_Mandatory_DOC.
     * @param rdoYes_Mandatory_DOC New value of property rdoYes_Mandatory_DOC.
     */
    public void setRdoYes_Mandatory_DOC(boolean rdoYes_Mandatory_DOC) {
        this.rdoYes_Mandatory_DOC = rdoYes_Mandatory_DOC;
    }
    
}
