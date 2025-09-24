/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition.
 * 
 *
 * GLLimitBean.java
 * 
 * Created on Wed Jun 08 12:14:52 GMT+05:30 2005
 */
package com.see.truetransact.serverside.generalledger.gllimit;

import javax.ejb.SessionBean;
import javax.ejb.SessionContext;
import javax.ejb.CreateException;

import java.util.HashMap;

import com.see.truetransact.serverexception.ServiceLocatorException;

import com.see.truetransact.serverside.TTDAO;

/**
 * GLLimit Bean which calls the DAO.
 *
 */
public class GLLimitBean extends TTDAO implements SessionBean {

    GLLimitDAO daoGLLimit = null;

    public void ejbCreate() throws CreateException, ServiceLocatorException {
        daoGLLimit = new GLLimitDAO();
    }

    public void ejbRemove() {
        daoGLLimit = null;
    }

    public void setSessionContext(SessionContext sess) {
    }

    public void ejbActivate() {
    }

    public void ejbPassivate() {
    }

    public HashMap execute(HashMap obj) throws Exception {
        daoGLLimit.execute(obj);
        return null;
    }

    public HashMap executeQuery(HashMap obj) throws Exception {
        return daoGLLimit.executeQuery(obj);
    }
}
