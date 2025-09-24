/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition.  
 * 
 * TradingTO.java
 *
 * Created on 16 September, 2011, 2:24 PM
 */

package com.see.truetransact.transferobject.trading;

import java.io.Serializable;
import com.see.truetransact.transferobject.TransferObject;
import java.util.Date;
/**
 *
 * @author  aravind
 */
public class TradingTO extends TransferObject implements Serializable{
	

	private String batch_id="";
	private String transID = "";
	private String achdID ="";
	private String actNUM ="";
    private Double inpAMOUNT =null;
	private String inpCURR ="";
	private Double aMOUNT =null;
	private Date trans_DT =null;
	private String transTYPE ="";
	private String instTYPE ="";
	private Date instDT =null;
	private String tokenNO ="";
	private String initTRANSID ="";
	private String initCHANNTYPE ="";
	private String pARTICULARS ="";
	private String sTATUS ="";
	private String instrumentNO1 ="";
	private String instrumentNO2 ="";
	private Double availableBALANCE =null;
	private String prodID ="";
	private String prodTYPE ="";
	private String authorizeSTATUS ="";
	private String authorizeBY ="";
	private Date authorizeDT =null;
	private String authorizeREMARKS ="";
	private String statusBY ="";
	private String  branchID ="";
	private Date statusDT =null;
	private String linkBATCHID ="";
	private String initiatedBRANCH ="";
	private Date linkBATCHDT =null;
	private String paymentSTATUS ="";
	private String paymentBY ="";
	private Date paymentSTATUSDT =null;
	private String panNUMBER ="";
	private String authorizeSTATUS2 ="";
	private String authorizeBY2 ="";
	
	private Date authorizeDT2 =null;
	private String transMode="TRANSFER";
	private String loanHIERARCHY ="";
        private String mode ="";
        
        
    /** toString method which returns this TO as a String. */
    public String toString() {
        StringBuffer strB = new StringBuffer(getTOStringStart(this.getClass().getName()));
        //strB.append(getTOStringKey(getKeyData()));
        strB.append(getTOString("batch_id", batch_id));
        strB.append(getTOString("branchID", branchID));
        strB.append(getTOString("transID", transID));
        strB.append(getTOString("achdID", achdID));
        strB.append(getTOString("actNUM", actNUM));
        strB.append(getTOString("transTYPE", transTYPE));
        strB.append(getTOString("sTATUS", sTATUS));
        strB.append(getTOString("statusBY", statusBY));
        strB.append(getTOString("statusDT", statusDT));
        strB.append(getTOString("authorizeSTATUS", authorizeSTATUS));
        strB.append(getTOString("authorizeDT", authorizeDT));
        strB.append(getTOString("authorizeBY", authorizeBY));
        strB.append(getTOString("initiatedBRANCH", initiatedBRANCH));
        strB.append(getTOString("prodID", prodID));
        strB.append(getTOString("prodTYPE", prodTYPE));
        strB.append(getTOString("linkBATCHID", linkBATCHID));
        strB.append(getTOString("paymentSTATUS", paymentSTATUS));
        strB.append(getTOString("authorizeSTATUS2", authorizeSTATUS2));
        strB.append(getTOString("authorizeBY2", authorizeBY2));
        strB.append(getTOString("mode", mode));
        strB.append(getTOString("aMOUNT", aMOUNT));
        strB.append(getTOString("trans_DT",trans_DT));
        strB.append(getTOString("instTYPE",instTYPE));
        strB.append(getTOString("instDT",instDT));
        strB.append(getTOString("authorizeDT2",authorizeDT2));
         strB.append(getTOString("pARTICULARS",pARTICULARS));
         strB.append(getTOString("inpAMOUNT",inpAMOUNT));
        strB.append(getTOStringEnd());
        return strB.toString();
    }
    
