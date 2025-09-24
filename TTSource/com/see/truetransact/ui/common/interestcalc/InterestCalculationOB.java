/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition. 
 * 
 * InterestCalculationOB.java
 *
 * Created on March 23, 2004, 5:44 PM
 */

package com.see.truetransact.ui.common.interestcalc;

import com.see.truetransact.clientexception.ClientParseException;
import com.see.truetransact.clientproxy.ProxyFactory;
import com.see.truetransact.clientutil.ClientConstants;
import com.see.truetransact.commonutil.CommonConstants;
import com.see.truetransact.clientutil.ComboBoxModel;
import com.see.truetransact.clientutil.TableModel;
import com.see.truetransact.commonutil.CommonConstants;
import com.see.truetransact.commonutil.CommonUtil;
import com.see.truetransact.commonutil.DateUtil;
import com.see.truetransact.clientutil.EnhancedTableModel;
import com.see.truetransact.commonutil.InterestCalc;
import com.see.truetransact.commonutil.interestcalc.InterestCalculation;
import com.see.truetransact.commonutil.interestcalc.DateDifference;
import com.see.truetransact.commonutil.interestcalc.Rounding;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Observable;
import java.util.GregorianCalendar;


/**
 *
 * @author  Pinky
 */

public class InterestCalculationOB  extends Observable {
    
    /** Creates a new instance of InterestCalculationOB */
    private static InterestCalculationOB interestCalculationOB;
    private final static ClientParseException parseException = ClientParseException.getInstance();
    private ProxyFactory proxy;
    private HashMap map;
    private HashMap keyValue;
    private ArrayList key;
    private ArrayList value;
    
    private InterestCalculation interestCalculation = new InterestCalculation();
    
    private ComboBoxModel cbmCompounded;
    private ComboBoxModel cbmProductID;
    private ComboBoxModel cbmRoundingPrincipal;
    private ComboBoxModel cbmRoundingInterest;
    private ComboBoxModel cbmPrincipalRoundingValue;
    private ComboBoxModel cbmYear;
    private ComboBoxModel cbmMonth;
    private ComboBoxModel cbmFloatPrecision;
    private ComboBoxModel cbmGracePeriod;
    private ComboBoxModel cbmAccountType;
    private ComboBoxModel cbmCompoundingType;
    private ComboBoxModel cbmRateOption;
    private ComboBoxModel cbmCategory;
    private ComboBoxModel cbmInterestPaymentFrequency;
    private TableModel tbmPeriodAmount;
    private EnhancedTableModel tbmResult;
    private ArrayList tblListResult = new ArrayList();
    
    //added Deposits
    private ComboBoxModel cbmDepositsCompounded ;
    
    final static int SIMPLE_INTEREST =1;
    final static int COMPOUND_INTEREST =2;
    final static int CONTINUOUS_COMPOUND =3;
    
    private int actionType;
    private int result;
    private String lblStatus = ClientConstants.ACTION_STATUS[ClientConstants.ACTIONTYPE_CANCEL];
    
    static {
        try {
            interestCalculationOB = new InterestCalculationOB();
        } catch(Exception e) {
            parseException.logException(e,true);
        }
    }
    
    private InterestCalculationOB()throws Exception{
        map = new HashMap();
        map.put(CommonConstants.JNDI, "groupeenJNDI");
        map.put(CommonConstants.HOME, "sysadmin.group.GroupScreenHome");
        map.put(CommonConstants.REMOTE,"sysadmin.group.GroupScreen");
        try {
            proxy = ProxyFactory.createProxy();
        } catch (Exception e) {
            System.err.println( "Exception " + e + "Caught" );
            e.printStackTrace();
        }
        populateCombo();
        fillComboProdID();
        setTable();
        tbmResult = new EnhancedTableModel(null, tblListResult); 

//        initTableModel();
    }
    public static InterestCalculationOB getInstance() {
        return interestCalculationOB;
    }
    
    private void initTableModel() {
//        tbmResult = new EnhancedTableModel(
//            new Object [][] {
//
//            },
//            new String [] {
//                "Account No", "Calculated Interest", "Total Amount"
//            }
//            
//        ); 
//        tbmResult = new EnhancedTableModel(null, tblListResult); 
    }
    
    private String txtPrincipal = "";
    private boolean rdoPeriodOption_Duration = false;
    private boolean rdoPeriodOption_Date = false;
    private String tdtFromDate = "";
    private String tdtToDate = "";
    private String cboProductID = "";
    private String cboCategory = "";
    private String cboInterestPaymentFrequency = "";
    private String cboYear = "";
    private String cboMonth = "";
    private String txtGracePeriod = "";
    private String cboGracePeriod = "";
    private String txtDays = "";
    private String txtMonths = "";
    private String txtYears = "";
    private boolean rdoInterestOption_Simple = false;
    private boolean rdoInterestOption_Fixed = false;
    private boolean rdoInterestOption_Compound = false;
    private boolean rdoInterestOption_Floating = false;
    private String cboAccountType = "";
    private String txtRateofInterest = "";
    private String txtPenalRate = "";
    private boolean rdoReport_Summary = false;
    private boolean rdoReport_Details = false;
    private String cboRoundingPrincipal = "";
    private String cboRoundingInterest = "";
    private String cboFloatPrecision = "";
    private String txtInterestCreditHead = "";
    private String txtInterestDebitHead = "";
    private String txtTotalInterest = "";
    private String txtTotalAmount = "";
    private String txtFromAccount = "";
    
    private String cboRateOption = "";
    private String cboCompounded = "";
    private String cboCompoundingType = "";
    private String cboDepositsCompounded = "";
    
    
    //deposits
    
    // Setter method for txtPrincipal
    void setTxtPrincipal(String txtPrincipal){
        this.txtPrincipal = txtPrincipal;
        setChanged();
    }
    // Getter method for txtPrincipal
    String getTxtPrincipal(){
        return this.txtPrincipal;
    }
    
    // Setter method for rdoPeriodOption_Duration
    void setRdoPeriodOption_Duration(boolean rdoPeriodOption_Duration){
        this.rdoPeriodOption_Duration = rdoPeriodOption_Duration;
        setChanged();
    }
    // Getter method for rdoPeriodOption_Duration
    boolean getRdoPeriodOption_Duration(){
        return this.rdoPeriodOption_Duration;
    }
    
    // Setter method for rdoPeriodOption_Date
    void setRdoPeriodOption_Date(boolean rdoPeriodOption_Date){
        this.rdoPeriodOption_Date = rdoPeriodOption_Date;
        setChanged();
    }
    // Getter method for rdoPeriodOption_Date
    boolean getRdoPeriodOption_Date(){
        return this.rdoPeriodOption_Date;
    }
    
    // Setter method for tdtFromDate
    void setTdtFromDate(String tdtFromDate){
        this.tdtFromDate = tdtFromDate;
        setChanged();
    }
    // Getter method for tdtFromDate
    String getTdtFromDate(){
        return this.tdtFromDate;
    }
    
    // Setter method for tdtToDate
    void setTdtToDate(String tdtToDate){
        this.tdtToDate = tdtToDate;
        setChanged();
    }
    // Getter method for tdtToDate
    String getTdtToDate(){
        return this.tdtToDate;
    }
    
