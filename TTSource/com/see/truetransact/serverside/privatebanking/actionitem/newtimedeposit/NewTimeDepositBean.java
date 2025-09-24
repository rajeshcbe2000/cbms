/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition.
 * 
 *
 * NewTimeDepositBean.java
 * 
 * Created on Tue Jul 13 13:46:20 GMT+05:30 2004
 */
package com.see.truetransact.serverside.privatebanking.actionitem.newtimedeposit;

import javax.ejb.SessionBean;
import javax.ejb.SessionContext;
import javax.ejb.CreateException;

import java.util.HashMap;

import com.see.truetransact.serverexception.ServiceLocatorException;
import com.see.truetransact.serverside.TTDAOImpl;

/**
 * NewTimeDeposit Bean which calls the DAO.
 *
 */
public class NewTimeDepositBean implements SessionBean, TTDAOImpl {

    NewTimeDepositDAO daoNewTimeDeposit = null;

    public void ejbCreate() throws CreateException, ServiceLocatorException {
        daoNewTimeDeposit = new NewTimeDepositDAO();
    }

    public void ejbRemove() {
        daoNewTimeDeposit = null;
    }

    public void setSessionContext(SessionContext sess) {
    }

    public void ejbActivate() {
    }

    public void ejbPassivate() {
    }

    public HashMap execute(HashMap obj) throws Exception {
        return daoNewTimeDeposit.execute(obj);
    }

    public HashMap executeQuery(HashMap obj) throws Exception {
        return daoNewTimeDeposit.executeQuery(obj);
    }
}
