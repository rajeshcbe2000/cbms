/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition.
 * 
 *
 * SanctionMasterOB.java
 * 
 * Created on Fri Feb 15 13:45:38 IST 2013
 */
package com.see.truetransact.ui.termloan.kcctopacs;

import com.see.truetransact.clientexception.ClientParseException;
import com.see.truetransact.clientproxy.ProxyFactory;
import com.see.truetransact.clientutil.*;
import com.see.truetransact.commonutil.CommonConstants;
import com.see.truetransact.commonutil.CommonUtil;
import com.see.truetransact.ui.termloan.GoldLoanOB;
import com.see.truetransact.uicomponent.CObservable;
import java.util.*;
import org.apache.log4j.Logger;
import com.see.truetransact.transferobject.termloan.kcctopacs.NclSubLimitTO;
import javax.swing.event.TableModelEvent;
import com.see.truetransact.transferobject.termloan.kcctopacs.NclSanctionDetailsTO;
import com.see.truetransact.transferobject.termloan.kcctopacs.NclClassificationTO;
import com.see.truetransact.transferobject.termloan.kcctopacs.NclAmtSlabWiseDetTO;
import com.see.truetransact.ui.TrueTransactMain;
import com.see.truetransact.commonutil.DateUtil;
import com.see.truetransact.clientutil.ClientConstants;
import com.see.truetransact.commonutil.CommonConstants;
import com.see.truetransact.uicomponent.CTable;
import com.see.truetransact.uivalidation.CurrencyValidation;
import java.text.*;

/**
 *
 * @author
 */
public class SanctionMasterOB extends CObservable {

    private String txtCustID = "";
    private String lblSanctionNoVal = "";
    private String slNo = "";
    private String tdtSanctionDt = "";
    private String tdtExpiryDt = "";
    private String txtSanctionAmt = "";
    private String txtTotalNoOfShare = "";
    private String txtTotalShareAmount = "";
    private String txtFinancialYear = "";
    private String txtSubLimitAmt = "";
    private String txtFinancialYearEnd = "";
    private String cboCAProdType = "";
    private String cboCAProdId = "";
    private String txtCAAccNo = "";
    private String cboKCCProdType = "";
    private String cboKCCProdId = "";
    private ComboBoxModel cbmCAProdType = null;
    private ComboBoxModel cbmCAProdId = null;
    private ComboBoxModel cbmKCCProdId = null;
    private String txtKCCAccNo = "";
    private String txtRemarks = "";
    private String txtTotMembers = "";
    private String txtSmallFarmers = "";
    private String txtMarginalFarmers = "";
    private String txtWomen = "";
    private String txtOthersMain = "";
    private String txtSC = "";
    private String txtST = "";
    private String txtSlabNoOfMembers = "";
    private String txtMinorityCommunity = "";
    private String txtTenantFarmers = "";
    private String txtOralLessees = "";
    private String txtOthers = "";
    private String txtMisc1 = "";
    private String txtMisc2 = "";
    private String txtAmount = "";
    private String cboFromAmount = "";
    private String cboToAmount = "";
    private String txtNoOfMembers = "";
    private String cboCategory = "";
    private String cboSubCategory = "";
    private ComboBoxModel cbmCategory = null;
    private ComboBoxModel cbmSubCategory = null;
    private ComboBoxModel cbmFromAmt = null;
    private ComboBoxModel cbmToAmt = null;
    private HashMap operationMap;
    private Date curDate;
    private ProxyFactory proxy = null;
    private final static ClientParseException parseException = ClientParseException.getInstance();
    private final static Logger log = Logger.getLogger(SanctionMasterOB.class);
    private HashMap lookUpHash;
    private HashMap keyValue;
    private ArrayList key;
    private ArrayList value;
    private String lblCAAccName = "";
    private String lblKCCAccName = "";
    private String lblTotalLimitAmt = "";
    private String endYear = "";
    private boolean newData = false;
    private boolean membereNewData = false;
    private boolean loanNewData = false;
    private int actionType;
    private int resultStatus;
    private int result;
    private EnhancedTableModel tblSubLimit;
    private EnhancedTableModel tblMemberDetails;
    private EnhancedTableModel tblAmtSlabWise;
    final ArrayList tableTitle = new ArrayList();
    final ArrayList memTabTitle = new ArrayList();
    final ArrayList loantabTitle = new ArrayList();
    private LinkedHashMap sublimitmap;
    private LinkedHashMap membermap;
    private LinkedHashMap loanmap;
    private HashMap _authorizeMap;
    private ArrayList IncVal = new ArrayList();
    private String lblStatus = ClientConstants.ACTION_STATUS[ClientConstants.ACTIONTYPE_CANCEL];
    String SanctionNo;
    private CTable _tblData;
    
    public SanctionMasterOB() {
        try {
            setOperationMap();
            curDate = ClientUtil.getCurrentDate();
            proxy = ProxyFactory.createProxy();
            sanctionMasterOB();
        } catch (Exception e) {
            parseException.logException(e, true);
            e.printStackTrace();
            log.info("SanctionMasterOB..." + e);
        }
    }

    private void sanctionMasterOB() throws Exception {
        setTableTile();
        tblSubLimit = new EnhancedTableModel(null, tableTitle);
        setMemTile();
        tblMemberDetails = new EnhancedTableModel(null, memTabTitle);
        setloanTile();
        tblAmtSlabWise = new EnhancedTableModel(null, loantabTitle);
        fillDropdown();
        notifyObservers();
    }

    public void resetSublimitTableValues() {
        tblSubLimit.setDataArrayList(null, tableTitle);
    }

    public void resetMembertableValues() {
        tblMemberDetails.setDataArrayList(null, memTabTitle);
    }

    public void resetLoantableValues() {
        tblAmtSlabWise.setDataArrayList(null, loantabTitle);
    }

    public void setTableTile() {
//        tableTitle.add("Start Fin_Year");
//        tableTitle.add("End Fin_Year");
        //Changed By Suresh
        tableTitle.add("Financial Year");
        tableTitle.add("Limit Amt");
    }

    public void setMemTile() {
        memTabTitle.add("Sl.no");
        memTabTitle.add("Category");
        memTabTitle.add("Sub Category");
        memTabTitle.add("No.Of Members");
        memTabTitle.add("Amount");
    }

    public void setloanTile() {
        loantabTitle.add("From Amount");
        loantabTitle.add("To Amount");
        loantabTitle.add("No .Of Members");
    }

