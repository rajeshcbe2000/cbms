/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition.
 * 
 *
 * DepositInterestApplicationOB.java
 *
 * Created on Mon Jun 13 18:24:58 IST 2011
 */
package com.see.truetransact.ui.deposit.multipletosinglecustomermsg;

import java.util.Date;
import java.util.List;
import java.util.HashMap;
import java.util.ArrayList;
import org.apache.log4j.Logger;
import com.see.truetransact.commonutil.DateUtil;
import com.see.truetransact.commonutil.TTException;
import com.see.truetransact.clientutil.ClientUtil;
import com.see.truetransact.commonutil.CommonUtil;
import com.see.truetransact.uicomponent.CObservable;
import com.see.truetransact.clientproxy.ProxyFactory;
import com.see.truetransact.clientutil.ClientConstants;
import com.see.truetransact.commonutil.CommonConstants;
import com.see.truetransact.clientutil.EnhancedTableModel;
import com.see.truetransact.clientexception.ClientParseException;
import com.see.truetransact.clientproxy.ProxyParameters;
import com.see.truetransact.commonutil.interestcalc.Rounding;

/**
 *
 * @author Sathiya
 */
public class SendSMSOB extends CObservable {

    final ArrayList tableTitle = new ArrayList();
    final ArrayList MDStableTitle = new ArrayList();
    private ArrayList IncVal = new ArrayList();
    private EnhancedTableModel tblDepositInterestApplication;
    private EnhancedTableModel tblMDSPrizedMoneyDetails;
    private EnhancedTableModel tblRenewTransTableData;
    private ProxyFactory proxy;
    private HashMap map;
    private final static Logger log = Logger.getLogger(SendSMSOB.class);
    private final static ClientParseException parseException = ClientParseException.getInstance();
    private int result;
    private int actionType;
    private boolean rdoPrizedMember_Yes = false;
    private boolean rdoPrizedMember_No = false;
    private List finalList = null;
    private List finalTableList = null;
    private String txtProductID = "";
    private String txtTokenNo = "";
    private List calFreqAccountList = null;
    private Date currDt;
    private String tdtTransDate = "";
    private String tdtMDSTransDate = "";
    private String txtSchemeName = "";
    private String tdtRenewTransDate = "";//Added By Kannan AR
    boolean chkRenewTransSingle = false;
    boolean chkRenewTransInterestApp = false;

    public String getTxtSchemeName() {
        return txtSchemeName;
    }

    public void setTxtSchemeName(String txtSchemeName) {
        this.txtSchemeName = txtSchemeName;
    }

    public String getMDSTdtTransDate() {
        return tdtMDSTransDate;
    }

    public boolean isChkRenewTransSingle() {
        return chkRenewTransSingle;
    }

    public boolean isChkRenewTransInterestApp() {
        return chkRenewTransInterestApp;
    }

    public void setChkRenewTransInterestApp(boolean chkRenewTransInterestApp) {
        this.chkRenewTransInterestApp = chkRenewTransInterestApp;
    }

    public void setChkRenewTransSingle(boolean chkRenewTransSingle) {
        this.chkRenewTransSingle = chkRenewTransSingle;
    }

    public void setTdtMDSTransDate(String tdtMDSTransDate) {
        this.tdtMDSTransDate = tdtMDSTransDate;
    }

    public String getTdtTransDate() {
        return tdtMDSTransDate;
    }

    public void setTdtTransDate(String tdtTransDate) {
        this.tdtTransDate = tdtTransDate;
    }

    public SendSMSOB() {
        try {
            currDt = ClientUtil.getCurrentDate();
            proxy = ProxyFactory.createProxy();
            map = new HashMap();
            map.put(CommonConstants.JNDI, "DepositInterestApplicationJNDI");
            map.put(CommonConstants.HOME, "deposit.interestapplication.DepositInterestApplicationHome");
            map.put(CommonConstants.REMOTE, "deposit.interestapplication.DepositInterestApplication");
            setDepositInterestTableTitle();
            setMDSPrizedDetailsTableTitle();
            tblDepositInterestApplication = new EnhancedTableModel(null, tableTitle);
            tblMDSPrizedMoneyDetails = new EnhancedTableModel(null, MDStableTitle);
            tblRenewTransTableData = new EnhancedTableModel(null, tableTitle);
        } catch (Exception e) {
            parseException.logException(e, true);
        }
    }

    public void setDepositInterestTableTitle() {
        tableTitle.add("Select");
        tableTitle.add("Cust ID");
        tableTitle.add("Name");
        tableTitle.add("Trans Date");
        tableTitle.add("Record Count");
//        tableTitle.add("Status");
        IncVal = new ArrayList();
    }

