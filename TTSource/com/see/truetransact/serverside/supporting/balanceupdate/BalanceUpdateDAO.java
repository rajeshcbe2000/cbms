/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition.  
 * 
 * BalanceUpdateDAO.java
 */

package com.see.truetransact.serverside.supporting.balanceupdate;

import java.util.List;
import java.util.HashMap;
import java.util.ArrayList;
import com.ibatis.db.sqlmap.SqlMap;
import com.see.truetransact.commonutil.CommonConstants;
import com.see.truetransact.commonutil.CommonUtil;
import com.see.truetransact.commonutil.DateUtil;
import com.see.truetransact.serverside.TTDAO;
import com.see.truetransact.servicelocator.ServiceLocator;
import com.see.truetransact.serverexception.TransRollbackException;
import com.see.truetransact.serverexception.ServiceLocatorException;
import com.see.truetransact.serverside.common.transaction.TransactionDAO;
import com.see.truetransact.serverside.common.log.LogDAO;
import com.see.truetransact.serverutil.ServerUtil;
import com.see.truetransact.transferobject.common.log.LogTO;
import com.see.truetransact.transferobject.supporting.balanceupdate.BalanceUpdateTO;
import java.util.Date;

public class BalanceUpdateDAO extends TTDAO {

    private static SqlMap sqlMap = null;
    private LogTO logTO;
    private LogDAO logDAO;
    private BalanceUpdate objBalanceUpdateTO;
    private String userID = "";
    private String branchCode = "";
    private ArrayList selectedArrayList;
    private ArrayList deletedArrayList;
    private int totalCount = 0;
    private BalanceUpdateTO objTO;
    BalanceUpdateTO objTO1 = new BalanceUpdateTO();
    Date currDt = null;

