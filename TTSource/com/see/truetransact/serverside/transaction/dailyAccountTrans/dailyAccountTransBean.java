/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition.
 * 
 *
 * AgentBean.java
 * 
 * Created on Wed Feb 02 13:11:28 IST 2005
 */
package com.see.truetransact.serverside.transaction.dailyAccountTrans;

//import com.see.truetransact.serverside.ActiveMemberList.*;
//import com.see.truetransact.serverside.supporting.balanceupdate.*;
import com.see.truetransact.serverside.payroll.arrear.*;
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
public class dailyAccountTransBean implements SessionBean, TTDAOImpl {

    dailyAccountTransDAO dailyAccountTransDAO = null;

    public void ejbCreate() throws CreateException, ServiceLocatorException {
        dailyAccountTransDAO = new dailyAccountTransDAO();
    }

    public void ejbRemove() {
        dailyAccountTransDAO = null;
    }

    public void setSessionContext(SessionContext sess) {
    }

    public void ejbActivate() {
    }

    public void ejbPassivate() {
    }

    public HashMap execute(HashMap obj) throws Exception {
        return dailyAccountTransDAO.execute(obj);
    }

    public HashMap executeQuery(HashMap obj) throws Exception {
        return dailyAccountTransDAO.executeQuery(obj);
    }
}
