/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition.
 **
 *
 * NomineeBean.java
 * 
 * Created on Fri Dec 31 08:37:24 IST 2004
 */
package com.see.truetransact.serverside.common.nominee;

import javax.ejb.SessionBean;
import javax.ejb.SessionContext;
import javax.ejb.CreateException;

import java.util.HashMap;

import com.see.truetransact.serverexception.ServiceLocatorException;

import com.see.truetransact.serverside.TTDAOImpl;

/**
 * Nominee Bean which calls the DAO.
 *
 */
public class NomineeBean implements SessionBean, TTDAOImpl {

    NomineeDAO daoNominee = null;

    public void ejbCreate() throws CreateException, ServiceLocatorException {
        daoNominee = new NomineeDAO();
    }

    public void ejbRemove() {
        daoNominee = null;
    }

    public void setSessionContext(SessionContext sess) {
    }

    public void ejbActivate() {
    }

    public void ejbPassivate() {
    }

    public HashMap execute(HashMap obj) throws Exception {
        return daoNominee.execute(obj);
    }

    public HashMap executeQuery(HashMap obj) throws Exception {
        return daoNominee.executeQuery(obj);
    }
}
