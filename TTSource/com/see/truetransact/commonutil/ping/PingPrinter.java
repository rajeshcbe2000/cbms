/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition. 
 * 
 * PingPrinter.java
 *
 * Created on January 23, 2004, 4:28 PM
 */

package com.see.truetransact.commonutil.ping;

import java.util.LinkedList;
import java.util.HashMap;

// Thread for printing targets as they're heard from
//
public class PingPrinter extends Thread {
    HashMap hMap = new HashMap();
    LinkedList pending = new LinkedList();
    
    public PingPrinter() {
        setName("Printer");
        setDaemon(true);
    }
    
    void add(PingTarget t) {
        synchronized (pending) {
            pending.add(t);
            pending.notify();
        }
    }
    
    public void run() {
        try {
            for (;;) {
                PingTarget t = null;
                synchronized (pending) {
                    while (pending.size() == 0)
                        pending.wait();
                    t = (PingTarget)pending.removeFirst();
                }
                hMap.put(t.getHost(), t.show());
            }
        } catch (InterruptedException x) {
            return;
        }
    }
    
    public HashMap getPingResult() {
        return hMap;
    }
}