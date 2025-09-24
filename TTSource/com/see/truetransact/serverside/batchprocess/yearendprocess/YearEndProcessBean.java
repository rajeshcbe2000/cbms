/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition.
 * 
 *
 * CarrierBean.java
 * 
 * Created on Wed Jan 05 14:59:17 IST 2005
 */
package com.see.truetransact.serverside.batchprocess.yearendprocess;

import javax.ejb.SessionBean;
import javax.ejb.SessionContext;
import javax.ejb.CreateException;

import java.util.HashMap;

import com.see.truetransact.serverexception.ServiceLocatorException;

import com.see.truetransact.serverside.TTDAOImpl;

/**
 * Carrier Bean which calls the DAO.
 *
 */
public class YearEndProcessBean implements SessionBean, TTDAOImpl {

    YearEndProcessDAO daoYearEndProcess = null;

    public void ejbCreate() throws CreateException, ServiceLocatorException {
        daoYearEndProcess = new YearEndProcessDAO();
    }

    public void ejbRemove() {
        daoYearEndProcess = null;
    }

    public void setSessionContext(SessionContext sess) {
    }

    public void ejbActivate() {
    }

    public void ejbPassivate() {
    }

    public HashMap execute(HashMap obj) throws Exception {
        return daoYearEndProcess.execute(obj);
    }

    public HashMap executeQuery(HashMap obj) throws Exception {
        return daoYearEndProcess.executeQuery(obj);
    }
}
