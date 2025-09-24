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

package com.see.truetransact.ui.termloan.emicalculator;

import com.see.truetransact.clientutil.TableSorter;
import com.see.truetransact.clientutil.EnhancedTableModel;
import com.see.truetransact.clientutil.TableUtil;
import com.see.truetransact.clientutil.ComboBoxModel;
import com.see.truetransact.clientutil.ClientUtil;
import com.see.truetransact.clientexception.ClientParseException;
import com.see.truetransact.clientproxy.ProxyFactory;
import com.see.truetransact.commonutil.CommonConstants;
import com.see.truetransact.commonutil.DateUtil;
import com.see.truetransact.commonutil.CommonUtil;
import com.see.truetransact.commonutil.TTException;
import com.see.truetransact.ui.common.authorizedsignatory.AuthorizedSignatoryOB;
import com.see.truetransact.commonutil.interestcalc.InterestCalculationBean;
import com.see.truetransact.commonutil.interestcalc.LoanCalculateInterest;
import com.see.truetransact.commonutil.interestcalc.InterestCalculationConstants;
import com.see.truetransact.ui.common.powerofattorney.PowerOfAttorneyOB;
import com.see.truetransact.ui.TrueTransactMain;
import com.see.truetransact.uicomponent.CObservable;
import com.see.truetransact.uicomponent.COptionPane;
import com.see.truetransact.clientutil.TableModel;
import com.see.truetransact.uicomponent.CTable;

import org.apache.log4j.Logger;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Set;
/**
 *
 * @author  152713
 */

public class TermLoanInstallmentOB extends CObservable{
    private       static TermLoanInstallmentOB termLoanInstallmentOB;
    private final        TermLoanInstallmentRB objTermLoanInstallmentRB = new TermLoanInstallmentRB();
    private final static Logger log = Logger.getLogger(TermLoanInstallmentOB.class);
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
    
    public TermLoanInstallmentOB(){
        termLoanInstallmentOB();
    }
    
    private void termLoanInstallmentOB(){
        try{
            fillDropdown();
            //            setInstallmentTitle();
            //            tblInstallment = new EnhancedTableModel(null, installmentTitle);
            tblInstallment = new CTable();
            tableUtilInstallment.setAttributeKey(SLNO);
            setChanged();
        }catch(Exception e){
            log.info("Exception in TermLoanInstallmentOB: "+e);
            parseException.logException(e,true);
        }
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
        getKeyValue((HashMap)keyValue.get("LOAN.FREQUENCY"));
        setCbmFrequency(new ComboBoxModel(key,value));
        getKeyValue((HashMap)keyValue.get("TERM_LOAN.REPAYMENT_TYPE"));
        setCbmRepaymentType(new ComboBoxModel(key,value));
        getKeyValue((HashMap)keyValue.get("OPERATIVEACCTPRODUCT.ROUNDOFF"));
        setCbmroundOfType(new ComboBoxModel(key,value));
        lookup_keys = null;
    }
    
    private void setBlankKeyValue(){
        key = new ArrayList();
        key.add("");
        value = new ArrayList();
        value.add("");
    }
    
