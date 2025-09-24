/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition.
 * 
 *
 * AccountTransferBean.java
 *
 * Created on September 25, 2003, 4:43 PM
 */
package com.see.truetransact.serverside.operativeaccount;

import javax.ejb.CreateException;
import java.rmi.RemoteException;
import java.util.HashMap;

import com.see.truetransact.commonutil.TTException;
import com.see.truetransact.serverside.TTDAOImpl;
import com.see.truetransact.serverexception.ServiceLocatorException;

/**
 *
 * @author amathan
 */
public class AccountTransferBean implements javax.ejb.SessionBean, TTDAOImpl {

    AccountTransferDAO accountTransferDAO = null;

    public void ejbCreate() throws CreateException, ServiceLocatorException {
        accountTransferDAO = new AccountTransferDAO();
    }

    public void ejbRemove() {
        accountTransferDAO = null;
    }

    /**
     * Creates a new instance of AccountTransferBean
     */
    public AccountTransferBean() {
    }

    public void ejbActivate() {
    }

    public void ejbPassivate() {
    }

    public void setSessionContext(javax.ejb.SessionContext sessionContext) {
    }

    /*
     * public Object getData(Object obj) throws TTException {
     * System.out.println("Entered AccountTransferBean"); Object objReturn =
     * null; try { objReturn = (Object) accountTransferDAO.getData(obj); } catch
     * (Exception ex) { ex.printStackTrace(); throw new
     * TTException(ex.toString()); } return objReturn;
    }
     */
    /*
     * public void insertData(Object obj) throws CreateException,
     * RemoteException,TTException { }
     *
     * public void updateData(Object obj) throws CreateException,
     * RemoteException { }
     *
     * public void deleteData(Object obj) throws CreateException,
     * RemoteException {
   }
     */
    public HashMap execute(HashMap param) throws Exception {
        return accountTransferDAO.execute(param);
    }

    public HashMap executeQuery(HashMap param) throws Exception {
        return accountTransferDAO.executeQuery(param);
    }
}
