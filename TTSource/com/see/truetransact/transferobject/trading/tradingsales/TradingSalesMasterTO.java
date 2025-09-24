/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition.
 * 
 *
 * TradingSalesMasterTO.java
 * 
 * Created on Thu Feb 10 15:07:29 IST 2005
 */

package com.see.truetransact.transferobject.trading.tradingsales;

import java.util.Date;
import java.io.Serializable;
import com.see.truetransact.transferobject.TransferObject;

/** Table name for this TO is TDS_CONFIG. */

public class TradingSalesMasterTO extends TransferObject implements Serializable {
    private String salesNo = "";
    private String salesType = "";
    private Date salesDate = null;
    private String place = "";
    private String memberType = "";
    private String empNo = "";
    private String empName = "";
    private String barcodeNumber = "";
    private String prodID = "";
    private String prodDesc = "";
    private String acNo = "";
    private String installment = "";
    private String installmentNo = "";
    private String installmentAmt = "";
    private String firstInstAmt = "";
    private Date instDt = null;
    private String grandTotal = "";
    private String transportationCharges = "";
    private String discount = "";
     private String adjust = "";
    private String discAmt = "";
    private String status = "";
    private String statusBy = "";
    private Date statusDt = null;
    private String authorizeStatus = null;
    private String authorizeBy = "";
    private Date authorizeDt = null;

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

    public String getSalesNo() {
        return salesNo;
    }

    public void setSalesNo(String salesNo) {
        this.salesNo = salesNo;
    }

    public String getSalesType() {
        return salesType;
    }

    public void setSalesType(String salesType) {
        this.salesType = salesType;
    }

    public Date getSalesDate() {
        return salesDate;
    }

    public void setSalesDate(Date salesDate) {
        this.salesDate = salesDate;
    }

    public String getPlace() {
        return place;
    }

    public void setPlace(String place) {
        this.place = place;
    }

    public String getMemberType() {
        return memberType;
    }

    public void setMemberType(String memberType) {
        this.memberType = memberType;
    }

    public String getEmpNo() {
        return empNo;
    }

    public void setEmpNo(String empNo) {
        this.empNo = empNo;
    }

    public String getEmpName() {
        return empName;
    }

    public void setEmpName(String empName) {
        this.empName = empName;
    }

    public String getBarcodeNumber() {
        return barcodeNumber;
    }

    public void setBarcodeNumber(String barcodeNumber) {
        this.barcodeNumber = barcodeNumber;
    }

    public String getProdID() {
        return prodID;
    }

    public void setProdID(String prodID) {
        this.prodID = prodID;
    }

    public String getProdDesc() {
        return prodDesc;
    }

    public void setProdDesc(String prodDesc) {
        this.prodDesc = prodDesc;
    }

    public String getAcNo() {
        return acNo;
    }

    public void setAcNo(String acNo) {
        this.acNo = acNo;
    }

    public String getInstallment() {
        return installment;
    }

    public void setInstallment(String installment) {
        this.installment = installment;
    }

    public String getInstallmentNo() {
        return installmentNo;
    }

    public void setInstallmentNo(String installmentNo) {
        this.installmentNo = installmentNo;
    }

    public String getInstallmentAmt() {
        return installmentAmt;
    }

    public void setInstallmentAmt(String installmentAmt) {
        this.installmentAmt = installmentAmt;
    }

    public String getFirstInstAmt() {
        return firstInstAmt;
    }

    public void setFirstInstAmt(String firstInstAmt) {
        this.firstInstAmt = firstInstAmt;
    }

    public Date getInstDt() {
        return instDt;
    }

    public void setInstDt(Date instDt) {
        this.instDt = instDt;
    }

    public String getGrandTotal() {
        return grandTotal;
    }

    public void setGrandTotal(String grandTotal) {
        this.grandTotal = grandTotal;
    }
    
    public String getKeyData() {
        setKeyColumns(salesNo);
        return salesNo;
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

    public String getAdjust() {
        return adjust;
    }

    public void setAdjust(String adjust) {
        this.adjust = adjust;
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
                strB.append(getTOString("salesType", salesType));
                strB.append(getTOString("salesDate", salesDate));
                strB.append(getTOString("place", place));
                strB.append(getTOString("memberType", memberType));
                strB.append(getTOString("empNo", empNo));
                strB.append(getTOString("empName", empName));
                strB.append(getTOString("barcodeNumber", barcodeNumber));
                strB.append(getTOString("prodID", prodID));
                strB.append(getTOString("prodDesc", prodDesc));
                strB.append(getTOString("acNo", acNo));
                strB.append(getTOString("installment", installment));
                strB.append(getTOString("installmentNo", installmentNo));
                strB.append(getTOString("installmentAmt", installmentAmt));
                strB.append(getTOString("firstInstAmt", firstInstAmt));
                strB.append(getTOString("instDt", instDt));
                strB.append(getTOString("grandTotal", grandTotal));
                strB.append(getTOString("discount", discount));
                strB.append(getTOString("discAmt", discAmt));
                strB.append(getTOString("adjust", adjust));
                strB.append(getTOString("transportationCharges", transportationCharges));
                strB.append(getTOString("status", status));
		strB.append(getTOString("statusBy", statusBy));
		strB.append(getTOString("statusDt", statusDt));
		strB.append(getTOString("authorizeStatus", authorizeStatus));
		strB.append(getTOString("authorizeBy", authorizeBy));
		strB.append(getTOString("authorizeDt", authorizeDt));
	        strB.append(getTOStringEnd());
		return strB.toString();
	}

	/** toXML method which returns this TO as a XML output. */
	public String toXML() {
		StringBuffer strB = new StringBuffer(getTOXmlStart(this.getClass().getName()));
                strB.append(getTOXml("salesNo", salesNo));
                strB.append(getTOXml("salesType", salesType));
                strB.append(getTOXml("salesDate", salesDate));
                strB.append(getTOXml("place", place));
                strB.append(getTOXml("memberType", memberType));
                strB.append(getTOXml("empNo", empNo));
                strB.append(getTOXml("empName", empName));
                strB.append(getTOXml("barcodeNumber", barcodeNumber));
                strB.append(getTOXml("prodID", prodID));
                strB.append(getTOXml("prodDesc", prodDesc));
                strB.append(getTOXml("acNo", acNo));
                strB.append(getTOXml("installment", installment));
                strB.append(getTOXml("installmentNo", installmentNo));
                strB.append(getTOXml("installmentAmt", installmentAmt));
                strB.append(getTOXml("firstInstAmt", firstInstAmt));
                strB.append(getTOXml("instDt", instDt));
                strB.append(getTOXml("grandTotal", grandTotal));
                strB.append(getTOXml("discount", discount));
                strB.append(getTOXml("discAmt", discAmt));
                strB.append(getTOXml("adjust", adjust));
                strB.append(getTOXml("transportationCharges", transportationCharges));
		strB.append(getTOXml("status", status));
		strB.append(getTOXml("statusBy", statusBy));
		strB.append(getTOXml("statusDt", statusDt));
		strB.append(getTOXml("authorizeStatus", authorizeStatus));
		strB.append(getTOXml("authorizeBy", authorizeBy));
		strB.append(getTOXml("authorizeDt", authorizeDt));
		strB.append(getTOXmlEnd());
		return strB.toString();
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