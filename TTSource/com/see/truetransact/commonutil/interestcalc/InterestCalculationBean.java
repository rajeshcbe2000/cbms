/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition. 
 * 
 * InterestCalculationBean.java
 *
 * Created on August 26, 2004, 9:37 AM
 */

package com.see.truetransact.commonutil.interestcalc;

/**
 *
 * @author  Pinky
 */
import java.util.Date;

public class InterestCalculationBean {
    
    private String principalAmt;
    private String rateOfInterest;
    
    private String duration_dd;
    private String duration_mm;
    private String duration_yy;
    private String duration_FromDate;
    private String duration_ToDate;
    private Date duration_FromDt;
    private Date duration_ToDt;
    
    private String yearOption;
    private String monthOption;
    
    private String interestType;
    private String compoundingPeriod;
    private String compoundingType;
    private String roundingFactor;
    private String roundingType;
    private String floatPrecision;
    private String noLoanRoundeInt;
    private boolean isDuration_ddmmyy;
    private boolean isRepaymentSchedule;
    /** Creates a new instance of InterestCalculationBean */
    public InterestCalculationBean() {
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
        compoundingPeriod = String.valueOf((new Double(compoundingPeriod)).intValue());
        
        if(compoundingPeriod.equals("90"))
            compoundingPeriod = InterestCalculationConstants.COMP_QUARTERLY ;
        else if(compoundingPeriod.equals("180"))
            compoundingPeriod = InterestCalculationConstants.COMP_SEMIANNUALLY ;
        else if(compoundingPeriod.equals("365"))
            compoundingPeriod = InterestCalculationConstants.COMP_ANNUALLY ;
        else if(compoundingPeriod.equals("30"))
            compoundingPeriod = InterestCalculationConstants.COMP_MONTHLY ;
        else if(compoundingPeriod.equals("15"))
            compoundingPeriod = InterestCalculationConstants.COMP_BIMONTHLY ;
        
        this.compoundingPeriod = compoundingPeriod;
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
    
    /** Getter for property floatPrecision.
     * @return Value of property floatPrecision.
     *
     */
    public java.lang.String getFloatPrecision() {
        return floatPrecision;
    }
    
    /** Setter for property floatPrecision.
     * @param floatPrecision New value of property floatPrecision.
     *
     */
    public void setFloatPrecision(java.lang.String floatPrecision) {
        this.floatPrecision = floatPrecision;
    }
    
    /** Getter for property interestType.
     * @return Value of property interestType.
     *
     */
    public java.lang.String getInterestType() {
        return interestType;
    }
    
    /** Setter for property interestType.
     * @param interestType New value of property interestType.
     *
     */
    public void setInterestType(java.lang.String interestType) {
        this.interestType = interestType;
    }
    
    /** Getter for property monthOption.
     * @return Value of property monthOption.
     *
     */
    public java.lang.String getMonthOption() {
        return monthOption;
    }
    
    /** Setter for property monthOption.
     * @param monthOption New value of property monthOption.
     *
     */
    public void setMonthOption(java.lang.String monthOption) {
        this.monthOption = monthOption;
    }
    
    /** Getter for property principalAmt.
     * @return Value of property principalAmt.
     *
     */
    public java.lang.String getPrincipalAmt() {
        return principalAmt;
    }
    
    /** Setter for property principalAmt.
     * @param principalAmt New value of property principalAmt.
     *
     */
    public void setPrincipalAmt(java.lang.String principalAmt) {
        this.principalAmt = principalAmt;
    }
    
    /** Getter for property rateOfInterest.
     * @return Value of property rateOfInterest.
     *
     */
    public java.lang.String getRateOfInterest() {
        return rateOfInterest;
    }
    
    /** Setter for property rateOfInterest.
     * @param rateOfInterest New value of property rateOfInterest.
     *
     */
    public void setRateOfInterest(java.lang.String rateOfInterest) {
        this.rateOfInterest = rateOfInterest;
    }
    
    /** Getter for property roundingFactor.
     * @return Value of property roundingFactor.
     *
     */
    public java.lang.String getRoundingFactor() {
        return roundingFactor;
    }
    
    /** Setter for property roundingFactor.
     * @param roundingFactor New value of property roundingFactor.
     *
     */
    public void setRoundingFactor(java.lang.String roundingFactor) {
        this.roundingFactor = roundingFactor;
    }
    
    /** Getter for property roundingType.
     * @return Value of property roundingType.
     *
     */
    public java.lang.String getRoundingType() {
        return roundingType;
    }
    
    /** Setter for property roundingType.
     * @param roundingType New value of property roundingType.
     *
     */
    public void setRoundingType(java.lang.String roundingType) {
        this.roundingType = roundingType;
    }
    
    /** Getter for property yearOption.
     * @return Value of property yearOption.
     *
     */
    public java.lang.String getYearOption() {
        return yearOption;
    }
    
    /** Setter for property yearOption.
     * @param yearOption New value of property yearOption.
     *
     */
    public void setYearOption(java.lang.String yearOption) {
        if (yearOption.equals(InterestCalculationConstants.YEAR_OPTION_ACTUAL)) {
            this.yearOption = yearOption;
        } else {
            this.yearOption = String.valueOf((new Double(yearOption)).intValue());
        }
    }
    
    /** Getter for property duration_dd.
     * @return Value of property duration_dd.
     *
     */
    public java.lang.String getDuration_dd() {
        return duration_dd;
    }
    
    /** Setter for property duration_dd.
     * @param duration_dd New value of property duration_dd.
     *
     */
    public void setDuration_dd(java.lang.String duration_dd) {
        this.duration_dd = duration_dd;
    }
    
    /** Getter for property duration_FromDate.
     * @return Value of property duration_FromDate.
     *
     */
    public java.lang.String getDuration_FromDate() {
        return duration_FromDate;
    }
    
    /** Setter for property duration_FromDate.
     * @param duration_FromDate New value of property duration_FromDate.
     *
     */
    public void setDuration_FromDate(java.lang.String duration_FromDate) {
        this.duration_FromDate = duration_FromDate;
    }
    
    /** Getter for property duration_mm.
     * @return Value of property duration_mm.
     *
     */
    public java.lang.String getDuration_mm() {
        return duration_mm;
    }
    
    /** Setter for property duration_mm.
     * @param duration_mm New value of property duration_mm.
     *
     */
    public void setDuration_mm(java.lang.String duration_mm) {
        this.duration_mm = duration_mm;
    }
    
    /** Getter for property duration_ToDate.
     * @return Value of property duration_ToDate.
     *
     */
    public java.lang.String getDuration_ToDate() {
        return duration_ToDate;
    }
    
    /** Setter for property duration_ToDate.
     * @param duration_ToDate New value of property duration_ToDate.
     *
     */
    public void setDuration_ToDate(java.lang.String duration_ToDate) {
        this.duration_ToDate = duration_ToDate;
    }
    
    /** Getter for property duration_yy.
     * @return Value of property duration_yy.
     *
     */
    public java.lang.String getDuration_yy() {
        return duration_yy;
    }
    
    /** Setter for property duration_yy.
     * @param duration_yy New value of property duration_yy.
     *
     */
    public void setDuration_yy(java.lang.String duration_yy) {
        this.duration_yy = duration_yy;
    }
    
    /** Getter for property isDuration_ddmmyy.
     * @return Value of property isDuration_ddmmyy.
     *
     */
    public boolean isIsDuration_ddmmyy() {
        return isDuration_ddmmyy;
    }
    
    /** Setter for property isDuration_ddmmyy.
     * @param isDuration_ddmmyy New value of property isDuration_ddmmyy.
     *
     */
    public void setIsDuration_ddmmyy(boolean isDuration_ddmmyy) {
        this.isDuration_ddmmyy = isDuration_ddmmyy;
    }
    
    public String toString() {
        StringBuffer strB = new StringBuffer();
        strB.append("	principalAmt	" + 	principalAmt	);
        strB.append("\n	rateOfInterest	" + 	rateOfInterest	);
        strB.append("\n	duration_dd	" + 	duration_dd	);
        strB.append("\n	duration_mm	" + 	duration_mm	);
        strB.append("\n	duration_yy	" + 	duration_yy	);
        strB.append("\n	duration_FromDate	" + 	duration_FromDate	);
        strB.append("\n	duration_ToDate	" + 	duration_ToDate	);
        strB.append("\n	duration_FromDt	" + 	duration_FromDt	);
        strB.append("\n	duration_ToDt	" + 	duration_ToDt	);
        strB.append("\n	yearOption	" + 	yearOption	);
        strB.append("\n	monthOption	" + 	monthOption	);
        strB.append("\n	interestType	" + 	interestType	);
        strB.append("\n	compoundingPeriod	" + 	compoundingPeriod	);
        strB.append("\n	compoundingType	" + 	compoundingType	);
        strB.append("\n	roundingFactor	" + 	roundingFactor	);
        strB.append("\n	roundingType	" + 	roundingType	);
        strB.append("\n	floatPrecision	" + 	floatPrecision	);
        strB.append("\n	isDuration_ddmmyy	" + 	String.valueOf(isDuration_ddmmyy)	);
         strB.append("\n	noLoanRoundeInt	" + 	noLoanRoundeInt);
        strB.append("\n");
        return strB.toString();
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
    
    /**
     * Getter for property duration_FromDt.
     * @return Value of property duration_FromDt.
     */
    public java.util.Date getDuration_FromDt() {
        return duration_FromDt;
    }
    
    /**
     * Setter for property duration_FromDt.
     * @param duration_FromDt New value of property duration_FromDt.
     */
    public void setDuration_FromDt(java.util.Date duration_FromDt) {
        this.duration_FromDt = duration_FromDt;
    }
    
    /**
     * Getter for property duration_ToDt.
     * @return Value of property duration_ToDt.
     */
    public java.util.Date getDuration_ToDt() {
        return duration_ToDt;
    }
    
    /**
     * Setter for property duration_ToDt.
     * @param duration_ToDt New value of property duration_ToDt.
     */
    public void setDuration_ToDt(java.util.Date duration_ToDt) {
        this.duration_ToDt = duration_ToDt;
    }
    
    /**
     * Getter for property isRepaymentSchedule.
     * @return Value of property isRepaymentSchedule.
     */
    public boolean isIsRepaymentSchedule() {
        return isRepaymentSchedule;
    }
    
    /**
     * Setter for property isRepaymentSchedule.
     * @param isRepaymentSchedule New value of property isRepaymentSchedule.
     */
    public void setIsRepaymentSchedule(boolean isRepaymentSchedule) {
        this.isRepaymentSchedule = isRepaymentSchedule;
    }
    
}
