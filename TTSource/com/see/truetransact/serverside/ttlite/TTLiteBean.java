/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition.
 * 
 *
 * TTLiteBean.java
 * 
 * Created on Tue May 25 09:45:30 IST 2004
 */
package com.see.truetransact.serverside.ttlite;

import javax.ejb.SessionBean;
import javax.ejb.SessionContext;
import javax.ejb.CreateException;

import java.util.HashMap;

import com.see.truetransact.serverside.TTDAOImpl;
import com.see.truetransact.serverexception.ServiceLocatorException;

/**
 * TTLite Bean which calls the DAO.
 *
 * @author Pranav
 */
public class TTLiteBean implements SessionBean, TTDAOImpl {

    TTLiteDAO daoTTLite = null;

    public void ejbCreate() throws CreateException, ServiceLocatorException {
        daoTTLite = new TTLiteDAO();
    }

    public void ejbRemove() {
        daoTTLite = null;
    }

    public void setSessionContext(SessionContext sess) {
    }

    public void ejbActivate() {
    }

    public void ejbPassivate() {
    }

    public HashMap execute(HashMap obj) throws Exception {
        return daoTTLite.execute(obj);
    }

    public HashMap executeQuery(HashMap obj) throws Exception {
        return daoTTLite.executeQuery(obj);
    }
}
