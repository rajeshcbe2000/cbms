/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition.
 * 
 *
 * BankBean.java
 * 
 * Created on Thu Feb 05 18:32:23 IST 2004
 */
package com.see.truetransact.serverside.sysadmin.bank;

import javax.ejb.SessionBean;
import javax.ejb.SessionContext;
import javax.ejb.CreateException;

import java.util.HashMap;

import com.see.truetransact.serverside.TTDAOImpl;
import com.see.truetransact.serverexception.ServiceLocatorException;

/**
 * Bank Bean which calls the DAO.
 *
 */
public class BankBean implements SessionBean, TTDAOImpl {

    BankDAO daoBank = null;

    public void ejbCreate() throws CreateException, ServiceLocatorException {
        daoBank = new BankDAO();
    }

    public void ejbRemove() {
        daoBank = null;
    }

    public void setSessionContext(SessionContext sess) {
    }

    public void ejbActivate() {
    }

    public void ejbPassivate() {
    }

    public HashMap execute(HashMap obj) throws Exception {
        return daoBank.execute(obj);
    }

    public HashMap executeQuery(HashMap obj) throws Exception {
        return daoBank.executeQuery(obj);
    }
}
