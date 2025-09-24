/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition.
 * 
 *
 * RecoveryListGenerationDAO.java
 *
 *
 */
package com.see.truetransact.serverside.salaryrecovery;

import java.util.List;
import java.util.Date;
import java.util.Map;
import java.util.HashMap;
import java.util.ArrayList;
import com.ibatis.db.sqlmap.SqlMap;
import com.see.truetransact.serverside.TTDAO;
import com.see.truetransact.commonutil.DateUtil;
import com.see.truetransact.serverutil.ServerUtil;
import com.see.truetransact.commonutil.CommonUtil;
import com.see.truetransact.commonutil.CommonConstants;
import com.see.truetransact.commonutil.interestcalc.Rounding;
import com.see.truetransact.servicelocator.ServiceLocator;
import com.see.truetransact.serverexception.ServiceLocatorException;
import com.see.truetransact.serverside.transaction.common.TransactionFactory;
import com.see.truetransact.transferobject.salaryrecovery.SalaryRecoveryListMasterTO;
import com.see.truetransact.transferobject.salaryrecovery.SalaryRecoveryListDetailTO;

import com.see.truetransact.serverside.common.viewall.SelectAllDAO;
import com.see.truetransact.transferobject.deposit.interestmaintenance.InterestMaintenanceRateTO;
import com.see.truetransact.transferobject.salaryrecovery.DedExmpListMasterTO;

/**
 * RecoveryListGenerationDAO DAO.
 *
 * @author Suresh
 */
public class DeductionExemptionListGenerationDAO extends TTDAO {

    private static SqlMap sqlMap = null;
    private Date currDt = null;
    private Map returnMap = null;
    private HashMap finalMap = null;
    private List recoveryList = null;
    private List recoveryRowList = null;
    private Date intUptoDt = null;
    private Date interestUptoDt = null;
    private SalaryRecoveryListMasterTO objSalaryRecoveryListMasterTO;
    private SalaryRecoveryListDetailTO objSalaryRecoveryListDetailTO;

    /**
     * Creates a new instance of OperativeAcctProductDAO
     */
    public DeductionExemptionListGenerationDAO() throws ServiceLocatorException {
        ServiceLocator locate = ServiceLocator.getInstance();
        sqlMap = (SqlMap) locate.getDAOSqlMap();
    }

