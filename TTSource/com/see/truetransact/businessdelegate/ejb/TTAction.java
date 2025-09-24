/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition.  
 * 
 *
 * TTAction.java
 *
 * Created on June 23, 2003, 12:23 PM
 */

package com.see.truetransact.businessdelegate.ejb;

import java.util.HashMap;
import java.rmi.RemoteException;

/** This interface is a TTAction interface which has all the common functionalities.
 *
 * @author Balachandar
 */

public interface TTAction {
    /** To insert, update and delete data from the database based on the object Bean
     * @param obj Collection of Beans in the HashMap
     * @param param Connecting to a specific Session Bean.
     * @throws Exception Throws Exception if any error occurred
     * @throws RemoteException Called in Remote Interface
     */
    public HashMap execute (HashMap obj, HashMap param) throws Exception, RemoteException;
    /** To Execute Query from the database based on the object Bean
     *
     * @param   obj         Collection of Beans in the HashMap
     * @param   param       Connecting to a specific Session Bean
     * @throws  Exception   Throws Exception if any error occurred
     * @throws  RemoteException Called in Remote Interface
     * @return  HashMap     Database data Returns as a HashMap
     */
    public HashMap executeQuery (HashMap obj, HashMap param) throws Exception, RemoteException;
}
