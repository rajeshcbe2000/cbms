/* Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition.
 * 
 *
 * PooledObject.java
 *
 * Created on May 25, 2004, 4:24 PM
 */

package com.see.truetransact.ui.batchprocess;

import com.see.truetransact.clientproxy.ProxyFactory;
import com.see.truetransact.clientproxy.ProxyParameters;

/**
 *
 * @author  bala
 */

public class PooledObject {
    protected static ProxyFactory proxyFactory;
    
    static {
        try {
//            ProxyParameters.USER_ID = "admin";
//            ProxyParameters.BRANCH_ID = "bran";
            proxyFactory = ProxyFactory.createProxy();
        } catch (Exception exc) {}
    }
    
    /** Creates a new instance of PooledObject */
    public PooledObject() {
    }
}
