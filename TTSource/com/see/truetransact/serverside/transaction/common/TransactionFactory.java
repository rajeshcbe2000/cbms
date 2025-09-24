/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition.
 * 
 *
 * TransactionFactory.java
 *
 * Created on June 17, 2004, 4:35 PM
 */
package com.see.truetransact.serverside.transaction.common;

import com.see.truetransact.serverside.transaction.common.product.operativeacct.OperativeTransaction;
import com.see.truetransact.serverside.transaction.common.product.deposit.DepositTransaction;
import com.see.truetransact.serverside.transaction.common.product.gl.GLTransaction;
import com.see.truetransact.serverside.transaction.common.product.loan.LoanTransaction;
import com.see.truetransact.serverside.transaction.common.product.advances.AdvancesTransaction;
import com.see.truetransact.serverside.transaction.common.product.loan.AgriLoanTransaction;
import com.see.truetransact.serverside.transaction.common.product.advances.AgriAdvancesTransaction;
import com.see.truetransact.serverside.transaction.common.product.bills.BillsTransaction;
import com.see.truetransact.serverside.transaction.common.product.otherbanksacct.AccountswithOtherBanksTransaction;
import com.see.truetransact.serverside.transaction.common.product.suspense.SuspenseTransaction;

/**
 *
 * @author bala
 */
public class TransactionFactory implements java.io.Serializable {

    public static final String OPERATIVE = "OA";
    public static final String DEPOSITS = "TD";
    public static final String GL = "GL";
    public static final String LOANS = "TL";
    public static final String ADVANCES = "AD";
    public static final String AGRILOANS = "ATL";
    public static final String AGRIADVANCES = "AAD";
    public static final String SUSPENSE = "SA";
    public static final String OTHERBANKACTS = "AB";

    /**
     * Creates a new instance of TransactionFactory
     */
    private TransactionFactory() {
    }

    /**
     * Returns a Transaction based on configuration.
     *
     * @throws Exception Throws Exception based on the error
     * @return Returns Transaction based on Configuration
     */
    public static Transaction createTransaction(String transType) throws Exception {
        if (transType.equals(OPERATIVE)) {
            return new OperativeTransaction();
        } else if (transType.equals(DEPOSITS)) {
            return new DepositTransaction();
        } else if (transType.equals(GL)) {
            return new GLTransaction();
        } else if (transType.equals(LOANS)) {
            return new LoanTransaction();
        } else if (transType.equals(ADVANCES)) {
            return new AdvancesTransaction();
        } else if (transType.equals("BILLS")) {
            return new BillsTransaction();
        } else if (transType.equals(AGRILOANS)) {
            return new AgriLoanTransaction();
        } else if (transType.equals(AGRIADVANCES)) {
            return new AgriAdvancesTransaction();
        } else if (transType.equals(SUSPENSE)) { /*
             * added to create a new Suspense Account Product
             */
            return new SuspenseTransaction();
        } else if (transType.equals(OTHERBANKACTS)) { /*
             * //OTHERBANKACTS
             */
            return new AccountswithOtherBanksTransaction();
        }

        throw new TransactionNotFoundException("Transaction Not Found " + transType);
    }
}
