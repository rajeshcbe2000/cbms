/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition.
 * 
 *
 * DepositLienDAO.java
 *
 * Created on Wed May 26 10:59:57 GMT+05:30 2004
 */
package com.see.truetransact.serverside.deposit.lien;

import com.ibatis.db.sqlmap.SqlMap;
import com.see.truetransact.serverutil.ServerUtil;
import com.see.truetransact.commonutil.CommonConstants;
import com.see.truetransact.commonutil.NoCommandException;
import com.see.truetransact.serverexception.TransRollbackException;
import com.see.truetransact.serverside.TTDAO;
import com.see.truetransact.serverexception.ServiceLocatorException;
import com.see.truetransact.serverside.common.idgenerate.IDGenerateDAO;
import com.see.truetransact.servicelocator.ServiceLocator;
import com.see.truetransact.transferobject.deposit.lien.DepositLienTO;
import com.see.truetransact.commonutil.TTException;
import com.see.truetransact.commonutil.CommonUtil;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Date;

/**
 * DepositLien DAO.
 *
 * @author Pinky
 *
 */
public class DepositLienDAO extends TTDAO {

    private static SqlMap sqlMap = null;
    private DepositLienTO objTO;
    private ArrayList lienTOs;
    private boolean callFromOtherDAO = false;
    private String lienNo = "";
    HashMap resultMap = new HashMap();
    Date currDt = null;

    /**
     * Creates a new instance of DepositLienDAO
     */
    public DepositLienDAO() throws ServiceLocatorException {
        ServiceLocator locate = ServiceLocator.getInstance();
        sqlMap = (SqlMap) locate.getDAOSqlMap();
    }

    private HashMap getData(HashMap map) throws Exception {
        HashMap returnMap = new HashMap();
        String mapStr = (String) map.get(CommonConstants.MAP_NAME);
        String where = (String) map.get(CommonConstants.MAP_WHERE);
        map = null;
        List list = (List) sqlMap.executeQueryForList(mapStr, where);
        returnMap.put(CommonConstants.MAP_NAME, list);
        ServiceLocator.flushCache(sqlMap);
        System.gc();
        return returnMap;
    }

    private void insertData() throws Exception {
        try {
            if (objTO.getLienNo() != null && objTO.getLienNo().length() > 0 && !objTO.getChkLos().equals("Y")) {
                sqlMap.executeUpdate("insertDepositLienTO", objTO);
            } else if (objTO.getLienNo() != null && objTO.getLienNo().length() > 0 && objTO.getChkLos().equals("Y")) {
                sqlMap.executeUpdate("insertLOSDepositLienTO", objTO);
            }
            //            sqlMap.executeUpdate("updateDepositLienTmpFileTO", objTO); //this one line added renewal purpose
        } catch (Exception e) {
            sqlMap.rollbackTransaction();
            e.printStackTrace();
            throw e;
        }
    }

    private void updateData(HashMap map) throws Exception {
        try {
            System.out.println("########updateData :" + map);

            int size = lienTOs.size();
            //            System.out.println("checkforvalue"+ isCallFromOtherDAO());
            if (!isCallFromOtherDAO()) {
                sqlMap.startTransaction();
            }
            System.out.println("########updateData :" + map);
            ArrayList lst = new ArrayList();
            for (int i = 0; i < size; i++) {
                objTO = (DepositLienTO) lienTOs.get(i);
                String chkVal = CommonUtil.convertObjToStr(map.get("CHK_VAL"));
                objTO.setChkLos(chkVal);
                objTO.setLienDt(setProperDtFormat(objTO.getLienDt()));
                System.out.println("lienno" + objTO.getLienNo());
                if (objTO.getLienNo().compareToIgnoreCase("-") == 0) {
                    objTO.setLienNo(this.getLienNo());
                    lienNo = objTO.getLienNo();
                    insertData();
                    lst.add(objTO.getLienNo());
                    resultMap.put("LIENNO", lst);
                } else if (objTO.getChkLos().equals("Y")) {
                    sqlMap.executeUpdate("updateDepositLosLienTO", objTO);
                } else {
                    sqlMap.executeUpdate("updateDepositLienTO", objTO);
                }
            }
            updateShadowLien(map);
            //                System.out.println("checkforvalue1"+ isCallFromOtherDAO());
            if (!isCallFromOtherDAO()) {
                sqlMap.commitTransaction();
            }
        } catch (Exception e) {
            sqlMap.rollbackTransaction();
            e.printStackTrace();
            throw new TransRollbackException(e);
        }
    }

    private Date setProperDtFormat(Date dt) {
        Date tempDt = (Date) currDt.clone();
        if (dt != null) {
            tempDt.setDate(dt.getDate());
            tempDt.setMonth(dt.getMonth());
            tempDt.setYear(dt.getYear());
            return tempDt;
        }
        return null;
    }

