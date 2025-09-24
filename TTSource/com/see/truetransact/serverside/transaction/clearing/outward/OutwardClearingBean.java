/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition.
 * 
 *
 * OutwardClearingBean.java
 * 
 * Created on Mon Jan 12 16:26:34 IST 2004
 */
package com.see.truetransact.serverside.transaction.clearing.outward;

import javax.ejb.SessionBean;
import javax.ejb.SessionContext;
import javax.ejb.CreateException;

import java.util.HashMap;

import com.see.truetransact.serverside.TTDAOImpl;
import com.see.truetransact.serverexception.ServiceLocatorException;

/**
 * OutwardClearing Bean which calls the DAO.
 *
 */
public class OutwardClearingBean implements SessionBean, TTDAOImpl {

    OutwardClearingDAO daoOutwardClearing = null;

    public void ejbCreate() throws CreateException, ServiceLocatorException {
        daoOutwardClearing = new OutwardClearingDAO();
    }

    public void ejbRemove() {
        daoOutwardClearing = null;
    }

    public void setSessionContext(SessionContext sess) {
    }

    public void ejbActivate() {
    }

    public void ejbPassivate() {
    }

    public HashMap execute(HashMap obj) throws Exception {
        return daoOutwardClearing.execute(obj);
    }

    public HashMap executeQuery(HashMap obj) throws Exception {
        return daoOutwardClearing.executeQuery(obj);
    }
}
