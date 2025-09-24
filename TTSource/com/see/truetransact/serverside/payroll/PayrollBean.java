/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition.
 * 
 *
 * PayrollBean.java
 * 
 * 
 */
package com.see.truetransact.serverside.payroll;

import com.see.truetransact.serverexception.ServiceLocatorException;
import com.see.truetransact.serverside.TTDAOImpl;
import java.util.HashMap;
import javax.ejb.CreateException;
import javax.ejb.SessionBean;
import javax.ejb.SessionContext;

/**
 * PayrollBean which calls the DAO.
 *
 */
public class PayrollBean implements SessionBean, TTDAOImpl {

    PayrollDAO dao = null;

    public void ejbCreate() throws CreateException, ServiceLocatorException {
        dao = new PayrollDAO();
    }

    public void ejbRemove() {
        dao = null;
    }

    public void setSessionContext(SessionContext sess) {
    }

    public void ejbActivate() {
    }

    public void ejbPassivate() {
    }

    public HashMap execute(HashMap obj) throws Exception {
        return dao.execute(obj);
    }

    public HashMap executeQuery(HashMap obj) throws Exception {
        return dao.executeQuery(obj);
    }
}
