/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition.
 * 
 *
 * ShareBean.java
 * 
 * Created on Sat Dec 25 13:35:00 IST 2004
 */
package com.see.truetransact.serverside.share;

import javax.ejb.SessionBean;
import javax.ejb.SessionContext;
import javax.ejb.CreateException;

import java.util.HashMap;

import com.see.truetransact.serverexception.ServiceLocatorException;

import com.see.truetransact.serverside.TTDAOImpl;

/**
 * Share Bean which calls the DAO.
 *
 */
public class ShareBean implements SessionBean, TTDAOImpl {

    ShareDAO daoShare = null;

    public void ejbCreate() throws CreateException, ServiceLocatorException {
        daoShare = new ShareDAO();
    }

    public void ejbRemove() {
        daoShare = null;
    }

    public void setSessionContext(SessionContext sess) {
    }

    public void ejbActivate() {
    }

    public void ejbPassivate() {
    }

    public HashMap execute(HashMap obj) throws Exception {
        return daoShare.execute(obj);
    }

    public HashMap executeQuery(HashMap obj) throws Exception {
        return daoShare.executeQuery(obj);
    }
}
