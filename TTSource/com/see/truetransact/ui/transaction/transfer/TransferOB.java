    /*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition.
 * 
 *
 * TransferOB.java
 *
 * Created on August 12, 2003, 3:43 PM 2010  
 */
package com.see.truetransact.ui.transaction.transfer;

import com.see.truetransact.clientexception.ClientParseException;
import com.see.truetransact.clientproxy.ProxyFactory;
import com.see.truetransact.clientutil.ClientConstants;
import com.see.truetransact.clientutil.ClientUtil;
import com.see.truetransact.clientutil.ttrintegration.TTIntegration;
import com.see.truetransact.clientutil.ComboBoxModel;
import com.see.truetransact.clientutil.TableModel;
import com.see.truetransact.commonutil.CommonConstants;
import com.see.truetransact.commonutil.CommonUtil;
import com.see.truetransact.commonutil.DateUtil;
import com.see.truetransact.transferobject.transaction.transfer.TxTransferTO;
import com.see.truetransact.transferobject.transaction.reconciliation.ReconciliationTO;
import org.apache.poi.hssf.usermodel.HSSFDateUtil;
import com.see.truetransact.ui.TrueTransactMain;
import com.see.truetransact.uicomponent.CObservable;
import com.see.truetransact.uicomponent.COptionPane;
import com.see.truetransact.commonutil.TTException;
import com.see.truetransact.clientproxy.ProxyParameters;
import com.see.truetransact.transferobject.product.loan.LoanProductAccHeadTO;
import com.see.truetransact.transferobject.servicetax.servicetaxdetails.ServiceTaxDetailsTO;
import com.see.truetransact.ui.common.servicetax.ServiceTaxCalculation;
import java.io.BufferedInputStream;
import java.io.DataInputStream;
import java.io.FileInputStream;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Date;
import java.util.HashSet;
import java.util.Observable;
import java.util.Set;

/**
 * @author Pranav
 *
 * @modified Pinky
 */
public class TransferOB extends CObservable {

    private TransferRB resourceBundle;
    private String linkBatchIdForGl = "";
    private String batchId = "";
    private String transId = "";
    private String productId = "";
    private String accountHeadDesc = "";
    private String accountNo = "";
    private String instrumentNo1 = "";
    private String instrumentNo2 = "";
    private String instrumentDate = "";
    private String currencyType = "";
    private String transferAmount = "";
    private String particulars = "";
    private String narration = "";
    private HashMap ALL_LOAN_AMOUNT = new HashMap();
    private HashMap multiple_ALL_LOAN_AMOUNT = new HashMap();
    private HashMap debitLoanType = new HashMap();
    private int totalDrInstruments = 0;
    private double totalDrAmount = 0.0;
    private int totalCrInstruments = 0;
    private double totalCrAmount = 0.0;
    private HashMap loanDebitType = null;
    private String depInterestAmt = "";
    private String accNumGl = "";
    private boolean isBulkUploadTxtFile = false;
    private boolean invalidBulkUploadTxtFile = false;
    //    private String modeOfOperation = "";
    //    private String constitution = "";
    //    private String category = "";
    //    private String openingDate = "";
    //    private String remarks = "";
    //    private String availableBalance = "";
    //    private String totalBalance = "";
    //    private String clearBalance = "";
    //    private String shadowCr = "";
    //    private String shadowDr = "";
    private String status = "";
    private String productTypeValue = "";
    private String instrumentTypeValue = "";
    private String currencyTypeValue = "";
    private String mainProductTypeValue = "";
    private HashMap map = null;
    private ProxyFactory proxy = null;
    private String accountHeadId = "";
    private String action = "";
    private String amount = "";
    private String transDate = "";
    private Date backDatedTransDate = null;
    private String transType = "";
    private String instType = "";
    //private String tokenNo = "0";
    private String initTransId = "";
    private String initChannType = "";
    private String transStatus = "";
    private String authorizeRemarks = null;
    private ComboBoxModel productTypeModel;
    private ComboBoxModel instrumentTypeModel;
    private ComboBoxModel currencyTypeModel;
    private ComboBoxModel mainProductTypeModel;
    private String initBran = "";
    private boolean rdoBulkTransaction_Yes = false;
    private boolean rdoBulkTransaction_No = false;
    //To populate comboboxes
    private ArrayList key;
    private ArrayList value;
    private HashMap keyValue;
    private HashMap lookupMap;
    private int operation;
    private boolean checkDebitTermLoan = false;
    private HashMap oldAmountMap;
    //To manipulate status message
    private int result;
    private String lblStatus = ClientConstants.ACTION_STATUS[ClientConstants.ACTIONTYPE_CANCEL];
    // to show error in case of business rule checks
    private final static ClientParseException parseException = ClientParseException.getInstance();
    private HashMap authMap = new HashMap();
    private TableModel tbmTransfer;
    public ArrayList transferTOs, deleteTOsList, reconciliationList;
    private HashMap authorizeMap = null;
    private String transAmt = "";
    private String loanActNo = "";
    private String transDepositAmt = "";
    private String depositTransId = null;
    private double interestAmt = 0.0;
    private String depLinkBatchId = "";
    private String depAccNO;
    private String renewDepAmt = null;
    public boolean depositRenewalFlag = false;
    public boolean renewalIntFlag = false;
    public String depositPenalAmt = null;
    public String depositPenalMonth = null;
    private boolean depositPenalFlag = false;
    private String depositProdId = null;
    private Date curDate = null;
    private Date valueDate = null;
    private HashMap linkMap = new HashMap();
    private HashMap penalMap = new HashMap();
    private HashMap procChargeHash = new HashMap();  //This hashmap used to store details for Processing Charge
    private String creatingFlexi = "";
    private double flexiAmount = 0.0;
    private boolean valueDateFlag = false;
    private HashMap valueDateMap = null;
    private String reconcile = "";
    private String balanceType = "";
    public HashMap reconcileMap;
    private String authBy = "";
    boolean minBalException = false;
    private long emiNoInstallment = 0;
    private boolean penalWaiveOff = false;
    private boolean rebateInterest = false;
    private String closedAccNo = null;
    private boolean hoAccount = false;
    ArrayList orgRespList = null;
    private String hoAccountStatus = "";
    private String prodDebitTransfer = "";
    //added by rishad 02/04/2014 for waive purpose
    private double penalWaiveAmount = 0.0;
    private String kccNature = "";
    private double actInterestLoan=0;
    private HashMap serviceTax_Map=null;
    private HashMap glserviceTax_Map=null;
    private String lblServiceTaxval="";
    private ServiceTaxDetailsTO objservicetaxDetTo;
    private String serviceTaxAccNo="";
    private String singleTranId="";
    private ArrayList servicTaxDetList = new ArrayList();
    //added by rishad for seting waive amount 09/04/2015
    private boolean interestWaiveoff = false;
    private boolean noticeWaiveoff = false;
    private boolean principalwaiveoff = false;
    private boolean arcWaiveOff=false;
    private double arcWaiveAmount=0.0;
    private double noticeWaiveAmount = 0.0;
    private double interestWaiveAmount = 0.0;
    private double principalWaiveAmount = 0.0;
    private boolean overDueIntWaiveOff = false; // for overdue interest
    private boolean decreeWaiveOff = false;
    private boolean arbitraryWaiveOff = false;
    private boolean epCostWaiveOff = false;
    private boolean postageWaiveOff = false;
    private boolean recoveryWaiveOff = false;
    private boolean measurementWaiveOff = false;
    private boolean advertiseWaiveOff = false;
    private boolean legalWaiveOff = false;
    private boolean insuranceWaiveOff = false;
    private boolean miscellaneousWaiveOff = false;
    private double overDueIntWaiveAmount = 0; // for overdue interest
    private double epCostWaiveAmount = 0;
    private double postageWaiveAmount = 0;
    private double recoveryWaiveAmount = 0;
    private double measurementWaiveAmount = 0;
    private double advertiseWaiveAmount = 0;
    private double legalWaiveAmount = 0;
    private double insuranceWaiveAmont = 0;
    private double miscellaneousWaiveAmount = 0;
    private double arbitarayWaivwAmount=0;
    private double decreeWaiveAmount=0;
    private int installMentNo=0;
    private HashMap otherBankMap;
    private String instalType="";
    public String displayWaive = "";
    private String EMIinSimpleInterest = ""; // Added by nithya on 19-05-2020 for KD-380
    private boolean koleFieldOperationWaiveOff = false;
    private boolean koleFieldExpenseWaiveOff = false;
    private double koleFieldOperationWaiveAmount=0.0;
    private double koleFieldExpenseWaiveAmount=0.0;
    private String accNumGlProdType = ""; //Added y nithya on 17-Jul-2025 for KD-4108

    public HashMap getGlserviceTax_Map() {
        return glserviceTax_Map;
    }

    public void setGlserviceTax_Map(HashMap glserviceTax_Map) {
        this.glserviceTax_Map = glserviceTax_Map;
    }
  
    public String getInstalType() {
        return instalType;
    }

    public void setInstalType(String instalType) {
        this.instalType = instalType;
    }
     
    public HashMap getOtherBankMap() {
        return otherBankMap;
    }

    public void setOtherBankMap(HashMap otherBankMap) {
        this.otherBankMap = otherBankMap;
    }

    public int getInstallMentNo() {
        return installMentNo;
    }

    public void setInstallMentNo(int installMentNo) {
        this.installMentNo = installMentNo;
    }
    
    public double getAdvertiseWaiveAmount() {
        return advertiseWaiveAmount;
    }

    public void setAdvertiseWaiveAmount(double advertiseWaiveAmount) {
        this.advertiseWaiveAmount = advertiseWaiveAmount;
    }

    public boolean isAdvertiseWaiveOff() {
        return advertiseWaiveOff;
    }

    public void setAdvertiseWaiveOff(boolean advertiseWaiveOff) {
        this.advertiseWaiveOff = advertiseWaiveOff;
    }

    public double getArbitarayWaivwAmount() {
        return arbitarayWaivwAmount;
    }

    public void setArbitarayWaivwAmount(double arbitarayWaivwAmount) {
        this.arbitarayWaivwAmount = arbitarayWaivwAmount;
    }

    public boolean isArbitraryWaiveOff() {
        return arbitraryWaiveOff;
    }

    public void setArbitraryWaiveOff(boolean arbitraryWaiveOff) {
        this.arbitraryWaiveOff = arbitraryWaiveOff;
    }

    public double getDecreeWaiveAmount() {
        return decreeWaiveAmount;
    }

    public void setDecreeWaiveAmount(double decreeWaiveAmount) {
        this.decreeWaiveAmount = decreeWaiveAmount;
    }

    public boolean isDecreeWaiveOff() {
        return decreeWaiveOff;
    }

    public void setDecreeWaiveOff(boolean decreeWaiveOff) {
        this.decreeWaiveOff = decreeWaiveOff;
    }

    public double getEpCostWaiveAmount() {
        return epCostWaiveAmount;
    }

    public void setEpCostWaiveAmount(double epCostWaiveAmount) {
        this.epCostWaiveAmount = epCostWaiveAmount;
    }

    public boolean isEpCostWaiveOff() {
        return epCostWaiveOff;
    }

    public void setEpCostWaiveOff(boolean epCostWaiveOff) {
        this.epCostWaiveOff = epCostWaiveOff;
    }

    public double getInsuranceWaiveAmont() {
        return insuranceWaiveAmont;
    }

    public void setInsuranceWaiveAmont(double insuranceWaiveAmont) {
        this.insuranceWaiveAmont = insuranceWaiveAmont;
    }

    public boolean isInsuranceWaiveOff() {
        return insuranceWaiveOff;
    }

    public void setInsuranceWaiveOff(boolean insuranceWaiveOff) {
        this.insuranceWaiveOff = insuranceWaiveOff;
    }

    public double getLegalWaiveAmount() {
        return legalWaiveAmount;
    }

    public void setLegalWaiveAmount(double legalWaiveAmount) {
        this.legalWaiveAmount = legalWaiveAmount;
    }

    public boolean isLegalWaiveOff() {
        return legalWaiveOff;
    }

    public void setLegalWaiveOff(boolean legalWaiveOff) {
        this.legalWaiveOff = legalWaiveOff;
    }

    public double getMiscellaneousWaiveAmount() {
        return miscellaneousWaiveAmount;
    }

    public void setMiscellaneousWaiveAmount(double miscellaneousWaiveAmount) {
        this.miscellaneousWaiveAmount = miscellaneousWaiveAmount;
    }

    public boolean isMiscellaneousWaiveOff() {
        return miscellaneousWaiveOff;
    }

    public void setMiscellaneousWaiveOff(boolean miscellaneousWaiveOff) {
        this.miscellaneousWaiveOff = miscellaneousWaiveOff;
    }

    public double getPostageWaiveAmount() {
        return postageWaiveAmount;
    }

    public void setPostageWaiveAmount(double postageWaiveAmount) {
        this.postageWaiveAmount = postageWaiveAmount;
    }

    public boolean isPostageWaiveOff() {
        return postageWaiveOff;
    }

    public void setPostageWaiveOff(boolean postageWaiveOff) {
        this.postageWaiveOff = postageWaiveOff;
    }
    
    public double getArcWaiveAmount() {
        return arcWaiveAmount;
    }

    public void setArcWaiveAmount(double arcWaiveAmount) {
        this.arcWaiveAmount = arcWaiveAmount;
    }

    public boolean isArcWaiveOff() {
        return arcWaiveOff;
    }

    public void setArcWaiveOff(boolean arcWaiveOff) {
        this.arcWaiveOff = arcWaiveOff;
    }
   
    public double getInterestWaiveAmount() {
        return interestWaiveAmount;
    }

    public void setInterestWaiveAmount(double interestWaiveAmount) {
        this.interestWaiveAmount = interestWaiveAmount;
    }

    public boolean isInterestWaiveoff() {
        return interestWaiveoff;
    }

    public void setInterestWaiveoff(boolean interestWaiveoff) {
        this.interestWaiveoff = interestWaiveoff;
    }

    public double getNoticeWaiveAmount() {
        return noticeWaiveAmount;
    }

    public void setNoticeWaiveAmount(double noticeWaiveAmount) {
        this.noticeWaiveAmount = noticeWaiveAmount;
    }

    public boolean isNoticeWaiveoff() {
        return noticeWaiveoff;
    }

    public void setNoticeWaiveoff(boolean noticeWaiveoff) {
        this.noticeWaiveoff = noticeWaiveoff;
    }

    public double getPrincipalWaiveAmount() {
        return principalWaiveAmount;
    }

    public void setPrincipalWaiveAmount(double principalWaiveAmount) {
        this.principalWaiveAmount = principalWaiveAmount;
    }

    public boolean isPrincipalwaiveoff() {
        return principalwaiveoff;
    }

    public void setPrincipalwaiveoff(boolean principalwaiveoff) {
        this.principalwaiveoff = principalwaiveoff;
    }

    // For overdue interest
    public double getOverDueIntWaiveAmount() {
        return overDueIntWaiveAmount;
    }

    public void setOverDueIntWaiveAmount(double overDueIntWaiveAmount) {
        this.overDueIntWaiveAmount = overDueIntWaiveAmount;
    }

    public boolean isOverDueIntWaiveOff() {
        return overDueIntWaiveOff;
    }

    public void setOverDueIntWaiveOff(boolean overDueIntWaiveOff) {
        this.overDueIntWaiveOff = overDueIntWaiveOff;
    }
    // For over due int
    public Date getBackDatedTransDate() {
        return backDatedTransDate;
    }

    public void setBackDatedTransDate(Date backDatedTransDate) {
        this.backDatedTransDate = backDatedTransDate;
    }
    
    public double getActInterestLoan() {
        return actInterestLoan;
    }

    public void setActInterestLoan(double actInterestLoan) {
        this.actInterestLoan = actInterestLoan;
    }

    public String getKccNature() {
        return kccNature;
    }

    public void setKccNature(String kccNature) {
        this.kccNature = kccNature;
    }

    public String getProdCreditTransfer() {
        return prodCreditTransfer;
    }

    public void setProdCreditTransfer(String prodCreditTransfer) {
        this.prodCreditTransfer = prodCreditTransfer;
    }

    public String getProdDebitTransfer() {
        return prodDebitTransfer;
    }

    public void setProdDebitTransfer(String prodDebitTransfer) {
        this.prodDebitTransfer = prodDebitTransfer;
    }
    private String prodCreditTransfer = "";

