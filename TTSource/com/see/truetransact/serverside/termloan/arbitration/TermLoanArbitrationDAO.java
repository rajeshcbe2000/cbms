/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition.
 **
 *
 * AgentDAO.java
 *
 * Created on Wed Feb 02 13:11:28 IST 2005
 */
package com.see.truetransact.serverside.termloan.arbitration;

import com.see.truetransact.serverside.termloan.charges.*;
import java.util.List;
import java.util.HashMap;
import java.util.Map;
import java.util.ArrayList;
import java.util.Date;
import java.util.Set;
import java.util.Iterator;
import com.ibatis.db.sqlmap.SqlMap;
import com.see.truetransact.serverutil.ServerUtil;
import com.see.truetransact.commonutil.CommonConstants;
import com.see.truetransact.commonutil.CommonUtil;
import com.see.truetransact.commonutil.DateUtil;
import com.see.truetransact.serverside.TTDAO;
import com.see.truetransact.servicelocator.ServiceLocator;
import com.see.truetransact.serverexception.ServiceLocatorException;
import com.see.truetransact.transferobject.common.mobile.SMSSubscriptionTO;
import com.see.truetransact.transferobject.termloan.chargesTo.TermLoanChargesTO;
import com.see.truetransact.serverside.batchprocess.task.TaskHeader;
import com.see.truetransact.serverside.batchprocess.task.authorizechk.InterestCalculationTask;
import com.see.truetransact.serverside.common.transaction.TransferTrans;
import com.see.truetransact.transferobject.transaction.transfer.TxTransferTO;
import com.see.truetransact.serverside.common.transaction.TransactionDAO;
import com.see.truetransact.serverside.transaction.common.TransactionFactory;
import com.see.truetransact.transferobject.termloan.loansubsidy.TermLoanSubsidyTO;
import com.see.truetransact.transferobject.termloan.TermLoanFacilityTO;
import com.see.truetransact.serverside.common.idgenerate.IDGenerateDAO;
import com.see.truetransact.commonutil.TTException;
import com.see.truetransact.transferobject.termloan.arbitration.TermLoanArbitrationTO;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.File;
import javax.print.*;
import org.w3c.dom.*;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

/**
 * Agent DAO.
 *
 */
public class TermLoanArbitrationDAO extends TTDAO {

    public static String fileName = "/smstemplate.xml";
    private static SqlMap sqlMap = null;
    private double paid_interest = 0;
    private double paid_penal_int = 0;
    private double paid_principal = 0;
    private Iterator smsIterator;
    private TransactionDAO transactionDAO;
    private HashMap prodMap = new HashMap();
    private Date currDt = null;
    private Date auctionDt = null;
    private String user = "";
    private Map cache;                  //used to hold references to Resources for re-use
    private String chitsNo = "";
    private String arbid = "";
    private HashMap returnMap;
    private boolean epEditChk = false;

    /**
     * Creates a new instance of AgentDAO
     */
    public TermLoanArbitrationDAO() throws ServiceLocatorException {
        ServiceLocator locate = ServiceLocator.getInstance();
        sqlMap = (SqlMap) locate.getDAOSqlMap();
    }

