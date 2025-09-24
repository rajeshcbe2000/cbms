/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition.
 * 
 *
 * TermLoanInterestOB.java
 *
 * Created on July 5, 2004, 4:27 PM
 */

package com.see.truetransact.ui.bills;

/**
 *
 * @author  shanmuga
 *
 */

import com.see.truetransact.clientproxy.ProxyParameters;
import com.see.truetransact.clientutil.CMandatoryDialog;
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
import com.see.truetransact.transferobject.bills.TermLoanInterestTO;
import com.see.truetransact.ui.TrueTransactMain;
import com.see.truetransact.uicomponent.CObservable;
import com.see.truetransact.uicomponent.COptionPane;

import org.apache.log4j.Logger;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;

public class TermLoanInterestOB extends CObservable{
    
    /** Creates a new instance of TermLoanInterestOB */
    private TermLoanInterestOB() throws Exception{
        curDate = ClientUtil.getCurrentDate();
        termLoanInterestOB();
    }
    
    private       static TermLoanInterestOB termLoanInterestOB;
    
    private final static Logger log = Logger.getLogger(TermLoanInterestOB.class);
    private final static ClientParseException parseException = ClientParseException.getInstance();
    
    private final   String  ACCOUNT_NO = "ACCOUNT_NO";
    private final   String  AGAINST_CLEAR_INT = "AGAINST_CLEAR_INT";
    private final   String  ALL_VALUES = "ALL_VALUES";
    private final   String  COMMAND = "COMMAND";
    private final   String  FROM_AMOUNT = "FROM_AMOUNT";
    private final   String  FROM_DATE = "FROM_DATE";
    private final   String  INSERT = "INSERT";
    private final   String  INTER_EXP_LIMIT = "INTER_EXP_LIMIT";
    private final   String  INTEREST = "INTEREST";
    private final   String  LIMIT_EXPIRY_INT = "LIMIT_EXPIRY_INT";
    private final   String  NO = "N";
    private final   String  OPTION = "OPTION";
    private final   String  PENAL_APPL = "PENAL_APPL";
    private final   String  PENAL_INT_RATE = "PENAL_INT_RATE";
    private final   String  PENAL_STATEMENT = "PENAL_STATEMENT";
    private final   String  PENALTY_INTEREST = "PENALTY_INTEREST";
    private final   String  SLNO = "SLNO";
    private final   String  TABLE_VALUES = "TABLE_VALUES";
    private final   String  TO_AMOUNT = "TO_AMOUNT";
    private final   String  TO_DATE = "TO_DATE";
    private final   String  UPDATE = "UPDATE";
    private final   String  YES = "Y";
    
    private final   java.util.ResourceBundle objTermLoanRB = java.util.ResourceBundle.getBundle("com.see.truetransact.ui.bills.TermLoanRB", ProxyParameters.LANGUAGE);
    
    private final   ArrayList interestTabTitle = new ArrayList();       //  Table Title of Interest
    private ArrayList interestTabValues = new ArrayList();             // ArrayList to display in Guarantor Table
    private ArrayList interestEachTabRecord;
    
    private EnhancedTableModel tblInterestTab;
    
    private TableUtil tableUtilInterest = new TableUtil();
    
    private LinkedHashMap interestAll = new LinkedHashMap();           // Both displayed and hidden values in the table
    private HashMap interestEachRecord;
    
    private String borrowerNo = "";
    private String strACNumber = "";
    private String lblAccHead_IM_2 = "";
    private String lblProdID_IM_Disp = "";
    private String lblAccNo_IM_2 = "";
    private String lblLimitAmt_2 = "";
    private String lblPLR_Limit_2 = "";
    private String lblSancDate_2 = "";
    private String lblExpiryDate_2 = "";
    private String tdtFrom = "";
    private String tdtTo = "";
    private String txtFromAmt = "";
    private String txtToAmt = "";
    private String txtInter = "";
    private String txtPenalInter = "";
    private String penalInter = "";
    private String txtAgainstClearingInter = "";
    private String txtPenalStatement = "";
    private String txtInterExpLimit = "";
    private String loanType="";
    private String depositNo="";
    private boolean isNew=false;
    Date curDate = null;
    
    private boolean enableInterExpLimit = true;
    static {
        try {
            termLoanInterestOB = new TermLoanInterestOB();
        } catch(Exception e) {
            log.info("try: " + e);
            parseException.logException(e,true);
        }
    }
    
    public static TermLoanInterestOB getInstance() {
        return termLoanInterestOB;
    }
    
    private void termLoanInterestOB() throws Exception{
        setInterestTabTitle();
        tableUtilInterest.setAttributeKey(SLNO);
        tblInterestTab = new EnhancedTableModel(null, interestTabTitle);
    }
    
    private void setInterestTabTitle() throws Exception{
        try{
            interestTabTitle.add(objTermLoanRB.getString("tblColumnInterestNo"));
            interestTabTitle.add(objTermLoanRB.getString("tblColumnInterestFromDate"));
            interestTabTitle.add(objTermLoanRB.getString("tblColumnInterestToDate"));
            interestTabTitle.add(objTermLoanRB.getString("tblColumnInterestFromAmt"));
            interestTabTitle.add(objTermLoanRB.getString("tblColumnInterestToAmt"));
            interestTabTitle.add(objTermLoanRB.getString("tblColumnInterestInterest"));
        }catch(Exception e){
            log.info("Exception in setInterestTabTitle(): "+e);
            parseException.logException(e,true);
        }
    }
    
