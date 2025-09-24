/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition.
 * 
 *
 * HeadConsolidationBean.java
 * 
 * Created on Wed Feb 02 13:11:28 IST 2015
 */
package com.see.truetransact.serverside.payroll.voucherprocessing;

import com.see.truetransact.serverexception.ServiceLocatorException;
import com.see.truetransact.serverside.TTDAOImpl;
import java.util.HashMap;
import javax.ejb.CreateException;
import javax.ejb.SessionBean;
import javax.ejb.SessionContext;

/**
 * HeadConsolidation Bean which calls the DAO.
 *
 */
public class HeadConsolidationBean implements SessionBean, TTDAOImpl {

    HeadConsolidationDAO daoHeadConsolidation = null;

    public void ejbCreate() throws CreateException, ServiceLocatorException {
        daoHeadConsolidation = new HeadConsolidationDAO();
    }

    public void ejbRemove() {
        daoHeadConsolidation = null;
    }

    public void setSessionContext(SessionContext sess) {
    }

    public void ejbActivate() {
    }

    public void ejbPassivate() {
    }

    public HashMap execute(HashMap obj) throws Exception {
        return daoHeadConsolidation.execute(obj);
    }

    public HashMap executeQuery(HashMap obj) throws Exception {
        return daoHeadConsolidation.executeQuery(obj);
    }
}
