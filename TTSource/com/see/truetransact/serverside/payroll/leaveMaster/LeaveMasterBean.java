/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition. 
 * 
 * LeaveMasterBean.java
 *
 * 
 */
package com.see.truetransact.serverside.payroll.leaveMaster;

import com.see.truetransact.serverexception.ServiceLocatorException;
import com.see.truetransact.serverside.TTDAOImpl;
import java.util.HashMap;
import javax.ejb.CreateException;
import javax.ejb.SessionBean;

public class LeaveMasterBean implements SessionBean, TTDAOImpl {

    LeaveMasterDAO daoLeaveManagement = null;

    public void ejbCreate() throws CreateException, ServiceLocatorException {
        System.out.println("Inside Create");
        daoLeaveManagement = new LeaveMasterDAO();
    }

    public void ejbActivate() {
        System.out.println("Inside Activate");
    }

    public void ejbPassivate() {
        System.out.println("Inside Passivate");
    }

    public void ejbRemove() {
        System.out.println("Inside Remove");
        daoLeaveManagement = null;
    }

    public void setSessionContext(javax.ejb.SessionContext sessionContext) {
        System.out.println("Inside Session");
    }

    public HashMap execute(HashMap obj) throws Exception {
        System.out.println("Inside execute");
        return daoLeaveManagement.execute(obj);
    }

    public HashMap executeQuery(HashMap obj) throws Exception {
        System.out.println("Inside execute query");
        return daoLeaveManagement.executeQuery(obj);
    }
}