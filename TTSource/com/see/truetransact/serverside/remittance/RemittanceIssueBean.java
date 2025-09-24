/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition.
 * 
 *
 * RemittanceIssueBean.java
 * 
 * Created on Fri Jan 09 17:36:32 IST 2004
 */
package com.see.truetransact.serverside.remittance;

import javax.ejb.SessionBean;
import javax.ejb.SessionContext;
import javax.ejb.CreateException;

import java.util.HashMap;

import com.see.truetransact.serverside.TTDAOImpl;
import com.see.truetransact.serverexception.ServiceLocatorException;

/**
 * RemittanceIssue Bean which calls the DAO.
 *
 */
public class RemittanceIssueBean implements SessionBean, TTDAOImpl {

    RemittanceIssueDAO daoRemittanceIssue = null;

    public void ejbCreate() throws CreateException, ServiceLocatorException {
        daoRemittanceIssue = new RemittanceIssueDAO();
    }

    public void ejbRemove() {
        daoRemittanceIssue = null;
    }

    public void setSessionContext(SessionContext sess) {
    }

    public void ejbActivate() {
    }

    public void ejbPassivate() {
    }

    public HashMap execute(HashMap obj) throws Exception {
        return daoRemittanceIssue.execute(obj);
    }

    public HashMap executeQuery(HashMap obj) throws Exception {
        return daoRemittanceIssue.executeQuery(obj);
    }
}
