/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition.
 * 
 *
 * RemittanceProductBean.java
 * 
 * Created on Wed Jan 07 19:24:17 IST 2004
 */
package com.see.truetransact.serverside.product.remittance;

import javax.ejb.SessionBean;
import javax.ejb.SessionContext;
import javax.ejb.CreateException;

import java.util.HashMap;

import com.see.truetransact.serverside.TTDAOImpl;
import com.see.truetransact.serverexception.ServiceLocatorException;

/**
 * RemittanceProduct Bean which calls the DAO.
 *
 */
public class RemittanceProductBean implements SessionBean, TTDAOImpl {

    RemittanceProductDAO daoRemittanceProduct = null;

    public void ejbCreate() throws CreateException, ServiceLocatorException {
        daoRemittanceProduct = new RemittanceProductDAO();
    }

    public void ejbRemove() {
        daoRemittanceProduct = null;
    }

    public void setSessionContext(SessionContext sess) {
    }

    public void ejbActivate() {
    }

    public void ejbPassivate() {
    }

    public HashMap execute(HashMap obj) throws Exception {
        return daoRemittanceProduct.execute(obj);
    }

    public HashMap executeQuery(HashMap obj) throws Exception {
        return daoRemittanceProduct.executeQuery(obj);
    }
}
