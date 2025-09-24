/*
 * @(#)Ping.java	1.2 01/12/13
 * Connect to each of a list of hosts and measure the time required to complete
 * the connection.  This example uses a selector and two additional threads in
 * order to demonstrate non-blocking connects and the multithreaded use of a
 * selector.
 *
 * Copyright 2001-2002 Sun Microsystems, Inc. All Rights Reserved.
 *
 * Redistribution and use in source and binary forms, with or
 * without modification, are permitted provided that the following
 * conditions are met:
 *
 * -Redistributions of source code must retain the above copyright
 * notice, this  list of conditions and the following disclaimer.
 *
 * -Redistribution in binary form must reproduct the above copyright
 * notice, this list of conditions and the following disclaimer in
 * the documentation and/or other materials provided with the
 * distribution.
 *
 * Neither the name of Sun Microsystems, Inc. or the names of
 * contributors may be used to endorse or promote products derived
 * from this software without specific prior written permission.
 *
 * This software is provided "AS IS," without a warranty of any
 * kind. ALL EXPRESS OR IMPLIED CONDITIONS, REPRESENTATIONS AND
 * WARRANTIES, INCLUDING ANY IMPLIED WARRANTY OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE OR NON-INFRINGEMENT, ARE HEREBY
 * EXCLUDED. SUN AND ITS LICENSORS SHALL NOT BE LIABLE FOR ANY
 * DAMAGES OR LIABILITIES  SUFFERED BY LICENSEE AS A RESULT OF  OR
 * RELATING TO USE, MODIFICATION OR DISTRIBUTION OF THE SOFTWARE OR
 * ITS DERIVATIVES. IN NO EVENT WILL SUN OR ITS LICENSORS BE LIABLE
 * FOR ANY LOST REVENUE, PROFIT OR DATA, OR FOR DIRECT, INDIRECT,
 * SPECIAL, CONSEQUENTIAL, INCIDENTAL OR PUNITIVE DAMAGES, HOWEVER
 * CAUSED AND REGARDLESS OF THE THEORY OF LIABILITY, ARISING OUT OF
 * THE USE OF OR INABILITY TO USE SOFTWARE, EVEN IF SUN HAS BEEN
 * ADVISED OF THE POSSIBILITY OF SUCH DAMAGES.
 *
 * You acknowledge that Software is not designed, licensed or
 * intended for use in the design, construction, operation or
 * maintenance of any nuclear facility.
 *
 * Modified by Bala
 */

package com.see.truetransact.commonutil.ping;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.HashMap;
import java.util.regex.Pattern;

public class Ping {
    HashMap hMap = new HashMap();
    
    // The default daytime port
    static int DAYTIME_PORT = 13;
    
    // The port we'll actually use
    static int port = DAYTIME_PORT;
    
    static String[] host;
    
    public Ping() {}
    
    public Ping(String host) {
        this.host[0] = new String(host);
    }
    
    public Ping(int port, String host) {
        this.port = port;
        this.host[0] = new String(host);
    }
    
    
    public HashMap pingAll(Object[] args, Object[] port){
        PingPrinter printer = new PingPrinter();
        /*if (args.length < 1) {
            System.err.println("Usage: java Ping [port] host...");
            return;
        }
        int firstArg = 0;
         
        // If the first argument is a string of digits then we take that
        // to be the port number to use
        if (Pattern.matches("[0-9]+", args[0])) {
            port = Integer.parseInt(args[0]);
            firstArg = 1;
        }*/
        
        try {
            // Create the threads and start them up
            printer.start();
            PingConnector connector = new PingConnector(printer);
            connector.start();
            
            // Create the targets and add them to the connector
            LinkedList targets = new LinkedList();
            for (int i = 0; i < args.length; i++) {
                PingTarget t = new PingTarget((String)args[i],  Integer.parseInt((String)port[i]));
                targets.add(t);
                connector.add(t);
            }
            
            // Wait for everything to finish
            Thread.sleep(2000);
            connector.shutdown();
            connector.join();
            
            // Print status of targets that have not yet been shown
            for (Iterator i = targets.iterator(); i.hasNext();) {
                PingTarget t = (PingTarget)i.next();
                if (!t.shown) {
                    hMap.put(t.getHost(), t.show());
                }
            }
            
        } catch (Exception e) {
            e.printStackTrace();
        }
        return printer.getPingResult();
    }
    
    public static void main(String str[]) {
        String strArr[] ={"www.yahoo.com", "172.26.200.237"};
        String port[] ={"80", "8080"};
        HashMap list = new Ping().pingAll(strArr, port);
        System.out.println (list.toString());
    }
    
}
