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
package com.see.truetransact.serverside.mdsapplication.mdsmastermaintenance;

import java.util.Set;
import java.util.List;
import java.util.Date;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import org.apache.log4j.Logger;
import com.ibatis.db.sqlmap.SqlMap;
import com.see.truetransact.clientutil.ClientUtil;
import com.see.truetransact.serverside.TTDAO;
import com.see.truetransact.serverutil.ServerUtil;
import com.see.truetransact.commonutil.CommonUtil;
import com.see.truetransact.commonutil.CommonConstants;
import com.see.truetransact.commonutil.DateUtil;
import com.see.truetransact.servicelocator.ServiceLocator;
import com.see.truetransact.serverexception.TransRollbackException;
import com.see.truetransact.transferobject.product.operativeacct.*;
import com.see.truetransact.serverexception.ServiceLocatorException;
import com.see.truetransact.serverside.common.transaction.TransferTrans;
import com.see.truetransact.serverside.transaction.transfer.TransferDAO;
import com.see.truetransact.transferobject.termloan.TermLoanCaseDetailTO;
import com.see.truetransact.serverside.common.transaction.TransactionDAO;
import com.see.truetransact.transferobject.common.transaction.TransactionTO;
import com.see.truetransact.transferobject.transaction.transfer.TxTransferTO;
import com.see.truetransact.serverside.transaction.common.TransactionFactory;
import com.see.truetransact.transferobject.termloan.chargesTo.TermLoanChargesTO;
import com.see.truetransact.transferobject.mdsapplication.mdsmastermaintenance.MDSMemberTypeTO;
import com.see.truetransact.transferobject.mdsapplication.mdsmastermaintenance.MDSDepositTypeTO;
import com.see.truetransact.transferobject.mdsapplication.mdsmastermaintenance.MDSSocietyTypeTO;
import com.see.truetransact.transferobject.mdsapplication.mdsmastermaintenance.MDSMasterMaintenanceTO;
import com.see.truetransact.transferobject.mdsapplication.mdsmastermaintenance.MDSMasterSecurityLandTO;
import com.see.truetransact.serverside.common.nominee.NomineeDAO;
import com.see.truetransact.transferobject.deposit.lien.DepositLienTO;
import com.see.truetransact.serverside.deposit.lien.DepositLienDAO;
// For Maintaining Logs...
import com.see.truetransact.serverside.common.log.LogDAO;
import com.see.truetransact.serverside.termloan.loanapplicationregister.LoanApplicationDAO;
import com.see.truetransact.serverside.transaction.cash.CashTransactionDAO;
import com.see.truetransact.transferobject.common.log.LogTO;
import com.see.truetransact.transferobject.termloan.GoldLoanSecurityTO;
import com.see.truetransact.transferobject.termloan.LoansSecurityGoldStockTO;
import com.see.truetransact.transferobject.transaction.cash.CashTransactionTO;
import java.sql.SQLException;
import java.util.logging.Level;

/**
 * This is used for OperativeAcctProductDAO Data Access.
 *
 * @author Balachandar
 *
 * @modified Pinky @modified Rahul
 */
public class MDSMasterMaintenanceDAO extends TTDAO {

    private final static Logger log = Logger.getLogger(MDSMasterMaintenanceDAO.class);
    private LinkedHashMap deletedDepositTableValues = null;
    private LinkedHashMap deletedSocietyTableValues = null;
    private LinkedHashMap deletedMemberTableValues = null;
    private LinkedHashMap depositTableDetails = null;
    private LinkedHashMap societyTableDetails = null;
    private LinkedHashMap memberTableDetails = null;
    private MDSMasterSecurityLandTO objMDSMasterSecurityLandTO;
    private TransactionDAO transactionDAO = null;
    TransferDAO transferDAO = new TransferDAO();
    private MDSMasterMaintenanceTO MaintenanceTO;
    private TermLoanCaseDetailTO objCaseDetailTO;
    private MDSDepositTypeTO objDepositTypeTO;
    private MDSSocietyTypeTO objMDSSocietyTypeTO;
    private MDSMemberTypeTO objMemberTypeTO;
    private static SqlMap sqlMap = null;
    private HashMap caseDetailMap;
    private Date CurrDt = null;
    private LogDAO logDAO;
    private LogTO logTO;
    private LinkedHashMap deletedCollateralTableValues = null;
    private LinkedHashMap collateralTableDetails = null;
    NomineeDAO objNomineeDAO = new NomineeDAO();
    final String SCREEN = "TD";
    private List SalarySecDetails = new ArrayList();
    HashMap transDetailMap = null;
    private Date currDt = null;//trans details
    HashMap returnMap;//trans details
//    private LogDAO logDAO;
//    private LogTO logTO;
    public List chargeLst = null; //charge details
    private String loanType = "";//charge details
    private String appNo = "";//charge details
    private final String PROD_ID = "PROD_ID";//charge details
    private final String LIMIT = "LIMIT";//charge details
    private final String BEHAVES_LIKE = "BEHAVES_LIKE";//charge details
    private final String INT_GET_FROM = "INT_GET_FROM";//charge details
    private final String SECURITY_DETAILS = "SECURITY_DETAILS";//charge details
    //  private final static Logger log = Logger.getLogger(LoanApplicationDAO.class);
//    private TransactionDAO transactionDAO = null ; //trans details
    private MDSMasterMaintenanceTO objTO;
    private LoansSecurityGoldStockTO objCustomerGoldStockSecurityTO; // Added by nithya on 07-03-2020 for KD-1379

    /**
     * Creates a new instance of OperativeAcctProductDAO
     */
    public MDSMasterMaintenanceDAO() throws ServiceLocatorException {
        ServiceLocator locate = ServiceLocator.getInstance();
        sqlMap = (SqlMap) locate.getDAOSqlMap();
    }

    private HashMap getData(HashMap map) throws Exception {
        System.out.println("@@@@@@@map" + map);
        HashMap returnMap = new HashMap();
        _branchCode = (String) map.get(CommonConstants.BRANCH_ID);
        CurrDt = ServerUtil.getCurrentDate(_branchCode);
        int subNo = CommonUtil.convertObjToInt(map.get("SUB_NO"));
        System.out.println("@@@@@@@ Sub No" + subNo);
        map.put("SUB_NO", subNo);   //AJITH
        List masterList = (List) sqlMap.executeQueryForList("getSelectMDSMasterMaintenanceTO", map);
        List memberList = (List) sqlMap.executeQueryForList("getSelectMDSMemberTypeTO", map);
        List depositList = (List) sqlMap.executeQueryForList("getSelectMDSDepositTypeTO", map);
        map.put("SUB_NO", CommonUtil.convertObjToInt(subNo));
        List societyList = (List) sqlMap.executeQueryForList("getSelectMDSSocietyTypeTO", map);
        String where = CommonUtil.convertObjToStr(map.get("CHITTAL_NO")) + "_" + subNo;
        List caseList = (List) sqlMap.executeQueryForList("getSelectTermLoanCaseDetailTO", where);
        returnMap.put("TermLoanCaseDetailTO", caseList);
        SalarySecDetails = sqlMap.executeQueryForList("getMDSSalSecDts", map);
        returnMap.put("SalarySecDetails", SalarySecDetails);
        if (memberList != null && memberList.size() > 0) {
            LinkedHashMap ParMap = new LinkedHashMap();
            System.out.println("@@@memberList" + memberList);
            for (int i = memberList.size(), j = 0; i > 0; i--, j++) {
                String st = ((MDSMemberTypeTO) memberList.get(j)).getMemberNo();
                ParMap.put(((MDSMemberTypeTO) memberList.get(j)).getMemberNo(), memberList.get(j));
            }
            System.out.println("@@@ParMap" + ParMap);
            returnMap.put("memberListTO", ParMap);
        }
        if (depositList != null && depositList.size() > 0) {
            LinkedHashMap ParMap = new LinkedHashMap();
            System.out.println("@@@depositList" + depositList);
            for (int i = depositList.size(), j = 0; i > 0; i--, j++) {
                String st = ((MDSDepositTypeTO) depositList.get(j)).getDepositNo();
                ParMap.put(((MDSDepositTypeTO) depositList.get(j)).getDepositNo(), depositList.get(j));
            }
            System.out.println("@@@ParMap" + ParMap);
            returnMap.put("depositListTO", ParMap);
        }
        if (societyList != null && societyList.size() > 0) {
            LinkedHashMap ParMap = new LinkedHashMap();
            System.out.println("@@@societyList" + societyList);
            for (int i = societyList.size(), j = 0; i > 0; i--, j++) {
                String st = ((MDSSocietyTypeTO) societyList.get(j)).getSecurityNo();
                ParMap.put(((MDSSocietyTypeTO) societyList.get(j)).getSecurityNo(), societyList.get(j));
            }
            System.out.println("@@@ParMap" + ParMap);
            returnMap.put("societyListTO", ParMap);
        }
        List collateralList = (List) sqlMap.executeQueryForList("getSelectMDSSecurityLandTO", map);
        if (collateralList != null && collateralList.size() > 0) {
            LinkedHashMap ParMap = new LinkedHashMap();
            System.out.println("@@@collateralList" + collateralList);
            for (int i = collateralList.size(), j = 0; i > 0; i--, j++) {
                String st = ((MDSMasterSecurityLandTO) collateralList.get(j)).getMemberNo();
                //ParMap.put(((MDSMasterSecurityLandTO) collateralList.get(j)).getMemberNo(), collateralList.get(j));
                ParMap.put(((MDSMasterSecurityLandTO) collateralList.get(j)).getMemberNo() + "_" + (j + 1), collateralList.get(j));
            }
            returnMap.put("CollateralListTO", ParMap);
        }
        collateralList = null;

        returnMap.put("MasterMaintenanceListTO", masterList);
        
        // Added by nithya on 07-03-2020 for KD-1379
        HashMap stockMap = new HashMap();
        stockMap.put("value",map.get("CHITTAL_NO"));
        List goldSlockList = (List) sqlMap.executeQueryForList("getSelectLoanSecurityGoldStockTO", stockMap);
        if (goldSlockList != null && goldSlockList.size() > 0) {
            LoansSecurityGoldStockTO objLoansSecurityGoldStockTO = (LoansSecurityGoldStockTO)goldSlockList.get(0);
            returnMap.put("CustomerGoldStockSecurityTO", objLoansSecurityGoldStockTO); 
        }
        //End

        objNomineeDAO = new NomineeDAO();
        map.put("DEPOSIT_NO", map.get("CHITTAL_NO") + "_" + String.valueOf(subNo));
        List list = (List) sqlMap.executeQueryForList("getSelectRenewNomineeTOTD", map);
        if (list != null && list.size() > 0) {
            returnMap.put("AccountNomineeList", list);
        }

        if(masterList != null && masterList.size() >0 ){
        	MaintenanceTO = (MDSMasterMaintenanceTO) masterList.get(0);
        }
//        if(map.containsKey("APPLICATION_NO") && map.get("APPLICATION_NO")!=null)
        if (MaintenanceTO.getApplNo() != null && !MaintenanceTO.getApplNo().equals("")) {
            HashMap whereM = new HashMap();//;(HashMap)map.get(CommonConstants.MAP_WHERE); //trans details
            String application_no = MaintenanceTO.getChittalNo() + "_" + MaintenanceTO.getSubNo() + "_" + MaintenanceTO.getApplNo();
            System.out.println("application_no..." + application_no);
            whereM.put("APPLICATION_NO", application_no);
            //trans details
            if (whereM.containsKey("APPLICATION_NO")) {
                HashMap getRemitTransMap = new HashMap();
                getRemitTransMap.put("TRANS_ID", whereM.get("APPLICATION_NO"));
                getRemitTransMap.put("TRANS_DT", CurrDt.clone());
                getRemitTransMap.put("BRANCH_CODE", map.get("BRANCH_CODE").toString());
                System.out.println("@#%$#@%#$%getRemitTransMap:" + getRemitTransMap);
                list = sqlMap.executeQueryForList("getSelectRemittanceIssueTransactionTODate1", getRemitTransMap);
//            list = sqlMap.executeQueryForList("getSelectRemittanceIssueTransactionTODate", where.get("REP_INT_CLS_NO"));
                if (list != null && list.size() > 0) {
                    returnMap.put("TRANSACTION_LIST", list);
                }
            }

        }
        return returnMap;
    }

