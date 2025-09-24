/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition.
 * 
 *
 * TDSConfigBean.java
 * 
 * Created on Mon Jan 31 16:34:23 IST 2005
 */
package com.see.truetransact.serverside.product.dailyCommission;

import javax.ejb.SessionBean;
import javax.ejb.SessionContext;
import javax.ejb.CreateException;

import java.util.HashMap;

import com.see.truetransact.serverexception.ServiceLocatorException;

import com.see.truetransact.serverside.TTDAOImpl;

/**
 * TDSConfig Bean which calls the DAO.
 *
 */
public class DailyCommissionBean implements SessionBean, TTDAOImpl {

    DailyCommissionDAO daoDailyCommission = null;

    public void ejbCreate() throws CreateException, ServiceLocatorException {
        daoDailyCommission = new DailyCommissionDAO();
    }

    public void ejbRemove() {
        daoDailyCommission = null;
    }

    public void setSessionContext(SessionContext sess) {
    }

    public void ejbActivate() {
    }

    public void ejbPassivate() {
    }

    public HashMap execute(HashMap obj) throws Exception {
        return daoDailyCommission.execute(obj);
    }

    public HashMap executeQuery(HashMap obj) throws Exception {
        return daoDailyCommission.executeQuery(obj);
    }
}
