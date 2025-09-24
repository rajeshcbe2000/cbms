/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition.
 * 
 *
 * InterestMaintenanceGroupBean.java
 * 
 * Created on Tue May 25 10:39:44 IST 2004
 */
package com.see.truetransact.serverside.sysadmin.servicetax;

import javax.ejb.SessionBean;
import javax.ejb.SessionContext;
import javax.ejb.CreateException;

import java.util.HashMap;

import com.see.truetransact.serverside.TTDAOImpl;
import com.see.truetransact.serverexception.ServiceLocatorException;

/**
 * InterestMaintenanceGroup Bean which calls the DAO.
 *
 */
public class ServiceTaxMaintenanceGroupBean implements SessionBean, TTDAOImpl {

    ServiceTaxMaintenanceGroupDAO daoServiceTaxMaintenanceGroup = null;

    public void ejbCreate() throws CreateException, ServiceLocatorException {
        daoServiceTaxMaintenanceGroup = new ServiceTaxMaintenanceGroupDAO();
    }

    public void ejbRemove() {
        daoServiceTaxMaintenanceGroup = null;
    }

    public void setSessionContext(SessionContext sess) {
    }

    public void ejbActivate() {
    }

    public void ejbPassivate() {
    }

    public HashMap execute(HashMap obj) throws Exception {
        return daoServiceTaxMaintenanceGroup.execute(obj);
    }

    public HashMap executeQuery(HashMap obj) throws Exception {
        return daoServiceTaxMaintenanceGroup.executeQuery(obj);
    }
}
