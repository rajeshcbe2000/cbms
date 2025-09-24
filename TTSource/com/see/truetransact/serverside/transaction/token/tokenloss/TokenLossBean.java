/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition.
 * 
 *
 * TokenLossBean.java
 * 
 * Created on Tue Jan 25 17:39:28 IST 2005
 */
package com.see.truetransact.serverside.transaction.token.tokenloss;

import javax.ejb.SessionBean;
import javax.ejb.SessionContext;
import javax.ejb.CreateException;

import java.util.HashMap;

import com.see.truetransact.serverexception.ServiceLocatorException;

import com.see.truetransact.serverside.TTDAOImpl;

/**
 * TokenLoss Bean which calls the DAO.
 *
 */
public class TokenLossBean implements SessionBean, TTDAOImpl {

    TokenLossDAO daoTokenLoss = null;

    public void ejbCreate() throws CreateException, ServiceLocatorException {
        daoTokenLoss = new TokenLossDAO();
    }

    public void ejbRemove() {
        daoTokenLoss = null;
    }

    public void setSessionContext(SessionContext sess) {
    }

    public void ejbActivate() {
    }

    public void ejbPassivate() {
    }

    public HashMap execute(HashMap obj) throws Exception {
        return daoTokenLoss.execute(obj);
    }

    public HashMap executeQuery(HashMap obj) throws Exception {
        return daoTokenLoss.executeQuery(obj);
    }
}