    public ArrayList getInterestTabTitle(){
        return interestTabTitle;
    }
    
    public void resetAllInterestDetails(){
        resetInterestDetails();
        setLblAccHead_IM_2("");
        setLblAccNo_IM_2("");
        setLblProdID_IM_Disp("");
        setLblSancDate_2("");
        setLblLimitAmt_2("");
        setLblExpiryDate_2("");
        setLblPLR_Limit_2("");
    }
    
    public void resetInterestDetails(){
        setTdtFrom("");
        setTdtTo("");
        setTxtFromAmt("");
        setTxtToAmt("");
        setTxtInter("");
        setTxtPenalInter("");
        setTxtAgainstClearingInter("");
        setTxtPenalStatement("");
        setTxtInterExpLimit("");
    }
    
    public void resetInterestCTable(){
        tblInterestTab.setDataArrayList(null, interestTabTitle);
        tableUtilInterest = new TableUtil();
        tableUtilInterest.setAttributeKey(SLNO);
    }
    
    public void changeStatusInterest(int resultType){
        try{
            HashMap oneRecord;
            if (resultType != 2){
                //If the Main Save Button pressed
                tableUtilInterest.getRemovedValues().clear();
            }
            java.util.Set keySet =  interestAll.keySet();
            Object[] objKeySet = (Object[]) keySet.toArray();
            // To change the Insert command to Update after Save Buttone Pressed
            // Interest Details
            for (int i = keySet.size() - 1,j = 0;i >= 0;--i,++j){
                oneRecord = (HashMap) interestAll.get(objKeySet[j]);
                if (oneRecord.get(COMMAND).equals(INSERT)){
                    // If the status is in Insert Mode then change the mode to Update
                    oneRecord.put(COMMAND, UPDATE);
                    interestAll.put(objKeySet[j], oneRecord);
                }
                oneRecord = null;
            }
            keySet = null;
            objKeySet = null;
        }catch(Exception e){
            log.info("Exception in changeStatusInterest(): "+e);
            parseException.logException(e,true);
        }
    }
    
    public void setTermLoanInterestTO(ArrayList interestList, String acctNum){
        try{
            TermLoanInterestTO termLoanInterestTO;
            HashMap interestRecordMap;
            LinkedHashMap allInterestRecords = new LinkedHashMap();
            ArrayList interestRecordList;
            ArrayList tabInterestRecords = new ArrayList();
            ArrayList removedValues = new ArrayList();
            String strSerialNo = "";
            // To retrieve the Interest Details from the Serverside
            for (int i = interestList.size() - 1,j = 0;i >= 0;--i,++j){
                termLoanInterestTO = (TermLoanInterestTO) interestList.get(j);
                interestRecordMap = new HashMap();
                interestRecordList = new ArrayList();
                
                if (CommonUtil.convertObjToStr(termLoanInterestTO.getSlno()).length() > 0){
                    strSerialNo = CommonUtil.convertObjToStr(termLoanInterestTO.getSlno());
                }else{
                    strSerialNo = String.valueOf(j + 1);
                }
                interestRecordList.add(strSerialNo);
                interestRecordList.add(DateUtil.getStringDate(termLoanInterestTO.getFromDt()));
                interestRecordList.add(DateUtil.getStringDate(termLoanInterestTO.getToDt()));
                interestRecordList.add(CommonUtil.convertObjToStr(termLoanInterestTO.getFromAmt()));
                interestRecordList.add(CommonUtil.convertObjToStr(termLoanInterestTO.getToAmt()));
                double prodInt = CommonUtil.convertObjToDouble(termLoanInterestTO.getInterest()).doubleValue();
                if (loanType.equals("LTD") && isNew){
                    HashMap hash=new HashMap();
     
                    hash.put(ACCOUNT_NO, getDepositNo());
                    List lst=ClientUtil.executeQuery("getIntRateforDeposit", hash);
                    if(lst.size()>0)
                    hash=(HashMap)lst.get(0);
                    
                    double intrate=CommonUtil.convertObjToDouble(hash.get("RATE_OF_INT")).doubleValue();
                    termLoanInterestTO.setInterest(new Double(prodInt+intrate));
                }
                interestRecordList.add(CommonUtil.convertObjToStr(termLoanInterestTO.getInterest()));

                tabInterestRecords.add(interestRecordList);
                
                interestRecordMap.put(SLNO, strSerialNo);
                interestRecordMap.put(FROM_DATE, DateUtil.getStringDate(termLoanInterestTO.getFromDt()));
                interestRecordMap.put(TO_DATE, DateUtil.getStringDate(termLoanInterestTO.getToDt()));
                interestRecordMap.put(FROM_AMOUNT, CommonUtil.convertObjToStr(termLoanInterestTO.getFromAmt()));
                interestRecordMap.put(TO_AMOUNT, CommonUtil.convertObjToStr(termLoanInterestTO.getToAmt()));
                interestRecordMap.put(INTEREST, CommonUtil.convertObjToStr(termLoanInterestTO.getInterest()));
                interestRecordMap.put(PENALTY_INTEREST, CommonUtil.convertObjToStr(termLoanInterestTO.getPenalInterest()));
                interestRecordMap.put(AGAINST_CLEAR_INT, CommonUtil.convertObjToStr(termLoanInterestTO.getAgainstClearingInt()));
                interestRecordMap.put(PENAL_STATEMENT, CommonUtil.convertObjToStr(termLoanInterestTO.getStatementPenal()));
                interestRecordMap.put(INTER_EXP_LIMIT, CommonUtil.convertObjToStr(termLoanInterestTO.getInterestExpiryLimit()));
                interestRecordMap.put(ACCOUNT_NO, CommonUtil.convertObjToStr(termLoanInterestTO.getAcctNum()));
                
                if (loanType.equals("LTD") && isNew)
                    interestRecordMap.put(COMMAND, INSERT);
                else
                    interestRecordMap.put(COMMAND, UPDATE);
                
                allInterestRecords.put(strSerialNo, interestRecordMap);
                                
                interestRecordList = null;
                interestRecordMap = null;
                strSerialNo = null;
            }
            interestAll.clear();
            interestTabValues.clear();
            
            interestAll = allInterestRecords;
            interestTabValues = tabInterestRecords;
            
            tblInterestTab.setDataArrayList(interestTabValues, interestTabTitle);
            tableUtilInterest.setRemovedValues(removedValues);
            tableUtilInterest.setAllValues(interestAll);
            tableUtilInterest.setTableValues(interestTabValues);
            if (acctNum != null){
                setMax_Del_Interest_No(acctNum);
            }
            tabInterestRecords = null;
            allInterestRecords = null;
            strSerialNo = null;
            isNew = false;
        }catch(Exception e){
            log.info("Error in setTermLoanInterestTO()..."+e);
            parseException.logException(e,true);
        }
    }
    
