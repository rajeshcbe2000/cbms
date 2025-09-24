/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition.
 **
 *
 * TermLoanBean.java
 *
 * Created on Fri Jan 09 18:03:55 CST 2004
 */
package com.see.truetransact.serverside.kolefieldsoperations.chargeposting;

import com.see.truetransact.serverside.termloan.riskfund.*;
import com.see.truetransact.serverside.termloan.*;
import javax.ejb.SessionBean;
import javax.ejb.SessionContext;
import javax.ejb.CreateException;

import java.util.HashMap;

import com.see.truetransact.serverside.TTDAOImpl;
import com.see.truetransact.serverexception.ServiceLocatorException;

/**
 * TermLoan Bean which calls the DAO.
 *
 * @author shanmugavel
 */
public class KoleFieldsKCCChargePostingBean implements SessionBean, TTDAOImpl {

    KoleFieldsKCCChargePostingDAO dao = null;

    public void ejbCreate() throws CreateException, ServiceLocatorException {
        dao = new KoleFieldsKCCChargePostingDAO();
        //                System.out.println("##########TermLoan Created############");
    }

    public void ejbRemove() {
        dao = null;
        //                System.out.println("&&&&&&&&&&&TermLoan Removed&&&&&&&&&&&&");
    }

    public void setSessionContext(SessionContext sess) {
    }

    public void ejbActivate() {
    }

    public void ejbPassivate() {
    }

    public HashMap execute(HashMap obj) throws Exception {
//        RiskFundDAO daoRisKFund = new RiskFundDAO();
        return dao.execute(obj);
        //daoTermLoan = null;
    }

    public HashMap executeQuery(HashMap obj) throws Exception {
//        RiskFundDAO daoRisKFund = new RiskFundDAO();
        return dao.executeQuery(obj);
    }
}
