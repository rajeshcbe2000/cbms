/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition.
 * 
 *
 * RemittancePaymentTOBean.java
 * 
 * Created on Sun Jan 18 11:32:21 IST 2004
 */
package com.see.truetransact.serverside.remittance;

import javax.ejb.SessionBean;
import javax.ejb.SessionContext;
import javax.ejb.CreateException;

import java.util.HashMap;

import com.see.truetransact.serverside.TTDAOImpl;
import com.see.truetransact.serverexception.ServiceLocatorException;

/**
 * RemittancePaymentTO Bean which calls the DAO.
 *
 */
public class RemittancePaymentBean implements SessionBean, TTDAOImpl {

    RemittancePaymentDAO daoRemittancePayment = null;

    public void ejbCreate() throws CreateException, ServiceLocatorException {
        daoRemittancePayment = new RemittancePaymentDAO();
    }

    public void ejbRemove() {
        daoRemittancePayment = null;
    }

    public void setSessionContext(SessionContext sess) {
    }

    public void ejbActivate() {
    }

    public void ejbPassivate() {
    }

    public HashMap execute(HashMap obj) throws Exception {
        return daoRemittancePayment.execute(obj);
    }

    public HashMap executeQuery(HashMap obj) throws Exception {
        return daoRemittancePayment.executeQuery(obj);
    }
}
