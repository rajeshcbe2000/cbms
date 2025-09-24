/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition.
 * 
 *
 * BlockedListBean.java
 * 
 * Created on Wed Feb 09 16:06:44 IST 2005
 */
package com.see.truetransact.serverside.sysadmin.blockedlist;

import javax.ejb.SessionBean;
import javax.ejb.SessionContext;
import javax.ejb.CreateException;

import java.util.HashMap;

import com.see.truetransact.serverexception.ServiceLocatorException;

import com.see.truetransact.serverside.TTDAOImpl;

/**
 * BlockedList Bean which calls the DAO.
 *
 */
public class BlockedListBean implements SessionBean, TTDAOImpl {

    BlockedListDAO daoBlockedList = null;

    public void ejbCreate() throws CreateException, ServiceLocatorException {
        daoBlockedList = new BlockedListDAO();
    }

    public void ejbRemove() {
        daoBlockedList = null;
    }

    public void setSessionContext(SessionContext sess) {
    }

    public void ejbActivate() {
    }

    public void ejbPassivate() {
    }

    public HashMap execute(HashMap obj) throws Exception {
        return daoBlockedList.execute(obj);
    }

    public HashMap executeQuery(HashMap obj) throws Exception {
        return daoBlockedList.executeQuery(obj);
    }
}
