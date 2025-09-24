/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition.
 * 
 *
 * BorrwingDAO.java
 *
 * Created on Thu Jan 20 17:19:05 IST 2005
 */
package com.see.truetransact.serverside.supporting.depositperiodwisesetting;

import java.util.List;
import java.util.ArrayList;


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
import com.see.truetransact.serverside.common.idgenerate.IDGenerateDAO;
import com.see.truetransact.serverside.common.log.LogDAO;
import com.see.truetransact.transferobject.common.log.LogTO;
import org.apache.log4j.Logger;

import com.see.truetransact.transferobject.supporting.depositperiodwisesetting.DepositPeriodwiseSettingTO;

//import com.see.truetransact.transferobject.supporting.DepositPeriodwiseSetting.DepositAmountwiseSettingTO;
import java.util.HashMap;
import java.util.Date;
/**
 * TokenConfig DAO.
 *
 */
public class DepositsDAO extends TTDAO {

    private static SqlMap sqlMap = null;
    private DepositPeriodwiseSettingTO objTO;
    //  private DepositAmountwiseSettingTO objTOO;
    private String PANEL = "";
    private LogDAO logDAO;
    private LogTO logTO;
    private final static Logger log = Logger.getLogger(DepositsDAO.class);
    private Date currDt = null;
    /**
     * Creates a new instance of TokenConfigDAO
     */
    public DepositsDAO() throws ServiceLocatorException {
        ServiceLocator locate = ServiceLocator.getInstance();
        sqlMap = (SqlMap) locate.getDAOSqlMap();
    }

    private HashMap getData(HashMap map) throws Exception {
        System.out.println("@@@@@@@map" + map);
        HashMap returnMap = new HashMap();
        // if(objTO!=null)
        // {
        //  System.out.println("@@@@@@sssssssssss@map"+objTO);
        if (PANEL.equals("A")) {
            System.out.println("@@@@@@aaaaaaaaaa@map" + map);
            List list = (List) sqlMap.executeQueryForList("getSelectDepositPeriodwiseSettingTO", map);
            returnMap.put("DepositsTO", list);
        } else if (PANEL.equals("B")) {
            System.out.println("@@@@@@aaaaabbbbbbbbbbbbbaaaaa@map" + map);
            List list = (List) sqlMap.executeQueryForList("getSelectDepositAmountwiseSettingTO", map);
            returnMap.put("DepositsTO", list);
        } else if (PANEL.equals("C")) {
            System.out.println("@@@@@@mapmapmapmapmapmapmapmapmap@map" + map);
            List list = (List) sqlMap.executeQueryForList("getSelectLoanPeriodwiseSettingTO", map);
            returnMap.put("DepositsTO", list);
        } else if (PANEL.equals("D")) {
            System.out.println("@@@@@@aaaaabbbbbbbbbbbbbaaaaa@map" + map);
            List list = (List) sqlMap.executeQueryForList("getSelectLoanAmountwiseSettingTO", map);
            returnMap.put("DepositsTO", list);
        } else if (PANEL.equals("E")) {
            System.out.println("@@@@@@aaaaabbbbbbbbbbbbbaaaaa@map" + map);
            List list = (List) sqlMap.executeQueryForList("getSelectLoanODPeriodwiseSettingTO", map);
            returnMap.put("DepositsTO", list);
        } else if (PANEL.equals("F")) {
            System.out.println("@@@@@@aaaaabbbbbbbbbbbbbaaaaa@map" + map);
            List list = (List) sqlMap.executeQueryForList("getSelectBaddoubtfullSettingTO", map);
            returnMap.put("DepositsTO", list);
        } else if (PANEL.equals("G")) {
            System.out.println("@@@@@@aaaaabbbbbbbbbbbbbaaaaa@map" + map);
            List list = (List) sqlMap.executeQueryForList("getSelectFluidparameterTO", map);
            returnMap.put("DepositsTO", list);
        }
        //  }
        return returnMap;
    }

