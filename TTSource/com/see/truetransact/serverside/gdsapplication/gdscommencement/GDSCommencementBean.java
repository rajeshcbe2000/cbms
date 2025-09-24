/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition.  
 * 
 *
 * OperativeAcctProductBean.java
 *
 * Created on August 20, 2003, 3:20 AM
 */
package com.see.truetransact.serverside.gdsapplication.gdscommencement;

import com.see.truetransact.serverside.mdsapplication.mdsconmmencement.*;
import javax.ejb.SessionBean;
import javax.ejb.SessionContext;
import javax.ejb.CreateException;

import java.util.HashMap;

import com.see.truetransact.serverside.TTDAOImpl;
import com.see.truetransact.serverexception.ServiceLocatorException;

/**
 * OperativeAcctProductBean which calls OperativeAcctProduct DAO
 *
 * @author Balachandar
 */
public class GDSCommencementBean implements SessionBean, TTDAOImpl {

    GDSCommencementDAO gdsCommencementDAO = null;

    public void ejbCreate() throws CreateException, ServiceLocatorException {
        gdsCommencementDAO = new GDSCommencementDAO();
    }

    public void ejbRemove() {
        gdsCommencementDAO = null;
    }

    public void setSessionContext(SessionContext sess) {
    }

    public void ejbActivate() {
    }

    public void ejbPassivate() {
    }

    public HashMap execute(HashMap obj) throws Exception {
        return gdsCommencementDAO.execute(obj);
    }

    public HashMap executeQuery(HashMap obj) throws Exception {
        return gdsCommencementDAO.executeQuery(obj);
    }
}