    public BalanceUpdateDAO() throws ServiceLocatorException {
        ServiceLocator locate = ServiceLocator.getInstance();
        sqlMap = (SqlMap) locate.getDAOSqlMap();
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

    private String getBalSheetId() throws Exception {
        //  final IDGenerateDAO dao = new IDGenerateDAO();
        final HashMap where = new HashMap();
        where.put(CommonConstants.MAP_WHERE, "BAL_SHEET_ID");
        HashMap map = generateID();
        return (String) map.get(CommonConstants.DATA);
    }

    public HashMap generateID() {
        HashMap hash = null, result = null;
        try {
            String mapName = "getCurrentID";
            HashMap where = new HashMap();
            where.put("ID_KEY", "BAL_SHEET_ID"); //Here u have to pass BORROW_ID or something else
            List list = null;
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

    public Date getProperDateFormat(Object obj) {
        Date currDate = null;
        if (obj != null && obj.toString().length() > 0) {
            Date tempDt = DateUtil.getDateMMDDYYYY(CommonUtil.convertObjToStr(obj));
            currDate = (Date) currDt.clone();
            currDate.setDate(tempDt.getDate());
            currDate.setMonth(tempDt.getMonth());
            currDate.setYear(tempDt.getYear());
        }
        return currDate;
    }

    private void insertData(HashMap map) throws Exception {
        try {
            //    objTO.setStatus(CommonConstants.STATUS_CREATED);

            String balSheetId = getBalSheetId();
            if (map.containsKey("BALANCE_LIST")) {
                HashMap bal = (HashMap) map.get("BALANCE_LIST");
                System.out.println("bal Date --" + bal + "  sixe====" + bal.size() + "mapData ===" + map);
                Date startDate = null;
                HashMap dateMap = new HashMap();
                dateMap.put("DATE", DateUtil.getDateMMDDYYYY(map.get("DATE_BALANCE").toString()));
                List yearStartDate = sqlMap.executeQueryForList("getYearStartDate", dateMap);
                if (yearStartDate != null && yearStartDate.size() > 0) {
                    HashMap startDateMap = (HashMap) yearStartDate.get(0);
                    startDate = (Date) ((startDateMap.get("DATE_S")));
                }
                int tabIndex = 0;
                double netLoss = 0, netProfit = 0;
                if (map.containsKey("TAB_INDEX") && map.get("TAB_INDEX") != null) {
                    tabIndex = CommonUtil.convertObjToInt(map.get("TAB_INDEX"));
                }
                System.out.println("tabIndex 999 ---" + tabIndex);
                if (tabIndex == 1) {
                    if (map.containsKey("NET_LOSS") && map.get("NET_LOSS") != null) {
                        netLoss = CommonUtil.convertObjToDouble(map.get("NET_LOSS"));
                    }
                    if (map.containsKey("NET_PROFIT") && map.get("NET_PROFIT") != null) {
                        netProfit = CommonUtil.convertObjToDouble(map.get("NET_PROFIT"));
                    }

                }
                if (map.containsKey("DELETE_KEY") && map.get("DELETE_KEY") != null && map.get("DELETE_KEY").equals("Y")) {
                    HashMap delMap = new HashMap();
                    delMap.put("DATE", DateUtil.getDateMMDDYYYY(CommonUtil.convertObjToStr(map.get("DATE_BALANCE"))));
                    delMap.put("FINAL_ACC_TYPE", CommonUtil.convertObjToStr(map.get("FINAL_ACC_TYPE")));
                    String selBranchId = CommonUtil.convertObjToStr(map.get("SEL_BRANCH_ID"));
                    delMap.put("BRANCH_CODE", selBranchId);

                    if (tabIndex == 0) {
                        sqlMap.executeUpdate("deleteBalanceSheet", delMap);
                    }
                    if (tabIndex == 1) {
                        sqlMap.executeUpdate("deleteFinalBalanceSheet", delMap);
                    }
                }
                System.out.println("netLoss 999 ---" + netLoss + "netProfit ---" + netProfit);
                String finalAccType = "", branchCode = "", userId = "";
                for (int i = 0; i < bal.size(); i++) {
                    HashMap balanc = (HashMap) bal.get(i);
                    BalanceUpdateTO balanceTO = new BalanceUpdateTO();
                    double balAmt = CommonUtil.convertObjToDouble(balanc.get("BALANCE"));
                    if (balAmt > 0) {
                        balanceTO.setAccountHeadId(CommonUtil.convertObjToStr(balanc.get("ACC_HEADS")));
                        balanceTO.setFrmdate(startDate);//getProperDateFormat(DateUtil.getDateMMDDYYYY(startDate)));
                        balanceTO.setTodate(DateUtil.getDateMMDDYYYY(balanc.get("DATE").toString()));
                        balanceTO.setAcctHeadDesc(CommonUtil.convertObjToStr(balanc.get("DES")));
                        balanceTO.setAmount(CommonUtil.convertObjToDouble(balanc.get("BALANCE")));
                        balanceTO.setBalanceType(CommonUtil.convertObjToStr(balanc.get("TYPE")));
                        balanceTO.setFinalacttype(CommonUtil.convertObjToStr(balanc.get("FINAL_ACC_TYPE")));
                        finalAccType = CommonUtil.convertObjToStr(balanc.get("FINAL_ACC_TYPE"));
                        balanceTO.setBranchcode(CommonUtil.convertObjToStr(balanc.get("BRANCH_CODE")));
                        branchCode = CommonUtil.convertObjToStr(balanc.get("BRANCH_CODE"));
                        balanceTO.setStatus(CommonConstants.STATUS_CREATED);
                        balanceTO.setStatusDt(currDt);
                        balanceTO.setStatusBy(((String) balanc.get(CommonConstants.USER_ID)));
                        balanceTO.setEntryMode("SYSTEM");
                        userId = ((String) balanc.get(CommonConstants.USER_ID));
                        balanceTO.setBalSheetId(balSheetId);
                        if (tabIndex == 0) {
                            sqlMap.executeUpdate("insertBalanceUpdateTO", balanceTO);
                        } else if (tabIndex == 1) {
                            //aDDED by sreekrishnan
                            if (checkExistence(balanceTO)) {
                                sqlMap.executeUpdate("updateBalanceFinalUpdateTO", balanceTO);
                            } else {
                                if(CommonUtil.convertObjToStr(balanceTO.getActualAmount()).equals("")){
                                    balanceTO.setActualAmount(CommonUtil.convertObjToDouble(0.0));
                                }
                                sqlMap.executeUpdate("insertBalanceFinalUpdateTO", balanceTO);
                            }
                        }
                    }
                }
                //case of net profit & loss  //&& !finalAccType.equals("BALANCE SHEET")
                if (tabIndex == 1 && (netLoss > 0 || netProfit > 0) ) {
                    BalanceUpdateTO balanceTO = new BalanceUpdateTO();
                    balanceTO.setAccountHeadId("");
                    balanceTO.setFrmdate(startDate);//getProperDateFormat(DateUtil.getDateMMDDYYYY(startDate)));
                    balanceTO.setTodate(DateUtil.getDateMMDDYYYY(map.get("DATE_BALANCE").toString()));
                    System.out.println("netLoss 888 ---" + netLoss + "netProfit 888---" + netProfit);
                    if (netLoss > 0) {
                        if (!finalAccType.equals("") && finalAccType.equals("TRADING")) {
                            balanceTO.setAcctHeadDesc("GROSS LOSS");
                        } else {
                            balanceTO.setAcctHeadDesc("NET LOSS");
                        }
                        balanceTO.setAmount(CommonUtil.convertObjToDouble(netLoss));
                        balanceTO.setBalanceType("CREDIT");
                    }
                    if (netProfit > 0) {
                        if (!finalAccType.equals("") && finalAccType.equals("TRADING")) {
                            balanceTO.setAcctHeadDesc("GROSS PROFIT");
                        } else {
                            balanceTO.setAcctHeadDesc("NET PROFIT");
                        }
                        balanceTO.setAmount(CommonUtil.convertObjToDouble(netProfit));
                        balanceTO.setBalanceType("DEBIT");
                    }

                    balanceTO.setFinalacttype(finalAccType);
                    balanceTO.setBranchcode(branchCode);
                    balanceTO.setStatus(CommonConstants.STATUS_CREATED);
                    balanceTO.setStatusDt(currDt);
                    balanceTO.setStatusBy(userId);
                    balanceTO.setBalSheetId(balSheetId);
                    balanceTO.setEntryMode("SYSTEM");
                    if (checkNetExistence(balanceTO)) {
                        sqlMap.executeUpdate("updateNetBalanceFinalUpdateTO", balanceTO);
                    } else {
                        if (CommonUtil.convertObjToStr(balanceTO.getActualAmount()).equals("")) {
                            balanceTO.setActualAmount(CommonUtil.convertObjToDouble(0.0));
                        }
                        sqlMap.executeUpdate("insertBalanceFinalUpdateTO", balanceTO);
                    }
                }
                //aDDED by sreekrishnan for manual entry
                if (map.containsKey("MANUAL_DATA")) {
                    System.out.println("MANUAL_DATA#^#^#^#^#^#" + map.get("MANUAL_DATA"));
                    BalanceUpdateTO balanceTO = new BalanceUpdateTO();
                    HashMap dataMap = new HashMap();
                    dataMap = (HashMap) map.get("MANUAL_DATA");
                    System.out.println("dataMap^#^^#^#" + dataMap);
                    ArrayList addList = new ArrayList(dataMap.keySet());
                    if (addList != null && addList.size() > 0) {
                        for (int i = 0; i < addList.size(); i++) {
                            balanceTO = (BalanceUpdateTO) dataMap.get(addList.get(i));
                            System.out.println("balanceTO^#^^#^#" + balanceTO);
                            balanceTO.setAccountHeadId("");
                            balanceTO.setFrmdate(startDate);
                            balanceTO.setTodate(DateUtil.getDateMMDDYYYY(map.get("DATE_BALANCE").toString()));
                            balanceTO.setFinalacttype(finalAccType);
                            balanceTO.setBranchcode(branchCode);
                            balanceTO.setStatus(CommonConstants.STATUS_CREATED);
                            balanceTO.setStatusDt(currDt);
                            balanceTO.setStatusBy(userId);
                            balanceTO.setBalSheetId(balSheetId);
                            balanceTO.setEntryMode("MANUAL");
                            if (tabIndex == 0) {
                                sqlMap.executeUpdate("insertBalanceUpdateTO", balanceTO);
                            } else if (tabIndex == 1) {
                                //aDDED by sreekrishnan
                                if (checkManualDataExistence(balanceTO)) {
                                    sqlMap.executeUpdate("updateNetBalanceFinalUpdateTO", balanceTO);
                                } else {
                                    if (CommonUtil.convertObjToStr(balanceTO.getActualAmount()).equals("")) {
                                        balanceTO.setActualAmount(CommonUtil.convertObjToDouble(0.0));
                                    }
                                    sqlMap.executeUpdate("insertBalanceFinalUpdateTO", balanceTO);
                                }
                            }
                        }
                    }
                }
                //end
           /* HashMap balanc = (HashMap)bal.get(0);
                BalanceUpdateTO balance = (BalanceUpdateTO)map.get("COMMON_DETAILS");
                balance.setAccountHeadId(CommonUtil.convertObjToStr(balanc.get("ACC_HEADS")));
                balance.setTodate(balance.getFrmdate());
                balance.setAcctHeadDesc(CommonUtil.convertObjToStr(balanc.get("DES")));
                balance.setAmount(CommonUtil.convertObjToStr(balanc.get("BALANCE")));
                try{
                sqlMap.executeUpdate("insertBalanceUpdateTO", balance);
                System.out.println("Kiiiiiiiiii"+balanc);
                System.out.println("Kiiiiiiiiii");}
                catch(Exception ex){
                System.out.println("Exception  s"+ex);
                }*/
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw new TransRollbackException(e);
        }
    }

    public boolean checkExistence(BalanceUpdateTO balanceTO) {
        boolean exist = false;
        BalanceUpdateTO balanceDataTO = new BalanceUpdateTO();
        try {
            List list = (List) sqlMap.executeQueryForList("getSelectBalanceFinalData", balanceTO);
            if (list != null && list.size() > 0) {
                exist = true;
            } else {
                exist = false;
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return exist;
    }

    public boolean checkManualDataExistence(BalanceUpdateTO balanceTO) {
        boolean exist = false;
        BalanceUpdateTO balanceDataTO = new BalanceUpdateTO();
        try {
            List list = (List) sqlMap.executeQueryForList("getSelectBalanceManualFinalData", balanceTO);
            if (list != null && list.size() > 0) {
                exist = true;
            } else {
                exist = false;
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return exist;
    }
    
    public boolean checkNetExistence(BalanceUpdateTO balanceTO) {
        boolean exist = false;
        BalanceUpdateTO balanceDataTO = new BalanceUpdateTO();
        try {
            List list = (List) sqlMap.executeQueryForList("getSelectNetBalanceFinalData", balanceTO);
            if (list != null && list.size() > 0) {
                exist = true;
            } else {
                exist = false;
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return exist;
    }

    public static void main(String str[]) {
        try {
            BalanceUpdateDAO dao = new BalanceUpdateDAO();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public HashMap execute(HashMap map) throws Exception {
        totalCount = 0;

        HashMap returnMap = new HashMap();
        _branchCode = (String) map.get(CommonConstants.BRANCH_ID);
        currDt = ServerUtil.getCurrentDate(_branchCode);
        System.out.println("BalanceUpdateDAO Execute Method : " + map);

        objBalanceUpdateTO = (BalanceUpdate) map.get("getBalanceUpdateDetails");

        final String command = CommonUtil.convertObjToStr(map.get("COMMAND"));
        System.out.println("command -- " + command + "  UP===" + CommonConstants.TOSTATUS_INSERT);
        if (map.containsKey("BALANCEUPDATE")) {
            BalanceUpdateTO objTO1 = new BalanceUpdateTO();
            for (int i = 0; i < map.size(); i++) {
//            objTO1.setBranchcode();
//            objTO1.setFinalacttype(cboFinalActType);
//            objTO1.setFrmdate(tdtFromDate);
//            objTO1.setAccountHeadId(CommonUtil.convertObjToStr(tmbBalanceUpdate.getValueAt(i, 0)));
//            objTO1.setAcctHeadDesc(CommonUtil.convertObjToStr(tmbBalanceUpdate.getValueAt(i, 1)));
//            objTO1.setAmount(CommonUtil.convertObjToStr(tmbBalanceUpdate.getValueAt(i, 2)));
//            setCbobranch(objTO1.getBranchcode());
//            setTdtFromDate(objTO1.getFrmdate());
//            setCboAccountHead(objTO1.getSubacttype());
//            setCboFinalActType(objTO1.getFinalacttype());
//            setTdtFromDate(objTO1.getFrmdate());
                System.out.println("mapmapmapmapmap" + map);
                selectedArrayList = (ArrayList) map.get("insertBalanceUpdateTO");
                System.out.println("insertBalanceUpdate selectedArrayList : " + selectedArrayList);
            }
        }
        if (map.containsKey("updateBalanceUpdate")) {
            deletedArrayList = (ArrayList) map.get("updateBalanceUpdate");
            System.out.println("updateBalanceUpdate deletedArrayList : " + deletedArrayList);
        }
        if (command.equals(CommonConstants.TOSTATUS_INSERT)) {
            insertData(map);
        } else if (command.equals(CommonConstants.TOSTATUS_UPDATE)) {
            updateData();
        }
        returnMap.put("TOTAL_COUNT", totalCount);
        System.out.println("returnMap : " + returnMap);
        destroyObjects();
        return returnMap;
    }

    private void destroyObjects() {
        objBalanceUpdateTO = null;
    }

    @Override
    public HashMap executeQuery(HashMap obj) throws Exception {
        return getData(obj);
    }

    private void updateData() throws Exception {
        try {
            sqlMap.startTransaction();
            if (deletedArrayList != null && deletedArrayList.size() != 0) {
                for (int i = 0; i < deletedArrayList.size(); i++) {
                    BalanceUpdateTO objBalanceUpdateTo = (BalanceUpdateTO) deletedArrayList.get(i);
                    System.out.println("reached inside : " + objBalanceUpdateTo);
                    sqlMap.executeUpdate("updateShareAccountTO", objBalanceUpdateTo);
                    totalCount = i + 1;
                }
            }
            sqlMap.commitTransaction();
        } catch (Exception e) {
            sqlMap.rollbackTransaction();
            e.printStackTrace();
            throw new TransRollbackException(e);
        }
    }

    private HashMap getData(HashMap map) throws Exception {
        HashMap returnMap = new HashMap();
        String where = (String) map.get(CommonConstants.MAP_WHERE);
        List list = (List) sqlMap.executeQueryForList("getSelectBalanceSheetTO", where);
        returnMap.put("BalanceSheetTO", list);
        return returnMap;
    }
}
