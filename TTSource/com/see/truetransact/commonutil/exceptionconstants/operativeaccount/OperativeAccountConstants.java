/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition. 
 * 
 * operativeaccountConstants.java
 *
 * Created on May 19, 2004, 12:20 PM
 */

package com.see.truetransact.commonutil.exceptionconstants.operativeaccount;

/**
 *
 * @author  rahul
 */
public interface OperativeAccountConstants {
    
    public final String CHEQUENOTISSUED = "CHEQUENOTISSUED";
    public final String CHEQUECLEARED = "CHEQUECLEARED";
    public final String CHEQUECLEARED_NA = "CHEQUECLEARED_NA";
    public final String CHEQUESTOPPED = "CHEQUESTOPPED";
    public final String CHEQUESTOPPED_NA = "CHEQUESTOPPED_NA";
    public final String CHEQUELOOSELEAF = "CHEQUELOOSELEAF";
    public final String CHEQUEREVOK_NA = "CHEQUEREVOK_NA";
    public final String FREEZE = "FREEZE";
    public final String EXCEPTION2 = "EXCEPTION2";
    public final String EXCEPTION3 = "EXCEPTION3";
    public final String LIEN = "LIEN";
    
    // Cheque Books Danger Level rule uses this constants
    public final String BOOKS_DANGER_LEVEL = "BDL";
    
    // Cheque Books Avaiable Level rule uses this constants
    public final String BOOKS_AVAIL_LEVEL = "BAL";
}
