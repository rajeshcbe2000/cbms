/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition.  
 * 
 *
 * HeadBean.java
 *
 * Created on August 25, 2003, 12:30 PM
 */
package com.see.truetransact.serverside.generalledger;

import javax.ejb.SessionBean;
import javax.ejb.SessionContext;
import javax.ejb.CreateException;

import com.see.truetransact.commonutil.TTException;

import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.HashMap;

import com.see.truetransact.serverside.TTDAOImpl;
import com.see.truetransact.serverexception.ServiceLocatorException;

import com.see.truetransact.transferobject.generalledger.MajorHeadTO;

/**
 *
 * @author Annamalai
 */
public class HeadBean implements SessionBean, TTDAOImpl {

    HeadDAO _objHeadDAO;

    public void ejbCreate() throws CreateException, ServiceLocatorException {
        _objHeadDAO = new HeadDAO();
    }

    public void ejbRemove() {
        _objHeadDAO = null;
    }

    public void setSessionContext(SessionContext sess) {
    }

    public void ejbActivate() {
    }

    public void ejbPassivate() {
    }

    public HashMap execute(HashMap param) throws Exception {
        return _objHeadDAO.execute(param);
    }

    public HashMap executeQuery(HashMap param) throws Exception {
        return _objHeadDAO.executeQuery(param);
    }
}
