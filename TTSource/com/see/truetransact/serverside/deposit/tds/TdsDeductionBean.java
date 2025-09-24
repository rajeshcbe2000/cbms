/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition.
 * 
 *
 * TdsDeductionBean.java
 * 
 * Created on Mon Mar 22 15:25:59 IST 2004
 */
package com.see.truetransact.serverside.deposit.tds;

import javax.ejb.SessionBean;
import javax.ejb.SessionContext;
import javax.ejb.CreateException;

import java.util.HashMap;

import com.see.truetransact.serverside.TTDAOImpl;
import com.see.truetransact.serverexception.ServiceLocatorException;

/**
 * TdsDeductionTO Bean which calls the DAO.
 *
 */
public class TdsDeductionBean implements SessionBean, TTDAOImpl {

    TdsDeductionDAO daoTdsDeductionTO = null;

    public void ejbCreate() throws CreateException, ServiceLocatorException {
        daoTdsDeductionTO = new TdsDeductionDAO();
    }

    public void ejbRemove() {
        daoTdsDeductionTO = null;
    }

    public void setSessionContext(SessionContext sess) {
    }

    public void ejbActivate() {
    }

    public void ejbPassivate() {
    }

    public HashMap execute(HashMap obj) throws Exception {
        return daoTdsDeductionTO.execute(obj);
    }

    public HashMap executeQuery(HashMap obj) throws Exception {
        return daoTdsDeductionTO.executeQuery(obj);
    }
}
