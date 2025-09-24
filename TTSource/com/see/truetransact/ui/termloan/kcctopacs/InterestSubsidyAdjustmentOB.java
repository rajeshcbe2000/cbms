/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition.
 * 
 *
 * InterestSubsidyAdjustmentOB.java
 * 
 * Created on Thu Apr 18 10:51:55 IST 2013
 */
package com.see.truetransact.ui.termloan.kcctopacs;

import java.util.*;
import java.util.List;
import java.util.HashMap;
import java.util.ArrayList;
import org.apache.log4j.Logger;
import com.see.truetransact.ui.TrueTransactMain;
import com.see.truetransact.commonutil.DateUtil;
import com.see.truetransact.clientutil.ClientUtil;
import com.see.truetransact.commonutil.CommonUtil;
import com.see.truetransact.clientutil.TableModel;
import com.see.truetransact.uicomponent.CObservable;
import com.see.truetransact.clientproxy.ProxyFactory;
import com.see.truetransact.clientutil.ComboBoxModel;
import com.see.truetransact.clientutil.ClientConstants;
import com.see.truetransact.commonutil.CommonConstants;
import com.see.truetransact.clientproxy.ProxyParameters;
import com.see.truetransact.clientutil.EnhancedTableModel;
import com.see.truetransact.commonutil.interestcalc.Rounding;
import com.see.truetransact.clientexception.ClientParseException;
import com.see.truetransact.ui.common.transaction.TransactionOB;
import com.see.truetransact.transferobject.termloan.kcctopacs.InterestSubsidyAdjustmentTO;

/**
 *
 * @author Suresh R
 */
public class InterestSubsidyAdjustmentOB extends CObservable {

    private TransactionOB transactionOB;
    private LinkedHashMap transactionDetailsTO = null;
    private LinkedHashMap allowedTransactionDetailsTO = null;
    private LinkedHashMap deletedTransactionDetailsTO = null;
    private final String NOT_DELETED_TRANS_TOs = "NOT_DELETED_TRANS_TOs";
    private String interestType = "";
    private String rdoSubsidy = "";
    private String txtSubSidyReleaseRefNo = "";
    private String tdtSubsidyReleaseDt = "";
    private String txtSubsidyReceivedAmt = "";
    private String txtSubsidyReceivedAmtPer = "";
    private String cboProdType = "";
    private String cboAgencyName = "";
    private String cboProdID = "";
    private String txtFromAccountNo = "";
    private String txtToAccountNo = "";
    private String tdtFromDt = "";
    private String tdtToDt = "";
    private String txtFromReleaseNo = "";
    private String txtToReleaseNo = "";
    private String txtOTSSanctionNo = "";
    private String tdtOTSSanctionDt = "";
    private String cboOTSSanctionedBy = "";
    private String txtOTSAmount = "";
    private String txtRemarks = "";
    private String subsidyAdjustNo = "";
    private String principalSubsidy = "";

    public String getPrincipalSubsidy() {
        return principalSubsidy;
    }

    public void setPrincipalSubsidy(String principalSubsidy) {
        this.principalSubsidy = principalSubsidy;
    }
    private InterestSubsidyAdjustmentTO objInterestSubsidyAdjustmentTO;
    private boolean newData = false;
    private HashMap _authorizeMap;
    private String lblStatus = ClientConstants.ACTION_STATUS[ClientConstants.ACTIONTYPE_CANCEL];
    private int result;
    private ProxyFactory proxy;
    private HashMap map;
    private final static Logger log = Logger.getLogger(InterestSubsidyAdjustmentOB.class);
    private final static ClientParseException parseException = ClientParseException.getInstance();
    private int actionType;
    private ArrayList IncVal = new ArrayList();
    private TableModel tblSubsidyDetails;
    private ArrayList tblSubsidyDetailsTitle = new ArrayList();
    private ArrayList tblSubsidyDetailsTitleAuth = new ArrayList();
    private ArrayList tblSubsidyRecoveryTitle = new ArrayList();
    private ArrayList tblSubsidyRecoveryTitleAuth = new ArrayList();
    private ArrayList tblSubsidyWriteOffTitle = new ArrayList();
    private ArrayList tblSubsidyWriteOffTitleAuth = new ArrayList();
    private ArrayList tblSubsidyOTSTitle = new ArrayList();
    private ArrayList tblSubsidyOTSTitleAuth = new ArrayList();
    private Date curDate = null;
    private HashMap lookUpHash;
    private HashMap lookupMap;
    private HashMap param;
    private HashMap keyValue;
    private ArrayList key;
    private ArrayList value;
    private ComboBoxModel cbmAgencyName, cbmProdType, cbmProdID, cbmOTSSanctionedBy;
    private EnhancedTableModel tblFinYearWise;
    final ArrayList tblFinYearWiseTitle = new ArrayList();
    private List finalList = null;

    public List getFinalList() {
        return finalList;
    }

    public void setFinalList(List finalList) {
        this.finalList = finalList;
    }

    public ComboBoxModel getCbmOTSSanctionedBy() {
        return cbmOTSSanctionedBy;
    }

    public void setCbmOTSSanctionedBy(ComboBoxModel cbmOTSSanctionedBy) {
        this.cbmOTSSanctionedBy = cbmOTSSanctionedBy;
    }

    /**
     * Creates a new instance of TDS InterestSubsidyAdjustmentOB
     */
    public InterestSubsidyAdjustmentOB() {
        try {
            proxy = ProxyFactory.createProxy();
            curDate = ClientUtil.getCurrentDate();
            map = new HashMap();
            map.put(CommonConstants.JNDI, "InterestSubsidyAdjustmentJNDI");
            map.put(CommonConstants.HOME, "InterestSubsidyAdjustmentHome");
            map.put(CommonConstants.REMOTE, "InterestSubsidyAdjustment");
            fillDropdown();
            setTblFinYearWise();
            tblFinYearWise = new EnhancedTableModel(null, tblFinYearWiseTitle);
            setTblSubsidyDetails();
            setTblSubsidyDetailsAuth();
            setTblRecoveryFromCust();
            setTblRecoveryFromCustAuth();
            setTblWriteOff();
            setTblWriteOffAuth();
            setTblOTS();
            setTblOTSAuth();
            tblSubsidyDetails = new TableModel(null, tblSubsidyDetailsTitle);
        } catch (Exception e) {
            parseException.logException(e, true);
        }
    }

    public void setTblFinYearWise() {
        tblFinYearWiseTitle.add("Financial Year");
        tblFinYearWiseTitle.add("Total Outstanding");
        tblFinYearWiseTitle.add("Overdue Amount");
    }

    public EnhancedTableModel getTblFinYearWise() {
        return tblFinYearWise;
    }

    public void setTblFinYearWise(EnhancedTableModel tblFinYearWise) {
        this.tblFinYearWise = tblFinYearWise;
    }

