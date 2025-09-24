/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition.
 * 
 *
 * TDSConfigTO.java
 * 
 * Created on Thu Feb 10 15:07:29 IST 2005
 */

package com.see.truetransact.transferobject.trading.tradingsales;

import java.util.Date;
import java.io.Serializable;
import com.see.truetransact.transferobject.TransferObject;

/** Table name for this TO is TDS_CONFIG. */

public class TradingSalesTO extends TransferObject implements Serializable {
    private String salesNo = "";
    private String prodName = "";
    private String rate = "";
    private String unitType = "";
    private String qty = "";
    private String mrp = "";
    private String tax = "";
    private String taxableAmt = "";
    private String cash = "";
    private String adjust = "";
    private String total = "";
    private String stockID = "";
    private String remarks = "";
    private String discAmt = "";
    private String status = "";
    private String statusBy = "";
    private Date statusDt = null;
    private String authorizeStatus = null;
    private String authorizeBy = "";
    private Date authorizeDt = null;
    private String transportationCharges = "";
    private String discount = "";
    private String slNo = "";

    public String getAuthorizeBy() {
        return authorizeBy;
    }

    public void setAuthorizeBy(String authorizeBy) {
        this.authorizeBy = authorizeBy;
    }

    public Date getAuthorizeDt() {
        return authorizeDt;
    }

    public void setAuthorizeDt(Date authorizeDt) {
        this.authorizeDt = authorizeDt;
    }

    public String getAuthorizeStatus() {
        return authorizeStatus;
    }

    public void setAuthorizeStatus(String authorizeStatus) {
        this.authorizeStatus = authorizeStatus;
    }
    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getStatusBy() {
        return statusBy;
    }

    public void setStatusBy(String statusBy) {
        this.statusBy = statusBy;
    }

    public Date getStatusDt() {
        return statusDt;
    }

    public void setStatusDt(Date statusDt) {
        this.statusDt = statusDt;
    }

    public String getProdName() {
        return prodName;
    }

    public void setProdName(String prodName) {
        this.prodName = prodName;
    }

    public String getRate() {
        return rate;
    }

    public void setRate(String rate) {
        this.rate = rate;
    }

    public String getUnitType() {
        return unitType;
    }

    public void setUnitType(String unitType) {
        this.unitType = unitType;
    }

    public String getQty() {
        return qty;
    }

    public void setQty(String qty) {
        this.qty = qty;
    }

    public String getMrp() {
        return mrp;
    }

    public void setMrp(String mrp) {
        this.mrp = mrp;
    }

    public String getTax() {
        return tax;
    }

    public void setTax(String tax) {
        this.tax = tax;
    }

    public String getTaxableAmt() {
        return taxableAmt;
    }

    public void setTaxableAmt(String taxableAmt) {
        this.taxableAmt = taxableAmt;
    }

    public String getCash() {
        return cash;
    }

    public void setCash(String cash) {
        this.cash = cash;
    }

    public String getAdjust() {
        return adjust;
    }

    public void setAdjust(String adjust) {
        this.adjust = adjust;
    }

    public String getTotal() {
        return total;
    }

    public void setTotal(String total) {
        this.total = total;
    }

    public String getSalesNo() {
        return salesNo;
    }

    public void setSalesNo(String salesNo) {
        this.salesNo = salesNo;
    }

    public String getTransportationCharges() {
        return transportationCharges;
    }

    public void setTransportationCharges(String transportationCharges) {
        this.transportationCharges = transportationCharges;
    }

    public String getDiscount() {
        return discount;
    }

    public void setDiscount(String discount) {
        this.discount = discount;
    }

    public String getSlNo() {
        return slNo;
    }

    public void setSlNo(String slNo) {
        this.slNo = slNo;
    }

    public String getStockID() {
        return stockID;
    }

