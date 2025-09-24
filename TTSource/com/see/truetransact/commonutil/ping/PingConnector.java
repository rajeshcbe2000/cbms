/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition. 
 * 
 * Connector.java
 *
 * Created on January 23, 2004, 4:44 PM
 */

package com.see.truetransact.commonutil.ping;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.channels.SocketChannel;
import java.util.LinkedList;
import java.util.regex.Pattern;
import java.nio.channels.Selector;
import java.net.InetAddress;
import java.nio.channels.SelectionKey;
import java.util.Iterator;

// Thread for connecting to all targets in parallel via a single selector
//
public class PingConnector extends Thread {
    Selector sel;
    PingPrinter printer;
    
    // List of pending targets.  We use this list because if we try to
    // register a channel with the selector while the connector thread is
    // blocked in the selector then we will block.
    //
    LinkedList pending = new LinkedList();
    
    public PingConnector(PingPrinter pr) throws IOException {
        printer = pr;
        sel = Selector.open();
        setName("Connector");
    }
    
    // Initiate a connection sequence to the given target and add the
    // target to the pending-target list
    //
    void add(PingTarget t) {
        SocketChannel sc = null;
        try {
            
            // Open the channel, set it to non-blocking, initiate connect
            sc = SocketChannel.open();
            sc.configureBlocking(false);
            sc.connect(t.address);
            
            // Record the time we started
            t.channel = sc;
            t.connectStart = System.currentTimeMillis();
            
            // Add the new channel to the pending list
            synchronized (pending) {
                pending.add(t);
            }
            
            // Nudge the selector so that it will process the pending list
            sel.wakeup();
            
        } catch (Exception x) {
            if (sc != null) {
                try {
                    sc.close();
                } catch (IOException xx) { }
            }
            t.failure = x;
            printer.add(t);
        }
    }
    
    // Process any targets in the pending list
    //
    void processPendingTargets() throws IOException {
        synchronized (pending) {
            while (pending.size() > 0) {
                PingTarget t = (PingTarget)pending.removeFirst();
                try {
                    
                    // Register the channel with the selector, indicating
                    // interest in connection completion and attaching the
                    // target object so that we can get the target back
                    // after the key is added to the selector's
                    // selected-key set
                    t.channel.register(sel, SelectionKey.OP_CONNECT, t);
                    
                } catch (Exception x) {
                    
                    // Something went wrong, so close the channel and
                    // record the failure
                    t.channel.close();
                    t.failure = x;
                    printer.add(t);
                    
                }
                
            }
        }
    }
    
    // Process keys that have become selected
    //
    void processSelectedKeys() throws IOException {
        for (Iterator i = sel.selectedKeys().iterator(); i.hasNext();) {
            
            // Retrieve the next key and remove it from the set
            SelectionKey sk = (SelectionKey)i.next();
            i.remove();
            
            // Retrieve the target and the channel
            PingTarget t = (PingTarget)sk.attachment();
            SocketChannel sc = (SocketChannel)sk.channel();
            
            // Attempt to complete the connection sequence
            try {
                if (sc.finishConnect()) {
                    sk.cancel();
                    t.connectFinish = System.currentTimeMillis();
                    sc.close();
                    printer.add(t);
                }
            } catch (Exception x) {
                sc.close();
                t.failure = x;
                printer.add(t);
            }
        }
    }
    
    volatile boolean shutdown = false;
    
    // Invoked by the main thread when it's time to shut down
    //
    void shutdown() {
        shutdown = true;
        sel.wakeup();
    }
    
    // Connector loop
    //
    public void run() {
        for (;;) {
            try {
                int n = sel.select();
                if (n > 0)
                    processSelectedKeys();
                processPendingTargets();
                if (shutdown) {
                    sel.close();
                    return;
                }
            } catch (IOException x) {
                x.printStackTrace();
            }
        }
    }    
}