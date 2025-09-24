/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition.
 * 
 *
 * SMSParameterBean.java
 * 
 * Created on Fri May 04 13:31:47 IST 2012
 */
package com.see.truetransact.serverside.sms;

import javax.ejb.SessionBean;
import javax.ejb.SessionContext;
import javax.ejb.CreateException;

import java.util.HashMap;

import com.see.truetransact.serverexception.ServiceLocatorException;

import com.see.truetransact.serverside.TTDAOImpl;

/**
 * SMSParameter Bean which calls the DAO.
 *
 */
public class SMSParameterBean implements SessionBean, TTDAOImpl {

    SMSParameterDAO daoSMSParameter = null;

    public void ejbCreate() throws CreateException, ServiceLocatorException {
        daoSMSParameter = new SMSParameterDAO();
    }

    public void ejbRemove() {
        daoSMSParameter = null;
    }

    public void setSessionContext(SessionContext sess) {
    }

    public void ejbActivate() {
    }

    public void ejbPassivate() {
    }

    public HashMap execute(HashMap obj) throws Exception {
        return daoSMSParameter.execute(obj);
    }

    public HashMap executeQuery(HashMap obj) throws Exception {
        return daoSMSParameter.executeQuery(obj);
    }
}
