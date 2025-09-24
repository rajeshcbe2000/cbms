/* Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition.
 * 
 *
 * EnvironmentVariables.java
 *
 * Created on February 27, 2004, 11:49 AM
 */

package com.see.truetransact.commonutil;

import java.util.HashMap;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.IOException;

/**
 * Fetches all the System Environment Varaibles available in the
 * Windows Environment. 
 * 
 * This is Specific to Windows
 *
 * @author  bala
 */
public class EnvironmentVariables extends HashMap {
    
    // Constructor which populate environment variables
    public EnvironmentVariables() {
        init();
    }
    
    /* populates all the System Environment variables
     * into HashMap.
     */
    private void init() {
        // Specific to Windows
        String[] cmd = {"cmd", "/c", "SET"};
        try {
            // executes Set Command to get the environment variables
            Process proc = Runtime.getRuntime().exec(cmd);
            BufferedReader inp = new BufferedReader(new InputStreamReader(proc.getInputStream()));
            String LINE = inp.readLine();
            
            // Parsing environment variable and putting into HashMap
            while (LINE != null) {
                int eqPos = LINE.indexOf("=");
                // checking for = value
                if (eqPos >= 0) {
                    String name = LINE.substring(0, eqPos);
                    String value = LINE.substring(eqPos + 1);
                    put(name.toUpperCase(), value);
                }
                LINE = inp.readLine();
            }
            inp.close();
        } catch (IOException e) {
            System.out.println("I/O error");
        }
    }
    
    /**
     * Gives the inputted name's value from Environment Variable
     */
    public synchronized Object get(Object name) {
        // this case-desensitizes the name, overriding Hashtable.get()
        String theName = name.toString().toUpperCase();
        return super.get(theName);
    }
    
    /**
     * Assigns new Environment variable in the System
     */
    public synchronized Object put(Object name, Object value) {
        String theName = name.toString().toUpperCase();
        String theValue = value.toString();
        Object result = super.get(theName);
        
        // this is windows specific
        String[] cmd = {"cmd", "/c", "SET " + theName + "=" + theValue};
        
        try {
            Process proc = Runtime.getRuntime().exec(cmd);
            BufferedReader inp = new BufferedReader(new InputStreamReader(proc.getInputStream()));
            String LINE = inp.readLine();
            while (LINE != null) {}
            super.put(name.toString().toUpperCase(), value.toString());
            return result;
        } catch (IOException e) {
            System.out.println("I/O error");
            return null;
        }
        
    }
    
    public static void main(String[] args) {
        EnvironmentVariables ev = new EnvironmentVariables();
        System.out.println(ev);
    }
}
