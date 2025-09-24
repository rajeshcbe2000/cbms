/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition.
 * 
 *
 * ShareProductBean.java
 * 
 * Created on Wed Nov 24 16:51:38 GMT+05:30 2004
 */
package com.see.truetransact.serverside.product.accountswithotherbank;

import javax.ejb.SessionBean;
import javax.ejb.SessionContext;
import javax.ejb.CreateException;

import java.util.HashMap;

import com.see.truetransact.serverexception.ServiceLocatorException;

import com.see.truetransact.serverside.TTDAOImpl;

/**
 * ShareProduct Bean which calls the DAO.
 *
 */
public class AccountswithOtherBankProductBean implements SessionBean, TTDAOImpl {

    AccountswithOtherBankProductDAO daoOtherbankActsProduct = null;

    public void ejbCreate() throws CreateException, ServiceLocatorException {
        daoOtherbankActsProduct = new AccountswithOtherBankProductDAO();
    }

    public void ejbRemove() {
        daoOtherbankActsProduct = null;
    }

    public void setSessionContext(SessionContext sess) {
    }

    public void ejbActivate() {
    }

    public void ejbPassivate() {
    }

    public HashMap execute(HashMap obj) throws Exception {
        return daoOtherbankActsProduct.execute(obj);
    }

    public HashMap executeQuery(HashMap obj) throws Exception {
        return daoOtherbankActsProduct.executeQuery(obj);
    }
}