    /** toXML method which returns this TO as a XML output. */
    public String toXML() {
        StringBuffer strB = new StringBuffer(getTOXmlStart(this.getClass().getName()));
        //strB.append(getTOXmlKey(getKeyData()));
        strB.append(getTOXml("batch_id", batch_id));
        strB.append(getTOXml("branchID", branchID));
        strB.append(getTOXml("transID", transID));
        strB.append(getTOXml("achdID", achdID));
        strB.append(getTOXml("actNUM", actNUM));
        strB.append(getTOXml("transTYPE", transTYPE));
        strB.append(getTOXml("sTATUS", sTATUS));
        strB.append(getTOXml("statusBY", statusBY));
        strB.append(getTOXml("statusDT", statusDT));
        strB.append(getTOXml("authorizeSTATUS", authorizeSTATUS));
        strB.append(getTOXml("authorizeDT", authorizeDT));
        strB.append(getTOXml("authorizeBY", authorizeBY));
        strB.append(getTOXml("initiatedBRANCH", initiatedBRANCH));
        strB.append(getTOXml("prodID", prodID));
        strB.append(getTOXml("prodTYPE", prodTYPE));
        strB.append(getTOXml("linkBATCHID", linkBATCHID));
        strB.append(getTOXml("paymentSTATUS", paymentSTATUS));
        strB.append(getTOXml("authorizeSTATUS2", authorizeSTATUS2));
        strB.append(getTOXml("authorizeBY2", authorizeBY2));
        strB.append(getTOXml("mode", mode));
        strB.append(getTOXml("aMOUNT", aMOUNT));
        strB.append(getTOXml("trans_DT",trans_DT));
        strB.append(getTOXml("instTYPE",instTYPE));
        strB.append(getTOXml("instDT",instDT));
        strB.append(getTOXml("authorizeDT2",authorizeDT2));
        strB.append(getTOXml("pARTICULARS",pARTICULARS));
        strB.append(getTOXml("inpAMOUNT",inpAMOUNT));
        strB.append(getTOXmlEnd());
        return strB.toString();
    }
       // private String batchId="";
        /**
         * Getter for property transID.
         * @return Value of property transID.
         */
        public String getTransID() {
            return transID;
        }        
	
        /**
         * Setter for property transID.
         * @param transID New value of property transID.
         */
        public void setTransID(String transID) {
            this.transID = transID;
        }        
	
        /**
         * Getter for property achdID.
         * @return Value of property achdID.
         */
        public String getAchdID() {
            return achdID;
        }        
	
        /**
         * Setter for property achdID.
         * @param achdID New value of property achdID.
         */
        public void setAchdID(String achdID) {
            this.achdID = achdID;
        }        

        /**
         * Getter for property actNUM.
         * @return Value of property actNUM.
         */
        public String getActNUM() {
            return actNUM;
        }
        
        /**
         * Setter for property actNUM.
         * @param actNUM New value of property actNUM.
         */
        public void setActNUM(String actNUM) {
            this.actNUM = actNUM;
        }
        
        /**
         * Getter for property inpAMOUNT.
         * @return Value of property inpAMOUNT.
         */
        public Double getInpAMOUNT() {
            return inpAMOUNT;
        }
        
        /**
         * Setter for property inpAMOUNT.
         * @param inpAMOUNT New value of property inpAMOUNT.
         */
        public void setInpAMOUNT(Double inpAMOUNT) {
            this.inpAMOUNT = inpAMOUNT;
        }
        
        /**
         * Getter for property inpCURR.
         * @return Value of property inpCURR.
         */
        public String getInpCURR() {
            return inpCURR;
        }
        
        /**
         * Setter for property inpCURR.
         * @param inpCURR New value of property inpCURR.
         */
        public void setInpCURR(String inpCURR) {
            this.inpCURR = inpCURR;
        }
        
        /**
         * Getter for property aMOUNT.
         * @return Value of property aMOUNT.
         */
        public Double getAMOUNT() {
            return aMOUNT;
        }
        
        /**
         * Setter for property aMOUNT.
         * @param aMOUNT New value of property aMOUNT.
         */
        public void setAMOUNT(Double aMOUNT) {
            this.aMOUNT = aMOUNT;
        }
        
        /**
         * Getter for property trans_DT.
         * @return Value of property trans_DT.
         */
        public Date getTrans_DT() {
            return trans_DT;
        }
        
        /**
         * Setter for property trans_DT.
         * @param trans_DT New value of property trans_DT.
         */
        public void setTrans_DT(Date trans_DT) {
            this.trans_DT = trans_DT;
        }
        
        /**
         * Getter for property transTYPE.
         * @return Value of property transTYPE.
         */
        public String getTransTYPE() {
            return transTYPE;
        }
        
        /**
         * Setter for property transTYPE.
         * @param transTYPE New value of property transTYPE.
         */
        public void setTransTYPE(String transTYPE) {
            this.transTYPE = transTYPE;
        }
        
        /**
         * Getter for property instTYPE.
         * @return Value of property instTYPE.
         */
        public String getInstTYPE() {
            return instTYPE;
        }
        
