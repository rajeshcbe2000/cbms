/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition. 
 * 
 * DailyAccountTransTO.java
 *
 * Created on December 26, 2007, 5:07 PM
 */
package com.see.truetransact.transferobject.transaction.dailyDepositTrans;

/**
 *
 * @author Bala
 */
import java.util.Date;
import java.io.Serializable;
import com.see.truetransact.transferobject.TransferObject;

public class DailyAccountTransTO extends TransferObject implements Serializable {

    private String col1 = "";
    private String col2 = "";    
    private String col3 = "";
    private String col4 = "";
    private String col5 = "";
    private String col6 = "";
    private String col7 = "";
    private String col8 = "";
    private String col9 = "";
    private String col10 = "";
    private String col11 = "";
    private String col12 = "";
    private String col13 = "";
    private String col14 = "";
    private String col15 = "";
    private String agentId = "";
    private String initiatedBranch = "";
    private Date colDate = null;

    public String getAgentId() {
        return agentId;
    }

    public void setAgentId(String agentId) {
        this.agentId = agentId;
    }

    public String getCol1() {
        return col1;
    }

    public void setCol1(String col1) {
        this.col1 = col1;
    }

    public String getCol10() {
        return col10;
    }

    public void setCol10(String col10) {
        this.col10 = col10;
    }

    public String getCol11() {
        return col11;
    }

    public void setCol11(String col11) {
        this.col11 = col11;
    }

    public String getCol12() {
        return col12;
    }

    public void setCol12(String col12) {
        this.col12 = col12;
    }

    public String getCol13() {
        return col13;
    }

    public void setCol13(String col13) {
        this.col13 = col13;
    }

    public String getCol14() {
        return col14;
    }

    public void setCol14(String col14) {
        this.col14 = col14;
    }

    public String getCol15() {
        return col15;
    }

    public void setCol15(String col15) {
        this.col15 = col15;
    }

    public String getCol2() {
        return col2;
    }

    public void setCol2(String col2) {
        this.col2 = col2;
    }

    public String getCol3() {
        return col3;
    }

    public void setCol3(String col3) {
        this.col3 = col3;
    }

    public String getCol4() {
        return col4;
    }

    public void setCol4(String col4) {
        this.col4 = col4;
    }

    public String getCol5() {
        return col5;
    }

    public void setCol5(String col5) {
        this.col5 = col5;
    }

    public String getCol6() {
        return col6;
    }

    public void setCol6(String col6) {
        this.col6 = col6;
    }

    public String getCol7() {
        return col7;
    }

    public void setCol7(String col7) {
        this.col7 = col7;
    }

    public String getCol8() {
        return col8;
    }

    public void setCol8(String col8) {
        this.col8 = col8;
    }

    public String getCol9() {
        return col9;
    }

    public void setCol9(String col9) {
        this.col9 = col9;
    }

    public Date getColDate() {
        return colDate;
    }

    public void setColDate(Date colDate) {
        this.colDate = colDate;
    }



    public String toString() {
        StringBuffer strB = new StringBuffer(getTOStringStart(this.getClass().getName()));
        strB.append(getTOString("col1", col1));
        strB.append(getTOString("col2", col2));
        strB.append(getTOString("col3", col3));
        strB.append(getTOString("col4", col4));
        strB.append(getTOString("col5", col5));
        strB.append(getTOString("col6", col6));
        strB.append(getTOString("col7", col7));
        strB.append(getTOString("col8", col8));
        strB.append(getTOString("col9", col9));
        strB.append(getTOString("col10", col10));
        strB.append(getTOString("col11", col11));
        strB.append(getTOString("col12", col12));
        strB.append(getTOString("col13", col13));
        strB.append(getTOString("col14", col14));
        strB.append(getTOString("col15", col15));
        strB.append(getTOString("agentId", agentId));
        strB.append(getTOString("initiatedBranch", initiatedBranch));
        strB.append(getTOString("colDate", colDate));
        strB.append(getTOStringEnd());
        return strB.toString();
    }

    public String toXML() {
        StringBuffer strB = new StringBuffer(getTOXmlStart(this.getClass().getName()));
        strB.append(getTOXml("col1", col1));
        strB.append(getTOXml("col2", col2));
        strB.append(getTOXml("col3", col3));
        strB.append(getTOXml("col4", col4));
        strB.append(getTOXml("col5", col5));
        strB.append(getTOXml("col6", col6));
        strB.append(getTOXml("col7", col7));
        strB.append(getTOXml("col8", col8));
        strB.append(getTOXml("col9", col9));
        strB.append(getTOXml("col10", col10));
        strB.append(getTOXml("col11", col11));
        strB.append(getTOXml("col12", col12));
        strB.append(getTOXml("col13", col13));
        strB.append(getTOXml("col14", col14));
        strB.append(getTOXml("col15", col15));
        strB.append(getTOXml("agentId", agentId));
        strB.append(getTOXml("initiatedBranch", initiatedBranch));
        strB.append(getTOXml("colDate", colDate));
        strB.append(getTOXmlEnd());
        return strB.toString();
    }

}