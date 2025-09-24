/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition.
 * 
 *
 * MDSStandingInstructionBean.java
 * 
 * Created on Tue Jan 11 13:18:08 IST 2015
 */
package com.see.truetransact.serverside.deposit.multipleclosing;

import javax.ejb.SessionBean;
import javax.ejb.SessionContext;
import javax.ejb.CreateException;

import java.util.HashMap;

import com.see.truetransact.serverside.TTDAOImpl;
import com.see.truetransact.serverexception.ServiceLocatorException;

/**
 * DepositMultiClosingBean which calls DepositMultiClosing DAO
 *
 * @author Shihad
 */
public class DepositMultiClosingBean implements SessionBean, TTDAOImpl {

    DepositMultiClosingDAO depositMultiClosingDAO = null;

    public void ejbCreate() throws CreateException, ServiceLocatorException {
        depositMultiClosingDAO = new DepositMultiClosingDAO();
    }

    public void ejbRemove() {
        depositMultiClosingDAO = null;
    }

    public void setSessionContext(SessionContext sess) {
    }

    public void ejbActivate() {
    }

    public void ejbPassivate() {
    }

    public HashMap execute(HashMap obj) throws Exception {
        return depositMultiClosingDAO.execute(obj);
    }

    public HashMap executeQuery(HashMap obj) throws Exception {
        return depositMultiClosingDAO.executeQuery(obj);
    }
}
