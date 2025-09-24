/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition. 
 * 
 * AdvancesProductBean.java
 *
 * Created on December 25, 2003, 5:10 PM
 */
package com.see.truetransact.serverside.product.advances;

import javax.ejb.SessionBean;
import javax.ejb.SessionContext;
import javax.ejb.CreateException;

import java.rmi.RemoteException;
import java.util.HashMap;

import com.see.truetransact.transferobject.product.advances.*;
import com.see.truetransact.commonutil.TTException;
import com.see.truetransact.serverside.TTDAOImpl;
import com.see.truetransact.serverexception.ServiceLocatorException;

/**
 * OperativeAcctProductBean which calls OperativeAcctProduct DAO
 *
 * @author Hemant
 */
public class AdvancesProductBean implements SessionBean, TTDAOImpl {

    AdvancesProductDAO advancesProductDAO = null;

    public void ejbCreate() throws CreateException, ServiceLocatorException {
        advancesProductDAO = new AdvancesProductDAO();
    }

    public void ejbRemove() {
        advancesProductDAO = null;
    }

    public void setSessionContext(SessionContext sess) {
    }

    public void ejbActivate() {
    }

    public void ejbPassivate() {
    }

    public HashMap execute(HashMap obj) throws Exception {
        return advancesProductDAO.execute(obj);
    }

    public HashMap executeQuery(HashMap obj) throws Exception {
        return advancesProductDAO.executeQuery(obj);
    }
}
