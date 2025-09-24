/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition.
 * 
 *
 * GroupScreenBean.java
 *
 * Created on March 2, 2004, 2:45 PM
 */
package com.see.truetransact.serverside.sysadmin.branchgroupscr;

import javax.ejb.SessionBean;
import java.util.HashMap;
import javax.ejb.SessionContext;
import javax.ejb.CreateException;

import com.see.truetransact.serverside.TTDAOImpl;
import com.see.truetransact.serverexception.ServiceLocatorException;

/**
 * Group Screen Bean
 *
 * @author Pinky
 */
public class BranchGroupScreenBean implements SessionBean, TTDAOImpl {

    private BranchGroupScreenDAO groupScreenDAO;

    public void ejbCreate() throws CreateException, ServiceLocatorException {
        groupScreenDAO = new BranchGroupScreenDAO();
    }

    public void ejbRemove() {
        groupScreenDAO = null;
    }

    public void setSessionContext(SessionContext sess) {
    }

    public void ejbActivate() {
    }

    public void ejbPassivate() {
    }

    public HashMap execute(HashMap obj) throws Exception {
        return groupScreenDAO.execute(obj);
    }

    public HashMap executeQuery(HashMap obj) throws Exception {
        return groupScreenDAO.executeQuery(obj);
    }
}
