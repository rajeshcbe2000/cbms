/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition.  
 **
 *
 * ViewAllDAO.java
 *
 * Created on August 18, 2003, 4:19 PM
 */
package com.see.truetransact.serverside.common.viewall;

import com.ibatis.db.sqlmap.SqlMap;
import com.see.truetransact.serverutil.ServerUtil;

import com.see.truetransact.servicelocator.*;
import com.see.truetransact.serverside.TTDAO;
import com.see.truetransact.serverexception.ServiceLocatorException;
import com.see.truetransact.commonutil.TTException;
import com.see.truetransact.commonutil.CommonConstants;

import java.util.List;
import java.util.HashMap;
import java.util.ArrayList;


import org.apache.log4j.Logger;

/**
 *
 * @author Balachandar
 */
public class ViewAllDAO extends TTDAO {

    private static SqlMap sqlMap = null;

    private HashMap getData(HashMap map) throws Exception {
        String mapName = (String) map.get(CommonConstants.MAP_NAME);
        HashMap where = null;

        if (map.containsKey(CommonConstants.MAP_WHERE)) {
            where = (HashMap) map.get(CommonConstants.MAP_WHERE);
        }

        if (mapName == null || mapName.trim().length() == 0) {
            throw new TTException("Map Name is Null");
        }
        //throw new TTException("Map Name is Null");

        HashMap returnMap = null;

        try {
            List list = null;
            ArrayList tableHead = null;
            ArrayList tableData = null;

            list = (List) sqlMap.executeQueryForList(mapName, where);
            returnMap = new HashMap();
            tableData = new ArrayList();

            int lstSize = 0;
            // Getting Headings from HashMap and putting into ArrayList
            if (!list.isEmpty() && list.size() > 0) {
                tableHead = new ArrayList(((HashMap) list.get(0)).keySet());
                lstSize = list.size();
            }

          //  ArrayList tmp = null;
            // Getting Data and putting into ArrayList
              if (!where.containsKey("FILTERED_LIST") || lstSize<100) {
                 ArrayList tmp = null;
                // Getting Data and putting into ArrayList
                for (int i=0; i < lstSize; i++) {
                    tmp = new ArrayList(((HashMap)list.get(i)).values());
                    tableData.add(tmp);
                }
            }
            returnMap.put(CommonConstants.TABLEHEAD, tableHead);
            returnMap.put(CommonConstants.TABLEDATA, tableData);
        } catch (Exception e) {
            // If you get an error at this point, it matters little what it was. It is going to be
            // unrecoverable and we will want the app to blow up good so we are aware of the
            // problem. You should always log such errors and re-throw them in such a way that
            // you can be made immediately aware of the problem.
            e.printStackTrace();
            throw new RuntimeException("Error initializing SqlConfig class. Cause: " + e);
        }
        return returnMap;
    }

    /**
     * Creates a new instance of ViewAllDAO
     */
    public ViewAllDAO() throws ServiceLocatorException {
        ServiceLocator locate = ServiceLocator.getInstance();
        sqlMap = (SqlMap) locate.getDAOSqlMap();
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        try {
            ViewAllDAO dao = new ViewAllDAO();
            HashMap mapParam = new HashMap();

            HashMap where = new HashMap();
            where.put("beh", "CA");

            mapParam.put("MAPNAME", "getSelectOperativeAcctProductTOList");
            //mapParam.put("WHERE", where);
            HashMap rMap = dao.getData(mapParam);
            System.out.println(rMap.toString());
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public HashMap execute(HashMap obj) throws Exception {
        _branchCode = (String) obj.get(CommonConstants.BRANCH_ID);
        return null;
    }

    public HashMap executeQuery(HashMap obj) throws Exception {
        _branchCode = (String) obj.get(CommonConstants.BRANCH_ID);
        return getData(obj);
    }
}