        /**
         * Setter for property instTYPE.
         * @param instTYPE New value of property instTYPE.
         */
        public void setInstTYPE(String instTYPE) {
            this.instTYPE = instTYPE;
        }
        
        /**
         * Getter for property instDT.
         * @return Value of property instDT.
         */
        public Date getInstDT() {
            return instDT;
        }
        
        /**
         * Setter for property instDT.
         * @param instDT New value of property instDT.
         */
        public void setInstDT(Date instDT) {
            this.instDT = instDT;
        }
        
        /**
         * Getter for property tokenNO.
         * @return Value of property tokenNO.
         */
        public String getTokenNO() {
            return tokenNO;
        }
        
        /**
         * Setter for property tokenNO.
         * @param tokenNO New value of property tokenNO.
         */
        public void setTokenNO(String tokenNO) {
            this.tokenNO = tokenNO;
        }
        
        /**
         * Getter for property initTRANSID.
         * @return Value of property initTRANSID.
         */
        public String getInitTRANSID() {
            return initTRANSID;
        }
        
        /**
         * Setter for property initTRANSID.
         * @param initTRANSID New value of property initTRANSID.
         */
        public void setInitTRANSID(String initTRANSID) {
            this.initTRANSID = initTRANSID;
        }
        
        /**
         * Getter for property initCHANNTYPE.
         * @return Value of property initCHANNTYPE.
         */
        public String getInitCHANNTYPE() {
            return initCHANNTYPE;
        }
        
        /**
         * Setter for property initCHANNTYPE.
         * @param initCHANNTYPE New value of property initCHANNTYPE.
         */
        public void setInitCHANNTYPE(String initCHANNTYPE) {
            this.initCHANNTYPE = initCHANNTYPE;
        }
        
        /**
         * Getter for property pARTICULARS.
         * @return Value of property pARTICULARS.
         */
        public String getPARTICULARS() {
            return pARTICULARS;
        }
        
        /**
         * Setter for property pARTICULARS.
         * @param pARTICULARS New value of property pARTICULARS.
         */
        public void setPARTICULARS(String pARTICULARS) {
            this.pARTICULARS = pARTICULARS;
        }
        
        /**
         * Getter for property sTATUS.
         * @return Value of property sTATUS.
         */
        public String getSTATUS() {
            return sTATUS;
        }
        
        /**
         * Setter for property sTATUS.
         * @param sTATUS New value of property sTATUS.
         */
        public void setSTATUS(String sTATUS) {
            this.sTATUS = sTATUS;
        }
        
        /**
         * Getter for property instrumentNO1.
         * @return Value of property instrumentNO1.
         */
        public String getInstrumentNO1() {
            return instrumentNO1;
        }
        
        /**
         * Setter for property instrumentNO1.
         * @param instrumentNO1 New value of property instrumentNO1.
         */
        public void setInstrumentNO1(String instrumentNO1) {
            this.instrumentNO1 = instrumentNO1;
        }
        
        /**
         * Getter for property instrumentNO2.
         * @return Value of property instrumentNO2.
         */
        public String getInstrumentNO2() {
            return instrumentNO2;
        }
        
        /**
         * Setter for property instrumentNO2.
         * @param instrumentNO2 New value of property instrumentNO2.
         */
        public void setInstrumentNO2(String instrumentNO2) {
            this.instrumentNO2 = instrumentNO2;
        }
        
        /**
         * Getter for property availableBALANCE.
         * @return Value of property availableBALANCE.
         */
        public Double getAvailableBALANCE() {
            return availableBALANCE;
        }
        
        /**
         * Setter for property availableBALANCE.
         * @param availableBALANCE New value of property availableBALANCE.
         */
        public void setAvailableBALANCE(Double availableBALANCE) {
            this.availableBALANCE = availableBALANCE;
        }
        
        /**
         * Getter for property prodID.
         * @return Value of property prodID.
         */
        public String getProdID() {
            return prodID;
        }
        
        /**
         * Setter for property prodID.
         * @param prodID New value of property prodID.
         */
        public void setProdID(String prodID) {
            this.prodID = prodID;
        }
        
        /**
         * Getter for property prodTYPE.
         * @return Value of property prodTYPE.
         */
        public String getProdTYPE() {
            return prodTYPE;
        }
        
