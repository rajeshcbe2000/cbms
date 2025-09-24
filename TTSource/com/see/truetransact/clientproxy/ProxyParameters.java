/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition. 
 * 
 * ProxyParameters.java
 *
 * Created on May 24, 2004, 11:06 AM
 */

package com.see.truetransact.clientproxy;

/**
 *
 * @author  bala
 */
public class ProxyParameters {
    public static String BANK_ID;  // Logged in Bank
    public static String USER_ID;  // Logged in User
    public static String BRANCH_ID;  // Logged in Branch
    public static String SELECTED_BRANCH_ID;  // Selected in Branch
    //public static String HIERARCHY_ID;  // Logged in HIERARCHY
    public static int HIERARCHY_ID;  // Logged in HIERARCHY
    public static java.util.Locale LANGUAGE = new java.util.Locale("hi"); // Language
    public static long lastAccessTime = 0; // Last Access Time in Minutes
    public static String dbDriverName;
    public static String SESSION_ID;
    public static String HEAD_OFFICE;
    public static String IP_ADDR;	//Added By Suresh R
    
//    public static String IP_ADDR=java.net.InetAddress.getLocalHost().getHostAddress();
    
    /** Creates a new instance of ProxyParameters */
    public ProxyParameters() {
    }
}
