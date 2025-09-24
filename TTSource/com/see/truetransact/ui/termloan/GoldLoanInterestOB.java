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

package com.see.truetransact.ui.termloan;

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
import com.see.truetransact.transferobject.termloan.TermLoanInterestTO;
import com.see.truetransact.ui.TrueTransactMain;
import com.see.truetransact.uicomponent.CObservable;
import com.see.truetransact.uicomponent.COptionPane;

import org.apache.log4j.Logger;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;

public class GoldLoanInterestOB extends CObservable{
    
    /** Creates a new instance of TermLoanInterestOB */
    public GoldLoanInterestOB() throws Exception{
        curDate = ClientUtil.getCurrentDate();
        termLoanInterestOB();
    }
    
    private       static GoldLoanInterestOB termLoanInterestOB;
    
    private final static Logger log = Logger.getLogger(GoldLoanInterestOB.class);
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
    private final   String  AUTHORIZE_STATUS="AUTHORIZE_STATUS";
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
    private TermLoanInterestTO existTermLoanInterestTO=null;
    
    private final   java.util.ResourceBundle objTermLoanRB = java.util.ResourceBundle.getBundle("com.see.truetransact.ui.termloan.GoldLoanRB", ProxyParameters.LANGUAGE);
    
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
    private HashMap intAuthStatusMap=null;
    Date curDate = null;
    private HashMap nextSlabList=new HashMap();
    private boolean enableInterExpLimit = true;
//    static {
//        try {
//            termLoanInterestOB = new GoldLoanInterestOB();
//        } catch(Exception e) {
//            log.info("try: " + e);
//            parseException.logException(e,true);
//        }
//    }
    
//    public static GoldLoanInterestOB getInstance() {
//        return termLoanInterestOB;
//    }
    
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
            interestTabTitle.add(objTermLoanRB.getString("tblColumnInterestStatus"));
            interestTabTitle.add(objTermLoanRB.getString("tblColumnAuthorizeStatus"));
            
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
                    if(lst !=null && lst.size()>0){
                        hash=(HashMap)lst.get(0);
                        double intrate=0;
                        intrate=CommonUtil.convertObjToDouble(hash.get("RATE_OF_INT")).doubleValue();
                        if(hash.get("BEHAVES_LIKE").equals("DAILY"))
                            intrate=0;
                        termLoanInterestTO.setInterest(new Double(prodInt+intrate));
                    }
                }
                interestRecordList.add(CommonUtil.convertObjToStr(termLoanInterestTO.getInterest()));
                interestRecordList.add(CommonUtil.convertObjToStr(termLoanInterestTO.getActiveStatus()));
                interestRecordList.add(CommonUtil.convertObjToStr(termLoanInterestTO.getAuthorizeStatus()));
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
                interestRecordMap.put("INT_ACTIVE_STATUS", CommonUtil.convertObjToStr(termLoanInterestTO.getActiveStatus()));
                interestRecordMap.put(AUTHORIZE_STATUS, CommonUtil.convertObjToStr(termLoanInterestTO.getAuthorizeStatus()));
                if(CommonUtil.convertObjToStr(termLoanInterestTO.getAuthorizeStatus()).length()>0)
                    setAuthorizeStatus(termLoanInterestTO.getAuthorizeStatus());
                
                if (loanType.equals("LTD") && isNew)
                    interestRecordMap.put(COMMAND, INSERT);
                else
                    interestRecordMap.put(COMMAND, UPDATE);
                
                allInterestRecords.put(strSerialNo, interestRecordMap);
                
                setTxtInter(CommonUtil.convertObjToStr(termLoanInterestTO.getInterest()));
                setPenalInter(CommonUtil.convertObjToStr(termLoanInterestTO.getPenalInterest()));
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
            Date lastToDate = getProperDateFormat(strLastToDate);
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
            List resultList = ClientUtil.executeQuery("getSelectTermLoanInterestMaxSLNO", transactionMap);
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
            GoldLoanOB termLoanOB = new GoldLoanOB();
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
                    objTermLoanInterestTO.setFromDt(getProperDateFormat(oneRecord.get(FROM_DATE)));
                    objTermLoanInterestTO.setInterest(CommonUtil.convertObjToDouble(oneRecord.get(INTEREST)));
                    objTermLoanInterestTO.setInterestExpiryLimit(CommonUtil.convertObjToDouble(oneRecord.get(INTER_EXP_LIMIT)));
                    objTermLoanInterestTO.setStatementPenal(CommonUtil.convertObjToDouble(oneRecord.get(PENAL_STATEMENT)));
                    objTermLoanInterestTO.setPenalInterest(CommonUtil.convertObjToDouble(oneRecord.get(PENALTY_INTEREST)));
                    objTermLoanInterestTO.setStatus(CommonUtil.convertObjToStr(oneRecord.get(COMMAND)));
                    objTermLoanInterestTO.setToAmt(CommonUtil.convertObjToDouble(oneRecord.get(TO_AMOUNT)));
                    objTermLoanInterestTO.setActiveStatus(CommonUtil.convertObjToStr(oneRecord.get("INT_ACTIVE_STATUS")));
                    objTermLoanInterestTO.setAuthorizeStatus(CommonUtil.convertObjToStr(oneRecord.get(AUTHORIZE_STATUS)));
                    
                    objTermLoanInterestTO.setRetCreateDt(getProperDateFormat(oneRecord.get("RET_CREATE_DT")));
                    
                    //                    objTermLoanInterestTO.setToDt((Date)oneRecord.get(TO_DATE));
                    objTermLoanInterestTO.setToDt(getProperDateFormat(oneRecord.get(TO_DATE)));
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
                objTermLoanInterestTO.setFromDt(getProperDateFormat(oneRecord.get(FROM_DATE)));
                objTermLoanInterestTO.setInterest(CommonUtil.convertObjToDouble(oneRecord.get(INTEREST)));
                objTermLoanInterestTO.setInterestExpiryLimit(CommonUtil.convertObjToDouble(oneRecord.get(INTER_EXP_LIMIT)));
                objTermLoanInterestTO.setStatementPenal(CommonUtil.convertObjToDouble(oneRecord.get(PENAL_STATEMENT)));
                objTermLoanInterestTO.setPenalInterest(CommonUtil.convertObjToDouble(oneRecord.get(PENALTY_INTEREST)));
                objTermLoanInterestTO.setStatus(CommonUtil.convertObjToStr(oneRecord.get(COMMAND)));
                objTermLoanInterestTO.setToAmt(CommonUtil.convertObjToDouble(oneRecord.get(TO_AMOUNT)));
                
                //                objTermLoanInterestTO.setToDt((Date)oneRecord.get(TO_DATE));
                objTermLoanInterestTO.setToDt(getProperDateFormat(oneRecord.get(TO_DATE)));
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
    
    public Date getProperDateFormat(Object obj) {
        Date currDt = null;
        if (obj!=null && obj.toString().length()>0) {
            Date tempDt= DateUtil.getDateMMDDYYYY(CommonUtil.convertObjToStr(obj));
            currDt=(Date)curDate.clone();
            currDt.setDate(tempDt.getDate());
            currDt.setMonth(tempDt.getMonth());
            currDt.setYear(tempDt.getYear());
        }
        return currDt;
    }
    
    
    public void setValByProdID(){
        try{
            GoldLoanOB termLoanOB = new GoldLoanOB();
            HashMap transactionMap = new HashMap();
            HashMap retrieve;
            transactionMap.put("PROD_ID", CommonUtil.convertObjToStr(termLoanOB.getCbmProductId().getKeyForSelected()));
            List resultList =  ClientUtil.executeQuery("getProdIntDetails", transactionMap);
            if (resultList !=null && resultList.size() > 0){
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
    
    public ArrayList getInterestDetails(String fromDate, String toDate, Date loanToDate,Date firstRepayInstalDt){
        ArrayList returnValue = new ArrayList();
        try{
            HashMap eachRecs;
            HashMap interestRateAndPeriod;
            java.util.Set keySet =  interestAll.keySet();
            Object[] objKeySet = (Object[]) keySet.toArray();
            Date repayFromDate = getProperDateFormat(fromDate);
            Date repayToDate = getProperDateFormat(toDate);
            Date interestFromDate;
            Date interestToDate;
            boolean gotFinalIntRate = false;
            for (int i = keySet.size() - 1,j = 0;i >= 0;--i,++j){
                interestRateAndPeriod = new HashMap();
                eachRecs = (HashMap) interestAll.get(objKeySet[j]);
                interestFromDate = getProperDateFormat(eachRecs.get(FROM_DATE));
//               if(firstRepayInstalDt.compareTo(interestFromDate)<=0){
                if ((eachRecs.get(TO_DATE) == null) || (CommonUtil.convertObjToStr(eachRecs.get(TO_DATE)).length() == 0)){
                    interestToDate = loanToDate;
                }else{
                    interestToDate = getProperDateFormat(eachRecs.get(TO_DATE));
                }
                
                if ((repayFromDate.compareTo(interestFromDate) >= 0 && interestToDate.compareTo(repayFromDate) > 0)){
                    interestRateAndPeriod.put(INTEREST, eachRecs.get(INTEREST));
                    interestRateAndPeriod.put(FROM_DATE, eachRecs.get(FROM_DATE));//DateUtil.getStringDate(repayFromDate
                    interestRateAndPeriod.put(TO_DATE, eachRecs.get(TO_DATE));
                }else if (interestFromDate.compareTo(repayFromDate) >= 0){// && interestToDate.compareTo(repayToDate) < 0) || (repayToDate.compareTo(interestFromDate) >= 0 && repayToDate.compareTo(interestToDate) < 0)){
                    interestRateAndPeriod.put(INTEREST, eachRecs.get(INTEREST));
                    interestRateAndPeriod.put(FROM_DATE, eachRecs.get(FROM_DATE));
                    interestRateAndPeriod.put(TO_DATE, eachRecs.get(TO_DATE));
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
//            }
            eachRecs = null;
            keySet = null;
            objKeySet = null;
        }catch(Exception e){
            log.info("Error in getInterestDetails(): "+e);
            parseException.logException(e,true);
        }
        return returnValue;
    }
    
        /*
         *set TO for if change the amount it shoude update  all flag same date
         **/
    private void setTermLoanInterestTO(HashMap eachRecs){
        
        existTermLoanInterestTO=new TermLoanInterestTO();
        existTermLoanInterestTO.setFromDt(getProperDateFormat(eachRecs.get(FROM_DATE)));
        existTermLoanInterestTO.setToDt(getProperDateFormat(eachRecs.get(TO_DATE)));
        existTermLoanInterestTO.setInterest(CommonUtil.convertObjToDouble(eachRecs.get(INTEREST)));
        existTermLoanInterestTO.setPenalInterest(CommonUtil.convertObjToDouble(eachRecs.get(PENALTY_INTEREST)));
        existTermLoanInterestTO.setAgainstClearingInt(CommonUtil.convertObjToDouble(eachRecs.get(AGAINST_CLEAR_INT)));
        existTermLoanInterestTO.setFromAmt(CommonUtil.convertObjToDouble(eachRecs.get(FROM_AMOUNT)));
        existTermLoanInterestTO.setToAmt(CommonUtil.convertObjToDouble(eachRecs.get(TO_AMOUNT)));
    }
    public  boolean setNextValue(){
        boolean dataEnable=false;
        if(nextSlabList!=null && nextSlabList.size()>0){
            setTdtFrom(CommonUtil.convertObjToStr(nextSlabList.get("fromDt")));
            
            if(nextSlabList!=null && nextSlabList.containsKey("FromAmt") && nextSlabList.get("FromAmt")!=null){
                setTxtFromAmt(CommonUtil.convertObjToStr(nextSlabList.get("FromAmt")));//CommonUtil.convertObjToStr(nextSlabList.get("ToAmt")));
                dataEnable=false;
            }else
                dataEnable=true;
            double maxAmt=CommonUtil.convertObjToDouble(nextSlabList.get("ToAmt")).doubleValue();
            if(CommonUtil.convertObjToInt(nextSlabList.get("FromPeriod"))==364636 && 9999999999l==maxAmt)
                //               resetComponents();
                //           resetPeriod();
                if(nextSlabList==null || nextSlabList.isEmpty()){
                    //               resultValue = 1;
                    //               period = setPeriod(resultValue);
                    //               setCboFromPeriod(period);
                    //               setCboFromAmount((String) getCbmFromAmount().getDataForKey("1"));
                }
            //           resetPeriod();
        }else{
            setTxtFromAmt(String.valueOf(1));
            dataEnable=true;
        }
        return dataEnable;
    }
    
    public boolean slabBasedEnableDisable(boolean actionTypeDelete){
        boolean maxSlabAvailable=false;
        nextSlabList=new HashMap();
        HashMap tempto=new HashMap();
        Date fromDt=getProperDateFormat(tdtFrom);
        //           double setToPeriod=364635;
        long setMaxamount=999999999;
        double minAmt=CommonUtil.convertObjToDouble(getTxtFromAmt()).doubleValue();
        double maxAmt=CommonUtil.convertObjToDouble(getTxtToAmt()).doubleValue();
        if(interestAll !=null && interestAll.size()>=1){
            java.util.Set keySet=interestAll.keySet();
            Object objKeySet[]=(Object[])keySet.toArray();
            for(int i=0;i<interestAll.size();i++){
                tempto=(HashMap)interestAll.get(objKeySet[i]);
                Date mapDt=getProperDateFormat(tempto.get("FROM_DATE"));
                if( fromDt !=null && DateUtil.dateDiff(fromDt,mapDt)==0){
                    if( maxAmt==setMaxamount || maxAmt>setMaxamount ){
                        nextSlabList=new HashMap();
                        break;
                    }
                    nextSlabList.put("FromAmt",  new Double(maxAmt+1));
                    nextSlabList.put("fromDt",fromDt);
                    maxSlabAvailable=true;
                }
            }
        }
        return maxSlabAvailable;
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
            
            List RetrasPectiveList= insertInterest(dataSize+1);
            //            maxAmountRangeCheck();
            HashMap eachRecs;
            java.util.Set keySet =  interestAll.keySet();
            Object[] objKeySet = (Object[]) keySet.toArray();
            int rowSelected = -1;
            
                /* max retraspective purpose
                 */
            //            if(keySet.size() > 0)
            //                if(setRetraspectiveCurrDt(objKeySet,tdtFrom))
            interestEachRecord.put("RET_CREATE_DT", curDate);
            //                else
            //                    interestEachRecord.put("RET_CREATE_DT", null);
            Date intiateDt=null;
            
            if((!update) && checkDuplicateRecord())
                return-1;
            if(update)
                validateSlabRate(recordPosition);
            
            if(keySet.size() > 0){
                for (int i = keySet.size() - 1,j = 0;i >= 0;--i,++j){
                    eachRecs = (HashMap) interestAll.get(objKeySet[j]);
                    ArrayList selectList=(ArrayList)interestTabValues.get(j);
                    //__ Check for the Duplication of Date
                    if (!( DateUtil.getDateMMDDYYYY(getTdtFrom()).before(DateUtil.getDateMMDDYYYY(CommonUtil.convertObjToStr(eachRecs.get(FROM_DATE))))
                    && ((!getTdtTo().equals("")) && DateUtil.getDateMMDDYYYY(getTdtTo()).before(DateUtil.getDateMMDDYYYY(CommonUtil.convertObjToStr(eachRecs.get(FROM_DATE))))))) {
                        if (CommonUtil.convertObjToStr(eachRecs.get(TO_DATE)).length() > 0 && (! getTdtTo().equals(""))) {
                            if (!(DateUtil.getDateMMDDYYYY(getTdtFrom()).after(DateUtil.getDateMMDDYYYY(CommonUtil.convertObjToStr(eachRecs.get(TO_DATE)))) &&
                            DateUtil.getDateMMDDYYYY(getTdtTo()).after(DateUtil.getDateMMDDYYYY(CommonUtil.convertObjToStr(eachRecs.get(TO_DATE))))))
                                dateRange = false;
                        }
                    }
                    //                    if(j==0)
                    intiateDt=getProperDateFormat(eachRecs.get("FROM_DATE"));
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
                    //                    if(!dateRange && !amountRange && j != recordPosition){
                    //                        rowSelected = i;
                    //                        String[] options = {objTermLoanRB.getString("cDialogOk"),objTermLoanRB.getString("cDialogCancel")};
                    //                        option = COptionPane.showOptionDialog(null, objTermLoanRB.getString("ROI_Existence_Warn"), CommonConstants.WARNINGTITLE,
                    //                        COptionPane.YES_NO_CANCEL_OPTION, COptionPane.WARNING_MESSAGE, null, options, options[0]);
                    //
                    //                        break;
                    //                    }
                    Date previousFromDt=null;
                    String   strtoDt=null;
                    Date toDt=null;
                    Date previousTodt=null;
                    /**
                     *RETRASPECTIVE DATE APPLIED OR NOT
                     */
                    HashMap retraspectiveStmap=null;
                    if(RetrasPectiveList!=null && RetrasPectiveList.size()>0){
                        retraspectiveStmap=(HashMap)RetrasPectiveList.get(0);
                        
                    }
                    String authStatus=CommonUtil.convertObjToStr(eachRecs.get("AUTHORIZE_STATUS"));
                    //                    if(authStatus.equals("AUTHORIZED"))
                    if((retraspectiveStmap !=null && DateUtil.dateDiff((Date)retraspectiveStmap.get("FROM_DT"),
                    (DateUtil.getDateMMDDYYYY(CommonUtil.convertObjToStr(eachRecs.get(FROM_DATE)))))>=0 &&
                    (!CommonUtil.convertObjToStr(selectList.get(6)).equals("RET")) &&
                    (!CommonUtil.convertObjToStr(selectList.get(6)).equals("N")))             ||
                    (retraspectiveStmap == null && (intiateDt!=null &&
                    DateUtil.dateDiff(intiateDt,(DateUtil.getDateMMDDYYYY(CommonUtil.convertObjToStr(tdtFrom))))<=0) &&
                    (!CommonUtil.convertObjToStr(selectList.get(6)).equals("RET")) &&
                    (!CommonUtil.convertObjToStr(selectList.get(6)).equals("N")))){
                        
                        //CHECK EXIST RECORD AUTHORIZE OR NOT
                        //IF NOT AUTHORIZE WE SHOULD AUTHORIZE THEN RETRASPECTIVE START
                        for(int u=0;u<keySet.size();u++){
                            HashMap oneMap = (HashMap) interestAll.get(objKeySet[u]);
                            if(oneMap.get(COMMAND).equals("UPDATE") && (!update)){
                                String authStatusOne=CommonUtil.convertObjToStr(oneMap.get("AUTHORIZE_STATUS"));
                                if(authStatusOne.length()==0){
                                    ClientUtil.showMessageWindow("Should Authorize Existing Record Then Retraspective Entry is Possible");
                                    return -1;
                                }
                            }
                        }
                        
                        //                        HashMap retraspectiveStmap=new HashMap();
                        //                        if(RetrasPectiveList!=null && RetrasPectiveList.size()>0){
                        //                            retraspectiveStmap=(HashMap)RetrasPectiveList.get(0);
                        //                        }
                        if(DateUtil.dateDiff(DateUtil.getDateMMDDYYYY(CommonUtil.convertObjToStr(eachRecs.get(FROM_DATE))),DateUtil.getDateMMDDYYYY(CommonUtil.convertObjToStr(tdtFrom)))<=0 ||
                        (CommonUtil.convertObjToStr(eachRecs.get(TO_DATE)).length() > 0  && DateUtil.dateDiff(DateUtil.getDateMMDDYYYY(CommonUtil.convertObjToStr(eachRecs.get(TO_DATE))),DateUtil.getDateMMDDYYYY(CommonUtil.convertObjToStr(tdtFrom)))<0)){
                            //                             if(RetrasPectiveList!=null && RetrasPectiveList.size()>0)
                            if( retraspectiveStmap !=null && DateUtil.dateDiff(DateUtil.getDateMMDDYYYY(CommonUtil.convertObjToStr(retraspectiveStmap.get("FROM_DT"))),DateUtil.getDateMMDDYYYY(CommonUtil.convertObjToStr(eachRecs.get(FROM_DATE))))==0) {
                                if( DateUtil.dateDiff(DateUtil.getDateMMDDYYYY(CommonUtil.convertObjToStr(retraspectiveStmap.get("FROM_DT"))),DateUtil.getDateMMDDYYYY(getTdtFrom()))!=0)
                                    dynamicInterestDetails(eachRecs,selectList,DateUtil.getDateMMDDYYYY(getTdtFrom()),data.size());
                                interestEachTabRecord.set(0,String.valueOf(dataSize+1));
                                interestEachRecord.put(SLNO,String.valueOf(dataSize+1));
                            }
                            
                            //                            if(retraspectiveStmap !=null && DateUtil.dateDiff(DateUtil.getDateMMDDYYYY(CommonUtil.convertObjToStr(retraspectiveStmap.get("FROM_DT"))),
                            //                            DateUtil.getDateMMDDYYYY(getTdtFrom()))<=0 && authStatus.equals("AUTHORIZED")
                            //                            || (retraspectiveStmap ==null && authStatus.equals("AUTHORIZED"))){
                            if(retraspectiveStmap !=null && DateUtil.dateDiff(DateUtil.getDateMMDDYYYY(CommonUtil.convertObjToStr(eachRecs.get(FROM_DATE))),
                            DateUtil.getDateMMDDYYYY(getTdtFrom()))<=0 && authStatus.equals("AUTHORIZED")
                            || (retraspectiveStmap ==null && authStatus.equals("AUTHORIZED"))){
                                
                                selectList.set(6,"N");
                                interestTabValues.set(j,selectList);
                                eachRecs.put("INT_ACTIVE_STATUS","N");
                                interestAll.put(String.valueOf(j+1),eachRecs);
                            }
                            
                        }
                    }
                    //                        previousFromDt=fromDt;
                    //                        previousTodt=DateUtil.addDays(previousFromDt, -1);
                    //                    }
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
                    Date currFromDt=null;
                    String   strtoDt=null;
                    Date toDt=null;
                    Date previousTodt=null;
                    Date fromDt=null;
                    interestTabValues = (ArrayList) result.get(TABLE_VALUES);
                    interestAll = (LinkedHashMap) result.get(ALL_VALUES);
                    option = CommonUtil.convertObjToInt(result.get(OPTION));
                    for(int a=(interestTabValues.size()-1);a>(-1);a--){
                        ArrayList selectList=new ArrayList();
                        ArrayList singleList=new ArrayList();
                        selectList=(ArrayList)interestTabValues.get(a);
                        HashMap selectMap=(HashMap)interestAll.get(String.valueOf(a+1));
                        //                           singleList.add(selectList.get(0));
                        if(a==(interestTabValues.size()-1))
                            fromDt=getProperDateFormat(selectList.get(1));
                        strtoDt=CommonUtil.convertObjToStr(selectList.get(2));
                        if(!strtoDt.equals(""))
                            toDt=getProperDateFormat(strtoDt);
                        String activeStatus= CommonUtil.convertObjToStr(selectList.get(6));
                        String authStatus=CommonUtil.convertObjToStr(selectList.get(7));
                        if(currFromDt !=null){
                            currFromDt=getProperDateFormat(selectList.get(1));
                            if(DateUtil.dateDiff(currFromDt,fromDt)>0 ){
                                
                                //                                  int yes= ClientUtil.confirmationAlert("To date add previous Dt");
                                //                                  if(yes==0){
                                
                                if(strtoDt.equals("")){
                                    selectList.set(2,DateUtil.getStringDate(previousTodt));//DateUtil.getStringDate(previousTodt));
                                    selectMap.put("TO_DATE",DateUtil.getStringDate(previousTodt));// DateUtil.getStringDate(previousTodt));
                                }
                                //                                      interestTabValues.set(i,selectList);
                                //                                      interestAll.put(String.valueOf(i+1),selectMap);
                                //                                  }
                                
                            }else{
                                if(strtoDt.equals("")){// && nextSlabList !=null && nextSlabList.size()==0){
                                    if(strtoDt.equals("") && authStatus.equals("AUTHORIZED") && activeStatus.equals("N")){
                                        selectList.set(2,CommonUtil.convertObjToStr(selectList.get(1)));//DateUtil.getStringDate(previousTodt));
                                        selectMap.put("TO_DATE",CommonUtil.convertObjToStr(selectList.get(1)));// DateUtil.getStringDate(previousTodt));
                                    }
                                    //                                        else{
                                    //                                    selectList.set(2,DateUtil.getStringDate(previousTodt));//fromDt
                                    //                                    selectMap.put("TO_DATE", DateUtil.getStringDate(previousTodt));
                                    //                                    }
                                }
                            }
                            interestTabValues.set(a,selectList);
                            interestAll.put(String.valueOf(a+1),selectMap);
                        }else
                            currFromDt=fromDt;
                        
                        /**
                         * //                         *RETRASPECTIVE DATE APPLIED OR NOT
                         * //                         */
                        //                        if(DateUtil.dateDiff(DateUtil.getDateMMDDYYYY(getTdtFrom()),(DateUtil.getDateMMDDYYYY(CommonUtil.convertObjToStr(selectMap.get(FROM_DATE)))))>=0 &&
                        //                        (!CommonUtil.convertObjToStr(selectList.get(6)).equals("RET"))){
                        //                            dynamicInterestDetails(selectMap,selectList,DateUtil.getDateMMDDYYYY(getTdtFrom()),data.size());
                        //                            selectList.set(6,"N");
                        //                            interestTabValues.set(a,selectList);
                        //                            selectMap.put("INT_ACTIVE_STATUS","N");
                        //                            interestAll.put(String.valueOf(a+1),selectMap);
                        //                        }
                        //                        previousFromDt=fromDt;
                        previousTodt=DateUtil.addDays(fromDt, -1);
                    }
                    
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
    
    public StringBuffer maxAmountRangeCheck(String toDate){
        HashMap eachRecs=new HashMap();
        int z=0;
        String previusDt=null;
        ArrayList alltotInterestList=new ArrayList();
        Double maxAmt=new Double(999999999);
        Double minAmt=new Double(1);
        boolean minAmtStatus=false;
        boolean maxAmtStatus=false;
        Date previusFromDt=null;
        ArrayList singleList=new ArrayList();
        ArrayList totList=new ArrayList();
        StringBuffer buf=new StringBuffer();
        ArrayList  tableList=(ArrayList)tableUtilInterest.getTableValues();
        if(tableList !=null && tableList.size()>0){
            for(int i=0;i<tableList.size();i++){
                singleList=(ArrayList)tableList.get(i);
                String fromDt=CommonUtil.convertObjToStr(singleList.get(1));
                if(previusDt ==null ||fromDt.equals(previusDt))
                    totList.add(singleList);
                else{
                    alltotInterestList.add(totList);
                    totList=new ArrayList();
                    totList.add(singleList);
                    
                    
                }
                
                if(i==tableList.size()-1)
                    alltotInterestList.add(totList);
                
                System.out.println("     "+singleList);
                previusDt=fromDt;
            }
        }
        for(int a=0;a<alltotInterestList.size();a++){
            String minAmtDisplay="";
            String maxAmtDisplay="";
            String loanPeriod="";
            minAmtStatus=false;
            maxAmtStatus=false;
            ArrayList allIntList=(ArrayList)alltotInterestList.get(a);
            for(int p=0;p<allIntList.size();p++){
                singleList=(ArrayList)allIntList.get(p);
                String fromDt=CommonUtil.convertObjToStr(singleList.get(1));
                Double fromAmt=CommonUtil.convertObjToDouble(singleList.get(3));
                Double toAmt=CommonUtil.convertObjToDouble(singleList.get(4));
                //min amount check
                if(fromAmt.equals(minAmt)) {
                    minAmtStatus=true;
                    minAmtDisplay="";
                }else if(!minAmtStatus){
                    minAmtDisplay=new String("Please Enter Minimum Amount Slab For Interest Slab Starting From   :"+fromDt+"\n");
                }
                //max amount check
                if(toAmt.equals(maxAmt) || toAmt.doubleValue()>maxAmt.doubleValue() ) {
                    maxAmtStatus=true;
                    maxAmtDisplay="";
                }else if(!maxAmtStatus){
                    maxAmtDisplay=new String("Please Enter Maximum Amount Slab For Interest Slab  Starting  From :"+fromDt+"\n");
                }
                
                if(alltotInterestList.size()-1==a)
                    if(allIntList.size()-1==p){
                        String tabtoDate=CommonUtil.convertObjToStr(singleList.get(2));
                        if(tabtoDate.length()>0){
                            if(DateUtil.dateDiff(DateUtil.getDateMMDDYYYY(tabtoDate),
                            DateUtil.getDateMMDDYYYY(toDate))>0){
                                loanPeriod=new String("Please Enter Max ROI for Max Period slab (Column 'TO-DATE' Value Should be Blank).");
                                
                            }
                        }else
                            loanPeriod="";
                    }
            }
            buf.append(loanPeriod);
            buf.append(minAmtDisplay);
            buf.append(maxAmtDisplay);
            System.out.println("buf "+buf);
        }
        return buf;
    }
    
    
    private boolean checkDuplicateRecord(){
        ArrayList tabList =(ArrayList)tblInterestTab.getDataArrayList();
        if(tabList !=null && tabList.size()>0){
            Date fromDt=getProperDateFormat(interestEachTabRecord.get(1));
            for(int i=0;i<tabList.size();i++){
                double toAmt=0;
                ArrayList singleList=(ArrayList)tabList.get(i);
                if(singleList !=null && singleList.size()>0){
                    Date tblfromDt=getProperDateFormat(singleList.get(1));
                    toAmt=CommonUtil.convertObjToDouble(singleList.get(4)).doubleValue();
                    if(DateUtil.dateDiff(fromDt,tblfromDt)>=0){
                        String authStatus=CommonUtil.convertObjToStr(singleList.get(7));
                        if(toAmt>=999999999 && authStatus.equals("")){
                            ClientUtil.showMessageWindow("Maximum Interest Slab Already Exist for  :" +
                            CommonUtil.convertObjToStr(singleList.get(1))
                            );
                            return true;
                        }
                    }
                    if(tabList.size()-1==i){
                        if(DateUtil.dateDiff(tblfromDt,fromDt)!=0){
                            if(toAmt <999999999){
                                ClientUtil.showMessageWindow("Please Reach Maximum Interest slab For "+CommonUtil.convertObjToStr(singleList.get(1)));
                                return true;
                            }
                        }
                    }
                }
                
            }
        }
        return false;
    }
    
    private boolean setRetraspectiveCurrDt(Object[] objKeySet ,String tdtDate){
        HashMap eachRecs=new HashMap();
        java.util.Set keySet =  interestAll.keySet();
        if(keySet!=null)
            for(int i=keySet.size()-1;i>=0;i--){
                eachRecs = (HashMap) interestAll.get(objKeySet[i]);
                if(DateUtil.dateDiff(DateUtil.getDateMMDDYYYY(CommonUtil.convertObjToStr(eachRecs.get("FROM_DATE"))),DateUtil.getDateMMDDYYYY(tdtDate))<=0)
                    return true;
                return false;
                
            }
        return false;
    }
    
    void dynamicInterestDetails(HashMap singleMap,ArrayList oneList,Object dynamicStart_dt,int newReocordPos){
        if(!(singleMap.get("INT_ACTIVE_STATUS")).equals("N")){
            boolean dynamicEntry_yesNo=true;
            ArrayList dynamicList=new ArrayList();
            HashMap tempHashMap=new HashMap();
            Date from_dt=getProperDateFormat(tdtFrom);
            from_dt=DateUtil.addDays(from_dt, -1);
            dynamicList.add(String.valueOf(newReocordPos+1));
            dynamicList.add(singleMap.get(FROM_DATE));
            dynamicList.add(DateUtil.getStringDate(from_dt));
            dynamicList.add(singleMap.get(FROM_AMOUNT));
            dynamicList.add(singleMap.get(TO_AMOUNT));
            dynamicList.add(singleMap.get(INTEREST));
            dynamicList.add("RET");
            
            tempHashMap.put(SLNO, String.valueOf(newReocordPos+1));
            tempHashMap.put(FROM_DATE, singleMap.get(FROM_DATE));
            tempHashMap.put(TO_DATE, DateUtil.getStringDate(from_dt));
            tempHashMap.put(FROM_AMOUNT, singleMap.get(FROM_AMOUNT));
            tempHashMap.put(TO_AMOUNT, singleMap.get(TO_AMOUNT));
            tempHashMap.put(INTEREST, singleMap.get(INTEREST));
            tempHashMap.put(PENALTY_INTEREST, singleMap.get(PENALTY_INTEREST));
            tempHashMap.put(AGAINST_CLEAR_INT, singleMap.get(AGAINST_CLEAR_INT));
            tempHashMap.put(PENAL_STATEMENT, singleMap.get(PENAL_STATEMENT));
            tempHashMap.put(INTER_EXP_LIMIT, singleMap.get(INTER_EXP_LIMIT));
            tempHashMap.put(ACCOUNT_NO,singleMap.get(ACCOUNT_NO));
            tempHashMap.put("INT_ACTIVE_STATUS", "R");
            interestTabValues.add(dynamicList);
            tempHashMap.put(COMMAND, "INSERT");
            interestAll.put(String.valueOf(interestAll.size()+1),tempHashMap);
        }
    }
    private List insertInterest(int recordPosition){
        interestEachTabRecord.add(String.valueOf(recordPosition));
        interestEachTabRecord.add(getTdtFrom());
        interestEachTabRecord.add(getTdtTo());
        interestEachTabRecord.add(getTxtFromAmt());
        interestEachTabRecord.add(getTxtToAmt());
        interestEachTabRecord.add(getTxtInter());
        if(intAuthStatusMap !=null && intAuthStatusMap.size()>0){
            interestEachTabRecord.add("R");
            interestEachTabRecord.add(intAuthStatusMap.get(AUTHORIZE_STATUS));
        }
        else {
            interestEachTabRecord.add("RET");
            interestEachTabRecord.add("");
        }
        
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
        interestEachRecord.put(ACCOUNT_NO, getLblAccNo_IM_2());
        interestEachRecord.put("INT_ACTIVE_STATUS", "R");
        if(intAuthStatusMap !=null && intAuthStatusMap.size()>0)
            interestEachRecord.put(AUTHORIZE_STATUS, intAuthStatusMap.get(AUTHORIZE_STATUS));
        else
            interestEachRecord.put(AUTHORIZE_STATUS, "");
        
        //        interestEachRecord.put(PRODUCT_ID, getLblProdID_IM_Disp());
        interestEachRecord.put(COMMAND, "");
        List lst=null;
        HashMap map=new HashMap();
        map.put(ACCOUNT_NO,getLblAccNo_IM_2());
        map.put(FROM_DATE,getProperDateFormat(getTdtFrom()));
        return  lst=ClientUtil.executeQuery("getSelectTermLoanAcctInterest",map);
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
                    intAuthStatusMap=new HashMap();
                    if(CommonUtil.convertObjToStr(eachRecs.get(AUTHORIZE_STATUS)).length()>0)
                        intAuthStatusMap.put(AUTHORIZE_STATUS,CommonUtil.convertObjToStr(eachRecs.get(AUTHORIZE_STATUS)));
                    setTermLoanInterestTO(eachRecs);
                    
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
            updateToDate(recordPosition);
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
    
          /* deleteing record we should update todate to previous record
           */
    private void updateToDate(int selectedRow){
        ArrayList singleList=(ArrayList)tblInterestTab.getDataArrayList().get(selectedRow);
        ArrayList totTabList=(ArrayList) tblInterestTab.getDataArrayList();
        ArrayList tabUtilList=new ArrayList();
        Date from_dt=null;
        from_dt=getProperDateFormat(singleList.get(1));
        from_dt=DateUtil.addDays(from_dt, -1);
        java.util.Set set=interestAll.keySet();
        Object objKeySet[]=(Object[])set.toArray();
        for(int i=totTabList.size()-1;i>=0;i--){
            singleList=(ArrayList)totTabList.get(i);
            HashMap oneList=(HashMap)interestAll.get(objKeySet[i]);
            //            tabUtilList=(ArrayList)interestAll.
            if(selectedRow>i){
                Date toDate=getProperDateFormat(singleList.get(2));
                //               String activeStatus=CommonUtil.convertObjToStr(singleList.get(7));
                if( toDate !=null && DateUtil.dateDiff(from_dt,toDate)==0 ){
                    tblInterestTab.setValueAt("",i , 2);
                    oneList.put(TO_DATE,null);
                    interestAll.put(objKeySet[i],oneList);
                }
            }
        }
    }
    
    public void validateSlabRate(int recordPosition){
        double existingToAmt=0;
        double changeToAmt=0;
        Date existFromDt=null;
        Date changeFromDt=null;
        //        //        boolean amt=false;
        if(existTermLoanInterestTO !=null){
            if(interestAll!=null){
                existFromDt =existTermLoanInterestTO.getFromDt();
                existFromDt=DateUtil.addDays(existFromDt, -1);
                changeFromDt=getProperDateFormat(interestEachTabRecord.get(1));
                existingToAmt=existTermLoanInterestTO.getToAmt().doubleValue();
                changeToAmt=CommonUtil.convertObjToDouble(interestEachTabRecord.get(4)).doubleValue();
                if(existingToAmt!=changeToAmt){
                    modifyList(existTermLoanInterestTO.getFromDt(),changeToAmt,existingToAmt,null,null,recordPosition);
                }
                if(DateUtil.dateDiff(existTermLoanInterestTO.getFromDt(),changeFromDt)!=0){
                    modifyList(existTermLoanInterestTO.getFromDt(),0,0,existFromDt,changeFromDt,recordPosition);
                }
            }
        }
    }
    
    public void  modifyList(Date roiDate,double Amt,double existAmt ,Date existFromDt,Date changeDt,int recordPosition){
        if(interestAll !=null ){
            ArrayList singleList=null;
            //            System.out.println("interestList###1111"+interestList);
            java.util.Set set=interestAll.keySet();
            Object objKeySet[]=(Object[])set.toArray();
            ArrayList selectedRecord=(ArrayList)tblInterestTab.getDataArrayList().get(recordPosition);
            for(int i=0;i<interestAll.size();i++){
                
                
                HashMap tempMap=(HashMap)interestAll.get(objKeySet[i]);
                singleList=new ArrayList();
                Date fromDt=getProperDateFormat(tempMap.get("FROM_DATE"));
                Date toDate=getProperDateFormat(tempMap.get(TO_DATE));
                String  activeStatus=CommonUtil.convertObjToStr(tempMap.get("INT_ACTIVE_STATUS"));
                if(DateUtil.dateDiff(fromDt, roiDate)==0){
                    
                    double toAmt=CommonUtil.convertObjToDouble(tempMap.get(TO_AMOUNT)).doubleValue();
                    if(existAmt == toAmt){
                        tempMap.put(TO_AMOUNT,new Double(Amt));
                        //                        interestList.set(i,tempMap);
                        interestAll.put(objKeySet[i],tempMap);
                        setTableList(tempMap,i,selectedRecord);
                    }
                    toAmt=CommonUtil.convertObjToDouble(tempMap.get(FROM_AMOUNT)).doubleValue();
                    if(existAmt+1 ==toAmt){
                        tempMap.put(FROM_AMOUNT,new Double(Amt+1));
                        interestAll.put(objKeySet[i],tempMap);
                        setTableList(tempMap,i,selectedRecord);
                    }
                    
                    if(changeDt!=null){
                        tempMap.put(FROM_DATE,changeDt);
                        interestAll.put(objKeySet[i],tempMap);
                        setTableList(tempMap,i,selectedRecord);
                    }
                }
                if(toDate !=null && existFromDt !=null && DateUtil.dateDiff(existFromDt, toDate)==0){
                    tempMap.put(TO_DATE,DateUtil.addDays(changeDt,-1));
                    interestAll.put(objKeySet[i],tempMap);
                    setTableList(tempMap,i,selectedRecord);
                }
                
            }
        }
        //        System.out.println("interestList###"+interestList);
    }
    public void setTableList(HashMap  tempTo,int i,ArrayList selectedRecord){
        ArrayList singleList=new ArrayList();
        singleList.add(CommonUtil.convertObjToStr(tempTo.get(SLNO)));
        singleList.add(CommonUtil.convertObjToStr(tempTo.get(FROM_DATE)));
        singleList.add(CommonUtil.convertObjToStr(tempTo.get(TO_DATE)));
        singleList.add(CommonUtil.convertObjToStr(tempTo.get(FROM_AMOUNT)));
        singleList.add(CommonUtil.convertObjToStr(tempTo.get(TO_AMOUNT)));
        singleList.add(CommonUtil.convertObjToStr(tempTo.get(INTEREST)));
        singleList.add(CommonUtil.convertObjToStr(selectedRecord.get(6)));
        singleList.add(CommonUtil.convertObjToStr(tempTo.get(AUTHORIZE_STATUS)));
        tblInterestTab.removeRow(i);
        tblInterestTab.insertRow(i,singleList);
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
        setAuthorizeStatus("");
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
    
    /**
     * Getter for property intAuthStatusMap.
     * @return Value of property intAuthStatusMap.
     */
    public java.util.HashMap getIntAuthStatusMap() {
        return intAuthStatusMap;
    }
    
    /**
     * Setter for property intAuthStatusMap.
     * @param intAuthStatusMap New value of property intAuthStatusMap.
     */
    public void setIntAuthStatusMap(java.util.HashMap intAuthStatusMap) {
        this.intAuthStatusMap = intAuthStatusMap;
    }
    
    /**
     * Getter for property nextSlabList.
     * @return Value of property nextSlabList.
     */
    public java.util.HashMap getNextSlabList() {
        return nextSlabList;
    }
    
    /**
     * Setter for property nextSlabList.
     * @param nextSlabList New value of property nextSlabList.
     */
    public void setNextSlabList(java.util.HashMap nextSlabList) {
        this.nextSlabList = nextSlabList;
    }
    
}
