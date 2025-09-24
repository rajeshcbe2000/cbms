/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition. 
 * 
 * ViewLogImportanceBean.java
 *
 * Created on January 7, 2005, 6:42 PM
 */
package com.see.truetransact.serverside.sysadmin.viewlogimportance;

import javax.ejb.SessionBean;
import javax.ejb.SessionContext;
import javax.ejb.CreateException;

import java.util.HashMap;

import com.see.truetransact.serverside.TTDAOImpl;
import com.see.truetransact.serverexception.ServiceLocatorException;

/**
 *
 * @author 152713
 */
public class ViewLogImportanceBean implements SessionBean, TTDAOImpl {

    ViewLogImportanceDAO viewLogImportanceDAO = null;

    public void ejbCreate() throws CreateException, ServiceLocatorException {
        viewLogImportanceDAO = new ViewLogImportanceDAO();
    }

    public HashMap execute(HashMap obj) throws Exception {
        return viewLogImportanceDAO.execute(obj);
    }

    public HashMap executeQuery(HashMap obj) throws Exception {
        return viewLogImportanceDAO.executeQuery(obj);
    }

    public void ejbActivate() {
    }

    public void ejbPassivate() {
    }

    public void ejbRemove() {
    }

    public void setSessionContext(SessionContext sessionContext) {
    }
}
