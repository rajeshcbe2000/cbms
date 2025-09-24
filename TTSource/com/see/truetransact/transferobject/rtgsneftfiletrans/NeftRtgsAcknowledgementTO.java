
/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition.  
 * 
 * NeftRtgsAcknowledgementTO.java
 */

package com.see.truetransact.transferobject.rtgsneftfiletrans;

import com.see.truetransact.transferobject.TransferObject;
import java.io.Serializable;
import java.util.Date;

/**
 *
 * @author Admin
 */
public class NeftRtgsAcknowledgementTO  extends TransferObject implements Serializable {
    
    String acctNum ="";
    String acctStatus="";
    Date ackDate=null;
    Double amount =null;
    String initBranchId="";
    String homeIfsCode="";
    String otherBrankIfsCode="";
    String remarks="";
    String status ="";
    String inwardUTR="" ;
    String outwardUTR="";
    String otherBankAcctNum="";
    String otherBankCustName="";
    String prodid="";
    String beneficiaryName="";
    String batch_id="";
    String inwardFAilAckUTR="";
    String fileName ="";
    String client_Code ="";
    String cr_Acct_Num ="";
    String beneficiary_Bank ="";
    Date valueDate = null;
    String corporateCode = "";

    public Date getValueDate() {
        return valueDate;
    }

    public void setValueDate(Date valueDate) {
        this.valueDate = valueDate;
    }

    public String getCorporateCode() {
        return corporateCode;
    }

    public void setCorporateCode(String corporateCode) {
        this.corporateCode = corporateCode;
    }
    
    public String getKeyData() {
		//setKeyColumns(borrowingNo);
		return acctNum;
	}
    
    public String toString() {
        StringBuffer strB = new StringBuffer(getTOStringStart(this.getClass().getName()));
        strB.append(getTOStringKey(getKeyData()));
        strB.append(getTOString("acctNum", acctNum));
        strB.append(getTOString("acctStatus", acctStatus));
        strB.append(getTOString("ackDate", ackDate));
        strB.append(getTOString("amount", amount));
        strB.append(getTOString("initBranchId", initBranchId));
        strB.append(getTOString("homeIfsCode", homeIfsCode));
        strB.append(getTOString("otherBrankIfsCode", otherBrankIfsCode));
        strB.append(getTOString("remarks", remarks));
        strB.append(getTOString("status", status));
        strB.append(getTOString("inwardUTR", inwardUTR));
        strB.append(getTOString("outwardUTR", outwardUTR));
        strB.append(getTOString("otherBankAcctNum", otherBankAcctNum));
        strB.append(getTOString("otherBankCustName", otherBankCustName));
        strB.append(getTOString("prodid", prodid));
        strB.append(getTOString("beneficiaryName", beneficiaryName));
        strB.append(getTOString("batch_id", batch_id));
        strB.append(getTOString("inwardFAilAckUTR", inwardFAilAckUTR));
        strB.append(getTOString("fileName", fileName));
        strB.append(getTOString("client_Code", client_Code));
        strB.append(getTOString("cr_Acct_Num", cr_Acct_Num));
        strB.append(getTOString("beneficiary_Bank", beneficiary_Bank));
        strB.append(getTOString("valueDate", valueDate));
        strB.append(getTOString("corporateCode", corporateCode));
        strB.append(getTOStringEnd());
        return strB.toString();
                
    }
    