    public void setMDSPrizedDetailsTableTitle() {
        MDStableTitle.add("Select");
        MDStableTitle.add("Auction Date");
        MDStableTitle.add("Amount");
        MDStableTitle.add("Installment No");
        MDStableTitle.add("Due Date");
//        MDStableTitle.add("Status");
        IncVal = new ArrayList();
    }

    public void insertTableData(HashMap whereMap) {
        try {
            ArrayList tableList = new ArrayList();
            HashMap custMap = new HashMap();
            List lst = null;
            if (whereMap.containsKey("DEPOSIT_INTEREST_SCREEN")) {
                custMap.put("SCREEN_NAME", "DEPOSIT_INTEREST_SCREEN");
                custMap.put("INT_AMOUNT_CHECK", "INT_AMOUNT_CHECK");
                custMap.put("TRANS_DT", DateUtil.getDateMMDDYYYY(tdtTransDate));
                lst = ClientUtil.executeQuery("getSMSTodaysNotProcessedRecords", custMap);
                if (lst != null && lst.size() > 0) {
                    for (int i = 0; i < lst.size(); i++) {
                        ArrayList rowList = new ArrayList();
                        HashMap multipletoSingleMap = (HashMap) lst.get(i);
                        rowList.add(new Boolean(false));
                        rowList.add(CommonUtil.convertObjToStr(multipletoSingleMap.get("CUST_ID")));
                        rowList.add(CommonUtil.convertObjToStr(multipletoSingleMap.get("CNAME")));
                        rowList.add(CommonUtil.convertObjToStr(multipletoSingleMap.get("TRANS_DT")));
                        rowList.add(CommonUtil.convertObjToStr(multipletoSingleMap.get("RECORD_COUNT")));
//                        rowList.add("Not Completed");
                        tableList.add(rowList);
                    }
                    setFinalList(tableList);
                }
            } else if (whereMap.containsKey("MDS_PRIZED_MONEY_DETAILS_SCREEN")) {
                custMap.put("SCREEN_NAME", "MDS_PRIZED_MONEY_DETAILS_SCREEN");
                custMap.put("TRANS_DT", DateUtil.getDateMMDDYYYY(tdtMDSTransDate));
                custMap.put("SCHEME_NAME", txtSchemeName);
                custMap.put("DRAW_AUCTION_DATE", DateUtil.getDateMMDDYYYY(tdtMDSTransDate));
                lst = ClientUtil.executeQuery("getMDSPrizedMoneyDetailsRecord", custMap);
                if (lst != null && lst.size() > 0) {
                    Rounding rod = new Rounding();
                    for (int i = 0; i < lst.size(); i++) {
                        ArrayList rowList = new ArrayList();
                        HashMap multipletoSingleMap = (HashMap) lst.get(i);
                        double noOfDivision = CommonUtil.convertObjToDouble(multipletoSingleMap.get("NO_OF_DIVISIONS")).doubleValue();
                        double noOfExistingDivision = CommonUtil.convertObjToDouble(multipletoSingleMap.get("EXISTING_DIVISION_NO")).doubleValue();
                        double installmentAmount = CommonUtil.convertObjToDouble(multipletoSingleMap.get("INSTALLMENT")).doubleValue();
                        String isSpecialScheme = CommonUtil.convertObjToStr(multipletoSingleMap.get("IS_SPECIAL_SCHEME"));
                        installmentAmount = (double) rod.getNearest((long) (installmentAmount * 100), 100) / 100;
                        if (noOfDivision == noOfExistingDivision && isSpecialScheme.equals("N")) {
                            rowList.add(new Boolean(false));
                            rowList.add(CommonUtil.convertObjToStr(multipletoSingleMap.get("DRAW_AUCTION_DATE")));
                            rowList.add(installmentAmount);
                            rowList.add(CommonUtil.convertObjToStr(multipletoSingleMap.get("INSTALLMENT_NO")));
                            rowList.add(CommonUtil.convertObjToStr(multipletoSingleMap.get("NEXT_INSTALLMENT_DATE")));
//                            rowList.add("Not Completed");
                            tableList.add(rowList);
                        } else if (isSpecialScheme.equals("Y")) {
                            rowList.add(new Boolean(false));
                            rowList.add(CommonUtil.convertObjToStr(multipletoSingleMap.get("DRAW_AUCTION_DATE")));
                            rowList.add(installmentAmount);
                            rowList.add(CommonUtil.convertObjToStr(multipletoSingleMap.get("INSTALLMENT_NO")));
                            rowList.add(CommonUtil.convertObjToStr(multipletoSingleMap.get("NEXT_INSTALLMENT_DATE")));
//                            rowList.add("Not Completed");
                            tableList.add(rowList);
                        }
                        setFinalList(tableList);
                    }
                }
            } else if (whereMap.containsKey("DEPOSIT_MULTIPLE_RENEWAL")) { //Added By Kannan AR
                custMap.put("SCREEN_NAME", "DEPOSIT_MULTIPLE_RENEWAL");
                custMap.put("TRANS_DT", DateUtil.getDateMMDDYYYY(getTdtRenewTransDate()));
                lst = ClientUtil.executeQuery("getSMSTodaysNotProcessedRecords", custMap);
                if (lst != null && lst.size() > 0) {
                    for (int i = 0; i < lst.size(); i++) {
                        ArrayList rowList = new ArrayList();
                        HashMap multipletoSingleMap = (HashMap) lst.get(i);
                        rowList.add(new Boolean(false));
                        rowList.add(CommonUtil.convertObjToStr(multipletoSingleMap.get("CUST_ID")));
                        rowList.add(CommonUtil.convertObjToStr(multipletoSingleMap.get("CNAME")));
                        rowList.add(CommonUtil.convertObjToStr(multipletoSingleMap.get("TRANS_DT")));
                        rowList.add(CommonUtil.convertObjToStr(multipletoSingleMap.get("RECORD_COUNT")));
                        tableList.add(rowList);
                    }
                    setFinalList(tableList);
                }
            }
            lst = null;
            custMap = null;
            System.out.println("#$# tableList:" + tableList);
            if (whereMap.containsKey("DEPOSIT_INTEREST_SCREEN")) {
                tblDepositInterestApplication = new EnhancedTableModel((ArrayList) tableList, tableTitle);
            } else if (whereMap.containsKey("MDS_PRIZED_MONEY_DETAILS_SCREEN")) {
                tblMDSPrizedMoneyDetails = new EnhancedTableModel((ArrayList) tableList, MDStableTitle);
            } else if (whereMap.containsKey("DEPOSIT_MULTIPLE_RENEWAL")) {
                tblRenewTransTableData = new EnhancedTableModel((ArrayList) tableList, tableTitle);
            }

        } catch (Exception e) {
            e.printStackTrace();
            parseException.logException(e, true);
        }
    }

