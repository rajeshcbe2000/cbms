package com.see.truetransact.transferobject.ttlite;

/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition.
 * .
 *
 * AccountSummaryTO.java
 *
 * Created on May 3, 2004, 3:08 PM
 */
import com.see.truetransact.transferobject.TransferObject;

/**
 * This class will act as a Bean class to the AccountSummary details
 *
 * @author Pranav
 */
public class AccountSummaryTO extends TransferObject {

    private String accountType;
    private String accountNumber;
    private String baseCurrency;
    private String branch;
    private String availableBalance;
    private String lastTransactionDate;
    private String custId;
    private String custName;

    public String getCustName() {
        return custName;
    }

    public void setCustName(String custName) {
        this.custName = custName;
    }
    public String getCustId() {
        return custId;
    }

    public void setCustId(String custId) {
        this.custId = custId;
    }
    /**
     * Creates a new instance of AccountSummary
     */
    public AccountSummaryTO() {
    }

    /**
     * Getter for property accountType.
     *
     * @return Value of property accountType.
     *
     */
    public String getAccountType() {
        return accountType;
    }

    /**
     * Setter for property accountType.
     *
     * @param accountNumber New value of property accountType.
     *
     */
    public void setAccountType(String accountType) {
        this.accountType = accountType;
    }

    /**
     * Getter for property accountNumber.
     *
     * @return Value of property accountNumber.
     *
     */
    public String getAccountNumber() {
        return accountNumber;
    }

    /**
     * Setter for property accountNumber.
     *
     * @param accountNumber New value of property accountNumber.
     *
     */
    public void setAccountNumber(String accountNumber) {
        this.accountNumber = accountNumber;
    }

    /**
     * Getter for property availableBalance.
     *
     * @return Value of property availableBalance.
     *
     */
    public String getAvailableBalance() {
        return availableBalance;
    }

    /**
     * Setter for property availableBalance.
     *
     * @param availableBalance New value of property availableBalance.
     *
     */
    public void setAvailableBalance(String availableBalance) {
        this.availableBalance = availableBalance;
    }

    /**
     * Getter for property baseCurrency.
     *
     * @return Value of property baseCurrency.
     *
     */
    public String getBaseCurrency() {
        return baseCurrency;
    }

    /**
     * Setter for property baseCurrency.
     *
     * @param baseCurrency New value of property baseCurrency.
     *
     */
    public void setBaseCurrency(String baseCurrency) {
        this.baseCurrency = baseCurrency;
    }

    /**
     * Getter for property branch.
     *
     * @return Value of property branch.
     *
     */
    public String getBranch() {
        return branch;
    }

    /**
     * Setter for property branch.
     *
     * @param branch New value of property branch.
     *
     */
    public void setBranch(String branch) {
        this.branch = branch;
    }

    /**
     * Getter for property lastTransactionDate.
     *
     * @return Value of property lastTransactionDate.
     *
     */
    public String getLastTransactionDate() {
        return lastTransactionDate;
    }

    /**
     * Setter for property lastTransactionDate.
     *
     * @param lastTransactionDate New value of property lastTransactionDate.
     *
     */
    public void setLastTransactionDate(String lastTransactionDate) {
        this.lastTransactionDate = lastTransactionDate;
    }
}
