/* Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition.
 * 
 *
 * ListnerObject.java
 *
 * Created on May 25, 2004, 12:56 PM
 */

package com.see.truetransact.ui.batchprocess;

/**
 * ListnerObject is used for watching PooledThread's status
 *
 * @author  Dr. Harvinder Singh
 */

public class ListnerObject extends Thread {
    private PooledThread[] threadObj;
    private ThreadPool threadPool;
    int check = 0;
    
    /**
     * ListnerObject's constructor.
     *
     * @param obj           PooledThread 
     * @param threadPool    ThreadPool
     * @param str           String
     */
    public ListnerObject(PooledThread[] obj, ThreadPool threadPool, String str) {
        threadObj = obj;
        this.threadPool = threadPool;
        System.out.println(" " + threadObj.length);
    }
    
    /**
     * Listens Thread Status and updates the Thread Pool
     */
    public void run() {
        while (true) {
            for(int i=0; i < threadObj.length; i++) {
                if (threadObj[i] != null && threadObj[i].getStatus()){
                    threadPool.updatePool(i, threadObj[i].getTaskHeader(), threadObj[i].getTaskStatus());
                }
            }
            try {
                sleep((int)(100));
            } catch  (InterruptedException e) { }
        }
    }
}