    public HashMap execute(HashMap map) throws Exception {

        System.out.println("Map in DAO: " + map);
        _branchCode = (String) map.get(CommonConstants.BRANCH_ID);
        final String command = CommonUtil.convertObjToStr(map.get("COMMAND"));
        CurrDt = ServerUtil.getCurrentDate(_branchCode);
        returnMap = new HashMap();
        logDAO = new LogDAO();
        logTO = new LogTO();
        MaintenanceTO = (MDSMasterMaintenanceTO) map.get("MasterMaintenanceDetailsTOData");
        objTO = (MDSMasterMaintenanceTO) map.get("MasterMaintenanceDetailsTOData");
        memberTableDetails = (LinkedHashMap) map.get("MemberTableDetails");
        depositTableDetails = (LinkedHashMap) map.get("DepositTableDetails");
        societyTableDetails = (LinkedHashMap) map.get("SocietyTableDetails");
        deletedMemberTableValues = (LinkedHashMap) map.get("deletedMemberTypeData");
        deletedDepositTableValues = (LinkedHashMap) map.get("deletedDepositTypeData");
        deletedSocietyTableValues = (LinkedHashMap) map.get("deletedSocietyTypeData");
        caseDetailMap = (HashMap) map.get("TermLoanCaseDetailsTO");
        if (map.containsKey("Charge List Data")) { //charge details
            //chargeLst = (List) map.get("Charge List Data");
            System.out.println("@##$#$% chargeLst #### 887478444515:" + chargeLst);
        }
        transDetailMap = new HashMap();//charge details
        if (map.containsKey("Transaction Details Data")) {
            transDetailMap = (HashMap) map.get("Transaction Details Data");
            // System.out.println("@##$#$% transDetailMap #### 34445555555555555556554:=" + transDetailMap);
            if (map.containsKey("Charge List Data")) {
                //chargeLst = (List) map.get("Charge List Data");
                System.out.println("@##$#$% chargeLst #### :3541654513" + chargeLst);
            }
        }
        if (map.containsKey("SalarySecDetails")) {
            SalarySecDetails = (List) map.get("SalarySecDetails");

        } else {
            SalarySecDetails = new ArrayList();
        }
        if (map.containsKey("CollateralTableDetails") || map.containsKey("deletedCollateralTypeData")) {
            collateralTableDetails = (LinkedHashMap) map.get("CollateralTableDetails");
            deletedCollateralTableValues = (LinkedHashMap) map.get("deletedCollateralTypeData");
        }
        if (map.containsKey("CUST_GOLD_SECURITY_STOCK") && map.get("CUST_GOLD_SECURITY_STOCK") != null && map.get("CUST_GOLD_SECURITY_STOCK").equals("CUST_GOLD_SECURITY_STOCK")) {
            if (map.containsKey(("CustomerGoldStockSecurityTO")) && map.get("CustomerGoldStockSecurityTO") != null) {
                objCustomerGoldStockSecurityTO = (LoansSecurityGoldStockTO) map.get("CustomerGoldStockSecurityTO");
            }
        }
        LogDAO objLogDAO = new LogDAO();
        HashMap execReturnMap = new HashMap();
        // Log Transfer Object
        LogTO objLogTO = new LogTO();
        objLogTO.setUserId((String) map.get(CommonConstants.USER_ID));
        objLogTO.setBranchId((String) map.get(CommonConstants.BRANCH_ID));
        objLogTO.setIpAddr((String) map.get(CommonConstants.IP_ADDR));
        objLogTO.setModule((String) map.get(CommonConstants.MODULE));
        objLogTO.setScreen((String) map.get(CommonConstants.SCREEN));

        if (command.equals(CommonConstants.TOSTATUS_INSERT)) {
//            insertData(map, objLogDAO, objLogTO);
        } else if (command.equals(CommonConstants.TOSTATUS_UPDATE)) {
            String onlyApplication = MaintenanceTO.getOnlyApplication();
            System.out.println("MaintenanceTO======" + MaintenanceTO + "onlyApplication==" + onlyApplication);
            if (onlyApplication.equals("Y")) {
                String sts = objTO.getCommand();
                String applN = objTO.getApplNo();
                System.out.println("applN.." + applN);
                objTO.setCommand(CommonConstants.TOSTATUS_INSERT);

                doLoanApplTransactions(map);
                System.out.println("sts.." + sts);
                objTO.setCommand(sts);
                System.out.println("objTO.getApplNo()" + objTO.getApplNo());
                objTO.setApplNo(applN);
                returnMap.put("APPLICATION_NO", objTO.getApplNo());

                HashMap bondMap = new HashMap();
                System.out.println("################## Execute Method MaintenanceTO : " + MaintenanceTO);
                bondMap.put("SCHEME", MaintenanceTO.getSchemeName());
                List LastBondNOList = sqlMap.executeQueryForList("getLastApplicationValue", bondMap);
                int LastBonNo = CommonUtil.convertObjToInt(((HashMap) LastBondNOList.get(0)).get("LAST_APPLICATION_NO").toString());
                int currentbondNO = CommonUtil.convertObjToInt(objTO.getApplNo());
                if (LastBonNo < currentbondNO) {
                    bondMap.put("LASTAPPLICATIONNO", currentbondNO);
                    sqlMap.executeUpdate("updateSchemeApplicationNO", bondMap);
                    //for trans
                    objTO.setCommand(CommonConstants.TOSTATUS_INSERT);
                    returnMap.put("APPLICATION_NO", objTO.getApplNo());
                    MaintenanceTO.setApplicationSet("Y");
                    MaintenanceTO.setApplNo(objTO.getApplNo());
                    //executeTransactionPart(map, transDetailMap,chargeLst);//trans charge details

                }
            }
            updateData(map, objLogDAO, objLogTO);
        } else if (command.equals(CommonConstants.TOSTATUS_DELETE)) {
            deleteData(map);
        } else if (map.containsKey(CommonConstants.AUTHORIZEMAP)) {
            HashMap authMap = (HashMap) map.get(CommonConstants.AUTHORIZEMAP);
            if (authMap != null) {
                authorize(authMap,map);
            }
        }
        map = null;
        destroyObjects();
        System.out.println("returnMap===" + returnMap);
        return returnMap;
    }

    private void executeCaseTabQuery(HashMap map) throws Exception {
        TermLoanCaseDetailTO objCaseDetailTO;
        try {
            if (caseDetailMap != null && caseDetailMap.size() > 0) {
                Set keySet;
                Object[] objKeySet;
                keySet = caseDetailMap.keySet();
                objKeySet = (Object[]) keySet.toArray();
                HashMap updateMap = new HashMap();
                // To retrieve the TermLoanCaseDetailTO from the caseDetailMap
                for (int i = caseDetailMap.size() - 1, j = 0; i >= 0; --i, ++j) {
                    objCaseDetailTO = (TermLoanCaseDetailTO) caseDetailMap.get(objKeySet[j]);
                    if (CommonUtil.convertObjToStr(objCaseDetailTO.getActNum()).length() < 1) {
                        objCaseDetailTO.setActNum(MaintenanceTO.getChittalNo() + "_" + MaintenanceTO.getSubNo());
                    }
                    if (map.get("COMMAND").equals(CommonConstants.TOSTATUS_DELETE)) {
                        objCaseDetailTO.setCommand(CommonUtil.convertObjToStr(map.get("COMMAND")));
                    }
                    logTO.setData(objCaseDetailTO.toString());
                    logTO.setPrimaryKey(objCaseDetailTO.getKeyData());
                    logTO.setStatus(objCaseDetailTO.getCommand());
                    System.out.println("$#%$%&%&objCaseDetailTO" + objCaseDetailTO);
                    executeOneTabQueries("TermLoanCaseDetailTO", objCaseDetailTO);
                    logDAO.addToLog(logTO);
                    objCaseDetailTO = null;
                }

                keySet = null;
                objKeySet = null;
            }
            objCaseDetailTO = null;
        } catch (Exception e) {
            sqlMap.rollbackTransaction();
            e.printStackTrace();
            throw e;
        }
    }

    private void executeOneTabQueries(String strTOName, TermLoanCaseDetailTO objCaseDetailTO) throws Exception {
        try {
            StringBuffer sbMapName = new StringBuffer();
            if (objCaseDetailTO.getCommand() != null) {
                sbMapName.append(objCaseDetailTO.getCommand().toLowerCase());
                System.out.println("insertcheck" + sbMapName);
                sbMapName.append(strTOName);
                System.out.println("tostring" + sbMapName.toString());
                sqlMap.executeUpdate(sbMapName.toString(), objCaseDetailTO);
            }
        } catch (Exception e) {
            sqlMap.rollbackTransaction();
            e.printStackTrace();
            throw e;
        }
    }

    private void insertData(HashMap map, LogDAO objLogDAO, LogTO objLogTO) throws Exception {
        try {
            sqlMap.startTransaction();
            MaintenanceTO.setBondSet("Y");
            MaintenanceTO.setApplNo(objTO.getApplNo());
            System.out.println("mmmma>>>>>>>>>>>" + MaintenanceTO);
            System.out.println("nnnn>>>>>>>>>>>>>>" + objTO);
            List lstAppnNo = sqlMap.executeQueryForList("getApplno", MaintenanceTO);
            if (lstAppnNo.size() > 0 && lstAppnNo != null) {
                HashMap mapAplNo = (HashMap) lstAppnNo.get(0);
                String applNo = CommonUtil.convertObjToStr(mapAplNo.get("APPL_NO"));
                String bondNo = CommonUtil.convertObjToStr(mapAplNo.get("BOND_NO"));
                System.out.println("applNo====" + applNo);
                MaintenanceTO.setApplNo(applNo);
                MaintenanceTO.setBondNo(bondNo);
            }
            MaintenanceTO.setApplicationSet("Y");
            sqlMap.executeUpdate("insertMDSMasterMaintenanceTO", MaintenanceTO);
            if (!SalarySecDetails.isEmpty()) {
                insertMdsSalarySecurity(MaintenanceTO);

            }
            insertMemberTableDetails();
            insertDepositTableDetails();
            insertSocietyTableDetails();
            String USERID = CommonUtil.convertObjToStr(map.get(CommonConstants.USER_ID));
            if (MaintenanceTO.getNominee().equals("Y")) {
                ArrayList nomineeTOList = new ArrayList();
                if (map.containsKey("AccountNomineeTO")) {
                    nomineeTOList = (ArrayList) map.get("AccountNomineeTO");
                }
                ArrayList nomineeDeleteList = new ArrayList();
                if (map.containsKey("AccountNomineeDeleteTO")) {
                    nomineeDeleteList = (ArrayList) map.get("AccountNomineeDeleteTO");
                }
                if (nomineeTOList != null && nomineeTOList.size() > 0) {
                    NomineeDAO objNomineeDAO = new NomineeDAO();
                    System.out.println("nomineeTOList List " + nomineeTOList);
                    System.out.println("nomineeDeleteList List " + nomineeDeleteList);
                    //                objNomineeDAO.deleteData(objTO.getDepositNo(), SCREEN);
                    objNomineeDAO.insertData(nomineeTOList, MaintenanceTO.getChittalNo() + "_" + MaintenanceTO.getSubNo(), false, USERID, SCREEN, objLogTO, objLogDAO);
                    if (nomineeDeleteList != null) {
                        objNomineeDAO.insertData(nomineeDeleteList, MaintenanceTO.getChittalNo() + "_" + MaintenanceTO.getSubNo(), true, USERID, SCREEN, objLogTO, objLogDAO);
                    }
                }
            }
            logTO.setData(MaintenanceTO.toString());
            logTO.setPrimaryKey(MaintenanceTO.getKeyData());
            logTO.setStatus(MaintenanceTO.getStatus());
            logDAO.addToLog(logTO);
            sqlMap.commitTransaction();
        } catch (Exception e) {
            sqlMap.rollbackTransaction();
            log.error(e);
            throw new TransRollbackException(e);
        }
        map = null;
    }

    private void insertMemberTableDetails() throws Exception {

        if (memberTableDetails != null) {
            ArrayList addList = new ArrayList(memberTableDetails.keySet());
            for (int i = 0; i < addList.size(); i++) {
                MDSMemberTypeTO objMemberTypeTO = (MDSMemberTypeTO) memberTableDetails.get(addList.get(i));
                sqlMap.executeUpdate("insertMDSMemberTypeTO", objMemberTypeTO);
                logTO.setData(objMemberTypeTO.toString());
                logTO.setPrimaryKey(objMemberTypeTO.getKeyData());
                logDAO.addToLog(logTO);
                objMemberTypeTO = null;
            }
        }
    }

    private void insertDepositTableDetails() throws Exception {
        if (depositTableDetails != null) {
            ArrayList addList = new ArrayList(depositTableDetails.keySet());
            for (int i = 0; i < addList.size(); i++) {
                MDSDepositTypeTO objMemberTypeTO = (MDSDepositTypeTO) depositTableDetails.get(addList.get(i));
                sqlMap.executeUpdate("insertMDSDepositTypeTO", objMemberTypeTO);
                HashMap hmap = new HashMap();
                ArrayList alist = new ArrayList();
                System.out.println("objDepositTypeTO@@@@" + objMemberTypeTO);
                hmap.put("DEPOSIT_NO", objMemberTypeTO.getDepositNo());
                List list = sqlMap.executeQueryForList("getAvailableBalForDep", hmap);
                if (list != null && list.size() > 0) {
                    hmap = (HashMap) list.get(0);
                    double avialbleBal = CommonUtil.convertObjToDouble(hmap.get("AVAILABLE_BALANCE")).doubleValue();
                    DepositLienTO objDepositLienTO = new DepositLienTO();
                    objDepositLienTO.setLienAcNo(objMemberTypeTO.getChittalNo());
                    objDepositLienTO.setDepositNo(objMemberTypeTO.getDepositNo());
                    objDepositLienTO.setLienAmount(CommonUtil.convertObjToDouble(avialbleBal));
                    objDepositLienTO.setLienDt(CurrDt);
                    objDepositLienTO.setStatus(objMemberTypeTO.getStatus());
                    objDepositLienTO.setStatusBy(objMemberTypeTO.getStatusBy());
                    objDepositLienTO.setStatusDt(objMemberTypeTO.getStatusDt());
                    objDepositLienTO.setRemarks("Lien From MDS");
                    objDepositLienTO.setDepositSubNo(CommonUtil.convertObjToInt("1"));
                    objDepositLienTO.setLienNo("-");
                    DepositLienDAO depositLienDao = new DepositLienDAO();
                    depositLienDao.setCallFromOtherDAO(true);
                    alist.add(objDepositLienTO);
                    hmap.put("lienTOs", alist);
                    hmap.put("SHADOWLIEN", objDepositLienTO.getLienAmount());
                    hmap.put("COMMAND", CommonConstants.TOSTATUS_INSERT);
                    hmap.put("BRANCH_CODE", _branchCode);
                    depositLienDao.execute(hmap);
                }

                logTO.setData(objMemberTypeTO.toString());
                logTO.setPrimaryKey(objMemberTypeTO.getKeyData());
                logDAO.addToLog(logTO);
                objMemberTypeTO = null;
            }
        }
    }

