/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition.
 * 
 *
 * PFMasterBean.java
 * 
 * 
 */
package com.see.truetransact.serverside.payroll.pfMaster;

import com.see.truetransact.serverexception.ServiceLocatorException;
import com.see.truetransact.serverside.TTDAOImpl;
import java.util.HashMap;
import javax.ejb.CreateException;
import javax.ejb.SessionBean;
import javax.ejb.SessionContext;

/**
 * PFMasterBean which calls the DAO.
 *
 */
public class PFMasterBean implements SessionBean, TTDAOImpl {

    PFMasterDAO daoPFMaster = null;

    public void ejbCreate() throws CreateException, ServiceLocatorException {
        daoPFMaster = new PFMasterDAO();
    }

    public void ejbRemove() {
        daoPFMaster = null;
    }

    public void setSessionContext(SessionContext sess) {
    }

    public void ejbActivate() {
    }

    public void ejbPassivate() {
    }

    public HashMap execute(HashMap obj) throws Exception {
        return daoPFMaster.execute(obj);
    }

    public HashMap executeQuery(HashMap obj) throws Exception {
        return daoPFMaster.executeQuery(obj);
    }
}
