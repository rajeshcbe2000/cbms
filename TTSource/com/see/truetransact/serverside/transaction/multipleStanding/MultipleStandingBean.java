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
package com.see.truetransact.serverside.transaction.multipleStanding;

import com.see.truetransact.serverside.payroll.payMaster.*;
import com.see.truetransact.serverside.termloan.groupLoan.*;
import com.see.truetransact.serverside.termloan.SHG.*;
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
public class MultipleStandingBean implements SessionBean, TTDAOImpl {

    MultipleStandingDAO multipleStandingDAO = null;

    public void ejbCreate() throws CreateException, ServiceLocatorException {
        multipleStandingDAO = new MultipleStandingDAO();
    }

    public void ejbRemove() {
        multipleStandingDAO = null;
    }

    public void setSessionContext(SessionContext sess) {
    }

    public void ejbActivate() {
    }

    public void ejbPassivate() {
    }

    public HashMap execute(HashMap obj) throws Exception {
        return multipleStandingDAO.execute(obj);
    }

    public HashMap executeQuery(HashMap obj) throws Exception {
        return multipleStandingDAO.executeQuery(obj);
    }
}
