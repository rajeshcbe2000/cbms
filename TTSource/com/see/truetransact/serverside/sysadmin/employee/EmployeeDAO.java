/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition.
 * 
 *
 * EmployeeDAO.java
 *
 * Created on Tue Feb 17 12:06:18 IST 2004
 */
package com.see.truetransact.serverside.sysadmin.employee;

import java.util.List;
import java.util.ArrayList;

import java.util.HashMap;

import com.ibatis.db.sqlmap.SqlMap;
import com.see.truetransact.serverutil.ServerUtil;

import com.see.truetransact.commonutil.CommonConstants;
import com.see.truetransact.commonutil.NoCommandException;
import com.see.truetransact.serverside.TTDAO;
import com.see.truetransact.serverutil.ServerConstants;
import com.see.truetransact.servicelocator.ServiceLocator;
import com.see.truetransact.serverexception.TransRollbackException;
import com.see.truetransact.serverexception.ServiceLocatorException;

import com.see.truetransact.transferobject.TOHeader;
import com.see.truetransact.commonutil.DateUtil;
import com.see.truetransact.serverside.common.idgenerate.IDGenerateDAO;
import com.see.truetransact.clientutil.ClientConstants;

import com.see.truetransact.transferobject.sysadmin.employee.EmployeeAddrTO;
import com.see.truetransact.transferobject.sysadmin.employee.EmployeeDetailsTO;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.Date;
/**
 * Employee DAO.
 *
 */
public class EmployeeDAO extends TTDAO {

    private static SqlMap sqlMap = null;
    private EmployeeAddrTO objEmployeeAddrTO;
    private EmployeeDetailsTO objEmployeeDetailsTO;
    private byte[] photoByteArray;
    final String EMPLOYEE_PHOTO = "employee\\";
    private Date currDt = null;
    /**
     * Creates a new instance of EmployeeDAO
     */
    public EmployeeDAO() throws ServiceLocatorException {
        ServiceLocator locate = ServiceLocator.getInstance();
        sqlMap = (SqlMap) locate.getDAOSqlMap();
    }

    private HashMap getData(HashMap map) throws Exception {
        HashMap returnMap = new HashMap();
        String where = (String) map.get(CommonConstants.MAP_WHERE);
        List list = (List) sqlMap.executeQueryForList("getSelectEmployeeDetailsTO", where);
        returnMap.put("EmployeeDetailsTO", list);
        list = (List) sqlMap.executeQueryForList("getSelectEmployeeAddrTO", where);
        returnMap.put("EmployeeAddrTO", list);
        return returnMap;
    }

    private void insertData() throws Exception {
        String employeeId = getEmployeeID();
        objEmployeeDetailsTO.setEmployeeCode(employeeId);
        objEmployeeDetailsTO.setStatus(CommonConstants.STATUS_CREATED);
        objEmployeeAddrTO.setEmployeeId(employeeId);
        if (objEmployeeDetailsTO.getPhotoFile() != null) {
            objEmployeeDetailsTO.setPhotoFile(employeeId + (String) objEmployeeDetailsTO.getPhotoFile());
            //Method to Store the Picture...
            storePicture();
        }
        sqlMap.executeUpdate("insertEmployeeDetailsTO", objEmployeeDetailsTO);
        sqlMap.executeUpdate("insertEmployeeAddrTO", objEmployeeAddrTO);
    }

    private String getEmployeeID() throws Exception {
        final IDGenerateDAO dao = new IDGenerateDAO();
        final HashMap where = new HashMap();
        where.put("WHERE", "EMPLOYEE_ID");
        return (String) (dao.executeQuery(where)).get(CommonConstants.DATA);
    }

    private void storePicture() throws Exception {
        //StringBuffer targetFileName = new StringBuffer("E:\\jboss-3.2.1_tomcat-4.1.24\\server\\default\\deploy\\truetransact.war\\employee\\");
        StringBuffer targetFileName = new StringBuffer(ServerConstants.SERVER_PATH).append(EMPLOYEE_PHOTO);
        targetFileName.append(objEmployeeDetailsTO.getPhotoFile());
        FileOutputStream writer = new FileOutputStream(targetFileName.toString());
        writer.write(photoByteArray);
        writer.flush();
        writer.close();
    }

    private void updateData() throws Exception {
        objEmployeeDetailsTO.setStatus(CommonConstants.STATUS_MODIFIED);
        if (objEmployeeDetailsTO.getPhotoFile() != null && photoByteArray != null) {
            objEmployeeDetailsTO.setPhotoFile(objEmployeeDetailsTO.getEmployeeCode() + (String) objEmployeeDetailsTO.getPhotoFile());
            //Method to Store the Picture...
            storePicture();
        }

        sqlMap.executeUpdate("updateEmployeeDetailsTO", objEmployeeDetailsTO);
        sqlMap.executeUpdate("updateEmployeeAddrTO", objEmployeeAddrTO);
    }

    private void deleteData() throws Exception {
        objEmployeeDetailsTO.setStatus(CommonConstants.STATUS_DELETED);
        sqlMap.startTransaction();
        sqlMap.executeUpdate("deleteEmployeeDetailsTO", objEmployeeDetailsTO);
    }

