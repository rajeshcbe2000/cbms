/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition.
 * 
 *
 * TermDepositBean.java
 *
 * Created on Fri Jan 09 17:49:52 GMT+05:30 2004
 */
package com.see.truetransact.serverside.deposit.autorenewal;

import com.see.truetransact.serverside.deposit.*;
import javax.ejb.SessionBean;
import javax.ejb.SessionContext;
import javax.ejb.CreateException;

import java.util.HashMap;

import com.see.truetransact.serverside.TTDAOImpl;
import com.see.truetransact.serverexception.ServiceLocatorException;

/**
 * TermDeposit Bean which calls the DAO.
 *
 */
public class DepositAutoRenewalBean implements SessionBean, TTDAOImpl {

    DepositAutoRenewalDAO daoDepositAutoRenewalDAO = null;

    public void ejbCreate() throws CreateException, ServiceLocatorException {
        daoDepositAutoRenewalDAO = new DepositAutoRenewalDAO();
    }

    public void ejbRemove() {
        daoDepositAutoRenewalDAO = null;
    }

    public void setSessionContext(SessionContext sess) {
    }

    public void ejbActivate() {
    }

    public void ejbPassivate() {
    }

    public HashMap execute(HashMap obj) throws Exception {
        return daoDepositAutoRenewalDAO.execute(obj);
    }

    public HashMap executeQuery(HashMap obj) throws Exception {
        return daoDepositAutoRenewalDAO.executeQuery(obj);
    }
}
