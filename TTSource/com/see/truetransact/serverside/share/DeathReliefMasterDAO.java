/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition.
 * 
 *
 * SalaryStructureDAO.java
 *
 * Created on Wed May 26 10:59:57 GMT+05:30 2004
 */
package com.see.truetransact.serverside.share;

import com.ibatis.db.sqlmap.SqlMap;
import com.see.truetransact.serverutil.ServerUtil;
import com.see.truetransact.commonutil.CommonConstants;
import com.see.truetransact.commonutil.NoCommandException;
import com.see.truetransact.serverexception.TransRollbackException;
import com.see.truetransact.serverside.TTDAO;
import com.see.truetransact.serverside.common.log.LogDAO;
import com.see.truetransact.transferobject.common.log.LogTO;
import com.see.truetransact.serverexception.ServiceLocatorException;
import com.see.truetransact.servicelocator.ServiceLocator;
import com.see.truetransact.transferobject.share.DeathReliefMasterTO;
import com.see.truetransact.serverside.common.idgenerate.IDGenerateDAO;
import com.see.truetransact.commonutil.CommonUtil;
import com.see.truetransact.commonutil.DateUtil;
import java.sql.*;
import oracle.sql.*;
import oracle.jdbc.driver.*;
import com.see.truetransact.commonutil.Dummy;
import com.see.truetransact.clientutil.ClientConstants;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Iterator;
import java.util.Date;

/**
 * DeathReliefMaster DAO.
 *
 * @author
 *
 */
public class DeathReliefMasterDAO extends TTDAO {

    private SqlMap sqlMap;
    private HashMap data;
    private Iterator addressIterator;
    private DeathReliefMasterTO objDrfMasterTO;
    private DeathReliefMasterTO objDeathReliefMasterTO;
    //    private SalaryDeductionTO objSLTO;
    private String _userId = "";
    private LinkedHashMap drfMasterMap;
    private LinkedHashMap deletedDrfMasterMap;
    private String key;
    private LogDAO logDAO;
    private LogTO logTO;
    private String addressKey = new String();
    HashMap resultMap = new HashMap();
    Date currDt = null;
    private String whereCondition;
    private HashMap whereConditions;
    private Connection conn;
    private Statement stmt;
    private ResultSet rset;
    private String cmd;
    private String dataBaseURL;
    private String userName;
    private String passWord;
    private String SERVER_ADDRESS;
    private String tableName;
    private String tableCondition;
    private int isMore = -1;
    private String addCondition;
    final String SCREEN = "CUS";
    private final String YES = "Y";
    private List list;
    private List ll = new ArrayList();
    Date stsdate = null;

    /**
     * Creates a new instance of DeductionDAO
     */
    public DeathReliefMasterDAO() throws ServiceLocatorException {
        ServiceLocator locate = ServiceLocator.getInstance();
        sqlMap = (SqlMap) locate.getDAOSqlMap();
    }

    private HashMap getData(HashMap map) throws Exception {
        return null;
    }

    private String getDrfMasterID() throws Exception {
//        final IDGenerateDAO dao = new IDGenerateDAO();
//        final HashMap where = new HashMap();
//        where.put("WHERE", "DRFPRODUCT");
//        return (String) (dao.executeQuery(where)).get(CommonConstants.DATA);
        return null;
    }

    private String getGeneralBoadyID() throws Exception {
        //  final IDGenerateDAO dao = new IDGenerateDAO();
        final HashMap where = new HashMap();
        ///////  where.put(CommonConstants.MAP_WHERE, "GBID");
        where.put(CommonConstants.MAP_WHERE, "DRFI_ID");
        // return "";
        HashMap map = generateID();
        //System.out.println("MAP IN DAOOOOOO=========="+map);
        return (String) map.get(CommonConstants.DATA);
        //  return (String) (dao.executeQuery(where)).get(CommonConstants.DATA);
    }

