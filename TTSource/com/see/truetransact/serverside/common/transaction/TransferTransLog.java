/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition.  
 * 
 * TransferTransLog.java
 *
 * Created on January 7, 2005, 12:34 PM
 */
package com.see.truetransact.serverside.common.transaction;

import java.util.ArrayList;
import java.util.HashMap;

import java.util.Date;
import com.see.truetransact.transferobject.common.transaction.FailureTxTransferTO;
import com.see.truetransact.commonutil.CommonUtil;
import com.see.truetransact.servicelocator.ServiceLocator;
import com.ibatis.db.sqlmap.SqlMap;

/**
 *
 * @author 152691
 */
public class TransferTransLog {

    private static SqlMap sqlMap = null;
    public static String BATCH_TYPE = "BATCH_TYPE";
    public static String FAILURE_REMARK = "FAILURE_REMARK";

    /**
     * Creates a new instance of TransferTransLog
     */
    public TransferTransLog(SqlMap sqlMap) throws Exception {
        this.sqlMap = sqlMap;
    }

    public void writeErrorBatch(ArrayList errorList, HashMap map, String logId) throws Exception {
        System.out.println("In writeErrorBatch() .......... ............. ...... ");
        int listSize = errorList.size();
        for (int i = 0; i < listSize; i++) {
            System.out.println(errorList.get(i).getClass().getName());
            FailureTxTransferTO objFailureTxTransferTO = (FailureTxTransferTO) errorList.get(i);
            objFailureTxTransferTO.setTransLogId(logId);
            objFailureTxTransferTO.setBatchType(CommonUtil.convertObjToStr(map.get(BATCH_TYPE)));
            objFailureTxTransferTO.setFailureRemarks(CommonUtil.convertObjToStr(map.get(FAILURE_REMARK)));
            sqlMap.executeUpdate("insertTxTransferLogTO", objFailureTxTransferTO);
        }
    }
}