    /**
     * To perform the necessary operation
     */
    public void doAction(List finalTableList, String tabName) {
        TTException exception = null;
        log.info("In doAction()");
        try {
            doActionPerform(finalTableList, tabName);
        } catch (Exception e) {
            System.out.println("##$$$##$#$#$#$# Exception e : " + e);
            log.info("Error In doAction()");
            setResult(ClientConstants.ACTIONTYPE_FAILED);
            e.printStackTrace();
            if (e instanceof TTException) {
                exception = (TTException) e;
            }
        }
        if (exception != null) {
            parseException.logException(exception, true);
            setResult(actionType);
        }
    }

    /**
     * To perform the necessary action
     */
    private void doActionPerform(List finalTableList, String tabName) throws Exception {
        final HashMap data = new HashMap();
        data.put("SMS_ACCOUNT_LIST", finalTableList);
        if (chkRenewTransSingle || chkRenewTransInterestApp) {
            data.put("SB_BALANCE", "SB_BALANCE");
        }
        data.put("BRANCH_CODE", ProxyParameters.BRANCH_ID);
        data.put("TRANS_DT", getTdtTransDate());
        data.put(tabName, tabName);
        data.put("SCHEME_NAME", txtSchemeName);
        System.out.println("Data in DepositInterestApplication OB : " + data);
        HashMap proxyResultMap = proxy.execute(data, map);
        chkRenewTransSingle = false;
        chkRenewTransInterestApp = false;
        setProxyReturnMap(proxyResultMap);
        setResult(getActionType());
    }

    public void resetForm() {
        resetTableValues();
        setChanged();
    }

    public void resetMDSForm() {
        resetMDSTableValues();
        setChanged();
    }

    public void resetDepRenewForm() {
        resetDepRenewTableValues();
        setChanged();
    }

    public void resetTableValues() {
        tblDepositInterestApplication.setDataArrayList(null, tableTitle);
    }

    public void resetMDSTableValues() {
        tblMDSPrizedMoneyDetails.setDataArrayList(null, MDStableTitle);
    }

    public void resetDepRenewTableValues() {
        tblRenewTransTableData.setDataArrayList(null, tableTitle);
    }

    /**
     * Getter for property tblMDSPrizedMoneyDetails.
     *
     * @return Value of property tblMDSPrizedMoneyDetails.
     */
    public com.see.truetransact.clientutil.EnhancedTableModel getTblMDSPrizedMoneyDetails() {
        return tblMDSPrizedMoneyDetails;
    }

    /**
     * Setter for property tblMDSPrizedMoneyDetails.
     *
     * @param tblMDSPrizedMoneyDetails New value of property
     * tblMDSPrizedMoneyDetails.
     */
    public void setTblMDSPrizedMoneyDetails(com.see.truetransact.clientutil.EnhancedTableModel tblMDSPrizedMoneyDetails) {
        this.tblMDSPrizedMoneyDetails = tblMDSPrizedMoneyDetails;
    }

