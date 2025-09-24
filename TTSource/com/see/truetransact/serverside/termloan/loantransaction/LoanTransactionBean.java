/*
 * Copyright 2019 Fincuro Solutions (P) Ltd. All rights reserved.
 *
 * This software is the proprietary information of Fincuro Solutions  (P) Ltd..
 **
 *
 * LoanTransaction.java
 * 
 * Created on Wed Feb 02 13:11:28 IST 2019
 */
package com.see.truetransact.serverside.termloan.loantransaction;

import javax.ejb.SessionBean;
import javax.ejb.SessionContext;
import javax.ejb.CreateException;

import java.util.HashMap;

import com.see.truetransact.serverexception.ServiceLocatorException;

import com.see.truetransact.serverside.TTDAOImpl;

/**
 * LoanTransaction Bean which calls the DAO.
 *
 */
public class LoanTransactionBean implements SessionBean, TTDAOImpl {

    LoanTransactionDAO daoLoanTransaction = null;

    public void ejbCreate() throws CreateException, ServiceLocatorException {
        daoLoanTransaction = new LoanTransactionDAO();
    }

    public void ejbRemove() {
        daoLoanTransaction = null;
    }

    public void setSessionContext(SessionContext sess) {
    }

    public void ejbActivate() {
    }

    public void ejbPassivate() {
    }

    public HashMap execute(HashMap obj) throws Exception {
        return daoLoanTransaction.execute(obj);
    }

    public HashMap executeQuery(HashMap obj) throws Exception {
        return daoLoanTransaction.executeQuery(obj);
    }
}
