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
 * Created by Kannan AR
 */

package com.see.truetransact.serverside.transaction.electronicpayment;

import javax.ejb.SessionBean;
import javax.ejb.SessionContext;
import javax.ejb.CreateException;

import java.util.HashMap;

import com.see.truetransact.serverexception.ServiceLocatorException;

import com.see.truetransact.serverside.TTDAOImpl;

/**
 * ElectronicPayment Bean which calls the DAO.
 *
 */

public class ElectronicPaymentBean implements SessionBean, TTDAOImpl {
	ElectronicPaymentDAO daoMultipleRealisation = null;

	public void ejbCreate() throws CreateException, ServiceLocatorException {
		daoMultipleRealisation = new ElectronicPaymentDAO();
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
