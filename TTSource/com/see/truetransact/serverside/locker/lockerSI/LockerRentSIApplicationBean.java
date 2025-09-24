/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition.  
 * 
 * LockerRentSIApplicationBean.java
 *
 * Created on March 1, 2012, 2:01 PM
 */
package com.see.truetransact.serverside.locker.lockerSI;

import javax.ejb.SessionBean;
import javax.ejb.SessionContext;
import javax.ejb.CreateException;

import java.util.HashMap;

import com.see.truetransact.serverside.TTDAOImpl;
import com.see.truetransact.serverexception.ServiceLocatorException;

/**
 *
 * @author Rajesh
 */
public class LockerRentSIApplicationBean implements SessionBean, TTDAOImpl {

    LockerRentSIApplicationDAO lockerRentSIApplicationDAO = null;

    public void ejbCreate() throws CreateException, ServiceLocatorException {
        lockerRentSIApplicationDAO = new LockerRentSIApplicationDAO();
    }

    public void ejbRemove() {
        lockerRentSIApplicationDAO = null;
    }

    public void setSessionContext(SessionContext sess) {
    }

    public void ejbActivate() {
    }

    public void ejbPassivate() {
    }

    public HashMap execute(HashMap obj) throws Exception {
        return lockerRentSIApplicationDAO.execute(obj);
    }

    public HashMap executeQuery(HashMap obj) throws Exception {
        return lockerRentSIApplicationDAO.executeQuery(obj);
    }

    /**
     * Creates a new instance of LockerRentSIApplicationBean
     */
    public LockerRentSIApplicationBean() {
    }
}