    private void updateShadowLien(HashMap map) throws Exception {
        try {
            objTO = (DepositLienTO) lienTOs.get(0);
            HashMap where = new HashMap();
            where.put("DEPOSITNO", objTO.getDepositNo());
            where.put("SUBDEPOSITNO", objTO.getDepositSubNo());
            where.put("SHADOWLIEN", map.get("SHADOWLIEN"));
            if (map.containsKey("MULTIPLE_LIEN_CLOSING") && map.get("MULTIPLE_LIEN_CLOSING") != null) {
                where.put("MULTIPLE_LIEN_CLOSING", "MULTIPLE_LIEN_CLOSING");//closing multiple lien
            }
            sqlMap.executeUpdate("updateShadowLien", where);
            where = null;
        } catch (Exception e) {
            sqlMap.rollbackTransaction();
            e.printStackTrace();
            throw new TransRollbackException(e);
        }
    }
    /*
     * private void deleteData() throws Exception { try {
     * sqlMap.executeUpdate("",objTO); } catch (Exception e) {
     * sqlMap.rollbackTransaction(); e.printStackTrace(); throw new
     * TransRollbackException(e); }
    }
     */

    private void updateDeleteStatusData(HashMap map) throws Exception {
        try {
            if (!isCallFromOtherDAO()) {
                sqlMap.startTransaction();
            }
            objTO = (DepositLienTO) lienTOs.get(0);
            objTO.setLienDt(setProperDtFormat(objTO.getLienDt()));
            sqlMap.executeUpdate("updateDeleteLienTO", objTO);
            updateShadowLien(map);
            if (!isCallFromOtherDAO()) {
                sqlMap.commitTransaction();
            }
        } catch (Exception e) {
            sqlMap.rollbackTransaction();
            e.printStackTrace();
            throw new TransRollbackException(e);
        }
    }

    private void updateAuthorizeData(HashMap map) throws Exception {
        try {
            System.out.println("########updateAuthorizeData :" + map);
            double availableBal = 0.0;
            double depositAmt = 0.0;
            String behavesLike = "";
            boolean balanceUpdate = true;
            String loanClosing = "";
            if (map.containsKey("LOAN_CLOSING") && map.get("LOAN_CLOSING") != null) {
                loanClosing = (String) map.get("LOAN_CLOSING");
            }
            String action = (String) map.get("ACTION");
            String status = (String) map.get("COMMAND_STATUS");
            map.put("TODAY_DT", currDt);
            map.put("SHADOWLIEN", new Double(CommonUtil.convertObjToDouble(map.get("SHADOWLIEN")).doubleValue()));
            if (!isCallFromOtherDAO()) {
                sqlMap.startTransaction();
            }
            String lineStatus = "";
            if (map.containsKey("STATUS") && map.get("STATUS") != null && map.get("STATUS").equals("UNLIENED")) {
                lineStatus = (String) map.get("STATUS");
            }
            if (lineStatus.equals("UNLIENED") && action.equals("AUTHORIZED")) {
                //Changed By Kannan AR if more than one loan available for one deposit, status should not update as created when other loans available
                //sqlMap.executeUpdate("updateSubAcInfoAvlBal", map);
                HashMap actMap = new HashMap();
                actMap.put("DEPOSIT_NO",map.get("DEPOSIT_ACT_NUM"));
                List actList = sqlMap.executeQueryForList("getActDetailsForDailyPrdct", actMap);
                if(actList != null && actList.size() > 0){
                    actMap = (HashMap)actList.get(0);
                    behavesLike = CommonUtil.convertObjToStr(actMap.get("BEHAVES_LIKE"));
                    if(behavesLike.equals("FIXED") || behavesLike.equals("CUMMULATIVE")){
                        if(actMap.containsKey("AVAILABLE_BALANCE") && actMap.get("AVAILABLE_BALANCE") != null){
                            availableBal = CommonUtil.convertObjToDouble(actMap.get("AVAILABLE_BALANCE"));
                            depositAmt = CommonUtil.convertObjToDouble(actMap.get("DEPOSIT_AMT"));
                            double lienAmt = CommonUtil.convertObjToDouble(map.get("LIENAMOUNT"));
                            if(availableBal + lienAmt > depositAmt){
                                balanceUpdate = false;
                                map.put("SUBNO", CommonUtil.convertObjToInt(map.get("SUBNO")));
                                System.out.println("@##@#@#@#@#@# Postgres changes 1 :"+map);
                                sqlMap.executeUpdate("updateAvlBalAsDePositAmount", map);
                            }
                        }
                    }
                }
                System.out.println("balanceUpdate :: " + balanceUpdate);
                if(balanceUpdate){
                map.put("SUBNO", CommonUtil.convertObjToInt(map.get("SUBNO")));
                System.out.println("#@#@#@#@#@#@# Postgres changes : "+map);
                sqlMap.executeUpdate("updateSubAcInfoOnlyAvlBal", map);
                }
            }
            if (!lineStatus.equals("UNLIENED")) {
                if (action.compareToIgnoreCase(CommonConstants.STATUS_EXCEPTION) != 0) {
                    map.put("SUBNO", CommonUtil.convertObjToInt(map.get("SUBNO")));
                    sqlMap.executeUpdate("updateSubAcInfoBal", map);
                }
            }
            sqlMap.executeUpdate("authorizeLienTO", map);
            //(action.compareToIgnoreCase(CommonConstants.STATUS_REJECTED)==0 &&
            //            lineStatus.compareToIgnoreCase(CommonConstants.STATUS_UNLIEN)==0)

            if ((action.compareToIgnoreCase(CommonConstants.STATUS_AUTHORIZED) == 0
                    && lineStatus.compareToIgnoreCase(CommonConstants.STATUS_CREATED) == 0)
                    || (action.compareToIgnoreCase(CommonConstants.STATUS_REJECTED) == 0
                    && lineStatus.compareToIgnoreCase(CommonConstants.STATUS_CREATED) == 0) && loanClosing.equals("LOAN_CLOSING")) {
                sqlMap.executeUpdate("rejectUnLienUpdate", map);
                sqlMap.executeUpdate("updateSubACInfoStatusToLien", map);
            }
            if ((action.compareToIgnoreCase(CommonConstants.STATUS_AUTHORIZED) == 0 && (!loanClosing.equals("LOAN_CLOSING")))) {
                sqlMap.executeUpdate("updateSubACInfoStatusToLien", map);
            }
            if ((action.compareToIgnoreCase(CommonConstants.STATUS_AUTHORIZED) == 0
                    && lineStatus.compareToIgnoreCase(CommonConstants.STATUS_UNLIEN) == 0)) {
                sqlMap.executeUpdate("updateSubACInfoStatusToUnlien", map);

            }
            //            if(action.compareToIgnoreCase(CommonConstants.STATUS_AUTHORIZED)==0 && status.compareToIgnoreCase(CommonConstants.STATUS_UNLIEN)==0)
            //                sqlMap.executeUpdate("updateSubACInfoStatusToUnlien", map);
            //            sqlMap.executeUpdate("updateSubACInfoStatusToLien", map);
            if (!isCallFromOtherDAO()) {
                sqlMap.commitTransaction();
            }
            loanClosing = null;
        } catch (Exception e) {
            sqlMap.rollbackTransaction();
            e.printStackTrace();
            throw new TransRollbackException(e);
        }
    }

