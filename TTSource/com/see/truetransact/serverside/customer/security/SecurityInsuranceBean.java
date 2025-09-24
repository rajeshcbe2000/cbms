/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition.  
 * 
 * SecurityInsuranceBean.java
 *
 * Created on January 13, 2005, 4:16 PM
 */
package com.see.truetransact.serverside.customer.security;

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
public class SecurityInsuranceBean implements SessionBean, TTDAOImpl {

    public void ejbCreate() throws CreateException, ServiceLocatorException {
    }

    public void ejbActivate() {
    }

    public void ejbPassivate() {
    }

    public void ejbRemove() {
    }

    public HashMap execute(HashMap obj) throws Exception {
        SecurityInsuranceDAO daoSecurityInsurance = new SecurityInsuranceDAO();
        return daoSecurityInsurance.execute(obj);
//        daoSecurityInsurance = null;
    }

    public HashMap executeQuery(HashMap obj) throws Exception {
        SecurityInsuranceDAO daoSecurityInsurance = new SecurityInsuranceDAO();
        return daoSecurityInsurance.executeQuery(obj);
    }

    public void setSessionContext(javax.ejb.SessionContext sessionContext) {
    }
}