    // Setter method for cboYear
    void setCboYear(String cboYear){
        this.cboYear = cboYear;
        setChanged();
    }
    // Getter method for cboYear
    String getCboYear(){
        return this.cboYear;
    }
    
    // Setter method for cboMonth
    void setCboMonth(String cboMonth){
        this.cboMonth = cboMonth;
        setChanged();
    }
    // Getter method for cboMonth
    String getCboMonth(){
        return this.cboMonth;
    }
    
    // Setter method for txtGracePeriod
    void setTxtGracePeriod(String txtGracePeriod){
        this.txtGracePeriod = txtGracePeriod;
        setChanged();
    }
    // Getter method for txtGracePeriod
    String getTxtGracePeriod(){
        return this.txtGracePeriod;
    }
    
    // Setter method for cboGracePeriod
    void setCboGracePeriod(String cboGracePeriod){
        this.cboGracePeriod = cboGracePeriod;
        setChanged();
    }
    // Getter method for cboGracePeriod
    String getCboGracePeriod(){
        return this.cboGracePeriod;
    }
    
    // Setter method for txtDays
    void setTxtDays(String txtDays){
        this.txtDays = txtDays;
        setChanged();
    }
    // Getter method for txtDays
    String getTxtDays(){
        return this.txtDays;
    }
    
    // Setter method for txtMonths
    void setTxtMonths(String txtMonths){
        this.txtMonths = txtMonths;
        setChanged();
    }
    // Getter method for txtMonths
    String getTxtMonths(){
        return this.txtMonths;
    }
    
    // Setter method for txtYears
    void setTxtYears(String txtYears){
        this.txtYears = txtYears;
        setChanged();
    }
    // Getter method for txtYears
    String getTxtYears(){
        return this.txtYears;
    }
    
    // Setter method for rdoInterestOption_Simple
    void setRdoInterestOption_Simple(boolean rdoInterestOption_Simple){
        this.rdoInterestOption_Simple = rdoInterestOption_Simple;
        setChanged();
    }
    // Getter method for rdoInterestOption_Simple
    boolean getRdoInterestOption_Simple(){
        return this.rdoInterestOption_Simple;
    }
    
    // Setter method for rdoInterestOption_Fixed
    void setRdoInterestOption_Fixed(boolean rdoInterestOption_Fixed){
        this.rdoInterestOption_Fixed = rdoInterestOption_Fixed;
        setChanged();
    }
    // Getter method for rdoInterestOption_Fixed
    boolean getRdoInterestOption_Fixed(){
        return this.rdoInterestOption_Fixed;
    }
    
    // Setter method for rdoInterestOption_Compound
    void setRdoInterestOption_Compound(boolean rdoInterestOption_Compound){
        this.rdoInterestOption_Compound = rdoInterestOption_Compound;
        setChanged();
    }
    // Getter method for rdoInterestOption_Compound
    boolean getRdoInterestOption_Compound(){
        return this.rdoInterestOption_Compound;
    }
    
    // Setter method for rdoInterestOption_Floating
    void setRdoInterestOption_Floating(boolean rdoInterestOption_Floating){
        this.rdoInterestOption_Floating = rdoInterestOption_Floating;
        setChanged();
    }
    // Getter method for rdoInterestOption_Floating
    boolean getRdoInterestOption_Floating(){
        return this.rdoInterestOption_Floating;
    }
    
    // Setter method for cboCompounded
    void setCbmCompounded(ComboBoxModel cbmCompounded){
        this.cbmCompounded = cbmCompounded;
        setChanged();
    }
    // Getter method for cboCompounded
    ComboBoxModel getCbmCompounded(){
        return this.cbmCompounded;
    }
    
    // Setter method for cboAccountType
    void setCboAccountType(String cboAccountType){
        this.cboAccountType = cboAccountType;
        setChanged();
    }
    // Getter method for cboAccountType
    String getCboAccountType(){
        return this.cboAccountType;
    }
    
    // Setter method for txtRateofInterest
    void setTxtRateofInterest(String txtRateofInterest){
        this.txtRateofInterest = txtRateofInterest;
        setChanged();
    }
    // Getter method for txtRateofInterest
    String getTxtRateofInterest(){
        return this.txtRateofInterest;
    }
    
    // Setter method for txtPenalRate
    void setTxtPenalRate(String txtPenalRate){
        this.txtPenalRate = txtPenalRate;
        setChanged();
    }
    // Getter method for txtPenalRate
    String getTxtPenalRate(){
        return this.txtPenalRate;
    }
    
    // Setter method for rdoReport_Summary
    void setRdoReport_Summary(boolean rdoReport_Summary){
        this.rdoReport_Summary = rdoReport_Summary;
        setChanged();
    }
    // Getter method for rdoReport_Summary
    boolean getRdoReport_Summary(){
        return this.rdoReport_Summary;
    }
    
    // Setter method for rdoReport_Details
    void setRdoReport_Details(boolean rdoReport_Details){
        this.rdoReport_Details = rdoReport_Details;
        setChanged();
    }
    // Getter method for rdoReport_Details
    boolean getRdoReport_Details(){
        return this.rdoReport_Details;
    }
    
    // Setter method for cboRoundingPrincipal
    void setCboRoundingPrincipal(String cboRoundingPrincipal){
        this.cboRoundingPrincipal = cboRoundingPrincipal;
        setChanged();
    }
    // Getter method for cboRoundingPrincipal
    String getCboRoundingPrincipal(){
        return this.cboRoundingPrincipal;
    }
    
    // Setter method for cboRoundingInterest
    void setCboRoundingInterest(String cboRoundingInterest){
        this.cboRoundingInterest = cboRoundingInterest;
        setChanged();
    }
    // Getter method for cboRoundingInterest
    String getCboRoundingInterest(){
        return this.cboRoundingInterest;
    }
    
    // Setter method for cboFloatPrecision
    void setCboFloatPrecision(String cboFloatPrecision){
        this.cboFloatPrecision = cboFloatPrecision;
        setChanged();
    }
    // Getter method for cboFloatPrecision
    String getCboFloatPrecision(){
        return this.cboFloatPrecision;
    }
    
    // Setter method for txtInterestCreditHead
    void setTxtInterestCreditHead(String txtInterestCreditHead){
        this.txtInterestCreditHead = txtInterestCreditHead;
        setChanged();
    }
    // Getter method for txtInterestCreditHead
    String getTxtInterestCreditHead(){
        return this.txtInterestCreditHead;
    }
    
    // Setter method for txtInterestDebitHead
    void setTxtInterestDebitHead(String txtInterestDebitHead){
        this.txtInterestDebitHead = txtInterestDebitHead;
        setChanged();
    }
    // Getter method for txtInterestDebitHead
    String getTxtInterestDebitHead(){
        return this.txtInterestDebitHead;
    }
    /*private double getDuration() {        
        String yearDuration = ((String)cbmYear.getSelectedItem());               
        int year=365;
        if (yearDuration.equals("360"))
              year=360;
        interestCalculation.setYearPeriod(year);
        
        double duration=0;
        Double days;
        if ( getRdoPeriodOption_Duration()== true) {           
            days= CommonUtil.convertObjToDouble(getTxtDays());
            if (days != null)
                duration += days.doubleValue();
            days = CommonUtil.convertObjToDouble(getTxtMonths());
            if(days != null)
                duration +=(30* days.doubleValue());
            days =  CommonUtil.convertObjToDouble(getTxtYears());
            if(days != null)
                duration+= (year*days.doubleValue());            
            duration=duration/year;
        }
        else if (getRdoPeriodOption_Date()== true) {
            boolean isActualMonth=true;
            Date d1 = DateUtil.getDateMMDDYYYY(getTdtFromDate());
            Date d2 = DateUtil.getDateMMDDYYYY(getTdtToDate());
            String monthPeriod = (String)cbmMonth.getSelectedItem();            
            duration = DateDifference.difference(d1,d2,monthPeriod,yearDuration);            
        }
        System.out.println("duration"+duration);
        return duration;
    }*/
    
