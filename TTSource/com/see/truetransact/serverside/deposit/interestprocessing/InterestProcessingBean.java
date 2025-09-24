/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition.
 * 
 *
 * ShareDividendCalculationBean.java
 * 
 * Created on Wed May 26 10:59:57 GMT+05:30 2004
 */
package com.see.truetransact.serverside.deposit.interestprocessing;

import javax.ejb.SessionBean;
import javax.ejb.SessionContext;
import javax.ejb.CreateException;

import java.util.HashMap;

import com.see.truetransact.serverside.TTDAOImpl;
import com.see.truetransact.serverexception.ServiceLocatorException;

/**
 * Deduction Bean which calls the DAO.
 *
 */
public class InterestProcessingBean implements SessionBean, TTDAOImpl {

    InterestProcessingDAO daoInterestProcessing = null;

    public void ejbCreate() throws CreateException, ServiceLocatorException {
        daoInterestProcessing = new InterestProcessingDAO();
    }

    public void ejbRemove() {
        daoInterestProcessing = null;
    }

    public void setSessionContext(SessionContext sess) {
    }

    public void ejbActivate() {
    }

    public void ejbPassivate() {
    }

    public HashMap execute(HashMap obj) throws Exception {
        return daoInterestProcessing.execute(obj);
    }

    public HashMap executeQuery(HashMap obj) throws Exception {
        return daoInterestProcessing.executeQuery(obj);
    }
}
