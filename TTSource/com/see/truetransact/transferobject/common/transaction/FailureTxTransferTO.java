/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition.  
 * 
 * FailureTxTransferTO.java
 *
 * Created on January 7, 2005, 12:25 PM
 */
package com.see.truetransact.transferobject.common.transaction;

import com.see.truetransact.transferobject.transaction.transfer.TxTransferTO;

/**
 *
 * @author 152691 (Sunil)
 */
public class FailureTxTransferTO extends TxTransferTO {

    /**
     * Creates a new instance of FailureTxTransferTO
     */
    public FailureTxTransferTO() {
    }
    private String transLogId;
    private String batchType;
    private String failureRemarks;

    /**
     * Getter for property transLogId.
     *
     * @return Value of property transLogId.
     */
    public java.lang.String getTransLogId() {
        return transLogId;
    }

    /**
     * Setter for property transLogId.
     *
     * @param transLogId New value of property transLogId.
     */
    public void setTransLogId(String transLogId) {
        this.transLogId = transLogId;
    }

    /**
     * Getter for property batchType.
     *
     * @return Value of property batchType.
     */
    public java.lang.String getBatchType() {
        return batchType;
    }

    /**
     * Setter for property batchType.
     *
     * @param batchType New value of property batchType.
     */
    public void setBatchType(String batchType) {
        this.batchType = batchType;
    }

    /**
     * Getter for property failureRemarks.
     *
     * @return Value of property failureRemarks.
     */
    public java.lang.String getFailureRemarks() {
        return failureRemarks;
    }

    /**
     * Setter for property failureRemarks.
     *
     * @param failureRemarks New value of property failureRemarks.
     */
    public void setFailureRemarks(java.lang.String failureRemarks) {
        this.failureRemarks = failureRemarks;
    }
}
