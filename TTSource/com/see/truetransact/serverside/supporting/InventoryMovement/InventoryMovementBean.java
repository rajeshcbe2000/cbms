/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition.
 * 
 *
 * RemitStopPaymentBean.java
 * 
 * Created on Tue Jan 25 10:29:17 IST 2005
 */
package com.see.truetransact.serverside.supporting.InventoryMovement;

import javax.ejb.SessionBean;
import javax.ejb.SessionContext;
import javax.ejb.CreateException;

import java.util.HashMap;

import com.see.truetransact.serverexception.ServiceLocatorException;

import com.see.truetransact.serverside.TTDAOImpl;

/**
 * RemitStopPayment Bean which calls the DAO.
 *
 */
public class InventoryMovementBean implements SessionBean, TTDAOImpl {

    InventoryMovementDAO daoInventoryMovement = null;

    public void ejbCreate() throws CreateException, ServiceLocatorException {
        daoInventoryMovement = new InventoryMovementDAO();
    }

    public void ejbRemove() {
        daoInventoryMovement = null;
    }

    public void setSessionContext(SessionContext sess) {
    }

    public void ejbActivate() {
    }

    public void ejbPassivate() {
    }

    public HashMap execute(HashMap obj) throws Exception {
        return daoInventoryMovement.execute(obj);
    }

    public HashMap executeQuery(HashMap obj) throws Exception {
        return daoInventoryMovement.executeQuery(obj);
    }
}
