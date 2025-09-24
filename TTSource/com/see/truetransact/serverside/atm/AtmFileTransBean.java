/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition.
 * 
 *
 * AtmFileTransBean.java
 * 
 * Created on Wed Feb 02 13:11:28 IST 2005
 */
package com.see.truetransact.serverside.atm;

import javax.ejb.SessionBean;
import javax.ejb.SessionContext;
import javax.ejb.CreateException;

import java.util.HashMap;

import com.see.truetransact.serverexception.ServiceLocatorException;

import com.see.truetransact.serverside.TTDAOImpl;
import com.see.truetransact.serverside.atm.AtmFileTransDAO;

/**
 * AtmFileTrans Bean which calls the DAO.
 *
 */
public class AtmFileTransBean implements SessionBean, TTDAOImpl {

    AtmFileTransDAO atmFileTransDAO = null;

    public void ejbCreate() throws CreateException, ServiceLocatorException {
        atmFileTransDAO = new AtmFileTransDAO();
    }

    public void ejbRemove() {
        atmFileTransDAO = null;
    }

    public void setSessionContext(SessionContext sess) {
    }

    public void ejbActivate() {
    }

    public void ejbPassivate() {
    }

    public HashMap execute(HashMap obj) throws Exception {
        return atmFileTransDAO.execute(obj);
    }

    public HashMap executeQuery(HashMap obj) throws Exception {
        return atmFileTransDAO.executeQuery(obj);
    }
}
