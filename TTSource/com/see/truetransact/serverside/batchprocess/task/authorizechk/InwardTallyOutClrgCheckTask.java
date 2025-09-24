/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition. 
 * 
 * InwardTallyOutClrgCheckTask.java
 *
 * Created on July 9, 2004, 4:40 PM
 */
package com.see.truetransact.serverside.batchprocess.task.authorizechk;

import com.see.truetransact.serverside.batchprocess.task.Task;
import com.see.truetransact.serverside.batchprocess.task.TaskStatus;
import com.see.truetransact.serverside.batchprocess.task.TaskHeader;
import com.see.truetransact.serverside.batchprocess.BatchConstants;
import com.see.truetransact.commonutil.CommonConstants;
import com.see.truetransact.servicelocator.ServiceLocator;
import com.see.truetransact.commonutil.CommonUtil;
import com.see.truetransact.serverutil.ServerUtil;
import com.see.truetransact.commonutil.TTException;
import com.ibatis.db.sqlmap.SqlMap;
import java.util.HashMap;
import java.util.List;
import java.util.ArrayList;

/**
 *
 * @author Sunil (152691)
 */
public class InwardTallyOutClrgCheckTask extends Task {

    private static SqlMap sqlMap = null;
    private String branch = null;

    /**
     * Creates a new instance of InwardTallyOutClrgCheckTask
     */
    public InwardTallyOutClrgCheckTask(TaskHeader header) throws Exception {
        setHeader(header);
        branch = header.getBranchID();
        ServiceLocator locate = ServiceLocator.getInstance();
        sqlMap = (SqlMap) locate.getDAOSqlMap();
    }

