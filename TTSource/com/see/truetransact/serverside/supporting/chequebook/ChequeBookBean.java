/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition.
 * 
 *
 * ChequeBookTOBean.java
 * 
 * Created on Fri Jan 23 12:22:26 IST 2004
 */
package com.see.truetransact.serverside.supporting.chequebook;

import javax.ejb.SessionBean;
import javax.ejb.SessionContext;
import javax.ejb.CreateException;

import java.util.HashMap;

import com.see.truetransact.serverside.TTDAOImpl;
import com.see.truetransact.serverexception.ServiceLocatorException;

/**
 * ChequeBookTO Bean which calls the DAO.
 *
 */
public class ChequeBookBean implements SessionBean, TTDAOImpl {

    ChequeBookDAO daoChequeBook = null;

    public void ejbCreate() throws CreateException, ServiceLocatorException {
        daoChequeBook = new ChequeBookDAO();
    }

    public void ejbRemove() {
        daoChequeBook = null;
    }

    public void setSessionContext(SessionContext sess) {
    }

    public void ejbActivate() {
    }

    public void ejbPassivate() {
    }

    public HashMap execute(HashMap obj) throws Exception {
        return daoChequeBook.execute(obj);
    }

    public HashMap executeQuery(HashMap obj) throws Exception {
        return daoChequeBook.executeQuery(obj);
    }
}
