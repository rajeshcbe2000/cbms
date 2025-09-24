/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition.
 * 
 *
 * GroupRoleBean.java
 * 
 * Created on Mon Apr 12 15:49:21 GMT+05:30 2004
 */
package com.see.truetransact.serverside.sysadmin.role;

import javax.ejb.SessionBean;
import javax.ejb.SessionContext;
import javax.ejb.CreateException;

import java.util.HashMap;

import com.see.truetransact.serverside.TTDAOImpl;
import com.see.truetransact.serverexception.ServiceLocatorException;

/**
 * GroupRoleBean which calls the DAO.
 *
 */
public class GroupRoleBean implements SessionBean, TTDAOImpl {

    GroupRoleDAO daoRole = null;

    public void ejbCreate() throws CreateException, ServiceLocatorException {
        daoRole = new GroupRoleDAO();
    }

    public void ejbRemove() {
        daoRole = null;
    }

    public void setSessionContext(SessionContext sess) {
    }

    public void ejbActivate() {
    }

    public void ejbPassivate() {
    }

    public HashMap execute(HashMap obj) throws Exception {
        return daoRole.execute(obj);
    }

    public HashMap executeQuery(HashMap obj) throws Exception {
        return daoRole.executeQuery(obj);
    }
}