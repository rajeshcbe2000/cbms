/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition.  
 * 
 * TradingPurchaseBean.java
 *
 * Created on 02 March, 2015, 4:50 PM
 */

package com.see.truetransact.serverside.trading.tradingpurchase;
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
public class TradingPurchaseBean implements SessionBean, TTDAOImpl{
  
    TradingPurchaseDAO tradingpurchaseDAO = null;

	public void ejbCreate() throws CreateException, ServiceLocatorException {
		tradingpurchaseDAO = new TradingPurchaseDAO();
	}

	public void ejbRemove()  {
		tradingpurchaseDAO = null;
	}

	public void setSessionContext(SessionContext sess) {
	}

	public void ejbActivate() {
	}

	public void ejbPassivate(){
	}

	public HashMap execute(HashMap obj)  throws Exception {
		return tradingpurchaseDAO.execute(obj);
	}

	public HashMap executeQuery (HashMap obj) throws Exception {
		return tradingpurchaseDAO.executeQuery(obj);
	}
    
}
