/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition.
 * 
 *
 * TermLoanInstallmentOB.java
 *
 * Created on Tue Jan 25 16:03:11 IST 2005
 */

package com.see.truetransact.ui.termloan.repayment;

import com.see.truetransact.ui.termloan.emicalculator.*;
import com.see.truetransact.clientutil.TableSorter;
import com.see.truetransact.clientutil.EnhancedTableModel;
import com.see.truetransact.clientutil.TableUtil;
import com.see.truetransact.clientutil.ComboBoxModel;
import com.see.truetransact.clientutil.ClientUtil;
import com.see.truetransact.clientexception.ClientParseException;
import com.see.truetransact.clientproxy.ProxyFactory;
import com.see.truetransact.clientproxy.ProxyParameters;
import com.see.truetransact.clientutil.*;
import com.see.truetransact.commonutil.CommonConstants;
import com.see.truetransact.commonutil.DateUtil;
import com.see.truetransact.commonutil.CommonUtil;
import com.see.truetransact.commonutil.TTException;
import com.see.truetransact.ui.common.authorizedsignatory.AuthorizedSignatoryOB;
import com.see.truetransact.commonutil.interestcalc.InterestCalculationBean;
import com.see.truetransact.commonutil.interestcalc.LoanCalculateInterest;
import com.see.truetransact.commonutil.interestcalc.InterestCalculationConstants;
import com.see.truetransact.transferobject.termloan.TermLoanInstallMultIntTO;
import com.see.truetransact.transferobject.termloan.TermLoanInstallmentTO;
import com.see.truetransact.ui.common.powerofattorney.PowerOfAttorneyOB;
import com.see.truetransact.ui.TrueTransactMain;
import com.see.truetransact.uicomponent.CObservable;
import com.see.truetransact.uicomponent.COptionPane;
import com.see.truetransact.transferobject.termloan.TermLoanInterestTO;
import com.see.truetransact.transferobject.termloan.TermLoanRepaymentTO;
import com.see.truetransact.uicomponent.CTable;
import java.util.*;

import org.apache.log4j.Logger;

/**
 *
 * @author  152713
 */

public class RepaymentScheduleCreationOB extends CObservable{
    private static RepaymentScheduleCreationOB repaymentScheduleCreationOB;   
    private final        TermLoanInstallmentRB objTermLoanInstallmentRB = new TermLoanInstallmentRB();
    private final static Logger log = Logger.getLogger(RepaymentScheduleCreationOB.class);
    private final static ClientParseException parseException = ClientParseException.getInstance();
    private LoanCalculateInterest  interestCalculator;
    private ArrayList installmentTitle = new ArrayList();  //  Table Title of Installment
    private CTable tblInstallment;
    private HashMap operationMap= new HashMap();
    private ArrayList installmentTabRow;
    private ArrayList installmentAllTabRecords = new ArrayList();
    private HashMap installmentRecord;
    private LinkedHashMap installmentAll = new LinkedHashMap();
    private TableUtil tableUtilInstallment = new TableUtil();
    private ProxyFactory proxy=null;
    private double repayAmt = 0.0;
    private double totalRepayAmount = 0.0;
    private double existTotals=0;
    private boolean isLimitAmtTallyRepayAmt = false;
    private boolean hideSubmit=false;
    private String txtLoanAmt = "";
    private String tdtInstallmentDate = "";
    private String txtPrincipalAmt = "";
    private String txtInterest = "";
    private String txtTotal = "";
    private String txtBalance = "";
    private String lblAcctNo_Disp = "";
    private String txtInterestRate = "";
    private String strInstallDate = "";
    private String cboFrequency = "";
    private String txtNoOfInstall = "";
    private String cboroundOfType="";
    private String cboRepaymentType="";
    private String txtAcctNo = "";
    
    
    private ComboBoxModel cbmFrequency;
    private ComboBoxModel cbmroundOfType;
    private ComboBoxModel cbmRepaymentType;
    private HashMap lookUpHash;
    private HashMap keyValue;
    private ArrayList key;
    private ArrayList value;
    
    private final String ALL_VALUES = "ALL_VALUES";
    private final String BALANCE = "BALANCE";
    private final String COMMAND = "COMMAND";
    private final String COMPOUNDING_TYPE = "COMPOUNDING_TYPE";
    private final String COMPOUNDING_PERIOD = "COMPOUNDING_PERIOD";
    private final String DURATION_DD = "DURATION_DD";
    private final String DURATION_MM = "DURATION_MM";
    private final String DURATION_YY = "DURATION_YY";
    private final String FLOAT_PRECISION = "FLOAT_PRECISION";
    private final String FROM_DT = "FROM_DATE";
    private final String INTEREST = "INTEREST";
    private final String ISDURATION_DDMMYY = "ISDURATION_DDMMYY";
    private final String INTEREST_RATE = "INTEREST_RATE";
    private final String INSTALLMENT_DATE = "INSTALLMENT_DATE";
    private final String INTEREST_TYPE = "INTEREST_TYPE";
    private final String MONTH_OPTION = "MONTH_OPTION";
    private final String NO_INSTALL = "NO_INSTALL";
    private final String NO = "NO";
    private final String OPTION = "OPTION";
    private final String PRINCIPAL = "PRINCIPAL";
    private final String PRINCIPAL_AMOUNT = "PRINCIPAL_AMOUNT";
    private final String REPAYMENT_FREQ = "REPAYMENT_FREQUENCY";
    private final String REPAYMENT_TYPE = "REPAYMENT_TYPE";
    private final String ROUNDING_FACTOR = "ROUNDING_FACTOR";
    private final String ROUNDING_TYPE = "ROUNDING_TYPE";
    private final String SLNO = "SLNO";
    private final String TABLE_VALUES = "TABLE_VALUES";
    private final String TO_DT = "TO_DATE";
    private final String TOTAL = "TOTAL";
    private final String VARIOUS_INTEREST_RATE = "VARIOUS_INTEREST_RATE";
    private final String YEAR_OPTION = "YEAR_OPTION";
    private final String YES = "YES";
    private final String CATEGORY_ID = "CATEGORY_ID";
    private final String FROM_DATE = "FROM_DATE";
    private final String AMOUNT = "AMOUNT";
    private final String PROD_ID = "PROD_ID";
    private final String TO_DATE = "TO_DATE";
    private final String ACT = "ACT";
    private final String PROD = "PROD";
    private Date curr_dt = null;
    private ArrayList repaymentEachTabRecord;
    private HashMap repaymentEachRecord; 
    private TableUtil tableUtilRepayment ;
    private ArrayList repaymentTabValues = new ArrayList();
    private LinkedHashMap repaymentAll = new LinkedHashMap();
    private EnhancedTableModel tblRepaymentTab;
    private final   ArrayList repaymentTabTitle = new ArrayList();      //  Table Title of Repayment
    private final   java.util.ResourceBundle objTermLoanRB = java.util.ResourceBundle.getBundle("com.see.truetransact.ui.termloan.TermLoanRB", ProxyParameters.LANGUAGE);
    private final   String  REF_SCHEDULE_NUMBER = "REF_SCHEDULE_NUMBER";
    private final   String  ACTIVE_STATUS = "ACTIVE_STATUS";
    private double activeLoanAmt = 0.0;
    private String txtAmtLastInstall = "";
    private String txtAmtPenulInstall = "";
    private String tdtLastInstall = "";
    private String txtTotalInstallAmt = "";
    private boolean deleteInstallment = false;
    private String txtMoratorium = "";
    private String repayScheduleMode = null;
    private String strRefScheduleNumber = null;
    private String fromDate = "";
    private String toDate = "";
    private String firstInstallDate = "";
    private String totalBaseAmount = "";
    private String disbusmentDate = "";
    private String newScheduleNo = "";
    private String disbusmentId = "";
    private String activeStatus = "";
    private String sanctionDate ="" ;
    private String repayFromDate = "";
    private String result;
    private String outstandingAmnt = "";
    private String sanctionedAmount = "";
    private boolean salaryRecovery=false;

    public boolean isSalaryRecovery() {
        return salaryRecovery;
    }

    public void setSalaryRecovery(boolean salaryRecovery) {
        this.salaryRecovery = salaryRecovery;
    }
    
    public String getSanctionedAmount() {
        return sanctionedAmount;
    }

    public void setSanctionedAmount(String sanctionedAmount) {
        this.sanctionedAmount = sanctionedAmount;
    }
    
    public String getOutstandingAmnt() {
        return outstandingAmnt;
    }

    public void setOutstandingAmnt(String outstandingAmnt) {
        this.outstandingAmnt = outstandingAmnt;
    }
    

    public String getRepayFromDate() {
        return repayFromDate;
    }

    public void setRepayFromDate(String repayFromDate) {
        this.repayFromDate = repayFromDate;
    }
   
    public String getSanctionDate() {
        return sanctionDate;
    }

    public void setSanctionDate(String sanctionDate) {
        this.sanctionDate = sanctionDate;
    }

    public String getActiveStatus() {
        return activeStatus;
    }

    public void setActiveStatus(String activeStatus) {
        this.activeStatus = activeStatus;
    }   
        
    public String getDisbusmentId() {
        return disbusmentId;
    }

    public void setDisbusmentId(String disbusmentId) {
        this.disbusmentId = disbusmentId;
    }
    
    
    public String getNewScheduleNo() {
        return newScheduleNo;
    }

