/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition.  
 * 
 * NbChequeBookMaintenanceBean.java
 */

package com.see.truetransact.serverside.netbankingrequest.nbchequebookmaintenance;

import com.see.truetransact.serverexception.ServiceLocatorException;
import com.see.truetransact.serverside.TTDAOImpl;
import javax.ejb.SessionBean;
import java.util.HashMap;
import javax.ejb.CreateException;
import javax.ejb.SessionContext;
/**
 *
 * @author Abhishek
 */
public class NbChequeBookMaintenanceBean implements SessionBean, TTDAOImpl{
     NbChequeBookMaintenanceDAO daoNetBankingRequest = null;

    public void ejbCreate() throws CreateException, ServiceLocatorException {
        daoNetBankingRequest = new NbChequeBookMaintenanceDAO();
    }

    public void ejbRemove() {
        daoNetBankingRequest = null;
    }

    public void setSessionContext(SessionContext sess) {
    }

    public void ejbActivate() {
    }

    public void ejbPassivate() {
    }

    public HashMap execute(HashMap obj) throws Exception {
         return daoNetBankingRequest.execute(obj);
    }
    
    public HashMap executeQuery(HashMap obj) throws Exception {
       return daoNetBankingRequest.executeQuery(obj);
    }
}
