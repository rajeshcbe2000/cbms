/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition. 
 * 
 * LeaveManagementBean.java
 *
 * Created on March 29, 2010, 3:42 PM
 */
package com.see.truetransact.serverside.sysadmin.leavemanagement;

import javax.ejb.SessionBean;
import javax.ejb.SessionContext;
import java.util.HashMap;
import javax.ejb.CreateException;
import com.see.truetransact.serverexception.ServiceLocatorException;

import com.see.truetransact.serverside.TTDAOImpl;

public class LeaveSanctionBean implements SessionBean, TTDAOImpl {

    LeaveSanctionDAO daoLeaveSanction = null;

    public void ejbCreate() throws CreateException, ServiceLocatorException {
        System.out.println("Inside Create");
        daoLeaveSanction = new LeaveSanctionDAO();
    }

    public void ejbActivate() {
        System.out.println("Inside Activate");
    }

    public void ejbPassivate() {
        System.out.println("Inside Passivate");
    }

    public void ejbRemove() {
        System.out.println("Inside Remove");
        daoLeaveSanction = null;
    }

    public void setSessionContext(javax.ejb.SessionContext sessionContext) {
        System.out.println("Inside Session");
    }

    public HashMap execute(HashMap obj) throws Exception {
        System.out.println("Inside execute");
        return daoLeaveSanction.execute(obj);
    }

    public HashMap executeQuery(HashMap obj) throws Exception {
        System.out.println("Inside execute query");
        return daoLeaveSanction.executeQuery(obj);
    }
}