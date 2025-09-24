/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition.
 * 
 *
 * DepositsProductDAO.java
 *
 * Created on Fri Dec 12 10:18:35 IST 2003
 */
package com.see.truetransact.serverside.product.deposits;

import java.util.List;
import java.util.ArrayList;
import java.util.HashMap;


import com.ibatis.db.sqlmap.SqlMap;
import com.see.truetransact.serverutil.ServerUtil;

import com.see.truetransact.commonutil.CommonConstants;
import com.see.truetransact.commonutil.NoCommandException;
import com.see.truetransact.serverside.TTDAO;
import com.see.truetransact.transferobject.TOHeader;
import com.see.truetransact.serverutil.ServerConstants;
import com.see.truetransact.servicelocator.ServiceLocator;
import com.see.truetransact.serverutil.ServerUtil;
import com.see.truetransact.serverexception.TransRollbackException;
import com.see.truetransact.serverexception.ServiceLocatorException;

import com.see.truetransact.serverside.deposit.interestmaintenance.InterestMaintenanceGroupDAO;

import com.see.truetransact.commonutil.TTException;

import com.see.truetransact.transferobject.product.deposits.DepositsProductTO;
import com.see.truetransact.transferobject.product.deposits.DepositsProductSchemeTO;
import com.see.truetransact.transferobject.product.deposits.DepositsProductAcHdTO;
import com.see.truetransact.transferobject.product.deposits.DepositsProductIntPayTO;
import com.see.truetransact.transferobject.product.deposits.DepositsProductRDTO;
import com.see.truetransact.transferobject.product.deposits.DepositsProductRenewalTO;
import com.see.truetransact.transferobject.product.deposits.DepositsProductTaxTO;

// For the Business Rules...
import com.see.truetransact.businessrule.RuleContext;
import com.see.truetransact.businessrule.RuleEngine;
import com.see.truetransact.businessrule.ValidationRule;
import com.see.truetransact.businessrule.generalledger.AccountMaintenanceRule;
import com.see.truetransact.commonutil.CommonUtil;

// For Maintaining Logs...
import com.see.truetransact.serverside.common.log.LogDAO;
import com.see.truetransact.transferobject.common.log.LogTO;

import com.see.truetransact.transferobject.deposit.interestmaintenance.InterestMaintenanceRateTO;
import com.see.truetransact.transferobject.product.deposits.AgentCommissionSlabSettingsTO;
import com.see.truetransact.transferobject.product.deposits.DepositsThriftBenevolentTO;
import com.see.truetransact.transferobject.product.deposits.WeeklyDepositSlabSettingsTO;

/**
 * DepositsProduct DAO.
 *
 */
public class DepositsProductDAO extends TTDAO {

    private static SqlMap sqlMap = null;
    private DepositsProductTO objDepositsProductTO;
    private DepositsProductSchemeTO objDepositsProductSchemeTO;
    private DepositsProductIntPayTO objDepositsProductIntPayTO;
    private DepositsProductAcHdTO objDepositsProductAcHdTO;
    private DepositsProductTaxTO objDepositsProductTaxTO;
    private DepositsProductRenewalTO objDepositsProductRenewalTO;
    private DepositsProductRDTO objDepositsProductRDTO;
    private InterestMaintenanceRateTO objInterestMaintenanceRateTO;
    private InterestMaintenanceGroupDAO interestMaintenanceGroupDAO;
    RuleContext context;
    RuleEngine engine;
    private ArrayList AgentsList = new ArrayList();
    private ArrayList delayedList = new ArrayList();
    private WeeklyDepositSlabSettingsTO objWeeklyDepositSettingsTo;
    private ArrayList weeklyDepositSettingsList;
    private ArrayList agentCommissionSlabSettingsList;
    private AgentCommissionSlabSettingsTO objAgentCommissionSlabSettingsTO;
    private DepositsThriftBenevolentTO objDepositsThriftBenevolentTO; // Added by nithya on 02-03-2016 for 0003897
    /**
     * Creates a new instance of DepositsProductDAO
     */
    public DepositsProductDAO() throws ServiceLocatorException {
        ServiceLocator locate = ServiceLocator.getInstance();
        sqlMap = (SqlMap) locate.getDAOSqlMap();
    }

