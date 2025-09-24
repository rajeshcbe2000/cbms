/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition.
 **
 *
 * SalaryStructureDAO.java
 *
 * Created on Wed May 26 10:59:57 GMT+05:30 2004
 */
package com.see.truetransact.serverside.common;

import com.ibatis.db.sqlmap.SqlMap;
import com.see.truetransact.serverutil.ServerUtil;
import com.see.truetransact.commonutil.CommonConstants;
import com.see.truetransact.commonutil.NoCommandException;
import com.see.truetransact.serverexception.TransRollbackException;
import com.see.truetransact.serverside.TTDAO;
import com.see.truetransact.serverexception.ServiceLocatorException;
import com.see.truetransact.servicelocator.ServiceLocator;
import com.see.truetransact.transferobject.common.SalaryStructureTO;
import com.see.truetransact.transferobject.common.CCAllowanceTO;
import com.see.truetransact.transferobject.common.HRAllowanceTO;
import com.see.truetransact.transferobject.common.DearnessAllowanceTO;
import com.see.truetransact.transferobject.common.TAllowanceTO;
import com.see.truetransact.transferobject.common.MAllowanceTO;
import com.see.truetransact.transferobject.common.OtherAllowanceTO;
import com.see.truetransact.commonutil.CommonUtil;
import com.see.truetransact.commonutil.DateUtil;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Date;

/**
 * SalaryStructure DAO.
 *
 * @author Sathiya
 *
 */
public class SalaryStructureDAO extends TTDAO {

    private static SqlMap sqlMap = null;
    private SalaryStructureTO objTO;
    private DearnessAllowanceTO objDATO;
    private CCAllowanceTO objCCATO;
    private HRAllowanceTO objHRATO;
    private TAllowanceTO objTATO;
    private MAllowanceTO objMATO;
    private OtherAllowanceTO objOATO;
    private ArrayList SalaryStructureTOs, DAllowancesTOs, CCAllowanceTOs, HRAllowanceTOs, TAllowanceTOs, MAllowanceTOs, OAllowanceTOs;
    private ArrayList deleteSalaryStructureTOs, deleteDAllowancesTOs, deleteCCAllowanceTOs, deleteHRAllowanceTOs,
            deleteTAllowanceTOs, deleteMAllowanceTOs, deleteOAllowanceTOs;
    HashMap resultMap = new HashMap();
    Date currDt = null;

    /**
     * Creates a new instance of SalaryStructureDAO
     */
    public SalaryStructureDAO() throws ServiceLocatorException {
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
        return returnMap;
    }

