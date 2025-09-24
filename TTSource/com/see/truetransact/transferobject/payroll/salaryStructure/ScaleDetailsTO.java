/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition.  
 * 
 * ScaleDetailsTO.java
 */

package com.see.truetransact.transferobject.payroll.salaryStructure;

/**
 *
 * @author anjuanand
 */
import com.see.truetransact.transferobject.TransferObject;
import java.io.Serializable;

/**
 * Table name for this TO is SCALE_DETAILS.
 */
public class ScaleDetailsTO extends TransferObject implements Serializable {

    private Integer scale_id;
    private Integer version_no;
    private Integer srl_no;
    private Double incr_amount;
    private Integer incr_count;
    private String count_freq = "";

    public Double getIncr_amount() {
        return incr_amount;
    }

    public void setIncr_amount(Double incr_amount) {
        this.incr_amount = incr_amount;
    }

    public String getCount_freq() {
        return count_freq;
    }

    public void setCount_freq(String count_freq) {
        this.count_freq = count_freq;
    }

    public Integer getIncr_count() {
        return incr_count;
    }

    public void setIncr_count(Integer incr_count) {
        this.incr_count = incr_count;
    }

    public Integer getScale_id() {
        return scale_id;
    }

    public void setScale_id(Integer scale_id) {
        this.scale_id = scale_id;
    }

    public Integer getSrl_no() {
        return srl_no;
    }

    public void setSrl_no(Integer srl_no) {
        this.srl_no = srl_no;
    }

    public Integer getVersion_no() {
        return version_no;
    }

    public void setVersion_no(Integer version_no) {
        this.version_no = version_no;
    }

    public String getKeyData() {
        setKeyColumns("");
        return "";
    }

    public String toString() {
        StringBuffer strB = new StringBuffer(getTOStringStart(this.getClass().getName()));
        strB.append(getTOStringKey(getKeyData()));
        strB.append(getTOString("scale_id", scale_id));
        strB.append(getTOString("version_no", version_no));
        strB.append(getTOString("srl_no", srl_no));
        strB.append(getTOString("incr_amount", incr_amount));
        strB.append(getTOString("incr_count", incr_count));
        strB.append(getTOString("count_freq", count_freq));
        strB.append(getTOStringEnd());
        return strB.toString();
    }

    public String toXML() {
        StringBuffer strB = new StringBuffer(getTOXmlStart(this.getClass().getName()));
        strB.append(getTOXmlKey(getKeyData()));
        strB.append(getTOXml("scale_id", scale_id));
        strB.append(getTOXml("version_no", version_no));
        strB.append(getTOXml("srl_no", srl_no));
        strB.append(getTOXml("incr_amount", incr_amount));
        strB.append(getTOXml("incr_count", incr_count));
        strB.append(getTOXml("count_freq", count_freq));
        strB.append(getTOXmlEnd());
        return strB.toString();
    }
}
