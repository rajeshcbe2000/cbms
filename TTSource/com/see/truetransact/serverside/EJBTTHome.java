/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition.  
 * 
 *
 * EJBTTHome.java
 *
 * Created on June 23, 2003, 11:18 AM
 */
package com.see.truetransact.serverside;

import javax.ejb.EJBLocalHome;
import javax.ejb.CreateException;

import com.see.truetransact.serverexception.ServiceLocatorException;

/**
 * @author Balachandar
 */
public interface EJBTTHome extends EJBLocalHome {

    public EJBTTRemote create() throws CreateException, ServiceLocatorException;
}