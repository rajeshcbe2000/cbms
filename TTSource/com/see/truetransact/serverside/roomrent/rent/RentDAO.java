/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition.
 **
 *
 * BorrwingDAO.java
 *
 * Created on Thu Jan 20 17:19:05 IST 2005
 */
package com.see.truetransact.serverside.roomrent.rent;

import java.util.*;
import java.util.ArrayList;
import java.text.SimpleDateFormat;
import java.text.DateFormat;
import com.ibatis.db.sqlmap.SqlMap;
import com.see.truetransact.serverutil.ServerUtil;

import com.see.truetransact.commonutil.CommonConstants;
import com.see.truetransact.commonutil.NoCommandException;
import com.see.truetransact.commonutil.CommonUtil;
import com.see.truetransact.serverside.TTDAO;
import com.see.truetransact.serverutil.ServerConstants;
import com.see.truetransact.servicelocator.ServiceLocator;
import com.see.truetransact.serverexception.TransRollbackException;
import com.see.truetransact.serverexception.ServiceLocatorException;
//import com.see.truetransact.serverside.common.idgenerate.IDGenerateDAO;
//import com.see.truetransact.serverside.common.log.LogDAO;
//import com.see.truetransact.transferobject.common.log.LogTO;

import com.see.truetransact.transferobject.roomrent.rent.RentTO;
import java.util.HashMap;

/**
 * TokenConfig DAO.
 *
 */
public class RentDAO extends TTDAO {

    private static SqlMap sqlMap = null;
    private RentTO objTO;

    //  private LogDAO logDAO;
    // private LogTO logTO;
    /**
     * Creates a new instance of TokenConfigDAO
     */
    public RentDAO() throws ServiceLocatorException {
        ServiceLocator locate = ServiceLocator.getInstance();
        sqlMap = (SqlMap) locate.getDAOSqlMap();
    }

    private HashMap getData(HashMap map) throws Exception {
        HashMap returnMap = new HashMap();
        HashMap where = (HashMap) map.get(CommonConstants.MAP_WHERE);

        //System.out.println("list1 INOPPPPPPPPPPPPPPPPPP ==============="+where);
        List list = (List) sqlMap.executeQueryForList("Rent.getSelectRent", where);
        System.out.println("list INOPPPPPPPPPPPPPPPPPP ===============" + list);
        List list1 = (List) sqlMap.executeQueryForList("Rent.getSelectRentDetails", where);
        System.out.println("list1 INOPPPPPPPPPPPPPPPPPP ===============" + list1);
        returnMap.put("RentTO", list);
        returnMap.put("list", list1);
        return returnMap;
    }

    private String getRMId() throws Exception {
        //  final IDGenerateDAO dao = new IDGenerateDAO();
        final HashMap where = new HashMap();
        where.put(CommonConstants.MAP_WHERE, "RMNUMBER");
        // return "";
        HashMap map = generateID("RMNUMBER");
        //System.out.println("MAP IN DAOOOOOO=========="+map);
        return (String) map.get(CommonConstants.DATA);
        //  return (String) (dao.executeQuery(where)).get(CommonConstants.DATA);
    }

    private String getRDetailsId() throws Exception {
        //  final IDGenerateDAO dao = new IDGenerateDAO();
        final HashMap where = new HashMap();
        where.put(CommonConstants.MAP_WHERE, "RENT_DETAILS_ID");
        // return "";
        HashMap map = generateID("RENT_DETAILS_ID");
        //System.out.println("MAP IN DAOOOOOO=========="+map);
        return (String) map.get(CommonConstants.DATA);
        //  return (String) (dao.executeQuery(where)).get(CommonConstants.DATA);
    }