        /**
         * Setter for property prodTYPE.
         * @param prodTYPE New value of property prodTYPE.
         */
        public void setProdTYPE(String prodTYPE) {
            this.prodTYPE = prodTYPE;
        }
        
        /**
         * Getter for property authorizeSTATUS.
         * @return Value of property authorizeSTATUS.
         */
        public String getAuthorizeSTATUS() {
            return authorizeSTATUS;
        }
        
        /**
         * Setter for property authorizeSTATUS.
         * @param authorizeSTATUS New value of property authorizeSTATUS.
         */
        public void setAuthorizeSTATUS(String authorizeSTATUS) {
            this.authorizeSTATUS = authorizeSTATUS;
        }
        
        /**
         * Getter for property authorizeBY.
         * @return Value of property authorizeBY.
         */
        public String getAuthorizeBY() {
            return authorizeBY;
        }
        
        /**
         * Setter for property authorizeBY.
         * @param authorizeBY New value of property authorizeBY.
         */
        public void setAuthorizeBY(String authorizeBY) {
            this.authorizeBY = authorizeBY;
        }
        
        /**
         * Getter for property authorizeDT.
         * @return Value of property authorizeDT.
         */
        public Date getAuthorizeDT() {
            return authorizeDT;
        }
        
        /**
         * Setter for property authorizeDT.
         * @param authorizeDT New value of property authorizeDT.
         */
        public void setAuthorizeDT(Date authorizeDT) {
            this.authorizeDT = authorizeDT;
        }
        
        /**
         * Getter for property authorizeREMARKS.
         * @return Value of property authorizeREMARKS.
         */
        public String getAuthorizeREMARKS() {
            return authorizeREMARKS;
        }
        
        /**
         * Setter for property authorizeREMARKS.
         * @param authorizeREMARKS New value of property authorizeREMARKS.
         */
        public void setAuthorizeREMARKS(String authorizeREMARKS) {
            this.authorizeREMARKS = authorizeREMARKS;
        }
        
        /**
         * Getter for property statusBY.
         * @return Value of property statusBY.
         */
        public String getStatusBY() {
            return statusBY;
        }
        
        /**
         * Setter for property statusBY.
         * @param statusBY New value of property statusBY.
         */
        public void setStatusBY(String statusBY) {
            this.statusBY = statusBY;
        }
        
        /**
         * Getter for property branchID.
         * @return Value of property branchID.
         */
        public String getBranchID() {
            return branchID;
        }
        
        /**
         * Setter for property branchID.
         * @param branchID New value of property branchID.
         */
        public void setBranchID(String branchID) {
            this.branchID = branchID;
        }
        
        /**
         * Getter for property statusDT.
         * @return Value of property statusDT.
         */
        public Date getStatusDT() {
            return statusDT;
        }
        
        /**
         * Setter for property statusDT.
         * @param statusDT New value of property statusDT.
         */
        public void setStatusDT(Date statusDT) {
            this.statusDT = statusDT;
        }
        
        /**
         * Getter for property linkBATCHID.
         * @return Value of property linkBATCHID.
         */
        public String getLinkBATCHID() {
            return linkBATCHID;
        }
        
        /**
         * Setter for property linkBATCHID.
         * @param linkBATCHID New value of property linkBATCHID.
         */
        public void setLinkBATCHID(String linkBATCHID) {
            this.linkBATCHID = linkBATCHID;
        }
        
        /**
         * Getter for property initiatedBRANCH.
         * @return Value of property initiatedBRANCH.
         */
        public String getInitiatedBRANCH() {
            return initiatedBRANCH;
        }
        
        /**
         * Setter for property initiatedBRANCH.
         * @param initiatedBRANCH New value of property initiatedBRANCH.
         */
        public void setInitiatedBRANCH(String initiatedBRANCH) {
            this.initiatedBRANCH = initiatedBRANCH;
        }
        
        /**
         * Getter for property linkBATCHDT.
         * @return Value of property linkBATCHDT.
         */
        public Date getLinkBATCHDT() {
            return linkBATCHDT;
        }
        
        /**
         * Setter for property linkBATCHDT.
         * @param linkBATCHDT New value of property linkBATCHDT.
         */
        public void setLinkBATCHDT(Date linkBATCHDT) {
            this.linkBATCHDT = linkBATCHDT;
        }
        
        /**
         * Getter for property paymentSTATUS.
         * @return Value of property paymentSTATUS.
         */
        public String getPaymentSTATUS() {
            return paymentSTATUS;
        }
        
