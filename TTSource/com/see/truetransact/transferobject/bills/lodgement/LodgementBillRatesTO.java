/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition.  
 * 
 * LodgementRemitTO.java
 *
 * Created on September 24, 2008, 11:43 AM
 */
package com.see.truetransact.transferobject.bills.lodgement;

import java.util.Date;
import java.io.Serializable;
import com.see.truetransact.transferobject.TransferObject;

/**
 *
 * @author Administrator
 */
public class LodgementBillRatesTO extends TransferObject implements Serializable {

    private String lodgeID = "";
    private Double transPeriod = null;
    private Double noOfIntDays = null;
    private String atParLimit = "";
    private String status = "";
    private Double discountRateBd = null;
    private Double overdueRateBd = null;
    private Double interestRateCbp = null;
    private Double overdueRateCbp = null;
    private Double postageRate = null;
    private Double rateForDelay = null;
    private String intIcc = "";

//	private String remitProdId = "";
//	private String remitCity = "";
//	private String remitDraweeBank = "";
//	private String remitDraweeBranchCode = "";
//        private Date remitInstDt = null;
//	private String remitFavouring = "";
//        private String remitFavouringIn = "";
//        private String remitInstAmt = "";
//	private String remitStatus = "";
//	private String remitInst1 = "";
//	private String remitInst2 = "";
//	private String lodgeID = "";
//	private String billActivity = "";
//
//	
    public String getKeyData() {
        setKeyColumns(lodgeID);
        return lodgeID;
    }

    /**
     * Getter for property lodgeID.
     *
     * @return Value of property lodgeID.
     */
    public java.lang.String getLodgeID() {
        return lodgeID;
    }

    /**
     * Setter for property lodgeID.
     *
     * @param lodgeID New value of property lodgeID.
     */
    public void setLodgeID(java.lang.String lodgeID) {
        this.lodgeID = lodgeID;
    }

    /**
     * Getter for property noOfIntDays.
     *
     * @return Value of property noOfIntDays.
     */
    public java.lang.Double getNoOfIntDays() {
        return noOfIntDays;
    }

    /**
     * Setter for property noOfIntDays.
     *
     * @param noOfIntDays New value of property noOfIntDays.
     */
    public void setNoOfIntDays(java.lang.Double noOfIntDays) {
        this.noOfIntDays = noOfIntDays;
    }

    /**
     * Getter for property atParLimit.
     *
     * @return Value of property atParLimit.
     */
    public java.lang.String getAtParLimit() {
        return atParLimit;
    }

    /**
     * Setter for property atParLimit.
     *
     * @param atParLimit New value of property atParLimit.
     */
    public void setAtParLimit(java.lang.String atParLimit) {
        this.atParLimit = atParLimit;
    }

    /**
     * Getter for property status.
     *
     * @return Value of property status.
     */
    public java.lang.String getStatus() {
        return status;
    }

    /**
     * Setter for property status.
     *
     * @param status New value of property status.
     */
    public void setStatus(java.lang.String status) {
        this.status = status;
    }

    /**
     * Getter for property discountRateBd.
     *
     * @return Value of property discountRateBd.
     */
    public java.lang.Double getDiscountRateBd() {
        return discountRateBd;
    }

    /**
     * Setter for property discountRateBd.
     *
     * @param discountRateBd New value of property discountRateBd.
     */
    public void setDiscountRateBd(java.lang.Double discountRateBd) {
        this.discountRateBd = discountRateBd;
    }

    /**
     * Getter for property overdueRateBd.
     *
     * @return Value of property overdueRateBd.
     */
    public java.lang.Double getOverdueRateBd() {
        return overdueRateBd;
    }

    /**
     * Setter for property overdueRateBd.
     *
     * @param overdueRateBd New value of property overdueRateBd.
     */
    public void setOverdueRateBd(java.lang.Double overdueRateBd) {
        this.overdueRateBd = overdueRateBd;
    }

    /**
     * Getter for property interestRateCbp.
     *
     * @return Value of property interestRateCbp.
     */
    public java.lang.Double getInterestRateCbp() {
        return interestRateCbp;
    }

    /**
     * Setter for property interestRateCbp.
     *
     * @param interestRateCbp New value of property interestRateCbp.
     */
    public void setInterestRateCbp(java.lang.Double interestRateCbp) {
        this.interestRateCbp = interestRateCbp;
    }

    /**
     * Getter for property overdueRateCbp.
     *
     * @return Value of property overdueRateCbp.
     */
    public java.lang.Double getOverdueRateCbp() {
        return overdueRateCbp;
    }

    /**
     * Setter for property overdueRateCbp.
     *
     * @param overdueRateCbp New value of property overdueRateCbp.
     */
    public void setOverdueRateCbp(java.lang.Double overdueRateCbp) {
        this.overdueRateCbp = overdueRateCbp;
    }

