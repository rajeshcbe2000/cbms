/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition.
 * 
 *
 * CashTransactionBean.java
 * 
 * Created on Wed Feb 25 16:04:18 IST 2004
 */
package com.see.truetransact.serverside.transaction.cash;

import javax.ejb.SessionBean;
import javax.ejb.SessionContext;
import javax.ejb.CreateException;

import java.util.HashMap;

import com.see.truetransact.serverexception.ServiceLocatorException;
import com.see.truetransact.serverside.TTDAOImpl;

/**
 * CashTransaction Bean which calls the DAO.
 *
 */
public class CashTransactionBean implements SessionBean, TTDAOImpl {

    CashTransactionDAO daoCashTransaction = null;

    public void ejbCreate() throws CreateException, ServiceLocatorException {
        daoCashTransaction = new CashTransactionDAO();
    }

    public void ejbRemove() {
        daoCashTransaction = null;
    }

    public void setSessionContext(SessionContext sess) {
    }

    public void ejbActivate() {
    }

    public void ejbPassivate() {
    }

    public HashMap execute(HashMap obj) throws Exception {
        return daoCashTransaction.execute(obj);
    }

    public HashMap executeQuery(HashMap obj) throws Exception {
        return daoCashTransaction.executeQuery(obj);
    }
}
