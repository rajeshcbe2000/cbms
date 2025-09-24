/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition.
 * 
 *
 * RecoveryListTallyBean.java
 * 
 * Created on Tue Oct 11 13:18:08 IST 2011
 */
package com.see.truetransact.serverside.salaryrecovery;

import javax.ejb.SessionBean;
import javax.ejb.SessionContext;
import javax.ejb.CreateException;

import java.util.HashMap;

import com.see.truetransact.serverside.TTDAOImpl;
import com.see.truetransact.serverexception.ServiceLocatorException;

/**
 *
 *
 * @author Suresh
 */
public class RecoveryListTallyBean implements SessionBean, TTDAOImpl {

    RecoveryListTallyDAO recoveryListTallyDAO = null;

    public void ejbCreate() throws CreateException, ServiceLocatorException {
        recoveryListTallyDAO = new RecoveryListTallyDAO();
    }

    public void ejbRemove() {
        recoveryListTallyDAO = null;
    }

    public void setSessionContext(SessionContext sess) {
    }

    public void ejbActivate() {
    }

    public void ejbPassivate() {
    }

    public HashMap execute(HashMap obj) throws Exception {
        return recoveryListTallyDAO.execute(obj);
    }

    public HashMap executeQuery(HashMap obj) throws Exception {
        return recoveryListTallyDAO.executeQuery(obj);
    }
}
