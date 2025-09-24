/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition.
 * 
 *
 * LodgementBean.java
 * 
 * Created on Mon Feb 07 15:08:33 IST 2005
 */
package com.see.truetransact.serverside.bills.lodgement;

import javax.ejb.SessionBean;
import javax.ejb.SessionContext;
import javax.ejb.CreateException;

import java.util.HashMap;

import com.see.truetransact.serverexception.ServiceLocatorException;

import com.see.truetransact.serverside.TTDAOImpl;

/**
 * Lodgement Bean which calls the DAO.
 *
 */
public class LodgementBean implements SessionBean, TTDAOImpl {

    LodgementDAO daoLodgement = null;

    public void ejbCreate() throws CreateException, ServiceLocatorException {
        daoLodgement = new LodgementDAO();
    }

    public void ejbRemove() {
        daoLodgement = null;
    }

    public void setSessionContext(SessionContext sess) {
    }

    public void ejbActivate() {
    }

    public void ejbPassivate() {
    }

    public HashMap execute(HashMap obj) throws Exception {
        return daoLodgement.execute(obj);
    }

    public HashMap executeQuery(HashMap obj) throws Exception {
        return daoLodgement.executeQuery(obj);
    }
}
