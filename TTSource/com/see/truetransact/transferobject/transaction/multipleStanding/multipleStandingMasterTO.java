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
package com.see.truetransact.transferobject.transaction.multipleStanding;
import com.see.truetransact.transferobject.transaction.balance.*;

import java.util.Date;
import java.io.Serializable;
import com.see.truetransact.transferobject.TransferObject;
import java.util.ArrayList;
import java.util.List;

/**
 * Table name for this TO is EMP_TRANSFER.
 */
public class multipleStandingMasterTO extends TransferObject implements Serializable {

    private String standingId = "";
    private String accountHead = "";
    private String transType = "";
    private Date statusDt = null;
    private Date authorizedDt = null;
    private Double transAmount = 0.0;
    private String transProdType = "";
    private String transProdID = "";
    private String transAccNo = "";
    private String status = "";
    private String statusBy = "";
    private String authorizeBy = "";
    private String authorizedStatus = null;
    private String branchId = ""; 
    private String particulars = "";

    public String getParticulars() {
        return particulars;
    }

    public void setParticulars(String particulars) {
        this.particulars = particulars;
    }

    /**
     * getKeyData returns the Primary Key Columns for this TO User needs to add
     * the Key columns as a setter Example : setKeyColumns("col1" +
     * KEY_VAL_SEPARATOR + "col2"); return col1 + KEY_VAL_SEPARATOR + col2;
     */
	 public String getKeyData() {
		setKeyColumns("standingId");
		return standingId;
	}
//
	/** toString method which returns this TO as a String. */
	public String toString() {
		StringBuffer strB = new StringBuffer(getTOStringStart(this.getClass().getName()));
		strB.append (getTOStringKey(getKeyData()));
		strB.append(getTOString("standingId", standingId));
		strB.append(getTOString("accountHead", accountHead));
		strB.append(getTOString("transType", transType));
		strB.append(getTOString("transAmount", transAmount));
		strB.append(getTOString("statusDt", statusDt));
		strB.append(getTOString("authorizedDt", authorizedDt));
		strB.append(getTOString("transProdType", transProdType));
		strB.append(getTOString("transProdID", transProdID));
		strB.append(getTOString("transAccNo", transAccNo));
		strB.append(getTOString("status", status));
		strB.append(getTOString("statusBy", statusBy));
		strB.append(getTOString("authorizeBy", authorizeBy));
		strB.append(getTOString("statusBy", statusBy));
		strB.append(getTOString("authorizedStatus", authorizedStatus));
                strB.append(getTOString("branchId", branchId));
                strB.append(getTOString("particulars", particulars));
		strB.append(getTOStringEnd());
		return strB.toString();
	}

	/** toXML method which returns this TO as a XML output. */
	public String toXML() {
		StringBuffer strB = new StringBuffer(getTOXmlStart(this.getClass().getName()));
		strB.append (getTOXmlKey(getKeyData()));
		strB.append(getTOXml("standingId", standingId));
		strB.append(getTOXml("accountHead", accountHead));
		strB.append(getTOXml("transType", transType));
		strB.append(getTOXml("transAmount", transAmount));
		strB.append(getTOXml("statusDt", statusDt));
		strB.append(getTOXml("authorizedDt", authorizedDt));
		strB.append(getTOXml("transProdType", transProdType));
		strB.append(getTOXml("transProdID", transProdID));
		strB.append(getTOXml("transAccNo", transAccNo));
		strB.append(getTOXml("status", status));
		strB.append(getTOXml("statusBy", statusBy));
		strB.append(getTOXml("authorizeBy", authorizeBy));
		strB.append(getTOXml("statusBy", statusBy));
		strB.append(getTOXml("authorizedStatus", authorizedStatus));
                strB.append(getTOXml("branchId", branchId));
                strB.append(getTOXml("particulars", particulars));
		strB.append(getTOXmlEnd());
		return strB.toString();
	}

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
//        
//        /**
//         * Getter for property empID.
//         * @return Value of property empID.
//         */
//        public int getTotalAttendance() {
//            return totalAttendance;
//        }
//        
//        /**
//         * Setter for property empID.
//         * @param empID New value of property empID.
//         */
//        public void setTotalAttendance(int totalAttendance) {
//            this.totalAttendance = totalAttendance;
//        }
////        
////        /**
////         * Getter for property currBran.
////         * @return Value of property currBran.
//     //    */
//        public java.lang.String getRemarks() {
//            return remarks;
//        }
//        
//        /**
//         * Setter for property currBran.
//         * @param currBran New value of property currBran.
//         */
//        public void setRemarks(java.lang.String remarks) {
//            this.remarks = remarks;
//        }
////        
////        /**
////         * Getter for property transferBran.
////         * @return Value of property transferBran.
////         */
////        public java.lang.String getTransferBran() {
////            return transferBran;
////        }
////        
////        /**
////         * Setter for property transferBran.
////         * @param transferBran New value of property transferBran.
////         */
////        public void setTransferBran(java.lang.String transferBran) {
////            this.transferBran = transferBran;
////        }
////        
//        /**
//         * Getter for property lastWorkingDay.
//         * @return Value of property lastWorkingDay.
//         */
//        public java.util.Date getgDate() {
//            return gDate;
//        }
////        
////        /**
////         * Setter for property lastWorkingDay.
////         * @param lastWorkingDay New value of property lastWorkingDay.
////         */
//        public void setgDate(java.util.Date gDate) {
//            this.gDate = gDate;
//        }
//        

  
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

    public String getAccountHead() {
        return accountHead;
    }

    public void setAccountHead(String accountHead) {
        this.accountHead = accountHead;
    }

    public String getAuthorizeBy() {
        return authorizeBy;
    }

    public void setAuthorizeBy(String authorizeBy) {
        this.authorizeBy = authorizeBy;
    }

    public Date getAuthorizedDt() {
        return authorizedDt;
    }

    public void setAuthorizedDt(Date authorizedDt) {
        this.authorizedDt = authorizedDt;
    }

    public String getAuthorizedStatus() {
        return authorizedStatus;
    }

    public void setAuthorizedStatus(String authorizedStatus) {
        this.authorizedStatus = authorizedStatus;
    }

    public String getBranchId() {
        return branchId;
    }

    public void setBranchId(String branchId) {
        this.branchId = branchId;
    }

    public String getStandingId() {
        return standingId;
    }

    public void setStandingId(String standingId) {
        this.standingId = standingId;
    }

    public String getStatusBy() {
        return statusBy;
    }

    public void setStatusBy(String statusBy) {
        this.statusBy = statusBy;
    }

    public String getTransAccNo() {
        return transAccNo;
    }

    public void setTransAccNo(String transAccNo) {
        this.transAccNo = transAccNo;
    }

    public Double getTransAmount() {
        return transAmount;
    }

    public void setTransAmount(Double transAmount) {
        this.transAmount = transAmount;
    }

    public String getTransProdID() {
        return transProdID;
    }

    public void setTransProdID(String transProdID) {
        this.transProdID = transProdID;
    }

    public String getTransProdType() {
        return transProdType;
    }

    public void setTransProdType(String transProdType) {
        this.transProdType = transProdType;
    }

    public String getTransType() {
        return transType;
    }

    public void setTransType(String transType) {
        this.transType = transType;
    }

}