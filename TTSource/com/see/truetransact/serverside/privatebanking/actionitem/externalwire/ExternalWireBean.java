/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition.
 * 
 *
 * ExternalWireBean.java
 * 
 * Created on Fri Jul 02 16:00:36 GMT+05:30 2004
 */
package com.see.truetransact.serverside.privatebanking.actionitem.externalwire;

import javax.ejb.SessionBean;
import javax.ejb.SessionContext;
import javax.ejb.CreateException;

import java.util.HashMap;

import com.see.truetransact.serverside.TTDAOImpl;
import com.see.truetransact.serverexception.ServiceLocatorException;

/**
 * ExternalWire Bean which calls the DAO.
 *
 */
public class ExternalWireBean implements SessionBean, TTDAOImpl {

    ExternalWireDAO daoExternalWire = null;

    public void ejbCreate() throws CreateException, ServiceLocatorException {
        daoExternalWire = new ExternalWireDAO();
    }

    public void ejbRemove() {
        daoExternalWire = null;
    }

    public void setSessionContext(SessionContext sess) {
    }

    public void ejbActivate() {
    }

    public void ejbPassivate() {
    }

    public HashMap execute(HashMap obj) throws Exception {
        return daoExternalWire.execute(obj);
    }

    public HashMap executeQuery(HashMap obj) throws Exception {
        return daoExternalWire.executeQuery(obj);
    }
}
