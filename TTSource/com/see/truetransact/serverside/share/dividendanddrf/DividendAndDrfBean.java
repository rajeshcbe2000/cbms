/*
 * Copyright 2010 Fincuro Solution (P) Ltd. All rights reserved.
 *
 * This software is the proprietary information of FIncuro Solution (P) Ltd..
 * 
 *
 * DividendAndDrfBean.java
 * 
 * Created on Fri Mar 27 10:59:57 GMT+05:30 2020
 */
package com.see.truetransact.serverside.share.dividendanddrf;

import javax.ejb.SessionBean;
import javax.ejb.SessionContext;
import javax.ejb.CreateException;

import java.util.HashMap;

import com.see.truetransact.serverside.TTDAOImpl;
import com.see.truetransact.serverexception.ServiceLocatorException;

/**
 * Dividend Bean which calls the DAO.
 *
 */
public class DividendAndDrfBean implements SessionBean, TTDAOImpl {

    DividendAndDrfDAO daoDividendAndDrf = null;

    public void ejbCreate() throws CreateException, ServiceLocatorException {
        daoDividendAndDrf = new DividendAndDrfDAO();
    }

    public void ejbRemove() {
        daoDividendAndDrf = null;
    }

    public void setSessionContext(SessionContext sess) {
    }

    public void ejbActivate() {
    }

    public void ejbPassivate() {
    }

    public HashMap execute(HashMap obj) throws Exception {
        return daoDividendAndDrf.execute(obj);
    }

    public HashMap executeQuery(HashMap obj) throws Exception {
        return daoDividendAndDrf.executeQuery(obj);
    }
}