    /**
     * To populate the appropriate keys and values of all the combo boxes in the
     * screen at the time of TermLoanOB instance creation
     *
     * @throws Exception will throw it to the TermLoanOB constructor
     */
    public void fillDropdown() throws Exception {
        lookUpHash = new HashMap();
        lookUpHash.put(CommonConstants.MAP_NAME, null);

        ArrayList lookup_keys = new ArrayList();
        lookup_keys.add("PRODUCTTYPE");
        lookup_keys.add("FARMER.CATEGORY");
        lookup_keys.add("FARMER.SUB_CATEGORY");
        lookup_keys.add("NCL_LOAN_SLAB_AMOUNT");
        lookUpHash.put(CommonConstants.PARAMFORQUERY, lookup_keys);
        keyValue = ClientUtil.populateLookupData(lookUpHash);
        getKeyValue((HashMap) keyValue.get("PRODUCTTYPE"));
        cbmCAProdType = new ComboBoxModel(key, value);
        cbmCAProdType.removeKeyAndElement("AD");
        cbmCAProdType.removeKeyAndElement("GL");
        cbmCAProdType.removeKeyAndElement("SA");
        cbmCAProdType.removeKeyAndElement("TD");
        cbmCAProdType.removeKeyAndElement("TL");

//        lookUpHash.put(CommonConstants.PARAMFORQUERY, lookup_keys);
//        keyValue = ClientUtil.populateLookupData(lookUpHash);
        getKeyValue((HashMap) keyValue.get("FARMER.CATEGORY"));
        cbmCategory = new ComboBoxModel(key, value);

//        lookUpHash.put(CommonConstants.PARAMFORQUERY, lookup_keys);
//        keyValue = ClientUtil.populateLookupData(lookUpHash);
        getKeyValue((HashMap) keyValue.get("FARMER.SUB_CATEGORY"));
        cbmSubCategory = new ComboBoxModel(key, value);

//        lookUpHash.put(CommonConstants.PARAMFORQUERY, lookup_keys);
//        keyValue = ClientUtil.populateLookupData(lookUpHash);
        getKeyValue((HashMap) keyValue.get("NCL_LOAN_SLAB_AMOUNT"));
        cbmFromAmt = new ComboBoxModel(key, value);
        cbmToAmt = new ComboBoxModel(key, value);

        lookUpHash = new HashMap();
        lookUpHash.put(CommonConstants.MAP_NAME, "Cash.getAccProductAD");
        keyValue = ClientUtil.populateLookupData(lookUpHash);
        getKeyValue((HashMap) keyValue.get("DATA"));
        cbmKCCProdId = new ComboBoxModel(key, value);

        cbmCAProdId = new ComboBoxModel();

        lookup_keys = null;
    }

    private void fillData(HashMap keyValue) throws Exception {
        key = (ArrayList) keyValue.get("KEY");
        value = (ArrayList) keyValue.get("VALUE");
    }

    private void makeComboBoxKeyValuesNull() {
        key = null;
        value = null;
        lookUpHash = null;
        keyValue = null;
    }

    private void getKeyValue(HashMap keyValue) throws Exception {
        key = (ArrayList) keyValue.get(CommonConstants.KEY);
        value = (ArrayList) keyValue.get(CommonConstants.VALUE);
    }

    public void setCbmCAProdId(String prodType) {
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
        cbmCAProdId = new ComboBoxModel(key, value);
        setChanged();
    }

    public String checkAcNoWithoutProdType(String actNum, String prodType) {
        HashMap mapData = new HashMap();
        String msg = "";
        try {//dont delete chck selectalldao
            mapData.put("ACT_NUM", actNum);
            List mapDataList = ClientUtil.executeQuery("getActNumFromAllProducts", mapData);
            if (mapDataList != null && mapDataList.size() > 0) {
                mapData = (HashMap) mapDataList.get(0);
                setTxtCAAccNo(CommonUtil.convertObjToStr(mapData.get("ACT_NUM")));
                setSelectedBranchID(CommonUtil.convertObjToStr(mapData.get("BRANCH_ID")));
                String acProdType = CommonUtil.convertObjToStr(mapData.get("PROD_TYPE"));
                String acProdId = CommonUtil.convertObjToStr(mapData.get("PROD_ID"));
                if (prodType.equals("AD") && !prodType.equals(acProdType)) {
                    msg = "Invalid KCC A/c No...";
                } else if (prodType.equals("AD") && prodType.equals(acProdType)) {
                    getCbmKCCProdId().setKeyForSelected(acProdId);
                } else {
                    setCbmCAProdId(acProdType);
                    getCbmCAProdType().setKeyForSelected(acProdType);
                    getCbmCAProdId().setKeyForSelected(acProdId);
                }
                if (msg.length() == 0) {
                    setAccName(prodType, actNum);
                }
            } else {
                msg = "Account not found...";
            }
            mapDataList = null;
        } catch (Exception e) {
            e.printStackTrace();
        }
        mapData = null;
        return msg;
    }

    private void setAccName(String prodType, String actNum) {
        if (CommonUtil.convertObjToStr(prodType).length() > 0) {

            final HashMap accountNameMap = new HashMap();
            accountNameMap.put("ACC_NUM", actNum);
            final List resultList = ClientUtil.executeQuery("getAccountNumberName" + prodType, accountNameMap);
            if (resultList.size() >= 1) {
                final HashMap resultMap = (HashMap) resultList.get(0);
                if (!prodType.equals("AD")) {
                    this.lblCAAccName = CommonUtil.convertObjToStr(resultMap.get("CUSTOMER_NAME").toString());
                } else {
                    this.lblKCCAccName = CommonUtil.convertObjToStr(resultMap.get("CUSTOMER_NAME").toString());
                }
            }
        }
        setChanged();
    }

    private void setOperationMap() throws Exception {
        operationMap = new HashMap();
        operationMap.put(CommonConstants.JNDI, "SanctionMasterJNDI");
        operationMap.put(CommonConstants.HOME, "SanctionMasterHome");
        operationMap.put(CommonConstants.REMOTE, "SanctionMaster");
    }

    public String getLblSanctionNoVal() {
        return lblSanctionNoVal;
    }

    public void setLblSanctionNoVal(String lblSanctionNoVal) {
        this.lblSanctionNoVal = lblSanctionNoVal;
    }

    // Setter method for txtCustID
    void setTxtCustID(String txtCustID) {
        this.txtCustID = txtCustID;
        setChanged();
    }

    // Getter method for txtCustID
    String getTxtCustID() {
        return this.txtCustID;
    }

    // Setter method for tdtSanctionDt
    void setTdtSanctionDt(String tdtSanctionDt) {
        this.tdtSanctionDt = tdtSanctionDt;
        setChanged();
    }

    // Getter method for tdtSanctionDt
    String getTdtSanctionDt() {
        return this.tdtSanctionDt;
    }

    public String getEndYear() {
        return endYear;
    }

    public void setEndYear(String endYear) {
        this.endYear = endYear;
    }

    // Setter method for tdtExpiryDt
    void setTdtExpiryDt(String tdtExpiryDt) {
        this.tdtExpiryDt = tdtExpiryDt;
        setChanged();
    }

    // Getter method for tdtExpiryDt
    String getTdtExpiryDt() {
        return this.tdtExpiryDt;
    }

    // Setter method for txtSanctionAmt
    void setTxtSanctionAmt(String txtSanctionAmt) {
        this.txtSanctionAmt = txtSanctionAmt;
        setChanged();
    }

    // Getter method for txtSanctionAmt
    String getTxtSanctionAmt() {
        return this.txtSanctionAmt;
    }

    // Setter method for txtTotalNoOfShare
    void setTxtTotalNoOfShare(String txtTotalNoOfShare) {
        this.txtTotalNoOfShare = txtTotalNoOfShare;
        setChanged();
    }

    // Getter method for txtTotalNoOfShare
    String getTxtTotalNoOfShare() {
        return this.txtTotalNoOfShare;
    }

