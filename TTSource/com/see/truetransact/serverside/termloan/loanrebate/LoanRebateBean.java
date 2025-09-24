/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition.
 **
 *
 * AgentBean.java
 * 
 * Created on Wed Feb 02 13:11:28 IST 2005
 */
package com.see.truetransact.serverside.termloan.loanrebate;

import javax.ejb.SessionBean;
import javax.ejb.SessionContext;
import javax.ejb.CreateException;

import java.util.HashMap;

import com.see.truetransact.serverexception.ServiceLocatorException;

import com.see.truetransact.serverside.TTDAOImpl;

/**
 * TermloanCharges Bean which calls the DAO.
 *
 */
public class LoanRebateBean implements SessionBean, TTDAOImpl {

    LoanRebateDAO daoLoanRebate = null;

    public void ejbCreate() throws CreateException, ServiceLocatorException {
        daoLoanRebate = new LoanRebateDAO();
    }

    public void ejbRemove() {
        daoLoanRebate = null;
    }

    public void setSessionContext(SessionContext sess) {
    }

    public void ejbActivate() {
    }

    public void ejbPassivate() {
    }

    public HashMap execute(HashMap obj) throws Exception {
//            TermLoanChargesDAO daoLoanCharges = new TermLoanChargesDAO();
        return daoLoanRebate.execute(obj);
    }

    public HashMap executeQuery(HashMap obj) throws Exception {
//            TermLoanChargesDAO daoLoanCharges = new TermLoanChargesDAO();
        return daoLoanRebate.executeQuery(obj);
    }
}
