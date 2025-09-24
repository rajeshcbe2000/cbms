/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition.  
 * 
 *
 * EJBTTRemote.java
 *
 * Created on June 23, 2003, 10:59 AM
 */
package com.see.truetransact.serverside;

import javax.ejb.EJBLocalObject;

import java.util.HashMap;

/**
 *
 * @author Balachandar
 */
public interface EJBTTRemote extends EJBLocalObject {

    public HashMap execute(HashMap obj) throws Exception;

    public HashMap executeQuery(HashMap obj) throws Exception;
}