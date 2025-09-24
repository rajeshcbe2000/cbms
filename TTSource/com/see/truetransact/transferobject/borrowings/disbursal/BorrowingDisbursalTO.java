/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition.
 * 
 *
 * TokenConfigTO.java
 * 
 * Created on Thu Jan 20 16:44:08 IST 2005
 */
package com.see.truetransact.transferobject.borrowings.disbursal;

import java.util.Date;
import java.io.Serializable;
import com.see.truetransact.transferobject.TransferObject;

/**
 * Table name for this TO is TOKEN_CONFIG.
 */
public class BorrowingDisbursalTO extends TransferObject implements Serializable {

    private String disbursalNo = "";
    private String borrowingrefNo = "";
    private String agencyCode = "";
    private String type = null;
    private String description = null;
    private Date sanctionDate = null;
    private Double sanctionAmt = null;
    private Double rateofInt = null;
    private Double noofInstallments = null;
    private String prinRepFrq = null;
    private String intRepFrq = "";
    private String morotorium = "";
    private Date sanctionExpDate = null;
    private String borrowingNo = "";
    private Double amtBorrowed = null;
    //added by Anju 15/5/14
    private String createdBy = "";
    /*
     * private String modeTrans = null; private String transAccDetails = "";
     * private String productId = ""; private String accNo = null; private
     * String accName = null;
     */
    private String authorizeStatus = null;
    private String authorizeBy = null;
    private Date authorizeDte = null;
    private String status = null;
    private Double amtBorrowedMaster = 0.0;
    private Double avalbalBorrowedMaster = 0.0;
    private String multiDis = "";

    /**
     * getKeyData returns the Primary Key Columns for this TO User needs to add
     * the Key columns as a setter Example : setKeyColumns("col1" +
     * KEY_VAL_SEPARATOR + "col2"); return col1 + KEY_VAL_SEPARATOR + col2;
     */
    public String getKeyData() {
        setKeyColumns(disbursalNo);
        return disbursalNo;
    }

    /**
     * toString method which returns this TO as a String.
     */
    public String toString() {
        StringBuffer strB = new StringBuffer(getTOStringStart(this.getClass().getName()));
        strB.append(getTOStringKey(getKeyData()));
        strB.append(getTOString("disbursalNo", disbursalNo));
        strB.append(getTOString("borrowingrefNo", borrowingrefNo));
        strB.append(getTOString("borrowingNo", borrowingNo));
        strB.append(getTOString("amtBorrowed", amtBorrowed));
        /*
         * strB.append(getTOString("modeTrans", modeTrans));
         * strB.append(getTOString("transAccDetails", transAccDetails));
         * strB.append(getTOString("productId", productId));
         * strB.append(getTOString("accNo", accNo));
         * strB.append(getTOString("accName", accName));
         */

        strB.append(getTOString("agencyCode", agencyCode));
        strB.append(getTOString("type", type));
        strB.append(getTOString("description", description));
        strB.append(getTOString("sanctionDate", sanctionDate));
        strB.append(getTOString("sanctionAmt", sanctionAmt));
        strB.append(getTOString("rateofInt", rateofInt));
        strB.append(getTOString("noofInstallments", noofInstallments));
        strB.append(getTOString("prinRepFrq", prinRepFrq));
        strB.append(getTOString("intRepFrq", intRepFrq));
        strB.append(getTOString("morotorium", morotorium));
        strB.append(getTOString("sanctionExpDate", sanctionExpDate));
        strB.append(getTOString("createdBy", createdBy));
        strB.append(getTOString("authorizeStatus", authorizeStatus));
        strB.append(getTOString("authorizeBy", authorizeBy));
        strB.append(getTOString("authorizeDte", authorizeDte));
        strB.append(getTOString("status", status));
        strB.append(getTOString("multiDis", multiDis));
        strB.append(getTOString("avalbalBorrowedMaster", String.valueOf(avalbalBorrowedMaster)));
        strB.append(getTOStringEnd());
        return strB.toString();
    }