    /**
     * Getter for property tblDepositInterestApplication.
     *
     * @return Value of property tblDepositInterestApplication.
     */
    public com.see.truetransact.clientutil.EnhancedTableModel getTblDepositInterestApplication() {
        return tblDepositInterestApplication;
    }

    /**
     * Setter for property tblDepositInterestApplication.
     *
     * @param tblDepositInterestApplication New value of property
     * tblDepositInterestApplication.
     */
    public void setTblDepositInterestApplication(com.see.truetransact.clientutil.EnhancedTableModel tblDepositInterestApplication) {
        this.tblDepositInterestApplication = tblDepositInterestApplication;
    }

    /**
     * Getter for property rdoPrizedMember_Yes.
     *
     * @return Value of property rdoPrizedMember_Yes.
     */
    public boolean getRdoPrizedMember_Yes() {
        return rdoPrizedMember_Yes;
    }

    /**
     * Setter for property rdoPrizedMember_Yes.
     *
     * @param rdoPrizedMember_Yes New value of property rdoPrizedMember_Yes.
     */
    public void setRdoPrizedMember_Yes(boolean rdoPrizedMember_Yes) {
        this.rdoPrizedMember_Yes = rdoPrizedMember_Yes;
    }

    /**
     * Getter for property rdoPrizedMember_No.
     *
     * @return Value of property rdoPrizedMember_No.
     */
    public boolean getRdoPrizedMember_No() {
        return rdoPrizedMember_No;
    }

    /**
     * Setter for property rdoPrizedMember_No.
     *
     * @param rdoPrizedMember_No New value of property rdoPrizedMember_No.
     */
    public void setRdoPrizedMember_No(boolean rdoPrizedMember_No) {
        this.rdoPrizedMember_No = rdoPrizedMember_No;
    }

    /**
     * Getter for property finalList.
     *
     * @return Value of property finalList.
     */
    public java.util.List getFinalList() {
        return finalList;
    }

    /**
     * Setter for property finalList.
     *
     * @param finalList New value of property finalList.
     */
    public void setFinalList(java.util.List finalList) {
        this.finalList = finalList;
    }

    /**
     * Getter for property txtProductID.
     *
     * @return Value of property txtProductID.
     */
    public java.lang.String getTxtProductID() {
        return txtProductID;
    }

    /**
     * Setter for property txtProductID.
     *
     * @param txtProductID New value of property txtProductID.
     */
    public void setTxtProductID(java.lang.String txtProductID) {
        this.txtProductID = txtProductID;
    }

    /**
     * Getter for property result.
     *
     * @return Value of property result.
     */
    public int getResult() {
        return result;
    }

    /**
     * Setter for property result.
     *
     * @param result New value of property result.
     */
    public void setResult(int result) {
        this.result = result;
    }

    /**
     * Getter for property actionType.
     *
     * @return Value of property actionType.
     */
    public int getActionType() {
        return actionType;
    }

    /**
     * Setter for property actionType.
     *
     * @param actionType New value of property actionType.
     */
    public void setActionType(int actionType) {
        this.actionType = actionType;
    }

    /**
     * Getter for property tableTitle.
     *
     * @return Value of property tableTitle.
     */
    public java.util.ArrayList getTableTitle() {
        return tableTitle;
    }

    /**
     * Getter for property txtTokenNo.
     *
     * @return Value of property txtTokenNo.
     */
    public java.lang.String getTxtTokenNo() {
        return txtTokenNo;
    }

    /**
     * Setter for property txtTokenNo.
     *
     * @param txtTokenNo New value of property txtTokenNo.
     */
    public void setTxtTokenNo(java.lang.String txtTokenNo) {
        this.txtTokenNo = txtTokenNo;
    }

    /**
     * Getter for property calFreqAccountList.
     *
     * @return Value of property calFreqAccountList.
     */
    public java.util.List getCalFreqAccountList() {
        return calFreqAccountList;
    }

    /**
     * Setter for property calFreqAccountList.
     *
     * @param calFreqAccountList New value of property calFreqAccountList.
     */
    public void setCalFreqAccountList(java.util.List calFreqAccountList) {
        this.calFreqAccountList = calFreqAccountList;
    }

    public String getTdtRenewTransDate() {
        return tdtRenewTransDate;
    }

    public void setTdtRenewTransDate(String tdtRenewTransDate) {
        this.tdtRenewTransDate = tdtRenewTransDate;
    }

    public EnhancedTableModel getTblRenewTransTableData() {
        return tblRenewTransTableData;
    }

    public void setTblRenewTransTableData(EnhancedTableModel tblRenewTransTableData) {
        this.tblRenewTransTableData = tblRenewTransTableData;
    }

}
