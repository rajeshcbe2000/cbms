/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition.
 * 
 * TermLoanOB.java
 *
 * Created on January 8, 2004, 4:24 PM
 */
package com.see.truetransact.ui.termloan.depositLoan;

import com.see.truetransact.clientutil.ClientConstants;
import com.see.truetransact.clientutil.ClientUtil;
import com.see.truetransact.clientutil.ComboBoxModel;
import com.see.truetransact.clientutil.EnhancedTableModel;
import com.see.truetransact.clientutil.TableModel;
import com.see.truetransact.clientutil.TableUtil;
import com.see.truetransact.ui.common.nominee.*;
import com.see.truetransact.clientexception.ClientParseException;
import com.see.truetransact.clientproxy.ProxyFactory;
import com.see.truetransact.clientproxy.ProxyParameters;
import com.see.truetransact.commonutil.CommonConstants;
import com.see.truetransact.commonutil.DateUtil;
import com.see.truetransact.commonutil.CommonUtil;
//import com.see.truetransact.commonutil.TTException;
import com.see.truetransact.transferobject.termloan.TermLoanJointAcctTO;
import com.see.truetransact.transferobject.termloan.TermLoanBorrowerTO;
import com.see.truetransact.transferobject.termloan.TermLoanClassificationTO;
import com.see.truetransact.transferobject.termloan.TermLoanSecurityTO;
import com.see.truetransact.transferobject.termloan.TermLoanCompanyTO;
import com.see.truetransact.transferobject.termloan.TermLoanFacilityTO;
import com.see.truetransact.transferobject.termloan.TermLoanOtherDetailsTO;
import com.see.truetransact.transferobject.termloan.TermLoanSanctionFacilityTO;
import com.see.truetransact.transferobject.termloan.TermLoanSanctionTO;
import com.see.truetransact.commonutil.interestcalc.Rounding;
import com.see.truetransact.transferobject.termloan.TermLoanExtenFacilityDetailsTO;
import com.see.truetransact.transferobject.transaction.cash.CashTransactionTO;
import com.see.truetransact.transferobject.transaction.transfer.TxTransferTO;

import com.see.truetransact.ui.termloan.loandisbursement.LoanDisbursementOB;
import com.see.truetransact.transferobject.common.mobile.SMSSubscriptionTO;
import com.see.truetransact.transferobject.servicetax.servicetaxdetails.ServiceTaxDetailsTO;

import com.see.truetransact.ui.TrueTransactMain;
import com.see.truetransact.uicomponent.CObservable;
import com.see.truetransact.uicomponent.COptionPane;
import com.see.truetransact.ui.deposit.lien.DepositLienUI;
import com.see.truetransact.uicomponent.CTable;

import com.see.truetransact.transferobject.termloan.TermLoanInterestTO;
import com.see.truetransact.transferobject.termloan.TermLoanRepaymentTO;
import com.see.truetransact.transferobject.termloan.TermLoanInstallmentTO;
import com.see.truetransact.transferobject.termloan.TermLoanInstallMultIntTO;
import com.see.truetransact.ui.common.servicetax.ServiceTaxCalculation;



import org.apache.log4j.Logger;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Set;
import java.util.Date;
import java.util.Observable;

/**
 *
 * @author shanmugavel Created on January 8, 2004, 4:24 PM
 *
 */
public class DepositLoanOB extends CObservable {

    String chittalNoMds = "";
    String membNo = "";
    String membType = "";
    String membName = "";
    String chittAmt = "";
    private String chkDepositLien = "";
    private int deleteFlag = 0;
    private int actionType;
    private int resultStatus;
    private int result;
    private String lblStatus = ClientConstants.ACTION_STATUS[ClientConstants.ACTIONTYPE_CANCEL];
    private static DepositLoanOB depositLoanOB;
    private final static Logger log = Logger.getLogger(DepositLoanOB.class);
    private final static ClientParseException parseException = ClientParseException.getInstance();
    private final String ACC_LIMIT = "ACC_LIMIT";
    private final String ACC_TYPE = "ACC_TYPE";
    private final String ACCT_NAME = "ACCT_NAME";
    private final String ACCT_NUM = "ACCT_NUM";
    private final String ACCT_STATUS = "ACCT_STATUS";
    private final String ALL_VALUES = "ALL_VALUES";
    private final String AOD_DATE = "AOD_DATE";
    private final String AUTHORIZE = "AUTHORIZE";
    private final String AUTHORIZED = "AUTHORIZED";
    private final String AUTHORIZE_BY_1 = "AUTHORIZE_BY_1";
    private final String AUTHORIZE_BY_2 = "AUTHORIZE_BY_2";
    private final String AUTHORIZE_DT_1 = "AUTHORIZE_DT_1";
    private final String AUTHORIZE_DT_2 = "AUTHORIZE_DT_2";
    private final String AUTHORIZE_STATUS_1 = "AUTHORIZE_STATUS_1";
    private final String AUTHORIZE_STATUS_2 = "AUTHORIZE_STATUS_2";
    private final String AVAILABLE_BALANCE = "AVAILABLE_BALANCE";
    private final String CLEAR_BALANCE = "CLEAR_BALANCE";
    private final String COMMAND = "COMMAND";
    private final String COMPOUND = "COMPOUND";
    private final String CONTACT_PERSON = "CONTACT_PERSON";
    private final String CONTACT_PHONE = "CONTACT_PHONE";
    private final String CUSTOMERGROUP = "CUSTOMERGROUP";
    private final String DELETE = "DELETE";
    private final String EXCEPTION = "EXCEPTION";
    private final String FACILITY_REPAY_DATE = "FACILITY_REPAY_DATE";
    private final String FACILITY_TYPE = "FACILITY_TYPE";
    private final String FALSE = "FALSE";
    private final String FLAG = "FLAG";
    private final String FROM = "FROM";
    private final String FULLY_SECURED = "FULLY_SECURED";
    private final String GROUP_DESC = "GROUP_DESC";
    private final String GUARANTAR = "GUARANTAR";
    private final String INSERT = "INSERT";
    private final String INSPECT_INSURANCE_GUARANTAR = "INSPECT,INSURANCE,GUARANTAR";
    private final String INSTALLMENT_DETAILS = "INSTALLMENT_DETAILS";
    private final String INSTALLMULTINT_DETAILS = "INSTALLMULTINT_DETAILS";
    private final String INT_GET_FROM = "INT_GET_FROM";
    private final String INTEREST = "INTEREST";
//    private final   String    INTEREST_NATURE = "INTEREST_NATURE";
    private final String INTEREST_TYPE = "INTEREST_TYPE";
    private final String INSURANCE = "INSURANCE";
    private final String INSURANCE_GUARANTAR = "INSURANCE,GUARANTAR";
    private final String LIMIT = "LIMIT";
    private final String INITIAL_MONEY_DEPOSIT = "INITIAL_MONEY_DEPOSIT";
    private final String MAIN = "MAIN";
    private final String MAX_DEL_SAN_DETAIL_SL_NO = "MAX_DEL_SAN_DETAIL_SL_NO";
//    private final   String    MAX_DEL_SAN_SL_NO = "MAX_DEL_SAN_SL_NO";
    private final String MORATORIUM_GIVEN = "MORATORIUM_GIVEN";
    private final String MORATORIUM_PERIOD = "MORATORIUM_PERIOD";
    private final String MULTI_DISBURSE = "MULTI_DISBURSE";
    private final String DRAWING_POWER = "DRAWING_POWER";
    private final String NEW = "NEW";
//    private final   String    NIL = "NIL";
    private final String NO = "N";
    private final String NO_INSTALLMENTS = "NO_INSTALLMENTS";
    private final String NON_PLR = "NON_PLR";
    private final String NOTE_DATE = "NOTE_DATE";
    private final String NOTE_EXP_DATE = "NOTE_EXP_DATE";
    private final String NUMERIC_ZERO = "0";
    private final String OPTION = "OPTION";
    private final String PARTLY_SECURED = "PARTLY_SECURED";
    private final String PLR = "PLR";
    private final String PLR_RATE = "PLR_RATE";
    private final String PLR_RATE_APPL = "PLR_RATE_APPL";
    private final String PROD_ID = "PROD_ID";
    private final String PURPOSE_DESC = "PURPOSE_DESC";
    private final String REJECT = "REJECT";
    private final String REMARKS = "REMARKS";
    private final String ACCT_OPEN_DT = "ACCT_OPEN_DT";
    private final String ACCT_CLOSE_DT = "ACCT_CLOSE_DT";
    private final String RECOMMANDED_BY = "RECOMMANDED_BY";
    private final String REPAY_FREQ = "REPAY_FREQ";
    private final String REPAYMENT_DETAILS = "REPAYMENT_DETAILS";
    private final String INSTALLMENT_ALL_DETAILS = "INSTALLMENT_ALL_DETAILS";
    private final String RISK_WEIGHT = "RISK_WEIGHT";
    private final String SANCTION = "SANCTION";
    private final String SANCTION_AUTHORITY = "SANCTION_AUTHORITY";
    private final String SANCTION_DATE = "SANCTION_DATE";
    private final String SANCTION_DT = "SANCTION_DT";
    private final String SANCTION_FACILITY = "SANCTION_FACILITY";
    private final String SANCTION_FACILITY_ALL = "SANCTION_FACILITY_ALL";
    private final String SANCTION_FACILITY_DELETED = "SANCTION_FACILITY_DELETED";
    private final String SANCTION_FACILITY_TABLE = "SANCTION_FACILITY_TABLE";
    private final String SANCTION_MODE = "SANCTION_MODE";
    private final String SANCTION_NO = "SANCTION_NO";
    private final String SANCTION_SL_NO = "SANCTION_SL_NO";
    private final String SECURITY_DETAILS = "SECURITY_DETAILS";
    private final String SECURITY_TYPE = "SECURITY_TYPE";
    private final String SHADOW_CREDIT = "SHADOW_CREDIT";
    private final String SHADOW_DEBIT = "SHADOW_DEBIT";
    private final String SIMPLE = "SIMPLE";
    private final String SLNO = "SLNO";
    private final String DEP_GET_PROD_ID = "LTD.getDepositProducts";
    private final String POFATTORNEY = "POFATTORNEY";
    private final String DOCDETAILS = "DOCDETAILS";
    private final String AUTHSIGNATORY = "AUTHSIGNATORY";   // NEW CODE
    private final String ACCTTRANSFER = "ACCTTRANSFER";
    private final String STOCK_INSPECT = "STOCK_INSPECT";
    private final String STOCK_INSPECT_GUARANTAR = "STOCK_INSPECT,GUARANTAR";
    private final String STOCK_INSPECT_INSURANCE = "STOCK_INSPECT,INSURANCE";
    private final String SUBMIT = "SUBMIT";
    private final String SUBSIDY = "SUBSIDY";
    private final String TABLE_VALUES = "TABLE_VALUES";
    private final String TO = "TO";
    private final String TO_DT = "TO_DT";
    private final String TOTAL_AVAILABLE_BALANCE = "TOTAL_AVAILABLE_BALANCE";
    private final String TRANSFERED = "TRANSFERED";
    private final String TRUE = "TRUE";
    private final String UNCLEAR_BALANCE = "UNCLEAR_BALANCE";
    private final String UNSECURED = "UNSECURED";
    private final String UPDATE = "UPDATE";
    private final String YES = "Y";
    private final String FIXED_RATE = "FIXED_RATE";
    private final ArrayList sanctionFacilityTitle = new ArrayList();  //  Table Title of Saction facility
    private final ArrayList sanctionMainTitle = new ArrayList();      //  Table Title of Sanction Main
    private final ArrayList sanctionMainTitleMds = new ArrayList();
    private final ArrayList shareTitle = new ArrayList();
    //    private final   TermLoanRB objTermLoanRB = new TermLoanRB();
    private final java.util.ResourceBundle objTermLoanRB = java.util.ResourceBundle.getBundle("com.see.truetransact.ui.termloan.depositLoan.DepositLoanRB", ProxyParameters.LANGUAGE);
    private EnhancedTableModel tblSanctionFacility;
    private EnhancedTableModel tblSanctionMain;
    private EnhancedTableModel tblSanctionMainMds;
    private EnhancedTableModel tblShare;
    private ProxyFactory proxy = null;
    private ArrayList LoanNo = new ArrayList();
    private ArrayList key;
    private ArrayList value;
    private ArrayList sanctionFacilityTabRow;
    private ArrayList sanctionFacilityAllTabRecords = new ArrayList(); // ArrayList to display in Sanction Facility
    private ArrayList sanctionMainTabRow;
    private ArrayList sanctionMainAllTabRecords = new ArrayList();     // ArrayList to display in Sanction Main
    private ArrayList facilityTabSanction;
    private ArrayList installmentTitle;
    private ArrayList allTblDataList;
    private ArrayList deleteTblDataList;
    private ArrayList newTblDataList;
    private ArrayList totDepositList;
    private LinkedHashMap objRepaymentInstallmentAllMap = new LinkedHashMap();
    private HashMap installmentAllMap = new HashMap();
    private LinkedHashMap sanctionFacilityAll = new LinkedHashMap();   // Both displayed and hidden values in the table
    private LinkedHashMap sanctionMainAll = new LinkedHashMap();       // Both displayed and hidden values in the table
    private LinkedHashMap facilityAll = new LinkedHashMap();           // Both displayed and hidden values in the table
//    private LinkedHashMap allScrDataMap=new LinkedHashMap();
    private HashMap singleScreenMap = new HashMap();
    private HashMap removedFaccilityTabValues = new HashMap();
    private HashMap sanctionFacilityRecord;
    private HashMap sanctionMainRecord;
    private HashMap facilityRecord;
    private HashMap lookUpHash;
    private HashMap operationMap;
    private HashMap keyValue;
    private HashMap authorizeMap;
    private HashMap transactionMap;
    private ComboBoxModel cbmConstitution;
    private ComboBoxModel cbmCategory;
    private ComboBoxModel cbmAcctStatus;
    private ComboBoxModel cbmSanctioningAuthority;
    private ComboBoxModel cbmModeSanction;
    private ComboBoxModel cbmDepositProduct;
    private ComboBoxModel cbmLoanProduct;
    private ComboBoxModel cbmRepayFreq;
    private ComboBoxModel cbmRepayFreq_LOAN;
    private ComboBoxModel cbmRepayFreq_ADVANCE;
    private ComboBoxModel cbmTypeOfFacility;
    private ComboBoxModel cbmSanctionSlNo;
    private ComboBoxModel cbmProductId;
    private ComboBoxModel cbmInterestType;
    private ComboBoxModel cbmRecommendedByType;
    private ComboBoxModel cbmIntGetFrom;
    private ComboBoxModel cbmAppraiserId;
    private boolean chkDocDetails = false;
    private boolean chkAuthorizedSignatory = false;
    private boolean chkPOFAttorney = false;
    private TableUtil tableUtilSanction_Facility = new TableUtil();
    private TableUtil tableUtilSanction_Main = new TableUtil();
    private String cboAccStatus = "";
    private String cboConstitution = "";
    private String cboCategory = "";
    private String cboSanctioningAuthority = "";
    private String cboModeSanction = "";
    private String cboLoanProduct = "";
    private String cboDepositProduct = "";
    private String cboIntGetFrom = "";
    private String txtSanctionNo = "";
    private String txtAcct_Name = "";
    private String tdtSanctionDate = "";
    private String txtDepositNo = "";
    private String lblCustNameValue = "";
    private String lblCustomerIdValue = "";
    private String lblMemberIdValue = "";
    private String lblShowAccountHeadId = "";
    private String txtInter = "";
    private String tdtRepaymentDt = "";
    private String tdtAccountOpenDate = "";
    private String txtLoanAmt = "";
    private String txtTotalNoOfShare = "";
    private String txtTotalShareAmount = "";
    private String txtSanctionRef = "";
    private String lblAcctNo_Sanction_Disp = "";
    private String depositAvilableAmt = "";
    private String lblEligibileAmtValue = "";
    //charge details
    //  private HashMap newTransactionMap; //charge details
    private List chargelst = null; //charge details
    // private HashMap operationMap; //charge details
    //charge end..
    //abi
    private String dep_mat_dt = "";
    private String dep_mat_amt = "";
    private String dep_credit_int = "";
    private String dep_rate_int = "";
    private String deposit_amt = "";
//    private String cboAccStatus = "";
//    private String cboSanctioningAuthority = "";
    private String txtSanctionRemarks = "";
    private String txtNoInstallments = "";
    private String cboRepayFreq = "";
    private String cboTypeOfFacility = "";
    private String txtLimit_SD = "";
    private String txtLimit_SDMoneyDeposit = "";
    private String txtFacility_Moratorium_Period = "";
    private String tdtFacility_Repay_Date = "";
    private boolean chkMoratorium_Given = false;
    private String tdtFDate = "";
    private String tdtTDate = "";
    private String sanctionSlNo = "";
    private String cboProductId = "";
    private boolean rdoSecurityDetails_Unsec = false;
    private boolean rdoSecurityDetails_Partly = false;
    private boolean rdoSecurityDetails_Fully = false;
    private boolean chkStockInspect = false;
    private boolean chkInsurance = false;
    private boolean chkGurantor = false;
    private boolean chkAcctTransfer = false;
    private boolean rdoAccType_New = false;
    private boolean rdoAccType_Transfered = false;
    private boolean rdoAccLimit_Main = false;
    private boolean rdoAccLimit_Submit = false;
    private boolean rdoNatureInterest_PLR = false;
    private boolean rdoNatureInterest_NonPLR = false;
    private String cboInterestType = "";
    private boolean rdoRiskWeight_Yes = false;
    private boolean rdoRiskWeight_No = false;
    private String tdtDemandPromNoteDate = "";
    private String tdtDemandPromNoteExpDate = "";
    private String tdtAODDate = "";
    private boolean rdoMultiDisburseAllow_Yes = false;
    private boolean rdoMultiDisburseAllow_No = false;
    private boolean rdoSubsidy_Yes = false;
    private boolean rdoSubsidy_No = false;
    private String txtPurposeDesc = "";
    private String txtGroupDesc = "";
    private boolean rdoInterest_Simple = false;
    private boolean rdoInterest_Compound = false;
    private boolean rdoRecarable_Yes = false;
    private boolean rdoRecarable_No = false;
    private boolean rdoDP_YES = false;
    private boolean rdoDP__NO = false;
    private String txtContactPerson = "";
    private String txtContactPhone = "";
    private String txtRemarks = "";
    private String cboRecommendedByType = "";
    private String AccountOpenDate = "";
    private String accountCloseDate = "";
    private String cboProdId = "";
    private String cboAppraiserId = "";
    private String cboProdID_RS = "";
    private String cboProdID_IDetail = "";
    private String borrowerNo = "";
    private String lblAccHead_2 = "";
    private String lblDepositNo = "";
    private String lblProductID_FD_Disp = "";
    private String lblProdID_IDetail_Disp = "";
    private String lblAccountHead_FD_Disp = "";
    private String lblAccHead_Idetail_2 = "";
    private String lblAccNo_Idetail_2 = "";
    private String lblDrawingPower_2 = "";
    private String txtPeriodDifference_Days = "";
    private String txtPeriodDifference_Months = "";
    private String txtPeriodDifference_Years = "";
    private String txtSanctionSlNo = "";
    private String strRealSanctionNo = "";
    private Double minLimitValue = new Double("0");
    private Double maxLimitValue = new Double("0");
    private Double minLoanPeriod = new Double("0");
    private Double maxLoanPeriod = new Double("0");
    private double eligibleMargin = 0.0;
    private String loanDisbursalAmt = "";
    private double eligibleAmt = 0.0;
    private String minDecLoanPeriod = "0";
    private String maxDecLoanPeriod = "0";
    private String interestMode = CommonConstants.TOSTATUS_INSERT;     // Mode of Interest Details
    private String classifiMode = CommonConstants.TOSTATUS_INSERT;     // Mode of Classification Details
    private String strACNumber = "";                                  // Current Account Number in Edit/Delete/Authorize Mode
    private String loanType = "";
    private HashMap depositCustDetMap;
    private LinkedHashMap isMultipleDeposit;
    private boolean lienChanged = false;
    private String loanACNo = "";
    private String depositNo = "";
    private String deleteAction;
    private String ProductFacilityType = "";
    private String BEHAVES_LIKE = "";
    private String renewAvailableBalance = "";
    Date curDate = null;
    private boolean updateAvailableBalance = false;
    private String oldSanction_no = "";
    private HashMap advanceLiablityMap = new HashMap();
    private String sanctionAmtRoundOff = "";
    private boolean canBorrowerDelete = false;
    private double shadowDebit = 0;
    private double shadowCredit = 0;
    private double clearBalance = 0;
    private double availableBalance = 0;
    private String partReject = "";
    public String tdtRepaymentDate = "";
    public String currDepositRepaymentDt = "";
    public String previousDepositRepaymentDt = "";
    private double slNoForSanction = 0;
    private double oldDepositIntRate = 0;
    private double depositInterest = 0;
    //interest calculation values storing
    private String interest = "";
    private String penalInterest = "";
    private String cboPurposeOfLoan = "";
    private ComboBoxModel cbmPurposeOfLoan;
    private String securityAccHeadValue = "";
    private String loadingProdId = "";
    private String newCustIdNo = "";
    private Date last_int_calc_dt = null;
    private String authorizeDt = "";
    private String txtNomineeNameNO = "";
    private String cboNomineeRelationNO = "";
    private String txtNomineePhoneNO = "";
    private String txtNomineeACodeNO = "";
    private String txtNomineeShareNO = "";
    private ComboBoxModel cbmNomineeRelationNO;
    private TermLoanInterestTO existTermLoanInterestTO;
    private HashMap newTransactionMap;
    //mobile banking
    private boolean isMobileBanking = false;
    SMSSubscriptionTO objSMSSubscriptionTO = null;
    private String txtMobileNo = "";
    private String tdtMobileSubscribedFrom = "";
    private TermLoanBorrowerTO objExistTermLoanBorrowerTO;
    private ArrayList nomineeTOList;
    public TermLoanSecurityTO objTermLoanSecurityTO = null;
    //  private List chargelst = null;
    //Added By Suresh
    private String productAuthRemarks = "";
    private String keyValueForPaddyAndMDSLoans = "";
    private Map paddyMap = null;
    private Map mdsMap = null;
    private String fromDate = "";
    private String toDate = "";
    private String firstInstallDate = "";
    private String repayScheduleMode = null;
    private DepositLoanRepaymentOB termLoanRepaymentOB;
    private String depoBehavesLike="";
    private String lblServiceTaxval="";
    private HashMap serviceTax_Map=null;
    private HashMap jointCustMap ; // Added by nithya on 24-09-2020 for KD-2275

    static {
        try {
            depositLoanOB = new DepositLoanOB();
        } catch (Exception e) {
            log.info("try: " + e);
            parseException.logException(e, true);
            e.printStackTrace();
        }
    }

    /**
     * Returns an instance of depositLoanOB
     *
     * @return depositLoanOB
     */
    public static DepositLoanOB getInstance() {
        return depositLoanOB;
    }

    public DepositLoanOB() {
        try {
            setOperationMap();
            curDate = ClientUtil.getCurrentDate();
            proxy = ProxyFactory.createProxy();
             
            termLoanOB();
        } catch (Exception e) {
            parseException.logException(e, true);
            e.printStackTrace();
            log.info("DepositLoanOB..." + e);
        }
    }

    private void termLoanOB() throws Exception {
        fillDropdown();
        // setSanctionFacilityTitle();
        if (getCboLoanProduct().equals(partReject)) {
            setSanctionMainTitle();
        }
        setSanctionMainTitleMds();
        loanType = "LTD";
        // setShareTitle();
        tableUtilSanction_Facility.setAttributeKey(SLNO);
        tableUtilSanction_Main.setAttributeKey(SANCTION_SL_NO);
        tblSanctionFacility = new EnhancedTableModel(null, sanctionFacilityTitle);
        tblSanctionMain = new EnhancedTableModel(null, sanctionMainTitle);
        tblSanctionMainMds = new EnhancedTableModel(null, sanctionMainTitleMds);
        tblShare = new EnhancedTableModel(null, shareTitle);
        termLoanRepaymentOB = new DepositLoanRepaymentOB();
        notifyObservers();
    }
    private void setSanctionFacilityTitle() throws Exception {
        try {
            sanctionFacilityTitle.add(objTermLoanRB.getString("tblColumnSanction_Facility1"));
            sanctionFacilityTitle.add(objTermLoanRB.getString("tblColumnSanction_Facility2"));
            sanctionFacilityTitle.add(objTermLoanRB.getString("tblColumnSanction_Facility3"));
            sanctionFacilityTitle.add(objTermLoanRB.getString("tblColumnSanction_Facility4"));
            sanctionFacilityTitle.add(objTermLoanRB.getString("tblColumnSanction_Facility5"));
            sanctionFacilityTitle.add(objTermLoanRB.getString("tblColumnSanction_Facility5"));
        } catch (Exception e) {
            log.info("Exception in setSanctionFacilityTitle: " + e);
            parseException.logException(e, true);
        }
    }

    public String getTxtNoInstallments() {
        return txtNoInstallments;
    }

    public void setTxtNoInstallments(String txtNoInstallments) {
        this.txtNoInstallments = txtNoInstallments;
    }

    private void setShareTitle() throws Exception {
        try {
            shareTitle.add(objTermLoanRB.getString("tblcolumnShareCustName"));
            shareTitle.add(objTermLoanRB.getString("tblcolumnShareCustId"));
            shareTitle.add(objTermLoanRB.getString("tblcolumnShareLoanNo"));
            shareTitle.add(objTermLoanRB.getString("tblcolumnShareType"));
            shareTitle.add(objTermLoanRB.getString("tblcolumnShareShareno"));
            shareTitle.add(objTermLoanRB.getString("tblcolumnShareNoOfShare"));
            shareTitle.add(objTermLoanRB.getString("tblcolumnShareShareAmt"));
            //            shareTitle.add(objTermLoanRB.getString("tblcolumnShareStatus"));
        } catch (Exception e) {
            log.info("Exception in setShare Title:" + e);
            parseException.logException(e, true);
        }
    }

    private void setSanctionMainTitle() throws Exception {
        try {
            sanctionMainTitle.add(objTermLoanRB.getString("tblColumnSanction_Main1"));
            sanctionMainTitle.add(objTermLoanRB.getString("tblColumnSanction_Main2"));
            sanctionMainTitle.add(objTermLoanRB.getString("tblColumnSanction_Main3"));
            sanctionMainTitle.add(objTermLoanRB.getString("tblColumnSanction_Main4"));
            sanctionMainTitle.add(objTermLoanRB.getString("tblColumnSanction_Main5"));
            sanctionMainTitle.add(objTermLoanRB.getString("tblColumnSanction_Main6"));
            sanctionMainTitle.add(objTermLoanRB.getString("tblColumnSanction_Main7"));
        } catch (Exception e) {
            log.info("Exception in setSanctionMainTitle: " + e);
            parseException.logException(e, true);
        }
    }

    private void setSanctionMainTitleMds() throws Exception {
        try {
            sanctionMainTitleMds.add("Chittal No");
            sanctionMainTitleMds.add("Member No");
            sanctionMainTitleMds.add("Member Type");
            sanctionMainTitleMds.add("Member Name");
            sanctionMainTitleMds.add("Chit Amount Paid");
//            sanctionMainTitle.add(objTermLoanRB.getString("tblColumnSanction_Main5"));
            //    sanctionMainTitle.add(objTermLoanRB.getString("tblColumnSanction_Main6"));
            //   sanctionMainTitle.add(objTermLoanRB.getString("tblColumnSanction_Main7"));
        } catch (Exception e) {
            log.info("Exception in sanctionMainTitleMds: " + e);
            parseException.logException(e, true);
        }
    }

    private void setInstallmentTitle() throws Exception {
        try {
            installmentTitle = new ArrayList();
            installmentTitle.add(objTermLoanRB.getString("tblColumnInstallNo"));
            installmentTitle.add(objTermLoanRB.getString("tblColumnInstallDate"));
            installmentTitle.add(objTermLoanRB.getString("tblColumnInstallPrincipal"));
            installmentTitle.add(objTermLoanRB.getString("tblColumnInstallInterest"));
            installmentTitle.add(objTermLoanRB.getString("tblColumnInstallTotal"));
            installmentTitle.add(objTermLoanRB.getString("tblColumnInstallBalance"));
            //            installmentTitle.add("EMI Amount");
        } catch (Exception e) {
            log.info("Exception in setInstallmentTitle: " + e);
            parseException.logException(e, true);
        }
    }