    private void insertSocietyTableDetails() throws Exception {
        if (societyTableDetails != null) {
            ArrayList addList = new ArrayList(societyTableDetails.keySet());
            for (int i = 0; i < addList.size(); i++) {
                MDSSocietyTypeTO objMDSSocietyTypeTO = (MDSSocietyTypeTO) societyTableDetails.get(addList.get(i));
                objMDSSocietyTypeTO.setStatusDt(CurrDt);
                sqlMap.executeUpdate("insertMDSSocietyTypeTO", objMDSSocietyTypeTO);
                logTO.setData(objMDSSocietyTypeTO.toString());
                logTO.setPrimaryKey(objMDSSocietyTypeTO.getKeyData());
                logDAO.addToLog(logTO);
                objMDSSocietyTypeTO = null;
            }
        }
    }

    private void updateData(HashMap map, LogDAO objLogDAO, LogTO objLogTO) throws Exception {
        try {
            HashMap whereMap = new HashMap();
            whereMap.put("SCHEME_NAME", MaintenanceTO.getSchemeName());
            whereMap.put("CHITTAL_NO", MaintenanceTO.getChittalNo());
            whereMap.put("SUB_NO", CommonUtil.convertObjToInt(MaintenanceTO.getSubNo()));
            map.put("SCHEME_NAME", MaintenanceTO.getSchemeName());
            map.put("CHITTAL_NO", MaintenanceTO.getChittalNo());
            map.put("SUB_NO", CommonUtil.convertObjToInt(MaintenanceTO.getSubNo()));
            List existList = (List) sqlMap.executeQueryForList("getSelectMDSMasterData", whereMap);
            if (existList != null && existList.size() > 0) {
                sqlMap.startTransaction();
                System.out.println("MaintenanceTOMaintenanceTO.." + MaintenanceTO.getBondSet());
                // MaintenanceTO.setBondSet("Y");
                sqlMap.executeUpdate("updateMDSMasterMaintenanceTO", MaintenanceTO);
//                if(!SalarySecDetails.isEmpty())
//                {
                updateMdsSalarySecurity(MaintenanceTO);
                updateMdsGoldSecurity(MaintenanceTO);//Added by sreekrishnan
//                }
                HashMap bondMap = new HashMap();
                bondMap.put("SCHEME", MaintenanceTO.getSchemeName());
                List LastBondNOList = sqlMap.executeQueryForList("getLastBondValue", bondMap);
                int LastBonNo = CommonUtil.convertObjToInt(((HashMap) LastBondNOList.get(0)).get("LAST_BOND_NO").toString());

                String defaulter = MaintenanceTO.getDefaulter();
                System.out.println("bondd dset..." + MaintenanceTO.getBondSet());
                if (defaulter.equals("N") && MaintenanceTO.getBondSet().equals("Y")) {
                    int currentbondNO = CommonUtil.convertObjToInt(MaintenanceTO.getBondNo());
                    if (LastBonNo < currentbondNO) {
                        bondMap.put("LASTBONDNO", currentbondNO);
                        sqlMap.executeUpdate("updateSchemeBondNO", bondMap);
                        //for trans
                        objTO.setCommand(CommonConstants.TOSTATUS_INSERT);
                        returnMap.put("APPLICATION_NO", objTO.getApplNo());
                        //executeTransactionPart(map, transDetailMap,chargeLst);//trans charge details
                    }
                }
                System.out.println("################## Update Method MaintenanceTO : " + MaintenanceTO);
                System.out.println("################## Update Method defaulter : " + defaulter);
                String onlyApplication = MaintenanceTO.getOnlyApplication();
                if (!onlyApplication.equals("Y") && defaulter.equals("N") && MaintenanceTO.getApplicationSet().equals("Y")) {
                    bondMap = new HashMap();
                    bondMap.put("SCHEME", MaintenanceTO.getSchemeName());
                    LastBondNOList = sqlMap.executeQueryForList("getLastApplicationValue", bondMap);
                    if(LastBondNOList != null && LastBondNOList.size() > 0){
                        HashMap lastBondMap = (HashMap) LastBondNOList.get(0);
                        if(lastBondMap != null && lastBondMap.containsKey("LAST_APPLICATION_NO") && lastBondMap.get("LAST_APPLICATION_NO") != null){
                        LastBonNo = CommonUtil.convertObjToInt(lastBondMap.get("LAST_APPLICATION_NO").toString());    
                        }
                    }
                    int currentbondNO = CommonUtil.convertObjToInt(MaintenanceTO.getApplNo());
                    System.out.println("currentbondNO===" + currentbondNO + "LastBonNo-----" + LastBonNo);
                    if (LastBonNo < currentbondNO) {
                        bondMap.put("LASTAPPLICATIONNO", currentbondNO);
                        sqlMap.executeUpdate("updateSchemeApplicationNO", bondMap);
                        //for trans
                        objTO.setCommand(CommonConstants.TOSTATUS_INSERT);
                        String sts = objTO.getCommand();
                        String applN = objTO.getApplNo();
                        System.out.println("applN.." + applN);
                        objTO.setCommand(CommonConstants.TOSTATUS_INSERT);
                        doLoanApplTransactions(map);
                        System.out.println("sts.." + sts);
                        objTO.setCommand(sts);
                        objTO.setApplNo(applN);
                        returnMap.put("APPLICATION_NO", objTO.getApplNo());
                        returnMap.put("APPLICATION_NO", objTO.getApplNo());
                        MaintenanceTO.setApplicationSet("Y");
                        //executeTransactionPart(map, transDetailMap,chargeLst);//trans charge details
                    }
                }
                updateMemberTableDetails();
                updateDepositTableDetails();
                updateSocietyTableDetails();
                updateCollateralTableDetails();
                executeCaseTabQuery(map);
                String USERID = CommonUtil.convertObjToStr(map.get(CommonConstants.USER_ID));
                ArrayList nomineeTOList = (ArrayList) map.get("AccountNomineeTO");
                ArrayList nomineeDeleteList = (ArrayList) map.get("AccountNomineeDeleteTO");
                if (nomineeTOList != null) {
                    NomineeDAO objNomineeDAO = new NomineeDAO();
                    if (nomineeTOList.size() > 0) {
                        System.out.println("Nominee List " + nomineeTOList);
                        objNomineeDAO.deleteData(MaintenanceTO.getChittalNo() + "_" + MaintenanceTO.getSubNo(), SCREEN);
                        objNomineeDAO.insertData(nomineeTOList, MaintenanceTO.getChittalNo() + "_" + MaintenanceTO.getSubNo(), false, USERID, SCREEN, objLogTO, objLogDAO);
                    }
                    if (nomineeDeleteList != null) {
                        objNomineeDAO.insertData(nomineeDeleteList, MaintenanceTO.getChittalNo() + "_" + MaintenanceTO.getSubNo(), true, USERID, SCREEN, objLogTO, objLogDAO);
                    }
                }
                System.out.println("##$%#$ Map : " + map);
                sqlMap.executeUpdate("updateMDSApplStandingDetail", map);
                logTO.setData(MaintenanceTO.toString());
                logTO.setBranchId(_branchCode);
                logTO.setPrimaryKey(MaintenanceTO.getKeyData());
                logTO.setStatus(MaintenanceTO.getStatus());
                logDAO.addToLog(logTO);
                sqlMap.commitTransaction();
            } else {
                insertData(map, objLogDAO, objLogTO);
                insertCollateralTableDetails();
            }
        } catch (Exception e) {
            sqlMap.rollbackTransaction();
            log.error(e);
            throw new TransRollbackException(e);
        }
        map = null;
    }

    //harge details
    private void executeTransactionPart(HashMap map, HashMap transDetailMap, List chargeLst) throws Exception {
        try {
            appNo = objTO.getApplNo();
            transDetailMap.put("ACCT_NUM", appNo);
            transDetailMap.put("BRANCH_CODE", map.get("BRANCH_CODE"));
            System.out.println("@##$#$% map #### :" + map);
            System.out.println("@##$#$% transDetailMap #### :" + transDetailMap);
            //  if(transDetailMap!=null && transDetailMap.size()>0){
            //  if(transDetailMap.containsKey("TRANSACTION_PART")){
            transDetailMap = getProdBehavesLike(transDetailMap);
            System.out.println("chargelist@@@@98364" + chargeLst);
            //////  insertTimeTransaction(map,transDetailMap,chargeLst);
            // }
            // }
        } catch (Exception e) {
            sqlMap.rollbackTransaction();
            e.printStackTrace();
            throw e;
        }
    }

    public Date getProperDateFormat(Object obj) {
        Date currDate = null;
        if (obj != null && obj.toString().length() > 0) {
            Date tempDt = DateUtil.getDateMMDDYYYY(CommonUtil.convertObjToStr(obj));
            currDate = (Date) CurrDt.clone();
            currDate.setDate(tempDt.getDate());
            currDate.setMonth(tempDt.getMonth());
            currDate.setYear(tempDt.getYear());
        }
        return currDate;
    }

    private HashMap getProdBehavesLike(HashMap dataMap) throws Exception {
        List list = (List) sqlMap.executeQueryForList("TermLoan.getBehavesLike", dataMap);
        if (list.size() > 0) {
            HashMap retrieveMap = (HashMap) list.get(0);
            dataMap.put(PROD_ID, retrieveMap.get(PROD_ID));
            dataMap.put("prodId", retrieveMap.get(PROD_ID));
            dataMap.put(LIMIT, retrieveMap.get(LIMIT));
            dataMap.put(BEHAVES_LIKE, retrieveMap.get(BEHAVES_LIKE));
            dataMap.put(INT_GET_FROM, retrieveMap.get(INT_GET_FROM));
            dataMap.put(SECURITY_DETAILS, retrieveMap.get(SECURITY_DETAILS));
            System.out.println("dataMap  ##" + dataMap);
            retrieveMap = null;
        }
        list = null;
        return dataMap;
    }

