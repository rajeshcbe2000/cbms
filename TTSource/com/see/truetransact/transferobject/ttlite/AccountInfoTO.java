package com.see.truetransact.transferobject.ttlite;

/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition.
 * .
 *
 * AccountInfoTO.java
 *
 * Created on May 4, 2004, 1:34 PM
 */
import com.see.truetransact.transferobject.TransferObject;

/**
 * This class will act as a Bean class to the Account Information
 *
 * @author Pranav
 */
public class AccountInfoTO extends TransferObject {

    private String accountNumber;
    private String lastTransactionDate;
    private String availableBalance;
    private String baseCurrency;
    private String accountType;
    private String category;
    private String status;
    private String name;

    /**
     * Creates a new instance of AccountInfoTO
     */
    public AccountInfoTO() {
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
     * @param accountType New value of property accountType.
     *
     */
    public void setAccountType(String accountType) {
        this.accountType = accountType;
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
     * Getter for property category.
     *
     * @return Value of property category.
     *
     */
    public String getCategory() {
        return category;
    }

    /**
     * Setter for property category.
     *
     * @param catagory New value of property category.
     *
     */
    public void setCategory(String category) {
        this.category = category;
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

    /**
     * Getter for property name.
     *
     * @return Value of property name.
     *
     */
    public String getName() {
        return name;
    }

    /**
     * Setter for property name.
     *
     * @param name New value of property name.
     *
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Getter for property status.
     *
     * @return Value of property status.
     *
     */
    public String getStatus() {
        return status;
    }

    /**
     * Setter for property status.
     *
     * @param status New value of property status.
     *
     */
    public void setStatus(String status) {
        this.status = status;
    }
}
