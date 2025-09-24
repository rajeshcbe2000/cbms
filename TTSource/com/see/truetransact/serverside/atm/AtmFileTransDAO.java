/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition.  
 * 
 * AtmFileTransDAO.java
 */

package com.see.truetransact.serverside.atm;

import com.ibatis.db.sqlmap.SqlMap;
import com.see.servicelocator.ServiceLocatorException;
import com.see.truetransact.commonutil.CommonConstants;
import com.see.truetransact.serverside.TTDAO;
import com.see.truetransact.servicelocator.ServiceLocator;
import com.see.truetransact.commonutil.CommonUtil;
import java.util.HashMap;
import com.see.truetransact.serverside.atm.ReadFilesFromFolder;

/**
 *
 * @author Sathiya
 */
public class AtmFileTransDAO extends TTDAO {

    private static SqlMap sqlMap = null;
    private String _branch;

    public AtmFileTransDAO() throws com.see.truetransact.serverexception.ServiceLocatorException {
        ServiceLocator locate = ServiceLocator.getInstance();
        sqlMap = (SqlMap) locate.getDAOSqlMap();
    }

    @Override
    public HashMap execute(HashMap obj) throws Exception {
        _branch = CommonUtil.convertObjToStr(obj.get("BRANCH_CODE"));
        getInwardTransactionFile();
        return null;
    }

    @Override
    public HashMap executeQuery(HashMap obj) throws Exception {
        return null;
    }

    private void getInwardTransactionFile() throws Exception {
        String path = "";
        ReadFilesFromFolder readFile = null;

        // FILE CAME FROM SPONSOR BANK
        path = CommonUtil.convertObjToStr(CommonConstants.ATM_OUR_INWARD_TRANSACTION);//"E:/KKC/Inward/ATM/TRANSACTION";
        readFile = new ReadFilesFromFolder("TRANSACTION", path);
        readFile.listFilesForFolder(readFile.folder, sqlMap, _branch);   //add branch code

        path = CommonUtil.convertObjToStr(CommonConstants.ATM_OUR_INWARD_TOPUP_ACK);//"E:/KKC/Inward/ATM/Topup_Ack";
        readFile = new ReadFilesFromFolder("TOPUP_DEBIT_TOPUP_ACK", path);
        readFile.listFilesForFolder(readFile.folder, sqlMap, "TOPUP");   //add branch code

        path = CommonUtil.convertObjToStr(CommonConstants.ATM_OUR_INWARD_STOP_ACK);//"E:/KKC/Inward/ATM/Stop_Ack"; //need to develop for this
        readFile = new ReadFilesFromFolder("STOP_REVOKE", path);
        readFile.listFilesForFolder(readFile.folder, sqlMap, "8000");
    }
}
