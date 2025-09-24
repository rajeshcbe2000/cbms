/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition. 
 * 
 * InterestCalc.java
 *
 * Created on March 5, 2005, 12:25 PM
 */

package com.see.truetransact.commonutil;


import com.see.truetransact.transferobject.batchprocess.interest.InterestBatchTO;
import com.see.truetransact.commonutil.CommonUtil;
import com.see.truetransact.commonutil.CommonConstants;
import com.see.truetransact.commonutil.interestcalc.InterestCalculation;
import com.see.truetransact.commonutil.interestcalc.InterestCalculationBean;
import com.see.truetransact.commonutil.interestcalc.InterestCalculationConstants;
import com.see.truetransact.commonutil.interestcalc.CommonCalculateInterest ;
import com.see.truetransact.commonutil.DateUtil;
import java.util.List;
import java.util.Date;
import java.util.HashMap;
import java.util.ArrayList;
import java.lang.Math;
/**
 *
 * @author  JK
 */
public class InterestCalc {
    
    
    /** Creates a new instance of InterestCalc */
    public InterestCalc() {
    }
        
    public static final String PRINCIPLE_AMT = "AMOUNT";
    public static final String DEPOSIT_DT = "DEPOSIT_DT";
    public static final String MATURITY_DT = "MATURITY_DT";
    public static final String ROI = "ROI" ;
    public static final String PERIOD_DAYS = "PERIOD_DAYS" ;
    public static final String PERIOD_MONTHS = "PERIOD_MONTHS" ;
    public static final String PERIOD_YEARS = "PERIOD_YEARS" ;
    public static final String ROUND_OFF = "ROUND_OFF" ;
    public static final String ROUND_OFF_HIGHER_VALUE = "HIGHER_VALUE" ;
    public static final String ROUND_OFF_LOWER_VALUE = "LOWER_VALUE" ;
    public static final String ROUND_OFF_NEAREST_VALUE = "NEAREST_VALUE" ;
    public static final String COMP_FREQ = "COMP_FREQ";
    public static final String BEHAVES_LIKE = "BEHAVES_LIKE";
    public final String BEHAVES_LIKE_RECURRING = "RECURRING";
    public final String BEHAVES_LIKE_CUMMULATIVE = "CUMMULATIVE";
    public final String BEHAVES_LIKE_FIXED = "FIXED";
    
    public final String BEHAVES_LIKE_DAILY = "DAILY";
    
    final static int SIMPLE_INTEREST =1;
    final static int COMPOUND_INTEREST =2;
    final static int CONTINUOUS_COMPOUND =3;
    final static int YEAR_PERIOD = 365;
    
