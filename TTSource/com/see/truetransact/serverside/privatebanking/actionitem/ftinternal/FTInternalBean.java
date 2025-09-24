/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition.
 * 
 *
 * FTInternalBean.java
 * 
 * Created on Tue Jun 22 12:22:13 GMT+05:30 2004
 */
package com.see.truetransact.serverside.privatebanking.actionitem.ftinternal;

import javax.ejb.SessionBean;
import javax.ejb.SessionContext;
import javax.ejb.CreateException;

import java.util.HashMap;

import com.see.truetransact.serverside.TTDAOImpl;
import com.see.truetransact.serverexception.ServiceLocatorException;

/**
 * FTInternal Bean which calls the DAO.
 *
 */
public class FTInternalBean implements SessionBean, TTDAOImpl {

    FTInternalDAO daoFTInternal = null;

    public void ejbCreate() throws CreateException, ServiceLocatorException {
        daoFTInternal = new FTInternalDAO();
    }

    public void ejbRemove() {
        daoFTInternal = null;
    }

    public void setSessionContext(SessionContext sess) {
    }

    public void ejbActivate() {
    }

    public void ejbPassivate() {
    }

    public HashMap execute(HashMap obj) throws Exception {
        return daoFTInternal.execute(obj);
    }

    public HashMap executeQuery(HashMap obj) throws Exception {
        return daoFTInternal.executeQuery(obj);
    }
}
