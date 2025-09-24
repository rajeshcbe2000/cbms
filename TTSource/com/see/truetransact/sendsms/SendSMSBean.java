/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition.
 * 
 *
 * SMSParameterBean.java
 * 
 * Created on Fri May 04 13:31:47 IST 2012
 */

package com.see.truetransact.sendsms;

import javax.ejb.SessionBean;
import javax.ejb.SessionContext;
import javax.ejb.CreateException;

import java.rmi.RemoteException;
import java.util.HashMap;

import com.see.truetransact.businessdelegate.ejb.TTAction;

/**
 * SMSParameter Bean which calls the DAO.
 *
 */

public class SendSMSBean implements SessionBean, TTAction {
	SendSMSDAO daoSendSMS = null;

	public void ejbCreate() throws CreateException, RemoteException {
            try {
		daoSendSMS = new SendSMSDAO();
            } catch(Exception tte){
                System.out.println("TTException caught");
                tte.printStackTrace();
            }
	}

	public void ejbRemove()  {
		daoSendSMS = null;
	}

	public void setSessionContext(SessionContext sess) {
	}

	public void ejbActivate() {
	}

	public void ejbPassivate(){
	}

	public HashMap execute(HashMap obj, HashMap param) throws Exception {
		return daoSendSMS.execute(obj);
	}

	public HashMap executeQuery (HashMap obj, HashMap param) throws Exception {
		return daoSendSMS.executeQuery(obj);
	}
        
}
