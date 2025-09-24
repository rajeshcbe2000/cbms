/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition.  
 **
 *
 * LookUpBean.java
 *
 * Created on June 23, 2003, 4:20 PM
 */

package com.see.truetransact.serverside.common.lookup;

import javax.ejb.SessionBean;
import javax.ejb.SessionContext;
import javax.ejb.CreateException;

import java.rmi.RemoteException;
import java.util.HashMap;

import com.see.truetransact.commonutil.TTException;

import com.see.truetransact.serverside.TTDAOImpl;

import com.see.truetransact.serverside.common.lookup.LookUpDAO;
import com.see.truetransact.transferobject.common.lookup.LookUpTO;
import com.see.truetransact.serverexception.ServiceLocatorException;

/**
 * Acct Bean which calls AccountHead DAO
 *
 * @author  Balachandar
 */

public class LookUpBean implements SessionBean, TTDAOImpl {
    LookUpDAO lookUpCreation;
    
    public void ejbCreate() throws CreateException, ServiceLocatorException {
        lookUpCreation = new LookUpDAO();
    }
    
    public void ejbRemove()  {
        lookUpCreation = null;
    }
    
    public void setSessionContext(SessionContext sess) {
    }
    
    public void ejbActivate() {
    }
    
    public void ejbPassivate(){
    }
    
    public HashMap execute(HashMap obj)  throws Exception {
		return null;
    }
    public HashMap executeQuery (HashMap obj) throws Exception {
        return lookUpCreation.executeQuery(obj);
    }
}
