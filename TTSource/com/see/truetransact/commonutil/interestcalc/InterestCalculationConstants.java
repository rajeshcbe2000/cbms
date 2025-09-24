/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition. 
 * 
 * InterestCalculationConstants.java
 *
 * Created on August 26, 2004, 2:39 PM
 */

package com.see.truetransact.commonutil.interestcalc;

/**
 *
 * @author  Pinky
 */

public interface InterestCalculationConstants {   
    String INTEREST = "INTEREST";
    String AMOUNT = "AMOUNT";
    
    String YEAR_OPTION_360 = "360";
    String YEAR_OPTION_365 = "365";
    String YEAR_OPTION_ACTUAL = "Actual";
    
    String MONTH_OPTION_30 = "30";
    String MONTH_OPTION_30E = "30E";
    String MONTH_OPTION_ACTUAL = "ActualMonth";
    
    String SIMPLE_INTEREST = "SIMPLE";
    String COMPOUND_INTEREST = "COMPOUND";
    
    String ROUNDING_NEAREST = "NEAREST";
    String ROUNDING_LOWER = "LOWER";
    String ROUNDING_HIGHER = "HIGHER";       
    
    String ROUNDING_VALUE_5_PAISE = "5_PAISE";
    String ROUNDING_VALUE_10_PAISE = "10_PAISE";
    String ROUNDING_VALUE_25_PAISE = "25_PAISE";
    String ROUNDING_VALUE_50_PAISE = "50_PAISE";
    String ROUNDING_VALUE_1_RUPEE = "1_RUPEE";
    String ROUNDING_VALUE_5_RUPEES = "5_RUPEES";
    String ROUNDING_VALUE_10_RUPEES = "10_RUPEES";
    String ROUNDING_VALUE_50_RUPEES = "50_RUPEES";
    String ROUNDING_VALUE_100_RUPEES = "100_RUPEES";
    
    String COMP_DAILY = "DAILY";
    String COMP_WEEKLY = "WEEKLY";
    String COMP_FORTNIGHT = "FORTNIGHTLY";
    String COMP_MONTHLY = "MONTHLY";
    String COMP_BIMONTHLY = "BIMONTHLY";
    String COMP_QUARTERLY = "QUARTERLY";
    String COMP_SEMIANNUALLY = "SEMIANNUALLY";
    String COMP_ANNUALLY="ANNUALLY";   
    
    String COMPTYPE_COMPOUND = "COMPOUND";
    String COMPTYPE_PRESENT_WORTH = "PRESENT_WORTH";
    String COMPTYPE_PERIODIC_DEPOSIT = "PERIODIC_DEPOSIT";
    String COMPTYPE_PRESENT_WORTH_PER_PERIOD = "PRESENT_WORTH_PER_PERIOD";
    String COMPTYPE_SINKING_FUND = "SINKING_FUND";
    String COMPTYPE_PARTIAL_PAYMENT = "PARTIAL_PAYMENT";    

}
