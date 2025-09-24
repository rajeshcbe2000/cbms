/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved..
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition.
 * 
 *
 * CustomerHistoryDAO.java
 *
 * Created on Mar 18, 2005, 4:14 PM
 */
package com.see.truetransact.serverside.customer;

import com.ibatis.db.sqlmap.SqlMap;
import com.see.truetransact.serverutil.ServerUtil;
import com.see.truetransact.transferobject.customer.CustomerHistoryTO;
import com.see.truetransact.servicelocator.*;
import com.see.truetransact.serverexception.ServiceLocatorException;
import com.see.truetransact.commonutil.AcctStatusConstants;
import com.see.truetransact.commonutil.CommonUtil;
import com.see.truetransact.commonutil.CommonConstants;
import java.util.HashMap;

/**
 * This is used for Customer History Data Access.
 *
 * @author Balachandar
 */
public class CustomerHistoryDAO {

    private static SqlMap sqlMap;

    static {
        try {
            ServiceLocator locate = ServiceLocator.getInstance();
            sqlMap = (SqlMap) locate.getDAOSqlMap();
        } catch (ServiceLocatorException se) {
            se.printStackTrace();
        }
    }

    public static void insertToHistory(CustomerHistoryTO objCustomerHistoryTO) throws Exception {
        try
        {
        String command = CommonUtil.convertObjToStr(objCustomerHistoryTO.getCommand());
        if (command.equals(CommonConstants.TOSTATUS_INSERT) || command.equals("")) {
            objCustomerHistoryTO.setStatus(CommonConstants.STATUS_CREATED);
            sqlMap.executeUpdate("insertCustomerHistoryTO", objCustomerHistoryTO);

            HashMap map = new HashMap();
            if (objCustomerHistoryTO.getRelationship().equals(AcctStatusConstants.ACCT_HOLDER)) {
                if (objCustomerHistoryTO.getProductType().equals("SH")) {
                    map.put("CUST_TYPE", AcctStatusConstants.MEMBER);
                } else {
                    map.put("CUST_TYPE", AcctStatusConstants.CUSTOMER);
                }
                map.put("CUST_ID", objCustomerHistoryTO.getCustId());
                sqlMap.executeUpdate("updateCustomerBasedOnHistoryTO", map);
            } else {
                map.put("CUST_TYPE", AcctStatusConstants.OTHERS);
                map.put("CUST_ID", objCustomerHistoryTO.getCustId());
                sqlMap.executeUpdate("updateCustomerBasedOnHistoryTO", map);
            }
        } else {
            if (command.equals(CommonConstants.TOSTATUS_UPDATE)) {
                objCustomerHistoryTO.setStatus(CommonConstants.STATUS_MODIFIED);
            } else {
                objCustomerHistoryTO.setStatus(CommonConstants.STATUS_DELETED);
            }
            sqlMap.executeUpdate("updateCustomerHistoryTO", objCustomerHistoryTO);
        }
    }
    catch(Exception e)
    {
    sqlMap.rollbackTransaction();
    e.printStackTrace();
    throw  e;
    }
    }
   
}
