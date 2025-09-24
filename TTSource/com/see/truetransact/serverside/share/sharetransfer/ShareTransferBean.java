/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition.
 * 
 *
 * ShareTransferBean.java
 * 
 * Created on Thu Feb 03 16:24:31 IST 2005
 */
package com.see.truetransact.serverside.share.sharetransfer;

import javax.ejb.SessionBean;
import javax.ejb.SessionContext;
import javax.ejb.CreateException;

import java.util.HashMap;

import com.see.truetransact.serverexception.ServiceLocatorException;

import com.see.truetransact.serverside.TTDAOImpl;

/**
 * ShareTransfer Bean which calls the DAO.
 *
 */
public class ShareTransferBean implements SessionBean, TTDAOImpl {

    ShareTransferDAO daoShareTransfer = null;

    public void ejbCreate() throws CreateException, ServiceLocatorException {
        daoShareTransfer = new ShareTransferDAO();
    }

    public void ejbRemove() {
        daoShareTransfer = null;
    }

    public void setSessionContext(SessionContext sess) {
    }

    public void ejbActivate() {
    }

    public void ejbPassivate() {
    }

    public HashMap execute(HashMap obj) throws Exception {
        return daoShareTransfer.execute(obj);
    }

    public HashMap executeQuery(HashMap obj) throws Exception {
        return daoShareTransfer.executeQuery(obj);
    }
}
