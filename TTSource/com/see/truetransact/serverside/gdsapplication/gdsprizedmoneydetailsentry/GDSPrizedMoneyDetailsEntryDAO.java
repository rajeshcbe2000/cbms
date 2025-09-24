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
package com.see.truetransact.serverside.gdsapplication.gdsprizedmoneydetailsentry;

import com.see.truetransact.serverside.mdsapplication.mdsprizedmoneydetailsentry.*;
import java.util.List;
import java.util.ArrayList;
import java.util.HashMap;
import org.apache.log4j.Logger;
import com.ibatis.db.sqlmap.SqlMap;
import com.see.truetransact.serverutil.ServerUtil;
import com.see.truetransact.commonutil.CommonConstants;
import com.see.truetransact.commonutil.NoCommandException;
import com.see.truetransact.transferobject.product.operativeacct.*;
import com.see.truetransact.transferobject.mdsapplication.mdsprizedmoneydetailsentry.MDSPrizedMoneyDetailsEntryTO;
import com.see.truetransact.serverside.TTDAO;
import com.see.truetransact.serverutil.ServerConstants;
import com.see.truetransact.serverutil.ServerUtil;
import com.see.truetransact.servicelocator.ServiceLocator;
import com.see.truetransact.serverexception.ServiceLocatorException;
import com.see.truetransact.serverexception.TransRollbackException;
import com.see.truetransact.commonutil.CommonUtil;
import com.see.truetransact.commonutil.DateUtil;
import com.see.truetransact.commonutil.TTException;
// For Maintaining Logs...
import com.see.truetransact.serverside.common.log.LogDAO;
import com.see.truetransact.serverside.common.sms.SmsConfigDAO;
import com.see.truetransact.serverside.common.transaction.TransferTrans;
import com.see.truetransact.serverside.transaction.common.TransactionFactory;
import com.see.truetransact.serverside.transaction.transfer.TransferDAO;
import com.see.truetransact.transferobject.common.log.LogTO;
import com.see.truetransact.transferobject.common.mobile.SMSSubscriptionTO;
import com.see.truetransact.transferobject.gdsapplication.gdsprizedmoneydetailsentry.GDSPrizedMoneyDetailsEntryTO;
import com.see.truetransact.transferobject.transaction.transfer.TxTransferTO;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.File;
import java.util.Date;
import java.util.Iterator;
import java.util.Map;
import javax.print.*;
import org.w3c.dom.*;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

/**
 * This is used for OperativeAcctProductDAO Data Access.
 *
 * @author Balachandar
 *
 * @modified Pinky @modified Rahul
 */
public class GDSPrizedMoneyDetailsEntryDAO extends TTDAO {

    private static SqlMap sqlMap = null;
    private GDSPrizedMoneyDetailsEntryTO PrizedMoneyDetailsEntryTo;
    private LogDAO logDAO;
    private LogTO logTO;
    private final static Logger log = Logger.getLogger(GDSPrizedMoneyDetailsEntryDAO.class);
    private Iterator smsIterator;
    public static String fileName = "/MDSsmsRemidertemplate.xml";
    private Map cache; 
    TransferDAO transferDAO = new TransferDAO();
    private Date currDt = null;
    private HashMap execReturnMap = null;
    
    /**
     * Creates a new instance of OperativeAcctProductDAO
     */
    public GDSPrizedMoneyDetailsEntryDAO() throws ServiceLocatorException {
        ServiceLocator locate = ServiceLocator.getInstance();
        sqlMap = (SqlMap) locate.getDAOSqlMap();
    }

    private HashMap getData(HashMap map) throws Exception {
        System.out.println("@@@@@@@map" + map);
        HashMap returnMap = new HashMap();
        List list = null;
        if(map.get("PRIZED_TYPE")!= null && !map.get("PRIZED_TYPE").equals("") && map.get("PRIZED_TYPE").equals("NO_AUCTION")){
            list = (List) sqlMap.executeQueryForList("getSelectMDSPrizedMoneyDetailsEntryTOForNoAuction", map);
        }else{
            if(map.containsKey("PREDEFINITION_INSTALLMENT") && !(map.get("PREDEFINITION_INSTALLMENT").equals("Y"))){
                map.put("SUB_NO", CommonUtil.convertObjToInt(map.get("SUB_NO")));
            }
           if(map.containsKey("SUB_NO") && map.get("SUB_NO") != null){
                map.put("SUB_NO",CommonUtil.convertObjToInt(map.get("SUB_NO")));
            }
            list = (List) sqlMap.executeQueryForList("getGDSSelectMDSPrizedMoneyDetailsEntryTO", map);
           
        }        
        returnMap.put("PrizedMoneyDetailsTO", list);
        return returnMap;
    }

