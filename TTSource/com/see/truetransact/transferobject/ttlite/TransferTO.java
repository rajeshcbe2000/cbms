/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition.
 * 
 *
 * TransferTO.java
 * 
 * Created on Tue Jun 01 12:55:06 GMT+05:30 2004
 */
package com.see.truetransact.transferobject.ttlite;

import java.util.Date;
import java.io.Serializable;
import com.see.truetransact.transferobject.TransferObject;

/**
 * Table name for this TO is TRANSFER_TRANS.
 */
public class TransferTO extends TransferObject implements Serializable {
	private String transId = "";
	private String batchId = "";
	private String acHdId = "";
	private String actNum = "";
	private Double inpAmount = null;
	private String inpCurr = "";
	private Double amount = null;
	private Date transDt = null;
	private String transType = "";
	private String instType = "";
	private Date instDt = null;
//	private String tokenNo = "";
	private String initTransId = "";
	private String initChannType = "";
	private String particulars = "";
	private String status = "";
	private String instrumentNo1 = "";
	private String instrumentNo2 = "";
	private String prodId = "";
	private String authorizeStatus = null;
	private String authorizeBy = "";
	private Date authorizeDt = null;
	private String authorizeRemarks = "";
	private String statusBy = "";
	private String branchId = "";
	private Date statusDt = null;
       //ashish------------------------------------
	private String productType = "";
        private String linkBatchId = "";
        private Date linkBatchDt = null;
        private String transMode = "";
        private String initiatedBranch = "";
        private String loanHierarchy = "";
        private String authorizeStatus2 = "";
        private String narration = "";
       //ashish------------------------------------
        /** Setter/Getter for TRANS_ID - table Field*/
	public void setTransId (String transId) {
		this.transId = transId;
	}
	public String getTransId () {
		return transId;
	}

    /**
     * Setter/Getter for BATCH_ID - table Field
     */
    public void setBatchId(String batchId) {
        this.batchId = batchId;
    }

    public String getBatchId() {
        return batchId;
    }

    /**
     * Setter/Getter for AC_HD_ID - table Field
     */
    public void setAcHdId(String acHdId) {
        this.acHdId = acHdId;
    }

    public String getAcHdId() {
        return acHdId;
    }

    /**
     * Setter/Getter for ACT_NUM - table Field
     */
    public void setActNum(String actNum) {
        this.actNum = actNum;
    }

    public String getActNum() {
        return actNum;
    }

    /**
     * Setter/Getter for INP_AMOUNT - table Field
     */
    public void setInpAmount(Double inpAmount) {
        this.inpAmount = inpAmount;
    }

    public Double getInpAmount() {
        return inpAmount;
    }

    /**
     * Setter/Getter for INP_CURR - table Field
     */
    public void setInpCurr(String inpCurr) {
        this.inpCurr = inpCurr;
    }

    public String getInpCurr() {
        return inpCurr;
    }

    /**
     * Setter/Getter for AMOUNT - table Field
     */
    public void setAmount(Double amount) {
        this.amount = amount;
    }

    public Double getAmount() {
        return amount;
    }

    /**
     * Setter/Getter for TRANS_DT - table Field
     */
    public void setTransDt(Date transDt) {
        this.transDt = transDt;
    }

    public Date getTransDt() {
        return transDt;
    }

    /**
     * Setter/Getter for TRANS_TYPE - table Field
     */
    public void setTransType(String transType) {
        this.transType = transType;
    }

    public String getTransType() {
        return transType;
    }

    /**
     * Setter/Getter for INST_TYPE - table Field
     */
    public void setInstType(String instType) {
        this.instType = instType;
    }

    public String getInstType() {
        return instType;
    }

    /**
     * Setter/Getter for INST_DT - table Field
     */
    public void setInstDt(Date instDt) {
        this.instDt = instDt;
    }

    public Date getInstDt() {
        return instDt;
    }

    /** Setter/Getter for TOKEN_NO - table Field*/
//    public void setTokenNo (String tokenNo) {
//            this.tokenNo = tokenNo;
//    }
//    public String getTokenNo () {
//            return tokenNo;
//    }

    /**
     * Setter/Getter for INIT_TRANS_ID - table Field
     */
    public void setInitTransId(String initTransId) {
        this.initTransId = initTransId;
    }

    public String getInitTransId() {
        return initTransId;
    }

    /**
     * Setter/Getter for INIT_CHANN_TYPE - table Field
     */
    public void setInitChannType(String initChannType) {
        this.initChannType = initChannType;
    }

    public String getInitChannType() {
        return initChannType;
    }

    /**
     * Setter/Getter for PARTICULARS - table Field
     */
    public void setParticulars(String particulars) {
        this.particulars = particulars;
    }

    public String getParticulars() {
        return particulars;
    }

    /**
     * Setter/Getter for STATUS - table Field
     */
    public void setStatus(String status) {
        this.status = status;
    }

