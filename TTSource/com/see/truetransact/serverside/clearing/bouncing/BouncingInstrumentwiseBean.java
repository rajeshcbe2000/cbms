/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition.
 * 
 *
 * BouncingInstrumentwiseBean.java
 * 
 * Created on Wed Apr 07 18:38:06 GMT+05:30 2004
 */
package com.see.truetransact.serverside.clearing.bouncing;

import javax.ejb.SessionBean;
import javax.ejb.SessionContext;
import javax.ejb.CreateException;

import java.util.HashMap;

import com.see.truetransact.serverexception.ServiceLocatorException;
import com.see.truetransact.serverside.TTDAOImpl;

/**
 * BouncingInstrumentwise Bean which calls the DAO.
 *
 */
public class BouncingInstrumentwiseBean implements SessionBean, TTDAOImpl {

    BouncingInstrumentwiseDAO daoBouncingInstrumentwise = null;

    public void ejbCreate() throws CreateException, ServiceLocatorException {
        daoBouncingInstrumentwise = new BouncingInstrumentwiseDAO();
    }

    public void ejbRemove() {
        daoBouncingInstrumentwise = null;
    }

    public void setSessionContext(SessionContext sess) {
    }

    public void ejbActivate() {
    }

    public void ejbPassivate() {
    }

    public HashMap execute(HashMap obj) throws Exception {
        return daoBouncingInstrumentwise.execute(obj);
    }

    public HashMap executeQuery(HashMap obj) throws Exception {
        return daoBouncingInstrumentwise.executeQuery(obj);
    }
}
