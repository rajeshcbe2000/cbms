/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition.  
 * 
 *
 * ProxyFactory.java
 *
 * Created on June 23, 2003, 12:23 PM
 */

package com.see.truetransact.clientproxy;

import com.see.truetransact.commonutil.CommonConstants;
import com.see.truetransact.commonutil.CommonUtil;
import com.see.truetransact.clientutil.ClientConstants;

import org.apache.log4j.Logger;

import java.util.HashMap;

/**
 * This class gives appropriate Proxy class based on the ClientConstants.
 *
 * @author Balachandar
 */
public abstract class ProxyFactory {
    private final static Logger log = Logger.getLogger(ProxyFactory.class);
    
    public static final String EJBAPPL_CLIENT = "EJBAPPL_CLIENT"; // EJB application Client String
    public static final String WEBAPPL_CLIENT = "WEBAPPL_CLIENT"; // Web Application Client String
    public static final String WEBHTTPS_CLIENT = "WEBHTTPS_CLIENT";

    /** This methods used to execute insert, update and delete operations
     *
     * @param       obj         HashMap which contains Data Bean for Insert, update and delete operations
     * @param       param       Used to connect other Session  Beans
     * @exception   Exception   Throws exception based on the backend error
     */
    public abstract HashMap execute(HashMap obj, HashMap param) throws Exception;
    
    /** Execute Query used to retrieve data from the database
     *
     * @param       obj         Parameter for the query (Where condition)
     * @param       param       Connection configuration for Business Session Beans
     * @exception   Exception   Throws Exception based on Backend error
     * @return      Returns Database data as HashMap
     */
    public abstract HashMap executeQuery(HashMap obj, HashMap param) throws Exception;
    
    /** Returns a proxy based on ClientConstants configuration.
     *
     * @throws  Exception   Throws Exception based on the proxy error
     * @return  Returns ProxyFactory based on ClientConstants Configuration
     */
    public static ProxyFactory createProxy() throws Exception {
//        log.info("Client Proxy Type " + ClientConstants.CLIENT_PROXY);
        try {
            ProxyParameters.IP_ADDR = java.net.InetAddress.getLocalHost().getHostAddress(); //Added By Suresh  Referred By Rajesh Sir
        } catch (Exception e) {
            System.out.println("Error in createProxy");
            e.printStackTrace();
        }
        if (ClientConstants.CLIENT_PROXY.equals(EJBAPPL_CLIENT)) {
            return EJBClientProxy.getInstance();
        } else if (ClientConstants.CLIENT_PROXY.equals(WEBAPPL_CLIENT)) {
            return WebClientProxy.getInstance();
        } else if (ClientConstants.CLIENT_PROXY.equals(WEBHTTPS_CLIENT)) {
            return WebHttpsClientProxy.getInstance();
        } else {
            log.error("Bad Proxy Creation : " + ClientConstants.CLIENT_PROXY);
            throw new ClientProxyException("Bad Proxy Creation : " + ClientConstants.CLIENT_PROXY);
        }
    }
    
    /* This method adds additional information 
     * to the parameter map
     */
    protected void addInfoToMap(HashMap obj) throws Exception {
//        if (!com.see.truetransact.ui.login.LoginUI.loginVisible) {
//            long currentTime = (new java.util.Date()).getTime();
//            if (ProxyParameters.lastAccessTime > 0 && 
//                (currentTime > (ProxyParameters.lastAccessTime + 
//                CommonUtil.convertObjToInt(CommonConstants.TIME_OUT) * 60 * 1000))) {
//                    System.out.println("#### currentTime if part : "+currentTime +
//                    "   /  ProxyParameters.lastAccessTime : " + ProxyParameters.lastAccessTime +
//                    CommonUtil.convertObjToInt(CommonConstants.TIME_OUT) * 60 * 1000);
//                    
////                    HashMap whereMap = new HashMap();
////                    whereMap.put("USERID", ProxyParameters.USER_ID);
////                    whereMap.put("STATUS", "LOGOUT");
////                    java.util.Date currLogoutDt = new java.util.Date();
////                    whereMap.put("CURR_DATE", currLogoutDt);
////                    HashMap screenLockMap=new HashMap();
////                    screenLockMap.put("USER_ID",ProxyParameters.USER_ID);
////                    com.see.truetransact.ui.login.LoginUI.loginVisible = true;
////                    com.see.truetransact.clientutil.ClientUtil.execute("DELETE_SCREEN_LOCK", screenLockMap);
//                    // Update the database login status as LOGOUT
////                    com.see.truetransact.clientutil.ClientUtil.execute("updateUserLogoutStatus", whereMap);
////                    whereMap.put("BRANCHCODE", ProxyParameters.BRANCH_ID);
////                    whereMap.put("LOGINSTATUS", "LOGOUT");
////                    whereMap.put("DATE_TIME", currLogoutDt);
////                    com.see.truetransact.clientutil.ClientUtil.execute("loginHistory", whereMap);
////                    com.see.truetransact.ui.login.LoginUI.loginVisible = false;
//                    com.see.truetransact.uicomponent.COptionPane.showMessageDialog(null, "Session Timed out. Please Login Again.");
//                    com.see.truetransact.ui.login.LoginUI loginUI = new com.see.truetransact.ui.login.LoginUI(true);
//
////                    java.awt.Dimension frameSize = loginUI.getSize();
////                    java.awt.Dimension screenSize = java.awt.Toolkit.getDefaultToolkit().getScreenSize();
////
////                    if (frameSize.height > screenSize.height){
////                        frameSize.height = screenSize.height;
////                    }
////                    if (frameSize.width > screenSize.width){
////                        frameSize.width = screenSize.width;
////                    }
////                    loginUI.setLocation((screenSize.width - frameSize.width) / 2,
////                    (screenSize.height - frameSize.height) / 2 - 60);
////                    loginUI.show();
//                    ProxyParameters.lastAccessTime = currentTime;
//                    throw new com.see.truetransact.commonutil.TTException("Timeout. Please Login Again.");
//            } else {
//                ProxyParameters.lastAccessTime = currentTime;
//                System.out.println("#### currentTime else part : "+currentTime);
//            }
//        }
        
        obj.put(CommonConstants.IP_ADDR, java.net.InetAddress.getLocalHost().getHostAddress());
        obj.put(CommonConstants.USER_ID, ProxyParameters.USER_ID);
        obj.put(CommonConstants.BRANCH_ID, ProxyParameters.BRANCH_ID);
        obj.put(CommonConstants.BANK_ID, ProxyParameters.BANK_ID);
        obj.put("DB_DRIVER_NAME", ProxyParameters.dbDriverName);
        obj.put(CommonConstants.SESSION_ID, ProxyParameters.SESSION_ID); //ADDED BY ABI
    }
}
