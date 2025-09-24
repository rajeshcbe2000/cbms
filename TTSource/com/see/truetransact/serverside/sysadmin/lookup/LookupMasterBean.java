/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition.
 * 
 *
 * LookupMasterTOBean.java
 * 
 * Created on Fri Feb 27 14:45:44 IST 2004
 */
package com.see.truetransact.serverside.sysadmin.lookup;

import javax.ejb.SessionBean;
import javax.ejb.SessionContext;
import javax.ejb.CreateException;

import java.util.HashMap;

import com.see.truetransact.serverside.TTDAOImpl;
import com.see.truetransact.serverexception.ServiceLocatorException;

/**
 * LookupMasterTO Bean which calls the DAO.
 *
 */
public class LookupMasterBean implements SessionBean, TTDAOImpl {

    LookupMasterDAO daoLookupMaster = null;

    public void ejbCreate() throws CreateException, ServiceLocatorException {
        daoLookupMaster = new LookupMasterDAO();
    }

    public void ejbRemove() {
        daoLookupMaster = null;
    }

    public void setSessionContext(SessionContext sess) {
    }

    public void ejbActivate() {
    }

    public void ejbPassivate() {
    }

    public HashMap execute(HashMap obj) throws Exception {
        return daoLookupMaster.execute(obj);
    }

    public HashMap executeQuery(HashMap obj) throws Exception {
        return daoLookupMaster.executeQuery(obj);
    }
}