    //---Method to Calculate the Maturity Amount, Interst amount etc according to the ROI...
    public HashMap calcAmtDetails(HashMap resultMap){
        //---Fields regarding the Interest Amount(s)...
        double interestAmt = 0;
        double interestOnMaturity= 0;
        
        //---Fields regarding the Rate of Interest...
        String rateMaturity = "0.0";
	if(CommonUtil.convertObjToStr(resultMap.get(BEHAVES_LIKE)).equals(BEHAVES_LIKE_FIXED)) {
            return calcFixedInterest(resultMap);
        }
	if(CommonUtil.convertObjToStr(resultMap.get(BEHAVES_LIKE)).equals(BEHAVES_LIKE_DAILY)) {
            System.out.println("###########behaveslikedaily resultMap : " +resultMap);
            return calcDailyInterest(resultMap);   
//            System.out.println("###########behaveslikedaily : " +resultMap);
        }
            
        InterestCalculationBean objInterestCalculationBean = new InterestCalculationBean();
        objInterestCalculationBean.setPrincipalAmt(CommonUtil.convertObjToStr(resultMap.get("AMOUNT")));
        
        //--- get the Prod Details
//        resultMap.putAll(getProdDetials(resultMap));
        
//        if(resultMap.containsKey("DAYS_YEARS")){
//            objInterestCalculationBean.setYearOption(CommonUtil.convertObjToStr(resultMap.get("DAYS_YEARS")));
            objInterestCalculationBean.setYearOption(InterestCalculationConstants.YEAR_OPTION_ACTUAL);
            
//        }else{
//            objInterestCalculationBean.setYearOption(InterestCalculationConstants.YEAR_OPTION_365);
//        }
        
        objInterestCalculationBean.setMonthOption(InterestCalculationConstants.MONTH_OPTION_ACTUAL);
        objInterestCalculationBean.setDuration_FromDate(DateUtil.getStringDate((Date)resultMap.get(DEPOSIT_DT)));
        objInterestCalculationBean.setDuration_ToDate(DateUtil.getStringDate((Date)resultMap.get(MATURITY_DT)));

//        if(!resultMap.containsKey("ROI")){
//        //---Get the Rate Of Interest for Calculating the Maturity Interest...
//            rateMaturity = setRateOfInterst(resultMap);
//            objInterestCalculationBean.setRateOfInterest(rateMaturity);
//        } else {
           objInterestCalculationBean.setRateOfInterest(CommonUtil.convertObjToStr(resultMap.get("ROI"))); 
//        }
        
//        objInterestCalculationBean.setInterestType(isCompounded(resultMap));
       objInterestCalculationBean.setInterestType(CommonUtil.convertObjToStr(resultMap.get("INT_TYPE")));
       //--- If it is an RD , set the parameter for it appropriately
       if(CommonUtil.convertObjToStr(resultMap.get(BEHAVES_LIKE)).equals(BEHAVES_LIKE_RECURRING)){
//           objInterestCalculationBean.setCompoundingType(InterestCalculationConstants.COMPTYPE_PERIODIC_DEPOSIT);
//           objInterestCalculationBean.setCompoundingPeriod(CommonUtil.convertObjToStr(resultMap.get(COMP_FREQ)));
//           objInterestCalculationBean.setIsDuration_ddmmyy(true);
//           objInterestCalculationBean.setDuration_dd(CommonUtil.convertObjToStr(resultMap.get(PERIOD_DAYS)));
//           objInterestCalculationBean.setDuration_mm(CommonUtil.convertObjToStr(resultMap.get(PERIOD_MONTHS)));
//           objInterestCalculationBean.setDuration_yy(CommonUtil.convertObjToStr(resultMap.get(PERIOD_YEARS)));
           return calcRecurringInterest(resultMap);
       } 
       //--- If it is Cumulative Deposit , set the parameter for it appropriately
       if(CommonUtil.convertObjToStr(resultMap.get(BEHAVES_LIKE)).equals(BEHAVES_LIKE_CUMMULATIVE)){
//            objInterestCalculationBean.setIsDuration_ddmmyy(true);
//            objInterestCalculationBean.setDuration_dd(CommonUtil.convertObjToStr(resultMap.get(PERIOD_DAYS)));
//            objInterestCalculationBean.setDuration_mm(CommonUtil.convertObjToStr(resultMap.get(PERIOD_MONTHS)));
//            objInterestCalculationBean.setDuration_yy(CommonUtil.convertObjToStr(resultMap.get(PERIOD_YEARS)));
//
//           objInterestCalculationBean.setCompoundingType(InterestCalculationConstants.COMPTYPE_COMPOUND);
//           objInterestCalculationBean.setCompoundingPeriod(CommonUtil.convertObjToStr(resultMap.get(COMP_FREQ)));
           return calcCummulativeInterest(resultMap);
       }
       /*else   */   
       if(CommonUtil.convertObjToStr(resultMap.get(BEHAVES_LIKE)).equals(BEHAVES_LIKE_FIXED)){
           objInterestCalculationBean.setIsDuration_ddmmyy(false);
           objInterestCalculationBean.setInterestType(InterestCalculationConstants.SIMPLE_INTEREST);
           objInterestCalculationBean.setFloatPrecision("0");
       }/**/ else {
            objInterestCalculationBean.setFloatPrecision("8");
       }

        System.out.println("resultMap:" + resultMap);
        objInterestCalculationBean.setRoundingType(roundOff(resultMap));
//        objInterestCalculationBean.setCompoundingType("COMPOUND");
        
        //---Calculate the Maturity Interest...
        HashMap interestMap = new HashMap();
        interestMap.put(CommonConstants.DATA, objInterestCalculationBean);
//        if(resultMap.containsKey("MODE"))
//            interestMap.put("MODE", resultMap.get("MODE"));
        interestMap = CommonCalculateInterest.getInterest(interestMap);
        
        interestOnMaturity = CommonUtil.convertObjToDouble(interestMap.get(InterestCalculationConstants.INTEREST)).doubleValue();
        interestAmt = interestOnMaturity;
        return interestMap;
    }
    
