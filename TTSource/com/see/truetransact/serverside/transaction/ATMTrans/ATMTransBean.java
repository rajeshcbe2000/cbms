/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition.  
 * 
 * ATMTransBean.java
 */

package com.see.truetransact.serverside.transaction.ATMTrans;

import com.see.truetransact.serverexception.ServiceLocatorException;
import com.see.truetransact.serverside.TTDAOImpl;
import com.see.truetransact.serverside.transaction.cash.CashTransactionDAO;
import java.util.HashMap;
import javax.ejb.CreateException;
import javax.ejb.SessionBean;
import javax.ejb.SessionContext;

/**
 *
 * @author noora
 */
public class ATMTransBean implements SessionBean, TTDAOImpl {

    ATMTransDAO daoATMTransaction = null;

    public void ejbCreate() throws CreateException, ServiceLocatorException {
        daoATMTransaction = new ATMTransDAO();
    }

    public void ejbRemove() {
        daoATMTransaction = null;
    }

    public void setSessionContext(SessionContext sess) {
    }

    public void ejbActivate() {
    }

    public void ejbPassivate() {
    }

    public HashMap execute(HashMap obj) throws Exception {
        return daoATMTransaction.execute(obj);
    }

    public HashMap executeQuery(HashMap obj) throws Exception {
        return daoATMTransaction.executeQuery(obj);
    }
}