    public void populateFromDate(){
        try{
            String strLastToDate = CommonUtil.convertObjToStr(((ArrayList)interestTabValues.get(interestTabValues.size() - 1)).get(2));
            Date lastToDate = DateUtil.getDateMMDDYYYY(strLastToDate);
            Date nextFromDate = DateUtil.addDays(lastToDate, 1);
            setTdtFrom(DateUtil.getStringDate(nextFromDate));
            strLastToDate = null;
            lastToDate = null;
            nextFromDate = null;
        }catch(Exception e){
            log.info("Error in populateFromDate() "+e);
            parseException.logException(e,true);
        }
    }
    
    private void setMax_Del_Interest_No(String acctNum){
        try{
            HashMap transactionMap = new HashMap();
            HashMap retrieve = new HashMap();
            transactionMap.put("acctNum", acctNum);
            List resultList = ClientUtil.executeQuery("getSelectBillsInterestMaxSLNO", transactionMap);
            if (resultList.size() > 0){
                retrieve = (HashMap) resultList.get(0);
                tableUtilInterest.setStr_MAX_DEL_SL_NO(CommonUtil.convertObjToStr(retrieve.get("MAX_SL_NO")));
            }
            retrieve = null;
            transactionMap = null;
            resultList = null;
        }catch(Exception e){
            log.info("Error In setMax_Del_Interest_No: "+e);
            parseException.logException(e,true);
        }
    }
    
