/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition.
 * 
 *
 * OutwardClearingTallyBean.java
 * 
 * Created on Tue Mar 30 12:31:31 PST 2004
 */
package com.see.truetransact.serverside.clearing.outwardtally;

import javax.ejb.SessionBean;
import javax.ejb.SessionContext;
import javax.ejb.CreateException;

import java.util.HashMap;

import com.see.truetransact.serverexception.ServiceLocatorException;
import com.see.truetransact.serverside.TTDAOImpl;

/**
 * OutwardClearingTally Bean which calls the DAO.
 *
 */
public class OutwardClearingTallyBean implements SessionBean, TTDAOImpl {

    OutwardClearingTallyDAO daoOutwardClearingTally = null;

    public void ejbCreate() throws CreateException, ServiceLocatorException {
        daoOutwardClearingTally = new OutwardClearingTallyDAO();
    }

    public void ejbRemove() {
        daoOutwardClearingTally = null;
    }

    public void setSessionContext(SessionContext sess) {
    }

    public void ejbActivate() {
    }

    public void ejbPassivate() {
    }

    public HashMap execute(HashMap obj) throws Exception {
        return daoOutwardClearingTally.execute(obj);
    }

    public HashMap executeQuery(HashMap obj) throws Exception {
        return daoOutwardClearingTally.executeQuery(obj);
    }
}