    //trans details
    private void doLoanApplTransactions(HashMap map) throws Exception, Exception {
        try {
            // objTO.setCommand("INSERT");
            System.out.println("mapmapmapINNNNNNNNN to :" + map);
            System.out.println("cccc" + objTO.getCommand());
            if (objTO.getCommand() != null) {
                //  objTO.setCommand("INSERT");
                if (objTO.getCommand().equals(CommonConstants.TOSTATUS_INSERT)) {
                    System.out.println("!@#!@#@# !ID GENERATED:" + objTO.getApplNo());
                    String application_no = objTO.getChittalNo() + "_" + objTO.getSubNo() + "_" + objTO.getApplNo();
                    String linkBatchId = objTO.getChittalNo() + "_" + objTO.getSubNo();
                    //4a  double amtBorrowed = CommonUtil.convertObjToDouble(objTO.getLoanAmt()).doubleValue() ;
                    //5a  System.out.println("@#$ amtBorrowed :"+amtBorrowed);
                    HashMap txMap;
                    HashMap whereMap = new HashMap();
                    whereMap.put("APPLICATION_NO", objTO.getApplNo());
                    HashMap temp = new HashMap();
                    temp.put("SCHEME_NAME", objTO.getSchemeName());
                    List prodList = sqlMap.executeQueryForList("getMdsProdDesc", temp);
                    temp = new HashMap();
                    temp = (HashMap) prodList.get(0);
                    HashMap m2 = new HashMap();
                    m2.put("SCHEME_NAME", temp.get("PROD_DESC").toString());
                    //  String s=objTO.getApplNo();
                    //    s=temp.get("PROD_DESC").toString()+s;
                    String s = objTO.getChittalNo();
                    objTO.setApplNo(s);
                    System.out.println("objTO.getSchemName>>>>>>>>>>>." + temp.get("PROD_DESC"));
                    //System.out.println("map.get(\"CHARGE_DESC\").toString()>>>>>111"+map.get("CHARGE_DESC").toString());
                    List chgDetails = (List) map.get("Charge List Data");
                    if (chgDetails != null) {
                    //if(map.get("Charge List Data")!= null){    
                        //for (int i = 0; i < chgDetails.size(); i++) {
                            //HashMap newMap = (HashMap) chgDetails.get(i);
                            ///m2.put("CHARGE_DESC", newMap.get("CHARGE_DESC").toString());                            //}
                            //HashMap acHeads = (HashMap) sqlMap.executeQueryForObject("getacHdData", m2);
                            //double chargeAmt = CommonUtil.convertObjToDouble(newMap.get("CHARGE_AMOUNT")).doubleValue();
                            //String achd = "";
                            //if (acHeads != null) {
                            //    achd = acHeads.get("ACC_HEAD").toString();
                            //}
                            TransferTrans objTransferTrans = new TransferTrans();
                            TxTransferTO transferTo = null;
                            objTransferTrans.setInitiatedBranch(_branchCode);
                            objTransferTrans.setLinkBatchId(linkBatchId);
                            txMap = new HashMap();
                            ArrayList transferList = new ArrayList();
                            LinkedHashMap TransactionDetailsMap = (LinkedHashMap) map.get("TransactionTO");
                            System.out.println("TransactionDetailsMap####---->" + TransactionDetailsMap);
                            if (TransactionDetailsMap.size() > 0) {
                                System.out.println("haaaaaaaaai inside TransactionDetailsMap>>>>>>>> ");
                                if (TransactionDetailsMap.containsKey("NOT_DELETED_TRANS_TOs")) {
                                    LinkedHashMap allowedTransDetailsTO = (LinkedHashMap) TransactionDetailsMap.get("NOT_DELETED_TRANS_TOs");
                                    TransactionTO objTransactionTO = null;
                                    if (allowedTransDetailsTO != null) {
                                            objTransactionTO = (TransactionTO) allowedTransDetailsTO.get(String.valueOf(1));
                                            System.out.println("objTransactionTO---->" + objTransactionTO);
                                            if (objTransactionTO.getTransType().equals("TRANSFER")) {
                                                for (int j = 0; j < chgDetails.size(); j++) {
                                                    HashMap newMap = (HashMap) chgDetails.get(j);
                                                    System.out.println("newMap>>>>>>>> "+newMap);
                                                    m2.put("CHARGE_DESC", newMap.get("CHARGE_DESC").toString());                         
                                                    HashMap acHeads = (HashMap) sqlMap.executeQueryForObject("getacHdData", m2);
                                                    double chargeAmt = CommonUtil.convertObjToDouble(newMap.get("CHARGE_AMOUNT")).doubleValue();
                                                    String achd = "";
                                                    if (acHeads != null) {
                                                        achd = acHeads.get("ACC_HEAD").toString();
                                                    }
                                                    //Credits...
                                                    if(chargeAmt>0){
                                                        txMap.put(TransferTrans.CR_AC_HD, achd);
                                                        txMap.put(TransferTrans.CR_PROD_TYPE, TransactionFactory.GL);
                                                        txMap.put(TransferTrans.CR_BRANCH, _branchCode);
                                                        txMap.put(TransferTrans.PARTICULARS, "To " + application_no + " Disbursement");
                                                        txMap.put(CommonConstants.USER_ID, logTO.getUserId());                                                    
                                                        txMap.put(TransferTrans.AUTHORIZE_STATUS_2, "ENTERED_AMOUNT");                                                                                             
                                                        txMap.put("TRANS_MODE_TYPE", "MDS");
                                                        txMap.put(TransferTrans.CURRENCY, "INR");
                                                        txMap.put("SCREEN_NAME",CommonUtil.convertObjToStr(map.get(CommonConstants.SCREEN)));
                                                        //transferTo.setLinkBatchId(linkBatchId);      
                                                        transferTo = objTransferTrans.getCreditTransferTO(txMap, chargeAmt); 
                                                        transferList.add(transferTo);            
                                                    }
                                                }
                                                //Debits...
                                                if (objTransactionTO.getProductType().equals("GL")) {
                                                    txMap.put(TransferTrans.DR_AC_HD, CommonUtil.convertObjToStr(objTransactionTO.getDebitAcctNo()));
                                                } else {
                                                    txMap.put(TransferTrans.DR_ACT_NUM, CommonUtil.convertObjToStr(objTransactionTO.getDebitAcctNo()));
                                                    txMap.put(TransferTrans.DR_PROD_ID, CommonUtil.convertObjToStr(objTransactionTO.getProductId()));
                                                }
                                                    txMap.put(TransferTrans.DR_BRANCH, _branchCode);
                                                    txMap.put("DR_INST_TYPE", "VOUCHER");                                                   
                                                    txMap.put(TransferTrans.DR_PROD_TYPE, CommonUtil.convertObjToStr(objTransactionTO.getProductType()));
                                                    txMap.put("SCREEN_NAME",CommonUtil.convertObjToStr(map.get(CommonConstants.SCREEN)));
                                                    //transferTo.setLinkBatchId(linkBatchId);      
                                                    transferTo = objTransferTrans.getDebitTransferTO(txMap, objTransactionTO.getTransAmt()); 
                                                    transferList.add(transferTo);
                                               if (transferList != null && transferList.size() > 0 && objTransactionTO.getTransAmt() > 0) {
                                                    System.out.println("transferList  ####" + transferList);               
                                                    objTransferTrans.doDebitCredit(transferList, _branchCode, false);
                                                }
                                                    
                                             //transfer ends...   
                                            } else {
                                                _branchCode = (String) map.get(CommonConstants.BRANCH_ID);
                                                double transAmt = CommonUtil.convertObjToDouble(map.get("CHARGE_AMOUNT"));
                                                System.out.println("chgDetails^^^^^^^cash" + chgDetails);
                                                //  TransactionTO transTO = new TransactionTO();
                                                CashTransactionTO objCashTO = null;
                                                ArrayList cashList = null;
                                                for (int j = 0; j < chgDetails.size(); j++) {
                                                    cashList = new ArrayList();
                                                    objCashTO = new CashTransactionTO();
                                                    HashMap newMap = (HashMap) chgDetails.get(j);
                                                    System.out.println("newMap#############>>>>>>>> "+newMap);
                                                    System.out.println("jjjjjjjj#############>>>>>>>> "+j);
                                                    System.out.println("chgDetails>>>>>>>> "+chgDetails.get(j));
                                                    m2.put("CHARGE_DESC", newMap.get("CHARGE_DESC").toString());                         
                                                    HashMap acHeads = (HashMap) sqlMap.executeQueryForObject("getacHdData", m2);
                                                    double chargeAmt = CommonUtil.convertObjToDouble(newMap.get("CHARGE_AMOUNT")).doubleValue();
                                                    String achd = "";
                                                    if (acHeads != null) {
                                                        achd = acHeads.get("ACC_HEAD").toString();
                                                    }
                                                    //Credits...
                                                    if(chargeAmt>0){  
                                                        System.out.println("transAmt22222222^^^^^^^" + transAmt);
                                                        objCashTO.setAcHdId(achd);
                                                        objCashTO.setProdType(TransactionFactory.GL);
                                                        objCashTO.setTransType(CommonConstants.CREDIT);
                                                        objCashTO.setInitTransId(CommonUtil.convertObjToStr(map.get("USER_ID")));
                                                        objCashTO.setBranchId(_branchCode);
                                                        objCashTO.setStatusBy(CommonUtil.convertObjToStr(map.get("USER_ID")));
                                                        objCashTO.setStatusDt(getProperDateFormat(CurrDt));
                                                        objCashTO.setAuthorizeStatus_2("ENTERED_AMOUNT");
                                                        objCashTO.setParticulars("By " + application_no + " Disbursement");
                                                        objCashTO.setInitiatedBranch(_branchCode);
                                                        objCashTO.setInitChannType(CommonConstants.CASHIER);
                                                        objCashTO.setTransModType("MDS");
                                                        objCashTO.setCommand(CommonConstants.TOSTATUS_INSERT);
                                                        objCashTO.setInpAmount(chargeAmt);
                                                        objCashTO.setScreenName(CommonUtil.convertObjToStr(map.get(CommonConstants.SCREEN)));
                                                    objCashTO.setAmount(chargeAmt);
                                                    objCashTO.setLinkBatchId(linkBatchId);
                                                    objCashTO.setGlTransActNum(CommonUtil.convertObjToStr(MaintenanceTO.getChittalNo()));
                                                    System.out.println("objCashTO^^^^^^^" + objCashTO);
                                                    cashList.add(objCashTO);
                                                    System.out.println("cashList-------cashh--------->" + cashList);
                                                    HashMap tranMap = new HashMap();
                                                    System.out.println("BRANCH_CODE---cashh------------->" + map.get("BRANCH_CODE"));
                                                    tranMap.put(CommonConstants.BRANCH_ID, CommonUtil.convertObjToStr(map.get("BRANCH_CODE")));
                                                    tranMap.put(CommonConstants.USER_ID, CommonUtil.convertObjToStr(map.get("USER_ID")));
                                                    tranMap.put(CommonConstants.IP_ADDR, CommonUtil.convertObjToStr(map.get("IP_ADDR")));
                                                    tranMap.put(CommonConstants.MODULE, CommonUtil.convertObjToStr(map.get("MODULE")));
                                                    tranMap.put(CommonConstants.SCREEN, CommonUtil.convertObjToStr(map.get("SCREEN")));
                                                    tranMap.put("MODE", CommonConstants.TOSTATUS_INSERT);
                                                    tranMap.put("DAILYDEPOSITTRANSTO", cashList);
                                                    CashTransactionDAO cashDao;
                                                    cashDao = new CashTransactionDAO();
                                                    System.out.println("tranMap---------------->" + tranMap);
                                                    tranMap = cashDao.execute(tranMap, false);
                                                    System.out.println("tranMap2222---------------->" + tranMap);
                                                    cashDao = null;
                                                    tranMap = null;
                                                    }
                                                }
                                            }
                                            //End cash
                                            objTransactionTO.setStatus(CommonConstants.STATUS_CREATED);
                                            objTransactionTO.setBatchId(application_no);
                                            objTransactionTO.setBatchDt(CurrDt);
                                            objTransactionTO.setTransId(objTransferTrans.getLinkBatchId());
                                            objTransactionTO.setBranchId(_branchCode);
                                            System.out.println("objTransactionTO------------------->" + objTransactionTO);
                                            sqlMap.executeUpdate("insertRemittanceIssueTransactionTO", objTransactionTO);
                                       // }
                                    }
                                }
                            }
                            //2a  amtBorrowed=0.0;
                            objTransferTrans = null;
                            transferList = null;
                            txMap = null;
                            // Code End
                           // getTransDetails(application_no);
                            getTransDetails(linkBatchId);
                        //}
                    }
                } else {
                    HashMap shareAcctNoMap = new HashMap();
                    //                    shareAcctNoMap = (HashMap)sqlMap.executeQueryForObject("transferResolvedShare", shareAcctNoMap);
                    //                    sqlMap.executeUpdate("updateShareAcctDetailsTO", shareAcctDetailsTO);
                    //1a   double amtBorrowed = CommonUtil.convertObjToDouble(objTO.getAmtBorrowed()).doubleValue() ;
                    if (objTO.getCommand().equals(CommonConstants.TOSTATUS_DELETE)) {
//                        shareAcctNoMap = new HashMap();
//                        shareAcctNoMap.put("LINK_BATCH_ID",objTO.getBorrowingNo());
//                        List lst = sqlMap.executeQueryForList("getAuthBatchTxTransferTOs", shareAcctNoMap);
////                        TxTransferTO txTransferTO = null;
//                        double oldAmount = 0;
//                        HashMap oldAmountMap = new HashMap();
//                        ArrayList transferList = new ArrayList();
//                        if (lst!=null && lst.size()>0) {
//                            for (int j=0; j<lst.size(); j++) {
//                                txTransferTO = (TxTransferTO) lst.get(j);
//                                System.out.println("#@$@#$@#$lst:"+lst);
//                            }
//                            
//                        }else{
//                            System.out.println("In Cash Edit");
//                            LinkedHashMap TransactionDetailsMap = (LinkedHashMap) map.get("TransactionTO");
//                            if(TransactionDetailsMap.containsKey("NOT_DELETED_TRANS_TOs")) {
//                                LinkedHashMap allowedTransDetailsTO = (LinkedHashMap)TransactionDetailsMap.get("NOT_DELETED_TRANS_TOs");
//                                TransactionTO objTransactionTO = null;
//                                if(allowedTransDetailsTO!=null && allowedTransDetailsTO.size()>0){
//                                    for (int J = 1;J <= allowedTransDetailsTO.size();J++)  {
//                                        objTransactionTO = (TransactionTO) allowedTransDetailsTO.get(String.valueOf(J));
//                                        
//                                        //                                if ((oldAmount != newAmount) || command.equals(CommonConstants.TOSTATUS_DELETE)) {
//                                        HashMap tempMap=new HashMap();
//                                        //                                        if(!CommonUtil.convertObjToStr(shareAcctDetailsTO.getShareNoFrom()).equals("ADD")){
//                                        List cLst1 = sqlMap.executeQueryForList("getSelectShareCashTransactionTO",   CommonUtil.convertObjToStr(objTransactionTO.getTransId()));
//                                        if (cLst1!=null && cLst1.size()>0) {
//                                            CashTransactionTO txTransferTO1 = null;
//                                            txTransferTO1 = (CashTransactionTO) cLst1.get(0);
//                                            oldAmount= CommonUtil.convertObjToDouble(txTransferTO1.getAmount()).doubleValue();
//                                            double newAmount = CommonUtil.convertObjToDouble(objTransactionTO.getTransAmt()).doubleValue();
//                                            txTransferTO1.setInpAmount(new Double(newAmount));
//                                            txTransferTO1.setAmount(new Double(newAmount));
//                                            txTransferTO1.setCommand(command);
//                                            txTransferTO1.setStatus(CommonConstants.STATUS_DELETED);
//                                            txTransferTO1.setStatusDt(CurrDt);
//                                            
//                                            map.put("PRODUCTTYPE", TransactionFactory.GL);
//                                            map.put("OLDAMOUNT", new Double(oldAmount));
//                                            map.put("CashTransactionTO", txTransferTO1);
//                                            CashTransactionDAO cashTransDAO = new CashTransactionDAO();
//                                            cashTransDAO.execute(map,false);
//                                        }
//                                        cLst1 = null;
//                                        //                                        }
//                                        
//                                        //                                                         for (int J = 1;J <= allowedTransDetailsTO.size();J++){
//                                        
//                                        objTransactionTO.setStatus(CommonConstants.STATUS_DELETED);
//                                        objTransactionTO.setTransId(objDrfTransactionTO.getDrfTransID());
//                                        objTransactionTO.setBatchId(objDrfTransactionTO.getDrfTransID());
//                                        objTransactionTO.setBranchId(_branchCode);
//                                        sqlMap.executeUpdate("updateRemittanceIssueTransactionTO", objTransactionTO);
//                                        
//                                    }
//                                    
//                                }
//                                
//                                //                                }
//                                
//                                
//                            }
//                            lst = null;
//                            oldAmountMap = null;
//                            transferList = null;
//                            shareAcctNoMap = null;
//                            txTransferTO = null;
//                        }
                    }
                }
            }
        } catch (Exception e) {
            //                sqlMap.rollbackTransaction();
            e.printStackTrace();
            //                throw new TransRollbackException(e);
            throw e;
        }
    }

