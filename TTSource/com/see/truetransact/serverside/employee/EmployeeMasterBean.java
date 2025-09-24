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
package com.see.truetransact.serverside.employee;

import javax.ejb.SessionBean;
import javax.ejb.SessionContext;
import javax.ejb.CreateException;

import java.rmi.RemoteException;
import java.util.HashMap;

import com.see.truetransact.serverside.employee.EmployeeMasterDAO;
import com.see.truetransact.serverexception.ServiceLocatorException;
import com.see.truetransact.serverside.TTDAOImpl;

/**
 * Customer Bean which calls CustomerDAO
 *
 * @author Balachandar
 */
public class EmployeeMasterBean implements SessionBean, TTDAOImpl {

    private EmployeeMasterDAO objEmployeeMasterDAO;

    public void ejbCreate() throws CreateException, ServiceLocatorException {
        objEmployeeMasterDAO = new EmployeeMasterDAO();
    }

    public void ejbRemove() {
        objEmployeeMasterDAO = null;
    }

    public void setSessionContext(SessionContext sess) {
    }

    public void ejbActivate() {
    }

    public void ejbPassivate() {
    }

    public HashMap execute(HashMap obj) throws Exception {
        return objEmployeeMasterDAO.execute(obj);
    }

    public HashMap executeQuery(HashMap obj) throws Exception {
        return objEmployeeMasterDAO.executeQuery(obj);
    }
}
