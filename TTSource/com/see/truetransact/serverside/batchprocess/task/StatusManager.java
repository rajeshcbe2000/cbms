/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition. 
 * 
 * StatusManager.java
 *
 * Created on May 18, 2004, 12:26 PM
 */
package com.see.truetransact.serverside.batchprocess.task;

import java.util.Observable;

/**
 *
 * @author bala
 */
public class StatusManager extends Observable {

    private static StatusManager mgr;
    private boolean stopped = false;

    static {
        mgr = new StatusManager();
    }

    /**
     * Creates a new instance of StatusManager
     */
    private StatusManager() {
    }

    public static StatusManager getInstance() {
        return mgr;
    }

    /**
     * Getter for property stop.
     *
     * @return Value of property stop.
     */
    public synchronized boolean isStopped() {
        return stopped;
    }

    /**
     * Setter for property stop.
     *
     * @param stop New value of property stop.
     */
    public void setStopped(boolean stopped) {
        this.stopped = stopped;
        System.out.println("updated .. " + stopped);
        setChanged();
        notifyObservers();
    }
}
