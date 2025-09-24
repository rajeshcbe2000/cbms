/* Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition.  
 * 
 *
 * TTDAO.java
 *
 * Created on June 23, 2003, 12:23 PM
 */
package com.see.truetransact.serverside;

import java.util.HashMap;

/**
 * Interface for DAO as well as EJBTTRemote interface.
 *
 * @author Balachandar
 */
public interface TTDAOImpl {

    public HashMap execute(HashMap obj) throws Exception;

    public HashMap executeQuery(HashMap obj) throws Exception;
}