    private void fillDropdown() throws Exception {
        try {
            lookupMap = new HashMap();
            lookupMap.put(CommonConstants.JNDI, "LookUpJNDI");
            lookupMap.put(CommonConstants.HOME, "common.lookup.LookUpHome");
            lookupMap.put(CommonConstants.REMOTE, "common.lookup.LookUp");
            lookupMap.put(CommonConstants.MAP_NAME, null);
            final ArrayList lookup_keys = new ArrayList();
            lookup_keys.add("SUBSIDY.INSTITUTIONS");
            lookup_keys.add("PRODUCTTYPE");
            lookup_keys.add("KCC_SANCTIONED_BY");
            lookupMap.put(CommonConstants.PARAMFORQUERY, lookup_keys);
            keyValue = com.see.truetransact.clientutil.ClientUtil.populateLookupData(lookupMap);
            getKeyValue((HashMap) keyValue.get("SUBSIDY.INSTITUTIONS"));
            cbmAgencyName = new ComboBoxModel(key, value);
            makeNull();

            getKeyValue((HashMap) keyValue.get("PRODUCTTYPE"));
            cbmProdType = new ComboBoxModel(key, value);
            cbmProdType.removeKeyAndElement("GL");
            cbmProdType.removeKeyAndElement("OA");
            cbmProdType.removeKeyAndElement("SA");
            cbmProdType.removeKeyAndElement("TD");
            makeNull();

            getKeyValue((HashMap) keyValue.get("KCC_SANCTIONED_BY"));
            cbmOTSSanctionedBy = new ComboBoxModel(key, value);
            makeNull();

            cbmProdID = new ComboBoxModel();
        } catch (NullPointerException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void getKeyValue(HashMap keyValue) throws Exception {
        key = (ArrayList) keyValue.get(CommonConstants.KEY);
        value = (ArrayList) keyValue.get(CommonConstants.VALUE);
    }

    private void makeNull() {
        key = null;
        value = null;
    }

    public void cellSubsidyEditableColumnTrue() {
        if (getActionType() == ClientConstants.ACTIONTYPE_NEW) {
            tblSubsidyDetails.setEditColoumnNo(7);
        }
    }

    public void cellEditableColumnFalse() {
        tblSubsidyDetails.setEditColoumnNo(-1);
    }

    public void setCbmProdId(String prodType) {
        try {
            if (!prodType.equals("")) {
                lookUpHash = new HashMap();
                lookUpHash.put(CommonConstants.MAP_NAME, "Cash.getAccProduct" + prodType);
                lookUpHash.put(CommonConstants.PARAMFORQUERY, null);
                keyValue = ClientUtil.populateLookupData(lookUpHash);
                getKeyValue((HashMap) keyValue.get(CommonConstants.DATA));
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        cbmProdID = new ComboBoxModel(key, value);
        setChanged();
    }

    public void setTblSubsidyDetails() {
        tblSubsidyDetailsTitle = new ArrayList();
        tblSubsidyDetailsTitle.add("Select");
        tblSubsidyDetailsTitle.add("Account No");
        tblSubsidyDetailsTitle.add("Name");
        tblSubsidyDetailsTitle.add("Release No");
        tblSubsidyDetailsTitle.add("Total Subsidy Amt");
        tblSubsidyDetailsTitle.add("Subsidy Amt Adjusted");
        tblSubsidyDetailsTitle.add("Balance Subsidy Due");
        tblSubsidyDetailsTitle.add("Today's Adjustment");
    }

    public void setTblSubsidyDetailsAuth() {
        tblSubsidyDetailsTitleAuth = new ArrayList();
        tblSubsidyDetailsTitleAuth.add("Account No");
        tblSubsidyDetailsTitleAuth.add("Name");
        tblSubsidyDetailsTitleAuth.add("Release No");
        tblSubsidyDetailsTitleAuth.add("Total Subsidy Amt");
        tblSubsidyDetailsTitleAuth.add("Subsidy Amt Adjusted");
        tblSubsidyDetailsTitleAuth.add("Balance Subsidy Due");
        tblSubsidyDetailsTitleAuth.add("Today's Adjustment");
    }

    public void setTblRecoveryFromCust() {
        tblSubsidyRecoveryTitle.add("Select");
        tblSubsidyRecoveryTitle.add("Account No");
        tblSubsidyRecoveryTitle.add("Name");
        tblSubsidyRecoveryTitle.add("Release No");
        tblSubsidyRecoveryTitle.add("Total Subsidy Amt");
        tblSubsidyRecoveryTitle.add("Subsidy Amt Adjusted");
        tblSubsidyRecoveryTitle.add("Balance Subsidy Due");
        tblSubsidyRecoveryTitle.add("Recovery From Cust");
    }

    public void setTblRecoveryFromCustAuth() {
        tblSubsidyRecoveryTitleAuth = new ArrayList();
        tblSubsidyRecoveryTitleAuth.add("Account No");
        tblSubsidyRecoveryTitleAuth.add("Name");
        tblSubsidyRecoveryTitleAuth.add("Release No");
        tblSubsidyRecoveryTitleAuth.add("Total Subsidy Amt");
        tblSubsidyRecoveryTitleAuth.add("Subsidy Amt Adjusted");
        tblSubsidyRecoveryTitleAuth.add("Balance Subsidy Due");
        tblSubsidyRecoveryTitleAuth.add("Recovery From Cust");
    }

    public void setTblWriteOff() {
        tblSubsidyWriteOffTitle.add("Select");
        tblSubsidyWriteOffTitle.add("Account No");
        tblSubsidyWriteOffTitle.add("Name");
        tblSubsidyWriteOffTitle.add("Release No");
        tblSubsidyWriteOffTitle.add("Total Subsidy Amt");
        tblSubsidyWriteOffTitle.add("Subsidy Amt Adjusted");
        tblSubsidyWriteOffTitle.add("Balance Subsidy Due");
        tblSubsidyWriteOffTitle.add("Write Off Amt");
    }

    public void setTblWriteOffAuth() {
        tblSubsidyWriteOffTitleAuth = new ArrayList();
        tblSubsidyWriteOffTitleAuth.add("Account No");
        tblSubsidyWriteOffTitleAuth.add("Name");
        tblSubsidyWriteOffTitleAuth.add("Release No");
        tblSubsidyWriteOffTitleAuth.add("Total Subsidy Amt");
        tblSubsidyWriteOffTitleAuth.add("Subsidy Amt Adjusted");
        tblSubsidyWriteOffTitleAuth.add("Balance Subsidy Due");
        tblSubsidyWriteOffTitleAuth.add("Write Off Amt");
    }

    public void setTblOTS() {
        tblSubsidyOTSTitle.add("Select");
        tblSubsidyOTSTitle.add("Account No");
        tblSubsidyOTSTitle.add("Name");
        tblSubsidyOTSTitle.add("Release No");
        tblSubsidyOTSTitle.add("Total Subsidy Amt");
        tblSubsidyOTSTitle.add("Subsidy Amt Adjusted");
        tblSubsidyOTSTitle.add("Balance Subsidy Due");
        tblSubsidyOTSTitle.add("OTS Amount");
    }

    public void setTblOTSAuth() {
        tblSubsidyOTSTitleAuth = new ArrayList();
        tblSubsidyOTSTitleAuth.add("Account No");
        tblSubsidyOTSTitleAuth.add("Name");
        tblSubsidyOTSTitleAuth.add("Release No");
        tblSubsidyOTSTitleAuth.add("Total Subsidy Amt");
        tblSubsidyOTSTitleAuth.add("Subsidy Amt Adjusted");
        tblSubsidyOTSTitleAuth.add("Balance Subsidy Due");
        tblSubsidyOTSTitleAuth.add("OTS Amount");
    }

    public void getData(HashMap whereMap) {
        try {
            final HashMap data = (HashMap) proxy.executeQuery(whereMap, map);
            System.out.println("#### After DAO Data : " + data);
            HashMap resultMap = new HashMap();
            if (data.containsKey("TRANSFER_TRANS_LIST")) {
                List transList = (List) data.get("TRANSFER_TRANS_LIST");
                resultMap.put("TRANSFER_TRANS_LIST", transList);
                setProxyReturnMap(resultMap);
            }
            if (data.containsKey("TRANSACTION_LIST")) {
                List list = (List) data.get("TRANSACTION_LIST");
                if (!list.isEmpty()) {
                    transactionOB.setDetails(list);
                }
            }
            if (data.containsKey("SubsidyInterestData")) {
                objInterestSubsidyAdjustmentTO = (InterestSubsidyAdjustmentTO) ((List) data.get("SubsidyInterestData")).get(0);
                populateIntSubventionDetails(objInterestSubsidyAdjustmentTO);
            }
            if (data.containsKey("SUBSIDY_DUE_LIST")) {
                populateUIData(data);
            }
        } catch (Exception e) {
            parseException.logException(e, true);
            e.printStackTrace();
        }
    }

    private void populateIntSubventionDetails(InterestSubsidyAdjustmentTO objInterestSubsidyAdjustmentTO) throws Exception {
        setSubsidyAdjustNo(objInterestSubsidyAdjustmentTO.getAdjustmentNo());
        setInterestType(objInterestSubsidyAdjustmentTO.getIntType());
        setTdtSubsidyReleaseDt(CommonUtil.convertObjToStr(objInterestSubsidyAdjustmentTO.getSubsidyDate()));
        setTxtSubSidyReleaseRefNo(CommonUtil.convertObjToStr(objInterestSubsidyAdjustmentTO.getSubsidyRefNo()));
        setTxtSubsidyReceivedAmt(CommonUtil.convertObjToStr(objInterestSubsidyAdjustmentTO.getSubsidyAmountReceived()));
        setTxtSubsidyReceivedAmtPer(CommonUtil.convertObjToStr(objInterestSubsidyAdjustmentTO.getSubsidyReceivedPer()));
        setCboAgencyName(CommonUtil.convertObjToStr(objInterestSubsidyAdjustmentTO.getAgencyName()));
        setCbmProdId(CommonUtil.convertObjToStr(objInterestSubsidyAdjustmentTO.getProdType()));
        setCboProdID((String) getCbmProdID().getDataForKey(CommonUtil.convertObjToStr(objInterestSubsidyAdjustmentTO.getProdId())));
        setCboProdType((String) getCbmProdType().getDataForKey(CommonUtil.convertObjToStr(objInterestSubsidyAdjustmentTO.getProdType())));
        setTxtFromAccountNo(CommonUtil.convertObjToStr(objInterestSubsidyAdjustmentTO.getFromAccNo()));
        setTxtToAccountNo(CommonUtil.convertObjToStr(objInterestSubsidyAdjustmentTO.getToAccNo()));
        setTxtFromReleaseNo(CommonUtil.convertObjToStr(objInterestSubsidyAdjustmentTO.getFromReleaseNo()));
        setTxtToReleaseNo(CommonUtil.convertObjToStr(objInterestSubsidyAdjustmentTO.getToReleaseNo()));
        setTdtFromDt(CommonUtil.convertObjToStr(objInterestSubsidyAdjustmentTO.getFromDt()));
        setTdtToDt(CommonUtil.convertObjToStr(objInterestSubsidyAdjustmentTO.getToDt()));
        setTdtOTSSanctionDt(CommonUtil.convertObjToStr(objInterestSubsidyAdjustmentTO.getOtsSanctionDt()));
        setCboOTSSanctionedBy(CommonUtil.convertObjToStr(objInterestSubsidyAdjustmentTO.getOtsSanctionBy()));
        setTxtOTSSanctionNo(CommonUtil.convertObjToStr(objInterestSubsidyAdjustmentTO.getOtsSanctionBy()));
        setTxtOTSAmount(CommonUtil.convertObjToStr(objInterestSubsidyAdjustmentTO.getOtsAmount()));
        setTxtRemarks(CommonUtil.convertObjToStr(objInterestSubsidyAdjustmentTO.getRemarks()));
        setRdoSubsidy(CommonUtil.convertObjToStr(objInterestSubsidyAdjustmentTO.getSubsidyType()));
    }

    public void populateUIData(HashMap data) throws Exception {
        try {
            ArrayList rowList = new ArrayList();
            ArrayList tableList = new ArrayList();
            HashMap dataMap = new HashMap();
            List releaseList = (List) data.get("SUBSIDY_DUE_LIST");
            if (releaseList != null && releaseList.size() > 0) {
                for (int i = 0; i < releaseList.size(); i++) {
                    dataMap = (HashMap) releaseList.get(i);
                    rowList = new ArrayList();
                    rowList.add(dataMap.get("ACCOUNT_NO"));
                    rowList.add(dataMap.get("NAME"));
                    rowList.add(dataMap.get("RELEASE_NO"));
                    rowList.add(CommonUtil.convertObjToDouble(dataMap.get("TOTAL_SUBSIDY_AMOUNT")));
                    rowList.add(CommonUtil.convertObjToDouble(dataMap.get("SUBSIDY_ADJUSTED_AMOUNT")));
                    rowList.add(CommonUtil.convertObjToDouble(dataMap.get("BALANCE_SUBSIDY_DUE")));
                    if (getInterestType().equals("S")) {
                        rowList.add(CommonUtil.convertObjToDouble(dataMap.get("TODAYS_ADJUSTMENT_AMOUNT")));
                    }else if (getInterestType().equals("R")) {
                        rowList.add(CommonUtil.convertObjToDouble(dataMap.get("RECOVERY_FROM_CUST_AMOUNT")));
                    }else if (getInterestType().equals("W")) {
                        rowList.add(CommonUtil.convertObjToDouble(dataMap.get("WRITE_OFF_AMOUNT")));
                    }else if (getInterestType().equals("O")) {
                        rowList.add(CommonUtil.convertObjToDouble(dataMap.get("OTS_AMOUNT")));
                    }
                    tableList.add(rowList);
                }
                if (getInterestType().equals("S")) {
                    tblSubsidyDetails = new TableModel(tableList, tblSubsidyDetailsTitleAuth);
                } else if (getInterestType().equals("R")) {
                    tblSubsidyDetails = new TableModel(tableList, tblSubsidyRecoveryTitleAuth);
                } else if (getInterestType().equals("W")) {
                    tblSubsidyDetails = new TableModel(tableList, tblSubsidyWriteOffTitleAuth);
                } else if (getInterestType().equals("O")) {
                    tblSubsidyDetails = new TableModel(tableList, tblSubsidyOTSTitleAuth);
                }
            }
        } catch (Exception e) {
            parseException.logException(e, true);
        }
    }

    public void insertTableDataAD(HashMap whereMap, String intType) throws Exception {
        try {
            ArrayList rowList = new ArrayList();
            ArrayList tableList = new ArrayList();
            HashMap dataMap = new HashMap();
            HashMap subsidyMapMap = new HashMap();
            Date dueDate = null;
            Date lastIntCalcDate = null;
            Date releaseDate = null;
            double totalSubsidyAmt = 0.0;
            double subsidyAmtAdjusted = 0.0;
            double balanceSubsidyAmt = 0.0;
            List releaseList = ClientUtil.executeQuery("getFinYearReleaseDetailsIntSubsidy", whereMap);
            if (releaseList != null && releaseList.size() > 0) {
                for (int i = 0; i < releaseList.size(); i++) {
                    dataMap = (HashMap) releaseList.get(i);
                    rowList = new ArrayList();
                    totalSubsidyAmt = 0.0;
                    subsidyAmtAdjusted = 0.0;
                    balanceSubsidyAmt = 0.0;
                    rowList.add(new Boolean(false));
                    rowList.add(CommonUtil.convertObjToStr(dataMap.get("KCC_ACT_NUM")));
                    rowList.add(CommonUtil.convertObjToStr(dataMap.get("CUSTOMER_NAME")));
                    rowList.add(CommonUtil.convertObjToStr(dataMap.get("RELEASE_NO")));
                    //Set Financial Year
                    dataMap.put("ADJD_FIN_YEAR",whereMap.get("START_YEAR")+"-"+whereMap.get("END_YEAR"));
                    
                    //Total Subsidy Amount From SUBSIDY_INDIVIDUAL_RATES Table
                    subsidyMapMap.put("RELEASE_NO", dataMap.get("RELEASE_NO"));
                    if (intType.equals("S")) {
                        subsidyMapMap.put("INSTITUTION_NAME", whereMap.get("INSTITUTION_NAME"));
                    }
                    subsidyMapMap.put("RELEASE_NO", dataMap.get("RELEASE_NO"));
                    subsidyMapMap.put("START_YEAR", whereMap.get("START_YEAR"));
                    subsidyMapMap.put("END_YEAR", whereMap.get("END_YEAR"));
                    List subsidyList = ClientUtil.executeQuery("getTotalSubsidyAmount", subsidyMapMap);
                    if (subsidyList != null && subsidyList.size() > 0) {
                        subsidyMapMap = (HashMap) subsidyList.get(0);
                        rowList.add(CommonUtil.convertObjToDouble(subsidyMapMap.get("SUBSIDY_AMOUNT")));
                        dataMap.put("TOTAL_SUBSIDY_AMOUNT", CommonUtil.convertObjToStr(subsidyMapMap.get("SUBSIDY_AMOUNT")));
                        totalSubsidyAmt = CommonUtil.convertObjToDouble(subsidyMapMap.get("SUBSIDY_AMOUNT"));
                    } else {
                        rowList.add(CommonUtil.convertObjToDouble(String.valueOf("0")));
                        dataMap.put("TOTAL_SUBSIDY_AMOUNT", CommonUtil.convertObjToStr(String.valueOf("0")));
                    }

                    //Subsidy Amount Adjusted
                    HashMap adjustedMap = new HashMap();
                    adjustedMap.put("RELEASE_NO", dataMap.get("RELEASE_NO"));
                    adjustedMap.put("START_YEAR", whereMap.get("START_YEAR"));
                    adjustedMap.put("END_YEAR", whereMap.get("END_YEAR"));
                    if (intType.equals("S")) {
                        adjustedMap.put("INSTITUTION_NAME", whereMap.get("INSTITUTION_NAME"));
                    }
                    adjustedMap.put("ADJD_FIN_YEAR",whereMap.get("START_YEAR")+"-"+whereMap.get("END_YEAR"));
                    List adjustedList = ClientUtil.executeQuery("getTotalAdjustedSubsidyAmount", adjustedMap);
                    if (adjustedList != null && adjustedList.size() > 0) {
                        adjustedMap = (HashMap) adjustedList.get(0);
                        if (intType.equals("S")) {
                            rowList.add(CommonUtil.convertObjToDouble(adjustedMap.get("TODAYS_ADJUSTMENT_AMOUNT")));
                            subsidyAmtAdjusted = CommonUtil.convertObjToDouble(adjustedMap.get("TODAYS_ADJUSTMENT_AMOUNT"));
                            dataMap.put("SUBSIDY_ADJUSTED_AMOUNT", CommonUtil.convertObjToStr(String.valueOf(subsidyAmtAdjusted)));
                        } else {
                            rowList.add(CommonUtil.convertObjToDouble(adjustedMap.get("TODAYS_ADJUSTMENT_AMOUNT"))
                                    + CommonUtil.convertObjToDouble(adjustedMap.get("RECOVERY_FROM_CUST_AMOUNT"))
                                    + CommonUtil.convertObjToDouble(adjustedMap.get("WRITE_OFF_AMOUNT"))
                                    + CommonUtil.convertObjToDouble(adjustedMap.get("OTS_AMOUNT")));
                            subsidyAmtAdjusted = CommonUtil.convertObjToDouble(adjustedMap.get("TODAYS_ADJUSTMENT_AMOUNT"))
                                    + CommonUtil.convertObjToDouble(adjustedMap.get("RECOVERY_FROM_CUST_AMOUNT"))
                                    + CommonUtil.convertObjToDouble(adjustedMap.get("WRITE_OFF_AMOUNT"))
                                    + CommonUtil.convertObjToDouble(adjustedMap.get("OTS_AMOUNT"));
                            dataMap.put("SUBSIDY_ADJUSTED_AMOUNT", CommonUtil.convertObjToStr(String.valueOf(subsidyAmtAdjusted)));
                        }
                    } else {
                        rowList.add(CommonUtil.convertObjToDouble(String.valueOf("0")));
                        dataMap.put("SUBSIDY_ADJUSTED_AMOUNT", CommonUtil.convertObjToStr(String.valueOf("0")));
                    }

                    //Calculate Balance Sibsidy Amount
                    balanceSubsidyAmt = totalSubsidyAmt - subsidyAmtAdjusted;
                    if(balanceSubsidyAmt<0){
                        balanceSubsidyAmt = 0;
                    }
                    rowList.add(CommonUtil.convertObjToDouble(String.valueOf(balanceSubsidyAmt)));
                    dataMap.put("BALANCE_SUBSIDY_DUE", CommonUtil.convertObjToStr(String.valueOf(balanceSubsidyAmt)));

                    rowList.add(CommonUtil.convertObjToDouble(String.valueOf("0")));
                    tableList.add(rowList);
                }
                if (intType.equals("S")) {
                    tblSubsidyDetails = new TableModel((ArrayList) tableList, tblSubsidyDetailsTitle);
                } else if (intType.equals("R")) {
                    tblSubsidyDetails = new TableModel((ArrayList) tableList, tblSubsidyRecoveryTitle);
                } else if (intType.equals("W")) {
                    tblSubsidyDetails = new TableModel((ArrayList) tableList, tblSubsidyWriteOffTitle);
                } else if (intType.equals("O")) {
                    tblSubsidyDetails = new TableModel((ArrayList) tableList, tblSubsidyOTSTitle);
                }
                cellSubsidyEditableColumnTrue();
                setFinalList(releaseList);
            }
        } catch (Exception e) {
            parseException.logException(e, true);
        }
    }
    
    public void insertTableDataTL(HashMap whereMap, String intType) throws Exception {
        try {
            ArrayList rowList = new ArrayList();
            ArrayList tableList = new ArrayList();
            HashMap dataMap = new HashMap();
            HashMap subsidyMapMap = new HashMap();
            Date dueDate = null;
            Date lastIntCalcDate = null;
            Date releaseDate = null;
            double totalSubsidyAmt = 0.0;
            double subsidyAmtAdjusted = 0.0;
            double balanceSubsidyAmt = 0.0;
            List shgSubsidyList = null;
            if (whereMap.get("SUBSIDY_TYPE").equals("I")) {
                shgSubsidyList = ClientUtil.executeQuery("getFinYearIntSubsidyTL", whereMap);
            }else{
                shgSubsidyList = ClientUtil.executeQuery("getFinYearPrincipalSubsidyTL", whereMap);
            }
            if (shgSubsidyList != null && shgSubsidyList.size() > 0) {
                for (int i = 0; i < shgSubsidyList.size(); i++) {
                    dataMap = (HashMap) shgSubsidyList.get(i);
                    rowList = new ArrayList();
                    totalSubsidyAmt = 0.0;
                    subsidyAmtAdjusted = 0.0;
                    balanceSubsidyAmt = 0.0;
                    rowList.add(new Boolean(false));
                    rowList.add(CommonUtil.convertObjToStr(dataMap.get("ACT_NUM")));
                    rowList.add(CommonUtil.convertObjToStr(dataMap.get("CUSTOMER_NAME")));
                    rowList.add(CommonUtil.convertObjToStr(dataMap.get("ACT_NUM")));
                    //Set Financial Year
                    if (whereMap.get("SUBSIDY_TYPE").equals("I")) {
                        dataMap.put("ADJD_FIN_YEAR", whereMap.get("START_YEAR") + "-" + whereMap.get("END_YEAR"));
                    } else {
                        dataMap.put("ADJD_FIN_YEAR", "");
                    }
                    //Only For TL Purpose
                    dataMap.put("RELEASE_NO", dataMap.get("ACT_NUM"));
                    dataMap.put("KCC_ACT_NUM", dataMap.get("ACT_NUM"));
                    //Total Subsidy Interest
                    if (whereMap.get("SUBSIDY_TYPE").equals("I")) {
                        subsidyMapMap.put("RELEASE_NO", dataMap.get("ACT_NUM"));
                        if (intType.equals("S")) {
                            subsidyMapMap.put("INSTITUTION_NAME", whereMap.get("INSTITUTION_NAME"));
                        }
                        subsidyMapMap.put("RELEASE_NO", dataMap.get("ACT_NUM"));
                        subsidyMapMap.put("START_YEAR", whereMap.get("START_YEAR"));
                        subsidyMapMap.put("END_YEAR", whereMap.get("END_YEAR"));
                        
                        List subsidyList = ClientUtil.executeQuery("getTotalSubsidyAmount", subsidyMapMap);
                        if (subsidyList != null && subsidyList.size() > 0) {
                            subsidyMapMap = (HashMap) subsidyList.get(0);
                            rowList.add(CommonUtil.convertObjToDouble(subsidyMapMap.get("SUBSIDY_AMOUNT")));
                            dataMap.put("TOTAL_SUBSIDY_AMOUNT", CommonUtil.convertObjToStr(subsidyMapMap.get("SUBSIDY_AMOUNT")));
                            totalSubsidyAmt = CommonUtil.convertObjToDouble(subsidyMapMap.get("SUBSIDY_AMOUNT"));
                        } else {
                            rowList.add(CommonUtil.convertObjToDouble(String.valueOf("0")));
                            dataMap.put("TOTAL_SUBSIDY_AMOUNT", CommonUtil.convertObjToStr(String.valueOf("0")));
                        }
                    } else { //Total Subsidy Pricipal
                        totalSubsidyAmt = CommonUtil.convertObjToDouble(dataMap.get("SUBSIDY_AMT"));
                        rowList.add(CommonUtil.convertObjToDouble(dataMap.get("SUBSIDY_AMT")));
                        dataMap.put("TOTAL_SUBSIDY_AMOUNT", CommonUtil.convertObjToStr(dataMap.get("SUBSIDY_AMT")));
                    }

                    //Subsidy Amount Adjusted
                    HashMap adjustedMap = new HashMap();
                    adjustedMap.put("RELEASE_NO", dataMap.get("ACT_NUM"));
                    adjustedMap.put("START_YEAR", whereMap.get("START_YEAR"));
                    adjustedMap.put("END_YEAR", whereMap.get("END_YEAR"));
                    if (intType.equals("S")) {
                        adjustedMap.put("INSTITUTION_NAME", whereMap.get("INSTITUTION_NAME"));
                    }
                    if (whereMap.get("SUBSIDY_TYPE").equals("I")) {
                        adjustedMap.put("ADJD_FIN_YEAR", whereMap.get("START_YEAR") + "-" + whereMap.get("END_YEAR"));
                    }
                    adjustedMap.put("SUBSIDY_TYPE", whereMap.get("SUBSIDY_TYPE"));
                    List adjustedList = ClientUtil.executeQuery("getTotalAdjustedSubsidyAmount", adjustedMap);
                    if (adjustedList != null && adjustedList.size() > 0) {
                        adjustedMap = (HashMap) adjustedList.get(0);
                        if (intType.equals("S")) {
                            rowList.add(CommonUtil.convertObjToDouble(adjustedMap.get("TODAYS_ADJUSTMENT_AMOUNT")));
                            subsidyAmtAdjusted = CommonUtil.convertObjToDouble(adjustedMap.get("TODAYS_ADJUSTMENT_AMOUNT"));
                            dataMap.put("SUBSIDY_ADJUSTED_AMOUNT", CommonUtil.convertObjToStr(String.valueOf(subsidyAmtAdjusted)));
                        } else {
                            rowList.add(CommonUtil.convertObjToDouble(adjustedMap.get("TODAYS_ADJUSTMENT_AMOUNT"))
                                    + CommonUtil.convertObjToDouble(adjustedMap.get("RECOVERY_FROM_CUST_AMOUNT"))
                                    + CommonUtil.convertObjToDouble(adjustedMap.get("WRITE_OFF_AMOUNT"))
                                    + CommonUtil.convertObjToDouble(adjustedMap.get("OTS_AMOUNT")));
                            subsidyAmtAdjusted = CommonUtil.convertObjToDouble(adjustedMap.get("TODAYS_ADJUSTMENT_AMOUNT"))
                                    + CommonUtil.convertObjToDouble(adjustedMap.get("RECOVERY_FROM_CUST_AMOUNT"))
                                    + CommonUtil.convertObjToDouble(adjustedMap.get("WRITE_OFF_AMOUNT"))
                                    + CommonUtil.convertObjToDouble(adjustedMap.get("OTS_AMOUNT"));
                            dataMap.put("SUBSIDY_ADJUSTED_AMOUNT", CommonUtil.convertObjToStr(String.valueOf(subsidyAmtAdjusted)));
                        }
                    } else {
                        rowList.add(CommonUtil.convertObjToDouble(String.valueOf("0")));
                        dataMap.put("SUBSIDY_ADJUSTED_AMOUNT", CommonUtil.convertObjToStr(String.valueOf("0")));
                    }

                    //Calculate Balance Sibsidy Amount
                    balanceSubsidyAmt = totalSubsidyAmt - subsidyAmtAdjusted;
                    if(balanceSubsidyAmt<0){
                        balanceSubsidyAmt = 0;
                    }
                    rowList.add(CommonUtil.convertObjToDouble(String.valueOf(balanceSubsidyAmt)));
                    dataMap.put("BALANCE_SUBSIDY_DUE", CommonUtil.convertObjToStr(String.valueOf(balanceSubsidyAmt)));

                    rowList.add(CommonUtil.convertObjToDouble(String.valueOf("0")));
                    tableList.add(rowList);
                }
                if (intType.equals("S")) {
                    tblSubsidyDetails = new TableModel((ArrayList) tableList, tblSubsidyDetailsTitle);
                } else if (intType.equals("R")) {
                    tblSubsidyDetails = new TableModel((ArrayList) tableList, tblSubsidyRecoveryTitle);
                } else if (intType.equals("W")) {
                    tblSubsidyDetails = new TableModel((ArrayList) tableList, tblSubsidyWriteOffTitle);
                } else if (intType.equals("O")) {
                    tblSubsidyDetails = new TableModel((ArrayList) tableList, tblSubsidyOTSTitle);
                }
                cellSubsidyEditableColumnTrue();
                setFinalList(shgSubsidyList);
            }
        } catch (Exception e) {
            parseException.logException(e, true);
        }
    }

    public void populateFinYearWiseTable(HashMap whereMap, String prodType) {
        whereMap.put(CommonConstants.BRANCH_ID, ProxyParameters.BRANCH_ID);
        HashMap viewMap = new HashMap();
        viewMap.put(CommonConstants.MAP_NAME, "getFinYearWiseOutstandingFromActNum"+prodType);
        viewMap.put(CommonConstants.MAP_WHERE, whereMap);
        viewMap = ClientUtil.executeTableQuery(viewMap);
        ArrayList data = (ArrayList) viewMap.get(CommonConstants.TABLEDATA);
        tblFinYearWise = new EnhancedTableModel(data, tblFinYearWiseTitle);
    }

    /**
     * To perform the appropriate operation
     */
    public void doAction() {
        try {
            doActionPerform();
        } catch (Exception e) {
            setResult(ClientConstants.ACTIONTYPE_FAILED);
            parseException.logException(e, true);
            e.printStackTrace();
        }
    }

    /**
     * To perform the necessary action
     */
    private void doActionPerform() throws Exception {
        final HashMap data = new HashMap();
        data.put("COMMAND", getCommand());
        if (getActionType() == ClientConstants.ACTIONTYPE_AUTHORIZE || getActionType() == ClientConstants.ACTIONTYPE_REJECT) {
            data.put(CommonConstants.AUTHORIZEMAP, get_authorizeMap());
        }
        data.put("SubsidyInterestData", setSubsidyInterestData());
        if (getFinalList() != null && getFinalList().size() > 0) {
            data.put("SUBSIDY_DUE_LIST", getFinalList());
        }
        if (getPrincipalSubsidy().equals("Y")) {
            data.put("PRINCIPAL_SUBSIDY", "Y");
        } else {
            if (allowedTransactionDetailsTO != null && allowedTransactionDetailsTO.size() > 0) {
                if (transactionDetailsTO == null) {
                    transactionDetailsTO = new LinkedHashMap();
                }
                transactionDetailsTO.put(NOT_DELETED_TRANS_TOs, allowedTransactionDetailsTO);
                data.put("TransactionTO", transactionDetailsTO);
                allowedTransactionDetailsTO = null;
            }
        }
        data.put("BRANCH_CODE", TrueTransactMain.BRANCH_ID);
        System.out.println("#$########## data : " + data);
        HashMap proxyResultMap = proxy.execute(data, map);
        setProxyReturnMap(proxyResultMap);
        setResult(actionType);
        _authorizeMap = null;
    }

    public InterestSubsidyAdjustmentTO setSubsidyInterestData() {
        final InterestSubsidyAdjustmentTO objInterestSubsidyAdjustmentTO = new InterestSubsidyAdjustmentTO();
        try {
            objInterestSubsidyAdjustmentTO.setAdjustmentNo(getSubsidyAdjustNo());
            objInterestSubsidyAdjustmentTO.setIntType(getInterestType());
            objInterestSubsidyAdjustmentTO.setSubsidyDate(DateUtil.getDateMMDDYYYY(getTdtSubsidyReleaseDt()));
            objInterestSubsidyAdjustmentTO.setSubsidyRefNo(getTxtSubSidyReleaseRefNo());
            objInterestSubsidyAdjustmentTO.setSubsidyAmountReceived(CommonUtil.convertObjToDouble(getTxtSubsidyReceivedAmt()));
            objInterestSubsidyAdjustmentTO.setSubsidyReceivedPer(CommonUtil.convertObjToDouble(getTxtSubsidyReceivedAmtPer()));
            objInterestSubsidyAdjustmentTO.setAgencyName(getCboAgencyName());
            objInterestSubsidyAdjustmentTO.setProdType(CommonUtil.convertObjToStr(getCbmProdType().getKeyForSelected()));
            objInterestSubsidyAdjustmentTO.setProdId(CommonUtil.convertObjToStr(getCbmProdID().getKeyForSelected()));
            objInterestSubsidyAdjustmentTO.setFromAccNo(getTxtFromAccountNo());
            objInterestSubsidyAdjustmentTO.setToAccNo(getTxtToAccountNo());
            objInterestSubsidyAdjustmentTO.setFromReleaseNo(getTxtFromReleaseNo());
            objInterestSubsidyAdjustmentTO.setToReleaseNo(getTxtToReleaseNo());
            objInterestSubsidyAdjustmentTO.setFromDt(DateUtil.getDateMMDDYYYY(getTdtFromDt()));
            objInterestSubsidyAdjustmentTO.setToDt(DateUtil.getDateMMDDYYYY(getTdtToDt()));
            objInterestSubsidyAdjustmentTO.setOtsSanctionDt(DateUtil.getDateMMDDYYYY(getTdtOTSSanctionDt()));
            objInterestSubsidyAdjustmentTO.setOtsSanctionBy(getCboOTSSanctionedBy());
            objInterestSubsidyAdjustmentTO.setOtsSanctionNo(getTxtOTSSanctionNo());
            objInterestSubsidyAdjustmentTO.setOtsAmount(CommonUtil.convertObjToDouble(getTxtOTSAmount()));
            objInterestSubsidyAdjustmentTO.setRemarks(getTxtRemarks());
            objInterestSubsidyAdjustmentTO.setSubsidyType(getRdoSubsidy());
            objInterestSubsidyAdjustmentTO.setStatus(getAction());
            objInterestSubsidyAdjustmentTO.setStatusBy(TrueTransactMain.USER_ID);
            objInterestSubsidyAdjustmentTO.setStatusDt(ClientUtil.getCurrentDateWithTime());
        } catch (Exception e) {
            log.info("Error In setSubventionInterestData()");
            e.printStackTrace();
        }
        return objInterestSubsidyAdjustmentTO;
    }

    private String getCommand() {
        String command = null;
        System.out.println("actionType : " + actionType);
        switch (actionType) {
            case ClientConstants.ACTIONTYPE_NEW:
                command = CommonConstants.TOSTATUS_INSERT;
                break;
            case ClientConstants.ACTIONTYPE_EDIT:
                command = CommonConstants.TOSTATUS_UPDATE;
                break;
            case ClientConstants.ACTIONTYPE_DELETE:
                command = CommonConstants.TOSTATUS_DELETE;
                break;
            case ClientConstants.ACTIONTYPE_AUTHORIZE:
                command = CommonConstants.STATUS_AUTHORIZED;
                break;
            case ClientConstants.ACTIONTYPE_REJECT:
                command = CommonConstants.STATUS_REJECTED;
                break;
            case ClientConstants.ACTIONTYPE_EXCEPTION:
                command = CommonConstants.STATUS_EXCEPTION;
            default:
        }
        // System.out.println("command : " + command);
        return command;
    }

    private String getAction() {
        String action = null;
        // System.out.println("actionType : " + actionType);
        switch (actionType) {
            case ClientConstants.ACTIONTYPE_NEW:
                action = CommonConstants.STATUS_CREATED;
                break;
            case ClientConstants.ACTIONTYPE_EDIT:
                action = CommonConstants.STATUS_MODIFIED;
                break;
            case ClientConstants.ACTIONTYPE_DELETE:
                action = CommonConstants.STATUS_DELETED;
                break;
            case ClientConstants.ACTIONTYPE_AUTHORIZE:
                action = CommonConstants.STATUS_AUTHORIZED;
                break;
            case ClientConstants.ACTIONTYPE_REJECT:
                action = CommonConstants.STATUS_REJECTED;
                break;
            case ClientConstants.ACTIONTYPE_EXCEPTION:
                action = CommonConstants.STATUS_EXCEPTION;
                break;
            default:
        }
        // System.out.println("command : " + command);
        return action;
    }

    public void resetForm() {
        interestType = "";
        txtSubSidyReleaseRefNo = "";
        tdtSubsidyReleaseDt = "";
        txtSubsidyReceivedAmt = "";
        txtSubsidyReceivedAmtPer = "";
        cboProdType = "";
        cboAgencyName = "";
        cboProdID = "";
        txtFromAccountNo = "";
        txtToAccountNo = "";
        tdtFromDt = "";
        tdtToDt = "";
        txtFromReleaseNo = "";
        txtToReleaseNo = "";
        txtOTSSanctionNo = "";
        tdtOTSSanctionDt = "";
        cboOTSSanctionedBy = "";
        txtOTSAmount = "";
        txtRemarks = "";
        subsidyAdjustNo = "";
        finalList = null;
        resetTableValues();
    }

    public void resetTableValues() {
        tblFinYearWise.setDataArrayList(null, tblFinYearWiseTitle);
        tblSubsidyDetails.setDataArrayList(null, tblSubsidyDetailsTitle);
    }
    
    public void resetSubsidyTableValues() {
        tblSubsidyDetails.setDataArrayList(null, tblSubsidyDetailsTitle);
    }

    public void ttNotifyObservers() {
        notifyObservers();
    }

    public boolean isNewData() {
        return newData;
    }

    public void setNewData(boolean newData) {
        this.newData = newData;
    }

    public java.util.HashMap get_authorizeMap() {
        return _authorizeMap;
    }

    public void set_authorizeMap(java.util.HashMap _authorizeMap) {
        this._authorizeMap = _authorizeMap;
    }

    /**
     * To set the status based on ActionType, either New, Edit, etc.,
     */
    public void setStatus() {
        this.setLblStatus(ClientConstants.ACTION_STATUS[this.getActionType()]);
        ttNotifyObservers();
    }

    public void setResultStatus() {
        this.setLblStatus(ClientConstants.RESULT_STATUS[this.getResult()]);
        ttNotifyObservers();
    }

    public int getResult() {
        return result;
    }

    public void setResult(int result) {
        this.result = result;
    }

    public java.lang.String getLblStatus() {
        return lblStatus;
    }

    public void setLblStatus(java.lang.String lblStatus) {
        this.lblStatus = lblStatus;
    }

    public HashMap getAuthorizeMap() {
        return _authorizeMap;
    }

    public void setAuthorizeMap(HashMap _authorizeMap) {
        this._authorizeMap = _authorizeMap;
    }

    public int getActionType() {
        return actionType;
    }

    public void setActionType(int actionType) {
        this.actionType = actionType;
    }

    /**
     * Getter for property allowedTransactionDetailsTO.
     *
     * @return Value of property allowedTransactionDetailsTO.
     */
    public java.util.LinkedHashMap getAllowedTransactionDetailsTO() {
        return allowedTransactionDetailsTO;
    }

    /**
     * Setter for property allowedTransactionDetailsTO.
     *
     * @param allowedTransactionDetailsTO New value of property
     * allowedTransactionDetailsTO.
     */
    public void setAllowedTransactionDetailsTO(java.util.LinkedHashMap allowedTransactionDetailsTO) {
        this.allowedTransactionDetailsTO = allowedTransactionDetailsTO;
    }

    /**
     * Getter for property transactionDetailsTO.
     *
     * @return Value of property transactionDetailsTO.
     */
    public java.util.LinkedHashMap getTransactionDetailsTO() {
        return transactionDetailsTO;
    }

    /**
     * Setter for property transactionDetailsTO.
     *
     * @param transactionDetailsTO New value of property transactionDetailsTO.
     */
    public void setTransactionDetailsTO(java.util.LinkedHashMap transactionDetailsTO) {
        this.transactionDetailsTO = transactionDetailsTO;
    }

    /**
     * Getter for property deletedTransactionDetailsTO.
     *
     * @return Value of property deletedTransactionDetailsTO.
     */
    public java.util.LinkedHashMap getDeletedTransactionDetailsTO() {
        return deletedTransactionDetailsTO;
    }

    /**
     * Setter for property deletedTransactionDetailsTO.
     *
     * @param deletedTransactionDetailsTO New value of property
     * deletedTransactionDetailsTO.
     */
    public void setDeletedTransactionDetailsTO(java.util.LinkedHashMap deletedTransactionDetailsTO) {
        this.deletedTransactionDetailsTO = deletedTransactionDetailsTO;
    }

    /**
     * Getter for property transactionOB.
     *
     * @return Value of property transactionOB.
     */
    public com.see.truetransact.ui.common.transaction.TransactionOB getTransactionOB() {
        return transactionOB;
    }

    /**
     * Setter for property transactionOB.
     *
     * @param transactionOB New value of property transactionOB.
     */
    public void setTransactionOB(com.see.truetransact.ui.common.transaction.TransactionOB transactionOB) {
        this.transactionOB = transactionOB;
    }

    public ComboBoxModel getCbmAgencyName() {
        return cbmAgencyName;
    }

    public void setCbmAgencyName(ComboBoxModel cbmAgencyName) {
        this.cbmAgencyName = cbmAgencyName;
    }

    public String getCboAgencyName() {
        return cboAgencyName;
    }

    public void setCboAgencyName(String cboAgencyName) {
        this.cboAgencyName = cboAgencyName;
    }

    public String getCboOTSSanctionedBy() {
        return cboOTSSanctionedBy;
    }

    public void setCboOTSSanctionedBy(String cboOTSSanctionedBy) {
        this.cboOTSSanctionedBy = cboOTSSanctionedBy;
    }

    public String getCboProdID() {
        return cboProdID;
    }

    public void setCboProdID(String cboProdID) {
        this.cboProdID = cboProdID;
    }

    public String getCboProdType() {
        return cboProdType;
    }

    public void setCboProdType(String cboProdType) {
        this.cboProdType = cboProdType;
    }

    public String getInterestType() {
        return interestType;
    }

    public void setInterestType(String interestType) {
        this.interestType = interestType;
    }

    public String getTdtFromDt() {
        return tdtFromDt;
    }

    public void setTdtFromDt(String tdtFromDt) {
        this.tdtFromDt = tdtFromDt;
    }

    public String getTdtOTSSanctionDt() {
        return tdtOTSSanctionDt;
    }

    public void setTdtOTSSanctionDt(String tdtOTSSanctionDt) {
        this.tdtOTSSanctionDt = tdtOTSSanctionDt;
    }

    public String getTdtSubsidyReleaseDt() {
        return tdtSubsidyReleaseDt;
    }

    public void setTdtSubsidyReleaseDt(String tdtSubsidyReleaseDt) {
        this.tdtSubsidyReleaseDt = tdtSubsidyReleaseDt;
    }

    public String getTdtToDt() {
        return tdtToDt;
    }

    public void setTdtToDt(String tdtToDt) {
        this.tdtToDt = tdtToDt;
    }

    public String getTxtFromAccountNo() {
        return txtFromAccountNo;
    }

    public void setTxtFromAccountNo(String txtFromAccountNo) {
        this.txtFromAccountNo = txtFromAccountNo;
    }

    public String getTxtFromReleaseNo() {
        return txtFromReleaseNo;
    }

    public void setTxtFromReleaseNo(String txtFromReleaseNo) {
        this.txtFromReleaseNo = txtFromReleaseNo;
    }

    public String getTxtOTSAmount() {
        return txtOTSAmount;
    }

    public void setTxtOTSAmount(String txtOTSAmount) {
        this.txtOTSAmount = txtOTSAmount;
    }

    public String getTxtOTSSanctionNo() {
        return txtOTSSanctionNo;
    }

    public void setTxtOTSSanctionNo(String txtOTSSanctionNo) {
        this.txtOTSSanctionNo = txtOTSSanctionNo;
    }

    public String getTxtRemarks() {
        return txtRemarks;
    }

    public void setTxtRemarks(String txtRemarks) {
        this.txtRemarks = txtRemarks;
    }

    public String getTxtSubSidyReleaseRefNo() {
        return txtSubSidyReleaseRefNo;
    }

    public void setTxtSubSidyReleaseRefNo(String txtSubSidyReleaseRefNo) {
        this.txtSubSidyReleaseRefNo = txtSubSidyReleaseRefNo;
    }

    public String getTxtSubsidyReceivedAmt() {
        return txtSubsidyReceivedAmt;
    }

    public void setTxtSubsidyReceivedAmt(String txtSubsidyReceivedAmt) {
        this.txtSubsidyReceivedAmt = txtSubsidyReceivedAmt;
    }

    public String getTxtToAccountNo() {
        return txtToAccountNo;
    }

    public void setTxtToAccountNo(String txtToAccountNo) {
        this.txtToAccountNo = txtToAccountNo;
    }

    public String getTxtToReleaseNo() {
        return txtToReleaseNo;
    }

    public void setTxtToReleaseNo(String txtToReleaseNo) {
        this.txtToReleaseNo = txtToReleaseNo;
    }

    public ComboBoxModel getCbmProdID() {
        return cbmProdID;
    }

    public void setCbmProdID(ComboBoxModel cbmProdID) {
        this.cbmProdID = cbmProdID;
    }

    public ComboBoxModel getCbmProdType() {
        return cbmProdType;
    }

    public void setCbmProdType(ComboBoxModel cbmProdType) {
        this.cbmProdType = cbmProdType;
    }

    public TableModel getTblSubsidyDetails() {
        return tblSubsidyDetails;
    }

    public void setTblSubsidyDetails(TableModel tblSubsidyDetails) {
        this.tblSubsidyDetails = tblSubsidyDetails;
    }

    public String getTxtSubsidyReceivedAmtPer() {
        return txtSubsidyReceivedAmtPer;
    }

    public void setTxtSubsidyReceivedAmtPer(String txtSubsidyReceivedAmtPer) {
        this.txtSubsidyReceivedAmtPer = txtSubsidyReceivedAmtPer;
    }

    public String getRdoSubsidy() {
        return rdoSubsidy;
    }

    public void setRdoSubsidy(String rdoSubsidy) {
        this.rdoSubsidy = rdoSubsidy;
    }

    public String getSubsidyAdjustNo() {
        return subsidyAdjustNo;
    }

    public void setSubsidyAdjustNo(String subsidyAdjustNo) {
        this.subsidyAdjustNo = subsidyAdjustNo;
    }
}