    public static void main(String str[]) {
        try {
            RecoveryListGenerationDAO dao = new RecoveryListGenerationDAO();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public HashMap execute(HashMap map) throws Exception {
        _branchCode = (String) map.get(CommonConstants.BRANCH_ID);
        currDt = ServerUtil.getCurrentDate(_branchCode);
        System.out.println(" ###### Map in RecoveryListGenerationDAO DAO : " + map);
        if (map.containsKey("RECOVERY_LIST")) {
            try {
                sqlMap.startTransaction();

                sqlMap.commitTransaction();
            } catch (Exception e) {
                sqlMap.rollbackTransaction();
                e.printStackTrace();
                throw e;
            }
            destroyObjects();
        }
        if (map.containsKey("INSMAP")) {
            try {
                sqlMap.startTransaction();
                insertAdd(map);
                sqlMap.commitTransaction();
            } catch (Exception e) {
                sqlMap.rollbackTransaction();
                e.printStackTrace();
                throw e;
            }
        }
        System.out.println("@#$@@$@@@$ returnMap : " + returnMap);
        return (HashMap) returnMap;
    }

    public void insertAdd(HashMap map) {
        HashMap newMap = (HashMap) map.get("INSMAP");
        SalaryRecoveryListDetailTO objSalaryRecoveryListDetailTO = new SalaryRecoveryListDetailTO();
        try {
            intUptoDt = (Date) newMap.get("INT_CALC_UPTO_DT");
            objSalaryRecoveryListDetailTO.setIntCalcUptoDt(intUptoDt);
            objSalaryRecoveryListDetailTO.setEmpRefNo(CommonUtil.convertObjToStr(newMap.get("EMP_REF_NO")));
            objSalaryRecoveryListDetailTO.setMemberName(CommonUtil.convertObjToStr(newMap.get("MEMBER_NAME")));
            objSalaryRecoveryListDetailTO.setExemptionMode(CommonUtil.convertObjToStr(newMap.get("EXEMPTION_MODE")));
            objSalaryRecoveryListDetailTO.setSchemeName(CommonUtil.convertObjToStr(newMap.get("SCHEME_NAME")));
            objSalaryRecoveryListDetailTO.setActNum(CommonUtil.convertObjToStr(newMap.get("ACT_NUM")));
            objSalaryRecoveryListDetailTO.setTotalDemand(CommonUtil.convertObjToDouble(newMap.get("TOTAL_DEMAND")));
            objSalaryRecoveryListDetailTO.setPrincipal(CommonUtil.convertObjToDouble(newMap.get("PRINCIPAL")));
            objSalaryRecoveryListDetailTO.setInterest(CommonUtil.convertObjToDouble(newMap.get("INTEREST")));
            objSalaryRecoveryListDetailTO.setPenal(CommonUtil.convertObjToDouble(newMap.get("PENAL")));
            objSalaryRecoveryListDetailTO.setCharges(CommonUtil.convertObjToDouble(newMap.get("CHARGES")));
            objSalaryRecoveryListDetailTO.setProd_ID(CommonUtil.convertObjToStr(newMap.get("PROD_ID")));
            objSalaryRecoveryListDetailTO.setProd_Type(CommonUtil.convertObjToStr(newMap.get("PROD_TYPE")));
            objSalaryRecoveryListDetailTO.setStatus(CommonConstants.STATUS_CREATED);
            objSalaryRecoveryListDetailTO.setPenalMonth(new Double(0));
            objSalaryRecoveryListDetailTO.setParticulars(CommonUtil.convertObjToStr(newMap.get("PARTICULARS")));
            if (map.containsKey("INSERT")) {
                sqlMap.executeUpdate("insertDedExmpListDetailTO", objSalaryRecoveryListDetailTO);
            } else if (map.containsKey("UPDATE")) {
                sqlMap.executeUpdate("updateSalaryRecoveryList", objSalaryRecoveryListDetailTO);
            } else if (map.containsKey("DELETE")) {
                sqlMap.executeUpdate("deleteSalaryRecoveryList", objSalaryRecoveryListDetailTO);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public HashMap executeQuery(HashMap obj) throws Exception {
        System.out.println("@#$@@$@@@$ Map in Dao : " + obj);
        HashMap returnMap = new HashMap();
        _branchCode = (String) obj.get(CommonConstants.BRANCH_ID);
        currDt = ServerUtil.getCurrentDate(_branchCode);
        intUptoDt = (Date) obj.get("CALC_INT_UPTO_DT");
        interestUptoDt = (Date) intUptoDt.clone();
        interestUptoDt.setDate(intUptoDt.getDate() + 1);
        System.out.println("@#$@@$@@@$ interestUptoDt + 1 : " + interestUptoDt);
        interestUptoDt = CommonUtil.getProperDate(currDt,interestUptoDt);
        System.out.println("@#$@@$@@@$ interestUptoDt + 1 : " + interestUptoDt);
        System.out.println("@#$@@$@@@$ intUptoDt: " + intUptoDt);
        returnMap = getData(obj);
        return returnMap;
    }

    private void insertMasterData(HashMap map) throws Exception {
        try {
            if (map.containsKey("CALC_INT_UPTO_DT")) {
                intUptoDt = CommonUtil.getProperDate(currDt,(Date) map.get("CALC_INT_UPTO_DT"));
                DedExmpListMasterTO objDedExmpListMasterTO = new DedExmpListMasterTO();
                objDedExmpListMasterTO.setIntCalcUptoDt(intUptoDt);
                objDedExmpListMasterTO.setCreatedBy(CommonUtil.convertObjToStr(map.get("USER_ID")));
                objDedExmpListMasterTO.setCreatedDt(currDt);
                sqlMap.executeUpdate("insertDedExmpListGeneration", objDedExmpListMasterTO);
            }
        } catch (Exception e) {
            sqlMap.rollbackTransaction();
        }
    }

    private HashMap getData(HashMap obj) throws Exception {
        HashMap singleRowMap = new HashMap();
        HashMap recoveryListMap = new HashMap();
        List listForRecovery = sqlMap.executeQueryForList("getDetailsForDedExemptionList", _branchCode);
        if (listForRecovery != null && listForRecovery.size() > 0) {
            try {
                sqlMap.startTransaction();
                finalMap = new HashMap();
                HashMap recoveryMap = null;
                recoveryList = new ArrayList();
                String prodType = "";
                String prodID = "";
                String actNum = "";
                String recoveryYesNo = "";
                String exemptionMode = "";
                int sno = 1;
                boolean insert = true;
                HashMap existMap = new HashMap();
                existMap.put("INT_CALC_UPTO_DT", intUptoDt);
                List existList = sqlMap.executeQueryForList("checkSameDateRcdInDedMastr", existMap);
                if (existList != null && existList.size() > 0) {
                    sqlMap.executeUpdate("deleteDedExmpDetailsData", existMap);
                    sqlMap.executeUpdate("deleteDedExmpDetailsMDSData", existMap);
                    sqlMap.executeUpdate("deleteDedExmpDetailsRDData", existMap);
                    sqlMap.executeUpdate("deleteDedExmpChargeDetails", existMap);
                    insert = false;
                }
                for (int i = 0; i < listForRecovery.size(); i++) {
                    boolean retired = false;
                    recoveryMap = (HashMap) listForRecovery.get(i);
                    System.out.println("recoveryMaprecoveryMap"+recoveryMap);
                    singleRowMap = new HashMap();
                    double glAmount = 0.0;
                    String empRefNoNew = CommonUtil.convertObjToStr(recoveryMap.get("EMP_REF_NO"));
                    HashMap empRefMap = new HashMap();
                    empRefMap.put("EMP_REF_NO", empRefNoNew);
                    Date retiredDate = new Date();
                    Date actualRetiredDate = new Date();
                    List retiredDateList = sqlMap.executeQueryForList("getRetireDate", empRefMap);
                    if (retiredDateList != null && retiredDateList.size() > 0) {
                        HashMap retiredHashMap = (HashMap) retiredDateList.get(0);
                        if (retiredHashMap != null && retiredHashMap.size() > 0 && retiredHashMap.containsKey("RET_DATE")) {
                            retiredDate = CommonUtil.getProperDate(currDt, DateUtil.getDateMMDDYYYY(CommonUtil.convertObjToStr(retiredHashMap.get("RET_DATE"))));
                        }
                    }
                    if (retiredDate != null && !retiredDate.equals("")) {
                        actualRetiredDate = CommonUtil.getProperDate(currDt, retiredDate);
                        java.util.GregorianCalendar gactualRetiredDateCalendar = new java.util.GregorianCalendar();
                        gactualRetiredDateCalendar.setGregorianChange(actualRetiredDate);
                        gactualRetiredDateCalendar.setTime(actualRetiredDate);
                        int retireDay = gactualRetiredDateCalendar.get(gactualRetiredDateCalendar.DAY_OF_MONTH);
                        if (retireDay == 1) {
                            gactualRetiredDateCalendar.set(gactualRetiredDateCalendar.MONTH, gactualRetiredDateCalendar.get(gactualRetiredDateCalendar.MONTH) - 1);
                            gactualRetiredDateCalendar.set(gactualRetiredDateCalendar.DAY_OF_MONTH, gactualRetiredDateCalendar.getActualMaximum(gactualRetiredDateCalendar.DAY_OF_MONTH));
                        } else {
                            gactualRetiredDateCalendar.set(gactualRetiredDateCalendar.DAY_OF_MONTH, gactualRetiredDateCalendar.getActualMaximum(gactualRetiredDateCalendar.DAY_OF_MONTH));
                        }
                        actualRetiredDate = CommonUtil.getProperDate(currDt, gactualRetiredDateCalendar.getTime());
                    }
                    int value = CommonUtil.convertObjToInt(actualRetiredDate.compareTo(intUptoDt));
                    if (value < 0) {
                        retired = true;
                    }
                    if (retired == false) {
                        glAmount = CommonUtil.convertObjToDouble(recoveryMap.get("AMOUNT")).doubleValue();
                        prodType = CommonUtil.convertObjToStr(recoveryMap.get("PRODTYPE"));
                        prodID = CommonUtil.convertObjToStr(recoveryMap.get("PROD_ID"));
                        actNum = CommonUtil.convertObjToStr(recoveryMap.get("ACT_NUM"));
                        recoveryYesNo = CommonUtil.convertObjToStr(recoveryMap.get("SALARY_RECOVERY"));
                        exemptionMode = CommonUtil.convertObjToStr(recoveryMap.get("EXEMPTION_MODE"));
                        if (prodType.equals(TransactionFactory.SUSPENSE)) {
                            glAmount = 0.0;
                            HashMap map1 = new HashMap();
                            map1.put("CLOCK_NO", actNum);
                            map1.put("AUTHORIZED_DT", CommonUtil.getProperDate(currDt,intUptoDt));
                            List aList = sqlMap.executeQueryForList("getsuspenseduedelist", map1);
                            if (aList.size() > 0) {
                                for (int j = 0; j < aList.size(); j++) {
                                    HashMap newMap = (HashMap) aList.get(j);
                                    glAmount += CommonUtil.convertObjToDouble(newMap.get("AMT"));
                                }
                            }
                        }
                        if (prodType.equals("MDS")) {
                            singleRowMap = calcMDS(actNum, prodID, prodType);
                        } else if ((prodType.equals(TransactionFactory.GL) || prodType.equals(TransactionFactory.ADVANCES)
                                || prodType.equals(TransactionFactory.OPERATIVE) || prodType.equals(TransactionFactory.SUSPENSE)) && glAmount > 0) {
                            singleRowMap = calcGL(actNum, prodID, prodType, glAmount);
                        } else if ((prodType.equals(TransactionFactory.LOANS) || prodType.equals(TransactionFactory.ADVANCES)) && glAmount == 0) {
                            singleRowMap = calcLoanPayments(actNum, prodID, prodType, "N");
                        } else if (prodType.equals(TransactionFactory.DEPOSITS)) {
                            singleRowMap = calcTermDeposits(actNum, prodID, prodType);
                        }
                        if (singleRowMap != null && singleRowMap.size() > 0) {
                            recoveryRowList = new ArrayList();
                            recoveryRowList.add(String.valueOf(sno++));
                            recoveryRowList.add(CommonUtil.convertObjToStr(recoveryMap.get("EMP_REF_NO")));
                            recoveryRowList.add(CommonUtil.convertObjToStr(recoveryMap.get("MEM_NAME")));
                            // recoveryRowList.add(CommonUtil.convertObjToStr(recoveryMap.get("EXEMPTION_MODE")));
                            recoveryRowList.add(exemptionMode);
                            recoveryRowList.add(CommonUtil.convertObjToStr(recoveryMap.get("PROD_DESC")));
                            recoveryRowList.add(CommonUtil.convertObjToStr(recoveryMap.get("ACT_NUM")));
                            recoveryRowList.add(CommonUtil.convertObjToStr(singleRowMap.get("TOTAL_DEMAND")));
                            recoveryRowList.add(CommonUtil.convertObjToStr(singleRowMap.get("PRINCIPAL")));
                            recoveryRowList.add(CommonUtil.convertObjToStr(singleRowMap.get("INTEREST")));
                            recoveryRowList.add(CommonUtil.convertObjToStr(singleRowMap.get("PENAL")));
                            recoveryRowList.add(CommonUtil.convertObjToStr(singleRowMap.get("CHARGES")));
                            recoveryRowList.add(CommonUtil.convertObjToStr(singleRowMap.get("CLEAR_BALANCE")));
                            recoveryList.add(recoveryRowList);

                            //Insert Details Data
                            SalaryRecoveryListDetailTO objSalaryRecoveryListDetailTO = new SalaryRecoveryListDetailTO();
                            objSalaryRecoveryListDetailTO.setIntCalcUptoDt(intUptoDt);
                            objSalaryRecoveryListDetailTO.setEmpRefNo(CommonUtil.convertObjToStr(recoveryMap.get("EMP_REF_NO")));
                            objSalaryRecoveryListDetailTO.setMemberName(CommonUtil.convertObjToStr(recoveryMap.get("MEM_NAME")));
                            objSalaryRecoveryListDetailTO.setSchemeName(CommonUtil.convertObjToStr(recoveryMap.get("PROD_DESC")));
                            objSalaryRecoveryListDetailTO.setActNum(CommonUtil.convertObjToStr(recoveryMap.get("ACT_NUM")));
                            objSalaryRecoveryListDetailTO.setExemptionMode(CommonUtil.convertObjToStr(recoveryMap.get("EXEMPTION_MODE")));
                            objSalaryRecoveryListDetailTO.setTotalDemand(CommonUtil.convertObjToDouble(singleRowMap.get("TOTAL_DEMAND")));
                            objSalaryRecoveryListDetailTO.setPrincipal(CommonUtil.convertObjToDouble(singleRowMap.get("PRINCIPAL")));
                            objSalaryRecoveryListDetailTO.setInterest(CommonUtil.convertObjToDouble(singleRowMap.get("INTEREST")));
                            objSalaryRecoveryListDetailTO.setPenal(CommonUtil.convertObjToDouble(singleRowMap.get("PENAL")));
                            objSalaryRecoveryListDetailTO.setCharges(CommonUtil.convertObjToDouble(singleRowMap.get("CHARGES")));
                            objSalaryRecoveryListDetailTO.setProd_ID(prodID);
                            objSalaryRecoveryListDetailTO.setProd_Type(prodType);
                            objSalaryRecoveryListDetailTO.setStatus(CommonConstants.STATUS_CREATED);
                            if (prodType.equals(TransactionFactory.DEPOSITS)) {
                                objSalaryRecoveryListDetailTO.setPenalMonth(CommonUtil.convertObjToDouble(singleRowMap.get("DEPOSIT_PENAL_MONTH")));
                            } else {
                                objSalaryRecoveryListDetailTO.setPenalMonth(new Double(0));
                            }
                            sqlMap.executeUpdate("insertDedExmpListDetailTO", objSalaryRecoveryListDetailTO);
                            if (glAmount == 0 && !prodType.equals(TransactionFactory.GL) && !prodType.equals(TransactionFactory.OPERATIVE) && !prodType.equals(TransactionFactory.SUSPENSE)) {                        //Update LOCK_STATUS YES
                                HashMap updateMap = new HashMap();
                                if (!prodType.equals("MDS")) {
                                    if (actNum.indexOf("_") != -1) {
                                        actNum = actNum.substring(0, actNum.indexOf("_"));
                                    }
                                    updateMap.put("ACCT_NUM", actNum);
                                } else {
                                    String chittalNo = "";
                                    String subNo = "";
                                    if (actNum.indexOf("_") != -1) {
                                        chittalNo = actNum.substring(0, actNum.indexOf("_"));
                                        subNo = actNum.substring(actNum.indexOf("_") + 1, actNum.length());
                                        updateMap.put("CHITTAL_NO", chittalNo);
                                        updateMap.put("SUB_NO", subNo);
                                    }
                                }
                                updateMap.put("LOCK_STATUS", "Y");
//                                sqlMap.executeUpdate("updateLockStatus" + prodType, updateMap);
                            }
                        }
                    }
                }
                if (insert) {
                    insertMasterData(obj);      // insertDetailData(map);
                }
                sqlMap.commitTransaction();
            } catch (Exception e) {
                sqlMap.rollbackTransaction();
                e.printStackTrace();
                throw e;
            }
            recoveryListMap.put("RECOVERY_LIST_TABLE_DATA", recoveryList);
            System.out.println("RECOVERY_LIST_TABLE_DATA" + recoveryList);
        }
        return recoveryListMap;
    }

    public HashMap calcLoanPayments(String actNum, String prodId, String prodType, String recoveryYesNo) throws Exception {
        HashMap dataMap = new HashMap();
        HashMap whereMap = new HashMap();
        whereMap.put("ACT_NUM", actNum);
        whereMap.put("CURR_DATE",getProperDateFormat(currDt.clone()));
        double intVal = 0, princVal =0;
        String remittedStatus = "";
        List resltList = null;
        if (prodType != null && prodType.equals("TL")) {
            System.out.println("whereMap :" + whereMap);
            HashMap hmap = new HashMap();
            hmap.put("ACT_NUM", CommonUtil.convertObjToStr(actNum));
            hmap.put("TRANS_DT", getProperDateFormat(intUptoDt.clone()));
            hmap.put("REC_TYPE", "Direct");
            System.out.println("intUptoDt ----------------------- :" + intUptoDt);
            resltList = sqlMap.executeQueryForList("getPenalIntDetailsTL", hmap);
            if (resltList != null && resltList.size() > 0) {
                HashMap ansMap = (HashMap) resltList.get(0);
                if (ansMap != null && ansMap.containsKey("RESULT")) {
                    String ans = CommonUtil.convertObjToStr(ansMap.get("RESULT"));
                    if (ans != null && ans.length() > 0) {
                        String[] ansArr = ans.split(":");
                        String intStr = "0", princStr = "0";
                        if (ansArr.length > 5) {
                            System.out.println("ansArr[4] :" + ansArr[5]);
                            if (ansArr[5].contains("=")) {
                                String[] splArr = ansArr[5].split("=");
                                if (splArr.length > 1) {
                                    intStr = splArr[1].trim();
                                }
                            }
                        }
                        if (ansArr.length > 4) {
                            System.out.println("ansArr[4] :" + ansArr[4]);
                            if (ansArr[4].contains("=")) {
                                String[] splArr = ansArr[4].split("=");
                                if (splArr.length > 1) {
                                    princStr = splArr[1].trim();
                                }
                            }
                        }
                        if (ansArr.length > 7) {
                            System.out.println("ansArr[7] :" + ansArr[7]);
                            if (ansArr[7].contains("=")) {
                                String[] splArr = ansArr[7].split("=");
                                if (splArr.length > 1) {
                                    remittedStatus = splArr[1].trim();
                                }
                            }
                        }
                        intVal = CommonUtil.convertObjToDouble(intStr);
                        princVal = CommonUtil.convertObjToDouble(princStr);
                    }
                }
            }
        }
        boolean isChkFlag = false;
         List pList = null;
//        if (prodType != null && prodType.equals("TL")) {
//            if (princVal > 0) {
//                isChkFlag = true;
//            } else {
//                if (princVal == 0) {
//                     pList = sqlMap.executeQueryForList("getNotProcessedCurrentMonthRecords", whereMap);
//                    if (pList.isEmpty()) {
//                        isChkFlag = true;
//                    } else {
//                        isChkFlag = false;
//                    }
//                }
//            }
//        } else {
//            List notProceesedList = sqlMap.executeQueryForList("getSelectNotProcessRecordcurrMonth", whereMap);
//            if (notProceesedList.isEmpty()) {
//                isChkFlag = true;
//            }
//            if (prodType != null && prodType.equals("AD")) {
//                 pList = sqlMap.executeQueryForList("getNotProcessedCurrentMonthRecords", whereMap);
//                if (pList.isEmpty()) {
//                    isChkFlag = true;
//                } else {
//                    isChkFlag = false;
//                }
//            }
//        }
        isChkFlag = true;
        if(isChkFlag){
        List parameterList = sqlMap.executeQueryForList("getRecoveryParameter", whereMap);
        String clear_balance = "";
        boolean flag = false;
        if (parameterList != null && parameterList.size() > 0) {
            int firstDay = 0;
            double clearBal = 0.0;
            java.util.Date repayDate = null;
            java.util.Date inst_dt = null;
            java.util.Date checkDate = (java.util.Date) intUptoDt.clone();
            Date checkingDate =(Date) intUptoDt.clone();
            java.util.Date sanctionDt = null;
            String EMIINSIMPLEINTREST = "";
            String MORATORIUMGIVEN = "";
            String INSTALLTYPE = "0";
            whereMap = (HashMap) parameterList.get(0);
            firstDay = CommonUtil.convertObjToInt(whereMap.get("FIRST_DAY"));
            checkingDate.setDate(firstDay);
            repayDate = (java.util.Date) whereMap.get("REPAYMENT_DT");
            sanctionDt = (java.util.Date) whereMap.get("FROM_DT");
            System.out.println("checkingDate"+checkingDate);
            long dtDiff = DateUtil.dateDiff(sanctionDt, checkingDate);
            System.out.println("dtDiffdtDiff"+dtDiff);
            if((double) dtDiff <0.0D){
                dataMap = null;
            }else{
            
            EMIINSIMPLEINTREST = CommonUtil.convertObjToStr(whereMap.get("EMI_IN_SIMPLEINTREST"));
            MORATORIUMGIVEN = CommonUtil.convertObjToStr(whereMap.get("MORATORIUM_GIVEN"));
            INSTALLTYPE = CommonUtil.convertObjToStr(whereMap.get("INSTALL_TYPE"));
            long diffDay = DateUtil.dateDiff(sanctionDt, checkDate);
            long diffRepayDay = DateUtil.dateDiff(repayDate, checkDate);
            System.out.println("kusdkjvbsdv" + diffRepayDay);
            System.out.println("### diffDay : "+diffDay);
             String behavesLike = CommonUtil.convertObjToStr(whereMap.get("FACILITY_TYPE"));
                if (/*INSTALLTYPE.equals("UNIFORM_PRINCIPLE_EMI") &&*/behavesLike != null && !behavesLike.equals("") && behavesLike.length()>0 && !behavesLike.equals("OD") && diffRepayDay < 0 && MORATORIUMGIVEN.equals("N")) {
            //if (/*INSTALLTYPE.equals("UNIFORM_PRINCIPLE_EMI") &&*/ diffRepayDay < 0 && MORATORIUMGIVEN.equals("N")) {
//                System.out.println("inside something");
                dataMap = null;
            } else {
                if ((double) diffDay >= 0.0D) {
//                    System.out.println("### Allow : "+checkDate);
                    if (recoveryYesNo.equals("N")) {
                        HashMap asAndWhenMap = interestCalculationTLAD(actNum, prodId, prodType);
//                        System.out.println((new StringBuilder()).append("@#@ asAndWhenMap is >>>>").append(asAndWhenMap).toString());
//                        System.out.println((new StringBuilder()).append("transDetail").append(actNum).append(_branchCode).toString());
                        HashMap insertPenal = new HashMap();
                        List chargeList = null;
                        HashMap loanInstall = new HashMap();
                        loanInstall.put("ACT_NUM", actNum);
                        loanInstall.put("BRANCH_CODE", _branchCode);
                        String moratorium = "";
                        List MoraList = ServerUtil.executeQuery("getMorotorium", loanInstall);
                        if (MoraList.size() > 0 && MoraList.get(0) != null) {
                            HashMap mapop = (HashMap) MoraList.get(0);
                            if (mapop.get("MORATORIUM_GIVEN") != null) {
                                moratorium = CommonUtil.convertObjToStr(mapop.get("MORATORIUM_GIVEN"));
                                clearBal = CommonUtil.convertObjToDouble(mapop.get("CLEAR_BALANCE"))*-1;
                            }
//                            System.out.println("soutMort" + moratorium);
                        }
                        double instalAmt = 0.0D;
                        double paidAmount = 0.0D;
                        HashMap emiMap = new HashMap();
                        String installtype = "";
                        String emi_uniform = "";
                        if (prodType != null && prodType.equals("TL")) {
                            double totPrinciple = 0.0D;
                            List emiList = sqlMap.executeQueryForList("getEmiTypeDetail", loanInstall);
                            if (emiList.size() > 0) {
                                emiMap = (HashMap) emiList.get(0);
                                installtype = emiMap.get("INSTALL_TYPE").toString();
                                emi_uniform = emiMap.get("EMI_IN_SIMPLEINTREST").toString();
                            }
                            HashMap allInstallmentMap = null;
                            if (!installtype.equals("UNIFORM_PRINCIPLE_EMI") || !emi_uniform.equals("Y")) {
                                List paidAmt = sqlMap.executeQueryForList("getPaidPrinciple", loanInstall);
                                allInstallmentMap = (HashMap) paidAmt.get(0);
                                System.out.println("allInstallmentMap..."+allInstallmentMap);
                                System.out.println("!!!!asAndWhenMap:"+asAndWhenMap);
                                totPrinciple = CommonUtil.convertObjToDouble(allInstallmentMap.get("PRINCIPLE")).doubleValue();
                                paidAmount = CommonUtil.convertObjToDouble(allInstallmentMap.get("PRINCIPLE")).doubleValue();
                                System.out.println("totPrinciple11:"+totPrinciple+" ::"+paidAmount);
                                if (asAndWhenMap == null || asAndWhenMap != null && asAndWhenMap.containsKey("AS_CUSTOMER_COMES") && asAndWhenMap.get("AS_CUSTOMER_COMES") != null && asAndWhenMap.get("AS_CUSTOMER_COMES").equals("N")) {
                                    paidAmt = sqlMap.executeQueryForList("getIntDetails", loanInstall);
                                    if (paidAmt != null && paidAmt.size() > 0) {
                                        allInstallmentMap = (HashMap) paidAmt.get(0);
                                    }
                                    double totExcessAmt = CommonUtil.convertObjToDouble(allInstallmentMap.get("EXCESS_AMT")).doubleValue();
                                    totPrinciple += totExcessAmt;
                                    System.out.println("in as cust comexx"+totPrinciple);
                                }
                                List lst = sqlMap.executeQueryForList("getAllLoanInstallment", loanInstall);
                                int i = 0;
                                do {
                                    if (i >= lst.size()) {
                                        break;
                                    }
                                    allInstallmentMap = (HashMap) lst.get(i);
                                    System.out.println("allInstallmentMap...,mmm>>"+allInstallmentMap);
                                    instalAmt = CommonUtil.convertObjToDouble(allInstallmentMap.get("PRINCIPAL_AMT")).doubleValue();
                                    System.out.println("instalAmtt..."+instalAmt+"toooottt"+totPrinciple);
                                    if (instalAmt <= totPrinciple) {
                                        totPrinciple -= instalAmt;
                                        System.out.println("totPrinciple-=instalAmt=="+totPrinciple);
                                        inst_dt = new java.util.Date();
                                        String in_date = CommonUtil.convertObjToStr(allInstallmentMap.get("INSTALLMENT_DT"));
                                        inst_dt = DateUtil.getDateMMDDYYYY(in_date);
                                    } else {
                                        inst_dt = new java.util.Date();
                                        String in_date = CommonUtil.convertObjToStr(allInstallmentMap.get("INSTALLMENT_DT"));
                                        inst_dt = DateUtil.getDateMMDDYYYY(in_date);
                                        System.out.println("clearBalclearBal"+clearBal);
                                        System.out.println("inst_dt=22===="+inst_dt+"currDt=22======"+intUptoDt);
                                        System.out.println("totPrrr22rrrrrrrrrrrrrr=" + DateUtil.dateDiff(repayDate, intUptoDt) + "ggggg=" + moratorium);
                                        if (DateUtil.dateDiff(checkDate, intUptoDt) >= 0L && moratorium != null && moratorium.equals("Y")) {
                                            if (DateUtil.dateDiff(repayDate, intUptoDt) < 0) {
                                                totPrinciple = 0.0D;
                                                flag = true;
                                                System.out.println("not here");
                                            } else {
                                                System.out.println("setting hererererrere");
                                                totPrinciple = CommonUtil.convertObjToDouble(allInstallmentMap.get("PRINCIPAL_AMT")).doubleValue()-totPrinciple;
                                                if (totPrinciple > clearBal) {
                                                    totPrinciple = clearBal;
                                                }
                                            }
                                        } else {
                                            System.out.println("herereererer");
                                            totPrinciple = CommonUtil.convertObjToDouble(allInstallmentMap.get("PRINCIPAL_AMT")).doubleValue() - totPrinciple;
//                                            if(totPrinciple>clearBal){
//                                                totPrinciple = clearBal;
//                                            }
                                            System.out.println("before Breakkkk" + totPrinciple);
                                        }
                                        break;
                                    }
                                    i++;
                                } while (true);
                            } else {
                                List paidAmtemi = sqlMap.executeQueryForList("getPaidPrincipleEMI1", loanInstall);
                                allInstallmentMap = (HashMap) paidAmtemi.get(0);
                                System.out.println("!!!!asAndWhenMap:"+asAndWhenMap);
                                System.out.println("allInstallmentMap..."+allInstallmentMap);
                                totPrinciple = CommonUtil.convertObjToDouble(allInstallmentMap.get("PRINCIPLE")).doubleValue();
                                paidAmount = CommonUtil.convertObjToDouble(allInstallmentMap.get("PRINCIPLE")).doubleValue();
//                                System.out.println("totPrinciple22:"+totPrinciple+" ::"+paidAmount);
                                if (asAndWhenMap == null || asAndWhenMap != null && asAndWhenMap.containsKey("AS_CUSTOMER_COMES") && asAndWhenMap.get("AS_CUSTOMER_COMES") != null && asAndWhenMap.get("AS_CUSTOMER_COMES").equals("N")) {
                                    paidAmtemi = sqlMap.executeQueryForList("getIntDetails", loanInstall);
                                    if (paidAmtemi != null && paidAmtemi.size() > 0) {
                                        allInstallmentMap = (HashMap) paidAmtemi.get(0);
                                    }
                                    System.out.println("allInstallmentMap444"+allInstallmentMap);
                                    double totExcessAmt = CommonUtil.convertObjToDouble(allInstallmentMap.get("EXCESS_AMT")).doubleValue();
                                    totPrinciple += totExcessAmt;
                                    System.out.println("totPrinciple"+totPrinciple);
                                }
                                List lst = sqlMap.executeQueryForList("getAllLoanInstallment", loanInstall);
                                int i = 0;
                                do {
                                    if (i >= lst.size()) {
                                        break;
                                    }
                                    allInstallmentMap = (HashMap) lst.get(i);
//                                    System.out.println("allInstallmentMap34243"+allInstallmentMap);
                                    instalAmt = CommonUtil.convertObjToDouble(allInstallmentMap.get("PRINCIPAL_AMT")).doubleValue();
//                                    System.out.println("111222instalAmt"+instalAmt+":::"+totPrinciple);
                                    if (instalAmt <= totPrinciple) {
                                        totPrinciple -= instalAmt;
//                                        System.out.println("chhhhnnn"+totPrinciple);
                                        inst_dt = new java.util.Date();
                                        String in_date = CommonUtil.convertObjToStr(allInstallmentMap.get("INSTALLMENT_DT"));
                                        inst_dt = DateUtil.getDateMMDDYYYY(in_date);
                                    } else {
                                        inst_dt = new java.util.Date();
                                        String in_date = CommonUtil.convertObjToStr(allInstallmentMap.get("INSTALLMENT_DT"));
                                        inst_dt = DateUtil.getDateMMDDYYYY(in_date);
                                        totPrinciple = CommonUtil.convertObjToDouble(allInstallmentMap.get("TOTAL_AMT")).doubleValue() - totPrinciple;
//                                        System.out.println("bbbkkk"+totPrinciple);
                                        break;
                                    }
//                                    System.out.println("totPrinciple@@@@@@@@@@@@"+totPrinciple);
                                    i++;
                                } while (true);
                            }
                            if (!installtype.equals("UNIFORM_PRINCIPLE_EMI") || !emi_uniform.equals("Y")) {
                                dataMap.put("INSTMT_AMT", Double.valueOf(instalAmt));
                            } else {
                                dataMap.put("INSTMT_AMT", allInstallmentMap.get("TOTAL_AMT"));
                            }
                            List aList = ServerUtil.executeQuery("getMorotorium", loanInstall);
                            String moret = "";
                            if (aList.size() > 0 && aList.get(0) != null) {
                                HashMap mapop = (HashMap) aList.get(0);
                                if (mapop.get("MORATORIUM_GIVEN") != null) {
                                    moret = mapop.get("MORATORIUM_GIVEN").toString();
                                }
                            }
                            System.out.println("inst_dt=33===="+inst_dt+"currDt===33===="+intUptoDt);
                            System.out.println("totPrrrrr333rrrrrrrrrrrr="+DateUtil.dateDiff(inst_dt, intUptoDt)+"ggggg="+moret);
                            System.out.println("totPrrrrrrrrrrrrrrrrr="+DateUtil.dateDiff(inst_dt, intUptoDt)+"ggggg="+moret);
                            if (DateUtil.dateDiff(inst_dt, intUptoDt) <= 0L && moret != null && moret.equals("Y")) {
                                dataMap.put("INSTMT_AMT", Integer.valueOf(0));
                                dataMap.put("PRINCIPAL", String.valueOf("0"));
                            }
                            java.util.Date addDt = (java.util.Date) intUptoDt.clone();
                            java.util.Date instDt = DateUtil.addDays(inst_dt, 1);
                            addDt.setDate(instDt.getDate());
                            addDt.setMonth(instDt.getMonth());
                            addDt.setYear(instDt.getYear());
                            loanInstall.put("FROM_DATE", getProperDateFormat(addDt.clone()));
                            loanInstall.put("TO_DATE", interestUptoDt);
//                            System.out.println("!! getTotalamount#####"+loanInstall);
                            List lst1 = null;
                            if (inst_dt != null && totPrinciple > 0.0D) {
                                lst1 = sqlMap.executeQueryForList("getTotalAmountOverDue", loanInstall);
                                System.out.println("listsize####"+lst1);
                            }
                            double principle = 0.0D;
//                            if (lst1 != null && lst1.size() > 0) {
//                                HashMap map = (HashMap) lst1.get(0);
//                                principle = CommonUtil.convertObjToDouble(map.get("PRINCIPAL_AMOUNT")).doubleValue();
//                            }
//                            totPrinciple += principle;
                            System.out.println("totPrinciple 1111####"+totPrinciple);
                            if (!installtype.equals("UNIFORM_PRINCIPLE_EMI") || !emi_uniform.equals("Y")) {
                                if (lst1 != null && lst1.size() > 0) {
                                    HashMap map = (HashMap) lst1.get(0);
                                    principle = CommonUtil.convertObjToDouble(map.get("PRINCIPAL_AMOUNT")).doubleValue();
                                }
                                totPrinciple += principle;
                            } else if (lst1 != null && lst1.size() > 0) {
                                    HashMap map = (HashMap) lst1.get(0);
                                    System.out.println("snnnnnn" + map);
                                    System.out.println("here here totPrinciple" + totPrinciple);
                                    principle = CommonUtil.convertObjToDouble(map.get("PRINCIPAL_AMOUNT")).doubleValue() /*+ CommonUtil.convertObjToDouble(map.get("INTEREST_AMOUNT")).doubleValue()*/;
                                    System.out.println("sdasdsd" + principle);
                                    if (principle == 0.0D) {
                                        List advList = sqlMap.executeQueryForList("getAdvAmt", loanInstall);
                                        if (advList.size() > 0 && advList != null) {
                                            map = (HashMap) advList.get(0);
                                            if (map.get("TOTAL_AMT") != null) {
                                                principle = CommonUtil.convertObjToDouble(map.get("TOTAL_AMT")).doubleValue();
                                                System.out.println(" here principle" + principle);
                                            }
                                            totPrinciple = principle;
                                            System.out.println("some totPrinciple" + totPrinciple);
                                        }
                                    } else {
                                        totPrinciple += principle;
                                    }
                                    System.out.println("totPrinciple66666" + totPrinciple);
//                                }
                            } else {
                                totPrinciple -= CommonUtil.convertObjToDouble(asAndWhenMap.get("INTEREST")).doubleValue();
                                System.out.println("innn eeelllsss333" + totPrinciple);
                            }
                            System.out.println("totPrinciple-->> "+totPrinciple);
                            insertPenal.put("CURR_MONTH_PRINCEPLE", new Double(totPrinciple));
                            insertPenal.put("INSTALL_DT", inst_dt);
                            if (asAndWhenMap.containsKey("MORATORIUM_INT_FOR_EMI")) {
                                insertPenal.put("MORATORIUM_INT_FOR_EMI", asAndWhenMap.get("MORATORIUM_INT_FOR_EMI"));
                            }
                            if (asAndWhenMap != null && asAndWhenMap.containsKey("AS_CUSTOMER_COMES") && asAndWhenMap.get("AS_CUSTOMER_COMES") != null && asAndWhenMap.get("AS_CUSTOMER_COMES").equals("Y")) {
                                double interest = CommonUtil.convertObjToDouble(asAndWhenMap.get("INTEREST")).doubleValue();
                                double penal = CommonUtil.convertObjToDouble(asAndWhenMap.get("LOAN_CLOSING_PENAL_INT")).doubleValue();
                                List facilitylst = sqlMap.executeQueryForList("LoneFacilityDetailAD", loanInstall);
                                if (facilitylst != null && facilitylst.size() > 0) {
                                    HashMap hash = (HashMap) facilitylst.get(0);
                                    if (hash.get("CLEAR_BALANCE") != null) {
                                        clear_balance = hash.get("CLEAR_BALANCE").toString();
                                    }
//                                    System.out.println("clear_balance ="+clear_balance+" aaa---"+CommonUtil.convertObjToStr(loanInstall.get("ACT_NUM")));
                                    hash.put("ACT_NUM", loanInstall.get("ACT_NUM"));
                                    if (asAndWhenMap.containsKey("PREMATURE")) {
                                        insertPenal.put("PREMATURE", asAndWhenMap.get("PREMATURE"));
                                    }
                                    if (asAndWhenMap.containsKey("PREMATURE") && asAndWhenMap.containsKey("PREMATURE_INT_CALC_AMT") && CommonUtil.convertObjToStr(asAndWhenMap.get("PREMATURE_INT_CALC_AMT")).equals("LOANSANCTIONAMT")) {
                                        hash.put("FROM_DT", hash.get("ACCT_OPEN_DT"));
                                    } else {
                                        hash.put("FROM_DT", hash.get("LAST_INT_CALC_DT"));
                                        hash.put("FROM_DT", DateUtil.addDays((java.util.Date) hash.get("FROM_DT"), 2));
                                    }
                                    hash.put("TO_DATE", interestUptoDt.clone());
                                    hash.put("SALARY_RECOVERY","SALARY_RECOVERY");
                                    if (asAndWhenMap == null || !asAndWhenMap.containsKey("INSTALL_TYPE") || asAndWhenMap.get("INSTALL_TYPE") == null || !asAndWhenMap.get("INSTALL_TYPE").equals("EMI")) {
                                        facilitylst = sqlMap.executeQueryForList("getPaidPrinciple", hash);
                                        hash.remove("SALARY_RECOVERY");
                                    } else {
                                        facilitylst = null;
                                        if (asAndWhenMap.containsKey("PRINCIPAL_DUE") && asAndWhenMap.get("PRINCIPAL_DUE") != null) {
                                            System.out.println("principaldue: "+asAndWhenMap.get("PRINCIPAL_DUE"));
                                            insertPenal.put("CURR_MONTH_PRINCEPLE", asAndWhenMap.get("PRINCIPAL_DUE"));
                                        }
                                    }
                                    if (facilitylst != null && facilitylst.size() > 0) {
                                        hash = (HashMap) facilitylst.get(0);
                                        interest -= CommonUtil.convertObjToDouble(hash.get("INTEREST")).doubleValue();
                                        penal -= CommonUtil.convertObjToDouble(hash.get("PENAL")).doubleValue();
                                        insertPenal.put("PAID_INTEREST", hash.get("INTEREST"));
                                    }
                                }
//                                System.out.println("####interest:"+interest);
                                if (interest > 0.0D) {
                                    insertPenal.put("CURR_MONTH_INT", new Double(interest));
                                } else {
                                    insertPenal.put("CURR_MONTH_INT", new Double(0.0D));
                                }
                                if (penal > 0.0D) {
                                    insertPenal.put("PENAL_INT", new Double(penal));
                                } else {
                                    insertPenal.put("PENAL_INT", new Double(0.0D));
                                }
                                insertPenal.put("INTEREST", Math.round(CommonUtil.convertObjToDouble(asAndWhenMap.get("INTEREST"))));
                                insertPenal.put("LOAN_CLOSING_PENAL_INT", asAndWhenMap.get("LOAN_CLOSING_PENAL_INT"));
                                insertPenal.put("LAST_INT_CALC_DT", asAndWhenMap.get("LAST_INT_CALC_DT"));
                                insertPenal.put("ROI", asAndWhenMap.get("ROI"));
                                chargeList = sqlMap.executeQueryForList("getChargeDetails", loanInstall);
                            } else {
                                List getIntDetails = sqlMap.executeQueryForList("getIntDetails", loanInstall);
                                HashMap hash = null;
                                if (getIntDetails != null) {
                                    for (int i = 0; i < getIntDetails.size(); i++) {
                                        hash = (HashMap) getIntDetails.get(i);
                                        String trn_mode = CommonUtil.convertObjToStr(hash.get("TRN_CODE"));
                                        double pBal = CommonUtil.convertObjToDouble(hash.get("PBAL")).doubleValue();
                                        double iBal = CommonUtil.convertObjToDouble(hash.get("IBAL")).doubleValue();
                                        double pibal = CommonUtil.convertObjToDouble(hash.get("PIBAL")).doubleValue();
                                        double excess = CommonUtil.convertObjToDouble(hash.get("EXCESS_AMT")).doubleValue();
                                        System.out.println("pBalcc99oogytf"+pBal+"::"+iBal+"::"+pibal+"::"+excess);
                                        pBal -= excess;
                                        System.out.println("pBalrrr"+pBal);
                                        if (pBal < totPrinciple) {
                                            System.out.println("pBal-->> "+pBal);
                                            insertPenal.put("CURR_MONTH_PRINCEPLE", new Double(pBal));
                                        }
//                                        System.out.println("insertPenal5555"+insertPenal);
                                        if (trn_mode.equals("C*")) {
                                            insertPenal.put("CURR_MONTH_INT", String.valueOf(iBal + pibal));
                                            insertPenal.put("PRINCIPLE_BAL", new Double(pBal));
                                            insertPenal.put("EBAL", hash.get("EBAL"));
                                            break;
                                        }
                                        if (!trn_mode.equals("DP")) {
                                            insertPenal.put("CURR_MONTH_INT", String.valueOf(iBal + pibal));
                                        }
                                        insertPenal.put("EBAL", hash.get("EBAL"));
                                        insertPenal.put("PRINCIPLE_BAL", new Double(pBal));
//                                        System.out.println("int principel detailsINSIDE LOAN##"+insertPenal);
                                    }

                                }
                                getIntDetails = sqlMap.executeQueryForList("getPenalIntDetails", loanInstall);
                                hash = (HashMap) getIntDetails.get(0);
                                insertPenal.put("PENAL_INT", hash.get("PIBAL"));
                            }
//                            System.out.println("insertPenalnnnnnnshdcgasdg"+insertPenal);
                        }
                        if (prodType != null && prodType.equals("AD") && asAndWhenMap != null && asAndWhenMap.size() > 0) {
                            if (asAndWhenMap.containsKey("AS_CUSTOMER_COMES") && asAndWhenMap.get("AS_CUSTOMER_COMES").equals("Y")) {
                                List facilitylst = sqlMap.executeQueryForList("LoneFacilityDetailAD", loanInstall);
                                double interest = CommonUtil.convertObjToDouble(asAndWhenMap.get("INTEREST")).doubleValue();
                                double penal = CommonUtil.convertObjToDouble(asAndWhenMap.get("LOAN_CLOSING_PENAL_INT")).doubleValue();
                                if (facilitylst != null && facilitylst.size() > 0) {
                                    HashMap hash = (HashMap) facilitylst.get(0);
                                    if (hash.get("CLEAR_BALANCE") != null) {
                                        clear_balance = hash.get("CLEAR_BALANCE").toString();
                                    }
                                    hash.put("ACT_NUM", loanInstall.get("ACT_NUM"));
                                    hash.put("FROM_DT", hash.get("LAST_INT_CALC_DT"));
                                    hash.put("FROM_DT", DateUtil.addDays((java.util.Date) hash.get("FROM_DT"), 2));
                                    hash.put("TO_DATE", DateUtil.addDaysProperFormat(interestUptoDt, -1));
                                    facilitylst = sqlMap.executeQueryForList("getPaidPrincipleAD", hash);
                                    if (facilitylst != null && facilitylst.size() > 0) {
                                        hash = (HashMap) facilitylst.get(0);
                                        interest -= CommonUtil.convertObjToDouble(hash.get("INTEREST")).doubleValue();
                                        penal -= CommonUtil.convertObjToDouble(hash.get("PENAL")).doubleValue();
                                    }
                                }
                                if (interest > 0.0D) {
                                    insertPenal.put("CURR_MONTH_INT", new Double(interest));
                                } else {
                                    insertPenal.put("CURR_MONTH_INT", new Double(0.0D));
                                }
                                if (penal > 0.0D) {
                                    insertPenal.put("PENAL_INT", new Double(penal));
                                } else {
                                    insertPenal.put("PENAL_INT", new Double(0.0D));
                                }
                                insertPenal.put("INTEREST", Math.round(CommonUtil.convertObjToDouble(asAndWhenMap.get("INTEREST"))));
                                insertPenal.put("LOAN_CLOSING_PENAL_INT", asAndWhenMap.get("LOAN_CLOSING_PENAL_INT"));
                                chargeList = sqlMap.executeQueryForList("getChargeDetails", loanInstall);
                            } else if (prodType != null && prodType.equals("AD")) {
                                List getIntDetails = sqlMap.executeQueryForList("getIntDetailsAD", loanInstall);
                                HashMap hash = null;
                                int i = 0;
                                do {
                                    if (i >= getIntDetails.size()) {
                                        break;
                                    }
                                    hash = (HashMap) getIntDetails.get(i);
                                    String trn_mode = CommonUtil.convertObjToStr(hash.get("TRN_CODE"));
                                    double iBal = CommonUtil.convertObjToDouble(hash.get("IBAL")).doubleValue();
                                    double pibal = CommonUtil.convertObjToDouble(hash.get("PIBAL")).doubleValue();
                                    double excess = CommonUtil.convertObjToDouble(hash.get("EXCESS_AMT")).doubleValue();
                                    if (trn_mode.equals("C*")) {
                                        insertPenal.put("CURR_MONTH_INT", String.valueOf(iBal + pibal));
                                        insertPenal.put("PRINCIPLE_BAL", new Double(CommonUtil.convertObjToDouble(hash.get("PBAL")).doubleValue() - excess));
                                        insertPenal.put("EBAL", hash.get("EBAL"));
                                        break;
                                    }
                                    insertPenal.put("CURR_MONTH_INT", String.valueOf(iBal + pibal));
                                    insertPenal.put("PRINCIPLE_BAL", new Double(CommonUtil.convertObjToDouble(hash.get("PBAL")).doubleValue() - excess));
                                    insertPenal.put("EBAL", hash.get("EBAL"));
//                                    System.out.println("int principel detailsINSIDE OD"+insertPenal);
                                    i++;
                                } while (true);
                                getIntDetails = sqlMap.executeQueryForList("getPenalIntDetailsAD", loanInstall);
                                if (getIntDetails.size() > 0) {
                                    hash = (HashMap) getIntDetails.get(0);
                                    insertPenal.put("PENAL_INT", hash.get("PIBAL"));
                                }
                                insertPenal.remove("PRINCIPLE_BAL");
                            }
                        }
                        if (prodType != null && prodType.equals("AD")) {
                            double pBalance = 0.0D;
                            java.util.Date expDt = null;
                            List expDtList = sqlMap.executeQueryForList("getLoanExpDate", loanInstall);
                            if (expDtList != null && expDtList.size() > 0) {
                                whereMap = new HashMap();
                                whereMap = (HashMap) expDtList.get(0);
                                pBalance = CommonUtil.convertObjToDouble(whereMap.get("PBAL")).doubleValue();
                                expDt = (java.util.Date) whereMap.get("TO_DT");
                                long diffDayPending = DateUtil.dateDiff(expDt, intUptoDt);
//                                System.out.println("############# Insert PBalance"+pBalance+"######diffDayPending :"+diffDayPending);
                                if (diffDayPending > 0L && pBalance > 0.0D) {
                                    insertPenal.put("PRINCIPLE_BAL", new Double(pBalance));
                                }
                            }
                        }
//                        System.out.println("####### insertPenal : "+insertPenal);
                        double chargeAmt = 0.0D;
                        whereMap = new HashMap();
                        whereMap.put("ACT_NUM", actNum);
                        chargeAmt = getChargeAmount(whereMap, prodType);
                        if (chargeAmt > 0.0D) {
                            dataMap.put("CHARGES", String.valueOf(chargeAmt));
                        } else {
                            dataMap.put("CHARGES", "0");
                        }
                        System.out.println("####### Single Row insertPenal : "+insertPenal);
                        double totalDemand = 0.0D;
                        double principalAmount = 0.0D;
                        if (insertPenal.containsKey("CURR_MONTH_PRINCEPLE")) {
                            principalAmount = CommonUtil.convertObjToDouble(insertPenal.get("CURR_MONTH_PRINCEPLE")).doubleValue();
                            totalDemand = CommonUtil.convertObjToDouble(insertPenal.get("CURR_MONTH_INT")).doubleValue() + CommonUtil.convertObjToDouble(insertPenal.get("PENAL_INT")).doubleValue() + chargeAmt;
                            System.out.println("totalDemand 111===="+totalDemand);
                        } else {
                            principalAmount = CommonUtil.convertObjToDouble(insertPenal.get("PRINCIPLE_BAL")).doubleValue();
                            totalDemand = CommonUtil.convertObjToDouble(insertPenal.get("CURR_MONTH_INT")).doubleValue() + CommonUtil.convertObjToDouble(insertPenal.get("PENAL_INT")).doubleValue() + chargeAmt;
                            System.out.println("totalDemand 222===="+totalDemand);
                        }
                        if (inst_dt != null && prodType.equals("TL")) {
//                            if (retired != null && retired.equals("YES")) {
//                                System.out.println("11111111clear_balance" + clear_balance);
//                                dataMap.put("PRINCIPAL", String.valueOf(Math.abs(Integer.parseInt(clear_balance))));
//                            } else 
//                            {
//                                System.out.println("intUptoDt"+intUptoDt+"inst_dt"+inst_dt);
//                                System.out.println("jeffin----"+DateUtil.dateDiff(intUptoDt, inst_dt));
                            if (DateUtil.dateDiff(intUptoDt, inst_dt) <= 0L) {
                                if (installtype.equals("UNIFORM_PRINCIPLE_EMI") && emi_uniform.equals("Y") && moratorium.equals("N")) {
                                    principalAmount -= CommonUtil.convertObjToDouble(insertPenal.get("CURR_MONTH_INT")).doubleValue();
//                                        System.out.println("222222222222222principalAmount" + principalAmount);
                                    if (principalAmount > 0) {
                                        dataMap.put("PRINCIPAL", String.valueOf(principalAmount));
                                    } else {
                                        dataMap.put("PRINCIPAL", "0");
                                    }
                                } else {
                                        System.out.println("33333333333principalAmount" + principalAmount);
                                    if (principalAmount > 0) {
                                        dataMap.put("PRINCIPAL", String.valueOf(principalAmount));
                                    } else {
                                        dataMap.put("PRINCIPAL", "0");
                                    }
                                }
                            } //                                else if (installtype.equals("UNIFORM_PRINCIPLE_EMI") && emi_uniform.equals("Y") && moratorium.equals("N")) {
                            //                                    principalAmount -= CommonUtil.convertObjToDouble(insertPenal.get("CURR_MONTH_INT")).doubleValue();
                            //                                    System.out.println("44444444principalAmount" + principalAmount);
                            //                                    dataMap.put("PRINCIPAL", String.valueOf(principalAmount));
                            //                                }
                            else {
                                    System.out.println("5555555");
                                dataMap.put("PRINCIPAL", "0");
                                principalAmount = 0.0;
                            }
//                            }
                            if (principalAmount == 0.0 && (!installtype.equals("UNIFORM_PRINCIPLE_EMI") || !emi_uniform.equals("Y")) || installtype.equals("UNIFORM_PRINCIPLE_EMI") && emi_uniform.equals("Y")) {
//                                System.out.println("############ instalAmt : "+instalAmt);
                                HashMap balanceMap = new HashMap();
                                double balanceLoanAmt = 0.0D;
                                double finalDemandAmt = 0.0D;
//                                System.out.println("############ actNum : "+actNum);
                                balanceMap.put("ACCOUNTNO", actNum);
                                List balannceAmtLst = sqlMap.executeQueryForList("getBalanceLoanPrincipalAmt", balanceMap);
                                if (balannceAmtLst != null && balannceAmtLst.size() > 0) {
                                    balanceMap = (HashMap) balannceAmtLst.get(0);
                                    balanceLoanAmt = CommonUtil.convertObjToDouble(balanceMap.get("LOAN_BALANCE_PRINCIPAL")).doubleValue();
//                                    System.out.println("############ instalAmt : "+instalAmt);
//                                    System.out.println("############ LoanBalancePrincAmt : "+balanceLoanAmt);
                                    double checkAmt = 0.0D;
                                    double totalPrincAmount = 0.0D;
                                    checkAmt = balanceLoanAmt - instalAmt;
                                    if (checkAmt > 0.0D) {
                                        if (!installtype.equals("UNIFORM_PRINCIPLE_EMI") || !emi_uniform.equals("Y")) {
                                            balanceMap.put("ACCT_NUM", actNum);
                                            //balanceMap.put("BALANCE_AMT", String.valueOf(checkAmt));
                                            balanceMap.put("BALANCE_AMT", checkAmt);
                                            List sumInstLst = sqlMap.executeQueryForList("getPrincipalAmtGreaterThanBalAmt", balanceMap);
                                            if (sumInstLst != null && sumInstLst.size() > 0) {
                                                balanceMap = (HashMap) sumInstLst.get(0);
                                                totalPrincAmount = CommonUtil.convertObjToDouble(balanceMap.get("PRINCIPAL_AMOUNT")).doubleValue();
                                                totalPrincAmount += instalAmt;
                                                finalDemandAmt = totalPrincAmount - paidAmount;
                                                if (balanceLoanAmt > finalDemandAmt) {
//                                                    System.out.println("66666666finalDemandAmt" + finalDemandAmt);
                                                    if (finalDemandAmt > 0) {
                                                        dataMap.put("PRINCIPAL", String.valueOf(finalDemandAmt));
                                                    } else {
                                                        dataMap.put("PRINCIPAL", "0");
                                                    }
                                                    principalAmount = finalDemandAmt;
                                                } else {
//                                                    System.out.println("7777777777balanceLoanAmt" + balanceLoanAmt);
                                                    if (balanceLoanAmt > 0) {
                                                        dataMap.put("PRINCIPAL", String.valueOf(balanceLoanAmt));
                                                    } else {
                                                        dataMap.put("PRINCIPAL", "0");
                                                    }
                                                    principalAmount = balanceLoanAmt;
                                                }
                                            }
                                            List aList = ServerUtil.executeQuery("getMorotorium", loanInstall);
                                            String moret = "";
                                            if (aList.size() > 0 && aList.get(0) != null) {
                                                HashMap mapop = (HashMap) aList.get(0);
                                                if (mapop.get("MORATORIUM_GIVEN") != null) {
                                                    moret = mapop.get("MORATORIUM_GIVEN").toString();
                                                }
                                            }
//                                            System.out.println("inst_dt==11==="+inst_dt+"currDt=11======"+currDt);
//                                            System.out.println("totPrrrrrrr11rrrrrrrrrr="+DateUtil.dateDiff(inst_dt, currDt)+"ggggg="+moret);
                                            if (DateUtil.dateDiff(inst_dt, intUptoDt) <= 0L && moret != null && moret.equals("Y")) {
                                                finalDemandAmt = 0.0D;
                                                principalAmount = 0.0D;
                                            }
//                                            System.out.println("############ finalDemandAmt : "+finalDemandAmt);
                                        }
                                    } else {
                                        HashMap transMap = new HashMap();
                                        transMap.put("ACT_NUM", actNum);
                                        transMap.put("BRANCH_CODE", _branchCode);
                                        List sanctionLst = sqlMap.executeQueryForList("getNoOfDaysinLoan", transMap);
                                        if (sanctionLst != null && sanctionLst.size() > 0) {
                                            HashMap recordMap = (HashMap) sanctionLst.get(0);
                                            int repayFreq = 0;
                                            repayFreq = CommonUtil.convertObjToInt(recordMap.get("REPAYMENT_FREQUENCY"));
                                            if (repayFreq == 1) {
                                                java.util.Date expiry_dt = null;
                                                expiry_dt = (java.util.Date) recordMap.get("TO_DT");
                                                expiry_dt = (java.util.Date) expiry_dt.clone();
//                                                System.out.println("########## expiry_dt : "+expiry_dt);
                                                if (DateUtil.dateDiff(intUptoDt, expiry_dt) >= 0L) {
                                                    principalAmount = 0.0D;
//                                                    System.out.println("888888principalAmount" + principalAmount);
                                                    dataMap.put("PRINCIPAL", String.valueOf(principalAmount));
                                                } else {
//                                                    System.out.println("999999balanceLoanAmt" + balanceLoanAmt);
                                                    if (balanceLoanAmt > 0) {
                                                        dataMap.put("PRINCIPAL", String.valueOf(balanceLoanAmt));
                                                    } else {
                                                        dataMap.put("PRINCIPAL", "0");
                                                    }
                                                    principalAmount = balanceLoanAmt;
                                                }
                                            } else {
//                                                System.out.println("101010101010balanceLoanAmt" + balanceLoanAmt);
                                                if (balanceLoanAmt > 0) {
                                                    dataMap.put("PRINCIPAL", String.valueOf(balanceLoanAmt));
                                                } else {
                                                    dataMap.put("PRINCIPAL", "0");
                                                }
                                                principalAmount = balanceLoanAmt;
                                            }
                                        }
                                    }
                                }
                            }
                        }
                        if (prodType.equals("AD")) {
//                            if (retired != null && retired.equals("YES")) {
//                                System.out.println("111222333444clear_balance" + clear_balance);
//                                dataMap.put("PRINCIPAL", String.valueOf(Math.abs(Integer.parseInt(clear_balance))));
//                            } else 
//                            {
                            if (principalAmount > 0.0D) {
//                                    System.out.println("222333444555principalAmount" + principalAmount);
                                dataMap.put("PRINCIPAL", String.valueOf(principalAmount));
                            } else {
//                                    System.out.println("333444555666");
                                dataMap.put("PRINCIPAL", "0");
                            }
//                            }
                        }
                        if (installtype.equals("UNIFORM_PRINCIPLE_EMI") && emi_uniform.equals("Y")) {
                            double dueamount = 0.0D;
                            double penal = 0.0D;
                            double totEmi = 0.0D;
                            double paidEmi = 0.0D;
                            double principle = 0.0D;
                            double interst = Double.parseDouble(insertPenal.get("CURR_MONTH_INT").toString());
//                            System.out.println("interst"+interst);
                            HashMap emi = new HashMap();
                            java.util.Date upto = (java.util.Date) intUptoDt.clone();
                            emi.put("ACC_NUM", actNum);
                            emi.put("UP_TO", getProperDateFormat(upto));
                            List totalEmiList = sqlMap.executeQueryForList("TotalEmi", emi);
                            if (totalEmiList != null && totalEmiList.size() > 0 && ((HashMap) totalEmiList.get(0)).get("TOTAL_AMOUNT") != null) {
                                HashMap aMap = new HashMap();
                                aMap = (HashMap) totalEmiList.get(0);
                                totEmi = Double.parseDouble(aMap.get("TOTAL_AMOUNT").toString());
//                                System.out.println("TOTAL_AMOUNTv"+totEmi);
                            } else {
                                totEmi = 0.0D;
                            }
                            HashMap paid = new HashMap();
                            paid.put("ACT_NUM", actNum);
                            paid.put("BRANCH_CODE", _branchCode);
                            List paidAmtemi = sqlMap.executeQueryForList("getPaidPrincipleEMI", loanInstall);
                            if (paidAmtemi != null && paidAmtemi.size() > 0 && ((HashMap) paidAmtemi.get(0)).get("PRINCIPLE") != null) {
                                paid = (HashMap) paidAmtemi.get(0);
//                                System.out.println("!!!!asAndWhenMap:"+paid);
                                paidEmi = CommonUtil.convertObjToDouble(paid.get("PRINCIPLE")).doubleValue();
//                                System.out.println("paidEmi"+paidEmi);
                            } else {
                                paidEmi = 0.0D;
                            }
//                            System.out.println("totEmi"+totEmi+"paidEmi"+paidEmi);
                            dueamount = totEmi - paidEmi;
                            double paidamount = paidEmi;
                            if (dueamount <= 0.0D) {
                                dueamount = CommonUtil.convertObjToDouble(dataMap.get("PRINCIPAL")).doubleValue() + interst;
                            }
//                            System.out.println("totalDemandsdas"+dueamount);
//                            System.out.println("+==========PENAL STARTS==============================+");
                            List scheduleList = sqlMap.executeQueryForList("getSchedules", emi);
                            java.util.Date penalStrats = new java.util.Date();
                            if (scheduleList != null && scheduleList.size() > 0) {
                                int k = 0;
                                do {
                                    if (k >= scheduleList.size()) {
                                        break;
                                    }
                                    HashMap eachInstall = new HashMap();
                                    eachInstall = (HashMap) scheduleList.get(k);
//                                    System.out.println("eachInstall"+eachInstall);
                                    double scheduledEmi = Double.parseDouble(eachInstall.get("TOTAL_AMT").toString());
                                    if (paidamount >= scheduledEmi) {
//                                        System.out.println("111paidamount"+paidamount+"scheduledEmi"+scheduledEmi);
                                        paidamount -= scheduledEmi;
//                                        System.out.println("paidamount"+paidamount);
                                    } else {
                                        String in_date = CommonUtil.convertObjToStr(eachInstall.get("INSTALLMENT_DT"));
                                        penalStrats = DateUtil.getDateMMDDYYYY(in_date);
//                                        System.out.println("penalStrats....."+penalStrats);
                                        break;
                                    }
                                    k++;
                                } while (true);
                                emi.put("FROM_DATE", penalStrats);
                                List getPenalData = sqlMap.executeQueryForList("getPenalData", emi);
                                List penalInterstRate = sqlMap.executeQueryForList("getPenalIntestRatefromMaintenance", emi);
                                double interstPenal = 0.0D;
                                double garce = 0.0D;
                                List graceDays = sqlMap.executeQueryForList("getGracePeriodDays", emi);
                                if (graceDays != null && graceDays.size() > 0) {
                                    HashMap map = new HashMap();
                                    map = (HashMap) graceDays.get(0);
                                    if (map != null && map.containsKey("GRACE_PERIOD_DAYS") && map.get("GRACE_PERIOD_DAYS") != null) {
                                        garce = Double.parseDouble(map.get("GRACE_PERIOD_DAYS").toString());
                                    } else {
                                        garce = 0.0D;
                                    }
                                } else {
                                    garce = 0.0D;
                                }
                                long gracedy = (long) garce;
                                int graceint = (int) garce;
                                if (penalInterstRate != null && penalInterstRate.size() > 0) {
                                    HashMap test = new HashMap();
                                    test = (HashMap) penalInterstRate.get(0);
                                    if (test != null && test.containsKey("PENAL_INTEREST") && test.get("PENAL_INTEREST") != null) {
                                        interstPenal = Double.parseDouble(test.get("PENAL_INTEREST").toString());
                                    } else {
                                        List limitList = sqlMap.executeQueryForList("getLimitFromLoanSanc", emi);
                                        double limit = Double.parseDouble(((HashMap) limitList.get(0)).get("LIMIT").toString());
                                        emi.put("LIMIT", Double.valueOf(limit));
                                        List penalFromROI = sqlMap.executeQueryForList("getPenalIntestRatefromROI", emi);
                                        if (penalFromROI != null && penalFromROI.size() > 0) {
                                            test = new HashMap();
                                            test = (HashMap) penalFromROI.get(0);
//                                            System.out.println("testttt"+test);
                                            interstPenal = Double.parseDouble(test.get("PENAL_INT").toString());
                                        }
                                    }
                                } else {
                                    List limitList = sqlMap.executeQueryForList("getLimitFromLoanSanc", emi);
                                    double limit = Double.parseDouble(((HashMap) limitList.get(0)).get("LIMIT").toString());
                                    emi.put("LIMIT", Double.valueOf(limit));
                                    List penalFromROI = sqlMap.executeQueryForList("getPenalIntestRatefromROI", emi);
                                    if (penalFromROI != null && penalFromROI.size() > 0) {
                                        HashMap test = new HashMap();
                                        test = (HashMap) penalFromROI.get(0);
                                        interstPenal = Double.parseDouble(test.get("PENAL_INT").toString());
                                    }
                                }
//                                System.out.println("interstPenal...."+interstPenal);
                                if (getPenalData != null && getPenalData.size() > 0) {
                                    for (k = 0; k < getPenalData.size(); k++) {
                                        HashMap amap = new HashMap();
                                        amap = (HashMap) getPenalData.get(k);
                                        String in_date = CommonUtil.convertObjToStr(amap.get("INSTALLMENT_DT"));
                                        java.util.Date currntDate = DateUtil.getDateMMDDYYYY(in_date);
                                        currntDate = DateUtil.addDays(currntDate, graceint);
//                                        System.out.println("5555currntDate.."+currntDate);
                                        HashMap holidayMap = new HashMap();
                                        holidayMap.put("CURR_DATE", currntDate);
                                        holidayMap.put("BRANCH_CODE", _branchCode);
                                        currntDate = CommonUtil.getProperDate(currDt, currntDate);
                                        holidayMap = new HashMap();
                                        boolean checkHoliday = true;
                                        String str = "any next working day";
//                                        System.out.println("instDate   "+currntDate);
                                        currntDate = CommonUtil.getProperDate(currDt, currntDate);
                                        holidayMap.put("NEXT_DATE", currntDate);
                                        holidayMap.put("BRANCH_CODE", _branchCode);
                                        while (checkHoliday) {
                                            boolean tholiday = false;
                                            List Holiday = sqlMap.executeQueryForList("checkHolidayDateOD", holidayMap);
                                            List weeklyOf = sqlMap.executeQueryForList("checkWeeklyOffOD", holidayMap);
                                            boolean isHoliday = Holiday.size() > 0;
                                            boolean isWeekOff = weeklyOf.size() > 0;
                                            if (isHoliday || isWeekOff) {
                                                if (str.equals("any next working day")) {
                                                    currntDate.setDate(currntDate.getDate() + 1);
                                                } else {
                                                    currntDate.setDate(currntDate.getDate() - 1);
                                                }
                                                holidayMap.put("NEXT_DATE", currntDate);
                                                checkHoliday = true;
//                                                System.out.println("#### holidayMap : "+holidayMap);
                                            } else {
                                                checkHoliday = false;
                                            }
                                        }
//                                        System.out.println("currntDatemmm"+currntDate);
//                                        System.out.println("DateUtil.dateDiff(currntDate,upto)"+DateUtil.dateDiff(currntDate, upto));
                                        long difference = DateUtil.dateDiff(currntDate, upto) - 1L;
                                        if (difference < 0L) {
                                            difference = 0L;
                                        }
//                                        System.out.println("difference..."+difference);
                                        double installment = Double.parseDouble(amap.get("TOTAL_AMT").toString());
//                                        System.out.println("installmentsadeasdasd"+installment);
                                        penal += (installment * (double) difference * interstPenal) / 36500D;
//                                        System.out.println("penallcalcuuu"+penal);
                                    }

                                }
                            }
                            principle = dueamount - interst;
                            System.out.println("mmmprinciple"+principle+"::penal"+penal+"::interst"+interst);
                            totalDemand = principle + penal + interst;
                            totalDemand = Math.round(totalDemand);
                            principle = Math.round(principle);
                            penal = Math.round(penal);
                            interst = Math.round(interst);
                            dataMap.put("INTEREST", Long.valueOf(Math.round(Double.parseDouble(insertPenal.get("CURR_MONTH_INT").toString()))));
                            dataMap.put("PENAL", Double.valueOf(penal));
//                            System.out.println("tttttoooo"+totalDemand);
                            if(principle > 0){
                                dataMap.put("PRINCIPAL", principle);
                            }
                            HashMap detailedMap = new HashMap();
                            detailedMap.put("ACCT_NUM", actNum);
                            detailedMap.put("ASONDT", getProperDateFormat(intUptoDt.clone()));
                            detailedMap.put("INSTALL_TYPE", installtype);
                            detailedMap.put("EMI_IN_SIMPLE_INTEREST", emi_uniform);
                            List advanceEmiList = sqlMap.executeQueryForList("getAdvAmtEmi", detailedMap);
                            if(advanceEmiList != null && advanceEmiList.size()>0){
                                HashMap advMap = (HashMap) advanceEmiList.get(0);
                                if(advMap != null && advMap.size()>0 && advMap.containsKey("BALANCE")){
                                    double balance = CommonUtil.convertObjToDouble(advMap.get("BALANCE"));
                                    System.out.println("balance"+balance);
                                    if(balance <= 0){
                                        double interest = CommonUtil.convertObjToDouble(insertPenal.get("CURR_MONTH_INT"));
                                        double insAmt = CommonUtil.convertObjToDouble(dataMap.get("INSTMT_AMT"));
                                        double princi = insAmt - interest;
                                        double princip = CommonUtil.convertObjToDouble(dataMap.get("PRINCIPAL"));
                                        
                                        System.out.println("interest"+interest+"insAmt"+insAmt+"princi"+princi+"princip"+princip);
                                        
                                        if (princi > 0) {
                                            if (princip > 0) {
                                                totalDemand -= princip;
                                                System.out.println("jefff");
                                            }
                                            totalDemand += princi;
                                            dataMap.put("PRINCIPAL", princi);
                                            System.out.println("prasnth"+totalDemand);
                                        }
                                    }
                                }
                            }
                            
                            double insttAmt = CommonUtil.convertObjToDouble(dataMap.get("INSTMT_AMT"));
                            double tempPrin = CommonUtil.convertObjToDouble(dataMap.get("PRINCIPAL"));
                            double tempInt = CommonUtil.convertObjToDouble(dataMap.get("INTEREST"));
                            
                            System.out.println("insttAmt"+insttAmt+"tempPrin"+tempPrin+"tempInt"+tempInt);
                            dataMap.put("CLEAR_BALANCE", clear_balance);

                            double temmmpPrinc = insttAmt - tempInt;
                            System.out.println("temmmpPrinc : "+temmmpPrinc);
                            if(tempPrin < temmmpPrinc){
                                totalDemand -= tempPrin;
                                totalDemand += temmmpPrinc;
                                dataMap.put("PRINCIPAL", temmmpPrinc);
                                System.out.println("prasnth" + totalDemand);
                            }else{
                                HashMap balanceMap = new HashMap(); 
                                balanceMap.put("ACCOUNTNO",actNum);
                                double outstandingAmt = 0;
                                List balannceAmtLst = sqlMap.executeQueryForList("getBalanceLoanPrincipalAmt", balanceMap);
                                if (balannceAmtLst != null && balannceAmtLst.size() > 0) {
                                    balanceMap = (HashMap) balannceAmtLst.get(0);
                                    outstandingAmt = CommonUtil.convertObjToDouble(balanceMap.get("LOAN_BALANCE_PRINCIPAL")).doubleValue();
                                }
                                if(insttAmt<tempInt){
                                    temmmpPrinc = (insttAmt*1) - tempInt;
                                    totalDemand = insttAmt*1+penal;
                                    if(temmmpPrinc<0){
                                        temmmpPrinc = (insttAmt*2) - tempInt;
                                        totalDemand = (insttAmt*2) +penal;                                        
                                    }
                                    dataMap.put("PRINCIPAL", temmmpPrinc);
                                    System.out.println("condition temmmpPrinc : "+temmmpPrinc);
                                }else{
                                    System.out.println("outstandingAmt : "+outstandingAmt);
                                    if(outstandingAmt<insttAmt){
                                        //temmmpPrinc = insttAmt - tempInt;
                                        //totalDemand = insttAmt+penal;
                                        temmmpPrinc = outstandingAmt;
                                        totalDemand = outstandingAmt + penal+tempInt;
                                        dataMap.put("PRINCIPAL", temmmpPrinc);
                                        dataMap.put("CLEAR_BALANCE", new Double(outstandingAmt*-1));
                                        System.out.println("outstandingAmt condition temmmpPrinc : "+temmmpPrinc);
                                    }else{
                                        temmmpPrinc = insttAmt - tempInt;
                                        totalDemand = insttAmt+penal;
                                        dataMap.put("PRINCIPAL", temmmpPrinc);
                                        System.out.println("else outstandingAmt temmmpPrinc : "+temmmpPrinc);
                                    }
                                }
                            }
                            
                                double princHere = CommonUtil.convertObjToDouble(dataMap.get("PRINCIPAL"));
                                if(princHere > clearBal){
                                    totalDemand-=princHere;
                                    totalDemand+=clearBal;
                                    dataMap.put("PRINCIPAL", clearBal);
                                }
                            
                            
                            if (totalDemand > 0) {
                                dataMap.put("TOTAL_DEMAND", Math.round(CommonUtil.convertObjToDouble(totalDemand)));
                            } else {
                                dataMap.put("TOTAL_DEMAND", "0");
                                dataMap = null;
                                return dataMap;
                            }
//                            System.out.println("mmmmmmiiinnneee"+dataMap);
                        } else {
                            totalDemand += principalAmount;
                            double princHere = CommonUtil.convertObjToDouble(dataMap.get("PRINCIPAL"));
                            if(princHere > clearBal){
                                totalDemand-=princHere;
                                totalDemand+=clearBal;
                                dataMap.put("PRINCIPAL", clearBal);
                            }
                            dataMap.put("INTEREST", Long.valueOf(Math.round(Double.parseDouble(insertPenal.get("CURR_MONTH_INT").toString()))));
                            dataMap.put("PENAL", insertPenal.get("PENAL_INT"));
                            if (totalDemand > 0) {
                                dataMap.put("TOTAL_DEMAND", Math.round(CommonUtil.convertObjToDouble(totalDemand)));
                            } else {
                                dataMap.put("TOTAL_DEMAND", "0");
                                dataMap = null;
                                return dataMap;
                            }
                            dataMap.put("CLEAR_BALANCE", clear_balance);
                        }
                        if (flag) {
                            System.out.println("444555666777");
                            dataMap.put("PRINCIPAL", Integer.valueOf(0));
                            totalDemand -= principalAmount;
                            if (totalDemand > 0) {
                                dataMap.put("TOTAL_DEMAND", Math.round(CommonUtil.convertObjToDouble(totalDemand)));
                            } else {
                                dataMap.put("TOTAL_DEMAND", "0");
                                dataMap = null;
                                return dataMap;
                            }
                            flag = false;
                        }
                    }
                    if (prodType != null && !prodType.equals("") && prodType.equals("AD")) {
                        double totalDemand = 0.0;
                        HashMap intMap = new HashMap();
                        intMap.put("AS_ON_DATE", getProperDateFormat(intUptoDt.clone()));
                        intMap.put("ACCT_NUM", CommonUtil.convertObjToStr(actNum));
                        List penalADList = sqlMap.executeQueryForList("TransAll.getPenalAmountForAD", intMap);
                        if (penalADList != null && penalADList.size() > 0) {
                            HashMap intDetailMap = (HashMap) penalADList.get(0);
                            System.out.println("intDetailMap" + intDetailMap);
                            if (intDetailMap != null && intDetailMap.size() > 0 && intDetailMap.containsKey("PENAL")) {
                                double penal = CommonUtil.convertObjToDouble(intDetailMap.get("PENAL"));
                                if (penal > 0) {
                                    if (dataMap.containsKey("INTEREST")) {
                                        totalDemand = CommonUtil.convertObjToDouble(dataMap.get("TOTAL_DEMAND"));
                                        double tempPenal = CommonUtil.convertObjToDouble(dataMap.get("PENAL"));
                                        double tempInterest  = CommonUtil.convertObjToDouble(dataMap.get("INTEREST"));
                                        if (tempInterest > 0) {
                                            totalDemand -= tempPenal;
                                            dataMap.remove("PENAL");
                                            dataMap.put("PENAL", penal);
                                            totalDemand = CommonUtil.convertObjToDouble(dataMap.get("TOTAL_DEMAND"));
                                            totalDemand += penal;
                                            if (totalDemand > 0) {
                                                dataMap.put("TOTAL_DEMAND", Math.round(CommonUtil.convertObjToDouble(totalDemand)));
                                            } else {
                                                dataMap.put("TOTAL_DEMAND", "0");
                                                dataMap = null;
                                                return dataMap;
                                            }
                                        }
                                    }

                                } else {
                                    dataMap.put("PENAL", "0");
                                }
                            }
                        }
                    }
                    if (dataMap != null && prodType != null && prodType.equals("TL")) {
                       if (resltList != null && resltList.size() > 0) {
                           System.out.println("dataMap ***SSSS******* ZZZ :" + dataMap);
                           double penalVal = CommonUtil.convertObjToDouble(dataMap.get("PENAL"));
                           double demandVal = 0;
                           if (princVal >= 0) {
                               demandVal = princVal + penalVal + intVal;
                           } else {
                               demandVal = penalVal + intVal;
                           }
                           System.out.println("totalDemand : " + demandVal);
                           dataMap.put("INTEREST", intVal);
                           if (princVal >= 0) {
                               dataMap.put("PRINCIPAL", princVal);
                           }
                           if (demandVal > 0) {
                               dataMap.put("TOTAL_DEMAND", demandVal);
                           } else {
                               dataMap.put("TOTAL_DEMAND", "0");
                               dataMap = null;
                               return dataMap;
                           }
                           System.out.println("dataMap ****ZZZZZZZZZZZZZ****** aaa :" + dataMap);
                       }
                    }
                } else {
//                    System.out.println("### Not Allow : "+checkDate);
                    dataMap = null;
                }
            }//Jeffu
            }
        }
        }
//        System.out.println("####### Single Row DataMap : "+dataMap);
        System.out.println("dataMapdataMapdataMapdataMap" + dataMap);
        return dataMap;
    }
    
    public HashMap calcGL(String actNo, String schemeName, String prodType, double glAmount) throws Exception {
        Double penal = 0.0;
        if (prodType.equalsIgnoreCase("SA")) {
            HashMap map1 = new HashMap();
            map1.put("ACT_NUM", actNo);
//            map1.put("INSTALLMENT_DATE", currDt1);
            map1.put("ASONDATE", getProperDateFormat(intUptoDt.clone()));
            HashMap lstDueMap = new HashMap();
            List lstDue = sqlMap.executeQueryForList("getSuspenseCalculationinAccounts", map1);
            if(lstDue != null && lstDue.size()>0){
                penal = 0.0;
                lstDueMap = (HashMap) lstDue.get(0);
                if(lstDueMap != null && lstDueMap.size()>0 && lstDueMap.containsKey("INTEREST")){
                    penal = CommonUtil.convertObjToDouble(lstDueMap.get("INTEREST"));
                }
            }
        }
        HashMap dataMap = new HashMap();
        dataMap.put("PRINCIPAL", new Double((glAmount)));
        dataMap.put("TOTAL_DEMAND", new Double((glAmount + penal)));
        dataMap.put("INTEREST", new Double(penal));
        dataMap.put("PENAL", new Double(0));
        dataMap.put("CHARGES", new Double(0));
        dataMap.put("CLEAR_BALANCE", new Double(0));
        return dataMap;
    }

    public HashMap calcMDS(String chittal_No, String schemeName, String prodType) throws Exception {
        HashMap dataMap = new HashMap();
        HashMap whereMap = new HashMap();
        HashMap productMap = new HashMap();
        HashMap installmentMap = new HashMap();
        String chittalNo = "";
        String subNo = "";
        if (chittal_No.indexOf("_") != -1) {
            chittalNo = chittal_No.substring(0, chittal_No.indexOf("_"));
            subNo = chittal_No.substring(chittal_No.indexOf("_") + 1, chittal_No.length());
        }
        dataMap.put("CHITTAL_NO", chittal_No);
        dataMap.put("SCHEME_NAME", schemeName);
        System.out.println("ANEEZ********chittal_No" + chittal_No);
        System.out.println("ANEEZ********SCHEMENAME" + dataMap.get("SCHEME_NAME"));
        double insAmt = 0.0;
        long pendingInst = 0;
        int divNo = 0;
        long insDueAmt = 0;
        int noOfInsPaid = 0;
        int instDay = 1;
        boolean prized = false;
        int curInsNo = 0;
        Date instDate = null;
        boolean bonusAvailabe = true;
        ArrayList penalList = new ArrayList();
        ArrayList bonusList = new ArrayList();
        ArrayList discountList = new ArrayList();

        long noOfInstPay = 0;
        int totIns = 0;
        Date startDate = null;
        Date insDate = null;
        int startMonth = 0;
        int insMonth = 0;
        int interestDay = 0;
        whereMap.put("SCHEME_NAME", dataMap.get("SCHEME_NAME"));
        whereMap.put("CHITTAL_NO", chittalNo);
        whereMap.put("SUB_NO", CommonUtil.convertObjToInt(subNo));
        List lst = sqlMap.executeQueryForList("getSelectSchemeAcctHead", whereMap);
        System.out.println("Aneez List::---->" + lst);
        String bonusFirstInst = "";
        if (lst != null && lst.size() > 0) {
            productMap = (HashMap) lst.get(0);
            insAmt = CommonUtil.convertObjToDouble(productMap.get("INSTALLMENT_AMOUNT")).doubleValue();
            interestDay = CommonUtil.convertObjToInt(productMap.get("INSTALLMENT_DAY"));
            totIns = CommonUtil.convertObjToInt(productMap.get("NO_OF_INSTALLMENTS"));
            startDate = DateUtil.getDateMMDDYYYY(CommonUtil.convertObjToStr(productMap.get("SCHEME_START_DT")));
            insDate = DateUtil.getDateMMDDYYYY(CommonUtil.convertObjToStr(productMap.get("SCHEME_START_DT")));
            startMonth = insDate.getMonth();
            //            bonusFirstInst = CommonUtil.convertObjToStr(productMap.get("BONUS_FIRST_INSTALLMENT"));
            bonusFirstInst = CommonUtil.convertObjToStr(productMap.get("ADVANCE_COLLECTION"));
        }
        Date endDate = DateUtil.getDateMMDDYYYY(CommonUtil.convertObjToStr(productMap.get("SCHEME_END_DT")));
        int startNoForPenal = 0;
        int addNo = 1;
        int firstInst_No = -1;
        if (bonusFirstInst.equals("Y")) {
            startNoForPenal = 1;
            addNo = 0;
            firstInst_No = 0;
        }
        List insList = sqlMap.executeQueryForList("getNoOfInstalmentsPaid", whereMap);
        if (insList != null && insList.size() > 0) {
            whereMap = (HashMap) insList.get(0);
            noOfInsPaid = CommonUtil.convertObjToInt(whereMap.get("NO_OF_INST"));
        }
        HashMap chittalMap = new HashMap();
        chittalMap.put("CHITTAL_NO", chittalNo);
        chittalMap.put("SUB_NO", CommonUtil.convertObjToInt(subNo));
        List chitLst = sqlMap.executeQueryForList("getSelctApplnReceiptDetails", chittalMap);
        if (chitLst != null && chitLst.size() > 0) {
            chittalMap = (HashMap) chitLst.get(0);
            instDay = CommonUtil.convertObjToInt(chittalMap.get("INSTALLMENT_DAY"));
            divNo = CommonUtil.convertObjToInt(chittalMap.get("DIVISION_NO"));
        }
        
        boolean notPaidThisMonth = false;
        HashMap mdsMap = new HashMap();
        mdsMap.put("CHITTAL_NO", chittalNo);
        List depositMaxList = sqlMap.executeQueryForList("getSelectMDSMaxtransDt", mdsMap);
        if (depositMaxList != null && depositMaxList.size() > 0) {
            mdsMap = (HashMap) depositMaxList.get(0);
         //   Date date = (Date) currDt.clone();
             Date date = (Date) intUptoDt.clone();
             System.out.println("intUptoDt-------- :"+intUptoDt);
            Date maxDate = getProperDateFormat(CommonUtil.convertObjToStr(mdsMap.get("TRANS_DT")));
            int currentMonth = (int)date.getMonth()+1;
            int maxTxnMonth = (int)maxDate.getMonth()+1;
            int currentYear = (int)date.getYear()+1900;
            int maxTxnYear = (int)maxDate.getYear()+1900;
            System.out.println("curr date month : " + currentMonth + " last transaction maxDate : " + maxTxnMonth +" curr year : " + currentYear + " last transaction year : " + maxTxnYear);
            if ((maxTxnMonth != currentMonth) && (maxTxnYear == currentYear)) {
                notPaidThisMonth = true;
                System.out.println("Please Continue make payment nextmonth : " + chittalNo);
            }else{
                notPaidThisMonth = false;
            }
        }else{
            notPaidThisMonth = true;
        }
        
        HashMap insDateMap = new HashMap();
        insDateMap.put("DIVISION_NO", CommonUtil.convertObjToInt(String.valueOf(divNo)));
        insDateMap.put("SCHEME_NAME", dataMap.get("SCHEME_NAME"));
        insDateMap.put("CURR_DATE", CommonUtil.getProperDate(currDt,intUptoDt));
        insDateMap.put("ADD_MONTHS", CommonUtil.convertObjToInt("-1"));
        List insDateLst = sqlMap.executeQueryForList("getMDSCurrentInsDate", insDateMap);
        if (insDateLst != null && insDateLst.size() > 0) {
            insDateMap = (HashMap) insDateLst.get(0);
            curInsNo = CommonUtil.convertObjToInt(insDateMap.get("INSTALLMENT_NO"));
            pendingInst = curInsNo - noOfInsPaid;
            if (pendingInst < 0) {
                pendingInst = 0;
            }
            insMonth = startMonth + curInsNo;
            insDate.setMonth(insMonth);
            if (pendingInst > 0) {
                noOfInstPay = pendingInst;
            }
            System.out.println("######## NoOfInstToPay *** : " + noOfInstPay + " chittalNo " + chittalNo);
        }
        if (noOfInstPay > 0 && notPaidThisMonth) {
            HashMap prizedMap = new HashMap();
            prizedMap.put("SCHEME_NAME", dataMap.get("SCHEME_NAME"));
            prizedMap.put("DIVISION_NO", String.valueOf(divNo));
            prizedMap.put("CHITTAL_NO", dataMap.get("CHITTAL_NO"));
            prizedMap.put("SUB_NO", CommonUtil.convertObjToInt(subNo));
            lst = sqlMap.executeQueryForList("getSelectPrizedDetailsEntryRecords", prizedMap);
            if (lst != null && lst.size() > 0) {
                prizedMap = (HashMap) lst.get(0);
                if (prizedMap.get("DRAW") != null && !prizedMap.get("DRAW").equals("") && prizedMap.get("DRAW").equals("Y")) {
                    prized = true;
                }
                if (prizedMap.get("AUCTION") != null && !prizedMap.get("AUCTION").equals("") && prizedMap.get("AUCTION").equals("Y")) {
                    prized = true;
                }
            } else {
                prized = false;
            }
            long totDiscAmt = 0;
            long penalAmt = 0;
            double netAmt = 0;
            double insAmtPayable = 0;
            double totBonusAmt = 0;
            double totalBonusAmt = 0;
            double bonusAmt = 0;
            String penalIntType = "";
            long penalValue = 0;
            String penalGraceType = "";
            long penalGraceValue = 0;
            String penalCalcBaseOn = "";
            long diffDayPending = 0;

            if (pendingInst > 0) {              //pending installment calculation starts...
                insDueAmt = (long) insAmt * pendingInst;
                double calc = 0;
                long totInst = pendingInst;
                penalCalcBaseOn = CommonUtil.convertObjToStr(productMap.get("PENAL_CALC"));
                if (prized == true) {
                    penalIntType = CommonUtil.convertObjToStr(productMap.get("PENAL_PRIZED_INT_TYPE"));
                    penalValue = CommonUtil.convertObjToLong(productMap.get("PENAL_PRIZED_INT_AMT"));
                    penalGraceType = CommonUtil.convertObjToStr(productMap.get("PENAL_PRIZED_GRACE_PERIOD_TYPE"));
                    penalGraceValue = CommonUtil.convertObjToLong(productMap.get("PENAL_PRIZED_GRACE_PERIOD"));
                } else if (prized == false) {
                    penalIntType = CommonUtil.convertObjToStr(productMap.get("PENAL_INT_TYPE"));
                    penalValue = CommonUtil.convertObjToLong(productMap.get("PENAL_INT_AMT"));
                    penalGraceType = CommonUtil.convertObjToStr(productMap.get("PENAL_GRACE_PERIOD_TYPE"));
                    penalGraceValue = CommonUtil.convertObjToLong(productMap.get("PENAL_GRACE_PERIOD"));
                }
                for (int j = startNoForPenal; j < noOfInstPay + startNoForPenal; j++) {
                    HashMap nextInstMap = new HashMap();
                    nextInstMap.put("SCHEME_NAME", dataMap.get("SCHEME_NAME"));
                    nextInstMap.put("DIVISION_NO", String.valueOf(divNo));
                    nextInstMap.put("SL_NO", new Double(j + noOfInsPaid));
                    List listRec = sqlMap.executeQueryForList("getSelectNextInstDate", nextInstMap);
                    if (listRec != null && listRec.size() > 0) {
                        double penal = 0;
                        nextInstMap = (HashMap) listRec.get(0);
                        instDate = DateUtil.getDateMMDDYYYY(CommonUtil.convertObjToStr(nextInstMap.get("NEXT_INSTALLMENT_DATE")));
                        totalBonusAmt = CommonUtil.convertObjToDouble(nextInstMap.get("NEXT_BONUS_AMOUNT"));
                        double installmentAmt = insAmt-totalBonusAmt;
                        System.out.println("installmentAmt"+installmentAmt);
                        if (interestDay > 0) {
                            instDate.setDate(interestDay);
                        }
                        //                    diffDayPending = DateUtil.dateDiff(instDate, currDt);
                        diffDayPending = DateUtil.dateDiff(instDate, intUptoDt);
                        System.out.println("First #########diffDay : " + diffDayPending + "### instDate : " + instDate + "####  intUptoDt : " + intUptoDt);
                        //Holiday Checking - Added By Suresh
                        HashMap holidayMap = new HashMap();
                        boolean checkHoliday = true;
                        System.out.println("instDate   " + instDate);
                        instDate = CommonUtil.getProperDate(currDt,instDate);
                        holidayMap.put("NEXT_DATE", instDate);
                        holidayMap.put("BRANCH_CODE", _branchCode);
                        while (checkHoliday) {
                            boolean tholiday = false;
                            List Holiday = sqlMap.executeQueryForList("checkHolidayDateOD", holidayMap);
                            List weeklyOf = sqlMap.executeQueryForList("checkWeeklyOffOD", holidayMap);
                            boolean isHoliday = Holiday.size() > 0 ? true : false;
                            boolean isWeekOff = weeklyOf.size() > 0 ? true : false;
                            if (isHoliday || isWeekOff) {
                                System.out.println("#### diffDayPending Holiday True : " + diffDayPending);
                                if (CommonUtil.convertObjToStr(productMap.get("HOLIDAY_INT")).equals("any next working day")) {
                                    diffDayPending -= 1;
                                    instDate.setDate(instDate.getDate() + 1);
                                } else {
                                    diffDayPending += 1;
                                    instDate.setDate(instDate.getDate() - 1);
                                }
                                holidayMap.put("NEXT_DATE", instDate);
                                checkHoliday = true;
                                System.out.println("#### holidayMap : " + holidayMap);
                            } else {
                                System.out.println("#### diffDay Holiday False : " + diffDayPending);
                                checkHoliday = false;
                            }
                        }
                        System.out.println("#### diffDayPending Final : " + diffDayPending);




                        if (penalCalcBaseOn != null && !penalCalcBaseOn.equals("") && penalCalcBaseOn.equals("Days")) {
                            if (penalGraceType != null && !penalGraceType.equals("") && penalGraceType.equals("Days")) {
                                if (diffDayPending > penalGraceValue) {
                                    if (penalIntType != null && !penalIntType.equals("") && penalIntType.equals("Percent")) {
                                        calc += (diffDayPending * penalValue * installmentAmt) / 36500;
                                    } else if (penalIntType != null && !penalIntType.equals("") && penalIntType.equals("Absolute")) {
                                        calc += penalValue;
                                    }
                                }
                            } else if (penalGraceType != null && !penalGraceType.equals("") && penalGraceType.equals("Installments")) {
                                // To be written
                                if (diffDayPending > penalGraceValue) {
                                    if (penalIntType != null && !penalIntType.equals("") && penalIntType.equals("Percent")) {
                                        //                                                calc = insAmt*(((pendingInst+1)*pendingInst/2)*penalValue)/1200;
                                        calc += (double) ((installmentAmt * penalValue) / 1200.0) * pendingInst--;
                                    } else if (penalIntType != null && !penalIntType.equals("") && penalIntType.equals("Absolute")) {
                                        calc += penalValue;
                                    }
                                }
                            }
                        } else if (penalCalcBaseOn != null && !penalCalcBaseOn.equals("") && penalCalcBaseOn.equals("Installments")) {
                            if (penalGraceType != null && !penalGraceType.equals("") && penalGraceType.equals("Days")) {
                                if (diffDayPending > penalGraceValue) {
                                    if (penalIntType != null && !penalIntType.equals("") && penalIntType.equals("Percent")) {
                                        calc += ((installmentAmt * penalValue) / 1200.0) * pendingInst--;
                                    } else if (penalIntType != null && !penalIntType.equals("") && penalIntType.equals("Absolute") && totInst <= noOfInstPay) {
                                        calc += penalValue;
                                    }
                                }
                            } else if (penalGraceType != null && !penalGraceType.equals("") && penalGraceType.equals("Installments")) {
                                // To be written
                                if (diffDayPending > penalGraceValue) {
                                    if (penalIntType != null && !penalIntType.equals("") && penalIntType.equals("Percent")) {
                                        //                                                calc = insAmt*(((pendingInst+1)*pendingInst/2)*penalValue)/1200;
                                        calc += (double) ((installmentAmt * penalValue) / 1200.0) * pendingInst--;
                                    } else if (penalIntType != null && !penalIntType.equals("") && penalIntType.equals("Absolute")) {
                                        calc += penalValue;
                                    }
                                }
                            }
                        }
                        //After Scheme End Date Penal Calculating
                        if ((j + 1 == noOfInstPay + startNoForPenal) && !(penalCalcBaseOn.equals("Days") && penalIntType.equals("Percent")) && (DateUtil.dateDiff(endDate, intUptoDt) > 0)) {
                            System.out.println("#### endDate : " + endDate);
                            if (penalIntType.equals("Percent")) {
                                diffDayPending = DateUtil.dateDiff(endDate, intUptoDt);
                                System.out.println("#### endDate_diffDayPending : " + diffDayPending);
                                calc += (double) ((((installmentAmt * noOfInstPay * penalValue) / 100.0) * diffDayPending) / 365);
                            }
                            // Absolute Not Required...
                        }
                        penal = (long) (calc + 0.5) - penal;
                        penalList.add(String.valueOf(penal));
                        installmentMap.put("PENAL", penalList);
                        penal = calc + 0.5;

                    }
                }
                if (calc > 0) {
                    penalAmt = (long) (calc + 0.5);
                }
            }//pending installment calculation ends...
            //Discount calculation details Starts...
            for (int k = 0; k < noOfInstPay; k++) {
                HashMap nextInstMap = new HashMap();
                nextInstMap.put("SCHEME_NAME", dataMap.get("SCHEME_NAME"));
                nextInstMap.put("DIVISION_NO", String.valueOf(divNo));
                nextInstMap.put("SL_NO", new Double(k + noOfInsPaid));
                List listRec = sqlMap.executeQueryForList("getSelectNextInstDate", nextInstMap);
                if (listRec == null || listRec.isEmpty()) {
                    Date curDate = (Date) currDt.clone();
                    int curMonth = curDate.getMonth();
                    System.out.println("@#$$#$#instDay" + instDay);
                    curDate.setMonth(curMonth + 1);
                    curDate.setDate(instDay);
                    listRec = new ArrayList();
                    nextInstMap.put("NEXT_INSTALLMENT_DATE", curDate);
                    listRec.add(nextInstMap);
                }
                if (listRec != null && listRec.size() > 0) {
                    nextInstMap = (HashMap) listRec.get(0);
                    instDate = DateUtil.getDateMMDDYYYY(CommonUtil.convertObjToStr(nextInstMap.get("NEXT_INSTALLMENT_DATE")));
                    //                long diffDay = DateUtil.dateDiff(instDate, currDt);
                    long diffDay = DateUtil.dateDiff(instDate, intUptoDt);
                    //                System.out.println("First #########diffDay : " + diffDay);
                    String hoildayInt = CommonUtil.convertObjToStr(productMap.get("HOLIDAY_INT"));
                    if (productMap.get("BONUS_ALLOWED") != null && !productMap.get("BONUS_ALLOWED").equals("") && (productMap.get("BONUS_ALLOWED").equals("Y")
                            || productMap.get("BONUS_ALLOWED").equals("N"))) {


                        String discount = CommonUtil.convertObjToStr(productMap.get("DISCOUNT_ALLOWED"));
                        if (discount != null && !discount.equals("") && discount.equals("Y")) {
                            String discountType = CommonUtil.convertObjToStr(productMap.get("DISCOUNT_RATE_TYPE"));
                            long discountValue = CommonUtil.convertObjToLong(productMap.get("DISCOUNT_RATE_AMT"));
                            if (prized == true) {//discount calculation for prized prerson...
                                String discountPrizedDays = CommonUtil.convertObjToStr(productMap.get("DIS_PRIZED_GRACE_PERIOD_DAYS"));
                                String discountPrizedMonth = CommonUtil.convertObjToStr(productMap.get("DIS_PRIZED_GRACE_PERIOD_MONTHS"));
                                String discountPrizedAfter = CommonUtil.convertObjToStr(productMap.get("DIS_PRIZED_GRACE_PERIOD_AFTER"));
                                String discountPrizedEnd = CommonUtil.convertObjToStr(productMap.get("DIS_PRIZED_GRACE_PERIOD_END"));
                                long discountPrizedValue = CommonUtil.convertObjToLong(productMap.get("DIS_PRIZED_GRACE_PERIOD"));
                                if (discountPrizedDays != null && !discountPrizedDays.equals("") && discountPrizedDays.equals("D") && diffDay <= discountPrizedValue) {
                                    if (discountType != null && !discountType.equals("") && discountType.equals("Percent")) {
                                        long calc = discountValue * (long) insAmt / 100;
                                        if (diffDay <= discountPrizedValue) {
                                            totDiscAmt = totDiscAmt + calc;
                                        }
                                    } else if (discountType != null && !discountType.equals("") && discountType.equals("Absolute")) {
                                        if (diffDay <= discountPrizedValue) {
                                            totDiscAmt = totDiscAmt + discountValue;
                                        }
                                    }
                                } else if (discountPrizedMonth != null && !discountPrizedMonth.equals("") && discountPrizedMonth.equals("M") && diffDay <= (discountPrizedValue * 30)) {
                                    if (discountType != null && !discountType.equals("") && discountType.equals("Percent")) {
                                        long calc = discountValue * (long) insAmt / 100;
                                        totDiscAmt = totDiscAmt + calc;
                                    } else if (discountType != null && !discountType.equals("") && discountType.equals("Absolute")) {
                                        totDiscAmt = totDiscAmt + discountValue;
                                    }
                                } else if (discountPrizedAfter != null && !discountPrizedAfter.equals("") && discountPrizedAfter.equals("A") && intUptoDt.getDate() <= discountPrizedValue) {
                                    if (discountType != null && !discountType.equals("") && discountType.equals("Percent")) {
                                        long calc = discountValue * (long) insAmt / 100;
                                        totDiscAmt = totDiscAmt + calc;
                                    } else if (discountType != null && !discountType.equals("") && discountType.equals("Absolute")) {
                                        totDiscAmt = totDiscAmt + discountValue;
                                    }
                                } else if (discountPrizedEnd != null && !discountPrizedEnd.equals("") && discountPrizedEnd.equals("E") && pendingInst < noOfInstPay) {
                                    if (discountType != null && !discountType.equals("") && discountType.equals("Percent")) {
                                        long calc = discountValue * (long) insAmt / 100;
                                        totDiscAmt = totDiscAmt + calc;
                                    } else if (discountType != null && !discountType.equals("") && discountType.equals("Absolute")) {
                                        totDiscAmt = totDiscAmt + discountValue;
                                    }
                                } else {
                                    totDiscAmt = 0;
                                }
                            } else if (prized == false) {//discount calculation non prized person...
                                String discountGraceDays = CommonUtil.convertObjToStr(productMap.get("DIS_GRACE_PERIOD_DAYS"));
                                String discountGraceMonth = CommonUtil.convertObjToStr(productMap.get("DIS_GRACE_PERIOD_MONTHS"));
                                String discountGraceAfter = CommonUtil.convertObjToStr(productMap.get("DIS_GRACE_PERIOD_AFTER"));
                                String discountGraceEnd = CommonUtil.convertObjToStr(productMap.get("DIS_GRACE_PERIOD_END"));
                                long discountGraceValue = CommonUtil.convertObjToLong(productMap.get("DIS_GRACE_PERIOD"));
                                if (discountGraceDays != null && !discountGraceDays.equals("") && discountGraceDays.equals("D")) {
                                    if (discountType != null && !discountType.equals("") && discountType.equals("Percent")) {
                                        long calc = discountValue * (long) insAmt / 100;
                                        if (diffDay <= discountGraceValue) {
                                            totDiscAmt = totDiscAmt + calc;
                                        }
                                    } else if (discountType != null && !discountType.equals("") && discountType.equals("Absolute")) {
                                        if (diffDay <= discountGraceValue) {
                                            totDiscAmt = totDiscAmt + discountValue;
                                        }
                                    } else {
                                        totDiscAmt = 0;
                                    }
                                } else if (discountGraceDays != null && !discountGraceDays.equals("") && discountGraceDays.equals("M") && diffDay <= discountGraceValue * 30 && pendingInst < noOfInstPay) {
                                    if (discountType != null && !discountType.equals("") && discountType.equals("Percent")) {
                                        long calc = discountValue * (long) insAmt / 100;
                                        totDiscAmt = totDiscAmt + calc;
                                    } else if (discountType != null && !discountType.equals("") && discountType.equals("Absolute")) {
                                        totDiscAmt = totDiscAmt + discountValue;
                                    }
                                } else if (discountGraceDays != null && !discountGraceDays.equals("") && discountGraceDays.equals("A") && intUptoDt.getDate() <= discountGraceValue && pendingInst < noOfInstPay) {
                                    if (discountType != null && !discountType.equals("") && discountType.equals("Percent")) {
                                        long calc = discountValue * (long) insAmt / 100;
                                        totDiscAmt = totDiscAmt + calc;
                                    } else if (discountType != null && !discountType.equals("") && discountType.equals("Absolute")) {
                                        totDiscAmt = totDiscAmt + discountValue;
                                    }
                                } else if (discountGraceDays != null && !discountGraceDays.equals("") && discountGraceDays.equals("E") && pendingInst < noOfInstPay) {
                                    if (discountType != null && !discountType.equals("") && discountType.equals("Percent")) {
                                        long calc = discountValue * (long) insAmt / 100;
                                        totDiscAmt = totDiscAmt + calc;
                                    } else if (discountType != null && !discountType.equals("") && discountType.equals("Absolute")) {
                                        totDiscAmt = totDiscAmt + discountValue;
                                    }
                                } else {
                                    totDiscAmt = 0;
                                }
                            }
                        } else if (discount != null && !discount.equals("") && discount.equals("N")) {
                            totDiscAmt = 0;
                        }
                    }

                }
            }

            //Bonus calculation details Starts...
            for (int l = startNoForPenal; l <= noOfInstPay - addNo; l++) {
                HashMap nextInstMap = new HashMap();
                nextInstMap.put("SCHEME_NAME", dataMap.get("SCHEME_NAME"));
                nextInstMap.put("DIVISION_NO", divNo);
                nextInstMap.put("SL_NO", new Double(l + noOfInsPaid + addNo + firstInst_No));
                List listRec = sqlMap.executeQueryForList("getSelectBonusAndNextInstDateWithoutDivision", nextInstMap);
                if (listRec == null || listRec.isEmpty()) {
                    //                Date curDate = (Date)currDt.clone();
                    Date curDate = (Date) intUptoDt.clone();
                    int curMonth = curDate.getMonth();
                    System.out.println("@#$$#$#instDay" + instDay);
                    curDate.setMonth(curMonth + 1);
                    curDate.setDate(instDay);
                    listRec = new ArrayList();
                    nextInstMap.put("NEXT_INSTALLMENT_DATE", curDate);
                    listRec.add(nextInstMap);
                    bonusAvailabe = false;
                }
                if (listRec != null && listRec.size() > 0) {
                    nextInstMap = (HashMap) listRec.get(0);
                    instDate = DateUtil.getDateMMDDYYYY(CommonUtil.convertObjToStr(nextInstMap.get("NEXT_INSTALLMENT_DATE")));
                    bonusAmt = CommonUtil.convertObjToDouble(CommonUtil.convertObjToStr(nextInstMap.get("NEXT_BONUS_AMOUNT"))).doubleValue();
                    //                long diffDay = DateUtil.dateDiff(instDate, currDt);
                    if (productMap.get("BONUS_ROUNDING") != null && !productMap.get("BONUS_ROUNDING").equals("") && productMap.get("BONUS_ROUNDING").equals("Y")
                            && bonusAmt > 0) {
                        Rounding rod = new Rounding();
                        if (productMap.get("BONUS_ROUNDOFF") != null && productMap.get("BONUS_ROUNDOFF").equals("NEAREST_VALUE")) {
                            bonusAmt = (double) rod.getNearest((long) (bonusAmt * 100), 100) / 100;
                        } else {
                            bonusAmt = (double) rod.lower((long) (bonusAmt * 100), 100) / 100;
                        }
                    }
                    long diffDay = DateUtil.dateDiff(instDate, intUptoDt);
                    //                System.out.println("First #########diffDay : " + diffDay);
                    String hoildayInt = CommonUtil.convertObjToStr(productMap.get("HOLIDAY_INT"));
                    if (productMap.get("BONUS_ALLOWED") != null && !productMap.get("BONUS_ALLOWED").equals("") && (productMap.get("BONUS_ALLOWED").equals("Y")
                            || productMap.get("BONUS_ALLOWED").equals("N"))) {
                        if (bonusAvailabe == true) {
                            if (prized == true) {
                                String bonusPrizedDays = CommonUtil.convertObjToStr(productMap.get("BONUS_PRIZED_GRACE_PERIOD_DAYS"));
                                String bonusPrizedMonth = CommonUtil.convertObjToStr(productMap.get("BONUS_PRIZED_GRACE_PERIOD_MNTH"));
                                String bonusPrizedAfter = CommonUtil.convertObjToStr(productMap.get("BONUS_PRIZED_GRACE_PERIOD_AFT"));
                                String bonusPrizedEnd = CommonUtil.convertObjToStr(productMap.get("BONUS_PRIZED_GRACE_PERIOD_END"));
                                long bonusPrizedValue = CommonUtil.convertObjToLong(productMap.get("BONUS_PRIZED_GRACE_PERIOD"));
                                if (bonusPrizedDays != null && !bonusPrizedDays.equals("") && bonusPrizedDays.equals("D") && diffDay <= bonusPrizedValue) {
                                    totBonusAmt = totBonusAmt + bonusAmt;
                                } else if (bonusPrizedMonth != null && !bonusPrizedMonth.equals("") && bonusPrizedMonth.equals("M") && diffDay <= (bonusPrizedValue * 30)) {
                                    totBonusAmt = totBonusAmt + bonusAmt;
                                } else if (bonusPrizedAfter != null && !bonusPrizedAfter.equals("") && bonusPrizedAfter.equals("A") && intUptoDt.getDate() <= bonusPrizedValue) {
                                    totBonusAmt = totBonusAmt + bonusAmt;
                                } else if (bonusPrizedEnd != null && !bonusPrizedEnd.equals("") && bonusPrizedEnd.equals("E")) {
                                } else {
                                }
                            } else if (prized == false) {
                                String bonusGraceDays = CommonUtil.convertObjToStr(productMap.get("BONUS_GRACE_PERIOD_DAYS"));
                                String bonusGraceMonth = CommonUtil.convertObjToStr(productMap.get("BONUS_GRACE_PERIOD_MONTHS"));
                                String bonusGraceOnAfter = CommonUtil.convertObjToStr(productMap.get("BONUS_GRACE_PERIOD_AFTER"));
                                String bonusGraceEnd = CommonUtil.convertObjToStr(productMap.get("BONUS_GRACE_PERIOD_END"));
                                long bonusGraceValue = CommonUtil.convertObjToLong(productMap.get("BONUS_GRACE_PERIOD"));
                                if (bonusGraceDays != null && !bonusGraceDays.equals("") && bonusGraceDays.equals("D") && diffDay <= bonusGraceValue) {
                                    totBonusAmt = totBonusAmt + bonusAmt;
                                } else if (bonusGraceMonth != null && !bonusGraceMonth.equals("") && bonusGraceMonth.equals("M") && diffDay <= (bonusGraceValue * 30)) {
                                    totBonusAmt = totBonusAmt + bonusAmt;
                                } else if (bonusGraceOnAfter != null && !bonusGraceOnAfter.equals("") && bonusGraceOnAfter.equals("A") && intUptoDt.getDate() <= bonusGraceValue) {
                                    totBonusAmt = totBonusAmt + bonusAmt;
                                } else if (bonusGraceEnd != null && !bonusGraceEnd.equals("") && bonusGraceEnd.equals("E")) {
                                } else {
                                }
                            }
                        }
                        //Added By Suresh
                        Rounding rod = new Rounding();
                        if (productMap.get("BONUS_ROUNDING") != null && !productMap.get("BONUS_ROUNDING").equals("") && productMap.get("BONUS_ROUNDING").equals("Y")
                                && bonusAmt > 0) {
                            if (productMap.get("BONUS_ROUNDOFF") != null && productMap.get("BONUS_ROUNDOFF").equals("NEAREST_VALUE")) {
                                bonusAmt = (double) rod.getNearest((long) (bonusAmt * 100), 100) / 100;
                            } else {
                                bonusAmt = (double) rod.lower((long) (bonusAmt * 100), 100) / 100;
                            }
                        }
                        bonusList.add(String.valueOf(bonusAmt));
                        installmentMap.put("BONUS", bonusList);
                    }
                }
                bonusAmt = 0;
            }
            //Arbitration Amount
            double arbitrationAmount = 0.0;
            whereMap.put("ACT_NUM", dataMap.get("CHITTAL_NO"));
            List caseChargeList = sqlMap.executeQueryForList("getMDSCaseChargeDetails", whereMap);
            if (caseChargeList != null && caseChargeList.size() > 0) {
                for (int i = 0; i < caseChargeList.size(); i++) {
                    whereMap = (HashMap) caseChargeList.get(i);
                    arbitrationAmount += CommonUtil.convertObjToDouble(whereMap.get("CHARGE_AMT")).doubleValue();
                }
            }
            //Notice Amount
            double noticeAmount = 0.0;
            whereMap.put("ACT_NUM", dataMap.get("CHITTAL_NO"));
            List noticeList = sqlMap.executeQueryForList("getMDSNoticeChargeDetails", whereMap);
            if (noticeList != null && noticeList.size() > 0) {
                for (int i = 0; i < noticeList.size(); i++) {
                    whereMap = (HashMap) noticeList.get(i);
                    noticeAmount += CommonUtil.convertObjToDouble(whereMap.get("CHARGE_AMT")).doubleValue();
                }
            }
            System.out.println("###### installmentMap" + installmentMap);
            System.out.println("###### maxLength" + noOfInstPay);
            System.out.println("###### bonusList" + bonusList);
            System.out.println("###### penalList" + penalList);
            for (int s = 0; s < noOfInstPay; s++) {
                HashMap insertMap = new HashMap();
                insertMap.put("INT_CALC_DT", CommonUtil.getProperDate(currDt,intUptoDt));
                insertMap.put("CHITTAL_NO", chittalNo);
                insertMap.put("SUB_NO", CommonUtil.convertObjToInt(subNo));
                insertMap.put("DIVISION_NO", String.valueOf(divNo));
                if (bonusList.size() > s) {
                    insertMap.put("BONUS", CommonUtil.convertObjToDouble(bonusList.get(s)));
                } else {
                    insertMap.put("BONUS", new Double(0));
                }
                if (penalList.size() > s) {
                    insertMap.put("PENAL", CommonUtil.convertObjToDouble(penalList.get(s)));
                } else {
                    insertMap.put("PENAL", new Double(0));
                }
                if (discountList.size() > s) {
                    insertMap.put("DISCOUNT", CommonUtil.convertObjToDouble(discountList.get(s)));
                } else {
                    insertMap.put("DISCOUNT", new Double(0));
                }
                insertMap.put("ARBITRATION_AMT", String.valueOf(arbitrationAmount));
                insertMap.put("NOTICE_AMT", String.valueOf(noticeAmount));
                noOfInsPaid += 1;
                insertMap.put("INSTALLMENT_NO", String.valueOf(noOfInsPaid));
                System.out.println("###### insertMap" + insertMap);
                sqlMap.executeUpdate("insertDedExmpMDSDetails", insertMap);
            }

            //Charges
            double chargeAmt = 0.0;
            whereMap.put("ACT_NUM", dataMap.get("CHITTAL_NO"));
            chargeAmt = getChargeAmount(whereMap, prodType);
            if (chargeAmt > 0) {
                dataMap.put("CHARGES", String.valueOf(chargeAmt));
            } else {
                dataMap.put("CHARGES", "0");
            }
            if (productMap.get("BONUS_ROUNDING") != null && !productMap.get("BONUS_ROUNDING").equals("") && productMap.get("BONUS_ROUNDING").equals("Y")
                    && totBonusAmt > 0) {
                Rounding rod = new Rounding();
                if (productMap.get("BONUS_ROUNDOFF") != null && productMap.get("BONUS_ROUNDOFF").equals("NEAREST_VALUE")) {
                    totBonusAmt = (double) rod.getNearest((long) (totBonusAmt * 100), 100) / 100;
                } else {
                    totBonusAmt = (double) rod.lower((long) (totBonusAmt * 100), 100) / 100;
                }
            }
            netAmt = (insAmt * noOfInstPay) + penalAmt - (totBonusAmt + totDiscAmt) + chargeAmt;
            insAmtPayable = (insAmt * noOfInstPay) - (totBonusAmt + totDiscAmt);
            dataMap.put("DIVISION_NO", String.valueOf(divNo));
            dataMap.put("CHIT_START_DT", startDate);
            dataMap.put("INSTALLMENT_DATE", insDate);
            dataMap.put("NO_OF_INSTALLMENTS", String.valueOf(totIns));
            dataMap.put("CURR_INST", String.valueOf(curInsNo));
            dataMap.put("PENDING_INST", String.valueOf(pendingInst));
            dataMap.put("PENDING_DUE_AMT", String.valueOf(insDueAmt));
            dataMap.put("NO_OF_INST_PAY", String.valueOf(noOfInstPay));
            dataMap.put("PRINCIPAL", String.valueOf(insAmtPayable)); // Principal Amount
            dataMap.put("PAID_INST", String.valueOf(noOfInsPaid));
            dataMap.put("PAID_DATE", currDt);
            dataMap.put("INTEREST", "0");
            dataMap.put("CLEAR_BALANCE", new Double(0));
            if (prized == true) {
                dataMap.put("PRIZED_MEMBER", "Y");
            } else {
                dataMap.put("PRIZED_MEMBER", "N");
            }
            dataMap.put("BONUS", new Double(totBonusAmt));
            dataMap.put("DISCOUNT", new Double(totDiscAmt));
            dataMap.put("PENAL", new Double(penalAmt));                 // Penal Amount
            dataMap.put("TOTAL_DEMAND", new Double(netAmt));
        } else {
            dataMap = null;
        }
        System.out.println("####### Single Row DataMap : " + dataMap);
        return dataMap;
    }

    private double getChargeAmount(HashMap whereMap, String prodType) {   //Charges
        double chargeAmount = 0.0;
        try {
            List chargeList = null;
            String actNo = "";
            HashMap recoverChrgMap = new HashMap();
            actNo = CommonUtil.convertObjToStr(whereMap.get("ACT_NUM"));
            chargeList = sqlMap.executeQueryForList("getChargeDetails", whereMap);
            if (chargeList != null && chargeList.size() > 0) {
                for (int i = 0; i < chargeList.size(); i++) {
                    whereMap = (HashMap) chargeList.get(i);
                    chargeAmount += CommonUtil.convertObjToDouble(whereMap.get("CHARGE_AMT")).doubleValue();
                    double chrgAmt = 0.0;
                    chrgAmt = CommonUtil.convertObjToDouble(whereMap.get("CHARGE_AMT")).doubleValue();
                    if (chrgAmt > 0) {
                        recoverChrgMap = new HashMap();
                        recoverChrgMap.put("INT_CALC_UPTO_DT", intUptoDt);
                        recoverChrgMap.put("ACT_NUM", actNo);
                        recoverChrgMap.put("CHARGE_TYPE", CommonUtil.convertObjToStr(whereMap.get("CHARGE_TYPE")));
                        recoverChrgMap.put("AMOUNT", CommonUtil.convertObjToDouble(whereMap.get("CHARGE_AMT")));
                        System.out.println("####### recoverChrgMap:" + recoverChrgMap);
                        sqlMap.executeUpdate("insertRecoveryChargesList", recoverChrgMap);
                    }
                }
            }
            chargeList = null;
            recoverChrgMap = null;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return chargeAmount;
    }
    
    public HashMap calcTermDeposits(String actNum, String prodId, String prodType) throws Exception {
        HashMap dataMap = new HashMap();
        HashMap whereMap = new HashMap();
        String behavesLike = "";
        long actualDelay = 0;
        String acNoSubNo = "";
        acNoSubNo = actNum;
        if (actNum.indexOf("_") != -1) {
            actNum = actNum.substring(0, actNum.indexOf("_"));
        }
        whereMap.put("ACT_NUM", actNum);
        List behavesLikeList = sqlMap.executeQueryForList("getBehavesLikeForDepositNo", whereMap);
        if (behavesLikeList != null && behavesLikeList.size() > 0) {
            whereMap = (HashMap) behavesLikeList.get(0);
            behavesLike = CommonUtil.convertObjToStr(whereMap.get("BEHAVES_LIKE"));
            if (!behavesLike.equals("") && behavesLike != null && behavesLike.equals("RECURRING")) {
                HashMap depositMaxMap = new HashMap();
                if (acNoSubNo.indexOf("_") != -1) {
                  depositMaxMap.put("DEPOSIT_NO", acNoSubNo);
                }else{
                  depositMaxMap.put("DEPOSIT_NO", acNoSubNo+"_1");  
                }
                System.out.println("depositMaxMap cc: " + depositMaxMap);
                List depositMaxList = sqlMap.executeQueryForList("getSelectRDDepositMaxtransDt", depositMaxMap);
                if (depositMaxList != null && depositMaxList.size() > 0) {
                    depositMaxMap = (HashMap) depositMaxList.get(0);
                    Date date = (Date) currDt.clone();
                    Date maxDate = getProperDateFormat(CommonUtil.convertObjToStr(depositMaxMap.get("TRANS_DT")));
                    Date transDueDate = getProperDateFormat(CommonUtil.convertObjToStr(depositMaxMap.get("DUE_DATE")));
                    System.out.println("curr date : " + date + " last transaction date : " + maxDate);
                    int transDueMonth = (int) transDueDate.getMonth() + 1;
                    int currentMonth = (int) date.getMonth() + 1;
                    int maxTxnMonth = (int) maxDate.getMonth() + 1;
                    int currentYear = (int) date.getYear() + 1900;
                    int maxTxnYear = (int) maxDate.getYear() + 1900;
                    System.out.println("curr date month : " + currentMonth + " last transaction maxDate : " + maxTxnMonth + "curr year : " + currentYear + " last transaction year : " + maxTxnYear);

                    HashMap accountMap = new HashMap();
                    HashMap lastMap = new HashMap();
                    HashMap rdDataMap = new HashMap();
                    rdDataMap.put("DEPOSIT_NO", actNum);
                    accountMap.put("DEPOSIT_NO", actNum);
                    accountMap.put("BRANCH_ID", _branchCode);
                    List lst = sqlMap.executeQueryForList("getProductIdForDeposits", accountMap);
                    if (lst != null && lst.size() > 0) {
                        accountMap = (HashMap) lst.get(0);
                        Date currDate = (Date) intUptoDt.clone();
                        String insBeyondMaturityDat = "";
                        List recurringLst = sqlMap.executeQueryForList("getRecurringDepositDetails", accountMap);
                        if (recurringLst != null && recurringLst.size() > 0) {
                            HashMap recurringMap = new HashMap();
                            recurringMap = (HashMap) recurringLst.get(0);
                            insBeyondMaturityDat = CommonUtil.convertObjToStr(recurringMap.get("INST_BEYOND_MATURITY_DATE"));
                        }
                        long totalDelay = 0;
                        double delayAmt = 0.0;
                        double tot_Inst_paid = 0.0;
                        double depAmt = CommonUtil.convertObjToDouble(accountMap.get("DEPOSIT_AMT"));
                        dataMap.put("INSTMT_AMT", depAmt);
                        Date matDt = new Date();
                        matDt.setTime(currDate.getTime());
                        Date depDt = new Date();
                        depDt.setTime(currDate.getTime());
                        lastMap.put("DEPOSIT_NO", actNum);
                        lst = sqlMap.executeQueryForList("getInterestDeptIntTable", lastMap);
                        if (lst != null && lst.size() > 0) {
                            lastMap = (HashMap) lst.get(0);
                            rdDataMap.put("DEPOSIT_AMT", lastMap.get("DEPOSIT_AMT"));
                            rdDataMap.put("MATURITY_DT", lastMap.get("MATURITY_DT"));
                            tot_Inst_paid = CommonUtil.convertObjToDouble(lastMap.get("TOTAL_INSTALL_PAID"));
                            HashMap prematureDateMap = new HashMap();
                            double monthPeriod = 0.0;
                            Date matDate = DateUtil.getDateMMDDYYYY(CommonUtil.convertObjToStr(lastMap.get("MATURITY_DT")));
                            if (matDate.getDate() > 0) {
                                matDt.setDate(matDate.getDate());
                                matDt.setMonth(matDate.getMonth());
                                matDt.setYear(matDate.getYear());
                            }
                            Date depDate = DateUtil.getDateMMDDYYYY(CommonUtil.convertObjToStr(lastMap.get("DEPOSIT_DT")));
                            if (depDate.getDate() > 0) {
                                depDt.setDate(depDate.getDate());
                                depDt.setMonth(depDate.getMonth());
                                depDt.setYear(depDate.getYear());
                            }
                            if (DateUtil.dateDiff((Date) matDt, (Date) currDate) > 0) {
                                matDt = CommonUtil.getProperDate(currDt, matDt);
                                prematureDateMap.put("TO_DATE", matDt);
                                prematureDateMap.put("FROM_DATE", lastMap.get("DEPOSIT_DT"));
                                lst = sqlMap.executeQueryForList("periodRunMap", prematureDateMap);
                                if (lst != null && lst.size() > 0) {
                                    prematureDateMap = (HashMap) lst.get(0);
                                    monthPeriod = CommonUtil.convertObjToDouble(prematureDateMap.get("NO_OF_MONTHS"));
                                    actualDelay = (long) monthPeriod - (long) tot_Inst_paid;
                                }
                                lst = null;
                            } else {
                                System.out.println("currDate" + currDate);
                                java.util.GregorianCalendar gactualCurrDateCalendar = new java.util.GregorianCalendar();
                                gactualCurrDateCalendar.setGregorianChange(currDate);
                                gactualCurrDateCalendar.setTime(currDate);
                                int curDay = gactualCurrDateCalendar.get(gactualCurrDateCalendar.DAY_OF_MONTH);
                                System.out.println("curDaycurDay" + curDay);
                                List recoveryList = sqlMap.executeQueryForList("getRecoveryParameters", whereMap);
                                int gracePeriod = 0;
                                if (recoveryList != null && recoveryList.size() > 0) {
                                    HashMap recoveryDetailsMap = (HashMap) recoveryList.get(0);
                                    if (recoveryDetailsMap != null && recoveryDetailsMap.size() > 0 && recoveryDetailsMap.containsKey("GRACE_PERIOD")) {
                                        gracePeriod = CommonUtil.convertObjToInt(recoveryDetailsMap.get("GRACE_PERIOD"));
                                    }
                                }
                                System.out.println("curDay : " + curDay + " gracePeriod : " + gracePeriod);
                                if (curDay > gracePeriod && transDueMonth > currentMonth && transDueMonth != currentMonth) {
                                    gactualCurrDateCalendar.add(gactualCurrDateCalendar.MONTH, 1);
                                    gactualCurrDateCalendar.set(gactualCurrDateCalendar.DAY_OF_MONTH, gracePeriod);
                                } else {
                                    gactualCurrDateCalendar.set(gactualCurrDateCalendar.DAY_OF_MONTH, gracePeriod);
                                }
                                Date tempcurrDate = new Date();
                                tempcurrDate = CommonUtil.getProperDate(currDt, gactualCurrDateCalendar.getTime());
                                Date dueDate = new Date();
                                HashMap detailedMap = new HashMap();
                                detailedMap.put("DEPOSIT_NO", actNum + "_1");
                                List newList = sqlMap.executeQueryForList("getBalnceDepositDetails", detailedMap);
                                if (newList != null && newList.size() > 0) {
                                    for (int i = 0; i < newList.size(); i++) {
                                        HashMap newMap = (HashMap) newList.get(i);
                                        if (newMap != null && newMap.size() > 0) {
                                            dueDate = DateUtil.getDateMMDDYYYY(CommonUtil.convertObjToStr(newMap.get("DUE_DATE")));
                                            System.out.println("tempcurrDate" + tempcurrDate + "dueDate" + dueDate);
                                            if (dueDate.compareTo(tempcurrDate) == 0 || dueDate.compareTo(tempcurrDate) < 0) {
                                                actualDelay = actualDelay + 1;
                                            }
                                        }
                                    }
                                }
                                System.out.println(" else actualDelay" + actualDelay + " monthPeriod" + monthPeriod + " tot_Inst_paid" + tot_Inst_paid);
                            }
                        }
                        lst = null;

                        if ((DateUtil.dateDiff((Date) matDt, (Date) intUptoDt) > 0) && !insBeyondMaturityDat.equals("") && insBeyondMaturityDat.equals("N")) {
                            dataMap = new HashMap();
                            return dataMap;
                        }
                        //delayed installment calculation...
                        String penalCalcType = "DAYS";//INSTALMENT
                        HashMap dailyMap = new HashMap();
                        dailyMap.put("ROI_GROUP_ID", prodId);
                        List list = (List) sqlMap.executeQueryForList("getSelectDepositsCommision", dailyMap);
                        if (list != null && list.size() > 0) {
                            InterestMaintenanceRateTO objInterestMaintenanceRateTO = (InterestMaintenanceRateTO) list.get(0);
                            if (objInterestMaintenanceRateTO != null) {
                                penalCalcType = CommonUtil.convertObjToStr(objInterestMaintenanceRateTO.getInstType());
                            }
                        }
                        if (DateUtil.dateDiff((Date) matDt, (Date) currDate) < 0 || insBeyondMaturityDat.equals("Y")) {
                            if (penalCalcType != null && !penalCalcType.equals("") && penalCalcType.equals("Installments")) {
                                depAmt = CommonUtil.convertObjToDouble(accountMap.get("DEPOSIT_AMT"));
                                dataMap.put("INSTMT_AMT", depAmt);
                                double chargeAmt = depAmt / 100;//100
                                HashMap delayMap = new HashMap();
                                delayMap.put("PROD_ID", accountMap.get("PROD_ID"));
                                delayMap.put("DEPOSIT_AMT", accountMap.get("DEPOSIT_AMT"));
                                lst = sqlMap.executeQueryForList("getSelectDelayedRate", delayMap);
                                if (lst != null && lst.size() > 0) {
                                    delayMap = (HashMap) lst.get(0);
                                    delayAmt = CommonUtil.convertObjToDouble(delayMap.get("PENAL_INT"));
                                    delayAmt = delayAmt * chargeAmt;
                                }
                                lst = null;
                                HashMap depRecMap = new HashMap();
                                depRecMap.put("DEPOSIT_NO", actNum + "_1");
                                List lstRec = sqlMap.executeQueryForList("getDepTransactionRecurring", depRecMap);
                                if (lstRec != null && lstRec.size() > 0) {//how many unCompleted quarter is there upto (one by one) we have to calculate simple interest formula....
                                    for (int i = 0; i < lstRec.size(); i++) {//when the customer is paid from that dt to today dt....
                                        depRecMap = (HashMap) lstRec.get(i);
                                        Date transDt = DateUtil.getDateMMDDYYYY(CommonUtil.convertObjToStr(depRecMap.get("TRANS_DT")));
                                        Date dueDate = DateUtil.getDateMMDDYYYY(CommonUtil.convertObjToStr(depRecMap.get("DUE_DATE")));
                                        int transMonth = transDt.getMonth() + 1;
                                        int dueMonth = dueDate.getMonth() + 1;
                                        int dueYear = dueDate.getYear() + 1900;
                                        int transYear = transDt.getYear() + 1900;
                                        int delayedInstallment;
                                        if (dueYear == transYear) {
                                            delayedInstallment = transMonth - dueMonth;
                                        } else {
                                            int diffYear = transYear - dueYear;
                                            delayedInstallment = (diffYear * 12 - dueMonth) + transMonth;
                                        }
                                        if (delayedInstallment < 0) {
                                            delayedInstallment = 0;
                                        }
                                        totalDelay = totalDelay + delayedInstallment;
                                    }
                                }
                                lstRec = null;
                                depRecMap = new HashMap();
                                depRecMap.put("DEPOSIT_NO", actNum + "_1");
                                depRecMap.put("DEPOSIT_DT", lastMap.get("DEPOSIT_DT"));
                                depRecMap.put("CURR_DT", currDate);
                                depRecMap.put("SL_NO", String.valueOf(tot_Inst_paid));
                                lstRec = sqlMap.executeQueryForList("getDepTransRecurr", depRecMap);
                                if (lstRec != null && lstRec.size() > 0) {//how many unCompleted quarter is there upto (one by one) we have to calculate simple interest formula....
                                    for (int i = 0; i < lstRec.size(); i++) {//when the customer is paid from that dt to today dt....
                                        depRecMap = (HashMap) lstRec.get(i);
                                        Date dueDate = DateUtil.getDateMMDDYYYY(CommonUtil.convertObjToStr(depRecMap.get("DUE_DATE")));
                                        int transMonth = currDate.getMonth() + 1;//2
                                        int dueMonth = dueDate.getMonth() + 1;//2
                                        int dueYear = dueDate.getYear() + 1900;//2015
                                        int transYear = currDate.getYear() + 1900;//2015
                                        int delayedInstallment;
                                        if (dueYear == transYear) {
                                            delayedInstallment = transMonth - dueMonth;
                                        } else {
                                            int diffYear = transYear - dueYear;
                                            delayedInstallment = (diffYear * 12 - dueMonth) + transMonth;
                                        }
                                        if (delayedInstallment < 0) {
                                            delayedInstallment = 0;
                                        }
                                        totalDelay = totalDelay + delayedInstallment;
                                    }
                                }
                                lstRec = null;
                                delayAmt = delayAmt * totalDelay;
                                double oldPenalAmt = CommonUtil.convertObjToDouble(accountMap.get("DELAYED_AMOUNT"));
                                long oldPenalMonth = CommonUtil.convertObjToLong(accountMap.get("DELAYED_MONTH"));
                                double balanceAmt = 0.0;
                                if (oldPenalAmt > 0) {
                                    balanceAmt = delayAmt - oldPenalAmt;
                                    totalDelay = totalDelay - oldPenalMonth;
                                } else {
                                    balanceAmt = delayAmt;
                                }
                                double principal = actualDelay * CommonUtil.convertObjToDouble(rdDataMap.get("DEPOSIT_AMT"));
                                double totalDemand = principal + balanceAmt;
                                rdDataMap.put("DELAYED_MONTH", accountMap.get("DELAYED_MONTH"));
                                rdDataMap.put("DEPOSIT_PENAL_AMT", String.valueOf(balanceAmt));
                                rdDataMap.put("PRINCIPAL", String.valueOf(principal));
                                rdDataMap.put("TOTAL_DEMAND", String.valueOf(totalDemand));
                                rdDataMap.put("DEPOSIT_PENAL_MONTH", String.valueOf(totalDelay));
//                                System.out.println(" balanceAmt>" + balanceAmt + " totalDelay" + totalDelay);
                            }
                            if (penalCalcType != null && !penalCalcType.equals("") && penalCalcType.equals("Days")) {
                                depAmt = CommonUtil.convertObjToDouble(accountMap.get("DEPOSIT_AMT"));
                                dataMap.put("INSTMT_AMT", depAmt);
                                HashMap delayMap = new HashMap();
                                double roi = 0.0;
                                delayMap.put("PROD_ID", accountMap.get("PROD_ID"));
                                delayMap.put("DEPOSIT_AMT", accountMap.get("DEPOSIT_AMT"));
                                lst = sqlMap.executeQueryForList("getSelectDelayedRate", delayMap);
                                if (lst != null && lst.size() > 0) {
                                    delayMap = (HashMap) lst.get(0);
                                    roi = CommonUtil.convertObjToDouble(delayMap.get("PENAL_INT"));
                                }
                                List lstRec = null;
                                HashMap depRecMap = new HashMap();
                                depRecMap = new HashMap();
                                double penal = 0.0;
                                depRecMap.put("DEPOSIT_NO", actNum + "_1");
                                depRecMap.put("DEPOSIT_DT", lastMap.get("DEPOSIT_DT"));
                                depRecMap.put("CURR_DT", currDate);
                                depRecMap.put("SL_NO", String.valueOf(tot_Inst_paid));
                                lstRec = sqlMap.executeQueryForList("getDepTransRecurr", depRecMap);
                                int size = CommonUtil.convertObjToInt(lstRec.size());
                                double totalInst = tot_Inst_paid;
                                if (lstRec != null && lstRec.size() > 0) {
                                    for (int i = 0; i < size; i++) {
                                        double tempPenal = 0.0;
                                        totalInst += 1;
                                        depRecMap = (HashMap) lstRec.get(i);
                                        double amount = depAmt * (i + 1);
                                        Date dueDate = DateUtil.getDateMMDDYYYY(CommonUtil.convertObjToStr(depRecMap.get("DUE_DATE")));
                                        if (DateUtil.dateDiff(dueDate, intUptoDt) > 0) {
                                            double diff = DateUtil.dateDiff(dueDate, intUptoDt) - 1;
                                            if (diff > 0) {
                                                penal = penal + ((amount * roi * diff) / 36500);
                                                tempPenal = penal;
                                            }
                                        }
                                        HashMap RDInstallMap = new HashMap();
                                        RDInstallMap.put("INT_CALC_UPTO_DT", intUptoDt.clone());
                                        RDInstallMap.put("DEPOSIT_NO", actNum);
                                        RDInstallMap.put("SUB_NO", CommonUtil.convertObjToInt("1"));
                                        RDInstallMap.put("INSTALLMENT_NO", totalInst);
                                        RDInstallMap.put("PENAL_AMT", tempPenal);
                                        RDInstallMap.put("INST_AMT", rdDataMap.get("DEPOSIT_AMT"));
                                        sqlMap.executeUpdate("insertDedExmpRDDetails", RDInstallMap);
                                        tempPenal = 0.0;
                                    }
                                }
                                totalInst += 1;
                                if (actualDelay > 0) {
                                    HashMap RDInstallMapCurr = new HashMap();
                                    RDInstallMapCurr.put("INT_CALC_UPTO_DT", intUptoDt.clone());
                                    RDInstallMapCurr.put("DEPOSIT_NO", actNum);
                                    RDInstallMapCurr.put("SUB_NO", CommonUtil.convertObjToInt("1"));
                                    RDInstallMapCurr.put("INSTALLMENT_NO", totalInst);
                                    RDInstallMapCurr.put("PENAL_AMT", CommonUtil.convertObjToDouble(0));
                                    RDInstallMapCurr.put("INST_AMT", rdDataMap.get("DEPOSIT_AMT"));
                                    sqlMap.executeUpdate("insertDedExmpRDDetails", RDInstallMapCurr);
                                }
                                penal = Math.round(penal);
                                double principal = actualDelay * CommonUtil.convertObjToDouble(rdDataMap.get("DEPOSIT_AMT"));
                                double totalDemand = principal + penal;
                                rdDataMap.put("DELAYED_MONTH", accountMap.get("DELAYED_MONTH"));
                                rdDataMap.put("DEPOSIT_PENAL_AMT", String.valueOf(penal));
                                rdDataMap.put("PRINCIPAL", String.valueOf(principal));
                                rdDataMap.put("TOTAL_DEMAND", String.valueOf(totalDemand));
                                rdDataMap.put("DEPOSIT_PENAL_MONTH", String.valueOf(totalDelay));
                            }
                            //Ends Here
                        }
                    }
                    dataMap.put("CLEAR_BALANCE", new Double(0));
                    dataMap.put("PRINCIPAL", rdDataMap.get("PRINCIPAL"));
                    dataMap.put("TOTAL", actualDelay);
                    dataMap.put("PRINCIPAL_OLD", rdDataMap.get("PRINCIPAL"));
                    dataMap.put("PENAL", rdDataMap.get("DEPOSIT_PENAL_AMT"));
                    dataMap.put("TOTAL_DEMAND", rdDataMap.get("TOTAL_DEMAND"));
                    dataMap.put("DEPOSIT_PENAL_MONTH", rdDataMap.get("DEPOSIT_PENAL_MONTH"));
                    dataMap.put("INTEREST", new Double(0));
                    dataMap.put("CHARGES", new Double(0));
                    if (CommonUtil.convertObjToDouble(rdDataMap.get("TOTAL_DEMAND")) <= 0.0) {
                        dataMap = null;
                    }
                }
            }
        }
        return dataMap;
    }

//    public HashMap calcLoanPayments(String actNum, String prodId, String prodType, String recoveryYesNo) throws Exception {
//        HashMap dataMap = new HashMap();
//        HashMap whereMap = new HashMap();
//        whereMap.put("ACT_NUM", actNum);
//        List parameterList = sqlMap.executeQueryForList("getRecoveryParameter", whereMap);
//        String clear_balance = "";
//        boolean flag = false;
//        System.out.println("prod type here"+prodType);
//        if (parameterList != null && parameterList.size() > 0) {
//            int firstDay = 0;
//            Date inst_dt = null;
//            Date checkDate = (Date) currDt.clone();
//            Date sanctionDt = null;
//            whereMap = (HashMap) parameterList.get(0);
//            firstDay = CommonUtil.convertObjToInt(whereMap.get("FIRST_DAY"));
//            sanctionDt = (Date) whereMap.get("FROM_DT");
//            System.out.println("### First Day : " + firstDay);
//            System.out.println("### checkDat : " + checkDate);
//            System.out.println("### sanctionDt : " + sanctionDt);
////            checkDate.setDate(firstDay);
//            System.out.println("### checkDat : " + checkDate);
//            long diffDay;
//            diffDay = DateUtil.dateDiff((Date) sanctionDt, (Date) checkDate);
//            System.out.println("### diffDay : " + diffDay);
//            if (diffDay >= 0.0) {
//                System.out.println("### Allow : " + checkDate);
//
//                // IF SALARY_RECOVERY = 'NO' ONLY
//                if (recoveryYesNo.equals("N")) {
////                    Date recoveryCheckDt = null;
////                    Date maxInstDt = null;
////                    Date lastIntCalcDt = null;
////                    long diffDayPending = 0;
////                    recoveryCheckDt = (Date)interestUptoDt.clone();
////                    recoveryCheckDt.setMonth(recoveryCheckDt.getMonth()-1);
////                    recoveryCheckDt = setProperDtFormat(recoveryCheckDt);
////                    System.out.println("########### After interestUptoDt : "+interestUptoDt);
////                    System.out.println("########### After recoveryCheckDt : "+recoveryCheckDt);
////                    HashMap instMap = new HashMap();
////                    instMap.put("ACT_NUM",actNum);
////                    instMap.put("CHECK_DT",recoveryCheckDt);
////                    List instList = sqlMap.executeQueryForList("getLoanMaxInstDt", instMap);
////                    if(instList!=null && instList.size()>0){
////                        instMap=(HashMap) instList.get(0);
////                        maxInstDt = (Date)instMap.get("INSTALLMENT_DT");
////                        HashMap intCalcMap = new HashMap();
////                        intCalcMap.put("WHERE",actNum);
////                        List intCalcList = sqlMap.executeQueryForList("getLastIntCalDateAD", intCalcMap);
////                        if(intCalcList!=null && intCalcList.size()>0 ){
////                            intCalcMap=(HashMap) intCalcList.get(0);
////                            if (intCalcMap.get("LAST_INT_CALC_DT")!=null) {
////                                lastIntCalcDt = (Date)intCalcMap.get("LAST_INT_CALC_DT");
////                                System.out.println("########### lastIntCalcDt : "+lastIntCalcDt);
////                                System.out.println("###########     maxInstDt : "+maxInstDt);
////                                System.out.println("###########interestUptoDt : "+interestUptoDt);
////                                diffDayPending= DateUtil.dateDiff((Date) lastIntCalcDt, (Date) maxInstDt);
////                                System.out.println("### diffDayPending : "+diffDayPending);
////                                if(diffDayPending<=0){
////                                    System.out.println("########### NOT ALLOWED : ");
////                                   dataMap = null; 
////                                   return dataMap;
////                                }else{
////                                    System.out.println("########### ALLOWED : ");
////                                }
////                            } else {
////                                dataMap = null; 
////                                return dataMap;
////                            }
////                        }
////                    }
////                }
//
//                    HashMap asAndWhenMap = interestCalculationTLAD(actNum, prodId, prodType);
//                    System.out.println("@#@ asAndWhenMap is >>>>" + asAndWhenMap);
//                    System.out.println("transDetail" + actNum + _branchCode);
//                    HashMap insertPenal = new HashMap();
//                    List chargeList = null;
//                    HashMap loanInstall = new HashMap();
//                    loanInstall.put("ACT_NUM", actNum);
//                    loanInstall.put("BRANCH_CODE", _branchCode);
//                    double instalAmt = 0.0;
//                    double paidAmount = 0.0;
//                    HashMap emiMap = new HashMap();
//                    String installtype = "";
//                    String emi_uniform = "";
//                    if (prodType != null && prodType.equals("TL")) {      //Only TL
//
//                        double totPrinciple = 0.0;
//                        List emiList = sqlMap.executeQueryForList("getEmiTypeDetail", loanInstall);
//                        if (emiList.size() > 0) {
//                            emiMap = (HashMap) emiList.get(0);
//                            installtype = emiMap.get("INSTALL_TYPE").toString();
//                            emi_uniform = emiMap.get("EMI_IN_SIMPLEINTREST").toString();
//                        }
//                        //HashMap allInstallmentMap=null;
//
//
//
//
//
//                        HashMap allInstallmentMap = null;
//                        if (!(installtype.equals("UNIFORM_PRINCIPLE_EMI") && emi_uniform.equals("Y"))) {
//                            List paidAmt = sqlMap.executeQueryForList("getPaidPrinciple", loanInstall);
//                            allInstallmentMap = (HashMap) paidAmt.get(0);
//                            System.out.println("allInstallmentMap..." + allInstallmentMap);
//                            System.out.println("!!!!asAndWhenMap:" + asAndWhenMap);
//                            totPrinciple = CommonUtil.convertObjToDouble(allInstallmentMap.get("PRINCIPLE")).doubleValue();
//                            paidAmount = CommonUtil.convertObjToDouble(allInstallmentMap.get("PRINCIPLE")).doubleValue();
//                            System.out.println("totPrinciple11:" + totPrinciple + " ::" + paidAmount);
//                            if (asAndWhenMap == null || (asAndWhenMap != null && asAndWhenMap.containsKey("AS_CUSTOMER_COMES") && (asAndWhenMap.get("AS_CUSTOMER_COMES") != null) && asAndWhenMap.get("AS_CUSTOMER_COMES").equals("N"))) {
//                                paidAmt = sqlMap.executeQueryForList("getIntDetails", loanInstall);
//                                if (paidAmt != null && paidAmt.size() > 0) {
//                                    allInstallmentMap = (HashMap) paidAmt.get(0);
//                                }
//                                double totExcessAmt = CommonUtil.convertObjToDouble(allInstallmentMap.get("EXCESS_AMT")).doubleValue();
//                                totPrinciple += totExcessAmt;
//                                System.out.println("in as cust comexx" + totPrinciple);
//                            }
//                            List lst = sqlMap.executeQueryForList("getAllLoanInstallment", loanInstall);
//
//                            for (int i = 0; i < lst.size(); i++) {
//                                allInstallmentMap = (HashMap) lst.get(i);
//                                System.out.println("allInstallmentMap...,mmm>>" + allInstallmentMap);
//                                instalAmt = CommonUtil.convertObjToDouble(allInstallmentMap.get("PRINCIPAL_AMT")).doubleValue();
//                                System.out.println("instalAmtt..." + instalAmt + "toooottt" + totPrinciple);
//                                if (instalAmt <= totPrinciple) {
//                                    totPrinciple -= instalAmt;
//                                    System.out.println("totPrinciple-=instalAmt==" + totPrinciple);
//                                    inst_dt = new Date();
//                                    String in_date = CommonUtil.convertObjToStr(allInstallmentMap.get("INSTALLMENT_DT"));
//                                    inst_dt = DateUtil.getDateMMDDYYYY(in_date);
//                                } else {
//
//                                    inst_dt = new Date();
//                                    String in_date = CommonUtil.convertObjToStr(allInstallmentMap.get("INSTALLMENT_DT"));
//                                    inst_dt = DateUtil.getDateMMDDYYYY(in_date);
//                                    //bbau22
//                                    List aList = ServerUtil.executeQuery("getMorotorium", loanInstall);
//                                    String moret = "";
//                                    if (aList.size() > 0 && aList.get(0) != null) {
//                                        HashMap mapop = (HashMap) aList.get(0);
//                                        if (mapop.get("MORATORIUM_GIVEN") != null) {
//                                            moret = mapop.get("MORATORIUM_GIVEN").toString();
//                                        }
//
//
//                                    }
////                                    System.out.println("inst_dt=22====" + inst_dt + "currDt=22======" + currDt);
////                                    System.out.println("totPrrr22rrrrrrrrrrrrrr=" + DateUtil.dateDiff(inst_dt, currDt) + "ggggg=" + moret);
////                                    System.out.println("totPrrrrrrrrrrrrrrrrr=" + DateUtil.dateDiff(inst_dt, currDt) + "ggggg=" + moret);
//                                    if (DateUtil.dateDiff(inst_dt, currDt) <= 0 && (moret != null && moret.equals("Y"))) {
//                                        totPrinciple = 0;
//                                        flag = true;
//                                        break;
//                                    } else {
//                                        //
//                                        totPrinciple = CommonUtil.convertObjToDouble(allInstallmentMap.get("PRINCIPAL_AMT")).doubleValue() - totPrinciple;
//                                        System.out.println("before Breakkkk" + totPrinciple);
//                                        break;
//                                    }
//                                }
//                            }
//                        } else {
//                            List paidAmtemi = sqlMap.executeQueryForList("getPaidPrincipleEMI1", loanInstall);
//                            allInstallmentMap = (HashMap) paidAmtemi.get(0);
//                            System.out.println("!!!!asAndWhenMap:" + asAndWhenMap);
//                            System.out.println("allInstallmentMap..." + allInstallmentMap);
//                            totPrinciple = CommonUtil.convertObjToDouble(allInstallmentMap.get("PRINCIPLE")).doubleValue();
//                            paidAmount = CommonUtil.convertObjToDouble(allInstallmentMap.get("PRINCIPLE")).doubleValue();
//                            System.out.println("totPrinciple22:" + totPrinciple + " ::" + paidAmount);
//                            if (asAndWhenMap == null || (asAndWhenMap != null && asAndWhenMap.containsKey("AS_CUSTOMER_COMES") && (asAndWhenMap.get("AS_CUSTOMER_COMES") != null) && asAndWhenMap.get("AS_CUSTOMER_COMES").equals("N"))) {
//                                paidAmtemi = sqlMap.executeQueryForList("getIntDetails", loanInstall);
//                                if (paidAmtemi != null && paidAmtemi.size() > 0) {
//                                    allInstallmentMap = (HashMap) paidAmtemi.get(0);
//                                }
//                                System.out.println("allInstallmentMap444" + allInstallmentMap);
//                                double totExcessAmt = CommonUtil.convertObjToDouble(allInstallmentMap.get("EXCESS_AMT")).doubleValue();
//                                totPrinciple += totExcessAmt;
//                                System.out.println("totPrinciple" + totPrinciple);
//                            }
//                            List lst = sqlMap.executeQueryForList("getAllLoanInstallment", loanInstall);
//                            for (int i = 0; i < lst.size(); i++) {
//                                allInstallmentMap = (HashMap) lst.get(i);
//                                System.out.println("allInstallmentMap34243" + allInstallmentMap);
//                                instalAmt = CommonUtil.convertObjToDouble(allInstallmentMap.get("PRINCIPAL_AMT")).doubleValue();
//                                System.out.println("111222instalAmt" + instalAmt + ":::" + totPrinciple);
//                                if (instalAmt <= totPrinciple) {
//                                    totPrinciple -= instalAmt;
//                                    System.out.println("chhhhnnn" + totPrinciple);
//                                    inst_dt = new Date();
//                                    String in_date = CommonUtil.convertObjToStr(allInstallmentMap.get("INSTALLMENT_DT"));
//                                    inst_dt = DateUtil.getDateMMDDYYYY(in_date);
//                                } else {
//                                    inst_dt = new Date();
//                                    String in_date = CommonUtil.convertObjToStr(allInstallmentMap.get("INSTALLMENT_DT"));
//                                    inst_dt = DateUtil.getDateMMDDYYYY(in_date);
//                                    totPrinciple = CommonUtil.convertObjToDouble(allInstallmentMap.get("TOTAL_AMT")).doubleValue() - totPrinciple;
//                                    System.out.println("bbbkkk" + totPrinciple);
//                                    break;
//                                }
//                                System.out.println("totPrinciple@@@@@@@@@@@@" + totPrinciple);
//                            }
//                        }
//                        if (!(installtype.equals("UNIFORM_PRINCIPLE_EMI") && emi_uniform.equals("Y"))) {
//                            //bb
//                            dataMap.put("INSTMT_AMT", instalAmt);
//                        } else {
//                            dataMap.put("INSTMT_AMT", (allInstallmentMap.get("TOTAL_AMT")));
//                        }
//                        //bbau33
//                        List aList = ServerUtil.executeQuery("getMorotorium", loanInstall);
//                        String moret = "";
//                        if (aList.size() > 0 && aList.get(0) != null) {
//                            HashMap mapop = (HashMap) aList.get(0);
//                            if (mapop.get("MORATORIUM_GIVEN") != null) {
//                                moret = mapop.get("MORATORIUM_GIVEN").toString();
//                            }
//
//
//                        }
////                        System.out.println("inst_dt=33====" + inst_dt + "currDt===33====" + currDt);
////                        System.out.println("totPrrrrr333rrrrrrrrrrrr=" + DateUtil.dateDiff(inst_dt, currDt) + "ggggg=" + moret);
////                        System.out.println("totPrrrrrrrrrrrrrrrrr=" + DateUtil.dateDiff(inst_dt, currDt) + "ggggg=" + moret);
//                        if (DateUtil.dateDiff(inst_dt, currDt) <= 0 && (moret != null && moret.equals("Y"))) {
//                            dataMap.put("INSTMT_AMT", 0);
//                            dataMap.put("PRINCIPAL", String.valueOf("0"));
//                        }
//
//
//
//                        Date addDt = (Date) currDt.clone();
//                        Date instDt = DateUtil.addDays(inst_dt, 1);
//                        addDt.setDate(instDt.getDate());
//                        addDt.setMonth(instDt.getMonth());
//                        addDt.setYear(instDt.getYear());
//                        loanInstall.put("FROM_DATE", addDt);//DateUtil.addDays(inst_dt,1));
//                        loanInstall.put("TO_DATE", interestUptoDt);
//                        System.out.println("!! getTotalamount#####" + loanInstall);
//                        List lst1 = null;
//                        if (inst_dt != null && (totPrinciple > 0)) {
//                            lst1 = sqlMap.executeQueryForList("getTotalAmountOverDue", loanInstall);
//                            System.out.println("listsize####" + lst1);
//                        }
//                        double principle = 0;
//                        if (lst1 != null && lst1.size() > 0) {
//                            HashMap map = (HashMap) lst1.get(0);
//                            principle = CommonUtil.convertObjToDouble(map.get("PRINCIPAL_AMOUNT")).doubleValue();
//                        }
//                        totPrinciple += principle;
//                        System.out.println("totPrinciple 1111####" + totPrinciple);
//
//                        if (!(installtype.equals("UNIFORM_PRINCIPLE_EMI") && emi_uniform.equals("Y"))) {
//
//                            if (lst1 != null && lst1.size() > 0) {
//                                HashMap map = (HashMap) lst1.get(0);
//                                principle = CommonUtil.convertObjToDouble(map.get("PRINCIPAL_AMOUNT")).doubleValue();
//                            }
//                            totPrinciple += principle;
//                        } else {
//                            if (lst1 != null && lst1.size() > 0) {
//                                HashMap map = (HashMap) lst1.get(0);
//                                System.out.println("snnnnnn" + map);
//                                principle = CommonUtil.convertObjToDouble(map.get("PRINCIPAL_AMOUNT")).doubleValue() + CommonUtil.convertObjToDouble(map.get("INTEREST_AMOUNT")).doubleValue();
//                                System.out.println("sdasdsd" + principle);
//                                if (principle == 0) {
//                                    List advList = sqlMap.executeQueryForList("getAdvAmt", loanInstall);
//                                    if (advList.size() > 0 && advList != null) {
//                                        map = (HashMap) advList.get(0);
//                                        if (map.get("TOTAL_AMT") != null) {
//                                            principle = CommonUtil.convertObjToDouble(map.get("TOTAL_AMT"));
//                                        }
//                                        totPrinciple = principle;
//                                    }
//                                } else {
//// totPrinciple += principle;
//                                    totPrinciple = principle;
//                                }
//                                System.out.println("totPrinciple66666" + totPrinciple);
////                             if(totPrinciple<0)
////                             {
////                                 totPrinciple=0;
////                             }
//
//                            } else {
//                                System.out.println("innn eeelllsss333" + totPrinciple);
//                                totPrinciple -= CommonUtil.convertObjToDouble(asAndWhenMap.get("INTEREST")).doubleValue();
//                                System.out.println("innn eeelllsss333" + totPrinciple);
////                            if(totPrinciple<0)
////                             {
////                                 totPrinciple=0;
////                             }
//                            }
//
//                        }
//
//
//
//
//                        insertPenal.put("CURR_MONTH_PRINCEPLE", new Double(totPrinciple));
//                        insertPenal.put("INSTALL_DT", inst_dt);
//                        if (asAndWhenMap.containsKey("MORATORIUM_INT_FOR_EMI")) {
//                            insertPenal.put("MORATORIUM_INT_FOR_EMI", asAndWhenMap.get("MORATORIUM_INT_FOR_EMI"));
//                        }
//                        if (asAndWhenMap != null && asAndWhenMap.containsKey("AS_CUSTOMER_COMES") && (asAndWhenMap.get("AS_CUSTOMER_COMES") != null) && asAndWhenMap.get("AS_CUSTOMER_COMES").equals("Y")) {
//                            double interest = CommonUtil.convertObjToDouble(asAndWhenMap.get("INTEREST")).doubleValue();
//                            double penal = CommonUtil.convertObjToDouble(asAndWhenMap.get("LOAN_CLOSING_PENAL_INT")).doubleValue();
//                            List facilitylst = sqlMap.executeQueryForList("LoneFacilityDetailAD", loanInstall);
//                            if (facilitylst != null && facilitylst.size() > 0) {
//                                HashMap hash = (HashMap) facilitylst.get(0);
//                                if (hash.get("CLEAR_BALANCE") != null) {
//                                    clear_balance = hash.get("CLEAR_BALANCE").toString();
//                                }
//                                System.out.println("clear_balance =" + clear_balance + " aaa---" + loanInstall.get("ACT_NUM"));
//                                hash.put("ACT_NUM", loanInstall.get("ACT_NUM"));
//                                if (asAndWhenMap.containsKey("PREMATURE")) {
//                                    insertPenal.put("PREMATURE", asAndWhenMap.get("PREMATURE"));
//                                }
//                                if (asAndWhenMap.containsKey("PREMATURE") && asAndWhenMap.containsKey("PREMATURE_INT_CALC_AMT")
//                                        && CommonUtil.convertObjToStr(asAndWhenMap.get("PREMATURE_INT_CALC_AMT")).equals("LOANSANCTIONAMT")) {
//                                    hash.put("FROM_DT", hash.get("ACCT_OPEN_DT"));
//                                } else {
//                                    hash.put("FROM_DT", hash.get("LAST_INT_CALC_DT"));
//                                    hash.put("FROM_DT", DateUtil.addDays(((Date) hash.get("FROM_DT")), 2));
//                                }
//                                hash.put("TO_DATE", interestUptoDt.clone());
//                                if (!(asAndWhenMap != null && asAndWhenMap.containsKey("INSTALL_TYPE") && asAndWhenMap.get("INSTALL_TYPE") != null && asAndWhenMap.get("INSTALL_TYPE").equals("EMI"))) {
//                                    facilitylst = sqlMap.executeQueryForList("getPaidPrinciple", hash);
//                                } else {
//                                    facilitylst = null;
//                                    if (asAndWhenMap.containsKey("PRINCIPAL_DUE") && asAndWhenMap.get("PRINCIPAL_DUE") != null) {
//                                        insertPenal.put("CURR_MONTH_PRINCEPLE", asAndWhenMap.get("PRINCIPAL_DUE"));
//                                    }
//                                }
//                                if (facilitylst != null && facilitylst.size() > 0) {
//                                    hash = (HashMap) facilitylst.get(0);
//                                    interest -= CommonUtil.convertObjToDouble(hash.get("INTEREST")).doubleValue();
//                                    penal -= CommonUtil.convertObjToDouble(hash.get("PENAL")).doubleValue();
//
//                                    insertPenal.put("PAID_INTEREST", hash.get("INTEREST"));
//                                }
//                            }
//                            System.out.println("####interest:" + interest);
//                            if (interest > 0) {
//                                insertPenal.put("CURR_MONTH_INT", new Double(interest));
//                            } else {
//                                insertPenal.put("CURR_MONTH_INT", new Double(0));
//                            }
//                            if (penal > 0) {
//                                insertPenal.put("PENAL_INT", new Double(penal));
//                            } else {
//                                insertPenal.put("PENAL_INT", new Double(0));
//                            }
//                            insertPenal.put("INTEREST", asAndWhenMap.get("INTEREST"));
//                            insertPenal.put("LOAN_CLOSING_PENAL_INT", asAndWhenMap.get("LOAN_CLOSING_PENAL_INT"));
//
//                            insertPenal.put("LAST_INT_CALC_DT", asAndWhenMap.get("LAST_INT_CALC_DT"));
//                            insertPenal.put("ROI", asAndWhenMap.get("ROI"));
//                            chargeList = sqlMap.executeQueryForList("getChargeDetails", loanInstall);
//                        } else {
//                            List getIntDetails = sqlMap.executeQueryForList("getIntDetails", loanInstall);
//                            HashMap hash = null;
//                            if (getIntDetails != null) {
//                                for (int i = 0; i < getIntDetails.size(); i++) {
//                                    hash = (HashMap) getIntDetails.get(i);
//                                    String trn_mode = CommonUtil.convertObjToStr(hash.get("TRN_CODE"));
//                                    double pBal = CommonUtil.convertObjToDouble(hash.get("PBAL")).doubleValue();
//                                    double iBal = CommonUtil.convertObjToDouble(hash.get("IBAL")).doubleValue();
//                                    double pibal = CommonUtil.convertObjToDouble(hash.get("PIBAL")).doubleValue();
//                                    double excess = CommonUtil.convertObjToDouble(hash.get("EXCESS_AMT")).doubleValue();
//                                    System.out.println("pBalcc99oogytf" + pBal + "::" + iBal + "::" + pibal + "::" + excess);
//                                    pBal -= excess;
//                                    System.out.println("pBalrrr" + pBal);
//                                    if (pBal < totPrinciple) {
//                                        insertPenal.put("CURR_MONTH_PRINCEPLE", new Double(pBal));
//                                    }
//                                    System.out.println("insertPenal5555" + insertPenal);
//                                    if (trn_mode.equals("C*")) {
//                                        insertPenal.put("CURR_MONTH_INT", String.valueOf(iBal + pibal));
//                                        insertPenal.put("PRINCIPLE_BAL", new Double(pBal));
//                                        insertPenal.put("EBAL", hash.get("EBAL"));
//                                        break;
//                                    } else {
//                                        if (!trn_mode.equals("DP")) {
//                                            insertPenal.put("CURR_MONTH_INT", String.valueOf(iBal + pibal));
//                                        }
//                                        insertPenal.put("EBAL", hash.get("EBAL"));
//                                        insertPenal.put("PRINCIPLE_BAL", new Double(pBal));
//                                    }
//                                    System.out.println("int principel detailsINSIDE LOAN##" + insertPenal);
//                                }
//                            }
//                            getIntDetails = sqlMap.executeQueryForList("getPenalIntDetails", loanInstall);
//                            hash = (HashMap) getIntDetails.get(0);
//                            insertPenal.put("PENAL_INT", hash.get("PIBAL"));
//                        }
//                        System.out.println("insertPenalnnnnnnshdcgasdg" + insertPenal);//abbbbb
//                    }
//
//                    if (prodType != null && prodType.equals("AD")) // Only  AD
//                    {
//                        if (asAndWhenMap != null && asAndWhenMap.size() > 0) {
//                            if (asAndWhenMap.containsKey("AS_CUSTOMER_COMES") && asAndWhenMap.get("AS_CUSTOMER_COMES").equals("Y")) {
//                                List facilitylst = sqlMap.executeQueryForList("LoneFacilityDetailAD", loanInstall);
//                                double interest = CommonUtil.convertObjToDouble(asAndWhenMap.get("INTEREST")).doubleValue();
//                                double penal = CommonUtil.convertObjToDouble(asAndWhenMap.get("LOAN_CLOSING_PENAL_INT")).doubleValue();
//                                if (facilitylst != null && facilitylst.size() > 0) {
//                                    HashMap hash = (HashMap) facilitylst.get(0);
//                                    if (hash.get("CLEAR_BALANCE") != null) {
//                                        clear_balance = hash.get("CLEAR_BALANCE").toString();
//                                    }
//                                    hash.put("ACT_NUM", loanInstall.get("ACT_NUM"));
//                                    hash.put("FROM_DT", hash.get("LAST_INT_CALC_DT"));
//                                    hash.put("FROM_DT", DateUtil.addDays(((Date) hash.get("FROM_DT")), 2));
//                                    hash.put("TO_DATE", DateUtil.addDaysProperFormat(interestUptoDt, -1));
//                                    facilitylst = sqlMap.executeQueryForList("getPaidPrincipleAD", hash);
//                                    if (facilitylst != null && facilitylst.size() > 0) {
//                                        hash = (HashMap) facilitylst.get(0);
//                                        interest -= CommonUtil.convertObjToDouble(hash.get("INTEREST")).doubleValue();
//                                        penal -= CommonUtil.convertObjToDouble(hash.get("PENAL")).doubleValue();
//                                    }
//                                }
//                                if (interest > 0) {
//                                    insertPenal.put("CURR_MONTH_INT", new Double(interest));
//                                } else {
//                                    insertPenal.put("CURR_MONTH_INT", new Double(0));
//                                }
//                                if (penal > 0) {
//                                    insertPenal.put("PENAL_INT", new Double(penal));
//                                } else {
//                                    insertPenal.put("PENAL_INT", new Double(0));
//                                }
//                                insertPenal.put("INTEREST", asAndWhenMap.get("INTEREST"));
//                                insertPenal.put("LOAN_CLOSING_PENAL_INT", asAndWhenMap.get("LOAN_CLOSING_PENAL_INT"));
//                                chargeList = sqlMap.executeQueryForList("getChargeDetails", loanInstall);
//                            } else {
//                                if (prodType != null && prodType.equals("AD")) {
//                                    List getIntDetails = sqlMap.executeQueryForList("getIntDetailsAD", loanInstall);
//                                    HashMap hash = null;
//
//                                    for (int i = 0; i < getIntDetails.size(); i++) {
//                                        hash = (HashMap) getIntDetails.get(i);
//                                        String trn_mode = CommonUtil.convertObjToStr(hash.get("TRN_CODE"));
//                                        double iBal = CommonUtil.convertObjToDouble(hash.get("IBAL")).doubleValue();
//                                        double pibal = CommonUtil.convertObjToDouble(hash.get("PIBAL")).doubleValue();
//                                        double excess = CommonUtil.convertObjToDouble(hash.get("EXCESS_AMT")).doubleValue();
//                                        if (trn_mode.equals("C*")) {
//                                            insertPenal.put("CURR_MONTH_INT", String.valueOf(iBal + pibal));
//                                            insertPenal.put("PRINCIPLE_BAL", new Double(CommonUtil.convertObjToDouble(hash.get("PBAL")).doubleValue() - excess));
//                                            insertPenal.put("EBAL", hash.get("EBAL"));
//                                            break;
//                                        } else {
//                                            insertPenal.put("CURR_MONTH_INT", String.valueOf(iBal + pibal));
//                                            insertPenal.put("PRINCIPLE_BAL", new Double(CommonUtil.convertObjToDouble(hash.get("PBAL")).doubleValue() - excess));
//                                            insertPenal.put("EBAL", hash.get("EBAL"));
//                                        }
//                                        System.out.println("int principel detailsINSIDE OD" + insertPenal);
//                                    }
//                                    getIntDetails = sqlMap.executeQueryForList("getPenalIntDetailsAD", loanInstall);
//                                    if (getIntDetails.size() > 0) {
//                                        hash = (HashMap) getIntDetails.get(0);
//                                        insertPenal.put("PENAL_INT", hash.get("PIBAL"));
//                                    }
//                                    insertPenal.remove("PRINCIPLE_BAL");
//
//                                }
//                            }
//                        }
//                    }
//                    //Added By Suresh  (Current Dt > To Date AND PBAL >0 in ADV_TRANS_DETAILS, Add Principle_Balance)
//                    if (prodType != null && prodType.equals("AD")) {
//                        double pBalance = 0.0;
//                        Date expDt = null;
//                        List expDtList = sqlMap.executeQueryForList("getLoanExpDate", loanInstall);
//                        if (expDtList != null && expDtList.size() > 0) {
//                            whereMap = new HashMap();
//                            whereMap = (HashMap) expDtList.get(0);
//                            pBalance = CommonUtil.convertObjToDouble(whereMap.get("PBAL")).doubleValue();
//                            expDt = (Date) whereMap.get("TO_DT");
//                            long diffDayPending = DateUtil.dateDiff(expDt, intUptoDt);
//                            System.out.println("############# Insert PBalance" + pBalance + "######diffDayPending :" + diffDayPending);
//                            if (diffDayPending > 0 && pBalance > 0) {
//                                insertPenal.put("PRINCIPLE_BAL", new Double(pBalance));
//                            }
//                        }
//                    }
//                    System.out.println("####### insertPenal : " + insertPenal);
//                    //Charges
//                    double chargeAmt = 0.0;
//                    whereMap = new HashMap();
//                    whereMap.put("ACT_NUM", actNum);
//                    chargeAmt = getChargeAmount(whereMap, prodType);
//                    if (chargeAmt > 0) {
//                        dataMap.put("CHARGES", String.valueOf(chargeAmt));
//                    } else {
//                        dataMap.put("CHARGES", "0");
//                    }
//                    System.out.println("####### Single Row insertPenal : " + insertPenal);
//                    double totalDemand = 0.0;
//                    double principalAmount = 0.0;
//                    if (insertPenal.containsKey("CURR_MONTH_PRINCEPLE")) {
//                        principalAmount = CommonUtil.convertObjToDouble(insertPenal.get("CURR_MONTH_PRINCEPLE")).doubleValue();     // Principal Amount
//                        totalDemand = CommonUtil.convertObjToDouble(insertPenal.get("CURR_MONTH_INT")).doubleValue()
//                                + CommonUtil.convertObjToDouble(insertPenal.get("PENAL_INT")).doubleValue() + chargeAmt;
//                        System.out.println("totalDemand 111====" + totalDemand);
//                    } else {
//                        principalAmount = CommonUtil.convertObjToDouble(insertPenal.get("PRINCIPLE_BAL")).doubleValue();     // Principal Amount
//                        totalDemand = CommonUtil.convertObjToDouble(insertPenal.get("CURR_MONTH_INT")).doubleValue()
//                                + CommonUtil.convertObjToDouble(insertPenal.get("PENAL_INT")).doubleValue() + chargeAmt;
//                        System.out.println("totalDemand 222====" + totalDemand);
//                    }
//
//
//                    if (inst_dt != null && prodType.equals("TL")) {
//                        // if (retired != null && retired.equals("YES")) {
//                        //   dataMap.put("PRINCIPAL",String.valueOf(Math.abs(Integer.parseInt(clear_balance))));
//                        // } else {
//                        System.out.println("intUptoDt" + intUptoDt + "inst_dt" + inst_dt);
//                        if (DateUtil.dateDiff(intUptoDt, inst_dt) <= 0) {
//                            if (installtype.equals("UNIFORM_PRINCIPLE_EMI") && emi_uniform.equals("Y")) {
//                                principalAmount = principalAmount - CommonUtil.convertObjToDouble(insertPenal.get("CURR_MONTH_INT"));
//                                dataMap.put("PRINCIPAL", String.valueOf(principalAmount));
//                            } else {
//                                dataMap.put("PRINCIPAL", String.valueOf(principalAmount));
//                            }
//                        } else {
//                            if (installtype.equals("UNIFORM_PRINCIPLE_EMI") && emi_uniform.equals("Y")) {
//                                principalAmount = principalAmount - CommonUtil.convertObjToDouble(insertPenal.get("CURR_MONTH_INT"));
//                                dataMap.put("PRINCIPAL", String.valueOf(principalAmount));
//                            } else {
//                                dataMap.put("PRINCIPAL", "0");
//                                principalAmount = 0.0;
//                            }
//                        }
//                        // }
//
//
////                     if(installtype.equals("UNIFORM_PRINCIPLE_EMI") && emi_uniform.equals("Y")){
////                    
////                     HashMap balanceMap = new HashMap();
////                      double totalPrincAmount=0.0;
////                      double balanceLoanAmt =0.0;
////                        double finalDemandAmt =0.0;
////                    List sumInstLstemi = sqlMap.executeQueryForList("getPrincipalAmtGreaterThanBalAmtforemi", balanceMap);
////                                if(sumInstLstemi!=null && sumInstLstemi.size()>0){
////                                    balanceMap =(HashMap) sumInstLstemi.get(0);
////                                    totalPrincAmount = CommonUtil.convertObjToDouble(balanceMap.get("TOTAL_AMT")).doubleValue();
////                                    //totalPrincAmount+=instalAmt;
////                                    finalDemandAmt = totalPrincAmount - paidAmount;
////                                    if(balanceLoanAmt>finalDemandAmt){
////                                        dataMap.put("PRINCIPAL", String.valueOf(instalAmt));
////                                        principalAmount = instalAmt;
////                                    }else{
////                                        dataMap.put("PRINCIPAL", String.valueOf(balanceLoanAmt));
////                                        principalAmount = balanceLoanAmt;
////                                    }
////                                }
////                    
////                    
////                    
////                    
////                    
////                     }
//
//                        // IF SALARY_RECOVERY = 'YES' ONLY
//                        if ((principalAmount == 0 && (!(installtype.equals("UNIFORM_PRINCIPLE_EMI") && emi_uniform.equals("Y")))) || (installtype.equals("UNIFORM_PRINCIPLE_EMI") && emi_uniform.equals("Y"))) {
//                            System.out.println("############ instalAmt : " + instalAmt);
//                            HashMap balanceMap = new HashMap();
//                            double balanceLoanAmt = 0.0;
//                            double finalDemandAmt = 0.0;
//                            System.out.println("############ actNum : " + actNum);
//                            balanceMap.put("ACCOUNTNO", actNum);
//                            List balannceAmtLst = sqlMap.executeQueryForList("getBalanceLoanPrincipalAmt", balanceMap);
//                            if (balannceAmtLst != null && balannceAmtLst.size() > 0) {
//                                balanceMap = (HashMap) balannceAmtLst.get(0);
//                                balanceLoanAmt = CommonUtil.convertObjToDouble(balanceMap.get("LOAN_BALANCE_PRINCIPAL")).doubleValue();
//                                System.out.println("############ instalAmt : " + instalAmt);
//                                System.out.println("############ LoanBalancePrincAmt : " + balanceLoanAmt);
//                                double checkAmt = 0.0;
//                                double totalPrincAmount = 0.0;
//                                checkAmt = balanceLoanAmt - instalAmt;
//                                if (checkAmt > 0) {
//                                    if (!(installtype.equals("UNIFORM_PRINCIPLE_EMI") && emi_uniform.equals("Y"))) {
//                                        balanceMap.put("ACCT_NUM", actNum);
//                                        balanceMap.put("BALANCE_AMT", String.valueOf(checkAmt));
//                                        List sumInstLst = sqlMap.executeQueryForList("getPrincipalAmtGreaterThanBalAmt", balanceMap);
//                                        if (sumInstLst != null && sumInstLst.size() > 0) {
//                                            balanceMap = (HashMap) sumInstLst.get(0);
//                                            totalPrincAmount = CommonUtil.convertObjToDouble(balanceMap.get("PRINCIPAL_AMOUNT")).doubleValue();
//                                            totalPrincAmount += instalAmt;
//                                            finalDemandAmt = totalPrincAmount - paidAmount;
//                                            if (balanceLoanAmt > finalDemandAmt) {
//                                                dataMap.put("PRINCIPAL", String.valueOf(finalDemandAmt));
//                                                principalAmount = finalDemandAmt;
//                                            } else {
//                                                dataMap.put("PRINCIPAL", String.valueOf(balanceLoanAmt));
//                                                principalAmount = balanceLoanAmt;
//                                            }
//                                        }
//                                        //bbau11
//                                        List aList = ServerUtil.executeQuery("getMorotorium", loanInstall);
//                                        String moret = "";
//                                        if (aList.size() > 0 && aList.get(0) != null) {
//                                            HashMap mapop = (HashMap) aList.get(0);
//                                            if (mapop.get("MORATORIUM_GIVEN") != null) {
//                                                moret = mapop.get("MORATORIUM_GIVEN").toString();
//                                            }
//
//
//                                        }
//                                        System.out.println("inst_dt==11===" + inst_dt + "currDt=11======" + currDt);
//                                        System.out.println("totPrrrrrrr11rrrrrrrrrr=" + DateUtil.dateDiff(inst_dt, currDt) + "ggggg=" + moret);
//                                        System.out.println("totPrrrrrrrrrrrrrrrrr=" + DateUtil.dateDiff(inst_dt, currDt) + "ggggg=" + moret);
//                                        if (DateUtil.dateDiff(inst_dt, currDt) <= 0 && (moret != null && moret.equals("Y"))) {
//                                            finalDemandAmt = 0;
//                                            principalAmount = 0;
//                                            //break;
//                                        }
//                                        System.out.println("############ finalDemandAmt : " + finalDemandAmt);
//                                    }
//                                } else {
//                                    HashMap transMap = new HashMap();
//                                    transMap.put("ACT_NUM", actNum);
//                                    transMap.put("BRANCH_CODE", _branchCode);
//                                    List sanctionLst = sqlMap.executeQueryForList("getNoOfDaysinLoan", transMap);
//                                    if (sanctionLst != null && sanctionLst.size() > 0) {
//                                        HashMap recordMap = (HashMap) sanctionLst.get(0);
//                                        int repayFreq = 0;
//                                        repayFreq = CommonUtil.convertObjToInt(recordMap.get("REPAYMENT_FREQUENCY"));
//                                        if (repayFreq == 1) {
//                                            Date expiry_dt = null;
//                                            expiry_dt = (Date) recordMap.get("TO_DT");
//                                            expiry_dt = (Date) expiry_dt.clone();
//                                            System.out.println("########## expiry_dt : " + expiry_dt);
//                                            if (DateUtil.dateDiff(intUptoDt, expiry_dt) >= 0) {
//                                                principalAmount = 0.0;
//                                                dataMap.put("PRINCIPAL", String.valueOf(principalAmount));
//                                            } else {
//                                                dataMap.put("PRINCIPAL", String.valueOf(balanceLoanAmt));
//                                                principalAmount = balanceLoanAmt;
//                                            }
//                                        } else {
//                                            dataMap.put("PRINCIPAL", String.valueOf(balanceLoanAmt));
//                                            principalAmount = balanceLoanAmt;
//                                        }
//                                    }
//                                }
//                            }
//                        }
//                    }
//                    if (prodType.equals("AD")) {
//                        // if (retired != null && retired.equals("YES")) {
//                        // dataMap.put("PRINCIPAL",String.valueOf(Math.abs(Integer.parseInt(clear_balance))));
//                        //  } else {
//                        if (principalAmount > 0) {
//                            dataMap.put("PRINCIPAL", String.valueOf(principalAmount));
//                        } else {
//
//                            dataMap.put("PRINCIPAL", "0");
//
//                        }
//                        // }    
//                    }
//
//                    if (installtype.equals("UNIFORM_PRINCIPLE_EMI") && emi_uniform.equals("Y")) {
//                        double dueamount = 0;
//                        double penal = 0;
//                        double totEmi = 0;
//                        double paidEmi = 0;
//                        double principle = 0;
//                        double interst = Double.parseDouble(insertPenal.get("CURR_MONTH_INT").toString());
//
//                        System.out.println("interst" + interst);
//                        HashMap emi = new HashMap();
//                        Date upto = (Date) currDt.clone();
//                        emi.put("ACC_NUM", actNum);
//                        emi.put("UP_TO", upto);
//
//                        List totalEmiList = sqlMap.executeQueryForList("TotalEmi", emi);
//                        if (totalEmiList != null && totalEmiList.size() > 0 && ((HashMap) totalEmiList.get(0)).get("TOTAL_AMOUNT") != null) {
//                            HashMap aMap = new HashMap();
//                            aMap = (HashMap) totalEmiList.get(0);
//                            totEmi = Double.parseDouble(aMap.get("TOTAL_AMOUNT").toString());
//                            System.out.println("TOTAL_AMOUNTv" + totEmi);
//                        } else {
//                            totEmi = 0;
//
//                        }
//                        HashMap paid = new HashMap();
//                        paid.put("ACT_NUM", actNum);
//                        paid.put("BRANCH_CODE", _branchCode);
//                        List paidAmtemi = sqlMap.executeQueryForList("getPaidPrincipleEMI", loanInstall);
//                        if (paidAmtemi != null && paidAmtemi.size() > 0 && ((HashMap) paidAmtemi.get(0)).get("PRINCIPLE") != null) {
//                            paid = (HashMap) paidAmtemi.get(0);
//                            System.out.println("!!!!asAndWhenMap:" + paid);
//                            // System.out.println("allInstallmentMap..."+allInstallmentMap);
//                            paidEmi = CommonUtil.convertObjToDouble(paid.get("PRINCIPLE")).doubleValue();
//                            System.out.println("paidEmi" + paidEmi);
//                        } else {
//                            paidEmi = 0;
//                        }
//                        System.out.println("totEmi" + totEmi + "paidEmi" + paidEmi);
//                        dueamount = totEmi - paidEmi;
//                        double paidamount = paidEmi;
//                        if (dueamount <= 0) {
//                            dueamount = CommonUtil.convertObjToDouble(dataMap.get("PRINCIPAL")) + interst;
//                        }
//                        System.out.println("totalDemandsdas" + dueamount);
//
//
//                        System.out.println("+==========PENAL STARTS==============================+");
//                        List scheduleList = sqlMap.executeQueryForList("getSchedules", emi);
//                        //  List penalInterstRate=
//
//
//                        Date penalStrats = new Date();
//                        if (scheduleList != null && scheduleList.size() > 0) {
//                            for (int k = 0; k < scheduleList.size(); k++) {
//                                HashMap eachInstall = new HashMap();
//                                eachInstall = (HashMap) scheduleList.get(k);
//                                System.out.println("eachInstall" + eachInstall);
//                                double scheduledEmi = Double.parseDouble(eachInstall.get("TOTAL_AMT").toString());
//                                if (paidamount >= scheduledEmi) {
//                                    System.out.println("111paidamount" + paidamount + "scheduledEmi" + scheduledEmi);
//                                    paidamount = paidamount - scheduledEmi;
//                                    System.out.println("paidamount" + paidamount);
//                                } else {
//                                    String in_date = CommonUtil.convertObjToStr(eachInstall.get("INSTALLMENT_DT"));
//                                    penalStrats = DateUtil.getDateMMDDYYYY(in_date);
//                                    //   penalStrats=DateUtil.getDateMMDDYYYY(eachInstall.get("INSTALLMENT_DT").toString());
//                                    System.out.println("penalStrats....." + penalStrats);
//                                    break;
//                                }
//
//
//                            }
//                            emi.put("FROM_DATE", penalStrats);
//                            List getPenalData = sqlMap.executeQueryForList("getPenalData", emi);
//                            List penalInterstRate = sqlMap.executeQueryForList("getPenalIntestRatefromMaintenance", emi);
//                            double interstPenal = 0;
//                            double garce = 0;
//                            List graceDays = sqlMap.executeQueryForList("getGracePeriodDays", emi);
//                            if (graceDays != null && graceDays.size() > 0) {
//                                HashMap map = new HashMap();
//                                map = (HashMap) graceDays.get(0);
//                                if (map != null && map.containsKey("GRACE_PERIOD_DAYS") && map.get("GRACE_PERIOD_DAYS") != null) {
//                                    garce = Double.parseDouble(map.get("GRACE_PERIOD_DAYS").toString());
//                                } else {
//                                    garce = 0;
//                                }
//                            } else {
//                                garce = 0;
//                            }
//                            long gracedy = (long) garce;
//                            int graceint = (int) garce;
//                            if (penalInterstRate != null && penalInterstRate.size() > 0) {
//                                HashMap test = new HashMap();
//                                test = (HashMap) penalInterstRate.get(0);
//                                if (test != null && test.containsKey("PENAL_INTEREST") && test.get("PENAL_INTEREST") != null) {
//                                    interstPenal = Double.parseDouble(test.get("PENAL_INTEREST").toString());
//                                } else {
//                                    List limitList = sqlMap.executeQueryForList("getLimitFromLoanSanc", emi);
//                                    double limit = Double.parseDouble(((HashMap) limitList.get(0)).get("LIMIT").toString());
//                                    emi.put("LIMIT", limit);
//                                    List penalFromROI = sqlMap.executeQueryForList("getPenalIntestRatefromROI", emi);
//                                    if (penalFromROI != null && penalFromROI.size() > 0) {
//                                        test = new HashMap();
//                                        test = (HashMap) penalFromROI.get(0);
//                                        System.out.println("testttt" + test);
//                                        interstPenal = Double.parseDouble(test.get("PENAL_INT").toString());
//                                    }
//                                }
//                            } else {
//                                List limitList = sqlMap.executeQueryForList("getLimitFromLoanSanc", emi);
//                                double limit = Double.parseDouble(((HashMap) limitList.get(0)).get("LIMIT").toString());
//                                emi.put("LIMIT", limit);
//                                List penalFromROI = sqlMap.executeQueryForList("getPenalIntestRatefromROI", emi);
//                                if (penalFromROI != null && penalFromROI.size() > 0) {
//                                    HashMap test = new HashMap();
//                                    test = (HashMap) penalFromROI.get(0);
//                                    interstPenal = Double.parseDouble(test.get("PENAL_INT").toString());
//                                }
//                            }
//                            System.out.println("interstPenal...." + interstPenal);
//                            if (getPenalData != null && getPenalData.size() > 0) {
//                                for (int k = 0; k < getPenalData.size(); k++) {
//                                    HashMap amap = new HashMap();
//                                    amap = (HashMap) getPenalData.get(k);
//
//
//
//                                    String in_date = CommonUtil.convertObjToStr(amap.get("INSTALLMENT_DT"));
//                                    Date currntDate = DateUtil.getDateMMDDYYYY(in_date);
//                                    currntDate = DateUtil.addDays(currntDate, graceint);
//                                    System.out.println("5555currntDate.." + currntDate);
//                                    // InterestCalculationTask incalc=new InterestCalculationTask(); 
//                                    HashMap holidayMap = new HashMap();
//                                    holidayMap.put("CURR_DATE", currntDate);
//                                    holidayMap.put("BRANCH_CODE", _branchCode);
////                                             currntDate=incalc.holiydaychecking(holidayMap);
//
//
//
//
//                                    currntDate = setProperDtFormat(currntDate);
////                            System.out.println("instDate   "+currntDate);
////                            holidayMap.put("NEXT_DATE",currntDate);
////                            holidayMap.put("BRANCH_CODE",_branchCode);
//                                    holidayMap = new HashMap();
//                                    boolean checkHoliday = true;
//                                    String str = "any next working day";
//                                    System.out.println("instDate   " + currntDate);
//                                    currntDate = setProperDtFormat(currntDate);
//                                    holidayMap.put("NEXT_DATE", currntDate);
//                                    holidayMap.put("BRANCH_CODE", _branchCode);
//                                    while (checkHoliday) {
//                                        boolean tholiday = false;
//                                        List Holiday = sqlMap.executeQueryForList("checkHolidayDateOD", holidayMap);
//                                        List weeklyOf = sqlMap.executeQueryForList("checkWeeklyOffOD", holidayMap);
//                                        boolean isHoliday = Holiday.size() > 0 ? true : false;
//                                        boolean isWeekOff = weeklyOf.size() > 0 ? true : false;
//                                        if (isHoliday || isWeekOff) {
//                                            // System.out.println("#### diffDayPending Holiday True : "+diffDayPending);
//                                            if (str.equals("any next working day")) {
//                                                // diffDayPending-=1;
//                                                currntDate.setDate(currntDate.getDate() + 1);
//                                            } else {
//                                                //diffDayPending+=1;
//                                                currntDate.setDate(currntDate.getDate() - 1);
//                                            }
//                                            holidayMap.put("NEXT_DATE", currntDate);
//                                            checkHoliday = true;
//                                            System.out.println("#### holidayMap : " + holidayMap);
//                                        } else {
//                                            //System.out.println("#### diffDay Holiday False : "+diffDayPending);
//                                            checkHoliday = false;
//                                        }
//                                    }
//                                    System.out.println("currntDatemmm" + currntDate);
//                                    //  Date currntDate=DateUtil.getDateMMDDYYYY(amap.get("INSTALLMENT_DT").toString());
//                                    System.out.println("DateUtil.dateDiff(currntDate,upto)" + DateUtil.dateDiff(currntDate, upto));
//
//                                    long difference = DateUtil.dateDiff(currntDate, upto) - 1;
//                                    if (difference < 0) {
//                                        difference = 0;
//                                    }
//                                    System.out.println("difference..." + difference);
//                                    double installment = Double.parseDouble(amap.get("TOTAL_AMT").toString());
//                                    System.out.println("installmentsadeasdasd" + installment);
//                                    penal = penal + ((installment * difference * interstPenal) / 36500);
//                                    System.out.println("penallcalcuuu" + penal);
//                                }
//
//                            }
//
//                        }
//                        principle = dueamount - interst;
//                        System.out.println("mmmprinciple" + principle + "::penal" + penal + "::interst" + interst);
//                        totalDemand = principle + penal + interst;
//                        totalDemand = Math.round(totalDemand);
//                        principle = Math.round(principle);
//                        penal = Math.round(penal);
//                        interst = Math.round(interst);
//                        System.out.println("tttttoooo" + totalDemand);
//                        dataMap.put("INTEREST", Math.round(Double.parseDouble(insertPenal.get("CURR_MONTH_INT").toString())));
//                        dataMap.put("PENAL", penal);
//                        dataMap.put("TOTAL_DEMAND", new Double(totalDemand));
//                        dataMap.put("CLEAR_BALANCE", clear_balance);
//                        System.out.println("mmmmmmiiinnneee" + dataMap);
//                    } else {
//                        totalDemand += principalAmount;
//                        System.out.println("totalDemand 333====" + totalDemand);
//                        dataMap.put("INTEREST", insertPenal.get("CURR_MONTH_INT"));
//                        dataMap.put("PENAL", insertPenal.get("PENAL_INT"));
//                        dataMap.put("TOTAL_DEMAND", new Double(totalDemand));
//                        dataMap.put("CLEAR_BALANCE", clear_balance);
//                    }
//
//                    if (flag) {
//                        dataMap.put("PRINCIPAL", 0);
//                        flag = false;
//                    }
//
//                    //if(totalDemand<=0){
//                    //    dataMap = null;
//                    // }
//                }
//            } else {
//                System.out.println("### Not Allow : " + checkDate);
//                dataMap = null;
//            }
//        }
//
//
//        System.out.println("####### Single Row DataMap : " + dataMap);
//        return dataMap;
//
//        /*
//         * HashMap dataMap=new HashMap(); HashMap whereMap=new HashMap();
//         * whereMap.put("ACT_NUM",actNum); List parameterList =
//         * sqlMap.executeQueryForList("getRecoveryParameter", whereMap);
//         * if(parameterList!=null && parameterList.size()>0){ int firstDay=0;
//         * Date inst_dt=null; Date checkDate = (Date)currDt.clone(); Date
//         * sanctionDt = null; whereMap=(HashMap) parameterList.get(0); firstDay
//         * =CommonUtil.convertObjToInt(whereMap.get("FIRST_DAY")); sanctionDt =
//         * (Date) whereMap.get("FROM_DT"); System.out.println("### First Day :
//         * "+firstDay); System.out.println("### checkDat : "+checkDate);
//         * System.out.println("### sanctionDt : "+sanctionDt);
//         * checkDate.setDate(firstDay); System.out.println("### checkDat :
//         * "+checkDate); long diffDay; diffDay= DateUtil.dateDiff((Date)
//         * sanctionDt, (Date) checkDate); System.out.println("### diffDay :
//         * "+diffDay); if (diffDay >= 0.0) { System.out.println("### Allow :
//         * "+checkDate);
//         *
//         * // IF SALARY_RECOVERY = 'NO' ONLY if(recoveryYesNo.equals("N")){ Date
//         * recoveryCheckDt = null; Date maxInstDt = null; Date lastIntCalcDt =
//         * null; long diffDayPending = 0; recoveryCheckDt =
//         * (Date)interestUptoDt.clone();
//         * recoveryCheckDt.setMonth(recoveryCheckDt.getMonth()); recoveryCheckDt
//         * = setProperDtFormat(recoveryCheckDt); System.out.println("###########
//         * After interestUptoDt : "+interestUptoDt);
//         * System.out.println("########### After recoveryCheckDt :
//         * "+recoveryCheckDt); HashMap instMap = new HashMap();
//         * instMap.put("ACT_NUM",actNum);
//         * instMap.put("CHECK_DT",recoveryCheckDt); List instList =
//         * sqlMap.executeQueryForList("getLoanMaxInstDt", instMap);
//         * if(instList!=null && instList.size()>0){ instMap=(HashMap)
//         * instList.get(0); maxInstDt = (Date)instMap.get("INSTALLMENT_DT");
//         * HashMap intCalcMap = new HashMap(); intCalcMap.put("WHERE",actNum);
//         * List intCalcList = sqlMap.executeQueryForList("getLastIntCalDateAD",
//         * intCalcMap); if(intCalcList!=null && intCalcList.size()>0 ){
//         * intCalcMap=(HashMap) intCalcList.get(0); if
//         * (intCalcMap.get("LAST_INT_CALC_DT")!=null) { lastIntCalcDt =
//         * (Date)intCalcMap.get("LAST_INT_CALC_DT");
//         * System.out.println("########### lastIntCalcDt : "+lastIntCalcDt);
//         * System.out.println("########### maxInstDt : "+maxInstDt);
//         * System.out.println("###########interestUptoDt : "+interestUptoDt);
//         * diffDayPending= DateUtil.dateDiff((Date) lastIntCalcDt, (Date)
//         * maxInstDt); System.out.println("### diffDayPending :
//         * "+diffDayPending); if(diffDayPending<=0){
//         * System.out.println("########### NOT ALLOWED : "); dataMap = null;
//         * return dataMap; }else{ System.out.println("########### ALLOWED : ");
//         * } } else { dataMap = null; return dataMap; } } } }
//         *
//         * HashMap asAndWhenMap = interestCalculationTLAD(actNum, prodId,
//         * prodType); System.out.println("@#@ asAndWhenMap is
//         * >>>>"+asAndWhenMap);
//         * System.out.println("transDetail"+actNum+_branchCode); HashMap
//         * insertPenal=new HashMap(); List chargeList=null; HashMap loanInstall
//         * = new HashMap(); loanInstall.put("ACT_NUM",actNum);
//         * loanInstall.put("BRANCH_CODE", _branchCode); double instalAmt=0.0;
//         * double paidAmount=0.0; HashMap emiMap=new HashMap(); String
//         * installtype=""; String emi_uniform=""; if(prodType !=null &&
//         * prodType.equals("TL")) { //Only TL double totPrinciple=0.0; List
//         * emiList=sqlMap.executeQueryForList("getEmiTypeDetail", loanInstall);
//         * if(emiList.size()>0){ emiMap=(HashMap)emiList.get(0);
//         * installtype=emiMap.get("INSTALL_TYPE").toString();
//         * emi_uniform=emiMap.get("EMI_IN_SIMPLEINTREST").toString(); } HashMap
//         * allInstallmentMap=null;
//         * if(!(installtype.equals("UNIFORM_PRINCIPLE_EMI") &&
//         * emi_uniform.equals("Y") )){ List
//         * paidAmt=sqlMap.executeQueryForList("getPaidPrinciple", loanInstall);
//         * allInstallmentMap=(HashMap)paidAmt.get(0);
//         * System.out.println("!!!!asAndWhenMap:"+asAndWhenMap);
//         * totPrinciple=CommonUtil.convertObjToDouble(allInstallmentMap.get("PRINCIPLE")).doubleValue();
//         * paidAmount =
//         * CommonUtil.convertObjToDouble(allInstallmentMap.get("PRINCIPLE")).doubleValue();
//         * if(asAndWhenMap ==null || (asAndWhenMap !=null &&
//         * asAndWhenMap.containsKey("AS_CUSTOMER_COMES") &&
//         * (asAndWhenMap.get("AS_CUSTOMER_COMES")!=null) &&
//         * asAndWhenMap.get("AS_CUSTOMER_COMES").equals("N"))){
//         * paidAmt=sqlMap.executeQueryForList("getIntDetails", loanInstall); if
//         * (paidAmt != null && paidAmt.size() > 0) {
//         * allInstallmentMap=(HashMap)paidAmt.get(0); } double
//         * totExcessAmt=CommonUtil.convertObjToDouble(allInstallmentMap.get("EXCESS_AMT")).doubleValue();
//         * totPrinciple+=totExcessAmt; } List
//         * lst=sqlMap.executeQueryForList("getAllLoanInstallment", loanInstall);
//         * for(int i=0;i<lst.size();i++) {
//         * allInstallmentMap=(HashMap)lst.get(i);
//         * instalAmt=CommonUtil.convertObjToDouble(allInstallmentMap.get("PRINCIPAL_AMT")).doubleValue();
//         * if(instalAmt<=totPrinciple) { totPrinciple-=instalAmt; inst_dt=new
//         * Date(); String
//         * in_date=CommonUtil.convertObjToStr(allInstallmentMap.get("INSTALLMENT_DT"));
//         * inst_dt=DateUtil.getDateMMDDYYYY(in_date); } else { inst_dt=new
//         * Date(); String
//         * in_date=CommonUtil.convertObjToStr(allInstallmentMap.get("INSTALLMENT_DT"));
//         * inst_dt=DateUtil.getDateMMDDYYYY(in_date);
//         * totPrinciple=CommonUtil.convertObjToDouble(allInstallmentMap.get("PRINCIPAL_AMT")).doubleValue()-totPrinciple;
//         * break; } } } else { List
//         * paidAmtemi=sqlMap.executeQueryForList("getPaidPrincipleEMI1",
//         * loanInstall); allInstallmentMap=(HashMap)paidAmtemi.get(0);
//         * System.out.println("!!!!asAndWhenMap:"+asAndWhenMap);
//         * System.out.println("allInstallmentMap..."+allInstallmentMap);
//         * totPrinciple=CommonUtil.convertObjToDouble(allInstallmentMap.get("PRINCIPLE")).doubleValue();
//         * paidAmount =
//         * CommonUtil.convertObjToDouble(allInstallmentMap.get("PRINCIPLE")).doubleValue();
//         * System.out.println("totPrinciple22:"+totPrinciple+" ::"+paidAmount);
//         * if(asAndWhenMap ==null || (asAndWhenMap !=null &&
//         * asAndWhenMap.containsKey("AS_CUSTOMER_COMES") &&
//         * (asAndWhenMap.get("AS_CUSTOMER_COMES")!=null) &&
//         * asAndWhenMap.get("AS_CUSTOMER_COMES").equals("N"))){
//         * paidAmtemi=sqlMap.executeQueryForList("getIntDetails", loanInstall);
//         * if (paidAmtemi != null && paidAmtemi.size() > 0) {
//         * allInstallmentMap=(HashMap)paidAmtemi.get(0); }
//         * System.out.println("allInstallmentMap444"+allInstallmentMap); double
//         * totExcessAmt=CommonUtil.convertObjToDouble(allInstallmentMap.get("EXCESS_AMT")).doubleValue();
//         * totPrinciple+=totExcessAmt;
//         * System.out.println("totPrinciple"+totPrinciple); } List
//         * lst=sqlMap.executeQueryForList("getAllLoanInstallment", loanInstall);
//         * for(int i=0;i<lst.size();i++) {
//         * allInstallmentMap=(HashMap)lst.get(i);
//         * System.out.println("allInstallmentMap34243"+allInstallmentMap);
//         * instalAmt=CommonUtil.convertObjToDouble(allInstallmentMap.get("TOTAL_AMT")).doubleValue();
//         * System.out.println("111222instalAmt"+instalAmt+":::"+totPrinciple);
//         * if(instalAmt<=totPrinciple) { totPrinciple-=instalAmt;
//         * System.out.println("chhhhnnn"+totPrinciple); inst_dt=new Date();
//         * String
//         * in_date=CommonUtil.convertObjToStr(allInstallmentMap.get("INSTALLMENT_DT"));
//         * inst_dt=DateUtil.getDateMMDDYYYY(in_date); } else { inst_dt=new
//         * Date(); String
//         * in_date=CommonUtil.convertObjToStr(allInstallmentMap.get("INSTALLMENT_DT"));
//         * inst_dt=DateUtil.getDateMMDDYYYY(in_date);
//         * totPrinciple=CommonUtil.convertObjToDouble(allInstallmentMap.get("TOTAL_AMT")).doubleValue()-totPrinciple;
//         * System.out.println("bbbkkk"+totPrinciple); break; }
//         * System.out.println("totPrinciple@@@@@@@@@@@@"+totPrinciple); } }
//         *
//         *
//         * Date addDt=(Date)currDt.clone(); Date
//         * instDt=DateUtil.addDays(inst_dt,1); addDt.setDate(instDt.getDate());
//         * addDt.setMonth(instDt.getMonth()); addDt.setYear(instDt.getYear());
//         * loanInstall.put("FROM_DATE",addDt);//DateUtil.addDays(inst_dt,1));
//         * loanInstall.put("TO_DATE",interestUptoDt); System.out.println("!!
//         * getTotalamount#####"+loanInstall); List lst1=null; if(inst_dt !=null
//         * &&(totPrinciple>0)) {
//         * lst1=sqlMap.executeQueryForList("getTotalAmountOverDue",loanInstall);
//         * System.out.println("listsize####"+lst1); } double principle=0;
//         * if(!(installtype.equals("UNIFORM_PRINCIPLE_EMI") &&
//         * emi_uniform.equals("Y"))){ if(lst1 !=null && lst1.size()>0){ HashMap
//         * map=(HashMap)lst1.get(0);
//         * principle=CommonUtil.convertObjToDouble(map.get("PRINCIPAL_AMOUNT")).doubleValue();
//         * } totPrinciple+=principle; } else { if(lst1 !=null && lst1.size()>0
//         * ){ HashMap map=(HashMap)lst1.get(0);
//         * System.out.println("snnnnnn"+map);
//         * principle=CommonUtil.convertObjToDouble(map.get("PRINCIPAL_AMOUNT")).doubleValue()+CommonUtil.convertObjToDouble(map.get("INTEREST_AMOUNT")).doubleValue();
//         * System.out.println("sdasdsd"+principle); if(principle==0){
//         * if(principle==0){ List
//         * advList=sqlMap.executeQueryForList("getAdvAmt", loanInstall);
//         * if(advList.size()>0 && advList!=null){ map=(HashMap)advList.get(0);
//         * if(map.get("TOTAL_AMT")!=null){
//         * principle=CommonUtil.convertObjToDouble(map.get("TOTAL_AMT")); }
//         * totPrinciple=principle ; } } }else{ // totPrinciple += principle;
//         * totPrinciple = principle; }
//         * System.out.println("totPrinciple66666"+totPrinciple); //
//         * if(totPrinciple<0) // { // totPrinciple=0; // }
//         *
//         * } else { System.out.println("innn eeelllsss333"+totPrinciple);
//         * totPrinciple-=CommonUtil.convertObjToDouble(asAndWhenMap.get("INTEREST")).doubleValue();
//         * System.out.println("innn eeelllsss333"+totPrinciple); //
//         * if(totPrinciple<0) // { // totPrinciple=0; // } }
//         *
//         * }
//         * insertPenal.put("CURR_MONTH_PRINCEPLE",new Double(totPrinciple));
//         * insertPenal.put("INSTALL_DT",inst_dt); if
//         * (asAndWhenMap.containsKey("MORATORIUM_INT_FOR_EMI")) {
//         * insertPenal.put("MORATORIUM_INT_FOR_EMI",asAndWhenMap.get("MORATORIUM_INT_FOR_EMI"));
//         * } if(asAndWhenMap !=null &&
//         * asAndWhenMap.containsKey("AS_CUSTOMER_COMES") &&
//         * (asAndWhenMap.get("AS_CUSTOMER_COMES")!=null) &&
//         * asAndWhenMap.get("AS_CUSTOMER_COMES").equals("Y")){ double
//         * interest=CommonUtil.convertObjToDouble(asAndWhenMap.get("INTEREST")).doubleValue();
//         * double penal
//         * =CommonUtil.convertObjToDouble(asAndWhenMap.get("LOAN_CLOSING_PENAL_INT")).doubleValue();
//         * List
//         * facilitylst=sqlMap.executeQueryForList("LoneFacilityDetailAD",loanInstall);
//         * if(facilitylst!=null && facilitylst.size()>0){ HashMap
//         * hash=(HashMap)facilitylst.get(0);
//         * hash.put("ACT_NUM",loanInstall.get("ACT_NUM")); if
//         * (asAndWhenMap.containsKey("PREMATURE")) {
//         * insertPenal.put("PREMATURE",asAndWhenMap.get("PREMATURE")); } if
//         * (asAndWhenMap.containsKey("PREMATURE") &&
//         * asAndWhenMap.containsKey("PREMATURE_INT_CALC_AMT") &&
//         * CommonUtil.convertObjToStr(asAndWhenMap.get("PREMATURE_INT_CALC_AMT")).equals("LOANSANCTIONAMT"))
//         * { hash.put("FROM_DT",hash.get("ACCT_OPEN_DT")); } else {
//         * hash.put("FROM_DT",hash.get("LAST_INT_CALC_DT"));
//         * hash.put("FROM_DT",DateUtil.addDays(((Date)hash.get("FROM_DT")),2));
//         * } hash.put("TO_DATE", interestUptoDt.clone()); if (!(asAndWhenMap !=
//         * null && asAndWhenMap.containsKey("INSTALL_TYPE") &&
//         * asAndWhenMap.get("INSTALL_TYPE") != null &&
//         * asAndWhenMap.get("INSTALL_TYPE").equals("EMI"))) {
//         * facilitylst=sqlMap.executeQueryForList("getPaidPrinciple",hash); }
//         * else { facilitylst=null; if(
//         * asAndWhenMap.containsKey("PRINCIPAL_DUE") &&
//         * asAndWhenMap.get("PRINCIPAL_DUE")!=null){
//         * insertPenal.put("CURR_MONTH_PRINCEPLE",asAndWhenMap.get("PRINCIPAL_DUE"));
//         * } } if(facilitylst!=null && facilitylst.size()>0){
//         * hash=(HashMap)facilitylst.get(0);
//         * interest-=CommonUtil.convertObjToDouble(hash.get("INTEREST")).doubleValue();
//         * penal-=CommonUtil.convertObjToDouble(hash.get("PENAL")).doubleValue();
//         *
//         * insertPenal.put("PAID_INTEREST",hash.get("INTEREST")); } }
//         * System.out.println("####interest:"+interest); if (interest > 0) {
//         * insertPenal.put("CURR_MONTH_INT",new Double(interest)); } else {
//         * insertPenal.put("CURR_MONTH_INT",new Double(0)); } if (penal > 0) {
//         * insertPenal.put("PENAL_INT",new Double(penal)); } else {
//         * insertPenal.put("PENAL_INT",new Double(0)); }
//         * insertPenal.put("INTEREST",asAndWhenMap.get("INTEREST"));
//         * insertPenal.put("LOAN_CLOSING_PENAL_INT",asAndWhenMap.get("LOAN_CLOSING_PENAL_INT"));
//         *
//         * insertPenal.put("LAST_INT_CALC_DT",asAndWhenMap.get("LAST_INT_CALC_DT"));
//         * insertPenal.put("ROI",asAndWhenMap.get("ROI"));
//         * chargeList=sqlMap.executeQueryForList("getChargeDetails",loanInstall);
//         * }else{ List getIntDetails=sqlMap.executeQueryForList("getIntDetails",
//         * loanInstall); HashMap hash=null; if (getIntDetails != null) { for(int
//         * i=0;i<getIntDetails.size();i++){ hash=(HashMap)getIntDetails.get(i);
//         * String trn_mode=CommonUtil.convertObjToStr(hash.get("TRN_CODE"));
//         * double
//         * pBal=CommonUtil.convertObjToDouble(hash.get("PBAL")).doubleValue();
//         * double
//         * iBal=CommonUtil.convertObjToDouble(hash.get("IBAL")).doubleValue();
//         * double pibal=
//         * CommonUtil.convertObjToDouble(hash.get("PIBAL")).doubleValue();
//         * double excess=
//         * CommonUtil.convertObjToDouble(hash.get("EXCESS_AMT")).doubleValue();
//         * pBal-=excess; if (pBal < totPrinciple) {
//         * insertPenal.put("CURR_MONTH_PRINCEPLE",new Double(pBal)); }
//         * if(trn_mode.equals("C*")){ insertPenal.put("CURR_MONTH_INT",
//         * String.valueOf(iBal + pibal)); insertPenal.put("PRINCIPLE_BAL",new
//         * Double(pBal)); insertPenal.put("EBAL",hash.get("EBAL")); break; }
//         * else { if (!trn_mode.equals("DP")) {
//         * insertPenal.put("CURR_MONTH_INT",String.valueOf(iBal + pibal)); }
//         * insertPenal.put("EBAL",hash.get("EBAL"));
//         * insertPenal.put("PRINCIPLE_BAL",new Double(pBal)); }
//         * System.out.println("int principel detailsINSIDE LOAN##"+insertPenal);
//         * } } getIntDetails=sqlMap.executeQueryForList("getPenalIntDetails",
//         * loanInstall); hash=(HashMap)getIntDetails.get(0);
//         * insertPenal.put("PENAL_INT",hash.get("PIBAL")); } }
//         *
//         * if(prodType !=null && prodType.equals("AD")) // Only AD { if
//         * (asAndWhenMap != null && asAndWhenMap.size() > 0) {
//         * if(asAndWhenMap.containsKey("AS_CUSTOMER_COMES") &&
//         * asAndWhenMap.get("AS_CUSTOMER_COMES").equals("Y")){ List
//         * facilitylst=sqlMap.executeQueryForList("LoneFacilityDetailAD",loanInstall);
//         * double
//         * interest=CommonUtil.convertObjToDouble(asAndWhenMap.get("INTEREST")).doubleValue();
//         * double penal
//         * =CommonUtil.convertObjToDouble(asAndWhenMap.get("LOAN_CLOSING_PENAL_INT")).doubleValue();
//         * if(facilitylst!=null && facilitylst.size()>0){ HashMap
//         * hash=(HashMap)facilitylst.get(0);
//         * hash.put("ACT_NUM",loanInstall.get("ACT_NUM"));
//         * hash.put("FROM_DT",hash.get("LAST_INT_CALC_DT"));
//         * hash.put("FROM_DT",DateUtil.addDays(((Date)hash.get("FROM_DT")),2));
//         * hash.put("TO_DATE",DateUtil.addDaysProperFormat(interestUptoDt,-1));
//         * facilitylst=sqlMap.executeQueryForList("getPaidPrincipleAD",hash);
//         * if(facilitylst!=null && facilitylst.size()>0){
//         * hash=(HashMap)facilitylst.get(0);
//         * interest-=CommonUtil.convertObjToDouble(hash.get("INTEREST")).doubleValue();
//         * penal-=CommonUtil.convertObjToDouble(hash.get("PENAL")).doubleValue();
//         * } } if (interest > 0) { insertPenal.put("CURR_MONTH_INT",new
//         * Double(interest)); } else { insertPenal.put("CURR_MONTH_INT",new
//         * Double(0)); } if (penal > 0) { insertPenal.put("PENAL_INT",new
//         * Double(penal)); } else { insertPenal.put("PENAL_INT",new Double(0));
//         * } insertPenal.put("INTEREST",asAndWhenMap.get("INTEREST"));
//         * insertPenal.put("LOAN_CLOSING_PENAL_INT",asAndWhenMap.get("LOAN_CLOSING_PENAL_INT"));
//         * chargeList=sqlMap.executeQueryForList("getChargeDetails",loanInstall);
//         * }else{ if(prodType !=null && prodType.equals("AD")) { List
//         * getIntDetails=sqlMap.executeQueryForList("getIntDetailsAD",
//         * loanInstall); HashMap hash=null;
//         *
//         * for(int i=0;i<getIntDetails.size();i++){
//         * hash=(HashMap)getIntDetails.get(i); String
//         * trn_mode=CommonUtil.convertObjToStr(hash.get("TRN_CODE")); double
//         * iBal=CommonUtil.convertObjToDouble(hash.get("IBAL")).doubleValue();
//         * double pibal=
//         * CommonUtil.convertObjToDouble(hash.get("PIBAL")).doubleValue();
//         * double excess=
//         * CommonUtil.convertObjToDouble(hash.get("EXCESS_AMT")).doubleValue();
//         * if(trn_mode.equals("C*")){
//         * insertPenal.put("CURR_MONTH_INT",String.valueOf(iBal + pibal));
//         * insertPenal.put("PRINCIPLE_BAL",new
//         * Double(CommonUtil.convertObjToDouble(hash.get("PBAL")).doubleValue()-excess));
//         * insertPenal.put("EBAL",hash.get("EBAL")); break; }else{
//         * insertPenal.put("CURR_MONTH_INT",String.valueOf(iBal + pibal));
//         * insertPenal.put("PRINCIPLE_BAL",new
//         * Double(CommonUtil.convertObjToDouble(hash.get("PBAL")).doubleValue()-excess));
//         * insertPenal.put("EBAL",hash.get("EBAL")); } System.out.println("int
//         * principel detailsINSIDE OD"+insertPenal); }
//         * getIntDetails=sqlMap.executeQueryForList("getPenalIntDetailsAD",
//         * loanInstall); if(getIntDetails.size()>0){
//         * hash=(HashMap)getIntDetails.get(0);
//         * insertPenal.put("PENAL_INT",hash.get("PIBAL")); }
//         * insertPenal.remove("PRINCIPLE_BAL");
//         *
//         * }
//         * }
//         * }
//         * }
//         * //Added By Suresh (Current Dt > To Date AND PBAL >0 in
//         * ADV_TRANS_DETAILS, Add Principle_Balance) if(prodType !=null &&
//         * prodType.equals("AD")){ double pBalance=0.0; Date expDt = null; List
//         * expDtList=sqlMap.executeQueryForList("getLoanExpDate", loanInstall);
//         * if(expDtList!=null && expDtList.size()>0){ whereMap = new HashMap();
//         * whereMap=(HashMap) expDtList.get(0); pBalance =
//         * CommonUtil.convertObjToDouble(whereMap.get("PBAL")).doubleValue();
//         * expDt = (Date)whereMap.get("TO_DT"); long diffDayPending =
//         * DateUtil.dateDiff(expDt,intUptoDt); System.out.println("#############
//         * Insert PBalance"+pBalance+"######diffDayPending :"+diffDayPending);
//         * if(diffDayPending>0 && pBalance>0){
//         * insertPenal.put("PRINCIPLE_BAL",new Double(pBalance)); } } }
//         * System.out.println("####### insertPenal : "+insertPenal); //Charges
//         * double chargeAmt =0.0; whereMap =new HashMap();
//         * whereMap.put("ACT_NUM",actNum); chargeAmt = getChargeAmount(whereMap,
//         * prodType); if(chargeAmt>0){ dataMap.put("CHARGES",
//         * String.valueOf(chargeAmt)); }else{ dataMap.put("CHARGES", "0"); }
//         * System.out.println("####### Single Row insertPenal : "+insertPenal);
//         * double totalDemand = 0.0; double principalAmount = 0.0;
//         * if(insertPenal.containsKey("CURR_MONTH_PRINCEPLE")){ principalAmount
//         * =
//         * CommonUtil.convertObjToDouble(insertPenal.get("CURR_MONTH_PRINCEPLE")).doubleValue();
//         * // Principal Amount totalDemand =
//         * CommonUtil.convertObjToDouble(insertPenal.get("CURR_MONTH_INT")).doubleValue()
//         * +
//         * CommonUtil.convertObjToDouble(insertPenal.get("PENAL_INT")).doubleValue()
//         * + chargeAmt; }else{ principalAmount =
//         * CommonUtil.convertObjToDouble(insertPenal.get("PRINCIPLE_BAL")).doubleValue();
//         * // Principal Amount totalDemand =
//         * CommonUtil.convertObjToDouble(insertPenal.get("CURR_MONTH_INT")).doubleValue()
//         * +
//         * CommonUtil.convertObjToDouble(insertPenal.get("PENAL_INT")).doubleValue()
//         * + chargeAmt; }
//         *
//         *
//         * if (inst_dt!=null && prodType.equals("TL")){
//         * if(DateUtil.dateDiff(intUptoDt, inst_dt)<=0 ){ if
//         * (installtype.equals("UNIFORM_PRINCIPLE_EMI") &&
//         * emi_uniform.equals("Y")) { principalAmount = principalAmount -
//         * CommonUtil.convertObjToDouble(insertPenal.get("CURR_MONTH_INT"));
//         * dataMap.put("PRINCIPAL", String.valueOf(principalAmount)); }else{
//         * dataMap.put("PRINCIPAL", String.valueOf(principalAmount)); } } else {
//         * if (installtype.equals("UNIFORM_PRINCIPLE_EMI") &&
//         * emi_uniform.equals("Y")) { principalAmount = principalAmount -
//         * CommonUtil.convertObjToDouble(insertPenal.get("CURR_MONTH_INT"));
//         * dataMap.put("PRINCIPAL", String.valueOf(principalAmount)); }else{
//         * dataMap.put("PRINCIPAL", "0"); principalAmount =0.0; } } // IF
//         * SALARY_RECOVERY = 'YES' ONLY if((principalAmount==0
//         * &&(!(installtype.equals("UNIFORM_PRINCIPLE_EMI") &&
//         * emi_uniform.equals("Y"))))
//         * ||(installtype.equals("UNIFORM_PRINCIPLE_EMI") &&
//         * emi_uniform.equals("Y"))){ System.out.println("############ instalAmt
//         * : "+instalAmt); HashMap balanceMap = new HashMap(); double
//         * balanceLoanAmt =0.0; double finalDemandAmt =0.0;
//         * System.out.println("############ actNum : "+actNum);
//         * balanceMap.put("ACCOUNTNO",actNum); List balannceAmtLst =
//         * sqlMap.executeQueryForList("getBalanceLoanPrincipalAmt", balanceMap);
//         * if(balannceAmtLst!=null && balannceAmtLst.size()>0){
//         * balanceMap=(HashMap) balannceAmtLst.get(0); balanceLoanAmt =
//         * CommonUtil.convertObjToDouble(balanceMap.get("LOAN_BALANCE_PRINCIPAL")).doubleValue();
//         * System.out.println("############ instalAmt : "+instalAmt);
//         * System.out.println("############ LoanBalancePrincAmt :
//         * "+balanceLoanAmt); double checkAmt =0.0; double totalPrincAmount=0.0;
//         * checkAmt = balanceLoanAmt - instalAmt; if(checkAmt>0){
//         * if(!(installtype.equals("UNIFORM_PRINCIPLE_EMI") &&
//         * emi_uniform.equals("Y"))){ balanceMap.put("ACCT_NUM",actNum);
//         * balanceMap.put("BALANCE_AMT",String.valueOf(checkAmt)); List
//         * sumInstLst =
//         * sqlMap.executeQueryForList("getPrincipalAmtGreaterThanBalAmt",
//         * balanceMap); if(sumInstLst!=null && sumInstLst.size()>0){ balanceMap
//         * =(HashMap) sumInstLst.get(0); totalPrincAmount =
//         * CommonUtil.convertObjToDouble(balanceMap.get("PRINCIPAL_AMOUNT")).doubleValue();
//         * totalPrincAmount+=instalAmt; finalDemandAmt = totalPrincAmount -
//         * paidAmount; if(balanceLoanAmt>finalDemandAmt){
//         * dataMap.put("PRINCIPAL", String.valueOf(finalDemandAmt));
//         * principalAmount = finalDemandAmt; }else{ dataMap.put("PRINCIPAL",
//         * String.valueOf(balanceLoanAmt)); principalAmount = balanceLoanAmt; }
//         * } System.out.println("############ finalDemandAmt :
//         * "+finalDemandAmt); } }else{ HashMap transMap=new HashMap();
//         * transMap.put("ACT_NUM",actNum);
//         * transMap.put("BRANCH_CODE",_branchCode); List sanctionLst =
//         * sqlMap.executeQueryForList("getNoOfDaysinLoan", transMap);
//         * if(sanctionLst !=null && sanctionLst.size()>0){ HashMap
//         * recordMap=(HashMap)sanctionLst.get(0); int repayFreq = 0; repayFreq =
//         * CommonUtil.convertObjToInt(recordMap.get("REPAYMENT_FREQUENCY"));
//         * if(repayFreq==1){ Date expiry_dt=null; expiry_dt =
//         * (Date)recordMap.get("TO_DT"); expiry_dt = (Date)expiry_dt.clone();
//         * System.out.println("########## expiry_dt : "+expiry_dt);
//         * if(DateUtil.dateDiff(intUptoDt, expiry_dt)>=0){ principalAmount =0.0;
//         * dataMap.put("PRINCIPAL", String.valueOf(principalAmount)); }else{
//         * dataMap.put("PRINCIPAL", String.valueOf(balanceLoanAmt));
//         * principalAmount = balanceLoanAmt; } }else{ dataMap.put("PRINCIPAL",
//         * String.valueOf(balanceLoanAmt)); principalAmount = balanceLoanAmt; }
//         * } } } } } if (prodType.equals("AD")){ if(principalAmount>0){
//         * dataMap.put("PRINCIPAL", String.valueOf(principalAmount)); }else{ if
//         * (installtype.equals("UNIFORM_PRINCIPLE_EMI") &&
//         * emi_uniform.equals("Y")) { dataMap.put("PRINCIPAL",
//         * String.valueOf(principalAmount)); } else { dataMap.put("PRINCIPAL",
//         * "0"); principalAmount = 0.0; } } }
//         *
//         * if(installtype.equals("UNIFORM_PRINCIPLE_EMI") &&
//         * emi_uniform.equals("Y")){ double dueamount=0; double penal=0; double
//         * totEmi=0; double paidEmi=0; double principle=0; double
//         * interst=Double.parseDouble(insertPenal.get("CURR_MONTH_INT").toString());
//         *
//         * System.out.println("interst"+interst); HashMap emi=new HashMap();
//         * Date upto=(Date)currDt.clone(); emi.put("ACC_NUM",actNum);
//         * emi.put("UP_TO",upto);
//         *
//         * List totalEmiList=sqlMap.executeQueryForList("TotalEmi",emi); if
//         * (totalEmiList != null && totalEmiList.size() > 0 && ((HashMap)
//         * totalEmiList.get(0)).get("TOTAL_AMOUNT") != null) { HashMap aMap=new
//         * HashMap(); aMap=(HashMap)totalEmiList.get(0);
//         * totEmi=Double.parseDouble(aMap.get("TOTAL_AMOUNT").toString());
//         * System.out.println("TOTAL_AMOUNTv"+totEmi); } else { totEmi=0;
//         *
//         * }
//         * HashMap paid = new HashMap(); paid.put("ACT_NUM",actNum);
//         * paid.put("BRANCH_CODE", _branchCode); List
//         * paidAmtemi=sqlMap.executeQueryForList("getPaidPrincipleEMI",
//         * loanInstall); if (paidAmtemi != null && paidAmtemi.size() > 0 &&
//         * ((HashMap) paidAmtemi.get(0)).get("PRINCIPLE") != null) { paid =
//         * (HashMap) paidAmtemi.get(0);
//         * System.out.println("!!!!asAndWhenMap:"+paid); //
//         * System.out.println("allInstallmentMap..."+allInstallmentMap);
//         * paidEmi=CommonUtil.convertObjToDouble(paid.get("PRINCIPLE")).doubleValue();
//         * System.out.println("paidEmi"+paidEmi); } else { paidEmi=0; }
//         * System.out.println("totEmi"+totEmi+"paidEmi"+paidEmi);
//         * dueamount=totEmi-paidEmi; double paidamount=paidEmi; if (dueamount <=
//         * 0) { dueamount =
//         * CommonUtil.convertObjToDouble(dataMap.get("PRINCIPAL")) + interst; }
//         * System.out.println("totalDemandsdas"+dueamount);
//         *
//         *
//         * System.out.println("+==========PENAL
//         * STARTS==============================+"); List
//         * scheduleList=sqlMap.executeQueryForList("getSchedules", emi); // List
//         * penalInterstRate=
//         *
//         *
//         * Date penalStrats=new Date(); if (scheduleList != null &&
//         * scheduleList.size() > 0) { for (int k = 0; k < scheduleList.size();
//         * k++) { HashMap eachInstall=new HashMap();
//         *
//         * eachInstall=(HashMap)scheduleList.get(k);
//         * System.out.println("eachInstall"+eachInstall); double scheduledEmi=
//         * Double.parseDouble(eachInstall.get("TOTAL_AMT").toString()); if
//         * (paidamount >= scheduledEmi) {
//         * System.out.println("111paidamount"+paidamount+"scheduledEmi"+scheduledEmi);
//         * paidamount=paidamount-scheduledEmi;
//         * System.out.println("paidamount"+paidamount); } else { String
//         * in_date=CommonUtil.convertObjToStr(eachInstall.get("INSTALLMENT_DT"));
//         * penalStrats=DateUtil.getDateMMDDYYYY(in_date); //
//         * penalStrats=DateUtil.getDateMMDDYYYY(eachInstall.get("INSTALLMENT_DT").toString());
//         * System.out.println("penalStrats....."+penalStrats); break; }
//         *
//         *
//         * }
//         * emi.put("FROM_DATE",penalStrats); List
//         * getPenalData=sqlMap.executeQueryForList("getPenalData", emi); List
//         * penalInterstRate=sqlMap.executeQueryForList("getPenalIntestRatefromMaintenance",
//         * emi); double interstPenal=0; double garce=0;
//         *
//         * List graceDays=sqlMap.executeQueryForList("getGracePeriodDays", emi);
//         * if (graceDays != null && graceDays.size() > 0) { HashMap map=new
//         * HashMap(); map=(HashMap)graceDays.get(0); if (map != null &&
//         * map.containsKey("GRACE_PERIOD_DAYS") && map.get("GRACE_PERIOD_DAYS")
//         * != null) { garce=
//         * Double.parseDouble(map.get("GRACE_PERIOD_DAYS").toString()); } else {
//         * garce=0; } } else { garce=0; }
//         *
//         *
//         * long gracedy=(long) garce; int graceint=(int) garce; if
//         * (penalInterstRate != null && penalInterstRate.size() > 0) { HashMap
//         * test=new HashMap(); test=(HashMap)penalInterstRate.get(0); if (test
//         * != null && test.containsKey("PENAL_INTEREST") &&
//         * test.get("PENAL_INTEREST") != null) { interstPenal=
//         * Double.parseDouble(test.get("PENAL_INTEREST").toString()); } else {
//         * List limitList=sqlMap.executeQueryForList("getLimitFromLoanSanc",
//         * emi); double
//         * limit=Double.parseDouble(((HashMap)limitList.get(0)).get("LIMIT").toString());
//         * emi.put("LIMIT",limit); List
//         * penalFromROI=sqlMap.executeQueryForList("getPenalIntestRatefromROI",
//         * emi); if (penalFromROI != null && penalFromROI.size() > 0) { test=new
//         * HashMap(); test=(HashMap)penalFromROI.get(0); interstPenal=
//         * Double.parseDouble(test.get("PENAL_INT").toString()); } } } else {
//         * List limitList=sqlMap.executeQueryForList("getLimitFromLoanSanc",
//         * emi); double
//         * limit=Double.parseDouble(((HashMap)limitList.get(0)).get("LIMIT").toString());
//         * emi.put("LIMIT",limit); List
//         * penalFromROI=sqlMap.executeQueryForList("getPenalIntestRatefromROI",
//         * emi); if (penalFromROI != null && penalFromROI.size() > 0) { HashMap
//         * test=new HashMap(); test=(HashMap)penalFromROI.get(0); interstPenal=
//         * Double.parseDouble(test.get("PENAL_INT").toString()); } }
//         * System.out.println("interstPenal...."+interstPenal); if (getPenalData
//         * != null && getPenalData.size() > 0) { for (int k = 0; k <
//         * getPenalData.size(); k++) { HashMap amap=new HashMap();
//         * amap=(HashMap)getPenalData.get(k);
//         *
//         *
//         *
//         * String
//         * in_date=CommonUtil.convertObjToStr(amap.get("INSTALLMENT_DT")); Date
//         * currntDate=DateUtil.getDateMMDDYYYY(in_date); currntDate=
//         * DateUtil.addDays(currntDate, graceint);
//         * System.out.println("5555currntDate.."+currntDate); //
//         * InterestCalculationTask incalc=new InterestCalculationTask(); HashMap
//         * holidayMap=new HashMap(); holidayMap.put("CURR_DATE", currntDate);
//         * holidayMap.put("BRANCH_CODE", _branchCode); //
//         * currntDate=incalc.holiydaychecking(holidayMap);
//         *
//         *
//         *
//         *
//         * currntDate = setProperDtFormat(currntDate); //
//         * System.out.println("instDate "+currntDate); //
//         * holidayMap.put("NEXT_DATE",currntDate); //
//         * holidayMap.put("BRANCH_CODE",_branchCode); holidayMap = new
//         * HashMap(); boolean checkHoliday=true; String str="any next working
//         * day"; System.out.println("instDate "+currntDate); currntDate =
//         * setProperDtFormat(currntDate);
//         * holidayMap.put("NEXT_DATE",currntDate);
//         * holidayMap.put("BRANCH_CODE",_branchCode); while(checkHoliday){
//         * boolean tholiday = false; List
//         * Holiday=sqlMap.executeQueryForList("checkHolidayDateOD",holidayMap);
//         * List
//         * weeklyOf=sqlMap.executeQueryForList("checkWeeklyOffOD",holidayMap);
//         * boolean isHoliday = Holiday.size()>0 ? true : false; boolean
//         * isWeekOff = weeklyOf.size()>0 ? true : false; if (isHoliday ||
//         * isWeekOff) { // System.out.println("#### diffDayPending Holiday True
//         * : "+diffDayPending); if(str.equals("any next working day")){ //
//         * diffDayPending-=1; currntDate.setDate(currntDate.getDate()+1); }else{
//         * //diffDayPending+=1; currntDate.setDate(currntDate.getDate()-1); }
//         * holidayMap.put("NEXT_DATE",currntDate); checkHoliday=true;
//         * System.out.println("#### holidayMap : "+holidayMap); }else{
//         * //System.out.println("#### diffDay Holiday False : "+diffDayPending);
//         * checkHoliday=false; } }
//         *
//         * System.out.println("currntDatemmm"+currntDate); // Date
//         * currntDate=DateUtil.getDateMMDDYYYY(amap.get("INSTALLMENT_DT").toString());
//         * System.out.println("DateUtil.dateDiff(currntDate,upto)"+DateUtil.dateDiff(currntDate,upto));
//         *
//         * long difference=DateUtil.dateDiff(currntDate,upto)-1; if (difference
//         * < 0) { difference=0; }
//         * System.out.println("difference..."+difference); double
//         * installment=Double.parseDouble(amap.get("TOTAL_AMT").toString());
//         * System.out.println("installmentsadeasdasd"+installment);
//         * penal=penal+((installment*difference*interstPenal)/36500);
//         * System.out.println("penallcalcuuu"+penal); }
//         *
//         * }
//         *
//         *
//         * }
//         * principle=dueamount-interst;
//         * System.out.println("mmmprinciple"+principle+"::penal"+penal+"::interst"+interst);
//         * totalDemand=principle+penal+interst;
//         * totalDemand=Math.round(totalDemand); principle=Math.round(principle);
//         * penal=Math.round(penal); interst=Math.round(interst);
//         * System.out.println("tttttoooo"+totalDemand); dataMap.put("INTEREST",
//         * Math.round(Double.parseDouble(insertPenal.get("CURR_MONTH_INT").toString())));
//         * dataMap.put("PENAL", penal); dataMap.put("TOTAL_DEMAND", new
//         * Double(totalDemand)); // dataMap.put("CLEAR_BALANCE", clear_balance);
//         * System.out.println("mmmmmmiiinnneee"+dataMap); } else {
//         * totalDemand+=principalAmount; System.out.println("totalDemand
//         * 333===="+totalDemand); dataMap.put("INTEREST",
//         * insertPenal.get("CURR_MONTH_INT")); dataMap.put("PENAL",
//         * insertPenal.get("PENAL_INT")); dataMap.put("TOTAL_DEMAND", new
//         * Double(totalDemand)); } if(totalDemand<=0){ dataMap = null; } }else{
//         * System.out.println("### Not Allow : "+checkDate); dataMap = null; } }
//         *
//         *
//         * System.out.println("####### Single Row DataMap : "+dataMap); return
//         * dataMap;
//         */
//    }
    

//    private double getChargeAmount(HashMap whereMap, String prodType) {   //Charges
//        double chargeAmount = 0.0;
//        try {
//            List chargeList = null;
//            String actNo = "";
//            HashMap recoverChrgMap = new HashMap();
//            actNo = CommonUtil.convertObjToStr(whereMap.get("ACT_NUM"));
//            chargeList = sqlMap.executeQueryForList("getChargeDetails", whereMap);
//            if (chargeList != null && chargeList.size() > 0) {
//                for (int i = 0; i < chargeList.size(); i++) {
//                    whereMap = (HashMap) chargeList.get(i);
//                    chargeAmount += CommonUtil.convertObjToDouble(whereMap.get("CHARGE_AMT")).doubleValue();
//                    double chrgAmt = 0.0;
//                    chrgAmt = CommonUtil.convertObjToDouble(whereMap.get("CHARGE_AMT")).doubleValue();
//                    if (chrgAmt > 0) {
//                        recoverChrgMap = new HashMap();
//                        recoverChrgMap.put("INT_CALC_UPTO_DT", intUptoDt);
//                        recoverChrgMap.put("ACT_NUM", actNo);
//                        recoverChrgMap.put("CHARGE_TYPE", CommonUtil.convertObjToStr(whereMap.get("CHARGE_TYPE")));
//                        recoverChrgMap.put("AMOUNT", CommonUtil.convertObjToStr(whereMap.get("CHARGE_AMT")));
//                        System.out.println("####### recoverChrgMap:" + recoverChrgMap);
//                        sqlMap.executeUpdate("insertRecoveryChargesList", recoverChrgMap);
//                    }
//                }
//            }
//            chargeList = null;
//            recoverChrgMap = null;
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//        return chargeAmount;
//    }

//    public HashMap interestCalculationTLAD(Object accountNo, Object prod_id, String prodType) throws Exception {
//        HashMap dataMap = new HashMap();
//        HashMap hash = new HashMap();
//        try {
//            hash.put("ACT_NUM", accountNo);
//            hash.put("PRODUCT_TYPE", prodType);
//            hash.put("PROD_ID", prod_id);
//            hash.put("TRANS_DT", interestUptoDt);
//            hash.put("INITIATED_BRANCH", _branchCode);
//            String mapNameForCalcInt = "IntCalculationDetail";
//            if (prodType.equals("AD")) {
//                mapNameForCalcInt = "IntCalculationDetailAD";
//            }
//            List lst = sqlMap.executeQueryForList(mapNameForCalcInt, hash);
//            System.out.println(accountNo + "," + prod_id + "," + "LIST   1>>>>>>" + lst);
//            if (lst != null && lst.size() > 0) {
//                hash = (HashMap) lst.get(0);
//                java.util.Iterator iterator = hash.keySet().iterator();
//              /*  while (iterator.hasNext()) {
//                    String key = iterator.next().toString();
//                    String value = hash.get(key).toString();
//                    System.out.println(key + " " + value);
//                }*/
//                if (hash.get("AS_CUSTOMER_COMES") != null && hash.get("AS_CUSTOMER_COMES").equals("N")) {
//                    hash = new HashMap();
//                    return hash;
//                }
//                hash.put("ACT_NUM", accountNo);
//                hash.put("PRODUCT_TYPE", prodType);
//                hash.put("PROD_ID", prod_id);
//                hash.put("TRANS_DT", intUptoDt);
//                hash.put("INITIATED_BRANCH", _branchCode);
//                hash.put("ACT_NUM", accountNo);
//                hash.put("BRANCH_ID", _branchCode);
//                hash.put("BRANCH_CODE", _branchCode);
//                hash.put("LOAN_ACCOUNT_CLOSING", "LOAN_ACCOUNT_CLOSING");
//                hash.put("CURR_DATE", interestUptoDt);
//                dataMap.put(CommonConstants.MAP_WHERE, hash);
//                System.out.println("map before intereest###" + dataMap);
//                hash = new SelectAllDAO().executeQuery(dataMap);
//                if (hash == null) {
//                    hash = new HashMap();
//                }
//                if (hash.containsKey("DATA") && hash.get("DATA") != null) {
//                    hash.putAll((HashMap) ((List) hash.get("DATA")).get(0));
//                }
//                hash.putAll((HashMap) dataMap.get(CommonConstants.MAP_WHERE));
//                System.out.println("hashinterestoutput###" + hash);
//            }
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//        return hash;
//    }
//
//    private Date setProperDtFormat(Date dt) {
//        Date tempDt = (Date) currDt.clone();
//        if (dt != null) {
//            tempDt.setDate(dt.getDate());
//            tempDt.setMonth(dt.getMonth());
//            tempDt.setYear(dt.getYear());
//            return tempDt;
//        }
//        return null;
//    }
  public HashMap interestCalculationTLAD(Object accountNo, Object prod_id, String prodType) throws Exception {
        HashMap dataMap = new HashMap();
        HashMap hash = new HashMap();
        try {
            hash.put("ACT_NUM", accountNo);
            hash.put("PRODUCT_TYPE", prodType);
            hash.put("PROD_ID", prod_id);
            hash.put("TRANS_DT", interestUptoDt);
            hash.put("INITIATED_BRANCH", _branchCode);
            String mapNameForCalcInt = "IntCalculationDetail";
            if (prodType.equals("AD")) {
                mapNameForCalcInt = "IntCalculationDetailAD";
            }
            List lst = sqlMap.executeQueryForList(mapNameForCalcInt, hash);
            System.out.println(accountNo + "," + prod_id + "," + "LIST   1>>>>>>" + lst);
            if (lst != null && lst.size() > 0) {
                hash = (HashMap) lst.get(0);
                java.util.Iterator iterator = hash.keySet().iterator();
              /*  while (iterator.hasNext()) {
                    String key = iterator.next().toString();
                    String value = hash.get(key).toString();
                    System.out.println(key + " " + value);
                }*/
                if (hash.get("AS_CUSTOMER_COMES") != null && hash.get("AS_CUSTOMER_COMES").equals("N")) {
                    hash = new HashMap();
                    return hash;
                }
                hash.put("ACT_NUM", accountNo);
                hash.put("PRODUCT_TYPE", prodType);
                hash.put("PROD_ID", prod_id);
                hash.put("TRANS_DT", intUptoDt);
                hash.put("INITIATED_BRANCH", _branchCode);
                hash.put("ACT_NUM", accountNo);
                hash.put("BRANCH_ID", _branchCode);
                hash.put("BRANCH_CODE", _branchCode);
                hash.put("LOAN_ACCOUNT_CLOSING", "LOAN_ACCOUNT_CLOSING");
                hash.put("CURR_DATE", interestUptoDt);
                hash.put("SAL_RECOVERY", "LIST_GENERATION");
                hash.put("SALARY_RECOVERY", "FROM_LIST_GENERATION");
                dataMap.put(CommonConstants.MAP_WHERE, hash);
                System.out.println("map before intereest###" + dataMap);
                hash = new SelectAllDAO().executeQuery(dataMap);
                if (hash == null) {
                    hash = new HashMap();
                }
                if (hash.containsKey("DATA") && hash.get("DATA") != null) {
                    hash.putAll((HashMap) ((List) hash.get("DATA")).get(0));
                }
                hash.putAll((HashMap) dataMap.get(CommonConstants.MAP_WHERE));
                System.out.println("hashinterestoutput###" + hash);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return hash;
    }
    private void destroyObjects() {
    }
    public Date getProperDateFormat(Object obj) {
        Date currDate1 = null;
        if (obj != null && obj.toString().length() > 0) {
            Date tempDt = DateUtil.getDateMMDDYYYY(CommonUtil.convertObjToStr(obj));
            currDate1 = (Date) currDt.clone();
            currDate1.setDate(tempDt.getDate());
            currDate1.setMonth(tempDt.getMonth());
            currDate1.setYear(tempDt.getYear());
        }
        return currDate1;
    }
}