    private void insertData(HashMap map) throws Exception {
        if (objTO != null) {
            try {
                sqlMap.startTransaction();
                System.out.println("objTOobjTOobjTOobjTOobjTO" + objTO);
                if (PANEL.equals("A")) {
                    objTO.setid(getID());
                    System.out.println("objTO.getid()1" + objTO.getid());
                    sqlMap.executeUpdate("insertDepositPeriodwiseSettingTO", objTO);
                    System.out.println("objTO.getid()2" + objTO.getid());
                }
                if (PANEL.equals("B")) {
                    objTO.setid(getAmountID());
                    System.out.println("objTO.getid()3" + objTO.getid());
                    sqlMap.executeUpdate("insertDepositAmountwiseSettingTO", objTO);
                    System.out.println("objTO.getid()4" + objTO.getid());
                }
                if (PANEL.equals("C")) {
                    objTO.setid(getID());
                    System.out.println("objTO.getid()3" + objTO.getid());
                    sqlMap.executeUpdate("insertLoanPeriodwiseSettingTO", objTO);
                    System.out.println("objTO.getid()4" + objTO.getid());
                }
                if (PANEL.equals("D")) {
                    objTO.setid(getAmountID());
                    System.out.println("objTO.getid()3" + objTO.getid());
                    sqlMap.executeUpdate("insertLoanAmountwiseSettingTO", objTO);
                    System.out.println("objTO.getid()4" + objTO.getid());
                }
                if (PANEL.equals("E")) {
                    objTO.setid(getAmountID());
                    System.out.println("objTO.getid()3" + objTO.getid());
                    sqlMap.executeUpdate("insertLoanODPeriodwiseSettingTO", objTO);
                    System.out.println("objTO.getid()4" + objTO.getid());
                }
                if (PANEL.equals("F")) {
                    objTO.setid(getAmountID());
                    System.out.println("objTO.getid()3" + objTO.getid());
                    sqlMap.executeUpdate("insertBaddoubtfullSettingTO", objTO);
                    System.out.println("objTO.getid()4" + objTO.getid());
                }
                if (PANEL.equals("G")) {
                    objTO.setid(getAmountID());
                    System.out.println("objTO.getid()3" + objTO.getid());
                    sqlMap.executeUpdate("insertFluidparameterTO", objTO);
                    System.out.println("objTO.getid()4" + objTO.getid());
                }
                sqlMap.commitTransaction();
            } catch (Exception e) {
                sqlMap.rollbackTransaction();
                log.error(e);
                throw new TransRollbackException(e);
            }
            map = null;
        }


    }

    private String getID() throws Exception {
        final IDGenerateDAO dao = new IDGenerateDAO();
        final HashMap where = new HashMap();
        System.out.println("EEEEEEEEEEEEEEEEEEEEEEE");
        where.put(CommonConstants.MAP_WHERE, "ID");

        return (String) (dao.executeQuery(where)).get(CommonConstants.DATA);
    }

    private String getAmountID() throws Exception {
        final IDGenerateDAO dao = new IDGenerateDAO();
        final HashMap where = new HashMap();
        where.put(CommonConstants.MAP_WHERE, "ID");
        return (String) (dao.executeQuery(where)).get(CommonConstants.DATA);
    }

    private void updateData(HashMap map) throws Exception {
        if (objTO != null) {
            try {
                sqlMap.startTransaction();
                if (PANEL.equals("A")) {
                    sqlMap.executeUpdate("updateDepositPeriodwiseSettingTO", objTO);

                }
                if (PANEL.equals("B")) {
                    sqlMap.executeUpdate("updateDepositAmountwiseSettingTO", objTO);
                }
                if (PANEL.equals("C")) {
                    sqlMap.executeUpdate("updateLoanPeriodwiseSettingTO", objTO);
                }
                if (PANEL.equals("D")) {
                    sqlMap.executeUpdate("updateLoanAmountwiseSettingTO", objTO);
                }
                if (PANEL.equals("E")) {
                    sqlMap.executeUpdate("updateLoanODPeriodwiseSettingTO", objTO);
                }
                if (PANEL.equals("F")) {
                    sqlMap.executeUpdate("updateBaddoubtfullSettingTO", objTO);
                }
                if (PANEL.equals("G")) {
                    sqlMap.executeUpdate("updateFluidparameterTO", objTO);
                }
                sqlMap.commitTransaction();
            } catch (Exception e) {
                sqlMap.rollbackTransaction();
                log.error(e);
                throw new TransRollbackException(e);
            }
            map = null;
        }

    }

    private void deleteData() throws Exception {
        try {
            sqlMap.startTransaction();
            String Statusby = objTO.getStatusBy();
            objTO.setStatusDt(currDt);
            System.out.println("objTOobjTOobjTOobjTO" + objTO);
            sqlMap.executeUpdate("deleteDepositPeriodwiseSettingTO", objTO);
//            logTO.setData(objTO.toString());
//            logTO.setPrimaryKey(objTO.getKeyData());
//            logTO.setStatus(objTO.getStatus());
//            logDAO.addToLog(logTO);
            sqlMap.commitTransaction();
        } catch (Exception e) {
            sqlMap.rollbackTransaction();
            log.error(e);
            throw new TransRollbackException(e);
        }
    }

