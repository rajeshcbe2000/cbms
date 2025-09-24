/*Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition.
 * 
 *
 * DayBeginProcessOB.java
 *
 * Created on September 10, 2004, 3:57 PM
 */

package com.see.truetransact.ui.batchprocess;

import com.see.truetransact.serverside.batchprocess.task.TaskHeader;
import com.see.truetransact.serverside.batchprocess.task.TaskStatus;
import com.see.truetransact.uicomponent.CObservable;

/**
 *
 * @author  shanmuga
 */
public class DayBeginProcessOB extends CObservable{
    private TaskStatus tskStatus;
    private TaskHeader tskHeader;
    private int exeTaskCount;
    private static DayBeginProcessOB dayBeginProcessOB;
    /** Creates a new instance of DayBeginProcessOB */
    private DayBeginProcessOB() {
        tskStatus = new TaskStatus();
        tskHeader = new TaskHeader();
        exeTaskCount = 0;
    }
    
    static {
        try {
            dayBeginProcessOB = new DayBeginProcessOB();
        } catch(Exception e) {
            e.printStackTrace();
        }
    }
    
    public static DayBeginProcessOB getInstance(){
        return dayBeginProcessOB;
    }
    
    public void setTaskStatus(TaskStatus tskStatus){
        this.tskStatus = tskStatus;
        setChanged();
    }
    
    public TaskStatus getTaskStatus(){
        return this.tskStatus;
    }
      
    public void setTaskHeader(TaskHeader tskHeader){
        this.tskHeader = tskHeader;
        setChanged();
    }
    
    public TaskHeader getTaskHeader(){
        return this.tskHeader;
    }
     
    public void setExeTaskCount(int exeTaskCount){
        this.exeTaskCount = exeTaskCount;
        setChanged();
    }
    
    public int getExeTaskCount(){
        return this.exeTaskCount;
    }
    
    public void ttNotifyObservers(){
        try{
            this.notifyObservers();
        }catch(Exception e){
            System.out.println(e);
        }
    }
}
