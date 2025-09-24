/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition.
 * 
 *
 * ReturnOfInstrumentsBean.java
 * 
 * Created on Tue Apr 06 17:27:23 GMT+05:30 2004
 */
package com.see.truetransact.serverside.clearing.returns;

import javax.ejb.SessionBean;
import javax.ejb.SessionContext;
import javax.ejb.CreateException;

import java.util.HashMap;

import com.see.truetransact.serverexception.ServiceLocatorException;
import com.see.truetransact.serverside.TTDAOImpl;

/**
 * ReturnOfInstruments Bean which calls the DAO.
 *
 */
public class ReturnOfInstrumentsBean implements SessionBean, TTDAOImpl {

    ReturnOfInstrumentsDAO daoReturnOfInstruments = null;

    public void ejbCreate() throws CreateException, ServiceLocatorException {
        daoReturnOfInstruments = new ReturnOfInstrumentsDAO();
    }

    public void ejbRemove() {
        daoReturnOfInstruments = null;
    }

    public void setSessionContext(SessionContext sess) {
    }

    public void ejbActivate() {
    }

    public void ejbPassivate() {
    }

    public HashMap execute(HashMap obj) throws Exception {
        return daoReturnOfInstruments.execute(obj);
    }

    public HashMap executeQuery(HashMap obj) throws Exception {
        return daoReturnOfInstruments.executeQuery(obj);
    }
}