    public HashMap execute(HashMap map) throws Exception {

        System.out.println("Map in DAO: " + map);
      
        HashMap returnMap = new HashMap();
        execReturnMap = new HashMap();
        _branchCode = (String) map.get(CommonConstants.BRANCH_ID);
        currDt = ServerUtil.getCurrentDate(_branchCode);
        logDAO = new LogDAO();
        logTO = new LogTO();
        PrizedMoneyDetailsEntryTo = (GDSPrizedMoneyDetailsEntryTO) map.get("GDSPrizedMoneyDetailsEntry");
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
        return execReturnMap;
    }
        
    private void insertData(HashMap map) throws Exception {
        try {
            sqlMap.startTransaction();
            HashMap slMap = new HashMap();
            System.out.println("in insertData map  "+map);
            System.out.println("in insertData PrizedMoneyDetailsEntryTo  "+PrizedMoneyDetailsEntryTo);
            if(PrizedMoneyDetailsEntryTo.getGds_no()!=null)
            {
                
               // slMap.put("SCHEME_NAME", PrizedMoneyDetailsEntryTo.getMdsSchemeName());
            slMap.put("DIVISION_NO", PrizedMoneyDetailsEntryTo.getDivisionNo());
            slMap.put("GROUP_NO", PrizedMoneyDetailsEntryTo.getMdsSchemeName());
            slMap.put("GDS_NO", PrizedMoneyDetailsEntryTo.getGds_no());
                List applicationLst = sqlMap.executeQueryForList("getGDSApplicationForGDSNo", slMap);
//                PrizedMoneyDetailsEntryTo.setPrizedAmount(CommonUtil.convertObjToStr(CommonUtil.convertObjToDouble(PrizedMoneyDetailsEntryTo.getPrizedAmount())/applicationLst.size()));
//                PrizedMoneyDetailsEntryTo.setTotalBonusAmount(CommonUtil.convertObjToStr(CommonUtil.convertObjToDouble(PrizedMoneyDetailsEntryTo.getTotalBonusAmount())/applicationLst.size()));
//                PrizedMoneyDetailsEntryTo.setNextBonusAmount(CommonUtil.convertObjToStr(CommonUtil.convertObjToDouble(PrizedMoneyDetailsEntryTo.getTotalBonusAmount())/CommonUtil.convertObjToDouble(PrizedMoneyDetailsEntryTo.getNoOfMemberPerDiv())));
//                PrizedMoneyDetailsEntryTo.setNetAmountPayable(CommonUtil.convertObjToStr(CommonUtil.convertObjToDouble(PrizedMoneyDetailsEntryTo.getNetAmountPayable())/applicationLst.size()));
//                PrizedMoneyDetailsEntryTo.setCommisionAmount(CommonUtil.convertObjToStr(CommonUtil.convertObjToDouble(PrizedMoneyDetailsEntryTo.getCommisionAmount())/applicationLst.size()));
//                PrizedMoneyDetailsEntryTo.setInstallAmountPaid(CommonUtil.convertObjToStr(CommonUtil.convertObjToDouble(PrizedMoneyDetailsEntryTo.getInstallAmountPaid())/applicationLst.size()));

                PrizedMoneyDetailsEntryTo.setPrizedAmount(CommonUtil.convertObjToDouble(PrizedMoneyDetailsEntryTo.getPrizedAmount()/applicationLst.size()));
                PrizedMoneyDetailsEntryTo.setTotalBonusAmount(CommonUtil.convertObjToDouble(PrizedMoneyDetailsEntryTo.getTotalBonusAmount()/applicationLst.size()));
                PrizedMoneyDetailsEntryTo.setNextBonusAmount(CommonUtil.convertObjToDouble(PrizedMoneyDetailsEntryTo.getTotalBonusAmount()/CommonUtil.convertObjToDouble(PrizedMoneyDetailsEntryTo.getNoOfMemberPerDiv())));
                PrizedMoneyDetailsEntryTo.setNetAmountPayable(CommonUtil.convertObjToDouble(PrizedMoneyDetailsEntryTo.getNetAmountPayable()/applicationLst.size()));
                PrizedMoneyDetailsEntryTo.setCommisionAmount(CommonUtil.convertObjToDouble(PrizedMoneyDetailsEntryTo.getCommisionAmount()/applicationLst.size()));
                PrizedMoneyDetailsEntryTo.setInstallAmountPaid(CommonUtil.convertObjToDouble(PrizedMoneyDetailsEntryTo.getInstallAmountPaid()/applicationLst.size()));

               

                for(int i=0;i<applicationLst.size();i++)
                {
                    HashMap applicationMap=(HashMap)applicationLst.get(i);
                    String schemeName=CommonUtil.convertObjToStr(applicationMap.get("SCHEME_NAME"));
                    String cittalNo=CommonUtil.convertObjToStr(applicationMap.get("CHITTAL_NO"));
                    slMap.put("SCHEME_NAME", schemeName);  //Set Co-Chittal SL_NO
                    slMap.put("DIVISION_NO", PrizedMoneyDetailsEntryTo.getDivisionNo());
                    slMap.put("GDS_NO", PrizedMoneyDetailsEntryTo.getGds_no());
                    slMap.put("CHITTAL_NO",cittalNo );
                    PrizedMoneyDetailsEntryTo.setChittalNo(CommonUtil.convertObjToStr(cittalNo));
                    PrizedMoneyDetailsEntryTo.setMdsSchemeName(CommonUtil.convertObjToStr(schemeName));
                    List lst = sqlMap.executeQueryForList("getGDSSchemeNameDetailsMaxSlNo", slMap);
            if (lst != null && lst.size() > 0) {
                slMap = (HashMap) lst.get(0);
                double slNO = CommonUtil.convertObjToDouble(slMap.get("SL_NO")).doubleValue() + 1;
                PrizedMoneyDetailsEntryTo.setSlNo(new Double(slNO));
                
                slMap.put("SL_NO", PrizedMoneyDetailsEntryTo.getSlNo());
                slMap.put("DIVISION_NO", CommonUtil.convertObjToInt(PrizedMoneyDetailsEntryTo.getDivisionNo()));
                slMap.put("GDS_NO", PrizedMoneyDetailsEntryTo.getGds_no());
                slMap.put("SCHEME_NAME", schemeName);
                List slLst = sqlMap.executeQueryForList("getGDSCoChittalSlNo", slMap);
                if (slLst != null && slLst.size() > 0) {
                    slNO -= 1;
                    PrizedMoneyDetailsEntryTo.setSlNo(new Double(slNO));
                }
            }
            
            
            sqlMap.executeUpdate("insertGDSPrizedMoneyDetailsEntryTO", PrizedMoneyDetailsEntryTo);
            //Added by sreekrishnan
            if(PrizedMoneyDetailsEntryTo.getAuctionTrans()!=null && PrizedMoneyDetailsEntryTo.getAuctionTrans().equals("Y")){
                autionTransaction(map);
            }
            logTO.setData(PrizedMoneyDetailsEntryTo.toString());
            logTO.setPrimaryKey(PrizedMoneyDetailsEntryTo.getKeyData());
            logTO.setStatus(PrizedMoneyDetailsEntryTo.getStatus());
            logDAO.addToLog(logTO);
                }
                
                
            
            }
            
            sqlMap.commitTransaction();
        } catch (Exception e) {
            sqlMap.rollbackTransaction();
            log.error(e);
            throw new TransRollbackException(e);
        }
        map = null;
    }

