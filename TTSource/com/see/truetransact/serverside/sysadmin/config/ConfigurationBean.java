/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition.
 * 
 *
 * ConfigurationBean.java
 * 
 * Created on Fri Feb 11 11:44:57 IST 2005
 */
package com.see.truetransact.serverside.sysadmin.config;

import javax.ejb.SessionBean;
import javax.ejb.SessionContext;
import javax.ejb.CreateException;

import java.util.HashMap;

import com.see.truetransact.serverexception.ServiceLocatorException;

import com.see.truetransact.serverside.TTDAOImpl;

/**
 * Configuration Bean which calls the DAO.
 *
 */
public class ConfigurationBean implements SessionBean, TTDAOImpl {

    ConfigurationDAO daoConfiguration = null;

    public void ejbCreate() throws CreateException, ServiceLocatorException {
        daoConfiguration = new ConfigurationDAO();
    }

    public void ejbRemove() {
        daoConfiguration = null;
    }

    public void setSessionContext(SessionContext sess) {
    }

    public void ejbActivate() {
    }

    public void ejbPassivate() {
    }

    public HashMap execute(HashMap obj) throws Exception {
        return daoConfiguration.execute(obj);
    }

    public HashMap executeQuery(HashMap obj) throws Exception {
        return daoConfiguration.executeQuery(obj);
    }
}