    public HashMap setTermLoanInterest(){
        HashMap interestMap = new HashMap();
        try{
            TermLoanOB termLoanOB = TermLoanOB.getInstance();
            TermLoanInterestTO objTermLoanInterestTO;
            java.util.Set keySet =  interestAll.keySet();
            Object[] objKeySet = (Object[]) keySet.toArray();
            HashMap oneRecord;
            if (!(termLoanOB.getCbmIntGetFrom().getKeyForSelected().equals("PROD") || termLoanOB.getCbmIntGetFrom().getKeyForSelected().equals(""))){
                // To set the values for Interest Transfer Object
                for (int i = keySet.size() - 1,j = 0;i >= 0;--i,++j){
                    oneRecord = (HashMap) interestAll.get(objKeySet[j]);
                    objTermLoanInterestTO = new TermLoanInterestTO();
                    objTermLoanInterestTO.setSlno(CommonUtil.convertObjToDouble( oneRecord.get(SLNO)));
//                    objTermLoanInterestTO.setBorrowNo(getBorrowerNo());
                    objTermLoanInterestTO.setAcctNum(getStrACNumber());
                    objTermLoanInterestTO.setAgainstClearingInt(CommonUtil.convertObjToDouble(oneRecord.get(AGAINST_CLEAR_INT)));
                    objTermLoanInterestTO.setCommand(CommonUtil.convertObjToStr(oneRecord.get(COMMAND)));
                    objTermLoanInterestTO.setFromAmt(CommonUtil.convertObjToDouble(oneRecord.get(FROM_AMOUNT)));
                    
//                    objTermLoanInterestTO.setFromDt((Date)oneRecord.get(FROM_DATE));
                    objTermLoanInterestTO.setFromDt(DateUtil.getDateMMDDYYYY(CommonUtil.convertObjToStr(oneRecord.get(FROM_DATE))));
                    objTermLoanInterestTO.setInterest(CommonUtil.convertObjToDouble(oneRecord.get(INTEREST)));
                    objTermLoanInterestTO.setInterestExpiryLimit(CommonUtil.convertObjToDouble(oneRecord.get(INTER_EXP_LIMIT)));
                    objTermLoanInterestTO.setStatementPenal(CommonUtil.convertObjToDouble(oneRecord.get(PENAL_STATEMENT)));
                    objTermLoanInterestTO.setPenalInterest(CommonUtil.convertObjToDouble(oneRecord.get(PENALTY_INTEREST)));
                    objTermLoanInterestTO.setStatus(CommonUtil.convertObjToStr(oneRecord.get(COMMAND)));
                    objTermLoanInterestTO.setToAmt(CommonUtil.convertObjToDouble(oneRecord.get(TO_AMOUNT)));
                    
//                    objTermLoanInterestTO.setToDt((Date)oneRecord.get(TO_DATE));
                    objTermLoanInterestTO.setToDt(DateUtil.getDateMMDDYYYY(CommonUtil.convertObjToStr(oneRecord.get(TO_DATE))));
                    if (oneRecord.get(COMMAND).equals(INSERT)){
                        objTermLoanInterestTO.setStatus(CommonConstants.STATUS_CREATED);
                    }else if (oneRecord.get(COMMAND).equals(UPDATE)){
                        objTermLoanInterestTO.setStatus(CommonConstants.STATUS_MODIFIED);
                    }
                    objTermLoanInterestTO.setStatusBy(TrueTransactMain.USER_ID);
                    objTermLoanInterestTO.setStatusDt(curDate);
                    interestMap.put(String.valueOf(j+1), objTermLoanInterestTO);

                    oneRecord = null;
                    objTermLoanInterestTO = null;
                }
            }
            
            
            // To set the values for Interest Transfer Object
            // where as the existing records in Database are deleted in client side
            // useful for updating the status
            for (int i = tableUtilInterest.getRemovedValues().size() - 1,j = 0;i >= 0;--i,++j){
                oneRecord = (HashMap) tableUtilInterest.getRemovedValues().get(j);
                objTermLoanInterestTO = new TermLoanInterestTO();
                objTermLoanInterestTO.setSlno(CommonUtil.convertObjToDouble( oneRecord.get(SLNO)));
//                objTermLoanInterestTO.setBorrowNo(getBorrowerNo());
                objTermLoanInterestTO.setAcctNum(getStrACNumber());
                objTermLoanInterestTO.setAgainstClearingInt(CommonUtil.convertObjToDouble(oneRecord.get(AGAINST_CLEAR_INT)));
                objTermLoanInterestTO.setCommand(CommonUtil.convertObjToStr(oneRecord.get(COMMAND)));
                objTermLoanInterestTO.setFromAmt(CommonUtil.convertObjToDouble(oneRecord.get(FROM_AMOUNT)));
                
//                objTermLoanInterestTO.setFromDt((Date)oneRecord.get(FROM_DATE));
                objTermLoanInterestTO.setFromDt(DateUtil.getDateMMDDYYYY(CommonUtil.convertObjToStr(oneRecord.get(FROM_DATE))));
                objTermLoanInterestTO.setInterest(CommonUtil.convertObjToDouble(oneRecord.get(INTEREST)));
                objTermLoanInterestTO.setInterestExpiryLimit(CommonUtil.convertObjToDouble(oneRecord.get(INTER_EXP_LIMIT)));
                objTermLoanInterestTO.setStatementPenal(CommonUtil.convertObjToDouble(oneRecord.get(PENAL_STATEMENT)));
                objTermLoanInterestTO.setPenalInterest(CommonUtil.convertObjToDouble(oneRecord.get(PENALTY_INTEREST)));
                objTermLoanInterestTO.setStatus(CommonUtil.convertObjToStr(oneRecord.get(COMMAND)));
                objTermLoanInterestTO.setToAmt(CommonUtil.convertObjToDouble(oneRecord.get(TO_AMOUNT)));
                
//                objTermLoanInterestTO.setToDt((Date)oneRecord.get(TO_DATE));
                objTermLoanInterestTO.setToDt(DateUtil.getDateMMDDYYYY(CommonUtil.convertObjToStr(oneRecord.get(TO_DATE))));
                objTermLoanInterestTO.setStatus(CommonConstants.STATUS_DELETED);
                objTermLoanInterestTO.setStatusBy(TrueTransactMain.USER_ID);
                objTermLoanInterestTO.setStatusDt(curDate);
                interestMap.put(String.valueOf(interestMap.size()+1), objTermLoanInterestTO);
                
                oneRecord = null;
                objTermLoanInterestTO = null;
            }
        }catch(Exception e){
            log.info("Exception caught in setTermLoanInterest: "+e);
            parseException.logException(e,true);
        }
        return interestMap;
    }
    
