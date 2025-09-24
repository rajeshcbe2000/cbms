/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition.
 * 
 *
 * DepositFreezeBean.java
 * 
 * Created on Wed Jun 02 18:39:20 GMT+05:30 2004
 */
package com.see.truetransact.serverside.deposit.freeze;

import javax.ejb.SessionBean;
import javax.ejb.SessionContext;
import javax.ejb.CreateException;

import java.util.HashMap;

import com.see.truetransact.serverside.TTDAOImpl;
import com.see.truetransact.serverexception.ServiceLocatorException;

/**
 * DepositFreeze Bean which calls the DAO.
 *
 */
public class DepositFreezeBean implements SessionBean, TTDAOImpl {

    DepositFreezeDAO daoDepositFreeze = null;

    public void ejbCreate() throws CreateException, ServiceLocatorException {
        daoDepositFreeze = new DepositFreezeDAO();
    }

    public void ejbRemove() {
        daoDepositFreeze = null;
    }

    public void setSessionContext(SessionContext sess) {
    }

    public void ejbActivate() {
    }

    public void ejbPassivate() {
    }

    public HashMap execute(HashMap obj) throws Exception {
        return daoDepositFreeze.execute(obj);
    }

    public HashMap executeQuery(HashMap obj) throws Exception {
        return daoDepositFreeze.executeQuery(obj);
    }
}
