/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition.
 * 
 *
 * MDSStandingInstructionBean.java
 * 
 * Created on Tue Oct 11 13:18:08 IST 2011
 */
package com.see.truetransact.serverside.deposit.multiplerenewal;

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
public class DepositMultiRenewalBean implements SessionBean, TTDAOImpl {

    DepositMultiRenewalDAO depositMultiRenewalDAO = null;

    public void ejbCreate() throws CreateException, ServiceLocatorException {
        depositMultiRenewalDAO = new DepositMultiRenewalDAO();
    }

    public void ejbRemove() {
        depositMultiRenewalDAO = null;
    }

    public void setSessionContext(SessionContext sess) {
    }

    public void ejbActivate() {
    }

    public void ejbPassivate() {
    }

    public HashMap execute(HashMap obj) throws Exception {
        return depositMultiRenewalDAO.execute(obj);
    }

    public HashMap executeQuery(HashMap obj) throws Exception {
        return depositMultiRenewalDAO.executeQuery(obj);
    }
}
