/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition. 
 * 
 * BatchConstants.java
 *
 * Created on May 18, 2004, 4:56 PM
 */
package com.see.truetransact.serverside.batchprocess;

/**
 *
 * @author bala
 */
public class BatchConstants {

    public static final int ACCEPTED = 1;
    public static final int REJECTED = 2;
    public static final int STARTED = 3;
    public static final int COMPLETED = 4;
    public static final int ERROR = 5;
    public static final int NO_DATA = 6;
    public static final String TASK_HEADER = "TASK_HEADER";

    /**
     * Creates a new instance of BatchConstants
     */
    private BatchConstants() {
    }
}
