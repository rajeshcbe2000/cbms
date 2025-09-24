/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition.
 * 
 *
 * AcctCreationBean.java
 *
 * Created on June 23, 2003, 4:20 PM
 */
package com.see.truetransact.serverside.generalledger;

import javax.ejb.SessionBean;
import javax.ejb.SessionContext;
import javax.ejb.CreateException;
import java.util.ArrayList;
import java.util.HashMap;
import com.see.truetransact.serverside.generalledger.AcctCreationDAO;
import com.see.truetransact.transferobject.generalledger.AccountCreationTO;

import com.see.truetransact.serverside.TTDAOImpl;
import com.see.truetransact.serverexception.ServiceLocatorException;

/**
 * AcctBean which calls AccountHeadDAO
 *
 * @author annamalai
 */
public class AcctCreationBean implements SessionBean, TTDAOImpl {

    AcctCreationDAO _objAcctCreationDAO;

    public void ejbCreate() throws CreateException, ServiceLocatorException {
        _objAcctCreationDAO = new AcctCreationDAO();
    }

    public void ejbRemove() {
        _objAcctCreationDAO = null;
    }

    public void setSessionContext(SessionContext sess) {
    }

    public void ejbActivate() {
    }

    public void ejbPassivate() {
    }

    public HashMap execute(HashMap obj) throws Exception {
        return _objAcctCreationDAO.execute(obj);
    }

    public HashMap executeQuery(HashMap param) throws Exception {
        return _objAcctCreationDAO.executeQuery(param);
    }
}