    /**
     * toXML method which returns this TO as a XML output.
     */
    public String toXML() {
        StringBuffer strB = new StringBuffer(getTOXmlStart(this.getClass().getName()));
        strB.append(getTOXmlKey(getKeyData()));
        strB.append(getTOXml("disbursalNo", disbursalNo));
        strB.append(getTOXml("borrowingrefNo", borrowingrefNo));
        strB.append(getTOXml("createdBy", createdBy));
        strB.append(getTOXml("borrowingNo", borrowingNo));
        strB.append(getTOXml("agencyCode", agencyCode));
        strB.append(getTOXml("type", type));
        strB.append(getTOXml("description", description));
        strB.append(getTOXml("sanctionDate", sanctionDate));
        strB.append(getTOXml("sanctionAmt", sanctionAmt));
        strB.append(getTOXml("rateofInt", rateofInt));
        strB.append(getTOXml("noofInstallments", noofInstallments));
        strB.append(getTOXml("prinRepFrq", prinRepFrq));
        strB.append(getTOXml("intRepFrq", intRepFrq));
        strB.append(getTOXml("morotorium", morotorium));
        strB.append(getTOXml("sanctionExpDate", sanctionExpDate));

        strB.append(getTOXml("amtBorrowed", amtBorrowed));
        /*
         * strB.append(getTOXml("modeTrans", modeTrans));
         * strB.append(getTOXml("transAccDetails", transAccDetails));
         * strB.append(getTOXml("productId", productId));
         * strB.append(getTOXml("accNo", accNo));
         * strB.append(getTOXml("accName", accName));
         */
        strB.append(getTOXml("authorizeStatus", authorizeStatus));
        strB.append(getTOXml("authorizeBy", authorizeBy));
        strB.append(getTOXml("authorizeDte", authorizeDte));
        strB.append(getTOXml("status", status));
        strB.append(getTOXml("multiDis", multiDis));
        strB.append(getTOXml("avalbalBorrowedMaster", String.valueOf(avalbalBorrowedMaster)));
        strB.append(getTOXmlEnd());
        return strB.toString();
    }

    /**
     * Getter for property disbursalNo.
     *
     * @return Value of property disbursalNo.
     */
    public java.lang.String getDisbursalNo() {
        return disbursalNo;
    }

    public String getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    /**
     * Setter for property disbursalNo.
     *
     * @param disbursalNo New value of property disbursalNo.
     */
    public void setDisbursalNo(java.lang.String disbursalNo) {
        this.disbursalNo = disbursalNo;
    }

    /**
     * Getter for property borrowingrefNo.
     *
     * @return Value of property borrowingrefNo.
     */
    public java.lang.String getBorrowingrefNo() {
        return borrowingrefNo;
    }

    /**
     * Setter for property borrowingrefNo.
     *
     * @param borrowingrefNo New value of property borrowingrefNo.
     */
    public void setBorrowingrefNo(java.lang.String borrowingrefNo) {
        this.borrowingrefNo = borrowingrefNo;
    }

    /**
     * Getter for property borrowingNo.
     *
     * @return Value of property borrowingNo.
     */
    public java.lang.String getBorrowingNo() {
        return borrowingNo;
    }

    /**
     * Setter for property borrowingNo.
     *
     * @param borrowingNo New value of property borrowingNo.
     */
    public void setBorrowingNo(java.lang.String borrowingNo) {
        this.borrowingNo = borrowingNo;
    }

    /**
     * Getter for property amtBorrowed.
     *
     * @return Value of property amtBorrowed.
     */
    public java.lang.Double getAmtBorrowed() {
        return amtBorrowed;
    }

    /**
     * Setter for property amtBorrowed.
     *
     * @param amtBorrowed New value of property amtBorrowed.
     */
    public void setAmtBorrowed(java.lang.Double amtBorrowed) {
        this.amtBorrowed = amtBorrowed;
    }