    private double returnDuration(String compounded, double d, double m, double y, int year) {
        double duration =0;
        if (compounded.length()>0 && !compounded.equals("") && compounded != null) {
            if (compounded.equalsIgnoreCase("DAILY")) {
                duration = y*year;
                duration += m*30;
                duration += d;
            } else if (compounded.equalsIgnoreCase("WEEKLY")) {
                duration = y*52;
                duration += m*4;
                duration += d/7;
            } else if (compounded.equalsIgnoreCase("FORTNIGHTLY")) {
                duration = y*24;
                duration += m/2;
                duration += d/60;
            } else if (compounded.equalsIgnoreCase("MONTHLY")) {
                duration = y*12;
                duration += m;
                duration += d/30;
            } else if (compounded.equalsIgnoreCase("BIMONTHLY")) {
                duration = y*24;
                duration += m/2;
                duration += d/60;
            } else if (compounded.equalsIgnoreCase("QUARTERLY")) {
                duration = y*4;
                duration += m/3;
                duration += d/90;
            } else if (compounded.equalsIgnoreCase("SEMIANNUALLY")) {
                duration = y*2;
                duration += m/6;
                duration += d/180;
            } else if (compounded.equalsIgnoreCase("ANNUALLY")) {
                duration = y;
                duration += m/12;
                duration += d/year;
            }
        }
        return duration;
    }
    
    private double getDuration() {
        String yearDuration = ((String)cbmYear.getSelectedItem());
        int year=365;
        if (yearDuration.equals("360"))
            year=360;
        
       interestCalculation.setYearPeriod(year);
        
        double duration=0;
        double d=0,m=0,y=0;
        Double days,months,years;        
        String compounded = (String)cbmDepositsCompounded.getKeyForSelected();
        if ( getRdoPeriodOption_Duration()== true) {
            days = CommonUtil.convertObjToDouble(getTxtDays());
            if (days != null)
                d += days.doubleValue();
            
            months = CommonUtil.convertObjToDouble(getTxtMonths());
            if (months != null)
                m += months.doubleValue();
            
            years = CommonUtil.convertObjToDouble(getTxtYears());
            if (years != null)
                y += years.doubleValue();
            
            duration = returnDuration(compounded,d,m,y,year);
        }
        else if (getRdoPeriodOption_Date()== true) {
            boolean isActualMonth=true;
            int dividend =0;
            
            Date d1 = DateUtil.getDateMMDDYYYY(getTdtFromDate());
            Date d2 = DateUtil.getDateMMDDYYYY(getTdtToDate());
            GregorianCalendar cal1 = new GregorianCalendar();
            GregorianCalendar cal2 = new GregorianCalendar();
            cal1.setTime(d1);
            cal2.setTime(d2);
            
            if (yearDuration.equals("365"))
                year = 365;
            else if (yearDuration.equals("360"))
                year = 360;
            else if (yearDuration.equals("Actual")) {
                if(cal1.isLeapYear(cal1.get(cal1.YEAR)))
                    year = 366;
                else
                    year = 365;
            }
            String monthPeriod = (String)cbmMonth.getSelectedItem();
            
            if (monthPeriod.equals("30") || monthPeriod.equals("30E"))
                dividend = 30;
            else if (monthPeriod.equals("ActualMonth"))
                dividend = cal1.getActualMaximum(cal1.DATE);
            else
                dividend = 30;
            
            y = DateDifference.difference(d1,d2,monthPeriod,yearDuration);
            
            d = y*year;
            y = d/year;
            d = d%year;
            m = d/dividend;
            d = d%dividend;
            
            duration = returnDuration(compounded,d,m,y,year);
        }        
        return duration;
    }

    public void doAction() {
        double interest;
        double amount;
        int i=-1;
        tbmPeriodAmount.setData(new ArrayList());
        tbmPeriodAmount.fireTableDataChanged();
        interestCalculation.setPrincipal(CommonUtil.convertObjToDouble(getTxtPrincipal()).doubleValue());
        interestCalculation.setPeriod(getDuration());
        interestCalculation.setRateOfInterest(CommonUtil.convertObjToDouble(getTxtRateofInterest()).doubleValue());
        System.out.println("####interestCalculation : "+interestCalculation.getRateOfInterest());
        if ( getRdoInterestOption_Simple() == true )
            interestCalculation.setInterestType(SIMPLE_INTEREST);
        else if ( getRdoInterestOption_Compound() == true) {
            System.out.println("####Compound Selected!!!!");
            interestCalculation.setInterestType(COMPOUND_INTEREST);
            interestCalculation.setRateOption(getCboRateOption());
            interestCalculation.setCompoundingPeriod((String)cbmCompounded.getKeyForSelected());
            String rate = CommonUtil.convertObjToStr(interestCalculation.getCompoundingPeriod());
            System.out.println("####interestCalculation rate : "+rate);
            double rateOfInt = interestCalculation.getRateOfInterest();
            rateOfInt = rateOfInt *12 /100;
            interestCalculation.setCompoundingPeriod(String.valueOf(rateOfInt));
            System.out.println("####interestCalculation after : "+interestCalculation.getRateOfInterest());
            //added deposits
//            interestCalculation.setCompoundingPeriod((String)cbmDepositsCompounded.getKeyForSelected());

            interestCalculation.setCompoundingType((String)cbmCompoundingType.getKeyForSelected());

            if (!interestCalculation.getCompoundingPeriod().equals("DAILY")) {
                double period = interestCalculation.getPeriod();
                for(i=1;i<=period;i++) {
                    interestCalculation.setPeriod(i);
                    ArrayList data = new ArrayList();
                    interestCalculation.calculateInterest();
                    amount=interestCalculation.getAmount();
                    data.add(String.valueOf(i));
                    data.add(new Double(principalRounding(amount)));
                    tbmPeriodAmount.addRow(data);
                    tbmPeriodAmount.fireTableDataChanged();
                }
                i=i-1;
                interestCalculation.setPeriod(period);
            }
            
        }
        System.out.println(interestCalculation.getRateOfInterest());
        interestCalculation.calculateInterest();
	System.out.println(interestCalculation.getAmount());
        System.out.println(interestCalculation.getInterest());
        interest =roundoff(interestCalculation.getInterest());
        amount = principalRounding(interestCalculation.getAmount());
        amount =roundoff(interestCalculation.getAmount());
        if(i!= getDuration() && i!=-1){
            System.out.println(i);
            ArrayList data= new ArrayList();
            double leftDays=((getDuration()-i)*interestCalculation.getYearPeriod());
            DecimalFormat d = new DecimalFormat();
            d.setMaximumFractionDigits(0);
            data.add(d.format(leftDays)+" Days");
            data.add(new Double(amount));
            tbmPeriodAmount.addRow(data);
            tbmPeriodAmount.fireTableDataChanged();
            
        }
        setTxtTotalInterest(CommonUtil.convertObjToStr(new Double(interest)));
        setTxtTotalAmount(CommonUtil.convertObjToStr(new Double(amount)));
//        updateResultTable();
        //resetOBFields();
        setChanged();
        notifyObservers();
    }
    public long getNearest(long number,long roundingFactor)  {
        long roundingFactorOdd = roundingFactor;
        if ((roundingFactor%2) != 0)
            roundingFactorOdd +=1;
        long mod = number%roundingFactor;
        if ((mod < (roundingFactor/2)) || (mod < (roundingFactorOdd/2)))
            return lower(number,roundingFactor);
        else
            return higher(number,roundingFactor);
    }
    
