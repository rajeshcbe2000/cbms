/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition.  
 * 
 *
 * ViewAllBean.java
 *
 * Created on June 23, 2003, 4:20 PM
 */
package com.see.truetransact.serverside.transaction.rollback;

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
public class RollBackBean implements SessionBean, TTDAOImpl {

    RollBackDAO objRollBackDAO = null;
    private final static Logger log = Logger.getLogger(RollBackBean.class);

    public void ejbCreate() throws CreateException, ServiceLocatorException {
        objRollBackDAO = new RollBackDAO();
    }

    public void ejbRemove() {
        objRollBackDAO = null;
    }

    public void setSessionContext(SessionContext sess) {
    }

    public void ejbActivate() {
    }

    public void ejbPassivate() {
    }

    public HashMap execute(HashMap obj) throws Exception {
        return objRollBackDAO.execute(obj);
    }

    public HashMap executeQuery(HashMap obj) throws Exception {
        return objRollBackDAO.executeQuery(obj);
    }
}
