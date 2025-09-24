/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition.
 * 
 *
 * InspectionBean.java
 * 
 * Created on Wed Jun 09 16:13:30 GMT+05:30 2004
 */
package com.see.truetransact.serverside.sysadmin.audit;

import javax.ejb.SessionBean;
import javax.ejb.SessionContext;
import javax.ejb.CreateException;

import java.util.HashMap;

import com.see.truetransact.serverside.TTDAOImpl;
import com.see.truetransact.serverexception.ServiceLocatorException;

/**
 * Inspection Bean which calls the DAO.
 *
 */
public class InspectionBean implements SessionBean, TTDAOImpl {

    InspectionDAO daoInspection = null;

    public void ejbCreate() throws CreateException, ServiceLocatorException {
        daoInspection = new InspectionDAO();
    }

    public void ejbRemove() {
        daoInspection = null;
    }

    public void setSessionContext(SessionContext sess) {
    }

    public void ejbActivate() {
    }

    public void ejbPassivate() {
    }

    public HashMap execute(HashMap obj) throws Exception {
        return daoInspection.execute(obj);
    }

    public HashMap executeQuery(HashMap obj) throws Exception {
        return daoInspection.executeQuery(obj);
    }
}