 private void autionTransaction(HashMap map) throws Exception {
        if(PrizedMoneyDetailsEntryTo.getAuctionTrans()!=null && PrizedMoneyDetailsEntryTo.getAuctionTrans().equals("Y")){
            HashMap acHeads = new HashMap();
            HashMap noticeHead = new HashMap();
            TransferTrans transferTrans = new TransferTrans();
            HashMap txMap = new HashMap();
            String stampAdvances = "";
            ArrayList transferList = new ArrayList();
            System.out.println("acHeads notice:" + acHeads);
            TxTransferTO transferTo = null;
            double transAmt = 0;
            double amountTotal = 0;
            HashMap totAmt = new HashMap();
            String strAcNum = "";
            System.out.println("PrizedMoneyDetailsEntryTo%$$%$%$%$%$%$%" + PrizedMoneyDetailsEntryTo); 
            HashMap applicationMap = new HashMap();
            applicationMap.put("SCHEME_NAME", PrizedMoneyDetailsEntryTo.getMdsSchemeName());
            List lst = sqlMap.executeQueryForList("getSelectSchemeAcctHead", applicationMap);
            if (lst != null && lst.size() > 0) {
                applicationMap = (HashMap) lst.get(0);
            }
            
             //KD-3772
            String isBonusTrans = "Y";
            HashMap checkMap = new HashMap();
            checkMap.put("SCHEME_NAME", PrizedMoneyDetailsEntryTo.getMdsSchemeName());
            List list = sqlMap.executeQueryForList("getifBonusTransactionRequired", checkMap);
            if (list != null && list.size() > 0) {
                checkMap = (HashMap) list.get(0);
                if (checkMap.containsKey("IS_BONUS_TRANSFER") && checkMap.get("IS_BONUS_TRANSFER") != null && !"".equalsIgnoreCase(CommonUtil.convertObjToStr(checkMap.get("IS_BONUS_TRANSFER")))) {
                    isBonusTrans = CommonUtil.convertObjToStr(checkMap.get("IS_BONUS_TRANSFER"));
                }
            } 
            
             if (isBonusTrans.equals("Y")) {
                if (CommonUtil.convertObjToDouble(PrizedMoneyDetailsEntryTo.getTotalBonusAmount()) > 0) {
                    txMap = new HashMap();
                    transferTo = new TxTransferTO();
                    txMap.put(TransferTrans.CR_PROD_TYPE, TransactionFactory.GL);
                    txMap.put(TransferTrans.CR_AC_HD, (String) applicationMap.get("BONUS_PAYABLE_HEAD"));
                    txMap.put(TransferTrans.CR_BRANCH, _branchCode);
                    txMap.put(TransferTrans.CURRENCY, "INR");
                    txMap.put("AUTHORIZE_STATUS_2", "ENTERED_AMOUNT");
                    txMap.put(TransferTrans.PARTICULARS, PrizedMoneyDetailsEntryTo.getChittalNo() + "_" + PrizedMoneyDetailsEntryTo.getSubNo() + " BONUS");
                    txMap.put("TRANS_MOD_TYPE", "MDS");
                    txMap.put("SCREEN_NAME", "MDS_PRIZED_MONEY_DETAILS");
                    txMap.put("GL_TRANS_ACT_NUM", CommonUtil.convertObjToStr(PrizedMoneyDetailsEntryTo.getChittalNo()));
                    txMap.put(CommonConstants.USER_ID, PrizedMoneyDetailsEntryTo.getStatusBy());
                    transferTrans.setInitiatedBranch(_branchCode);
                    transferTo = transferTrans.getCreditTransferTO(txMap, CommonUtil.convertObjToDouble(PrizedMoneyDetailsEntryTo.getTotalBonusAmount()));
                    transferList.add(transferTo);
                }
            }
            if(CommonUtil.convertObjToDouble(PrizedMoneyDetailsEntryTo.getCommisionAmount())>0){
                txMap = new HashMap();
                transferTo = new TxTransferTO();
                txMap.put(TransferTrans.CR_PROD_TYPE, TransactionFactory.GL);
                txMap.put(TransferTrans.CR_AC_HD, (String) applicationMap.get("COMMISION_HEAD"));
                txMap.put(TransferTrans.CR_BRANCH, _branchCode);
                txMap.put(TransferTrans.CURRENCY, "INR");
                txMap.put("AUTHORIZE_STATUS_2", "ENTERED_AMOUNT");
                txMap.put(TransferTrans.PARTICULARS, PrizedMoneyDetailsEntryTo.getChittalNo() + "_" + PrizedMoneyDetailsEntryTo.getSubNo() + " COMMISION");
                txMap.put("TRANS_MOD_TYPE", "MDS");
                txMap.put("SCREEN_NAME", "MDS_PRIZED_MONEY_DETAILS");
                txMap.put("GL_TRANS_ACT_NUM", CommonUtil.convertObjToStr(PrizedMoneyDetailsEntryTo.getChittalNo()));
                txMap.put(CommonConstants.USER_ID, PrizedMoneyDetailsEntryTo.getStatusBy());
                transferTrans.setInitiatedBranch(_branchCode);
                transferTo = transferTrans.getCreditTransferTO(txMap, CommonUtil.convertObjToDouble(PrizedMoneyDetailsEntryTo.getCommisionAmount()));    
                transferList.add(transferTo);
            }
            if (isBonusTrans.equals("Y")) {
                if (CommonUtil.convertObjToDouble(PrizedMoneyDetailsEntryTo.getTotalBonusAmount())
                        + CommonUtil.convertObjToDouble(PrizedMoneyDetailsEntryTo.getCommisionAmount()) > 0) {
                    txMap = new HashMap();
                    transferTo = new TxTransferTO();
                    txMap.put(TransferTrans.DR_PROD_TYPE, TransactionFactory.GL);
                    txMap.put(TransferTrans.DR_AC_HD, (String) applicationMap.get("PAYMENT_HEAD"));
                    txMap.put(TransferTrans.DR_BRANCH, _branchCode);
                    txMap.put(TransferTrans.CURRENCY, "INR");
                    txMap.put("AUTHORIZE_STATUS_2", "ENTERED_AMOUNT");
                    txMap.put(TransferTrans.PARTICULARS, PrizedMoneyDetailsEntryTo.getChittalNo() + "_" + PrizedMoneyDetailsEntryTo.getSubNo() + " NET AMOUNT");
                    txMap.put("TRANS_MOD_TYPE", "MDS");
                    txMap.put("SCREEN_NAME", "MDS_PRIZED_MONEY_DETAILS");
                    txMap.put("GL_TRANS_ACT_NUM", CommonUtil.convertObjToStr(PrizedMoneyDetailsEntryTo.getChittalNo()));
                    txMap.put(CommonConstants.USER_ID, PrizedMoneyDetailsEntryTo.getStatusBy());
                    transferTrans.setInitiatedBranch(_branchCode);
                    transferTo = transferTrans.getDebitTransferTO(txMap, CommonUtil.convertObjToDouble(PrizedMoneyDetailsEntryTo.getTotalBonusAmount())
                            + CommonUtil.convertObjToDouble(PrizedMoneyDetailsEntryTo.getCommisionAmount()));
                    transferList.add(transferTo);
                }
            }else{
                 if (CommonUtil.convertObjToDouble(PrizedMoneyDetailsEntryTo.getCommisionAmount()) > 0) {
                    txMap = new HashMap();
                    transferTo = new TxTransferTO();
                    txMap.put(TransferTrans.DR_PROD_TYPE, TransactionFactory.GL);
                    txMap.put(TransferTrans.DR_AC_HD, (String) applicationMap.get("PAYMENT_HEAD"));
                    txMap.put(TransferTrans.DR_BRANCH, _branchCode);
                    txMap.put(TransferTrans.CURRENCY, "INR");
                    txMap.put("AUTHORIZE_STATUS_2", "ENTERED_AMOUNT");
                    txMap.put(TransferTrans.PARTICULARS, PrizedMoneyDetailsEntryTo.getChittalNo() + "_" + PrizedMoneyDetailsEntryTo.getSubNo() + " NET AMOUNT");
                    txMap.put("TRANS_MOD_TYPE", "MDS");
                    txMap.put("SCREEN_NAME", "MDS_PRIZED_MONEY_DETAILS");
                    txMap.put("GL_TRANS_ACT_NUM", CommonUtil.convertObjToStr(PrizedMoneyDetailsEntryTo.getChittalNo()));
                    txMap.put(CommonConstants.USER_ID, PrizedMoneyDetailsEntryTo.getStatusBy());
                    transferTrans.setInitiatedBranch(_branchCode);
                    transferTo = transferTrans.getDebitTransferTO(txMap, 
                             CommonUtil.convertObjToDouble(PrizedMoneyDetailsEntryTo.getCommisionAmount()));
                    transferList.add(transferTo);
                }
             }
            if (transferList != null && transferList.size() > 0) {
                System.out.println("transferList  eeee####" + transferList);
                //transferTrans.doDebitCredit(transferList, _branchCode, false);
                HashMap transMap = new HashMap();
                transMap.put("MODE", CommonConstants.TOSTATUS_INSERT);
                transMap.put("COMMAND", CommonConstants.TOSTATUS_INSERT);
                transMap.put("TxTransferTO", transferList);
                transMap.put(CommonConstants.BRANCH_ID, _branchCode);
                transMap = transferDAO.execute(transMap, false);
                System.out.println("transMap X:"+transMap);
                HashMap linkBatchMap = new HashMap();
                linkBatchMap.put("LINK_BATCH_ID", transMap.get("TRANS_ID"));
                linkBatchMap.put("BATCH_ID", transMap.get("TRANS_ID"));
                linkBatchMap.put("TRANS_DT", currDt);
                linkBatchMap.put("INITIATED_BRANCH", _branchCode);
                sqlMap.executeUpdate("updateLinkBatchIdTransfer", linkBatchMap);
                getTransDetails(CommonUtil.convertObjToStr(transMap.get("TRANS_ID")));
                linkBatchMap = null;
                transMap = null;                
            }
        }
    }
        