    private String getLienNo() throws Exception {
        IDGenerateDAO dao = new IDGenerateDAO();
        HashMap where = new HashMap();
        where.put(CommonConstants.MAP_WHERE, "DEPOSIT_LIEN_NO");
        String lienNo = (String) (dao.executeQuery(where)).get(CommonConstants.DATA);
        dao = null;
        where = null;
        return lienNo;
    }

    public static void main(String str[]) {
        try {
            DepositLienDAO dao = new DepositLienDAO();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public HashMap execute(HashMap map) throws Exception {
        System.out.println("depositlien execute###" + map);
        _branchCode = (String) map.get(CommonConstants.BRANCH_ID);
        currDt = ServerUtil.getCurrentDate(_branchCode);
        lienTOs = (ArrayList) map.get("lienTOs");
        final String command = (String) map.get("COMMAND");
        if (command.equals(CommonConstants.AUTHORIZEDATA)) {
            updateAuthorizeData(map);
        } else if (lienTOs != null && lienTOs.size() > 0) {
            if (command.equals(CommonConstants.TOSTATUS_INSERT)) {
                updateData(map);
            } else if (command.equals(CommonConstants.TOSTATUS_UPDATE)) {
                updateData(map);
            } else if (command.equals(CommonConstants.TOSTATUS_DELETE)) {
                updateDeleteStatusData(map);
            } else {
                throw new NoCommandException();
            }
        }

        map = null;
        destroyObjects();
        //        if (lienNo.length()>0) {
        //            HashMap returnMap = new HashMap();
        //            returnMap.put("LIENNO",lienNo);
        //            return returnMap;
        //        }
        ServiceLocator.flushCache(sqlMap);
        System.gc();
        return resultMap;
    }

    public HashMap executeQuery(HashMap obj) throws Exception {
        _branchCode = (String) obj.get(CommonConstants.BRANCH_ID);
        currDt = ServerUtil.getCurrentDate(_branchCode);
        return getData(obj);
    }

    private void destroyObjects() {
        objTO = null;
        lienTOs = null;
    }

    /**
     * Getter for property callFromOtherDAO.
     *
     * @return Value of property callFromOtherDAO.
     */
    public boolean isCallFromOtherDAO() {
        return callFromOtherDAO;
    }

    /**
     * Setter for property callFromOtherDAO.
     *
     * @param callFromOtherDAO New value of property callFromOtherDAO.
     */
    public void setCallFromOtherDAO(boolean callFromOtherDAO) {
        this.callFromOtherDAO = callFromOtherDAO;
    }
}
