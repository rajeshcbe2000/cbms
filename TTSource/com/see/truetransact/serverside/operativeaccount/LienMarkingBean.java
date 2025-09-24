/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition.
 * 
 *
 * LienMarkingBean.java
 *
 * Created on September 08, 2003, 1:00 PM
 */
package com.see.truetransact.serverside.operativeaccount;

import javax.ejb.SessionBean;
import javax.ejb.SessionContext;
import javax.ejb.CreateException;

import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.HashMap;

import com.see.truetransact.transferobject.operativeaccount.*;
import com.see.truetransact.commonutil.TTException;
import com.see.truetransact.serverside.TTDAOImpl;
import com.see.truetransact.serverexception.ServiceLocatorException;

/**
 * AccountsBean which calls AccountsBeanDAO
 *
 * @author Karthik
 */
public class LienMarkingBean implements SessionBean, TTDAOImpl {

    private LienMarkingDAO lienMarkingDAO;

    public void ejbCreate() throws CreateException, ServiceLocatorException {
        lienMarkingDAO = new LienMarkingDAO();
    }

    public void ejbRemove() {
        lienMarkingDAO = null;
    }

    public void setSessionContext(SessionContext sess) {
    }

    public void ejbActivate() {
    }

    public void ejbPassivate() {
    }

    public HashMap execute(HashMap obj) throws Exception {
        return lienMarkingDAO.execute(obj);
    }

    public HashMap executeQuery(HashMap obj) throws Exception {
        return lienMarkingDAO.executeQuery(obj);
    }
}
