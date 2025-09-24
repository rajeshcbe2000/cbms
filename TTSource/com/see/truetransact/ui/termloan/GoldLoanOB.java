/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition.
 * 
 * TermLoanOB.java
 *
 * Created on January 8, 2004, 4:24 PM
 */
package com.see.truetransact.ui.termloan;

//import com.jidesoft.utils.SortedList;
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
import com.see.truetransact.clientutil.*;
import com.see.truetransact.commonutil.CommonConstants;
import com.see.truetransact.commonutil.DateUtil;
import com.see.truetransact.commonutil.CommonUtil;
//import com.see.truetransact.commonutil.TTException;
import com.see.truetransact.transferobject.termloan.TermLoanJointAcctTO;
import com.see.truetransact.transferobject.termloan.TermLoanBorrowerTO;
import com.see.truetransact.transferobject.termloan.TermLoanClassificationTO;
import com.see.truetransact.transferobject.termloan.GoldLoanSecurityTO;
//import com.see.truetransact.transferobject.termloan.TermLoanCompanyTO;
import com.see.truetransact.transferobject.termloan.TermLoanFacilityTO;
//import com.see.truetransact.transferobject.termloan.TermLoanOtherDetailsTO;
import com.see.truetransact.transferobject.termloan.TermLoanSanctionFacilityTO;
import com.see.truetransact.transferobject.termloan.TermLoanSanctionTO;

import com.see.truetransact.transferobject.termloan.TermLoanExtenFacilityDetailsTO;

import com.see.truetransact.ui.termloan.loandisbursement.LoanDisbursementOB;
//import com.see.truetransact.ui.termloan.agrisubsidydetails.AgriSubSidyOB;
//import com.see.truetransact.ui.termloan.settlement.SettlementOB;
//import com.see.truetransact.ui.termloan.accounttransfer.ActTransOB;

import com.see.truetransact.transferobject.common.mobile.SMSSubscriptionTO;
import com.see.truetransact.ui.TrueTransactMain;
import com.see.truetransact.uicomponent.CObservable;
import com.see.truetransact.uicomponent.COptionPane;
import com.see.truetransact.ui.deposit.lien.DepositLienUI;
import com.see.truetransact.uicomponent.CTable;

import com.see.truetransact.transferobject.termloan.TermLoanInterestTO;
import com.see.truetransact.transferobject.termloan.TermLoanRepaymentTO;
import com.see.truetransact.transferobject.common.mobile.SMSSubscriptionTO;
import com.see.truetransact.transferobject.product.loan.LoanProductAccHeadTO;
import com.see.truetransact.transferobject.servicetax.servicetaxdetails.ServiceTaxDetailsTO;
import com.see.truetransact.ui.common.servicetax.ServiceTaxCalculation;
import java.util.*;

import org.apache.log4j.Logger;


/**
 *
 * @author shanmugavel Created on January 8, 2004, 4:24 PM
 *
 */
public class GoldLoanOB extends CObservable {

    private int actionType;
    private int resultStatus;
    private String lblStatus = ClientConstants.ACTION_STATUS[ClientConstants.ACTIONTYPE_CANCEL];
    //    private       static AuthorizedSignatoryOB termLoanAuthorizedSignatoryOB;
    //    private       static AuthorizedSignatoryInstructionOB termLoanAuthorizedSignatoryInstructionOB;
    //    private       static AgriSubSidyOB  agriSubSidyOB;
    //    private       static SettlementOB   settlementOB;
    //    private       static ActTransOB actTransOB;
    //    private       static PowerOfAttorneyOB termLoanPoAOB;
    NomineeOB nomineeOB;
    private static GoldLoanOB termLoanOB;
    private GoldLoanBorrowerOB termLoanBorrowerOB;
//    private       TermLoanCompanyOB termLoanCompanyOB;
    private GoldLoanSecurityOB termLoanSecurityOB;
    private GoldLoanRepaymentOB termLoanRepaymentOB;
//    private       TermLoanGuarantorOB termLoanGuarantorOB;
    private TermLoanDocumentDetailsOB termLoanDocumentDetailsOB;
    private GoldLoanInterestOB termLoanInterestOB;
    private GoldLoanClassificationOB termLoanClassificationOB;
//    private       TermLoanOtherDetailsOB termLoanOtherDetailsOB;
//    private       TermLoanAdditionalSanctionOB  termLoanAdditionalSanctionOB;//bala
    private static LoanDisbursementOB agriSubLimitOB;
    private final static Logger log = Logger.getLogger(GoldLoanOB.class);
    private final static ClientParseException parseException = ClientParseException.getInstance();
    private final String ACC_LIMIT = "ACC_LIMIT";
    private final String ACC_TYPE = "ACC_TYPE";
    private final String ACCT_NAME = "ACCT_NAME";
    private final String ACCT_NUM = "ACCT_NUM";
    private final String RENEWAL_ACCT_NUM = "RENEWAL_ACCT_NUM";
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
    private final ArrayList shareTitle = new ArrayList();
    //    private final   TermLoanRB objTermLoanRB = new TermLoanRB();
    private final java.util.ResourceBundle objTermLoanRB = java.util.ResourceBundle.getBundle("com.see.truetransact.ui.termloan.GoldLoanRB", ProxyParameters.LANGUAGE);
    private EnhancedTableModel tblSanctionFacility;
    private EnhancedTableModel tblSanctionMain;
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
    private LinkedHashMap objRepaymentInstallmentAllMap = new LinkedHashMap();
    private HashMap installmentAllMap = new HashMap();
    private LinkedHashMap sanctionFacilityAll = new LinkedHashMap();   // Both displayed and hidden values in the table
    private LinkedHashMap sanctionMainAll = new LinkedHashMap();       // Both displayed and hidden values in the table
    private LinkedHashMap facilityAll = new LinkedHashMap();           // Both displayed and hidden values in the table
    private HashMap removedFaccilityTabValues = new HashMap();
    private HashMap sanctionFacilityRecord;
    private HashMap sanctionMainRecord;
    private HashMap facilityRecord;
    private HashMap renewalFacilityRecord;
    private HashMap lookUpHash;
    private HashMap operationMap;
    private HashMap keyValue;
    private HashMap authorizeMap;
    private HashMap transactionMap;
    private HashMap renewalParamMap;
    private ComboBoxModel cbmAccStatus;
    private ComboBoxModel cbmSanctioningAuthority;
    private ComboBoxModel cbmModeSanction;
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
    private ComboBoxModel cbmRenewalSanctioningAuthority;
    private ComboBoxModel cbmRenewalPurposeOfLoan;
    private ComboBoxModel cbmRenewalAccStatus;
    private ComboBoxModel cbmRenewalRepayFreq;
    private ComboBoxModel cbmRepayType;
    private ComboBoxModel cbmRenewalPurityOfGold;
    private ComboBoxModel cbmRenewalAppraiserId;
    private boolean chkDocDetails = false;
    private boolean chkAuthorizedSignatory = false;
    private boolean chkPOFAttorney = false;
    private TableUtil tableUtilSanction_Facility = new TableUtil();
    private TableUtil tableUtilSanction_Main = new TableUtil();
    private String cboIntGetFrom = "";
    private String txtSanctionNo = "";
    private String txtAcct_Name = "";
    private String tdtSanctionDate = "";
    private String cboAccStatus = "";
    private String cboSanctioningAuthority = "";
    private String txtSanctionRemarks = "";
    private String cboModeSanction = "";
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
    private String tdtRenewalToDate = "";
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
    private String renewalBorrowerNo = "";
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
    private String minDecLoanPeriod = "0";
    private String maxDecLoanPeriod = "0";
    private String interestMode = CommonConstants.TOSTATUS_INSERT;     // Mode of Interest Details
    private String classifiMode = CommonConstants.TOSTATUS_INSERT;     // Mode of Classification Details
    private String strACNumber = "";                                  // Current Account Number in Edit/Delete/Authorize Mode
    private String loanType = "";
    private HashMap depositCustDetMap;
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
    private double slNoForSanction = 0;
    //interest calculation values storing
    private String interest = "";
    private String penalInterest = "";
    private String cboPurposeOfLoan = "";
    private ComboBoxModel cbmPurposeOfLoan;
    private String securityAccHeadValue = "";
    private String loadingProdId = "";
     private String rnwLoadingProdId = "";
    private String newCustIdNo = "";
    private Date last_int_calc_dt = null;
    private String authorizeDt = "";
    private String txtNomineeNameNO = "";
    private String cboNomineeRelationNO = "";
    private String txtNomineePhoneNO = "";
    private String txtNomineeACodeNO = "";
    private String txtNomineeShareNO = "";
    private ComboBoxModel cbmNomineeRelationNO;
    private ComboBoxModel cbmGoldLoanProd;
     private ComboBoxModel cbmRnwGoldLoanProd;
    private String txtRenewalAppId="";
    private TableModel _tableModel;
    private CTable _tblData;
    private ArrayList _heading;
    private HashMap dataHash;
    private ArrayList data;
    private int dataSize;
    public final int MAXDATA = 1000;
    private boolean _isAvailable = true;
    private boolean _clearFlag = true;
    private LinkedHashMap goldItemMap = new LinkedHashMap();
    private HashMap serviceTax_Map=null;// Added by nithya on 09-10-2018 for KD 263 GST - Needed to implement the GST in GOld Loan renewal
    private String lblServiceTaxval="";// KD 263
    private int maximumDaysForLoan = 0;
    private int maximumDaysForRenewLoan = 0;
    private String photoFile;
    private byte[] photoByteArray;  // Added by nithya on 29-10-2019 for KD-763	Need Gold ornaments photo saving option
    private byte[] renewalPhotoByteArray; // Added by nithya on 29-10-2019 for KD-763	Need Gold ornaments photo saving option

    public String getTxtRenewalAppId() {
        return txtRenewalAppId;
    }

    public void setTxtRenewalAppId(String txtRenewalAppId) {
        this.txtRenewalAppId = txtRenewalAppId;
    }

    public ComboBoxModel getCbmGoldLoanProd() {
        return cbmGoldLoanProd;
    }

    public void setCbmGoldLoanProd(ComboBoxModel cbmGoldLoanProd) {
        this.cbmGoldLoanProd = cbmGoldLoanProd;
    }

    public ComboBoxModel getCbmRnwGoldLoanProd() {
        return cbmRnwGoldLoanProd;
    }

    public void setCbmRnwGoldLoanProd(ComboBoxModel cbmRnwGoldLoanProd) {
        this.cbmRnwGoldLoanProd = cbmRnwGoldLoanProd;
    }

    public String getRnwLoadingProdId() {
        return rnwLoadingProdId;
    }
    public void setRnwLoadingProId(String rnwLoadingProdId) {
        this.rnwLoadingProdId = rnwLoadingProdId;
    }
    
    private ArrayList nomineeTOList;
    //mobile banking
    private boolean isMobileBanking = false;
    SMSSubscriptionTO objSMSSubscriptionTO = null;
    private String txtMobileNo = "";
    private String tdtMobileSubscribedFrom = "";
    public GoldLoanSecurityTO objTermLoanSecurityTO = null;
    private List chargelst = null;
    private String renewalAcctNum = "";
    private boolean renewalYesNo = false;
    private String asAnWhenCustomer = "";
    //renewal gold loan
    private String lblRenewalAcctNo_Sanction_Disp = "";
    private String tdtRenewalSanctionDate = "";
    private String cboRenewalSanctioningAuthority = "";
    private boolean rdoRenewalPriority = false;
    private boolean rdoRenewalNonPriority = false;
    private String cboRenewalPurposeOfLoan = "";
    private String txtRenewalSanctionRemarks = "";
    private String txtRenewalLimit_SD = "";
    private String tdtRenewalAccountOpenDate = "";
    private String cboRenewalAccStatus = "";
    private String txtRenewalNoInstallments = "";
    private String cboRenewalRepayFreq = "";
    private String tdtRenewalDemandPromNoteExpDate = "";
    private String tdtRenewalDemandPromNoteDate = "";
    private String txtRenewalInter = "";
    private String txtRenewalPenalInter = "";
    private HashMap allLoanAmount = new HashMap();
    private double totRecivableAmt = 0;
    private double charges=0;
//private String cboRenewalSanctioningAuthority="";
//private String cboRenewalAccStatus="";
//private String cboRenewalRepayFreq="";
//private String cboRenewalPurityOfGold="";
//private String cboRenewalAppraiserId="";
    public double getCharges() {
        return charges;
    }
    public void setCharges(double charges) {
        this.charges = charges;
    }

//    static {
//        try {
//            termLoanOB = new GoldLoanOB();
//        } catch(Exception e) {
//            log.info("try: " + e);
//            parseException.logException(e,true);
//            e.printStackTrace();
//        }
//    }
    public GoldLoanOB() {
        try {
            setOperationMap();
            curDate = ClientUtil.getCurrentDate();
            proxy = ProxyFactory.createProxy();
            termLoanOB();
        } catch (Exception e) {
            parseException.logException(e, true);
            e.printStackTrace();
            log.info("GoldLoanOB..." + e);
        }
    }

    /**
     * Returns an instance of TermLoanOB
     *
     * @return TermLoanOB
     */
//    public static GoldLoanOB getInstance() {
//        return termLoanOB;
//    }
//    /** Creates a new instance of TermLoanOB */
//    public GoldLoanOB() throws Exception {
//            setOperationMap();
//            try {
//                curDate = ClientUtil.getCurrentDate();
//                proxy = ProxyFactory.createProxy();
//            } catch (Exception e) {
//                parseException.logException(e,true);
//                e.printStackTrace();
//                log.info("TermLoanOB..."+e);
//            }
//            termLoanOB();
//        }
    private void termLoanOB() throws Exception {
        //        termLoanBorrowerOB = GoldLoanBorrowerOB.getInstance();    // Changed By Suresh
        //        termLoanCompanyOB = TermLoanCompanyOB.getInstance();
        //        termLoanGuarantorOB = TermLoanGuarantorOB.getInstance();
        //        termLoanSecurityOB = GoldLoanSecurityOB.getInstance();
        termLoanDocumentDetailsOB = TermLoanDocumentDetailsOB.getInstance();
        //        termLoanInterestOB = GoldLoanInterestOB.getInstance();
        //        termLoanClassificationOB = GoldLoanClassificationOB.getInstance();
        //        termLoanOtherDetailsOB = TermLoanOtherDetailsOB.getInstance();
        //        termLoanAdditionalSanctionOB= TermLoanAdditionalSanctionOB.getInstance();
        termLoanBorrowerOB = new GoldLoanBorrowerOB();
//        termLoanCompanyOB = new  TermLoanCompanyOB();
        termLoanSecurityOB = new GoldLoanSecurityOB();
        termLoanRepaymentOB = new GoldLoanRepaymentOB();
//        termLoanGuarantorOB = new TermLoanGuarantorOB();
//        termLoanDocumentDetailsOB = new TermLoanDocumentDetailsOB();
        termLoanInterestOB = new GoldLoanInterestOB();
        termLoanClassificationOB = new GoldLoanClassificationOB();
//        termLoanOtherDetailsOB = new TermLoanOtherDetailsOB();
//        termLoanAdditionalSanctionOB= new TermLoanAdditionalSanctionOB();
        fillDropdown();
        setSanctionFacilityTitle();
        setSanctionMainTitle();
        setShareTitle();
        tableUtilSanction_Facility.setAttributeKey(SLNO);
        tableUtilSanction_Main.setAttributeKey(SANCTION_SL_NO);
        tblSanctionFacility = new EnhancedTableModel(null, sanctionFacilityTitle);
        tblSanctionMain = new EnhancedTableModel(null, sanctionMainTitle);
        tblShare = new EnhancedTableModel(null, shareTitle);
        notifyObservers();
    }

    //    public void setExtendedOB(AuthorizedSignatoryOB authOB, AuthorizedSignatoryInstructionOB authInstOB, PowerOfAttorneyOB poaOB,
    //    LoanDisbursementOB agriSUBLIMITOB){//, AgriSubSidyOB ag ,SettlementOB settmntOB ,ActTransOB actTransOB){
    //        termLoanAuthorizedSignatoryOB = authOB;
    //        termLoanAuthorizedSignatoryInstructionOB = authInstOB;
    //        termLoanPoAOB = poaOB;
    //        agriSubLimitOB=agriSUBLIMITOB;
    ////        agriSubSidyOB=ag;
    ////        settlementOB=settmntOB;
    ////        this.actTransOB=actTransOB;
    //    }
    private void setSanctionFacilityTitle() throws Exception {
        try {
            sanctionFacilityTitle.add(objTermLoanRB.getString("tblColumnSanction_Facility1"));
            sanctionFacilityTitle.add(objTermLoanRB.getString("tblColumnSanction_Facility2"));
            sanctionFacilityTitle.add(objTermLoanRB.getString("tblColumnSanction_Facility3"));
            sanctionFacilityTitle.add(objTermLoanRB.getString("tblColumnSanction_Facility4"));
            sanctionFacilityTitle.add(objTermLoanRB.getString("tblColumnSanction_Facility5"));
        } catch (Exception e) {
            log.info("Exception in setSanctionFacilityTitle: " + e);
            parseException.logException(e, true);
        }
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
        } catch (Exception e) {
            log.info("Exception in setSanctionMainTitle: " + e);
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
        lookup_keys.add("TERMLOAN.GOLDITEM");
        lookup_keys.add("PURPOSE_OF_LOAN");
        lookup_keys.add("RELATIONSHIP");

        lookUpHash.put(CommonConstants.PARAMFORQUERY, lookup_keys);

        keyValue = ClientUtil.populateLookupData(lookUpHash);

        getKeyValue((HashMap) keyValue.get("CATEGORY"));
        termLoanBorrowerOB.setCbmCategory(new ComboBoxModel(key, value));

        getKeyValue((HashMap) keyValue.get("CONSTITUTION"));
        termLoanBorrowerOB.setCbmConstitution(new ComboBoxModel(key, value));

//        getKeyValue((HashMap)keyValue.get("CUSTOMER.ADDRTYPE"));
//        termLoanCompanyOB.setCbmAddressType(new ComboBoxModel(key,value));
//        
//        getKeyValue((HashMap)keyValue.get("CUSTOMER.CITY"));
//        termLoanCompanyOB.setCbmCity_CompDetail(new ComboBoxModel(key,value));
//        
//        getKeyValue((HashMap)keyValue.get("CUSTOMER.STATE"));
//        termLoanCompanyOB.setCbmState_CompDetail(new ComboBoxModel(key,value));
//        
//        getKeyValue((HashMap)keyValue.get("CUSTOMER.COUNTRY"));
//        termLoanCompanyOB.setCbmCountry_CompDetail(new ComboBoxModel(key,value));

        getKeyValue((HashMap) keyValue.get("TERM_LOAN.BUSINESS_NATURE"));
        ArrayList tempKey = key;
        ArrayList tempVal = value;
        getKeyValue((HashMap) keyValue.get("CUSTOMER.PRIMARYOCCUPATION"));
        for (int i = key.size() - 1, j = 0; i >= 0; --i, ++j) {
            if (!key.get(j).equals("")) {
                tempKey.add(key.get(j));
            }
        }
        for (int i = value.size() - 1, j = 0; i >= 0; --i, ++j) {
            if (!value.get(j).equals("")) {
                tempVal.add(value.get(j));
            }
        }
//        termLoanCompanyOB.setCbmNatureBusiness(new ComboBoxModel(tempKey, tempVal));

        getKeyValue((HashMap) keyValue.get("LOAN.ACCOUNT_STATUS"));
        setCbmAccStatus(new ComboBoxModel(key, value));
        setCbmRenewalAccStatus(new ComboBoxModel(key, value));

        getKeyValue((HashMap) keyValue.get("LOAN.INT_GET_FROM"));
        cbmIntGetFrom = new ComboBoxModel(key, value);

        getKeyValue((HashMap) keyValue.get("CUSTOMER.CITY"));
        cbmSanctioningAuthority = new ComboBoxModel(key, value);

        getKeyValue((HashMap) keyValue.get("TERM_LOAN.SANCTIONING_AUTHORITY"));
        cbmSanctioningAuthority = new ComboBoxModel(key, value);

        getKeyValue((HashMap) keyValue.get("TERM_LOAN.SANCTIONING_AUTHORITY"));
        cbmRenewalSanctioningAuthority = new ComboBoxModel(key, value);


        getKeyValue((HashMap) keyValue.get("TERM_LOAN.SANCTION_MODE"));
        cbmModeSanction = new ComboBoxModel(key, value);

        getKeyValue((HashMap) keyValue.get("LOAN.FREQUENCY"));
        cbmRepayFreq = new ComboBoxModel(key, value);

        cbmRenewalRepayFreq = new ComboBoxModel(key, value);
        getKeyValue((HashMap) keyValue.get("RELATIONSHIP"));
        cbmNomineeRelationNO = new ComboBoxModel(key, value);

        getKeyValue((HashMap) keyValue.get("LOAN.FREQUENCY"));
        setCbmRepayFreq_LOAN(new ComboBoxModel(key, value));
        
        getKeyValue((HashMap)keyValue.get("TERM_LOAN.REPAYMENT_TYPE"));
        termLoanRepaymentOB.setCbmRepayType(new ComboBoxModel(key,value));

        getKeyValue((HashMap) keyValue.get("FREQUENCY"));
        for (int i = key.size() - 1; i >= 0; --i) {
            if (key.get(i).equals("180") || key.get(i).equals("90") || key.get(i).equals("7") || key.get(i).equals("1")) {
                key.remove(i);
            }
        }
        for (int i = value.size() - 1; i >= 0; --i) {
            if (value.get(i).equals("Half Yearly") || value.get(i).equals("Quaterly") || value.get(i).equals("Weekly") || value.get(i).equals("Daily")) {
                value.remove(i);
            }
        }
        setCbmRepayFreq_ADVANCE(new ComboBoxModel(key, value));

        getKeyValue((HashMap) keyValue.get("LOANPRODUCT.OPERATESLIKE"));
        cbmTypeOfFacility = new ComboBoxModel(key, value);
        //        if(loanType.equals("OTHERS"))
        //            cbmTypeOfFacility.removeElement("Loans Against Deposits");
//        //System.out.println("loanType###"+loanType);
//        getKeyValue((HashMap)keyValue.get("CONSTITUTION"));
//        termLoanGuarantorOB.setCbmConstitution_GD(new ComboBoxModel(key,value));
//        
//        getKeyValue((HashMap)keyValue.get("CUSTOMER.CITY"));
//        termLoanGuarantorOB.setCbmCity_GD(new ComboBoxModel(key,value));
//        
//        getKeyValue((HashMap)keyValue.get("CUSTOMER.STATE"));
//        termLoanGuarantorOB.setCbmState_GD(new ComboBoxModel(key,value));
//        
//        getKeyValue((HashMap)keyValue.get("CUSTOMER.COUNTRY"));
//        termLoanGuarantorOB.setCbmCountry_GD(new ComboBoxModel(key,value));
//        
//        getKeyValue((HashMap)keyValue.get("PRODUCTTYPE"));
//        termLoanGuarantorOB.setCbmProdType(new ComboBoxModel(key,value));
//        
//        getKeyValue((HashMap)keyValue.get("TERM_LOAN.GUARANTEE_STATUS"));
//        termLoanGuarantorOB.setCbmGuaranStatus(new ComboBoxModel(key,value));

        getKeyValue((HashMap) keyValue.get("TERMLOAN.INTERESTTYPE"));
        cbmInterestType = new ComboBoxModel(key, value);
        getKeyValue((HashMap) keyValue.get("BOARDDIRECTORS"));
        cbmRecommendedByType = new ComboBoxModel(key, value);

        getKeyValue((HashMap) keyValue.get("LOAN.FREQUENCY"));
        termLoanRepaymentOB.setCbmRepayFreq_Repayment(new ComboBoxModel(key, value));

        getKeyValue((HashMap) keyValue.get("TERM_LOAN.REPAYMENT_TYPE"));
        termLoanRepaymentOB.setCbmRepayType(new ComboBoxModel(key, value));

        getKeyValue((HashMap) keyValue.get("TERM_LOAN.COMMODITY_CODE"));
        termLoanClassificationOB.setCbmCommodityCode(new ComboBoxModel(key, value));

        getKeyValue((HashMap) keyValue.get("TERM_LOAN.SECTOR_CODE"));
        termLoanClassificationOB.setCbmSectorCode1(new ComboBoxModel(key, value));

        getKeyValue((HashMap) keyValue.get("TERM_LOAN.INDUSTRY_CODE"));
        termLoanClassificationOB.setCbmIndusCode(new ComboBoxModel(key, value));

        getKeyValue((HashMap) keyValue.get("TERM_LOAN.20_CODE"));
        termLoanClassificationOB.setCbm20Code(new ComboBoxModel(key, value));

        getKeyValue((HashMap) keyValue.get("TERM_LOAN.GOVT_SCHEME_CODE"));
        termLoanClassificationOB.setCbmGovtSchemeCode(new ComboBoxModel(key, value));

        getKeyValue((HashMap) keyValue.get("TERM_LOAN.GUARANTEE_COVER_CODE"));
        termLoanClassificationOB.setCbmGuaranteeCoverCode(new ComboBoxModel(key, value));

        getKeyValue((HashMap) keyValue.get("TERM_LOAN.HEALTH_CODE"));
        termLoanClassificationOB.setCbmHealthCode(new ComboBoxModel(key, value));

        getKeyValue((HashMap) keyValue.get("TERM_LOAN.DISTRICT_CODE"));
        termLoanClassificationOB.setCbmDistrictCode(new ComboBoxModel(key, value));

        getKeyValue((HashMap) keyValue.get("TERM_LOAN.WEAKERSECTION_CODE"));
        termLoanClassificationOB.setCbmWeakerSectionCode(new ComboBoxModel(key, value));

        getKeyValue((HashMap) keyValue.get("TERM_LOAN.REFINANCING_INSTITUTION"));
        termLoanClassificationOB.setCbmRefinancingInsti(new ComboBoxModel(key, value));

        getKeyValue((HashMap) keyValue.get("TERM_LOAN.ASSET_STATUS"));
        termLoanClassificationOB.setCbmAssetCode(new ComboBoxModel(key, value));

        getKeyValue((HashMap) keyValue.get("TERM_LOAN.FACILITY"));
        termLoanClassificationOB.setCbmTypeFacility(new ComboBoxModel(key, value));

//        getKeyValue((HashMap)keyValue.get("ACT_OP_MODE"));
//        termLoanOtherDetailsOB.setCbmOpModeAI(new ComboBoxModel(key,value));
//        
//        getKeyValue((HashMap)keyValue.get("SETTLEMENT_MODE"));
//        termLoanOtherDetailsOB.setCbmSettlementModeAI(new ComboBoxModel(key,value));
//        
//        getKeyValue((HashMap)keyValue.get("FREQUENCY"));
//        termLoanOtherDetailsOB.setCbmStmtFreqAD(new ComboBoxModel(key,value));

        lookup_keys.add("");
        getKeyValue((HashMap) keyValue.get("GOLD_CONFIGURATION"));
        termLoanSecurityOB.setCbmPurityOfGold(new ComboBoxModel(key, value));


        lookup_keys.add("");
        getKeyValue((HashMap) keyValue.get("TERMLOAN.GOLDITEM"));
        termLoanSecurityOB.setCbmItem(new ComboBoxModel(key, value));
        ArrayList<String> itemValue = value;
        Collections.sort(itemValue);
        for (int i = 0; i < itemValue.size(); i++) {
            if (!itemValue.get(i).equals("")) {
                goldItemMap.put(itemValue.get(i).toUpperCase(), "1");
            }
        }

        lookup_keys.add("");
        getKeyValue((HashMap) keyValue.get("GOLD_CONFIGURATION"));
//        setCbmRenewalPurityOfGold(new ComboBoxModel(key,value));
        termLoanSecurityOB.setCbmRenewalPurityOfGold(new ComboBoxModel(key, value));

        lookup_keys.add("");
        getKeyValue((HashMap) keyValue.get("PURPOSE_OF_LOAN"));
        termLoanClassificationOB.setCbmPurposeCode(new ComboBoxModel(key, value));
        cbmRenewalPurposeOfLoan = new ComboBoxModel(key, value);

//        lookUpHash=new HashMap();
//        lookUpHash.put(CommonConstants.MAP_NAME, "InwardClearing.getBank");
//        keyValue = ClientUtil.populateLookupData(lookUpHash);
//        getKeyValue((HashMap)keyValue.get("DATA"));
//        termLoanGuarantorOB.setCbmPLIName(new ComboBoxModel(key,value));

        HashMap param = new HashMap();
        param.put(CommonConstants.MAP_NAME, "getAppraiserCode");
        HashMap whereMap1 = new HashMap();
        whereMap1.put(CommonConstants.BRANCH_ID, ProxyParameters.BRANCH_ID);
          param.put(CommonConstants.PARAMFORQUERY, whereMap1);
          
       // param.put(key, value)
        keyValue = ClientUtil.populateLookupData(param);
        fillData((HashMap) keyValue.get(CommonConstants.DATA));
        cbmAppraiserId = new ComboBoxModel(key, value);
        cbmRenewalAppraiserId= new ComboBoxModel(key,value);
        termLoanSecurityOB.setCbmRenewalAppraiserId(new ComboBoxModel(key, value));
        //        getKeyValue((HashMap)keyValue.get("TERM_LOAN.PURPOSE_CODE"));
        //        termLoanClassificationOB.setCbmPurposeCode(new ComboBoxModel(key,value));

        //        lookUpHash = new HashMap();
        //        lookUpHash.put(CommonConstants.MAP_NAME,"TermLoan.getProdId");
        //        lookUpHash.put(CommonConstants.PARAMFORQUERY,null);
        //        keyValue = ClientUtil.populateLookupData(lookUpHash);
        //
        //        getKeyValue((HashMap)keyValue.get(CommonConstants.DATA));

        setProdIDAsBlank();
        HashMap whereMap = new HashMap();
        List list = ClientUtil.executeQuery("getGoldLoanProductIDs", whereMap);
        //System.out.println("getGoldLoanProductIDs" + list);
        key = new ArrayList();
        value = new ArrayList();
        if (list != null && list.size() > 1) {
            key.add("");
            value.add("");
        }
        for (int i = 0; i < list.size(); i++) {
            whereMap = (HashMap) list.get(i);            
            key.add(whereMap.get("KEY"));
            value.add(whereMap.get("VALUE"));
            //System.out.println("whereMap" + whereMap);

        }
        cbmGoldLoanProd = new ComboBoxModel(key, value);
        cbmRnwGoldLoanProd = new ComboBoxModel(key, value);

        //fillData((HashMap)whereMap.get(CommonConstants.DATA));

//        termLoanGuarantorOB.setCbmProdId("");

        /*//Added by sreekrishnan
        HashMap goldMap = new HashMap();
        goldMap.put("LOOKUP_ID", "GOLD_CONFIGURATION");
        List goldList = ClientUtil.executeQuery("getDefaultGoldPurity", goldMap);
        //System.out.println("getGoldLoanProductIDs" + goldList);
        key = new ArrayList();
        value = new ArrayList();
        if (goldList != null && goldList.size() > 1) {
            key.add("");
            value.add("");
        }
        for (int i = 0; i < goldList.size(); i++) {
            goldMap = (HashMap) goldList.get(i);
            key.add(goldMap.get("KEY"));
            value.add(goldMap.get("VALUE"));
            //System.out.println("goldMap" + goldMap);

        }
        termLoanSecurityOB.setCbmPurityOfGold(new ComboBoxModel(key, value));
        termLoanSecurityOB.setCbmRenewalPurityOfGold(new ComboBoxModel(key, value));*/
        
        tempKey = null;
        tempVal = null;
        lookup_keys = null;
    }

    private void fillData(HashMap keyValue) throws Exception {
        key = (ArrayList) keyValue.get("KEY");
        value = (ArrayList) keyValue.get("VALUE");

    }

    private void setOperationMap() throws Exception {
        operationMap = new HashMap();
        operationMap.put(CommonConstants.JNDI, "GoldLoanJNDI");
        operationMap.put(CommonConstants.HOME, "goldloan.GoldLoanHome");
        operationMap.put(CommonConstants.REMOTE, "goldloan.GoldLoan");
    }

    private void setBlankKeyValue() {
        key = new ArrayList();
        key.add("");
        value = new ArrayList();
        value.add("");
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
    public void populateData(HashMap whereMap, NomineeOB objNomineeOB) {
        log.info("In populateData..." + whereMap);
        HashMap mapData = null;
        try {
            whereMap.put("UI_PRODUCT_TYPE", "TL");
            whereMap.put("KEY_VALUE", "KEY_VALUE");
            mapData = (HashMap) proxy.executeQuery(whereMap, operationMap);
            log.info("proxy.executeQuery is working fine");
            populateOB(mapData, objNomineeOB);
            //            authOB.ttNotifyObservers();
            //            poaOB.ttNotifyObservers();
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
    private void populateOB(HashMap mapData, NomineeOB objNomineeOB) {
        log.info("In populateOB...");
        ArrayList sanctionList;
        ArrayList sanctionFacilityList;
        ArrayList facilityList;
        ArrayList nomineeList = null;
        //System.out.println("populateOb###3" + mapData);
        // Populate the Tabs on the Basis of Borrower Number or Account Number
        //        if ((mapData.get("KEY_VALUE").equals("BORROWER_NUMBER")) || (mapData.get("KEY_VALUE").equals("AUTHORIZE"))){
        // Populate the Tabs on the Basis of Borrower Number
        if (((List) mapData.get("TermLoanBorrowerTO")).size() > 0) {
            //System.out.println("TermLoanBorrowerTOTermLoanBorrowerTO");
            // Populate the Borrower Tab if the corresponding record is existing in Database
            setBorrowerNo(((TermLoanBorrowerTO) ((List) mapData.get("TermLoanBorrowerTO")).get(0)).getBorrowNo());
            termLoanBorrowerOB.setTermLoanBorrowerTO((TermLoanBorrowerTO) ((List) mapData.get("TermLoanBorrowerTO")).get(0));
            setStatusBy(termLoanBorrowerOB.getStatusBy());
            //                setStatusBy(TrueTransactMain.USER_ID);
            setAuthorizeStatus(termLoanBorrowerOB.getAuthorizeStatus());
        } else {
            return;
        }
        // To Populate the Main Customer
        //System.out.println("To Populate the Main Customer");
        HashMap queryMap = new HashMap();
        queryMap.put("CUST_ID", termLoanBorrowerOB.getTxtCustID());
        termLoanBorrowerOB.populateBorrowerTabCustomerDetails(queryMap, false, loanType);
        if (termLoanBorrowerOB.getCboConstitution().equals("Joint Account")) {
            //System.out.println("Joint Account");
            termLoanBorrowerOB.setTermLoanJointAcctTO((ArrayList) (mapData.get("TermLoanJointAcctTO")));
        }
//        if (((List) mapData.get("TermLoanCompanyTO")).size() > 0){
//            // Populate the Company Tab if the corresponding record is existing in Database
////            termLoanCompanyOB.setTermLoanCompanyTO((TermLoanCompanyTO) ((List) mapData.get("TermLoanCompanyTO")).get(0));
//        }

        /* To set the ArrayList in NomineeOB so as to set the data in the Nominee-Table...*/
        nomineeList = (ArrayList) mapData.get("AccountNomineeList");
        objNomineeOB.setNomimeeList(nomineeList);
        objNomineeOB.setNomineeTabData();
        objNomineeOB.ttNotifyObservers();
        //            termLoanAuthorizedSignatoryOB.setAuthorizedSignatoryTO((ArrayList) (mapData.get("AuthorizedSignatoryTO")), getBorrowerNo());
        //            termLoanAuthorizedSignatoryInstructionOB.setAuthorizedSignatoryInstructionTO((ArrayList) (mapData.get("AuthorizedSignatoryInstructionTO")), getBorrowerNo());
        //            if(mapData.containsKey("PowerAttorneyTO") && mapData.get("PowerAttorneyTO")!=null)
        //                termLoanPoAOB.setTermLoanPowerAttorneyTO((ArrayList) (mapData.get("PowerAttorneyTO")), getBorrowerNo());
        sanctionList = (ArrayList) (mapData.get("TermLoanSanctionTO"));
        sanctionFacilityList = new ArrayList();
        sanctionFacilityList = (ArrayList) (mapData.get("TermLoanSanctionFacilityTO"));
        //System.out.println("sanctionFacilityList" + sanctionFacilityList);
        setTermLoanSanctionTO(sanctionList, sanctionFacilityList);
        //            tableUtilSanction_Main.setAllValues(sanctionMainAll);
        //            tableUtilSanction_Main.setTableValues(sanctionMainAllTabRecords);
        //            setMax_Del_San_Details_No(getBorrowerNo());
        //
        facilityList = (ArrayList) (mapData.get("TermLoanFacilityTO"));
        setTermLoanFacilityTO(facilityList);
        //            if(loanType.equals("OTHERS"))
        //                setShareTab(mapData);
        queryMap = null;
        //        }
        //        if ((mapData.get("KEY_VALUE").equals("ACCOUNT_NUMBER")) || (mapData.get("KEY_VALUE").equals("AUTHORIZE"))){
        // Populate the Tabs on the Basis of Account Number
        // To populate Facility details at the time of authorization
        //            if (mapData.get("KEY_VALUE").equals("AUTHORIZE")){
        if (mapData.containsKey("TermLoanSanctionTO.AUTHORIZE") && ((List) mapData.get("TermLoanSanctionTO.AUTHORIZE")).size() > 0) {
            //System.out.println("TermLoanSanctionTO.AUTHORIZETermLoanSanctionTO.AUTHORIZE");
            ArrayList sanctionAuthorizeList = (ArrayList) (mapData.get("TermLoanSanctionTO.AUTHORIZE"));
            setTermLoanSanctionTOForAuthorize(sanctionAuthorizeList);
        }
        if (mapData.containsKey("TermLoanSanctionFacilityTO.AUTHORIZE") && ((List) mapData.get("TermLoanSanctionFacilityTO.AUTHORIZE")).size() > 0) {
            //System.out.println("TermLoanSanctionFacilityTO.AUTHORIZE");
            ArrayList sanctionFacilityAuthorizeList = (ArrayList) (mapData.get("TermLoanSanctionFacilityTO.AUTHORIZE"));
            setTermLoanSanctionFacilityTOForAuthorize(sanctionFacilityAuthorizeList);
            facilityList = (ArrayList) (mapData.get("TermLoanFacilityTO.AUTHORIZE"));
            // The return HashMap contains "sanctionNo" as key and
            // corresponding sanction number as value
            HashMap transactionMap = setTermLoanFacilityByAcctNo(facilityList);
            // To get the sanction details and sanction limit
            List resultList = (List) ClientUtil.executeQuery("getSanctionDetails", transactionMap);
            // If there is any record populate it
            if (resultList.size() > 0) {
                // Interest Maintenance tab details
                termLoanInterestOB.setLblSancDate_2(DateUtil.getStringDate((java.util.Date) ((HashMap) resultList.get(0)).get(SANCTION_DT)));
                termLoanInterestOB.setLblLimitAmt_2(CommonUtil.convertObjToStr(((HashMap) resultList.get(0)).get(LIMIT)));
                //                    termLoanSecurityOB.setLimitAmount(CommonUtil.convertObjToDouble(termLoanInterestOB.getLblLimitAmt_2()).doubleValue());
                termLoanInterestOB.setLblExpiryDate_2(DateUtil.getStringDate((java.util.Date) ((HashMap) resultList.get(0)).get(TO_DT)));
                // Classification tab details
                termLoanClassificationOB.setLblSanctionDate2(termLoanInterestOB.getLblSancDate_2());
            }
            getPLR_Rate(getLblProductID_FD_Disp());
            transactionMap = null;
            resultList = null;
        }
        //                sanctionAuthorizeList = null;
        //                sanctionFacilityAuthorizeList = null;
        //            }
        termLoanSecurityOB.resetSecurityDetails();
        //            termLoanSecurityOB.resetSecurityTableUtil();
        if (loanType.equals("OTHERS")) {
            //System.out.println("OTHERS");
            //            termLoanSecurityOB.setTblSecurityTab(new EnhancedTableModel(null, termLoanSecurityOB.getSecurityTabTitle()));
            termLoanSecurityOB.getTblSecurityTab().setDataArrayList(null, termLoanSecurityOB.getSecurityTabTitle());
            //            else
            //                termLoanSecurityOB.getTblSecurityTab().setDataArrayList(null, termLoanSecurityOB.getDepositSecurityTabTitle());
            if (mapData.containsKey("TermLoanSecurityTO") && ((List) mapData.get("TermLoanSecurityTO")).size() > 0) {
                //System.out.println("TermLoanSecurityTO");
                objTermLoanSecurityTO = (GoldLoanSecurityTO) ((List) mapData.get("TermLoanSecurityTO")).get(0);
                termLoanSecurityOB.populateSecurityData(objTermLoanSecurityTO);
                getCbmAppraiserId().setKeyForSelected(CommonUtil.convertObjToStr(objTermLoanSecurityTO.getAppraiserId()));
                // Added by nithya on 02-07-2018 for 0013083: gold loan renewal appraiser id issue
                if (getCbmAppraiserId().getKeyForSelected() == null || CommonUtil.convertObjToStr(getCbmAppraiserId().getKeyForSelected()).length() <= 0) {
                    try {
                        filldropDownApp();
                    } catch (Exception ex) {
                        //System.out.println("Exception :: " + ex);
                    }
                    getCbmAppraiserId().setKeyForSelected(CommonUtil.convertObjToStr(objTermLoanSecurityTO.getAppraiserId()));
                }
            }
            termLoanSecurityOB.setTermLoanSecurityTO((ArrayList) (mapData.get("TermLoanSecurityTO")), getStrACNumber(), CommonUtil.convertObjToStr(getCbmTypeOfFacility().getKeyForSelected()));
        }
        // Populate the Tabs on the Basis of Account Number
        termLoanRepaymentOB.resetRepaymentSchedule();
        termLoanRepaymentOB.getTblRepaymentTab().setDataArrayList(null, termLoanRepaymentOB.getRepaymentTabTitle());
        termLoanRepaymentOB.setTermLoanRepaymentTO((ArrayList) (mapData.get("TermLoanRepaymentTO")), getStrACNumber());
        //populate additional sanction limit

        //            termLoanAdditionalSanctionOB.resetAdditionalSanctionDetails();
        //            termLoanAdditionalSanctionOB.getTblPeakSanctionTab().setDataArrayList(null, termLoanAdditionalSanctionOB.getAdditionalSanctionTabTitle());
        //            termLoanAdditionalSanctionOB.setTermLoanAdditionalSanctionTO((ArrayList) (mapData.get("TermLoanAdditionalSanctionTO")), getStrACNumber());

        //            agriSubLimitOB.getTblSubLimit().setDataArrayList(null, agriSubLimitOB.setSubLimitTableTitle());
        //            agriSubLimitOB.setSubLimitTO((ArrayList) (mapData.get("TermLoanDisburstTO")),getStrACNumber());

        //populate mumbai details
        //            agriSubSidyOB.setSubSidyTO((ArrayList)(mapData.get("TermLoanFacilityExtnTO")),getStrACNumber());//dontdelete
        //             agriSubSidyOB.notifyObservers();//dontdelete
        //
        //populate settlement ob
        //             if(mapData.containsKey("SettlementTO") && mapData.get("SettlementTO")!=null)
        //             {
        //             settlementOB.setSettlemtnOB(mapData);//dontdelete
        //             }
        //               if(mapData.containsKey("ActTransTO") && mapData.get("ActTransTO")!=null)
        //                   actTransOB.resetAcctTransfer();//dontdelete
        //                    actTransOB.setActTransTO(mapData);//dontdelete

        // Populate the Tabs on the Basis of Account Number
//        termLoanGuarantorOB.resetGuarantorDetails();
//        termLoanGuarantorOB.getTblGuarantorTab().setDataArrayList(null, termLoanGuarantorOB.getGuarantorTabTitle());
//        if(((ArrayList)mapData.get("TermLoanGuarantorTO")).isEmpty())
//            termLoanGuarantorOB.getTblGuarantorTab().setDataArrayList(null, termLoanGuarantorOB.getInstitGuarantorTabTitle());
//        termLoanGuarantorOB.setTermLoanGuarantorTO((ArrayList) (mapData.get("TermLoanGuarantorTO")), getStrACNumber());
//        termLoanGuarantorOB.setTermLoanInstitGuarantorTO((ArrayList) (mapData.get("TermLoanInstitGuarantorTO")), getStrACNumber());
        // Populate the Tabs on the Basis of Account Number
        //            termLoanInterestOB.resetInterestDetails();
        //            termLoanInterestOB.getTblInterestTab().setDataArrayList(null, termLoanInterestOB.getInterestTabTitle());
        objSMSSubscriptionTO = null;
        if (isMobileBanking && mapData.containsKey("SMSSubscriptionTO") && ((List) mapData.get("SMSSubscriptionTO")).size() > 0) {
            objSMSSubscriptionTO = (SMSSubscriptionTO) ((List) mapData.get("SMSSubscriptionTO")).get(0);
            objSMSSubscriptionTO.setStatus(CommonConstants.STATUS_MODIFIED);
            setSMSSubscriptionTO(objSMSSubscriptionTO);
        }

        //System.out.println("### setDetailsForLTD - depositCustDetMap 2 : " + depositCustDetMap);
        if (loanType.equals("LTD")) {
            termLoanInterestOB.setLoanType(loanType);
            //                termLoanInterestOB.setDepositNo(CommonUtil.convertObjToStr(depositCustDetMap.get("DEPOSIT_NO")));
        }
        termLoanInterestOB.setTermLoanInterestTO((ArrayList) (mapData.get("TermLoanInterestTO")), getStrACNumber());
        setInterest(CommonUtil.convertObjToStr(termLoanInterestOB.getTxtInter()));
        setPenalInterest(CommonUtil.convertObjToStr(termLoanInterestOB.getPenalInter()));
        if (((List) mapData.get("TermLoanClassificationTO")).size() > 0) {
            // Populate the Classification Tab if the corresponding record is existing in Database
            termLoanClassificationOB.setTermLoanClassificationTO((TermLoanClassificationTO) ((List) mapData.get("TermLoanClassificationTO")).get(0));
            termLoanClassificationOB.setClassifiDetails(CommonConstants.TOSTATUS_UPDATE);
        } else {
            termLoanClassificationOB.setClassifiDetails(CommonConstants.TOSTATUS_INSERT);
        }

//        if (((List) mapData.get("TermLoanOtherDetailsTO")).size() > 0){
//            // Populate the Other Details Tab if the corresponding record is existing in Database
//            termLoanOtherDetailsOB.setTermLoanOtherDetailsTO((TermLoanOtherDetailsTO) ((List) mapData.get("TermLoanOtherDetailsTO")).get(0));
//            termLoanOtherDetailsOB.setOtherDetailsMode(CommonConstants.TOSTATUS_UPDATE);
//        }else{
//            termLoanOtherDetailsOB.setOtherDetailsMode(CommonConstants.TOSTATUS_INSERT);
//        }

        // Populate the Tabs on the Basis of Account Number
        termLoanDocumentDetailsOB.resetDocumentDetails();
        termLoanDocumentDetailsOB.getTblDocumentTab().setDataArrayList(null, termLoanDocumentDetailsOB.getDocumentTabTitle());
        termLoanDocumentDetailsOB.setTermLoanDocumentTO((ArrayList) (mapData.get("TermLoanDocumentTO")));
        populateRenewalSanctionDeails(mapData);        
        if (mapData.containsKey("STOCK_PHOTO_FILE") && mapData.get("STOCK_PHOTO_FILE") != null) {// Added by nithya on 29-10-2019 for KD-763	Need Gold ornaments photo saving option
            HashMap photoMap = (HashMap) mapData.get("STOCK_PHOTO_FILE");
            if (mapData.containsKey("NEW_BORROWER_DETAILS") && mapData.get("NEW_BORROWER_DETAILS") != null) {
                if (photoMap.containsKey("PHOTO") && photoMap.get("PHOTO") != null) {
                    setRenewalPhotoByteArray((byte[]) photoMap.get("PHOTO"));
                }
            } else {
                if (photoMap.containsKey("PHOTO") && photoMap.get("PHOTO") != null) {
                    setPhotoByteArray((byte[]) photoMap.get("PHOTO"));
                }
            }
        }

        // Populate the Tabs on the Basis of Account Number
        //            termLoanPeakSanctionOB.resetPeakSanctionDetails();
        //            termLoanPeakSanctionOB.getTblPeakSanctionTab().setDataArrayList(null, termLoanPeakSanctionOB.getPeakSanctionTabTitle());
        //            termLoanPeakSanctionOB.setTermLoanPeakSanctionTO((ArrayList) (mapData.get("TermLoanDocumentTO")));

        //        }
        sanctionList = null;
        sanctionFacilityList = null;
        facilityList = null;
        //        setChanged();
        ttNotifyObservers();
    }

    private void setSMSSubscriptionTO(SMSSubscriptionTO objSMSSubscriptionTO) {
        setTxtMobileNo(CommonUtil.convertObjToStr(objSMSSubscriptionTO.getMobileNo()));
        setTdtMobileSubscribedFrom(DateUtil.getStringDate(objSMSSubscriptionTO.getSubscriptionDt()));
    }

    private void setTermLoanSanctionTO(ArrayList objSanctionTO, ArrayList objSanctionFacilityTO) {
        try {
            TermLoanSanctionTO objSanction;
            TermLoanSanctionFacilityTO objSanctionFacility;
            sanctionMainAllTabRecords = new ArrayList();
            ArrayList sanctionTabRec;
            ArrayList sanctionFacilityTabRec;
            ArrayList sanctionRemovedValues = new ArrayList();
            ArrayList removedValuesSanctionFacility;
            HashMap sanctionRecord;
            HashMap sanctionFacilityLocalRecord;
            String sanctionNo;
            String sanctionMaxDelSlNo = "";
            sanctionMainAll.clear();
            ArrayList sanctionFacilityTableValues;
            LinkedHashMap sanctionFacilityAllValues;
            // To retrieve the Sanction Details one by one from the Databse
            for (int i = objSanctionTO.size() - 1, j = 0; i >= 0; --i, ++j) {
                objSanction = (TermLoanSanctionTO) objSanctionTO.get(j);
                sanctionTabRec = new ArrayList();
                removedValuesSanctionFacility = new ArrayList();
                sanctionRecord = new HashMap();
                sanctionFacilityTableValues = new ArrayList();
                sanctionFacilityAllValues = new LinkedHashMap();

                setTxtSanctionNo(CommonUtil.convertObjToStr(objSanction.getSlNo()));
                setTdtSanctionDate(DateUtil.getStringDate(objSanction.getSanctionDt()));
                setCboSanctioningAuthority(CommonUtil.convertObjToStr(getCbmSanctioningAuthority().getDataForKey(objSanction.getSanctionAuthority())));
                setCboModeSanction(CommonUtil.convertObjToStr(getCbmModeSanction().getDataForKey(objSanction.getSanctionMode())));
                setTxtSanctionRemarks(CommonUtil.convertObjToStr(objSanction.getRemarks()));
            }
            //                sanctionTabRec.add(CommonUtil.convertObjToStr(objSanction.getSanctionNo()));
            //                sanctionTabRec.add(CommonUtil.convertObjToStr(objSanction.getSlNo()));
            //                sanctionTabRec.add(CommonUtil.convertObjToStr(getCbmSanctioningAuthority().getDataForKey(objSanction.getSanctionAuthority())));
            //                sanctionTabRec.add(CommonUtil.convertObjToStr(getCbmModeSanction().getDataForKey(objSanction.getSanctionMode())));
            //                sanctionMainAllTabRecords.add(sanctionTabRec);
            //
            //                sanctionRecord.put(SANCTION_NO,CommonUtil.convertObjToStr(objSanction.getSlNo()));
            //                sanctionRecord.put(SANCTION_SL_NO,objSanction.getSanctionNo());
            //                sanctionRecord.put(SANCTION_AUTHORITY, objSanction.getSanctionAuthority());
            //                sanctionRecord.put(SANCTION_DATE, DateUtil.getStringDate(objSanction.getSanctionDt()));
            //                sanctionRecord.put(SANCTION_MODE, objSanction.getSanctionMode());
            //                sanctionRecord.put(REMARKS, objSanction.getRemarks());
            //
            //                sanctionRecord.put(COMMAND, UPDATE);
            //
            // To populate the RemovedValues(ArrayList) in Sanction_Facility TableUtil Object
            // for the first time only after retrieving from the Database
            //                sanctionRecord.put(FLAG, TRUE);
            // To retrieve the Sanction Facility Details one by one from the Databse
            for (int k = objSanctionFacilityTO.size() - 1, m = 0; k >= 0; --k, ++m) {
                objSanctionFacility = (TermLoanSanctionFacilityTO) objSanctionFacilityTO.get(m);                
                sanctionNo = objSanctionFacility.getSanctionNo();
                //                    if (sanctionNo.equals(objSanction.getSanctionNo())){
                // For getting the record corresponding to the Sanction Number
                //                        sanctionFacilityTabRec = new ArrayList();
                //                        sanctionFacilityLocalRecord = new HashMap();
                setTxtNoInstallments(CommonUtil.convertObjToStr(objSanctionFacility.getNoInstall()));
                setTxtLimit_SD(CommonUtil.convertObjToStr(objSanctionFacility.getLimit()));
                cbmProductId.setKeyForSelected(objSanctionFacility.getProductId());
                //System.out.println("cbmGoldLoanProd" + objSanctionFacility.getProductId());
                cbmGoldLoanProd.setKeyForSelected(objSanctionFacility.getProductId());
                //                        sanctionFacilityTabRec.add(CommonUtil.convertObjToStr(objSanctionFacility.getSlNo()));
                //                        sanctionFacilityTabRec.add(CommonUtil.convertObjToStr(getCbmTypeOfFacility().getDataForKey(objSanctionFacility.getFacilityType())));
                //                        sanctionFacilityTabRec.add(CommonUtil.convertObjToStr(objSanctionFacility.getLimit()));
                //                        sanctionFacilityTabRec.add(DateUtil.getStringDate(objSanctionFacility.getFromDt()));
                //                        sanctionFacilityTabRec.add(DateUtil.getStringDate(objSanctionFacility.getToDt()));
                //
                //                        sanctionFacilityLocalRecord.put(SANCTION_NO, objSanction.getSlNo());
                //                        sanctionFacilityLocalRecord.put(SLNO, CommonUtil.convertObjToStr(objSanctionFacility.getSlNo()));
                //                        sanctionFacilityLocalRecord.put(FACILITY_TYPE, objSanctionFacility.getFacilityType());
                //                        sanctionFacilityLocalRecord.put(LIMIT, CommonUtil.convertObjToStr(objSanctionFacility.getLimit()));
                //                        sanctionFacilityLocalRecord.put(INITIAL_MONEY_DEPOSIT, CommonUtil.convertObjToStr(objSanctionFacility.getInitialMoneyDeposit()));
                //
                //                        sanctionFacilityLocalRecord.put(FROM, DateUtil.getStringDate(objSanctionFacility.getFromDt()));
                //                        sanctionFacilityLocalRecord.put(TO, DateUtil.getStringDate(objSanctionFacility.getToDt()));
                //                        sanctionFacilityLocalRecord.put(NO_INSTALLMENTS, CommonUtil.convertObjToStr(objSanctionFacility.getNoInstall()));
                //                        sanctionFacilityLocalRecord.put(REPAY_FREQ, CommonUtil.convertObjToStr(objSanctionFacility.getRepaymentFrequency()));
                if (objSanctionFacility.getRepaymentFrequency() != null && CommonUtil.convertObjToDouble(objSanctionFacility.getRepaymentFrequency()).doubleValue() == 360) {
                    setCboRepayFreq("Yearly");
                } else if (objSanctionFacility.getRepaymentFrequency() != null && CommonUtil.convertObjToDouble(objSanctionFacility.getRepaymentFrequency()).doubleValue() == 180) {
                    setCboRepayFreq("Half Yearly");
                } else if (objSanctionFacility.getRepaymentFrequency() != null && CommonUtil.convertObjToDouble(objSanctionFacility.getRepaymentFrequency()).doubleValue() == 90) {
                    setCboRepayFreq("Quaterly");
                } else if (objSanctionFacility.getRepaymentFrequency() != null && CommonUtil.convertObjToDouble(objSanctionFacility.getRepaymentFrequency()).doubleValue() == 30) {
                    setCboRepayFreq("Monthly");
                } else if (objSanctionFacility.getRepaymentFrequency() != null && CommonUtil.convertObjToDouble(objSanctionFacility.getRepaymentFrequency()).doubleValue() == 15) {
                    setCboRepayFreq("Fortnight");
                } else {//if (objSanctionFacility.getRepaymentFrequency() != null && CommonUtil.convertObjToDouble(objSanctionFacility.getRepaymentFrequency()).doubleValue() == 0) {// Added by nithya on 03-07-2019 for KD-546 New Gold Loan -45 days maturity type                   
                    setCboRepayFreq("USER_DEFINED_GOLD_LOAN");//Added by nithya on 06-07-2019 for KD 546 - New Gold Loan -45 days maturity type
                }
                //                      cbmRepayFreq.setKeyForSelected(CommonUtil.convertObjToStr(objSanctionFacility.getRepaymentFrequency()));
                //                        setCboRepayFreq(CommonUtil.convertObjToStr(getCbmRepayFreq().getDataForKey(CommonUtil.convertObjToStr(objSanctionFacility.getRepaymentFrequency()))));
                //                        sanctionFacilityLocalRecord.put(PROD_ID, CommonUtil.convertObjToStr(objSanctionFacility.getProductId()));
                //                        sanctionFacilityLocalRecord.put(MORATORIUM_GIVEN, CommonUtil.convertObjToStr(objSanctionFacility.getMoratoriumGiven()));
                //                        sanctionFacilityLocalRecord.put(FACILITY_REPAY_DATE, DateUtil.getStringDate(objSanctionFacility.getRepaymentDt()));
                //                        sanctionFacilityLocalRecord.put(MORATORIUM_PERIOD, CommonUtil.convertObjToStr(objSanctionFacility.getNoMoratorium()));
                //
                //                        sanctionFacilityLocalRecord.put(COMMAND, UPDATE);
                //
                //                        sanctionFacilityTableValues.add(sanctionFacilityTabRec);
                //                        sanctionFacilityAllValues.put(CommonUtil.convertObjToStr(objSanctionFacility.getSlNo()), sanctionFacilityLocalRecord);
                //
                //                        sanctionFacilityTabRec = null;
                //                        sanctionFacilityLocalRecord = null;
                //                    }
                //                    sanctionNo = null;
            }

            //                HashMap transactionMap = new HashMap();
            //                HashMap retrieve = new HashMap();
            //                transactionMap.put("borrowNo", getBorrowerNo());
            //                transactionMap.put("sanctionNo", objSanction.getSanctionNo());
            //                List resultList = ClientUtil.executeQuery("getSelectTermLoanSanctionFacilityMaxSLNO", transactionMap);
            //                if (resultList.size() > 0){
            //                    retrieve = (HashMap) resultList.get(0);
            //                    sanctionMaxDelSlNo = CommonUtil.convertObjToStr(retrieve.get("MAX_SL_NO"));
            //                }
            //                retrieve = null;
            //                transactionMap = null;
            //                resultList = null;
            //
            //                sanctionRecord.put(SANCTION_FACILITY_DELETED, removedValuesSanctionFacility);
            //                sanctionRecord.put(SANCTION_FACILITY_TABLE, sanctionFacilityTableValues);
            //                sanctionRecord.put(SANCTION_FACILITY_ALL, sanctionFacilityAllValues);
            //                sanctionRecord.put(MAX_DEL_SAN_DETAIL_SL_NO, sanctionMaxDelSlNo);
            //
            //                sanctionMainAll.put(CommonUtil.convertObjToStr(objSanction.getSanctionNo()), sanctionRecord);
            //
            //                sanctionFacilityTableValues = null;
            //                sanctionFacilityAllValues = null;
            //                sanctionTabRec = null;
            //                sanctionMaxDelSlNo = null;
            //            }
            //            tblSanctionMain.setDataArrayList(sanctionMainAllTabRecords, sanctionMainTitle);
            //            tableUtilSanction_Main.setRemovedValues(sanctionRemovedValues);
            //            sanctionFacilityTabRec = null;
            //            sanctionRecord = null;
            //            sanctionFacilityLocalRecord = null;
        } catch (Exception e) {
            log.info("Exception caught in setTermLoanSanctionTO: " + e);
            parseException.logException(e, true);
        }
    }

    private void setShareTab(HashMap mapData) {

        ArrayList shareTableList = new ArrayList();
        ArrayList jointAccount = (ArrayList) mapData.get("TermLoanJointAcctTO");
        if (jointAccount != null && jointAccount.size() > 0) {
            for (int i = 0; i < jointAccount.size(); i++) {
                TermLoanJointAcctTO termloanJoint = (TermLoanJointAcctTO) jointAccount.get(i);
                String cust_id = termloanJoint.getCustId();
                shareTableList.add(getShareDetails(cust_id));
            }
        } else {
            String cust_id = termLoanBorrowerOB.getTxtCustID();
            shareTableList.add(getShareDetails(cust_id));

        }
        if (!((ArrayList) shareTableList.get(0)).isEmpty()) {
            ArrayList facilityList = (ArrayList) (mapData.get("TermLoanFacilityTO"));
            if (facilityList != null && facilityList.size() > 0) {
                for (int t = 0; t < facilityList.size(); t++) {
                    ArrayList finalshareTab = new ArrayList();
                    TermLoanFacilityTO objTermLoanFacilityTO = (TermLoanFacilityTO) facilityList.get(t);
                    ArrayList actnum = new ArrayList();
                    CommonUtil.convertObjToStr(objTermLoanFacilityTO.getAcctNum());
                    actnum.add(CommonUtil.convertObjToStr(objTermLoanFacilityTO.getAcctNum()));
                    for (int k = 0; k < shareTableList.size(); k++) {
                        ArrayList list = (ArrayList) shareTableList.get(k);
                        finalshareTab.add(list.get(0));
                        finalshareTab.add(list.get(1));
                        finalshareTab.add(actnum.get(0));
                        finalshareTab.add(list.get(2));
                        finalshareTab.add(list.get(3));
                        finalshareTab.add(list.get(4));
                        finalshareTab.add(list.get(5));
                        //System.out.println("finalshareTab####" + finalshareTab + "shareTitle#3" + shareTitle);
                        shareTableList.set(k, finalshareTab);
                        finalshareTab = new ArrayList();
                    }
                    tblShare.setDataArrayList(shareTableList, shareTitle);
                    actnum = new ArrayList();
                }
            }
        }
        mapData = new HashMap();
        shareTableList = new ArrayList();
    }

    public ArrayList getShareDetails(String Cust_id) {
        HashMap shareMap = new HashMap();
        ArrayList setList = new ArrayList();
        shareMap.put("CUST_ID", Cust_id);

        List share = ClientUtil.executeQuery("getShareAccInfoDataForLoan", shareMap);
        if (share != null && share.size() > 0) {
            shareMap = (HashMap) share.get(0);
            double sharefee = CommonUtil.convertObjToDouble(shareMap.get("SHARE_FEE")).doubleValue();
            double shareamt = CommonUtil.convertObjToDouble(shareMap.get("SHARE_AMOUNT")).doubleValue();
            double noofShare = CommonUtil.convertObjToDouble(shareMap.get("NO_OF_SHARES")).doubleValue();
            double totShareAmt = (sharefee + shareamt) * noofShare;
            setList.add(shareMap.get("CUSTNAME"));
            setList.add(shareMap.get("CUST_ID"));
            setList.add(shareMap.get("SHARE_TYPE"));
            setList.add(shareMap.get("SHARE_ACCT_NO"));
            setList.add(shareMap.get("NO_OF_SHARES"));
            setList.add(new Double(totShareAmt));
            //            setList.add(shareMap.get("STATUS"));

        }
        return setList;

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

    private void setTermLoanSanctionTOForAuthorize(ArrayList objSanctionTOList) {
        try {
            TermLoanSanctionTO objTermLoanSanctionTO;
            objTermLoanSanctionTO = (TermLoanSanctionTO) objSanctionTOList.get(0);

            setTxtSanctionSlNo(objTermLoanSanctionTO.getSanctionNo());
            setTxtSanctionNo(CommonUtil.convertObjToStr(objTermLoanSanctionTO.getSlNo()));
            setStrRealSanctionNo(getTxtSanctionNo());
            setTdtSanctionDate(CommonUtil.convertObjToStr(objTermLoanSanctionTO.getSanctionDt()));
            setCboSanctioningAuthority(CommonUtil.convertObjToStr(getCbmSanctioningAuthority().getDataForKey(objTermLoanSanctionTO.getSanctionAuthority())));
            setTxtSanctionRemarks(objTermLoanSanctionTO.getRemarks());
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
            setSanctionSlNo(CommonUtil.convertObjToStr(objTermLoanSanctionFacilityTO.getSlNo()));
            cbmRepayFreq.setKeyForSelected(CommonUtil.convertObjToStr(objTermLoanSanctionFacilityTO.getRepaymentFrequency()));
            setLblProductID_FD_Disp(objTermLoanSanctionFacilityTO.getProductId());
            populateRestofProdId_AccHead();
            getCbmTypeOfFacility().setKeyForSelected(CommonUtil.convertObjToStr(objTermLoanSanctionFacilityTO.getFacilityType()));
            setCboTypeOfFacility(CommonUtil.convertObjToStr(getCbmTypeOfFacility().getDataForKey(CommonUtil.convertObjToStr(objTermLoanSanctionFacilityTO.getFacilityType()))));
            // To set the product ids based on Type of facility
            setFacilityProductID(CommonUtil.convertObjToStr(objTermLoanSanctionFacilityTO.getFacilityType()));
            setTxtLimit_SD(CommonUtil.convertObjToStr(objTermLoanSanctionFacilityTO.getLimit()));
            setTxtLimit_SDMoneyDeposit(CommonUtil.convertObjToStr(objTermLoanSanctionFacilityTO.getInitialMoneyDeposit()));
            termLoanInterestOB.setLblLimitAmt_2(getTxtLimit_SD());
            setTdtFDate(DateUtil.getStringDate(objTermLoanSanctionFacilityTO.getFromDt()));
            setTdtTDate(DateUtil.getStringDate(objTermLoanSanctionFacilityTO.getToDt()));
            if (CommonUtil.convertObjToStr(objTermLoanSanctionFacilityTO.getMoratoriumGiven()).equals("Y")) {
                setChkMoratorium_Given(true);
            } else if (CommonUtil.convertObjToStr(objTermLoanSanctionFacilityTO.getMoratoriumGiven()).equals("N")) {
                setChkMoratorium_Given(false);
            }
            setTdtFacility_Repay_Date(DateUtil.getStringDate(objTermLoanSanctionFacilityTO.getRepaymentDt()));
            setTxtFacility_Moratorium_Period(CommonUtil.convertObjToStr(objTermLoanSanctionFacilityTO.getNoMoratorium()));
            termLoanInterestOB.setLblExpiryDate_2(getTdtTDate());
            setTxtNoInstallments(CommonUtil.convertObjToStr(objTermLoanSanctionFacilityTO.getNoInstall()));
            //            setCboRepayFreq(CommonUtil.convertObjToStr(getCbmRepayFreq().getDataForKey(CommonUtil.convertObjToStr(objTermLoanSanctionFacilityTO.getRepaymentFrequency()))));
            getCbmRepayFreq().setKeyForSelected(CommonUtil.convertObjToStr(objTermLoanSanctionFacilityTO.getRepaymentFrequency()));
            getCbmProductId().setKeyForSelected(CommonUtil.convertObjToStr(objTermLoanSanctionFacilityTO.getProductId()));
            setFacilityAcctHead();
            //            setCboProductId(CommonUtil.convertObjToStr(getCbmProductId().getDataForKey(CommonUtil.convertObjToStr(objTermLoanSanctionFacilityTO.getProductId()))));
            getPLR_Rate(CommonUtil.convertObjToStr(getCbmProductId().getKeyForSelected()));
        } catch (Exception e) {
            log.info("Exception caught in setTermLoanSanctionFacilityTOForAuthorize: " + e);
            parseException.logException(e, true);
        }
    }

    private void setTermLoanFacilityTO(ArrayList objFacilityTOList) {
        try {
            TermLoanFacilityTO objTermLoanFacilityTO;
            HashMap facilityRecord;
            LinkedHashMap allLocalRecords = new LinkedHashMap();
            int serialNo;
            // To retrieve the Facility Details from the Database
            for (int i = objFacilityTOList.size() - 1, j = 0; i >= 0; --i, ++j) {
                objTermLoanFacilityTO = (TermLoanFacilityTO) objFacilityTOList.get(j);
                facilityRecord = new HashMap();
                //                facilityRecord.put(SANCTION_NO, objTermLoanFacilityTO.getSanctionNo());
                //                facilityRecord.put(SLNO, CommonUtil.convertObjToStr(objTermLoanFacilityTO.getSlNo()));
                //                facilityRecord.put(PROD_ID, CommonUtil.convertObjToStr(objTermLoanFacilityTO.getProdId()));
                //                facilityRecord.put(SECURITY_TYPE, CommonUtil.convertObjToStr(objTermLoanFacilityTO.getSecurityType()));
                //                facilityRecord.put(SECURITY_DETAILS, CommonUtil.convertObjToStr(objTermLoanFacilityTO.getSecurityDetails()));
                //                facilityRecord.put(ACC_TYPE, CommonUtil.convertObjToStr(objTermLoanFacilityTO.getAccountType()));
                //                //                facilityRecord.put(INTEREST_NATURE, CommonUtil.convertObjToStr(objTermLoanFacilityTO.getInterestNature()));
                //                facilityRecord.put(INTEREST_TYPE, CommonUtil.convertObjToStr(objTermLoanFacilityTO.getInterestType()));
                //                facilityRecord.put(ACC_LIMIT, CommonUtil.convertObjToStr(objTermLoanFacilityTO.getAccountLimit()));
                //                facilityRecord.put(RISK_WEIGHT, CommonUtil.convertObjToStr(objTermLoanFacilityTO.getRiskWeight()));
                //                facilityRecord.put(NOTE_DATE, DateUtil.getStringDate(objTermLoanFacilityTO.getDemandPromDt()));
                //                facilityRecord.put(NOTE_EXP_DATE, DateUtil.getStringDate(objTermLoanFacilityTO.getDemandPromExpdt()));
                //                facilityRecord.put(MULTI_DISBURSE, CommonUtil.convertObjToStr(objTermLoanFacilityTO.getMultiDisburse()));
                //                //                facilityRecord.put(SUBSIDY, CommonUtil.convertObjToStr(objTermLoanFacilityTO.getSubsidy()));
                //                facilityRecord.put(AOD_DATE, DateUtil.getStringDate(objTermLoanFacilityTO.getAodDt()));
                //                facilityRecord.put(ACCT_STATUS, objTermLoanFacilityTO.getAcctStatus());
                //                facilityRecord.put(ACCT_NAME, objTermLoanFacilityTO.getAcctName());
                //                facilityRecord.put(ACCT_NUM, objTermLoanFacilityTO.getAcctNum());
                //                facilityRecord.put(PURPOSE_DESC, objTermLoanFacilityTO.getPurposeDesc());
                //                facilityRecord.put(GROUP_DESC, objTermLoanFacilityTO.getGroupDesc());
                //                facilityRecord.put(INTEREST, CommonUtil.convertObjToStr(objTermLoanFacilityTO.getInterest()));
                //                facilityRecord.put(CONTACT_PERSON, objTermLoanFacilityTO.getContactPerson());
                //                facilityRecord.put(CONTACT_PHONE, objTermLoanFacilityTO.getContactPhone());
                //                facilityRecord.put(REMARKS, objTermLoanFacilityTO.getRemarks());
                //                facilityRecord.put(INT_GET_FROM, objTermLoanFacilityTO.getIntGetFrom());
                //                facilityRecord.put(AUTHORIZE_BY_1, objTermLoanFacilityTO.getAuthorizeBy1());
                //                facilityRecord.put(AUTHORIZE_BY_2, objTermLoanFacilityTO.getAuthorizeBy2());
                //                facilityRecord.put(AUTHORIZE_DT_1, objTermLoanFacilityTO.getAuthorizeDt1());
                //                facilityRecord.put(AUTHORIZE_DT_2, objTermLoanFacilityTO.getAuthorizeDt2());
                //                facilityRecord.put(AUTHORIZE_STATUS_1, objTermLoanFacilityTO.getAuthorizeStatus1());
                //
                //                facilityRecord.put(AUTHORIZE_STATUS_2, objTermLoanFacilityTO.getAuthorizeStatus2());
                //                facilityRecord.put(RECOMMANDED_BY, objTermLoanFacilityTO.getRecommendedBy());
                //                facilityRecord.put(ACCT_OPEN_DT, objTermLoanFacilityTO.getAccOpenDt());
                //                facilityRecord.put(ACCT_CLOSE_DT, objTermLoanFacilityTO.getAccCloseDt());
                //                facilityRecord.put(AVAILABLE_BALANCE, objTermLoanFacilityTO.getAvailableBalance());// ADD BY BALA
                //                facilityRecord.put(DRAWING_POWER, objTermLoanFacilityTO.getDpYesNo());// ADD BY BALA
                //                facilityRecord.put(AUTHSIGNATORY, CommonUtil.convertObjToStr(objTermLoanFacilityTO.getAuthorizedSignatory()));
                //                facilityRecord.put(DOCDETAILS, CommonUtil.convertObjToStr(objTermLoanFacilityTO.getDocDetails()));
                //                facilityRecord.put(POFATTORNEY, CommonUtil.convertObjToStr(objTermLoanFacilityTO.getPofAttorney()));
                //                facilityRecord.put(ACCTTRANSFER, CommonUtil.convertObjToStr(objTermLoanFacilityTO.getAcctTransfer()));
                //
                //                //dont update while saving time
                //                facilityRecord.put("SHADOW_DEBIT", objTermLoanFacilityTO.getShadowDebit());
                //                facilityRecord.put("SHADOW_CREDIT", objTermLoanFacilityTO.getShadowCredit());
                //                facilityRecord.put("CLEAR_BALANCE", objTermLoanFacilityTO.getClearBalance());
                //                //
                //                facilityRecord.put("LAST_INT_CALC_DT", objTermLoanFacilityTO.getLastIntCalcDt());
                //                facilityRecord.put(COMMAND, UPDATE);
                //                serialNo = CommonUtil.convertObjToInt(objTermLoanFacilityTO.getSlNo());
                //                allLocalRecords.put(getFacilityKey(objTermLoanFacilityTO.getSanctionNo(), serialNo), facilityRecord);
                setAuthorizeDt(CommonUtil.convertObjToStr(objTermLoanFacilityTO.getAuthorizeDt1()));
                setAccountOpenDate(CommonUtil.convertObjToStr(objTermLoanFacilityTO.getAccOpenDt()));
                setCboAccStatus(CommonUtil.convertObjToStr(getCbmAccStatus().getDataForKey(objTermLoanFacilityTO.getAcctStatus())));
                setAccountCloseDate(CommonUtil.convertObjToStr(objTermLoanFacilityTO.getAccCloseDt()));
                setTdtDemandPromNoteDate(CommonUtil.convertObjToStr(objTermLoanFacilityTO.getDemandPromDt()));
                setTdtDemandPromNoteExpDate(CommonUtil.convertObjToStr(objTermLoanFacilityTO.getDemandPromExpdt()));
                setAvailableBalance(CommonUtil.convertObjToDouble(objTermLoanFacilityTO.getAvailableBalance()).doubleValue());
                if (CommonUtil.convertObjToStr(objTermLoanFacilityTO.getIsMobileBanking()).equals("Y")) {
                    setIsMobileBanking(true);
                } else {
                    setIsMobileBanking(false);
                }
                facilityRecord = null;
                objTermLoanFacilityTO = null;
            }

            facilityAll.clear();
            facilityAll = allLocalRecords;
            allLocalRecords = null;
        } catch (Exception e) {
            log.info("Exception caught in setTermLoanFacilityTO: " + e);
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
                setClearBalance(CommonUtil.convertObjToDouble(objTermLoanFacilityTO.getClearBalance()).doubleValue());
                setAvailableBalance(CommonUtil.convertObjToDouble(objTermLoanFacilityTO.getAvailableBalance()).doubleValue());
                setCboAccStatus(CommonUtil.convertObjToStr(getCbmAccStatus().getDataForKey(facilityRecord.get(ACCT_STATUS))));
                setCboRecommendedByType(CommonUtil.convertObjToStr(getCbmRecommendedByType().getDataForKey(facilityRecord.get(RECOMMANDED_BY))));
                setAccountOpenDate(CommonUtil.convertObjToStr(facilityRecord.get(ACCT_OPEN_DT)));
                setAccountCloseDate(CommonUtil.convertObjToStr(facilityRecord.get(ACCT_CLOSE_DT)));
                setCboIntGetFrom(CommonUtil.convertObjToStr(getCbmIntGetFrom().getDataForKey(facilityRecord.get(INT_GET_FROM))));
                setTxtContactPerson(CommonUtil.convertObjToStr(facilityRecord.get(CONTACT_PERSON)));
                setTxtContactPhone(CommonUtil.convertObjToStr(facilityRecord.get(CONTACT_PHONE)));
                setTxtRemarks(CommonUtil.convertObjToStr(facilityRecord.get(REMARKS)));
                setTxtPurposeDesc(CommonUtil.convertObjToStr(facilityRecord.get(PURPOSE_DESC)));
                setTxtGroupDesc(CommonUtil.convertObjToStr(facilityRecord.get(GROUP_DESC)));
                setTdtDemandPromNoteDate(CommonUtil.convertObjToStr(facilityRecord.get(NOTE_DATE)));
                setTdtDemandPromNoteExpDate(CommonUtil.convertObjToStr(facilityRecord.get(NOTE_EXP_DATE)));
                setTdtAODDate(CommonUtil.convertObjToStr(facilityRecord.get(AOD_DATE)));
                if (facilityRecord.get(SECURITY_DETAILS).equals(UNSECURED)) {
                    setRdoSecurityDetails_Unsec(true);
                } else if (facilityRecord.get(SECURITY_DETAILS).equals(PARTLY_SECURED)) {
                    setRdoSecurityDetails_Partly(true);
                } else if (facilityRecord.get(SECURITY_DETAILS).equals(FULLY_SECURED)) {
                    setRdoSecurityDetails_Fully(true);
                }
                setTxtAcct_Name(CommonUtil.convertObjToStr(facilityRecord.get(ACCT_NAME)));
                if (facilityRecord.get(SECURITY_TYPE).equals(INSPECT_INSURANCE_GUARANTAR)) {
                    setChkStockInspect(true);
                    setChkInsurance(true);
                    setChkGurantor(true);
                } else if (facilityRecord.get(SECURITY_TYPE).equals(STOCK_INSPECT_INSURANCE)) {
                    setChkStockInspect(true);
                    setChkInsurance(true);
                } else if (facilityRecord.get(SECURITY_TYPE).equals(STOCK_INSPECT_GUARANTAR)) {
                    setChkStockInspect(true);
                    setChkGurantor(true);
                } else if (facilityRecord.get(SECURITY_TYPE).equals(INSURANCE_GUARANTAR)) {
                    setChkInsurance(true);
                    setChkGurantor(true);
                } else if (facilityRecord.get(SECURITY_TYPE).equals(STOCK_INSPECT)) {
                    setChkStockInspect(true);
                } else if (facilityRecord.get(SECURITY_TYPE).equals(INSURANCE)) {
                    setChkInsurance(true);
                } else if (facilityRecord.get(SECURITY_TYPE).equals(GUARANTAR)) {
                    setChkGurantor(true);
                }

                if (facilityRecord.get(ACC_TYPE).equals(NEW)) {
                    setRdoAccType_New(true);
                } else if (facilityRecord.get(ACC_TYPE).equals(TRANSFERED)) {
                    setRdoAccType_Transfered(true);
                }

                if (facilityRecord.get(ACC_LIMIT).equals(MAIN)) {
                    setRdoAccLimit_Main(true);
                } else if (facilityRecord.get(ACC_LIMIT).equals(SUBMIT)) {
                    setRdoAccLimit_Submit(true);
                }

                if (facilityRecord.get(RISK_WEIGHT).equals(YES)) {
                    setRdoRiskWeight_Yes(true);
                } else if (facilityRecord.get(RISK_WEIGHT).equals(NO)) {
                    setRdoRiskWeight_No(true);
                }

                //                if (facilityRecord.get(INTEREST_NATURE).equals(PLR)){
                //                    setRdoNatureInterest_PLR(true);
                //                }else if (facilityRecord.get(INTEREST_NATURE).equals(NON_PLR)){
                //                    setRdoNatureInterest_NonPLR(true);
                //                }

                if (facilityRecord.get(MULTI_DISBURSE).equals(YES)) {
                    setRdoMultiDisburseAllow_Yes(true);
                } else if (facilityRecord.get(MULTI_DISBURSE).equals(NO)) {
                    setRdoMultiDisburseAllow_No(true);
                }

                if (facilityRecord.get(DRAWING_POWER) != null && facilityRecord.get(DRAWING_POWER).equals(YES)) {
                    setRdoDP_YES(true);
                } else if (facilityRecord.get(DRAWING_POWER) != null && facilityRecord.get(DRAWING_POWER).equals(NO)) {
                    setRdoDP__NO(true);
                }


                if (facilityRecord.get(INTEREST).equals(SIMPLE)) {
                    setRdoInterest_Simple(true);
                } else if (facilityRecord.get(INTEREST).equals(COMPOUND)) {
                    setRdoInterest_Compound(true);
                }

                if (facilityRecord.get(ACCTTRANSFER).equals(ACCTTRANSFER)) {
                    setChkAcctTransfer(true);
                } else {
                    setChkAcctTransfer(false);
                }
                //                if (facilityRecord.containsKey(POFATTORNEY) && facilityRecord.get(POFATTORNEY)!=null && facilityRecord.get(POFATTORNEY).equals(POFATTORNEY)){
                //                    setChkPOFAttorney(true);
                //                }else
                //                    setChkPOFAttorney(false);
                if (facilityRecord.containsKey(AUTHSIGNATORY) && facilityRecord.get(AUTHSIGNATORY) != null && facilityRecord.get(AUTHSIGNATORY).equals(AUTHSIGNATORY)) {
                    setChkAuthorizedSignatory(true);
                } else {
                    setChkAuthorizedSignatory(false);
                }
                if (facilityRecord.containsKey(DOCDETAILS) && facilityRecord.get(DOCDETAILS) != null && facilityRecord.get(DOCDETAILS).equals(DOCDETAILS)) {
                    setChkDocDetails(true);
                } else {
                    setChkDocDetails(false);
                }

                //                setFacilityProdID(CommonUtil.convertObjToInt(objTermLoanFacilityTO.getSanctionNo()), CommonUtil.convertObjToInt(objTermLoanFacilityTO.getSlNo()));
                setLblProductID_FD_Disp(CommonUtil.convertObjToStr(facilityRecord.get(PROD_ID)));
                populateRestofProdId_AccHead();
                setCboInterestType(CommonUtil.convertObjToStr(getCbmInterestType().getDataForKey(CommonUtil.convertObjToStr(facilityRecord.get(INTEREST_TYPE)))));
//                termLoanGuarantorOB.setLblAccNo_GD_2(objTermLoanFacilityTO.getAcctNum());
                termLoanDocumentDetailsOB.setLblAcctNo_Disp_DocumentDetails(objTermLoanFacilityTO.getAcctNum());
                termLoanClassificationOB.setLblAccNo_CD_2(objTermLoanFacilityTO.getAcctNum());
//                termLoanOtherDetailsOB.setStrACNumber(objTermLoanFacilityTO.getAcctNum());
                setLblAccNo_Idetail_2(objTermLoanFacilityTO.getAcctNum());
                termLoanInterestOB.setLblAccNo_IM_2(objTermLoanFacilityTO.getAcctNum());
                getPLR_Rate(CommonUtil.convertObjToStr(facilityRecord.get(PROD_ID)));
                termLoanRepaymentOB.setLblAccNo_RS_2(objTermLoanFacilityTO.getAcctNum());
                termLoanSecurityOB.setLblAccNoSec_2(objTermLoanFacilityTO.getAcctNum());
//                termLoanOtherDetailsOB.setLblAcctNo_Disp_ODetails(objTermLoanFacilityTO.getAcctNum());
                termLoanClassificationOB.setLblSanctionNo2(sanctionNo);
                setStrACNumber(objTermLoanFacilityTO.getAcctNum());
                if (loanType.equals("LTD") && getStrACNumber().length() > 0) {
                    HashMap accountMap = new HashMap();
                    accountMap.put("ACCT_NUM", getStrACNumber());
                    List lst = ClientUtil.executeQuery("getDepositbeforeAuthDetails", accountMap); //getDepositDetails
                    if (lst != null && lst.size() > 0) {
                        accountMap = (HashMap) lst.get(0);
                        setLblDepositNo(CommonUtil.convertObjToStr(accountMap.get("DEPOSIT_NO")));
                    }
                } else {
                    setLblDepositNo("");
                }
                cbmIntGetFrom.setKeyForSelected(facilityRecord.get(INT_GET_FROM));
                facilityRecord = null;
                objTermLoanFacilityTO = null;
            }
        } catch (Exception e) {
            log.info("Exception caught in setTermLoanFacilityByAcctNo: " + e);
            parseException.logException(e, true);
        }
        return returnMap;
    }

    void populateRenewalSanctionDeails(HashMap dataMap) {        
        HashMap datacollectionMap = new HashMap();
        ArrayList list = new ArrayList();
        if (dataMap != null) {

            if (dataMap.containsKey("NEW_BORROWER_DETAILS") && dataMap.get("NEW_BORROWER_DETAILS") != null) {
                datacollectionMap = (HashMap) dataMap.get("NEW_BORROWER_DETAILS");
                list = (ArrayList) datacollectionMap.get("TermLoanSanctionFacilityTO");
                TermLoanSanctionFacilityTO sanctionFacilityTo = (TermLoanSanctionFacilityTO) list.get(0);
//            TermLoanSanctionFacilityTO sanctionFacilityTo=(TermLoanSanctionFacilityTO)(List)datacollectionMap.get("TermLoanSanctionFacilityTO");
                list = (ArrayList) datacollectionMap.get("TermLoanSanctionTO");
                TermLoanSanctionTO termLoanSanctionTo = (TermLoanSanctionTO) list.get(0);//datacollectionMap.get("TermLoanSanctionTO");
                list = (ArrayList) datacollectionMap.get("TermLoanFacilityTO");
                TermLoanFacilityTO termLoanFacilityTo = (TermLoanFacilityTO) list.get(0);
                list = (ArrayList) datacollectionMap.get("TermLoanInterestTO");
                TermLoanInterestTO termLoanInterestTo = (TermLoanInterestTO) list.get(0);
//            sanctionFacilityTo
                setRenewalBorrowerNo(CommonUtil.convertObjToStr(sanctionFacilityTo.getBorrowNo()));
                setTdtRenewalSanctionDate(CommonUtil.convertObjToStr(sanctionFacilityTo.getFromDt()));
                setRenewalAcctNum(CommonUtil.convertObjToStr(termLoanFacilityTo.getAcctNum()));
                setCboRenewalSanctioningAuthority(CommonUtil.convertObjToStr(termLoanSanctionTo.getSanctionAuthority()));
                setTdtRenewalAccountOpenDate(CommonUtil.convertObjToStr(sanctionFacilityTo.getFromDt()));
                setLblRenewalAcctNo_Sanction_Disp(CommonUtil.convertObjToStr(termLoanFacilityTo.getAcctNum()));

                setTxtRenewalLimit_SD(CommonUtil.convertObjToStr(sanctionFacilityTo.getLimit()));
                setTxtRenewalNoInstallments(CommonUtil.convertObjToStr(sanctionFacilityTo.getNoInstall()));

                getCbmRenewalSanctioningAuthority().setKeyForSelected(termLoanSanctionTo.getSanctionAuthority());
                getCbmRenewalRepayFreq().setKeyForSelected(sanctionFacilityTo.getRepaymentFrequency());
                getCbmRenewalAccStatus().setKeyForSelected(termLoanFacilityTo.getAcctStatus());
                setCboRenewalAccStatus(CommonUtil.convertObjToStr(termLoanFacilityTo.getAcctStatus()));
                setCboRenewalPurposeOfLoan(CommonUtil.convertObjToStr(termLoanFacilityTo.getAcctStatus()));
                if (sanctionFacilityTo.getRepaymentFrequency() != null && CommonUtil.convertObjToDouble(sanctionFacilityTo.getRepaymentFrequency()).doubleValue() == 360) {
                    setCboRenewalRepayFreq("Yearly");
                } else if (sanctionFacilityTo.getRepaymentFrequency() != null && CommonUtil.convertObjToDouble(sanctionFacilityTo.getRepaymentFrequency()).doubleValue() == 180) {
                    setCboRenewalRepayFreq("Half Yearly");
                } else if (sanctionFacilityTo.getRepaymentFrequency() != null && CommonUtil.convertObjToDouble(sanctionFacilityTo.getRepaymentFrequency()).doubleValue() == 90) {
                    setCboRenewalRepayFreq("Quaterly");
                } else if (sanctionFacilityTo.getRepaymentFrequency() != null && CommonUtil.convertObjToDouble(sanctionFacilityTo.getRepaymentFrequency()).doubleValue() == 30) {
                    setCboRenewalRepayFreq("Monthly");
                } else if (sanctionFacilityTo.getRepaymentFrequency() != null && CommonUtil.convertObjToDouble(sanctionFacilityTo.getRepaymentFrequency()).doubleValue() == 15) {
                    setCboRenewalRepayFreq("Fortnight");
                } else{// if (sanctionFacilityTo.getRepaymentFrequency() != null && CommonUtil.convertObjToDouble(sanctionFacilityTo.getRepaymentFrequency()).doubleValue() == 0) {// Added by nithya on 03-07-2019 for KD-546 New Gold Loan -45 days maturity type                   
                    setCboRenewalRepayFreq("USER_DEFINED_GOLD_LOAN");//Added by nithya on 06-07-2019 for KD 546 - New Gold Loan -45 days maturity type
                }



//            setCboRenewalRepayFreq(CommonUtil.convertObjToStr(sanctionFacilityTo.getRepaymentFrequency()));
//            cboRenewalRepayFreq();
                setTdtRenewalDemandPromNoteDate(CommonUtil.convertObjToStr(termLoanFacilityTo.getDemandPromDt()));
                setTdtRenewalDemandPromNoteExpDate(CommonUtil.convertObjToStr(termLoanFacilityTo.getDemandPromExpdt()));
                setTxtRenewalInter(CommonUtil.convertObjToStr(termLoanInterestTo.getInterest()));
                setTxtRenewalPenalInter(CommonUtil.convertObjToStr(termLoanInterestTo.getPenalInterest()));

            }

            if (dataMap.containsKey("NEW_FACILITY_DETAILS") && dataMap.get("NEW_FACILITY_DETAILS") != null) {
                datacollectionMap = (HashMap) dataMap.get("NEW_FACILITY_DETAILS");
                list = (ArrayList) datacollectionMap.get("TermLoanSecurityTO");
                if (list != null && list.size() > 0) {
                    GoldLoanSecurityTO termLoanSecurityTO = (GoldLoanSecurityTO) list.get(0);

//            rdoRenewalPriority();
//            setRdoRenewalNonPriority();
//            cbmRenewalPurposeOfLoan();
//            txtRenewalSanctionRemarks();
//            txtRenewalSanctionRemarks();
                    termLoanSecurityOB.populateRenewalSecurityData(termLoanSecurityTO);
//            setTxtRenewalGrossWeight(CommonUtil.convertObjToStr(termLoanSecurityTO.getGrossWeight()));
//            setTxtRenewalNetWeight(CommonUtil.convertObjToStr(termLoanSecurityTO.getGrossWeight()));
//            cboRenewalPurityOfGold();
//            setTxtRenewalMarketRate(CommonUtil.convertObjToStr(termLoanSecurityTO.getMarketRate()));
//            setTxtRenewalSecurityValue(CommonUtil.convertObjToStr(termLoanSecurityTO.getSecurityValue()));
//            setTxtRenewalAreaParticular(CommonUtil.convertObjToStr(termLoanSecurityTO.getParticulars()));
//            setTxtRenewalMargin(CommonUtil.convertObjToStr(termLoanSecurityTO.getMargin()));
//            setTxtRenewalMarginAmt(CommonUtil.convertObjToStr(termLoanSecurityTO.getMarginAmt()));
//            setTxtRenewalEligibleLoan(CommonUtil.convertObjToStr(termLoanSecurityTO.getEligibleLoanAmt()));
//            cboRenewalAppraiserId();
                }
            }
        }
    }

    /**
     * To perform the appropriate operation
     *
     * @param saveMode is the integer value to know which save button pressed
     */
    public void doAction(NomineeOB objNomineeOB, int saveMode) {
        log.info("In doAction...");
        try {
            //If actionType such as NEW, EDIT, DELETE, then proceed
            if (actionType != ClientConstants.ACTIONTYPE_CANCEL) {
                //If actionType has got propervalue then doActionPerform, else throw error
                if (getCommand() != null || getAuthorizeMap() != null) {
                    log.info("before doActionPerform...");
                    doActionPerform(objNomineeOB, saveMode);
                } else {
                    log.info("In doAction()-->getCommand() is null:");
                }
            } else {
                log.info("In doAction()-->actionType is null:");
            }
        } catch (Exception e) {
            //System.out.println("e--------->" + e);
            parseException.logException(e, true);
            setResult(ClientConstants.ACTIONTYPE_FAILED);
            log.info("Error in doAction():" + e);
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
        termLoanClassificationOB.setCommand(command);
//        termLoanOtherDetailsOB.setCommand(command);
        return command;
    }

    //interestslab rate max amount hit or not
    public boolean checkMaxAmountRange(String toDate) {
        if (getStrACNumber().length() > 0) {
            StringBuffer buf = termLoanInterestOB.maxAmountRangeCheck(toDate);
            if (buf != null && buf.length() > 0) {
                ClientUtil.showMessageWindow(buf.toString());
                return false;
            }
        }
        return true;
    }

    private void doActionPerform(NomineeOB objNomineeOB, int saveMode) throws Exception {
        log.info("doActionPerform");
        setTxtSanctionSlNo(String.valueOf("1"));
        insertFacilityDetails(getTxtSanctionSlNo(), Integer.parseInt(getTxtSanctionSlNo()));
        HashMap objTermLoanFacilityTOMap = setTermLoanFacilitySingleRecord();// setTermLoanFacility();
        HashMap objSantionDetailsTOMap = setTermLoanSanctionSingleRecord();//setTermLoanSanction()
        HashMap objTermLoanSanctionTOMap = (HashMap) objSantionDetailsTOMap.get(SANCTION);
        HashMap objTermLoanSanctionFacilityTOMap = (HashMap) objSantionDetailsTOMap.get(SANCTION_FACILITY);
        HashMap objTermLoanJointAcctTOMap = termLoanBorrowerOB.setTermLoanJointAcct();
        if (getNewCustIdNo().length() > 0) {
            termLoanBorrowerOB.setTxtCustID(CommonUtil.convertObjToStr(getNewCustIdNo()));
        }
        TermLoanBorrowerTO objTermLoanBorrowerTO = termLoanBorrowerOB.setTermLoanBorrower("", false);
//        TermLoanCompanyTO objTermLoanCompanyTO = termLoanCompanyOB.setTermLoanCompany();
        //        HashMap objTermLoanAuthorizedSignatoryTOList = termLoanAuthorizedSignatoryOB.setAuthorizedSignatory();
        //        HashMap objTermLoanAuthorizedSignatoryInstructionTOList = termLoanAuthorizedSignatoryInstructionOB.setAuthorizedSignatoryInstruction();
        //        HashMap objTermLoanPowerAttorneyTOMap = termLoanPoAOB.setTermLoanPowerAttorney();
        //        HashMap objExtnFacilityDetails=agriSubSidyOB.setAgriSubSidyTo();//dontdelete
        //        HashMap objTermLoanSubLimitTOList =  agriSubLimitOB.setAgriSubLimitTo();
        //        HashMap objSettlementTOMap = settlementOB.setSettlement(saveMode);//dontdelete
        //        HashMap objSettlementBankTOMap = settlementOB.setSettlementBank(saveMode);//dontdelete
        //        HashMap objAcctTransTOMap = actTransOB.setAcctTrans();//dontdelete
        //        HashMap objTermLoanSecurityTOMap = termLoanSecurityOB.setTermLoanSecurity();
        //        HashMap objTermLoanAdditionalSanMap=termLoanAdditionalSanctionOB.setAdditionalSanction();
        HashMap repaymentInstallmentMap = new HashMap();
        //        if(termLoanRepaymentOB.getRepaymentAll() != null){
        //            termLoanRepaymentOB.setTermLoanRepayment();
        HashMap objRepaymentInstallmentMap = termLoanRepaymentOB.setTermLoanRepayment();
        //            HashMap objTermLoanInstallmentTOMap = (HashMap) termLoanRepaymentOB.setRepayInstallmentMap.get(INSTALLMENT_DETAILS);
        //            objRepaymentInstallmentAllMap = (LinkedHashMap)((HashMap)getInstallmentAllMap()).get("ALL_RECORDS");
        //            repaymentInstallmentMap.put("INSTALLMENT_DETAILS",objRepaymentInstallmentAllMap);
        //        }
        HashMap objTermLoanRepaymentTOMap = setTermLoanRepayment();
        //System.out.println("objTermLoanRepaymentTOMap" + objTermLoanRepaymentTOMap);
        //System.out.println("printsavemode" + objRepaymentInstallmentMap);
        //        if(saveMode==1){
        //        //System.out.println("checkthis"+termLoanRepaymentOB.getRepaymentEachRecord().get(INSTALLMENT_ALL_DETAILS));
        //        objRepaymentInstallmentAllMap=(LinkedHashMap)((HashMap)termLoanRepaymentOB.getRepaymentEachRecord()).get(INSTALLMENT_ALL_DETAILS);
        //        //System.out.println("thisisforcheckob"+objRepaymentInstallmentAllMap);
        //        }
        //        HashMap objTermLoanRepaymentTOMap = (HashMap) objRepaymentInstallmentMap.get(REPAYMENT_DETAILS);
        HashMap objTermLoanInstallmentTOMap = (HashMap) objRepaymentInstallmentMap.get(INSTALLMENT_DETAILS);
        HashMap objTermLoanInstallMultIntMap = (HashMap) objRepaymentInstallmentMap.get(INSTALLMULTINT_DETAILS);
        HashMap objTermLoanInstalltransactionMap = null;
        if (objRepaymentInstallmentMap.containsKey("INT_TRANSACTION_REPAYMENT")) {
            objTermLoanInstalltransactionMap = (HashMap) objRepaymentInstallmentMap.get("INT_TRANSACTION_REPAYMENT");
        }

//        HashMap objTermLoanGuarantorTOMap = termLoanGuarantorOB.setTermLoanGuarantor();
//        HashMap objTermLoanInstitGuarantorTOMap = termLoanGuarantorOB.setTermInsititLoanGuarantor();
        HashMap objTermLoanDocumentTOMap = termLoanDocumentDetailsOB.setTermLoanDocument();
        HashMap objTermLoanInterestTOMap = setTermLoanInterest("", false);
        TermLoanClassificationTO objTermLoanClassificationTO = termLoanClassificationOB.setTermLoanClassification();
        //System.out.println("isRenewalYesNo() : "+isRenewalYesNo());
        GoldLoanSecurityTO objTermLoanSecurityTO;
        if (isRenewalYesNo()) {
             objTermLoanSecurityTO = termLoanSecurityOB.setGoldLoanRenewalSecurityTO();
        }else{
             objTermLoanSecurityTO = termLoanSecurityOB.setGoldLoanSecurityTO();
        }
        objTermLoanSecurityTO.setCommand(getCommand());
//        TermLoanOtherDetailsTO objTermLoanOtherDetailsTO = termLoanOtherDetailsOB.setTermLoanOtherDetails();
        if (getCommand().equals(DELETE)) {
            objTermLoanBorrowerTO.setCommand(DELETE);
//            objTermLoanCompanyTO.setCommand(DELETE);
        } else {
            objTermLoanBorrowerTO.setCommand(getCommand());
//            objTermLoanCompanyTO.setCommand(getCommand());
        }
        //System.out.println("getCommand(): " + getCommand());
        if (getCommand().equals(INSERT)) {
            objTermLoanBorrowerTO.setStatus(CommonConstants.STATUS_CREATED);
//            objTermLoanCompanyTO.setStatus(CommonConstants.STATUS_CREATED);
            objTermLoanSecurityTO.setStatus(CommonConstants.STATUS_CREATED);
        } else if (getCommand().equals(UPDATE)) {
            objTermLoanBorrowerTO.setStatus(CommonConstants.STATUS_MODIFIED);
//            objTermLoanCompanyTO.setStatus(CommonConstants.STATUS_MODIFIED);
            objTermLoanSecurityTO.setStatus(CommonConstants.STATUS_MODIFIED);
            objTermLoanSecurityTO.setAcctNum(getStrACNumber());
        } else if (getCommand().equals(DELETE)) {
            objTermLoanBorrowerTO.setStatus(CommonConstants.STATUS_DELETED);
//            objTermLoanCompanyTO.setStatus(CommonConstants.STATUS_DELETED);
            objTermLoanSecurityTO.setStatus(CommonConstants.STATUS_DELETED);
            objTermLoanSecurityTO.setAcctNum(getStrACNumber());
        }
        objTermLoanClassificationTO.setCommand(termLoanClassificationOB.getClassifiDetails());
//        objTermLoanOtherDetailsTO.setCommand(termLoanOtherDetailsOB.getOtherDetailsMode());
        if (termLoanClassificationOB.getClassifiDetails().equals(CommonConstants.TOSTATUS_INSERT)) {
            objTermLoanClassificationTO.setStatus(CommonConstants.STATUS_CREATED);
        } else if (termLoanClassificationOB.getClassifiDetails().equals(CommonConstants.TOSTATUS_UPDATE)) {
            objTermLoanClassificationTO.setStatus(CommonConstants.STATUS_MODIFIED);
        } else if (termLoanClassificationOB.getClassifiDetails().equals(CommonConstants.TOSTATUS_DELETE)) {
            objTermLoanClassificationTO.setStatus(CommonConstants.STATUS_DELETED);
        }

//        if (termLoanOtherDetailsOB.getOtherDetailsMode().equals(CommonConstants.TOSTATUS_INSERT)){
//            objTermLoanOtherDetailsTO.setStatus(CommonConstants.STATUS_CREATED);
//        }else if (termLoanOtherDetailsOB.getOtherDetailsMode().equals(CommonConstants.TOSTATUS_UPDATE)){
//            objTermLoanOtherDetailsTO.setStatus(CommonConstants.STATUS_MODIFIED);
//        }else if (termLoanOtherDetailsOB.getOtherDetailsMode().equals(CommonConstants.TOSTATUS_DELETE)){
//            objTermLoanOtherDetailsTO.setStatus(CommonConstants.STATUS_DELETED);
//        }

        HashMap data = new HashMap();
        data.put("PHOTO", this.photoByteArray);// Added by nithya on 29-10-2019 for KD-763	Need Gold ornaments photo saving option
        if (isRenewalYesNo()) {
            data.put("RENEWAL_GOLDLOAN", renewalGoldLoan(objNomineeOB));
            data.put("RENEWAL_PARAM", getRenewalParamMap());
            data.put("RENEWAL_PHOTO", this.renewalPhotoByteArray);// Added by nithya on 29-10-2019 for KD-763	Need Gold ornaments photo saving option
        }else{
            if (getServiceTax_Map() != null && getServiceTax_Map().size() > 0) {
                data.put("serviceTaxDetails", getServiceTax_Map());
                data.put("serviceTaxDetailsTO", setServiceTaxDetails());
            } 
        }
        //System.out.println("RENEWAL_PARAM" + getRenewalParamMap());
        data.put("TermLoanJointAcctTO", objTermLoanJointAcctTOMap);
        data.put("TermLoanBorrowerTO", objTermLoanBorrowerTO);
//        data.put("TermLoanCompanyTO",objTermLoanCompanyTO);
        // Set the Networth Value and As on date
//        data.put("NETWORTH_DETAILS", termLoanCompanyOB.getNetworthDetails());
        //        data.put("AuthorizedSignatoryTO",objTermLoanAuthorizedSignatoryTOList);
        //        data.put("AuthorizedSignatoryInstructionTO",objTermLoanAuthorizedSignatoryInstructionTOList);
        //        data.put("PowerAttorneyTO", objTermLoanPowerAttorneyTOMap);
        data.put("TermLoanSanctionTO", objTermLoanSanctionTOMap);
        data.put("TermLoanSanctionFacilityTO", objTermLoanSanctionFacilityTOMap);
        data.put("TermLoanFacilityTO", objTermLoanFacilityTOMap);
        //        data.put("TermLoanSecurityTO", objTermLoanSecurityTOMap);
        data.put("TermLoanSecurityTO", objTermLoanSecurityTO);
        //System.out.println("objTermLoanRepaymentTOMap#$$$$$" + objTermLoanRepaymentTOMap);
        data.put("TermLoanRepaymentTO", objTermLoanRepaymentTOMap);
        data.put("TermLoanInstallmentTO", objTermLoanInstallmentTOMap);
        data.put("TermLoanInstallMultIntTO", objTermLoanInstallMultIntMap);
//        data.put("TermLoanGuarantorTO", objTermLoanGuarantorTOMap);
//        data.put("TermLoanInstitGuarantorTO", objTermLoanInstitGuarantorTOMap);
        data.put("TermLoanInterestTO", objTermLoanInterestTOMap);
        data.put("TermLoanDocumentTO", objTermLoanDocumentTOMap);
        data.put("TermLoanClassificationTO", objTermLoanClassificationTO);
        data.put("APPRAISER_ID", termLoanSecurityOB.getTxtAppraiserId());
        data.put("PRODUCT_ID", getLoadingProdId());
        data.put("OLD_PRODUCT_ID", getRnwLoadingProdId());
        if (objSMSSubscriptionTO != null) {
            data.put("SMSSubscriptionTO", objSMSSubscriptionTO);
        }

        //        data.put("TermLoanAdditionalSanctionTO",objTermLoanAdditionalSanMap);
        //        data.put("TermLoanDisbursementTO",objTermLoanSubLimitTOList);
        //        data.put("ActTransTO", objAcctTransTOMap);//dontdelete
        //        data.put("SettlementTO", objSettlementTOMap);//dontdelete
        //        data.put("SettlementBankTO", objSettlementBankTOMap);//dontdelete

        //        if(objExtnFacilityDetails !=null && objExtnFacilityDetails.size()>0)
        //            data.put("TermLoanExtenFacilityDetailsTO",objExtnFacilityDetails);//dontdelete
        if (objTermLoanInstalltransactionMap != null) {
            data.put("INT_TRANSACTION_REPAYMENT", objTermLoanInstalltransactionMap);
        }
        if (partReject.length() > 0) {
            data.put("PARTIALLY_REJECT", "PARTIALLY_REJECT");
        }
        if (advanceLiablityMap != null && (!advanceLiablityMap.isEmpty())) {
            data.put("AdvancesLiablityExceedLimit", advanceLiablityMap);
        }
        if (getCommand().equals("UPDATE")) {
            if (termLoanClassificationOB.getListAssetStatus() != null) {
                data.put("NPAHISTORY", termLoanClassificationOB.getListAssetStatus());
            }
            data.put("ACCOUNT_NUMBER", getStrACNumber());
        }
        //        if(saveMode==1){
        data.put("TermRepaymentInstallmentAllTO", objRepaymentInstallmentAllMap);
        //        //System.out.println("goingDAO"+data.get("TermRepaymentInstallmentAllTO"));
        //        }

        String facilityType = CommonUtil.convertObjToStr(getCbmTypeOfFacility().getKeyForSelected());
        if (facilityType.length() > 0 && facilityType.equals("CC") || facilityType.equals("OD")) {
//            data.put("TermLoanOtherDetailsTO", objTermLoanOtherDetailsTO);
        } else {
//            data.put("TermLoanOtherDetailsTO", null);
        }

        if (getCommand().equals(CommonConstants.TOSTATUS_INSERT)) {
            if (loanType.equals("LTD")) {
                data.put("LTD", depositCustDetMap);
            }
        }
        if (loanType.equals("LTD")) {
            data.put("LOAN_TYPE", "LTD");
        } else {
            data.put("LOAN_TYPE", "");
        }
        if (getAuthorizeMap() != null) {
            data.put(CommonConstants.AUTHORIZEMAP, getAuthorizeMap());
        }
        if (getTransactionMap() != null) {
            data.put("Transaction Details Data", getTransactionMap());
        }
        if (getChargelst() != null) {
            data.put("Charge List Data", getChargelst());
        }
//        ADDED HERE BY NIKHIL
        data.put("AccountNomineeTO", objNomineeOB.getNomimeeList());
        data.put("AccountNomineeDeleteTO", objNomineeOB.getDeleteNomimeeList());       
        data.put(CommonConstants.MODULE, getModule());
        data.put(CommonConstants.SCREEN, getScreen());
        data.put("UI_PRODUCT_TYPE", "TL");          
        HashMap proxyResultMap = new HashMap();
        proxyResultMap = proxy.execute(data, operationMap);  //TEST PURPOSE COMMENTED
        setProxyReturnMap(proxyResultMap);
        //System.out.println("proxyresultmap#$$$$$" + proxyResultMap);
        if (proxyResultMap != null) {
            if (proxyResultMap.containsKey("ACCTNO") && loanType.equals("OTHERS")) {
                ClientUtil.showMessageWindow("Loan Account No. : " + proxyResultMap.get("ACCTNO"));
            }
            String accNo = CommonUtil.convertObjToStr(proxyResultMap.get("ACCTNO"));
            setStrACNumber(accNo);
            if (proxyResultMap.size() > 0) {
                if (proxyResultMap.containsKey("LIENNO")) {
                    String lienNo = CommonUtil.convertObjToStr(proxyResultMap.get("LIENNO"));
                    loanACNo = CommonUtil.convertObjToStr(proxyResultMap.get("ACCTNO"));
                    //                setStrACNumber(loanACNo);

                    HashMap lienMap = new HashMap();
                    if (depositCustDetMap != null) {
                        ClientUtil.showMessageWindow("Lien Marked for this Loan\nLien No : " + lienNo
                                + "\nThe Lien will be opened for Edit...");

                        lienMap.put("CUSTOMER_NAME", depositCustDetMap.get("CUSTOMER"));
                        lienMap.put("PRODID", depositCustDetMap.get("PRODUCT ID"));
                        lienMap.put("DEPOSIT_ACT_NUM", depositCustDetMap.get("DEPOSIT_NO"));
                        lienMap.put("SUBNO", CommonUtil.convertObjToInt(depositCustDetMap.get("DEPOSIT_SUB_NO")));
                        lienMap.put("CUST_ID", depositCustDetMap.get("CUST_ID"));
                        DepositLienUI depLienUI = new DepositLienUI();
                        depLienUI.setLoanLienDisable(true);
                        depLienUI.setViewType(ClientConstants.ACTIONTYPE_EDIT);
                        depLienUI.fillData(lienMap);
                        com.see.truetransact.ui.TrueTransactMain.showScreen(depLienUI);
                        setLienChanged(depLienUI.isLienChanged());
                        if (isLienChanged()) {
                            depLienUI.dispose();
                            depLienUI = null;
                        }
                    } else {
                        ClientUtil.showMessageWindow("Lien Marked for this Loan Lien No  is: " + lienNo);
                    }
                }
            }
        }
        log.info("doActionPerform is over...");
        setResult(actionType);
        if (saveMode == 1) {
            actionType = ClientConstants.ACTIONTYPE_CANCEL;
        } else if (saveMode == 2 || saveMode == 3 || saveMode == 4) {
            actionType = ClientConstants.ACTIONTYPE_EDIT;
        }

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
            termLoanBorrowerOB.changeStatusJointAcct();
            // Authorized Signatory Tab
            //            termLoanAuthorizedSignatoryOB.changeStatusAuthorizedSignatory(getResult());
            // Authorized Signatory Instruction Tab
            //            termLoanAuthorizedSignatoryInstructionOB.changeStatusAuthorizedSignatoryInstruction(getResult());
            // Power of Attorney DetaiLOANS_SANCTION_DETAILSls
            //            termLoanPoAOB.changeStatusPoA(getResult());
            // Sanction Details
            if (getResult() != 2) {
                //If the Main Save Button pressed
                tableUtilSanction_Main.getRemovedValues().clear();
                tableUtilSanction_Facility.getRemovedValues().clear();
            }
            keySet = sanctionMainAll.keySet();
            objKeySet = (Object[]) keySet.toArray();
            // To change the Insert command to Update after Save Buttone Pressed
            // Sanction Details
            for (int i = keySet.size() - 1, j = 0; i >= 0; --i, ++j) {
                oneRecord = (HashMap) sanctionMainAll.get(objKeySet[j]);
                sanctionTabFacility = (LinkedHashMap) oneRecord.get(SANCTION_FACILITY_ALL);
                sanctionKeySet = sanctionTabFacility.keySet();
                objSanctionKeySet = (Object[]) sanctionKeySet.toArray();
                if (oneRecord.get(COMMAND).equals(INSERT)) {
                    // If the status is in Insert Mode then change the mode to Update
                    oneRecord.put(COMMAND, UPDATE);
                }
                // To change the Insert command to Update after Save Buttone Pressed
                // Sanction Facility Details
                for (int k = sanctionKeySet.size() - 1, m = 0; k >= 0; --k, ++m) {
                    oneRec = (HashMap) sanctionTabFacility.get(objSanctionKeySet[m]);
                    if (oneRec.get(COMMAND).equals(INSERT)) {
                        // If the status is in Insert Mode then change the mode to Update
                        oneRec.put(COMMAND, UPDATE);
                        sanctionTabFacility.put(objSanctionKeySet[m], oneRec);
                    }
                }
                oneRecord.put(SANCTION_FACILITY_ALL, sanctionTabFacility);
                sanctionMainAll.put(objKeySet[j], oneRecord);
                oneRecord = null;
            }
            tableUtilSanction_Main.setAllValues(sanctionMainAll);

            // Facility Details Tab
            if (getResult() != 2) {
                //If the Main Save Button pressed
                removedFaccilityTabValues.clear();
            }
            keySet = facilityAll.keySet();
            objKeySet = (Object[]) keySet.toArray();
            // To change the Insert command to Update after Save Buttone Pressed
            // Facility Details
            for (int i = keySet.size() - 1, j = 0; i >= 0; --i, ++j) {
                oneRecord = (HashMap) facilityAll.get(objKeySet[j]);
                if (oneRecord.get(COMMAND).equals(INSERT)) {
                    // If the status is in Insert Mode then change the mode to Update
                    oneRecord.put(COMMAND, UPDATE);
                    facilityAll.put(objKeySet[j], oneRecord);
                }
                oneRecord = null;
            }
            // Security Details
            termLoanSecurityOB.changeStatusSecurity(getResult());
            // Repayment Details
            termLoanRepaymentOB.changeStatusRepay(getResult());
            // Guarantor Details
//            termLoanGuarantorOB.changeStatusGuarantor(getResult());
            // Document Details
            termLoanDocumentDetailsOB.changeStatusDocument(getResult());
            // Interest Details
            termLoanInterestOB.changeStatusInterest(getResult());
            termLoanClassificationOB.setClassifiDetails(UPDATE);
//            termLoanOtherDetailsOB.setOtherDetailsMode(UPDATE);
            keySet = null;
            sanctionKeySet = null;
            objKeySet = null;
            objSanctionKeySet = null;
            sanctionTabFacility = null;
            oneRec = null;
            objTermLoanJointAcctTOMap = null;
            objTermLoanBorrowerTO = null;
//            objTermLoanCompanyTO = null;
            //            objTermLoanAuthorizedSignatoryTOList = null;
            //            objTermLoanAuthorizedSignatoryInstructionTOList = null;
            //            objTermLoanPowerAttorneyTOMap = null;
            objSantionDetailsTOMap = null;
            objTermLoanSanctionTOMap = null;
            objTermLoanSanctionFacilityTOMap = null;
            objTermLoanFacilityTOMap = null;
            //            objTermLoanSecurityTOMap = null;
            objRepaymentInstallmentMap = null;
            //            objTermLoanRepaymentTOMap = null;
            objTermLoanInstallmentTOMap = null;
//            objTermLoanGuarantorTOMap = null;
//            objTermLoanInstitGuarantorTOMap=null;
            objTermLoanDocumentTOMap = null;
            objTermLoanInstallMultIntMap = null;
            objTermLoanInterestTOMap = null;
            objTermLoanClassificationTO = null;
//            objTermLoanOtherDetailsTO = null;
            objRepaymentInstallmentAllMap = null;
            data = null;
        }
    }

    private HashMap renewalGoldLoan(NomineeOB objNomineeOB) throws Exception {
        HashMap renewalMap = new HashMap();
        setTxtSanctionSlNo(String.valueOf("1"));
        insertRenewalFacilityDetails(getTxtSanctionSlNo(), Integer.parseInt(getTxtSanctionSlNo()));
        HashMap objTermLoanFacilityTOMap = setTermLoanRenewalFacilitySingleRecord();// setTermLoanFacility();
        HashMap objSantionDetailsTOMap = setTermLoanRenewalSanctionSingleRecord();//setTermLoanSanction()
        HashMap objTermLoanSanctionTOMap = (HashMap) objSantionDetailsTOMap.get(SANCTION);
        HashMap objTermLoanSanctionFacilityTOMap = (HashMap) objSantionDetailsTOMap.get(SANCTION_FACILITY);
        HashMap objTermLoanJointAcctTOMap = termLoanBorrowerOB.setTermLoanJointAcct();
        if (getNewCustIdNo().length() > 0) {
            termLoanBorrowerOB.setTxtCustID(CommonUtil.convertObjToStr(getNewCustIdNo()));
        }
        TermLoanBorrowerTO objTermLoanBorrowerTO = termLoanBorrowerOB.setTermLoanBorrower(CommonUtil.convertObjToStr(getLblRenewalAcctNo_Sanction_Disp()), renewalYesNo);
//        TermLoanCompanyTO objTermLoanCompanyTO = termLoanCompanyOB.setTermLoanCompany();
        //        HashMap objTermLoanAuthorizedSignatoryTOList = termLoanAuthorizedSignatoryOB.setAuthorizedSignatory();
        //        HashMap objTermLoanAuthorizedSignatoryInstructionTOList = termLoanAuthorizedSignatoryInstructionOB.setAuthorizedSignatoryInstruction();
        //        HashMap objTermLoanPowerAttorneyTOMap = termLoanPoAOB.setTermLoanPowerAttorney();
        //        HashMap objExtnFacilityDetails=agriSubSidyOB.setAgriSubSidyTo();//dontdelete
        //        HashMap objTermLoanSubLimitTOList =  agriSubLimitOB.setAgriSubLimitTo();
        //        HashMap objSettlementTOMap = settlementOB.setSettlement(saveMode);//dontdelete
        //        HashMap objSettlementBankTOMap = settlementOB.setSettlementBank(saveMode);//dontdelete
        //        HashMap objAcctTransTOMap = actTransOB.setAcctTrans();//dontdelete
        //        HashMap objTermLoanSecurityTOMap = termLoanSecurityOB.setTermLoanSecurity();
        //        HashMap objTermLoanAdditionalSanMap=termLoanAdditionalSanctionOB.setAdditionalSanction();
        HashMap repaymentInstallmentMap = new HashMap();
        //        if(termLoanRepaymentOB.getRepaymentAll() != null){
        //            termLoanRepaymentOB.setTermLoanRepayment();
        HashMap objRepaymentInstallmentMap = termLoanRepaymentOB.setTermLoanRenewalRepayment(lblRenewalAcctNo_Sanction_Disp, renewalYesNo);
        //System.out.println("renewal instmap" + objRepaymentInstallmentMap);

        //            HashMap objTermLoanInstallmentTOMap = (HashMap) termLoanRepaymentOB.setRepayInstallmentMap.get(INSTALLMENT_DETAILS);
        //            objRepaymentInstallmentAllMap = (LinkedHashMap)((HashMap)getInstallmentAllMap()).get("ALL_RECORDS");
        //            repaymentInstallmentMap.put("INSTALLMENT_DETAILS",objRepaymentInstallmentAllMap);
        //        }
        HashMap objTermLoanRepaymentTOMap = setTermLoanRenewalRepaymentSchedule();
//        //System.out.println("printsavemode"+saveMode);
        //        if(saveMode==1){
        //        //System.out.println("checkthis"+termLoanRepaymentOB.getRepaymentEachRecord().get(INSTALLMENT_ALL_DETAILS));
        //        objRepaymentInstallmentAllMap=(LinkedHashMap)((HashMap)termLoanRepaymentOB.getRepaymentEachRecord()).get(INSTALLMENT_ALL_DETAILS);
        //        //System.out.println("thisisforcheckob"+objRepaymentInstallmentAllMap);
        //        }
        //        HashMap objTermLoanRepaymentTOMap = (HashMap) objRepaymentInstallmentMap.get(REPAYMENT_DETAILS);
        HashMap objTermLoanInstallmentTOMap = (HashMap) objRepaymentInstallmentMap.get(INSTALLMENT_DETAILS);
        HashMap objTermLoanInstallMultIntMap = (HashMap) objRepaymentInstallmentMap.get(INSTALLMULTINT_DETAILS);
        HashMap objTermLoanInstalltransactionMap = null;
        if (objRepaymentInstallmentMap.containsKey("INT_TRANSACTION_REPAYMENT")) {
            objTermLoanInstalltransactionMap = (HashMap) objRepaymentInstallmentMap.get("INT_TRANSACTION_REPAYMENT");
        }

//        HashMap objTermLoanGuarantorTOMap = termLoanGuarantorOB.setTermLoanGuarantor();
//        HashMap objTermLoanInstitGuarantorTOMap = termLoanGuarantorOB.setTermInsititLoanGuarantor();
        HashMap objTermLoanDocumentTOMap = termLoanDocumentDetailsOB.setTermLoanDocument();
        HashMap objTermLoanInterestTOMap = setTermLoanInterest(CommonUtil.convertObjToStr(getLblRenewalAcctNo_Sanction_Disp()), renewalYesNo);
        TermLoanClassificationTO objTermLoanClassificationTO = termLoanClassificationOB.setTermLoanClassification();
        GoldLoanSecurityTO objTermLoanSecurityTO = termLoanSecurityOB.setGoldLoanRenewalSecurityTO();

//        TermLoanOtherDetailsTO objTermLoanOtherDetailsTO = termLoanOtherDetailsOB.setTermLoanOtherDetails();
        if (CommonUtil.convertObjToStr(lblRenewalAcctNo_Sanction_Disp).length() == 0) {
            objTermLoanBorrowerTO.setCommand("INSERT");
            objTermLoanSecurityTO.setCommand("INSERT");
//            objTermLoanCompanyTO.setCommand(DELETE);
        } else {
            objTermLoanBorrowerTO.setCommand("UPDATE");
            objTermLoanSecurityTO.setCommand("UPDATE");
//            objTermLoanCompanyTO.setCommand(getCommand());
        }
        //System.out.println("getCommand(): " + getCommand());
        if (CommonUtil.convertObjToStr(lblRenewalAcctNo_Sanction_Disp).length() == 0) {
            objTermLoanBorrowerTO.setStatus(CommonConstants.STATUS_CREATED);
//            objTermLoanCompanyTO.setStatus(CommonConstants.STATUS_CREATED);
            objTermLoanSecurityTO.setStatus(CommonConstants.STATUS_CREATED);
            objTermLoanClassificationTO.setCommand("INSERT");
            objTermLoanClassificationTO.setStatus(CommonConstants.STATUS_CREATED);

        } else if (CommonUtil.convertObjToStr(lblRenewalAcctNo_Sanction_Disp).length() > 0) {
            objTermLoanBorrowerTO.setStatus(CommonConstants.STATUS_MODIFIED);
//            objTermLoanCompanyTO.setStatus(CommonConstants.STATUS_MODIFIED);
            objTermLoanSecurityTO.setStatus(CommonConstants.STATUS_MODIFIED);
            objTermLoanSecurityTO.setAcctNum(lblRenewalAcctNo_Sanction_Disp);
            objTermLoanClassificationTO.setCommand("UPDATE");
            objTermLoanClassificationTO.setStatus(CommonConstants.STATUS_MODIFIED);
        }
        objTermLoanClassificationTO.setAcctNum(lblRenewalAcctNo_Sanction_Disp);
//        else if (getCommand().equals(DELETE)){
//            objTermLoanBorrowerTO.setStatus(CommonConstants.STATUS_DELETED);
////            objTermLoanCompanyTO.setStatus(CommonConstants.STATUS_DELETED);
//            objTermLoanSecurityTO.setStatus(CommonConstants.STATUS_DELETED);
//            objTermLoanSecurityTO.setAcctNum(getStrACNumber());
//        }
//        objTermLoanClassificationTO.setCommand(termLoanClassificationOB.getClassifiDetails());
//        objTermLoanOtherDetailsTO.setCommand(termLoanOtherDetailsOB.getOtherDetailsMode());
//        if (termLoanClassificationOB.getClassifiDetails().equals(CommonConstants.TOSTATUS_INSERT)){
//            objTermLoanClassificationTO.setStatus(CommonConstants.STATUS_CREATED);
//        }else if (termLoanClassificationOB.getClassifiDetails().equals(CommonConstants.TOSTATUS_UPDATE)){
//            objTermLoanClassificationTO.setStatus(CommonConstants.STATUS_MODIFIED);
//        }else if (termLoanClassificationOB.getClassifiDetails().equals(CommonConstants.TOSTATUS_DELETE)){
//            objTermLoanClassificationTO.setStatus(CommonConstants.STATUS_DELETED);
//        }

//        if (termLoanOtherDetailsOB.getOtherDetailsMode().equals(CommonConstants.TOSTATUS_INSERT)){
//            objTermLoanOtherDetailsTO.setStatus(CommonConstants.STATUS_CREATED);
//        }else if (termLoanOtherDetailsOB.getOtherDetailsMode().equals(CommonConstants.TOSTATUS_UPDATE)){
//            objTermLoanOtherDetailsTO.setStatus(CommonConstants.STATUS_MODIFIED);
//        }else if (termLoanOtherDetailsOB.getOtherDetailsMode().equals(CommonConstants.TOSTATUS_DELETE)){
//            objTermLoanOtherDetailsTO.setStatus(CommonConstants.STATUS_DELETED);
//        }

        HashMap data = new HashMap();
        data.put("TermLoanJointAcctTO", objTermLoanJointAcctTOMap);
        data.put("TermLoanBorrowerTO", objTermLoanBorrowerTO);
//        data.put("TermLoanCompanyTO",objTermLoanCompanyTO);
        // Set the Networth Value and As on date
//        data.put("NETWORTH_DETAILS", termLoanCompanyOB.getNetworthDetails());
        //        data.put("AuthorizedSignatoryTO",objTermLoanAuthorizedSignatoryTOList);
        //        data.put("AuthorizedSignatoryInstructionTO",objTermLoanAuthorizedSignatoryInstructionTOList);
        //        data.put("PowerAttorneyTO", objTermLoanPowerAttorneyTOMap);
        data.put("TermLoanSanctionTO", objTermLoanSanctionTOMap);
        data.put("TermLoanSanctionFacilityTO", objTermLoanSanctionFacilityTOMap);
        data.put("TermLoanFacilityTO", objTermLoanFacilityTOMap);
        //        data.put("TermLoanSecurityTO", objTermLoanSecurityTOMap);
        data.put("TermLoanSecurityTO", objTermLoanSecurityTO);
        data.put("TermLoanRepaymentTO", objTermLoanRepaymentTOMap);
        data.put("TermLoanInstallmentTO", objTermLoanInstallmentTOMap);
        data.put("TermLoanInstallMultIntTO", objTermLoanInstallMultIntMap);
//        data.put("TermLoanGuarantorTO", objTermLoanGuarantorTOMap);
//        data.put("TermLoanInstitGuarantorTO", objTermLoanInstitGuarantorTOMap);
        data.put("TermLoanInterestTO", objTermLoanInterestTOMap);
        data.put("TermLoanDocumentTO", objTermLoanDocumentTOMap);
        data.put("TermLoanClassificationTO", objTermLoanClassificationTO);
        data.put("APPRAISER_ID", termLoanSecurityOB.getTxtAppraiserId());
        data.put("PRODUCT_ID", getLoadingProdId());
        if (objSMSSubscriptionTO != null) {
            data.put("SMSSubscriptionTO", objSMSSubscriptionTO);
        }
        HashMap allMap=new HashMap();
        if (getAllLoanAmount() != null && getAllLoanAmount().size() > 0) {
            renewalMap.put("ACT_CLOSING_CHARGE",getCharges());
            renewalMap.put("ALL_AMOUNT", getAllLoanAmount());
            renewalMap.put("OLD_ACCT_NUM", getStrACNumber());
            //System.out.println("getTotRecivableAmt()==="+getTotRecivableAmt());
            //System.out.println("getCharges()==="+getCharges());
            //System.out.println("getTxtRenewalLimit_SD()==="+getTxtRenewalLimit_SD());
            renewalMap.put("TOTAL_RECEVABLE", new Double(getTotRecivableAmt()));
            renewalMap.put("RENEWAL_SAN_AMT", new Double(getTxtRenewalLimit_SD()));
            renewalMap.put("OLD_PROD_ID",getRnwLoadingProdId());
            data.put("RENEWAL_TRANS", renewalMap);

        }
        data.put("RENEW_OLD_PROD_ID", getRnwLoadingProdId());
        data.put("OLD_RENEW_ACT_NO", getStrACNumber());
        //        data.put("TermLoanAdditionalSanctionTO",objTermLoanAdditionalSanMap);
        //        data.put("TermLoanDisbursementTO",objTermLoanSubLimitTOList);
        //        data.put("ActTransTO", objAcctTransTOMap);//dontdelete
        //        data.put("SettlementTO", objSettlementTOMap);//dontdelete
        //        data.put("SettlementBankTO", objSettlementBankTOMap);//dontdelete

        //        if(objExtnFacilityDetails !=null && objExtnFacilityDetails.size()>0)
        //            data.put("TermLoanExtenFacilityDetailsTO",objExtnFacilityDetails);//dontdelete
        if (objTermLoanInstalltransactionMap != null) {
            data.put("INT_TRANSACTION_REPAYMENT", objTermLoanInstalltransactionMap);
        }
        if (partReject.length() > 0) {
            data.put("PARTIALLY_REJECT", "PARTIALLY_REJECT");
        }
        if (advanceLiablityMap != null && (!advanceLiablityMap.isEmpty())) {
            data.put("AdvancesLiablityExceedLimit", advanceLiablityMap);
        }
        if (getCommand().equals("UPDATE")) {
            if (termLoanClassificationOB.getListAssetStatus() != null) {
                data.put("NPAHISTORY", termLoanClassificationOB.getListAssetStatus());
            }
            data.put("ACCOUNT_NUMBER", getStrACNumber());
        }
        //        if(saveMode==1){
        data.put("TermRepaymentInstallmentAllTO", objRepaymentInstallmentAllMap);
        //        //System.out.println("goingDAO"+data.get("TermRepaymentInstallmentAllTO"));
        //        }

        String facilityType = CommonUtil.convertObjToStr(getCbmTypeOfFacility().getKeyForSelected());
        if (facilityType.length() > 0 && facilityType.equals("CC") || facilityType.equals("OD")) {
//            data.put("TermLoanOtherDetailsTO", objTermLoanOtherDetailsTO);
        } else {
//            data.put("TermLoanOtherDetailsTO", null);
        }

        if (getCommand().equals(CommonConstants.TOSTATUS_INSERT)) {
            if (loanType.equals("LTD")) {
                data.put("LTD", depositCustDetMap);
            }
        }
        if (loanType.equals("LTD")) {
            data.put("LOAN_TYPE", "LTD");
        } else {
            data.put("LOAN_TYPE", "");
        }
        if (getAuthorizeMap() != null) {
            data.put(CommonConstants.AUTHORIZEMAP, getAuthorizeMap());
        }
        if (getTransactionMap() != null) {
            data.put("Transaction Details Data", getTransactionMap());
        }
        if (getChargelst() != null) {
            data.put("Charge List Data", getChargelst());
        }
//        ADDED HERE BY NIKHIL
        data.put("AccountNomineeTO", objNomineeOB.getNomimeeList());
        data.put("AccountNomineeDeleteTO", objNomineeOB.getDeleteNomimeeList());


        data.put(CommonConstants.MODULE, getModule());
        data.put(CommonConstants.SCREEN, getScreen());
        data.put("UI_PRODUCT_TYPE", "TL");
        data.put("RENEWAL_PARAM", getRenewalParamMap());        
         // Added by nithya on 09-10-2018 for KD 263 GST - Needed to implement the GST in GOld Loan renewal
        if (isRenewalYesNo()) {
            data.put("APPRAISER_ID", getCbmRenewalAppraiserId().getKeyForSelected()); // Added by nithya on 21-06-2019 for KD-534 APPRAISER NAME NOT UPDATING WHEN GOLD LOAN RENEWAL
            if (getServiceTax_Map() != null && getServiceTax_Map().size() > 0) {
                data.put("serviceTaxDetails", getServiceTax_Map());
                data.put("serviceTaxDetailsTO", setServiceTaxDetails());
            }
        }
        // END        
        System.out.print("dataonly#######renewal" + data);
        return data;

    }

//    public HashMap renewalGoldLoan()throws Exception{
//               setTxtSanctionSlNo(String.valueOf("1"));
//               insertFacilityDetails(getTxtSanctionSlNo(),Integer.parseInt(getTxtSanctionSlNo()));
//               HashMap objTermLoanFacilityTOMap =setTermLoanFacilitySingleRecord();// setTermLoanFacility();
//               HashMap objSantionDetailsTOMap = setTermLoanSanctionSingleRecord();//setTermLoanSanction()
//               HashMap objTermLoanSanctionTOMap = (HashMap) objSantionDetailsTOMap.get(SANCTION);
//               HashMap objTermLoanSanctionFacilityTOMap = (HashMap) objSantionDetailsTOMap.get(SANCTION_FACILITY);
//               HashMap objTermLoanJointAcctTOMap = termLoanBorrowerOB.setTermLoanJointAcct();
//               if(getNewCustIdNo().length()>0){
//                   termLoanBorrowerOB.setTxtCustID(CommonUtil.convertObjToStr(getNewCustIdNo()));
//               }
//               TermLoanBorrowerTO objTermLoanBorrowerTO = termLoanBorrowerOB.setTermLoanBorrower();
//               HashMap repaymentInstallmentMap = new HashMap();
//               HashMap objRepaymentInstallmentMap = termLoanRepaymentOB.setTermLoanRepayment();
//               HashMap objTermLoanRepaymentTOMap = setTermLoanRepayment();
//               HashMap objTermLoanInstallmentTOMap = (HashMap) objRepaymentInstallmentMap.get(INSTALLMENT_DETAILS);
//               HashMap objTermLoanInstallMultIntMap = (HashMap) objRepaymentInstallmentMap.get(INSTALLMULTINT_DETAILS);
//               HashMap objTermLoanInstalltransactionMap=null;
//               if(objRepaymentInstallmentMap.containsKey("INT_TRANSACTION_REPAYMENT"))
//                   objTermLoanInstalltransactionMap = (HashMap) objRepaymentInstallmentMap.get("INT_TRANSACTION_REPAYMENT");
//               HashMap objTermLoanDocumentTOMap = termLoanDocumentDetailsOB.setTermLoanDocument();
//               HashMap objTermLoanInterestTOMap = setTermLoanInterest();
//               TermLoanClassificationTO objTermLoanClassificationTO = termLoanClassificationOB.setTermLoanClassification();
//               GoldLoanSecurityTO objTermLoanSecurityTO = termLoanSecurityOB.setGoldLoanSecurityTO();
//               objTermLoanSecurityTO.setCommand(getCommand());
//               if (getCommand().equals(DELETE)) {
//                   objTermLoanBorrowerTO.setCommand(DELETE);
//               } else {
//                   objTermLoanBorrowerTO.setCommand(getCommand());
//               }
//               //System.out.println("getCommand(): "+getCommand());
//               if (getCommand().equals(INSERT)){
//                   objTermLoanBorrowerTO.setStatus(CommonConstants.STATUS_CREATED);
//                   objTermLoanSecurityTO.setStatus(CommonConstants.STATUS_CREATED);
//               }else if (getCommand().equals(UPDATE)){
//                   objTermLoanBorrowerTO.setStatus(CommonConstants.STATUS_MODIFIED);
//                   objTermLoanSecurityTO.setStatus(CommonConstants.STATUS_MODIFIED);
//                   objTermLoanSecurityTO.setAcctNum(getStrACNumber());
//               }else if (getCommand().equals(DELETE)){
//                   objTermLoanBorrowerTO.setStatus(CommonConstants.STATUS_DELETED);
//                   objTermLoanSecurityTO.setStatus(CommonConstants.STATUS_DELETED);
//                   objTermLoanSecurityTO.setAcctNum(getStrACNumber());
//               }
//               objTermLoanClassificationTO.setCommand(termLoanClassificationOB.getClassifiDetails());
//               if (termLoanClassificationOB.getClassifiDetails().equals(CommonConstants.TOSTATUS_INSERT)){
//                   objTermLoanClassificationTO.setStatus(CommonConstants.STATUS_CREATED);
//               }else if (termLoanClassificationOB.getClassifiDetails().equals(CommonConstants.TOSTATUS_UPDATE)){
//                   objTermLoanClassificationTO.setStatus(CommonConstants.STATUS_MODIFIED);
//               }else if (termLoanClassificationOB.getClassifiDetails().equals(CommonConstants.TOSTATUS_DELETE)){
//                   objTermLoanClassificationTO.setStatus(CommonConstants.STATUS_DELETED);
//               }
//               
//               return new  HashMap();
//    }
//    
    public HashMap setTermLoanInterest(String renewalNo, boolean isRenewal) {
        HashMap interestMap = new HashMap();
        try {
            //            TermLoanOB termLoanOB = TermLoanOB.getInstance();
            TermLoanInterestTO objTermLoanInterestTO = new TermLoanInterestTO();
            objTermLoanInterestTO.setSlno(CommonUtil.convertObjToDouble(new Double(1)));
            //                    objTermLoanInterestTO.setBorrowNo(getBorrowerNo());
            if ((renewalNo != null && renewalNo.length() > 0) || isRenewal) {
                objTermLoanInterestTO.setAcctNum(renewalNo);
                objTermLoanInterestTO.setCommand("INSERT");
                objTermLoanInterestTO.setStatus(CommonConstants.STATUS_CREATED);
            } else {
                objTermLoanInterestTO.setAcctNum(getStrACNumber());
                objTermLoanInterestTO.setAgainstClearingInt(new Double(0));
                objTermLoanInterestTO.setCommand(CommonUtil.convertObjToStr(getCommand()));
                if (getCommand().equals(INSERT)) {
                    objTermLoanInterestTO.setStatus(CommonConstants.STATUS_CREATED);
                } else {//if (getCommand().equals(UPDATE)){
                    objTermLoanInterestTO.setStatus(CommonConstants.STATUS_MODIFIED);
                }

            }
            objTermLoanInterestTO.setAgainstClearingInt(new Double(0));

            objTermLoanInterestTO.setFromAmt(CommonUtil.convertObjToDouble(new Double(1)));
            objTermLoanInterestTO.setFromDt(curDate);
            if ((renewalNo != null && renewalNo.length() > 0) || isRenewal) {
                objTermLoanInterestTO.setInterest(CommonUtil.convertObjToDouble(getTxtRenewalInter()));
                objTermLoanInterestTO.setPenalInterest(CommonUtil.convertObjToDouble(getTxtRenewalPenalInter()));

            } else {
                objTermLoanInterestTO.setInterest(CommonUtil.convertObjToDouble(getInterest()));
                objTermLoanInterestTO.setPenalInterest(CommonUtil.convertObjToDouble(getPenalInterest()));
            }

            objTermLoanInterestTO.setInterestExpiryLimit(CommonUtil.convertObjToDouble(new Double(0)));
            objTermLoanInterestTO.setStatementPenal(CommonUtil.convertObjToDouble(new Double(0)));

            objTermLoanInterestTO.setToAmt(CommonUtil.convertObjToDouble(new Double(999999999)));
            objTermLoanInterestTO.setActiveStatus(CommonUtil.convertObjToStr("R"));
            objTermLoanInterestTO.setAuthorizeStatus(CommonUtil.convertObjToStr(null));
            objTermLoanInterestTO.setRetCreateDt(curDate);
            objTermLoanInterestTO.setToDt(DateUtil.getDateMMDDYYYY(CommonUtil.convertObjToStr(null)));
            if (getCommand().equals(INSERT)) {
                objTermLoanInterestTO.setStatus(CommonConstants.STATUS_CREATED);
            } else {//if (getCommand().equals(UPDATE)){
                objTermLoanInterestTO.setStatus(CommonConstants.STATUS_MODIFIED);
            }
            objTermLoanInterestTO.setStatusBy(TrueTransactMain.USER_ID);
            objTermLoanInterestTO.setStatusDt(curDate);
            interestMap.put(String.valueOf(1), objTermLoanInterestTO);
        } catch (Exception e) {
        }
        return interestMap;
    }

    private HashMap setTermLoanSanction() {
        HashMap returnValue = new HashMap();
        try {
            TermLoanSanctionTO objTermLoanSanctionTO;
            TermLoanSanctionFacilityTO objTermLoanSanctionFacilityTO;
            HashMap sanctionMap = new HashMap();
            HashMap sanctionFacilityMap = new HashMap();
            HashMap eachSanctionRec;
            LinkedHashMap sanctionFacilityRecs;
            HashMap eachSanctionFacilityRec;
            int sanctionFacilityKey = 0;
            Set keySet = sanctionMainAll.keySet();
            Object[] objKeySet = (Object[]) keySet.toArray();
            Set sanctionFacilityKeySet;
            Object[] objSanctionFacilityKeySet;

            boolean isSanctionDeleted = false;

            // To set the values for Sanction Transfer Object
            for (int i = sanctionMainAll.size() - 1, j = 0; i >= 0; --i, ++j) {
                eachSanctionRec = (HashMap) sanctionMainAll.get(objKeySet[j]);
                objTermLoanSanctionTO = new TermLoanSanctionTO();
                objTermLoanSanctionTO.setBorrowNo(borrowerNo);
                objTermLoanSanctionTO.setSanctionNo(CommonUtil.convertObjToStr(eachSanctionRec.get(SANCTION_SL_NO)));
                objTermLoanSanctionTO.setSlNo(CommonUtil.convertObjToStr(eachSanctionRec.get(SANCTION_NO)));
                objTermLoanSanctionTO.setSanctionAuthority(CommonUtil.convertObjToStr(eachSanctionRec.get(SANCTION_AUTHORITY)));

                //                objTermLoanSanctionTO.setSanctionDt((Date)eachSanctionRec.get(SANCTION_DATE));
                objTermLoanSanctionTO.setSanctionDt(getProperDateFormat(eachSanctionRec.get(SANCTION_DATE)));
                objTermLoanSanctionTO.setSanctionMode(CommonUtil.convertObjToStr(eachSanctionRec.get(SANCTION_MODE)));
                objTermLoanSanctionTO.setRemarks(CommonUtil.convertObjToStr(eachSanctionRec.get(REMARKS)));
                objTermLoanSanctionTO.setCommand(CommonUtil.convertObjToStr(eachSanctionRec.get(COMMAND)));
                if (eachSanctionRec.get(COMMAND).equals(INSERT)) {
                    objTermLoanSanctionTO.setStatus(CommonConstants.STATUS_CREATED);
                } else if (eachSanctionRec.get(COMMAND).equals(UPDATE)) {
                    objTermLoanSanctionTO.setStatus(CommonConstants.STATUS_MODIFIED);
                }
                objTermLoanSanctionTO.setStatusBy(TrueTransactMain.USER_ID);
                objTermLoanSanctionTO.setStatusDt(curDate);
                sanctionFacilityRecs = (LinkedHashMap) eachSanctionRec.get(SANCTION_FACILITY_ALL);
                if (getCommand().equals(CommonConstants.TOSTATUS_DELETE)) {
                    if (sanctionFacilityRecs.size() == 1) {
                        if (objTermLoanSanctionTO.getSanctionNo().equals(getTxtSanctionSlNo())
                                && objTermLoanSanctionTO.getSlNo().equals(getTxtSanctionNo())) {
                            objTermLoanSanctionTO.setStatus(CommonConstants.STATUS_DELETED);
                            objTermLoanSanctionTO.setCommand(getCommand());
                            isSanctionDeleted = true;
                        }
                    }
                }

                sanctionFacilityKeySet = sanctionFacilityRecs.keySet();
                objSanctionFacilityKeySet = (Object[]) sanctionFacilityKeySet.toArray();
                // To set the values for Sanction Facility Transfer Object
                for (int k = sanctionFacilityRecs.size() - 1, m = 0; k >= 0; --k, ++m) {
                    eachSanctionFacilityRec = (HashMap) sanctionFacilityRecs.get(objSanctionFacilityKeySet[m]);
                    objTermLoanSanctionFacilityTO = new TermLoanSanctionFacilityTO();
                    sanctionFacilityKey++;
                    objTermLoanSanctionFacilityTO.setBorrowNo(borrowerNo);
                    //                    objTermLoanSanctionFacilityTO.setSanctionNo(CommonUtil.convertObjToStr( eachSanctionFacilityRec.get(SANCTION_NO)));
                    objTermLoanSanctionFacilityTO.setSanctionNo(objTermLoanSanctionTO.getSanctionNo());
                    objTermLoanSanctionFacilityTO.setSlNo(CommonUtil.convertObjToDouble(eachSanctionFacilityRec.get(SLNO)));
                    objTermLoanSanctionFacilityTO.setFacilityType(CommonUtil.convertObjToStr(eachSanctionFacilityRec.get(FACILITY_TYPE)));
                    objTermLoanSanctionFacilityTO.setLimit(CommonUtil.convertObjToDouble(eachSanctionFacilityRec.get(LIMIT)));

                    //                    objTermLoanSanctionFacilityTO.setFromDt((Date)eachSanctionFacilityRec.get(FROM));
                    objTermLoanSanctionFacilityTO.setFromDt(getProperDateFormat(eachSanctionFacilityRec.get(FROM)));

                    //                    objTermLoanSanctionFacilityTO.setToDt((Date)eachSanctionFacilityRec.get(TO));
                    objTermLoanSanctionFacilityTO.setToDt(getProperDateFormat(eachSanctionFacilityRec.get(TO)));
                    objTermLoanSanctionFacilityTO.setNoInstall(CommonUtil.convertObjToDouble(eachSanctionFacilityRec.get(NO_INSTALLMENTS)));
                    objTermLoanSanctionFacilityTO.setRepaymentFrequency(CommonUtil.convertObjToDouble(eachSanctionFacilityRec.get(REPAY_FREQ)));
                    objTermLoanSanctionFacilityTO.setProductId(CommonUtil.convertObjToStr(eachSanctionFacilityRec.get(PROD_ID)));
                    objTermLoanSanctionFacilityTO.setMoratoriumGiven(CommonUtil.convertObjToStr(eachSanctionFacilityRec.get(MORATORIUM_GIVEN)));

                    //                    objTermLoanSanctionFacilityTO.setRepaymentDt((Date)eachSanctionFacilityRec.get(FACILITY_REPAY_DATE));
                    objTermLoanSanctionFacilityTO.setRepaymentDt(getProperDateFormat(eachSanctionFacilityRec.get(FACILITY_REPAY_DATE)));
                    objTermLoanSanctionFacilityTO.setNoMoratorium(CommonUtil.convertObjToDouble(eachSanctionFacilityRec.get(MORATORIUM_PERIOD)));
                    objTermLoanSanctionFacilityTO.setCommand(CommonUtil.convertObjToStr(eachSanctionFacilityRec.get(COMMAND)));
                    if (eachSanctionFacilityRec.get(COMMAND).equals(INSERT)) {
                        objTermLoanSanctionFacilityTO.setStatus(CommonConstants.STATUS_CREATED);
                    } else if (eachSanctionFacilityRec.get(COMMAND).equals(UPDATE)) {
                        objTermLoanSanctionFacilityTO.setStatus(CommonConstants.STATUS_MODIFIED);
                    }
                    if (getCommand().equals(CommonConstants.TOSTATUS_DELETE)) {
                        if (objTermLoanSanctionFacilityTO.getSanctionNo().equals(getTxtSanctionSlNo())
                                && objTermLoanSanctionFacilityTO.getSlNo().doubleValue() == CommonUtil.convertObjToDouble(getSanctionSlNo()).doubleValue()) {
                            objTermLoanSanctionFacilityTO.setStatus(CommonConstants.STATUS_DELETED);
                            objTermLoanSanctionFacilityTO.setCommand(getCommand());
                        }
                    }
                    objTermLoanSanctionFacilityTO.setStatusBy(TrueTransactMain.USER_ID);
                    objTermLoanSanctionFacilityTO.setStatusDt(curDate);
                    if (objTermLoanSanctionFacilityTO.getSlNo().doubleValue() == slNoForSanction) {
                        sanctionFacilityMap.put(String.valueOf(sanctionFacilityKey), objTermLoanSanctionFacilityTO);
                    }
                    eachSanctionFacilityRec = null;
                    objTermLoanSanctionFacilityTO = null;
                }

                if (CommonUtil.convertObjToStr(eachSanctionRec.get(SANCTION_SL_NO)).equals(getTxtSanctionSlNo()) || getTxtSanctionSlNo().length() == 0) {
                    sanctionMap.put(eachSanctionRec.get(SANCTION_SL_NO), objTermLoanSanctionTO);
                }
                objTermLoanSanctionTO = null;
                eachSanctionRec = null;
                sanctionFacilityRecs = null;
                sanctionFacilityKeySet = null;
                objSanctionFacilityKeySet = null;
            }
            // To set the values for Sanction Transfer Object
            // where as the existing records in Database are deleted in client side
            // useful for updating the status
            for (int i = tableUtilSanction_Main.getRemovedValues().size() - 1, j = 0; i >= 0; --i, ++j) {
                eachSanctionRec = (HashMap) tableUtilSanction_Main.getRemovedValues().get(j);
                objTermLoanSanctionTO = new TermLoanSanctionTO();
                objTermLoanSanctionTO.setBorrowNo(borrowerNo);
                objTermLoanSanctionTO.setSanctionNo(CommonUtil.convertObjToStr(eachSanctionRec.get(SANCTION_NO)));
                objTermLoanSanctionTO.setSlNo(CommonUtil.convertObjToStr(eachSanctionRec.get(SANCTION_SL_NO)));
                objTermLoanSanctionTO.setSanctionAuthority(CommonUtil.convertObjToStr(eachSanctionRec.get(SANCTION_AUTHORITY)));

                //                objTermLoanSanctionTO.setSanctionDt((Date)eachSanctionRec.get(SANCTION_DATE));
                objTermLoanSanctionTO.setSanctionDt(getProperDateFormat(eachSanctionRec.get(SANCTION_DATE)));
                objTermLoanSanctionTO.setSanctionMode(CommonUtil.convertObjToStr(eachSanctionRec.get(SANCTION_MODE)));
                objTermLoanSanctionTO.setRemarks(CommonUtil.convertObjToStr(eachSanctionRec.get(REMARKS)));
                objTermLoanSanctionTO.setCommand(CommonUtil.convertObjToStr(eachSanctionRec.get(COMMAND)));
                objTermLoanSanctionTO.setStatus(CommonConstants.STATUS_DELETED);
                objTermLoanSanctionTO.setStatusBy(TrueTransactMain.USER_ID);
                objTermLoanSanctionTO.setStatusDt(curDate);
                sanctionFacilityRecs = (LinkedHashMap) eachSanctionRec.get(SANCTION_FACILITY_ALL);

                sanctionFacilityKeySet = sanctionFacilityRecs.keySet();
                objSanctionFacilityKeySet = (Object[]) sanctionFacilityKeySet.toArray();
                // To set the values for Sanction Facility Transfer Object
                // where as the existing records in Database are deleted in client side
                // useful for updating the status
                for (int k = sanctionFacilityRecs.size() - 1, l = 0; k >= 0; --k, ++l) {
                    eachSanctionFacilityRec = (HashMap) sanctionFacilityRecs.get(objSanctionFacilityKeySet[l]);
                    objTermLoanSanctionFacilityTO = new TermLoanSanctionFacilityTO();
                    sanctionFacilityKey++;
                    objTermLoanSanctionFacilityTO.setBorrowNo(borrowerNo);
                    objTermLoanSanctionFacilityTO.setSanctionNo(CommonUtil.convertObjToStr(eachSanctionFacilityRec.get(SANCTION_NO)));
                    objTermLoanSanctionFacilityTO.setSlNo(CommonUtil.convertObjToDouble(eachSanctionFacilityRec.get(SLNO)));
                    objTermLoanSanctionFacilityTO.setFacilityType(CommonUtil.convertObjToStr(eachSanctionFacilityRec.get(FACILITY_TYPE)));
                    objTermLoanSanctionFacilityTO.setLimit(CommonUtil.convertObjToDouble(eachSanctionFacilityRec.get(LIMIT)));
                    //                    objTermLoanSanctionFacilityTO.setFromDt((Date)eachSanctionFacilityRec.get(FROM));
                    objTermLoanSanctionFacilityTO.setFromDt(getProperDateFormat(eachSanctionFacilityRec.get(FROM)));

                    //                    objTermLoanSanctionFacilityTO.setToDt((Date)eachSanctionFacilityRec.get(TO));
                    objTermLoanSanctionFacilityTO.setToDt(getProperDateFormat(eachSanctionFacilityRec.get(TO)));
                    objTermLoanSanctionFacilityTO.setNoInstall(CommonUtil.convertObjToDouble(eachSanctionFacilityRec.get(NO_INSTALLMENTS)));
                    objTermLoanSanctionFacilityTO.setRepaymentFrequency(CommonUtil.convertObjToDouble(eachSanctionFacilityRec.get(REPAY_FREQ)));
                    objTermLoanSanctionFacilityTO.setProductId(CommonUtil.convertObjToStr(eachSanctionFacilityRec.get(PROD_ID)));
                    objTermLoanSanctionFacilityTO.setMoratoriumGiven(CommonUtil.convertObjToStr(eachSanctionFacilityRec.get(MORATORIUM_GIVEN)));

                    //                    objTermLoanSanctionFacilityTO.setRepaymentDt((Date)eachSanctionFacilityRec.get(FACILITY_REPAY_DATE));
                    objTermLoanSanctionFacilityTO.setRepaymentDt(getProperDateFormat(eachSanctionFacilityRec.get(FACILITY_REPAY_DATE)));
                    objTermLoanSanctionFacilityTO.setNoMoratorium(CommonUtil.convertObjToDouble(eachSanctionFacilityRec.get(MORATORIUM_PERIOD)));
                    objTermLoanSanctionFacilityTO.setCommand(CommonConstants.TOSTATUS_DELETE);
                    objTermLoanSanctionFacilityTO.setStatus(CommonConstants.STATUS_DELETED);
                    objTermLoanSanctionFacilityTO.setStatusBy(TrueTransactMain.USER_ID);
                    objTermLoanSanctionFacilityTO.setStatusDt(curDate);
                    sanctionFacilityMap.put(String.valueOf(sanctionFacilityKey), objTermLoanSanctionFacilityTO);
                    eachSanctionFacilityRec = null;
                    objTermLoanSanctionFacilityTO = null;
                }
                sanctionMap.put(eachSanctionRec.get(SANCTION_SL_NO), objTermLoanSanctionTO);
                eachSanctionRec = null;
                sanctionFacilityRecs = null;
                objTermLoanSanctionTO = null;
                sanctionFacilityKeySet = null;
                objSanctionFacilityKeySet = null;
            }
            // To set the values for Power of Sanction Facility Object
            // where as the existing records in Database are deleted in client side
            // useful for updating the status
            for (int i = tableUtilSanction_Facility.getRemovedValues().size() - 1, j = 0; i >= 0; --i, ++j) {
                eachSanctionFacilityRec = (HashMap) tableUtilSanction_Facility.getRemovedValues().get(j);
                objTermLoanSanctionFacilityTO = new TermLoanSanctionFacilityTO();
                sanctionFacilityKey++;
                objTermLoanSanctionFacilityTO.setBorrowNo(borrowerNo);
                objTermLoanSanctionFacilityTO.setSanctionNo(CommonUtil.convertObjToStr(eachSanctionFacilityRec.get(SANCTION_NO)));
                objTermLoanSanctionFacilityTO.setSlNo(CommonUtil.convertObjToDouble(eachSanctionFacilityRec.get(SLNO)));
                objTermLoanSanctionFacilityTO.setFacilityType(CommonUtil.convertObjToStr(eachSanctionFacilityRec.get(FACILITY_TYPE)));
                objTermLoanSanctionFacilityTO.setLimit(CommonUtil.convertObjToDouble(eachSanctionFacilityRec.get(LIMIT)));

                //                objTermLoanSanctionFacilityTO.setFromDt((Date)eachSanctionFacilityRec.get(FROM));
                objTermLoanSanctionFacilityTO.setFromDt(getProperDateFormat(eachSanctionFacilityRec.get(FROM)));
                //                objTermLoanSanctionFacilityTO.setToDt((Date)eachSanctionFacilityRec.get(TO));
                objTermLoanSanctionFacilityTO.setToDt(getProperDateFormat(eachSanctionFacilityRec.get(TO)));
                objTermLoanSanctionFacilityTO.setNoInstall(CommonUtil.convertObjToDouble(eachSanctionFacilityRec.get(NO_INSTALLMENTS)));
                objTermLoanSanctionFacilityTO.setRepaymentFrequency(CommonUtil.convertObjToDouble(eachSanctionFacilityRec.get(REPAY_FREQ)));
                objTermLoanSanctionFacilityTO.setProductId(CommonUtil.convertObjToStr(eachSanctionFacilityRec.get(PROD_ID)));
                objTermLoanSanctionFacilityTO.setMoratoriumGiven(CommonUtil.convertObjToStr(eachSanctionFacilityRec.get(MORATORIUM_GIVEN)));

                //                objTermLoanSanctionFacilityTO.setRepaymentDt((Date)eachSanctionFacilityRec.get(FACILITY_REPAY_DATE));
                objTermLoanSanctionFacilityTO.setRepaymentDt(getProperDateFormat(eachSanctionFacilityRec.get(FACILITY_REPAY_DATE)));
                objTermLoanSanctionFacilityTO.setNoMoratorium(CommonUtil.convertObjToDouble(eachSanctionFacilityRec.get(MORATORIUM_PERIOD)));
                objTermLoanSanctionFacilityTO.setCommand(CommonConstants.TOSTATUS_DELETE);
                objTermLoanSanctionFacilityTO.setStatus(CommonConstants.STATUS_DELETED);
                objTermLoanSanctionFacilityTO.setStatusBy(TrueTransactMain.USER_ID);
                objTermLoanSanctionFacilityTO.setStatusDt(curDate);
                sanctionFacilityMap.put(String.valueOf(sanctionFacilityKey), objTermLoanSanctionFacilityTO);
                eachSanctionFacilityRec = null;
                objTermLoanSanctionFacilityTO = null;
            }

            canBorrowerDelete = false;
            if (getCommand().equals(CommonConstants.TOSTATUS_DELETE)) {
                if (sanctionMainAll.size() == 1 && isSanctionDeleted) {
                    canBorrowerDelete = true;
                }
            }
            isSanctionDeleted = false;

            returnValue.put(SANCTION, sanctionMap);
            returnValue.put(SANCTION_FACILITY, sanctionFacilityMap);

        } catch (Exception e) {
            log.info("Exception caught In setTermLoanSanction: " + e);
            parseException.logException(e, true);
        }
        return returnValue;
    }

    public HashMap setTermLoanRepayment() {
        HashMap repayInstallmentMap = new HashMap();
        HashMap repaymentMap = new HashMap();
        HashMap installmentMap = new HashMap();
        HashMap installMultIntMap = new HashMap();
        boolean reschudleavailable = false;
        try {
            TermLoanRepaymentTO termLoanRepaymentTO = new TermLoanRepaymentTO();
            //            termLoanRepaymentTO.setAcctNum(getStrACNumber());
            //                termLoanRepaymentTO.setBorrowNo(getBorrowerNo());
            termLoanRepaymentTO.setScheduleNo(CommonUtil.convertObjToDouble(new Double(1)));
            termLoanRepaymentTO.setCommand(CommonUtil.convertObjToStr(getCommand()));
            //            if (oneRecord.get(ADD_SIS).equals(YES)){
            //                termLoanRepaymentTO.setAddSi(YES);
            //            }else if (oneRecord.get(ADD_SIS).equals(NO)){
            //                termLoanRepaymentTO.setAddSi(NO);
            //            }else{
            //                termLoanRepaymentTO.setAddSi(" ");
            //            }
            termLoanRepaymentTO.setAmtLastInstall(CommonUtil.convertObjToDouble(getTxtLimit_SD()));
            termLoanRepaymentTO.setAmtPenultimateInstall(CommonUtil.convertObjToDouble(getTxtLimit_SD()));
            termLoanRepaymentTO.setInstallType(CommonUtil.convertObjToStr(termLoanRepaymentOB.getCbmRepayType().getKeyForSelected()));
            termLoanRepaymentTO.setRepaymentType(CommonUtil.convertObjToDouble(termLoanRepaymentOB.getCbmRepayFreq_Repayment().getKeyForSelected()));
            if(getCboRepayFreq().equals("USER_DEFINED_GOLD_LOAN")){ //Added by nithya on 06-07-2019 for KD 546 - New Gold Loan -45 days maturity type
              termLoanRepaymentTO.setRepaymentType(CommonUtil.convertObjToDouble(getMaximumDaysForLoan()));  
            }
            termLoanRepaymentTO.setNoInstallments(CommonUtil.convertObjToDouble(getTxtNoInstallments()));
            termLoanRepaymentTO.setFromDate(curDate);
            termLoanRepaymentTO.setFirstInstallDt(curDate);
            termLoanRepaymentTO.setLastInstallDt(curDate);
            termLoanRepaymentTO.setTotalBaseAmt(CommonUtil.convertObjToDouble(getTxtLimit_SD()));
            termLoanRepaymentTO.setLoanAmount(CommonUtil.convertObjToDouble(getTxtLimit_SD()));
            //            if (oneRecord.get(POST_DT_CHQ_ALLOW).equals(YES)){
            //                termLoanRepaymentTO.setPostDateChqallowed(YES);
            //            }else if (oneRecord.get(POST_DT_CHQ_ALLOW).equals(NO)){
            //                termLoanRepaymentTO.setPostDateChqallowed(NO);
            //            }else{
            //                termLoanRepaymentTO.setPostDateChqallowed(" ");
            //            }
            //            if (oneRecord.get(ACTIVE_STATUS).equals(YES)){
            //                termLoanRepaymentTO.setRepayActive(YES);
            //            }else if (oneRecord.get(ACTIVE_STATUS).equals(NO)){
            //                termLoanRepaymentTO.setRepayActive(NO);
            //            }else{
            //                termLoanRepaymentTO.setRepayActive(" ");
            //            }
            termLoanRepaymentTO.setRepayActive("Y");
            termLoanRepaymentTO.setTotalInstallAmt(CommonUtil.convertObjToDouble(new Double(0)));
            termLoanRepaymentTO.setDisbursementDt(curDate);
            termLoanRepaymentTO.setDisbursementId(CommonUtil.convertObjToDouble(new Double(0)));
            termLoanRepaymentTO.setRepayMorotorium(CommonUtil.convertObjToDouble(new Double(0)));
            termLoanRepaymentTO.setScheduleMode(CommonUtil.convertObjToStr(""));
            termLoanRepaymentTO.setRefScheduleNo(CommonUtil.convertObjToDouble(new Double(0)));
            if (getCommand().equals(INSERT)) {
                termLoanRepaymentTO.setStatus(CommonConstants.STATUS_CREATED);
            } else if (getCommand().equals(UPDATE)) {
                termLoanRepaymentTO.setStatus(CommonConstants.STATUS_MODIFIED);
            }
            termLoanRepaymentTO.setStatusBy(TrueTransactMain.USER_ID);
            termLoanRepaymentTO.setStatusDt(curDate);
            repaymentMap.put(String.valueOf(1), termLoanRepaymentTO);
            termLoanRepaymentTO = null;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return repaymentMap;
    }

    public HashMap setTermLoanRenewalRepaymentSchedule() {
        HashMap repayInstallmentMap = new HashMap();
        HashMap repaymentMap = new HashMap();
        HashMap installmentMap = new HashMap();
        HashMap installMultIntMap = new HashMap();
        boolean reschudleavailable = false;
        try {
            TermLoanRepaymentTO termLoanRepaymentTO = new TermLoanRepaymentTO();
            //            termLoanRepaymentTO.setAcctNum(getStrACNumber());
            //                termLoanRepaymentTO.setBorrowNo(getBorrowerNo());
            termLoanRepaymentTO.setScheduleNo(CommonUtil.convertObjToDouble(new Double(1)));
            if (CommonUtil.convertObjToStr(lblRenewalAcctNo_Sanction_Disp).length() == 0) {
                termLoanRepaymentTO.setCommand("INSERT");
                termLoanRepaymentTO.setStatus(CommonConstants.STATUS_CREATED);
            } else {
                termLoanRepaymentTO.setCommand("UPDATE");
                termLoanRepaymentTO.setStatus(CommonConstants.STATUS_MODIFIED);
            }
            //            if (oneRecord.get(ADD_SIS).equals(YES)){
            //                termLoanRepaymentTO.setAddSi(YES);
            //            }else if (oneRecord.get(ADD_SIS).equals(NO)){
            //                termLoanRepaymentTO.setAddSi(NO);
            //            }else{
            //                termLoanRepaymentTO.setAddSi(" ");
            //            }
            termLoanRepaymentTO.setAmtLastInstall(CommonUtil.convertObjToDouble(getTxtRenewalLimit_SD()));
            termLoanRepaymentTO.setAmtPenultimateInstall(CommonUtil.convertObjToDouble(getTxtRenewalLimit_SD()));
            termLoanRepaymentTO.setInstallType(CommonUtil.convertObjToStr("EYI"));
            termLoanRepaymentTO.setRepaymentType(CommonUtil.convertObjToDouble(new Double(365)));
            if(getCboRenewalRepayFreq().equals("USER_DEFINED_GOLD_LOAN")){ //Added by nithya on 06-07-2019 for KD 546 - New Gold Loan -45 days maturity type
              termLoanRepaymentTO.setRepaymentType(CommonUtil.convertObjToDouble(getMaximumDaysForRenewLoan()));  
            }
            termLoanRepaymentTO.setNoInstallments(CommonUtil.convertObjToDouble(new Double(1)));
            termLoanRepaymentTO.setFromDate(curDate);
            termLoanRepaymentTO.setFirstInstallDt(curDate);
            termLoanRepaymentTO.setLastInstallDt(curDate);
            termLoanRepaymentTO.setTotalBaseAmt(CommonUtil.convertObjToDouble(getTxtRenewalLimit_SD()));
            termLoanRepaymentTO.setLoanAmount(CommonUtil.convertObjToDouble(getTxtRenewalLimit_SD()));
            //            if (oneRecord.get(POST_DT_CHQ_ALLOW).equals(YES)){
            //                termLoanRepaymentTO.setPostDateChqallowed(YES);
            //            }else if (oneRecord.get(POST_DT_CHQ_ALLOW).equals(NO)){
            //                termLoanRepaymentTO.setPostDateChqallowed(NO);
            //            }else{
            //                termLoanRepaymentTO.setPostDateChqallowed(" ");
            //            }
            //            if (oneRecord.get(ACTIVE_STATUS).equals(YES)){
            //                termLoanRepaymentTO.setRepayActive(YES);
            //            }else if (oneRecord.get(ACTIVE_STATUS).equals(NO)){
            //                termLoanRepaymentTO.setRepayActive(NO);
            //            }else{
            //                termLoanRepaymentTO.setRepayActive(" ");
            //            }
            termLoanRepaymentTO.setRepayActive("Y");
            termLoanRepaymentTO.setTotalInstallAmt(CommonUtil.convertObjToDouble(new Double(0)));
            termLoanRepaymentTO.setDisbursementDt(curDate);
            termLoanRepaymentTO.setDisbursementId(CommonUtil.convertObjToDouble(new Double(0)));
            termLoanRepaymentTO.setRepayMorotorium(CommonUtil.convertObjToDouble(new Double(0)));
            termLoanRepaymentTO.setScheduleMode(CommonUtil.convertObjToStr(""));
            termLoanRepaymentTO.setRefScheduleNo(CommonUtil.convertObjToDouble(new Double(0)));
//            if (getCommand().equals(INSERT)){
//                termLoanRepaymentTO.setStatus(CommonConstants.STATUS_CREATED);
//            }else if (getCommand().equals(UPDATE)){
//                termLoanRepaymentTO.setStatus(CommonConstants.STATUS_MODIFIED);
//            }
            termLoanRepaymentTO.setStatusBy(TrueTransactMain.USER_ID);
            termLoanRepaymentTO.setStatusDt(curDate);
            repaymentMap.put(String.valueOf(1), termLoanRepaymentTO);
            termLoanRepaymentTO = null;
        } catch (Exception e) {
        }
        return repaymentMap;
    }

    public Date getProperDateFormat(Object obj) {
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

    /* single sanction singleloan
     **/
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



            objTermLoanSanctionTO.setSanctionNo(CommonUtil.convertObjToStr(getTxtSanctionSlNo()));
            objTermLoanSanctionTO.setSlNo(CommonUtil.convertObjToStr(getTxtSanctionNo()));
            objTermLoanSanctionTO.setSanctionAuthority(CommonUtil.convertObjToStr(cbmSanctioningAuthority.getKeyForSelected()));
            objTermLoanSanctionTO.setSanctionDt(getProperDateFormat(tdtSanctionDate));
            objTermLoanSanctionTO.setSanctionMode(CommonUtil.convertObjToStr(cbmModeSanction.getKeyForSelected()));
            objTermLoanSanctionTO.setRemarks(CommonUtil.convertObjToStr(txtSanctionRemarks));



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
            objTermLoanSanctionFacilityTO.setSlNo(CommonUtil.convertObjToDouble(getTxtSanctionSlNo()));
            objTermLoanSanctionFacilityTO.setFacilityType(CommonUtil.convertObjToStr(cbmTypeOfFacility.getKeyForSelected()));
            objTermLoanSanctionFacilityTO.setLimit(CommonUtil.convertObjToDouble(getTxtLimit_SD()));

            //                    objTermLoanSanctionFacilityTO.setFromDt((Date)eachSanctionFacilityRec.get(FROM));
            if (tdtFDate != null && (!tdtFDate.equals(""))) {
                objTermLoanSanctionFacilityTO.setFromDt(getProperDateFormat(tdtFDate));
            } else if (tdtFDate != null && tdtFDate.equals("")) {
                tdtFDate = DateUtil.getStringDate(curDate);
            }
            objTermLoanSanctionFacilityTO.setNoInstall(CommonUtil.convertObjToDouble(txtNoInstallments));
            //                    objTermLoanSanctionFacilityTO.setToDt((Date)eachSanctionFacilityRec.get(TO));
            //                    objTermLoanSanctionFacilityTO.setToDt(DateUtil.getDateMMDDYYYY(CommonUtil.convertObjToStr(tdtTDate)));
            //                    objTermLoanSanctionFacilityTO.setRepay
            Date tdtDate = null;
            if (getCboRepayFreq().length() > 0 && getCboRepayFreq().equals("Yearly")) {
                objTermLoanSanctionFacilityTO.setRepaymentFrequency(new Double(360));
                tdtDate = DateUtil.getDateMMDDYYYY(tdtFDate);
                tdtDate = DateUtil.addDays(tdtDate, 365);
            } else if (getCboRepayFreq().length() > 0 && getCboRepayFreq().equals("Half Yearly")) {
                objTermLoanSanctionFacilityTO.setRepaymentFrequency(new Double(180));
                tdtDate = DateUtil.getDateMMDDYYYY(tdtFDate);
                tdtDate = DateUtil.addDays(tdtDate, 180);
            } else if (getCboRepayFreq().length() > 0 && getCboRepayFreq().equals("Quaterly")) {
                objTermLoanSanctionFacilityTO.setRepaymentFrequency(new Double(90));
                tdtDate = DateUtil.getDateMMDDYYYY(tdtFDate);
                tdtDate = DateUtil.addDays(tdtDate, 90);
            } else if (getCboRepayFreq().length() > 0 && getCboRepayFreq().equals("Monthly")) {
                objTermLoanSanctionFacilityTO.setRepaymentFrequency(new Double(30));
                tdtDate = DateUtil.getDateMMDDYYYY(tdtFDate);
                tdtDate = DateUtil.addDays(tdtDate, 30);
                objTermLoanSanctionFacilityTO.setToDt(tdtDate);
            } else if (getCboRepayFreq().length() > 0 && getCboRepayFreq().equals("Fortnight")){
                objTermLoanSanctionFacilityTO.setRepaymentFrequency(new Double(15));
                tdtDate = DateUtil.getDateMMDDYYYY(tdtFDate);
                tdtDate = DateUtil.addDays(tdtDate, 15);
                objTermLoanSanctionFacilityTO.setToDt(tdtDate);
            } else if (getCboRepayFreq().length() > 0 && getCboRepayFreq().equals("USER_DEFINED_GOLD_LOAN")){// Added by nithya on 03-07-2019 for KD-546 New Gold Loan -45 days maturity type
                objTermLoanSanctionFacilityTO.setRepaymentFrequency(new Double(getMaximumDaysForLoan()));
                tdtDate = DateUtil.getDateMMDDYYYY(tdtFDate);
                tdtDate = DateUtil.addDays(tdtDate, getMaximumDaysForLoan());
                objTermLoanSanctionFacilityTO.setToDt(tdtDate);
            } 
            objTermLoanSanctionFacilityTO.setFromDt(getProperDateFormat(getTdtDemandPromNoteDate()));
            objTermLoanSanctionFacilityTO.setToDt(getProperDateFormat(getTdtDemandPromNoteExpDate()));
            objTermLoanSanctionFacilityTO.setRepaymentDt(getProperDateFormat(tdtTDate));
            //                    objTermLoanSanctionFacilityTO.setRepaymentFrequency(CommonUtil.convertObjToDouble(cbmRepayFreq.getKeyForSelected()));
            objTermLoanSanctionFacilityTO.setProductId(CommonUtil.convertObjToStr(cbmProductId.getKeyForSelected()));
            if (getChkMoratorium_Given()) {
                objTermLoanSanctionFacilityTO.setMoratoriumGiven("Y");
            } else {
                objTermLoanSanctionFacilityTO.setMoratoriumGiven("N");
            }
            objTermLoanSanctionFacilityTO.setNoMoratorium(CommonUtil.convertObjToDouble(getTxtFacility_Moratorium_Period()));
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

    /* single sanction singleloan
     **/
    private HashMap setTermLoanRenewalSanctionSingleRecord() {
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



            objTermLoanSanctionTO.setSanctionNo(CommonUtil.convertObjToStr(getTxtSanctionSlNo()));
            objTermLoanSanctionTO.setSlNo(CommonUtil.convertObjToStr("1"));
            objTermLoanSanctionTO.setSanctionAuthority(CommonUtil.convertObjToStr(cbmRenewalSanctioningAuthority.getKeyForSelected()));
            objTermLoanSanctionTO.setSanctionDt(getProperDateFormat(tdtRenewalSanctionDate));
            objTermLoanSanctionTO.setSanctionMode(CommonUtil.convertObjToStr(cbmModeSanction.getKeyForSelected()));
            objTermLoanSanctionTO.setRemarks(CommonUtil.convertObjToStr(txtRenewalSanctionRemarks));


            if (CommonUtil.convertObjToStr(lblRenewalAcctNo_Sanction_Disp).length() == 0) {
                objTermLoanSanctionTO.setCommand(CommonUtil.convertObjToStr("INSERT"));
                objTermLoanSanctionTO.setStatus(CommonConstants.STATUS_CREATED);
            } else if (CommonUtil.convertObjToStr(lblRenewalAcctNo_Sanction_Disp).length() > 0) {
                objTermLoanSanctionTO.setStatus(CommonConstants.STATUS_MODIFIED);
            }

//            if (getCommand().equals(INSERT)){
//                objTermLoanSanctionTO.setStatus(CommonConstants.STATUS_CREATED);
//            }else if (getCommand().equals(UPDATE)){
//                objTermLoanSanctionTO.setStatus(CommonConstants.STATUS_MODIFIED);
//            }
//            else if (getCommand().equals(DELETE)){
//                objTermLoanSanctionTO.setStatus(CommonConstants.STATUS_DELETED);
//            }
            objTermLoanSanctionTO.setStatusBy(TrueTransactMain.USER_ID);
            objTermLoanSanctionTO.setStatusDt(curDate);


            sanctionMap.put(objTermLoanSanctionTO.getSlNo(), objTermLoanSanctionTO);

            //sanction details

            objTermLoanSanctionFacilityTO = new TermLoanSanctionFacilityTO();
            objTermLoanSanctionFacilityTO.setBorrowNo(renewalBorrowerNo);
            //                    objTermLoanSanctionFacilityTO.setSanctionNo(CommonUtil.convertObjToStr( eachSanctionFacilityRec.get(SANCTION_NO)));
            objTermLoanSanctionFacilityTO.setSanctionNo(objTermLoanSanctionTO.getSanctionNo());
            objTermLoanSanctionFacilityTO.setSlNo(CommonUtil.convertObjToDouble("1"));
            objTermLoanSanctionFacilityTO.setFacilityType(CommonUtil.convertObjToStr(cbmTypeOfFacility.getKeyForSelected()));
            objTermLoanSanctionFacilityTO.setLimit(CommonUtil.convertObjToDouble(getTxtRenewalLimit_SD()));

            //                    objTermLoanSanctionFacilityTO.setFromDt((Date)eachSanctionFacilityRec.get(FROM));
            if (tdtRenewalAccountOpenDate != null && (!tdtRenewalAccountOpenDate.equals(""))) {
                objTermLoanSanctionFacilityTO.setFromDt(getProperDateFormat(tdtRenewalAccountOpenDate));
            } else if (tdtRenewalAccountOpenDate != null && tdtRenewalAccountOpenDate.equals("")) {
                tdtFDate = DateUtil.getStringDate(curDate);
            }
            objTermLoanSanctionFacilityTO.setNoInstall(CommonUtil.convertObjToDouble(txtRenewalNoInstallments));
            //                    objTermLoanSanctionFacilityTO.setToDt((Date)eachSanctionFacilityRec.get(TO));
            //                    objTermLoanSanctionFacilityTO.setToDt(DateUtil.getDateMMDDYYYY(CommonUtil.convertObjToStr(tdtTDate)));
            //                    objTermLoanSanctionFacilityTO.setRepay
            Date tdtTDate = null;
            if (getCboRenewalRepayFreq().length() > 0 && getCboRenewalRepayFreq().equals("Yearly")) {
                objTermLoanSanctionFacilityTO.setRepaymentFrequency(new Double(360));
                tdtTDate = DateUtil.getDateMMDDYYYY(tdtFDate);
                tdtTDate = DateUtil.addDays(tdtTDate, 365);
            } else if (getCboRenewalRepayFreq().length() > 0 && getCboRenewalRepayFreq().equals("Half Yearly")) {
                objTermLoanSanctionFacilityTO.setRepaymentFrequency(new Double(180));
                tdtTDate = DateUtil.getDateMMDDYYYY(tdtFDate);
                tdtTDate = DateUtil.addDays(tdtTDate, 180);
            } else if (getCboRenewalRepayFreq().length() > 0 && getCboRenewalRepayFreq().equals("Quaterly")) {
                objTermLoanSanctionFacilityTO.setRepaymentFrequency(new Double(90));
                tdtTDate = DateUtil.getDateMMDDYYYY(tdtFDate);
                tdtTDate = DateUtil.addDays(tdtTDate, 90);
            } else if (getCboRenewalRepayFreq().length() > 0 && getCboRenewalRepayFreq().equals("Monthly")) {
                objTermLoanSanctionFacilityTO.setRepaymentFrequency(new Double(30));
                tdtTDate = DateUtil.getDateMMDDYYYY(tdtFDate);
                tdtTDate = DateUtil.addDays(tdtTDate, 30);
                objTermLoanSanctionFacilityTO.setToDt(tdtTDate);
            } else if (getCboRenewalRepayFreq().length() > 0 && getCboRenewalRepayFreq().equals("Fortnight")) {
                objTermLoanSanctionFacilityTO.setRepaymentFrequency(new Double(15));
                tdtTDate = DateUtil.getDateMMDDYYYY(tdtFDate);
                tdtTDate = DateUtil.addDays(tdtTDate, 15);
                objTermLoanSanctionFacilityTO.setToDt(tdtTDate);
            } else if (getCboRenewalRepayFreq().length() > 0 && getCboRenewalRepayFreq().equals("USER_DEFINED_GOLD_LOAN")) {// Added by nithya on 03-07-2019 for KD-546 New Gold Loan -45 days maturity type
                objTermLoanSanctionFacilityTO.setRepaymentFrequency(new Double(getMaximumDaysForRenewLoan()));
                tdtTDate = DateUtil.getDateMMDDYYYY(tdtFDate);
                tdtTDate = DateUtil.addDays(tdtTDate, getMaximumDaysForRenewLoan());
                objTermLoanSanctionFacilityTO.setToDt(tdtTDate);
            }
                        
            objTermLoanSanctionFacilityTO.setToDt(getProperDateFormat(tdtRenewalToDate));
            objTermLoanSanctionFacilityTO.setRepaymentDt(getProperDateFormat(tdtRenewalToDate));
            //                    objTermLoanSanctionFacilityTO.setRepaymentFrequency(CommonUtil.convertObjToDouble(cbmRepayFreq.getKeyForSelected()));
            objTermLoanSanctionFacilityTO.setProductId(CommonUtil.convertObjToStr(getLoadingProdId()));//cbmProductId.getKeyForSelected()));
            if (getChkMoratorium_Given()) {
                objTermLoanSanctionFacilityTO.setMoratoriumGiven("Y");
            } else {
                objTermLoanSanctionFacilityTO.setMoratoriumGiven("N");
            }
            objTermLoanSanctionFacilityTO.setNoMoratorium(CommonUtil.convertObjToDouble(getTxtFacility_Moratorium_Period()));
            if (CommonUtil.convertObjToStr(lblRenewalAcctNo_Sanction_Disp).length() == 0) {
                objTermLoanSanctionFacilityTO.setCommand(CommonUtil.convertObjToStr("INSERT"));
                objTermLoanSanctionFacilityTO.setStatus(CommonConstants.STATUS_CREATED);
            } else {
                objTermLoanSanctionFacilityTO.setCommand(CommonUtil.convertObjToStr("UPDATE"));
                objTermLoanSanctionFacilityTO.setStatus(CommonConstants.STATUS_MODIFIED);

            }

//            if (getCommand().equals(CommonConstants.TOSTATUS_DELETE))
//                objTermLoanSanctionFacilityTO.setStatus(CommonConstants.STATUS_DELETED);

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

    private HashMap setTermLoanFacility() {
        HashMap facilityMap = new HashMap();
        try {
            TermLoanFacilityTO objTermLoanFacilityTO;
            Set keySet = facilityAll.keySet();
            Object[] objKeySet = (Object[]) keySet.toArray();
            HashMap oneRecord;
            // To set the values for Facility Transfer Object
            for (int i = keySet.size() - 1, j = 0; i >= 0; --i, ++j) {
                oneRecord = (HashMap) facilityAll.get(objKeySet[j]);
                objTermLoanFacilityTO = new TermLoanFacilityTO();
                objTermLoanFacilityTO.setBorrowNo(borrowerNo);
                objTermLoanFacilityTO.setSanctionNo(CommonUtil.convertObjToStr(oneRecord.get(SANCTION_NO)));
                objTermLoanFacilityTO.setSlNo(CommonUtil.convertObjToDouble(oneRecord.get(SLNO)));
                objTermLoanFacilityTO.setProdId(CommonUtil.convertObjToStr(oneRecord.get(PROD_ID)));
                objTermLoanFacilityTO.setAcctStatus(CommonUtil.convertObjToStr(oneRecord.get(ACCT_STATUS)));
                objTermLoanFacilityTO.setAcctNum(CommonUtil.convertObjToStr(oneRecord.get(ACCT_NUM)));
                objTermLoanFacilityTO.setAcctName(CommonUtil.convertObjToStr(oneRecord.get(ACCT_NAME)));
                objTermLoanFacilityTO.setIntGetFrom(CommonUtil.convertObjToStr(oneRecord.get(INT_GET_FROM)));
                objTermLoanFacilityTO.setSecurityType(CommonUtil.convertObjToStr(oneRecord.get(SECURITY_TYPE)));
                objTermLoanFacilityTO.setSecurityDetails(CommonUtil.convertObjToStr(oneRecord.get(SECURITY_DETAILS)));
                objTermLoanFacilityTO.setAccountType(CommonUtil.convertObjToStr(oneRecord.get(ACC_TYPE)));
                //                objTermLoanFacilityTO.setInterestNature(CommonUtil.convertObjToStr( oneRecord.get(INTEREST_NATURE)));
                //                objTermLoanFacilityTO.setInterestType(CommonUtil.convertObjToStr( oneRecord.get(INTEREST_TYPE)));
                objTermLoanFacilityTO.setInterestType("FIXED_RATE");
                objTermLoanFacilityTO.setAccountLimit(CommonUtil.convertObjToStr(oneRecord.get(ACC_LIMIT)));
                objTermLoanFacilityTO.setRiskWeight(CommonUtil.convertObjToStr(oneRecord.get(RISK_WEIGHT)));

                //                objTermLoanFacilityTO.setDemandPromDt((Date)oneRecord.get(NOTE_DATE));
                objTermLoanFacilityTO.setDemandPromDt(getProperDateFormat(oneRecord.get(NOTE_DATE)));

                //                objTermLoanFacilityTO.setDemandPromExpdt((Date)oneRecord.get(NOTE_EXP_DATE));
                objTermLoanFacilityTO.setDemandPromExpdt(getProperDateFormat(oneRecord.get(NOTE_EXP_DATE)));
                objTermLoanFacilityTO.setMultiDisburse(CommonUtil.convertObjToStr(oneRecord.get(MULTI_DISBURSE)));

                //                objTermLoanFacilityTO.setAodDt((Date)oneRecord.get(AOD_DATE));
                objTermLoanFacilityTO.setAodDt(getProperDateFormat(oneRecord.get(AOD_DATE)));
                objTermLoanFacilityTO.setPurposeDesc(CommonUtil.convertObjToStr(oneRecord.get(PURPOSE_DESC)));
                objTermLoanFacilityTO.setGroupDesc(CommonUtil.convertObjToStr(oneRecord.get(GROUP_DESC)));
                objTermLoanFacilityTO.setInterest(CommonUtil.convertObjToStr(oneRecord.get(INTEREST)));
                objTermLoanFacilityTO.setDpYesNo(CommonUtil.convertObjToStr(oneRecord.get(DRAWING_POWER)));
                objTermLoanFacilityTO.setContactPerson(CommonUtil.convertObjToStr(oneRecord.get(CONTACT_PERSON)));
                objTermLoanFacilityTO.setContactPhone(CommonUtil.convertObjToStr(oneRecord.get(CONTACT_PHONE)));
                objTermLoanFacilityTO.setRemarks(CommonUtil.convertObjToStr(oneRecord.get(REMARKS)));
                objTermLoanFacilityTO.setCommand(CommonUtil.convertObjToStr(oneRecord.get(COMMAND)));
                objTermLoanFacilityTO.setAuthorizedSignatory(CommonUtil.convertObjToStr(oneRecord.get(AUTHSIGNATORY)));
                objTermLoanFacilityTO.setPofAttorney(CommonUtil.convertObjToStr(oneRecord.get(POFATTORNEY)));
                objTermLoanFacilityTO.setDocDetails(CommonUtil.convertObjToStr(oneRecord.get(DOCDETAILS)));
                objTermLoanFacilityTO.setAcctTransfer(CommonUtil.convertObjToStr(oneRecord.get(ACCTTRANSFER)));


                //                objTermLoanFacilityTO.setAccOpenDt((Date)oneRecord.get(ACCT_OPEN_DT));
                objTermLoanFacilityTO.setAccOpenDt(getProperDateFormat(oneRecord.get(ACCT_OPEN_DT)));
                if (oneRecord.containsKey(ACCT_CLOSE_DT) && oneRecord.get(ACCT_CLOSE_DT) != null) {
                    objTermLoanFacilityTO.setAccCloseDt(getProperDateFormat(oneRecord.get(ACCT_CLOSE_DT)));
                }
                objTermLoanFacilityTO.setRecommendedBy(CommonUtil.convertObjToStr(oneRecord.get(RECOMMANDED_BY)));
                if (oneRecord.get(COMMAND).equals(INSERT)) {
                    objTermLoanFacilityTO.setCreateDt(curDate);
                    objTermLoanFacilityTO.setCreatedBy(TrueTransactMain.USER_ID);
                    objTermLoanFacilityTO.setStatus(CommonConstants.STATUS_CREATED);
                    objTermLoanFacilityTO.setAvailableBalance(CommonUtil.convertObjToDouble(oneRecord.get(AVAILABLE_BALANCE)));
                    objTermLoanFacilityTO.setClearBalance(CommonUtil.convertObjToDouble(oneRecord.get(CLEAR_BALANCE)));
                    objTermLoanFacilityTO.setUnclearBalance(CommonUtil.convertObjToDouble(oneRecord.get(UNCLEAR_BALANCE)));
                    objTermLoanFacilityTO.setShadowDebit(CommonUtil.convertObjToDouble(oneRecord.get(SHADOW_DEBIT)));
                    objTermLoanFacilityTO.setShadowCredit(CommonUtil.convertObjToDouble(oneRecord.get(SHADOW_CREDIT)));
                    objTermLoanFacilityTO.setTotalBalance(CommonUtil.convertObjToDouble(oneRecord.get(TOTAL_AVAILABLE_BALANCE)));
                    objTermLoanFacilityTO.setLoanBalancePrincipal(new Double(0.0));
                    objTermLoanFacilityTO.setLoanPaidInt(new Double(0.0));
                    objTermLoanFacilityTO.setLoanPaidPenalint(new Double(0.0));
                    objTermLoanFacilityTO.setExcessAmt(new Double(0.0));
                    objTermLoanFacilityTO.setLastTransDt(curDate);
                } else if (oneRecord.get(COMMAND).equals(UPDATE)) {
                    objTermLoanFacilityTO.setStatus(CommonConstants.STATUS_MODIFIED);
                    //                    if(loanType.equals("OTHERS"))
                    objTermLoanFacilityTO.setAvailableBalance(CommonUtil.convertObjToDouble(oneRecord.get(AVAILABLE_BALANCE))); //FOR UPDATE AVAILABLE AMT
                }

                if (getCommand().equals(CommonConstants.TOSTATUS_DELETE)) {
                    if (objTermLoanFacilityTO.getSanctionNo().equals(getTxtSanctionSlNo())
                            && objTermLoanFacilityTO.getSlNo().doubleValue() == CommonUtil.convertObjToDouble(getSanctionSlNo()).doubleValue()) {
                        objTermLoanFacilityTO.setStatus(CommonConstants.STATUS_DELETED);
                    }
                }

                objTermLoanFacilityTO.setAuthorizeBy1(CommonUtil.convertObjToStr(oneRecord.get(AUTHORIZE_BY_1)));
                objTermLoanFacilityTO.setAuthorizeBy2(CommonUtil.convertObjToStr(oneRecord.get(AUTHORIZE_BY_2)));

                //                objTermLoanFacilityTO.setAuthorizeDt1((Date)oneRecord.get(AUTHORIZE_DT_1));
                objTermLoanFacilityTO.setAuthorizeDt1(getProperDateFormat(oneRecord.get(AUTHORIZE_DT_1)));

                //                objTermLoanFacilityTO.setAuthorizeDt2((Date)oneRecord.get(AUTHORIZE_DT_2));
                objTermLoanFacilityTO.setAuthorizeDt2(getProperDateFormat(oneRecord.get(AUTHORIZE_DT_2)));
                objTermLoanFacilityTO.setAuthorizeStatus1(CommonUtil.convertObjToStr(oneRecord.get(AUTHORIZE_STATUS_1)).length()>0 ? CommonUtil.convertObjToStr(oneRecord.get(AUTHORIZE_STATUS_1)) : null);
                objTermLoanFacilityTO.setAuthorizeStatus2(CommonUtil.convertObjToStr(oneRecord.get(AUTHORIZE_STATUS_2)));
                objTermLoanFacilityTO.setStatusBy(TrueTransactMain.USER_ID);
                objTermLoanFacilityTO.setStatusDt(curDate);
                objTermLoanFacilityTO.setBranchId(getSelectedBranchID());
                if (objTermLoanFacilityTO.getAcctNum().equals(getStrACNumber())) {
                    facilityMap.put(String.valueOf(j + 1), objTermLoanFacilityTO);
                    slNoForSanction = objTermLoanFacilityTO.getSlNo().doubleValue();
                }

                objTermLoanFacilityTO = null;
                if (oneRecord.get(COMMAND).equals(INSERT)) {
                    TermLoanExtenFacilityDetailsTO termLoanExtenFacilityDetailsTO = new TermLoanExtenFacilityDetailsTO();

                    //                      agriSubSidyOB.setAgriSubSidyTo();//dontdelete
                }
                oneRecord = null;
            }
            keySet = null;
            objKeySet = null;

            Set removedKeySet = removedFaccilityTabValues.keySet();
            Object[] objRemovedKeySet = (Object[]) removedKeySet.toArray();
            // To set the values for Facility Transfer Object
            // where as the existing records in Database are deleted in client side
            // useful for updating the status
            for (int i = removedFaccilityTabValues.size() - 1, j = 0; i >= 0; --i, ++j) {
                oneRecord = (HashMap) removedFaccilityTabValues.get(objRemovedKeySet[j]);
                objTermLoanFacilityTO = new TermLoanFacilityTO();
                objTermLoanFacilityTO.setBorrowNo(borrowerNo);
                objTermLoanFacilityTO.setSanctionNo(CommonUtil.convertObjToStr(oneRecord.get(SANCTION_NO)));
                objTermLoanFacilityTO.setSlNo(CommonUtil.convertObjToDouble(oneRecord.get(SLNO)));
                objTermLoanFacilityTO.setProdId(CommonUtil.convertObjToStr(oneRecord.get(PROD_ID)));
                objTermLoanFacilityTO.setAcctStatus(CommonUtil.convertObjToStr(oneRecord.get(ACCT_STATUS)));
                objTermLoanFacilityTO.setAcctName(CommonUtil.convertObjToStr(oneRecord.get(ACCT_NAME)));
                objTermLoanFacilityTO.setIntGetFrom(CommonUtil.convertObjToStr(oneRecord.get(INT_GET_FROM)));
                objTermLoanFacilityTO.setSecurityType(CommonUtil.convertObjToStr(oneRecord.get(SECURITY_TYPE)));
                objTermLoanFacilityTO.setSecurityDetails(CommonUtil.convertObjToStr(oneRecord.get(SECURITY_DETAILS)));
                objTermLoanFacilityTO.setAccountType(CommonUtil.convertObjToStr(oneRecord.get(ACC_TYPE)));
                //                objTermLoanFacilityTO.setInterestNature(CommonUtil.convertObjToStr( oneRecord.get(INTEREST_NATURE)));
                objTermLoanFacilityTO.setInterestType(CommonUtil.convertObjToStr(oneRecord.get(INTEREST_TYPE)));
                objTermLoanFacilityTO.setAccountLimit(CommonUtil.convertObjToStr(oneRecord.get(ACC_LIMIT)));
                objTermLoanFacilityTO.setRiskWeight(CommonUtil.convertObjToStr(oneRecord.get(RISK_WEIGHT)));

                //                objTermLoanFacilityTO.setDemandPromDt((Date)oneRecord.get(NOTE_DATE));
                objTermLoanFacilityTO.setDemandPromDt(getProperDateFormat(oneRecord.get(NOTE_DATE)));

                //                objTermLoanFacilityTO.setDemandPromExpdt((Date)oneRecord.get(NOTE_EXP_DATE));
                objTermLoanFacilityTO.setDemandPromExpdt(getProperDateFormat(oneRecord.get(NOTE_EXP_DATE)));
                objTermLoanFacilityTO.setMultiDisburse(CommonUtil.convertObjToStr(oneRecord.get(MULTI_DISBURSE)));

                //                objTermLoanFacilityTO.setAodDt((Date)oneRecord.get(AOD_DATE));
                objTermLoanFacilityTO.setAodDt(getProperDateFormat(oneRecord.get(AOD_DATE)));
                objTermLoanFacilityTO.setPurposeDesc(CommonUtil.convertObjToStr(oneRecord.get(PURPOSE_DESC)));
                objTermLoanFacilityTO.setGroupDesc(CommonUtil.convertObjToStr(oneRecord.get(GROUP_DESC)));
                objTermLoanFacilityTO.setInterest(CommonUtil.convertObjToStr(oneRecord.get(INTEREST)));
                objTermLoanFacilityTO.setContactPerson(CommonUtil.convertObjToStr(oneRecord.get(CONTACT_PERSON)));
                objTermLoanFacilityTO.setContactPhone(CommonUtil.convertObjToStr(oneRecord.get(CONTACT_PHONE)));
                objTermLoanFacilityTO.setRemarks(CommonUtil.convertObjToStr(oneRecord.get(REMARKS)));
                objTermLoanFacilityTO.setCommand(CommonUtil.convertObjToStr(oneRecord.get(COMMAND)));
                objTermLoanFacilityTO.setStatus(CommonConstants.STATUS_DELETED);
                objTermLoanFacilityTO.setStatusBy(TrueTransactMain.USER_ID);
                objTermLoanFacilityTO.setStatusDt(curDate);
                objTermLoanFacilityTO.setAuthorizeBy1(CommonUtil.convertObjToStr(oneRecord.get(AUTHORIZE_BY_1)));
                objTermLoanFacilityTO.setAuthorizeBy2(CommonUtil.convertObjToStr(oneRecord.get(AUTHORIZE_BY_2)));

                //                objTermLoanFacilityTO.setAuthorizeDt1((Date)oneRecord.get(AUTHORIZE_DT_1));
                objTermLoanFacilityTO.setAuthorizeDt1(getProperDateFormat(oneRecord.get(AUTHORIZE_DT_1)));

                //                objTermLoanFacilityTO.setAuthorizeDt2((Date)oneRecord.get(AUTHORIZE_DT_2));
                objTermLoanFacilityTO.setAuthorizeDt2(getProperDateFormat(oneRecord.get(AUTHORIZE_DT_2)));
                objTermLoanFacilityTO.setAuthorizeStatus1(CommonUtil.convertObjToStr(oneRecord.get(AUTHORIZE_STATUS_1)).length()>0 ? CommonUtil.convertObjToStr(oneRecord.get(AUTHORIZE_STATUS_1)) : null);
                objTermLoanFacilityTO.setAuthorizeStatus2(CommonUtil.convertObjToStr(oneRecord.get(AUTHORIZE_STATUS_2)));
                objTermLoanFacilityTO.setBranchId(getSelectedBranchID());
                facilityMap.put(String.valueOf(facilityMap.size() + 1), objTermLoanFacilityTO);
                oneRecord = null;
                objTermLoanFacilityTO = null;
            }
            removedKeySet = null;
            objRemovedKeySet = null;
        } catch (Exception e) {
            log.info("Exception caught In setTermLoanFacility: " + e);
            parseException.logException(e, true);
        }
        return facilityMap;
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
            objTermLoanFacilityTO.setProdId(CommonUtil.convertObjToStr(getLoadingProdId()));
            //                objTermLoanFacilityTO.setProdId(CommonUtil.convertObjToStr( facilityRecord.get(PROD_ID)));
            if (isRenewalYesNo()) {
                objTermLoanFacilityTO.setAcctStatus(CommonUtil.convertObjToStr("RENEWAL"));
            } else {
                objTermLoanFacilityTO.setAcctStatus(CommonUtil.convertObjToStr(facilityRecord.get(ACCT_STATUS)));
            }
            objTermLoanFacilityTO.setAcctNum(CommonUtil.convertObjToStr(facilityRecord.get(ACCT_NUM)));
            objTermLoanFacilityTO.setAcctName(CommonUtil.convertObjToStr(facilityRecord.get(ACCT_NAME)));
            objTermLoanFacilityTO.setIntGetFrom(String.valueOf("ACT"));
            objTermLoanFacilityTO.setSecurityType(CommonUtil.convertObjToStr(facilityRecord.get(SECURITY_TYPE)));
            objTermLoanFacilityTO.setSecurityDetails(String.valueOf("FULLY_SECURED"));
            objTermLoanFacilityTO.setAccountType(CommonUtil.convertObjToStr(facilityRecord.get(ACC_TYPE)));
            //                objTermLoanFacilityTO.setInterestNature(CommonUtil.convertObjToStr( oneRecord.get(INTEREST_NATURE)));
            objTermLoanFacilityTO.setInterestType(String.valueOf("FIXED_RATE"));
            objTermLoanFacilityTO.setAccountLimit(String.valueOf("MAIN"));
            objTermLoanFacilityTO.setRiskWeight(String.valueOf("N"));

            //                objTermLoanFacilityTO.setDemandPromDt((Date)oneRecord.get(NOTE_DATE));
            objTermLoanFacilityTO.setDemandPromDt(getProperDateFormat(facilityRecord.get(NOTE_DATE)));

            //                objTermLoanFacilityTO.setDemandPromExpdt((Date)oneRecord.get(NOTE_EXP_DATE));
            objTermLoanFacilityTO.setDemandPromExpdt(getProperDateFormat(facilityRecord.get(NOTE_EXP_DATE)));
            objTermLoanFacilityTO.setMultiDisburse(String.valueOf("Y"));

            //                objTermLoanFacilityTO.setAodDt((Date)oneRecord.get(AOD_DATE));
            objTermLoanFacilityTO.setAodDt(getProperDateFormat(facilityRecord.get(AOD_DATE)));
            objTermLoanFacilityTO.setPurposeDesc(CommonUtil.convertObjToStr(facilityRecord.get(PURPOSE_DESC)));
            objTermLoanFacilityTO.setGroupDesc(CommonUtil.convertObjToStr(facilityRecord.get(GROUP_DESC)));
            objTermLoanFacilityTO.setInterest(String.valueOf("SIMPLE"));
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
            objTermLoanFacilityTO.setAccOpenDt(getProperDateFormat(facilityRecord.get(ACCT_OPEN_DT)));
            if (facilityRecord.containsKey(ACCT_CLOSE_DT) && facilityRecord.get(ACCT_CLOSE_DT) != null) {
                objTermLoanFacilityTO.setAccCloseDt(getProperDateFormat(facilityRecord.get(ACCT_CLOSE_DT)));
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
                if (getAvailableBalance() == 0) {
                    objTermLoanFacilityTO.setAvailableBalance(new Double(0));
                } else {
                    objTermLoanFacilityTO.setAvailableBalance(CommonUtil.convertObjToDouble(getTxtLimit_SD()));
                }
            }

            if (getCommand().equals(CommonConstants.TOSTATUS_DELETE)) {
                if (objTermLoanFacilityTO.getSanctionNo().equals(getTxtSanctionSlNo())
                        && objTermLoanFacilityTO.getSlNo().doubleValue() == CommonUtil.convertObjToDouble(getSanctionSlNo()).doubleValue()) {
                    objTermLoanFacilityTO.setStatus(CommonConstants.STATUS_DELETED);
                }
            }

            objTermLoanFacilityTO.setAuthorizeBy1(CommonUtil.convertObjToStr(facilityRecord.get(AUTHORIZE_BY_1)));
            objTermLoanFacilityTO.setAuthorizeBy2(CommonUtil.convertObjToStr(facilityRecord.get(AUTHORIZE_BY_2)));

            //                objTermLoanFacilityTO.setAuthorizeDt1((Date)oneRecord.get(AUTHORIZE_DT_1));
            objTermLoanFacilityTO.setAuthorizeDt1(getProperDateFormat(facilityRecord.get(AUTHORIZE_DT_1)));

            //                objTermLoanFacilityTO.setAuthorizeDt2((Date)oneRecord.get(AUTHORIZE_DT_2));
            objTermLoanFacilityTO.setAuthorizeDt2(getProperDateFormat(facilityRecord.get(AUTHORIZE_DT_2)));
            objTermLoanFacilityTO.setAuthorizeStatus1(CommonUtil.convertObjToStr(facilityRecord.get(AUTHORIZE_STATUS_1)).length()>0 ? CommonUtil.convertObjToStr(facilityRecord.get(AUTHORIZE_STATUS_1)) : null);
            objTermLoanFacilityTO.setAuthorizeStatus2(CommonUtil.convertObjToStr(facilityRecord.get(AUTHORIZE_STATUS_2)));
            objTermLoanFacilityTO.setStatusBy(TrueTransactMain.USER_ID);
            objTermLoanFacilityTO.setStatusDt(curDate);            
            if(getSelectedBranchID() != null && CommonUtil.convertObjToStr(getSelectedBranchID()).length() > 0){ // Added by nithya on 06-12-2019 for KD-992
             objTermLoanFacilityTO.setBranchId(getSelectedBranchID());
            }else{
             objTermLoanFacilityTO.setBranchId(TrueTransactMain.BRANCH_ID);   
            }
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

    private HashMap setTermLoanRenewalFacilitySingleRecord() {
        HashMap facilityMap = new HashMap();
        try {
            TermLoanFacilityTO objTermLoanFacilityTO;
            Set keySet = facilityAll.keySet();
            Object[] objKeySet = (Object[]) keySet.toArray();
            HashMap oneRecord;
            // To set the values for Facility Transfer Object

            objTermLoanFacilityTO = new TermLoanFacilityTO();
            objTermLoanFacilityTO.setBorrowNo(borrowerNo);
            objTermLoanFacilityTO.setSanctionNo(CommonUtil.convertObjToStr(renewalFacilityRecord.get(SANCTION_NO)));
            objTermLoanFacilityTO.setSlNo(CommonUtil.convertObjToDouble(renewalFacilityRecord.get(SLNO)));
            objTermLoanFacilityTO.setProdId(CommonUtil.convertObjToStr(getLoadingProdId()));
            //                objTermLoanFacilityTO.setProdId(CommonUtil.convertObjToStr( facilityRecord.get(PROD_ID)));
            objTermLoanFacilityTO.setAcctStatus(CommonUtil.convertObjToStr(renewalFacilityRecord.get(ACCT_STATUS)));
            objTermLoanFacilityTO.setAcctNum(CommonUtil.convertObjToStr(renewalFacilityRecord.get(ACCT_NUM)));
            objTermLoanFacilityTO.setRenewalAcctNo(CommonUtil.convertObjToStr(renewalFacilityRecord.get(RENEWAL_ACCT_NUM)));
            objTermLoanFacilityTO.setAcctName(CommonUtil.convertObjToStr(renewalFacilityRecord.get(ACCT_NAME)));
            objTermLoanFacilityTO.setIntGetFrom(String.valueOf("ACT"));
            objTermLoanFacilityTO.setSecurityType(CommonUtil.convertObjToStr(renewalFacilityRecord.get(SECURITY_TYPE)));
            objTermLoanFacilityTO.setSecurityDetails(String.valueOf("FULLY_SECURED"));
            objTermLoanFacilityTO.setAccountType(CommonUtil.convertObjToStr(renewalFacilityRecord.get(ACC_TYPE)));
            //                objTermLoanFacilityTO.setInterestNature(CommonUtil.convertObjToStr( oneRecord.get(INTEREST_NATURE)));
            objTermLoanFacilityTO.setInterestType(String.valueOf("FIXED_RATE"));
            objTermLoanFacilityTO.setAccountLimit(String.valueOf("MAIN"));
            objTermLoanFacilityTO.setRiskWeight(String.valueOf("N"));

            //                objTermLoanFacilityTO.setDemandPromDt((Date)oneRecord.get(NOTE_DATE));
            objTermLoanFacilityTO.setDemandPromDt(getProperDateFormat(renewalFacilityRecord.get(NOTE_DATE)));

            //                objTermLoanFacilityTO.setDemandPromExpdt((Date)oneRecord.get(NOTE_EXP_DATE));
            objTermLoanFacilityTO.setDemandPromExpdt(getProperDateFormat(renewalFacilityRecord.get(NOTE_EXP_DATE)));
            objTermLoanFacilityTO.setMultiDisburse(String.valueOf("Y"));

            //                objTermLoanFacilityTO.setAodDt((Date)oneRecord.get(AOD_DATE));
            objTermLoanFacilityTO.setAodDt(getProperDateFormat(renewalFacilityRecord.get(AOD_DATE)));
            objTermLoanFacilityTO.setPurposeDesc(CommonUtil.convertObjToStr(renewalFacilityRecord.get(PURPOSE_DESC)));
            objTermLoanFacilityTO.setGroupDesc(CommonUtil.convertObjToStr(renewalFacilityRecord.get(GROUP_DESC)));
            objTermLoanFacilityTO.setInterest(String.valueOf("SIMPLE"));
            objTermLoanFacilityTO.setDpYesNo(CommonUtil.convertObjToStr(renewalFacilityRecord.get(DRAWING_POWER)));
            objTermLoanFacilityTO.setContactPerson(CommonUtil.convertObjToStr(renewalFacilityRecord.get(CONTACT_PERSON)));
            objTermLoanFacilityTO.setContactPhone(CommonUtil.convertObjToStr(renewalFacilityRecord.get(CONTACT_PHONE)));
            objTermLoanFacilityTO.setRemarks(CommonUtil.convertObjToStr(renewalFacilityRecord.get(REMARKS)));

            objTermLoanFacilityTO.setAuthorizedSignatory(CommonUtil.convertObjToStr(renewalFacilityRecord.get(AUTHSIGNATORY)));
            objTermLoanFacilityTO.setPofAttorney(CommonUtil.convertObjToStr(renewalFacilityRecord.get(POFATTORNEY)));
            objTermLoanFacilityTO.setDocDetails(CommonUtil.convertObjToStr(renewalFacilityRecord.get(DOCDETAILS)));
            objTermLoanFacilityTO.setAcctTransfer(CommonUtil.convertObjToStr(renewalFacilityRecord.get(ACCTTRANSFER)));


            //                objTermLoanFacilityTO.setAccOpenDt((Date)oneRecord.get(ACCT_OPEN_DT));
            objTermLoanFacilityTO.setAccOpenDt(getProperDateFormat(renewalFacilityRecord.get(ACCT_OPEN_DT)));
            if (renewalFacilityRecord.containsKey(ACCT_CLOSE_DT) && renewalFacilityRecord.get(ACCT_CLOSE_DT) != null) {
                objTermLoanFacilityTO.setAccCloseDt(getProperDateFormat(renewalFacilityRecord.get(ACCT_CLOSE_DT)));
            }
            objTermLoanFacilityTO.setRecommendedBy(CommonUtil.convertObjToStr(renewalFacilityRecord.get(RECOMMANDED_BY)));
            if (CommonUtil.convertObjToStr(getLblRenewalAcctNo_Sanction_Disp()).length() == 0) {
                objTermLoanFacilityTO.setCommand(CommonUtil.convertObjToStr("INSERT"));
                objTermLoanFacilityTO.setCreateDt(curDate);
                objTermLoanFacilityTO.setCreatedBy(TrueTransactMain.USER_ID);
                objTermLoanFacilityTO.setStatus(CommonConstants.STATUS_CREATED);
                objTermLoanFacilityTO.setAvailableBalance(CommonUtil.convertObjToDouble(renewalFacilityRecord.get(AVAILABLE_BALANCE)));
                objTermLoanFacilityTO.setClearBalance(CommonUtil.convertObjToDouble(renewalFacilityRecord.get(CLEAR_BALANCE)));
                objTermLoanFacilityTO.setUnclearBalance(CommonUtil.convertObjToDouble(renewalFacilityRecord.get(UNCLEAR_BALANCE)));
                objTermLoanFacilityTO.setShadowDebit(CommonUtil.convertObjToDouble(renewalFacilityRecord.get(SHADOW_DEBIT)));
                objTermLoanFacilityTO.setShadowCredit(CommonUtil.convertObjToDouble(renewalFacilityRecord.get(SHADOW_CREDIT)));
                objTermLoanFacilityTO.setTotalBalance(CommonUtil.convertObjToDouble(renewalFacilityRecord.get(TOTAL_AVAILABLE_BALANCE)));
                objTermLoanFacilityTO.setLoanBalancePrincipal(new Double(0.0));
                objTermLoanFacilityTO.setLoanPaidInt(new Double(0.0));
                objTermLoanFacilityTO.setLoanPaidPenalint(new Double(0.0));
                objTermLoanFacilityTO.setExcessAmt(new Double(0.0));
                objTermLoanFacilityTO.setLastTransDt(curDate);
               } else if (CommonUtil.convertObjToStr(getLblRenewalAcctNo_Sanction_Disp()).length() > 0) {
                objTermLoanFacilityTO.setStatus(CommonConstants.STATUS_MODIFIED);
                objTermLoanFacilityTO.setCommand(CommonUtil.convertObjToStr("UPDATE"));
                if(getRnwLoadingProdId()!=null && getRnwLoadingProdId().length()>0)
                 objTermLoanFacilityTO.setProdId(CommonUtil.convertObjToStr(getRnwLoadingProdId()));
                if (getAvailableBalance() == 0) {
                    objTermLoanFacilityTO.setAvailableBalance(new Double(0));
                } else {
                    objTermLoanFacilityTO.setAvailableBalance(CommonUtil.convertObjToDouble(getTxtLimit_SD()));
                }
            }

//            if (getCommand().equals(CommonConstants.TOSTATUS_DELETE))
//                if (objTermLoanFacilityTO.getSanctionNo().equals(getTxtSanctionSlNo()) &&
//                objTermLoanFacilityTO.getSlNo().doubleValue() == CommonUtil.convertObjToDouble(getSanctionSlNo()).doubleValue()) {
//                    objTermLoanFacilityTO.setStatus(CommonConstants.STATUS_DELETED);
//                }

            objTermLoanFacilityTO.setAuthorizeBy1(CommonUtil.convertObjToStr(renewalFacilityRecord.get(AUTHORIZE_BY_1)));
            objTermLoanFacilityTO.setAuthorizeBy2(CommonUtil.convertObjToStr(renewalFacilityRecord.get(AUTHORIZE_BY_2)));

            //                objTermLoanFacilityTO.setAuthorizeDt1((Date)oneRecord.get(AUTHORIZE_DT_1));
            objTermLoanFacilityTO.setAuthorizeDt1(getProperDateFormat(renewalFacilityRecord.get(AUTHORIZE_DT_1)));

            //                objTermLoanFacilityTO.setAuthorizeDt2((Date)oneRecord.get(AUTHORIZE_DT_2));
            objTermLoanFacilityTO.setAuthorizeDt2(getProperDateFormat(renewalFacilityRecord.get(AUTHORIZE_DT_2)));
            objTermLoanFacilityTO.setAuthorizeStatus1(CommonUtil.convertObjToStr(renewalFacilityRecord.get(AUTHORIZE_STATUS_1)).length()>0 ? CommonUtil.convertObjToStr(renewalFacilityRecord.get(AUTHORIZE_STATUS_1)) : null);
            objTermLoanFacilityTO.setAuthorizeStatus2(CommonUtil.convertObjToStr(renewalFacilityRecord.get(AUTHORIZE_STATUS_2)));
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



//            if (objTermLoanFacilityTO.getAcctNum().equals(getStrACNumber())) {  only insert 
            facilityMap.put(String.valueOf(objTermLoanFacilityTO.getSlNo()), objTermLoanFacilityTO);
            slNoForSanction = objTermLoanFacilityTO.getSlNo().doubleValue();
//            }

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

    /**
     * This method will reset all the fields of TermLoanUI
     */
    public void resetForm() {
        //  Borrower Details
        termLoanBorrowerOB.resetBorrowerDetails();
        //  Company Details
//        termLoanCompanyOB.resetCustomerDetails();
        // Sanction Facility
        resetSanctionFacility();
        // Sanction Main Table
        resetSanctionMain();
        termLoanSecurityOB.setTxtEligibleLoan("");
        termLoanSecurityOB.setSecurityValue("");
        termLoanSecurityOB.setTxtAppraiserId("");

        termLoanInterestOB.setTxtInter("");
        termLoanInterestOB.setPenalInter("");
        
        serviceTax_Map = null;// Added by nithya on 09-10-2018 for KD 263 GST - Needed to implement the GST in GOld Loan renewal
        lblServiceTaxval=""; // Added by nithya on 09-10-2018 for KD 263 GST - Needed to implement the GST in GOld Loan renewal

        // Facility Details Table
        resetFacilityDetails();
        setAllLoanAmount(null);
        //        agriSubLimitOB.resetFormComponets();
        //        agriSubLimitOB.resetFormComponetsSubLimit();
        //        agriSubLimitOB.destroyObjects();
        //        agriSubLimitOB.notifyObservers();
        // Reset the tabs based on the Account Number
        //        agriSubSidyOB.resetSubsidyDetails();//dontdelete
        //        settlementOB.resetAllFieldsInPoA();//dontdelete
        //        settlementOB.resetSetBnkForm();//dontdelete
        //        actTransOB.resetAcctTransfer();//dontdelete
        resetBasedOnAcctNumber();
        resetRenewalDetails();
        resetMobileBankingDetails(); // Added by nithya
        setPhotoByteArray(null);// Added by nithya on 29-10-2019 for KD-763 Need Gold ornaments photo saving option
        setRenewalPhotoByteArray(null);// Added by nithya on 29-10-2019 for KD-763 Need Gold ornaments photo saving option
        setServiceTax_Map(null);
//        termLoanAdditionalSanctionOB.resetPeakSanctionDetails();
        destroyObjects();
        createObject();
        ttNotifyObservers();
    }

    private void resetMobileBankingDetails(){
        setTxtMobileNo("");
        setTdtMobileSubscribedFrom(""); 
        setIsMobileBanking(false);
    }
    
    private void resetRenewalDetails() {

        setTdtRenewalSanctionDate("");
        setCboRenewalSanctioningAuthority("");
        setRdoRenewalPriority(false);
        setRdoRenewalNonPriority(false);
        cbmRenewalPurposeOfLoan.setKeyForSelected("");
        setTxtRenewalSanctionRemarks("");
        setTxtRenewalLimit_SD("");
        setTdtRenewalAccountOpenDate("");
        cbmRenewalAccStatus.setKeyForSelected("");
        setTxtRenewalNoInstallments("");
        cbmRenewalRepayFreq.setKeyForSelected("");
        setTdtRenewalDemandPromNoteExpDate("");
        setTdtRenewalDemandPromNoteDate("");
        setTxtRenewalInter("");
        setTxtRenewalPenalInter("");
        setLblRenewalAcctNo_Sanction_Disp("");
        setCboRenewalPurposeOfLoan("");
        setCboRenewalRepayFreq("");
//        setTxtRenewalGrossWeight("");
//        setTxtRenewalNetWeight("");
//        cbmRenewalPurityOfGold.setKeyForSelected("");
//        setTxtRenewalMarketRate("");
//        setTxtRenewalSecurityValue("");
//        setTxtRenewalAreaParticular("");
//        setTxtRenewalMargin("");
//        setTxtRenewalMargin("");
//        setTxtRenewalEligibleLoan("");
//        cbmRenewalAppraiserId.setKeyForSelected("");
//observable. lblRenewalAppraiserNameValue
        setChargelst(null);
    }

    /**
     * This method will reset the tabs which are all dependent to term loan
     * account number
     */
    public void resetBasedOnAcctNumber() {
        // Security Details Table
        termLoanSecurityOB.resetAllSecurityDetails();
        // Repayment Schedule
        termLoanRepaymentOB.resetAllRepayment();
        // Interest Details
        termLoanInterestOB.resetAllInterestDetails();
        //        if (loanType.equals("OTHERS")) {
        // Guarantor Details
//        termLoanGuarantorOB.resetAllGuarantorDetails();
        //        }
        // Document Details
        termLoanDocumentDetailsOB.resetAllDocumentDetails();
        // Classification Details
        termLoanClassificationOB.resetClassificationDetails();
        // Other Details
//        termLoanOtherDetailsOB.resetOtherDetailsFields();

//        termLoanAdditionalSanctionOB.resetPeakSanctionDetails();
        // Set Account Number as null
        resetStrAcNumber();
    }

    public void resetSanctionFacility() {
        setProdIDAsBlank();
        setCboProductId("");
        getCbmProductId().setKeyForSelected("");
        setLblAccHead_2("");
        getCbmTypeOfFacility().setKeyForSelected("");
        setCboTypeOfFacility("");
        setTxtNoInstallments("");
        cbmRepayFreq.setKeyForSelected("");
        setCboRepayFreq("");
        setTxtLimit_SD("");
        setTxtLimit_SDMoneyDeposit("");
        setTxtFacility_Moratorium_Period("");
        setTdtFacility_Repay_Date("");
        setChkMoratorium_Given(false);
        setTdtFDate("");
        setTdtTDate("");
        setMaxLimitValue(CommonUtil.convertObjToDouble("0"));
        setMaxLoanPeriod(CommonUtil.convertObjToDouble("0"));
        setMinLimitValue(CommonUtil.convertObjToDouble("0"));
        setMinLoanPeriod(CommonUtil.convertObjToDouble("0"));
        setTxtPeriodDifference_Days("");
        setTxtPeriodDifference_Months("");
        setTxtPeriodDifference_Years("");
        setMinDecLoanPeriod("0");
        setMaxDecLoanPeriod("0");
        setLblDepositNo("");
        resetStrAcNumber();
        setPartReject("");
        setChargelst(null);
    }

    public void resetStrAcNumber() {
        // Set Account Number as null
        setStrACNumber("");
    }

    public void resetAllFacilityDetails() {
        resetFacilityDetails();
        resetFacilityTabSubsidy();
        resetFacilityTabInterestNature();
        resetFacilityTabContactDetails();
        nomineeTOList = null;
    }

    public void resetFacilityTabInterestNature() {
        setRdoNatureInterest_NonPLR(false);
        setRdoNatureInterest_PLR(false);
    }

    public void resetFacilityTabSubsidy() {
        setRdoSubsidy_No(false);
        setRdoSubsidy_Yes(false);
    }

    public void resetFacilityTabContactDetails() {
        setTxtContactPerson("");
        setTxtContactPhone("");
    }

    public void resetFacilityDetails() {
        setLblAccountHead_FD_Disp("");
        setLblProductID_FD_Disp("");
        resetStrAcNumber();
        setCboAccStatus("");
        cbmIntGetFrom.setKeyForSelected("");
        setCboIntGetFrom("");
        setRdoSecurityDetails_Unsec(false);
        setRdoSecurityDetails_Partly(false);
        setRdoSecurityDetails_Fully(false);
        setTxtAcct_Name("");
        setChkStockInspect(false);
        setChkInsurance(false);
        setChkGurantor(false);
        setRdoAccType_New(false);
        setRdoAccType_Transfered(false);
        setRdoAccLimit_Main(false);
        setRdoAccLimit_Submit(false);
        setRdoRiskWeight_Yes(false);
        setRdoRiskWeight_No(false);
        setCboInterestType("");
        setTdtDemandPromNoteDate("");
        setAccountOpenDate("");
        setCboRecommendedByType("");
        setTdtDemandPromNoteExpDate("");
        setTdtAODDate("");
        setRdoMultiDisburseAllow_No(false);
        setRdoMultiDisburseAllow_Yes(false);
        setTxtPurposeDesc("");
        setTxtGroupDesc("");
        setRdoInterest_Compound(false);
        setRdoInterest_Simple(false);
        setTxtRemarks("");
        setRdoDP_YES(false);
        setRdoDP__NO(false);
        setChkAuthorizedSignatory(false);
        setChkDocDetails(false);
        setChkGurantor(false);
        //        setChkPOFAttorney(false);
        setClearBalance(0);
        setAvailableBalance(0);
        setChargelst(null);
    }

    public void resetSanctionFacilityTable() {
        tblSanctionFacility = new EnhancedTableModel(null, sanctionFacilityTitle);
    }

    public void resetSanctionMain() {
        tblSanctionFacility = new EnhancedTableModel(null, sanctionFacilityTitle);
        setTxtSanctionNo("");
        setTxtSanctionSlNo("");
        setTdtSanctionDate("");
        setCboSanctioningAuthority("");
        setTxtSanctionRemarks("");
        setCboModeSanction("");
    }

    /**
     * will return the action type
     *
     * @return the appropriate action taken
     */
    public int getActionType() {
        log.info("In getActionType...");
        return actionType;
    }

    /**
     * will set the action type whether it is new or edit or delete
     *
     * @param actionType is new or edit or delete
     */
    public void setActionType(int actionType) {
        log.info("In setActionType: " + actionType);
        this.actionType = actionType;
        setChanged();
    }

    /**
     * It will set the result of the query executed
     *
     * @param resultStatus is the integer value which gives whether the data are
     * inserted or updated or deleted or the execution is failed
     */
    public void setResult(int resultStatus) {
        log.info("In setResult...");
        this.resultStatus = resultStatus;
        setChanged();
    }

    /**
     * Return the result of the executed query
     *
     * @return the result of the executed query
     */
    public int getResult() {
        log.info("In getResult...");
        return resultStatus;
    }

    void setTxtSanctionNo(String txtSanctionNo) {
        this.txtSanctionNo = txtSanctionNo;
        setChanged();
    }

    String getTxtSanctionNo() {
        return this.txtSanctionNo;
    }

    void setTdtSanctionDate(String tdtSanctionDate) {
        this.tdtSanctionDate = tdtSanctionDate;
        setChanged();
    }

    String getTdtSanctionDate() {
        return this.tdtSanctionDate;
    }

    void setCbmSanctioningAuthority(ComboBoxModel cbmSanctioningAuthority) {
        this.cbmSanctioningAuthority = cbmSanctioningAuthority;
        setChanged();
    }

    ComboBoxModel getCbmSanctioningAuthority() {
        return this.cbmSanctioningAuthority;
    }

    void setCboSanctioningAuthority(String cboSanctioningAuthority) {
        this.cboSanctioningAuthority = cboSanctioningAuthority;
        setChanged();
    }

    String getCboSanctioningAuthority() {
        return this.cboSanctioningAuthority;
    }

    void setTxtSanctionRemarks(String txtSanctionRemarks) {
        this.txtSanctionRemarks = txtSanctionRemarks;
        setChanged();
    }

    String getTxtSanctionRemarks() {
        return this.txtSanctionRemarks;
    }

    void setCbmModeSanction(ComboBoxModel cbmModeSanction) {

        this.cbmModeSanction = cbmModeSanction;
        setChanged();
    }

    ComboBoxModel getCbmModeSanction() {
        return this.cbmModeSanction;
    }

    void setCboModeSanction(String cboModeSanction) {
        this.cboModeSanction = cboModeSanction;
        setChanged();
    }

    String getCboModeSanction() {
        return this.cboModeSanction;
    }

    void setTxtNoInstallments(String txtNoInstallments) {
        this.txtNoInstallments = txtNoInstallments;
        setChanged();
    }

    String getTxtNoInstallments() {
        return this.txtNoInstallments;
    }

    void setCbmRepayFreq(ComboBoxModel cbmRepayFreq) {
        this.cbmRepayFreq = cbmRepayFreq;
        setChanged();
    }

    ComboBoxModel getCbmRepayFreq() {
        return this.cbmRepayFreq;
    }

    void setCboRepayFreq(String cboRepayFreq) {
        this.cboRepayFreq = cboRepayFreq;
        setChanged();
    }

    String getCboRepayFreq() {
        return this.cboRepayFreq;
    }

    public void setCbmTypeOfFacility(ComboBoxModel cbmTypeOfFacility) {
        this.cbmTypeOfFacility = cbmTypeOfFacility;
        setChanged();
    }

    public ComboBoxModel getCbmTypeOfFacility() {
        return this.cbmTypeOfFacility;
    }

    // Update the cboTypeOfFacility in UI whenever the value changed since it is
    // not updated in update() method of TermLoanUI
    void setCboTypeOfFacility(String cboTypeOfFacility) {
        this.cboTypeOfFacility = cboTypeOfFacility;
        setChanged();
    }

    String getCboTypeOfFacility() {
        return this.cboTypeOfFacility;
    }

    void setTxtLimit_SD(String txtLimit_SD) {
        this.txtLimit_SD = txtLimit_SD;
        termLoanRepaymentOB.setStrLimitAmt(this.txtLimit_SD);
        setChanged();
    }

    String getTxtLimit_SD() {
        return this.txtLimit_SD;
    }

    public void setCbmAccStatus(ComboBoxModel cbmAccStatus) {
        this.cbmAccStatus = cbmAccStatus;
        setChanged();
    }

    ComboBoxModel getCbmAccStatus() {
        return cbmAccStatus;
    }

    void setCboAccStatus(String cboAccStatus) {
        this.cboAccStatus = cboAccStatus;
        setChanged();
    }

    String getCboAccStatus() {
        return this.cboAccStatus;
    }

    void setTdtFDate(String tdtFDate) {
        this.tdtFDate = tdtFDate;
        setChanged();
    }

    String getTdtFDate() {
        return this.tdtFDate;
    }

    void setTxtFacility_Moratorium_Period(String txtFacility_Moratorium_Period) {
        this.txtFacility_Moratorium_Period = txtFacility_Moratorium_Period;
        setChanged();
    }

    String getTxtFacility_Moratorium_Period() {
        return this.txtFacility_Moratorium_Period;
    }

    void setTdtFacility_Repay_Date(String tdtFacility_Repay_Date) {
        this.tdtFacility_Repay_Date = tdtFacility_Repay_Date;
        setChanged();
    }

    String getTdtFacility_Repay_Date() {
        return this.tdtFacility_Repay_Date;
    }

    void setChkMoratorium_Given(boolean chkMoratorium_Given) {
        this.chkMoratorium_Given = chkMoratorium_Given;
        setChanged();
    }

    boolean getChkMoratorium_Given() {
        return this.chkMoratorium_Given;
    }

    void setTdtTDate(String tdtTDate) {
        this.tdtTDate = tdtTDate;
        setChanged();
    }

    String getTdtTDate() {
        return this.tdtTDate;
    }

    void setSanctionSlNo(String sanctionSlNo) {
        this.sanctionSlNo = sanctionSlNo;
        setChanged();

    }

    String getSanctionSlNo() {
        return this.sanctionSlNo;
    }

    void setCbmProductId(ComboBoxModel cbmProductId) {
        this.cbmProductId = cbmProductId;
        setChanged();
    }

    ComboBoxModel getCbmProductId() {
        return this.cbmProductId;
    }

    void setCboProductId(String cboProductId) {
        this.cboProductId = cboProductId;
        setChanged();
    }

    String getCboProductId() {
        return this.cboProductId;
    }

    void setRdoSecurityDetails_Unsec(boolean rdoSecurityDetails_Unsec) {
        this.rdoSecurityDetails_Unsec = rdoSecurityDetails_Unsec;
        setChanged();
    }

    boolean getRdoSecurityDetails_Unsec() {
        return this.rdoSecurityDetails_Unsec;
    }

    void setRdoSecurityDetails_Partly(boolean rdoSecurityDetails_Partly) {
        this.rdoSecurityDetails_Partly = rdoSecurityDetails_Partly;
        setChanged();
    }

    boolean getRdoSecurityDetails_Partly() {
        return this.rdoSecurityDetails_Partly;
    }

    /**
     * @param rdoSecurityDetails_Fully
     */
    void setRdoSecurityDetails_Fully(boolean rdoSecurityDetails_Fully) {
        this.rdoSecurityDetails_Fully = rdoSecurityDetails_Fully;
        setChanged();
    }

    boolean getRdoSecurityDetails_Fully() {
        return this.rdoSecurityDetails_Fully;
    }

    void setChkStockInspect(boolean chkStockInspect) {
        this.chkStockInspect = chkStockInspect;
        setChanged();
    }

    boolean getChkStockInspect() {
        return this.chkStockInspect;
    }

    void setChkInsurance(boolean chkInsurance) {
        this.chkInsurance = chkInsurance;
        setChanged();
    }

    boolean getChkInsurance() {
        return this.chkInsurance;
    }

    void setChkGurantor(boolean chkGurantor) {
        this.chkGurantor = chkGurantor;
        setChanged();
    }

    boolean getChkGurantor() {
        return this.chkGurantor;
    }

    void setRdoAccType_New(boolean rdoAccType_New) {
        this.rdoAccType_New = rdoAccType_New;
        setChanged();
    }

    boolean getRdoAccType_New() {
        return this.rdoAccType_New;
    }

    void setRdoAccType_Transfered(boolean rdoAccType_Transfered) {
        this.rdoAccType_Transfered = rdoAccType_Transfered;
        setChanged();
    }

    boolean getRdoAccType_Transfered() {
        return this.rdoAccType_Transfered;
    }

    void setRdoAccLimit_Main(boolean rdoAccLimit_Main) {
        this.rdoAccLimit_Main = rdoAccLimit_Main;
        setChanged();
    }

    boolean getRdoAccLimit_Main() {
        return this.rdoAccLimit_Main;
    }

    void setRdoAccLimit_Submit(boolean rdoAccLimit_Submit) {
        this.rdoAccLimit_Submit = rdoAccLimit_Submit;
        setChanged();
    }

    boolean getRdoAccLimit_Submit() {
        return this.rdoAccLimit_Submit;
    }

    void setRdoNatureInterest_PLR(boolean rdoNatureInterest_PLR) {
        this.rdoNatureInterest_PLR = rdoNatureInterest_PLR;
        setChanged();
    }

    boolean getRdoNatureInterest_PLR() {
        return this.rdoNatureInterest_PLR;
    }

    void setRdoNatureInterest_NonPLR(boolean rdoNatureInterest_NonPLR) {
        this.rdoNatureInterest_NonPLR = rdoNatureInterest_NonPLR;
        setChanged();
    }

    boolean getRdoNatureInterest_NonPLR() {
        return this.rdoNatureInterest_NonPLR;
    }

    void setCbmInterestType(ComboBoxModel cbmInterestType) {
        this.cbmInterestType = cbmInterestType;
        setChanged();
    }

    ComboBoxModel getCbmInterestType() {
        return this.cbmInterestType;
    }

    void setCboInterestType(String cboInterestType) {
        this.cboInterestType = cboInterestType;
        setChanged();
    }

    String getCboInterestType() {
        return this.cboInterestType;
    }

    void setRdoRiskWeight_Yes(boolean rdoRiskWeight_Yes) {
        this.rdoRiskWeight_Yes = rdoRiskWeight_Yes;
        setChanged();
    }

    boolean getRdoRiskWeight_Yes() {
        return this.rdoRiskWeight_Yes;
    }

    void setRdoRiskWeight_No(boolean rdoRiskWeight_No) {
        this.rdoRiskWeight_No = rdoRiskWeight_No;
        setChanged();
    }

    boolean getRdoRiskWeight_No() {
        return this.rdoRiskWeight_No;
    }

    void setTdtDemandPromNoteDate(String tdtDemandPromNoteDate) {
        this.tdtDemandPromNoteDate = tdtDemandPromNoteDate;
        setChanged();
    }

    String getTdtDemandPromNoteDate() {
        return this.tdtDemandPromNoteDate;
    }

    void setTdtDemandPromNoteExpDate(String tdtDemandPromNoteExpDate) {
        this.tdtDemandPromNoteExpDate = tdtDemandPromNoteExpDate;
        setChanged();
    }

    String getTdtDemandPromNoteExpDate() {
        return this.tdtDemandPromNoteExpDate;
    }

    void setTdtAODDate(String tdtAODDate) {
        this.tdtAODDate = tdtAODDate;
        setChanged();
    }

    String getTdtAODDate() {
        return this.tdtAODDate;
    }

    void setRdoMultiDisburseAllow_Yes(boolean rdoMultiDisburseAllow_Yes) {
        this.rdoMultiDisburseAllow_Yes = rdoMultiDisburseAllow_Yes;
        setChanged();
    }

    boolean getRdoMultiDisburseAllow_Yes() {
        return this.rdoMultiDisburseAllow_Yes;
    }

    void setRdoMultiDisburseAllow_No(boolean rdoMultiDisburseAllow_No) {
        this.rdoMultiDisburseAllow_No = rdoMultiDisburseAllow_No;
        setChanged();
    }

    boolean getRdoMultiDisburseAllow_No() {
        return this.rdoMultiDisburseAllow_No;
    }

    void setRdoSubsidy_Yes(boolean rdoSubsidy_Yes) {
        this.rdoSubsidy_Yes = rdoSubsidy_Yes;
        setChanged();
    }

    boolean getRdoSubsidy_Yes() {
        return this.rdoSubsidy_Yes;
    }

    void setRdoSubsidy_No(boolean rdoSubsidy_No) {
        this.rdoSubsidy_No = rdoSubsidy_No;
        setChanged();
    }

    boolean getRdoSubsidy_No() {
        return this.rdoSubsidy_No;
    }

    void setTxtPurposeDesc(String txtPurposeDesc) {
        this.txtPurposeDesc = txtPurposeDesc;
        setChanged();
    }

    String getTxtPurposeDesc() {
        return this.txtPurposeDesc;
    }

    void setTxtGroupDesc(String txtGroupDesc) {
        this.txtGroupDesc = txtGroupDesc;
        setChanged();
    }

    String getTxtGroupDesc() {
        return this.txtGroupDesc;
    }

    void setRdoInterest_Simple(boolean rdoInterest_Simple) {
        this.rdoInterest_Simple = rdoInterest_Simple;
        setChanged();
    }

    boolean getRdoInterest_Simple() {
        return this.rdoInterest_Simple;
    }

    void setRdoInterest_Compound(boolean rdoInterest_Compound) {
        this.rdoInterest_Compound = rdoInterest_Compound;
        setChanged();
    }

    boolean getRdoInterest_Compound() {
        return this.rdoInterest_Compound;
    }

    void setTxtContactPerson(String txtContactPerson) {
        this.txtContactPerson = txtContactPerson;
        setChanged();
    }

    String getTxtContactPerson() {
        return this.txtContactPerson;
    }

    void setTxtContactPhone(String txtContactPhone) {
        this.txtContactPhone = txtContactPhone;
        setChanged();
    }

    String getTxtContactPhone() {
        return this.txtContactPhone;
    }

    void setTxtRemarks(String txtRemarks) {
        this.txtRemarks = txtRemarks;
        setChanged();
    }

    String getTxtRemarks() {
        return this.txtRemarks;
    }

    void setLblProductID_FD_Disp(String lblProductID_FD_Disp) {
        this.lblProductID_FD_Disp = lblProductID_FD_Disp;
        setChanged();
    }

    String getLblProductID_FD_Disp() {
        return this.lblProductID_FD_Disp;
    }

    void setLblProdID_IDetail_Disp(String lblProdID_IDetail_Disp) {
        this.lblProdID_IDetail_Disp = lblProdID_IDetail_Disp;
        setChanged();
    }

    String getLblProdID_IDetail_Disp() {
        return this.lblProdID_IDetail_Disp;
    }

    /**
     * will return the current status of the process
     *
     * @return the current status of the process
     */
    public String getLblStatus() {
        return lblStatus;
    }

    /**
     * will set the current status of the process
     *
     * @param lblStatus is the current status of the process
     */
    public void setLblStatus(String lblStatus) {
        this.lblStatus = lblStatus;
        setChanged();
    }

    /**
     * will reset the current status to Cancel mode
     */
    public void resetStatus() {
        this.setLblStatus(ClientConstants.ACTION_STATUS[0]);
    }

    /**
     * To set the status based on ActionType, either New, Edit, etc.,
     */
    public void setStatus() {
        this.setLblStatus(ClientConstants.ACTION_STATUS[this.getActionType()]);
        //        agriSubLimitOB.setLblStatus(ClientConstants.ACTION_STATUS[this.getActionType()]);

        ttNotifyObservers();
    }

    /**
     * To update the Status based on result performed by doAction() method
     */
    public void setResultStatus() {
        this.setLblStatus(ClientConstants.RESULT_STATUS[this.getResult()]);
        ttNotifyObservers();
    }

    /**
     * It will call the notifyObservers()
     */
    public void ttNotifyObservers() {
        notifyObservers();
    }

    /**
     * This method will set the Borrower Number in all the TermLoanxxxxOB
     *
     * @param borrowerNo is the Borrower Number
     */
    public void setBorrowerNo(String borrowerNo) {
//        termLoanCompanyOB.setBorrowerNo(borrowerNo);
        //        termLoanAuthorizedSignatoryOB.setBorrowerNo(borrowerNo);
        //        termLoanAuthorizedSignatoryInstructionOB.setBorrowerNo(borrowerNo);
        //        termLoanPoAOB.setBorrowerNo(borrowerNo);
        termLoanSecurityOB.setBorrowerNo(borrowerNo);
        termLoanInterestOB.setBorrowerNo(borrowerNo);
        termLoanRepaymentOB.setBorrowerNo(borrowerNo);
//        termLoanGuarantorOB.setBorrowerNo(borrowerNo);
        termLoanDocumentDetailsOB.setBorrowerNo(borrowerNo);
        termLoanInterestOB.setBorrowerNo(borrowerNo);
        this.borrowerNo = borrowerNo;
        setChanged();
    }

    /**
     * returns the borrower number
     *
     * @return the borrower number
     */
    public String getBorrowerNo() {
        return this.borrowerNo;
    }

    public void setLblAccHead_2(String lblAccHead_2) {
        this.lblAccHead_2 = lblAccHead_2;
        setChanged();
    }

    public String getLblAccHead_2() {
        return this.lblAccHead_2;
    }

    public void setLblAccountHead_FD_Disp(String lblAccountHead_FD_Disp) {
        this.lblAccountHead_FD_Disp = lblAccountHead_FD_Disp;
        setChanged();
    }

    public String getLblAccountHead_FD_Disp() {
        return this.lblAccountHead_FD_Disp;
    }

    public void setLblAccHead_Idetail_2(String lblAccHead_Idetail_2) {
        this.lblAccHead_Idetail_2 = lblAccHead_Idetail_2;
        setChanged();
    }

    public String getLblAccHead_Idetail_2() {
        return this.lblAccHead_Idetail_2;
    }

    public void setLblAccNo_Idetail_2(String lblAccNo_Idetail_2) {
        this.lblAccNo_Idetail_2 = lblAccNo_Idetail_2;
        setChanged();
    }

    public String getLblAccNo_Idetail_2() {
        return this.lblAccNo_Idetail_2;
    }

    public void setLblDrawingPower_2(String lblDrawingPower_2) {
        this.lblDrawingPower_2 = lblDrawingPower_2;
        setChanged();
    }

    public String getLblDrawingPower_2() {
        return this.lblDrawingPower_2;
    }

    private void setMinLimitValue(Double minLimitValue) {
        this.minLimitValue = minLimitValue;
        setChanged();
    }

    public Double getMinLimitValue() {
        return this.minLimitValue;
    }

    private void setMaxLimitValue(Double maxLimitValue) {
        this.maxLimitValue = maxLimitValue;
        setChanged();
    }

    public Double getMaxLimitValue() {
        return this.maxLimitValue;
    }

    private void setMinLoanPeriod(Double minLoanPeriod) {
        this.minLoanPeriod = minLoanPeriod;
        setChanged();
    }

    public Double getMinLoanPeriod() {
        return this.minLoanPeriod;
    }

    private void setMaxLoanPeriod(Double maxLoanPeriod) {
        this.maxLoanPeriod = maxLoanPeriod;
        setChanged();
    }

    public Double getMaxLoanPeriod() {
        return this.maxLoanPeriod;
    }

    private void setMinDecLoanPeriod(String minDecLoanPeriod) {
        this.minDecLoanPeriod = minDecLoanPeriod;
        setChanged();
    }

    public String getMinDecLoanPeriod() {
        return this.minDecLoanPeriod;
    }

    private void setMaxDecLoanPeriod(String maxDecLoanPeriod) {
        this.maxDecLoanPeriod = maxDecLoanPeriod;
        setChanged();
    }

    public String getMaxDecLoanPeriod() {
        return this.maxDecLoanPeriod;
    }

    public void setTxtSanctionSlNo(String txtSanctionSlNo) {
        this.txtSanctionSlNo = txtSanctionSlNo;
        setChanged();
    }

    public String getTxtSanctionSlNo() {
        return this.txtSanctionSlNo;
    }

    public void setStrRealSanctionNo(String strRealSanctionNo) {
        this.strRealSanctionNo = strRealSanctionNo;
        setChanged();
    }

    public String getStrRealSanctionNo() {
        return this.strRealSanctionNo;
    }

    public void setStrACNumber(String strACNumber) {
        termLoanSecurityOB.setStrACNumber(strACNumber);
        termLoanRepaymentOB.setStrACNumber(strACNumber);
        termLoanDocumentDetailsOB.setStrACNumber(strACNumber);
        termLoanClassificationOB.setStrACNumber(strACNumber);
//        termLoanOtherDetailsOB.setStrACNumber(strACNumber);
        termLoanInterestOB.setStrACNumber(strACNumber);
//        termLoanAdditionalSanctionOB.setStrACNumber(strACNumber);
        //        agriSubSidyOB.setStrAcctNo(strACNumber);//dontdelete
        //        agriSubLimitOB.setSubLimitStrNo(strACNumber);
        //        settlementOB.setStrAccNumber(strACNumber);
        //        actTransOB.setAcctNum(strACNumber);//dontdelete
        this.strACNumber = strACNumber;
        setChanged();
    }

    public String getStrACNumber() {
        return this.strACNumber;
    }

    void setTblSanctionFacility(EnhancedTableModel tblSanctionFacility) {
        this.tblSanctionFacility = tblSanctionFacility;
        setChanged();
    }

    EnhancedTableModel getTblSanctionFacility() {
        return this.tblSanctionFacility;
    }

    void setTblSanctionMain(EnhancedTableModel tblSanctionMain) {
        this.tblSanctionMain = tblSanctionMain;
        setChanged();
    }

    EnhancedTableModel getTblSanctionMain() {
        return this.tblSanctionMain;
    }

    void setTblFacilityTabSanction(EnhancedTableModel tblSanctionFacility) {
        this.tblSanctionFacility = tblSanctionFacility;
        setChanged();
    }

    EnhancedTableModel getTblFacilityTabSanction() {
        return this.tblSanctionFacility;
    }

    void setTblFacilityTabMainSanction(EnhancedTableModel tblSanctionMain) {
        this.tblSanctionMain = tblSanctionMain;
        setChanged();
    }

    EnhancedTableModel getTblFacilityTabMainSanction() {
        return this.tblSanctionMain;
    }

    /**
     * This method will be called at the time of Facility Tab save button
     * pressed. This will retrieve the corresponding borrower number based on
     * custId, acctStatus, constitution, category, references and populate it in
     * Borrower Tab.
     */
    public void setBorrowerNumber() {
        try {
            if (termLoanBorrowerOB.getLblBorrowerNo_2().length() <= 0) {
                HashMap transactionMap = new HashMap();
                HashMap retrieve = new HashMap();
                String mapName = "getBorrowerNumber";
                if (loanType.equals("LTD")) {
                    transactionMap.put("LOAN_NO", getLoanACNo());
                    mapName = "getBorrowerNumberForLTD";
                } else {
                    transactionMap.put("custId", termLoanBorrowerOB.getTxtCustID());
                    transactionMap.put("constitution", termLoanBorrowerOB.getCbmConstitution().getKeyForSelected());
                    transactionMap.put("category", termLoanBorrowerOB.getCbmCategory().getKeyForSelected());
                }
                List resultList = (List) ClientUtil.executeQuery(mapName, transactionMap);
                if (resultList.size() > 0) {
                    // If atleast one Record exist
                    retrieve = (HashMap) resultList.get(0);
                    setBorrowerNo(CommonUtil.convertObjToStr(retrieve.get("BORROW_NO")));
                    termLoanBorrowerOB.setLblBorrowerNo_2(CommonUtil.convertObjToStr(retrieve.get("BORROW_NO")));
                }
                retrieve = null;
                transactionMap = null;
                resultList = null;
            }
        } catch (Exception e) {
            log.info("Exception in setBorrowerNumber(): " + e);
            parseException.logException(e, true);
        }
    }

    /**
     * To update the Product ID for the tables which are all having Account
     * Number as a key
     *
     * @param sanctionNo is the position of the record selected in the sanction
     * details table
     * @param slno is the position of the record selected in the sanction
     * facility details table
     */
    public void updateProd_ID(int sanctionNo, int slno) {
        try {
            String strsanctionNo = CommonUtil.convertObjToStr(((ArrayList) tblSanctionMain.getDataArrayList().get(sanctionNo)).get(1));

            slno = CommonUtil.convertObjToInt(((ArrayList) tblSanctionFacility.getDataArrayList().get(slno)).get(0));

            HashMap transactionMap = new HashMap();
            transactionMap.put("borrowNo", getBorrowerNo());
            transactionMap.put("acctNum", getStrACNumber());
            transactionMap.put("sanctionNo", strsanctionNo);
            transactionMap.put("slNo", String.valueOf(slno));
            transactionMap.put("facilityType", getCbmTypeOfFacility().getKeyForSelected());
            transactionMap.put("productId", getCbmProductId().getKeyForSelected());
            transactionMap.put("prodId", getCbmProductId().getKeyForSelected());

            ClientUtil.execute("updateProdID_TermLoanSanctionFacilityTO", transactionMap);
            ClientUtil.execute("updateProdID_TermLoanFacilityTO", transactionMap);
            //            ClientUtil.execute("updateProdID_TermLoanSecurityTO", transactionMap);
            ClientUtil.execute("updateProdID_TermLoanRepaymentTO", transactionMap);
//            ClientUtil.execute("updateProdID_TermLoanGuarantorTO", transactionMap);
            ClientUtil.execute("updateProdID_TermLoanInterestTO", transactionMap);
            ClientUtil.execute("updateProdID_TermLoanClassificationTO", transactionMap);

            transactionMap = null;
        } catch (Exception e) {
            log.info("Exception in updateProd_ID:  " + e);
            parseException.logException(e, true);
        }
    }

    /**
     * To display Account Head in Facility Details and its following Tabs
     */
    public void setFacilityAcctHead() {
        try {
            if (CommonUtil.convertObjToStr(getLoadingProdId()).length() > 0) {
                HashMap transactionMap = new HashMap();
                HashMap retrieve;
                transactionMap.put("prodId", getLoadingProdId());
                transactionMap.put("PROD_ID", getLoadingProdId());

                List resultList1 = ClientUtil.executeQuery("getProdIntDetails", transactionMap);
                List resultList2 = ClientUtil.executeQuery("TermLoan.getProdHead", transactionMap);
                List resultList3 = ClientUtil.executeQuery("TermLoan.getProduct_Details", transactionMap);

                if (resultList1.size() > 0) {
                    // If Product Account Head exist in Database
                    retrieve = (HashMap) resultList1.get(0);
                    if (retrieve.containsKey(PLR_RATE_APPL)) {
                        if (retrieve.get(PLR_RATE_APPL).equals(YES)) {
                            setRdoNatureInterest_PLR(true);
                            setRdoNatureInterest_NonPLR(false);
                        } else if (retrieve.get(PLR_RATE_APPL).equals(NO)) {
                            setRdoNatureInterest_NonPLR(true);
                            setRdoNatureInterest_PLR(false);
                        }
                    }
                }
                retrieve = null;

                if (resultList2.size() > 0) {
                    // If Product Account Head exist in Database
                    retrieve = (HashMap) resultList2.get(0);
                    setLblAccHead_2(CommonUtil.convertObjToStr(retrieve.get("AC_HEAD")));
                    setLblAccountHead_FD_Disp(CommonUtil.convertObjToStr(retrieve.get("AC_HEAD")));
                    setEligibleMargin(CommonUtil.convertObjToDouble(retrieve.get("DEP_ELIGIBLE_LOAN_AMT")).doubleValue());
                    setSanctionAmtRoundOff(CommonUtil.convertObjToStr(retrieve.get("DEPOSIT_ROUNDOFF")));
                } else {
                    setEligibleMargin(0.0);
                    setSanctionAmtRoundOff("");
                }
                retrieve = null;
                if (resultList3.size() > 0) {
                    retrieve = (HashMap) resultList3.get(0);
                    if (retrieve.containsKey(SUBSIDY)) {
                        if (retrieve.get(SUBSIDY).equals(YES)) {
                            setRdoSubsidy_Yes(true);
                            setRdoSubsidy_No(false);
                        } else if (retrieve.get(SUBSIDY).equals(NO)) {
                            setRdoSubsidy_No(true);
                            setRdoSubsidy_Yes(false);
                        }
                    }
                    setMinLimitValue(CommonUtil.convertObjToDouble(retrieve.get("MIN_AMT_LOAN")));
                    setMaxLimitValue(CommonUtil.convertObjToDouble(retrieve.get("MAX_AMT_LOAN")));
                    setMaxLoanPeriod(CommonUtil.convertObjToDouble(retrieve.get("MAX_PERIOD")));
                    setMinLoanPeriod(CommonUtil.convertObjToDouble(retrieve.get("MIN_PERIOD")));
                    termLoanRepaymentOB.setInterest_Rate(CommonUtil.convertObjToDouble(retrieve.get("APPL_INTEREST")).doubleValue());
                }
                setAllTabProdID(CommonUtil.convertObjToStr(getCbmProductId().getKeyForSelected()));
                setAllTabAccHead();
                retrieve = null;
                transactionMap = null;
                resultList1 = null;
                resultList2 = null;
                resultList3 = null;
            }
        } catch (Exception e) {
            log.info("Exception caught in setFacilityAcctHead: " + e);
            parseException.logException(e, true);
        }
    }

    public void setFacilityContactDetails(String CustID) {
        try {
            HashMap retrieve;
            List resultList = termLoanBorrowerOB.getCustOpenDate(CustID);
            if (resultList.size() > 0) {
                // If atleast one Record exist
                retrieve = (HashMap) resultList.get(0);
                if (retrieve.get("CUST_TYPE").equals("CORPORATE")) {
                    // If it is the Corporate Customer
                    setTxtContactPerson(CommonUtil.convertObjToStr(retrieve.get("COMP_NAME")));
                } else {
                    setTxtContactPerson(CommonUtil.convertObjToStr(retrieve.get("CUSTOMER NAME")));
                }
            }
            StringBuffer stbPhone = new StringBuffer("");
            int phoneCount = 0;
            List phoneList = termLoanBorrowerOB.getCustPhone(CustID);
            if (phoneList.size() > 0) {
                // To retrieve the Contact Phone Numbers of the Customer
                for (int i = phoneList.size() - 1, j = 0; i >= 0; --i, ++j) {
                    retrieve = (HashMap) phoneList.get(j);
                    if (retrieve.get("PHONE_TYPE_ID") != null && (retrieve.get("PHONE_TYPE_ID")).equals("LAND LINE")) {
                        // If the Phone Type ID is Land Line
                        if (phoneCount != 0) {
                            // To avoid appending comma at the end
                            stbPhone.append(", ");
                        }
                        stbPhone.append(CommonUtil.convertObjToStr(retrieve.get("AREA_CODE")));
                        stbPhone.append(CommonUtil.convertObjToStr(retrieve.get("PHONE_NUMBER")));
                        phoneCount++;
                    }
                    retrieve = null;
                }
            }
            setTxtContactPhone(stbPhone.toString());
            stbPhone = null;
            phoneList = null;
            resultList = null;
        } catch (Exception e) {
            log.info("Exception caught in setFacilityContactDetails: " + e);
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
            // //System.out.println("getMaxLimitValuegetMaxLimitValue"+getMaxLimitValue);
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
     * To check whether the Period entered in UI is matched with the minimum and
     * maximum period in Loan Product
     *
     * @return true if the entered period in UI fall within minimum and maximum
     * period of Loan Product else false
     * @param moratorium_Period is the no. of months for moratorium
     * @param noInstall is the String object having number of installment value
     */
    public boolean checkFacilityPeriod(String noInstall, String moratorium_Period) {
        boolean returnValue = false;
        try {
            Double frequency = CommonUtil.convertObjToDouble(getCbmRepayFreq().getKeyForSelected());
            Double no_Install = CommonUtil.convertObjToDouble(noInstall);
            int period = no_Install.intValue() * frequency.intValue();
            period = period + (CommonUtil.convertObjToInt(moratorium_Period) * 30);
            if ((getMaxLoanPeriod().intValue() >= period) && (getMinLoanPeriod().intValue() <= period)) {
                returnValue = true;
            }
            frequency = null;
            no_Install = null;
        } catch (Exception e) {
            log.info("Exception caught in checkFacilityPeriod:  " + e);
            parseException.logException(e, true);
        }
        return returnValue;
    }

    /**
     * To check whether the Period entered in UI is matched with the minimum and
     * maximum period in Loan Product
     *
     * @return true if the entered period in UI fall within minimum and maximum
     * period of Loan Product else false
     * @param strFromDate is the facility from date
     * @param strToDate is the facility to date
     * @param noInstall is the String object having number of installment value
     */
    public boolean checkFacilityPeriod(java.util.Date datFromDate, java.util.Date datToDate) {
        boolean returnValue = false;
        try {
            double period = DateUtil.dateDiff(datFromDate, datToDate);
            if ((getMaxLoanPeriod().doubleValue() >= period) && (getMinLoanPeriod().doubleValue() <= period)) {
                returnValue = true;
            }
        } catch (Exception e) {
            log.info("Exception caught in checkFacilityPeriod:  " + e);
            parseException.logException(e, true);
        }
        return returnValue;
    }

    public boolean isDepositDaily(HashMap hash) {
        if (hash.get("BEHAVES_LIKE").equals("DAILY")) {
            double depPeriod = CommonUtil.convertObjToDouble(hash.get("PREMATURE_WITHDRAWAL")).doubleValue();
            if (DateUtil.dateDiff((Date) hash.get("DEPOSIT_DT"), curDate) >= depPeriod) {
                return false;
            } else {
                return true;
            }
        } else {
            return false;
        }
    }

    public boolean setDetailsForLTD(HashMap hash) {
        try {
            HashMap resultMap = new HashMap();
            HashMap resultCustMap = new HashMap();
            depositCustDetMap = new HashMap();
            depositCustDetMap.putAll(hash);
            //System.out.println("### setDetailsForLTD - depositCustDetMap 1 : " + depositCustDetMap);
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
                //System.out.println("ternLoanBorrower###" + termLoanBorrowerOB.getTxtCustID());
                if (resultCustMap != null && CommonUtil.convertObjToStr(resultCustMap.get("CUST_ID")).equals(termLoanBorrowerOB.getTxtCustID())) {
                    return true;
                } else if (resultCustMap != null && resultCustMap.containsKey("LIEN_AC_NO")) {
                    ClientUtil.showMessageWindow("Deposit Holder" + resultMap.get("DEPOSIT_NO") + "\n"
                            + "Already Disbursed This LoanHolder" + resultCustMap.get("LIEN_AC_NO"));
                    return false;
                }
                //                else
                //                    return true;
                depositCustDetMap.putAll(resultMap);
                //System.out.println("### setDetailsForLTD - depositCustDetMap 2 : " + depositCustDetMap);
                //                setTdtTDate(CommonUtil.convertObjToStr(resultMap.get("MATURITY_DT")));
                setTdtTDate(DateUtil.getStringDate((java.util.Date) resultMap.get("MATURITY_DT")));
                tdtRepaymentDate = DateUtil.getStringDate((java.util.Date) resultMap.get("MATURITY_DT"));
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

    public void populatePeriodDifference(String strNoInstall, String strRepayFreq, String strMoratorium) {
        try {
            int period = 0;
            int yrs = 0;
            int months = 0;
            int days = 0;
            if (strRepayFreq.length() > 0) {
                // If the Repayment Frequency is Lump Sum
                if (strRepayFreq.equals("1")) {
                    days++;
                }
                // If the Repayment Frequency is Monthly
                if (strRepayFreq.equals("30")) {
                    months += CommonUtil.convertObjToInt(strNoInstall);
                }
                // If the Repayment Frequency is Quaterly
                if (strRepayFreq.equals("90")) {
                    months += (CommonUtil.convertObjToInt(strNoInstall) * 3);
                }
                // If the Repayment Frequency is Half yearly
                if (strRepayFreq.equals("180")) {
                    months += (CommonUtil.convertObjToInt(strNoInstall) * 6);
                }
                // If the Repayment Frequency is Yearly
                if (strRepayFreq.equals("365")) {
                    yrs += CommonUtil.convertObjToInt(strNoInstall);
                }
            }
            if (strMoratorium.length() > 0) {
                months += CommonUtil.convertObjToInt(strMoratorium);
            }
            while (months >= 12) {
                months -= 12;
                yrs++;
            }
            setTxtPeriodDifference_Years(String.valueOf(yrs));
            setTxtPeriodDifference_Months(String.valueOf(months));
            setTxtPeriodDifference_Days(String.valueOf(days));
        } catch (Exception e) {
            log.info("Exception caught in populatePeriodDifference:  " + e);
            parseException.logException(e, true);
        }
    }

    public void populatePeriodDifference(String strFromDate, String strToDate) {
        try {
            double difference = DateUtil.dateDiff(DateUtil.getDateMMDDYYYY(strFromDate), DateUtil.getDateMMDDYYYY(strToDate));
            int period = (int) difference;
            int yrs = period / 365;
            int months = (period - (yrs * 365)) / 30;
            int days = (period - ((yrs * 365) + (months * 30)));

            setTxtPeriodDifference_Years(String.valueOf(yrs));
            setTxtPeriodDifference_Months(String.valueOf(months));
            setTxtPeriodDifference_Days(String.valueOf(days));
        } catch (Exception e) {
            log.info("Exception caught in populatePeriodDifference:  " + e);
            parseException.logException(e, true);
        }
    }

    /**
     * To display the minimum and maximum Loan Period in Year wise, Monthly
     * wise, Day wise
     */
    public void decoratePeriod() {
        try {
            final int minPeriod = getMinLoanPeriod().intValue();
            final int maxPeriod = getMaxLoanPeriod().intValue();
            if (minPeriod % 360 == 0 || minPeriod % 365 == 0) {
                setMinDecLoanPeriod((minPeriod / 360) + " Year(s)");
            } else if (minPeriod % 30 == 0) {
                setMinDecLoanPeriod((minPeriod / 30) + " Month(s)");
            } else {
                setMinDecLoanPeriod(minPeriod + " Day(s)");
            }
            if (maxPeriod % 360 == 0 || maxPeriod % 365 == 0) {
                setMaxDecLoanPeriod((maxPeriod / 360) + " Year(s)");
            } else if (maxPeriod % 30 == 0) {
                setMaxDecLoanPeriod((maxPeriod / 30) + " Month(s)");
            } else {
                setMaxDecLoanPeriod(maxPeriod + " Day(s)");
            }
        } catch (Exception e) {
            log.info("Exception caught in decoratePeriod:  " + e);
            parseException.logException(e, true);
        }
    }

    private void setAllTabProdID(String prodID) {
        setLblProductID_FD_Disp(prodID);
        termLoanSecurityOB.setLblProdId_Disp(prodID);
        termLoanRepaymentOB.setLblProdID_RS_Disp(prodID);
//        termLoanGuarantorOB.setLblProdID_GD_Disp(prodID);
        termLoanDocumentDetailsOB.setLblProdID_Disp_DocumentDetails(prodID);
        termLoanInterestOB.setLblProdID_IM_Disp(prodID);
        termLoanClassificationOB.setLblProdID_CD_Disp(prodID);
//        termLoanOtherDetailsOB.setLblProdID_Disp_ODetails(prodID);
    }

    private void setAllTabAccHead() {
        termLoanSecurityOB.setLblAccHeadSec_2(getLblAccHead_2());
        termLoanRepaymentOB.setLblAccHead_RS_2(getLblAccHead_2());
//        termLoanGuarantorOB.setLblAccHead_GD_2(getLblAccHead_2());
        termLoanDocumentDetailsOB.setLblAcctHead_Disp_DocumentDetails(getLblAccHead_2());
        termLoanInterestOB.setLblAccHead_IM_2(getLblAccHead_2());
        termLoanClassificationOB.setLblAccHead_CD_2(getLblAccHead_2());
//        termLoanOtherDetailsOB.setLblAcctHead_Disp_ODetails(getLblAccHead_2());
    }

    private void updateProdID_AccHead() {
        termLoanSecurityOB.setLblAccHeadSec_2(getLblAccountHead_FD_Disp());
        termLoanRepaymentOB.setLblAccHead_RS_2(getLblAccountHead_FD_Disp());
//        termLoanGuarantorOB.setLblAccHead_GD_2(getLblAccountHead_FD_Disp());
        termLoanDocumentDetailsOB.setLblAcctHead_Disp_DocumentDetails(getLblAccountHead_FD_Disp());
        setLblAccHead_Idetail_2(getLblAccountHead_FD_Disp());
        termLoanInterestOB.setLblAccHead_IM_2(getLblAccountHead_FD_Disp());
        termLoanClassificationOB.setLblAccHead_CD_2(getLblAccountHead_FD_Disp());
//        termLoanOtherDetailsOB.setLblAcctHead_Disp_ODetails(getLblAccountHead_FD_Disp());
        termLoanSecurityOB.setLblProdId_Disp(getLblProductID_FD_Disp());
        termLoanRepaymentOB.setLblProdID_RS_Disp(getLblProductID_FD_Disp());
//        termLoanGuarantorOB.setLblProdID_GD_Disp(getLblProductID_FD_Disp());
        termLoanDocumentDetailsOB.setLblProdID_Disp_DocumentDetails(getLblProductID_FD_Disp());
        setLblProdID_IDetail_Disp(getLblProductID_FD_Disp());
        termLoanInterestOB.setLblProdID_IM_Disp(getLblProductID_FD_Disp());
        termLoanClassificationOB.setLblProdID_CD_Disp(getLblProductID_FD_Disp());
//        termLoanOtherDetailsOB.setLblProdID_Disp_ODetails(getLblProductID_FD_Disp());
        setChanged();
    }

    public void createTableUtilSanctionFacility() {
        tableUtilSanction_Facility = null;
        tableUtilSanction_Facility = new TableUtil();
        tableUtilSanction_Facility.setAttributeKey(SLNO);
    }

    private void periodMultipleMessage() {
        StringBuffer strPeriod = new StringBuffer("The Loan Period is in multiples of ");
        try {
            HashMap transactionMap = new HashMap();
            HashMap retrieve;
            transactionMap.put("prodId", getCbmProductId().getKeyForSelected());
            List resultList = ClientUtil.executeQuery("TermLoan.getLoanPeriodMultiples", transactionMap);
            if (resultList.size() > 0) {
                // If Product Account Head exist in Database
                retrieve = (HashMap) resultList.get(0);

                if (!CommonUtil.convertObjToStr(retrieve.get("LOAN_PERIODS_MULTIPLES")).equals(CommonUtil.convertObjToStr(getCbmRepayFreq().getKeyForSelected()))) {
                    strPeriod.append(cbmRepayFreq.getDataForKey(CommonUtil.convertObjToStr(retrieve.get("LOAN_PERIODS_MULTIPLES"))));
                    int option = -1;
                    String[] options = {objTermLoanRB.getString("cDialogOk")};
                    option = COptionPane.showOptionDialog(null, strPeriod.toString(), CommonConstants.WARNINGTITLE,
                            COptionPane.OK_OPTION, COptionPane.WARNING_MESSAGE, null, options, options[0]);
                    strPeriod = null;
                }
            }
            retrieve = null;
            transactionMap = null;
            resultList = null;
        } catch (Exception e) {
            log.info("Exception caught in periodMultipleMessage: " + e);
            parseException.logException(e, true);
        }
    }

    public int addSanctionFacilityTab(int row, boolean update, boolean updateMain, int mainSlNo, int facilityTabSlNo) {
        int option = -1;
        try {

            //            if (!loanType.equals("LTD"))
            //                periodMultipleMessage();
            sanctionFacilityTabRow = new ArrayList();
            sanctionFacilityRecord = new HashMap();
            HashMap result = new HashMap();
            HashMap sanctionMainRec = new HashMap();
            ArrayList data = tblSanctionFacility.getDataArrayList();
            tblSanctionFacility.setDataArrayList(data, sanctionFacilityTitle);
            final int dataSize = data.size();
            String strSanctionSlNo = "";
            Set keySet = sanctionFacilityAll.keySet();
            Object[] objKeySet = (Object[]) keySet.toArray();
            insertSanctionFacility(dataSize + 1);
            // If the Mode is not update then insert the record
            if (!update) {
                // If the table is not in Edit Mode
                if (updateMain == true) {
                    if (objKeySet.length > 0) {
                        strSanctionSlNo = CommonUtil.convertObjToStr(((HashMap) sanctionFacilityAll.get(objKeySet[0])).get(SANCTION_NO));
                        sanctionFacilityRecord.put(SANCTION_NO, strSanctionSlNo);
                    }
                }
                result = tableUtilSanction_Facility.insertTableValues(sanctionFacilityTabRow, sanctionFacilityRecord);

                sanctionFacilityAllTabRecords = (ArrayList) result.get(TABLE_VALUES);
                sanctionFacilityAll = (LinkedHashMap) result.get(ALL_VALUES);
                option = CommonUtil.convertObjToInt(result.get(OPTION));
                // To save the changes in Sanction Main at the time of Sanction main edit
                if (updateMain == true) {
                    sanctionMainRec = (HashMap) sanctionMainAll.get(((ArrayList) sanctionMainAllTabRecords.get(mainSlNo)).get(0));
                    sanctionMainRec.put(SANCTION_FACILITY_ALL, sanctionFacilityAll);
                    sanctionMainRec.put(SANCTION_FACILITY_TABLE, sanctionFacilityAllTabRecords);
                    sanctionMainAll.put(((ArrayList) sanctionMainAllTabRecords.get(mainSlNo)).get(0), sanctionMainRec);
                }
                tblSanctionFacility.setDataArrayList(sanctionFacilityAllTabRecords, sanctionFacilityTitle);
                option = dataSize;
            } else {
                option = updateSanctionFacility(row, updateMain, mainSlNo);
                option = row;
            }
            setChanged();
            keySet = null;
            objKeySet = null;
            data = null;
            sanctionMainRec = null;
            result = null;
        } catch (Exception e) {
            log.info("Exception caught in addSanctionFacilityTab: " + e);
            parseException.logException(e, true);
        }
        return option;
    }

    private void insertSanctionFacility(int slno) {
        try {
            sanctionFacilityTabRow.add(String.valueOf(slno));
            sanctionFacilityTabRow.add(getCboTypeOfFacility());
            sanctionFacilityTabRow.add(CommonUtil.convertObjToStr(txtLimit_SD));
            sanctionFacilityTabRow.add(tdtFDate);
            sanctionFacilityTabRow.add(tdtTDate);

            sanctionFacilityRecord.put(SLNO, String.valueOf(slno));
            //            sanctionFacilityRecord.put(SANCTION_NO, getTxtSanctionNo());
            sanctionFacilityRecord.put(FACILITY_TYPE, CommonUtil.convertObjToStr(cbmTypeOfFacility.getKeyForSelected()));
            sanctionFacilityRecord.put(LIMIT, CommonUtil.convertObjToStr(txtLimit_SD));
            sanctionFacilityRecord.put(INITIAL_MONEY_DEPOSIT, CommonUtil.convertObjToStr(txtLimit_SDMoneyDeposit));
            sanctionFacilityRecord.put(FROM, tdtFDate);
            sanctionFacilityRecord.put(TO, tdtTDate);
            if (getChkMoratorium_Given()) {
                sanctionFacilityRecord.put(MORATORIUM_GIVEN, "Y");
            } else {
                sanctionFacilityRecord.put(MORATORIUM_GIVEN, "N");
            }
            sanctionFacilityRecord.put(FACILITY_REPAY_DATE, getTdtFacility_Repay_Date());
            sanctionFacilityRecord.put(MORATORIUM_PERIOD, getTxtFacility_Moratorium_Period());
            sanctionFacilityRecord.put(PROD_ID, CommonUtil.convertObjToStr(cbmProductId.getKeyForSelected()));
            sanctionFacilityRecord.put(REPAY_FREQ, CommonUtil.convertObjToStr(cbmRepayFreq.getKeyForSelected()));
            sanctionFacilityRecord.put(NO_INSTALLMENTS, CommonUtil.convertObjToStr(txtNoInstallments));
            sanctionFacilityRecord.put(SANCTION_NO, getTxtSanctionSlNo());
            sanctionFacilityRecord.put(COMMAND, "");
        } catch (Exception e) {
            log.info("Exception caught in insertSanctionFacility: " + e);
            parseException.logException(e, true);
        }
    }

    // Update the cboTypeOfFacility after calling this method in UI
    public void populateSanctionFacility(int slno) {
        try {
            HashMap eachRecs = new HashMap();
            Set keySet = sanctionFacilityAll.keySet();
            Object[] objKeySet = (Object[]) keySet.toArray();
            ArrayList sanctionFacilityTableValue = (ArrayList) tblSanctionFacility.getDataArrayList().get(slno);
            //System.out.println("populate sanction facility #####" + sanctionFacilityTableValue);
            // To populate the corresponding Sanction Facility Details
            for (int i = sanctionFacilityAll.size() - 1, j = 0; i >= 0; --i, ++j) {
                // If the record matches with the key then populate it in UI
                if ((CommonUtil.convertObjToStr(((HashMap) sanctionFacilityAll.get(objKeySet[j])).get(SLNO))).equals(CommonUtil.convertObjToStr(sanctionFacilityTableValue.get(0)))) {
                    // To populate the Corresponding record from CTable
                    eachRecs = (HashMap) sanctionFacilityAll.get(objKeySet[j]);
                    setLblProductID_FD_Disp(CommonUtil.convertObjToStr(eachRecs.get(PROD_ID)));
                    populateRestofProdId_AccHead();
                    getCbmTypeOfFacility().setKeyForSelected(CommonUtil.convertObjToStr(eachRecs.get(FACILITY_TYPE)));
                    setCboTypeOfFacility(CommonUtil.convertObjToStr(getCbmTypeOfFacility().getDataForKey(CommonUtil.convertObjToStr(eachRecs.get(FACILITY_TYPE)))));
                    setTxtLimit_SD(CommonUtil.convertObjToStr(eachRecs.get(LIMIT)));
                    setTxtLimit_SDMoneyDeposit(CommonUtil.convertObjToStr(eachRecs.get(INITIAL_MONEY_DEPOSIT)));
                    agriSubLimitOB.setMainLimit(CommonUtil.convertObjToStr(eachRecs.get(LIMIT)));
                    termLoanInterestOB.setLblLimitAmt_2(getTxtLimit_SD());
                    setTdtFDate(CommonUtil.convertObjToStr(eachRecs.get(FROM)));
                    setTdtTDate(CommonUtil.convertObjToStr(eachRecs.get(TO)));
                    if (CommonUtil.convertObjToStr(eachRecs.get(MORATORIUM_GIVEN)).equals("Y")) {
                        setChkMoratorium_Given(true);
                    } else if (CommonUtil.convertObjToStr(eachRecs.get(MORATORIUM_GIVEN)).equals("N")) {
                        setChkMoratorium_Given(false);
                    }
                    setTdtFacility_Repay_Date(CommonUtil.convertObjToStr(eachRecs.get(FACILITY_REPAY_DATE)));
                    setTxtFacility_Moratorium_Period(CommonUtil.convertObjToStr(eachRecs.get(MORATORIUM_PERIOD)));
                    termLoanInterestOB.setLblExpiryDate_2(getTdtTDate());
                    setTxtNoInstallments(CommonUtil.convertObjToStr(eachRecs.get(NO_INSTALLMENTS)));
                    ttNotifyObservers();
                    cbmRepayFreq.setKeyForSelected(CommonUtil.convertObjToStr(eachRecs.get(REPAY_FREQ)));
                    setCboRepayFreq(CommonUtil.convertObjToStr(getCbmRepayFreq().getDataForKey(CommonUtil.convertObjToStr(eachRecs.get(REPAY_FREQ)))));
                    //                    setCboProductId(CommonUtil.convertObjToStr(getCbmProductId().getDataForKey(CommonUtil.convertObjToStr(eachRecs.get(PROD_ID)))));
                    getCbmProductId().setKeyForSelected(CommonUtil.convertObjToStr(eachRecs.get(PROD_ID)));
                    getPLR_Rate(CommonUtil.convertObjToStr(getCbmProductId().getKeyForSelected()));
                    setChanged();
                    break;
                }
            }
            eachRecs = null;
            keySet = null;
            objKeySet = null;
            sanctionFacilityTableValue = null;
        } catch (Exception e) {
            log.info("Exception caught in populateSanctionFacility: " + e);
            parseException.logException(e, true);
        }
    }

    public void getPLR_Rate(String prod_id) {
        try {
            HashMap transactionMap = new HashMap();
            HashMap retrieve = new HashMap();
            transactionMap.put("PROD_ID", prod_id);
            List resultList = ClientUtil.executeQuery("getProdIntDetails", transactionMap);
            if (resultList.size() > 0) {
                // If the result contains atleast one record
                retrieve = (HashMap) resultList.get(0);
                if (retrieve.get(PLR_RATE_APPL).equals(YES)) {
                    termLoanInterestOB.setLblPLR_Limit_2(CommonUtil.convertObjToStr(retrieve.get(PLR_RATE)));
                }
            }
            retrieve = null;
            transactionMap = null;
            resultList = null;
        } catch (Exception e) {
            log.info("Exception caught in getPLR_Rate: " + e);
            parseException.logException(e, true);
        }
    }

    public int getIncrementType() {
        int incType = 0;
        Double incVal = null;
        try {
            if (isRenewalYesNo()) {
                incVal = CommonUtil.convertObjToDouble(cbmRenewalRepayFreq.getKeyForSelected());
            } else {
                incVal = CommonUtil.convertObjToDouble(cbmRepayFreq.getKeyForSelected());
            }
            incType = incVal.intValue();
            incVal = null;
        } catch (Exception e) {
            log.info("Exception caught in getIncrementType: " + e);
            parseException.logException(e, true);
        }
        return incType;
    }

    public int getRepayScheduleIncrementType() {
        int incType = 0;
        Double incVal = null;
        try {
            if (isRenewalYesNo()) {
                incVal = CommonUtil.convertObjToDouble(cbmRenewalRepayFreq.getKeyForSelected());
            } else {
                incVal = CommonUtil.convertObjToDouble(cbmRepayFreq.getKeyForSelected());
            }
            incType = incVal.intValue();
            incVal = null;
        } catch (Exception e) {
            log.info("Exception caught in getIncrementType: " + e);
            parseException.logException(e, true);
        }
        return incType;
    }

    public int getInstallNo(String strNoInstalls, int incType) {
        int instNo = 0;
        try {
            Double insNo = CommonUtil.convertObjToDouble(strNoInstalls);
            instNo = insNo.intValue();// - 1;
            if (incType == 7) {
                instNo = instNo * 7;
            } else if (incType >= 90) {
                instNo = ((int) (incType / 30)) * instNo;
            }
            insNo = null;
        } catch (Exception e) {
            log.info("Exception caught in getInstallAmt: " + e);
            parseException.logException(e, true);
        }
        return instNo;
    }

    public void populateSanctionTabProdID(int slno) {
        try {
            if (slno != -1 && (tblSanctionFacility.getRowCount() != 0)) {
                HashMap eachRecs = new HashMap();
                Set keySet = sanctionFacilityAll.keySet();
                Object[] objKeySet = (Object[]) keySet.toArray();
                ArrayList sanctionFacilityTableValue = (ArrayList) tblSanctionFacility.getDataArrayList().get(slno);
                // To populate the corresponding Sanction Facility Details
                for (int i = sanctionFacilityAll.size() - 1, j = 0; i >= 0; --i, ++j) {
                    // If the record matches with the key then populate it in UI
                    if ((CommonUtil.convertObjToStr(((HashMap) sanctionFacilityAll.get(objKeySet[j])).get(SLNO))).equals(CommonUtil.convertObjToStr(sanctionFacilityTableValue.get(0)))) {
                        // To populate the Corresponding record from CTable
                        eachRecs = (HashMap) sanctionFacilityAll.get(objKeySet[j]);
                        //                        setCboProductId(CommonUtil.convertObjToStr(getCbmProductId().getDataForKey(CommonUtil.convertObjToStr(eachRecs.get(PROD_ID)))));
                        getCbmProductId().setKeyForSelected(CommonUtil.convertObjToStr(eachRecs.get(PROD_ID)));
                        setChanged();
                        break;
                    }
                }
                eachRecs = null;
                keySet = null;
                objKeySet = null;
                sanctionFacilityTableValue = null;
            }
        } catch (Exception e) {
            log.info("Exception caught in populateSanctionTabProdID: " + e);
            parseException.logException(e, true);
        }
    }

    private int updateSanctionFacility(int row, boolean updateMain, int mainSlNo) {
        log.info("In updateSanctionFacility...");
        HashMap result = new HashMap();
        int option = -1;
        int count = 0;
        HashMap sanctionMainRec = new HashMap();
        String strSanctionNo;
        try {
            strSanctionNo = CommonUtil.convertObjToStr(((HashMap) sanctionFacilityAll.get(((ArrayList) sanctionFacilityAllTabRecords.get(row)).get(0))).get(SANCTION_NO));
            sanctionFacilityRecord.put(SANCTION_NO, strSanctionNo);
            result = tableUtilSanction_Facility.updateTableValues(sanctionFacilityTabRow, sanctionFacilityRecord, row);
            sanctionFacilityAllTabRecords = (ArrayList) result.get(TABLE_VALUES);
            sanctionFacilityAll = (LinkedHashMap) result.get(ALL_VALUES);
            option = CommonUtil.convertObjToInt(result.get(OPTION));
            // To save the changes in Sanction Main at the time of Sanction main edit
            if (updateMain == true) {
                sanctionMainRec = (HashMap) sanctionMainAll.get(((ArrayList) sanctionMainAllTabRecords.get(mainSlNo)).get(0));
                sanctionMainRec.put(SANCTION_FACILITY_ALL, sanctionFacilityAll);
                sanctionMainRec.put(SANCTION_FACILITY_TABLE, sanctionFacilityAllTabRecords);
                sanctionMainAll.put(((ArrayList) sanctionMainAllTabRecords.get(mainSlNo)).get(0), sanctionMainRec);
            }
            tblSanctionFacility.setDataArrayList(sanctionFacilityAllTabRecords, sanctionFacilityTitle);
        } catch (Exception e) {
            log.info("Exception caught in updateSanctionFacility: " + e);
            parseException.logException(e, true);
        }
        strSanctionNo = null;
        result = null;
        sanctionMainRec = null;
        return option;
    }

    public void setProdIDAsBlank() {
        setBlankKeyValue();
        cbmProductId = new ComboBoxModel(key, value);
    }

    private void setFacilityProductID(String strFacilityType) {
        try {
            cbmProductId = null;
            HashMap transactionMap = new HashMap();
            HashMap retrieve = new HashMap();

            ArrayList keyList = new ArrayList();
            keyList.add("");

            ArrayList valList = new ArrayList();
            valList.add("");

            if (strFacilityType == null) {
                transactionMap.put("behavesLike", CommonUtil.convertObjToStr(cbmTypeOfFacility.getKeyForSelected()));
            } else {
                transactionMap.put("behavesLike", strFacilityType);
            }

            List resultList = ClientUtil.executeQuery("TermLoan.getProdID_Behaves", transactionMap);

            for (int i = resultList.size() - 1, j = 0; i >= 0; --i, ++j) {
                // If the result contains atleast one record
                retrieve = (HashMap) resultList.get(j);
                keyList.add(retrieve.get("PROD_ID"));
                valList.add(retrieve.get("PROD_DESC"));
            }

            cbmProductId = new ComboBoxModel(keyList, valList);

            transactionMap = null;
            resultList = null;
            keyList = null;
            valList = null;
        } catch (Exception e) {
            log.info("Exception caught in setFacilityProductID: " + e);
            parseException.logException(e, true);
        }
    }

    public void setFacilityProductID() {
        setFacilityProductID(null);
    }

    public int deleteSanctionFacility(int sanctionTabFacilityRow, boolean updateMain, int mainSlNo, int facilityTabSanctionNo, int facilityTabSlNo) {
        log.info("In deleteSanctionFacility...");
        int option = -1;
        try {
            HashMap result = new HashMap();
            HashMap sanctionMainRec = new HashMap();

            ArrayList data = tblSanctionFacility.getDataArrayList();

            int keysanctionTabFacilityRow = CommonUtil.convertObjToInt(((ArrayList) data.get(sanctionTabFacilityRow)).get(0));

            data = tblSanctionMain.getDataArrayList();
            String strSanctionNo = CommonUtil.convertObjToStr(((ArrayList) data.get(mainSlNo)).get(1));
            mainSlNo = CommonUtil.convertObjToInt(((ArrayList) data.get(mainSlNo)).get(0));

            data = tblSanctionMain.getDataArrayList();
            if (facilityTabSanctionNo > -1) {
                facilityTabSanctionNo = CommonUtil.convertObjToInt(((ArrayList) data.get(facilityTabSanctionNo)).get(1));
            }

            data = tblSanctionFacility.getDataArrayList();
            if (facilityTabSlNo > -1) {
                facilityTabSlNo = CommonUtil.convertObjToInt(((ArrayList) data.get(facilityTabSlNo)).get(0));
            }

            option = -1;

            if (facilityAll.containsKey(getFacilityKey(strSanctionNo, keysanctionTabFacilityRow))) {
                //                String[] options = {objTermLoanRB.getString("cDialogOk")};
                //                option = COptionPane.showOptionDialog(null, objTermLoanRB.getString("existanceFacilityWarning"), CommonConstants.WARNINGTITLE,
                //                COptionPane.OK_OPTION, COptionPane.WARNING_MESSAGE, null, options, options[0]); commented by abi a for  delete not transaction rec
                //                List lt=(List)hash;
                //                System.out.print("hash###"+lt);
                //                if(lblAcctNo_Sanction_Disp)
                HashMap hash2 = new HashMap();
                hash2.put("ACT_NUM", getLoanACNo());
                List lst = ClientUtil.executeQuery("checkTransaction", hash2);
                hash2 = (HashMap) lst.get(0);
                int count = 0;
                count = CommonUtil.convertObjToInt(hash2.get("CNT"));
                if (count != 0) {
                    ClientUtil.displayAlert("This Account already Disbursed");
                } else {
                    int val = ClientUtil.confirmationAlert("are u sure want to delete LoanAccount\n" + getLoanACNo());
                    if (val == 0) {
                        option = 0;
                        result = tableUtilSanction_Facility.deleteTableValues(sanctionTabFacilityRow);
                        sanctionFacilityAllTabRecords = (ArrayList) result.get(TABLE_VALUES);
                        Object hash = (Object) sanctionFacilityAllTabRecords.get(0);
                        System.out.print("hash###" + hash);
                    }
                }
            } else {
                result = tableUtilSanction_Facility.deleteTableValues(sanctionTabFacilityRow);
                sanctionFacilityAllTabRecords = (ArrayList) result.get(TABLE_VALUES);
                sanctionFacilityAll = (LinkedHashMap) result.get(ALL_VALUES);

                // To save the changes in Sanction Main at the time of Sanction main edit
                if (updateMain == true) {
                    sanctionMainRec = (HashMap) sanctionMainAll.get(String.valueOf(mainSlNo));
                    sanctionMainRec.put(SANCTION_FACILITY_ALL, sanctionFacilityAll);
                    sanctionMainRec.put(SANCTION_FACILITY_TABLE, sanctionFacilityAllTabRecords);
                    sanctionMainAll.put(String.valueOf(mainSlNo), sanctionMainRec);
                }
                option = 0;
            }
            tblSanctionFacility.setDataArrayList(sanctionFacilityAllTabRecords, sanctionFacilityTitle);
            sanctionMainRec = null;
            strSanctionNo = null;
        } catch (Exception e) {
            log.info("Exception caught in deleteSanctionFacility..." + e);
            parseException.logException(e, true);
        }
        return option;
    }

    public void setSanctionTableWarningMessage() {
        int option = -1;
        String[] options = {objTermLoanRB.getString("cDialogOk")};
        option = COptionPane.showOptionDialog(null, objTermLoanRB.getString("existanceSanctionDetailsWarning"), CommonConstants.WARNINGTITLE,
                COptionPane.OK_OPTION, COptionPane.WARNING_MESSAGE, null, options, options[0]);
    }

    public void setSanctionNumber() {
        try {
            HashMap eachRec;
            Set keySet = sanctionFacilityAll.keySet();
            Object[] objKeySet = (Object[]) keySet.toArray();
            // To set the Sanction Number for the Inserted Records
            // It will be identified by the value NIL
            for (int i = keySet.size() - 1; i >= 0; --i) {
                eachRec = (HashMap) sanctionFacilityAll.get(objKeySet[i]);
                eachRec.put(SANCTION_NO, getTxtSanctionNo());
                sanctionFacilityAll.put(objKeySet[i], eachRec);
                eachRec = null;
            }
            keySet = null;
            objKeySet = null;
        } catch (Exception e) {
            log.info("Exception caught in setSanctionNumber()..." + e);
            parseException.logException(e, true);
        }
    }

    public int addSanctionMainTab(int row, boolean update) {
        int option = -1;
        try {
            HashMap result = new HashMap();
            ArrayList data = tblSanctionMain.getDataArrayList();
            tblSanctionMain.setDataArrayList(data, sanctionMainTitle);
            final int dataSize = data.size();
            insertSanctionMain(dataSize + 1, sanctionFacilityAllTabRecords, sanctionFacilityAll);
            if (!update) {
                // If the table is not in Edit Mode
                result = tableUtilSanction_Main.insertTableValues(sanctionMainTabRow, sanctionMainRecord, SANCTION_SL_NO);

                sanctionMainAllTabRecords = (ArrayList) result.get(TABLE_VALUES);
                sanctionMainAll = (LinkedHashMap) result.get(ALL_VALUES);
                option = CommonUtil.convertObjToInt(result.get(OPTION));

                tblSanctionMain.setDataArrayList(sanctionMainAllTabRecords, sanctionMainTitle);
                option = dataSize;
            } else {
                option = updateSanctionMain(row);
                option = row;
            }
            //            tblSanctionFacility.setDataArrayList(null, sanctionFacilityTitle);
            setChanged();
            result = null;
            data = null;
        } catch (Exception e) {
            log.info("Exception caught in addSanctionMainTab..." + e);
            parseException.logException(e, true);
        }
        return option;
    }

    private void insertSanctionMain(int slno, ArrayList tableValues, LinkedHashMap allValues) {
        try {
            sanctionMainTabRow.add(String.valueOf(slno));
            sanctionMainTabRow.add(getTxtSanctionNo());
            sanctionMainTabRow.add(getCboSanctioningAuthority());
            sanctionMainTabRow.add(getCboModeSanction());

            sanctionMainRecord.put(SANCTION_NO, getTxtSanctionNo());
            sanctionMainRecord.put(SANCTION_SL_NO, String.valueOf(slno));
            sanctionMainRecord.put(SANCTION_DATE, tdtSanctionDate);
            sanctionMainRecord.put(SANCTION_AUTHORITY, CommonUtil.convertObjToStr(cbmSanctioningAuthority.getKeyForSelected()));
            sanctionMainRecord.put(REMARKS, txtSanctionRemarks);
            sanctionMainRecord.put(SANCTION_MODE, CommonUtil.convertObjToStr(cbmModeSanction.getKeyForSelected()));
            sanctionMainRecord.put(SANCTION_FACILITY_TABLE, tableValues);
            sanctionMainRecord.put(SANCTION_FACILITY_ALL, allValues);
            // To populate the RemovedValues(ArrayList) in Sanction_Facility TableUtil Object
            // for the first time only after retrieving from the Database
            sanctionMainRecord.put(FLAG, FALSE);
            sanctionMainRecord.put(COMMAND, "");
        } catch (Exception e) {
            log.info("Exception caught in insertSanctionMain..." + e);
            parseException.logException(e, true);
        }
    }

    public void populateSanctionMain(int slno) {
        try {
            HashMap eachRecs = new HashMap();
            Set keySet = sanctionMainAll.keySet();
            Object[] objKeySet = (Object[]) keySet.toArray();
            ArrayList sanctionMainTableValue = (ArrayList) tblSanctionMain.getDataArrayList().get(slno);

            // To populate the corresponding record from the sanction details
            for (int i = sanctionMainAll.size() - 1, j = 0; i >= 0; --i, ++j) {
                if ((CommonUtil.convertObjToStr(((HashMap) sanctionMainAll.get(objKeySet[j])).get(SANCTION_SL_NO))).equals(CommonUtil.convertObjToStr(sanctionMainTableValue.get(0)))) {
                    // To populate the Corresponding record from CTable
                    eachRecs = (HashMap) sanctionMainAll.get(objKeySet[j]);
                    setTxtSanctionNo(CommonUtil.convertObjToStr(eachRecs.get(SANCTION_NO)));
                    setTxtSanctionSlNo(CommonUtil.convertObjToStr(eachRecs.get(SANCTION_SL_NO)));
                    setStrRealSanctionNo(getTxtSanctionNo());
                    setTdtSanctionDate(CommonUtil.convertObjToStr(eachRecs.get(SANCTION_DATE)));
                    setCboSanctioningAuthority(CommonUtil.convertObjToStr(getCbmSanctioningAuthority().getDataForKey(CommonUtil.convertObjToStr(eachRecs.get(SANCTION_AUTHORITY)))));
                    setTxtSanctionRemarks(CommonUtil.convertObjToStr(eachRecs.get(REMARKS)));
                    setCboModeSanction(CommonUtil.convertObjToStr(getCbmModeSanction().getDataForKey(CommonUtil.convertObjToStr(eachRecs.get(SANCTION_MODE)))));
                    sanctionFacilityAllTabRecords = (ArrayList) eachRecs.get(SANCTION_FACILITY_TABLE);
                    sanctionFacilityAll = (LinkedHashMap) eachRecs.get(SANCTION_FACILITY_ALL);

                    //to update changesanction no into sanction facility & facility all
                    oldSanction_no = CommonUtil.convertObjToStr(eachRecs.get(SANCTION_NO));
                    // To populate the RemovedValues(ArrayList) in Sanction_Facility TableUtil Object
                    // for the first time only after retrieving from the Database
                    if (eachRecs.get(FLAG).equals(TRUE)) {
                        tableUtilSanction_Facility.setRemovedValues((ArrayList) eachRecs.get(SANCTION_FACILITY_DELETED));
                        //                        eachRecs.put(FLAG, FALSE);
                    }
                    tableUtilSanction_Facility.setAllValues(sanctionFacilityAll);
                    tableUtilSanction_Facility.setTableValues(sanctionFacilityAllTabRecords);
                    tableUtilSanction_Facility.setStr_MAX_DEL_SL_NO(CommonUtil.convertObjToStr(eachRecs.get(MAX_DEL_SAN_DETAIL_SL_NO)));

                    tblSanctionFacility.setDataArrayList(sanctionFacilityAllTabRecords, sanctionFacilityTitle);
                    // For Sanction Facility Table (According to the Sanction Number the Facility Details will be displayed
                    createSanctionMainRowObjects();
                    sanctionMainAll.put(objKeySet[j], eachRecs);
                    setChanged();
                    break;
                }
            }
            eachRecs = null;
            keySet = null;
            objKeySet = null;
            sanctionMainTableValue = null;
        } catch (Exception e) {
            log.info("Exception caught in populateSanctionMain..." + e);
            parseException.logException(e, true);
        }
    }

    public void createSanctionMainRowObjects() {
        sanctionMainTabRow = new ArrayList();
        sanctionMainRecord = new HashMap();
    }

    private int updateSanctionMain(int row) {
        HashMap result = new HashMap();
        int option = -1;
        Object tempVal = null;
        try {
            result = tableUtilSanction_Main.updateTableValues(sanctionMainTabRow, sanctionMainRecord, row);
            sanctionMainAllTabRecords = (ArrayList) result.get(TABLE_VALUES);
            sanctionMainAll = (LinkedHashMap) result.get(ALL_VALUES);
            option = CommonUtil.convertObjToInt(result.get(OPTION));

            tblSanctionMain.setDataArrayList(sanctionMainAllTabRecords, sanctionMainTitle);
        } catch (Exception e) {
            log.info("Exception caught in updateSanctionMain()..." + e);
            parseException.logException(e, true);
        }
        result = null;
        return option;
    }

    public void deleteSanctionMain(int row) {
        try {
            HashMap result = new HashMap();
            result = tableUtilSanction_Main.deleteTableValues(row);
            sanctionMainAll = (LinkedHashMap) result.get(ALL_VALUES);
            // To rearrange the Sanction Number in Sanction Facility Details Table
            // rearrangeSanctionNo();
            sanctionMainAllTabRecords = (ArrayList) result.get(TABLE_VALUES);
            tblSanctionMain.setDataArrayList(sanctionMainAllTabRecords, sanctionMainTitle);
            tblSanctionFacility.setDataArrayList(null, sanctionFacilityTitle);
            result = null;
        } catch (Exception e) {
            log.info("Exception caught in deleteSanctionMain()..." + e);
            parseException.logException(e, true);
        }
    }

    public void populateFacilityTabSanction(int sanctionNumberFacility/*, int sanctionTabNumber*/) {
        try {
            HashMap eachRecs;
            facilityTabSanction = new ArrayList();
            Set keySet = sanctionMainAll.keySet();
            Object[] objKeySet = (Object[]) keySet.toArray();
            String strRecordKey = CommonUtil.convertObjToStr(((ArrayList) (tblSanctionMain.getDataArrayList().get(sanctionNumberFacility))).get(0));
            // To populate the corresponding record from Facility Details
            for (int i = sanctionMainAll.size() - 1, j = 0; i >= 0; --i, ++j) {
                if ((CommonUtil.convertObjToStr(((HashMap) sanctionMainAll.get(objKeySet[j])).get(SANCTION_SL_NO))).equals(String.valueOf(strRecordKey))) {
                    // To populate the Corresponding record from CTable
                    eachRecs = new HashMap();
                    eachRecs = (HashMap) sanctionMainAll.get(objKeySet[j]);
                    facilityTabSanction = (ArrayList) eachRecs.get(SANCTION_FACILITY_TABLE);
                    //                    if (sanctionNumberFacility == sanctionTabNumber){
                    // if facility table sanction number and
                    // sanction table sanction number are equal
                    facilityTabSanction = sanctionFacilityAllTabRecords;
                    //                    }

                    tblSanctionFacility.setDataArrayList(facilityTabSanction, sanctionFacilityTitle);
                    setChanged();
                    break;
                }
            }
            keySet = null;
            objKeySet = null;
            eachRecs = null;
            strRecordKey = null;
        } catch (Exception e) {
            log.info("Exception caught in populateFacilityTabSanction: " + e);
            parseException.logException(e, true);
        }
    }

    public void addFacilityDetails(int sanctionSlNo, int slno) {
        try {
            facilityRecord = new HashMap();
            String strSanctionNo = CommonUtil.convertObjToStr(((ArrayList) tblSanctionMain.getDataArrayList().get(sanctionSlNo)).get(0));
            //            sanctionSlNo   = CommonUtil.convertObjToInt(((ArrayList)tblSanctionMain.getDataArrayList().get(sanctionSlNo)).get(0));
            slno = CommonUtil.convertObjToInt(((ArrayList) tblSanctionFacility.getDataArrayList().get(slno)).get(0));
            String strFacilityKey = getFacilityKey(strSanctionNo, slno);
            insertFacilityDetails(strSanctionNo, slno);
            if (facilityAll.containsKey(strFacilityKey) && getStrACNumber().length() > 0) {
                // If the key already exist in the Linked Hash Map then it status
                // will be changed to UPDATE
                if (getBEHAVES_LIKE().equals("OD")) {
                    facilityRecord.put(AVAILABLE_BALANCE, getRenewAvailableBalance());
                } else {
                    if (isUpdateAvailableBalance()) {
                        facilityRecord.put(AVAILABLE_BALANCE, getTxtLimit_SD());
                    } else {
                        HashMap facilityRec = (HashMap) facilityAll.get(strFacilityKey);
                        facilityRecord.put(AVAILABLE_BALANCE, facilityRec.get(AVAILABLE_BALANCE));
                    }
                }

                facilityRecord.put(COMMAND, UPDATE);

            } else {
                // At the time of creating new account set the available balance, total available balance
                // shadow debit, shadow credit, clear balance, unclear balance
                facilityRecord.put(SHADOW_DEBIT, "0");
                facilityRecord.put(SHADOW_CREDIT, "0");
                facilityRecord.put(AVAILABLE_BALANCE, getTxtLimit_SD());
                facilityRecord.put(TOTAL_AVAILABLE_BALANCE, "0");
                facilityRecord.put(CLEAR_BALANCE, "0");
                facilityRecord.put(UNCLEAR_BALANCE, "0");
            }
            if (getActionType() != 1) {
                // If the status is not New
                setActionType(ClientConstants.ACTIONTYPE_EDIT);
            }
            facilityAll.put(strFacilityKey, facilityRecord);
            strSanctionNo = null;
            setBEHAVES_LIKE("");
        } catch (Exception e) {
            log.info("Exception caught in addFacilityDetails: " + e);
            parseException.logException(e, true);
        }
    }

    private void insertFacilityDetails(String strSanctionNo, int slno) {
        facilityRecord = new HashMap();
        facilityRecord.put(PROD_ID, CommonUtil.convertObjToStr("GL"));
        //        facilityRecord.put(PROD_ID, CommonUtil.convertObjToStr(getLblProductID_FD_Disp()));
        facilityRecord.put(ACCT_STATUS, CommonUtil.convertObjToStr(cbmAccStatus.getKeyForSelected()));
        if (strSanctionNo.length() > 0) {
            facilityRecord.put(SANCTION_NO, strSanctionNo);
        }
        if (slno > 0) {
            facilityRecord.put(SLNO, String.valueOf(slno));
        }
        facilityRecord.put(INTEREST_TYPE, CommonUtil.convertObjToStr(cbmInterestType.getKeyForSelected()));
        facilityRecord.put(NOTE_DATE, getTdtDemandPromNoteDate());
        facilityRecord.put(NOTE_EXP_DATE, getTdtDemandPromNoteExpDate());
        facilityRecord.put(AOD_DATE, getTdtAODDate());
        facilityRecord.put(PURPOSE_DESC, getTxtPurposeDesc());
        facilityRecord.put(GROUP_DESC, getTxtGroupDesc());
        facilityRecord.put(CONTACT_PERSON, getTxtContactPerson());
        facilityRecord.put(CONTACT_PHONE, getTxtContactPhone());
        facilityRecord.put(REMARKS, getTxtRemarks());
        facilityRecord.put(ACCT_NAME, getTxtAcct_Name());
        facilityRecord.put(INT_GET_FROM, CommonUtil.convertObjToStr(cbmIntGetFrom.getKeyForSelected()));
        facilityRecord.put(COMMAND, INSERT);
        facilityRecord.put(AUTHORIZED, NO);
        facilityRecord.put(AUTHORIZE_BY_1, null);
        facilityRecord.put(AUTHORIZE_BY_2, null);
        facilityRecord.put(AUTHORIZE_DT_1, null);
        facilityRecord.put(AUTHORIZE_DT_2, null);
        facilityRecord.put(AUTHORIZE_STATUS_1, null);
        facilityRecord.put(AUTHORIZE_STATUS_2, null);
        facilityRecord.put(ACCT_OPEN_DT, getAccountOpenDate());
        facilityRecord.put(RECOMMANDED_BY, getCboRecommendedByType());
        facilityRecord.put(ACCT_NUM, getStrACNumber());;

        if (getRdoSecurityDetails_Unsec() == true) {
            facilityRecord.put(SECURITY_DETAILS, UNSECURED);
        } else if (getRdoSecurityDetails_Partly() == true) {
            facilityRecord.put(SECURITY_DETAILS, PARTLY_SECURED);
        } else if (getRdoSecurityDetails_Fully() == true) {
            facilityRecord.put(SECURITY_DETAILS, FULLY_SECURED);
        } else {
            facilityRecord.put(SECURITY_DETAILS, "");
        }
        if (isRdoDP_YES()) {
            facilityRecord.put(DRAWING_POWER, "Y");
        } else if (isRdoDP__NO()) {
            facilityRecord.put(DRAWING_POWER, "N");
        } else {
            facilityRecord.put(DRAWING_POWER, " ");
        }
        if ((getChkStockInspect() == true) && (getChkInsurance() == true) && (getChkGurantor() == true)) {
            facilityRecord.put(SECURITY_TYPE, INSPECT_INSURANCE_GUARANTAR);
        } else if ((getChkStockInspect() == true) && (getChkInsurance() == true)) {
            facilityRecord.put(SECURITY_TYPE, STOCK_INSPECT_INSURANCE);
        } else if ((getChkStockInspect() == true) && (getChkGurantor() == true)) {
            facilityRecord.put(SECURITY_TYPE, STOCK_INSPECT_GUARANTAR);
        } else if ((getChkInsurance() == true) && (getChkGurantor() == true)) {
            facilityRecord.put(SECURITY_TYPE, INSURANCE_GUARANTAR);
        } else if (getChkStockInspect() == true) {
            facilityRecord.put(SECURITY_TYPE, STOCK_INSPECT);
        } else if (getChkInsurance() == true) {
            facilityRecord.put(SECURITY_TYPE, INSURANCE);
        } else if (getChkGurantor() == true) {
            facilityRecord.put(SECURITY_TYPE, GUARANTAR);
        } else {
            facilityRecord.put(SECURITY_TYPE, "");
        }
        if (getChkAcctTransfer() == true) {
            facilityRecord.put(ACCTTRANSFER, ACCTTRANSFER);
        } else {
            facilityRecord.put(ACCTTRANSFER, "");
        }

        if (getRdoAccType_New() == true) {
            facilityRecord.put(ACC_TYPE, NEW);
        } else if (getRdoAccType_Transfered() == true) {
            facilityRecord.put(ACC_TYPE, TRANSFERED);
        } else {
            facilityRecord.put(ACC_TYPE, "");
        }

        if (getRdoAccLimit_Main() == true) {
            facilityRecord.put(ACC_LIMIT, MAIN);
        } else if (getRdoAccLimit_Submit() == true) {
            facilityRecord.put(ACC_LIMIT, SUBMIT);
        } else {
            facilityRecord.put(ACC_LIMIT, "");
        }

        if (getRdoRiskWeight_Yes() == true) {
            facilityRecord.put(RISK_WEIGHT, YES);
        } else if (getRdoRiskWeight_No() == true) {
            facilityRecord.put(RISK_WEIGHT, NO);
        } else {
            facilityRecord.put(RISK_WEIGHT, " ");
        }

        if (getRdoMultiDisburseAllow_Yes() == true) {
            facilityRecord.put(MULTI_DISBURSE, YES);
        } else if (getRdoMultiDisburseAllow_No() == true) {
            facilityRecord.put(MULTI_DISBURSE, NO);
        } else {
            facilityRecord.put(MULTI_DISBURSE, " ");
        }

        if (getRdoInterest_Simple() == true) {
            facilityRecord.put(INTEREST, SIMPLE);
        } else if (getRdoInterest_Compound() == true) {
            facilityRecord.put(INTEREST, COMPOUND);
        } else {
            facilityRecord.put(INTEREST, " ");
        }
        if (isChkAuthorizedSignatory()) {
            facilityRecord.put(AUTHSIGNATORY, AUTHSIGNATORY);
        } else {
            facilityRecord.put(AUTHSIGNATORY, " ");
        }

        if (isChkPOFAttorney()) {
            facilityRecord.put(POFATTORNEY, POFATTORNEY);
        } else {
            facilityRecord.put(POFATTORNEY, " ");
        }

        if (isChkDocDetails()) {
            facilityRecord.put(DOCDETAILS, DOCDETAILS);
        } else {
            facilityRecord.put(DOCDETAILS, " ");
        }

        if (getStrACNumber().length() > 0) {
            // If the key already exist in the Linked Hash Map then it status
            // will be changed to UPDATE
            if (getBEHAVES_LIKE().equals("OD")) {
                facilityRecord.put(AVAILABLE_BALANCE, getRenewAvailableBalance());
            } else {
                if (getClearBalance() < 0) {
                    facilityRecord.put(AVAILABLE_BALANCE, String.valueOf(getAvailableBalance()));
                } else if (isUpdateAvailableBalance()) {
                    facilityRecord.put(AVAILABLE_BALANCE, getTxtLimit_SD());
                }

            }

        } else {
            // At the time of creating new account set the available balance, total available balance
            // shadow debit, shadow credit, clear balance, unclear balance
            facilityRecord.put(SHADOW_DEBIT, "0");
            facilityRecord.put(SHADOW_CREDIT, "0");
            facilityRecord.put(AVAILABLE_BALANCE, getTxtLimit_SD());
            facilityRecord.put(TOTAL_AVAILABLE_BALANCE, "0");
            facilityRecord.put(CLEAR_BALANCE, "0");
            facilityRecord.put(UNCLEAR_BALANCE, "0");
        }
    }

    private String getFacilityKey(String sanctionNo, int slno) {
        String strFacilityKey = "";
        StringBuffer strbufKey = new StringBuffer();
        try {
            strbufKey.append(sanctionNo);
            strbufKey.append("#");
            strbufKey.append(String.valueOf(slno));
            strFacilityKey = new String(strbufKey);
        } catch (Exception e) {
            log.info("Exception caught in getFacilityKey: " + e);
            parseException.logException(e, true);
        }
        strbufKey = null;
        return strFacilityKey;
    }

    private void insertRenewalFacilityDetails(String strSanctionNo, int slno) {
        renewalFacilityRecord = new HashMap();
        renewalFacilityRecord.put(PROD_ID, CommonUtil.convertObjToStr("GL"));
        //        facilityRecord.put(PROD_ID, CommonUtil.convertObjToStr(getLblProductID_FD_Disp()));
        renewalFacilityRecord.put(ACCT_STATUS, ClientConstants.VIEW_TYPE_NEW);
        if (strSanctionNo.length() > 0) {
            renewalFacilityRecord.put(SANCTION_NO, strSanctionNo);
        }
        if (slno > 0) {
            renewalFacilityRecord.put(SLNO, String.valueOf(slno));
        }
        renewalFacilityRecord.put(INTEREST_TYPE, CommonUtil.convertObjToStr(cbmInterestType.getKeyForSelected()));
        renewalFacilityRecord.put(NOTE_DATE, getTdtRenewalDemandPromNoteDate());
        renewalFacilityRecord.put(NOTE_EXP_DATE, getTdtRenewalDemandPromNoteExpDate());
        renewalFacilityRecord.put(AOD_DATE, getTdtRenewalDemandPromNoteExpDate());
        renewalFacilityRecord.put(PURPOSE_DESC, getTxtPurposeDesc());
        renewalFacilityRecord.put(GROUP_DESC, getTxtGroupDesc());
        renewalFacilityRecord.put(CONTACT_PERSON, getTxtContactPerson());
        renewalFacilityRecord.put(CONTACT_PHONE, getTxtContactPhone());
        renewalFacilityRecord.put(REMARKS, getTxtRemarks());
        renewalFacilityRecord.put(ACCT_NAME, getTxtAcct_Name());
        renewalFacilityRecord.put(INT_GET_FROM, "ACT");
        renewalFacilityRecord.put(COMMAND, INSERT);
        renewalFacilityRecord.put(AUTHORIZED, NO);
        renewalFacilityRecord.put(AUTHORIZE_BY_1, null);
        renewalFacilityRecord.put(AUTHORIZE_BY_2, null);
        renewalFacilityRecord.put(AUTHORIZE_DT_1, null);
        renewalFacilityRecord.put(AUTHORIZE_DT_2, null);
        renewalFacilityRecord.put(AUTHORIZE_STATUS_1, null);
        renewalFacilityRecord.put(AUTHORIZE_STATUS_2, null);
        renewalFacilityRecord.put(ACCT_OPEN_DT, getTdtRenewalAccountOpenDate());
        renewalFacilityRecord.put(RECOMMANDED_BY, getCboRecommendedByType());
        renewalFacilityRecord.put(ACCT_NUM, getLblRenewalAcctNo_Sanction_Disp());
        renewalFacilityRecord.put(RENEWAL_ACCT_NUM, getStrACNumber());

        if (getRdoSecurityDetails_Unsec() == true) {
            renewalFacilityRecord.put(SECURITY_DETAILS, UNSECURED);
        } else if (getRdoSecurityDetails_Partly() == true) {
            renewalFacilityRecord.put(SECURITY_DETAILS, PARTLY_SECURED);
        } else if (getRdoSecurityDetails_Fully() == true) {
            renewalFacilityRecord.put(SECURITY_DETAILS, FULLY_SECURED);
        } else {
            renewalFacilityRecord.put(SECURITY_DETAILS, "");
        }
        if (isRdoDP_YES()) {
            renewalFacilityRecord.put(DRAWING_POWER, "Y");
        } else if (isRdoDP__NO()) {
            renewalFacilityRecord.put(DRAWING_POWER, "N");
        } else {
            renewalFacilityRecord.put(DRAWING_POWER, " ");
        }
        if ((getChkStockInspect() == true) && (getChkInsurance() == true) && (getChkGurantor() == true)) {
            renewalFacilityRecord.put(SECURITY_TYPE, INSPECT_INSURANCE_GUARANTAR);
        } else if ((getChkStockInspect() == true) && (getChkInsurance() == true)) {
            renewalFacilityRecord.put(SECURITY_TYPE, STOCK_INSPECT_INSURANCE);
        } else if ((getChkStockInspect() == true) && (getChkGurantor() == true)) {
            renewalFacilityRecord.put(SECURITY_TYPE, STOCK_INSPECT_GUARANTAR);
        } else if ((getChkInsurance() == true) && (getChkGurantor() == true)) {
            renewalFacilityRecord.put(SECURITY_TYPE, INSURANCE_GUARANTAR);
        } else if (getChkStockInspect() == true) {
            facilityRecord.put(SECURITY_TYPE, STOCK_INSPECT);
        } else if (getChkInsurance() == true) {
            renewalFacilityRecord.put(SECURITY_TYPE, INSURANCE);
        } else if (getChkGurantor() == true) {
            renewalFacilityRecord.put(SECURITY_TYPE, GUARANTAR);
        } else {
            renewalFacilityRecord.put(SECURITY_TYPE, "");
        }
        if (getChkAcctTransfer() == true) {
            renewalFacilityRecord.put(ACCTTRANSFER, ACCTTRANSFER);
        } else {
            renewalFacilityRecord.put(ACCTTRANSFER, "");
        }

        if (getRdoAccType_New() == true) {
            renewalFacilityRecord.put(ACC_TYPE, NEW);
        } else if (getRdoAccType_Transfered() == true) {
            renewalFacilityRecord.put(ACC_TYPE, TRANSFERED);
        } else {
            renewalFacilityRecord.put(ACC_TYPE, "");
        }

        if (getRdoAccLimit_Main() == true) {
            renewalFacilityRecord.put(ACC_LIMIT, MAIN);
        } else if (getRdoAccLimit_Submit() == true) {
            renewalFacilityRecord.put(ACC_LIMIT, SUBMIT);
        } else {
            renewalFacilityRecord.put(ACC_LIMIT, "");
        }

        if (getRdoRiskWeight_Yes() == true) {
            renewalFacilityRecord.put(RISK_WEIGHT, YES);
        } else if (getRdoRiskWeight_No() == true) {
            renewalFacilityRecord.put(RISK_WEIGHT, NO);
        } else {
            renewalFacilityRecord.put(RISK_WEIGHT, " ");
        }

        if (getRdoMultiDisburseAllow_Yes() == true) {
            renewalFacilityRecord.put(MULTI_DISBURSE, YES);
        } else if (getRdoMultiDisburseAllow_No() == true) {
            renewalFacilityRecord.put(MULTI_DISBURSE, NO);
        } else {
            renewalFacilityRecord.put(MULTI_DISBURSE, " ");
        }

        if (getRdoInterest_Simple() == true) {
            renewalFacilityRecord.put(INTEREST, SIMPLE);
        } else if (getRdoInterest_Compound() == true) {
            renewalFacilityRecord.put(INTEREST, COMPOUND);
        } else {
            renewalFacilityRecord.put(INTEREST, " ");
        }
        if (isChkAuthorizedSignatory()) {
            renewalFacilityRecord.put(AUTHSIGNATORY, AUTHSIGNATORY);
        } else {
            renewalFacilityRecord.put(AUTHSIGNATORY, " ");
        }

        if (isChkPOFAttorney()) {
            renewalFacilityRecord.put(POFATTORNEY, POFATTORNEY);
        } else {
            renewalFacilityRecord.put(POFATTORNEY, " ");
        }

        if (isChkDocDetails()) {
            renewalFacilityRecord.put(DOCDETAILS, DOCDETAILS);
        } else {
            renewalFacilityRecord.put(DOCDETAILS, " ");
        }

        if (getStrACNumber().length() > 0) {
            // If the key already exist in the Linked Hash Map then it status
            // will be changed to UPDATE
//            if(getBEHAVES_LIKE().equals("OD"))
//                renewalFacilityRecord.put(AVAILABLE_BALANCE, getRenewAvailableBalance());
//            else {
//                if(getClearBalance()<0)
//                    renewalFacilityRecord.put(AVAILABLE_BALANCE, String.valueOf(getAvailableBalance()));
//                else if (isUpdateAvailableBalance())
            renewalFacilityRecord.put(AVAILABLE_BALANCE, getTxtRenewalLimit_SD());

//            }

        } else {
            // At the time of creating new account set the available balance, total available balance
            // shadow debit, shadow credit, clear balance, unclear balance
            renewalFacilityRecord.put(SHADOW_DEBIT, "0");
            renewalFacilityRecord.put(SHADOW_CREDIT, "0");
            renewalFacilityRecord.put(AVAILABLE_BALANCE, getTxtLimit_SD());
            renewalFacilityRecord.put(TOTAL_AVAILABLE_BALANCE, "0");
            renewalFacilityRecord.put(CLEAR_BALANCE, "0");
            renewalFacilityRecord.put(UNCLEAR_BALANCE, "0");
        }
    }

    public void populateFacilityDetails(int sanctionSlNo, int slno) {
        try {
            ArrayList data = tblSanctionMain.getDataArrayList();
            String strSanctionNo = CommonUtil.convertObjToStr(((ArrayList) data.get(sanctionSlNo)).get(1));
            sanctionSlNo = CommonUtil.convertObjToInt(((ArrayList) data.get(sanctionSlNo)).get(0));

            //            termLoanSecurityOB.setLimitAmount(CommonUtil.convertObjToDouble(((ArrayList)tblSanctionFacility.getDataArrayList().get(slno)).get(2)).doubleValue());
            termLoanInterestOB.setLblLimitAmt_2(CommonUtil.convertObjToStr(((ArrayList) tblSanctionFacility.getDataArrayList().get(slno)).get(2)));
            termLoanInterestOB.setLblExpiryDate_2(CommonUtil.convertObjToStr(((ArrayList) tblSanctionFacility.getDataArrayList().get(slno)).get(4)));
            slno = CommonUtil.convertObjToInt(((ArrayList) tblSanctionFacility.getDataArrayList().get(slno)).get(0));

            // To set the sanction no. in Classification details
            termLoanClassificationOB.setLblSanctionNo2(strSanctionNo);
            String strFacilityKey = getFacilityKey(String.valueOf(sanctionSlNo), slno);
            termLoanClassificationOB.setLblSanctionDate2(CommonUtil.convertObjToStr(((HashMap) sanctionMainAll.get(String.valueOf(sanctionSlNo))).get(SANCTION_DATE)));
            termLoanInterestOB.setLblSancDate_2(termLoanClassificationOB.getLblSanctionDate2());
            getPLR_Rate(CommonUtil.convertObjToStr(getCbmProductId().getKeyForSelected()));
            resetFacilityDetails();
            if (facilityAll.containsKey(strFacilityKey)) {
                // To populate the Corresponding record from CTable
                HashMap oneFacilityRec = (HashMap) facilityAll.get(strFacilityKey);

                setTxtContactPerson(CommonUtil.convertObjToStr(oneFacilityRec.get(CONTACT_PERSON)));
                setTxtContactPhone(CommonUtil.convertObjToStr(oneFacilityRec.get(CONTACT_PHONE)));
                setTxtRemarks(CommonUtil.convertObjToStr(oneFacilityRec.get(REMARKS)));
                setTxtPurposeDesc(CommonUtil.convertObjToStr(oneFacilityRec.get(PURPOSE_DESC)));
                setTxtGroupDesc(CommonUtil.convertObjToStr(oneFacilityRec.get(GROUP_DESC)));
                setTdtDemandPromNoteDate(CommonUtil.convertObjToStr(oneFacilityRec.get(NOTE_DATE)));
                setTdtDemandPromNoteExpDate(CommonUtil.convertObjToStr(oneFacilityRec.get(NOTE_EXP_DATE)));
                setTdtAODDate(CommonUtil.convertObjToStr(oneFacilityRec.get(AOD_DATE)));
                setTxtAcct_Name(CommonUtil.convertObjToStr(oneFacilityRec.get(ACCT_NAME)));
                setAccountOpenDate(CommonUtil.convertObjToStr(oneFacilityRec.get(ACCT_OPEN_DT)));
                setAccountCloseDate(CommonUtil.convertObjToStr(oneFacilityRec.get(ACCT_CLOSE_DT)));
                setCboRecommendedByType(CommonUtil.convertObjToStr(getCbmRecommendedByType().getDataForKey(CommonUtil.convertObjToStr(oneFacilityRec.get(RECOMMANDED_BY)))));
                setLast_int_calc_dt(getProperDateFormat(oneFacilityRec.get("LAST_INT_CALC_DT")));
                if (oneFacilityRec.get(SECURITY_DETAILS).equals(UNSECURED)) {
                    setRdoSecurityDetails_Unsec(true);
                } else if (oneFacilityRec.get(SECURITY_DETAILS).equals(PARTLY_SECURED)) {
                    setRdoSecurityDetails_Partly(true);
                } else if (oneFacilityRec.get(SECURITY_DETAILS).equals(FULLY_SECURED)) {
                    setRdoSecurityDetails_Fully(true);
                }

                if (oneFacilityRec.get(SECURITY_TYPE).equals(INSPECT_INSURANCE_GUARANTAR)) {
                    setChkStockInspect(true);
                    setChkInsurance(true);
                    setChkGurantor(true);
                } else if (oneFacilityRec.get(SECURITY_TYPE).equals(STOCK_INSPECT_INSURANCE)) {
                    setChkStockInspect(true);
                    setChkInsurance(true);
                } else if (oneFacilityRec.get(SECURITY_TYPE).equals(STOCK_INSPECT_GUARANTAR)) {
                    setChkStockInspect(true);
                    setChkGurantor(true);
                } else if (oneFacilityRec.get(SECURITY_TYPE).equals(INSURANCE_GUARANTAR)) {
                    setChkInsurance(true);
                    setChkGurantor(true);
                } else if (oneFacilityRec.get(SECURITY_TYPE).equals(STOCK_INSPECT)) {
                    setChkStockInspect(true);
                } else if (oneFacilityRec.get(SECURITY_TYPE).equals(INSURANCE)) {
                    setChkInsurance(true);
                } else if (oneFacilityRec.get(SECURITY_TYPE).equals(GUARANTAR)) {
                    setChkGurantor(true);
                }
                if (oneFacilityRec.get(ACCTTRANSFER).equals(ACCTTRANSFER)) {
                    setChkAcctTransfer(true);
                }
                if (oneFacilityRec.get(ACC_TYPE).equals(NEW)) {
                    setRdoAccType_New(true);
                } else if (oneFacilityRec.get(ACC_TYPE).equals(TRANSFERED)) {
                    setRdoAccType_Transfered(true);
                }

                if (oneFacilityRec.get(ACC_LIMIT).equals(MAIN)) {
                    setRdoAccLimit_Main(true);
                } else if (oneFacilityRec.get(ACC_LIMIT).equals(SUBMIT)) {
                    setRdoAccLimit_Submit(true);
                }

                if (oneFacilityRec.get(RISK_WEIGHT).equals(YES)) {
                    setRdoRiskWeight_Yes(true);
                } else if (oneFacilityRec.get(RISK_WEIGHT).equals(NO)) {
                    setRdoRiskWeight_No(true);
                }

                //                if (oneFacilityRec.get(INTEREST_NATURE).equals(PLR)){
                //                    setRdoNatureInterest_PLR(true);
                //                }else if (oneFacilityRec.get(INTEREST_NATURE).equals(NON_PLR)){
                //                    setRdoNatureInterest_NonPLR(true);
                //                }

                if (oneFacilityRec.get(MULTI_DISBURSE).equals(YES)) {
                    setRdoMultiDisburseAllow_Yes(true);
                } else if (oneFacilityRec.get(MULTI_DISBURSE).equals(NO)) {
                    setRdoMultiDisburseAllow_No(true);
                }

                if (oneFacilityRec.get(INTEREST).equals(SIMPLE)) {
                    setRdoInterest_Simple(true);
                } else if (oneFacilityRec.get(INTEREST).equals(COMPOUND)) {
                    setRdoInterest_Compound(true);
                }
                if (oneFacilityRec.get(DRAWING_POWER) != null && oneFacilityRec.get(DRAWING_POWER).equals(YES)) {
                    setRdoDP_YES(true);
                } else if (oneFacilityRec.get(DRAWING_POWER) != null && oneFacilityRec.get(DRAWING_POWER).equals(NO)) {
                    setRdoDP__NO(true);
                }
                setCboAccStatus(CommonUtil.convertObjToStr(getCbmAccStatus().getDataForKey(CommonUtil.convertObjToStr(oneFacilityRec.get(ACCT_STATUS)))));
                cbmIntGetFrom.setKeyForSelected(oneFacilityRec.get(INT_GET_FROM));
                //                setCboIntGetFrom(CommonUtil.convertObjToStr(getCbmIntGetFrom().getDataForKey(CommonUtil.convertObjToStr(oneFacilityRec.get(INT_GET_FROM)))));
                setCboInterestType(CommonUtil.convertObjToStr(getCbmInterestType().getDataForKey(CommonUtil.convertObjToStr(oneFacilityRec.get(INTEREST_TYPE)))));
                // transaction happen or not
                setShadowDebit(CommonUtil.convertObjToDouble(oneFacilityRec.get("SHADOW_DEBIT")).doubleValue());
                setShadowCredit(CommonUtil.convertObjToDouble(oneFacilityRec.get("SHADOW_CREDIT")).doubleValue());
                setClearBalance(CommonUtil.convertObjToDouble(oneFacilityRec.get("CLEAR_BALANCE")).doubleValue());
                //
                populateAccountNumber(String.valueOf(sanctionSlNo), slno);
                oneFacilityRec = null;
            } else {
                resetBasedOnAcctNumber();
                if (ProductFacilityType.length() > 0 && ProductFacilityType.equals("SI_BEARING")) {
                    setRdoInterest_Simple(true);
                    setRdoInterest_Compound(false);
                } else {
                    setRdoInterest_Simple(false);
                    setRdoInterest_Compound(true);
                }
                //                setAccountOpenDate(CommonUtil.convertObjToStr(DateUtil.getDateWithoutMinitues(curDate)));
                setAccountOpenDate(CommonUtil.convertObjToStr(curDate));
                if (loanType.equals("LTD")) {
                    setRdoMultiDisburseAllow_No(true);
                }
            }
            if (loanType.equals("LTD")) {
                setRdoSecurityDetails_Fully(true);
                setCboInterestType(CommonUtil.convertObjToStr(getCbmInterestType().getDataForKey(FIXED_RATE)));
                //                setRdoMultiDisburseAllow_No(true);
                if (depositCustDetMap != null) {
                    String intType = CommonUtil.convertObjToStr(depositCustDetMap.get("INT_TYPE"));
                    //                    if (intType.equals(SIMPLE)) {
                    //                        setRdoInterest_Simple(true);
                    //                        setRdoInterest_Compound(false);
                    //                    } else {
                    //                        setRdoInterest_Simple(false);
                    //                        setRdoInterest_Compound(true);
                    //                    }
                }
            }
            strSanctionNo = null;
            setChanged();
            ttNotifyObservers();
        } catch (Exception e) {
            log.info("Exception caught in populateFacilityDetails: " + e);
            parseException.logException(e, true);
        }
    }

    public void setLTDSecurityDetails() {
        if (loanType.equals("LTD")) {
            setRdoSecurityDetails_Fully(true);
            setRdoSecurityDetails_Unsec(false);

            setCboInterestType(CommonUtil.convertObjToStr(getCbmInterestType().getDataForKey(FIXED_RATE)));

            //                setRdoMultiDisburseAllow_No(true);
            if (depositCustDetMap != null) {
                String intType = CommonUtil.convertObjToStr(depositCustDetMap.get("INT_TYPE"));
                //                    if (intType.equals(SIMPLE)) {
                //                        setRdoInterest_Simple(true);
                //                        setRdoInterest_Compound(false);
                //                    } else {
                //                        setRdoInterest_Simple(false);
                //                        setRdoInterest_Compound(true);
                //                    }
            }
        }


        if (loanType.equals("LTD")) {
            setRdoMultiDisburseAllow_No(true);
        }
    }

    public void setFacilityProdID(int slno) {
        try {
            HashMap eachRecs = new HashMap();
            Set keySet = sanctionFacilityAll.keySet();
            Object[] objKeySet = (Object[]) keySet.toArray();
            ArrayList sanctionFacilityTableValue = (ArrayList) tblSanctionFacility.getDataArrayList().get(slno);
            // To populate the corresponding Sanction Facility Details
            for (int i = sanctionFacilityAll.size() - 1, j = 0; i >= 0; --i, ++j) {
                // If the record matches with the key then populate it in UI
                if ((CommonUtil.convertObjToStr(((HashMap) sanctionFacilityAll.get(objKeySet[j])).get(SLNO))).equals(CommonUtil.convertObjToStr(sanctionFacilityTableValue.get(0)))) {
                    // To populate the Corresponding record from CTable
                    eachRecs = (HashMap) sanctionFacilityAll.get(objKeySet[j]);
                    setLblProductID_FD_Disp(CommonUtil.convertObjToStr(eachRecs.get(PROD_ID)));
                    populateRestofProdId_AccHead();
                    setChanged();
                    break;
                }
                eachRecs = null;
            }
            eachRecs = null;
            keySet = null;
            objKeySet = null;
            sanctionFacilityTableValue = null;
        } catch (Exception e) {
            log.info("Exception caught in setFacilityProdID: " + e);
            parseException.logException(e, true);
        }
    }

    private void populateRestofProdId_AccHead() {
        try {
            HashMap idTransactionMap = new HashMap();
            HashMap idRetrieve;
            idTransactionMap.put("prodId", getLblProductID_FD_Disp());
            List idResultList = ClientUtil.executeQuery("TermLoan.getProdHead", idTransactionMap);
            if (idResultList.size() > 0) {
                // If Product Account Head exist in Database
                idRetrieve = (HashMap) idResultList.get(0);
                setLblAccountHead_FD_Disp(CommonUtil.convertObjToStr(idRetrieve.get("AC_HEAD")));
            }
            idRetrieve = null;
            idTransactionMap = null;
            idResultList = null;
            updateProdID_AccHead();
        } catch (Exception e) {
            log.info("Exception caught in populateRestofProdId_AccHead: " + e);
            parseException.logException(e, true);
        }
    }

    public HashMap getCompFreqRoundOffValues() {
        HashMap transactionMap = new HashMap();
        HashMap retrieve = new HashMap();
        try {
            transactionMap.put("PROD_ID", getLblProductID_FD_Disp());
            List resultList = ClientUtil.executeQuery("getCompFreqRoundOff_LoanProd", transactionMap);
            if (resultList.size() > 0) {
                // If Product Account Head exist in Database
                retrieve = (HashMap) resultList.get(0);
            }
            transactionMap = null;
            resultList = null;
        } catch (Exception e) {
            log.info("Exception caught in getCompFreqRoundOffValues: " + e);
            parseException.logException(e, true);
        }
        return retrieve;
    }

    private void populateAccountNumber(String strSanctionNo, int slno) {
        try {
            HashMap transactionMap = new HashMap();
            HashMap retrieve;
            transactionMap.put("borrowNo", getBorrowerNo());
            transactionMap.put("sanctionNo", strSanctionNo);
            transactionMap.put("slNo", String.valueOf(slno));
            List resultList = (List) ClientUtil.executeQuery("getAccountNumber", transactionMap);
            if (resultList.size() > 0) {
                // If the Account Number exist in Database
                retrieve = (HashMap) resultList.get(0);
                cbmIntGetFrom.setKeyForSelected(retrieve.get(INT_GET_FROM));
                setCboIntGetFrom(CommonUtil.convertObjToStr(getCbmIntGetFrom().getDataForKey(CommonUtil.convertObjToStr(retrieve.get(INT_GET_FROM)))));
//                termLoanGuarantorOB.setLblAccNo_GD_2(CommonUtil.convertObjToStr(retrieve.get(ACCT_NUM)));
//                termLoanOtherDetailsOB.setStrACNumber(CommonUtil.convertObjToStr(retrieve.get(ACCT_NUM)));
                termLoanClassificationOB.setLblAccNo_CD_2(CommonUtil.convertObjToStr(retrieve.get(ACCT_NUM)));
                setLblAccNo_Idetail_2(CommonUtil.convertObjToStr(retrieve.get(ACCT_NUM)));
                termLoanInterestOB.setLblAccNo_IM_2(CommonUtil.convertObjToStr(retrieve.get(ACCT_NUM)));
                termLoanRepaymentOB.setLblAccNo_RS_2(CommonUtil.convertObjToStr(retrieve.get(ACCT_NUM)));
                termLoanSecurityOB.setLblAccNoSec_2(CommonUtil.convertObjToStr(retrieve.get(ACCT_NUM)));
//                termLoanOtherDetailsOB.setLblAcctNo_Disp_ODetails(CommonUtil.convertObjToStr(retrieve.get(ACCT_NUM)));
                setStrACNumber(CommonUtil.convertObjToStr(retrieve.get(ACCT_NUM)));
                termLoanDocumentDetailsOB.setLblAcctNo_Disp_DocumentDetails(CommonUtil.convertObjToStr(retrieve.get(ACCT_NUM)));
                termLoanSecurityOB.setLblProdId_Disp(getLblProductID_FD_Disp());
//                termLoanOtherDetailsOB.setLblProdID_Disp_ODetails(getLblProductID_FD_Disp());
                termLoanRepaymentOB.setLblProdID_RS_Disp(getLblProductID_FD_Disp());
//                termLoanGuarantorOB.setLblProdID_GD_Disp(getLblProductID_FD_Disp());
                termLoanDocumentDetailsOB.setLblProdID_Disp_DocumentDetails(getLblProductID_FD_Disp());
                setLblProdID_IDetail_Disp(getLblProductID_FD_Disp());
                termLoanInterestOB.setLblProdID_IM_Disp(getLblProductID_FD_Disp());
                termLoanClassificationOB.setLblProdID_CD_Disp(getLblProductID_FD_Disp());
                if (loanType.equals("LTD") && getStrACNumber().length() > 0) {
                    HashMap accountMap = new HashMap();
                    accountMap.put("ACCT_NUM", getStrACNumber());
                    List lst = ClientUtil.executeQuery("getDepositbeforeAuthDetails", accountMap);//getDepositDetails
                    if (lst != null && lst.size() > 0) {
                        accountMap = (HashMap) lst.get(0);
                        setLblDepositNo(CommonUtil.convertObjToStr(accountMap.get("DEPOSIT_NO")));
                    }
                } else {
                    setLblDepositNo("");
                }
            }
            retrieve = null;
            transactionMap = null;
            resultList = null;
        } catch (Exception e) {
            log.info("Exception caught in populateAccountNumber: " + e);
            parseException.logException(e, true);
        }
    }

    /*
     * If the customer is a share holder then the query will return a value
     * more than 0
     */
    public boolean isThisCustShareHolder(String strCust) {
        boolean isShareHolder = true;
        /*try{
         HashMap transactionMap = new HashMap();
         HashMap retrieve;
         transactionMap.put("CUST_ID", strCust);
         List resultList = (List) ClientUtil.executeQuery("getThisCustShareHolderOrNot", transactionMap);
         if (resultList.size() > 0){
         // If the Account Number exist in Database
         retrieve = (HashMap) resultList.get(0);
         if (CommonUtil.convertObjToInt(retrieve.get("NO_SHARE")) <= 0){
         showWarningMsg("isNotAShareHolderWarning");
         }else{
         isShareHolder = true;
         }
         }
         }catch(Exception e){
         log.info("Exception caught in isThisCustShareHolder: "+e);
         parseException.logException(e,true);
         }*/
        return isShareHolder;
    }

    /*
     * If the customer is a Deposit Account holder then the query will return a value
     * more than 0
     */
    public boolean isThisCustDepositAcctHolder(String strCust) {
        boolean isDepositAcctHolder = false;
        try {
            HashMap transactionMap = new HashMap();
            HashMap retrieve;
            transactionMap.put("CUST_ID", strCust);
            List resultList = (List) ClientUtil.executeQuery("getThisCustDepositAcctHolderOrNot", transactionMap);
            if (resultList.size() > 0) {
                // If the Account Number exist in Database
                retrieve = (HashMap) resultList.get(0);
                if (CommonUtil.convertObjToInt(retrieve.get("NO_ACCT")) <= 0) {
                    showWarningMsg("isNotADepositAcctHolderWarning");
                } else {
                    isDepositAcctHolder = true;
                }
            }
        } catch (Exception e) {
            log.info("Exception caught in isThisCustDepositAcctHolder: " + e);
            parseException.logException(e, true);
        }
        return isDepositAcctHolder;
    }

    //regarding reapayment outstanding +uptodateinterest
    public void interestTransaction(HashMap interstMap) throws Exception {
        try {
            interstMap.put("INT_TRANSACTION_REPAYMENT", "INT_TRANSACTION_REPAYMENT");
            HashMap mapData = (HashMap) proxy.executeQuery(interstMap, operationMap);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public boolean checkSanctionNoDublication(String strSanctionNo) {
        boolean save = false;
        try {
            HashMap transactionMap = new HashMap();
            HashMap retrieve;
            transactionMap.put("sanctionNo", strSanctionNo);
            List resultList = (List) ClientUtil.executeQuery("getCountOfSanctionNo", transactionMap);
            if (!(strSanctionNo.equals(""))) {
                if (resultList.size() > 0) {
                    // If the Query has result
                    retrieve = (HashMap) resultList.get(0);
                    if (CommonUtil.convertObjToStr(retrieve.get("SANCTION_NO")).equals(NUMERIC_ZERO)) {
                        save = true;
                    } else {
                        if (getStrRealSanctionNo().equals(strSanctionNo)) {
                            // Don't do anything if Existing and entered Sanction Number are same
                            save = true;
                        } else {
                            int option = -1;
                            String[] options = {objTermLoanRB.getString("cDialogOk")};
                            option = COptionPane.showOptionDialog(null, objTermLoanRB.getString("existanceSanctionNoWarning"), CommonConstants.WARNINGTITLE,
                                    COptionPane.OK_OPTION, COptionPane.WARNING_MESSAGE, null, options, options[0]);
                            save = false;
                        }
                    }
                }
            }
            transactionMap = null;
            resultList = null;
        } catch (Exception e) {
            log.info("Exception caught in checkSanctionNoDublication: " + e);
            parseException.logException(e, true);
        }
        return save;
    }

    public void deleteFacilityRecord(int sanctionSlNo, int slno) {
        try {
            //System.out.println("#$#$# (DELETE) facilityAll : " + facilityAll);
            HashMap removedRecord = new HashMap();
            String strSanctionNo = CommonUtil.convertObjToStr(((ArrayList) tblSanctionMain.getDataArrayList().get(sanctionSlNo)).get(1));
            sanctionSlNo = CommonUtil.convertObjToInt(((ArrayList) tblSanctionMain.getDataArrayList().get(sanctionSlNo)).get(0));
            //            slno       = CommonUtil.convertObjToInt(((ArrayList)tblSanctionFacility.getDataArrayList().get(slno)).get(0));
            String strFacilityKey = getFacilityKey(strSanctionNo, slno);
            if (facilityAll.containsKey(strFacilityKey)) {
                removedRecord = (HashMap) facilityAll.remove(strFacilityKey);
                if (removedRecord.get(COMMAND).equals(UPDATE)) {
                    // Change the status to DELETE if it is existing in Database
                    removedRecord.put(COMMAND, DELETE);
                    removedFaccilityTabValues.put(strFacilityKey, removedRecord);
                }
            }
            removedRecord = null;
            strFacilityKey = null;
            strSanctionNo = null;
        } catch (Exception e) {
            log.info("Exception caught in deleteFacilityRecord: " + e);
            parseException.logException(e, true);
        }
    }

    public void populateCustomerProdLeveFields() {
        try {
            if (termLoanBorrowerOB.getTxtCustID().length() > 0) {
                HashMap transactionMap = new HashMap();
                HashMap retrieve;
                transactionMap.put("CUST_ID", termLoanBorrowerOB.getTxtCustID());
                List resultList = (List) ClientUtil.executeQuery("getCustGroupDesc", transactionMap);
                if (resultList.size() > 0) {
                    retrieve = (HashMap) resultList.get(0);
                    setTxtGroupDesc(CommonUtil.convertObjToStr(retrieve.get(CUSTOMERGROUP)));
                }
                transactionMap = null;
                retrieve = null;
                resultList = null;
            }
        } catch (Exception e) {
            log.info("Exception caught in populateCustomerProdLeveFields: " + e);
            parseException.logException(e, true);
        }
    }

    public void setDefaultValB4AcctCreation() {
        try {
            setCboAccStatus(CommonUtil.convertObjToStr(this.getCbmAccStatus().getDataForKey(NEW)));
            setTdtDemandPromNoteDate(DateUtil.getStringDate(curDate));
            termLoanBorrowerOB.getTxtCustID();
        } catch (Exception e) {
            log.info("Exception caught in setDefaultValB4AcctCreation: " + e);
            parseException.logException(e, true);
        }
    }

    public void sanctionFacilityEditWarning() {
        int option = -1;
        String[] options = {objTermLoanRB.getString("cDialogOk")};
        option = COptionPane.showOptionDialog(null, objTermLoanRB.getString("existanceFacilityEditWarning"), CommonConstants.WARNINGTITLE,
                COptionPane.OK_OPTION, COptionPane.WARNING_MESSAGE, null, options, options[0]);
    }

    public void repaymentExistingWarning() {
        int option = -1;
        String[] options = {objTermLoanRB.getString("cDialogOk")};
        option = COptionPane.showOptionDialog(null, objTermLoanRB.getString("existanceRepaymentWarning"), CommonConstants.WARNINGTITLE,
                COptionPane.OK_OPTION, COptionPane.WARNING_MESSAGE, null, options, options[0]);
    }

    public int interestGetFromValChangeWarn() {
        int option = -1;
        String[] options = {objTermLoanRB.getString("cDialogYes"), objTermLoanRB.getString("cDialogNo")};
        option = COptionPane.showOptionDialog(null, objTermLoanRB.getString("interestGetFromValChangeWarning"), CommonConstants.WARNINGTITLE,
                COptionPane.YES_NO_OPTION, COptionPane.WARNING_MESSAGE, null, options, options[0]);
        return option;
    }

    public int showWarningMsg(String strWarnMsg) {
        int option = -1;
        String[] options = {objTermLoanRB.getString("cDialogOk")};
        option = COptionPane.showOptionDialog(null, objTermLoanRB.getString(strWarnMsg), CommonConstants.WARNINGTITLE,
                COptionPane.OK_OPTION, COptionPane.WARNING_MESSAGE, null, options, options[0]);
        return option;
    }

    public int constitutionCustWarn(String strWarnMsg) {
        return showWarningMsg(strWarnMsg);
    }

    public void existInstallment(HashMap resultMap, boolean uniformPrincipal) throws Exception {
        TableModel tableModel = new TableModel();
        if (uniformPrincipal) {
            setInstallmentTitleForUniformType();
        } else {
            setInstallmentTitle();
        }
        ArrayList heading = installmentTitle;
        ArrayList data = (ArrayList) resultMap.get("TABLEDATA");
        tableModel.setHeading(heading);
        if (data != null) {
            tableModel.setData(data);
            //                    //System.out.println("### Data Size : " + installmentAllTabRecords.size());
        }
        tableModel.fireTableDataChanged();
        CTable tblInstallment = new CTable();
        tblInstallment.setModel(tableModel);
        tblInstallment.revalidate();
        tblInstallment.show();

    }

    /**
     * This method will destroy or nullify the ArrayList, LinkedHashMap,
     * EnhancedTableModel, TableUtil instances
     */
    public void destroyObjects() {
        termLoanBorrowerOB.destroyObjects();
        //        termLoanAuthorizedSignatoryOB.destroyObjects();
        //        termLoanAuthorizedSignatoryInstructionOB.destroyObjects();
        //        termLoanPoAOB.destroyObjects();
        termLoanSecurityOB.destroyObjects();
        termLoanRepaymentOB.destroyObjects();
        termLoanInterestOB.destroyObjects();
//        termLoanGuarantorOB.destroyObjects();
        termLoanDocumentDetailsOB.destroyObjects();
        //        agriSubLimitOB.destroyObjects();
        //        settlementOB.destroyObjects();//dontdelete
        //        agriSubLimitOB.destroyObjects();
        tblSanctionFacility = null;
        tblSanctionMain = null;
        tableUtilSanction_Facility = null;
        tableUtilSanction_Main = null;
        facilityTabSanction = null;
        facilityAll = null;
        sanctionMainAllTabRecords = null;
        advanceLiablityMap = null;
        objSMSSubscriptionTO = null;
        //        actTransOB.resetAcctTransfer();//dontdelete
    }

    /**
     * This method will create instances like ArrayList, LinkedHashMap,
     * EnhancedTableModel, TableUtil
     */
    public void createObject() {
        termLoanBorrowerOB.createObject();
        termLoanSecurityOB.createObject();
        termLoanRepaymentOB.createObject();
        termLoanInterestOB.createObject();
        //        if (loanType.equals("OTHERS")) {
        //        termLoanAuthorizedSignatoryOB.createObject();
        //        termLoanAuthorizedSignatoryInstructionOB.createObject();
        //        termLoanPoAOB.createObject();
//        termLoanGuarantorOB.createObject();
        termLoanDocumentDetailsOB.createObject();
//        termLoanAdditionalSanctionOB.createObject();
        //        }
        tblSanctionFacility = new EnhancedTableModel(null, sanctionFacilityTitle);
        tblSanctionMain = new EnhancedTableModel(null, sanctionMainTitle);
        tblShare = new EnhancedTableModel(null, shareTitle);
        facilityTabSanction = new ArrayList();
        tblSanctionFacility.setDataArrayList(null, sanctionFacilityTitle);
        sanctionMainAllTabRecords = new ArrayList();
        tblSanctionMain.setDataArrayList(null, sanctionMainTitle);
        facilityAll = new LinkedHashMap();
        tableUtilSanction_Facility = new TableUtil();
        tableUtilSanction_Facility.setAttributeKey(SLNO);
        tableUtilSanction_Main = new TableUtil();
        tableUtilSanction_Main.setAttributeKey(SANCTION_SL_NO);
        //        settlementOB.createObject();//dontdelete
    }

    public void destroyCreateSanctionFacilityObjects() {
        sanctionFacilityAllTabRecords = new ArrayList();
        sanctionFacilityAll = new LinkedHashMap();
        tableUtilSanction_Facility.setAllValues(sanctionFacilityAll);
        tableUtilSanction_Facility.setTableValues(sanctionFacilityAllTabRecords);
    }

    public HashMap loanInterestCalculationAsAndWhen(HashMap whereMap) {
        HashMap mapData = new HashMap();
        try {//dont delete this methode check select dao
            List mapDataList = ClientUtil.executeQuery("", whereMap); //, frame);
            mapData = (HashMap) mapDataList.get(0);
        } catch (Exception e) {
            e.printStackTrace();
        }
        //System.out.println("#### MapData :" + mapData);
        return mapData;
    }

    public int getFacilitySize() {
        return facilityAll.size();
    }

    public boolean checkAllfacilityDetails() {
        ArrayList mainSanction = tblSanctionMain.getDataArrayList();
        ArrayList sanctionDetails = tblSanctionFacility.getDataArrayList();
        //System.out.println("mainSanctionArrayList" + mainSanction);
        //System.out.println("sanctionDetails##" + sanctionDetails);
        Set keySet = sanctionMainAll.keySet();
        Object[] objKeySet = (Object[]) keySet.toArray();
        for (int i = 0; i < mainSanction.size(); i++) {
            String strSanctionNumber = CommonUtil.convertObjToStr(((ArrayList) mainSanction.get(i)).get(1));
            int SlNo = CommonUtil.convertObjToInt(((ArrayList) mainSanction.get(i)).get(0));
            for (int j = 0; j < sanctionMainAll.size(); j++) {
                if ((CommonUtil.convertObjToStr(((HashMap) sanctionMainAll.get(objKeySet[j])).get(SANCTION_SL_NO)).equals(String.valueOf(SlNo)))) {
                    HashMap eachRecs = new HashMap();
                    LinkedHashMap sanctionFacilityAlls = new LinkedHashMap();
                    eachRecs = (HashMap) sanctionMainAll.get(objKeySet[j]);
                    sanctionFacilityAlls = (LinkedHashMap) eachRecs.get(SANCTION_FACILITY_ALL);
                    Set sanFacilityKeySet = sanctionFacilityAlls.keySet();
                    Object[] objSanFacilityKeySet = (Object[]) sanFacilityKeySet.toArray();
                    for (int k = 0; k < sanctionFacilityAlls.size(); k++) {
                        int slno = CommonUtil.convertObjToInt(((HashMap) sanctionFacilityAlls.get(objSanFacilityKeySet[k])).get(SLNO));
                        String strFacilityKeys = getFacilityKey(strSanctionNumber, slno);
                        //System.out.println("strFacilitykey " + strFacilityKeys + " SlNo " + SlNo + " strSanctionNumber " + strSanctionNumber);
                        if (!facilityAll.containsKey(strFacilityKeys)) {
                            return false;
                        }
                    }
                }
                //            for(int j=0;j<sanctionDetails.size();j++){
                //            int    SlNo=CommonUtil.convertObjToInt(((ArrayList)sanctionDetails.get(j)).get(0));
            }
        }
        return true;
    }

    public HashMap getLoanChargeDetails(HashMap loanInstall) {
        List chargeList = ClientUtil.executeQuery("getChargeDetails", loanInstall);

//    if(chargeList !=null && chargeList.size()>0){
//                Map otherChargesMap = new HashMap();
//                for(int i=0;i<chargeList.size();i++){
//                    HashMap chargeMap=(HashMap)chargeList.get(i);
//                    
//                    double chargeAmt=CommonUtil.convertObjToDouble(chargeMap.get("CHARGE_AMT")).doubleValue();
//                    if(chargeMap.get("CHARGE_TYPE").equals("POSTAGE CHARGES") && chargeAmt>0 ){
//                        row=new ArrayList();
//                        row.add(transRB.getString("POSTAGE CHARGES"));
//                        row.add(chargeMap.get("CHARGE_AMT"));
//                        totalRecievable += CommonUtil.convertObjToDouble(chargeMap.get("CHARGE_AMT")).doubleValue();
//                        data.add(row);
//                        TermLoanCloseCharge.put("POSTAGE CHARGES",chargeMap.get("CHARGE_AMT"));
//                        calaclatedIntChargeMap.put(transRB.getString("POSTAGE CHARGES"),chargeMap.get("CHARGE_AMT"));
//                    } else if(chargeMap.get("CHARGE_TYPE").equals("MISCELLANEOUS CHARGES")&& chargeAmt>0){
//                        row=new ArrayList();
//                        row.add(transRB.getString("MISCELLANEOUS CHARGES"));
//                        row.add(chargeMap.get("CHARGE_AMT"));
//                        totalRecievable += CommonUtil.convertObjToDouble(chargeMap.get("CHARGE_AMT")).doubleValue();
//                        data.add(row);
//                        TermLoanCloseCharge.put("MISCELLANEOUS CHARGES",chargeMap.get("CHARGE_AMT"));
//                        calaclatedIntChargeMap.put(transRB.getString("MISCELLANEOUS CHARGES"),chargeMap.get("CHARGE_AMT"));
//                    } else if(chargeMap.get("CHARGE_TYPE").equals("LEGAL CHARGES") && chargeAmt>0){
//                        row=new ArrayList();
//                        row.add(transRB.getString("LEGAL CHARGES"));
//                        row.add(chargeMap.get("CHARGE_AMT"));
//                        totalRecievable += CommonUtil.convertObjToDouble(chargeMap.get("CHARGE_AMT")).doubleValue();
//                        data.add(row);
//                        TermLoanCloseCharge.put("LEGAL CHARGES",chargeMap.get("CHARGE_AMT"));
//                        calaclatedIntChargeMap.put(transRB.getString("LEGAL CHARGES"),chargeMap.get("CHARGE_AMT"));
//                    } else if(chargeMap.get("CHARGE_TYPE").equals("INSURANCE CHARGES") && chargeAmt>0){
//                        row=new ArrayList();
//                        row.add(transRB.getString("INSURANCE CHARGES"));
//                        row.add(chargeMap.get("CHARGE_AMT"));
//                        totalRecievable += CommonUtil.convertObjToDouble(chargeMap.get("CHARGE_AMT")).doubleValue();
//                        data.add(row);
//                        TermLoanCloseCharge.put("INSURANCE CHARGES",chargeMap.get("CHARGE_AMT"));
//                        calaclatedIntChargeMap.put(transRB.getString("INSURANCE CHARGES"),chargeMap.get("CHARGE_AMT"));
//                    } else if(chargeMap.get("CHARGE_TYPE").equals("EXECUTION DECREE CHARGES") && chargeAmt>0){
//                        row=new ArrayList();
//                        row.add(transRB.getString("EXECUTION DECREE CHARGES"));
//                        row.add(chargeMap.get("CHARGE_AMT"));
//                        totalRecievable += CommonUtil.convertObjToDouble(chargeMap.get("CHARGE_AMT")).doubleValue();
//                        data.add(row);
//                        TermLoanCloseCharge.put("EXECUTION DECREE CHARGES",chargeMap.get("CHARGE_AMT"));
//                        calaclatedIntChargeMap.put(transRB.getString("EXECUTION DECREE CHARGES"),chargeMap.get("CHARGE_AMT"));
//                    } else if(chargeMap.get("CHARGE_TYPE").equals("ARBITRARY CHARGES") && chargeAmt>0){
//                        row=new ArrayList();
//                        row.add(transRB.getString("ARBITRARY CHARGES"));
//                        row.add(chargeMap.get("CHARGE_AMT"));
//                        totalRecievable += CommonUtil.convertObjToDouble(chargeMap.get("CHARGE_AMT")).doubleValue();
//                        data.add(row);
//                        row=new ArrayList();
//                        TermLoanCloseCharge.put("ARBITRARY CHARGES",chargeMap.get("CHARGE_AMT"));
//                        calaclatedIntChargeMap.put(transRB.getString("ARBITRARY CHARGES"),chargeMap.get("CHARGE_AMT"));
//                    }
//                    
//                    else {
//                        row=new ArrayList();
//                        row.add(chargeMap.get("CHARGE_TYPE"));
//                        row.add(chargeMap.get("CHARGE_AMT"));
//                        totalRecievable += CommonUtil.convertObjToDouble(chargeMap.get("CHARGE_AMT")).doubleValue();
//                        data.add(row);
//                        row=new ArrayList();
//                        otherChargesMap.put(chargeMap.get("CHARGE_TYPE"),chargeMap.get("CHARGE_AMT"));
//                    }
//                }
//                TermLoanCloseCharge.put("OTHER_CHARGES",otherChargesMap);
//                //                calaclatedIntChargeMap.put(chargeMap.get("CHARGE_TYPE"),otherChargesMap);
//            }
        return null;
    }

    public boolean checkAllfacilitySanctionnoUpdateDetails(String newSanctionNo, String sanctionSlNo) {
        ArrayList mainSanction = tblSanctionMain.getDataArrayList();
        ArrayList sanctionDetails = tblSanctionFacility.getDataArrayList();
        //System.out.println("mainSanctionArrayList" + mainSanction);
        //System.out.println("sanctionDetails##" + sanctionDetails);
        boolean isTrue = false;
        Set keySet = sanctionMainAll.keySet();
        Object[] objKeySet = (Object[]) keySet.toArray();
        //        for(int i=0;i<mainSanction.size();i++){
        //            String strSanctionNumber=CommonUtil.convertObjToStr(((ArrayList)mainSanction.get(i)).get(1));
        String strSanctionNumber = oldSanction_no;
        int SlNo = CommonUtil.convertObjToInt(sanctionSlNo);//((ArrayList)mainSanction.get(i)).get(0));
        for (int j = 0; j < sanctionMainAll.size(); j++) {
            if ((CommonUtil.convertObjToStr(((HashMap) sanctionMainAll.get(objKeySet[j])).get(SANCTION_SL_NO)).equals(String.valueOf(SlNo)))) {
                HashMap eachRecs = new HashMap();
                LinkedHashMap sanctionFacilityAlls = new LinkedHashMap();
                eachRecs = (HashMap) sanctionMainAll.get(objKeySet[j]);
                sanctionFacilityAlls = (LinkedHashMap) eachRecs.get(SANCTION_FACILITY_ALL);
                Set sanFacilityKeySet = sanctionFacilityAlls.keySet();
                Object[] objSanFacilityKeySet = (Object[]) sanFacilityKeySet.toArray();
                for (int k = 0; k < sanctionFacilityAlls.size(); k++) {
                    int slno = CommonUtil.convertObjToInt(((HashMap) sanctionFacilityAlls.get(objSanFacilityKeySet[k])).get(SLNO));
                    String strFacilityKeys = getFacilityKey(strSanctionNumber, slno);
                    String strFacilitySetnewKeys = getFacilityKey(newSanctionNo, slno);
                    //System.out.println("strFacilitykey " + strFacilityKeys + " SlNo " + SlNo + " strSanctionNumber " + strSanctionNumber);
                    if (facilityAll.containsKey(strFacilityKeys)) {
                        facilityAll.put(strFacilitySetnewKeys, facilityAll.get(strFacilityKeys));
                        if (!strFacilitySetnewKeys.equals(strFacilityKeys)) {
                            facilityAll.remove(strFacilityKeys);
                        }
                        isTrue = true;
                        //                            return false;
                    }
                }
                //                }
                //            for(int j=0;j<sanctionDetails.size();j++){
                //            int    SlNo=CommonUtil.convertObjToInt(((ArrayList)sanctionDetails.get(j)).get(0));
            }
        }
        return isTrue;
    }

    public void loadingDocumentDetails() {
        HashMap documentMap = new HashMap();
        String prodId = "GL";
        documentMap.put("PROD_ID", prodId);
        List lst = ClientUtil.executeQuery("getSelectProductLevelDocDetails", documentMap);
        if (lst != null && lst.size() > 0) {
            documentMap.put("TermLoanDocumentTO", lst);
            //System.out.println("documentMap :" + documentMap);
            termLoanDocumentDetailsOB.resetDocumentDetails();
            termLoanDocumentDetailsOB.getTblDocumentTab().setDataArrayList(null, termLoanDocumentDetailsOB.getDocumentTabTitle());
            termLoanDocumentDetailsOB.setTermLoanDocumentTO((ArrayList) (documentMap.get("TermLoanDocumentTO")));
        }
    }

    /**
     * Getter for property cbmIntGetFrom.
     *
     * @return Value of property cbmIntGetFrom.
     */
    public com.see.truetransact.clientutil.ComboBoxModel getCbmIntGetFrom() {
        return cbmIntGetFrom;
    }

    /**
     * Setter for property cbmIntGetFrom.
     *
     * @param cbmIntGetFrom New value of property cbmIntGetFrom.
     */
    public void setCbmIntGetFrom(com.see.truetransact.clientutil.ComboBoxModel cbmIntGetFrom) {
        this.cbmIntGetFrom = cbmIntGetFrom;
    }

    /**
     * Getter for property cboIntGetFrom.
     *
     * @return Value of property cboIntGetFrom.
     */
    public java.lang.String getCboIntGetFrom() {
        return cboIntGetFrom;
    }

    /**
     * Setter for property cboIntGetFrom.
     *
     * @param cboIntGetFrom New value of property cboIntGetFrom.
     */
    public void setCboIntGetFrom(java.lang.String cboIntGetFrom) {
        this.cboIntGetFrom = cboIntGetFrom;
    }

    /**
     * Getter for property txtPeriodDifference_Days.
     *
     * @return Value of property txtPeriodDifference_Days.
     */
    public java.lang.String getTxtPeriodDifference_Days() {
        return txtPeriodDifference_Days;
    }

    /**
     * Setter for property txtPeriodDifference_Days.
     *
     * @param txtPeriodDifference_Days New value of property
     * txtPeriodDifference_Days.
     */
    public void setTxtPeriodDifference_Days(java.lang.String txtPeriodDifference_Days) {
        this.txtPeriodDifference_Days = txtPeriodDifference_Days;
    }

    /**
     * Getter for property txtPeriodDifference_Months.
     *
     * @return Value of property txtPeriodDifference_Months.
     */
    public java.lang.String getTxtPeriodDifference_Months() {
        return txtPeriodDifference_Months;
    }

    /**
     * Setter for property txtPeriodDifference_Months.
     *
     * @param txtPeriodDifference_Months New value of property
     * txtPeriodDifference_Months.
     */
    public void setTxtPeriodDifference_Months(java.lang.String txtPeriodDifference_Months) {
        this.txtPeriodDifference_Months = txtPeriodDifference_Months;
    }

    /**
     * Getter for property txtPeriodDifference_Years.
     *
     * @return Value of property txtPeriodDifference_Years.
     */
    public java.lang.String getTxtPeriodDifference_Years() {
        return txtPeriodDifference_Years;
    }

    /**
     * Setter for property txtPeriodDifference_Years.
     *
     * @param txtPeriodDifference_Years New value of property
     * txtPeriodDifference_Years.
     */
    public void setTxtPeriodDifference_Years(java.lang.String txtPeriodDifference_Years) {
        this.txtPeriodDifference_Years = txtPeriodDifference_Years;
    }

    /**
     * Getter for property cbmRepayFreq_ADVANCE.
     *
     * @return Value of property cbmRepayFreq_ADVANCE.
     */
    public com.see.truetransact.clientutil.ComboBoxModel getCbmRepayFreq_ADVANCE() {
        return cbmRepayFreq_ADVANCE;
    }

    /**
     * Setter for property cbmRepayFreq_ADVANCE.
     *
     * @param cbmRepayFreq_PROD New value of property cbmRepayFreq_ADVANCE.
     */
    public void setCbmRepayFreq_ADVANCE(com.see.truetransact.clientutil.ComboBoxModel cbmRepayFreq_ADVANCE) {
        this.cbmRepayFreq_ADVANCE = cbmRepayFreq_ADVANCE;
    }

    /**
     * Getter for property cbmRepayFreq_LOAN.
     *
     * @return Value of property cbmRepayFreq_LOAN.
     */
    public com.see.truetransact.clientutil.ComboBoxModel getCbmRepayFreq_LOAN() {
        return cbmRepayFreq_LOAN;
    }

    /**
     * Setter for property cbmRepayFreq_LOAN.
     *
     * @param cbmRepayFreq_LOAN New value of property cbmRepayFreq_LOAN.
     */
    public void setCbmRepayFreq_LOAN(com.see.truetransact.clientutil.ComboBoxModel cbmRepayFreq_LOAN) {
        this.cbmRepayFreq_LOAN = cbmRepayFreq_LOAN;
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
     * Getter for property txtAcct_Name.
     *
     * @return Value of property txtAcct_Name.
     */
    public java.lang.String getTxtAcct_Name() {
        return txtAcct_Name;
    }

    /**
     * Setter for property txtAcct_Name.
     *
     * @param txtAcct_Name New value of property txtAcct_Name.
     */
    public void setTxtAcct_Name(java.lang.String txtAcct_Name) {
        this.txtAcct_Name = txtAcct_Name;
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
     * Getter for property lienChanged.
     *
     * @return Value of property lienChanged.
     */
    public boolean isLienChanged() {
        return lienChanged;
    }

    /**
     * Setter for property lienChanged.
     *
     * @param lienChanged New value of property lienChanged.
     */
    public void setLienChanged(boolean lienChanged) {
        this.lienChanged = lienChanged;
    }

    /**
     * Getter for property loanACNo.
     *
     * @return Value of property loanACNo.
     */
    public java.lang.String getLoanACNo() {
        return loanACNo;
    }

    /**
     * Setter for property loanACNo.
     *
     * @param loanACNo New value of property loanACNo.
     */
    public void setLoanACNo(java.lang.String loanACNo) {
        this.loanACNo = loanACNo;
    }

    /**
     * Getter for property depositCustDetMap.
     *
     * @return Value of property depositCustDetMap.
     */
    public java.util.HashMap getDepositCustDetMap() {
        return depositCustDetMap;
    }

    /**
     * Setter for property depositCustDetMap.
     *
     * @param depositCustDetMap New value of property depositCustDetMap.
     */
    public void setDepositCustDetMap(java.util.HashMap depositCustDetMap) {
        this.depositCustDetMap = depositCustDetMap;
    }

    /**
     * Getter for property depositNo.
     *
     * @return Value of property depositNo.
     */
    public java.lang.String getDepositNo() {
        return depositNo;
    }

    /**
     * Setter for property depositNo.
     *
     * @param depositNo New value of property depositNo.
     */
    public void setDepositNo(java.lang.String depositNo) {
        this.depositNo = depositNo;
        termLoanInterestOB.setDepositNo("");
        termLoanInterestOB.setDepositNo(depositNo);

    }

    /**
     * Getter for property deleteAction.
     *
     * @return Value of property deleteAction.
     */
    public java.lang.String getDeleteAction() {
        return deleteAction;
    }

    /**
     * Setter for property deleteAction.
     *
     * @param deleteAction New value of property deleteAction.
     */
    public void setDeleteAction(java.lang.String deleteAction) {
        this.deleteAction = deleteAction;
    }

    /**
     * Getter for property ProductFacilityType.
     *
     * @return Value of property ProductFacilityType.
     */
    public java.lang.String getProductFacilityType() {
        return ProductFacilityType;
    }

    /**
     * Setter for property ProductFacilityType.
     *
     * @param ProductFacilityType New value of property ProductFacilityType.
     */
    public void setProductFacilityType(java.lang.String ProductFacilityType) {
        this.ProductFacilityType = ProductFacilityType;
    }

    /**
     * Getter for property cbmRecommendedByType.
     *
     * @return Value of property cbmRecommendedByType.
     */
    public com.see.truetransact.clientutil.ComboBoxModel getCbmRecommendedByType() {
        return cbmRecommendedByType;
    }

    /**
     * Setter for property cbmRecommendedByType.
     *
     * @param cbmRecommendedByType New value of property cbmRecommendedByType.
     */
    public void setCbmRecommendedByType(com.see.truetransact.clientutil.ComboBoxModel cbmRecommendedByType) {
        this.cbmRecommendedByType = cbmRecommendedByType;
    }

    /**
     * Getter for property cboRecommendedByType.
     *
     * @return Value of property cboRecommendedByType.
     */
    public java.lang.String getCboRecommendedByType() {
        return cboRecommendedByType;
    }

    /**
     * Setter for property cboRecommendedByType.
     *
     * @param cboRecommendedByType New value of property cboRecommendedByType.
     */
    public void setCboRecommendedByType(java.lang.String cboRecommendedByType) {
        this.cboRecommendedByType = cboRecommendedByType;
    }

    /**
     * Getter for property AccountOpenDate.
     *
     * @return Value of property AccountOpenDate.
     */
    public java.lang.String getAccountOpenDate() {
        return AccountOpenDate;
    }

    /**
     * Setter for property AccountOpenDate.
     *
     * @param AccountOpenDate New value of property AccountOpenDate.
     */
    public void setAccountOpenDate(java.lang.String AccountOpenDate) {
        this.AccountOpenDate = AccountOpenDate;
    }

    /**
     * Getter for property tblShare.
     *
     * @return Value of property tblShare.
     */
    public com.see.truetransact.clientutil.EnhancedTableModel getTblShare() {
        return tblShare;
    }

    /**
     * Setter for property tblShare.
     *
     * @param tblShare New value of property tblShare.
     */
    public void setTblShare(com.see.truetransact.clientutil.EnhancedTableModel tblShare) {
        this.tblShare = tblShare;
        setChanged();
    }

    /**
     * Getter for property BEHAVES_LIKE.
     *
     * @return Value of property BEHAVES_LIKE.
     */
    public java.lang.String getBEHAVES_LIKE() {
        return BEHAVES_LIKE;
    }

    /**
     * Setter for property BEHAVES_LIKE.
     *
     * @param BEHAVES_LIKE New value of property BEHAVES_LIKE.
     */
    public void setBEHAVES_LIKE(java.lang.String BEHAVES_LIKE) {
        this.BEHAVES_LIKE = BEHAVES_LIKE;
    }

    /**
     * Getter for property renewAvailableBalance.
     *
     * @return Value of property renewAvailableBalance.
     */
    public java.lang.String getRenewAvailableBalance() {
        return renewAvailableBalance;
    }

    /**
     * Setter for property renewAvailableBalance.
     *
     * @param renewAvailableBalance New value of property renewAvailableBalance.
     */
    public void setRenewAvailableBalance(java.lang.String renewAvailableBalance) {
        this.renewAvailableBalance = renewAvailableBalance;
    }

    /**
     * Getter for property updateAvailableBalance.
     *
     * @return Value of property updateAvailableBalance.
     */
    public boolean isUpdateAvailableBalance() {
        return updateAvailableBalance;
    }

    /**
     * Setter for property updateAvailableBalance.
     *
     * @param updateAvailableBalance New value of property
     * updateAvailableBalance.
     */
    public void setUpdateAvailableBalance(boolean updateAvailableBalance) {
        this.updateAvailableBalance = updateAvailableBalance;
    }

    /**
     * Getter for property advanceLiablityMap.
     *
     * @return Value of property advanceLiablityMap.
     */
    public java.util.HashMap getAdvanceLiablityMap() {
        return advanceLiablityMap;
    }

    /**
     * Setter for property advanceLiablityMap.
     *
     * @param advanceLiablityMap New value of property advanceLiablityMap.
     */
    public void setAdvanceLiablityMap(java.util.HashMap advanceLiablityMap) {
        this.advanceLiablityMap = advanceLiablityMap;
    }

    /**
     * Getter for property oldSanction_no.
     *
     * @return Value of property oldSanction_no.
     */
    public java.lang.String getOldSanction_no() {
        return oldSanction_no;
    }

    /**
     * Setter for property oldSanction_no.
     *
     * @param oldSanction_no New value of property oldSanction_no.
     */
    public void setOldSanction_no(java.lang.String oldSanction_no) {
        this.oldSanction_no = oldSanction_no;
    }

    /**
     * Getter for property lblDepositNo.
     *
     * @return Value of property lblDepositNo.
     */
    public java.lang.String getLblDepositNo() {
        return lblDepositNo;
    }

    /**
     * Setter for property lblDepositNo.
     *
     * @param lblDepositNo New value of property lblDepositNo.
     */
    public void setLblDepositNo(java.lang.String lblDepositNo) {
        this.lblDepositNo = lblDepositNo;
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
     * Getter for property shadowDebit.
     *
     * @return Value of property shadowDebit.
     */
    public double getShadowDebit() {
        return shadowDebit;
    }

    /**
     * Setter for property shadowDebit.
     *
     * @param shadowDebit New value of property shadowDebit.
     */
    public void setShadowDebit(double shadowDebit) {
        this.shadowDebit = shadowDebit;
    }

    /**
     * Getter for property shadowCredit.
     *
     * @return Value of property shadowCredit.
     */
    public double getShadowCredit() {
        return shadowCredit;
    }

    /**
     * Setter for property shadowCredit.
     *
     * @param shadowCredit New value of property shadowCredit.
     */
    public void setShadowCredit(double shadowCredit) {
        this.shadowCredit = shadowCredit;
    }

    /**
     * Getter for property clearBalance.
     *
     * @return Value of property clearBalance.
     */
    public double getClearBalance() {
        return clearBalance;
    }

    /**
     * Setter for property clearBalance.
     *
     * @param clearBalance New value of property clearBalance.
     */
    public void setClearBalance(double clearBalance) {
        this.clearBalance = clearBalance;
    }

    /**
     * Getter for property partReject.
     *
     * @return Value of property partReject.
     */
    public java.lang.String getPartReject() {
        return partReject;
    }

    /**
     * Setter for property partReject.
     *
     * @param partReject New value of property partReject.
     */
    public void setPartReject(java.lang.String partReject) {
        this.partReject = partReject;
    }

    /**
     * Getter for property rdoDP__NO.
     *
     * @return Value of property rdoDP__NO.
     */
    public boolean isRdoDP__NO() {
        return rdoDP__NO;
    }

    /**
     * Setter for property rdoDP__NO.
     *
     * @param rdoDP__NO New value of property rdoDP__NO.
     */
    public void setRdoDP__NO(boolean rdoDP__NO) {
        this.rdoDP__NO = rdoDP__NO;
    }

    /**
     * Getter for property rdoDP_YES.
     *
     * @return Value of property rdoDP_YES.
     */
    public boolean isRdoDP_YES() {
        return rdoDP_YES;
    }

    /**
     * Setter for property rdoDP_YES.
     *
     * @param rdoDP_YES New value of property rdoDP_YES.
     */
    public void setRdoDP_YES(boolean rdoDP_YES) {
        this.rdoDP_YES = rdoDP_YES;
    }

    /**
     * Getter for property chkDocDetails.
     *
     * @return Value of property chkDocDetails.
     */
    public boolean isChkDocDetails() {
        return chkDocDetails;
    }

    /**
     * Setter for property chkDocDetails.
     *
     * @param chkDocDetails New value of property chkDocDetails.
     */
    public void setChkDocDetails(boolean chkDocDetails) {
        this.chkDocDetails = chkDocDetails;
    }

    /**
     * Getter for property chkAuthorizedSignatory.
     *
     * @return Value of property chkAuthorizedSignatory.
     */
    public boolean isChkAuthorizedSignatory() {
        return chkAuthorizedSignatory;
    }

    /**
     * Setter for property chkAuthorizedSignatory.
     *
     * @param chkAuthorizedSignatory New value of property
     * chkAuthorizedSignatory.
     */
    public void setChkAuthorizedSignatory(boolean chkAuthorizedSignatory) {
        this.chkAuthorizedSignatory = chkAuthorizedSignatory;
    }

    /**
     * Getter for property chkPOFAttorney.
     *
     * @return Value of property chkPOFAttorney.
     */
    public boolean isChkPOFAttorney() {
        return chkPOFAttorney;
    }

    /**
     * Setter for property chkPOFAttorney.
     *
     * @param chkPOFAttorney New value of property chkPOFAttorney.
     */
    //    public void setChkPOFAttorney(boolean chkPOFAttorney) {
    //        this.chkPOFAttorney = chkPOFAttorney;
    //    }
    //    public int getAuthorizeSigantoryRecord(){
    //        return termLoanAuthorizedSignatoryOB.getTblAuthorized().getDataArrayList().size();
    //    }
    //    public int getPOARecord(){
    //        return termLoanPoAOB.getTblPoA().getDataArrayList().size();
    //    }
    //    public int getActTransferMadatoryCheck(){
    //        return termLoanPoAOB.getTblPoA().getDataArrayList().size();
    //    }
    /**
     * Getter for property last_int_calc_dt.
     *
     * @return Value of property last_int_calc_dt.
     */
    public java.util.Date getLast_int_calc_dt() {
        return last_int_calc_dt;
    }

    /**
     * Setter for property last_int_calc_dt.
     *
     * @param last_int_calc_dt New value of property last_int_calc_dt.
     */
    public void setLast_int_calc_dt(java.util.Date last_int_calc_dt) {
        this.last_int_calc_dt = last_int_calc_dt;
    }

    /**
     * Getter for property accountCloseDate.
     *
     * @return Value of property accountCloseDate.
     */
    public java.lang.String getAccountCloseDate() {
        return accountCloseDate;
    }

    /**
     * Setter for property accountCloseDate.
     *
     * @param accountCloseDate New value of property accountCloseDate.
     */
    public void setAccountCloseDate(java.lang.String accountCloseDate) {
        this.accountCloseDate = accountCloseDate;
    }

    /**
     * Getter for property txtLimit_SDMoneyDeposit.
     *
     * @return Value of property txtLimit_SDMoneyDeposit.
     */
    public java.lang.String getTxtLimit_SDMoneyDeposit() {
        return txtLimit_SDMoneyDeposit;
    }

    /**
     * Setter for property txtLimit_SDMoneyDeposit.
     *
     * @param txtLimit_SDMoneyDeposit New value of property
     * txtLimit_SDMoneyDeposit.
     */
    public void setTxtLimit_SDMoneyDeposit(java.lang.String txtLimit_SDMoneyDeposit) {
        this.txtLimit_SDMoneyDeposit = txtLimit_SDMoneyDeposit;
    }

    /**
     * Getter for property chkAcctTransfer.
     *
     * @return Value of property chkAcctTransfer.
     */
    public boolean getChkAcctTransfer() {
        return chkAcctTransfer;
    }

    /**
     * Setter for property chkAcctTransfer.
     *
     * @param chkAcctTransfer New value of property chkAcctTransfer.
     */
    public void setChkAcctTransfer(boolean chkAcctTransfer) {
        this.chkAcctTransfer = chkAcctTransfer;
    }

    /**
     * Getter for property availableBalance.
     *
     * @return Value of property availableBalance.
     */
    public double getAvailableBalance() {
        return availableBalance;
    }

    /**
     * Setter for property availableBalance.
     *
     * @param availableBalance New value of property availableBalance.
     */
    public void setAvailableBalance(double availableBalance) {
        this.availableBalance = availableBalance;
    }

    /**
     * Getter for property interest.
     *
     * @return Value of property interest.
     */
    public java.lang.String getInterest() {
        return interest;
    }

    /**
     * Setter for property interest.
     *
     * @param interest New value of property interest.
     */
    public void setInterest(java.lang.String interest) {
        this.interest = interest;
    }

    /**
     * Getter for property penalInterest.
     *
     * @return Value of property penalInterest.
     */
    public java.lang.String getPenalInterest() {
        return penalInterest;
    }

    /**
     * Setter for property penalInterest.
     *
     * @param penalInterest New value of property penalInterest.
     */
    public void setPenalInterest(java.lang.String penalInterest) {
        this.penalInterest = penalInterest;
    }

    /**
     * Getter for property cboPurposeOfLoan.
     *
     * @return Value of property cboPurposeOfLoan.
     */
    public java.lang.String getCboPurposeOfLoan() {
        return cboPurposeOfLoan;
    }

    /**
     * Setter for property cboPurposeOfLoan.
     *
     * @param cboPurposeOfLoan New value of property cboPurposeOfLoan.
     */
    public void setCboPurposeOfLoan(java.lang.String cboPurposeOfLoan) {
        this.cboPurposeOfLoan = cboPurposeOfLoan;
    }

    /**
     * Getter for property cbmPurposeOfLoan.
     *
     * @return Value of property cbmPurposeOfLoan.
     */
    public com.see.truetransact.clientutil.ComboBoxModel getCbmPurposeOfLoan() {
        return cbmPurposeOfLoan;
    }

    /**
     * Setter for property cbmPurposeOfLoan.
     *
     * @param cbmPurposeOfLoan New value of property cbmPurposeOfLoan.
     */
    public void setCbmPurposeOfLoan(com.see.truetransact.clientutil.ComboBoxModel cbmPurposeOfLoan) {
        this.cbmPurposeOfLoan = cbmPurposeOfLoan;
    }

    /**
     * Getter for property securityAccHeadValue.
     *
     * @return Value of property securityAccHeadValue.
     */
    public java.lang.String getSecurityAccHeadValue() {
        return securityAccHeadValue;
    }

    /**
     * Setter for property securityAccHeadValue.
     *
     * @param securityAccHeadValue New value of property securityAccHeadValue.
     */
    public void setSecurityAccHeadValue(java.lang.String securityAccHeadValue) {
        this.securityAccHeadValue = securityAccHeadValue;
    }

    /**
     * Getter for property installmentAllMap.
     *
     * @return Value of property installmentAllMap.
     */
    public java.util.HashMap getInstallmentAllMap() {
        return installmentAllMap;
    }

    /**
     * Setter for property installmentAllMap.
     *
     * @param installmentAllMap New value of property installmentAllMap.
     */
    public void setInstallmentAllMap(java.util.HashMap installmentAllMap) {
        this.installmentAllMap = installmentAllMap;
    }

    /**
     * Getter for property loadingProdId.
     *
     * @return Value of property loadingProdId.
     */
    public java.lang.String getLoadingProdId() {
        return loadingProdId;
    }

    /**
     * Setter for property loadingProdId.
     *
     * @param loadingProdId New value of property loadingProdId.
     */
    public void setLoadingProdId(java.lang.String loadingProdId) {
        this.loadingProdId = loadingProdId;
    }

    /**
     * Getter for property newCustIdNo.
     *
     * @return Value of property newCustIdNo.
     */
    public java.lang.String getNewCustIdNo() {
        return newCustIdNo;
    }

    /**
     * Setter for property newCustIdNo.
     *
     * @param newCustIdNo New value of property newCustIdNo.
     */
    public void setNewCustIdNo(java.lang.String newCustIdNo) {
        this.newCustIdNo = newCustIdNo;
    }

    /**
     * Getter for property authorizeDt.
     *
     * @return Value of property authorizeDt.
     */
    public java.lang.String getAuthorizeDt() {
        return authorizeDt;
    }

    /**
     * Setter for property authorizeDt.
     *
     * @param authorizeDt New value of property authorizeDt.
     */
    public void setAuthorizeDt(java.lang.String authorizeDt) {
        this.authorizeDt = authorizeDt;
    }

    /**
     * Getter for property transactionMap.
     *
     * @return Value of property transactionMap.
     */
    public java.util.HashMap getTransactionMap() {
        return transactionMap;
    }

    /**
     * Setter for property transactionMap.
     *
     * @param transactionMap New value of property transactionMap.
     */
    public void setTransactionMap(java.util.HashMap transactionMap) {
        this.transactionMap = transactionMap;
    }

    /**
     * Getter for property chargelst.
     *
     * @return Value of property chargelst.
     */
    public java.util.List getChargelst() {
        return chargelst;
    }

    /**
     * Setter for property chargelst.
     *
     * @param chargelst New value of property chargelst.
     */
    public void setChargelst(java.util.List chargelst) {
        this.chargelst = chargelst;
    }

    /**
     * Getter for property cboAppraiserId.
     *
     * @return Value of property cboAppraiserId.
     */
    public java.lang.String getCboAppraiserId() {
        return cboAppraiserId;
    }

    /**
     * Setter for property cboAppraiserId.
     *
     * @param cboAppraiserId New value of property cboAppraiserId.
     */
    public void setCboAppraiserId(java.lang.String cboAppraiserId) {
        this.cboAppraiserId = cboAppraiserId;
    }

    /**
     * Getter for property cbmAppraiserId.
     *
     * @return Value of property cbmAppraiserId.
     */
    public com.see.truetransact.clientutil.ComboBoxModel getCbmAppraiserId() {
        return cbmAppraiserId;
    }

    /**
     * Setter for property cbmAppraiserId.
     *
     * @param cbmAppraiserId New value of property cbmAppraiserId.
     */
    public void setCbmAppraiserId(com.see.truetransact.clientutil.ComboBoxModel cbmAppraiserId) {
        this.cbmAppraiserId = cbmAppraiserId;
    }

    /**
     * Getter for property txtNomineeNameNO.
     *
     * @return Value of property txtNomineeNameNO.
     */
    public java.lang.String getTxtNomineeNameNO() {
        return txtNomineeNameNO;
    }

    /**
     * Setter for property txtNomineeNameNO.
     *
     * @param txtNomineeNameNO New value of property txtNomineeNameNO.
     */
    public void setTxtNomineeNameNO(java.lang.String txtNomineeNameNO) {
        this.txtNomineeNameNO = txtNomineeNameNO;
    }

    /**
     * Getter for property cboNomineeRelationNO.
     *
     * @return Value of property cboNomineeRelationNO.
     */
    public java.lang.String getCboNomineeRelationNO() {
        return cboNomineeRelationNO;
    }

    /**
     * Setter for property cboNomineeRelationNO.
     *
     * @param cboNomineeRelationNO New value of property cboNomineeRelationNO.
     */
    public void setCboNomineeRelationNO(java.lang.String cboNomineeRelationNO) {
        this.cboNomineeRelationNO = cboNomineeRelationNO;
    }

    /**
     * Getter for property txtNomineePhoneNO.
     *
     * @return Value of property txtNomineePhoneNO.
     */
    public java.lang.String getTxtNomineePhoneNO() {
        return txtNomineePhoneNO;
    }

    /**
     * Setter for property txtNomineePhoneNO.
     *
     * @param txtNomineePhoneNO New value of property txtNomineePhoneNO.
     */
    public void setTxtNomineePhoneNO(java.lang.String txtNomineePhoneNO) {
        this.txtNomineePhoneNO = txtNomineePhoneNO;
    }

    /**
     * Getter for property txtNomineeACodeNO.
     *
     * @return Value of property txtNomineeACodeNO.
     */
    public java.lang.String getTxtNomineeACodeNO() {
        return txtNomineeACodeNO;
    }

    /**
     * Setter for property txtNomineeACodeNO.
     *
     * @param txtNomineeACodeNO New value of property txtNomineeACodeNO.
     */
    public void setTxtNomineeACodeNO(java.lang.String txtNomineeACodeNO) {
        this.txtNomineeACodeNO = txtNomineeACodeNO;
    }

    /**
     * Getter for property txtNomineeShareNO.
     *
     * @return Value of property txtNomineeShareNO.
     */
    public java.lang.String getTxtNomineeShareNO() {
        return txtNomineeShareNO;
    }

    /**
     * Setter for property txtNomineeShareNO.
     *
     * @param txtNomineeShareNO New value of property txtNomineeShareNO.
     */
    public void setTxtNomineeShareNO(java.lang.String txtNomineeShareNO) {
        this.txtNomineeShareNO = txtNomineeShareNO;
    }

    /**
     * Getter for property cbmNomineeRelationNO.
     *
     * @return Value of property cbmNomineeRelationNO.
     */
    public com.see.truetransact.clientutil.ComboBoxModel getCbmNomineeRelationNO() {
        return cbmNomineeRelationNO;
    }

    /**
     * Setter for property cbmNomineeRelationNO.
     *
     * @param cbmNomineeRelationNO New value of property cbmNomineeRelationNO.
     */
    public void setCbmNomineeRelationNO(com.see.truetransact.clientutil.ComboBoxModel cbmNomineeRelationNO) {
        this.cbmNomineeRelationNO = cbmNomineeRelationNO;
    }

    /**
     * Getter for property termLoanRepaymentOB.
     *
     * @return Value of property termLoanRepaymentOB.
     */
    public com.see.truetransact.ui.termloan.GoldLoanRepaymentOB getTermLoanRepaymentOB() {
        return termLoanRepaymentOB;
    }

    /**
     * Setter for property termLoanRepaymentOB.
     *
     * @param termLoanRepaymentOB New value of property termLoanRepaymentOB.
     */
    public void setTermLoanRepaymentOB(com.see.truetransact.ui.termloan.GoldLoanRepaymentOB termLoanRepaymentOB) {
        this.termLoanRepaymentOB = termLoanRepaymentOB;
    }

    /**
     * Getter for property termLoanClassificationOB.
     *
     * @return Value of property termLoanClassificationOB.
     */
    public com.see.truetransact.ui.termloan.GoldLoanClassificationOB getTermLoanClassificationOB() {
        return termLoanClassificationOB;
    }

    /**
     * Setter for property termLoanClassificationOB.
     *
     * @param termLoanClassificationOB New value of property
     * termLoanClassificationOB.
     */
    public void setTermLoanClassificationOB(com.see.truetransact.ui.termloan.GoldLoanClassificationOB termLoanClassificationOB) {
        this.termLoanClassificationOB = termLoanClassificationOB;
    }

    /**
     * Getter for property termLoanBorrowerOB.
     *
     * @return Value of property termLoanBorrowerOB.
     */
    public com.see.truetransact.ui.termloan.GoldLoanBorrowerOB getTermLoanBorrowerOB() {
        return termLoanBorrowerOB;
    }

    /**
     * Setter for property termLoanBorrowerOB.
     *
     * @param termLoanBorrowerOB New value of property termLoanBorrowerOB.
     */
    public void setTermLoanBorrowerOB(com.see.truetransact.ui.termloan.GoldLoanBorrowerOB termLoanBorrowerOB) {
        this.termLoanBorrowerOB = termLoanBorrowerOB;
    }

//    /**
//     * Getter for property termLoanCompanyOB.
//     * @return Value of property termLoanCompanyOB.
//     */
//    public com.see.truetransact.ui.termloan.TermLoanCompanyOB getTermLoanCompanyOB() {
//        return termLoanCompanyOB;
//    }
//    
//    /**
//     * Setter for property termLoanCompanyOB.
//     * @param termLoanCompanyOB New value of property termLoanCompanyOB.
//     */
//    public void setTermLoanCompanyOB(com.see.truetransact.ui.termloan.TermLoanCompanyOB termLoanCompanyOB) {
//        this.termLoanCompanyOB = termLoanCompanyOB;
//    }
//    /**
//     * Getter for property termLoanGuarantorOB.
//     * @return Value of property termLoanGuarantorOB.
//     */
//    public com.see.truetransact.ui.termloan.TermLoanGuarantorOB getTermLoanGuarantorOB() {
//        return termLoanGuarantorOB;
//    }
//    
//    /**
//     * Setter for property termLoanGuarantorOB.
//     * @param termLoanGuarantorOB New value of property termLoanGuarantorOB.
//     */
//    public void setTermLoanGuarantorOB(com.see.truetransact.ui.termloan.TermLoanGuarantorOB termLoanGuarantorOB) {
//        this.termLoanGuarantorOB = termLoanGuarantorOB;
//    }
    /**
     * Getter for property termLoanInterestOB.
     *
     * @return Value of property termLoanInterestOB.
     */
    public com.see.truetransact.ui.termloan.GoldLoanInterestOB getTermLoanInterestOB() {
        return termLoanInterestOB;
    }

    /**
     * Setter for property termLoanInterestOB.
     *
     * @param termLoanInterestOB New value of property termLoanInterestOB.
     */
    public void setTermLoanInterestOB(com.see.truetransact.ui.termloan.GoldLoanInterestOB termLoanInterestOB) {
        this.termLoanInterestOB = termLoanInterestOB;
    }

//    /**
//     * Getter for property termLoanOtherDetailsOB.
//     * @return Value of property termLoanOtherDetailsOB.
//     */
//    public com.see.truetransact.ui.termloan.TermLoanOtherDetailsOB getTermLoanOtherDetailsOB() {
//        return termLoanOtherDetailsOB;
//    }
//    
//    /**
//     * Setter for property termLoanOtherDetailsOB.
//     * @param termLoanOtherDetailsOB New value of property termLoanOtherDetailsOB.
//     */
//    public void setTermLoanOtherDetailsOB(com.see.truetransact.ui.termloan.TermLoanOtherDetailsOB termLoanOtherDetailsOB) {
//        this.termLoanOtherDetailsOB = termLoanOtherDetailsOB;
//    }
//    
    /**
     *
     *
     * /**
     * Getter for property termLoanSecurityOB.
     *
     * @return Value of property termLoanSecurityOB.
     */
    public com.see.truetransact.ui.termloan.GoldLoanSecurityOB getTermLoanSecurityOB() {
        return termLoanSecurityOB;
    }

    /**
     * Setter for property termLoanSecurityOB.
     *
     * @param termLoanSecurityOB New value of property termLoanSecurityOB.
     */
    public void setTermLoanSecurityOB(com.see.truetransact.ui.termloan.GoldLoanSecurityOB termLoanSecurityOB) {
        this.termLoanSecurityOB = termLoanSecurityOB;
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
     * Getter for property renewalAcctNum.
     *
     * @return Value of property renewalAcctNum.
     */
    public java.lang.String getRenewalAcctNum() {
        return renewalAcctNum;
    }

    /**
     * Setter for property renewalAcctNum.
     *
     * @param renewalAcctNum New value of property renewalAcctNum.
     */
    public void setRenewalAcctNum(java.lang.String renewalAcctNum) {
        this.renewalAcctNum = renewalAcctNum;
    }

    /**
     * Getter for property cboRenewalSanctioningAuthority.
     *
     * @return Value of property cboRenewalSanctioningAuthority.
     */
    public java.lang.String getCboRenewalSanctioningAuthority() {
        return cboRenewalSanctioningAuthority;
    }

    /**
     * Setter for property cboRenewalSanctioningAuthority.
     *
     * @param cboRenewalSanctioningAuthority New value of property
     * cboRenewalSanctioningAuthority.
     */
    public void setCboRenewalSanctioningAuthority(java.lang.String cboRenewalSanctioningAuthority) {
        this.cboRenewalSanctioningAuthority = cboRenewalSanctioningAuthority;
    }

    /**
     * Getter for property cboRenewalPurposeOfLoan.
     *
     * @return Value of property cboRenewalPurposeOfLoan.
     */
    public java.lang.String getCboRenewalPurposeOfLoan() {
        return cboRenewalPurposeOfLoan;
    }

    /**
     * Setter for property cboRenewalPurposeOfLoan.
     *
     * @param cboRenewalPurposeOfLoan New value of property
     * cboRenewalPurposeOfLoan.
     */
    public void setCboRenewalPurposeOfLoan(java.lang.String cboRenewalPurposeOfLoan) {
        this.cboRenewalPurposeOfLoan = cboRenewalPurposeOfLoan;
    }

    /**
     * Getter for property cboRenewalAccStatus.
     *
     * @return Value of property cboRenewalAccStatus.
     */
    public java.lang.String getCboRenewalAccStatus() {
        return cboRenewalAccStatus;
    }

    /**
     * Setter for property cboRenewalAccStatus.
     *
     * @param cboRenewalAccStatus New value of property cboRenewalAccStatus.
     */
    public void setCboRenewalAccStatus(java.lang.String cboRenewalAccStatus) {
        this.cboRenewalAccStatus = cboRenewalAccStatus;
    }

    /**
     * Getter for property cboRenewalRepayFreq.
     *
     * @return Value of property cboRenewalRepayFreq.
     */
    public java.lang.String getCboRenewalRepayFreq() {
        return cboRenewalRepayFreq;
    }

    /**
     * Setter for property cboRenewalRepayFreq.
     *
     * @param cboRenewalRepayFreq New value of property cboRenewalRepayFreq.
     */
    public void setCboRenewalRepayFreq(java.lang.String cboRenewalRepayFreq) {
        this.cboRenewalRepayFreq = cboRenewalRepayFreq;
    }

    /**
     * Getter for property cbmRenewalAppraiserId.
     *
     * @return Value of property cbmRenewalAppraiserId.
     */
    public com.see.truetransact.clientutil.ComboBoxModel getCbmRenewalAppraiserId() {
        return cbmRenewalAppraiserId;
    }

//    /**
//     * Setter for property cbmRenewalAppraiserId.
//     * @param cbmRenewalAppraiserId New value of property cbmRenewalAppraiserId.
//     */
    public void setCbmRenewalAppraiserId(com.see.truetransact.clientutil.ComboBoxModel cbmRenewalAppraiserId) {
        this.cbmRenewalAppraiserId = cbmRenewalAppraiserId;
   }
//    
//    /**
//     * Getter for property cbmRenewalPurityOfGold.
//     * @return Value of property cbmRenewalPurityOfGold.
//     */
//    public com.see.truetransact.clientutil.ComboBoxModel getCbmRenewalPurityOfGold() {
//        return cbmRenewalPurityOfGold;
//    }
//    
//    /**
//     * Setter for property cbmRenewalPurityOfGold.
//     * @param cbmRenewalPurityOfGold New value of property cbmRenewalPurityOfGold.
//     */
//    public void setCbmRenewalPurityOfGold(com.see.truetransact.clientutil.ComboBoxModel cbmRenewalPurityOfGold) {
//        this.cbmRenewalPurityOfGold = cbmRenewalPurityOfGold;
//    }
    /**
     * Getter for property cbmRenewalRepayFreq.
     *
     * @return Value of property cbmRenewalRepayFreq.
     */
    public com.see.truetransact.clientutil.ComboBoxModel getCbmRenewalRepayFreq() {
        return cbmRenewalRepayFreq;
    }

    /**
     * Setter for property cbmRenewalRepayFreq.
     *
     * @param cbmRenewalRepayFreq New value of property cbmRenewalRepayFreq.
     */
    public void setCbmRenewalRepayFreq(com.see.truetransact.clientutil.ComboBoxModel cbmRenewalRepayFreq) {
        this.cbmRenewalRepayFreq = cbmRenewalRepayFreq;
    }

    /**
     * Getter for property cbmRenewalAccStatus.
     *
     * @return Value of property cbmRenewalAccStatus.
     */
    public com.see.truetransact.clientutil.ComboBoxModel getCbmRenewalAccStatus() {
        return cbmRenewalAccStatus;
    }

    /**
     * Setter for property cbmRenewalAccStatus.
     *
     * @param cbmRenewalAccStatus New value of property cbmRenewalAccStatus.
     */
    public void setCbmRenewalAccStatus(com.see.truetransact.clientutil.ComboBoxModel cbmRenewalAccStatus) {
        this.cbmRenewalAccStatus = cbmRenewalAccStatus;
    }

    /**
     * Getter for property cbmRenewalPurposeOfLoan.
     *
     * @return Value of property cbmRenewalPurposeOfLoan.
     */
    public com.see.truetransact.clientutil.ComboBoxModel getCbmRenewalPurposeOfLoan() {
        return cbmRenewalPurposeOfLoan;
    }

    /**
     * Setter for property cbmRenewalPurposeOfLoan.
     *
     * @param cbmRenewalPurposeOfLoan New value of property
     * cbmRenewalPurposeOfLoan.
     */
    public void setCbmRenewalPurposeOfLoan(com.see.truetransact.clientutil.ComboBoxModel cbmRenewalPurposeOfLoan) {
        this.cbmRenewalPurposeOfLoan = cbmRenewalPurposeOfLoan;
    }

    /**
     * Getter for property cbmRenewalSanctioningAuthority.
     *
     * @return Value of property cbmRenewalSanctioningAuthority.
     */
    public com.see.truetransact.clientutil.ComboBoxModel getCbmRenewalSanctioningAuthority() {
        return cbmRenewalSanctioningAuthority;
    }

    /**
     * Setter for property cbmRenewalSanctioningAuthority.
     *
     * @param cbmRenewalSanctioningAuthority New value of property
     * cbmRenewalSanctioningAuthority.
     */
    public void setCbmRenewalSanctioningAuthority(com.see.truetransact.clientutil.ComboBoxModel cbmRenewalSanctioningAuthority) {
        this.cbmRenewalSanctioningAuthority = cbmRenewalSanctioningAuthority;
    }

    /**
     * Getter for property asAnWhenCustomer.
     *
     * @return Value of property asAnWhenCustomer.
     */
    public java.lang.String getAsAnWhenCustomer() {
        return asAnWhenCustomer;
    }

    /**
     * Setter for property asAnWhenCustomer.
     *
     * @param asAnWhenCustomer New value of property asAnWhenCustomer.
     */
    public void setAsAnWhenCustomer(java.lang.String asAnWhenCustomer) {
        this.asAnWhenCustomer = asAnWhenCustomer;
    }

    /**
     * Getter for property renewalYesNo.
     *
     * @return Value of property renewalYesNo.
     */
    public boolean isRenewalYesNo() {
        return renewalYesNo;
    }

    /**
     * Setter for property renewalYesNo.
     *
     * @param renewalYesNo New value of property renewalYesNo.
     */
    public void setRenewalYesNo(boolean renewalYesNo) {
        this.renewalYesNo = renewalYesNo;
    }

    /**
     * Getter for property txtRenewalPenalInter.
     *
     * @return Value of property txtRenewalPenalInter.
     */
    public java.lang.String getTxtRenewalPenalInter() {
        return txtRenewalPenalInter;
    }

    /**
     * Setter for property txtRenewalPenalInter.
     *
     * @param txtRenewalPenalInter New value of property txtRenewalPenalInter.
     */
    public void setTxtRenewalPenalInter(java.lang.String txtRenewalPenalInter) {
        this.txtRenewalPenalInter = txtRenewalPenalInter;
    }

    /**
     * Getter for property txtRenewalInter.
     *
     * @return Value of property txtRenewalInter.
     */
    public java.lang.String getTxtRenewalInter() {
        return txtRenewalInter;
    }

    /**
     * Setter for property txtRenewalInter.
     *
     * @param txtRenewalInter New value of property txtRenewalInter.
     */
    public void setTxtRenewalInter(java.lang.String txtRenewalInter) {
        this.txtRenewalInter = txtRenewalInter;
    }

    /**
     * Getter for property tdtRenewalDemandPromNoteExpDate.
     *
     * @return Value of property tdtRenewalDemandPromNoteExpDate.
     */
    public java.lang.String getTdtRenewalDemandPromNoteExpDate() {
        return tdtRenewalDemandPromNoteExpDate;
    }

    /**
     * Setter for property tdtRenewalDemandPromNoteExpDate.
     *
     * @param tdtRenewalDemandPromNoteExpDate New value of property
     * tdtRenewalDemandPromNoteExpDate.
     */
    public void setTdtRenewalDemandPromNoteExpDate(java.lang.String tdtRenewalDemandPromNoteExpDate) {
        this.tdtRenewalDemandPromNoteExpDate = tdtRenewalDemandPromNoteExpDate;
    }

    /**
     * Getter for property txtRenewalNoInstallments.
     *
     * @return Value of property txtRenewalNoInstallments.
     */
    public java.lang.String getTxtRenewalNoInstallments() {
        return txtRenewalNoInstallments;
    }

    /**
     * Setter for property txtRenewalNoInstallments.
     *
     * @param txtRenewalNoInstallments New value of property
     * txtRenewalNoInstallments.
     */
    public void setTxtRenewalNoInstallments(java.lang.String txtRenewalNoInstallments) {
        this.txtRenewalNoInstallments = txtRenewalNoInstallments;
    }

    /**
     * Getter for property tdtRenewalAccountOpenDate.
     *
     * @return Value of property tdtRenewalAccountOpenDate.
     */
    public java.lang.String getTdtRenewalAccountOpenDate() {
        return tdtRenewalAccountOpenDate;
    }

    /**
     * Setter for property tdtRenewalAccountOpenDate.
     *
     * @param tdtRenewalAccountOpenDate New value of property
     * tdtRenewalAccountOpenDate.
     */
    public void setTdtRenewalAccountOpenDate(java.lang.String tdtRenewalAccountOpenDate) {
        this.tdtRenewalAccountOpenDate = tdtRenewalAccountOpenDate;
    }

    /**
     * Getter for property txtRenewalLimit_SD.
     *
     * @return Value of property txtRenewalLimit_SD.
     */
    public java.lang.String getTxtRenewalLimit_SD() {
        return txtRenewalLimit_SD;
    }

    /**
     * Setter for property txtRenewalLimit_SD.
     *
     * @param txtRenewalLimit_SD New value of property txtRenewalLimit_SD.
     */
    public void setTxtRenewalLimit_SD(java.lang.String txtRenewalLimit_SD) {
        this.txtRenewalLimit_SD = txtRenewalLimit_SD;
    }

    /**
     * Getter for property txtRenewalSanctionRemarks.
     *
     * @return Value of property txtRenewalSanctionRemarks.
     */
    public java.lang.String getTxtRenewalSanctionRemarks() {
        return txtRenewalSanctionRemarks;
    }

    /**
     * Setter for property txtRenewalSanctionRemarks.
     *
     * @param txtRenewalSanctionRemarks New value of property
     * txtRenewalSanctionRemarks.
     */
    public void setTxtRenewalSanctionRemarks(java.lang.String txtRenewalSanctionRemarks) {
        this.txtRenewalSanctionRemarks = txtRenewalSanctionRemarks;
    }

    /**
     * Getter for property rdoRenewalNonPriority.
     *
     * @return Value of property rdoRenewalNonPriority.
     */
    public boolean isRdoRenewalNonPriority() {
        return rdoRenewalNonPriority;
    }

    /**
     * Setter for property rdoRenewalNonPriority.
     *
     * @param rdoRenewalNonPriority New value of property rdoRenewalNonPriority.
     */
    public void setRdoRenewalNonPriority(boolean rdoRenewalNonPriority) {
        this.rdoRenewalNonPriority = rdoRenewalNonPriority;
    }

    /**
     * Getter for property rdoRenewalPriority.
     *
     * @return Value of property rdoRenewalPriority.
     */
    public boolean isRdoRenewalPriority() {
        return rdoRenewalPriority;
    }

    /**
     * Setter for property rdoRenewalPriority.
     *
     * @param rdoRenewalPriority New value of property rdoRenewalPriority.
     */
    public void setRdoRenewalPriority(boolean rdoRenewalPriority) {
        this.rdoRenewalPriority = rdoRenewalPriority;
    }

    /**
     * Getter for property tdtRenewalSanctionDate.
     *
     * @return Value of property tdtRenewalSanctionDate.
     */
    public java.lang.String getTdtRenewalSanctionDate() {
        return tdtRenewalSanctionDate;
    }

    /**
     * Setter for property tdtRenewalSanctionDate.
     *
     * @param tdtRenewalSanctionDate New value of property
     * tdtRenewalSanctionDate.
     */
    public void setTdtRenewalSanctionDate(java.lang.String tdtRenewalSanctionDate) {
        this.tdtRenewalSanctionDate = tdtRenewalSanctionDate;
    }

    /**
     * Getter for property lblRenewalAcctNo_Sanction_Disp.
     *
     * @return Value of property lblRenewalAcctNo_Sanction_Disp.
     */
    public java.lang.String getLblRenewalAcctNo_Sanction_Disp() {
        return lblRenewalAcctNo_Sanction_Disp;
    }

    /**
     * Setter for property lblRenewalAcctNo_Sanction_Disp.
     *
     * @param lblRenewalAcctNo_Sanction_Disp New value of property
     * lblRenewalAcctNo_Sanction_Disp.
     */
    public void setLblRenewalAcctNo_Sanction_Disp(java.lang.String lblRenewalAcctNo_Sanction_Disp) {
        this.lblRenewalAcctNo_Sanction_Disp = lblRenewalAcctNo_Sanction_Disp;
    }

    /**
     * Getter for property tdtRenewalDemandPromNoteDate.
     *
     * @return Value of property tdtRenewalDemandPromNoteDate.
     */
    public java.lang.String getTdtRenewalDemandPromNoteDate() {
        return tdtRenewalDemandPromNoteDate;
    }

    /**
     * Setter for property tdtRenewalDemandPromNoteDate.
     *
     * @param tdtRenewalDemandPromNoteDate New value of property
     * tdtRenewalDemandPromNoteDate.
     */
    public void setTdtRenewalDemandPromNoteDate(java.lang.String tdtRenewalDemandPromNoteDate) {
        this.tdtRenewalDemandPromNoteDate = tdtRenewalDemandPromNoteDate;
    }

    /**
     * Getter for property renewalFacilityRecord.
     *
     * @return Value of property renewalFacilityRecord.
     */
    public java.util.HashMap getRenewalFacilityRecord() {
        return renewalFacilityRecord;
    }

    /**
     * Setter for property renewalFacilityRecord.
     *
     * @param renewalFacilityRecord New value of property renewalFacilityRecord.
     */
    public void setRenewalFacilityRecord(java.util.HashMap renewalFacilityRecord) {
        this.renewalFacilityRecord = renewalFacilityRecord;
    }

    /**
     * Getter for property renewalBorrowerNo.
     *
     * @return Value of property renewalBorrowerNo.
     */
    public java.lang.String getRenewalBorrowerNo() {
        return renewalBorrowerNo;
    }

    /**
     * Setter for property renewalBorrowerNo.
     *
     * @param renewalBorrowerNo New value of property renewalBorrowerNo.
     */
    public void setRenewalBorrowerNo(java.lang.String renewalBorrowerNo) {
        this.renewalBorrowerNo = renewalBorrowerNo;
    }

    /**
     * Getter for property tdtRenewalToDate.
     *
     * @return Value of property tdtRenewalToDate.
     */
    public java.lang.String getTdtRenewalToDate() {
        return tdtRenewalToDate;
    }

    /**
     * Setter for property tdtRenewalToDate.
     *
     * @param tdtRenewalToDate New value of property tdtRenewalToDate.
     */
    public void setTdtRenewalToDate(java.lang.String tdtRenewalToDate) {
        this.tdtRenewalToDate = tdtRenewalToDate;
    }

    /**
     * Getter for property allLoanAmount.
     *
     * @return Value of property allLoanAmount.
     */
    public java.util.HashMap getAllLoanAmount() {
        return allLoanAmount;
    }

    /**
     * Setter for property allLoanAmount.
     *
     * @param allLoanAmount New value of property allLoanAmount.
     */
    public void setAllLoanAmount(java.util.HashMap allLoanAmount) {
        this.allLoanAmount = allLoanAmount;
    }

    /**
     * Getter for property totRecivableAmt.
     *
     * @return Value of property totRecivableAmt.
     */
    public double getTotRecivableAmt() {
        return totRecivableAmt;
    }

    /**
     * Setter for property totRecivableAmt.
     *
     * @param totRecivableAmt New value of property totRecivableAmt.
     */
    public void setTotRecivableAmt(double totRecivableAmt) {
        this.totRecivableAmt = totRecivableAmt;
    }

    public HashMap getRenewalParamMap() {
        return renewalParamMap;
    }

    public void setRenewalParamMap(HashMap renewalParamMap) {
        this.renewalParamMap = renewalParamMap;
    }
     public ArrayList populateData(HashMap mapID, CTable tblData) {

        if (mapID != null && mapID.size() > 0) {
            _tblData = tblData;
            HashMap whereMap = null;
            if (mapID.containsKey(CommonConstants.MAP_WHERE)) {
                if (mapID.get(CommonConstants.MAP_WHERE) instanceof HashMap) {
                    whereMap = (HashMap) mapID.get(CommonConstants.MAP_WHERE);
                } else {
                    //System.out.println("Convert other data type to HashMap:" + mapID);
                }
            } else {
                whereMap = new HashMap();
            }

            if (!whereMap.containsKey(CommonConstants.BRANCH_ID)) {
                whereMap.put(CommonConstants.BRANCH_ID, ProxyParameters.BRANCH_ID);
            }
            if (!whereMap.containsKey(CommonConstants.USER_ID)) {
                whereMap.put(CommonConstants.USER_ID, ProxyParameters.USER_ID);
            }

            mapID.put(CommonConstants.MAP_WHERE, whereMap);

            //System.out.println("Screen   : " + getClass());
            //System.out.println("Map Name : " + mapID.get(CommonConstants.MAP_NAME));
            //System.out.println("Map      : " + mapID);

//        _isAvailable = ClientUtil.setTableModel(mapID, tblData);
            dataHash = ClientUtil.executeTableQuery(mapID);
            _heading = (ArrayList) dataHash.get(CommonConstants.TABLEHEAD);
            data = (ArrayList) dataHash.get(CommonConstants.TABLEDATA);
            setDataSize(data.size());
//        //System.out.println("### Data : "+data);
            _clearFlag = true;
        } else {
            _heading = null;
            setDataSize(0);
            _clearFlag = false;
        }

        if (getDataSize() <= MAXDATA) {
            populateTable();
        }
        return _heading;
    }

    public void populateTable() {
//        ArrayList heading = (ArrayList) dataHash.get(CommonConstants.TABLEHEAD);
        boolean dataExist;
        if (_heading != null) {
            _isAvailable = true;
            dataExist = true;
            TableSorter tableSorter = new TableSorter();
            tableSorter.addMouseListenerToHeaderInTable(_tblData);
            TableModel tableModel = new TableModel();
            tableModel.setHeading(_heading);
            tableModel.setData(data);
            tableModel.fireTableDataChanged();
            tableSorter.setModel(tableModel);
            tableSorter.fireTableDataChanged();
            _tblData.setModel(tableSorter);
            _tblData.revalidate();
            if (_heading.get(2) != null && CommonUtil.convertObjToStr(_heading.get(2)).equals("BILL_STATUS")) {
                _tblData.getColumnModel().getColumn(2).setPreferredWidth(0);
                _tblData.getColumnModel().getColumn(2).setMinWidth(0);
                _tblData.getColumnModel().getColumn(2).setMaxWidth(0);
            }
        } else {
            _isAvailable = false;
            dataExist = false;

            TableSorter tableSorter = new TableSorter();
            tableSorter.addMouseListenerToHeaderInTable(_tblData);
            TableModel tableModel = new TableModel();
            tableModel.setHeading(new ArrayList());
            tableModel.setData(new ArrayList());
            tableModel.fireTableDataChanged();
            tableSorter.setModel(tableModel);
            tableSorter.fireTableDataChanged();

            _tblData.setModel(tableSorter);
            _tblData.revalidate();
            // if(_clearFlag)
            // ClientUtil.noDataAlert();
        }
        if (_tblData.getModel() instanceof TableSorter) {
            _tableModel = ((TableSorter) _tblData.getModel()).getModel();
        } else {
            _tableModel = (TableModel) _tblData.getModel();
        }
    }

    public void setTable(CTable tbl) {
        _tblData = tbl;
    }

    public int getDataSize() {
        return dataSize;
    }

    /**
     * Setter for property dataSize.
     *
     * @param dataSize New value of property dataSize.
     */
    public void setDataSize(int dataSize) {
        this.dataSize = dataSize;
    }

    public LinkedHashMap getGoldItemMap() {
        return goldItemMap;
    }

    public void setGoldItemMap(LinkedHashMap goldItemMap) {
        this.goldItemMap = goldItemMap;
    }

    public ComboBoxModel getCbmRepayType() {
        return cbmRepayType;
    }

    public void setCbmRepayType(ComboBoxModel cbmRepayType) {
        this.cbmRepayType = cbmRepayType;
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
    
    // Added by nithya on 02-07-2018 for 0013083: gold loan renewal appraiser id issue
    public void filldropDownApp() throws Exception {
        HashMap param = new HashMap();
        param.put(CommonConstants.MAP_NAME, "getAppraiserCodeIfBranchChange");
        HashMap whereMap1 = new HashMap();
        whereMap1.put(CommonConstants.BRANCH_ID, ProxyParameters.BRANCH_ID);
        param.put(CommonConstants.PARAMFORQUERY, whereMap1);
        keyValue = ClientUtil.populateLookupData(param);
        fillData((HashMap) keyValue.get(CommonConstants.DATA));
        cbmAppraiserId = new ComboBoxModel(key, value);
    }
    
    // Added by nithya on 09-10-2018 for KD 263 GST - Needed to implement the GST in GOld Loan renewal
    public String getLblServiceTaxval() {
        return lblServiceTaxval;
    }

    public void setLblServiceTaxval(String lblServiceTaxval) {
        this.lblServiceTaxval = lblServiceTaxval;
    }

    public HashMap getServiceTax_Map() {
        return serviceTax_Map;
    }

    public void setServiceTax_Map(HashMap serviceTax_Map) {
        this.serviceTax_Map = serviceTax_Map;
    }
    
    public ServiceTaxDetailsTO setServiceTaxDetails() {
        final ServiceTaxDetailsTO objservicetaxDetTo = new ServiceTaxDetailsTO();
        try {
            objservicetaxDetTo.setCommand(getCommand());
            objservicetaxDetTo.setStatus(CommonConstants.STATUS_CREATED);
            objservicetaxDetTo.setStatusBy(TrueTransactMain.USER_ID);
            objservicetaxDetTo.setAcct_Num(getStrACNumber());
            objservicetaxDetTo.setParticulars("Gold Loan Renewal");
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
            objservicetaxDetTo.setCreatedDt(ClientUtil.getCurrentDate());
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
    
    public List calcServiceTaxAmount(String accNum,String prodId){// Changed the retyrn type from double to list
               HashMap whereMap = new HashMap();
        whereMap.put("ACT_NUM", accNum);
        List chargeAmtList = ClientUtil.executeQuery("getChargeDetails", whereMap);
        double taxAmt=0;
        HashMap taxMap; // Added by nithya
        List taxSettingsList = new ArrayList();// Added by nithya
        if (chargeAmtList != null && chargeAmtList.size() > 0 ) {
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
                        String chargetype="";
                        if(chargeMap.containsKey("CHARGE_TYPE")){
                            chargetype=CommonUtil.convertObjToStr(chargeMap.get("CHARGE_TYPE"));
                        }
                        if (chargetype!=null&&chargetype.equals("EP CHARGE")) {
                            accId = CommonUtil.convertObjToStr(accHeadObj.getEpCost());
                        }
                        if (chargetype!=null&&chargetype.equals("ARC CHARGE")) {
                            accId = CommonUtil.convertObjToStr(accHeadObj.getArcCost());
                        }
                        if (chargetype!=null&&chargetype.equals("MISCELLANEOUS CHARGES")) {
                            accId = CommonUtil.convertObjToStr(accHeadObj.getMiscServChrg());
                        }
                        if (chargetype!=null&&chargetype.equals("POSTAGE CHARGES")) {
                            accId = CommonUtil.convertObjToStr(accHeadObj.getPostageCharges());
                        }
                        if (chargetype!=null&&chargetype.equals("ADVERTISE CHARGES")) {
                            accId = CommonUtil.convertObjToStr(accHeadObj.getAdvertisementHead());
                        }
                        if (chargetype!=null&&chargetype.equals("ARBITRARY CHARGES")) {
                            accId = CommonUtil.convertObjToStr(accHeadObj.getArbitraryCharges());
                        }
                        if (chargetype!=null&&chargetype.equals("LEGAL CHARGES")) {
                            accId = CommonUtil.convertObjToStr(accHeadObj.getLegalCharges());
                        }
                        if (chargetype!=null&&chargetype.equals("NOTICE CHARGES")) {
                            accId = CommonUtil.convertObjToStr(accHeadObj.getNoticeCharges());
                        }
                        if (chargetype!=null&&chargetype.equals("INSURANCE CHARGES")) {
                            accId = CommonUtil.convertObjToStr(accHeadObj.getInsuranceCharges());
                        }
                        if (chargetype!=null&&chargetype.equals("EXECUTION DECREE CHARGES")) {
                            accId = CommonUtil.convertObjToStr(accHeadObj.getExecutionDecreeCharges());
                        }
                        // Added by nithya on 13-04-2018 for handling GST for other charges 
                        if (chargetype != null && chargetype.equals("OTHER CHARGES")) {
                            accId = CommonUtil.convertObjToStr(accHeadObj.getOthrChrgsHead());
                        }
                        //checkFlag = checkServiceTaxApplicable(accId);
                        checkForTaxMap = checkServiceTaxApplicable(accId);// Added by nithya
//                        if (checkFlag != null && checkFlag.equals("Y")) {// Commented by nithya 
//                            String charge_amt = CommonUtil.convertObjToStr(chargeMap.get("CHARGE_AMT"));
//                            taxAmt = taxAmt + CommonUtil.convertObjToDouble(charge_amt);
//                        }
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
       // return taxAmt;
        return taxSettingsList;
     }
    
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
    
    // END

    public int getMaximumDaysForLoan() {
        return maximumDaysForLoan;
    }

    public void setMaximumDaysForLoan(int maximumDaysForLoan) {
        this.maximumDaysForLoan = maximumDaysForLoan;
    }

    public int getMaximumDaysForRenewLoan() {
        return maximumDaysForRenewLoan;
    }

    public void setMaximumDaysForRenewLoan(int maximumDaysForRenewLoan) {
        this.maximumDaysForRenewLoan = maximumDaysForRenewLoan;
    }

    public byte[] getPhotoByteArray() {
        return photoByteArray;
    }

    public void setPhotoByteArray(byte[] photoByteArray) {
        this.photoByteArray = photoByteArray;
    }

    public String getPhotoFile() {
        return photoFile;
    }

    public void setPhotoFile(String photoFile) {
        this.photoFile = photoFile;
    }

    public byte[] getRenewalPhotoByteArray() {
        return renewalPhotoByteArray;
    }

    public void setRenewalPhotoByteArray(byte[] renewalPhotoByteArray) {
        this.renewalPhotoByteArray = renewalPhotoByteArray;
    }    
    
}
