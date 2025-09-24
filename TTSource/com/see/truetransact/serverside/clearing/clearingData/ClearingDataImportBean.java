/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition.
 * 
 *
 * ClearingDataBean.java
 * 
 * Created on Wed Mar 17 11:06:22 PST 2004
 */
package com.see.truetransact.serverside.clearing.clearingData;

import javax.ejb.SessionBean;
import javax.ejb.SessionContext;
import javax.ejb.CreateException;

import java.util.HashMap;

import com.see.truetransact.serverexception.ServiceLocatorException;
import com.see.truetransact.serverside.TTDAOImpl;

/**
 * ClearingData Bean which calls the DAO.
 *
 */
public class ClearingDataImportBean implements SessionBean, TTDAOImpl {

    ClearingDataImportDAO daoClearingData = null;

    public void ejbCreate() throws CreateException, ServiceLocatorException {
        daoClearingData = new ClearingDataImportDAO();
    }

    public void ejbRemove() {
        daoClearingData = null;
    }

    public void setSessionContext(SessionContext sess) {
    }

    public void ejbActivate() {
    }

    public void ejbPassivate() {
    }

    public HashMap execute(HashMap obj) throws Exception {
        return daoClearingData.execute(obj);
    }

    public HashMap executeQuery(HashMap obj) throws Exception {
        return daoClearingData.executeQuery(obj);
    }
}
