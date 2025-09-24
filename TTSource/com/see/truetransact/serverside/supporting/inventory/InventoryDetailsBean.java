/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition.
 * 
 *
 * InventoryDetailsBean.java
 * 
 * Created on Mon Aug 23 12:49:08 IST 2004
 */
package com.see.truetransact.serverside.supporting.inventory;

import javax.ejb.SessionBean;
import javax.ejb.SessionContext;
import javax.ejb.CreateException;

import java.util.HashMap;
import com.see.truetransact.serverside.TTDAOImpl;
import com.see.truetransact.serverexception.ServiceLocatorException;

/**
 * InventoryDetails Bean which calls the DAO.
 *
 */
public class InventoryDetailsBean implements SessionBean, TTDAOImpl {

    InventoryDetailsDAO daoInventoryDetails = null;

    public void ejbCreate() throws CreateException, ServiceLocatorException {
        daoInventoryDetails = new InventoryDetailsDAO();
    }

    public void ejbRemove() {
        daoInventoryDetails = null;
    }

    public void setSessionContext(SessionContext sess) {
    }

    public void ejbActivate() {
    }

    public void ejbPassivate() {
    }

    public HashMap execute(HashMap obj) throws Exception {
        return daoInventoryDetails.execute(obj);
    }

    public HashMap executeQuery(HashMap obj) throws Exception {
        return daoInventoryDetails.executeQuery(obj);
    }
}
