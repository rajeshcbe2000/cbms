/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition.
 * 
 *
 * InventoryMasterBean.java
 * 
 * Created on Fri Aug 20 14:36:56 IST 2004
 */
package com.see.truetransact.serverside.supporting.inventory;

import javax.ejb.SessionBean;
import javax.ejb.SessionContext;
import javax.ejb.CreateException;

import java.util.HashMap;

import com.see.truetransact.serverexception.ServiceLocatorException;
import com.see.truetransact.serverside.TTDAOImpl;

/**
 * InventoryMaster Bean which calls the DAO.
 *
 */
public class InventoryMasterBean implements SessionBean, TTDAOImpl {

    InventoryMasterDAO daoInventoryMaster = null;

    public void ejbCreate() throws CreateException, ServiceLocatorException {
        daoInventoryMaster = new InventoryMasterDAO();
    }

    public void ejbRemove() {
        daoInventoryMaster = null;
    }

    public void setSessionContext(SessionContext sess) {
    }

    public void ejbActivate() {
    }

    public void ejbPassivate() {
    }

    public HashMap execute(HashMap obj) throws Exception {
        return daoInventoryMaster.execute(obj);
    }

    public HashMap executeQuery(HashMap obj) throws Exception {
        return daoInventoryMaster.executeQuery(obj);
    }
}
