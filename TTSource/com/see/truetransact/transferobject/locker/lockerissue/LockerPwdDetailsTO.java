/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition.  
 * 
 * LockerPwdDetailsTO.java
 *
 * Created on May 25, 2010, 6:04 PM
 */
package com.see.truetransact.transferobject.locker.lockerissue;

import java.util.Date;
import java.io.Serializable;
import com.see.truetransact.transferobject.TransferObject;

/**
 *
 * @author Administrator
 */
public class LockerPwdDetailsTO extends TransferObject implements Serializable {

    private String prodID = "";
    private String custID = "";
    private String locNum = "";
    private String pwd = "";
    private String status = "";
    private Date statusDt = null;
    private String branID = "";
    private String remarks = "";
//        private String chargeType = "";
//        private Date fromDate = null;
//        private Date toDate = null;
//        private Double commision = null;
//        private Double serviceTax = null;

    /**
     * Getter for property prodID.
     *
     * @return Value of property prodID.
     */
    public java.lang.String getProdID() {
        return prodID;
    }

    /**
     * Setter for property prodID.
     *
     * @param prodID New value of property prodID.
     */
    public void setProdID(java.lang.String prodID) {
        this.prodID = prodID;
    }

    /**
     * Getter for property chargeType.
     *
     * @return Value of property chargeType.
     */
//        public java.lang.String getChargeType() {
//            return chargeType;
//        }
//        
//        /**
//         * Setter for property chargeType.
//         * @param chargeType New value of property chargeType.
//         */
//        public void setChargeType(java.lang.String chargeType) {
//            this.chargeType = chargeType;
//        }
    /**
     * Getter for property fromDate.
     *
     * @return Value of property fromDate.
     */
//        public java.util.Date getFromDate() {
//            return fromDate;
//        }
//        
//        /**
//         * Setter for property fromDate.
//         * @param fromDate New value of property fromDate.
//         */
//        public void setFromDate(java.util.Date fromDate) {
//            this.fromDate = fromDate;
//        }
    /**
     * Getter for property toDate.
     *
     * @return Value of property toDate.
     */
//        public java.util.Date getToDate() {
//            return toDate;
//        }
//        
//        /**
//         * Setter for property toDate.
//         * @param toDate New value of property toDate.
//         */
//        public void setToDate(java.util.Date toDate) {
//            this.toDate = toDate;
//        }
    /**
     * Getter for property commision.
     *
     * @return Value of property commision.
     */
//        public java.lang.Double getCommision() {
//            return commision;
//        }
//        
//        /**
//         * Setter for property commision.
//         * @param commision New value of property commision.
//         */
//        public void setCommision(java.lang.Double commision) {
//            this.commision = commision;
//        }
    /**
     * Getter for property serviceTax.
     *
     * @return Value of property serviceTax.
     */
//        public java.lang.Double getServiceTax() {
//            return serviceTax;
//        }
//        
//        /**
//         * Setter for property serviceTax.
//         * @param serviceTax New value of property serviceTax.
//         */
//        public void setServiceTax(java.lang.Double serviceTax) {
//            this.serviceTax = serviceTax;
//        }
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
     * Getter for property statusDt.
     *
     * @return Value of property statusDt.
     */
    public java.util.Date getStatusDt() {
        return statusDt;
    }

    /**
     * Setter for property statusDt.
     *
     * @param statusDt New value of property statusDt.
     */
    public void setStatusDt(java.util.Date statusDt) {
        this.statusDt = statusDt;
    }

    /**
     * Getter for property custID.
     *
     * @return Value of property custID.
     */
    public java.lang.String getCustID() {
        return custID;
    }

    /**
     * Setter for property custID.
     *
     * @param custID New value of property custID.
     */
    public void setCustID(java.lang.String custID) {
        this.custID = custID;
    }

    /**
     * Getter for property locNum.
     *
     * @return Value of property locNum.
     */
    public java.lang.String getLocNum() {
        return locNum;
    }

    /**
     * Setter for property locNum.
     *
     * @param locNum New value of property locNum.
     */
    public void setLocNum(java.lang.String locNum) {
        this.locNum = locNum;
    }

    /**
     * Getter for property branID.
     *
     * @return Value of property branID.
     */
    public java.lang.String getBranID() {
        return branID;
    }

    /**
     * Setter for property branID.
     *
     * @param branID New value of property branID.
     */
    public void setBranID(java.lang.String branID) {
        this.branID = branID;
    }

    public String getKeyData() {
        setKeyColumns("locNum");
        return locNum;
    }

