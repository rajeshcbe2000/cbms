/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition.
 * 
 *
 * OperativeAcctProductDAO.java
 *
 * Created on June 18, 2003, 4:14 PM
 */
package com.see.truetransact.serverside.product.mds;

import java.util.List;
import java.util.HashMap;
import com.ibatis.db.sqlmap.SqlMap;
import com.see.truetransact.serverside.TTDAO;
import com.see.truetransact.commonutil.CommonUtil;
import com.see.truetransact.serverutil.ServerUtil;
import com.see.truetransact.commonutil.CommonConstants;
import com.see.truetransact.commonutil.NoCommandException;
import com.see.truetransact.servicelocator.ServiceLocator;
import com.see.truetransact.serverexception.TransRollbackException;
import com.see.truetransact.transferobject.product.mds.MDSProductTO;
import com.see.truetransact.serverexception.ServiceLocatorException;
import com.see.truetransact.transferobject.product.mds.MDSProductAcctHeadTO;
import com.see.truetransact.transferobject.product.mds.MDSProductOtherDetailsTO;
// For the Business Rules...
import com.see.truetransact.businessrule.RuleContext;
import com.see.truetransact.businessrule.RuleEngine;
// For Maintaining Logs...
import com.see.truetransact.serverside.common.log.LogDAO;
import com.see.truetransact.transferobject.common.log.LogTO;
import java.util.Date;
/**
 * This is used for OperativeAcctProductDAO Data Access.
 *
 * @author Sathiya
 *
 * @modified Pinky @modified Rahul
 */
public class MDSProductDAO extends TTDAO {

    private static SqlMap sqlMap = null;
//    private OperativeAcctProductTO operativeAcctProductTO;
//    private OperativeAcctParamTO operativeAcctParamTO;
//    private OperativeAcctIntRecvParamTO operativeAcctIntRecvParamTO;
//    private OperativeAcctIntPayParamTO operativeAcctIntPayParamTO;
//    private OperativeAcctChargesParamTO operativeAcctChargesParamTO;
//    private OperativeAcctSpclitemParamTO operativeAcctSpclitemParamTO;
//    private OperativeAcctIntRateParamTO operativeAcctIntRateParamTO;
//    private OperativeAcctHeadParamTO operativeAcctHeadParamTO;
//    private ArrayList operativeAcctIntRateTOs;
    private MDSProductOtherDetailsTO mdsProductOtherDetailsTO = null;
    private MDSProductTO mdsProductTO = null;
    RuleContext context;
    RuleEngine engine;
    private Date currDt = null;
    /**
     * Creates a new instance of OperativeAcctProductDAO
     */
    public MDSProductDAO() throws ServiceLocatorException {
        ServiceLocator locate = ServiceLocator.getInstance();
        sqlMap = (SqlMap) locate.getDAOSqlMap();
    }

    private HashMap getData(HashMap map) throws Exception {
        HashMap returnMap = new HashMap();
        String where = (String) map.get(CommonConstants.MAP_WHERE);
        List list = (List) sqlMap.executeQueryForList("getMDSProductTO", where);
        returnMap.put("getMDSProductTO", list);

        list = (List) sqlMap.executeQueryForList("getMDSProductOtherDetailsTO", where);
        returnMap.put("getMDSProductOtherDetailsTO", list);
        System.out.println("returnMap :" + returnMap);
        return returnMap;
    }

