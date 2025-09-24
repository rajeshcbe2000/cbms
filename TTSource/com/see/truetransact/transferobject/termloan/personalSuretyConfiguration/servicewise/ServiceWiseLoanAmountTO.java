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
package com.see.truetransact.transferobject.termloan.personalSuretyConfiguration.servicewise;

import java.util.Date;
import java.io.Serializable;
import com.see.truetransact.transferobject.TransferObject;
import java.util.ArrayList;
import java.util.List;

/**
 * Table name for this TO is EMP_TRANSFER.
 */
public class ServiceWiseLoanAmountTO extends TransferObject implements Serializable {

//	private String maxSurety = "";
    private Double fromServicePeriod = 0.0;
    private Double toServicePeriod = 0.0;
    private Double totServicePeriodRequired = 0.0;
    private Double totSecurityRequired = 0.0;
    private Double minimumLoanAmount = 0.0;
    private Date fromDate = null;
//	private String closeBefore = "";
//	private int totalAttendance = 0;
//	private String remarks = "";
    private String gbid = "";
    private Date statusDt = null;
    private Double maximumLoanAmount = 0.0;
    private String prodType = "";
    private String prodID = "";
    private String ProdDesc = "";
    private List selectedList = new ArrayList();
//        private String branCode = "";
    private String status = "";
//   private int pan=0;
//        private Date statusDt = null;
//        private String statusBy = "";
//        private Date createdDt = null;
//        private String createdBy = "";
//	private String authorizeBy = "";
//	private Date authorizeDt = null;
//        private String authorizedStatus = "";
//        private String empName = "";
//        private String currBranName = "";

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
	public String toString() {
	StringBuffer strB = new StringBuffer(getTOStringStart(this.getClass().getName()));
            strB.append(getTOString("fromServicePeriod", fromServicePeriod));
            strB.append(getTOString("toServicePeriod", toServicePeriod));
            strB.append(getTOString("totServicePeriodRequired", totServicePeriodRequired));
            strB.append(getTOString("totSecurityRequired", totSecurityRequired));
            strB.append(getTOString("minimumLoanAmount", minimumLoanAmount));
            strB.append(getTOString("maximumLoanAmount", maximumLoanAmount));
            strB.append(getTOString("fromDate", fromDate));
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
		strB.append(getTOStringEnd());
		return strB.toString();
	}
//
//	/** toXML method which returns this TO as a XML output. */
	public String toXML() {
		StringBuffer strB = new StringBuffer(getTOXmlStart(this.getClass().getName()));
                strB.append(getTOXml("fromServicePeriod", fromServicePeriod));
                strB.append(getTOXml("toServicePeriod", toServicePeriod));
                strB.append(getTOXml("totServicePeriodRequired", totServicePeriodRequired));
                strB.append(getTOXml("totSecurityRequired", totSecurityRequired));
                strB.append(getTOXml("minimumLoanAmount", minimumLoanAmount));
                strB.append(getTOXml("maximumLoanAmount", maximumLoanAmount));
                strB.append(getTOXml("fromDate", fromDate));
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
		strB.append(getTOXmlEnd());
		return strB.toString();
	}
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
     * Getter for property gbid.
     *
     * @return Value of property gbid.
     */
    public java.lang.String getGbid() {
        return gbid;
    }

    public Double getTotSecurityRequired() {
        return totSecurityRequired;
    }

    public void setTotSecurityRequired(Double totSecurityRequired) {
        this.totSecurityRequired = totSecurityRequired;
    }

    public Double getTotServicePeriodRequired() {
        return totServicePeriodRequired;
    }