    public static void main(String str[]) {
        try {
            TermLoanChargesDAO dao = new TermLoanChargesDAO();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public HashMap execute(HashMap map) throws Exception {
        System.out.println("TermLoanCharges Map Dao : " + map);
        returnMap = new HashMap();
        _branchCode = (String) map.get(CommonConstants.BRANCH_ID);
        currDt = ServerUtil.getCurrentDate(_branchCode);
        user = CommonUtil.convertObjToStr(map.get(CommonConstants.USER_ID));
        try {
            sqlMap.startTransaction();
            if (map.containsKey("ARBITRATION")) {
                if (map.containsKey("ARBITRATION_UPDATE")) {  // added by nithya on 10-03-2016
                    updateArbitrationAfterProcess(map);
                    //loan act charge insertion
                    startSubsidyTransaction(map);
                } else {
                    insertArbitration(map);
                }
            }
            if (map.containsKey("AWARD")) {
                updateArbitration(map);
            }
            if (map.containsKey("EP_FILING")) {
                String prodtype = CommonUtil.convertObjToStr(map.get("PROD_TYPE"));
                insertEPFiling(map);
                // if(!prodtype.equals("MDS"))
                startSubsidyTransactionEp(map);
            }
            if (map.containsKey("DELETE_ARC")) { // nithya
                deleteArbitration(map);
            }
            sqlMap.commitTransaction();
        } catch (Exception e) {
            e.printStackTrace();
            sqlMap.rollbackTransaction();
            throw e;
        }
        epEditChk = false;
        ServiceLocator.flushCache(sqlMap);
        System.gc();
        returnMap.put("ARC_ID", arbid);
        return (returnMap);
    }

    private void updateArbitration(HashMap arbMap) {
        try {
            if (arbMap.containsKey("AWARD_LIST")) {
                TermLoanArbitrationTO objarbTo = new TermLoanArbitrationTO();
                TermLoanChargesTO objChargeTO = null;
                ArrayList arbList;
                arbList = (ArrayList) arbMap.get("AWARD_LIST");
                if (arbList.size() > 0) {
                    for (int i = 0; i < arbList.size(); i++) {
                        ArrayList newList = (ArrayList) arbList.get(i);
                        objarbTo.setAct_num(CommonUtil.convertObjToStr(newList.get(0)));
                        objarbTo.setArbitDate(DateUtil.getDateMMDDYYYY(newList.get(6).toString()));//(Date) newList.get(6));
                        objarbTo.setAwardno(CommonUtil.convertObjToStr(newList.get(8)));
                        objarbTo.setAwardDate(DateUtil.getDateMMDDYYYY(newList.get(9).toString()));//((Date) newList.get(9)));
                        objarbTo.setInspordno(CommonUtil.convertObjToStr(newList.get(10)));
                        objarbTo.setInspordDate(DateUtil.getDateMMDDYYYY(newList.get(11).toString()));//(Date) (newList.get(11)));
                       // System.out.println("arbto" + objarbTo);
                        sqlMap.executeUpdate("updateTermLoanArbitrationFileList", objarbTo);

                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
    
     // added by nithya on 10-03-2016
      
    private void deleteArbitration(HashMap arbMap) {
        try {
            if (arbMap.containsKey("DELETE_ARC")) { 
                TermLoanArbitrationTO objarbTo = new TermLoanArbitrationTO();
                ArrayList accountNoList;
                if(arbMap.containsKey("ARC_ID")){
                    objarbTo.setArbid(arbMap.get("ARC_ID").toString());
                }
                accountNoList = (ArrayList) arbMap.get("ACCOUNT_LIST");
                if(accountNoList.size() > 0){
                    for (int i = 0; i < accountNoList.size(); i++) {
                        objarbTo.setAct_num(accountNoList.get(i).toString());
                        sqlMap.executeUpdate("deleteCustomerFromARC", objarbTo);
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
    
     // added by nithya on 10-03-2016
    public void updateArbitrationAfterProcess(HashMap arbMap) {
         try {
            if (arbMap.containsKey("ARBITRATION_POST_LIST")) { 
                TermLoanArbitrationTO objarbTOUpdate = new TermLoanArbitrationTO();
                TermLoanChargesTO objChargeTO = null;
                String prodid = CommonUtil.convertObjToStr(arbMap.get("PROD_ID"));
                String prodtype = CommonUtil.convertObjToStr(arbMap.get("PROD_TYPE"));  
                objarbTOUpdate.setArcStatus(arbMap.get("ARC_STATUS").toString());
                objarbTOUpdate.setArbid(arbMap.get("ARC_ID").toString());
                arbid = CommonUtil.convertObjToStr(arbMap.get("ARC_ID"));
                ArrayList arbList;
                arbList = (ArrayList) arbMap.get("ARBITRATION_POST_LIST");
                 if (arbList.size() > 0) {
                    for (int i = 0; i < arbList.size(); i++) {
                        ArrayList newList = (ArrayList) arbList.get(i);
                        objarbTOUpdate.setAct_num(CommonUtil.convertObjToStr(newList.get(0)));
                        sqlMap.executeUpdate("updateTermLoanArbitrationFileListAfterProcess", objarbTOUpdate);
                        objarbTOUpdate.setArcfee(CommonUtil.convertObjToDouble(newList.get(6)));
                        objarbTOUpdate.setMiscCharges(CommonUtil.convertObjToDouble(newList.get(10)));
                        objChargeTO = new TermLoanChargesTO();
                        objChargeTO.setProd_Id(prodid);
                        objChargeTO.setProd_Type(prodtype);
                        objChargeTO.setAct_num(objarbTOUpdate.getAct_num());
                        objChargeTO.setChargeDt(currDt);
                        objChargeTO.setStatus_Dt(currDt);
                        objChargeTO.setStatus_By(user);
                        objChargeTO.setAuthorize_Dt(currDt);
                        objChargeTO.setAuthorize_by(user);
                        objChargeTO.setAuthorize_Status("AUTHORIZED");
                        objChargeTO.setCharge_Type("ARC_COST");
                        objChargeTO.setAmount(objarbTOUpdate.getArcfee());
                        objChargeTO.setStatus(CommonConstants.STATUS_CREATED);
                        sqlMap.executeUpdate("insertTermLoanChargeTO", objChargeTO);
                        double miscCharges=CommonUtil.convertObjToDouble(newList.get(10)); // nithya
                        if(miscCharges>0){
                            objChargeTO = new TermLoanChargesTO();
                            objChargeTO.setProd_Id(prodid);
                            objChargeTO.setProd_Type(prodtype);
                            objChargeTO.setAct_num(objarbTOUpdate.getAct_num());
                            objChargeTO.setChargeDt(currDt);
                            objChargeTO.setStatus_Dt(currDt);
                            objChargeTO.setStatus_By(user);
                            objChargeTO.setAuthorize_Dt(currDt);
                            objChargeTO.setAuthorize_by(user);
                            objChargeTO.setAuthorize_Status("AUTHORIZED");
                            objChargeTO.setCharge_Type("MISCELLANEOUS CHARGES");
                            objChargeTO.setAmount(objarbTOUpdate.getMiscCharges());
                            objChargeTO.setStatus(CommonConstants.STATUS_CREATED);
                            sqlMap.executeUpdate("insertTermLoanChargeTO", objChargeTO);
                        }
                    }
                }   
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }         
    
    // End

    private String getArbitrationId() throws Exception {
        final IDGenerateDAO dao = new IDGenerateDAO();
        final HashMap where = new HashMap();
        where.put(CommonConstants.MAP_WHERE, "ARC_ID");
        return (String) (dao.executeQuery(where)).get(CommonConstants.DATA);
    }

    private String getEPFilingId() throws Exception {
        final IDGenerateDAO dao = new IDGenerateDAO();
        final HashMap where = new HashMap();
        where.put(CommonConstants.MAP_WHERE, "EP_ID");
        return (String) (dao.executeQuery(where)).get(CommonConstants.DATA);
    }

    public void insertArbitration(HashMap arbMap) {
        try {
            if (arbMap.containsKey("ARBITRATION_POST_LIST")) {
                TermLoanArbitrationTO objarbTo = new TermLoanArbitrationTO();
                TermLoanArbitrationTO objarbTOUpdate = new TermLoanArbitrationTO();                
                String prodid = CommonUtil.convertObjToStr(arbMap.get("PROD_ID"));
                String prodtype = CommonUtil.convertObjToStr(arbMap.get("PROD_TYPE")); 
                String branchId = CommonUtil.convertObjToStr(arbMap.get("BRANCH_ID")); // Added by nithya
                arbid = getArbitrationId();      
                objarbTo.setArbid(arbid);                 
                
                // objarbTo.setArbRate(CommonUtil.convertObjToDouble(arbMap.get("ARB_RATE")));
                objarbTo.setArbitDate((Date) arbMap.get("ARB_DATE"));
                objarbTo.setReportingDate((Date) arbMap.get("REPORTING_DATE")); // Added by nithya on 05-03-2016 for 0003914
                objarbTo.setArcStatus(arbMap.get("ARC_STATUS").toString()); // Added by nithya
                objarbTOUpdate.setArbid(arbMap.get("ARC_ID").toString()); //Added by nithya
                ArrayList arbList;
                arbList = (ArrayList) arbMap.get("ARBITRATION_POST_LIST");
                if (arbList.size() > 0) {
                    for (int i = 0; i < arbList.size(); i++) {
                        ArrayList newList = (ArrayList) arbList.get(i);
                        objarbTo.setAct_num(CommonUtil.convertObjToStr(newList.get(0)));
                        objarbTo.setMem_no(CommonUtil.convertObjToStr(newList.get(1)));                                              
                        objarbTo.setProdId(prodid); // Added by nithya 
                        objarbTo.setBranchId(branchId); // Added by nithya
                        //objarbTo.setProdType(prodtype); // nithya
                        if (prodtype != null && prodtype.equals("MDS")) {                        
                            objarbTo.setPrinc_due(CommonUtil.convertObjToDouble(newList.get(3))); // Modified by nithya
                            objarbTo.setInt_due(CommonUtil.convertObjToDouble(newList.get(4))); // Modified by nithya
                            objarbTo.setPenal(0.0);//no months due // Modified by nithya
                            objarbTo.setCharges(CommonUtil.convertObjToDouble(newList.get(5)));
                            objarbTo.setArcfee(CommonUtil.convertObjToDouble(newList.get(6)));
                            objarbTo.setTotalarc(CommonUtil.convertObjToDouble(newList.get(7)));
                            objarbTo.setFileNo(CommonUtil.convertObjToStr(newList.get(8)));
                            objarbTo.setArbRate(CommonUtil.convertObjToDouble(newList.get(9)));//cc
                            objarbTo.setArb_type(CommonUtil.convertObjToStr(prodtype));//cc
                            objarbTo.setMiscCharges(CommonUtil.convertObjToDouble(newList.get(newList.size()-1)));//cc // nithya modified
                        }else if (prodtype != null && prodtype.equals("ROOMS")) {  //Added by nithya on 29-12-2021
                            // [2, KRISHNAN, 9150, 149, 9299.0, 465.0, 9763.95, , 5, ]
                            objarbTo.setPrinc_due(CommonUtil.convertObjToDouble(newList.get(2))); 
                            objarbTo.setInt_due(0.0); 
                            objarbTo.setPenal(CommonUtil.convertObjToDouble(newList.get(3)));
                            objarbTo.setCharges(0.0);
                            objarbTo.setArcfee(CommonUtil.convertObjToDouble(newList.get(5)));
                            objarbTo.setTotalarc(CommonUtil.convertObjToDouble(newList.get(6)));
                            objarbTo.setFileNo(CommonUtil.convertObjToStr(newList.get(7)));
                            objarbTo.setArbRate(CommonUtil.convertObjToDouble(newList.get(8)));//cc
                            objarbTo.setArb_type(CommonUtil.convertObjToStr(prodtype));//cc
                            objarbTo.setMiscCharges(CommonUtil.convertObjToDouble(newList.get(newList.size()-1)));//cc // nithya modified
                        } else {
                            objarbTo.setPrinc_due(CommonUtil.convertObjToDouble(newList.get(2)));
                            objarbTo.setInt_due(CommonUtil.convertObjToDouble(newList.get(3)));
                            objarbTo.setPenal(CommonUtil.convertObjToDouble(newList.get(4)));
                            objarbTo.setCharges(CommonUtil.convertObjToDouble(newList.get(5)));
                            objarbTo.setArcfee(CommonUtil.convertObjToDouble(newList.get(6)));
                            objarbTo.setTotalarc(CommonUtil.convertObjToDouble(newList.get(7)));
                            objarbTo.setFileNo(CommonUtil.convertObjToStr(newList.get(8)));
                            objarbTo.setArbRate(CommonUtil.convertObjToDouble(newList.get(9)));//cc
                            objarbTo.setArb_type(CommonUtil.convertObjToStr(prodtype));//cc
                            objarbTo.setMiscCharges(CommonUtil.convertObjToDouble(newList.get(newList.size()-1)));//cc // nithya modified
                        }
                       sqlMap.executeUpdate("insertTermLoanArbitrationFileList", objarbTo);                   
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void insertEPFiling(HashMap arbMap) {
        try {
            if (arbMap.containsKey("EP_LIST")) {
                TermLoanArbitrationTO objarbTo = new TermLoanArbitrationTO();
                TermLoanChargesTO objChargeTO = null;
                String prodid = "";
                String prodtype = CommonUtil.convertObjToStr(arbMap.get("PROD_TYPE"));
                arbid = getEPFilingId();
                objarbTo.setEpid(arbid);
                //  String prodType=
                objarbTo.setEpDate((Date) arbMap.get("FILING_DT"));
                ArrayList arbList;
                arbList = (ArrayList) arbMap.get("EP_LIST");
                if (arbList.size() > 0) {
                    for (int i = 0; i < arbList.size(); i++) {
                        ArrayList newList = (ArrayList) arbList.get(i);
                        objarbTo.setAct_num(CommonUtil.convertObjToStr(newList.get(0)));
                        objarbTo.setInspordDate((Date) newList.get(7));
                        objarbTo.setEp_app_fee(CommonUtil.convertObjToDouble(newList.get(8)));
                        objarbTo.setEp_sales_fee(CommonUtil.convertObjToDouble(newList.get(9)));
                        objarbTo.setEp_postage_fee(CommonUtil.convertObjToDouble(newList.get(10)));
                        objarbTo.setEpfee(/*
                                 * CommonUtil.convertObjToDouble(newList.get(8))+
                                 * CommonUtil.convertObjToDouble(newList.get(9))+
                                CommonUtil.convertObjToDouble(newList.get(10))+
                                 */
                                CommonUtil.convertObjToDouble(newList.get(11)));//8
                        objarbTo.setTot_ep(
                                // CommonUtil.convertObjToDouble(newList.get(8))+
                                //  CommonUtil.convertObjToDouble(newList.get(9))+
                                //  CommonUtil.convertObjToDouble(newList.get(10))+
                                CommonUtil.convertObjToDouble(newList.get(12)));//9
                        objarbTo.setFileNo(CommonUtil.convertObjToStr(newList.get(13)));
                        // objarbTo.setEpid(CommonUtil.convertObjToStr(newList.get(14)));
                        objarbTo.setEpNo(CommonUtil.convertObjToStr(newList.get(15)));
                        if (newList.get(14) != null && newList.get(14).toString().startsWith("EP")) {
                            System.out.println("objarbTo :: " + objarbTo);
                             HashMap updateMap = new HashMap();
                             updateMap.put("fileNo", objarbTo.getFileNo());
                             updateMap.put("act_num", objarbTo.getAct_num());
                             updateMap.put("EP_ID", newList.get(14).toString());
                             updateMap.put("epNo",objarbTo.getEpNo());
                            //sqlMap.executeUpdate("updateFileNoForFiling", objarbTo);
                             sqlMap.executeUpdate("updateFileNoForFiling", updateMap);
                            epEditChk = true;
                        } else {
                              
                            //KD-3449 - Added by nithya on 28-10-2022
                            objarbTo.setPrinc_due(CommonUtil.convertObjToDouble(newList.get(2)));
                            objarbTo.setInt_due(CommonUtil.convertObjToDouble(newList.get(3)));
                            objarbTo.setPenal(CommonUtil.convertObjToDouble(newList.get(4)));
                            objarbTo.setCharges(CommonUtil.convertObjToDouble(newList.get(5)));
                            objarbTo.setArcfee(CommonUtil.convertObjToDouble(newList.get(6)));
                            sqlMap.executeUpdate("updateTermLoanArbitrationEPList", objarbTo);
                            objChargeTO = new TermLoanChargesTO();
                            prodid = objarbTo.getAct_num().substring(4, 7);
                            objChargeTO.setProd_Id(prodid);
                            objChargeTO.setProd_Type(prodtype);
                            objChargeTO.setAct_num(objarbTo.getAct_num());
                            objChargeTO.setChargeDt(currDt);
                            objChargeTO.setStatus_Dt(currDt);
                            objChargeTO.setStatus_By(user);
                            objChargeTO.setAuthorize_Dt(currDt);
                            objChargeTO.setAuthorize_by(user);
                            objChargeTO.setAuthorize_Status("AUTHORIZED");
                            objChargeTO.setCharge_Type("EP_COST");
                            objChargeTO.setAmount(objarbTo.getEpfee() + CommonUtil.convertObjToDouble(objarbTo.getEp_app_fee())
                                    + CommonUtil.convertObjToDouble(objarbTo.getEp_sales_fee())
                                    + CommonUtil.convertObjToDouble(objarbTo.getEp_postage_fee()));
                            objChargeTO.setStatus(CommonConstants.STATUS_CREATED);
                            sqlMap.executeUpdate("insertTermLoanChargeTO", objChargeTO);
                        }
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void startSubsidyTransactionEp(HashMap transMap) throws Exception {
        try {
            if (!epEditChk) {
                HashMap inputMap = new HashMap();
                HashMap txMap = new HashMap();
                TxTransferTO transferTo = new TxTransferTO();
                TransferTrans transferTrans = new TransferTrans();
                HashMap authorizeMap = new HashMap();
                TermLoanFacilityTO termFacilityTo = null;
                transactionDAO = new TransactionDAO(CommonConstants.DEBIT);
                //String command =CommonUtil.convertObjToStr(transMap.get("COMMAND"));
                //  HashMap subsidyAcctList =(HashMap)transMap.get("INSERT_SUBSIDY");
//            double totSubsidyDebitAmt =CommonUtil.convertObjToDouble(transMap.get("TOTAL_SUBSIDY_TRANS_AMT")).doubleValue();
                ArrayList transferList = new ArrayList(); // for local transfer
                ArrayList prodIdList = new ArrayList();
                if (transMap.containsKey("EP_LIST")) {
                    ArrayList arbList = (ArrayList) transMap.get("EP_LIST");
//            prodIdList=(ArrayList)transMap.get("PRODID_LIST");
//            if(prodIdList !=null && prodIdList.size()>0){
//                for(int i=0;i<prodIdList.size();i++){
//                    String prodId=CommonUtil.convertObjToStr(prodIdList.get(i));
//                    inputMap.put("PRODUCT_ID",prodId);
//                    List lst =sqlMap.executeQueryForList("getLoanAccountHeads",inputMap);
////                    if(lst !=null && lst.size()>0){
//                        inputMap=(HashMap)lst.get(0);
//                        Set set =(Set)subsidyAcctList.keySet();
//                        Object obj[]=(Object[])set.toArray();
                    String prod_id = "i";
                    String prev_prodid = "p";
                    HashMap dataMap = new HashMap();
                    double amount = 0.0;

                    if (arbList.size() > 0) {
                        for (int j = 0; j < arbList.size(); j++) {

//                            TermLoanSubsidyTO objTermLoanSubsidyTO=(TermLoanSubsidyTO)subsidyAcctList.get(obj[j]);
//                           // objTermLoanSubsidyTO.setAuthorizeStatus(command);
//                            objTermLoanSubsidyTO.setAuthorizeBy(CommonUtil.convertObjToStr(transMap.get(CommonConstants.USER_ID)));
//                            objTermLoanSubsidyTO.setAuthorizeDate(currDt);
                            //        if(command.equals(CommonConstants.STATUS_AUTHORIZED)){


                            ArrayList newList = (ArrayList) arbList.get(j);
                            // if(newList.get(14)!=null && newList.get(14).toString().startsWith("EP"))
                            //{
                            String act_num = CommonUtil.convertObjToStr(newList.get(0));
                            if (act_num.length() > 0) {
                                prod_id = act_num.substring(4, 7);
                            }
                            if (CommonUtil.convertObjToStr(transMap.get("PROD_TYPE")).equals("MDS")) {
                                dataMap.put("PROD_ID", prod_id);
                                List list = (List) sqlMap.executeQueryForList("getArcMDSHeads", dataMap);
                                if (list != null && list.size() > 0) {
                                    dataMap = (HashMap) list.get(0);
                                }
                            } else {
                                if (!prod_id.equals(prev_prodid)) {
                                    dataMap.put("PROD_ID", prod_id);
                                    List list = (List) sqlMap.executeQueryForList("getArcHeads", dataMap);
                                    if (list != null && list.size() > 0) {
                                        dataMap = (HashMap) list.get(0);
                                    }
                                }
                            }
                            prev_prodid = prod_id;
                            amount = amount + CommonUtil.convertObjToDouble(newList.get(8))
                                    + CommonUtil.convertObjToDouble(newList.get(9)) + CommonUtil.convertObjToDouble(newList.get(10))
                                    + CommonUtil.convertObjToDouble(newList.get(11));
                        }
                        if ((CommonUtil.convertObjToStr(dataMap.get("EP_COST")).length() != 0 && CommonUtil.convertObjToStr(dataMap.get("EP_EXPENSE")).length() != 0)) {

                            txMap.put(TransferTrans.DR_PROD_TYPE, TransactionFactory.GL);

                            txMap.put(TransferTrans.DR_AC_HD, dataMap.get("EP_COST")); //(String)inputMap.get("AC_DEBIT_INT")
                            //    txMap.put(TransferTrans.DR_ACT_NUM, newList.get(0));
                            txMap.put(TransferTrans.DR_PROD_ID, prod_id);
                            txMap.put(TransferTrans.DR_BRANCH, _branchCode);
                            txMap.put(CommonConstants.USER_ID, transMap.get(CommonConstants.USER_ID));
                            txMap.put(TransferTrans.AUTHORIZE_STATUS_2, "ENTERED_AMOUNT");
                            txMap.put(TransferTrans.PARTICULARS, "EP Amt from" + dataMap.get("EP_COST"));
                            txMap.put("SCREEN_NAME" ,"LOAN_FILE");
                            txMap.put("TRANS_MOD_TYPE", TransactionFactory.GL); 
                            // txMap.put("LINK_BATCH_ID", newList.get(0));
//                            if(transMap.containsKey("PROD_TYPE") && transMap.get("PROD_TYPE")!=null){
//                                 if (CommonUtil.convertObjToStr(transMap.get("PROD_TYPE")).equals("MDS")) {
//                                    txMap.put("TRANS_MOD_TYPE", "MDS");
//                                 }
//                                 else{
//                                     txMap.put("TRANS_MOD_TYPE", "TL"); 
//                                 }
//                            }
                            transferTo = transactionDAO.addTransferDebitLocal(txMap, amount);
                            transferList.add(transferTo);

                        }
                        //}

                        //                        Set set =(Set)subsidyAcctList.keySet();
                        //                        Object obj[]=(Object[])set.toArray();

                        //                        for(int j=0;j<set.size();j++){
                        //                            TermLoanSubsidyTO objTermLoanSubsidyTO=(TermLoanSubsidyTO)subsidyAcctList.get(obj[j]);
                        //
                        //
                        if ((CommonUtil.convertObjToStr(dataMap.get("EP_COST")).length() != 0 && CommonUtil.convertObjToStr(dataMap.get("EP_EXPENSE")).length() != 0)) {
                            txMap.put(TransferTrans.CR_PROD_TYPE, TransactionFactory.GL);
                            //
                            txMap.put(TransferTrans.CR_AC_HD, dataMap.get("EP_EXPENSE"));
                            //txMap.put(TransferTrans.CR_ACT_NUM, objTermLoanSubsidyTO.getAcctNum());
                            //txMap.put(TransferTrans.CR_PROD_ID, dataMap.get("PROD_ID"));
                            txMap.put(TransferTrans.CR_BRANCH, _branchCode);
                            txMap.put(TransferTrans.CURRENCY, "INR");
                            txMap.put(TransferTrans.PARTICULARS, "EP Amt to" + dataMap.get("EP_EXPENSE"));
                            txMap.put("SCREEN_NAME" ,"LOAN_FILE");
                            txMap.put("TRANS_MOD_TYPE", TransactionFactory.GL); 
//                            if(transMap.containsKey("PROD_TYPE") && transMap.get("PROD_TYPE")!=null){
//                                if (CommonUtil.convertObjToStr(transMap.get("PROD_TYPE")).equals("MDS")) {
//                                    txMap.put("TRANS_MOD_TYPE", "MDS");
//                                }
//                                else{
//                                     txMap.put("TRANS_MOD_TYPE", "TL"); 
//                                }
//                            }
                            transferTo = transactionDAO.addTransferCreditLocal(txMap, CommonUtil.convertObjToDouble(amount));
                            transferList.add(transferTo);
                            txMap = new HashMap();
                            transactionDAO.setCommandMode(CommonConstants.TOSTATUS_INSERT);
//                        transactionDAO.setLoanDebitInt("C*");
//                        transactionDAO.setBreakLoanHierachy("Y");
                            transactionDAO.setLinkBatchID(arbid);//here we have to pass an arb id;
                            transactionDAO.setInitiatedBranch(_branchCode);
                            transactionDAO.doTransferLocal(transferList, _branchCode);

                            authorizeMap.put(CommonConstants.BRANCH_ID, _branchCode);
                            authorizeMap.put(CommonConstants.USER_ID, transMap.get("USER_ID"));
                            transactionDAO.authorizeCashAndTransfer(arbid, "AUTHORIZED", authorizeMap);
                        }
                        //

                    }
                }
                //                        transactionDAO.setCommandMode(commandMode);

//                    }
//                }
//            }
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw new TTException();

        }

    }

    private String getFormattedFullDate(Date dueDate) throws Exception {
        java.text.SimpleDateFormat DATE_FORMAT = new java.text.SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
        return DATE_FORMAT.format(dueDate);
    }

    private String getAccountNo(String actNum) {
        String tempAcNo = CommonUtil.lpad(actNum.substring(7, 13), 13, '*');
        System.out.println("@#@ tempAcNo:" + tempAcNo);
        return tempAcNo;
    }

//    public String  insertUpdateSubsidyDetails(HashMap transMap) throws Exception{
//        HashMap subsidyAcctList =(HashMap)transMap.get("INSERT_SUBSIDY");
//        String newBatchId ="";
//        String status="";
//        if(subsidyAcctList !=null && subsidyAcctList.size()>0){
//            Set set =(Set)subsidyAcctList.keySet();
//            Object obj[]=(Object[])set.toArray();
//            if(transMap.containsKey("COMMAND") && transMap.get("COMMAND").equals("INSERT")){
//                 newBatchId =generateSubsidyBatchID();
//            }
//            
//            for(int j=0;j<set.size();j++){
//                TermLoanSubsidyTO objTermLoanSubsidyTO=(TermLoanSubsidyTO)subsidyAcctList.get(obj[j]);
//                if(objTermLoanSubsidyTO.getCommand().equals(CommonConstants.TOSTATUS_INSERT)){
//                    if(CommonUtil.convertObjToStr(objTermLoanSubsidyTO.getSubsidyId()).length()==0){
//                        objTermLoanSubsidyTO.setSubsidyId(newBatchId);
//                    }
//                    sqlMap.executeUpdate("insertTermLoanSubsidyDetailsTO", objTermLoanSubsidyTO);
//                }
//                else{
//                    sqlMap.executeUpdate("updateTermLoanSubsidyDetailsTO", objTermLoanSubsidyTO);
//                }
//                
//            }
//        }
////        status= CommonConstants.TOSTATUS_INSERT
//        return status;
//    }
//    
      /*
     * method to get the batch id, will be called once for one batch
     */
//    private String generateSubsidyBatchID() throws Exception{
//        IDGenerateDAO dao = new IDGenerateDAO();
//        final HashMap where = new HashMap();
//        where.put(CommonConstants.MAP_WHERE, "SUBSIDY.BATCH_ID");
//        where.put("INIT_TRANS_ID", "INIT_TRANS_ID");//for trans_batch_id resetting, branch code
//        where.put(CommonConstants.BRANCH_ID, _branchCode);
//        String batchID =  (String)(dao.executeQuery(where)).get(CommonConstants.DATA);
//        dao=null;
//        return batchID;
//    }
////     public void updateSubsidyDetails(HashMap transMap) throws Exception{
//        HashMap subsidyAcctList =(HashMap)transMap.get("UPDATE_SUBSIDY");
//        if(subsidyAcctList !=null && subsidyAcctList.size()>0){
//        Set set =(Set)subsidyAcctList.keySet();
//        Object obj[]=(Object[])set.toArray();
//        for(int j=0;j<set.size();j++){
//            TermLoanSubsidyTO objTermLoanSubsidyTO=(TermLoanSubsidyTO)subsidyAcctList.get(obj[j]);
//            sqlMap.executeUpdate("updateTermLoanSubsidyDetailsTO", objTermLoanSubsidyTO);
//    }
//    }
//     }
//    
    /**
     * DO TRANSACTION WHILE AUTHORIZE TIME
     *
     */
    public void startSubsidyTransaction(HashMap transMap) throws Exception {
        try {
            HashMap inputMap = new HashMap();
            HashMap txMap = new HashMap();
            TxTransferTO transferTo = new TxTransferTO();
            TransferTrans transferTrans = new TransferTrans();
            HashMap authorizeMap = new HashMap();
            TermLoanFacilityTO termFacilityTo = null;
            transactionDAO = new TransactionDAO(CommonConstants.DEBIT);
            ArrayList transferList = new ArrayList(); // for local transfer
            ArrayList prodIdList = new ArrayList();
            if (transMap.containsKey("ARBITRATION_POST_LIST")) {
                ArrayList arbList = (ArrayList) transMap.get("ARBITRATION_POST_LIST");
                double amount = 0.0;
                HashMap dataMap = new HashMap();
                if (CommonUtil.convertObjToStr(transMap.get("PROD_TYPE")).equals("MDS")) {
                    dataMap.put("PROD_ID", transMap.get("PROD_ID"));
                    List list = (List) sqlMap.executeQueryForList("getArcMDSHeads", dataMap);
                    if (list != null && list.size() > 0) {
                        dataMap = (HashMap) list.get(0);
                    }
                }else if (CommonUtil.convertObjToStr(transMap.get("PROD_TYPE")).equals("ROOMS")) {
                    dataMap.put("PROD_ID", transMap.get("PROD_ID"));
                    List list = (List) sqlMap.executeQueryForList("getArcRoomRentHeads", dataMap);
                    if (list != null && list.size() > 0) {
                        dataMap = (HashMap) list.get(0);
                    }
                } else {
                    dataMap.put("PROD_ID", transMap.get("PROD_ID"));
                    List list = (List) sqlMap.executeQueryForList("getArcHeads", dataMap);
                    if (list != null && list.size() > 0) {
                        dataMap = (HashMap) list.get(0);
                    }
                }
                if (arbList.size() > 0 && (CommonUtil.convertObjToStr(dataMap.get("ARC_COST")).length() != 0 && CommonUtil.convertObjToStr(dataMap.get("ARC_EXPENSE")).length() != 0)) {
                    for (int j = 0; j < arbList.size(); j++) {
                        ArrayList newList = (ArrayList) arbList.get(j);
                        amount = amount + CommonUtil.convertObjToDouble(newList.get(6));
                    }
                    txMap.put(TransferTrans.DR_PROD_TYPE, TransactionFactory.GL);
                    txMap.put(TransferTrans.DR_AC_HD, dataMap.get("ARC_COST")); //(String)inputMap.get("AC_DEBIT_INT")
                    txMap.put(TransferTrans.DR_BRANCH, transMap.get(CommonConstants.SELECTED_BRANCH_ID));
                    txMap.put(CommonConstants.USER_ID, transMap.get(CommonConstants.USER_ID));
                    txMap.put(TransferTrans.AUTHORIZE_STATUS_2, "ENTERED_AMOUNT");
                    txMap.put(TransferTrans.PARTICULARS, "ARC Amt from" + dataMap.get("ARC_COST"));
                    txMap.put("SCREEN_NAME", "LOAN_ARBITRATION");
                    txMap.put(transferTrans.INITIATED_BRANCH, _branchCode);
                    txMap.put("TRANS_MOD_TYPE", TransactionFactory.GL); 
//                    if (transMap.containsKey("PROD_TYPE") && transMap.get("PROD_TYPE") != null) {
//                        if (CommonUtil.convertObjToStr(transMap.get("PROD_TYPE")).equals("MDS")) {
//                            txMap.put("TRANS_MOD_TYPE", "MDS");
//                        } else {
//                            txMap.put("TRANS_MOD_TYPE", "TL");
//                        }
//                    }
                    transferTo = transactionDAO.addTransferDebitLocal(txMap, CommonUtil.convertObjToDouble(amount));
                    transferList.add(transferTo);
                    txMap.put(TransferTrans.CR_PROD_TYPE, TransactionFactory.GL);
                    txMap.put(TransferTrans.CR_AC_HD, dataMap.get("ARC_EXPENSE"));
                    txMap.put(TransferTrans.CR_BRANCH, transMap.get(CommonConstants.SELECTED_BRANCH_ID));
                    txMap.put(TransferTrans.CURRENCY, "INR");
                    txMap.put(TransferTrans.PARTICULARS, "Arc Amt to" + dataMap.get("ARC_EXPENSE"));
                    txMap.put("SCREEN_NAME", "LOAN_ARBITRATION");
                    txMap.put("TRANS_MOD_TYPE", TransactionFactory.GL); 
//                    if (transMap.containsKey("PROD_TYPE") && transMap.get("PROD_TYPE") != null) {
//                        if (CommonUtil.convertObjToStr(transMap.get("PROD_TYPE")).equals("MDS")) {
//                            txMap.put("TRANS_MOD_TYPE", "MDS");
//                        } else {
//                            txMap.put("TRANS_MOD_TYPE", "TL");
//                        }
//                    }
                    txMap.put(transferTrans.INITIATED_BRANCH, _branchCode);
                    transferTo = transactionDAO.addTransferCreditLocal(txMap, CommonUtil.convertObjToDouble(amount));
                    transferList.add(transferTo);
                    txMap = new HashMap();
                    transactionDAO.setCommandMode(CommonConstants.TOSTATUS_INSERT);
                    transactionDAO.setLinkBatchID(transMap.get("ARC_ID").toString());//here we have to pass an arb id; Modified by nithya
                    transactionDAO.setInitiatedBranch(_branchCode);
                    transactionDAO.doTransferLocal(transferList, _branchCode);
                    authorizeMap.put(CommonConstants.BRANCH_ID, _branchCode);
                    authorizeMap.put(CommonConstants.USER_ID, transMap.get("USER_ID"));
                    transactionDAO.authorizeCashAndTransfer(arbid, "AUTHORIZED", authorizeMap);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw new TTException();

        }
    }

    public void insertNoticeCharges(HashMap inputMap) throws Exception {
        System.out.println("map in insertCharges : " + inputMap);
        ArrayList totList = new ArrayList();
        //Added BY Suresh
        String prod_type = "";
        String notice_type = "";
        //  Date auctionDt=null;
        if (inputMap.containsKey("PROD_TYPE")) {
            prod_type = CommonUtil.convertObjToStr(inputMap.get("PROD_TYPE"));

            System.out.println("###### PRODUCT_TYPE : " + prod_type);
        }
        if (inputMap.containsKey("AUCTIONDT")) {
            auctionDt = (Date) (inputMap.get("AUCTIONDT"));
            System.out.println("auctionDt>>>>>111@@@@>>>" + auctionDt);
        }
        if (inputMap.containsKey("TYPE_OF_NOTICE")) {
            notice_type = CommonUtil.convertObjToStr(inputMap.get("TYPE_OF_NOTICE"));
        }
        System.out.println("inputMap.get(NOTICE_CHARGES)>>>>>2222@@@@>>>" + (HashMap) inputMap.get("NOTICE_CHARGES"));
        HashMap map = (HashMap) inputMap.get("NOTICE_CHARGES");
        boolean onlyChargeDetails = ((Boolean) inputMap.get("ONLY_CHARGE_DETAILS")).booleanValue();
        if (prod_type.equals("") && !prod_type.equals("MDS")) {
            List prodList = sqlMap.executeQueryForList("getProductsForLoanNotice", new HashMap());
            HashMap tempMap = null;
            for (int i = 0; i < prodList.size(); i++) {
                tempMap = (HashMap) prodList.get(i);
                prodMap.put(tempMap.get("PROD_DESC"), tempMap.get("PROD_ID"));
            }
            System.out.println("prodMap in insertCharges : " + prodMap);
        }
        System.out.println("auctionDt>>>>>2222@@@@>>>" + auctionDt);
        //  System.out.println("map>>>>>111@@@@>>>"+map+"map.size()>>>>>111@@@@>>>"+map.size());
        if (map != null && map.size() > 0) {
            Object[] accountList = map.keySet().toArray();
            ArrayList tempList = null;
            for (int i = 0; i < accountList.length; i++) {
                tempList = (ArrayList) map.get(accountList[i]);
                ArrayList rowList = null;
                for (int j = 0; j < tempList.size(); j++) {
                    rowList = (ArrayList) tempList.get(j);
                    totList.add(insertNoticeChargesTO(rowList, onlyChargeDetails, prod_type));
                }

            }
            stampAdvancesTransaction(totList, notice_type, prod_type);
            noticeAdvancesTransaction(totList, notice_type, prod_type);

        }
    }

    private void noticeAdvancesTransaction(ArrayList totList, String notice_type, String prod_type) throws Exception {

        if (!prod_type.equals("MDS")) {
            HashMap acHeads = new HashMap();
            HashMap noticeHead = new HashMap();
            TransferTrans transferTrans = new TransferTrans();
            HashMap txMap = new HashMap();
            String stampAdvances = "";
            ArrayList transferList = new ArrayList();
         //   System.out.println("acHeads notice:" + acHeads);
            TxTransferTO transferTo = null;
            double transAmt = 0;
            double amountTotal = 0;
            String branchID = _branchCode;
            HashMap totAmt = new HashMap();
            System.out.println("totListtt notice" + totList);
            if (totList != null && totList.size() > 0) {
                for (int i = 0; i < totList.size(); i++) {
                    HashMap acctMap = (HashMap) totList.get(i);
                    System.out.println("acctMappp notice" + acctMap);
                    String act_num = CommonUtil.convertObjToStr(acctMap.get("ACT_NUM"));
                    acctMap.put("NOTICE_TYPE", notice_type);
                    if (i == 0) {
                        if (!prod_type.equals("MDS")) {
                            //loan part only
                            acHeads = (HashMap) sqlMap.executeQueryForObject("getLoanAccountClosingHeads", act_num);
                            stampAdvances = CommonUtil.convertObjToStr(acHeads.get("CREDIT_NOTICE_ADVANCES"));
                            noticeHead = (HashMap) sqlMap.executeQueryForObject("getChargesForLoanNotices", acctMap);
                            if (stampAdvances.equals("") || stampAdvances.equals("N")) {
                                break;
                            }
                        } else {
                            //mds part only
                            acctMap.put("PROD_ID", act_num);
                            acHeads = (HashMap) sqlMap.executeQueryForObject("getMDSAccountClosingHeads", act_num);
                            noticeHead = (HashMap) sqlMap.executeQueryForObject("getChargesForMDSNotices", acctMap);
                        }






                        if (noticeHead != null && noticeHead.size() > 0) {
                            transAmt = CommonUtil.convertObjToDouble(noticeHead.get("NOTICE_CHARGE_AMT")).doubleValue();
                        }

                    }

                    // New mode
//                double transAmt=CommonUtil.convertObjToDouble(acctMap.get("AMOUNT")).doubleValue();
                    amountTotal += transAmt;
                    System.out.println("transAmt notice ###" + transAmt);
                    txMap = new HashMap();
                    txMap.put(TransferTrans.DR_PROD_TYPE, TransactionFactory.GL);
                    txMap.put(TransferTrans.DR_AC_HD, (String) acHeads.get("NOTICE_CHARGES"));
                    txMap.put(TransferTrans.DR_BRANCH, branchID);
                    txMap.put(TransferTrans.CURRENCY, "INR");
                    txMap.put("AUTHORIZE_STATUS_2", "ENTERED_AMOUNT");
                    transferTrans.setInitiatedBranch(_branchCode);
                    txMap.put(TransferTrans.PARTICULARS, CommonUtil.convertObjToStr(acctMap.get("ACT_NUM")));
                    transferTo = transferTrans.getDebitTransferTO(txMap, transAmt);
                    System.out.println("chitsNo22 notice" + chitsNo);
                    if (prod_type.equals("MDS")) {
                        //  acctMap.get("CHITTAL")
                        if (acctMap.containsKey("CHITTAL") && acctMap.get("CHITTAL") != null && !acctMap.get("CHITTAL").toString().equals("")) {
                            transferTo.setLinkBatchId(acctMap.get("CHITTAL").toString());
                        } else {
                            transferTo.setLinkBatchId(CommonUtil.convertObjToStr(acctMap.get("ACT_NUM")));
                        }

                    } else {
                        transferTo.setLinkBatchId(CommonUtil.convertObjToStr(acctMap.get("ACT_NUM")));
                    }
                    transferList.add(transferTo);
                }
                txMap.put(TransferTrans.CR_AC_HD, (String) acHeads.get("NOTICE_ADVANCES_HEAD"));
                txMap.put(TransferTrans.CR_BRANCH, branchID);
                txMap.put(TransferTrans.CURRENCY, "INR");
                txMap.put(TransferTrans.CR_PROD_TYPE, TransactionFactory.GL);
             //   System.out.println("txmap notice ####" + txMap);
                txMap.put(TransferTrans.PARTICULARS, "Notice Advances Reversal");
                if (amountTotal > 0) {
                    txMap.put("AUTHORIZE_STATUS_2", "ENTERED_AMOUNT");
                    transferTo = transferTrans.getCreditTransferTO(txMap, amountTotal);
                }


                if (prod_type.equals("MDS")) {
                    System.out.println("mmmmm notice" + chitsNo);
                    if (!chitsNo.equals("")) {
                        transferTo.setLinkBatchId(chitsNo);
                    }
                }
//                    else
//                    {
//                         transferTo.setLinkBatchId(CommonUtil.convertObjToStr(acctMap.get("ACT_NUM")));
//                    }
//                    
//                }else
//                {
//                
//                
//                transferTo.setLinkBatchId(CommonUtil.convertObjToStr(acctMap.get("ACT_NUM")));
//                }

                transferList.add(transferTo);
                if (transferList != null && transferList.size() > 0 && amountTotal > 0) {
                    System.out.println("transferList  eeee####" + transferList + "amountTotal eee##" + amountTotal + "stampAdvances eee" + stampAdvances);
                    if (prod_type.equals("MDS")) {
                        transferTrans.doDebitCredit(transferList, branchID, true);
                    } else if (stampAdvances.equals("Y")) {
                        transferTrans.doDebitCredit(transferList, branchID, true);
                    }

                }
            }
        }
    }

    public HashMap insertNoticeChargesTO(ArrayList chargeRow, boolean onlyChargeDetails, String prod_type) throws Exception {
        System.out.println("$#@$#@$@@ chargeRow:" + chargeRow);
        HashMap chargeDetMap = null;
        HashMap transMap = new HashMap();
        TermLoanChargesTO objChargeTO = null;
        //Changed By Suresh
        String tempAmount = "";
        chitsNo = "";
        if (!prod_type.equals("") && prod_type.equals("MDS")) {
            tempAmount = CommonUtil.convertObjToStr(chargeRow.get(8));
            chitsNo = CommonUtil.convertObjToStr(chargeRow.get(2));
            System.out.println("chitsNo..." + chitsNo);
        } else {
            tempAmount = CommonUtil.convertObjToStr(chargeRow.get(14));
        }
        System.out.println("##### tempAmount:" + tempAmount);
        if (((Boolean) chargeRow.get(0)).booleanValue() && tempAmount.lastIndexOf("+") != -1) {
            chargeDetMap = new HashMap();
            objChargeTO = new TermLoanChargesTO();
            if (!prod_type.equals("") && prod_type.equals("MDS")) {
                objChargeTO.setAct_num(CommonUtil.convertObjToStr(chargeRow.get(2)));
                chargeDetMap.put("PROD_TYPE", "");
                chargeDetMap.put("PROD_ID", "");
                chargeDetMap.put("CUST_ID", "");
                chargeDetMap.put("ACT_NUM", chargeRow.get(2));
                chargeDetMap.put("MEMBER_NO", chargeRow.get(3));
                chargeDetMap.put("CUST_NAME", chargeRow.get(4));
                chargeDetMap.put("CUST_TYPE", chargeRow.get(9));
            } else {
                String prod_Id = CommonUtil.convertObjToStr(chargeRow.get(5));
                chargeDetMap.put("PROD_TYPE", "TL");
                prod_Id = CommonUtil.convertObjToStr(prodMap.get(prod_Id));
                chargeDetMap.put("PROD_ID", prod_Id);
                System.out.println("$#@$#@$@@prod_id" + prod_Id);
                objChargeTO.setProd_Id(prod_Id);
                objChargeTO.setProd_Type("TL");
                objChargeTO.setAct_num(CommonUtil.convertObjToStr(chargeRow.get(1)));
                chargeDetMap.put("ACT_NUM", chargeRow.get(1));
                chargeDetMap.put("MEMBER_NO", chargeRow.get(3));
                String name = CommonUtil.convertObjToStr(chargeRow.get(2));
                name = name.length() > 128 ? name.substring(0, 127) : name;
                chargeDetMap.put("CUST_ID", name);
                chargeDetMap.put("CUST_NAME", chargeRow.get(4));
                chargeDetMap.put("CUST_TYPE", chargeRow.get(15));
            }
            objChargeTO.setChargeDt(currDt);
            objChargeTO.setStatus_Dt(currDt);
            objChargeTO.setStatus_By(user);
            objChargeTO.setAuthorize_Dt(currDt);
            objChargeTO.setAuthorize_by(user);
            objChargeTO.setAuthorize_Status("AUTHORIZED");
            chargeDetMap.put("SENT_BY", user);
            chargeDetMap.put("SENT_DT", currDt);
            System.out.println("auctionDt222@@@>>>>" + auctionDt);          
            auctionDt = setProperDtFormat(auctionDt); // Added by nithya on 03-08-2017 for 0007230: Auction Notice Processing issue.
            if (auctionDt == null) {
                chargeDetMap.put("AUCTION_DT", null);
            } else {
                chargeDetMap.put("AUCTION_DT", auctionDt);
            }
            String amount[] = tempAmount.replace("+", ",").split(",");
            Double noticeCharge = new Double(0);
            Double postageCharge = new Double(0);
            noticeCharge = CommonUtil.convertObjToDouble(amount[0]);
            postageCharge = CommonUtil.convertObjToDouble(amount[1]);

            if (!onlyChargeDetails) {
                chargeDetMap.put("NOTICE_CHARGE", noticeCharge);
                chargeDetMap.put("POSTAGE_CHARGE", postageCharge);

                if (noticeCharge.doubleValue() > 0) {
                    objChargeTO.setCharge_Type("NOTICE CHARGES");
                    objChargeTO.setAmount(noticeCharge);
                    List noticeChargeList = (List) sqlMap.executeQueryForList("getSelectTermLoanChargeList", objChargeTO);
                    System.out.println("$#@$#@$@@ noticeChargeList : " + noticeChargeList);

                    if (noticeChargeList != null && noticeChargeList.size() > 0) {
                        HashMap noticeChargeMap = (HashMap) noticeChargeList.get(0);
                        noticeCharge = new Double(noticeCharge.doubleValue()
                                + CommonUtil.convertObjToDouble(noticeChargeMap.get("AMOUNT")).doubleValue());
                        objChargeTO.setAmount(noticeCharge);
                        String chargeNo = CommonUtil.convertObjToStr(noticeChargeMap.get("CHARGE_NO"));
                        objChargeTO.setStatus(CommonConstants.STATUS_MODIFIED);
                        objChargeTO.setChargeGenerateNo(new Long(CommonUtil.convertObjToLong(chargeNo)));
                        sqlMap.executeUpdate("updateTermLoanChargeTO", objChargeTO);
                    } else {
                        objChargeTO.setStatus(CommonConstants.STATUS_CREATED);
                        sqlMap.executeUpdate("insertTermLoanChargeTO", objChargeTO);
                    }
                }
                if (prod_type.equals("MDS")) {
                    transMap.put("ACT_NUM", chargeRow.get(1));
                    transMap.put("CHITTAL", chargeRow.get(2));
                } else {
                    transMap.put("ACT_NUM", objChargeTO.getAct_num());
                }
//                    transMap.put("AMOUNT",postageCharge);NOTICE_TYPE
                transMap.put("PROD_ID", objChargeTO.getProd_Id());

                if (postageCharge.doubleValue() > 0) {
                    objChargeTO.setCharge_Type("POSTAGE CHARGES");
                    objChargeTO.setAmount(postageCharge);
                    List postageChargeList = (List) sqlMap.executeQueryForList("getSelectTermLoanChargeList", objChargeTO);
                    System.out.println("$#@$#@$@@postageChargeList : " + postageChargeList);
                    if (postageChargeList != null && postageChargeList.size() > 0) {
                        HashMap postageChargeMap = (HashMap) postageChargeList.get(0);
                        postageCharge = new Double(postageCharge.doubleValue()
                                + CommonUtil.convertObjToDouble(postageChargeMap.get("AMOUNT")).doubleValue());
                        objChargeTO.setAmount(postageCharge);//noticeCharge
                        String chargeNo = CommonUtil.convertObjToStr(postageChargeMap.get("CHARGE_NO"));
                        objChargeTO.setStatus(CommonConstants.STATUS_MODIFIED);
                        objChargeTO.setChargeGenerateNo(new Long(CommonUtil.convertObjToLong(chargeNo)));
                        sqlMap.executeUpdate("updateTermLoanChargeTO", objChargeTO);
                    } else {
                        objChargeTO.setStatus(CommonConstants.STATUS_CREATED);
                        sqlMap.executeUpdate("insertTermLoanChargeTO", objChargeTO);
                    }
                }
            } else {
                chargeDetMap.put("NOTICE_CHARGE", new Double(0));
                chargeDetMap.put("POSTAGE_CHARGE", new Double(0));
            }
            sqlMap.executeUpdate("insertNoticeChargeDet", chargeDetMap);
        }
        System.out.println("transMapmmm" + transMap);
        return transMap;
    }

    public HashMap executeQuery(HashMap obj) throws Exception {
        _branchCode = (String) obj.get(CommonConstants.BRANCH_ID);
        currDt = ServerUtil.getCurrentDate(_branchCode);
        return getData(obj);
    }

    public HashMap getData(HashMap obj) throws Exception {
        System.out.println("obj####" + obj);
        TaskHeader header = new TaskHeader();
        header.setBranchID(_branchCode);
        HashMap getDateMap = new HashMap();
        HashMap returnMap = new HashMap();
        obj.put("WHERE", obj.get("ACT_NUM"));
        InterestCalculationTask interestCalTask = new InterestCalculationTask(header);


        //            List lst=(List)sqlMap.executeQueryForList("getLastIntCalDate",obj);
        //            getDateMap=(HashMap)lst.get(0);
        //            prod_id=  CommonUtil.convertObjToStr(getDateMap.get("PROD_ID"));
        //            map.put("PROD_ID",prod_id);

        HashMap behaveLike = (HashMap) (sqlMap.executeQueryForList("getLoanBehaves", obj).get(0));
        Date CURR_DATE = new java.util.Date();
        CURR_DATE = ServerUtil.getCurrentDateProperFormat(_branchCode);
     //   System.out.println("curr_date###1" + CURR_DATE);
        CURR_DATE = (Date) currDt.clone();
        String mapNameForLastIntCalcDt = "getLastIntCalDate";
        if (CommonUtil.convertObjToStr(behaveLike.get("BEHAVES_LIKE")).equals("OD")) {
            mapNameForLastIntCalcDt = "getLastIntCalDateAD";
        }
        List lst = (List) sqlMap.executeQueryForList(mapNameForLastIntCalcDt, obj);
        getDateMap = (HashMap) lst.get(0);
        Date lastPayDate = (Date) getDateMap.get("LAST_INT_CALC_DT");
        double interest = 0;
        double penalInt = 0;
        if (CommonUtil.convertObjToStr(behaveLike.get("BEHAVES_LIKE")).equals("OD")) {
            //                lst=(List)sqlMap.executeQueryForList("getLastIntCalDate",map);
            //                getDateMap=(HashMap)lst.get(0);
            //                lastPayDate=(Date)getDateMap.get("LAST_INTCALC_DTDEBIT");
            //                noOfDays=DateUtil.dateDiff(lastPayDate, currDt);
            //                Date CURR_DATE=currDt.clone();
            //                    getDateMap.put("CURR_DATE",CURR_DATE);
            obj.put("BRANCH_ID", _branchCode);
            obj.put("ACT_NUM", obj.get(CommonConstants.MAP_WHERE));
            obj.put("LAST_INT_CALC_DT", lastPayDate);
            obj.put("PROD_ID", getDateMap.get("PROD_ID"));
            obj.put("BEHAVES_LIKE", behaveLike.get("BEHAVES_LIKE"));
            obj.put("LOAN_ACCOUNT_CLOSING", "LOAN_ACCOUNT_CLOSING");
            System.out.println("before od interest calculation####" + obj);
            getDateMap = interestCalTask.interestCalcTermLoanAD(obj); // we need same used for TL also

            if (getDateMap.containsKey("LOAN_CLOSING_PENAL_INT")) {
                penalInt = CommonUtil.convertObjToDouble(getDateMap.get("LOAN_CLOSING_PENAL_INT")).doubleValue();
            }
            interest = CommonUtil.convertObjToDouble(getDateMap.get("INTEREST")).doubleValue();
            //                lst=sqlMap.executeQueryForList("getSumProductOD",getDateMap);
            //                getDateMap=(HashMap)lst.get(0);
            System.out.println(lastPayDate + "OD  #####!" + getDateMap);
            //                    returnMap.put("AccountInterest",getDateMap.get("INTEREST"));
            returnMap.put("AccountInterest", new Double(interest));
            returnMap.put("AccountPenalInterest", new Double(penalInt));
        } else {
            HashMap hash = new HashMap();
            //                    map.put("CURR_DATE",CURR_DATE);
            obj.put("BRANCH_ID", obj.get("BRANCH_CODE"));
            obj.put("ACT_NUM", obj.get("WHERE"));
            obj.put("BEHAVES_LIKE", behaveLike.get("BEHAVES_LIKE"));
            obj.put("LAST_INT_CALC_DT", lastPayDate);
            obj.put("PROD_ID", getDateMap.get("PROD_ID"));
            obj.put("LOAN_ACCOUNT_CLOSING", "LOAN_ACCOUNT_CLOSING");
            System.out.println("before od interest calculation####" + obj);
            //                hash=interestCalTask.calculateInterestNonBatchAndbatch(map);  //already calculating interest now using dayend balance comment
            hash = interestCalTask.interestCalcTermLoanAD(obj);
            System.out.println("totInterest#####" + hash);
            if (hash != null) {
                //                    returnMap.put("AccountInterest",hash.get("TOT_INT"));
                if (obj.containsKey("DEPOSIT_PREMATURE_CLOSER")) {
                    returnMap.put("AccountInterest", hash.get("FINAL_INT"));
                    //                             returnMap.put("AccountPenalInterest",hash.get("LOAN_INT"));
                } else {
                    returnMap.put("AccountInterest", hash.get("INTEREST"));

                    if (hash.containsKey("LOAN_CLOSING_PENAL_INT")) {
                        penalInt = CommonUtil.convertObjToDouble(hash.get("LOAN_CLOSING_PENAL_INT")).doubleValue();
                    }
                    interest = CommonUtil.convertObjToDouble(hash.get("INTEREST")).doubleValue();

                    returnMap.put("AccountInterest", new Double(interest));
                    returnMap.put("AccountPenalInterest", new Double(penalInt));
                    returnMap.put("LOANBALANCE", hash.get("PRINCIPAL_BAL"));
                    returnMap.put("ASSET_STATUS", hash.get("ASSET_STATUS"));
                    returnMap.put("ROI", hash.get("ROI"));
                    returnMap.put("REPAYMENT_TYPE", hash.get("REPAYMENT_TYPE"));
                    returnMap.put("PRINCIPAL_DUE", hash.get("PRINCIPAL_DUE"));
                    returnMap.put("SHOW_INSTALLMENT_NO", hash.get("SHOW_INSTALLMENT_NO"));

                    if (hash.containsKey("UPDATE_RET_APP_DT") && hash.get("UPDATE_RET_APP_DT") != null) {
                        returnMap.put("UPDATE_RET_APP_DT", hash.get("UPDATE_RET_APP_DT"));
                    }


                }
            }
            hash = null;
        }
        returnMap.putAll(getDateMap);
        HashMap principalMap = new HashMap();
        System.out.println("returnMap  ##" + returnMap);
        if (!(returnMap != null && returnMap.containsKey("REPAYMENT_TYPE") && returnMap.get("REPAYMENT_TYPE") != null && returnMap.get("REPAYMENT_TYPE").equals("EMI"))) {
            principalMap = getPrincipalDue(obj);
            returnMap.put("AccountInterest", new Double(interest - paid_interest));
            returnMap.put("AccountPenalInterest", new Double(penalInt - paid_penal_int));

        } else {
            List installLst = sqlMap.executeQueryForList("getAllLoanInstallment", obj);
            if (installLst != null && installLst.size() > 0) {
                returnMap.put("NO_OF_INSTALLMENT", new Long(installLst.size()));
            }
            HashMap dueMap = (HashMap) installLst.get(0);
            returnMap.put("INSTALLMENT AMT", dueMap.get("TOTAL_AMT"));
        }
        //        if(obj.containsKey("MODE") && obj.get("MODE")!=null && obj.get("MODE").equals("UPDATE"))
        //        {
        System.out.println("obj###" + obj);
        lst = sqlMap.executeQueryForList("getSelectTermLoanChargeDetailsTO", obj.get("ACT_NUM"));
        returnMap.put("TermLoanChargesTO", lst);
        //        }
        System.out.println("returnMap####   " + returnMap);
        returnMap.putAll(principalMap);
        System.out.println("returnMap####" + returnMap);
        ServiceLocator.flushCache(sqlMap);
        System.gc();
        return returnMap;
    }
    //as an when customer get principal due

    public HashMap getPrincipalDue(HashMap map) throws Exception {
        String prodType = CommonUtil.convertObjToStr(map.get("PROD_TYPE"));
        HashMap insertPenal = null;
        HashMap returnMap = new HashMap();
        paid_interest = 0;
        paid_penal_int = 0;
        paid_principal = 0;
        Date curr_dt = (Date) currDt.clone();
        if (prodType != null && prodType.equals("TL")) {

            HashMap InstalDate = new HashMap();
            List getInstallDate = null;

            Date cDate = ServerUtil.getCurrentDateProperFormat(_branchCode);
            HashMap allInstallmentMap = null;
            double principleAmt = 0;
            double intAmt = 0;
            double clearBalance = 0;
            List facilitylst = sqlMap.executeQueryForList("LoneFacilityDetailAD", map);
            if (facilitylst != null && facilitylst.size() > 0) {
                HashMap hash = (HashMap) facilitylst.get(0);
                clearBalance = CommonUtil.convertObjToDouble(hash.get("CLEAR_BALANCE")).doubleValue();
                clearBalance = clearBalance * -1;
            }
            List paidAmt = sqlMap.executeQueryForList("getPaidPrinciple", map);
            allInstallmentMap = (HashMap) paidAmt.get(0);
            double totPrinciple = CommonUtil.convertObjToDouble(allInstallmentMap.get("PRINCIPLE")).doubleValue();
            paidAmt = sqlMap.executeQueryForList("getIntDetails", map);
            if (paidAmt != null && paidAmt.size() > 0) {
                allInstallmentMap = (HashMap) paidAmt.get(0);
            }
            double pbal = CommonUtil.convertObjToDouble(allInstallmentMap.get("EXCESS_AMT")).doubleValue();
            //            totPrinciple+=totExcessAmt;
            List lst = sqlMap.executeQueryForList("getAllLoanInstallment", map);
            Date inst_dt = null;
            double instalAmt = 0;
            for (int i = 0; i < lst.size(); i++) {
                allInstallmentMap = (HashMap) lst.get(i);
                instalAmt = CommonUtil.convertObjToDouble(allInstallmentMap.get("PRINCIPAL_AMT")).doubleValue();
                if (instalAmt <= totPrinciple) {
                    totPrinciple -= instalAmt;
                    //                        if(lst.size()==1){
                    inst_dt = new Date();
                    String in_date = CommonUtil.convertObjToStr(allInstallmentMap.get("INSTALLMENT_DT"));
                    inst_dt = DateUtil.getDateMMDDYYYY(in_date);
                    //                        }
                    if (DateUtil.dateDiff(curr_dt, inst_dt) >= 0) {
                        paid_principal += instalAmt;
                    }

                } else {
                    inst_dt = new Date();
                    String in_date = CommonUtil.convertObjToStr(allInstallmentMap.get("INSTALLMENT_DT"));
                    inst_dt = DateUtil.getDateMMDDYYYY(in_date);
                    System.out.println("paid_principal" + paid_principal + "totprincipal###" + totPrinciple);
                    if (DateUtil.dateDiff(curr_dt, inst_dt) >= 0) {
                        paid_principal += totPrinciple;
                    }
                    System.out.println("paid_principal" + paid_principal + "totprincipal###" + totPrinciple);

                    totPrinciple = CommonUtil.convertObjToDouble(allInstallmentMap.get("PRINCIPAL_AMT")).doubleValue() - totPrinciple;

                    break;
                }

            }
            returnMap.put("INSTALLMENT AMT", new Double(instalAmt));
            returnMap.put("NO_OF_INSTALLMENT", new Long(lst.size()));
            returnMap.put("PAID_PRINCIPAL", new Double(paid_principal));
            Date from_Dt = DateUtil.addDays(inst_dt, 1);
//            Date currDt = currDt.clone();
            currDt.setDate(from_Dt.getDate());
            currDt.setMonth(from_Dt.getMonth());
            currDt.setYear(from_Dt.getYear());
            if (currDt != null) {
                map.put("FROM_DT", currDt);//DateUtil.addDays(inst_dt,1));
            }
            map.put("TO_DATE", cDate);
            System.out.println("getTotalamount#####" + map);
            List lst1 = null;
            if (inst_dt != null && (totPrinciple > 0)) {
                lst1 = sqlMap.executeQueryForList("getTotalAmountOverDue", map);
                System.out.println("listsize####" + lst1);
            }
            double principle = 0;
            if (lst1 != null && lst1.size() > 0) {
                HashMap overDuemap = (HashMap) lst1.get(0);
                principle = CommonUtil.convertObjToDouble(overDuemap.get("PRINCIPAL_AMOUNT")).doubleValue();
            }
            totPrinciple += principle;
            insertPenal = new HashMap();
            if (inst_dt != null) {
                if (DateUtil.dateDiff(currDt, inst_dt) <= 0 && clearBalance >= totPrinciple) {
                    returnMap.put("PRINCIPAL_DUE", new Double(totPrinciple));
                    insertPenal.put("CURR_MONTH_PRINCEPLE", new Double(totPrinciple));
                } else if (DateUtil.dateDiff(currDt, inst_dt) <= 0 && clearBalance < totPrinciple) {
                    returnMap.put("PRINCIPAL_DUE", new Double(clearBalance));
                    insertPenal.put("CURR_MONTH_PRINCEPLE", new Double(clearBalance));
                } else {
                    returnMap.put("PRINCIPAL_DUE", new Double(0));
                    insertPenal.put("CURR_MONTH_PRINCEPLE", new Double(0));
                }
            }
            insertPenal.put("INSTALL_DT", inst_dt);
            map.put("FROM_DT", map.get("LAST_INT_CALC_DT"));
            map.put("FROM_DT", DateUtil.addDays(((Date) map.get("FROM_DT")), 2));//
            map.put("TO_DATE", cDate);
            System.out.println("map#####@@@@" + map);
            List getIntDetails = sqlMap.executeQueryForList("getPaidPrinciple", map);//from_dt,to_date,act_num
            HashMap hash = null;
            if (getIntDetails != null) {
                for (int i = 0; i < getIntDetails.size(); i++) {
                    hash = (HashMap) getIntDetails.get(i);
                    returnMap.put("PAID_INTEREST", hash.get("INTEREST"));
                    returnMap.put("PAID_PENAL_INTEREST", hash.get("PENAL"));
                    paid_interest = CommonUtil.convertObjToDouble(hash.get("INTEREST")).doubleValue();
                    paid_penal_int = CommonUtil.convertObjToDouble(hash.get("PENAL")).doubleValue();
                }
            }
        } else {
            map.put("FROM_DT", map.get("LAST_INT_CALC_DT"));
            map.put("FROM_DT", DateUtil.addDays(((Date) map.get("FROM_DT")), 2));//2
            map.put("TO_DATE", ServerUtil.getCurrentDateProperFormat(_branchCode));
            System.out.println("map$$$^^^^" + map);
            List lst = sqlMap.executeQueryForList("getPaidPrincipleAD", map);
            HashMap hash = new HashMap();
            int i = 0;
            if (lst != null) {
                hash = (HashMap) lst.get(i);
            }
            returnMap.put("PAID_INTEREST", hash.get("INTEREST"));
            returnMap.put("PAID_PENAL_INTEREST", hash.get("PENAL"));
            paid_interest = CommonUtil.convertObjToDouble(hash.get("INTEREST")).doubleValue();
            paid_penal_int = CommonUtil.convertObjToDouble(hash.get("PENAL")).doubleValue();
        }
        System.out.println("returnMap####" + returnMap);
        return returnMap;
    }

    /*
     * more interest already collected from customer now banker want to return
     * back
     */
    private void stampAdvancesTransaction(ArrayList totList, String notice_type, String prod_type) throws Exception {


        HashMap acHeads = new HashMap();
        HashMap noticeHead = new HashMap();
        TransferTrans transferTrans = new TransferTrans();
        HashMap txMap = new HashMap();
        String stampAdvances = "";
        ArrayList transferList = new ArrayList();
        System.out.println("acHeads:" + acHeads);
        TxTransferTO transferTo = null;
        double transAmt = 0;
        double amountTotal = 0;
        String branchID = _branchCode;
        HashMap totAmt = new HashMap();
        System.out.println("totListtt" + totList);
        if (totList != null && totList.size() > 0) {
            for (int i = 0; i < totList.size(); i++) {
                HashMap acctMap = (HashMap) totList.get(i);
                System.out.println("acctMappp" + acctMap);
                String act_num = CommonUtil.convertObjToStr(acctMap.get("ACT_NUM"));
                acctMap.put("NOTICE_TYPE", notice_type);
                if (i == 0) {
                    if (!prod_type.equals("MDS")) {
                        //loan part only
                        acHeads = (HashMap) sqlMap.executeQueryForObject("getLoanAccountClosingHeads", act_num);
                        stampAdvances = CommonUtil.convertObjToStr(acHeads.get("CREDIT_STAMP_ADVANCES"));
                        noticeHead = (HashMap) sqlMap.executeQueryForObject("getChargesForLoanNotices", acctMap);
                        if (stampAdvances.equals("") || stampAdvances.equals("N")) {
                            break;
                        }
                    } else {
                        //mds part only
                        acctMap.put("PROD_ID", act_num);
                        acHeads = (HashMap) sqlMap.executeQueryForObject("getMDSAccountClosingHeads", act_num);
                        noticeHead = (HashMap) sqlMap.executeQueryForObject("getChargesForMDSNotices", acctMap);
                    }






                    if (noticeHead != null && noticeHead.size() > 0) {
                        transAmt = CommonUtil.convertObjToDouble(noticeHead.get("POSTAGE_AMT")).doubleValue();
                    }

                }

                // New mode
//                double transAmt=CommonUtil.convertObjToDouble(acctMap.get("AMOUNT")).doubleValue();
                amountTotal += transAmt;
                System.out.println("transAmt  ###" + transAmt);
                txMap = new HashMap();
                txMap.put(TransferTrans.DR_PROD_TYPE, TransactionFactory.GL);
                txMap.put(TransferTrans.DR_AC_HD, (String) acHeads.get("POSTAGE_CHARGES"));
                txMap.put(TransferTrans.DR_BRANCH, branchID);
                txMap.put(TransferTrans.CURRENCY, "INR");
                txMap.put("AUTHORIZE_STATUS_2", "ENTERED_AMOUNT");
                transferTrans.setInitiatedBranch(_branchCode);
                txMap.put(TransferTrans.PARTICULARS, CommonUtil.convertObjToStr(acctMap.get("ACT_NUM")));
                transferTo = transferTrans.getDebitTransferTO(txMap, transAmt);
                System.out.println("chitsNo22" + chitsNo);
                if (prod_type.equals("MDS")) {
                    //  acctMap.get("CHITTAL")
                    if (acctMap.containsKey("CHITTAL") && acctMap.get("CHITTAL") != null && !acctMap.get("CHITTAL").toString().equals("")) {
                        transferTo.setLinkBatchId(acctMap.get("CHITTAL").toString());
                    } else {
                        transferTo.setLinkBatchId(CommonUtil.convertObjToStr(acctMap.get("ACT_NUM")));
                    }

                } else {
                    transferTo.setLinkBatchId(CommonUtil.convertObjToStr(acctMap.get("ACT_NUM")));
                }
                transferList.add(transferTo);
            }
            txMap.put(TransferTrans.CR_AC_HD, (String) acHeads.get("STAMP_ADVANCES_HEAD"));
            txMap.put(TransferTrans.CR_BRANCH, branchID);
            txMap.put(TransferTrans.CURRENCY, "INR");
            txMap.put(TransferTrans.CR_PROD_TYPE, TransactionFactory.GL);
            System.out.println("txmap  ####" + txMap);
            txMap.put(TransferTrans.PARTICULARS, "Stamp Advances Reversal");
            if (amountTotal > 0) {
                txMap.put("AUTHORIZE_STATUS_2", "ENTERED_AMOUNT");
                transferTo = transferTrans.getCreditTransferTO(txMap, amountTotal);
            }


            if (prod_type.equals("MDS")) {
                System.out.println("mmmmm" + chitsNo);
                if (!chitsNo.equals("")) {
                    transferTo.setLinkBatchId(chitsNo);
                }
            }
//                    else
//                    {
//                         transferTo.setLinkBatchId(CommonUtil.convertObjToStr(acctMap.get("ACT_NUM")));
//                    }
//                    
//                }else
//                {
//                
//                
//                transferTo.setLinkBatchId(CommonUtil.convertObjToStr(acctMap.get("ACT_NUM")));
//                }

            transferList.add(transferTo);
            if (transferList != null && transferList.size() > 0 && amountTotal > 0) {
                System.out.println("transferList  ####" + transferList + "amountTotal##" + amountTotal + "stampAdvances" + stampAdvances);
                if (prod_type.equals("MDS")) {
                    transferTrans.doDebitCredit(transferList, branchID, true);
                } else if (stampAdvances.equals("Y")) {
                    transferTrans.doDebitCredit(transferList, branchID, true);
                }

            }
        }
    }

    /*
     * more interest already collected from customer now banker want to return
     * back
     */
    private void exccessCollectedTransaction(HashMap map) throws Exception {
        ArrayList transList = null;
        HashMap notDeleted = new HashMap();
        HashMap acHeads = new HashMap();
        transactionDAO = new TransactionDAO(CommonConstants.DEBIT);
//        transactionDAO.setTransType(CommonConstants.DEBIT);
        String branchID = CommonUtil.convertObjToStr(map.get(CommonConstants.BRANCH_ID));
        HashMap totAmt = new HashMap();
        if (map.containsKey("ALL_AMOUNT")) {
            totAmt = (HashMap) map.get("ALL_AMOUNT");
        }
        String act_num = CommonUtil.convertObjToStr(map.get("ACT_NUM"));
        acHeads = (HashMap) sqlMap.executeQueryForObject("getLoanAccountClosingHeads", act_num);
        HashMap txMap = new HashMap();
        transactionDAO.setInitiatedBranch(_branchCode);
        transactionDAO.setLinkBatchID(act_num);
       // System.out.println("acHeads:" + acHeads);
        TxTransferTO transferTo = null;
        String behaves = "";
        Double loanTempAmt = new Double(0);
        // New mode
        double reverseInt = CommonUtil.convertObjToDouble(totAmt.get("TRANSACTION_AMT")).doubleValue();
        System.out.println("reverseInt  ###" + reverseInt);
//                    reverseInt=reverseInt*(-1);
        System.out.println("reverseInt  ###" + reverseInt);
        txMap = new HashMap();
        txMap.put(TransferTrans.DR_PROD_TYPE, TransactionFactory.GL);
        txMap.put(TransferTrans.DR_AC_HD, (String) acHeads.get("AC_DEBIT_INT"));
        txMap.put(TransferTrans.DR_BRANCH, branchID);
        txMap.put(TransferTrans.CURRENCY, "INR");
        txMap.put("AUTHORIZEREMARKS", "ACT_CLOSE");

        txMap.put(TransferTrans.CR_PROD_ID, (String) acHeads.get("PROD_ID"));
        txMap.put(TransferTrans.CR_AC_HD, (String) acHeads.get("ACCT_HEAD"));
        txMap.put(TransferTrans.CR_ACT_NUM, act_num);
        txMap.put(TransferTrans.CR_BRANCH, branchID);
        txMap.put(TransferTrans.CURRENCY, "INR");
//                    if(behaves.equals("OD"))
//                        txMap.put(TransferTrans.CR_PROD_TYPE, TransactionFactory.ADVANCES);
//                    else
        txMap.put(TransferTrans.CR_PROD_TYPE, TransactionFactory.LOANS);
        txMap.put("AUTHORIZEREMARKS", "ACT_CLOSE");
        System.out.println("txmap  ####" + txMap);
        txMap.put(TransferTrans.PARTICULARS, "Excess Interest");
        transferTo = transactionDAO.addTransferCreditLocal(txMap, reverseInt);
        ArrayList transferList = new ArrayList();
        transferTo.setStatusBy(CommonUtil.convertObjToStr(map.get(CommonConstants.USER_ID)));
        transferList.add(transferTo);
        txMap.put(TransferTrans.PARTICULARS, act_num);
        transferTo = transactionDAO.addTransferDebitLocal(txMap, reverseInt);
        transferTo.setStatusBy(CommonUtil.convertObjToStr(map.get(CommonConstants.USER_ID)));
        transferList.add(transferTo);
        // //System.out.println("transferList:" + transferList);
        transactionDAO.setCommandMode(CommonConstants.TOSTATUS_INSERT);
        if (totAmt != null && totAmt.size() > 0) {
            transactionDAO.setLoanAmtMap(totAmt);
        }

        transactionDAO.doTransferLocal(transferList, branchID);

    }

    private void destroyObjects() {
        transactionDAO.setLoanAmtMap(new HashMap());
    }
    
     private Date setProperDtFormat(Date dt) {   // Added by nithya on 03-08-2017 for 0007230: Auction Notice Processing issue.
        Date tempDt = (Date) currDt.clone();
        if (dt != null) {
            tempDt.setDate(dt.getDate());
            tempDt.setMonth(dt.getMonth());
            tempDt.setYear(dt.getYear());
            return tempDt;
        }
        return null;
    }
}
