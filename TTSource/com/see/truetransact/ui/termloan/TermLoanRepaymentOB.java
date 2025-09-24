/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition.
 * 
 *
 * TermLoanRepaymentOB.java
 *
 * Created on July 2, 2004, 5:14 PM
 */

package com.see.truetransact.ui.termloan;

/**
 *
 * @author  shanmuga
 */

import com.see.truetransact.clientproxy.ProxyParameters;
import com.see.truetransact.clientutil.ClientConstants;
import com.see.truetransact.clientutil.ClientUtil;
import com.see.truetransact.clientutil.ComboBoxModel;
import com.see.truetransact.clientutil.EnhancedTableModel;
import com.see.truetransact.clientutil.TableUtil;
import com.see.truetransact.clientexception.ClientParseException;
import com.see.truetransact.commonutil.CommonConstants;
import com.see.truetransact.commonutil.CommonUtil;
import com.see.truetransact.commonutil.DateUtil;
import com.see.truetransact.commonutil.interestcalc.InterestCalculationConstants;
import com.see.truetransact.commonutil.TTException;
import com.see.truetransact.transferobject.termloan.TermLoanInstallmentTO;
import com.see.truetransact.transferobject.termloan.TermLoanInstallMultIntTO;
import com.see.truetransact.transferobject.termloan.TermLoanRepaymentTO;
import com.see.truetransact.ui.termloan.emicalculator.TermLoanInstallmentOB;
import com.see.truetransact.ui.TrueTransactMain;
import com.see.truetransact.uicomponent.CObservable;
import com.see.truetransact.uicomponent.COptionPane;
import java.util.*;

import org.apache.log4j.Logger;


public class TermLoanRepaymentOB extends CObservable{
    
    /** Creates a new instance of TermLoanRepaymentOB */
    private TermLoanRepaymentOB()  throws Exception{
        curDate = ClientUtil.getCurrentDate();
        termLoanRepaymentOB();
    }
    
    private       static TermLoanRepaymentOB termLoanRepaymentOB;
    
    private final static Logger log = Logger.getLogger(TermLoanRepaymentOB.class);
    private final static ClientParseException parseException = ClientParseException.getInstance();
    
    private final   String  ACCOUNT_NO = "ACCOUNT_NO";
    private final   String  ACTIVE_STATUS = "ACTIVE_STATUS";
    private final   String  ADD_SIS = "ADD_SIS";
    private final   String  ALL_RECORDS = "ALL_RECORDS";
    private final   String  ALL_VALUES = "ALL_VALUES";
    private final   String  BALANCE = "BALANCE";
    private final   String  COMMAND = "COMMAND";
    private final   String  DISBURSEMENT_AMT = "DISBURSEMENT_AMT";
    private final   String  DISBURSEMENT_DT = "DISBURSEMENT_DT";
    private final   String  DISBURSEMENT_ID = "DISBURSEMENT_ID";
    private final   String  FROM_DATE = "FROM_DATE";
    private final   String  INSTAL_STDT="INSTAL_STDT";
    private final   String  INSERT = "INSERT";
    private final   String  INSTALLMENT_DATE = "INSTALLMENT_DATE";
    private final   String  INSTALLMENT_DETAILS = "INSTALLMENT_DETAILS";
    private final   String  INSTALLMULTINT_DETAILS = "INSTALLMULTINT_DETAILS";
    private final   String  INTEREST_AMOUNT = "INTEREST_AMOUNT";
    private final   String  INTEREST_RATE = "INTEREST_RATE";
    private final   String  LAST_INSTALL_AMT = "LAST_INSTALL_AMT";
    private final   String  LOAN_AMT = "LOAN_AMT";
    private final   String  LUMP_SUM = "LUMP_SUM";
    private final   String  MORATORIUM_GIVEN = "MORATORIUM_GIVEN";
    private final   String  MORATORIUM_INTEREST = "MORATORIUM_INTEREST";
    private final   String  MULTIPLE_INTEREST = "MULTIPLE_INTEREST";
    private final   String  NO = "N";
    private final   String  NO_INSTALLMENTS = "NO_INSTALLMENTS";
    private final   String  NO_MORATORIUM = "NO_MORATORIUM";
    private final   String  OPTION = "OPTION";
    private final   String  PENULTI_AMT_INSTALL = "PENULTI_AMT_INSTALL";
    private final   String  POST_DT_CHQ_ALLOW = "POST_DT_CHQ_ALLOW";
    private final   String  PRINCIPAL = "PRINCIPAL";
    private final   String  REF_SCHEDULE_NUMBER = "REF_SCHEDULE_NUMBER";
    private final   String  REPAY_FREQ = "REPAY_FREQ";
    private final   String  REPAY_TYPE = "REPAY_TYPE";
    private final   String  REPAYMENT_DETAILS = "REPAYMENT_DETAILS";
    private final   String  INSTALMENT_ALL="INSTALMENT_ALL";
    private final   String  SCHEDULE_MODE = "SCHEDULE_MODE";
    private final   String  REPAY_MOROTORIUM="REPAY_MOROTORIUM";
    private final   String  SLNO = "SLNO";
    private final   String  STATUS_REPAY = "STATUS_REPAY";
    private final   String  TABLE_VALUES = "TABLE_VALUES";
    private final   String  TO_DATE = "TO_DATE";
    private final   String  TOT_BASE_AMT = "TOT_BASE_AMT";
    private final   String  TOT_INSTALL_AMT = "TOT_INSTALL_AMT";
    private final   String  TOTAL = "TOTAL";
    private final   String  YES = "Y";
    private final   String  UPDATE = "UPDATE";
    private final   ArrayList repaymentTabTitle = new ArrayList();      //  Table Title of Repayment
    private ArrayList repaymentTabValues = new ArrayList();
    private ArrayList repaymentEachTabRecord;
    private LinkedHashMap repaymentAll = new LinkedHashMap();          // Both displayed and hidden values in the table
    private LinkedHashMap installmentAll = new LinkedHashMap();
    private LinkedHashMap moratoriumInterestAll = new LinkedHashMap();
    
    private HashMap repaymentEachRecord;
    private EnhancedTableModel tblRepaymentTab;
    private TableUtil tableUtilRepayment = new TableUtil();
    //    private final   TermLoanRB objTermLoanRB = new TermLoanRB();
    
    private final   java.util.ResourceBundle objTermLoanRB = java.util.ResourceBundle.getBundle("com.see.truetransact.ui.termloan.TermLoanRB", ProxyParameters.LANGUAGE);
    
    private ComboBoxModel cbmRepayFreq_Repayment;
    private ComboBoxModel cbmRepayType;
    private ComboBoxModel cbmSanRepaymentType;
    
    private String borrowerNo = "";
    private String txtScheduleNo = "";
    private String txtLaonAmt = "";
    private String cboRepayFreq_Repayment = "";
    private String cboRepayType = "";
    private String cboSanRepaymentType="";
    private String txtNoMonthsMora = "";
    private String tdtFirstInstall = "";
    private String tdtLastInstall = "";
    private String txtTotalBaseAmt = "";
    private String txtAmtPenulInstall = "";
    private String txtAmtLastInstall = "";
    private String txtTotalInstallAmt = "";
    private String tdtDisbursement_Dt = "";
    private String tdtRepayFromDate ="";
    private boolean rdoDoAddSIs_Yes = false;
    private boolean rdoDoAddSIs_No = false;
    private boolean rdoPostDatedCheque_Yes = false;
    private boolean rdoPostDatedCheque_No = false;
    private boolean rdoActive_Repayment = false;
    private boolean rdoInActive_Repayment = false;
    private String txtNoInstall = "";
    private String txtRepayScheduleMode = "";
    private String lblAccHead_RS_2 = "";
    private String lblAccNo_RS_2 = "";
    private String lblProdID_RS_Disp = "";
    private String strRefScheduleNumber = "";
    private String strACNumber = "";
    private String strLimitAmt = "";
    private String strDisbursementID = "";
    private String disbursementDate = "";
    private double interest_rate = 0.0;
    private double emi = 0.0;
    private boolean allowMultiRepay = true;                             // Flag to allow Multiple Repayment Schedule
    private double activeLoanAmt = 0.0;
    private HashMap uptoDtInterestMap=null;
    private boolean  newMode =false;
    private boolean chkEmiUniform = false;

    public boolean isChkEmiUniform() {
        return chkEmiUniform;
    }

    public void setChkEmiUniform(boolean chkEmiUniform) {
        this.chkEmiUniform = chkEmiUniform;
    }
    
    public boolean isNewMode() {
        return newMode;
    }

    public void setNewMode(boolean newMode) {
        this.newMode = newMode;
    }
    
    Date curDate = null;
    
    static {
        try {
            termLoanRepaymentOB = new TermLoanRepaymentOB();
        } catch(Exception e) {
            log.info("try: " + e);
            parseException.logException(e,true);
        }
    }
    
    private void termLoanRepaymentOB()  throws Exception{
        setRepaymentTabTitle();
        tableUtilRepayment.setAttributeKey(SLNO);
        tblRepaymentTab = new EnhancedTableModel(null, repaymentTabTitle);
    }
    
    public static TermLoanRepaymentOB getInstance() {
        return termLoanRepaymentOB;
    }
    
    private void setRepaymentTabTitle() throws Exception{
        try{
            repaymentTabTitle.add(objTermLoanRB.getString("tblColumnRepaymentScheduleNo"));
            repaymentTabTitle.add(objTermLoanRB.getString("tblColumnRepaymentLoanAmount"));
            repaymentTabTitle.add(objTermLoanRB.getString("tblColumnRepaymentSceduleMode"));
            repaymentTabTitle.add(objTermLoanRB.getString("tblColumnRepaymentRefScheduleNo"));
        }catch(Exception e){
            log.info("Exception in setRepaymentTabTitle(): "+e);
            parseException.logException(e,true);
        }
    }
    
    public ArrayList getRepaymentTabTitle(){
        return this.repaymentTabTitle;
    }
    
    public void resetRepaymentCTable(){
        tblRepaymentTab.setDataArrayList(null, repaymentTabTitle);
        tableUtilRepayment = new TableUtil();
        tableUtilRepayment.setAttributeKey(SLNO);
    }
    
    public void resetAllRepayment(){
        setLblAccHead_RS_2("");
        setLblAccNo_RS_2("");
        setLblProdID_RS_Disp("");
        setActiveLoanAmt(0.0);
        resetRepaymentSchedule();
      //  cbmSanRepaymentType.setKeyForSelected("");
    }
    
    public void resetRepaymentSchedule(){
        setTxtScheduleNo("");
        setTxtLaonAmt("");
        setCboRepayFreq_Repayment("");
        setCboSanRepaymentType("");
        cbmRepayFreq_Repayment.setKeyForSelected("");
        setCboRepayType("");
        cbmRepayType.setKeyForSelected("");
//        cbmSanRepaymentType.setKeyForSelected("");
        setTxtNoMonthsMora("");
        setTdtFirstInstall("");
        setTdtLastInstall("");
        setTxtNoInstall("");
        setTxtTotalBaseAmt("");
        setTxtAmtPenulInstall("");
        setTxtAmtLastInstall("");
        setTxtTotalInstallAmt("");
        setTdtDisbursement_Dt("");
        setTdtRepayFromDate("");
        setStrDisbursementID("");
        setTxtRepayScheduleMode("");
        setStrRefScheduleNumber("");
        setRdoDoAddSIs_Yes(false);
        setRdoDoAddSIs_No(false);
        setRdoPostDatedCheque_Yes(false);
        setRdoPostDatedCheque_No(false);
        setRdoActive_Repayment(false);
        setRdoInActive_Repayment(false);
        setInstallmentAll(null);
        setMoratoriumInterestAll(null);
    }
    
