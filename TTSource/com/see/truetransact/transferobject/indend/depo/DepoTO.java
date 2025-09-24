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
package com.see.truetransact.transferobject.indend.depo;

import java.util.Date;
import java.io.Serializable;
import com.see.truetransact.transferobject.TransferObject;

/**
 * Table name for this TO is TOKEN_CONFIG.
 */
public class DepoTO extends TransferObject implements Serializable {

    private String salesId = "", storeId = "", depId = "", 
            remarks = "", salesachdId = "", deponame = "",
            purchaseachdId = "", purretachdId = "", salretachdId = "", damageachdId = "",
            serviceTaxachdId = "", vatachdId = "", deficiateachdId = "",
            misIncomegpHead = "", comReciedgpHd = "", purVatTaxgpHead = "", purRetrnVatgpHead = "", saleVatTaxgpHead = "", saleRetrnVatTaxgpHead = "",
            damageVatgpHead = "", deficitVatgpHead = "", otherExpnsgpHead = "", stockHd="";
    Double opngstock, vatOpngStock;
    private Double profitPercentage = null;
    private String authorizeStatus = null;
    private String authorizeBy = null;
    private Date authorizeDte = null, stkasondate = null;
    private String status = null;
    private String discountHead = ""; // Added by nithya on 02-04-2020 for KD-1732
    private String discountVatHead = "";

    /**
     * getKeyData returns the Primary Key Columns for this TO User needs to add
     * the Key columns as a setter Example : setKeyColumns("col1" +
     * KEY_VAL_SEPARATOR + "col2"); return col1 + KEY_VAL_SEPARATOR + col2;
     */
    public String getKeyData() {
        //setKeyColumns(borrowingNo);
        return depId;
    }

    /**
     * toString method which returns this TO as a String.
     */
    public String toString() {
        StringBuffer strB = new StringBuffer(getTOStringStart(this.getClass().getName()));
        strB.append(getTOStringKey(getKeyData()));
        strB.append(getTOString("depId", depId));
        strB.append(getTOString("salesId", salesId));
        strB.append(getTOString("storeId", storeId));
        strB.append(getTOString("profitPercentage", profitPercentage));
        strB.append(getTOString("remarks", remarks));
        strB.append(getTOString("salesachdId", salesachdId));
        strB.append(getTOString("deponame", deponame));
        strB.append(getTOString("opngstock", opngstock));
        strB.append(getTOString("stkasondate", stkasondate));
        strB.append(getTOString("purchaseachdId", purchaseachdId));
        strB.append(getTOString("salretachdId", salretachdId));
        strB.append(getTOString("damageachdId", damageachdId));
        strB.append(getTOString("serviceTaxachdId", serviceTaxachdId));
        strB.append(getTOString("vatachdId", vatachdId));
        strB.append(getTOString("deficiateachdId", deficiateachdId));
        strB.append(getTOString("misIncomegpHead", misIncomegpHead));
        strB.append(getTOString("comReciedgpHd", comReciedgpHd));
        strB.append(getTOString("purVatTaxgpHead", purVatTaxgpHead));
        strB.append(getTOString("stockHd", stockHd));
        strB.append(getTOString("purRetrnVatgpHead", purRetrnVatgpHead));
        strB.append(getTOString("saleVatTaxgpHead", saleVatTaxgpHead));
        strB.append(getTOString("saleRetrnVatTaxgpHead", saleRetrnVatTaxgpHead));
        strB.append(getTOString("damageVatgpHead", damageVatgpHead));
        strB.append(getTOString("deficitVatgpHead", deficitVatgpHead));
        strB.append(getTOString("otherExpnsgpHead", otherExpnsgpHead));
        strB.append(getTOString("authorizeStatus", authorizeStatus));
        strB.append(getTOString("authorizeBy", authorizeBy));
        strB.append(getTOString("authorizeDte", authorizeDte));
        strB.append(getTOString("status", status));
        strB.append(getTOString("discountHead", discountHead));
        strB.append(getTOString("discountVatHead", discountVatHead));
        strB.append(getTOStringEnd());
        return strB.toString();
    }

