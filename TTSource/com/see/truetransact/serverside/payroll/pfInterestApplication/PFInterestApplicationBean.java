/**
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition. 
 * 
 * Author   : Chithra
 * Location : Thrissur
 * Date of Completion : 1-07-2015
 */
package com.see.truetransact.serverside.payroll.pfInterestApplication;

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
public class PFInterestApplicationBean implements SessionBean, TTDAOImpl {

    private PFInterestApplicationDAO pfInterestApplicationDAO;

    public void ejbCreate() throws CreateException, ServiceLocatorException {
        pfInterestApplicationDAO = new PFInterestApplicationDAO();
    }

    public void ejbRemove() {
        pfInterestApplicationDAO = null;
    }

    public void setSessionContext(SessionContext sess) {
    }

    public void ejbActivate() {
    }

    public void ejbPassivate() {
    }

    public HashMap execute(HashMap obj) throws Exception {
        return pfInterestApplicationDAO.execute(obj);
    }

    public HashMap executeQuery(HashMap obj) throws Exception {
        return pfInterestApplicationDAO.executeQuery(obj);
    }
}
