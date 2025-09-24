/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition.
 **
 *
 * IntroducerBean.java
 * 
 * Created on Fri Dec 31 11:53:03 IST 2004
 */
package com.see.truetransact.serverside.common.introducer;

import javax.ejb.SessionBean;
import javax.ejb.SessionContext;
import javax.ejb.CreateException;

import java.util.HashMap;

import com.see.truetransact.serverexception.ServiceLocatorException;

import com.see.truetransact.serverside.TTDAOImpl;

/**
 * Introducer Bean which calls the DAO.
 *
 */
public class IntroducerBean implements SessionBean, TTDAOImpl {

    IntroducerDAO daoIntroducer = null;

    public void ejbCreate() throws CreateException, ServiceLocatorException {
        daoIntroducer = new IntroducerDAO();
    }

    public void ejbRemove() {
        daoIntroducer = null;
    }

    public void setSessionContext(SessionContext sess) {
    }

    public void ejbActivate() {
    }

    public void ejbPassivate() {
    }

    public HashMap execute(HashMap obj) throws Exception {
        return daoIntroducer.execute(obj);
    }

    public HashMap executeQuery(HashMap obj) throws Exception {
        return daoIntroducer.executeQuery(obj);
    }
}