    /**
     * toString method which returns this TO as a String.
     */
    public String toString() {
        StringBuffer strB = new StringBuffer(getTOStringStart(this.getClass().getName()));
        strB.append(getTOStringKey(getKeyData()));
        strB.append(getTOString("locNum", locNum));
        strB.append(getTOString("prodID", prodID));
//		strB.append(getTOString("chargeType", chargeType));
//		strB.append(getTOString("fromDate", fromDate));
//		strB.append(getTOString("toDate", toDate));
//		strB.append(getTOString("commision", commision));
//		strB.append(getTOString("serviceTax", serviceTax));
        strB.append(getTOString("status", status));
//		strB.append(getTOString("todLimit", todLimit));
//		strB.append(getTOString("groupCodeId", groupCodeId));
        strB.append(getTOString("statusDt", statusDt));
//		strB.append(getTOString("prevActNum", prevActNum));
//		strB.append(getTOString("clearBalance", clearBalance));
//		strB.append(getTOString("unclearBalance", unclearBalance));
//		strB.append(getTOString("floatBalance", floatBalance));
//		strB.append(getTOString("effectiveBalance", effectiveBalance));
//		strB.append(getTOString("availableBalance", availableBalance));
        strB.append(getTOString("custID", custID));
//		strB.append(getTOString("locNum", locNum));
        strB.append(getTOString("branID", branID));
//		strB.append(getTOString("closedBy", closedBy));
//		strB.append(getTOString("closedDt", closedDt));
//		strB.append(getTOString("baseCurr", baseCurr));
//		strB.append(getTOString("lastTransDt", lastTransDt));
//		strB.append(getTOString("shadowCredit", shadowCredit));
//		strB.append(getTOString("shadowDebit", shadowDebit));
//		strB.append(getTOString("authorizedDt", authorizedDt));
////		strB.append(getTOString("actStatusDt", actStatusDt));
//		strB.append(getTOString("status", status));
//		strB.append(getTOString("statusBy", statusBy));
//		strB.append(getTOString("statusDt", statusDt));
////		strB.append(getTOString("lienAmt", lienAmt));
////		strB.append(getTOString("freezeAmt", freezeAmt));
////		strB.append(getTOString("totalBalance", totalBalance));
//		strB.append(getTOString("commAddrType", commAddrType));
////		strB.append(getTOString("productAmt", productAmt));
//		strB.append(getTOString("categoryId", categoryId));
////		strB.append(getTOString("flexiDepositAmt", flexiDepositAmt));
//		strB.append(getTOString("acctName", acctName));
//		strB.append(getTOString("remarks", remarks));
        strB.append(getTOStringEnd());
        return strB.toString();
    }

    /**
     * toXML method which returns this TO as a XML output.
     */
    public String toXML() {
        StringBuffer strB = new StringBuffer(getTOXmlStart(this.getClass().getName()));
        strB.append(getTOXmlKey(getKeyData()));
        strB.append(getTOXml("locNum", locNum));
        strB.append(getTOXml("prodID", prodID));
        strB.append(getTOXml("pwd", pwd));
//		strB.append(getTOXml("fromDate", fromDate));
//		strB.append(getTOXml("toDate", toDate));
//		strB.append(getTOXml("commision", commision));
//		strB.append(getTOXml("serviceTax", serviceTax));
        strB.append(getTOXml("status", status));
//		strB.append(getTOXml("todLimit", todLimit));
//		strB.append(getTOXml("groupCodeId", groupCodeId));
        strB.append(getTOXml("statusDt", statusDt));
//		strB.append(getTOXml("prevActNum", prevActNum));
//		strB.append(getTOXml("clearBalance", clearBalance));
//		strB.append(getTOXml("unclearBalance", unclearBalance));
//		strB.append(getTOXml("floatBalance", floatBalance));
//		strB.append(getTOXml("effectiveBalance", effectiveBalance));
//		strB.append(getTOXml("availableBalance", availableBalance));
        strB.append(getTOXml("custID", custID));
//		strB.append(getTOXml("locNum", locNum));
        strB.append(getTOXml("branID", branID));
//		strB.append(getTOXml("closedBy", closedBy));
//		strB.append(getTOXml("closedDt", closedDt));
//		strB.append(getTOXml("baseCurr", baseCurr));
//		strB.append(getTOXml("lastTransDt", lastTransDt));
//		strB.append(getTOXml("shadowCredit", shadowCredit));
////		strB.append(getTOXml("shadowDebit", shadowDebit));
//		strB.append(getTOXml("authorizedDt", authorizedDt));
////		strB.append(getTOXml("actStatusDt", actStatusDt));
//		strB.append(getTOXml("status", status));
//		strB.append(getTOXml("statusBy", statusBy));
//		strB.append(getTOXml("statusDt", statusDt));
////		strB.append(getTOXml("lienAmt", lienAmt));
////		strB.append(getTOXml("freezeAmt", freezeAmt));
////		strB.append(getTOXml("totalBalance", totalBalance));
//		strB.append(getTOXml("commAddrType", commAddrType));
////		strB.append(getTOXml("productAmt", productAmt));
//		strB.append(getTOXml("categoryId", categoryId));
////		strB.append(getTOXml("flexiDepositAmt", flexiDepositAmt));
//		strB.append(getTOXml("acctName", acctName));
//		strB.append(getTOXml("remarks", remarks));
        strB.append(getTOXmlEnd());
        return strB.toString();
    }

    /**
     * Getter for property pwd.
     *
     * @return Value of property pwd.
     */
    public java.lang.String getPwd() {
        return pwd;
    }

    /**
     * Setter for property pwd.
     *
     * @param pwd New value of property pwd.
     */
    public void setPwd(java.lang.String pwd) {
        this.pwd = pwd;
    }

    /**
     * Getter for property remarks.
     *
     * @return Value of property remarks.
     */
    public java.lang.String getRemarks() {
        return remarks;
    }

    /**
     * Setter for property remarks.
     *
     * @param remarks New value of property remarks.
     */
    public void setRemarks(java.lang.String remarks) {
        this.remarks = remarks;
    }
}