    private HashMap calcFixedInterest(HashMap resultMap) {
        System.out.println("#### FixedresultMap : "+resultMap);  
        double intAmt = 0.0;
        double amt = 0.0;
        double period = 0.0;
        HashMap returnMap = new HashMap();
        InterestCalculation interestCalculation = new InterestCalculation();
        interestCalculation.setPrincipal(CommonUtil.convertObjToDouble(resultMap.get("AMOUNT")).doubleValue());
        interestCalculation.setPeriod(CommonUtil.convertObjToDouble(resultMap.get("PEROID")).doubleValue());
        interestCalculation.setRateOfInterest(CommonUtil.convertObjToDouble(resultMap.get("ROI")).doubleValue());
        interestCalculation.setYearPeriod(YEAR_PERIOD);
        interestCalculation.setInterestType(SIMPLE_INTEREST);
        period = CommonUtil.convertObjToDouble(resultMap.get("PEROID")).doubleValue();

        if(resultMap.get("DISCOUNTED_RATE")!=null && resultMap.get("DISCOUNTED_RATE").equals("Y") && 
        resultMap.get("INTEREST_TYPE")!= null && resultMap.get("INTEREST_TYPE").equals("MONTHLY") && period >29 ){            
            double roi = 0.0; 
            double amount = CommonUtil.convertObjToDouble(resultMap.get("AMOUNT")).doubleValue();
            period = CommonUtil.convertObjToDouble(resultMap.get("PEROID")).doubleValue();
            roi = CommonUtil.convertObjToDouble(resultMap.get("ROI")).doubleValue();
            period = period /30;
            intAmt = roi/4 / (Math.pow((1+(roi/1200)),2) + (1+(roi/1200)) +1);
            System.out.println("intAmt: "+intAmt);
            double calcAmt = amount /100;
            intAmt = intAmt*calcAmt;
            intAmt = (double)getNearest((long)(intAmt *100),100)/100;                    
            intAmt = intAmt * period;
            System.out.println("rate: "+intAmt);
            returnMap.put("INTEREST",new Double(intAmt));
            returnMap.put("AMOUNT",new Double(amount));
            return returnMap;
        }else if(resultMap.get("DISCOUNTED_RATE") != null && resultMap.containsKey("CALC_OPENING_MODE") && period <365){
            interestCalculation.calculateInterest();            
        }
          //commented by rishad 05/04/2016 mantis id:   issue assigned by jithesh and soji
//        else if(resultMap.get("DISCOUNTED_RATE")!=null && resultMap.get("DISCOUNTED_RATE").equals("Y") && period <365){
//            interestCalculation.calculateInterest();
//        }
        else{
            double principal =0.0;
            double rateOfInterest =0.0;
            double years = CommonUtil.convertObjToDouble(resultMap.get("PERIOD_YEARS")).doubleValue();
            double months = CommonUtil.convertObjToDouble(resultMap.get("PERIOD_MONTHS")).doubleValue();
            principal = CommonUtil.convertObjToDouble(resultMap.get("AMOUNT")).doubleValue();
            rateOfInterest = CommonUtil.convertObjToDouble(resultMap.get("ROI")).doubleValue();
            years = years * 12;
            double total = years + months;
            double greateramount = principal+(principal * rateOfInterest * total /1200);
            double interestgreater = greateramount - principal;
            double days = CommonUtil.convertObjToDouble(resultMap.get("PERIOD_DAYS")).doubleValue();
            double lessamount = principal+(principal * rateOfInterest * days /36500);
            double interestless = lessamount - principal;
            intAmt = interestgreater + interestless;
            returnMap.put("INTEREST",new Double(intAmt));
            returnMap.put("AMOUNT",new Double(principal));
            return returnMap;
        }
        intAmt = (double)getNearest((long)(interestCalculation.getInterest() *100),100)/100;        
//        intAmt = (double)roundOffLower((long)(interestCalculation.getInterest()*100),5)/100;
        returnMap.put("INTEREST",new Double(intAmt));
        amt = (double)getNearest((long)(interestCalculation.getAmount() *100),100)/100;
//        amt = (double)roundOffLower((long)(interestCalculation.getAmount()*100),5)/100;
        returnMap.put("AMOUNT",new Double(amt));
        System.out.println("#### FixedreturnMap : "+returnMap);
        return returnMap;
    }

