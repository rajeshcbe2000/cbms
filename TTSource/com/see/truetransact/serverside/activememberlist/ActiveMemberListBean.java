/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition.
 **
 *
 * AgentBean.java
 * 
 * Created on Wed Feb 02 13:11:28 IST 2005
 */
package com.see.truetransact.serverside.activememberlist;

//import com.see.truetransact.serverside.ActiveMemberList.*;
import javax.ejb.SessionBean;
import javax.ejb.SessionContext;
import javax.ejb.CreateException;

import java.util.HashMap;

import com.see.truetransact.serverexception.ServiceLocatorException;

import com.see.truetransact.serverside.TTDAOImpl;

/**
 * Agent Bean which calls the DAO.
 *
 */
public class ActiveMemberListBean implements SessionBean, TTDAOImpl {

    ActiveMemberListDAO daoActive_MemberList = null;

    public void ejbCreate() throws CreateException, ServiceLocatorException {
        daoActive_MemberList = new ActiveMemberListDAO();
    }

    public void ejbRemove() {
        daoActive_MemberList = null;
    }

    public void setSessionContext(SessionContext sess) {
    }

    public void ejbActivate() {
    }

    public void ejbPassivate() {
    }

    public HashMap execute(HashMap obj) throws Exception {
        return daoActive_MemberList.execute(obj);
    }

    public HashMap executeQuery(HashMap obj) throws Exception {
        return daoActive_MemberList.executeQuery(obj);
    }
}