    private HashMap getData(HashMap map) throws Exception {
        HashMap returnMap = new HashMap();
        HashMap depoThriftBenevolentMap = new HashMap();
        
        String where = (String) map.get(CommonConstants.MAP_WHERE);
        List list = (List) sqlMap.executeQueryForList("getSelectDepositsProductTO", where);
        if(list!=null && list.size()>0){
        returnMap.put("DEPT_PROD", list);
        }

        list = (List) sqlMap.executeQueryForList("getSelectDepositsProductSchemeTO", where);
        if(list!=null && list.size()>0){
        returnMap.put("DepositsProductSchemeTO", list);
        DepositsProductSchemeTO objDepositsProductSchemeTO = (DepositsProductSchemeTO)list.get(0);
        if(objDepositsProductSchemeTO.getAgentcommSlabRequired() != null && objDepositsProductSchemeTO.getAgentcommSlabRequired().length() > 0 && objDepositsProductSchemeTO.getAgentcommSlabRequired().equalsIgnoreCase("Y")){
            List newLst = null;
            newLst = (List) sqlMap.executeQueryForList("getAgentCommissionCalcSlabData", objDepositsProductSchemeTO);
            returnMap.put("AGENT_COMM_SLAB_SETTING", newLst); 
        }
       }

        list = (List) sqlMap.executeQueryForList("getSelectDepositsProductIntPayTO", where);
        if(list!=null && list.size()>0){
        returnMap.put("DEPT_PROD_INTPAY", list);
        }

        list = (List) sqlMap.executeQueryForList("getSelectDepositsProductAcHdTO", where);
        if(list!=null && list.size()>0){
        returnMap.put("DEPT_PROD_ACHD", list);
        }

        list = (List) sqlMap.executeQueryForList("getSelectDepositsProductRenewalTO", where);
        if(list!=null && list.size()>0){
        returnMap.put("DEPT_PROD_RENEWAL", list);
        }

        list = (List) sqlMap.executeQueryForList("getSelectDepositsProductTaxTO", where);
        if(list!=null && list.size()>0){
        returnMap.put("DEPT_PROD_TAX", list);
        }

       // Added by nithya on 02-03-2016 for 0003897
        
        list = (List) sqlMap.executeQueryForList("getSelectedDepositsThriftBenevolentTO", where);
        if(list != null && list.size() > 0){
        	returnMap.put("DEPO_THRIFT_BENEVOLENT",list);
        }         
       
        // End
        
        list = (List) sqlMap.executeQueryForList("getSelectDepositsProductRDTO", where);
        System.out.println("list====" + list);
        if (list != null && list.size() > 0) {
            returnMap.put("DEPT_PROD_RD", list);
            //Added by Anju Anand for Mantis Id: 0010363
            DepositsProductRDTO objDepProductRDTO = (DepositsProductRDTO) list.get(0);
            if (objDepProductRDTO.getDepositFreq() != null && CommonUtil.convertObjToStr(objDepProductRDTO.getDepositFreq()).length() > 0 && (objDepProductRDTO.getDepositFreq().equals("7") || objDepProductRDTO.getDepositFreq().equals("999"))) { //Added 999(installments) by nithya on 23-01-2017 for 0005664
                List newLst = null;
                newLst = (List) sqlMap.executeQueryForList("getWeeklyDepSlabData", objDepProductRDTO);
                returnMap.put("WEEKLY_DEP_SETTING", newLst);
            }
        }
       

        //Agents interestRateMaintenance.... For dailyDeposit Customer.....
        System.out.println("AGENTS_COMMISION_DAILY : " + where);
        HashMap dailyMap = new HashMap();
        dailyMap.put("ROI_GROUP_ID", where);
        list = (List) sqlMap.executeQueryForList("getSelectDepositsCommision", dailyMap);
        returnMap.put("AGENTS_COMMISION_DAILY", list);
        System.out.println("AGENTS_COMMISION_DAILY LIST : " + list);
        System.out.println("Authorization return map" + returnMap);
        return returnMap;
    }

//    private HashMap AgentsCommision () {
//        int AgentsCommision = AgentsList.size();
//        
//        for(int i=0; i<AgentsCommision; i++) {
//            HashMap returnMap = new HashMap();
//            sqlMap.executeUpdate("insertInterestMaintananceRateTO", objInterestMaintenanceRateTO);
//            returnMap.put("InterestMaintenanceRateTO", list);
//        }
//        returnMap;
//    }
    private void insertData(LogDAO objLogDAO, LogTO objLogTO, ArrayList weeklyDepositSettingsList, WeeklyDepositSlabSettingsTO objWeeklyDepTo) throws Exception {
        try {
            //            sqlMap.startTransaction();
            objDepositsProductTO.setStatus(CommonConstants.STATUS_CREATED);

            sqlMap.executeUpdate("insertDepositsProductTO", objDepositsProductTO);
            objLogTO.setData(objDepositsProductTO.toString());
            objLogTO.setPrimaryKey(objDepositsProductTO.getKeyData());
            objLogDAO.addToLog(objLogTO);

            sqlMap.executeUpdate("insertDepositsProductSchemeTO", objDepositsProductSchemeTO);
            objLogTO.setData(objDepositsProductSchemeTO.toString());
            objLogTO.setPrimaryKey(objDepositsProductSchemeTO.getKeyData());
            objLogDAO.addToLog(objLogTO);

            sqlMap.executeUpdate("insertDepositsProductIntPayTO", objDepositsProductIntPayTO);
            objLogTO.setData(objDepositsProductIntPayTO.toString());
            objLogTO.setPrimaryKey(objDepositsProductIntPayTO.getKeyData());
            objLogDAO.addToLog(objLogTO);

            sqlMap.executeUpdate("insertDepositsProductAcHdTO", objDepositsProductAcHdTO);
            objLogTO.setData(objDepositsProductAcHdTO.toString());
            objLogTO.setPrimaryKey(objDepositsProductAcHdTO.getKeyData());
            objLogDAO.addToLog(objLogTO);

            sqlMap.executeUpdate("insertDepositsProductTaxTO", objDepositsProductTaxTO);
            objLogTO.setData(objDepositsProductTaxTO.toString());
            objLogTO.setPrimaryKey(objDepositsProductTaxTO.getKeyData());
            objLogDAO.addToLog(objLogTO);

            sqlMap.executeUpdate("insertDepositsProductRenewalTO", objDepositsProductRenewalTO);
            objLogTO.setData(objDepositsProductRenewalTO.toString());
            objLogTO.setPrimaryKey(objDepositsProductRenewalTO.getKeyData());
            objLogDAO.addToLog(objLogTO);

            sqlMap.executeUpdate("insertDepositsProductRDTO", objDepositsProductRDTO);
            objLogTO.setData(objDepositsProductRDTO.toString());
            objLogTO.setPrimaryKey(objDepositsProductRDTO.getKeyData());
            objLogDAO.addToLog(objLogTO);

            int lst = delayedList.size();
            ArrayList delayed = new ArrayList();
            for (int i = 0; i < lst; i++) {
                delayed = (ArrayList) delayedList.get(i);
                HashMap delayedMap = new HashMap();
                sqlMap.executeUpdate("insertRdDelayedInstallments", delayedMap);
                objLogTO.setData(objDepositsProductRDTO.toString());
                objLogTO.setPrimaryKey(objDepositsProductRDTO.getKeyData());
                objLogDAO.addToLog(objLogTO);
            }
            //update id generation 
            HashMap idMap = new HashMap();
            idMap.put("ID_KEY", "LIABILITY_ID");
            sqlMap.executeUpdate("updateIDGenerated", idMap);
            //            sqlMap.commitTransaction();
            
            //Added by Anju Anand for Mantis Id: 0010363
            if (weeklyDepositSettingsList != null && weeklyDepositSettingsList.size() > 0) {
                for (int i = 0; i < weeklyDepositSettingsList.size(); i++) {
                    objWeeklyDepositSettingsTo = (WeeklyDepositSlabSettingsTO) weeklyDepositSettingsList.get(i);
                    objWeeklyDepositSettingsTo.setProdId(objDepositsProductRDTO.getProdId());
                    objWeeklyDepositSettingsTo.setCreatedBy(objWeeklyDepTo.getCreatedBy());
                    objWeeklyDepositSettingsTo.setCreatedDt(objWeeklyDepTo.getCreatedDt());
                    objWeeklyDepositSettingsTo.setStatus(objWeeklyDepTo.getStatus());
                    objWeeklyDepositSettingsTo.setStatusBy(objWeeklyDepTo.getStatusBy());
                    objWeeklyDepositSettingsTo.setStatusDt(objWeeklyDepTo.getStatusDt());
                    objWeeklyDepositSettingsTo.setAuthorizeStatus(objWeeklyDepTo.getAuthorizeStatus());
                    objWeeklyDepositSettingsTo.setAuthorizeDt(objWeeklyDepTo.getAuthorizeDt());
                    objWeeklyDepositSettingsTo.setAuthorizeBy(objWeeklyDepTo.getAuthorizeBy());
                    sqlMap.executeUpdate("insertWeeklyDepositTO", objWeeklyDepositSettingsTo);
                }
            }
            
            // Added by nithya on 17-06-2020
            if(agentCommissionSlabSettingsList != null && agentCommissionSlabSettingsList.size() > 0){
                for(int i=0; i < agentCommissionSlabSettingsList.size(); i++){
                    objAgentCommissionSlabSettingsTO = (AgentCommissionSlabSettingsTO)agentCommissionSlabSettingsList.get(i);  
                    objAgentCommissionSlabSettingsTO.setProdId(objDepositsProductTO.getProdId());
                    sqlMap.executeUpdate("insertAgentCommissionSlabSettingsTO", objAgentCommissionSlabSettingsTO);
                }
            }
            // End
            
            // Added by nithya on 02-03-2016 for 0003897
            if(objDepositsThriftBenevolentTO != null){
                sqlMap.executeUpdate("insertDepositsThriftBenevolentTO", objDepositsThriftBenevolentTO);
                objLogTO.setData(objDepositsThriftBenevolentTO.toString());
                objLogTO.setPrimaryKey(objDepositsThriftBenevolentTO.getKeyData());
                objLogDAO.addToLog(objLogTO);
            }
            // End
            
        } catch (Exception e) {
            sqlMap.rollbackTransaction();
            e.printStackTrace();
            throw new TransRollbackException(e);
        }
    }