    private void getTransDetails(String batchId) throws Exception {
        HashMap getTransMap = new HashMap();
        // String branchId=CommonUtil.convertObjToStr(map.get("BRANCH_CODE"));
        System.out.println("currDt######" + currDt + "bcode@@@@@@@@@@@" + _branchCode);


        getTransMap.put("BATCH_ID", batchId);
        getTransMap.put("TRANS_DT", CurrDt.clone());
        getTransMap.put(CommonConstants.BRANCH_ID, _branchCode);
        System.out.println("########getTransferTransBatchID returnMap depCloseAmt:" + getTransMap);
        returnMap = new HashMap();
        List transList = (List) sqlMap.executeQueryForList("getTransferDetails", getTransMap);
        if (transList != null && transList.size() > 0) {
            returnMap.put("TRANSFER_TRANS_LIST", transList);
            System.out.println("########transfrretrnmap>>>>>>>>>>>///" + returnMap);
            System.out.println("########transfrlist>>>>>>>>>>>///" + transList);
        }
        List cashList = (List) sqlMap.executeQueryForList("getCashDetails", getTransMap);
        if (cashList != null && cashList.size() > 0) {
            returnMap.put("CASH_TRANS_LIST", cashList);
            System.out.println("########cashretrnmap>>>>>>>>>>>///" + returnMap);
            System.out.println("########cashlist>>>>>>>>>>>///" + cashList);

        }
        getTransMap.clear();
        getTransMap = null;
        transList = null;
        cashList = null;
    }

    //end...
    private void updateCollateralTableDetails() throws Exception {
        if (collateralTableDetails != null && collateralTableDetails.size() > 0) {
            ArrayList addList = new ArrayList(collateralTableDetails.keySet());
            MDSMasterSecurityLandTO objMDSMasterSecurityLandTO = null;
            for (int i = 0; i < collateralTableDetails.size(); i++) {
                objMDSMasterSecurityLandTO = new MDSMasterSecurityLandTO();
                objMDSMasterSecurityLandTO = (MDSMasterSecurityLandTO) collateralTableDetails.get(addList.get(i));
                if (objMDSMasterSecurityLandTO.getStatus().equals(CommonConstants.STATUS_CREATED)) {
                    objMDSMasterSecurityLandTO.setBranchCode(_branchCode);
                    sqlMap.executeUpdate("insertMDSSecurityLandTO", objMDSMasterSecurityLandTO);
                } else {
                    objMDSMasterSecurityLandTO.setBranchCode(_branchCode);
                    sqlMap.executeUpdate("updateMDSSecurityLandTO1", objMDSMasterSecurityLandTO);
                }
            }
        }
        if (deletedCollateralTableValues != null && deletedCollateralTableValues.size() > 0) {
            ArrayList addList = new ArrayList(deletedCollateralTableValues.keySet());
            MDSMasterSecurityLandTO objMDSMasterSecurityLandTO = null;
            for (int i = 0; i < deletedCollateralTableValues.size(); i++) {
                objMDSMasterSecurityLandTO = new MDSMasterSecurityLandTO();
                objMDSMasterSecurityLandTO = (MDSMasterSecurityLandTO) deletedCollateralTableValues.get(addList.get(i));
                objMDSMasterSecurityLandTO.setStatusDt(getProperDateFormat(CurrDt));
                objMDSMasterSecurityLandTO.setStatusBy(MaintenanceTO.getStatusBy());
                sqlMap.executeUpdate("deleteMDSSecurityLandTO1", objMDSMasterSecurityLandTO);
            }
        }
    }

    private void insertCollateralTableDetails() throws Exception {
        if (collateralTableDetails != null && collateralTableDetails.size() > 0) {
            ArrayList addList = new ArrayList(collateralTableDetails.keySet());
            for (int i = 0; i < addList.size(); i++) {
                MDSMasterSecurityLandTO objMDSMasterSecurityLandTO = (MDSMasterSecurityLandTO) collateralTableDetails.get(addList.get(i));
                objMDSMasterSecurityLandTO.setBranchCode(_branchCode);
                sqlMap.executeUpdate("insertMDSSecurityLandTO", objMDSMasterSecurityLandTO);
                logTO.setData(objMDSMasterSecurityLandTO.toString());
                logTO.setPrimaryKey(objMDSMasterSecurityLandTO.getKeyData());
                logDAO.addToLog(logTO);
                objMDSMasterSecurityLandTO = null;
            }
        }
    }

    private void updateMemberTableDetails() throws Exception {
        if (deletedMemberTableValues != null) {
            System.out.println("######## deletedMemberTableValues :" + deletedMemberTableValues);
            ArrayList addList = new ArrayList(deletedMemberTableValues.keySet());
            MDSMemberTypeTO objMemberTypeTO = null;
            for (int i = 0; i < deletedMemberTableValues.size(); i++) {
                objMemberTypeTO = new MDSMemberTypeTO();
                objMemberTypeTO = (MDSMemberTypeTO) deletedMemberTableValues.get(addList.get(i));
                System.out.println("$#@$$%objMemberTypeTO" + objMemberTypeTO);
                objMemberTypeTO.setStatusDt(getProperDateFormat(CurrDt));
                objMemberTypeTO.setStatusBy(MaintenanceTO.getStatusBy());
                sqlMap.executeUpdate("deleteMDSMemberTypeTO", objMemberTypeTO);
            }
        }
        if (memberTableDetails != null) {
            System.out.println("######## memberTableDetails :" + memberTableDetails);
            ArrayList addList = new ArrayList(memberTableDetails.keySet());
            MDSMemberTypeTO objMemberTypeTO = null;
            for (int i = 0; i < memberTableDetails.size(); i++) {
                objMemberTypeTO = new MDSMemberTypeTO();
                objMemberTypeTO = (MDSMemberTypeTO) memberTableDetails.get(addList.get(i));
                objMemberTypeTO.setStatusDt(getProperDateFormat(CurrDt));
                System.out.println("$#@$$%objMemberTypeTO" + objMemberTypeTO);
                if (objMemberTypeTO.getStatus().equals(CommonConstants.STATUS_CREATED)) {
                    sqlMap.executeUpdate("insertMDSMemberTypeTO", objMemberTypeTO);
                } else {
                    sqlMap.executeUpdate("updateMDSMemberTypeTO", objMemberTypeTO);
                }
            }
        }

    }

    private void updateDepositTableDetails() throws Exception {
        if (deletedDepositTableValues != null) {
            System.out.println("######## deletedDepositTableValues :" + deletedDepositTableValues);
            ArrayList addList = new ArrayList(deletedDepositTableValues.keySet());
            MDSDepositTypeTO objDepositTypeTO = null;
            for (int i = 0; i < deletedDepositTableValues.size(); i++) {
                objDepositTypeTO = new MDSDepositTypeTO();
                objDepositTypeTO = (MDSDepositTypeTO) deletedDepositTableValues.get(addList.get(i));
                System.out.println("$#@$$%objDepositTypeTO" + objDepositTypeTO);
                HashMap hmap = new HashMap();
                hmap.put("LIENAMOUNT", new Double(0.0));
                hmap.put("DEPOSIT_ACT_NUM", objDepositTypeTO.getDepositNo());
                hmap.put("CHITTAL_NO", objDepositTypeTO.getChittalNo());
                hmap.put("SUBNO", CommonUtil.convertObjToInt(1));
                hmap.put("STATUS", "DELETED");
//                hmap.put("STATUS_BY", objDepositTypeTO.getStatusBy());
//                hmap.put("STATUS_DT", getProperDateFormat(objDepositTypeTO.getStatusDt()));
                hmap.put("STATUS_BY", MaintenanceTO.getStatusBy());
                hmap.put("STATUS_DT", CurrDt.clone());
                List lst = sqlMap.executeQueryForList("getLienDEtailsForDelete", hmap);
                sqlMap.executeUpdate("deleteMDSDepositTypeTO", hmap);
                if (lst != null && lst.size() > 0) {
                    HashMap hash = (HashMap) lst.get(0);
                    String auth = CommonUtil.convertObjToStr(hash.get("AUTHORIZE_STATUS"));
                    if (auth.equals("AUTHORIZED")) {
                        hmap.put("LIENAMOUNT",  CommonUtil.convertObjToDouble(hash.get("LIEN_AMOUNT")));
                        hmap.put("SHADOWLIEN", new Double(0.0));
                        sqlMap.executeUpdate("updateSubAcInfoAvlBal", hmap);
                    } else {
                        hmap.put("SHADOWLIEN",hash.get("LIEN_AMOUNT"));
                        hmap.put("LIENAMOUNT", new Double(0.0));
                        sqlMap.executeUpdate("updateSubAcInfoBal", hmap);
                    }


                    sqlMap.executeUpdate("deleteLienForMDS", hmap);
                }

            }
        }
        if (depositTableDetails != null) {
            System.out.println("######## depositTableDetails :" + depositTableDetails);
            ArrayList addList = new ArrayList(depositTableDetails.keySet());
            double prizedAmount = 0.0;
            double avialbleBal = 0.0;
            MDSDepositTypeTO objDepositTypeTO = null;
            for (int i = 0; i < depositTableDetails.size(); i++) {
                objDepositTypeTO = new MDSDepositTypeTO();
                objDepositTypeTO = (MDSDepositTypeTO) depositTableDetails.get(addList.get(i));
                objDepositTypeTO.setStatusDt(getProperDateFormat(CurrDt));
                System.out.println("$#@$$%objDepositTypeTO" + objDepositTypeTO);
                if (objDepositTypeTO.getStatus().equals(CommonConstants.STATUS_CREATED)) {
                    sqlMap.executeUpdate("insertMDSDepositTypeTO", objDepositTypeTO);

                    HashMap hmap = new HashMap();
                    ArrayList alist = new ArrayList();
                    System.out.println("objDepositTypeTO@@@@" + objDepositTypeTO);
                    hmap.put("DEPOSIT_NO", objDepositTypeTO.getDepositNo());
                    hmap.put("CHITTAL_NO", objDepositTypeTO.getChittalNo());
                    hmap.put("SUB_NO", CommonUtil.convertObjToInt(objDepositTypeTO.getSubNo()));
                    //List list = sqlMap.executeQueryForList("getAvailableBalForDep", hmap);
                    List list ;
                    if (objDepositTypeTO.getProdType().equals("MDS")) {
                       list = sqlMap.executeQueryForList("getAvailableBalForMDSChittal", hmap);
                    }else{
                       list = sqlMap.executeQueryForList("getAvailableBalForDep", hmap);
                    }
                    if (i == 0) {
                        double noinset = 0.0;
                        double instAmt = 0.0;
                        double paidInst = 0.0;
                        double totAmt = 0.0;
                        List mdsList = sqlMap.executeQueryForList("getTotalInstAmount", hmap);
                        List mdsList1 = sqlMap.executeQueryForList("getSelctApplnReceiptDetails", hmap);
                        if (mdsList1 != null && mdsList1.size() > 0) {
                            hmap = (HashMap) mdsList1.get(0);
                            noinset = CommonUtil.convertObjToDouble(hmap.get("NO_OF_INSTALLMENTS")).doubleValue();
                            instAmt = CommonUtil.convertObjToDouble(hmap.get("INST_AMT")).doubleValue();
                            totAmt = instAmt * noinset;
                        }
                        if (mdsList != null && mdsList.size() > 0) {
                            hmap = (HashMap) mdsList.get(0);
                            paidInst = CommonUtil.convertObjToDouble(hmap.get("NO_INST_PAID")).doubleValue();
                            paidInst = paidInst * instAmt;
                        }
                        double tobepaidInst = totAmt - paidInst;
                        prizedAmount = tobepaidInst;
//                        List list1=sqlMap.executeQueryForList("getPrizedAmountDetails",hmap);
//                        if(list1!=null && list1.size()>0){
//                            HashMap hashMap=(HashMap)list1.get(0);
//                            prizedAmount=CommonUtil.convertObjToDouble(hashMap.get("PRIZED_AMOUNT")).doubleValue();
//                        }
                    }
                    if (objDepositTypeTO.getProdType().equals("TD") || objDepositTypeTO.getProdType().equals("Deposits") || objDepositTypeTO.getProdType().equals("MDS")) {
                        if (prizedAmount > 0.0) {
                            System.out.println("SAME CHITTAL ---->"+objDepositTypeTO.getSameChittal());
                            if(objDepositTypeTO.getSameChittal()!=null && objDepositTypeTO.getSameChittal().equals("N")){
                                hmap = (HashMap) list.get(0);
                                avialbleBal = CommonUtil.convertObjToDouble(hmap.get("AVAILABLE_BALANCE")).doubleValue();
                            }
                            DepositLienTO objDepositLienTO = new DepositLienTO();
                            objDepositLienTO.setLienAcNo(objDepositTypeTO.getChittalNo());
                            objDepositLienTO.setDepositNo(objDepositTypeTO.getDepositNo());
                            if (prizedAmount > avialbleBal) {
                                objDepositLienTO.setLienAmount(new Double(avialbleBal));
                                prizedAmount = prizedAmount - avialbleBal;
                            } else {
                                objDepositLienTO.setLienAmount(new Double(prizedAmount));
                            }
                            objDepositLienTO.setLienDt(CurrDt);
                            objDepositLienTO.setStatus(objDepositTypeTO.getStatus());
                            objDepositLienTO.setStatusBy(objDepositTypeTO.getStatusBy());
                            objDepositLienTO.setStatusDt(objDepositTypeTO.getStatusDt());
                            objDepositLienTO.setRemarks("Lien From MDS");
                            objDepositLienTO.setDepositSubNo(CommonUtil.convertObjToInt("1"));
                            objDepositLienTO.setLienNo("-");
                            DepositLienDAO depositLienDao = new DepositLienDAO();
                            depositLienDao.setCallFromOtherDAO(true);
                            alist.add(objDepositLienTO);
                            hmap.put("lienTOs", alist);
                            hmap.put("SHADOWLIEN", objDepositLienTO.getLienAmount());
                            hmap.put("COMMAND", CommonConstants.TOSTATUS_INSERT);
                            hmap.put("BRANCH_CODE", _branchCode);
                            depositLienDao.execute(hmap);
                        }
                    } else {
                        prizedAmount = prizedAmount - CommonUtil.convertObjToDouble(objDepositTypeTO.getMaturityValue()).doubleValue();
                    }
                } else {
                    sqlMap.executeUpdate("updateMDSDepositTypeTO", objDepositTypeTO);
                }
            }
        }

    }

