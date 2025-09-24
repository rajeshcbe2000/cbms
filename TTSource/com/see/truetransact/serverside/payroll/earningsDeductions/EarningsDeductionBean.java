/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition.
 * 
 *
 * EarningsDeductionBean.java
 * 
 * Created on Wed May 26 10:59:57 GMT+05:30 2004
 */
package com.see.truetransact.serverside.payroll.earningsDeductions;

import com.see.truetransact.serverexception.ServiceLocatorException;
import com.see.truetransact.serverside.TTDAOImpl;
import java.util.HashMap;
import javax.ejb.CreateException;
import javax.ejb.SessionBean;
import javax.ejb.SessionContext;

/**
 * EarningsDeduction Bean which calls the DAO.
 *
 */
public class EarningsDeductionBean implements SessionBean, TTDAOImpl {

    EarningsDeductionDAO daoEarningsDeduction = null;

    public void ejbCreate() throws CreateException, ServiceLocatorException {
        daoEarningsDeduction = new EarningsDeductionDAO();
    }

    public void ejbRemove() {
        daoEarningsDeduction = null;
    }

    public void setSessionContext(SessionContext sess) {
    }

    public void ejbActivate() {
    }

    public void ejbPassivate() {
    }

    public HashMap execute(HashMap obj) throws Exception {
        return daoEarningsDeduction.execute(obj);
    }

    public HashMap executeQuery(HashMap obj) throws Exception {
        return daoEarningsDeduction.executeQuery(obj);
    }
}
