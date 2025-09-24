/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition.
 * 
 *
 * FixedAssetsTransBean.java
 * 
 * Created on Tue Jan 18 17:58:40 IST 2011
 */
package com.see.truetransact.serverside.sysadmin.fixedassets;

import javax.ejb.SessionBean;
import javax.ejb.SessionContext;
import javax.ejb.CreateException;

import java.util.HashMap;

import com.see.truetransact.serverexception.ServiceLocatorException;

import com.see.truetransact.serverside.TTDAOImpl;

/**
 * FixedAssetsTrans Bean which calls the DAO.
 *
 */
public class FixedAssetsTransBean implements SessionBean, TTDAOImpl {

    FixedAssetsTransDAO daoFixedAssetsTrans = null;

    public void ejbCreate() throws CreateException, ServiceLocatorException {
        daoFixedAssetsTrans = new FixedAssetsTransDAO();
    }

    public void ejbRemove() {
        daoFixedAssetsTrans = null;
    }

    public void setSessionContext(SessionContext sess) {
    }

    public void ejbActivate() {
    }

    public void ejbPassivate() {
    }

    public HashMap execute(HashMap obj) throws Exception {
        return daoFixedAssetsTrans.execute(obj);
    }

    public HashMap executeQuery(HashMap obj) throws Exception {
        return daoFixedAssetsTrans.executeQuery(obj);
    }
}
