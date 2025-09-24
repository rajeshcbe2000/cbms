
/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition.  
 * 
 *
 * MDSLetterGenerationDAOBean.java
 *
 * Created on August 20, 2003, 3:20 AM
 */
package com.see.truetransact.serverside.mdsapplication.mdslettergeneration;

import javax.ejb.SessionBean;
import javax.ejb.SessionContext;
import javax.ejb.CreateException;

import java.util.HashMap;

import com.see.truetransact.serverside.TTDAOImpl;
import com.see.truetransact.serverexception.ServiceLocatorException;

/**
 * OperativeAcctProductBean which calls OperativeAcctProduct DAO
 *
 * @author Suresh
 */
public class MDSLetterGenerationBean implements SessionBean, TTDAOImpl {

    MDSLetterGenerationDAO mdsLettorGenerationDAO = null;

    public void ejbCreate() throws CreateException, ServiceLocatorException {
        mdsLettorGenerationDAO = new MDSLetterGenerationDAO();
    }

    public void ejbRemove() {
        mdsLettorGenerationDAO = null;
    }

    public void setSessionContext(SessionContext sess) {
    }

    public void ejbActivate() {
    }

    public void ejbPassivate() {
    }

    public HashMap execute(HashMap obj) throws Exception {
        return mdsLettorGenerationDAO.execute(obj);
    }

    public HashMap executeQuery(HashMap obj) throws Exception {
        return mdsLettorGenerationDAO.executeQuery(obj);
    }
}