    public HashMap generateID() {

        HashMap hash = null, result = null;
        try {
            String mapName = "getCurrentID";
            HashMap where = new HashMap();
            where.put("ID_KEY", "DRFI_ID"); //Here u have to pass BORROW_ID or something else
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

    private void setList(List ll) {
        this.ll = ll;
    }

    private List getList() {
        return ll;
    }

    private void insertData() throws Exception {
        try {

            //DRFMASTERTO
            getAllTOs();
            logTO.setData(objDrfMasterTO.toString());
            logTO.setPrimaryKey(objDrfMasterTO.getKeyData());
            logTO.setStatus(objDrfMasterTO.getCommand());
            sqlMap.executeUpdate("insertDeathReliefMaster", objDrfMasterTO);
            // objDrfMasterTO.get
            objDrfMasterTO.setDrfInterestID(getGeneralBoadyID());
            System.out.println("...." + objDrfMasterTO.getStatusDate());
            sqlMap.executeUpdate("insertDrfInterestDetails", objDrfMasterTO);
            processDrfMasterData(objDrfMasterTO.getCommand());
            System.out.println("DDD");
            String prdid = objDrfMasterTO.getProdId();
            String interestid = objDrfMasterTO.getDrfInterestID();
            String sts = objDrfMasterTO.getStatus();
            String stsBy = objDrfMasterTO.getStatusBy();
            stsdate = objDrfMasterTO.getStatusDate();
            String auth = objDrfMasterTO.getAuthorizeBy();
            String authSts = objDrfMasterTO.getAuthorizeStatus();
            Date authDate = objDrfMasterTO.getAuthorizeDate();

            for (int h = 0; h < ll.size(); h++) {
                HashMap hm = new HashMap();
                hm = (HashMap) ll.get(h);
                DeathReliefMasterTO dTO = new DeathReliefMasterTO();
                dTO.setProdId(prdid);
                dTO.setDrfInterestID(interestid);
                dTO.setTdtFromDt(DateUtil.getDateMMDDYYYY(hm.get("FROM").toString()));
                System.out.println("i::" + h + "FROMMMM:::" + DateUtil.getDateMMDDYYYY(hm.get("FROM").toString()) + "   " + dTO.getTdtFromDt());

                if (hm.get("TO").toString().equals("-")) {
                    dTO.setToDt(null);
                } else {

                    dTO.setToDt(DateUtil.getDateMMDDYYYY(hm.get("TO").toString()));
                }
                System.out.println("i::" + h + "TOOO:::" + DateUtil.getDateMMDDYYYY(hm.get("TO").toString()) + "   " + dTO.getToDt());
                dTO.setInterestRate(hm.get("INTEREST").toString());
                System.out.println("i::" + h + "INNTERESTTT:::" + hm.get("INTEREST").toString() + "   " + dTO.getInterestRate());
                dTO.setStatus(sts);
                dTO.setStatusBy(stsBy);
                dTO.setStatusDate(stsdate);
                dTO.setAuthorizeBy(auth);
                dTO.setAuthorizeDate(authDate);
                dTO.setAuthorizeStatus(authSts);
                sqlMap.executeUpdate("insertDrfRates", dTO);
                System.out.println("FROMMMM:::");
                //System.out.println("TOOOO::::"+hm.get("TO"));



            }

            // DeathReliefMasterOB drf=new DeathReliefMasterOB();
            //List lll=drf.getBuffer();
//            ArrayList buffer=new ArrayList();
//            buffer=objDrfMasterTO.getBufferTO();
//            for(int i=0;i<buffer.size();i++)
//            {
//             DeathReliefMasterTO drMaster=new DeathReliefMasterTO();
//            drMaster=(DeathReliefMasterTO)buffer.get(i);
//            System.out.println("INN DDDAAAOOOO   >>>"+drMaster.getTdtFromDt());
//            System.out.println("INN DDDAAAOOOO   >>>"+drMaster.getToDt());
//            System.out.println("INN DDDAAAOOOO   >>>"+drMaster.getInterestRate());
//            }





            //        logDAO.addToLog(logTO);

            logDAO.addToLog(logTO);
            makeDataNull();
            makeNull();
        } catch (Exception e) {
            sqlMap.rollbackTransaction();
            e.printStackTrace();
            throw e;
        }
    }

    private void updateData() throws Exception {

        getAllTOs();
        logTO.setData(objDrfMasterTO.toString());
        logTO.setPrimaryKey(objDrfMasterTO.getKeyData());
        logTO.setStatus(objDrfMasterTO.getCommand());
        sqlMap.executeUpdate("updateDeathReliefMaster", objDrfMasterTO);
        // objDrfMasterTO.setDrfInterestID(getGeneralBoadyID());
        System.out.println(">>>>>>>> " + objDrfMasterTO.getCalculationFrequency());
        sqlMap.executeUpdate("updateDrfInterestDetails", objDrfMasterTO);
        String prdid = objDrfMasterTO.getProdId();
        String interestid = objDrfMasterTO.getDrfInterestID();
        sqlMap.executeUpdate("deleteTab", objDrfMasterTO);



        System.out.println("DDD");

        prdid = objDrfMasterTO.getProdId();
        interestid = objDrfMasterTO.getDrfInterestID();
        String sts = "MODIFIED";
        String stsBy = objDrfMasterTO.getStatusBy();
        Date stsdate = objDrfMasterTO.getStatusDate();
        String auth = objDrfMasterTO.getAuthorizeBy();
        String authSts = objDrfMasterTO.getAuthorizeStatus();
        Date authDate = objDrfMasterTO.getAuthorizeDate();

        for (int h = 0; h < ll.size(); h++) {
            HashMap hm = new HashMap();
            hm = (HashMap) ll.get(h);
            DeathReliefMasterTO dTO = new DeathReliefMasterTO();
            dTO.setProdId(prdid);
            dTO.setDrfInterestID(interestid);
            dTO.setTdtFromDt(DateUtil.getDateMMDDYYYY(hm.get("FROM").toString()));
            System.out.println("i::" + h + "FROMMMM:::" + DateUtil.getDateMMDDYYYY(hm.get("FROM").toString()) + "   " + dTO.getTdtFromDt());
            if (hm.get("TO").toString().equals("-")) {
                dTO.setToDt(null);
            } else {

                dTO.setToDt(DateUtil.getDateMMDDYYYY(hm.get("TO").toString()));
            }
            System.out.println("i::" + h + "TOOO:::" + DateUtil.getDateMMDDYYYY(hm.get("TO").toString()) + "   " + dTO.getToDt());
            dTO.setInterestRate(hm.get("INTEREST").toString());
            System.out.println("i::" + h + "INNTERESTTT:::" + hm.get("INTEREST").toString() + "   " + dTO.getInterestRate());
            dTO.setStatus(sts);
            dTO.setStatusBy(stsBy);
            dTO.setStatusDate(stsdate);
            dTO.setAuthorizeBy(auth);
            dTO.setAuthorizeDate(authDate);
            dTO.setAuthorizeStatus(authSts);
            sqlMap.executeUpdate("insertDrfRates", dTO);
            System.out.println("FROMMMM:::");
        }











        processDrfMasterData(objDrfMasterTO.getCommand());
        final String USERID = logTO.getBranchId();
        logDAO.addToLog(logTO);
        makeDataNull();
        makeNull();

    }

    private void deleteData() throws Exception {
        //        try {
        getAllTOs();
        System.out.println("!@#!@#objDrfMasterTO" + objDrfMasterTO);
        sqlMap.executeUpdate("deleteDeathReliefMaster", objDrfMasterTO);
//        sqlMap.executeUpdate("deleteDeathReliefDetails", objDrfMasterTO);


        String prdid = objDrfMasterTO.getProdId();
        String interestid = objDrfMasterTO.getDrfInterestID();
        String sts = "DELETED";
        String stsBy = objDrfMasterTO.getStatusBy();
        Date stsdate = objDrfMasterTO.getStatusDate();
        String auth = objDrfMasterTO.getAuthorizeBy();
        String authSts = objDrfMasterTO.getAuthorizeStatus();
        Date authDate = objDrfMasterTO.getAuthorizeDate();

        for (int h = 0; h < ll.size(); h++) {
            HashMap hm = new HashMap();
            hm = (HashMap) ll.get(h);
            DeathReliefMasterTO dTO = new DeathReliefMasterTO();
            dTO.setProdId(prdid);
            dTO.setDrfInterestID(interestid);
            dTO.setTdtFromDt(DateUtil.getDateMMDDYYYY(hm.get("FROM").toString()));
            System.out.println("i::" + h + "FROMMMM:::" + DateUtil.getDateMMDDYYYY(hm.get("FROM").toString()) + "   " + dTO.getTdtFromDt());
            if (hm.get("TO").toString().equals("-")) {
                dTO.setToDt(null);
            } else {
                dTO.setToDt(DateUtil.getDateMMDDYYYY(hm.get("TO").toString()));
            }
            System.out.println("i::" + h + "TOOO:::" + DateUtil.getDateMMDDYYYY(hm.get("TO").toString()) + "   " + dTO.getToDt());
            dTO.setInterestRate(hm.get("INTEREST").toString());
            System.out.println("i::" + h + "INNTERESTTT:::" + hm.get("INTEREST").toString() + "   " + dTO.getInterestRate());
            dTO.setStatus(sts);
            dTO.setStatusBy(stsBy);
            dTO.setStatusDate(stsdate);
            dTO.setAuthorizeBy(auth);
            dTO.setAuthorizeDate(authDate);
            dTO.setAuthorizeStatus(authSts);
            sqlMap.executeUpdate("deleteTab2", dTO);
            System.out.println("FROMMMM:::");
        }





        //ADDED 01-12-2010
        processDrfMasterData(objDrfMasterTO.getCommand());
        logDAO.addToLog(logTO);
        makeDataNull();
        makeNull();
    }

    private void makeDataNull() {
        data = null;
    }

    private void makeNull() {
        drfMasterMap = null;
        deletedDrfMasterMap = null;
        objDrfMasterTO = null;
        objDeathReliefMasterTO = null;
    }

    private void processDrfMasterData(String command) throws Exception {

        if (deletedDrfMasterMap != null) {
            addressIterator = deletedDrfMasterMap.keySet().iterator();
            for (int i = deletedDrfMasterMap.size(); i > 0; i--) {
                key = (String) addressIterator.next();
                System.out.println("entering deleted drf map!!" + deletedDrfMasterMap);
                objDeathReliefMasterTO = (DeathReliefMasterTO) deletedDrfMasterMap.get(key);
                logTO.setData(objDeathReliefMasterTO.toString());
                logTO.setPrimaryKey(objDeathReliefMasterTO.getKeyData());
                objDeathReliefMasterTO.setStatus(CommonConstants.STATUS_DELETED);
                logTO.setStatus(CommonConstants.STATUS_DELETED);
                sqlMap.executeUpdate("deleteDeathReliefDetails", objDeathReliefMasterTO);
                logDAO.addToLog(logTO);
            }
            deletedDrfMasterMap = null;
        }
        if (drfMasterMap != null) {
            addressIterator = drfMasterMap.keySet().iterator();

            for (int i = drfMasterMap.size(); i > 0; i--) {
                addressKey = (String) addressIterator.next();
                objDeathReliefMasterTO = (DeathReliefMasterTO) drfMasterMap.get(addressKey);
                logTO.setData(objDeathReliefMasterTO.toString());
                logTO.setPrimaryKey(objDeathReliefMasterTO.getKeyData());
                logTO.setStatus(objDeathReliefMasterTO.getCommand());
                //if customer Id exists, set to customerAddressTO objectobj
                System.out.println("#$%objDeathReliefMasterTO:" + objDeathReliefMasterTO);
                if (command.equals(CommonConstants.TOSTATUS_UPDATE)) {
                    if (objDeathReliefMasterTO.getStatus().equals(CommonConstants.STATUS_CREATED)) {
                        sqlMap.executeUpdate("insertDeathReliefMasterDetails", objDeathReliefMasterTO);
                    } else {
                        objDeathReliefMasterTO.setStatus(CommonConstants.STATUS_MODIFIED);
                        sqlMap.executeUpdate("updateDrfMasterDetails", objDeathReliefMasterTO);
                    }

                } else if (command.equals(CommonConstants.TOSTATUS_INSERT)) {

                    sqlMap.executeUpdate("insertDeathReliefMasterDetails", objDeathReliefMasterTO);
                } else if (command.equals(CommonConstants.TOSTATUS_DELETE)) {
                    objDeathReliefMasterTO.setStatus(CommonConstants.STATUS_DELETED);
                    sqlMap.executeUpdate("deleteDeathReliefDetails", objDeathReliefMasterTO);

                }

                logDAO.addToLog(logTO);
            }
            drfMasterMap = null;
        }

    }

    private void getAllTOs() throws Exception {
        objDrfMasterTO = (DeathReliefMasterTO) data.get("DRFMASTERTO");
        ll = (List) data.get("MYLIST");
        if (data.containsKey("DELETEDDRFMASTER")) {
            deletedDrfMasterMap = (LinkedHashMap) data.get("DELETEDDRFMASTER");
        }
        if (data.containsKey("DRFMASTER")) {
            drfMasterMap = (LinkedHashMap) data.get("DRFMASTER");
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

    public static void main(String str[]) {
        try {
            DeathReliefMasterDAO dao = new DeathReliefMasterDAO();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public HashMap executeQuery(HashMap condition) throws Exception {
        System.out.println("###condition" + condition);
        _branchCode = (String) condition.get(CommonConstants.BRANCH_ID);
        currDt = ServerUtil.getCurrentDate(_branchCode);
        whereConditions = condition;
        if (condition.containsKey(CommonConstants.MAP_WHERE)) {
            whereCondition = (String) condition.get(CommonConstants.MAP_WHERE);
        }
        if (!condition.containsKey("AUTH_DATA")) {
            System.out.println("###whereconditions" + whereConditions);
            getDrfMasterData();
            getDrfTableData();
            getDrfInterestDts();
            getDrfInterestRate();
            makeNull();
            makeQueryNull();
        } else if (condition.containsKey("AUTH_DATA")) {
            System.out.println("@#$@#$@#$condition:" + condition);
            sqlMap.executeUpdate("authorizeDrfMaster", condition);
            sqlMap.executeUpdate("authorizeDrfDetails", condition);
            sqlMap.executeUpdate("authorizeDrfInterest", condition);
            sqlMap.executeUpdate("authorizeDrfInterestDetails", condition);

        }
        return data;

    }

    private void getDrfTableData() throws Exception {
        if (data == null) {
            data = new HashMap();
        }

        list = (List) sqlMap.executeQueryForList("getSelectDrfProuctTO", whereConditions);
        if (list.size() > 0) {
            drfMasterMap = new LinkedHashMap();
            for (int i = list.size(), j = 0; i > 0; i--, j++) {
                drfMasterMap.put(((DeathReliefMasterTO) list.get(j)).getDrfSlNo(), list.get(j));
            }
            data.put("DRFPRODUCT", drfMasterMap);
        }
    }

    private void getDrfInterestRate() throws Exception {
        if (data == null) {
            data = new HashMap();
        }
        List lol = new ArrayList();
        System.out.println("drfratee111");
        list = (List) sqlMap.executeQueryForList("getDrfInterestRates", whereConditions);
        if (list.size() > 0) {
            //  drfMasterMap = new LinkedHashMap();
            for (int i = 0; i < list.size(); i++) {
                HashMap m = new HashMap();
                DeathReliefMasterTO dTO = (DeathReliefMasterTO) list.get(i);
                m.put("FROM", dTO.getTdtFromDt());
                if (dTO.getToDt() == null) {
                    m.put("TO", "-");
                } else {
                    m.put("TO", dTO.getToDt());
                }
                m.put("INTEREST", dTO.getInterestRate());
                lol.add(m);
            }
            //drfMasterMap.put( ((DeathReliefMasterTO)list.get(j)).getDrfSlNo(),list.get(j));
            //}
            data.put("DRFPRODUCT1", lol);
            System.out.println("drfratee111" + lol.size());
        }
    }

    private void getDrfMasterData() throws Exception {
        if (data == null) {
            data = new HashMap();
        }
        whereConditions.put("CURRENT_DT", currDt);
        System.out.println("#@$@#$whereConditions:" + whereConditions);
        list = (List) sqlMap.executeQueryForList("getSelectDrfMasterTO", whereConditions);
        System.out.println("@#$@#$@#4list :" + list);
        objDrfMasterTO = new DeathReliefMasterTO();
        if (list.size() > 0) {
            objDrfMasterTO = (DeathReliefMasterTO) list.get(0);
            data.put("DRFMASTERTO", objDrfMasterTO);
        }
    }

    private void getDrfInterestDts() throws Exception {
        if (data == null) {
            data = new HashMap();
        }
        whereConditions.put("CURRENT_DT", currDt);
        System.out.println("#@$@#$whereConditions:" + whereConditions);
        list = (List) sqlMap.executeQueryForList("getSelectDrfMasterTO1", whereConditions);
        System.out.println("@#$@#$@#4list :" + list);
        objDrfMasterTO = new DeathReliefMasterTO();
        if (list.size() > 0) {
            objDrfMasterTO = (DeathReliefMasterTO) list.get(0);
            data.put("DRFMASTERTO1", objDrfMasterTO);
        }


    }

    private void makeQueryNull() {
        whereCondition = null;
        list = null;
    }

    public HashMap execute(HashMap obj) throws Exception {
        _branchCode = (String) obj.get(CommonConstants.BRANCH_ID);
        currDt = ServerUtil.getCurrentDate(_branchCode);
        System.out.println("#### DAO execute obj map : " + obj);
        HashMap resultMap = new HashMap();
        try {
            DeathReliefMasterTO objDeathReliefMasterTO = (DeathReliefMasterTO) obj.get("DRFMASTERTO");
            System.out.println("@!#$@#$@#$objDeathReliefMasterTO:" + objDeathReliefMasterTO);
            logDAO = new LogDAO();
            logTO = new LogTO();
            logTO.setUserId(CommonUtil.convertObjToStr(obj.get(CommonConstants.USER_ID)));
            logTO.setBranchId(CommonUtil.convertObjToStr(obj.get(CommonConstants.SELECTED_BRANCH_ID)));
            logTO.setInitiatedBranch(CommonUtil.convertObjToStr(obj.get(CommonConstants.BRANCH_ID)));
            logTO.setIpAddr(CommonUtil.convertObjToStr(obj.get(CommonConstants.IP_ADDR)));
            logTO.setModule(CommonUtil.convertObjToStr(obj.get(CommonConstants.MODULE)));
            logTO.setScreen(CommonUtil.convertObjToStr(obj.get(CommonConstants.SCREEN)));
            data = obj;
            //Start the transaction
            sqlMap.startTransaction();
            System.out.println("objDeathReliefMasterTO.getCommand()--------------->" + objDeathReliefMasterTO.getCommand());
            cmd = objDeathReliefMasterTO.getCommand();
            System.out.println("cmd--------------->" + cmd);

            if (objDeathReliefMasterTO.getCommand().equals(CommonConstants.TOSTATUS_INSERT)) {
                insertData();
            } else if (objDeathReliefMasterTO.getCommand().equals(CommonConstants.TOSTATUS_UPDATE)) {
                updateData();
            } else if (objDeathReliefMasterTO.getCommand().equals(CommonConstants.TOSTATUS_DELETE)) {
                deleteData();
            } else {
                sqlMap.rollbackTransaction();
                throw new NoCommandException();
            }
            resultMap.put("PRODUCTID", objDeathReliefMasterTO.getProdId());
            //Commit the transaction
            sqlMap.commitTransaction();
//            if( cmd.equals(CommonConstants.TOSTATUS_INSERT) ){
//                
//                tableName = "EMPLOYEE_MASTER";
//                tableCondition = "SYS_EMPID";
//                isMore = 0;
//                storePhotoSign();
//                
//            }
        } catch (Exception ex) {
            sqlMap.rollbackTransaction();
            throw ex;
        }
        return resultMap;
    }

    private void authorize(HashMap map) throws Exception {
        ArrayList selectedList = (ArrayList) map.get(CommonConstants.AUTHORIZEDATA);
        HashMap AuthMap = new HashMap();
        AuthMap = (HashMap) selectedList.get(0);
        System.out.println("@@@@@@@AuthMap" + AuthMap);
        try {
        } catch (Exception e) {
            sqlMap.rollbackTransaction();
            e.printStackTrace();
            throw new TransRollbackException(e);
        }
    }

    private void destroyObjects() {
    }
}