    public void setValByProdID(){
        try{
            TermLoanOB termLoanOB = TermLoanOB.getInstance();
            HashMap transactionMap = new HashMap();
            HashMap retrieve;
            transactionMap.put("PROD_ID", termLoanOB.getCbmProductId().getKeyForSelected());
            List resultList = (List) ClientUtil.executeQuery("getProdIntDetails", transactionMap);
            if (resultList.size() > 0){
                // If the Product ID exist in Database
                retrieve = (HashMap) resultList.get(0);
                if (retrieve.containsKey(PENAL_APPL)){
                    if (retrieve.get(PENAL_APPL).equals(YES)){
                        setPenalInter(CommonUtil.convertObjToStr(retrieve.get(PENAL_INT_RATE)));
                    }else{
                        setPenalInter("");
                    }
                }else{
                    setPenalInter("");
                }
                if (retrieve.containsKey(LIMIT_EXPIRY_INT)){
                    if (retrieve.get(LIMIT_EXPIRY_INT).equals(NO)){
                        setEnableInterExpLimit(false);
                    }else{
                        setEnableInterExpLimit(true);
                    }
                }else{
                    setEnableInterExpLimit(true);
                }
            }
            termLoanOB = null;
            transactionMap = null;
            resultList = null;
        }catch(Exception e){
            log.info("Exception caught in setValByProdID: "+e);
            parseException.logException(e,true);
        }
    }
    
    public String checkLastInterestDate(int tableCount){
        String lastIntDate = "";
        try{
            HashMap lastRecord = (HashMap) interestAll.get(((ArrayList) tblInterestTab.getDataArrayList().get(tableCount-1)).get(0));
            lastIntDate = CommonUtil.convertObjToStr(lastRecord.get(TO_DATE));
        }catch(Exception e){
            log.info("Exception caught in checkLastInterestDate: "+e);
            parseException.logException(e,true);
        }
        return lastIntDate;
    }
    
    private void setEnableInterExpLimit(boolean enableInterExpLimit){
        this.enableInterExpLimit = enableInterExpLimit;
        setChanged();
    }
    
    public boolean getEnableInterExpLimit(){
        return this.enableInterExpLimit ;
    }
    
    public void setStrACNumber(String strACNumber){
        this.strACNumber = strACNumber;
        setChanged();
    }
    
    public String getStrACNumber(){
        return this.strACNumber;
    }
    
    void setTblInterestTab(EnhancedTableModel tblInterestTab){
        log.info("In setTblInterestTab()...");
        
        this.tblInterestTab = tblInterestTab;
        setChanged();
    }
    
    EnhancedTableModel getTblInterestTab(){
        return this.tblInterestTab;
    }
    
    public void setLblPLR_Limit_2(String lblPLR_Limit_2){
        this.lblPLR_Limit_2 = lblPLR_Limit_2;
        setChanged();
    }
    
    public String getLblPLR_Limit_2(){
        return this.lblPLR_Limit_2;
    }
    
    public void setBorrowerNo(String borrowerNo){
        this.borrowerNo = borrowerNo;
        setChanged();
    }
    
    public String getBorrowerNo(){
        return this.borrowerNo;
    }
    
    void setLblProdID_IM_Disp(String lblProdID_IM_Disp){
        this.lblProdID_IM_Disp = lblProdID_IM_Disp;
        setChanged();
    }
    String getLblProdID_IM_Disp(){
        return this.lblProdID_IM_Disp;
    }
    
    void setTdtFrom(String tdtFrom){
        this.tdtFrom = tdtFrom;
        setChanged();
    }
    String getTdtFrom(){
        return this.tdtFrom;
    }
    
    void setTdtTo(String tdtTo){
        this.tdtTo = tdtTo;
        setChanged();
    }
    String getTdtTo(){
        return this.tdtTo;
    }
    
    void setTxtFromAmt(String txtFromAmt){
        this.txtFromAmt = txtFromAmt;
        setChanged();
    }
    String getTxtFromAmt(){
        return this.txtFromAmt;
    }
    
    void setTxtToAmt(String txtToAmt){
        this.txtToAmt = txtToAmt;
        setChanged();
    }
    String getTxtToAmt(){
        return this.txtToAmt;
    }
    
    void setTxtInter(String txtInter){
        this.txtInter = txtInter;
        setChanged();
    }
    String getTxtInter(){
        return this.txtInter;
    }
    
    void setTxtPenalInter(String txtPenalInter){
        this.txtPenalInter = txtPenalInter;
        setChanged();
    }
    String getTxtPenalInter(){
        return this.txtPenalInter;
    }
    
    void setPenalInter(String penalInter){
        this.penalInter = penalInter;
        setChanged();
    }
    String getPenalInter(){
        return this.penalInter;
    }
    
    void setTxtAgainstClearingInter(String txtAgainstClearingInter){
        this.txtAgainstClearingInter = txtAgainstClearingInter;
        setChanged();
    }
    String getTxtAgainstClearingInter(){
        return this.txtAgainstClearingInter;
    }
    
    void setTxtInterExpLimit(String txtInterExpLimit){
        this.txtInterExpLimit = txtInterExpLimit;
        setChanged();
    }
    String getTxtInterExpLimit(){
        return this.txtInterExpLimit;
    }
    
    
    public void setLblAccHead_IM_2(String lblAccHead_IM_2){
        this.lblAccHead_IM_2 = lblAccHead_IM_2;
        setChanged();
    }
    
    public String getLblAccHead_IM_2(){
        return this.lblAccHead_IM_2;
    }
    
