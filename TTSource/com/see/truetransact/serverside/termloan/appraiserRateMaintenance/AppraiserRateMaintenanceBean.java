/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved..
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition.
 **
 *
 * BranchGroupBean.java
 * 
 * Created on Thu Aug 25 11:43:54 IST 2005
 */
package com.see.truetransact.serverside.termloan.appraiserRateMaintenance;

import javax.ejb.SessionBean;
import javax.ejb.SessionContext;
import javax.ejb.CreateException;

import java.util.HashMap;

import com.see.truetransact.serverexception.ServiceLocatorException;

import com.see.truetransact.serverside.TTDAOImpl;

/**
 * BranchGroup Bean which calls the DAO.
 *
 */
public class AppraiserRateMaintenanceBean implements SessionBean, TTDAOImpl {

    AppraiserRateMaintenanceDAO daoBranchGroup = null;

    public void ejbCreate() throws CreateException, ServiceLocatorException {
        daoBranchGroup = new AppraiserRateMaintenanceDAO();
    }

    public void ejbRemove() {
        daoBranchGroup = null;
    }

    public void setSessionContext(SessionContext sess) {
    }

    public void ejbActivate() {
    }

    public void ejbPassivate() {
    }

    public HashMap execute(HashMap obj) throws Exception {
        return daoBranchGroup.execute(obj);
    }

    public HashMap executeQuery(HashMap obj) throws Exception {
        return daoBranchGroup.executeQuery(obj);
    }
}
