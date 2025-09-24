/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition.
 * 
 *
 * AccountTransferDAO.java
 *
 * Created on September 25, 2003, 3:32 PM
 */
package com.see.truetransact.serverside.operativeaccount;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Date;
import java.util.ArrayList;

import com.see.truetransact.serverside.TTDAO;
import com.ibatis.db.sqlmap.SqlMap;
import com.see.truetransact.serverutil.ServerUtil;
import com.see.truetransact.servicelocator.*;
import com.see.truetransact.commonutil.CommonUtil;
import com.see.truetransact.serverexception.ServiceLocatorException;
import com.see.truetransact.commonutil.TTException;

/**
 *
 * @author amathan
 */
public class AccountTransferDAO extends TTDAO {

    static SqlMap _sqlMap;

    /**
     * Creates a new instance of AccountTransferDAO
     */
    public AccountTransferDAO() throws ServiceLocatorException {
        final ServiceLocator locate = ServiceLocator.getInstance();
        _sqlMap = (SqlMap) locate.getDAOSqlMap();
    }

    public HashMap execute(HashMap param) throws Exception {
        final ArrayList accNum = (ArrayList) param.get("ACT_NUMBER");
        final HashMap statusMap = (HashMap) param.get("ACT_STATUS");
        HashMap insMap = new HashMap();
        Date curDt = ServerUtil.getCurrentDate(CommonUtil.convertObjToStr(param.get("BRANCH_CODE")));

        final int accNumSize = accNum.size();
        _sqlMap.startTransaction();
        for (int i = 0; i < accNumSize; i++) {
            insMap = new HashMap();

            _sqlMap.executeUpdate("updateAccountTransMap", accNum.get(i));
            insMap.put("ACT_NUM", CommonUtil.convertObjToStr(accNum.get(i)));
            insMap.put("OLD_STATUS", CommonUtil.convertObjToStr(statusMap.get(accNum.get(i))));
            insMap.put("CREATED_BY", param.get("USER_ID"));
            insMap.put("CREATED_DT", curDt);
            _sqlMap.executeUpdate("InoperativeOrDormantToOperativeAccount", insMap);


        }
        _sqlMap.commitTransaction();
        insMap = new HashMap();
        return null;
    }

    public HashMap executeQuery(HashMap param) throws Exception {
        final HashMap returnMap = new HashMap();
        final LinkedHashMap accTransMap = new LinkedHashMap();
        final List list = (List) _sqlMap.executeQueryForList("getAccountTransMap", param.get(com.see.truetransact.commonutil.CommonConstants.MAP_WHERE));
        final int listSize = list.size();
        for (int i = 0; i < listSize; i++) {
            accTransMap.put(((HashMap) list.get(i)).get("Account Number"), list.get(i));
        }
        returnMap.put("AccountTransferDetails", accTransMap);
        return returnMap;
    }
}
