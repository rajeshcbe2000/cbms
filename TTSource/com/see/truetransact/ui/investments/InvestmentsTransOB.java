/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition.
 **
 *
 * ShareProductOB.java
 *
 * Created on Fri Jan 07 12:01:51 IST 2005
 */
package com.see.truetransact.ui.investments;

import com.see.truetransact.uicomponent.CObservable;
import com.see.truetransact.clientexception.ClientParseException;
import com.see.truetransact.commonutil.CommonConstants;
import com.see.truetransact.clientutil.ClientConstants;
import com.see.truetransact.commonutil.DateUtil;
import com.see.truetransact.clientproxy.ProxyFactory;
import com.see.truetransact.clientproxy.ProxyParameters;
import com.see.truetransact.clientutil.ComboBoxModel;
import com.see.truetransact.clientutil.ClientUtil;
import com.see.truetransact.transferobject.investments.InvestmentsMasterTO;
import com.see.truetransact.transferobject.investments.InvestmentsTransTO;
import com.see.truetransact.ui.TrueTransactMain;
import com.see.truetransact.commonutil.CommonUtil;
import com.see.truetransact.ui.common.transaction.TransactionOB;
import com.see.truetransact.transferobject.transaction.transfer.TxTransferTO;
import com.see.truetransact.clientutil.EnhancedTableModel;
//import com.see.truetransact.ui.deposit.TableManipulation;

import org.apache.log4j.Logger;

import java.util.HashMap;
import java.util.ArrayList;
import java.util.List;
import java.util.Date;
import java.util.ResourceBundle;
import java.util.LinkedHashMap;

/**
 *
 * @author Ashok Vijayakumar
 */
public class InvestmentsTransOB extends CObservable {

    Date curDate = ClientUtil.getCurrentDate();
    private final static ClientParseException parseException = ClientParseException.getInstance();
    private final static Logger _log = Logger.getLogger(InvestmentsMasterOB.class);
    private ResourceBundle resourceBundle = java.util.ResourceBundle.getBundle("com.see.truetransact.ui.investments.InvestmentsTransRB", ProxyParameters.LANGUAGE);
    private ProxyFactory proxy = null;
    private ComboBoxModel cbmInvestmentBehaves, cbmIntPayFreq, cbmInvestmentBehavesTrans, cbmInvestmentType, cbmInvestmentTypeSBorCA;
    private HashMap map, lookUpHash, keyValue, oldAmountMap;
    private int _result, _actionType;
    private ArrayList key, value, tblnvestmentTransDetColTitle;
    final ArrayList tableTitle = new ArrayList();
    private EnhancedTableModel tblInvestmentTransDet, tblCheckBookTable;
    private final String NOT_DELETED_TRANS_TOs = "NOT_DELETED_TRANS_TOs";
    private static InvestmentsTransOB objInvestmentsTransOB;//Singleton Object
    //     private static InvestmentsMasterOB objInvestmentsMasterOB;
    private TransactionOB transactionOB;
    private String lblStatus = ClientConstants.ACTION_STATUS[ClientConstants.ACTIONTYPE_CANCEL];
    Date currDt = null;
    private LinkedHashMap transactionDetailsTO;
    private LinkedHashMap deletedTransactionDetailsTO;
    private LinkedHashMap allowedTransactionDetailsTO;
    private String cboInvestmentBehaves = "";
    private String cboInvestmentBehavesTrans = "";
    private String cboIntPayFreq = "";
    private String cboInvestmentType = "";
    private String investmentID = "";
    private String investmentName = "";
    private Date IssueDt = null;
    private Double years = null;
    private Double months = null;
    private Double days = null;
    private Date maturityDate = null;
    private Double faceValue = null;
    private Double couponRate = null;
    private String SLR = "";
    private String callOption = "";
    private String putOption = "";
    private String setUpOption = "";
    private Double availableNoOfUnits = null;
    private Date lastIntPaidDate = null;
    private Double totalPremiumPaid = null;
    private Double totalPremiumCollected = null;
    private Double totalInterestPaid = null;
    private Double totalInterestCollected = null;
    private String txtDebitParticulars = "";
    private String txtCreditParticulars = "";
    private String txtNarration1 = "";
    private String batch_Id = "";
    private String trans_Id = "";
    private Date trans_Dt = null;
    private String trans_type = "";
    private String tran_Code = "";
    private Date purchas_Date = null;
    private String purchase_Mode = "";
    private String purchse_Through = "";
    private String callingTransAcctNo = "";
    private String broker_Name = "";
    private String txtNarration = "";
    private Double purchase_Rate = null;
    private Double no_Of_Units = null;
    private Double investment_amount = null;
    private Double txtPrematureROI = null;
    private Double txtPrematureIntAmt = null;
    private Double discount_Amount = null;
    private Double premium_Amount = null;
    private Double broken_Period_Interest = null;
    private Double broken_Commession = null;
    private HashMap authorizeMap;
    private Date initiatedDate = null;
    private Double outstandingAmount = null;
    private Double maturityAmount = null;
    private Date uptoIntDate = null;
    private String closeStatus = "";
    private Double excessOrShort = null;
    private String purchaseSaleBy = "";
    private boolean unExists = false;
    private String preCloserRate = "";
    private String closerType = "";
    private String amortizationAmt = "";
    private Date closerDate = null;
    private String investmentStatus = "";
    private String txtAccRefNo = "";
    private String txtInternalAccNo = "";
    private String rdoSBorCA = "";
    private String cboInvestmentTypeSBorCA = "";
    private String txtInvestmentIDTransSBorCA = "";
    private String txtInvestmentRefNoTrans = "";
    private String txtInvestmentInternalNoTrans = "";
    private String txtChequeNo = "";
    private HashMap renewalMap = new HashMap();
    private String closingType = "";
    private String interestType = "";
    private ArrayList IncVal = new ArrayList();
    private HashMap finalMap = new HashMap();
    private HashMap mapData = null;
    private Date tdtTransactionDt = null;
    private double txtInvestTDS=0;
    private boolean closeWithInterest=false;

    public boolean isCloseWithInterest() {
        return closeWithInterest;
    }

    public void setCloseWithInterest(boolean closeWithInterest) {
        this.closeWithInterest = closeWithInterest;
    }

    public double getTxtInvestTDS() {
        return txtInvestTDS;
    }

    public void setTxtInvestTDS(double txtInvestTDS) {
        this.txtInvestTDS = txtInvestTDS;
    }
    

    /**
     * Creates a new instance of ShareProductOB
     */
    private InvestmentsTransOB() {
        map = new HashMap();
        map.put(CommonConstants.JNDI, "InvestmentsTransJNDI");
        map.put(CommonConstants.HOME, "serverside.investments.InvestmentsTransHome");
        map.put(CommonConstants.REMOTE, "serverside.investments.InvestmentsTrans");
        try {
            proxy = ProxyFactory.createProxy();
            initUIComboBoxModel();
            fillDropdown();
            settblnvestmentTransDetColTitleCol();
            currDt = (Date) curDate.clone();
            tblInvestmentTransDet = new EnhancedTableModel(null, tblnvestmentTransDetColTitle);
            setTableTile();
            tblCheckBookTable = new EnhancedTableModel(null, tableTitle);
        } catch (Exception e) {
            parseException.logException(e, true);
        }
    }

    static {
        try {
            _log.info("Creating InvestmentsMasterOB...");
            objInvestmentsTransOB = new InvestmentsTransOB();
        } catch (Exception e) {
            parseException.logException(e, true);
        }
    }

    private void setTableTile() throws Exception {
        tableTitle.add("Sl No");
        tableTitle.add("Invest Id");
        tableTitle.add("Int.A/c No");
        tableTitle.add("Amount");
        tableTitle.add("Narration");
//        tableTitle.add("Cr Particulars");
//        tableTitle.add("Dr Particulars");
        IncVal = new ArrayList();
    }

    /**
     * Creating Instances of ComboBoxModel
     */
    private void initUIComboBoxModel() {
        cbmInvestmentBehaves = new ComboBoxModel();
    }

    /* Filling up the the ComboBoxModel with key, value */
    private void fillDropdown() throws Exception {
        try {
            _log.info("Inside FillDropDown");
            lookUpHash = new HashMap();
            lookUpHash.put(CommonConstants.MAP_NAME, null);
            final ArrayList lookup_keys = new ArrayList();
            lookup_keys.add("INVESTMENT");
            lookup_keys.add("DEPOSITSPRODUCT.DEPOSITPERIOD");

            lookUpHash.put(CommonConstants.PARAMFORQUERY, lookup_keys);
            keyValue = ClientUtil.populateLookupData(lookUpHash);
            getKeyValue((HashMap) keyValue.get("INVESTMENT"));
            cbmInvestmentBehaves = new ComboBoxModel(key, value);
            cbmInvestmentBehavesTrans = new ComboBoxModel(key, value);
            getKeyValue((HashMap) keyValue.get("DEPOSITSPRODUCT.DEPOSITPERIOD"));
            cbmIntPayFreq = new ComboBoxModel(key, value);

            HashMap param = new HashMap();
            param.put(CommonConstants.MAP_NAME, "getInvestmentOtherBankSBorCA");
            keyValue = ClientUtil.populateLookupData(param);
            fillData((HashMap) keyValue.get(CommonConstants.DATA));
            cbmInvestmentTypeSBorCA = new ComboBoxModel(key, value);

        } catch (NullPointerException e) {
            parseException.logException(e, true);
        } catch (Exception e) {
            parseException.logException(e, true);
        }
    }

    private void fillData(HashMap keyValue) throws Exception {
        key = (ArrayList) keyValue.get("KEY");
        value = (ArrayList) keyValue.get("VALUE");
    }

