/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition.
 * 
 *
 * SHGOB.java
 *
 * Created on Sat Oct 15 11:56:39 IST 2011
 */
package com.see.truetransact.ui.transaction.multipleStanding;

import com.see.truetransact.ui.payroll.payMaster.*;
import java.util.Observable;
import java.util.List;
import java.util.HashMap;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import org.apache.log4j.Logger;
import com.see.truetransact.ui.TrueTransactMain;
import com.see.truetransact.clientutil.ClientUtil;
import com.see.truetransact.commonutil.CommonUtil;
import com.see.truetransact.clientproxy.ProxyFactory;
import com.see.truetransact.clientutil.ComboBoxModel;
import com.see.truetransact.clientutil.ClientConstants;
import com.see.truetransact.commonutil.CommonConstants;
import com.see.truetransact.clientproxy.ProxyParameters;
import com.see.truetransact.clientutil.EnhancedTableModel;
import com.see.truetransact.clientexception.ClientParseException;
import com.see.truetransact.commonutil.DateUtil;
import com.see.truetransact.transferobject.payroll.PayRollTo;
import com.see.truetransact.transferobject.payroll.earningsDeductions.EarnDeduPayTO;
import com.see.truetransact.transferobject.transaction.multipleStanding.MultiStandingMasterTO;
import com.see.truetransact.transferobject.transaction.multipleStanding.MultipleStandingActDetailsTO;
import com.see.truetransact.transferobject.transaction.multipleStanding.multipleStandingMasterTO;
import com.see.truetransact.ui.common.transaction.TransactionOB;
import com.see.truetransact.uicomponent.CTable;
import java.util.Date;

/**
 *
 * @sreeKrishanan
 */
public class MultipleStandingOB extends Observable {

    final String NO = "N";
    final String YES = "Y";
    private String txtMemberNo = "";
    private String txtGroupId = "";
    private String lblMemberNameVal = "";
    private String lblStreetVal = "";
    private String lblAreaVal = "";
    private String lblCityVal = "";
    private String lblStateVal = "";
    private String txtGroupName = "";
    private String cboProdId = "";
    private String txtAccountNo = "";
    private ComboBoxModel cbmProdId;
    private ComboBoxModel cbmPayCode;
    private HashMap lookUpHash;
    private ComboBoxModel cbmProdTypCr;
    private ComboBoxModel cbmProdIdCr;
    private ComboBoxModel cbmProdSIType; // nithya
    private boolean activeStaus = false;
    private boolean payTrans = false;
    private TransactionOB transactionOB;
    private LinkedHashMap transactionDetailsTO;
    private HashMap proxyReturnMap;
    private String custID = "";
    private String limitAmt = "";
    private String AcctNo = "";
    private String groupLoanName = "";
    private String custName = "";
    private String creditNo = "";
    private double totalAmount = 0.0;
    private String txtEmployeeId = "";
    private String PayCode = "";
    private String prodType = "";
    private String prodID = "";
    private String paycodeType = "";
    //for filling dropdown
    private HashMap lookupMap;
    private HashMap param;
    private HashMap keyValue;
    private ArrayList key;
    private ArrayList value;
    final ArrayList tableTitle = new ArrayList();
    private ArrayList IncVal = new ArrayList();
    private EnhancedTableModel tblSHGDetails;
    private boolean newData = false;
    private LinkedHashMap PayMasterMap;
    private LinkedHashMap PayRollMap;
    private LinkedHashMap deletedPayMasterMap;
    private HashMap _authorizeMap;
    private String lblStatus = ClientConstants.ACTION_STATUS[ClientConstants.ACTIONTYPE_CANCEL];
    private int result;
    private ProxyFactory proxy;
    private HashMap map;
    private final static Logger log = Logger.getLogger(MultipleStandingOB.class);
    private final static ClientParseException parseException = ClientParseException.getInstance();
    private int actionType;
    private multipleStandingMasterTO multipleTO = null;
    private String lblEmployeeName = "";
    private String getSelectedTab = "";
    private String transType = "";
    private Date currDt = null;
    private double pfBalance = 0.0;
    private String payModuleType = "";
    private String pfAcNo = "";
    private String contraTransType = "";
    private List finalList = null;
    private String glAccountHead = "";
    private double FinalTotalAmount = 0.0;
    private String standingId ="";
    private String particulars ="";
    
    // Added by nithya
    private String siDescription = "";
    private String siProdType = "";
    private String siProdId = "";
    private String siActNo = "";
    private String siPurpose = "";
    private String siMasterParticulars = "";
    private String chkIsActive = "";
    private String masterTransType = "";
    private String accountTransType = "";
    private String masterAccountNo = "";
    private String masterProdType = "";
    private String lblActHolderName = "";
    private String multiStandingId = "";

    public String getMultiStandingId() {
        return multiStandingId;
    }

    public void setMultiStandingId(String multiStandingId) {
        this.multiStandingId = multiStandingId;
    }

    public String getLblActHolderName() {
        return lblActHolderName;
    }

    public void setLblActHolderName(String lblActHolderName) {
        this.lblActHolderName = lblActHolderName;
    }
    
    public String getMasterProdType() {
        return masterProdType;
    }

    public void setMasterProdType(String masterProdType) {
        this.masterProdType = masterProdType;
    }
    

    public String getMasterAccountNo() {
        return masterAccountNo;
    }

    public void setMasterAccountNo(String masterAccountNo) {
        this.masterAccountNo = masterAccountNo;
    }   
    

    public String getAccountTransType() {
        return accountTransType;
    }

    public void setAccountTransType(String accountTransType) {
        this.accountTransType = accountTransType;
    }

    public String getMasterTransType() {
        return masterTransType;
    }

    public void setMasterTransType(String masterTransType) {
        this.masterTransType = masterTransType;
    }
   
    public String getChkIsActive() {
        return chkIsActive;
    }

    public void setChkIsActive(String chkIsActive) {
        this.chkIsActive = chkIsActive;
    }
    
    public String getSiMasterParticulars() {
        return siMasterParticulars;
    }

    public void setSiMasterParticulars(String siMasterParticulars) {
        this.siMasterParticulars = siMasterParticulars;
    }
    
    public String getSiProdId() {
        return siProdId;
    }

    public void setSiProdId(String siProdId) {
        this.siProdId = siProdId;
    }
    
    public String getSiActNo() {
        return siActNo;
    }

    public void setSiActNo(String siActNo) {
        this.siActNo = siActNo;
    }

    public String getSiDescription() {
        return siDescription;
    }

    public void setSiDescription(String siDescription) {
        this.siDescription = siDescription;
    }

    public String getSiProdType() {
        return siProdType;
    }

    public void setSiProdType(String siProdType) {
        this.siProdType = siProdType;
    }

    public String getSiPurpose() {
        return siPurpose;
    }

    public void setSiPurpose(String siPurpose) {
        this.siPurpose = siPurpose;
    }
    
    // Added by nithya end    

    public ComboBoxModel getCbmProdSIType() {
        return cbmProdSIType;
    }

    public void setCbmProdSIType(ComboBoxModel cbmProdSIType) {
        this.cbmProdSIType = cbmProdSIType;
    }

