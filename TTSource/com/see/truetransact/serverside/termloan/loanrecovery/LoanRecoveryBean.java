/*
 * Copyright 2019 Fincuro Solutions (P) Ltd. All rights reserved.
 *
 * This software is the proprietary information of Fincuro Solutions  (P) Ltd..
 **
 *
 * LoanRecovery.java
 * 
 * Created on Wed Feb 02 13:11:28 IST 2019
 */
package com.see.truetransact.serverside.termloan.loanrecovery;

import com.see.truetransact.serverexception.ServiceLocatorException;
import com.see.truetransact.serverside.TTDAOImpl;
import java.util.HashMap;
import javax.ejb.CreateException;
import javax.ejb.SessionBean;
import javax.ejb.SessionContext;

/**
 * LoanRecovery Bean which calls the DAO.
 *
 */
public class LoanRecoveryBean implements SessionBean, TTDAOImpl {

    LoanRecoveryDAO daoLoanRecovery = null;

    public void ejbCreate() throws CreateException, ServiceLocatorException {
        daoLoanRecovery = new LoanRecoveryDAO();
    }

    public void ejbRemove() {
        daoLoanRecovery = null;
    }

    public void setSessionContext(SessionContext sess) {
    }

    public void ejbActivate() {
    }

    public void ejbPassivate() {
    }

    public HashMap execute(HashMap obj) throws Exception {
        return daoLoanRecovery.execute(obj);
    }

    public HashMap executeQuery(HashMap obj) throws Exception {
        return daoLoanRecovery.executeQuery(obj);
    }
}