    public void setNewScheduleNo(String newScheduleNo) {
        this.newScheduleNo = newScheduleNo;
    }    
    
    public String getDisbusmentDate() {
        return disbusmentDate;
    }

    public void setDisbusmentDate(String disbusmentDate) {
        this.disbusmentDate = disbusmentDate;
    }

    public String getTotalBaseAmount() {
        return totalBaseAmount;
    }

    public void setTotalBaseAmount(String totalBaseAmount) {
        this.totalBaseAmount = totalBaseAmount;
    }
  
    public String getFirstInstallDate() {
        return firstInstallDate;
    }

    public String getFromDate() {
        return fromDate;
    }

    public String getToDate() {
        return toDate;
    }

    public void setFirstInstallDate(String firstInstallDate) {
        this.firstInstallDate = firstInstallDate;
    }

    public void setFromDate(String fromDate) {
        this.fromDate = fromDate;
    }

    public void setToDate(String toDate) {
        this.toDate = toDate;
    }

    public String getRepayScheduleMode() {
        return repayScheduleMode;
    }

    public String getStrRefScheduleNumber() {
        return strRefScheduleNumber;
    }

    public void setRepayScheduleMode(String repayScheduleMode) {
        this.repayScheduleMode = repayScheduleMode;
    }

    public void setStrRefScheduleNumber(String strRefScheduleNumber) {
        this.strRefScheduleNumber = strRefScheduleNumber;
    }
    

    public String getTxtMoratorium() {
        return txtMoratorium;
    }

    public void setTxtMoratorium(String txtMoratorium) {
        this.txtMoratorium = txtMoratorium;
    }

    public String getTdtLastInstall() {
        return tdtLastInstall;
    }

    public String getTxtAmtLastInstall() {
        return txtAmtLastInstall;
    }

    public String getTxtAmtPenulInstall() {
        return txtAmtPenulInstall;
    }

    public String getTxtTotalInstallAmt() {
        return txtTotalInstallAmt;
    }

    public void setTdtLastInstall(String tdtLastInstall) {
        this.tdtLastInstall = tdtLastInstall;
    }

    public void setTxtAmtLastInstall(String txtAmtLastInstall) {
        this.txtAmtLastInstall = txtAmtLastInstall;
    }

    public void setTxtAmtPenulInstall(String txtAmtPenulInstall) {
        this.txtAmtPenulInstall = txtAmtPenulInstall;
    }

    public void setTxtTotalInstallAmt(String txtTotalInstallAmt) {
        this.txtTotalInstallAmt = txtTotalInstallAmt;
    }
    
    
    public double getActiveLoanAmt() {
        return activeLoanAmt;
    }

    public void setActiveLoanAmt(double activeLoanAmt) {
        this.activeLoanAmt = activeLoanAmt;
    }
    
    Date curDate = null;
    
    static {
        try {
            repaymentScheduleCreationOB = new RepaymentScheduleCreationOB();
        } catch(Exception e) {
            log.info("try: " + e);
            parseException.logException(e,true);
            e.printStackTrace();
        }
    }

     public static RepaymentScheduleCreationOB getInstance() {
        return repaymentScheduleCreationOB;
    }
     
      private void setOperationMap() throws Exception{
        operationMap = new HashMap();
        operationMap.put(CommonConstants.JNDI, "TermLoanJNDI");
        operationMap.put(CommonConstants.HOME, "termloan.TermLoanHome");
        operationMap.put(CommonConstants.REMOTE, "termloan.TermLoan");
        fillDropdown();
    }
    
    private RepaymentScheduleCreationOB() throws Exception{
//        try{
//             fillDropdown();           
//            tblInstallment = new CTable();
//            tableUtilInstallment.setAttributeKey(SLNO);
//            setChanged();
//        }catch(Exception e){
//            log.info("Exception in RepaymentScheduleCreationOB: "+e);
//            parseException.logException(e,true);
//        }
        
        setOperationMap();        
        try {
            curDate = ClientUtil.getCurrentDate();
            proxy = ProxyFactory.createProxy();
            ttNotifyObservers();
        } catch (Exception e) {
            parseException.logException(e,true);
            e.printStackTrace();
            log.info("RepaymentScheduleCreationOB..."+e);
        }
        RepaymentScheduleCreationOB();
    }
    
    private void RepaymentScheduleCreationOB() throws Exception{
        setRepaymentTabTitle();        
        tblRepaymentTab = new EnhancedTableModel(null, repaymentTabTitle);
        notifyObservers();
    }
    
    public void fillDropdown() throws Exception{
        lookUpHash = new HashMap();
        lookUpHash.put(CommonConstants.MAP_NAME,null);
        
        ArrayList lookup_keys = new ArrayList();
        lookup_keys.add("LOAN.FREQUENCY");
        lookup_keys.add("TERM_LOAN.REPAYMENT_TYPE");
        lookup_keys.add("OPERATIVEACCTPRODUCT.ROUNDOFF");
        lookUpHash.put(CommonConstants.PARAMFORQUERY, lookup_keys);
        
        keyValue = ClientUtil.populateLookupData(lookUpHash);
        //System.out.println("keyValue : " +keyValue);
        getKeyValue((HashMap)keyValue.get("LOAN.FREQUENCY"));
        cbmFrequency = new ComboBoxModel(key,value);
        getKeyValue((HashMap)keyValue.get("TERM_LOAN.REPAYMENT_TYPE"));
        cbmRepaymentType = new ComboBoxModel(key,value);
        getKeyValue((HashMap)keyValue.get("OPERATIVEACCTPRODUCT.ROUNDOFF"));
        cbmroundOfType = new ComboBoxModel(key,value);
        // cbmRepayFreq = new ComboBoxModel(key,value);
        lookup_keys = null;
    }
    
    private void setBlankKeyValue(){
        key = new ArrayList();
        key.add("");
        value = new ArrayList();
        value.add("");
    }
    
    private void getKeyValue(HashMap keyValue)  throws Exception{
        //System.out.println("keyValue" + keyValue);
        key = (ArrayList)keyValue.get(CommonConstants.KEY);
        value = (ArrayList)keyValue.get(CommonConstants.VALUE);
    }
    private void setInstallmentTitle() throws Exception{
        try {
            installmentTitle = new ArrayList();
            installmentTitle.add(objTermLoanInstallmentRB.getString("tblColumnInstallNo"));
            installmentTitle.add(objTermLoanInstallmentRB.getString("tblColumnInstallDate"));
            installmentTitle.add(objTermLoanInstallmentRB.getString("tblColumnInstallPrincipal"));
            installmentTitle.add(objTermLoanInstallmentRB.getString("tblColumnInstallInterest"));
            installmentTitle.add(objTermLoanInstallmentRB.getString("tblColumnInstallTotal"));
            installmentTitle.add(objTermLoanInstallmentRB.getString("tblColumnInstallBalance"));
            installmentTitle.add(objTermLoanInstallmentRB.getString("tblColumnInstallActiveStatus"));
            //            installmentTitle.add("EMI Amount");
        }catch(Exception e) {
            log.info("Exception in setInstallmentTitle: "+e);
            parseException.logException(e,true);
        }
    }
    
    private void setInstallmentTitleForUniformType() throws Exception{
        try {
            installmentTitle = new ArrayList();
            installmentTitle.add(objTermLoanInstallmentRB.getString("tblColumnInstallNo"));
            installmentTitle.add(objTermLoanInstallmentRB.getString("tblColumnInstallDate"));
            installmentTitle.add(objTermLoanInstallmentRB.getString("tblColumnInstallPrincipal"));
            installmentTitle.add(objTermLoanInstallmentRB.getString("tblColumnInstallInterest"));
            installmentTitle.add(objTermLoanInstallmentRB.getString("tblColumnInstallTotal"));
            installmentTitle.add("EMI Amount");
            installmentTitle.add(objTermLoanInstallmentRB.getString("tblColumnInstallBalance"));
            installmentTitle.add(objTermLoanInstallmentRB.getString("tblColumnInstallActiveStatus"));
            //            tblInstallment = new EnhancedTableModel(null, installmentTitle);
        }catch(Exception e) {
            log.info("Exception in setInstallmentTitle: "+e);
            parseException.logException(e,true);
        }
    }
    
    public void resetInstallmentCTable(){
        tblInstallment = new CTable();
        tableUtilInstallment = new TableUtil();
        tableUtilInstallment.setAttributeKey(SLNO);
    }
    
    public void resetInstallmentDetails(){
        setTxtTotal("");
        setTxtBalance("");
        setTxtInterest("");
        setTxtPrincipalAmt("");
        setTdtInstallmentDate("");
    }
    
    public void resetOB(){
        setTxtAcctNo("");
        setCboRepaymentType("");
        setTxtInterest("");
        setCboFrequency((""));    
        setOutstandingAmnt("");
        setSanctionedAmount("");
    }
    // Setter method for txtLoanAmt
    void setTxtLoanAmt(String txtLoanAmt){
        this.txtLoanAmt = txtLoanAmt;
        setChanged();
    }
    // Getter method for txtLoanAmt
    String getTxtLoanAmt(){
        return this.txtLoanAmt;
    }
    
    // Setter method for tdtInstallmentDate
    void setTdtInstallmentDate(String tdtInstallmentDate){
        this.tdtInstallmentDate = tdtInstallmentDate;
        setChanged();
    }
    // Getter method for tdtInstallmentDate
    String getTdtInstallmentDate(){
        return this.tdtInstallmentDate;
    }
    