    /**
     * toXML method which returns this TO as a XML output.
     */
    public String toXML() {
        StringBuffer strB = new StringBuffer(getTOXmlStart(this.getClass().getName()));
        strB.append(getTOXmlKey(getKeyData()));
        strB.append(getTOXml("depId ", depId));
        strB.append(getTOXml("salesId", salesId));
        strB.append(getTOXml("storeId", storeId));
        strB.append(getTOXml("profitPercentage", profitPercentage));
        strB.append(getTOXml("remarks", remarks));
        strB.append(getTOXml("salesachdId", salesachdId));
        strB.append(getTOXml("deponame", deponame));
        strB.append(getTOXml("opngstock", opngstock));
        strB.append(getTOXml("stkasondate", stkasondate));
        strB.append(getTOXml("purchaseachdId", purchaseachdId));
        strB.append(getTOXml("salretachdId", salretachdId));
        strB.append(getTOXml("damageachdId", damageachdId));
        strB.append(getTOXml("serviceTaxachdId", serviceTaxachdId));
        strB.append(getTOXml("vatachdId", vatachdId));
        strB.append(getTOXml("deficiateachdId", deficiateachdId));
        strB.append(getTOXml("misIncomegpHead", misIncomegpHead));
        strB.append(getTOXml("comReciedgpHd", comReciedgpHd));
        strB.append(getTOXml("purVatTaxgpHead", purVatTaxgpHead));
        strB.append(getTOXml("stockHd", stockHd));
        strB.append(getTOXml("purRetrnVatgpHead", purRetrnVatgpHead));
        strB.append(getTOXml("saleVatTaxgpHead", saleVatTaxgpHead));
        strB.append(getTOXml("saleRetrnVatTaxgpHead", saleRetrnVatTaxgpHead));
        strB.append(getTOXml("damageVatgpHead", damageVatgpHead));
        strB.append(getTOXml("deficitVatgpHead", deficitVatgpHead));
        strB.append(getTOXml("otherExpnsgpHead", otherExpnsgpHead));
        strB.append(getTOXml("authorizeStatus", authorizeStatus));
        strB.append(getTOXml("authorizeBy", authorizeBy));
        strB.append(getTOXml("authorizeDte", authorizeDte));
        strB.append(getTOXml("status", status));
        strB.append(getTOXml("discountHead", discountHead));
        strB.append(getTOXml("discountVatHead", discountVatHead));
        strB.append(getTOXmlEnd());
        return strB.toString();
    }

    public String getComReciedgpHd() {
        return comReciedgpHd;
    }

    public void setComReciedgpHd(String comReciedgpHd) {
        this.comReciedgpHd = comReciedgpHd;
    }

    public String getDamageVatgpHead() {
        return damageVatgpHead;
    }

    public void setDamageVatgpHead(String damageVatgpHead) {
        this.damageVatgpHead = damageVatgpHead;
    }

    public String getDeficitVatgpHead() {
        return deficitVatgpHead;
    }

    public void setDeficitVatgpHead(String deficitVatgpHead) {
        this.deficitVatgpHead = deficitVatgpHead;
    }

    public String getMisIncomegpHead() {
        return misIncomegpHead;
    }

    public void setMisIncomegpHead(String misIncomegpHead) {
        this.misIncomegpHead = misIncomegpHead;
    }

    public String getOtherExpnsgpHead() {
        return otherExpnsgpHead;
    }

    public void setOtherExpnsgpHead(String otherExpnsgpHead) {
        this.otherExpnsgpHead = otherExpnsgpHead;
    }

    public String getPurRetrnVatgpHead() {
        return purRetrnVatgpHead;
    }

    public void setPurRetrnVatgpHead(String purRetrnVatgpHead) {
        this.purRetrnVatgpHead = purRetrnVatgpHead;
    }

    public String getPurVatTaxgpHead() {
        return purVatTaxgpHead;
    }

    public void setPurVatTaxgpHead(String purVatTaxgpHead) {
        this.purVatTaxgpHead = purVatTaxgpHead;
    }

    public String getSaleRetrnVatTaxgpHead() {
        return saleRetrnVatTaxgpHead;
    }

    public void setSaleRetrnVatTaxgpHead(String saleRetrnVatTaxgpHead) {
        this.saleRetrnVatTaxgpHead = saleRetrnVatTaxgpHead;
    }

    public String getSaleVatTaxgpHead() {
        return saleVatTaxgpHead;
    }

    public void setSaleVatTaxgpHead(String saleVatTaxgpHead) {
        this.saleVatTaxgpHead = saleVatTaxgpHead;
    }

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
     * Getter for property salesId.
     *
     * @return Value of property salesId.
     */
    public java.lang.String getSalesId() {
        return salesId;
    }

    /**
     * Setter for property salesId.
     *
     * @param salesId New value of property salesId.
     */
    public void setSalesId(java.lang.String salesId) {
        this.salesId = salesId;
    }

    /**
     * Getter for property storeId.
     *
     * @return Value of property storeId.
     */
    public java.lang.String getStoreId() {
        return storeId;
    }

    /**
     * Setter for property storeId.
     *
     * @param storeId New value of property storeId.
     */
    public void setStoreId(java.lang.String storeId) {
        this.storeId = storeId;
    }

    /**
     * Getter for property depId.
     *
     * @return Value of property depId.
     */
    public java.lang.String getDepId() {
        return depId;
    }

