/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition.
 * 
 *
 * DrfTransactionBean.java
 * 
 * Created on Wed May 26 10:59:57 GMT+05:30 2004
 */
package com.see.truetransact.serverside.share;

import javax.ejb.SessionBean;
import javax.ejb.SessionContext;
import javax.ejb.CreateException;

import java.util.HashMap;

import com.see.truetransact.serverside.TTDAOImpl;
import com.see.truetransact.serverexception.ServiceLocatorException;

/**
 * Deduction Bean which calls the DAO.
 *
 */
public class DrfTransactionBean implements SessionBean, TTDAOImpl {

    DrfTransactionDAO daoDrfTransaction = null;

    public void ejbCreate() throws CreateException, ServiceLocatorException {
        daoDrfTransaction = new DrfTransactionDAO();
    }

    public void ejbRemove() {
        daoDrfTransaction = null;
    }

    public void setSessionContext(SessionContext sess) {
    }

    public void ejbActivate() {
    }

    public void ejbPassivate() {
    }

    public HashMap execute(HashMap obj) throws Exception {
        return daoDrfTransaction.execute(obj);
    }

    public HashMap executeQuery(HashMap obj) throws Exception {
        return daoDrfTransaction.executeQuery(obj);
    }
}