    private void insertData(HashMap map, LogDAO objLogDAO, LogTO objLogTO) throws Exception {
        try {
            getTOs(map);
//            sqlMap.startTransaction();
//            operativeAcctProductTO.setCreatedBy((String) map.get(CommonConstants.USER_ID));
//            operativeAcctProductTO.setStatus(CommonConstants.STATUS_CREATED);
//            operativeAcctProductTO.setSDate(currDt);
//            operativeAcctProductTO.setCreatedDt(currDt);

            sqlMap.executeUpdate("insertMDSProductTO", mdsProductTO);
//            objLogTO.setData(operativeAcctProductTO.toString());
//            objLogTO.setPrimaryKey(operativeAcctProductTO.getKeyData());
            objLogDAO.addToLog(objLogTO);



            sqlMap.executeUpdate("insertMDSProductOtherDetailsTO", mdsProductOtherDetailsTO);
//            objLogTO.setData(operativeAcctIntRecvParamTO.toString());
//            objLogTO.setPrimaryKey(operativeAcctIntRecvParamTO.getKeyData());
            objLogDAO.addToLog(objLogTO);

            //            insertOperativeIntRateTOs(objLogDAO, objLogTO);
//            sqlMap.commitTransaction();
        } catch (Exception e) {
            sqlMap.rollbackTransaction();
            e.printStackTrace();
            throw new TransRollbackException(e);
        }
    }

//    private void updateChargeMaintenance(OperativeAcctChargesParamTO operativeAcctChargesParamTO,String log)throws Exception{
//        HashMap map =new HashMap();
//        List allbranch=sqlMap.executeQueryForList("getAllBranches","");
//        if(allbranch!=null && allbranch.size()>0){
//            for(int i=0;i<allbranch.size();i++){
//                HashMap hash=(HashMap)allbranch.get(i);
////                List operativeproduct=sqlMap.executeQueryForList("getOperativeProduct","");
////                if(operativeproduct !=null && operativeproduct.size()>0){
////                    for(int j=0;j<operativeproduct.size();j++){
////                        HashMap operativemap=(HashMap)operativeproduct.get(j);
////                        System.out.println(log+"operativemap####"+operativemap);
////                        if(CommonUtil.convertObjToStr(operativemap.get("BEHAVES_LIKE")).equals("CA")){
////                            
//                            if(log.equals("CREATED")  ){
//                                
//                                map.put("APPLIED_DATE",currDt);
//                                map.put("PROD_TYPE", "OA");
//                                map.put("PROD_ID", operativeAcctChargesParamTO.getProdId());
//                                map.put("BRANCH_ID",hash.get("BRANCH_ID"));
//                                map.put("CHARGE_TYPE","FolioChargesTask");
//                                map.put("LAST_CHARG_CALC_DT",operativeAcctChargesParamTO.getLastFolioChargedt());
//                                map.put("NEXT_CHARG_CALC_DT",operativeAcctChargesParamTO.getNextFolioChargedt());
//                                System.out.println("insertallcharges###"+map);
//                                sqlMap.executeUpdate("insertallcharges", map);
//                                
//                            }   if( log.equals("UPDATE")){
//                                map.put("PROD_ID", operativeAcctChargesParamTO.getProdId());
//                                map.put("LAST_CHARG_CALC_DT",operativeAcctChargesParamTO.getLastFolioChargedt());
//                                map.put("NEXT_CHARG_CALC_DT",operativeAcctChargesParamTO.getNextFolioChargedt());
//                                map.put("BRANCH_ID",hash.get("BRANCH_ID"));
//                                map.put("CHARGE_TYPE","FolioChargesTask");
//                                System.out.println("updateFolioCharges###"+map);
//                                sqlMap.executeUpdate("updateFolioCharges", map);
//                            }
////                            
////                        }
////                    }
////                }
//            }
//        }
//    }
    private void updateData(HashMap map, LogDAO objLogDAO, LogTO objLogTO) throws Exception {
        try {
            getTOs(map);
            mdsProductTO.setStatusBy((String) map.get(CommonConstants.USER_ID));
            mdsProductTO.setStatus(CommonConstants.STATUS_MODIFIED);
            mdsProductTO.setStatusDt(currDt);

            sqlMap.executeUpdate("updateMDSProductTO", mdsProductTO);
//            objLogTO.setData(operativeAcctProductTO.toString());
//            objLogTO.setPrimaryKey(operativeAcctProductTO.getKeyData());
            objLogDAO.addToLog(objLogTO);


            sqlMap.executeUpdate("updateMDSProductOtherDetailsTO", mdsProductOtherDetailsTO);
//            objLogTO.setData(operativeAcctIntRecvParamTO.toString());
//            objLogTO.setPrimaryKey(operativeAcctIntRecvParamTO.getKeyData());
            objLogDAO.addToLog(objLogTO);
        } catch (Exception e) {
            sqlMap.rollbackTransaction();
            e.printStackTrace();
            throw new TransRollbackException(e);
        }
    }

