/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition.
 **
 *
 * SanctionMasterBean.java
 * 
 * Created on Thu Mar 07 18:10:16 IST 2013
 */
package com.see.truetransact.serverside.termloan.kcctopacs;

import com.see.truetransact.serverexception.ServiceLocatorException;
import com.see.truetransact.serverside.TTDAOImpl;
import java.util.HashMap;
import javax.ejb.CreateException;
import javax.ejb.SessionBean;
import javax.ejb.SessionContext;

/**
 * SanctionMaster Bean which calls the DAO.
 *
 */
public class SanctionMasterBean implements SessionBean, TTDAOImpl {

    SanctionMasterDAO daoSanctionMaster = null;

    public void ejbCreate() throws CreateException, ServiceLocatorException {
        daoSanctionMaster = new SanctionMasterDAO();
    }

    public void ejbRemove() {
        daoSanctionMaster = null;
    }

    public void setSessionContext(SessionContext sess) {
    }

    public void ejbActivate() {
    }

    public void ejbPassivate() {
    }

    public HashMap execute(HashMap obj) throws Exception {
        return daoSanctionMaster.execute(obj);
    }

    public HashMap executeQuery(HashMap obj) throws Exception {
        return daoSanctionMaster.executeQuery(obj);
    }
}
