/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition.
 * 
 *
 * AccountsBean.java
 *
 * Created on September 08, 2003, 1:00 PM
 */
package com.see.truetransact.serverside.operativeaccount;

import javax.ejb.SessionBean;
import javax.ejb.SessionContext;
import javax.ejb.CreateException;

import java.util.HashMap;

import com.see.truetransact.transferobject.operativeaccount.*;
import com.see.truetransact.serverside.TTDAOImpl;
import com.see.truetransact.serverexception.ServiceLocatorException;

/**
 * AccountsBean which calls AccountsBeanDAO
 *
 * @author Karthik
 */
public class TodAllowedBean implements SessionBean, TTDAOImpl {

    private TodAllowedDAO todAllowedDAO;

    public void ejbCreate() throws CreateException, ServiceLocatorException {
        todAllowedDAO = new TodAllowedDAO();
    }

    public void ejbRemove() {
        todAllowedDAO = null;
    }

    public void setSessionContext(SessionContext sess) {
    }

    public void ejbActivate() {
    }

    public void ejbPassivate() {
    }

    public HashMap execute(HashMap obj) throws Exception {
        return todAllowedDAO.execute(obj);
    }

    public HashMap executeQuery(HashMap obj) throws Exception {
        return todAllowedDAO.executeQuery(obj);
    }
}
