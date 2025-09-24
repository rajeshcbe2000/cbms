/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition.
 * 
 *
 * BillsBean.java
 * 
 * Created on Mon Mar 15 16:08:52 GMT+05:30 2004
 */
package com.see.truetransact.serverside.salaryrecovery;

import javax.ejb.SessionBean;
import javax.ejb.SessionContext;
import javax.ejb.CreateException;
import com.see.truetransact.serverside.product.bills.BillsDAO;

import java.util.HashMap;

import com.see.truetransact.serverside.TTDAOImpl;
import com.see.truetransact.serverexception.ServiceLocatorException;

/**
 * Bills Bean which calls the DAO.
 *
 */
public class RecoveryParametersBean implements SessionBean, TTDAOImpl {

    RecoveryParametersDAO daoRecoveryParameters = null;

    public void ejbCreate() throws CreateException, ServiceLocatorException {
        daoRecoveryParameters = new RecoveryParametersDAO();
    }

    public void ejbRemove() {
        daoRecoveryParameters = null;
    }

    public void setSessionContext(SessionContext sess) {
    }

    public void ejbActivate() {
    }

    public void ejbPassivate() {
    }

    public HashMap execute(HashMap obj) throws Exception {
        return daoRecoveryParameters.execute(obj);
    }

    public HashMap executeQuery(HashMap obj) throws Exception {
        return daoRecoveryParameters.executeQuery(obj);
    }
}