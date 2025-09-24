/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition.
 **
 *
 * LookUpDAO.java
 *
 * Created on August 18, 2003, 4:19 PM
 */
package com.see.truetransact.serverside.common.lookup;

import com.ibatis.db.sqlmap.SqlMap;
import com.see.truetransact.serverutil.ServerUtil;

import com.see.truetransact.transferobject.common.lookup.LookUpTO;
import com.see.truetransact.servicelocator.*;
import com.see.truetransact.serverside.TTDAO;
import com.see.truetransact.commonutil.CommonConstants;


import com.see.truetransact.commonutil.TTException;
import com.see.truetransact.serverexception.ServiceLocatorException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import java.rmi.RemoteException;
import java.util.Map;

/**
 *
 * @author Administrator
 */
public class LookUpDAO extends TTDAO {

    private ArrayList lookupKey;
    private HashMap keyValue;
    private HashMap resultMap;
    private final String MAPNAMEVALUE = "getLookUp"; // Variable to store value to lookup from xml map file
    private String queryparam;
    private List list;
    private ArrayList key;
    private ArrayList value;
    private SqlMap sqlMap = null;

    /**
     * Creates a new instance of LookUpDAO
     */
    public LookUpDAO() throws ServiceLocatorException {
        ServiceLocator locate = ServiceLocator.getInstance();
        sqlMap = (SqlMap) locate.getDAOSqlMap();
        resultMap = new HashMap();
    }

    public HashMap execute(HashMap obj) throws Exception {
        _branchCode = (String) obj.get(CommonConstants.BRANCH_ID);
        return null;
    }

    public HashMap executeQuery(HashMap queryMap) throws Exception {
        _branchCode = (String) queryMap.get(CommonConstants.BRANCH_ID);
        HashMap result;
        /*
         * If its from lookup_master, mapname can be null
         * If its from other table, it will have
         *  map name which is given newly in xml lookup maping file
         */
        if (queryMap.get(CommonConstants.MAP_NAME) != null) {
            result = getSingleLookUp(queryMap);
        } else {
            result = getMultipleLookUp(queryMap);
        }
        return result;
    }

    private HashMap getSingleLookUp(HashMap queryMap) throws Exception {
        if (queryMap.get(CommonConstants.PARAMFORQUERY) instanceof HashMap) {
            list = (List) sqlMap.executeQueryForList((String) queryMap.get(CommonConstants.MAP_NAME), (HashMap) queryMap.get(CommonConstants.PARAMFORQUERY));
        } else {
            list = (List) sqlMap.executeQueryForList((String) queryMap.get(CommonConstants.MAP_NAME), (String) queryMap.get(CommonConstants.PARAMFORQUERY));
        }
        Map tempMap = new HashMap();
        tempMap.put(CommonConstants.DATA, getMap(list));
        makeNull();
        return (HashMap) tempMap;
    }

    private HashMap getMultipleLookUp(HashMap queryMap) throws Exception {
        lookupKey = (ArrayList) queryMap.get(CommonConstants.PARAMFORQUERY); //ArrayList of lookupkeys
//        System.out.println("lookupKey.size() " + lookupKey.size());
        Map tempMap = new HashMap();
        for (int x = lookupKey.size(), y = 0; x > 0; x--, y++) {
//            System.out.println("lookupKey value " + lookupKey.get(y));
            queryparam = (String) lookupKey.get(y);
            if (!resultMap.containsKey(queryparam)) {
                list = (List) sqlMap.executeQueryForList(MAPNAMEVALUE, queryparam);
                tempMap.put(queryparam, getMap(list));
                resultMap.putAll(tempMap);
            } else {
                tempMap.put(queryparam, resultMap.get(queryparam));
            }
            makeNull();
        }
//        System.out.println("tempMap inside lookupdao " + tempMap);
//        System.out.println("resultMap inside lookupdao " + resultMap);
        lookupKey = null;
        return (HashMap) tempMap;
    }

    private HashMap getMap(List list) throws Exception {
        key = new ArrayList();
        value = new ArrayList();

        //The first values in the ArrayList key and value are empty String to display the
        //first row of all dropdowns to be empty String
        key.add("");
        value.add("");
        for (int i = 0, j = list.size(); i < j; i++) {
            key.add(((LookUpTO) list.get(i)).getLookUpRefID());
            value.add(((LookUpTO) list.get(i)).getLookUpDesc());
        }
        keyValue = new HashMap();
        keyValue.put("KEY", key);
        keyValue.put("VALUE", value);
        return keyValue;
    }

    private void makeNull() {
        list = null;
        key = null;
        value = null;
        keyValue = null;
    }

    public static void main(String args[]) throws Exception {
        HashMap param = new HashMap();
        ArrayList lookupKey = new ArrayList();
        HashMap lookupValues;
        HashMap keyValue;
        ArrayList key;
        ArrayList value;


        //for multiple lookup
        param.put(CommonConstants.MAP_NAME, null);

        lookupKey.add("CUSTOMER.TITLE");
        lookupKey.add("CUSTOMER.RESIDENTIALSTATUS");
        param.put(CommonConstants.PARAMFORQUERY, lookupKey);

        lookupValues = new LookUpDAO().executeQuery(param);
        keyValue = (HashMap) lookupValues.get("CUSTOMER.TITLE");
        key = (ArrayList) keyValue.get("KEY");
        value = (ArrayList) keyValue.get("VALUE");
        for (int i = 0; i < key.size(); i++) {
            System.out.println(key.get(i) + " - " + value.get(i));
        }

        keyValue = (HashMap) lookupValues.get("CUSTOMER.RESIDENTIALSTATUS");
        key = (ArrayList) keyValue.get("KEY");
        value = (ArrayList) keyValue.get("VALUE");
        for (int i = 0; i < key.size(); i++) {
            System.out.println(key.get(i) + " - " + value.get(i));
        }

        //for single lookup
        param.put("mapname", "getAccProducts");
        param.put("paramforquery", null);
        lookupValues = new LookUpDAO().executeQuery(param);
        keyValue = (HashMap) lookupValues.get("DATA");
        key = (ArrayList) keyValue.get("KEY");
        value = (ArrayList) keyValue.get("VALUE");
        for (int i = 0; i < key.size(); i++) {
            System.out.println(key.get(i) + " - " + value.get(i));
        }
    }
}