    private void getTransDetails(String batchId) throws Exception {
        HashMap getTransMap = new HashMap();
        getTransMap.put("BATCH_ID", batchId);
        getTransMap.put("TRANS_DT", currDt);
        getTransMap.put(CommonConstants.BRANCH_ID, _branchCode);
        getTransMap.put("AUTHORIZE_STATUS", "AUTHORIZE_STATUS");
        //System.out.println("########getTransferTransBatchID returnMap depCloseAmt:"+resultMap);
        //returnMap = new HashMap();
        List transList = (List) sqlMap.executeQueryForList("getTransferDetails", getTransMap);
        if (transList != null && transList.size() > 0) {
            execReturnMap.put("TRANSFER_TRANS_LIST", transList);
        }
        List cashList = (List) sqlMap.executeQueryForList("getCashDetails", getTransMap);
        if (cashList != null && cashList.size() > 0) {
            execReturnMap.put("CASH_TRANS_LIST", cashList);
        }
        getTransMap.clear();
        getTransMap = null;
        transList = null;
        cashList = null;
    }
    
    private void updateData(HashMap map) throws Exception {
        try {
            sqlMap.startTransaction(); 
            System.out.println("PrizedMoneyDetailsEntryTo#%##%#%"+PrizedMoneyDetailsEntryTo);
   
            HashMap slMap = new HashMap();
            slMap.put("DIVISION_NO", CommonUtil.convertObjToInt(PrizedMoneyDetailsEntryTo.getDivisionNo()));
            slMap.put("GROUP_NO", PrizedMoneyDetailsEntryTo.getMdsSchemeName());
            slMap.put("GDS_NO", PrizedMoneyDetailsEntryTo.getGds_no());
            List applicationLst = sqlMap.executeQueryForList("getGDSApplicationForGDSNo", slMap);
            for (int i = 0; i < applicationLst.size(); i++) {
                HashMap applicationMap = (HashMap) applicationLst.get(i);
                String schemeName = CommonUtil.convertObjToStr(applicationMap.get("SCHEME_NAME"));
                String cittalNo = CommonUtil.convertObjToStr(applicationMap.get("CHITTAL_NO"));
                PrizedMoneyDetailsEntryTo.setChittalNo(cittalNo);
                PrizedMoneyDetailsEntryTo.setMdsSchemeName(schemeName);
                sqlMap.executeUpdate("updateGDSPrizedMoneyDetailsEntryTO", PrizedMoneyDetailsEntryTo);
            }           
            
            logTO.setData(PrizedMoneyDetailsEntryTo.toString());
            logTO.setPrimaryKey(PrizedMoneyDetailsEntryTo.getKeyData());
            logTO.setStatus(PrizedMoneyDetailsEntryTo.getStatus());
            logDAO.addToLog(logTO);
            sqlMap.commitTransaction();
        } catch (Exception e) {
            sqlMap.rollbackTransaction();
            log.error(e);
            throw new TransRollbackException(e);
        }
        map = null;
    }

