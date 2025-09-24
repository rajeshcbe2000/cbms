/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition.
 * 
 *
 * MultiLevelBean.java
 * 
 * Created on Thu Sep 09 15:32:52 GMT+05:30 2004
 */
package com.see.truetransact.serverside.sysadmin.levelcontrol.multilevel;

import javax.ejb.SessionBean;
import javax.ejb.SessionContext;
import javax.ejb.CreateException;

import java.util.HashMap;

import com.see.truetransact.serverside.TTDAOImpl;
import com.see.truetransact.serverexception.ServiceLocatorException;

/**
 * MultiLevel Bean which calls the DAO.
 *
 */
public class MultiLevelBean implements SessionBean, TTDAOImpl {

    MultiLevelDAO daoMultiLevel = null;

    public void ejbCreate() throws CreateException, ServiceLocatorException {
        daoMultiLevel = new MultiLevelDAO();
    }

    public void ejbRemove() {
        daoMultiLevel = null;
    }

    public void setSessionContext(SessionContext sess) {
    }

    public void ejbActivate() {
    }

    public void ejbPassivate() {
    }

    public HashMap execute(HashMap obj) throws Exception {
        return daoMultiLevel.execute(obj);
    }

    public HashMap executeQuery(HashMap obj) throws Exception {
        return daoMultiLevel.executeQuery(obj);
    }
}