    private void updateSocietyTableDetails() throws Exception {
        if (societyTableDetails != null) {
            System.out.println("######## societyTableDetails :" + societyTableDetails);
            ArrayList addList = new ArrayList(societyTableDetails.keySet());
            MDSSocietyTypeTO objMDSSocietyTypeTO = null;
            for (int i = 0; i < societyTableDetails.size(); i++) {
                objMDSSocietyTypeTO = new MDSSocietyTypeTO();
                objMDSSocietyTypeTO = (MDSSocietyTypeTO) societyTableDetails.get(addList.get(i));
                //objMDSSocietyTypeTO.setStatusDt(getProperDateFormat(CurrDt));
                objMDSSocietyTypeTO.setStatusDt(CurrDt);
                System.out.println("$#@$$%objMDSSocietyTypeTO" + objMDSSocietyTypeTO);
                if (objMDSSocietyTypeTO.getStatus().equals(CommonConstants.STATUS_CREATED)) {
                    sqlMap.executeUpdate("insertMDSSocietyTypeTO", objMDSSocietyTypeTO);
                } else {
                    sqlMap.executeUpdate("updateMDSSocietyTypeTO", objMDSSocietyTypeTO);
                }
            }
        }
        if (deletedSocietyTableValues != null) {
            System.out.println("######## deletedSocietyTableValues :" + deletedSocietyTableValues);
            ArrayList addList = new ArrayList(deletedSocietyTableValues.keySet());
            MDSSocietyTypeTO objMDSSocietyTypeTO = null;
            for (int i = 0; i < deletedSocietyTableValues.size(); i++) {
                objMDSSocietyTypeTO = new MDSSocietyTypeTO();
                objMDSSocietyTypeTO = (MDSSocietyTypeTO) deletedSocietyTableValues.get(addList.get(i));
                objMDSSocietyTypeTO.setStatusBy(MaintenanceTO.getStatusBy());
                objMDSSocietyTypeTO.setStatusDt(CurrDt);
                System.out.println("$#@$$%objMDSSocietyTypeTO" + objMDSSocietyTypeTO);
                sqlMap.executeUpdate("deleteMDSSocietyTypeTO", objMDSSocietyTypeTO);
            }
        }
    }

    private void deleteData(HashMap map) throws Exception {
        try {
            sqlMap.startTransaction();
            sqlMap.executeUpdate("deleteMDSMasterMaintenanceTO", MaintenanceTO);
            sqlMap.executeUpdate("deleteStatusMDSSalSec", MaintenanceTO);

            sqlMap.executeUpdate("deleteMDSMemberTypeDetails", MaintenanceTO);
            sqlMap.executeUpdate("deleteMDSDepositTypeDetails", MaintenanceTO);
            sqlMap.executeUpdate("deleteMDSSocietyTypeDetails", MaintenanceTO);
            sqlMap.executeUpdate("deleteMDSSecurityCollateralDetails", MaintenanceTO);
            if (depositTableDetails != null) {
                ArrayList addList = new ArrayList(depositTableDetails.keySet());
                for (int i = 0; i < addList.size(); i++) {
                    MDSDepositTypeTO objMemberTypeTO = (MDSDepositTypeTO) depositTableDetails.get(addList.get(i));
                    HashMap hmap = new HashMap();
                    hmap.put("DEPOSIT_ACT_NUM", objMemberTypeTO.getDepositNo());
                    hmap.put("SUBNO", CommonUtil.convertObjToInt("1"));
                    hmap.put("STATUS", "UNLIENED");
                    hmap.put("STATUS_BY", objMemberTypeTO.getStatusBy());
                    hmap.put("STATUS_DT", objMemberTypeTO.getStatusDt());
                    hmap.put("CHITTAL_NO", objDepositTypeTO.getChittalNo());
                    List lst = sqlMap.executeQueryForList("getLienDEtailsForDelete", hmap);
                    sqlMap.executeUpdate("deleteMDSDepositTypeTO", hmap);
                    if (lst != null && lst.size() > 0) {
                        HashMap hash = (HashMap) lst.get(0);
                        System.out.println("hash@@@@" + hash);
                        String auth = CommonUtil.convertObjToStr(hash.get("AUTHORIZE_STATUS"));
                        if (auth.equals("AUTHORIZED")) {
                            hmap.put("LIENAMOUNT", CommonUtil.convertObjToDouble(hash.get("LIEN_AMOUNT")));
                            hmap.put("SHADOWLIEN", new Double(0.0));
                            sqlMap.executeUpdate("updateSubAcInfoAvlBal", hmap);
                        } else {
                            hmap.put("SHADOWLIEN", hash.get("LIEN_AMOUNT"));
                            hmap.put("LIENAMOUNT", new Double(0.0));
                        }

                        sqlMap.executeUpdate("deleteLienForMDS", hmap);
                    }


                }
            }
            logTO.setData(MaintenanceTO.toString());
            logTO.setPrimaryKey(MaintenanceTO.getKeyData());
            logTO.setStatus(MaintenanceTO.getStatus());
            logDAO.addToLog(logTO);
            sqlMap.commitTransaction();
        } catch (Exception e) {
            sqlMap.rollbackTransaction();
            log.error(e);
            throw new TransRollbackException(e);
        }
        map = null;
    }

    private void authorize(HashMap map,HashMap Actmap) throws Exception {
        ArrayList selectedList = (ArrayList) map.get(CommonConstants.AUTHORIZEDATA);
        HashMap AuthMap = new HashMap();
        AuthMap = (HashMap) selectedList.get(0);
        String status = (String) AuthMap.get("STATUS");
        transactionDAO = new TransactionDAO(CommonConstants.DEBIT);
        AuthMap.put("STATUS_DT",CurrDt.clone());
        try {
            sqlMap.startTransaction();
            logTO.setData(map.toString());
            sqlMap.executeUpdate("authorizeMDSMasterMaintenance", AuthMap);
            sqlMap.executeUpdate("authorizeMDSSalSec", AuthMap);
            sqlMap.executeUpdate("authorizeMDSMemberDetails", AuthMap);
            sqlMap.executeUpdate("authorizeMDSDepositDetails", AuthMap);
            sqlMap.executeUpdate("authorizeMDSSocietyDetails", AuthMap);
            sqlMap.executeUpdate("authorizeMDSCollateralDetails", AuthMap);
            //Added by sreekrishan
            HashMap aHmap = new HashMap();
            aHmap.put("acctNum", AuthMap.get("CHITTAL_NO"));
            aHmap.put("authorizeStatus", AuthMap.get("STATUS"));
            aHmap.put("authorizeDt", getProperDateFormat(AuthMap.get("AUTHORIZED_DT")));
            aHmap.put("authorizeBy", AuthMap.get("AUTHORIZED_BY"));
            sqlMap.executeUpdate("authorizeGoldLoanSecurityTO", aHmap);
            sqlMap.executeUpdate("authorizeLoansSecurityGoldStockTO", aHmap); // Added by nithya on 07-03-2020 for KD-1379
            doTransactionCaseDetails(AuthMap, map);
            HashMap amap = new HashMap();
            amap.put("CHITTAL_NO", CommonUtil.convertObjToStr(AuthMap.get("CHITTAL_NO")));
            String sub_no=CommonUtil.convertObjToStr(AuthMap.get("SUB_NO"));
            amap.put("SUB_NO",CommonUtil.convertObjToInt(AuthMap.get("SUB_NO")));   //AJITH
            List getApplication_no = sqlMap.executeQueryForList("getApplication_No", amap);
            String applicationNo = "";
            if (getApplication_no != null && getApplication_no.size() > 0) {
                HashMap aplNo = new HashMap();
				if(getApplication_no!=null && getApplication_no.get(0)!=null){
                        aplNo = (HashMap) getApplication_no.get(0);
                     if (aplNo != null && aplNo.containsKey("APPLICATION_NO") && aplNo.get("APPLICATION_NO")!= null && !aplNo.get("APPLICATION_NO").equals("")) {
                        applicationNo = aplNo.get("APPLICATION_NO").toString();
                      }
				}
            }
            String applctnNo = AuthMap.get("CHITTAL_NO").toString() + "_" + AuthMap.get("SUB_NO").toString() + "_" + applicationNo;
            if (CommonUtil.convertObjToStr(AuthMap.get("STATUS")).equals("REJECTED")) {

              
                amap.put("APPLICATION_NO", applctnNo);
                amap.put("BRANCH_CODE", _branchCode);
                sqlMap.executeUpdate("upadetRemitIssueMds", amap);



                sqlMap.executeUpdate("UpadteAppauthorizeMDSMasterMaintenance", AuthMap);

            }
            insertCaseDetails(AuthMap);
            if (AuthMap.containsKey("LIEN_DETAILS")) {
                ArrayList lienList = (ArrayList) AuthMap.get("LIEN_DETAILS");
                if (lienList != null && lienList.size() > 0) {
                    for (int i = 0; i < lienList.size(); i++) {
                        HashMap lienMap = (HashMap) lienList.get(i);
                        lienMap.put("TODAY_DT", CurrDt.clone());
                        lienMap.put("SHADOWLIEN",CommonUtil.convertObjToDouble(CommonUtil.convertObjToDouble(lienMap.get("SHADOWLIEN")).doubleValue()));
                        lienMap.put("SUBNO", CommonUtil.convertObjToInt(lienMap.get("SUBNO")));
                        if (lienMap.get("STATUS").equals("AUTHORIZED")) {
                            sqlMap.executeUpdate("updateSubAcInfoBal", lienMap);
                            sqlMap.executeUpdate("updateLienForMDS", lienMap);
                            sqlMap.executeUpdate("updateSubACInfoStatusToLien", lienMap);
                        } else {
                            sqlMap.executeUpdate("updateLienForMDS", lienMap);
                            sqlMap.executeUpdate("updateSubAcInfoBal", lienMap);
                        }
                    }
                }
            }
            //transaction auth
             String linkBatchId = CommonUtil.convertObjToStr(AuthMap.get("CHITTAL_NO"))+"_"+CommonUtil.convertObjToStr(AuthMap.get("SUB_NO"));
             if (Actmap.containsKey("TransactionTO") && Actmap.get("TransactionTO") != null) {
                    System.out.println("transactioninvest1111>>>>>>>");
                    HashMap transactionDetailsMap = (LinkedHashMap) Actmap.get("TransactionTO");
                    TransactionTO transactionTO = new TransactionTO();
                    LinkedHashMap allowedTransDetailsTO = new LinkedHashMap();
                    if (transactionDetailsMap.size() > 0 && transactionDetailsMap.containsKey("NOT_DELETED_TRANS_TOs")) {
                        allowedTransDetailsTO = (LinkedHashMap) transactionDetailsMap.get("NOT_DELETED_TRANS_TOs");
                    }
                    
//                    System.out.println("transactioninvest1111####@@@@@333>>>>>>>" + allowedTransDetailsTO);
//                    System.out.println("transactioninvest1111@@@@44444>>>>>>>" + (TransactionTO) allowedTransDetailsTO.get(String.valueOf(1)));
                    if(allowedTransDetailsTO != null && allowedTransDetailsTO.size() > 0){
                    transactionTO = (TransactionTO) allowedTransDetailsTO.get(String.valueOf(1));
                    }
                    HashMap cashAuthMap = new HashMap();
                      //  System.out.println("borrowMap@@@" + borrowMap);
                        cashAuthMap.put(CommonConstants.BRANCH_ID, Actmap.get("BRANCH_CODE"));
                        cashAuthMap.put(CommonConstants.USER_ID, Actmap.get("USER_ID"));
                        cashAuthMap.put("DAILY", "DAILY");
                        System.out.println("cashtranslinkBatchId1111@@@" + linkBatchId);
                       // System.out.println("cashtransstatus1111@@@" + status);
                        System.out.println("cashtranscashAuthMap1111@@@" + cashAuthMap);

                        TransactionDAO.authorizeCashAndTransfer(linkBatchId, status, cashAuthMap);
                    //}
                    HashMap transMap = new HashMap();
                    transMap.put("LINK_BATCH_ID", linkBatchId);
                    sqlMap.executeUpdate("updateInstrumentNO1Transfer", transMap);
                    sqlMap.executeUpdate("updateInstrumentNO1Cash", transMap);
                    transMap = null;
                }
                TransactionTO objTransactionTO = new TransactionTO();
                objTransactionTO.setBatchId(CommonUtil.convertObjToStr(applctnNo));
                objTransactionTO.setTransId(CommonUtil.convertObjToStr(linkBatchId));
                objTransactionTO.setBranchId(_branchCode);
                sqlMap.executeUpdate("deleteRemittanceIssueTransactionTO", objTransactionTO);
            //end
            sqlMap.commitTransaction();
        } catch (Exception e) {
            sqlMap.rollbackTransaction();
            e.printStackTrace();
            throw new TransRollbackException(e);
        }
    }