    private void updateData(LogDAO objLogDAO, LogTO objLogTO, ArrayList weeklyDepositSettingsList, WeeklyDepositSlabSettingsTO objWeeklyDepTo) throws Exception {
        try {
            //            sqlMap.startTransaction();
            objDepositsProductTO.setStatus(CommonConstants.STATUS_MODIFIED);

            sqlMap.executeUpdate("updateDepositsProductTO", objDepositsProductTO);
            objLogTO.setData(objDepositsProductTO.toString());
            objLogTO.setPrimaryKey(objDepositsProductTO.getKeyData());
            objLogDAO.addToLog(objLogTO);

            sqlMap.executeUpdate("updateDepositsProductSchemeTO", objDepositsProductSchemeTO);
            objLogTO.setData(objDepositsProductSchemeTO.toString());
            objLogTO.setPrimaryKey(objDepositsProductSchemeTO.getKeyData());
            objLogDAO.addToLog(objLogTO);

            sqlMap.executeUpdate("updateDepositsProductIntPayTO", objDepositsProductIntPayTO);
            objLogTO.setData(objDepositsProductIntPayTO.toString());
            objLogTO.setPrimaryKey(objDepositsProductIntPayTO.getKeyData());
            objLogDAO.addToLog(objLogTO);

            sqlMap.executeUpdate("updateDepositsProductAcHdTO", objDepositsProductAcHdTO);
            objLogTO.setData(objDepositsProductAcHdTO.toString());
            objLogTO.setPrimaryKey(objDepositsProductAcHdTO.getKeyData());
            objLogDAO.addToLog(objLogTO);

            sqlMap.executeUpdate("updateDepositsProductTaxTO", objDepositsProductTaxTO);
            objLogTO.setData(objDepositsProductTaxTO.toString());
            objLogTO.setPrimaryKey(objDepositsProductTaxTO.getKeyData());
            objLogDAO.addToLog(objLogTO);
            System.out.println("weeklyBasis  " + objDepositsProductRenewalTO.getWeeklyBasis());
            sqlMap.executeUpdate("updateDepositsProductRenewalTO", objDepositsProductRenewalTO);
            objLogTO.setData(objDepositsProductRenewalTO.toString());
            objLogTO.setPrimaryKey(objDepositsProductRenewalTO.getKeyData());
            objLogDAO.addToLog(objLogTO);

            sqlMap.executeUpdate("updateDepositsProductRDTO", objDepositsProductRDTO);
            objLogTO.setData(objDepositsProductRDTO.toString());
            objLogTO.setPrimaryKey(objDepositsProductRDTO.getKeyData());
            objLogDAO.addToLog(objLogTO);
            
            // Added by nithya on 02-03-2016 for 0003897
            
            if(objDepositsThriftBenevolentTO != null){
                sqlMap.executeUpdate("insertDepositsThriftBenevolentTO", objDepositsThriftBenevolentTO);
                objLogTO.setData(objDepositsThriftBenevolentTO.toString());
                objLogTO.setPrimaryKey(objDepositsProductRDTO.getKeyData());
                objLogDAO.addToLog(objLogTO);
            }
            
            // End


            int lst = delayedList.size();
            ArrayList delayed = new ArrayList();
            for (int i = 0; i < lst; i++) {
                delayed = (ArrayList) delayedList.get(i);
                HashMap delayedMap = new HashMap();
//                delayedMap.put("FROM_AMT",delayed.get)
                sqlMap.executeUpdate("updateRdDelayedInstallments", delayedMap);
                objLogTO.setData(objDepositsProductRDTO.toString());
                objLogTO.setPrimaryKey(objDepositsProductRDTO.getKeyData());
                objLogDAO.addToLog(objLogTO);
            }
            
            //Added by Anju Anand for Mantis Id: 0010363
            if (weeklyDepositSettingsList != null && weeklyDepositSettingsList.size() > 0) {
                sqlMap.executeUpdate("deleteWeeklyDepositTO", objWeeklyDepTo);
                for (int i = 0; i < weeklyDepositSettingsList.size(); i++) {
                    objWeeklyDepositSettingsTo = (WeeklyDepositSlabSettingsTO) weeklyDepositSettingsList.get(i);
                    objWeeklyDepositSettingsTo.setProdId(objDepositsProductRDTO.getProdId());
                    objWeeklyDepositSettingsTo.setCreatedBy(objWeeklyDepTo.getCreatedBy());
                    objWeeklyDepositSettingsTo.setCreatedDt(objWeeklyDepTo.getCreatedDt());
                    objWeeklyDepositSettingsTo.setStatus(objWeeklyDepTo.getStatus());
                    objWeeklyDepositSettingsTo.setStatusBy(objWeeklyDepTo.getStatusBy());
                    objWeeklyDepositSettingsTo.setStatusDt(objWeeklyDepTo.getStatusDt());
                    objWeeklyDepositSettingsTo.setAuthorizeStatus(objWeeklyDepTo.getAuthorizeStatus());
                    objWeeklyDepositSettingsTo.setAuthorizeDt(objWeeklyDepTo.getAuthorizeDt());
                    objWeeklyDepositSettingsTo.setAuthorizeBy(objWeeklyDepTo.getAuthorizeBy());
                    sqlMap.executeUpdate("insertWeeklyDepositTO", objWeeklyDepositSettingsTo);
                }
            }
            
             // Added by nithya on 17-06-2020
            if(agentCommissionSlabSettingsList != null && agentCommissionSlabSettingsList.size() > 0){
                sqlMap.executeUpdate("deleteAgentCommissionSlabSettingsTO", objDepositsProductTO);
                for(int i=0; i < agentCommissionSlabSettingsList.size(); i++){
                    System.out.println("objAgentCommissionSlabSettingsTO inside :: " + objAgentCommissionSlabSettingsTO);
                    objAgentCommissionSlabSettingsTO = (AgentCommissionSlabSettingsTO)agentCommissionSlabSettingsList.get(i); 
                    objAgentCommissionSlabSettingsTO.setProdId(objDepositsProductTO.getProdId());
                    objAgentCommissionSlabSettingsTO.setStatus("CREATED");
                    objAgentCommissionSlabSettingsTO.setStatusDt(objDepositsProductTO.getStatusDt());
                    objAgentCommissionSlabSettingsTO.setCreatedBy(objDepositsProductTO.getCreatedBy());
                    objAgentCommissionSlabSettingsTO.setCreatedDt(objDepositsProductTO.getCreatedDt());
                    sqlMap.executeUpdate("insertAgentCommissionSlabSettingsTO", objAgentCommissionSlabSettingsTO);
                }
            }
            // End

            //            sqlMap.commitTransaction();
        } catch (Exception e) {
            sqlMap.rollbackTransaction();
            e.printStackTrace();
            throw new TransRollbackException(e);
        }
    }

