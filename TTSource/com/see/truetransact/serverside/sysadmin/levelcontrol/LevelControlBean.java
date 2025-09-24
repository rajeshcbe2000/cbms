/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition.
 * 
 *
 * LevelControlBean.java
 * 
 * Created on Tue Mar 02 12:54:53 IST 2004
 */
package com.see.truetransact.serverside.sysadmin.levelcontrol;

import javax.ejb.SessionBean;
import javax.ejb.SessionContext;
import javax.ejb.CreateException;

import java.util.HashMap;

import com.see.truetransact.serverside.TTDAOImpl;
import com.see.truetransact.serverexception.ServiceLocatorException;

/**
 * LevelControl Bean which calls the DAO.
 *
 */
public class LevelControlBean implements SessionBean, TTDAOImpl {

    LevelControlDAO daoLevelControl = null;

    public void ejbCreate() throws CreateException, ServiceLocatorException {
        daoLevelControl = new LevelControlDAO();
    }

    public void ejbRemove() {
        daoLevelControl = null;
    }

    public void setSessionContext(SessionContext sess) {
    }

    public void ejbActivate() {
    }

    public void ejbPassivate() {
    }

    public HashMap execute(HashMap obj) throws Exception {
        return daoLevelControl.execute(obj);
    }

    public HashMap executeQuery(HashMap obj) throws Exception {
        return daoLevelControl.executeQuery(obj);
    }
}
