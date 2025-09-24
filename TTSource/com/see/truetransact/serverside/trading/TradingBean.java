/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition. 
 * 
 * TradingBean.java
 *
 * Created on 16 September, 2011, 4:52 PM
 */

package com.see.truetransact.serverside.trading;
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
public class TradingBean implements SessionBean, TTDAOImpl{
  
    TradingDAO daoTrading = null;

	public void ejbCreate() throws CreateException, ServiceLocatorException {
		daoTrading = new TradingDAO();
	}

	public void ejbRemove()  {
		daoTrading = null;
	}

	public void setSessionContext(SessionContext sess) {
	}

	public void ejbActivate() {
	}

	public void ejbPassivate(){
	}

	public HashMap execute(HashMap obj)  throws Exception {
		return daoTrading.execute(obj);
	}

	public HashMap executeQuery (HashMap obj) throws Exception {
		return daoTrading.executeQuery(obj);
	}
    
}
