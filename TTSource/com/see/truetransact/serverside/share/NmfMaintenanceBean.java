/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition.
 * 
 *
 * ShareResolutionBean.java
 * 
 * Created on Thu Apr 28 12:59:32 IST 2005
 */
package com.see.truetransact.serverside.share;

import com.see.truetransact.serverside.share.shareresolution.*;
import javax.ejb.SessionBean;
import javax.ejb.SessionContext;
import javax.ejb.CreateException;

import java.util.HashMap;

import com.see.truetransact.serverexception.ServiceLocatorException;

import com.see.truetransact.serverside.TTDAOImpl;

/**
 * ShareResolution Bean which calls the DAO.
 *
 */
public class NmfMaintenanceBean implements SessionBean, TTDAOImpl {

    NmfMaintenanceDAO daoNmfMaintenance = null;

    public void ejbCreate() throws CreateException, ServiceLocatorException {
        daoNmfMaintenance = new NmfMaintenanceDAO();
    }

    public void ejbRemove() {
        daoNmfMaintenance = null;
    }

    public void setSessionContext(SessionContext sess) {
    }

    public void ejbActivate() {
    }

    public void ejbPassivate() {
    }

    public HashMap execute(HashMap obj) throws Exception {
        return daoNmfMaintenance.execute(obj);
    }

    public HashMap executeQuery(HashMap obj) throws Exception {
        return daoNmfMaintenance.executeQuery(obj);
    }
}