    public void setLblAccNo_IM_2(String lblAccNo_IM_2){
        this.lblAccNo_IM_2 = lblAccNo_IM_2;
        setChanged();
    }
    
    public String getLblAccNo_IM_2(){
        return this.lblAccNo_IM_2;
    }
    
    public void setLblSancDate_2(String lblSancDate_2){
        this.lblSancDate_2 = lblSancDate_2;
        setChanged();
    }
    
    public String getLblSancDate_2(){
        return this.lblSancDate_2;
    }
    
    public void setLblExpiryDate_2(String lblExpiryDate_2){
        this.lblExpiryDate_2 = lblExpiryDate_2;
        setChanged();
    }
    
    public String getLblExpiryDate_2(){
        return this.lblExpiryDate_2;
    }
    
    public void setLblLimitAmt_2(String lblLimitAmt_2){
        this.lblLimitAmt_2 = lblLimitAmt_2;
        setChanged();
    }
    
    public String getLblLimitAmt_2(){
        return this.lblLimitAmt_2;
    }
    
    public void ttNotifyObservers(){
        notifyObservers();
    }
    
    public ArrayList getInterestDetails(String fromDate, String toDate, Date loanToDate){
        ArrayList returnValue = new ArrayList();
        try{
            HashMap eachRecs;
            HashMap interestRateAndPeriod;
            java.util.Set keySet =  interestAll.keySet();
            Object[] objKeySet = (Object[]) keySet.toArray();
            Date repayFromDate = DateUtil.getDateMMDDYYYY(fromDate);
            Date repayToDate = DateUtil.getDateMMDDYYYY(toDate);
            Date interestFromDate;
            Date interestToDate;
            boolean gotFinalIntRate = false;
            for (int i = keySet.size() - 1,j = 0;i >= 0;--i,++j){
                interestRateAndPeriod = new HashMap();
                eachRecs = (HashMap) interestAll.get(objKeySet[j]);
                interestFromDate = DateUtil.getDateMMDDYYYY(CommonUtil.convertObjToStr(eachRecs.get(FROM_DATE)));
                
                if ((eachRecs.get(TO_DATE) == null) || (CommonUtil.convertObjToStr(eachRecs.get(TO_DATE)).length() == 0)){
                    interestToDate = loanToDate;
                }else{
                    interestToDate = DateUtil.getDateMMDDYYYY(CommonUtil.convertObjToStr(eachRecs.get(TO_DATE)));
                }
                
                if (repayFromDate.compareTo(interestFromDate) >= 0 && interestToDate.compareTo(repayFromDate) > 0){
                    interestRateAndPeriod.put(INTEREST, eachRecs.get(INTEREST));
                    interestRateAndPeriod.put(FROM_DATE, DateUtil.getStringDate(repayFromDate));
                }else if ((interestFromDate.compareTo(repayFromDate) >= 0 && interestToDate.compareTo(repayToDate) < 0) || (repayToDate.compareTo(interestFromDate) >= 0 && repayToDate.compareTo(interestToDate) < 0)){
                    interestRateAndPeriod.put(INTEREST, eachRecs.get(INTEREST));
                    interestRateAndPeriod.put(FROM_DATE, eachRecs.get(FROM_DATE));
                }else{
                    interestRateAndPeriod = null;
                }
                if (interestRateAndPeriod != null){
                    returnValue.add(interestRateAndPeriod);
                }
                interestRateAndPeriod = null;
                interestFromDate = null;
                interestToDate = null;
                eachRecs = null;
            }
            eachRecs = null;
            keySet = null;
            objKeySet = null;
        }catch(Exception e){
            log.info("Error in getInterestDetails(): "+e);
            parseException.logException(e,true);  
        }
        return returnValue;
    }
    
