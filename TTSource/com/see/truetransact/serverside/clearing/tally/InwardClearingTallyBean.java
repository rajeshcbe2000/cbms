/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition.
 * 
 *
 * InwardClearingTallyBean.java
 * 
 * Created on Wed Mar 17 18:11:59 IST 2004
 */
package com.see.truetransact.serverside.clearing.tally;

import javax.ejb.SessionBean;
import javax.ejb.SessionContext;
import javax.ejb.CreateException;

import java.util.HashMap;

import com.see.truetransact.serverexception.ServiceLocatorException;
import com.see.truetransact.serverside.TTDAOImpl;

/**
 * InwardClearingTally Bean which calls the DAO.
 *
 */
public class InwardClearingTallyBean implements SessionBean, TTDAOImpl {

    InwardClearingTallyDAO daoInwardClearingTally = null;

    public void ejbCreate() throws CreateException, ServiceLocatorException {
        daoInwardClearingTally = new InwardClearingTallyDAO();
    }

    public void ejbRemove() {
        daoInwardClearingTally = null;
    }

    public void setSessionContext(SessionContext sess) {
    }

    public void ejbActivate() {
    }

    public void ejbPassivate() {
    }

    public HashMap execute(HashMap obj) throws Exception {
        return daoInwardClearingTally.execute(obj);
    }

    public HashMap executeQuery(HashMap obj) throws Exception {
        return daoInwardClearingTally.executeQuery(obj);
    }
}
