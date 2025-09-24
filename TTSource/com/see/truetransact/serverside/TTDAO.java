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
public abstract class TTDAO {

    protected String _branchCode;

    public abstract HashMap execute(HashMap obj) throws Exception;

    public abstract HashMap executeQuery(HashMap obj) throws Exception;
}