    public int addInterestDetails(int recordPosition, boolean update){
        int option = -1;
        try{
            log.info("Add Interest Details...");
            boolean dateRange = true;
            boolean amountRange = true;
            
            interestEachTabRecord = new ArrayList();
            interestEachRecord = new HashMap();
            HashMap result = new HashMap();
            ArrayList data = tblInterestTab.getDataArrayList();
            tblInterestTab.setDataArrayList(data, interestTabTitle);
            final int dataSize = data.size();
            insertInterest(dataSize+1);
            HashMap eachRecs;
            java.util.Set keySet =  interestAll.keySet();
            Object[] objKeySet = (Object[]) keySet.toArray();
            int rowSelected = -1;
            
            if(keySet.size() > 0){
                for (int i = keySet.size() - 1,j = 0;i >= 0;--i,++j){
                    eachRecs = (HashMap) interestAll.get(objKeySet[j]);
                    //__ Check for the Duplication of Date
                    if (!( DateUtil.getDateMMDDYYYY(getTdtFrom()).before(DateUtil.getDateMMDDYYYY(CommonUtil.convertObjToStr(eachRecs.get(FROM_DATE))))
                    && DateUtil.getDateMMDDYYYY(getTdtTo()).before(DateUtil.getDateMMDDYYYY(CommonUtil.convertObjToStr(eachRecs.get(FROM_DATE)))))) {
                        if (CommonUtil.convertObjToStr(eachRecs.get(TO_DATE)).length() > 0) {
                            if (!(DateUtil.getDateMMDDYYYY(getTdtFrom()).after(DateUtil.getDateMMDDYYYY(CommonUtil.convertObjToStr(eachRecs.get(TO_DATE)))) &&
                            DateUtil.getDateMMDDYYYY(getTdtTo()).after(DateUtil.getDateMMDDYYYY(CommonUtil.convertObjToStr(eachRecs.get(TO_DATE))))))
                                dateRange = false;
                        }
                    }
                    
                    // setFromAmount, setToAmount, setFromPeriod, setToPeriod
                    if(((CommonUtil.convertObjToDouble(eachRecs.get(FROM_AMOUNT)).doubleValue() < CommonUtil.convertObjToDouble(interestEachRecord.get(FROM_AMOUNT)).doubleValue())
                    && (CommonUtil.convertObjToDouble(eachRecs.get(TO_AMOUNT)).doubleValue() < CommonUtil.convertObjToDouble(interestEachRecord.get(FROM_AMOUNT)).doubleValue()))
                    ||((CommonUtil.convertObjToDouble(eachRecs.get(FROM_AMOUNT)).doubleValue() > CommonUtil.convertObjToDouble(interestEachRecord.get(TO_AMOUNT)).doubleValue())
                    && (CommonUtil.convertObjToDouble(eachRecs.get(TO_AMOUNT)).doubleValue() > CommonUtil.convertObjToDouble(interestEachRecord.get(TO_AMOUNT)).doubleValue()))){
                        amountRange = true;
                    }else{
                        amountRange = false;
                    }
                    
                    /** If the Record(s) Already Exists in the Table, Display the Alert...
                     */
                    if(!dateRange && !amountRange && j != recordPosition){
                        rowSelected = i;
                        String[] options = {objTermLoanRB.getString("cDialogOk"),objTermLoanRB.getString("cDialogCancel")};
                        option = COptionPane.showOptionDialog(null, objTermLoanRB.getString("ROI_Existence_Warn"), CommonConstants.WARNINGTITLE,
                        COptionPane.YES_NO_CANCEL_OPTION, COptionPane.WARNING_MESSAGE, null, options, options[0]);
                        
                        break;
                    }
                }
            }else{
                //__ If there is no row in the Table, Add the Row...
                dateRange   = true;
                amountRange  = true;
            }
            
            if (option == -1){
                if (!update) {
                    // If the table is not in Edit Mode
                    result = tableUtilInterest.insertTableValues(interestEachTabRecord, interestEachRecord);

                    interestTabValues = (ArrayList) result.get(TABLE_VALUES);
                    interestAll = (LinkedHashMap) result.get(ALL_VALUES);
                    option = CommonUtil.convertObjToInt(result.get(OPTION));

                    tblInterestTab.setDataArrayList(interestTabValues, interestTabTitle);
                }else{
                    option = updateInterestTab(recordPosition);
                }
            }
            
            setChanged();
            
            interestEachTabRecord = null;
            interestEachRecord = null;
            result = null;
            data = null;
        }catch(Exception e){
            log.info("Error in addInterestDetails()..."+e);
            parseException.logException(e,true);
        }
        return option;
    }
    
    private void insertInterest(int recordPosition){
        interestEachTabRecord.add(String.valueOf(recordPosition));
        interestEachTabRecord.add(getTdtFrom());
        interestEachTabRecord.add(getTdtTo());
        interestEachTabRecord.add(getTxtFromAmt());
        interestEachTabRecord.add(getTxtToAmt());
        interestEachTabRecord.add(getTxtInter());
        
        interestEachRecord.put(SLNO, String.valueOf(recordPosition));
        interestEachRecord.put(FROM_DATE, getTdtFrom());
        interestEachRecord.put(TO_DATE, getTdtTo());
        interestEachRecord.put(FROM_AMOUNT, getTxtFromAmt());
        interestEachRecord.put(TO_AMOUNT, getTxtToAmt());
        interestEachRecord.put(INTEREST, getTxtInter());
        interestEachRecord.put(PENALTY_INTEREST, getTxtPenalInter());
        interestEachRecord.put(AGAINST_CLEAR_INT, getTxtAgainstClearingInter());
        interestEachRecord.put(PENAL_STATEMENT, getTxtPenalStatement());
        interestEachRecord.put(INTER_EXP_LIMIT, getTxtInterExpLimit());
        interestEachRecord.put(ACCOUNT_NO, getLblAccHead_IM_2());
//        interestEachRecord.put(PRODUCT_ID, getLblProdID_IM_Disp());
        interestEachRecord.put(COMMAND, "");
    }
    
    private int updateInterestTab(int recordPosition){
        int option = -1;
        HashMap result = new HashMap();
        try{
            result = tableUtilInterest.updateTableValues(interestEachTabRecord, interestEachRecord, recordPosition);
            
            interestTabValues = (ArrayList) result.get(TABLE_VALUES);
            interestAll = (LinkedHashMap) result.get(ALL_VALUES);
            option = CommonUtil.convertObjToInt(result.get(OPTION));
            
            tblInterestTab.setDataArrayList(interestTabValues, interestTabTitle);
            
        }catch(Exception e){
            log.info("Error in updateInterestTab()..."+e);
            parseException.logException(e,true);
        }
        result = null;
        return option;
    }
    
