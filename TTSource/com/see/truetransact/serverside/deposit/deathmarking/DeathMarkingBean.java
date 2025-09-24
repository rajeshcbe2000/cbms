/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition.
 * 
 *
 * DeathMarkingBean.java
 * 
 * Created on Thu May 27 10:22:23 GMT+05:30 2004
 */
package com.see.truetransact.serverside.deposit.deathmarking;

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
public class DeathMarkingBean implements SessionBean, TTDAOImpl {

    DeathMarkingDAO daoDeathMarking = null;

    public void ejbCreate() throws CreateException, ServiceLocatorException {
        daoDeathMarking = new DeathMarkingDAO();
    }

    public void ejbRemove() {
        daoDeathMarking = null;
    }

    public void setSessionContext(SessionContext sess) {
    }

    public void ejbActivate() {
    }

    public void ejbPassivate() {
    }

    public HashMap execute(HashMap obj) throws Exception {
        return daoDeathMarking.execute(obj);
    }

    public HashMap executeQuery(HashMap obj) throws Exception {
        return daoDeathMarking.executeQuery(obj);
    }
}
