/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition.
 * 
 *
 * OtherBankBean.java
 * 
 * Created on Thu Dec 30 17:43:45 IST 2004
 */
package com.see.truetransact.serverside.sysadmin.stateTalukMaster;

import javax.ejb.SessionBean;
import javax.ejb.SessionContext;
import javax.ejb.CreateException;

import java.util.HashMap;

import com.see.truetransact.serverexception.ServiceLocatorException;

import com.see.truetransact.serverside.TTDAOImpl;

/**
 * OtherBank Bean which calls the DAO.
 *
 */
public class StateTalukBean implements SessionBean, TTDAOImpl {

    StateTalukDAO daoStateTaluk = null;

    public void ejbCreate() throws CreateException, ServiceLocatorException {
        daoStateTaluk = new StateTalukDAO();
    }

    public void ejbRemove() {
        daoStateTaluk = null;
    }

    public void setSessionContext(SessionContext sess) {
    }

    public void ejbActivate() {
    }

    public void ejbPassivate() {
    }

    public HashMap execute(HashMap obj) throws Exception {
        return daoStateTaluk.execute(obj);
    }

    public HashMap executeQuery(HashMap obj) throws Exception {
        return daoStateTaluk.executeQuery(obj);
    }
}
