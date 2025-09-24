/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition.
 * 
 *
 * BatchProcessBean.java
 *
 * Created on Mon May 17 11:06:22 PST 2004
 */
package com.see.truetransact.serverside.batchprocess.ejb;

import javax.ejb.SessionBean;
import javax.ejb.SessionContext;
import javax.ejb.CreateException;

import java.util.HashMap;

import com.see.truetransact.serverexception.ServiceLocatorException;
import com.see.truetransact.serverside.TTDAOImpl;

/**
 * BatchProcessBean which calls the DAO.
 *
 */
public class BatchProcessBean implements SessionBean, TTDAOImpl {

    BatchProcessDAO batchProcessDAO;

    public void ejbCreate() throws CreateException, ServiceLocatorException {
        batchProcessDAO = new BatchProcessDAO();
    }

    public void ejbRemove() {
        batchProcessDAO = null;
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
        System.out.println("Execute Query of Batch Process Bean");
        return batchProcessDAO.executeQuery(obj);
    }
}