    public HashMap generateID(String key) {

        HashMap hash = null, result = null;
        try {
            String mapName = "getCurrentID";
            HashMap where = new HashMap();
            where.put("ID_KEY", key); //Here u have to pass BORROW_ID or something else
            List list = null;

            //sqlMap.startTransaction();
            sqlMap.executeUpdate("updateIDGenerated", where);  // This update statement just updates curr_value=curr_value+1
            list = (List) sqlMap.executeQueryForList(mapName, where);  // This will get u the updated curr_value, prefix and length
            //sqlMap.commitTransaction();

            if (list.size() > 0) {
                hash = (HashMap) list.get(0);
                String strPrefix = "", strLen = "";

                // Prefix for the ID.
                if (hash.containsKey("PREFIX")) {
                    strPrefix = (String) hash.get("PREFIX");
                    if (strPrefix == null || strPrefix.trim().length() == 0) {
                        strPrefix = "";
                    }
                }

                // Maximum Length for the ID
                int len = 10;
                if (hash.containsKey("ID_LENGTH")) {
                    strLen = String.valueOf(hash.get("ID_LENGTH"));
                    if (strLen == null || strLen.trim().length() == 0) {
                        len = 10;
                    } else {
                        len = Integer.parseInt(strLen.trim());
                    }
                }

                int numFrom = strPrefix.trim().length();

                String newID = String.valueOf(hash.get("CURR_VALUE"));

                // Number Part of the String and incrementing 1 (ex. only 00085 from OGGOT00085)
                result = new HashMap();
                String genID = strPrefix.toUpperCase() + CommonUtil.lpad(newID, len - numFrom, '0');
                result.put(CommonConstants.DATA, genID);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    public Date getDateValue(String date1) {
        DateFormat formatter;
        Date date = null;
        try {
            System.out.println("date1 66666666666=========:" + date1);
            // String str_date=date1;
            //  formatter = new SimpleDateFormat("MM/dd/yyyy");
            //  date = (Date)formatter.parse(str_date);  
            //      System.out.println("dateAFETRRR 66666666666=========:"+date); 




            SimpleDateFormat sdf1 = new SimpleDateFormat("dd/MM/yyyy");
            SimpleDateFormat sdf2 = new SimpleDateFormat("MM/dd/yyyy");
            // String s1 = "2008-03-30T15:30:00.000+02:00";
            // date1 = date1.substring(0, date1.indexOf('.'));
            System.out.println("Result==> " + sdf2.format(sdf1.parse(date1)));
            date = new Date(sdf2.format(sdf1.parse(date1)));
            System.out.println("date IOOOOOOO==> " + date);
        } catch (Exception e) {
            System.out.println("Error in getDateValue():" + e);
        }
        return date;
    }

    private void insertData() throws Exception {
        try {
            sqlMap.startTransaction();

            objTO.setRmNumber(getRMId());
            objTO.setStatus(CommonConstants.STATUS_CREATED);
            //  logTO.setData(objTO.toString());
            //  logTO.setPrimaryKey(objTO.getKeyData());
            //   logTO.setStatus(objTO.getCommand());
            sqlMap.executeUpdate("insertRentTO", objTO);

            Vector dataV = objTO.getDataV();
            if (dataV != null) {
                for (int i = 0; i < dataV.size(); i++) {
                    //String []data=dataV.get(i);
                    Vector data = (Vector) dataV.get(i);
                    System.out.println("dataV.get(i)" + data.get(0));
                    String roomNo = data.get(0).toString();
                    String EffDate = data.get(1).toString();
                    String rentAmt = data.get(2).toString();
                    String rentFrq = data.get(3).toString();
                    String penelRate = data.get(4).toString();
                    String versionNo = objTO.getRmNumber() + "" + roomNo;
                    objTO.setRentDetailsId(getRDetailsId());
                    objTO.setRoomNo(roomNo);
                    objTO.setVersNo(versionNo);
                    objTO.setEffDate(getDateValue(EffDate));
                    objTO.setRentAmt(Double.valueOf(rentAmt));
                    objTO.setRentFeq(rentFrq);
                    objTO.setPenelRate(Double.valueOf(penelRate));
                    sqlMap.executeUpdate("insertRentDetailsTO", objTO);
                }


            }
            //  logDAO.addToLog(logTO);
            sqlMap.commitTransaction();
        } catch (Exception e) {
            sqlMap.rollbackTransaction();
            e.printStackTrace();
            throw new TransRollbackException(e);
        }
    }

    private void updateData() throws Exception {
        try {
            sqlMap.startTransaction();
            objTO.setStatus(CommonConstants.STATUS_MODIFIED);
            //logTO.setData(objTO.toString());
            //  logTO.setPrimaryKey(objTO.getKeyData());
            // logTO.setStatus(objTO.getCommand());
            sqlMap.executeUpdate("updateRentTO", objTO);

            //For details insertion
            Vector dataV = objTO.getDataV();
            if (dataV != null) {
                for (int i = 0; i < dataV.size(); i++) {
                    //String []data=dataV.get(i);
                    String rdId = "";
                    Vector data = (Vector) dataV.get(i);
                    System.out.println("dataV.get(i)" + data.get(0));
                    String roomNo = data.get(0).toString();
                    String EffDate = data.get(1).toString();
                    String rentAmt = data.get(2).toString();
                    String rentFrq = data.get(3).toString();
                    String penelRate = data.get(4).toString();
                    rdId = data.get(5).toString();
                    System.out.println("RDID INMMMMMMMMMMMMMMMMMMMM==========================" + rdId);
                    if (rdId == null || rdId.equals("")) {
                        System.out.println("RDID ((99999999999999999999999999999999999==========================" + getRDetailsId());
                        // objTO.setRentDetailsId(getRDetailsId());
                    } else {
                        objTO.setRentDetailsId(rdId);
                    }
                    String versionNo = objTO.getRmNumber() + "" + roomNo;
                    objTO.setRentDetailsId(rdId);
                    objTO.setRoomNo(roomNo);
                    objTO.setVersNo(versionNo);
                    objTO.setEffDate(getDateValue(EffDate));
                    if (rentAmt != null) {
                        rentAmt = rentAmt.replaceAll(",", "");
                    }
                    objTO.setRentAmt(Double.valueOf(rentAmt));
                    objTO.setRentFeq(rentFrq);
                    objTO.setPenelRate(Double.valueOf(penelRate));
                    if (rdId == null || rdId.equals("")) {
                        objTO.setStatus(CommonConstants.STATUS_CREATED);
                        objTO.setRentDetailsId(getRDetailsId());
                        System.out.println("RDID 7777777777777777777777777777777=================" + objTO.getRentDetailsId());
                        sqlMap.executeUpdate("insertRentDetailsTO", objTO);
                    } else {
                        sqlMap.executeUpdate("updateRentDetailsTO", objTO);
                    }
                }


            }
            //
            //  logDAO.addToLog(logTO);
            sqlMap.commitTransaction();
        } catch (Exception e) {
            sqlMap.rollbackTransaction();
            e.printStackTrace();
            throw new TransRollbackException(e);
        }
    }

    private void deleteData() throws Exception {
        try {
            sqlMap.startTransaction();
            objTO.setStatus(CommonConstants.STATUS_DELETED);
            //logTO.setData(objTO.toString());
            // logTO.setPrimaryKey(objTO.getKeyData());
            // logTO.setStatus(objTO.getCommand());
            sqlMap.executeUpdate("deleteRentTO", objTO);
            //   logDAO.addToLog(logTO);
            sqlMap.commitTransaction();
        } catch (Exception e) {
            sqlMap.rollbackTransaction();
            e.printStackTrace();
            throw new TransRollbackException(e);
        }
    }

    public static void main(String str[]) {
        try {
            RentDAO dao = new RentDAO();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public HashMap execute(HashMap map) throws Exception {
        _branchCode = (String) map.get(CommonConstants.BRANCH_ID);
        objTO = (RentTO) map.get("RentTO");
        final String command = objTO.getCommand();
        HashMap returnMap = null;
        ////  logDAO = new LogDAO();
        //  logTO = new LogTO();

        // logTO.setUserId(CommonUtil.convertObjToStr(map.get(CommonConstants.USER_ID)));
        // logTO.setBranchId(CommonUtil.convertObjToStr(map.get(CommonConstants.BRANCH_ID)));
        // logTO.setIpAddr(CommonUtil.convertObjToStr(map.get(CommonConstants.IP_ADDR)));
        // logTO.setModule(CommonUtil.convertObjToStr(map.get(CommonConstants.MODULE)));
        // logTO.setScreen(CommonUtil.convertObjToStr(map.get(CommonConstants.SCREEN)));

        if (command.equals(CommonConstants.TOSTATUS_INSERT)) {
            insertData();
            returnMap = new HashMap();
            returnMap.put("RMNUMBER", objTO.getRmNumber());
        } else if (command.equals(CommonConstants.TOSTATUS_UPDATE)) {
            updateData();
        } else if (command.equals(CommonConstants.TOSTATUS_DELETE)) {
            deleteData();
        } else {
            throw new NoCommandException();
        }

        destroyObjects();
        return returnMap;
    }

    public HashMap executeQuery(HashMap obj) throws Exception {
        _branchCode = (String) obj.get(CommonConstants.BRANCH_ID);
        return getData(obj);
    }

    private void destroyObjects() {
        objTO = null;
    }
}