    private void doTransactionCaseDetails(HashMap AuthMap, HashMap map) throws Exception {
        System.out.println("#@$#@@%@$%@CaseDetailTO" + AuthMap);
        if (CommonUtil.convertObjToStr(AuthMap.get("STATUS")).equals("AUTHORIZED")) {
            String actNum = CommonUtil.convertObjToStr(AuthMap.get("CHITTAL_NO")) + "_" + CommonUtil.convertObjToStr(AuthMap.get("SUB_NO"));
            AuthMap.put("ACT_NUM", actNum);
            List lst = (List) sqlMap.executeQueryForList("getMDSCaseChargeDetails", AuthMap);
            System.out.println("#@$#@@%@$%@CaseDetailTO" + lst);
            if (lst != null && lst.size() > 0) {
            } else {
                System.out.println("#@$#@@%@$%@CaseDetailTO" + AuthMap);
                HashMap applicationMap = new HashMap();
                double totalAmount = 0.0;
                applicationMap.put("SCHEME_NAME", CommonUtil.convertObjToStr(AuthMap.get("SCHEME_NAME")));
                List appLst = sqlMap.executeQueryForList("getSelectSchemeAcctHead", applicationMap);
                if (appLst != null && appLst.size() > 0) {
                    applicationMap = (HashMap) appLst.get(0);
                }
                AuthMap.put("ACCT_NUM", actNum);
                List caseLst = (List) sqlMap.executeQueryForList("getSelectTermLoanCaseDetail", AuthMap);
                System.out.println("########## CaseDetailTO" + lst);
                if (caseLst != null && caseLst.size() > 0) {
                    TxTransferTO transferTo = new TxTransferTO();
                    ArrayList TxTransferTO = new ArrayList();
                    TransactionTO transactionTO = new TransactionTO();
                    transactionDAO = new TransactionDAO(CommonConstants.DEBIT);
                    transactionDAO.setInitiatedBranch(_branchCode);
                    transactionDAO.setCommandMode(CommonConstants.TOSTATUS_INSERT);
                    transactionDAO.setTransactionType(transactionDAO.TRANSFER);
                    HashMap txMap = new HashMap();
                    for (int k = 0; k < caseLst.size(); k++) {
                        double cost = 0.0;
                        double expense = 0.0;
                        HashMap CaseMap = (HashMap) caseLst.get(k);
                        String caseStatus = CommonUtil.convertObjToStr(CaseMap.get("CASE_STATUS"));
                        cost = CommonUtil.convertObjToDouble(CaseMap.get("FILING_FEES")).doubleValue();
                        expense = CommonUtil.convertObjToDouble(CaseMap.get("MISC_CHARGES")).doubleValue();
                        totalAmount += cost + expense;
                        String costType = "";
                        String expType = "";
                        System.out.println("$#@$#@$@@caseStatus" + caseStatus);
                        if (caseStatus.equals("Arbitration Case")) {
                            costType = "ARC_COST";
                            expType = "ARC_EXPENSE";
                        } else if (caseStatus.equals("Execution of Award")) {
                            costType = "EA_COST";
                            expType = "EA_EXPENSE";
                        } else if (caseStatus.equals("Execution Process")) {
                            costType = "EP_COST";
                            expType = "EP_EXPENSE";
                        }
                        //COST
                        if (cost > 0) {
                            transferTo = new TxTransferTO();
                            txMap = new HashMap();
                            transferTo.setInstrumentNo2("APPL_GL_TRANS");
                            txMap.put(TransferTrans.DR_AC_HD, applicationMap.get(costType));
                            txMap.put(TransferTrans.DR_PROD_TYPE, TransactionFactory.GL);
                            txMap.put(TransferTrans.DR_BRANCH, _branchCode);
                            txMap.put(TransferTrans.PARTICULARS, costType + applicationMap.get(" MP_MDS_CODE") + "-" + actNum);
                            txMap.put(TransferTrans.AUTHORIZE_STATUS_2, "ENTERED_AMOUNT");
                            txMap.put(TransferTrans.DR_INST_TYPE, "VOUCHER");
                            txMap.put("SCREEN_NAME",CommonUtil.convertObjToStr(map.get(CommonConstants.SCREEN)));
                            transferTo = transactionDAO.addTransferDebitLocal(txMap, cost);
                            transferTo.setTransId("-");
                            transferTo.setBatchId("-");
                            transferTo.setTransDt(CurrDt);
                            transferTo.setInitiatedBranch(_branchCode);
                            transferTo.setStatusBy(CommonUtil.convertObjToStr(map.get("USER_ID")));
                            transferTo.setInitTransId(CommonUtil.convertObjToStr(map.get("USER_ID")));
                            transactionDAO.setLinkBatchID(CommonUtil.convertObjToStr(AuthMap.get("CHITTAL_NO")));
                            transactionTO.setChequeNo("SERVICE_TAX");
                            transferTo.setGlTransActNum(CommonUtil.convertObjToStr(MaintenanceTO.getChittalNo()));
                            txMap.put("SCREEN_NAME",CommonUtil.convertObjToStr(map.get(CommonConstants.SCREEN)));
                            TxTransferTO.add(transferTo);
                        }
                        //EXPENSE
                        if (expense > 0) {
                            transferTo = new TxTransferTO();
                            txMap = new HashMap();
                            transferTo.setInstrumentNo2("APPL_GL_TRANS");
                            txMap.put(TransferTrans.DR_AC_HD, applicationMap.get(expType));
                            txMap.put(TransferTrans.DR_PROD_TYPE, TransactionFactory.GL);
                            txMap.put(TransferTrans.DR_BRANCH, _branchCode);
                            txMap.put(TransferTrans.PARTICULARS, costType + applicationMap.get(" MP_MDS_CODE") + "-" + actNum);
                            txMap.put(TransferTrans.AUTHORIZE_STATUS_2, "ENTERED_AMOUNT");
                            txMap.put(TransferTrans.DR_INST_TYPE, "VOUCHER");
                            txMap.put("SCREEN_NAME",CommonUtil.convertObjToStr(map.get(CommonConstants.SCREEN)));
                            transferTo = transactionDAO.addTransferDebitLocal(txMap, expense);
                            transferTo.setTransId("-");
                            transferTo.setBatchId("-");
                            transferTo.setTransDt(CurrDt);
                            transferTo.setInitiatedBranch(_branchCode);
                            transferTo.setStatusBy(CommonUtil.convertObjToStr(map.get("USER_ID")));
                            transferTo.setInitTransId(CommonUtil.convertObjToStr(map.get("USER_ID")));
                            transactionDAO.setLinkBatchID(CommonUtil.convertObjToStr(AuthMap.get("CHITTAL_NO")));
                            transactionTO.setChequeNo("SERVICE_TAX");
                            transferTo.setGlTransActNum(CommonUtil.convertObjToStr(MaintenanceTO.getChittalNo()));
                            txMap.put("SCREEN_NAME",CommonUtil.convertObjToStr(map.get(CommonConstants.SCREEN)));
                            TxTransferTO.add(transferTo);
                        }
                    }
                    System.out.println("#### totalAmount : " + totalAmount);
                    //EXPENSE
                    if (totalAmount > 0) {
                        transferTo = new TxTransferTO();
                        txMap = new HashMap();
                        transferTo.setInstrumentNo2("APPL_GL_TRANS");
                        txMap.put(TransferTrans.CR_AC_HD, applicationMap.get("CASE_EXPENSE_HEAD"));
                        txMap.put(TransferTrans.CR_PROD_TYPE, TransactionFactory.GL);
                        txMap.put(TransferTrans.CR_BRANCH, _branchCode);
                        txMap.put(TransferTrans.PARTICULARS, applicationMap.get(" MP_MDS_CODE") + "-" + actNum);
                        txMap.put(TransferTrans.AUTHORIZE_STATUS_2, "ENTERED_AMOUNT");
                        txMap.put(TransferTrans.DR_INST_TYPE, "VOUCHER");
                        txMap.put("SCREEN_NAME",CommonUtil.convertObjToStr(map.get(CommonConstants.SCREEN)));
                        transferTo = transactionDAO.addTransferCreditLocal(txMap, totalAmount);
                        transferTo.setTransId("-");
                        transferTo.setBatchId("-");
                        transferTo.setTransDt(CurrDt);
                        transferTo.setInitiatedBranch(_branchCode);
                        transferTo.setStatusBy(CommonUtil.convertObjToStr(map.get("USER_ID")));
                        transferTo.setInitTransId(CommonUtil.convertObjToStr(map.get("USER_ID")));
                        transactionDAO.setLinkBatchID(CommonUtil.convertObjToStr(AuthMap.get("CHITTAL_NO")));
                        transactionTO.setChequeNo("SERVICE_TAX");
                        transferTo.setGlTransActNum(CommonUtil.convertObjToStr(MaintenanceTO.getChittalNo()));
                        txMap.put("SCREEN_NAME",CommonUtil.convertObjToStr(map.get(CommonConstants.SCREEN)));
                        TxTransferTO.add(transferTo);
                        // Calling Transfer DAO
                        HashMap transMap = new HashMap();
                        transferDAO = new TransferDAO();
                        map.put("MODE", CommonConstants.TOSTATUS_INSERT);
                        map.put("COMMAND", map.get("MODE"));
                        map.put("TxTransferTO", TxTransferTO);
                        map.put("BRANCH_CODE", _branchCode);
                        try {
                            System.out.println("#### TransferDAO map : " + map);
                            transMap = transferDAO.execute(map, false);
                            HashMap linkBatchMap = new HashMap();
                            linkBatchMap.put("LINK_BATCH_ID", transMap.get("TRANS_ID"));
                            linkBatchMap.put("BATCH_ID", transMap.get("TRANS_ID"));
                            linkBatchMap.put("TRANS_DT", CurrDt);
                            linkBatchMap.put("INITIATED_BRANCH", _branchCode);
                            sqlMap.executeUpdate("updateLinkBatchIdTransfer", linkBatchMap);
                            authorizeTransaction(transMap, map);
                        } catch (Exception e) {
                            sqlMap.rollbackTransaction();
                            e.printStackTrace();
                            throw e;
                        }
                    }
                }
            }
        }



    }

    public void authorizeTransaction(HashMap transMap, HashMap map) throws Exception {
        try {
            if (transMap != null && transMap.get("TRANS_ID") != null && !transMap.get("TRANS_ID").equals("")) {
                System.out.println("TRANSFER TRANS_ID :" + transMap.get("TRANS_ID"));
                String authorizeStatus = CommonUtil.convertObjToStr(CommonConstants.STATUS_AUTHORIZED);
                String linkBatchId = CommonUtil.convertObjToStr(transMap.get("TRANS_ID"));
                HashMap cashAuthMap = new HashMap();
                cashAuthMap.put(CommonConstants.BRANCH_ID, map.get(CommonConstants.BRANCH_ID));
                cashAuthMap.put(CommonConstants.USER_ID, map.get(CommonConstants.USER_ID));
                transactionDAO = new TransactionDAO(CommonConstants.DEBIT);
                TransactionDAO.authorizeCashAndTransferWithoutRemitIssueDet(linkBatchId, authorizeStatus, cashAuthMap);
                cashAuthMap = null;
                transMap = null;
            }
        } catch (Exception e) {
            sqlMap.rollbackTransaction();
            e.printStackTrace();
            throw e;
        }
    }