    private void insertData() throws Exception {
        try {
            sqlMap.startTransaction();
            double tempMaxSlNo = 0.0;
            if (SalaryStructureTOs != null && SalaryStructureTOs.size() > 0) {
                System.out.println("@#$@#$@#$SalaryStructureTOs:" + SalaryStructureTOs);
                int size = SalaryStructureTOs.size();
                for (int i = 0; i < size; i++) {
                    objTO = (SalaryStructureTO) SalaryStructureTOs.get(i);
                    if (i == 0) {
                        HashMap existingDateMap = new HashMap();
                        Date fromDate = DateUtil.getDateMMDDYYYY(CommonUtil.convertObjToStr(objTO.getFromDate()));
                        Date currDate = (Date) currDt.clone();
                        if (fromDate != null && fromDate.getDate() > 0) {
                            currDate.setDate(fromDate.getDate());
                            currDate.setMonth(fromDate.getMonth());
                            currDate.setYear(fromDate.getYear());
                        }
                        existingDateMap.put("GRADE", CommonUtil.convertObjToStr(objTO.getGrade()).toUpperCase());
                        existingDateMap.put("BRANCH_CODE", objTO.getBranchCode());
                        existingDateMap.put("FROM_DATE", currDate);
                        List existingLst = sqlMap.executeQueryForList("getSelectExistingRecords", existingDateMap);
                        if (existingLst != null && existingLst.size() > 0) {
                            existingDateMap = (HashMap) existingLst.get(0);
                            System.out.println("########insert existingLst :" + existingLst);
                            fromDate = DateUtil.getDateMMDDYYYY(CommonUtil.convertObjToStr(existingDateMap.get("FROM_DATE")));
                            Date newRecDate = DateUtil.getDateMMDDYYYY(CommonUtil.convertObjToStr(objTO.getFromDate()));
                            System.out.println("########insert newRecDate :" + newRecDate + "fromDate :" + fromDate);
                            if (DateUtil.dateDiff(fromDate, newRecDate) == 0) {
                                existingDateMap = new HashMap();
                                existingDateMap.put("FROM_DATE", currDate);
                                existingDateMap.put("GRADE", CommonUtil.convertObjToStr(objTO.getGrade()).toUpperCase());
                                existingDateMap.put("BRANCH_CODE", objTO.getBranchCode());
                                existingDateMap.put("STATUS", "INACTIVE");
                                existingDateMap.put("AUTHORIZE_STATUS", "INACTIVE");
                                sqlMap.executeUpdate("updateStatusDeletedasExistingRecSS", existingDateMap);
                            }
                        }
                    }
                    if (i == 0) {
                        String grade = CommonUtil.convertObjToStr(objTO.getGrade());
                        List slList = sqlMap.executeQueryForList("getSelectMaxofSlNoSS", grade);
                        if (slList != null && slList.size() > 0) {
                            HashMap listMap = new HashMap();
                            listMap = (HashMap) slList.get(0);
                            tempMaxSlNo = CommonUtil.convertObjToDouble(listMap.get("TEMP_SL_NO")).doubleValue();
                            tempMaxSlNo = tempMaxSlNo + 1;
                        }
                    }
                    if (CommonUtil.convertObjToStr(objTO.getStatus()).equals(CommonConstants.STATUS_CREATED)) {
                        objTO.setToDate(null);
                        objTO.setTempSlNo(new Double(tempMaxSlNo));
                        System.out.println("########insert objTO :" + objTO);
                        sqlMap.executeUpdate("insertSalaryStructureTO", objTO);
                    } else if (CommonUtil.convertObjToStr(objTO.getStatus()).equals(CommonConstants.STATUS_DELETED)) {
                        objTO.setStatus(CommonConstants.STATUS_DELETED);
                        System.out.println("########delete Records objTO :" + objTO);
                        sqlMap.executeUpdate("deleteSalaryStructureTO", objTO);
                    }
                }
            }
            if (DAllowancesTOs != null && DAllowancesTOs.size() > 0) {
                int size = DAllowancesTOs.size();
                for (int i = 0; i < size; i++) {
                    objDATO = (DearnessAllowanceTO) DAllowancesTOs.get(i);
                    if (i == 0) {
                        HashMap existingDateMap = new HashMap();
                        Date fromDate = DateUtil.getDateMMDDYYYY(CommonUtil.convertObjToStr(objDATO.getDAfromDate()));
                        Date currDate = (Date) currDt.clone();
                        if (fromDate != null && fromDate.getDate() > 0) {
                            currDate.setDate(fromDate.getDate());
                            currDate.setMonth(fromDate.getMonth());
                            currDate.setYear(fromDate.getYear());
                        }
                        existingDateMap.put("GRADE", CommonUtil.convertObjToStr(objDATO.getDAgrade()).toUpperCase());
                        existingDateMap.put("BRANCH_CODE", objDATO.getBranchCode());
                        existingDateMap.put("FROM_DATE", currDate);
                        List existingLst = sqlMap.executeQueryForList("getSelectExistingRecords", existingDateMap);
                        if (existingLst != null && existingLst.size() > 0) {
                            existingDateMap = (HashMap) existingLst.get(0);
                            System.out.println("########insert existingLst :" + existingLst);
                            fromDate = DateUtil.getDateMMDDYYYY(CommonUtil.convertObjToStr(existingDateMap.get("FROM_DATE")));
                            Date newRecDate = DateUtil.getDateMMDDYYYY(CommonUtil.convertObjToStr(objDATO.getDAfromDate()));
                            System.out.println("########insert newRecDate :" + newRecDate + "fromDate :" + fromDate);
                            if (DateUtil.dateDiff(fromDate, newRecDate) == 0) {
                                existingDateMap = new HashMap();
                                existingDateMap.put("FROM_DATE", currDate);
                                existingDateMap.put("GRADE", CommonUtil.convertObjToStr(objDATO.getDAgrade()).toUpperCase());
                                existingDateMap.put("BRANCH_CODE", objDATO.getBranchCode());
                                existingDateMap.put("STATUS", "INACTIVE");
                                existingDateMap.put("AUTHORIZE_STATUS", "INACTIVE");
                                sqlMap.executeUpdate("updateStatusDeletedasExistingRecDA", existingDateMap);
                            }
                        }
                    }
                    if (i == 0) {
                        List slList = sqlMap.executeQueryForList("getSelectMaxofSlNoDA", null);
                        if (slList != null && slList.size() > 0) {
                            HashMap listMap = new HashMap();
                            listMap = (HashMap) slList.get(0);
                            tempMaxSlNo = CommonUtil.convertObjToDouble(listMap.get("TEMP_SL_NO")).doubleValue();
                            tempMaxSlNo = tempMaxSlNo + 1;
                        }
                    }
                    if (CommonUtil.convertObjToStr(objDATO.getStatus()).equals(CommonConstants.STATUS_CREATED)) {
                        objDATO.setDAtoDate(null);
                        objDATO.setTempSlNo(new Double(tempMaxSlNo));
                        System.out.println("########insert DAobjTO :" + objDATO);
                        sqlMap.executeUpdate("insertDAllowanceTO", objDATO);
                    } else if (CommonUtil.convertObjToStr(objDATO.getStatus()).equals(CommonConstants.STATUS_DELETED)) {
                        objDATO.setStatus(CommonConstants.STATUS_DELETED);
                        System.out.println("########delete DARecords objDATO :" + objDATO);
                        sqlMap.executeUpdate("deletetDAllowanceTO", objDATO);
                    }
                }
            }
            if (CCAllowanceTOs != null && CCAllowanceTOs.size() > 0) {
                int size = CCAllowanceTOs.size();
                for (int i = 0; i < size; i++) {
                    objCCATO = (CCAllowanceTO) CCAllowanceTOs.get(i);
                    if (i == 0) {
                        HashMap existingDateMap = new HashMap();
                        Date fromDate = DateUtil.getDateMMDDYYYY(CommonUtil.convertObjToStr(objCCATO.getCCfromDate()));
                        Date currDate = (Date) currDt.clone();
                        if (fromDate != null && fromDate.getDate() > 0) {
                            currDate.setDate(fromDate.getDate());
                            currDate.setMonth(fromDate.getMonth());
                            currDate.setYear(fromDate.getYear());
                        }
                        existingDateMap.put("GRADE", CommonUtil.convertObjToStr(objCCATO.getCCgrade()).toUpperCase());
                        existingDateMap.put("BRANCH_CODE", objCCATO.getBranchCode());
                        existingDateMap.put("FROM_DATE", currDate);
                        List existingLst = sqlMap.executeQueryForList("getSelectExistingRecords", existingDateMap);
                        if (existingLst != null && existingLst.size() > 0) {
                            existingDateMap = (HashMap) existingLst.get(0);
                            System.out.println("########insert existingLst :" + existingLst);
                            fromDate = DateUtil.getDateMMDDYYYY(CommonUtil.convertObjToStr(existingDateMap.get("FROM_DATE")));
                            Date newRecDate = DateUtil.getDateMMDDYYYY(CommonUtil.convertObjToStr(objCCATO.getCCfromDate()));
                            System.out.println("########insert newRecDate :" + newRecDate + "fromDate :" + fromDate);
                            if (DateUtil.dateDiff(fromDate, newRecDate) == 0) {
                                existingDateMap = new HashMap();
                                existingDateMap.put("FROM_DATE", currDate);
                                existingDateMap.put("GRADE", CommonUtil.convertObjToStr(objCCATO.getCCgrade()).toUpperCase());
                                existingDateMap.put("BRANCH_CODE", objCCATO.getBranchCode());
                                existingDateMap.put("STATUS", "INACTIVE");
                                existingDateMap.put("AUTHORIZE_STATUS", "INACTIVE");
                                sqlMap.executeUpdate("updateStatusDeletedasExistingRecCCA", existingDateMap);
                            }
                        }
                    }
                    if (i == 0) {
                        List slList = sqlMap.executeQueryForList("getSelectMaxofSlNoCC", null);
                        if (slList != null && slList.size() > 0) {
                            HashMap listMap = new HashMap();
                            listMap = (HashMap) slList.get(0);
                            tempMaxSlNo = CommonUtil.convertObjToDouble(listMap.get("TEMP_SL_NO")).doubleValue();
                            tempMaxSlNo = tempMaxSlNo + 1;
                        }
                    }
                    if (CommonUtil.convertObjToStr(objCCATO.getStatus()).equals(CommonConstants.STATUS_CREATED)) {
                        objCCATO.setCCtoDate(null);
                        objCCATO.setTempSlNo(new Double(tempMaxSlNo));
                        System.out.println("########insert CCAobjTO :" + objCCATO);
                        sqlMap.executeUpdate("insertCCAllowanceTO", objCCATO);
                    } else if (CommonUtil.convertObjToStr(objCCATO.getStatus()).equals(CommonConstants.STATUS_DELETED)) {
                        objCCATO.setStatus(CommonConstants.STATUS_DELETED);
                        System.out.println("########delete CCARecords objCCATO :" + objCCATO);
                        //                        sqlMap.executeUpdate("deleteCCAllowanceTO", objCCATO);
                    }
                }
            }
            if (HRAllowanceTOs != null && HRAllowanceTOs.size() > 0) {
                int size = HRAllowanceTOs.size();
                for (int i = 0; i < size; i++) {
                    objHRATO = (HRAllowanceTO) HRAllowanceTOs.get(i);
                    if (i == 0) {
                        HashMap existingDateMap = new HashMap();
                        Date fromDate = DateUtil.getDateMMDDYYYY(CommonUtil.convertObjToStr(objHRATO.getHRAfromDate()));
                        Date currDate = (Date) currDt.clone();
                        if (fromDate != null && fromDate.getDate() > 0) {
                            currDate.setDate(fromDate.getDate());
                            currDate.setMonth(fromDate.getMonth());
                            currDate.setYear(fromDate.getYear());
                        }
                        existingDateMap.put("GRADE", CommonUtil.convertObjToStr(objHRATO.getHRAgrade()).toUpperCase());
                        existingDateMap.put("BRANCH_CODE", objHRATO.getBranchCode());
                        existingDateMap.put("FROM_DATE", currDate);
                        List existingLst = sqlMap.executeQueryForList("getSelectExistingRecords", existingDateMap);
                        if (existingLst != null && existingLst.size() > 0) {
                            existingDateMap = (HashMap) existingLst.get(0);
                            System.out.println("########insert existingLst :" + existingLst);
                            fromDate = DateUtil.getDateMMDDYYYY(CommonUtil.convertObjToStr(existingDateMap.get("FROM_DATE")));
                            Date newRecDate = DateUtil.getDateMMDDYYYY(CommonUtil.convertObjToStr(objHRATO.getHRAfromDate()));
                            System.out.println("########insert newRecDate :" + newRecDate + "fromDate :" + fromDate);
                            if (DateUtil.dateDiff(fromDate, newRecDate) == 0) {
                                existingDateMap = new HashMap();
                                existingDateMap.put("FROM_DATE", currDate);
                                existingDateMap.put("GRADE", CommonUtil.convertObjToStr(objHRATO.getHRAgrade()).toUpperCase());
                                existingDateMap.put("BRANCH_CODE", objHRATO.getBranchCode());
                                existingDateMap.put("STATUS", "INACTIVE");
                                existingDateMap.put("AUTHORIZE_STATUS", "INACTIVE");
                                sqlMap.executeUpdate("updateStatusDeletedasExistingRecHRA", existingDateMap);
                            }
                        }
                    }
                    if (i == 0) {
                        List slList = sqlMap.executeQueryForList("getSelectMaxofSlNoHRA", null);
                        if (slList != null && slList.size() > 0) {
                            HashMap listMap = new HashMap();
                            listMap = (HashMap) slList.get(0);
                            tempMaxSlNo = CommonUtil.convertObjToDouble(listMap.get("TEMP_SL_NO")).doubleValue();
                            tempMaxSlNo = tempMaxSlNo + 1;
                        }
                    }
                    if (CommonUtil.convertObjToStr(objHRATO.getStatus()).equals(CommonConstants.STATUS_CREATED)) {
                        objHRATO.setHRAtoDate(null);
                        objHRATO.setTempSlNo(new Double(tempMaxSlNo));
                        System.out.println("########insert HRAobjTO :" + objHRATO);
                        sqlMap.executeUpdate("insertHRAllowanceTO", objHRATO);
                    } else if (CommonUtil.convertObjToStr(objHRATO.getStatus()).equals(CommonConstants.STATUS_DELETED)) {
                        objHRATO.setStatus(CommonConstants.STATUS_DELETED);
                        System.out.println("########delete HRARecords objHRATO :" + objHRATO);
                        //                        sqlMap.executeUpdate("deleteHRAllowanceTO", objHRATO);
                    }
                }
            }
            if (TAllowanceTOs != null && TAllowanceTOs.size() > 0) {
                int size = TAllowanceTOs.size();
                for (int i = 0; i < size; i++) {
                    objTATO = (TAllowanceTO) TAllowanceTOs.get(i);
                    if (i == 0) {
                        HashMap existingDateMap = new HashMap();
                        Date fromDate = DateUtil.getDateMMDDYYYY(CommonUtil.convertObjToStr(objTATO.getTAfromDate()));
                        Date currDate = (Date) currDt.clone();
                        if (fromDate != null && fromDate.getDate() > 0) {
                            currDate.setDate(fromDate.getDate());
                            currDate.setMonth(fromDate.getMonth());
                            currDate.setYear(fromDate.getYear());
                        }
                        existingDateMap.put("GRADE", CommonUtil.convertObjToStr(objTATO.getTAgrade()).toUpperCase());
                        existingDateMap.put("BRANCH_CODE", objTATO.getBranchCode());
                        existingDateMap.put("FROM_DATE", currDate);
                        List existingLst = sqlMap.executeQueryForList("getSelectExistingRecords", existingDateMap);
                        if (existingLst != null && existingLst.size() > 0) {
                            existingDateMap = (HashMap) existingLst.get(0);
                            System.out.println("########insert existingLst :" + existingLst);
                            fromDate = DateUtil.getDateMMDDYYYY(CommonUtil.convertObjToStr(existingDateMap.get("FROM_DATE")));
                            Date newRecDate = DateUtil.getDateMMDDYYYY(CommonUtil.convertObjToStr(objTATO.getTAfromDate()));
                            System.out.println("########insert newRecDate :" + newRecDate + "fromDate :" + fromDate);
                            if (DateUtil.dateDiff(fromDate, newRecDate) == 0) {
                                existingDateMap = new HashMap();
                                existingDateMap.put("FROM_DATE", currDate);
                                existingDateMap.put("GRADE", CommonUtil.convertObjToStr(objTATO.getTAgrade()).toUpperCase());
                                existingDateMap.put("BRANCH_CODE", objTATO.getBranchCode());
                                existingDateMap.put("STATUS", "INACTIVE");
                                existingDateMap.put("AUTHORIZE_STATUS", "INACTIVE");
                                sqlMap.executeUpdate("updateStatusDeletedasExistingRecTA", existingDateMap);
                            }
                        }
                    }
                    if (i == 0) {
                        List slList = sqlMap.executeQueryForList("getSelectMaxofSlNoTA", null);
                        if (slList != null && slList.size() > 0) {
                            HashMap listMap = new HashMap();
                            listMap = (HashMap) slList.get(0);
                            tempMaxSlNo = CommonUtil.convertObjToDouble(listMap.get("TEMP_SL_NO")).doubleValue();
                            tempMaxSlNo = tempMaxSlNo + 1;
                        }
                    }
                    if (CommonUtil.convertObjToStr(objTATO.getStatus()).equals(CommonConstants.STATUS_CREATED)) {
                        objTATO.setTAtoDate(null);
                        objTATO.setTempSlNo(new Double(tempMaxSlNo));
                        System.out.println("########insert TAobjTO :" + objTATO);
                        sqlMap.executeUpdate("insertTAllowanceTO", objTATO);
                    } else if (CommonUtil.convertObjToStr(objTATO.getStatus()).equals(CommonConstants.STATUS_DELETED)) {
                        objTATO.setStatus(CommonConstants.STATUS_DELETED);
                        System.out.println("########delete TARecords objTATO :" + objTATO);
                        //                        sqlMap.executeUpdate("deleteTAllowanceTO", objTATO);
                    }
                }
            }
            if (MAllowanceTOs != null && MAllowanceTOs.size() > 0) {
                int size = MAllowanceTOs.size();
                for (int i = 0; i < size; i++) {
                    objMATO = (MAllowanceTO) MAllowanceTOs.get(i);
                    if (i == 0) {
                        HashMap existingDateMap = new HashMap();
                        Date fromDate = DateUtil.getDateMMDDYYYY(CommonUtil.convertObjToStr(objMATO.getMAfromDate()));
                        Date currDate = (Date) currDt.clone();
                        if (fromDate != null && fromDate.getDate() > 0) {
                            currDate.setDate(fromDate.getDate());
                            currDate.setMonth(fromDate.getMonth());
                            currDate.setYear(fromDate.getYear());
                        }
                        existingDateMap.put("GRADE", CommonUtil.convertObjToStr(objMATO.getMAgrade()).toUpperCase());
                        existingDateMap.put("BRANCH_CODE", objMATO.getBranchCode());
                        existingDateMap.put("FROM_DATE", currDate);
                        List existingLst = sqlMap.executeQueryForList("getSelectExistingRecords", existingDateMap);
                        if (existingLst != null && existingLst.size() > 0) {
                            existingDateMap = (HashMap) existingLst.get(0);
                            System.out.println("########insert existingLst :" + existingLst);
                            fromDate = DateUtil.getDateMMDDYYYY(CommonUtil.convertObjToStr(existingDateMap.get("FROM_DATE")));
                            Date newRecDate = DateUtil.getDateMMDDYYYY(CommonUtil.convertObjToStr(objMATO.getMAfromDate()));
                            System.out.println("########insert newRecDate :" + newRecDate + "fromDate :" + fromDate);
                            if (DateUtil.dateDiff(fromDate, newRecDate) == 0) {
                                existingDateMap = new HashMap();
                                existingDateMap.put("FROM_DATE", currDate);
                                existingDateMap.put("GRADE", CommonUtil.convertObjToStr(objMATO.getMAgrade()).toUpperCase());
                                existingDateMap.put("BRANCH_CODE", objMATO.getBranchCode());
                                existingDateMap.put("STATUS", "INACTIVE");
                                existingDateMap.put("AUTHORIZE_STATUS", "INACTIVE");
                                sqlMap.executeUpdate("updateStatusDeletedasExistingRecMA", existingDateMap);
                            }
                        }
                    }
                    if (i == 0) {
                        List slList = sqlMap.executeQueryForList("getSelectMaxofSlNoMA", null);
                        if (slList != null && slList.size() > 0) {
                            HashMap listMap = new HashMap();
                            listMap = (HashMap) slList.get(0);
                            tempMaxSlNo = CommonUtil.convertObjToDouble(listMap.get("TEMP_SL_NO")).doubleValue();
                            tempMaxSlNo = tempMaxSlNo + 1;
                        }
                    }
                    if (CommonUtil.convertObjToStr(objMATO.getStatus()).equals(CommonConstants.STATUS_CREATED)) {
                        objMATO.setMAtoDate(null);
                        objMATO.setTempSlNo(new Double(tempMaxSlNo));
                        System.out.println("########insert MAobjTO :" + objMATO);
                        sqlMap.executeUpdate("insertMAllowanceTO", objMATO);
                    } else if (CommonUtil.convertObjToStr(objMATO.getStatus()).equals(CommonConstants.STATUS_DELETED)) {
                        objMATO.setStatus(CommonConstants.STATUS_DELETED);
                        System.out.println("########delete MARecords objMATO :" + objMATO);
                        //                        sqlMap.executeUpdate("deleteMAllowanceTO", objMATO);
                    }
                }
            }
            if (OAllowanceTOs != null && OAllowanceTOs.size() > 0) {
                int size = OAllowanceTOs.size();
                for (int i = 0; i < size; i++) {
                    objOATO = (OtherAllowanceTO) OAllowanceTOs.get(i);
                    if (i == 0) {
                        HashMap existingDateMap = new HashMap();
                        Date fromDate = DateUtil.getDateMMDDYYYY(CommonUtil.convertObjToStr(objOATO.getOAfromDate()));
                        Date currDate = (Date) currDt.clone();
                        if (fromDate != null && fromDate.getDate() > 0) {
                            currDate.setDate(fromDate.getDate());
                            currDate.setMonth(fromDate.getMonth());
                            currDate.setYear(fromDate.getYear());
                        }
                        existingDateMap.put("GRADE", CommonUtil.convertObjToStr(objOATO.getOAgrade()).toUpperCase());
                        existingDateMap.put("BRANCH_CODE", objOATO.getBranchCode());
                        existingDateMap.put("FROM_DATE", currDate);
                        List existingLst = sqlMap.executeQueryForList("getSelectExistingRecords", existingDateMap);
                        if (existingLst != null && existingLst.size() > 0) {
                            existingDateMap = (HashMap) existingLst.get(0);
                            System.out.println("########insert existingLst :" + existingLst);
                            fromDate = DateUtil.getDateMMDDYYYY(CommonUtil.convertObjToStr(existingDateMap.get("FROM_DATE")));
                            Date newRecDate = DateUtil.getDateMMDDYYYY(CommonUtil.convertObjToStr(objOATO.getOAfromDate()));
                            System.out.println("########insert newRecDate :" + newRecDate + "fromDate :" + fromDate);
                            if (DateUtil.dateDiff(fromDate, newRecDate) == 0) {
                                existingDateMap = new HashMap();
                                existingDateMap.put("FROM_DATE", currDate);
                                existingDateMap.put("GRADE", CommonUtil.convertObjToStr(objOATO.getOAgrade()).toUpperCase());
                                existingDateMap.put("BRANCH_CODE", objOATO.getBranchCode());
                                existingDateMap.put("STATUS", "INACTIVE");
                                existingDateMap.put("AUTHORIZE_STATUS", "INACTIVE");
                                sqlMap.executeUpdate("updateStatusDeletedasExistingRecOA", existingDateMap);
                            }
                        }
                    }
                    if (i == 0) {
                        List slList = sqlMap.executeQueryForList("getSelectMaxofSlNoOA", null);
                        if (slList != null && slList.size() > 0) {
                            HashMap listMap = new HashMap();
                            listMap = (HashMap) slList.get(0);
                            tempMaxSlNo = CommonUtil.convertObjToDouble(listMap.get("TEMP_SL_NO")).doubleValue();
                            tempMaxSlNo = tempMaxSlNo + 1;
                        }
                    }
                    if (CommonUtil.convertObjToStr(objOATO.getStatus()).equals(CommonConstants.STATUS_CREATED)) {
                        objOATO.setOAtoDate(null);
                        objOATO.setTempSlNo(new Double(tempMaxSlNo));
                        System.out.println("########insert objOATO :" + objOATO);
                        sqlMap.executeUpdate("insertOAllowanceTO", objOATO);
                    } else if (CommonUtil.convertObjToStr(objOATO.getStatus()).equals(CommonConstants.STATUS_DELETED)) {
                        objOATO.setStatus(CommonConstants.STATUS_DELETED);
                        System.out.println("########delete OARecords objOATO :" + objOATO);
                        //                        sqlMap.executeUpdate("deleteOAllowanceTO", objOATO);
                    }
                }
            }
            sqlMap.commitTransaction();
        } catch (Exception e) {
            sqlMap.rollbackTransaction();
            e.printStackTrace();
            throw e;
        }
    }

