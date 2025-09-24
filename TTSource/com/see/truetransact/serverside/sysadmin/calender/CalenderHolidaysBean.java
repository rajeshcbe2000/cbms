/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition.
 * 
 *
 * CalenderHolidaysTOBean.java
 * 
 * Created on Fri Jan 23 16:33:10 IST 2004
 */
package com.see.truetransact.serverside.sysadmin.calender;

import javax.ejb.SessionBean;
import javax.ejb.SessionContext;
import javax.ejb.CreateException;

import java.util.HashMap;

import com.see.truetransact.serverside.TTDAOImpl;
import com.see.truetransact.serverexception.ServiceLocatorException;

/**
 * CalenderHolidaysTO Bean which calls the DAO.
 *
 */
public class CalenderHolidaysBean implements SessionBean, TTDAOImpl {

    CalenderHolidaysDAO daoCalenderHolidaysTO = null;

    public void ejbCreate() throws CreateException, ServiceLocatorException {
        daoCalenderHolidaysTO = new CalenderHolidaysDAO();
    }

    public void ejbRemove() {
        daoCalenderHolidaysTO = null;
    }

    public void setSessionContext(SessionContext sess) {
    }

    public void ejbActivate() {
    }

    public void ejbPassivate() {
    }

    public HashMap execute(HashMap obj) throws Exception {
        return daoCalenderHolidaysTO.execute(obj);
    }

    public HashMap executeQuery(HashMap obj) throws Exception {
        return daoCalenderHolidaysTO.executeQuery(obj);
    }
}
