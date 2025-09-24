/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition.  
 **
 *
 * AccountsBean.java
 *
 * Created on September 08, 2003, 1:00 PM
 */
package com.see.truetransact.serverside.locker.lockerissue;

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
public class LockerIssueBean implements SessionBean, TTDAOImpl {

    LockerIssueDAO lockerissueDAO = null;

    public void ejbCreate() throws CreateException, ServiceLocatorException {
        lockerissueDAO = new LockerIssueDAO();
    }

    public void ejbRemove() {
        lockerissueDAO = null;
    }

    public void setSessionContext(SessionContext sess) {
    }

    public void ejbActivate() {
    }

    public void ejbPassivate() {
    }

    public HashMap execute(HashMap hash) throws Exception {
        return lockerissueDAO.execute(hash);
    }

    public HashMap executeQuery(HashMap hash) throws Exception {
        return lockerissueDAO.executeQuery(hash);
    }
}