        /**
         * Setter for property paymentSTATUS.
         * @param paymentSTATUS New value of property paymentSTATUS.
         */
        public void setPaymentSTATUS(String paymentSTATUS) {
            this.paymentSTATUS = paymentSTATUS;
        }
        
        /**
         * Getter for property paymentBY.
         * @return Value of property paymentBY.
         */
        public String getPaymentBY() {
            return paymentBY;
        }
        
        /**
         * Setter for property paymentBY.
         * @param paymentBY New value of property paymentBY.
         */
        public void setPaymentBY(String paymentBY) {
            this.paymentBY = paymentBY;
        }
        
        /**
         * Getter for property paymentSTATUSDT.
         * @return Value of property paymentSTATUSDT.
         */
        public Date getPaymentSTATUSDT() {
            return paymentSTATUSDT;
        }
        
        /**
         * Setter for property paymentSTATUSDT.
         * @param paymentSTATUSDT New value of property paymentSTATUSDT.
         */
        public void setPaymentSTATUSDT(Date paymentSTATUSDT) {
            this.paymentSTATUSDT = paymentSTATUSDT;
        }
        
        /**
         * Getter for property panNUMBER.
         * @return Value of property panNUMBER.
         */
        public String getPanNUMBER() {
            return panNUMBER;
        }
        
        /**
         * Setter for property panNUMBER.
         * @param panNUMBER New value of property panNUMBER.
         */
        public void setPanNUMBER(String panNUMBER) {
            this.panNUMBER = panNUMBER;
        }
        
        /**
         * Getter for property authorizeSTATUS2.
         * @return Value of property authorizeSTATUS2.
         */
        public String getAuthorizeSTATUS2() {
            return authorizeSTATUS2;
        }
        
        /**
         * Setter for property authorizeSTATUS2.
         * @param authorizeSTATUS2 New value of property authorizeSTATUS2.
         */
        public void setAuthorizeSTATUS2(String authorizeSTATUS2) {
            this.authorizeSTATUS2 = authorizeSTATUS2;
        }
        
        /**
         * Getter for property authorizeBY2.
         * @return Value of property authorizeBY2.
         */
        public String getAuthorizeBY2() {
            return authorizeBY2;
        }
        
        /**
         * Setter for property authorizeBY2.
         * @param authorizeBY2 New value of property authorizeBY2.
         */
        public void setAuthorizeBY2(String authorizeBY2) {
            this.authorizeBY2 = authorizeBY2;
        }
        
        /**
         * Getter for property authorizeDT2.
         * @return Value of property authorizeDT2.
         */
        public Date getAuthorizeDT2() {
            return authorizeDT2;
        }
        
        /**
         * Setter for property authorizeDT2.
         * @param authorizeDT2 New value of property authorizeDT2.
         */
        public void setAuthorizeDT2(Date authorizeDT2) {
            this.authorizeDT2 = authorizeDT2;
        }
        
        /**
         * Getter for property loanHIERARCHY.
         * @return Value of property loanHIERARCHY.
         */
        public String getLoanHIERARCHY() {
            return loanHIERARCHY;
        }
        
        /**
         * Setter for property loanHIERARCHY.
         * @param loanHIERARCHY New value of property loanHIERARCHY.
         */
        public void setLoanHIERARCHY(String loanHIERARCHY) {
            this.loanHIERARCHY = loanHIERARCHY;
        }
        
        /**
         * Getter for property mode.
         * @return Value of property mode.
         */
        public java.lang.String getMode() {
            return mode;
        }
        
        /**
         * Setter for property mode.
         * @param mode New value of property mode.
         */
        public void setMode(java.lang.String mode) {
            this.mode = mode;
        }
        
        /**
         * Getter for property transMode.
         * @return Value of property transMode.
         */
        public java.lang.String getTransMode() {
            return transMode;
        }
        
        /**
         * Setter for property transMode.
         * @param transMode New value of property transMode.
         */
        public void setTransMode(java.lang.String transMode) {
            this.transMode = transMode;
        }
        
        /**
         * Getter for property batch_id.
         * @return Value of property batch_id.
         */
        public java.lang.String getBatch_id() {
            return batch_id;
        }        
      
        /**
         * Setter for property batch_id.
         * @param batch_id New value of property batch_id.
         */
        public void setBatch_id(java.lang.String batch_id) {
            this.batch_id = batch_id;
        }        
        
}