    public void populateInterestDetails(int recordPosition){
        try{
            HashMap eachRecs;
            java.util.Set keySet =  interestAll.keySet();
            Object[] objKeySet = (Object[]) keySet.toArray();
            String strRecordKey = CommonUtil.convertObjToStr(((ArrayList) (tblInterestTab.getDataArrayList().get(recordPosition))).get(0));
            
            // To populate the corresponding record from the Interest Details
            for (int i = keySet.size() - 1, j = 0;i >= 0;--i, ++j){
                if (CommonUtil.convertObjToStr(((HashMap) interestAll.get(objKeySet[j])).get(SLNO)).equals(strRecordKey)){
                    // To populate the Corresponding record from CTable
                    eachRecs = (HashMap) interestAll.get(objKeySet[j]);
                    
                    setTdtFrom(CommonUtil.convertObjToStr(eachRecs.get(FROM_DATE)));
                    setTdtTo(CommonUtil.convertObjToStr(eachRecs.get(TO_DATE)));
                    setTxtFromAmt(CommonUtil.convertObjToStr(eachRecs.get(FROM_AMOUNT)));
                    setTxtToAmt(CommonUtil.convertObjToStr(eachRecs.get(TO_AMOUNT)));
                    setTxtInter(CommonUtil.convertObjToStr(eachRecs.get(INTEREST)));
                    setTxtPenalInter(CommonUtil.convertObjToStr(eachRecs.get(PENALTY_INTEREST)));
                    setTxtAgainstClearingInter(CommonUtil.convertObjToStr(eachRecs.get(AGAINST_CLEAR_INT)));
                    setTxtPenalStatement(CommonUtil.convertObjToStr(eachRecs.get(PENAL_STATEMENT)));
                    setTxtInterExpLimit(CommonUtil.convertObjToStr(eachRecs.get(INTER_EXP_LIMIT)));
                    
                    break;
                }
                eachRecs = null;
            }
            keySet = null;
            
            objKeySet = null;
            setChanged();
            ttNotifyObservers();
        }catch(Exception e){
            log.info("Error in populateInterestDetails()..."+e);
            parseException.logException(e,true);
        }
    }
    
    public void deleteInterestTabRecord(int recordPosition){
        HashMap result = new HashMap();
        try{
            result = tableUtilInterest.deleteTableValues(recordPosition);
            
            interestTabValues = (ArrayList) result.get(TABLE_VALUES);
            interestAll = (LinkedHashMap) result.get(ALL_VALUES);
            
            tblInterestTab.setDataArrayList(interestTabValues, interestTabTitle);
            
        }catch(Exception e){
            log.info("Error in deleteInterestTabRecord()..."+e);
            parseException.logException(e,true);
        }
        result = null;
    }
    
    // To create objects
    public void createObject(){
        interestTabValues = new ArrayList();
        interestAll = new LinkedHashMap();
        tblInterestTab.setDataArrayList(null, interestTabTitle);
        tableUtilInterest = new TableUtil();
        tableUtilInterest.setAttributeKey(SLNO);
    }
    
    // To destroy Objects
    public void destroyObjects(){
        interestTabValues = null;
        interestAll = null;
        tableUtilInterest = null;
    }
    
    /**
     * Getter for property txtPenalStatement.
     * @return Value of property txtPenalStatement.
     */
    public java.lang.String getTxtPenalStatement() {
        return txtPenalStatement;
    }
    
    /**
     * Setter for property txtPenalStatement.
     * @param txtPenalStatement New value of property txtPenalStatement.
     */
    public void setTxtPenalStatement(java.lang.String txtPenalStatement) {
        this.txtPenalStatement = txtPenalStatement;
    }
    
    /**
     * Getter for property loanType.
     * @return Value of property loanType.
     */
    public java.lang.String getLoanType() {
        return loanType;
    }
    
    /**
     * Setter for property loanType.
     * @param loanType New value of property loanType.
     */
    public void setLoanType(java.lang.String loanType) {
        this.loanType = loanType;
    }
    
    /**
     * Getter for property depositNo.
     * @return Value of property depositNo.
     */
    public java.lang.String getDepositNo() {
        return depositNo;
    }
    
    /**
     * Setter for property depositNo.
     * @param depositNo New value of property depositNo.
     */
    public void setDepositNo(java.lang.String depositNo) {
        this.depositNo = depositNo;
    }
    
    /**
     * Getter for property interestAll.
     * @return Value of property interestAll.
     */
    public java.util.LinkedHashMap getInterestAll() {
        return interestAll;
    }
    
    /**
     * Setter for property interestAll.
     * @param interestAll New value of property interestAll.
     */
    public void setInterestAll(java.util.LinkedHashMap interestAll) {
        this.interestAll = interestAll;
    }
    
    /**
     * Getter for property isNew.
     * @return Value of property isNew.
     */
    public boolean isIsNew() {
        return isNew;
    }
    
    /**
     * Setter for property isNew.
     * @param isNew New value of property isNew.
     */
    public void setIsNew(boolean isNew) {
        this.isNew = isNew;
    }
    
}
