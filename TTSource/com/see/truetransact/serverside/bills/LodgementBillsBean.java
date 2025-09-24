/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition.
 * 
 *
 * LodgementBillsTOBean.java
 * 
 * Created on Tue Mar 16 17:29:44 IST 2004
 */
package com.see.truetransact.serverside.bills;

import javax.ejb.SessionBean;
import javax.ejb.SessionContext;
import javax.ejb.CreateException;

import java.util.HashMap;

import com.see.truetransact.serverexception.ServiceLocatorException;
import com.see.truetransact.serverside.TTDAOImpl;

/**
 * LodgementBillsTO Bean which calls the DAO.
 *
 */
public class LodgementBillsBean implements SessionBean, TTDAOImpl {

    LodgementBillsDAO daoLodgementBills = null;

    public void ejbCreate() throws CreateException, ServiceLocatorException {
        daoLodgementBills = new LodgementBillsDAO();
    }

    public void ejbRemove() {
        daoLodgementBills = null;
    }

    public void setSessionContext(SessionContext sess) {
    }

    public void ejbActivate() {
    }

    public void ejbPassivate() {
    }

    public HashMap execute(HashMap obj) throws Exception {
        return daoLodgementBills.execute(obj);
    }

    public HashMap executeQuery(HashMap obj) throws Exception {
        return daoLodgementBills.executeQuery(obj);
    }
}
