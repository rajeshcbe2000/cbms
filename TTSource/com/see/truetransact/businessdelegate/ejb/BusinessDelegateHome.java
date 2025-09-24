/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition.
 * 
 *
 * BusinessDelegateHome.java
 *
 * Created on June 23, 2003, 12:23 PM
 */

package com.see.truetransact.businessdelegate.ejb;

import javax.ejb.EJBHome;
import javax.ejb.CreateException;

import java.rmi.RemoteException;

/**
 * This interface is a Home interface which returns Remote Interface for BusinessDelegateBean.
 *
 * @author  Balachandar
 */

public interface BusinessDelegateHome extends EJBHome {
    
    /** Home's create method which returns BusinessDelegateRemote Interface.
     *
     * @throws  CreateException EJB's create Exception
     * @throws  RemoteException Remote Interface is used
     * @return  Returns BusinessDelegate's Remote Interface
     */
    public BusinessDelegateRemote create() throws CreateException, RemoteException;
}
