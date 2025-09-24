/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition.
 **
 *
 * TDSExemptionBean.java
 * 
 * Created on Wed Feb 02 10:32:55 IST 2005
 */
package com.see.truetransact.serverside.tds.tdsexemption;

import javax.ejb.SessionBean;
import javax.ejb.SessionContext;
import javax.ejb.CreateException;

import java.util.HashMap;

import com.see.truetransact.serverexception.ServiceLocatorException;

import com.see.truetransact.serverside.TTDAOImpl;

/**
 * TDSExemption Bean which calls the DAO.
 *
 */
public class TDSExemptionBean implements SessionBean, TTDAOImpl {

    TDSExemptionDAO daoTDSExemption = null;

    public void ejbCreate() throws CreateException, ServiceLocatorException {
        daoTDSExemption = new TDSExemptionDAO();
    }

    public void ejbRemove() {
        daoTDSExemption = null;
    }

    public void setSessionContext(SessionContext sess) {
    }

    public void ejbActivate() {
    }

    public void ejbPassivate() {
    }

    public HashMap execute(HashMap obj) throws Exception {
        return daoTDSExemption.execute(obj);
    }

    public HashMap executeQuery(HashMap obj) throws Exception {
        return daoTDSExemption.executeQuery(obj);
    }
}
