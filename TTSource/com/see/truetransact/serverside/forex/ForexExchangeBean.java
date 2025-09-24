/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition.
 **
 *
 * ForexExchangeBean.java
 * 
 * Created on Tue May 04 18:27:02 IST 2004
 */
package com.see.truetransact.serverside.forex;

import javax.ejb.SessionBean;
import javax.ejb.SessionContext;
import javax.ejb.CreateException;

import java.util.HashMap;

import com.see.truetransact.serverside.TTDAOImpl;
import com.see.truetransact.serverexception.ServiceLocatorException;

/**
 * ForexExchange Bean which calls the DAO.
 *
 */
public class ForexExchangeBean implements SessionBean, TTDAOImpl {

    ForexExchangeDAO daoForexExchange = null;

    public void ejbCreate() throws CreateException, ServiceLocatorException {
        daoForexExchange = new ForexExchangeDAO();
    }

    public void ejbRemove() {
        daoForexExchange = null;
    }

    public void setSessionContext(SessionContext sess) {
    }

    public void ejbActivate() {
    }

    public void ejbPassivate() {
    }

    public HashMap execute(HashMap obj) throws Exception {
        return daoForexExchange.execute(obj);
    }

    public HashMap executeQuery(HashMap obj) throws Exception {
        return daoForexExchange.executeQuery(obj);
    }
}
