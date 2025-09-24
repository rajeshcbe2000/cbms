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
import java.util.Date;
import java.util.HashMap;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import org.apache.log4j.Logger;
import com.ibatis.db.sqlmap.SqlMap;
import com.see.truetransact.serverside.TTDAO;
import com.see.truetransact.serverutil.ServerUtil;
import com.see.truetransact.commonutil.CommonUtil;
import com.see.truetransact.commonutil.CommonConstants;
import com.see.truetransact.commonutil.DateUtil;
import com.see.truetransact.serverside.common.log.LogDAO;
import com.see.truetransact.servicelocator.ServiceLocator;
import com.see.truetransact.transferobject.common.log.LogTO;
import com.see.truetransact.serverexception.TransRollbackException;
import com.see.truetransact.transferobject.product.operativeacct.*;
import com.see.truetransact.serverexception.ServiceLocatorException;
import com.see.truetransact.transferobject.mdsapplication.mdsprizedmoneydetailsentry.MDSPrizedMoneyDetailsEntryTO;
import com.see.truetransact.transferobject.mdsapplication.mdsprizedmoneypayment.MDSPrizedMoneyPaymentTO;
import com.see.truetransact.transferobject.product.mds.MDSProductSchemeTO;
import com.see.truetransact.transferobject.product.mds.MDSProductAcctHeadTO;
import com.see.truetransact.transferobject.product.loan.LoanProductChargesTabTO;
import com.see.truetransact.transferobject.product.mds.MDSProductInstallmentScheduleTO;
import com.see.truetransact.transferobject.sms.SMSParameterTO;
import java.sql.SQLException;

/**
 * This is used for OperativeAcctProductDAO Data Access.
 *
 * @author Balachandar
 *
 * @modified Pinky @modified Rahul
 */
public class MDSTypeDAO extends TTDAO {

    private final static Logger log = Logger.getLogger(MDSTypeDAO.class);
    private LoanProductChargesTabTO loanProductChargesTabTO;
    private MDSProductInstallmentScheduleTO objScheduleTO;
    private LinkedHashMap deletedTableValues = null;
    private LinkedHashMap tableDetails = null;
    private MDSProductAcctHeadTO acctHeadTo;
    private ArrayList loanProductchrgTabTO;
    private MDSProductSchemeTO schemeTo;
    private static SqlMap sqlMap = null;
    private Date CurrDt = null;
    private LogDAO logDAO;
    private LogTO logTO;
    private SMSParameterTO objSMSParameterTO;
    int key = 1; 
    int totMembers = 0;

