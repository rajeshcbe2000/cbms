/* Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition.
 * 
 *
 * ThreadPool.java
 *
 * Created on May 25, 2004, 12:56 PM
 */

package com.see.truetransact.ui.batchprocess;

import java.util.HashMap;
import java.util.ArrayList;

import com.see.truetransact.serverside.batchprocess.task.TaskHeader;
import com.see.truetransact.serverside.batchprocess.task.TaskStatus;

/**
 *
 * @author  Dr. Harvinder Singh
 */

public class ThreadPool extends Thread {
    private PooledThread[] pt = new PooledThread[2];
    private ListnerObject listObj;
    private HashMap threadHashMap = new HashMap(pt.length);
    private String foundKey = "";
    private ArrayList alpt =null;
    private ArrayList alkey = null;
    private ArrayList taskArray = null;
    private ProcessOB dayOB = null;
    
    private int currPos = 0;
    
    /** Creates a new instance of ThreadPool */
    public ThreadPool(ProcessOB dayOB){
        this.dayOB = dayOB;
    }
    
    
    /** Creates a new instance of ThreadPool */
    public ThreadPool(){
        
    }
    
    /*
     * Populates all the Available TaskHeader
     * in a ArrayList.
     */
    public void setTaskList(ArrayList list) {
        this.taskArray = list;
    }
    
    private void setTaskArray() {
        taskArray = new ArrayList();
        
        TaskHeader header = new TaskHeader();
        header.setTaskClass("NewToOperativeTask");
        taskArray.add(header);
        
        header = new TaskHeader();
        header.setTaskClass("OperativeToInOperativeTask");
        taskArray.add(header);
        
        header = new TaskHeader();
        header.setTaskClass("InOperativeToDormantTask");
        taskArray.add(header);
    }
    
    public static void main(String args[]) {
        ThreadPool tp = new ThreadPool();
        
        ArrayList lst = new ArrayList();
        
        TaskHeader header = new TaskHeader();
        header.setTaskClass("NewToOperativeTask");
        lst.add(header);
        
        tp.setTaskList(lst);
        
        tp.initPooledThread();
        tp.start();
    }
    
    /**
     * Thread's run method which checks the availablePool and
     * starts the PooledThread Object
     */
    public void run() {
        while (true) {
            alpt = new ArrayList();
            alkey = new ArrayList();
            int n = getFromPool(alpt, alkey);
            if ((n > 0) && (taskArray.size() > currPos)) {
                System.out.println(" Thread started " + n);
                for (int i=0; i < n; i++) {
                    if (taskArray.size() > currPos) {
                        
                        pt[i] = (PooledThread ) alpt.get(i);
                        String strkey = (String) alkey.get(i);
                        
                        pt[i] = new PooledThread(strkey, (TaskHeader) taskArray.get(currPos++));
                        new Thread(pt[i]).start();
                    }
                }
                alpt.clear();
                alkey.clear();
                alpt = null;
                alkey = null;
            }
            try {
                sleep(50);
            } catch  (InterruptedException e) { }
        }
    }
    
    /**
     * init Method which fills PooledThread Array
     * and adding the ListnerObject for that.
     */
    public void initPooledThread() {
        //setTaskArray();
        String str;
       // System.out.println("pt====="+pt+"pt.length"+pt.length);
        for (int i=0; i < pt.length; i++) {
            str = "" + i;
           // System.out.println("taskArray=== "+taskArray+" taskArray.size() "+taskArray.size()+" currPos==="+currPos);
            if (taskArray.size() > currPos) {
                pt[i]= new PooledThread(str, (TaskHeader) taskArray.get(currPos++));
              //  System.out.println("pt[i]========="+pt[i]);
                threadHashMap.put(str, pt[i]);
            }
        }
        
        listObj = new ListnerObject(pt,this, "test this");
        listObj.start();
        int size = threadHashMap.size();
        
        for (int i=0; i < size; i++) {
            str ="" + i;
            new Thread((PooledThread) threadHashMap.remove(str)).start();
        }
    }
    
    public void stopAllThreads(){
        this.stop();
        listObj.stop();
    }
    /*
     * Gets the available threads
     */
    private synchronized int getFromPool(ArrayList alpt, ArrayList alkey) {
        String str = "";
        int size = threadHashMap.size();
        int i =0;
        PooledThread ptok = null;
        if (size > 0) {
            for (i =0; i < size; i++) {
                str = "" + i;
                if (threadHashMap.containsKey(str)){
                    ptok = (PooledThread) threadHashMap.remove(str);
                    alpt.add(ptok);
                    alkey.add(str);
                }
            }
            return (size - threadHashMap.size());
        }
        return 0;
    }
    
    /**
     * updatePool is used by ListnerObject
     */
    public  synchronized void  updatePool(int index, TaskHeader tskHeader, TaskStatus tskStatus) {
        String str = "" + index;
        threadHashMap.put(str, pt[index]);
        dayOB.setTaskStatus(tskStatus);
        dayOB.setTaskHeader(tskHeader);
        dayOB.ttNotifyObservers();
    }
}