    private HashMap calcCummulativeInterest(HashMap resultMap) {
        InterestCalculation interestCalculation = new InterestCalculation();
        interestCalculation.setPrincipal(CommonUtil.convertObjToDouble(resultMap.get("AMOUNT")).doubleValue());
        double period = CommonUtil.convertObjToDouble(resultMap.get("PEROID")).doubleValue();
//        double rateOfInterest = CommonUtil.convertObjToDouble(resultMap.get("ROI")).doubleValue();
        interestCalculation.setRateOfInterest(CommonUtil.convertObjToDouble(resultMap.get("ROI")).doubleValue());
//        rateOfInterest = rateOfInterest/12;
        period = period/30;
        period = (double)roundOffLower((long)(period*100),100)/100;
//        period  = (double)getNearest((long)(period *100),100)/100;
        interestCalculation.setPeriod(period);
//        interestCalculation.setRateOfInterest(rateOfInterest);
        interestCalculation.setYearPeriod(YEAR_PERIOD);
        interestCalculation.setInterestType(COMPOUND_INTEREST);
        interestCalculation.setCompoundingPeriod("DAILY1");
        interestCalculation.setCompoundingType("COMPOUND");
        interestCalculation.calculateInterest();
        HashMap returnMap = new HashMap();
        System.out.println("interestCalculation.getInterest() ==="+interestCalculation.getInterest());
        double intAmt = 0.0;
        if(resultMap.get("LOWER_VALUE")!=null && resultMap.get("LOWER_VALUE").equals("LOWER_VALUE")){
         intAmt = (double)roundOffLower((long)(interestCalculation.getInterest()*100),100)/100;//added by vivek for lower value
        }else{
          intAmt = (double)getNearest((long)(interestCalculation.getInterest()*100),100)/100;//babu on 7-May-2013  
        }
         System.out.println("intAmt ==="+intAmt);
        returnMap.put("INTEREST",new Double(intAmt));
        double amt = 0.0;
        if(resultMap.get("LOWER_VALUE")!=null && resultMap.get("LOWER_VALUE").equals("LOWER_VALUE")){
         amt = (double)roundOffLower((long)(interestCalculation.getAmount()*100),100)/100;//added by vivek for lower value
        }else if(resultMap.get("HIGHER_VALUE")!=null && resultMap.get("HIGHER_VALUE").equals("HIGHER_VALUE")){
         amt = (double)higher((long)(interestCalculation.getAmount()*100),100)/100;//added by vivek for lower value
        }else if(resultMap.get("NEAREST_VALUE")!=null && resultMap.get("NEAREST_VALUE").equals("NEAREST_VALUE")){
          amt = (double)getNearest((long)(interestCalculation.getAmount()*100),100)/100;//babu on 7-May-2013  
        }else{
            amt = interestCalculation.getAmount();
            System.out.println("interestCalculation.getAmount()111111>>>>>>"+interestCalculation.getAmount());
        }
        System.out.println("interestCalculation.getAmount()222222>>>>"+interestCalculation.getAmount());
        returnMap.put("AMOUNT",new Double(amt));
        System.out.println("#### CummulativereturnMap : "+returnMap);
        return returnMap;
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
    
    private HashMap calcRecurringInterest(HashMap resultMap) {
        System.out.println("#####resultMap : "+resultMap);
        InterestCalculation interestCalculation = new InterestCalculation();
        interestCalculation.setPrincipal(CommonUtil.convertObjToDouble(resultMap.get("AMOUNT")).doubleValue());
        double period = CommonUtil.convertObjToDouble(resultMap.get("PEROID")).doubleValue();
        double rateOfInterest = CommonUtil.convertObjToDouble(resultMap.get("ROI")).doubleValue();
        rateOfInterest = rateOfInterest/12;
        interestCalculation.setPeriod(period);
        interestCalculation.setRateOfInterest(rateOfInterest);
        interestCalculation.setYearPeriod(YEAR_PERIOD);
        interestCalculation.setInterestType(COMPOUND_INTEREST);
        interestCalculation.setCompoundingPeriod("DAILY1");
        interestCalculation.setCompoundingType("PERIODIC_DEPOSITIBA");
        if(resultMap!=null && resultMap.containsKey("WEEKLY_DEP")){
         interestCalculation.setRec_priod(7);  
        }
        interestCalculation.calculateInterest();
        HashMap returnMap = new HashMap();
        double intAmt = (double)roundOffLower((long)(interestCalculation.getInterest()*100),5)/100;
        returnMap.put("INTEREST",new Double(intAmt));
        double amt = (double)roundOffLower((long)(interestCalculation.getAmount()*100),5)/100;
        returnMap.put("AMOUNT",new Double(amt));
        System.out.println("#### RecurringreturnMap : "+returnMap);
        return returnMap;
    }

    public long roundOffLower(long number,long roundingFactor) {
        long mod = number%roundingFactor;
        return number-mod;
    }    

    private HashMap calcDailyInterest(HashMap resultMap) {
        InterestCalculation interestCalculation = new InterestCalculation();
        double amount = CommonUtil.convertObjToDouble(resultMap.get("AMOUNT")).doubleValue();
        interestCalculation.setPrincipal(CommonUtil.convertObjToDouble(resultMap.get("AMOUNT")).doubleValue());
        interestCalculation.setPeriod(CommonUtil.convertObjToDouble(resultMap.get("PEROID")).doubleValue());
        interestCalculation.setRateOfInterest(CommonUtil.convertObjToDouble(resultMap.get("ROI")).doubleValue());
        interestCalculation.setYearPeriod(YEAR_PERIOD);
        interestCalculation.setInterestType(SIMPLE_INTEREST);
        interestCalculation.calculateInterest();
        HashMap returnMap = new HashMap();
        double intAmt = (double)roundOffLower((long)(interestCalculation.getInterest()*100),5)/100;
        returnMap.put("INTEREST",new Double(intAmt));
        double amt = (double)roundOffLower((long)(interestCalculation.getAmount()*100),5)/100;
        returnMap.put("AMOUNT",new Double(amt));
        System.out.println("#### DailyreturnMap : "+returnMap);
        return returnMap;
    }
    
    public static void main (String st[]) {
        HashMap resultMap = new HashMap();
        resultMap.put("AMOUNT", "1000");
        resultMap.put("INT_TYPE", "SIMPLE");
        resultMap.put("ROI", "5");
        resultMap.put("ROUND_OFF", "NEAREST_VALUE");
        resultMap.put("COMP_FREQ", "90");
        resultMap.put("BEHAVES_LIKE", "FIXED");
        resultMap.put("PERIOD_DAYS", "0");
        resultMap.put("PERIOD_MONTHS", "0");
        resultMap.put("PERIOD_YEARS", "1");
        
        resultMap.put("DEPOSIT_DT", DateUtil.getDateMMDDYYYY("3/4/2003"));
        resultMap.put("MATURITY_DT", DateUtil.getDateMMDDYYYY("3/4/2004"));
        
        InterestCalc intCalc = new InterestCalc();
        System.out.println("calc:" + intCalc.calcAmtDetails(resultMap));
    }
//    
//    private String setRateOfInterst(HashMap resultMap) {
//        HashMap map = new HashMap();
//        map.put(PROD_ID, resultMap.get(PROD_ID));
//        map.put(PRODUCT_TYPE, PRODUCT_TYPE_VALUE);
//        map.put(CATEGORY_ID, resultMap.get(CATEGORY_ID));
//        map.put(PRINCIPLE_AMT, resultMap.get(PRINCIPLE_AMT));
//        map.put(PERIOD, CommonUtil.convertObjToStr(resultMap.get(PERIOD)));
//        
//        //---Deposit date is Present inCase of the Deposit accounts...
//        if (!resultMap.containsKey(DEPOSIT_DT)) {
//            map.put(DEPOSIT_DT, ServerUtil.getCurrentDate());
//        }else{
//            map.put(DEPOSIT_DT, (Date)resultMap.get(DEPOSIT_DT));
//       }
//        
//        String roi = "";
//        final HashMap dataMap = getInterestRates(map);
//        if(dataMap  != null){
//            System.out.println("dataMap: " + dataMap);
//            roi  = CommonUtil.convertObjToStr(((HashMap)dataMap).get(ROI));
//        }
//        System.out.println("roi: " + roi);
//        return roi ;
//    }
//        
//    //---To get the Compound Freq Period, if the rate Type is Compounded...
//    private String isCompounded(HashMap resultMap){
//        /** If the Interest Calculation is Compounded...
//         */
//        String compoundingPeriod = null;
//        if(CommonUtil.convertObjToStr(resultMap.get("COMP_TYPE")).equalsIgnoreCase("Y")){
//            compoundingPeriod = InterestCalculationConstants.COMPOUND_INTEREST ;
//            if(CommonUtil.convertObjToInt(resultMap.get("COMP_FREQ")) == 365){
//                compoundingPeriod = InterestCalculationConstants.COMP_ANNUALLY ;
//            }else if(CommonUtil.convertObjToInt(resultMap.get("COMP_FREQ")) == 180){
//                compoundingPeriod = InterestCalculationConstants.COMP_SEMIANNUALLY ;
//            }else if(CommonUtil.convertObjToInt(resultMap.get("COMP_FREQ")) == 90){
//                compoundingPeriod = InterestCalculationConstants.COMP_QUARTERLY ;
//            }else if(CommonUtil.convertObjToInt(resultMap.get("COMP_FREQ")) == 30){
//                compoundingPeriod = InterestCalculationConstants.COMP_MONTHLY ;
//            }else if(CommonUtil.convertObjToInt(resultMap.get("COMP_FREQ")) == 7){
//                compoundingPeriod = InterestCalculationConstants.COMP_WEEKLY ;
//            }
//            compoundingPeriod = InterestCalculationConstants.COMPTYPE_COMPOUND ;
//            
//        }else{ //---Default interest type is Simple Interest...
//            compoundingPeriod = InterestCalculationConstants.SIMPLE_INTEREST ;
//            
//        }
//        return compoundingPeriod ;
//    }
    
    //---Method to Insert the record into the Table...
    private String roundOff(HashMap resultMap){
        /** Check for the Rounding Off Criteria
         */
        String roundOffVal = null ;
        if(CommonUtil.convertObjToStr(resultMap.get(ROUND_OFF)).equalsIgnoreCase(ROUND_OFF_HIGHER_VALUE)){
            roundOffVal = InterestCalculationConstants.ROUNDING_HIGHER ;
        } else if(CommonUtil.convertObjToStr(resultMap.get(ROUND_OFF)).equalsIgnoreCase(ROUND_OFF_LOWER_VALUE)){
            roundOffVal = InterestCalculationConstants.ROUNDING_LOWER ;
        } else if(CommonUtil.convertObjToStr(resultMap.get(ROUND_OFF)).equalsIgnoreCase(ROUND_OFF_NEAREST_VALUE)){
            roundOffVal = InterestCalculationConstants.ROUNDING_NEAREST ;
        }
        
        return roundOffVal;
    }
    
//        
//    public int getPENAL() {
//        return PENAL;
//    }
//    
//    public void setPENAL(int PENAL) {
//        this.PENAL = PENAL;
//    }
//    
//    //---To get the Rate of Interest...
//    public HashMap getInterestRates(HashMap inputMap){
//        inputMap.put(PRODUCT_TYPE, "TD");
//        List dataList = (List)ClientUtil.executeQuery("icm.getInterestRates", inputMap);
//        HashMap dataMap = new HashMap();
//        if(dataList.size()>0){
//        dataMap =  (HashMap) dataList.get(0);
//        }
//        return dataMap;
//    }
//    
//    //---To get the ProdDetails...
//    private HashMap getProdDetials(HashMap inputMap){
//        HashMap outputMap = new HashMap();
//        List list = (List) ClientUtil.executeQuery("getDepProdIntPay", inputMap);
//        if(list.size()>0){
//        outputMap.putAll((HashMap)list.get(0));
//        }
//        return outputMap;
//    }
}
