/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition.
 * 
 *
 * EditMigratedDataBean.java
 * 
 * Created on Tue Oct 11 13:18:08 IST 2011
 */

package com.see.truetransact.serverside.transaction.editMigratedData;

import javax.ejb.SessionBean;
import javax.ejb.SessionContext;
import javax.ejb.CreateException;

import java.util.HashMap;

import com.see.truetransact.serverside.TTDAOImpl;
import com.see.truetransact.serverexception.ServiceLocatorException;

/**
 * OperativeAcctProductBean which calls OperativeAcctProduct DAO
 *
 * @author  Balachandar
 */

public class EditMigratedDataBean implements SessionBean, TTDAOImpl {
    EditMigratedDataDAO editMigratedDataDAO = null;
    public void ejbCreate() throws CreateException, ServiceLocatorException {
        editMigratedDataDAO = new EditMigratedDataDAO();
    }
    
    public void ejbRemove()  {
        editMigratedDataDAO = null;
    }
    
    public void setSessionContext(SessionContext sess) {
    }
    
    public void ejbActivate() {
    }
    
    public void ejbPassivate(){
    }

    public HashMap execute(HashMap obj)  throws Exception {
        return editMigratedDataDAO.execute(obj);
    }

    public HashMap executeQuery (HashMap obj) throws Exception {
	return editMigratedDataDAO.executeQuery(obj);
    }
}