    public String getStatus() {
        return status;
    }

    /**
     * Setter/Getter for INSTRUMENT_NO1 - table Field
     */
    public void setInstrumentNo1(String instrumentNo1) {
        this.instrumentNo1 = instrumentNo1;
    }

    public String getInstrumentNo1() {
        return instrumentNo1;
    }

    /**
     * Setter/Getter for INSTRUMENT_NO2 - table Field
     */
    public void setInstrumentNo2(String instrumentNo2) {
        this.instrumentNo2 = instrumentNo2;
    }

    public String getInstrumentNo2() {
        return instrumentNo2;
    }

    /**
     * Setter/Getter for PROD_ID - table Field
     */
    public void setProdId(String prodId) {
        this.prodId = prodId;
    }

    public String getProdId() {
        return prodId;
    }

    /**
     * Setter/Getter for AUTHORIZE_STATUS - table Field
     */
    public void setAuthorizeStatus(String authorizeStatus) {
        this.authorizeStatus = authorizeStatus;
    }

    public String getAuthorizeStatus() {
        return authorizeStatus;
    }

    /**
     * Setter/Getter for AUTHORIZE_BY - table Field
     */
    public void setAuthorizeBy(String authorizeBy) {
        this.authorizeBy = authorizeBy;
    }

    public String getAuthorizeBy() {
        return authorizeBy;
    }

    /**
     * Setter/Getter for AUTHORIZE_DT - table Field
     */
    public void setAuthorizeDt(Date authorizeDt) {
        this.authorizeDt = authorizeDt;
    }

    public Date getAuthorizeDt() {
        return authorizeDt;
    }

    /**
     * Setter/Getter for AUTHORIZE_REMARKS - table Field
     */
    public void setAuthorizeRemarks(String authorizeRemarks) {
        this.authorizeRemarks = authorizeRemarks;
    }

    public String getAuthorizeRemarks() {
        return authorizeRemarks;
    }

    /**
     * Setter/Getter for STATUS_BY - table Field
     */
    public void setStatusBy(String statusBy) {
        this.statusBy = statusBy;
    }

    public String getStatusBy() {
        return statusBy;
    }

    /**
     * Setter/Getter for BRANCH_ID - table Field
     */
    public void setBranchId(String branchId) {
        this.branchId = branchId;
    }

    public String getBranchId() {
        return branchId;
    }

	/** Setter/Getter for STATUS_DT - table Field*/
	public void setStatusDt (Date statusDt) {
		this.statusDt = statusDt;
	}
	public Date getStatusDt () {
		return statusDt;
	}
        //ashish---------------------------------------
        /** Setter/Getter for PROD_TYPE - table Field*/
	public void setProductType (String productType) {
		this.productType = productType;
	}
	public String getProductType () {
		return productType;
	}
        /** Setter/Getter for LINK_BATCH_ID - table Field*/ 
        
        public void setLinkBatchId(String linkBatchId ){
                this.linkBatchId = linkBatchId;
        }
        public String getLinkBatchId(){
                return linkBatchId;
        }
        /** Setter/Getter for LINK_BATCH_DT - table Field*/ 
        public void setLinkBatchDt(Date linkBatchDt){
            this.linkBatchDt = linkBatchDt;
        }
        public Date getLinkBatchDt(){
            return linkBatchDt;
        }
         /** Setter/Getter for TRANS_MODE - table Field*/ 
        public void setTransMode(String transMode){
            this.transMode = transMode;
        }
        public String getTransMode(){
            return transMode;
        }
         /** Setter/Getter for INITIATED_BRANCH - table Field*/ 
        public void setInitiatedBranch(String initiatedBranch){
            this.initiatedBranch = initiatedBranch;
        }
        public String getInitiatedBranch(){
            return initiatedBranch;
        }
        /** Setter/Getter for LOAN_HIERARCHY - table Field*/ 
        public void setLoanHierarchy(String loanHierarchy){
            this.loanHierarchy = loanHierarchy;
        }
        public String getLoanHierarchy(){
            return loanHierarchy;
        }
         /** Setter/Getter for AUTHORIZE_STATUS_2 - table Field*/ 
        public void setAuthorizeStatus2(String authorizeStatus2){
            this.authorizeStatus2 = authorizeStatus2;
        }
        public String getAuthorizeStatus2(){
            return authorizeStatus2;
        }
         /** Setter/Getter for NARRATION - table Field*/ 
        public void setNarration(String narration){
            this.narration = narration;
        }
        public String getNarration(){
            return narration;
        }
        //ashish---------------------------------------

	/** getKeyData returns the Primary Key Columns for this TO
	 *  User needs to add the Key columns as a setter 
	 *  Example : 
	 *            setKeyColumns("col1" + KEY_VAL_SEPARATOR + "col2"); 
	 *            return col1 + KEY_VAL_SEPARATOR + col2; 
	 */
	 public String getKeyData() {
		setKeyColumns("");
		return "";
	}