    public void changeStatusRepay(int resultType){
        try{
            // Repayment Details
            HashMap oneRecord;
            if (resultType != 2){
                //If the Main Save Button pressed
                tableUtilRepayment.getRemovedValues().clear();
            }
            java.util.Set keySet =  repaymentAll.keySet();
            Object[] objKeySet = (Object[]) keySet.toArray();
            // To change the Insert command to Update after Save Buttone Pressed
            // Repayment Details
            for (int i = keySet.size() - 1,j = 0;i >= 0;--i,++j){
                oneRecord = (HashMap) repaymentAll.get(objKeySet[j]);
                if (oneRecord.get(COMMAND).equals(INSERT)){
                    // If the status is in Insert Mode then change the mode to Update
                    oneRecord.put(COMMAND, UPDATE);
                    oneRecord.put(INSTALLMENT_DETAILS, null);
                    oneRecord.put(MORATORIUM_INTEREST, null);
                    repaymentAll.put(objKeySet[j], oneRecord);
                }
                oneRecord = null;
            }
        }catch(Exception e){
            log.info("Error in changeStatusRepay: "+e);
            parseException.logException(e,true);
        }
    }
        public int getRepayScheduleIncrementType(){
        int incType = 0;
        try{
            Double incVal = CommonUtil.convertObjToDouble(cbmRepayFreq_Repayment.getKeyForSelected());
            incType = incVal.intValue();
            incVal = null;
        }catch(Exception e){
            log.info("Exception caught in getIncrementType: "+e);
            parseException.logException(e,true);
        }
        return incType;
    }

    public void setTermLoanRepaymentTO(ArrayList repaymentList, String acctNum){
        try{
            double loanAmount = 0.0;
            setActiveLoanAmt(0.0);
            TermLoanRepaymentTO termLoanRepaymentTO;
            HashMap repaymentRecordMap;
            LinkedHashMap allRepaymentRecords = new LinkedHashMap();
            ArrayList removedValues = new ArrayList();
            ArrayList repaymentRecordList;
            ArrayList tabRepaymentRecords = new ArrayList();
            System.out.println("#### repaymentList : "+repaymentList);
            // To retrieve the Repayment Details from the Serverside
            for (int i = repaymentList.size() - 1,j = 0;i >= 0;--i,++j){
                termLoanRepaymentTO = (TermLoanRepaymentTO) repaymentList.get(j);
                repaymentRecordMap = new HashMap();
                repaymentRecordList = new ArrayList();
                
                repaymentRecordList.add(CommonUtil.convertObjToStr(termLoanRepaymentTO.getScheduleNo()));
                repaymentRecordList.add(CommonUtil.convertObjToStr(termLoanRepaymentTO.getLoanAmount()));
                repaymentRecordList.add(CommonUtil.convertObjToStr(termLoanRepaymentTO.getScheduleMode()));
                repaymentRecordList.add(CommonUtil.convertObjToStr(termLoanRepaymentTO.getRefScheduleNo()));
                
                tabRepaymentRecords.add(repaymentRecordList);
                
                repaymentRecordMap.put(SLNO, CommonUtil.convertObjToStr(termLoanRepaymentTO.getScheduleNo()));
                //                repaymentRecordMap.put(PRODUCT_ID, CommonUtil.convertObjToStr(termLoanRepaymentTO.getProdId()));
                repaymentRecordMap.put(ACCOUNT_NO, CommonUtil.convertObjToStr(termLoanRepaymentTO.getAcctNum()));
                if (CommonUtil.convertObjToStr(termLoanRepaymentTO.getAddSi()).equals(YES)){
                    repaymentRecordMap.put(ADD_SIS, YES);
                }else if (CommonUtil.convertObjToStr(termLoanRepaymentTO.getAddSi()).equals(NO)){
                    repaymentRecordMap.put(ADD_SIS, NO);
                }else{
                    repaymentRecordMap.put(ADD_SIS, " ");
                }
                if (CommonUtil.convertObjToStr(termLoanRepaymentTO.getPostDateChqallowed()).equals(YES)){
                    repaymentRecordMap.put(POST_DT_CHQ_ALLOW, YES);
                }else if(CommonUtil.convertObjToStr(termLoanRepaymentTO.getPostDateChqallowed()).equals(NO)){
                    repaymentRecordMap.put(POST_DT_CHQ_ALLOW, NO);
                }else{
                    repaymentRecordMap.put(POST_DT_CHQ_ALLOW, " ");
                }
                repaymentRecordMap.put(LOAN_AMT, CommonUtil.convertObjToStr(termLoanRepaymentTO.getLoanAmount()));
                repaymentRecordMap.put(REPAY_TYPE, CommonUtil.convertObjToStr(termLoanRepaymentTO.getInstallType()));
                cbmSanRepaymentType.setKeyForSelected(CommonUtil.convertObjToStr(termLoanRepaymentTO.getInstallType()));
                setCboSanRepaymentType(CommonUtil.convertObjToStr(getCbmSanRepaymentType().getDataForKey(CommonUtil.convertObjToStr(termLoanRepaymentTO.getInstallType()))));
                System.out.println("sanRepaytype"+getCboSanRepaymentType());
                loanAmount = termLoanRepaymentTO.getLoanAmount().doubleValue();
                if (CommonUtil.convertObjToStr(termLoanRepaymentTO.getInstallType()).equals(LUMP_SUM)){
                    setAllowMultiRepay(false);
                }
                if (termLoanRepaymentTO.getRepayActive() != null){
                    if (termLoanRepaymentTO.getRepayActive().equals(YES)){
                        repaymentRecordMap.put(ACTIVE_STATUS, YES);
                        setActiveLoanAmt(activeLoanAmt + loanAmount);
                    }else if (termLoanRepaymentTO.getRepayActive().equals(NO)){
                        repaymentRecordMap.put(ACTIVE_STATUS, NO);
                    }else{
                        repaymentRecordMap.put(ACTIVE_STATUS, "");
                    }
                }
                
                if (CommonUtil.convertObjToStr(CommonConstants.SAL_REC_MODULE).equals("Y")) {
                    if (termLoanRepaymentTO.getEmi_uniform().equals("Y")) {
                        setChkEmiUniform(true);
                    } else {
                        setChkEmiUniform(false);
                    }
                }
                
                repaymentRecordMap.put(REPAY_FREQ, CommonUtil.convertObjToStr(termLoanRepaymentTO.getRepaymentType()));
                repaymentRecordMap.put(NO_INSTALLMENTS, CommonUtil.convertObjToStr(termLoanRepaymentTO.getNoInstallments()));
                repaymentRecordMap.put(FROM_DATE, DateUtil.getStringDate(termLoanRepaymentTO.getFromDate()));
                repaymentRecordMap.put(INSTAL_STDT, DateUtil.getStringDate(termLoanRepaymentTO.getFirstInstallDt()));
                repaymentRecordMap.put(TO_DATE, DateUtil.getStringDate(termLoanRepaymentTO.getLastInstallDt()));
                repaymentRecordMap.put(TOT_BASE_AMT, CommonUtil.convertObjToStr(termLoanRepaymentTO.getTotalBaseAmt()));
                repaymentRecordMap.put(TOT_INSTALL_AMT, CommonUtil.convertObjToStr(termLoanRepaymentTO.getTotalInstallAmt()));
                repaymentRecordMap.put(LAST_INSTALL_AMT, CommonUtil.convertObjToStr(termLoanRepaymentTO.getAmtLastInstall()));
                repaymentRecordMap.put(PENULTI_AMT_INSTALL, CommonUtil.convertObjToStr(termLoanRepaymentTO.getAmtPenultimateInstall()));
                repaymentRecordMap.put(DISBURSEMENT_ID, CommonUtil.convertObjToStr(termLoanRepaymentTO.getDisbursementId()));
                repaymentRecordMap.put(SCHEDULE_MODE, CommonUtil.convertObjToStr(termLoanRepaymentTO.getScheduleMode()));
                repaymentRecordMap.put(REF_SCHEDULE_NUMBER, CommonUtil.convertObjToStr(termLoanRepaymentTO.getRefScheduleNo()));
                repaymentRecordMap.put(DISBURSEMENT_DT, DateUtil.getStringDate(termLoanRepaymentTO.getDisbursementDt()));
                repaymentRecordMap.put(REPAY_MOROTORIUM, CommonUtil.convertObjToStr(termLoanRepaymentTO.getRepayMorotorium()));
                repaymentRecordMap.put(INSTALLMENT_DETAILS, null);
                repaymentRecordMap.put(MORATORIUM_INTEREST, null);
                repaymentRecordMap.put(COMMAND, UPDATE);
                
                allRepaymentRecords.put(CommonUtil.convertObjToStr(termLoanRepaymentTO.getScheduleNo()), repaymentRecordMap);
                
                repaymentRecordList = null;
                repaymentRecordMap = null;
            }
            repaymentAll.clear();
            repaymentTabValues.clear();
            repaymentAll = allRepaymentRecords;
            repaymentTabValues = tabRepaymentRecords;
            System.out.println("#### repaymentTabTitle : "+repaymentTabTitle);
            System.out.println("#### repaymentTabValues : "+repaymentTabValues);
            tblRepaymentTab.setDataArrayList(repaymentTabValues, repaymentTabTitle);
            tableUtilRepayment.setRemovedValues(removedValues);
            tableUtilRepayment.setAllValues(repaymentAll);
            tableUtilRepayment.setTableValues(repaymentTabValues);
            setMax_Del_Schedule_No(acctNum);
            tabRepaymentRecords = null;
            allRepaymentRecords = null;
        }catch(Exception e){
            log.info("Error in setTermLoanRepaymentTO: "+e);
            parseException.logException(e,true);
        }
    }
    
    private void setMax_Del_Schedule_No(String act_Num){
        try{
            HashMap transactionMap = new HashMap();
            HashMap retrieve = new HashMap();
            transactionMap.put("acctNum", act_Num);
            List resultList = ClientUtil.executeQuery("getSelectTermLoanRepaymentMaxSLNO", transactionMap);
            if (resultList.size() > 0){
                retrieve = (HashMap) resultList.get(0);
                tableUtilRepayment.setStr_MAX_DEL_SL_NO(CommonUtil.convertObjToStr(retrieve.get("MAX_SCHEDULE_NO")));
            }
            retrieve = null;
            transactionMap = null;
            resultList = null;
        }catch(Exception e){
            log.info("Error In setMax_Del_Schedule_No: "+e);
            parseException.logException(e,true);
        }
    }
    