    private void updateData(HashMap map) throws Exception {
        try {
            sqlMap.startTransaction();
            double tempMaxSlNo = 0.0;
            double subSlNo = 0.0;
            double tempSlno = 0.0;

            if (SalaryStructureTOs != null && SalaryStructureTOs.size() > 0) {
                int size = SalaryStructureTOs.size();
                System.out.println("######## objTO :" + size + "SalaryStructureTOs :" + SalaryStructureTOs);
                System.out.println("########updateData :" + map + "size :" + size);
                for (int i = 0; i < size; i++) {
                    objTO = (SalaryStructureTO) SalaryStructureTOs.get(i);
                    objTO.setStatusDate(currDt);
                    objTO.setToDate(null);
                    if (i == 0) {
                        tempSlno = CommonUtil.convertObjToDouble(objTO.getTempSlNo()).doubleValue();
                        String grade = CommonUtil.convertObjToStr(objTO.getGrade());
                        List slList = sqlMap.executeQueryForList("getSelectMaxofSlNoSS", grade);
                        if (slList != null && slList.size() > 0) {
                            HashMap listMap = new HashMap();
                            listMap = (HashMap) slList.get(0);
                            tempMaxSlNo = CommonUtil.convertObjToDouble(listMap.get("TEMP_SL_NO")).doubleValue();
                            subSlNo = CommonUtil.convertObjToDouble(listMap.get("SL_NO")).doubleValue();
                        }
                    }
                    if (CommonUtil.convertObjToStr(objTO.getStatus()).equals(CommonConstants.STATUS_MODIFIED)) {
                        objTO.setStatus(CommonConstants.STATUS_MODIFIED);
                        System.out.println("#@$@#$tempSlno" + tempSlno);
                        objTO.setTempSlNo(new Double(tempSlno));
                        System.out.println("########update objTO :" + objTO);
                        sqlMap.executeUpdate("updateSalaryStructureTO", objTO);
                        HashMap editMap = new HashMap();
                        editMap.put("GRADE", CommonUtil.convertObjToStr(objTO.getGrade()));
                        sqlMap.executeUpdate("updateAuthStatusToNullSS", editMap);
                    } else if (CommonUtil.convertObjToStr(objTO.getStatus()).equals(CommonConstants.STATUS_CREATED)) {
//                        subSlNo = subSlNo + 1;
//                        objTO.setSlNo(String.valueOf((long)subSlNo));
                        objTO.setTempSlNo(new Double(tempMaxSlNo));
                        System.out.println("########update Insert New Records objTO :" + objTO);
                        sqlMap.executeUpdate("insertSalaryStructureTO", objTO);
                    } else if (CommonUtil.convertObjToStr(objTO.getStatus()).equals(CommonConstants.STATUS_DELETED)) {
                        objTO.setStatus(CommonConstants.STATUS_DELETED);
                        System.out.println("########delete Records objTO :" + objTO);
                        sqlMap.executeUpdate("deleteSalaryStructureTO", objTO);
                    }
                }
            }
            if (DAllowancesTOs != null && DAllowancesTOs.size() > 0) {
                int size = DAllowancesTOs.size();
                System.out.println("######## objDATO :" + size + "DAllowancesTOs :" + DAllowancesTOs);
                for (int i = 0; i < size; i++) {
                    objDATO = (DearnessAllowanceTO) DAllowancesTOs.get(i);
                    objDATO.setStatusDate(currDt);
                    objDATO.setDAtoDate(null);
                    if (i == 0) {
                        List slList = sqlMap.executeQueryForList("getSelectMaxofSlNoDA", null);
                        if (slList != null && slList.size() > 0) {
                            HashMap listMap = new HashMap();
                            listMap = (HashMap) slList.get(0);
                            tempMaxSlNo = CommonUtil.convertObjToDouble(listMap.get("TEMP_SL_NO")).doubleValue();
                            subSlNo = CommonUtil.convertObjToDouble(listMap.get("SL_NO")).doubleValue();
                        }
                    }
                    if (CommonUtil.convertObjToStr(objDATO.getStatus()).equals(CommonConstants.STATUS_MODIFIED)) {
                        objDATO.setStatus(CommonConstants.STATUS_MODIFIED);
                        System.out.println("########update objDATO :" + objDATO);
                        sqlMap.executeUpdate("updateDAllowanceTO", objDATO);
                        HashMap editMap = new HashMap();
                        editMap.put("GRADE", CommonUtil.convertObjToStr(objDATO.getDAgrade()));
                        sqlMap.executeUpdate("updateAuthStatusToNullSS", editMap);
                    } else if (CommonUtil.convertObjToStr(objDATO.getStatus()).equals(CommonConstants.STATUS_CREATED)) {
//                        subSlNo = subSlNo + 1;
//                        objDATO.setDAslNo(String.valueOf((long)subSlNo));
                        objDATO.setTempSlNo(new Double(tempMaxSlNo));
                        System.out.println("########update Insert New Records objDATO :" + objDATO);
                        sqlMap.executeUpdate("insertDAllowanceTO", objDATO);
                    } else if (CommonUtil.convertObjToStr(objDATO.getStatus()).equals(CommonConstants.STATUS_DELETED)) {
                        objDATO.setStatus(CommonConstants.STATUS_DELETED);
                        System.out.println("########delete Records objDATO :" + objDATO);
                        sqlMap.executeUpdate("deletetDAllowanceTO", objDATO);
                    }
                }
            }
            if (CCAllowanceTOs != null && CCAllowanceTOs.size() > 0) {
                int size = CCAllowanceTOs.size();
                System.out.println("######## objCCATO :" + size + "CCAllowanceTOs :" + CCAllowanceTOs);
                for (int i = 0; i < size; i++) {
                    objCCATO = (CCAllowanceTO) CCAllowanceTOs.get(i);
                    objCCATO.setStatusDate(currDt);
                    objCCATO.setCCtoDate(null);
                    if (i == 0) {
                        List slList = sqlMap.executeQueryForList("getSelectMaxofSlNoCC", null);
                        if (slList != null && slList.size() > 0) {
                            HashMap listMap = new HashMap();
                            listMap = (HashMap) slList.get(0);
                            tempMaxSlNo = CommonUtil.convertObjToDouble(listMap.get("TEMP_SL_NO")).doubleValue();
                            subSlNo = CommonUtil.convertObjToDouble(listMap.get("SL_NO")).doubleValue();
                        }
                    }
                    if (CommonUtil.convertObjToStr(objCCATO.getStatus()).equals(CommonConstants.STATUS_MODIFIED)) {
                        objCCATO.setStatus(CommonConstants.STATUS_MODIFIED);
                        System.out.println("########update objCCATO :" + objCCATO);
                        sqlMap.executeUpdate("updateCCAllowanceTO", objCCATO);
                        HashMap editMap = new HashMap();
                        editMap.put("GRADE", CommonUtil.convertObjToStr(objCCATO.getCCgrade()));
                        sqlMap.executeUpdate("updateAuthStatusToNullSS", editMap);
                    } else if (CommonUtil.convertObjToStr(objCCATO.getStatus()).equals(CommonConstants.STATUS_CREATED)) {
//                        subSlNo = subSlNo + 1;
//                        objCCATO.setCCslNo(String.valueOf((long)subSlNo));
                        objCCATO.setTempSlNo(new Double(tempMaxSlNo));
                        System.out.println("########update Insert New Records objCCATO :" + objCCATO);
                        sqlMap.executeUpdate("insertCCAllowanceTO", objCCATO);
                    } else if (CommonUtil.convertObjToStr(objCCATO.getStatus()).equals(CommonConstants.STATUS_DELETED)) {
                        objCCATO.setStatus(CommonConstants.STATUS_DELETED);
                        System.out.println("########delete Records objCCATO :" + objCCATO);
                        sqlMap.executeUpdate("deletetCCAllowanceTO", objCCATO);
                    }
                }
            }
            if (HRAllowanceTOs != null && HRAllowanceTOs.size() > 0) {
                int size = HRAllowanceTOs.size();
                System.out.println("######## objHRATO :" + size + "HRAllowanceTOs :" + HRAllowanceTOs);
                for (int i = 0; i < size; i++) {
                    objHRATO = (HRAllowanceTO) HRAllowanceTOs.get(i);
                    objHRATO.setStatusDate(currDt);
                    objHRATO.setHRAtoDate(null);
                    if (i == 0) {
                        List slList = sqlMap.executeQueryForList("getSelectMaxofSlNoHRA", null);
                        if (slList != null && slList.size() > 0) {
                            HashMap listMap = new HashMap();
                            listMap = (HashMap) slList.get(0);
                            tempMaxSlNo = CommonUtil.convertObjToDouble(listMap.get("TEMP_SL_NO")).doubleValue();
                            subSlNo = CommonUtil.convertObjToDouble(listMap.get("SL_NO")).doubleValue();
                        }
                    }
                    if (CommonUtil.convertObjToStr(objHRATO.getStatus()).equals(CommonConstants.STATUS_MODIFIED)) {
                        objHRATO.setStatus(CommonConstants.STATUS_MODIFIED);
                        System.out.println("########update objHRATO :" + objHRATO);
                        sqlMap.executeUpdate("updateHRAllowanceTO", objHRATO);
                        HashMap editMap = new HashMap();
                        editMap.put("GRADE", CommonUtil.convertObjToStr(objHRATO.getHRAgrade()));
                        sqlMap.executeUpdate("updateAuthStatusToNullSS", editMap);
                    } else if (CommonUtil.convertObjToStr(objHRATO.getStatus()).equals(CommonConstants.STATUS_CREATED)) {
//                        subSlNo = subSlNo + 1;
//                        objHRATO.setHRAslNo(String.valueOf((long)subSlNo));
                        objHRATO.setTempSlNo(new Double(tempMaxSlNo));
                        System.out.println("########update Insert New Records objHRATO :" + objHRATO);
                        sqlMap.executeUpdate("insertHRAllowanceTO", objHRATO);
                    } else if (CommonUtil.convertObjToStr(objHRATO.getStatus()).equals(CommonConstants.STATUS_DELETED)) {
                        objHRATO.setStatus(CommonConstants.STATUS_DELETED);
                        System.out.println("########delete Records objHRATO :" + objHRATO);
                        sqlMap.executeUpdate("deletetHRAllowanceTO", objHRATO);
                    }
                }
            }
            if (TAllowanceTOs != null && TAllowanceTOs.size() > 0) {
                int size = TAllowanceTOs.size();
                System.out.println("######## objTATO :" + size + "TAllowanceTOs :" + TAllowanceTOs);
                for (int i = 0; i < size; i++) {
                    objTATO = (TAllowanceTO) TAllowanceTOs.get(i);
                    objTATO.setStatusDate(currDt);
                    if (i == 0) {
                        List slList = sqlMap.executeQueryForList("getSelectMaxofSlNoTA", null);
                        if (slList != null && slList.size() > 0) {
                            HashMap listMap = new HashMap();
                            listMap = (HashMap) slList.get(0);
                            tempMaxSlNo = CommonUtil.convertObjToDouble(listMap.get("TEMP_SL_NO")).doubleValue();
                            subSlNo = CommonUtil.convertObjToDouble(listMap.get("SL_NO")).doubleValue();
                        }
                    }
                    if (CommonUtil.convertObjToStr(objTATO.getStatus()).equals(CommonConstants.STATUS_MODIFIED)) {
                        objTATO.setStatus(CommonConstants.STATUS_MODIFIED);
                        System.out.println("########update objTATO :" + objTATO);
                        sqlMap.executeUpdate("updateTAllowanceTO", objTATO);
                        HashMap editMap = new HashMap();
                        editMap.put("GRADE", CommonUtil.convertObjToStr(objTATO.getTAgrade()));
                        sqlMap.executeUpdate("updateAuthStatusToNullSS", editMap);
                    } else if (CommonUtil.convertObjToStr(objTATO.getStatus()).equals(CommonConstants.STATUS_CREATED)) {
//                        subSlNo = subSlNo + 1;
//                        objTATO.setTAslNo(String.valueOf((long)subSlNo));
                        objTATO.setTempSlNo(new Double(tempMaxSlNo));
                        System.out.println("########update Insert New Records objTATO :" + objTATO);
                        sqlMap.executeUpdate("insertTAllowanceTO", objTATO);
                    } else if (CommonUtil.convertObjToStr(objTATO.getStatus()).equals(CommonConstants.STATUS_DELETED)) {
                        objTATO.setStatus(CommonConstants.STATUS_DELETED);
                        System.out.println("########delete Records objTATO :" + objTATO);
                        sqlMap.executeUpdate("deletetTAllowanceTO", objTATO);
                    }
                }
            }
            if (MAllowanceTOs != null && MAllowanceTOs.size() > 0) {
                int size = MAllowanceTOs.size();
                System.out.println("######## objMATO :" + size + "MAllowanceTOs :" + MAllowanceTOs);
                for (int i = 0; i < size; i++) {
                    objMATO = (MAllowanceTO) MAllowanceTOs.get(i);
                    objMATO.setStatusDate(currDt);
                    if (i == 0) {
                        List slList = sqlMap.executeQueryForList("getSelectMaxofSlNoMA", null);
                        if (slList != null && slList.size() > 0) {
                            HashMap listMap = new HashMap();
                            listMap = (HashMap) slList.get(0);
                            tempMaxSlNo = CommonUtil.convertObjToDouble(listMap.get("TEMP_SL_NO")).doubleValue();
                            subSlNo = CommonUtil.convertObjToDouble(listMap.get("SL_NO")).doubleValue();
                        }
                    }
                    if (CommonUtil.convertObjToStr(objMATO.getStatus()).equals(CommonConstants.STATUS_MODIFIED)) {
                        objMATO.setStatus(CommonConstants.STATUS_MODIFIED);
                        System.out.println("########update objMATO :" + objMATO);
                        sqlMap.executeUpdate("updateMAllowanceTO", objMATO);
                        HashMap editMap = new HashMap();
                        editMap.put("GRADE", CommonUtil.convertObjToStr(objMATO.getMAgrade()));
                        sqlMap.executeUpdate("updateAuthStatusToNullSS", editMap);
                    } else if (CommonUtil.convertObjToStr(objMATO.getStatus()).equals(CommonConstants.STATUS_CREATED)) {
//                        subSlNo = subSlNo + 1;
//                        objMATO.setMAslNo(String.valueOf((long)subSlNo));
                        objMATO.setTempSlNo(new Double(tempMaxSlNo));
                        System.out.println("########update Insert New Records objMATO :" + objMATO);
                        sqlMap.executeUpdate("insertMAllowanceTO", objMATO);
                    } else if (CommonUtil.convertObjToStr(objMATO.getStatus()).equals(CommonConstants.STATUS_DELETED)) {
                        objMATO.setStatus(CommonConstants.STATUS_DELETED);
                        System.out.println("########delete Records objMATO :" + objMATO);
                        sqlMap.executeUpdate("deletetMAllowanceTO", objMATO);
                    }
                }
            }
            if (OAllowanceTOs != null && OAllowanceTOs.size() > 0) {
                int size = OAllowanceTOs.size();
                System.out.println("######## objOATO :" + size + "OAllowanceTOs :" + OAllowanceTOs);
                for (int i = 0; i < size; i++) {
                    objOATO = (OtherAllowanceTO) OAllowanceTOs.get(i);
                    objOATO.setStatusDate(currDt);
                    if (i == 0) {
//                        List slList = sqlMap.executeQueryForList("getSelectMaxofSlNoOA",null);
//                        if(slList!=null && slList.size()>0){
//                            HashMap listMap = new HashMap() ;
//                            listMap = (HashMap)slList.get(0);
//                            tempMaxSlNo = CommonUtil.convertObjToDouble(listMap.get("TEMP_SL_NO")).doubleValue();
//                            subSlNo = CommonUtil.convertObjToDouble(listMap.get("SL_NO")).doubleValue();
//                        }
                    }
                    if (CommonUtil.convertObjToStr(objOATO.getStatus()).equals(CommonConstants.STATUS_MODIFIED)) {
                        objOATO.setStatus(CommonConstants.STATUS_MODIFIED);
                        System.out.println("########update objOATO :" + objOATO);
                        sqlMap.executeUpdate("updateOAllowanceTO", objOATO);
                        HashMap editMap = new HashMap();
                        editMap.put("GRADE", CommonUtil.convertObjToStr(objOATO.getOAgrade()));
                        sqlMap.executeUpdate("updateAuthStatusToNullSS", editMap);
                    } else if (CommonUtil.convertObjToStr(objOATO.getStatus()).equals(CommonConstants.STATUS_CREATED)) {
//                        subSlNo = subSlNo + 1;
//                        objOATO.setOAslNo(new Double((long)subSlNo));
                        objOATO.setTempSlNo(new Double(tempMaxSlNo));
                        System.out.println("########update Insert New Records objOATO :" + objOATO);
                        sqlMap.executeUpdate("insertOAllowanceTO", objOATO);
                    } else if (CommonUtil.convertObjToStr(objOATO.getStatus()).equals(CommonConstants.STATUS_DELETED)) {
                        objOATO.setStatus(CommonConstants.STATUS_DELETED);
                        System.out.println("########delete Records objOATO :" + objOATO);
                        sqlMap.executeUpdate("deletetOAllowanceTO", objOATO);
                    }
                }
            }
            sqlMap.commitTransaction();
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

    private void updateDeleteStatusData(HashMap map) throws Exception {
        try {
            int size = SalaryStructureTOs.size();
            System.out.println("########updatesize :" + size);
            sqlMap.startTransaction();
            //            ArrayList lst = new ArrayList();
            for (int i = 0; i < size; i++) {
                objTO = (SalaryStructureTO) SalaryStructureTOs.get(0);
                objTO.setSlNo(String.valueOf(i + 1));
                objTO.setToDate(null);
                sqlMap.executeUpdate("updateDeleteLienTO", objTO);
            }
            sqlMap.commitTransaction();
        } catch (Exception e) {
            sqlMap.rollbackTransaction();
            e.printStackTrace();
            throw new TransRollbackException(e);
        }
    }

    public static void main(String str[]) {
        try {
            SalaryStructureDAO dao = new SalaryStructureDAO();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public HashMap execute(HashMap map) throws Exception {
        System.out.println("SalaryStructure execute###" + map);
        _branchCode = (String) map.get(CommonConstants.BRANCH_ID);
        currDt = ServerUtil.getCurrentDate(_branchCode);
        SalaryStructureTOs = (ArrayList) map.get("SalaryStructureTOs");
        DAllowancesTOs = (ArrayList) map.get("DAllowancesTOs");
        CCAllowanceTOs = (ArrayList) map.get("CCAllowanceTOs");
        HRAllowanceTOs = (ArrayList) map.get("HRAllowanceTOs");
        TAllowanceTOs = (ArrayList) map.get("TAllowanceTOs");
        MAllowanceTOs = (ArrayList) map.get("MAllowanceTOs");
        OAllowanceTOs = (ArrayList) map.get("OAllowanceTOs");

        deleteSalaryStructureTOs = (ArrayList) map.get("deleteSalaryStructure");
        deleteDAllowancesTOs = (ArrayList) map.get("deleteDAllowancesList");
        deleteCCAllowanceTOs = (ArrayList) map.get("deleteCCAllowanceList");
        deleteHRAllowanceTOs = (ArrayList) map.get("deleteHRAllowanceList");
        deleteTAllowanceTOs = (ArrayList) map.get("deleteTAllowanceList");
        deleteMAllowanceTOs = (ArrayList) map.get("deleteMAllowanceList");
        deleteOAllowanceTOs = (ArrayList) map.get("deleteOAllowanceList");
        final String command = (String) map.get("COMMAND");
        if (SalaryStructureTOs != null && SalaryStructureTOs.size() > 0) {
            if (command.equals(CommonConstants.TOSTATUS_INSERT)) {
                insertData();
            } else if (command.equals(CommonConstants.TOSTATUS_UPDATE)) {
                updateData(map);
                //            } else if (command.equals(CommonConstants.TOSTATUS_DELETE)){
                //                updateDeleteStatusData(map);
            } else {
                throw new NoCommandException();
            }
        }

        map = null;
        destroyObjects();
        return resultMap;
    }

    public HashMap executeQuery(HashMap obj) throws Exception {
        _branchCode = (String) obj.get(CommonConstants.BRANCH_ID);
        currDt = ServerUtil.getCurrentDate(_branchCode);
        return getData(obj);
    }

    private void destroyObjects() {
        objTO = null;
        objDATO = null;
        objCCATO = null;
        objHRATO = null;
        objTATO = null;
        objMATO = null;
        objOATO = null;
        SalaryStructureTOs = null;
        DAllowancesTOs = null;
        CCAllowanceTOs = null;
        HRAllowanceTOs = null;
        TAllowanceTOs = null;
        MAllowanceTOs = null;
        OAllowanceTOs = null;
        deleteSalaryStructureTOs = null;
        deleteDAllowancesTOs = null;
        deleteCCAllowanceTOs = null;
        deleteHRAllowanceTOs = null;
        deleteTAllowanceTOs = null;
        deleteMAllowanceTOs = null;
        deleteOAllowanceTOs = null;

    }
}