    private void deleteData(HashMap map) throws Exception {
        try {
            sqlMap.startTransaction();
            sqlMap.executeUpdate("deleteMDSPrizedMoneyDetailsEntryTO", PrizedMoneyDetailsEntryTo);
            logTO.setData(PrizedMoneyDetailsEntryTo.toString());
            logTO.setPrimaryKey(PrizedMoneyDetailsEntryTo.getKeyData());
            logTO.setStatus(PrizedMoneyDetailsEntryTo.getStatus());
            logDAO.addToLog(logTO);
            sqlMap.commitTransaction();
        } catch (Exception e) {
            sqlMap.rollbackTransaction();
            log.error(e);
            throw new TransRollbackException(e);
        }
        map = null;
    }

    private void authorize(HashMap map) throws Exception {
        ArrayList selectedList = (ArrayList) map.get(CommonConstants.AUTHORIZEDATA);
        HashMap AuthMap = new HashMap();
        HashMap smsMap = new HashMap();
        HashMap smsDataMap = new HashMap();
        SmsConfigDAO smsConDao = new SmsConfigDAO();
        AuthMap = (HashMap) selectedList.get(0);
        System.out.println("@@@@@@@AuthMap" + AuthMap);
        try {
            sqlMap.startTransaction();
            logTO.setData(map.toString());
            sqlMap.executeUpdate("authorizeGDSPrizedMoneyDetails", AuthMap);
            //Added by sreekrishnsn for sms remider..         
            if(AuthMap.get("STATUS").equals(CommonConstants.STATUS_AUTHORIZED)){
                List smsList = sqlMap.executeQueryForList("getDetailsForPrizedDetailsSMS", AuthMap);
                if (smsList != null && smsList.size() > 0) {
                    for(int i = 0;i<smsList.size();i++){
                        smsMap = (HashMap) smsList.get(i);
                        String chittalNo = CommonUtil.convertObjToStr(smsMap.get("CHITTAL_NO"));
                        smsDataMap.put(chittalNo, smsMap);
                    }
                    System.out.println("smsMap#%#%#%#%#%"+smsMap);
                    if (smsDataMap.size() > 0) {
                        HashMap smsData = new HashMap();
                        smsData.put("SMS", smsDataMap);
                        smsData.put("MDS_PRIZED_REMIDER", "");
                        smsData.put(CommonConstants.BRANCH_ID,_branchCode);
                        //smsConDao.MdsSmsConfiguration(smsData);
                    }                    
                }
            }
            //Added by sreekrishnan
            if(AuthMap.get("STATUS").equals(CommonConstants.STATUS_AUTHORIZED) ||  AuthMap.get("STATUS").equals(CommonConstants.STATUS_REJECTED)){
                if(!AuthMap.get("TRANS_ID").equals("") && AuthMap.get("TRANS_ID")!=null){
                    HashMap transferTransParam = new HashMap();
                    transferDAO = new TransferDAO();
                    transferTransParam.put(CommonConstants.BRANCH_ID, CommonUtil.convertObjToStr(AuthMap.get("BRANCH_CODE")));
                    transferTransParam.put(CommonConstants.USER_ID, AuthMap.get(CommonConstants.USER_ID));
                    transferTransParam.put("LINK_BATCH_ID", CommonUtil.convertObjToStr(AuthMap.get("TRANS_ID")));                        
                    ArrayList transferTransList = (ArrayList) sqlMap.executeQueryForList("getAuthBatchTxTransferTOs", transferTransParam);
                    if (transferTransList != null) {
                        String batchId = ((TxTransferTO) transferTransList.get(0)).getBatchId();
                        System.out.println("###@@# batchId : " + batchId);
                        HashMap transAuthMap = new HashMap();
                        transAuthMap.put("BATCH_ID", batchId);
                        transAuthMap.put(CommonConstants.AUTHORIZESTATUS, CommonUtil.convertObjToStr(AuthMap.get("STATUS")));
                        transAuthMap.put(CommonConstants.AUTHORIZEDATA, transferTransList);
                        transferTransParam.put(CommonConstants.AUTHORIZEMAP, transAuthMap);
                        transferDAO.execute(transferTransParam, false);
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
    
    public static void main(String str[]) {
        try {
            GDSPrizedMoneyDetailsEntryDAO dao = new GDSPrizedMoneyDetailsEntryDAO();

        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public HashMap executeQuery(HashMap obj) throws Exception {
        _branchCode = (String) obj.get(CommonConstants.BRANCH_ID);
        return getData(obj);
    }

    private void destroyObjects() {
        PrizedMoneyDetailsEntryTo = null;
    }
}
