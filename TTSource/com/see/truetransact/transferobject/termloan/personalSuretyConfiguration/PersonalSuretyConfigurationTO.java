/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition.
 * 
 *
 * EmpTransferTO.java
 * 
 * Created on Wed Jun 28  2010 swaroop
 */
package com.see.truetransact.transferobject.termloan.personalSuretyConfiguration;

import java.util.Date;
import java.io.Serializable;
import com.see.truetransact.transferobject.TransferObject;
import java.util.ArrayList;
import java.util.List;

/**
 * Table name for this TO is EMP_TRANSFER.
 */
public class PersonalSuretyConfigurationTO extends TransferObject implements Serializable {

    private String maxSurety = "";
    private String closeBefore = "";
    private String gbid = "";
    private Date statusDt = null;
    private Double maximumLoanAmount = 0.0;
    private String prodType = "";
    private String prodID = "";
    private String ProdDesc = "";
    private List selectedList = new ArrayList();
    private String status = "";
    private Integer pan = 0;
    private Date effectiveDate = null;
    private Integer maxNoOfLoans = 0; // Added by nithya on 21-07-2016 for 4922
    private String maxLoanSurety = ""; //which means Maximum Number of Loan allowed for a surity (Added by rishad)
    private String shareType = "";
    private Double maxSuretyAmt = 0.0;

    public String getShareType() {
        return shareType;
    }

    public void setShareType(String shareType) {
        this.shareType = shareType;
    }

    public Integer getMaxNoOfLoans() {
        return maxNoOfLoans;
    }

    public void setMaxNoOfLoans(Integer maxNoOfLoans) {
        this.maxNoOfLoans = maxNoOfLoans;
    }
 
    /**
     * getKeyData returns the Primary Key Columns for this TO User needs to add
     * the Key columns as a setter Example : setKeyColumns("col1" +
     * KEY_VAL_SEPARATOR + "col2"); return col1 + KEY_VAL_SEPARATOR + col2;
     */
//	 public String getKeyData() {
//		setKeyColumns("empTransferID");
//		return empTransferID;
//	}
//
//	/** toString method which returns this TO as a String. */
//	public String toString() {
//		StringBuffer strB = new StringBuffer(getTOStringStart(this.getClass().getName()));
//		strB.append (getTOStringKey(getKeyData()));
//		strB.append(getTOString("empTransferID", empTransferID));
//		strB.append(getTOString("empID", empID));
//		strB.append(getTOString("currBran", currBran));
//		strB.append(getTOString("transferBran", transferBran));
//		strB.append(getTOString("lastWorkingDay", lastWorkingDay));
//		strB.append(getTOString("doj", doj));
//		strB.append(getTOString("roleInCurrBran", roleInCurrBran));
//		strB.append(getTOString("roleInTransferBran", roleInTransferBran));
//		strB.append(getTOString("applType", applType));
//		strB.append(getTOString("branCode", branCode));
//		strB.append(getTOString("status", status));
//		strB.append(getTOString("statusDt", statusDt));
//		strB.append(getTOString("statusBy", statusBy));
//		strB.append(getTOString("createdDt", createdDt));
//                strB.append(getTOString("createdBy", createdBy));
//                strB.append(getTOString("authorizeBy", authorizeBy));
//                strB.append(getTOString("authorizeDt", authorizeDt));
//                strB.append(getTOString("authorizedStatus", authorizedStatus));
//                strB.append(getTOString("empName", empName));
//                strB.append(getTOString("currBranName", currBranName));
//		strB.append(getTOStringEnd());
//		return strB.toString();
//	}
//
//	/** toXML method which returns this TO as a XML output. */
//	public String toXML() {
//		StringBuffer strB = new StringBuffer(getTOXmlStart(this.getClass().getName()));
//		strB.append (getTOXmlKey(getKeyData()));
//		strB.append(getTOXml("empTransferID", empTransferID));
//		strB.append(getTOXml("empID", empID));
//		strB.append(getTOXml("currBran", currBran));
//		strB.append(getTOXml("transferBran", transferBran));
//		strB.append(getTOXml("lastWorkingDay", lastWorkingDay));
//		strB.append(getTOXml("doj", doj));
//		strB.append(getTOXml("roleInCurrBran", roleInCurrBran));
//		strB.append(getTOXml("roleInTransferBran", roleInTransferBran));
//		strB.append(getTOXml("applType", applType));
//		strB.append(getTOXml("branCode", branCode));
//		strB.append(getTOXml("status", status));
//		strB.append(getTOXml("statusDt", statusDt));
//		strB.append(getTOXml("statusBy", statusBy));
//		strB.append(getTOXml("createdDt", createdDt));
//                strB.append(getTOXml("createdBy", createdBy));
//                strB.append(getTOXml("authorizeBy", authorizeBy));
//                strB.append(getTOXml("authorizeDt", authorizeDt));
//                strB.append(getTOXml("authorizedStatus", authorizedStatus));
//                strB.append(getTOXml("empName", empName));
//                strB.append(getTOXml("currBranName", currBranName));
//		strB.append(getTOXmlEnd());
//		return strB.toString();
//	}
//
//        /**
//         * Getter for property empTransferID.
//         * @return Value of property empTransferID.
//         */
//        public java.lang.String getVenu() {
//            return venu;
//        }
//        
//        /**
//         * Setter for property empTransferID.
//         * @param empTransferID New value of property empTransferID.
//         */
//        public void setVenu(java.lang.String venu) {
//            this.venu = venu;
//        }
    public java.lang.String getStatus() {
        return status;
    }