    private void deleteData(LogDAO objLogDAO, LogTO objLogTO) throws Exception {
        try {
            //            sqlMap.startTransaction();
            objDepositsProductTO.setStatus(CommonConstants.STATUS_DELETED);

            sqlMap.executeUpdate("deleteDepositsProductTO", objDepositsProductTO);
            objLogTO.setData(objDepositsProductTO.toString());
            objLogTO.setPrimaryKey(objDepositsProductTO.getKeyData());
            objLogDAO.addToLog(objLogTO);
            /*
             * sqlMap.executeUpdate ("deleteDepositsProductSchemeTO",
             * objDepositsProductSchemeTO); sqlMap.executeUpdate
             * ("deleteDepositsProductIntPayTO", objDepositsProductIntPayTO);
             * sqlMap.executeUpdate ("deleteDepositsProductAcHdTO",
             * objDepositsProductAcHdTO); sqlMap.executeUpdate
             * ("deleteDepositsProductTaxTO", objDepositsProductTaxTO);
             * sqlMap.executeUpdate ("deleteDepositsProductRenewalTO",
             * objDepositsProductRenewalTO); sqlMap.executeUpdate
             * ("deleteDepositsProductRDTO", objDepositsProductRDTO);
             */
            //            sqlMap.commitTransaction();
        } catch (Exception e) {
            sqlMap.rollbackTransaction();
            e.printStackTrace();
            throw new TransRollbackException(e);
        }
    }

