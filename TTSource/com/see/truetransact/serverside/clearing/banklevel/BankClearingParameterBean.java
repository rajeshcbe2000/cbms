/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition.
 * 
 *
 * ParameterBean.java
 * 
 * Created on Wed Mar 17 11:06:22 PST 2004
 */
package com.see.truetransact.serverside.clearing.banklevel;

import javax.ejb.SessionBean;
import javax.ejb.SessionContext;
import javax.ejb.CreateException;

import java.util.HashMap;

import com.see.truetransact.serverexception.ServiceLocatorException;
import com.see.truetransact.serverside.TTDAOImpl;

/**
 * Parameter Bean which calls the DAO.
 *
 */
public class BankClearingParameterBean implements SessionBean, TTDAOImpl {

    BankClearingParameterDAO daoParameter = null;

    public void ejbCreate() throws CreateException, ServiceLocatorException {
        daoParameter = new BankClearingParameterDAO();
    }

    public void ejbRemove() {
        daoParameter = null;
    }

    public void setSessionContext(SessionContext sess) {
    }

    public void ejbActivate() {
    }

    public void ejbPassivate() {
    }

    public HashMap execute(HashMap obj) throws Exception {
        return daoParameter.execute(obj);
    }

    public HashMap executeQuery(HashMap obj) throws Exception {
        return daoParameter.executeQuery(obj);
    }
}
