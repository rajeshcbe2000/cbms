/*
 * Copyright 2004 SeE Consulting (P) Ltd. All rights reserved.
 *
 * This software is the proprietary information of SeE Consulting (P) Ltd..
 * Use is subject to license terms.
 *
 * AgentBean.java
 * 
 * Created on Wed Feb 02 13:11:28 IST 2005
 */
package com.see.truetransact.serverside.share.shareconversion;

import com.see.truetransact.serverside.termloan.arbitration.*;
import com.see.truetransact.serverside.termloan.charges.*;
import javax.ejb.SessionBean;
import javax.ejb.SessionContext;
import javax.ejb.CreateException;

import java.util.HashMap;

import com.see.truetransact.serverexception.ServiceLocatorException;

import com.see.truetransact.serverside.TTDAOImpl;

/**
 * TermloanCharges Bean which calls the DAO.
 *
 */
public class ShareConversionBean implements SessionBean, TTDAOImpl {

    ShareConversionDAO objShareConversionDAO = null;

    public void ejbCreate() throws CreateException, ServiceLocatorException {
        objShareConversionDAO = new ShareConversionDAO();
    }

    public void ejbRemove() {
        objShareConversionDAO = null;
    }

    public void setSessionContext(SessionContext sess) {
    }

    public void ejbActivate() {
    }

    public void ejbPassivate() {
    }

    public HashMap execute(HashMap obj) throws Exception {
//            TermLoanChargesDAO daoLoanCharges = new TermLoanChargesDAO();
        return objShareConversionDAO.execute(obj);
    }

    public HashMap executeQuery(HashMap obj) throws Exception {
//            TermLoanChargesDAO daoLoanCharges = new TermLoanChargesDAO();
        return objShareConversionDAO.executeQuery(obj);
    }
}