    public static void main(String str[]) {
        try {
            EmployeeDAO dao = new EmployeeDAO();
            EmployeeDetailsTO objEmployeeDetailsTO = new EmployeeDetailsTO();
            EmployeeAddrTO objEmployeeAddrTO = new EmployeeAddrTO();

            TOHeader toHeader = new TOHeader();//To tell what to do... Insert, Update, Delete...
            toHeader.setCommand(CommonConstants.TOSTATUS_UPDATE);
            objEmployeeDetailsTO.setTOHeader(toHeader);

            //Bank_Employee...
            objEmployeeDetailsTO.setBranchCode("B001");
            objEmployeeDetailsTO.setEmployeeCode("E0001011");
            objEmployeeDetailsTO.setTitle("MRS.");
            objEmployeeDetailsTO.setLname("KAPOOR");
            objEmployeeDetailsTO.setFname("KAREENA");
            objEmployeeDetailsTO.setDesigId("J01");
//            objEmployeeDetailsTO.setDob(currDt);
            objEmployeeDetailsTO.setMaritalStatus("SINGLE");
            objEmployeeDetailsTO.setGender("M");
//            objEmployeeDetailsTO.setDoj(currDt);
//            objEmployeeDetailsTO.setDol(currDt);
//            objEmployeeDetailsTO.setDow(currDt);
            objEmployeeDetailsTO.setDepttId("L01");
            objEmployeeDetailsTO.setManagerCode("MAN1");
            objEmployeeDetailsTO.setOfficialEmail("rahu@fincuro.com");
            objEmployeeDetailsTO.setAlternateEmail("rahul123@fincuro.com");
            objEmployeeDetailsTO.setOfficePhone("9886072662");
            objEmployeeDetailsTO.setHomePhone("91 191 2450036");
            objEmployeeDetailsTO.setCellular("9886072662");
            objEmployeeDetailsTO.setPanNo("AA111");
            objEmployeeDetailsTO.setSsn("SA111");
            objEmployeeDetailsTO.setPassportNo("A105");
            objEmployeeDetailsTO.setSkills("NO SKILLS");
            objEmployeeDetailsTO.setEducation("EDUCATED");
            objEmployeeDetailsTO.setExperience("NOTHING LIKE THAT");
            objEmployeeDetailsTO.setPhotoFile(".jpg");
            objEmployeeDetailsTO.setResponsibility("NONE");
            objEmployeeDetailsTO.setPerformance("UP TO THE MARK");
            objEmployeeDetailsTO.setComments("NO COMMENTS");
            objEmployeeDetailsTO.setCreatedBy("MAN");
            objEmployeeDetailsTO.setCreatedDt(DateUtil.getDateMMDDYYYY(null));
            objEmployeeDetailsTO.setStatus("CREATED");
            objEmployeeDetailsTO.setEmployeeType("TIME PASS");

            // EMPLOYEE_ADDR...
            objEmployeeAddrTO.setEmployeeId("E004");
            objEmployeeAddrTO.setStreet("MAX STREET");
            objEmployeeAddrTO.setArea("NORTH");
            objEmployeeAddrTO.setCity("SANTIAGO");
            objEmployeeAddrTO.setState("CALIF");
            objEmployeeAddrTO.setPinCode("11111");
            objEmployeeAddrTO.setCountryCode("USA");

            HashMap hash = new HashMap();
            hash.put("EmployeeDetailsTO", objEmployeeDetailsTO);
            hash.put("EmployeeAddrTO", objEmployeeAddrTO);

            System.out.println("Before execute");
            dao.execute(hash);

        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public HashMap execute(HashMap map) throws Exception {
        objEmployeeDetailsTO = (EmployeeDetailsTO) map.get("EmployeeDetailsTO");
        objEmployeeAddrTO = (EmployeeAddrTO) map.get("EmployeeAddrTO");

        photoByteArray = (byte[]) map.get("photo");

        final String command = objEmployeeDetailsTO.getCommand();
        _branchCode = (String) map.get(CommonConstants.BRANCH_ID);
        currDt = ServerUtil.getCurrentDate(_branchCode);
        try {
            sqlMap.startTransaction();

            if (command.equals(CommonConstants.TOSTATUS_INSERT)) {
                insertData();
            } else if (command.equals(CommonConstants.TOSTATUS_UPDATE)) {
                updateData();
            } else if (command.equals(CommonConstants.TOSTATUS_DELETE)) {
                deleteData();
            } else {
                throw new NoCommandException();
            }

            sqlMap.commitTransaction();
        } catch (Exception e) {
            sqlMap.rollbackTransaction();
            e.printStackTrace();
            throw new TransRollbackException(e);
        }
        destroyObjects();
        return null;
    }

    public HashMap executeQuery(HashMap obj) throws Exception {
        _branchCode = (String) obj.get(CommonConstants.BRANCH_ID);
        currDt = ServerUtil.getCurrentDate(_branchCode);
        return getData(obj);
    }

    private void destroyObjects() {
        objEmployeeDetailsTO = null;
        objEmployeeAddrTO = null;
    }
}
