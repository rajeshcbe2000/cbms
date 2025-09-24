/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition. 
 * 
 * AgriCardBean.java
 *
 * Created on January 1, 2009, 6:02 PM
 */
package com.see.truetransact.serverside.product.loan.agriculturecard;

import java.beans.*;
import javax.ejb.SessionBean;
import javax.ejb.SessionContext;
import javax.ejb.CreateException;

import java.rmi.RemoteException;
import java.util.HashMap;

import com.see.truetransact.transferobject.product.operativeacct.*;
import com.see.truetransact.commonutil.TTException;
import com.see.truetransact.serverside.TTDAOImpl;
import com.see.truetransact.serverexception.ServiceLocatorException;

/**
 *
 * @author Administrator
 */
public class AgriCardBean implements SessionBean, TTDAOImpl {

    AgriCardDAO objAgriCardDao = null;

    /**
     * Creates a new instance of AgriCardBean
     */
    public void ejbCreate() throws CreateException, ServiceLocatorException {
        objAgriCardDao = new AgriCardDAO();
    }

    public void ejbActivate() throws javax.ejb.EJBException, java.rmi.RemoteException {
    }

    public void ejbPassivate() throws javax.ejb.EJBException, java.rmi.RemoteException {
    }

    public void ejbRemove() throws javax.ejb.EJBException, java.rmi.RemoteException {
        objAgriCardDao = null;
    }

    public HashMap execute(HashMap obj) throws Exception {
        return objAgriCardDao.execute(obj);
    }

    public HashMap executeQuery(HashMap obj) throws Exception {
        return objAgriCardDao.executeQuery(obj);
    }

    public void setSessionContext(javax.ejb.SessionContext sessionContext) throws javax.ejb.EJBException, java.rmi.RemoteException {
    }
}
