/*
 * Copyright 2004 SeE Consulting (P) Ltd. All rights reserved.
 *
 * This software is the proprietary information of SeE Consulting (P) Ltd..
 * Use is subject to license terms.
 *
 * MultipleRealisationBean.java
 * 
 *  Created on Wed Nov 13 13:59:17 IST 2019
 *
 * Created by Sathiya
 */

package com.see.truetransact.serverside.transaction.electronicreconciliation;

import javax.ejb.SessionBean;
import javax.ejb.SessionContext;
import javax.ejb.CreateException;

import java.util.HashMap;

import com.see.truetransact.serverexception.ServiceLocatorException;

import com.see.truetransact.serverside.TTDAOImpl;

/**
 * ElectronicReconciliationRequestBean which calls the DAO.
 *
 */

public class ElectronicReconciliationBean implements SessionBean, TTDAOImpl {
	ElectronicReconciliationDAO daoMultipleRealisation = null;

	public void ejbCreate() throws CreateException, ServiceLocatorException {
		daoMultipleRealisation = new ElectronicReconciliationDAO();
	}

	public void ejbRemove()  {
		daoMultipleRealisation = null;
	}

	public void setSessionContext(SessionContext sess) {
	}

	public void ejbActivate() {
	}

	public void ejbPassivate(){
	}

	public HashMap execute(HashMap obj)  throws Exception {
		return daoMultipleRealisation.execute(obj);
	}

	public HashMap executeQuery (HashMap obj) throws Exception {
		return daoMultipleRealisation.executeQuery(obj);
	}
}
