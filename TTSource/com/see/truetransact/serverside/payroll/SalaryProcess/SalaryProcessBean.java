/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition.  
 * 
 *
 * SalaryProcessBean.java
 *
 * Created on Novmber 20, 2014, 3:20 PM
 */
package com.see.truetransact.serverside.payroll.SalaryProcess;


import javax.ejb.SessionBean;
import javax.ejb.SessionContext;
import javax.ejb.CreateException;

import java.util.HashMap;

import com.see.truetransact.serverside.TTDAOImpl;
import com.see.truetransact.serverexception.ServiceLocatorException;

/**
 * SalaryProcessBean which calls SalaryProcess DAO
 *
 * @author Rishad
 */
public class SalaryProcessBean implements SessionBean, TTDAOImpl {

    SalaryProcessDAO  SalaryProcessDAO = null;

    public void ejbCreate() throws CreateException, ServiceLocatorException {
       SalaryProcessDAO = new SalaryProcessDAO();
    }

    public void ejbRemove() {
        SalaryProcessDAO = null;
    }

    public void setSessionContext(SessionContext sess) {
    }

    public void ejbActivate() {
    }

    public void ejbPassivate() {
    }

    public HashMap execute(HashMap obj) throws Exception {
        return SalaryProcessDAO.execute(obj);
    }

    public HashMap executeQuery(HashMap obj) throws Exception {
        return SalaryProcessDAO.executeQuery(obj);
    }
}
