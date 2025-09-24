/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition.
 * 
 *
 * CashMovementBean.java
 * 
 * Created on Fri Jan 28 14:31:54 IST 2005
 */
package com.see.truetransact.serverside.transaction.cashmanagement;

import javax.ejb.SessionBean;
import javax.ejb.SessionContext;
import javax.ejb.CreateException;

import java.util.HashMap;

import com.see.truetransact.serverexception.ServiceLocatorException;

import com.see.truetransact.serverside.TTDAOImpl;

/**
 * CashMovement Bean which calls the DAO.
 *
 */
public class CashMovementBean implements SessionBean, TTDAOImpl {

    CashMovementDAO daoCashMovement = null;

    public void ejbCreate() throws CreateException, ServiceLocatorException {
        daoCashMovement = new CashMovementDAO();
    }

    public void ejbRemove() {
        daoCashMovement = null;
    }

    public void setSessionContext(SessionContext sess) {
    }

    public void ejbActivate() {
    }

    public void ejbPassivate() {
    }

    public HashMap execute(HashMap obj) throws Exception {
        return daoCashMovement.execute(obj);
    }

    public HashMap executeQuery(HashMap obj) throws Exception {
        return daoCashMovement.executeQuery(obj);
    }
}
