/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition.  
 **
 *
 * ViewAllBean.java
 *
 * Created on June 23, 2003, 4:20 PM
 */
package com.see.truetransact.serverside.common.report;

import java.rmi.RemoteException;
import java.util.HashMap;

import javax.ejb.SessionBean;
import javax.ejb.SessionContext;
import javax.ejb.CreateException;

import org.apache.log4j.Logger;

import com.see.truetransact.serverexception.ServiceLocatorException;
import com.see.truetransact.serverside.TTDAOImpl;

/**
 * Acct Bean which calls AccountHead DAO
 *
 * @author Balachandar
 */
public class GenerateReportBean implements SessionBean, TTDAOImpl {

    GenerateReportDAO objGenerateReportDAO = null;
    private final static Logger log = Logger.getLogger(GenerateReportBean.class);

    public void ejbCreate() throws CreateException, ServiceLocatorException {
        objGenerateReportDAO = new GenerateReportDAO();
    }

    public void ejbRemove() {
        objGenerateReportDAO = null;
    }

    public void setSessionContext(SessionContext sess) {
    }

    public void ejbActivate() {
    }

    public void ejbPassivate() {
    }

    public HashMap execute(HashMap obj) throws Exception {
        return null;
    }

    public HashMap executeQuery(HashMap obj) throws Exception {
        return objGenerateReportDAO.executeQuery(obj);
    }
}
