/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition.
 * 
 *
 * DepositClosingHomeBean.java
 * 
 * Created on Thu May 20 15:53:06 GMT+05:30 2004
 */
package com.see.truetransact.serverside.deposit.closing;

import javax.ejb.SessionBean;
import javax.ejb.SessionContext;
import javax.ejb.CreateException;

import java.util.HashMap;

import com.see.truetransact.serverside.TTDAOImpl;
import com.see.truetransact.serverexception.ServiceLocatorException;

/**
 * DepositClosingHome Bean which calls the DAO.
 *
 */
public class DepositClosingBean implements SessionBean, TTDAOImpl {

    DepositClosingDAO daoDepositClosingHome = null;

    public void ejbCreate() throws CreateException, ServiceLocatorException {
        daoDepositClosingHome = new DepositClosingDAO();
    }

    public void ejbRemove() {
        daoDepositClosingHome = null;
    }

    public void setSessionContext(SessionContext sess) {
    }

    public void ejbActivate() {
    }

    public void ejbPassivate() {
    }

    public HashMap execute(HashMap obj) throws Exception {
        return daoDepositClosingHome.execute(obj);
    }

    public HashMap executeQuery(HashMap obj) throws Exception {
        return daoDepositClosingHome.executeQuery(obj);
    }
}
