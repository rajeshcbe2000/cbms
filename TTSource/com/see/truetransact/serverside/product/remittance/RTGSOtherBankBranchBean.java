/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition.  
 * 
 *
 * RTGSOtherBankBranch.java
 *
 * Created on August 20, 2003, 3:20 AM
 */

package com.see.truetransact.serverside.product.remittance;

import javax.ejb.SessionBean;
import javax.ejb.SessionContext;
import javax.ejb.CreateException;

import java.util.HashMap;

import com.see.truetransact.serverside.TTDAOImpl;
import com.see.truetransact.serverexception.ServiceLocatorException;

/**
 * This is used for RTGSOtherBankBranchDAO Data Access.
 *
 * @author  Suresh R
 *
 */
public class RTGSOtherBankBranchBean implements SessionBean, TTDAOImpl {
    RTGSOtherBankBranchDAO daoRTGSOtherBankBranch = null;
    public void ejbCreate() throws CreateException, ServiceLocatorException {
        daoRTGSOtherBankBranch = new RTGSOtherBankBranchDAO();
    }
    
    public void ejbRemove()  {
        daoRTGSOtherBankBranch = null;
    }
    
    public void setSessionContext(SessionContext sess) {
    }
    
    public void ejbActivate() {
    }
    
    public void ejbPassivate(){
    }

    public HashMap execute(HashMap obj)  throws Exception {
        return daoRTGSOtherBankBranch.execute(obj);
    }

    public HashMap executeQuery (HashMap obj) throws Exception {
	return daoRTGSOtherBankBranch.executeQuery(obj);
    }
}