    /**
     * Getter for property postageRate.
     *
     * @return Value of property postageRate.
     */
    public java.lang.Double getPostageRate() {
        return postageRate;
    }

    /**
     * Setter for property postageRate.
     *
     * @param postageRate New value of property postageRate.
     */
    public void setPostageRate(java.lang.Double postageRate) {
        this.postageRate = postageRate;
    }

    /**
     * Getter for property rateForDelay.
     *
     * @return Value of property rateForDelay.
     */
    public java.lang.Double getRateForDelay() {
        return rateForDelay;
    }

    /**
     * Setter for property rateForDelay.
     *
     * @param rateForDelay New value of property rateForDelay.
     */
    public void setRateForDelay(java.lang.Double rateForDelay) {
        this.rateForDelay = rateForDelay;
    }

    /**
     * toString method which returns this TO as a String.
     */
    public String toString() {
        StringBuffer strB = new StringBuffer(getTOStringStart(this.getClass().getName()));
        strB.append(getTOStringKey(getKeyData()));
        strB.append(getTOString("lodgeID", lodgeID));
        strB.append(getTOString("transPeriod", transPeriod));
        strB.append(getTOString("noOfIntDays", noOfIntDays));
        strB.append(getTOString("atParLimit", atParLimit));
        strB.append(getTOString("status", status));
        strB.append(getTOString("discountRateBd", discountRateBd));
        strB.append(getTOString("overdueRateBd", overdueRateBd));
        strB.append(getTOString("interestRateCbp", interestRateCbp));
        strB.append(getTOString("overdueRateCbp", overdueRateCbp));
        strB.append(getTOString("postageRate", postageRate));
        strB.append(getTOString("rateForDelay", rateForDelay));
        strB.append(getTOString("intIcc", intIcc));
//                 strB.append(getTOString("remitFavouringIn", remitFavouringIn));
        strB.append(getTOStringEnd());
        return strB.toString();
    }

    /**
     * toXML method which returns this TO as a XML output.
     */
    public String toXML() {
        StringBuffer strB = new StringBuffer(getTOXmlStart(this.getClass().getName()));
        strB.append(getTOXmlKey(getKeyData()));
        strB.append(getTOXml("lodgeID", lodgeID));
        strB.append(getTOXml("transPeriod", transPeriod));
        strB.append(getTOXml("noOfIntDays", noOfIntDays));
        strB.append(getTOXml("atParLimit", atParLimit));
        strB.append(getTOXml("status", status));
        strB.append(getTOXml("discountRateBd", discountRateBd));
        strB.append(getTOXml("overdueRateBd", overdueRateBd));
        strB.append(getTOXml("interestRateCbp", interestRateCbp));
        strB.append(getTOXml("overdueRateCbp", overdueRateCbp));
        strB.append(getTOXml("postageRate", postageRate));
        strB.append(getTOXml("rateForDelay", rateForDelay));
        strB.append(getTOXml("intIcc", intIcc));
//                strB.append(getTOXml("remitFavouringIn", remitFavouringIn));
        strB.append(getTOXmlEnd());
        return strB.toString();
    }

    /**
     * Getter for property transPeriod.
     *
     * @return Value of property transPeriod.
     */
    public java.lang.Double getTransPeriod() {
        return transPeriod;
    }

    /**
     * Setter for property transPeriod.
     *
     * @param transPeriod New value of property transPeriod.
     */
    public void setTransPeriod(java.lang.Double transPeriod) {
        this.transPeriod = transPeriod;
    }

    /**
     * Getter for property intIcc.
     *
     * @return Value of property intIcc.
     */
    public java.lang.String getIntIcc() {
        return intIcc;
    }