	/** toString method which returns this TO as a String. */
	public String toString() {
		StringBuffer strB = new StringBuffer(getTOStringStart(this.getClass().getName()));
		strB.append (getTOStringKey(getKeyData()));
		strB.append(getTOString("transId", transId));
		strB.append(getTOString("batchId", batchId));
		strB.append(getTOString("acHdId", acHdId));
		strB.append(getTOString("actNum", actNum));
		strB.append(getTOString("inpAmount", inpAmount));
		strB.append(getTOString("inpCurr", inpCurr));
		strB.append(getTOString("amount", amount));
		strB.append(getTOString("transDt", transDt));
		strB.append(getTOString("transType", transType));
		strB.append(getTOString("instType", instType));
		strB.append(getTOString("instDt", instDt));
		//strB.append(getTOString("tokenNo", tokenNo));
		strB.append(getTOString("initTransId", initTransId));
		strB.append(getTOString("initChannType", initChannType));
		strB.append(getTOString("particulars", particulars));
		strB.append(getTOString("status", status));
		strB.append(getTOString("instrumentNo1", instrumentNo1));
		strB.append(getTOString("instrumentNo2", instrumentNo2));
		strB.append(getTOString("prodId", prodId));
		strB.append(getTOString("authorizeStatus", authorizeStatus));
		strB.append(getTOString("authorizeBy", authorizeBy));
		strB.append(getTOString("authorizeDt", authorizeDt));
		strB.append(getTOString("authorizeRemarks", authorizeRemarks));
		strB.append(getTOString("statusBy", statusBy));
		strB.append(getTOString("branchId", branchId));
		strB.append(getTOString("statusDt", statusDt));
                //ashish--------------------------------------
                strB.append(getTOString("productType", productType));
                strB.append(getTOString("linkBatchId", linkBatchId));
                strB.append(getTOString("linkBatchDt", linkBatchDt));
                strB.append(getTOString("transMode", transMode));
                strB.append(getTOString("initiatedBranchId", initiatedBranch));
                strB.append(getTOString("loanHierarchy", loanHierarchy));
                strB.append(getTOString("authorizeStatus2", authorizeStatus2));
                strB.append(getTOString("narration", narration));
                //ashish--------------------------------------
		strB.append(getTOStringEnd());
		return strB.toString();
	}

	/** toXML method which returns this TO as a XML output. */
	public String toXML() {
		StringBuffer strB = new StringBuffer(getTOXmlStart(this.getClass().getName()));
		strB.append (getTOXmlKey(getKeyData()));
		strB.append(getTOXml("transId", transId));
		strB.append(getTOXml("batchId", batchId));
		strB.append(getTOXml("acHdId", acHdId));
		strB.append(getTOXml("actNum", actNum));
		strB.append(getTOXml("inpAmount", inpAmount));
		strB.append(getTOXml("inpCurr", inpCurr));
		strB.append(getTOXml("amount", amount));
		strB.append(getTOXml("transDt", transDt));
		strB.append(getTOXml("transType", transType));
		strB.append(getTOXml("instType", instType));
		strB.append(getTOXml("instDt", instDt));
		//strB.append(getTOXml("tokenNo", tokenNo));
		strB.append(getTOXml("initTransId", initTransId));
		strB.append(getTOXml("initChannType", initChannType));
		strB.append(getTOXml("particulars", particulars));
		strB.append(getTOXml("status", status));
		strB.append(getTOXml("instrumentNo1", instrumentNo1));
		strB.append(getTOXml("instrumentNo2", instrumentNo2));
		strB.append(getTOXml("prodId", prodId));
		strB.append(getTOXml("authorizeStatus", authorizeStatus));
		strB.append(getTOXml("authorizeBy", authorizeBy));
		strB.append(getTOXml("authorizeDt", authorizeDt));
		strB.append(getTOXml("authorizeRemarks", authorizeRemarks));
		strB.append(getTOXml("statusBy", statusBy));
		strB.append(getTOXml("branchId", branchId));
		strB.append(getTOXml("statusDt", statusDt));
                //ashish-------------------------------------
                strB.append(getTOXml("productType", productType));
                strB.append(getTOXml("linkBatchId", linkBatchId));
                strB.append(getTOXml("linkBatchDt", linkBatchDt));
                strB.append(getTOXml("transMode", transMode));
                strB.append(getTOXml("initiatedBranchId", initiatedBranch));
                strB.append(getTOXml("loanHierarchy", loanHierarchy));
                strB.append(getTOXml("authorizeStatus2", authorizeStatus2));
                strB.append(getTOXml("narration", narration));
                //ashish-------------------------------------
		strB.append(getTOXmlEnd());
		return strB.toString();
	}

}