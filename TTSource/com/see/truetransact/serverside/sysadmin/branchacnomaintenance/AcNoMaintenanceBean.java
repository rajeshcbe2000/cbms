/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition.
 * 
 *
 * AcNoMaintenanceTOBean.java
 * 
 * Created on Fri Jan 23 16:33:10 IST 2004
 */
package com.see.truetransact.serverside.sysadmin.branchacnomaintenance;

import javax.ejb.SessionBean;
import javax.ejb.SessionContext;
import javax.ejb.CreateException;

import java.util.HashMap;

import com.see.truetransact.serverside.TTDAOImpl;
import com.see.truetransact.serverexception.ServiceLocatorException;

/**
 * AcNoMaintenanceTO Bean which calls the DAO.
 *
 */
public class AcNoMaintenanceBean implements SessionBean, TTDAOImpl {

    AcNoMaintenanceDAO daoAcNoMaintenanceTO = null;

    public void ejbCreate() throws CreateException, ServiceLocatorException {
        daoAcNoMaintenanceTO = new AcNoMaintenanceDAO();
    }

    public void ejbRemove() {
        daoAcNoMaintenanceTO = null;
    }

    public void setSessionContext(SessionContext sess) {
    }

    public void ejbActivate() {
    }

    public void ejbPassivate() {
    }

    public HashMap execute(HashMap obj) throws Exception {
        return daoAcNoMaintenanceTO.execute(obj);
    }

    public HashMap executeQuery(HashMap obj) throws Exception {
        return daoAcNoMaintenanceTO.executeQuery(obj);
    }
}
