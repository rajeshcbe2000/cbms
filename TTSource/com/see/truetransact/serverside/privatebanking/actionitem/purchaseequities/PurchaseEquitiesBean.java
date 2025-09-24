/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition.
 * 
 *
 * PurchaseEquitiesBean.java
 * 
 * Created on Thu Jul 08 14:19:49 GMT+05:30 2004
 */
package com.see.truetransact.serverside.privatebanking.actionitem.purchaseequities;

import javax.ejb.SessionBean;
import javax.ejb.SessionContext;
import javax.ejb.CreateException;

import java.util.HashMap;

import com.see.truetransact.serverside.TTDAOImpl;
import com.see.truetransact.serverexception.ServiceLocatorException;

/**
 * PurchaseEquities Bean which calls the DAO.
 *
 */
public class PurchaseEquitiesBean implements SessionBean, TTDAOImpl {

    PurchaseEquitiesDAO daoPurchaseEquities = null;

    public void ejbCreate() throws CreateException, ServiceLocatorException {
        daoPurchaseEquities = new PurchaseEquitiesDAO();
    }

    public void ejbRemove() {
        daoPurchaseEquities = null;
    }

    public void setSessionContext(SessionContext sess) {
    }

    public void ejbActivate() {
    }

    public void ejbPassivate() {
    }

    public HashMap execute(HashMap obj) throws Exception {
        return daoPurchaseEquities.execute(obj);
    }

    public HashMap executeQuery(HashMap obj) throws Exception {
        return daoPurchaseEquities.executeQuery(obj);
    }
}