    private void deleteData(HashMap map, LogDAO objLogDAO, LogTO objLogTO) throws Exception {
        try {
            mdsProductTO.setStatusBy((String) map.get(CommonConstants.USER_ID));
            mdsProductTO.setStatus(CommonConstants.STATUS_MODIFIED);
            mdsProductTO.setStatusDt(currDt);
            sqlMap.executeUpdate("deleteMDSProductTO", mdsProductTO);
//            objLogTO.setData(operativeAcctProductTO.toString());
//            objLogTO.setPrimaryKey(operativeAcctProductTO.getKeyData());
            objLogDAO.addToLog(objLogTO);
        } catch (Exception e) {
            sqlMap.rollbackTransaction();
            e.printStackTrace();
            throw new TransRollbackException(e);
        }
    }

    public static void main(String str[]) {
        try {
            MDSProductDAO dao = new MDSProductDAO();
            HashMap inputMap = new HashMap();
            //inputMap.put("ACCT_HD", "CCS_D");

            System.out.println(sqlMap.executeQueryForList("OperativeAcctProduct.getSelectAcctHeadTOList", null));

        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public HashMap execute(HashMap map) throws Exception {
        _branchCode = (String) map.get(CommonConstants.BRANCH_ID);
        currDt = ServerUtil.getCurrentDate(_branchCode);
        /*
         * To Verify The Account Head data...
         */
        System.out.println("Map in DAO: " + map);
        if (map.containsKey("ACCT_HD")) {
            ServerUtil.verifyAccountHead(map);
        }
        /*
         * data fot the Normal operations like Insert, Update, and/or Delete...
         */
        // Log DAO
        LogDAO objLogDAO = new LogDAO();

        // Log Transfer Object
        LogTO objLogTO = new LogTO();
        objLogTO.setUserId((String) map.get(CommonConstants.USER_ID));
        objLogTO.setBranchId((String) map.get(CommonConstants.BRANCH_ID));
        objLogTO.setIpAddr((String) map.get(CommonConstants.IP_ADDR));
        objLogTO.setModule((String) map.get(CommonConstants.MODULE));
        objLogTO.setScreen((String) map.get(CommonConstants.SCREEN));

        mdsProductOtherDetailsTO = new MDSProductOtherDetailsTO();
        mdsProductTO = new MDSProductTO();
        mdsProductTO = (MDSProductTO) map.get("mdsProductTO");
        mdsProductOtherDetailsTO = (MDSProductOtherDetailsTO) map.get("mdsProductOtherDetailsTO");
        System.out.println("mdsProductTO " + mdsProductTO);
        System.out.println("mdsProductTO...." + mdsProductTO.getRdoPending() + "...." + mdsProductTO.getRdoPrized() + "mmmm" + mdsProductTO.getRdoMethod1() + "mmmm" + mdsProductTO.getRdoMethod2());
        System.out.println("mdsProductOtherDetailsTO " + mdsProductOtherDetailsTO);
        if (map.containsKey("mdsProductTO")) {
            final String command = CommonUtil.convertObjToStr(map.get("COMMAND"));
            try {
                System.out.println("try Block :");
                sqlMap.startTransaction();
                objLogTO.setStatus(command);
                if (command.equals(CommonConstants.TOSTATUS_INSERT)) {
                    System.out.println("insert record ");
                    insertData(map, objLogDAO, objLogTO);
                    System.out.println("insert record completed");
                } else if (command.equals(CommonConstants.TOSTATUS_UPDATE)) {
                    updateData(map, objLogDAO, objLogTO);
                } else if (command.equals(CommonConstants.TOSTATUS_DELETE)) {
                    deleteData(map, objLogDAO, objLogTO);
                } else {
                    throw new NoCommandException();
                }

                sqlMap.commitTransaction();
            } catch (Exception e) {
                sqlMap.rollbackTransaction();
                e.printStackTrace();
                throw e;
            }
            objLogDAO = null;
            objLogTO = null;
            destroyObjects();
        }
        return null;
    }

    //    private void verifyAccountHead(HashMap inputMap)throws Exception{
    //        System.out.println("In verifyAccountHead");
    //        engine = new RuleEngine();
    //        context = new RuleContext();
    //        context.addRule(new AccountMaintenanceRule());
    //
    //        ArrayList list = (ArrayList)engine.validateAll(context, inputMap);
    //        if(list!=null ){
    //            System.out.println("list in DAO: "+list);
    //            HashMap exception = new HashMap();
    //            exception.put(CommonConstants.EXCEPTION_LIST, list);
    //            exception.put(CommonConstants.CONSTANT_CLASS,
    //            "com.see.truetransact.clientutil.exceptionhashmap.generalledger.GeneralLedgerRuleHashMap");
    //            throw new TTException(exception);
    //            //sqlMap.rollbackTransaction();
    //        }
    //
    //    }
    public HashMap executeQuery(HashMap obj) throws Exception {
        _branchCode = (String) obj.get(CommonConstants.BRANCH_ID);
        currDt = ServerUtil.getCurrentDate(_branchCode);
        return getData(obj);
    }

    private void destroyObjects() {
//        operativeAcctProductTO = null;
//        operativeAcctParamTO= null;
//        operativeAcctIntRecvParamTO = null;
//        operativeAcctIntPayParamTO = null;
//        operativeAcctChargesParamTO = null;
//        operativeAcctSpclitemParamTO = null;
//        operativeAcctHeadParamTO = null;
//        operativeAcctIntRateParamTO = null;
    }
    //    private void insertOperativeIntRateTOs(LogDAO objLogDAO, LogTO objLogTO) throws Exception {
    //        try{
    //            int j = operativeAcctIntRateTOs.size();
    //            for (int i=0;i<j;i++) {
    //                operativeAcctIntRateParamTO = (OperativeAcctIntRateParamTO)operativeAcctIntRateTOs.get(i);
    //                operativeAcctIntRateParamTO.setProdId(operativeAcctProductTO.getProdId());
    //                sqlMap.executeUpdate("insertOperativeAcctIntRateParamTO", operativeAcctIntRateParamTO);
    //                objLogTO.setData(operativeAcctIntRateParamTO.toString());
    //                objLogTO.setPrimaryKey(operativeAcctIntRateParamTO.getKeyData());
    //                objLogDAO.addToLog(objLogTO);
    //            }
    //        }catch(Exception e) {
    //            throw new Exception();
    //        }
    //    }

    private void getTOs(HashMap map) {
//        operativeAcctParamTO= (OperativeAcctParamTO) map.get("OperativeAcctParamTO");
//        operativeAcctIntRecvParamTO = (OperativeAcctIntRecvParamTO) map.get("OperativeAcctIntRecvParamTO");
//        operativeAcctIntPayParamTO = (OperativeAcctIntPayParamTO) map.get("OperativeAcctIntPayParamTO");
//        operativeAcctChargesParamTO = (OperativeAcctChargesParamTO) map.get("OperativeAcctChargesParamTO");
//        operativeAcctSpclitemParamTO = (OperativeAcctSpclitemParamTO) map.get("OperativeAcctSpclitemParamTO");
//        operativeAcctHeadParamTO = (OperativeAcctHeadParamTO) map.get("OperativeAcctHeadParamTO");
//        operativeAcctIntRateTOs = (ArrayList)map.get("OperativeAcctIntRateParamTO");
    }
}
