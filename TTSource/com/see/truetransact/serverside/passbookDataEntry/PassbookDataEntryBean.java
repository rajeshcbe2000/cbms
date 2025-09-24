/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition.
 * 
 *
 * PassbookDataEntryBean.java
 * 
 * 
 */
package com.see.truetransact.serverside.passbookDataEntry;

import com.see.truetransact.serverexception.ServiceLocatorException;
import com.see.truetransact.serverside.TTDAOImpl;
import java.util.HashMap;
import javax.ejb.CreateException;
import javax.ejb.SessionBean;
import javax.ejb.SessionContext;

/**
 * PassbookDataEntryBean which calls the DAO.
 *
 */
public class PassbookDataEntryBean implements SessionBean, TTDAOImpl {

    PassbookDataEntryDAO daoPassBookDataEntry = null;

    public void ejbCreate() throws CreateException, ServiceLocatorException {
        daoPassBookDataEntry = new PassbookDataEntryDAO();
    }

    public void ejbRemove() {
        daoPassBookDataEntry = null;
    }

    public void setSessionContext(SessionContext sess) {
    }

    public void ejbActivate() {
    }

    public void ejbPassivate() {
    }

    public HashMap execute(HashMap obj) throws Exception {
        return daoPassBookDataEntry.execute(obj);
    }

    public HashMap executeQuery(HashMap obj) throws Exception {
        return daoPassBookDataEntry.executeQuery(obj);
    }
}
