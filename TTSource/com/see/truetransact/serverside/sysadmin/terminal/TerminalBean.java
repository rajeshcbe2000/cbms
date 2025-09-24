/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition.
 * 
 *
 * TerminalBean.java
 * 
 * Created on Mon Feb 02 17:15:01 GMT+05:30 2004
 */
package com.see.truetransact.serverside.sysadmin.terminal;

import javax.ejb.SessionBean;
import javax.ejb.SessionContext;
import javax.ejb.CreateException;

import java.util.HashMap;

import com.see.truetransact.serverside.TTDAOImpl;
import com.see.truetransact.serverexception.ServiceLocatorException;

/**
 * Terminal Bean which calls the DAO.
 *
 */
public class TerminalBean implements SessionBean, TTDAOImpl {

    TerminalDAO daoTerminal = null;

    public void ejbCreate() throws CreateException, ServiceLocatorException {
        daoTerminal = new TerminalDAO();
    }

    public void ejbRemove() {
        daoTerminal = null;
    }

    public void setSessionContext(SessionContext sess) {
    }

    public void ejbActivate() {
    }

    public void ejbPassivate() {
    }

    public HashMap execute(HashMap obj) throws Exception {
        return daoTerminal.execute(obj);
    }

    public HashMap executeQuery(HashMap obj) throws Exception {
        return daoTerminal.executeQuery(obj);
    }
}