    // Setter method for txtPrincipalAmt
    void setTxtPrincipalAmt(String txtPrincipalAmt){
        this.txtPrincipalAmt = txtPrincipalAmt;
        setChanged();
    }
    // Getter method for txtPrincipalAmt
    String getTxtPrincipalAmt(){
        return this.txtPrincipalAmt;
    }
    
    // Setter method for txtInterest
    void setTxtInterest(String txtInterest){
        this.txtInterest = txtInterest;
        setChanged();
    }
    // Getter method for txtInterest
    String getTxtInterest(){
        return this.txtInterest;
    }
    
    // Setter method for txtTotal
    void setTxtTotal(String txtTotal){
        this.txtTotal = txtTotal;
        setChanged();
    }
    // Getter method for txtTotal
    String getTxtTotal(){
        return this.txtTotal;
    }
    
    // Setter method for txtBalance
    void setTxtBalance(String txtBalance){
        this.txtBalance = txtBalance;
        setChanged();
    }
    // Getter method for txtBalance
    String getTxtBalance(){
        return this.txtBalance;
    }
    
    /**
     * Getter for property txtInterestRate.
     * @return Value of property txtInterestRate.
     */
    public java.lang.String getTxtInterestRate() {
        return txtInterestRate;
    }
    
    /**
     * Setter for property txtInterestRate.
     * @param txtInterestRate New value of property txtInterestRate.
     */
    public void setTxtInterestRate(java.lang.String txtInterestRate) {
        this.setChanged();
        this.txtInterestRate = txtInterestRate;
    }
    
    /**
     * Getter for property lblAcctNo_Disp.
     * @return Value of property lblAcctNo_Disp.
     */
    public java.lang.String getLblAcctNo_Disp() {
        return lblAcctNo_Disp;
    }
    
    /**
     * Setter for property lblAcctNo_Disp.
     * @param lblAcctNo_Disp New value of property lblAcctNo_Disp.
     */
    public void setLblAcctNo_Disp(java.lang.String lblAcctNo_Disp) {
        this.setChanged();
        this.lblAcctNo_Disp = lblAcctNo_Disp;
    }
    
    public void ttNotifyObservers(){
        this.notifyObservers();
    }
    
    /**
     * Getter for property tblInstallment.
     * @return Value of property tblInstallment.
     */
    public com.see.truetransact.uicomponent.CTable getTblInstallment() {
        return tblInstallment;
    }
    
    /**
     * Setter for property tblInstallment.
     * @param tblInstallment New value of property tblInstallment.
     */
    public void setTblInstallment(com.see.truetransact.uicomponent.CTable tblInstallment) {
        this.setChanged();
        this.tblInstallment = tblInstallment;
    }
    
    /**
     * Getter for property cboFrequency.
     * @return Value of property cboFrequency.
     */
    public java.lang.String getCboFrequency() {
        return cboFrequency;
    }
    
    /**
     * Setter for property cboFrequency.
     * @param cboFrequency New value of property cboFrequency.
     */
    public void setCboFrequency(java.lang.String cboFrequency) {
        this.cboFrequency = cboFrequency;
    }
    
    /**
     * Getter for property cbmFrequency.
     * @return Value of property cbmFrequency.
     */
    public com.see.truetransact.clientutil.ComboBoxModel getCbmFrequency() {
        return cbmFrequency;
    }
    
    /**
     * Setter for property cbmFrequency.
     * @param cbmFrequency New value of property cbmFrequency.
     */
    public void setCbmFrequency(com.see.truetransact.clientutil.ComboBoxModel cbmFrequency) {
        this.cbmFrequency = cbmFrequency;
    }
    
    /**
     * Getter for property txtNoOfInstall.
     * @return Value of property txtNoOfInstall.
     */
    public java.lang.String getTxtNoOfInstall() {
        return txtNoOfInstall;
    }
    
