/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition.  
 * 
 * CustomerProofTo.java
 */

package com.see.truetransact.transferobject.customer;

import com.see.truetransact.transferobject.TransferObject;
import java.io.Serializable;

/**
 *
 * @author Rishad
 */
public class CustomerProofTo extends TransferObject implements Serializable {
    
     private String custId = "";
     private String proofType="";
     private String uniqueId="";
     private String status = "";

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
     
     

    public String getCustId() {
        return custId;
    }

    public void setCustId(String custId) {
        this.custId = custId;
    }

    public String getProofType() {
        return proofType;
    }

    public void setProofType(String proofType) {
        this.proofType = proofType;
    }

    public String getUniqueId() {
        return uniqueId;
    }

    public void setUniqueId(String uniqueId) {
        this.uniqueId = uniqueId;
    }
     
     public String getKeyData() {
        setKeyColumns(custId);
        return custId;
    } 
     
       /**
     * toString method which returns this TO as a String.
     */
     
    @Override
      public String toString() {
         StringBuffer strB = new StringBuffer(getTOStringStart(this.getClass().getName()));
         strB.append(getTOStringKey(getKeyData())); 
         strB.append(getTOString("custId", custId));
         strB.append(getTOString("proofType", proofType));
         strB.append(getTOString("uniqueId",uniqueId));
         strB.append(getTOString("status",status));
         strB.append(getTOStringEnd());
         return strB.toString(); 
      }
    
     /**
     * toXML method which returns this TO as a XML output.
     */
    public String toXML() {
        StringBuffer strB = new StringBuffer(getTOXmlStart(this.getClass().getName()));
        strB.append(getTOXmlKey(getKeyData()));
        strB.append(getTOXml("custId", custId));
        strB.append(getTOXml("proofType", proofType));
        strB.append(getTOXml("uniqueId", uniqueId));
        strB.append(getTOXml("status",status));
        strB.append(getTOXmlEnd());
        return strB.toString();
    }
    
    
    
}