    public long lower(long number,long roundingFactor) {
        long mod = number%roundingFactor;
        return number-mod;
    }
    
    public long higher(long number,long roundingFactor) {
        long mod = number%roundingFactor;
        if ( mod == 0)
            return number;
        return (number-mod) + roundingFactor ;
    }
    
    public HashMap calculateIntDetails(HashMap detailsHash){
        InterestCalc interestCalc = new InterestCalc();
        HashMap amtDetHash = new HashMap();
        if (detailsHash.get("BEHAVES_LIKE").equals("FIXED"))
            amtDetHash = interestCalc.calcAmtDetails(detailsHash);             
        double monthAmt = 0.0;
        int period = CommonUtil.convertObjToInt(detailsHash.get("PEROID"));
        double intAmt = CommonUtil.convertObjToDouble(amtDetHash.get("INTEREST")).doubleValue();
        double months = CommonUtil.convertObjToDouble(detailsHash.get("PERIOD_MONTHS")).doubleValue();
        double days = CommonUtil.convertObjToDouble(detailsHash.get("PERIOD_DAYS")).doubleValue();
        double years = CommonUtil.convertObjToDouble(detailsHash.get("PERIOD_YEARS")).doubleValue();
        Date startDt = DateUtil.getDateMMDDYYYY(CommonUtil.convertObjToStr(detailsHash.get("DEPOSIT_DT")));
        double totalPeriod = 0.0;
        int addingPeriod = 0;
        double payAmt = 0.0;
        if (detailsHash.get("BEHAVES_LIKE").equals("FIXED")){
            if(detailsHash.get("INTEREST_TYPE").equals("MONTHLY")){
                monthAmt = intAmt / (months + years *12);
                startDt = DateUtil.addDays(startDt, 30);
                totalPeriod = months + years *12;
                addingPeriod = 30;
            }else if(detailsHash.get("INTEREST_TYPE").equals("QUATERLY")){
                monthAmt = intAmt / ((months + years *12)/3);
                startDt = DateUtil.addDays(startDt, 90);            
                totalPeriod = ((months + years *12)/3);
                addingPeriod = 90;
            }else if(detailsHash.get("INTEREST_TYPE").equals("HALF YEARLY")){
                monthAmt = intAmt / ((months + years *12)/6);
                startDt = DateUtil.addDays(startDt, 180);            
                totalPeriod = ((months + years *12)/6);
                addingPeriod = 180;
            }else if(detailsHash.get("INTEREST_TYPE").equals("YEARLY")){
                monthAmt = intAmt / ((months + years *12)/12);
                startDt = DateUtil.addDays(startDt, 360);
                totalPeriod = ((months + years *12)/12);
                addingPeriod = 360;
            }else if(detailsHash.get("INTEREST_TYPE").equals("DATE OF MATURITY")){
                totalPeriod = 1;
                monthAmt = intAmt;
                startDt = DateUtil.addDays(startDt, period);            
            }
        }else if (!detailsHash.get("BEHAVES_LIKE").equals("DAILY") && !detailsHash.get("BEHAVES_LIKE").equals("FIXED")){
//            monthAmt = intAmt / ((months + years *12)/3);
            startDt = DateUtil.addDays(startDt, 90);            
            totalPeriod = (((days/30) + months + years *12)/3);
            addingPeriod = 90;
        }
        totalPeriod = (double)higher((long)(totalPeriod *100),100)/100;
        monthAmt = (double)getNearest((long)(monthAmt *100),100)/100;        
        tbmResult.setDataArrayList(null,tblListResult);
        ArrayList data = new ArrayList();
        ArrayList newData = new ArrayList();
        if (!detailsHash.get("BEHAVES_LIKE").equals("DAILY") && detailsHash.get("BEHAVES_LIKE").equals("FIXED")){
            for(int i=1;i<=totalPeriod;i++) {
                data = new ArrayList();
                if(!detailsHash.get("INTEREST_TYPE").equals("DATE OF MATURITY")){
                    if(i >1 && i == totalPeriod){
                        double leftPeriod = i;
                        double balanceAmt = intAmt - (monthAmt * (leftPeriod-1));
                        balanceAmt = (double)getNearest((long)(balanceAmt *100),100)/100;        
                        startDt = DateUtil.getDateMMDDYYYY(CommonUtil.convertObjToStr(detailsHash.get("DEPOSIT_DT")));
                        startDt = DateUtil.addDays(startDt, period);                        
                        if(balanceAmt>0){
                            data.add(startDt);
                            data.add(new Double(balanceAmt));                        
                        }
                    }else{
                        data.add(startDt);
                        data.add(new Double(monthAmt));
                        startDt = DateUtil.addDays(startDt, addingPeriod);
                        period = period - addingPeriod;
                    }
                }else{
                    data.add(startDt);
                    data.add(new Double(monthAmt));
                    startDt = DateUtil.addDays(startDt, addingPeriod);                    
                }
                newData.add(data);
            }
            tbmResult.setDataArrayList(newData,tblListResult);
            setChanged();
        }//dont make changes...here after...
        double totintAmt = 0.0;
        double depositAmt = CommonUtil.convertObjToDouble(detailsHash.get("AMOUNT")).doubleValue();       
        if (!detailsHash.get("BEHAVES_LIKE").equals("DAILY") && detailsHash.get("BEHAVES_LIKE").equals("CUMMULATIVE")){
            for(int i=1;i<=totalPeriod;i++) {
                data = new ArrayList();
                if(period <90){
                    double amount = CommonUtil.convertObjToDouble(detailsHash.get("AMOUNT")).doubleValue();                    
                    detailsHash.put("AMOUNT",new Double(depositAmt+totintAmt));
                    detailsHash.put("PEROID",new Double(period));
                    detailsHash.put("BEHAVES_LIKE","FIXED");
                    detailsHash.put("PERIOD_YEARS",new Double(0));
                    detailsHash.put("PERIOD_MONTHS",new Double(0));
                    detailsHash.put("DISCOUNTED_RATE","Y");
                    amtDetHash = interestCalc.calcAmtDetails(detailsHash);
                    intAmt = CommonUtil.convertObjToDouble(amtDetHash.get("INTEREST")).doubleValue();                    
                    startDt = DateUtil.getDateMMDDYYYY(CommonUtil.convertObjToStr(detailsHash.get("DEPOSIT_DT")));
                    startDt = DateUtil.addDays(startDt, period);                        
                    if(intAmt>0){
                        data.add(startDt);
                        data.add(new Double(intAmt));                        
                    }
                    totintAmt = totintAmt+intAmt; 
                }else{
                    double amount = CommonUtil.convertObjToDouble(detailsHash.get("AMOUNT")).doubleValue();
                    detailsHash.put("PEROID",new Double(90));
                    detailsHash.put("AMOUNT",new Double(amount+intAmt));                    
                    amtDetHash = interestCalc.calcAmtDetails(detailsHash);
                    intAmt = CommonUtil.convertObjToDouble(amtDetHash.get("INTEREST")).doubleValue();                    
                    data.add(startDt);
                    data.add(new Double(intAmt));
                    startDt = DateUtil.addDays(startDt, addingPeriod);
                    period = period - 90;
                    totintAmt = totintAmt+intAmt; 
                }
                newData.add(data);
            }
            amtDetHash.put("INTEREST",new Double(totintAmt));
            tbmResult.setDataArrayList(newData,tblListResult);
            setChanged();
        }
        double depAmt = 0.0,totMatAmt = 0.0;
        
        if (!detailsHash.get("BEHAVES_LIKE").equals("DAILY") && detailsHash.get("BEHAVES_LIKE").equals("RECURRING")){
            for(int i=1;i<=totalPeriod;i++) {
                data = new ArrayList();
                double amount = CommonUtil.convertObjToDouble(detailsHash.get("AMOUNT")).doubleValue();
                if(i == 1)
                    detailsHash.put("PEROID",new Double(90));
                else
                    detailsHash.put("PEROID",new Double(90 * i));
                detailsHash.put("AMOUNT",new Double(amount));                    
                amtDetHash = interestCalc.calcAmtDetails(detailsHash);
                intAmt = CommonUtil.convertObjToDouble(amtDetHash.get("INTEREST")).doubleValue();
                depAmt = CommonUtil.convertObjToDouble(amtDetHash.get("AMOUNT")).doubleValue();
                data.add(startDt);
                double balanceAmt = intAmt - totintAmt;
                data.add(new Double(balanceAmt));
                startDt = DateUtil.addDays(startDt, addingPeriod);
                period = period - 90;
                totintAmt = totintAmt + balanceAmt;
                newData.add(data);
            }
            amtDetHash.put("INTEREST",new Double(totintAmt));
            amtDetHash.put("AMOUNT",new Double(depAmt));
            tbmResult.setDataArrayList(newData,tblListResult);
            setChanged();
        }
        return amtDetHash;
    }

