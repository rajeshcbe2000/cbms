    /*
    * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
    *
    * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition.
    * 
    *
    * NewTimeDepositDAO.java
    *
    * Created on Tue Jul 13 13:46:20 GMT+05:30 2004
    */
    package com.see.truetransact.serverside.privatebanking.actionitem.newtimedeposit;

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
    import com.see.truetransact.serverside.common.idgenerate.IDGenerateDAO;
    import com.see.truetransact.serverside.common.log.LogDAO;
    import com.see.truetransact.transferobject.common.log.LogTO;
    import com.see.truetransact.commonutil.CommonUtil;

    import com.see.truetransact.transferobject.privatebanking.actionitem.newtimedeposit.NewTimeDepositTO;

    /**
    * @author Ashok
    *
    */
    public class NewTimeDepositDAO extends TTDAO {

        private static SqlMap sqlMap = null;
        private NewTimeDepositTO objTO;
        private LogDAO logDAO;
        private LogTO logTO;

        /**
        * Creates a new instance of NewTimeDepositDAO
        */
        public NewTimeDepositDAO() throws ServiceLocatorException {
            ServiceLocator locate = ServiceLocator.getInstance();
            sqlMap = (SqlMap) locate.getDAOSqlMap();
        }

        private HashMap getData(HashMap map) throws Exception {
            HashMap returnMap = new HashMap();
            String where = (String) map.get(CommonConstants.MAP_WHERE);
            List list = (List) sqlMap.executeQueryForList("getSelectNewTimeDepositTO", where);
            returnMap.put("NewTimeDepositTO", list);
            list = null;
            where = null;
            return returnMap;
        }

        /**
        * Returns an Autogenearated Id using IDGeneratorDAO
        */
        private String getTimeDepositId() throws Exception {
            final IDGenerateDAO dao = new IDGenerateDAO();
            final HashMap where = new HashMap();
            where.put(CommonConstants.MAP_WHERE, "PVT_TIME_DEPOSIT_ID");
            return (String) (dao.executeQuery(where)).get(CommonConstants.DATA);
        }

        private void insertData() throws Exception {
            try {
                sqlMap.startTransaction();
                String depositId = getTimeDepositId();
                objTO.setRefId(depositId);
                depositId = null;
                logTO.setData(objTO.toString());
                logTO.setPrimaryKey(objTO.getKeyData());
                logTO.setStatus(objTO.getCommand());
                sqlMap.executeUpdate("insertNewTimeDepositTO", objTO);
                logDAO.addToLog(logTO);
                sqlMap.commitTransaction();
            } catch (Exception e) {
                sqlMap.rollbackTransaction();
                throw e;
            }
        }

        private void updateData() throws Exception {
            try {
                sqlMap.startTransaction();
                logTO.setData(objTO.toString());
                logTO.setPrimaryKey(objTO.getKeyData());
                logTO.setStatus(objTO.getCommand());
                sqlMap.executeUpdate("updateNewTimeDepositTO", objTO);
                logDAO.addToLog(logTO);
                sqlMap.commitTransaction();
            } catch (Exception e) {
                sqlMap.rollbackTransaction();
                throw e;
            }
        }

        private void deleteData() throws Exception {
            try {
                sqlMap.startTransaction();
                logTO.setData(objTO.toString());
                logTO.setPrimaryKey(objTO.getKeyData());
                logTO.setStatus(objTO.getCommand());
                sqlMap.executeUpdate("deleteNewTimeDepositTO", objTO);
                logDAO.addToLog(logTO);
                sqlMap.commitTransaction();
            } catch (Exception e) {
                sqlMap.rollbackTransaction();
                throw e;
            }
        }

        public static void main(String str[]) {
            try {
                NewTimeDepositDAO dao = new NewTimeDepositDAO();
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }

        public HashMap execute(HashMap map) throws Exception {
            _branchCode = (String) map.get(CommonConstants.BRANCH_ID);
            objTO = (NewTimeDepositTO) map.get("NewTimeDepositTO");
            final String command = objTO.getCommand();

            logDAO = new LogDAO();
            logTO = new LogTO();

            logTO.setUserId(CommonUtil.convertObjToStr(map.get(CommonConstants.USER_ID)));
            logTO.setBranchId(CommonUtil.convertObjToStr(map.get(CommonConstants.BRANCH_ID)));
            logTO.setIpAddr(CommonUtil.convertObjToStr(map.get(CommonConstants.IP_ADDR)));
            logTO.setModule(CommonUtil.convertObjToStr(map.get(CommonConstants.MODULE)));
            logTO.setScreen(CommonUtil.convertObjToStr(map.get(CommonConstants.SCREEN)));

            if (command.equals(CommonConstants.TOSTATUS_INSERT)) {
                insertData();
            } else if (command.equals(CommonConstants.TOSTATUS_UPDATE)) {
                updateData();
            } else if (command.equals(CommonConstants.TOSTATUS_DELETE)) {
                deleteData();
            } else {
                throw new NoCommandException();
            }

            destroyObjects();
            return null;
        }

        public HashMap executeQuery(HashMap obj) throws Exception {
            _branchCode = (String) obj.get(CommonConstants.BRANCH_ID);
            return getData(obj);
        }

        private void destroyObjects() {
            objTO = null;
        }
    }
