/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition.
 * 
 *
 * TokenConfigBean.java
 * 
 * Created on Thu Jan 20 17:19:05 IST 2005
 */
package com.see.truetransact.serverside.customer.customeridchange;

import javax.ejb.SessionBean;
import javax.ejb.SessionContext;
import javax.ejb.CreateException;

import java.util.HashMap;

import com.see.truetransact.serverexception.ServiceLocatorException;

import com.see.truetransact.serverside.TTDAOImpl;

/**
 * TokenConfig Bean which calls the DAO.
 *
 */
public class CustomerIdChangeBean implements SessionBean, TTDAOImpl {

    CustomerIdChangeDAO daoCustomerIdChange = null;

    public void ejbCreate() throws CreateException, ServiceLocatorException {
        daoCustomerIdChange = new CustomerIdChangeDAO();
    }

    public void ejbRemove() {
        daoCustomerIdChange = null;
    }

    public void setSessionContext(SessionContext sess) {
    }

    public void ejbActivate() {
    }

    public void ejbPassivate() {
    }

    public HashMap execute(HashMap obj) throws Exception {
        return daoCustomerIdChange.execute(obj);
    }

    public HashMap executeQuery(HashMap obj) throws Exception {
        return daoCustomerIdChange.executeQuery(obj);
    }
}