    /**
     * Creates a new instance of OperativeAcctProductDAO
     */
    public MDSTypeDAO() throws ServiceLocatorException {
        ServiceLocator locate = ServiceLocator.getInstance();
        sqlMap = (SqlMap) locate.getDAOSqlMap();
    }
    private List getSMSParamData(HashMap authMap) {
        List smsList = null;
        smsList = new ArrayList();
        HashMap whereMap = new HashMap();
        try {
            if ((authMap.containsKey("PROD_ID") && authMap.get("PROD_ID") != null)
                    && (authMap.containsKey("SCHEME_NAME") && authMap.get("SCHEME_NAME") != null)) {
                whereMap.put("PROD_TYPE",CommonConstants.MDS_TRANSMODE_TYPE);
                whereMap.put("PROD_ID",authMap.get("SCHEME_NAME"));
                smsList = (List) sqlMap.executeQueryForList("getSelectSMSParameter", whereMap);
            }
            return smsList;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return smsList;
    }
    private HashMap getData(HashMap map) throws Exception {
        System.out.println("@@@@@@@map" + map);
        HashMap returnMap = new HashMap();
        List smsList = getSMSParamData(map);
        if(smsList != null && smsList.size() > 0){
            returnMap.put("SMSListTO", smsList);
        }
        List schemeList = (List) sqlMap.executeQueryForList("getSelectMDSProductSchemeTO", map);
        returnMap.put("SchemeListTO", schemeList);
        List accHeadList = (List) sqlMap.executeQueryForList("getMDSProductAcctHeadTO", map);
        if (accHeadList != null && accHeadList.size() > 0) {
            returnMap.put("getMDSProductAcctHeadTO", accHeadList);
        }
        List scheduleList = (List) sqlMap.executeQueryForList("getSelectMDSProductInstallmentScheduleTO", map);
        if (scheduleList != null && scheduleList.size() > 0) {
            LinkedHashMap ParMap = new LinkedHashMap();
            System.out.println("@@@scheduleList" + scheduleList);
            for (int i = scheduleList.size(), j = 0; i > 0; i--, j++) {
                String st = CommonUtil.convertObjToStr(((MDSProductInstallmentScheduleTO) scheduleList.get(j)).getInstallmentNo());
                ParMap.put(((MDSProductInstallmentScheduleTO) scheduleList.get(j)).getInstallmentNo(), scheduleList.get(j));
            }
            System.out.println("@@@ParMap" + ParMap);
            returnMap.put("ScheduleListTO", ParMap);
        }
        List noticelist = (List) sqlMap.executeQueryForList("getSelectMDSProductNoticeTO", map);
        if (noticelist != null && noticelist.size() > 0) {
            returnMap.put("MDSProductNoticeTO", noticelist);
        }
        return returnMap;
    }

    public static void main(String str[]) {
        try {
            MDSTypeDAO dao = new MDSTypeDAO();
            HashMap inputMap = new HashMap();

        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public HashMap execute(HashMap map) throws Exception {

        System.out.println("Map in DAO: " + map);
        _branchCode = (String) map.get(CommonConstants.BRANCH_ID);
        CurrDt = ServerUtil.getCurrentDate(_branchCode);
        HashMap returnMap = new HashMap();
        logDAO = new LogDAO();
        logTO = new LogTO();
        if (map.containsKey("SchemeDetailsTOData")) {
            schemeTo = (MDSProductSchemeTO) map.get("SchemeDetailsTOData");
            totMembers = CommonUtil.convertObjToInt(schemeTo.getTotalNoOfMembers());
        }
        if(map.containsKey("SMSParameterTo") && map.get("SMSParameterTo") != null){
        	objSMSParameterTO = (SMSParameterTO) map.get("SMSParameterTo");
        }
        acctHeadTo = (MDSProductAcctHeadTO) map.get("mdsProductAcctHeadTO");
        tableDetails = (LinkedHashMap) map.get("ScheduleTableDetails");
        deletedTableValues = (LinkedHashMap) map.get("deletedFixedAssetsDescription");
        if (map.containsKey("LoanProductChargesTabTO")) {
            loanProductchrgTabTO = (ArrayList) map.get("LoanProductChargesTabTO");
        }
         //Added by sreekrishnan
        if (map.containsKey("CREATE_HEAD")) {
            key = sqlMap.executeUpdate("createAccountHeads", map);
            HashMap resultMap = new HashMap();            
            resultMap.put("RESULT", key);
            return resultMap;
        }
        final String command = CommonUtil.convertObjToStr(map.get("COMMAND"));

        if (command.equals(CommonConstants.TOSTATUS_INSERT)) {
            insertData(map);
        } else if (command.equals(CommonConstants.TOSTATUS_UPDATE)) {
            updateData(map);
        } else if (command.equals(CommonConstants.TOSTATUS_DELETE)) {
            deleteData(map);
        } else if (map.containsKey(CommonConstants.AUTHORIZEMAP)) {
            HashMap authMap = (HashMap) map.get(CommonConstants.AUTHORIZEMAP);
            if (authMap != null) {
                authorize(authMap);
            }
        }
        map = null;
        destroyObjects();
        return returnMap;
    }

    private void insertData(HashMap map) throws Exception {
        try {
            sqlMap.startTransaction();
            sqlMap.executeUpdate("insertMDSProductSchemeTO", schemeTo);
            sqlMap.executeUpdate("insertMDSProductAcctHeadTO", acctHeadTo);
            if (objSMSParameterTO != null) {
                if (objSMSParameterTO.getSmsAlert() != null && CommonUtil.convertObjToStr(objSMSParameterTO.getSmsAlert()).equalsIgnoreCase("Y")) {
                    sqlMap.executeUpdate("insertSMSParameter", objSMSParameterTO);
                }
            }
            insertScheduleTableDetails();
            if (loanProductchrgTabTO != null && loanProductchrgTabTO.size() > 0) {
                insertNoticeCharge();
            }
            logTO.setData(schemeTo.toString());
            logTO.setPrimaryKey(schemeTo.getKeyData());
            logTO.setStatus(schemeTo.getStatus());
            logDAO.addToLog(logTO);
            sqlMap.commitTransaction();
        } catch (Exception e) {
            sqlMap.rollbackTransaction();
            log.error(e);
            throw new TransRollbackException(e);
        }
        map = null;
    }

    private void insertNoticeCharge() throws Exception {
        for (int i = 0; i < loanProductchrgTabTO.size(); i++) {
            try {
                loanProductChargesTabTO = (LoanProductChargesTabTO) loanProductchrgTabTO.get(i);
                loanProductChargesTabTO.setProdId(CommonUtil.convertObjToStr(schemeTo.getSchemeName()));
                sqlMap.executeUpdate("insertMDSNoticeChargeTO", loanProductChargesTabTO);
            } catch (Exception e) {
                sqlMap.rollbackTransaction();
                e.printStackTrace();
                throw new TransRollbackException(e);
            }
        }
    }

    private void insertScheduleTableDetails() throws Exception {

        if (tableDetails != null) {
            ArrayList addList = new ArrayList(tableDetails.keySet());
            for (int i = 0; i < addList.size(); i++) {
                MDSProductInstallmentScheduleTO objScheduleTO = (MDSProductInstallmentScheduleTO) tableDetails.get(addList.get(i));
                objScheduleTO.setStatusDt(getProperDateFormat(objScheduleTO.getStatusDt()));
                sqlMap.executeUpdate("insertMDSProductInstallmentScheduleTO", objScheduleTO);               
                //Added by sreekrishnan for 0010573
                int division = CommonUtil.convertObjToInt(objScheduleTO.getDividion());
                for (int j = 1; j <= division; j++) {
                    objScheduleTO.setDividion(j);
                    objScheduleTO.setCommission(getSchemeCommission(objScheduleTO));
                    setPrizedMoneyDetailsEntryData(objScheduleTO);                
                }
                logTO.setData(objScheduleTO.toString());
                logTO.setPrimaryKey(objScheduleTO.getKeyData());
                logDAO.addToLog(logTO);
                objScheduleTO = null;
            }
        }
    }
        
   public void setPrizedMoneyDetailsEntryData(MDSProductInstallmentScheduleTO objScheduleTO) {        
        final MDSPrizedMoneyDetailsEntryTO objMDSPrizedMoneyDetailsEntryTO = new MDSPrizedMoneyDetailsEntryTO();
        try{
            objMDSPrizedMoneyDetailsEntryTO.setMdsSchemeName(objScheduleTO.getSchemeName());
            objMDSPrizedMoneyDetailsEntryTO.setDrawAuctionDate(getProperDateFormat(objScheduleTO.getInstallmentDt()));
            objMDSPrizedMoneyDetailsEntryTO.setInstallmentNo(objScheduleTO.getInstallmentNo());
            objMDSPrizedMoneyDetailsEntryTO.setDivisionNo(objScheduleTO.getDividion()); //AJITH
            objMDSPrizedMoneyDetailsEntryTO.setDraw("Y");
            objMDSPrizedMoneyDetailsEntryTO.setAuction("");
            objMDSPrizedMoneyDetailsEntryTO.setUserDefined("");
            objMDSPrizedMoneyDetailsEntryTO.setNextInstallmentDate(getProperDateFormat(DateUtil.addDays(objScheduleTO.getInstallmentDt(),30)));
            if(CommonUtil.convertObjToStr(objScheduleTO.getTotalMembers()).equals("0")){
                objScheduleTO.setTotalMembers(CommonUtil.convertObjToInt(totMembers));
            }
            objMDSPrizedMoneyDetailsEntryTO.setNetAmountPayable(CommonUtil.convertObjToDouble(objScheduleTO.getAmount())*objScheduleTO.getTotalMembers()); //AJITH
            objMDSPrizedMoneyDetailsEntryTO.setCommisionAmount(objScheduleTO.getCommission());  //AJITH
            objMDSPrizedMoneyDetailsEntryTO.setTotalBonusAmount(CommonUtil.convertObjToDouble(objScheduleTO.getBonus())*objScheduleTO.getTotalMembers());  //AJITH
            objMDSPrizedMoneyDetailsEntryTO.setPrizedAmount(CommonUtil.convertObjToDouble(objMDSPrizedMoneyDetailsEntryTO.getNetAmountPayable())-CommonUtil.convertObjToDouble(objMDSPrizedMoneyDetailsEntryTO.getCommisionAmount())-CommonUtil.convertObjToDouble(objMDSPrizedMoneyDetailsEntryTO.getTotalBonusAmount()));    //AJITH        
            objMDSPrizedMoneyDetailsEntryTO.setNextBonusAmount(objScheduleTO.getBonus()); 
            objMDSPrizedMoneyDetailsEntryTO.setCommisionAmount(objScheduleTO.getCommission());  //AJITH
            objMDSPrizedMoneyDetailsEntryTO.setTotalDiscount(0.0);      //AJITH Changed from ""           
            objMDSPrizedMoneyDetailsEntryTO.setAppNo(null);     //AJITH Changed from "" 
            objMDSPrizedMoneyDetailsEntryTO.setChittalNo("");
            objMDSPrizedMoneyDetailsEntryTO.setMemberType("");
            objMDSPrizedMoneyDetailsEntryTO.setMemberName("");
            objMDSPrizedMoneyDetailsEntryTO.setInstallmentPaid(null);     //AJITH Changed from "" 
            objMDSPrizedMoneyDetailsEntryTO.setInstallAmountPaid(null);     //AJITH Changed from "" 
            objMDSPrizedMoneyDetailsEntryTO.setInstallmentDue(null);     //AJITH Changed from "" 
            objMDSPrizedMoneyDetailsEntryTO.setInstalOverdueAmt(null);     //AJITH Changed from "" 
            objMDSPrizedMoneyDetailsEntryTO.setSubNo(null);     //AJITH Changed from "" 
            objMDSPrizedMoneyDetailsEntryTO.setStatus(objScheduleTO.getStatus());
            objMDSPrizedMoneyDetailsEntryTO.setStatusBy(objScheduleTO.getStatusBy());
            objMDSPrizedMoneyDetailsEntryTO.setStatusDt(getProperDateFormat(objScheduleTO.getStatusDt()));
            objMDSPrizedMoneyDetailsEntryTO.setPaymentDate(null);
            objMDSPrizedMoneyDetailsEntryTO.setOldChittalNo("");
            objMDSPrizedMoneyDetailsEntryTO.setOldSubNo(null);
            objMDSPrizedMoneyDetailsEntryTO.setBranchId(_branchCode);
            objMDSPrizedMoneyDetailsEntryTO.setAuctionTrans("N");
            objMDSPrizedMoneyDetailsEntryTO.setSlNo(CommonUtil.convertObjToDouble(objScheduleTO.getInstallmentNo()));
            sqlMap.executeUpdate("insertMDSPrizedMoneyDetailsEntryTO", objMDSPrizedMoneyDetailsEntryTO);
        }catch(Exception e){
            log.info("Error In setMDSPrizedMoneyDetailsEntryTOData()");
            e.printStackTrace();
        }
    }
    
      public void setUpdatePrizedMoneyDetailsEntryData(MDSProductInstallmentScheduleTO objScheduleTO) {        
        final MDSPrizedMoneyDetailsEntryTO objMDSPrizedMoneyDetailsEntryTO = new MDSPrizedMoneyDetailsEntryTO();
        try{
            objMDSPrizedMoneyDetailsEntryTO.setMdsSchemeName(objScheduleTO.getSchemeName());
            objMDSPrizedMoneyDetailsEntryTO.setDrawAuctionDate(getProperDateFormat(objScheduleTO.getInstallmentDt()));
            objMDSPrizedMoneyDetailsEntryTO.setInstallmentNo(objScheduleTO.getInstallmentNo()); 
            objMDSPrizedMoneyDetailsEntryTO.setDivisionNo(objScheduleTO.getDividion());  //AJITH
            objMDSPrizedMoneyDetailsEntryTO.setDraw("Y");
            objMDSPrizedMoneyDetailsEntryTO.setAuction("");
            objMDSPrizedMoneyDetailsEntryTO.setUserDefined("");
            objMDSPrizedMoneyDetailsEntryTO.setNextInstallmentDate((DateUtil.addDays(objScheduleTO.getInstallmentDt(),30)));
            objMDSPrizedMoneyDetailsEntryTO.setNetAmountPayable(CommonUtil.convertObjToDouble(objScheduleTO.getAmount())*objScheduleTO.getTotalMembers());   //AJITH
            objMDSPrizedMoneyDetailsEntryTO.setCommisionAmount(objScheduleTO.getCommission()); //AJITH
            objMDSPrizedMoneyDetailsEntryTO.setTotalBonusAmount(CommonUtil.convertObjToDouble(objScheduleTO.getBonus())*objScheduleTO.getTotalMembers());  //AJITH
            objMDSPrizedMoneyDetailsEntryTO.setPrizedAmount(CommonUtil.convertObjToDouble(objMDSPrizedMoneyDetailsEntryTO.getNetAmountPayable())-CommonUtil.convertObjToDouble(objMDSPrizedMoneyDetailsEntryTO.getCommisionAmount())-CommonUtil.convertObjToDouble(objMDSPrizedMoneyDetailsEntryTO.getTotalBonusAmount())); //AJITH            
            objMDSPrizedMoneyDetailsEntryTO.setNextBonusAmount(objScheduleTO.getBonus()); 
            objMDSPrizedMoneyDetailsEntryTO.setCommisionAmount(objScheduleTO.getCommission());  //AJITH
            objMDSPrizedMoneyDetailsEntryTO.setTotalDiscount(0.0); //AJITH            
            objMDSPrizedMoneyDetailsEntryTO.setAppNo(null); //AJITH
            objMDSPrizedMoneyDetailsEntryTO.setChittalNo("");
            objMDSPrizedMoneyDetailsEntryTO.setMemberType("");
            objMDSPrizedMoneyDetailsEntryTO.setMemberName("");
            objMDSPrizedMoneyDetailsEntryTO.setInstallmentPaid(null); //AJITH
            objMDSPrizedMoneyDetailsEntryTO.setInstallAmountPaid(null); //AJITH
            objMDSPrizedMoneyDetailsEntryTO.setInstallmentDue(null); //AJITH
            objMDSPrizedMoneyDetailsEntryTO.setInstalOverdueAmt(null); //AJITH
            objMDSPrizedMoneyDetailsEntryTO.setSubNo(null); //AJITH
            objMDSPrizedMoneyDetailsEntryTO.setStatus(objScheduleTO.getStatus());
            objMDSPrizedMoneyDetailsEntryTO.setStatusBy(objScheduleTO.getStatusBy());
            objMDSPrizedMoneyDetailsEntryTO.setStatusDt(getProperDateFormat(objScheduleTO.getStatusDt()));
            objMDSPrizedMoneyDetailsEntryTO.setPaymentDate(null);
            objMDSPrizedMoneyDetailsEntryTO.setOldChittalNo("");
            objMDSPrizedMoneyDetailsEntryTO.setOldSubNo(null); //AJITH
            objMDSPrizedMoneyDetailsEntryTO.setBranchId(_branchCode);
            objMDSPrizedMoneyDetailsEntryTO.setAuctionTrans("N");
            objMDSPrizedMoneyDetailsEntryTO.setSlNo(CommonUtil.convertObjToDouble(objScheduleTO.getInstallmentNo()));
            objMDSPrizedMoneyDetailsEntryTO.setPredefinedInstall("Y");
            sqlMap.executeUpdate("updatePridefinedMDSPrizedMoneyDetailsEntryTO", objMDSPrizedMoneyDetailsEntryTO);
        }catch(Exception e){
            log.info("Error In setMDSPrizedMoneyDetailsEntryTOData()");
            e.printStackTrace();
        }
    }
      
    private void updateData(HashMap map) throws Exception {
        try {
            List smsParamList = null;
            sqlMap.startTransaction();
            HashMap whereMap = new HashMap();
            sqlMap.executeUpdate("updateMDSProductSchemeTO", schemeTo);
            //Added by sreekrishnan for updating changed scheme dates
            HashMap updateMap = new HashMap();
            updateMap.put("START_DT", schemeTo.getSchemeStartDt());
            updateMap.put("END_DT", schemeTo.getSchemeEndDt());
            updateMap.put("SCHEME_NAME", schemeTo.getSchemeName());
            sqlMap.executeUpdate("updateApplicationSchemeStartDt", updateMap);
            sqlMap.executeUpdate("updateMasterMaintenanceSchemeStartDt", updateMap);
            //End
            sqlMap.executeUpdate("updateMDSProductAcctHeadTO", acctHeadTo);
            if (objSMSParameterTO != null) {
                whereMap.put("PROD_TYPE", CommonUtil.convertObjToStr(CommonConstants.MDS_TRANSMODE_TYPE));
                whereMap.put("PROD_ID", CommonUtil.convertObjToStr(objSMSParameterTO.getProdId()));
                if (objSMSParameterTO.getSmsAlert() != null && CommonUtil.convertObjToStr(objSMSParameterTO.getSmsAlert()).equalsIgnoreCase("Y")) {
                    smsParamList = (List) sqlMap.executeQueryForList("getSelectSMSParameter", whereMap);
                    if (smsParamList.isEmpty()) {
                        sqlMap.executeUpdate("insertSMSParameter", objSMSParameterTO);
                    } else {
                        sqlMap.executeUpdate("updateSMSParameter", objSMSParameterTO);
                    }
                } else {
                    if (smsParamList != null && !smsParamList.isEmpty()) {
                        sqlMap.executeUpdate("deleteSMSParameterWithMDSType", objSMSParameterTO);
                    }
                }
            }
            if(updateShedule()){
            updateScheduleTableDetails(map);
            }
            updateNoticeCharge();
            logTO.setData(schemeTo.toString());
            logTO.setPrimaryKey(schemeTo.getKeyData());
            logTO.setStatus(schemeTo.getStatus());
            logDAO.addToLog(logTO);
            sqlMap.commitTransaction();
        } catch (Exception e) {
            sqlMap.rollbackTransaction();
            log.error(e);
            throw new TransRollbackException(e);
        }
        map = null;
    }

    
    private boolean updateShedule() throws SQLException{
        boolean updateFlag = false;
        int chitCnt = 0;
        HashMap whereMap = new HashMap();
        whereMap.put("SCHEME_NAME",schemeTo.getSchemeName());
        List chitList = (List) sqlMap.executeQueryForList("getPredefinedChittalcount", whereMap);
        if(chitList != null && chitList.size() > 0){
            whereMap = (HashMap)chitList.get(0);
            if(whereMap.containsKey("CHIT_CNT") && whereMap.get("CHIT_CNT") != null){
                chitCnt = CommonUtil.convertObjToInt(whereMap.get("CHIT_CNT"));
            }
        }
        if(chitCnt == 0){
            updateFlag = true;
        }
        return updateFlag;
    }
    

    private void updateNoticeCharge() throws Exception {
        if (loanProductchrgTabTO != null && loanProductchrgTabTO.size() > 0) {
            if (loanProductchrgTabTO.size() > 0) {
                loanProductChargesTabTO = (LoanProductChargesTabTO) loanProductchrgTabTO.get(0);
                loanProductChargesTabTO.setProdId(CommonUtil.convertObjToStr(schemeTo.getSchemeName()));
                sqlMap.executeUpdate("deleteMDSNoticeChargesTO", loanProductChargesTabTO);
            }
            for (int i = 0; i < loanProductchrgTabTO.size(); i++) {
                try {
                    loanProductChargesTabTO = (LoanProductChargesTabTO) loanProductchrgTabTO.get(i);
                    loanProductChargesTabTO.setProdId(CommonUtil.convertObjToStr(schemeTo.getSchemeName()));
                    sqlMap.executeUpdate("insertMDSNoticeChargeTO", loanProductChargesTabTO);
                } catch (Exception e) {
                    sqlMap.rollbackTransaction();
                    e.printStackTrace();
                    throw new TransRollbackException(e);
                }
            }
        }
    }

    private void updateScheduleTableDetails(HashMap updateMap) throws Exception {
        if (tableDetails != null) {
            System.out.println("######## tableDetails :" + tableDetails);
            ArrayList addList = new ArrayList(tableDetails.keySet());
            MDSProductInstallmentScheduleTO objScheduleTO = null;
            //Delete first entries and insert new....
            sqlMap.executeUpdate("deletePridefinedMDSPrizedMoneyDetailsEntryTO", updateMap);
            for (int i = 0; i < tableDetails.size(); i++) {
                objScheduleTO = new MDSProductInstallmentScheduleTO();
                objScheduleTO = (MDSProductInstallmentScheduleTO) tableDetails.get(addList.get(i));
                objScheduleTO.setStatusDt((Date)CurrDt.clone());
                System.out.println("objScheduleTO @$@$@#$@#$@#" + objScheduleTO);
                if (objScheduleTO.getStatus().equals(CommonConstants.STATUS_CREATED)) {
                    sqlMap.executeUpdate("insertMDSProductInstallmentScheduleTO", objScheduleTO);                       
                } else {
                    sqlMap.executeUpdate("updateMDSProductInstallmentScheduleTO", objScheduleTO);                    
                }                
                int division = CommonUtil.convertObjToInt(updateMap.get("SCHEME_DIVISION"));
                for (int j = 1; j <= division; j++) {
                    objScheduleTO.setDividion(j);  
                    objScheduleTO.setCommission(getSchemeCommission(objScheduleTO));
                    setPrizedMoneyDetailsEntryData(objScheduleTO);                
                }
            }
        }
        if (deletedTableValues != null) {
            System.out.println("######## deletedTableValues :" + deletedTableValues);
            ArrayList addList = new ArrayList(deletedTableValues.keySet());
            MDSProductInstallmentScheduleTO objScheduleTO = null;
            for (int i = 0; i < deletedTableValues.size(); i++) {
                objScheduleTO = new MDSProductInstallmentScheduleTO();
                objScheduleTO = (MDSProductInstallmentScheduleTO) deletedTableValues.get(addList.get(i));
                System.out.println("objScheduleTO" + objScheduleTO);
                sqlMap.executeUpdate("deleteMDSProductScheduleTO", objScheduleTO);
            }
        }
    }

    private void deleteData(HashMap map) throws Exception {
        try {
            sqlMap.startTransaction();
            sqlMap.executeUpdate("deleteMDSProductSchemeTO", schemeTo);
            sqlMap.executeUpdate("deleteMDSProductInstallmentScheduleTO", schemeTo);
            if (loanProductchrgTabTO != null && loanProductchrgTabTO.size() > 0) {
                loanProductChargesTabTO = (LoanProductChargesTabTO) loanProductchrgTabTO.get(0);
                loanProductChargesTabTO.setProdId(CommonUtil.convertObjToStr(schemeTo.getSchemeName()));
                sqlMap.executeUpdate("deleteMDSNoticeChargesTO", loanProductChargesTabTO);
            }
	    	if (objSMSParameterTO.getSmsAlert() != null && CommonUtil.convertObjToStr(objSMSParameterTO.getSmsAlert()).equalsIgnoreCase("Y")) {
                sqlMap.executeUpdate("deleteSMSParameterWithMDSType", objSMSParameterTO);   
            }
            logTO.setData(schemeTo.toString());
            logTO.setPrimaryKey(schemeTo.getKeyData());
            logTO.setStatus(schemeTo.getStatus());
            logDAO.addToLog(logTO);
            sqlMap.commitTransaction();
        } catch (Exception e) {
            sqlMap.rollbackTransaction();
            log.error(e);
            throw new TransRollbackException(e);
        }
        map = null;
    }
    private void authorozeSMSParameter(HashMap authMap) {
        objSMSParameterTO = new SMSParameterTO();
        try {
            if (authMap.containsKey("STATUS") && authMap.get("STATUS") != null && CommonUtil.convertObjToStr(authMap.get("STATUS")).equalsIgnoreCase("AUTHORIZED")
				&& (authMap.containsKey("PROD_ID") && authMap.get("PROD_ID") != null)
                && (authMap.containsKey("SCHEME_NAME") && authMap.get("SCHEME_NAME") != null)) {
                objSMSParameterTO.setAuthorizeStatus(CommonUtil.convertObjToStr(authMap.get("STATUS")));
                objSMSParameterTO.setAuthorizedDt((Date) authMap.get("AUTHORIZED_DT"));
                objSMSParameterTO.setAuthorizedBy(CommonUtil.convertObjToStr(authMap.get("AUTHORIZED_BY")));
                objSMSParameterTO.setProdId(CommonUtil.convertObjToStr(authMap.get("SCHEME_NAME")));
                objSMSParameterTO.setProdType(CommonConstants.MDS_TRANSMODE_TYPE);
                sqlMap.executeUpdate("authorizeSMSParameter", objSMSParameterTO);
            }else if(authMap.containsKey("STATUS") && authMap.get("STATUS") != null && CommonUtil.convertObjToStr(authMap.get("STATUS")).equalsIgnoreCase("REJECTED")){
                sqlMap.executeUpdate("deleteSMSParameterWithMDSType", objSMSParameterTO);   
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void authorize(HashMap map) throws Exception {
        ArrayList selectedList = (ArrayList) map.get(CommonConstants.AUTHORIZEDATA);
        HashMap AuthMap = new HashMap();
        AuthMap = (HashMap) selectedList.get(0);
        System.out.println("@@@@@@@AuthMap" + AuthMap);
        try {
            sqlMap.startTransaction();
            logTO.setData(map.toString());
            authorozeSMSParameter(AuthMap);
            sqlMap.executeUpdate("authorizeMDSSchemeDetails", AuthMap);
            sqlMap.executeUpdate("authorizeMDSScheduleDetails", AuthMap);
            //Added by sreekrishnan for 0010573
            sqlMap.executeUpdate("authorizePrizedMoneyDetails", AuthMap);           
            sqlMap.commitTransaction();
        } catch (Exception e) {
            sqlMap.rollbackTransaction();
            e.printStackTrace();
            throw new TransRollbackException(e);
        }
    }
    
    public Date getProperDateFormat(Object obj) {
        Date curDt = null;
        if (obj != null && obj.toString().length() > 0) {
            Date tempDt = DateUtil.getDateMMDDYYYY(CommonUtil.convertObjToStr(obj));
            curDt = (Date) CurrDt.clone();
            curDt.setDate(tempDt.getDate());
            curDt.setMonth(tempDt.getMonth());
            curDt.setYear(tempDt.getYear());
        }
        return curDt;
    }

    public HashMap executeQuery(HashMap obj) throws Exception {
        _branchCode = (String) obj.get(CommonConstants.BRANCH_ID);
        CurrDt = ServerUtil.getCurrentDate(_branchCode);
        return getData(obj);
    }

    private void destroyObjects() {
        schemeTo = null;
        acctHeadTo = null;
        objScheduleTO = null;
        loanProductChargesTabTO = null;
        objSMSParameterTO = null;
    }
    
    public double getSchemeCommission(MDSProductInstallmentScheduleTO objScheduleTO) {        
        double commission = 0.0;
        HashMap commissionMap = new HashMap();
        try{
        if(CommonUtil.convertObjToStr(objScheduleTO.getTotalMembers()).equals("0")){
            objScheduleTO.setTotalMembers(totMembers);
        }
        commissionMap.put("TOTAL_AMOUNT", (CommonUtil.convertObjToDouble(objScheduleTO.getAmount())*CommonUtil.convertObjToDouble(objScheduleTO.getTotalMembers())));   
        commissionMap.put("PROD_ID", CommonUtil.convertObjToStr(objScheduleTO.getProdId()));        
        List commissionlst = sqlMap.executeQueryForList("getSchemeCommission", commissionMap);
        if (commissionlst != null && commissionlst.size() > 0) {
            commissionMap = (HashMap) commissionlst.get(0);
            if (CommonUtil.convertObjToDouble(commissionMap.get("COMMISSION")) > 0) {
                commission = CommonUtil.convertObjToDouble(commissionMap.get("COMMISSION"));
                System.out.println("commission#$#$11^"+commission);
            }
        }
        }catch(Exception e){
            e.printStackTrace();
        }
        return commission;
    } 
        
}
