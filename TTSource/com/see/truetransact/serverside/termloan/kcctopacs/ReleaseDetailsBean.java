/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition.
 **
 *
 * ReleaseDetailsBean.java
 * 
 * Created on Fri Apr 19 11:33:46 IST 2013
 */
package com.see.truetransact.serverside.termloan.kcctopacs;

import javax.ejb.SessionBean;
import javax.ejb.SessionContext;
import javax.ejb.CreateException;

import java.util.HashMap;

import com.see.truetransact.serverside.TTDAOImpl;
import com.see.truetransact.serverexception.ServiceLocatorException;

/**
 * ReleaseDetailsBean which calls ReleaseDetailsDAO
 *
 * @author Suresh
 */
public class ReleaseDetailsBean implements SessionBean, TTDAOImpl {

    ReleaseDetailsDAO releaseDetailsDAO = null;

    public void ejbCreate() throws CreateException, ServiceLocatorException {
        releaseDetailsDAO = new ReleaseDetailsDAO();
    }

    public void ejbRemove() {
        releaseDetailsDAO = null;
    }

    public void setSessionContext(SessionContext sess) {
    }

    public void ejbActivate() {
    }

    public void ejbPassivate() {
    }

    public HashMap execute(HashMap obj) throws Exception {
        return releaseDetailsDAO.execute(obj);
    }

    public HashMap executeQuery(HashMap obj) throws Exception {
        return releaseDetailsDAO.executeQuery(obj);
    }
}
