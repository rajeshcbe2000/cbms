/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition.
 * 
 *
 * ForexDenominationMasterBean.java
 * 
 * Created on Thu Jan 27 12:22:03 IST 2005
 */
package com.see.truetransact.serverside.sysadmin.denomination;

import javax.ejb.SessionBean;
import javax.ejb.SessionContext;
import javax.ejb.CreateException;

import java.util.HashMap;

import com.see.truetransact.serverexception.ServiceLocatorException;

import com.see.truetransact.serverside.TTDAOImpl;

/**
 * ForexDenominationMaster Bean which calls the DAO.
 *
 */
public class ForexDenominationMasterBean implements SessionBean, TTDAOImpl {

    ForexDenominationMasterDAO daoForexDenominationMaster = null;

    public void ejbCreate() throws CreateException, ServiceLocatorException {
        daoForexDenominationMaster = new ForexDenominationMasterDAO();
    }

    public void ejbRemove() {
        daoForexDenominationMaster = null;
    }

    public void setSessionContext(SessionContext sess) {
    }

    public void ejbActivate() {
    }

    public void ejbPassivate() {
    }

    public HashMap execute(HashMap obj) throws Exception {
        return daoForexDenominationMaster.execute(obj);
    }

    public HashMap executeQuery(HashMap obj) throws Exception {
        return daoForexDenominationMaster.executeQuery(obj);
    }
}
