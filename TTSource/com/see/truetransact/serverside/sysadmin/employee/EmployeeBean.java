/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition.
 * 
 *
 * EmployeeBean.java
 * 
 * Created on Tue Feb 17 12:06:18 IST 2004
 */
package com.see.truetransact.serverside.sysadmin.employee;

import javax.ejb.SessionBean;
import javax.ejb.SessionContext;
import javax.ejb.CreateException;

import java.util.HashMap;

import com.see.truetransact.serverside.TTDAOImpl;
import com.see.truetransact.serverexception.ServiceLocatorException;

/**
 * Employee Bean which calls the DAO.
 *
 */
public class EmployeeBean implements SessionBean, TTDAOImpl {

    EmployeeDAO daoEmployee = null;

    public void ejbCreate() throws CreateException, ServiceLocatorException {
        daoEmployee = new EmployeeDAO();
    }

    public void ejbRemove() {
        daoEmployee = null;
    }

    public void setSessionContext(SessionContext sess) {
    }

    public void ejbActivate() {
    }

    public void ejbPassivate() {
    }

    public HashMap execute(HashMap obj) throws Exception {
        return daoEmployee.execute(obj);
    }

    public HashMap executeQuery(HashMap obj) throws Exception {
        return daoEmployee.executeQuery(obj);
    }
}
