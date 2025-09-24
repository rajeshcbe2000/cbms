/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition. 
 * 
 * InterestCalculation.java
 *
 * Created on March 24, 2004, 4:49 PM
 */

package com.see.truetransact.commonutil.interestcalc;
/**
 *
 * @author  Pinky
 *
 */
import com.see.truetransact.commonutil.CommonUtil;
import com.see.truetransact.commonutil.DateUtil;
import com.see.truetransact.clientutil.ClientUtil;
import com.see.truetransact.commonutil.CommonConstants;
import java.util.List;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Calendar;
import java.util.LinkedHashMap;
import java.util.HashMap;
import com.ibatis.db.sqlmap.SqlMap;
import java.util.GregorianCalendar;
import org.apache.log4j.Logger;

public class InterestCalculation {
    private double emivalues;
    private double emivalueto ;
    double lastinstalment;
        
    private double addvalue;
    private final static Logger log = Logger.getLogger(InterestCalculation.class);
    private double principal;
    private double rateOfInterest;
    private double period;
    private double principalOutstand;
    private int interestType;
    final static int SIMPLE_INTEREST = 1;
    final static int COMPOUND_INTEREST = 2;
    int nextInterestPosition = 0;
    int repayInterestSize = 0;
    
    private String compoundingType;
    private String compoundingPeriod;
    
    private int yearPeriod;
    private double interestTotal;
    private long amountTotal;
    private double loanAmt;
    private double repayAmt;
    private double totalRepayAmt;
    private double interest;
    private double amount;
    private double emi;
    private String rateOption = "PER_ANNUM";
    private HashMap repaymentMap;
    private HashMap oneMap;
    private LinkedHashMap installmentAllRecords;
    private HashMap installmentOneRecord;
    private Date lastInstallDate;
    private Date nextInstallDate;
    private Date currentInterestDate;
    private Date nextInterestDate;
    private ArrayList installmentAllTabRecords;
    private ArrayList various_interestList;
    private  Date startInstalldt=new Date();
    private final String BALANCE = "BALANCE";
    private final String COMMAND = "COMMAND";
    private final String FROM_DATE = "FROM_DATE";
    private final String INSTALLMENT_DATE = "INSTALLMENT_DATE";
    private final String INTEREST_AMOUNT = "INTEREST_AMOUNT";
    private final String INTEREST_RATE = "INTEREST_RATE";
    private final String MULTIPLE_INTEREST = "MULTIPLE_INTEREST";
    private final String PRINCIPAL = "PRINCIPAL";
    private final String SLNO = "SLNO";
    private final String TO_DATE = "TO_DATE";
    private final String TOTAL = "TOTAL";
    private InterestCalculationBean interestCalcBean;
    private LoanCalculateInterestWithoutStatic loanCalculateInt=null;
    private int FREQ=0;
    private SqlMap sqlMap;
    private boolean rateOfIntCalculated = false;
    private String noLoanRoundeInt="";
    private int rec_priod = 0;
    private double installmentAmount=0;
    /** Creates a new instance of InterestCalculation */
    public InterestCalculation() {
    }
    
    public InterestCalculation(InterestCalculationBean intCalcBean) {
        this.interestCalcBean = intCalcBean;
    }
    
     public InterestCalculation(InterestCalculationBean intCalcBean, LoanCalculateInterestWithoutStatic loanCalculateInt) {
        this.interestCalcBean = intCalcBean;
        this.loanCalculateInt = loanCalculateInt;
    }
    private void simpleInterest() {
        if (interestCalcBean == null) {
            amount = principal+(principal * rateOfInterest * (period / getYearPeriod()));
         //   System.out.println("SimpleInterestCalculation : "+amount);
        } else {
            period = period - (((period * 365) - ((int)(period * 365)))/365);
        //    System.out.println("simpleInterest period :" +period);
            amount = principal+(principal * rateOfInterest * period);
        }
      //  System.out.println ("Amount : " + amount + " : PRN : " + principal + ":" + rateOfInterest + ":" + period);
        interest = amount - principal;
//        System.out.println("simpleinterestAmount" +amount);
//        System.out.println("simpleinterestinterest" +interest);
//        System.out.println("simpleinterestperiod" +period);
//        System.out.println("simpleinterestrateOfInterest" +rateOfInterest);
        
    }
    
    private void continuousCompounded() {
        amount=principal*Math.exp(period*rateOfInterest);
        interest=amount-principal;
    }
    private void compoundInterest() {
        //Cummulative Deposits.....
     //   System.out.println("####compoundInterest period : "+period);        
//        period = period/30 ; // period displays.... 
    //    System.out.println("####compoundInterest period after : "+period);        
       // System.out.println("####compoundInterest rateOfInterest : "+rateOfInterest);
//        rateOfInterest = Math.pow((1+rateOfInterest*4.0),3.0)-1.0;
//        if (!rateOfIntCalculated) {
//            rateOfInterest = (rateOfInterest * 100*12) ;
//        rateOfInterest = rateOfInterest *12;
     //       System.out.println("####compoundInterest rateOfInterest : "+rateOfInterest);
//            rateOfInterest = (double)getNearest((long)(rateOfInterest*100),100)/100;
         //   System.out.println("####compoundInterest rateOfInterest : "+rateOfInterest);
//            rateOfInterest = rateOfInterest / 100.0;
//        }
//        period = (double)getNearest((long)(period *100),100)/100;
    //    System.out.println("####compoundInterest period after : "+period);        
        amount = principal*(Math.pow((1+rateOfInterest/4.0),period/12 * 4.0));
        interest = amount-principal;
//        rateOfIntCalculated = true;
//        rateOfInterest = rateOfInterest /12;
    //    System.out.println("#### rateOfInterest : "+rateOfInterest);
    }
    private void presentWorth() {
        amount =principal*(1/Math.pow((1+rateOfInterest),period));
        interest=0;
    }
    private void periodicDeposit(){
//        System.out.println("####periodicDeposit interest : "+interest);
//        System.out.println("####periodicDeposit Rateofinterest : "+rateOfInterest);
//        System.out.println("####periodicDeposit period : "+period);
//        System.out.println("####periodicDeposit principal : "+principal);
//        System.out.println("####periodicDeposit amount : "+amount);
        amount=principal*((Math.pow((1+rateOfInterest),period)-1)/rateOfInterest);
        interest=amount-(principal*period);
    }
    