    public void setStockID(String stockID) {
        this.stockID = stockID;
    }

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }

    public String getDiscAmt() {
        return discAmt;
    }

    public void setDiscAmt(String discAmt) {
        this.discAmt = discAmt;
    }
    
    
    
    
    
	/** toString method which returns this TO as a String. */
	public String toString() {
		StringBuffer strB = new StringBuffer(getTOStringStart(this.getClass().getName()));
		strB.append(getTOString("salesNo", salesNo));
                strB.append(getTOString("prodName", prodName));
                strB.append(getTOString("rate", rate));
                strB.append(getTOString("unitType", unitType));
                strB.append(getTOString("qty", qty));
                strB.append(getTOString("mrp", mrp));
                strB.append(getTOString("tax", tax));
                strB.append(getTOString("taxableAmt", taxableAmt));
                strB.append(getTOString("cash", cash));
                strB.append(getTOString("adjust", adjust));
                strB.append(getTOString("total", total));
                strB.append(getTOString("transportationCharges", transportationCharges));
                strB.append(getTOString("discount", discount));
                strB.append(getTOString("discAmt", discAmt));
                strB.append(getTOString("stockID", stockID));
                strB.append(getTOString("remarks", remarks));
                strB.append(getTOString("status", status));
		strB.append(getTOString("statusBy", statusBy));
		strB.append(getTOString("statusDt", statusDt));
		strB.append(getTOString("authorizeStatus", authorizeStatus));
		strB.append(getTOString("authorizeBy", authorizeBy));
		strB.append(getTOString("authorizeDt", authorizeDt));
                strB.append(getTOString("slNo", slNo));
	        strB.append(getTOStringEnd());
		return strB.toString();
	}

	/** toXML method which returns this TO as a XML output. */
	public String toXML() {
		StringBuffer strB = new StringBuffer(getTOXmlStart(this.getClass().getName()));
                strB.append(getTOXml("salesNo", salesNo));
                strB.append(getTOXml("prodName", prodName));
                strB.append(getTOXml("rate", rate));
                strB.append(getTOXml("unitType", unitType));
                strB.append(getTOXml("qty", qty));
                strB.append(getTOXml("mrp", mrp));
                strB.append(getTOXml("tax", tax));
                strB.append(getTOXml("taxableAmt", taxableAmt));
                strB.append(getTOXml("cash", cash));
                strB.append(getTOXml("adjust", adjust));
                strB.append(getTOXml("total", total));
                strB.append(getTOXml("transportationCharges", transportationCharges));
                strB.append(getTOXml("discount", discount));
                strB.append(getTOXml("discAmt", discAmt));
                strB.append(getTOXml("stockID", stockID));
                strB.append(getTOXml("remarks", remarks));
		strB.append(getTOXml("status", status));
		strB.append(getTOXml("statusBy", statusBy));
		strB.append(getTOXml("statusDt", statusDt));
		strB.append(getTOXml("authorizeStatus", authorizeStatus));
		strB.append(getTOXml("authorizeBy", authorizeBy));
		strB.append(getTOXml("authorizeDt", authorizeDt));
                strB.append(getTOXml("slNo", slNo));
		strB.append(getTOXmlEnd());
		return strB.toString();
	}
        
        public String getKeyData() {
        setKeyColumns(salesNo);
        return salesNo;
    }

        /**
         * Getter for property tdsCrAchdId.
         * @return Value of property tdsCrAchdId.
         */
//        public java.lang.String getTdsCrAchdId() {
//            return tdsCrAchdId;
//        }
//        
//        /**
//         * Setter for property tdsCrAchdId.
//         * @param tdsCrAchdId New value of property tdsCrAchdId.
//         */
//        public void setTdsCrAchdId(java.lang.String tdsCrAchdId) {
//            this.tdsCrAchdId = tdsCrAchdId;
//        }
        
        /**
         * Getter for property custType.
         * @return Value of property custType.
         */
//        public java.lang.String getCustType() {
//            return custType;
//        }
//        
//        /**
//         * Setter for property custType.
//         * @param custType New value of property custType.
//         */
//        public void setCustType(java.lang.String custType) {
//            this.custType = custType;
//        }
        
}