    public HashMap execute(HashMap map) throws Exception {
        _branchCode = (String) map.get(CommonConstants.BRANCH_ID);
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
        HashMap returnMap = null;
        // Log Transfer Object
        LogTO objLogTO = new LogTO();
        objLogTO.setUserId((String) map.get(CommonConstants.USER_ID));
        objLogTO.setBranchId((String) map.get(CommonConstants.BRANCH_ID));
        objLogTO.setIpAddr((String) map.get(CommonConstants.IP_ADDR));
        objLogTO.setModule((String) map.get(CommonConstants.MODULE));
        objLogTO.setScreen((String) map.get(CommonConstants.SCREEN));

        if (map.containsKey("DELAYED_MAP") && map.get("DELAYED_MAP") != null) {
            delayedList = (ArrayList) map.get("DELAYED_MAP");
        }

        if (map.containsKey("DEPT_PROD")) {

            objDepositsProductTO = (DepositsProductTO) map.get("DEPT_PROD");
            objDepositsProductSchemeTO = (DepositsProductSchemeTO) map.get("DEPT_PROD_SCHEME");
            objDepositsProductIntPayTO = (DepositsProductIntPayTO) map.get("DEPT_PROD_INTPAY");
            objDepositsProductAcHdTO = (DepositsProductAcHdTO) map.get("DEPT_PROD_ACHD");
            objDepositsProductTaxTO = (DepositsProductTaxTO) map.get("DEPT_PROD_TAX");
            objDepositsProductRenewalTO = (DepositsProductRenewalTO) map.get("DEPT_PROD_RENEWAL");
            objDepositsProductRDTO = (DepositsProductRDTO) map.get("DEPT_PROD_RD");
            
            // Added by nithya on 02-03-2016 for 0003897
            if(map.containsKey("DEPO_THRIFT_BENEVOLENT")){
                objDepositsThriftBenevolentTO = (DepositsThriftBenevolentTO) map.get("DEPO_THRIFT_BENEVOLENT");
            }
            // End
            
            final String command = objDepositsProductTO.getCommand();
           
            //Added by Anju Anand for Mantis Id: 0010363
            if (map.containsKey("WeeklyDepositSlabData") && !map.get("WeeklyDepositSlabData").equals("") && map.get("WeeklyDepositSlabData") != null) {
                weeklyDepositSettingsList = (ArrayList) map.get("WeeklyDepositSlabData");
                objWeeklyDepositSettingsTo = (WeeklyDepositSlabSettingsTO) map.get("WeeklyDepositData");
            } 
            
            //Added  by nithya on 17-06-2020
             if (map.containsKey("AgentCommissionSlabData") && !map.get("AgentCommissionSlabData").equals("") && map.get("AgentCommissionSlabData") != null) {
                agentCommissionSlabSettingsList = (ArrayList) map.get("AgentCommissionSlabData");                
            }
             //End
            
            try {
                sqlMap.startTransaction();
                objLogTO.setStatus(command);

                if (command.equals(CommonConstants.TOSTATUS_INSERT)) {
                    insertData(objLogDAO, objLogTO, weeklyDepositSettingsList, objWeeklyDepositSettingsTo);
//                    if (objDepositsProductTO != null) {
//                        System.out.println("**** objDepositsProductTO :" + objDepositsProductTO);
////                        returnMap.put("PROD_ID",objDepositsProductTO.getProdId());
//                    }
                } else if (command.equals(CommonConstants.TOSTATUS_UPDATE)) {
                    updateData(objLogDAO, objLogTO, weeklyDepositSettingsList, objWeeklyDepositSettingsTo);
                } else if (command.equals(CommonConstants.TOSTATUS_DELETE)) {
                    deleteData(objLogDAO, objLogTO);;
                } else {
                    throw new NoCommandException();
                }

                sqlMap.commitTransaction();

// Agents InterestMaintenance sending to interestMaintenanceGroupDAO from here......
                ArrayList arrayList = (ArrayList) map.get("AGENTS_COMMISION");
                if (map.containsKey("AGENTS_COMMISION") && map.get("AGENTS_COMMISION") != null && arrayList.size() > 0) {
                    interestMaintenanceGroupDAO = new InterestMaintenanceGroupDAO();
                    map.put("BRAHCH_ID", _branchCode);
                    map.put("COMMAND", command);
                    interestMaintenanceGroupDAO.execute(map);
                    System.out.println("######InterestMaintenanceRateTO :" + map);

                }
                ArrayList deletedArrayList = (ArrayList) map.get("AGENTS_COMMISION_DELETED_RECORD");
                if (map.containsKey("AGENTS_COMMISION_DELETED_RECORD") && map.get("AGENTS_COMMISION_DELETED_RECORD") != null
                        && deletedArrayList.size() > 0) {
                    interestMaintenanceGroupDAO = new InterestMaintenanceGroupDAO();
                    map.put("BRAHCH_ID", _branchCode);
                    map.put("COMMAND", command);
                    interestMaintenanceGroupDAO.execute(map);
                    System.out.println("######InterestMaintenanceRateTO :" + map);
                }

            } catch (Exception e) {
                sqlMap.rollbackTransaction();
                e.printStackTrace();
                throw e;
            }
            objLogDAO = null;
            objLogTO = null;

            destroyObjects();
        }
        return returnMap;
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
        return getData(obj);
    }

    private void destroyObjects() {
        objDepositsProductTO = null;
        agentCommissionSlabSettingsList = null;
        objAgentCommissionSlabSettingsTO = null;
    }

    public static void main(String str[]) {
        try {
            DepositsProductDAO objDepositsProductDAO = new DepositsProductDAO();



            DepositsProductTO objDepositsProductTO = new DepositsProductTO();
            TOHeader toHeader = new TOHeader();
            toHeader.setCommand(CommonConstants.TOSTATUS_INSERT);
            objDepositsProductTO.setStatus(CommonConstants.STATUS_CREATED);

            objDepositsProductTO.setProdId("DP101");
            objDepositsProductTO.setProdDesc("Gold Deposit");
            objDepositsProductTO.setAcctHead("AH101");
            objDepositsProductTO.setBehavesLike("Deposit");
//            objDepositsProductTO.setRemarks("Opportunity");


            objDepositsProductTO.setTOHeader(toHeader);

            HashMap hash = new HashMap();
            hash.put("DEPT_PROD", objDepositsProductTO);

            objDepositsProductDAO.execute(hash);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}
