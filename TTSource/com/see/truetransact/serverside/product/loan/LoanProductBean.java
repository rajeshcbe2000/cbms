/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition. 
 * 
 * LoanProductBean.java
 *
 * Created on December 8, 2003, 12:32 PM
 */
package com.see.truetransact.serverside.product.loan;

import java.beans.*;
import javax.ejb.SessionBean;
import javax.ejb.SessionContext;
import javax.ejb.CreateException;

import java.rmi.RemoteException;
import java.util.HashMap;

import com.see.truetransact.transferobject.product.operativeacct.*;
import com.see.truetransact.commonutil.TTException;
import com.see.truetransact.serverside.TTDAOImpl;
import com.see.truetransact.serverexception.ServiceLocatorException;

/**
 *
 * @author rahul
 */
public class LoanProductBean implements SessionBean, TTDAOImpl {

    LoanProductDAO loanProductDAO = null;

    public void ejbCreate() throws CreateException, ServiceLocatorException {
        loanProductDAO = new LoanProductDAO();
    }

    public void ejbRemove() {
        loanProductDAO = null;
    }

    public void setSessionContext(SessionContext sess) {
    }

    public void ejbActivate() {
    }

    public void ejbPassivate() {
    }

    public HashMap execute(HashMap obj) throws Exception {
        return loanProductDAO.execute(obj);
    }

    public HashMap executeQuery(HashMap obj) throws Exception {
        return loanProductDAO.executeQuery(obj);
    }
}