    public HashMap execute(HashMap map) throws Exception {
        System.out.println("@@@@@@@ExecuteMap" + map);
        HashMap returnMap = new HashMap();
        _branchCode = (String) map.get(CommonConstants.BRANCH_ID);
        currDt = ServerUtil.getCurrentDate(_branchCode);
        logDAO = new LogDAO();
        logTO = new LogTO();
        if (map.get("DepositPeriodwiseSetting") != null) {
            System.out.println("map -------------" + map.get("DepositPeriodwiseSetting"));
            objTO = (DepositPeriodwiseSettingTO) map.get("DepositPeriodwiseSetting");

            objTO.setBranCode(_branchCode);
            PANEL = "A";
        }
        if (map.get("DepositAmountwiseSetting") != null) {
            System.out.println("map -------------" + map.get("DepositAmountwiseSetting"));
            objTO = (DepositPeriodwiseSettingTO) map.get("DepositAmountwiseSetting");
            objTO.setBranCode(_branchCode);
            PANEL = "B";
        }
        if (map.get("LoanPeriodwiseSetting") != null) {
            System.out.println("map -------------" + map.get("LoanPeriodwiseSetting"));
            objTO = (DepositPeriodwiseSettingTO) map.get("LoanPeriodwiseSetting");
            objTO.setBranCode(_branchCode);
            PANEL = "C";
        }


        if (map.get("LoanAmountwiseSetting") != null) {
            System.out.println("map -------------" + map.get("LoanAmountwiseSetting"));
            objTO = (DepositPeriodwiseSettingTO) map.get("LoanAmountwiseSetting");
            objTO.setBranCode(_branchCode);
            PANEL = "D";
        }
        if (map.get("LoanODPeriodwiseSetting") != null) {
            System.out.println("map -------------" + map.get("LoanODPeriodwiseSetting"));
            objTO = (DepositPeriodwiseSettingTO) map.get("LoanODPeriodwiseSetting");
            objTO.setBranCode(_branchCode);
            PANEL = "E";
        }

        if (map.get("BadDoubtfullsetting") != null) {
            System.out.println("map -------------" + map.get("BadDoubtfullsetting"));
            objTO = (DepositPeriodwiseSettingTO) map.get("BadDoubtfullsetting");
            objTO.setBranCode(_branchCode);
            PANEL = "F";
        }

        if (map.get("Fluidparameter") != null) {
            System.out.println("map -------------" + map.get("BadDoubtfullsetting"));
            objTO = (DepositPeriodwiseSettingTO) map.get("Fluidparameter");
            objTO.setBranCode(_branchCode);
            PANEL = "G";
        }
        final String command = CommonUtil.convertObjToStr(map.get("COMMAND"));

        if (command.equals(CommonConstants.TOSTATUS_INSERT)) {
            insertData(map);
            System.out.println("objTO.getid()4444" + objTO.getid());
            if (objTO != null) {
                if (PANEL.equals("A")) {
                    // returnMap = null;
                    System.out.println("objTO.getid()4445554" + objTO.getid());
                    returnMap = new HashMap();
                    returnMap.put("ID", objTO.getid());
                }
                if (PANEL.equals("B")) {
                    System.out.println("objTO.getid()4488844" + objTO.getid());
                    returnMap = new HashMap();
                    returnMap.put("ID", objTO.getid());
                }
                if (PANEL.equals("C")) {
                    System.out.println("objTO.getid()4488844" + objTO.getid());
                    returnMap = new HashMap();
                    returnMap.put("ID", objTO.getid());
                }
                if (PANEL.equals("D")) {
                    System.out.println("objTO.getid()4488844" + objTO.getid());
                    returnMap = new HashMap();
                    returnMap.put("ID", objTO.getid());
                }
                if (PANEL.equals("E")) {
                    System.out.println("objTO.getid()4488844" + objTO.getid());
                    returnMap = new HashMap();
                    returnMap.put("ID", objTO.getid());
                }
                if (PANEL.equals("F")) {
                    System.out.println("objTO.getid()4488844" + objTO.getid());
                    returnMap = new HashMap();
                    returnMap.put("ID", objTO.getid());
                }

                if (PANEL.equals("G")) {
                    System.out.println("objTO.getid()4488844" + objTO.getid());
                    returnMap = new HashMap();
                    returnMap.put("ID", objTO.getid());
                }
            }



        } else if (command.equals(CommonConstants.TOSTATUS_UPDATE)) {
            updateData(map);
        } else if (command.equals(CommonConstants.TOSTATUS_DELETE)) {
            deleteData();

            // if (command.equals(CommonConstants.TOSTATUS_INSERT)){
            //      insertData(map);
            //      returnMap.put("ID",objTO.getid());
            //   }else if (command.equals(CommonConstants.TOSTATUS_UPDATE)){
            //      updateData(map);
            //   }else if (command.equals(CommonConstants.TOSTATUS_DELETE)){
            //       deleteData();
        } else if (map.containsKey(CommonConstants.AUTHORIZEMAP)) {
            HashMap authMap = (HashMap) map.get(CommonConstants.AUTHORIZEMAP);

        }
        map = null;
        destroyObjects();
        return returnMap;
    }

    public HashMap executeQuery(HashMap obj) throws Exception {
        _branchCode = (String) obj.get(CommonConstants.BRANCH_ID);
        currDt = ServerUtil.getCurrentDate(_branchCode);
        System.out.println("objjhgfjhfjhfjhfjfjfjf43545" + obj);
        return getData(obj);

    }

    private void destroyObjects() {
        objTO = null;
        logTO = null;
        logDAO = null;
    }
}