    //Rounding for Periodic Deposits IBA(indian Bank Association)
    public long higher(long number,long roundingFactor) {
        long mod = number%roundingFactor;
        if ( mod == 0)
            return number;
        return (number-mod) + roundingFactor ;
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
    //periodic Deposit IBA
    
    private void periodicDepositIBA() {
//        System.out.println("####periodicDeposit IBAinterest : "+interest);
//        System.out.println("####periodicDeposit IBARateofinterest : "+rateOfInterest);
//        System.out.println("####periodicDeposit IBAperiod : "+period);
//        System.out.println("####periodicDeposit IBAprincipal : "+principal);
//        System.out.println("####periodicDeposit IBAamount : "+amount);
        
//        System.out.println("####periodicDeposit ibaInterestwithout 1/3 : "+ibaInterest);
//        ibaInterest = (double)getNearest((long)(ibaInterest*100),100)/100;
//        System.out.println("####periodicDeposit IBAamountwith 1/3 : "+ibaInterest);

//this is working perfectly... when we create deposit...

        double ibaInterest = (rateOfInterest * 100*12) ;
        double installment = 0.0;
        installment = period /30;
        period = installment /3;
        amount = principal * (Math.pow((1+ibaInterest/400),period)-1) / (1 - Math.pow((1+ibaInterest/400),-1/3.0));        
        interest=amount-(principal*installment);
              
//        System.out.println("####periodicDepositperiod : "+period);                
//        period = period /30;
//        rateOfInterest = rateOfInterest *100;
//        period = (double)getNearest((long)(period *100),100)/100;
//        System.out.println("####periodicDeposit : "+period);        
//        amount = 2 * principal * (1.0+600.0/rateOfInterest) * (Math.pow((1+rateOfInterest/400.0),period/3)-1.0);
//        interest=amount-(principal*period);
//        amount = (double)getNearest((long)(amount*100),5)/100;
//        System.out.println("####periodicDeposit amount : "+amount);
        
   }
    
    private void presentWorthPerPeriod() {
        amount=principal*((1-(1/Math.pow((1+rateOfInterest),period)))/rateOfInterest);
        interest=0;
    }
    private void sinkingFund() {
        amount= principal*(rateOfInterest/(Math.pow((1+rateOfInterest),period)-1));
        interest=0;
    }
    private void partialPayment() {
        amount=principal*(rateOfInterest/(1-(1/Math.pow((1+rateOfInterest),period))));
        interest=0;
    }
    
    private void repayment() {
        amount = principal * (rateOfInterest / (1 - (1 / Math.pow((1 + rateOfInterest), period))));
        emi = amount;
        interest = rateOfInterest * principal;
        amountTotal+=amount;
        interestTotal+=interest;
       // System.out.println("emi"+emi+"interest"+interest);
    }

    private void repaymentEMIF() {
        interest = ((principal*CommonUtil.convertObjToDouble(repaymentMap.get("LOAN_INTEREST_RATE"))*period)/1200)/period;
        amount = (principal/period);
        emi = amount;
        amountTotal+=amount;
        interestTotal+=interest;
    }
    
    private void repaymentUniformPrinciplalEMI() {
        if (installmentAmount > 0) {
            amount = installmentAmount;
        } else {
            amount = principal / period;
        }
        interest = rateOfInterest * principalOutstand;
        emi = amount + LoanCalculateInterest.amountRouding(interest);
        amountTotal+=amount;
        interestTotal+=interest;
        
    }
    
    private void installmentInterestAmt(){
        // To get the interest amount at the time of repayment
        interest = principal * rateOfInterest * period;
        
    //    System.out.println(interest+"INT" +"principle@@@@@@"+principal+"rateof interest ####"+rateOfInterest+"period"+period);
    }
    
    private void calculateAllRepayments() {
        try {
            String emiType = "";
            //System.out.println("repaymentMap :: " + repaymentMap);
            if (repaymentMap.containsKey("REPAYMENT_TYPE"))//EMI_TYPE
            {
                emiType = CommonUtil.convertObjToStr(repaymentMap.get("REPAYMENT_TYPE"));//EMI_TYPE
            }
            if (repaymentMap.containsKey("VARIOUS_INTEREST_RATE")) {
                // If the interest rate is floating throughout the loan period
                various_interestList = ((ArrayList) repaymentMap.get("VARIOUS_INTEREST_RATE"));
                nextInterestPosition = 0;
                repayInterestSize = various_interestList.size();
                // Get the first interest rate and it's Effective rate
                oneMap = (HashMap) various_interestList.get(nextInterestPosition++);
                currentInterestDate = DateUtil.getDateMMDDYYYY(CommonUtil.convertObjToStr(oneMap.get("FROM_DATE")));
                setRateOfInterest(CommonUtil.convertObjToDouble((oneMap).get("INTEREST")).doubleValue());
                setCompoundedValue();
                if (nextInterestPosition < repayInterestSize) {
                    nextInterestDate = DateUtil.getDateMMDDYYYY(CommonUtil.convertObjToStr(((HashMap) various_interestList.get(nextInterestPosition)).get("FROM_DATE")));
                }
            }
            //commented by rishad 02/02/2016
//            if (repaymentMap.containsKey("REPAYMENT_TYPE") && repaymentMap.get("REPAYMENT_TYPE").equals("USER_DEFINED")) {
//                repayment();
//            } else

            if (repaymentMap.containsKey("REPAYMENT_TYPE") && repaymentMap.get("REPAYMENT_TYPE").equals("MORATORIUM")) {
                lastInstallDate = (Date) repaymentMap.get("FROM_DATE");
                nextInstallDate = (Date) repaymentMap.get("TO_DATE");
                installmentAllRecords = new LinkedHashMap();
                installmentOneRecord = new HashMap();

                if (various_interestList.size() == 1) {
                    setPeriod(LoanCalculateInterest.getDuration(getInterestCalculationBean(lastInstallDate, nextInstallDate), this));
                    simpleInterest();
                    HashMap multipleInterestMap = new HashMap();
                    ArrayList multipleInterestList = new ArrayList();
                    multipleInterestMap.put(INTEREST_RATE, String.valueOf(getRateOfInterest()));
                    multipleInterestMap.put(FROM_DATE, DateUtil.getStringDate(lastInstallDate));
                    multipleInterestMap.put(TO_DATE, DateUtil.getStringDate(DateUtil.addDays(nextInstallDate, -1)));
                    multipleInterestList.add(multipleInterestMap);
                    installmentOneRecord.put(MULTIPLE_INTEREST, multipleInterestList);
                    multipleInterestMap = null;
                } else {
                    isVariousInterestRateOccured();
                }
                installmentAllRecords.put(String.valueOf(installmentAllRecords.size()), installmentOneRecord);
            } else if (repaymentMap.get("REPAYMENT_TYPE").equals("REPAYMENT")) {
          //      System.out.println("### repaymentMap@@@#####" + repaymentMap);
            //    System.out.println("####repaymentMap44444" + repaymentMap);
                Date lastInstallDate = (Date) repaymentMap.get("FROM_DATE");
                Date nextInstallDate = (Date) repaymentMap.get("TO_DATE");
                installmentAllRecords = new LinkedHashMap();
                installmentOneRecord = new HashMap();
               // System.out.println(nextInstallDate + " , " + lastInstallDate + "#### various_interestList " + various_interestList);
                //System.out.println("#### various_interestList.size() " + various_interestList.size());
                // If only one interest rate is applicable
                if (various_interestList.size() == 1) {
//                    LoanCalculateInterest loanInt=new LoanCalculateInterest();
                    setPeriod(LoanCalculateInterest.getDuration(getInterestCalculationBean(lastInstallDate, nextInstallDate), this)); //commented by abi testing 18-jan-2010
                    installmentInterestAmt();
                    if (!(getNoLoanRoundeInt() != null && getNoLoanRoundeInt().length() > 0)) {
                    //    System.out.println("interest INMMMM=====" + interest);
                        interest = LoanCalculateInterest.amountRouding(interest);
                    //    System.out.println("interest 222222222222=====" + interest);
                    }
                    HashMap multipleInterestMap = new HashMap();
                    ArrayList multipleInterestList = new ArrayList();
                    multipleInterestMap.put(INTEREST_RATE, String.valueOf(getRateOfInterest()));
                    multipleInterestMap.put(FROM_DATE, DateUtil.getStringDate(lastInstallDate));
                    multipleInterestMap.put(TO_DATE, DateUtil.getStringDate(DateUtil.addDays(nextInstallDate, -1)));
                    multipleInterestList.add(multipleInterestMap);
                    installmentOneRecord.put(MULTIPLE_INTEREST, multipleInterestList);
                    multipleInterestMap = null;
                } else {
                    isVariousInterestRateOccuredForRepayment();
                }

                installmentAllRecords.put(String.valueOf(installmentAllRecords.size()), installmentOneRecord);
            } else {
             //   System.out.println("repay"+repaymentMap);
//                repayment(); 
                repayAmt = 0.0;
                totalRepayAmt = 0.0;
                ArrayList oneRec;
                Date telkFirstInstallDt = null;
                installmentAllTabRecords = new ArrayList();
                installmentAllRecords = new LinkedHashMap();
                // Principal outstanding amount
                principalOutstand = principal;
                // Loan Amount
                loanAmt = principal;
                // Balance amount from the loan amount
                double balanceAmt = principal;
                // Principal amount
                double principalPortion = 0.0;
                // Difference in every installment(It comes because of rounding the value)
                double diff = 0.0;
                // EMI Amount
                double emiAmount = 0.0;
                double emiTotals = 0, intTotal = 0, minusvalue = 0;
                // No. of installments
                double Tprinciple = 0;
                int noOfInstallment = CommonUtil.convertObjToInt(repaymentMap.get("NO_INSTALL"));
                // No. of days between each installment(Repayment Frequency)
                int noOfDays = 30;
                int frequency = CommonUtil.convertObjToInt(repaymentMap.get("REPAYMENT_FREQUENCY"));
                String strInstallDate;
                // Previous installment date(First time Loan's from date(If moratorium given then start after moratorium))
                if(repaymentMap.containsKey("EMIF_DATE")){
                    lastInstallDate = (Date) repaymentMap.get("EMIF_DATE");
                }else{
                    lastInstallDate = (Date) repaymentMap.get("FROM_DATE");
                }
                System.out.println("lastInstallDate%$#%#$%#"+lastInstallDate);
                //Added BY Suresh
                if (CommonUtil.convertObjToStr(CommonConstants.SAL_REC_MODULE).equals("Y")||(repaymentMap.containsKey("SALARY_RECOVERY")&&repaymentMap.get("SALARY_RECOVERY").equals("Y"))) {
                    HashMap whereMap = new HashMap();

                    List recoveryParameterList = ClientUtil.executeQuery("getRecoveryParameters", whereMap);
                    if (recoveryParameterList != null & recoveryParameterList.size() > 0) {
                        whereMap = (HashMap) recoveryParameterList.get(0);
                        int firstDay = 0;
                        int sanctionDay = 0;
                        firstDay = CommonUtil.convertObjToInt(whereMap.get("FIRST_DAY"));
                        sanctionDay = lastInstallDate.getDate();
                     //   System.out.println("###### firstDay : " + firstDay);
                      //  System.out.println("###### sanctionDay : " + sanctionDay);
                        GregorianCalendar cal = new GregorianCalendar((lastInstallDate.getYear() + 1900), lastInstallDate.getMonth(), lastInstallDate.getDate());
                        int lastDayOfMonth = cal.getActualMaximum(cal.DAY_OF_MONTH);
                        telkFirstInstallDt = cal.getTime();
                     //   System.out.println("telkFirstInstallDt"+telkFirstInstallDt);
                        // added by shihad on 12.12.2014 - mantis 9446
                        if (repaymentMap.containsKey("PROD_ID") && repaymentMap.get("PROD_ID") != null) {
                            HashMap paramMap = new HashMap();
                            paramMap.put("PRODUCT_ID", repaymentMap.get("PROD_ID"));
                            List loanTypeList = ClientUtil.executeQuery("getLoanProdCategory", paramMap);
                            if (loanTypeList != null && loanTypeList.size() > 0) {
                                HashMap loanTYpe = (HashMap) loanTypeList.get(0);
                                if (loanTYpe != null) {
                                    String loanTyp = CommonUtil.convertObjToStr(loanTYpe.get("AUTHORIZE_REMARK"));
                                    if (loanTyp != null && loanTyp.length() > 0) {
                                        if (loanTyp.equals("GOLD_LOAN")) {
                                            if (sanctionDay <= firstDay) {
                                                cal.set(lastInstallDate.getYear() + 1901, lastInstallDate.getMonth(), lastDayOfMonth);// modified by shihad
                                                telkFirstInstallDt = cal.getTime();
                                            } else {
                                                cal.set(lastInstallDate.getYear() + 1901, lastInstallDate.getMonth() + 1, lastInstallDate.getDate());// modified by shihad
                                                lastDayOfMonth = cal.getActualMaximum(cal.DAY_OF_MONTH);
                                                telkFirstInstallDt = cal.getTime();
                                                telkFirstInstallDt.setDate(lastDayOfMonth);
                                            }
                                        }
                                    }
                                }
                            }
                        } else {
                        //    System.out.println("coming in else part");
                          //  System.out.println("sanctionDay"+sanctionDay);
                            if (sanctionDay <= firstDay) {
                                cal.set(cal.DAY_OF_MONTH, cal.getActualMaximum(cal.DAY_OF_MONTH));
                                telkFirstInstallDt = cal.getTime();
                            } else {
                                cal.set(cal.DAY_OF_MONTH, 1);
                                cal.set(cal.MONTH, cal.get(cal.MONTH)+1);
                                lastDayOfMonth = cal.getActualMaximum(cal.DAY_OF_MONTH);
                                telkFirstInstallDt = cal.getTime();
                                telkFirstInstallDt.setDate(lastDayOfMonth);
                            }
                        }
                        lastInstallDate = telkFirstInstallDt;
                    }
                }
                if (CommonUtil.convertObjToStr(CommonConstants.SAL_REC_MODULE).equals("Y")||(repaymentMap.containsKey("SALARY_RECOVERY")&&repaymentMap.get("SALARY_RECOVERY").equals("Y"))) {
                    startInstalldt = telkFirstInstallDt;
                    strInstallDate = DateUtil.getStringDate(startInstalldt);
                }
                System.out.println("startInstalldt%$#%#$%#"+startInstalldt);
                Date durationTo_date = null;
                if (repaymentMap.containsKey("TO_DATE") && repaymentMap.get("TO_DATE") != null) {
                    durationTo_date = (Date) repaymentMap.get("TO_DATE");
                }else if(repaymentMap.containsKey("EMIF_DATE")){
                    durationTo_date = (Date) repaymentMap.get("EMIF_DATE");
                }
                System.out.println("durationTo_date%$#%#$%#"+durationTo_date);
                noOfDays = 0;
                int days = 0;
                days = 0;
                double subsidyAmount = 0.0;
                boolean subsidyExists = false;
               // System.out.println("FIRSTnexTINSTALLMENT_DATE#####" + nextInstallDate+"lastInstallDate"+lastInstallDate);
                Date firstInstDt = null;
                int currdat = lastInstallDate.getDate();
                //     System.out.println("curr"+currdat);
                if (repaymentMap.containsKey("INSTALLMENT_AMOUNT")) {
                    installmentAmount = CommonUtil.convertObjToDouble(repaymentMap.get("INSTALLMENT_AMOUNT"));
                }
                //KD-3421 - BY NITHYA
                if (repaymentMap.containsKey("SUBSIDY_EXISTS") && repaymentMap.get("SUBSIDY_EXISTS") != null && repaymentMap.get("SUBSIDY_EXISTS").equals("Y")) {
                    subsidyAmount = CommonUtil.convertObjToDouble(repaymentMap.get("SUBSIDY_AMOUNT"));
                    subsidyExists = true; 
                }
                
                for (int i = noOfInstallment - 1, j = 1; i >= 0; --i, ++j) {
                    if (j == 1) {
                        firstInstDt = (Date) lastInstallDate.clone();
                    }
                    oneRec = new ArrayList();
                    installmentOneRecord = new HashMap();
                    if (isVariousInterestRateOccured() == false) {
                        //
                        if (emiType.equals("EMI")||emiType.equals("USER_DEFINED")) {                            
                            //Added by sreekrishnan 0004985
                            if(repaymentMap.containsKey("EMI_FLAT_RATE")){
                                repaymentEMIF();//EMI FLAT RATE
                            }else{
                                repayment();
                            }
                        }else {
                            repaymentUniformPrinciplalEMI();
                        }
                    }
                    amount = LoanCalculateInterest.amountRouding(amount);//TEMP COMMENT BY ABI ON 17-JAN-09
                    interest = LoanCalculateInterest.amountRouding(interest);//TEMP COMMENT BY ABI ON 17-JAN-09
                    strInstallDate = DateUtil.getStringDate(lastInstallDate);
                    // To level the last installment Principal and previous installment's balance
                    if (i != 0 || noOfInstallment == 1) {
                        if ((emiType.equals("EMI") && !repaymentMap.containsKey("EMI_FLAT_RATE"))||emiType.equals("USER_DEFINED")) //                            principalPortion = amount - interest;
                        // Above line commented and following if condition added by Rajesh
                        {
                            if (j == 1) {
                                principalPortion = amount - interest;
                            } else {
                                principalPortion = emivalueto - interest;
                            }
                        } else {
                            principalPortion = amount;
                        }
                    }
                    principalPortion = LoanCalculateInterest.amountRouding(principalPortion);//TEMP COMMENT BY ABI ON 17-JAN-09
                    principalOutstand -= principalPortion;

                    if (j == 1) {
                        startInstalldt = lastInstallDate;
                    }
                    oneRec.add(String.valueOf(j));
                    installmentOneRecord.put(SLNO, oneRec.get(0));
                    if (startInstalldt.getDate() >= 29) {
                        int numberDate = startInstalldt.getDate();
                        int day = 0;
                        int noOfMonth = 0;
                        if(!repaymentMap.containsKey("USER_DEFINED_GOLD_LOAN_REPAY_FREQUENCY")){//Added by nithya on 06-07-2019 for KD 546 - New Gold Loan -45 days maturity type
                            noOfMonth = (int) frequency / 30;
                        }
                        for (int k = 0; k < noOfMonth; k++) {
                            if ((CommonConstants.SAL_REC_MODULE == null || CommonConstants.SAL_REC_MODULE.equals("") || CommonConstants.SAL_REC_MODULE.equals("N"))&& (repaymentMap.containsKey("SALARY_RECOVERY")&&repaymentMap.get("SALARY_RECOVERY").equals("N"))) {
                                java.util.GregorianCalendar gCalendar = new java.util.GregorianCalendar();
                                gCalendar.setGregorianChange(DateUtil.getDateMMDDYYYY(CommonUtil.convertObjToStr(startInstalldt)));
                                gCalendar.setTime(DateUtil.getDateMMDDYYYY(CommonUtil.convertObjToStr(startInstalldt)));
                                gCalendar.set(gCalendar.DAY_OF_MONTH, 1);
                                gCalendar.set(gCalendar.MONTH, gCalendar.get(gCalendar.MONTH) + 1);
                                int maxDate = gCalendar.getActualMaximum(gCalendar.DAY_OF_MONTH);
                                if (maxDate <= currdat) {
                                    gCalendar.set(gCalendar.DAY_OF_MONTH, maxDate);
                                } else {
                                    gCalendar.set(gCalendar.DAY_OF_MONTH, currdat);
                                }
                                startInstalldt = gCalendar.getTime();
                            }
                            if ((CommonConstants.SAL_REC_MODULE != null && !CommonConstants.SAL_REC_MODULE.equals("") && CommonConstants.SAL_REC_MODULE.equals("Y"))||(repaymentMap.containsKey("SALARY_RECOVERY")&&repaymentMap.get("SALARY_RECOVERY").equals("Y"))) {
                                java.util.GregorianCalendar gCalendar = new java.util.GregorianCalendar();
                                gCalendar.setGregorianChange(DateUtil.getDateMMDDYYYY(CommonUtil.convertObjToStr(startInstalldt)));
                                gCalendar.setTime(DateUtil.getDateMMDDYYYY(CommonUtil.convertObjToStr(startInstalldt)));
                                gCalendar.set(gCalendar.DATE, 1);
                                int curMonth = gCalendar.get(gCalendar.MONTH);
                                int nxtMonth = curMonth + 1;
                                gCalendar.set(gCalendar.MONTH, nxtMonth);
                                gCalendar.set(gCalendar.DATE, gCalendar.getActualMaximum(gCalendar.DATE));
                                startInstalldt = gCalendar.getTime();
                            }
                       //     System.out.println("startInstalldtafter" + startInstalldt);
                        }
                        if (frequency == 1) {
                            int datediff = (int) DateUtil.dateDiff(startInstalldt, durationTo_date);
                            startInstalldt = durationTo_date;
                        }
                        
                        if (frequency == 15) {  // Added by nithya on 04-02-2018 for 0008926: Need to add New Repay frequency in Gold Loan                          
                            startInstalldt = DateUtil.addDays(startInstalldt, frequency);
                        }
                        
                        if(repaymentMap.containsKey("USER_DEFINED_GOLD_LOAN_REPAY_FREQUENCY") && repaymentMap.get("USER_DEFINED_GOLD_LOAN_REPAY_FREQUENCY") != null){//Added by nithya on 06-07-2019 for KD 546 - New Gold Loan -45 days maturity type
                            startInstalldt = DateUtil.addDays(startInstalldt, frequency);
                        }
                        
                        strInstallDate = DateUtil.getStringDate(startInstalldt);
                    //    System.out.println("before" + startInstalldt);
                      //  System.out.println(days + "@@@#########startInstalldt" + startInstalldt);
                    } else {
                        Date date = new Date();
                        strInstallDate = DateUtil.getStringDate(startInstalldt);
                  //      System.out.println("elsefirstdate" + startInstalldt);
                        noOfDays = 0;  
                        int noOfMonth = 0;
                        if(!repaymentMap.containsKey("USER_DEFINED_GOLD_LOAN_REPAY_FREQUENCY")){//Added by nithya on 06-07-2019 for KD 546 - New Gold Loan -45 days maturity type
                          noOfMonth = (int) frequency / 30;
                        }
//                        System.out.println("firstInstDtfirstInstDt" + firstInstDt);
//                        System.out.println("noof month depandeon frequency#####" + noOfMonth);
                        boolean feb = startInstalldt.getMonth() == 1 ? true : false;
                        for (int v = 0; v < noOfMonth; v++) {
                            days = 0;
                            startInstalldt = DateUtil.addDays(startInstalldt, 30);
                       //     System.out.println("startInstalldtstartInstalldt" + startInstalldt);
                            if ((CommonConstants.SAL_REC_MODULE == null || CommonConstants.SAL_REC_MODULE.equals("") || CommonConstants.SAL_REC_MODULE.equals("N"))&&(repaymentMap.containsKey("SALARY_RECOVERY")&&repaymentMap.get("SALARY_RECOVERY").equals("N"))) {
                                if (feb) {
                                    startInstalldt.setDate(firstInstDt.getDate());
                                }
                            }
                       //     System.out.println("elsesecond" + startInstalldt);
                        }
                        if (frequency == 1) {
                            int datediff = (int) DateUtil.dateDiff(startInstalldt, durationTo_date);
                            startInstalldt = durationTo_date;
                        }
                        if (frequency == 15) {  // Added by nithya on 04-02-2018 for 0008926: Need to add New Repay frequency in Gold Loan                          
                            startInstalldt = DateUtil.addDays(startInstalldt, frequency);
                        }
                        if(repaymentMap.containsKey("USER_DEFINED_GOLD_LOAN_REPAY_FREQUENCY") && repaymentMap.get("USER_DEFINED_GOLD_LOAN_REPAY_FREQUENCY") != null){//Added by nithya on 06-07-2019 for KD 546 - New Gold Loan -45 days maturity type
                            startInstalldt = DateUtil.addDays(startInstalldt, frequency);
                        }
                        strInstallDate = DateUtil.getStringDate(startInstalldt);
                    }


                    if ((CommonUtil.convertObjToStr(CommonConstants.SAL_REC_MODULE).equals("Y")||(repaymentMap.containsKey("SALARY_RECOVERY")&&repaymentMap.get("SALARY_RECOVERY").equals("Y"))) && j == 1) {
                        startInstalldt = telkFirstInstallDt;
                        strInstallDate = DateUtil.getStringDate(startInstalldt);
                    }
                    System.out.println("####strInstallDate" + strInstallDate);
                    oneRec.add(strInstallDate);
                    installmentOneRecord.put(INSTALLMENT_DATE, oneRec.get(1));
                    lastInstallDate = DateUtil.addDays(lastInstallDate, noOfDays);
                    noOfDays = 0;
                 //   System.out.println("BEFOREnexTINSTALLMENT_DATE#####" + nextInstallDate);
                    nextInstallDate = DateUtil.addDays(lastInstallDate, noOfDays);
                    nextInstallDate = lastInstallDate;
                   // System.out.println("AFTERnexTINSTALLMENT_DATE#####" + nextInstallDate);
                    diff += ((interest + principalPortion) - interest + principalPortion);
                    
                    if(j == noOfInstallment && subsidyExists && subsidyAmount > 0){ //KD-3421
                        principalPortion += subsidyAmount;
                    }
                    emiAmount = principalPortion;
                    oneRec.add(CommonUtil.convertObjToStr(String.valueOf(LoanCalculateInterest.amountRouding(principalPortion))));//TEMP COMMENT BY ABI ON 17-JAN-09
                    installmentOneRecord.put(PRINCIPAL, oneRec.get(2));
                    interest = LoanCalculateInterest.amountRouding(interest);//TEMP COMMENT BY ABI ON 17-JAN-09
                    intTotal = intTotal + interest;
                    emiAmount += interest;

                    oneRec.add(CommonUtil.convertObjToStr(String.valueOf(interest)));
                    installmentOneRecord.put(INTEREST_AMOUNT, oneRec.get(3));
                    installmentOneRecord.put(INTEREST_RATE, CommonUtil.convertObjToStr(String.valueOf(getRateOfInterest())));
                    repayAmt += (interest + principalPortion);
                    totalRepayAmt += (interest + principalPortion);

                    totalRepayAmt = LoanCalculateInterest.amountRouding(totalRepayAmt);//TEMP COMMENT BY ABI ON 17-JAN-09
                    emivalues = emiAmount;
                    double a = 0, b = 0;
                    if (j == 1) {
                        emivalueto = emivalues;
                    } else if (emivalueto >= emivalues && i != 0) {
                        a = emivalueto - emivalues;
                        addvalue += a;
                    } else if (i != 0) {
                        b = emivalues - emivalueto;
                        minusvalue += b;
                    }
                    double emiAmt = 0;
                    if (i == 0) {
                        emiAmt = emiAmount - addvalue + minusvalue;
                    } else {
                        emiAmt = emiAmount + a - b;
                    }
                    if (!emiType.equals("EMI")) {
                        emiAmt = emiAmount;
                    }
                    emiTotals += emiAmt;
                    oneRec.add(CommonUtil.convertObjToStr(String.valueOf(emiAmt)));
                    installmentOneRecord.put(TOTAL, oneRec.get(4));
                    Tprinciple = Tprinciple + principalPortion;
                  //  System.out.println("#### P tot :EMITYPE " + emiType);
                    if (emiType.equals("UNIFORM_PRINCIPLE_EMI")) {
                        oneRec.add("");
                    }
                    oneRec.add(CommonUtil.convertObjToStr(String.valueOf(LoanCalculateInterest.amountRouding(principalOutstand))));//TEMP COMMENT BY ABI ON 17-JAN-09

                //    System.out.println("balance@@@@@@" + CommonUtil.convertObjToStr(String.valueOf(LoanCalculateInterest.amountRouding(principalOutstand))));
                    if (emiType.equals("UNIFORM_PRINCIPLE_EMI")) //if condition provid by abi for loan principal emi
                    {
                        installmentOneRecord.put(BALANCE, oneRec.get(6));
                    } else {
                        installmentOneRecord.put(BALANCE, oneRec.get(5));
                    }
                    installmentOneRecord.put(COMMAND, com.see.truetransact.commonutil.CommonConstants.TOSTATUS_INSERT);
                    oneRec.add("Y");
                    installmentOneRecord.put("ACTIVE_STATUS", "Y");
                    installmentAllTabRecords.add(oneRec);
                 //   System.out.println("### oneRec : " + oneRec);
                    if (i == 0) {
                        oneRec = new ArrayList();
                        oneRec.add("Total");
                        oneRec.add(new String(""));
                        oneRec.add(CommonUtil.convertObjToStr(String.valueOf(LoanCalculateInterest.amountRouding(Tprinciple))));//TEMP COMMENT BY ABI ON 17-JAN-09
                        oneRec.add(CommonUtil.convertObjToStr(String.valueOf(LoanCalculateInterest.amountRouding(intTotal))));//TEMP COMMENT BY ABI ON 17-JAN-09
                        oneRec.add(CommonUtil.convertObjToStr(String.valueOf(LoanCalculateInterest.amountRouding(emiTotals))));//TEMP COMMENT BY ABI ON 17-JAN-09
                        oneRec.add(new String(""));

                        oneRec.add(new String(""));
                        if (!emiType.equals("EMI")) {
                            oneRec.add(new String(""));
                        }
                        installmentAllTabRecords.add(oneRec);
                    }
                    installmentAllRecords.put(String.valueOf(j), installmentOneRecord);
                    strInstallDate = null;
                    oneRec = null;
                    installmentOneRecord = null;
                    // Assign Principal outstanding amount to Principal
                    if (emiType.equals("EMI") && !repaymentMap.containsKey("EMI_FLAT_RATE")) {
                        principal = principalOutstand;
                    }
                    // To level the last installment Principal and previous installment's balance
                    if (i == 1) {
                        principalPortion = principalOutstand;
                    }
                    // Reduce the period by 1
                    if (emiType.equals("EMI") && !repaymentMap.containsKey("EMI_FLAT_RATE")) {
                        --period;
                    }
                }
                oneMap = null;
                oneRec = null;
            }
        } catch (Exception e) {
            log.info("Exception in calculateAllRepayments: " + e);
            e.printStackTrace();
        }
    }
    
    private boolean isVariousInterestRateOccured(){
        boolean isVariousInterestRateOccured = false;
        try{
            boolean flag = true;
            double oldAmount = getAmount();
            double oldPeriod = getPeriod();
            double oldPrincipal = getPrincipal();
            double totInterestAmt = 0.0;
            if (repaymentMap.containsKey("VARIOUS_INTEREST_RATE")){
                HashMap multipleInterestMap = new HashMap();
                ArrayList multipleInterestList = new ArrayList();
                while(nextInterestPosition < repayInterestSize){
                    // Repeat this while Loop if nextInterestPosition is less than repayInterestSize
                    if (currentInterestDate.compareTo(nextInstallDate) == 0 || (nextInstallDate.compareTo(nextInterestDate) > 0 && lastInstallDate.compareTo(nextInterestDate) < 0)){
                        // If Last installment date less than next interest from date and
                        // if next installment date greater than next interest from date
                        oneMap = (HashMap) various_interestList.get(nextInterestPosition);
                        currentInterestDate = DateUtil.getDateMMDDYYYY(CommonUtil.convertObjToStr(oneMap.get("FROM_DATE")));
                        setRateOfInterest(CommonUtil.convertObjToDouble(((HashMap) various_interestList.get(nextInterestPosition - 1)).get("INTEREST")).doubleValue());
                        setPeriod(LoanCalculateInterest.getDuration(getInterestCalculationBean(lastInstallDate, currentInterestDate), this));
                        setPrincipal(oldPrincipal);
                        simpleInterest();
                        multipleInterestMap = new HashMap();
                        multipleInterestMap.put(INTEREST_RATE, String.valueOf(getRateOfInterest()));
                        multipleInterestMap.put(FROM_DATE, DateUtil.getStringDate(lastInstallDate));
                        multipleInterestMap.put(TO_DATE, DateUtil.getStringDate(DateUtil.addDays(currentInterestDate, -1)));
                        multipleInterestList.add(multipleInterestMap);

                        totInterestAmt += getInterest();
                        nextInterestPosition++;
                        if (nextInterestPosition <= repayInterestSize - 1){
                            nextInterestDate = DateUtil.getDateMMDDYYYY(CommonUtil.convertObjToStr(((HashMap) various_interestList.get(nextInterestPosition)).get("FROM_DATE")));
                        }
                        setPrincipal(oldPrincipal);
                        isVariousInterestRateOccured = true;
                        multipleInterestMap = null;
                        oneMap = null;
                        flag = false;
                    }else{
                        break;
                    }
                }
                if (flag == false){
                    setPeriod(LoanCalculateInterest.getDuration(getInterestCalculationBean(currentInterestDate, nextInstallDate), this));
                    setRateOfInterest(CommonUtil.convertObjToDouble(((HashMap) various_interestList.get(nextInterestPosition - 1)).get("INTEREST")).doubleValue());
                    simpleInterest();
                    totInterestAmt += getInterest();
                    multipleInterestMap = new HashMap();
                    multipleInterestMap.put(INTEREST_RATE, String.valueOf(getRateOfInterest()));
                    multipleInterestMap.put(FROM_DATE, DateUtil.getStringDate(currentInterestDate));
                    multipleInterestMap.put(TO_DATE, DateUtil.getStringDate(DateUtil.addDays(nextInstallDate, -1)));
                    multipleInterestList.add(multipleInterestMap);
                    installmentOneRecord.put(MULTIPLE_INTEREST, multipleInterestList);

                    setInterest(totInterestAmt);
                    setAmount(emi);
                    setPrincipal(emi);
                    setPeriod(oldPeriod);
                }
                if (flag){
                    // If there is no floating interest rate then get the rate fall in that period
                    setRateOfInterest(CommonUtil.convertObjToDouble(((HashMap) various_interestList.get(nextInterestPosition - 1)).get("INTEREST")).doubleValue());
                    setCompoundedValue();
                }
                multipleInterestMap = null;
            }
        }catch(Exception e){
            log.info("Exception in isVariousInterestRateOccured(): "+e);
            e.printStackTrace();
        }
        return isVariousInterestRateOccured;
    }
    
    private boolean isVariousInterestRateOccuredForRepayment(){
        boolean isVariousInterestRateOccuredForRepayment = false;
        try{
            boolean flag = true;
            double totInterestAmt = 0.0;
            if (repaymentMap.containsKey("VARIOUS_INTEREST_RATE")){
            //    System.out.println("VARIOUS_INTEREST_RATE####"+repaymentMap);
                HashMap multipleInterestMap = new HashMap();
                ArrayList multipleInterestList = new ArrayList();
                while(nextInterestPosition < repayInterestSize){
                    // Repeat this while Loop if nextInterestPosition is less than repayInterestSize
                    if (currentInterestDate.compareTo(nextInstallDate) == 0 || (nextInstallDate.compareTo(nextInterestDate) > 0 && lastInstallDate.compareTo(nextInterestDate) < 0)){
                        // If Last installment date less than next interest from date and
                        // if next installment date greater than next interest from date
               //         System.out.println("!!!!!!     :VARIOUS_INTEREST_RATE"+oneMap);
                        oneMap = (HashMap) various_interestList.get(nextInterestPosition);
                        currentInterestDate = DateUtil.getDateMMDDYYYY(CommonUtil.convertObjToStr(oneMap.get("FROM_DATE")));
                        setRateOfInterest(CommonUtil.convertObjToDouble(((HashMap) various_interestList.get(nextInterestPosition - 1)).get("INTEREST")).doubleValue());
                        setPeriod(LoanCalculateInterest.getDuration(getInterestCalculationBean(lastInstallDate, currentInterestDate), this));
                        installmentInterestAmt();
                        multipleInterestMap = new HashMap();
                        multipleInterestMap.put(INTEREST_RATE, String.valueOf(getRateOfInterest()));
                        multipleInterestMap.put(FROM_DATE, DateUtil.getStringDate(lastInstallDate));
                        multipleInterestMap.put(TO_DATE, DateUtil.getStringDate(DateUtil.addDays(currentInterestDate, -1)));
                        multipleInterestList.add(multipleInterestMap);
                        totInterestAmt += getInterest();
                        nextInterestPosition++;
                        if (nextInterestPosition <= repayInterestSize - 1){
                            nextInterestDate = DateUtil.getDateMMDDYYYY(CommonUtil.convertObjToStr(((HashMap) various_interestList.get(nextInterestPosition)).get("FROM_DATE")));
                        }
                        isVariousInterestRateOccuredForRepayment = true;
                        multipleInterestMap = null;
                        oneMap = null;
                        flag = false;
                    }else{
                        break;
                    }
                }
                if (flag == false){
                    setPeriod(LoanCalculateInterest.getDuration(getInterestCalculationBean(currentInterestDate, nextInstallDate), this));
                    setRateOfInterest(CommonUtil.convertObjToDouble(((HashMap) various_interestList.get(nextInterestPosition - 1)).get("INTEREST")).doubleValue());
                    installmentInterestAmt();
                    totInterestAmt += getInterest();
                    multipleInterestMap = new HashMap();
                    multipleInterestMap.put(INTEREST_RATE, String.valueOf(getRateOfInterest()));
                    multipleInterestMap.put(FROM_DATE, DateUtil.getStringDate(currentInterestDate));
                    multipleInterestMap.put(TO_DATE, DateUtil.getStringDate(DateUtil.addDays(nextInstallDate, -1)));
                    multipleInterestList.add(multipleInterestMap);
                    installmentOneRecord.put(MULTIPLE_INTEREST, multipleInterestList);
                    setInterest(totInterestAmt);
                }
                if (flag){
                    // If there is no floating interest rate then get the rate fall in that period
                    setRateOfInterest(CommonUtil.convertObjToDouble(((HashMap) various_interestList.get(nextInterestPosition - 1)).get("INTEREST")).doubleValue());
                    setCompoundedValue();
                }
                multipleInterestMap = null;
            }
        }catch(Exception e){
            log.info("Exception in isVariousInterestRateOccuredForRepayment(): "+e);
            e.printStackTrace();
        }
        return isVariousInterestRateOccuredForRepayment;
    }
    
    private InterestCalculationBean getInterestCalculationBean(Date fromDt, Date toDt){
        InterestCalculationBean intCalcBean = new InterestCalculationBean();
        try{
       //     System.out.println(toDt+"fromDt"+fromDt);
            intCalcBean.setIsDuration_ddmmyy(false);
            intCalcBean.setYearOption(interestCalcBean.getYearOption());
            intCalcBean.setMonthOption(interestCalcBean.getMonthOption());
            intCalcBean.setDuration_FromDt((Date)fromDt.clone());
            intCalcBean.setDuration_ToDt((Date)toDt.clone());
        }catch(Exception e){
            log.info("Exception in getInterestCalculationBean(): "+e);
            e.printStackTrace();
        }
        return intCalcBean;
    }
    
    public boolean checkLoanLimit(){
        if (repayAmt >= loanAmt){
            return true;
        }
        return false;
    }
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        try {
            double test = 12456.50000;
            DecimalFormat d = new DecimalFormat();
            d.setMaximumFractionDigits(2);
            d.setDecimalSeparatorAlwaysShown(true);
            log.info(d.format(test));
            double dbl = d.parse(d.format(test)).doubleValue();
            
            //            log.info(dbl);
        }catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    public double getPeriod() {
        return period;
    }
    
    /** Setter for property period.
     * @param period New value of property period.
     *
     */
    public void setPeriod(double period) {
        this.period = period;
    }
    
    /** Getter for property principal.
     * @return Value of property principal.
     *
     */
    public double getPrincipal() {
        return principal;
    }
    
    /** Setter for property principal.
     * @param principal New value of property principal.
     *
     */
    public void setPrincipal(double principal) {
        this.principal = principal;
    }
    
    /** Getter for property rateOfInterest.
     * @return Value of property rateOfInterest.
     *
     */
    public double getRateOfInterest() {
        return rateOfInterest*100;
    }
    
    /** Setter for property rateOfInterest.
     * @param rateOfInterest New value of property rateOfInterest.
     *
     */
    public void setRateOfInterest(double rateOfInterest) {
        this.rateOfInterest = rateOfInterest/100;
    }
    public void calculateInterest() {
        if ( getInterestType() == SIMPLE_INTEREST ){
            simpleInterest();
        }
        else if ( getInterestType() == COMPOUND_INTEREST ) {
            if ( compoundingPeriod.equals("DAILY") )
                continuousCompounded();
            else 
                compoundType();
        }
//        if (getFREQ() > (getPeriod()*365) ) {
//            interest=0;
//        }
      //  System.out.println("#####calculateInterestgetFREQ" + getFREQ());    
    }
    private void compoundType(){
        if (compoundingType.equals("COMPOUND"))
            compoundInterest();
        if (compoundingType.equals("PRESENT_WORTH"))
            presentWorth();
        else if (compoundingType.equals("PERIODIC_DEPOSIT"))
            periodicDepositIBA();
        else if (compoundingType.equals("PERIODIC_DEPOSITIBA")) {
            periodicDepositIBA();
        }
        else if (compoundingType.equals("PRESENT_WORTH_PER_PERIOD"))
            presentWorthPerPeriod();
        else if (compoundingType.equals("SINKING_FUND"))
            sinkingFund();
        else if (compoundingType.equals("PARTIAL_PAYMENT"))
            partialPayment();
        else if (compoundingType.equals("REPAYMENT"))
            calculateAllRepayments();
        
    }
    /** Getter for property interestType.
     * @return Value of property interestType.
     *
     */
    public int getInterestType() {
        return interestType;
    }
    
    /** Setter for property interestType.
     * @param interestType New value of property interestType.
     *
     */
    public void setInterestType(int interestType) {
        this.interestType = interestType;
    }
    
    /** Getter for property compoundingType.
     * @return Value of property compoundingType.
     *
     */
    public java.lang.String getCompoundingType() {
        return compoundingType;
    }
    
    /** Setter for property compoundingType.
     * @param compoundingType New value of property compoundingType.
     *
     */
    public void setCompoundingType(java.lang.String compoundingType) {
        this.compoundingType = compoundingType;
    }
    private void setCompoundedValue() {
        if(rateOption.equalsIgnoreCase("PER_PERIOD")) {
            /*if ( compoundingPeriod.equals("DAILY"))
                rateOfInterest = rateOfInterest*yearPeriod;*/
            if ( compoundingPeriod.equals("WEEKLY"))
                rateOfInterest = rateOfInterest*(yearPeriod/7);
            else if ( compoundingPeriod.equals("FORTNIGHTLY"))
                rateOfInterest = rateOfInterest*24 ;
            else if ( compoundingPeriod.equals("MONTHLY"))
                rateOfInterest = rateOfInterest*12;
            else if ( compoundingPeriod.equals("BIMONTHLY"))
                rateOfInterest = rateOfInterest*6;
            else if ( compoundingPeriod.equals("QUARTERLY"))
                rateOfInterest = rateOfInterest*4;
            else if ( compoundingPeriod.equals("SEMIANNUALLY"))
                rateOfInterest = rateOfInterest*2;
            else if ( compoundingPeriod.equals("ANNUALLY"))
                rateOfInterest = rateOfInterest*1;
        }else if(rateOption.equalsIgnoreCase("PER_ANNUM")) {
            if ( compoundingPeriod.equals("DAILY"))
                rateOfInterest = rateOfInterest/yearPeriod;
            else if ( compoundingPeriod.equals("WEEKLY"))
                rateOfInterest = rateOfInterest/(yearPeriod/7);
            else if ( compoundingPeriod.equals("FORTNIGHTLY"))
                rateOfInterest = rateOfInterest/24 ;
            else if ( compoundingPeriod.equals("MONTHLY")) {
                rateOfInterest = rateOfInterest/12;
//                rateOfInterest = Math.pow(1.0+(rateOfInterest/((1.0/12.0)*12.0)),1.0/12.0)-1.0;
          //      System.out.println("### RateOfInterest MONTHLY : "+rateOfInterest);
            }
            else if ( compoundingPeriod.equals("BIMONTHLY"))
                rateOfInterest = rateOfInterest/6;
            else if ( compoundingPeriod.equals("QUARTERLY")){
                rateOfInterest = rateOfInterest/4;
//                rateOfInterest = Math.pow((1+rateOfInterest/4.0),1.0/3.0)-1.0;// remarks regarding repayment schdule purpose commented   from advice of srinath ,rajesh mar-9-2010
              //  System.out.println("### RateOfInterest QUARTERLY : "+rateOfInterest);
//                rateOfInterest = Math.pow(1.0+(rateOfInterest/((1.0/3.0)*12.0)),1.0/3.0)-1.0;
            }else if ( compoundingPeriod.equals("SEMIANNUALLY"))
                rateOfInterest = rateOfInterest/2;
            else if ( compoundingPeriod.equals("ANNUALLY"))
                rateOfInterest = rateOfInterest/1;
        }
    }
    
    /** Getter for property yearPeriod.
     * @return Value of property yearPeriod.
     *
     */
     public int getYearPeriod() {
        return yearPeriod;
    }
    
    /** Setter for property yearPeriod.
     * @param yearPeriod New value of property yearPeriod.
     *
     */
    public void setYearPeriod(int yearPeriod) {
        this.yearPeriod = yearPeriod;
    }
    
    /** Getter for property compoundingPeriod.
     * @return Value of property compoundingPeriod.
     *
     */
    public java.lang.String getCompoundingPeriod() {
        return compoundingPeriod;
    }
    
    /** Setter for property compoundingPeriod.
     * @param compoundingPeriod New value of property compoundingPeriod.
     *
     */
    public void setCompoundingPeriod(java.lang.String compoundingPeriod) {
        this.compoundingPeriod = compoundingPeriod;
        setCompoundedValue();
    }
    
    /** Getter for property amount.
     * @return Value of property amount.
     *
     */
    public double getAmount() {
        return amount;
    }
    
    /** Setter for property amount.
     * @param amount New value of property amount.
     *
     */
    public void setAmount(double amount) {
        this.amount = amount;
    }
    
    /** Getter for property interest.
     * @return Value of property interest.
     *
     */
    public double getInterest() {
        return interest;
    }
    
    /** Setter for property interest.
     * @param interest New value of property interest.
     *
     */
    public void setInterest(double interest) {
        this.interest = interest;
    }
    
    /**
     * Getter for property rateOption.
     * @return Value of property rateOption.
     */
    public java.lang.String getRateOption() {
        return rateOption;
    }
    
    /**
     * Setter for property rateOption.
     * @param rateOption New value of property rateOption.
     */
    public void setRateOption(java.lang.String rateOption) {
        this.rateOption = rateOption;
    }
    
    /**
     * Getter for property repaymentMap.
     * @return Value of property repaymentMap.
     */
    public java.util.HashMap getRepaymentMap() {
        return repaymentMap;
    }
    
    /**
     * Setter for property repaymentMap.
     * @param repaymentMap New value of property repaymentMap.
     */
    public void setRepaymentMap(java.util.HashMap repaymentMap) {
        this.repaymentMap = repaymentMap;
    }
    
    /**
     * Getter for property installmentAllTabRecords.
     * @return Value of property installmentAllTabRecords.
     */
    public java.util.ArrayList getInstallmentAllTabRecords() {
        return installmentAllTabRecords;
    }
    
    /**
     * Setter for property installmentAllTabRecords.
     * @param installmentAllTabRecords New value of property installmentAllTabRecords.
     */
    private void setInstallmentAllTabRecords(java.util.ArrayList installmentAllTabRecords) {
        this.installmentAllTabRecords = installmentAllTabRecords;
    }
    
    /**
     * Getter for property lastInstallDate.
     * @return Value of property lastInstallDate.
     */
    private java.util.Date getLastInstallDate() {
        return lastInstallDate;
    }
    
    /**
     * Setter for property lastInstallDate.
     * @param lastInstallDate New value of property lastInstallDate.
     */
    private void setLastInstallDate(java.util.Date lastInstallDate) {
        this.lastInstallDate = lastInstallDate;
    }
    
    /**
     * Getter for property nextInstallDate.
     * @return Value of property nextInstallDate.
     */
    private java.util.Date getNextInstallDate() {
        return nextInstallDate;
    }
    
    /**
     * Setter for property nextInstallDate.
     * @param nextInstallDate New value of property nextInstallDate.
     */
    private void setNextInstallDate(java.util.Date nextInstallDate) {
        this.nextInstallDate = nextInstallDate;
    }
    
    /**
     * Getter for property currentInterestDate.
     * @return Value of property currentInterestDate.
     */
    private java.util.Date getCurrentInterestDate() {
        return currentInterestDate;
    }
    
    /**
     * Setter for property currentInterestDate.
     * @param currentInterestDate New value of property currentInterestDate.
     */
    private void setCurrentInterestDate(java.util.Date currentInterestDate) {
        this.currentInterestDate = currentInterestDate;
    }
    
    /**
     * Getter for property nextInterestDate.
     * @return Value of property nextInterestDate.
     */
    private java.util.Date getNextInterestDate() {
        return nextInterestDate;
    }
    
    /**
     * Setter for property nextInterestDate.
     * @param nextInterestDate New value of property nextInterestDate.
     */
    private void setNextInterestDate(java.util.Date nextInterestDate) {
        this.nextInterestDate = nextInterestDate;
    }
    
    /**
     * Getter for property oneMap.
     * @return Value of property oneMap.
     */
    private java.util.HashMap getOneMap() {
        return oneMap;
    }
    
    /**
     * Setter for property oneMap.
     * @param oneMap New value of property oneMap.
     */
    private void setOneMap(java.util.HashMap oneMap) {
        this.oneMap = oneMap;
    }
    
    /**
     * Getter for property nextInterestPosition.
     * @return Value of property nextInterestPosition.
     */
    private int getCurrentInterestPosition() {
        return nextInterestPosition;
    }
    
    /**
     * Setter for property nextInterestPosition.
     * @param nextInterestPosition New value of property nextInterestPosition.
     */
    private void setCurrentInterestPosition(int nextInterestPosition) {
        this.nextInterestPosition = nextInterestPosition;
    }
    
    /**
     * Getter for property various_interestList.
     * @return Value of property various_interestList.
     */
    private java.util.ArrayList getVarious_interestList() {
        return various_interestList;
    }
    
    /**
     * Setter for property various_interestList.
     * @param various_interestList New value of property various_interestList.
     */
    
    private void setVarious_interestList(java.util.ArrayList various_interestList) {
        this.various_interestList = various_interestList;
    }
    
    /**
     * Getter for property repayInterestSize.
     * @return Value of property repayInterestSize.
     */
    private int getRepayInterestSize() {
        return repayInterestSize;
    }
    
    /**
     * Setter for property repayInterestSize.
     * @param repayInterestSize New value of property repayInterestSize.
     */
    private void setRepayInterestSize(int repayInterestSize) {
        this.repayInterestSize = repayInterestSize;
    }
    
    /**
     * Getter for property repaymentAllRecords.
     * @return Value of property repaymentAllRecords.
     */
    public java.util.LinkedHashMap getInstallmentAllRecords() {
        return installmentAllRecords;
    }
    
    /**
     * Setter for property repaymentAllRecords.
     * @param repaymentAllRecords New value of property repaymentAllRecords.
     */
    public void setInstallmentAllRecords(java.util.LinkedHashMap installmentAllRecords) {
        this.installmentAllRecords = installmentAllRecords;
    }
    
    /**
     * Getter for property loanAmt.
     * @return Value of property loanAmt.
     */
    private double getLoanAmt() {
        return loanAmt;
    }
    
    /**
     * Setter for property loanAmt.
     * @param loanAmt New value of property loanAmt.
     */
    private void setLoanAmt(double loanAmt) {
        this.loanAmt = loanAmt;
    }
    
    /**
     * Getter for property repayAmt.
     * @return Value of property repayAmt.
     */
    private double getRepayAmt() {
        return repayAmt;
    }
    
    /**
     * Setter for property repayAmt.
     * @param repayAmt New value of property repayAmt.
     */
    private void setRepayAmt(double repayAmt) {
        this.repayAmt = repayAmt;
    }
    
    /**
     * Getter for property totalRepayAmt.
     * @return Value of property totalRepayAmt.
     */
    public double getTotalRepayAmt() {
        return totalRepayAmt;
    }
    
    /**
     * Setter for property totalRepayAmt.
     * @param totalRepayAmt New value of property totalRepayAmt.
     */
    private void setTotalRepayAmt(double totalRepayAmt) {
        this.totalRepayAmt = totalRepayAmt;
    }
    
    /**
     * Getter for property emi.
     * @return Value of property emi.
     */
    private double getEmi() {
        return emi;
    }
    
    /**
     * Setter for property emi.
     * @param emi New value of property emi.
     */
    private void setEmi(double emi) {
        this.emi = emi;
    }
    
    /**
     * Getter for property FREQ.
     * @return Value of property FREQ.
     */
    public int getFREQ() {
        return FREQ;
    }
    
    /**
     * Setter for property FREQ.
     * @param FREQ New value of property FREQ.
     */
    public void setFREQ(int FREQ) {
        this.FREQ = FREQ;
    }
    
    /**
     * Getter for property noLoanRoundeInt.
     * @return Value of property noLoanRoundeInt.
     */
    public java.lang.String getNoLoanRoundeInt() {
        return noLoanRoundeInt;
    }
    
    /**
     * Setter for property noLoanRoundeInt.
     * @param noLoanRoundeInt New value of property noLoanRoundeInt.
     */
    public void setNoLoanRoundeInt(java.lang.String noLoanRoundeInt) {
        this.noLoanRoundeInt = noLoanRoundeInt;
    }

    public int getRec_priod() {
        return rec_priod;
    }

    public void setRec_priod(int rec_priod) {
        this.rec_priod = rec_priod;
    }
}