    /**
     * Setter for property empTransferID.
     *
     * @param empTransferID New value of property empTransferID.
     */
    public void setStatus(java.lang.String status) {
        this.status = status;
    }
    /**
     * Getter for property gbid.
     *
     * @return Value of property gbid.
     */
    public java.lang.String getGbid() {
        return gbid;
    }

    /**
     * Setter for property gbid.
     *
     * @param gbid New value of property gbid.
     */
    public void setGbid(java.lang.String gbid) {
        this.gbid = gbid;
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
     * Getter for property maxSurety.
     *
     * @return Value of property maxSurety.
     */
    public String getMaxSurety() {
        return maxSurety;
    }

    /**
     * Setter for property maxSurety.
     *
     * @param maxSurety New value of property maxSurety.
     */
    public void setMaxSurety(String maxSurety) {
        this.maxSurety = maxSurety;
    }

    /**
     * Getter for property closeBefore.
     *
     * @return Value of property closeBefore.
     */
    public String getCloseBefore() {
        return closeBefore;
    }

    /**
     * Setter for property closeBefore.
     *
     * @param closeBefore New value of property closeBefore.
     */
    public void setCloseBefore(String closeBefore) {
        this.closeBefore = closeBefore;
    }

    /**
     * Getter for property maximumLoanAmount.
     *
     * @return Value of property maximumLoanAmount.
     */
    public Double getMaximumLoanAmount() {
        return maximumLoanAmount;
    }

    /**
     * Setter for property maximumLoanAmount.
     *
     * @param maximumLoanAmount New value of property maximumLoanAmount.
     */
    public void setMaximumLoanAmount(Double maximumLoanAmount) {
        this.maximumLoanAmount = maximumLoanAmount;
    }

    /**
     * Getter for property prodType.
     *
     * @return Value of property prodType.
     */
    public String getProdType() {
        return prodType;
    }

    /**
     * Setter for property prodType.
     *
     * @param prodType New value of property prodType.
     */
    public void setProdType(String prodType) {
        this.prodType = prodType;
    }

    /**
     * Getter for property selectedList.
     *
     * @return Value of property selectedList.
     */
    public List getSelectedList() {
        return selectedList;
    }

    /**
     * Setter for property selectedList.
     *
     * @param selectedList New value of property selectedList.
     */
    public void setSelectedList(List selectedList) {
        this.selectedList = selectedList;
    }

    /**
     * Getter for property pan.
     *
     * @return Value of property pan.
     */
    public Integer getPan() {
        return pan;
    }

    /**
     * Setter for property pan.
     *
     * @param pan New value of property pan.
     */
    public void setPan(Integer pan) {
        this.pan = pan;
    }

    /**
     * Getter for property prodID.
     *
     * @return Value of property prodID.
     */
    public String getProdID() {
        return prodID;
    }

    /**
     * Setter for property prodID.
     *
     * @param prodID New value of property prodID.
     */
    public void setProdID(String prodID) {
        this.prodID = prodID;
    }

    /**
     * Getter for property ProdDesc.
     *
     * @return Value of property ProdDesc.
     */
    public String getProdDesc() {
        return ProdDesc;
    }

    /**
     * Setter for property ProdDesc.
     *
     * @param ProdDesc New value of property ProdDesc.
     */
    public void setProdDesc(String ProdDesc) {
        this.ProdDesc = ProdDesc;
    }

    /**
     * Getter for property effectiveDate.
     *
     * @return Value of property effectiveDate.
     */
    public Date getEffectiveDate() {
        return effectiveDate;
    }

    /**
     * Setter for property effectiveDate.
     *
     * @param effectiveDate New value of property effectiveDate.
     */
    public void setEffectiveDate(Date effectiveDate) {
        this.effectiveDate = effectiveDate;
    }

    public String getMaxLoanSurety() {
        return maxLoanSurety;
    }

    public void setMaxLoanSurety(String maxLoanSurety) {
        this.maxLoanSurety = maxLoanSurety;
    }
    
    public Double getMaxSuretyAmt() {
        return maxSuretyAmt;
    }

    public void setMaxSuretyAmt(Double maxSuretyAmt) {
        this.maxSuretyAmt = maxSuretyAmt;
    }
    
}