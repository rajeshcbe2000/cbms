/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition. 
 * 
 * RestoreStockBean.java
 *
 * Created on 02 March, 2015, 4:50 PM
 */

package com.see.truetransact.serverside.trading.restorestock;
import javax.ejb.SessionBean;
import javax.ejb.SessionContext;
import javax.ejb.CreateException;
import java.util.HashMap;

import com.see.truetransact.serverside.TTDAOImpl;
import com.see.truetransact.serverexception.ServiceLocatorException;

/**
 *
 * @author  aravind
 */
public class RestoreStockBean implements SessionBean, TTDAOImpl{
  
    RestoreStockDAO restoreStockDAO = null;

	public void ejbCreate() throws CreateException, ServiceLocatorException {
		restoreStockDAO = new RestoreStockDAO();
	}

	public void ejbRemove()  {
		restoreStockDAO = null;
	}

	public void setSessionContext(SessionContext sess) {
	}

	public void ejbActivate() {
	}

	public void ejbPassivate(){
	}

	public HashMap execute(HashMap obj)  throws Exception {
		return restoreStockDAO.execute(obj);
	}

	public HashMap executeQuery (HashMap obj) throws Exception {
		return restoreStockDAO.executeQuery(obj);
	}
    
}
