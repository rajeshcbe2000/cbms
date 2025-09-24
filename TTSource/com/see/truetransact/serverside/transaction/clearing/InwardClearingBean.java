/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition.
 * 
 *
 * InwardClearingTOBean.java
 * 
 * Created on Tue Jan 06 18:05:48 IST 2004
 */
package com.see.truetransact.serverside.transaction.clearing;

import javax.ejb.SessionBean;
import javax.ejb.SessionContext;
import javax.ejb.CreateException;

import java.util.HashMap;

import com.see.truetransact.serverside.TTDAOImpl;
import com.see.truetransact.serverexception.ServiceLocatorException;

/**
 * InwardClearingTO Bean which calls the DAO.
 *
 */
public class InwardClearingBean implements SessionBean, TTDAOImpl {

    InwardClearingDAO daoInwardClearing = null;

    public void ejbCreate() throws CreateException, ServiceLocatorException {
        daoInwardClearing = new InwardClearingDAO();
    }

    public void ejbRemove() {
        daoInwardClearing = null;
    }

    public void setSessionContext(SessionContext sess) {
    }

    public void ejbActivate() {
    }

    public void ejbPassivate() {
    }

    public HashMap execute(HashMap obj) throws Exception {
        return daoInwardClearing.execute(obj);
    }

    public HashMap executeQuery(HashMap obj) throws Exception {
        return daoInwardClearing.executeQuery(obj);
    }
}
