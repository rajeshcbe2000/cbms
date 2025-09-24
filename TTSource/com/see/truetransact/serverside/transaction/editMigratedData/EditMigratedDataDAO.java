/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition.
 * 
 *
 * EditMigratedDataDAO.java
 *
 * Created on Tue Oct 11 13:18:08 IST 2011
 */

package com.see.truetransact.serverside.transaction.editMigratedData;

import java.util.List;
import java.util.Date;
import java.util.Map;
import java.util.HashMap;
import java.util.Iterator;
import com.ibatis.db.sqlmap.SqlMap;
import com.see.truetransact.serverside.TTDAO;
import com.see.truetransact.commonutil.DateUtil;
import com.see.truetransact.serverutil.ServerUtil;
import com.see.truetransact.commonutil.CommonUtil;
import com.see.truetransact.commonutil.CommonConstants;
import com.see.truetransact.servicelocator.ServiceLocator;
import com.see.truetransact.serverexception.ServiceLocatorException;
import com.see.truetransact.serverside.common.idgenerate.IDGenerateDAO;
import com.see.truetransact.serverside.transaction.transfer.TransferDAO;
import com.see.truetransact.serverside.common.transaction.TransactionDAO;
import com.see.truetransact.transferobject.mdsapplication.mdsreceiptentry.MDSReceiptEntryTO;

/**
 * EditMigratedData DAO.
 * @author  Suresh R
 */


public class EditMigratedDataDAO extends TTDAO {
    private static SqlMap sqlMap = null;
    private Date currDt = null;
    private String mig_Change_ID;
    private HashMap mig_Change_Map = new HashMap();
    private String schemeName = "";
    private Map returnMap = null;
    private Iterator processLstIterator;
    /** Creates a new instance of OperativeAcctProductDAO */
    public EditMigratedDataDAO() throws ServiceLocatorException {
        ServiceLocator locate = ServiceLocator.getInstance();
        sqlMap = (SqlMap) locate.getDAOSqlMap();
    }
    