    /**
     * Getter for property modeTrans.
     *
     * @return Value of property modeTrans.
     */
    /*
     * public java.lang.String getModeTrans() { return modeTrans; }
     */
    /**
     * Setter for property modeTrans.
     *
     * @param modeTrans New value of property modeTrans.
     */
    /*
     * public void setModeTrans(java.lang.String modeTrans) { this.modeTrans =
     * modeTrans;
        }
     */
    /**
     * Getter for property transAccDetails.
     *
     * @return Value of property transAccDetails.
     */
    /*
     * public java.lang.String getTransAccDetails() { return transAccDetails;
        }
     */
    /**
     * Setter for property transAccDetails.
     *
     * @param transAccDetails New value of property transAccDetails.
     */
    /*
     * public void setTransAccDetails(java.lang.String transAccDetails) {
     * this.transAccDetails = transAccDetails;
        }
     */
    /**
     * Getter for property productId.
     *
     * @return Value of property productId.
     */
    /*
     * public java.lang.String getProductId() { return productId;
        }
     */
    /**
     * Setter for property productId.
     *
     * @param productId New value of property productId.
     */
    /*
     * public void setProductId(java.lang.String productId) { this.productId =
     * productId;
        }
     */
    /**
     * Getter for property accNo.
     *
     * @return Value of property accNo.
     */
    /*
     * public java.lang.String getAccNo() { return accNo;
        }
     */
    /**
     * Setter for property accNo.
     *
     * @param accNo New value of property accNo.
     */
    /*
     * public void setAccNo(java.lang.String accNo) { this.accNo = accNo;
        }
     */
    /**
     * Getter for property accName.
     *
     * @return Value of property accName.
     */
    /*
     * public java.lang.String getAccName() { return accName;
        }
     */
    /**
     * Setter for property accName.
     *
     * @param accName New value of property accName.
     */
    /*
     * public void setAccName(java.lang.String accName) { this.accName =
     * accName;
        }
     */
    /**
     * Getter for property authorizeStatus.
     *
     * @return Value of property authorizeStatus.
     */
    public java.lang.String getAuthorizeStatus() {
        return authorizeStatus;
    }

    /**
     * Setter for property authorizeStatus.
     *
     * @param authorizeStatus New value of property authorizeStatus.
     */
    public void setAuthorizeStatus(java.lang.String authorizeStatus) {
        this.authorizeStatus = authorizeStatus;
    }

    /**
     * Getter for property authorizeBy.
     *
     * @return Value of property authorizeBy.
     */
    public java.lang.String getAuthorizeBy() {
        return authorizeBy;
    }

    /**
     * Setter for property authorizeBy.
     *
     * @param authorizeBy New value of property authorizeBy.
     */
    public void setAuthorizeBy(java.lang.String authorizeBy) {
        this.authorizeBy = authorizeBy;
    }

    /**
     * Getter for property authorizeDte.
     *
     * @return Value of property authorizeDte.
     */
    public java.util.Date getAuthorizeDte() {
        return authorizeDte;
    }

