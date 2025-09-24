 /*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition.
 * 
 *
 * UserDAO.java
 *
 * Created on September 08, 2003, 1:00 PM
 *
 * Modified on April 16, 2004, 10:42 AM
 *
 */
package com.see.truetransact.serverside.deposit.autorenewal;

import com.see.truetransact.serverside.directoryboardsetting.*;
import com.ibatis.db.sqlmap.SqlMap;
import com.see.truetransact.serverutil.ServerUtil;
import com.see.truetransact.commonutil.CommonConstants;
import com.see.truetransact.commonutil.NoCommandException;
import com.see.truetransact.serverexception.TransRollbackException;
import com.see.truetransact.serverside.TTDAO;
import com.see.truetransact.serverexception.ServiceLocatorException;
import com.see.truetransact.servicelocator.ServiceLocator;
import com.see.truetransact.transferobject.sysadmin.emptransfer.EmpTransferTO;
import com.see.truetransact.serverside.common.idgenerate.IDGenerateDAO;
import com.see.truetransact.transferobject.directoryboardsetting.DirectoryBoardTO;
import com.see.truetransact.commonutil.CommonUtil;



import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.apache.log4j.Logger;
// For Maintaining Logs...
import com.see.truetransact.serverside.common.log.LogDAO;
import com.see.truetransact.transferobject.common.log.LogTO;
import java.util.Date;
/**
 * This is used for User Data Access.
 *
 * @author Nithya
 */
public class DepositAutoRenewalDAO extends TTDAO {

    private static SqlMap sqlMap = null;
    // For Maintaining Logs...
    private LogDAO logDAO;
    private LogTO logTO;
    private final static Logger log = Logger.getLogger(DepositAutoRenewalDAO.class);
    private Date currDt = null;
    /**
     * Creates a new instance of roleDAO
     */
    public DepositAutoRenewalDAO() throws ServiceLocatorException {
        ServiceLocator locate = ServiceLocator.getInstance();
        sqlMap = (SqlMap) locate.getDAOSqlMap();
    }


    public HashMap execute(HashMap map) throws Exception {
        System.out.println("@@@@@@@ExecuteMap" + map);
        List list = null;
        HashMap returnMap = new HashMap();
        _branchCode = (String) map.get(CommonConstants.BRANCH_ID);
        currDt = ServerUtil.getCurrentDate(_branchCode);
        try {
            if (map.containsKey("AUTO_RENEWAL_PROCESS") && map.get("AUTO_RENEWAL_PROCESS") != null) {
                list = sqlMap.executeQueryForList("processDepositAutoRenewal", map);
            }else if (map.containsKey("AUTO_RENEWAL_POST") && map.get("AUTO_RENEWAL_POST") != null) {
                list = sqlMap.executeQueryForList("postDepositAutoRenewal", map);
            }
            System.out.println("list :: " + list);
            if(list != null && list.size() > 0){
                returnMap = (HashMap)list.get(0);
            }
        } catch (Exception e) {
            e.printStackTrace();
            sqlMap.rollbackTransaction();
            throw e;
        }
        map = null;
        destroyObjects();
        return returnMap;
    }

    public HashMap executeQuery(HashMap obj) throws Exception {      
        return null;
    }

    private void destroyObjects() {
        logTO = null;
        logDAO = null;
    }
}