    /**
     * Setter for property intIcc.
     *
     * @param intIcc New value of property intIcc.
     */
    public void setIntIcc(java.lang.String intIcc) {
        this.intIcc = intIcc;
    }
//
//        /**
//         * Getter for property remitProdId.
//         * @return Value of property remitProdId.
//         */
//        public java.lang.String getRemitProdId() {
//            return remitProdId;
//        }        
//      
//        /**
//         * Setter for property remitProdId.
//         * @param remitProdId New value of property remitProdId.
//         */
//        public void setRemitProdId(java.lang.String remitProdId) {
//            this.remitProdId = remitProdId;
//        }        
//        
//        /**
//         * Getter for property remitCity.
//         * @return Value of property remitCity.
//         */
//        public java.lang.String getRemitCity() {
//            return remitCity;
//        }
//        
//        /**
//         * Setter for property remitCity.
//         * @param remitCity New value of property remitCity.
//         */
//        public void setRemitCity(java.lang.String remitCity) {
//            this.remitCity = remitCity;
//        }
//        
//        /**
//         * Getter for property remitDraweeBank.
//         * @return Value of property remitDraweeBank.
//         */
//        public java.lang.String getRemitDraweeBank() {
//            return remitDraweeBank;
//        }
//        
//        /**
//         * Setter for property remitDraweeBank.
//         * @param remitDraweeBank New value of property remitDraweeBank.
//         */
//        public void setRemitDraweeBank(java.lang.String remitDraweeBank) {
//            this.remitDraweeBank = remitDraweeBank;
//        }
//        
//        /**
//         * Getter for property remitDraweeBranchCode.
//         * @return Value of property remitDraweeBranchCode.
//         */
//        public java.lang.String getRemitDraweeBranchCode() {
//            return remitDraweeBranchCode;
//        }
//        
//        /**
//         * Setter for property remitDraweeBranchCode.
//         * @param remitDraweeBranchCode New value of property remitDraweeBranchCode.
//         */
//        public void setRemitDraweeBranchCode(java.lang.String remitDraweeBranchCode) {
//            this.remitDraweeBranchCode = remitDraweeBranchCode;
//        }
//        
//        /**
//         * Getter for property remitFavouring.
//         * @return Value of property remitFavouring.
//         */
//        public java.lang.String getRemitFavouring() {
//            return remitFavouring;
//        }
//        
//        /**
//         * Setter for property remitFavouring.
//         * @param remitFavouring New value of property remitFavouring.
//         */
//        public void setRemitFavouring(java.lang.String remitFavouring) {
//            this.remitFavouring = remitFavouring;
//        }
//        
//        /**
//         * Getter for property remitStatus.
//         * @return Value of property remitStatus.
//         */
//        public java.lang.String getRemitStatus() {
//            return remitStatus;
//        }
//        
//        /**
//         * Setter for property remitStatus.
//         * @param remitStatus New value of property remitStatus.
//         */
//        public void setRemitStatus(java.lang.String remitStatus) {
//            this.remitStatus = remitStatus;
//        }
//        
//        /**
//         * Getter for property remitInst1.
//         * @return Value of property remitInst1.
//         */
//        public java.lang.String getRemitInst1() {
//            return remitInst1;
//        }
//        
//        /**
//         * Setter for property remitInst1.
//         * @param remitInst1 New value of property remitInst1.
//         */
//        public void setRemitInst1(java.lang.String remitInst1) {
//            this.remitInst1 = remitInst1;
//        }
//        
//        /**
//         * Getter for property remitInst2.
//         * @return Value of property remitInst2.
//         */
//        public java.lang.String getRemitInst2() {
//            return remitInst2;
//        }
//        
//        /**
//         * Setter for property remitInst2.
//         * @param remitInst2 New value of property remitInst2.
//         */
//        public void setRemitInst2(java.lang.String remitInst2) {
//            this.remitInst2 = remitInst2;
//        }
//        
//        /**
//         * Getter for property lodgeID.
//         * @return Value of property lodgeID.
//         */
//        public java.lang.String getLodgeID() {
//            return lodgeID;
//        }
//        
//        /**
//         * Setter for property lodgeID.
//         * @param lodgeID New value of property lodgeID.
//         */
//        public void setLodgeID(java.lang.String lodgeID) {
//            this.lodgeID = lodgeID;
//        }
//        
//        /**
//         * Getter for property remitInstAmt.
//         * @return Value of property remitInstAmt.
//         */
//        public java.lang.String getRemitInstAmt() {
//            return remitInstAmt;
//        }        
//      
//        /**
//         * Setter for property remitInstAmt.
//         * @param remitInstAmt New value of property remitInstAmt.
//         */
//        public void setRemitInstAmt(java.lang.String remitInstAmt) {
//            this.remitInstAmt = remitInstAmt;
//        }        
//        
//        /**
//         * Getter for property remitInstDt.
//         * @return Value of property remitInstDt.
//         */
//        public java.util.Date getRemitInstDt() {
//            return remitInstDt;
//        }
//        
//        /**
//         * Setter for property remitInstDt.
//         * @param remitInstDt New value of property remitInstDt.
//         */
//        public void setRemitInstDt(java.util.Date remitInstDt) {
//            this.remitInstDt = remitInstDt;
//        }
//        
//        /**
//         * Getter for property billActivity.
//         * @return Value of property billActivity.
//         */
//        public java.lang.String getBillActivity() {
//            return billActivity;
//        }
//        
//        /**
//         * Setter for property billActivity.
//         * @param billActivity New value of property billActivity.
//         */
//        public void setBillActivity(java.lang.String billActivity) {
//            this.billActivity = billActivity;
//        }
//        
//        /**
//         * Getter for property remitFavouringIn.
//         * @return Value of property remitFavouringIn.
//         */
//        public java.lang.String getRemitFavouringIn() {
//            return remitFavouringIn;
//        }
//        
//        /**
//         * Setter for property remitFavouringIn.
//         * @param remitFavouringIn New value of property remitFavouringIn.
//         */
//        public void setRemitFavouringIn(java.lang.String remitFavouringIn) {
//            this.remitFavouringIn = remitFavouringIn;
//        }
}
