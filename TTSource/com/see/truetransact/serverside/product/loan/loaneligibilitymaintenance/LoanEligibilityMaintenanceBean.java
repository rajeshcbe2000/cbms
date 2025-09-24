/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition. 
 * 
 * LoanEligibilityMaintenanceBean.java
 *
 * Created on 31 January, 2013, 3:30 PM
 */
package com.see.truetransact.serverside.product.loan.loaneligibilitymaintenance;

import java.beans.*;
import javax.ejb.SessionBean;
import javax.ejb.SessionContext;
import javax.ejb.CreateException;

import java.rmi.RemoteException;
import java.util.HashMap;

import com.see.truetransact.transferobject.product.operativeacct.*;
import com.see.truetransact.commonutil.TTException;
import com.see.truetransact.serverside.TTDAOImpl;
import com.see.truetransact.serverexception.ServiceLocatorException;

/**
 *
 * @author Admin
 */
public class LoanEligibilityMaintenanceBean implements SessionBean, TTDAOImpl {

    LoanEligibilityMaintenanceDAO loanEligibilityMaintenanceDAO = null;

    public void ejbCreate() throws CreateException, ServiceLocatorException {
        loanEligibilityMaintenanceDAO = new LoanEligibilityMaintenanceDAO();
    }

    public void ejbRemove() {
        loanEligibilityMaintenanceDAO = null;
    }

    public void setSessionContext(SessionContext sess) {
    }

    public void ejbActivate() {
    }

    public void ejbPassivate() {
    }

    public HashMap execute(HashMap obj) throws Exception {
        return loanEligibilityMaintenanceDAO.execute(obj);
    }

    public HashMap executeQuery(HashMap obj) throws Exception {
        return loanEligibilityMaintenanceDAO.executeQuery(obj);
    }
}
