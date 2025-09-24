/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition. 
 * 
 * commonCalculateInterest.java
 *
 * Created on August 26, 2004, 9:47 AM
 */

package com.see.truetransact.commonutil.interestcalc;

import java.util.HashMap;
import java.util.Date;
import java.util.GregorianCalendar;
import com.see.truetransact.commonutil.CommonConstants;
import com.see.truetransact.commonutil.CommonUtil;
import com.see.truetransact.commonutil.DateUtil;
import java.text.DecimalFormat;

/**
 *
 * @author  Pinky
 */

public class CommonCalculateInterest {
    private static InterestCalculationBean interestBean;
    
    /** Creates a new instance of commonCalculateInterest */
    public CommonCalculateInterest() {
    }
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        InterestCalculationBean testing = new InterestCalculationBean();
        testing.setPrincipalAmt("1000");
        testing.setRateOfInterest("6");
        
        testing.setIsDuration_ddmmyy(false);
//        testing.setDuration_dd("0");
//        testing.setDuration_mm("1");
//        testing.setDuration_yy("0");
        
        // duriation
        testing.setYearOption(InterestCalculationConstants.YEAR_OPTION_ACTUAL);
        testing.setMonthOption(InterestCalculationConstants.MONTH_OPTION_ACTUAL);

        // dates
//        testing.setYearOption(InterestCalculationConstants.YEAR_OPTION_ACTUAL);
//        testing.setMonthOption(InterestCalculationConstants.MONTH_OPTION_ACTUAL);

        testing.setDuration_FromDate("31/03/2004");
        testing.setDuration_ToDate("31/03/2005");

        testing.setInterestType(InterestCalculationConstants.COMPOUND_INTEREST);
        testing.setCompoundingPeriod("30");
        testing.setCompoundingType(InterestCalculationConstants.COMPTYPE_PERIODIC_DEPOSIT);
        testing.setFloatPrecision("2");
        testing.setRoundingType(InterestCalculationConstants.ROUNDING_HIGHER);
        testing.setRoundingFactor(InterestCalculationConstants.ROUNDING_VALUE_10_PAISE);
        
        HashMap testingMap = new HashMap();
        testingMap.put(CommonConstants.DATA, testing);
        //System.out.println(System.currentTimeMillis());
        testingMap = CommonCalculateInterest.getInterest(testingMap);
        //System.out.println(System.currentTimeMillis());
        System.out.println(testingMap.get(InterestCalculationConstants.INTEREST));
        System.out.println(testingMap.get(InterestCalculationConstants.AMOUNT));
        
    }
    
    public static HashMap getInterest(HashMap parameter){
      //  System.out.println("#######getInterestMethod : " + parameter);
        HashMap resultMap = new HashMap();
        try{
            interestBean = (InterestCalculationBean)parameter.get(CommonConstants.DATA);
            
            InterestCalculation ic = new InterestCalculation(interestBean);
//            if (parameter.containsKey("MODE"))
//                ic = new InterestCalculation();
//            else
//                ic = new InterestCalculation(interestBean);
            
            if (parameter.containsKey("REPAYMENT_DETAILS")){
                ic.setRepaymentMap((HashMap) parameter.get("REPAYMENT_DETAILS"));
            }
            populateIC(ic);
            if(parameter.containsKey("FREQ")) 
                ic.setFREQ(CommonUtil.convertObjToInt(parameter.get("FREQ")));
          //  System.out.println("#####getInterestsetFREQ "+ic.getFREQ());
       //     System.out.println("##### ic "+ic);
            if (parameter.containsKey("NO_OF_DAYS")) {  // NO_OF_DAYS only for TOD, OD, AGRI OD
//                ic.setPeriod(CommonUtil.convertObjToInt(parameter.get("NO_OF_DAYS"))/365.0);
                ic.setPeriod(1.0/365); // Calculation will be p * 1 * r /36500. p will be the sum of daily product. So no need of period.
                ic.setRateOfInterest(ic.getRateOfInterest()*12);    // Because rateOption by default per annum, So roi/12 made in populateIC->ic.setCompoundingPeriod()
            }
            ic.calculateInterest();
          //  System.out.println("getInterest calculateInterest"+ic.getInterest());
            ic.setInterest(roundOff(ic.getInterest()));
          //  System.out.println("setInterest roundOffcalculateInterest"+ic.getInterest());            
            ic.setAmount(amountRouding(ic.getAmount()));
      //      System.out.println("setAmount amountRoudingcalculateInterest"+ic.getInterest());            
            resultMap.put(InterestCalculationConstants.INTEREST, new Double(ic.getInterest()));
            resultMap.put(InterestCalculationConstants.AMOUNT, new Double(ic.getAmount()));
            if (parameter.containsKey("REPAYMENT_DETAILS")){
                resultMap.put("TABLE_RECORDS", ic.getInstallmentAllTabRecords());
                resultMap.put("ALL_RECORDS", ic.getInstallmentAllRecords());
                resultMap.put("TOTAL_REPAY_AMOUNT", String.valueOf(ic.getTotalRepayAmt()));
            }
            
            ic=null;
        }catch(Exception e){
            System.out.println("Exception caught in getInterest(): "+e);
            e.printStackTrace();
        }
        return resultMap;
    }
    private static void populateIC(InterestCalculation ic){
        ic.setPrincipal(CommonUtil.convertObjToDouble(interestBean.getPrincipalAmt()).doubleValue());
        ic.setRateOfInterest(CommonUtil.convertObjToDouble(interestBean.getRateOfInterest()).doubleValue());
        
        //ic.setYearPeriod(CommonUtil.convertObjToInt(interestBean.getYearOption()));
        ic.setPeriod(getDuration(ic));
      //  System.out.println("getDuration(ic)" + getDuration(ic));
        ic.setInterestType(getInterestType());
       // System.out.println("interestBean*******^^*^"+interestBean);
     //   System.out.println("interestBean.getCompoundingPeriod()&&&&&&"+interestBean.getCompoundingPeriod());
        ic.setCompoundingPeriod(interestBean.getCompoundingPeriod()==null?"":interestBean.getCompoundingPeriod());
        ic.setCompoundingType(interestBean.getCompoundingType()==null?"":interestBean.getCompoundingType());
    }
    
