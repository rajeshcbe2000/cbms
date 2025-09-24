/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition.  
 * 
 * DirectorBoardHashMap.java
 */
package com.see.truetransact.ui.directorboardmeeting;
import java.util.HashMap;
import com.see.truetransact.uimandatory.UIMandatoryHashMap;
public class DirectorBoardHashMap implements UIMandatoryHashMap {
    private HashMap mandatoryMap;
    public DirectorBoardHashMap(){
        mandatoryMap = new HashMap();
        //mandatoryMap.put("cboBoardMember", new Boolean(true));
        mandatoryMap.put("tdtMeetingDate", new Boolean(true));
        mandatoryMap.put("cboBoardMember", new Boolean(true));
        mandatoryMap.put("txtSittingFeeAmount", new Boolean(true));
        mandatoryMap.put("tdtPaidDate", new Boolean(true));
        
        

    }
    public HashMap getMandatoryHashMap(){
        return this.mandatoryMap;
    }
}
