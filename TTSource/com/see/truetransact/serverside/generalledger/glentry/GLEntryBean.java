/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition.
 * 
 *
 * GLEntryBean.java
 * 
 * Created on Tue Jan 04 11:04:14 IST 2005
 */
package com.see.truetransact.serverside.generalledger.glentry;

import javax.ejb.SessionBean;
import javax.ejb.SessionContext;
import javax.ejb.CreateException;

import java.util.HashMap;

import com.see.truetransact.serverexception.ServiceLocatorException;

import com.see.truetransact.serverside.TTDAOImpl;

/**
 * GLEntry Bean which calls the DAO.
 *
 */
public class GLEntryBean implements SessionBean, TTDAOImpl {

    GLEntryDAO daoGLEntry = null;

    public void ejbCreate() throws CreateException, ServiceLocatorException {
        daoGLEntry = new GLEntryDAO();
    }

    public void ejbRemove() {
        daoGLEntry = null;
    }

    public void setSessionContext(SessionContext sess) {
    }

    public void ejbActivate() {
    }

    public void ejbPassivate() {
    }

    public HashMap execute(HashMap obj) throws Exception {
        return daoGLEntry.execute(obj);
    }

    public HashMap executeQuery(HashMap obj) throws Exception {
        return daoGLEntry.executeQuery(obj);
    }
}
