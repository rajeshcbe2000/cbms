/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition.
 * 
 *
 * ShareDividendPaymentBean.java
 * 
 * Created on Wed May 26 10:59:57 GMT+05:30 2004
 */
package com.see.truetransact.serverside.share;

import javax.ejb.SessionBean;
import javax.ejb.SessionContext;
import javax.ejb.CreateException;

import java.util.HashMap;

import com.see.truetransact.serverside.TTDAOImpl;
import com.see.truetransact.serverexception.ServiceLocatorException;

/**
 * Deduction Bean which calls the DAO.
 *
 */
public class ShareDividendPaymentBean implements SessionBean, TTDAOImpl {

    ShareDividendPaymentDAO daoShareDividendPayment = null;

    public void ejbCreate() throws CreateException, ServiceLocatorException {
        daoShareDividendPayment = new ShareDividendPaymentDAO();
    }

    public void ejbRemove() {
        daoShareDividendPayment = null;
    }

    public void setSessionContext(SessionContext sess) {
    }

    public void ejbActivate() {
    }

    public void ejbPassivate() {
    }

    public HashMap execute(HashMap obj) throws Exception {
        return daoShareDividendPayment.execute(obj);
    }

    public HashMap executeQuery(HashMap obj) throws Exception {
        return daoShareDividendPayment.executeQuery(obj);
    }
}