    public void setTotServicePeriodRequired(Double totServicePeriodRequired) {
        this.totServicePeriodRequired = totServicePeriodRequired;
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
     * Getter for property fromServicePeriod.
     *
     * @return Value of property fromServicePeriod.
     */
    public Double getFromServicePeriod() {
        return fromServicePeriod;
    }

    /**
     * Setter for property fromServicePeriod.
     *
     * @param fromServicePeriod New value of property fromServicePeriod.
     */
    public void setFromServicePeriod(Double fromServicePeriod) {
        this.fromServicePeriod = fromServicePeriod;
    }

    /**
     * Getter for property toServicePeriod.
     *
     * @return Value of property toServicePeriod.
     */
    public Double getToServicePeriod() {
        return toServicePeriod;
    }

    /**
     * Setter for property toServicePeriod.
     *
     * @param toServicePeriod New value of property toServicePeriod.
     */
    public void setToServicePeriod(Double toServicePeriod) {
        this.toServicePeriod = toServicePeriod;
    }

    /**
     * Getter for property minimumLoanAmount.
     *
     * @return Value of property minimumLoanAmount.
     */
    public Double getMinimumLoanAmount() {
        return minimumLoanAmount;
    }

    /**
     * Setter for property minimumLoanAmount.
     *
     * @param minimumLoanAmount New value of property minimumLoanAmount.
     */
    public void setMinimumLoanAmount(Double minimumLoanAmount) {
        this.minimumLoanAmount = minimumLoanAmount;
    }

    /**
     * Getter for property fromDate.
     *
     * @return Value of property fromDate.
     */
    public Date getFromDate() {
        return fromDate;
    }

    /**
     * Setter for property fromDate.
     *
     * @param fromDate New value of property fromDate.
     */
    public void setFromDate(Date fromDate) {
        this.fromDate = fromDate;
    }
}
//        /**
//         * Getter for property doj.
//         * @return Value of property doj.
//         */
//        public java.util.Date getDoj() {
//            return doj;
//        }
//        
//        /**
//         * Setter for property doj.
//         * @param doj New value of property doj.
//         */
//        public void setDoj(java.util.Date doj) {
//            this.doj = doj;
//        }
//        
//        /**
//         * Getter for property roleInCurrBran.
//         * @return Value of property roleInCurrBran.
//         */
//        public java.lang.String getRoleInCurrBran() {
//            return roleInCurrBran;
//        }
//        
//        /**
//         * Setter for property roleInCurrBran.
//         * @param roleInCurrBran New value of property roleInCurrBran.
//         */
//        public void setRoleInCurrBran(java.lang.String roleInCurrBran) {
//            this.roleInCurrBran = roleInCurrBran;
//        }
//        
//        /**
//         * Getter for property roleInTransferBran.
//         * @return Value of property roleInTransferBran.
//         */
//        public java.lang.String getRoleInTransferBran() {
//            return roleInTransferBran;
//        }
//        
//        /**
//         * Setter for property roleInTransferBran.
//         * @param roleInTransferBran New value of property roleInTransferBran.
//         */
//        public void setRoleInTransferBran(java.lang.String roleInTransferBran) {
//            this.roleInTransferBran = roleInTransferBran;
//        }
//        
//        /**
//         * Getter for property applType.
//         * @return Value of property applType.
//         */
//        public java.lang.String getApplType() {
//            return applType;
//        }
//        
//        /**
//         * Setter for property applType.
//         * @param applType New value of property applType.
//         */
//        public void setApplType(java.lang.String applType) {
//            this.applType = applType;
//        }
//        
//        /**
//         * Getter for property branCode.
//         * @return Value of property branCode.
//         */
//        public java.lang.String getBranCode() {
//            return branCode;
//        }
//        
//        /**
//         * Setter for property branCode.
//         * @param branCode New value of property branCode.
//         */
//        public void setBranCode(java.lang.String branCode) {
//            this.branCode = branCode;
//        }
//        
//        /**
//         * Getter for property status.
//         * @return Value of property status.
//         */
//        public java.lang.String getStatus() {
//            return status;
//        }
//        
//        /**
//         * Setter for property status.
//         * @param status New value of property status.
//         */
//        public void setStatus(java.lang.String status) {
//            this.status = status;
//        }
//        
//        /**
//         * Getter for property statusDt.
//         * @return Value of property statusDt.
//         */
//        public java.util.Date getStatusDt() {
//            return statusDt;
//        }
//        
//        /**
//         * Setter for property statusDt.
//         * @param statusDt New value of property statusDt.
//         */
//        public void setStatusDt(java.util.Date statusDt) {
//            this.statusDt = statusDt;
//        }
//        
//        /**
//         * Getter for property statusBy.
//         * @return Value of property statusBy.
//         */
//        public java.lang.String getStatusBy() {
//            return statusBy;
//        }
//        
//        /**
//         * Setter for property statusBy.
//         * @param statusBy New value of property statusBy.
//         */
//        public void setStatusBy(java.lang.String statusBy) {
//            this.statusBy = statusBy;
//        }
//        
//        /**
//         * Getter for property createdDt.
//         * @return Value of property createdDt.
//         */
//        public java.util.Date getCreatedDt() {
//            return createdDt;
//        }
//        
//        /**
//         * Setter for property createdDt.
//         * @param createdDt New value of property createdDt.
//         */
//        public void setCreatedDt(java.util.Date createdDt) {
//            this.createdDt = createdDt;
//        }
//        
//        /**
//         * Getter for property createdBy.
//         * @return Value of property createdBy.
//         */
//        public java.lang.String getCreatedBy() {
//            return createdBy;
//        }
//        
//        /**
//         * Setter for property createdBy.
//         * @param createdBy New value of property createdBy.
//         */
//        public void setCreatedBy(java.lang.String createdBy) {
//            this.createdBy = createdBy;
//        }
//        
//        /**
//         * Getter for property authorizeBy.
//         * @return Value of property authorizeBy.
//         */
//        public java.lang.String getAuthorizeBy() {
//            return authorizeBy;
//        }
//        
//        /**
//         * Setter for property authorizeBy.
//         * @param authorizeBy New value of property authorizeBy.
//         */
//        public void setAuthorizeBy(java.lang.String authorizeBy) {
//            this.authorizeBy = authorizeBy;
//        }
//        
//        /**
//         * Getter for property authorizeDt.
//         * @return Value of property authorizeDt.
//         */
//        public java.util.Date getAuthorizeDt() {
//            return authorizeDt;
//        }
//        
//        /**
//         * Setter for property authorizeDt.
//         * @param authorizeDt New value of property authorizeDt.
//         */
//        public void setAuthorizeDt(java.util.Date authorizeDt) {
//            this.authorizeDt = authorizeDt;
//        }
//        
//        /**
//         * Getter for property authorizedStatus.
//         * @return Value of property authorizedStatus.
//         */
//        public java.lang.String getAuthorizedStatus() {
//            return authorizedStatus;
//        }
//        
//        /**
//         * Setter for property authorizedStatus.
//         * @param authorizedStatus New value of property authorizedStatus.
//         */
//        public void setAuthorizedStatus(java.lang.String authorizedStatus) {
//            this.authorizedStatus = authorizedStatus;
//        }
//        
//        /**
//         * Getter for property empName.
//         * @return Value of property empName.
//         */
//        public java.lang.String getEmpName() {
//            return empName;
//        }
//        
//        /**
//         * Setter for property empName.
//         * @param empName New value of property empName.
//         */
//        public void setEmpName(java.lang.String empName) {
//            this.empName = empName;
//        }
//        
//        /**
//         * Getter for property currBranName.
//         * @return Value of property currBranName.
//         */
//        public java.lang.String getCurrBranName() {
//            return currBranName;
//        }
//        
//        /**
//         * Setter for property currBranName.
//         * @param currBranName New value of property currBranName.
//         */
//        public void setCurrBranName(java.lang.String currBranName) {
//            this.currBranName = currBranName;
//        }
//        
//}