    // Setter method for txtTotalShareAmount
    void setTxtTotalShareAmount(String txtTotalShareAmount) {
        this.txtTotalShareAmount = txtTotalShareAmount;
        setChanged();
    }

    // Getter method for txtTotalShareAmount
    String getTxtTotalShareAmount() {
        return this.txtTotalShareAmount;
    }

    // Setter method for txtFinancialYear
    void setTxtFinancialYear(String txtFinancialYear) {
        this.txtFinancialYear = txtFinancialYear;
        setChanged();
    }

    public String getSlNo() {
        return slNo;
    }

    public void setSlNo(String slNo) {
        this.slNo = slNo;
    }

    // Getter method for txtFinancialYear
    String getTxtFinancialYear() {
        return this.txtFinancialYear;
    }

    // Setter method for txtSubLimitAmt
    void setTxtSubLimitAmt(String txtSubLimitAmt) {
        this.txtSubLimitAmt = txtSubLimitAmt;
        setChanged();
    }

    // Getter method for txtSubLimitAmt
    String getTxtSubLimitAmt() {
        return this.txtSubLimitAmt;
    }

    // Setter method for txtFinancialYearEnd
    void setTxtFinancialYearEnd(String txtFinancialYearEnd) {
        this.txtFinancialYearEnd = txtFinancialYearEnd;
        setChanged();
    }

    // Getter method for txtFinancialYearEnd
    String getTxtFinancialYearEnd() {
        return this.txtFinancialYearEnd;
    }

    // Setter method for cboCAProdType
    void setCboCAProdType(String cboCAProdType) {
        this.cboCAProdType = cboCAProdType;
        setChanged();
    }

    // Getter method for cboCAProdType
    String getCboCAProdType() {
        return this.cboCAProdType;
    }

    // Setter method for cboCAProdId
    void setCboCAProdId(String cboCAProdId) {
        this.cboCAProdId = cboCAProdId;
        setChanged();
    }

    // Getter method for cboCAProdId
    String getCboCAProdId() {
        return this.cboCAProdId;
    }

    // Setter method for txtCAAccNo
    void setTxtCAAccNo(String txtCAAccNo) {
        this.txtCAAccNo = txtCAAccNo;
        setChanged();
    }

    // Getter method for txtCAAccNo
    String getTxtCAAccNo() {
        return this.txtCAAccNo;
    }

    // Setter method for cboKCCProdType
    void setCboKCCProdType(String cboKCCProdType) {
        this.cboKCCProdType = cboKCCProdType;
        setChanged();
    }

    // Getter method for cboKCCProdType
    String getCboKCCProdType() {
        return this.cboKCCProdType;
    }

    // Setter method for cboKCCProdId
    void setCboKCCProdId(String cboKCCProdId) {
        this.cboKCCProdId = cboKCCProdId;
        setChanged();
    }

    // Getter method for cboKCCProdId
    String getCboKCCProdId() {
        return this.cboKCCProdId;
    }

    // Setter method for txtKCCAccNo
    void setTxtKCCAccNo(String txtKCCAccNo) {
        this.txtKCCAccNo = txtKCCAccNo;
        setChanged();
    }

    // Getter method for txtKCCAccNo
    String getTxtKCCAccNo() {
        return this.txtKCCAccNo;
    }

    // Setter method for txtRemarks
    void setTxtRemarks(String txtRemarks) {
        this.txtRemarks = txtRemarks;
        setChanged();
    }

    // Getter method for txtRemarks
    String getTxtRemarks() {
        return this.txtRemarks;
    }

    // Setter method for txtTotMembers
    void setTxtTotMembers(String txtTotMembers) {
        this.txtTotMembers = txtTotMembers;
        setChanged();
    }

    // Getter method for txtTotMembers
    String getTxtTotMembers() {
        return this.txtTotMembers;
    }

    // Setter method for txtSmallFarmers
    void setTxtSmallFarmers(String txtSmallFarmers) {
        this.txtSmallFarmers = txtSmallFarmers;
        setChanged();
    }

    // Getter method for txtSmallFarmers
    String getTxtSmallFarmers() {
        return this.txtSmallFarmers;
    }

    // Setter method for txtMarginalFarmers
    void setTxtMarginalFarmers(String txtMarginalFarmers) {
        this.txtMarginalFarmers = txtMarginalFarmers;
        setChanged();
    }

    // Getter method for txtMarginalFarmers
    String getTxtMarginalFarmers() {
        return this.txtMarginalFarmers;
    }

    // Setter method for txtWomen
    void setTxtWomen(String txtWomen) {
        this.txtWomen = txtWomen;
        setChanged();
    }
    // Getter method for txtWomen

    String getTxtWomen() {
        return this.txtWomen;
    }

    // Setter method for txtOthersMain
    void setTxtOthersMain(String txtOthersMain) {
        this.txtOthersMain = txtOthersMain;
        setChanged();
    }
    // Getter method for txtOthersMain

    String getTxtOthersMain() {
        return this.txtOthersMain;
    }

    // Setter method for txtSC
    void setTxtSC(String txtSC) {
        this.txtSC = txtSC;
        setChanged();
    }
    // Getter method for txtSC

    String getTxtSC() {
        return this.txtSC;
    }

    // Setter method for txtST
    void setTxtST(String txtST) {
        this.txtST = txtST;
        setChanged();
    }
    // Getter method for txtST

    String getTxtST() {
        return this.txtST;
    }

    // Setter method for txtMinorityCommunity
    void setTxtMinorityCommunity(String txtMinorityCommunity) {
        this.txtMinorityCommunity = txtMinorityCommunity;
        setChanged();
    }
    // Getter method for txtMinorityCommunity

    String getTxtMinorityCommunity() {
        return this.txtMinorityCommunity;
    }

    // Setter method for txtTenantFarmers
    void setTxtTenantFarmers(String txtTenantFarmers) {
        this.txtTenantFarmers = txtTenantFarmers;
        setChanged();
    }
    // Getter method for txtTenantFarmers

    String getTxtTenantFarmers() {
        return this.txtTenantFarmers;
    }

    // Setter method for txtOralLessees
    void setTxtOralLessees(String txtOralLessees) {
        this.txtOralLessees = txtOralLessees;
        setChanged();
    }
    // Getter method for txtOralLessees

    String getTxtOralLessees() {
        return this.txtOralLessees;
    }

    // Setter method for txtOthers
    void setTxtOthers(String txtOthers) {
        this.txtOthers = txtOthers;
        setChanged();
    }
    // Getter method for txtOthers

    String getTxtOthers() {
        return this.txtOthers;
    }

    // Setter method for txtMisc1
    void setTxtMisc1(String txtMisc1) {
        this.txtMisc1 = txtMisc1;
        setChanged();
    }
    // Getter method for txtMisc1

    String getTxtMisc1() {
        return this.txtMisc1;
    }

    // Setter method for txtMisc2
    void setTxtMisc2(String txtMisc2) {
        this.txtMisc2 = txtMisc2;
        setChanged();
    }
    // Getter method for txtMisc2

    String getTxtMisc2() {
        return this.txtMisc2;
    }

    // Setter method for cboFromAmount
    void setCboFromAmount(String cboFromAmount) {
        this.cboFromAmount = cboFromAmount;
        setChanged();
    }
    // Getter method for cboFromAmount

