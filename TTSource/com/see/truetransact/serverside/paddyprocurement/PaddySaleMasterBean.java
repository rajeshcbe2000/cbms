/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition.
 * 
 *
 * PaddySaleMasterBean.java
 * 
 * Created on Fri Jun 10 15:52:50 IST 2011
 */
package com.see.truetransact.serverside.paddyprocurement;

import javax.ejb.SessionBean;
import javax.ejb.SessionContext;
import javax.ejb.CreateException;

import java.util.HashMap;

import com.see.truetransact.serverexception.ServiceLocatorException;

import com.see.truetransact.serverside.TTDAOImpl;

/**
 * PaddySaleMaster Bean which calls the DAO.
 *
 */
public class PaddySaleMasterBean implements SessionBean, TTDAOImpl {

    PaddySaleMasterDAO daoPaddySaleMaster = null;

    public void ejbCreate() throws CreateException, ServiceLocatorException {
        daoPaddySaleMaster = new PaddySaleMasterDAO();
    }

    public void ejbRemove() {
        daoPaddySaleMaster = null;
    }

    public void setSessionContext(SessionContext sess) {
    }

    public void ejbActivate() {
    }

    public void ejbPassivate() {
    }

    public HashMap execute(HashMap obj) throws Exception {
        return daoPaddySaleMaster.execute(obj);
    }

    public HashMap executeQuery(HashMap obj) throws Exception {
        return daoPaddySaleMaster.executeQuery(obj);
    }
}
