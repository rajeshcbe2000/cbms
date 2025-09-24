/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition.  
 * 
 * TradingGroupBean.java
 *
 * Created on 02 March, 2015, 4:50 PM
 */

package com.see.truetransact.serverside.trading.tradinggroup;
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
public class TradingGroupBean implements SessionBean, TTDAOImpl{
  
    TradingGroupDAO tradingGroupDAO = null;

	public void ejbCreate() throws CreateException, ServiceLocatorException {
		tradingGroupDAO = new TradingGroupDAO();
	}

	public void ejbRemove()  {
		tradingGroupDAO = null;
	}

	public void setSessionContext(SessionContext sess) {
	}

	public void ejbActivate() {
	}

	public void ejbPassivate(){
	}

	public HashMap execute(HashMap obj)  throws Exception {
		return tradingGroupDAO.execute(obj);
	}

	public HashMap executeQuery (HashMap obj) throws Exception {
		return tradingGroupDAO.executeQuery(obj);
	}
    
}