    public void insertTable(ArrayList tableList) {
        try {
            ArrayList aList = new ArrayList();
            if (tableList != null) {
                for (int i = 0; i < tableList.size(); i++) {
                    ArrayList eachList = new ArrayList();
                    ArrayList eachListadd = new ArrayList();
                    eachList = (ArrayList) tableList.get(i);
                    System.out.println("eachList" + eachList);
                    eachListadd.add(i + 1);
                    eachListadd.add((String) eachList.get(0));
                    eachListadd.add((String) eachList.get(1));
                    eachListadd.add((String) eachList.get(2));
                    eachListadd.add((String) eachList.get(3));
//                    eachListadd.add((String) eachList.get(4));
//                    eachListadd.add((String) eachList.get(5));        
                    aList.add(eachListadd);
                }
            }
            System.out.println("aList" + aList);
            tblCheckBookTable = new EnhancedTableModel((ArrayList) aList, tableTitle);
            notifyObservers();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void settblnvestmentTransDetColTitleCol() {
        tblnvestmentTransDetColTitle = new ArrayList();
        tblnvestmentTransDetColTitle.add("Trans_Date");
        tblnvestmentTransDetColTitle.add("Batch ID");
        tblnvestmentTransDetColTitle.add("Trn Code");
        tblnvestmentTransDetColTitle.add("Trans Type");
        tblnvestmentTransDetColTitle.add("Investment Amount");
        tblnvestmentTransDetColTitle.add("Available Amount");
        tblnvestmentTransDetColTitle.add("Interest Amount");

        //        tblnvestmentTransDetColTitle.add("AmortizationAmount");
    }
    /* Return the key,value(Array List) to be used up in ComboBoxModel */

    private void getKeyValue(HashMap keyValue) throws Exception {
        key = (ArrayList) keyValue.get(CommonConstants.KEY);
        value = (ArrayList) keyValue.get(CommonConstants.VALUE);
    }

    /**
     * Returns an instance of ShareProductOB.
     *
     * @return ShareProductOB
     */
    public static InvestmentsTransOB getInstance() throws Exception {
        return objInvestmentsTransOB;
    }

    public void setResult(int result) {
        _result = result;
        setResultStatus();
        setChanged();
    }

    public int getActionType() {
        return _actionType;
    }

    public void setActionType(int actionType) {
        _actionType = actionType;
        setStatus();
        setChanged();
    }

    /**
     * To set the status based on ActionType, either New, Edit, etc.,
     */
    public void setStatus() {
        this.setLblStatus(ClientConstants.ACTION_STATUS[this.getActionType()]);
    }

    /**
     * To update the Status based on result performed by doAction() method
     */
    public void setResultStatus() {
        this.setLblStatus(ClientConstants.RESULT_STATUS[this.getResult()]);
    }

    /**
     * Getter for property lblStatus.
     *
     * @return Value of property lblStatus.
     *
     */
    public java.lang.String getLblStatus() {
        return lblStatus;
    }

    /**
     * Setter for property lblStatus.
     *
     * @param lblStatus New value of property lblStatus.
     *
     */
    public void setLblStatus(java.lang.String lblStatus) {
        this.lblStatus = lblStatus;
        setChanged();
    }

    public void resetStatus() {
        this.setLblStatus(ClientConstants.ACTION_STATUS[0]);
    }

    public int getResult() {
        return _result;
    }

    /**
     * Returns an Instance of InvestmentMaster
     */
    public InvestmentsMasterTO getInvestmentsMasterTO(String command) {
        InvestmentsMasterTO objgetInvestmentsMasterTO = new InvestmentsMasterTO();
        final String yes = "Y";
        final String no = "N";
        objgetInvestmentsMasterTO.setCommand(command);
        if (objgetInvestmentsMasterTO.getCommand().equalsIgnoreCase(CommonConstants.TOSTATUS_INSERT)) {
            objgetInvestmentsMasterTO.setStatus(CommonConstants.STATUS_CREATED);
        } else if (objgetInvestmentsMasterTO.getCommand().equalsIgnoreCase(CommonConstants.TOSTATUS_UPDATE)) {
            objgetInvestmentsMasterTO.setStatus(CommonConstants.STATUS_MODIFIED);
        } else if (objgetInvestmentsMasterTO.getCommand().equalsIgnoreCase(CommonConstants.TOSTATUS_DELETE)) {
            objgetInvestmentsMasterTO.setStatus(CommonConstants.STATUS_DELETED);
        }
        objgetInvestmentsMasterTO.setCboInvestmentBehaves(CommonUtil.convertObjToStr(cbmInvestmentBehaves.getKeyForSelected()));
        objgetInvestmentsMasterTO.setCboIntPayFreq(CommonUtil.convertObjToStr(cbmIntPayFreq.getKeyForSelected()));
        objgetInvestmentsMasterTO.setInvestmentID(CommonUtil.convertObjToStr(getInvestmentID()));
        objgetInvestmentsMasterTO.setInvestmentName(CommonUtil.convertObjToStr(getInvestmentName()));
        objgetInvestmentsMasterTO.setIssueDt(getIssueDt());
        objgetInvestmentsMasterTO.setYears(getYears());
        objgetInvestmentsMasterTO.setMonths(getMonths());
        objgetInvestmentsMasterTO.setDays(getDays());
        objgetInvestmentsMasterTO.setMaturityDate(getMaturityDate());
        objgetInvestmentsMasterTO.setFaceValue(getFaceValue());
        objgetInvestmentsMasterTO.setCouponRate(getCouponRate());
        objgetInvestmentsMasterTO.setSLR(CommonUtil.convertObjToStr(getSLR()));
        objgetInvestmentsMasterTO.setCallOption(CommonUtil.convertObjToStr(getCallOption()));
        objgetInvestmentsMasterTO.setPutOption(CommonUtil.convertObjToStr(getPutOption()));
        objgetInvestmentsMasterTO.setSetUpOption(CommonUtil.convertObjToStr(getSetUpOption()));
        objgetInvestmentsMasterTO.setStatusBy(TrueTransactMain.USER_ID);
        objgetInvestmentsMasterTO.setStatusDt(curDate);
        objgetInvestmentsMasterTO.setLastIntPaidDate(getLastIntPaidDate());
        objgetInvestmentsMasterTO.setAvailableNoOfUnits(getAvailableNoOfUnits());
        objgetInvestmentsMasterTO.setTotalPremiumCollected(getTotalPremiumCollected());
        objgetInvestmentsMasterTO.setTotalPremiumPaid(getTotalPremiumPaid());
        objgetInvestmentsMasterTO.setTotalInterestPaid(getTotalInterestPaid());
        objgetInvestmentsMasterTO.setTotalInterestCollected(getTotalInterestCollected());
        objgetInvestmentsMasterTO.setOutstandingAmount(getOutstandingAmount());
        objgetInvestmentsMasterTO.setMaturityAmount(getMaturityAmount());
        objgetInvestmentsMasterTO.setInitiatedDate(getInitiatedDate());
        return objgetInvestmentsMasterTO;

    }

    public InvestmentsTransTO getInvestmentsTransTO(String command) {
        InvestmentsTransTO objgetInvestmentsTransTO = new InvestmentsTransTO();
        final String yes = "Y";
        final String no = "N";
        objgetInvestmentsTransTO.setCommand(command);
        if (objgetInvestmentsTransTO.getCommand().equalsIgnoreCase(CommonConstants.TOSTATUS_INSERT)) {
            objgetInvestmentsTransTO.setStatus(CommonConstants.STATUS_CREATED);
        } else if (objgetInvestmentsTransTO.getCommand().equalsIgnoreCase(CommonConstants.TOSTATUS_UPDATE)) {
            objgetInvestmentsTransTO.setStatus(CommonConstants.STATUS_MODIFIED);
        } else if (objgetInvestmentsTransTO.getCommand().equalsIgnoreCase(CommonConstants.TOSTATUS_DELETE)) {
            objgetInvestmentsTransTO.setStatus(CommonConstants.STATUS_DELETED);
        }
        objgetInvestmentsTransTO.setInvestmentBehaves(CommonUtil.convertObjToStr(cbmInvestmentBehaves.getKeyForSelected()));
        //        objgetInvestmentsTransTO.setInvestmentBehaves(CommonUtil.convertObjToStr(getCboInvestmentBehaves()));
        objgetInvestmentsTransTO.setInvestmentID(CommonUtil.convertObjToStr(getInvestmentID()));
        objgetInvestmentsTransTO.setInvestment_internal_Id(CommonUtil.convertObjToStr(getTxtInternalAccNo()));
        objgetInvestmentsTransTO.setCboInvestmentType(CommonUtil.convertObjToStr(cbmInvestmentTypeSBorCA.getKeyForSelected()));
        objgetInvestmentsTransTO.setTxtInvestmentIDTransSBorCA(CommonUtil.convertObjToStr(getTxtInvestmentIDTransSBorCA()));
        objgetInvestmentsTransTO.setTxtInvestmentRefNoTrans(CommonUtil.convertObjToStr(getTxtInvestmentRefNoTrans()));
        objgetInvestmentsTransTO.setTxtInvestmentInternalNoTrans(CommonUtil.convertObjToStr(getTxtInvestmentInternalNoTrans()));
        objgetInvestmentsTransTO.setTxtChequeNo(CommonUtil.convertObjToStr(getTxtChequeNo()));
        System.out.println("getTxtNarration1()>>>>" + getTxtNarration1());
        objgetInvestmentsTransTO.setNarration(CommonUtil.convertObjToStr(getTxtNarration1()));
        objgetInvestmentsTransTO.setRdoSBorCA(CommonUtil.convertObjToStr(getRdoSBorCA()));
        objgetInvestmentsTransTO.setInvestment_Ref_No(CommonUtil.convertObjToStr(getTxtAccRefNo()));
        objgetInvestmentsTransTO.setInvestmentName(CommonUtil.convertObjToStr(getInvestmentName()));
        objgetInvestmentsTransTO.setBatchID(CommonUtil.convertObjToStr(getBatch_Id()));
        System.out.println("");
        if(getTdtTransactionDt()!=null && !getTdtTransactionDt().equals("")){
            objgetInvestmentsTransTO.setTransDT(getTdtTransactionDt());
        }else{
            objgetInvestmentsTransTO.setTransDT((Date)curDate.clone());
        }
        objgetInvestmentsTransTO.setTransType(CommonUtil.convertObjToStr(getTrans_type()));
        objgetInvestmentsTransTO.setTrnCode(getTran_Code());//CommonUtil.convertObjToStr(
        objgetInvestmentsTransTO.setAmount(new Double(0.0));
        objgetInvestmentsTransTO.setPurchaseDt(setProperDtFormat(getPurchas_Date()));
        objgetInvestmentsTransTO.setPurchaseMode(CommonUtil.convertObjToStr(getPurchase_Mode()));
        objgetInvestmentsTransTO.setPurchaseThrough(CommonUtil.convertObjToStr(getPurchse_Through()));
        objgetInvestmentsTransTO.setBrokerName(CommonUtil.convertObjToStr(getBroker_Name()));
        objgetInvestmentsTransTO.setPurchaseRate(getPurchase_Rate());
        objgetInvestmentsTransTO.setNoOfUnits(getNo_Of_Units());
        objgetInvestmentsTransTO.setInvestmentAmount(getInvestment_amount());
        objgetInvestmentsTransTO.setPrematureROI(CommonUtil.convertObjToDouble(getTxtPrematureROI()));
        System.out.println("hjkhqwerhhiuh>>>"+getTxtPrematureIntAmt());
        if (getTxtPrematureIntAmt()!=null && !getTxtPrematureIntAmt().equals("")) {
            System.out.println("mmdjj111");
            objgetInvestmentsTransTO.setPrematureInt(CommonUtil.convertObjToDouble(getTxtPrematureIntAmt()));
        } else {
            System.out.println("mmdjj222");
            objgetInvestmentsTransTO.setPrematureInt(CommonUtil.convertObjToDouble("0.00"));
        }
        objgetInvestmentsTransTO.setDiscountAmount(getDiscount_Amount());
        objgetInvestmentsTransTO.setPremiumAmount(getPremium_Amount());
        objgetInvestmentsTransTO.setBrokenPeriodInterest(getBroken_Period_Interest());
        objgetInvestmentsTransTO.setBrokerCommession(getBroken_Commession());
        objgetInvestmentsTransTO.setStatusBy(TrueTransactMain.USER_ID);
        objgetInvestmentsTransTO.setStatusDt(curDate);
        objgetInvestmentsTransTO.setDividendAmount(new Double(0));
        objgetInvestmentsTransTO.setLastIntPaidDate(setProperDtFormat(getUptoIntDate()));
        objgetInvestmentsTransTO.setPurchaseSaleBy(getPurchaseSaleBy());
        objgetInvestmentsTransTO.setAuthorizeBy(getCloseStatus());
        objgetInvestmentsTransTO.setInitiatedBranch(TrueTransactMain.BRANCH_ID);
        objgetInvestmentsTransTO.setClosingType(getClosingType());
        objgetInvestmentsTransTO.setInterestType(getInterestType());
        objgetInvestmentsTransTO.setCrParticulars(getTxtCreditParticulars());
        objgetInvestmentsTransTO.setDrParticulars(getTxtDebitParticulars());
        objgetInvestmentsTransTO.setInvestTDS(getTxtInvestTDS());
        return objgetInvestmentsTransTO;
    }

    private Date setProperDtFormat(Date dt) {
        Date tempDt = (Date) curDate.clone();
        if (dt != null) {
            tempDt.setDate(dt.getDate());
            tempDt.setMonth(dt.getMonth());
            tempDt.setYear(dt.getYear());
            return tempDt;
        }
        return null;
    }

    /**
     * Sets all the InvsetmentMaster values to the OB varibles there by
     * populatin the UI fields
     */
    private void setInvestmentsMasterTO(InvestmentsMasterTO objInvestmentsMasterTO) {

        setCboInvestmentBehaves(CommonUtil.convertObjToStr(getCbmInvestmentBehaves().getDataForKey(objInvestmentsMasterTO.getCboInvestmentBehaves())));
        setCboIntPayFreq(CommonUtil.convertObjToStr(getCbmIntPayFreq().getDataForKey(objInvestmentsMasterTO.getCboIntPayFreq())));
        setInvestmentID(CommonUtil.convertObjToStr(objInvestmentsMasterTO.getInvestmentID()));
        setInvestmentName(CommonUtil.convertObjToStr(objInvestmentsMasterTO.getInvestmentName()));
        setIssueDt(objInvestmentsMasterTO.getIssueDt());
        setYears(objInvestmentsMasterTO.getYears());
        setMonths(objInvestmentsMasterTO.getMonths());
        setDays(objInvestmentsMasterTO.getDays());
        setMaturityDate(objInvestmentsMasterTO.getMaturityDate());
        setFaceValue(objInvestmentsMasterTO.getFaceValue());
        setCouponRate(objInvestmentsMasterTO.getCouponRate());
        setSLR(CommonUtil.convertObjToStr(objInvestmentsMasterTO.getSLR()));
        setCallOption(CommonUtil.convertObjToStr(objInvestmentsMasterTO.getCallOption()));
        setPutOption(CommonUtil.convertObjToStr(objInvestmentsMasterTO.getPutOption()));
        setSetUpOption(CommonUtil.convertObjToStr(objInvestmentsMasterTO.getSetUpOption()));
        setLastIntPaidDate(objInvestmentsMasterTO.getLastIntPaidDate());
        setAvailableNoOfUnits(CommonUtil.convertObjToDouble(objInvestmentsMasterTO.getAvailableNoOfUnits()));
        setTotalInterestCollected(CommonUtil.convertObjToDouble(objInvestmentsMasterTO.getTotalInterestCollected()));
        setTotalInterestPaid(CommonUtil.convertObjToDouble(objInvestmentsMasterTO.getTotalInterestPaid()));
        setTotalPremiumPaid(CommonUtil.convertObjToDouble(objInvestmentsMasterTO.getTotalPremiumPaid()));
        setTotalPremiumCollected(CommonUtil.convertObjToDouble(objInvestmentsMasterTO.getTotalPremiumCollected()));
        setMaturityAmount(objInvestmentsMasterTO.getMaturityAmount());
        setOutstandingAmount(objInvestmentsMasterTO.getOutstandingAmount());
        setPreCloserRate(objInvestmentsMasterTO.getPreCloserRate());
        setCloserType(objInvestmentsMasterTO.getCloserType());
        setAmortizationAmt(objInvestmentsMasterTO.getAmortizationAmt());
        setCloserDate(objInvestmentsMasterTO.getCloserDate());
        setInvestmentStatus(objInvestmentsMasterTO.getInvestmentStatus());
        setChanged();
        notifyObservers();
    }

    private void setInvestmentsTransTO(InvestmentsTransTO objInvestmentsTransTO) {
        setPurchas_Date(DateUtil.getDateMMDDYYYY(DateUtil.getStringDate(objInvestmentsTransTO.getPurchaseDt())));
        setPurchase_Mode(CommonUtil.convertObjToStr(objInvestmentsTransTO.getPurchaseMode()));
        setPurchse_Through(CommonUtil.convertObjToStr(objInvestmentsTransTO.getPurchaseThrough()));
        setBatch_Id(CommonUtil.convertObjToStr(objInvestmentsTransTO.getBatchID()));
        setBroker_Name(CommonUtil.convertObjToStr(objInvestmentsTransTO.getBrokerName()));
        setPurchase_Rate(CommonUtil.convertObjToDouble(objInvestmentsTransTO.getPurchaseRate()));
        setNo_Of_Units(CommonUtil.convertObjToDouble(objInvestmentsTransTO.getNoOfUnits()));
        setDiscount_Amount(CommonUtil.convertObjToDouble(objInvestmentsTransTO.getDiscountAmount()));
        setPremium_Amount(CommonUtil.convertObjToDouble(objInvestmentsTransTO.getPremiumAmount()));
        setBroken_Period_Interest(CommonUtil.convertObjToDouble(objInvestmentsTransTO.getBrokenPeriodInterest()));
        setBroken_Commession(CommonUtil.convertObjToDouble(objInvestmentsTransTO.getBrokerCommession()));
        setInvestment_amount(objInvestmentsTransTO.getInvestmentAmount());
        setTxtPrematureIntAmt(objInvestmentsTransTO.getPrematureInt());
        setTxtPrematureROI(objInvestmentsTransTO.getPrematureROI());
        setTran_Code(CommonUtil.convertObjToStr(objInvestmentsTransTO.getTrnCode()));
        setUptoIntDate(DateUtil.getDateMMDDYYYY(DateUtil.getStringDate(objInvestmentsTransTO.getLastIntPaidDate())));
        setCloseStatus(CommonUtil.convertObjToStr(objInvestmentsTransTO.getAuthorizeBy()));
        setPurchaseSaleBy(CommonUtil.convertObjToStr(objInvestmentsTransTO.getPurchaseSaleBy()));
        //Added By Suresh
        setCboInvestmentBehaves(CommonUtil.convertObjToStr(getCbmInvestmentBehaves().getDataForKey(objInvestmentsTransTO.getInvestmentBehaves())));
        setInvestmentID(CommonUtil.convertObjToStr(objInvestmentsTransTO.getInvestmentID()));
        setInvestmentName(CommonUtil.convertObjToStr(objInvestmentsTransTO.getInvestmentName()));
        setPurchas_Date(objInvestmentsTransTO.getPurchaseDt());
        setPurchase_Mode(CommonUtil.convertObjToStr(objInvestmentsTransTO.getPurchaseMode()));
        setTxtInternalAccNo(CommonUtil.convertObjToStr(objInvestmentsTransTO.getInvestment_internal_Id()));
        setCboInvestmentTypeSBorCA(CommonUtil.convertObjToStr(getCbmInvestmentTypeSBorCA().getDataForKey(objInvestmentsTransTO.getCboInvestmentType())));
        setTxtInvestmentIDTransSBorCA(CommonUtil.convertObjToStr(objInvestmentsTransTO.getTxtInvestmentIDTransSBorCA()));
        setTxtInvestmentRefNoTrans(CommonUtil.convertObjToStr(objInvestmentsTransTO.getTxtInvestmentRefNoTrans()));
        setRdoSBorCA(CommonUtil.convertObjToStr(objInvestmentsTransTO.getRdoSBorCA()));
        setTxtInvestmentInternalNoTrans(CommonUtil.convertObjToStr(objInvestmentsTransTO.getTxtInvestmentInternalNoTrans()));
        setTxtChequeNo(CommonUtil.convertObjToStr(objInvestmentsTransTO.getTxtChequeNo()));
        setTxtNarration1(CommonUtil.convertObjToStr(objInvestmentsTransTO.getNarration()));
        setTxtCreditParticulars(CommonUtil.convertObjToStr(objInvestmentsTransTO.getCrParticulars()));
        setTxtDebitParticulars(CommonUtil.convertObjToStr(objInvestmentsTransTO.getDrParticulars()));
        setTxtAccRefNo(CommonUtil.convertObjToStr(objInvestmentsTransTO.getInvestment_Ref_No()));
        setTdtTransactionDt(DateUtil.getDateMMDDYYYY(DateUtil.getStringDate(objInvestmentsTransTO.getTransDT())));
        setTxtInvestTDS(CommonUtil.convertObjToDouble(objInvestmentsTransTO.getInvestTDS()));
        setChanged();
        notifyObservers();
    }
    /* Executes Query using the TO object */

    public void execute(String command) {
        try {
            HashMap term = new HashMap();
            int countRec = 0;
            HashMap proxyResultMap = null;
            term.put(CommonConstants.USER_ID, TrueTransactMain.USER_ID);
            term.put(CommonConstants.MODULE, getModule());
            term.put(CommonConstants.SCREEN, getScreen());
            term.put(CommonConstants.BRANCH_ID, TrueTransactMain.BRANCH_ID);
            term.put("COMMAND", command);
            if (!command.equals("AUTHORIZE")) {
                term.put(CommonConstants.BRANCH_ID, TrueTransactMain.BRANCH_ID);
                term.put("InvestmentsMasterTO", getInvestmentsMasterTO(command));
                term.put("InvestmentsTransTO", getInvestmentsTransTO(command));
                transactionDetailsTO = new LinkedHashMap();
                transactionDetailsTO.put(NOT_DELETED_TRANS_TOs, allowedTransactionDetailsTO);
                term.put("TransactionTO", transactionDetailsTO);                
                if (getTran_Code().equals("Closure")) {
                    term.put("CLOSING_TYPE", getClosingType());
                    term.put("INTEREST_TYPE", getInterestType());
                }
                if (getActionType() == ClientConstants.ACTIONTYPE_EDIT) {
                    term.put("OLDAMOUNT", oldAmountMap);
                }
                String investmentType = CommonUtil.convertObjToStr(cbmInvestmentBehaves.getKeyForSelected());
                System.out.print("####### investmentType : " + investmentType);
                System.out.print("####### getTran_Code : " + getTran_Code());
                if (investmentType.equals("OTHER_BANK_CCD") && getTran_Code().equals("Renewal")) {
                    System.out.print("###Renewal Data : " + getRenewalMap());
                    if (getRenewalMap() != null && getRenewalMap().size() > 0) {
                        term.put("RENEWAL_DATA", getRenewalMap());
                    }
                }
            } else {
                term.put(CommonConstants.AUTHORIZEMAP, getAuthorizeMap());
                transactionDetailsTO = new LinkedHashMap();
                transactionDetailsTO.put(NOT_DELETED_TRANS_TOs, allowedTransactionDetailsTO);
                term.put("TransactionTO", transactionDetailsTO);
            }
            if (getTxtNarration1() != null) {
                term.put("NARRATION_DETAILS", getTxtNarration1());
            }
            if (getTxtDebitParticulars() != null) {
                term.put("DEBIT_PARTICULARS", getTxtDebitParticulars());
            }
            if (getTxtCreditParticulars() != null) {
                term.put("CREDIT_PARTICULARS", getTxtCreditParticulars());
            }
            if(getTdtTransactionDt()!=null && !getTdtTransactionDt().equals("")){
                if(DateUtil.dateDiff(getTdtTransactionDt(), curDate) != 0){
                    term.put("TRANS_DATE", getProperDateFormat(getTdtTransactionDt()));
                    term.put("BACK_DATED_TRANSACTION", "BACK_DATED_TRANSACTION");                    
                }else{
                    term.put("TRANS_DATE", getProperDateFormat(getTdtTransactionDt()));
                }
            }
            System.out.println("getFinalMap() in ob >>>>>"+getFinalMap());
            term.put("finalMap", getFinalMap());
            if (countRec == 0) {
                System.out.print("####### Data : " + term);
                proxyResultMap = proxy.execute(term, map);
                setProxyReturnMap(proxyResultMap);
                System.out.println("######## proxyResultMap :" + proxyResultMap);
            } else {
                ClientUtil.displayAlert("THIS PRODUCT HAVING ACCOUNT");
            }
            setResult(getActionType());

        } catch (Exception e) {
            setResult(ClientConstants.ACTIONTYPE_FAILED);
            parseException.logException(e, true);
        }
    }

    public Date getProperDateFormat(Object obj) {
        Date properDt = null;
        if (obj != null && obj.toString().length() > 0) {
            Date tempDt = DateUtil.getDateMMDDYYYY(CommonUtil.convertObjToStr(obj));
            properDt = (Date) curDate.clone();
            properDt.setDate(tempDt.getDate());
            properDt.setMonth(tempDt.getMonth());
            properDt.setYear(tempDt.getYear());
        }
        return properDt;
    }
    
    public String callForBehaves() {
        return CommonUtil.convertObjToStr(cbmInvestmentBehaves.getKeyForSelected());
    }

    public String callForBehavesTrans() {
        return CommonUtil.convertObjToStr(cbmInvestmentBehavesTrans.getKeyForSelected());
    }

    public String callForBehavesSBorCATrans() {
        return CommonUtil.convertObjToStr(cbmInvestmentTypeSBorCA.getKeyForSelected());
    }

    /**
     * Resetting all tbe Fields of UI
     */
    public void resetForm() {
        setCboInvestmentBehaves("");
        setCboIntPayFreq("");
        setInvestmentID("");
        setInvestmentName("");
        setIssueDt(null);
        setFaceValue(null);
        setMonths(null);
        setDays(null);
        setCouponRate(null);
        setMaturityDate(null);
        setSLR("");
        setCallOption("");
        setSetUpOption("");
        setNo_Of_Units(null);
        setLastIntPaidDate(null);
        setCboIntPayFreq("");
        setTotalInterestCollected(null);
        setTotalInterestPaid(null);
        setTotalPremiumCollected(null);
        setTotalPremiumPaid(null);
        setPurchas_Date(null);
        setPurchase_Mode("");
        setPurchse_Through("");
        setTxtNarration("");
        setTxtCreditParticulars("");
        setTxtDebitParticulars("");
        setAvailableNoOfUnits(null);
        setInvestment_amount(null);
        setTxtPrematureIntAmt(null);
        setClosingType("");
        setInterestType("");
        setTxtPrematureROI(null);
        setDiscount_Amount(null);
        setBroken_Period_Interest(null);
        setBroken_Commession(null);
        setPremium_Amount(null);
        setTran_Code("");
        oldAmountMap = null;
        setPurchase_Rate(null);
        setOutstandingAmount(null);
        setPurchaseSaleBy("");
        resetTable();
        setChanged();
        setUptoIntDate(null);
        setMaturityAmount(null);
        setTxtAccRefNo("");
        setTxtInternalAccNo("");
        setCboInvestmentTypeSBorCA("");
        setTxtInvestmentIDTransSBorCA("");
        setTxtInvestmentRefNoTrans("");
        setRdoSBorCA("");
        setTxtInvestmentInternalNoTrans("");
        setTxtChequeNo("");
        setTxtAccRefNo("");
        setCallingTransAcctNo("");
        setTdtTransactionDt(null);
        renewalMap = new HashMap();
        setTxtInvestTDS(0.0);
        setCloseWithInterest(false);
        notifyObservers();
    }

    /**
     * This checks whether user entered sharetype already exists if it exists it
     * returns true otherwise false
     */
    public boolean isInvsetMentMasterTypeExists(String InvestmentName) {
        boolean exists = false;
        ArrayList resultList = (ArrayList) ClientUtil.executeQuery("getSelectInvestmentMaster", null);
        if (resultList != null) {
            for (int i = 0; i < resultList.size(); i++) {
                HashMap resultMap = (HashMap) resultList.get(i);
                String investProdType = CommonUtil.convertObjToStr(resultMap.get("INVSETMENT_NAME"));
                if (investProdType.equalsIgnoreCase(InvestmentName)) {
                    exists = true;
                    break;
                }
            }
        }
        return exists;
    }

    /* Populates the TO object by executing a Query */
    public void populateData(HashMap whereMap) {
        mapData = new HashMap();
        try {
            System.out.println("CommonUtil.convertObjToStr(cbmInvestmentBehavesTrans.getKeyForSelected())>>" + CommonUtil.convertObjToStr(cbmInvestmentBehavesTrans.getKeyForSelected()));
            System.out.println("whereMap1111>>>>" + whereMap);
            if (whereMap.containsKey("TRANSACTION")) {
                whereMap.put("INVESTMENT_TYPE", CommonUtil.convertObjToStr(cbmInvestmentBehavesTrans.getKeyForSelected()));
                System.out.println("whereMap>>>>" + whereMap);
            }
            mapData = proxy.executeQuery(whereMap, map);
            System.out.println("mapData------------->" + mapData);
            if (!whereMap.containsKey("TRANSACTION")) {
//                InvestmentsMasterTO objInvestmentsMasterTO =
//                (InvestmentsMasterTO) ((List)((HashMap) mapData.get("InvestmentsMasterTO")).get("InvestmentsMasterTO")).get(0);
//                setInvestmentsMasterTO(objInvestmentsMasterTO);
            }
            if (!whereMap.containsKey("MASTER") && !whereMap.containsKey("TRANSACTION")) {
                InvestmentsTransTO objInvestmentsTransTO =
                        (InvestmentsTransTO) ((List) ((HashMap) mapData.get("InvestmentsTransTO")).get("InvestmentsTransTO")).get(0);
                setTableData(mapData);
                setInvestmentsTransTO(objInvestmentsTransTO);
                List list = (List) mapData.get("TransactionTO");
                if (!list.isEmpty()) {
                    transactionOB.setDetails(list);
                }
                if (getActionType() == ClientConstants.ACTIONTYPE_EDIT) {
                    oldAmountMap = new HashMap();
                    TxTransferTO txTransferTO = new TxTransferTO();
                    List oldLst = (List) mapData.get("transferTrans");
                    if (!oldLst.isEmpty()) {
                        for (int i = 0; i < oldLst.size(); i++) {
                            txTransferTO = new TxTransferTO();
                            txTransferTO = (TxTransferTO) oldLst.get(i);
                            oldAmountMap.put(txTransferTO.getTransId(), txTransferTO.getAmount());

                        }
                    }
                    txTransferTO = null;
                }

            }
            if (whereMap.containsKey("UNAUTHORIZEDEXISTS")) {

                int exists = CommonUtil.convertObjToInt(mapData.get("EXISTS"));
                if (exists == 0) {
                    setUnExists(false);
                } else {
                    setUnExists(true);
                }
            }

            if (whereMap.containsKey("TRANSACTION")) {
                ArrayList transList = (ArrayList) mapData.get("getSelectInvestmentTransDetailsTO");
                if (transList != null && transList.size() > 0) {
                    setInvestmentAmortizationTable(transList);
                }
//                InvestmentsMasterTO objInvestmentsMasterTO =
//                (InvestmentsMasterTO) ((List)((HashMap) mapData.get("InvestmentsMasterTO")).get("InvestmentsMasterTO")).get(0);
//                setInvestmentsMasterTO(objInvestmentsMasterTO);
            }

        } catch (Exception e) {
            setResult(ClientConstants.ACTIONTYPE_FAILED);
            e.printStackTrace();
            parseException.logException(e, true);

        }
    }

    private void setInvestmentAmortizationTable(ArrayList transList) {
        tblInvestmentTransDet = new EnhancedTableModel();
        ArrayList dataList = new ArrayList();
        for (int i = 0; i < transList.size(); i++) {
            ArrayList invAmrDetRow = new ArrayList();
            InvestmentsTransTO objInvestmentsTransTO = new InvestmentsTransTO();
            objInvestmentsTransTO = (InvestmentsTransTO) transList.get(i);
            invAmrDetRow.add(0, CommonUtil.convertObjToStr(objInvestmentsTransTO.getTransDT()));
            invAmrDetRow.add(1, objInvestmentsTransTO.getBatchID());
            invAmrDetRow.add(2, CommonUtil.convertObjToStr(objInvestmentsTransTO.getTrnCode()));
            invAmrDetRow.add(3, objInvestmentsTransTO.getTransType());
            invAmrDetRow.add(4, CommonUtil.convertObjToStr(objInvestmentsTransTO.getInvestmentAmount()));
            invAmrDetRow.add(5, CommonUtil.convertObjToStr(objInvestmentsTransTO.getInvAvailableBal()));
            invAmrDetRow.add(6, CommonUtil.convertObjToStr(objInvestmentsTransTO.getBrokenPeriodInterest()));

            dataList.add(invAmrDetRow);
            invAmrDetRow = null;
        }
        tblInvestmentTransDet.setDataArrayList(dataList, tblnvestmentTransDetColTitle);
        setChanged();
        notifyObservers();
    }

    public void setTableData(HashMap mapData) {
        List tblData = ((List) ((HashMap) mapData.get("InvestmentsTransTO")).get("InvestmentsTransTO"));
        ArrayList tblList = new ArrayList();
        for (int i = 0; i < tblData.size(); i++) {
            InvestmentsTransTO objTo = (InvestmentsTransTO) tblData.get(i);
            ArrayList sList = new ArrayList();
            sList.add(objTo.getInvestmentID());
            sList.add(objTo.getInvestment_internal_Id());
            sList.add(CommonUtil.convertObjToStr(objTo.getInvestmentAmount()));
            sList.add(CommonUtil.convertObjToStr(objTo.getNarration()));
//            sList.add(CommonUtil.convertObjToStr(objTo.getCrParticulars()));
//            sList.add(CommonUtil.convertObjToStr(objTo.getDrParticulars()));
            tblList.add(sList);
        }
        insertTable(tblList);
    }

    public void showData(String acno) {
        List tblData = ((List) ((HashMap) mapData.get("InvestmentsTransTO")).get("InvestmentsTransTO"));
        ArrayList tblList = new ArrayList();
        for (int i = 0; i < tblData.size(); i++) {
            InvestmentsTransTO objTo = (InvestmentsTransTO) tblData.get(i);
            if (acno.equals(objTo.getInvestment_internal_Id())) {
                setInvestmentsTransTO(objTo);
            }
        }
    }

    public void showDatanew(ArrayList aList) {
        InvestmentsTransTO objTo = (InvestmentsTransTO) aList.get(0);
        setInvestmentsTransTO(objTo);
    }

    public void resetTable() {
        try {
            ArrayList data = tblInvestmentTransDet.getDataArrayList();
            for (int i = data.size(); i > 0; i--) {
                tblInvestmentTransDet.removeRow(i - 1);
            }
        } catch (Exception e) {
            setResult(ClientConstants.ACTIONTYPE_FAILED);
            //            log.info("Error in resetTable():");
        }
    }

    /**
     * Getter for property cbmInvestmentBehaves.
     *
     * @return Value of property cbmInvestmentBehaves.
     */
    public com.see.truetransact.clientutil.ComboBoxModel getCbmInvestmentBehaves() {
        return cbmInvestmentBehaves;
    }

    /**
     * Setter for property cbmInvestmentBehaves.
     *
     * @param cbmInvestmentBehaves New value of property cbmInvestmentBehaves.
     */
    public void setCbmInvestmentBehaves(com.see.truetransact.clientutil.ComboBoxModel cbmInvestmentBehaves) {
        this.cbmInvestmentBehaves = cbmInvestmentBehaves;
    }

    /**
     * Getter for property cboInvestmentBehaves.
     *
     * @return Value of property cboInvestmentBehaves.
     */
    public java.lang.String getCboInvestmentBehaves() {
        return cboInvestmentBehaves;
    }

    /**
     * Setter for property cboInvestmentBehaves.
     *
     * @param cboInvestmentBehaves New value of property cboInvestmentBehaves.
     */
    public void setCboInvestmentBehaves(java.lang.String cboInvestmentBehaves) {
        this.cboInvestmentBehaves = cboInvestmentBehaves;
    }

    /**
     * Getter for property cbmIntPayFreq.
     *
     * @return Value of property cbmIntPayFreq.
     */
    public com.see.truetransact.clientutil.ComboBoxModel getCbmIntPayFreq() {
        return cbmIntPayFreq;
    }

    /**
     * Setter for property cbmIntPayFreq.
     *
     * @param cbmIntPayFreq New value of property cbmIntPayFreq.
     */
    public void setCbmIntPayFreq(com.see.truetransact.clientutil.ComboBoxModel cbmIntPayFreq) {
        this.cbmIntPayFreq = cbmIntPayFreq;
    }

    /**
     * Getter for property IntPayFreq.
     *
     * @return Value of property IntPayFreq.
     */
    /**
     * Getter for property investmentID.
     *
     * @return Value of property investmentID.
     */
    public java.lang.String getInvestmentID() {
        return investmentID;
    }

    /**
     * Setter for property investmentID.
     *
     * @param investmentID New value of property investmentID.
     */
    public void setInvestmentID(java.lang.String investmentID) {
        this.investmentID = investmentID;
    }

    /**
     * Getter for property cboIntPayFreq.
     *
     * @return Value of property cboIntPayFreq.
     */
    public java.lang.String getCboIntPayFreq() {
        return cboIntPayFreq;
    }

    /**
     * Setter for property cboIntPayFreq.
     *
     * @param cboIntPayFreq New value of property cboIntPayFreq.
     */
    public void setCboIntPayFreq(java.lang.String cboIntPayFreq) {
        this.cboIntPayFreq = cboIntPayFreq;
    }

    /**
     * Getter for property investmentName.
     *
     * @return Value of property investmentName.
     */
    public java.lang.String getInvestmentName() {
        return investmentName;
    }

    /**
     * Setter for property investmentName.
     *
     * @param investmentName New value of property investmentName.
     */
    public void setInvestmentName(java.lang.String investmentName) {
        this.investmentName = investmentName;
    }

    /**
     * Getter for property IssueDt.
     *
     * @return Value of property IssueDt.
     */
    public java.util.Date getIssueDt() {
        return IssueDt;
    }

    /**
     * Setter for property IssueDt.
     *
     * @param IssueDt New value of property IssueDt.
     */
    public void setIssueDt(java.util.Date IssueDt) {
        this.IssueDt = IssueDt;
    }

    /**
     * Getter for property months.
     *
     * @return Value of property months.
     */
    public Double getMonths() {
        return months;
    }

    /**
     * Setter for property months.
     *
     * @param months New value of property months.
     */
    public void setMonths(Double months) {
        this.months = months;
    }

    /**
     * Getter for property days.
     *
     * @return Value of property days.
     */
    public Double getDays() {
        return days;
    }

    /**
     * Setter for property days.
     *
     * @param days New value of property days.
     */
    public void setDays(Double days) {
        this.days = days;
    }

    /**
     * Getter for property maturityDate.
     *
     * @return Value of property maturityDate.
     */
    public java.util.Date getMaturityDate() {
        return maturityDate;
    }

    /**
     * Setter for property maturityDate.
     *
     * @param maturityDate New value of property maturityDate.
     */
    public void setMaturityDate(java.util.Date maturityDate) {
        this.maturityDate = maturityDate;
    }

    /**
     * Getter for property faceValue.
     *
     * @return Value of property faceValue.
     */
    public Double getFaceValue() {
        return faceValue;
    }

    /**
     * Setter for property faceValue.
     *
     * @param faceValue New value of property faceValue.
     */
    public void setFaceValue(Double faceValue) {
        this.faceValue = faceValue;
    }

    /**
     * Getter for property couponRate.
     *
     * @return Value of property couponRate.
     */
    public Double getCouponRate() {
        return couponRate;
    }

    /**
     * Setter for property couponRate.
     *
     * @param couponRate New value of property couponRate.
     */
    public void setCouponRate(Double couponRate) {
        this.couponRate = couponRate;
    }

    /**
     * Getter for property SLR.
     *
     * @return Value of property SLR.
     */
    public java.lang.String getSLR() {
        return SLR;
    }

    /**
     * Setter for property SLR.
     *
     * @param SLR New value of property SLR.
     */
    public void setSLR(java.lang.String SLR) {
        this.SLR = SLR;
    }

    /**
     * Getter for property callOption.
     *
     * @return Value of property callOption.
     */
    public java.lang.String getCallOption() {
        return callOption;
    }

    /**
     * Setter for property callOption.
     *
     * @param callOption New value of property callOption.
     */
    public void setCallOption(java.lang.String callOption) {
        this.callOption = callOption;
    }

    /**
     * Getter for property putOption.
     *
     * @return Value of property putOption.
     */
    public java.lang.String getPutOption() {
        return putOption;
    }

    /**
     * Setter for property putOption.
     *
     * @param putOption New value of property putOption.
     */
    public void setPutOption(java.lang.String putOption) {
        this.putOption = putOption;
    }

    /**
     * Getter for property setUpOption.
     *
     * @return Value of property setUpOption.
     */
    public java.lang.String getSetUpOption() {
        return setUpOption;
    }

    /**
     * Setter for property setUpOption.
     *
     * @param setUpOption New value of property setUpOption.
     */
    public void setSetUpOption(java.lang.String setUpOption) {
        this.setUpOption = setUpOption;
    }

    /**
     * Getter for property transactionOB.
     *
     * @return Value of property transactionOB.
     */
    public void setTransactionOB(TransactionOB transactionOB) {
        this.transactionOB = transactionOB;
    }

    /**
     * Getter for property availableNoOfUnits.
     *
     * @return Value of property availableNoOfUnits.
     */
    public Double getAvailableNoOfUnits() {
        return availableNoOfUnits;
    }

    /**
     * Setter for property availableNoOfUnits.
     *
     * @param availableNoOfUnits New value of property availableNoOfUnits.
     */
    public void setAvailableNoOfUnits(Double availableNoOfUnits) {
        this.availableNoOfUnits = availableNoOfUnits;
    }

    /**
     * Getter for property lastIntPaidDate.
     *
     * @return Value of property lastIntPaidDate.
     */
    public java.util.Date getLastIntPaidDate() {
        return lastIntPaidDate;
    }

    /**
     * Setter for property lastIntPaidDate.
     *
     * @param lastIntPaidDate New value of property lastIntPaidDate.
     */
    public void setLastIntPaidDate(java.util.Date lastIntPaidDate) {
        this.lastIntPaidDate = lastIntPaidDate;
    }

    /**
     * Getter for property totalPremiumPaid.
     *
     * @return Value of property totalPremiumPaid.
     */
    public Double getTotalPremiumPaid() {
        return totalPremiumPaid;
    }

    /**
     * Setter for property totalPremiumPaid.
     *
     * @param totalPremiumPaid New value of property totalPremiumPaid.
     */
    public void setTotalPremiumPaid(Double totalPremiumPaid) {
        this.totalPremiumPaid = totalPremiumPaid;
    }

    /**
     * Getter for property totalPremiumCollected.
     *
     * @return Value of property totalPremiumCollected.
     */
    public Double getTotalPremiumCollected() {
        return totalPremiumCollected;
    }

    /**
     * Setter for property totalPremiumCollected.
     *
     * @param totalPremiumCollected New value of property totalPremiumCollected.
     */
    public void setTotalPremiumCollected(Double totalPremiumCollected) {
        this.totalPremiumCollected = totalPremiumCollected;
    }

    /**
     * Getter for property totalInterestPaid.
     *
     * @return Value of property totalInterestPaid.
     */
    public Double getTotalInterestPaid() {
        return totalInterestPaid;
    }

    /**
     * Setter for property totalInterestPaid.
     *
     * @param totalInterestPaid New value of property totalInterestPaid.
     */
    public void setTotalInterestPaid(Double totalInterestPaid) {
        this.totalInterestPaid = totalInterestPaid;
    }

    /**
     * Getter for property totalInterestCollected.
     *
     * @return Value of property totalInterestCollected.
     */
    public Double getTotalInterestCollected() {
        return totalInterestCollected;
    }

    /**
     * Setter for property totalInterestCollected.
     *
     * @param totalInterestCollected New value of property
     * totalInterestCollected.
     */
    public void setTotalInterestCollected(Double totalInterestCollected) {
        this.totalInterestCollected = totalInterestCollected;
    }

    /**
     * Getter for property batch_Id.
     *
     * @return Value of property batch_Id.
     */
    public java.lang.String getBatch_Id() {
        return batch_Id;
    }

    /**
     * Setter for property batch_Id.
     *
     * @param batch_Id New value of property batch_Id.
     */
    public void setBatch_Id(java.lang.String batch_Id) {
        this.batch_Id = batch_Id;
    }

    /**
     * Getter for property trans_Id.
     *
     * @return Value of property trans_Id.
     */
    public java.lang.String getTrans_Id() {
        return trans_Id;
    }

    /**
     * Setter for property trans_Id.
     *
     * @param trans_Id New value of property trans_Id.
     */
    public void setTrans_Id(java.lang.String trans_Id) {
        this.trans_Id = trans_Id;
    }

    /**
     * Getter for property trans_Dt.
     *
     * @return Value of property trans_Dt.
     */
    public java.util.Date getTrans_Dt() {
        return trans_Dt;
    }

    /**
     * Setter for property trans_Dt.
     *
     * @param trans_Dt New value of property trans_Dt.
     */
    public void setTrans_Dt(java.util.Date trans_Dt) {
        this.trans_Dt = trans_Dt;
    }

    /**
     * Getter for property trans_type.
     *
     * @return Value of property trans_type.
     */
    public java.lang.String getTrans_type() {
        return trans_type;
    }

    /**
     * Setter for property trans_type.
     *
     * @param trans_type New value of property trans_type.
     */
    public void setTrans_type(java.lang.String trans_type) {
        this.trans_type = trans_type;
    }

    /**
     * Getter for property tran_Code.
     *
     * @return Value of property tran_Code.
     */
    public java.lang.String getTran_Code() {
        return tran_Code;
    }

    /**
     * Setter for property tran_Code.
     *
     * @param tran_Code New value of property tran_Code.
     */
    public void setTran_Code(java.lang.String tran_Code) {
        this.tran_Code = tran_Code;
    }

    /**
     * Getter for property purchas_Date.
     *
     * @return Value of property purchas_Date.
     */
    public java.util.Date getPurchas_Date() {
        return purchas_Date;
    }

    /**
     * Setter for property purchas_Date.
     *
     * @param purchas_Date New value of property purchas_Date.
     */
    public void setPurchas_Date(java.util.Date purchas_Date) {
        this.purchas_Date = purchas_Date;
    }

    /**
     * Getter for property purchase_Mode.
     *
     * @return Value of property purchase_Mode.
     */
    public java.lang.String getPurchase_Mode() {
        return purchase_Mode;
    }

    /**
     * Setter for property purchase_Mode.
     *
     * @param purchase_Mode New value of property purchase_Mode.
     */
    public void setPurchase_Mode(java.lang.String purchase_Mode) {
        this.purchase_Mode = purchase_Mode;
    }

    /**
     * Getter for property purchse_Through.
     *
     * @return Value of property purchse_Through.
     */
    public java.lang.String getPurchse_Through() {
        return purchse_Through;
    }

    /**
     * Setter for property purchse_Through.
     *
     * @param purchse_Through New value of property purchse_Through.
     */
    public void setPurchse_Through(java.lang.String purchse_Through) {
        this.purchse_Through = purchse_Through;
    }

    /**
     * Getter for property broker_Name.
     *
     * @return Value of property broker_Name.
     */
    public java.lang.String getBroker_Name() {
        return broker_Name;
    }

    /**
     * Setter for property broker_Name.
     *
     * @param broker_Name New value of property broker_Name.
     */
    public void setBroker_Name(java.lang.String broker_Name) {
        this.broker_Name = broker_Name;
    }

    /**
     * Getter for property no_Of_Units.
     *
     * @return Value of property no_Of_Units.
     */
    public Double getNo_Of_Units() {
        return no_Of_Units;
    }

    /**
     * Setter for property no_Of_Units.
     *
     * @param no_Of_Units New value of property no_Of_Units.
     */
    public void setNo_Of_Units(Double no_Of_Units) {
        this.no_Of_Units = no_Of_Units;
    }

    /**
     * Getter for property investment_amount.
     *
     * @return Value of property investment_amount.
     */
    public Double getInvestment_amount() {
        return investment_amount;
    }

    /**
     * Setter for property investment_amount.
     *
     * @param investment_amount New value of property investment_amount.
     */
    public void setInvestment_amount(Double investment_amount) {
        this.investment_amount = investment_amount;
    }

    /**
     * Getter for property discount_Amount.
     *
     * @return Value of property discount_Amount.
     */
    public Double getDiscount_Amount() {
        return discount_Amount;
    }

    /**
     * Setter for property discount_Amount.
     *
     * @param discount_Amount New value of property discount_Amount.
     */
    public void setDiscount_Amount(Double discount_Amount) {
        this.discount_Amount = discount_Amount;
    }

    /**
     * Getter for property premium_Amount.
     *
     * @return Value of property premium_Amount.
     */
    public Double getPremium_Amount() {
        return premium_Amount;
    }

    /**
     * Setter for property premium_Amount.
     *
     * @param premium_Amount New value of property premium_Amount.
     */
    public void setPremium_Amount(Double premium_Amount) {
        this.premium_Amount = premium_Amount;
    }

    /**
     * Getter for property broken_Period_Interest.
     *
     * @return Value of property broken_Period_Interest.
     */
    public Double getBroken_Period_Interest() {
        return broken_Period_Interest;
    }

    /**
     * Setter for property broken_Period_Interest.
     *
     * @param broken_Period_Interest New value of property
     * broken_Period_Interest.
     */
    public void setBroken_Period_Interest(Double broken_Period_Interest) {
        this.broken_Period_Interest = broken_Period_Interest;
    }

    /**
     * Getter for property broken_Commession.
     *
     * @return Value of property broken_Commession.
     */
    public Double getBroken_Commession() {
        return broken_Commession;
    }

    /**
     * Setter for property broken_Commession.
     *
     * @param broken_Commession New value of property broken_Commession.
     */
    public void setBroken_Commession(Double broken_Commession) {
        this.broken_Commession = broken_Commession;
    }

    /**
     * Getter for property purchase_Rate.
     *
     * @return Value of property purchase_Rate.
     */
    public Double getPurchase_Rate() {
        return purchase_Rate;
    }

    /**
     * Setter for property purchase_Rate.
     *
     * @param purchase_Rate New value of property purchase_Rate.
     */
    public void setPurchase_Rate(Double purchase_Rate) {
        this.purchase_Rate = purchase_Rate;
    }

    /**
     * Getter for property years.
     *
     * @return Value of property years.
     */
    public java.lang.Double getYears() {
        return years;
    }

    /**
     * Setter for property years.
     *
     * @param years New value of property years.
     */
    public void setYears(java.lang.Double years) {
        this.years = years;
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
     * Getter for property authorizeMap.
     *
     * @return Value of property authorizeMap.
     */
    public java.util.HashMap getAuthorizeMap() {
        return authorizeMap;
    }

    /**
     * Setter for property authorizeMap.
     *
     * @param authorizeMap New value of property authorizeMap.
     */
    public void setAuthorizeMap(java.util.HashMap authorizeMap) {
        this.authorizeMap = authorizeMap;
    }

    /**
     * Getter for property initiatedDate.
     *
     * @return Value of property initiatedDate.
     */
    public java.util.Date getInitiatedDate() {
        return initiatedDate;
    }

    /**
     * Setter for property initiatedDate.
     *
     * @param initiatedDate New value of property initiatedDate.
     */
    public void setInitiatedDate(java.util.Date initiatedDate) {
        this.initiatedDate = initiatedDate;
    }

    /**
     * Getter for property outstandingAmount.
     *
     * @return Value of property outstandingAmount.
     */
    public java.lang.Double getOutstandingAmount() {
        return outstandingAmount;
    }

    /**
     * Setter for property outstandingAmount.
     *
     * @param outstandingAmount New value of property outstandingAmount.
     */
    public void setOutstandingAmount(java.lang.Double outstandingAmount) {
        this.outstandingAmount = outstandingAmount;
    }

    /**
     * Getter for property maturityAmount.
     *
     * @return Value of property maturityAmount.
     */
    public java.lang.Double getMaturityAmount() {
        return maturityAmount;
    }

    /**
     * Setter for property maturityAmount.
     *
     * @param maturityAmount New value of property maturityAmount.
     */
    public void setMaturityAmount(java.lang.Double maturityAmount) {
        this.maturityAmount = maturityAmount;
    }

    /**
     * Getter for property uptoIntDate.
     *
     * @return Value of property uptoIntDate.
     */
    public java.util.Date getUptoIntDate() {
        return uptoIntDate;
    }

    /**
     * Setter for property uptoIntDate.
     *
     * @param uptoIntDate New value of property uptoIntDate.
     */
    public void setUptoIntDate(java.util.Date uptoIntDate) {
        this.uptoIntDate = uptoIntDate;
    }

    /**
     * Getter for property closeStatus.
     *
     * @return Value of property closeStatus.
     */
    public java.lang.String getCloseStatus() {
        return closeStatus;
    }

    /**
     * Setter for property closeStatus.
     *
     * @param closeStatus New value of property closeStatus.
     */
    public void setCloseStatus(java.lang.String closeStatus) {
        this.closeStatus = closeStatus;
    }

    /**
     * Getter for property excessOrShort.
     *
     * @return Value of property excessOrShort.
     */
    public java.lang.Double getExcessOrShort() {
        return excessOrShort;
    }

    /**
     * Setter for property excessOrShort.
     *
     * @param excessOrShort New value of property excessOrShort.
     */
    public void setExcessOrShort(java.lang.Double excessOrShort) {
        this.excessOrShort = excessOrShort;
    }

    /**
     * Getter for property tblInvestmentTransDet.
     *
     * @return Value of property tblInvestmentTransDet.
     */
    public com.see.truetransact.clientutil.EnhancedTableModel getTblInvestmentTransDet() {
        return tblInvestmentTransDet;
    }

    /**
     * Setter for property tblInvestmentTransDet.
     *
     * @param tblInvestmentTransDet New value of property tblInvestmentTransDet.
     */
    public void setTblInvestmentTransDet(com.see.truetransact.clientutil.EnhancedTableModel tblInvestmentTransDet) {
        this.tblInvestmentTransDet = tblInvestmentTransDet;
    }

    /**
     * Getter for property purchaseSaleBy.
     *
     * @return Value of property purchaseSaleBy.
     */
    public java.lang.String getPurchaseSaleBy() {
        return purchaseSaleBy;
    }

    /**
     * Setter for property purchaseSaleBy.
     *
     * @param purchaseSaleBy New value of property purchaseSaleBy.
     */
    public void setPurchaseSaleBy(java.lang.String purchaseSaleBy) {
        this.purchaseSaleBy = purchaseSaleBy;
    }

    /**
     * Getter for property cbmInvestmentBehavesTrans.
     *
     * @return Value of property cbmInvestmentBehavesTrans.
     */
    public com.see.truetransact.clientutil.ComboBoxModel getCbmInvestmentBehavesTrans() {
        return cbmInvestmentBehavesTrans;
    }

    /**
     * Setter for property cbmInvestmentBehavesTrans.
     *
     * @param cbmInvestmentBehavesTrans New value of property
     * cbmInvestmentBehavesTrans.
     */
    public void setCbmInvestmentBehavesTrans(com.see.truetransact.clientutil.ComboBoxModel cbmInvestmentBehavesTrans) {
        this.cbmInvestmentBehavesTrans = cbmInvestmentBehavesTrans;
    }

    /**
     * Getter for property cboInvestmentBehavesTrans.
     *
     * @return Value of property cboInvestmentBehavesTrans.
     */
    public java.lang.String getCboInvestmentBehavesTrans() {
        return cboInvestmentBehavesTrans;
    }

    /**
     * Setter for property cboInvestmentBehavesTrans.
     *
     * @param cboInvestmentBehavesTrans New value of property
     * cboInvestmentBehavesTrans.
     */
    public void setCboInvestmentBehavesTrans(java.lang.String cboInvestmentBehavesTrans) {
        this.cboInvestmentBehavesTrans = cboInvestmentBehavesTrans;
    }

    /**
     * Getter for property unExists.
     *
     * @return Value of property unExists.
     */
    public boolean isUnExists() {
        return unExists;
    }

    /**
     * Setter for property unExists.
     *
     * @param unExists New value of property unExists.
     */
    public void setUnExists(boolean unExists) {
        this.unExists = unExists;
    }

    /**
     * Getter for property closerType.
     *
     * @return Value of property closerType.
     */
    public java.lang.String getCloserType() {
        return closerType;
    }

    /**
     * Setter for property closerType.
     *
     * @param closerType New value of property closerType.
     */
    public void setCloserType(java.lang.String closerType) {
        this.closerType = closerType;
    }

    /**
     * Getter for property preCloserRate.
     *
     * @return Value of property preCloserRate.
     */
    public java.lang.String getPreCloserRate() {
        return preCloserRate;
    }

    /**
     * Setter for property preCloserRate.
     *
     * @param preCloserRate New value of property preCloserRate.
     */
    public void setPreCloserRate(java.lang.String preCloserRate) {
        this.preCloserRate = preCloserRate;
    }

    /**
     * Getter for property amortizationAmt.
     *
     * @return Value of property amortizationAmt.
     */
    public java.lang.String getAmortizationAmt() {
        return amortizationAmt;
    }

    /**
     * Setter for property amortizationAmt.
     *
     * @param amortizationAmt New value of property amortizationAmt.
     */
    public void setAmortizationAmt(java.lang.String amortizationAmt) {
        this.amortizationAmt = amortizationAmt;
    }

    /**
     * Getter for property closerDate.
     *
     * @return Value of property closerDate.
     */
    public java.util.Date getCloserDate() {
        return closerDate;
    }

    /**
     * Setter for property closerDate.
     *
     * @param closerDate New value of property closerDate.
     */
    public void setCloserDate(java.util.Date closerDate) {
        this.closerDate = closerDate;
    }

    /**
     * Getter for property investmentStatus.
     *
     * @return Value of property investmentStatus.
     */
    public java.lang.String getInvestmentStatus() {
        return investmentStatus;
    }

    /**
     * Setter for property investmentStatus.
     *
     * @param investmentStatus New value of property investmentStatus.
     */
    public void setInvestmentStatus(java.lang.String investmentStatus) {
        this.investmentStatus = investmentStatus;
    }

    /**
     * Getter for property currDt.
     *
     * @return Value of property currDt.
     */
    public java.util.Date getCurrDt() {
        return currDt;
    }

    /**
     * Setter for property currDt.
     *
     * @param currDt New value of property currDt.
     */
    public void setCurrDt(java.util.Date currDt) {
        this.currDt = currDt;
    }

    /**
     * Getter for property txtAccRefNo.
     *
     * @return Value of property txtAccRefNo.
     */
    public java.lang.String getTxtAccRefNo() {
        return txtAccRefNo;
    }

    /**
     * Setter for property txtAccRefNo.
     *
     * @param txtAccRefNo New value of property txtAccRefNo.
     */
    public void setTxtAccRefNo(java.lang.String txtAccRefNo) {
        this.txtAccRefNo = txtAccRefNo;
    }

    /**
     * Getter for property txtInternalAccNo.
     *
     * @return Value of property txtInternalAccNo.
     */
    public java.lang.String getTxtInternalAccNo() {
        return txtInternalAccNo;
    }

    /**
     * Setter for property txtInternalAccNo.
     *
     * @param txtInternalAccNo New value of property txtInternalAccNo.
     */
    public void setTxtInternalAccNo(java.lang.String txtInternalAccNo) {
        this.txtInternalAccNo = txtInternalAccNo;
    }

    /**
     * Getter for property cboInvestmentType.
     *
     * @return Value of property cboInvestmentType.
     */
    public java.lang.String getCboInvestmentType() {
        return cboInvestmentType;
    }

    /**
     * Setter for property cboInvestmentType.
     *
     * @param cboInvestmentType New value of property cboInvestmentType.
     */
    public void setCboInvestmentType(java.lang.String cboInvestmentType) {
        this.cboInvestmentType = cboInvestmentType;
    }

    /**
     * Getter for property cbmInvestmentType.
     *
     * @return Value of property cbmInvestmentType.
     */
    public com.see.truetransact.clientutil.ComboBoxModel getCbmInvestmentType() {
        return cbmInvestmentType;
    }

    /**
     * Setter for property cbmInvestmentType.
     *
     * @param cbmInvestmentType New value of property cbmInvestmentType.
     */
    public void setCbmInvestmentType(com.see.truetransact.clientutil.ComboBoxModel cbmInvestmentType) {
        this.cbmInvestmentType = cbmInvestmentType;
    }

    /**
     * Getter for property rdoSBorCA.
     *
     * @return Value of property rdoSBorCA.
     */
    public java.lang.String getRdoSBorCA() {
        return rdoSBorCA;
    }

    /**
     * Setter for property rdoSBorCA.
     *
     * @param rdoSBorCA New value of property rdoSBorCA.
     */
    public void setRdoSBorCA(java.lang.String rdoSBorCA) {
        this.rdoSBorCA = rdoSBorCA;
    }

    /**
     * Getter for property cboInvestmentTypeSBorCA.
     *
     * @return Value of property cboInvestmentTypeSBorCA.
     */
    public java.lang.String getCboInvestmentTypeSBorCA() {
        return cboInvestmentTypeSBorCA;
    }

    /**
     * Setter for property cboInvestmentTypeSBorCA.
     *
     * @param cboInvestmentTypeSBorCA New value of property
     * cboInvestmentTypeSBorCA.
     */
    public void setCboInvestmentTypeSBorCA(java.lang.String cboInvestmentTypeSBorCA) {
        this.cboInvestmentTypeSBorCA = cboInvestmentTypeSBorCA;
    }

    /**
     * Getter for property txtInvestmentIDTransSBorCA.
     *
     * @return Value of property txtInvestmentIDTransSBorCA.
     */
    public java.lang.String getTxtInvestmentIDTransSBorCA() {
        return txtInvestmentIDTransSBorCA;
    }

    /**
     * Setter for property txtInvestmentIDTransSBorCA.
     *
     * @param txtInvestmentIDTransSBorCA New value of property
     * txtInvestmentIDTransSBorCA.
     */
    public void setTxtInvestmentIDTransSBorCA(java.lang.String txtInvestmentIDTransSBorCA) {
        this.txtInvestmentIDTransSBorCA = txtInvestmentIDTransSBorCA;
    }

    /**
     * Getter for property txtInvestmentRefNoTrans.
     *
     * @return Value of property txtInvestmentRefNoTrans.
     */
    public java.lang.String getTxtInvestmentRefNoTrans() {
        return txtInvestmentRefNoTrans;
    }

    /**
     * Setter for property txtInvestmentRefNoTrans.
     *
     * @param txtInvestmentRefNoTrans New value of property
     * txtInvestmentRefNoTrans.
     */
    public void setTxtInvestmentRefNoTrans(java.lang.String txtInvestmentRefNoTrans) {
        this.txtInvestmentRefNoTrans = txtInvestmentRefNoTrans;
    }

    /**
     * Getter for property txtInvestmentInternalNoTrans.
     *
     * @return Value of property txtInvestmentInternalNoTrans.
     */
    public java.lang.String getTxtInvestmentInternalNoTrans() {
        return txtInvestmentInternalNoTrans;
    }

    /**
     * Setter for property txtInvestmentInternalNoTrans.
     *
     * @param txtInvestmentInternalNoTrans New value of property
     * txtInvestmentInternalNoTrans.
     */
    public void setTxtInvestmentInternalNoTrans(java.lang.String txtInvestmentInternalNoTrans) {
        this.txtInvestmentInternalNoTrans = txtInvestmentInternalNoTrans;
    }

    /**
     * Getter for property cbmInvestmentTypeSBorCA.
     *
     * @return Value of property cbmInvestmentTypeSBorCA.
     */
    public com.see.truetransact.clientutil.ComboBoxModel getCbmInvestmentTypeSBorCA() {
        return cbmInvestmentTypeSBorCA;
    }

    /**
     * Setter for property cbmInvestmentTypeSBorCA.
     *
     * @param cbmInvestmentTypeSBorCA New value of property
     * cbmInvestmentTypeSBorCA.
     */
    public void setCbmInvestmentTypeSBorCA(com.see.truetransact.clientutil.ComboBoxModel cbmInvestmentTypeSBorCA) {
        this.cbmInvestmentTypeSBorCA = cbmInvestmentTypeSBorCA;
    }

    /**
     * Getter for property txtChequeNo.
     *
     * @return Value of property txtChequeNo.
     */
    public java.lang.String getTxtChequeNo() {
        return txtChequeNo;
    }

    /**
     * Setter for property txtChequeNo.
     *
     * @param txtChequeNo New value of property txtChequeNo.
     */
    public void setTxtChequeNo(java.lang.String txtChequeNo) {
        this.txtChequeNo = txtChequeNo;
    }

    /**
     * Getter for property renewalMap.
     *
     * @return Value of property renewalMap.
     */
    public java.util.HashMap getRenewalMap() {
        return renewalMap;
    }

    /**
     * Setter for property renewalMap.
     *
     * @param renewalMap New value of property renewalMap.
     */
    public void setRenewalMap(java.util.HashMap renewalMap) {
        this.renewalMap = renewalMap;
    }

    /**
     * Getter for property txtNarration.
     *
     * @return Value of property txtNarration.
     */
    public java.lang.String getTxtNarration() {
        return txtNarration;
    }

    /**
     * Setter for property txtNarration.
     *
     * @param txtNarration New value of property txtNarration.
     */
    public void setTxtNarration(java.lang.String txtNarration) {
        this.txtNarration = txtNarration;
    }

    /**
     * Getter for property callingTransAcctNo.
     *
     * @return Value of property callingTransAcctNo.
     */
    public java.lang.String getCallingTransAcctNo() {
        return callingTransAcctNo;
    }

    /**
     * Setter for property callingTransAcctNo.
     *
     * @param callingTransAcctNo New value of property callingTransAcctNo.
     */
    public void setCallingTransAcctNo(java.lang.String callingTransAcctNo) {
        this.callingTransAcctNo = callingTransAcctNo;
    }

    public Double getTxtPrematureROI() {
        return txtPrematureROI;
    }

    public void setTxtPrematureROI(Double txtPrematureROI) {
        this.txtPrematureROI = txtPrematureROI;
    }

  

    public Double getTxtPrematureIntAmt() {
        return txtPrematureIntAmt;
    }

    public void setTxtPrematureIntAmt(Double txtPrematureIntAmt) {
        this.txtPrematureIntAmt = txtPrematureIntAmt;
    }

    
    /**
     * Getter for property closingType.
     *
     * @return Value of property closingType.
     */
    public java.lang.String getClosingType() {
        return closingType;
    }

    /**
     * Setter for property closingType.
     *
     * @param closingType New value of property closingType.
     */
    public void setClosingType(java.lang.String closingType) {
        this.closingType = closingType;
    }

    /**
     * Getter for property interestType.
     *
     * @return Value of property interestType.
     */
    public java.lang.String getInterestType() {
        return interestType;
    }

    /**
     * Setter for property interestType.
     *
     * @param interestType New value of property interestType.
     */
    public void setInterestType(java.lang.String interestType) {
        this.interestType = interestType;
    }

    public EnhancedTableModel getTblCheckBookTable() {
        return tblCheckBookTable;
    }

    public void setTblCheckBookTable(EnhancedTableModel tblCheckBookTable) {
        this.tblCheckBookTable = tblCheckBookTable;
    }

    //    /**
    //     * Getter for property _authorizeMap.
    //     * @return Value of property _authorizeMap.
    //     */
    //    public java.util.HashMap getauthorizeMap() {
    //        return _authorizeMap;
    //    }
    //
    //    /**
    //     * Setter for property _authorizeMap.
    //     * @param _authorizeMap New value of property _authorizeMap.
    //     */
    //    public void setauthorizeMap(java.util.HashMap _authorizeMap) {
    //        this._authorizeMap = _authorizeMap;
    //    }
    //
    public HashMap getFinalMap() {
        return finalMap;
    }

    public void setFinalMap(HashMap finalMap) {
        this.finalMap = finalMap;
    }

    public String getTxtDebitParticulars() {
        return txtDebitParticulars;
    }

    public void setTxtDebitParticulars(String txtDebitParticulars) {
        this.txtDebitParticulars = txtDebitParticulars;
    }

    public String getTxtCreditParticulars() {
        return txtCreditParticulars;
    }

    public void setTxtCreditParticulars(String txtCreditParticulars) {
        this.txtCreditParticulars = txtCreditParticulars;
    }

    public String getTxtNarration1() {
        return txtNarration1;
    }

    public void setTxtNarration1(String txtNarration1) {
        this.txtNarration1 = txtNarration1;
    }

    public Date getTdtTransactionDt() {
        return tdtTransactionDt;
    }

    public void setTdtTransactionDt(Date tdtTransactionDt) {
        this.tdtTransactionDt = tdtTransactionDt;
    }
    
}