    public TaskStatus executeTask() throws Exception {
        TaskStatus status = new TaskStatus();
        status.setStatus(BatchConstants.STARTED);
        HashMap paramMap = new HashMap();
        paramMap.put("TODAY_DT", ServerUtil.getCurrentDate(super._branchCode));
        paramMap.put("BRANCH_CODE", branch);

//        List output = sqlMap.executeQueryForList("getInwardTallyNotClosed", paramMap);


//        ArrayList output = new ArrayList();
//        List inwardTally = sqlMap.executeQueryForList("getInwardTally", paramMap);
//        List clearingTally = sqlMap.executeQueryForList("getClearingTally", paramMap);
//        HashMap inwardMap = null ;
//        HashMap clearingMap = null ;
//        boolean found = false ;
//        for(int inwardCount= 0,inwardTotalCount= inwardTally.size(); inwardCount< inwardTotalCount;inwardCount++){
//            inwardMap = (HashMap)inwardTally.get(inwardCount) ;
//            for(int clearingCount= 0,clearingTotalCount= clearingTally.size(); clearingCount< clearingTotalCount;clearingCount++){
//                clearingMap = (HashMap)clearingTally.get(clearingCount) ;
//                if(clearingMap.get("SCHEDULE_NO") == inwardMap.get("SCHEDULE_NO")){
//                    if(clearingMap.get("PHY_AMT") == inwardMap.get("PHY_AMT") &&
//                    clearingMap.get("PHY_COUNT") == inwardMap.get("PHY_COUNT")){
//                        clearingMap.put("FOUND", "true");
//                        inwardMap.put("FOUND", "true");
//                        
//                        break ;
//                    }
//                }
//                else{
//                    if(clearingMap.get("FOUND")== null)
//                        clearingMap.put("FOUND", "false");
//                    else if(!clearingMap.get("FOUND").equals("true"))
//                        clearingMap.put("FOUND", "false");
//                }
//            }
//            if(inwardMap.get("FOUND")== null)
//                inwardMap.put("FOUND", "false");
//            else if(!inwardMap.get("FOUND").equals("true"))
//                inwardMap.put("FOUND", "false");
//        }
//        
//        for(int inwardCount= 0,inwardTotalCount= inwardTally.size(); inwardCount< inwardTotalCount;inwardCount++){
//            if(((HashMap)inwardTally.get(inwardCount)).containsKey("FOUND"))
//                if(((HashMap)inwardTally.get(inwardCount)).get("FOUND").equals("false"))
//                    output.add(inwardTally.get(inwardCount));
//        }
//        
//        for(int clearingCount= 0,clearingTotalCount= clearingTally.size(); clearingCount< clearingTotalCount;clearingCount++){
//            if(((HashMap)clearingTally.get(clearingCount)).containsKey("FOUND"))
//                if(((HashMap)clearingTally.get(clearingCount)).get("FOUND").equals("false")){
//                    found = false ;
//                    for(int i =0, j = output.size();i < j; i++){
//                        if(((HashMap)clearingTally.get(clearingCount)).get("SCHEDULE_NO").equals( 
//                            ((HashMap)output.get(i)).get("SCHEDULE_NO")) && 
//                            ((HashMap)clearingTally.get(clearingCount)).get("CLEARING_TYPE").equals( 
//                            ((HashMap)output.get(i)).get("CLEARING_TYPE"))){
//                                System.out.println("in true ....");
//                                found = true ;
//                                break ;
//                        }
//                    }
//                }
//                    if(found == false)
//                        output.add(clearingTally.get(clearingCount));
//        }
//        
//        paramMap = null ;

        ArrayList output = new ArrayList();
        List inwardTally = sqlMap.executeQueryForList("getInwardTally", paramMap);
        List clearingTally = sqlMap.executeQueryForList("getClearingTally", paramMap);
        HashMap inwardMap = null;
        HashMap clearingMap = null;
        boolean found = false;
        for (int inwardCount = 0, inwardTotalCount = inwardTally.size(); inwardCount < inwardTotalCount; inwardCount++) {
            inwardMap = (HashMap) inwardTally.get(inwardCount);
            for (int clearingCount = 0, clearingTotalCount = clearingTally.size(); clearingCount < clearingTotalCount; clearingCount++) {

                clearingMap = (HashMap) clearingTally.get(clearingCount);
                //System.out.println("clearingMap " + inwardCount + " = " + clearingMap);
                if (clearingMap.get("SCHEDULE_NO").equals(inwardMap.get("SCHEDULE_NO"))
                        && clearingMap.get("CLEARING_TYPE").equals(inwardMap.get("CLEARING_TYPE"))) {
                    if (clearingMap.get("PHY_AMT").equals(inwardMap.get("PHY_AMT"))
                            && clearingMap.get("PHY_COUNT").equals(inwardMap.get("PHY_COUNT"))) {
                        clearingMap.put("FOUND", "true");
                        inwardMap.put("FOUND", "true");
                        break;
                    }
                } else {
                    if (clearingMap.get("FOUND") == null) {
                        clearingMap.put("FOUND", "false");
                    } else if (!clearingMap.get("FOUND").equals("true")) {
                        clearingMap.put("FOUND", "false");
                    }
                }

            }
            if (inwardMap.get("FOUND") == null) {
                inwardMap.put("FOUND", "false");
            } else if (!inwardMap.get("FOUND").equals("true")) {
                inwardMap.put("FOUND", "false");
            }
        }

        for (int inwardCount = 0, inwardTotalCount = inwardTally.size(); inwardCount < inwardTotalCount; inwardCount++) {
            if (((HashMap) inwardTally.get(inwardCount)).containsKey("FOUND")) {
                if (((HashMap) inwardTally.get(inwardCount)).get("FOUND").equals("false")) {
                    ((HashMap) inwardTally.get(inwardCount)).remove("FOUND");
                    output.add(inwardTally.get(inwardCount));
                }
            }
        }

        for (int clearingCount = 0, clearingTotalCount = clearingTally.size(); clearingCount < clearingTotalCount; clearingCount++) {
            if (((HashMap) clearingTally.get(clearingCount)).containsKey("FOUND")) {
                if (((HashMap) clearingTally.get(clearingCount)).get("FOUND").equals("false")) {
                    found = false;
                    for (int i = 0, j = output.size(); i < j; i++) {
                        if (((HashMap) clearingTally.get(clearingCount)).get("SCHEDULE_NO").equals(
                                ((HashMap) output.get(i)).get("SCHEDULE_NO"))
                                && ((HashMap) clearingTally.get(clearingCount)).get("CLEARING_TYPE").equals(
                                ((HashMap) output.get(i)).get("CLEARING_TYPE"))) {
                            found = true;
                            break;
                        }
                    }
                } else {
                    found = true;
                }
            }
            if (found == false) {
                ((HashMap) clearingTally.get(clearingCount)).remove("FOUND");
                output.add(clearingTally.get(clearingCount));
            }
        }

        if (output.size() > 0) {
            status.setStatus(BatchConstants.ERROR);
//            throw new TTException("Inward Tally and Inward Clearing does not tally");
        } else {
            status.setStatus(BatchConstants.COMPLETED);
        }

        return status;
    }

    public static void main(String args[]) {
        try {
            TaskHeader header = new TaskHeader();
            header.setTaskClass("InwardTallyOutClrgCheckTask");
            HashMap paramMap = new HashMap();
            header.setBranchID("BRAN");
            header.setTaskParam(paramMap);
            InwardTallyOutClrgCheckTask ft = new InwardTallyOutClrgCheckTask(header);
            ft.executeTask();
        } catch (Exception E) {
            E.printStackTrace();
        }
    }
}