    String getCboFromAmount() {
        return this.cboFromAmount;
    }

    // Setter method for cboToAmount
    void setCboToAmount(String cboToAmount) {
        this.cboToAmount = cboToAmount;
        setChanged();
    }
    // Getter method for cboToAmount

    String getCboToAmount() {
        return this.cboToAmount;
    }

    // Setter method for txtNoOfMembers
    void setTxtNoOfMembers(String txtNoOfMembers) {
        this.txtNoOfMembers = txtNoOfMembers;
        setChanged();
    }
    // Getter method for txtNoOfMembers

    String getTxtNoOfMembers() {
        return this.txtNoOfMembers;
    }

    public ComboBoxModel getCbmCAProdId() {
        return cbmCAProdId;
    }

    public void setCbmCAProdId(ComboBoxModel cbmCAProdId) {
        this.cbmCAProdId = cbmCAProdId;
    }

    public ComboBoxModel getCbmCAProdType() {
        return cbmCAProdType;
    }

    public void setCbmCAProdType(ComboBoxModel cbmCAProdType) {
        this.cbmCAProdType = cbmCAProdType;
    }

    public ComboBoxModel getCbmKCCProdId() {
        return cbmKCCProdId;
    }

    public void setCbmKCCProdId(ComboBoxModel cbmKCCProdId) {
        this.cbmKCCProdId = cbmKCCProdId;
    }

    public String getLblCAAccName() {
        return lblCAAccName;
    }

    public void setLblCAAccName(String lblCAAccName) {
        this.lblCAAccName = lblCAAccName;
    }

    public String getLblKCCAccName() {
        return lblKCCAccName;
    }

    public void setLblKCCAccName(String lblKCCAccName) {
        this.lblKCCAccName = lblKCCAccName;
    }

    public String getLblTotalLimitAmt() {
        return lblTotalLimitAmt;
    }

    public void setLblTotalLimitAmt(String lblTotalLimitAmt) {
        this.lblTotalLimitAmt = lblTotalLimitAmt;
    }

    public int getActionType() {
        return actionType;
    }

    public void setActionType(int actionType) {
        this.actionType = actionType;
        setChanged();
    }

    /**
     * It will call the notifyObservers()
     */
    public void ttNotifyObservers() {
        notifyObservers();
    }

    public String getLblStatus() {
        return lblStatus;
    }

    public void setLblStatus(String lblStatus) {
        this.lblStatus = lblStatus;
        setChanged();
    }

    /**
     * Getter for property newData.
     *
     * @return Value of property newData.
     */
    public boolean isNewData() {
        return newData;
    }

    /**
     * Setter for property newData.
     *
     * @param newData New value of property newData.
     */
    public void setNewData(boolean newData) {
        this.newData = newData;
    }

    public boolean isLoanNewData() {
        return loanNewData;
    }

    public void setLoanNewData(boolean loanNewData) {
        this.loanNewData = loanNewData;
    }

    public boolean isMembereNewData() {
        return membereNewData;
    }

    public void setMembereNewData(boolean membereNewData) {
        this.membereNewData = membereNewData;
    }

    /**
     * Getter for property tblSubLimit.
     *
     * @return Value of property tblSubLimit.
     */
    public com.see.truetransact.clientutil.EnhancedTableModel getTblSubLimit() {
        return tblSubLimit;
    }

    /**
     * Setter for property tblSubLimit.
     *
     * @param tblSubLimit New value of property tblSubLimit.
     */
    public void setTblSubLimit(com.see.truetransact.clientutil.EnhancedTableModel tblSubLimit) {
        this.tblSubLimit = tblSubLimit;
    }

    public ComboBoxModel getCbmFromAmt() {
        return cbmFromAmt;
    }

    public void setCbmFromAmt(ComboBoxModel cbmFromAmt) {
        this.cbmFromAmt = cbmFromAmt;
    }

    public ComboBoxModel getCbmToAmt() {
        return cbmToAmt;
    }

    public void setCbmToAmt(ComboBoxModel cbmToAmt) {
        this.cbmToAmt = cbmToAmt;
    }

    /**
     * Getter for property _authorizeMap.
     *
     * @return Value of property _authorizeMap.
     */
    public java.util.HashMap get_authorizeMap() {
        return _authorizeMap;
    }

