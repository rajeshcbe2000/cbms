/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition.
 **
 *
 * TDSConfigBean.java
 * 
 * Created on Mon Jan 31 16:34:23 IST 2005
 */
package com.see.truetransact.serverside.tds.tdsconfig;

import javax.ejb.SessionBean;
import javax.ejb.SessionContext;
import javax.ejb.CreateException;

import java.util.HashMap;

import com.see.truetransact.serverexception.ServiceLocatorException;

import com.see.truetransact.serverside.TTDAOImpl;

/**
 * TDSConfig Bean which calls the DAO.
 *
 */
public class TDSConfigBean implements SessionBean, TTDAOImpl {

    TDSConfigDAO daoTDSConfig = null;

    public void ejbCreate() throws CreateException, ServiceLocatorException {
        daoTDSConfig = new TDSConfigDAO();
    }

    public void ejbRemove() {
        daoTDSConfig = null;
    }

    public void setSessionContext(SessionContext sess) {
    }

    public void ejbActivate() {
    }

    public void ejbPassivate() {
    }

    public HashMap execute(HashMap obj) throws Exception {
        return daoTDSConfig.execute(obj);
    }

    public HashMap executeQuery(HashMap obj) throws Exception {
        return daoTDSConfig.executeQuery(obj);
    }
}
