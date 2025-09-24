/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition.
 * 
 *
 * DepositRolloverBean.java
 * 
 * Created on Thu Jun 17 10:24:49 GMT+05:30 2004
 */
package com.see.truetransact.serverside.privatebanking.orders;

import javax.ejb.SessionBean;
import javax.ejb.SessionContext;
import javax.ejb.CreateException;

import java.util.HashMap;

import com.see.truetransact.serverside.TTDAOImpl;
import com.see.truetransact.serverexception.ServiceLocatorException;

/**
 * DepositRollover Bean which calls the DAO.
 *
 */
public class DepositRolloverBean implements SessionBean, TTDAOImpl {

    DepositRolloverDAO daoDepositRollover = null;

    public void ejbCreate() throws CreateException, ServiceLocatorException {
        daoDepositRollover = new DepositRolloverDAO();
    }

    public void ejbRemove() {
        daoDepositRollover = null;
    }

    public void setSessionContext(SessionContext sess) {
    }

    public void ejbActivate() {
    }

    public void ejbPassivate() {
    }

    public HashMap execute(HashMap obj) throws Exception {
        return daoDepositRollover.execute(obj);
    }

    public HashMap executeQuery(HashMap obj) throws Exception {
        return daoDepositRollover.executeQuery(obj);
    }
}
