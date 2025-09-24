/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition.
 **
 *
 * ChargesBean.java
 * 
 * Created on Fri Dec 24 10:41:17 IST 2004
 */
package com.see.truetransact.serverside.common.charges;

import javax.ejb.SessionBean;
import javax.ejb.SessionContext;
import javax.ejb.CreateException;

import java.util.HashMap;

import com.see.truetransact.serverexception.ServiceLocatorException;

import com.see.truetransact.serverside.TTDAOImpl;

/**
 * Charges Bean which calls the DAO.
 *
 */
public class LoanChargesBean implements SessionBean, TTDAOImpl {

    LoanChargesDAO daoLoanCharges = null;

    public void ejbCreate() throws CreateException, ServiceLocatorException {
        daoLoanCharges = new LoanChargesDAO();
    }

    public void ejbRemove() {
        daoLoanCharges = null;
    }

    public void setSessionContext(SessionContext sess) {
    }

    public void ejbActivate() {
    }

    public void ejbPassivate() {
    }

    public HashMap execute(HashMap obj) throws Exception {
        return daoLoanCharges.execute(obj);
    }

    public HashMap executeQuery(HashMap obj) throws Exception {
        return daoLoanCharges.executeQuery(obj);
    }
}
