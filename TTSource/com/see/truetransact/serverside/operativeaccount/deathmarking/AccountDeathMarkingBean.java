/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition.
 * 
 *
 * DeathMarkingBean.java
 * 
 * Created on Thu Jun 03 15:22:11 GMT+05:30 2004
 */
package com.see.truetransact.serverside.operativeaccount.deathmarking;

import javax.ejb.SessionBean;
import javax.ejb.SessionContext;
import javax.ejb.CreateException;

import java.util.HashMap;

import com.see.truetransact.serverside.TTDAOImpl;
import com.see.truetransact.serverexception.ServiceLocatorException;

/**
 * DeathMarking Bean which calls the DAO.
 *
 */
public class AccountDeathMarkingBean implements SessionBean, TTDAOImpl {

    AccountDeathMarkingDAO daoAccountDeathMarking = null;

    public void ejbCreate() throws CreateException, ServiceLocatorException {
        daoAccountDeathMarking = new AccountDeathMarkingDAO();
    }

    public void ejbRemove() {
        daoAccountDeathMarking = null;
    }

    public void setSessionContext(SessionContext sess) {
    }

    public void ejbActivate() {
    }

    public void ejbPassivate() {
    }

    public HashMap execute(HashMap obj) throws Exception {
        return daoAccountDeathMarking.execute(obj);
    }

    public HashMap executeQuery(HashMap obj) throws Exception {
        return daoAccountDeathMarking.executeQuery(obj);
    }
}
