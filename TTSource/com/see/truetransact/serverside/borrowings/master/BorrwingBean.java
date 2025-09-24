/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition.
 * 
 *
 * BorrwingBean.java
 * 
 * Created on Thu Jan 20 17:19:05 IST 2005
 */
package com.see.truetransact.serverside.borrowings.master;

import javax.ejb.SessionBean;
import javax.ejb.SessionContext;
import javax.ejb.CreateException;

import java.util.HashMap;

import com.see.truetransact.serverexception.ServiceLocatorException;

import com.see.truetransact.serverside.TTDAOImpl;

/**
 * TokenConfig Bean which calls the DAO.
 *
 */
public class BorrwingBean implements SessionBean, TTDAOImpl {

    BorrwingDAO dao = null;

    public void ejbCreate() throws CreateException, ServiceLocatorException {
        dao = new BorrwingDAO();
    }

    public void ejbRemove() {
        dao = null;
    }

    public void setSessionContext(SessionContext sess) {
    }

    public void ejbActivate() {
    }

    public void ejbPassivate() {
    }

    public HashMap execute(HashMap obj) throws Exception {
        return dao.execute(obj);
    }

    public HashMap executeQuery(HashMap obj) throws Exception {
        return dao.executeQuery(obj);
    }
}