    public HashMap setTermLoanRepayment( boolean isnew){
        HashMap repayInstallmentMap = new HashMap();
        HashMap repaymentMap = new HashMap();
        HashMap installmentMap = new HashMap();
        HashMap installMultIntMap = new HashMap();
        boolean reschudleavailable=false;
        try{
            LinkedHashMap interestRecords;
            HashMap interestOneRecord;
            TermLoanRepaymentTO termLoanRepaymentTO;
            TermLoanInstallmentTO termLoanInstallmentTO;
            TermLoanInstallMultIntTO termLoanInstallMultIntTO;
            ArrayList installMultiIntList;
            java.util.Set interestKeySet;
            Object[] objInterestKeySet;
            java.util.Set keySet =  repaymentAll.keySet();
            Object[] objKeySet = (Object[]) keySet.toArray();
            HashMap oneRecord;
            // To set the values for Insurance Transfer Object
            for (int i = keySet.size() - 1,j = 0;i >= 0;--i,++j){
                oneRecord = (HashMap) repaymentAll.get(objKeySet[j]);
                termLoanRepaymentTO = new TermLoanRepaymentTO();
                termLoanRepaymentTO.setAcctNum(getStrACNumber());
                //                termLoanRepaymentTO.setBorrowNo(getBorrowerNo());
                termLoanRepaymentTO.setScheduleNo(CommonUtil.convertObjToDouble(oneRecord.get(SLNO)));
                termLoanRepaymentTO.setCommand(CommonUtil.convertObjToStr(oneRecord.get(COMMAND)));
                if (oneRecord.get(ADD_SIS).equals(YES)){
                    termLoanRepaymentTO.setAddSi(YES);
                }else if (oneRecord.get(ADD_SIS).equals(NO)){
                    termLoanRepaymentTO.setAddSi(NO);
                }else{
                    termLoanRepaymentTO.setAddSi(" ");
                }
                termLoanRepaymentTO.setAmtLastInstall(CommonUtil.convertObjToDouble(oneRecord.get(LAST_INSTALL_AMT)));
                termLoanRepaymentTO.setAmtPenultimateInstall(CommonUtil.convertObjToDouble(oneRecord.get(PENULTI_AMT_INSTALL)));
                
//                termLoanRepaymentTO.setFirstInstallDt((Date)oneRecord.get(FROM_DATE));
                termLoanRepaymentTO.setFromDate(getProperDateFormat(oneRecord.get(FROM_DATE)));
                termLoanRepaymentTO.setFirstInstallDt(getProperDateFormat(oneRecord.get(INSTAL_STDT)));
                if(isnew)
                    termLoanRepaymentTO.setRepaymentType(CommonUtil.convertObjToDouble(getCbmSanRepaymentType().getKeyForSelected()));
                 else
                    termLoanRepaymentTO.setRepaymentType(CommonUtil.convertObjToDouble(oneRecord.get(REPAY_FREQ)));
                
//                termLoanRepaymentTO.setLastInstallDt((Date)oneRecord.get(TO_DATE));
                termLoanRepaymentTO.setLastInstallDt(getProperDateFormat(oneRecord.get(TO_DATE)));
                termLoanRepaymentTO.setLoanAmount(CommonUtil.convertObjToDouble(oneRecord.get(LOAN_AMT)));
                termLoanRepaymentTO.setNoInstallments(CommonUtil.convertObjToDouble(oneRecord.get(NO_INSTALLMENTS)));
                if (oneRecord.get(POST_DT_CHQ_ALLOW).equals(YES)){
                    termLoanRepaymentTO.setPostDateChqallowed(YES);
                }else if (oneRecord.get(POST_DT_CHQ_ALLOW).equals(NO)){
                    termLoanRepaymentTO.setPostDateChqallowed(NO);
                }else{
                    termLoanRepaymentTO.setPostDateChqallowed(" ");
                }
                if (oneRecord.get(ACTIVE_STATUS).equals(YES)){
                    termLoanRepaymentTO.setRepayActive(YES);
                }else if (oneRecord.get(ACTIVE_STATUS).equals(NO)){
                    termLoanRepaymentTO.setRepayActive(NO);
                }else{
                    termLoanRepaymentTO.setRepayActive(" ");
                }
                //                termLoanRepaymentTO.setProdId(CommonUtil.convertObjToStr(getLblProdID_RS_Disp()));
                termLoanRepaymentTO.setInstallType(CommonUtil.convertObjToStr(oneRecord.get(REPAY_TYPE)));
                termLoanRepaymentTO.setTotalBaseAmt(CommonUtil.convertObjToDouble(oneRecord.get(TOT_BASE_AMT)));
                termLoanRepaymentTO.setTotalInstallAmt(CommonUtil.convertObjToDouble(oneRecord.get(TOT_INSTALL_AMT)));
                
//                termLoanRepaymentTO.setDisbursementDt((Date)oneRecord.get(DISBURSEMENT_DT));
                termLoanRepaymentTO.setDisbursementDt(getProperDateFormat(oneRecord.get(DISBURSEMENT_DT)));
                // These fields are in server side and not in UI
                //            termLoanRepaymentTO.setRepayInterest(new);
                //            termLoanRepaymentTO.setIntType();
                //            termLoanRepaymentTO.setEmi();
                //            termLoanRepaymentTO.setRepaymentPr();
                //            termLoanRepaymentTO.setBalanceLoanAmt();
                termLoanRepaymentTO.setDisbursementId(CommonUtil.convertObjToDouble(oneRecord.get(DISBURSEMENT_ID)));
                termLoanRepaymentTO.setRepayMorotorium(CommonUtil.convertObjToDouble(oneRecord.get(REPAY_MOROTORIUM)));
                termLoanRepaymentTO.setScheduleMode(CommonUtil.convertObjToStr(oneRecord.get(SCHEDULE_MODE)));
                termLoanRepaymentTO.setRefScheduleNo(CommonUtil.convertObjToDouble(oneRecord.get(REF_SCHEDULE_NUMBER)));
                if (oneRecord.get(COMMAND).equals(INSERT)){
                    termLoanRepaymentTO.setStatus(CommonConstants.STATUS_CREATED);
                }else if (oneRecord.get(COMMAND).equals(UPDATE)){
                    termLoanRepaymentTO.setStatus(CommonConstants.STATUS_MODIFIED);
                }
                termLoanRepaymentTO.setStatusBy(TrueTransactMain.USER_ID);
                termLoanRepaymentTO.setStatusDt(curDate);
                
                repaymentMap.put(String.valueOf(j+1), termLoanRepaymentTO);
                if (oneRecord.get(INSTALLMENT_DETAILS) != null){  // && (oneRecord.get(COMMAND).equals(INSERT))  //Condition removed to insert in update mode also
                    interestRecords = ((LinkedHashMap)oneRecord.get(INSTALLMENT_DETAILS));
                    interestKeySet =  interestRecords.keySet();
                    objInterestKeySet = (Object[]) interestKeySet.toArray();
                    int noOfInstallment = interestKeySet.size();
                    for (int m = noOfInstallment - 1;m >= 0;--m){
                        interestOneRecord = (HashMap) interestRecords.get(objInterestKeySet[m]);
                        termLoanInstallmentTO = new TermLoanInstallmentTO();
                        termLoanInstallmentTO.setAcctNum(getStrACNumber());
                        termLoanInstallmentTO.setBalanceAmt(CommonUtil.convertObjToDouble(interestOneRecord.get(BALANCE)));
                        termLoanInstallmentTO.setCommand(CommonUtil.convertObjToStr(interestOneRecord.get(COMMAND)));
                        
//                        termLoanInstallmentTO.setInstallmentDt((Date)interestOneRecord.get(INSTALLMENT_DATE));
                        termLoanInstallmentTO.setInstallmentDt(getProperDateFormat(interestOneRecord.get(INSTALLMENT_DATE)));
                        termLoanInstallmentTO.setInstallmentSlno(CommonUtil.convertObjToDouble(interestOneRecord.get(SLNO)));
                        termLoanInstallmentTO.setInterestAmt(CommonUtil.convertObjToDouble(interestOneRecord.get(INTEREST_AMOUNT)));
                        termLoanInstallmentTO.setInterestRate(CommonUtil.convertObjToDouble(interestOneRecord.get(INTEREST_RATE)));
                        termLoanInstallmentTO.setPrincipalAmt(CommonUtil.convertObjToDouble(interestOneRecord.get(PRINCIPAL)));
                        termLoanInstallmentTO.setScheduleId(CommonUtil.convertObjToDouble(oneRecord.get(SLNO)));
                        termLoanInstallmentTO.setTotalAmt(CommonUtil.convertObjToDouble(interestOneRecord.get(TOTAL)));
                         termLoanInstallmentTO.setActiveStatus(CommonUtil.convertObjToStr(interestOneRecord.get("ACTIVE_STATUS")));
                        termLoanInstallmentTO.setInstallmentPaid(NO);
                        if (interestOneRecord.containsKey(MULTIPLE_INTEREST)){
                            installMultiIntList = (ArrayList) interestOneRecord.get(MULTIPLE_INTEREST);
                            HashMap multiIntRec;
                            for (int n = installMultiIntList.size()-1;n>=0;--n){
                                multiIntRec = (HashMap) installMultiIntList.get(n);
                                termLoanInstallMultIntTO = new TermLoanInstallMultIntTO();
                                termLoanInstallMultIntTO.setAcctNum(getStrACNumber());
                                if (interestOneRecord.get(COMMAND).equals(INSERT)){
                                    termLoanInstallMultIntTO.setCommand(CommonConstants.TOSTATUS_INSERT);
                                }else if (interestOneRecord.get(COMMAND).equals(UPDATE)){
                                    termLoanInstallMultIntTO.setCommand(CommonConstants.TOSTATUS_UPDATE);
                                }
                                termLoanInstallMultIntTO.setInstallmentSlno(termLoanInstallmentTO.getInstallmentSlno());
                                termLoanInstallMultIntTO.setInterestRate(CommonUtil.convertObjToDouble(multiIntRec.get(INTEREST_RATE)));
                                termLoanInstallMultIntTO.setFromDt(getProperDateFormat(multiIntRec.get(FROM_DATE)));
                                termLoanInstallMultIntTO.setToDt(getProperDateFormat(multiIntRec.get(TO_DATE)));
                                
//                                termLoanInstallMultIntTO.setFromDt((Date)multiIntRec.get(FROM_DATE));
//                                
//                                termLoanInstallMultIntTO.setToDt((Date)multiIntRec.get(TO_DATE));
                                termLoanInstallMultIntTO.setScheduleId(termLoanInstallmentTO.getScheduleId());
                                installMultIntMap.put(String.valueOf(installMultIntMap.size()), termLoanInstallMultIntTO);
                                termLoanInstallMultIntTO = null;
                                multiIntRec = null;
                            }
                            multiIntRec = null;
                            termLoanInstallMultIntTO = null;
                            installMultiIntList = null;
                        }
                        if (interestOneRecord.get(COMMAND).equals(INSERT)){
                            termLoanInstallmentTO.setStatus(CommonConstants.STATUS_CREATED);
                            reschudleavailable=true;
                        }else if (interestOneRecord.get(COMMAND).equals(UPDATE)){
                            termLoanInstallmentTO.setStatus(CommonConstants.STATUS_MODIFIED);
                        }
                        installmentMap.put(String.valueOf(installmentMap.size()+1), termLoanInstallmentTO);
                        interestOneRecord = null;
                    }
                }
                if (oneRecord.get(MORATORIUM_INTEREST) != null && (oneRecord.get(COMMAND).equals(INSERT))){
                    interestRecords = ((LinkedHashMap)oneRecord.get(MORATORIUM_INTEREST));
                    interestKeySet =  interestRecords.keySet();
                    objInterestKeySet = (Object[]) interestKeySet.toArray();
                    HashMap moratoriumIntRec = (HashMap) interestRecords.get(objInterestKeySet[0]);
                    if (moratoriumIntRec.containsKey(MULTIPLE_INTEREST)){
                        installMultiIntList = (ArrayList) moratoriumIntRec.get(MULTIPLE_INTEREST);
                        HashMap multiIntRec;
                        for (int n = installMultiIntList.size()-1;n>=0;--n){
                            multiIntRec = (HashMap) installMultiIntList.get(n);
                            termLoanInstallMultIntTO = new TermLoanInstallMultIntTO();
                            termLoanInstallMultIntTO.setAcctNum(getStrACNumber());
                            if (oneRecord.get(COMMAND).equals(INSERT)){
                                termLoanInstallMultIntTO.setCommand(CommonConstants.TOSTATUS_INSERT);
                            }else if (oneRecord.get(COMMAND).equals(UPDATE)){
                                termLoanInstallMultIntTO.setCommand(CommonConstants.TOSTATUS_UPDATE);
                            }
                            termLoanInstallMultIntTO.setInterestRate(CommonUtil.convertObjToDouble(multiIntRec.get(INTEREST_RATE)));
                            termLoanInstallMultIntTO.setFromDt(getProperDateFormat(multiIntRec.get(FROM_DATE)));
                            termLoanInstallMultIntTO.setToDt(getProperDateFormat(multiIntRec.get(TO_DATE)));
                            
//                             termLoanInstallMultIntTO.setFromDt((Date)multiIntRec.get(FROM_DATE));
//                             
//                            termLoanInstallMultIntTO.setToDt((Date)multiIntRec.get(TO_DATE));
                            termLoanInstallMultIntTO.setScheduleId(CommonUtil.convertObjToDouble(oneRecord.get(SLNO)));
                            installMultIntMap.put(String.valueOf(installMultIntMap.size()), termLoanInstallMultIntTO);
                            termLoanInstallMultIntTO = null;
                            multiIntRec = null;
                        }
                        multiIntRec = null;
                        termLoanInstallMultIntTO = null;
                        installMultiIntList = null;
                    }
                    moratoriumIntRec = null;
                }
                oneRecord = null;
                termLoanRepaymentTO = null;
            }
            
            // To set the values for Repayment Transfer Object
            // where as the existing records in Database are deleted in client side
            // useful for updating the status
            for (int i = tableUtilRepayment.getRemovedValues().size() - 1,j = 0;i >= 0;--i,++j){
                oneRecord = (HashMap) tableUtilRepayment.getRemovedValues().get(j);
                termLoanRepaymentTO = new TermLoanRepaymentTO();
                termLoanRepaymentTO.setAcctNum(getStrACNumber());
                //                termLoanRepaymentTO.setBorrowNo(getBorrowerNo());
                termLoanRepaymentTO.setScheduleNo(CommonUtil.convertObjToDouble(oneRecord.get(SLNO)));
                termLoanRepaymentTO.setCommand(CommonUtil.convertObjToStr(oneRecord.get(COMMAND)));
                if (oneRecord.get(ADD_SIS).equals(YES)){
                    termLoanRepaymentTO.setAddSi(YES);
                }else if (oneRecord.get(ADD_SIS).equals(NO)){
                    termLoanRepaymentTO.setAddSi(NO);
                }else{
                    termLoanRepaymentTO.setAddSi(" ");
                }
                termLoanRepaymentTO.setAmtLastInstall(CommonUtil.convertObjToDouble(oneRecord.get(LAST_INSTALL_AMT)));
                termLoanRepaymentTO.setAmtPenultimateInstall(CommonUtil.convertObjToDouble(oneRecord.get(PENULTI_AMT_INSTALL)));
                
//                termLoanRepaymentTO.setFirstInstallDt((Date)oneRecord.get(FROM_DATE));
                termLoanRepaymentTO.setFromDate(getProperDateFormat(oneRecord.get(FROM_DATE)));
                termLoanRepaymentTO.setFirstInstallDt(getProperDateFormat(oneRecord.get(INSTAL_STDT)));
                termLoanRepaymentTO.setRepaymentType(CommonUtil.convertObjToDouble(oneRecord.get(REPAY_FREQ)));
                
//                termLoanRepaymentTO.setLastInstallDt((Date)oneRecord.get(TO_DATE));
                termLoanRepaymentTO.setLastInstallDt(getProperDateFormat(oneRecord.get(TO_DATE)));
                termLoanRepaymentTO.setLoanAmount(CommonUtil.convertObjToDouble(oneRecord.get(LOAN_AMT)));
                //                if (oneRecord.get(MORATORIUM_GIVEN).equals(YES)){
                //                    termLoanRepaymentTO.setMoratoriumGiven(YES);
                //                }else{
                //                    termLoanRepaymentTO.setMoratoriumGiven(NO);
                //                }
                termLoanRepaymentTO.setNoInstallments(CommonUtil.convertObjToDouble(oneRecord.get(NO_INSTALLMENTS)));
                //                termLoanRepaymentTO.setNoMoratorium(CommonUtil.convertObjToDouble(oneRecord.get(NO_MORATORIUM)));
                if (oneRecord.get(POST_DT_CHQ_ALLOW).equals(YES)){
                    termLoanRepaymentTO.setPostDateChqallowed(YES);
                }else if (oneRecord.get(POST_DT_CHQ_ALLOW).equals(NO)){
                    termLoanRepaymentTO.setPostDateChqallowed(NO);
                }else{
                    termLoanRepaymentTO.setPostDateChqallowed(" ");
                }
                if (oneRecord.get(ACTIVE_STATUS).equals(YES)){
                    termLoanRepaymentTO.setRepayActive(YES);
                }else if (oneRecord.get(ACTIVE_STATUS).equals(NO)){
                    termLoanRepaymentTO.setRepayActive(NO);
                }else{
                    termLoanRepaymentTO.setRepayActive(" ");
                }
                //                termLoanRepaymentTO.setProdId(CommonUtil.convertObjToStr(getLblProdID_RS_Disp()));
                termLoanRepaymentTO.setInstallType(CommonUtil.convertObjToStr(oneRecord.get(REPAY_TYPE)));
                termLoanRepaymentTO.setTotalBaseAmt(CommonUtil.convertObjToDouble(oneRecord.get(TOT_BASE_AMT)));
                termLoanRepaymentTO.setTotalInstallAmt(CommonUtil.convertObjToDouble(oneRecord.get(TOT_INSTALL_AMT)));
                
//                termLoanRepaymentTO.setDisbursementDt((Date)oneRecord.get(DISBURSEMENT_DT));
                termLoanRepaymentTO.setDisbursementDt(getProperDateFormat(oneRecord.get(DISBURSEMENT_DT)));
                // These fields are in server side and not in UI
                //            termLoanRepaymentTO.setRepayInterest(new);
                //            termLoanRepaymentTO.setIntType();
                //            termLoanRepaymentTO.setEmi();
                //            termLoanRepaymentTO.setRepaymentPr();
                //            termLoanRepaymentTO.setBalanceLoanAmt();
                termLoanRepaymentTO.setDisbursementId(CommonUtil.convertObjToDouble(oneRecord.get(DISBURSEMENT_ID)));
                termLoanRepaymentTO.setScheduleMode(CommonUtil.convertObjToStr(oneRecord.get(SCHEDULE_MODE)));
                termLoanRepaymentTO.setRefScheduleNo(CommonUtil.convertObjToDouble(oneRecord.get(REF_SCHEDULE_NUMBER)));
                termLoanRepaymentTO.setStatus(CommonConstants.STATUS_DELETED);
                termLoanRepaymentTO.setStatusBy(TrueTransactMain.USER_ID);
                termLoanRepaymentTO.setStatusDt(curDate);
                repaymentMap.put(String.valueOf(repaymentMap.size()+1), termLoanRepaymentTO);
                
                termLoanInstallmentTO = new TermLoanInstallmentTO();
                termLoanInstallmentTO.setAcctNum(getStrACNumber());
                termLoanInstallmentTO.setCommand(CommonConstants.TOSTATUS_DELETE);
                termLoanInstallmentTO.setInstallmentSlno(CommonUtil.convertObjToDouble(""));
                termLoanInstallmentTO.setScheduleId(CommonUtil.convertObjToDouble(oneRecord.get(SLNO)));
                termLoanInstallmentTO.setStatus(CommonConstants.STATUS_DELETED);
                
                installmentMap.put(String.valueOf(installmentMap.size()+1), termLoanInstallmentTO);
                
                oneRecord = null;
                termLoanRepaymentTO = null;
            }
            keySet = null;
            objKeySet = null;
            interestKeySet = null;
            objInterestKeySet = null;
            termLoanInstallMultIntTO = null;
            installMultiIntList = null;
            repayInstallmentMap.put(REPAYMENT_DETAILS, repaymentMap);
            repayInstallmentMap.put(INSTALLMENT_DETAILS, installmentMap);
            repayInstallmentMap.put(INSTALLMULTINT_DETAILS, installMultIntMap);
            if(reschudleavailable && getUptoDtInterestMap() !=null &&  getUptoDtInterestMap().size()>0)
                repayInstallmentMap.put("INT_TRANSACTION_REPAYMENT",getUptoDtInterestMap());
        }catch(Exception e){
            log.info("Error In setTermLoanInsurance() "+e);
            parseException.logException(e,true);
        }
        return repayInstallmentMap;
    }
    