    /**
     * Setter for property txtNoOfInstall.
     * @param txtNoOfInstall New value of property txtNoOfInstall.
     */
    public void setTxtNoOfInstall(java.lang.String txtNoOfInstall) {
        this.txtNoOfInstall = txtNoOfInstall;
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
    
    public void populateData(HashMap data, CTable tbl){
        try{
            this.tblInstallment = tbl;
            calculateInterest(data);
            ttNotifyObservers();
        }catch(Exception e){
            log.info("Exception in populateData: "+e);
            parseException.logException(e,true);
        }
    }
    
    public HashMap calculateInterest(HashMap data){
        HashMap resultMap = new HashMap();
        try{
            interestCalculator = new LoanCalculateInterest();
            InterestCalculationBean interestBean;
            interestBean = new InterestCalculationBean();
            interestBean.setDuration_yy(CommonUtil.convertObjToStr(data.get(DURATION_YY)));
            interestBean.setCompoundingPeriod(CommonUtil.convertObjToStr(data.get(COMPOUNDING_PERIOD)));
            interestBean.setPrincipalAmt(CommonUtil.convertObjToStr(data.get(PRINCIPAL_AMOUNT)));
            interestBean.setRateOfInterest(CommonUtil.convertObjToStr(data.get(INTEREST)));
            interestBean.setCompoundingType(CommonUtil.convertObjToStr(data.get(COMPOUNDING_TYPE)));
            interestBean.setInterestType(CommonUtil.convertObjToStr(data.get(INTEREST_TYPE)));
            if (data.containsKey(ISDURATION_DDMMYY)){
                if (data.get(ISDURATION_DDMMYY).equals(YES)){
                    interestBean.setIsDuration_ddmmyy(true);
                }else if (data.get(ISDURATION_DDMMYY).equals(NO)){
                    interestBean.setIsDuration_ddmmyy(false);
                }
            }else{
                if (data.containsKey(FROM_DT) && data.containsKey(TO_DT)){
                    interestBean.setIsDuration_ddmmyy(false);
                }
            }
            
            if (data.containsKey(FROM_DT)){
                interestBean.setDuration_FromDate(CommonUtil.convertObjToStr(data.get(FROM_DT)));
            }else{
                interestBean.setDuration_FromDate(DateUtil.getStringDate(ClientUtil.getCurrentDate()));
            }
            if (data.containsKey(TO_DT)){
                interestBean.setDuration_ToDate(CommonUtil.convertObjToStr(data.get(TO_DT)));
            }
            //commented by rishad
//            if (data.containsKey(REPAYMENT_TYPE)){
//                if (data.get(REPAYMENT_TYPE).equals("USER_DEFINED")){
//                    interestBean.setIsDuration_ddmmyy(false);
//                }
//            }
            if (data.containsKey(FLOAT_PRECISION)){
                interestBean.setFloatPrecision(CommonUtil.convertObjToStr(data.get(FLOAT_PRECISION)));
            }else{
                interestBean.setFloatPrecision("2");
            }
            if (data.containsKey(YEAR_OPTION)){
                interestBean.setYearOption(CommonUtil.convertObjToStr(data.get(YEAR_OPTION)));
            }else{
                interestBean.setYearOption(InterestCalculationConstants.YEAR_OPTION_ACTUAL);
            }
            if (data.containsKey(MONTH_OPTION)){
                interestBean.setMonthOption(CommonUtil.convertObjToStr(data.get(MONTH_OPTION)));
            }else{
                interestBean.setMonthOption(InterestCalculationConstants.MONTH_OPTION_ACTUAL);
            }
            if (data.containsKey(ROUNDING_TYPE)){
                interestBean.setRoundingType(CommonUtil.convertObjToStr(data.get(ROUNDING_TYPE)));
            }else{
                interestBean.setRoundingType(InterestCalculationConstants.ROUNDING_HIGHER);
            }
            if (data.containsKey(ROUNDING_FACTOR)){
                interestBean.setRoundingFactor(CommonUtil.convertObjToStr(data.get(ROUNDING_FACTOR)));
            }else{
                interestBean.setRoundingFactor(InterestCalculationConstants.ROUNDING_VALUE_5_PAISE);
            }
            HashMap map = new HashMap();
            HashMap repayMap = new HashMap();
            if (data.containsKey(FROM_DT)){
                repayMap.put(FROM_DT, data.get(FROM_DT));
            }else{
                repayMap.put(FROM_DT, ClientUtil.getCurrentDate());
            }
            if (data.containsKey(TO_DT)){
                repayMap.put(TO_DT, data.get(TO_DT));
            }
            if (data.containsKey(REPAYMENT_FREQ)){
                // To find the next installment date
                repayMap.put(REPAYMENT_FREQ, data.get(REPAYMENT_FREQ));
            }
            if (data.containsKey(NO_INSTALL)){
                // No. of Installments
                repayMap.put(NO_INSTALL, data.get(NO_INSTALL));
            }
            if (data.containsKey("INSTALLMENT_AMOUNT")) {
                repayMap.put("INSTALLMENT_AMOUNT", data.get("INSTALLMENT_AMOUNT"));
            }
            if (data.containsKey(VARIOUS_INTEREST_RATE)){
                repayMap.put(VARIOUS_INTEREST_RATE, data.get(VARIOUS_INTEREST_RATE));
            }
            if (data.containsKey(REPAYMENT_TYPE)){
                repayMap.put(REPAYMENT_TYPE, data.get(REPAYMENT_TYPE));
            }
            if (data.containsKey("PROD_ID")) {
                repayMap.put("PROD_ID", data.get("PROD_ID"));
            }
            if (data.containsKey("SALARY_RECOVERY") && data.get("SALARY_RECOVERY") != null) {
                repayMap.put("SALARY_RECOVERY", data.get("SALARY_RECOVERY"));
            } else {
                repayMap.put("SALARY_RECOVERY", "N");
            }
            //            if(data.containsKey("EMI_TYPE"))
            //            {
            //                repayMap.put("EMI_TYPE", data.get("EMI_TYPE"));
            //            } else {
            //                repayMap.put("EMI_TYPE", data.get(REPAYMENT_TYPE));
            //            }
            map.put(CommonConstants.DATA, interestBean);
            map.put("REPAYMENT_DETAILS", repayMap);
            //            System.out.println("#### repayMap : "+repayMap);
            showExistingInstallment(data);
            resultMap = interestCalculator.getInterest(map);
            if (resultMap.containsKey("TABLE_RECORDS")){
                if(!hideSubmit)
                installmentAllTabRecords = (ArrayList) resultMap.get("TABLE_RECORDS");
               // System.out.println("####installmentalltabrecord"+installmentAllTabRecords);
                //                if(data.containsKey("EMI_TYPE"))
                if(data.get(REPAYMENT_TYPE).equals("UNIFORM_PRINCIPLE_EMI")) {
                    setInstallmentTitleForUniformType();
                }
                else
                    setInstallmentTitle();
                //                tableUtilInstallment.setAttributeKey(SLNO);
                tableUtilInstallment.setTableValues(installmentAllTabRecords);
                System.out.println("### Heading Size : "+installmentTitle.size());
                
                TableModel tableModel = new TableModel();
                tableModel.setHeading(installmentTitle);
                if(installmentAllTabRecords!=null){
                    tableModel.setData(installmentAllTabRecords);
                    //System.out.println("### Data Size : " + installmentAllTabRecords.size());
                }
                tableModel.fireTableDataChanged();
                
                tblInstallment.setModel(tableModel);
                tblInstallment.revalidate();
                if(data.get(REPAYMENT_TYPE).equals("UNIFORM_PRINCIPLE_EMI"))
                    addTableColumn();
                
                
                //        tblInstallment.setDataArrayList(installmentAllTabRecords, installmentTitle);
                //               // setChanged();
            }
            if (resultMap.containsKey("ALL_RECORDS")){
                installmentAll = (LinkedHashMap) resultMap.get("ALL_RECORDS");
                //System.out.println("checkInstallmentall"+installmentAll);
                tableUtilInstallment.setAllValues(installmentAll);
            }
            if (resultMap.containsKey("TOTAL_REPAY_AMOUNT")){
                totalRepayAmount = CommonUtil.convertObjToDouble(resultMap.get("TOTAL_REPAY_AMOUNT")).doubleValue();
            }
            double interestAmount = CommonUtil.convertObjToDouble(resultMap.get(InterestCalculationConstants.INTEREST)).doubleValue();
            double prinAmount = CommonUtil.convertObjToDouble(resultMap.get(InterestCalculationConstants.AMOUNT)).doubleValue();
            if (data.containsKey(REPAYMENT_TYPE)){
                isLimitAmtTallyRepayAmt = true;
            }
            interestBean = null;
            map = null;
            repayMap = null;
        }catch(Exception e){
            log.info("Exception in calculateInterest: "+e);
            parseException.logException(e,true);
        }
        return resultMap;
    }
    
    private void showExistingInstallment(HashMap collectedData) {
        HashMap map = new HashMap();
        try {
            HashMap dataMap = new HashMap();
            map.put("ACCT_NUM", collectedData.get("ACT_NO"));
            existTotals = 0;
            if (collectedData.containsKey("SCHEDULE_ID")) {
                map.put("SCHEDULE_ID", collectedData.get("SCHEDULE_ID"));
            } else {
                return;
            }
            if (collectedData.get(REPAYMENT_TYPE).equals("UNIFORM_PRINCIPLE_EMI")) {
                dataMap.put(CommonConstants.MAP_NAME, "selectLoansInstallmentTL");
            } else {
                dataMap.put(CommonConstants.MAP_NAME, "selectLoansInstallmentEMITL");
            }
            dataMap.put(CommonConstants.MAP_WHERE, map);
            HashMap resultMap = ClientUtil.executeTableQuery(dataMap);
            if (resultMap != null && resultMap.size() > 0 && resultMap.get("TABLEHEAD") != null) {
                hideSubmit = true;
                installmentAllTabRecords = new ArrayList();
                installmentAllTabRecords = (ArrayList) resultMap.get("TABLEDATA");
                for (int i = 0; i < installmentAllTabRecords.size(); i++) {
                    ArrayList singleList = (ArrayList) installmentAllTabRecords.get(i);
                    existTotals += CommonUtil.convertObjToDouble(singleList.get(4)).doubleValue();
                }
            } else {
                hideSubmit = false;
            }
            //                observable.existInstallment(resultMap,uniformPrincipal);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    private void addTableColumn() {
        //        double t=0;
        //        int rowcount = installmentAllTabRecords.size()-1;
        //        String totals = ((ArrayList)installmentAllTabRecords.get(rowcount)).get(4).toString();
        //        System.out.println("#### totals : "+totals);
        //        double tot = Double.parseDouble(totals);
        //        t=LoanCalculateInterest.amountRouding(tot/(rowcount-1));
        //        double tableTot = t*(rowcount-1);
        //        int k;
        //        ArrayList tempList = new ArrayList();
        //        for(k=0;k<rowcount-2;k++) {
        //            tempList = (ArrayList)installmentAllTabRecords.get(k);
        //            System.out.println("#### tempList : "+tempList);
        //            tempList.set(5, new Double(t));
        //            installmentAllTabRecords.set(k, tempList);
        //            System.out.println("#### installmentAllTabRecords : "+installmentAllTabRecords.get(k));
        //        }
        //        System.out.println("##### tot = "+tot);
        //        System.out.println("##### tableTot = "+tableTot);
        //        double finalTot =0;
        //        if (tot>=tableTot) {
        //            finalTot = t+(tot-tableTot);
        //        }
        //        else
        //            finalTot = t-(tableTot-tot);
        //            tempList = (ArrayList)installmentAllTabRecords.get(k);
        //            tempList.set(5, new Double(finalTot));
        //            installmentAllTabRecords.set(k, tempList);
        double t=0;
        int rowcount=tblInstallment.getRowCount();
        //System.out.println("#####nrowcounttotal"+rowcount);
        
        String totals=CommonUtil.convertObjToStr(tblInstallment.getValueAt(rowcount-1, 4));
        double tot =0;
        if(existTotals>0){
            tot = existTotals;
            rowcount+=1;
        }
        else
             tot = Double.parseDouble(totals);
        t=LoanCalculateInterest.amountRouding(tot/(rowcount-1));
        double tableTot = t*(rowcount-1);
        int k;
        
        for(k=0;k<rowcount-2;k++) {
            //System.out.println("##### k = "+k);
            tblInstallment.setValueAt(String.valueOf(t), k, 5);
        }
        System.out.println("##### tot = "+tot);
        System.out.println("##### tableTot = "+tableTot);
        double finalTot =0;
        try {
            if (tot>=tableTot) {
                finalTot = t+(tot-tableTot);
            }
            else
                finalTot = t-(tableTot-tot);
            tblInstallment.setValueAt(String.valueOf(finalTot), k, 5);
        } catch (java.lang.IllegalArgumentException e){
            e.printStackTrace();
        }
    }
    public boolean checkLoanLimit(){
        return isLimitAmtTallyRepayAmt;
    }
    
    
    
    public void storeEmiAmount()
    {    HashMap mapData=new HashMap();
         HashMap hash=new HashMap();
         try {
             proxy = ProxyFactory.createProxy();
             operationMap.put(CommonConstants.JNDI, "TermLoanJNDI");
             operationMap.put(CommonConstants.HOME, "termloan.TermLoanHome");
             operationMap.put(CommonConstants.REMOTE, "termloan.TermLoan");
             
             if(installmentAllTabRecords.size()>0){
                 hash.put("ALL_EMI_RECORDS",installmentAllTabRecords);
                 System.out.println("this for list"+installmentAllTabRecords);
                 mapData =  (HashMap) proxy.executeQuery(hash, operationMap);
                 System.out.println("mapdata"+mapData);
             }
         } catch (Exception e) {
             parseException.logException(e,true);
             e.printStackTrace();
             log.info("TermLoanstoreemiamount..."+e);
         }
         
         
    }
    
    
    
    
    
    public void validateLoanLimitAmount(){
        if (repayAmt >= CommonUtil.convertObjToDouble(getTxtLoanAmt()).doubleValue()){
            isLimitAmtTallyRepayAmt = true;
            setTotalRepayAmount(repayAmt);
        }else{
            isLimitAmtTallyRepayAmt = false;
        }
    }
    
    public int addInstallmentDetails(int recordPosition, boolean update){
        int option = -1;
        try{
            installmentTabRow = new ArrayList();
            installmentRecord = new HashMap();
            HashMap result = new HashMap();
            ArrayList data = ((TableModel)tblInstallment.getModel()).getDataArrayList();
            ((TableModel)tblInstallment.getModel()).setData(data);
            ((TableModel)tblInstallment.getModel()).setHeading(installmentTitle);
            final int dataSize = data.size();
            insertInstallment(dataSize+1);
            if (!update) {
                // If the table is not in Edit Mode
                result = tableUtilInstallment.insertTableValues(installmentTabRow, installmentRecord);
                
                installmentAllTabRecords = (ArrayList) result.get(TABLE_VALUES);
                installmentAll = (LinkedHashMap) result.get(ALL_VALUES);
                option = CommonUtil.convertObjToInt(result.get(OPTION));
                
                //                tblInstallment.setDataArrayList(installmentAllTabRecords, installmentTitle);
                ((TableModel)tblInstallment.getModel()).setData(data);
                ((TableModel)tblInstallment.getModel()).setHeading(installmentTitle);
                repayAmt += CommonUtil.convertObjToDouble(txtTotal).doubleValue();
            }else{
                option = updateInstallmentTab(recordPosition);
            }
            
            setChanged();
            
            installmentTabRow = null;
            installmentRecord = null;
            result = null;
            data = null;
        }catch(Exception e){
            log.info("Error in addInstallmentDetails: "+e);
            parseException.logException(e,true);
        }
        return option;
    }
    
    private void insertInstallment(int recordPosition){
        installmentTabRow.add(String.valueOf(recordPosition));
        installmentTabRow.add(getTdtInstallmentDate());
        installmentTabRow.add(getTxtPrincipalAmt());
        installmentTabRow.add(getTxtInterest());
        installmentTabRow.add(getTxtTotal());
        installmentTabRow.add(getTxtBalance());
        
        installmentRecord.put(SLNO, String.valueOf(recordPosition));
        installmentRecord.put(INSTALLMENT_DATE, getTdtInstallmentDate());
        installmentRecord.put(PRINCIPAL, getTxtPrincipalAmt());
        installmentRecord.put(INTEREST_RATE, getTxtInterest());
        installmentRecord.put(TOTAL, getTxtTotal());
        installmentRecord.put(BALANCE, getTxtBalance());
        installmentRecord.put(COMMAND, "");
    }
    
    private int updateInstallmentTab(int recordPosition){
        int option = -1;
        HashMap result = new HashMap();
        try{
            //            double prevAmt = CommonUtil.convertObjToDouble(((ArrayList) (tblInstallment.getDataArrayList().get(recordPosition))).get(4)).doubleValue();
            //            result = tableUtilInstallment.updateTableValues(installmentTabRow, installmentRecord, recordPosition);
            //
            //            installmentAllTabRecords = (ArrayList) result.get(TABLE_VALUES);
            //            installmentAll = (LinkedHashMap) result.get(ALL_VALUES);
            //            option = CommonUtil.convertObjToInt(result.get(OPTION));
            //
            //            tblInstallment.setDataArrayList(installmentAllTabRecords, installmentTitle);
            //            repayAmt -= prevAmt;
            //            repayAmt += CommonUtil.convertObjToDouble(txtTotal).doubleValue();
        }catch(Exception e){
            log.info("Error in updateInstallmentTab: "+e);
            parseException.logException(e,true);
        }
        result = null;
        return option;
    }
    
    public void populateInstallmentDetails(int recordPosition){
        try{
            //            HashMap eachRecs;
            //            java.util.Set keySet =  installmentAll.keySet();
            //            Object[] objKeySet = (Object[]) keySet.toArray();
            //            String strRecordKey = (String) ((ArrayList) (tblInstallment.getDataArrayList().get(recordPosition))).get(0);
            //
            //            // To populate the corresponding record from the Installment Details
            //            for (int i = keySet.size() - 1, j = 0;i >= 0;--i, ++j){
            //                if (((String) ((HashMap) installmentAll.get(objKeySet[j])).get(SLNO)).equals(strRecordKey)){
            //                    // To populate the Corresponding record from CTable
            //                    eachRecs = (HashMap) installmentAll.get(objKeySet[j]);
            //
            //                    setTdtInstallmentDate(CommonUtil.convertObjToStr(eachRecs.get(INSTALLMENT_DATE)));
            //                    setTxtPrincipalAmt(CommonUtil.convertObjToStr(eachRecs.get(PRINCIPAL)));
            //                    setTxtInterest(CommonUtil.convertObjToStr(eachRecs.get(INTEREST_RATE)));
            //                    setTxtTotal(CommonUtil.convertObjToStr(eachRecs.get(TOTAL)));
            //                    setTxtBalance(CommonUtil.convertObjToStr(eachRecs.get(BALANCE)));
            //                    break;
            //                }
            //                eachRecs = null;
            //            }
            //            keySet = null;
            //
            //            objKeySet = null;
            //            setChanged();
            //            ttNotifyObservers();
        }catch(Exception e){
            log.info("Error in populateInstallmentDetails: "+e);
            parseException.logException(e,true);
        }
    }
    
    public void deleteInstallmentTabRecord(int recordPosition){
        HashMap result = new HashMap();
        try{
            //            result = tableUtilInstallment.deleteTableValues(recordPosition);
            //
            //            installmentAllTabRecords = (ArrayList) result.get(TABLE_VALUES);
            //            installmentAll = (LinkedHashMap) result.get(ALL_VALUES);
            //
            //            tblInstallment.setDataArrayList(installmentAllTabRecords, installmentTitle);
            //            repayAmt -= CommonUtil.convertObjToDouble(txtTotal).doubleValue();
        }catch(Exception e){
            log.info("Error in deleteInstallmentTabRecord: "+e);
            parseException.logException(e,true);
        }
        result = null;
    }
    
    // To create objects
    public void createObject(){
        installmentAllTabRecords = new ArrayList();
        //        tblInstallment.setDataArrayList(null, installmentTitle);
        TableModel tableModel = new TableModel();
        tableModel.setHeading(installmentTitle);
        tblInstallment.setModel(tableModel);
        installmentAll = new LinkedHashMap();
        tableUtilInstallment = new TableUtil();
        tableUtilInstallment.setAttributeKey(SLNO);
    }
    
    // To destroy Objects
    public void destroyObjects(){
        installmentAllTabRecords = null;
        installmentAll = null;
        tableUtilInstallment = null;
    }
    
    /**
     * Getter for property totalRepayAmount.
     * @return Value of property totalRepayAmount.
     */
    public double getTotalRepayAmount() {
        return totalRepayAmount;
    }
    
    /**
     * Setter for property totalRepayAmount.
     * @param totalRepayAmount New value of property totalRepayAmount.
     */
    public void setTotalRepayAmount(double totalRepayAmount) {
        this.totalRepayAmount = totalRepayAmount;
    }
    
    /**
     * Getter for property cboroundOfType.
     * @return Value of property cboroundOfType.
     */
    public java.lang.String getCboroundOfType() {
        return cboroundOfType;
    }
    
    /**
     * Setter for property cboroundOfType.
     * @param cboroundOfType New value of property cboroundOfType.
     */
    public void setCboroundOfType(java.lang.String cboroundOfType) {
        this.cboroundOfType = cboroundOfType;
    }
    
    /**
     * Getter for property cboRepaymentType.
     * @return Value of property cboRepaymentType.
     */
    public java.lang.String getCboRepaymentType() {
        return cboRepaymentType;
    }
    
    /**
     * Setter for property cboRepaymentType.
     * @param cboRepaymentType New value of property cboRepaymentType.
     */
    public void setCboRepaymentType(java.lang.String cboRepaymentType) {
        this.cboRepaymentType = cboRepaymentType;
    }
    
    /**
     * Getter for property cbmroundOfType.
     * @return Value of property cbmroundOfType.
     */
    public com.see.truetransact.clientutil.ComboBoxModel getCbmroundOfType() {
        return cbmroundOfType;
    }
    
    /**
     * Setter for property cbmroundOfType.
     * @param cbmroundOfType New value of property cbmroundOfType.
     */
    public void setCbmroundOfType(com.see.truetransact.clientutil.ComboBoxModel cbmroundOfType) {
        this.cbmroundOfType = cbmroundOfType;
    }
    
    /**
     * Getter for property cbmRepaymentType.
     * @return Value of property cbmRepaymentType.
     */
    public com.see.truetransact.clientutil.ComboBoxModel getCbmRepaymentType() {
        return cbmRepaymentType;
    }
    
    /**
     * Setter for property cbmRepaymentType.
     * @param cbmRepaymentType New value of property cbmRepaymentType.
     */
    public void setCbmRepaymentType(com.see.truetransact.clientutil.ComboBoxModel cbmRepaymentType) {
        this.cbmRepaymentType = cbmRepaymentType;
    }
    
    /**
     * Getter for property hideSubmit.
     * @return Value of property hideSubmit.
     */
    public boolean isHideSubmit() {
        return hideSubmit;
    }
    
    /**
     * Setter for property hideSubmit.
     * @param hideSubmit New value of property hideSubmit.
     */
    public void setHideSubmit(boolean hideSubmit) {
        this.hideSubmit = hideSubmit;
    }

    public String getTxtAcctNo() {
        return txtAcctNo;
    }

    public void setTxtAcctNo(String txtAcctNo) {
        this.txtAcctNo = txtAcctNo;
    }
   
    public HashMap getRepaymentDetailsForAcctno(HashMap acctMap){
        HashMap detailMap = new HashMap();
         List lst = ClientUtil.executeQuery("getRepaymentDetailsForAcctno", acctMap);
         if(lst != null && lst.size() > 0){
            detailMap = (HashMap)lst.get(0);
         }
         return detailMap;
    }
    
    public HashMap getLoansDetailsForScheduleCreation(HashMap acctMap){
        
        HashMap detailMap = new HashMap();
         List lst = ClientUtil.executeQuery("getLoansDetailsForScheduleCreation", acctMap);
         if(lst != null && lst.size() > 0){
            detailMap = (HashMap)lst.get(0);
         }
         return detailMap;
    }
    
    // getInterestDetailsWhereConditions
    public String getInterestDetailsConditions(HashMap acctMap){
        HashMap detailMap = new HashMap(); 
        String interestAmount = null;
        List list = ClientUtil.executeQuery("getInterestDetailsWhereConditions", acctMap); 
        HashMap whereConditionMap = new HashMap();
        HashMap finalMap = new HashMap();
        String strIntGetFrom = "";
        String interstType = "";
//        whereConditionMap.put(CATEGORY_ID, "");
//        whereConditionMap.put(AMOUNT, "");
//        whereConditionMap.put(PROD_ID, "");
//        whereConditionMap.put(FROM_DATE, null);
//        whereConditionMap.put(TO_DATE, null);
        if (list != null && list.size() > 0) {
            finalMap = (HashMap) list.get(0);
            whereConditionMap.put(CATEGORY_ID, CommonUtil.convertObjToStr(finalMap.get("CATEGORY")));
            whereConditionMap.put(AMOUNT, new java.math.BigDecimal(CommonUtil.convertObjToDouble(finalMap.get("LIMIT")).doubleValue()));
            whereConditionMap.put(PROD_ID, CommonUtil.convertObjToStr(finalMap.get("PROD_ID")));
            whereConditionMap.put(FROM_DATE, setProperDtFormat(DateUtil.getDateMMDDYYYY(CommonUtil.convertObjToStr(finalMap.get("FROM_DATE")))));
            whereConditionMap.put(TO_DATE, setProperDtFormat(DateUtil.getDateMMDDYYYY(CommonUtil.convertObjToStr(finalMap.get("TO_DATE")))));
            strIntGetFrom = CommonUtil.convertObjToStr(finalMap.get("INT_GET_FROM"));
            interstType = CommonUtil.convertObjToStr(finalMap.get("INTEREST_TYPE"));
        }
        list = null;
        List interstList = new ArrayList();
        if (strIntGetFrom.equals(PROD)) {
            if (interstType.equals("FLOATING_RATE")) {
                interstList = ClientUtil.executeQuery("getSelectProductTermLoanInterestFloatTO", whereConditionMap);
            } else {
                interstList = ClientUtil.executeQuery("getSelectProductTermLoanInterestTO", whereConditionMap); 
            }
        } else if (strIntGetFrom.equals(ACT)) {
            interstList = ClientUtil.executeQuery("getSelectTermLoanInterestTO", acctMap); 
            //interestAmount = list.get(0).;          
        }
        
        System.out.println("interest list ::" + interstList);
        TermLoanInterestTO  objTermLoanInterestTO  = new TermLoanInterestTO ();
        objTermLoanInterestTO = (TermLoanInterestTO)interstList.get(0);
        interestAmount = objTermLoanInterestTO.getInterest().toString();
        whereConditionMap = null;
        finalMap = null;
        strIntGetFrom = null;
//        if(list != null && list.size() > 0){
//            detailMap = (HashMap)list.get(0);
//         }
      
      return interestAmount; 
    }
    
    private Date setProperDtFormat(Date dt) {
       curr_dt = ClientUtil.getCurrentDate();
        Date tempDt = (Date) curr_dt.clone();
        if (dt != null) {
            tempDt.setDate(dt.getDate());
            tempDt.setMonth(dt.getMonth());
            tempDt.setYear(dt.getYear());
            return tempDt;
        }
        return null;
    }
    
    
    private int addRepaymentDetails(int recordPosition, boolean update){
        int option = -1;
        try{
            tableUtilRepayment = new TableUtil();
            tableUtilRepayment.setAttributeKey(SLNO);
            log.info("Add Repayment Details...");
            repaymentEachTabRecord = new ArrayList();
            repaymentEachRecord = new HashMap();
            HashMap result = new HashMap();            
            insertRepayment(recordPosition);              
            repaymentEachTabRecord.add(3, repaymentEachTabRecord.get(0));
            repaymentEachRecord.put(REF_SCHEDULE_NUMBER, repaymentEachRecord.get(SLNO));                
            result = tableUtilRepayment.insertTableValues(repaymentEachTabRecord, repaymentEachRecord);                
            repaymentTabValues = (ArrayList) result.get(TABLE_VALUES);
            System.out.println("repaymentTabValues :: " + repaymentTabValues);
            repaymentAll = (LinkedHashMap) result.get(ALL_VALUES);
            System.out.println("repaymentAll :: " + repaymentAll);
            option = CommonUtil.convertObjToInt(result.get(OPTION));                
            tblRepaymentTab.setDataArrayList(repaymentTabValues, repaymentTabTitle);
            if (repaymentEachRecord.get(ACTIVE_STATUS).equals(YES)){
             //setActiveLoanAmt(activeLoanAmt+CommonUtil.convertObjToDouble(getTxtLaonAmt()).doubleValue());
            }           
            setChanged();
            repaymentEachTabRecord = null;
            repaymentEachRecord = null;
            result = null;
            
        }catch(Exception e){
            log.info("Error in addRepaymentDetails..."+e);
            parseException.logException(e,true);
        }
        return option;
    }
    
    private int updateRepaymentTab(int recordPosition){
        int option = -1;
        HashMap result = new HashMap();
        try{
            String strRecordKey = CommonUtil.convertObjToStr(((ArrayList) (tblRepaymentTab.getDataArrayList().get(recordPosition))).get(0));
            double loanBeforeUpdate = CommonUtil.convertObjToDouble(((HashMap)repaymentAll.get(strRecordKey)).get("LOAN_AMT")).doubleValue();
            String activeStatusBeforeUpdate = CommonUtil.convertObjToStr(((HashMap)repaymentAll.get(strRecordKey)).get(ACTIVE_STATUS));
            double currLoanAmt = CommonUtil.convertObjToDouble(getTxtLoanAmt()).doubleValue();
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
    
    // Test data : 0001241105869
    
    public void repaymentFillData(java.util.LinkedHashMap data, double totRepayVal) {
        populateEMICalculatedFields(data, totRepayVal);
        deleteInstallment = false;
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
    
    public HashMap getMaxScheduleNumberForScheduleCreation(HashMap acctMap){
        HashMap detailMap = new HashMap();
         List lst = ClientUtil.executeQuery("getMaxScheduleNumberForScheduleCreation", acctMap);
         if(lst != null && lst.size() > 0){
            detailMap = (HashMap)lst.get(0);
         }
         return detailMap; 
    }
    
    public List getMaxScheduleNumberForScheduleCreationList(HashMap acctMap){        
         List lst = ClientUtil.executeQuery("getMaxScheduleNumberForScheduleCreation", acctMap);
         return lst; 
    }
    
    private HashMap getMaxAccountNumberForAcct(HashMap acctMap){
        HashMap detailMap = new HashMap();
        List lst = ClientUtil.executeQuery("getMaxAccountNumberForAcct", acctMap);
        if(lst != null && lst.size() > 0){
            detailMap = (HashMap)lst.get(0);
         }
        return detailMap;
    }
    
    public List getMaxInstallmentScheduleForCreationList(HashMap acctMap){       
         List lst = ClientUtil.executeQuery("getMaxInstallmentScheduleForCreation", acctMap);         
         return lst; 
    }
    public HashMap getMaxInstallmentScheduleForCreation(HashMap acctMap){
        HashMap detailMap = new HashMap();
         List lst = ClientUtil.executeQuery("getMaxInstallmentScheduleForCreation", acctMap);
         if(lst != null && lst.size() > 0){
            detailMap = (HashMap)lst.get(0);
         }
         return detailMap; 
    }
    
   
    private void insertRepayment(int recordPosition){
        repaymentEachTabRecord.add(String.valueOf(recordPosition));
        repaymentEachTabRecord.add(getTxtLoanAmt());
        repaymentEachTabRecord.add(getRepayScheduleMode());
        repaymentEachTabRecord.add(getStrRefScheduleNumber());        
        repaymentEachRecord.put(SLNO, String.valueOf(recordPosition)); 
        repaymentEachRecord.put(ACTIVE_STATUS, "Y");
        repaymentEachRecord.put("SCHEDULE_MODE",getRepayScheduleMode() );
        repaymentEachRecord.put("ADD_SIS", "N");
        repaymentEachRecord.put("POST_DT_CHQ_ALLOW", "N");
        repaymentEachRecord.put("LOAN_AMT", CommonUtil.convertObjToStr(getTxtLoanAmt()));       
        repaymentEachRecord.put("REPAY_TYPE", CommonUtil.convertObjToStr(getCbmRepaymentType().getKeyForSelected()));
        repaymentEachRecord.put("REPAY_FREQ", CommonUtil.convertObjToStr(getCbmFrequency().getKeyForSelected()));
        repaymentEachRecord.put("NO_INSTALLMENTS", CommonUtil.convertObjToStr(getTxtNoOfInstall()));
        repaymentEachRecord.put(FROM_DATE, getFromDate()); // from date - no change
        repaymentEachRecord.put("INSTAL_STDT", getFirstInstallDate()); // first install date FirstInstallDate - correct
        repaymentEachRecord.put(TO_DATE, getTdtLastInstall()); // last installdate  - setTdtLastInstall correct
        repaymentEachRecord.put("REPAY_MOROTORIUM", getTxtMoratorium());        
        repaymentEachRecord.put("TOT_BASE_AMT", CommonUtil.convertObjToStr(getTotalBaseAmount()));
        repaymentEachRecord.put("TOT_INSTALL_AMT", CommonUtil.convertObjToStr(getTxtTotalInstallAmt()));
        repaymentEachRecord.put("LAST_INSTALL_AMT", CommonUtil.convertObjToStr(getTxtAmtLastInstall()));
        repaymentEachRecord.put("PENULTI_AMT_INSTALL", CommonUtil.convertObjToStr(getTxtAmtPenulInstall()));
        repaymentEachRecord.put("INSTALLMENT_DETAILS", getInstallmentAll());       
        //repaymentEachRecord.put("MORATORIUM_INTEREST", getMoratoriumInterestAll());
        repaymentEachRecord.put("DISBURSEMENT_DT", getDisbusmentDate()); // To be set
        repaymentEachRecord.put(REF_SCHEDULE_NUMBER, getStrRefScheduleNumber());        
        repaymentEachRecord.put(COMMAND, "INSERT");
    }

    void doAction() {
        HashMap updateMap = new HashMap();
        HashMap proxyResultMap = new HashMap();
        HashMap existingSchedulesMap = new HashMap();
        HashMap existingInstallmentMap = new HashMap();
        try{
            HashMap acctNumMap = new HashMap();       
            acctNumMap.put("ACCT_NUM", getTxtAcctNo());
            
            updateMap.put("REPAY_SCHEDULE_CREATE","REPAY_SCHEDULE_CREATE");
            
            List existingScheduleList = getMaxScheduleNumberForScheduleCreationList(acctNumMap);
            //System.out.println("existingScheduleList :: "+ existingScheduleList); 
            updateMap.put("UPDATE_REPAY_SCHEDULE_TABLE",existingScheduleList);
            
//            for(int i=0; i< existingScheduleList.size(); i++){
//                HashMap newMap = (HashMap)existingScheduleList.get(i);
//                existingSchedulesMap.put("ACCT_NUM",newMap.get("ACCT_NUM"));
//                existingSchedulesMap.put("SCHEDULE_NO",newMap.get("SCHEDULE_NO"));                       
//            }
//            System.out.println("existingSchedulesMap :: "+ existingSchedulesMap);
            
            
            HashMap newScheduleMap = getMaxAccountNumberForAcct(acctNumMap);            
            int recordPosition = (CommonUtil.convertObjToInt(newScheduleMap.get("NEW_SCHEDULE")))+1;               
            //System.out.println("recordPosition :: " + recordPosition);
            
          
            //HashMap existingInstallmentMap = getMaxInstallmentScheduleForCreation(acctNumMap);
            List existingInstallmentList = getMaxInstallmentScheduleForCreationList(acctNumMap);            
            updateMap.put("UPDATE_LOANS_INSTALLMENT_TABLE",existingInstallmentList);            
            //System.out.println("existingInstallmentList :: "+ existingInstallmentList);
            
//            
//            for(int i=0; i< existingInstallmentList.size(); i++){
//                HashMap newMap = (HashMap)existingInstallmentList.get(i);
//                existingInstallmentMap.put("ACCT_NUM",newMap.get("ACCT_NUM"));
//                existingInstallmentMap.put("SCHEDULE_NO",newMap.get("SCHEDULE_ID"));                       
//            }
            
            
            int option = addRepaymentDetails(recordPosition, false);
            HashMap repayInstallMap = setTermLoanRepayment(true);
            HashMap objTermLoanRepaymentTOMap = (HashMap) repayInstallMap.get("REPAYMENT_DETAILS");
            HashMap objTermLoanInstallmentTOMap = (HashMap) repayInstallMap.get("INSTALLMENT_DETAILS");
            HashMap objTermLoanInstallMultIntMap = (HashMap) repayInstallMap.get("INSTALLMULTINT_DETAILS");
            updateMap.put("TermLoanRepaymentTO", objTermLoanRepaymentTOMap);
            updateMap.put("TermLoanInstallmentTO", objTermLoanInstallmentTOMap);
            updateMap.put("TermLoanInstallMultIntTO", objTermLoanInstallMultIntMap);            
            //System.out.println("objTermLoanRepaymentTOMap :: "+ objTermLoanRepaymentTOMap);
            //System.out.println("objTermLoanInstallmentTOMap :: "+ objTermLoanInstallmentTOMap);
            //System.out.println("objTermLoanInstallMultIntMap :: "+ objTermLoanInstallMultIntMap);
            proxyResultMap = proxy.execute(updateMap, operationMap);
            setResult(ClientConstants.RESULT_STATUS[1]);
            repaymentAll = null;
            repayInstallMap = null;
            objTermLoanRepaymentTOMap = null;
            objTermLoanInstallmentTOMap = null;
            objTermLoanInstallMultIntMap = null;
            //destroyObjects();
        }catch (Exception e) {
            setResult(ClientConstants.RESULT_STATUS[4]);
            parseException.logException(e, true);
            e.printStackTrace();
        }
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
   
   
   public HashMap setTermLoanRepayment(boolean isnew){
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
            for (int i = keySet.size() - 1,j = 0;i >= 0;--i,++j){
                oneRecord = (HashMap) repaymentAll.get(objKeySet[j]);
                termLoanRepaymentTO = new TermLoanRepaymentTO();
                termLoanRepaymentTO.setAcctNum(getTxtAcctNo());
                //                termLoanRepaymentTO.setBorrowNo(getBorrowerNo());
                termLoanRepaymentTO.setScheduleNo(CommonUtil.convertObjToDouble(oneRecord.get(REF_SCHEDULE_NUMBER)));
                termLoanRepaymentTO.setCommand(CommonUtil.convertObjToStr(oneRecord.get(COMMAND)));
//                if (oneRecord.get(ADD_SIS).equals(YES)){
//                    termLoanRepaymentTO.setAddSi(YES);
//                }else if (oneRecord.get(ADD_SIS).equals(NO)){
                   termLoanRepaymentTO.setAddSi("N");
//                }else{
//                    termLoanRepaymentTO.setAddSi(" ");
//                }
                termLoanRepaymentTO.setAmtLastInstall(CommonUtil.convertObjToDouble(oneRecord.get("LAST_INSTALL_AMT")));
                termLoanRepaymentTO.setAmtPenultimateInstall(CommonUtil.convertObjToDouble(oneRecord.get("PENULTI_AMT_INSTALL")));
                
//              termLoanRepaymentTO.setFirstInstallDt((Date)oneRecord.get(FROM_DATE));
                termLoanRepaymentTO.setFromDate(getProperDateFormat(oneRecord.get(FROM_DATE)));
                termLoanRepaymentTO.setFirstInstallDt(getProperDateFormat(oneRecord.get("INSTAL_STDT")));
//                if(isnew)
//                    termLoanRepaymentTO.setRepaymentType(CommonUtil.convertObjToDouble(getCbmRepaymentType().getKeyForSelected()));
//                 else
                    termLoanRepaymentTO.setRepaymentType(CommonUtil.convertObjToDouble(oneRecord.get("REPAY_FREQ")));
                
//              termLoanRepaymentTO.setLastInstallDt((Date)oneRecord.get(TO_DATE));
                termLoanRepaymentTO.setLastInstallDt(getProperDateFormat(oneRecord.get(TO_DATE)));
                termLoanRepaymentTO.setLoanAmount(CommonUtil.convertObjToDouble(oneRecord.get("LOAN_AMT")));
                termLoanRepaymentTO.setNoInstallments(CommonUtil.convertObjToDouble(oneRecord.get("NO_INSTALLMENTS")));
//                if (oneRecord.get("POST_DT_CHQ_ALLOW").equals(YES)){
//                    termLoanRepaymentTO.setPostDateChqallowed(YES);
//                }else if (oneRecord.get("POST_DT_CHQ_ALLOW").equals(NO)){
                   termLoanRepaymentTO.setPostDateChqallowed("N");
//                }else{
//                    termLoanRepaymentTO.setPostDateChqallowed(" ");
//                }
                if (oneRecord.get(ACTIVE_STATUS).equals("Y")){
                    termLoanRepaymentTO.setRepayActive("Y");
                }else if (oneRecord.get(ACTIVE_STATUS).equals("N")){
                    termLoanRepaymentTO.setRepayActive("N");
                }else{
                    termLoanRepaymentTO.setRepayActive(" ");
                }
                //                termLoanRepaymentTO.setProdId(CommonUtil.convertObjToStr(getLblProdID_RS_Disp()));
                termLoanRepaymentTO.setInstallType(CommonUtil.convertObjToStr(oneRecord.get("REPAY_TYPE")));
                termLoanRepaymentTO.setTotalBaseAmt(CommonUtil.convertObjToDouble(oneRecord.get("TOT_BASE_AMT")));
                termLoanRepaymentTO.setTotalInstallAmt(CommonUtil.convertObjToDouble(oneRecord.get("TOT_INSTALL_AMT")));
                
//                termLoanRepaymentTO.setDisbursementDt((Date)oneRecord.get(DISBURSEMENT_DT));
                termLoanRepaymentTO.setDisbursementDt(getProperDateFormat(oneRecord.get("DISBURSEMENT_DT")));
                // These fields are in server side and not in UI
                //            termLoanRepaymentTO.setRepayInterest(new);
                //            termLoanRepaymentTO.setIntType();
                //            termLoanRepaymentTO.setEmi();
                //            termLoanRepaymentTO.setRepaymentPr();
                //            termLoanRepaymentTO.setBalanceLoanAmt();
                termLoanRepaymentTO.setDisbursementId(CommonUtil.convertObjToDouble(oneRecord.get("DISBURSEMENT_ID")));
                termLoanRepaymentTO.setRepayMorotorium(CommonUtil.convertObjToDouble(oneRecord.get("REPAY_MOROTORIUM")));
                termLoanRepaymentTO.setScheduleMode(CommonUtil.convertObjToStr(oneRecord.get("SCHEDULE_MODE")));
                termLoanRepaymentTO.setRefScheduleNo(CommonUtil.convertObjToDouble(oneRecord.get(REF_SCHEDULE_NUMBER)));
                if (oneRecord.get(COMMAND).equals("INSERT")){
                    termLoanRepaymentTO.setStatus(CommonConstants.STATUS_CREATED);
                }else if (oneRecord.get(COMMAND).equals("UPDATE")){
                    termLoanRepaymentTO.setStatus(CommonConstants.STATUS_MODIFIED);
                }
                termLoanRepaymentTO.setStatusBy(TrueTransactMain.USER_ID);
                termLoanRepaymentTO.setStatusDt(curDate);
                
                repaymentMap.put(String.valueOf(j+1), termLoanRepaymentTO);
                if (oneRecord.get("INSTALLMENT_DETAILS") != null){  // && (oneRecord.get(COMMAND).equals(INSERT))  //Condition removed to insert in update mode also
                    interestRecords = ((LinkedHashMap)oneRecord.get("INSTALLMENT_DETAILS"));
                    interestKeySet =  interestRecords.keySet();
                    objInterestKeySet = (Object[]) interestKeySet.toArray();
                    int noOfInstallment = interestKeySet.size();
                    for (int m = noOfInstallment - 1;m >= 0;--m){
                        interestOneRecord = (HashMap) interestRecords.get(objInterestKeySet[m]);
                        termLoanInstallmentTO = new TermLoanInstallmentTO();
                        termLoanInstallmentTO.setAcctNum(getTxtAcctNo());
                        termLoanInstallmentTO.setBalanceAmt(CommonUtil.convertObjToDouble(interestOneRecord.get(BALANCE)));
                        termLoanInstallmentTO.setCommand(CommonUtil.convertObjToStr(interestOneRecord.get(COMMAND)));
                        
//                        termLoanInstallmentTO.setInstallmentDt((Date)interestOneRecord.get(INSTALLMENT_DATE));
                        termLoanInstallmentTO.setInstallmentDt(getProperDateFormat(interestOneRecord.get(INSTALLMENT_DATE)));
                        termLoanInstallmentTO.setInstallmentSlno(CommonUtil.convertObjToDouble(interestOneRecord.get(SLNO)));
                        termLoanInstallmentTO.setInterestAmt(CommonUtil.convertObjToDouble(interestOneRecord.get("INTEREST_AMOUNT")));
                        termLoanInstallmentTO.setInterestRate(CommonUtil.convertObjToDouble(interestOneRecord.get(INTEREST_RATE)));
                        termLoanInstallmentTO.setPrincipalAmt(CommonUtil.convertObjToDouble(interestOneRecord.get(PRINCIPAL)));
                        termLoanInstallmentTO.setScheduleId(CommonUtil.convertObjToDouble(oneRecord.get(REF_SCHEDULE_NUMBER)));
                        termLoanInstallmentTO.setTotalAmt(CommonUtil.convertObjToDouble(interestOneRecord.get(TOTAL)));
                         termLoanInstallmentTO.setActiveStatus(CommonUtil.convertObjToStr(interestOneRecord.get("ACTIVE_STATUS")));
                        termLoanInstallmentTO.setInstallmentPaid("N");
                        if (interestOneRecord.containsKey("MULTIPLE_INTEREST")){
                            installMultiIntList = (ArrayList) interestOneRecord.get("MULTIPLE_INTEREST");
                            HashMap multiIntRec;
                            for (int n = installMultiIntList.size()-1;n>=0;--n){
                                multiIntRec = (HashMap) installMultiIntList.get(n);
                                termLoanInstallMultIntTO = new TermLoanInstallMultIntTO();
                                termLoanInstallMultIntTO.setAcctNum(getTxtAcctNo());
                                if (interestOneRecord.get(COMMAND).equals("INSERT")){
                                    termLoanInstallMultIntTO.setCommand(CommonConstants.TOSTATUS_INSERT);
                                }else if (interestOneRecord.get(COMMAND).equals("UPDATE")){
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
                        if (interestOneRecord.get(COMMAND).equals("INSERT")){
                            termLoanInstallmentTO.setStatus(CommonConstants.STATUS_CREATED);
                            reschudleavailable=true;
                        }else if (interestOneRecord.get(COMMAND).equals("UPDATE")){
                            termLoanInstallmentTO.setStatus(CommonConstants.STATUS_MODIFIED);
                        }
                        installmentMap.put(String.valueOf(installmentMap.size()+1), termLoanInstallmentTO);
                        interestOneRecord = null;
                    }
                }
                if (oneRecord.get("MORATORIUM_INTEREST") != null && (oneRecord.get(COMMAND).equals("INSERT"))){
                    interestRecords = ((LinkedHashMap)oneRecord.get("MORATORIUM_INTEREST"));
                    interestKeySet =  interestRecords.keySet();
                    objInterestKeySet = (Object[]) interestKeySet.toArray();
                    HashMap moratoriumIntRec = (HashMap) interestRecords.get(objInterestKeySet[0]);
                    if (moratoriumIntRec.containsKey("MULTIPLE_INTEREST")){
                        installMultiIntList = (ArrayList) moratoriumIntRec.get("MULTIPLE_INTEREST");
                        HashMap multiIntRec;
                        for (int n = installMultiIntList.size()-1;n>=0;--n){
                            multiIntRec = (HashMap) installMultiIntList.get(n);
                            termLoanInstallMultIntTO = new TermLoanInstallMultIntTO();
                            termLoanInstallMultIntTO.setAcctNum(getTxtAcctNo());
                            if (oneRecord.get(COMMAND).equals("INSERT")){
                                termLoanInstallMultIntTO.setCommand(CommonConstants.TOSTATUS_INSERT);
                            }else if (oneRecord.get(COMMAND).equals("UPDATE")){
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
            keySet = null;
            objKeySet = null;
            interestKeySet = null;
            objInterestKeySet = null;
            termLoanInstallMultIntTO = null;
            installMultiIntList = null;
            repayInstallmentMap.put("REPAYMENT_DETAILS", repaymentMap);
            repayInstallmentMap.put("INSTALLMENT_DETAILS", installmentMap);
            repayInstallmentMap.put("INSTALLMULTINT_DETAILS", installMultIntMap); 
            installmentMap = null;
            repaymentMap = null;
            installMultIntMap = null;
        }catch(Exception e){
            log.info("Error In setTermLoanRepayment() "+e);
            parseException.logException(e,true);
        }
        return repayInstallmentMap; 
   } 
   
   public int getIncrementType(){
        int incType = 0;
        try{
            Double incVal = CommonUtil.convertObjToDouble(getCbmFrequency().getKeyForSelected());
            incType = incVal.intValue();
            incVal = null;
        }catch(Exception e){
            log.info("Exception caught in getIncrementType: "+e);
            parseException.logException(e,true);
        }
        return incType;
    }
   
   public int getInstallNo(String strNoInstalls, int incType){
        int instNo = 0;
        try{
            Double insNo = CommonUtil.convertObjToDouble(strNoInstalls);
            instNo = insNo.intValue();// - 1;
            if (incType == 7){
                instNo = instNo * 7;
            }else if (incType >= 90){
                instNo = ((int) (incType / 30)) * instNo;
            }
            insNo = null;
        }catch(Exception e){
            log.info("Exception caught in getInstallAmt: "+e);
            parseException.logException(e,true);
        }
        return instNo;
    }
   
   public void resetOBFields(){
      setCboFrequency("");
      setCboRepaymentType("");
      setCboroundOfType("");
      setTxtAcctNo("");
      setTxtInterest("");
      setTxtInterestRate("");
      setTxtMoratorium("");
      setTxtNoOfInstall("");
      setFirstInstallDate("");
      setFromDate("");
      setDisbusmentDate("");
      setDisbusmentId("");
      setRepayFromDate("");
      setRepayScheduleMode("");
      setToDate("");
      setFirstInstallDate("");
      setTxtLoanAmt("");
      setActiveStatus("");
      setTdtInstallmentDate("");
      setTdtLastInstall("");    
   }
   
   /**
     * Setter for property result.
     *
     * @param result New value of property result.
     */
    public void setResult(java.lang.String result) {
        this.result = result;
    }
    
     /**
     * Getter for property result.
     *
     * @return Value of property result.
     */
    public java.lang.String getResult() {
        return result;
    }
   
}