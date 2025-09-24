/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition.
 * 
 *
 * EmpTrainingBean.java
 *
 * Created on june, 2010, 1:00 PM
 */
package com.see.truetransact.serverside.sysadmin.emptraining;

import javax.ejb.SessionBean;
import javax.ejb.SessionContext;
import javax.ejb.CreateException;

import java.util.HashMap;

import com.see.truetransact.transferobject.sysadmin.user.*;
import com.see.truetransact.commonutil.TTException;
import com.see.truetransact.serverside.TTDAOImpl;
import com.see.truetransact.serverexception.ServiceLocatorException;

/**
 *
 *
 * @author swaroop
 */
public class EmpTrainingBean implements SessionBean, TTDAOImpl {

    private EmpTrainingDAO empTrainingDAO;

    public void ejbCreate() throws CreateException, ServiceLocatorException {
        empTrainingDAO = new EmpTrainingDAO();
    }

    public void ejbRemove() {
        empTrainingDAO = null;
    }

    public void setSessionContext(SessionContext sess) {
    }

    public void ejbActivate() {
    }

    public void ejbPassivate() {
    }

    public HashMap execute(HashMap obj) throws Exception {
        return empTrainingDAO.execute(obj);
    }

    public HashMap executeQuery(HashMap obj) throws Exception {
        return empTrainingDAO.executeQuery(obj);
    }
}