    public String getParticulars() {
        return particulars;
    }

    public void setParticulars(String particulars) {
        this.particulars = particulars;
    }
    
    public String getStandingId() {
        return standingId;
    }

    public void setStandingId(String standingId) {
        this.standingId = standingId;
    }
    

    public double getFinalTotalAmount() {
        return FinalTotalAmount;
    }

    public void setFinalTotalAmount(double FinalTotalAmount) {
        this.FinalTotalAmount = FinalTotalAmount;
    }

    public String getGlAccountHead() {
        return glAccountHead;
    }

    public void setGlAccountHead(String glAccountHead) {
        this.glAccountHead = glAccountHead;
    }

    public List getFinalList() {
        return finalList;
    }

    public void setFinalList(List finalList) {
        this.finalList = finalList;
    }

    public String getContraTransType() {
        return contraTransType;
    }

    public void setContraTransType(String contraTransType) {
        this.contraTransType = contraTransType;
    }

    public String getPfAcNo() {
        return pfAcNo;
    }

    public void setPfAcNo(String pfAcNo) {
        this.pfAcNo = pfAcNo;
    }
    
    public String getPayModuleType() {
        return payModuleType;
    }

    public void setPayModuleType(String payModuleType) {
        this.payModuleType = payModuleType;
    }

    public double getPfBalance() {
        return pfBalance;
    }

    public void setPfBalance(double pfBalance) {
        this.pfBalance = pfBalance;
    }

    public String getTransType() {
        return transType;
    }

    public void setTransType(String transType) {
        this.transType = transType;
    }

    public String getGetSelectedTab() {
        return getSelectedTab;
    }

    public void setGetSelectedTab(String getSelectedTab) {
        this.getSelectedTab = getSelectedTab;
    }

    public HashMap getProxyReturnMap() {
        return proxyReturnMap;
    }

    public void setProxyReturnMap(HashMap proxyReturnMap) {
        this.proxyReturnMap = proxyReturnMap;
    }

    public LinkedHashMap getAllowedTransactionDetailsTO() {
        return allowedTransactionDetailsTO;
    }

    public void setAllowedTransactionDetailsTO(LinkedHashMap allowedTransactionDetailsTO) {
        this.allowedTransactionDetailsTO = allowedTransactionDetailsTO;
    }

    public LinkedHashMap getTransactionDetailsTO() {
        return transactionDetailsTO;
    }

    public void setTransactionDetailsTO(LinkedHashMap transactionDetailsTO) {
        this.transactionDetailsTO = transactionDetailsTO;
    }

    public TransactionOB getTransactionOB() {
        return transactionOB;
    }

    public void setTransactionOB(TransactionOB transactionOB) {
        this.transactionOB = transactionOB;
    }
    private LinkedHashMap allowedTransactionDetailsTO;
    private final String NOT_DELETED_TRANS_TOs = "NOT_DELETED_TRANS_TOs";

    public boolean isPayTrans() {
        return payTrans;
    }

    public void setPayTrans(boolean payTrans) {
        this.payTrans = payTrans;
    }
    private boolean rdoGlAccountType = false;
    private String tdtFromDate = "";

    public int getRecoveryMonth() {
        return recoveryMonth;
    }

    public void setRecoveryMonth(int recoveryMonth) {
        this.recoveryMonth = recoveryMonth;
    }

    public String getTdtFromDate() {
        return tdtFromDate;
    }

    public void setTdtFromDate(String tdtFromDate) {
        this.tdtFromDate = tdtFromDate;
    }
    private int recoveryMonth = 0;

    public boolean isRdoGlAccountType() {
        return rdoGlAccountType;
    }

    public void setRdoGlAccountType(boolean rdoGlAccountType) {
        this.rdoGlAccountType = rdoGlAccountType;
    }

    public boolean isRdoOtherAccountType() {
        return rdoOtherAccountType;
    }

    public void setRdoOtherAccountType(boolean rdoOtherAccountType) {
        this.rdoOtherAccountType = rdoOtherAccountType;
    }
    private boolean rdoOtherAccountType = false;

    public boolean isActiveStaus() {
        return activeStaus;
    }

    public void setActiveStaus(boolean activeStaus) {
        this.activeStaus = activeStaus;
    }

    public ComboBoxModel getCbmProdIdCr() {
        return cbmProdIdCr;
    }

    public void setCbmProdIdCr(ComboBoxModel cbmProdIdCr) {
        this.cbmProdIdCr = cbmProdIdCr;
    }

    public ComboBoxModel getCbmProdTypCr() {
        return cbmProdTypCr;
    }

    public void setCbmProdTypCr(ComboBoxModel cbmProdTypCr) {
        this.cbmProdTypCr = cbmProdTypCr;
    }

    public ComboBoxModel getCbmPayCode() {
        return cbmPayCode;
    }

    public void setCbmPayCode(ComboBoxModel cbmPayCode) {
        this.cbmPayCode = cbmPayCode;
    }

    public String getAcctNo() {
        return AcctNo;
    }

    public void setAcctNo(String AcctNo) {
        this.AcctNo = AcctNo;
    }

    public String getPaycodeType() {
        return paycodeType;
    }

    public void setPaycodeType(String paycodeType) {
        this.paycodeType = paycodeType;
    }

    public String getProdID() {
        return prodID;
    }

    public void setProdID(String prodID) {
        this.prodID = prodID;
    }

    public String getProdType() {
        return prodType;
    }

    public void setProdType(String prodType) {
        this.prodType = prodType;
    }

    public String getPayCode() {
        return PayCode;
    }

    public void setPayCode(String PayCode) {
        this.PayCode = PayCode;
    }

    public String getPayDescription() {
        return PayDescription;
    }

    public void setPayDescription(String PayDescription) {
        this.PayDescription = PayDescription;
    }
    private String PayDescription = "";

    public String getTxtEmployeeId() {
        return txtEmployeeId;
    }

    public void setTxtEmployeeId(String txtEmployeeId) {
        this.txtEmployeeId = txtEmployeeId;
    }

    public String getLblEmployeeName() {
        return lblEmployeeName;
    }

    public void setLblEmployeeName(String lblEmployeeName) {
        this.lblEmployeeName = lblEmployeeName;
    }

    public double getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(double totalAmount) {
        this.totalAmount = totalAmount;
    }

    public String getCreditNo() {
        return creditNo;
    }

    public void setCreditNo(String creditNo) {
        this.creditNo = creditNo;
    }

    public String getCustName() {
        return custName;
    }

    public void setCustName(String custName) {
        this.custName = custName;
    }

    public String getGroupLoanName() {
        return groupLoanName;
    }

    public void setGroupLoanName(String groupLoanName) {
        this.groupLoanName = groupLoanName;
    }

    public String getLimitAmt() {
        return limitAmt;
    }

    public void setLimitAmt(String limitAmt) {
        this.limitAmt = limitAmt;
    }

    public String getCustID() {
        return custID;
    }

    public void setCustID(String custID) {
        this.custID = custID;
    }

