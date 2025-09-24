/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition.
 **
 *
 * SupplierMasterBean.java
 * 
 * Created on Fri Jun 10 15:52:50 IST 2011
 */
package com.see.truetransact.serverside.inventory;

import javax.ejb.SessionBean;
import javax.ejb.SessionContext;
import javax.ejb.CreateException;

import java.util.HashMap;

import com.see.truetransact.serverexception.ServiceLocatorException;

import com.see.truetransact.serverside.TTDAOImpl;

/**
 * SupplierMaster Bean which calls the DAO.
 *
 */
public class SupplierMasterBean implements SessionBean, TTDAOImpl {

    SupplierMasterDAO daoSupplierMaster = null;

    public void ejbCreate() throws CreateException, ServiceLocatorException {
        daoSupplierMaster = new SupplierMasterDAO();
    }

    public void ejbRemove() {
        daoSupplierMaster = null;
    }

    public void setSessionContext(SessionContext sess) {
    }

    public void ejbActivate() {
    }

    public void ejbPassivate() {
    }

    public HashMap execute(HashMap obj) throws Exception {
        return daoSupplierMaster.execute(obj);
    }

    public HashMap executeQuery(HashMap obj) throws Exception {
        return daoSupplierMaster.executeQuery(obj);
    }
}
