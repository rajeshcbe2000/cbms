/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition.  
 * 
 *
 * AccountsBean.java
 *
 * Created on September 08, 2003, 1:00 PM
 */
package com.see.truetransact.serverside.operativeaccount;

import javax.ejb.SessionBean;
import javax.ejb.SessionContext;
import javax.ejb.CreateException;

import java.util.HashMap;

import com.see.truetransact.serverside.TTDAOImpl;
import com.see.truetransact.serverexception.ServiceLocatorException;

import com.see.truetransact.transferobject.operativeaccount.*;

/**
 * AccountsBean which calls AccountsBeanDAO
 *
 * @author Pranav
 */
public class AccountBean implements SessionBean, TTDAOImpl {

    AccountDAO accountDAO = null;

    public void ejbCreate() throws CreateException, ServiceLocatorException {
        accountDAO = new AccountDAO();
    }

    public void ejbRemove() {
        accountDAO = null;
    }

    public void setSessionContext(SessionContext sess) {
    }

    public void ejbActivate() {
    }

    public void ejbPassivate() {
    }

    public HashMap execute(HashMap hash) throws Exception {
        return accountDAO.execute(hash);
    }

    public HashMap executeQuery(HashMap hash) throws Exception {
        return accountDAO.executeQuery(hash);
    }
}
