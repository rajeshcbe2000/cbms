/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition.  
 * 
 *
 * CustomerBean.java
 *
 * Created on June 23, 2003, 4:20 PM
 */
package com.see.truetransact.serverside.customer;

import javax.ejb.SessionBean;
import javax.ejb.SessionContext;
import javax.ejb.CreateException;

import java.rmi.RemoteException;
import java.util.HashMap;

import com.see.truetransact.serverside.customer.CustomerDAO;
import com.see.truetransact.serverexception.ServiceLocatorException;
import com.see.truetransact.serverside.TTDAOImpl;

/**
 * Customer Bean which calls CustomerDAO
 *
 * @author Balachandar
 */
public class CustomerBean implements SessionBean, TTDAOImpl {

    private CustomerDAO objCustomerDAO;

    public void ejbCreate() throws CreateException, ServiceLocatorException {
        objCustomerDAO = new CustomerDAO();
    }

    public void ejbRemove() {
        objCustomerDAO = null;
    }

    public void setSessionContext(SessionContext sess) {
    }

    public void ejbActivate() {
    }

    public void ejbPassivate() {
    }

    public HashMap execute(HashMap obj) throws Exception {
        return objCustomerDAO.execute(obj);
    }

    public HashMap executeQuery(HashMap obj) throws Exception {
        return objCustomerDAO.executeQuery(obj);
    }
}