    void setTblDepSubNo(EnhancedTableModel tbmResult){
        this.tbmResult = tbmResult;
        setChanged();
    }
    
    EnhancedTableModel getTblDepSubNo(){
        return this.tbmResult;
    }
    
    
    private void updateResultTable() {
        resetTableModel();
        if(!txtTotalInterest.equals("") && !txtTotalAmount.equals("")) {
            java.util.List list = new java.util.ArrayList();
            list.add(txtFromAccount);
            list.add(txtTotalInterest);
            list.add(txtTotalAmount);
            tbmResult.addRow((java.util.ArrayList)list);
        }
    }
    /** Getter for property txtTotalInterest.
     * @return Value of property txtTotalInterest.
     *
     */
    public java.lang.String getTxtTotalInterest() {
        return txtTotalInterest;
    }
    
    /** Setter for property txtTotalInterest.
     * @param txtTotalInterest New value of property txtTotalInterest.
     *
     */
    public void setTxtTotalInterest(java.lang.String txtTotalInterest) {
        this.txtTotalInterest = txtTotalInterest;
    }
    public void resetOBFields() {
        cboProductID="";
        txtFromAccount="";
        txtPrincipal = "";
        rdoPeriodOption_Duration = false;
        rdoPeriodOption_Date = false;
        tdtFromDate = "";
        tdtToDate = "";
        cboYear = "";
        cboMonth = "";
        txtGracePeriod = "";
        cboGracePeriod = "";
        txtDays = "";
        txtMonths = "";
        txtYears = "";
        rdoInterestOption_Simple = false;
        rdoInterestOption_Fixed = false;
        rdoInterestOption_Compound = false;
        rdoInterestOption_Floating = false;
        cboAccountType = "";
        txtRateofInterest = "";
        txtPenalRate = "";
        rdoReport_Summary = false;
        rdoReport_Details = false;
        cboRoundingPrincipal = "";
        cboRoundingInterest = "";
        cboFloatPrecision = "";
        txtInterestCreditHead = "";
        txtInterestDebitHead = "";
        setCboCompounded("");
        setCboDepositsCompounded("");
        setCboCompoundingType("");
        
        //txtTotalInterest = "";
        //txtTotalAmount = "";
        //  setTable();
        resetTableModel();
        setChanged();
        notifyObservers();
    }
    
    public void resetTableModel() {
        int rows = tbmResult.getRowCount();
        for(int i=0;i<rows;i++) {
            tbmResult.removeRow(0);
        }
    }
    private void  populateCombo() {
        ArrayList key = new ArrayList();
        ArrayList value = new ArrayList();
        key.add("DAILY");
        key.add("WEEKLY");
        key.add("FORTNIGHTLY");
        key.add("MONTHLY");
        key.add("BIMONTHLY");
        key.add("QUARTERLY");
        key.add("SEMIANNUALLY");
        key.add("ANNUALLY");
        
        value.add("Daily");
        value.add("Weekly");
        value.add("Fortnightly");
        value.add("Monthly");
        value.add("Bimonthly");
        value.add("Quarterly");
        value.add("Semiannually");
        value.add("Annually");
        
        cbmCompounded = new ComboBoxModel(key,value);
        //added Deposits
        key = new ArrayList();
        value = new ArrayList();
        key.add("DAILY");
        key.add("WEEKLY");
        key.add("FORTNIGHTLY");
        key.add("MONTHLY");
        key.add("BIMONTHLY");
        key.add("QUARTERLY");
        key.add("SEMIANNUALLY");
        key.add("ANNUALLY");
        
        value.add("Daily");
        value.add("Weekly");
        value.add("Fortnightly");
        value.add("Monthly");
        value.add("Bimonthly");
        value.add("Quarterly");
        value.add("Semiannually");
        value.add("Annually");
        
        cbmDepositsCompounded = new ComboBoxModel(key,value);
        
        key = new ArrayList();
        value = new ArrayList();
        key.add("");
        key.add("NEAREST");
        key.add("LOWER");
        key.add("HIGHER");
        value.add("");
        value.add("Nearest");
        value.add("Lower");
        value.add("Higher");
        
        cbmRoundingInterest = new ComboBoxModel(key,value);
        cbmRoundingPrincipal = new ComboBoxModel(key,value);
        
        key = new ArrayList();
        value = new ArrayList();
        key.add("");
        key.add("5_PAISE");
        key.add("10_PAISE");
        key.add("25_PAISE");
        key.add("50_PAISE");
        key.add("1_RUPEE");
        key.add("5_RUPEES");
        key.add("10_RUPEES");
        key.add("50_RUPEES");
        key.add("100_RUPEES");
        
        value.add("");
        value.add("5 paise");
        value.add("10 paise");
        value.add("25 paise");
        value.add("50 paise");
        value.add("1 rupee");
        value.add("5 rupee");
        value.add("10 rupee");
        value.add("50 rupee");
        value.add("100 rupee");
        
        cbmPrincipalRoundingValue = new ComboBoxModel(key,value);
        
        key =  new ArrayList();
        value = new ArrayList();
        
        cbmProductID = new ComboBoxModel(key,value);
        
        key =  new ArrayList();
        value = new ArrayList();
        value.add("360");
        value.add("365");
        value.add("Actual");
        
        
        cbmYear = new ComboBoxModel(key,value);
        
        key =  new ArrayList();
        value = new ArrayList();
        value.add("30");
        value.add("30E");
        value.add("ActualMonth");        
        
        cbmMonth = new ComboBoxModel(key,value);
        
        key =  new ArrayList();
        value = new ArrayList();
        value.add("Days");
        value.add("Months");
        
        cbmGracePeriod = new ComboBoxModel(key,value);
        
        key =  new ArrayList();
        value = new ArrayList();
        value.add("Account-wise");
        value.add("Ledger-wise");
        
        cbmAccountType = new ComboBoxModel(key,value);
        
        key =  new ArrayList();
        value = new ArrayList();
        
        value.add("");
        value.add("0");
        value.add("1");
        value.add("2");
        value.add("3");
        value.add("4");
        
        cbmFloatPrecision = new ComboBoxModel(key,value);
        
        key = new ArrayList();
        value = new ArrayList();
        key.add("COMPOUND");
        key.add("PRESENT_WORTH");
        key.add("PERIODIC_DEPOSIT");
        key.add("PERIODIC_DEPOSITIBA");
        key.add("PRESENT_WORTH_PER_PERIOD");
        key.add("SINKING_FUND");
        key.add("PARTIAL_PAYMENT");
        
        value.add("Compound Interest");
        value.add("Present Worth");
        value.add("Periodic Deposit");
        value.add("Periodic Deposit IBA");
        value.add("Present Worth of Periodic deposit");
        value.add("Sinking Fund");
        value.add("Partial Payment");
        
        cbmCompoundingType = new ComboBoxModel(key,value);
        
        key = new ArrayList();
        value = new ArrayList();
        key.add("");
        key.add("PER_ANNUM");
        key.add("PER_PERIOD");
        
        value.add("");
        value.add("PER ANNUM");
        value.add("PER PERIOD");     
        
        this.cbmRateOption = new ComboBoxModel(key,value);
    }
    