    /** Creates a new instance of TDS MiantenanceOB */
    public MultipleStandingOB() {
        try {
            currDt = ClientUtil.getCurrentDate();
            proxy = ProxyFactory.createProxy();
            map = new HashMap();
            map.put(CommonConstants.JNDI, "MultipleStandingJNDI");
            map.put(CommonConstants.HOME, "MultipleStandingHome");
            map.put(CommonConstants.REMOTE, "MultipleStanding");
            setTableTile();
            tblSHGDetails = new EnhancedTableModel(null, tableTitle);
            fillDropdown();
            //fillDropdown("");
        } catch (Exception e) {
            parseException.logException(e, true);
        }
    }

    private void setTableTile() throws Exception {
        tableTitle.add("Product Type");
        tableTitle.add("Prod Id");
        tableTitle.add("Account No.");
        tableTitle.add("Amount");
        tableTitle.add("Status");
        tableTitle.add("Particulars");
        IncVal = new ArrayList();
    }

    public void setCbmProdIdCr(String prodType) {
        if (CommonUtil.convertObjToStr(prodType).length() > 1) {
            if (prodType.equals(CommonConstants.GL_TRANSMODE_TYPE)) {
                key = new ArrayList();
                value = new ArrayList();
            } else {
                try {
                    lookUpHash = new HashMap();
                    lookUpHash.put(CommonConstants.MAP_NAME, "Cash.getAccProduct" + prodType);
                    lookUpHash.put(CommonConstants.PARAMFORQUERY, null);
                    keyValue = ClientUtil.populateLookupData(lookUpHash);
                    fillData((HashMap) keyValue.get(CommonConstants.DATA));
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
        } else {
            key = new ArrayList();
            value = new ArrayList();
            key.add("");
            value.add("");
        }
        cbmProdIdCr = new ComboBoxModel(key, value);
        this.cbmProdIdCr = cbmProdIdCr;
        setChanged();
    }

    public void fillDropdown(String payType) throws Exception {
        try {
            ArrayList Paykey = new ArrayList();
            ArrayList Payvalue = new ArrayList();
            HashMap mapShare = new HashMap();
            List PaykeyValue = null;
            if (payType != null && payType.length() > 0) {
                mapShare.put("PAY_EARNDEDU", payType);
            }
            PaykeyValue = ClientUtil.executeQuery("getSelectPayCodes", mapShare);
            Paykey.add("");
            Payvalue.add("");
            if (PaykeyValue != null && PaykeyValue.size() > 0) {
                for (int i = 0; i < PaykeyValue.size(); i++) {
                    mapShare = (HashMap) PaykeyValue.get(i);
                    Paykey.add(mapShare.get("PAY_CODE"));
                    Payvalue.add(mapShare.get("PAY_DESCRI"));
                }
            }
            cbmPayCode = new ComboBoxModel(Paykey, Payvalue);
            Paykey = null;
            Payvalue = null;
            PaykeyValue.clear();
            PaykeyValue = null;
            mapShare.clear();
            mapShare = null;   //

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void fillDropdown() throws Exception {
        try {
            lookUpHash = new HashMap();
            lookUpHash.put(CommonConstants.MAP_NAME, null);
            final ArrayList lookup_keys = new ArrayList();
            lookup_keys.add("MULTIPLE_STANDING_PRODUCTTYPE");
            lookup_keys.add("PRODUCTTYPE");
            lookUpHash.put(CommonConstants.PARAMFORQUERY, lookup_keys);
            keyValue = ClientUtil.populateLookupData(lookUpHash);
            fillData((HashMap) keyValue.get("MULTIPLE_STANDING_PRODUCTTYPE"));
            cbmProdTypCr = new ComboBoxModel(key, value);    
            cbmProdTypCr.removeKeyAndElement("AB");    
            cbmProdTypCr.removeKeyAndElement("AD");
            cbmProdTypCr.removeKeyAndElement("TD");
            cbmProdTypCr.removeKeyAndElement("TL");
            key = new ArrayList();
            value = new ArrayList();
            value.add("");
            value.add("CREDIT");
            value.add("DEBIT");
            key.add("");
            key.add("CREDIT");
            key.add("DEBIT");            
            cbmPayCode = new ComboBoxModel(key, value);
            // Added by nithya for sicombobox
            fillData((HashMap) keyValue.get("PRODUCTTYPE"));
            cbmProdSIType = new ComboBoxModel(key,value);
            cbmProdSIType.removeKeyAndElement("AB");    
            cbmProdSIType.removeKeyAndElement("AD");
            cbmProdSIType.removeKeyAndElement("TD");
            cbmProdSIType.removeKeyAndElement("TL");    
            
        } catch (NullPointerException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void fillData(HashMap keyValue) throws Exception {
        key = (ArrayList) keyValue.get(CommonConstants.KEY);
        value = (ArrayList) keyValue.get(CommonConstants.VALUE);
    }

    /** To retrieve a particular customer's accountclosing record */
    public void getData(HashMap whereMap) {
        try {
            final HashMap data = (HashMap) proxy.executeQuery(whereMap, map);
            System.out.println("#@@%@@#%@#data" + data);
             if (data.containsKey("StandingMasterTO_DATA")) {                 
                  MultiStandingMasterTO objMultiStandingMasterTO = (MultiStandingMasterTO) data.get("StandingMasterTO_DATA");
                  System.out.println("objMultiStandingMasterTO :: " + objMultiStandingMasterTO); 
                  populateMultipleStandingMaster(objMultiStandingMasterTO);
             }
            if (data.containsKey("StandingMasterTO_ACT_DATA")) {
                PayMasterMap = (LinkedHashMap) data.get("StandingMasterTO_ACT_DATA");
                System.out.println("nithya PayMasterMap :: " + PayMasterMap);
                ArrayList addList = new ArrayList(PayMasterMap.keySet());
                for (int i = 0; i < addList.size(); i++) {
                    MultipleStandingActDetailsTO StandingMasterTO = (MultipleStandingActDetailsTO) PayMasterMap.get(addList.get(i));
                    StandingMasterTO.setStatus(CommonConstants.STATUS_MODIFIED);
                    ArrayList incTabRow = new ArrayList();
                    incTabRow.add(StandingMasterTO.getActProdType());
                    incTabRow.add(StandingMasterTO.getActProdId());
                    incTabRow.add(StandingMasterTO.getActAccNo());
                    incTabRow.add(StandingMasterTO.getTransAmount());
                    if(StandingMasterTO.getIsActive().equalsIgnoreCase("Y")){
                        incTabRow.add("Active");
                    }else{
                        incTabRow.add("Inactive");
                    } 
                    incTabRow.add(StandingMasterTO.getParticulars());
                    tblSHGDetails.addRow(incTabRow);
                    StandingMasterTO.setStatusDt((Date)currDt.clone());
                    StandingMasterTO.setStandingId(StandingMasterTO.getStandingId());
                    //insertPayroll(earnDeduPayTO);
                    setStandingId(StandingMasterTO.getStandingId());
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            parseException.logException(e, true);
        }
    }

    private void populateMultipleStandingMaster(MultiStandingMasterTO objMultiStandingMasterTO) throws Exception {
//        private String siDescription = "";
//        private String siProdType = "";
//        private String siProdId = "";
//        private String siActNo = "";
//        private String siPurpose = "";
//        private String siMasterParticulars = "";  
        try {
            setStandingId(objMultiStandingMasterTO.getStandingId());
            setSiProdType(objMultiStandingMasterTO.getMasterProdType());
            setSiProdId(objMultiStandingMasterTO.getMasterProdId());
            setSiActNo(objMultiStandingMasterTO.getMasterActNo());
            setSiDescription(objMultiStandingMasterTO.getSiDescription());
            setSiPurpose(objMultiStandingMasterTO.getParticulars());
            System.out.println("nithya inside populateMaster objMultiStandingMasterTO :: " + objMultiStandingMasterTO);
            notifyObservers();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    public void getRemittData(HashMap whereMap) {
        try {
            getTransDetails(whereMap);
        } catch (Exception e) {
            parseException.logException(e, true);
            e.printStackTrace();
        }
    }

    public boolean setPFBalance(String employeeId, String ayCode) {
        HashMap pfMap = new HashMap();
        boolean checkFlag = false;
        pfMap.put("EMPLOYEEID", employeeId);
        pfMap.put("PAY_CODE", ayCode);
        List lst = ClientUtil.executeQuery("getPFBalance", pfMap);
        if (lst != null && lst.size() > 0) {
            pfMap = (HashMap) lst.get(0);
            setPfBalance(CommonUtil.convertObjToDouble(pfMap.get("BALANCE")));     
            setPayModuleType(CommonUtil.convertObjToStr(pfMap.get("PAY_MODULE_TYPE")));  
            setPfAcNo(CommonUtil.convertObjToStr(pfMap.get("PF_ACT_NO")));  
            checkFlag = true;
        }
        return checkFlag;
    }

    public void getTransDetails(HashMap whereMap) {
        final HashMap mapData;
        try {
           // whereMap.put("REMITT_ISSUE_TRANS", "REMITT_ISSUE_TRANS");
            mapData = proxy.executeQuery(whereMap, map);
            System.out.println("@#$@#$@#$mapData:" + mapData);
            if (mapData.containsKey("TRANSACTION_LIST")) {
                List list = (List) mapData.get("TRANSACTION_LIST");
                if (!list.isEmpty()) {
                    transactionOB.setDetails(list);
                }
            }
        } catch (Exception e) {
            System.out.println("Error In populateData()");
            parseException.logException(e, true);
            e.printStackTrace();
        }
    }

    public void getCustName(String custId) {
        String custName = "";
        HashMap custMap = new HashMap();
        custMap.put("CUST_ID", custId);
        custMap.put("BRANCH_ID", ProxyParameters.BRANCH_ID);
        List lst = ClientUtil.executeQuery("getSelectCustomer", custMap);
        if (lst != null && lst.size() > 0) {
            custMap = (HashMap) lst.get(0);
            setCustName(CommonUtil.convertObjToStr(custMap.get("CUSTOMER")));
        }
    }

    public boolean checkPayCodeExistence(String payCode, String employee) {
        boolean flag = false;
        HashMap payMap = new HashMap();
        payMap.put("PAY_CODE", payCode);
        payMap.put("EMPLOYEEID", employee);
        List lst = ClientUtil.executeQuery("getSelectPaycodeCheck", payMap);
        if (lst != null && lst.size() > 0) {
            flag = true;
        }
        return flag;
    }

    public void getPayCodes(String PayCode) {
        HashMap custMap = new HashMap();
        custMap.put("PAY_DESCRI", PayCode);
        List lst = ClientUtil.executeQuery("getPayCodes", custMap);
        if (lst != null && lst.size() > 0) {
            custMap = (HashMap) lst.get(0);
            setPayCode(CommonUtil.convertObjToStr(custMap.get("PAY_CODE")));
        }
    }

    public void getPayDescription(String PayCode) {
        HashMap custMap = new HashMap();
        custMap.put("PAY_CODE", PayCode);
        List lst = ClientUtil.executeQuery("getPayDescription", custMap);
        if (lst != null && lst.size() > 0) {
            custMap = (HashMap) lst.get(0);
            setPayDescription(CommonUtil.convertObjToStr(custMap.get("PAY_DESCRI")));
        }
    }

    public void getEmployeeName(String epID) {
        String custName = "";
        HashMap custMap = new HashMap();
        custMap.put("EMPLOYEEID", epID);
        List lst = ClientUtil.executeQuery("getSelectEmployee", custMap);
        if (lst != null && lst.size() > 0) {
            custMap = (HashMap) lst.get(0);
            setLblEmployeeName(CommonUtil.convertObjToStr(custMap.get("EMPLOYEE_NAME")));
        }
    }

    public double setOdAmount(String actNum) {
        double amount = 0.0;
        HashMap odMap = new HashMap();
        odMap.put("ACT_NUM", actNum);
        odMap.put("BRANCH_ID", ProxyParameters.BRANCH_ID);
        List lst = ClientUtil.executeQuery("getOdAmount", odMap);
        if (lst != null && lst.size() > 0) {
            odMap = (HashMap) lst.get(0);
            amount = CommonUtil.convertObjToDouble(odMap.get("AVAILABLE_BALANCE"));
        }
        return amount;
    }

    public boolean isExist(String ccNum) {
        boolean ccFlag = false;
        ccNum = ccNum.toUpperCase();
        HashMap odMap = new HashMap();
        odMap.put("CC_NUM", ccNum);
        List lst = ClientUtil.executeQuery("getCCNoPresent", odMap);
        if (lst != null && lst.size() > 0) {
            return true;
        } else {
            return false;
        }
    }

    public String getPaycodeType(String Paycode) {
        HashMap odMap = new HashMap();
        String PaycodeType = "";
        odMap.put("PAY_CODE", Paycode);
        List lst = ClientUtil.executeQuery("getPayCodeType", odMap);
        if (lst != null && lst.size() > 0) {
            odMap = (HashMap) lst.get(0);
            PaycodeType = CommonUtil.convertObjToStr(odMap.get("PAY_EARNDEDU"));
        }
        return PaycodeType;
    }

    public String getPayModuleType(String Paycode) {
        HashMap odMap = new HashMap();
        String PaycodeType = "";
        odMap.put("PAY_CODE", Paycode);
        List lst = ClientUtil.executeQuery("getPayModuleType", odMap);
        if (lst != null && lst.size() > 0) {
            odMap = (HashMap) lst.get(0);
            PaycodeType = CommonUtil.convertObjToStr(odMap.get("PAY_MODULE_TYPE"));
        }
        return PaycodeType;
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
        return command;
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

    public void addToTable(int rowSelected, boolean updateMode) {
        try {
            int rowSel = rowSelected;
            final MultipleStandingActDetailsTO multipleStandingTO = new MultipleStandingActDetailsTO();
            if (PayMasterMap == null) {
                PayMasterMap = new LinkedHashMap();
            }
            if (getActionType() == ClientConstants.ACTIONTYPE_EDIT) {
                if (isNewData()) {
                    multipleStandingTO.setStatusDt((Date)currDt.clone());
                    multipleStandingTO.setStatusBy(TrueTransactMain.USER_ID);
                    multipleStandingTO.setStatus(CommonConstants.STATUS_CREATED);
                } else {
                    multipleStandingTO.setStatusDt((Date)currDt.clone());
                    multipleStandingTO.setStatusBy(TrueTransactMain.USER_ID);
                    multipleStandingTO.setStatus(CommonConstants.STATUS_MODIFIED);
                    multipleStandingTO.setStandingId(getStandingId());
                }
            } else {
                multipleStandingTO.setStatusDt((Date)currDt.clone());
                multipleStandingTO.setStatusBy(TrueTransactMain.USER_ID);
                multipleStandingTO.setStatus(CommonConstants.STATUS_CREATED);
            }

            if (isNewData()) {
                ArrayList data = tblSHGDetails.getDataArrayList();
            }
            //multipleStandingTO.setAccountHead(getTxtEmployeeId());
            if(getPayCode().equalsIgnoreCase("DEBIT")){
                multipleStandingTO.setTransType("CREDIT");
            }else{
                multipleStandingTO.setTransType("DEBIT");
            }            
            multipleStandingTO.setTransAmount(getTotalAmount());
            multipleStandingTO.setParticulars(getParticulars());
            if (getProdType().equals(CommonConstants.GL_TRANSMODE_TYPE)) {
                multipleStandingTO.setActProdType(CommonConstants.GL_TRANSMODE_TYPE);
                multipleStandingTO.setActAccNo(getAcctNo());
            } else if (!getProdType().equals(CommonConstants.GL_TRANSMODE_TYPE)) {
                multipleStandingTO.setActProdType(getProdType());
                multipleStandingTO.setActProdId(getProdID());
                multipleStandingTO.setActAccNo(getAcctNo());
            }
            if(getChkIsActive().equalsIgnoreCase("Y")){
                multipleStandingTO.setIsActive("Y");
            }else{
                multipleStandingTO.setIsActive("N");
            }    
            if(getActionType() == ClientConstants.ACTIONTYPE_EDIT){
               multipleStandingTO.setStandingId(getStandingId());  
            }
            PayMasterMap.put(multipleStandingTO.getActAccNo(), multipleStandingTO);
            updateScheduleDetails(rowSel, multipleStandingTO);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void setPayCodeTransction() throws Exception {
        final EarnDeduPayTO earnDeduPayTO = new EarnDeduPayTO();
        if (PayMasterMap == null) {
            PayMasterMap = new LinkedHashMap();
        }
        earnDeduPayTO.setStatusDate(currDt);
        earnDeduPayTO.setStatusBy(TrueTransactMain.USER_ID);
        earnDeduPayTO.setStatus(CommonConstants.STATUS_CREATED);
        earnDeduPayTO.setEmployeeId(getTxtEmployeeId());
        earnDeduPayTO.setPayCode(getPayCode());
        earnDeduPayTO.setAmount(getTotalAmount());
        if (getProdType().equals(CommonConstants.GL_TRANSMODE_TYPE)) {
            earnDeduPayTO.setProdType(CommonConstants.GL_TRANSMODE_TYPE);
        } else if (!getProdType().equals(CommonConstants.GL_TRANSMODE_TYPE)) {
            earnDeduPayTO.setProdType(getProdType());
            earnDeduPayTO.setProdId(getProdID());
            earnDeduPayTO.setAccNo(getAcctNo());
        }
        earnDeduPayTO.setCreatedBy(TrueTransactMain.USER_ID);
        earnDeduPayTO.setCreatedDate(currDt);
        PayMasterMap.put(earnDeduPayTO.getPayCode(), earnDeduPayTO);
        insertPayroll(earnDeduPayTO);
    }

    private void insertPayroll(EarnDeduPayTO earnDeduPayTO) throws Exception {
        final PayRollTo payRollTo = new PayRollTo();
        PayRollMap = new LinkedHashMap();
        payRollTo.setEmployeeId(earnDeduPayTO.getEmployeeId());
        payRollTo.setMonth(earnDeduPayTO.getCreatedDate());
        payRollTo.setPayType(CommonUtil.convertObjToStr(getPaycodeType(CommonUtil.convertObjToStr(earnDeduPayTO.getPayCode()))));
        payRollTo.setTransDt(earnDeduPayTO.getCreatedDate());
        payRollTo.setPayCode(earnDeduPayTO.getPayCode());
        payRollTo.setPayDesc(getPayDescription());
        payRollTo.setAmount(earnDeduPayTO.getAmount());
        if (earnDeduPayTO.getProdType().equals(CommonConstants.GL_TRANSMODE_TYPE)) {
            payRollTo.setProdType(CommonConstants.GL_TRANSMODE_TYPE);
        } else if (!earnDeduPayTO.getProdType().equals(CommonConstants.GL_TRANSMODE_TYPE)) {
            payRollTo.setProdType(earnDeduPayTO.getProdType());
            payRollTo.setProdId(earnDeduPayTO.getProdId());
            payRollTo.setAcct_num(earnDeduPayTO.getAccNo());
        }
        payRollTo.setSrlNo(CommonUtil.convertObjToInt("1"));
        payRollTo.setStatus("posted");
        payRollTo.setCreatedBy(TrueTransactMain.USER_ID);
        payRollTo.setCreatedDt(earnDeduPayTO.getCreatedDate());
        payRollTo.setStatusBy(TrueTransactMain.USER_ID);
        payRollTo.setAuthorizeBy(TrueTransactMain.USER_ID);
        payRollTo.setAuthorizeStatus(YES);
        payRollTo.setTransType(getTransType());
        PayRollMap.put("PayRollTo", payRollTo);

    }

    private void updateScheduleDetails(int rowSel, MultipleStandingActDetailsTO multipleStandingTO) throws Exception {
        Object selectedRow;
        boolean rowExists = false;
        for (int i = tblSHGDetails.getRowCount(), j = 0; i > 0; i--, j++) {
            selectedRow = ((ArrayList) tblSHGDetails.getDataArrayList().get(j)).get(2);
            if (multipleStandingTO.getActAccNo().equals(CommonUtil.convertObjToStr(selectedRow))) {
                rowExists = true;
                ArrayList IncParRow = new ArrayList();
                ArrayList data = tblSHGDetails.getDataArrayList();
                data.remove(rowSel);
                IncParRow.add(getProdType());
                IncParRow.add(getProdID());
                IncParRow.add(getAcctNo());
                IncParRow.add(getTotalAmount());
                if(getChkIsActive().equalsIgnoreCase("Y")){
                    IncParRow.add("Active");
                }else{
                    IncParRow.add("Inactive"); 
                }  
                IncParRow.add(getParticulars());
                tblSHGDetails.insertRow(rowSel, IncParRow);
                IncParRow = null;
            }
        }
        if (!rowExists) {
            ArrayList IncParRow = new ArrayList();
            IncParRow.add(getProdType());
            IncParRow.add(getProdID());
            IncParRow.add(getAcctNo());
            IncParRow.add(getTotalAmount());
            if (getChkIsActive().equalsIgnoreCase("Y")) {
                IncParRow.add("Active");
            } else {
                IncParRow.add("Inactive");
            }  
            IncParRow.add(getParticulars());
            tblSHGDetails.insertRow(tblSHGDetails.getRowCount(), IncParRow);
            IncParRow = null;
        }
    }

    public void populateSHGDetails(String row) {
        try {
            resetSHGDetails();
            System.out.println("@#%#@%#@%"+PayMasterMap);
            final MultipleStandingActDetailsTO multipleStandingTO = (MultipleStandingActDetailsTO) PayMasterMap.get(row);
            System.out.println("@#%#@%#@%"+multipleStandingTO);
            populateTableData(multipleStandingTO);

        } catch (Exception e) {
            parseException.logException(e, true);
        }
    }

    public void deleteTableData(String val, int row) {
       
        if (deletedPayMasterMap == null) {
            deletedPayMasterMap = new LinkedHashMap();
        }
        MultipleStandingActDetailsTO objMultipleStandingActDetailsTO = (MultipleStandingActDetailsTO) PayMasterMap.get(val);
        objMultipleStandingActDetailsTO.setStatus(CommonConstants.STATUS_DELETED);
        deletedPayMasterMap.put(CommonUtil.convertObjToStr(tblSHGDetails.getValueAt(row, 3)), PayMasterMap.get(val));
        Object obj;
        obj = val;
        System.out.println("nithya testing deletedPayMasterMap :: " + deletedPayMasterMap);
        PayMasterMap.remove(val);
        //resetTableValues(); 
        removeDeletedAccounts(val);
        try {
            populateTable();
        } catch (Exception e) {
            parseException.logException(e, true);
        }
    }
    
    private void removeDeletedAccounts(String val){
        for(int i=0; i<tblSHGDetails.getRowCount(); i++){
            if(val.equalsIgnoreCase(CommonUtil.convertObjToStr(tblSHGDetails.getValueAt(i,2)))){
                tblSHGDetails.removeRow(i);
            }
        }
    }

    private void populateTable() throws Exception {
        ArrayList incDataList = new ArrayList();
        incDataList = new ArrayList(PayMasterMap.keySet());
        ArrayList addList = new ArrayList(PayMasterMap.keySet());
        int length = incDataList.size();
        for (int i = 0; i < length; i++) {
            ArrayList incTabRow = new ArrayList();
            EarnDeduPayTO earnDeduPayTO = (EarnDeduPayTO) PayMasterMap.get(addList.get(i));
            IncVal.add(earnDeduPayTO);
            if (!earnDeduPayTO.getStatus().equals("DELETED")) {
                incTabRow.add(earnDeduPayTO.getPayCode());
                getPayDescription(earnDeduPayTO.getPayCode());
                incTabRow.add(getPayDescription());
                incTabRow.add(getPaycodeType(earnDeduPayTO.getPayCode()));
                incTabRow.add(earnDeduPayTO.getAmount());
                tblSHGDetails.addRow(incTabRow);
            }
        }
        notifyObservers();
    }

    private void populateTableData(MultipleStandingActDetailsTO StandingTO) throws Exception {
        try {
        
        setProdType(CommonUtil.convertObjToStr(StandingTO.getActProdType()));
        setProdID(CommonUtil.convertObjToStr(StandingTO.getActProdId()));
        setAcctNo(CommonUtil.convertObjToStr(StandingTO.getActAccNo()));
        populateActHolderName(StandingTO.getActAccNo(),StandingTO.getActProdId(),StandingTO.getActProdType());
        setParticulars(CommonUtil.convertObjToStr(StandingTO.getParticulars()));
        setChkIsActive(CommonUtil.convertObjToStr(StandingTO.getIsActive()));
        setTotalAmount(CommonUtil.convertObjToDouble(StandingTO.getTransAmount()));
        setParticulars(CommonUtil.convertObjToStr(StandingTO.getParticulars()));
        // nithya************
        //setPayDescription(CommonUtil.convertObjToStr(StandingTO.getTransType()));
            //setChanged();
            notifyObservers();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /** To perform the necessary operation */
    public void doAction() {
        try {
            if (actionType != ClientConstants.ACTIONTYPE_CANCEL || get_authorizeMap() != null) {
                doActionPerform();
            }
        } catch (Exception e) {
            setResult(ClientConstants.ACTIONTYPE_FAILED);
            parseException.logException(e, true);
        }
    }

    /** To perform the necessary action */
    private void doActionPerform() throws Exception {
        System.out.println("PayMasterMap :: nithya :: " + PayMasterMap);
        final HashMap data = new HashMap();
        try {
            if (getGetSelectedTab().equals("TRANSACTION_DATA")) {
                setPayCodeTransction();
                data.put("TRANS_TYPE", getTransType());
                data.put("ACCOUNT_HEAD_ID", getGlAccountHead());
                data.put("TOTAL_AMOUNT", getFinalTotalAmount());
                data.put("STANDING_TABLE_DATA",getFinalList());
                data.put("MULTI_STANDING_ID",getMultiStandingId());// Added by nithya on 04-06-2018 for 0011637: Multiple Standing- Need to update the LinkBatch ID as Multiple standing ID for identify the Data
            }
            data.put("TAB_DATA", getGetSelectedTab());
            data.put("COMMAND", getCommand());
            if (get_authorizeMap() == null) {
                data.put("StandingMasterDetails", PayMasterMap);
                if (deletedPayMasterMap != null && deletedPayMasterMap.size() > 0) {
                    data.put("DeleteStandingMasterDetails", deletedPayMasterMap);
                }
                MultiStandingMasterTO objMultiStandingMasterTO = new MultiStandingMasterTO();
                objMultiStandingMasterTO = getSIMasterDetails();
                data.put("SI_MASTER_DETAILS",objMultiStandingMasterTO);
            } else {
                data.put(CommonConstants.AUTHORIZEMAP, get_authorizeMap());
                //if (PayRollMap != null && PayRollMap.size() > 0) {
                  //  data.put("PayRollMap", PayRollMap);                    
                //}
            }
            data.put(CommonConstants.MODULE, "Multiple Standing");
            data.put(CommonConstants.SCREEN, "Multiple Standing");
            data.put(CommonConstants.INITIATED_BRANCH, ProxyParameters.BRANCH_ID);
            data.put("BRANCH_ID", ProxyParameters.BRANCH_ID);
            data.put("MODE", CommonConstants.TOSTATUS_INSERT); 
            data.put(CommonConstants.USER_ID, TrueTransactMain.USER_ID);
            System.out.println("%#%#%#%#%data%#%#%#%%" + data);
            HashMap proxyResultMap = proxy.execute(data, map);
            setProxyReturnMap(proxyResultMap);
            _authorizeMap = null;
            setResult(getActionType());
        } catch (Exception e) {
            setResult(ClientConstants.ACTIONTYPE_FAILED);
            ClientUtil.showMessageWindow(e.getMessage());
        }

    }

    protected void getCustomerAddressDetails(String value) {
        HashMap custAddressMap = new HashMap();
        custAddressMap.put("CUST_ID", value);
        custAddressMap.put("BRANCH_ID", ProxyParameters.BRANCH_ID);
        List lst = ClientUtil.executeQuery("getSelectAccInfoDisplay", custAddressMap);
        if (lst != null && lst.size() > 0) {
            custAddressMap = (HashMap) lst.get(0);
            setLblStreetVal(CommonUtil.convertObjToStr(custAddressMap.get("STREET")));
            setLblAreaVal(CommonUtil.convertObjToStr(custAddressMap.get("AREA")));
            setLblCityVal(CommonUtil.convertObjToStr(custAddressMap.get("CITY1")));
            setLblStateVal(CommonUtil.convertObjToStr(custAddressMap.get("STATE1")));
        }
    }

    public static boolean validateCreditNo(com.see.truetransact.uicomponent.CTextField txtCCNO) {
        boolean valid = false;
        int alpha = 0;
        int numeric = 0;
        int other = 0;
        if (txtCCNO.getText() != null && txtCCNO.getText() != "") {
            txtCCNO.setText(txtCCNO.getText().toUpperCase());
            String ccNum = txtCCNO.getText();
            valid = true;
            if (ccNum.length() < 8) {
                valid = false;
            }
            if (valid) {
                for (int i = 0, j = ccNum.length(); i < j; i++) {
                    if ((int) ccNum.charAt(i) > 64 && (int) ccNum.charAt(i) < 91) {
                        alpha += 1;
                    } else if ((int) ccNum.charAt(i) > 47 && (int) ccNum.charAt(i) < 58) {
                        numeric += 1;
                    } else {
                        other += 1;
                    }
                }
                if (alpha > 0 && numeric > 0) {
                    valid = true;
                } else {
                    valid = false;
                }
                if (other > 0) {
                    valid = false;
                }
            }
            if (!valid) {
                txtCCNO.setText("");
            }
        }
        return valid;
    }

    public void resetForm() {
        PayMasterMap = null;
        deletedPayMasterMap = null;
        PayRollMap = null;
        resetTableValues();
        setTxtGroupId("");
        setTxtGroupName("");
        setCboProdId("");
        setTxtAccountNo("");
        resetSHGDetails();
        setTransType("");
        setGlAccountHead("");
        setChanged();
        setContraTransType("");
        setSiDescription("");
        setSiProdType("");
        setSiProdId("");
        setSiActNo("");
        setSiPurpose("");
        setSiMasterParticulars("");
        setChkIsActive("");  
        setMasterTransType("");
        setAccountTransType("");
        setMasterAccountNo("");
        setMasterProdType("");
        setLblActHolderName("");
        setMultiStandingId("");// Added by nithya on 04-06-2018 for 0011637: Multiple Standing- Need to update the LinkBatch ID as Multiple standing ID for identify the Data
    }

    public void resetSHGDetails() {
        setTotalAmount(0);
        setAcctNo("");
        setProdID("");
        setProdType("");
        setRdoGlAccountType(false);
        setRdoOtherAccountType(false);
        setActiveStaus(false);
        setRecoveryMonth(0);
        setTdtFromDate("");
        setPfBalance(0.0);
        setGlAccountHead("");
        setFinalTotalAmount(0);
        setParticulars("");
        setLblActHolderName("");
        setMultiStandingId("");// Added by nithya on 04-06-2018 for 0011637: Multiple Standing- Need to update the LinkBatch ID as Multiple standing ID for identify the Data
        //setSiDescription("");
        //setSiProdType("");
        //setSiProdId("");
       // setSiActNo("");
        //setSiPurpose("");
       // setSiMasterParticulars("");
       // setChkIsActive("");     
    }

    public void resetTableValues() {
        tblSHGDetails.setDataArrayList(null, tableTitle);
    }

    // Setter method for txtMemberNo
    void setTxtMemberNo(String txtMemberNo) {
        this.txtMemberNo = txtMemberNo;
        setChanged();
    }
    // Getter method for txtMemberNo

    String getTxtMemberNo() {
        return this.txtMemberNo;
    }

    // Setter method for txtGroupId
    void setTxtGroupId(String txtGroupId) {
        this.txtGroupId = txtGroupId;
        setChanged();
    }
    // Getter method for txtGroupId

    String getTxtGroupId() {
        return this.txtGroupId;
    }

    /**
     * Getter for property tblSHGDetails.
     * @return Value of property tblSHGDetails.
     */
    public com.see.truetransact.clientutil.EnhancedTableModel getTblSHGDetails() {
        return tblSHGDetails;
    }

    /**
     * Setter for property tblSHGDetails.
     * @param tblSHGDetails New value of property tblSHGDetails.
     */
    public void setTblSHGDetails(com.see.truetransact.clientutil.EnhancedTableModel tblSHGDetails) {
        this.tblSHGDetails = tblSHGDetails;
    }

    /**
     * Getter for property newData.
     * @return Value of property newData.
     */
    public boolean isNewData() {
        return newData;
    }

    /**
     * Setter for property newData.
     * @param newData New value of property newData.
     */
    public void setNewData(boolean newData) {
        this.newData = newData;
    }

    /**
     * Getter for property _authorizeMap.
     * @return Value of property _authorizeMap.
     */
    public java.util.HashMap get_authorizeMap() {
        return _authorizeMap;
    }

    /**
     * Setter for property _authorizeMap.
     * @param _authorizeMap New value of property _authorizeMap.
     */
    public void set_authorizeMap(java.util.HashMap _authorizeMap) {
        this._authorizeMap = _authorizeMap;
    }

    /**
     * Getter for property lblStatus.
     * @return Value of property lblStatus.
     */
    public java.lang.String getLblStatus() {
        return lblStatus;
    }

    /**
     * Setter for property lblStatus.
     * @param lblStatus New value of property lblStatus.
     */
    public void setLblStatus(java.lang.String lblStatus) {
        this.lblStatus = lblStatus;
    }

    /**
     * Getter for property result.
     * @return Value of property result.
     */
    public int getResult() {
        return result;
    }

    /**
     * Setter for property result.
     * @param result New value of property result.
     */
    public void setResult(int result) {
        this.result = result;
    }

    /**
     * Getter for property actionType.
     * @return Value of property actionType.
     */
    public int getActionType() {
        return actionType;
    }

    /**
     * Setter for property actionType.
     * @param actionType New value of property actionType.
     */
    public void setActionType(int actionType) {
        this.actionType = actionType;
    }

    /**
     * Getter for property lblMemberNameVal.
     * @return Value of property lblMemberNameVal.
     */
    public java.lang.String getLblMemberNameVal() {
        return lblMemberNameVal;
    }

    /**
     * Setter for property lblMemberNameVal.
     * @param lblMemberNameVal New value of property lblMemberNameVal.
     */
    public void setLblMemberNameVal(java.lang.String lblMemberNameVal) {
        this.lblMemberNameVal = lblMemberNameVal;
    }

    /**
     * Getter for property lblStreetVal.
     * @return Value of property lblStreetVal.
     */
    public java.lang.String getLblStreetVal() {
        return lblStreetVal;
    }

    /**
     * Setter for property lblStreetVal.
     * @param lblStreetVal New value of property lblStreetVal.
     */
    public void setLblStreetVal(java.lang.String lblStreetVal) {
        this.lblStreetVal = lblStreetVal;
    }

    /**
     * Getter for property lblAreaVal.
     * @return Value of property lblAreaVal.
     */
    public java.lang.String getLblAreaVal() {
        return lblAreaVal;
    }

    /**
     * Setter for property lblAreaVal.
     * @param lblAreaVal New value of property lblAreaVal.
     */
    public void setLblAreaVal(java.lang.String lblAreaVal) {
        this.lblAreaVal = lblAreaVal;
    }

    /**
     * Getter for property lblCityVal.
     * @return Value of property lblCityVal.
     */
    public java.lang.String getLblCityVal() {
        return lblCityVal;
    }

    /**
     * Setter for property lblCityVal.
     * @param lblCityVal New value of property lblCityVal.
     */
    public void setLblCityVal(java.lang.String lblCityVal) {
        this.lblCityVal = lblCityVal;
    }

    /**
     * Getter for property lblStateVal.
     * @return Value of property lblStateVal.
     */
    public java.lang.String getLblStateVal() {
        return lblStateVal;
    }

    /**
     * Setter for property lblStateVal.
     * @param lblStateVal New value of property lblStateVal.
     */
    public void setLblStateVal(java.lang.String lblStateVal) {
        this.lblStateVal = lblStateVal;
    }

    /** To set the status based on ActionType, either New, Edit, etc., */
    public void setStatus() {
        this.setLblStatus(ClientConstants.ACTION_STATUS[this.getActionType()]);
    }

    /** To update the Status based on result performed by doAction() method */
    public void setResultStatus() {
        this.setLblStatus(ClientConstants.RESULT_STATUS[this.getResult()]);
    }

    /**
     * Getter for property txtGroupName.
     * @return Value of property txtGroupName.
     */
    public java.lang.String getTxtGroupName() {
        return txtGroupName;
    }

    /**
     * Setter for property txtGroupName.
     * @param txtGroupName New value of property txtGroupName.
     */
    public void setTxtGroupName(java.lang.String txtGroupName) {
        this.txtGroupName = txtGroupName;
    }

    /**
     * Getter for property cboProdId.
     * @return Value of property cboProdId.
     */
    public java.lang.String getCboProdId() {
        return cboProdId;
    }

    /**
     * Setter for property cboProdId.
     * @param cboProdId New value of property cboProdId.
     */
    public void setCboProdId(java.lang.String cboProdId) {
        this.cboProdId = cboProdId;
    }

    /**
     * Getter for property txtAccountNo.
     * @return Value of property txtAccountNo.
     */
    public java.lang.String getTxtAccountNo() {
        return txtAccountNo;
    }

    /**
     * Setter for property txtAccountNo.
     * @param txtAccountNo New value of property txtAccountNo.
     */
    public void setTxtAccountNo(java.lang.String txtAccountNo) {
        this.txtAccountNo = txtAccountNo;
    }

    /**
     * Getter for property cbmProdId.
     * @return Value of property cbmProdId.
     */
    public com.see.truetransact.clientutil.ComboBoxModel getCbmProdId() {
        return cbmProdId;
    }

    /**
     * Setter for property cbmProdId.
     * @param cbmProdId New value of property cbmProdId.
     */
    public void setCbmProdId(com.see.truetransact.clientutil.ComboBoxModel cbmProdId) {
        this.cbmProdId = cbmProdId;
    }
    
    private MultiStandingMasterTO getSIMasterDetails(){        
        MultiStandingMasterTO objMultiStandingMasterTO = new MultiStandingMasterTO();
//        private String standingId = "";
//    private String accountHead = "";
//    private String transType = "";
//    private String masterProdType = "";
//    private String masterProdId = "";
//    private String masterActNo = "";
//    private Date masterLastTransDt = null;
//    private String status = "";
//    private String statusBy = "";
//    private Date statusDt = null;
//    private String authorizeBy = "";
//    private String authorizedStatus = "";
//    private Date authorizeDt = null;
//    private String particulars = "";
//    private String branchId = "";
        objMultiStandingMasterTO.setStandingId(getStandingId());
        objMultiStandingMasterTO.setAccountHead(getTxtEmployeeId());
        objMultiStandingMasterTO.setTransType(getPayCode());
        objMultiStandingMasterTO.setMasterProdType(getSiProdType());
        objMultiStandingMasterTO.setMasterProdId(getSiProdId());
        objMultiStandingMasterTO.setMasterActNo(getSiActNo()); 
        objMultiStandingMasterTO.setParticulars(getSiPurpose());
        objMultiStandingMasterTO.setStatusDt(currDt);
        objMultiStandingMasterTO.setStatusBy(TrueTransactMain.USER_ID);
        objMultiStandingMasterTO.setStatus(CommonConstants.STATUS_CREATED);
        objMultiStandingMasterTO.setSiDescription(getSiDescription()); 
        return objMultiStandingMasterTO;
    } 
    
    private void populateActHolderName(String ACCOUNTNO,String prodId,String prodType){
        setLblActHolderName(ACCOUNTNO);
        HashMap viewMap = new HashMap();
        if (!prodType.equals("GL")) {
            viewMap.put(CommonConstants.MAP_NAME, "Cash.getAccountList" + prodType);
        } else {
            viewMap.put(CommonConstants.MAP_NAME, "Cash.getSelectAcctHead");
        }
        HashMap whereMap = new HashMap();
        whereMap.put("PROD_ID", prodId);
        whereMap.put("SELECTED_BRANCH", ACCOUNTNO.substring(0, 4));
        viewMap.put(CommonConstants.MAP_WHERE, whereMap);
        whereMap.put("ACT_NUM", ACCOUNTNO); // Added by nithya on 24-09-2020 for KD-2276
        whereMap.put("AccountNo",ACCOUNTNO);
        whereMap.put("BRANCH_CODE", ProxyParameters.BRANCH_ID);
        whereMap.put(CommonConstants.DB_DRIVER_NAME, ProxyParameters.dbDriverName);
        whereMap.put("FILTERED_LIST", "FILTERED_LIST" + "_" + ProxyParameters.dbDriverName);
        List list = ClientUtil.executeQuery(CommonUtil.convertObjToStr(viewMap.get(CommonConstants.MAP_NAME)), whereMap);
        if (list != null && list.size() > 0) {
            HashMap custNAmeMap = (HashMap) list.get(0);
            System.out.println("custNAmeMap :: " + custNAmeMap);
            if (prodType.equalsIgnoreCase("GL")) {
                if (custNAmeMap.containsKey("A/C HEAD DESCRIPTION") && custNAmeMap.get("A/C HEAD DESCRIPTION") != null && !custNAmeMap.get("A/C HEAD DESCRIPTION").equals("") && CommonUtil.convertObjToStr(custNAmeMap.get("A/C HEAD DESCRIPTION")).length() > 0) {
                    setLblActHolderName(CommonUtil.convertObjToStr(custNAmeMap.get("A/C HEAD DESCRIPTION")));
                }
            } else {
                if (custNAmeMap.containsKey("CustomerName") && custNAmeMap.get("CustomerName") != null && !custNAmeMap.get("CustomerName").equals("") && CommonUtil.convertObjToStr(custNAmeMap.get("CustomerName")).length() > 0) {
                    setLblActHolderName(CommonUtil.convertObjToStr(custNAmeMap.get("CustomerName")));
                }
            }
        }
    }
}