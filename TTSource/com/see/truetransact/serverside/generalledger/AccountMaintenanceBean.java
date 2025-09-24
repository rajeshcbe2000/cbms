/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition.  
 * 
 *
 * AccountMaintenanceBean.java
 *
 * Created on Septmeber 2, 2003, 3:20 AM
 */
package com.see.truetransact.serverside.generalledger;

import javax.ejb.SessionBean;
import javax.ejb.SessionContext;
import javax.ejb.CreateException;

import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.HashMap;

import com.see.truetransact.transferobject.generalledger.*;
import com.see.truetransact.commonutil.TTException;
import com.see.truetransact.serverside.TTDAOImpl;
import com.see.truetransact.serverexception.ServiceLocatorException;

/**
 * AccountMaintenanceBean which calls AccountMaintenance DAO
 *
 * @author Balachandar
 * @author Annamalai
 */
public class AccountMaintenanceBean implements SessionBean, TTDAOImpl {

    AccountMaintenanceDAO _accountMaintenanceDAO;

    public void ejbCreate() throws CreateException, ServiceLocatorException {
        _accountMaintenanceDAO = new AccountMaintenanceDAO();
    }

    public void ejbRemove() {
        _accountMaintenanceDAO = null;
    }

    public void setSessionContext(SessionContext sess) {
    }

    public void ejbActivate() {
    }

    public void ejbPassivate() {
    }

    public HashMap execute(HashMap param) throws Exception {
        return _accountMaintenanceDAO.execute(param);
    }

    public HashMap executeQuery(HashMap param) throws Exception {
        return _accountMaintenanceDAO.executeQuery(param);
    }
}
