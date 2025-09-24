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
package com.see.truetransact.serverside.product.locker;

import javax.ejb.SessionBean;
import javax.ejb.SessionContext;
import javax.ejb.CreateException;
//import com.see.truetransact.serverside.product.bills.BillsDAO;
import com.see.truetransact.serverside.product.locker.LockerProdDAO;

import java.util.HashMap;

import com.see.truetransact.serverside.TTDAOImpl;
import com.see.truetransact.serverexception.ServiceLocatorException;

/**
 * Bills Bean which calls the DAO.
 *
 */
public class LockerProdBean implements SessionBean, TTDAOImpl {

    LockerProdDAO daoLockerProd = null;

    public void ejbCreate() throws CreateException, ServiceLocatorException {
        daoLockerProd = new LockerProdDAO();
    }

    public void ejbRemove() {
        daoLockerProd = null;
    }

    public void setSessionContext(SessionContext sess) {
    }

    public void ejbActivate() {
    }

    public void ejbPassivate() {
    }

    public HashMap execute(HashMap obj) throws Exception {
        return daoLockerProd.execute(obj);
    }

    public HashMap executeQuery(HashMap obj) throws Exception {
        return daoLockerProd.executeQuery(obj);
    }
}