    public String toXML() {
        StringBuffer strB = new StringBuffer(getTOXmlStart(this.getClass().getName()));
        strB.append(getTOXmlKey(getKeyData()));
        strB.append(getTOXml("acctNum", acctNum));
        strB.append(getTOXml("acctStatus", acctStatus));
        strB.append(getTOXml("ackDate", ackDate));
        strB.append(getTOXml("amount", amount));
        strB.append(getTOXml("initBranchId", initBranchId));
        strB.append(getTOXml("homeIfsCode", homeIfsCode));
        strB.append(getTOXml("otherBrankIfsCode", otherBrankIfsCode));
        strB.append(getTOXml("remarks", remarks));
        strB.append(getTOXml("status", status));
        strB.append(getTOXml("inwardUTR", inwardUTR));
        strB.append(getTOXml("outwardUTR", outwardUTR));
        strB.append(getTOXml("otherBankAcctNum", otherBankAcctNum));
        strB.append(getTOXml("otherBankCustName", otherBankCustName));
        strB.append(getTOXml("prodid", prodid));
        strB.append(getTOXml("beneficiaryName", beneficiaryName));
        strB.append(getTOXml("batch_id", batch_id));
        strB.append(getTOXml("inwardFAilAckUTR", inwardFAilAckUTR));
        strB.append(getTOXml("fileName", fileName));
        strB.append(getTOXml("client_Code", client_Code));
        strB.append(getTOXml("cr_Acct_Num", cr_Acct_Num));
        strB.append(getTOXml("beneficiary_Bank", beneficiary_Bank));
        strB.append(getTOXml("valueDate", valueDate));
        strB.append(getTOXml("corporateCode", corporateCode));
        strB.append(getTOXmlEnd());
        return strB.toString();
                
    }
    
    public String getAcctNum() {
        return acctNum;
    }

    public void setAcctNum(String acctNum) {
        this.acctNum = acctNum;
    }

    public String getAcctStatus() {
        return acctStatus;
    }

    public void setAcctStatus(String acctStatus) {
        this.acctStatus = acctStatus;
    }

    

    public Double getAmount() {
        return amount;
    }

    public void setAmount(Double amount) {
        this.amount = amount;
    }

    public String getHomeIfsCode() {
        return homeIfsCode;
    }

    public void setHomeIfsCode(String homeIfsCode) {
        this.homeIfsCode = homeIfsCode;
    }

    public String getInitBranchId() {
        return initBranchId;
    }

    public void setInitBranchId(String initBranchId) {
        this.initBranchId = initBranchId;
    }

    public String getOtherBrankIfsCode() {
        return otherBrankIfsCode;
    }

    public void setOtherBrankIfsCode(String otherBrankIfsCode) {
        this.otherBrankIfsCode = otherBrankIfsCode;
    }

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

     public String getInwardUTR() {
        return inwardUTR;
    }

    public void setInwardUTR(String inwardUTR) {
        this.inwardUTR = inwardUTR;
    }

    public String getOutwardUTR() {
        return outwardUTR;
    }

    public void setOutwardUTR(String outwardUTR) {
        this.outwardUTR = outwardUTR;
    }
    public String getOtherBankAcctNum() {
        return otherBankAcctNum;
    }

    public void setOtherBankAcctNum(String otherBankAcctNum) {
        this.otherBankAcctNum = otherBankAcctNum;
    }

    public Date getAckDate() {
        return ackDate;
    }

    public void setAckDate(Date ackDate) {
        this.ackDate = ackDate;
    }

    public String getBeneficiaryName() {
        return beneficiaryName;
    }

    public void setBeneficiaryName(String beneficiaryName) {
        this.beneficiaryName = beneficiaryName;
    }

    public String getOtherBankCustName() {
        return otherBankCustName;
    }

    public void setOtherBankCustName(String otherBankCustName) {
        this.otherBankCustName = otherBankCustName;
    }

    public String getProdid() {
        return prodid;
    }

    public void setProdid(String prodid) {
        this.prodid = prodid;
    }
    public String getBatch_id() {
        return batch_id;
    }

    public void setBatch_id(String batch_id) {
        this.batch_id = batch_id;
    }

    public String getInwardFAilAckUTR() {
        return inwardFAilAckUTR;
    }

    public void setInwardFAilAckUTR(String inwardFAilAckUTR) {
        this.inwardFAilAckUTR = inwardFAilAckUTR;
    }


    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getClient_Code() {
        return client_Code;
    }

    public void setClient_Code(String client_Code) {
        this.client_Code = client_Code;
    }

    public String getCr_Acct_Num() {
        return cr_Acct_Num;
    }

    public void setCr_Acct_Num(String cr_Acct_Num) {
        this.cr_Acct_Num = cr_Acct_Num;
    }

    public String getBeneficiary_Bank() {
        return beneficiary_Bank;
    }

    public void setBeneficiary_Bank(String beneficiary_Bank) {
        this.beneficiary_Bank = beneficiary_Bank;
    }

    
}


 