    /** Getter for property cbmProductID.
     * @return Value of property cbmProductID.
     *
     */
    public ComboBoxModel getCbmProductID() {
        return cbmProductID;
    }
    
    /** Setter for property cbmProductID.
     * @param cbmProductID New value of property cbmProductID.
     *
     */
    public void setCbmProductID(ComboBoxModel cbmProductID) {
        this.cbmProductID = cbmProductID;
    }
    
    /** Getter for property cbmRoundingInterest.
     * @return Value of property cbmRoundingInterest.
     *
     */
    public ComboBoxModel getCbmRoundingInterest() {
        return cbmRoundingInterest;
    }
    
    /** Setter for property cbmRoundingInterest.
     * @param cbmRoundingInterest New value of property cbmRoundingInterest.
     *
     */
    public void setCbmRoundingInterest(ComboBoxModel cbmRoundingInterest) {
        this.cbmRoundingInterest = cbmRoundingInterest;
    }
    
    /** Getter for property cbmRoundingPrincipal.
     * @return Value of property cbmRoundingPrincipal.
     *
     */
    public ComboBoxModel getCbmRoundingPrincipal() {
        return cbmRoundingPrincipal;
    }
    
    /** Setter for property cbmRoundingPrincipal.
     * @param cbmRoundingPrincipal New value of property cbmRoundingPrincipal.
     *
     */
    public void setCbmRoundingPrincipal(ComboBoxModel cbmRoundingPrincipal) {
        this.cbmRoundingPrincipal = cbmRoundingPrincipal;
    }
    
    /** Getter for property _actionType.
     * @return Value of property _actionType.
     *
     */
    public int getActionType() {
        return actionType;
    }
    
    /** Setter for property _actionType.
     * @param _actionType New value of property _actionType.
     *
     */
    public void setActionType(int actionType) {
        this.actionType = actionType;
        setStatus();
    }
    
    /** Getter for property _result.
     * @return Value of property _result.
     *
     */
    public int getResult() {
        return result;
    }
    
    /** Setter for property _result.
     * @param _result New value of property _result.
     *
     */
    public void setResult(int result) {
        this.result = result;
    }
    public void setStatus(){
        this.setLblStatus( ClientConstants.ACTION_STATUS[this.getActionType()]);
    }
    
    /** To update the Status based on result performed by doAction() method */
    public void setResultStatus(){
        this.setLblStatus(ClientConstants.RESULT_STATUS[this.getResult()]);
    }
    
    /** Getter for property lblStatus.
     * @return Value of property lblStatus.
     *
     */
    public java.lang.String getLblStatus() {
        return lblStatus;
    }
    
    /** Setter for property lblStatus.
     * @param lblStatus New value of property lblStatus.
     *
     */
    public void setLblStatus(java.lang.String lblStatus) {
        this.lblStatus = lblStatus;
    }
    
    /** Getter for property cbmPrincipalRoundingValue.
     * @return Value of property cbmPrincipalRoundingValue.
     *
     */
    public ComboBoxModel getCbmPrincipalRoundingValue() {
        return cbmPrincipalRoundingValue;
    }
    
    /** Setter for property cbmPrincipalRoundingValue.
     * @param cbmPrincipalRoundingValue New value of property cbmPrincipalRoundingValue.
     *
     */
    public void setCbmPrincipalRoundingValue(ComboBoxModel cbmPrincipalRoundingValue) {
        this.cbmPrincipalRoundingValue = cbmPrincipalRoundingValue;
    }
    
    /** Getter for property cbmAccountType.
     * @return Value of property cbmAccountType.
     *
     */
    public ComboBoxModel getCbmAccountType() {
        return cbmAccountType;
    }
    
    /** Setter for property cbmAccountType.
     * @param cbmAccountType New value of property cbmAccountType.
     *
     */
    public void setCbmAccountType(ComboBoxModel cbmAccountType) {
        this.cbmAccountType = cbmAccountType;
    }
    
    /** Getter for property cbmFloatPrecision.
     * @return Value of property cbmFloatPrecision.
     *
     */
    public ComboBoxModel getCbmFloatPrecision() {
        return cbmFloatPrecision;
    }
    
    /** Setter for property cbmFloatPrecision.
     * @param cbmFloatPrecision New value of property cbmFloatPrecision.
     *
     */
    public void setCbmFloatPrecision(ComboBoxModel cbmFloatPrecision) {
        this.cbmFloatPrecision = cbmFloatPrecision;
    }
    
    /** Getter for property cbmGracePeriod.
     * @return Value of property cbmGracePeriod.
     *
     */
    public ComboBoxModel getCbmGracePeriod() {
        return cbmGracePeriod;
    }
    
    /** Setter for property cbmGracePeriod.
     * @param cbmGracePeriod New value of property cbmGracePeriod.
     *
     */
    public void setCbmGracePeriod(ComboBoxModel cbmGracePeriod) {
        this.cbmGracePeriod = cbmGracePeriod;
    }
    