    /**
     * Setter for property authorizeDte.
     *
     * @param authorizeDte New value of property authorizeDte.
     */
    public void setAuthorizeDte(java.util.Date authorizeDte) {
        this.authorizeDte = authorizeDte;
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
     * Getter for property agencyCode.
     *
     * @return Value of property agencyCode.
     */
    public java.lang.String getAgencyCode() {
        return agencyCode;
    }

    /**
     * Setter for property agencyCode.
     *
     * @param agencyCode New value of property agencyCode.
     */
    public void setAgencyCode(java.lang.String agencyCode) {
        this.agencyCode = agencyCode;
    }

    /**
     * Getter for property type.
     *
     * @return Value of property type.
     */
    public java.lang.String getType() {
        return type;
    }

    /**
     * Setter for property type.
     *
     * @param type New value of property type.
     */
    public void setType(java.lang.String type) {
        this.type = type;
    }

    /**
     * Getter for property description.
     *
     * @return Value of property description.
     */
    public java.lang.String getDescription() {
        return description;
    }

    /**
     * Setter for property description.
     *
     * @param description New value of property description.
     */
    public void setDescription(java.lang.String description) {
        this.description = description;
    }

    /**
     * Getter for property sanctionDate.
     *
     * @return Value of property sanctionDate.
     */
    public java.util.Date getSanctionDate() {
        return sanctionDate;
    }

    /**
     * Setter for property sanctionDate.
     *
     * @param sanctionDate New value of property sanctionDate.
     */
    public void setSanctionDate(java.util.Date sanctionDate) {
        this.sanctionDate = sanctionDate;
    }

    /**
     * Getter for property sanctionAmt.
     *
     * @return Value of property sanctionAmt.
     */
    public java.lang.Double getSanctionAmt() {
        return sanctionAmt;
    }

    /**
     * Setter for property sanctionAmt.
     *
     * @param sanctionAmt New value of property sanctionAmt.
     */
    public void setSanctionAmt(java.lang.Double sanctionAmt) {
        this.sanctionAmt = sanctionAmt;
    }

    /**
     * Getter for property rateofInt.
     *
     * @return Value of property rateofInt.
     */
    public java.lang.Double getRateofInt() {
        return rateofInt;
    }

    /**
     * Setter for property rateofInt.
     *
     * @param rateofInt New value of property rateofInt.
     */
    public void setRateofInt(java.lang.Double rateofInt) {
        this.rateofInt = rateofInt;
    }

    /**
     * Getter for property noofInstallments.
     *
     * @return Value of property noofInstallments.
     */
    public java.lang.Double getNoofInstallments() {
        return noofInstallments;
    }

    /**
     * Setter for property noofInstallments.
     *
     * @param noofInstallments New value of property noofInstallments.
     */
    public void setNoofInstallments(java.lang.Double noofInstallments) {
        this.noofInstallments = noofInstallments;
    }

    /**
     * Getter for property prinRepFrq.
     *
     * @return Value of property prinRepFrq.
     */
    public java.lang.String getPrinRepFrq() {
        return prinRepFrq;
    }

    /**
     * Setter for property prinRepFrq.
     *
     * @param prinRepFrq New value of property prinRepFrq.
     */
    public void setPrinRepFrq(java.lang.String prinRepFrq) {
        this.prinRepFrq = prinRepFrq;
    }

    /**
     * Getter for property intRepFrq.
     *
     * @return Value of property intRepFrq.
     */
    public java.lang.String getIntRepFrq() {
        return intRepFrq;
    }

    /**
     * Setter for property intRepFrq.
     *
     * @param intRepFrq New value of property intRepFrq.
     */
    public void setIntRepFrq(java.lang.String intRepFrq) {
        this.intRepFrq = intRepFrq;
    }

    /**
     * Getter for property morotorium.
     *
     * @return Value of property morotorium.
     */
    public java.lang.String getMorotorium() {
        return morotorium;
    }

    /**
     * Setter for property morotorium.
     *
     * @param morotorium New value of property morotorium.
     */
    public void setMorotorium(java.lang.String morotorium) {
        this.morotorium = morotorium;
    }

    /**
     * Getter for property sanctionExpDate.
     *
     * @return Value of property sanctionExpDate.
     */
    public java.util.Date getSanctionExpDate() {
        return sanctionExpDate;
    }

    /**
     * Setter for property sanctionExpDate.
     *
     * @param sanctionExpDate New value of property sanctionExpDate.
     */
    public void setSanctionExpDate(java.util.Date sanctionExpDate) {
        this.sanctionExpDate = sanctionExpDate;
    }

    /**
     * Getter for property amtBorrowedMaster.
     *
     * @return Value of property amtBorrowedMaster.
     */
    public Double getAmtBorrowedMaster() {
        return amtBorrowedMaster;
    }

    /**
     * Setter for property amtBorrowedMaster.
     *
     * @param amtBorrowedMaster New value of property amtBorrowedMaster.
     */
    public void setAmtBorrowedMaster(Double amtBorrowedMaster) {
        this.amtBorrowedMaster = amtBorrowedMaster;
    }

    /**
     * Getter for property avalbalBorrowedMaster.
     *
     * @return Value of property avalbalBorrowedMaster.
     */
    public Double getAvalbalBorrowedMaster() {
        return avalbalBorrowedMaster;
    }

    /**
     * Setter for property avalbalBorrowedMaster.
     *
     * @param avalbalBorrowedMaster New value of property avalbalBorrowedMaster.
     */
    public void setAvalbalBorrowedMaster(Double avalbalBorrowedMaster) {
        this.avalbalBorrowedMaster = avalbalBorrowedMaster;
    }

    /**
     * Getter for property multiDis.
     *
     * @return Value of property multiDis.
     */
    public java.lang.String getMultiDis() {
        return multiDis;
    }

    /**
     * Setter for property multiDis.
     *
     * @param multiDis New value of property multiDis.
     */
    public void setMultiDis(java.lang.String multiDis) {
        this.multiDis = multiDis;
    }
}