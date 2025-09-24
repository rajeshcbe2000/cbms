/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition.
 **
 *
 * CurrencyExchangeBean.java
 * 
 * Created on Mon Jan 12 14:27:32 GMT+05:30 2004
 */
package com.see.truetransact.serverside.forex;

import javax.ejb.SessionBean;
import javax.ejb.SessionContext;
import javax.ejb.CreateException;

import java.util.HashMap;

import com.see.truetransact.serverside.TTDAOImpl;
import com.see.truetransact.serverexception.ServiceLocatorException;

/**
 * CurrencyExchange Bean which calls the DAO.
 *
 */
public class CurrencyExchangeBean implements SessionBean, TTDAOImpl {

    CurrencyExchangeDAO daoCurrencyExchange = null;

    public void ejbCreate() throws CreateException, ServiceLocatorException {
        daoCurrencyExchange = new CurrencyExchangeDAO();
    }

    public void ejbRemove() {
        daoCurrencyExchange = null;
    }

    public void setSessionContext(SessionContext sess) {
    }

    public void ejbActivate() {
    }

    public void ejbPassivate() {
    }

    public HashMap execute(HashMap obj) throws Exception {
        return daoCurrencyExchange.execute(obj);
    }

    public HashMap executeQuery(HashMap obj) throws Exception {
        return daoCurrencyExchange.executeQuery(obj);
    }
}