    public ArrayList getDeletedRepaymentScheduleNos () {
        ArrayList deletedList = new ArrayList();
        HashMap oneRecord;
        for (int i = tableUtilRepayment.getRemovedValues().size() - 1,j = 0;i >= 0;--i,++j){
            oneRecord = (HashMap) tableUtilRepayment.getRemovedValues().get(j);
            deletedList.add(oneRecord.get(SLNO));
        }
        oneRecord=null;
        return deletedList;
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
    
    public void setInterest_Rate(double interest_rate){
        this.interest_rate = interest_rate;
    }
    public double getInterest_Rate(){
        return this.interest_rate;
    }
    
    private void setEMI(double emi){
        this.emi = emi;
    }
    private double getEMI(){
        return this.emi;
    }
    
    public void calculateTotalBaseAmount(HashMap repayData){
        // Don't callt this method when there is no loan amount.
        try{
            if (getTxtLaonAmt().length() <= 0){
                return;
            }
            TermLoanInstallmentOB termLoanInstallmentOB = new TermLoanInstallmentOB();
            if (!repayData.containsKey(FROM_DATE))
                repayData.put("FROM_DATE", getDisbursementDate());
            HashMap resultMap = termLoanInstallmentOB.calculateInterest(repayData);
            Double interestAmtForMoratoriumPeriod = CommonUtil.convertObjToDouble(resultMap.get(InterestCalculationConstants.INTEREST));
            double totBaseAmt = CommonUtil.convertObjToDouble(getTxtLaonAmt()).doubleValue() + interestAmtForMoratoriumPeriod.doubleValue();
            setTxtTotalBaseAmt(String.valueOf(totBaseAmt));
            setMoratoriumInterestAll((LinkedHashMap)resultMap.get(ALL_RECORDS));
            resultMap = null;
            termLoanInstallmentOB = null;
        }catch(Exception e){
            log.info("Error In calculateTotalBaseAmount() "+e);
            parseException.logException(e,true);
        }
    }
    public void populateEMICalculatedFields(LinkedHashMap map, double totRepayVal){
        try{
            java.util.Set keySet =  map.keySet();
            Object[] objKeySet = (Object[]) keySet.toArray();
            if (keySet.size() > 0){
                setTxtAmtLastInstall(CommonUtil.convertObjToStr(((HashMap)map.get(objKeySet[keySet.size() - 1])).get("TOTAL")));
                if (keySet.size() == 1){
                    setTxtAmtPenulInstall(CommonUtil.convertObjToStr(((HashMap)map.get(objKeySet[keySet.size() - 1])).get("TOTAL")));
                }else{
                    setTxtAmtPenulInstall(CommonUtil.convertObjToStr(((HashMap)map.get(objKeySet[keySet.size() - 2])).get("TOTAL")));
                }
                setTdtLastInstall(CommonUtil.convertObjToStr(((HashMap)map.get(objKeySet[keySet.size() - 1])).get("INSTALLMENT_DATE")));
            }
            setTxtTotalInstallAmt(String.valueOf(totRepayVal));
            System.out.println("maponly"+map);
            setInstallmentAll(map);
            ttNotifyObservers();
        }catch(Exception e){
            e.printStackTrace();
            log.info("Error In populateEMICalculatedFields() "+e);
            parseException.logException(e,true);
        }
    }
    
    public HashMap getActiveAndInActiveScheduleNo(){
        HashMap resultMap = new HashMap();
        StringBuffer inActiveScheduleNo = new StringBuffer();
        StringBuffer activeDisbursementNo = new StringBuffer();
        try{
            HashMap eachRecs;
            java.util.Set keySet =  repaymentAll.keySet();
            Object[] objKeySet = (Object[]) keySet.toArray();
            int noOfInActiveSchedules = 0;
            int inActiveCounter = 0;
            int noOfActiveSchedules = 0;
            int activeCounter = 0;
            for (int i = keySet.size() - 1, j = 0;i >= 0;--i,++j){
                eachRecs = (HashMap) repaymentAll.get(objKeySet[j]);
                if (eachRecs.get(ACTIVE_STATUS).equals(NO)){
                    ++noOfInActiveSchedules;
                }else if (eachRecs.get(ACTIVE_STATUS).equals(YES)){
                    ++noOfActiveSchedules;
                }
            }
            for (int i = keySet.size() - 1, j = 0;i >= 0;--i,++j){
                eachRecs = (HashMap) repaymentAll.get(objKeySet[j]);
                if (eachRecs.get(ACTIVE_STATUS).equals(NO)){
                    if (inActiveCounter == 0 || noOfInActiveSchedules == inActiveCounter){
                        inActiveScheduleNo.append("'" + objKeySet[j] + "'");
                    }else{
                        inActiveScheduleNo.append(",'" + objKeySet[j] + "'");
                    }
                    inActiveCounter++;
                }else if ((!eachRecs.get(DISBURSEMENT_ID).equals("")) && (eachRecs.get(ACTIVE_STATUS).equals(YES))){
                    if (activeCounter == 0 || noOfActiveSchedules == activeCounter){
                        activeDisbursementNo.append("'" + eachRecs.get(DISBURSEMENT_ID) + "'");
                    }else{
                        activeDisbursementNo.append(",'" + eachRecs.get(DISBURSEMENT_ID) + "'");
                    }
                    activeCounter++;
                }
            }
            resultMap.put("ACTIVE_NO", activeDisbursementNo);
            resultMap.put("INACTIVE_NO", inActiveScheduleNo);
        }catch(Exception e){
            log.info("Error In getActiveAndInActiveScheduleNo() "+e);
            parseException.logException(e,true);
        }
        return resultMap;
    }
    
    public void populateDisbursementDetails(HashMap retrieve){
        try{
            setTxtLaonAmt(CommonUtil.convertObjToStr(retrieve.get(DISBURSEMENT_AMT)));
            setStrDisbursementID(CommonUtil.convertObjToStr(retrieve.get(DISBURSEMENT_ID)));
            setTxtRepayScheduleMode(CommonUtil.convertObjToStr(retrieve.get(SCHEDULE_MODE)));
            setDisbursementDate(DateUtil.getStringDate((Date) retrieve.get(DISBURSEMENT_DT)));
        }catch(Exception e){
            log.info("Error In populateDisbursementDetails() "+e);
            parseException.logException(e,true);
        }
    }
    public void setStrACNumber(String strACNumber){
        this.strACNumber = strACNumber;
        setChanged();
    }
    
    public String getStrACNumber(){
        return this.strACNumber;
    }
    
    public void setBorrowerNo(String borrowerNo){
        this.borrowerNo = borrowerNo;
        setChanged();
    }
    
    public String getBorrowerNo(){
        return this.borrowerNo;
    }
    
    void setLblProdID_RS_Disp(String lblProdID_RS_Disp){
        this.lblProdID_RS_Disp = lblProdID_RS_Disp;
        setChanged();
    }
    String getLblProdID_RS_Disp(){
        return this.lblProdID_RS_Disp;
    }
    
    public void setAllowMultiRepay(boolean allowMultiRepay){
        this.allowMultiRepay = allowMultiRepay;
    }
    
    boolean getAllowMultiRepay(){
        return this.allowMultiRepay;
    }
    
    public void setLblAccHead_RS_2(String lblAccHead_RS_2){
        this.lblAccHead_RS_2 = lblAccHead_RS_2;
        setChanged();
    }
    
    public String getLblAccHead_RS_2(){
        return this.lblAccHead_RS_2;
    }
    
    public void setLblAccNo_RS_2(String lblAccNo_RS_2){
        this.lblAccNo_RS_2 = lblAccNo_RS_2;
        setChanged();
    }
    
    public String getLblAccNo_RS_2(){
        return this.lblAccNo_RS_2;
    }
    
    void setTxtScheduleNo(String txtScheduleNo){
        this.txtScheduleNo = txtScheduleNo;
        setChanged();
    }
    String getTxtScheduleNo(){
        return this.txtScheduleNo;
    }
    
    void setTxtLaonAmt(String txtLaonAmt){
        this.txtLaonAmt = txtLaonAmt;
        setChanged();
    }
    String getTxtLaonAmt(){
        return this.txtLaonAmt;
    }
    
    void setCbmRepayFreq_Repayment(ComboBoxModel cbmRepayFreq_Repayment){
        this.cbmRepayFreq_Repayment = cbmRepayFreq_Repayment;
        setChanged();
    }
    ComboBoxModel getCbmRepayFreq_Repayment(){
        return this.cbmRepayFreq_Repayment;
    }
    
    void setCboRepayFreq_Repayment(String cboRepayFreq_Repayment){
        this.cboRepayFreq_Repayment = cboRepayFreq_Repayment;
        setChanged();
    }
    String getCboRepayFreq_Repayment(){
        return this.cboRepayFreq_Repayment;
    }
    
    void setCbmRepayType(ComboBoxModel cbmRepayType){
        this.cbmRepayType = cbmRepayType;
        setChanged();
    }
    ComboBoxModel getCbmRepayType(){
        return this.cbmRepayType;
    }
    
    void setCboRepayType(String cboRepayType){
        this.cboRepayType = cboRepayType;
        setChanged();
    }
    String getCboRepayType(){
        return this.cboRepayType;
    }
    
    void setTxtNoMonthsMora(String txtNoMonthsMora){
        this.txtNoMonthsMora = txtNoMonthsMora;
        setChanged();
    }
    String getTxtNoMonthsMora(){
        return this.txtNoMonthsMora;
    }
    
    void setTdtFirstInstall(String tdtFirstInstall){
        this.tdtFirstInstall = tdtFirstInstall;
        setChanged();
    }
    String getTdtFirstInstall(){
        return this.tdtFirstInstall;
    }
    
    void setTdtLastInstall(String tdtLastInstall){
        this.tdtLastInstall = tdtLastInstall;
        setChanged();
    }
    String getTdtLastInstall(){
        return this.tdtLastInstall;
    }
    
    void setTxtTotalBaseAmt(String txtTotalBaseAmt){
        this.txtTotalBaseAmt = txtTotalBaseAmt;
        setChanged();
    }
    String getTxtTotalBaseAmt(){
        return this.txtTotalBaseAmt;
    }
    
    void setTxtAmtPenulInstall(String txtAmtPenulInstall){
        this.txtAmtPenulInstall = txtAmtPenulInstall;
        setChanged();
    }
    String getTxtAmtPenulInstall(){
        return this.txtAmtPenulInstall;
    }
    
    void setTxtAmtLastInstall(String txtAmtLastInstall){
        this.txtAmtLastInstall = txtAmtLastInstall;
        setChanged();
    }
    String getTxtAmtLastInstall(){
        return this.txtAmtLastInstall;
    }
    
    void setTxtTotalInstallAmt(String txtTotalInstallAmt){
        this.txtTotalInstallAmt = txtTotalInstallAmt;
        setChanged();
    }
    String getTxtTotalInstallAmt(){
        return this.txtTotalInstallAmt;
    }
    
    void setRdoDoAddSIs_Yes(boolean rdoDoAddSIs_Yes){
        this.rdoDoAddSIs_Yes = rdoDoAddSIs_Yes;
        setChanged();
    }
    boolean getRdoDoAddSIs_Yes(){
        return this.rdoDoAddSIs_Yes;
    }
    
    void setRdoDoAddSIs_No(boolean rdoDoAddSIs_No){
        this.rdoDoAddSIs_No = rdoDoAddSIs_No;
        setChanged();
    }
    boolean getRdoDoAddSIs_No(){
        return this.rdoDoAddSIs_No;
    }
    
    void setRdoPostDatedCheque_Yes(boolean rdoPostDatedCheque_Yes){
        this.rdoPostDatedCheque_Yes = rdoPostDatedCheque_Yes;
        setChanged();
    }
    boolean getRdoPostDatedCheque_Yes(){
        return this.rdoPostDatedCheque_Yes;
    }
    
    void setRdoPostDatedCheque_No(boolean rdoPostDatedCheque_No){
        this.rdoPostDatedCheque_No = rdoPostDatedCheque_No;
        setChanged();
    }
    boolean getRdoPostDatedCheque_No(){
        return this.rdoPostDatedCheque_No;
    }
    
    void setTxtNoInstall(String txtNoInstall){
        this.txtNoInstall = txtNoInstall;
        setChanged();
    }
    String getTxtNoInstall(){
        return this.txtNoInstall;
    }
    
    void setTblRepaymentTab(EnhancedTableModel tblRepaymentTab){
        log.info("In tblRepaymentTab()...");
        
        this.tblRepaymentTab = tblRepaymentTab;
        setChanged();
    }
    
    EnhancedTableModel getTblRepaymentTab(){
        return this.tblRepaymentTab;
    }
    
    public void ttNotifyObservers(){
        notifyObservers();
    }
    public int getRepayIncrementType(){
        int incType = 0;
        try{
            Double incVal = CommonUtil.convertObjToDouble(getCbmRepayFreq_Repayment().getKeyForSelected());
            incType = incVal.intValue();
            incVal = null;
        }catch(Exception e){
            log.info("Exception caught in getRepayIncrementType: "+e);
            parseException.logException(e,true);
        }
        return incType;
    }
    
    public void setFirstInstallDate(){
        try{
            HashMap transactionMap = new HashMap();
            HashMap retrieve;
            transactionMap.put("acctNum", getLblAccNo_RS_2());
            List resultList        = ClientUtil.executeQuery("getFacilityExpDate", transactionMap);
            if (resultList.size() > 0){
                setTdtFirstInstall(DateUtil.getStringDate((java.util.Date) ((HashMap) resultList.get(0)).get(TO_DATE)));
                setTdtLastInstall(DateUtil.getStringDate((java.util.Date) ((HashMap) resultList.get(0)).get(FROM_DATE)));
            }
            transactionMap = null;
            resultList = null;
        }catch(Exception e){
            log.info("Exception caught in setFirstInstallDate: "+e);
            parseException.logException(e,true);
        }
    }
    
    public String checkLastRepaymentDate(int tableCount){
        String lastRepayDate = "";
        try{
            HashMap lastRecord = (HashMap) repaymentAll.get(((ArrayList) tblRepaymentTab.getDataArrayList().get(tableCount-1)).get(0));
            lastRepayDate = CommonUtil.convertObjToStr(lastRecord.get(TO_DATE));
        }catch(Exception e){
            log.info("Exception caught in checkLastRepaymentDate: "+e);
            parseException.logException(e,true);
        }
        return lastRepayDate;
    }
    
    public Date chkLastRepayDate(int tableCount){
        Date lastRepayDate = new Date();
        try{
            HashMap lastRecord = (HashMap) repaymentAll.get(((ArrayList) tblRepaymentTab.getDataArrayList().get(tableCount-1)).get(0));
            lastRepayDate = getProperDateFormat(lastRecord.get(TO_DATE));
        }catch(Exception e){
            log.info("Exception caught in chkLastRepayDate: "+e);
            parseException.logException(e,true);
        }
        return lastRepayDate;
    }
    
    public int addRepaymentDetails(int recordPosition, boolean update){
        int option = -1;
        try{
            log.info("Add Repayment Details...");
            repaymentEachTabRecord = new ArrayList();
            repaymentEachRecord = new HashMap();
            HashMap result = new HashMap();
            ArrayList data = tblRepaymentTab.getDataArrayList();
            tblRepaymentTab.setDataArrayList(data, repaymentTabTitle);
            final int dataSize = data.size();
            
            insertRepayment(dataSize+1);
            
            if (!update && recordPosition==-1) {
                // If the table is not in Edit Mode
//                if (repaymentEachRecord.get(ACTIVE_STATUS).equals(YES) && CommonUtil.convertObjToDouble(getStrLimitAmt()).doubleValue() < (getActiveLoanAmt() + CommonUtil.convertObjToDouble(getTxtLaonAmt()).doubleValue())){//commented by abi regarding outstanding amt is greater
//                    repayTabWarning("loanExceedLimitWarning");
//                    return 1;
//                }
                
                repaymentEachTabRecord.add(3, repaymentEachTabRecord.get(0));
                repaymentEachRecord.put(REF_SCHEDULE_NUMBER, repaymentEachRecord.get(SLNO));
                
                result = tableUtilRepayment.insertTableValues(repaymentEachTabRecord, repaymentEachRecord);
                
                repaymentTabValues = (ArrayList) result.get(TABLE_VALUES);
                repaymentAll = (LinkedHashMap) result.get(ALL_VALUES);
                option = CommonUtil.convertObjToInt(result.get(OPTION));
                
                tblRepaymentTab.setDataArrayList(repaymentTabValues, repaymentTabTitle);
                if (repaymentEachRecord.get(ACTIVE_STATUS).equals(YES)){
                    setActiveLoanAmt(activeLoanAmt+CommonUtil.convertObjToDouble(getTxtLaonAmt()).doubleValue());
                }
            }else{
                option = updateRepaymentTab(recordPosition);
            }
            setChanged();
            repaymentEachTabRecord = null;
            repaymentEachRecord = null;
            result = null;
            data = null;
        }catch(Exception e){
            log.info("Error in addRepaymentDetails..."+e);
            parseException.logException(e,true);
        }
        return option;
    }
    
    private void insertRepayment(int recordPosition){
        repaymentEachTabRecord.add(String.valueOf(recordPosition));
        repaymentEachTabRecord.add(getTxtLaonAmt());
        repaymentEachTabRecord.add(getTxtRepayScheduleMode());
        repaymentEachTabRecord.add(getStrRefScheduleNumber());
        
        repaymentEachRecord.put(SLNO, String.valueOf(recordPosition));
        //        repaymentEachRecord.put(PRODUCT_ID, getLblProdID_RS_Disp());
        if (getRdoDoAddSIs_Yes()){
            repaymentEachRecord.put(ADD_SIS, YES);
        }else if (getRdoDoAddSIs_No()){
            repaymentEachRecord.put(ADD_SIS, NO);
        }else{
            repaymentEachRecord.put(ADD_SIS, " ");
        }
        
        if (getRdoPostDatedCheque_Yes()){
            repaymentEachRecord.put(POST_DT_CHQ_ALLOW, YES);
        }else if(getRdoPostDatedCheque_No()){
            repaymentEachRecord.put(POST_DT_CHQ_ALLOW, NO);
        }else{
            repaymentEachRecord.put(POST_DT_CHQ_ALLOW, " ");
        }
        
        if (getRdoActive_Repayment()){
            repaymentEachRecord.put(ACTIVE_STATUS, YES);
        }else if (getRdoInActive_Repayment()){
            repaymentEachRecord.put(ACTIVE_STATUS, NO);
        }else{
            repaymentEachRecord.put(ACTIVE_STATUS, " ");
        }
        repaymentEachRecord.put(DISBURSEMENT_ID, getStrDisbursementID());
        repaymentEachRecord.put(SCHEDULE_MODE, getTxtRepayScheduleMode());
        repaymentEachRecord.put(LOAN_AMT, CommonUtil.convertObjToStr(getTxtLaonAmt()));
        if(isNewMode()==true)
            repaymentEachRecord.put(REPAY_TYPE, CommonUtil.convertObjToStr(getCboSanRepaymentType()));
        else
            repaymentEachRecord.put(REPAY_TYPE, CommonUtil.convertObjToStr(getCbmRepayType().getKeyForSelected()));
        repaymentEachRecord.put(REPAY_FREQ, CommonUtil.convertObjToStr(getCbmRepayFreq_Repayment().getKeyForSelected()));
        repaymentEachRecord.put(NO_INSTALLMENTS, CommonUtil.convertObjToStr(getTxtNoInstall()));
        repaymentEachRecord.put(FROM_DATE, getTdtRepayFromDate());
        repaymentEachRecord.put(INSTAL_STDT, getTdtFirstInstall());
        repaymentEachRecord.put(TO_DATE, getTdtLastInstall());
        repaymentEachRecord.put(REPAY_MOROTORIUM, getTxtNoMonthsMora());
        
        repaymentEachRecord.put(TOT_BASE_AMT, CommonUtil.convertObjToStr(getTxtTotalBaseAmt()));
        repaymentEachRecord.put(TOT_INSTALL_AMT, CommonUtil.convertObjToStr(getTxtTotalInstallAmt()));
        repaymentEachRecord.put(LAST_INSTALL_AMT, CommonUtil.convertObjToStr(getTxtAmtLastInstall()));
        repaymentEachRecord.put(PENULTI_AMT_INSTALL, CommonUtil.convertObjToStr(getTxtAmtPenulInstall()));
        repaymentEachRecord.put(INSTALLMENT_DETAILS, getInstallmentAll());
        System.out.println("FORCHECK"+getInstallmentAll());
        repaymentEachRecord.put(MORATORIUM_INTEREST, getMoratoriumInterestAll());
        repaymentEachRecord.put(DISBURSEMENT_DT, getTdtDisbursement_Dt());
        repaymentEachRecord.put(REF_SCHEDULE_NUMBER, getStrRefScheduleNumber());
        
        repaymentEachRecord.put(COMMAND, "");
    }
    
    private int updateRepaymentTab(int recordPosition){
        int option = -1;
        HashMap result = new HashMap();
        try{
            String strRecordKey = CommonUtil.convertObjToStr(((ArrayList) (tblRepaymentTab.getDataArrayList().get(recordPosition))).get(0));
            double loanBeforeUpdate = CommonUtil.convertObjToDouble(((HashMap)repaymentAll.get(strRecordKey)).get(LOAN_AMT)).doubleValue();
            String activeStatusBeforeUpdate = CommonUtil.convertObjToStr(((HashMap)repaymentAll.get(strRecordKey)).get(ACTIVE_STATUS));
            double currLoanAmt = CommonUtil.convertObjToDouble(getTxtLaonAmt()).doubleValue();
            String currActiveStatus = CommonUtil.convertObjToStr(repaymentEachRecord.get(ACTIVE_STATUS));
            double dummyLoanAmt = getActiveLoanAmt();
            
            if (activeStatusBeforeUpdate == YES){
                dummyLoanAmt -= loanBeforeUpdate;
            }
            if (currActiveStatus == YES){
                dummyLoanAmt += currLoanAmt;
            }
//            if (CommonUtil.convertObjToDouble(getStrLimitAmt()).doubleValue() < dummyLoanAmt){
//                repayTabWarning("loanExceedLimitWarning");commented regarding outstanding repayment
//                return 1;
//            }
            
            result = tableUtilRepayment.updateTableValues(repaymentEachTabRecord, repaymentEachRecord, recordPosition);
            
            repaymentTabValues = (ArrayList) result.get(TABLE_VALUES);
            repaymentAll = (LinkedHashMap) result.get(ALL_VALUES);
            option = CommonUtil.convertObjToInt(result.get(OPTION));
            
            tblRepaymentTab.setDataArrayList(repaymentTabValues, repaymentTabTitle);
            setActiveLoanAmt(dummyLoanAmt);
            strRecordKey = null;
            activeStatusBeforeUpdate = null;
        }catch(Exception e){
            log.info("Error in updateRepaymentTab..."+e);
            parseException.logException(e,true);
        }
        result = null;
        return option;
    }
    
    public void populateRepaymentDetails(int recordPosition){
        try{
            HashMap eachRecs;
            java.util.Set keySet =  repaymentAll.keySet();
            Object[] objKeySet = (Object[]) keySet.toArray();
            String strRecordKey = CommonUtil.convertObjToStr(((ArrayList) (tblRepaymentTab.getDataArrayList().get(recordPosition))).get(0));
            // To populate the corresponding record from the repayment Details
            for (int i = keySet.size() - 1, j = 0;i >= 0;--i, ++j){
                if ((CommonUtil.convertObjToStr(((HashMap) repaymentAll.get(objKeySet[j])).get(SLNO))).equals(strRecordKey)){
                    // To populate the Corresponding record from CTable
                    eachRecs = (HashMap) repaymentAll.get(objKeySet[j]);
                    if (eachRecs.get(ADD_SIS).equals(YES)){
                        setRdoDoAddSIs_Yes(true);
                    }else if (eachRecs.get(ADD_SIS).equals(NO)){
                        setRdoDoAddSIs_No(true);
                    }
                    setTxtAmtLastInstall(CommonUtil.convertObjToStr(eachRecs.get(LAST_INSTALL_AMT)));
                    setTxtAmtPenulInstall(CommonUtil.convertObjToStr(eachRecs.get(PENULTI_AMT_INSTALL)));
                    setTdtRepayFromDate(CommonUtil.convertObjToStr(eachRecs.get(FROM_DATE)));
                    setTdtFirstInstall(CommonUtil.convertObjToStr(eachRecs.get(INSTAL_STDT)));
                    cbmRepayFreq_Repayment.setKeyForSelected(CommonUtil.convertObjToStr(eachRecs.get(REPAY_FREQ)));
                    setCboRepayFreq_Repayment(CommonUtil.convertObjToStr(getCbmRepayFreq_Repayment().getDataForKey(CommonUtil.convertObjToStr(eachRecs.get(REPAY_FREQ)))));
                    cbmRepayType.setKeyForSelected(CommonUtil.convertObjToStr(eachRecs.get(REPAY_TYPE)));
                    setCboRepayType(CommonUtil.convertObjToStr(getCbmRepayType().getDataForKey(CommonUtil.convertObjToStr(eachRecs.get(REPAY_TYPE)))));
                    System.out.println("jith1212"+getCboRepayType());
                    cbmSanRepaymentType.setKeyForSelected(CommonUtil.convertObjToStr(eachRecs.get(REPAY_TYPE)));
                    setCboSanRepaymentType(CommonUtil.convertObjToStr(getCbmSanRepaymentType().getDataForKey(CommonUtil.convertObjToStr(eachRecs.get(REPAY_TYPE)))));
                    setTdtLastInstall(CommonUtil.convertObjToStr(eachRecs.get(TO_DATE)));
                    setTxtLaonAmt(CommonUtil.convertObjToStr(eachRecs.get(LOAN_AMT)));
                    setTxtNoInstall(CommonUtil.convertObjToStr(eachRecs.get(NO_INSTALLMENTS)));
                    if (eachRecs.get(POST_DT_CHQ_ALLOW).equals(YES)){
                        setRdoPostDatedCheque_Yes(true);
                    }else if(eachRecs.get(POST_DT_CHQ_ALLOW).equals(NO)){
                        setRdoPostDatedCheque_No(true);
                    }
                    if (eachRecs.containsKey(ACTIVE_STATUS)){
                        if (eachRecs.get(ACTIVE_STATUS).equals(YES)){
                            setRdoActive_Repayment(true);
                        }else if (eachRecs.get(ACTIVE_STATUS).equals(NO)){
                            setRdoInActive_Repayment(true);
                        }
                    }
                    setTxtScheduleNo(CommonUtil.convertObjToStr(eachRecs.get(SLNO)));
                    setTxtTotalBaseAmt(CommonUtil.convertObjToStr(eachRecs.get(TOT_BASE_AMT)));
                    setTxtTotalInstallAmt(CommonUtil.convertObjToStr(eachRecs.get(TOT_INSTALL_AMT)));
                    setStrDisbursementID(CommonUtil.convertObjToStr(eachRecs.get(DISBURSEMENT_ID)));
                    setTxtNoMonthsMora(CommonUtil.convertObjToStr(eachRecs.get(REPAY_MOROTORIUM)));
                    setTxtRepayScheduleMode(CommonUtil.convertObjToStr(eachRecs.get(SCHEDULE_MODE)));
                    setTdtDisbursement_Dt(CommonUtil.convertObjToStr(eachRecs.get(DISBURSEMENT_DT)));
                    setStrRefScheduleNumber(CommonUtil.convertObjToStr(eachRecs.get(REF_SCHEDULE_NUMBER)));
                    setInstallmentAll((LinkedHashMap)eachRecs.get(INSTALLMENT_DETAILS));
                    break;
                }
                eachRecs = null;
            }
            keySet = null;
            objKeySet = null;
            setChanged();
            ttNotifyObservers();
        }catch(Exception e){
            log.info("Error in populateRepaymentDetails..."+e);
            parseException.logException(e,true);
        }
    }
    public boolean getRepaymentisActive(){
         boolean returnValue=false;
        if(repaymentAll !=null){
           
            java.util.Set set=repaymentAll.keySet();
            Object obj[]=(Object[])set.toArray();
            for(int i=0;i<set.size();i++){
                HashMap eachmap =(HashMap)repaymentAll.get(obj[i]);
                if(eachmap.get(ACTIVE_STATUS).equals("N") )
                    returnValue=true;
            }
        }
        return returnValue;
    }
    public void deleteRepaymentTabRecord(int recordPosition){
        HashMap result = new HashMap();
        try{
            String strRecordKey = CommonUtil.convertObjToStr(((ArrayList) (tblRepaymentTab.getDataArrayList().get(recordPosition))).get(0));
            double loanBeforeUpdate = CommonUtil.convertObjToDouble(((HashMap)repaymentAll.get(strRecordKey)).get(LOAN_AMT)).doubleValue();
            String activeStatusBeforeUpdate = CommonUtil.convertObjToStr(((HashMap)repaymentAll.get(strRecordKey)).get(ACTIVE_STATUS));
            
            result = tableUtilRepayment.deleteTableValues(recordPosition);
            repaymentTabValues = (ArrayList) result.get(TABLE_VALUES);
            repaymentAll = (LinkedHashMap) result.get(ALL_VALUES);
            tblRepaymentTab.setDataArrayList(repaymentTabValues, repaymentTabTitle);
            if (activeStatusBeforeUpdate == YES){
                setActiveLoanAmt(activeLoanAmt - loanBeforeUpdate);
            }
            strRecordKey = null;
            activeStatusBeforeUpdate = null;
        }catch(Exception e){
            log.info("Error in deleteRepaymentTabRecord..."+e);
            parseException.logException(e,true);
        }
        result = null;
    }
    
    public boolean checkRepayLumpSumRecCount(int rowCount, boolean repayNewMode){
        if (rowCount > 1){
            repayTabWarning("existanceRepayLumpSumWarning");
            return true;
        }else if (rowCount == 1 && repayNewMode){
            repayTabWarning("existanceRepayLumpSumWarning");
            return true;
        }
        return false;
    }
    
    
    public void repayTabWarning(String warningMsg){
        // Give warning message if the the repayment type is Lump Sum with more than one Repayment Schedule
        int option = -1;
        String[] options = {objTermLoanRB.getString("cDialogOk")};
        option = COptionPane.showOptionDialog(null, objTermLoanRB.getString(warningMsg), CommonConstants.WARNINGTITLE,
        COptionPane.OK_OPTION, COptionPane.WARNING_MESSAGE, null, options, options[0]);
    }
    
    public String validateActiveSchedules(boolean isActive, String strScheduleMode, int selectedRow){
        String warnMsg = "";
        HashMap eachRecs;
        java.util.Set keySet =  repaymentAll.keySet();
        Object[] objKeySet = (Object[]) keySet.toArray();
        String strRecordKey = "";
        int activeScheduleCount = 0;
        if (selectedRow >= 0){
            strRecordKey = CommonUtil.convertObjToStr(((ArrayList) (tblRepaymentTab.getDataArrayList().get(selectedRow))).get(0));
        }
        // To populate the corresponding record from the repayment Details
        for (int i = keySet.size() - 1, j = 0;i >= 0;--i, ++j){
            eachRecs = (HashMap) repaymentAll.get(objKeySet[j]);
            /*
             * Only one repayment schedule will be in Active Status without Disburesement ID or
             * repayment mode should be REPHASE or MERGED
             * If these conditions are not satisfied throw an Exception
             *
             */
            if (eachRecs.get(ACTIVE_STATUS).equals(YES) &&
            ((CommonUtil.convertObjToStr(eachRecs.get(DISBURSEMENT_ID)).length() <= 0) ||
            (CommonUtil.convertObjToStr(eachRecs.get(SCHEDULE_MODE)).equals(CommonConstants.REPAY_SCHEDULE_MODE_REPHASE)) || (CommonUtil.convertObjToStr(eachRecs.get(SCHEDULE_MODE)).equals(CommonConstants.REPAY_SCHEDULE_MODE_MERGED)))){
                activeScheduleCount++;
                if (activeScheduleCount >= 1 && selectedRow < 0){
                    warnMsg = "activeScheduleWarning";
                    break;
                }
            }
            eachRecs = null;
        }
        
        eachRecs = null;
        strRecordKey = null;
        keySet = null;
        objKeySet = null;
        
        return warnMsg;
    }
    
    public void setRepaymentFrequency(String selectedValue){
        try{
            // Following lines commented by Rajesh
//            if (selectedValue.length() == 0){
//                setCboRepayType("");
//                cbmRepayType.setKeyForSelected("");
//            }else if (selectedValue.equals("Monthly")){
//                 if (!getCboRepayType().equals("Uniform Principle EMI")) {
//                    setCboRepayType("EMI");
//                    cbmRepayType.setKeyForSelected("EMI");
//                 }
//            }else if (selectedValue.equals("Quaterly")){
//                setCboRepayType("EQI");
//                cbmRepayType.setKeyForSelected("EQI");
//            }else if (selectedValue.equals("Half Yearly")){
//                setCboRepayType("EHI");
//                cbmRepayType.setKeyForSelected("EHI");
//            }else if (selectedValue.equals("Yearly")){
//                setCboRepayType("EYI");
//                cbmRepayType.setKeyForSelected("EYI");
//            }else if (selectedValue.equals("User Defined")){
//                setCboRepayType("User Defined");
//                cbmRepayType.setKeyForSelected("User Defined");
//            }else if (selectedValue.equals("Lump Sum")){
//                setCboRepayType("Lump Sum");
//                cbmRepayType.setKeyForSelected("Lump Sum");
//            }
            // Following lines added by Rajesh
            System.out.println("repayfrequency "+selectedValue);
            if (selectedValue.length() == 0){
                setCboRepayType("");
                cbmRepayType.setKeyForSelected("");
            }else if (selectedValue.equals("Monthly")){
                 if (!(getCboRepayType().equals("Uniform Principle EMI")||  CommonUtil.convertObjToStr(getCbmSanRepaymentType().getSelectedItem()).equals("Uniform Principle EMI"))) {
                    setCboRepayType("EMI");
                    cbmRepayType.setKeyForSelected("EMI");
            }else {
                setCboRepayType("Uniform Principle EMI");
                cbmRepayType.setKeyForSelected("UNIFORM_PRINCIPLE_EMI");
            }
            }else if (selectedValue.equals("Quaterly")){
                if(CommonUtil.convertObjToStr(getCbmSanRepaymentType().getSelectedItem()).equals("Uniform Principle EMI")){
                  setCboRepayType("Uniform Principle EMI");
                cbmRepayType.setKeyForSelected("UNIFORM_PRINCIPLE_EMI");
                }else{
                setCboRepayType("EQI");
                cbmRepayType.setKeyForSelected("EQI");
                }
            }else if (selectedValue.equals("Half Yearly")){
                System.out.println("chkhalf");
                if(CommonUtil.convertObjToStr(getCbmSanRepaymentType().getSelectedItem()).equals("Uniform Principle EMI")){
                  setCboRepayType("Uniform Principle EMI");
                    cbmRepayType.setKeyForSelected("UNIFORM_PRINCIPLE_EMI");
                }else{
                setCboRepayType("EHI");
                cbmRepayType.setKeyForSelected("EHI");
                }
            }else if (selectedValue.equals("Yearly")){
                if(CommonUtil.convertObjToStr(getCbmSanRepaymentType().getSelectedItem()).equals("Uniform Principle EMI")){
                  setCboRepayType("Uniform Principle EMI");
                cbmRepayType.setKeyForSelected("UNIFORM_PRINCIPLE_EMI");
                }else{
                setCboRepayType("EYI");
                cbmRepayType.setKeyForSelected("EYI");
                }
            }else if (selectedValue.equals("User Defined")){
                setCboRepayType("User Defined");
               // cbmRepayType.setKeyForSelected("User Defined");
                cbmRepayType.setKeyForSelected("USER_DEFINED");
            }else if (selectedValue.equals("Lump Sum")){
                setCboRepayType("Lump Sum");
                cbmRepayType.setKeyForSelected("Lump Sum");
            }else {
                System.out.println("chklast");
                setCboRepayType("Uniform Principle EMI");
                cbmRepayType.setKeyForSelected("UNIFORM_PRINCIPLE_EMI");
            }
            System.out.println("key of san repaytype"+cbmSanRepaymentType.getKeyForSelected().toString());
         //   if(TermLoanOB.getInstance().getActionType()==ClientConstants.ACTIONTYPE_EDIT){
            setCboSanRepaymentType(CommonUtil.convertObjToStr(cbmSanRepaymentType.getSelectedItem().toString()));
         //   }
            System.out.println("chk for repay type"+getCboSanRepaymentType());
            //cboSanRepaymentType.
            // Following lines added by Rajesh
            //commented by abi
//            if (selectedValue.length() == 0){
//                setCboRepayType("");
//                cbmRepayType.setKeyForSelected("");
//            }else {
//                setCboRepayType("Uniform Principle EMI");
//                cbmRepayType.setKeyForSelected("Uniform Principle EMI");
//            }
        }catch(Exception e){
            log.info("Error in setRepaymentFrequency: "+e);
            parseException.logException(e,true);
        }
    }
    
    // To create objects
    public void createObject(){
        repaymentTabValues = new ArrayList();
        tblRepaymentTab.setDataArrayList(null, repaymentTabTitle);
        repaymentAll = new LinkedHashMap();
        tableUtilRepayment = new TableUtil();
        tableUtilRepayment.setAttributeKey(SLNO);
    }
    
    // To destroy Objects
    public void destroyObjects(){
        repaymentTabValues = null;
        repaymentAll = null;
        tableUtilRepayment = null;
        uptoDtInterestMap  =null;
    }
    
    /**
     * Getter for property rdoActive_Repayment.
     * @return Value of property rdoActive_Repayment.
     */
    public boolean getRdoActive_Repayment() {
        return rdoActive_Repayment;
    }
    
    /**
     * Setter for property rdoActive_Repayment.
     * @param rdoActive_Repayment New value of property rdoActive_Repayment.
     */
    public void setRdoActive_Repayment(boolean rdoActive_Repayment) {
        this.rdoActive_Repayment = rdoActive_Repayment;
    }
    
    /**
     * Getter for property rdoInActive_Repayment.
     * @return Value of property rdoInActive_Repayment.
     */
    public boolean getRdoInActive_Repayment() {
        return rdoInActive_Repayment;
    }
    
    /**
     * Setter for property rdoInActive_Repayment.
     * @param rdoInActive_Repayment New value of property rdoInActive_Repayment.
     */
    public void setRdoInActive_Repayment(boolean rdoInActive_Repayment) {
        this.rdoInActive_Repayment = rdoInActive_Repayment;
    }
    
    /**
     * Getter for property activeLoanAmt.
     * @return Value of property activeLoanAmt.
     */
    public double getActiveLoanAmt() {
        return activeLoanAmt;
    }
    
    /**
     * Setter for property activeLoanAmt.
     * @param activeLoanAmt New value of property activeLoanAmt.
     */
    public void setActiveLoanAmt(double activeLoanAmt) {
        this.activeLoanAmt = activeLoanAmt;
    }
    
    /**
     * Getter for property strLimitAmt.
     * @return Value of property strLimitAmt.
     */
    public java.lang.String getStrLimitAmt() {
        return strLimitAmt;
    }
    
    /**
     * Setter for property strLimitAmt.
     * @param strLimitAmt New value of property strLimitAmt.
     */
    public void setStrLimitAmt(java.lang.String strLimitAmt) {
        this.strLimitAmt = strLimitAmt;
    }
    
    /**
     * Getter for property strDisbursementID.
     * @return Value of property strDisbursementID.
     */
    public java.lang.String getStrDisbursementID() {
        return strDisbursementID;
    }
    
    /**
     * Setter for property strDisbursementID.
     * @param strDisbursementID New value of property strDisbursementID.
     */
    public void setStrDisbursementID(java.lang.String strDisbursementID) {
        this.strDisbursementID = strDisbursementID;
    }
    
    /**
     * Getter for property installmentAll.
     * @return Value of property installmentAll.
     */
    public java.util.LinkedHashMap getInstallmentAll() {
        return installmentAll;
    }
    
    /**
     * Setter for property installmentAll.
     * @param installmentAll New value of property installmentAll.
     */
    public void setInstallmentAll(java.util.LinkedHashMap installmentAll) {
        this.installmentAll = installmentAll;
    }
    
    /**
     * Getter for property disbursementDate.
     * @return Value of property disbursementDate.
     */
    public java.lang.String getDisbursementDate() {
        return disbursementDate;
    }
    
    /**
     * Setter for property disbursementDate.
     * @param disbursementDate New value of property disbursementDate.
     */
    public void setDisbursementDate(java.lang.String disbursementDate) {
        this.disbursementDate = disbursementDate;
    }
    
    /**
     * Getter for property moratoriumInterestAll.
     * @return Value of property moratoriumInterestAll.
     */
    public java.util.LinkedHashMap getMoratoriumInterestAll() {
        return moratoriumInterestAll;
    }
    
    /**
     * Setter for property moratoriumInterestAll.
     * @param moratoriumInterestAll New value of property moratoriumInterestAll.
     */
    public void setMoratoriumInterestAll(java.util.LinkedHashMap moratoriumInterestAll) {
        this.moratoriumInterestAll = moratoriumInterestAll;
    }
    
    /**
     * Getter for property tdtDisbursement_Dt.
     * @return Value of property tdtDisbursement_Dt.
     */
    public java.lang.String getTdtDisbursement_Dt() {
        return tdtDisbursement_Dt;
    }
    
    /**
     * Setter for property tdtDisbursement_Dt.
     * @param tdtDisbursement_Dt New value of property tdtDisbursement_Dt.
     */
    public void setTdtDisbursement_Dt(java.lang.String tdtDisbursement_Dt) {
        this.tdtDisbursement_Dt = tdtDisbursement_Dt;
        setDisbursementDate(getTdtDisbursement_Dt());
    }
    
    /**
     * Getter for property txtRepayScheduleMode.
     * @return Value of property txtRepayScheduleMode.
     */
    public java.lang.String getTxtRepayScheduleMode() {
        return txtRepayScheduleMode;
    }
    
    /**
     * Setter for property txtRepayScheduleMode.
     * @param txtRepayScheduleMode New value of property txtRepayScheduleMode.
     */
    public void setTxtRepayScheduleMode(java.lang.String txtRepayScheduleMode) {
        this.txtRepayScheduleMode = txtRepayScheduleMode;
    }
    
    /**
     * Getter for property strRefScheduleNumber.
     * @return Value of property strRefScheduleNumber.
     */
    public java.lang.String getStrRefScheduleNumber() {
        return strRefScheduleNumber;
    }
    
    /**
     * Setter for property strRefScheduleNumber.
     * @param strRefScheduleNumber New value of property strRefScheduleNumber.
     */
    public void setStrRefScheduleNumber(java.lang.String strRefScheduleNumber) {
        this.strRefScheduleNumber = strRefScheduleNumber;
    }
    
    /**
     * Getter for property repaymentEachRecord.
     * @return Value of property repaymentEachRecord.
     */
    public java.util.HashMap getRepaymentEachRecord() {
        return repaymentEachRecord;
    }
    
    /**
     * Setter for property repaymentEachRecord.
     * @param repaymentEachRecord New value of property repaymentEachRecord.
     */
    public void setRepaymentEachRecord(java.util.HashMap repaymentEachRecord) {
        this.repaymentEachRecord = repaymentEachRecord;
    }
    
    /**
     * Getter for property repaymentAll.
     * @return Value of property repaymentAll.
     */
    public java.util.LinkedHashMap getRepaymentAll() {
        return repaymentAll;
    }
    
    /**
     * Setter for property repaymentAll.
     * @param repaymentAll New value of property repaymentAll.
     */
    public void setRepaymentAll(java.util.LinkedHashMap repaymentAll) {
        this.repaymentAll = repaymentAll;
    }
    
    /**
     * Getter for property tableUtilRepayment.
     * @return Value of property tableUtilRepayment.
     */
    public com.see.truetransact.clientutil.TableUtil getTableUtilRepayment() {
        return tableUtilRepayment;
    }
    
    /**
     * Setter for property tableUtilRepayment.
     * @param tableUtilRepayment New value of property tableUtilRepayment.
     */
    public void setTableUtilRepayment(com.see.truetransact.clientutil.TableUtil tableUtilRepayment) {
        this.tableUtilRepayment = tableUtilRepayment;
    }
    

   
    
    /**
     * Getter for property uptoDtInterestMap.
     * @return Value of property uptoDtInterestMap.
     */
    public java.util.HashMap getUptoDtInterestMap() {
        return uptoDtInterestMap;
    }
    
    /**
     * Setter for property uptoDtInterestMap.
     * @param uptoDtInterestMap New value of property uptoDtInterestMap.
     */
    public void setUptoDtInterestMap(java.util.HashMap uptoDtInterestMap) {
        this.uptoDtInterestMap = uptoDtInterestMap;
    }
    

    /**
     * Getter for property tdtRepayFromDate.
     * @return Value of property tdtRepayFromDate.
     */
    public java.lang.String getTdtRepayFromDate() {
        return tdtRepayFromDate;
    }
    
    /**
     * Setter for property tdtRepayFromDate.
     * @param tdtRepayFromDate New value of property tdtRepayFromDate.
     */
    public void setTdtRepayFromDate(java.lang.String tdtRepayFromDate) {
        this.tdtRepayFromDate = tdtRepayFromDate;
    }

    /**
     * Getter for property cbmSanRepaymentType.
     * @return Value of property cbmSanRepaymentType.
     */
    public com.see.truetransact.clientutil.ComboBoxModel getCbmSanRepaymentType() {
        return cbmSanRepaymentType;
    }
    
    /**
     * Setter for property cbmSanRepaymentType.
     * @param cbmSanRepaymentType New value of property cbmSanRepaymentType.
     */
    public void setCbmSanRepaymentType(com.see.truetransact.clientutil.ComboBoxModel cbmSanRepaymentType) {
        this.cbmSanRepaymentType = cbmSanRepaymentType;
    }
      public String getCboSanRepaymentType() {
        return cboSanRepaymentType;
    }

    public void setCboSanRepaymentType(String cboSanRepaymentType) {
        this.cboSanRepaymentType = cboSanRepaymentType;
         setChanged();
    }

    //    public void calculateEMI(){
    //        try{
    //            double interest_rate = getInterest_Rate();
    //            double real_int_rate = interest_rate / (12.0 * 100.0);
    //            double no_install = CommonUtil.convertObjToDouble(getTxtNoInstall()).doubleValue();
    //            double loan_amt = CommonUtil.convertObjToDouble(getTxtLaonAmt()).doubleValue();
    //            double pow_val = java.lang.Math.pow((1 + real_int_rate), no_install);
    //            emi = (loan_amt * real_int_rate * (pow_val / (pow_val - 1.0)));
    ////          emi =  p * r * (((1 + r) ^ n) / (((1 + r) ^ n) - 1));
    //            java.text.DecimalFormat objDecimalFormat = new java.text.DecimalFormat();
    //            objDecimalFormat.applyPattern("################.##");
    //            objDecimalFormat.setDecimalSeparatorAlwaysShown(false);
    //            String strEMI = objDecimalFormat.format(emi);
    //            String strTot_Amt = objDecimalFormat.format(emi * no_install);
    //            setTxtAmtPenulInstall(strEMI);
    //            setTxtAmtLastInstall(strEMI);
    //            setTxtTotalInstallAmt(strTot_Amt);
    //        }catch(Exception e){
    //            log.info("Error In calculateEMI() "+e);
    //            parseException.logException(e,true);
    //        }
    //    }
}