    /** Getter for property cbmMonth.
     * @return Value of property cbmMonth.
     *
     */
    public ComboBoxModel getCbmMonth() {
        return cbmMonth;
    }
    
    /** Setter for property cbmMonth.
     * @param cbmMonth New value of property cbmMonth.
     *
     */
    public void setCbmMonth(ComboBoxModel cbmMonth) {
        this.cbmMonth = cbmMonth;
    }
    
    /** Getter for property cbmYear.
     * @return Value of property cbmYear.
     *
     */
    public ComboBoxModel getCbmYear() {
        return cbmYear;
    }
    
    /** Setter for property cbmYear.
     * @param cbmYear New value of property cbmYear.
     *
     */
    public void setCbmYear(ComboBoxModel cbmYear) {
        this.cbmYear = cbmYear;
    }
    
    public double roundoff(double interest) {        
        Rounding rd = new Rounding();
        String interestRounding = (String)cbmRoundingInterest.getKeyForSelected();
        String floatPrecisionStr = (String)cbmFloatPrecision.getSelectedItem();
        if (( interestRounding.length() != 0) && (floatPrecisionStr.length()!= 0)){
            int floatPrecision = Integer.parseInt(floatPrecisionStr);
            interest= interest*Math.pow(10, floatPrecision+1);
            long number = (long)interest;
            System.out.println(number);
            if(interestRounding.equals("NEAREST"))
                number =rd.getNearest(number,10);
            else if (interestRounding.equals("LOWER"))
                number =rd.lower(number,10);
            else if (interestRounding.equals("HIGHER"))
                number =rd.higher(number,10);
            interest=(double)number/Math.pow(10, floatPrecision+1);
        }        
        return interest;
    }
    private double principalRounding(double amount) {
        String roundingValue = (String)cbmPrincipalRoundingValue.getKeyForSelected();
        String roundingMode = (String)cbmRoundingPrincipal.getKeyForSelected();
        if( ( roundingValue.length() != 0) && (roundingMode.length() != 0 )) {
            try {
                DecimalFormat d = new DecimalFormat();
                d.setMaximumFractionDigits(2);
                d.setDecimalSeparatorAlwaysShown(true);
                String str = d.parse(d.format(amount)).toString();
                System.out.println("Princilap Amt : String "+str);
                amount = Double.parseDouble(str);
                System.out.println("Princilap Amt : Double "+amount);
                amount = amount*100;
                long principal = (long) amount;
                System.out.println(principal);
                long roundingFactor=1;
                Rounding rd = new Rounding();
                if ( roundingValue.equals("5_PAISE"))
                    roundingFactor = 5;
                else if ( roundingValue.equals("10_PAISE"))
                    roundingFactor = 10;
                else if ( roundingValue.equals("25_PAISE"))
                    roundingFactor = 25;
                else if ( roundingValue.equals("50_PAISE"))
                    roundingFactor = 50;
                else if ( roundingValue.equals("1_RUPEE"))
                    roundingFactor = 100;
                else if ( roundingValue.equals("5_RUPEES"))
                    roundingFactor = 5*100;
                else if ( roundingValue.equals("10_RUPEES"))
                    roundingFactor = 10*100;
                else if ( roundingValue.equals("50_RUPEES"))
                    roundingFactor = 50*100;
                else if ( roundingValue.equals("100_RUPEES"))
                    roundingFactor = 100*100;
                if ( roundingMode.equals("NEAREST"))
                    principal = rd.getNearest(principal,roundingFactor);
                else if (roundingMode.equals("LOWER"))
                    principal = rd.lower(principal,roundingFactor);
                else if (roundingMode.equals("HIGHER"))
                    principal = rd.higher(principal, roundingFactor);
                amount=(double)principal/100;
            }catch(Exception e) {
                e.printStackTrace();
            }
        }
        return amount;
    }
    
    /** Getter for property txtTotalAmount.
     * @return Value of property txtTotalAmount.
     *
     */
    public java.lang.String getTxtTotalAmount() {
        return txtTotalAmount;
    }
    
    /** Setter for property txtTotalAmount.
     * @param txtTotalAmount New value of property txtTotalAmount.
     *
     */
    public void setTxtTotalAmount(java.lang.String txtTotalAmount) {
        this.txtTotalAmount = txtTotalAmount;
    }
    /** Getter for property cbmCompoundingType.
     * @return Value of property cbmCompoundingType.
     *
     */
    public ComboBoxModel getCbmCompoundingType() {
        return cbmCompoundingType;
    }
    
    /** Setter for property cbmCompoundingType.
     * @param cbmCompoundingType New value of property cbmCompoundingType.
     *
     */
    public void setCbmCompoundingType(ComboBoxModel cbmCompoundingType) {
        this.cbmCompoundingType = cbmCompoundingType;
    }
    private void setTable() {
        tblListResult = new ArrayList();
        tblListResult.add("Value Date");
        tblListResult.add("Interest Amount");        
//        tbmResult = new EnhancedTableModel(new ArrayList(),tblListResult);
    }
    
    /** Getter for property tbmPeriodAmount.
     * @return Value of property tbmPeriodAmount.
     *
     */
    public TableModel getTbmPeriodAmount() {
        return tbmPeriodAmount;
    }
    
    /** Setter for property tbmPeriodAmount.
     * @param tbmPeriodAmount New value of property tbmPeriodAmount.
     *
     */
    public void setTbmPeriodAmount(TableModel tbmPeriodAmount) {
        this.tbmPeriodAmount = tbmPeriodAmount;
    }
    
    public void fillComboProdID() {
        try{
            HashMap lookUpHash = new HashMap();
            lookUpHash.put(CommonConstants.MAP_NAME,"getProdIDKayValue4IC");
            keyValue = com.see.truetransact.clientutil.ClientUtil.populateLookupData(lookUpHash);
            getKeyValue((HashMap)keyValue.get(CommonConstants.DATA));
            cbmProductID= new ComboBoxModel(key,value);
            
            lookUpHash = new HashMap();
            lookUpHash.put(CommonConstants.MAP_NAME,null);
            final ArrayList lookup_keys = new ArrayList();
            lookup_keys.add("CATEGORY");
            lookup_keys.add("DEPOSITSPRODUCT.DEPOSITPERIOD");
            lookUpHash.put(CommonConstants.PARAMFORQUERY, lookup_keys);

            keyValue = com.see.truetransact.clientutil.ClientUtil.populateLookupData(lookUpHash);
            getKeyValue((HashMap)keyValue.get("CATEGORY"));
            cbmCategory = new ComboBoxModel(key,value);
            getKeyValue((HashMap)keyValue.get("DEPOSITSPRODUCT.DEPOSITPERIOD"));
            cbmInterestPaymentFrequency = new ComboBoxModel(key,value);
        }catch(Exception e) {
            e.printStackTrace();
        }  
    }
    
    private void getKeyValue(HashMap keyValue)  throws Exception{
        key = (ArrayList)keyValue.get(CommonConstants.KEY);
        value = (ArrayList)keyValue.get(CommonConstants.VALUE);
    }
    
