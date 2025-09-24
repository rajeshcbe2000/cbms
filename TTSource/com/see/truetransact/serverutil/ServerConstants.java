/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition.  
 **
 *
 * ServerConstants.java
 *
 * Created on June 18, 2003, 4:28 PM
 */

package com.see.truetransact.serverutil;

/**
 * Serverside constants
 *
 * @author  Balachandar
 */
import com.see.truetransact.commonutil.Dummy;
import com.see.truetransact.transferobject.sysadmin.config.ConfigPasswordTO;
import java.util.List;
import java.util.HashMap;

public class ServerConstants {
    private static java.util.Properties serverProperties = new java.util.Properties();
    public static String SERVER_PATH; // SERVER_PATH Address or Machine Name
    static {
        try {
            Dummy cons = new Dummy();
            serverProperties.load(cons.getClass().getResourceAsStream("/com/see/truetransact/commonutil/TT.properties"));
            SERVER_PATH = serverProperties.getProperty("SERVER_PATH");
            cons = null;
        } catch (Exception ex) {
            
        }
    }
    
    // DAO URL value
    public static final String DAO_URL = "com/see/truetransact/serverutil/SqlMapConfig.xml";
    public static final String DAO_URL_BATCH = "com/see/truetransact/serverutil/SqlMapBatchConfig.xml";
    public static final String DAO_URL_LOG = "com/see/truetransact/serverutil/SqlMapLogConfig.xml";
    
    // Serverside EJB package
    public static final String EJB_PKG =  "com.see.truetransact.serverside.";

    private static HashMap test = new HashMap();
    public static final String HO = ((ConfigPasswordTO)((List)com.see.truetransact.serverutil.ServerUtil.executeQuery("getSelectConfigPasswordTO", test)).get(0)).getCboBranches();
    
}
