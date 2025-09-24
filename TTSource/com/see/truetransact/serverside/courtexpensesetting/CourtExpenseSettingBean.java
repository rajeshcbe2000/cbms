/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition.
 * 
 *
 * EmpTransferBean.java
 *
 * Created on june, 2010, 1:00 PM
 */
package com.see.truetransact.serverside.courtexpensesetting;

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
public class CourtExpenseSettingBean implements SessionBean, TTDAOImpl {

    private CourtExpenseSettingDAO courtexpenseDAO;

    public void ejbCreate() throws CreateException, ServiceLocatorException {
        courtexpenseDAO = new CourtExpenseSettingDAO();
    }

    public void ejbRemove() {
        courtexpenseDAO = null;
    }

    public void setSessionContext(SessionContext sess) {
    }

    public void ejbActivate() {
    }

    public void ejbPassivate() {
    }

    public HashMap execute(HashMap obj) throws Exception {
        return courtexpenseDAO.execute(obj);
    }

    public HashMap executeQuery(HashMap obj) throws Exception {
        return courtexpenseDAO.executeQuery(obj);
    }
}