    private void setInstallmentTitleForUniformType() throws Exception {
        try {
            installmentTitle = new ArrayList();
            installmentTitle.add(objTermLoanRB.getString("tblColumnInstallNo"));
            installmentTitle.add(objTermLoanRB.getString("tblColumnInstallDate"));
            installmentTitle.add(objTermLoanRB.getString("tblColumnInstallPrincipal"));
            installmentTitle.add(objTermLoanRB.getString("tblColumnInstallInterest"));
            installmentTitle.add(objTermLoanRB.getString("tblColumnInstallTotal"));
            installmentTitle.add("EMI Amount");
            installmentTitle.add(objTermLoanRB.getString("tblColumnInstallBalance"));
            //            tblInstallment = new EnhancedTableModel(null, installmentTitle);
        } catch (Exception e) {
            log.info("Exception in setInstallmentTitle: " + e);
            parseException.logException(e, true);
        }
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
        lookup_keys.add("ACT_OP_MODE");
        lookup_keys.add("SETTLEMENT_MODE");
        lookup_keys.add("LOAN.ACCOUNT_STATUS");
        lookup_keys.add("CATEGORY");
        lookup_keys.add("CONSTITUTION");
        lookup_keys.add("CORPORATE.ADDRESS_TYPE");
        lookup_keys.add("CUSTOMER.ADDRTYPE");
        lookup_keys.add("CUSTOMER.CITY");
        lookup_keys.add("CUSTOMER.STATE");
        lookup_keys.add("CUSTOMER.COUNTRY");
        lookup_keys.add("TERM_LOAN.BUSINESS_NATURE");
        lookup_keys.add("CUSTOMER.PRIMARYOCCUPATION");
        lookup_keys.add("TERM_LOAN.SANCTIONING_AUTHORITY");
        lookup_keys.add("TERM_LOAN.SANCTION_MODE");
        lookup_keys.add("FREQUENCY");
        lookup_keys.add("PRODUCTTYPE");
        lookup_keys.add("LOAN.FREQUENCY");
        lookup_keys.add("LOANPRODUCT.OPERATESLIKE");
        lookup_keys.add("TERMLOAN.INTERESTTYPE");
        lookup_keys.add("TERM_LOAN.SECURITY_CATEGORY");
        lookup_keys.add("TERM_LOAN.CHARGE_NATURE");
        lookup_keys.add("TERM_LOAN.MILLS_INDUSTRIAL_USERS");
        lookup_keys.add("TERM_LOAN.REPAYMENT_TYPE");
        lookup_keys.add("TERM_LOAN.REPAYMENT_FREQUENCY");
        lookup_keys.add("TERM_LOAN.COMMODITY_CODE");
        lookup_keys.add("TERM_LOAN.SECTOR_CODE");
        lookup_keys.add("TERM_LOAN.PURPOSE_CODE");
        lookup_keys.add("TERM_LOAN.INDUSTRY_CODE");
        lookup_keys.add("TERM_LOAN.RISK_NATURE");
        lookup_keys.add("TERM_LOAN.20_CODE");
        lookup_keys.add("TERM_LOAN.GOVT_SCHEME_CODE");
        lookup_keys.add("TERM_LOAN.GUARANTEE_COVER_CODE");
        lookup_keys.add("TERM_LOAN.HEALTH_CODE");
        lookup_keys.add("TERM_LOAN.DISTRICT_CODE");
        lookup_keys.add("TERM_LOAN.WEAKERSECTION_CODE");
        lookup_keys.add("TERM_LOAN.REFINANCING_INSTITUTION");
        lookup_keys.add("TERM_LOAN.ASSET_STATUS");
        lookup_keys.add("TERM_LOAN.FACILITY");
        lookup_keys.add("LOAN.INT_GET_FROM");
        lookup_keys.add("BOARDDIRECTORS");
        lookup_keys.add("TERM_LOAN.GUARANTEE_STATUS");
        lookup_keys.add("GOLD_CONFIGURATION");
        lookup_keys.add("PURPOSE_OF_LOAN");
        lookup_keys.add("RELATIONSHIP");

        lookUpHash.put(CommonConstants.PARAMFORQUERY, lookup_keys);

        keyValue = ClientUtil.populateLookupData(lookUpHash);

        getKeyValue((HashMap) keyValue.get("CATEGORY"));
        setCbmCategory(new ComboBoxModel(key, value));

        getKeyValue((HashMap) keyValue.get("CONSTITUTION"));
        setCbmConstitution(new ComboBoxModel(key, value));

        getKeyValue((HashMap) keyValue.get("LOAN.ACCOUNT_STATUS"));
        setCbmAcctStatus(new ComboBoxModel(key, value));


        getKeyValue((HashMap) keyValue.get("TERM_LOAN.SANCTIONING_AUTHORITY"));
        cbmSanctioningAuthority = new ComboBoxModel(key, value);

        getKeyValue((HashMap) keyValue.get("TERM_LOAN.SANCTION_MODE"));
        cbmModeSanction = new ComboBoxModel(key, value);




        HashMap param = new HashMap();
        HashMap whereMap = new HashMap();
        whereMap.put("behavesLike", "LOANS_AGAINST_DEPOSITS");
        List resultList = ClientUtil.executeQuery("TermLoan.getProdID_Behaves", whereMap);
        ArrayList keyList = new ArrayList();
        ArrayList valList = new ArrayList();
        keyList.add("");
        valList.add("");
        for (int i = resultList.size() - 1, j = 0; i >= 0; --i, ++j) {
            // If the result contains atleast one record
            HashMap retrieve = (HashMap) resultList.get(j);
            keyList.add(retrieve.get("PROD_ID"));
            valList.add(retrieve.get("PROD_DESC"));
        }
        cbmLoanProduct = new ComboBoxModel(keyList, valList);
    }