    private void getKeyValue(HashMap keyValue)  throws Exception{
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
            if(data.containsKey("SUBSIDY_EXISTS") && data.get("SUBSIDY_EXISTS") != null && data.get("SUBSIDY_EXISTS").equals("Y")){
             double principal = CommonUtil.convertObjToDouble(data.get(PRINCIPAL_AMOUNT)) - CommonUtil.convertObjToDouble(data.get("SUBSIDY_AMOUNT"));
             interestBean.setPrincipalAmt(String.valueOf(principal));
            }
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
            if(data.containsKey("INSTALLMENT_AMOUNT"))
            {
            repayMap.put("INSTALLMENT_AMOUNT",data.get("INSTALLMENT_AMOUNT"));
            }
            if (data.containsKey("SALARY_RECOVERY") && data.get("SALARY_RECOVERY") != null) {
                repayMap.put("SALARY_RECOVERY", data.get("SALARY_RECOVERY"));
            } else {
                repayMap.put("SALARY_RECOVERY", "N");
            }
            if (data.containsKey(VARIOUS_INTEREST_RATE)){
                repayMap.put(VARIOUS_INTEREST_RATE, data.get(VARIOUS_INTEREST_RATE));
            }
            if (data.containsKey(REPAYMENT_TYPE)){
                repayMap.put(REPAYMENT_TYPE, data.get(REPAYMENT_TYPE));
            }
            if(data.containsKey("PROD_ID")){
                repayMap.put("PROD_ID", data.get("PROD_ID"));
            }
            //            if(data.containsKey("EMI_TYPE"))
            //            {
            //                repayMap.put("EMI_TYPE", data.get("EMI_TYPE"));
            //            } else {
            //                repayMap.put("EMI_TYPE", data.get(REPAYMENT_TYPE));
            //            }
            map.put(CommonConstants.DATA, interestBean);
            if(data.containsKey("INTEREST")){
                repayMap.put("LOAN_INTEREST_RATE", data.get("INTEREST"));
            }
            if(data.containsKey("EMIF_DATE")){
                repayMap.put("EMIF_DATE", data.get("EMIF_DATE"));
            }
            //Added by sreekrishnan to find EMI calculation type
            if (data.containsKey(REPAYMENT_TYPE)){
                if(data.get("REPAYMENT_TYPE")!=null && !data.get("REPAYMENT_TYPE").equals("") && data.get("REPAYMENT_TYPE").equals("EMI")){                    
                    List emiList = ClientUtil.executeQuery("getEMIcalculationType", data);                    
                        if (emiList != null && emiList.size() > 0) {
                            HashMap emiMap = new HashMap();
                            emiMap = (HashMap) emiList.get(0);
                            if(emiMap.get("EMI_FLAT_RATE").equals("Y")){
                                repayMap.put("EMI_FLAT_RATE",emiMap.get("EMI_FLAT_RATE"));
                            }
                        }
                    }
            }
            map.put("REPAYMENT_DETAILS", repayMap);   
            if(data.containsKey("USER_DEFINED_GOLD_LOAN_REPAY_FREQUENCY")){//Added by nithya on 06-07-2019 for KD 546 - New Gold Loan -45 days maturity type
                repayMap.put("USER_DEFINED_GOLD_LOAN_REPAY_FREQUENCY","USER_DEFINED_GOLD_LOAN_REPAY_FREQUENCY");
            }
            
             if(data.containsKey("SUBSIDY_EXISTS") && data.get("SUBSIDY_EXISTS") != null && data.get("SUBSIDY_EXISTS").equals("Y")){
               repayMap.put("SUBSIDY_EXISTS","Y");
               repayMap.put("SUBSIDY_AMOUNT",data.get("SUBSIDY_AMOUNT"));
             }else{
               repayMap.put("SUBSIDY_EXISTS","N");
               repayMap.put("SUBSIDY_AMOUNT",0.0);  
             }
            
                        System.out.println("#### repayMap : sreee"+repayMap);
            showExistingInstallment(data);
            resultMap = interestCalculator.getInterest(map);
            if (resultMap.containsKey("TABLE_RECORDS")){
                if(!hideSubmit)
                installmentAllTabRecords = (ArrayList) resultMap.get("TABLE_RECORDS");
                System.out.println("####installmentalltabrecord"+installmentAllTabRecords);
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
                    System.out.println("### Data Size : " + installmentAllTabRecords.size());
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
                System.out.println("checkInstallmentall"+installmentAll);
                tableUtilInstallment.setAllValues(installmentAll);
            }
            if (resultMap.containsKey("TOTAL_REPAY_AMOUNT")){
                totalRepayAmount = CommonUtil.convertObjToDouble(resultMap.get("TOTAL_REPAY_AMOUNT")).doubleValue();
            }
            double interestAmount = CommonUtil.convertObjToDouble(resultMap.get(InterestCalculationConstants.INTEREST)).doubleValue();
            double prinAmount = CommonUtil.convertObjToDouble(resultMap.get(InterestCalculationConstants.AMOUNT)).doubleValue();
            System.out.println("hrereerrere"+data);
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
    
    
    
    private void showExistingInstallment(HashMap collectedData){
             HashMap map=new HashMap();
             
             try{
                 
                 HashMap dataMap =new HashMap();
                 map.put("ACCT_NUM",collectedData.get("ACT_NO"));
                 existTotals=0;
                 if(collectedData.containsKey("SCHEDULE_ID"))
                     map.put("SCHEDULE_ID",collectedData.get("SCHEDULE_ID"));
                 else{
                     return;
                 }
                    if(collectedData.get(REPAYMENT_TYPE).equals("UNIFORM_PRINCIPLE_EMI"))
                        dataMap.put(CommonConstants.MAP_NAME,"selectLoansInstallmentTL");
                    else
                        dataMap.put(CommonConstants.MAP_NAME,"selectLoansInstallmentEMITL");
                 
                 dataMap.put(CommonConstants.MAP_WHERE,map);
                 
                 HashMap resultMap=ClientUtil.executeTableQuery(dataMap);
                 if(resultMap !=null && resultMap.size()>0 && resultMap.get("TABLEHEAD") !=null){
                     hideSubmit=true;
                     installmentAllTabRecords=new ArrayList();
                     installmentAllTabRecords=    (ArrayList) resultMap.get("TABLEDATA");
                     for(int i=0;i<installmentAllTabRecords.size();i++){
                         ArrayList singleList=(ArrayList)installmentAllTabRecords.get(i);
                     existTotals+=CommonUtil.convertObjToDouble(singleList.get(4)).doubleValue();
                     }
                 }
                 else
                     hideSubmit=false;
                 //                observable.existInstallment(resultMap,uniformPrincipal);
                 
             }catch (Exception e){
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
        System.out.println("#####nrowcounttotal"+rowcount);
        
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
            System.out.println("##### k = "+k);
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
        System.out.println("repayAmt^#^#^"+repayAmt);
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
    
}