    /**
     * Setter for property _authorizeMap.
     *
     * @param _authorizeMap New value of property _authorizeMap.
     */
    public void set_authorizeMap(java.util.HashMap _authorizeMap) {
        this._authorizeMap = _authorizeMap;
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
     * will reset the current status to Cancel mode
     */
    public void resetStatus() {
        this.setLblStatus(ClientConstants.ACTION_STATUS[0]);
    }

    /**
     * Getter for property tblMemberDetails.
     *
     * @return Value of property tblSubLimit.
     */
    public com.see.truetransact.clientutil.EnhancedTableModel getTblMemberDetails() {
        return tblMemberDetails;
    }

    /**
     * Setter for property tblMemberDetails.
     *
     * @param tblMemberDetails New value of property tblSubLimit.
     */
    public void setTblMemberDetails(com.see.truetransact.clientutil.EnhancedTableModel tblMemberDetails) {
        this.tblMemberDetails = tblMemberDetails;
    }

    public ComboBoxModel getCbmCategory() {
        return cbmCategory;
    }

    public void setCbmCategory(ComboBoxModel cbmCategory) {
        this.cbmCategory = cbmCategory;
    }

    public ComboBoxModel getCbmSubCategory() {
        return cbmSubCategory;
    }

    public void setCbmSubCategory(ComboBoxModel cbmSubCategory) {
        this.cbmSubCategory = cbmSubCategory;
    }

    public String getCboCategory() {
        return cboCategory;
    }

    public void setCboCategory(String cboCategory) {
        this.cboCategory = cboCategory;
    }

    public String getCboSubCategory() {
        return cboSubCategory;
    }

    public void setCboSubCategory(String cboSubCategory) {
        this.cboSubCategory = cboSubCategory;
    }

    public String getTxtAmount() {
        return txtAmount;
    }

    public void setTxtAmount(String txtAmount) {
        this.txtAmount = txtAmount;
    }

    public String getTxtSlabNoOfMembers() {
        return txtSlabNoOfMembers;
    }

    public void setTxtSlabNoOfMembers(String txtSlabNoOfMembers) {
        this.txtSlabNoOfMembers = txtSlabNoOfMembers;
    }

    /**
     * Getter for property tblAmtSlabWise.
     *
     * @return Value of property tblAmtSlabWise.
     */
    public com.see.truetransact.clientutil.EnhancedTableModel getTbltblAmtSlabWise() {
        return tblAmtSlabWise;
    }

    /**
     * Setter for property tblAmtSlabWise.
     *
     * @param tblAmtSlabWise New value of property tblAmtSlabWise.
     */
    public void setTbllLoanSlabwiseDetails(com.see.truetransact.clientutil.EnhancedTableModel tblAmtSlabWise) {
        this.tblAmtSlabWise = tblAmtSlabWise;
    }

    /**
     * To set the status based on ActionType, either New, Edit, etc.,
     */
    public void setStatus() {
        this.setLblStatus(ClientConstants.ACTION_STATUS[this.getActionType()]);
        ttNotifyObservers();
    }

    public void resetSublimitTable() {
        setTxtFinancialYear("");
        setTxtSubLimitAmt("");
        setTxtFinancialYearEnd("");
    }

    public void resetMemberTable() {
        setCboCategory("");
        setCboSubCategory("");
        setTxtAmount("");
        setTxtNoOfMembers("");
    }

    public void resetLoanTable() {
        setCboToAmount("");
        setCboFromAmount("");
        setTxtSlabNoOfMembers("");
    }

    public void resetForm() {
        setTxtCustID("");
        setLblSanctionNoVal("");
        setTdtSanctionDt("");
        setTdtExpiryDt("");
        setTxtSanctionAmt("");
        setTxtRemarks("");
        setCboCAProdId("");
        setCboKCCProdType("");
        setTxtCAAccNo("");
        setTxtKCCAccNo("");
        setCboKCCProdId("");
        setCboCAProdType("");
        setEndYear("");
        membermap = null;
        sublimitmap = null;
        loanmap = null;
    }

    public void addDataToSublimitTable(int rowSelected, boolean updateMode) {
        try {
            int rowSel = rowSelected;
            final NclSubLimitTO sublimitTO = new NclSubLimitTO();
            if (sublimitmap == null) {
                sublimitmap = new LinkedHashMap();
            }
            if (isNewData()) {
                ArrayList data = tblSubLimit.getDataArrayList();
            }
            sublimitTO.setStartFinYear(CommonUtil.convertObjToDouble(getTxtFinancialYear()));
            sublimitTO.setEndFinYear(CommonUtil.convertObjToDouble(getTxtFinancialYearEnd()));
//            sublimitTO.setLimitAmt(CommonUtil.convertObjToDouble(getTxtSubLimitAmt()));
            sublimitTO.setLimitAmt(CommonUtil.convertObjToStr(ClientUtil.convertObjToCurrency(getTxtSubLimitAmt())));
            sublimitTO.setNclSanctionNo(getLblSanctionNoVal());
            sublimitmap.put(CommonUtil.convertObjToStr(sublimitTO.getStartFinYear()), sublimitTO);
//            System.out.println("$$$$$$sublimitmap : "+sublimitmap);
            updateSublimitTable(rowSel, sublimitTO);
        } catch (Exception e) {
            parseException.logException(e, true);
        }
    }
    
    private void updateSublimitTable(int rowSel, NclSubLimitTO sublimitTO) throws Exception {
        Object selectedRow;
        String selectedRowData="";
        boolean rowExists = false;
        //If row already exists update it, else create a new row & append        
        for (int i = tblSubLimit.getRowCount(), j = 0; i > 0; i--, j++) {
            selectedRow = ((ArrayList) tblSubLimit.getDataArrayList().get(j)).get(0);
            //Changed By Suresh
            selectedRowData = CommonUtil.convertObjToStr(selectedRow);
            selectedRowData = selectedRowData.substring(0,selectedRowData.indexOf("-"));
            if (CommonUtil.convertObjToStr(sublimitTO.getStartFinYear()).equals(CommonUtil.convertObjToStr(selectedRowData))) {
                rowExists = true;
                ArrayList SubRow = new ArrayList();
                ArrayList data = tblSubLimit.getDataArrayList();
                data.remove(rowSel);
                //Changed By Suresh
//                SubRow.add(sublimitTO.getStartFinYear());
//                SubRow.add(sublimitTO.getEndFinYear());
                SubRow.add(CommonUtil.convertObjToStr(sublimitTO.getStartFinYear())+"-"+CommonUtil.convertObjToStr(sublimitTO.getEndFinYear()));
                SubRow.add(sublimitTO.getLimitAmt());
                tblSubLimit.insertRow(rowSel, SubRow);
                
                SubRow = null;                
            }
        }
        if (!rowExists) {
            ArrayList SubRow = new ArrayList();
            //Changed By Suresh
//            SubRow.add(sublimitTO.getStartFinYear());
//            SubRow.add(sublimitTO.getEndFinYear());            
            SubRow.add(CommonUtil.convertObjToStr(sublimitTO.getStartFinYear())+"-"+CommonUtil.convertObjToStr(sublimitTO.getEndFinYear()));
            SubRow.add(sublimitTO.getLimitAmt());
            tblSubLimit.insertRow(tblSubLimit.getRowCount(), SubRow); 
              
//            setRightAlignment(1);            
            SubRow = null;            
        }
    }
                    
    public void populateSublimitTableDetails(String row) {
        try {
            resetSublimitTable();
            final NclSubLimitTO sublimitTO = (NclSubLimitTO) sublimitmap.get(row);
            populateSublimitTableData(sublimitTO);
        } catch (Exception e) {
            parseException.logException(e, true);
        }
    }

    private void populateSublimitTableData(NclSubLimitTO sublimitTO) throws Exception {
        setTxtFinancialYear(CommonUtil.convertObjToStr(sublimitTO.getStartFinYear()));
        setTxtFinancialYearEnd(CommonUtil.convertObjToStr(sublimitTO.getEndFinYear()));
        setTxtSubLimitAmt(CommonUtil.convertObjToStr(sublimitTO.getLimitAmt()));
        setChanged();
        notifyObservers();
    }

    public void deleteSublimitTableData(String val, int row) {
        NclSubLimitTO sublimitTO = (NclSubLimitTO) sublimitmap.get(val);
        Object obj;
        obj = val;
        sublimitmap.remove(val);
        resetSublimitTableValues();
        try {
            populateSublimitTable();
        } catch (Exception e) {
            parseException.logException(e, true);
        }
    }

    private void populateSublimitTable() throws Exception {
        ArrayList DataList = new ArrayList();
        DataList = new ArrayList(sublimitmap.keySet());
        ArrayList addList = new ArrayList(sublimitmap.keySet());
        int length = DataList.size();
        for (int i = 0; i < length; i++) {
            ArrayList incTabRow = new ArrayList();
            NclSubLimitTO sublimitTO = (NclSubLimitTO) sublimitmap.get(addList.get(i));
            IncVal.add(sublimitTO);
            //Changed By Suresh
//            incTabRow.add(sublimitTO.getStartFinYear());
//            incTabRow.add(sublimitTO.getEndFinYear());
            incTabRow.add(CommonUtil.convertObjToStr(sublimitTO.getStartFinYear())+"-"+CommonUtil.convertObjToStr(sublimitTO.getEndFinYear()));
//            incTabRow.add(sublimitTO.getLimitAmt());
            incTabRow.add(CommonUtil.convertObjToStr(ClientUtil.convertObjToCurrency(sublimitTO.getLimitAmt())));
            tblSubLimit.addRow(incTabRow);
        }
        notifyObservers();
    }

    public void addDataToMemberDetailsTable(int rowSelected, boolean updateMode) {
        try {
            int rowSel = rowSelected;
            final NclClassificationTO memberTO = new NclClassificationTO();

            if (membermap == null) {
                membermap = new LinkedHashMap();
            }
            int slno = 0;
            int nums[] = new int[50];
            int max = nums[0];
            if (!updateMode) {
                ArrayList data = tblMemberDetails.getDataArrayList();
                slno = serialNo(data);
            } else {
                if (isMembereNewData()) {
                    ArrayList data = tblMemberDetails.getDataArrayList();
                    slno = serialNo(data);
                } else {
                    int b = CommonUtil.convertObjToInt(tblMemberDetails.getValueAt(rowSelected, 0));
                    slno = b;
                }
            }
            memberTO.setCountMembers(CommonUtil.convertObjToDouble((getTxtTotMembers())));
            memberTO.setNclSanctionNo(getLblSanctionNoVal());
            memberTO.setTotMembers(CommonUtil.convertObjToDouble(getTxtNoOfMembers()));
            memberTO.setAmt(CommonUtil.convertObjToStr(ClientUtil.convertObjToCurrency(getTxtAmount())));
//            memberTO.setAmt(CommonUtil.convertObjToDouble(getTxtAmount()));
            memberTO.setCategory(getCboCategory());
            memberTO.setSubcategory(getCboSubCategory());
            memberTO.setSlNo(String.valueOf(slno));
            membermap.put(slno, memberTO);
//            System.out.println("$$$$$$$membermap : "+membermap);
            String sno = String.valueOf(slno);
            updateMemberTable(rowSel, sno,memberTO);
        } catch (Exception e) {
            parseException.logException(e, true);
        }
    }

    private void updateMemberTable(int rowSel, String sno,NclClassificationTO memberTO) throws Exception {
        Object selectedRow;
        boolean rowExists = false;
        //If row already exists update it, else create a new row & append        
        for (int i = tblMemberDetails.getRowCount(), j = 0; i > 0; i--, j++) {
            selectedRow = ((ArrayList) tblMemberDetails.getDataArrayList().get(j)).get(0);
            if (sno.equals(CommonUtil.convertObjToStr(selectedRow))) {
                rowExists = true;
                ArrayList MemRow = new ArrayList();
                ArrayList data = tblMemberDetails.getDataArrayList();
                data.remove(rowSel);
                MemRow.add(sno);
                MemRow.add(CommonUtil.convertObjToStr(getCboCategory()));
                MemRow.add(CommonUtil.convertObjToStr(getCboSubCategory()));
                MemRow.add(CommonUtil.convertObjToDouble(txtNoOfMembers));
//                MemRow.add(CommonUtil.convertObjToDouble(txtAmount));
                MemRow.add(memberTO.getAmt());
                tblMemberDetails.insertRow(rowSel, MemRow);
                MemRow = null;
            }
        }

        if (!rowExists) {
            ArrayList MemRow = new ArrayList();
            MemRow.add(String.valueOf(sno));
            MemRow.add(CommonUtil.convertObjToStr(getCboCategory()));
            MemRow.add(CommonUtil.convertObjToStr(getCboSubCategory()));
            MemRow.add(CommonUtil.convertObjToDouble(txtNoOfMembers));
//            MemRow.add(CommonUtil.convertObjToDouble(txtAmount));
            MemRow.add(memberTO.getAmt());
            tblMemberDetails.insertRow(tblMemberDetails.getRowCount(), MemRow);
            MemRow = null;
        }
    }

    public int serialNo(ArrayList data) {
        final int dataSize = data.size();
        int nums[] = new int[50];
        int max = nums[0];
        int slno = 0;
        int a = 0;
        slno = dataSize + 1;
        for (int i = 0; i < data.size(); i++) {
            a = CommonUtil.convertObjToInt(tblMemberDetails.getValueAt(i, 0));
            nums[i] = a;
            if (nums[i] > max) {
                max = nums[i];
            }
            slno = max + 1;
        }
        return slno;
    }

    public void deleteMemberTableData(int val, int row) {
        NclClassificationTO memberTO = (NclClassificationTO) membermap.get(val);
        Object obj;
        obj = val;
        membermap.remove(val);
        resetMembertableValues();
        try {
            populateMemberTable();
        } catch (Exception e) {
            parseException.logException(e, true);
        }
    }

    private void populateMemberTable() throws Exception {
        ArrayList DataList = new ArrayList();
        DataList = new ArrayList(membermap.keySet());
        ArrayList addList = new ArrayList(membermap.keySet());
        int length = DataList.size();
        for (int i = 0; i < length; i++) {
            ArrayList incTabRow = new ArrayList();
            NclClassificationTO memberTO = (NclClassificationTO) membermap.get(addList.get(i));
            IncVal.add(memberTO);
            incTabRow.add(memberTO.getSlNo());
            incTabRow.add(memberTO.getCategory());
            incTabRow.add(memberTO.getSubcategory());
            incTabRow.add(memberTO.getTotMembers());
            incTabRow.add(CommonUtil.convertObjToStr(ClientUtil.convertObjToCurrency(memberTO.getAmt())));
//            incTabRow.add(memberTO.getAmt());
            tblMemberDetails.addRow(incTabRow);
        }
    }

    public void deleteloanTableData(String val, int row) {
        NclAmtSlabWiseDetTO loanTO = (NclAmtSlabWiseDetTO) loanmap.get(val);
        Object obj;
        obj = val;
        loanmap.remove(val);
        convertProperDataForAmtSlabDetails();        
        resetLoantableValues();
        try {
            populateloanTable();            
        } catch (Exception e) {
            parseException.logException(e, true);
        }
    }

    private void populateloanTable() throws Exception {
        ArrayList DataList = new ArrayList();
        DataList = new ArrayList(loanmap.keySet());
        ArrayList addList = new ArrayList(loanmap.keySet());
        int length = DataList.size();
        for (int i = 0; i < length; i++) {
            ArrayList incTabRow = new ArrayList();
            NclAmtSlabWiseDetTO loanTO = (NclAmtSlabWiseDetTO) loanmap.get(addList.get(i));
            IncVal.add(loanTO);
            incTabRow.add(CommonUtil.convertObjToStr(ClientUtil.convertObjToCurrency(loanTO.getFromAmt())));
            incTabRow.add(CommonUtil.convertObjToStr(ClientUtil.convertObjToCurrency(loanTO.getToAmt())));  
//            incTabRow.add(loanTO.getFromAmt());
//            incTabRow.add(loanTO.getToAmt());
            incTabRow.add(loanTO.getSlabNoOfMembers());
            tblAmtSlabWise.addRow(incTabRow);
        }
    }
       
    public void populateMemberTableDetails(int row) {
        try {
            resetMemberTable();
            final NclClassificationTO memberTO = (NclClassificationTO) membermap.get(row);
            populateMemberTableData(memberTO);
        } catch (Exception e) {
            parseException.logException(e, true);
        }
    }

    private void populateMemberTableData(NclClassificationTO memberTO) throws Exception {
        setTxtTotMembers(CommonUtil.convertObjToStr(memberTO.getCountMembers()));
        setCboCategory(memberTO.getCategory());
        setCboSubCategory(memberTO.getSubcategory());
        setTxtAmount(CommonUtil.convertObjToStr(memberTO.getAmt()));
        setTxtNoOfMembers(CommonUtil.convertObjToStr(memberTO.getTotMembers()));
        setChanged();
    }

    public void populateLoanTableDetails(String row) {
        try {
            resetLoanTable();
            final NclAmtSlabWiseDetTO loanTO = (NclAmtSlabWiseDetTO) loanmap.get(row);
            populateLoanTableData(loanTO);
        } catch (Exception e) {
            parseException.logException(e, true);
        }
    }

    private void populateLoanTableData(NclAmtSlabWiseDetTO loanTO) throws Exception {
        String frmAmt = loanTO.getFromAmt();
        frmAmt = frmAmt.replace(".00", "");
        frmAmt = frmAmt.replace(",", "");        
        String ToAmt = loanTO.getToAmt();
        ToAmt = ToAmt.replace(".00", "");
        ToAmt = ToAmt.replace(",", "");
        setCboFromAmount(ClientUtil.convertObjToCurrency(frmAmt));
        setCboToAmount(ClientUtil.convertObjToCurrency(ToAmt));
        
//        setCboFromAmount(ClientUtil.convertObjToCurrency(loanTO.getFromAmt()));
//        setCboToAmount(ClientUtil.convertObjToCurrency(loanTO.getToAmt()));
        setTxtSlabNoOfMembers(CommonUtil.convertObjToStr(loanTO.getSlabNoOfMembers()));        
        setChanged();
    }

    public void addDataToLoanAmtSlabWiseTable(int rowSelected, boolean updateMode) {
        try {
            int rowSel = rowSelected;
            final NclAmtSlabWiseDetTO loanTO = new NclAmtSlabWiseDetTO();
            if (loanmap == null) {
                loanmap = new LinkedHashMap();
            }
            if (isLoanNewData()) {
                ArrayList data = tblAmtSlabWise.getDataArrayList();
            }
            loanTO.setNclSanctionNo(getLblSanctionNoVal());            
            loanTO.setFromAmt(CommonUtil.convertObjToStr(ClientUtil.convertObjToCurrency(getCboFromAmount())));
            loanTO.setToAmt(CommonUtil.convertObjToStr(ClientUtil.convertObjToCurrency(getCboToAmount())));
            loanTO.setSlabNoOfMembers(CommonUtil.convertObjToDouble(getTxtSlabNoOfMembers()));
            String key=loanTO.getFromAmt();
            key=key.replace(".00", "");
            key=key.replace(",", "");
            loanmap.put(key, loanTO);              
//            loanmap.put(CommonUtil.convertObjToStr(loanTO.getFromAmt()), loanTO);
            updateLoanAmtSlabWiseTable(rowSel, loanTO);
        } catch (Exception e) {
            parseException.logException(e, true);
        }
    }

    private void updateLoanAmtSlabWiseTable(int rowSel, NclAmtSlabWiseDetTO loanTO) throws Exception {
        Object selectedRow;
        boolean rowExists = false;
        //If row already exists update it, else create a new row & append        
        for (int i = tblAmtSlabWise.getRowCount(), j = 0; i > 0; i--, j++) {
            selectedRow = ((ArrayList) tblAmtSlabWise.getDataArrayList().get(j)).get(0);
            if (CommonUtil.convertObjToStr(loanTO.getFromAmt()).equals(CommonUtil.convertObjToStr(selectedRow))) {
                rowExists = true;
                ArrayList LoanRow = new ArrayList();
                ArrayList data = tblAmtSlabWise.getDataArrayList();
                data.remove(rowSel);
                LoanRow.add(loanTO.getFromAmt());
                LoanRow.add(loanTO.getToAmt());
                LoanRow.add(loanTO.getSlabNoOfMembers());
                tblAmtSlabWise.insertRow(rowSel, LoanRow);
                LoanRow = null;
            }
        }
        if (!rowExists) {
            ArrayList LoanRow = new ArrayList();
            LoanRow.add(loanTO.getFromAmt());
            LoanRow.add(loanTO.getToAmt());
            LoanRow.add(loanTO.getSlabNoOfMembers());
            tblAmtSlabWise.insertRow(tblAmtSlabWise.getRowCount(), LoanRow);
            LoanRow = null;
        }
    }

    /**
     * To perform the necessary operation
     */
    public void doAction() {
        try {
            if (actionType != ClientConstants.ACTIONTYPE_CANCEL || get_authorizeMap() != null) {
                convertProperDataForSublimit();
                convertProperDataForMemberDetails();
                convertProperDataForAmtSlabDetails();
                doActionPerform();
            }
        } catch (Exception e) {
            setResult(ClientConstants.ACTIONTYPE_FAILED);
            parseException.logException(e, true);
        }
    }

    /**
     * To perform the necessary action
     */
    private void doActionPerform() throws Exception {
        final HashMap data = new HashMap();
        data.put("COMMAND", getCommand());
        if (get_authorizeMap() == null) {
            data.put("NclSanctionDetails", setNclSanctionDetailsData());
            if (sublimitmap != null && sublimitmap.size() > 0) {
                data.put("SublimitDetails", sublimitmap);
            }
            if (membermap != null && membermap.size() > 0) {
                data.put("MemberDetails", membermap);
            }
            if (loanmap != null && loanmap.size() > 0) {
                data.put("LoanSlabDetails", loanmap);
            }
            if (getEndYear().length() > 0) {
                data.put("END_YEAR", getEndYear());
            }
        } else {
            data.put(CommonConstants.AUTHORIZEMAP, get_authorizeMap());
        }
        System.out.println("data in SanctionMaster OB : " + data);
        HashMap proxyResultMap = proxy.execute(data, operationMap);
        if(proxyResultMap.containsKey("NCL_SANCTION_NO"))
        SanctionNo = CommonUtil.convertObjToStr(proxyResultMap.get("NCL_SANCTION_NO"));        
        setProxyReturnMap(proxyResultMap);
        _authorizeMap = null;
        setResult(getActionType());
    }

    /**
     * To populate data into the screen
     */
    public NclSanctionDetailsTO setNclSanctionDetailsData() {

        final NclSanctionDetailsTO objSanctionDetailsrTO = new NclSanctionDetailsTO();
        try {
            objSanctionDetailsrTO.setCustId(CommonUtil.convertObjToStr(getTxtCustID()));
            objSanctionDetailsrTO.setNclSanctionNo(getLblSanctionNoVal());
            objSanctionDetailsrTO.setSanctionDt(DateUtil.getDateMMDDYYYY(getTdtSanctionDt()));
            objSanctionDetailsrTO.setExpiryDt(DateUtil.getDateMMDDYYYY(getTdtExpiryDt()));
            objSanctionDetailsrTO.setSanctionAmt(CommonUtil.convertObjToDouble(getTxtSanctionAmt()));
            objSanctionDetailsrTO.setRemarks(getTxtRemarks());
            objSanctionDetailsrTO.setCaprodtype(CommonUtil.convertObjToStr(getCbmCAProdType().getKeyForSelected()));
            objSanctionDetailsrTO.setCaProdId(CommonUtil.convertObjToStr(getCbmCAProdId().getKeyForSelected()));
            objSanctionDetailsrTO.setKccProdId(CommonUtil.convertObjToStr(getCbmKCCProdId().getKeyForSelected()));
            objSanctionDetailsrTO.setCaActNum(getTxtCAAccNo());
            objSanctionDetailsrTO.setKccActNum(getTxtKCCAccNo());
            objSanctionDetailsrTO.setStatus(getAction());
            objSanctionDetailsrTO.setStatusBy(TrueTransactMain.USER_ID);
            objSanctionDetailsrTO.setStatusDt(ClientUtil.getCurrentDateWithTime());
            objSanctionDetailsrTO.setBranchId(TrueTransactMain.BRANCH_ID);
        } catch (Exception e) {
            log.info("Error In setNclSanctionDetailsData()");
            e.printStackTrace();
        }
        return objSanctionDetailsrTO;
    }

    public void convertProperDataForSublimit(){
        ArrayList addList = new ArrayList(sublimitmap.keySet());
                for (int i = 0; i < addList.size(); i++) {
                    NclSubLimitTO sublimitTO = (NclSubLimitTO) sublimitmap.get(addList.get(i));
                    String LimitAmt=sublimitTO.getLimitAmt();
                    LimitAmt=LimitAmt.replace(",", "");
                    sublimitTO.setLimitAmt(LimitAmt);
                    sublimitmap.put(CommonUtil.convertObjToStr(sublimitTO.getStartFinYear()), sublimitTO);
                    
    }                            
    }
    
    public void convertProperDataForMemberDetails(){
        ArrayList addList = new ArrayList(membermap.keySet());
                for (int i = 0; i < addList.size(); i++) {
                    NclClassificationTO memberTO = (NclClassificationTO) membermap.get(addList.get(i));
                    String Amt=memberTO.getAmt();
                    Amt=Amt.replace(",", "");
                    memberTO.setAmt(Amt);
                    membermap.put(addList.get(i), memberTO);                    
    }                          
    }
    
     public void convertProperDataForAmtSlabDetails(){
        ArrayList addList = new ArrayList(loanmap.keySet());
                for (int i = 0; i < addList.size(); i++) {
                    NclAmtSlabWiseDetTO loanTO = (NclAmtSlabWiseDetTO) loanmap.get(addList.get(i));
                    String frmAmt=loanTO.getFromAmt();
                    String toAmt=loanTO.getToAmt();
                    frmAmt=frmAmt.replace(",", "");
                    toAmt=toAmt.replace(",", "");
                    loanTO.setFromAmt(frmAmt);
                    loanTO.setToAmt(toAmt);                    
                    loanmap.put(addList.get(i), loanTO);                    
    }                 
    }
         
     
    public void getData(HashMap whereMap) {
        try {
            final HashMap data = (HashMap) proxy.executeQuery(whereMap, operationMap);
            System.out.println("#@@%@@#%@#data" + data);
            if (data.containsKey("objSanctionDetailsrTO")) {
                NclSanctionDetailsTO SanctionTO = (NclSanctionDetailsTO) ((List) data.get("objSanctionDetailsrTO")).get(0);
                populateSanctionData(SanctionTO);
            }
            if (data.containsKey("SUB_LIMIT")) {
                sublimitmap = (LinkedHashMap) data.get("SUB_LIMIT");
                ArrayList addList = new ArrayList(sublimitmap.keySet());
                for (int i = 0; i < addList.size(); i++) {
                    NclSubLimitTO sublimitTO = (NclSubLimitTO) sublimitmap.get(addList.get(i));
                    ArrayList SubRow = new ArrayList();
                    //Changed By Suresh
//                    SubRow.add(sublimitTO.getStartFinYear());
//                    SubRow.add(sublimitTO.getEndFinYear());
                    SubRow.add(CommonUtil.convertObjToStr(sublimitTO.getStartFinYear())+"-"+CommonUtil.convertObjToStr(sublimitTO.getEndFinYear()));
//                    SubRow.add(sublimitTO.getLimitAmt());
                     SubRow.add(ClientUtil.convertObjToCurrency(sublimitTO.getLimitAmt()));
                    tblSubLimit.addRow(SubRow);
                }
            }
            if (data.containsKey("MEMBER_DATA")) {
                membermap = (LinkedHashMap) data.get("MEMBER_DATA");
                ArrayList addList = new ArrayList(membermap.keySet());
                for (int i = 0; i < addList.size(); i++) {
                    NclClassificationTO memberTO = (NclClassificationTO) membermap.get(addList.get(i));
                    ArrayList MemRow = new ArrayList();
                    MemRow.add(String.valueOf(i + 1));
                    MemRow.add(memberTO.getCategory());
                    MemRow.add(memberTO.getSubcategory());
                    MemRow.add(memberTO.getTotMembers());
                    MemRow.add(ClientUtil.convertObjToCurrency(memberTO.getAmt()));
                    tblMemberDetails.addRow(MemRow);
                }
            }
            if (data.containsKey("LOAN_DATA")) {
                loanmap = (LinkedHashMap) data.get("LOAN_DATA");
                ArrayList addList = new ArrayList(loanmap.keySet());
                for (int i = 0; i < addList.size(); i++) {
                    NclAmtSlabWiseDetTO loanTO = (NclAmtSlabWiseDetTO) loanmap.get(addList.get(i));
                    ArrayList LoanRow = new ArrayList();
                    LoanRow.add(ClientUtil.convertObjToCurrency(loanTO.getFromAmt()));
                    LoanRow.add(ClientUtil.convertObjToCurrency(loanTO.getToAmt()));
                    LoanRow.add(loanTO.getSlabNoOfMembers());
                    tblAmtSlabWise.addRow(LoanRow);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            parseException.logException(e, true);
        }
    }

    private void populateSanctionData(NclSanctionDetailsTO SanctionTO) throws Exception {
        this.setLblSanctionNoVal(CommonUtil.convertObjToStr(SanctionTO.getNclSanctionNo()));
        this.setTxtCustID(CommonUtil.convertObjToStr(SanctionTO.getCustId()));
        this.setTdtSanctionDt(CommonUtil.convertObjToStr(SanctionTO.getSanctionDt()));
        this.setTdtExpiryDt(CommonUtil.convertObjToStr(SanctionTO.getExpiryDt()));
        this.setTxtKCCAccNo(CommonUtil.convertObjToStr(SanctionTO.getKccActNum()));
        this.setTxtCAAccNo(CommonUtil.convertObjToStr(SanctionTO.getCaActNum()));
        this.setTxtRemarks(CommonUtil.convertObjToStr(SanctionTO.getRemarks()));
        setCbmCAProdId(CommonUtil.convertObjToStr(SanctionTO.getCaprodtype()));
        setCboCAProdId((String) getCbmCAProdId().getDataForKey(CommonUtil.convertObjToStr(SanctionTO.getCaProdId())));
        setCboKCCProdId((String) getCbmKCCProdId().getDataForKey(CommonUtil.convertObjToStr(SanctionTO.getKccProdId())));
        this.setTxtSanctionAmt(CommonUtil.convertObjToStr(SanctionTO.getSanctionAmt()));
        setCboCAProdType((String) getCbmCAProdType().getDataForKey(CommonUtil.convertObjToStr(SanctionTO.getCaprodtype())));
        setChanged();
        notifyObservers();
    }

    private void setRightAlignment(int col) {
        javax.swing.table.DefaultTableCellRenderer r = new javax.swing.table.DefaultTableCellRenderer();
        r.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);        
        _tblData.getColumnModel().getColumn(col).setCellRenderer(r);
        _tblData.getColumnModel().getColumn(col).sizeWidthToFit();       
    }
   
    private String getAction() {
        String action = null;
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
        return action;
    }

    private String getCommand() {
        String command = null;
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
        return command;
    }
}
