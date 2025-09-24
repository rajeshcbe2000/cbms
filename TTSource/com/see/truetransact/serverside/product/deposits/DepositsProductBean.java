/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition.
 * 
 *
 * DepositsProductBean.java
 * 
 * Created on Fri Jan 09 17:55:12 GMT+05:30 2004
 */
package com.see.truetransact.serverside.product.deposits;

import javax.ejb.SessionBean;
import javax.ejb.SessionContext;
import javax.ejb.CreateException;

import com.see.truetransact.serverside.TTDAOImpl;
import com.see.truetransact.serverexception.ServiceLocatorException;

import java.util.HashMap;

/**
 * DepositsProduct Bean which calls the DAO.
 *
 */
public class DepositsProductBean implements SessionBean, TTDAOImpl {

    DepositsProductDAO daoDepositsProduct = null;

    public void ejbCreate() throws CreateException, ServiceLocatorException {
        daoDepositsProduct = new DepositsProductDAO();
    }

    public void ejbRemove() {
        daoDepositsProduct = null;
    }

    public void setSessionContext(SessionContext sess) {
    }

    public void ejbActivate() {
    }

    public void ejbPassivate() {
    }

    public HashMap execute(HashMap obj) throws Exception {
        return daoDepositsProduct.execute(obj);
    }

    public HashMap executeQuery(HashMap obj) throws Exception {
        return daoDepositsProduct.executeQuery(obj);
    }
}
