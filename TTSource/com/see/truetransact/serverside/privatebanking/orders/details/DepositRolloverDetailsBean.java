/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition.
 * 
 *
 * DepositRolloverDetailsBean.java
 * 
 * Created on Wed Jul 07 17:46:29 GMT+05:30 2004
 */
package com.see.truetransact.serverside.privatebanking.orders.details;

import javax.ejb.SessionBean;
import javax.ejb.SessionContext;
import javax.ejb.CreateException;
import com.see.truetransact.serverexception.ServiceLocatorException;
import com.see.truetransact.serverside.TTDAOImpl;
import java.util.HashMap;

/**
 * DepositRolloverDetails Bean which calls the DAO.
 *
 */
public class DepositRolloverDetailsBean implements SessionBean, TTDAOImpl {

    DepositRolloverDetailsDAO daoDepositRolloverDetails = null;

    public void ejbCreate() throws CreateException, ServiceLocatorException {
        daoDepositRolloverDetails = new DepositRolloverDetailsDAO();
    }

    public void ejbRemove() {
        daoDepositRolloverDetails = null;
    }

    public void setSessionContext(SessionContext sess) {
    }

    public void ejbActivate() {
    }

    public void ejbPassivate() {
    }

    public HashMap execute(HashMap obj) throws Exception {
        return daoDepositRolloverDetails.execute(obj);
    }

    public HashMap executeQuery(HashMap obj) throws Exception {
        return daoDepositRolloverDetails.executeQuery(obj);
    }
}
