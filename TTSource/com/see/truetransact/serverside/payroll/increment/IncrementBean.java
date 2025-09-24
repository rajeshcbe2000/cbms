/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition.
 * 
 *
 * IncrementBean.java
 * 
 * Created on Fri Nov 14 10:00:00 IST 2014
 */
package com.see.truetransact.serverside.payroll.increment;

import javax.ejb.SessionBean;
import javax.ejb.SessionContext;
import javax.ejb.CreateException;
import java.util.HashMap;
import com.see.truetransact.serverexception.ServiceLocatorException;
import com.see.truetransact.serverside.TTDAOImpl;

/**
 * Increment Bean which calls the DAO.
 *
 */
public class IncrementBean implements SessionBean, TTDAOImpl {

    IncrementDAO daoIncrement = null;

    public void ejbCreate() throws CreateException, ServiceLocatorException {
        daoIncrement = new IncrementDAO();
    }

    public void ejbRemove() {
        daoIncrement = null;
    }

    public void setSessionContext(SessionContext sess) {
    }

    public void ejbActivate() {
    }

    public void ejbPassivate() {
    }

    public HashMap execute(HashMap obj) throws Exception {
        return daoIncrement.execute(obj);
    }

    public HashMap executeQuery(HashMap obj) throws Exception {
        return daoIncrement.executeQuery(obj);
    }
}
