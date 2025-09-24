/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition.
 * 
 *
 * BranchManagementBean.java
 *
 * Created on September 08, 2003, 1:00 PM
 */
package com.see.truetransact.serverside.sysadmin.branch;

import javax.ejb.SessionBean;
import javax.ejb.SessionContext;
import javax.ejb.CreateException;

import java.util.HashMap;

import com.see.truetransact.commonutil.TTException;
import com.see.truetransact.serverside.TTDAOImpl;
import com.see.truetransact.serverexception.ServiceLocatorException;

/**
 * BranchManagementBean which calls AccountsBeanDAO
 *
 * @author Karthik
 */
public class BranchManagementBean implements SessionBean, TTDAOImpl {

    private BranchManagementDAO branchManagementDAO;

    public void ejbCreate() throws CreateException, ServiceLocatorException {
        branchManagementDAO = new BranchManagementDAO();
    }

    public void ejbRemove() {
        branchManagementDAO = null;
    }

    public void setSessionContext(SessionContext sess) {
    }

    public void ejbActivate() {
    }

    public void ejbPassivate() {
    }

    public HashMap execute(HashMap obj) throws Exception {
        return branchManagementDAO.execute(obj);
    }

    public HashMap executeQuery(HashMap obj) throws Exception {
        return branchManagementDAO.executeQuery(obj);
    }
}
