/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition. 
 * 
 * SIChargesHeadTO.java
 *
 * Created on November 11, 2008, 3:43 PM
 */
package com.see.truetransact.transferobject.sysadmin.config;

import java.util.Date;
import java.io.Serializable;
import com.see.truetransact.transferobject.TransferObject;

/**
 *
 * @author Administrator
 */
public class SIChargesHeadTO extends TransferObject implements Serializable {

    private String siChargesHd = "";
    private String remitChargesHd = "";
    private String acceptChargesHd = "";
    private String execChargesHd = "";
    private String failChargesHd = "";
    private String serviceTaxHd = "";
    private Double serviceTax = null;
    private String nmfAcHd = "";

    public String getNmfAcHd() {
        return nmfAcHd;
    }

    public void setNmfAcHd(String nmfAcHd) {
        this.nmfAcHd = nmfAcHd;
    }

    /**
     * Getter for property siChargesHd.
     *
     * @return Value of property siChargesHd.
     */
    public java.lang.String getSiChargesHd() {
        return siChargesHd;
    }

    /**
     * Setter for property siChargesHd.
     *
     * @param siChargesHd New value of property siChargesHd.
     */
    public void setSiChargesHd(java.lang.String siChargesHd) {
        this.siChargesHd = siChargesHd;
    }

    /**
     * Getter for property remitChargesHd.
     *
     * @return Value of property remitChargesHd.
     */
    public java.lang.String getRemitChargesHd() {
        return remitChargesHd;
    }

    /**
     * Setter for property remitChargesHd.
     *
     * @param remitChargesHd New value of property remitChargesHd.
     */
    public void setRemitChargesHd(java.lang.String remitChargesHd) {
        this.remitChargesHd = remitChargesHd;
    }

    /**
     * Getter for property acceptChargesHd.
     *
     * @return Value of property acceptChargesHd.
     */
    public java.lang.String getAcceptChargesHd() {
        return acceptChargesHd;
    }

    /**
     * Setter for property acceptChargesHd.
     *
     * @param acceptChargesHd New value of property acceptChargesHd.
     */
    public void setAcceptChargesHd(java.lang.String acceptChargesHd) {
        this.acceptChargesHd = acceptChargesHd;
    }

    /**
     * Getter for property execChargesHd.
     *
     * @return Value of property execChargesHd.
     */
    public java.lang.String getExecChargesHd() {
        return execChargesHd;
    }

    /**
     * Setter for property execChargesHd.
     *
     * @param execChargesHd New value of property execChargesHd.
     */
    public void setExecChargesHd(java.lang.String execChargesHd) {
        this.execChargesHd = execChargesHd;
    }

    /**
     * Getter for property failChargesHd.
     *
     * @return Value of property failChargesHd.
     */
    public java.lang.String getFailChargesHd() {
        return failChargesHd;
    }

    /**
     * Setter for property failChargesHd.
     *
     * @param failChargesHd New value of property failChargesHd.
     */
    public void setFailChargesHd(java.lang.String failChargesHd) {
        this.failChargesHd = failChargesHd;
    }

    public String getKeyData() {
        setKeyColumns("");
        return "";
    }

    /**
     * toString method which returns this TO as a String.
     */
    public String toString() {
        StringBuffer strB = new StringBuffer(getTOStringStart(this.getClass().getName()));
        strB.append(getTOStringKey(getKeyData()));
        strB.append(getTOString("siChargesHd", siChargesHd));
        strB.append(getTOString("remitChargesHd", remitChargesHd));
        strB.append(getTOString("acceptChargesHd", acceptChargesHd));
        strB.append(getTOString("execChargesHd", execChargesHd));
        strB.append(getTOString("failChargesHd", failChargesHd));
        strB.append(getTOString("serviceTaxHd", serviceTaxHd));
        strB.append(getTOString("serviceTax", serviceTax));
        strB.append(getTOString("nmfAcHd", nmfAcHd));
        strB.append(getTOStringEnd());
        return strB.toString();
    }

    /**
     * toXML method which returns this TO as a XML output.
     */
    public String toXML() {
        StringBuffer strB = new StringBuffer(getTOXmlStart(this.getClass().getName()));
        strB.append(getTOXmlKey(getKeyData()));
        strB.append(getTOXml("siChargesHd", siChargesHd));
        strB.append(getTOXml("remitChargesHd", remitChargesHd));
        strB.append(getTOXml("acceptChargesHd", acceptChargesHd));
        strB.append(getTOXml("execChargesHd", execChargesHd));
        strB.append(getTOXml("failChargesHd", failChargesHd));
        strB.append(getTOXml("serviceTaxHd", serviceTaxHd));
        strB.append(getTOXml("serviceTax", serviceTax));
        strB.append(getTOXml("nmfAcHd", nmfAcHd));
        strB.append(getTOXmlEnd());
        return strB.toString();
    }

    /**
     * Getter for property serviceTaxHd.
     *
     * @return Value of property serviceTaxHd.
     */
    public java.lang.String getServiceTaxHd() {
        return serviceTaxHd;
    }

    /**
     * Setter for property serviceTaxHd.
     *
     * @param serviceTaxHd New value of property serviceTaxHd.
     */
    public void setServiceTaxHd(java.lang.String serviceTaxHd) {
        this.serviceTaxHd = serviceTaxHd;
    }

    /**
     * Getter for property serviceTax.
     *
     * @return Value of property serviceTax.
     */
    public java.lang.Double getServiceTax() {
        return serviceTax;
    }

    /**
     * Setter for property serviceTax.
     *
     * @param serviceTax New value of property serviceTax.
     */
    public void setServiceTax(java.lang.Double serviceTax) {
        this.serviceTax = serviceTax;
    }
}
