/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition.
 * 
 *
 * SuspiciousConfigBean.java
 * 
 * Created on Sat Jan 08 15:03:26 IST 2005
 */
package com.see.truetransact.serverside.sysadmin.suspiciousconfig;

import javax.ejb.SessionBean;
import javax.ejb.SessionContext;
import javax.ejb.CreateException;

import java.util.HashMap;

import com.see.truetransact.serverexception.ServiceLocatorException;

import com.see.truetransact.serverside.TTDAOImpl;

/**
 * SuspiciousConfig Bean which calls the DAO.
 *
 */
public class SuspiciousConfigBean implements SessionBean, TTDAOImpl {

    SuspiciousConfigDAO daoSuspiciousConfig = null;

    public void ejbCreate() throws CreateException, ServiceLocatorException {
        daoSuspiciousConfig = new SuspiciousConfigDAO();
    }

    public void ejbRemove() {
        daoSuspiciousConfig = null;
    }

    public void setSessionContext(SessionContext sess) {
    }

    public void ejbActivate() {
    }

    public void ejbPassivate() {
    }

    public HashMap execute(HashMap obj) throws Exception {
        return daoSuspiciousConfig.execute(obj);
    }

    public HashMap executeQuery(HashMap obj) throws Exception {
        return daoSuspiciousConfig.executeQuery(obj);
    }
}