    public static void main(String str[]) {
        try {
            EditMigratedDataDAO dao = new EditMigratedDataDAO();
            HashMap inputMap = new HashMap();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
    
    private String getMig_Change_ID() throws Exception{
        final IDGenerateDAO dao = new IDGenerateDAO();
        final HashMap where = new HashMap();
        where.put(CommonConstants.MAP_WHERE, "MIG_CHANGE_ID");
        return (String) (dao.executeQuery(where)).get(CommonConstants.DATA);
    }
    
    public HashMap execute(HashMap map)  throws Exception {
        _branchCode = (String) map.get(CommonConstants.BRANCH_ID);
        currDt = ServerUtil.getCurrentDate(_branchCode);
        System.out.println("############# Map in DAO : "+map);
        if(map.containsKey("LOAN_TRANCTION_ENTRY")){        // LOAN_TRANSACTION_ENTRY_SCREEN
            String prod_Type ="";
            String prod_ID ="";
            String act_Num ="";
            System.out.println("############# Map in LoanTransactionData DAO : "+map);
            if(map.containsKey("LOAN_TRANS_MAP")){
                try{
                    HashMap loanTransMap = new HashMap();
                    sqlMap.startTransaction();
                    returnMap = new HashMap();
                    prod_Type = CommonUtil.convertObjToStr(map.get("PROD_TYPE"));
                    prod_ID = CommonUtil.convertObjToStr(map.get("PROD_ID"));
                    act_Num = CommonUtil.convertObjToStr(map.get("ACT_NUM"));
                    loanTransMap = (HashMap) map.get("LOAN_TRANS_MAP");
                    if(prod_Type.equals("TL")){
                        insertLoanTransDetail(loanTransMap,map,prod_Type,prod_ID,act_Num);
                    }else{
                        System.out.println("########### Advance Type");
                        insertLoanAdvanceTransDetail(loanTransMap,map,prod_Type,prod_ID,act_Num);
                    }
                    
                    //Update Function   LOAN_TRANS_DETAILS
                    HashMap loanMap = new HashMap();
                    loanMap.put("ACT_NUM",act_Num);
                    //Insert DAY_END_BALANCE TABLE
                    if(prod_Type.equals("TL")){
                        sqlMap.executeUpdate("updateLoanTransDetailFunction", loanMap);
                        //sqlMap.executeUpdate("insertLoanDayEndDailyBalanceTL", loanMap);
                    }else{
                        sqlMap.executeUpdate("insertLoanDayEndDailyBalanceAD", loanMap);
                    }
                    Date newLastIntCalDt = null;
                    newLastIntCalDt = (Date)map.get("NEW_LAST_INT_CALC_DT");
                    newLastIntCalDt = setProperDtFormat(newLastIntCalDt);
                    map.put("NEW_LAST_INT_CALC_DT",newLastIntCalDt);
                    sqlMap.executeUpdate("updateMigratedLastIntCalcDt", map);
                    returnMap.put("STATUS", "INSERTED");
                    sqlMap.commitTransaction();
                } catch (Exception e){
                    sqlMap.rollbackTransaction();
                    e.printStackTrace();
                    throw e;
                }
                destroyObjects();
            }
        }else if(map.containsKey("KCC_TRANCTION_ENTRY")){        // KCC_TRANSACTION_ENTRY_SCREEN
            String release_Num ="";
            System.out.println("############# Map in KCC TransactionData DAO : "+map);
            if(map.containsKey("KCC_TRANS_MAP")){
                try{
                    HashMap loanTransMap = new HashMap();
                    sqlMap.startTransaction();
                    returnMap = new HashMap();
                    release_Num = CommonUtil.convertObjToStr(map.get("RELEASE_NO"));
                    loanTransMap = (HashMap) map.get("KCC_TRANS_MAP");
                    insertKCCTransDetail(loanTransMap,map,release_Num);
                    returnMap.put("STATUS", "INSERTED");
                    sqlMap.commitTransaction();
                } catch (Exception e){
                    sqlMap.rollbackTransaction();
                    e.printStackTrace();
                    throw e;
                }
                destroyObjects();
            }
        }else if(map.containsKey("SUBSIDY_INTEREST_DETAILS")){        // KCC_INTEREST_SUBSIDY
            String release_Num ="";
            System.out.println("############# Map in KCC TransactionData DAO : "+map);
            if(map.containsKey("SUBSIDY_INTEREST_MAP")){
                try{
                    HashMap subsidyMap = new HashMap();
                    sqlMap.startTransaction();
                    returnMap = new HashMap();
                    release_Num = CommonUtil.convertObjToStr(map.get("RELEASE_NO"));
                    subsidyMap = (HashMap) map.get("SUBSIDY_INTEREST_MAP");
                    insertKCCInterestSubsidy(subsidyMap,map,release_Num);
                    returnMap.put("STATUS", "INSERTED");
                    sqlMap.commitTransaction();
                } catch (Exception e){
                    sqlMap.rollbackTransaction();
                    e.printStackTrace();
                    throw e;
                }
                destroyObjects();
            }
        }else if(map.containsKey("BALANCE_UPDATING")){        // LOAN_TRANSACTION_ENTRY_SCREEN
            String prod_Type ="";
            String prod_ID ="";
            String act_Num ="";
            System.out.println("############# Balance Updating List DAO : "+map);
            if(map.containsKey("BALANCE_UPDATING")){
                try{
                    HashMap accountMap = new HashMap();
                    List finalList = null;
                    sqlMap.startTransaction();
                    returnMap = new HashMap();
                    finalList = (List) map.get("BALANCE_UPDATING");
                    if(finalList!=null && finalList.size()>0){
                        for(int i=0; i<finalList.size(); i++){
                            accountMap = new HashMap();
                            accountMap = (HashMap)finalList.get(i);
                            System.out.println("########## accountMap : "+accountMap);
                            if(map.containsKey("PROD_TYPE") && map.get("PROD_TYPE").equals("OA")){
                                sqlMap.executeUpdate("updateOperativeAccountBalance", accountMap);
                            }else if(map.containsKey("PROD_TYPE") && map.get("PROD_TYPE").equals("TD")){
                                sqlMap.executeUpdate("updateDepositAccountBalance", accountMap);
                            }
                        }
                    }
                    returnMap.put("STATUS", "INSERTED");
                    sqlMap.commitTransaction();
                } catch (Exception e){
                    sqlMap.rollbackTransaction();
                    e.printStackTrace();
                    throw e;
                }
                destroyObjects();
            }
        }else{                  // EDIT_MIGRATED_DATA_SCREEN
            String prod_Type ="";
            System.out.println("############# Map in EditMigratedData DAO : "+map);
            if(map.containsKey("PROD_TYPE")){
                try{
                    sqlMap.startTransaction();
                    returnMap = new HashMap();
                    mig_Change_Map = new HashMap();
                    mig_Change_ID = getMig_Change_ID();
                    mig_Change_Map.put("MIG_CHANGE_ID", mig_Change_ID);
                    map.put("MIG_CHANGE_ID", mig_Change_ID);
                    prod_Type = CommonUtil.convertObjToStr(map.get("PROD_TYPE"));
                    if(prod_Type.equals("TL") || prod_Type.equals("AD")){
                        updateLoanDetails(map);
                    }else if(prod_Type.equals("TD")){
                        updateDepositDetails(map);
                    }else if(prod_Type.equals("MDS")){
                        if(map.get("MIG_STATUS").equals("UPDATE")){
                            updateMDSDetails(map);
                        }else if(map.get("MIG_STATUS").equals("NEW")){
                            insertMDSDetails(map);
                        }else if(map.get("MIG_STATUS").equals("DELETE")){
                            deleteMDSDetails(map);
                        }
                    }
                    insertMigMasterData(map);
                    returnMap.put("STATUS", "UPDATED");
                    if(prod_Type.equals("MDS")){
                        if(map.get("MIG_STATUS").equals("DELETE")){
                            returnMap.put("STATUS", "DELETED");
                        }else if(map.get("MIG_STATUS").equals("NEW")){
                            returnMap.put("STATUS", "INSERTED");
                        }
                    }
                    sqlMap.commitTransaction();
                } catch (Exception e){
                    sqlMap.rollbackTransaction();
                    e.printStackTrace();
                    throw e;
                }
                destroyObjects();
            }
        }
        map=null;
        System.out.println("@#$@@$@@@$ returnMap : " +returnMap);
        return (HashMap)returnMap;
    }
    
    public void insertKCCInterestSubsidy(HashMap subsidyMap, HashMap map, String release_Num) throws Exception {
        processLstIterator = subsidyMap.keySet().iterator();
        String key1 = "";
        Date repayDt = null;
        HashMap singleRecordMap = new HashMap();
        HashMap whereMap = new HashMap();
        whereMap.put("ACT_NUM",release_Num);
        sqlMap.executeUpdate("deleteIndividualSubsidy", whereMap);
        for(int i=0; i<subsidyMap.size();i++){
            singleRecordMap = new HashMap();
            key1 = (String)processLstIterator.next();
            singleRecordMap=(HashMap) subsidyMap.get(key1);
            singleRecordMap.put("ACT_NUM",release_Num);
            repayDt = (Date)singleRecordMap.get("REPAYMENT_DATE");
            singleRecordMap.put("REPAYMENT_DATE", setProperDtFormat(repayDt));
            sqlMap.executeUpdate("insertIndividualSubsidyOldDetails", singleRecordMap);
        }
    }
    
    public void insertKCCTransDetail(HashMap loanTransMap, HashMap map, String release_Num) throws Exception {
        processLstIterator = loanTransMap.keySet().iterator();
        String key1 = "";
        HashMap singleRecordMap = new HashMap();
        int j=1;
        Date transactionDt = null;
        HashMap whereMap = new HashMap();
        whereMap.put("ACT_NUM",release_Num);
        sqlMap.executeUpdate("deleteLoanTransDetails", whereMap);
        double paid_Balance=0.0;
        for(int i=0; i<loanTransMap.size();i++){
            singleRecordMap = new HashMap();
            String chargeType="";
            double chargePaid=0.0;
            double chargeBalance=0.0;
            String transDateDD ="";
            String transDate1 ="";
            String transDateMM ="";
            String transDateYY ="";
            key1 = (String)processLstIterator.next();
            singleRecordMap=(HashMap) loanTransMap.get(key1);
            singleRecordMap.put("ACT_NUM",release_Num);
            singleRecordMap.put("PROD_ID","");
            singleRecordMap.put("BRANCH_CODE",_branchCode);
            singleRecordMap.put("AUTHORIZE_STATUS",CommonConstants.STATUS_AUTHORIZED);
            singleRecordMap.put("AUTHORIZE_BY",map.get("USER_ID"));
            singleRecordMap.put("AUTHORIZE_DT",currDt);
            singleRecordMap.put("TRANS_SLNO",String.valueOf(j));
            chargeType=CommonUtil.convertObjToStr(singleRecordMap.get("CHARGE_TYPE"));
            chargePaid=CommonUtil.convertObjToDouble(singleRecordMap.get("CHARGE_PAID")).doubleValue();
            chargeBalance=CommonUtil.convertObjToDouble(singleRecordMap.get("CHARGE_BALANCE")).doubleValue();
            singleRecordMap.put("POSTAGE_CHARGE","");
            singleRecordMap.put("POSTAGE_CHARGE_BAL","");
            singleRecordMap.put("ARBITARY_CHARGE","");
            singleRecordMap.put("ARBITARY_CHARGE_BAL","");
            singleRecordMap.put("LEGAL_CHARGE","");
            singleRecordMap.put("LEGAL_CHARGE_BAL","");
            singleRecordMap.put("INSURANCE_CHARGE","");
            singleRecordMap.put("INSURANCE_CHARGE_BAL","");
            singleRecordMap.put("MISC_CHARGES","");
            singleRecordMap.put("MISC_CHARGES_BAL","");
            singleRecordMap.put("EXE_DEGREE","");
            singleRecordMap.put("EXE_DEGREE_BAL","");
            singleRecordMap.put("ADVERTISE_CHARGE","");
            singleRecordMap.put("ADVERTISE_CHARGE_BAL","");
            if(chargeType.length()>0 && (chargePaid>0 || chargeBalance>0)){
                if(CommonUtil.convertObjToStr(singleRecordMap.get("CHARGE_TYPE")).equals("POSTAGE CHARGES")){
                    singleRecordMap.put("POSTAGE_CHARGE",String.valueOf(chargePaid));
                    singleRecordMap.put("POSTAGE_CHARGE_BAL",String.valueOf(chargeBalance));
                }else if(CommonUtil.convertObjToStr(singleRecordMap.get("CHARGE_TYPE")).equals("ARBITRARY CHARGES")){
                    singleRecordMap.put("ARBITARY_CHARGE",String.valueOf(chargePaid));
                    singleRecordMap.put("ARBITARY_CHARGE_BAL",String.valueOf(chargeBalance));
                }else if(CommonUtil.convertObjToStr(singleRecordMap.get("CHARGE_TYPE")).equals("LEGAL CHARGES")){
                    singleRecordMap.put("LEGAL_CHARGE",String.valueOf(chargePaid));
                    singleRecordMap.put("LEGAL_CHARGE_BAL",String.valueOf(chargeBalance));
                }else if(CommonUtil.convertObjToStr(singleRecordMap.get("CHARGE_TYPE")).equals("INSURANCE CHARGES")){
                    singleRecordMap.put("INSURANCE_CHARGE",String.valueOf(chargePaid));
                    singleRecordMap.put("INSURANCE_CHARGE_BAL",String.valueOf(chargeBalance));
                }else if(CommonUtil.convertObjToStr(singleRecordMap.get("CHARGE_TYPE")).equals("MISCELLANEOUS CHARGES")){
                    singleRecordMap.put("MISC_CHARGES",String.valueOf(chargePaid));
                    singleRecordMap.put("MISC_CHARGES_BAL",String.valueOf(chargeBalance));
                }else if(CommonUtil.convertObjToStr(singleRecordMap.get("CHARGE_TYPE")).equals("EXECUTION DECREE CHARGES")){
                    singleRecordMap.put("EXE_DEGREE",String.valueOf(chargePaid));
                    singleRecordMap.put("EXE_DEGREE_BAL",String.valueOf(chargeBalance));
                }else if(CommonUtil.convertObjToStr(singleRecordMap.get("CHARGE_TYPE")).equals("ADVERTISE CHARGES")){
                    singleRecordMap.put("ADVERTISE_CHARGE",String.valueOf(chargePaid));
                    singleRecordMap.put("ADVERTISE_CHARGE_BAL",String.valueOf(chargeBalance));
                }
            }
            String transDate = CommonUtil.convertObjToStr(singleRecordMap.get("TRANS_DATE"));
            transDateDD = CommonUtil.convertObjToStr(transDate.substring(0,transDate.indexOf("/")));
            transDate1 = CommonUtil.convertObjToStr(transDate.substring(transDate.indexOf("/")+1, transDate.length()));
            transDateMM = CommonUtil.convertObjToStr(transDate1.substring(0,transDate1.indexOf("/")));
            transDateYY = CommonUtil.convertObjToStr(transDate1.substring(transDate1.indexOf("/")+1, transDate1.length()));
            transactionDt = (Date)currDt.clone();
            transactionDt.setDate(CommonUtil.convertObjToInt(transDateDD));
            transactionDt.setMonth(CommonUtil.convertObjToInt(transDateMM)-1);
            transactionDt.setYear(CommonUtil.convertObjToInt(transDateYY)-1900);
            transactionDt = setProperDtFormat(transactionDt);
            singleRecordMap.put("TRANS_DATE",transactionDt);
            System.out.println("############ singleRecordMap "+key1+" : "+singleRecordMap);
            sqlMap.executeUpdate("insertLoanTransDetails", singleRecordMap);
            singleRecordMap.put("AMT",String.valueOf(-1*CommonUtil.convertObjToDouble(singleRecordMap.get("PRINCIPAL_BALANCE")).doubleValue()));
            sqlMap.executeUpdate("insertLoanDayEndDailyBalance", singleRecordMap);
            //Updating Available Balance And Last Int CalcDate
            if(i==loanTransMap.size()-1){
                HashMap updateMap = new HashMap();
                updateMap.put("RELEASE_NO",release_Num);
                updateMap.put("LAST_INT_CALC_DT",transactionDt);
                updateMap.put("LAST_TRANS_DT",transactionDt);
                updateMap.put("CLEAR_BALANCE", String.valueOf(CommonUtil.convertObjToDouble(singleRecordMap.get("PRINCIPAL_BALANCE")).doubleValue()));
                sqlMap.executeUpdate("updateReleaseBalanceAndLastIntCalcDate", updateMap);
            }
            j++;
        }
    }
    
    public void insertLoanTransDetail(HashMap loanTransMap, HashMap map, String prod_Type, String prod_ID, String act_Num) throws Exception {
        processLstIterator = loanTransMap.keySet().iterator();
        String key1 = "";
        HashMap singleRecordMap = new HashMap();
        int j=1;
        Date transactionDt = null;
        Date onlineDt = null;
        HashMap whereMap = new HashMap();
        whereMap.put("ACT_NUM",act_Num);
        //sqlMap.executeUpdate("deleteLoanDayEndDailyBalance", whereMap);
        if (map.containsKey("BRANCH_CODE")) {
            List onLineDateLst = sqlMap.executeQueryForList("getOnlineDate", map);
            if (onLineDateLst != null && onLineDateLst.get(0) != null) {
                onlineDt = (Date) onLineDateLst.get(0);
                onlineDt = setProperDtFormat(onlineDt);
                whereMap.put("ONLINE_DATE", onlineDt);
                sqlMap.executeUpdate("deleteLoanTransDetailsTL", whereMap);   //Loan_Trans_Details Deleting Trans_Date<Online_date
                double paid_Balance = 0.0;
                for (int i = 0; i < loanTransMap.size(); i++) {
                    singleRecordMap = new HashMap();
                    String chargeType = "";
                    double chargePaid = 0.0;
                    double chargeBalance = 0.0;
                    String transDateDD = "";
                    String transDate1 = "";
                    String transDateMM = "";
                    String transDateYY = "";
                    String transCode = "";
                    key1 = (String) processLstIterator.next();
                    singleRecordMap = (HashMap) loanTransMap.get(key1);
                    singleRecordMap.put("ACT_NUM", act_Num);
                    singleRecordMap.put("PROD_ID", prod_ID);
                    singleRecordMap.put("BRANCH_CODE", _branchCode);
                    singleRecordMap.put("AUTHORIZE_STATUS", CommonConstants.STATUS_AUTHORIZED);
                    singleRecordMap.put("AUTHORIZE_BY", map.get("USER_ID"));
                    singleRecordMap.put("AUTHORIZE_DT", currDt);
                    singleRecordMap.put("TRANS_SLNO", String.valueOf(j));
                    chargeType = CommonUtil.convertObjToStr(singleRecordMap.get("CHARGE_TYPE"));
                    chargePaid = CommonUtil.convertObjToDouble(singleRecordMap.get("CHARGE_PAID")).doubleValue();
                    chargeBalance = CommonUtil.convertObjToDouble(singleRecordMap.get("CHARGE_BALANCE")).doubleValue();
                    singleRecordMap.put("POSTAGE_CHARGE", "");
                    singleRecordMap.put("POSTAGE_CHARGE_BAL", "");
                    singleRecordMap.put("ARBITARY_CHARGE", "");
                    singleRecordMap.put("ARBITARY_CHARGE_BAL", "");
                    singleRecordMap.put("LEGAL_CHARGE", "");
                    singleRecordMap.put("LEGAL_CHARGE_BAL", "");
                    singleRecordMap.put("INSURANCE_CHARGE", "");
                    singleRecordMap.put("INSURANCE_CHARGE_BAL", "");
                    singleRecordMap.put("MISC_CHARGES", "");
                    singleRecordMap.put("MISC_CHARGES_BAL", "");
                    singleRecordMap.put("EXE_DEGREE", "");
                    singleRecordMap.put("EXE_DEGREE_BAL", "");
                    singleRecordMap.put("ADVERTISE_CHARGE", "");
                    singleRecordMap.put("ADVERTISE_CHARGE_BAL", "");
                    if (chargeType.length() > 0 && (chargePaid > 0 || chargeBalance > 0)) {
                        if (CommonUtil.convertObjToStr(singleRecordMap.get("CHARGE_TYPE")).equals("POSTAGE CHARGES")) {
                            singleRecordMap.put("POSTAGE_CHARGE", String.valueOf(chargePaid));
                            singleRecordMap.put("POSTAGE_CHARGE_BAL", String.valueOf(chargeBalance));
                        } else if (CommonUtil.convertObjToStr(singleRecordMap.get("CHARGE_TYPE")).equals("ARBITRARY CHARGES")) {
                            singleRecordMap.put("ARBITARY_CHARGE", String.valueOf(chargePaid));
                            singleRecordMap.put("ARBITARY_CHARGE_BAL", String.valueOf(chargeBalance));
                        } else if (CommonUtil.convertObjToStr(singleRecordMap.get("CHARGE_TYPE")).equals("LEGAL CHARGES")) {
                            singleRecordMap.put("LEGAL_CHARGE", String.valueOf(chargePaid));
                            singleRecordMap.put("LEGAL_CHARGE_BAL", String.valueOf(chargeBalance));
                        } else if (CommonUtil.convertObjToStr(singleRecordMap.get("CHARGE_TYPE")).equals("INSURANCE CHARGES")) {
                            singleRecordMap.put("INSURANCE_CHARGE", String.valueOf(chargePaid));
                            singleRecordMap.put("INSURANCE_CHARGE_BAL", String.valueOf(chargeBalance));
                        } else if (CommonUtil.convertObjToStr(singleRecordMap.get("CHARGE_TYPE")).equals("MISCELLANEOUS CHARGES")) {
                            singleRecordMap.put("MISC_CHARGES", String.valueOf(chargePaid));
                            singleRecordMap.put("MISC_CHARGES_BAL", String.valueOf(chargeBalance));
                        } else if (CommonUtil.convertObjToStr(singleRecordMap.get("CHARGE_TYPE")).equals("EXECUTION DECREE CHARGES")) {
                            singleRecordMap.put("EXE_DEGREE", String.valueOf(chargePaid));
                            singleRecordMap.put("EXE_DEGREE_BAL", String.valueOf(chargeBalance));
                        } else if (CommonUtil.convertObjToStr(singleRecordMap.get("CHARGE_TYPE")).equals("ADVERTISE CHARGES")) {
                            singleRecordMap.put("ADVERTISE_CHARGE", String.valueOf(chargePaid));
                            singleRecordMap.put("ADVERTISE_CHARGE_BAL", String.valueOf(chargeBalance));
                        }
                    }
                    String transDate = CommonUtil.convertObjToStr(singleRecordMap.get("TRANS_DATE"));
                    System.out.println("############ transDate " + transDate);
                    transDateDD = CommonUtil.convertObjToStr(transDate.substring(0, transDate.indexOf("/")));
                    transDate1 = CommonUtil.convertObjToStr(transDate.substring(transDate.indexOf("/") + 1, transDate.length()));
                    transDateMM = CommonUtil.convertObjToStr(transDate1.substring(0, transDate1.indexOf("/")));
                    transDateYY = CommonUtil.convertObjToStr(transDate1.substring(transDate1.indexOf("/") + 1, transDate1.length()));
                    System.out.println("############ transDateDD " + transDateDD);
                    System.out.println("############ transDateMM " + transDateMM);
                    System.out.println("############ transDateYY " + transDateYY);
                    transactionDt = (Date) currDt.clone();
                    transactionDt.setDate(CommonUtil.convertObjToInt(transDateDD));
                    transactionDt.setMonth(CommonUtil.convertObjToInt(transDateMM) - 1);
                    transactionDt.setYear(CommonUtil.convertObjToInt(transDateYY) - 1900);
                    transactionDt = setProperDtFormat(transactionDt);
                    System.out.println("############ transactionDt " + transactionDt);
                    singleRecordMap.put("TRANS_DATE", transactionDt);
                    System.out.println("############ singleRecordMap " + key1 + " : " + singleRecordMap);
                    if (onlineDt != null) {
                        if (DateUtil.dateDiff(transactionDt, onlineDt) > 0) {
                            sqlMap.executeUpdate("insertLoanTransDetails", singleRecordMap);
                        }
                    }
                    singleRecordMap.put("AMT", String.valueOf(-1 * CommonUtil.convertObjToDouble(singleRecordMap.get("PRINCIPAL_BALANCE")).doubleValue()));
                    //sqlMap.executeUpdate("insertLoanDayEndDailyBalance", singleRecordMap);
                    //Updating Available Balance
                    transCode = CommonUtil.convertObjToStr(singleRecordMap.get("TRANS_CODE"));
                    if (!(transCode.equals("DI") || transCode.equals("DP") || transCode.equals("DPI"))) {
                        paid_Balance += CommonUtil.convertObjToDouble(singleRecordMap.get("PRINCIPAL_PAID")).doubleValue();
                    }
                    if (i == loanTransMap.size() - 1) {
                        HashMap sanctionMap = new HashMap();
                        sanctionMap.put("ACCT_NUM", act_Num);
                        List sanctionLst = sqlMap.executeQueryForList("getSanctionAmount", sanctionMap);
                        if (sanctionLst != null && sanctionLst.size() > 0) {
                            sanctionMap = (HashMap) sanctionLst.get(0);
                            singleRecordMap.put("CURRENT_BALANCE", String.valueOf(CommonUtil.convertObjToDouble(sanctionMap.get("SANCTION_AMOUNT")).doubleValue()
                                    - (paid_Balance + CommonUtil.convertObjToDouble(singleRecordMap.get("PRINCIPAL_BALANCE")).doubleValue())));
                            sqlMap.commitTransaction();
                            sqlMap.startTransaction();
                            //sqlMap.executeUpdate("updateLoanFacilityBalance", singleRecordMap);
                        }
                    }
                    j++;
                }
            }
        }
    }
    
    public void insertLoanAdvanceTransDetail(HashMap loanTransMap, HashMap map, String prod_Type, String prod_ID, String act_Num) throws Exception {
        processLstIterator = loanTransMap.keySet().iterator();
        String key1 = "";
        HashMap singleRecordMap = new HashMap();
        Date transactionDt = null;
        HashMap whereMap = new HashMap();
        whereMap.put("ACT_NUM",act_Num);
        sqlMap.executeUpdate("deleteAdvanceLoanDayEndBalance", whereMap);
        sqlMap.executeUpdate("deleteAdvanceLoanTransDetails", whereMap);
        for(int i=0; i<loanTransMap.size();i++){
            singleRecordMap = new HashMap();
            String transDateDD ="";
            String transDate1 ="";
            String transDateMM ="";
            String transDateYY ="";
            key1 = (String)processLstIterator.next();
            singleRecordMap=(HashMap) loanTransMap.get(key1);
            singleRecordMap.put("ACT_NUM",act_Num);
            singleRecordMap.put("PROD_ID",prod_ID);
            singleRecordMap.put("BRANCH_CODE",_branchCode);
            String transDate = CommonUtil.convertObjToStr(singleRecordMap.get("TRANS_DATE"));
            System.out.println("############ transDate "+transDate);
            transDateDD = CommonUtil.convertObjToStr(transDate.substring(0,transDate.indexOf("/")));
            transDate1 = CommonUtil.convertObjToStr(transDate.substring(transDate.indexOf("/")+1, transDate.length()));
            transDateMM = CommonUtil.convertObjToStr(transDate1.substring(0,transDate1.indexOf("/")));
            transDateYY = CommonUtil.convertObjToStr(transDate1.substring(transDate1.indexOf("/")+1, transDate1.length()));
            System.out.println("############ transDateDD "+transDateDD);
            System.out.println("############ transDateMM "+transDateMM);
            System.out.println("############ transDateYY "+transDateYY);
            transactionDt = (Date)currDt.clone();
            transactionDt.setDate(CommonUtil.convertObjToInt(transDateDD));
            transactionDt.setMonth(CommonUtil.convertObjToInt(transDateMM)-1);
            transactionDt.setYear(CommonUtil.convertObjToInt(transDateYY)-1900);
            transactionDt = setProperDtFormat(transactionDt);
            System.out.println("############ transactionDt "+transactionDt);
            singleRecordMap.put("DAY_END_DT",transactionDt);
            if(CommonUtil.convertObjToStr(singleRecordMap.get("BALANCE_TYPE")).equals("CREDIT")){
                singleRecordMap.put("AMOUNT",String.valueOf(-1*CommonUtil.convertObjToDouble(singleRecordMap.get("AMOUNT")).doubleValue()));
            }
            System.out.println("############ singleRecordMap "+key1+" : "+singleRecordMap);
            singleRecordMap.put("AMT",String.valueOf(-1*CommonUtil.convertObjToDouble(singleRecordMap.get("AMOUNT")).doubleValue()));
            //sqlMap.executeUpdate("insertAdvanceLoanDayEndBalance", singleRecordMap);
            if(i==loanTransMap.size()-1){
                HashMap sanctionMap = new HashMap();
                sanctionMap.put("ACCT_NUM",act_Num);
                List sanctionLst=sqlMap.executeQueryForList("getSanctionAmount",sanctionMap);
                if(sanctionLst !=null && sanctionLst.size()>0){
                    sanctionMap=(HashMap)sanctionLst.get(0);
                    singleRecordMap.put("CURRENT_BALANCE", String.valueOf(CommonUtil.convertObjToDouble(sanctionMap.get("SANCTION_AMOUNT")).doubleValue()-
                    CommonUtil.convertObjToDouble(singleRecordMap.get("AMOUNT")).doubleValue()));
                }
                singleRecordMap.put("AUTHORIZE_STATUS",CommonConstants.STATUS_AUTHORIZED);
                singleRecordMap.put("AUTHORIZE_BY",map.get("USER_ID"));
                singleRecordMap.put("TRANS_SLNO","1");
                sqlMap.executeUpdate("insertAdvanceLoanTransDetails", singleRecordMap);
                //sqlMap.executeUpdate("updateLoanFacilityBalanceAD", singleRecordMap);
            }
        }
    }
    
    public void insertMDSDetails(HashMap map) throws Exception {
        System.out.println("###### MDSmap : "+map);
        String actNo =CommonUtil.convertObjToStr(map.get("ACT_NUM"));
        String chittalNo = "";
        String subNo = "";
        if (actNo.indexOf("_")!=-1) {
            chittalNo = actNo.substring(0,actNo.indexOf("_"));
            subNo = actNo.substring(actNo.indexOf("_")+1, actNo.length());
        }
        map.put("CHITTAL_NO",chittalNo);
        map.put("SUB_NO",CommonUtil.convertObjToInt(subNo));
        map.put("NET_TRANS_ID",map.get("MIG_CHANGE_ID"));
        map.put("STATUS","CREATED");
        map.put("STATUS_BY","MIGRATED");
        map.put("STATUS_DT",currDt);
        map.put("AUTHORIZE_STATUS","AUTHORIZED");
        map.put("AUTHORIZE_BY",map.get(CommonConstants.USER_ID));
        map.put("AUTHORIZE_DT",currDt);
        sqlMap.executeUpdate("insertMigratedMDSReceiptNewDetails", map);
        sqlMap.executeUpdate("insertMigratedMDSTransNewDetails", map);
    }
    
    public void deleteMDSDetails(HashMap map) throws Exception {
        System.out.println("###### MDSmap : "+map);
        map.put("CHITTAL_NO",map.get("ACT_NUM"));
        sqlMap.executeUpdate("updateMigrateMDSReceiptStatus", map);
        sqlMap.executeUpdate("updateMigrateMDSTransStatus", map);
    }
    
    public void updateMDSDetails(HashMap map) throws Exception {
        System.out.println("###### MDSmap : "+map);
        HashMap oldMDSMap = new HashMap();
        map.put("CHITTAL_NO",map.get("ACT_NUM"));
        List MDSLst=sqlMap.executeQueryForList("getMDSReceiptDetails",map);
        if(MDSLst !=null && MDSLst.size()>0){
            oldMDSMap=(HashMap)MDSLst.get(0);
            oldMDSMap.put("ACT_NUM",map.get("ACT_NUM"));
            oldMDSMap.put("MIG_CHANGE_ID",map.get("MIG_CHANGE_ID"));
            sqlMap.executeUpdate("insertMigratedDataForMDS", oldMDSMap);
            sqlMap.executeUpdate("updateMigratedMDSTransDetails", map);
            sqlMap.executeUpdate("updateMigratedMDSReceiptDetails", map);
        }
    }
    
    public void updateDepositDetails(HashMap map) throws Exception {
        System.out.println("###### Depositmap : "+map);
        HashMap depositMap = new HashMap();
        map.put("DEPOSIT NO",map.get("ACT_NUM"));
        List depositLst=sqlMap.executeQueryForList("getSelectDepSubNoAccInfoTO",map);
        if(depositLst !=null && depositLst.size()>0){
            depositMap=(HashMap)depositLst.get(0);
            depositMap.put("ACT_NUM",map.get("ACT_NUM"));
            depositMap.put("MIG_CHANGE_ID",map.get("MIG_CHANGE_ID"));
            depositMap.put("USER_ID",map.get("USER_ID"));
            sqlMap.executeUpdate("insertMigratedDataForDeposit", depositMap);
            sqlMap.executeUpdate("updateMigratedDepositDetails", map);
        }
        //Insert Deposit Interest Table
        if(map.containsKey("DEPOSIT_INTEREST_MAP")){
            HashMap depositInterestMap = new HashMap();
            depositInterestMap = (HashMap) map.get("DEPOSIT_INTEREST_MAP");
            processLstIterator = depositInterestMap.keySet().iterator();
            String key1 = "";
            HashMap singleRecordMap = new HashMap();
            HashMap whereMap = new HashMap();
            whereMap.put("ACT_NUM",map.get("ACT_NUM"));
            sqlMap.executeUpdate("deleteDepositInterestDetails", whereMap);
            for(int i=0; i<depositInterestMap.size();i++){
                singleRecordMap = new HashMap();
                key1 = (String)processLstIterator.next();
                singleRecordMap=(HashMap) depositInterestMap.get(key1);
                singleRecordMap.put("ACT_NUM",map.get("ACT_NUM"));
                singleRecordMap.put("INT_TYPE","SIMPLE");
                singleRecordMap.put("RATE_OF_INT",map.get("RATE_OF_INT"));
                singleRecordMap.put("PROD_ID",map.get("PROD_ID"));
                singleRecordMap.put("PRODUCT_TYPE","TD");
                Date interestDate = DateUtil.getDateMMDDYYYY(CommonUtil.convertObjToStr(singleRecordMap.get("INTEREST_DATE"))+"");
                interestDate = setProperDtFormat(interestDate);
                singleRecordMap.put("INTEREST_DATE",interestDate);
                if(CommonUtil.convertObjToStr(singleRecordMap.get("INTEREST_TYPE")).equals("Credit")){
                    singleRecordMap.put("INTEREST_TYPE","CREDIT");
                }else{
                    singleRecordMap.put("INTEREST_TYPE","DEBIT");
                }
                System.out.println("############ singleRecordMap : "+singleRecordMap);
                sqlMap.executeUpdate("insertDepositInterestDetails", singleRecordMap);
            }
        }
        //Deposit Freeze
        HashMap whereMap = new HashMap();
        whereMap.put("ACT_NUM",map.get("ACT_NUM"));
        sqlMap.executeUpdate("deleteDepositFreezeDetails", whereMap);
        if(map.containsKey("FREEZE") && map.get("FREEZE").equals("Y")){
            whereMap.put("DEPOSIT_NO",map.get("ACT_NUM"));
            whereMap.put("SUB_NO",CommonUtil.convertObjToInt("1"));
            whereMap.put("FSL_NO",map.get("ACT_NUM"));
            whereMap.put("AMOUNT",map.get("MATURITY_AMT"));
            whereMap.put("REMARKS","DATA_ENTRY");
            whereMap.put("STATUS","CREATED");
            whereMap.put("AUTHORIZE_STATUS","AUTHORIZED");
            sqlMap.executeUpdate("insertDepositFreezeDetails", whereMap);
        }
    }
    
    public void updateLoanDetails(HashMap map) throws Exception {
        System.out.println("###### Loanmap : "+map);
        HashMap loanMap = new HashMap();
        map.put("WHERE",map.get("ACT_NUM"));
        Date oldLastIntCalDt = null;
        Date newLastIntCalDt = null;
        String prod_Type = CommonUtil.convertObjToStr(map.get("PROD_TYPE"));
        List loanIntCalcLst=sqlMap.executeQueryForList("getLastIntCalDateAD",map);
        if (loanIntCalcLst != null && loanIntCalcLst.size() > 0) {
            loanMap = (HashMap) loanIntCalcLst.get(0);
            oldLastIntCalDt = (Date) loanMap.get("LAST_INT_CALC_DT");
            oldLastIntCalDt = setProperDtFormat(oldLastIntCalDt);
            map.put("OLD_LAST_INT_CALC_DT", oldLastIntCalDt);
            newLastIntCalDt = (Date) map.get("NEW_LAST_INT_CALC_DT");
            newLastIntCalDt = setProperDtFormat(newLastIntCalDt);
            map.put("NEW_LAST_INT_CALC_DT", newLastIntCalDt);
            sqlMap.executeUpdate("insertMigratedDataForLoan", map);
            sqlMap.executeUpdate("updateMigratedLastIntCalcDt", map);       //Loans_Facility_Details    - Update
            sqlMap.executeUpdate("updateMigratedSanctionDetails", map);     //Loans_Sanction_Details    - Update
            sqlMap.executeUpdate("updateMigratedRateOfInterest", map);      //Loans_Int_Maintenance     - Update
            if (!prod_Type.equals("AD")) {
                sqlMap.executeUpdate("deleteMigratedTermLoanRepayment", map);   //Loans_Repay_Schedule      - Delete  
                sqlMap.executeUpdate("insertMigratedTermLoanRepayment", map);   //Loans_Repay_Schedule      - Insert
                sqlMap.commitTransaction();
                sqlMap.startTransaction();
                sqlMap.executeUpdate("insertLoanInstallmentFunction", map);     //Loans_Installment         - Delete And Insert
            }
        }
    }
    
    public void insertMigMasterData(HashMap masterMap) throws Exception {
        masterMap.put("CHANGE_DT",currDt);
        masterMap.put("STATUS_BY",masterMap.get(CommonConstants.USER_ID));
        System.out.println("###### masterMap : "+masterMap);
        sqlMap.executeUpdate("insertEditMigratedMasterData", masterMap);
    }
    
    private Date setProperDtFormat(Date dt) {
        Date tempDt=(Date)currDt.clone();
        if(dt!=null) {
            tempDt.setDate(dt.getDate());
            tempDt.setMonth(dt.getMonth());
            tempDt.setYear(dt.getYear());
            return tempDt;
        }
        return null;
    }
    
    public HashMap executeQuery(HashMap obj) throws Exception {
        _branchCode = (String) obj.get(CommonConstants.BRANCH_ID);
        currDt = ServerUtil.getCurrentDate(_branchCode);
        return null;
    }
    
    private void destroyObjects() {
        mig_Change_Map=null;
    }
}