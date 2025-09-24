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
import com.see.truetransact.transferobject.common.HaltingAllowanceTO;
import com.see.truetransact.transferobject.common.MisecllaniousDeductionTO;
import com.see.truetransact.transferobject.common.GratuityTO;
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
public class MisecllaniousDeductionDAO extends TTDAO {

    private static SqlMap sqlMap = null;
    private HaltingAllowanceTO objHATO;
    private MisecllaniousDeductionTO objMDTO;
    private GratuityTO objGATO;
    private ArrayList HaltingTOs, MisecllaniousDeductionTOs, GratuityTOs, HRAllowanceTOs, TAllowanceTOs, MAllowanceTOs, OAllowanceTOs;
    private ArrayList deleteHaltingList, deleteMisecllaniousDeductionList, deleteGratuityList, deleteHRAllowanceTOs,
            deleteTAllowanceTOs, deleteMAllowanceTOs, deleteOAllowanceTOs;
    HashMap resultMap = new HashMap();
    Date currDt = null;

    /**
     * Creates a new instance of SalaryStructureDAO
     */
    public MisecllaniousDeductionDAO() throws ServiceLocatorException {
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
            System.out.println("@#$@#$inside insert deleteMisecllaniousDeductionList:" + deleteMisecllaniousDeductionList);
            double tempMaxSlNo = 0.0;
            if (deleteHaltingList != null && deleteHaltingList.size() > 0) {
                int size = deleteHaltingList.size();
                for (int i = 0; i < size; i++) {
                    objHATO = (HaltingAllowanceTO) deleteHaltingList.get(i);
                    if (CommonUtil.convertObjToStr(objHATO.getStatus()).equals(CommonConstants.STATUS_DELETED)) {
                        objHATO.setStatus(CommonConstants.STATUS_DELETED);
                        System.out.println("########delete Records objHATO :" + objHATO);
                        sqlMap.executeUpdate("deleteHaltingAllowanceTO", objHATO);
                    }
                }
                deleteHaltingList = null;
            }
            if (HaltingTOs != null && HaltingTOs.size() > 0) {
                int size = HaltingTOs.size();
                for (int i = 0; i < size; i++) {
                    objHATO = (HaltingAllowanceTO) HaltingTOs.get(i);
                    if (i == 0) {
                        List slList = sqlMap.executeQueryForList("getSelectMaxofSlNoHA", null);
                        if (slList != null && slList.size() > 0) {
                            HashMap listMap = new HashMap();
                            listMap = (HashMap) slList.get(0);
                            tempMaxSlNo = CommonUtil.convertObjToDouble(listMap.get("TEMP_SL_NO")).doubleValue();
                            tempMaxSlNo = tempMaxSlNo + 1;
                        }
                    }
                    if (CommonUtil.convertObjToStr(objHATO.getStatus()).equals(CommonConstants.STATUS_CREATED)) {
                        objHATO.setHalting_to_date(null);
                        objHATO.setTempSlNo(new Double((long) tempMaxSlNo));
                        System.out.println("########insert objHATO :" + objHATO);
                        sqlMap.executeUpdate("insertHaltingAllowanceTO", objHATO);
                    } else if (CommonUtil.convertObjToStr(objHATO.getStatus()).equals(CommonConstants.STATUS_DELETED)) {
                        objHATO.setStatus(CommonConstants.STATUS_DELETED);
                        System.out.println("########delete Records objHATO :" + objHATO);
                        sqlMap.executeUpdate("deleteHaltingAllowanceTO", objHATO);
                    }
                }
            }
            if (deleteMisecllaniousDeductionList != null && deleteMisecllaniousDeductionList.size() > 0) {
                int size = deleteMisecllaniousDeductionList.size();
                System.out.println("@#$@#$@#$size:" + size);
                for (int i = 0; i < size; i++) {
                    objMDTO = (MisecllaniousDeductionTO) deleteMisecllaniousDeductionList.get(i);
                    if (CommonUtil.convertObjToStr(objMDTO.getStatus()).equals(CommonConstants.STATUS_DELETED)) {
                        objMDTO.setStatus(CommonConstants.STATUS_DELETED);
                        System.out.println("########delete Records objMDTO :" + objMDTO);
                        sqlMap.executeUpdate("deleteMisecllaniousAllowanceTO", objMDTO);
                    }
                }
                deleteMisecllaniousDeductionList = null;
            }
            if (MisecllaniousDeductionTOs != null && MisecllaniousDeductionTOs.size() > 0) {
                int size = MisecllaniousDeductionTOs.size();
                for (int i = 0; i < size; i++) {
                    objMDTO = (MisecllaniousDeductionTO) MisecllaniousDeductionTOs.get(i);
                    System.out.println("!@#!@#!@#objMDTO:" + objMDTO);
                    if (i == 0) {
                        List slList = sqlMap.executeQueryForList("getSelectMaxofSlNoMD", null);
                        if (slList != null && slList.size() > 0) {
                            HashMap listMap = new HashMap();
                            listMap = (HashMap) slList.get(0);
                            tempMaxSlNo = CommonUtil.convertObjToDouble(listMap.get("TEMP_SL_NO")).doubleValue();
                            tempMaxSlNo = tempMaxSlNo + 1;
                        }
                    }
                    if (CommonUtil.convertObjToStr(objMDTO.getStatus()).equals(CommonConstants.STATUS_DELETED)) {
                        objMDTO.setStatus(CommonConstants.STATUS_DELETED);
                        System.out.println("########delete DARecords objMDTO :" + objMDTO);
                        sqlMap.executeUpdate("deleteMisecllaniousAllowanceTO", objMDTO);
                    } else if (CommonUtil.convertObjToStr(objMDTO.getStatus()).equals(CommonConstants.STATUS_CREATED)) {
                        objMDTO.setMd_to_date(null);
                        objMDTO.setTempSlNo(new Double((long) tempMaxSlNo));
                        System.out.println("########insert DAobjTO :" + objMDTO);
                        sqlMap.executeUpdate("insertMisecllaniousAllowanceTO", objMDTO);
                    }
                }
            }
            if (deleteGratuityList != null && deleteGratuityList.size() > 0) {
                int size = deleteGratuityList.size();
                for (int i = 0; i < size; i++) {
                    objGATO = (GratuityTO) deleteGratuityList.get(i);
                    if (CommonUtil.convertObjToStr(objGATO.getStatus()).equals(CommonConstants.STATUS_DELETED)) {
                        objGATO.setStatus(CommonConstants.STATUS_DELETED);
                        System.out.println("########delete Records objGATO :" + objGATO);
                        sqlMap.executeUpdate("deleteGratuityTO", objGATO);
                    }
                }
                deleteGratuityList = null;
            }
            if (GratuityTOs != null && GratuityTOs.size() > 0) {
                int size = GratuityTOs.size();
                for (int i = 0; i < size; i++) {
                    objGATO = (GratuityTO) GratuityTOs.get(i);
                    if (i == 0) {
                        List slList = sqlMap.executeQueryForList("getSelectMaxofSlNoGA", null);
                        if (slList != null && slList.size() > 0) {
                            HashMap listMap = new HashMap();
                            listMap = (HashMap) slList.get(0);
                            tempMaxSlNo = CommonUtil.convertObjToDouble(listMap.get("TEMP_SL_NO")).doubleValue();
                            tempMaxSlNo = tempMaxSlNo + 1;
                        }
                    }
                    if (CommonUtil.convertObjToStr(objGATO.getStatus()).equals(CommonConstants.STATUS_DELETED)) {
                        objGATO.setStatus(CommonConstants.STATUS_DELETED);
                        System.out.println("########delete gratuity objGATO :" + objGATO);
                        sqlMap.executeUpdate("deleteGratuityTO", objGATO);
                    } else if (CommonUtil.convertObjToStr(objGATO.getStatus()).equals(CommonConstants.STATUS_CREATED)) {
                        objGATO.setGratuity_to_date(null);
                        objGATO.setTempSlNo(new Double((long) tempMaxSlNo));
                        System.out.println("########insert objGATO :" + objGATO);
                        sqlMap.executeUpdate("insertGratuityTO", objGATO);
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
            System.out.println("@#$@#$inside update deleteMisecllaniousDeductionList:" + deleteMisecllaniousDeductionList);
            if (deleteHaltingList != null && deleteHaltingList.size() > 0) {
                int size = deleteHaltingList.size();
                for (int i = 0; i < size; i++) {
                    objHATO = (HaltingAllowanceTO) deleteHaltingList.get(i);
                    if (CommonUtil.convertObjToStr(objHATO.getStatus()).equals(CommonConstants.STATUS_DELETED)) {
                        objHATO.setStatus(CommonConstants.STATUS_DELETED);
                        System.out.println("########delete Records objHATO :" + objHATO);
                        sqlMap.executeUpdate("deleteHaltingAllowanceTO", objHATO);
                    }
                }
                deleteHaltingList = null;
            }
            if (HaltingTOs != null && HaltingTOs.size() > 0) {
                double tempMaxSlNo = 0;
                double subSlNo = 0;
                double tempSlno = 0.0;
                int size = HaltingTOs.size();
                System.out.println("######## objHATO :" + size + "SalaryStructureTOs :" + HaltingTOs);
                System.out.println("########updateData :" + map + "size :" + size);
                for (int i = 0; i < size; i++) {
                    objHATO = (HaltingAllowanceTO) HaltingTOs.get(i);
                    objHATO.setStatusDate(currDt);
                    objHATO.setHalting_to_date(null);
                    System.out.println("@#$@#$@#4objHATO:" + objHATO);
                    if (i == 0) {
                        tempSlno = CommonUtil.convertObjToDouble(objHATO.getTempSlNo()).doubleValue();
                        String grade = CommonUtil.convertObjToStr(objHATO.getHalting_grade());
                        List slList = sqlMap.executeQueryForList("getSelectMaxofSlNoSS", grade);
                        if (slList != null && slList.size() > 0) {
                            HashMap listMap = new HashMap();
                            listMap = (HashMap) slList.get(0);
                            tempMaxSlNo = CommonUtil.convertObjToDouble(listMap.get("TEMP_SL_NO")).doubleValue();
                            subSlNo = CommonUtil.convertObjToDouble(listMap.get("SL_NO")).doubleValue();
                        }
                    }
                    //                    if(i == 0){
                    //                        List slList = sqlMap.executeQueryForList("getSelectMaxofSlNoHA",null);
                    //                        if(slList!=null && slList.size()>0){
                    //                            HashMap listMap = new HashMap() ;
                    //                            listMap = (HashMap)slList.get(0);
                    //                            tempMaxSlNo = CommonUtil.convertObjToLong(listMap.get("TEMP_SL_NO"));
                    //                            subSlNo = CommonUtil.convertObjToLong(listMap.get("SL_NO"));
                    //                            System.out.println("tempMaxSlNo :"+tempMaxSlNo +"subSlNo :"+subSlNo);
                    //                        }
                    //                    }else{
                    //                        if(!CommonUtil.convertObjToStr(objHATO.getStatus()).equals(CommonConstants.STATUS_DELETED)){
                    //                            subSlNo = subSlNo + 1;
                    //                        }
                    //                    }
                    System.out.println("tempMaxSlNo :" + tempMaxSlNo + "subSlNo :" + subSlNo);
                    if (CommonUtil.convertObjToStr(objHATO.getStatus()).equals(CommonConstants.STATUS_DELETED)) {
                        objHATO.setStatus(CommonConstants.STATUS_DELETED);
//                        objHATO.setHalting_sl_no(new Double((long)subSlNo));
                        objHATO.setTempSlNo(new Double(tempMaxSlNo));
                        System.out.println("########delete Records objHATO :" + objHATO);
                        sqlMap.executeUpdate("deleteHaltingAllowanceTO", objHATO);
                    } else if (CommonUtil.convertObjToStr(objHATO.getStatus()).equals(CommonConstants.STATUS_MODIFIED)) {
                        objHATO.setTempSlNo(new Double(tempMaxSlNo));
//                        objHATO.setHalting_sl_no(new Double((long)subSlNo));
                        objHATO.setStatus(CommonConstants.STATUS_MODIFIED);
                        System.out.println("tempMaxSlNo :" + tempMaxSlNo + "subSlNo :" + subSlNo);
                        System.out.println("########update objHATO :" + objHATO);
                        sqlMap.executeUpdate("updateHaltingAllowanceTO", objHATO);
                    } else if (CommonUtil.convertObjToStr(objHATO.getStatus()).equals(CommonConstants.STATUS_CREATED)) {
                        //                        subSlNo = subSlNo + 1;
//                        objHATO.setHalting_sl_no(new Double((long)subSlNo));
                        objHATO.setTempSlNo(new Double(tempMaxSlNo));
                        System.out.println("tempMaxSlNo :" + tempMaxSlNo + "subSlNo :" + subSlNo);
                        System.out.println("########update Insert New Records objHATO :" + objHATO);
                        sqlMap.executeUpdate("insertHaltingAllowanceTO", objHATO);
                    }
                }
            }
            if (deleteMisecllaniousDeductionList != null && deleteMisecllaniousDeductionList.size() > 0) {
                int size = deleteMisecllaniousDeductionList.size();
                System.out.println("@#$@#$inside mscl deduction:" + size + ":deleteMisecllaniousDeductionList:" + deleteMisecllaniousDeductionList);
                for (int i = 0; i < size; i++) {
                    objMDTO = (MisecllaniousDeductionTO) deleteMisecllaniousDeductionList.get(i);
                    if (CommonUtil.convertObjToStr(objMDTO.getStatus()).equals(CommonConstants.STATUS_DELETED)) {
                        objMDTO.setStatus(CommonConstants.STATUS_DELETED);
                        objMDTO.setStatusDate(currDt);
                        System.out.println("########delete Records objMDTO :" + objMDTO);
                        sqlMap.executeUpdate("deleteMisecllaniousAllowanceTO", objMDTO);
                    }
                }
                deleteMisecllaniousDeductionList = null;
            }
            if (MisecllaniousDeductionTOs != null && MisecllaniousDeductionTOs.size() > 0) {
                double tempMaxSlNo = 0;
                double subSlNo = 0;
                double tempSlno = 0.0;
                int size = MisecllaniousDeductionTOs.size();
                System.out.println("######## objMDTO :" + size + "MisecllaniousDeductionTOs :" + MisecllaniousDeductionTOs);
                for (int i = 0; i < size; i++) {
                    objMDTO = (MisecllaniousDeductionTO) MisecllaniousDeductionTOs.get(i);
                    objMDTO.setStatusDate(currDt);
//                    objMDTO.setMd_from_date(null);
                    if (i == 0) {
                        tempSlno = CommonUtil.convertObjToDouble(objMDTO.getTempSlNo()).doubleValue();
                        String grade = CommonUtil.convertObjToStr(objMDTO.getMd_grade());
                        List slList = sqlMap.executeQueryForList("getSelectMaxofSlNoSS", grade);
                        if (slList != null && slList.size() > 0) {
                            HashMap listMap = new HashMap();
                            listMap = (HashMap) slList.get(0);
                            tempMaxSlNo = CommonUtil.convertObjToDouble(listMap.get("TEMP_SL_NO")).doubleValue();
                            subSlNo = CommonUtil.convertObjToDouble(listMap.get("SL_NO")).doubleValue();
                        }
                    }
                    if (CommonUtil.convertObjToStr(objMDTO.getStatus()).equals(CommonConstants.STATUS_DELETED)) {
                        objMDTO.setStatus(CommonConstants.STATUS_DELETED);
//                        objMDTO.setMd_sl_no(new Double((long)subSlNo));
                        objMDTO.setTempSlNo(new Double(tempMaxSlNo));
                        System.out.println("########delete Records objMDTO :" + objMDTO);
                        sqlMap.executeUpdate("deleteMisecllaniousAllowanceTO", objMDTO);
                    } else if (CommonUtil.convertObjToStr(objMDTO.getStatus()).equals(CommonConstants.STATUS_MODIFIED)) {
                        objMDTO.setStatus(CommonConstants.STATUS_MODIFIED);
                        objMDTO.setTempSlNo(new Double(tempMaxSlNo));
                        System.out.println("########update objMDTO :" + objMDTO);
                        sqlMap.executeUpdate("updateMisecllaniousAllowanceTO", objMDTO);
                    } else if (CommonUtil.convertObjToStr(objMDTO.getStatus()).equals(CommonConstants.STATUS_CREATED)) {
                        //                        subSlNo = subSlNo + 1;
//                        objMDTO.setMd_sl_no(new Double((long)subSlNo));
                        objMDTO.setTempSlNo(new Double(tempMaxSlNo));
                        System.out.println("########update Insert New Records objMDTO :" + objMDTO);
                        sqlMap.executeUpdate("insertMisecllaniousAllowanceTO", objMDTO);
                    }
                }
            }
            if (deleteGratuityList != null && deleteGratuityList.size() > 0) {
                int size = deleteGratuityList.size();
                for (int i = 0; i < size; i++) {
                    objGATO = (GratuityTO) deleteGratuityList.get(i);
                    if (CommonUtil.convertObjToStr(objGATO.getStatus()).equals(CommonConstants.STATUS_DELETED)) {
                        objGATO.setStatus(CommonConstants.STATUS_DELETED);
                        System.out.println("########delete Records objGATO :" + objGATO);
                        sqlMap.executeUpdate("deleteGratuityTO", objGATO);
                    }
                }
                deleteGratuityList = null;
            }
            if (GratuityTOs != null && GratuityTOs.size() > 0) {
                double tempMaxSlNo = 0;
                double subSlNo = 0;
                double tempSlno = 0.0;
                int size = GratuityTOs.size();
                System.out.println("######## objGATO :" + size + "GratuityTOs :" + GratuityTOs);
                for (int i = 0; i < size; i++) {
                    objGATO = (GratuityTO) GratuityTOs.get(i);
                    objGATO.setStatusDate(currDt);
                    objGATO.setGratuity_to_date(null);
                    if (i == 0) {
                        tempSlno = CommonUtil.convertObjToDouble(objGATO.getTempSlNo()).doubleValue();
                        String grade = CommonUtil.convertObjToStr(objGATO.getGratuity_grade());
                        List slList = sqlMap.executeQueryForList("getSelectMaxofSlNoSS", grade);
                        if (slList != null && slList.size() > 0) {
                            HashMap listMap = new HashMap();
                            listMap = (HashMap) slList.get(0);
                            tempMaxSlNo = CommonUtil.convertObjToDouble(listMap.get("TEMP_SL_NO")).doubleValue();
                            subSlNo = CommonUtil.convertObjToDouble(listMap.get("SL_NO")).doubleValue();
                        }
                    }
//                    if(i == 0){
//                        List slList = sqlMap.executeQueryForList("getSelectMaxofSlNoGA",null);
//                        if(slList!=null && slList.size()>0){
//                            HashMap listMap = new HashMap() ;
//                            listMap = (HashMap)slList.get(0);
//                            tempMaxSlNo = CommonUtil.convertObjToLong(listMap.get("TEMP_SL_NO"));
//                            subSlNo = CommonUtil.convertObjToLong(listMap.get("SL_NO"));
//                        }
//                    }else{
//                        if(!CommonUtil.convertObjToStr(objHATO.getStatus()).equals(CommonConstants.STATUS_DELETED)){
//                            subSlNo = subSlNo + 1;
//                        }
//                    }
                    if (CommonUtil.convertObjToStr(objGATO.getStatus()).equals(CommonConstants.STATUS_DELETED)) {
                        objGATO.setStatus(CommonConstants.STATUS_DELETED);
//                        objGATO.setGratuity_sl_no(new Double((long)subSlNo));
                        objGATO.setTempSlNo(new Double(tempMaxSlNo));
                        System.out.println("########delete Records objGATO :" + objGATO);
                        sqlMap.executeUpdate("deleteGratuityTO", objGATO);
                    } else if (CommonUtil.convertObjToStr(objGATO.getStatus()).equals(CommonConstants.STATUS_MODIFIED)) {
                        objGATO.setStatus(CommonConstants.STATUS_MODIFIED);
//                        objGATO.setGratuity_sl_no(new Double((long)subSlNo));
                        objGATO.setTempSlNo(new Double(tempMaxSlNo));
                        System.out.println("########update objGATO :" + objGATO);
                        sqlMap.executeUpdate("updateGratuityTO", objGATO);
                    } else if (CommonUtil.convertObjToStr(objGATO.getStatus()).equals(CommonConstants.STATUS_CREATED)) {
                        //                        subSlNo = subSlNo + 1;
//                        objGATO.setGratuity_sl_no(new Double((long)subSlNo));
                        objGATO.setTempSlNo(new Double(tempMaxSlNo));
                        System.out.println("########update Insert New Records objGATO :" + objGATO);
                        sqlMap.executeUpdate("insertGratuityTO", objGATO);
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
            //            int size = SalaryStructureTOs.size();
            //            System.out.println("########updatesize :"+size);
            //            sqlMap.startTransaction();
            ////            ArrayList lst = new ArrayList();
            //            for(int i=0;i<size;i++){
            //                objTO =(SalaryStructureTO)SalaryStructureTOs.get(0);
            //                objTO.setSlNo(String.valueOf(i+1));
            //                objTO.setToDate(null);
            //                sqlMap.executeUpdate("updateDeleteLienTO",objTO);
            //            }
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
        HaltingTOs = (ArrayList) map.get("HaltingTOs");
        MisecllaniousDeductionTOs = (ArrayList) map.get("MisecllaniousDeductionTOs");
        GratuityTOs = (ArrayList) map.get("GratuityTOs");
        deleteHaltingList = (ArrayList) map.get("deleteHaltingList");
        deleteMisecllaniousDeductionList = (ArrayList) map.get("deleteMisecllaniousDeductionList");
        System.out.println("@#$@#$deleteMisecllaniousDeductionList:" + deleteMisecllaniousDeductionList);
        deleteGratuityList = (ArrayList) map.get("deleteGratuityList");
        final String command = (String) map.get("COMMAND");
//        if(HaltingTOs!=null && HaltingTOs.size()>0 ){
        if (command.equals(CommonConstants.TOSTATUS_INSERT)) {
            insertData();
        } else if (command.equals(CommonConstants.TOSTATUS_UPDATE)) {
            updateData(map);
            //            } else if (command.equals(CommonConstants.TOSTATUS_DELETE)){
            //                updateDeleteStatusData(map);
        } else {
            throw new NoCommandException();
        }
//        }

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
        objHATO = null;
        objMDTO = null;
        objGATO = null;
        HaltingTOs = null;
        MisecllaniousDeductionTOs = null;
        GratuityTOs = null;
        HRAllowanceTOs = null;
        TAllowanceTOs = null;
        MAllowanceTOs = null;
        OAllowanceTOs = null;
    }
}
