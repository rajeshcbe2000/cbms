/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition.  
 * 
 * LoginBean.java
 *
 * Created on March 2, 2005, 10:08 AM
 */
package com.see.truetransact.serverside.login;

/**
 *
 * @author JK
 */
import javax.ejb.SessionBean;
import javax.ejb.SessionContext;
import javax.ejb.CreateException;

import java.util.HashMap;

import com.see.truetransact.serverside.TTDAOImpl;
import com.see.truetransact.serverexception.ServiceLocatorException;

public class LoginBean implements SessionBean, TTDAOImpl {

    LoginDAO daoLogin = null;

    public void ejbCreate() throws CreateException, ServiceLocatorException {
        daoLogin = new LoginDAO();
    }

    public void ejbRemove() {
        daoLogin = null;
    }

    public void setSessionContext(SessionContext sess) {
    }

    public void ejbActivate() {
    }

    public void ejbPassivate() {
    }

    public HashMap execute(HashMap obj) throws Exception {
        return daoLogin.execute(obj);
    }

    public HashMap executeQuery(HashMap obj) throws Exception {
        return daoLogin.executeQuery(obj);
    }
}
