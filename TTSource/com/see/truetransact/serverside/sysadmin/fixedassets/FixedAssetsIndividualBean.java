/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition.
 * 
 *
 * EmpTrainingBean.java
 *
 * Created on june, 2010, 1:00 PM
 */
package com.see.truetransact.serverside.sysadmin.fixedassets;

import javax.ejb.SessionBean;
import javax.ejb.SessionContext;
import javax.ejb.CreateException;

import java.util.HashMap;

import com.see.truetransact.transferobject.sysadmin.user.*;
import com.see.truetransact.commonutil.TTException;
import com.see.truetransact.serverside.TTDAOImpl;
import com.see.truetransact.serverexception.ServiceLocatorException;

/**
 *
 *
 * @author swaroop
 */
public class FixedAssetsIndividualBean implements SessionBean, TTDAOImpl {

    private FixedAssetsIndividualDAO fIDDAO;

    public void ejbCreate() throws CreateException, ServiceLocatorException {
        fIDDAO = new FixedAssetsIndividualDAO();
    }

    public void ejbRemove() {
        fIDDAO = null;
    }

    public void setSessionContext(SessionContext sess) {
    }

    public void ejbActivate() {
    }

    public void ejbPassivate() {
    }

    public HashMap execute(HashMap obj) throws Exception {
        return fIDDAO.execute(obj);
    }

    public HashMap executeQuery(HashMap obj) throws Exception {
        return fIDDAO.executeQuery(obj);
    }
}