    public void getAccNoDetails(String accNo) {
        HashMap mapName = new HashMap();
        
        mapName.put("ACC_NO",accNo);
        java.util.List result = com.see.truetransact.clientutil.ClientUtil.executeQuery("getSelectDepositSubAccountsINFO",mapName); 
        setTxtFromAccount(accNo);
        if(result!= null && result.size()>0){
            HashMap hash = (HashMap)result.get(0);
            setTxtRateofInterest(CommonUtil.convertObjToStr(hash.get("RATE_OF_INT")));
            setTxtDays(CommonUtil.convertObjToStr(hash.get("DEPOSIT_PERIOD_DD")));
            setTxtMonths(CommonUtil.convertObjToStr(hash.get("DEPOSIT_PERIOD_MM")));
            setTxtYears(CommonUtil.convertObjToStr(hash.get("DEPOSIT_PERIOD_YY")));
            setTxtPrincipal(CommonUtil.convertObjToStr(hash.get("DEPOSIT_AMT")));
       }else {
                System.out.println("Empty List"); 
       }
        notifyObservers();
    }
    
    /** Getter for property txtFromAccount.
     * @return Value of property txtFromAccount.
     *
     */
    public java.lang.String getTxtFromAccount() {
        return txtFromAccount;
    }
    
    /** Setter for property txtFromAccount.
     * @param txtFromAccount New value of property txtFromAccount.
     *
     */
    public void setTxtFromAccount(java.lang.String txtFromAccount) {
        this.txtFromAccount = txtFromAccount;
        setChanged();
    }
    
    /** Getter for property tbmResult.
     * @return Value of property tbmResult.
     *
     */
    public com.see.truetransact.clientutil.EnhancedTableModel getTbmResult() {
        return tbmResult;
    }
    
    /** Setter for property tbmResult.
     * @param tbmResult New value of property tbmResult.
     *
     */
    public void setTbmResult(com.see.truetransact.clientutil.EnhancedTableModel tbmResult) {
        this.tbmResult = tbmResult;
    }
    
    /** Getter for property cboProductID.
     * @return Value of property cboProductID.
     *
     */
    public java.lang.String getCboProductID() {
        return cboProductID;
    }
    
    /** Setter for property cboProductID.
     * @param cboProductID New value of property cboProductID.
     *
     */
    public void setCboProductID(java.lang.String cboProductID) {
        this.cboProductID = cboProductID;
    }
    
    /**
     * Getter for property cboRateOption.
     * @return Value of property cboRateOption.
     */
    public java.lang.String getCboRateOption() {
        return cboRateOption;
    }
    
    /**
     * Setter for property cboRateOption.
     * @param cboRateOption New value of property cboRateOption.
     */
    public void setCboRateOption(java.lang.String cboRateOption) {
        this.cboRateOption = cboRateOption;
    }
    
    /**
     * Getter for property cbmRateOption.
     * @return Value of property cbmRateOption.
     */
    public com.see.truetransact.clientutil.ComboBoxModel getCbmRateOption() {
        return cbmRateOption;
    }
    
    /**
     * Setter for property cbmRateOption.
     * @param cbmRateOption New value of property cbmRateOption.
     */
    public void setCbmRateOption(com.see.truetransact.clientutil.ComboBoxModel cbmRateOption) {
        this.cbmRateOption = cbmRateOption;
    }
    
    /**
     * Getter for property cbmDepositsCompounded.
     * @return Value of property cbmDepositsCompounded.
     */
    ComboBoxModel getCbmDepositsCompounded() {
        return this.cbmDepositsCompounded;
    }    
   
    /**
     * Setter for property cbmDepositsCompounded.
     * @param cbmDepositsCompounded New value of property cbmDepositsCompounded.
     */
    void setCbmDepositsCompounded(ComboBoxModel cbmDepositsCompounded) {
        this.cbmDepositsCompounded = cbmDepositsCompounded;
        setChanged();
    }    
    
    /**
     * Getter for property cboCompounded.
     * @return Value of property cboCompounded.
     */
    public java.lang.String getCboCompounded() {
        return cboCompounded;
    }
    
    /**
     * Setter for property cboCompounded.
     * @param cboCompounded New value of property cboCompounded.
     */
    public void setCboCompounded(java.lang.String cboCompounded) {
        this.cboCompounded = cboCompounded;
    }
    
    /**
     * Getter for property cboCompoundingType.
     * @return Value of property cboCompoundingType.
     */
    public java.lang.String getCboCompoundingType() {
        return cboCompoundingType;
    }
    
    /**
     * Setter for property cboCompoundingType.
     * @param cboCompoundingType New value of property cboCompoundingType.
     */
    public void setCboCompoundingType(java.lang.String cboCompoundingType) {
        this.cboCompoundingType = cboCompoundingType;
    }
    
    /**
     * Getter for property cboDepositsCompounded.
     * @return Value of property cboDepositsCompounded.
     */
    public java.lang.String getCboDepositsCompounded() {
        return cboDepositsCompounded;
    }
    
    /**
     * Setter for property cboDepositsCompounded.
     * @param cboDepositsCompounded New value of property cboDepositsCompounded.
     */
    public void setCboDepositsCompounded(java.lang.String cboDepositsCompounded) {
        this.cboDepositsCompounded = cboDepositsCompounded;
    }
    
    /**
     * Getter for property cboCategory.
     * @return Value of property cboCategory.
     */
    public java.lang.String getCboCategory() {
        return cboCategory;
    }
    
    /**
     * Setter for property cboCategory.
     * @param cboCategory New value of property cboCategory.
     */
    public void setCboCategory(java.lang.String cboCategory) {
        this.cboCategory = cboCategory;
    }
    
    /**
     * Getter for property cbmCategory.
     * @return Value of property cbmCategory.
     */
    public com.see.truetransact.clientutil.ComboBoxModel getCbmCategory() {
        return cbmCategory;
    }
    
    /**
     * Setter for property cbmCategory.
     * @param cbmCategory New value of property cbmCategory.
     */
    public void setCbmCategory(com.see.truetransact.clientutil.ComboBoxModel cbmCategory) {
        this.cbmCategory = cbmCategory;
    }
    
    /**
     * Getter for property cbmInterestPaymentFrequency.
     * @return Value of property cbmInterestPaymentFrequency.
     */
    public com.see.truetransact.clientutil.ComboBoxModel getCbmInterestPaymentFrequency() {
        return cbmInterestPaymentFrequency;
    }
    
    /**
     * Setter for property cbmInterestPaymentFrequency.
     * @param cbmInterestPaymentFrequency New value of property cbmInterestPaymentFrequency.
     */
    public void setCbmInterestPaymentFrequency(com.see.truetransact.clientutil.ComboBoxModel cbmInterestPaymentFrequency) {
        this.cbmInterestPaymentFrequency = cbmInterestPaymentFrequency;
    }
    
    /**
     * Getter for property cboInterestPaymentFrequency.
     * @return Value of property cboInterestPaymentFrequency.
     */
    public java.lang.String getCboInterestPaymentFrequency() {
        return cboInterestPaymentFrequency;
    }
    
    /**
     * Setter for property cboInterestPaymentFrequency.
     * @param cboInterestPaymentFrequency New value of property cboInterestPaymentFrequency.
     */
    public void setCboInterestPaymentFrequency(java.lang.String cboInterestPaymentFrequency) {
        this.cboInterestPaymentFrequency = cboInterestPaymentFrequency;
    }
    
}