    /**
     * Creates a new instance of TransferOB
     */
    public TransferOB() {
        /*
         * set up the map for proxy, this will be used to invoke the business
         * methods
         */
        curDate = ClientUtil.getCurrentDate();
        map = new HashMap();
        map.put(CommonConstants.JNDI, "TransferJNDI");
        map.put(CommonConstants.HOME, "transaction.transfer.TransferHome");
        map.put(CommonConstants.REMOTE, "transaction.transfer.TransferRemote");
        try {
            // create the proxy
            proxy = ProxyFactory.createProxy();
            fillDropdown();
            transferTOs = new ArrayList();
            deleteTOsList = new ArrayList();
            reconciliationList = new ArrayList();
            oldAmountMap = new HashMap();
            setTable();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void setTable() {
        ArrayList columnHeader = new ArrayList();
        columnHeader.add("Account No");
        columnHeader.add("Batch ID");
        columnHeader.add("Trans ID");
        columnHeader.add("Amount");
        columnHeader.add("Type");
        if (getOperation() == ClientConstants.ACTIONTYPE_AUTHORIZE) {
            columnHeader.add("Verified");
        }
        ArrayList data = new ArrayList();
        tbmTransfer = new TableModel(data, columnHeader);
    }

    /**
     * To fill the comboboxes
     */
    private void fillDropdown() throws Exception {
        try {
            lookupMap = new HashMap();
            lookupMap.put(CommonConstants.JNDI, "LookUpJNDI");
            lookupMap.put(CommonConstants.HOME, "common.lookup.LookUpHome");
            lookupMap.put(CommonConstants.REMOTE, "common.lookup.LookUp");

            final HashMap param = new HashMap();
            key = new ArrayList();
            value = new ArrayList();
            key.add("");
            value.add("");
            productTypeModel = new ComboBoxModel(key, value);
            instrumentTypeModel = new ComboBoxModel(key, value);

            final ArrayList lookupKey = new ArrayList();
            lookupKey.add("FOREX.CURRENCY");
            lookupKey.add("PRODUCTTYPE");
            param.put(CommonConstants.MAP_NAME, null);
            param.put(CommonConstants.PARAMFORQUERY, lookupKey);
            HashMap lookupValues = ClientUtil.populateLookupData(param);

            fillData((HashMap) lookupValues.get("FOREX.CURRENCY"));
            currencyTypeModel = new ComboBoxModel(key, value);

            fillData((HashMap) lookupValues.get("PRODUCTTYPE"));
            mainProductTypeModel = new ComboBoxModel(key, value);
            if(mainProductTypeModel.containsElement("MDS")){
               mainProductTypeModel.removeKeyAndElement(CommonUtil.convertObjToStr("MDS"));
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void fillInstrumentType() {
        final HashMap param = new HashMap();
        final ArrayList lookupKey = new ArrayList();
        lookupKey.add("INSTRUMENTTYPE");
        try {
            param.put(CommonConstants.MAP_NAME, null);
            param.put(CommonConstants.PARAMFORQUERY, lookupKey);
            HashMap lookupValues = ClientUtil.populateLookupData(param);

            fillData((HashMap) lookupValues.get("INSTRUMENTTYPE"));
            if (this.getOperation() == ClientConstants.ACTIONTYPE_NEW) {
                int index = key.indexOf("ONLINE_TRANSFER");
                key.remove(index);
                value.remove(index);
            }
            key.add("ECS");
            value.add("ECS");
            instrumentTypeModel = new ComboBoxModel(key, value);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * To set the key & value for comboboxes
     */
    private void fillData(HashMap keyValue) throws Exception {
        key = (ArrayList) keyValue.get(CommonConstants.KEY);
        value = (ArrayList) keyValue.get(CommonConstants.VALUE);
    }

    /**
     * To get data for comboboxes
     */
    private HashMap populateDataLocal(HashMap obj) throws Exception {
        keyValue = proxy.executeQuery(obj, lookupMap);

        return keyValue;
    }

    /**
     * set the values on the UI to some initial value
     */
    public void resetOBFields() {
        batchId = "";
        status = "";
        action = "";
        totalDrInstruments = 0;
        totalDrAmount = 0.0;
        totalCrInstruments = 0;
        totalCrAmount = 0.0;
        authorizeMap = null;
        setDepositPenalAmt("");
        setDepositPenalMonth("");
        resetTable();
        valueDateMap = null;
        valueDate = null;
        resetTransactionDetails();
        valueDateFlag = false;
        rdoBulkTransaction_No = false;
        rdoBulkTransaction_Yes = false;
        setClosedAccNo("");
        setHoAccount(false);
        setHoAccountStatus("");
        setLblServiceTaxval("");
        setServiceTaxAccNo("");
        setServiceTax_Map(null);
        setGlserviceTax_Map(null);
        setSingleTranId("");
        servicTaxDetList = new ArrayList();
         setPenalWaiveOff(false);
        setRebateInterest(false);
        setInterestWaiveoff(false);
        setNoticeWaiveoff(false);
        setPrincipalwaiveoff(false);
        penalWaiveAmount = 0;
        interestWaiveAmount = 0;
        noticeWaiveAmount = 0;
        principalWaiveAmount = 0;
        setPenalWaiveAmount(0);
        setInterestWaiveAmount(0);
        setPrincipalWaiveAmount(0);
        setNoticeWaiveAmount(0);
        setArbitarayWaivwAmount(0);
        setArcWaiveAmount(0);
        setAdvertiseWaiveAmount(0);
        setInsuranceWaiveAmont(0);
        setLegalWaiveAmount(0);
        setMiscellaneousWaiveAmount(0);
        setDecreeWaiveAmount(0);
        setEpCostWaiveAmount(0);
        setPostageWaiveAmount(0);
        setPenalWaiveOff(false);
        setInterestWaiveoff(false);
        setPrincipalwaiveoff(false);
        setNoticeWaiveoff(false);
        setArbitraryWaiveOff(false);
        setArcWaiveOff(false);
        setAdvertiseWaiveOff(false);
        setLegalWaiveOff(false);
        setInsuranceWaiveOff(false);
        setDecreeWaiveOff(false);
        setInsuranceWaiveOff(false);
        setPostageWaiveOff(false);
        setMiscellaneousWaiveOff(false);
        setPenalWaiveAmount(0);
        setInterestWaiveAmount(0);
        setPrincipalWaiveAmount(0);
        setNoticeWaiveAmount(0);
        setArbitarayWaivwAmount(0);
        setArcWaiveAmount(0);
        setAdvertiseWaiveAmount(0);
        setInsuranceWaiveAmont(0);
        setLegalWaiveAmount(0);
        setMiscellaneousWaiveAmount(0);
        setDecreeWaiveAmount(0);
        setEpCostWaiveAmount(0);
        setPostageWaiveAmount(0);
        setPenalWaiveOff(false);
        setInterestWaiveoff(false);
        setPrincipalwaiveoff(false);
        setNoticeWaiveoff(false);
        setArbitraryWaiveOff(false);
        setArcWaiveOff(false);
        setAdvertiseWaiveOff(false);
        setLegalWaiveOff(false);
        setInsuranceWaiveOff(false);
        setDecreeWaiveOff(false);
        setInsuranceWaiveOff(false);
        setPostageWaiveOff(false);
        setMiscellaneousWaiveOff(false);
        setEMIinSimpleInterest("N"); // Added by nithya on 19-05-2020 for KD-380
        isBulkUploadTxtFile = false;
        setRecoveryWaiveAmount(0);
        setRecoveryWaiveOff(false);
        setMeasurementWaiveAmount(0);
        setMeasurementWaiveOff(false);
        
        koleFieldExpenseWaiveAmount = 0.0;
        koleFieldOperationWaiveAmount = 0.0;
        setKoleFieldExpenseWaiveOff(false);
        setKoleFieldOperationWaiveOff(false);
        
        setAccNumGlProdType(""); //Added y nithya on 17-Jul-2025 for KD-4108
    }

    public void resetTransactionDetails() {
        transId = "";
        productId = "";
        accountHeadDesc = "";
        accountNo = "";
        instrumentNo1 = "";
        instrumentNo2 = "";
        instrumentDate = "";
        currencyType = "";
        transferAmount = "";
        particulars = "";
        narration = "";
        //
        //        constitution = "";
        //        category = "";
        //        openingDate = "";
        //        remarks = "";
        //
        //        availableBalance = "";
        //        totalBalance = "";
        //        clearBalance = "";
        //        shadowCr = "";
        //        shadowDr = "";

        accountHeadId = "";
        amount = "";
        transDate = "";
        transType = "";
        instType = "";
        initTransId = "";
        initChannType = "";
        transStatus = "";

        productTypeValue = "";
        instrumentTypeValue = "";
        currencyTypeValue = "";
        mainProductTypeValue = "";
        lblServiceTaxval="";
        setChanged();
        notifyObservers();
    }

    /**
     * set the value of Account head ID and description based on the product
     * selected in the UI this method will use the LookupMap
     */
    public String getAccountHeadForProductId(String productId) {
        /*
         * may be the screen has been cleared, in that scenario we will have the
         * cboProductId as "", and we don;t want anything to be shown in place
         * of the account head description
         */

        if (productId == null || productId.equals("")) {
            return "";
        }
        /*
         * based on the selection from the product combo box, one accound head
         * will be fetched from database and displayed on screen same LookUp
         * bean will be used for this purpose
         */
        /*
         * HashMap hash; ArrayList key, value;
         *
         * hash = new HashMap();
         * hash.put(CommonConstants.MAP_NAME,"getAccHead");
         * hash.put(CommonConstants.PARAMFORQUERY, productId); hash =
         * (HashMap)(ClientUtil.populateLookupData(hash)).get(CommonConstants.DATA);
         * key = (ArrayList) hash.get(CommonConstants.KEY); value = (ArrayList)
         * hash.get(CommonConstants.VALUE);
         *
         * // the 0th value is blank bydefault accountHeadId =
         * (String)key.get(1); accountHeadDesc = (String)value.get(1);
         *
         * hash = null; key = null; value = null;
         */

        final HashMap accountHeadMap = new HashMap();
        accountHeadMap.put("PROD_ID", productId);
        final List resultList = ClientUtil.executeQuery("getAccountHeadProd" + this.getMainProductTypeValue(), accountHeadMap);
        if(resultList != null && resultList.size() > 0){
            final HashMap resultMap = (HashMap) resultList.get(0);
            accountHeadId = (resultMap.get("AC_HEAD").toString());
            accountHeadDesc = (resultMap.get("AC_HEAD_DESC").toString());
        }
        return accountHeadId + " [" + accountHeadDesc + "]";
    }

    /**
     * get the value
     */
    public int getData(String transactionId) {
        ArrayList arr = null;
        try {
            getTransferData();
            this.fillInstrumentType();
            if (transactionId != null) {
                return populateTransfer(transactionId);
            }
            notifyObservers();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return -1;
    }
    
    private void setTransferTO(TxTransferTO to) {
        setTransId(to.getTransId());
        setBatchId(to.getBatchId());
        setMainProductTypeValue(to.getProdType());
        setProductTypeValue(to.getProdType());
        setProductId(to.getProdId());
        setAccountHeadId(to.getAcHdId());
        setAccountNo(to.getActNum());
        setTransferAmount(CommonUtil.convertObjToStr(to.getInpAmount()));
        setCurrencyType(to.getInpCurr());
        setCurrencyTypeValue(to.getInpCurr());
        setAmount(CommonUtil.convertObjToStr(to.getAmount()));
        Double amt = to.getAmount();
        //System.out.println("Double into  String " + amt.toString());
        setAmount(String.valueOf(to.getAmount()));
        setTransDate(DateUtil.getStringDate(to.getTransDt()));
        setTransType(to.getTransType());
        setInstType(to.getInstType());
        setInstrumentTypeValue(to.getInstType());
        setInstrumentNo1(to.getInstrumentNo1());
        setInstrumentNo2(to.getInstrumentNo2());
        setInstrumentDate(DateUtil.getStringDate(to.getInstDt()));
        setInitTransId(to.getInitTransId());
        setInitChannType(to.getInitChannType());
        setParticulars(to.getParticulars());
        setNarration(to.getNarration());
        setLinkBatchIdForGl(to.getLinkBatchId());
        setTransStatus(to.getStatus());
        //this.getProducts();
        setAuthorizeRemarks(to.getAuthorizeRemarks());
        setAuthBy(to.getAuthorizeBy());
        setChanged();
        notifyObservers();
    }

    // Checks a/c no. existence without prod_type & prod_id
    public boolean checkAcNoWithoutProdType(String actNum, boolean branchIdOnly) {

        HashMap mapData = new HashMap();
        boolean isExists = false;
        try {//dont delete chck selectalldao
            if (actNum.indexOf("_") > 0) {
                actNum = actNum.substring(0, actNum.indexOf("_"));
            }

            //System.out.println("actNum" + actNum);
            if(actNum.length( ) == 0){
                actNum = null;
            }
            mapData.put("ACT_NUM", actNum);
            List mapDataList = ClientUtil.executeQuery("getActNumFromAllProducts", mapData);
            //System.out.println("#### mapDataList :" + mapDataList);
            if (mapDataList != null && mapDataList.size() > 0) {
                mapData = (HashMap) mapDataList.get(0);
                setSelectedBranchID(CommonUtil.convertObjToStr(mapData.get("BRANCH_ID")));
                if (branchIdOnly) {
                    return true;
                } else {
                    setAccountNo(CommonUtil.convertObjToStr(mapData.get("ACT_NUM")));
                }
                mainProductTypeModel.setKeyForSelected(CommonUtil.convertObjToStr(mapData.get("PROD_TYPE")));
                setMainProductTypeValue(CommonUtil.convertObjToStr(mapData.get("PROD_TYPE")));
                getProducts();
                productTypeModel.setKeyForSelected(CommonUtil.convertObjToStr(mapData.get("PROD_ID")));
                isExists = true;
            } else {
                ArrayList key = new ArrayList();
                ArrayList value = new ArrayList();
                key.add("");
                value.add("");
                productTypeModel = new ComboBoxModel(key, value);
                isExists = false;
                key = null;
                value = null;
                isExists = false;
            }
            mapDataList = null;
        } catch (Exception e) {
            e.printStackTrace();
        }
        mapData = null;
        return isExists;
    }

    public boolean depositAuthorizationValidation(ArrayList transferTOs) {
        int size = this.transferTOs.size();
        TxTransferTO objTO;
        boolean auth = false;
        for (int i = 0; i < size; i++) {
            objTO = (TxTransferTO) transferTOs.get(i);
            if (!objTO.getStatus().equalsIgnoreCase(CommonConstants.STATUS_DELETED) && this.operation == ClientConstants.ACTIONTYPE_AUTHORIZE
                    && objTO.getTransType().equals(CommonConstants.CREDIT) && objTO.getProdType() != null && objTO.getProdType().equals("TD")) {
                HashMap depAuthMap = new HashMap();
                String AcNo = "";
                if (objTO.getActNum().lastIndexOf("_") != -1) {
                    AcNo = objTO.getActNum().substring(0, objTO.getActNum().lastIndexOf("_"));
                }
                depAuthMap.put("DEPOSIT_NO", AcNo);
                List lst = ClientUtil.executeQuery("getSelectRemainingBalance", depAuthMap);
                if (lst != null && lst.size() > 0) {
                    depAuthMap = (HashMap) lst.get(0);
                    double depositAmt = CommonUtil.convertObjToDouble(depAuthMap.get("DEPOSIT_AMT")).doubleValue();
                    double totalBalance = CommonUtil.convertObjToDouble(depAuthMap.get("TOTAL_BALANCE")).doubleValue();
                    double remainingAmt = depositAmt - totalBalance;
                    if (remainingAmt < CommonUtil.convertObjToDouble(objTO.getAmount()).doubleValue()) {
                        if (remainingAmt > 0) {
                            ClientUtil.showAlertWindow("Cannot authorize, amount exceeding the deposit amount"
                                    + "\n Deposit Amount is :" + depositAmt
                                    + "\n Balance Amount to be collected :" + remainingAmt);
                        } else {
                            ClientUtil.showAlertWindow("Cannot authorize, amount exceeding the deposit amount"
                                    + "\n Deposit Amount is :" + depositAmt
                                    + "Please Reject the Transaction ");
                        }
                        auth = true;
                    }
                }
            }
        }
        return auth;
    }
    //as and when customer comes interest calculaion

    public HashMap loanInterestCalculationAsAndWhen(HashMap whereMap) {
        HashMap mapData = new HashMap();
        try {//dont delete chck selectalldao
            List mapDataList = ClientUtil.executeQuery("", whereMap); //, frame);
            mapData = (HashMap) mapDataList.get(0);
        } catch (Exception e) {
            e.printStackTrace();
        }
        //System.out.println("#### MapData :" + mapData);
        return mapData;
    }

    private HashMap populateBean(String parameter) throws Exception {
        //System.out.println("penal in transfer");
        HashMap transaction = new HashMap();
        HashMap rdPenalMap=new HashMap();
        if (!minBalException) {
            transferTOs.addAll(this.deleteTOsList);
        }

        if (operation == ClientConstants.ACTIONTYPE_NEW) {
            transaction.put("COMMAND", CommonConstants.TOSTATUS_INSERT);
        } else if (operation == ClientConstants.ACTIONTYPE_EDIT) {
            transaction.put("COMMAND", CommonConstants.TOSTATUS_UPDATE);
        } else if (operation == ClientConstants.ACTIONTYPE_DELETE) {
            transaction.put("COMMAND", CommonConstants.TOSTATUS_DELETE);
            setStatusForTOs();
        }
        if (!getReconcile().equals("") && getReconcile().equals("N")) {
            transaction.put("LIST_OF_REDUCED", reconcileMap);
        }
        transaction.put("TxTransferTO", this.transferTOs);
        //System.out.println("@@@@transaction" + transaction);
        if (operation == ClientConstants.ACTIONTYPE_NEW && reconciliationList.size() > 0) {
            transaction.put("ReconciliationTO", this.reconciliationList);
        }
        transaction.put("OLDAMOUNT", this.oldAmountMap);

//        if(depositPenalFlag == true){
        boolean depositFlag = false;
        TxTransferTO objTOTD = null;
        int size = this.transferTOs.size();
        HashMap depositPenalMap = null;
        HashMap multiDepositPenalMap = new HashMap();
        for (int j = 0; j < size; j++) {
            objTOTD = (TxTransferTO) transferTOs.get(j);
            if (objTOTD.getProdType() != null && objTOTD.getProdType().equals("TD")) {
                depositFlag = true;
                depositPenalMap = new HashMap();
//                    break;
            }
//            }
            //System.out.println("depositFlag===" + depositFlag + "depositPenalFlag===" + depositPenalFlag);
            if (depositFlag == true) {
                String act_Num = CommonUtil.convertObjToStr(objTOTD.getActNum());
                if (act_Num.lastIndexOf("_") != -1) {
                    act_Num = act_Num.substring(0, act_Num.lastIndexOf("_"));
                }
                HashMap accountMap = new HashMap();
                accountMap.put("DEPOSIT_NO", act_Num);
                rdPenalMap.put("DEPOSIT_NO", act_Num);//Added by Ajay Sharma for Mantis ID - 7952 dated 21-May-2014
                accountMap.put("BRANCH_ID", objTOTD.getBranchId());
                List lst = ClientUtil.executeQuery("getProductIdForDeposits", accountMap);
                if (lst != null && lst.size() > 0) {
                    accountMap = (HashMap) lst.get(0);
                    HashMap prodMap = new HashMap();
                    prodMap.put("PROD_ID", accountMap.get("PROD_ID"));
                    List lstBehave = ClientUtil.executeQuery("getBehavesLikeForDeposit", prodMap);
                    if (lstBehave != null && lstBehave.size() > 0) {
                        prodMap = (HashMap) lstBehave.get(0);
                        if (prodMap.get("BEHAVES_LIKE").equals("RECURRING")) {
                            Date currDt = (Date) curDate.clone();
                            long totalDelay = 0;
                            long actualDelay = 0;
                            double delayAmt = 0.0;
                            double tot_Inst_paid = 0.0;
                            double depAmt = CommonUtil.convertObjToDouble(accountMap.get("DEPOSIT_AMT")).doubleValue();
                            Date matDt = (Date) curDate.clone();
                            Date depDt = (Date) curDate.clone();
                            HashMap lastMap = new HashMap();
                            lastMap.put("DEPOSIT_NO", act_Num);
                            lst = ClientUtil.executeQuery("getInterestDeptIntTable", lastMap);
                            //System.out.println("#######getLastApplDt for balanceMap:" + lst);
                            if (lst != null && lst.size() > 0) {
                                lastMap = (HashMap) lst.get(0);
                                tot_Inst_paid = CommonUtil.convertObjToDouble(lastMap.get("TOTAL_INSTALL_PAID")).doubleValue();
                                HashMap prematureDateMap = new HashMap();
                                double monthPeriod = 0.0;
                                Date matDate = DateUtil.getDateMMDDYYYY(CommonUtil.convertObjToStr(lastMap.get("MATURITY_DT")));
                                if (matDate.getDate() > 0) {
                                    matDt.setDate(matDate.getDate());
                                    matDt.setMonth(matDate.getMonth());
                                    matDt.setYear(matDate.getYear());
                                }
                                Date depDate = DateUtil.getDateMMDDYYYY(CommonUtil.convertObjToStr(lastMap.get("DEPOSIT_DT")));
                                if (depDate.getDate() > 0) {
                                    depDt.setDate(depDate.getDate());
                                    depDt.setMonth(depDate.getMonth());
                                    depDt.setYear(depDate.getYear());
                                }
                                if (DateUtil.dateDiff((Date) matDt, (Date) currDt) > 0) {
                                    prematureDateMap.put("TO_DATE", matDt);
                                    prematureDateMap.put("FROM_DATE", lastMap.get("DEPOSIT_DT"));
                                    lst = ClientUtil.executeQuery("periodRunMap", prematureDateMap);
                                    if (lst != null && lst.size() > 0) {
                                        prematureDateMap = (HashMap) lst.get(0);
                                        monthPeriod = CommonUtil.convertObjToDouble(prematureDateMap.get("NO_OF_MONTHS")).doubleValue();
                                        actualDelay = (long) monthPeriod - (long) tot_Inst_paid;
                                    }
                                    lst = null;
                                } else {
                                    int dep = depDt.getMonth() + 1;
                                    int curr = currDt.getMonth() + 1;
                                    int depYear = depDt.getYear() + 1900;
                                    int currYear = currDt.getYear() + 1900;
                                    if (depYear == currYear) {
                                        monthPeriod = curr - dep;
                                        actualDelay = (long) monthPeriod - (long) tot_Inst_paid;
                                    } else {
                                        int diffYear = currYear - depYear;
                                        monthPeriod = (diffYear * 12 - dep) + curr;
                                        actualDelay = (long) monthPeriod - (long) tot_Inst_paid;
                                    }
                                }
                            }
                            lst = null;
                            //delayed installment calculation...
                            if (depositPenalFlag == true) {  // Condition added by Rajesh
                                if (DateUtil.dateDiff((Date) matDt, (Date) currDt) < 0) {
                                    depAmt = CommonUtil.convertObjToDouble(accountMap.get("DEPOSIT_AMT")).doubleValue();
                                    double chargeAmt = depAmt / 100;
                                    HashMap delayMap = new HashMap();
                                    delayMap.put("PROD_ID", accountMap.get("PROD_ID"));
                                    delayMap.put("DEPOSIT_AMT", accountMap.get("DEPOSIT_AMT"));
                                    lst = ClientUtil.executeQuery("getSelectDelayedRate", delayMap);
                                    if (lst != null && lst.size() > 0) {
                                        delayMap = (HashMap) lst.get(0);
                                        delayAmt = CommonUtil.convertObjToDouble(delayMap.get("PENAL_INT")).doubleValue();
                                        delayAmt = delayAmt * chargeAmt;
                                        //System.out.println("######recurring delayAmt : " + delayAmt);
                                    }
                                    lst = null;
                                    HashMap depRecMap = new HashMap();
                                    depRecMap.put("DEPOSIT_NO", objTOTD.getActNum());
                                    List lstRec = ClientUtil.executeQuery("getDepTransactionRecurring", depRecMap);
                                    if (lstRec != null && lstRec.size() > 0) {//how many unCompleted quarter is there upto (one by one) we have to calculate simple interest formula....
                                        for (int i = 0; i < lstRec.size(); i++) {//when the customer is paid from that dt to today dt....
                                            depRecMap = (HashMap) lstRec.get(i);
                                            Date transDt = DateUtil.getDateMMDDYYYY(CommonUtil.convertObjToStr(depRecMap.get("TRANS_DT")));
                                            Date dueDate = DateUtil.getDateMMDDYYYY(CommonUtil.convertObjToStr(depRecMap.get("DUE_DATE")));
                                            int transMonth = transDt.getMonth() + 1;
                                            int dueMonth = dueDate.getMonth() + 1;
                                            int dueYear = dueDate.getYear() + 1900;
                                            int transYear = transDt.getYear() + 1900;
                                            int delayedInstallment;// = transMonth - dueMonth;
                                            if (dueYear == transYear) {
                                                delayedInstallment = transMonth - dueMonth;
                                            } else {
                                                int diffYear = transYear - dueYear;
                                                delayedInstallment = (diffYear * 12 - dueMonth) + transMonth;
                                            }
                                            if (delayedInstallment < 0) {
                                                delayedInstallment = 0;
                                            }
                                            totalDelay = totalDelay + delayedInstallment;
                                        }
                                    }
                                    lstRec = null;
                                    //totalDelay=0;//added by chithra on 16-05-14 for correcting  credit amount posting into trans_details
                                    depRecMap = new HashMap();
                                    depRecMap.put("DEPOSIT_NO", objTOTD.getActNum());
                                    depRecMap.put("DEPOSIT_DT", lastMap.get("DEPOSIT_DT"));
                                    depRecMap.put("CURR_DT", currDt);
                                    depRecMap.put("SL_NO", new Double(tot_Inst_paid));
                                    lstRec = ClientUtil.executeQuery("getDepTransRecurr", depRecMap);
                                    if (lstRec != null && lstRec.size() > 0) {//how many unCompleted quarter is there upto (one by one) we have to calculate simple interest formula....
                                        for (int i = 0; i < lstRec.size(); i++) {//when the customer is paid from that dt to today dt....
                                            depRecMap = (HashMap) lstRec.get(i);
                                            Date dueDate = DateUtil.getDateMMDDYYYY(CommonUtil.convertObjToStr(depRecMap.get("DUE_DATE")));
                                            int transMonth = currDt.getMonth() + 1;
                                            int dueMonth = dueDate.getMonth() + 1;
                                            int dueYear = dueDate.getYear() + 1900;
                                            int transYear = currDt.getYear() + 1900;
                                            int delayedInstallment;// = transMonth - dueMonth;
                                            if (dueYear == transYear) {
                                                delayedInstallment = transMonth - dueMonth;
                                            } else {
                                                int diffYear = transYear - dueYear;
                                                delayedInstallment = (diffYear * 12 - dueMonth) + transMonth;
                                            }
                                            if (delayedInstallment < 0) {
                                                delayedInstallment = 0;
                                            }
                                            totalDelay = totalDelay + delayedInstallment;
                                        }
                                    }
                                    lstRec = null;
                                    delayAmt = delayAmt * totalDelay;
                                    delayAmt = (double) getNearest((long) (delayAmt * 100), 100) / 100;
                                    double oldPenalAmt = CommonUtil.convertObjToDouble(accountMap.get("DELAYED_AMOUNT")).doubleValue();
                                    long oldPenalMonth = CommonUtil.convertObjToLong(accountMap.get("DELAYED_MONTH"));
                                    double balanceAmt = 0.0;
                                    if (oldPenalAmt > 0) {
                                        balanceAmt = delayAmt - oldPenalAmt;
                                        totalDelay = totalDelay - oldPenalMonth;
                                    } else {
                                        balanceAmt = delayAmt;
                                    }
                                    if (balanceAmt > 0) {
                                        //System.out.println("balanceAmt    ojiouki" + balanceAmt);
                                        //System.out.println("totalDelay kjoiij" + totalDelay);
                                       // depositPenalMap.put("DEPOSIT_PENAL_AMT", String.valueOf(balanceAmt));
                                       // depositPenalMap.put("DEPOSIT_PENAL_MONTH", String.valueOf(totalDelay));
                                        
                                        depositPenalMap.put("DEPOSIT_PENAL_AMT", getDepositPenalAmt());
                                        depositPenalMap.put("DEPOSIT_PENAL_MONTH", getDepositPenalMonth());
                                        multiDepositPenalMap.put(objTOTD.getActNum(), depositPenalMap);
//                                        rdPenalMap.put("DELAYED_AMOUNT", String.valueOf(balanceAmt));//Added by Ajay Sharma for Mantis ID - 7952 dated 21-May-2014
//                                        rdPenalMap.put("TEMP_DELAYED_AMOUNT", String.valueOf(balanceAmt));
//                                        rdPenalMap.put("DELAYED_MONTH", String.valueOf(totalDelay));
//                                        rdPenalMap.put("TEMP_DELAYED_MONTH", String.valueOf(totalDelay));
                                        rdPenalMap.put("DELAYED_AMOUNT", getDepositPenalAmt());//Added by Ajay Sharma for Mantis ID - 7952 dated 21-May-2014
                                        rdPenalMap.put("TEMP_DELAYED_AMOUNT", getDepositPenalAmt());
                                        rdPenalMap.put("DELAYED_MONTH", getDepositPenalMonth());
                                        rdPenalMap.put("TEMP_DELAYED_MONTH", getDepositPenalMonth());
                                        //System.out.println("Value of Map 1: "+rdPenalMap);
                                        ClientUtil.execute("updateDepositPenalAmountTemp", rdPenalMap);
//                                    transaction.put(objTOTD.getActNum(),depositPenalMap);
                                    }
                                }

                            }
                        }
                    }
                    lstBehave = null;
                }
                depositFlag = false;
            }
            // System.out.println("getDepositPenalAmt()ffffff"+getDepositPenalAmt());
            // System.out.println("getDepositPenalMonthffffff"+getDepositPenalMonth());
            if (depositFlag && getDepositPenalAmt() != null) {
                if (Double.parseDouble(getDepositPenalAmt()) > 0) {
                    depositPenalMap.put("DEPOSIT_PENAL_AMT", getDepositPenalAmt());
                    depositPenalMap.put("DEPOSIT_PENAL_MONTH", getDepositPenalMonth());
                    multiDepositPenalMap.put(objTOTD.getActNum(), depositPenalMap);

                }
            }
        }
        if (backDatedTransDate != null && !backDatedTransDate.equals("")) {
            transaction.put("BACK_DATED_TRANSACTION_DELETION","BACK_DATED_TRANSACTION_DELETION");
        }
        if (multiDepositPenalMap != null && multiDepositPenalMap.size() > 0) {
            transaction.put("MULTIPLE_DEPOSIT_PENAL", multiDepositPenalMap);
        }
//            HashMap prodMap = new HashMap();
//            prodMap.put("PROD_ID",depositProdId);
//            System.out.println("#### getBehavesLikeForDeposit :"+prodMap);
//            List lst = ClientUtil.executeQuery("getBehavesLikeForDeposit", prodMap);
//            if(lst!=null && lst.size()>0)
//                prodMap = (HashMap)lst.get(0);
//            if(prodMap.get("BEHAVES_LIKE").equals("RECURRING")){
//                double penalAmt = CommonUtil.convertObjToDouble(getDepositPenalAmt()).doubleValue();
//                double penalMonth = CommonUtil.convertObjToDouble(getDepositPenalMonth()).doubleValue();
//                if(penalAmt>0){
//                    transaction.put("DEPOSIT_PENAL_AMT",getDepositPenalAmt());
//                    transaction.put("DEPOSIT_PENAL_MONTH",getDepositPenalMonth());
//                }
//            }
//        }
        if (!getCreatingFlexi().equals("") && getCreatingFlexi().equals("FLEXI_LIEN_CREATION")) {
            transaction.put("FLEXI_LIEN_CREATION", getCreatingFlexi());
            transaction.put("FLEXI_AMOUNT", String.valueOf(getFlexiAmount()));
        }
        if (!getCreatingFlexi().equals("") && getCreatingFlexi().equals("FLEXI_LIEN_DELETION")) {
            transaction.put("FLEXI_LIEN_DELETION", getCreatingFlexi());
        }
        //        HashMap hash=null;
        //        hash=checkLoanDebit();
        if (debitLoanType != null && debitLoanType.size() > 0) {
            transaction.putAll(debitLoanType);
        }
        transaction.put(CommonConstants.MODULE, getModule());
        transaction.put(CommonConstants.SCREEN, getScreen());
        if (procChargeHash.size() > 0) {
            transaction.put("PROCCHARGEHASH", procChargeHash);
        }
//        if( ALL_LOAN_AMOUNT !=null && ALL_LOAN_AMOUNT.size()>0){
//           
//            if(isPenalWaiveOff()){
//                ALL_LOAN_AMOUNT.put("PENAL_WAIVE_OFF","Y");
//            }else{
//                ALL_LOAN_AMOUNT.put("PENAL_WAIVE_OFF","N");
//            }
//            transaction.put("ALL_AMOUNT",ALL_LOAN_AMOUNT);
//							
//            }
        
        if (multiple_ALL_LOAN_AMOUNT != null && multiple_ALL_LOAN_AMOUNT.size() > 0) {

            if (isPenalWaiveOff()) {
                multiple_ALL_LOAN_AMOUNT.put("PENAL_WAIVE_OFF", "Y");
                multiple_ALL_LOAN_AMOUNT.put("PENAL_WAIVE_AMT", penalWaiveAmount);
            } else {
                multiple_ALL_LOAN_AMOUNT.put("PENAL_WAIVE_OFF", "N");
            }
             //added by rishad 08/04/2015 for setting waiveamount to all_loan_amount map for waive transaction
            if (isInterestWaiveoff()) {
                multiple_ALL_LOAN_AMOUNT.put("WAIVE_OFF_INTEREST", "Y");
                multiple_ALL_LOAN_AMOUNT.put("INTEREST_WAIVE_AMT", interestWaiveAmount);
            } else {
                multiple_ALL_LOAN_AMOUNT.put("INTEREST_WAIVE_OFF", "N");
            }
            if (isNoticeWaiveoff()) {
                multiple_ALL_LOAN_AMOUNT.put("NOTICE_WAIVE_OFF", "Y");
                multiple_ALL_LOAN_AMOUNT.put("NOTICE_WAIVE_AMT", noticeWaiveAmount);
            } else {
                multiple_ALL_LOAN_AMOUNT.put("NOTICE_WAIVE_OFF", "N");
            }
            if (isPrincipalwaiveoff()) {
                multiple_ALL_LOAN_AMOUNT.put("PRINCIPAL_WAIVE_OFF", "Y");
                multiple_ALL_LOAN_AMOUNT.put("PRINCIPAL_WAIVE_AMT", principalWaiveAmount);
            } else {
                multiple_ALL_LOAN_AMOUNT.put("PRINCIPAL_WAIVE_OFF", "N");
            }
            if (isArcWaiveOff()) {
                multiple_ALL_LOAN_AMOUNT.put("ARC_WAIVE_OFF", "Y");
                multiple_ALL_LOAN_AMOUNT.put("ARC_WAIVE_AMT", arcWaiveAmount);
            } else {
                multiple_ALL_LOAN_AMOUNT.put("ARC_WAIVE_OFF", "N");
            }
            if (isArbitraryWaiveOff()) {
                multiple_ALL_LOAN_AMOUNT.put("ARBITRAY_WAIVE_OFF", "Y");
                multiple_ALL_LOAN_AMOUNT.put("ARBITRAY_WAIVE_AMT", arbitarayWaivwAmount);
            } else {
                multiple_ALL_LOAN_AMOUNT.put("ARBITRAY_WAIVE_OFF", "N");
            }
            if (isDecreeWaiveOff()) {
                multiple_ALL_LOAN_AMOUNT.put("DECREE_WAIVE_OFF", "Y");
                multiple_ALL_LOAN_AMOUNT.put("DECREE_WAIVE_AMT", decreeWaiveAmount);
            } else {
                multiple_ALL_LOAN_AMOUNT.put("DECREE_WAIVE_OFF", "N");
            }

            if (isEpCostWaiveOff()) {
                multiple_ALL_LOAN_AMOUNT.put("EP_WAIVE_OFF", "Y");
                multiple_ALL_LOAN_AMOUNT.put("EP_WAIVE_AMT", epCostWaiveAmount);
            } else {
                multiple_ALL_LOAN_AMOUNT.put("EP_WAIVE_OFF", "N");
            }
            if (isAdvertiseWaiveOff()) {
                multiple_ALL_LOAN_AMOUNT.put("ADVERTISE_WAIVE_OFF", "Y");
                multiple_ALL_LOAN_AMOUNT.put("ADVERTISE_WAIVE_AMT", advertiseWaiveAmount);
            } else {
                multiple_ALL_LOAN_AMOUNT.put("ADVERTISE_WAIVE_OFF", "N");
            }
            if (isInsuranceWaiveOff()) {
                multiple_ALL_LOAN_AMOUNT.put("INSURENCE_WAIVE_OFF", "Y");
                multiple_ALL_LOAN_AMOUNT.put("INSURENCE_WAIVE_AMT", insuranceWaiveAmont);
            } else {
                multiple_ALL_LOAN_AMOUNT.put("INSURENCE_WAIVE_OFF", "N");
            }
            if (isLegalWaiveOff()) {
                multiple_ALL_LOAN_AMOUNT.put("LEGAL_WAIVE_OFF", "Y");
                multiple_ALL_LOAN_AMOUNT.put("LEGAL_WAIVE_AMT", legalWaiveAmount);
            } else {
                multiple_ALL_LOAN_AMOUNT.put("LEGAL_WAIVE_OFF", "N");
            }
            if (isMiscellaneousWaiveOff()) {
                multiple_ALL_LOAN_AMOUNT.put("MISCELLANEOUS_WAIVE_OFF", "Y");
                multiple_ALL_LOAN_AMOUNT.put("MISCELLANEOUS_WAIVE_AMT", miscellaneousWaiveAmount);
            } else {
                multiple_ALL_LOAN_AMOUNT.put("MISCELLANEOUS_WAIVE_OFF", "N");
            }
            if (isPostageWaiveOff()) {
                multiple_ALL_LOAN_AMOUNT.put("POSTAGE_WAIVE_OFF", "Y");
                multiple_ALL_LOAN_AMOUNT.put("POSTAGE_WAIVE_AMT", postageWaiveAmount);
            } else {
                multiple_ALL_LOAN_AMOUNT.put("POSTAGE_WAIVE_OFF", "N");
            }           
            // For overdue interest -- start
            if (isOverDueIntWaiveOff()) {
                multiple_ALL_LOAN_AMOUNT.put("OVERDUEINT_WAIVE_OFF", "Y");
                multiple_ALL_LOAN_AMOUNT.put("OVERDUEINT_WAIVE_AMT", overDueIntWaiveAmount);
            } else {
                multiple_ALL_LOAN_AMOUNT.put("OVERDUEINT_WAIVE_OFF", "N");
            }
          // For overdue interest -- end
            if (isRecoveryWaiveOff()) {
                multiple_ALL_LOAN_AMOUNT.put("RECOVERY_WAIVE_OFF", "Y");
                multiple_ALL_LOAN_AMOUNT.put("RECOVERY_WAIVE_AMT", recoveryWaiveAmount);
            } else {
                multiple_ALL_LOAN_AMOUNT.put("RECOVERY_WAIVE_OFF", "N");
            }
            if (isMeasurementWaiveOff()) {
                multiple_ALL_LOAN_AMOUNT.put("MEASUREMENT_WAIVE_OFF", "Y");
                multiple_ALL_LOAN_AMOUNT.put("MEASUREMENT_WAIVE_AMT", measurementWaiveAmount);
            } else {
                multiple_ALL_LOAN_AMOUNT.put("MEASUREMENT_WAIVE_OFF", "N");
            } 
            
            //Kole field operation expense
            if (isKoleFieldExpenseWaiveOff()) {
                ALL_LOAN_AMOUNT.put("KOLE_FIELD_EXPENSE_WAIVE_OFF", "Y");
                ALL_LOAN_AMOUNT.put("KOLE_FIELD_EXPENSE_WAIVE_AMT", koleFieldExpenseWaiveAmount);
            } else {
                ALL_LOAN_AMOUNT.put("KOLE_FIELD_EXPENSE_WAIVE_OFF", "N");
            }
            
           if (isKoleFieldOperationWaiveOff()) {
                ALL_LOAN_AMOUNT.put("KOLE_FIELD_OPERATION_WAIVE_OFF", "Y");
                ALL_LOAN_AMOUNT.put("KOLE_FIELD_OPERATION_WAIVE_AMT", koleFieldOperationWaiveAmount);
            } else {
                ALL_LOAN_AMOUNT.put("KOLE_FIELD_OPERATION_WAIVE_OFF", "N");
            }
            
            //End
            
            
            transaction.put("MULTIPLE_ALL_AMOUNT", multiple_ALL_LOAN_AMOUNT);
        }
        
        //Added by sreekrishnan
        if (getOtherBankMap()!=null && !getOtherBankMap().equals("") && getOtherBankMap().size()>0) {
            transaction.put("OTHER_BANK_MAP", getOtherBankMap());
        }
   
        if (isRebateInterest()) {
            transaction.put("REBATE_INTEREST", "Y");
        } else {
            transaction.put("REBATE_INTEREST", "N");
        }

        if (getEmiNoInstallment() > 0) {
            transaction.put("EMI_INSTALLMENT", new Long(getEmiNoInstallment()));
        }
        if (parameter != null && parameter.length() > 0) {
            transaction.put("EXCEPTION", "EXCEPTION");
        }
        if (getDepInterestAmt().equals("DEP_INTEREST_AMT")) {
            transaction.put("DEP_INTEREST_AMT", "DEP_INTEREST_AMT");
        }
        if (!getDepLinkBatchId().equals("") && getDepLinkBatchId().equals("DEP_LINK")) {
            transaction.put("LINK_BATCH_ID", getDepAccNO());
        }
        if (valueDateFlag) {
            //System.out.println("here now");
            transaction.put("VALUE_DATE", valueDateMap);
        }
        if (isHoAccount() == true) {
            if (getTransType() == CommonConstants.DEBIT) {
                transaction.put("RESPONDING", "RESPONDING");
            }
            transaction.put("ORG_RESP_DETAILS", getOrgRespList());
        }
         //Added by chithra for service tax
        
        if (servicTaxDetList != null && servicTaxDetList.size() > 0) {
            if (servicTaxDetList.size() > 1) {
                double sertottxamt = 0, edamt = 0, hamt = 0, swachhcess = 0, krishiKalyan = 0, totamt = 0;
                for (int i = 0; i < servicTaxDetList.size(); i++) {
                    //System.out.println("gdvsffvv chithraaaaaaaaa");
                    ServiceTaxDetailsTO objserDetTo = (ServiceTaxDetailsTO) servicTaxDetList.get(i);
                    sertottxamt = sertottxamt + objserDetTo.getServiceTaxAmt();
                    edamt = edamt + objserDetTo.getEducationCess();
                    hamt = hamt + objserDetTo.getHigherCess();
                    swachhcess = swachhcess + objserDetTo.getSwachhCess();
                    krishiKalyan = krishiKalyan + objserDetTo.getKrishiKalyan();
                    totamt = totamt + objserDetTo.getTotalTaxAmt();
                }
               // double totamt = sertottxamt + edamt + hamt;
                ServiceTaxCalculation serviceTaxObj = new ServiceTaxCalculation();
                String amtv = CommonUtil.convertObjToStr(totamt);
                amtv = serviceTaxObj.roundOffAmt(amtv,"NEAREST_VALUE");

                // Commented the below lines of code by nithya on 07-11-2018 for KD 319 - Transfer screen : GST transaction is not correct if two sets of GST [ for GL and for Loan ]
                /*serviceTax_Map.put("SERVICE_TAX", sertottxamt);
                serviceTax_Map.put("TOT_TAX_AMT", amtv);
                serviceTax_Map.put("EDUCATION_CESS", edamt);
                serviceTax_Map.put("HIGHER_EDU_CESS", hamt);
                serviceTax_Map.put("SWACHH_CESS", swachhcess);
                serviceTax_Map.put("KRISHIKALYAN_CESS", krishiKalyan);*/
                // End
                ArrayList temp = new ArrayList();
                temp.add(setServiceTaxDetails());
                servicTaxDetList = temp;
            }
            transaction.put("SERVICE_TAX_DETAILS", servicTaxDetList);
        }
        if (glserviceTax_Map!=null && glserviceTax_Map.containsKey("GL_TRANS")) {
            transaction.put("SERVICE_TAX_MAP", glserviceTax_Map);
        }

        //End.....
        //transaction.put("DEBIT_TRANSFER_TRANSACTION", getProdDebitTransfer());
        //transaction.put("CREDIT_TRANSFER_TRANSACTION", getProdCreditTransfer());
//        transaction.put("ACT_CLOSING_MIN_BAL_CHECK_CASH_TRANSFER","ACT_CLOSING_MIN_BAL_CHECK_CASH_TRANSFER");
        //System.out.println("transaction             ::"+transaction);
        return transaction;
    }

    public HashMap checkLoanDebit(String instrumentType) {
        loanDebitType = new HashMap();
        if (authorizeMap != null) {
            ArrayList toList = new ArrayList();
            toList = (ArrayList) authorizeMap.get("AUTHORIZEDATA");
            for (int i = 0; i < toList.size(); i++) {
                TxTransferTO obj = (TxTransferTO) toList.get(i);
                if (obj.getProdType().equals("TL") && obj.getTransType().equals("DEBIT") || obj.getProdType().equals("ATL") && obj.getTransType().equals("DEBIT") || (obj.getProdType().equals("AD") && obj.getTransType().equals("DEBIT")
                        || obj.getProdType().equals("AAD") && obj.getTransType().equals("DEBIT"))) {  //&& obj.getInstType().equals("VOUCHER")
                    loanDebitType.put("DEBIT_LOAN_TYPE", obj.getAuthorizeRemarks());
                }
            }
        } else {
            int res = 0;
            do {
                loanDebitType = new HashMap();
                if (getProductTypeValue().equals("TL") && getTransType().equals("DEBIT") || getProductTypeValue().equals("ATL") && getTransType().equals("DEBIT") || (getProductTypeValue().equals("AAD") && getTransType().equals("DEBIT") && instrumentType.equals("VOUCHER"))
                        || (getProductTypeValue().equals("AD") && getTransType().equals("DEBIT") && instrumentType.equals("VOUCHER"))) {
                    String[] debitType = {"DebitPrinciple", "Debit Interest", "Debit_Penal_Int", "Other_Charges"};
                    String var = "";
                    if (getLinkMap() != null && getLinkMap().containsKey("AS_CUSTOMER_COMES") && getLinkMap().get("AS_CUSTOMER_COMES").equals("Y")) {
                        var = "DebitPrinciple";
                    } else {
                        var = (String) COptionPane.showInputDialog(null, "Select Transaction Type", "Transaction type", COptionPane.QUESTION_MESSAGE, null, debitType, "");
                    }
                    //System.out.println("var@#####" + var);
                    res = 0;
                    if (var.equals("Debit Interest")) {
                        loanDebitType.put("DEBIT_LOAN_TYPE", "DI");
                    }
                    if (var.equals("DebitPrinciple")) {
                        loanDebitType.put("DEBIT_LOAN_TYPE", "DP");
                    }
                    if (var.equals("Debit_Penal_Int")) {
                        loanDebitType.put("DEBIT_LOAN_TYPE", "DPI");
                    }
                    if (var.equals("Other_Charges")) {
                        loanDebitType.put("DEBIT_LOAN_TYPE", "OTHERCHARGES");
                    }
                    if (!var.equals("DebitPrinciple")) {
                        double clearbalance = CommonUtil.convertObjToDouble(ALL_LOAN_AMOUNT.get("CLEAR_BALANCE")).doubleValue();
                        double availableBalance = CommonUtil.convertObjToDouble(ALL_LOAN_AMOUNT.get("LOAN_BALANCE_PRINCIPAL")).doubleValue();

                        if (clearbalance == 0.0) {
                            res = ClientUtil.confirmationAlert("Caution : Disbursement Not Yet Done \n" + "Whether to Proceed");
                        }
                    }
                } else if (getProductTypeValue().equals("AD") && getTransType().equals("DEBIT") && (!instrumentType.equals("VOUCHER"))) {
                    loanDebitType.put("DEBIT_LOAN_TYPE", "DP");
                }
            } while (res != 0);
        }
        return loanDebitType;
    }

    private void setStatusForTOs() {
        TxTransferTO objTO;
        this.setTransStatus(CommonConstants.STATUS_DELETED);
        int size = this.transferTOs.size();
        for (int i = 0; i < size; i++) {
            objTO = (TxTransferTO) transferTOs.get(i);
            objTO = this.setTOStatus(objTO);
            transferTOs.set(i, objTO);
        }
        objTO = null;
    }

    private void setReconcileStatusForTOs() {
        ReconciliationTO objTO;
        this.setTransStatus(CommonConstants.STATUS_DELETED);
        int size = this.reconciliationList.size();
        for (int i = 0; i < size; i++) {
            objTO = (ReconciliationTO) reconciliationList.get(i);
            objTO = this.setReconcileTOStatus(objTO);
            reconciliationList.set(i, objTO);
        }
        objTO = null;
    }

    private TxTransferTO getTxTransferTO() {
        TxTransferTO newTxTransferTO = new TxTransferTO();
        //Added by sreekrishnan for trans mod type
        if (!getProdDebitTransfer().equals("") && getProdDebitTransfer() != null && getProdDebitTransfer().length()>0)//bb1
        {
            newTxTransferTO.setTransModType(getProdDebitTransfer());
            setProdDebitTransfer("");
        } 
        if (!getProdCreditTransfer().equals("") && getProdCreditTransfer() != null && getProdCreditTransfer().length()>0 )//bb1
        {
            newTxTransferTO.setTransModType(getProdCreditTransfer());
            setProdCreditTransfer("");
        }
        newTxTransferTO.setBatchId(getBatchId());
        newTxTransferTO.setTransId(getTransId());
        newTxTransferTO.setAcHdId(getAccountHeadId());

        // If AcHdId is not passed... retrive from the database based on Product Type and Prod ID
        if ((getAccountHeadId() == null || getAccountHeadId().trim().equals(""))
                && getAccountHeadId() != null && !getProductId().trim().equals("") && !getMainProductTypeValue().equals("GL")) {
            String qry = "getAccountHead" + getMainProductTypeValue();
            HashMap where = new HashMap();
            where.put(CommonConstants.MAP_WHERE, getProductId());
            List lst = ClientUtil.executeQuery(qry, where);
            if (lst != null && lst.size() > 0) {
                newTxTransferTO.setAcHdId((String) lst.get(0));
            }
        }

        newTxTransferTO.setActNum(getAccountNo());
        newTxTransferTO.setInpAmount(CommonUtil.convertObjToDouble(getTransferAmount()));
        newTxTransferTO.setInpCurr(getCurrencyType());
        newTxTransferTO.setAmount(CommonUtil.convertObjToDouble(getAmount()));
        //        newTxTransferTO.setTransDt(DateUtil.getDateMMDDYYYY(getTransDate()));
        Date dtDate = DateUtil.getDateMMDDYYYY(getTransDate());
        if (dtDate != null) {
            Date Dt = (Date) curDate.clone();
            Dt.setDate(dtDate.getDate());
            Dt.setMonth(dtDate.getMonth());
            Dt.setYear(dtDate.getYear());
            newTxTransferTO.setTransDt(Dt);
        } else {
            newTxTransferTO.setTransDt(DateUtil.getDateMMDDYYYY(getTransDate()));
        }
        newTxTransferTO.setTransType(getTransType());
        newTxTransferTO.setInstType(getInstType());
        newTxTransferTO.setInstrumentNo1(getInstrumentNo1());
        newTxTransferTO.setInstrumentNo2(getInstrumentNo2());
        //        newTxTransferTO.setInstDt(DateUtil.getDateMMDDYYYY(getInstrumentDate()));
        Date ttDate = DateUtil.getDateMMDDYYYY(getInstrumentDate());
        if (ttDate != null) {
            Date Tt = (Date) curDate.clone();
            Tt.setDate(ttDate.getDate());
            Tt.setMonth(ttDate.getMonth());
            Tt.setYear(ttDate.getYear());
            newTxTransferTO.setInstDt(Tt);
        } else {
            newTxTransferTO.setInstDt(DateUtil.getDateMMDDYYYY(getInstrumentDate()));
        }
        newTxTransferTO.setProdType(getMainProductTypeValue());
        newTxTransferTO.setParticulars(getParticulars());
        newTxTransferTO.setNarration(getNarration());
        newTxTransferTO.setProdId(getProductId());
        newTxTransferTO.setTransMode(CommonConstants.TX_TRANSFER);
        System.out.print("ggacc no ====" + getAccNumGl());
        if (getMainProductTypeValue().equals("GL") && (getAccNumGl() != null && !getAccNumGl().equalsIgnoreCase("")))//bb1
        {
            newTxTransferTO.setLinkBatchId(getAccNumGl());
            newTxTransferTO.setTransModType(getAccNumGlProdType());//Added y nithya on 17-Jul-2025 for KD-4108
        } else {
            newTxTransferTO.setLinkBatchId(getClosedAccNo());
        }
        if (getMainProductTypeValue().equals("TL")
                && getTransType().equals("DEBIT") || getMainProductTypeValue().equals("ATL")
                && getTransType().equals("DEBIT") || getMainProductTypeValue().equals("AD")
                && getTransType().equals("DEBIT") || getMainProductTypeValue().equals("AAD")
                && getTransType().equals("DEBIT")) {
            newTxTransferTO.setAuthorizeRemarks(CommonUtil.convertObjToStr(loanDebitType.get("DEBIT_LOAN_TYPE")));
            //System.out.println("nextTransferto###" + newTxTransferTO);
        }
//        if(getMainProductTypeValue().equals("TD") && getTransType().equals("CREDIT")){
//            depositPenalFlag = true;
//            depositProdId = getProductId();
//            double totAmt = 0.0;
//            double depAmt = 0.0;
//            double creditAmt = 0.0;
//            double txtAmt = CommonUtil.convertObjToDouble(getAmount()).doubleValue();
//            String AcNo = CommonUtil.convertObjToStr(getAccountNo());
//            if (AcNo.lastIndexOf("_")!=-1){
//                AcNo = AcNo.substring(0,AcNo.lastIndexOf("_"));
//            }
//            HashMap renewMap = new HashMap();
//            renewMap.put("DEPOSIT_NO",AcNo);
//            List lst = ClientUtil.executeQuery("getRenewalNewDetails", renewMap);
//            if(lst!=null && lst.size()>0){            
//                renewMap = (HashMap)lst.get(0);
//                totAmt = CommonUtil.convertObjToDouble(renewMap.get("TOTAL_BALANCE")).doubleValue();
//                depAmt = CommonUtil.convertObjToDouble(renewMap.get("DEPOSIT_AMT")).doubleValue();
//                if(getOperation() == ClientConstants.ACTIONTYPE_NEW)
//                    creditAmt = CommonUtil.convertObjToDouble(renewMap.get("SHADOW_CREDIT")).doubleValue();
//                else
//                    creditAmt = 0.0;
//                setTransDepositAmt(String.valueOf(depAmt));
//            }
//            renewMap = new HashMap();
//            renewMap.put("DEPOSIT_NO",AcNo);
//            lst = ClientUtil.executeQuery("getDepNoFromOldDeposit", renewMap);
//            if(lst!=null && lst.size()>0){
//                renewMap = (HashMap)lst.get(0);
//                String accNo = CommonUtil.convertObjToStr(renewMap.get("RENEWAL_FROM_DEPOSIT"));
//                if(accNo!=null && accNo.length()>0){
//                    setTransDepositAmt(String.valueOf(depAmt));
//                    double balance = depAmt - (totAmt+creditAmt);
//                    if(txtAmt != balance ){
//                        ClientUtil.showAlertWindow("Balance Amount is Not Matching...\n"+
//                        "Credit to this Deposit A/c:"+balance);
//                        if(balance == 0)
//                            newTxTransferTO.setAmount(new Double(balance));
//                        else
//                            newTxTransferTO.setAmount(new Double(txtAmt));
//                    }
//                }
//                renewMap = null;
//            }
//        }
        if (getMainProductTypeValue().equals("TD") && getTransType().equals("DEBIT")) {
            double totAmt = CommonUtil.convertObjToDouble(getRenewDepAmt()).doubleValue();
            double depAmt = CommonUtil.convertObjToDouble(getTransDepositAmt()).doubleValue();
            double txtAmt = CommonUtil.convertObjToDouble(getAmount()).doubleValue();
            setTransDepositAmt(String.valueOf(depAmt));
            if (txtAmt > totAmt) {
                ClientUtil.showAlertWindow("Amount is exceeding for that Available Balance ...");
                newTxTransferTO.setAmount(new Double(0.0));
            }
        }
        if (renewalIntFlag == true)//this is atthe time renewal any interest amount is withdrawing only it will work
        {
            newTxTransferTO.setInstrumentNo1("INTEREST_AMT");
        }
        if (depositRenewalFlag == true)//this is atthe time renewal any deposit & interest amount is withdrawing only it will work
        {
            newTxTransferTO.setInstrumentNo2("DEPOSIT_RENEWAL");//any renewal is going through means that authorization go through renewal screen itself,transfer screen should not come.
        }
        /*if(getMainProductTypeValue().equals("TD") && getTransType().equals("CREDIT")){ // Commented the code by nithya on 08-07-2020 for KD-2066
             HashMap prodMap = new HashMap();
                prodMap.put("PROD_ID", newTxTransferTO.getProdId());
                List lst = ClientUtil.executeQuery("getBehavesLikeForDeposit", prodMap);
                if (lst != null && lst.size() > 0) {
                    prodMap = (HashMap) lst.get(0);
                    if (prodMap != null && prodMap.get("BEHAVES_LIKE").equals("RECURRING")) {
                        newTxTransferTO.setLinkBatchId(newTxTransferTO.getActNum());
                    }
                }
        }*/
        if(getMainProductTypeValue().equals("TD")){ // Added by nithya on 08-07-2020 for KD-2066
             HashMap prodMap = new HashMap();
                prodMap.put("PROD_ID", newTxTransferTO.getProdId());
                List lst = ClientUtil.executeQuery("getBehavesLikeForDeposit", prodMap);
                if (lst != null && lst.size() > 0) {
                    prodMap = (HashMap) lst.get(0);
                    if (prodMap != null && (prodMap.get("BEHAVES_LIKE").equals("RECURRING") || prodMap.get("BEHAVES_LIKE").equals("DAILY"))) {
                        newTxTransferTO.setLinkBatchId(newTxTransferTO.getActNum());
                    }
                }
        }
        newTxTransferTO.setScreenName(getScreen());
        return newTxTransferTO;
    }

    //for bulk transaction purpose we created form excel sheet to screen
    private boolean getTxTransferTOBulkTransaction() {
        TxTransferTO newTxTransferTO = new TxTransferTO();
        StringBuffer act_num = new StringBuffer();
        StringBuffer act_num1 = new StringBuffer();
        StringBuffer glBuffer = new StringBuffer();
        ArrayList allTransactionTOs = new ArrayList();
        ArrayList singleTransList = null;
        HashMap actMap = new HashMap();
        HashMap accountNoMap = new HashMap();
        HashMap removeGLMap = new HashMap();
        HashMap glMap = new HashMap();

        try {
            javax.swing.JFileChooser fc = new javax.swing.JFileChooser();
            int result = fc.showOpenDialog(null);
            if (result == fc.APPROVE_OPTION) {
                java.io.File selectedFile = fc.getSelectedFile();
                String name = selectedFile.getName();
                if (name.substring(name.indexOf(".") + 1, name.length()).equalsIgnoreCase("xls")) {
                    isBulkUploadTxtFile = false;
                    java.io.FileInputStream inpuStream = new java.io.FileInputStream(selectedFile);
                    HSSFWorkbook workbook = new HSSFWorkbook(inpuStream);//new FileInputStream(fileToBeRead));
                    HSSFSheet sheet = workbook.getSheetAt(0);
                    int rows = sheet.getPhysicalNumberOfRows();//sheet.getLastRowNum();//sheet.getPhysicalNumberOfRows();
                    //System.out.println("last row number ##" + sheet.getLastRowNum() + "sheet.getPhysicalNumberOfRows()" + sheet.getPhysicalNumberOfRows());
                    if (valueDateMap == null) {
                        valueDateMap = new HashMap();
                    }
                    for (int i = 1; i < rows; i++) {
                        HSSFRow row = sheet.getRow(i);
                        newTxTransferTO = new TxTransferTO();
                        HSSFCell cell = null;
                        cell = row.getCell((short) 0);
                        if (CommonUtil.convertObjToStr(getCellValue(cell)).length() == 0) {
                            continue;
                        }
                        newTxTransferTO = (TxTransferTO) setBulkTransaction(row);
                        if (CommonUtil.convertObjToStr(newTxTransferTO.getProdType()).equals("GL")) {
                            removeGLMap.put(newTxTransferTO.getAcHdId(), newTxTransferTO.getAcHdId());
                            if (glBuffer.length() > 0) {
                                glBuffer.append(",");
                            }
                            glBuffer.append("'" + newTxTransferTO.getAcHdId() + "'");

                        } else if (CommonUtil.convertObjToStr(newTxTransferTO.getProdType()).equals("OA")
                                || CommonUtil.convertObjToStr(newTxTransferTO.getProdType()).equals("AD")
                                || CommonUtil.convertObjToStr(newTxTransferTO.getProdType()).equals("TL")
                                || CommonUtil.convertObjToStr(newTxTransferTO.getProdType()).equals("SA")
                                || CommonUtil.convertObjToStr(newTxTransferTO.getProdType()).equals("AB")) {//if Condition Added By Revathi.L
                            accountNoMap.put(newTxTransferTO.getActNum(), newTxTransferTO.getActNum());
                        }
                        newTxTransferTO.setScreenName(getScreen());
                        singleTransList = new ArrayList();
                        singleTransList.add(newTxTransferTO);
                        allTransactionTOs.add(singleTransList);
                        if (CommonUtil.convertObjToStr(newTxTransferTO.getProdType()).equals("OA")
                                || CommonUtil.convertObjToStr(newTxTransferTO.getProdType()).equals("AD")
                                || CommonUtil.convertObjToStr(newTxTransferTO.getProdType()).equals("TL")
                                || CommonUtil.convertObjToStr(newTxTransferTO.getProdType()).equals("SA")
                                || CommonUtil.convertObjToStr(newTxTransferTO.getProdType()).equals("AB")) {
                            //this portion need to rewrite to dynamic .now possible upto 1998
                            if (i < 999) {
                                if (act_num.length() > 0) {
                                    act_num.append(",");
                                }
                                act_num.append("'" + newTxTransferTO.getActNum() + "'");
                            } else {
                                if (act_num1.length() > 0) {
                                    act_num1.append(",");
                                }
                                act_num1.append("'" + newTxTransferTO.getActNum() + "'");
                            }
                        }
                    }
                    if (rows < 999) {
                        act_num1.append("''");
                    }
                    actMap.put("ACT_NUM", act_num);
                    actMap.put("ACT_NUM1", act_num1);
                    if (glBuffer != null && glBuffer.length() > 0) {
                        glMap.put("ACT_NUM", glBuffer);
                    }
                    if (verifiyOperativeAcctNo(actMap, allTransactionTOs, accountNoMap, glMap, removeGLMap)) {
                        return true;
                    }
                    inpuStream.close();
                } else if (name.substring(name.indexOf(".") + 1, name.length()).equalsIgnoreCase("txt")) { //Txt File Upload Developed By Suresh R 28-Jul-2017
                    isBulkUploadTxtFile = true;
                    invalidBulkUploadTxtFile = false;
                    //System.out.println("######### TXT File Format :");
                    FileInputStream fis = new FileInputStream(selectedFile);
                    BufferedInputStream bis = new BufferedInputStream(fis);
                    DataInputStream dis = new DataInputStream(bis);
                    String line = "";
                    String qualifeir = "";
                    List customerList = new ArrayList();
                    List totCustomerList = new ArrayList();
                    if (valueDateMap == null) {
                        valueDateMap = new HashMap();
                    }
                    int x = 0;
                    while (dis.available() > 0 && (line = dis.readLine()).length() > 0) {
                        //System.out.println("#### line        : " + line);
                        if (x == 0) {
                            x = x + 1;
                        } else {
                            String[] parts = line.split("\\|");
                            customerList = new ArrayList();
                            for (String s : parts) {
                                customerList.add(s);
                            }
                            if (customerList != null && customerList.size() > 0) {
                                totCustomerList.add(customerList);
                            }
                        }
                    }
                    //System.out.println("######### totCustomerList : " + totCustomerList);
                    fis.close();
                    if (totCustomerList != null && totCustomerList.size() > 0) {
                        Double amount = 0.0;
                        List singleLst = new ArrayList();
                        for (int i = 0; i < totCustomerList.size(); i++) {
                            singleLst = (ArrayList) totCustomerList.get(i);
                            amount = CommonUtil.convertObjToDouble(singleLst.get(6));
                            if (amount > 0) {
                                newTxTransferTO = (TxTransferTO) setBulkTransactionAD(singleLst);  //SET TRANSACTION TO
                                if(invalidBulkUploadTxtFile){
                                    return true;
                                }
                                if (CommonUtil.convertObjToStr(newTxTransferTO.getProdType()).equals("GL")) {
                                    removeGLMap.put(newTxTransferTO.getAcHdId(), newTxTransferTO.getAcHdId());
                                    if (glBuffer.length() > 0) {
                                        glBuffer.append(",");
                                    }
                                    glBuffer.append("'" + newTxTransferTO.getAcHdId() + "'");
                                } else if (CommonUtil.convertObjToStr(newTxTransferTO.getProdType()).equals("OA")
                                        || CommonUtil.convertObjToStr(newTxTransferTO.getProdType()).equals("AD")
                                        || CommonUtil.convertObjToStr(newTxTransferTO.getProdType()).equals("TL")
                                        || CommonUtil.convertObjToStr(newTxTransferTO.getProdType()).equals("SA")) {
                                    accountNoMap.put(newTxTransferTO.getActNum(), newTxTransferTO.getActNum());
                                }
                                newTxTransferTO.setScreenName(getScreen());
                                singleTransList = new ArrayList();
                                singleTransList.add(newTxTransferTO);
                                allTransactionTOs.add(singleTransList);
                                if (CommonUtil.convertObjToStr(newTxTransferTO.getProdType()).equals("OA")
                                        || CommonUtil.convertObjToStr(newTxTransferTO.getProdType()).equals("AD")
                                        || CommonUtil.convertObjToStr(newTxTransferTO.getProdType()).equals("TL")
                                        || CommonUtil.convertObjToStr(newTxTransferTO.getProdType()).equals("SA")) {
                                    if (i < 999) {
                                        if (act_num.length() > 0) {
                                            act_num.append(",");
                                        }
                                        act_num.append("'" + newTxTransferTO.getActNum() + "'");
                                    } else {
                                        if (act_num1.length() > 0) {
                                            act_num1.append(",");
                                        }
                                        act_num1.append("'" + newTxTransferTO.getActNum() + "'");
                                    }
                                }
                            }
                        }
                        if (totCustomerList.size() < 999) {
                            act_num1.append("''");
                        }
                        actMap.put("ACT_NUM", act_num);
                        actMap.put("ACT_NUM1", act_num1);
                        if (glBuffer != null && glBuffer.length() > 0) {
                            glMap.put("ACT_NUM", glBuffer);
                        }
                        if (verifiyOperativeAcctNo(actMap, allTransactionTOs, accountNoMap, glMap, removeGLMap)) {
                            return true;
                        }
                    }
                } else {
                    ClientUtil.displayAlert("Selected file Should be in Excel or Txt Format");
                    return true;
                }
            } else {
                return false;
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    private boolean verifiyOperativeAcctNo(HashMap actMap, ArrayList allTransactionTOs, HashMap accountNoMap, HashMap glMap, HashMap removeGLMap) {
//         HashMap resultMap =new HashMap();
        StringBuffer act_num = new StringBuffer();
        StringBuffer act_numib = new StringBuffer();
        ArrayList tempList = new ArrayList();
        ArrayList objList = null;
        ArrayList currList = null;
        HashMap accMap = new HashMap();
        HashMap singleMap = new HashMap();
        HashMap gldataMap = new HashMap();
        double amount = 0;
        double tot = 0.0;
        HashMap ibMap=new HashMap();
        int x1=1;
        // HashMap whereMap=new HashMap();
        List glList = null;
        //actMap.put(CommonConstants.MAP_WHERE, actMap.get("ACT_NUM"));//Commented By Revathi.L In actMap not take ACT_NUM1 Key values
        //actMap.put(CommonConstants.MAP_WHERE, actMap);
        if (glMap != null && glMap.size() > 0) {
            gldataMap.put("BRANCH_CODE", TrueTransactMain.BRANCH_ID);
            gldataMap.put("BULK_TRANS", "BULK_TRANS");
            gldataMap.put(CommonConstants.MAP_WHERE, glMap.get("ACT_NUM"));
            glList = ClientUtil.executeQuery("Transfer.getSelectAcctHeadDBBulk", gldataMap);
        }
        objList = new ArrayList();
        HashMap actAmountMap = new HashMap();
        double debitAmt = 0.0;
        double totAmt = 0.0;
        for (int i = 0; i < allTransactionTOs.size(); i++) {//Added By Revathi.L 27/07/2017 Multiple Debit in Same Account Number Sum Debit Amount for Available balance checking
            objList = new ArrayList();
            objList = (ArrayList) allTransactionTOs.get(i);
            TxTransferTO obj = (TxTransferTO) objList.get(0);
            if (obj.getTransType().equals(CommonConstants.DEBIT)) {
                if (actAmountMap.containsKey(obj.getActNum())) {
                    debitAmt = CommonUtil.convertObjToDouble(actAmountMap.get(obj.getActNum()));
                    totAmt = debitAmt + obj.getAmount();
                    actAmountMap.put(obj.getActNum(), totAmt);
                } else {
                    actAmountMap.put(obj.getActNum(), obj.getAmount());
                }
            }
        }
        //List lst = ClientUtil.executeQuery("getVerifiedActNumber", actMap);//Commented By Revathi
        List lst = ClientUtil.executeQuery("getVerifiedAllActNumber", actMap); //Added By Revathi
        for (int j = 0; j < allTransactionTOs.size(); j++) {
            if (lst != null && lst.size() > 0) {
                for (int i = 0; i < lst.size(); i++) {
                    singleMap = (HashMap) lst.get(i);
                    String operativeActNum = CommonUtil.convertObjToStr(singleMap.get("ACT_NUM"));
                    String achd = CommonUtil.convertObjToStr(singleMap.get("AC_HD_ID"));
                    String prodId = CommonUtil.convertObjToStr(singleMap.get("PROD_ID"));
                    String branchCode = CommonUtil.convertObjToStr(singleMap.get("BRANCH_CODE"));
                    double avilableBalance=CommonUtil.convertObjToDouble(singleMap.get("AVAILABLE_BALANCE"));
                    boolean balanceCheckRequired = true;
                    boolean otherbankAct = false;
                    objList = new ArrayList();
                    objList = (ArrayList) allTransactionTOs.get(j);
                    TxTransferTO obj = (TxTransferTO) objList.get(0);                    
                    if (obj.getActNum() != null && obj.getActNum().equals(operativeActNum)) {
                        obj.setAcHdId(achd);
                        obj.setBranchId(branchCode);
                        obj.setProdId(prodId);
                        obj.setProdType("OA");
                        if (CommonUtil.convertObjToStr(singleMap.get("PROD_TYPE")).equals("OA")) {// Added By Revathi
                            obj.setProdType("OA");
                        } else if (CommonUtil.convertObjToStr(singleMap.get("PROD_TYPE")).equals("AD")) { 
                            obj.setProdType("AD");
                        } else if (CommonUtil.convertObjToStr(singleMap.get("PROD_TYPE")).equals("SA")) { 
                            obj.setProdType("SA");
                            balanceCheckRequired = checkSuspanceActBalCheck(obj.getActNum());
                        } else if (CommonUtil.convertObjToStr(singleMap.get("PROD_TYPE")).equals("AB")) { 
                            obj.setProdType("AB");
                            otherbankAct = true;
                        } else {
                            obj.setProdType("TL");
                        }
                        accountNoMap.remove(obj.getActNum());
                        objList.set(0, obj);
                        tempList.add(objList);
//                        lst.remove(i); FOR DUPLICATE REOCORD
                    } 
                    //balanceCheckRequired & otherbankAct condition added by nithya for suspense account validation
                    if (obj.getActNum() != null && obj.getActNum().equals(operativeActNum) && obj.getTransType().equals(CommonConstants.DEBIT) && CommonUtil.convertObjToDouble(actAmountMap.get(operativeActNum)) > avilableBalance && balanceCheckRequired && !otherbankAct) {//if Condition Changed By Revathi.L
                        if (accMap.containsKey(operativeActNum)) {
                        } else {
                            accMap.put(operativeActNum, operativeActNum);
                            if (act_numib.length() == 0) {
                                x1++;
                                act_numib.append(operativeActNum);
                            } else {
                                x1++;
                                act_numib.append("," + operativeActNum);
                            }
                            if (x1 % 5 == 0) {
                                act_numib.append("\n");
                            }
                            System.out.println("act_numib : " + act_numib);
                            System.out.println("i " + i);
                            System.out.println("j " + j);
                        }
                    }
                }
            }
            //gl transaction
            if (glList != null && glList.size() > 0) {
                for (int i = 0; i < glList.size(); i++) {
                    singleMap = (HashMap) glList.get(i);
//                     String operativeActNum=CommonUtil.convertObjToStr(singleMap.get("ACT_NUM"));
                    String achd = CommonUtil.convertObjToStr(singleMap.get("A/C HEAD"));
//                     String prodId=CommonUtil.convertObjToStr(singleMap.get("PROD_ID"));
                    String branchCode = CommonUtil.convertObjToStr(TrueTransactMain.BRANCH_ID);
                    objList = new ArrayList();
                    objList = (ArrayList) allTransactionTOs.get(j);
                    TxTransferTO obj = (TxTransferTO) objList.get(0);
                    if (obj.getAcHdId().equals(achd)) {
                        obj.setAcHdId(achd);
                        obj.setBranchId(branchCode);
                        obj.setProdId("");
                        obj.setProdType("GL");
                        removeGLMap.remove(achd);
                        objList.set(0, obj);
                        tempList.add(objList);
//                        lst.remove(i); FOR DUPLICATE REOCORD
                    }
                }
            }
        }
        if (accountNoMap != null && accountNoMap.size() > 0) {
            java.util.Set keySet = (java.util.Set) accountNoMap.keySet();
            Object[] objKeySet = (Object[]) keySet.toArray();
            int x = 0;
            for (int i = 0; i < keySet.size(); i++) {
                act_num.append("" + accountNoMap.get(objKeySet[i]) + ",");
                x++;
                if (x == 5) {
                    act_num.append("\n");
                    x = 0;
                }
            }
        }
        if (removeGLMap != null && removeGLMap.size() > 0) {
            java.util.Set keySet = (java.util.Set) removeGLMap.keySet();
            Object[] objKeySet = (Object[]) keySet.toArray();
            int x = 0;
            for (int i = 0; i < keySet.size(); i++) {
                act_num.append("" + glMap.get(objKeySet[i]) + ",");
                x++;
                if (x == 5) {
                    act_num.append("\n");
                    x = 0;
                }
            }
        }
        if (act_num.length()>0) {
            ClientUtil.showMessageWindow("Please check the Excel/Txt sheet data. Following A/C Nos are Wrong " + act_num.toString());
            return true;
        }
        if(act_numib.length()>0)
        {
         ClientUtil.showMessageWindow("Please check the Excel/Txt sheet data. Following A/C Nos having Insufficient Balance " + act_numib.toString());
            return true;
        }
        if (tempList != null && tempList.size() > 0) {
            for (int i = 0; i < tempList.size(); i++) {
                objList = new ArrayList();
                objList = (ArrayList) tempList.get(i);
                TxTransferTO obj = (TxTransferTO) objList.get(0);
                transferTOs.add(obj);
                valueDateMap.put(String.valueOf(transferTOs.size() - 1), getValueDate());
                ArrayList irRow = this.setRow(obj);
                tbmTransfer.insertRow(tbmTransfer.getRowCount(), irRow);
            }
        }

        return false;
    }
    
    private TxTransferTO setBulkTransaction(HSSFRow row) {
        TxTransferTO obj = new TxTransferTO();
        HSSFCell cell = null;
        cell = row.getCell((short) 0);
        String prodType = CommonUtil.convertObjToStr(getCellValue(cell));
        obj.setProdType(CommonUtil.convertObjToStr(getCellValue(cell)));
        cell = row.getCell((short) 1);
        if (prodType.equals("OA") ||prodType.equals("AD")||prodType.equals("TL") || prodType.equals("SA") || prodType.equals("AB")) {//Added By Revathi.L
            obj.setActNum(CommonUtil.convertObjToStr(getCellValue(cell)));
        } else if (prodType.equals("GL")) {
            obj.setAcHdId(CommonUtil.convertObjToStr(getCellValue(cell)));
        }
        cell = row.getCell((short) 2);
        obj.setTransType(CommonUtil.convertObjToStr(getCellValue(cell)));

        cell = row.getCell((short) 3);
        obj.setAmount(CommonUtil.convertObjToDouble(getCellValue(cell)));
        obj.setInpAmount(CommonUtil.convertObjToDouble(getCellValue(cell)));
//         obj.setAmount(CommonUtil.convertObjToDouble(new Double((row.getCell((short)2).getNumericCellValue()))));
//         obj.setInpAmount(CommonUtil.convertObjToDouble(new Double(row.getCell((short)2).getNumericCellValue())));
        //         obj.setBranchId(String.valueOf((row.getCell((short)6).getStringCellValue())));
        cell = row.getCell((short) 4);
        obj.setInstType(getCellValue(cell));
        cell = row.getCell((short) 5);
        obj.setInstrumentNo1(getCellValue(cell));
        cell = row.getCell((short) 6);
        obj.setInstrumentNo2(getCellValue(cell));
        if (prodType.equals("AD") && CommonUtil.convertObjToStr(obj.getTransType()).equals("DEBIT")) {//Added By Revathi.L
            obj.setAuthorizeRemarks("DP");
        }

//         obj.setInstType(CommonUtil.convertObjToStr(row.getCell((short)3).getStringCellValue()));
//         if(CommonUtil.convertObjToStr(row.getCell((short)4)).length()>0)
//         {
//            obj.setInstrumentNo1(CommonUtil.convertObjToStr(row.getCell((short)4).getStringCellValue()));
//         }else{
//             obj.setInstrumentNo1("");
//         }
//         if(CommonUtil.convertObjToStr(row.getCell((short)5)).length()>0)
//         {
//            obj.setInstrumentNo2(CommonUtil.convertObjToStr(new Double (row.getCell((short)5).getNumericCellValue())));
//         }else{
//             obj.setInstrumentNo2("");
//         }
//         obj.setInstrumentNo2(CommonUtil.convertObjToStr(row.getCell((short)5).getStringCellValue()));
        cell = row.getCell((short) 7);
        double dv = CommonUtil.convertObjToDouble(getCellValue(cell)).doubleValue();
        if (HSSFDateUtil.isValidExcelDate(dv)) {
            Date date = HSSFDateUtil.getJavaDate(dv);
            obj.setInstDt(date);
        }
//         obj.setInstDt(new Date(row.getCell((short)6).getDateCellValue()));


        cell = row.getCell((short) 8);
        obj.setParticulars(getCellValue(cell));
//         obj.setParticulars(CommonUtil.convertObjToStr((row.getCell((short)7).getStringCellValue())));
        obj.setBatchId("-");
        obj.setTransId("-");
        obj.setTransDt(curDate);
        obj.setInitiatedBranch(ProxyParameters.BRANCH_ID);
        obj.setInitChannType(TrueTransactMain.BRANCH_ID);
        obj.setInitTransId(TrueTransactMain.USER_ID);
        obj.setInpCurr("INR");
        obj.setInstDt(curDate);

        obj.setTransMode("TRANSFER");
        obj.setLinkBatchId(obj.getActNum());
        obj.setStatusDt(curDate);
        obj.setStatusBy(TrueTransactMain.USER_ID);
        obj.setStatus("CREATED");
        obj.setTransModType(obj.getProdType());
        obj.setScreenName(getScreen());
        return obj;

    }
    
    //This method added By Suresh R     28-Jul-2017
    private TxTransferTO setBulkTransactionAD(List singleLst) {
        String actNum = " ";
        String transType = " ";
        Double amount = 0.0;
        String particulars = " ";
        String prodType = "AD";
        TxTransferTO obj = new TxTransferTO();
        actNum = CommonUtil.convertObjToStr(singleLst.get(8));
        particulars = CommonUtil.convertObjToStr(singleLst.get(12));
        amount = CommonUtil.convertObjToDouble(singleLst.get(6));
        transType = CommonUtil.convertObjToStr(singleLst.get(4));
        if (actNum.length() > 0) {
            HashMap whereMap = new HashMap();
            whereMap.put("ACT_NUM", actNum);
            List mapDataList = ClientUtil.executeQuery("getActNumFromAllProducts", whereMap);
            if (mapDataList != null && mapDataList.size() > 0) {
                whereMap = (HashMap) mapDataList.get(0);
                prodType = CommonUtil.convertObjToStr(whereMap.get("PROD_TYPE"));
                if(!(prodType.equals("OA")||prodType.equals("AD"))){
                    ClientUtil.showMessageWindow("Account's Other than SB/CA/OD not allowed - "+actNum);
                    invalidBulkUploadTxtFile = true;
                    return obj;
                }
                if (prodType.length() > 0 && (prodType.equals("OA")||prodType.equals("AD"))) {
                    if (transType.equals("D")) {
                        transType = "DEBIT";
                    } else if (transType.equals("C")) {
                        transType = "CREDIT";
                    }
                    obj.setProdType(prodType);
                    obj.setActNum(actNum);
                    obj.setTransType(transType);
                    obj.setAmount(CommonUtil.convertObjToDouble(amount));
                    obj.setInpAmount(CommonUtil.convertObjToDouble(amount));
                    if (prodType.equals("AD") && CommonUtil.convertObjToStr(obj.getTransType()).equals("DEBIT")) {
                        obj.setAuthorizeRemarks("DP");
                    }
                    obj.setParticulars(particulars);
                    obj.setInstDt(curDate);
                    obj.setTransDt(curDate);
                    obj.setBatchId("-");
                    obj.setTransId("-");
                    obj.setInitiatedBranch(TrueTransactMain.BRANCH_ID);
                    obj.setInitChannType(TrueTransactMain.BRANCH_ID);
                    obj.setInitTransId(TrueTransactMain.USER_ID);
                    obj.setInpCurr("INR");
                    obj.setTransMode("TRANSFER");
                    obj.setInstType("VOUCHER");
                    obj.setLinkBatchId(obj.getActNum());
                    obj.setStatusDt(curDate);
                    obj.setStatusBy(TrueTransactMain.USER_ID);
                    obj.setStatus("CREATED");
                    obj.setTransModType(obj.getProdType());
                    obj.setNarration("BULK_TRANSACTION");
                    obj.setScreenName(getScreen());
                }
            }else{
                ClientUtil.showMessageWindow("In-Valid Account Number : "+actNum);
                invalidBulkUploadTxtFile = true;
                return obj;
            }
        }
        return obj;
    }
        

    public String getCellValue(HSSFCell cell) {
        String rowFields = "";
//           HSSFCell cell = row.getCell((short)3);  
        if (cell != null) {
            switch (cell.getCellType()) {
                case 0:
                    rowFields += cell.getNumericCellValue();
                    break;
                case 1:
                    rowFields += cell.getStringCellValue();
                    break;
//                case 2:  
//                    rowFields += cell.getDateCellValue() + ";";  
//                    break;       
            }
        }
        return rowFields;
    }

    public boolean calcRecurringDates() {
        String prodId = CommonUtil.convertObjToStr(getProductId());
        HashMap recurrMap = new HashMap();
        recurrMap.put("PROD_ID", prodId);
        List lst = ClientUtil.executeQuery("getBehavesLikeForDeposit", recurrMap);
        if (lst != null && lst.size() > 0) {
            HashMap recurringMap = (HashMap) lst.get(0);
            if (getMainProductTypeValue().equals("TD") && getTransType().equals("CREDIT") && recurringMap.get("BEHAVES_LIKE").equals("RECURRING")) {
                String depositNo = getAccountNo();
                if (depositNo.lastIndexOf("_") != -1) {
                    depositNo = depositNo.substring(0, depositNo.lastIndexOf("_"));
                }
                recurrMap.put("DEPOSIT_NO", depositNo);
                lst = ClientUtil.executeQuery("getDepositAmountForRecurring", recurrMap);
                if (lst != null && lst.size() > 0) {
                    recurringMap = (HashMap) lst.get(0);
                    double depAmt = CommonUtil.convertObjToDouble(recurringMap.get("DEPOSIT_AMT")).doubleValue();
                    double totInstall = CommonUtil.convertObjToDouble(recurringMap.get("TOTAL_INSTALLMENTS")).doubleValue();
                    double totPaid = CommonUtil.convertObjToDouble(recurringMap.get("TOTAL_INSTALL_PAID")).doubleValue();
                    double totAmt = CommonUtil.convertObjToDouble(recurringMap.get("TOTAL_BALANCE")).doubleValue();
                    double balAmt = depAmt * totInstall;
                    double payAmt = balAmt - totAmt;
                    double txtAmt = CommonUtil.convertObjToDouble(getAmount()).doubleValue();
                    //System.out.println("#######Current Date : " + curDate);
                    //System.out.println("#######Current : " + recurringMap.get("MATURITY_DT"));
                    //System.out.println("#######totAmt : " + totAmt);
                    //System.out.println("#######balAmt : " + balAmt);
                    //System.out.println("#######payAmt : " + payAmt);
                    double penalAmt = CommonUtil.convertObjToDouble(getDepositPenalAmt()).doubleValue();
                    if (penalAmt > 0) {
                        payAmt = payAmt + penalAmt;
                    }
                    if (payAmt >= txtAmt && payAmt != 0) {
                        //Commented By Suresh
//                        if(DateUtil.dateDiff((Date)recurringMap.get("MATURITY_DT"),curDate)<0){
//                            Date matDt = null;
//                            if(recurringMap.get("LAST_TRANS_DT")==null){
//                                //                            lastDt = DateUtil.getDateMMDDYYYY(CommonUtil.convertObjToStr(recurringMap.get("DEPOSIT_DT")));
//                            }else {
//                                matDt =(Date)recurringMap.get("MATURITY_DT");
//                                Date currDt = (Date)curDate.clone();
//                                long maturityDt = DateUtil.dateDiff(currDt,matDt);
//                                System.out.println("#######maturityDt : "+maturityDt);
//                                long installment = CommonUtil.convertObjToInt(recurringMap.get("TOTAL_INSTALLMENTS"));
//                                if(installment == 1){
//                                    if(maturityDt<=30){
//                                        Date addMatDate = DateUtil.addDays((Date)curDate.clone(),30);
//                                        System.out.println("####*****Maturity Date : "+addMatDate);
//                                        HashMap updateMap = new HashMap();
//                                        updateMap.put("MATURITY_DT",addMatDate);
//                                        updateMap.put("DEPOSIT_NO",depositNo);
//                                        ClientUtil.execute("updateMaturityDate", updateMap);
//                                    }
//                                }else{
//                                }
//                            }
//                            //                    return true;
//                        }else{
//                            ClientUtil.showAlertWindow("This Deposit has been Already Matured....");
//                            return false;
//                        }
                    } else {
                        ClientUtil.showAlertWindow("Exceeding the total Installments Amount...\n"
                                + "Balance Amount is " + payAmt);
                        return false;
                    }
                }
            }
        }
        return true;
    }

    // To perform Appropriate operation... Insert, Update, Delete...
    public void doAction(String parameter) {
        TTException exception = null;
        HashMap proxyReturnMap = null;
        boolean oaExist = true;
        try {

            // The following block added by Rajesh to avoid Save operation after Authorization.
            // If one person opened a transaction for Edit and another person opened the same
            // transaction for Authorization, the system is allowing to save after Authorization also.
            // So, after authorization again the GL gets updated and a/c level shadow credit/debit goes negative.
            // In this case the should not allow to save or some error message should display.
            if ((!getBatchId().equals("-")) && getOperation() != ClientConstants.ACTIONTYPE_AUTHORIZE && getOperation() != ClientConstants.ACTIONTYPE_REJECT) {
                HashMap whereMap = new HashMap();
                whereMap.put("BATCH_ID", getBatchId());
                //screen lock
                HashMap maps = new HashMap();
                maps.put("USER_ID", ProxyParameters.USER_ID);
                maps.put("TRANS_ID", getBatchId());
                maps.put("TRANS_DT", curDate.clone());
                maps.put("INITIATED_BRANCH", ProxyParameters.BRANCH_ID);
                List lstlock = ClientUtil.executeQuery("selectauthorizationLock", maps);
                if (lstlock != null && lstlock.size() > 0) {
                    HashMap map = new HashMap();
                    StringBuffer open = new StringBuffer();
                    for (int i = 0; i < lstlock.size(); i++) {
                        map = (HashMap) lstlock.get(i);
                        open.append("\n" + "User Id  :" + " ");
                        open.append(CommonUtil.convertObjToStr(map.get("OPEN_BY")) + "\n");
                        open.append("Mode Of Operation  :" + " ");
                        open.append(CommonUtil.convertObjToStr(map.get("MODE_OF_OPERATION")) + " ");
                    }
                    ClientUtil.showMessageWindow("Already opened by" + open);
                    return;
                }
                //
                if (backDatedTransDate != null && !backDatedTransDate.equals("")) {
                    System.out.println("Backdated deletion entry : ");
                }else{
                    whereMap.put("TRANS_DT", curDate.clone());
                    whereMap.put("INITIATED_BRANCH", ProxyParameters.BRANCH_ID);
                    List lst = ClientUtil.executeQuery("getTransferAuthorizeStatus", whereMap);
                    if (lst != null && lst.size() > 0) {
                        whereMap = (HashMap) lst.get(0);
                        String authStatus = CommonUtil.convertObjToStr(whereMap.get("AUTHORIZE_STATUS"));
                        String authBy = CommonUtil.convertObjToStr(whereMap.get("AUTHORIZE_BY"));
                        if (!authStatus.equals("") && getOperation() != ClientConstants.ACTIONTYPE_EXCEPTION) {
                            setOperation(ClientConstants.ACTIONTYPE_FAILED);
                            throw new TTException("This transaction already " + authStatus.toLowerCase() + " by " + authBy);
                        }
                    }
                }
            }
            // End


            if (this.authorizeMap != null) {

                HashMap autho = new HashMap();
                autho.put(CommonConstants.AUTHORIZEMAP, authorizeMap);
                autho.put(CommonConstants.MODULE, getModule());
                autho.put(CommonConstants.SCREEN, getScreen());
                autho.put("OLDAMOUNT", null);
//                autho.put("ACT_CLOSING_MIN_BAL_CHECK_CASH_TRANSFER","ACT_CLOSING_MIN_BAL_CHECK_CASH_TRANSFER");
                checkLoanDebit("debitloanType");
                boolean isCreditToDailyDeposit = isCreditToDailyDepositWithoutAgent();
                if(isCreditToDailyDeposit){// Added by nithya for KD-391 - Daily Deposit- Here after saving the transaction, wrongly inserting a Cash Transaction without agent ID
                    autho.put("CREDIT_TO_DEPOSIT_TRANSFER_SCREEN", "CREDIT_TO_DEPOSIT_TRANSFER_SCREEN");
                }
                if (loanDebitType != null && loanDebitType.size() > 0) {
                    autho.putAll(loanDebitType);
                }
                if (parameter != null && parameter.length() > 0) {
                    autho.put("EXCEPTION", "EXCEPTION");
                }
                if (authMap.containsKey("LINK_BATCH_ID")) {
                    autho.put("LINK_BATCH_ID", authMap.get("LINK_BATCH_ID"));
                    autho.put("BATCH_ID", authMap.get("BATCH_ID"));
                    autho.put("REMARKS", "Due to Processing Charge");
                }
                if (isHoAccount() == true) {
                    if (getTransType() == CommonConstants.DEBIT) {
                        autho.put("RESPONDING", "RESPONDING");
                    }
                    autho.put("ORG_RESP_DETAILS", getOrgRespList());
                }
                if(CommonUtil.convertObjToDouble(getActInterestLoan())>0){
                    autho.put("ACTUAL_INTEREST", getActInterestLoan());
                }
                //System.out.println("TRANSFEROB####" + autho);
                if (TrueTransactMain.SERVICE_TAX_REQ.equals("Y")) {
                autho.put("SERVICE_TAX_AUTH", "SERVICE_TAX_AUTH");
                 }
                //                if (oaExist) {
                //                    if (procChargeHash.containsKey("PROC_AMT"))
                //                        autho.put("PROCCHARGEMAP",procChargeHash);
                //System.out.println("autho is " + autho);
                //setIsTransaction(true);
                proxyReturnMap = proxy.execute(autho, map);
                //                } else
                //                    throw new TTException("Insufficient Balance in Operative A/c to Debit Processing amount...");
                authorizeMap = null;
            } else {
                //System.out.println("populateBean!!!!!");
//                map.put("ACT_CLOSING_MIN_BAL_CHECK_CASH_TRANSFER","ACT_CLOSING_MIN_BAL_CHECK_CASH_TRANSFER");
                //                setTransactionDetailTLAD();
                //setIsTransaction(true);
                proxyReturnMap = proxy.execute(populateBean(parameter), map);
            }
            if (proxyReturnMap != null && proxyReturnMap.containsKey(CommonConstants.TRANS_ID) && proxyReturnMap.get(CommonConstants.TRANS_ID) != null) {
                ClientUtil.showMessageWindow("Transaction No. : " + CommonUtil.convertObjToStr(proxyReturnMap.get(CommonConstants.TRANS_ID)));
                int yesNo = 0;
                String[] options = {"Yes", "No"};
                yesNo = COptionPane.showOptionDialog(null, "Do you want to print?", CommonConstants.WARNINGTITLE,
                        COptionPane.OK_OPTION, COptionPane.WARNING_MESSAGE,
                        null, options, options[0]);
                //System.out.println("#$#$$ yesNo : " + yesNo);
                if (yesNo == 0) {
                    TTIntegration ttIntgration = null;
                    HashMap paramMap = new HashMap();
                    paramMap.put("TransId", proxyReturnMap.get("SINGLE_TRANS_ID"));
                    paramMap.put("TransDt", curDate);
                    paramMap.put("BranchId", ProxyParameters.BRANCH_ID);
                    ttIntgration.setParam(paramMap);
                    ttIntgration.integrationForPrint("ReceiptPayment", false);
                }
                debitLoanType = new HashMap();
                ALL_LOAN_AMOUNT = new HashMap();
            }
            reconcileMap = null;
            setCreatingFlexi("");
            setFlexiAmount(0.0);
            setDepositTransId(CommonUtil.convertObjToStr(proxyReturnMap.get("TRANS_ID")));
            //System.out.println("******getDepositTransId :" + getDepositTransId());
            //            long transSlNo=0;
            //            HashMap interestMap=new HashMap();
            //            HashMap insetMap=new HashMap();
            //            String prodType="";
            //             String acctNum="";
            //            for(int j=0;j<transferTOs.size();j++){
            //
            //                TxTransferTO txTransfer=new TxTransferTO();
            //                txTransfer=(TxTransferTO)transferTOs.get(j);
            //                if(txTransfer.getProdType().equals("TL")){
            //                    prodType="TL";
            //                    acctNum=txTransfer.getActNum();
            //                    break;
            //                }
            //                if(txTransfer.getProdType().equals("AD")){
            //                    prodType="AD";
            //                    acctNum=txTransfer.getActNum();
            //                    break;
            //                }
            //            }
            //            if(operation==ClientConstants.ACTIONTYPE_AUTHORIZE)
            //                for(int j=0;j<transferTOs.size();j++){
            //
            //                    TxTransferTO txTransfer=new TxTransferTO();
            //                    txTransfer=(TxTransferTO)transferTOs.get(j);
            //
            //                    if(acctNum !=null && acctNum.length()>0){
            //                        interestMap.put("ACT_NUM",acctNum);
            //                        List lst=null;
            //                        if(prodType.equals("TL")){
            //                            lst=ClientUtil.executeQuery("getIntDetails", interestMap);
            //                            //                            prodType="TL";
            //                        }
            //                        if(prodType.equals("AD")){
            //                            lst=ClientUtil.executeQuery("getIntDetailsAD", interestMap);
            //                            //                            prodType="AD";
            //                        }
            //                        if(lst!=null && lst.size()>0){
            //                            interestMap=(HashMap)lst.get(0);
            //                            insetMap.put("ACCOUNTNO",acctNum);
            //                            insetMap.put("TRANSTYPE","CREDIT");
            //                            insetMap.put("BRANCH_CODE",TrueTransactMain.BRANCH_ID);
            //                            transSlNo=CommonUtil.convertObjToLong(interestMap.get("TRANS_SLNO"));
            //                        }
            //                    }
            //                    if (txTransfer.getProdType().equals("GL")) {
            //                        System.out.println("interestMap###"+interestMap);
            //                        double amount=txTransfer.getAmount().doubleValue();
            //                        //                                long transSlNo=CommonUtil.convertObjToLong(interestMap.get("TRANS_SLNO"));
            //                        transSlNo++;
            //                        if(txTransfer.getAuthorizeRemarks()!=null && txTransfer.getAuthorizeRemarks().equals("INTEREST")){
            //
            //                            insetMap.put("IBAL",new Double(0));
            //                            insetMap.put("INTEREST",new Double(amount));
            //                        }else{
            //                            insetMap.put("IBAL",new Double(0));
            //                            insetMap.put("INTEREST",new Double(0));
            //                        }
            //                        if(txTransfer.getAuthorizeRemarks()!=null && txTransfer.getAuthorizeRemarks().equals("PENAL_INT")){
            //                            insetMap.put("PENAL",new Double(amount));
            //                            insetMap.put("PIBAL",new Double(0));
            //                        }else{
            //                            insetMap.put("PENAL",new Double(0));
            //                            insetMap.put("PIBAL",new Double(0));
            //                        }
            //                        insetMap.put("TRANS_SLNO",new Long(transSlNo));
            //                        insetMap.put("PRINCIPAL",new Double(0));
            //                        insetMap.put("PBAL",interestMap.get("PBAL"));
            //                        insetMap.put("NPA_INTEREST",new Double(0));
            //                        insetMap.put("NPA_INT_BAL",new Double(0));
            //                        insetMap.put("NPA_PENAL",new Double(0));
            //                        insetMap.put("NPA_PENAL_BAL",new Double(0));
            //                        insetMap.put("EXCESS_AMT",new Double(0));
            //                        insetMap.put("AUTHORIZE_STATUS","AUTHORIZED");
            //                        insetMap.put("EXPENSE",new Double(0));
            //                        insetMap.put("EBAL",new Double(0));
            //                        insetMap.put("TRANS_ID",String .valueOf("dummy"));
            //                        insetMap.put("PARTICULARS",String .valueOf("dummy"));
            //                        insetMap.put("PARTICULARS",String .valueOf("dummy"));
            //                        insetMap.put("TRN_CODE",String .valueOf("C*"));
            //
            //                        System.out.println("insetMap######"+insetMap);
            //                        if(prodType.equals("TL"))
            //                            ClientUtil.execute("insertLoansDisbursementDetailsCumLoan",insetMap);
            //                        if(prodType.equals("AD"))
            //                            ClientUtil.execute("insertAuthorizeAdvTransDetails",insetMap);
            //                        interestMap=new HashMap();
            //                    }
            //
            //                }
            interestAmt = depTrans();
            setInterestAmt(interestAmt);
            setResult(getOperation());
            operation = ClientConstants.ACTIONTYPE_CANCEL;
            minBalException = false;
        } catch (Exception e) {
            setResult(ClientConstants.ACTIONTYPE_FAILED);
            e.printStackTrace();

            if (e instanceof TTException) {
                exception = (TTException) e;
            }
        }

        // If TT Exception
        if (exception != null) {
            HashMap exceptionHashMap = exception.getExceptionHashMap();
            //System.out.println("exception###" + exception);
            //System.out.println("exception.getExceptionHashMap();" + exception.getExceptionHashMap());
            if (exceptionHashMap != null) {
                ArrayList list = (ArrayList) exceptionHashMap.get(CommonConstants.EXCEPTION_LIST);
                if (list != null && list.size() > 0 && list.get(0) instanceof String && ((String) list.get(0)).startsWith("SUSPECIOUS")
                        || CommonUtil.convertObjToStr(list.get(0)).equals("AED") || CommonUtil.convertObjToStr(list.get(0)).equals("AEL")
                        || CommonUtil.convertObjToStr(list.get(0)).equals("ESL")) {
                    TTException tt = new TTException(CommonUtil.convertObjToStr(list.get(0)));
//                    parseException.logException(tt, true);
                    
                    String alertMsg = "";
                    if (((String) list.get(0)).startsWith("SUSPECIOUS")) {
                        alertMsg = "Suspecious Activity Tracking : Account is Suspecious !!! ";
                        if (((String) list.get(0)).equals("SUSPECIOUS_CE")) {
                            alertMsg += "SUSPECIOUS_CE";
                        } else if (((String) list.get(0)).equals("SUSPECIOUS_WE")) {
                            alertMsg += "SUSPECIOUS_WE";
                        }
                    } else if (CommonUtil.convertObjToStr(list.get(0)).equals("AED")) {
                        alertMsg = "Advances Date has Expired - ADVANCES_EXPIRY_DATE";
                    } else if (CommonUtil.convertObjToStr(list.get(0)).equals("ESL")) {
                        alertMsg = "Loan Exceeding Sub Limit - EXCEED_SUB_LIMIT";
                    } else if (CommonUtil.convertObjToStr(list.get(0)).equals("AEL")) {
                        alertMsg = "Available Balance is less than the input Amount - ADVANCES_EXCEED_LIMIT";
                    } else {
                        alertMsg = CommonUtil.convertObjToStr(list.get(0));
                    }
                    ClientUtil.displayAlert(alertMsg);
                    
                    
//                    Object[] dialogOption = {"EXCEPTION", "CANCEL"};
//                    parseException.setDialogOptions(dialogOption);
//                    if (parseException.logException(tt, true) == 0) {
//                        try {
//                            //                        setResult(ac);
//                            doAction("EXCEPTION");
//
//                        } catch (Exception e) {
//                            if (e instanceof TTException) {
//                                Object[] dialog = {"OK"};
//                                parseException.setDialogOptions(dialog);
//                                exception = (TTException) e;
//                                parseException.logException(exception, true);
//                            }
//                        }
//                    }
                    Object[] dialogOption1 = {"OK"};
                    parseException.setDialogOptions(dialogOption1);
                } else if (CommonUtil.convertObjToStr(list.get(0)).equals("MIN")) {
                    minBalException = true;
                    Object[] dialogOption = {"CONTINUE", "CANCEL"};
                    parseException.setDialogOptions(dialogOption);
                    if (parseException.logException(exception, true) == 0) {
                        try {
                            //                        setResult(ac);
                            doAction("EXCEPTION");

                        } catch (Exception e) {
                            if (e instanceof TTException) {
                                Object[] dialog = {"OK"};
                                parseException.setDialogOptions(dialog);
                                exception = (TTException) e;
                                parseException.logException(exception, true);
                            }
                        }
                    }
                    Object[] dialogOption1 = {"OK"};
                    parseException.setDialogOptions(dialogOption1);
                } else {
                    parseException.logException(exception, true);
                }
            } else { // To Display Transaction No showing String message
                parseException.logException(exception, true);
                setResult(getOperation());
            }
        }
    }

    public double depTrans() {
        HashMap depMap = new HashMap();
        double intAmt = 0.0;
        double amt = 0.0;
        depMap.put("BATCH_ID", getDepositTransId());
        depMap.put("TRANS_DT", curDate);
        depMap.put("INITIATED_BRANCH", ProxyParameters.BRANCH_ID);
        List lst = ClientUtil.executeQuery("getTransactionAmount", depMap);
        if (lst.size() > 0) {
            for (int i = 0; i < lst.size(); i++) {
                depMap = (HashMap) lst.get(i);
                amt = CommonUtil.convertObjToDouble(depMap.get("AMOUNT")).doubleValue();
                intAmt = amt + intAmt;
            }
        }
        //System.out.println("#####intAmt : " + intAmt);
        return intAmt;
    }

    private ArrayList setActNumBlankForGL() {
        for (int i = 0; i < transferTOs.size(); i++) {
            TxTransferTO txtransferTo = (TxTransferTO) transferTOs.get(i);
            if (txtransferTo.getProdType().equals("GL")) {
                txtransferTo.setActNum("");
            }
        }
      //  System.out.println("transferTOs#####" + transferTOs);
        return transferTOs;
    }

    private boolean checkForOA() {
        String loanProdId = "";
        HashMap hash = new HashMap();
        HashMap whereMap = new HashMap();
        HashMap actHash = new HashMap();
        String actNum = "";
        if (getLoanActNo().lastIndexOf("_") != -1) {
            actNum = getLoanActNo().substring(0, getLoanActNo().lastIndexOf("_"));
        } else {
            actNum = getLoanActNo();
        }
        whereMap.put(CommonConstants.MAP_NAME, "getProcPercentageTL");
        actHash.put("ACT_NUM", actNum);
        //System.out.println("###ACTNUMBER" + actNum);
        whereMap.put(CommonConstants.MAP_WHERE, actHash);
        double procPer = 0.0, oaAmt = 0.0, procAmt = 0.0;
        try {
            hash = proxy.executeQuery(whereMap, map);
            ArrayList a = (ArrayList) hash.get(CommonConstants.DATA);
            HashMap b = (HashMap) a.get(0);
            //System.out.println("firsthash" + hash);
            procPer = CommonUtil.convertObjToDouble(b.get("PROC_CHRG_PER")).doubleValue();
            loanProdId = CommonUtil.convertObjToStr(b.get("PROD_ID"));
            //        procPer = Double.parseDouble(c);
            String ACT_NUM = getAccountNo();
            //System.out.println("operative account  ##" + ACT_NUM);
            whereMap.put(CommonConstants.MAP_WHERE, ACT_NUM);
            whereMap.put(CommonConstants.MAP_NAME, "getOABalanceTransferTL");
            hash = proxy.executeQuery(whereMap, map);
            //System.out.println("secondhash" + hash);
            hash = (HashMap) ((ArrayList) hash.get(CommonConstants.DATA)).get(0);
            hash.put("LOAN_PROD_ID", loanProdId);
            oaAmt = Double.parseDouble(hash.get("CLEAR_BALANCE").toString());
            procChargeHash = new HashMap();
            if (procPer > 0) {
                procChargeHash.putAll(hash);
            }
            a = null;
            b = null;
        } catch (Exception e) {
            System.out.println(e);
        }
        double amt = Double.parseDouble(transAmt.replaceAll(",", ""));
        procAmt = amt * (procPer / 100);
        //        if (procAmt <= oaAmt)
        procChargeHash.put("LINK_BATCH_ID", actNum);
        procChargeHash.put("PROC_AMT", new Double(procAmt));
        procChargeHash.put("OA_ACT_NUM", hash.get("ACT_NUM"));
        procChargeHash.put("OA_PROD_ID", hash.get("PROD_ID"));
        procChargeHash.put("TL_PROD_ID", loanProdId);
        procChargeHash.put("PROC_AMT", new Double(procAmt));
        //System.out.println("proceschargehash####" + procChargeHash);
        hash = null;
        whereMap = null;
        actHash = null;
        if (procAmt > oaAmt) {
            int result = ClientUtil.confirmationAlert("Insufficient Balance in Operative A/c to Debit Processing amount.Collect Manually..");
            operation = ClientConstants.ACTIONTYPE_CANCEL;
            if (result == 0) {
                procChargeHash = new HashMap();
            }
            return true;
        }
        return true;
    }

    private TxTransferTO setTransactiontoTLAD(TxTransferTO txtransTo) {
        TxTransferTO txTo = new TxTransferTO();
        txTo.setProdType(txtransTo.getProdType());
        txTo.setProdId(txtransTo.getProdId());
        txTo.setActNum(txtransTo.getActNum());
        txTo.setAcHdId(txtransTo.getAcHdId());
        txTo.setAmount(txtransTo.getAmount());
        txTo.setAuthorizeBy(txtransTo.getAuthorizeBy());
        txTo.setAuthorizeDt(txtransTo.getAuthorizeDt());
        txTo.setAuthorizeRemarks(txtransTo.getAuthorizeRemarks());
        txTo.setAuthorizeStatus(txtransTo.getAuthorizeStatus());
        txTo.setBatchId(txtransTo.getBatchId());
        txTo.setBranchId(txtransTo.getBranchId());
        txTo.setInitChannType(txtransTo.getInitChannType());
        txTo.setInitTransId(txtransTo.getInitTransId());
        txTo.setInitiatedBranch(txtransTo.getInitiatedBranch());
        txTo.setInpAmount(txtransTo.getInpAmount());
        txTo.setAuthorizeBy(txtransTo.getAuthorizeBy());
        txTo.setTransDt(txtransTo.getTransDt());
        txTo.setTransType(txtransTo.getTransType());
        txTo.setInstType(txtransTo.getInstType());
        txTo.setInstrumentNo1(txtransTo.getInstrumentNo1());
        txTo.setInstrumentNo2(txtransTo.getInstrumentNo2());
        txTo.setInstDt(txtransTo.getInstDt());
        txTo.setTransId(txtransTo.getTransId());
        txTo.setTransMode(txtransTo.getTransMode());
        txTo.setTransType(txtransTo.getTransType());
        txTo.setStatus(txtransTo.getStatus());
        txTo.setStatusBy(txtransTo.getStatusBy());
        txTo.setStatusDt(txtransTo.getStatusDt());
        txTo.setScreenName(getScreen());

        return txTo;

    }

    public HashMap asAnWhenCustomerComesYesNO(String acct_no, String batch_id) {
        HashMap map = new HashMap();
        List lst = null;
        if (batch_id == null) {
            map.put("ACT_NUM", acct_no);
        } else {
            map.put("BATCH_ID", batch_id);
        }
        map.put("TRANS_DT", curDate);
        map.put("INITIATED_BRANCH", ProxyParameters.BRANCH_ID);
        if (acct_no != null && acct_no.length() > 0) {
            lst = ClientUtil.executeQuery("IntCalculationDetail", map);
        }
        if (lst == null || lst.isEmpty()) {
            lst = ClientUtil.executeQuery("IntCalculationDetailAD", map);
        }
        if (lst != null && lst.size() > 0) {
            map = (HashMap) lst.get(0);
            setLinkMap(map);
        } else {
            setLinkMap(null);
        }
        return map;
    }

    public HashMap depositPenalReceving(String batch_id) {
        HashMap map = new HashMap();
        List lst = null;
        if (!CommonUtil.convertObjToStr(batch_id).equals("")) {
            //            map.put("ACT_NUM",acct_no);
            //        else
            map.put("BATCH_ID", batch_id);
            //        if(acct_no !=null && acct_no.length()>0)
            map.put("TRANS_DT", curDate);
            map.put("INITIATED_BRANCH", ProxyParameters.BRANCH_ID);
            lst = ClientUtil.executeQuery("getDepositRecurringList", map);
            if (lst != null && lst.size() > 0) {
                map = (HashMap) lst.get(0);
                setPenalMap(map);
            }
        }
        return map;
    }

    public List getDocumentDetail(String mapName, String mapWhere) {
        //System.out.println("#### amount : " + amount);
        List lst = null;
        HashMap hash = new HashMap();
        hash.put(CommonConstants.MAP_NAME, mapName);
        hash.put(CommonConstants.MAP_WHERE, mapWhere);
        try {
            lst = (List) proxy.executeQuery(hash, map).get(CommonConstants.DATA);
        } catch (Exception e) {
            System.out.println(e);
        }
        hash = null;
        return lst;
    }

    /*
     * this method will check if the batch has been tallied or not
     */
    public boolean isBatchTalliedArrayList() {
        int size = this.transferTOs.size();
        double creditAmt = 0, debitAmt = 0;
        TxTransferTO objTO;
        for (int i = 0; i < size; i++) {
            objTO = (TxTransferTO) transferTOs.get(i);
            if (!objTO.getStatus().equalsIgnoreCase(CommonConstants.STATUS_DELETED)) {
                if (objTO.getTransType().equals(CommonConstants.DEBIT)) {
                    debitAmt += objTO.getAmount().doubleValue();
                    long dr = roundOff((long) (debitAmt * 1000));
                    debitAmt = dr / 100.0;
                } else if (objTO.getTransType().equals(CommonConstants.CREDIT)) {
                    creditAmt += objTO.getAmount().doubleValue();
                    long cr = roundOff((long) (creditAmt * 1000));
                    creditAmt = cr / 100.0;
                }
            }
        }
        if (creditAmt == debitAmt) {
            return true;
        }
        return false;
    }

    /**
     * this mehtod will return the basic account information for the transfering
     * account, as the data elements in the hashmap
     */
    public HashMap getTransActDetails(String actNum) {
        // if there is no specified account number then return a null hashmap
        //System.out.println("from Trans Details" + actNum);
        if (actNum == null || actNum.equals("")) {
            return null;
        }

        HashMap myMap = new HashMap();
        myMap.put("ACT_NUM", actNum);
        myMap.put("BRANCH_CODE", TrueTransactMain.BRANCH_ID);

        ArrayList resultList = (ArrayList) ClientUtil.executeQuery("getBalance" + this.getMainProductTypeValue(), myMap);
        HashMap resultMap = (HashMap) resultList.get(0);

        ArrayList resultList1 = (ArrayList) ClientUtil.executeQuery("getActData" + this.getMainProductTypeValue(), myMap);

        HashMap resultMap1 = null;

        if (resultList1 != null && resultList1.size() > 0) {
            resultMap1 = (HashMap) resultList1.get(0);
            resultMap.putAll(resultMap1);
        }

        return resultMap;
    }

    public ComboBoxModel getProductTypeModel() {
        return productTypeModel;
    }

    public void setProductTypeModel(ComboBoxModel _productTypeModel) {
        productTypeModel = _productTypeModel;
        setChanged();
    }

    public String getProductTypeValue() {
        return productTypeValue;
    }

    public void setProductTypeValue(String _productTypeValue) {
        productTypeValue = _productTypeValue;
        setChanged();
    }

    public String getBatchId() {
        return batchId;
    }

    public void setBatchId(String _batchId) {
        batchId = _batchId;
        setChanged();
    }

    public String getTransId() {
        return transId;
    }

    public void setTransId(String _transId) {
        transId = _transId;
        setChanged();
    }

    public String getProductId() {
        return productId;
    }

    public void setProductId(String _productId) {
        productId = _productId;
        setChanged();
    }

    public String getAccountHeadDesc() {
        return accountHeadDesc;
    }

    public void setAccountHeadDesc(String _accountHeadDesc) {
        accountHeadDesc = _accountHeadDesc;
    }

    public String getAccountNo() {
        return accountNo;
    }

    public void setAccountNo(String _accountNo) {
        accountNo = _accountNo;
        setChanged();
    }

    public ComboBoxModel getInstrumentTypeModel() {
        return instrumentTypeModel;
    }

    public void setInstrumentTypeModel(ComboBoxModel _instrumentTypeModel) {
        instrumentTypeModel = _instrumentTypeModel;
        setChanged();
    }

    public String getInstrumentTypeValue() {
        return instrumentTypeValue;
    }

    public void setInstrumentTypeValue(String _instrumentTypeValue) {
        instrumentTypeValue = _instrumentTypeValue;
        setChanged();
    }

    public String getInstrumentNo1() {
        return instrumentNo1;
    }

    public void setInstrumentNo1(String _instrumentNo1) {
        instrumentNo1 = _instrumentNo1;
        setChanged();
    }

    public String getInstrumentNo2() {
        return instrumentNo2;
    }

    public void setInstrumentNo2(String _instrumentNo2) {
        instrumentNo2 = _instrumentNo2;
        setChanged();
    }

    public String getInstrumentDate() {
        return instrumentDate;
    }

    public void setInstrumentDate(String _instrumentDate) {
        instrumentDate = _instrumentDate;
        setChanged();
    }

    public ComboBoxModel getCurrencyTypeModel() {
        return currencyTypeModel;
    }

    public void setCurrencyTypeModel(ComboBoxModel _currencyTypeModel) {
        currencyTypeModel = _currencyTypeModel;
        setChanged();
    }

    public String getCurrencyTypeValue() {
        return currencyTypeValue;
    }

    public void setCurrencyTypeValue(String _currencyTypeValue) {
        currencyTypeValue = _currencyTypeValue;
        setChanged();
    }

    public String getCurrencyType() {
        return currencyType;
    }

    public void setCurrencyType(String _currencyType) {
        currencyType = _currencyType;
        setChanged();
    }

    public String getTransferAmount() {
        return transferAmount;
    }

    public void setTransferAmount(String _transferAmount) {
        transferAmount = _transferAmount;
        setChanged();
    }

    public String getParticulars() {
        return particulars;
    }

    public void setParticulars(String _particulars) {
        particulars = _particulars;
        setChanged();
    }

    public int getTotalDrInstruments() {
        return totalDrInstruments;
    }

    public void setTotalDrInstruments(int _totalDrInstruments) {
        totalDrInstruments = _totalDrInstruments;
        setChanged();
    }

    public double getTotalDrAmount() {
        return totalDrAmount;
    }

    public void setTotalDrAmount(double _totalDrAmount) {
        totalDrAmount = _totalDrAmount;
        setChanged();
    }

    public int getTotalCrInstruments() {
        return totalCrInstruments;
    }

    public void setTotalCrInstruments(int _totalCrInstruments) {
        totalCrInstruments = _totalCrInstruments;
        setChanged();
    }

    public double getTotalCrAmount() {
        return totalCrAmount;
    }

    public void setTotalCrAmount(double _totalCrAmount) {
        totalCrAmount = _totalCrAmount;
        setChanged();
    }

    //    public String getModeOfOperation() {
    //        return modeOfOperation;
    //    }
    //
    //    public void setModeOfOperation(String _modeOfOperation) {
    //        modeOfOperation = _modeOfOperation;
    //        setChanged();
    //    }
    //
    //    public String getConstitution() {
    //        return constitution;
    //    }
    //
    //    public void setConstitution(String _constitution) {
    //        constitution = _constitution;
    //        setChanged();
    //    }
    //
    //    public String getCategory() {
    //        return category;
    //    }
    //
    //    public void setCategory(String _category) {
    //        category = _category;
    //        setChanged();
    //    }
    //
    //    public String getOpeningDate() {
    //        return openingDate;
    //    }
    //
    //    public void setOpeningDate(String _openingDate) {
    //        openingDate = _openingDate;
    //        setChanged();
    //    }
    //
    //    public String getRemarks() {
    //        return remarks;
    //    }
    //
    //    public void setRemarks(String _remarks) {
    //        remarks = _remarks;
    //        setChanged();
    //    }
    //
    //    public String getAvailableBalance() {
    //        return availableBalance;
    //    }
    //
    //    public void setAvailableBalance(String _availableBalance) {
    //        availableBalance = _availableBalance;
    //        setChanged();
    //    }
    //
    //    public String getTotalBalance() {
    //        return totalBalance;
    //    }
    //
    //    public void setTotalBalance(String _totalBalance) {
    //        totalBalance = _totalBalance;
    //        setChanged();
    //    }
    //
    //    public String getClearBalance() {
    //        return clearBalance;
    //    }
    //    public void setClearBalance(String _clearBalance) {
    //        clearBalance = _clearBalance;
    //        setChanged();
    //    }
    //
    //    public String getShadowCr() {
    //        return shadowCr;
    //    }
    //
    //    public void setShadowCr(String _shadowCr) {
    //        shadowCr = _shadowCr;
    //        setChanged();
    //    }
    //
    //    public String getShadowDr() {
    //        return shadowDr;
    //    }
    //
    //    public void setShadowDr(String _shadowDr) {
    //        shadowDr = _shadowDr;
    //        setChanged();
    //    }
    public String getStatus() {
        return status;
    }

    public void setStatus(String _status) {
        status = _status;
        setChanged();
    }

    public String getAccountHeadId() {
        return accountHeadId;
    }

    public void setAccountHeadId(String _accountHeadId) {
        accountHeadId = _accountHeadId;
    }

    public String getAction() {
        return action;
    }

    public void setAction(String _action) {
        action = _action;
    }

    public String getAmount() {
        return getTransferAmount();
    }

    public void setAmount(String _amount) {
        amount = _amount;
        setChanged();
    }

    public String getTransDate() {
        return transDate;
    }

    public void setTransDate(String _transDate) {
        transDate = _transDate;
    }

    public String getTransType() {
        return transType;
    }

    public void setTransType(String _transType) {
        transType = _transType;
    }

    public String getInstType() {
        return instType;
    }

    public void setInstType(String _instType) {
        instType = _instType;
    }

    public String getInitTransId() {
        return initTransId;
    }

    public void setInitTransId(String _initTransId) {
        initTransId = _initTransId;

    }

    public String getInitChannType() {
        return initChannType;
    }

    public void setInitChannType(String _initChannType) {
        initChannType = _initChannType;
    }

    public String getTransStatus() {
        return transStatus;
    }

    public void setTransStatus(String _transStatus) {
        transStatus = _transStatus;
    }

    public int getOperation() {
        return operation;
    }

    public void setOperation(int operation) {
        this.operation = operation;
    }

    public int getResult() {
        return this.result;
    }

    public void setResult(int result) {
        this.result = result;
        setChanged();
    }

    public String getLblStatus() {
        return this.lblStatus;
    }

    public void setLblStatus(String lblStatus) {
        this.lblStatus = lblStatus;
        setChanged();
    }

    /**
     * To set the status based on ActionType, either New, Edit, etc.,
     */
    public void setStatus() {
        this.setLblStatus(ClientConstants.ACTION_STATUS[this.getOperation()]);


    }

    /**
     * To update the Status based on result performed by doAction() method
     */
    public void setResultStatus() {
        this.setLblStatus(ClientConstants.RESULT_STATUS[this.getResult()]);
    }

    public void getProducts() {
        List list = null;
        ArrayList key = new ArrayList();
        ArrayList value = new ArrayList();
        key.add("");
        value.add("");

        HashMap data;
        if (getTransType().equalsIgnoreCase(CommonConstants.CREDIT)) {
            list = ClientUtil.executeQuery("Transfer.getCreditProduct" + getMainProductTypeValue(), null);
        } else if (getTransType().equalsIgnoreCase(CommonConstants.DEBIT)) {
            list = ClientUtil.executeQuery("Transfer.getDebitProduct" + getMainProductTypeValue(), null);
        }
        if (list != null && list.size() > 0) {
            int size = list.size();
            for (int i = 0; i < size; i++) {
                data = (HashMap) list.get(i);
                key.add(data.get("PRODID"));
                value.add(data.get("PRODDESC"));
            }
        }

        data = null;

        productTypeModel = new ComboBoxModel(key, value);
        setChanged();
    }

    public void getTransferData() {
        HashMap whereMap = new HashMap();
        whereMap.put("BATCHID", batchId);
        whereMap.put("TRANS_DT", DateUtil.getDateMMDDYYYY(CommonUtil.convertObjToStr(transDate)));
        whereMap.put("INITIATED_BRANCH", initBran);
        String mapName = "getBatchTxTransferTOs";
        List list = null;
        if (this.operation == ClientConstants.ACTIONTYPE_EDIT || this.operation == ClientConstants.ACTIONTYPE_DELETE || this.operation == ClientConstants.ACTIONTYPE_AUTHORIZE || this.operation == ClientConstants.ACTIONTYPE_VIEW) {
            HashMap tempMap = new HashMap();
            tempMap.put("BATCH_ID", batchId);
            if(backDatedTransDate!=null && !backDatedTransDate.equals("")){
                tempMap.put("TRANS_DT", backDatedTransDate);
            }else{
                tempMap.put("TRANS_DT", getCurDate());
            }
            tempMap.put("INITIATED_BRANCH", ProxyParameters.BRANCH_ID);
            list = ClientUtil.executeQuery("getValueDateTO", tempMap);
            if (list != null && list.size() > 0) {
                for (int i = 0; i < list.size(); i++) {
                    tempMap = (HashMap) list.get(i);
                    if (valueDateMap == null) {
                        valueDateMap = new HashMap();
                    }
                    valueDateMap.put(String.valueOf(tempMap.get("ROWNUM")), tempMap.get("VALUE_DT"));
                }
            }
            list = null;
            //System.out.println("#$#$# list of ValueDates : " + valueDateMap);
        }
        if (this.operation == ClientConstants.ACTIONTYPE_REJECT
                || this.operation == ClientConstants.ACTIONTYPE_EXCEPTION
                || this.operation == ClientConstants.ACTIONTYPE_AUTHORIZE) {
            mapName = "getBatchTxTransferTOsAuthorize";
        }
        if (this.operation == ClientConstants.ACTIONTYPE_EXCEPTION) {
            whereMap.put("AUTHORIZE_STATUS", "EXCEPTION");
        }
        if (this.operation == ClientConstants.ACTIONTYPE_REJECT) {
            whereMap.put("AUTHORIZE_STATUS", "REJECT");
        }
        if (this.operation == ClientConstants.ACTIONTYPE_AUTHORIZE) {
            whereMap.put("AUTHORIZE_STATUS", "AUTHORIZE");
        }
        if (backDatedTransDate != null && !backDatedTransDate.equals("")) {
            whereMap.put("TRANS_DT", backDatedTransDate);
        } else {
            whereMap.put("TRANS_DT", curDate);
        }
        whereMap.put("INITIATED_BRANCH", ProxyParameters.BRANCH_ID);
        list = ClientUtil.executeQuery(mapName, whereMap);
        if (list != null && list.size() > 0) {
            this.transferTOs = (ArrayList) list;
            setTableData();
            //scrrenlock
            HashMap hash = new HashMap();
            hash.put("USER_ID", ProxyParameters.USER_ID);
            hash.put("TRANS_ID", batchId);
            if (this.operation == ClientConstants.ACTIONTYPE_REJECT) {
                hash.put("MODE_OF_OPERATION", "REJECT");
            }
            if (this.operation == ClientConstants.ACTIONTYPE_EXCEPTION) {
                hash.put("MODE_OF_OPERATION", "EXCEPTION");
            }
            if (this.operation == ClientConstants.ACTIONTYPE_AUTHORIZE) {
                hash.put("MODE_OF_OPERATION", "AUTHORIZE");
            }
            if (this.operation == ClientConstants.ACTIONTYPE_EDIT) {
                hash.put("MODE_OF_OPERATION", "EDIT");
            }
            if (this.operation == ClientConstants.ACTIONTYPE_DELETE) {
                hash.put("MODE_OF_OPERATION", "DELETE");
            }
            if (this.operation == ClientConstants.ACTIONTYPE_VIEW) {
                hash.put("MODE_OF_OPERATION", "VIEW");
            }
            if (backDatedTransDate != null && !backDatedTransDate.equals("")) {
                hash.put("TRANS_DT", backDatedTransDate);
            } else {
                hash.put("TRANS_DT", curDate);
            }
            hash.put("INITIATED_BRANCH", ProxyParameters.BRANCH_ID);
            ClientUtil.execute("insertauthorizationLock", hash);
            //
        } else {
            resetTable();
        }
        whereMap = null;
    }

    private void setTableData() {
        displayWaive = "";
        ArrayList row;
        ArrayList rows = new ArrayList();
        TxTransferTO obj;
        int size = this.transferTOs.size();
        for (int i = 0; i < size; i++) {
            obj = (TxTransferTO) this.transferTOs.get(i);
            authorizeAccountNo(obj);
            row = setRow(obj);
            setOldAmountMap(obj);
            rows.add(row);
        }
        setTable();
        tbmTransfer.setData(rows);
        tbmTransfer.fireTableDataChanged();
        obj = null;
    }

    private void setOldAmountMap(TxTransferTO obj) {
        this.oldAmountMap.put(obj.getTransId(), obj.getAmount());
    }

    private ArrayList setRow(TxTransferTO obj) {
        ArrayList row = new ArrayList();
        row.add(obj.getActNum());
        row.add(obj.getBatchId());
        row.add(obj.getTransId());
        row.add(obj.getAmount());
        row.add(obj.getTransType());
        if (obj.getProdType().equals("GL") && obj.getTransType().equals("DEBIT")) {
            row.set(0, null);
        }
        if (getOperation() == ClientConstants.ACTIONTYPE_AUTHORIZE) {
            row.add("No");
        }
        if ((getOperation() == ClientConstants.ACTIONTYPE_AUTHORIZE || getOperation() == ClientConstants.ACTIONTYPE_REJECT)
                && CommonUtil.convertObjToStr(obj.getTransType()).equals("CREDIT")
                && CommonUtil.convertObjToStr(obj.getAuthorizeRemarks()).contains("WAIVE")) {
            displayWaive += obj.getAuthorizeRemarks() + " : " + obj.getAmount() + "\n";
        }
        return row;
    }

    public void authorizeAccountNo(TxTransferTO obj) {
        if (obj.getTransType().equals(CommonConstants.DEBIT) && obj.getProdType().equals("TL")) {
            String actNum;
            if (getLoanActNo().lastIndexOf("_") != -1) {
                actNum = getLoanActNo().substring(0, getLoanActNo().lastIndexOf("_"));
            } else {
                actNum = getLoanActNo();
            }
            authMap.put("ACT_NUM", actNum);
            if (actNum.length() > 0) {// Added By Revathi 13/03/24 reff by Rejesh to avoid unnecessary query run.
                List lst = ClientUtil.executeQuery("getLinkBatchValues", authMap);
                //System.out.println(authMap + "####lst" + lst);
                if (lst != null && lst.size() > 0) {
                    authMap = (HashMap) lst.get(0);
                }
            }
        }
    }
    public void setserviceTaxAmt(String batchId) {
         HashMap whrMap = new HashMap();
                whrMap.put("ACCT_NUM", batchId);
                List lst = ClientUtil.executeQuery("getServiceTaxAmount", whrMap);
                if (lst != null && lst.size() > 0) {
                    HashMap map = (HashMap) lst.get(0);
                    if (map != null && map.containsKey("TOTAL_TAX_AMOUNT")) {
                        setLblServiceTaxval(CommonUtil.convertObjToStr(map.get("TOTAL_TAX_AMOUNT")));
                        setServiceTaxAccNo(CommonUtil.convertObjToStr(map.get("ACCT_NUM")));
                    }
                }
    }
    public void populatTranferTO(int rowNum) {
        TxTransferTO obj = (TxTransferTO) transferTOs.get(rowNum);
        if (operation == ClientConstants.ACTIONTYPE_AUTHORIZE) {
//            ArrayList row = (ArrayList)tbmTransfer.getDataArrayList().get(rowNum);
//            row.set(5, "Yes");
            tbmTransfer.setValueAt("Yes", rowNum, 5);
        }
        Date valueDt = null;
        if (valueDateMap != null && valueDateMap.containsKey(String.valueOf(rowNum))) {
            valueDt = (Date) valueDateMap.get(String.valueOf(rowNum));
        } else {
            valueDt = (Date) getCurDate().clone();
        }
        setValueDate(valueDt);
        this.setTransferTO(obj);//bb1
        if (TrueTransactMain.SERVICE_TAX_REQ.equals("Y")) {
            setserviceTaxAmt(obj.getBatchId());
        }
        if (getCurDate() != null && getValueDate() != null
                && DateUtil.dateDiff(getCurDate(), getValueDate()) != 0) {
            valueDateFlag = true;
        }
        obj = null;
    }
    public void removeServiceTaxDetails() {
        int size = this.transferTOs.size();
        for (int i = 0; i < size; i++) {
            TxTransferTO obj = this.setTOStatus((TxTransferTO) this.transferTOs.get(i));
            if (obj.getParticulars() != null && obj.getParticulars().equals("Service Tax Recived")) {
                if (obj.getBatchId().compareToIgnoreCase("-") != 0) {
                    if (oldAmountMap.containsKey(obj.getTransId())) {
                        obj.setAmount((Double) oldAmountMap.get(obj.getTransId()));
                        obj.setInpAmount((Double) oldAmountMap.get(obj.getTransId()));
                    }
                    deleteTOsList.add(obj);
                }
                transferTOs.remove(i);
                tbmTransfer.removeRow(i);
                tbmTransfer.fireTableDataChanged();
            }
            obj = null;
            setServiceTax_Map(null);
            setGlserviceTax_Map(null);
            setLblServiceTaxval("");
            break;
        }
    }
    public void deleteTransferData(int rowNum) {
        TxTransferTO obj = this.setTOStatus((TxTransferTO) this.transferTOs.get(rowNum));
        if (obj.getBatchId().compareToIgnoreCase("-") != 0) {
            if (oldAmountMap.containsKey(obj.getTransId())) {
                obj.setAmount((Double) oldAmountMap.get(obj.getTransId()));
                obj.setInpAmount((Double) oldAmountMap.get(obj.getTransId()));
            }
            deleteTOsList.add(obj);
        }
        transferTOs.remove(rowNum);
        tbmTransfer.removeRow(rowNum);
        tbmTransfer.fireTableDataChanged();
        if (TrueTransactMain.SERVICE_TAX_REQ.equals("Y")) {
            String taxApplicable = "";
            HashMap whereMap = new HashMap();
            whereMap.put("AC_HD_ID", obj.getAcHdId());
            List temp = ClientUtil.executeQuery("getCheckServiceTaxApplicableForShare", whereMap);
            if (temp != null && temp.size() > 0) {
                HashMap value = (HashMap) temp.get(0);
                if (value != null && value.containsKey("SERVICE_TAX_APPLICABLE")) {
                    taxApplicable = CommonUtil.convertObjToStr(value.get("SERVICE_TAX_APPLICABLE"));
                }
            }
            if (taxApplicable != null && taxApplicable.equals("Y")) {
                removeServiceTaxDetails();
            }
        }
        obj = null;
    }

    public boolean moreThanOneCreditNotAllowed(String prodType) {
        TxTransferTO to = new TxTransferTO();
        HashMap recurringMap = new HashMap();
        int count = 0;
        int debitList = 0;
        boolean val = false;
        for (int i = 0; i < transferTOs.size(); i++) {
            to = (TxTransferTO) transferTOs.get(i);
            if (to.getProdType().equals("TL") && to.getTransType().equals("CREDIT") || prodType.equals("TL")) {
                val = true;
            }
            if (to.getProdType().equals("TD") && to.getTransType().equals("CREDIT") || prodType.equals("TD")) {
                String prodId = "";
                if (prodType.equals("TD")) {
                    prodId = CommonUtil.convertObjToStr(productTypeModel.getKeyForSelected());
                } else {
                    prodId = CommonUtil.convertObjToStr(to.getProdId());
                }
                recurringMap.put("PROD_ID", prodId);
                List lst = ClientUtil.executeQuery("getBehavesLikeForDeposit", recurringMap);
                if (lst != null && lst.size() > 0) {
                    recurringMap = (HashMap) lst.get(0);
                    if (recurringMap.get("BEHAVES_LIKE").equals("RECURRING")) {
                        val = true;
                    }
                }
            }
            if (to.getTransType().equals("CREDIT")) {  //&& to.getProdType().equals("TL")
                count++;
            }
            if (to.getTransType().equals("DEBIT")) {
                debitList++;
            }
        }
//        if(val && (count>=1 ||debitList>1)){ commented by abi multiploe credit needed for loan & deposit
//            if(count>=1){
//                if( to.getProdType().equals("TL") && to.getTransType().equals("CREDIT") || prodType.equals("TL")){
//                    ClientUtil.showMessageWindow("More than OneCredit is not Allowed for Term Loans");
//                }else if( to.getProdType().equals("TD") && to.getTransType().equals("CREDIT") || prodType.equals("TD")
//                && recurringMap.get("BEHAVES_LIKE").equals("RECURRING")){
//                    ClientUtil.showMessageWindow("More than OneCredit is not Allowed for Recurring Installments...");  
//                }
//            }
//            if(debitList>1){
//                ClientUtil.showMessageWindow("More than OneDebit is not Allowed for Term Loans");
//            }
//            return true;
//        }else
        return false;
    }

    public String getTermLoanBatch_id() {
        for (int i = 0; i < transferTOs.size(); i++) {
            TxTransferTO to = (TxTransferTO) transferTOs.get(i);
            if (to.getLinkBatchId() != null && to.getLinkBatchId().length() > 0) {
                if (to.getProdType().equals("GL") || ((to.getProdType().equals("AD") || to.getProdType().equals("TL")) && to.getTransType().equals("CREDIT"))) {
                    HashMap map = new HashMap();
                    String link = (String) to.getLinkBatchId();
                    map.put("WHERE", link);
                    HashMap maps = new HashMap();
                    maps.put("WHERE", map);
                    List lst = ClientUtil.executeQuery("getLastIntCalDateAD", maps);
                    if (lst != null && lst.size() > 0) {
                        return to.getBatchId();
                    }
                }
            }
        }
        return "";
    }

    public int insertTransferData(int rowNo) {
        // For value Date
        if (valueDateMap == null) {
            valueDateMap = new HashMap();
        }
        if(getLblServiceTaxval()!=null && CommonUtil.convertObjToDouble(getLblServiceTaxval())>0){
            servicTaxDetList.add(setServiceTaxDetails());
        }
        // End For Value Date
        ReconciliationTO reconciliationTO = new ReconciliationTO();
        TxTransferTO obj = null;
        if (isRdoBulkTransaction_Yes() && rowNo == -1 && getOperation() == ClientConstants.ACTIONTYPE_NEW && isBulkUploadTxtFile) {
            obj = this.getTxTransferTO();
        }else if (isRdoBulkTransaction_Yes() && rowNo == -1 && getOperation() == ClientConstants.ACTIONTYPE_NEW) {
            if (this.getTxTransferTOBulkTransaction()) {
                return 1;
            } else {
                return 0;
            }
        } else {
            obj = this.getTxTransferTO();
        }
        if (getOperation() == ClientConstants.ACTIONTYPE_NEW && !getReconcile().equals("") && getReconcile().equals("Y")) {
            reconciliationTO.setAcHdId(getAccountHeadId());
            reconciliationTO.setTransAmount(obj.getAmount());
            reconciliationTO.setBalanceAmount(obj.getAmount());
            reconciliationTO.setTransType(getTransType());
            reconciliationTO.setTransMode(obj.getTransMode());
        }
        if (rowNo == -1) {
            if (this.getBatchId() == null || this.getBatchId().equalsIgnoreCase("")) {
                obj.setBatchId("-");
            }
            obj.setTransId("-");
            obj = setTOStatus(obj);
            
            transferTOs.add(0, obj);
            
            if (!reconciliationTO.getAcHdId().equals("") && reconciliationTO.getAcHdId().length() > 0) {
                reconciliationList.add(reconciliationTO);
                setReconcileTOStatus(reconciliationTO);
            }
            valueDateMap.put(String.valueOf(transferTOs.size() - 1), getValueDate());
            ArrayList irRow = this.setRow(obj);
            tbmTransfer.insertRow(0, irRow);//tbmTransfer.getRowCount()
            if (glserviceTax_Map != null && glserviceTax_Map.containsKey("GL_TRANS")  && getLblServiceTaxval() != null && CommonUtil.convertObjToDouble(getLblServiceTaxval()) > 0 && (getMainProductTypeValue().equals("GL") || getMainProductTypeValue().equals("TD"))) {
                tbmTransfer.fireTableDataChanged();
                int cunt = transferTOs.size();
                obj = this.getTxTransferTO();
                transferTOs.add(cunt, obj);
                obj.setAmount(CommonUtil.convertObjToDouble(getLblServiceTaxval()));
                obj.setInpAmount(CommonUtil.convertObjToDouble(getLblServiceTaxval()));
                obj.setAcHdId(CommonUtil.convertObjToStr(glserviceTax_Map.get("TAX_HEAD_ID")));
                obj.setParticulars("Service Tax Recived" + obj.getActNum());
                obj.setStatus("");
                transferTOs.set(cunt, obj);
                valueDateMap.put(String.valueOf(transferTOs.size() - 1), getValueDate());
                irRow = this.setRow(obj);
                tbmTransfer.insertRow(0, irRow);
            }
            
        } else {
            obj = updateTransferTO((TxTransferTO) transferTOs.get(rowNo), obj);
            if (obj.getBatchId().compareToIgnoreCase("-") != 0) {
                obj = setTOStatus(obj);
            }
            ArrayList irRow = setRow(obj);
            if (obj.getTransId().equals("-")) {
                obj.setStatus(CommonConstants.STATUS_CREATED);
            }
            transferTOs.set(rowNo, obj);
            
            valueDateMap.put(String.valueOf(rowNo), getValueDate());
            tbmTransfer.removeRow(rowNo);
            tbmTransfer.insertRow(rowNo, irRow);
        }
        tbmTransfer.fireTableDataChanged();
        obj = null;
        return 0;
    }

    public TxTransferTO setTOStatus(TxTransferTO obj) {
        obj.setStatus(this.getTransStatus());
        obj.setStatusBy(TrueTransactMain.USER_ID);
        obj.setStatusDt(curDate);
        obj.setScreenName(getScreen());

        if (this.getTransStatus().equalsIgnoreCase(CommonConstants.STATUS_CREATED)) {
            obj.setBranchId(getSelectedBranchID());
            obj.setInitiatedBranch(TrueTransactMain.BRANCH_ID);
            obj.setInitTransId(TrueTransactMain.USER_ID);
            obj.setInitChannType(TrueTransactMain.BRANCH_ID);
        }
        return obj;
    }

    public ReconciliationTO setReconcileTOStatus(ReconciliationTO obj) {
        obj.setStatus(this.getTransStatus());
        obj.setStatusBy(TrueTransactMain.USER_ID);
        obj.setStatusDt(curDate);
        if (this.getTransStatus().equalsIgnoreCase(CommonConstants.STATUS_CREATED)) {
            obj.setBranchId(getSelectedBranchID());
            obj.setInitiatedBranch(TrueTransactMain.BRANCH_ID);
        }
        return obj;
    }

    private TxTransferTO updateTransferTO(TxTransferTO oldTO, TxTransferTO newTO) {
        oldTO.setAmount(newTO.getAmount());
        oldTO.setInpAmount(newTO.getInpAmount());
        oldTO.setInpCurr(newTO.getInpCurr());
        oldTO.setInstDt(newTO.getInstDt());
        oldTO.setInstType(newTO.getInstType());
        oldTO.setInstrumentNo1(newTO.getInstrumentNo1());
        oldTO.setInstrumentNo2(newTO.getInstrumentNo2());
        oldTO.setParticulars(newTO.getParticulars());
        oldTO.setNarration(newTO.getNarration());
        oldTO.setActNum(newTO.getActNum());
        oldTO.setTransMode(newTO.getTransMode());
        //Wrongly setting prodtype,prodid,branchcode,achdid because of oldto these values are not setting at the time of clicking table.
        oldTO.setProdType(newTO.getProdType());
        oldTO.setProdId(newTO.getProdId());
        oldTO.setAcHdId(newTO.getAcHdId());
        //modified by rishad 04-06-2015 for mantis 0010681: While Editiing transanction in Transfer screen, Branch ID becomes 'NULL'
       // oldTO.setBranchId(newTO.getBranchId());
          oldTO.setBranchId(getSelectedBranchID());
        
        newTO = null;
        return oldTO;
    }

    public void resetTable() {

        this.tbmTransfer.setData(new ArrayList());
        this.tbmTransfer.fireTableDataChanged();
        this.transferTOs.clear();
        this.reconciliationList.clear();
        this.deleteTOsList.clear();
        this.oldAmountMap.clear();

    }

    // The following method added by Rajesh.
    // To check any interbranch transactions happened or not.
    public boolean checkForInterBranchTransExistance() {
        boolean isInterBranchTrans = false;
        if (getTransferTOs() != null && getTransferTOs().size() > 0) {
            for (int i = 0; i < getTransferTOs().size(); i++) {
                TxTransferTO objTxTransferTO = (TxTransferTO) getTransferTOs().get(i);
                if (objTxTransferTO != null) {
                    if (!objTxTransferTO.getBranchId().equals(TrueTransactMain.BRANCH_ID)) {
                        isInterBranchTrans = true;
                    }
                }
                objTxTransferTO = null;
            }
        }
        return isInterBranchTrans;
    }

    /**
     * Getter for property transferTOs.
     *
     * @return Value of property transferTOs.
     *
     */
    public ArrayList getTransferTOs() {
        return transferTOs;
    }

    /**
     * Setter for property transferTOs.
     *
     * @param transferTOs New value of property transferTOs.
     *
     */
    public void setTransferTOs(ArrayList transferTOs) {
        this.transferTOs = transferTOs;
    }

    /**
     * Getter for property deleteTOsList.
     *
     * @return Value of property deleteTOsList.
     *
     */
    public ArrayList getDeleteTOsList() {
        return deleteTOsList;
    }

    /**
     * Setter for property deleteTOsList.
     *
     * @param deleteTOsList New value of property deleteTOsList.
     *
     */
    public void setDeleteTOsList(ArrayList deleteTOsList) {
        this.deleteTOsList = deleteTOsList;
    }

    /**
     * Getter for property tbmTransfer.
     *
     * @return Value of property tbmTransfer.
     *
     */
    public TableModel getTbmTransfer() {
        return tbmTransfer;
    }

    /**
     * Setter for property tbmTransfer.
     *
     * @param tbmTransfer New value of property tbmTransfer.
     *
     */
    public void setTbmTransfer(TableModel tbmTransfer) {
        this.tbmTransfer = tbmTransfer;
    }

    public void checkOAbalanceForTL() {
        boolean oaExist;
        if (isCheckDebitTermLoan()) {
            String actNum;
            if (getLoanActNo().lastIndexOf("_") != -1) {
                actNum = getLoanActNo().substring(0, getLoanActNo().lastIndexOf("_"));
            } else {
                actNum = getLoanActNo();
            }
            HashMap checkClearBalance = new HashMap();
            checkClearBalance.put("ACT_NUM", actNum);
            List lst = ClientUtil.executeQuery("getClearBalance", checkClearBalance);
            if (lst != null && lst.size() > 0) {
                checkClearBalance = (HashMap) lst.get(0);
                long clearBalance = CommonUtil.convertObjToLong(checkClearBalance.get("CLEAR_BALANCE"));
                if (clearBalance == 0) {
                    if (getTransAmt().length() > 0) {
                        oaExist = checkForOA();
                        //
                    }
                }
                checkClearBalance = null;
            }
            lst = null;
        }

    }

    public int populateTransfer(String transId) {
        TxTransferTO obj;
        int size = this.transferTOs.size();
        for (int i = 0; i < size; i++) {
            obj = (TxTransferTO) transferTOs.get(i);
            if (obj.getTransId().equals(transId)) {
                populatTranferTO(i);
                obj = null;
                return i;
            }
        }
        return -1;
    }
    
    public void getTransDetails() {
        TxTransferTO obj = null;
        int size = this.transferTOs.size();
        double crAmt = 0, dbAmt = 0;
        int crInst = 0, dbInst = 0;
        for (int i = 0; i < size; i++) {
            obj = (TxTransferTO) this.transferTOs.get(i);
            if (obj.getTransType().equalsIgnoreCase(CommonConstants.DEBIT)) {
                dbAmt += obj.getAmount().doubleValue();
                long dr = roundOff((long) (dbAmt * 1000));
                dbAmt = dr / 100.0;
                dbInst += 1;
            } else if (obj.getTransType().equalsIgnoreCase(CommonConstants.CREDIT)) {
                crAmt += obj.getAmount().doubleValue();
                long cr = roundOff((long) (crAmt * 1000));
                crAmt = cr / 100.0;
                crInst += 1;
            }
        }
        setTotalDrInstruments(new Integer(dbInst).intValue());
        setTotalDrAmount(new Double(dbAmt).doubleValue());
        setTotalCrInstruments(new Integer(crInst).intValue());
        setTotalCrAmount(new Double(crAmt).doubleValue());

    }

    private long roundOff(long amt) {
        long amount = amt / 10;
//        int lastDigit = (int)amt%10;
        int lastDigit = (int) (amt % 10);  //() brackets added because sometimes returns 8 if 0 also.
        if (lastDigit > 5) {
            amount++;
        }
        return amount;
    }

    public boolean isAccountNoExists(String actNum, boolean isGL) {
        TxTransferTO objTO;
        int size = this.transferTOs.size();
        String checkActNum = "";
        boolean isExists = false;
        for (int i = 0; i < size; i++) {
            objTO = (TxTransferTO) transferTOs.get(i);
            checkActNum = isGL ? objTO.getAcHdId() : objTO.getActNum();
            if (actNum.equals(checkActNum)) {
                isExists = true;
                break;
            }
        }
        objTO = null;
        return isExists;
    }
    //Added by sreekrishnan for pending authorization checking
    public boolean isAuthorizationPending(String actNum) {
        boolean isPending = false;
        HashMap transMap = new HashMap();
        transMap.put("LINK_BATCH_ID", actNum);
        transMap.put("TRANS_DT", curDate);
        transMap.put("INITIATED_BRANCH", ProxyParameters.BRANCH_ID);
        List pendingList = ClientUtil.executeQuery("getPendingTransactionTransferAD", transMap);
        if (pendingList != null && pendingList.size() > 0) {
            HashMap hashTrans = (HashMap) pendingList.get(0);
            String trans_actnum = CommonUtil.convertObjToStr(hashTrans.get("ACT_NUM"));
            if (trans_actnum.equals(actNum)) {
                //ClientUtil.showMessageWindow(" There is Pending Transaction please Authorize OR Reject first  ");
                isPending = true;
                hashTrans = null;
                pendingList = null;
            }
        }
        return isPending;
    }
    
    public void authorize(String remarks) {
        authorizeMap = new HashMap();
        authorizeMap.put("BATCH_ID", this.getBatchId());
        authorizeMap.put("REMARKS", remarks);
        //check screen lock
        HashMap authDataMap = new HashMap();
        authDataMap.put("TRANS_ID", this.getBatchId());
        authDataMap.put("USER_ID", ProxyParameters.USER_ID);
        authDataMap.put("TRANS_DT", curDate);
        authDataMap.put("INITIATED_BRANCH", ProxyParameters.BRANCH_ID);
        List lst = ClientUtil.executeQuery("selectauthorizationLock", authDataMap);
        StringBuffer open = new StringBuffer();
        if (lst != null && lst.size() > 0) {
            for (int i = 0; i < lst.size(); i++) {
                HashMap map = (HashMap) lst.get(i);
                open.append("\n" + "User Id  :" + " ");
                open.append(CommonUtil.convertObjToStr(map.get("OPEN_BY")) + "\n");
                open.append("Mode Of Operation  :" + " ");
                open.append(CommonUtil.convertObjToStr(map.get("MODE_OF_OPERATION")) + " ");
            }
            ClientUtil.showMessageWindow("Already opened by" + open);
            return;
        }
        authorizeMap.put(CommonConstants.AUTHORIZESTATUS, this.getAuthorizeStatus1());
        double penalAmt = CommonUtil.convertObjToDouble(getDepositPenalAmt()).doubleValue();
        if (penalAmt > 0) {
            authorizeMap.put("DEPOSIT_PENAL_AMT", String.valueOf(penalAmt));
            authorizeMap.put("DEPOSIT_PENAL_MONTH", getDepositPenalMonth());
        }
        setActNumBlankForGL();
        authorizeMap.put(CommonConstants.AUTHORIZEDATA, this.transferTOs);

        // The following if condition added by Rajesh.
        if (checkForInterBranchTransExistance()) {
            authorizeMap.put("INTER_BRANCH_TRANS", new Boolean(true));
        }

        if (this.operation == ClientConstants.ACTIONTYPE_AUTHORIZE) {
            if (!this.isBatchTalliedArrayList()) {
                resourceBundle = new TransferRB();
                COptionPane.showMessageDialog(null, resourceBundle.getString("BATCH_TALLY"));
                this.setResult(ClientConstants.ACTIONTYPE_FAILED);
                return;
            }
        }
//        boolean depauth = depositAuthorizationValidation(transferTOs);
//        if(depauth == false)
        doAction(null);
    }

    private String getAuthorizeStatus1() {
        if (this.getOperation() == ClientConstants.ACTIONTYPE_AUTHORIZE) {
            return CommonConstants.STATUS_AUTHORIZED;
        } else if (this.getOperation() == ClientConstants.ACTIONTYPE_EXCEPTION) {
            return CommonConstants.STATUS_EXCEPTION;
        } else if (this.getOperation() == ClientConstants.ACTIONTYPE_REJECT) {
            return CommonConstants.STATUS_REJECTED;
        }
        return "";
    }

    public long getNearest(long number, long roundingFactor) {
        long roundingFactorOdd = roundingFactor;
        if ((roundingFactor % 2) != 0) {
            roundingFactorOdd += 1;
        }
        long mod = number % roundingFactor;
        if ((mod <= (roundingFactor / 2)) || (mod <= (roundingFactorOdd / 2))) {
            return lower(number, roundingFactor);
        } else {
            return higher(number, roundingFactor);
        }
    }

    public long lower(long number, long roundingFactor) {
        long mod = number % roundingFactor;
        return number - mod;
    }

    public long higher(long number, long roundingFactor) {
        long mod = number % roundingFactor;
        if (mod == 0) {
            return number;
        }
        return (number - mod) + roundingFactor;
    }

    /**
     * Getter for property authorizeMap.
     *
     * @return Value of property authorizeMap.
     *
     */
    public HashMap getAuthorizeMap() {
        return authorizeMap;
    }

    /**
     * Setter for property authorizeMap.
     *
     * @param authorizeMap New value of property authorizeMap.
     *
     */
    public void setAuthorizeMap(HashMap authorizeMap) {
        this.authorizeMap = authorizeMap;
    }

    /**
     * Getter for property oldAmountMap.
     *
     * @return Value of property oldAmountMap.
     *
     */
    public HashMap getOldAmountMap() {
        return oldAmountMap;
    }

    /**
     * Setter for property oldAmountMap.
     *
     * @param oldAmountMap New value of property oldAmountMap.
     *
     */
    public void setOldAmountMap(HashMap oldAmountMap) {
        this.oldAmountMap = oldAmountMap;
    }

    /**
     * Getter for property mainProductTypeValue.
     *
     * @return Value of property mainProductTypeValue.
     *
     */
    public java.lang.String getMainProductTypeValue() {
        return mainProductTypeValue;
    }

    /**
     * Setter for property mainProductTypeValue.
     *
     * @param mainProductTypeValue New value of property mainProductTypeValue.
     *
     */
    public void setMainProductTypeValue(java.lang.String mainProductTypeValue) {
        this.mainProductTypeValue = mainProductTypeValue;
    }

    /**
     * Getter for property mainProductTypeModel.
     *
     * @return Value of property mainProductTypeModel.
     *
     */
    public com.see.truetransact.clientutil.ComboBoxModel getMainProductTypeModel() {
        return mainProductTypeModel;
    }

    /**
     * Setter for property mainProductTypeModel.
     *
     * @param mainProductTypeModel New value of property mainProductTypeModel.
     *
     */
    public void setMainProductTypeModel(com.see.truetransact.clientutil.ComboBoxModel mainProductTypeModel) {
        this.mainProductTypeModel = mainProductTypeModel;
    }

    public void getAccountHead() {
        HashMap whereMap = new HashMap();
        whereMap.put("ACHDID", getAccountHeadId());
        String mapName = "getSelectAcctHeadDesc";
        List list = ClientUtil.executeQuery(mapName, whereMap);
        if (list != null && list.size() > 0) {
            setAccountHeadDesc((String) ((HashMap) list.get(0)).get("AC_HD_DESC"));
        }
    }

    /**
     * Getter for property transAmt.
     *
     * @return Value of property transAmt.
     */
    public java.lang.String getTransAmt() {
        return transAmt;
    }

    /**
     * Setter for property transAmt.
     *
     * @param transAmt New value of property transAmt.
     */
    public void setTransAmt(java.lang.String transAmt) {
        this.transAmt = transAmt;
    }

    /**
     * Getter for property loanActNo.
     *
     * @return Value of property loanActNo.
     */
    public java.lang.String getLoanActNo() {
        return loanActNo;
    }

    /**
     * Setter for property loanActNo.
     *
     * @param loanActNo New value of property loanActNo.
     */
    public void setLoanActNo(java.lang.String loanActNo) {
        this.loanActNo = loanActNo;
    }

    /**
     * Getter for property transDepositAmt.
     *
     * @return Value of property transDepositAmt.
     */
    public java.lang.String getTransDepositAmt() {
        return transDepositAmt;
    }

    /**
     * Setter for property transDepositAmt.
     *
     * @param transDepositAmt New value of property transDepositAmt.
     */
    public void setTransDepositAmt(java.lang.String transDepositAmt) {
        this.transDepositAmt = transDepositAmt;
    }

    /**
     * Getter for property checkDebitTermLoan.
     *
     * @return Value of property checkDebitTermLoan.
     */
    public boolean isCheckDebitTermLoan() {
        return checkDebitTermLoan;
    }

    /**
     * Setter for property checkDebitTermLoan.
     *
     * @param checkDebitTermLoan New value of property checkDebitTermLoan.
     */
    public void setCheckDebitTermLoan(boolean checkDebitTermLoan) {
        this.checkDebitTermLoan = checkDebitTermLoan;
    }

    /**
     * Getter for property ALL_LOAN_AMOUNT.
     *
     * @return Value of property ALL_LOAN_AMOUNT.
     */
    public java.util.HashMap getALL_LOAN_AMOUNT() {
        return ALL_LOAN_AMOUNT;
    }

    /**
     * Setter for property ALL_LOAN_AMOUNT.
     *
     * @param ALL_LOAN_AMOUNT New value of property ALL_LOAN_AMOUNT.
     */
    public void setALL_LOAN_AMOUNT(java.util.HashMap ALL_LOAN_AMOUNT) {
        this.ALL_LOAN_AMOUNT = ALL_LOAN_AMOUNT;
    }

    /**
     * Getter for property depositTransId.
     *
     * @return Value of property depositTransId.
     */
    public java.lang.String getDepositTransId() {
        return depositTransId;
    }

    /**
     * Setter for property depositTransId.
     *
     * @param depositTransId New value of property depositTransId.
     */
    public void setDepositTransId(java.lang.String depositTransId) {
        this.depositTransId = depositTransId;
    }

    /**
     * Getter for property interestAmt.
     *
     * @return Value of property interestAmt.
     */
    public double getInterestAmt() {
        return interestAmt;
    }

    /**
     * Setter for property interestAmt.
     *
     * @param interestAmt New value of property interestAmt.
     */
    public void setInterestAmt(double interestAmt) {
        this.interestAmt = interestAmt;
    }

    /**
     * Getter for property debitLoanType.
     *
     * @return Value of property debitLoanType.
     */
    public java.util.HashMap getDebitLoanType() {
        return debitLoanType;
    }

    /**
     * Setter for property debitLoanType.
     *
     * @param debitLoanType New value of property debitLoanType.
     */
    public void setDebitLoanType(java.util.HashMap debitLoanType) {
        this.debitLoanType = debitLoanType;
    }

    /**
     * Getter for property depInterestAmt.
     *
     * @return Value of property depInterestAmt.
     */
    public java.lang.String getDepInterestAmt() {
        return depInterestAmt;
    }

    /**
     * Setter for property depInterestAmt.
     *
     * @param depInterestAmt New value of property depInterestAmt.
     */
    public void setDepInterestAmt(java.lang.String depInterestAmt) {
        this.depInterestAmt = depInterestAmt;
    }

    /**
     * Getter for property depLinkBatchId.
     *
     * @return Value of property depLinkBatchId.
     */
    public java.lang.String getDepLinkBatchId() {
        return depLinkBatchId;
    }

    /**
     * Setter for property depLinkBatchId.
     *
     * @param depLinkBatchId New value of property depLinkBatchId.
     */
    public void setDepLinkBatchId(java.lang.String depLinkBatchId) {
        this.depLinkBatchId = depLinkBatchId;
    }

    /**
     * Getter for property depAccNO.
     *
     * @return Value of property depAccNO.
     */
    public java.lang.String getDepAccNO() {
        return depAccNO;
    }

    /**
     * Setter for property depAccNO.
     *
     * @param depAccNO New value of property depAccNO.
     */
    public void setDepAccNO(java.lang.String depAccNO) {
        this.depAccNO = depAccNO;
    }

    /**
     * Getter for property renewDepAmt.
     *
     * @return Value of property renewDepAmt.
     */
    public java.lang.String getRenewDepAmt() {
        return renewDepAmt;
    }

    /**
     * Setter for property renewDepAmt.
     *
     * @param renewDepAmt New value of property renewDepAmt.
     */
    public void setRenewDepAmt(java.lang.String renewDepAmt) {
        this.renewDepAmt = renewDepAmt;
    }

    /**
     * Getter for property linkMap.
     *
     * @return Value of property linkMap.
     */
    public java.util.HashMap getLinkMap() {
        return linkMap;
    }

    /**
     * Setter for property linkMap.
     *
     * @param linkMap New value of property linkMap.
     */
    public void setLinkMap(java.util.HashMap linkMap) {
        this.linkMap = linkMap;
    }

    /**
     * Getter for property depositPenalAmt.
     *
     * @return Value of property depositPenalAmt.
     */
    public java.lang.String getDepositPenalAmt() {
        return depositPenalAmt;
    }

    /**
     * Setter for property depositPenalAmt.
     *
     * @param depositPenalAmt New value of property depositPenalAmt.
     */
    public void setDepositPenalAmt(java.lang.String depositPenalAmt) {
        this.depositPenalAmt = depositPenalAmt;
    }

    /**
     * Getter for property depositPenalMonth.
     *
     * @return Value of property depositPenalMonth.
     */
    public java.lang.String getDepositPenalMonth() {
        return depositPenalMonth;
    }

    /**
     * Setter for property depositPenalMonth.
     *
     * @param depositPenalMonth New value of property depositPenalMonth.
     */
    public void setDepositPenalMonth(java.lang.String depositPenalMonth) {
        this.depositPenalMonth = depositPenalMonth;
    }

    /**
     * Getter for property penalMap.
     *
     * @return Value of property penalMap.
     */
    public java.util.HashMap getPenalMap() {
        return penalMap;
    }

    /**
     * Setter for property penalMap.
     *
     * @param penalMap New value of property penalMap.
     */
    public void setPenalMap(java.util.HashMap penalMap) {
        this.penalMap = penalMap;
    }

    /**
     * Getter for property authorizeRemarks.
     *
     * @return Value of property authorizeRemarks.
     */
    public java.lang.String getAuthorizeRemarks() {
        return authorizeRemarks;
    }

    /**
     * Setter for property authorizeRemarks.
     *
     * @param authorizeRemarks New value of property authorizeRemarks.
     */
    public void setAuthorizeRemarks(java.lang.String authorizeRemarks) {
        this.authorizeRemarks = authorizeRemarks;
    }

    /**
     * Getter for property creatingFlexi.
     *
     * @return Value of property creatingFlexi.
     */
    public java.lang.String getCreatingFlexi() {
        return creatingFlexi;
    }

    /**
     * Setter for property creatingFlexi.
     *
     * @param creatingFlexi New value of property creatingFlexi.
     */
    public void setCreatingFlexi(java.lang.String creatingFlexi) {
        this.creatingFlexi = creatingFlexi;
    }

    /**
     * Getter for property flexiAmount.
     *
     * @return Value of property flexiAmount.
     */
    public double getFlexiAmount() {
        return flexiAmount;
    }

    /**
     * Setter for property flexiAmount.
     *
     * @param flexiAmount New value of property flexiAmount.
     */
    public void setFlexiAmount(double flexiAmount) {
        this.flexiAmount = flexiAmount;
    }

    /**
     * Getter for property curDate.
     *
     * @return Value of property curDate.
     */
    public java.util.Date getCurDate() {
        return curDate;
    }

    /**
     * Setter for property curDate.
     *
     * @param curDate New value of property curDate.
     */
    public void setCurDate(java.util.Date curDate) {
        this.curDate = curDate;
    }

    /**
     * Getter for property valueDate.
     *
     * @return Value of property valueDate.
     */
    public java.util.Date getValueDate() {
        return valueDate;
    }

    /**
     * Setter for property valueDate.
     *
     * @param valueDate New value of property valueDate.
     */
    public void setValueDate(java.util.Date valueDate) {
        this.valueDate = valueDate;
    }

    public double getPenalWaiveAmount() {
        return penalWaiveAmount;
    }

    public void setPenalWaiveAmount(double penalWaiveAmount) {
        this.penalWaiveAmount = penalWaiveAmount;
    }

    public void checkForValueDate() {
        if (valueDateMap != null) {
            ArrayList txList = new ArrayList();
            txList = (ArrayList) getTransferTOs();
            if (txList != null && txList.size() > 0) {//as an when cusstomer credit loan account moore than one credit is not allowed
                TxTransferTO to = new TxTransferTO();
                HashMap recurringMap = new HashMap();
                int count = 0;
                int debitList = 0;
                boolean val = false;
                Date valDate = null;
                Date trnDate = null;
                for (int i = 0; i < transferTOs.size(); i++) {
                    to = (TxTransferTO) transferTOs.get(i);
                    trnDate = to.getTransDt();
                    valDate = (Date) valueDateMap.get(String.valueOf(i));
                    if (trnDate != null && valDate != null && DateUtil.dateDiff(trnDate, valDate) != 0) {
                        valueDateFlag = true;
                    }
                }
            }
            //            valueDtMap.get(
        }
    }

    public String getAcHdID(int rowNum) {
        TxTransferTO obj = (TxTransferTO) transferTOs.get(rowNum);
        String acHdId = obj.getAcHdId();
        return acHdId;
    }

    /**
     * Getter for property reconcile.
     *
     * @return Value of property reconcile.
     */
    public java.lang.String getReconcile() {
        return reconcile;
    }

    /**
     * Setter for property reconcile.
     *
     * @param reconcile New value of property reconcile.
     */
    public void setReconcile(java.lang.String reconcile) {
        this.reconcile = reconcile;
    }

    /**
     * Getter for property balanceType.
     *
     * @return Value of property balanceType.
     */
    public java.lang.String getBalanceType() {
        return balanceType;
    }

    /**
     * Setter for property balanceType.
     *
     * @param balanceType New value of property balanceType.
     */
    public void setBalanceType(java.lang.String balanceType) {
        this.balanceType = balanceType;
    }

    /**
     * Getter for property initBran.
     *
     * @return Value of property initBran.
     */
    public java.lang.String getInitBran() {
        return initBran;
    }

    /**
     * Setter for property initBran.
     *
     * @param initBran New value of property initBran.
     */
    public void setInitBran(java.lang.String initBran) {
        this.initBran = initBran;
    }

    /**
     * Getter for property authBy.
     *
     * @return Value of property authBy.
     */
    public java.lang.String getAuthBy() {
        return authBy;
    }

    /**
     * Setter for property authBy.
     *
     * @param authBy New value of property authBy.
     */
    public void setAuthBy(java.lang.String authBy) {
        this.authBy = authBy;
    }

    /**
     * Getter for property depositPenalFlag.
     *
     * @return Value of property depositPenalFlag.
     */
    public boolean isDepositPenalFlag() {
        return depositPenalFlag;
    }

    /**
     * Setter for property depositPenalFlag.
     *
     * @param depositPenalFlag New value of property depositPenalFlag.
     */
    public void setDepositPenalFlag(boolean depositPenalFlag) {
        this.depositPenalFlag = depositPenalFlag;
    }

    /**
     * Getter for property emiNoInstallment.
     *
     * @return Value of property emiNoInstallment.
     */
    public long getEmiNoInstallment() {
        return emiNoInstallment;
    }

    /**
     * Setter for property emiNoInstallment.
     *
     * @param emiNoInstallment New value of property emiNoInstallment.
     */
    public void setEmiNoInstallment(long emiNoInstallment) {
        this.emiNoInstallment = emiNoInstallment;
    }

    /**
     * Getter for property rdoBulkTransaction_Yes.
     *
     * @return Value of property rdoBulkTransaction_Yes.
     */
    public boolean isRdoBulkTransaction_Yes() {
        return rdoBulkTransaction_Yes;
    }

    /**
     * Setter for property rdoBulkTransaction_Yes.
     *
     * @param rdoBulkTransaction_Yes New value of property
     * rdoBulkTransaction_Yes.
     */
    public void setRdoBulkTransaction_Yes(boolean rdoBulkTransaction_Yes) {
        this.rdoBulkTransaction_Yes = rdoBulkTransaction_Yes;
    }

    /**
     * Getter for property rdoBulkTransaction_No.
     *
     * @return Value of property rdoBulkTransaction_No.
     */
    public boolean isRdoBulkTransaction_No() {
        return rdoBulkTransaction_No;
    }

    /**
     * Setter for property rdoBulkTransaction_No.
     *
     * @param rdoBulkTransaction_No New value of property rdoBulkTransaction_No.
     */
    public void setRdoBulkTransaction_No(boolean rdoBulkTransaction_No) {
        this.rdoBulkTransaction_No = rdoBulkTransaction_No;
    }

    /**
     * Getter for property narration.
     *
     * @return Value of property narration.
     */
    public java.lang.String getNarration() {
        return narration;
    }

    /**
     * Setter for property narration.
     *
     * @param narration New value of property narration.
     */
    public void setNarration(java.lang.String narration) {
        this.narration = narration;
    }

    /**
     * Getter for property penalWaiveOff.
     *
     * @return Value of property penalWaiveOff.
     */
    public boolean isPenalWaiveOff() {
        return penalWaiveOff;
    }

    /**
     * Setter for property penalWaiveOff.
     *
     * @param penalWaiveOff New value of property penalWaiveOff.
     */
    public void setPenalWaiveOff(boolean penalWaiveOff) {
        this.penalWaiveOff = penalWaiveOff;
    }

    /**
     * Getter for property rebateInterest.
     *
     * @return Value of property rebateInterest.
     */
    public boolean isRebateInterest() {
        return rebateInterest;
    }

    /**
     * Setter for property rebateInterest.
     *
     * @param rebateInterest New value of property rebateInterest.
     */
    public void setRebateInterest(boolean rebateInterest) {
        this.rebateInterest = rebateInterest;
    }

    /**
     * Getter for property multiple_ALL_LOAN_AMOUNT.
     *
     * @return Value of property multiple_ALL_LOAN_AMOUNT.
     */
    public java.util.HashMap getMultiple_ALL_LOAN_AMOUNT() {
        return multiple_ALL_LOAN_AMOUNT;
    }

    /**
     * Setter for property multiple_ALL_LOAN_AMOUNT.
     *
     * @param multiple_ALL_LOAN_AMOUNT New value of property
     * multiple_ALL_LOAN_AMOUNT.
     */
    public void setMultiple_ALL_LOAN_AMOUNT(java.util.HashMap multiple_ALL_LOAN_AMOUNT) {
        this.multiple_ALL_LOAN_AMOUNT = multiple_ALL_LOAN_AMOUNT;
    }

    /**
     * Getter for property closedAccNo.
     *
     * @return Value of property closedAccNo.
     */
    public java.lang.String getClosedAccNo() {
        return closedAccNo;
    }

    /**
     * Setter for property closedAccNo.
     *
     * @param closedAccNo New value of property closedAccNo.
     */
    public void setClosedAccNo(java.lang.String closedAccNo) {
        this.closedAccNo = closedAccNo;
    }

    public boolean isHoAccount() {
        return hoAccount;
    }

    public void setHoAccountStatus(String hoAccountStatus) {
        this.hoAccountStatus = hoAccountStatus;
    }

    public String getHoAccountStatus() {
        return hoAccountStatus;
    }

    public void setHoAccount(boolean hoAccount) {
        this.hoAccount = hoAccount;
    }

    public ArrayList getOrgRespList() {
        return orgRespList;
    }

    public void setOrgRespList(ArrayList orgRespList) {
        this.orgRespList = orgRespList;
    }

    public String getAccNumGl() {
        return accNumGl;
    }

    public void setAccNumGl(String accNumGl) {
        this.accNumGl = accNumGl;
    }

    public String getLinkBatchIdForGl() {
        return linkBatchIdForGl;
    }

    public void setLinkBatchIdForGl(String linkBatchIdForGl) {
        this.linkBatchIdForGl = linkBatchIdForGl;
    }
    
    public boolean validationForInterbranchTxn() {
        TxTransferTO toList = new TxTransferTO();
        boolean interBranchFlag = false;
        for (int i = 0; i < transferTOs.size(); i++) {
            toList = (TxTransferTO) transferTOs.get(i);
            if (ProxyParameters.BRANCH_ID.equals(toList.getBranchId()) && ProxyParameters.BRANCH_ID.equals(toList.getInitiatedBranch())) {
                return interBranchFlag = false;
            } else {
                interBranchFlag = true;
            }
        }
        return interBranchFlag;
    }

   public boolean interbranchTxnTLAccountCount() {
        TxTransferTO toList = new TxTransferTO();
        boolean interBranchFlag = false;
        int interBranchTLCount = 0;
        for (int i = 0; i < transferTOs.size(); i++) {
            toList = (TxTransferTO) transferTOs.get(i);
            if (!ProxyParameters.BRANCH_ID.equals(toList.getBranchId()) && ProxyParameters.BRANCH_ID.equals(toList.getInitiatedBranch()) && 
            toList.getProdType().equals("TL")) {
                interBranchTLCount = interBranchTLCount++;
                if(interBranchTLCount>1){
                    return interBranchFlag = true;
                }else{
                    interBranchFlag = false;
                }
            } else {
                interBranchFlag = false;
            }
        }
        return interBranchFlag;
    }
    public ServiceTaxDetailsTO setServiceTaxDetails() {
        final ServiceTaxDetailsTO objservicetaxDetTo = new ServiceTaxDetailsTO();
        try {
            objservicetaxDetTo.setCommand("INSERT");
            if (getOperation() == ClientConstants.ACTIONTYPE_NEW) {
                objservicetaxDetTo.setStatus(CommonConstants.STATUS_CREATED);
            }
            if (getOperation() == ClientConstants.ACTIONTYPE_EDIT) {
                objservicetaxDetTo.setStatus(CommonConstants.STATUS_MODIFIED);
            }

            objservicetaxDetTo.setStatusBy(TrueTransactMain.USER_ID);
            objservicetaxDetTo.setAcct_Num(getAccNumGl());
            objservicetaxDetTo.setParticulars("Loan Application");
            objservicetaxDetTo.setBranchID(TrueTransactMain.BRANCH_ID);
            objservicetaxDetTo.setTrans_type("C");
            if (serviceTax_Map != null && serviceTax_Map.containsKey("SERVICE_TAX")) {
                objservicetaxDetTo.setServiceTaxAmt(CommonUtil.convertObjToDouble(serviceTax_Map.get("SERVICE_TAX")));
            }
            if (serviceTax_Map != null && serviceTax_Map.containsKey("EDUCATION_CESS")) {
                objservicetaxDetTo.setEducationCess(CommonUtil.convertObjToDouble(serviceTax_Map.get("EDUCATION_CESS")));
            }
            if (serviceTax_Map != null && serviceTax_Map.containsKey("HIGHER_EDU_CESS")) {
                objservicetaxDetTo.setHigherCess(CommonUtil.convertObjToDouble(serviceTax_Map.get("HIGHER_EDU_CESS")));
            }
            if (serviceTax_Map != null && serviceTax_Map.containsKey(ServiceTaxCalculation.SWACHH_CESS)) {
                objservicetaxDetTo.setSwachhCess(CommonUtil.convertObjToDouble(serviceTax_Map.get(ServiceTaxCalculation.SWACHH_CESS)));
            }
            if (serviceTax_Map != null && serviceTax_Map.containsKey(ServiceTaxCalculation.KRISHIKALYAN_CESS)) {
                objservicetaxDetTo.setKrishiKalyan(CommonUtil.convertObjToDouble(serviceTax_Map.get(ServiceTaxCalculation.KRISHIKALYAN_CESS)));
            }
            if (serviceTax_Map != null && serviceTax_Map.containsKey("TOT_TAX_AMT")) {
                objservicetaxDetTo.setTotalTaxAmt(CommonUtil.convertObjToDouble(serviceTax_Map.get("TOT_TAX_AMT")));
            }
            double roudVal = objservicetaxDetTo.getTotalTaxAmt() - (objservicetaxDetTo.getServiceTaxAmt() + objservicetaxDetTo.getEducationCess() + objservicetaxDetTo.getHigherCess() + objservicetaxDetTo.getSwachhCess() + objservicetaxDetTo.getKrishiKalyan());
            ServiceTaxCalculation serviceTaxObj = new ServiceTaxCalculation();
            objservicetaxDetTo.setRoundVal(CommonUtil.convertObjToStr(serviceTaxObj.roundOffAmtForRoundVal(roudVal)));
            objservicetaxDetTo.setStatusDt(curDate);
            objservicetaxDetTo.setCreatedBy(TrueTransactMain.USER_ID);
            objservicetaxDetTo.setCreatedDt(curDate);

        } catch (Exception e) {
            e.printStackTrace();
        }
        return objservicetaxDetTo;

    }
    public String getLblServiceTaxval() {
        return lblServiceTaxval;
    }

    public void setLblServiceTaxval(String lblServiceTaxval) {
        this.lblServiceTaxval = lblServiceTaxval;
    }

    public String getServiceTaxAccNo() {
        return serviceTaxAccNo;
    }

    public void setServiceTaxAccNo(String serviceTaxAccNo) {
        this.serviceTaxAccNo = serviceTaxAccNo;
    }

    public HashMap getServiceTax_Map() {
        return serviceTax_Map;
    }

    public void setServiceTax_Map(HashMap serviceTax_Map) {
        this.serviceTax_Map = serviceTax_Map;
    }

    public String getSingleTranId() {
        return singleTranId;
    }

    public void setSingleTranId(String singleTranId) {
        this.singleTranId = singleTranId;
    }

    public boolean isIsBulkUploadTxtFile() {
        return isBulkUploadTxtFile;
    }

    public void setIsBulkUploadTxtFile(boolean isBulkUploadTxtFile) {
        this.isBulkUploadTxtFile = isBulkUploadTxtFile;
    }

//    public double calcServiceTaxAmount(String accNum, String prodId) {
//        HashMap whereMap = new HashMap();
//        whereMap.put("ACT_NUM", accNum);
//        List chargeAmtList = ClientUtil.executeQuery("getChargeDetails", whereMap);
//        double taxAmt = 0;
//        if (chargeAmtList != null && chargeAmtList.size() > 0) {
//            String checkFlag = "N";
//            whereMap = new HashMap();
//            whereMap.put("value", prodId);
//            List accHeadList = ClientUtil.executeQuery("getSelectLoanProductAccHeadTO", whereMap);
//            if (accHeadList != null && accHeadList.size() > 0) {
//                for (int i = 0; i < chargeAmtList.size(); i++) {
//                    HashMap chargeMap = (HashMap) chargeAmtList.get(i);
//                    if (chargeMap != null && chargeMap.size() > 0) {
//
//                        String accId = "";
//
//                        LoanProductAccHeadTO accHeadObj = (LoanProductAccHeadTO) accHeadList.get(0);
//                        String chargetype = "";
//                        if (chargeMap.containsKey("CHARGE_TYPE")) {
//                            chargetype = CommonUtil.convertObjToStr(chargeMap.get("CHARGE_TYPE"));
//                        }
//                        if (chargetype != null && chargetype.equals("EP CHARGE")) {
//                            accId = CommonUtil.convertObjToStr(accHeadObj.getEpCost());
//                        }
//                        if (chargetype != null && chargetype.equals("ARC CHARGE")) {
//                            accId = CommonUtil.convertObjToStr(accHeadObj.getArcCost());
//                        }
//                        if (chargetype != null && chargetype.equals("MISCELLANEOUS CHARGES")) {
//                            accId = CommonUtil.convertObjToStr(accHeadObj.getMiscServChrg());
//                        }
//                        if (chargetype != null && chargetype.equals("POSTAGE CHARGES")) {
//                            accId = CommonUtil.convertObjToStr(accHeadObj.getPostageCharges());
//                        }
//                        if (chargetype != null && chargetype.equals("ADVERTISE CHARGES")) {
//                            accId = CommonUtil.convertObjToStr(accHeadObj.getAdvertisementHead());
//                        }
//                        if (chargetype != null && chargetype.equals("ARBITRARY CHARGES")) {
//                            accId = CommonUtil.convertObjToStr(accHeadObj.getArbitraryCharges());
//                        }
//                        if (chargetype != null && chargetype.equals("LEGAL CHARGES")) {
//                            accId = CommonUtil.convertObjToStr(accHeadObj.getLegalCharges());
//                        }
//                        if (chargetype != null && chargetype.equals("NOTICE CHARGES")) {
//                            accId = CommonUtil.convertObjToStr(accHeadObj.getNoticeCharges());
//                        }
//                        if (chargetype != null && chargetype.equals("INSURANCE CHARGES")) {
//                            accId = CommonUtil.convertObjToStr(accHeadObj.getInsuranceCharges());
//                        }
//                        if (chargetype != null && chargetype.equals("EXECUTION DECREE CHARGES")) {
//                            accId = CommonUtil.convertObjToStr(accHeadObj.getExecutionDecreeCharges());
//                        }
//                        checkFlag = checkServiceTaxApplicable(accId);
//                        if (checkFlag != null && checkFlag.equals("Y")) {
//                            String charge_amt = CommonUtil.convertObjToStr(chargeMap.get("CHARGE_AMT"));
//                            taxAmt = taxAmt + CommonUtil.convertObjToDouble(charge_amt);
//                        }
//                    }
//
//                }
//            }
//        }
//        return taxAmt;
//    }
    
    
    public List calcServiceTaxAmount(String accNum, String prodId) {
        HashMap whereMap = new HashMap();
        whereMap.put("ACT_NUM", accNum);
        List chargeAmtList = ClientUtil.executeQuery("getChargeDetails", whereMap);
        double taxAmt = 0;        
        HashMap taxMap;
        List taxSettingsList = new ArrayList();
        if (chargeAmtList != null && chargeAmtList.size() > 0) {
            String checkFlag = "N";
            HashMap checkForTaxMap = new HashMap();
            whereMap = new HashMap();
            whereMap.put("value", prodId);
            List accHeadList = ClientUtil.executeQuery("getSelectLoanProductAccHeadTO", whereMap);
            if (accHeadList != null && accHeadList.size() > 0) {
                for (int i = 0; i < chargeAmtList.size(); i++) {
                    HashMap chargeMap = (HashMap) chargeAmtList.get(i);
                    if (chargeMap != null && chargeMap.size() > 0) {

                        String accId = "";

                        LoanProductAccHeadTO accHeadObj = (LoanProductAccHeadTO) accHeadList.get(0);
                        String chargetype = "";
                        if (chargeMap.containsKey("CHARGE_TYPE")) {
                            chargetype = CommonUtil.convertObjToStr(chargeMap.get("CHARGE_TYPE"));
                        }
                        if (chargetype != null && chargetype.equals("EP CHARGE") && getEpCostWaiveAmount() == 0) {
                            accId = CommonUtil.convertObjToStr(accHeadObj.getEpCost());
                        }
                        if (chargetype != null && chargetype.equals("ARC CHARGE") && getArcWaiveAmount() == 0) {
                            accId = CommonUtil.convertObjToStr(accHeadObj.getArcCost());
                        }
                        if (chargetype != null && chargetype.equals("MISCELLANEOUS CHARGES") && getMiscellaneousWaiveAmount() == 0) {
                            accId = CommonUtil.convertObjToStr(accHeadObj.getMiscServChrg());
                        }
                        if (chargetype != null && chargetype.equals("POSTAGE CHARGES") && getPostageWaiveAmount() == 0) {
                            accId = CommonUtil.convertObjToStr(accHeadObj.getPostageCharges());
                        }
                        if (chargetype != null && chargetype.equals("ADVERTISE CHARGES") && getAdvertiseWaiveAmount() == 0) {
                            accId = CommonUtil.convertObjToStr(accHeadObj.getAdvertisementHead());
                        }
                        if (chargetype != null && chargetype.equals("ARBITRARY CHARGES") && getArbitarayWaivwAmount() == 0) {
                            accId = CommonUtil.convertObjToStr(accHeadObj.getArbitraryCharges());
                        }
                        if (chargetype != null && chargetype.equals("LEGAL CHARGES") && getLegalWaiveAmount() == 0) {
                            accId = CommonUtil.convertObjToStr(accHeadObj.getLegalCharges());
                        }
                        if (chargetype != null && chargetype.equals("NOTICE CHARGES") && getNoticeWaiveAmount() == 0) {
                            accId = CommonUtil.convertObjToStr(accHeadObj.getNoticeCharges());
                        }
                        if (chargetype != null && chargetype.equals("INSURANCE CHARGES") && getInsuranceWaiveAmont() == 0) {
                            accId = CommonUtil.convertObjToStr(accHeadObj.getInsuranceCharges());
                        }
                        if (chargetype != null && chargetype.equals("EXECUTION DECREE CHARGES") && getDecreeWaiveAmount() == 0) {
                            accId = CommonUtil.convertObjToStr(accHeadObj.getExecutionDecreeCharges());
                        }
                        // Added by nithya on 13-04-2018 for handling GST for other charges 
                        if (chargetype != null && chargetype.equals("OTHER CHARGES")) {
                            accId = CommonUtil.convertObjToStr(accHeadObj.getOthrChrgsHead());
                        }
                        if (chargetype != null && chargetype.equals("RECOVERY CHARGES") && getRecoveryWaiveAmount() == 0) {
                            accId = CommonUtil.convertObjToStr(accHeadObj.getRecoveryCharges());
                        }
                        if (chargetype != null && chargetype.equals("MEASUREMENT CHARGES") && getMeasurementWaiveAmount() == 0) {
                            accId = CommonUtil.convertObjToStr(accHeadObj.getMeasurementCharges());
                        }
                        
                        if (chargetype != null && chargetype.equals("KOLEFIELD EXPENSE") && getKoleFieldExpenseWaiveAmount() == 0) {
                            accId = CommonUtil.convertObjToStr(accHeadObj.getKoleFieldexpense());
                        }
                        
                        if (chargetype != null && chargetype.equals("KOLEFIELD OPERATION") && getKoleFieldOperationWaiveAmount() == 0) {
                            accId = CommonUtil.convertObjToStr(accHeadObj.getKoleFieldOperation());
                        }
                        
                        
                        checkForTaxMap = checkServiceTaxApplicable(accId);
                       if(checkForTaxMap.containsKey("SERVICE_TAX_APPLICABLE") && checkForTaxMap.get("SERVICE_TAX_APPLICABLE") != null && CommonUtil.convertObjToStr(checkForTaxMap.get("SERVICE_TAX_APPLICABLE")).equalsIgnoreCase("Y")){
                            if(checkForTaxMap.containsKey("SERVICE_TAX_ID") && checkForTaxMap.get("SERVICE_TAX_ID") != null && CommonUtil.convertObjToStr(checkForTaxMap.get("SERVICE_TAX_ID")).length() > 0){
                                if(CommonUtil.convertObjToDouble(chargeMap.get("CHARGE_AMT")) > 0){
                                   taxMap = new HashMap();
                                   taxMap.put("SETTINGS_ID",checkForTaxMap.get("SERVICE_TAX_ID"));
                                   taxMap.put(ServiceTaxCalculation.TOT_AMOUNT,chargeMap.get("CHARGE_AMT"));
                                   taxSettingsList.add(taxMap);   
                                }                                     
                            }
                        }
                    }

                }
            }
        }
        return taxSettingsList;
    }
    
    
//      public String checkServiceTaxApplicable(String accheadId) {
//        String checkFlag = "N";
//        if (TrueTransactMain.SERVICE_TAX_REQ.equals("Y")) {
//            HashMap whereMap = new HashMap();
//            whereMap.put("AC_HD_ID", accheadId);
//            List accHeadList = ClientUtil.executeQuery("getCheckServiceTaxApplicableForShare", whereMap);
//            if (accHeadList != null && accHeadList.size() > 0) {
//                HashMap accHeadMap = (HashMap) accHeadList.get(0);
//                if (accHeadMap != null && accHeadMap.containsKey("SERVICE_TAX_APPLICABLE")) {
//                    checkFlag = CommonUtil.convertObjToStr(accHeadMap.get("SERVICE_TAX_APPLICABLE"));
//                }
//            }
//        }
//        return checkFlag;
//    }
    
     public HashMap checkServiceTaxApplicable(String accheadId) {
        String checkFlag = "N";
        HashMap checkForTaxMap = new HashMap();
        if (TrueTransactMain.SERVICE_TAX_REQ.equals("Y")) {
            HashMap whereMap = new HashMap();
            whereMap.put("AC_HD_ID", accheadId);
            List accHeadList = ClientUtil.executeQuery("getCheckServiceTaxApplicableForShare", whereMap);           
            if (accHeadList != null && accHeadList.size() > 0) {
                HashMap accHeadMap = (HashMap) accHeadList.get(0);
                if (accHeadMap != null && accHeadMap.containsKey("SERVICE_TAX_APPLICABLE")&& accHeadMap.containsKey("SERVICE_TAX_ID")) {
                    checkFlag = CommonUtil.convertObjToStr(accHeadMap.get("SERVICE_TAX_APPLICABLE"));
                    checkForTaxMap.put("SERVICE_TAX_APPLICABLE",accHeadMap.get("SERVICE_TAX_APPLICABLE"));
                    checkForTaxMap.put("SERVICE_TAX_ID",accHeadMap.get("SERVICE_TAX_ID"));
                }
            }
        }
        return checkForTaxMap;
    }
    
       public HashMap getCompFreqRoundOffValues(String prod_id){
        HashMap transactionMap = new HashMap();
        HashMap retrieve = new HashMap();
        try{
            transactionMap.put("PROD_ID", prod_id);
            List resultList = ClientUtil.executeQuery("getCompFreqRoundOff_LoanProd", transactionMap);
            if (resultList.size() > 0){
                // If Product Account Head exist in Database
                retrieve = (HashMap) resultList.get(0);
            }
            transactionMap = null;
            resultList = null;
        }catch(Exception e){
            e.printStackTrace();
            parseException.logException(e,true);
        }
        return retrieve;
    }
    // Added by nithya for KD-391 - Daily Deposit- Here after saving the transaction, wrongly inserting a Cash Transaction without agent ID    
    public boolean isCreditToDailyDepositWithoutAgent() {
        boolean isCreditToDailyDeposit = false;
        String ddTrans = "";
        if (authorizeMap != null) {     
            //System.out.println("executing here :: "+ authorizeMap);
            ArrayList toList = new ArrayList();
            toList = (ArrayList) authorizeMap.get("AUTHORIZEDATA");
            for (int i = 0; i < toList.size(); i++) {
                TxTransferTO obj = (TxTransferTO) toList.get(i);
                if (obj.getProdType().equals("TD") && obj.getTransType().equals("CREDIT")) { 
                     // isCreditToDailyDeposit = true;
                    // getDailyDepositTransEntryRequiredForProd
                   HashMap checkMap = new HashMap();
                   checkMap.put("PROD_ID", obj.getProdId());
                   List depositProdList = ClientUtil.executeQuery("getDailyDepositTransEntryRequiredForProd", checkMap);
                   if(depositProdList != null && depositProdList.size() > 0){
                      checkMap = (HashMap)depositProdList.get(0);
                      ddTrans = CommonUtil.convertObjToStr(checkMap.get("DD_TRANS_ENTRY"));
                      if(ddTrans.equalsIgnoreCase("Y")){
                           isCreditToDailyDeposit = true;
                      }
                   }                  
                }
            }
        }
        return isCreditToDailyDeposit;
    }     
    
    public String getEMIinSimpleInterest() {
        return EMIinSimpleInterest;
    }

    public void setEMIinSimpleInterest(String EMIinSimpleInterest) {
        this.EMIinSimpleInterest = EMIinSimpleInterest;
    }

    public double getMeasurementWaiveAmount() {
        return measurementWaiveAmount;
    }

    public void setMeasurementWaiveAmount(double measurementWaiveAmount) {
        this.measurementWaiveAmount = measurementWaiveAmount;
    }

    public boolean isMeasurementWaiveOff() {
        return measurementWaiveOff;
    }

    public void setMeasurementWaiveOff(boolean measurementWaiveOff) {
        this.measurementWaiveOff = measurementWaiveOff;
    }

    public double getRecoveryWaiveAmount() {
        return recoveryWaiveAmount;
    }

    public void setRecoveryWaiveAmount(double recoveryWaiveAmount) {
        this.recoveryWaiveAmount = recoveryWaiveAmount;
    }

    public boolean isRecoveryWaiveOff() {
        return recoveryWaiveOff;
    }

    public void setRecoveryWaiveOff(boolean recoveryWaiveOff) {
        this.recoveryWaiveOff = recoveryWaiveOff;
    }
    
    
    private boolean checkSuspanceActBalCheck(String accNo) {
        HashMap wheMap = new HashMap();
        boolean balanceCheckRequired = true;
        wheMap.put("ACCT_NUM", accNo);
        List lt1 = ClientUtil.executeQuery("getNegativeAmtCheckForSA", wheMap);
        if (lt1 != null && lt1.size() > 0) {
            HashMap tMap = (HashMap) lt1.get(0);
            String negYn = CommonUtil.convertObjToStr(tMap.get("NEG_AMT_YN"));
            if(negYn.equals("Y")){
                balanceCheckRequired =  false;
            }
        }
        return balanceCheckRequired;
    }

    public boolean isKoleFieldOperationWaiveOff() {
        return koleFieldOperationWaiveOff;
    }

    public void setKoleFieldOperationWaiveOff(boolean koleFieldOperationWaiveOff) {
        this.koleFieldOperationWaiveOff = koleFieldOperationWaiveOff;
    }

    public boolean isKoleFieldExpenseWaiveOff() {
        return koleFieldExpenseWaiveOff;
    }

    public void setKoleFieldExpenseWaiveOff(boolean koleFieldExpenseWaiveOff) {
        this.koleFieldExpenseWaiveOff = koleFieldExpenseWaiveOff;
    }

    public double getKoleFieldOperationWaiveAmount() {
        return koleFieldOperationWaiveAmount;
    }

    public void setKoleFieldOperationWaiveAmount(double koleFieldOperationWaiveAmount) {
        this.koleFieldOperationWaiveAmount = koleFieldOperationWaiveAmount;
    }

    public double getKoleFieldExpenseWaiveAmount() {
        return koleFieldExpenseWaiveAmount;
    }

    public void setKoleFieldExpenseWaiveAmount(double koleFieldExpenseWaiveAmount) {
        this.koleFieldExpenseWaiveAmount = koleFieldExpenseWaiveAmount;
    }   

    public String getAccNumGlProdType() {
        return accNumGlProdType;
    }

    public void setAccNumGlProdType(String accNumGlProdType) {
        this.accNumGlProdType = accNumGlProdType;
    }
    
    
    
    
}