    /**
     * Setter for property depId.
     *
     * @param depId New value of property depId.
     */
    public void setDepId(java.lang.String depId) {
        this.depId = depId;
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

    /**
     * Getter for property salesachdId.
     *
     * @return Value of property salesachdId.
     */
    public java.lang.String getSalesachdId() {
        return salesachdId;
    }

    /**
     * Setter for property salesachdId.
     *
     * @param salesachdId New value of property salesachdId.
     */
    public void setSalesachdId(java.lang.String salesachdId) {
        this.salesachdId = salesachdId;
    }

    /**
     * Getter for property deponame.
     *
     * @return Value of property deponame.
     */
    public java.lang.String getDeponame() {
        return deponame;
    }

    /**
     * Setter for property deponame.
     *
     * @param deponame New value of property deponame.
     */
    public void setDeponame(java.lang.String deponame) {
        this.deponame = deponame;
    }

    /**
     * Getter for property opngstock.
     *
     * @return Value of property opngstock.
     */
    public Double getOpngstock() {
        return opngstock;
    }

    /**
     * Setter for property opngstock.
     *
     * @param opngstock New value of property opngstock.
     */
    public void setOpngstock(Double opngstock) {
        this.opngstock = opngstock;
    }

    /**
     * Getter for property stkasondate.
     *
     * @return Value of property stkasondate.
     */
    public Date getStkasondate() {
        return stkasondate;
    }

    /**
     * Setter for property stkasondate.
     *
     * @param stkasondate New value of property stkasondate.
     */
    public void setStkasondate(Date stkasondate) {
        this.stkasondate = stkasondate;
    }

    /**
     * Getter for property purchaseachdId.
     *
     * @return Value of property purchaseachdId.
     */
    public java.lang.String getPurchaseachdId() {
        return purchaseachdId;
    }

    /**
     * Setter for property purchaseachdId.
     *
     * @param purchaseachdId New value of property purchaseachdId.
     */
    public void setPurchaseachdId(java.lang.String purchaseachdId) {
        this.purchaseachdId = purchaseachdId;
    }

    /**
     * Getter for property purretachdId.
     *
     * @return Value of property purretachdId.
     */
    public java.lang.String getPurretachdId() {
        return purretachdId;
    }

    /**
     * Setter for property purretachdId.
     *
     * @param purretachdId New value of property purretachdId.
     */
    public void setPurretachdId(java.lang.String purretachdId) {
        this.purretachdId = purretachdId;
    }

    /**
     * Getter for property salretachdId.
     *
     * @return Value of property salretachdId.
     */
    public java.lang.String getSalretachdId() {
        return salretachdId;
    }

    /**
     * Setter for property salretachdId.
     *
     * @param salretachdId New value of property salretachdId.
     */
    public void setSalretachdId(java.lang.String salretachdId) {
        this.salretachdId = salretachdId;
    }

    /**
     * Getter for property damageachdId.
     *
     * @return Value of property damageachdId.
     */
    public java.lang.String getDamageachdId() {
        return damageachdId;
    }

    /**
     * Setter for property damageachdId.
     *
     * @param damageachdId New value of property damageachdId.
     */
    public void setDamageachdId(java.lang.String damageachdId) {
        this.damageachdId = damageachdId;
    }

    /**
     * Getter for property serviceTaxachdId.
     *
     * @return Value of property serviceTaxachdId.
     */
    public java.lang.String getServiceTaxachdId() {
        return serviceTaxachdId;
    }

    /**
     * Setter for property serviceTaxachdId.
     *
     * @param serviceTaxachdId New value of property serviceTaxachdId.
     */
    public void setServiceTaxachdId(java.lang.String serviceTaxachdId) {
        this.serviceTaxachdId = serviceTaxachdId;
    }

    /**
     * Getter for property vatachdId.
     *
     * @return Value of property vatachdId.
     */
    public java.lang.String getVatachdId() {
        return vatachdId;
    }

    /**
     * Setter for property vatachdId.
     *
     * @param vatachdId New value of property vatachdId.
     */
    public void setVatachdId(java.lang.String vatachdId) {
        this.vatachdId = vatachdId;
    }

    /**
     * Getter for property deficiateachdId.
     *
     * @return Value of property deficiateachdId.
     */
    public java.lang.String getDeficiateachdId() {
        return deficiateachdId;
    }

    /**
     * Setter for property deficiateachdId.
     *
     * @param deficiateachdId New value of property deficiateachdId.
     */
    public void setDeficiateachdId(java.lang.String deficiateachdId) {
        this.deficiateachdId = deficiateachdId;
    }

    public Double getVatOpngStock() {
        return vatOpngStock;
    }

    public void setVatOpngStock(Double vatOpngStock) {
        this.vatOpngStock = vatOpngStock;
    }

    public Double getProfitPercentage() {
        return profitPercentage;
    }

    public void setProfitPercentage(Double profitPercentage) {
        this.profitPercentage = profitPercentage;
    }

    

    public String getStockHd() {
        return stockHd;
    }

    public void setStockHd(String stockHd) {
        this.stockHd = stockHd;
    }

    public String getDiscountHead() {
        return discountHead;
    }

    public void setDiscountHead(String discountHead) {
        this.discountHead = discountHead;
    }

    public String getDiscountVatHead() {
        return discountVatHead;
    }

    public void setDiscountVatHead(String discountVatHead) {
        this.discountVatHead = discountVatHead;
    }
    
    
    
}