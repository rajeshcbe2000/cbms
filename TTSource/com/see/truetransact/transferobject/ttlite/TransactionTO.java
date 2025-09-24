package com.see.truetransact.transferobject.ttlite;

/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition.
 * .
 *
 * TransactionTO.java
 *
 * Created on May 6, 2004, 11:12 PM
 */
import com.see.truetransact.transferobject.TransferObject;

/**
 * This class will act as a Bean class to the Transaction Information
 *
 * @author Pranav
 */
public class TransactionTO extends TransferObject {

    private String serialNo;
    private String date;
    private String description;
    private String type;
    private Double amount;
    private Double debit;
    private Double credit;
    private Double balance;
    private Double bonus;

    public Double getBonus() {
        return bonus;
    }

    public void setBonus(Double bonus) {
        this.bonus = bonus;
    }
    public Double getBalance() {
        return balance;
    }

    public void setBalance(Double balance) {
        this.balance = balance;
    }

    public Double getCredit() {
        return credit;
    }

    public void setCredit(Double credit) {
        this.credit = credit;
    }

    public Double getDebit() {
        return debit;
    }

    public void setDebit(Double debit) {
        this.debit = debit;
    }
    /** Creates a new instance of TransactionTO */
    public TransactionTO() {
    }

    /**
     * Getter for property amount.
     *
     * @return Value of property amount.
     *
     */
    public Double getAmount() {
        return amount;
    }

    /**
     * Setter for property amount.
     *
     * @param amount New value of property amount.
     *
     */
    public void setAmount(Double amount) {
        this.amount = amount;
    }

    /**
     * Getter for property Date.
     *
     * @return Value of property Date.
     *
     */
    public String getDate() {
        return date;
    }

    /**
     * Setter for property Date.
     *
     * @param Date New value of property Date.
     *
     */
    public void setDate(String date) {
        this.date = date;
    }

    /**
     * Getter for property description.
     *
     * @return Value of property description.
     *
     */
    public String getDescription() {
        return description;
    }

    /**
     * Setter for property description.
     *
     * @param description New value of property description.
     *
     */
    public void setDescription(String description) {
        this.description = description;
    }

    /**
     * Getter for property serialNo.
     *
     * @return Value of property serialNo.
     *
     */
    public String getSerialNo() {
        return serialNo;
    }

    /**
     * Setter for property serialNo.
     *
     * @param serialNo New value of property serialNo.
     *
     */
    public void setSerialNo(String serialNo) {
        this.serialNo = serialNo;
    }

    /**
     * Getter for property Type.
     *
     * @return Value of property Type.
     *
     */
    public String getType() {
        return type;
    }

    /**
     * Setter for property Type.
     *
     * @param Type New value of property Type.
     *
     */
    public void setType(String type) {
        this.type = type;
    }
}
