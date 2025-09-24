/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition.
 **
 *
 * ExchangeRateBean.java
 * 
 * Created on Mon Jan 12 17:16:01 GMT+05:30 2004
 */
package com.see.truetransact.serverside.forex;

import javax.ejb.SessionBean;
import javax.ejb.SessionContext;
import javax.ejb.CreateException;

import java.util.HashMap;

import com.see.truetransact.serverside.TTDAOImpl;
import com.see.truetransact.serverexception.ServiceLocatorException;

/**
 * ExchangeRate Bean which calls the DAO.
 *
 */
public class ExchangeRateBean implements SessionBean, TTDAOImpl {

    ExchangeRateDAO daoExchangeRate = null;

    public void ejbCreate() throws CreateException, ServiceLocatorException {
        daoExchangeRate = new ExchangeRateDAO();
    }

    public void ejbRemove() {
        daoExchangeRate = null;
    }

    public void setSessionContext(SessionContext sess) {
    }

    public void ejbActivate() {
    }

    public void ejbPassivate() {
    }

    public HashMap execute(HashMap obj) throws Exception {
        return daoExchangeRate.execute(obj);
    }

    public HashMap executeQuery(HashMap obj) throws Exception {
        return daoExchangeRate.executeQuery(obj);
    }
}
