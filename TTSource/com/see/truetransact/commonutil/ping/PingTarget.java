/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition. 
 * 
 * Target.java
 *
 * Created on January 23, 2004, 4:20 PM
 */

package com.see.truetransact.commonutil.ping;

import java.net.InetSocketAddress;
import java.net.InetAddress;
import java.nio.channels.SocketChannel;
import java.util.HashMap;
import java.util.LinkedHashMap;

// Representation of a ping target
//
public class PingTarget {
    public static final String IPADDR = "IP Address";
    public static final String HOST = "Host";
    public static final String PINGRESULT = "Result";
    public static final String UNKNOWN = "Unknown Host";
    public static final String TIMEOUT = "Timed Out";
    public static final String MISEC = " ms";
    
    public InetSocketAddress address;
    public SocketChannel channel;
    public Exception failure;
    public long connectStart;
    public long connectFinish = 0;
    public boolean shown = false;
    private String _host;
    private int _port = 0;
    
    public String getHost() {
        return _host;
    }
    
    public PingTarget(String host, int port) {
        try {
            _host = host;
            _port = port;
            
            address = new InetSocketAddress(InetAddress.getByName(host),
            port);
        } catch (Exception x) {
            failure = x;
        }
    }
    
    public HashMap show() {
        LinkedHashMap map = new LinkedHashMap();
        String result;
        try {
            if (connectFinish != 0)
                result = Long.toString(connectFinish - connectStart) + MISEC;
            else if (failure != null)
                result = failure.toString();
            else
                result = TIMEOUT;

            map.put(IPADDR, address.getAddress().getHostAddress());
            map.put(HOST, address.getHostName());
            map.put(PINGRESULT, result);

            System.out.println(address.getAddress().getHostAddress() + " : " + address.getHostName() + " : " + result);
            shown = true;
        } catch (Exception exc) {
            map.put(IPADDR, _host);
            map.put(HOST, _host);
            map.put(PINGRESULT, UNKNOWN);
            
            shown = true;
        }
        return map;
    }
    
}
