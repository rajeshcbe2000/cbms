/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition.
 * 
 *
 * BranchGLBean.java
 * 
 * Created on Fri Dec 31 11:43:54 IST 2004
 */
package com.see.truetransact.serverside.generalledger.branchgl;

import javax.ejb.SessionBean;
import javax.ejb.SessionContext;
import javax.ejb.CreateException;

import java.util.HashMap;

import com.see.truetransact.serverexception.ServiceLocatorException;

import com.see.truetransact.serverside.TTDAOImpl;

/**
 * BranchGL Bean which calls the DAO.
 *
 */
public class BranchGLBean implements SessionBean, TTDAOImpl {

    BranchGLDAO daoBranchGL = null;

    public void ejbCreate() throws CreateException, ServiceLocatorException {
        daoBranchGL = new BranchGLDAO();
    }

    public void ejbRemove() {
        daoBranchGL = null;
    }

    public void setSessionContext(SessionContext sess) {
    }

    public void ejbActivate() {
    }

    public void ejbPassivate() {
    }

    public HashMap execute(HashMap obj) throws Exception {
        return daoBranchGL.execute(obj);
    }

    public HashMap executeQuery(HashMap obj) throws Exception {
        return daoBranchGL.executeQuery(obj);
    }
}