    public void getDepositProducts(String loanProductID) {
        try {
            lookUpHash = new HashMap();
            //Added By Suresh
            if (getProductAuthRemarks().equals("MDS_LOAN")) {
                lookUpHash.put(CommonConstants.MAP_NAME, "getMDSLoanProductSchemes");
            } else if (getProductAuthRemarks().equals("PADDY_LOAN")) {
                lookUpHash.put(CommonConstants.MAP_NAME, "getPaddyLoanProductID");
            } else if (getProductAuthRemarks().equals("OTHER_LOAN")) {
                lookUpHash.put(CommonConstants.MAP_NAME, DEP_GET_PROD_ID);
            }
            lookUpHash.put(CommonConstants.PARAMFORQUERY, loanProductID);
            keyValue = ClientUtil.populateLookupData(lookUpHash);
            getKeyValue((HashMap) keyValue.get(CommonConstants.DATA));
            cbmDepositProduct = new ComboBoxModel(key, value);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void fillData(HashMap keyValue) throws Exception {
        key = (ArrayList) keyValue.get("KEY");
        value = (ArrayList) keyValue.get("VALUE");
    }

    private void setOperationMap() throws Exception {
        operationMap = new HashMap();
        operationMap.put(CommonConstants.JNDI, "TermLoanJNDI");
        operationMap.put(CommonConstants.HOME, "termloan.TermLoanHome");
        operationMap.put(CommonConstants.REMOTE, "termloan.TermLoan");
    }

    private void setBlankKeyValue() {
        key = new ArrayList();
        key.add("");
        value = new ArrayList();
        value.add("");
    }

    public double getLienAmount(HashMap depositMap, boolean isdepositDeleted) {
        Rounding rd = new Rounding();
        System.out.println("depositMap11>>##@@>>"+depositMap);
        HashMap prodMap = new HashMap();
        String depositLienRoundOff = "";
        prodMap.put("prodId", getCbmLoanProduct().getKeyForSelected());
        List lst = ClientUtil.executeQuery("TermLoan.getProdHead", prodMap);
        if (lst.size() > 0) {
            prodMap = (HashMap) lst.get(0);
            //System.out.println("prodMap" + prodMap);
            depositLienRoundOff = CommonUtil.convertObjToStr(prodMap.get("DEPOSIT_ROUNDOFF"));
        }
        int PERIOD = CommonUtil.convertObjToInt(prodMap.get("DEP_AMT_MATURING_PERIOD"));
        Date currentDate = ClientUtil.getCurrentDate();
        Date cdate = DateUtil.addDays(currentDate, PERIOD);
        Date matdate = DateUtil.getDateMMDDYYYY(CommonUtil.convertObjToStr(getDep_mat_dt()));
        double eligibleMargin = 0.0;
        if (PERIOD > 0) {
            if (DateUtil.dateDiff(matdate, cdate) >= 0) {

                eligibleMargin = CommonUtil.convertObjToDouble(prodMap.get("DEP_AMT_LOAN_MATURING")).doubleValue();
            } else {
                eligibleMargin = CommonUtil.convertObjToDouble(prodMap.get("DEP_ELIGIBLE_LOAN_AMT")).doubleValue();
            }
        } else {
            eligibleMargin = CommonUtil.convertObjToDouble(prodMap.get("DEP_ELIGIBLE_LOAN_AMT")).doubleValue();
        }

//        double eligibleMargin=CommonUtil.convertObjToDouble(prodMap.get("DEP_ELIGIBLE_LOAN_AMT")).doubleValue();
        double availBal = 0;
        if (isdepositDeleted) {
            availBal = CommonUtil.convertObjToDouble(depositMap.get("DEPOSIT_AMT")).doubleValue();
            setDepositAvilableAmt(CommonUtil.convertObjToStr(depositMap.get("DEPOSIT_AMT")));

        } else {
            availBal = CommonUtil.convertObjToDouble(depositMap.get("DEPOSIT_AVAILABLE_BALANCE")).doubleValue();
            setDepositAvilableAmt(CommonUtil.convertObjToStr(depositMap.get("DEPOSIT_AVAILABLE_BALANCE")));
        }
        
        // Code to be added by nithya for RBI
        if (CommonUtil.convertObjToStr(CommonConstants.SAL_REC_MODULE).equals("Y")) {  
            System.out.println("Done for RBI");
            System.out.println("getDepoBehavesLike() :: " + getDepoBehavesLike());
            if(getDepoBehavesLike().equals("THRIFT")){
                availBal = CommonUtil.convertObjToDouble(depositMap.get("CLEAR_BALANCE")).doubleValue();
                if(availBal < 0){
                    availBal = -1 * availBal;
                }
            }            
        }
        // End
        
        availBal = (availBal * eligibleMargin / 100.0);  //85.0
        availBal = depositSanctionRoundOff(availBal, depositLienRoundOff);//roundOffDepositLien
        if (isdepositDeleted) {
            double loanAmt = availBal - Double.parseDouble(getTxtLoanAmt());
            if (loanAmt < 0) {
                loanAmt *= -1;
            }
            setTxtLoanAmt(String.valueOf(loanAmt));
            setLblEligibileAmtValue(String.valueOf(loanAmt));
        } else {
           if (actionType == ClientConstants.ACTIONTYPE_NEW) {
            if (getTxtLoanAmt() != null && getTxtLoanAmt().length() > 0) {
                double totloan = availBal + setLienFromTableData(eligibleMargin);
                System.out.println("availBal1111???>>>"+availBal);
                setTxtLoanAmt(String.valueOf(totloan));//Double.parseDouble(getTxtLoanAmt())));
                setEligibleAmt(totloan);
                setLblEligibileAmtValue(String.valueOf(totloan));
            } else {
                setTxtLoanAmt(String.valueOf(availBal));
                setLblEligibileAmtValue(String.valueOf(availBal));
                System.out.println("availBal???>>>"+availBal);
                setEligibleAmt(availBal);
            }
           }
        }
        return availBal;
    }

    /*
     *set TO for existing interst details in edit mode 
     **/
    public boolean setTermLoanInterestTO(ArrayList eachRecs, HashMap depositIntMap, boolean newDeposit) {
        try {

            existTermLoanInterestTO = (TermLoanInterestTO) eachRecs.get(0);

            if (getCommand() == INSERT || newDeposit) {
                if (tblSanctionMain != null && tblSanctionMain.getRowCount() > 0) {
                    if (getOldDepositIntRate() != 0 && getOldDepositIntRate() != (existTermLoanInterestTO.getInterest().doubleValue() + CommonUtil.convertObjToDouble(depositIntMap.get("DEPOSIT_INT")).doubleValue())) {
                        ClientUtil.displayAlert("Interest Rate Not Match Exist Deposit" + "\n" + "Please Select another Deposit");
                        return true;
                    }
                }
                setTxtInter(String.valueOf(existTermLoanInterestTO.getInterest().doubleValue() + CommonUtil.convertObjToDouble(depositIntMap.get("DEPOSIT_INT")).doubleValue()));
                setDepositInterest(CommonUtil.convertObjToDouble(depositIntMap.get("DEPOSIT_INT")).doubleValue());
                setOldDepositIntRate(existTermLoanInterestTO.getInterest().doubleValue() + CommonUtil.convertObjToDouble(depositIntMap.get("DEPOSIT_INT")).doubleValue());
            } else {
                setTxtInter(String.valueOf(existTermLoanInterestTO.getInterest().doubleValue()));
            }

        } catch (Exception e) {

            e.printStackTrace();
        }
        return false;
    }

    private long depositSanctionRoundOff(double limit, String depositLienRoundOff) {
        Rounding re = new Rounding();
        long roundOffValue = 0;
        //        CommonUtil.convertObjToLong(
        long longLimit = (long) limit;
        if (depositLienRoundOff.length() > 0) {
            String roundOff = depositLienRoundOff;
            if (roundOff.length() > 0) {
                if (roundOff.equals("NEAREST_TENS")) {
                    roundOffValue = 10;
                }
                if (roundOff.equals("NEAREST_HUNDREDS")) {
                    roundOffValue = 100;
                }
                if (roundOff.equals("NEAREST_VALUE")) {
                    roundOffValue = 1;
                }
            }
            Rounding rd = new Rounding();
            /* lien marked next higher but limit marked lower  */
            if (!roundOff.equals("NO_ROUND_OFF")) {
                longLimit = rd.lower(longLimit, roundOffValue);
            }
        }
        return longLimit;
    }

    private void getKeyValue(HashMap keyValue) throws Exception {
        key = (ArrayList) keyValue.get(CommonConstants.KEY);
        value = (ArrayList) keyValue.get(CommonConstants.VALUE);
    }

    /**
     * To retrieve the data from database to populate in UI based on Borrower
     * Number, Account Number
     *
     * @param whereMap is the HashMap having where conditions
     */
    public void populateData(HashMap whereMap) {

        log.info("In populateData..." + whereMap);
        HashMap mapData = null;
        try {
            whereMap.put("UI_PRODUCT_TYPE", "TL");
            whereMap.put(CommonConstants.MAP_WHERE, whereMap.get("LOAN_NO"));//BORROW_NO
            setStrACNumber(CommonUtil.convertObjToStr(whereMap.get("LOAN_NO")));
            if (whereMap.containsKey("BORROWER NO")) {
                whereMap.put("BORROW_NO", whereMap.get("BORROWER NO"));
            }
            whereMap.put("LOAN_TYPE", "LTD"); // Added By Suresh
            mapData = (HashMap) proxy.executeQuery(whereMap, operationMap);
            log.info("proxy.executeQuery is working fine");
            if (mapData.containsKey("PaddyTO") && mapData.get("PaddyTO") != null) {
                if (paddyMap == null) {
                    paddyMap = new HashMap();
                }
                paddyMap = (HashMap) mapData.get("PaddyTO");
            }
            System.out.println("mapData@@! MDSTO11@@>>>" + mapData.get("MDSTO"));
//            if (mapData.containsKey("MDSTO") && mapData.get("MDSTO")!=null) {
//                if(mdsMap == null){
//                    mdsMap = new HashMap();
//                }
//                mdsMap = (HashMap) mapData.get("MDSTO");
            System.out.println("mapData.get(MDSTO) in ob>>>>" + mapData.get("MDSTO"));
            if (mapData.get("MDSTO") != null && ((List) mapData.get("MDSTO")).size() > 0) {
                setMdsLoanTableData((List) (mapData.get("MDSTO")));
                setProductAuthRemarks("MDS_LOAN");
            }

//            }
            populateOB(mapData);

        } catch (Exception e) {
            log.info("Exception caught in populateData" + e);
            parseException.logException(e, true);
            e.printStackTrace();
        }
    }

    /**
     * To retrieve the data from database to populate in UI based on Borrower
     * Number, Account Number
     *
     * @param mapData is the HashMap having the values retrieved from database
     */
    private void populateOB(HashMap mapData) {
        log.info(" populateOB...");
        ArrayList sanctionList;
        ArrayList sanctionFacilityList;
        ArrayList facilityList;
        ArrayList caseList;
        try {
            System.out.println("populateOb###3" + mapData);
            // Populate the Tabs on the Basis of Borrower Number or Account Number
//        if ((mapData.get("KEY_VALUE").equals("BORROWER_NUMBER")) || (mapData.get("KEY_VALUE").equals("AUTHORIZE"))){
            // Populate the Tabs on the Basis of Borrower Number
            if (((List) mapData.get("TermLoanBorrowerTO")).size() > 0) {
                // Populate the Borrower Tab if the corresponding record is existing in Database
                setBorrowerNo(((TermLoanBorrowerTO) ((List) mapData.get("TermLoanBorrowerTO")).get(0)).getBorrowNo());
                setTermLoanBorrower((TermLoanBorrowerTO) ((List) mapData.get("TermLoanBorrowerTO")).get(0), true);
//                setStatusBy(termLoanBorrowerOB.getStatusBy());
//                //                setStatusBy(TrueTransactMain.USER_ID);
//                setAuthorizeStatus(termLoanBorrowerOB.getAuthorizeStatus());
            } else {
                return;
            }
            // To Populate the Main Customer
            HashMap queryMap = new HashMap();
//            queryMap.put("CUST_ID", termLoanBorrowerOB.getTxtCustID());
//            termLoanBorrowerOB.populateBorrowerTabCustomerDetails(queryMap, false,loanType);
//            if(termLoanBorrowerOB.getCboConstitution().equals("Joint Account")) {
//                termLoanBorrowerOB.setTermLoanJointAcctTO((ArrayList) (mapData.get("TermLoanJointAcctTO")));
//            }
            if (((List) mapData.get("TermLoanCompanyTO")).size() > 0) {
                // Populate the Company Tab if the corresponding record is existing in Database
                setTermLoanCompany((TermLoanCompanyTO) ((List) mapData.get("TermLoanCompanyTO")).get(0));
            }
            if (loanType.equals("OTHERS")) //                setShareTab(mapData);
            {
                queryMap = null;
            }
//        }
//        if ((mapData.get("KEY_VALUE").equals("ACCOUNT_NUMBER")) || (mapData.get("KEY_VALUE").equals("AUTHORIZE"))){
            // Populate the Tabs on the Basis of Account Number
            // To populate Facility details at the time of authorization
//            if (mapData.get("KEY_VALUE").equals("AUTHORIZE")){
            ArrayList sanctionAuthorizeList = (ArrayList) (mapData.get("TermLoanSanctionTO.AUTHORIZE"));
            setTermLoanSanctionTOForAuthorize(sanctionAuthorizeList);
            ArrayList sanctionFacilityAuthorizeList = (ArrayList) (mapData.get("TermLoanSanctionFacilityTO.AUTHORIZE"));
            setTermLoanSanctionFacilityTOForAuthorize(sanctionFacilityAuthorizeList);
            facilityList = (ArrayList) (mapData.get("TermLoanFacilityTO.AUTHORIZE"));
            // The return HashMap contains "sanctionNo" as key and
            // corresponding sanction number as value
            HashMap transactionMap = setTermLoanFacilityByAcctNo(facilityList);

            //SET DEPOSIT DETAILS IN UI

            setTermLoanDepositLienTO();

            // To get the sanction details and sanction limit

//                getPLR_Rate(getLblProductID_FD_Disp());
            transactionMap = null;
//                resultList = null;
            sanctionAuthorizeList = null;
            sanctionFacilityAuthorizeList = null;
            if (loanType.equals("OTHERS")) {
            }

            objSMSSubscriptionTO = null;
            if (isMobileBanking && mapData.containsKey("SMSSubscriptionTO") && ((List) mapData.get("SMSSubscriptionTO")).size() > 0) {
                objSMSSubscriptionTO = (SMSSubscriptionTO) ((List) mapData.get("SMSSubscriptionTO")).get(0);
                objSMSSubscriptionTO.setStatus(CommonConstants.STATUS_MODIFIED);
                setSMSSubscriptionTO(objSMSSubscriptionTO);
            }

            // Populate the Tabs on the Basis of Account Number
//            termLoanRepaymentOB.resetRepaymentSchedule();
//            termLoanRepaymentOB.getTblRepaymentTab().setDataArrayList(null, termLoanRepaymentOB.getRepaymentTabTitle());
            setTermLoanRepayment((ArrayList) (mapData.get("TermLoanRepaymentTO")));
            //populate settlement ob
            if (mapData.containsKey("SettlementTO") && mapData.get("SettlementTO") != null) {
//             settlementOB.setSettlemtnOB(mapData);//dontdelete
            }
            if (mapData.containsKey("ActTransTO") && mapData.get("ActTransTO") != null) {
                System.out.println("### setDetailsForLTD - depositCustDetMap 2 : " + depositCustDetMap);
            }
            if (loanType.equals("LTD")) {
                setLoanType(loanType);
                //                termLoanInterestOB.setDepositNo(CommonUtil.convertObjToStr(depositCustDetMap.get("DEPOSIT_NO")));
            }
            setTermLoanInterestTO((ArrayList) (mapData.get("TermLoanInterestTO")), null, false);
            if (((List) mapData.get("TermLoanClassificationTO")).size() > 0) {
                // Populate the Classification Tab if the corresponding record is existing in Database
                setTermLoanClassification((TermLoanClassificationTO) ((List) mapData.get("TermLoanClassificationTO")).get(0));
//                termLoanClassificationOB.setClassifiDetails(CommonConstants.TOSTATUS_UPDATE);
            }
//            else{
//                termLoanClassificationOB.setClassifiDetails(CommonConstants.TOSTATUS_INSERT);
//            }

//            if (((List) mapData.get("TermLoanOtherDetailsTO")).size() > 0){
//                // Populate the Other Details Tab if the corresponding record is existing in Database
//                termLoanOtherDetailsOB.setTermLoanOtherDetailsTO((TermLoanOtherDetailsTO) ((List) mapData.get("TermLoanOtherDetailsTO")).get(0));
//                termLoanOtherDetailsOB.setOtherDetailsMode(CommonConstants.TOSTATUS_UPDATE);
//            }else{
//                termLoanOtherDetailsOB.setOtherDetailsMode(CommonConstants.TOSTATUS_INSERT);
//            }

            // Populate the Tabs on the Basis of Account Number
//            termLoanDocumentDetailsOB.resetDocumentDetails();
//            termLoanDocumentDetailsOB.getTblDocumentTab().setDataArrayList(null, termLoanDocumentDetailsOB.getDocumentTabTitle());
//            termLoanDocumentDetailsOB.setTermLoanDocumentTO((ArrayList) (mapData.get("TermLoanDocumentTO")));
            if (mapData.get("getDepositLoanInEdit") != null && ((List) mapData.get("getDepositLoanInEdit")).size() > 0) {
                setDepositLoanTableData((List) (mapData.get("getDepositLoanInEdit")));
            }

            sanctionList = null;
            sanctionFacilityList = null;
            facilityList = null;
            setChanged();
            ttNotifyObservers();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void setSMSSubscriptionTO(SMSSubscriptionTO objSMSSubscriptionTO) {
        setTxtMobileNo(CommonUtil.convertObjToStr(objSMSSubscriptionTO.getMobileNo()));
        setTdtMobileSubscribedFrom(DateUtil.getStringDate(objSMSSubscriptionTO.getSubscriptionDt()));
    }

    private void setTermLoanDepositLienTO() {
        HashMap mapData = new HashMap();
        if (getProductAuthRemarks().equals("MDS_LOAN")) {
            String chitNo = "";
            if (getMdsMap() != null && getMdsMap().size() > 0) {
                Map hash = getMdsMap();
                setTxtDepositNo(CommonUtil.convertObjToStr(hash.get("CHITTAL_NO") + "_" + CommonUtil.convertObjToStr(hash.get("SUB_NO"))));
                setKeyValueForPaddyAndMDSLoans(CommonUtil.convertObjToStr(hash.get("CHITTAL_NO") + "_" + CommonUtil.convertObjToStr(hash.get("SUB_NO"))));
                chitNo = CommonUtil.convertObjToStr(hash.get("CHITTAL_NO"));
                mapData.put("CHITTAL_NO", chitNo);
                List lst = ClientUtil.executeQuery("getMDSLoanProdId", mapData);
                if (lst != null && lst.size() > 0) {
                    HashMap map = (HashMap) lst.get(0);
                    cbmDepositProduct.setKeyForSelected(CommonUtil.convertObjToStr(map.get("MDS_PROD_ID")));
                }
            }
        } else if (getProductAuthRemarks().equals("PADDY_LOAN")) {
            String paddyNo = "";
            if (getPaddyMap() != null && getPaddyMap().size() > 0) {
                Map hash = getPaddyMap();
                setTxtDepositNo(CommonUtil.convertObjToStr(hash.get("CND_NO")));
                setKeyValueForPaddyAndMDSLoans(CommonUtil.convertObjToStr(hash.get("CND_NO")));
                paddyNo = CommonUtil.convertObjToStr(hash.get("CND_NO"));
                mapData.put("CND_NUMBER", paddyNo);
                List lst = ClientUtil.executeQuery("getPaddyLoanProdId", mapData);
                if (lst != null && lst.size() > 0) {
                    HashMap map = (HashMap) lst.get(0);
                    cbmDepositProduct.setKeyForSelected(CommonUtil.convertObjToStr(map.get("PADDY_PROD_ID")));
                }
            }
        } else if (getProductAuthRemarks().equals("OTHER_LOAN")) {
            mapData.put("ACCT_NO", getStrACNumber());
            List lst = ClientUtil.executeQuery("getDepositAndLienDetails", mapData);
            if (lst != null && lst.size() > 0) {
                HashMap map = (HashMap) lst.get(0);
                setTxtDepositNo(CommonUtil.convertObjToStr(map.get("DEPOSIT_NO")));
                cbmDepositProduct.setKeyForSelected(CommonUtil.convertObjToStr(map.get("DEPOSIT_PRODID")));
                setLblShowAccountHeadId(CommonUtil.convertObjToStr(map.get("LOAN_ACHD")));
            }
        }
    }

    private void setDepositLoanTableData(List lst) {
        ArrayList singleList = new ArrayList();
        if (allTblDataList == null) {
            allTblDataList = new ArrayList();
        }
        HashMap singleMap = new HashMap();
        if (lst != null) {
            for (int i = 0; i < lst.size(); i++) {
                singleMap = (HashMap) lst.get(i);
                singleList = new ArrayList();
                singleList.add(singleMap.get("DEPOSIT_NO"));
                //singleList.add(singleMap.get("DEPOSIT_AMT"));
                singleList.add(singleMap.get("AVAILABLE_BALANCE"));//KD-3641 : DAY DEPOSIT LOAN
                singleList.add(singleMap.get("RATE_OF_INT"));
                singleList.add(singleMap.get("MATURITY_AMT"));
                singleList.add(singleMap.get("TOTAL_INT_CREDIT"));
                singleList.add(CommonUtil.convertObjToStr(singleMap.get("MATURITY_DT")));
                singleList.add("MODIFIED");
                allTblDataList.add(singleList);
            }
            tblSanctionMain.setDataArrayList(allTblDataList, sanctionMainTitle);
        }
    }

    private void setMdsLoanTableData(List lst) {
        ArrayList singleList = new ArrayList();
        if (allTblDataList == null) {
            allTblDataList = new ArrayList();
        }
        HashMap singleMap = new HashMap();
        if (lst != null) {
            for (int i = 0; i < lst.size(); i++) {
                singleMap = (HashMap) lst.get(i);
                singleList = new ArrayList();
                singleList.add(singleMap.get("CHITTAL_NO"));
                singleList.add(singleMap.get("MEMBER_NO"));
                singleList.add(singleMap.get("MEMBER_TYPE"));
                singleList.add(singleMap.get("MEMBER_NAME"));
                singleList.add(singleMap.get("AMOUNT"));
                //  singleList.add(CommonUtil.convertObjToStr(singleMap.get("MATURITY_DT")));
                // singleList.add("MODIFIED");
                allTblDataList.add(singleList);
            }
            tblSanctionMainMds.setDataArrayList(allTblDataList, sanctionMainTitleMds);
        }
    }
 public int getInstallNo(String strNoInstalls, int incType){
        int instNo = 0;
        try{
            Double insNo = CommonUtil.convertObjToDouble(strNoInstalls);
            instNo = insNo.intValue();// - 1;
            if (incType == 7){
                instNo = instNo * 7;
            }else if (incType >= 90){
                instNo = ((int) (incType / 30)) * instNo;
            }
            insNo = null;
        }catch(Exception e){
            log.info("Exception caught in getInstallAmt: "+e);
            parseException.logException(e,true);
        }
        return instNo;
    }
    public void deleteTableData(int row) {
        ArrayList totList = tblSanctionMain.getDataArrayList();
        HashMap depositMap = new HashMap();
        if (deleteTblDataList == null) {
            deleteTblDataList = new ArrayList();
        }
        if (totList != null) {
            ArrayList singleList = (ArrayList) totList.get(row);
//         singleList.add("DELETED");
            if (singleList != null && singleList.size() > 0) {
                depositMap.put("DEPOSIT_NO", singleList.get(0));
                depositMap.put("DEPOSIT_AMT", singleList.get(1));
                singleList.set(6, "DELETED");
                deleteTblDataList.add(singleList);
                totList.remove(row);
                tblSanctionMain.setDataArrayList(totList, sanctionMainTitle);
                getLienAmount(depositMap, true);
            }
        }

    }

    public void deleteTableMdsData(int row) {
        ArrayList totList = tblSanctionMainMds.getDataArrayList();
        HashMap depositMap = new HashMap();
        if (deleteTblDataList == null) {
            deleteTblDataList = new ArrayList();
        }
        if (totList != null) {
            ArrayList singleList = (ArrayList) totList.get(row);
//          singleList.add("DELETED");
            if (singleList != null && singleList.size() > 0) {
//              depositMap.put("DEPOSIT_NO",singleList.get(0));
//              depositMap.put("DEPOSIT_AMT",singleList.get(4));
                System.out.println("singleList.get(0) in ob>>" + singleList.get(0));
                String chittalNo2 = "";
                String subNo2 = "";
                String keyValue1 = CommonUtil.convertObjToStr(singleList.get(0));
                if (keyValue1.indexOf("_") != -1) {
                    chittalNo2 = keyValue1.substring(0, keyValue1.indexOf("_"));
                    subNo2 = keyValue1.substring(keyValue1.indexOf("_") + 1, keyValue1.length());
                }
                System.out.println("chittalNo2 in oBB>>>" + chittalNo2);
                System.out.println("singleList.get(4) in oBB>>>" + singleList.get(4));
                depositMap.put("DEPOSIT_NO", chittalNo2);
                depositMap.put("DEPOSIT_AMT", singleList.get(4));
                try {
                    System.out.println("getCommand() in ob??>>" + getCommand());
                    if (getCommand().equals(UPDATE)) {
                        chittalNo2 = CommonUtil.convertObjToStr(singleList.get(0));
                        System.out.println("chittalNo2>>>>???>>" + chittalNo2);
                        depositMap.put("DEPOSIT_NO", chittalNo2);
                        HashMap mdsdepositMap = new HashMap();
                        HashMap executeMap = new HashMap();
                        mdsdepositMap.put("CHITTAL_NO", chittalNo2);
                        //  executeMap.put(CommonConstants.MAP_WHERE,mdsdepositMap);
                        ClientUtil.execute("updateMDSApplDeletedLoanGivenStatus", mdsdepositMap);
                    }
                } catch (Exception e) {
                    log.info("Error In getCommand OB " + e);
                    parseException.logException(e, true);
                }
                singleList.set(3, "DELETED");
                singleList.set(0, chittalNo2);
                singleList.set(4, singleList.get(4));
                deleteTblDataList.add(singleList);
                totList.remove(row);
                tblSanctionMainMds.setDataArrayList(totList, sanctionMainTitleMds);
                getLienAmount(depositMap, true);
            }
        }

    }

    public double setLienFromTableData(double eligibleMargin) {
        ArrayList totList = null;
       if (getProductAuthRemarks().equals("MDS_LOAN")) {
          // System.out.println("innn))((");
          totList = tblSanctionMainMds.getDataArrayList();  
       }else{
          totList = tblSanctionMain.getDataArrayList();
       }
        ArrayList singleList = null;
        HashMap depositMap = new HashMap();
        double availBal = 0;
        HashMap transactionMap = new HashMap();
        transactionMap.put("prodId", getCbmLoanProduct().getKeyForSelected());
        transactionMap.put("PROD_ID", getCbmLoanProduct().getKeyForSelected());
        List resultList2 = ClientUtil.executeQuery("TermLoan.getProdHead", transactionMap);
        if (totList != null) {
            if (resultList2.size() > 0) {
                transactionMap = (HashMap) resultList2.get(0);
                int PERIOD = CommonUtil.convertObjToInt(transactionMap.get("DEP_AMT_MATURING_PERIOD"));
                Date currentDate = ClientUtil.getCurrentDate();
                Date cdate = DateUtil.addDays(currentDate, PERIOD);
                for (int i = 0; i < totList.size(); i++) {
                    singleList = (ArrayList) totList.get(i);
                    double depositAmt = 0.0;
                    if (getProductAuthRemarks().equals("MDS_LOAN")) {
                    depositAmt = CommonUtil.convertObjToDouble(singleList.get(4)).doubleValue();    
                    }else{
                    depositAmt = CommonUtil.convertObjToDouble(singleList.get(1)).doubleValue();
                    }
                    if (PERIOD > 0) {
                        Date matdat = DateUtil.getDateMMDDYYYY(CommonUtil.convertObjToStr(singleList.get(5)));
                        if (DateUtil.dateDiff(matdat, cdate) >= 0) {
                            eligibleMargin = CommonUtil.convertObjToDouble(transactionMap.get("DEP_AMT_LOAN_MATURING")).doubleValue();
                        } else {
                            eligibleMargin = CommonUtil.convertObjToDouble(transactionMap.get("DEP_ELIGIBLE_LOAN_AMT")).doubleValue();
                        }
                    } else {
                        eligibleMargin = CommonUtil.convertObjToDouble(transactionMap.get("DEP_ELIGIBLE_LOAN_AMT")).doubleValue();
                    }
                    availBal += (depositAmt * eligibleMargin / 100.0);
                }
            }
        }
        System.out.println("availBal@@##@1111###>>>>>"+availBal);
        return availBal;
    }

    private void setTermLoanSanctionTOForAuthorize(ArrayList objSanctionTOList) {
        try {
            TermLoanSanctionTO objTermLoanSanctionTO;
            objTermLoanSanctionTO = (TermLoanSanctionTO) objSanctionTOList.get(0);

            setTxtSanctionRef(objTermLoanSanctionTO.getSanctionNo());
            setTxtSanctionNo(CommonUtil.convertObjToStr(objTermLoanSanctionTO.getSlNo()));
//            setStrRealSanctionNo(getTxtSanctionNo());
            setTdtAccountOpenDate(DateUtil.getStringDate(objTermLoanSanctionTO.getSanctionDt()));


            setCboSanctioningAuthority(CommonUtil.convertObjToStr(getCbmSanctioningAuthority().getDataForKey(objTermLoanSanctionTO.getSanctionAuthority())));
            setTxtRemarks(objTermLoanSanctionTO.getRemarks());
            setCboModeSanction(CommonUtil.convertObjToStr(getCbmModeSanction().getDataForKey(objTermLoanSanctionTO.getSanctionMode())));
            objTermLoanSanctionTO = null;
        } catch (Exception e) {
            log.info("Exception caught in setTermLoanSanctionTOForAuthorize: " + e);
            parseException.logException(e, true);
        }
    }

    private void setTermLoanSanctionFacilityTOForAuthorize(ArrayList objSanctionFacilityTOList) {
        try {
            TermLoanSanctionFacilityTO objTermLoanSanctionFacilityTO;
            objTermLoanSanctionFacilityTO = (TermLoanSanctionFacilityTO) objSanctionFacilityTOList.get(0);
            setTxtSanctionNo(String.valueOf((objTermLoanSanctionFacilityTO.getSlNo().doubleValue())));
//            cbmRepayFreq.setKeyForSelected(CommonUtil.convertObjToStr(objTermLoanSanctionFacilityTO.getRepaymentFrequency()));


//            getCbmTypeOfFacility().setKeyForSelected(CommonUtil.convertObjToStr(objTermLoanSanctionFacilityTO.getFacilityType()));
//            setCboTypeOfFacility(CommonUtil.convertObjToStr(getCbmTypeOfFacility().getDataForKey(CommonUtil.convertObjToStr(objTermLoanSanctionFacilityTO.getFacilityType()))));
//            // To set the product ids based on Type of facility
//            setFacilityProductID(CommonUtil.convertObjToStr(objTermLoanSanctionFacilityTO.getFacilityType()));
            setLblEligibileAmtValue(CommonUtil.convertObjToStr(objTermLoanSanctionFacilityTO.getLimit()));
              setTxtLoanAmt(CommonUtil.convertObjToStr(objTermLoanSanctionFacilityTO.getLimit()));
            setTdtAccountOpenDate(DateUtil.getStringDate(objTermLoanSanctionFacilityTO.getFromDt()));
         //   setTdtRepaymentDt(DateUtil.getStringDate(objTermLoanSanctionFacilityTO.getToDt()));

            setTdtRepaymentDt(DateUtil.getStringDate(objTermLoanSanctionFacilityTO.getRepaymentDt()));
            setToDate(DateUtil.getStringDate(objTermLoanSanctionFacilityTO.getToDt()));
            setTxtNoInstallments(CommonUtil.convertObjToStr(objTermLoanSanctionFacilityTO.getNoInstall()));
//            setTxtFacility_Moratorium_Period(CommonUtil.convertObjToStr(objTermLoanSanctionFacilityTO.getNoMoratorium()));
//            termLoanInterestOB.setLblExpiryDate_2(getTdtTDate());
//            setTxtNoInstallments(CommonUtil.convertObjToStr(objTermLoanSanctionFacilityTO.getNoInstall()));
//            setCboRepayFreq(CommonUtil.convertObjToStr(getCbmRepayFreq().getDataForKey(CommonUtil.convertObjToStr(objTermLoanSanctionFacilityTO.getRepaymentFrequency()))));
//            getCbmRepayFreq().setKeyForSelected(CommonUtil.convertObjToStr(objTermLoanSanctionFacilityTO.getRepaymentFrequency()));
//            getCbmProductId().setKeyForSelected(CommonUtil.convertObjToStr(objTermLoanSanctionFacilityTO.getProductId()));
        } catch (Exception e) {
            log.info("Exception caught in setTermLoanSanctionFacilityTOForAuthorize: " + e);
            parseException.logException(e, true);
        }
    }

    private HashMap setTermLoanFacilityByAcctNo(ArrayList objFacilityTOList) {
        HashMap returnMap = new HashMap();
        try {
            TermLoanFacilityTO objTermLoanFacilityTO;
            HashMap facilityRecord;
            //            LinkedHashMap allLocalRecords = new LinkedHashMap();
            String sanctionNo = null;
            int serialNo = -1;
            // To retrieve the Facility Details from the Database
            if (objFacilityTOList.size() > 0) {
                objTermLoanFacilityTO = (TermLoanFacilityTO) objFacilityTOList.get(0);
                facilityRecord = new HashMap();
                facilityRecord.put(SANCTION_NO, objTermLoanFacilityTO.getSanctionNo());
                returnMap.put("sanctionNo", objTermLoanFacilityTO.getSanctionNo());
                facilityRecord.put(SLNO, objTermLoanFacilityTO.getSlNo());
                returnMap.put("slNo", objTermLoanFacilityTO.getSlNo());
                returnMap.put("borrowNo", objTermLoanFacilityTO.getBorrowNo());

                facilityRecord.put(PROD_ID, CommonUtil.convertObjToStr(objTermLoanFacilityTO.getProdId()));
                facilityRecord.put(SECURITY_TYPE, CommonUtil.convertObjToStr(objTermLoanFacilityTO.getSecurityType()));
                facilityRecord.put(SECURITY_DETAILS, CommonUtil.convertObjToStr(objTermLoanFacilityTO.getSecurityDetails()));
                facilityRecord.put(ACC_TYPE, CommonUtil.convertObjToStr(objTermLoanFacilityTO.getAccountType()));
                //                facilityRecord.put(INTEREST_NATURE, CommonUtil.convertObjToStr(objTermLoanFacilityTO.getInterestNature()));
                facilityRecord.put(INTEREST_TYPE, CommonUtil.convertObjToStr(objTermLoanFacilityTO.getInterestType()));
                facilityRecord.put(ACC_LIMIT, CommonUtil.convertObjToStr(objTermLoanFacilityTO.getAccountLimit()));
                facilityRecord.put(RISK_WEIGHT, CommonUtil.convertObjToStr(objTermLoanFacilityTO.getRiskWeight()));
                facilityRecord.put(NOTE_DATE, DateUtil.getStringDate(objTermLoanFacilityTO.getDemandPromDt()));
                facilityRecord.put(NOTE_EXP_DATE, DateUtil.getStringDate(objTermLoanFacilityTO.getDemandPromExpdt()));
                facilityRecord.put(MULTI_DISBURSE, CommonUtil.convertObjToStr(objTermLoanFacilityTO.getMultiDisburse()));
                //                facilityRecord.put(SUBSIDY, CommonUtil.convertObjToStr(objTermLoanFacilityTO.getSubsidy()));
                facilityRecord.put(AOD_DATE, DateUtil.getStringDate(objTermLoanFacilityTO.getAodDt()));
                facilityRecord.put(PURPOSE_DESC, objTermLoanFacilityTO.getPurposeDesc());
                facilityRecord.put(GROUP_DESC, objTermLoanFacilityTO.getGroupDesc());
                facilityRecord.put(ACCT_STATUS, objTermLoanFacilityTO.getAcctStatus());
                facilityRecord.put(ACCT_NAME, objTermLoanFacilityTO.getAcctName());
                facilityRecord.put(INTEREST, CommonUtil.convertObjToStr(objTermLoanFacilityTO.getInterest()));
                facilityRecord.put(CONTACT_PERSON, objTermLoanFacilityTO.getContactPerson());
                facilityRecord.put(CONTACT_PHONE, objTermLoanFacilityTO.getContactPhone());
                facilityRecord.put(INT_GET_FROM, objTermLoanFacilityTO.getIntGetFrom());
                facilityRecord.put(AUTHORIZE_BY_1, objTermLoanFacilityTO.getAuthorizeBy1());
                facilityRecord.put(AUTHORIZE_BY_2, objTermLoanFacilityTO.getAuthorizeBy2());
                facilityRecord.put(AUTHORIZE_DT_1, objTermLoanFacilityTO.getAuthorizeDt1());
                facilityRecord.put(AUTHORIZE_DT_2, objTermLoanFacilityTO.getAuthorizeDt2());
                facilityRecord.put(AUTHORIZE_STATUS_1, objTermLoanFacilityTO.getAuthorizeStatus1());
                facilityRecord.put(AUTHORIZE_STATUS_2, objTermLoanFacilityTO.getAuthorizeStatus2());
                facilityRecord.put(REMARKS, objTermLoanFacilityTO.getRemarks());
                facilityRecord.put(RECOMMANDED_BY, objTermLoanFacilityTO.getRecommendedBy());
                facilityRecord.put(ACCT_OPEN_DT, objTermLoanFacilityTO.getAccOpenDt());
                facilityRecord.put(ACCT_CLOSE_DT, objTermLoanFacilityTO.getAccCloseDt());
                facilityRecord.put(DOCDETAILS, objTermLoanFacilityTO.getDocDetails());
                facilityRecord.put(POFATTORNEY, objTermLoanFacilityTO.getPofAttorney());
                facilityRecord.put(AUTHSIGNATORY, objTermLoanFacilityTO.getAuthorizedSignatory());
                facilityRecord.put(DRAWING_POWER, objTermLoanFacilityTO.getDpYesNo());
                facilityRecord.put(ACCTTRANSFER, CommonUtil.convertObjToStr(objTermLoanFacilityTO.getAcctTransfer()));
                facilityRecord.put(AVAILABLE_BALANCE, objTermLoanFacilityTO.getAvailableBalance());
                facilityRecord.put(COMMAND, UPDATE);
                sanctionNo = objTermLoanFacilityTO.getSanctionNo();
                serialNo = CommonUtil.convertObjToInt(objTermLoanFacilityTO.getSlNo());
                setStrACNumber(objTermLoanFacilityTO.getAcctNum());
                if (CommonUtil.convertObjToStr(objTermLoanFacilityTO.getIsMobileBanking()).equals("Y")) {
                    setIsMobileBanking(true);
                } else {
                    setIsMobileBanking(false);
                }
                setTxtLoanAmt(CommonUtil.convertObjToStr(objTermLoanFacilityTO.getShadowDebit()));
                setStrACNumber(objTermLoanFacilityTO.getAcctNum());
                setLblAcctNo_Sanction_Disp(objTermLoanFacilityTO.getAcctNum());
                cbmLoanProduct.setKeyForSelected(CommonUtil.convertObjToStr(objTermLoanFacilityTO.getProdId()));

                if (loanType.equals("LTD") && getStrACNumber().length() > 0) {
                    HashMap accountMap = new HashMap();
                    accountMap.put("ACCT_NUM", getStrACNumber());

                    List lst = ClientUtil.executeQuery("getDepositbeforeAuthDetails", accountMap); //getDepositDetails
                    if (lst != null && lst.size() > 0) {
                        accountMap = (HashMap) lst.get(0);
                    }
                } else {
                    cbmIntGetFrom.setKeyForSelected(facilityRecord.get(INT_GET_FROM));
                }
                System.out.println("mmmmmuuultttttt"+objTermLoanFacilityTO.getMultiDisburse());
                if (CommonUtil.convertObjToStr(objTermLoanFacilityTO.getMultiDisburse()).equalsIgnoreCase(YES)) {
                    setRdoMultiDisburseAllow_Yes(true);
                } else {
                    setRdoMultiDisburseAllow_No(true);
                }
                if (CommonUtil.convertObjToStr(objTermLoanFacilityTO.getMultiDisburse()).equalsIgnoreCase(NO)) {
                    setRdoMultiDisburseAllow_Yes(false);
                } else {
                    setRdoMultiDisburseAllow_No(false);
                }
                facilityRecord = null;
                objTermLoanFacilityTO = null;
            }
        } catch (Exception e) {
            log.info("Exception caught in setTermLoanFacilityByAcctNo: " + e);
            parseException.logException(e, true);
        }
        return returnMap;
    }

    private void setTermLoanSanctionTO(ArrayList objSanctionTO, ArrayList objSanctionFacilityTO) {
        try {
        } catch (Exception e) {
            log.info("Exception caught in setTermLoanSanctionTO: " + e);
            parseException.logException(e, true);
        }
    }

    private void setMax_Del_San_Details_No(String borrowNo) {
        try {
            HashMap transactionMap = new HashMap();
            HashMap retrieve = new HashMap();
            transactionMap.put("borrowNo", borrowNo);
            List resultList = ClientUtil.executeQuery("getSelectTermLoanSanctionMaxSLNO", transactionMap);
            if (resultList.size() > 0) {
                retrieve = (HashMap) resultList.get(0);
                tableUtilSanction_Main.setStr_MAX_DEL_SL_NO(CommonUtil.convertObjToStr(retrieve.get("MAX_SL_NO")));
            }
            retrieve = null;
            transactionMap = null;
            resultList = null;
        } catch (Exception e) {
            log.info("Error In setMax_Del_PoA_No: " + e);
            parseException.logException(e, true);
        }
    }

    /**
     * Getter for property cbmConstitution.
     *
     * @return Value of property cbmConstitution.
     */
    public com.see.truetransact.clientutil.ComboBoxModel getCbmConstitution() {
        return cbmConstitution;
    }

    /**
     * Setter for property cbmConstitution.
     *
     * @param cbmConstitution New value of property cbmConstitution.
     */
    public void setCbmConstitution(com.see.truetransact.clientutil.ComboBoxModel cbmConstitution) {
        this.cbmConstitution = cbmConstitution;
    }

    /**
     * Getter for property cbmCategory.
     *
     * @return Value of property cbmCategory.
     */
    public com.see.truetransact.clientutil.ComboBoxModel getCbmCategory() {
        return cbmCategory;
    }

    /**
     * Setter for property cbmCategory.
     *
     * @param cbmCategory New value of property cbmCategory.
     */
    public void setCbmCategory(com.see.truetransact.clientutil.ComboBoxModel cbmCategory) {
        this.cbmCategory = cbmCategory;
    }

    /**
     * Getter for property cbmAcctStatus.
     *
     * @return Value of property cbmAcctStatus.
     */
    public com.see.truetransact.clientutil.ComboBoxModel getCbmAcctStatus() {
        return cbmAcctStatus;
    }

    /**
     * Setter for property cbmAcctStatus.
     *
     * @param cbmAcctStatus New value of property cbmAcctStatus.
     */
    public void setCbmAcctStatus(com.see.truetransact.clientutil.ComboBoxModel cbmAcctStatus) {
        this.cbmAcctStatus = cbmAcctStatus;
    }

    /**
     * Getter for property cbmSanctioningAuthority.
     *
     * @return Value of property cbmSanctioningAuthority.
     */
    public com.see.truetransact.clientutil.ComboBoxModel getCbmSanctioningAuthority() {
        return cbmSanctioningAuthority;
    }

    /**
     * Setter for property cbmSanctioningAuthority.
     *
     * @param cbmSanctioningAuthority New value of property
     * cbmSanctioningAuthority.
     */
    public void setCbmSanctioningAuthority(com.see.truetransact.clientutil.ComboBoxModel cbmSanctioningAuthority) {
        this.cbmSanctioningAuthority = cbmSanctioningAuthority;
    }

    /**
     * Getter for property cbmModeSanction.
     *
     * @return Value of property cbmModeSanction.
     */
    public com.see.truetransact.clientutil.ComboBoxModel getCbmModeSanction() {
        return cbmModeSanction;
    }

    /**
     * Setter for property cbmModeSanction.
     *
     * @param cbmModeSanction New value of property cbmModeSanction.
     */
    public void setCbmModeSanction(com.see.truetransact.clientutil.ComboBoxModel cbmModeSanction) {
        this.cbmModeSanction = cbmModeSanction;
    }

    public void resetForm() {
        resetAllFields();
        ttNotifyObservers();
    }

    public void resetRemainingFields(boolean isNotFinalSave) {
        setCboAccStatus("");
        if (!isNotFinalSave) {
            cboConstitution = "";
            cboCategory = "";
            cboSanctioningAuthority = "";
            cboModeSanction = "";


            cbmCategory.setKeyForSelected("");
            cbmConstitution.setKeyForSelected("");
            tdtAccountOpenDate = "";
            tdtRepaymentDt = "";
            txtSanctionRef = "";
            txtSanctionNo = "";
            txtSanctionSlNo = "";
            txtInter = "";
            setDepositInterest(0);
            lblAcctNo_Sanction_Disp = "";
            txtRemarks = "";
            setDepositAvilableAmt("");
            dep_mat_dt = "";
            dep_mat_amt = "";
            dep_credit_int = "";
            dep_rate_int = "";
            deposit_amt = "";
            membName = "";
            membNo = "";
            membType = "";
            chittAmt = "";
        }
        lblCustNameValue = "";
        lblCustomerIdValue = "";
        lblMemberIdValue = "";

        txtDepositNo = "";
        ttNotifyObservers();
    }

    public void resetAllFields() {
        setCboAccStatus("");
        cboConstitution = "";
        cboCategory = "";
        cboSanctioningAuthority = "";
        cboModeSanction = "";
        cbmLoanProduct.setKeyForSelected("");
        if (cbmDepositProduct != null) {
            cbmDepositProduct.setKeyForSelected("");
        }
        txtDepositNo = "";
        lblShowAccountHeadId = "";
        lblCustNameValue = "";
        lblCustomerIdValue = "";
        lblMemberIdValue = "";
        cbmCategory.setKeyForSelected("");
        cbmConstitution.setKeyForSelected("");
        tdtAccountOpenDate = "";
        tdtRepaymentDt = "";
        previousDepositRepaymentDt = "";
        txtSanctionRef = "";
        txtSanctionNo = "";
        txtSanctionSlNo = "";
        txtInter = "";
        setDepositInterest(0);
        lblAcctNo_Sanction_Disp = "";
        txtLoanAmt = "";
        txtRemarks = "";
        depositAvilableAmt = "";
        txtMobileNo = "";
        tdtMobileSubscribedFrom = "";
        isMobileBanking = false;
        productAuthRemarks = "";
        txtTotalShareAmount = "";
        txtTotalNoOfShare = "";
        lblEligibileAmtValue = "";
        txtNoInstallments="";
        serviceTax_Map = null;
        jointCustMap = null;
        setLoanDisbursalAmt("");
        ttNotifyObservers();
    }

    /**
     * It will call the notifyObservers()
     */
    public void ttNotifyObservers() {
        notifyObservers();
    }

    public boolean setDetailsForLTD(HashMap hash) {
        try {
            HashMap resultMap = new HashMap();
            HashMap resultCustMap = new HashMap();
            depositCustDetMap = new HashMap();
            depositCustDetMap.putAll(hash);
            List resultList;
            List recultCustid = null;
            if (getActionType() == ClientConstants.ACTIONTYPE_AUTHORIZE || getActionType() == ClientConstants.ACTIONTYPE_REJECT) {
                resultList = ClientUtil.executeQuery("getDepositLienDetailsForLTD", hash);
            } else {
                resultList = ClientUtil.executeQuery("getDepositDetailsForLTD", hash);
                recultCustid = ClientUtil.executeQuery("getDepositCustDetailsForLTD", hash);

            }
            if (recultCustid != null && recultCustid.size() > 0) {
                resultCustMap = (HashMap) recultCustid.get(0);

            }
            if (resultList.size() > 0) {
                resultMap = (HashMap) resultList.get(0);

//                System.out.println("ternLoanBorrower###"+termLoanBorrowerOB.getTxtCustID());
                //added by rishad mp 08/09/2015 for avoiding repayment date going to null 
                if(!getDepoBehavesLike().equals("THRIFT")){
                setTdtRepaymentDt(DateUtil.getStringDate((java.util.Date)resultMap.get("MATURITY_DT")));
                tdtRepaymentDate = DateUtil.getStringDate((java.util.Date) resultMap.get("MATURITY_DT"));}
                //end
                if (resultCustMap != null && CommonUtil.convertObjToStr(resultCustMap.get("CUST_ID")).equals(getLblCustomerIdValue())) {
                    return true;
                } else if (resultCustMap != null && resultCustMap.containsKey("LIEN_AC_NO")) {
                      if (CommonUtil.convertObjToStr(CommonConstants.SAL_REC_MODULE).equals("Y")){
                          return true;
                      }
                      else{
                    ClientUtil.showMessageWindow("Deposit Holder" + resultMap.get("DEPOSIT_NO") + "\n"
                            + "Already Disbursed This LoanHolder" + resultCustMap.get("LIEN_AC_NO"));
                    return false;}
                }
                //                else
                //                    return true;
                depositCustDetMap.putAll(resultMap);
                //                setTdtTDate(CommonUtil.convertObjToStr(resultMap.get("MATURITY_DT")));
                setCurrDepositRepaymentDt(DateUtil.getStringDate((java.util.Date) resultMap.get("MATURITY_DT")));
                Date firstDt = DateUtil.getDateMMDDYYYY(getTdtRepaymentDt());
           //     setTdtRepaymentDt(DateUtil.getStringDate((java.util.Date) resultMap.get("MATURITY_DT")));

                if (tblSanctionMain != null && tblSanctionMain.getRowCount() > 0) {
                    for (int i = 0; i < tblSanctionMain.getRowCount(); i++) {

                        Date secondDt = DateUtil.getDateMMDDYYYY(getTdtRepaymentDt());
                        //                if(getCurrDepositRepaymentDt() !=null && getCurrDepositRepaymentDt() .length()>0  && getPreviousDepositRepaymentDt() !=null && getPreviousDepositRepaymentDt().length()>0)
                        //                if(DateUtil.dateDiff(DateUtil.getDateMMDDYYYY(getCurrDepositRepaymentDt()),DateUtil.getDateMMDDYYYY(getPreviousDepositRepaymentDt()))!=0){
                        if (DateUtil.dateDiff(firstDt, secondDt) > 0) {
                            setTdtRepaymentDt(DateUtil.getStringDate(firstDt));
                        } else {
                            setTdtRepaymentDt(DateUtil.getStringDate(secondDt));
                        }
                    }
                }

                //                setTdtRepaymentDt(DateUtil.getStringDate((java.util.Date)resultMap.get("MATURITY_DT")));
               if(!getDepoBehavesLike().equals("THRIFT")){
                tdtRepaymentDate = DateUtil.getStringDate((java.util.Date) resultMap.get("MATURITY_DT"));
                setPreviousDepositRepaymentDt(DateUtil.getStringDate((java.util.Date) resultMap.get("MATURITY_DT")));}
                return true;
            }
            resultMap = null;
            resultList = null;

        } catch (Exception e) {
            log.info("Exception caught in setDetailsForLTD : " + e);
            parseException.logException(e, true);
        }
        return false;
    }

    /**
     * To display Account Head in Facility Details and its following Tabs
     */
    public void setFacilityAcctHead() {
        try {
            HashMap transactionMap = new HashMap();
            HashMap retrieve;
            transactionMap.put("prodId", getCbmLoanProduct().getKeyForSelected());
            transactionMap.put("PROD_ID", getCbmLoanProduct().getKeyForSelected());

//            List resultList1 = ClientUtil.executeQuery("getProdIntDetails", transactionMap);
            List resultList2 = ClientUtil.executeQuery("TermLoan.getProdHead", transactionMap);
            List resultList3 = ClientUtil.executeQuery("TermLoan.getProduct_Details", transactionMap);

//            if (resultList1.size() > 0){
//                // If Product Account Head exist in Database
//                retrieve = (HashMap) resultList1.get(0);
//                if (retrieve.containsKey(PLR_RATE_APPL)){
//                    if (retrieve.get(PLR_RATE_APPL).equals(YES)){
//                        setRdoNatureInterest_PLR(true);
//                        setRdoNatureInterest_NonPLR(false);
//                    }else if (retrieve.get(PLR_RATE_APPL).equals(NO)){
//                        setRdoNatureInterest_NonPLR(true);
//                        setRdoNatureInterest_PLR(false);
//                    }
//                }
//            }
            retrieve = null;

            if (resultList2.size() > 0) {
                // If Product Account Head exist in Database
                retrieve = (HashMap) resultList2.get(0);
                setLblShowAccountHeadId(CommonUtil.convertObjToStr(retrieve.get("AC_HEAD")));
//                setLblAccountHead_FD_Disp(CommonUtil.convertObjToStr(retrieve.get("AC_HEAD")));
                int PERIOD = CommonUtil.convertObjToInt(retrieve.get("DEP_AMT_MATURING_PERIOD"));
                Date currentDate = ClientUtil.getCurrentDate();
                Date cdate = DateUtil.addDays(currentDate, PERIOD);
                Date matdate = DateUtil.getDateMMDDYYYY(CommonUtil.convertObjToStr(getDep_mat_dt()));
                if (PERIOD > 0) {
                    if (DateUtil.dateDiff(matdate, cdate) >= 0) {
                        setEligibleMargin(CommonUtil.convertObjToDouble(retrieve.get("DEP_AMT_LOAN_MATURING")).doubleValue());
                    } else {
                        setEligibleMargin(CommonUtil.convertObjToDouble(retrieve.get("DEP_ELIGIBLE_LOAN_AMT")).doubleValue());
                    }
                } else {
                    setEligibleMargin(CommonUtil.convertObjToDouble(retrieve.get("DEP_ELIGIBLE_LOAN_AMT")).doubleValue());
                }
//                setEligibleMargin(CommonUtil.convertObjToDouble(retrieve.get("DEP_ELIGIBLE_LOAN_AMT")).doubleValue());
                setSanctionAmtRoundOff(CommonUtil.convertObjToStr(retrieve.get("DEPOSIT_ROUNDOFF")));
//                setProductCategory(CommonUtil.convertObjToStr(retrieve.get("AUTHORIZE_REMARK")));
            } else {
                setEligibleMargin(0.0);
                setSanctionAmtRoundOff("");
            }
            retrieve = null;
            if (resultList3.size() > 0) {
                retrieve = (HashMap) resultList3.get(0);
//                if (retrieve.containsKey(SUBSIDY)){
//                    if (retrieve.get(SUBSIDY).equals(YES)){
//                        setRdoSubsidy_Yes(true);
//                        setRdoSubsidy_No(false);
//                    }else if (retrieve.get(SUBSIDY).equals(NO)){
//                        setRdoSubsidy_No(true);
//                        setRdoSubsidy_Yes(false);
//                    }
//                }
                setMinLimitValue(CommonUtil.convertObjToDouble(retrieve.get("MIN_AMT_LOAN")));
                setMaxLimitValue(CommonUtil.convertObjToDouble(retrieve.get("MAX_AMT_LOAN")));
//                setMaxLoanPeriod(CommonUtil.convertObjToDouble(retrieve.get("MAX_PERIOD")));
//                setMinLoanPeriod(CommonUtil.convertObjToDouble(retrieve.get("MIN_PERIOD")));
//                termLoanRepaymentOB.setInterest_Rate(CommonUtil.convertObjToDouble(retrieve.get("APPL_INTEREST")).doubleValue());
            }
//            setAllTabProdID(CommonUtil.convertObjToStr(getCbmProductId().getKeyForSelected()));
//            setAllTabAccHead();
//            retrieve = null;
//            transactionMap = null;
//            resultList1 = null;
//            resultList2 = null;
//            resultList3 = null;
        } catch (Exception e) {
            log.info("Exception caught in setFacilityAcctHead: " + e);
            parseException.logException(e, true);
        }
    }

    /**
     * To check whether the Limit entered in UI is falling within the minimum
     * and maximum loan amount in Loan Product
     *
     * @param strEnteredValue is the loan amount value entered in the Sanction
     * Facility Details
     * @return true if the limit amount entered in UI fall within the minimum
     * and maximum value of Loan Product else false
     */
    public boolean checkLimitValue(String strEnteredValue) {
        boolean returnValue = false;
        try {
            Double limitValueUI = CommonUtil.convertObjToDouble(strEnteredValue);
            if ((getMaxLimitValue().doubleValue() >= limitValueUI.doubleValue()) && (getMinLimitValue().doubleValue() <= limitValueUI.doubleValue())) {
                returnValue = true;
            }
            limitValueUI = null;
        } catch (Exception e) {
            log.info("Exception caught in checkLimitValue:  " + e);
            parseException.logException(e, true);
        }
        return returnValue;
    }

    /**
     * To perform the appropriate operation
     *
     * @param saveMode is the integer value to know which save button pressed
     */
    public void doAction(int saveMode) {
        log.info("In doAction...");
        try {
            //If actionType such as NEW, EDIT, DELETE, then proceed
            if (actionType != ClientConstants.ACTIONTYPE_CANCEL) {
                //If actionType has got propervalue then doActionPerform, else throw error
                if (getCommand() != null || getAuthorizeMap() != null) {
                    log.info("before doActionPerform...");
                    saveAction();
                } else {
                    log.info("In doAction()-->getCommand() is null:");
                }
            } else {
                log.info("In doAction()-->actionType is null:");
            }
        } catch (Exception e) {
            System.out.println("e--------->" + e);
            parseException.logException(e, true);
            setResult(ClientConstants.ACTIONTYPE_FAILED);
            log.info("Error in doAction():" + e);
        }
    }

    //we need to set value to all related termloan "TO" 
    private void saveAction() throws Exception {


        HashMap objSantionDetailsTOMap = setTermLoanSanctionSingleRecord();//setTermLoanSanction()
        HashMap objTermLoanSanctionTOMap = (HashMap) objSantionDetailsTOMap.get(SANCTION);
        HashMap objTermLoanSanctionFacilityTOMap = (HashMap) objSantionDetailsTOMap.get(SANCTION_FACILITY);

        HashMap objTermLoanJointAcctTOMap = setTermLoanJointAcct();
        TermLoanBorrowerTO objTermLoanBorrowerTO = setTermLoanBorrower(null, false);
        TermLoanCompanyTO objTermLoanCompanyTO = setTermLoanCompany(null);
        HashMap objTermLoanAuthorizedSignatoryTOList = new HashMap();// termLoanAuthorizedSignatoryOB.setAuthorizedSignatory();
        HashMap objTermLoanAuthorizedSignatoryInstructionTOList = new HashMap();// termLoanAuthorizedSignatoryInstructionOB.setAuthorizedSignatoryInstruction();
        HashMap objTermLoanPowerAttorneyTOMap = new HashMap();

        insertFacilityDetails(String.valueOf(1), 1);

        HashMap objTermLoanFacilityTOMap = setTermLoanFacilitySingleRecord();// 


        HashMap objTermLoanSecurityTOMap = setTermLoanSecurity();
        HashMap objRepaymentInstallmentMap = null;
        if (getDepoBehavesLike().equalsIgnoreCase("THRIFT")) {
            objRepaymentInstallmentMap = termLoanRepaymentOB.setTermLoanRepayment(false);
        } else {
            objRepaymentInstallmentMap = setTermLoanRepayment(null);
        }
        HashMap objTermLoanRepaymentTOMap = (HashMap) objRepaymentInstallmentMap.get(REPAYMENT_DETAILS);
        HashMap objTermLoanInstallmentTOMap = (HashMap) objRepaymentInstallmentMap.get(INSTALLMENT_DETAILS);
        HashMap objTermLoanInstallMultIntMap = (HashMap) objRepaymentInstallmentMap.get(INSTALLMULTINT_DETAILS);
        HashMap objTermLoanGuarantorTOMap = new HashMap();
        HashMap objTermLoanDocumentTOMap = new HashMap();
        HashMap objTermLoanInterestTOMap = setTermLoanInterest();
        TermLoanClassificationTO objTermLoanClassificationTO = setTermLoanClassification(null);


        if (getCommand().equals(DELETE) && !canBorrowerDelete) {
            objTermLoanBorrowerTO.setCommand(UPDATE);
            objTermLoanCompanyTO.setCommand(UPDATE);
        } else {
            objTermLoanBorrowerTO.setCommand(getCommand());
            objTermLoanCompanyTO.setCommand(getCommand());
        }
        System.out.println("getCommand(): " + getCommand());
        if (getCommand().equals(INSERT)) {
            objTermLoanBorrowerTO.setStatus(CommonConstants.STATUS_CREATED);
            objTermLoanCompanyTO.setStatus(CommonConstants.STATUS_CREATED);
        } else if (getCommand().equals(UPDATE)) {
            objTermLoanBorrowerTO.setStatus(CommonConstants.STATUS_MODIFIED);
            objTermLoanCompanyTO.setStatus(CommonConstants.STATUS_MODIFIED);
        } else if (getCommand().equals(DELETE) && canBorrowerDelete) {
            objTermLoanBorrowerTO.setStatus(CommonConstants.STATUS_DELETED);
            objTermLoanCompanyTO.setStatus(CommonConstants.STATUS_DELETED);
        }
        objTermLoanClassificationTO.setCommand(getCommand());
//        objTermLoanOtherDetailsTO.setCommand(termLoanOtherDetailsOB.getOtherDetailsMode());
        if (getCommand().equals(CommonConstants.TOSTATUS_INSERT)) {
            objTermLoanClassificationTO.setStatus(CommonConstants.STATUS_CREATED);
        } else if (getCommand().equals(CommonConstants.TOSTATUS_UPDATE)) {
            objTermLoanClassificationTO.setStatus(CommonConstants.STATUS_MODIFIED);
        } else if (getCommand().equals(CommonConstants.TOSTATUS_DELETE)) {
            objTermLoanClassificationTO.setStatus(CommonConstants.STATUS_DELETED);
        }


        HashMap data = new HashMap();
        //data.put("TermLoanJointAcctTO", objTermLoanJointAcctTOMap);
        data.put("TermLoanJointAcctTO", getJointCustMap()); // Added by nithya on 23-09-2020
        data.put("TermLoanBorrowerTO", objTermLoanBorrowerTO);
        data.put("TermLoanCompanyTO", objTermLoanCompanyTO);
        // Set the Networth Value and As on date
//        data.put("NETWORTH_DETAILS", termLoanCompanyOB.getNetworthDetails());
        data.put("AuthorizedSignatoryTO", objTermLoanAuthorizedSignatoryTOList);
        data.put("AuthorizedSignatoryInstructionTO", objTermLoanAuthorizedSignatoryInstructionTOList);
        data.put("PowerAttorneyTO", objTermLoanPowerAttorneyTOMap);
        data.put("TermLoanSanctionTO", objTermLoanSanctionTOMap);
        data.put("TermLoanSanctionFacilityTO", objTermLoanSanctionFacilityTOMap);
        data.put("TermLoanFacilityTO", objTermLoanFacilityTOMap);
        data.put("TermLoanSecurityTO", objTermLoanSecurityTOMap);
        data.put("TermLoanRepaymentTO", objTermLoanRepaymentTOMap);
        data.put("TermLoanInstallmentTO", objTermLoanInstallmentTOMap);
        data.put("TermLoanInstallMultIntTO", objTermLoanInstallMultIntMap);
//        data.put("TermLoanGuarantorTO", objTermLoanGuarantorTOMap);
        data.put("TermLoanInterestTO", objTermLoanInterestTOMap);
//        data.put("TermLoanDocumentTO", objTermLoanDocumentTOMap);
        data.put("TermLoanClassificationTO", objTermLoanClassificationTO);
        if (objSMSSubscriptionTO != null) {
            data.put("SMSSubscriptionTO", objSMSSubscriptionTO);
        }

        if (getCommand().equals("UPDATE")) {
            data.put("EDIT_TRANS_PART", editModeTransDetail());
        }
//            if(termLoanClassificationOB.getListAssetStatus() !=null)
//                
//                data.put("NPAHISTORY",termLoanClassificationOB.getListAssetStatus());
//        }

//        String facilityType = CommonUtil.convertObjToStr(getCbmTypeOfFacility().getKeyForSelected());

        data.put("TermLoanOtherDetailsTO", null);


        if (getCommand().equals(CommonConstants.TOSTATUS_INSERT) || getCommand().equals(CommonConstants.TOSTATUS_UPDATE)) {
//            if (loanType.equals("LTD")) {
            if (depositCustDetMap == null) {
                depositCustDetMap = new HashMap();
            }
            if (getCommand().equals(CommonConstants.TOSTATUS_UPDATE)) {
                if (deleteTblDataList != null && deleteTblDataList.size() > 0) {

                    depositCustDetMap.put("DELETE_LIEN", getdeletedTableData(deleteTblDataList));
                }


                depositCustDetMap.put("UPDATE_DEPOSIT_LIEN", getTableData());

            }
            if (isMultipleDeposit != null && isMultipleDeposit.size() > 0) {
                depositCustDetMap.put("MULTIPLE_DEPOSIT", isMultipleDeposit);
            }
            data.put("LTD", depositCustDetMap);
            data.put("LOAN_TYPE", "LTD");

//            }
        }
        //Added By Suresh
        if (getProductAuthRemarks().equals("MDS_LOAN")) {
            data.put("PRODUCT_CATEGORY", "MDS_LOAN");
            data.put("KEYVALUE", getKeyValueForPaddyAndMDSLoans());
            data.put("LOAN_TYPE", "LTD");
        } else if (getProductAuthRemarks().equals("PADDY_LOAN")) {
            data.put("PRODUCT_CATEGORY", "PADDY_LOAN");
            data.put("KEYVALUE", getKeyValueForPaddyAndMDSLoans());
            data.put("LOAN_TYPE", "LTD");
        } else if (getProductAuthRemarks().equals("OTHER_LOAN")) {
            data.put("PRODUCT_CATEGORY", "OTHER_LOAN");
        }
        if (getAuthorizeMap() != null) {
            data.put(CommonConstants.AUTHORIZEMAP, getAuthorizeMap());
        }
        if (getNewTransactionMap() != null) {
            data.put("Transaction Details Data", getNewTransactionMap());
        }

        System.out.println("getChargelst>>>..." + getChargelst());
        if (getChargelst() != null && getChargelst().size() > 0) { //charge details
            data.put("Charge List Data", getChargelst());
        }

        data.put(CommonConstants.MODULE, getModule());
        data.put(CommonConstants.SCREEN, getScreen());
        data.put("UI_PRODUCT_TYPE", "TL");
        if (getChkDepositLien() != null) {
            data.put("CHK_UNLIEN", getChkDepositLien().toString());
        }
        if (getServiceTax_Map() != null && getServiceTax_Map().size() > 0) {
            data.put("serviceTaxDetails", getServiceTax_Map());
            data.put("serviceTaxDetailsTO", setServiceTaxDetails());
        }
        System.out.print("dataonly#######" + data);

        HashMap proxyResultMap = new HashMap();
        proxyResultMap = proxy.execute(data, operationMap);
        System.out.println("proxyresultmap#$$$$$" + proxyResultMap);
        setProxyReturnMap(proxyResultMap);
        if (proxyResultMap != null) {
//            if(proxyResultMap.containsKey("ACCTNO") && loanType.equals("OTHERS"))
            loanACNo = CommonUtil.convertObjToStr(proxyResultMap.get("ACCTNO"));
            if(loanACNo!=null && loanACNo.length()>0)
            ClientUtil.showMessageWindow("Loan  Account No. : " + proxyResultMap.get("ACCTNO"));
            setStrACNumber(loanACNo);
//                setActionType(actionType = ClientConstants.ACTIONTYPE_CANCEL);

            if (proxyResultMap.size() > 0) {
//                if (proxyResultMap.containsKey("LIENNO")) {
//                    String lienNo = CommonUtil.convertObjToStr(proxyResultMap.get("LIENNO"));
//                    loanACNo = CommonUtil.convertObjToStr(proxyResultMap.get("ACCTNO"));
//                    //                setStrACNumber(loanACNo);
//                    ClientUtil.showMessageWindow("Lien Marked for this Loan\nLien No : " + lienNo
//                    + "\nThe Lien will be opened for Edit...");
//                    HashMap lienMap = new HashMap();
//                    lienMap.put("CUSTOMER_NAME",depositCustDetMap.get("CUSTOMER"));
//                    lienMap.put("PRODID",depositCustDetMap.get("PRODUCT ID"));
//                    lienMap.put("DEPOSIT_ACT_NUM",depositCustDetMap.get("DEPOSIT_NO"));
//                    lienMap.put("SUBNO",depositCustDetMap.get("DEPOSIT_SUB_NO"));
//                    lienMap.put("CUST_ID",depositCustDetMap.get("CUST_ID"));
//                    DepositLienUI depLienUI = new DepositLienUI();
//                    depLienUI.setLoanLienDisable(true);
//                    depLienUI.setViewType(ClientConstants.ACTIONTYPE_EDIT);
//                    depLienUI.fillData(lienMap);
//                    com.see.truetransact.ui.TrueTransactMain.showScreen(depLienUI);
//                    setLienChanged(depLienUI.isLienChanged());
//                    if (isLienChanged()) {
//                        depLienUI.dispose();
//                        depLienUI = null;
//                    }
//                }
            }
        }
        log.info("doActionPerform is over...");
        setResult(actionType);
//        if (saveMode == 1){
//            actionType = ClientConstants.ACTIONTYPE_CANCEL;
//        }else if (saveMode == 2 || saveMode == 3 || saveMode == 4){
//            actionType = ClientConstants.ACTIONTYPE_EDIT;
//        }

        // If the actionType is not Failed then change the INSERT Command as UPDATE
        if (getResult() != 4) {
            Set keySet;
            Set sanctionKeySet;
            Object[] objKeySet;
            Object[] objSanctionKeySet;
            LinkedHashMap sanctionTabFacility;
            HashMap oneRecord;
            HashMap oneRec;
            // Joint Acct
//            termLoanBorrowerOB.changeStatusJointAcct();
//            // Authorized Signatory Tab
//            termLoanAuthorizedSignatoryOB.changeStatusAuthorizedSignatory(getResult());
//            // Authorized Signatory Instruction Tab
//            termLoanAuthorizedSignatoryInstructionOB.changeStatusAuthorizedSignatoryInstruction(getResult());
//            // Power of Attorney DetaiLOANS_SANCTION_DETAILSls
//            termLoanPoAOB.changeStatusPoA(getResult());
//            // Sanction Details
//            if (getResult() != 2){
//                //If the Main Save Button pressed
//                tableUtilSanction_Main.getRemovedValues().clear();
//                tableUtilSanction_Facility.getRemovedValues().clear();
//            }
//            keySet =  sanctionMainAll.keySet();
//            objKeySet = (Object[]) keySet.toArray();
            // To change the Insert command to Update after Save Buttone Pressed
            // Sanction Details
//            for (int i = keySet.size() - 1,j = 0;i >= 0;--i,++j){
//                oneRecord           = (HashMap) sanctionMainAll.get(objKeySet[j]);
//                sanctionTabFacility = (LinkedHashMap) oneRecord.get(SANCTION_FACILITY_ALL);
//                sanctionKeySet      = sanctionTabFacility.keySet();
//                objSanctionKeySet   = (Object[]) sanctionKeySet.toArray();
//                if (oneRecord.get(COMMAND).equals(INSERT)){
//                    // If the status is in Insert Mode then change the mode to Update
//                    oneRecord.put(COMMAND, UPDATE);
//                }
//                // To change the Insert command to Update after Save Buttone Pressed
//                // Sanction Facility Details
//                for (int k = sanctionKeySet.size() - 1, m = 0;k >= 0;--k,++m){
//                    oneRec = (HashMap) sanctionTabFacility.get(objSanctionKeySet[m]);
//                    if (oneRec.get(COMMAND).equals(INSERT)){
//                        // If the status is in Insert Mode then change the mode to Update
//                        oneRec.put(COMMAND, UPDATE);
//                        sanctionTabFacility.put(objSanctionKeySet[m], oneRec);
//                    }
//                }
//                oneRecord.put(SANCTION_FACILITY_ALL, sanctionTabFacility);
//                sanctionMainAll.put(objKeySet[j], oneRecord);
//                oneRecord = null;
//            }
            tableUtilSanction_Main.setAllValues(sanctionMainAll);

//            // Facility Details Tab
//            if (getResult() != 2){
//                //If the Main Save Button pressed
//                removedFaccilityTabValues.clear();
//            }
//            keySet =  facilityAll.keySet();
//            objKeySet = (Object[]) keySet.toArray();
//            // To change the Insert command to Update after Save Buttone Pressed
//            // Facility Details
//            for (int i = keySet.size() - 1,j = 0;i >= 0;--i,++j){
//                oneRecord = (HashMap) facilityAll.get(objKeySet[j]);
//                if (oneRecord.get(COMMAND).equals(INSERT)){
//                    // If the status is in Insert Mode then change the mode to Update
//                    oneRecord.put(COMMAND, UPDATE);
//                    facilityAll.put(objKeySet[j], oneRecord);
//                }
//                oneRecord = null;
//            }
            // Security Details
//            termLoanSecurityOB.changeStatusSecurity(getResult());
//            // Repayment Details
//            termLoanRepaymentOB.changeStatusRepay(getResult());
//            // Guarantor Details
//            termLoanGuarantorOB.changeStatusGuarantor(getResult());
//            // Document Details
//            termLoanDocumentDetailsOB.changeStatusDocument(getResult());
//            // Interest Details
//            termLoanInterestOB.changeStatusInterest(getResult());
//            termLoanClassificationOB.setClassifiDetails(UPDATE);
//            termLoanOtherDetailsOB.setOtherDetailsMode(UPDATE);
            keySet = null;
            sanctionKeySet = null;
            objKeySet = null;
            objSanctionKeySet = null;
            sanctionTabFacility = null;
            oneRec = null;
            objTermLoanJointAcctTOMap = null;
            objTermLoanBorrowerTO = null;
            objTermLoanCompanyTO = null;
            objTermLoanAuthorizedSignatoryTOList = null;
            objTermLoanAuthorizedSignatoryInstructionTOList = null;
            objTermLoanPowerAttorneyTOMap = null;
            objSantionDetailsTOMap = null;
            objTermLoanSanctionTOMap = null;
            objTermLoanSanctionFacilityTOMap = null;
            objTermLoanFacilityTOMap = null;
            objTermLoanSecurityTOMap = null;
            objRepaymentInstallmentMap = null;
            objTermLoanRepaymentTOMap = null;
            objTermLoanInstallmentTOMap = null;
            objTermLoanGuarantorTOMap = null;
            objTermLoanDocumentTOMap = null;
            objTermLoanInstallMultIntMap = null;
            objTermLoanInterestTOMap = null;
            objTermLoanClassificationTO = null;
//            objTermLoanOtherDetailsTO = null;
            objRepaymentInstallmentAllMap = null;
            data = null;
        }

    }

    public LinkedHashMap getdeletedTableData(ArrayList deletedData) {
        HashMap singleMap = null;
        ArrayList singleList = null;
        LinkedHashMap returnLinkedMap = new LinkedHashMap();
        for (int i = 0; i < deletedData.size(); i++) {
            singleMap = new HashMap();
            singleList = new ArrayList();
            singleList = (ArrayList) deletedData.get(i);
            System.out.println("singleList.get(0) in ob>>>" + singleList.get(0));
            System.out.println("singleList.get(1) in ob>>>" + singleList.get(1));
            singleMap.put("DEPOSIT_NO", singleList.get(0));
            singleMap.put("DEPOSIT_AMT", singleList.get(4));
            System.out.println("getProductAuthRemarks111>>>" + getProductAuthRemarks());
            if (getProductAuthRemarks().equals("MDS_LOAN")) {
                if(singleList.get(3).equals("DELETED")){
                singleMap.put("STATUS", singleList.get(3));
                deleteFlag = 1;
                }else{
                singleMap.put("STATUS", "MODIFIED");
            }
            } else {
                singleMap.put("STATUS", singleList.get(6));
            }


            returnLinkedMap.put(CommonUtil.convertObjToStr(singleList.get(0)), singleMap);
        }
        System.out.println("deletereturnLinkedMap#####" + returnLinkedMap);
        return returnLinkedMap;
    }

    // available balance is changed then we need to call this method
    private HashMap editModeTransDetail() {
        HashMap transMap = new HashMap();
        if (getResult() != ClientConstants.ACTIONTYPE_FAILED) {
            String displayStr = "";
            String oldBatchId = "";
            String newBatchId = "";
            String actNum = CommonUtil.convertObjToStr(getStrACNumber());
            transMap = new HashMap();
            transMap.put("LINK_BATCH_ID", getStrACNumber());
            transMap.put("CURR_DT", curDate);
            List lst = ClientUtil.executeQuery("getAuthBatchTxTransferTOs", transMap);
            if (lst != null && lst.size() > 0) {
                displayStr += "Transfer Transaction Details...\n";
                ArrayList transferTransList = new ArrayList();
                for (int i = 0; i < lst.size(); i++) {

                    TxTransferTO objTxTransferTO = (TxTransferTO) lst.get(i);


                    transMap.put("OLDAMOUNT", objTxTransferTO.getAmount());
                    transferTransList.add(objTxTransferTO);
                    objTxTransferTO.setAmount(CommonUtil.convertObjToDouble(getTxtLoanAmt()));

                }
                transMap.put("TxTransferTO", transferTransList);
            }

            transMap = new HashMap();

            transMap.put("LINK_BATCH_ID", getStrACNumber());
            transMap.put("TRANS_DT", curDate);
            lst = ClientUtil.executeQuery("getCashTransactionTOForAuthorzation", transMap);
            if (lst != null && lst.size() > 0) {

                for (int i = 0; i < lst.size(); i++) {
                    CashTransactionTO objCashTransactionTO = (CashTransactionTO) lst.get(i);
                    objCashTransactionTO.setCommand("UPDATE");
                    transMap.put("OLDAMOUNT", objCashTransactionTO.getAmount());
                    objCashTransactionTO.setAmount(CommonUtil.convertObjToDouble(getTxtLoanAmt()));
                    transMap.put("CashTransactionTO", objCashTransactionTO);

                }
            }

        }
        return transMap;
    }

    public LinkedHashMap getTableData() {
        HashMap singleMap = null;
        ArrayList singleList = null;
        ArrayList tblList = tblSanctionMain.getDataArrayList();
        LinkedHashMap returnLinkedMap = new LinkedHashMap();
        for (int i = 0; i < tblList.size(); i++) {
            singleMap = new HashMap();
            singleList = new ArrayList();
            singleList = (ArrayList) tblList.get(i);
            singleMap.put("DEPOSIT_NO", singleList.get(0));
            singleMap.put("DEPOSIT_AMT", singleList.get(1));
            if (getProductAuthRemarks().equals("MDS_LOAN")) {
                singleMap.put("DEPOSIT_AMT", singleList.get(4));
                if (deleteFlag == 1) {
                    singleMap.put("STATUS", "DELETED");
                } else {
                    singleMap.put("STATUS", "MODIFIED");
                }
            } else {
                singleMap.put("STATUS", singleList.get(6));
            }


            returnLinkedMap.put(CommonUtil.convertObjToStr(singleList.get(0)), singleMap);
        }
        System.out.println("returnLinkedMap#####" + returnLinkedMap);
        return returnLinkedMap;
    }
    //more than one deposit able to give for single loan  kerla co operative

    public void checkMultipleDeposit() {
        if (getProductAuthRemarks().equals("MDS_LOAN")) {
            ArrayList totTableList = (ArrayList) tblSanctionMainMds.getDataArrayList();
            ArrayList singleList = null;
            HashMap singleMap = new HashMap();
            isMultipleDeposit = new LinkedHashMap();
            for (int i = 0; i < totTableList.size(); i++) {
                singleList = (ArrayList) totTableList.get(i);
                singleMap = new HashMap();
                singleMap.put("DEPOSIT_NO", singleList.get(0));
                singleMap.put("DEPOSIT_AMT", singleList.get(4));
                isMultipleDeposit.put(String.valueOf(i), singleMap);
            }
        } else {
            ArrayList totTableList = (ArrayList) tblSanctionMain.getDataArrayList();
            ArrayList singleList = null;
            HashMap singleMap = new HashMap();
            isMultipleDeposit = new LinkedHashMap();
            for (int i = 0; i < totTableList.size(); i++) {
                singleList = (ArrayList) totTableList.get(i);
                singleMap = new HashMap();
                singleMap.put("DEPOSIT_NO", singleList.get(0));
                singleMap.put("DEPOSIT_AMT", singleList.get(1));
                isMultipleDeposit.put(String.valueOf(i), singleMap);
            }
        }
    }

    public void setTableData(boolean isNotFinalSave) {
        if (allTblDataList == null) {
            allTblDataList = new ArrayList();//totDepositList
        }
        ArrayList singleDepositList = new ArrayList();


        singleDepositList.add(txtDepositNo);
        singleDepositList.add(deposit_amt);//deposit amt
        singleDepositList.add(new Double(getDepositInterest()));
        singleDepositList.add(dep_mat_amt);//maturity amt
        singleDepositList.add(dep_credit_int);//total int credit amt
//        singleDepositList.add(tdtRepaymentDt);
        singleDepositList.add(tdtRepaymentDate);
        singleDepositList.add("CREATED");

        if (txtDepositNo.length() > 0) {
            if (allTblDataList != null && allTblDataList.size() > 0) {
                allTblDataList.add(allTblDataList.size(), singleDepositList);
            } else {
                allTblDataList.add(singleDepositList);
            }

            tblSanctionMain = new EnhancedTableModel(allTblDataList, sanctionMainTitle);
            setAllScreenData();
            if (isNotFinalSave) {
                resetRemainingFields(isNotFinalSave);
                ttNotifyObservers();
            }
        }
    }

    public void setTableData1(boolean isNotFinalSave) {
        if (allTblDataList == null) {
            allTblDataList = new ArrayList();//totDepositList
        }
        ArrayList singleDepositList = new ArrayList();

        System.out.println("membNo@@!!>>>" + membNo);
        System.out.println("membType@@!!>>>" + membType);
        System.out.println("membName@@!!>>>" + membName);
        System.out.println("chittAmt@@!!>>>" + chittAmt);
        System.out.println("chittalNoMds@@!!>>>" + chittalNoMds);
        singleDepositList.add(chittalNoMds);
        singleDepositList.add(membNo);
        singleDepositList.add(membType);//deposit amt
        singleDepositList.add(membName);
        singleDepositList.add(chittAmt);//maturity amt
        //  singleDepositList.add(dep_credit_int);//total int credit amt
//        singleDepositList.add(tdtRepaymentDt);
        // singleDepositList.add(tdtRepaymentDate);      
        // singleDepositList.add("CREATED");

        if (txtDepositNo.length() > 0) {
            if (allTblDataList != null && allTblDataList.size() > 0) {
                allTblDataList.add(allTblDataList.size(), singleDepositList);
            } else {
                allTblDataList.add(singleDepositList);
            }

            tblSanctionMainMds = new EnhancedTableModel(allTblDataList, sanctionMainTitleMds);
            setAllScreenData();
            if (isNotFinalSave) {
                resetRemainingFields(isNotFinalSave);
                ttNotifyObservers();
            }
        }
    }

    public void setAllScreenData() {
        if (singleScreenMap == null) {
            singleScreenMap = new HashMap();
        }

        singleScreenMap.put("LOANS_PRODUCT", CommonUtil.convertObjToStr(cbmLoanProduct.getKeyForSelected()));

        singleScreenMap.put("DEPOSIT_PRODUCT", CommonUtil.convertObjToStr(cbmDepositProduct.getKeyForSelected()));
        singleScreenMap.put("CONSTITUTION", CommonUtil.convertObjToStr(cbmConstitution.getKeyForSelected()));
        singleScreenMap.put("CATEGORY", CommonUtil.convertObjToStr(cbmCategory.getKeyForSelected()));
        singleScreenMap.put("SANCTION_AUTHORITY", CommonUtil.convertObjToStr(cbmSanctioningAuthority.getKeyForSelected()));
        singleScreenMap.put("DEPOSIT_NO", txtDepositNo);
        singleScreenMap.put("ACCT_OPEN_DT", tdtAccountOpenDate);
        singleScreenMap.put("ACCT_HODER_NAME", lblCustNameValue);
        singleScreenMap.put("CUSTOMER_ID", lblCustomerIdValue);

        singleScreenMap.put("LOAN_AMT", txtLoanAmt);
        singleScreenMap.put("REPAYMENT_DT", tdtRepaymentDt);
        singleScreenMap.put("LOAN_INT", txtInter);
        singleScreenMap.put("SANCTION_NO", String.valueOf("1"));
        singleScreenMap.put("SANCTION_SLNO", txtSanctionRef);
        singleScreenMap.put("REMARKS", txtRemarks);
        if (getDepoBehavesLike().equalsIgnoreCase("THRIFT")) {
            singleScreenMap.put("To_DATE", toDate);
            singleScreenMap.put("NO_INSTALL", txtNoInstallments);
        }
    }

    private String getCommand() throws Exception {
        log.info("getCommand");
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
                command = AUTHORIZE;
                break;
            case ClientConstants.ACTIONTYPE_EXCEPTION:
                command = EXCEPTION;
                break;
            case ClientConstants.ACTIONTYPE_REJECT:
                command = REJECT;
                break;
            default:
        }

        return command;
    }

    public TermLoanClassificationTO setTermLoanClassification(TermLoanClassificationTO objTermLoanClassificationTO) {
        TermLoanClassificationTO termLoanClassificationTO = new TermLoanClassificationTO();
        try {

            if (objTermLoanClassificationTO != null && objTermLoanClassificationTO.getAcctNum().length() > 0 && getCommand() == UPDATE) {

                termLoanClassificationTO = objTermLoanClassificationTO;
                System.out.println("termLoanClassificationTO   update###" + termLoanClassificationTO);
            } else {
//            termLoanClassificationTO.setAcctNum();
                //            termLoanClassificationTO.setBorrowNo(getLblBorrowerNo_2());
                termLoanClassificationTO.setCommand(getCommand());
                //            termLoanClassificationTO.setNpaDt(DateUtil.getDateMMDDYYYY(CommonUtil.convertObjToStr(getTdtNPADate())));
//            Date dtDate = DateUtil.getDateMMDDYYYY(CommonUtil.convertObjToStr(getTdtNPADate()));
//            if(dtDate != null){
//                Date Dt = (Date)curDate.clone();
//                Dt.setDate(dtDate.getDate());
//                Dt.setMonth(dtDate.getMonth());
//                Dt.setYear(dtDate.getYear());
//                termLoanClassificationTO.setNpaDt(Dt);
//            }else{
////                termLoanClassificationTO.setNpaDt(DateUtil.getDateMMDDYYYY(CommonUtil.convertObjToStr(getTdtNPADate())));
//            }
                termLoanClassificationTO.setCommodityCode("");
                termLoanClassificationTO.setAssetStatus("STANDARD_ASSETS");
                termLoanClassificationTO.setBusinessSectorCode(CommonUtil.convertObjToStr(""));

//            if (getChkDirectFinance()){
                termLoanClassificationTO.setDirectFinance(YES);
//            }else{
//                termLoanClassificationTO.setDirectFinance(NO);
//            }

                termLoanClassificationTO.setDistrictCode("");

//            if (getChkDocumentcomplete()){
                termLoanClassificationTO.setDocumentComplete(YES);
//            }else{
//                termLoanClassificationTO.setDocumentComplete(NO);
//            }

//            if (getChkECGC()){
//                termLoanClassificationTO.setEcgc(YES);
//            }else{
                termLoanClassificationTO.setEcgc(NO);
//            }



                termLoanClassificationTO.setProdId(CommonUtil.convertObjToStr(singleScreenMap.get("LOANS_PRODUCT")));
                termLoanClassificationTO.setPurposeCode("");
                termLoanClassificationTO.setQis(YES);
                termLoanClassificationTO.setStatusBy(TrueTransactMain.USER_ID);
                termLoanClassificationTO.setStatusDt(curDate);
            }
        } catch (Exception e) {
            log.info("Error In setTermLoanClassificationTO() " + e);
            parseException.logException(e, true);
        }
        return termLoanClassificationTO;
    }

    public HashMap setTermLoanSecurity() {
        HashMap securityMap = new HashMap();
        try {
            TermLoanSecurityTO objTermLoanSecurityTO;
//            java.util.Set keySet =  securityAll.keySet();
//            Object[] objKeySet = (Object[]) keySet.toArray();
//            HashMap oneRecord;

            // To set the values for Security Transfer Object
//            for (int i = keySet.size() - 1,j = 0;i >= 0;--i,++j){
//                oneRecord = (HashMap) securityAll.get(objKeySet[j]);
            objTermLoanSecurityTO = new TermLoanSecurityTO();
            objTermLoanSecurityTO.setAcctNum("");
//                objTermLoanSecurityTO.setBorrowNo(getBorrowerNo());
            objTermLoanSecurityTO.setCustId(CommonUtil.convertObjToStr(singleScreenMap.get("CUSTOMER_ID")));
            objTermLoanSecurityTO.setCommand(getCommand());
//                objTermLoanSecurityTO.setFromDt((Date)oneRecord.get(FROM_DATE));
//                objTermLoanSecurityTO.setFromDt(DateUtil.getDateMMDDYYYY(CommonUtil.convertObjToStr(oneRecord.get(FROM_DATE))));
//                objTermLoanSecurityTO.setSecurityNo(CommonUtil.convertObjToDouble(oneRecord.get(SECURITY_NO)));
//                objTermLoanSecurityTO.setSlno(CommonUtil.convertObjToDouble(oneRecord.get(SLNO)));
//                objTermLoanSecurityTO.setSecurityAmt(CommonUtil.convertObjToDouble(oneRecord.get(SECURITY_VALUE)));
//                objTermLoanSecurityTO.setStatus(CommonUtil.convertObjToStr(oneRecord.get(COMMAND)));
////                objTermLoanSecurityTO.setToDt((Date)oneRecord.get(TO_DATE));
//                objTermLoanSecurityTO.setToDt(DateUtil.getDateMMDDYYYY(CommonUtil.convertObjToStr(oneRecord.get(TO_DATE))));
//                objTermLoanSecurityTO.setMargin(CommonUtil.convertObjToDouble(oneRecord.get(MARGIN)));
//                objTermLoanSecurityTO.setEligibleLoanAmt(CommonUtil.convertObjToDouble(oneRecord.get(ELIGIBLE_LOAN)));
//                if (oneRecord.get(COMMAND).equals(INSERT)){
//                    objTermLoanSecurityTO.setStatus(CommonConstants.STATUS_CREATED);
//                }else if (oneRecord.get(COMMAND).equals(UPDATE)){
//                    objTermLoanSecurityTO.setStatus(CommonConstants.STATUS_MODIFIED);
//                }
            objTermLoanSecurityTO.setStatusBy(TrueTransactMain.USER_ID);
            objTermLoanSecurityTO.setStatusDt(curDate);
//                securityMap.put(String.valueOf(j+1), objTermLoanSecurityTO);
//                oneRecord = null;
            objTermLoanSecurityTO = null;
//            }

            // To set the values for Security Transfer Object
            // where as the existing records in Database are deleted in client side
            // useful for updating the status
//            for (int i = tableUtilSecurity.getRemovedValues().size() - 1,j = 0;i >= 0;--i,++j){
//                oneRecord = (HashMap) tableUtilSecurity.getRemovedValues().get(j);
//                objTermLoanSecurityTO = new TermLoanSecurityTO();
//                objTermLoanSecurityTO.setAcctNum(CommonUtil.convertObjToStr(oneRecord.get(ACCOUNT_NO)));
////                objTermLoanSecurityTO.setBorrowNo(getBorrowerNo());
//                objTermLoanSecurityTO.setCustId(CommonUtil.convertObjToStr(oneRecord.get(CUSTOMER_ID)));
//                objTermLoanSecurityTO.setCommand(CommonUtil.convertObjToStr(oneRecord.get(COMMAND)));
////                objTermLoanSecurityTO.setFromDt((Date)oneRecord.get(FROM_DATE));
//                objTermLoanSecurityTO.setFromDt(DateUtil.getDateMMDDYYYY(CommonUtil.convertObjToStr(oneRecord.get(FROM_DATE))));
//                objTermLoanSecurityTO.setSecurityNo(CommonUtil.convertObjToDouble(oneRecord.get(SECURITY_NO)));
//                objTermLoanSecurityTO.setSlno(CommonUtil.convertObjToDouble(oneRecord.get(SLNO)));
//                objTermLoanSecurityTO.setSecurityAmt(CommonUtil.convertObjToDouble(oneRecord.get(SECURITY_VALUE)));
//                objTermLoanSecurityTO.setStatus(CommonUtil.convertObjToStr(oneRecord.get(COMMAND)));
////                objTermLoanSecurityTO.setToDt((Date)oneRecord.get(TO_DATE));
//                objTermLoanSecurityTO.setToDt(DateUtil.getDateMMDDYYYY(CommonUtil.convertObjToStr(oneRecord.get(TO_DATE))));
//                objTermLoanSecurityTO.setMargin(CommonUtil.convertObjToDouble(oneRecord.get(MARGIN)));
//                objTermLoanSecurityTO.setEligibleLoanAmt(CommonUtil.convertObjToDouble(oneRecord.get(ELIGIBLE_LOAN)));
//                objTermLoanSecurityTO.setStatus(CommonConstants.STATUS_DELETED);
//                objTermLoanSecurityTO.setStatusBy(TrueTransactMain.USER_ID);
//                objTermLoanSecurityTO.setStatusDt(curDate);
//                securityMap.put(String.valueOf(securityMap.size()+1), objTermLoanSecurityTO);
//                oneRecord = null;
//                objTermLoanSecurityTO = null;
//            }

//            securityMap.put("OLD_ELIGIBLE_LOAN_AMT", oldEligibleLoanAmtMap);
        } catch (Exception e) {
            log.info("Error In setTermLoanSecurity()..." + e);
            parseException.logException(e, true);
        }
        return securityMap;
    }

    private HashMap setTermLoanFacilitySingleRecord() {
        HashMap facilityMap = new HashMap();
        try {
            TermLoanFacilityTO objTermLoanFacilityTO;
            Set keySet = facilityAll.keySet();
            Object[] objKeySet = (Object[]) keySet.toArray();
            HashMap oneRecord;
            // To set the values for Facility Transfer Object

            objTermLoanFacilityTO = new TermLoanFacilityTO();
            objTermLoanFacilityTO.setBorrowNo(borrowerNo);
            objTermLoanFacilityTO.setSanctionNo(CommonUtil.convertObjToStr(facilityRecord.get(SANCTION_NO)));
            objTermLoanFacilityTO.setSlNo(CommonUtil.convertObjToDouble(facilityRecord.get(SLNO)));
            objTermLoanFacilityTO.setProdId(CommonUtil.convertObjToStr(facilityRecord.get(PROD_ID)));
            objTermLoanFacilityTO.setAcctStatus(CommonUtil.convertObjToStr(facilityRecord.get(ACCT_STATUS)));
            objTermLoanFacilityTO.setAcctNum(CommonUtil.convertObjToStr(facilityRecord.get(ACCT_NUM)));
            objTermLoanFacilityTO.setAcctName(CommonUtil.convertObjToStr(facilityRecord.get(ACCT_NAME)));
            objTermLoanFacilityTO.setIntGetFrom(CommonUtil.convertObjToStr(facilityRecord.get(INT_GET_FROM)));
            objTermLoanFacilityTO.setSecurityType(CommonUtil.convertObjToStr(facilityRecord.get(SECURITY_TYPE)));
            objTermLoanFacilityTO.setSecurityDetails(CommonUtil.convertObjToStr(facilityRecord.get(SECURITY_DETAILS)));
            objTermLoanFacilityTO.setAccountType(CommonUtil.convertObjToStr(facilityRecord.get(ACC_TYPE)));
            //                objTermLoanFacilityTO.setInterestNature(CommonUtil.convertObjToStr( oneRecord.get(INTEREST_NATURE)));
            objTermLoanFacilityTO.setInterestType(CommonUtil.convertObjToStr(facilityRecord.get(INTEREST_TYPE)));
            objTermLoanFacilityTO.setAccountLimit(CommonUtil.convertObjToStr(facilityRecord.get(ACC_LIMIT)));
            objTermLoanFacilityTO.setRiskWeight(CommonUtil.convertObjToStr(facilityRecord.get(RISK_WEIGHT)));

            //                objTermLoanFacilityTO.setDemandPromDt((Date)oneRecord.get(NOTE_DATE));
            objTermLoanFacilityTO.setDemandPromDt(getProperFormatDate(facilityRecord.get(NOTE_DATE)));

            //                objTermLoanFacilityTO.setDemandPromExpdt((Date)oneRecord.get(NOTE_EXP_DATE));
            objTermLoanFacilityTO.setDemandPromExpdt(getProperFormatDate(facilityRecord.get(NOTE_EXP_DATE)));
            objTermLoanFacilityTO.setMultiDisburse(CommonUtil.convertObjToStr(facilityRecord.get(MULTI_DISBURSE)));

            //                objTermLoanFacilityTO.setAodDt((Date)oneRecord.get(AOD_DATE));
            objTermLoanFacilityTO.setAodDt(getProperFormatDate(facilityRecord.get(AOD_DATE)));
            objTermLoanFacilityTO.setPurposeDesc(CommonUtil.convertObjToStr(facilityRecord.get(PURPOSE_DESC)));
            objTermLoanFacilityTO.setGroupDesc(CommonUtil.convertObjToStr(facilityRecord.get(GROUP_DESC)));
            objTermLoanFacilityTO.setInterest(CommonUtil.convertObjToStr(facilityRecord.get(INTEREST)));
            objTermLoanFacilityTO.setDpYesNo(CommonUtil.convertObjToStr(facilityRecord.get(DRAWING_POWER)));
            objTermLoanFacilityTO.setContactPerson(CommonUtil.convertObjToStr(facilityRecord.get(CONTACT_PERSON)));
            objTermLoanFacilityTO.setContactPhone(CommonUtil.convertObjToStr(facilityRecord.get(CONTACT_PHONE)));
            objTermLoanFacilityTO.setRemarks(CommonUtil.convertObjToStr(facilityRecord.get(REMARKS)));
            objTermLoanFacilityTO.setCommand(CommonUtil.convertObjToStr(getCommand()));
            objTermLoanFacilityTO.setAuthorizedSignatory(CommonUtil.convertObjToStr(facilityRecord.get(AUTHSIGNATORY)));
            objTermLoanFacilityTO.setPofAttorney(CommonUtil.convertObjToStr(facilityRecord.get(POFATTORNEY)));
            objTermLoanFacilityTO.setDocDetails(CommonUtil.convertObjToStr(facilityRecord.get(DOCDETAILS)));
            objTermLoanFacilityTO.setAcctTransfer(CommonUtil.convertObjToStr(facilityRecord.get(ACCTTRANSFER)));


            //                objTermLoanFacilityTO.setAccOpenDt((Date)oneRecord.get(ACCT_OPEN_DT));
            objTermLoanFacilityTO.setAccOpenDt(getProperFormatDate(facilityRecord.get(ACCT_OPEN_DT)));
            if (facilityRecord.containsKey(ACCT_CLOSE_DT) && facilityRecord.get(ACCT_CLOSE_DT) != null) {
                objTermLoanFacilityTO.setAccCloseDt(getProperFormatDate(facilityRecord.get(ACCT_CLOSE_DT)));
            }
            objTermLoanFacilityTO.setRecommendedBy(CommonUtil.convertObjToStr(facilityRecord.get(RECOMMANDED_BY)));
            if (getCommand().equals(INSERT)) {
                objTermLoanFacilityTO.setCreateDt(curDate);
                objTermLoanFacilityTO.setCreatedBy(TrueTransactMain.USER_ID);
                objTermLoanFacilityTO.setStatus(CommonConstants.STATUS_CREATED);
                objTermLoanFacilityTO.setAvailableBalance(CommonUtil.convertObjToDouble(facilityRecord.get(AVAILABLE_BALANCE)));
                objTermLoanFacilityTO.setClearBalance(CommonUtil.convertObjToDouble(facilityRecord.get(CLEAR_BALANCE)));
                objTermLoanFacilityTO.setUnclearBalance(CommonUtil.convertObjToDouble(facilityRecord.get(UNCLEAR_BALANCE)));
                objTermLoanFacilityTO.setShadowDebit(CommonUtil.convertObjToDouble(facilityRecord.get(SHADOW_DEBIT)));
                objTermLoanFacilityTO.setShadowCredit(CommonUtil.convertObjToDouble(facilityRecord.get(SHADOW_CREDIT)));
                objTermLoanFacilityTO.setTotalBalance(CommonUtil.convertObjToDouble(facilityRecord.get(TOTAL_AVAILABLE_BALANCE)));
                objTermLoanFacilityTO.setLoanBalancePrincipal(new Double(0.0));
                objTermLoanFacilityTO.setLoanPaidInt(new Double(0.0));
                objTermLoanFacilityTO.setLoanPaidPenalint(new Double(0.0));
                objTermLoanFacilityTO.setExcessAmt(new Double(0.0));
                objTermLoanFacilityTO.setLastTransDt(curDate);
            } else if (getCommand().equals(UPDATE)) {
                objTermLoanFacilityTO.setStatus(CommonConstants.STATUS_MODIFIED);
                //                    if(loanType.equals("OTHERS"))
                objTermLoanFacilityTO.setAvailableBalance(CommonUtil.convertObjToDouble(facilityRecord.get(AVAILABLE_BALANCE))); //FOR UPDATE AVAILABLE AMT
            } else if (getCommand().equals(DELETE)) {
                objTermLoanFacilityTO.setStatus(CommonConstants.STATUS_DELETED);
            }
//                if (getCommand().equals(CommonConstants.TOSTATUS_DELETE))
//                    if (objTermLoanFacilityTO.getSanctionNo().equals(getTxtSanctionSlNo()) &&
//                    objTermLoanFacilityTO.getSlNo().doubleValue() == CommonUtil.convertObjToDouble(getSanctionSlNo()).doubleValue()) {
//                        objTermLoanFacilityTO.setStatus(CommonConstants.STATUS_DELETED);
//                    }

            objTermLoanFacilityTO.setAuthorizeBy1(CommonUtil.convertObjToStr(facilityRecord.get(AUTHORIZE_BY_1)));
            objTermLoanFacilityTO.setAuthorizeBy2(CommonUtil.convertObjToStr(facilityRecord.get(AUTHORIZE_BY_2)));

            //                objTermLoanFacilityTO.setAuthorizeDt1((Date)oneRecord.get(AUTHORIZE_DT_1));
            objTermLoanFacilityTO.setAuthorizeDt1(getProperFormatDate(facilityRecord.get(AUTHORIZE_DT_1)));

            //                objTermLoanFacilityTO.setAuthorizeDt2((Date)oneRecord.get(AUTHORIZE_DT_2));
            objTermLoanFacilityTO.setAuthorizeDt2(getProperFormatDate(facilityRecord.get(AUTHORIZE_DT_2)));
            objTermLoanFacilityTO.setAuthorizeStatus1(CommonUtil.convertObjToStr(facilityRecord.get(AUTHORIZE_STATUS_1)));
            objTermLoanFacilityTO.setAuthorizeStatus2(CommonUtil.convertObjToStr(facilityRecord.get(AUTHORIZE_STATUS_2)));
            objTermLoanFacilityTO.setStatusBy(TrueTransactMain.USER_ID);
            objTermLoanFacilityTO.setStatusDt(curDate);
            objTermLoanFacilityTO.setBranchId(getSelectedBranchID());
            if (isMobileBanking) {
                objTermLoanFacilityTO.setIsMobileBanking("Y");
            } else {
                objTermLoanFacilityTO.setIsMobileBanking("N");
            }
            if (isMobileBanking) {
                if (objSMSSubscriptionTO == null) {
                    objSMSSubscriptionTO = new SMSSubscriptionTO();
                }
                objSMSSubscriptionTO.setProdType("TL");
                objSMSSubscriptionTO.setProdId(objTermLoanFacilityTO.getProdId());
                objSMSSubscriptionTO.setActNum(objTermLoanFacilityTO.getAcctNum());

                objSMSSubscriptionTO.setMobileNo(CommonUtil.convertObjToStr(getTxtMobileNo()));
                Date smsSubscriptionDt = DateUtil.getDateMMDDYYYY(getTdtMobileSubscribedFrom());
                if (smsSubscriptionDt != null) {
                    Date smsDt = (Date) curDate.clone();
                    smsDt.setDate(smsSubscriptionDt.getDate());
                    smsDt.setMonth(smsSubscriptionDt.getMonth());
                    smsDt.setYear(smsSubscriptionDt.getYear());
                    objSMSSubscriptionTO.setSubscriptionDt(smsDt);
                } else {
                    objSMSSubscriptionTO.setSubscriptionDt(DateUtil.getDateMMDDYYYY(getTdtMobileSubscribedFrom()));
                }
                if (!CommonUtil.convertObjToStr(objSMSSubscriptionTO.getStatus()).equals(CommonConstants.STATUS_MODIFIED)) {
                    objSMSSubscriptionTO.setStatus(CommonConstants.STATUS_CREATED);
                }
                objSMSSubscriptionTO.setStatusBy(ProxyParameters.USER_ID);
                objSMSSubscriptionTO.setStatusDt(curDate);
            } else {

                if (objSMSSubscriptionTO != null) {
                    objSMSSubscriptionTO.setStatus(CommonConstants.STATUS_DELETED);
                } else {
                    objSMSSubscriptionTO = null;
                }
            }

            if (objTermLoanFacilityTO.getAcctNum().equals(getStrACNumber())) {
                facilityMap.put(String.valueOf(objTermLoanFacilityTO.getSlNo()), objTermLoanFacilityTO);
                slNoForSanction = objTermLoanFacilityTO.getSlNo().doubleValue();
            }

            objTermLoanFacilityTO = null;
            if (getCommand().equals(INSERT)) {
                TermLoanExtenFacilityDetailsTO termLoanExtenFacilityDetailsTO = new TermLoanExtenFacilityDetailsTO();

//                      agriSubSidyOB.setAgriSubSidyTo();//dontdelete
            }
            oneRecord = null;

        } catch (Exception e) {
            e.printStackTrace();
            log.info("Exception caught In setTermLoanFacility: " + e);
            parseException.logException(e, true);
        }
        return facilityMap;
    }

    private void insertFacilityDetails(String strSanctionNo, int slno) {
        facilityRecord = new HashMap();
        facilityRecord.put(PROD_ID, singleScreenMap.get("LOANS_PRODUCT"));
        facilityRecord.put(ACCT_STATUS, "NEW");
        facilityRecord.put(SANCTION_NO, strSanctionNo);
        facilityRecord.put(SLNO, String.valueOf(slno));
        if (getDepoBehavesLike().equalsIgnoreCase("THRIFT")) {
            facilityRecord.put(INTEREST_TYPE, "FLOATING_RATE");
        } else {
            facilityRecord.put(INTEREST_TYPE, "FIXED_RATE");
        }
        facilityRecord.put(NOTE_DATE, null);
        facilityRecord.put(NOTE_EXP_DATE, null);
        facilityRecord.put(AOD_DATE, null);
        facilityRecord.put(PURPOSE_DESC, null);
        facilityRecord.put(GROUP_DESC, null);
        facilityRecord.put(CONTACT_PERSON, singleScreenMap.get("ACCT_HODER_NAME"));
        facilityRecord.put(CONTACT_PHONE, null);
        facilityRecord.put(REMARKS, singleScreenMap.get("REMARKS"));
        facilityRecord.put(ACCT_NAME, null);
          if (getDepoBehavesLike().equalsIgnoreCase("THRIFT")) {
              facilityRecord.put(INT_GET_FROM, "PROD");
        } else {
             facilityRecord.put(INT_GET_FROM, "ACT");
        }
        facilityRecord.put(COMMAND, INSERT);
        facilityRecord.put(AUTHORIZED, NO);
        facilityRecord.put(AUTHORIZE_BY_1, null);
        facilityRecord.put(AUTHORIZE_BY_2, null);
        facilityRecord.put(AUTHORIZE_DT_1, null);
        facilityRecord.put(AUTHORIZE_DT_2, null);
        facilityRecord.put(AUTHORIZE_STATUS_1, null);
        facilityRecord.put(AUTHORIZE_STATUS_2, null);
        facilityRecord.put(ACCT_OPEN_DT, getProperFormatDate(singleScreenMap.get("ACCT_OPEN_DT")));
        facilityRecord.put(RECOMMANDED_BY, null);


        facilityRecord.put(ACCT_NUM, getStrACNumber());;
        facilityRecord.put(SECURITY_DETAILS, FULLY_SECURED);
        facilityRecord.put(DRAWING_POWER, " ");
        facilityRecord.put(SECURITY_TYPE, "");
        facilityRecord.put(ACCTTRANSFER, "");
        facilityRecord.put(ACC_TYPE, NEW);
        facilityRecord.put(ACC_LIMIT, MAIN);
        facilityRecord.put(RISK_WEIGHT, " ");
       // facilityRecord.put(MULTI_DISBURSE, NO);
        facilityRecord.put(INTEREST, SIMPLE);
        facilityRecord.put(AUTHSIGNATORY, " ");
        facilityRecord.put(POFATTORNEY, " ");
        facilityRecord.put(DOCDETAILS, " ");
        
         if (isRdoMultiDisburseAllow_Yes()== true){
            facilityRecord.put(MULTI_DISBURSE, YES);
        }else if (isRdoMultiDisburseAllow_No() == true){
            facilityRecord.put(MULTI_DISBURSE, NO);
        }else{
            facilityRecord.put(MULTI_DISBURSE, " ");
        }

        if (getStrACNumber() != "_" && getStrACNumber().length() > 0) {
            // If the key already exist in the Linked Hash Map then it status
            // will be changed to UPDATE

//                    if(getClearBalance()<0)
//                        facilityRecord.put(AVAILABLE_BALANCE, String.valueOf(getAvailableBalance()));
//                    else if (isUpdateAvailableBalance())
            //facilityRecord.put(AVAILABLE_BALANCE, singleScreenMap.get("LOAN_AMT"));
            facilityRecord.put(AVAILABLE_BALANCE, getLoanDisbursalAmt());


        } else {
            // At the time of creating new account set the available balance, total available balance
            // shadow debit, shadow credit, clear balance, unclear balance
            facilityRecord.put(SHADOW_DEBIT, "0");
            facilityRecord.put(SHADOW_CREDIT, "0");
            //facilityRecord.put(AVAILABLE_BALANCE, singleScreenMap.get("LOAN_AMT"));
            facilityRecord.put(AVAILABLE_BALANCE, getLoanDisbursalAmt());
            facilityRecord.put(TOTAL_AVAILABLE_BALANCE, "0");
            facilityRecord.put(CLEAR_BALANCE, "0");
            facilityRecord.put(UNCLEAR_BALANCE, "0");
        }
    }

    public TermLoanCompanyTO setTermLoanCompany(TermLoanCompanyTO existTermLoanCompanyTO) {
        log.info("In setTermLoanCompany...");

        final TermLoanCompanyTO objTermLoanCompanyTO = new TermLoanCompanyTO();
        try {
            objTermLoanCompanyTO.setCoRegNo(null);
//            objTermLoanCompanyTO.setBorrowNo("");

//            Date EstDt = DateUtil.getDateMMDDYYYY(tdtDateEstablishment);
//            if(EstDt != null){
//            Date estDate = (Date)curDate.clone();
//            estDate.setDate(EstDt.getDate());
//            estDate.setMonth(EstDt.getMonth());
//            estDate.setYear(EstDt.getYear());
//            objTermLoanCompanyTO.setEstablishDt(estDate);
//            }else{
            objTermLoanCompanyTO.setEstablishDt(null);
//            }
//            objTermLoanCompanyTO.setEstablishDt(DateUtil.getDateMMDDYYYY(tdtDateEstablishment));

//            Date DelDt = DateUtil.getDateMMDDYYYY(tdtDealingWithBankSince);
//            if(DelDt != null){
//            Date delDate = (Date)curDate.clone();
//            delDate.setDate(DelDt.getDate());
//            delDate.setMonth(DelDt.getMonth());
//            delDate.setYear(DelDt.getYear());
//            objTermLoanCompanyTO.setDealBankSince(delDate);
//            }else{
            objTermLoanCompanyTO.setDealBankSince(null);
//            }
//            objTermLoanCompanyTO.setDealBankSince(DateUtil.getDateMMDDYYYY(tdtDealingWithBankSince));
            objTermLoanCompanyTO.setNetWorth(null);

//            Date AsDt = DateUtil.getDateMMDDYYYY(tdtAsOn);
//            if(AsDt != null){
//            Date asDate = (Date)curDate.clone();
//            asDate.setDate(AsDt.getDate());
//            asDate.setMonth(AsDt.getMonth());
//            asDate.setYear(AsDt.getYear());
//            objTermLoanCompanyTO.setNetWorthOn(DateUtil.getDateMMDDYYYY(tdtAsOn));
//            }else{
            objTermLoanCompanyTO.setNetWorthOn(null);
//            }
//            objTermLoanCompanyTO.setNetWorthOn(DateUtil.getDateMMDDYYYY(tdtAsOn));
            objTermLoanCompanyTO.setChiefExecName(null);
            objTermLoanCompanyTO.setAddrType(null);
            objTermLoanCompanyTO.setStreet(null);
            objTermLoanCompanyTO.setArea(null);
            objTermLoanCompanyTO.setCity(null);
            objTermLoanCompanyTO.setState(null);
            objTermLoanCompanyTO.setCountryCode(null);
            objTermLoanCompanyTO.setPincode(null);
            objTermLoanCompanyTO.setPhone(null);
            objTermLoanCompanyTO.setRiskRate(null);
            objTermLoanCompanyTO.setBusinessNature(null);
            objTermLoanCompanyTO.setRemarks(null);

//            Date CrDt = DateUtil.getDateMMDDYYYY(tdtCreditFacilityAvailSince);
//            if(CrDt != null){
//            Date crDate = (Date)curDate.clone();
//            crDate.setDate(CrDt.getDate());
//            crDate.setMonth(CrDt.getMonth());
//            crDate.setYear(CrDt.getYear());
//            objTermLoanCompanyTO.setCrFacilitiesSince(crDate);
//            }

//            else{
//                objTermLoanCompanyTO.setCrFacilitiesSince(DateUtil.getDateMMDDYYYY(tdtCreditFacilityAvailSince));
//            }
//            objTermLoanCompanyTO.setCrFacilitiesSince(DateUtil.getDateMMDDYYYY(tdtCreditFacilityAvailSince));
            objTermLoanCompanyTO.setStatusBy(TrueTransactMain.USER_ID);
            objTermLoanCompanyTO.setStatusDt(curDate);
        } catch (Exception e) {
            log.info("Error in setTermLoanCompany()" + e);
            parseException.logException(e, true);
//            e.printStackTrace();
        }
        return objTermLoanCompanyTO;
    }

    /**
     *
     * @return
     */
    public HashMap setTermLoanJointAcct() {
        HashMap jointAcctMap = new HashMap();
        try {
            TermLoanJointAcctTO objTermLoanJointAcctTO;
//            java.util.Set keySet =  jntAcctAll.keySet();
//            Object[] objKeySet = (Object[]) keySet.toArray();
//            HashMap oneRecord;
//            // To set the values for Security Transfer Object
//            for (int i = keySet.size() - 1,j = 0;i >= 0;--i,++j){
//                oneRecord = (HashMap) jntAcctAll.get(objKeySet[j]);
//                objTermLoanJointAcctTO = new TermLoanJointAcctTO();
//                objTermLoanJointAcctTO.setBorrowNo(lblBorrowerNo_2);
//                objTermLoanJointAcctTO.setCommand(CommonUtil.convertObjToStr(oneRecord.get(STATUS)));
//                objTermLoanJointAcctTO.setCustId(CommonUtil.convertObjToStr(oneRecord.get(CUST_ID)));
//                objTermLoanJointAcctTO.setStatus(CommonUtil.convertObjToStr(oneRecord.get(STATUS)));
//                objTermLoanJointAcctTO.setStatusBy(TrueTransactMain.USER_ID);
//                objTermLoanJointAcctTO.setStatusDt(ClientUtil.getCurrentDate());
//                jointAcctMap.put(String.valueOf(jointAcctMap.size() + 1), objTermLoanJointAcctTO);
//                objTermLoanJointAcctTO = null;
//                oneRecord = null;
//            }
//            keySet = null;
//            objKeySet = null;
        } catch (Exception e) {
            log.info("Exception caught in setTermLoanJointAcct: ");
            parseException.logException(e, true);
        }
        return jointAcctMap;
    }

    public TermLoanBorrowerTO setTermLoanBorrower(TermLoanBorrowerTO objTermLoanBorrowerTO, boolean isPopulatingData) {
        TermLoanBorrowerTO resultobjTermLoanBorrowerTO = new TermLoanBorrowerTO();
        try {

            if (getCommand() == INSERT) {
                objTermLoanBorrowerTO = new TermLoanBorrowerTO();

                objTermLoanBorrowerTO.setBorrowNo("");
                objTermLoanBorrowerTO.setConstitution(CommonUtil.convertObjToStr(singleScreenMap.get("CONSTITUTION")));
                objTermLoanBorrowerTO.setCategory(CommonUtil.convertObjToStr(singleScreenMap.get("CATEGORY")));
                objTermLoanBorrowerTO.setCustId(CommonUtil.convertObjToStr(singleScreenMap.get("CUSTOMER_ID")));
                objTermLoanBorrowerTO.setReferences(null);
                objTermLoanBorrowerTO.setStatusBy(TrueTransactMain.USER_ID);
                objTermLoanBorrowerTO.setStatusDt(curDate);
                objTermLoanBorrowerTO.setBranchCode(ProxyParameters.BRANCH_ID);
                objTermLoanBorrowerTO.setInitiatedBranch(ProxyParameters.BRANCH_ID);
                return objTermLoanBorrowerTO;
//            objTermLoanBorrowerTO.setShgID(getTxtSHGId());
            } else {
                if (isPopulatingData) {
                    objExistTermLoanBorrowerTO = objTermLoanBorrowerTO;
                    objExistTermLoanBorrowerTO.setStatusDt(curDate);
                    cbmCategory.setKeyForSelected(CommonUtil.convertObjToStr(objTermLoanBorrowerTO.getCategory()));
                    cbmConstitution.setKeyForSelected(CommonUtil.convertObjToStr(objTermLoanBorrowerTO.getConstitution()));
                } else {
                    return objExistTermLoanBorrowerTO;
                }
                System.out.println("objTermLoanBorrowerTO####" + objTermLoanBorrowerTO);
            }
        } catch (Exception e) {
            log.info("Error in setTermLoanBorrower()" + e);
            parseException.logException(e, true);
        }
        return resultobjTermLoanBorrowerTO;
    }

//    void setTxtCustID(String txtCustID){
//        this.txtCustID = txtCustID;
//        setChanged();
//    }
    public HashMap setTermLoanInterest() {
        HashMap interestMap = new HashMap();
        try {
//            TermLoanOB termLoanOB = TermLoanOB.getInstance();
            TermLoanInterestTO objTermLoanInterestTO;
//            java.util.Set keySet =  interestAll.keySet();
//            Object[] objKeySet = (Object[]) keySet.toArray();
//            HashMap oneRecord;
//            if (!(termLoanOB.getCbmIntGetFrom().getKeyForSelected().equals("PROD") || termLoanOB.getCbmIntGetFrom().getKeyForSelected().equals(""))){
            // To set the values for Interest Transfer Object
//                for (int i = keySet.size() - 1,j = 0;i >= 0;--i,++j){
//                    oneRecord = (HashMap) interestAll.get(objKeySet[j]);
            objTermLoanInterestTO = new TermLoanInterestTO();
            objTermLoanInterestTO.setSlno(CommonUtil.convertObjToDouble(new Double(1)));
            //                    objTermLoanInterestTO.setBorrowNo(getBorrowerNo());
            if (getCommand().equals(INSERT)) {
                objTermLoanInterestTO.setAcctNum(getStrACNumber());
            } else {
                objTermLoanInterestTO.setAcctNum(existTermLoanInterestTO.getAcctNum());
            }
            objTermLoanInterestTO.setAgainstClearingInt(new Double(0.0));
            objTermLoanInterestTO.setCommand(CommonUtil.convertObjToStr(getCommand()));
            objTermLoanInterestTO.setFromAmt(new Double(1));

            //                    objTermLoanInterestTO.setFromDt((Date)oneRecord.get(FROM_DATE));
            objTermLoanInterestTO.setFromDt(curDate);
            objTermLoanInterestTO.setInterest(CommonUtil.convertObjToDouble(singleScreenMap.get("LOAN_INT")));
            objTermLoanInterestTO.setInterestExpiryLimit(new Double(0.0));
            objTermLoanInterestTO.setStatementPenal(new Double(0.0));
            objTermLoanInterestTO.setPenalInterest(new Double(0.0));
//                    objTermLoanInterestTO.setStatus(CommonUtil.convertObjToStr(getCommand()));
            objTermLoanInterestTO.setToAmt(new Double(999999999));
            objTermLoanInterestTO.setActiveStatus(CommonUtil.convertObjToStr("R"));
            objTermLoanInterestTO.setAuthorizeStatus("");

            objTermLoanInterestTO.setRetCreateDt(curDate);

            //                    objTermLoanInterestTO.setToDt((Date)oneRecord.get(TO_DATE));
//                    objTermLoanInterestTO.setToDt(DateUtil.getDateMMDDYYYY(CommonUtil.convertObjToStr(getTdtRepaymentDt())));
            if (getCommand().equals(INSERT)) {
                objTermLoanInterestTO.setStatus(CommonConstants.STATUS_CREATED);
            } else if (getCommand().equals(UPDATE)) {
                objTermLoanInterestTO.setStatus(CommonConstants.STATUS_MODIFIED);
            } else if (getCommand().equals(DELETE)) {
                objTermLoanInterestTO.setStatus(CommonConstants.STATUS_DELETED);
            }

            System.out.println("objTermLoanInterestTO#####" + objTermLoanInterestTO);
            objTermLoanInterestTO.setStatusBy(TrueTransactMain.USER_ID);
            objTermLoanInterestTO.setStatusDt(curDate);
            interestMap.put(String.valueOf(1), objTermLoanInterestTO);

//                    oneRecord = null;
            objTermLoanInterestTO = null;
//                }
//            }

//            
//            // To set the values for Interest Transfer Object
//            // where as the existing records in Database are deleted in client side
//            // useful for updating the status
//            for (int i = tableUtilInterest.getRemovedValues().size() - 1,j = 0;i >= 0;--i,++j){
//                oneRecord = (HashMap) tableUtilInterest.getRemovedValues().get(j);
//                objTermLoanInterestTO = new TermLoanInterestTO();
//                objTermLoanInterestTO.setSlno(CommonUtil.convertObjToDouble( oneRecord.get(SLNO)));
//                //                objTermLoanInterestTO.setBorrowNo(getBorrowerNo());
//                objTermLoanInterestTO.setAcctNum(getStrACNumber());
//                objTermLoanInterestTO.setAgainstClearingInt(CommonUtil.convertObjToDouble(oneRecord.get(AGAINST_CLEAR_INT)));
//                objTermLoanInterestTO.setCommand(CommonUtil.convertObjToStr(oneRecord.get(COMMAND)));
//                objTermLoanInterestTO.setFromAmt(CommonUtil.convertObjToDouble(oneRecord.get(FROM_AMOUNT)));
//                
//                //                objTermLoanInterestTO.setFromDt((Date)oneRecord.get(FROM_DATE));
//                objTermLoanInterestTO.setFromDt(DateUtil.getDateMMDDYYYY(CommonUtil.convertObjToStr(oneRecord.get(FROM_DATE))));
//                objTermLoanInterestTO.setInterest(CommonUtil.convertObjToDouble(oneRecord.get(INTEREST)));
//                objTermLoanInterestTO.setInterestExpiryLimit(CommonUtil.convertObjToDouble(oneRecord.get(INTER_EXP_LIMIT)));
//                objTermLoanInterestTO.setStatementPenal(CommonUtil.convertObjToDouble(oneRecord.get(PENAL_STATEMENT)));
//                objTermLoanInterestTO.setPenalInterest(CommonUtil.convertObjToDouble(oneRecord.get(PENALTY_INTEREST)));
//                objTermLoanInterestTO.setStatus(CommonUtil.convertObjToStr(oneRecord.get(COMMAND)));
//                objTermLoanInterestTO.setToAmt(CommonUtil.convertObjToDouble(oneRecord.get(TO_AMOUNT)));
//                
//                //                objTermLoanInterestTO.setToDt((Date)oneRecord.get(TO_DATE));
//                objTermLoanInterestTO.setToDt(DateUtil.getDateMMDDYYYY(CommonUtil.convertObjToStr(oneRecord.get(TO_DATE))));
//                objTermLoanInterestTO.setStatus(CommonConstants.STATUS_DELETED);
//                objTermLoanInterestTO.setStatusBy(TrueTransactMain.USER_ID);
//                objTermLoanInterestTO.setStatusDt(curDate);
//                interestMap.put(String.valueOf(interestMap.size()+1), objTermLoanInterestTO);
//                
//                oneRecord = null;
//                objTermLoanInterestTO = null;
//            }
        } catch (Exception e) {
            log.info("Exception caught in setTermLoanInterest: " + e);
            parseException.logException(e, true);
        }
        return interestMap;
    }

    private HashMap setTermLoanSanctionSingleRecord() {
        HashMap returnValue = new HashMap();
        try {
            TermLoanSanctionTO objTermLoanSanctionTO = new TermLoanSanctionTO();
            TermLoanSanctionFacilityTO objTermLoanSanctionFacilityTO;
            HashMap sanctionMap = new HashMap();
            HashMap sanctionFacilityMap = new HashMap();
            HashMap eachSanctionRec;
            LinkedHashMap sanctionFacilityRecs;
            HashMap eachSanctionFacilityRec;
            int sanctionFacilityKey = 0;



            objTermLoanSanctionTO.setSanctionNo(String.valueOf(1));
            objTermLoanSanctionTO.setSlNo(CommonUtil.convertObjToStr(singleScreenMap.get("SANCTION_SLNO")));//getTxtSanctionNo
            objTermLoanSanctionTO.setSanctionAuthority(CommonUtil.convertObjToStr(singleScreenMap.get("SANCTION_AUTHORITY")));
            objTermLoanSanctionTO.setSanctionDt(getProperFormatDate(singleScreenMap.get("ACCT_OPEN_DT")));
            objTermLoanSanctionTO.setSanctionMode("");
            objTermLoanSanctionTO.setRemarks(CommonUtil.convertObjToStr(singleScreenMap.get("REMARKS")));
            


            objTermLoanSanctionTO.setCommand(CommonUtil.convertObjToStr(getCommand()));

            if (getCommand().equals(INSERT)) {
                objTermLoanSanctionTO.setStatus(CommonConstants.STATUS_CREATED);
            } else if (getCommand().equals(UPDATE)) {
                objTermLoanSanctionTO.setStatus(CommonConstants.STATUS_MODIFIED);
            } else if (getCommand().equals(DELETE)) {
                objTermLoanSanctionTO.setStatus(CommonConstants.STATUS_DELETED);
            }
            objTermLoanSanctionTO.setStatusBy(TrueTransactMain.USER_ID);
            objTermLoanSanctionTO.setStatusDt(curDate);


            sanctionMap.put(objTermLoanSanctionTO.getSlNo(), objTermLoanSanctionTO);

            //sanction details

            objTermLoanSanctionFacilityTO = new TermLoanSanctionFacilityTO();
            objTermLoanSanctionFacilityTO.setBorrowNo(borrowerNo);
            //                    objTermLoanSanctionFacilityTO.setSanctionNo(CommonUtil.convertObjToStr( eachSanctionFacilityRec.get(SANCTION_NO)));
            objTermLoanSanctionFacilityTO.setSanctionNo(objTermLoanSanctionTO.getSanctionNo());
            objTermLoanSanctionFacilityTO.setSlNo(new Double(1));
//                    objTermLoanSanctionFacilityTO.setFacilityType(CommonUtil.convertObjToStr( cbmTypeOfFacility.getKeyForSelected()));
            //objTermLoanSanctionFacilityTO.setLimit(CommonUtil.convertObjToDouble(singleScreenMap.get("LOAN_AMT")));//getTxtLoanAmt()));
            objTermLoanSanctionFacilityTO.setLimit(CommonUtil.convertObjToDouble(getLoanDisbursalAmt()));
            //                    objTermLoanSanctionFacilityTO.setFromDt((Date)eachSanctionFacilityRec.get(FROM));
            objTermLoanSanctionFacilityTO.setFromDt(getProperFormatDate(singleScreenMap.get("ACCT_OPEN_DT")));

            //                    objTermLoanSanctionFacilityTO.setToDt((Date)eachSanctionFacilityRec.get(TO));
          
            if (getDepoBehavesLike().equalsIgnoreCase("THRIFT")) {
                objTermLoanSanctionFacilityTO.setToDt(getProperFormatDate(getToDate()));
                objTermLoanSanctionFacilityTO.setNoInstall(CommonUtil.convertObjToDouble(getTxtNoInstallments()));
                objTermLoanSanctionFacilityTO.setRepaymentFrequency(new Double(30));
                objTermLoanSanctionFacilityTO.setRepaymentDt(getProperFormatDate(getTdtRepaymentDt()));
            } else {
                objTermLoanSanctionFacilityTO.setToDt(getProperFormatDate(singleScreenMap.get("REPAYMENT_DT")));
                objTermLoanSanctionFacilityTO.setNoInstall(new Double(1));
                objTermLoanSanctionFacilityTO.setRepaymentFrequency(new Double(1));
               objTermLoanSanctionFacilityTO.setRepaymentDt(getProperFormatDate(singleScreenMap.get("REPAYMENT_DT")));
            }
            objTermLoanSanctionFacilityTO.setProductId(CommonUtil.convertObjToStr(singleScreenMap.get("LOANS_PRODUCT")));
//                     if (getChkMoratorium_Given()){
//                objTermLoanSanctionFacilityTO.setMoratoriumGiven("Y");
//            }else{
            objTermLoanSanctionFacilityTO.setMoratoriumGiven("N");
//            }

           
            objTermLoanSanctionFacilityTO.setNoMoratorium(CommonUtil.convertObjToDouble("0"));
            objTermLoanSanctionFacilityTO.setCommand(CommonUtil.convertObjToStr(getCommand()));
            if (getCommand().equals(INSERT)) {
                objTermLoanSanctionFacilityTO.setStatus(CommonConstants.STATUS_CREATED);
            } else if (getCommand().equals(UPDATE)) {
                objTermLoanSanctionFacilityTO.setStatus(CommonConstants.STATUS_MODIFIED);
            }
            if (getCommand().equals(CommonConstants.TOSTATUS_DELETE)) {
                objTermLoanSanctionFacilityTO.setStatus(CommonConstants.STATUS_DELETED);
            }

            objTermLoanSanctionFacilityTO.setStatusBy(TrueTransactMain.USER_ID);
            objTermLoanSanctionFacilityTO.setStatusDt(curDate);
            sanctionFacilityMap.put(objTermLoanSanctionFacilityTO.getSlNo(), objTermLoanSanctionFacilityTO);
            eachSanctionFacilityRec = null;
            objTermLoanSanctionFacilityTO = null;
//                }

            objTermLoanSanctionTO = null;
            eachSanctionRec = null;
            sanctionFacilityRecs = null;

//            }

            canBorrowerDelete = false;
            returnValue.put(SANCTION, sanctionMap);
            returnValue.put(SANCTION_FACILITY, sanctionFacilityMap);

        } catch (Exception e) {
            log.info("Exception caught In setTermLoanSanction: " + e);
            parseException.logException(e, true);
        }
        return returnValue;
    }

    /**
     * ltd repayment schedule
     *
     */
    public HashMap setTermLoanRepayment(ArrayList objTermLoanRepaymentTO) {
        HashMap repayInstallmentMap = new HashMap();
        HashMap repaymentMap = new HashMap();
        HashMap installmentMap = new HashMap();
        HashMap installMultIntMap = new HashMap();
        boolean reschudleavailable = false;
        try {
            LinkedHashMap interestRecords;
            HashMap interestOneRecord;
            TermLoanRepaymentTO termLoanRepaymentTO;
            TermLoanInstallmentTO termLoanInstallmentTO;
            TermLoanInstallMultIntTO termLoanInstallMultIntTO;
            ArrayList installMultiIntList;
            java.util.Set interestKeySet;
            Object[] objInterestKeySet;
//            java.util.Set keySet =  repaymentAll.keySet();
//            Object[] objKeySet = (Object[]) keySet.toArray();
//            HashMap oneRecord;
            // To set the values for Insurance Transfer Object
//            for (int i = keySet.size() - 1,j = 0;i >= 0;--i,++j){
//                oneRecord = (HashMap) repaymentAll.get(objKeySet[j]);
            if (getCommand() == INSERT) {
                termLoanRepaymentTO = new TermLoanRepaymentTO();
                termLoanRepaymentTO.setAcctNum("");
                //                termLoanRepaymentTO.setBorrowNo(getBorrowerNo());
                termLoanRepaymentTO.setScheduleNo(new Double(1));
                termLoanRepaymentTO.setCommand("INSERT");
//                if (oneRecord.get(ADD_SIS).equals(YES)){
                termLoanRepaymentTO.setAddSi(YES);
//                }else if (oneRecord.get(ADD_SIS).equals(NO)){
//                    termLoanRepaymentTO.setAddSi(NO);
//                }else{
//                    termLoanRepaymentTO.setAddSi(" ");
//                }
                termLoanRepaymentTO.setAmtLastInstall(CommonUtil.convertObjToDouble(singleScreenMap.get("LOAN_AMT")));
                termLoanRepaymentTO.setAmtPenultimateInstall(CommonUtil.convertObjToDouble(singleScreenMap.get("LOAN_AMT")));

//                termLoanRepaymentTO.setFirstInstallDt((Date)oneRecord.get(FROM_DATE));
                termLoanRepaymentTO.setFromDate(getProperFormatDate(singleScreenMap.get("ACCT_OPEN_DT")));
                termLoanRepaymentTO.setFirstInstallDt(getProperFormatDate(singleScreenMap.get("REPAYMENT_DT")));
                termLoanRepaymentTO.setRepaymentType(new Double(1080));

//                termLoanRepaymentTO.setLastInstallDt((Date)oneRecord.get(TO_DATE));
                termLoanRepaymentTO.setLastInstallDt(getProperFormatDate(singleScreenMap.get("REPAYMENT_DT")));
                termLoanRepaymentTO.setLoanAmount(CommonUtil.convertObjToDouble(singleScreenMap.get("LOAN_AMT")));
                termLoanRepaymentTO.setNoInstallments(CommonUtil.convertObjToDouble("1"));
//                if (oneRecord.get(POST_DT_CHQ_ALLOW).equals(YES)){
                termLoanRepaymentTO.setPostDateChqallowed(YES);
//                }else if (oneRecord.get(POST_DT_CHQ_ALLOW).equals(NO)){
//                    termLoanRepaymentTO.setPostDateChqallowed(NO);
//                }else{
//                    termLoanRepaymentTO.setPostDateChqallowed(" ");
//                }
//                if (oneRecord.get(ACTIVE_STATUS).equals(YES)){
                termLoanRepaymentTO.setRepayActive(YES);
//                }else if (oneRecord.get(ACTIVE_STATUS).equals(NO)){
//                    termLoanRepaymentTO.setRepayActive(NO);
//                }else{
//                    termLoanRepaymentTO.setRepayActive(" ");
//                }
                //                termLoanRepaymentTO.setProdId(CommonUtil.convertObjToStr(getLblProdID_RS_Disp()));
                termLoanRepaymentTO.setInstallType(String.valueOf("LUMP_SUM"));
                termLoanRepaymentTO.setTotalBaseAmt(CommonUtil.convertObjToDouble(singleScreenMap.get("LOAN_AMT")));
                termLoanRepaymentTO.setTotalInstallAmt(CommonUtil.convertObjToDouble(singleScreenMap.get("LOAN_AMT")));

//                termLoanRepaymentTO.setDisbursementDt((Date)oneRecord.get(DISBURSEMENT_DT));
                termLoanRepaymentTO.setDisbursementDt(getProperFormatDate(singleScreenMap.get("REPAYMENT_DT")));
                // These fields are in server side and not in UI
                //            termLoanRepaymentTO.setRepayInterest(new);
                //            termLoanRepaymentTO.setIntType();
                //            termLoanRepaymentTO.setEmi();
                //            termLoanRepaymentTO.setRepaymentPr();
                //            termLoanRepaymentTO.setBalanceLoanAmt();
                termLoanRepaymentTO.setDisbursementId(new Double(0));
                termLoanRepaymentTO.setRepayMorotorium(null);
                termLoanRepaymentTO.setScheduleMode("REGULAR");
                termLoanRepaymentTO.setRefScheduleNo(new Double(1));
                if (getCommand().equals(INSERT)) {
                    termLoanRepaymentTO.setStatus(CommonConstants.STATUS_CREATED);
                } else if (getCommand().equals(UPDATE)) {
                    termLoanRepaymentTO.setStatus(CommonConstants.STATUS_MODIFIED);
                }
                termLoanRepaymentTO.setStatusBy(TrueTransactMain.USER_ID);
                termLoanRepaymentTO.setStatusDt(curDate);

                repaymentMap.put(String.valueOf(1), termLoanRepaymentTO);
//                if (oneRecord.get(INSTALLMENT_DETAILS) != null){  // && (oneRecord.get(COMMAND).equals(INSERT))  //Condition removed to insert in update mode also
//                    interestRecords = ((LinkedHashMap)oneRecord.get(INSTALLMENT_DETAILS));
//                    interestKeySet =  interestRecords.keySet();
//                    objInterestKeySet = (Object[]) interestKeySet.toArray();
//                    int noOfInstallment = interestKeySet.size();
//                    for (int m = noOfInstallment - 1;m >= 0;--m){
//                        interestOneRecord = (HashMap) interestRecords.get(objInterestKeySet[m]);
                termLoanInstallmentTO = new TermLoanInstallmentTO();
                termLoanInstallmentTO.setAcctNum("");
                termLoanInstallmentTO.setBalanceAmt(CommonUtil.convertObjToDouble(singleScreenMap.get("LOAN_AMT")));
                termLoanInstallmentTO.setCommand(getCommand());

//                        termLoanInstallmentTO.setInstallmentDt((Date)interestOneRecord.get(INSTALLMENT_DATE));
                termLoanInstallmentTO.setInstallmentDt(getProperFormatDate(singleScreenMap.get("REPAYMENT_DT")));
                termLoanInstallmentTO.setInstallmentSlno(new Double(1));
                termLoanInstallmentTO.setInterestAmt(CommonUtil.convertObjToDouble(singleScreenMap.get("LOAN_AMT")));
                termLoanInstallmentTO.setInterestRate(CommonUtil.convertObjToDouble(singleScreenMap.get("LOAN_INT")));
                termLoanInstallmentTO.setPrincipalAmt(CommonUtil.convertObjToDouble(singleScreenMap.get("LOAN_AMT")));
                termLoanInstallmentTO.setScheduleId(new Double(1));
                termLoanInstallmentTO.setTotalAmt(CommonUtil.convertObjToDouble(singleScreenMap.get("LOAN_AMT")));
                termLoanInstallmentTO.setActiveStatus("Y");
                termLoanInstallmentTO.setInstallmentPaid(NO);
//                        if (interestOneRecord.containsKey(MULTIPLE_INTEREST)){
//                            installMultiIntList = (ArrayList) interestOneRecord.get(MULTIPLE_INTEREST);
//                            HashMap multiIntRec;
//                            for (int n = installMultiIntList.size()-1;n>=0;--n){
//                                multiIntRec = (HashMap) installMultiIntList.get(n);
//                                termLoanInstallMultIntTO = new TermLoanInstallMultIntTO();
//                                termLoanInstallMultIntTO.setAcctNum(getStrACNumber());
//                                if (interestOneRecord.get(COMMAND).equals(INSERT)){
//                                    termLoanInstallMultIntTO.setCommand(CommonConstants.TOSTATUS_INSERT);
//                                }else if (interestOneRecord.get(COMMAND).equals(UPDATE)){
//                                    termLoanInstallMultIntTO.setCommand(CommonConstants.TOSTATUS_UPDATE);
//                                }
//                                termLoanInstallMultIntTO.setInstallmentSlno(termLoanInstallmentTO.getInstallmentSlno());
//                                termLoanInstallMultIntTO.setInterestRate(CommonUtil.convertObjToDouble(multiIntRec.get(INTEREST_RATE)));
//                                termLoanInstallMultIntTO.setFromDt(DateUtil.getDateMMDDYYYY(CommonUtil.convertObjToStr(multiIntRec.get(FROM_DATE))));
//                                termLoanInstallMultIntTO.setToDt(DateUtil.getDateMMDDYYYY(CommonUtil.convertObjToStr(multiIntRec.get(TO_DATE))));
//                                
////                                termLoanInstallMultIntTO.setFromDt((Date)multiIntRec.get(FROM_DATE));
////                                
////                                termLoanInstallMultIntTO.setToDt((Date)multiIntRec.get(TO_DATE));
//                                termLoanInstallMultIntTO.setScheduleId(termLoanInstallmentTO.getScheduleId());
//                                installMultIntMap.put(String.valueOf(installMultIntMap.size()), termLoanInstallMultIntTO);
//                                termLoanInstallMultIntTO = null;
//                                multiIntRec = null;
//                            }
//                            multiIntRec = null;
//                            termLoanInstallMultIntTO = null;
//                            installMultiIntList = null;
//                        }
                if (getCommand().equals(INSERT)) {
                    termLoanInstallmentTO.setStatus(CommonConstants.STATUS_CREATED);
                    reschudleavailable = true;
                } else if (getCommand().equals(UPDATE)) {
                    termLoanInstallmentTO.setStatus(CommonConstants.STATUS_MODIFIED);
                }
                installmentMap.put(String.valueOf(installmentMap.size() + 1), termLoanInstallmentTO);
                interestOneRecord = null;
//                    }
//                }
//                if (oneRecord.get(MORATORIUM_INTEREST) != null && (oneRecord.get(COMMAND).equals(INSERT))){
//                    interestRecords = ((LinkedHashMap)oneRecord.get(MORATORIUM_INTEREST));
//                    interestKeySet =  interestRecords.keySet();
//                    objInterestKeySet = (Object[]) interestKeySet.toArray();
//                    HashMap moratoriumIntRec = (HashMap) interestRecords.get(objInterestKeySet[0]);
//                    if (moratoriumIntRec.containsKey(MULTIPLE_INTEREST)){
//                        installMultiIntList = (ArrayList) moratoriumIntRec.get(MULTIPLE_INTEREST);
//                        HashMap multiIntRec;
//                        for (int n = installMultiIntList.size()-1;n>=0;--n){
//                            multiIntRec = (HashMap) installMultiIntList.get(n);
//                            termLoanInstallMultIntTO = new TermLoanInstallMultIntTO();
//                            termLoanInstallMultIntTO.setAcctNum(getStrACNumber());
//                            if (oneRecord.get(COMMAND).equals(INSERT)){
//                                termLoanInstallMultIntTO.setCommand(CommonConstants.TOSTATUS_INSERT);
//                            }else if (oneRecord.get(COMMAND).equals(UPDATE)){
//                                termLoanInstallMultIntTO.setCommand(CommonConstants.TOSTATUS_UPDATE);
//                            }
//                            termLoanInstallMultIntTO.setInterestRate(CommonUtil.convertObjToDouble(multiIntRec.get(INTEREST_RATE)));
//                            termLoanInstallMultIntTO.setFromDt(DateUtil.getDateMMDDYYYY(CommonUtil.convertObjToStr(multiIntRec.get(FROM_DATE))));
//                            termLoanInstallMultIntTO.setToDt(DateUtil.getDateMMDDYYYY(CommonUtil.convertObjToStr(multiIntRec.get(TO_DATE))));
//                            
////                             termLoanInstallMultIntTO.setFromDt((Date)multiIntRec.get(FROM_DATE));
////                             
////                            termLoanInstallMultIntTO.setToDt((Date)multiIntRec.get(TO_DATE));
//                            termLoanInstallMultIntTO.setScheduleId(CommonUtil.convertObjToDouble(oneRecord.get(SLNO)));
//                            installMultIntMap.put(String.valueOf(installMultIntMap.size()), termLoanInstallMultIntTO);
//                            termLoanInstallMultIntTO = null;
//                            multiIntRec = null;
//                        }
//                        multiIntRec = null;
//                        termLoanInstallMultIntTO = null;
//                        installMultiIntList = null;
//                    }
//                    moratoriumIntRec = null;
//                }
//                oneRecord = null;
                termLoanRepaymentTO = null;
//            }
            } else {
                termLoanRepaymentTO = (TermLoanRepaymentTO) objTermLoanRepaymentTO.get(0);
                repaymentMap.put(String.valueOf(1), termLoanRepaymentTO);
                installmentMap = new HashMap();
            }
            // To set the values for Repayment Transfer Object
            // where as the existing records in Database are deleted in client side
            // useful for updating the status
//            for (int i = tableUtilRepayment.getRemovedValues().size() - 1,j = 0;i >= 0;--i,++j){
//                oneRecord = (HashMap) tableUtilRepayment.getRemovedValues().get(j);
//                termLoanRepaymentTO = new TermLoanRepaymentTO();
//                termLoanRepaymentTO.setAcctNum(getStrACNumber());
//                //                termLoanRepaymentTO.setBorrowNo(getBorrowerNo());
//                termLoanRepaymentTO.setScheduleNo(CommonUtil.convertObjToDouble(oneRecord.get(SLNO)));
//                termLoanRepaymentTO.setCommand(CommonUtil.convertObjToStr(oneRecord.get(COMMAND)));
//                if (oneRecord.get(ADD_SIS).equals(YES)){
//                    termLoanRepaymentTO.setAddSi(YES);
//                }else if (oneRecord.get(ADD_SIS).equals(NO)){
//                    termLoanRepaymentTO.setAddSi(NO);
//                }else{
//                    termLoanRepaymentTO.setAddSi(" ");
//                }
//                termLoanRepaymentTO.setAmtLastInstall(CommonUtil.convertObjToDouble(oneRecord.get(LAST_INSTALL_AMT)));
//                termLoanRepaymentTO.setAmtPenultimateInstall(CommonUtil.convertObjToDouble(oneRecord.get(PENULTI_AMT_INSTALL)));
//                
////                termLoanRepaymentTO.setFirstInstallDt((Date)oneRecord.get(FROM_DATE));
//                termLoanRepaymentTO.setFromDate(DateUtil.getDateMMDDYYYY(CommonUtil.convertObjToStr(oneRecord.get(FROM_DATE))));
//                termLoanRepaymentTO.setFirstInstallDt(DateUtil.getDateMMDDYYYY(CommonUtil.convertObjToStr(oneRecord.get(INSTAL_STDT))));
//                termLoanRepaymentTO.setRepaymentType(CommonUtil.convertObjToDouble(oneRecord.get(REPAY_FREQ)));
//                
////                termLoanRepaymentTO.setLastInstallDt((Date)oneRecord.get(TO_DATE));
//                termLoanRepaymentTO.setLastInstallDt(DateUtil.getDateMMDDYYYY(CommonUtil.convertObjToStr(oneRecord.get(TO_DATE))));
//                termLoanRepaymentTO.setLoanAmount(CommonUtil.convertObjToDouble(oneRecord.get(LOAN_AMT)));
//                //                if (oneRecord.get(MORATORIUM_GIVEN).equals(YES)){
//                //                    termLoanRepaymentTO.setMoratoriumGiven(YES);
//                //                }else{
//                //                    termLoanRepaymentTO.setMoratoriumGiven(NO);
//                //                }
//                termLoanRepaymentTO.setNoInstallments(CommonUtil.convertObjToDouble(oneRecord.get(NO_INSTALLMENTS)));
//                //                termLoanRepaymentTO.setNoMoratorium(CommonUtil.convertObjToDouble(oneRecord.get(NO_MORATORIUM)));
//                if (oneRecord.get(POST_DT_CHQ_ALLOW).equals(YES)){
//                    termLoanRepaymentTO.setPostDateChqallowed(YES);
//                }else if (oneRecord.get(POST_DT_CHQ_ALLOW).equals(NO)){
//                    termLoanRepaymentTO.setPostDateChqallowed(NO);
//                }else{
//                    termLoanRepaymentTO.setPostDateChqallowed(" ");
//                }
//                if (oneRecord.get(ACTIVE_STATUS).equals(YES)){
//                    termLoanRepaymentTO.setRepayActive(YES);
//                }else if (oneRecord.get(ACTIVE_STATUS).equals(NO)){
//                    termLoanRepaymentTO.setRepayActive(NO);
//                }else{
//                    termLoanRepaymentTO.setRepayActive(" ");
//                }
//                //                termLoanRepaymentTO.setProdId(CommonUtil.convertObjToStr(getLblProdID_RS_Disp()));
//                termLoanRepaymentTO.setInstallType(CommonUtil.convertObjToStr(oneRecord.get(REPAY_TYPE)));
//                termLoanRepaymentTO.setTotalBaseAmt(CommonUtil.convertObjToDouble(oneRecord.get(TOT_BASE_AMT)));
//                termLoanRepaymentTO.setTotalInstallAmt(CommonUtil.convertObjToDouble(oneRecord.get(TOT_INSTALL_AMT)));
//                
////                termLoanRepaymentTO.setDisbursementDt((Date)oneRecord.get(DISBURSEMENT_DT));
//                termLoanRepaymentTO.setDisbursementDt(DateUtil.getDateMMDDYYYY(CommonUtil.convertObjToStr(oneRecord.get(DISBURSEMENT_DT))));
//                // These fields are in server side and not in UI
//                //            termLoanRepaymentTO.setRepayInterest(new);
//                //            termLoanRepaymentTO.setIntType();
//                //            termLoanRepaymentTO.setEmi();
//                //            termLoanRepaymentTO.setRepaymentPr();
//                //            termLoanRepaymentTO.setBalanceLoanAmt();
//                termLoanRepaymentTO.setDisbursementId(CommonUtil.convertObjToDouble(oneRecord.get(DISBURSEMENT_ID)));
//                termLoanRepaymentTO.setScheduleMode(CommonUtil.convertObjToStr(oneRecord.get(SCHEDULE_MODE)));
//                termLoanRepaymentTO.setRefScheduleNo(CommonUtil.convertObjToDouble(oneRecord.get(REF_SCHEDULE_NUMBER)));
//                termLoanRepaymentTO.setStatus(CommonConstants.STATUS_DELETED);
//                termLoanRepaymentTO.setStatusBy(TrueTransactMain.USER_ID);
//                termLoanRepaymentTO.setStatusDt(curDate);
//                repaymentMap.put(String.valueOf(repaymentMap.size()+1), termLoanRepaymentTO);
//                
//                termLoanInstallmentTO = new TermLoanInstallmentTO();
//                termLoanInstallmentTO.setAcctNum(getStrACNumber());
//                termLoanInstallmentTO.setCommand(CommonConstants.TOSTATUS_DELETE);
//                termLoanInstallmentTO.setInstallmentSlno(CommonUtil.convertObjToDouble(""));
//                termLoanInstallmentTO.setScheduleId(CommonUtil.convertObjToDouble(oneRecord.get(SLNO)));
//                termLoanInstallmentTO.setStatus(CommonConstants.STATUS_DELETED);
//                
//                installmentMap.put(String.valueOf(installmentMap.size()+1), termLoanInstallmentTO);
//                
//                oneRecord = null;
//                termLoanRepaymentTO = null;
//            }
//            keySet = null;

//            objKeySet = null;
            interestKeySet = null;
            objInterestKeySet = null;
            termLoanInstallMultIntTO = null;
            installMultiIntList = null;
            repayInstallmentMap.put(REPAYMENT_DETAILS, repaymentMap);
            repayInstallmentMap.put(INSTALLMENT_DETAILS, installmentMap);
            repayInstallmentMap.put(INSTALLMULTINT_DETAILS, installMultIntMap);
//            if(reschudleavailable && getUptoDtInterestMap() !=null &&  getUptoDtInterestMap().size()>0)
//                repayInstallmentMap.put("INT_TRANSACTION_REPAYMENT",getUptoDtInterestMap());
        } catch (Exception e) {
              e.printStackTrace();
            log.info("Error In setTermLoanInsurance() " + e);
            parseException.logException(e, true);
        }
        return repayInstallmentMap;
    }

    public Date getProperFormatDate(Object obj) {
        Date currDt = null;
        if (obj != null && obj.toString().length() > 0) {
            Date tempDt = DateUtil.getDateMMDDYYYY(CommonUtil.convertObjToStr(obj));
            currDt = (Date) curDate.clone();
            currDt.setDate(tempDt.getDate());
            currDt.setMonth(tempDt.getMonth());
            currDt.setYear(tempDt.getYear());
        }
        return currDt;
    }

    /**
     * This method will destroy or nullify the ArrayList, LinkedHashMap,
     * EnhancedTableModel, TableUtil instances
     */
    public void destroyObjects() {
//        termLoanBorrowerOB.destroyObjects();
//        termLoanAuthorizedSignatoryOB.destroyObjects();
//        termLoanAuthorizedSignatoryInstructionOB.destroyObjects();
//        termLoanPoAOB.destroyObjects();
//        termLoanSecurityOB.destroyObjects();
//        termLoanRepaymentOB.destroyObjects();
//        termLoanInterestOB.destroyObjects();
//        termLoanGuarantorOB.destroyObjects();
//        termLoanDocumentDetailsOB.destroyObjects();
//        agriSubLimitOB.destroyObjects();
////        settlementOB.destroyObjects();//dontdelete
//        agriSubLimitOB.destroyObjects();
//        tblSanctionFacility = null;
        tblSanctionMainMds = null;
        tblSanctionMain = null;
        totDepositList = null;
        deleteTblDataList = null;
        totDepositList = null;
        allTblDataList = null;
        oldDepositIntRate = 0;
        objSMSSubscriptionTO = null;
        mdsMap = null;
        paddyMap = null;
    }

    /**
     * This method will create instances like ArrayList, LinkedHashMap,
     * EnhancedTableModel, TableUtil
     */
    public void createObject() {
//        termLoanBorrowerOB.createObject();
//        termLoanSecurityOB.createObject();
//        termLoanRepaymentOB.createObject();
//        termLoanInterestOB.createObject();
//        //        if (loanType.equals("OTHERS")) {
//        termLoanAuthorizedSignatoryOB.createObject();
//        termLoanAuthorizedSignatoryInstructionOB.createObject();
//        termLoanPoAOB.createObject();
//        termLoanGuarantorOB.createObject();
//        termLoanDocumentDetailsOB.createObject();
//        termLoanAdditionalSanctionOB.createObject();
//        //        }
//        tblSanctionFacility = new EnhancedTableModel(null, sanctionFacilityTitle);
        tblSanctionMain = new EnhancedTableModel(null, sanctionMainTitle);
        tblSanctionMainMds = new EnhancedTableModel(null, sanctionMainTitleMds);

        totDepositList = new ArrayList();
//        tblShare=new EnhancedTableModel(null,shareTitle);
//        facilityTabSanction = new ArrayList();
//        tblSanctionFacility.setDataArrayList(null, sanctionFacilityTitle);
//        sanctionMainAllTabRecords = new ArrayList();
        tblSanctionMain.setDataArrayList(null, sanctionMainTitle);
        tblSanctionMainMds.setDataArrayList(null, sanctionMainTitleMds);
//        facilityAll = new LinkedHashMap();
//        tableUtilSanction_Facility = new TableUtil();
//        tableUtilSanction_Facility.setAttributeKey(SLNO);
//        tableUtilSanction_Main = new TableUtil();
//        tableUtilSanction_Main.setAttributeKey(SANCTION_SL_NO);
//        settlementOB.createObject();//dontdelete
    }

    public String getDepoBehavesLike() {
        return depoBehavesLike;
    }

    public void setDepoBehavesLike(String depoBehavesLike) {
        this.depoBehavesLike = depoBehavesLike;
    }
   
    public com.see.truetransact.ui.termloan.depositLoan.DepositLoanRepaymentOB getTermLoanRepaymentOB() {
        return termLoanRepaymentOB;
    }

    public void setTermLoanRepaymentOB(com.see.truetransact.ui.termloan.depositLoan.DepositLoanRepaymentOB termLoanRepaymentOB) {
      this.termLoanRepaymentOB = termLoanRepaymentOB;
    }

    public String getCboRepayFreq() {
        return cboRepayFreq;
    }

    public void setCboRepayFreq(String cboRepayFreq) {
        this.cboRepayFreq = cboRepayFreq;
    }



    public String getRepayScheduleMode() {
        return repayScheduleMode;
    }

    public void setRepayScheduleMode(String repayScheduleMode) {
        this.repayScheduleMode = repayScheduleMode;
    }
  
    public String getFirstInstallDate() {
        return firstInstallDate;
    }

    public void setFirstInstallDate(String firstInstallDate) {
        this.firstInstallDate = firstInstallDate;
    }

    public String getFromDate() {
        return fromDate;
    }

    public void setFromDate(String fromDate) {
        this.fromDate = fromDate;
    }

    public String getToDate() {
        return toDate;
    }

    public void setToDate(String toDate) {
        this.toDate = toDate;
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
     * Getter for property resultStatus.
     *
     * @return Value of property resultStatus.
     */
    public int getResultStatus() {
        return resultStatus;
    }

    /**
     * Setter for property resultStatus.
     *
     * @param resultStatus New value of property resultStatus.
     */
    public void setResultStatus(int resultStatus) {
        this.resultStatus = resultStatus;
        setChanged();
    }

    /**
     * Getter for property cboAccStatus.
     *
     * @return Value of property cboAccStatus.
     */
    public java.lang.String getCboAccStatus() {
        return cboAccStatus;
    }

    /**
     * Setter for property cboAccStatus.
     *
     * @param cboAccStatus New value of property cboAccStatus.
     */
    public void setCboAccStatus(java.lang.String cboAccStatus) {
        this.cboAccStatus = cboAccStatus;
        setChanged();
    }

    /**
     * Getter for property cboConstitution.
     *
     * @return Value of property cboConstitution.
     */
    public java.lang.String getCboConstitution() {
        return cboConstitution;
    }

    /**
     * Setter for property cboConstitution.
     *
     * @param cboConstitution New value of property cboConstitution.
     */
    public void setCboConstitution(java.lang.String cboConstitution) {
        this.cboConstitution = cboConstitution;
    }

    /**
     * Getter for property cboCategory.
     *
     * @return Value of property cboCategory.
     */
    public java.lang.String getCboCategory() {
        return cboCategory;

    }

    /**
     * Setter for property cboCategory.
     *
     * @param cboCategory New value of property cboCategory.
     */
    public void setCboCategory(java.lang.String cboCategory) {
        this.cboCategory = cboCategory;
    }

    /**
     * Getter for property cboSanctioningAuthority.
     *
     * @return Value of property cboSanctioningAuthority.
     */
    public java.lang.String getCboSanctioningAuthority() {
        return cboSanctioningAuthority;
    }

    /**
     * Setter for property cboSanctioningAuthority.
     *
     * @param cboSanctioningAuthority New value of property
     * cboSanctioningAuthority.
     */
    public void setCboSanctioningAuthority(java.lang.String cboSanctioningAuthority) {
        this.cboSanctioningAuthority = cboSanctioningAuthority;
        setChanged();
    }

    /**
     * Getter for property cboModeSanction.
     *
     * @return Value of property cboModeSanction.
     */
    public java.lang.String getCboModeSanction() {
        return cboModeSanction;

    }

    /**
     * Setter for property cboModeSanction.
     *
     * @param cboModeSanction New value of property cboModeSanction.
     */
    public void setCboModeSanction(java.lang.String cboModeSanction) {
        this.cboModeSanction = cboModeSanction;
        setChanged();
    }

    /**
     * Getter for property tblSanctionMain.
     *
     * @return Value of property tblSanctionMain.
     */
    public com.see.truetransact.clientutil.EnhancedTableModel getTblSanctionMain() {
        return tblSanctionMain;
    }

    /**
     * Setter for property tblSanctionMain.
     *
     * @param tblSanctionMain New value of property tblSanctionMain.
     */
    public void setTblSanctionMain(com.see.truetransact.clientutil.EnhancedTableModel tblSanctionMain) {
        this.tblSanctionMain = tblSanctionMain;
    }

    /**
     * Getter for property cbmDepositProduct.
     *
     * @return Value of property cbmDepositProduct.
     */
    public com.see.truetransact.clientutil.ComboBoxModel getCbmDepositProduct() {
        return cbmDepositProduct;
    }

    /**
     * Setter for property cbmDepositProduct.
     *
     * @param cbmDepositProduct New value of property cbmDepositProduct.
     */
    public void setCbmDepositProduct(com.see.truetransact.clientutil.ComboBoxModel cbmDepositProduct) {
        this.cbmDepositProduct = cbmDepositProduct;
        setChanged();
    }

    /**
     * Getter for property cbmLoanProduct.
     *
     * @return Value of property cbmLoanProduct.
     */
    public com.see.truetransact.clientutil.ComboBoxModel getCbmLoanProduct() {
        return cbmLoanProduct;
    }

    /**
     * Setter for property cbmLoanProduct.
     *
     * @param cbmLoanProduct New value of property cbmLoanProduct.
     */
    public void setCbmLoanProduct(com.see.truetransact.clientutil.ComboBoxModel cbmLoanProduct) {
        this.cbmLoanProduct = cbmLoanProduct;
    }

    /**
     * Getter for property cboLoanProduct.
     *
     * @return Value of property cboLoanProduct.
     */
    public java.lang.String getCboLoanProduct() {
        return cboLoanProduct;
    }

    /**
     * Setter for property cboLoanProduct.
     *
     * @param cboLoanProduct New value of property cboLoanProduct.
     */
    public void setCboLoanProduct(java.lang.String cboLoanProduct) {
        this.cboLoanProduct = cboLoanProduct;
        setChanged();
    }

    /**
     * Getter for property txtDepositNo.
     *
     * @return Value of property txtDepositNo.
     */
    public java.lang.String getTxtDepositNo() {
        return txtDepositNo;
    }

    /**
     * Setter for property txtDepositNo.
     *
     * @param txtDepositNo New value of property txtDepositNo.
     */
    public void setTxtDepositNo(java.lang.String txtDepositNo) {
        this.txtDepositNo = txtDepositNo;
        setChanged();
    }

    /**
     * Getter for property cboDepositProduct.
     *
     * @return Value of property cboDepositProduct.
     */
    public java.lang.String getCboDepositProduct() {
        return cboDepositProduct;
    }

    /**
     * Setter for property cboDepositProduct.
     *
     * @param cboDepositProduct New value of property cboDepositProduct.
     */
    public void setCboDepositProduct(java.lang.String cboDepositProduct) {
        this.cboDepositProduct = cboDepositProduct;
        setChanged();
    }

    /**
     * Getter for property lblCustNameValue.
     *
     * @return Value of property lblCustNameValue.
     */
    public java.lang.String getLblCustNameValue() {
        return lblCustNameValue;
    }

    /**
     * Setter for property lblCustNameValue.
     *
     * @param lblCustNameValue New value of property lblCustNameValue.
     */
    public void setLblCustNameValue(java.lang.String lblCustNameValue) {
        this.lblCustNameValue = lblCustNameValue;
        setChanged();
    }

    /**
     * Getter for property lblCustomerIdValue.
     *
     * @return Value of property lblCustomerIdValue.
     */
    public java.lang.String getLblCustomerIdValue() {
        return lblCustomerIdValue;
    }

    /**
     * Setter for property lblCustomerIdValue.
     *
     * @param lblCustomerIdValue New value of property lblCustomerIdValue.
     */
    public void setLblCustomerIdValue(java.lang.String lblCustomerIdValue) {
        this.lblCustomerIdValue = lblCustomerIdValue;
        setChanged();
    }

    /**
     * Getter for property lblMemberIdValue.
     *
     * @return Value of property lblMemberIdValue.
     */
    public java.lang.String getLblMemberIdValue() {
        return lblMemberIdValue;
    }

    /**
     * Setter for property lblMemberIdValue.
     *
     * @param lblMemberIdValue New value of property lblMemberIdValue.
     */
    public void setLblMemberIdValue(java.lang.String lblMemberIdValue) {
        this.lblMemberIdValue = lblMemberIdValue;
        setChanged();
    }

    /**
     * Getter for property lblShowAccountHeadId.
     *
     * @return Value of property lblShowAccountHeadId.
     */
    public java.lang.String getLblShowAccountHeadId() {
        return lblShowAccountHeadId;
    }

    /**
     * Setter for property lblShowAccountHeadId.
     *
     * @param lblShowAccountHeadId New value of property lblShowAccountHeadId.
     */
    public void setLblShowAccountHeadId(java.lang.String lblShowAccountHeadId) {
        this.lblShowAccountHeadId = lblShowAccountHeadId;
        setChanged();
    }

    /**
     * Getter for property txtInter.
     *
     * @return Value of property txtInter.
     */
    public java.lang.String getTxtInter() {
        return txtInter;
    }

    /**
     * Setter for property txtInter.
     *
     * @param txtInter New value of property txtInter.
     */
    public void setTxtInter(java.lang.String txtInter) {
        this.txtInter = txtInter;
        setChanged();
    }

    /**
     * Getter for property tdtRepaymentDt.
     *
     * @return Value of property tdtRepaymentDt.
     */
    public java.lang.String getTdtRepaymentDt() {
        return tdtRepaymentDt;
    }

    /**
     * Setter for property tdtRepaymentDt.
     *
     * @param tdtRepaymentDt New value of property tdtRepaymentDt.
     */
    public void setTdtRepaymentDt(java.lang.String tdtRepaymentDt) {
        this.tdtRepaymentDt = tdtRepaymentDt;
        setChanged();
    }

    /**
     * Getter for property tdtAccountOpenDate.
     *
     * @return Value of property tdtAccountOpenDate.
     */
    public java.lang.String getTdtAccountOpenDate() {
        return tdtAccountOpenDate;
    }

    /**
     * Setter for property tdtAccountOpenDate.
     *
     * @param tdtAccountOpenDate New value of property tdtAccountOpenDate.
     */
    public void setTdtAccountOpenDate(java.lang.String tdtAccountOpenDate) {
        this.tdtAccountOpenDate = tdtAccountOpenDate;
        setChanged();
    }

    /**
     * Getter for property txtLoanAmt.
     *
     * @return Value of property txtLoanAmt.
     */
    public java.lang.String getTxtLoanAmt() {
        return txtLoanAmt;
    }

    /**
     * Setter for property txtLoanAmt.
     *
     * @param txtLoanAmt New value of property txtLoanAmt.
     */
    public void setTxtLoanAmt(java.lang.String txtLoanAmt) {
        this.txtLoanAmt = txtLoanAmt;
        setChanged();
    }

    /**
     * Getter for property txtTotalNoOfShare.
     *
     * @return Value of property txtTotalNoOfShare.
     */
    public java.lang.String getTxtTotalNoOfShare() {
        return txtTotalNoOfShare;
    }

    /**
     * Setter for property txtTotalNoOfShare.
     *
     * @param txtTotalNoOfShare New value of property txtTotalNoOfShare.
     */
    public void setTxtTotalNoOfShare(java.lang.String txtTotalNoOfShare) {
        this.txtTotalNoOfShare = txtTotalNoOfShare;
        setChanged();
    }

    /**
     * Getter for property txtTotalShareAmount.
     *
     * @return Value of property txtTotalShareAmount.
     */
    public java.lang.String getTxtTotalShareAmount() {
        return txtTotalShareAmount;
    }

    /**
     * Setter for property txtTotalShareAmount.
     *
     * @param txtTotalShareAmount New value of property txtTotalShareAmount.
     */
    public void setTxtTotalShareAmount(java.lang.String txtTotalShareAmount) {
        this.txtTotalShareAmount = txtTotalShareAmount;
        setChanged();
    }

    /**
     * Getter for property txtSanctionRef.
     *
     * @return Value of property txtSanctionRef.
     */
    public java.lang.String getTxtSanctionRef() {
        return txtSanctionRef;
    }

    /**
     * Setter for property txtSanctionRef.
     *
     * @param txtSanctionRef New value of property txtSanctionRef.
     */
    public void setTxtSanctionRef(java.lang.String txtSanctionRef) {
        this.txtSanctionRef = txtSanctionRef;
        setChanged();
    }

    /**
     * To set the status based on ActionType, either New, Edit, etc.,
     */
    public void setStatus() {
        this.setLblStatus(ClientConstants.ACTION_STATUS[this.getActionType()]);
        setChanged();
        ttNotifyObservers();
    }

    /**
     * To update the Status based on result performed by () method
     */
    public void setResultStatus() {
        this.setLblStatus(ClientConstants.RESULT_STATUS[this.getResult()]);
        ttNotifyObservers();
    }

    /**
     * Getter for property lblStatus.
     *
     * @return Value of property lblStatus.
     */
    public java.lang.String getLblStatus() {
        return lblStatus;
    }

    /**
     * Setter for property lblStatus.
     *
     * @param lblStatus New value of property lblStatus.
     */
    public void setLblStatus(java.lang.String lblStatus) {
        this.lblStatus = lblStatus;
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
     * Getter for property lblAcctNo_Sanction_Disp.
     *
     * @return Value of property lblAcctNo_Sanction_Disp.
     */
    public java.lang.String getLblAcctNo_Sanction_Disp() {
        return lblAcctNo_Sanction_Disp;
    }

    /**
     * Setter for property lblAcctNo_Sanction_Disp.
     *
     * @param lblAcctNo_Sanction_Disp New value of property
     * lblAcctNo_Sanction_Disp.
     */
    public void setLblAcctNo_Sanction_Disp(java.lang.String lblAcctNo_Sanction_Disp) {
        this.lblAcctNo_Sanction_Disp = lblAcctNo_Sanction_Disp;
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
     * Getter for property strACNumber.
     *
     * @return Value of property strACNumber.
     */
    public java.lang.String getStrACNumber() {
        return strACNumber;
    }

    /**
     * Setter for property strACNumber.
     *
     * @param strACNumber New value of property strACNumber.
     */
    public void setStrACNumber(java.lang.String strACNumber) {
        this.strACNumber = strACNumber;
    }

    /**
     * Getter for property txtRemarks.
     *
     * @return Value of property txtRemarks.
     */
    public java.lang.String getTxtRemarks() {
        return txtRemarks;
    }

    /**
     * Setter for property txtRemarks.
     *
     * @param txtRemarks New value of property txtRemarks.
     */
    public void setTxtRemarks(java.lang.String txtRemarks) {
        this.txtRemarks = txtRemarks;
    }

    /**
     * Getter for property borrowerNo.
     *
     * @return Value of property borrowerNo.
     */
    public java.lang.String getBorrowerNo() {
        return borrowerNo;
    }

    /**
     * Setter for property borrowerNo.
     *
     * @param borrowerNo New value of property borrowerNo.
     */
    public void setBorrowerNo(java.lang.String borrowerNo) {
        this.borrowerNo = borrowerNo;
    }

    /**
     * Getter for property loanType.
     *
     * @return Value of property loanType.
     */
    public java.lang.String getLoanType() {
        return loanType;
    }

    /**
     * Setter for property loanType.
     *
     * @param loanType New value of property loanType.
     */
    public void setLoanType(java.lang.String loanType) {
        this.loanType = loanType;
    }

    /**
     * Getter for property txtSanctionNo.
     *
     * @return Value of property txtSanctionNo.
     */
    public java.lang.String getTxtSanctionNo() {
        return txtSanctionNo;
    }

    /**
     * Setter for property txtSanctionNo.
     *
     * @param txtSanctionNo New value of property txtSanctionNo.
     */
    public void setTxtSanctionNo(java.lang.String txtSanctionNo) {
        this.txtSanctionNo = txtSanctionNo;
    }

    /**
     *
     *
     * /**
     * Getter for property isMultipleDeposit.
     *
     * @return Value of property isMultipleDeposit.
     */
    public java.util.LinkedHashMap getIsMultipleDeposit() {
        return isMultipleDeposit;
    }

    /**
     * Setter for property isMultipleDeposit.
     *
     * @param isMultipleDeposit New value of property isMultipleDeposit.
     */
    public void setIsMultipleDeposit(java.util.LinkedHashMap isMultipleDeposit) {
        this.isMultipleDeposit = isMultipleDeposit;
    }

    /**
     * Getter for property newTransactionMap.
     *
     * @return Value of property newTransactionMap.
     */
    public java.util.HashMap getNewTransactionMap() {
        return newTransactionMap;
    }

    /**
     * Setter for property newTransactionMap.
     *
     * @param newTransactionMap New value of property newTransactionMap.
     */
    public void setNewTransactionMap(java.util.HashMap newTransactionMap) {
        this.newTransactionMap = newTransactionMap;
    }

    /**
     * Getter for property minLimitValue.
     *
     * @return Value of property minLimitValue.
     */
    public java.lang.Double getMinLimitValue() {
        return minLimitValue;
    }

    /**
     * Setter for property minLimitValue.
     *
     * @param minLimitValue New value of property minLimitValue.
     */
    public void setMinLimitValue(java.lang.Double minLimitValue) {
        this.minLimitValue = minLimitValue;
    }

    /**
     * Getter for property maxLimitValue.
     *
     * @return Value of property maxLimitValue.
     */
    public java.lang.Double getMaxLimitValue() {
        return maxLimitValue;
    }

    /**
     * Setter for property maxLimitValue.
     *
     * @param maxLimitValue New value of property maxLimitValue.
     */
    public void setMaxLimitValue(java.lang.Double maxLimitValue) {
        this.maxLimitValue = maxLimitValue;
    }

    /**
     * Getter for property sanctionAmtRoundOff.
     *
     * @return Value of property sanctionAmtRoundOff.
     */
    public java.lang.String getSanctionAmtRoundOff() {
        return sanctionAmtRoundOff;
    }

    /**
     * Setter for property sanctionAmtRoundOff.
     *
     * @param sanctionAmtRoundOff New value of property sanctionAmtRoundOff.
     */
    public void setSanctionAmtRoundOff(java.lang.String sanctionAmtRoundOff) {
        this.sanctionAmtRoundOff = sanctionAmtRoundOff;
    }

    /**
     * Getter for property eligibleAmt.
     *
     * @return Value of property eligibleAmt.
     */
    public double getEligibleAmt() {
        return eligibleAmt;
    }

    /**
     * Setter for property eligibleAmt.
     *
     * @param eligibleAmt New value of property eligibleAmt.
     */
    public void setEligibleAmt(double eligibleAmt) {
        this.eligibleAmt = eligibleAmt;
    }

    /**
     * Getter for property eligibleMargin.
     *
     * @return Value of property eligibleMargin.
     */
    public double getEligibleMargin() {
        return eligibleMargin;
    }

    /**
     * Setter for property eligibleMargin.
     *
     * @param eligibleMargin New value of property eligibleMargin.
     */
    public void setEligibleMargin(double eligibleMargin) {
        this.eligibleMargin = eligibleMargin;
    }

    /**
     * Getter for property currDepositRepaymentDt.
     *
     * @return Value of property currDepositRepaymentDt.
     */
    public java.lang.String getCurrDepositRepaymentDt() {
        return currDepositRepaymentDt;
    }

    /**
     * Setter for property currDepositRepaymentDt.
     *
     * @param currDepositRepaymentDt New value of property
     * currDepositRepaymentDt.
     */
    public void setCurrDepositRepaymentDt(java.lang.String currDepositRepaymentDt) {
        this.currDepositRepaymentDt = currDepositRepaymentDt;
    }

    /**
     * Getter for property previousDepositRepaymentDt.
     *
     * @return Value of property previousDepositRepaymentDt.
     */
    public java.lang.String getPreviousDepositRepaymentDt() {
        return previousDepositRepaymentDt;
    }

    /**
     * Setter for property previousDepositRepaymentDt.
     *
     * @param previousDepositRepaymentDt New value of property
     * previousDepositRepaymentDt.
     */
    public void setPreviousDepositRepaymentDt(java.lang.String previousDepositRepaymentDt) {
        this.previousDepositRepaymentDt = previousDepositRepaymentDt;
    }

    /**
     * Getter for property oldDepositIntRate.
     *
     * @return Value of property oldDepositIntRate.
     */
    public double getOldDepositIntRate() {
        return oldDepositIntRate;
    }

    /**
     * Setter for property oldDepositIntRate.
     *
     * @param oldDepositIntRate New value of property oldDepositIntRate.
     */
    public void setOldDepositIntRate(double oldDepositIntRate) {
        this.oldDepositIntRate = oldDepositIntRate;
    }

    /**
     * Getter for property depositAvilableAmt.
     *
     * @return Value of property depositAvilableAmt.
     */
    public java.lang.String getDepositAvilableAmt() {
        return depositAvilableAmt;
    }

    /**
     * Setter for property depositAvilableAmt.
     *
     * @param depositAvilableAmt New value of property depositAvilableAmt.
     */
    public void setDepositAvilableAmt(java.lang.String depositAvilableAmt) {
        this.depositAvilableAmt = depositAvilableAmt;
    }

    /**
     * Getter for property dep_mat_dt.
     *
     * @return Value of property dep_mat_dt.
     */
    public java.lang.String getDep_mat_dt() {
        return dep_mat_dt;
    }

    /**
     * Setter for property dep_mat_dt.
     *
     * @param dep_mat_dt New value of property dep_mat_dt.
     */
    public void setDep_mat_dt(java.lang.String dep_mat_dt) {
        this.dep_mat_dt = dep_mat_dt;
    }

    /**
     * Getter for property dep_mat_amt.
     *
     * @return Value of property dep_mat_amt.
     */
    public java.lang.String getDep_mat_amt() {
        return dep_mat_amt;
    }

    /**
     * Setter for property dep_mat_amt.
     *
     * @param dep_mat_amt New value of property dep_mat_amt.
     */
    public void setDep_mat_amt(java.lang.String dep_mat_amt) {
        this.dep_mat_amt = dep_mat_amt;
    }

    /**
     * Getter for property dep_credit_int.
     *
     * @return Value of property dep_credit_int.
     */
    public java.lang.String getDep_credit_int() {
        return dep_credit_int;
    }

    /**
     * Setter for property dep_credit_int.
     *
     * @param dep_credit_int New value of property dep_credit_int.
     */
    public void setDep_credit_int(java.lang.String dep_credit_int) {
        this.dep_credit_int = dep_credit_int;
    }

    /**
     * Getter for property dep_rate_int.
     *
     * @return Value of property dep_rate_int.
     */
    public java.lang.String getDep_rate_int() {
        return dep_rate_int;
    }

    /**
     * Setter for property dep_rate_int.
     *
     * @param dep_rate_int New value of property dep_rate_int.
     */
    public void setDep_rate_int(java.lang.String dep_rate_int) {
        this.dep_rate_int = dep_rate_int;
    }

    /**
     * Getter for property deposit_amt.
     *
     * @return Value of property deposit_amt.
     */
    public java.lang.String getDeposit_amt() {
        return deposit_amt;
    }

    /**
     * Setter for property deposit_amt.
     *
     * @param deposit_amt New value of property deposit_amt.
     */
    public void setDeposit_amt(java.lang.String deposit_amt) {
        this.deposit_amt = deposit_amt;
    }

    /**
     * Getter for property isMobileBanking.
     *
     * @return Value of property isMobileBanking.
     */
    public boolean getIsMobileBanking() {
        return isMobileBanking;
    }

    /**
     * Setter for property isMobileBanking.
     *
     * @param isMobileBanking New value of property isMobileBanking.
     */
    public void setIsMobileBanking(boolean isMobileBanking) {
        this.isMobileBanking = isMobileBanking;
    }

    /**
     * Getter for property txtMobileNo.
     *
     * @return Value of property txtMobileNo.
     */
    public java.lang.String getTxtMobileNo() {
        return txtMobileNo;
    }

    /**
     * Setter for property txtMobileNo.
     *
     * @param txtMobileNo New value of property txtMobileNo.
     */
    public void setTxtMobileNo(java.lang.String txtMobileNo) {
        this.txtMobileNo = txtMobileNo;
    }

    /**
     * Getter for property tdtMobileSubscribedFrom.
     *
     * @return Value of property tdtMobileSubscribedFrom.
     */
    public java.lang.String getTdtMobileSubscribedFrom() {
        return tdtMobileSubscribedFrom;
    }

    /**
     * Setter for property tdtMobileSubscribedFrom.
     *
     * @param tdtMobileSubscribedFrom New value of property
     * tdtMobileSubscribedFrom.
     */
    public void setTdtMobileSubscribedFrom(java.lang.String tdtMobileSubscribedFrom) {
        this.tdtMobileSubscribedFrom = tdtMobileSubscribedFrom;
    }

    /**
     * Getter for property productAuthRemarks.
     *
     * @return Value of property productAuthRemarks.
     */
    public java.lang.String getProductAuthRemarks() {
        return productAuthRemarks;
    }

    /**
     * Setter for property productAuthRemarks.
     *
     * @param productAuthRemarks New value of property productAuthRemarks.
     */
    public void setProductAuthRemarks(java.lang.String productAuthRemarks) {
        this.productAuthRemarks = productAuthRemarks;
    }

    /**
     * Getter for property keyValueForPaddyAndMDSLoans.
     *
     * @return Value of property keyValueForPaddyAndMDSLoans.
     */
    public java.lang.String getKeyValueForPaddyAndMDSLoans() {
        return keyValueForPaddyAndMDSLoans;
    }

    /**
     * Setter for property keyValueForPaddyAndMDSLoans.
     *
     * @param keyValueForPaddyAndMDSLoans New value of property
     * keyValueForPaddyAndMDSLoans.
     */
    public void setKeyValueForPaddyAndMDSLoans(java.lang.String keyValueForPaddyAndMDSLoans) {
        this.keyValueForPaddyAndMDSLoans = keyValueForPaddyAndMDSLoans;
    }

    /**
     * Getter for property paddyMap.
     *
     * @return Value of property paddyMap.
     */
    public java.util.Map getPaddyMap() {
        return paddyMap;
    }

    /**
     * Setter for property paddyMap.
     *
     * @param paddyMap New value of property paddyMap.
     */
    public void setPaddyMap(java.util.Map paddyMap) {
        this.paddyMap = paddyMap;
    }

    /**
     * Getter for property mdsMap.
     *
     * @return Value of property mdsMap.
     */
    public java.util.Map getMdsMap() {
        return mdsMap;
    }

    /**
     * Setter for property mdsMap.
     *
     * @param mdsMap New value of property mdsMap.
     */
    public void setMdsMap(java.util.Map mdsMap) {
        this.mdsMap = mdsMap;
    }

    /**
     * Getter for property depositInterest.
     *
     * @return Value of property depositInterest.
     */
    public double getDepositInterest() {
        return depositInterest;
    }

    /**
     * Setter for property depositInterest.
     *
     * @param depositInterest New value of property depositInterest.
     */
    public void setDepositInterest(double depositInterest) {
        this.depositInterest = depositInterest;
    }

    public String getLblEligibileAmtValue() {
        return lblEligibileAmtValue;
    }

    public void setLblEligibileAmtValue(String lblEligibileAmtValue) {
        this.lblEligibileAmtValue = lblEligibileAmtValue;
    }

    public List getChargelst() {
        return chargelst;
    }

    public void setChargelst(List chargelst) {
        this.chargelst = chargelst;
    }

    public String getChkDepositLien() {
        return chkDepositLien;
    }

    public void setChkDepositLien(String chkDepositLien) {
        this.chkDepositLien = chkDepositLien;
    }

    public String getMembNo() {
        return membNo;
    }

    public void setMembNo(String membNo) {
        this.membNo = membNo;
    }

    public String getMembType() {
        return membType;
    }

    public void setMembType(String membType) {
        this.membType = membType;
    }

    public String getMembName() {
        return membName;
    }

    public void setMembName(String membName) {
        this.membName = membName;
    }

    public String getChittAmt() {
        return chittAmt;
    }

    public void setChittAmt(String chittAmt) {
        this.chittAmt = chittAmt;
    }

    public EnhancedTableModel getTblSanctionMainMds() {
        return tblSanctionMainMds;
    }

    public void setTblSanctionMainMds(EnhancedTableModel tblSanctionMainMds) {
        this.tblSanctionMainMds = tblSanctionMainMds;
    }

    public String getChittalNoMds() {
        return chittalNoMds;
    }

    public void setChittalNoMds(String chittalNoMds) {
        this.chittalNoMds = chittalNoMds;
    }

    public boolean isRdoMultiDisburseAllow_No() {
        return rdoMultiDisburseAllow_No;
    }

    public void setRdoMultiDisburseAllow_No(boolean rdoMultiDisburseAllow_No) {
        this.rdoMultiDisburseAllow_No = rdoMultiDisburseAllow_No;
    }

    public boolean isRdoMultiDisburseAllow_Yes() {
        return rdoMultiDisburseAllow_Yes;
    }

    public void setRdoMultiDisburseAllow_Yes(boolean rdoMultiDisburseAllow_Yes) {
        this.rdoMultiDisburseAllow_Yes = rdoMultiDisburseAllow_Yes;
    }
    
    public long getMobileNo(String custId) {
        long mobileNo = 0;
        HashMap mobileMap = new HashMap();
        mobileMap.put("CUST_ID",custId);
        mobileMap.put("BRANCH_CODE",ProxyParameters.BRANCH_ID);
        List list = ClientUtil.executeQuery("getSMSContactForDepositMaturedCustomer", mobileMap);
        if (list != null && list.size() > 0) {
            mobileMap = (HashMap)list.get(0);
            mobileNo = CommonUtil.convertObjToLong(mobileMap.get("CONTACT_NO"));
        }
        return mobileNo;
    }

    public String getLblServiceTaxval() {
        return lblServiceTaxval;
    }

    public HashMap getServiceTax_Map() {
        return serviceTax_Map;
    }

    public void setServiceTax_Map(HashMap serviceTax_Map) {
        this.serviceTax_Map = serviceTax_Map;
    }

    public void setLblServiceTaxval(String lblServiceTaxval) {
        this.lblServiceTaxval = lblServiceTaxval;
    }  
    
    public ServiceTaxDetailsTO setServiceTaxDetails() {
        final ServiceTaxDetailsTO objservicetaxDetTo = new ServiceTaxDetailsTO();
        try {
            objservicetaxDetTo.setCommand(getCommand());
            objservicetaxDetTo.setStatus(CommonConstants.STATUS_CREATED);
            objservicetaxDetTo.setStatusBy(TrueTransactMain.USER_ID);
            objservicetaxDetTo.setAcct_Num(getBorrowerNo());
            objservicetaxDetTo.setParticulars("Term Loans/Advance Accounts");
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
            if (serviceTax_Map != null && serviceTax_Map.containsKey("TOT_TAX_AMT")) {
                objservicetaxDetTo.setTotalTaxAmt(CommonUtil.convertObjToDouble(serviceTax_Map.get("TOT_TAX_AMT")));
            }
            //added by rishad 10/04/2017 
            if (serviceTax_Map != null && serviceTax_Map.containsKey(ServiceTaxCalculation.SWACHH_CESS)) {
                objservicetaxDetTo.setSwachhCess(CommonUtil.convertObjToDouble(serviceTax_Map.get(ServiceTaxCalculation.SWACHH_CESS)));
            }
            if (serviceTax_Map != null && serviceTax_Map.containsKey(ServiceTaxCalculation.KRISHIKALYAN_CESS)) {
                objservicetaxDetTo.setKrishiKalyan(CommonUtil.convertObjToDouble(serviceTax_Map.get(ServiceTaxCalculation.KRISHIKALYAN_CESS)));
            }
            double roudVal = objservicetaxDetTo.getTotalTaxAmt() - (objservicetaxDetTo.getServiceTaxAmt() + objservicetaxDetTo.getEducationCess() + objservicetaxDetTo.getHigherCess() + objservicetaxDetTo.getSwachhCess() + objservicetaxDetTo.getKrishiKalyan());
            ServiceTaxCalculation serviceTaxObj = new ServiceTaxCalculation();
            objservicetaxDetTo.setRoundVal(CommonUtil.convertObjToStr(serviceTaxObj.roundOffAmtForRoundVal(roudVal)));
            objservicetaxDetTo.setStatusDt(ClientUtil.getCurrentDate());

            if (getCommand().equalsIgnoreCase("INSERT")) {
                objservicetaxDetTo.setCreatedBy(TrueTransactMain.USER_ID);
                objservicetaxDetTo.setCreatedDt(ClientUtil.getCurrentDate());
            }
            
        } catch (Exception e) {
            log.info("Error In setLoanApplicationData()");
            e.printStackTrace();
        }
        return objservicetaxDetTo;

    }

    public HashMap getJointCustMap() {
        return jointCustMap;
    }

    public void setJointCustMap(HashMap jointCustMap) {
        this.jointCustMap = jointCustMap;
    }
    
    public void setJointCustData(ArrayList jointCustList) { // Added by nithya on 24-09-2020 for KD-2275
        HashMap jointAcctMap = new HashMap();
        jointCustMap = new HashMap();
        TermLoanJointAcctTO objTermLoanJointAcctTO;
        try {
            System.out.println("jointCustList :: " + jointCustList);
            if (jointCustList != null && jointCustList.size() > 0) {
                for (int i = 0; i < jointCustList.size(); i++) {
                    objTermLoanJointAcctTO = new TermLoanJointAcctTO();
                    objTermLoanJointAcctTO.setBorrowNo("");
                    objTermLoanJointAcctTO.setCommand("CREATED");
                    objTermLoanJointAcctTO.setCustId(CommonUtil.convertObjToStr(jointCustList.get(i)));
                    objTermLoanJointAcctTO.setStatus("CREATED");
                    objTermLoanJointAcctTO.setStatusBy(TrueTransactMain.USER_ID);
                    objTermLoanJointAcctTO.setStatusDt(ClientUtil.getCurrentDate());
                    jointAcctMap.put(String.valueOf(jointAcctMap.size() + 1), objTermLoanJointAcctTO);
                    objTermLoanJointAcctTO = null;
                }
            }
            setJointCustMap(jointAcctMap);            
        } catch (Exception e) {
            log.info("Exception caught in setTermLoanJointAcct: ");
            parseException.logException(e, true);
        }        
    }

    public String getLoanDisbursalAmt() {
        return loanDisbursalAmt;
    }

    public void setLoanDisbursalAmt(String loanDisbursalAmt) {
        this.loanDisbursalAmt = loanDisbursalAmt;
    }
    
}