    private void insertCaseDetails(HashMap AuthMap) throws Exception {
        // CASE AUTHORIZE
        System.out.println("#@$#@@%@$%@CaseDetailTO" + AuthMap);
        String actNum = CommonUtil.convertObjToStr(AuthMap.get("CHITTAL_NO")) + "_" + CommonUtil.convertObjToStr(AuthMap.get("SUB_NO"));
        AuthMap.put("ACCT_NUM", actNum);
        List lst = (List) sqlMap.executeQueryForList("getSelectTermLoanCaseDetail", AuthMap);
        System.out.println("#@$#@@%@$%@CaseDetailTO" + lst);
        if (lst != null && lst.size() > 0) {
            TermLoanChargesTO objChargeTO = null;
            for (int k = 0; k < lst.size(); k++) {
                objChargeTO = new TermLoanChargesTO();
                HashMap CaseMap = (HashMap) lst.get(k);
                String caseStatus = CommonUtil.convertObjToStr(CaseMap.get("CASE_STATUS"));
                String fileCharge = CommonUtil.convertObjToStr(CaseMap.get("FILING_FEES"));
                String miscCharge = CommonUtil.convertObjToStr(CaseMap.get("MISC_CHARGES"));
                objChargeTO.setAct_num(CommonUtil.convertObjToStr(CaseMap.get("ACT_NUM")));
                if (CaseMap.get("FILING_DT") != null) {
                    objChargeTO.setChargeDt((Date) CaseMap.get("FILING_DT"));
                } else {
                    objChargeTO.setChargeDt(null);
                }
                objChargeTO.setStatus_Dt(CurrDt);
                objChargeTO.setStatus_By(CommonUtil.convertObjToStr(AuthMap.get("AUTHORIZED_BY")));
                objChargeTO.setAuthorize_Dt(CurrDt);
                objChargeTO.setAuthorize_by(CommonUtil.convertObjToStr(AuthMap.get("AUTHORIZED_BY")));
                objChargeTO.setAuthorize_Status(CommonUtil.convertObjToStr(AuthMap.get("STATUS")));
                String costType = "";
                String expType = "";
                System.out.println("$#@$#@$@@caseStatus" + caseStatus);
                if (caseStatus.equals("Arbitration Case")) {
                    costType = "ARC Cost";
                    expType = "ARC Expense";
                } else if (caseStatus.equals("Execution of Award")) {
                    costType = "EA Cost";
                    expType = "EA Expense";
                } else if (caseStatus.equals("Execution Process")) {
                    costType = "EP Cost";
                    expType = "EP Expense";
                }
                objChargeTO.setCharge_Type(costType);
                List chargeCostList = (List) sqlMap.executeQueryForList("getSelectTermLoanChargeList", objChargeTO);
                System.out.println("$#@$#@$@@ chargeCostList : " + chargeCostList);
                objChargeTO.setCharge_Type(expType);
                List chargeExpList = (List) sqlMap.executeQueryForList("getSelectTermLoanChargeList", objChargeTO);
                System.out.println("$#@$#@$@@chargeExpList : " + chargeExpList);
                if (chargeCostList != null && chargeCostList.size() > 0) {
                    HashMap chargeCostMap = (HashMap) chargeCostList.get(0);
                    String chargeNo = CommonUtil.convertObjToStr(chargeCostMap.get("CHARGE_NO"));
                    objChargeTO.setStatus(CommonConstants.STATUS_MODIFIED);
                    objChargeTO.setCharge_Type(costType);
                    objChargeTO.setAmount(CommonUtil.convertObjToDouble(fileCharge));
                    objChargeTO.setChargeGenerateNo(new Long(CommonUtil.convertObjToLong(chargeNo)));
                    sqlMap.executeUpdate("updateTermLoanChargeTO", objChargeTO);
                } else {
                    objChargeTO.setStatus(CommonConstants.STATUS_CREATED);
                    objChargeTO.setCharge_Type(costType);
                    objChargeTO.setAmount(CommonUtil.convertObjToDouble(fileCharge));
                    sqlMap.executeUpdate("insertTermLoanChargeTO", objChargeTO);
                }
                if (chargeExpList != null && chargeExpList.size() > 0) {
                    HashMap chargeExpMap = (HashMap) chargeExpList.get(0);
                    String chargeNo = CommonUtil.convertObjToStr(chargeExpMap.get("CHARGE_NO"));
                    objChargeTO.setStatus(CommonConstants.STATUS_MODIFIED);
                    objChargeTO.setCharge_Type(expType);
                    objChargeTO.setAmount((Double) CommonUtil.convertObjToDouble(miscCharge));
                    objChargeTO.setChargeGenerateNo(new Long(CommonUtil.convertObjToLong(chargeNo)));
                    sqlMap.executeUpdate("updateTermLoanChargeTO", objChargeTO);
                } else {
                    objChargeTO.setCharge_Type(expType);
                    objChargeTO.setAmount((Double) CommonUtil.convertObjToDouble(miscCharge));
                    sqlMap.executeUpdate("insertTermLoanChargeTO", objChargeTO);
                }

                objChargeTO.setCharge_Type(costType);
                chargeCostList = (List) sqlMap.executeQueryForList("getSelectTermLoanChargeList", objChargeTO);
                System.out.println("$#@$#@$@@ chargeCostList : " + chargeCostList);
                objChargeTO.setCharge_Type(expType);
                chargeExpList = (List) sqlMap.executeQueryForList("getSelectTermLoanChargeList", objChargeTO);
                System.out.println("$#@$#@$@@chargeExpList : " + chargeExpList);
                if (chargeCostList != null && chargeCostList.size() > 0) {
                    HashMap chargeCostMap = (HashMap) chargeCostList.get(0);
                    sqlMap.executeUpdate("updateChargeNoCostFromTermLoanAcctCharge", chargeCostMap);
                }
                if (chargeExpList != null && chargeExpList.size() > 0) {
                    HashMap chargeExpMap = (HashMap) chargeExpList.get(0);
                    sqlMap.executeUpdate("updateChargeNoExpFromTermLoanAcctCharge", chargeExpMap);
                }
            }
        }
    }

    public HashMap executeQuery(HashMap obj) throws Exception {
        return getData(obj);
    }

    private void destroyObjects() {
        caseDetailMap = null;
        collateralTableDetails = null;
        deletedCollateralTableValues = null;
        objCustomerGoldStockSecurityTO = null; // Added by nithya on 07-03-2020 for KD-1379
    }

    public static void main(String str[]) {
        try {
            MDSMasterMaintenanceDAO dao = new MDSMasterMaintenanceDAO();
            HashMap inputMap = new HashMap();

        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private void updateMdsSalarySecurity(MDSMasterMaintenanceTO MaintenanceTO) throws Exception {
        //To change body of generated methods, choose Tools | Templates.
        MaintenanceTO.setStatus("DELETED");
        MaintenanceTO.setStatusDt(CurrDt);
        MaintenanceTO.setStatusBy(MaintenanceTO.getStatusBy());
        sqlMap.executeUpdate("deleteMdsSalSec", MaintenanceTO);
        System.out.println("yooo " + SalarySecDetails.isEmpty());
        if (!SalarySecDetails.isEmpty()) {
            System.out.println("innn ");
            MaintenanceTO.setStatus("CREATED");
            insertMdsSalarySecurity(MaintenanceTO);
        }

    }

    private boolean checkGoldSecurity(MDSMasterMaintenanceTO MaintenanceTO) throws Exception {
        HashMap map = new HashMap();
        boolean flag = false;
        map.put("GROSS_WEIGHT", CommonUtil.convertObjToDouble(MaintenanceTO.getGrossWeight()));    //AJITH
        map.put("NET_WEIGHT", CommonUtil.convertObjToDouble(MaintenanceTO.getNetWeight()));    //AJITH
        map.put("PARTICULARS", CommonUtil.convertObjToStr(MaintenanceTO.getJewellaryDetails()));
        map.put("ACCT_NUM", CommonUtil.convertObjToStr(MaintenanceTO.getChittalNo()));
        List lst = sqlMap.executeQueryForList("getGoldSecurityExist", map);
        System.out.println("checkGoldSecurity##" + lst);
        if (lst != null && lst.size() > 0) {
                map = (HashMap) lst.get(0);
                flag = true;
        }else{
            flag = false;
        }
        return flag;
    }
    
    private void updateMdsGoldSecurity(MDSMasterMaintenanceTO MaintenanceTO) throws Exception {
        //To change body of generated methods, choose Tools | Templates.
        if (MaintenanceTO.getGoldValue() != null && CommonUtil.convertObjToDouble(MaintenanceTO.getGoldValue()) > 0) {
                GoldLoanSecurityTO objTermLoanSecurityTO = new GoldLoanSecurityTO();
                objTermLoanSecurityTO.setSlNo(CommonUtil.convertObjToInt("0"));
                objTermLoanSecurityTO.setAcctNum(MaintenanceTO.getChittalNo());
                objTermLoanSecurityTO.setStatus(CommonConstants.STATUS_CREATED);
                objTermLoanSecurityTO.setAsOn((Date)CurrDt.clone());
                objTermLoanSecurityTO.setGrossWeight(CommonUtil.convertObjToDouble(MaintenanceTO.getGrossWeight()));   //AJITH
                objTermLoanSecurityTO.setNetWeight(CommonUtil.convertObjToDouble(MaintenanceTO.getNetWeight()));   //AJITH
                objTermLoanSecurityTO.setPurity("");
                objTermLoanSecurityTO.setMarketRate("");
                objTermLoanSecurityTO.setSecurityValue(CommonUtil.convertObjToStr(MaintenanceTO.getGoldValue()));   ///check
                objTermLoanSecurityTO.setMargin("");
                objTermLoanSecurityTO.setMarginAmt("");
                objTermLoanSecurityTO.setEligibleLoanAmt(CommonUtil.convertObjToStr(MaintenanceTO.getPrizedAmount()));
                objTermLoanSecurityTO.setAppraiserId(""); //getCbmRenewalAppraiserId().getKeyForSelected()));
                objTermLoanSecurityTO.setParticulars(MaintenanceTO.getJewellaryDetails());
                objTermLoanSecurityTO.setStatusBy(MaintenanceTO.getStatusBy());
                objTermLoanSecurityTO.setStatusDt(getProperDateFormat(MaintenanceTO.getStatusDt()));
                objTermLoanSecurityTO.setNoofPacket(CommonUtil.convertObjToInt("1"));
            if (objCustomerGoldStockSecurityTO != null && objCustomerGoldStockSecurityTO.getPledgeAmount() > 0 && checkGoldStockSecurityForLoan(objCustomerGoldStockSecurityTO)) {                          
                System.out.println("update objCustomerGoldStockSecurityTO :: " + objCustomerGoldStockSecurityTO);
                HashMap releaseMap = new HashMap();
                releaseMap.put("RELEASE_DT", CurrDt.clone());
                releaseMap.put("ACCT_NUM", objCustomerGoldStockSecurityTO.getAcctNum());
                sqlMap.executeUpdate("updateLoansSecurityGoldStockReleaseDt", releaseMap);
                sqlMap.executeUpdate("insertLoansSecurityGoldStockTO", objCustomerGoldStockSecurityTO);
            }else if(checkGoldSecurity(MaintenanceTO)){
                double netwght = CommonUtil.convertObjToDouble(MaintenanceTO.getNetWeight());
                double grossWght = CommonUtil.convertObjToDouble(MaintenanceTO.getGrossWeight());
                if (netwght == 0 && grossWght == 0) {// Added by nithya on 24-11-2016
                    objTermLoanSecurityTO.setIsRelease("Y");
                    objTermLoanSecurityTO.setReleaseDt((Date) CurrDt.clone());
                    sqlMap.executeUpdate("updateGoldReleaseStatus",objTermLoanSecurityTO);
                }else{
                    sqlMap.executeUpdate("insertGoldLoanSecurityTO", objTermLoanSecurityTO);                    
                    //Added by sreekrishnan
                    objTermLoanSecurityTO.setIsRelease("Y");
                    objTermLoanSecurityTO.setReleaseDt((Date) CurrDt.clone());
                    sqlMap.executeUpdate("updateGoldReleaseStatus", objTermLoanSecurityTO);
                }                
            }else{
                sqlMap.executeUpdate("insertGoldLoanSecurityTO",objTermLoanSecurityTO);
            }
        }
    }
    
    private void insertMdsSalarySecurity(MDSMasterMaintenanceTO MaintenanceTO) throws Exception {
        for (int i = 0; i < SalarySecDetails.size(); i++) {
            HashMap docMap = new HashMap();
            docMap = (HashMap) SalarySecDetails.get(i);

            MaintenanceTO.setSalaryCerficateNo(docMap.get("SALARY_CERTIFICATE_NO").toString());
            MaintenanceTO.setEmpMemberNo(docMap.get("EMP_MEMBER_NO").toString());
            MaintenanceTO.setEmpAddress(docMap.get("EMP_ADDRESS").toString());
            MaintenanceTO.setCity(docMap.get("CITY").toString());
            MaintenanceTO.setSlno(docMap.get("SL_NO").toString());
            MaintenanceTO.setPin(CommonUtil.convertObjToInt(docMap.get("PIN"))); //AJITH
            MaintenanceTO.setDesignation(docMap.get("DESIGNATION").toString());
            MaintenanceTO.setContactNo(docMap.get("CONTACT_NO").toString());
           // MaintenanceTO.setRetirementDt(DateUtil.getDateMMDDYYYY(docMap.get("RETIREMENT_DT").toString()));            
            MaintenanceTO.setRetirementDt(getProperDateFormat(docMap.get("RETIREMENT_DT")));        
            MaintenanceTO.setEmpName(docMap.get("EMP_NAME").toString());
            MaintenanceTO.setTotalSalary(CommonUtil.convertObjToDouble(docMap.get("TOTAL_SALARY"))); //AJITH
            MaintenanceTO.setNetworth(docMap.get("NETWORTH").toString());
            MaintenanceTO.setSalaryRemarks(docMap.get("REMARKS").toString());

            sqlMap.executeUpdate("insertMdsSalSecDetails", MaintenanceTO);

            MaintenanceTO.setSalaryCerficateNo("");
            MaintenanceTO.setEmpMemberNo("");
            MaintenanceTO.setEmpAddress("");
            MaintenanceTO.setCity("");
            MaintenanceTO.setSlno("");
            MaintenanceTO.setPin(null); //AJITH
            MaintenanceTO.setDesignation("");
            MaintenanceTO.setContactNo("");
            MaintenanceTO.setRetirementDt(null);
            MaintenanceTO.setEmpName("");
            MaintenanceTO.setTotalSalary(null); //AJITH
            MaintenanceTO.setNetworth("");
            MaintenanceTO.setSalaryRemarks("");
        }
        //To change body of generated methods, choose Tools | Templates.
    }
    
    private boolean checkGoldStockSecurityForLoan(LoansSecurityGoldStockTO objLoansSecurityGoldStockTO) throws Exception {
        HashMap map = new HashMap();
        boolean flag = false;        
        map.put("GOLD_STOCK_ID", CommonUtil.convertObjToStr(objLoansSecurityGoldStockTO.getGoldStockId()));
        map.put("ACCT_NUM", CommonUtil.convertObjToStr(objLoansSecurityGoldStockTO.getAcctNum()));
        List lst = sqlMap.executeQueryForList("getGoldStockSecurityExistForLoan", map);       
        if (lst != null && lst.size() > 0) {
               flag = false;
        }else{
            flag = true;
        }
        return flag;
    }
}