/*    private static double getDuration(InterestCalculation ic){
        String yearDuration = interestBean.getYearOption();
        int year=365;
        if (yearDuration.equals(InterestCalculationConstants.YEAR_OPTION_360))
            year=360;
        double duration=0;
        if (interestBean.isIsDuration_ddmmyy()) {
            Double days;
            days= CommonUtil.convertObjToDouble(interestBean.getDuration_dd());
            if (days != null)
                duration += days.doubleValue();
            days = CommonUtil.convertObjToDouble(interestBean.getDuration_mm());
            if(days != null)
                duration +=(30* days.doubleValue());
            days =  CommonUtil.convertObjToDouble(interestBean.getDuration_yy());
            if(days != null)
                duration+= (year*days.doubleValue());
            duration=duration/year;
        }else if(!interestBean.isIsDuration_ddmmyy()) {
            Date fromDate = DateUtil.getDateMMDDYYYY(interestBean.getDuration_FromDate());
            Date toDate = DateUtil.getDateMMDDYYYY(interestBean.getDuration_ToDate());
            if(fromDate!=null && toDate!=null)
                duration = DateDifference.difference(fromDate,toDate,interestBean.getMonthOption(),interestBean.getYearOption());
            fromDate = null;
            toDate=null;
        }
        ic.setYearPeriod(year);
        return duration;
    }*/
    
    private static double returnDuration(String compounded, double d, double m, double y, int year) {
        double duration =0;
        if (compounded != null && compounded.length()>0 && !compounded.equals("")) {
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
    
    private static double getDuration(InterestCalculation ic) {
        String yearDuration = interestBean.getYearOption();
        int year=365;
        if (yearDuration.equals(InterestCalculationConstants.YEAR_OPTION_360))
            year=360;
        
       ic.setYearPeriod(year);
        
        double duration=0;
        double d=0,m=0,y=0;
        Double days,months,years;        
        String compounded = interestBean.getCompoundingPeriod(); //(String)cbmCompounded.getKeyForSelected();
        if ( interestBean.isIsDuration_ddmmyy()) {
            days = CommonUtil.convertObjToDouble( interestBean.getDuration_dd());
            if (days != null)
                d += days.doubleValue();
            
            months = CommonUtil.convertObjToDouble(interestBean.getDuration_mm());
            if (months != null)
                m += months.doubleValue();
            
            years = CommonUtil.convertObjToDouble(interestBean.getDuration_yy());
            if (years != null)
                y += years.doubleValue();
            
            duration = returnDuration(compounded,d,m,y,year);
        } else {
            boolean isActualMonth=true;
            int dividend =0;
            
            Date d1 = DateUtil.getDateMMDDYYYY(interestBean.getDuration_FromDate());
            Date d2 = DateUtil.getDateMMDDYYYY(interestBean.getDuration_ToDate());
            GregorianCalendar cal1 = new GregorianCalendar();
            GregorianCalendar cal2 = new GregorianCalendar();
            cal1.setTime(d1);
            cal2.setTime(d2);
            
            if (yearDuration.equals(InterestCalculationConstants.YEAR_OPTION_365))
                year = 365;
            else if (yearDuration.equals(InterestCalculationConstants.YEAR_OPTION_360))
                year = 360;
            else if (yearDuration.equals(InterestCalculationConstants.YEAR_OPTION_ACTUAL)) {
                if(cal1.isLeapYear(cal1.get(cal1.YEAR)))
                    year = 366;
                else
                    year = 365;
            }
            String monthPeriod =  interestBean.getMonthOption();
            
            if (monthPeriod.equals(InterestCalculationConstants.MONTH_OPTION_30) 
                    || monthPeriod.equals(InterestCalculationConstants.MONTH_OPTION_30E))
                dividend = 30;
            else if (monthPeriod.equals(InterestCalculationConstants.MONTH_OPTION_ACTUAL))
                dividend = cal1.getActualMaximum(cal1.DATE);
            else
                dividend = 30;
            
            y = DateDifference.difference(d1,d2,monthPeriod,yearDuration);
            
            if (compounded != null && compounded.length() > 0) {
                d = y*year;
                y = d/year;
                d = d%year;
                m = d/dividend;
                d = d%dividend;
            
                duration = returnDuration(compounded,d,m,y,year);
         //       System.out.println("Duration getDuration "+duration);
            } else {
                duration = y;
            }
        }
        return duration;
    }
    
    public static double getDuration(InterestCalculationBean intCalcBean, InterestCalculation ic){
        String yearDuration = intCalcBean.getYearOption();
        int year=365;
        if (yearDuration.equals(InterestCalculationConstants.YEAR_OPTION_360))
            year=360;
        double duration=0;
        if (intCalcBean.isIsDuration_ddmmyy()) {
            Double days;
            days= CommonUtil.convertObjToDouble(intCalcBean.getDuration_dd());
            if (days != null)
                duration += days.doubleValue();
            days = CommonUtil.convertObjToDouble(intCalcBean.getDuration_mm());
            if(days != null)
                duration +=(30* days.doubleValue());
            days =  CommonUtil.convertObjToDouble(intCalcBean.getDuration_yy());
            if(days != null)
                duration+= (year*days.doubleValue());
            duration=duration/year;
        }else if(!intCalcBean.isIsDuration_ddmmyy()) {
            Date fromDate = DateUtil.getDateMMDDYYYY(intCalcBean.getDuration_FromDate());
            Date toDate = DateUtil.getDateMMDDYYYY(intCalcBean.getDuration_ToDate());
            if(fromDate!=null && toDate!=null)
                duration = DateDifference.difference(fromDate,toDate,intCalcBean.getMonthOption(),intCalcBean.getYearOption());
            fromDate = null;
            toDate=null;
        }
        ic.setYearPeriod(year);
        return duration;
    }
    
    private static int getInterestType(){
        final int SIMPLE_INTEREST = 1;
        final int COMPOUND_INTEREST = 2;
        
        if(interestBean.getInterestType().equalsIgnoreCase(InterestCalculationConstants.SIMPLE_INTEREST))
            return SIMPLE_INTEREST;
        else if(interestBean.getInterestType().equalsIgnoreCase(InterestCalculationConstants.COMPOUND_INTEREST))
            return COMPOUND_INTEREST;
        return -1;
    }
    public static double roundOff(double interest){
        Rounding rd = new Rounding();
        String roundingType = interestBean.getRoundingType();
        String floatPrecisionStr = interestBean.getFloatPrecision();
        if(roundingType!=null && roundingType.length()>0 &&
        floatPrecisionStr!=null && floatPrecisionStr.length()>0) {
            int floatPrecision = Integer.parseInt(floatPrecisionStr);
            interest= interest*Math.pow(10, floatPrecision+1);
            long number = (long)interest;
            if(roundingType.equals(InterestCalculationConstants.ROUNDING_NEAREST))
                if (interestBean.getRoundingFactor()!=null)
                    number =rd.getNearest(number,10);
                else
                    number = rd.getNearest(number, (long)Math.pow(10, floatPrecision+1));
            else if (roundingType.equals(InterestCalculationConstants.ROUNDING_LOWER))
                if (interestBean.getRoundingFactor()!=null)
                    number =rd.lower(number,10);
                else
                    number = rd.lower(number, (long)Math.pow(10, floatPrecision+1));
            else if (roundingType.equals(InterestCalculationConstants.ROUNDING_HIGHER))
                if (interestBean.getRoundingFactor()!=null)
                    number =rd.higher(number,10);
                else
                    number = rd.higher(number, (long)Math.pow(10, floatPrecision+1));
            interest=(double)number/Math.pow(10,floatPrecision+1);
        //    System.out.println("insideroundoffAmount"+interest);
        }
        rd = null;
       // System.out.println("outsideroundoffAmount"+interest);
        return interest;
        
    }
    
    public static double amountRouding(double amount, InterestCalculationBean interestBean) {
        return amountRouding(amount) ;
    }
    
    public static double amountRouding(double amount) {
        String roundingValue = interestBean.getRoundingFactor();
        String roundingMode = interestBean.getRoundingType();
        if(roundingValue!=null && roundingValue.length() != 0 &&
        roundingMode!=null && roundingMode.length() != 0) {
            try {
                DecimalFormat d = new DecimalFormat();
                d.setMaximumFractionDigits(2);
                d.setDecimalSeparatorAlwaysShown(true);
                String str = d.parse(d.format(amount)).toString();
                amount = Double.parseDouble(str);
                amount = amount*100;
                long principal = (long) amount;
                long roundingFactor=1;
                Rounding rd = new Rounding();
                if ( roundingValue.equals(InterestCalculationConstants.ROUNDING_VALUE_5_PAISE))
                    roundingFactor = 5;
                else if ( roundingValue.equals(InterestCalculationConstants.ROUNDING_VALUE_10_PAISE))
                    roundingFactor = 10;
                else if ( roundingValue.equals(InterestCalculationConstants.ROUNDING_VALUE_25_PAISE))
                    roundingFactor = 25;
                else if ( roundingValue.equals(InterestCalculationConstants.ROUNDING_VALUE_50_PAISE))
                    roundingFactor = 50;
                else if ( roundingValue.equals(InterestCalculationConstants.ROUNDING_VALUE_1_RUPEE))
                    roundingFactor = 100;
                else if ( roundingValue.equals(InterestCalculationConstants.ROUNDING_VALUE_5_RUPEES))
                    roundingFactor = 5*100;
                else if ( roundingValue.equals(InterestCalculationConstants.ROUNDING_VALUE_10_RUPEES))
                    roundingFactor = 10*100;
                else if ( roundingValue.equals(InterestCalculationConstants.ROUNDING_VALUE_50_RUPEES))
                    roundingFactor = 50*100;
                else if ( roundingValue.equals(InterestCalculationConstants.ROUNDING_VALUE_100_RUPEES))
                    roundingFactor = 100*100;
                if ( roundingMode.equals(InterestCalculationConstants.ROUNDING_NEAREST))
                    principal = rd.getNearest(principal,roundingFactor);
                else if (roundingMode.equals(InterestCalculationConstants.ROUNDING_LOWER))
                    principal = rd.lower(principal,roundingFactor);
                else if (roundingMode.equals(InterestCalculationConstants.ROUNDING_HIGHER))
                    principal = rd.higher(principal, roundingFactor);
                amount=(double)principal/100;
                
                d = null;
                rd = null;
            }catch(Exception e) {
                e.printStackTrace();
            }
        } else {
                Rounding rd = new Rounding();
                long principal = (long) amount;
                if ( roundingMode.equals(InterestCalculationConstants.ROUNDING_NEAREST))
                    principal = rd.getNearest(principal, 100);
                else if (roundingMode.equals(InterestCalculationConstants.ROUNDING_LOWER))
                    principal = rd.lower(principal, 100);
                else if (roundingMode.equals(InterestCalculationConstants.ROUNDING_HIGHER))
                    principal = rd.higher(principal, 100);
                amount=(double)principal/100;
        }
        return amount;
    }
}