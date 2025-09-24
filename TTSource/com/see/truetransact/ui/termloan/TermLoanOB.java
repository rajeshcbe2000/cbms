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
import com.see.truetransact.clientutil.ClientConstants;
import com.see.truetransact.clientutil.ClientUtil;
import com.see.truetransact.clientutil.ComboBoxModel;
import com.see.truetransact.clientutil.EnhancedTableModel;
import com.see.truetransact.clientutil .TableModel;
import com.see.truetransact.clientutil.TableUtil;
import com.see.truetransact.clientexception.ClientParseException;
import com.see.truetransact.clientproxy.ProxyFactory;
import com.see.truetransact.clientproxy.ProxyParameters;
import com.see.truetransact.commonutil.CommonConstants;
import com.see.truetransact.commonutil.DateUtil;
import com.see.truetransact.commonutil.CommonUtil;
//import com.see.truetransact.commonutil.TTException;
import com.see.truetransact.transferobject.termloan.TermLoanJointAcctTO;
import com.see.truetransact.transferobject.termloan.TermLoanCaseDetailTO;
import com.see.truetransact.transferobject.termloan.DailyLoanSanctionDetailsTO;
import com.see.truetransact.transferobject.termloan.TermLoanBorrowerTO;
import com.see.truetransact.transferobject.termloan.TermLoanClassificationTO;
import com.see.truetransact.transferobject.termloan.TermLoanCourtDetailsTO;
import com.see.truetransact.transferobject.termloan.TermLoanCompanyTO;
import com.see.truetransact.transferobject.termloan.TermLoanFacilityTO;
import com.see.truetransact.transferobject.termloan.TermLoanOtherDetailsTO;
import com.see.truetransact.transferobject.termloan.TermLoanSanctionFacilityTO; 
import com.see.truetransact.transferobject.termloan.TermLoanSecurityMemberTO;
import com.see.truetransact.transferobject.termloan.TermLoanSecuritySalaryTO;
import com.see.truetransact.transferobject.termloan.TermLoanSecurityLandTO;
import com.see.truetransact.transferobject.termloan.TermLoanSanctionTO;
import com.see.truetransact.ui.common.authorizedsignatory.AuthorizedSignatoryInstructionOB;
import com.see.truetransact.transferobject.termloan.TermLoanExtenFacilityDetailsTO;
import com.see.truetransact.ui.common.authorizedsignatory.AuthorizedSignatoryOB;
import com.see.truetransact.ui.termloan.loandisbursement.LoanDisbursementOB;
//import com.see.truetransact.ui.termloan.agrisubsidydetails.AgriSubSidyOB;
//import com.see.truetransact.ui.termloan.settlement.SettlementOB;
//import com.see.truetransact.ui.termloan.accounttransfer.ActTransOB;
import com.see.truetransact.ui.common.powerofattorney.PowerOfAttorneyOB;
import com.see.truetransact.transferobject.common.mobile.SMSSubscriptionTO;
import com.see.truetransact.transferobject.servicetax.servicetaxdetails.ServiceTaxDetailsTO;
import com.see.truetransact.transferobject.termloan.*;
import com.see.truetransact.transferobject.transaction.cash.CashTransactionTO;
import com.see.truetransact.transferobject.transaction.transfer.TxTransferTO;
import com.see.truetransact.ui.TrueTransactMain;
import com.see.truetransact.uicomponent.CObservable;
import com.see.truetransact.uicomponent.COptionPane;
import com.see.truetransact.ui.deposit.lien.DepositLienUI;
import com.see.truetransact.uicomponent.CTable;
import com.see.truetransact.transferobject.termloan.loanapplicationregister.LoanApplicationTO;
import com.see.truetransact.ui.common.servicetax.ServiceTaxCalculation;

import org.apache.log4j.Logger;

import java.util.ArrayList;
import java.util.Map;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Set;
import java.util.Date;
/**
 *
 * @author  shanmugavel
 * Created on January 8, 2004, 4:24 PM
 *
 */
public class TermLoanOB extends CObservable {
    
    private String balanceShareAmt = "";

  
    private int actionType;
    private int resultStatus;
    
    private String lblStatus = ClientConstants.ACTION_STATUS[ClientConstants.ACTIONTYPE_CANCEL];
    
    private       static TermLoanOB termLoanOB;
    private       static TermLoanBorrowerOB termLoanBorrowerOB;
    private       static TermLoanCompanyOB termLoanCompanyOB;
    private       static PowerOfAttorneyOB termLoanPoAOB;
    private       static TermLoanSecurityOB termLoanSecurityOB;
    private       static TermLoanRepaymentOB termLoanRepaymentOB;
    private       static TermLoanGuarantorOB termLoanGuarantorOB;
    private       static TermLoanDocumentDetailsOB termLoanDocumentDetailsOB;
    private       static TermLoanInterestOB termLoanInterestOB;
    private       static TermLoanClassificationOB termLoanClassificationOB;
    private       static TermLoanOtherDetailsOB termLoanOtherDetailsOB;
    private       static AuthorizedSignatoryOB termLoanAuthorizedSignatoryOB;
    private       static AuthorizedSignatoryInstructionOB termLoanAuthorizedSignatoryInstructionOB;
    private       static TermLoanAdditionalSanctionOB  termLoanAdditionalSanctionOB;//bala
    private       static LoanDisbursementOB agriSubLimitOB;
//    private       static AgriSubSidyOB  agriSubSidyOB;
//    private       static SettlementOB   settlementOB;
//    private       static ActTransOB actTransOB;
    private final static Logger log = Logger.getLogger(TermLoanOB.class);
    private final static ClientParseException parseException = ClientParseException.getInstance();
    
    private final   String    ACC_LIMIT = "ACC_LIMIT";
    private final   String    ACC_TYPE = "ACC_TYPE";
    private final   String    ACCT_NAME = "ACCT_NAME";
    private final   String    ACCT_NUM = "ACCT_NUM";
    private final   String    ACCT_STATUS = "ACCT_STATUS";
    private final   String    ALL_VALUES = "ALL_VALUES";
    private final   String    AOD_DATE = "AOD_DATE";
    private final   String    AUTHORIZE = "AUTHORIZE";
    private final   String    AUTHORIZED = "AUTHORIZED";
    private final   String    AUTHORIZE_BY_1 = "AUTHORIZE_BY_1";
    private final   String    AUTHORIZE_BY_2 = "AUTHORIZE_BY_2";
    private final   String    AUTHORIZE_DT_1 = "AUTHORIZE_DT_1";
    private final   String    AUTHORIZE_DT_2 = "AUTHORIZE_DT_2";
    private final   String    AUTHORIZE_STATUS_1 = "AUTHORIZE_STATUS_1";
    private final   String    AUTHORIZE_STATUS_2 = "AUTHORIZE_STATUS_2";
    private final   String    AVAILABLE_BALANCE = "AVAILABLE_BALANCE";
    private final   String    CLEAR_BALANCE = "CLEAR_BALANCE";
    private final   String    COMMAND = "COMMAND";
    private final   String    COMPOUND = "COMPOUND";
    private final   String    CONTACT_PERSON = "CONTACT_PERSON";
    private final String DEALER_ID = "DEALER_ID";//Added By Revathi.L
    private final   String    SALARY_RECOVERY = "SALARY_RECOVERY";
    private final   String    LOCK_STATUS = "LOCK_STATUS";
    private final   String    CONTACT_PHONE = "CONTACT_PHONE";
    private final   String    CUSTOMERGROUP = "CUSTOMERGROUP";
    private final   String    DELETE = "DELETE";
    private final   String    EXCEPTION = "EXCEPTION";
    private final   String    FACILITY_REPAY_DATE = "FACILITY_REPAY_DATE";
    private final   String    FACILITY_TYPE = "FACILITY_TYPE";
    private final   String    FALSE = "FALSE";
    private final   String    FLAG = "FLAG";
    private final   String    FROM = "FROM";
    private final   String    FULLY_SECURED = "FULLY_SECURED";
    private final   String    GROUP_DESC = "GROUP_DESC";
    private final   String    GUARANTAR = "GUARANTAR";
    private final   String    INSERT = "INSERT";
    private final   String    INSPECT_INSURANCE_GUARANTAR = "INSPECT,INSURANCE,GUARANTAR";
    private final   String    INSTALLMENT_DETAILS = "INSTALLMENT_DETAILS";
    private final   String    INSTALLMULTINT_DETAILS = "INSTALLMULTINT_DETAILS";
    private final   String    INT_GET_FROM = "INT_GET_FROM";
    private final   String    INTEREST = "INTEREST";
    private final   String    INTEREST_NATURE = "INTEREST_NATURE";
    private final   String    INTEREST_TYPE = "INTEREST_TYPE";
    private final   String    INSURANCE = "INSURANCE";
    private final   String    INSURANCE_GUARANTAR = "INSURANCE,GUARANTAR";
    private final   String    LIMIT = "LIMIT";
    private final   String    INITIAL_MONEY_DEPOSIT = "INITIAL_MONEY_DEPOSIT";
    private final   String    MAIN = "MAIN";
    private final   String    MAX_DEL_SAN_DETAIL_SL_NO = "MAX_DEL_SAN_DETAIL_SL_NO";
    private final   String    MAX_DEL_SAN_SL_NO = "MAX_DEL_SAN_SL_NO";
    private final   String    MORATORIUM_GIVEN = "MORATORIUM_GIVEN";
    private final   String    MORATORIUM_PERIOD = "MORATORIUM_PERIOD";
    private final   String    MULTI_DISBURSE = "MULTI_DISBURSE";
    private final   String    DRAWING_POWER = "DRAWING_POWER";
    private final   String    NEW = "NEW";
    private final   String    NIL = "NIL";
    private final   String    NO = "N";
    private final   String    NO_INSTALLMENTS = "NO_INSTALLMENTS";
    private final   String    NON_PLR = "NON_PLR";
    private final   String    NOTE_DATE = "NOTE_DATE";
    private final   String    NOTE_EXP_DATE = "NOTE_EXP_DATE";
    private final   String    NUMERIC_ZERO = "0";
    private final   String    OPTION = "OPTION";
    private final   String    PARTLY_SECURED = "PARTLY_SECURED";
    private final   String    PLR = "PLR";
    private final   String    PLR_RATE = "PLR_RATE";
    private final   String    PLR_RATE_APPL = "PLR_RATE_APPL";
    private final   String    PROD_ID = "PROD_ID";
    private final   String    PURPOSE_DESC = "PURPOSE_DESC";
    private final   String    REJECT = "REJECT";
    private final   String    REMARKS = "REMARKS";
    private final   String    ACCT_OPEN_DT="ACCT_OPEN_DT";
    private final   String    ACCT_CLOSE_DT="ACCT_CLOSE_DT";
    private final   String    RECOMMANDED_BY="RECOMMANDED_BY";
    private final   String    RECOMMANDED_BY2="RECOMMANDED_BY2";
    private final   String    KOLE_LAND_AREA="KOLE_LAND_AREA";
    private final   String    REPAY_FREQ = "REPAY_FREQ";
    private final   String    REPAYMENT_DETAILS = "REPAYMENT_DETAILS";
    private final   String    INSTALLMENT_ALL_DETAILS = "INSTALLMENT_ALL_DETAILS";
    private final   String    RISK_WEIGHT = "RISK_WEIGHT";
    private final   String    SANCTION = "SANCTION";
    private final   String    SANCTION_AUTHORITY = "SANCTION_AUTHORITY";
    private final   String    SANCTION_DATE = "SANCTION_DATE";
    private final   String    SANCTION_DT = "SANCTION_DT";
    private final   String    SANCTION_FACILITY = "SANCTION_FACILITY";
    private final   String    SANCTION_FACILITY_ALL = "SANCTION_FACILITY_ALL";
    private final   String    SANCTION_FACILITY_DELETED = "SANCTION_FACILITY_DELETED";
    private final   String    SANCTION_FACILITY_TABLE = "SANCTION_FACILITY_TABLE";
    private final   String    SANCTION_MODE = "SANCTION_MODE";
    private final   String    SANCTION_NO = "SANCTION_NO";
    private final   String    SANCTION_SL_NO = "SANCTION_SL_NO";
    private final   String    SECURITY_DETAILS = "SECURITY_DETAILS";
    private final   String    SECURITY_TYPE = "SECURITY_TYPE";
    private final   String    SHADOW_CREDIT = "SHADOW_CREDIT";
    private final   String    SHADOW_DEBIT = "SHADOW_DEBIT";
    private final   String    SIMPLE = "SIMPLE";
    private final   String    SLNO = "SLNO";
    
    
    private final   String    POFATTORNEY="POFATTORNEY";
    private final   String    DOCDETAILS="DOCDETAILS";
    private final   String    AUTHSIGNATORY="AUTHSIGNATORY";   // NEW CODE
    private final   String    ACCTTRANSFER="ACCTTRANSFER";
    private final   String    STOCK_INSPECT = "STOCK_INSPECT";
    private final   String    STOCK_INSPECT_GUARANTAR = "STOCK_INSPECT,GUARANTAR";
    private final   String    STOCK_INSPECT_INSURANCE = "STOCK_INSPECT,INSURANCE";
    private final   String    SUBMIT = "SUBMIT";
    private final   String    SUBSIDY = "SUBSIDY";
    private final   String    SUBSIDY_AMT = "SUBSIDY_AMT";    
    private final   String    SUBSIDY_DT = "SUBSIDY_DT";
    private final   String    SUBSIDY_ACHEAD = "SUBSIDY_ACHEAD";
    private final   String    SUBSIDY_ADJUSTED_AMT = "SUBSIDY_ADJUSTED_AMT";
     
    private final   String    REBATE_AMT = "REBATE_AMT";  
    private final   String    REBATE_DT = "REBATE_DT";    
    private final   String    REBATE_ALLOWED="REBATE_ALLOWED";
    
    private final   String    TABLE_VALUES = "TABLE_VALUES";
    private final   String    TO = "TO";
    private final   String    TO_DT = "TO_DT";
    private final   String    TOTAL_AVAILABLE_BALANCE = "TOTAL_AVAILABLE_BALANCE";
    private final   String    TRANSFERED = "TRANSFERED";
    private final   String    TRUE = "TRUE";
    private final   String    UNCLEAR_BALANCE = "UNCLEAR_BALANCE";
    private final   String    UNSECURED = "UNSECURED";
    private final   String    UPDATE = "UPDATE";
    private final   String    YES = "Y";
    private final   String    FIXED_RATE = "FIXED_RATE";
    
    private final   String    ENHANCE ="ENHANCE";
    private final   String    ODRENEWAL="ODRENEWAL";
    private final   ArrayList sanctionFacilityTitle = new ArrayList();  //  Table Title of Saction facility
    private final   ArrayList sanctionMainTitle = new ArrayList();      //  Table Title of Sanction Main
    private final   ArrayList shareTitle=new ArrayList();
    private final   ArrayList salaryTitle=new ArrayList();
    private final   ArrayList cropTitle=new ArrayList();
    
    
    private final String YEAR ="YEARS";
    private final String MONTH ="MONTHS";
    private final String DAY ="DAYS";
    
    private final String YEAR1 ="Years";
    private final String MONTH1 ="Months";
    private final String DAY1 ="Days";
    
//    private final String YES ="Y";
//    private final String NO ="N";
    private String duration = "";
    private int resultValue=0;
    private final int year = 365;
    private final int month = 30;
    private final int day = 1;
    private String kccNature = "";
    private boolean kccFlag = false;
    private String delIntFlag="";
    private static String suspenceAccountNo = null;
    private static String suspenceProductID = null;
    private static boolean tableCheck = false;
    private boolean isTransacton=false;
    private EnhancedTableModel tblVehicleTypeDetails;
    private LinkedHashMap vehicleTypeMap;
    private String txtVehicleMemNo = "";
    private String txtVehicleMembName = "";
    private String txtVehicleNo = "";
    private String txtVehicleContactNum = "";
    private String txtVehicleMemType = "";
    private String txtVehicleDetails="";
    private String txtVehicleRcBookNo="";
    private String txtVehicleDate="";
    private String txtVehicleType="";
    final ArrayList tableVehicleTitle = new ArrayList();
    private LinkedHashMap deletedVehicleTypeMap;
    private boolean vehicleTypeData = false;
    private boolean salaryTypeData = false;
    private double txtVehicleMemSal=0.0;
    private double txtVehicleNetworth=0.0;

    public double getTxtVehicleMemSal() {
        return txtVehicleMemSal;
    }

    public void setTxtVehicleMemSal(double txtVehicleMemSal) {
        this.txtVehicleMemSal = txtVehicleMemSal;
    }

    public double getTxtVehicleNetworth() {
        return txtVehicleNetworth;
    }

    public void setTxtVehicleNetworth(double txtVehicleNetworth) {
        this.txtVehicleNetworth = txtVehicleNetworth;
    }
    
    public boolean isVehicleTypeData() {
        return vehicleTypeData;
    }

    public void setVehicleTypeData(boolean vehicleTypeData) {
        this.vehicleTypeData = vehicleTypeData;
    }

    public boolean isSalaryTypeData() {
        return salaryTypeData;
    }

    public void setSalaryTypeData(boolean salaryTypeData) {
        this.salaryTypeData = salaryTypeData;
    }

    public EnhancedTableModel getTblVehicleTypeDetails() {
        return tblVehicleTypeDetails;
    }

    public void setTblVehicleTypeDetails(EnhancedTableModel tblVehicleTypeDetails) {
        this.tblVehicleTypeDetails = tblVehicleTypeDetails;
    }

    public String getTxtVehicleContactNum() {
        return txtVehicleContactNum;
    }

    public void setTxtVehicleContactNum(String txtVehicleContactNum) {
        this.txtVehicleContactNum = txtVehicleContactNum;
    }

    public String getTxtVehicleDate() {
        return txtVehicleDate;
    }

    public void setTxtVehicleDate(String txtVehicleDate) {
        this.txtVehicleDate = txtVehicleDate;
    }

    public String getTxtVehicleDetails() {
        return txtVehicleDetails;
    }

    public void setTxtVehicleDetails(String txtVehicleDetails) {
        this.txtVehicleDetails = txtVehicleDetails;
    }

    public String getTxtVehicleMemNo() {
        return txtVehicleMemNo;
    }

    public void setTxtVehicleMemNo(String txtVehicleMemNo) {
        this.txtVehicleMemNo = txtVehicleMemNo;
    }

    public String getTxtVehicleMemType() {
        return txtVehicleMemType;
    }

    public void setTxtVehicleMemType(String txtVehicleMemType) {
        this.txtVehicleMemType = txtVehicleMemType;
    }

    public String getTxtVehicleMembName() {
        return txtVehicleMembName;
    }

    public void setTxtVehicleMembName(String txtVehicleMembName) {
        this.txtVehicleMembName = txtVehicleMembName;
    }

    public String getTxtVehicleNo() {
        return txtVehicleNo;
    }

    public void setTxtVehicleNo(String txtVehicleNo) {
        this.txtVehicleNo = txtVehicleNo;
    }

    public String getTxtVehicleRcBookNo() {
        return txtVehicleRcBookNo;
    }

    public void setTxtVehicleRcBookNo(String txtVehicleRcBookNo) {
        this.txtVehicleRcBookNo = txtVehicleRcBookNo;
    }

    public String getTxtVehicleType() {
        return txtVehicleType;
    }

    public void setTxtVehicleType(String txtVehicleType) {
        this.txtVehicleType = txtVehicleType;
    }

    public LinkedHashMap getVehicleTypeMap() {
        return vehicleTypeMap;
    }

    public void setVehicleTypeMap(LinkedHashMap vehicleTypeMap) {
        this.vehicleTypeMap = vehicleTypeMap;
    }

    public boolean isIsTransacton() {
        return isTransacton;
    }

    public void setIsTransacton(boolean isTransacton) {
        this.isTransacton = isTransacton;
    }
    
    //    private final   TermLoanRB objTermLoanRB = new TermLoanRB();
   
    public static boolean isTableCheck() {
        return tableCheck;
    }

    public static void setTableCheck(boolean tableCheck) {
        TermLoanOB.tableCheck = tableCheck;
    }
    
    
    public static String getSuspenceAccountNo() {
        return suspenceAccountNo;
    }

    public static void setSuspenceAccountNo(String suspenceAccountNo) {
        TermLoanOB.suspenceAccountNo = suspenceAccountNo;
    }

    public static String getSuspenceProductID() {
        return suspenceProductID;
    }

    public static void setSuspenceProductID(String suspenceProductID) {
        TermLoanOB.suspenceProductID = suspenceProductID;
    }
    
    public String getDelIntFlag() {
        return delIntFlag;
    }

    public void setDelIntFlag(String delIntFlag) {
        this.delIntFlag = delIntFlag;
    }

    public boolean isKccFlag() {
        return kccFlag;
    }

    public void setKccFlag(boolean kccFlag) {
        this.kccFlag = kccFlag;
    }

    public String getKccNature() {
        return kccNature;
    }

    public void setKccNature(String kccNature) {
        this.kccNature = kccNature;
    }
    
    private final   java.util.ResourceBundle objTermLoanRB = java.util.ResourceBundle.getBundle("com.see.truetransact.ui.termloan.TermLoanRB", ProxyParameters.LANGUAGE);
    
    private EnhancedTableModel tblSanctionFacility;
    private EnhancedTableModel tblSanctionMain;
    private EnhancedTableModel tblShare;
    private EnhancedTableModel tblSalarySecrityTable;
    
    private EnhancedTableModel tblCropDetails;
    
    private ProxyFactory proxy = null;
    private ArrayList LoanNo=new ArrayList();
    private ArrayList key;
    private ArrayList value;
    private ArrayList sanctionFacilityTabRow;
    private ArrayList sanctionFacilityAllTabRecords = new ArrayList(); // ArrayList to display in Sanction Facility
    private ArrayList sanctionMainTabRow;
    private ArrayList sanctionMainAllTabRecords = new ArrayList();     // ArrayList to display in Sanction Main
    private ArrayList facilityTabSanction;
    private ArrayList installmentTitle;
    private LinkedHashMap objRepaymentInstallmentAllMap =new LinkedHashMap();
    private LinkedHashMap sanctionFacilityAll = new LinkedHashMap();   // Both displayed and hidden values in the table
    private LinkedHashMap sanctionMainAll = new LinkedHashMap();       // Both displayed and hidden values in the table
    private LinkedHashMap facilityAll = new LinkedHashMap();           // Both displayed and hidden values in the table
    private LinkedHashMap salarySecurityAll = new LinkedHashMap();  
    private LinkedHashMap salarySecurityDeleteAll = new LinkedHashMap(); 
    
    private HashMap removedFaccilityTabValues = new HashMap();
    private HashMap sanctionFacilityRecord;
    private HashMap sanctionMainRecord;
    private HashMap facilityRecord;
    
    private HashMap lookUpHash;
    private HashMap operationMap;
    private HashMap keyValue;
    private HashMap authorizeMap;
    
    private LinkedHashMap caseDetaillMap;
    private LinkedHashMap deletedCaseDetaillMap;
    private boolean isCaseDetailsTrans=false;
    private String loanCaseAuthStatus="";

    
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
    private ComboBoxModel cbmRecommendedByType2;
    private ComboBoxModel cbmIntGetFrom;
    private ComboBoxModel cbmCaseStatus;
    private ComboBoxModel cbmSecurityCity;
    private ComboBoxModel cbmNature;
    private ComboBoxModel cbmRight;
    private ComboBoxModel cbmPledge;
    private ComboBoxModel cbmDocumentType;
    private ComboBoxModel cbmDirectPaymentProdType;
    private ComboBoxModel cbmDirectPaymentProdId;
    private ComboBoxModel cbmAgentId;
    private ComboBoxModel cbmDirectRepaymentLoanPeriod;
    private boolean DirectRepayment_Yes =false;
    private boolean DirectRepayment_No =false;
    private String txtDirectRepaymentAcctNo ="";
    private String txtDirectRepaymentAcctHead ="";
    
    
    //court order details
    private String txtCourtOrderNo ="";
    private String tdtCourtOrderDate ="";
    private String tdtOTSDate ="";
    private String txtOTSRate ="";
    private String txtTotAmountDue= "";
    private String txtSettlementAmt = "";
    private double txtPrincipalAmount  =0.0;
    private double txtInterestAmount =0.0;
    private double txtPenalInterestAmount  =0.0;
    private double txtChargeAmount  =0.0;
    private double txtTotalAmountWrittenOff  =0.0;
    private String txtNoInstallment ="";
    private ComboBoxModel cbmFreq=null;
    private String txtInstallmentAmt  ="";
    private boolean rdoRepaySingle_YES=false;
    private boolean rdoRepaySingle_NO=false;
    private String firstInstallmentDt="";
    private String lastInstallmentDt="";
    private String txtPenal="";
    private String courtRemarks="";
    private boolean updateCourtDetails =false;
    
    //crop details 
    private String txtAreaAcrs ="";
    private String txtEligibleCropAmt ="";

    
    
    private HashMap newCourtDetailsMap=new HashMap(); 
    private HashMap editCourtDetailsMap=new HashMap(); 
    private HashMap deleteCourtDetailsMap=new HashMap(); 
    private boolean chkOTS =false;
    private int court_slno=0;
    
    private double periodData = 0;// for setting data depending on period comboboxes...
    private double resultData = 0;// for setting data depending on period comboboxes...
    
    private String docGenId="";
    
    private boolean chkDocDetails  =false;
    private boolean chkAuthorizedSignatory=false;
    private boolean chkPOFAttorney=false;
    
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
    private String txtLimit_SDMoneyDeposit="";
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
    private boolean chkEligibleAmt =false;
    private boolean chkAcctTransfer=false;
    private boolean rdoAccType_New = false;
    private boolean rdoAccType_Transfered = false;
    private boolean rdoAccLimit_Main = false;
    private boolean rdoAccLimit_Submit = false;
    private boolean rdoNatureInterest_PLR = false;
    private boolean rdoNatureInterest_NonPLR = false;
    private boolean dailyLoan = false;
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
    private boolean rdoDP_YES=false;
    private boolean rdoDP__NO=false;
    private String txtContactPerson = "";
    private String txtDealerID = "";//Added By Revathi.L
    private String txtContactPhone = "";
    private String txtRemarks = "";
    private String txtKoleLandArea = "";
    private String cboRecommendedByType="";
    private String cboRecommendedByType2 = "";
    private String AccountOpenDate="";
    private String accountCloseDate="";
    private String cboProdId = "";
    private String cboProdID_RS = "";
    private String cboProdID_IDetail = "";
    private String borrowerNo = "";
    private String lblAccHead_2 = "";
    private String lblDepositNo="";
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
    private String MaxLoanPeriodChar ="";
    private double eligibleMargin=0.0;
    private String minDecLoanPeriod = "0";
    private String maxDecLoanPeriod = "0";
    private String interestMode  = CommonConstants.TOSTATUS_INSERT;     // Mode of Interest Details
    private String classifiMode  = CommonConstants.TOSTATUS_INSERT;     // Mode of Classification Details
    private String strACNumber   = "";                                  // Current Account Number in Edit/Delete/Authorize Mode
    private String loanType = "";
    private HashMap depositCustDetMap;
    private boolean lienChanged = false;
    private String loanACNo="";
    private String depositNo="";
    private String deleteAction;
    private String ProductFacilityType="";
    private String BEHAVES_LIKE="";
    private String renewAvailableBalance="";
    Date curDate = null;
    private boolean updateAvailableBalance = false;
    private String oldSanction_no="";
    private HashMap advanceLiablityMap=new HashMap();
    private String  sanctionAmtRoundOff="";
    private boolean canBorrowerDelete = false;
    private double shadowDebit=0;
    private double shadowCredit=0;
    private double clearBalance=0;
    private double availableBalance=0;
    private String partReject="";
    public String tdtRepaymentDate="";
    private double slNoForSanction=0;
    private String cboCaseStatus = "";
    private String txtCaseNumber = "";
    private String tdtlFillingDt = "";
    private String txtFillingFees = "";
    private String txtMiscCharges = "";
    
    // Security Details
    private String txtMemNo = "";
    private String txtMemPriority = "";
    private String txtMemName = "";
    private String txtMemNetworth = "";
    private String txtContactNum = "";
    private String txtMemType = "";
    private boolean memberTypeData = false;
    private EnhancedTableModel tblMemberTypeDetails;
    private TermLoanSecurityMemberTO objMemberTypeTO;
    private LinkedHashMap memberTypeMap;
    private LinkedHashMap deletedMemberTypeMap;
    final ArrayList tableMemberTitle = new ArrayList();
    
    private String txtSalaryCertificateNo = "";
    private String txtEmployerName = "";
    private String txtAddress = "";
    private String cboSecurityCity = "";
    private String txtPinCode = "";
    private String txtDesignation = "";
    private String txtContactNo = "";
    private String tdtRetirementDt = "";
    private String txtMemberNum = "";
    private String txtTotalSalary = "";
    private String txtNetWorth = "";
    private String txtSalaryRemark = "";
    private int securitySlNo=0;
    private TermLoanSecuritySalaryTO objTermLoanSecuritySalaryTO;
    
    private String txtDirectRepaymentLoanPeriod="";
    private String txtOwnerMemNo = "";
    private String txtOwnerMemberNname = "";
    private String txtDocumentNo = "";
    private String txtDocumentType = "";
    private String tdtDocumentDate = "";
    private String txtRegisteredOffice = "";
    private String cboPledge = "";
    private String tdtPledgeDate = "";
    private String txtPledgeNo = "";
    private String txtPledgeAmount = "";
    private String txtVillage = "";
    private String txtSurveyNo = "";
    private String txtTotalArea = "";
    private String cboNature = "";
    private String cboRight = "";
    private String cboDocumentType="";
    private String txtAreaParticular = "";
    private String txtPledgeType="";
    private boolean rdoGahanYes=false;
    private boolean rdoGahanNo=false;
    private boolean collateralTypeData = false;
    private EnhancedTableModel tblCollateralDetails;
    private EnhancedTableModel tblJointCollateral;
    private TermLoanSecurityLandTO objTermLoanSecurityLandTO;
    private LinkedHashMap collateralMap;
    private LinkedHashMap deletedCollateralMap;
    final ArrayList tableCollateralTitle = new ArrayList();
    final ArrayList tableCollateralJointTitle = new ArrayList();
    private EnhancedTableModel tblCaseDetails;
    final ArrayList tableTitle = new ArrayList();
    private ArrayList IncVal = new ArrayList();
    private boolean newData = false;
    private ArrayList caseTabValues = new ArrayList(); 
    private String productCategory = "";
    private String keyValueForPaddyAndMDSLoans = "";
    private Map paddyMap = null;
    private Map mdsMap = null;
    private HashMap newTransactionMap;
    private HashMap pledgeValMap=new HashMap();
    //mobile banking
    private boolean isMobileBanking=false;
    SMSSubscriptionTO objSMSSubscriptionTO = null;
    private String txtMobileNo="";
    private String tdtMobileSubscribedFrom="";
    //OD RENEWAL
    private boolean rdoEnhance_Yes =false;
    private boolean rdoEnhance_No =false;
    private boolean rdoRenewal_Yes =false;
    private boolean rdoRenewal_No =false;
    
    // SUBSIDY & REBATE
    

    private String  txtSubsidyAmt="";
    private String     txtSubsidyAccHead="";
    private String   tdtSubsidyAppDt="";
    private String  txtSubsidyAdjustedAmt="";
    private boolean   rdoRebateInterest_Yes=false;
    private boolean     rdoRebateInterest_No=false;
    private String    txtRebateInterest_Amt="";
    private String   tdtRebateInterest_App_Dt="";
    //subsidy end

    private List chargelst = null;
    private String rdoSalaryRecovery = "";
    private String lockStatus = "";
    
    private String rdoGoldSecurityStockExists = "N"; // Added by nithya on 07-03-2020 for KD-1379
    private String txtGoldSecurityId = "";
    private String txtJewelleryDetails="";
    private String txtGrossWeight="";
    private String txtNetWeight="";
    private String txtValueOfGold="";
    private String txtGoldRemarks="";
    private String cboProductTypeSecurity="";
    private String txtDepNo="";
    private String cboDepProdID="";
    private String tdtDepDt="";
    private String txtDepAmount="";
    private String txtMaturityDt="";
    private String txtMaturityValue="";
    private String txtRateOfInterest="";
    private ComboBoxModel cbmProdType;
    private ComboBoxModel cbmProdTypeSecurity;
    private ComboBoxModel cbmProdId;
    private ComboBoxModel cbmCropName;
    private boolean depositTypeData=false;
    private ComboBoxModel cbmDepProdID;
    private LinkedHashMap deletedDepositTypeMap;
    private LinkedHashMap depositTypeMap;
    private EnhancedTableModel tblDepositTypeDetails;
    private LinkedHashMap DepositTypeMap;
    private String securityAmt="";
    private String chkRecovery="";

    public String getSecurityAmt() {
        return securityAmt;
    }

    public void setSecurityAmt(String securityAmt) {
        this.securityAmt = securityAmt;
    }
    final ArrayList tableTitleDepositList = new ArrayList();
    private boolean losTypeData=false;
    private String cboLosOtherInstitution;
    private String txtLosName="";
    private String cboLosSecurityType="";
    private String txtLosSecurityNo="";
    private String txtLosAmount="";
    private String tdtLosIssueDate="";
    private String tdtLosMaturityDt="";
    private String txtLosMaturityValue="";
    private String txtLosRemarks="";
    private ComboBoxModel cbmLosInstitution;
    private ComboBoxModel cbmLosSecurityType;
    private EnhancedTableModel tblLosTypeDetails;
    final ArrayList tableTitleLosList = new ArrayList();
    private LinkedHashMap losTypeMap;
    private LinkedHashMap deletedLosTypeMap;
    private HashMap loanMap;
    private String oldSurvyNo="";
    private HashMap serviceTax_Map=null;
    private String lblServiceTaxval="";

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

    
    public String getOldSurvyNo() {
        return oldSurvyNo;
    }

    public void setOldSurvyNo(String oldSurvyNo) {
        this.oldSurvyNo = oldSurvyNo;
    }
    private int rowCoun;

    public int getRowCoun() {
        return rowCoun;
    }

    public void setRowCoun(int rowCoun) {
        this.rowCoun = rowCoun;
    }

    
    
    //for EMI
    private boolean chkDiminishing=false;
    
    private Date   last_int_calc_dt=null;
	static {
        try {
            termLoanOB = new TermLoanOB();
        } catch(Exception e) {
            log.info("try: " + e);
            parseException.logException(e,true);
            e.printStackTrace();
        }
    }
    
    /**
     * Returns an instance of TermLoanOB
     *
     * @return TermLoanOB
     */
    public static TermLoanOB getInstance() {
        return termLoanOB;
    }
    
    /** Creates a new instance of TermLoanOB */
    private TermLoanOB() throws Exception {
        setOperationMap();
        try {
            curDate = ClientUtil.getCurrentDate();
            proxy = ProxyFactory.createProxy();
        } catch (Exception e) {
            parseException.logException(e,true);
            e.printStackTrace();
            log.info("TermLoanOB..."+e);
        }
        termLoanOB();
    }
    
    private void termLoanOB() throws Exception{
        termLoanBorrowerOB = TermLoanBorrowerOB.getInstance();
        termLoanCompanyOB = TermLoanCompanyOB.getInstance();
        termLoanSecurityOB = TermLoanSecurityOB.getInstance();
        termLoanRepaymentOB = TermLoanRepaymentOB.getInstance();
        termLoanGuarantorOB = TermLoanGuarantorOB.getInstance();
        termLoanDocumentDetailsOB = TermLoanDocumentDetailsOB.getInstance();
        termLoanInterestOB = TermLoanInterestOB.getInstance();
        termLoanClassificationOB = TermLoanClassificationOB.getInstance();
        termLoanOtherDetailsOB = TermLoanOtherDetailsOB.getInstance();
        termLoanAdditionalSanctionOB= TermLoanAdditionalSanctionOB.getInstance();
        fillDropdown();
        setVehicleTableTile();
        setSanctionFacilityTitle();
        setSanctionMainTitle();
        setShareTitle();
        setSalaryTitle();
        setCropTitle();
        setCaseDetailTile();
        setMemberTableTile();
        setCollateralTableTile();
        setCollateralJointTableTile();
        setDepositTableTile();
        setLosTableTile();
        tblVehicleTypeDetails= new EnhancedTableModel(null, tableVehicleTitle);
        tblCaseDetails= new EnhancedTableModel(null, tableTitle);
        tblMemberTypeDetails = new EnhancedTableModel(null, tableMemberTitle);
        tblCollateralDetails = new EnhancedTableModel(null, tableCollateralTitle);
        tblJointCollateral = new EnhancedTableModel(null, tableCollateralJointTitle);
        tblDepositTypeDetails = new EnhancedTableModel(null, tableTitleDepositList);
        tblLosTypeDetails = new EnhancedTableModel(null, tableTitleLosList);
        tableUtilSanction_Facility.setAttributeKey(SLNO);
        tableUtilSanction_Main.setAttributeKey(SANCTION_SL_NO);
        tblSanctionFacility = new EnhancedTableModel(null, sanctionFacilityTitle);
        tblSanctionMain = new EnhancedTableModel(null, sanctionMainTitle);
        tblShare=new EnhancedTableModel(null,shareTitle);
        tblSalarySecrityTable=new EnhancedTableModel(null,salaryTitle);
        tblCropDetails=new EnhancedTableModel(null,salaryTitle);
        notifyObservers();
    }
    private void setMemberTableTile() throws Exception{
        tableMemberTitle.add("Member No");
        tableMemberTitle.add("Name");
        tableMemberTitle.add("Member Type");
        tableMemberTitle.add("Contact No");
        tableMemberTitle.add("Networth");
        tableMemberTitle.add("Priority");
        IncVal = new ArrayList();
    }
    private void setVehicleTableTile() throws Exception {
        tableVehicleTitle.add("Member No");
        tableVehicleTitle.add("Name");
        tableVehicleTitle.add("Vehicle No");
        //tableVehicleTitle.add("Vehicle Type");
        //tableVehicleTitle.add("Contact No");
        tableVehicleTitle.add("Vehicle Rc BookNo");
        tableVehicleTitle.add("Salary");
        tableVehicleTitle.add("NetWorth");

        IncVal = new ArrayList();
    }
    private void setCollateralTableTile() throws Exception{
        tableCollateralTitle.add("Member No");
        tableCollateralTitle.add("Name");
        tableCollateralTitle.add("Doc No");
        tableCollateralTitle.add("Pledge Amt");
        tableCollateralTitle.add("SurveyNo");
        tableCollateralTitle.add("TotalArea");
        IncVal = new ArrayList();
    }
    
    private void setCollateralJointTableTile() throws Exception{
        tableCollateralJointTitle.add("Cust Id");
        tableCollateralJointTitle.add("Name");
        tableCollateralJointTitle.add("Constitution");
        IncVal = new ArrayList();
    }
    public void setExtendedOB(AuthorizedSignatoryOB authOB, AuthorizedSignatoryInstructionOB authInstOB, PowerOfAttorneyOB poaOB,
    LoanDisbursementOB agriSUBLIMITOB){//, AgriSubSidyOB ag ,SettlementOB settmntOB ,ActTransOB actTransOB){
        termLoanAuthorizedSignatoryOB = authOB;
        termLoanAuthorizedSignatoryInstructionOB = authInstOB;
        termLoanPoAOB = poaOB;
        agriSubLimitOB=agriSUBLIMITOB;
    }
     private void setCaseDetailTile() throws Exception{
        tableTitle.add("Status");
        tableTitle.add("Number");
        tableTitle.add("File Date");
        tableTitle.add("Auth Status");
        IncVal = new ArrayList();
    }
    private void setSanctionFacilityTitle() throws Exception{
        try {
            sanctionFacilityTitle.add(objTermLoanRB.getString("tblColumnSanction_Facility1"));
            sanctionFacilityTitle.add(objTermLoanRB.getString("tblColumnSanction_Facility2"));
            sanctionFacilityTitle.add(objTermLoanRB.getString("tblColumnSanction_Facility3"));
            sanctionFacilityTitle.add(objTermLoanRB.getString("tblColumnSanction_Facility4"));
            sanctionFacilityTitle.add(objTermLoanRB.getString("tblColumnSanction_Facility5"));
        }catch(Exception e) {
            log.info("Exception in setSanctionFacilityTitle: "+e);
            parseException.logException(e,true);
        }
    }
    private void setShareTitle() throws Exception{
        try{
            shareTitle.add(objTermLoanRB.getString("tblcolumnShareCustName"));
            shareTitle.add(objTermLoanRB.getString("tblcolumnShareCustId"));
            shareTitle.add(objTermLoanRB.getString("tblcolumnShareLoanNo"));
            shareTitle.add(objTermLoanRB.getString("tblcolumnShareType"));
            shareTitle.add(objTermLoanRB.getString("tblcolumnShareShareno"));
            shareTitle.add(objTermLoanRB.getString("tblcolumnShareNoOfShare"));
            shareTitle.add(objTermLoanRB.getString("tblcolumnShareShareAmt"));
            //            shareTitle.add(objTermLoanRB.getString("tblcolumnShareStatus"));
        }
        catch(Exception e){
            log.info("Exception in setShare Title:"+e);
            parseException.logException(e,true);
        }
    }
    
    private void setSalaryTitle() throws Exception{
        try{
            salaryTitle.add(objTermLoanRB.getString("tblcolumnSalaryslno"));
            salaryTitle.add(objTermLoanRB.getString("tblcolumnSalaryCertificate"));
            salaryTitle.add(objTermLoanRB.getString("tblcolumnSalaryMemberNo"));
            salaryTitle.add(objTermLoanRB.getString("tblcolumnSalaryMemberName"));
            salaryTitle.add(objTermLoanRB.getString("tblcolumnSalaryContactNo"));
            salaryTitle.add(objTermLoanRB.getString("tblcolumnSalaryNetworth"));
        }
        catch(Exception e){
            log.info("Exception in setShare Title:"+e);
            parseException.logException(e,true);
        }
    }    
    
    private void setCropTitle() throws Exception{
        try{
            cropTitle.add(objTermLoanRB.getString("tblcolumnSalaryslno"));
            cropTitle.add(objTermLoanRB.getString("tblcolumnSalaryCertificate"));
            cropTitle.add(objTermLoanRB.getString("tblcolumnSalaryMemberNo"));
            cropTitle.add(objTermLoanRB.getString("tblcolumnSalaryMemberName"));
           
        }
        catch(Exception e){
            log.info("Exception in setShare Title:"+e);
            parseException.logException(e,true);
        }
    }    
    private void setSanctionMainTitle() throws Exception{
        try {
            sanctionMainTitle.add(objTermLoanRB.getString("tblColumnSanction_Main1"));
            sanctionMainTitle.add(objTermLoanRB.getString("tblColumnSanction_Main2"));
            sanctionMainTitle.add(objTermLoanRB.getString("tblColumnSanction_Main3"));
            sanctionMainTitle.add(objTermLoanRB.getString("tblColumnSanction_Main4"));
        }catch(Exception e) {
            log.info("Exception in setSanctionMainTitle: "+e);
            parseException.logException(e,true);
        }
    }
    
    
        private void setInstallmentTitle() throws Exception{
        try {
            installmentTitle = new ArrayList();
            installmentTitle.add(objTermLoanRB.getString("tblColumnInstallNo"));
            installmentTitle.add(objTermLoanRB.getString("tblColumnInstallDate"));
            installmentTitle.add(objTermLoanRB.getString("tblColumnInstallPrincipal"));
            installmentTitle.add(objTermLoanRB.getString("tblColumnInstallInterest"));
            installmentTitle.add(objTermLoanRB.getString("tblColumnInstallTotal"));
            installmentTitle.add(objTermLoanRB.getString("tblColumnInstallBalance"));
            //            installmentTitle.add("EMI Amount");
        }catch(Exception e) {
            log.info("Exception in setInstallmentTitle: "+e);
            parseException.logException(e,true);
        }
    }
    
    private void setInstallmentTitleForUniformType() throws Exception{
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
        }catch(Exception e) {
            log.info("Exception in setInstallmentTitle: "+e);
            parseException.logException(e,true);
        }
    }

    
    /**
     * To populate the appropriate keys and values of all the combo
     * boxes in the screen at the time of TermLoanOB instance creation
     * @throws Exception will throw it to the TermLoanOB constructor
     */
    public void fillDropdown() throws Exception{
        lookUpHash = new HashMap();
        lookUpHash.put(CommonConstants.MAP_NAME,null);
        
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
        lookup_keys.add("TERM_LOAN.CASE_TYPE"); 
        lookup_keys.add("SECURITY.NATURE");
        lookup_keys.add("SECURITY.PLEDGE");
        lookup_keys.add("SECURITY.RIGHT");
        lookup_keys.add("SECURITY_TYPE");
        lookup_keys.add("LOSSECURITYTYPE");
        lookup_keys.add("LOSINSTITUTION");
        lookup_keys.add("TERMLOAN.DOCUMENT_TYPE");
        lookup_keys.add("PRODUCTTYPE");
        lookup_keys.add("PERIOD");
        lookup_keys.add("CROP_TYPE");
        
        lookUpHash.put(CommonConstants.PARAMFORQUERY, lookup_keys);
        
        keyValue = ClientUtil.populateLookupData(lookUpHash);
        
        getKeyValue((HashMap)keyValue.get("CATEGORY"));
        termLoanBorrowerOB.setCbmCategory(new ComboBoxModel(key,value));
        getKeyValue((HashMap)keyValue.get("LOSINSTITUTION"));
        cbmLosInstitution=new ComboBoxModel(key,value);
        getKeyValue((HashMap)keyValue.get("LOSSECURITYTYPE"));
        cbmLosSecurityType=new ComboBoxModel(key,value);
        
        getKeyValue((HashMap)keyValue.get("TERM_LOAN.CASE_TYPE"));
        setCbmCaseStatus(new ComboBoxModel(key,value));
        
        getKeyValue((HashMap)keyValue.get("PERIOD"));
        setCbmDirectRepaymentLoanPeriod(new ComboBoxModel(key,value));
        
        
        getKeyValue((HashMap)keyValue.get("CONSTITUTION"));
        termLoanBorrowerOB.setCbmConstitution(new ComboBoxModel(key,value));
        
        getKeyValue((HashMap)keyValue.get("CUSTOMER.ADDRTYPE"));
        termLoanCompanyOB.setCbmAddressType(new ComboBoxModel(key,value));
        
        getKeyValue((HashMap)keyValue.get("CUSTOMER.CITY"));
        termLoanCompanyOB.setCbmCity_CompDetail(new ComboBoxModel(key,value));
        
        getKeyValue((HashMap)keyValue.get("CUSTOMER.CITY"));
        setCbmSecurityCity(new ComboBoxModel(key,value));
        
        getKeyValue((HashMap)keyValue.get("SECURITY.NATURE"));
        setCbmNature(new ComboBoxModel(key,value));
        
        getKeyValue((HashMap)keyValue.get("SECURITY.RIGHT"));
        setCbmRight(new ComboBoxModel(key,value));
        
        getKeyValue((HashMap)keyValue.get("SECURITY.PLEDGE"));
        setCbmPledge(new ComboBoxModel(key,value));

        getKeyValue((HashMap)keyValue.get("TERMLOAN.DOCUMENT_TYPE"));//SECURITY_TYPE
        setCbmDocumentType(new ComboBoxModel(key,value));
        
        getKeyValue((HashMap)keyValue.get("PRODUCTTYPE"));
        cbmDirectPaymentProdType = new ComboBoxModel(key,value);
        
        getKeyValue((HashMap)keyValue.get("CUSTOMER.STATE"));
        termLoanCompanyOB.setCbmState_CompDetail(new ComboBoxModel(key,value));
        
        getKeyValue((HashMap)keyValue.get("CUSTOMER.COUNTRY"));
        termLoanCompanyOB.setCbmCountry_CompDetail(new ComboBoxModel(key,value));
        
        getKeyValue((HashMap)keyValue.get("TERM_LOAN.BUSINESS_NATURE"));
        ArrayList tempKey = key;
        ArrayList tempVal = value;
        getKeyValue((HashMap)keyValue.get("CUSTOMER.PRIMARYOCCUPATION"));
        for (int i = key.size() - 1,j = 0;i >= 0;--i,++j){
            if (!key.get(j).equals("")){
                tempKey.add(key.get(j));
            }
        }
        for (int i = value.size() - 1,j = 0;i >= 0;--i,++j){
            if (!value.get(j).equals("")){
                tempVal.add(value.get(j));
            }
        }
        termLoanCompanyOB.setCbmNatureBusiness(new ComboBoxModel(tempKey, tempVal));
        
        getKeyValue((HashMap)keyValue.get("LOAN.ACCOUNT_STATUS"));
        setCbmAccStatus(new ComboBoxModel(key,value));
        
        getKeyValue((HashMap)keyValue.get("LOAN.INT_GET_FROM"));
        cbmIntGetFrom = new ComboBoxModel(key,value);
        
        getKeyValue((HashMap)keyValue.get("CUSTOMER.CITY"));
        cbmSanctioningAuthority = new ComboBoxModel(key,value);
        
        getKeyValue((HashMap)keyValue.get("TERM_LOAN.SANCTIONING_AUTHORITY"));
        cbmSanctioningAuthority = new ComboBoxModel(key,value);
        
        getKeyValue((HashMap)keyValue.get("TERM_LOAN.SANCTION_MODE"));
        cbmModeSanction = new ComboBoxModel(key,value);
        
        getKeyValue((HashMap)keyValue.get("LOAN.FREQUENCY"));
        cbmRepayFreq = new ComboBoxModel(key,value);
        
        getKeyValue((HashMap)keyValue.get("LOAN.FREQUENCY"));
        setCbmRepayFreq_LOAN(new ComboBoxModel(key,value));
        
        getKeyValue((HashMap)keyValue.get("LOAN.FREQUENCY"));
        setCbmFreq(new ComboBoxModel(key,value));
        
        getKeyValue((HashMap)keyValue.get("TERM_LOAN.SANCTION_MODE"));
        cbmModeSanction = new ComboBoxModel(key,value);
        
        getKeyValue((HashMap)keyValue.get("FREQUENCY"));
        for (int i = key.size() - 1;i >= 0;--i){
            if (key.get(i).equals("180") || key.get(i).equals("90") || key.get(i).equals("7") /*|| key.get(i).equals("1")*/){
                key.remove(i);
            }
        }
        for (int i = value.size() - 1;i >= 0;--i){
            if (value.get(i).equals("Half Yearly") || value.get(i).equals("Quaterly") || value.get(i).equals("Weekly")  /*|| value.get(i).equals("Daily")*/){
                value.remove(i);
            }
        }
        setCbmRepayFreq_ADVANCE(new ComboBoxModel(key,value));
        
        getKeyValue((HashMap)keyValue.get("LOANPRODUCT.OPERATESLIKE"));
        cbmTypeOfFacility = new ComboBoxModel(key,value);
        //        if(loanType.equals("OTHERS"))
        //            cbmTypeOfFacility.removeElement("Loans Against Deposits");
        ////System.out.println("loanType###"+loanType);
        getKeyValue((HashMap)keyValue.get("CONSTITUTION"));
        termLoanGuarantorOB.setCbmConstitution_GD(new ComboBoxModel(key,value));
        
        getKeyValue((HashMap)keyValue.get("CUSTOMER.CITY"));
        termLoanGuarantorOB.setCbmCity_GD(new ComboBoxModel(key,value));
        
        getKeyValue((HashMap)keyValue.get("CUSTOMER.STATE"));
        termLoanGuarantorOB.setCbmState_GD(new ComboBoxModel(key,value));
        
        getKeyValue((HashMap)keyValue.get("CUSTOMER.COUNTRY"));
        termLoanGuarantorOB.setCbmCountry_GD(new ComboBoxModel(key,value));
        
        getKeyValue((HashMap)keyValue.get("PRODUCTTYPE"));
        termLoanGuarantorOB.setCbmProdType(new ComboBoxModel(key,value));
        
        getKeyValue((HashMap)keyValue.get("TERM_LOAN.GUARANTEE_STATUS"));
        termLoanGuarantorOB.setCbmGuaranStatus(new ComboBoxModel(key,value));
        
        getKeyValue((HashMap)keyValue.get("TERMLOAN.INTERESTTYPE"));
        cbmInterestType = new ComboBoxModel(key,value);
        getKeyValue((HashMap)keyValue.get("BOARDDIRECTORS"));
        cbmRecommendedByType = new ComboBoxModel(key,value);
        cbmRecommendedByType2 = new ComboBoxModel(key,value);
        
        getKeyValue((HashMap)keyValue.get("LOAN.FREQUENCY"));
        termLoanRepaymentOB.setCbmRepayFreq_Repayment(new ComboBoxModel(key,value));
        
        getKeyValue((HashMap)keyValue.get("TERM_LOAN.REPAYMENT_TYPE"));
        termLoanRepaymentOB.setCbmRepayType(new ComboBoxModel(key,value));
        termLoanRepaymentOB.setCbmSanRepaymentType(new ComboBoxModel(key,value));
        
        getKeyValue((HashMap)keyValue.get("TERM_LOAN.COMMODITY_CODE"));
        termLoanClassificationOB.setCbmCommodityCode(new ComboBoxModel(key,value));
        
        getKeyValue((HashMap)keyValue.get("TERM_LOAN.SECTOR_CODE"));
        termLoanClassificationOB.setCbmSectorCode1(new ComboBoxModel(key,value));
        
        getKeyValue((HashMap)keyValue.get("TERM_LOAN.PURPOSE_CODE"));
        termLoanClassificationOB.setCbmPurposeCode(new ComboBoxModel(key,value));
        
        getKeyValue((HashMap)keyValue.get("TERM_LOAN.INDUSTRY_CODE"));
        termLoanClassificationOB.setCbmIndusCode(new ComboBoxModel(key,value));
        
        getKeyValue((HashMap)keyValue.get("TERM_LOAN.20_CODE"));
        termLoanClassificationOB.setCbm20Code(new ComboBoxModel(key,value));
        
        getKeyValue((HashMap)keyValue.get("TERM_LOAN.GOVT_SCHEME_CODE"));
        termLoanClassificationOB.setCbmGovtSchemeCode(new ComboBoxModel(key,value));
        
        getKeyValue((HashMap)keyValue.get("TERM_LOAN.GUARANTEE_COVER_CODE"));
        termLoanClassificationOB.setCbmGuaranteeCoverCode(new ComboBoxModel(key,value));
        
        getKeyValue((HashMap)keyValue.get("TERM_LOAN.HEALTH_CODE"));
        termLoanClassificationOB.setCbmHealthCode(new ComboBoxModel(key,value));
        
        getKeyValue((HashMap)keyValue.get("TERM_LOAN.DISTRICT_CODE"));
        termLoanClassificationOB.setCbmDistrictCode(new ComboBoxModel(key,value));
        
        getKeyValue((HashMap)keyValue.get("TERM_LOAN.WEAKERSECTION_CODE"));
        termLoanClassificationOB.setCbmWeakerSectionCode(new ComboBoxModel(key,value));
        
        getKeyValue((HashMap)keyValue.get("TERM_LOAN.REFINANCING_INSTITUTION"));
        termLoanClassificationOB.setCbmRefinancingInsti(new ComboBoxModel(key,value));
        
        getKeyValue((HashMap)keyValue.get("TERM_LOAN.ASSET_STATUS"));
        termLoanClassificationOB.setCbmAssetCode(new ComboBoxModel(key,value));
        
        getKeyValue((HashMap)keyValue.get("TERM_LOAN.FACILITY"));
        termLoanClassificationOB.setCbmTypeFacility(new ComboBoxModel(key,value));
        
        getKeyValue((HashMap)keyValue.get("ACT_OP_MODE"));
        termLoanOtherDetailsOB.setCbmOpModeAI(new ComboBoxModel(key,value));
        
        getKeyValue((HashMap)keyValue.get("SETTLEMENT_MODE"));
        termLoanOtherDetailsOB.setCbmSettlementModeAI(new ComboBoxModel(key,value));
        
        getKeyValue((HashMap)keyValue.get("FREQUENCY"));
        termLoanOtherDetailsOB.setCbmStmtFreqAD(new ComboBoxModel(key,value));
        
        getKeyValue((HashMap)keyValue.get("CROP_TYPE"));
        setCbmCropName(new ComboBoxModel(key,value));
        
        lookup_keys.add("PRODUCTTYPE");
        lookUpHash.put(CommonConstants.PARAMFORQUERY, lookup_keys);
        keyValue = com.see.truetransact.clientutil.ClientUtil.populateLookupData(lookUpHash);
        getKeyValue((HashMap)keyValue.get("PRODUCTTYPE"));
        cbmProdType= new ComboBoxModel(key,value);
        HashMap where=new HashMap();
        where.put("BRANCH_ID", ProxyParameters.BRANCH_ID);
        //List lst = (List)ClientUtil.executeQuery("getAgentIdName", where); //Commented By Suresh R Ref. By Mr Abi
        List lst = (List) ClientUtil.executeQuery("getAgentIDDetails", where);
        ////System.out.println("########ListForAgent : "+lst);
        getMap(lst);
        setCbmAgentId(new ComboBoxModel(key,value));
        
        
        
        key = new ArrayList();
        value = new ArrayList();
        key.add("");
        value.add("");
        key.add("TD");
        value.add("Deposits");
        key.add("MDS");
        value.add("MDS");
        cbmProdTypeSecurity = new ComboBoxModel(key,value);
        
        cbmProdId = new ComboBoxModel();
        cbmDepProdID = new ComboBoxModel();
        
          lookUpHash=new HashMap();
            lookUpHash.put(CommonConstants.MAP_NAME, "InwardClearing.getBank");
            keyValue = ClientUtil.populateLookupData(lookUpHash);
            getKeyValue((HashMap)keyValue.get("DATA"));
            termLoanGuarantorOB.setCbmPLIName(new ComboBoxModel(key,value));
        
        //        lookUpHash = new HashMap();
        //        lookUpHash.put(CommonConstants.MAP_NAME,"TermLoan.getProdId");
        //        lookUpHash.put(CommonConstants.PARAMFORQUERY,null);
        //        keyValue = ClientUtil.populateLookupData(lookUpHash);
        //
        //        getKeyValue((HashMap)keyValue.get(CommonConstants.DATA));
        
        setProdIDAsBlank();
        
        termLoanGuarantorOB.setCbmProdId("");
        
        tempKey = null;
        tempVal = null;
        lookup_keys = null;
    }
    
    private void getMap(List list) throws Exception{
        key = new ArrayList();
        value = new ArrayList();
        
        //The first values in the ArrayList key and value are empty String to display the
        //first row of all dropdowns to be empty String
        key.add("");
        value.add("");
        for (int i=0, j=list.size(); i < j; i++) {
            key.add(((HashMap)list.get(i)).get("KEY"));
            value.add(((HashMap)list.get(i)).get("VALUE"));
        }
    }
    private void setOperationMap() throws Exception{
        operationMap = new HashMap();
        operationMap.put(CommonConstants.JNDI, "TermLoanJNDI");
        operationMap.put(CommonConstants.HOME, "termloan.TermLoanHome");
        operationMap.put(CommonConstants.REMOTE, "termloan.TermLoan");
    }
    
    private void setBlankKeyValue(){
        key = new ArrayList();
        key.add("");
        value = new ArrayList();
        value.add("");
    }
    
    private void getKeyValue(HashMap keyValue)  throws Exception{
        setBlankKeyValue();
        key = (ArrayList)keyValue.get(CommonConstants.KEY);
        value = (ArrayList)keyValue.get(CommonConstants.VALUE);
    }
    
    
     public void setCbmProdId(String prodType) {
        if (CommonUtil.convertObjToStr(prodType).length()>1) {
            if (prodType.equals("GL")) {
                key = new ArrayList();
                value = new ArrayList();
            } else {
                try {
                    lookUpHash = new HashMap();
                    lookUpHash.put(CommonConstants.MAP_NAME,"Cash.getAccProduct" + prodType);
                    lookUpHash.put(CommonConstants.PARAMFORQUERY, null);
                    keyValue = ClientUtil.populateLookupData(lookUpHash);
                    getKeyValue((HashMap)keyValue.get(CommonConstants.DATA));
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
        cbmDirectPaymentProdId = new ComboBoxModel(key,value);
        this.cbmDirectPaymentProdId = cbmDirectPaymentProdId;
        setChanged();
    }
     
    public void resetMemberTypeDetails() {
        setTxtMemNo("");
        setTxtMemName("");
        setTxtMemType("");
        setTxtContactNum("");
        setTxtMemNetworth("");
        setTxtMemPriority("");
    }
    
    public void resetCollateralDetails() {
        setTxtOwnerMemNo("");
        setTxtOwnerMemberNname("");
        setTxtDocumentNo("");
        setTxtDocumentType("");
        setTdtDocumentDate("");
        setTxtRegisteredOffice("");
        setCboPledge("");
        setTdtPledgeDate("");
        setTxtPledgeNo("");
        setTxtPledgeAmount("");
        setTxtVillage("");
        setTxtSurveyNo("");
        setTxtTotalArea("");
        setCboNature("");
        setCboRight("");
        setCboDocumentType("");
        setTxtAreaParticular("");
        setRdoGahanNo(false);
        setRdoGahanYes(false);
        setTxtPledgeType("");
        setDocGenId("");
    }
    public void resetGoldTypeDetails() {
        setTxtJewelleryDetails("");
        setTxtGoldRemarks("");
        setTxtGrossWeight("");
        setTxtValueOfGold("");
        setTxtNetWeight("");
        setRdoGoldSecurityStockExists("N"); // Added by nithya on 07-03-2020 for KD-1379
        setTxtGoldSecurityId("");
    }
     public void addVehicleTypeTable(int rowSelected, boolean updateMode) {
        try {
            int rowSel = rowSelected;
            final TermLoanSecurityVehicleTO objVehicleTypeTO = new TermLoanSecurityVehicleTO();
            if (vehicleTypeMap == null) {
                vehicleTypeMap= new LinkedHashMap();
            }
            if (getActionType() == ClientConstants.ACTIONTYPE_EDIT) {
                if (isVehicleTypeData()) {
                    objVehicleTypeTO.setStatusDt(curDate);
                    objVehicleTypeTO.setStatusBy(TrueTransactMain.USER_ID);
                    objVehicleTypeTO.setStatus(CommonConstants.STATUS_CREATED);
                } else {
                    objVehicleTypeTO.setStatusDt(curDate);
                    objVehicleTypeTO.setStatusBy(TrueTransactMain.USER_ID);
                    objVehicleTypeTO.setStatus(CommonConstants.STATUS_MODIFIED);
                }
            } else {
                objVehicleTypeTO.setStatusDt(curDate);
                objVehicleTypeTO.setStatusBy(TrueTransactMain.USER_ID);
                objVehicleTypeTO.setStatus(CommonConstants.STATUS_CREATED);
            }
           // objVehicleTypeTO.setAcctNum(getStrACNumber());
            objVehicleTypeTO.setMemberNo(getTxtVehicleMemNo());
            objVehicleTypeTO.setMemberName(getTxtVehicleMembName());
            objVehicleTypeTO.setMemberType(getTxtVehicleMemType());
            objVehicleTypeTO.setContactNo(CommonUtil.convertObjToInt(getTxtVehicleContactNum()));
            objVehicleTypeTO.setVehicleNo(getTxtVehicleNo());
            objVehicleTypeTO.setVehicleRcBookNo(getTxtVehicleRcBookNo());
            objVehicleTypeTO.setVehicleType(getTxtVehicleType());
            objVehicleTypeTO.setVehichleDetails(getTxtVehicleDetails());
            objVehicleTypeTO.setVehicleDate(getProperDateFormat(getTxtVehicleDate()));
            objVehicleTypeTO.setMemSalary(getTxtVehicleMemSal());
            objVehicleTypeTO.setNetWorth(getTxtVehicleNetworth());
            vehicleTypeMap.put(objVehicleTypeTO.getMemberNo(), objVehicleTypeTO);
            updateVehicleTypeDetails(rowSel, objVehicleTypeTO, updateMode);
            //            ttNotifyObservers();
        } catch (Exception e) {
            parseException.logException(e, true);
        }
    }
private void updateVehicleTypeDetails(int rowSel, TermLoanSecurityVehicleTO objVehicleTypeTO, boolean updateMode) throws Exception {
        Object selectedRow;
        boolean rowExists = false;
        //If row already exists update it, else create a new row & append
        for (int i = tblVehicleTypeDetails.getRowCount(), j = 0; i > 0; i--, j++) {
            selectedRow = ((ArrayList) tblVehicleTypeDetails.getDataArrayList().get(j)).get(0);
            if (getTxtVehicleMemNo().equals(CommonUtil.convertObjToStr(selectedRow))) {
                rowExists = true;
                ArrayList IncParRow = new ArrayList();
                ArrayList data = tblVehicleTypeDetails.getDataArrayList();
                data.remove(rowSel);
                IncParRow.add(getTxtVehicleMemNo());
                IncParRow.add(getTxtVehicleMembName());
                IncParRow.add(getTxtVehicleNo());
               // IncParRow.add(getTxtVehicleType());
              //  IncParRow.add(getTxtContactNum());
                IncParRow.add(getTxtVehicleRcBookNo());
                IncParRow.add(getTxtVehicleMemSal());
                IncParRow.add(getTxtVehicleNetworth());
                tblVehicleTypeDetails.insertRow(rowSel, IncParRow);
                IncParRow = null;
            }
        }
        if (!rowExists) {
            ArrayList IncParRow = new ArrayList();
            IncParRow.add(getTxtVehicleMemNo());
            IncParRow.add(getTxtVehicleMembName());
            IncParRow.add(getTxtVehicleNo());
          //  IncParRow.add(getTxtVehicleType());
           // IncParRow.add(getTxtContactNum());
            IncParRow.add(getTxtVehicleRcBookNo());
            IncParRow.add(getTxtVehicleMemSal());
            IncParRow.add(getTxtVehicleNetworth());
            tblVehicleTypeDetails.insertRow(tblVehicleTypeDetails.getRowCount(), IncParRow);
            IncParRow = null;
        }
    }
    public void populateVehicleTypeDetails(String row) {
        try {
            resetVehicleTypeDetails();
            final TermLoanSecurityVehicleTO objVehicleTypeTO = (TermLoanSecurityVehicleTO) vehicleTypeMap.get(row);
            populateVehicleTableData(objVehicleTypeTO);
        } catch (Exception e) {
            parseException.logException(e, true);
        }
    }

    private void populateVehicleTableData(TermLoanSecurityVehicleTO objVehicleTypeTO) throws Exception {
        setTxtVehicleMemNo(CommonUtil.convertObjToStr(objVehicleTypeTO.getMemberNo()));
        setTxtVehicleMemType(CommonUtil.convertObjToStr(objVehicleTypeTO.getMemberType()));
        setTxtVehicleMembName(CommonUtil.convertObjToStr(objVehicleTypeTO.getMemberName()));
        setTxtVehicleContactNum(CommonUtil.convertObjToStr(objVehicleTypeTO.getContactNo()));
        setTxtVehicleNo(CommonUtil.convertObjToStr(objVehicleTypeTO.getVehicleNo()));
        setTxtVehicleRcBookNo(CommonUtil.convertObjToStr(objVehicleTypeTO.getVehicleRcBookNo()));
        setTxtVehicleDetails(CommonUtil.convertObjToStr(objVehicleTypeTO.getVehichleDetails()));
        setTxtVehicleType(CommonUtil.convertObjToStr(objVehicleTypeTO.getVehicleType()));
        setTxtVehicleDate(CommonUtil.convertObjToStr(objVehicleTypeTO.getVehicleDate()));
        setTxtVehicleMemSal(objVehicleTypeTO.getMemSalary());
        setTxtVehicleNetworth(objVehicleTypeTO.getNetWorth());
    }

    public void addMemberTypeTable(int rowSelected, boolean updateMode){
        try{
            int rowSel=rowSelected;
            final TermLoanSecurityMemberTO objMemberTypeTO = new TermLoanSecurityMemberTO();
            if( memberTypeMap == null ){
                memberTypeMap = new LinkedHashMap();
            }
            
            
            if(getActionType()==ClientConstants.ACTIONTYPE_EDIT){
                if(isMemberTypeData()){
                    objMemberTypeTO.setStatusDt(curDate);
                    objMemberTypeTO.setStatusBy(TrueTransactMain.USER_ID);
                    objMemberTypeTO.setStatus(CommonConstants.STATUS_CREATED);
                }else{
                    objMemberTypeTO.setStatusDt(curDate);
                    objMemberTypeTO.setStatusBy(TrueTransactMain.USER_ID);
                    objMemberTypeTO.setStatus(CommonConstants.STATUS_MODIFIED);
                }
            }else{
                objMemberTypeTO.setStatusDt(curDate);
                objMemberTypeTO.setStatusBy(TrueTransactMain.USER_ID);
                objMemberTypeTO.setStatus(CommonConstants.STATUS_CREATED);
            }
            objMemberTypeTO.setAcctNum(getStrACNumber());
            objMemberTypeTO.setMemberNo(getTxtMemNo());
            objMemberTypeTO.setMemberName(getTxtMemName());
            objMemberTypeTO.setMemberType(getTxtMemType());
            objMemberTypeTO.setContactNo(CommonUtil.convertObjToLong(getTxtContactNum()));
            objMemberTypeTO.setNetworth(getTxtMemNetworth());
            objMemberTypeTO.setPriority(CommonUtil.convertObjToInt(getTxtMemPriority()));
            memberTypeMap.put(objMemberTypeTO.getMemberNo(),objMemberTypeTO);
            updateMemberTypeDetails(rowSel,objMemberTypeTO,updateMode);
//            ttNotifyObservers();
        }catch(Exception e){
            parseException.logException(e,true);
        }
    }
    
    public void addCollateralTable(int rowSelected, boolean updateMode){
        try{
            int rowSel=rowSelected;
            final TermLoanSecurityLandTO objTermLoanSecurityLandTO = new TermLoanSecurityLandTO();
            TermLoanSecurityLandTO obj =new TermLoanSecurityLandTO();
            if( collateralMap == null ){
                collateralMap = new LinkedHashMap();
            }
            if(rowSelected !=-1 &&(!tblCollateralDetails.getDataArrayList().isEmpty())){
                ArrayList list=(ArrayList) tblCollateralDetails.getDataArrayList().get(rowSelected);
                // obj =(TermLoanSecurityLandTO)collateralMap.get(list.get(0));
                obj =(TermLoanSecurityLandTO)collateralMap.get(list.get(0)+"_"+(rowSelected+1));
                
            }
            if(getActionType()==ClientConstants.ACTIONTYPE_EDIT){
                if(isCollateralTypeData() || (!obj.getStatus().equals(CommonConstants.STATUS_MODIFIED))){
                    objTermLoanSecurityLandTO.setStatusDt(curDate);
                    objTermLoanSecurityLandTO.setStatusBy(TrueTransactMain.USER_ID);
                    objTermLoanSecurityLandTO.setStatus(CommonConstants.STATUS_CREATED);
                }else{
                    objTermLoanSecurityLandTO.setStatusDt(curDate);
                    objTermLoanSecurityLandTO.setStatusBy(TrueTransactMain.USER_ID);
                    objTermLoanSecurityLandTO.setStatus(CommonConstants.STATUS_MODIFIED);
                }
            }else{
                objTermLoanSecurityLandTO.setStatusDt(curDate);
                objTermLoanSecurityLandTO.setStatusBy(TrueTransactMain.USER_ID);
                objTermLoanSecurityLandTO.setStatus(CommonConstants.STATUS_CREATED);
            }
            if(isRdoGahanYes())
                objTermLoanSecurityLandTO.setGahanYesNo("Y");
            else  if(isRdoGahanNo())
                objTermLoanSecurityLandTO.setGahanYesNo("N");
            else {
                objTermLoanSecurityLandTO.setGahanYesNo("");
            }
            objTermLoanSecurityLandTO.setAcctNum(getStrACNumber());
            objTermLoanSecurityLandTO.setMemberNo(getTxtOwnerMemNo());
            objTermLoanSecurityLandTO.setMemberName(getTxtOwnerMemberNname());
            objTermLoanSecurityLandTO.setDocumentNo(getTxtDocumentNo());
            objTermLoanSecurityLandTO.setDocumentType(getCboDocumentType());
            objTermLoanSecurityLandTO.setDocumentDt(getProperDateFormat(getTdtDocumentDate()));
            objTermLoanSecurityLandTO.setRegisteredOffice(getTxtRegisteredOffice());
            objTermLoanSecurityLandTO.setPledge(getCboPledge());
//            objTermLoanSecurityLandTO.setPledge(getTxtPledgeType());
            objTermLoanSecurityLandTO.setPledgeDt(getProperDateFormat(getTdtPledgeDate()));
            objTermLoanSecurityLandTO.setPledgeNo(getTxtPledgeNo());
            objTermLoanSecurityLandTO.setPledgeAmount(CommonUtil.convertObjToDouble(getTxtPledgeAmount()));
            objTermLoanSecurityLandTO.setVillage(getTxtVillage());
            objTermLoanSecurityLandTO.setSurveyNo(getTxtSurveyNo());
            objTermLoanSecurityLandTO.setTotalArea(getTxtTotalArea());
            objTermLoanSecurityLandTO.setNature(getCboNature());
            objTermLoanSecurityLandTO.setRight(getCboRight());
            objTermLoanSecurityLandTO.setRemarks(getTxtAreaParticular());
            objTermLoanSecurityLandTO.setDocGenId(getDocGenId());
            //collateralMap.put(objTermLoanSecurityLandTO.getMemberNo(),objTermLoanSecurityLandTO);
            objTermLoanSecurityLandTO.setOldSurvyNo(getOldSurvyNo());
            ////System.out.println("objTermLoanSecurityLandTO.getMemberNo()===="+objTermLoanSecurityLandTO.getMemberNo());
            ////System.out.println("getRowCoun()----88888"+getRowCoun());
           collateralMap.put(objTermLoanSecurityLandTO.getMemberNo()+"_"+(getRowCoun()),objTermLoanSecurityLandTO);
            updateCollateralDetails(rowSel,objTermLoanSecurityLandTO,updateMode);
        }catch(Exception e){
            parseException.logException(e,true);
        }
    }
    
    public boolean isTallyGahanSecurity(double sanctionAmt,boolean partiallySecuried){
        double securityAmt=0;
        ////System.out.println("CollateralMap======"+collateralMap);
        if(collateralMap !=null){
            Set set=collateralMap.keySet();
            Object obj[]=(Object[])set.toArray();
            for(int i=0;i<collateralMap.size();i++){
                TermLoanSecurityLandTO  objTermLoanSecurityLandTO =(TermLoanSecurityLandTO)collateralMap.get(obj[i]);
                securityAmt+=CommonUtil.convertObjToDouble(objTermLoanSecurityLandTO.getPledgeAmount()).doubleValue();
            }
            if(securityAmt>0 && partiallySecuried)
                return false;
            ////System.out.println("sanctionAmt=="+sanctionAmt+"securityAmt===="+securityAmt);
            if(sanctionAmt>securityAmt){
                return true;
            }else{
                return false;
            }
        }
        return false;
    }
    private void updateCollateralDetails(int rowSel,  TermLoanSecurityLandTO objTermLoanSecurityLandTO, boolean updateMode)  throws Exception{
        Object selectedRow;
        boolean rowExists = false;
        //If row already exists update it, else create a new row & append
        if(!(rowSel==-1)){
        for(int i = tblCollateralDetails.getRowCount(), j = 0; i > 0; i--,j++){
            selectedRow = ((ArrayList)tblCollateralDetails.getDataArrayList().get(j)).get(0);
            if(getTxtOwnerMemNo().equals(CommonUtil.convertObjToStr(selectedRow))){
                rowExists = true;
                ArrayList IncParRow = new ArrayList();
                ArrayList data = tblCollateralDetails.getDataArrayList();
                data.remove(rowSel);
                IncParRow.add(getTxtOwnerMemNo());
                IncParRow.add(getTxtOwnerMemberNname());
                IncParRow.add(getTxtDocumentNo());
                IncParRow.add(getTxtPledgeAmount());
                IncParRow.add(getTxtSurveyNo());
                IncParRow.add(getTxtTotalArea());
                tblCollateralDetails.insertRow(rowSel,IncParRow);
                IncParRow = null;
            }
        }
        }
        if(!rowExists){
            ArrayList IncParRow = new ArrayList();
            IncParRow.add(getTxtOwnerMemNo());
            IncParRow.add(getTxtOwnerMemberNname());
            IncParRow.add(getTxtDocumentNo());
            IncParRow.add(getTxtPledgeAmount());
            IncParRow.add(getTxtSurveyNo());
            IncParRow.add(getTxtTotalArea());
            tblCollateralDetails.insertRow(tblCollateralDetails.getRowCount(),IncParRow);
            IncParRow = null;
        }
    }
    
    public void updateCollateralJointDetails(String memberNo){
        HashMap dataMap =new HashMap();
        ArrayList totList=new ArrayList();
        dataMap.put("MEMBER_NO",memberNo);
        dataMap.put("DOCUMENT_GEN_ID",getDocGenId());
        ArrayList IncParRow=null;
        List lst=null;
        //         tblJointCollateral = new EnhancedTableModel(null, tableCollateralJointTitle);
        if(isRdoGahanYes()==true){
            lst=ClientUtil.executeQuery("getGahanJointDetailsforLoanFromGahan", dataMap);
        }else{
            lst=ClientUtil.executeQuery("getGahanJointDetailsforLoan", dataMap);
        }
        if(lst !=null && lst.size()>0){
            for(int i=0;i<lst.size();i++){
                HashMap resultMap=(HashMap)lst.get(i);
                IncParRow = new ArrayList();
                IncParRow.add(resultMap.get("CUST_ID"));
                IncParRow.add(resultMap.get("CUSTOMER"));
                IncParRow.add(resultMap.get("CONSTITUTION"));
                totList.add(IncParRow);
                //            tblJointCollateral.insertRow(tblJointCollateral.getRowCount(),IncParRow);
            }
        }
        tblJointCollateral.setDataArrayList(totList,tableCollateralJointTitle);
    }
    
    private void updateMemberTypeDetails(int rowSel,  TermLoanSecurityMemberTO objMemberTypeTO, boolean updateMode)  throws Exception{
        Object selectedRow;
        boolean rowExists = false;
        //If row already exists update it, else create a new row & append
        for(int i = tblMemberTypeDetails.getRowCount(), j = 0; i > 0; i--,j++){
            selectedRow = ((ArrayList)tblMemberTypeDetails.getDataArrayList().get(j)).get(0);
            if(getTxtMemNo().equals(CommonUtil.convertObjToStr(selectedRow))){
                rowExists = true;
                ArrayList IncParRow = new ArrayList();
                ArrayList data = tblMemberTypeDetails.getDataArrayList();
                data.remove(rowSel);
                IncParRow.add(getTxtMemNo());
                IncParRow.add(getTxtMemName());
                IncParRow.add(getTxtMemType());
                IncParRow.add(getTxtContactNum());
                IncParRow.add(getTxtMemNetworth());
                IncParRow.add(getTxtMemPriority());
                tblMemberTypeDetails.insertRow(rowSel,IncParRow);
                IncParRow = null;
            }
        }
        if(!rowExists){
            ArrayList IncParRow = new ArrayList();
            IncParRow.add(getTxtMemNo());
            IncParRow.add(getTxtMemName());
            IncParRow.add(getTxtMemType());
            IncParRow.add(getTxtContactNum());
            IncParRow.add(getTxtMemNetworth());
            IncParRow.add(getTxtMemPriority());
            tblMemberTypeDetails.insertRow(tblMemberTypeDetails.getRowCount(),IncParRow);
            IncParRow = null;
        }
    }
    
    public void populateMemberTypeDetails(String row){
        try{
            resetMemberTypeDetails();
            final TermLoanSecurityMemberTO objMemberTypeTO = (TermLoanSecurityMemberTO)memberTypeMap.get(row);
            populateMemberTableData(objMemberTypeTO);
            
        }catch(Exception e){
            parseException.logException(e,true);
        }
    }
    
    public void populateCollateralDetails(String row){
        try{
            resetCollateralDetails();
            ////System.out.println("collateralMap===="+collateralMap);
            final TermLoanSecurityLandTO objTermLoanSecurityLandTO = (TermLoanSecurityLandTO)collateralMap.get(row);
            populateCollateralTableData(objTermLoanSecurityLandTO);
            
        }catch(Exception e){
            parseException.logException(e,true);
        }
    }
  
    private void populateMemberTableData(TermLoanSecurityMemberTO objMemberTypeTO) throws Exception {
        setTxtMemNo(CommonUtil.convertObjToStr(objMemberTypeTO.getMemberNo()));
        setTxtMemType(CommonUtil.convertObjToStr(objMemberTypeTO.getMemberType()));
        setTxtMemName(CommonUtil.convertObjToStr(objMemberTypeTO.getMemberName()));
        setTxtContactNum(CommonUtil.convertObjToStr(objMemberTypeTO.getContactNo()));
        setTxtMemNetworth(CommonUtil.convertObjToStr(objMemberTypeTO.getNetworth()));
        setTxtMemPriority(CommonUtil.convertObjToStr(objMemberTypeTO.getPriority()));
    }

    public void resetVehicleTypeDetails() {
        setTxtVehicleMemNo("");
        setTxtVehicleMembName("");
        setTxtVehicleMemType("");
        setTxtVehicleMemType("");
        setTxtVehicleNo("");
        setTxtVehicleRcBookNo("");
        setTxtVehicleDate("");
        setTxtVehicleDetails("");
        setTxtVehicleType("");
        setTxtVehicleNetworth(0.0);
        setTxtVehicleMemSal(0.0);
    }
     public void deleteVehicleTableData(String val, int row) {
        if (deletedVehicleTypeMap == null) {
            deletedVehicleTypeMap = new LinkedHashMap();
        }
        TermLoanSecurityVehicleTO objVehicleTypeTO = (TermLoanSecurityVehicleTO) vehicleTypeMap.get(val);
        objVehicleTypeTO.setStatus(CommonConstants.STATUS_DELETED);
        deletedVehicleTypeMap.put(CommonUtil.convertObjToStr(tblVehicleTypeDetails.getValueAt(row, 0)), vehicleTypeMap.get(val));
        Object obj;
        obj = val;
        vehicleTypeMap.remove(val);
        tblVehicleTypeDetails.setDataArrayList(null, tableVehicleTitle);
        try {
            populateVehicleTable();
        } catch (Exception e) {
            parseException.logException(e, true);
        }
    }
    private void populateVehicleTable() throws Exception {
        ArrayList incDataList = new ArrayList();
        incDataList = new ArrayList(vehicleTypeMap.keySet());
        ArrayList addList = new ArrayList(vehicleTypeMap.keySet());
        int length = incDataList.size();
        for (int i = 0; i < length; i++) {
            ArrayList incTabRow = new ArrayList();
            TermLoanSecurityVehicleTO objVehicleTypeTo = (TermLoanSecurityVehicleTO) vehicleTypeMap.get(addList.get(i));
            IncVal.add(objVehicleTypeTo);
            if (!objVehicleTypeTo.getStatus().equals("DELETED")) {
                incTabRow.add(CommonUtil.convertObjToStr(objVehicleTypeTo.getMemberNo()));
                //  incTabRow.add(CommonUtil.convertObjToStr(objVehicleTypeTo.getMemberType()));
                incTabRow.add(CommonUtil.convertObjToStr(objVehicleTypeTo.getMemberName()));
                incTabRow.add(CommonUtil.convertObjToStr(objVehicleTypeTo.getVehicleNo()));
                //incTabRow.add(CommonUtil.convertObjToStr(objVehicleTypeTo.getVehicleType()));
                //incTabRow.add(CommonUtil.convertObjToStr(objVehicleTypeTo.getContactNo()));
                incTabRow.add(CommonUtil.convertObjToStr(objVehicleTypeTo.getVehicleRcBookNo()));
                incTabRow.add(CommonUtil.convertObjToStr(objVehicleTypeTo.getMemSalary()));
                incTabRow.add(CommonUtil.convertObjToStr(objVehicleTypeTo.getNetWorth()));
                tblVehicleTypeDetails.addRow(incTabRow);
            }
        }
    }
    private void populateCollateralTableData(TermLoanSecurityLandTO objTermLoanSecurityLandTO)  throws Exception{
       if(CommonUtil.convertObjToStr(objTermLoanSecurityLandTO.getGahanYesNo()).equals("Y"))
            setRdoGahanYes(true);
       else if(CommonUtil.convertObjToStr(objTermLoanSecurityLandTO.getGahanYesNo()).equals("N"))
           setRdoGahanNo(true);
      
        setTxtOwnerMemNo(CommonUtil.convertObjToStr(objTermLoanSecurityLandTO.getMemberNo()));
        setTxtOwnerMemberNname(CommonUtil.convertObjToStr(objTermLoanSecurityLandTO.getMemberName()));
        setTxtDocumentNo(CommonUtil.convertObjToStr(objTermLoanSecurityLandTO.getDocumentNo()));
        setCboDocumentType(CommonUtil.convertObjToStr(objTermLoanSecurityLandTO.getDocumentType()));
        setTdtDocumentDate(CommonUtil.convertObjToStr(objTermLoanSecurityLandTO.getDocumentDt()));
        setTxtRegisteredOffice(CommonUtil.convertObjToStr(objTermLoanSecurityLandTO.getRegisteredOffice()));
        setCboPledge(CommonUtil.convertObjToStr(objTermLoanSecurityLandTO.getPledge()));
//        settPledgeType(CommonUtil.convertObjToStr(objTermLoanSecurityLandTO.getPledge()));
        setTdtPledgeDate(CommonUtil.convertObjToStr(objTermLoanSecurityLandTO.getPledgeDt()));
        setTxtPledgeNo(CommonUtil.convertObjToStr(objTermLoanSecurityLandTO.getPledgeNo()));
        setTxtPledgeAmount(CommonUtil.convertObjToStr(objTermLoanSecurityLandTO.getPledgeAmount()));
        setTxtVillage(CommonUtil.convertObjToStr(objTermLoanSecurityLandTO.getVillage()));
        setTxtSurveyNo(CommonUtil.convertObjToStr(objTermLoanSecurityLandTO.getSurveyNo()));
        setTxtTotalArea(CommonUtil.convertObjToStr(objTermLoanSecurityLandTO.getTotalArea()));
        setCboNature(CommonUtil.convertObjToStr(objTermLoanSecurityLandTO.getNature()));
        setCboRight(CommonUtil.convertObjToStr(objTermLoanSecurityLandTO.getRight()));
        setTxtAreaParticular(CommonUtil.convertObjToStr(objTermLoanSecurityLandTO.getRemarks()));
        setDocGenId(objTermLoanSecurityLandTO.getDocGenId());// Added by nithya on 20-07-2018 for KD-177 gahan joint customer display issue
    }
    
    public void deleteMemberTableData(String val, int row){
        if(deletedMemberTypeMap == null){
            deletedMemberTypeMap = new LinkedHashMap();
        }
        TermLoanSecurityMemberTO objMemberTypeTO = (TermLoanSecurityMemberTO) memberTypeMap.get(val);
        objMemberTypeTO.setStatus(CommonConstants.STATUS_DELETED);
        deletedMemberTypeMap.put(CommonUtil.convertObjToStr(tblMemberTypeDetails.getValueAt(row,0)),memberTypeMap.get(val));
        Object obj;
        obj=val;
        memberTypeMap.remove(val);
        tblMemberTypeDetails.setDataArrayList(null,tableMemberTitle);
        try{
            populateMemberTable();
        }
        catch(Exception e){
            parseException.logException(e,true);
        }
    }
    
    public void deleteCollateralTableData(String val, int row){
        if(deletedCollateralMap == null){
            deletedCollateralMap = new LinkedHashMap();
        }
        TermLoanSecurityLandTO objTermLoanSecurityLandTO = (TermLoanSecurityLandTO) collateralMap.get(val);
        objTermLoanSecurityLandTO.setStatus(CommonConstants.STATUS_DELETED);
        deletedCollateralMap.put(CommonUtil.convertObjToStr(tblCollateralDetails.getValueAt(row,0)),collateralMap.get(val));
        Object obj;
        obj=val;
        //collateralMap.remove(val);
        tblCollateralDetails.setDataArrayList(null,tableCollateralTitle);
        try{
            populateCollateralTable();
            objTermLoanSecurityLandTO.setOldSurvyNo(getOldSurvyNo());
        }
        catch(Exception e){
            parseException.logException(e,true);
        }
    }
    
    private void populateMemberTable()  throws Exception{
        ArrayList incDataList = new ArrayList();
        incDataList = new ArrayList(memberTypeMap.keySet());
        ArrayList addList =new ArrayList(memberTypeMap.keySet());
        int length = incDataList.size();
        for(int i=0; i<length; i++){
            ArrayList incTabRow = new ArrayList();
            TermLoanSecurityMemberTO objMemberTypeTO = (TermLoanSecurityMemberTO) memberTypeMap.get(addList.get(i));
            IncVal.add(objMemberTypeTO);
            if(!objMemberTypeTO.getStatus().equals("DELETED")){
                incTabRow.add(CommonUtil.convertObjToStr(objMemberTypeTO.getMemberNo()));
                incTabRow.add(CommonUtil.convertObjToStr(objMemberTypeTO.getMemberType()));
                incTabRow.add(CommonUtil.convertObjToStr(objMemberTypeTO.getMemberName()));
                incTabRow.add(CommonUtil.convertObjToStr(objMemberTypeTO.getContactNo()));
                incTabRow.add(CommonUtil.convertObjToStr(objMemberTypeTO.getNetworth()));
                incTabRow.add(CommonUtil.convertObjToStr(objMemberTypeTO.getPriority()));
                tblMemberTypeDetails.addRow(incTabRow);
            }
        }
    }
    
    private void populateCollateralTable()  throws Exception{
        ////System.out.println("collateralMap in populateCollateralTable"+collateralMap);
        ArrayList incDataList = new ArrayList();
        incDataList = new ArrayList(collateralMap.keySet());
        ArrayList addList =new ArrayList(collateralMap.keySet());
        int length = incDataList.size();
        LinkedHashMap tempCollateralmp= new LinkedHashMap();
        for(int i=0; i<length; i++){
            ArrayList incTabRow = new ArrayList();
            TermLoanSecurityLandTO objTermLoanSecurityLandTO = (TermLoanSecurityLandTO) collateralMap.get(addList.get(i));
            IncVal.add(objTermLoanSecurityLandTO);
            if(!objTermLoanSecurityLandTO.getStatus().equals("DELETED")){
                incTabRow.add(CommonUtil.convertObjToStr(objTermLoanSecurityLandTO.getMemberNo()));
                incTabRow.add(CommonUtil.convertObjToStr(objTermLoanSecurityLandTO.getMemberName()));
                incTabRow.add(CommonUtil.convertObjToStr(objTermLoanSecurityLandTO.getDocumentNo()));
                incTabRow.add(CommonUtil.convertObjToStr(objTermLoanSecurityLandTO.getPledgeAmount()));
                incTabRow.add(CommonUtil.convertObjToStr(objTermLoanSecurityLandTO.getSurveyNo()));
                incTabRow.add(CommonUtil.convertObjToStr(objTermLoanSecurityLandTO.getTotalArea()));
                tblCollateralDetails.addRow(incTabRow);
                tempCollateralmp.put(objTermLoanSecurityLandTO.getMemberNo()+"_"+(i+1), objTermLoanSecurityLandTO);
            }
        }
        collateralMap= new LinkedHashMap();
        collateralMap.putAll(tempCollateralmp);
        ////System.out.println("collateralMap====for test"+collateralMap);
    }
    
    /**
     * To retrieve the data from database to populate in UI based on
     * Borrower Number, Account Number
     * @param whereMap is the HashMap having where conditions
     */
    public void populateData(HashMap whereMap, AuthorizedSignatoryOB authOB, PowerOfAttorneyOB poaOB) {
        log.info("In populateData..."+whereMap);
        System.out.println("loanType###"+loanType);
        HashMap mapData = null;
        try {
            whereMap.put("UI_PRODUCT_TYPE", "TL");
            whereMap.put("LOAN_TYPE", loanType);
            whereMap.put("PRODUCT_CATEGORY", getProductCategory());
            mapData =  (HashMap) proxy.executeQuery(whereMap, operationMap);
            log.info("proxy.executeQuery is working fine");
            if (mapData.containsKey("PaddyTO") && mapData.get("PaddyTO")!=null) {
                paddyMap = (HashMap) mapData.get("PaddyTO");
                setProductCategory("PADDY_LOAN");
            }
            if (mapData.containsKey("MDSTO") && mapData.get("MDSTO")!=null) {
                mdsMap = (HashMap) mapData.get("MDSTO");
                setProductCategory("MDS_LOAN");
            }
            populateOB(mapData);
            authOB.ttNotifyObservers();
            poaOB.ttNotifyObservers();
        } catch( Exception e ) {
            log.info("Exception caught in populateData"+e);
            parseException.logException(e,true);
            e.printStackTrace();
        }
    }
    
    /**
     * To retrieve the data from database to populate in UI based on
     * Borrower Number, Account Number
     * @param mapData is the HashMap having the values retrieved from database
     */
    private void populateOB(HashMap mapData) {
        log.info("In populateOB...");
        ArrayList sanctionList;
        ArrayList sanctionFacilityList;
        ArrayList facilityList;
        ArrayList caseList;
           
            HashMap kccNatureMap = new HashMap();
            HashMap kccMap  = new HashMap();
            kccNatureMap.put("ACT_NUM", mapData.get("ACCT_NUM"));
            List kccList = ClientUtil.executeQuery("getKccNature", kccNatureMap);
            if (kccList != null && kccList.size() > 0) {
              	kccMap=(HashMap)kccList.get(0);
               	if(kccMap.containsKey("KCC_NATURE") && kccMap.get("KCC_NATURE")!=null &&
                    kccMap.get("KCC_NATURE").equals("Y"))
                   setKccNature("Y");
              	else
                   setKccNature("N");
            }else{
                   setKccNature("N");
			}
            //System.out.println("setKccNature()"+getKccNature());
               ////System.out.println("populateOb###3"+mapData);
        // Populate the Tabs on the Basis of Borrower Number or Account Number
//        if ((mapData.get("KEY_VALUE").equals("BORROWER_NUMBER")) || (mapData.get("KEY_VALUE").equals("AUTHORIZE"))){
            // Populate the Tabs on the Basis of Borrower Number
            if (((List) mapData.get("TermLoanBorrowerTO")).size() > 0){
                // Populate the Borrower Tab if the corresponding record is existing in Database
                setBorrowerNo(((TermLoanBorrowerTO) ((List) mapData.get("TermLoanBorrowerTO")).get(0)).getBorrowNo());
                termLoanBorrowerOB.setTermLoanBorrowerTO((TermLoanBorrowerTO) ((List) mapData.get("TermLoanBorrowerTO")).get(0));
                setStatusBy(termLoanBorrowerOB.getStatusBy());
                //                setStatusBy(TrueTransactMain.USER_ID);
                setAuthorizeStatus(termLoanBorrowerOB.getAuthorizeStatus());
            }else{
                return;
            }
            // To Populate the Main Customer
            HashMap queryMap = new HashMap();
            queryMap.put("CUST_ID", termLoanBorrowerOB.getTxtCustID());
            termLoanBorrowerOB.populateBorrowerTabCustomerDetails(queryMap, false,loanType);
            if(termLoanBorrowerOB.getCboConstitution().equals("Joint Account")) {
                termLoanBorrowerOB.setTermLoanJointAcctTO((ArrayList) (mapData.get("TermLoanJointAcctTO")));
            }
            if (((List) mapData.get("TermLoanCompanyTO")).size() > 0){
                // Populate the Company Tab if the corresponding record is existing in Database
                termLoanCompanyOB.setTermLoanCompanyTO((TermLoanCompanyTO) ((List) mapData.get("TermLoanCompanyTO")).get(0));
            }
            termLoanAuthorizedSignatoryOB.setAuthorizedSignatoryTO((ArrayList) (mapData.get("AuthorizedSignatoryTO")), getBorrowerNo());
            termLoanAuthorizedSignatoryInstructionOB.setAuthorizedSignatoryInstructionTO((ArrayList) (mapData.get("AuthorizedSignatoryInstructionTO")), getBorrowerNo());
            if(mapData.containsKey("PowerAttorneyTO") && mapData.get("PowerAttorneyTO")!=null)
                termLoanPoAOB.setTermLoanPowerAttorneyTO((ArrayList) (mapData.get("PowerAttorneyTO")), getBorrowerNo());
//            sanctionList = (ArrayList) (mapData.get("TermLoanSanctionTO"));
//            sanctionFacilityList = (ArrayList) (mapData.get("TermLoanSanctionFacilityTO"));
//            setTermLoanSanctionTO(sanctionList, sanctionFacilityList);
//            tableUtilSanction_Main.setAllValues(sanctionMainAll);
//            tableUtilSanction_Main.setTableValues(sanctionMainAllTabRecords);
//            setMax_Del_San_Details_No(getBorrowerNo());
//            
//            facilityList = (ArrayList) (mapData.get("TermLoanFacilityTO"));
//            setTermLoanFacilityTO(facilityList);
            if(loanType.equals("OTHERS"))
                setShareTab(mapData);
            queryMap = null;
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
                if(mapData.containsKey("TermLoanDailyLoanSanctionTO") && mapData.get("TermLoanDailyLoanSanctionTO")!=null ){
                    setDailyLoanSanctionTO((ArrayList)mapData.get("TermLoanDailyLoanSanctionTO"));
                }
                // The return HashMap contains "sanctionNo" as key and
                // corresponding sanction number as value
                HashMap transactionMap = setTermLoanFacilityByAcctNo(facilityList);
                // To get the sanction details and sanction limit
                List resultList = (List) ClientUtil.executeQuery("getSanctionDetails", transactionMap);
                // If there is any record populate it
                if (resultList.size() > 0){
                    // Interest Maintenance tab details
                    termLoanInterestOB.setLblSancDate_2(DateUtil.getStringDate((java.util.Date) ((HashMap) resultList.get(0)).get(SANCTION_DT)));
                    termLoanInterestOB.setLblLimitAmt_2(CommonUtil.convertObjToStr(((HashMap) resultList.get(0)).get(LIMIT)));
                    //                    termLoanSecurityOB.setLimitAmount(CommonUtil.convertObjToDouble(termLoanInterestOB.getLblLimitAmt_2()).doubleValue());
                    termLoanInterestOB.setLblExpiryDate_2(DateUtil.getStringDate((java.util.Date) ((HashMap) resultList.get(0)).get(TO_DT)));
                    // Classification tab details
                    termLoanClassificationOB.setLblSanctionDate2(termLoanInterestOB.getLblSancDate_2());
                }
                objSMSSubscriptionTO=null;
                if(isMobileBanking  && mapData.containsKey("SMSSubscriptionTO") && ((List) mapData.get("SMSSubscriptionTO")).size() > 0){
                    objSMSSubscriptionTO = (SMSSubscriptionTO) ((List) mapData.get("SMSSubscriptionTO")).get(0);
                    objSMSSubscriptionTO.setStatus(CommonConstants.STATUS_MODIFIED);
                    setSMSSubscriptionTO(objSMSSubscriptionTO);
                }
                //Added By Suresh
//                if(CommonUtil.convertObjToStr(CommonConstants.SAL_REC_MODULE).equals("Y")){
                    HashMap whereMap = new HashMap();
                    whereMap.put("LOAN_NO",mapData.get("ACCT_NUM"));
                    List lockStatusList = ClientUtil.executeQuery("getLockStatusDetails", whereMap);
                    if(lockStatusList!=null && lockStatusList.size()>0){
                        whereMap = (HashMap)lockStatusList.get(0);
                        setRdoSalaryRecovery(CommonUtil.convertObjToStr(whereMap.get("SALARY_RECOVERY")));
                        setLockStatus(CommonUtil.convertObjToStr(whereMap.get("LOCK_STATUS")));
                    }
               // }
                getPLR_Rate(getLblProductID_FD_Disp());
                transactionMap = null;
                resultList = null;
                sanctionAuthorizeList = null;
                sanctionFacilityAuthorizeList = null;
            if(loanType.equals("OTHERS")) {
            }
            // Populate the Tabs on the Basis of Account Number
            termLoanRepaymentOB.resetRepaymentSchedule();
            termLoanRepaymentOB.getTblRepaymentTab().setDataArrayList(null, termLoanRepaymentOB.getRepaymentTabTitle());
            termLoanRepaymentOB.setTermLoanRepaymentTO((ArrayList) (mapData.get("TermLoanRepaymentTO")), getStrACNumber());
            ////System.out.println("datassss"+mapData.get("TermLoanRepaymentTO"));
//            if(mapData.get("TermLoanRepaymentTO")!=null){
//                ArrayList aList=(ArrayList) mapData.get("TermLoanRepaymentTO");
//                ////System.out.println("aList.size()"+aList.size());
//                for(int i=0;i<aList.size();i++){
//                  TermLoanRepaymentTO   termLoanRepaymentTO = (TermLoanRepaymentTO) aList.get(i);
//                  if(!termLoanRepaymentTO.getStatus().equals("DELETED")){
//                      ////System.out.println("termLoanRepaymentTO.getInstallType()"+termLoanRepaymentTO.getInstallType());
//                    //  setCbm
//                }
//                }
//            }
            //populate additional sanction limit
            
            termLoanAdditionalSanctionOB.resetAdditionalSanctionDetails();
            termLoanAdditionalSanctionOB.getTblPeakSanctionTab().setDataArrayList(null, termLoanAdditionalSanctionOB.getAdditionalSanctionTabTitle());
            termLoanAdditionalSanctionOB.setTermLoanAdditionalSanctionTO((ArrayList) (mapData.get("TermLoanAdditionalSanctionTO")), getStrACNumber());
            
            agriSubLimitOB.getTblSubLimit().setDataArrayList(null, agriSubLimitOB.setSubLimitTableTitle());
            agriSubLimitOB.setSubLimitTO((ArrayList) (mapData.get("TermLoanDisburstTO")),getStrACNumber());
            
            //populate mumbai details 
//            agriSubSidyOB.setSubSidyTO((ArrayList)(mapData.get("TermLoanFacilityExtnTO")),getStrACNumber());//dontdelete
//             agriSubSidyOB.notifyObservers();//dontdelete
//             
             //populate settlement ob
        if(mapData.containsKey("SettlementTO") && mapData.get("SettlementTO")!=null) {
//             settlementOB.setSettlemtnOB(mapData);//dontdelete
             }
               if(mapData.containsKey("ActTransTO") && mapData.get("ActTransTO")!=null)
//                   actTransOB.resetAcctTransfer();//dontdelete
//                    actTransOB.setActTransTO(mapData);//dontdelete
             
            // Populate the Tabs on the Basis of Account Number
            termLoanGuarantorOB.resetGuarantorDetails();
            termLoanGuarantorOB.getTblGuarantorTab().setDataArrayList(null, termLoanGuarantorOB.getGuarantorTabTitle());
            if(((ArrayList)mapData.get("TermLoanGuarantorTO")).isEmpty())
            termLoanGuarantorOB.getTblGuarantorTab().setDataArrayList(null, termLoanGuarantorOB.getInstitGuarantorTabTitle());
            termLoanGuarantorOB.setTermLoanGuarantorTO((ArrayList) (mapData.get("TermLoanGuarantorTO")), getStrACNumber());
            termLoanGuarantorOB.setTermLoanInstitGuarantorTO((ArrayList) (mapData.get("TermLoanInstitGuarantorTO")), getStrACNumber());
            // Populate the Tabs on the Basis of Account Number
            termLoanInterestOB.resetInterestDetails();
            termLoanInterestOB.getTblInterestTab().setDataArrayList(null, termLoanInterestOB.getInterestTabTitle());
            ////System.out.println("### setDetailsForLTD - depositCustDetMap 2 : "+depositCustDetMap);
            if(loanType.equals("LTD")) {
                termLoanInterestOB.setLoanType(loanType);
                //                termLoanInterestOB.setDepositNo(CommonUtil.convertObjToStr(depositCustDetMap.get("DEPOSIT_NO")));
            }
            termLoanInterestOB.setTermLoanInterestTO((ArrayList) (mapData.get("TermLoanInterestTO")), getStrACNumber());
            if (((List) mapData.get("TermLoanClassificationTO")).size() > 0){
                // Populate the Classification Tab if the corresponding record is existing in Database
                termLoanClassificationOB.setTermLoanClassificationTO((TermLoanClassificationTO) ((List) mapData.get("TermLoanClassificationTO")).get(0));
                termLoanClassificationOB.setClassifiDetails(CommonConstants.TOSTATUS_UPDATE);
            }else{
                termLoanClassificationOB.setClassifiDetails(CommonConstants.TOSTATUS_INSERT);
            }
            
            if (((List) mapData.get("TermLoanOtherDetailsTO")).size() > 0){
                // Populate the Other Details Tab if the corresponding record is existing in Database
                termLoanOtherDetailsOB.setTermLoanOtherDetailsTO((TermLoanOtherDetailsTO) ((List) mapData.get("TermLoanOtherDetailsTO")).get(0));
                termLoanOtherDetailsOB.setOtherDetailsMode(CommonConstants.TOSTATUS_UPDATE);
            }else{
                termLoanOtherDetailsOB.setOtherDetailsMode(CommonConstants.TOSTATUS_INSERT);
            }
            
            // Populate the Tabs on the Basis of Account Number
            termLoanDocumentDetailsOB.resetDocumentDetails();
            termLoanDocumentDetailsOB.getTblDocumentTab().setDataArrayList(null, termLoanDocumentDetailsOB.getDocumentTabTitle());
            termLoanDocumentDetailsOB.setTermLoanDocumentTO((ArrayList) (mapData.get("TermLoanDocumentTO")));
            
            if (((List) mapData.get("TermLoanCaseDetailTO")).size() > 0){
                if(mapData.containsKey("TermLoanCaseDetailTO")){
                    setTermLoanCaseDetailTable((ArrayList) (mapData.get("TermLoanCaseDetailTO")), getStrACNumber()); 
                }
            }

            if (((List) mapData.get("TermLoanCourtDetailsTO")).size() > 0){
                if(mapData.containsKey("TermLoanCourtDetailsTO")){
                    populateCourtDetails((ArrayList) (mapData.get("TermLoanCourtDetailsTO"))); 
                }
            }else{
                updateCourtDetails=false;
            }
            
            
            if(mapData.containsKey("memberListTO")){
                memberTypeMap = (LinkedHashMap)mapData.get("memberListTO");
                ArrayList addList =new ArrayList(memberTypeMap.keySet());
                for(int i=0;i<addList.size();i++){
                    TermLoanSecurityMemberTO  objMemberTypeTO = (TermLoanSecurityMemberTO)  memberTypeMap.get(addList.get(i));
                    objMemberTypeTO.setStatus(CommonConstants.STATUS_MODIFIED);
                    ArrayList incTabRow = new ArrayList();
                    incTabRow.add(CommonUtil.convertObjToStr(objMemberTypeTO.getMemberNo()));
                    incTabRow.add(CommonUtil.convertObjToStr(objMemberTypeTO.getMemberName()));
                    incTabRow.add(CommonUtil.convertObjToStr(objMemberTypeTO.getMemberType()));
                    incTabRow.add(CommonUtil.convertObjToStr(objMemberTypeTO.getContactNo()));
                    incTabRow.add(CommonUtil.convertObjToStr(objMemberTypeTO.getNetworth()));
                    incTabRow.add(CommonUtil.convertObjToStr(objMemberTypeTO.getPriority()));
                    tblMemberTypeDetails.addRow(incTabRow);
                }
            }
           if (mapData.containsKey("TermLoanSecurityvehicleListTO")) {
            //                objTermLoanSecuritySalaryTO = (TermLoanSecuritySalaryTO) ((List) mapData.get("TermLoanSecuritySalaryTO")).get(0);
            vehicleTypeMap = (LinkedHashMap) mapData.get("TermLoanSecurityvehicleListTO");
            ArrayList addList = new ArrayList(vehicleTypeMap.keySet());
            for (int i = 0; i < addList.size(); i++) {
                TermLoanSecurityVehicleTO objVehicleTypeTO = (TermLoanSecurityVehicleTO) vehicleTypeMap.get(addList.get(i));
                objVehicleTypeTO.setStatus(CommonConstants.STATUS_MODIFIED);
                ArrayList incTabRow = new ArrayList();
                incTabRow.add(CommonUtil.convertObjToStr(objVehicleTypeTO.getMemberNo()));
                incTabRow.add(CommonUtil.convertObjToStr(objVehicleTypeTO.getMemberName()));
                incTabRow.add(CommonUtil.convertObjToStr(objVehicleTypeTO.getVehicleNo()));
                //  incTabRow.add(CommonUtil.convertObjToStr(objVehicleTypeTO.getVehicleType()));
                // incTabRow.add(CommonUtil.convertObjToStr(objVehicleTypeTO.getContactNo()));
                incTabRow.add(CommonUtil.convertObjToStr(objVehicleTypeTO.getVehicleRcBookNo()));
                incTabRow.add(CommonUtil.convertObjToStr(objVehicleTypeTO.getMemSalary()));
                incTabRow.add(CommonUtil.convertObjToStr(objVehicleTypeTO.getNetWorth()));
                tblVehicleTypeDetails.addRow(incTabRow);
            }
        }
            if(mapData.containsKey("CollateralListTO")){
                collateralMap = (LinkedHashMap)mapData.get("CollateralListTO");
                ArrayList addList =new ArrayList(collateralMap.keySet());
                for(int i=0;i<addList.size();i++){
                    TermLoanSecurityLandTO  objTermLoanSecurityLandTO = (TermLoanSecurityLandTO)  collateralMap.get(addList.get(i));
                    objTermLoanSecurityLandTO.setStatus(CommonConstants.STATUS_MODIFIED);
                    ArrayList incTabRow = new ArrayList();
                    incTabRow.add(CommonUtil.convertObjToStr(objTermLoanSecurityLandTO.getMemberNo()));
                    incTabRow.add(CommonUtil.convertObjToStr(objTermLoanSecurityLandTO.getMemberName()));
                    incTabRow.add(CommonUtil.convertObjToStr(objTermLoanSecurityLandTO.getDocumentNo()));
                    incTabRow.add(CommonUtil.convertObjToStr(objTermLoanSecurityLandTO.getPledgeAmount()));
                    incTabRow.add(CommonUtil.convertObjToStr(objTermLoanSecurityLandTO.getSurveyNo()));
                    incTabRow.add(CommonUtil.convertObjToStr(objTermLoanSecurityLandTO.getTotalArea()));
                    tblCollateralDetails.addRow(incTabRow);
                    //jiby
                    //collateralMap.put(addList.get(i),objTermLoanSecurityLandTO);
                    collateralMap.remove(objTermLoanSecurityLandTO.getMemberNo());
                    collateralMap.put(CommonUtil.convertObjToStr(objTermLoanSecurityLandTO.getMemberNo())+"_"+(i+1),objTermLoanSecurityLandTO);
                    addPledgeAmountMap(objTermLoanSecurityLandTO.getDocumentNo(),CommonUtil.convertObjToDouble(objTermLoanSecurityLandTO.getPledgeAmount()).doubleValue());
                    
                }
            }
            
            // Added by nithya on 07-03-2020 for KD-1379
            if(mapData.containsKey("CustomerGoldStockSecurityTO")){
                LoansSecurityGoldStockTO objLoansSecurityGoldStockTO = (LoansSecurityGoldStockTO) mapData.get("CustomerGoldStockSecurityTO");
                setRdoGoldSecurityStockExists("Y");    
                setTxtGoldSecurityId(objLoansSecurityGoldStockTO.getGoldStockId());                
           }
            
            // End
            
            
            if(mapData.containsKey("TermLoanSecuritySalaryTO")){
//                objTermLoanSecuritySalaryTO = (TermLoanSecuritySalaryTO) ((List) mapData.get("TermLoanSecuritySalaryTO")).get(0);
                populateTermLoanSecuritySalaryTOData(mapData);
            }
            
        
        if(mapData.containsKey("DeositTypeDetails")){
            depositTypeMap = (LinkedHashMap)mapData.get("DeositTypeDetails");
            ArrayList addList =new ArrayList(depositTypeMap.keySet());
            for(int i=0;i<addList.size();i++){
                TermLoanDepositTypeTO  objTermLoanDepositTypeTO= (TermLoanDepositTypeTO)  depositTypeMap.get(addList.get(i));
                objTermLoanDepositTypeTO.setStatus(CommonConstants.STATUS_MODIFIED);
                ArrayList incTabRow = new ArrayList();
                
                incTabRow.add(CommonUtil.convertObjToStr(objTermLoanDepositTypeTO.getProdType()));
                incTabRow.add(CommonUtil.convertObjToStr(objTermLoanDepositTypeTO.getTxtDepNo()));
                incTabRow.add(CommonUtil.convertObjToStr(objTermLoanDepositTypeTO.getTxtDepAmount()));
                incTabRow.add(CommonUtil.convertObjToStr(objTermLoanDepositTypeTO.getTxtMaturityValue()));
                
                tblDepositTypeDetails.addRow(incTabRow);
                depositTypeMap.put(addList.get(i),objTermLoanDepositTypeTO);
                //                    addPledgeAmountMap(objTermLoanSecurityLandTO.getDocumentNo(),CommonUtil.convertObjToDouble(objTermLoanSecurityLandTO.getPledgeAmount()).doubleValue());
                
            }
        }
        
        if(mapData.containsKey("LosTypeDetails")){
            losTypeMap = (LinkedHashMap)mapData.get("LosTypeDetails");
            ArrayList addList =new ArrayList(losTypeMap.keySet());
            for(int i=0;i<addList.size();i++){
                TermLoanLosTypeTO  objTermLoanLosTypeTO= (TermLoanLosTypeTO)  losTypeMap.get(addList.get(i));
                objTermLoanLosTypeTO.setStatus(CommonConstants.STATUS_MODIFIED);
                ArrayList incTabRow = new ArrayList();
                incTabRow.add(CommonUtil.convertObjToStr(objTermLoanLosTypeTO.getLosInstitution()));
                incTabRow.add(CommonUtil.convertObjToStr(objTermLoanLosTypeTO.getLosName()));
                incTabRow.add(CommonUtil.convertObjToStr(objTermLoanLosTypeTO.getLosSecurityNo()));
                incTabRow.add(CommonUtil.convertObjToStr(objTermLoanLosTypeTO.getLosSecurityType()));
                incTabRow.add(CommonUtil.convertObjToStr(objTermLoanLosTypeTO.getLosAmount()));
                tblLosTypeDetails.addRow(incTabRow);
                losTypeMap.put(addList.get(i),objTermLoanLosTypeTO);
                
            }
        }
        
        sanctionList = null;
        sanctionFacilityList = null;
        facilityList = null;
        setChanged();
        ttNotifyObservers();
    }
    public void populateLoanApplicationData(){
        HashMap mapData=getLoanMap();
        if(mapData!=null && mapData.containsKey("memberListTO")){
            memberTypeMap = (LinkedHashMap)mapData.get("memberListTO");
            ArrayList addList =new ArrayList(memberTypeMap.keySet());
            for(int i=0;i<addList.size();i++){
                TermLoanSecurityMemberTO  objMemberTypeTO = (TermLoanSecurityMemberTO)  memberTypeMap.get(addList.get(i));
                objMemberTypeTO.setStatusDt(curDate);
                objMemberTypeTO.setStatusBy(TrueTransactMain.USER_ID);
                objMemberTypeTO.setStatus(CommonConstants.STATUS_CREATED);
                ArrayList incTabRow = new ArrayList();
                incTabRow.add(CommonUtil.convertObjToStr(objMemberTypeTO.getMemberNo()));
                incTabRow.add(CommonUtil.convertObjToStr(objMemberTypeTO.getMemberName()));
                incTabRow.add(CommonUtil.convertObjToStr(objMemberTypeTO.getMemberType()));
                incTabRow.add(CommonUtil.convertObjToStr(objMemberTypeTO.getContactNo()));
                incTabRow.add(CommonUtil.convertObjToStr(objMemberTypeTO.getNetworth()));
                incTabRow.add(CommonUtil.convertObjToStr(objMemberTypeTO.getPriority()));
                tblMemberTypeDetails.addRow(incTabRow);
            }
        }
        if (mapData.containsKey("TermLoanSecurityvehicleListTO")) {
            //                objTermLoanSecuritySalaryTO = (TermLoanSecuritySalaryTO) ((List) mapData.get("TermLoanSecuritySalaryTO")).get(0);
            vehicleTypeMap = (LinkedHashMap) mapData.get("TermLoanSecurityvehicleListTO");
            ArrayList addList = new ArrayList(vehicleTypeMap.keySet());
            for (int i = 0; i < addList.size(); i++) {
                TermLoanSecurityVehicleTO objVehicleTypeTO = (TermLoanSecurityVehicleTO) vehicleTypeMap.get(addList.get(i));
                objVehicleTypeTO.setStatus(CommonConstants.STATUS_MODIFIED);
                objVehicleTypeTO.setVehicleDate(getProperDateFormat(objVehicleTypeTO.getVehicleDate()));
                ArrayList incTabRow = new ArrayList();
                incTabRow.add(CommonUtil.convertObjToStr(objVehicleTypeTO.getMemberNo()));
                incTabRow.add(CommonUtil.convertObjToStr(objVehicleTypeTO.getMemberName()));
                incTabRow.add(CommonUtil.convertObjToStr(objVehicleTypeTO.getVehicleNo()));
                //incTabRow.add(CommonUtil.convertObjToStr(objVehicleTypeTO.getVehicleType()));
                // incTabRow.add(CommonUtil.convertObjToStr(objVehicleTypeTO.getContactNo()));
                incTabRow.add(CommonUtil.convertObjToStr(objVehicleTypeTO.getVehicleRcBookNo()));
                incTabRow.add(CommonUtil.convertObjToStr(objVehicleTypeTO.getMemSalary()));
                incTabRow.add(CommonUtil.convertObjToStr(objVehicleTypeTO.getNetWorth()));
                tblVehicleTypeDetails.addRow(incTabRow);
            }
        }
        if (mapData != null && mapData.containsKey("CollateralListTO")) {
            collateralMap = (LinkedHashMap) mapData.get("CollateralListTO");
            ArrayList addList = new ArrayList(collateralMap.keySet());
            for (int i = 0; i < addList.size(); i++) {
                TermLoanSecurityLandTO objTermLoanSecurityLandTO = (TermLoanSecurityLandTO) collateralMap.get(addList.get(i));
                objTermLoanSecurityLandTO.setStatusDt(curDate);
                objTermLoanSecurityLandTO.setStatusBy(TrueTransactMain.USER_ID);
                objTermLoanSecurityLandTO.setStatus(CommonConstants.STATUS_CREATED);
                ArrayList incTabRow = new ArrayList();
                incTabRow.add(CommonUtil.convertObjToStr(objTermLoanSecurityLandTO.getMemberNo()));
                incTabRow.add(CommonUtil.convertObjToStr(objTermLoanSecurityLandTO.getMemberName()));
                incTabRow.add(CommonUtil.convertObjToStr(objTermLoanSecurityLandTO.getDocumentNo()));
                incTabRow.add(CommonUtil.convertObjToStr(objTermLoanSecurityLandTO.getPledgeAmount()));
                incTabRow.add(CommonUtil.convertObjToStr(objTermLoanSecurityLandTO.getSurveyNo()));
                incTabRow.add(CommonUtil.convertObjToStr(objTermLoanSecurityLandTO.getTotalArea()));
                tblCollateralDetails.addRow(incTabRow);
                //test
                //collateralMap.put(addList.get(i),objTermLoanSecurityLandTO);
                collateralMap.put(CommonUtil.convertObjToStr(objTermLoanSecurityLandTO.getMemberNo()) + "_" + (i + 1), objTermLoanSecurityLandTO);
                addPledgeAmountMap(objTermLoanSecurityLandTO.getDocumentNo(), CommonUtil.convertObjToDouble(objTermLoanSecurityLandTO.getPledgeAmount()).doubleValue());

            }
        }
        if(mapData!=null && mapData.containsKey("TermLoanSecuritySalaryTO")){
            //                objTermLoanSecuritySalaryTO = (TermLoanSecuritySalaryTO) ((List) mapData.get("TermLoanSecuritySalaryTO")).get(0);
            if(salarySecurityAll ==null){
                salarySecurityAll =new LinkedHashMap();
            }
            tblSalarySecrityTable.setDataArrayList(null,salaryTitle);
            LinkedHashMap  totsalarySecurity = (LinkedHashMap)mapData.get("TermLoanSecuritySalaryTO");
            ArrayList addList =new ArrayList(totsalarySecurity.keySet());
            for(int i=0;i<addList.size();i++){
                TermLoanSecuritySalaryTO  objTermLoanSecuritySalaryTO = (TermLoanSecuritySalaryTO)  totsalarySecurity.get(addList.get(i));
//                objTermLoanSecuritySalaryTO.setStatusDt(curDate);
//                objTermLoanSecuritySalaryTO.setStatusBy(TrueTransactMain.USER_ID);
                objTermLoanSecuritySalaryTO.setStatus(CommonConstants.STATUS_CREATED);
                ArrayList incTabRow = new ArrayList();
                securitySlNo=CommonUtil.convertObjToInt(objTermLoanSecuritySalaryTO.getSlNo());
                incTabRow.add(CommonUtil.convertObjToStr(objTermLoanSecuritySalaryTO.getSlNo()));
                incTabRow.add(CommonUtil.convertObjToStr(objTermLoanSecuritySalaryTO.getSalaryCerficateNo()));
                incTabRow.add(CommonUtil.convertObjToStr(objTermLoanSecuritySalaryTO.getEmpMemberNo()));
                incTabRow.add(CommonUtil.convertObjToStr(objTermLoanSecuritySalaryTO.getEmpName()));
                
                incTabRow.add(CommonUtil.convertObjToStr(objTermLoanSecuritySalaryTO.getContactNo()));
                incTabRow.add(CommonUtil.convertObjToStr(objTermLoanSecuritySalaryTO.getNetworth()));
                
                tblSalarySecrityTable.addRow(incTabRow);
                salarySecurityAll.put(CommonUtil.convertObjToStr(objTermLoanSecuritySalaryTO.getSlNo()),objTermLoanSecuritySalaryTO);
                
            }
            
        }
        if(mapData!=null && mapData.containsKey("DeositTypeDetails")){
            depositTypeMap = (LinkedHashMap)mapData.get("DeositTypeDetails");
            ArrayList addList =new ArrayList(depositTypeMap.keySet());
            for(int i=0;i<addList.size();i++){
                TermLoanDepositTypeTO  objTermLoanDepositTypeTO= (TermLoanDepositTypeTO)  depositTypeMap.get(addList.get(i));
                objTermLoanDepositTypeTO.setStatusDt(curDate);
                objTermLoanDepositTypeTO.setStatusBy(TrueTransactMain.USER_ID);
                objTermLoanDepositTypeTO.setStatus(CommonConstants.STATUS_CREATED);
                ArrayList incTabRow = new ArrayList();
                
                incTabRow.add(CommonUtil.convertObjToStr(objTermLoanDepositTypeTO.getProdType()));
                incTabRow.add(CommonUtil.convertObjToStr(objTermLoanDepositTypeTO.getTxtDepNo()));
                incTabRow.add(CommonUtil.convertObjToStr(objTermLoanDepositTypeTO.getTxtDepAmount()));
                incTabRow.add(CommonUtil.convertObjToStr(objTermLoanDepositTypeTO.getTxtMaturityValue()));
                
                tblDepositTypeDetails.addRow(incTabRow);
                depositTypeMap.put(addList.get(i),objTermLoanDepositTypeTO);
                //                    addPledgeAmountMap(objTermLoanSecurityLandTO.getDocumentNo(),CommonUtil.convertObjToDouble(objTermLoanSecurityLandTO.getPledgeAmount()).doubleValue());
                
            }
        }
        
        if(mapData!=null && mapData.containsKey("LosTypeDetails")){
            losTypeMap = (LinkedHashMap)mapData.get("LosTypeDetails");
            ArrayList addList =new ArrayList(losTypeMap.keySet());
            for(int i=0;i<addList.size();i++){
                TermLoanLosTypeTO  objTermLoanLosTypeTO= (TermLoanLosTypeTO)  losTypeMap.get(addList.get(i));
                objTermLoanLosTypeTO.setStatusDt(curDate);
                objTermLoanLosTypeTO.setStatusBy(TrueTransactMain.USER_ID);
                objTermLoanLosTypeTO.setStatus(CommonConstants.STATUS_CREATED);
                ArrayList incTabRow = new ArrayList();
                incTabRow.add(CommonUtil.convertObjToStr(objTermLoanLosTypeTO.getLosInstitution()));
                incTabRow.add(CommonUtil.convertObjToStr(objTermLoanLosTypeTO.getLosName()));
                incTabRow.add(CommonUtil.convertObjToStr(objTermLoanLosTypeTO.getLosSecurityNo()));
                incTabRow.add(CommonUtil.convertObjToStr(objTermLoanLosTypeTO.getLosSecurityType()));
                incTabRow.add(CommonUtil.convertObjToStr(objTermLoanLosTypeTO.getLosAmount()));
                tblLosTypeDetails.addRow(incTabRow);
                losTypeMap.put(addList.get(i),objTermLoanLosTypeTO);
                
            }
        }
//        setChanged();
//        ttNotifyObservers();
    }
    private void setDailyLoanSanctionTO(ArrayList dailyLoanSanctionT0){
        try{
            if(dailyLoanSanctionT0 !=null && dailyLoanSanctionT0.size()>0){
                DailyLoanSanctionDetailsTO  objDailyLoanSanctionDetailsTO =new DailyLoanSanctionDetailsTO();
                objDailyLoanSanctionDetailsTO=(DailyLoanSanctionDetailsTO)dailyLoanSanctionT0.get(0);
                cbmAgentId.setKeyForSelected(CommonUtil.convertObjToStr(objDailyLoanSanctionDetailsTO.getAgentId()));
                cbmDirectPaymentProdType.setKeyForSelected(CommonUtil.convertObjToStr(objDailyLoanSanctionDetailsTO.getProdType()));
                if(getCbmDirectPaymentProdId() !=null)
                    cbmDirectPaymentProdId.setKeyForSelected(CommonUtil.convertObjToStr(objDailyLoanSanctionDetailsTO.getProdId()));
                setTxtDirectRepaymentAcctHead(CommonUtil.convertObjToStr(objDailyLoanSanctionDetailsTO.getAcctHead()));
                setTxtDirectRepaymentAcctNo(CommonUtil.convertObjToStr(objDailyLoanSanctionDetailsTO.getAccountNum()));
                String dirctPayment =CommonUtil.convertObjToStr(objDailyLoanSanctionDetailsTO.getDirectPayment());
                resultValue = CommonUtil.convertObjToInt(objDailyLoanSanctionDetailsTO.getLoanPeriod());
                String periodChar=CommonUtil.convertObjToStr(objDailyLoanSanctionDetailsTO.getMaxPeriodChar());
                if(periodChar.equals("DAYS")){
                    cbmDirectRepaymentLoanPeriod.setKeyForSelected(periodChar);
                    setTxtDirectRepaymentLoanPeriod(String.valueOf(resultValue));
                }else{
                    String period = setPeriod(resultValue);
                    cbmDirectRepaymentLoanPeriod.setSelectedItem(period);
                    setTxtDirectRepaymentLoanPeriod(String.valueOf(resultValue));
                }
                resetPeriod();
                setDailyLoan(true);
                if(dirctPayment.equals(YES)){
                    setDirectRepayment_Yes(true);
                }else if(dirctPayment.equals(NO)){
                    setDirectRepayment_No(true);
                }
            }
            
        }
        catch(Exception e){
            e.printStackTrace();
        }
    }
     private void setSMSSubscriptionTO(SMSSubscriptionTO objSMSSubscriptionTO) {
        setTxtMobileNo(CommonUtil.convertObjToStr(objSMSSubscriptionTO.getMobileNo()));
        setTdtMobileSubscribedFrom(DateUtil.getStringDate(objSMSSubscriptionTO.getSubscriptionDt()));
    }
    
    private void populateTermLoanSecuritySalaryTOData(HashMap mapData) {
        
        if(salarySecurityAll ==null){
            salarySecurityAll =new LinkedHashMap();
        }
        tblSalarySecrityTable.setDataArrayList(null,salaryTitle);
      LinkedHashMap  totsalarySecurity = (LinkedHashMap)mapData.get("TermLoanSecuritySalaryTO");
        ArrayList addList =new ArrayList(totsalarySecurity.keySet());
        for(int i=0;i<addList.size();i++){
            TermLoanSecuritySalaryTO  objTermLoanSecuritySalaryTO = (TermLoanSecuritySalaryTO)  totsalarySecurity.get(addList.get(i));
            objTermLoanSecuritySalaryTO.setStatus(CommonConstants.STATUS_MODIFIED);
            ArrayList incTabRow = new ArrayList();
            securitySlNo=CommonUtil.convertObjToInt(objTermLoanSecuritySalaryTO.getSlNo());
            incTabRow.add(CommonUtil.convertObjToStr(objTermLoanSecuritySalaryTO.getSlNo()));
            incTabRow.add(CommonUtil.convertObjToStr(objTermLoanSecuritySalaryTO.getSalaryCerficateNo()));
            incTabRow.add(CommonUtil.convertObjToStr(objTermLoanSecuritySalaryTO.getEmpMemberNo()));
            incTabRow.add(CommonUtil.convertObjToStr(objTermLoanSecuritySalaryTO.getEmpName()));
            
            incTabRow.add(CommonUtil.convertObjToStr(objTermLoanSecuritySalaryTO.getContactNo()));
            incTabRow.add(CommonUtil.convertObjToStr(objTermLoanSecuritySalaryTO.getNetworth()));
            
            tblSalarySecrityTable.addRow(incTabRow);
            salarySecurityAll.put(CommonUtil.convertObjToStr(objTermLoanSecuritySalaryTO.getSlNo()),objTermLoanSecuritySalaryTO);
            
          
            
        }
        
        //        salarySecurityAll
        //        this.setTxtSalaryCertificateNo(CommonUtil.convertObjToStr(objTermLoanSecuritySalaryTO.getSalaryCerficateNo()));
        //        this.setTxtEmployerName(CommonUtil.convertObjToStr(objTermLoanSecuritySalaryTO.getEmpName()));
        //        this.setTxtAddress(CommonUtil.convertObjToStr(objTermLoanSecuritySalaryTO.getEmpAddress()));
        //        this.setCboSecurityCity(CommonUtil.convertObjToStr(objTermLoanSecuritySalaryTO.getCity()));
        //        this.setTxtPinCode(CommonUtil.convertObjToStr(objTermLoanSecuritySalaryTO.getPin()));
        //        this.setTxtDesignation(CommonUtil.convertObjToStr(objTermLoanSecuritySalaryTO.getDesignation()));
        //        this.setTxtContactNo(CommonUtil.convertObjToStr(objTermLoanSecuritySalaryTO.getContactNo()));
        //        this.setTdtRetirementDt(CommonUtil.convertObjToStr(objTermLoanSecuritySalaryTO.getRetirementDt()));
        //        this.setTxtMemberNum(CommonUtil.convertObjToStr(objTermLoanSecuritySalaryTO.getEmpMemberNo()));
        //        this.setTxtTotalSalary(CommonUtil.convertObjToStr(objTermLoanSecuritySalaryTO.getTotalSalary()));
        //        this.setTxtNetWorth(CommonUtil.convertObjToStr(objTermLoanSecuritySalaryTO.getNetworth()));
        //        this.setTxtSalaryRemark(CommonUtil.convertObjToStr(objTermLoanSecuritySalaryTO.getSalaryRemarks()));
        //        setChanged();
        //        notifyObservers();
    }
    
            
    public void populateCourtDetails (ArrayList list){
        if(editCourtDetailsMap ==null)
            editCourtDetailsMap =new HashMap();
        TermLoanCourtDetailsTO  obj = new TermLoanCourtDetailsTO();
        obj =(TermLoanCourtDetailsTO)list.get(0);
        txtCourtOrderNo =obj.getCourtOrderNo();
        tdtCourtOrderDate =CommonUtil.convertObjToStr(obj.getCourtOrderDate());
        tdtOTSDate =CommonUtil.convertObjToStr(obj.getOTSDate());
        txtOTSRate =CommonUtil.convertObjToStr(obj.getOTSRate());
        txtTotAmountDue  =CommonUtil.convertObjToStr(obj.getTotAmountDue());
        txtSettlementAmt  =CommonUtil.convertObjToStr(obj.getSettlementAmt());
        txtPrincipalAmount  =CommonUtil.convertObjToDouble(obj.getPrincipalAmount()).doubleValue();
        txtInterestAmount =CommonUtil.convertObjToDouble(obj.getInterestAmount()).doubleValue();
        txtPenalInterestAmount  =CommonUtil.convertObjToDouble(obj.getPenalInterestAmount()).doubleValue();
        txtChargeAmount  =CommonUtil.convertObjToDouble(obj.getChargeAmount()).doubleValue();
        txtTotalAmountWrittenOff  =CommonUtil.convertObjToDouble(obj.getOTSRate()).doubleValue();
        txtNoInstallment =CommonUtil.convertObjToStr(obj.getInstallmentNo());
        cbmFreq.setKeyForSelected(CommonUtil.convertObjToStr(obj.getFreq()));
        txtInstallmentAmt  =CommonUtil.convertObjToStr(obj.getInstallmentAmt());
        court_slno=CommonUtil.convertObjToInt(obj.getSlno());
       
        if(CommonUtil.convertObjToStr(obj.getRepaySingle_YES()).equals("Y")){
            rdoRepaySingle_YES=true;
        }else if(CommonUtil.convertObjToStr(obj.getRepaySingle_YES()).equals("N")){
            rdoRepaySingle_NO=true;
        }else{
             rdoRepaySingle_YES=false;
             rdoRepaySingle_NO=false;
        }
       
        firstInstallmentDt=CommonUtil.convertObjToStr(obj.getFirstInstallmentDt());
        lastInstallmentDt=CommonUtil.convertObjToStr(obj.getLastInstallmentDt());
        txtPenal=CommonUtil.convertObjToStr(CommonUtil.convertObjToDouble(obj.getPenal()).doubleValue());
        courtRemarks=CommonUtil.convertObjToStr(obj.getCourtRemarks());
        updateCourtDetails=false;
        editCourtDetailsMap.put("1",obj);
        
    }
    public void setTermLoanCaseDetailTable(ArrayList caseList, String acctNum){
        try{
            TermLoanCaseDetailTO termLoanCaseTO;
            if( caseDetaillMap == null ){
                caseDetaillMap = new LinkedHashMap();
            }
            ArrayList caseRecordList;
            ArrayList tabCaseRecords = new ArrayList();
            // To retrieve the Case Details from the Serverside
            for (int i = caseList.size() - 1,j = 0;i >= 0;--i,++j){
                termLoanCaseTO = (TermLoanCaseDetailTO) caseList.get(j);
                caseRecordList = new ArrayList();
                caseRecordList.add(CommonUtil.convertObjToStr(termLoanCaseTO.getCaseStatus()));
                caseRecordList.add(CommonUtil.convertObjToStr(termLoanCaseTO.getCaseNo()));
                caseRecordList.add(CommonUtil.convertObjToStr(termLoanCaseTO.getFillingDt()));
                caseRecordList.add(CommonUtil.convertObjToStr(termLoanCaseTO.getAuthStatus()));
                tabCaseRecords.add(caseRecordList);
                termLoanCaseTO.setCommand(CommonConstants.TOSTATUS_UPDATE);
                caseDetaillMap.put(CommonUtil.convertObjToStr(termLoanCaseTO.getCaseStatus()), termLoanCaseTO);
                if(getActionType()==ClientConstants.ACTIONTYPE_AUTHORIZE && CommonUtil.convertObjToStr(termLoanCaseTO.getAuthStatus()).length()==0){
                    isCaseDetailsTrans=true;
                }
                caseRecordList = null;
            }
            caseTabValues.clear();
            caseTabValues = tabCaseRecords;
            tblCaseDetails.setDataArrayList(caseTabValues, tableTitle);
            tabCaseRecords = null;
        }catch(Exception e){
            log.info("Error in setTermLoanCaseDetailTable()..."+e);
            parseException.logException(e,true);
        }
    }            
          
      public void setAccountHead() {
        try {
            ////System.out.println("here for edit"+(String)cbmDirectPaymentProdId.getKeyForSelected());
            final HashMap accountHeadMap = new HashMap();
            String prodType =CommonUtil.convertObjToStr(cbmDirectPaymentProdType.getKeyForSelected());
             String prodId =CommonUtil.convertObjToStr(cbmDirectPaymentProdId.getKeyForSelected());
            accountHeadMap.put("PROD_ID",(String)cbmDirectPaymentProdId.getKeyForSelected());
            if(!prodType.equals("") && !prodType.equals("GL") && !prodId.equals("")){
                final List resultList = ClientUtil.executeQuery("getAccountHeadProd"+prodType, accountHeadMap);
                final HashMap resultMap = (HashMap)resultList.get(0);
                setTxtDirectRepaymentAcctHead(resultMap.get("AC_HEAD").toString());
    
//                setLblAccHdDesc(resultMap.get("AC_HEAD_DESC").toString());
//                this.setCr_cash(((String)resultMap.get("CR_CASH")==null)?" ":((String)resultMap.get("CR_CASH")));
//                this.setDr_cash(((String)resultMap.get("DR_CASH")==null)?"":((String)resultMap.get("DR_CASH")));
            }
        }catch(Exception e){
        }
    }
    
    public void addToCaseTable(int rowSelected){
        try{
            int rowSel=rowSelected;
            final TermLoanCaseDetailTO objCaseDetailTO = new TermLoanCaseDetailTO();
            if( caseDetaillMap == null ){
                caseDetaillMap = new LinkedHashMap();
            }
            if(getActionType()==ClientConstants.ACTIONTYPE_EDIT){
                if(isNewData()){
                    objCaseDetailTO.setCommand(CommonConstants.TOSTATUS_INSERT);
                    isCaseDetailsTrans=true;
                }else{
                    objCaseDetailTO.setCommand(CommonConstants.TOSTATUS_UPDATE);
                }
            }else{
                objCaseDetailTO.setCommand(CommonConstants.TOSTATUS_INSERT);
                isCaseDetailsTrans=true;
            }
            ////System.out.println("getCboCaseStatus()###"+getCboCaseStatus());
            objCaseDetailTO.setCaseStatus(CommonUtil.convertObjToStr(cbmCaseStatus.getKeyForSelected()));
            objCaseDetailTO.setCaseNo(getTxtCaseNumber());
            objCaseDetailTO.setFillingDt(getProperDateFormat(getTdtlFillingDt()));
            objCaseDetailTO.setFillingFees(getTxtFillingFees());
            objCaseDetailTO.setMiscCharges(getTxtMiscCharges());
            objCaseDetailTO.setAuthStatus(getLoanCaseAuthStatus());
//            objCaseDetailTO.setCommand(getCommand());
            caseDetaillMap.put(objCaseDetailTO.getCaseStatus(),objCaseDetailTO);
            updateCaseDetails(rowSel,objCaseDetailTO);
        }catch(Exception e){
            parseException.logException(e,true);
        }
    }
    
    private void updateCaseDetails(int rowSel, TermLoanCaseDetailTO objCaseDetailTO)  throws Exception{
        Object selectedRow;
        boolean rowExists = false;
        //If row already exists update it, else create a new row & append
        for(int i = tblCaseDetails.getRowCount(), j = 0; i > 0; i--,j++){
            selectedRow = ((ArrayList)tblCaseDetails.getDataArrayList().get(j)).get(0);
            if(getCboCaseStatus().equals(CommonUtil.convertObjToStr(selectedRow))){
                rowExists = true;
                ArrayList IncParRow = new ArrayList();
                ArrayList data = tblCaseDetails.getDataArrayList();
                data.remove(rowSel);
                IncParRow.add(getCboCaseStatus());
                IncParRow.add(getTxtCaseNumber());
                IncParRow.add(getTdtlFillingDt());
               
                IncParRow.add(getLoanCaseAuthStatus());
                tblCaseDetails.insertRow(rowSel,IncParRow);
                IncParRow = null;
            }
        }
        if(!rowExists){
            ArrayList IncParRow = new ArrayList();
            IncParRow.add(getCboCaseStatus());
            IncParRow.add(getTxtCaseNumber());
            IncParRow.add(getTdtlFillingDt());
            IncParRow.add(getLoanCaseAuthStatus());
            tblCaseDetails.insertRow(tblCaseDetails.getRowCount(),IncParRow);
            IncParRow = null;
        }
    }
    
    
    public void populateCaseDetails(String row){
        try{
            resetCaseDetails();
            final TermLoanCaseDetailTO objCaseDetailTO = (TermLoanCaseDetailTO)caseDetaillMap.get(row);
            populateTableData(objCaseDetailTO);
            
        }catch(Exception e){
            parseException.logException(e,true);
        }
    }
    
    private void populateTableData(TermLoanCaseDetailTO objCaseDetailTO)  throws Exception{
        setCboCaseStatus(CommonUtil.convertObjToStr(objCaseDetailTO.getCaseStatus()));
        setTxtCaseNumber(CommonUtil.convertObjToStr(objCaseDetailTO.getCaseNo()));
        setTdtlFillingDt(CommonUtil.convertObjToStr(objCaseDetailTO.getFillingDt()));
        setTxtFillingFees(CommonUtil.convertObjToStr(objCaseDetailTO.getFillingFees()));
        setTxtMiscCharges(CommonUtil.convertObjToStr(objCaseDetailTO.getMiscCharges()));
        setLoanCaseAuthStatus(CommonUtil.convertObjToStr(objCaseDetailTO.getAuthStatus()));
        setChanged();
        notifyObservers();
    }
    
    public void deleteCaseTableData(String val, int row){
        if(deletedCaseDetaillMap == null){
            deletedCaseDetaillMap = new LinkedHashMap();
        }
        TermLoanCaseDetailTO objCaseDetailTO = (TermLoanCaseDetailTO) caseDetaillMap.get(val);
        objCaseDetailTO.setCommand(CommonConstants.TOSTATUS_DELETE);
        deletedCaseDetaillMap.put(CommonUtil.convertObjToStr(tblCaseDetails.getValueAt(row,0)),caseDetaillMap.get(val));
        Object obj;
        obj=val;
        caseDetaillMap.remove(val);
        caseDetaillMap.put(val,objCaseDetailTO);
        resetTableValues();
        try{
            populateCaseTable();
        }
        catch(Exception e){
            parseException.logException(e,true);
        }
    }
    
    private void populateCaseTable()  throws Exception{
        ArrayList incDataList = new ArrayList();
        incDataList = new ArrayList(caseDetaillMap.keySet());
        ArrayList addList =new ArrayList(caseDetaillMap.keySet());
        int length = incDataList.size();
        for(int i=0; i<length; i++){
            ArrayList incTabRow = new ArrayList();
            TermLoanCaseDetailTO objCaseDetailTO = (TermLoanCaseDetailTO) caseDetaillMap.get(addList.get(i));
            IncVal.add(objCaseDetailTO);
            if(!objCaseDetailTO.getCommand().equals(CommonConstants.TOSTATUS_DELETE)){
                incTabRow.add(CommonUtil.convertObjToStr(objCaseDetailTO.getCaseStatus()));
                incTabRow.add(CommonUtil.convertObjToStr(objCaseDetailTO.getCaseNo()));
                incTabRow.add(CommonUtil.convertObjToStr(objCaseDetailTO.getFillingDt()));
                incTabRow.add(CommonUtil.convertObjToStr(objCaseDetailTO.getAuthStatus()));
                tblCaseDetails.addRow(incTabRow);
            }
        }
        notifyObservers();
    }
    
    public void resetTableValues(){
        tblCaseDetails.setDataArrayList(null,tableTitle);
    }
    public void resetSecurityMemberTableValues(){
        tblMemberTypeDetails.setDataArrayList(null,tableMemberTitle);
    }
    
    public void resetSecurityVehicleTableValues(){
        tblVehicleTypeDetails.setDataArrayList(null,tableVehicleTitle);
    }
    
    
    public void resetSecurityCollateralTableValues(){
        tblCollateralDetails.setDataArrayList(null,tableCollateralTitle);
    }
    
    public void resetDepositTypeTableValues(){
        tblDepositTypeDetails.setDataArrayList(null,tableTitleDepositList);
    }
    public void resetLosTypeTableValues(){
        tblLosTypeDetails.setDataArrayList(null,tableTitleLosList);
    }
    
    public void resetCaseDetails() {
        setCboCaseStatus("");
        setTxtFillingFees("");
        setTdtlFillingDt("");
        setTxtCaseNumber("");
        setTxtMiscCharges("");
        setLoanCaseAuthStatus("");
        setChanged();
        ttNotifyObservers();
    }
    
    private void setTermLoanSanctionTO(ArrayList objSanctionTO, ArrayList objSanctionFacilityTO){
        try{
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
            for (int i = objSanctionTO.size() - 1,j = 0;i >= 0;--i,++j){
                objSanction = (TermLoanSanctionTO) objSanctionTO.get(j);
                sanctionTabRec = new ArrayList();
                removedValuesSanctionFacility = new ArrayList();
                sanctionRecord = new HashMap();
                sanctionFacilityTableValues = new ArrayList();
                sanctionFacilityAllValues = new LinkedHashMap();
                
                sanctionTabRec.add(CommonUtil.convertObjToStr(objSanction.getSanctionNo()));
                sanctionTabRec.add(CommonUtil.convertObjToStr(objSanction.getSlNo()));
                sanctionTabRec.add(CommonUtil.convertObjToStr(getCbmSanctioningAuthority().getDataForKey(objSanction.getSanctionAuthority())));
                sanctionTabRec.add(CommonUtil.convertObjToStr(getCbmModeSanction().getDataForKey(objSanction.getSanctionMode())));
                sanctionMainAllTabRecords.add(sanctionTabRec);
                
                sanctionRecord.put(SANCTION_NO,CommonUtil.convertObjToStr(objSanction.getSlNo()));
                sanctionRecord.put(SANCTION_SL_NO,objSanction.getSanctionNo());
                sanctionRecord.put(SANCTION_AUTHORITY, objSanction.getSanctionAuthority());
                sanctionRecord.put(SANCTION_DATE, DateUtil.getStringDate(objSanction.getSanctionDt()));
                sanctionRecord.put(SANCTION_MODE, objSanction.getSanctionMode());
                sanctionRecord.put(REMARKS, objSanction.getRemarks());
                
                sanctionRecord.put(COMMAND, UPDATE);
                
                // To populate the RemovedValues(ArrayList) in Sanction_Facility TableUtil Object
                // for the first time only after retrieving from the Database
                sanctionRecord.put(FLAG, TRUE);
                // To retrieve the Sanction Facility Details one by one from the Databse
                for (int k = objSanctionFacilityTO.size() - 1,m = 0;k >= 0;--k,++m){
                    objSanctionFacility = (TermLoanSanctionFacilityTO) objSanctionFacilityTO.get(m);
                    sanctionNo = objSanctionFacility.getSanctionNo();
                    if (sanctionNo.equals(objSanction.getSanctionNo())){
                        // For getting the record corresponding to the Sanction Number
                        sanctionFacilityTabRec = new ArrayList();
                        sanctionFacilityLocalRecord = new HashMap();
                        
                        sanctionFacilityTabRec.add(CommonUtil.convertObjToStr(objSanctionFacility.getSlNo()));
                        sanctionFacilityTabRec.add(CommonUtil.convertObjToStr(getCbmTypeOfFacility().getDataForKey(objSanctionFacility.getFacilityType())));
                        sanctionFacilityTabRec.add(CommonUtil.convertObjToStr(objSanctionFacility.getLimit()));
                        sanctionFacilityTabRec.add(DateUtil.getStringDate(objSanctionFacility.getFromDt()));
                        sanctionFacilityTabRec.add(DateUtil.getStringDate(objSanctionFacility.getToDt()));
                        
                        sanctionFacilityLocalRecord.put(SANCTION_NO, objSanction.getSlNo());
                        sanctionFacilityLocalRecord.put(SLNO, CommonUtil.convertObjToStr(objSanctionFacility.getSlNo()));
                        sanctionFacilityLocalRecord.put(FACILITY_TYPE, objSanctionFacility.getFacilityType());
                        sanctionFacilityLocalRecord.put(LIMIT, CommonUtil.convertObjToStr(objSanctionFacility.getLimit()));
                        sanctionFacilityLocalRecord.put(INITIAL_MONEY_DEPOSIT, CommonUtil.convertObjToStr(objSanctionFacility.getInitialMoneyDeposit()));
                        
                        sanctionFacilityLocalRecord.put(FROM, DateUtil.getStringDate(objSanctionFacility.getFromDt()));
                        sanctionFacilityLocalRecord.put(TO, DateUtil.getStringDate(objSanctionFacility.getToDt()));
                        sanctionFacilityLocalRecord.put(NO_INSTALLMENTS, CommonUtil.convertObjToStr(objSanctionFacility.getNoInstall()));
                        sanctionFacilityLocalRecord.put(REPAY_FREQ, CommonUtil.convertObjToStr(objSanctionFacility.getRepaymentFrequency()));
                        sanctionFacilityLocalRecord.put(PROD_ID, CommonUtil.convertObjToStr(objSanctionFacility.getProductId()));
                        sanctionFacilityLocalRecord.put(MORATORIUM_GIVEN, CommonUtil.convertObjToStr(objSanctionFacility.getMoratoriumGiven()));
                        sanctionFacilityLocalRecord.put(FACILITY_REPAY_DATE, DateUtil.getStringDate(objSanctionFacility.getRepaymentDt()));
                        sanctionFacilityLocalRecord.put(MORATORIUM_PERIOD, CommonUtil.convertObjToStr(objSanctionFacility.getNoMoratorium()));
                        
                        sanctionFacilityLocalRecord.put(COMMAND, UPDATE);
                        
                        sanctionFacilityTableValues.add(sanctionFacilityTabRec);
                        sanctionFacilityAllValues.put(CommonUtil.convertObjToStr(objSanctionFacility.getSlNo()), sanctionFacilityLocalRecord);
                        
                        sanctionFacilityTabRec = null;
                        sanctionFacilityLocalRecord = null;
                    }
                    sanctionNo = null;
                }
                
                HashMap transactionMap = new HashMap();
                HashMap retrieve = new HashMap();
                transactionMap.put("borrowNo", getBorrowerNo());
                transactionMap.put("sanctionNo", objSanction.getSanctionNo());
                List resultList = ClientUtil.executeQuery("getSelectTermLoanSanctionFacilityMaxSLNO", transactionMap);
                if (resultList.size() > 0){
                    retrieve = (HashMap) resultList.get(0);
                    sanctionMaxDelSlNo = CommonUtil.convertObjToStr(retrieve.get("MAX_SL_NO"));
                }
                retrieve = null;
                transactionMap = null;
                resultList = null;
                
                sanctionRecord.put(SANCTION_FACILITY_DELETED, removedValuesSanctionFacility);
                sanctionRecord.put(SANCTION_FACILITY_TABLE, sanctionFacilityTableValues);
                sanctionRecord.put(SANCTION_FACILITY_ALL, sanctionFacilityAllValues);
                sanctionRecord.put(MAX_DEL_SAN_DETAIL_SL_NO, sanctionMaxDelSlNo);
                
                sanctionMainAll.put(CommonUtil.convertObjToStr(objSanction.getSanctionNo()), sanctionRecord);
                
                sanctionFacilityTableValues = null;
                sanctionFacilityAllValues = null;
                sanctionTabRec = null;
                sanctionMaxDelSlNo = null;
            }
            tblSanctionMain.setDataArrayList(sanctionMainAllTabRecords, sanctionMainTitle);
            tableUtilSanction_Main.setRemovedValues(sanctionRemovedValues);
            sanctionFacilityTabRec = null;
            sanctionRecord = null;
            sanctionFacilityLocalRecord = null;
        }catch(Exception e){
            log.info("Exception caught in setTermLoanSanctionTO: "+e);
            parseException.logException(e,true);
        }
    }
    private void setShareTab(HashMap mapData){
        
        ArrayList shareTableList=new ArrayList();
        ArrayList jointAccount=(ArrayList)mapData.get("TermLoanJointAcctTO");
        if(jointAccount !=null && jointAccount.size()>0){
            for(int i=0;i<jointAccount.size();i++){
                TermLoanJointAcctTO termloanJoint=(TermLoanJointAcctTO)jointAccount.get(i);
                String cust_id=termloanJoint.getCustId();
                shareTableList.add(getShareDetails(cust_id));
            }
        }else{
            String cust_id= termLoanBorrowerOB.getTxtCustID();
            shareTableList.add(getShareDetails(cust_id));
            
        }
        if(! ((ArrayList)shareTableList.get(0)).isEmpty()){
            ArrayList facilityList = (ArrayList) (mapData.get("TermLoanFacilityTO"));
            if(facilityList !=null && facilityList.size()>0){
                for(int t=0;t<facilityList.size();t++){
                    ArrayList finalshareTab=new ArrayList();
                    TermLoanFacilityTO  objTermLoanFacilityTO = (TermLoanFacilityTO) facilityList.get(t);
                    ArrayList actnum=new ArrayList();CommonUtil.convertObjToStr(objTermLoanFacilityTO.getAcctNum());
                    actnum.add(CommonUtil.convertObjToStr(objTermLoanFacilityTO.getAcctNum()));                    
                    for(int k=0;k<shareTableList.size();k++){
                        ArrayList list=(ArrayList)shareTableList.get(k);
                        // Added null check by nithya for joint loans authorization issue ]
                        if(list != null && list.size() > 0){
                            finalshareTab.add(list.get(0));
                            finalshareTab.add(list.get(1));
                            finalshareTab.add(actnum.get(0));
                            finalshareTab.add(list.get(2));
                            finalshareTab.add(list.get(3));
                            finalshareTab.add(list.get(4));
                            finalshareTab.add(list.get(5));
                            ////System.out.println("finalshareTab####"+finalshareTab+"shareTitle#3"+shareTitle);
                            shareTableList.set(k, finalshareTab);
                            finalshareTab = new ArrayList();
                        }  
                    }
                    tblShare.setDataArrayList(shareTableList, shareTitle);
                    actnum=new ArrayList();
                }
            }}
        mapData=new HashMap();
        shareTableList=new ArrayList();
    }
    public ArrayList getShareDetails(String Cust_id){
        HashMap shareMap =new HashMap();
        ArrayList setList=new  ArrayList();
        shareMap.put("CUST_ID",Cust_id);
        
        List share= ClientUtil.executeQuery("getShareAccInfoDataForLoan",shareMap);
        if(share !=null && share.size()>0){
            shareMap=(HashMap)share.get(0);
            double sharefee=CommonUtil.convertObjToDouble(shareMap.get("SHARE_FEE")).doubleValue();
            double shareamt=CommonUtil.convertObjToDouble(shareMap.get("SHARE_AMOUNT")).doubleValue();
            double noofShare=CommonUtil.convertObjToDouble(shareMap.get("NO_OF_SHARES")).doubleValue();
            double totShareAmt=(sharefee+shareamt)*noofShare;
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
    private void setMax_Del_San_Details_No(String borrowNo){
        try{
            HashMap transactionMap = new HashMap();
            HashMap retrieve = new HashMap();
            transactionMap.put("borrowNo", borrowNo);
            List resultList = ClientUtil.executeQuery("getSelectTermLoanSanctionMaxSLNO", transactionMap);
            if (resultList.size() > 0){
                retrieve = (HashMap) resultList.get(0);
                tableUtilSanction_Main.setStr_MAX_DEL_SL_NO(CommonUtil.convertObjToStr(retrieve.get("MAX_SL_NO")));
            }
            retrieve = null;
            transactionMap = null;
            resultList = null;
        }catch(Exception e){
            log.info("Error In setMax_Del_PoA_No: "+e);
            parseException.logException(e,true);
        }
    }
    
    private void setTermLoanSanctionTOForAuthorize(ArrayList objSanctionTOList){
        try{
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
        }catch(Exception e){
            log.info("Exception caught in setTermLoanSanctionTOForAuthorize: "+e);
            parseException.logException(e,true);
        }
    }
    
    private void setTermLoanSanctionFacilityTOForAuthorize(ArrayList objSanctionFacilityTOList){
        try{
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
            if (CommonUtil.convertObjToStr(objTermLoanSanctionFacilityTO.getMoratoriumGiven()).equals("Y")){
                setChkMoratorium_Given(true);
            }else if (CommonUtil.convertObjToStr(objTermLoanSanctionFacilityTO.getMoratoriumGiven()).equals("N")){
                setChkMoratorium_Given(false);
            }
            
             if (CommonUtil.convertObjToStr(objTermLoanSanctionFacilityTO.getEligibleAmt()).equals("Y")){
                setChkEligibleAmt(true);
            }else if (CommonUtil.convertObjToStr(objTermLoanSanctionFacilityTO.getEligibleAmt()).equals("N")){
                setChkEligibleAmt(false);
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
            //jit
            ////System.out.println("getTdtFacility"+getTdtFacility_Repay_Date());
        }catch(Exception e){
            log.info("Exception caught in setTermLoanSanctionFacilityTOForAuthorize: "+e);
            parseException.logException(e,true);
        }
    }
    
    private void setTermLoanFacilityTO(ArrayList objFacilityTOList){
        try{
            TermLoanFacilityTO objTermLoanFacilityTO;
            HashMap facilityRecord;
            LinkedHashMap allLocalRecords = new LinkedHashMap();
            int serialNo;
            // To retrieve the Facility Details from the Database
            for (int i = objFacilityTOList.size() - 1,j = 0;i >= 0;--i,++j){
                objTermLoanFacilityTO = (TermLoanFacilityTO) objFacilityTOList.get(j);
                facilityRecord = new HashMap();
                facilityRecord.put(SANCTION_NO, objTermLoanFacilityTO.getSanctionNo());
                facilityRecord.put(SLNO, CommonUtil.convertObjToStr(objTermLoanFacilityTO.getSlNo()));
                facilityRecord.put(PROD_ID, CommonUtil.convertObjToStr(objTermLoanFacilityTO.getProdId()));
                facilityRecord.put(SECURITY_TYPE, CommonUtil.convertObjToStr(objTermLoanFacilityTO.getSecurityType()));
                facilityRecord.put(SECURITY_DETAILS, CommonUtil.convertObjToStr(objTermLoanFacilityTO.getSecurityDetails()));
                facilityRecord.put(ACC_TYPE, CommonUtil.convertObjToStr(objTermLoanFacilityTO.getAccountType()));
                facilityRecord.put(INTEREST_TYPE, CommonUtil.convertObjToStr(objTermLoanFacilityTO.getInterestType()));
                facilityRecord.put(ACC_LIMIT, CommonUtil.convertObjToStr(objTermLoanFacilityTO.getAccountLimit()));
                facilityRecord.put(RISK_WEIGHT, CommonUtil.convertObjToStr(objTermLoanFacilityTO.getRiskWeight()));
                facilityRecord.put(NOTE_DATE, DateUtil.getStringDate(objTermLoanFacilityTO.getDemandPromDt()));
                facilityRecord.put(NOTE_EXP_DATE, DateUtil.getStringDate(objTermLoanFacilityTO.getDemandPromExpdt()));
                facilityRecord.put(MULTI_DISBURSE, CommonUtil.convertObjToStr(objTermLoanFacilityTO.getMultiDisburse()));
                //                facilityRecord.put(SUBSIDY, CommonUtil.convertObjToStr(objTermLoanFacilityTO.getSubsidy()));
                facilityRecord.put(AOD_DATE, DateUtil.getStringDate(objTermLoanFacilityTO.getAodDt()));
                facilityRecord.put(ACCT_STATUS, objTermLoanFacilityTO.getAcctStatus());
                facilityRecord.put(ACCT_NAME, objTermLoanFacilityTO.getAcctName());
                facilityRecord.put(ACCT_NUM, objTermLoanFacilityTO.getAcctNum());
                facilityRecord.put(PURPOSE_DESC, objTermLoanFacilityTO.getPurposeDesc());
                facilityRecord.put(GROUP_DESC, objTermLoanFacilityTO.getGroupDesc());
                facilityRecord.put(INTEREST, CommonUtil.convertObjToStr(objTermLoanFacilityTO.getInterest()));
                facilityRecord.put(CONTACT_PERSON, objTermLoanFacilityTO.getContactPerson());
                facilityRecord.put(DEALER_ID, objTermLoanFacilityTO.getDealerID());
                facilityRecord.put(SALARY_RECOVERY, objTermLoanFacilityTO.getSalaryRecovery());
                facilityRecord.put(LOCK_STATUS, objTermLoanFacilityTO.getLockStatus());
                facilityRecord.put(CONTACT_PHONE, objTermLoanFacilityTO.getContactPhone());
                facilityRecord.put(REMARKS, objTermLoanFacilityTO.getRemarks());
                facilityRecord.put(INT_GET_FROM, objTermLoanFacilityTO.getIntGetFrom());
                facilityRecord.put(AUTHORIZE_BY_1, objTermLoanFacilityTO.getAuthorizeBy1());
                facilityRecord.put(AUTHORIZE_BY_2, objTermLoanFacilityTO.getAuthorizeBy2());
                facilityRecord.put(AUTHORIZE_DT_1, objTermLoanFacilityTO.getAuthorizeDt1());
                facilityRecord.put(AUTHORIZE_DT_2, objTermLoanFacilityTO.getAuthorizeDt2());
                facilityRecord.put(AUTHORIZE_STATUS_1, objTermLoanFacilityTO.getAuthorizeStatus1());

                facilityRecord.put(AUTHORIZE_STATUS_2, objTermLoanFacilityTO.getAuthorizeStatus2());
                facilityRecord.put(RECOMMANDED_BY, objTermLoanFacilityTO.getRecommendedBy());
                facilityRecord.put(RECOMMANDED_BY2, objTermLoanFacilityTO.getRecommendedBy2());
                facilityRecord.put(KOLE_LAND_AREA, objTermLoanFacilityTO.getKoleLandArea());
                facilityRecord.put(ACCT_OPEN_DT, objTermLoanFacilityTO.getAccOpenDt());
                facilityRecord.put(ACCT_CLOSE_DT, objTermLoanFacilityTO.getAccCloseDt());
                facilityRecord.put(AVAILABLE_BALANCE, objTermLoanFacilityTO.getAvailableBalance());// ADD BY BALA
                facilityRecord.put(DRAWING_POWER, objTermLoanFacilityTO.getDpYesNo());// ADD BY BALA
                facilityRecord.put(AUTHSIGNATORY, CommonUtil.convertObjToStr(objTermLoanFacilityTO.getAuthorizedSignatory()));
                facilityRecord.put(DOCDETAILS, CommonUtil.convertObjToStr(objTermLoanFacilityTO.getDocDetails()));
                facilityRecord.put(POFATTORNEY, CommonUtil.convertObjToStr(objTermLoanFacilityTO.getPofAttorney()));
                facilityRecord.put(ACCTTRANSFER, CommonUtil.convertObjToStr(objTermLoanFacilityTO.getAcctTransfer()));
                
                //dont update while saving time
                facilityRecord.put("SHADOW_DEBIT", objTermLoanFacilityTO.getShadowDebit());
                facilityRecord.put("SHADOW_CREDIT", objTermLoanFacilityTO.getShadowCredit());
                facilityRecord.put("CLEAR_BALANCE", objTermLoanFacilityTO.getClearBalance());
                //
                facilityRecord.put("LAST_INT_CALC_DT", objTermLoanFacilityTO.getLastIntCalcDt());
                facilityRecord.put(COMMAND, UPDATE);
                serialNo = CommonUtil.convertObjToInt(objTermLoanFacilityTO.getSlNo());
                allLocalRecords.put(getFacilityKey(objTermLoanFacilityTO.getSanctionNo(), serialNo), facilityRecord);
                facilityRecord = null;
                objTermLoanFacilityTO = null;
            }
            facilityAll.clear();
            facilityAll = allLocalRecords;
            allLocalRecords = null;
        }catch(Exception e){
            log.info("Exception caught in setTermLoanFacilityTO: "+e);
            parseException.logException(e,true);
        }
    }
    
    private HashMap setTermLoanFacilityByAcctNo(ArrayList objFacilityTOList){
        HashMap returnMap = new HashMap();
        try{
            TermLoanFacilityTO objTermLoanFacilityTO;
            HashMap facilityRecord;
            //            LinkedHashMap allLocalRecords = new LinkedHashMap();
            String sanctionNo = null;
            int serialNo = -1;
            // To retrieve the Facility Details from the Database
            if (objFacilityTOList.size() > 0){
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
                facilityRecord.put(DEALER_ID, objTermLoanFacilityTO.getDealerID());//Added By Revathi.L
                facilityRecord.put(SALARY_RECOVERY, objTermLoanFacilityTO.getContactPerson());
                facilityRecord.put(LOCK_STATUS, objTermLoanFacilityTO.getContactPerson());
                facilityRecord.put(CONTACT_PHONE, objTermLoanFacilityTO.getContactPhone());
                facilityRecord.put(INT_GET_FROM, objTermLoanFacilityTO.getIntGetFrom());
                facilityRecord.put(AUTHORIZE_BY_1, objTermLoanFacilityTO.getAuthorizeBy1());
                facilityRecord.put(AUTHORIZE_BY_2, objTermLoanFacilityTO.getAuthorizeBy2());
                facilityRecord.put(AUTHORIZE_DT_1, objTermLoanFacilityTO.getAuthorizeDt1());
                facilityRecord.put(AUTHORIZE_DT_2, objTermLoanFacilityTO.getAuthorizeDt2());
                facilityRecord.put(AUTHORIZE_STATUS_1, objTermLoanFacilityTO.getAuthorizeStatus1());
                facilityRecord.put(AUTHORIZE_STATUS_2, objTermLoanFacilityTO.getAuthorizeStatus2());
                facilityRecord.put(REMARKS, objTermLoanFacilityTO.getRemarks());
                facilityRecord.put(RECOMMANDED_BY,objTermLoanFacilityTO.getRecommendedBy());
                facilityRecord.put(RECOMMANDED_BY2,objTermLoanFacilityTO.getRecommendedBy2());
                facilityRecord.put(KOLE_LAND_AREA,objTermLoanFacilityTO.getKoleLandArea());
                facilityRecord.put(ACCT_OPEN_DT,objTermLoanFacilityTO.getAccOpenDt());
                facilityRecord.put(ACCT_CLOSE_DT, objTermLoanFacilityTO.getAccCloseDt());
                facilityRecord.put(DOCDETAILS,objTermLoanFacilityTO.getDocDetails());
                facilityRecord.put(POFATTORNEY,objTermLoanFacilityTO.getPofAttorney());
                facilityRecord.put(AUTHSIGNATORY,objTermLoanFacilityTO.getAuthorizedSignatory());
                facilityRecord.put(DRAWING_POWER,objTermLoanFacilityTO.getDpYesNo());
                facilityRecord.put(ACCTTRANSFER, CommonUtil.convertObjToStr(objTermLoanFacilityTO.getAcctTransfer()));
                facilityRecord.put(AVAILABLE_BALANCE, objTermLoanFacilityTO.getAvailableBalance());
                facilityRecord.put("OTS", CommonUtil.convertObjToStr(objTermLoanFacilityTO.getOts()));
                facilityRecord.put(COMMAND, UPDATE);
                sanctionNo = objTermLoanFacilityTO.getSanctionNo();
                serialNo = CommonUtil.convertObjToInt(objTermLoanFacilityTO.getSlNo());
                setStrACNumber(objTermLoanFacilityTO.getAcctNum());
                setClearBalance(CommonUtil.convertObjToDouble(objTermLoanFacilityTO.getClearBalance()).doubleValue());
                setAvailableBalance(CommonUtil.convertObjToDouble(objTermLoanFacilityTO.getAvailableBalance()).doubleValue());
                setCboAccStatus(CommonUtil.convertObjToStr(getCbmAccStatus().getDataForKey(facilityRecord.get(ACCT_STATUS))));
                setCboRecommendedByType(CommonUtil.convertObjToStr(getCbmRecommendedByType().getDataForKey(facilityRecord.get(RECOMMANDED_BY))));
                setCboRecommendedByType2(CommonUtil.convertObjToStr(getCbmRecommendedByType().getDataForKey(facilityRecord.get(RECOMMANDED_BY2))));
                setTxtKoleLandArea(CommonUtil.convertObjToStr(objTermLoanFacilityTO.getKoleLandArea()));
                setAccountOpenDate(CommonUtil.convertObjToStr(facilityRecord.get(ACCT_OPEN_DT)));
                setAccountCloseDate(CommonUtil.convertObjToStr(facilityRecord.get(ACCT_CLOSE_DT)));
                setCboIntGetFrom(CommonUtil.convertObjToStr(getCbmIntGetFrom().getDataForKey(facilityRecord.get(INT_GET_FROM))));
                setTxtContactPerson(CommonUtil.convertObjToStr(facilityRecord.get(CONTACT_PERSON)));
                setTxtDealerID(CommonUtil.convertObjToStr(facilityRecord.get(DEALER_ID)));
                setRdoSalaryRecovery(CommonUtil.convertObjToStr(facilityRecord.get(SALARY_RECOVERY)));
                setLockStatus(CommonUtil.convertObjToStr(facilityRecord.get(LOCK_STATUS)));
                setTxtContactPhone(CommonUtil.convertObjToStr(facilityRecord.get(CONTACT_PHONE)));
                setTxtRemarks(CommonUtil.convertObjToStr(facilityRecord.get(REMARKS)));
                setTxtPurposeDesc(CommonUtil.convertObjToStr(facilityRecord.get(PURPOSE_DESC)));
                setTxtGroupDesc(CommonUtil.convertObjToStr(facilityRecord.get(GROUP_DESC)));
                setTdtDemandPromNoteDate(CommonUtil.convertObjToStr(facilityRecord.get(NOTE_DATE)));
                setTdtDemandPromNoteExpDate(CommonUtil.convertObjToStr(facilityRecord.get(NOTE_EXP_DATE)));
                setTdtAODDate(CommonUtil.convertObjToStr(facilityRecord.get(AOD_DATE)));
                
                setTxtSubsidyAmt(CommonUtil.convertObjToStr(objTermLoanFacilityTO.getSubsidyAmt()));
                setTxtSubsidyAccHead(CommonUtil.convertObjToStr(objTermLoanFacilityTO.getSubsidyAdjustAchd()));
                setTxtSubsidyAdjustedAmt(CommonUtil.convertObjToStr(objTermLoanFacilityTO.getSubsidyAdjustAmt()));
                
                setTdtSubsidyAppDt(CommonUtil.convertObjToStr(objTermLoanFacilityTO.getSubsidyDate()));
                setTxtRebateInterest_Amt(CommonUtil.convertObjToStr(objTermLoanFacilityTO.getRebateAmt()));
                setTdtRebateInterest_App_Dt(CommonUtil.convertObjToStr(objTermLoanFacilityTO.getRebateDate()));
                
                setTxtGoldRemarks(objTermLoanFacilityTO.getTxtGoldRemarks());
                setTxtGrossWeight(objTermLoanFacilityTO.getTxtGrossWeight());
                setTxtJewelleryDetails(objTermLoanFacilityTO.getTxtJewelleryDetails());
                setTxtNetWeight(objTermLoanFacilityTO.getTxtNetWeight());
                setTxtValueOfGold(CommonUtil.convertObjToStr(objTermLoanFacilityTO.getTxtValueOfGold()));
                
                
                
                if(CommonUtil.convertObjToStr(objTermLoanFacilityTO.getSubsidyAllowed()).equals("Y")){
                    setRdoSubsidy_Yes(true);
                }else if(CommonUtil.convertObjToStr(objTermLoanFacilityTO.getSubsidyAllowed()).equals("N")){
                    setRdoSubsidy_No(true);
                }
                
                if(CommonUtil.convertObjToStr(objTermLoanFacilityTO.getRebateAllowed()).equals("Y")){
                    setRdoRebateInterest_Yes(true);
                }else if(CommonUtil.convertObjToStr(objTermLoanFacilityTO.getRebateAllowed()).equals("N")){
                    setRdoRebateInterest_No(true);
                }
                
                
                if (facilityRecord.get(SECURITY_DETAILS).equals(UNSECURED)){
                    setRdoSecurityDetails_Unsec(true);
                }else if (facilityRecord.get(SECURITY_DETAILS).equals(PARTLY_SECURED)){
                    setRdoSecurityDetails_Partly(true);
                }else if (facilityRecord.get(SECURITY_DETAILS).equals(FULLY_SECURED)){
                    setRdoSecurityDetails_Fully(true);
                }
                if(CommonUtil.convertObjToStr(objTermLoanFacilityTO.getIsMobileBanking()).equals("Y")) {
                    setIsMobileBanking(true);
                }else{
                    setIsMobileBanking(false);
                }
                //od renewal purpose added
                String odRenewal=CommonUtil.convertObjToStr(objTermLoanFacilityTO.getAuthorizeStatus2());
                if(odRenewal.length()>0 && odRenewal.equals(ENHANCE)){
                    setRdoEnhance_Yes(true);
                    setRdoRenewal_Yes(false);
                }
                else if(odRenewal.length()>0 && odRenewal.equals(ODRENEWAL)){
                    setRdoRenewal_Yes(true);
                    setRdoEnhance_Yes(false);

                }else{
                    setRdoEnhance_No(true);
                    setRdoRenewal_No(true);
                }

                
                setTxtAcct_Name(CommonUtil.convertObjToStr(facilityRecord.get(ACCT_NAME)));
                if (facilityRecord.get(SECURITY_TYPE).equals(INSPECT_INSURANCE_GUARANTAR)){
                    setChkStockInspect(true);
                    setChkInsurance(true);
                    setChkGurantor(true);
                }else if (facilityRecord.get(SECURITY_TYPE).equals(STOCK_INSPECT_INSURANCE)){
                    setChkStockInspect(true);
                    setChkInsurance(true);
                }else if (facilityRecord.get(SECURITY_TYPE).equals(STOCK_INSPECT_GUARANTAR)){
                    setChkStockInspect(true);
                    setChkGurantor(true);
                }else if (facilityRecord.get(SECURITY_TYPE).equals(INSURANCE_GUARANTAR)){
                    setChkInsurance(true);
                    setChkGurantor(true);
                }else if (facilityRecord.get(SECURITY_TYPE).equals(STOCK_INSPECT)){
                    setChkStockInspect(true);
                }else if (facilityRecord.get(SECURITY_TYPE).equals(INSURANCE)){
                    setChkInsurance(true);
                }else if (facilityRecord.get(SECURITY_TYPE).equals(GUARANTAR)){
                    setChkGurantor(true);
                }
                
                if (facilityRecord.get(ACC_TYPE).equals(NEW)){
                    setRdoAccType_New(true);
                }else if (facilityRecord.get(ACC_TYPE).equals(TRANSFERED)){
                    setRdoAccType_Transfered(true);
                }
                
                if (facilityRecord.get(ACC_LIMIT).equals(MAIN)){
                    setRdoAccLimit_Main(true);
                }else if (facilityRecord.get(ACC_LIMIT).equals(SUBMIT)){
                    setRdoAccLimit_Submit(true);
                }
                
                if (facilityRecord.get(RISK_WEIGHT).equals(YES)){
                    setRdoRiskWeight_Yes(true);
                }else if (facilityRecord.get(RISK_WEIGHT).equals(NO)){
                    setRdoRiskWeight_No(true);
                }
                 if (facilityRecord.get("OTS").equals(YES)){
                    setChkOTS(true);
                }else if (facilityRecord.get("OTS").equals(NO)){
                    setChkOTS(false);
                }
                
                //                if (facilityRecord.get(INTEREST_NATURE).equals(PLR)){
                //                    setRdoNatureInterest_PLR(true);
                //                }else if (facilityRecord.get(INTEREST_NATURE).equals(NON_PLR)){
                //                    setRdoNatureInterest_NonPLR(true);
                //                }
                
                if (facilityRecord.get(MULTI_DISBURSE).equals(YES)){
                    setRdoMultiDisburseAllow_Yes(true);
                }else if (facilityRecord.get(MULTI_DISBURSE).equals(NO)){
                    setRdoMultiDisburseAllow_No(true);
                }
                
                if (facilityRecord.get(DRAWING_POWER) !=null && facilityRecord.get(DRAWING_POWER).equals(YES)){
                    setRdoDP_YES(true);
                }else if (facilityRecord.get(DRAWING_POWER) !=null && facilityRecord.get(DRAWING_POWER).equals(NO)){
                    setRdoDP__NO(true);
                }
                
                
                if (facilityRecord.get(INTEREST).equals(SIMPLE)){
                    setRdoInterest_Simple(true);
                }else if (facilityRecord.get(INTEREST).equals(COMPOUND)){
                    setRdoInterest_Compound(true);
                }
                
                 if(facilityRecord.get(ACCTTRANSFER).equals(ACCTTRANSFER))
                    setChkAcctTransfer(true);
                 else
                     setChkAcctTransfer(false);
                if (facilityRecord.containsKey(POFATTORNEY) && facilityRecord.get(POFATTORNEY)!=null && facilityRecord.get(POFATTORNEY).equals(POFATTORNEY)){
                    setChkPOFAttorney(true);
                }else
                    setChkPOFAttorney(false);
                if (facilityRecord.containsKey(AUTHSIGNATORY) && facilityRecord.get(AUTHSIGNATORY)!=null && facilityRecord.get(AUTHSIGNATORY).equals(AUTHSIGNATORY)){
                    setChkAuthorizedSignatory(true);
                }else
                    setChkAuthorizedSignatory(false);
                if (facilityRecord.containsKey(DOCDETAILS) && facilityRecord.get(DOCDETAILS)!=null && facilityRecord.get(DOCDETAILS).equals(DOCDETAILS)){
                    setChkDocDetails(true);
                }else
                    setChkDocDetails(false);
                
                //                setFacilityProdID(CommonUtil.convertObjToInt(objTermLoanFacilityTO.getSanctionNo()), CommonUtil.convertObjToInt(objTermLoanFacilityTO.getSlNo()));
                setLblProductID_FD_Disp(CommonUtil.convertObjToStr(facilityRecord.get(PROD_ID)));
                populateRestofProdId_AccHead();
                setCboInterestType(CommonUtil.convertObjToStr(getCbmInterestType().getDataForKey(CommonUtil.convertObjToStr(facilityRecord.get(INTEREST_TYPE)))));
                termLoanGuarantorOB.setLblAccNo_GD_2(objTermLoanFacilityTO.getAcctNum());
                termLoanDocumentDetailsOB.setLblAcctNo_Disp_DocumentDetails(objTermLoanFacilityTO.getAcctNum());
                termLoanClassificationOB.setLblAccNo_CD_2(objTermLoanFacilityTO.getAcctNum());
                termLoanOtherDetailsOB.setStrACNumber(objTermLoanFacilityTO.getAcctNum());
                setLblAccNo_Idetail_2(objTermLoanFacilityTO.getAcctNum());
                termLoanInterestOB.setLblAccNo_IM_2(objTermLoanFacilityTO.getAcctNum());
                getPLR_Rate(CommonUtil.convertObjToStr(facilityRecord.get(PROD_ID)));
                termLoanRepaymentOB.setLblAccNo_RS_2(objTermLoanFacilityTO.getAcctNum());
//                termLoanSecurityOB.setLblAccNoSec_2(objTermLoanFacilityTO.getAcctNum());
                termLoanOtherDetailsOB.setLblAcctNo_Disp_ODetails(objTermLoanFacilityTO.getAcctNum());
                termLoanClassificationOB.setLblSanctionNo2(sanctionNo);
                setStrACNumber(objTermLoanFacilityTO.getAcctNum());
                if(loanType.equals("LTD") && getStrACNumber().length()>0){
                    HashMap accountMap=new  HashMap();
                    accountMap.put("ACCT_NUM",getStrACNumber());
                    List lst=ClientUtil.executeQuery("getDepositbeforeAuthDetails",accountMap); //getDepositDetails
                    if(lst !=null && lst.size()>0){
                        accountMap=(HashMap)lst.get(0);
                        setLblDepositNo(CommonUtil.convertObjToStr(accountMap.get("DEPOSIT_NO")));
                    }
                }
                else
                    setLblDepositNo("");
                cbmIntGetFrom.setKeyForSelected(facilityRecord.get(INT_GET_FROM));
                facilityRecord = null;
                objTermLoanFacilityTO = null;
            }
        }catch(Exception e){
            log.info("Exception caught in setTermLoanFacilityByAcctNo: "+e);
            parseException.logException(e,true);
        }
        return returnMap;
    }
    
    /**
     * To perform the appropriate operation
     * @param saveMode is the integer value to know which save button pressed
     */
    public void doAction(int saveMode) {
        log.info("In doAction...");
        try {
            //If actionType such as NEW, EDIT, DELETE, then proceed
            if( actionType != ClientConstants.ACTIONTYPE_CANCEL ){
                //If actionType has got propervalue then doActionPerform, else throw error
                if( getCommand() != null || getAuthorizeMap() != null ){
                    log.info("before doActionPerform...");
                    doActionPerform(saveMode);
                }
                else{
                    log.info("In doAction()-->getCommand() is null:" );
                }
            }
            else
                log.info("In doAction()-->actionType is null:" );
        } catch (Exception e) {
            ////System.out.println("e--------->"+e);
            parseException.logException(e,true);
            setResult(ClientConstants.ACTIONTYPE_FAILED);
            log.info("Error in doAction():"+e);
        }
    }
    
    
    private String getCommand() throws Exception{
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
        termLoanOtherDetailsOB.setCommand(command);
        return command;
    }
    
    //interestslab rate max amount hit or not
    public  boolean checkMaxAmountRange(String toDate){
        if(getStrACNumber().length() > 0){
            StringBuffer buf=termLoanInterestOB.maxAmountRangeCheck(toDate);
            if(buf !=null && buf.length()>0) {
                ClientUtil.showMessageWindow(buf.toString());
                return false;
            }
        }
        return true;
    }
    
    private void doActionPerform(int saveMode) throws Exception{
        log.info("doActionPerform");
        boolean isnew=false;
        HashMap objDailyLoanSanctionDetailsTOMap =null;
        HashMap objOTSCourtDetailsTOMap =null;
        insertFacilityDetails(getTxtSanctionSlNo(),Integer.parseInt(getTxtSanctionSlNo()));
        HashMap objTermLoanFacilityTOMap =setTermLoanFacilitySingleRecord();// setTermLoanFacility();
        HashMap objSantionDetailsTOMap = setTermLoanSanctionSingleRecord();//setTermLoanSanction()
        HashMap objTermLoanSanctionTOMap = (HashMap) objSantionDetailsTOMap.get(SANCTION);
        HashMap objTermLoanSanctionFacilityTOMap = (HashMap) objSantionDetailsTOMap.get(SANCTION_FACILITY);
        HashMap objTermLoanJointAcctTOMap = termLoanBorrowerOB.setTermLoanJointAcct();
        TermLoanBorrowerTO objTermLoanBorrowerTO = termLoanBorrowerOB.setTermLoanBorrower();
        TermLoanCompanyTO objTermLoanCompanyTO = termLoanCompanyOB.setTermLoanCompany();
        HashMap objTermLoanAuthorizedSignatoryTOList = termLoanAuthorizedSignatoryOB.setAuthorizedSignatory();
        HashMap objTermLoanAuthorizedSignatoryInstructionTOList = termLoanAuthorizedSignatoryInstructionOB.setAuthorizedSignatoryInstruction();
        HashMap objTermLoanPowerAttorneyTOMap = termLoanPoAOB.setTermLoanPowerAttorney();
//        HashMap objExtnFacilityDetails=agriSubSidyOB.setAgriSubSidyTo();//dontdelete
        HashMap objTermLoanSubLimitTOList =  agriSubLimitOB.setAgriSubLimitTo();
        System.out.println("isDailyLoan^#^#^#^#^#^#"+isDailyLoan());
        if(isDailyLoan()){
             objDailyLoanSanctionDetailsTOMap= (HashMap)setDailyLoanSanctionDetailsTO();
        }
        if(chkOTS){
            objOTSCourtDetailsTOMap=(HashMap)setOTSCourtDetailsTOMap();
        }
//        HashMap objSettlementTOMap = settlementOB.setSettlement(saveMode);//dontdelete
//        HashMap objSettlementBankTOMap = settlementOB.setSettlementBank(saveMode);//dontdelete
//        HashMap objAcctTransTOMap = actTransOB.setAcctTrans();//dontdelete
//        HashMap objTermLoanSecurityTOMap = termLoanSecurityOB.setTermLoanSecurity();
        HashMap objTermLoanAdditionalSanMap=termLoanAdditionalSanctionOB.setAdditionalSanction();
//        if(getApopulateDisbursementDetailsctionType()==ClientConstants.ACTIONTYPE_NEW && CommonUtil.convertObjToStr(termLoanRepaymentOB.getCbmSanRepaymentType().getSelectedItem()).equals("Uniform Principle EMI")){
//            termLoanRepaymentOB.getCbmRepayType().setKeyForSelected(CommonUtil.convertObjToStr(termLoanRepaymentOB.getCbmSanRepaymentType()));
//            isnew=true;
//        }
        HashMap objRepaymentInstallmentMap = termLoanRepaymentOB.setTermLoanRepayment(isnew);
        ////System.out.println("printsavemode"+saveMode);
          ////System.out.println("objRepaymentInstallmentMap"+objRepaymentInstallmentMap);
        //        if(saveMode==1){
        //            ////System.out.println("checkthis"+termLoanRepaymentOB.getRepaymentEachRecord().get(INSTALLMENT_ALL_DETAILS));
        //         objRepaymentInstallmentAllMap=(LinkedHashMap)((HashMap)termLoanRepaymentOB.getRepaymentEachRecord()).get(INSTALLMENT_ALL_DETAILS);
        //        ////System.out.println("thisisforcheckob"+objRepaymentInstallmentAllMap);
        //        }
        HashMap objTermLoanRepaymentTOMap = (HashMap) objRepaymentInstallmentMap.get(REPAYMENT_DETAILS);
        HashMap objTermLoanInstallmentTOMap = (HashMap) objRepaymentInstallmentMap.get(INSTALLMENT_DETAILS);
        HashMap objTermLoanInstallMultIntMap = (HashMap) objRepaymentInstallmentMap.get(INSTALLMULTINT_DETAILS);
       //commented by rishad 30/10/2015 for EMI TYPE
        //////System.out.println("chk...."+objTermLoanRepaymentTOMap);
        if(objTermLoanRepaymentTOMap!=null && objTermLoanRepaymentTOMap.size()>0){
            for(int i = 0;i<objTermLoanRepaymentTOMap.size();i++){
                int cheking = i+1;
                String check = CommonUtil.convertObjToStr(cheking);
                TermLoanRepaymentTO objRepay = (TermLoanRepaymentTO) objTermLoanRepaymentTOMap.get(check);
                if (objRepay.getScheduleMode().equals("REGULAR")) {
                    if (objRepay.getInstallType().equals("EMI") && isChkDiminishing()) {
                        //objRepay.setInstallType("UNIFORM_PRINCIPLE_EMI");
                        objRepay.setEmi_uniform("Y");
                        objTermLoanRepaymentTOMap.put(check, objRepay);
                    } else {
                        objRepay.setEmi_uniform("N");
                        objTermLoanRepaymentTOMap.put(check, objRepay);
                    }
                }
            }                     
        }
        System.out.println("chk....after :: "+objTermLoanRepaymentTOMap);
         
        HashMap objTermLoanInstalltransactionMap=null;
        if(objRepaymentInstallmentMap.containsKey("INT_TRANSACTION_REPAYMENT"))
            objTermLoanInstalltransactionMap = (HashMap) objRepaymentInstallmentMap.get("INT_TRANSACTION_REPAYMENT");
        
        HashMap objTermLoanGuarantorTOMap = termLoanGuarantorOB.setTermLoanGuarantor();
        HashMap objTermLoanInstitGuarantorTOMap = termLoanGuarantorOB.setTermInsititLoanGuarantor();
        HashMap objTermLoanDocumentTOMap = termLoanDocumentDetailsOB.setTermLoanDocument();
        HashMap objTermLoanInterestTOMap = termLoanInterestOB.setTermLoanInterest();
        TermLoanClassificationTO objTermLoanClassificationTO = termLoanClassificationOB.setTermLoanClassification();
        
        TermLoanOtherDetailsTO objTermLoanOtherDetailsTO = termLoanOtherDetailsOB.setTermLoanOtherDetails();
        if (getCommand().equals(DELETE) && !canBorrowerDelete) {
            objTermLoanBorrowerTO.setCommand(UPDATE);
            objTermLoanCompanyTO.setCommand(UPDATE);
        } else {
            objTermLoanBorrowerTO.setCommand(getCommand());
            objTermLoanCompanyTO.setCommand(getCommand());
        }
        ////System.out.println("getCommand(): "+getCommand());
        if (getCommand().equals(INSERT)){
            objTermLoanBorrowerTO.setStatus(CommonConstants.STATUS_CREATED);
            objTermLoanCompanyTO.setStatus(CommonConstants.STATUS_CREATED);
        }else if (getCommand().equals(UPDATE)){
            objTermLoanBorrowerTO.setStatus(CommonConstants.STATUS_MODIFIED);
            objTermLoanCompanyTO.setStatus(CommonConstants.STATUS_MODIFIED);
        }else if (getCommand().equals(DELETE) && canBorrowerDelete){
            objTermLoanBorrowerTO.setStatus(CommonConstants.STATUS_DELETED);
            objTermLoanCompanyTO.setStatus(CommonConstants.STATUS_DELETED);
        }
        objTermLoanClassificationTO.setCommand(termLoanClassificationOB.getClassifiDetails());
        objTermLoanOtherDetailsTO.setCommand(termLoanOtherDetailsOB.getOtherDetailsMode());
        if (termLoanClassificationOB.getClassifiDetails().equals(CommonConstants.TOSTATUS_INSERT)){
            objTermLoanClassificationTO.setStatus(CommonConstants.STATUS_CREATED);
        }else if (termLoanClassificationOB.getClassifiDetails().equals(CommonConstants.TOSTATUS_UPDATE)){
            objTermLoanClassificationTO.setStatus(CommonConstants.STATUS_MODIFIED);
        }else if (termLoanClassificationOB.getClassifiDetails().equals(CommonConstants.TOSTATUS_DELETE)){
            objTermLoanClassificationTO.setStatus(CommonConstants.STATUS_DELETED);
        }
        
        if (termLoanOtherDetailsOB.getOtherDetailsMode().equals(CommonConstants.TOSTATUS_INSERT)){
            objTermLoanOtherDetailsTO.setStatus(CommonConstants.STATUS_CREATED);
        }else if (termLoanOtherDetailsOB.getOtherDetailsMode().equals(CommonConstants.TOSTATUS_UPDATE)){
            objTermLoanOtherDetailsTO.setStatus(CommonConstants.STATUS_MODIFIED);
        }else if (termLoanOtherDetailsOB.getOtherDetailsMode().equals(CommonConstants.TOSTATUS_DELETE)){
            objTermLoanOtherDetailsTO.setStatus(CommonConstants.STATUS_DELETED);
        }
        
        
        HashMap data = new HashMap();
        data.put("TermLoanJointAcctTO",  objTermLoanJointAcctTOMap);
        data.put("TermLoanBorrowerTO",objTermLoanBorrowerTO);
        data.put("TermLoanCompanyTO",objTermLoanCompanyTO);
        // Set the Networth Value and As on date
        data.put("NETWORTH_DETAILS", termLoanCompanyOB.getNetworthDetails());
        data.put("AuthorizedSignatoryTO",objTermLoanAuthorizedSignatoryTOList);
        data.put("AuthorizedSignatoryInstructionTO",objTermLoanAuthorizedSignatoryInstructionTOList);
        data.put("PowerAttorneyTO", objTermLoanPowerAttorneyTOMap);
        data.put("TermLoanSanctionTO", objTermLoanSanctionTOMap);
        data.put("TermLoanSanctionFacilityTO", objTermLoanSanctionFacilityTOMap);
        data.put("TermLoanFacilityTO", objTermLoanFacilityTOMap);
        data.put("TermLoanRepaymentTO", objTermLoanRepaymentTOMap);
        data.put("TermLoanInstallmentTO", objTermLoanInstallmentTOMap);
        data.put("TermLoanInstallMultIntTO", objTermLoanInstallMultIntMap);
        data.put("TermLoanGuarantorTO", objTermLoanGuarantorTOMap);
        data.put("TermLoanInstitGuarantorTO", objTermLoanInstitGuarantorTOMap);
        data.put("TermLoanInterestTO", objTermLoanInterestTOMap);
        data.put("TermLoanDocumentTO", objTermLoanDocumentTOMap);
        data.put("TermLoanClassificationTO", objTermLoanClassificationTO);
        data.put("TermLoanAdditionalSanctionTO",objTermLoanAdditionalSanMap);
        data.put("TermLoanDisbursementTO",objTermLoanSubLimitTOList);
        data.put("TermLoanCaseDetailsTO",caseDetaillMap);
        
         if(objOTSCourtDetailsTOMap !=null && objOTSCourtDetailsTOMap.size()>0){
             data.put("OTSCourtDetailsTO",objOTSCourtDetailsTOMap);
        }
        
        if(objDailyLoanSanctionDetailsTOMap !=null){
             data.put("DailyLoanSanctionDetailsTO",objDailyLoanSanctionDetailsTOMap);
        }
        
        data.put("DepositSecurityTableDetails",depositTypeMap);
        data.put("LosSecurityTableDetails",losTypeMap);
        data.put("SECURITY_AMT",getSecurityAmt());
        
        if(deletedDepositTypeMap != null && deletedDepositTypeMap.size()>0 ){
            data.put("deletedDepositTypeData",deletedDepositTypeMap);
        }
        
        if(deletedLosTypeMap!=null && deletedLosTypeMap.size()>0){
            data.put("deletedLosTypeData",deletedLosTypeMap);
        }
          if (objSMSSubscriptionTO!=null) {
            data.put("SMSSubscriptionTO", objSMSSubscriptionTO);
        }
//        if(getTxtSalaryCertificateNo().length()>0 || getTxtTotalSalary().length()>0 ){
//            objTermLoanSecuritySalaryTO = setTermLoanSecuritySalaryTOData();
            if(salarySecurityAll !=null &&salarySecurityAll .size()>0 )
                data.put("TermLoanSecuritySalaryTOData",salarySecurityAll);
            if(salarySecurityDeleteAll !=null && salarySecurityDeleteAll .size()>0 )
                data.put("TermLoanSecuritySalaryTODeletedData",salarySecurityDeleteAll);
//        }
        ////System.out.println("memberTypeMap memberTypeMap"+memberTypeMap);
        if(memberTypeMap !=null && memberTypeMap.size()>0){
            data.put("MemberTableDetails",memberTypeMap);
        }
        if(deletedMemberTypeMap != null && deletedMemberTypeMap.size()>0 ){
            data.put("deletedMemberTypeData",deletedMemberTypeMap);
        }
         //added by rishad 24/03/2016 for ading vehicle security RBI
          if (vehicleTypeMap != null && vehicleTypeMap.size() > 0) {
           data.put("VehicleTableDetails", vehicleTypeMap);
        }
        if (deletedVehicleTypeMap != null && deletedVehicleTypeMap.size() > 0) {
            data.put("deletedVehicleTypeData", deletedVehicleTypeMap);
        }
        if(collateralMap!=null && collateralMap.size()>0){
            data.put("CollateralTableDetails",collateralMap);
        }
        if(deletedCollateralMap!= null && deletedCollateralMap.size()>0 ){
            data.put("deletedCollateralTypeData",deletedCollateralMap);
        }
//            if(deletedCaseDetaillMap!=null && deletedCaseDetaillMap.size()>0 ){
//                data.put("TermLoanDeletedCaseDetailsTO",deletedCaseDetaillMap);
//            }
//        data.put("ActTransTO", objAcctTransTOMap);//dontdelete
//        data.put("SettlementTO", objSettlementTOMap);//dontdelete
//        data.put("SettlementBankTO", objSettlementBankTOMap);//dontdelete
        
//        if(objExtnFacilityDetails !=null && objExtnFacilityDetails.size()>0)
//            data.put("TermLoanExtenFacilityDetailsTO",objExtnFacilityDetails);//dontdelete
        if(objTermLoanInstalltransactionMap !=null)
            data.put("INT_TRANSACTION_REPAYMENT",objTermLoanInstalltransactionMap);
        if(partReject.length()>0)
            data.put("PARTIALLY_REJECT","PARTIALLY_REJECT");
        if(advanceLiablityMap !=null && (!advanceLiablityMap.isEmpty()))
            data.put("AdvancesLiablityExceedLimit",advanceLiablityMap);
        if(getCommand().equals("UPDATE")){
            //commented by rishad 
            //data.put("EDIT_TRANS_PART",editModeTransDetail());
            if(termLoanClassificationOB.getListAssetStatus() !=null)
                
                data.put("NPAHISTORY",termLoanClassificationOB.getListAssetStatus());
        }
        //        if(saveMode==1){
        //        data.put("TermRepaymentInstallmentAllTO",objRepaymentInstallmentAllMap);
        //        ////System.out.println("goingDAO"+data.get("TermRepaymentInstallmentAllTO"));
        //        }
        
        String facilityType = CommonUtil.convertObjToStr(getCbmTypeOfFacility().getKeyForSelected());
        if (facilityType.equals("CC") || facilityType.equals("OD")){
            data.put("TermLoanOtherDetailsTO", objTermLoanOtherDetailsTO);
        }else{
            data.put("TermLoanOtherDetailsTO", null);
        }
        
        if (getCommand().equals(CommonConstants.TOSTATUS_INSERT)) {
            if (loanType.equals("LTD")) {
                data.put("LTD",depositCustDetMap);
            }
        }
        if (loanType.equals("LTD")) {
            data.put("LOAN_TYPE","LTD");
            data.put("PRODUCT_CATEGORY", getProductCategory());
            data.put("KEYVALUE", getKeyValueForPaddyAndMDSLoans());
        } else {
            data.put("LOAN_TYPE","");
        }
        if (getAuthorizeMap() != null){
            data.put(CommonConstants.AUTHORIZEMAP, getAuthorizeMap());
        }
        HashMap KccRenewalTOMap = new HashMap();
        System.out.println("getKccNature"+getKccNature());
        if(getKccNature().equals("Y")){
            System.out.println("kccFlag####"+isKccFlag());
            if(isKccFlag()){
                KccRenewalTOMap = insertKccRenewalDetail();
                if(KccRenewalTOMap !=null){
                    data.put("KccRenewalTO",KccRenewalTOMap.get("KCC_RENEWAL"));
                }
            }
        }
        if(getNewTransactionMap() != null ){
            data.put("Transaction Details Data", getNewTransactionMap());
        }
        if(getSuspenceAccountNo() != null && !getSuspenceAccountNo().equals("") && getSuspenceProductID() != null && !getSuspenceProductID().equals("")){
            data.put("SUSPENCE_ACCT_NO", getSuspenceAccountNo());
            data.put("SUSPENCE_PROD_ID", getSuspenceProductID());
        }
        if(getChargelst() != null && getChargelst().size()>0){
            data.put("Charge List Data", getChargelst());
        }
        data.put(CommonConstants.MODULE, getModule());
        data.put(CommonConstants.SCREEN, getScreen());
        data.put("UI_PRODUCT_TYPE", "TL");
        data.put("BALANCE_SHARE",getBalanceShareAmt());//vivek
//        ////System.out.println("getChkRecovery>>>>"+getChkRecovery());
//        if(getChkRecovery()!=null && getChkRecovery()=="Y"){
//         data.put("RECOVERY", "Y");   
//        }else{
//         data.put("RECOVERY", "N");   
//        }
        data.put("CASHIER_AUTH_ALLOWED",TrueTransactMain.CASHIER_AUTH_ALLOWED);//added by babu
        ////System.out.print("dataonly#######"+data);
        HashMap proxyResultMap = new HashMap();
//        if(true)
//            return;
        //System.out.println("getDelIntFlag ---->"+getDelIntFlag());
        data.put("INT_GET_FROM",getCboIntGetFrom());
        data.put("DELETE_INT_FLAG", getDelIntFlag());
          if (getServiceTax_Map() != null && getServiceTax_Map().size() > 0) {
            data.put("serviceTaxDetails", getServiceTax_Map());
            data.put("serviceTaxDetailsTO", setServiceTaxDetails());
        }
        // Added by nithya on 07-03-2020 for KD-1379
          if(rdoGoldSecurityStockExists.equalsIgnoreCase("Y")){
            LoansSecurityGoldStockTO objCustomerGoldStockSecurityTO = getCustomerGoldStockSecurityTO();
            data.put("CUST_GOLD_SECURITY_STOCK", "CUST_GOLD_SECURITY_STOCK");
            data.put("CustomerGoldStockSecurityTO",objCustomerGoldStockSecurityTO);
         }
        // end 
        setIsTransacton(true);
        proxyResultMap = proxy.execute(data,operationMap);
        setProxyReturnMap(proxyResultMap);
        ////System.out.println("proxyresultmap#$$$$$"+proxyResultMap);
        if (proxyResultMap!=null) {
            if(proxyResultMap.containsKey("ACCTNO") && loanType.equals("OTHERS") ||
            getProductCategory().equals("PADDY_LOAN") || getProductCategory().equals("MDS_LOAN"))
                ClientUtil.showMessageWindow("Loan Account No. : "+proxyResultMap.get("ACCTNO"));
            String accNo = CommonUtil.convertObjToStr(proxyResultMap.get("ACCTNO"));
            setStrACNumber(accNo);                   

            if (proxyResultMap.size() > 0) {
                if (proxyResultMap.containsKey("LIENNO")) {
                    String lienNo = CommonUtil.convertObjToStr(proxyResultMap.get("LIENNO"));
                    loanACNo = CommonUtil.convertObjToStr(proxyResultMap.get("ACCTNO"));
                    //                setStrACNumber(loanACNo);
                    
                    HashMap lienMap = new HashMap();
                    if(depositCustDetMap!= null){
                        ClientUtil.showMessageWindow("<html>Loan Account No. : <b>"+loanACNo
                        + "</b><br><br>Lien Marked for this Loan<br>Lien No : <b>" + lienNo
                        + "</b><br>The Lien will be opened for Edit...");
                        
                        lienMap.put("CUSTOMER_NAME",depositCustDetMap.get("CUSTOMER"));
                        lienMap.put("PRODID",depositCustDetMap.get("PRODUCT ID"));
                        lienMap.put("DEPOSIT_ACT_NUM",depositCustDetMap.get("DEPOSIT_NO"));
                        lienMap.put("SUBNO",CommonUtil.convertObjToInt(depositCustDetMap.get("DEPOSIT_SUB_NO")));
                        lienMap.put("CUST_ID",depositCustDetMap.get("CUST_ID"));
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
                    }else{
                        ClientUtil.showMessageWindow("Lien Marked for this Loan Lien No  is: " + lienNo);                       
                    }
                }
            }
        }
        log.info("doActionPerform is over...");
        setResult(actionType);
        if (saveMode == 1){
            actionType = ClientConstants.ACTIONTYPE_CANCEL;
        }else if (saveMode == 2 || saveMode == 3 || saveMode == 4){
            actionType = ClientConstants.ACTIONTYPE_EDIT;
        }
        
        // If the actionType is not Failed then change the INSERT Command as UPDATE
        if (getResult() != 4){
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
            termLoanAuthorizedSignatoryOB.changeStatusAuthorizedSignatory(getResult());
            // Authorized Signatory Instruction Tab
            termLoanAuthorizedSignatoryInstructionOB.changeStatusAuthorizedSignatoryInstruction(getResult());
            // Power of Attorney DetaiLOANS_SANCTION_DETAILSls
            termLoanPoAOB.changeStatusPoA(getResult());
            // Sanction Details
            if (getResult() != 2){
                //If the Main Save Button pressed
                tableUtilSanction_Main.getRemovedValues().clear();
                tableUtilSanction_Facility.getRemovedValues().clear();
            }
            keySet =  sanctionMainAll.keySet();
            objKeySet = (Object[]) keySet.toArray();
            // To change the Insert command to Update after Save Buttone Pressed
            // Sanction Details
            for (int i = keySet.size() - 1,j = 0;i >= 0;--i,++j){
                oneRecord           = (HashMap) sanctionMainAll.get(objKeySet[j]);
                sanctionTabFacility = (LinkedHashMap) oneRecord.get(SANCTION_FACILITY_ALL);
                sanctionKeySet      = sanctionTabFacility.keySet();
                objSanctionKeySet   = (Object[]) sanctionKeySet.toArray();
                if (oneRecord.get(COMMAND).equals(INSERT)){
                    // If the status is in Insert Mode then change the mode to Update
                    oneRecord.put(COMMAND, UPDATE);
                }
                // To change the Insert command to Update after Save Buttone Pressed
                // Sanction Facility Details
                for (int k = sanctionKeySet.size() - 1, m = 0;k >= 0;--k,++m){
                    oneRec = (HashMap) sanctionTabFacility.get(objSanctionKeySet[m]);
                    if (oneRec.get(COMMAND).equals(INSERT)){
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
            if (getResult() != 2){
                //If the Main Save Button pressed
                removedFaccilityTabValues.clear();
            }
            keySet =  facilityAll.keySet();
            objKeySet = (Object[]) keySet.toArray();
            // To change the Insert command to Update after Save Buttone Pressed
            // Facility Details
            for (int i = keySet.size() - 1,j = 0;i >= 0;--i,++j){
                oneRecord = (HashMap) facilityAll.get(objKeySet[j]);
                if (oneRecord.get(COMMAND).equals(INSERT)){
                    // If the status is in Insert Mode then change the mode to Update
                    oneRecord.put(COMMAND, UPDATE);
                    facilityAll.put(objKeySet[j], oneRecord);
                }
                oneRecord = null;
            }
            // Security Details
//            termLoanSecurityOB.changeStatusSecurity(getResult());
            // Repayment Details
            termLoanRepaymentOB.changeStatusRepay(getResult());
            // Guarantor Details
            termLoanGuarantorOB.changeStatusGuarantor(getResult());
            // Document Details
            termLoanDocumentDetailsOB.changeStatusDocument(getResult());
            // Interest Details
            termLoanInterestOB.changeStatusInterest(getResult());
            termLoanClassificationOB.setClassifiDetails(UPDATE);
            termLoanOtherDetailsOB.setOtherDetailsMode(UPDATE);
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
            objRepaymentInstallmentMap = null;
            objTermLoanRepaymentTOMap = null;
            objTermLoanInstallmentTOMap = null;
            objTermLoanGuarantorTOMap = null;
            objTermLoanInstitGuarantorTOMap=null;
            objTermLoanDocumentTOMap = null;
            objTermLoanInstallMultIntMap = null;
            objTermLoanInterestTOMap = null;
            objTermLoanClassificationTO = null;
            objTermLoanOtherDetailsTO = null;
            objRepaymentInstallmentAllMap=null;
            data = null;
            caseDetaillMap = null;
            chargelst = null;
        }
    }
    
    
    public HashMap setOTSCourtDetailsTOMap(){
       TermLoanCourtDetailsTO objTermLoanCourtDetailsTO =new TermLoanCourtDetailsTO();
        HashMap returnMap =new HashMap();
        if(editCourtDetailsMap !=null && editCourtDetailsMap.size()>0){
            if(updateCourtDetails==true){
            objTermLoanCourtDetailsTO=insertingOTS();
            objTermLoanCourtDetailsTO.setCommand(CommonConstants.TOSTATUS_UPDATE);
            objTermLoanCourtDetailsTO.setStatus(CommonConstants.STATUS_MODIFIED);
            }
        }else if(deleteCourtDetailsMap !=null && deleteCourtDetailsMap.size()>0){
//             court_slno++;
//             objTermLoanCourtDetailsTO=insertingOTS();
             objTermLoanCourtDetailsTO.setAcctNum(getStrACNumber());
             objTermLoanCourtDetailsTO.setCommand(CommonConstants.TOSTATUS_DELETE);
             objTermLoanCourtDetailsTO.setStatus(CommonConstants.STATUS_DELETED);
             
             
        }else{
             court_slno++;
             objTermLoanCourtDetailsTO=insertingOTS();
             objTermLoanCourtDetailsTO.setCommand(CommonConstants.TOSTATUS_INSERT);
             objTermLoanCourtDetailsTO.setStatus(CommonConstants.STATUS_CREATED);
             
             
        }
        if(objTermLoanCourtDetailsTO !=null){
        objTermLoanCourtDetailsTO.setSlno(String.valueOf(court_slno));
        returnMap.put(String.valueOf(court_slno),objTermLoanCourtDetailsTO); 
        }
         if(deleteCourtDetailsMap !=null && deleteCourtDetailsMap.size()>0){
             Set set =deleteCourtDetailsMap.keySet();
             Object obj[]=(Object[])set.toArray();
             objTermLoanCourtDetailsTO=(TermLoanCourtDetailsTO)deleteCourtDetailsMap.get(obj[0]);
            returnMap.put(String.valueOf(objTermLoanCourtDetailsTO.getSlno()),objTermLoanCourtDetailsTO); 
        }
        ////System.out.println ("returnMap###"+returnMap);
        return returnMap;
    }
    
    public TermLoanCourtDetailsTO insertingOTS(){
        TermLoanCourtDetailsTO obj =new TermLoanCourtDetailsTO();
        obj.setAcctNum(getStrACNumber());
        obj.setCourtOrderNo(txtCourtOrderNo);
        obj.setCourtOrderDate(getProperDateFormat(tdtCourtOrderDate));
        obj.setOTSDate(getProperDateFormat(tdtOTSDate));
        obj.setOTSRate(new Double(txtOTSRate));
        obj.setPrincipalAmount(new Double(txtPrincipalAmount));
        obj.setSettlementAmt(new Double(txtSettlementAmt));
        obj.setChargeAmount(new Double(txtChargeAmount));
        obj.setFirstInstallmentDt(getProperDateFormat(firstInstallmentDt));
        obj.setLastInstallmentDt(getProperDateFormat(lastInstallmentDt));
        obj.setFreq(CommonUtil.convertObjToStr(cbmFreq.getKeyForSelected()));
        obj.setInstallmentAmt(new Double(txtInstallmentAmt));
        obj.setInstallmentNo(txtNoInstallment);
        obj.setCourtRemarks(courtRemarks);
        obj.setInterestAmount(new Double(txtInterestAmount));
        obj.setPenalInterestAmount(new Double(txtPenalInterestAmount));
        obj.setPenal(new Double(txtPenal));
        if(rdoRepaySingle_YES)
            obj.setRepaySingle_YES(YES);
        else
            obj.setRepaySingle_YES(NO);
//        
        obj.setTotAmountDue(new Double(txtTotAmountDue));



        return obj;
        
    }
    public HashMap setDailyLoanSanctionDetailsTO(){
        HashMap dailyMap =new HashMap();
        try{
        
        DailyLoanSanctionDetailsTO objDailyLoanSanctionDetailsTO =new DailyLoanSanctionDetailsTO();
        objDailyLoanSanctionDetailsTO.setAgentId(CommonUtil.convertObjToStr(cbmAgentId.getKeyForSelected()));
        if(isDirectRepayment_Yes()==true)
        objDailyLoanSanctionDetailsTO.setDirectPayment(YES);
        else if(isDirectRepayment_No()==true)
        objDailyLoanSanctionDetailsTO.setDirectPayment(NO);
        else
            objDailyLoanSanctionDetailsTO.setDirectPayment("");
        
//        objDailyLoanSanctionDetailsTO.setLoanPeriod(loanPeriod
        
        objDailyLoanSanctionDetailsTO.setProdType(CommonUtil.convertObjToStr(cbmDirectPaymentProdType.getKeyForSelected()));
        objDailyLoanSanctionDetailsTO.setProdId(CommonUtil.convertObjToStr(cbmDirectPaymentProdId.getKeyForSelected()));
        objDailyLoanSanctionDetailsTO.setAcctHead(getTxtDirectRepaymentAcctHead());
        objDailyLoanSanctionDetailsTO.setAccountNum(getTxtDirectRepaymentAcctNo());
        
        String repayPeriod=CommonUtil.convertObjToStr(cbmDirectRepaymentLoanPeriod.getKeyForSelected());
        objDailyLoanSanctionDetailsTO.setMaxPeriodChar(repayPeriod);
        String repayPeriodDays=CommonUtil.convertObjToStr(txtDirectRepaymentLoanPeriod);
        if(repayPeriod.length() > 0 && repayPeriodDays.length()>0){
                duration = repayPeriod;
                periodData = setCombo(duration);
                resultData = periodData * Double.parseDouble(repayPeriodDays);
                objDailyLoanSanctionDetailsTO.setLoanPeriod(new Double (resultData));
         }
        if(getActionType()==ClientConstants.ACTIONTYPE_NEW){
            objDailyLoanSanctionDetailsTO.setLoanAcctNum("_");
            objDailyLoanSanctionDetailsTO.setStatus(CommonConstants.STATUS_CREATED);
            objDailyLoanSanctionDetailsTO.setCommand("INSERT");
        }else if(getActionType()==ClientConstants.ACTIONTYPE_EDIT){
            objDailyLoanSanctionDetailsTO.setLoanAcctNum(getStrACNumber());
            objDailyLoanSanctionDetailsTO.setStatus(CommonConstants.STATUS_MODIFIED);
            objDailyLoanSanctionDetailsTO.setCommand("UPDATE");
        }
       
  
        dailyMap.put("1",objDailyLoanSanctionDetailsTO);
        }catch(Exception e){
            e.printStackTrace();
        }
          return dailyMap;
    }
    /** To populate data into the screen */
    public TermLoanSecuritySalaryTO setTermLoanSecuritySalaryTOData() {
        
        final TermLoanSecuritySalaryTO objTermLoanSecuritySalaryTO = new TermLoanSecuritySalaryTO();
        try{
            objTermLoanSecuritySalaryTO.setAcctNum(getStrACNumber());
            objTermLoanSecuritySalaryTO.setSalaryCerficateNo(getTxtSalaryCertificateNo());
            objTermLoanSecuritySalaryTO.setEmpName(getTxtEmployerName());
            objTermLoanSecuritySalaryTO.setEmpAddress(getTxtAddress());
            objTermLoanSecuritySalaryTO.setCity(getCboSecurityCity());
            objTermLoanSecuritySalaryTO.setPin(CommonUtil.convertObjToDouble(getTxtPinCode()));
            objTermLoanSecuritySalaryTO.setDesignation(getTxtDesignation());
            objTermLoanSecuritySalaryTO.setContactNo(CommonUtil.convertObjToLong(getTxtContactNo()));
            System.out.println("date retirement ==="+getDateFormat(getTdtRetirementDt()));
            objTermLoanSecuritySalaryTO.setRetirementDt(getDateFormat(getTdtRetirementDt()));
            objTermLoanSecuritySalaryTO.setEmpMemberNo(getTxtMemberNum());
            objTermLoanSecuritySalaryTO.setTotalSalary(CommonUtil.convertObjToDouble(getTxtTotalSalary()));
            objTermLoanSecuritySalaryTO.setNetworth(getTxtNetWorth());
            objTermLoanSecuritySalaryTO.setSalaryRemarks(getTxtSalaryRemark());
        }catch(Exception e){
            log.info("Error In setTermLoanSecuritySalaryTOData()");
            e.printStackTrace();
        }
        return objTermLoanSecuritySalaryTO;
    }
    
    public Date getDateFormat(String d)
    {
      Date s=null;
        try
        {
        java.text.SimpleDateFormat dt= new java.text.SimpleDateFormat("MM/dd/yyyy");
        s = dt.parse(d);
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
        return s;
    }
    private HashMap setTermLoanSanction(){
        HashMap returnValue = new HashMap();
        try{
            TermLoanSanctionTO objTermLoanSanctionTO;
            TermLoanSanctionFacilityTO objTermLoanSanctionFacilityTO;
            HashMap sanctionMap = new HashMap();
            HashMap sanctionFacilityMap = new HashMap();
            HashMap eachSanctionRec;
            LinkedHashMap sanctionFacilityRecs;
            HashMap eachSanctionFacilityRec;
            int sanctionFacilityKey = 0;
            Set keySet =  sanctionMainAll.keySet();
            Object[] objKeySet = (Object[]) keySet.toArray();
            Set sanctionFacilityKeySet;
            Object[] objSanctionFacilityKeySet;
            
            boolean isSanctionDeleted = false;
            
            // To set the values for Sanction Transfer Object
            for (int i = sanctionMainAll.size() - 1, j = 0;i >= 0;--i,++j){
                eachSanctionRec = (HashMap) sanctionMainAll.get(objKeySet[j]);
                objTermLoanSanctionTO = new TermLoanSanctionTO();
                objTermLoanSanctionTO.setBorrowNo(borrowerNo);
                objTermLoanSanctionTO.setSanctionNo(CommonUtil.convertObjToStr( eachSanctionRec.get(SANCTION_SL_NO)));
                objTermLoanSanctionTO.setSlNo(CommonUtil.convertObjToStr( eachSanctionRec.get(SANCTION_NO)));
                objTermLoanSanctionTO.setSanctionAuthority(CommonUtil.convertObjToStr( eachSanctionRec.get(SANCTION_AUTHORITY)));
                
                //                objTermLoanSanctionTO.setSanctionDt((Date)eachSanctionRec.get(SANCTION_DATE));
                objTermLoanSanctionTO.setSanctionDt(getProperDateFormat(eachSanctionRec.get(SANCTION_DATE)));
                objTermLoanSanctionTO.setSanctionMode(CommonUtil.convertObjToStr( eachSanctionRec.get(SANCTION_MODE)));
                objTermLoanSanctionTO.setRemarks(CommonUtil.convertObjToStr( eachSanctionRec.get(REMARKS)));
                objTermLoanSanctionTO.setCommand(CommonUtil.convertObjToStr( eachSanctionRec.get(COMMAND)));
                if (eachSanctionRec.get(COMMAND).equals(INSERT)){
                    objTermLoanSanctionTO.setStatus(CommonConstants.STATUS_CREATED);
                }else if (eachSanctionRec.get(COMMAND).equals(UPDATE)){
                    objTermLoanSanctionTO.setStatus(CommonConstants.STATUS_MODIFIED);
                }
                objTermLoanSanctionTO.setStatusBy(TrueTransactMain.USER_ID);
                objTermLoanSanctionTO.setStatusDt(curDate);
                sanctionFacilityRecs = (LinkedHashMap) eachSanctionRec.get(SANCTION_FACILITY_ALL);
                if (getCommand().equals(CommonConstants.TOSTATUS_DELETE))
                    if (sanctionFacilityRecs.size()==1)
                        if (objTermLoanSanctionTO.getSanctionNo().equals(getTxtSanctionSlNo()) &&
                        objTermLoanSanctionTO.getSlNo().equals(getTxtSanctionNo())) {
                            objTermLoanSanctionTO.setStatus(CommonConstants.STATUS_DELETED);
                            objTermLoanSanctionTO.setCommand(getCommand());
                            isSanctionDeleted = true;
                        }
                
                sanctionFacilityKeySet =  sanctionFacilityRecs.keySet();
                objSanctionFacilityKeySet = (Object[]) sanctionFacilityKeySet.toArray();
                // To set the values for Sanction Facility Transfer Object
                for (int k = sanctionFacilityRecs.size() - 1, m = 0;k >= 0;--k,++m){
                    eachSanctionFacilityRec = (HashMap) sanctionFacilityRecs.get(objSanctionFacilityKeySet[m]);
                    objTermLoanSanctionFacilityTO = new TermLoanSanctionFacilityTO();
                    sanctionFacilityKey++;
                    objTermLoanSanctionFacilityTO.setBorrowNo(borrowerNo);
                    //                    objTermLoanSanctionFacilityTO.setSanctionNo(CommonUtil.convertObjToStr( eachSanctionFacilityRec.get(SANCTION_NO)));
                    objTermLoanSanctionFacilityTO.setSanctionNo(objTermLoanSanctionTO.getSanctionNo());
                    objTermLoanSanctionFacilityTO.setSlNo(CommonUtil.convertObjToDouble( eachSanctionFacilityRec.get(SLNO)));
                    objTermLoanSanctionFacilityTO.setFacilityType(CommonUtil.convertObjToStr( eachSanctionFacilityRec.get(FACILITY_TYPE)));
                    objTermLoanSanctionFacilityTO.setLimit(CommonUtil.convertObjToDouble(eachSanctionFacilityRec.get(LIMIT)));
                    
                    //                    objTermLoanSanctionFacilityTO.setFromDt((Date)eachSanctionFacilityRec.get(FROM));
                    objTermLoanSanctionFacilityTO.setFromDt(getProperDateFormat(eachSanctionFacilityRec.get(FROM)));
                    
                    //                    objTermLoanSanctionFacilityTO.setToDt((Date)eachSanctionFacilityRec.get(TO));
                    objTermLoanSanctionFacilityTO.setToDt(getProperDateFormat(eachSanctionFacilityRec.get(TO)));
                    objTermLoanSanctionFacilityTO.setNoInstall(CommonUtil.convertObjToDouble(eachSanctionFacilityRec.get(NO_INSTALLMENTS)));
                    objTermLoanSanctionFacilityTO.setRepaymentFrequency(CommonUtil.convertObjToDouble(eachSanctionFacilityRec.get(REPAY_FREQ)));
                    objTermLoanSanctionFacilityTO.setProductId(CommonUtil.convertObjToStr( eachSanctionFacilityRec.get(PROD_ID)));
                    objTermLoanSanctionFacilityTO.setMoratoriumGiven(CommonUtil.convertObjToStr(eachSanctionFacilityRec.get(MORATORIUM_GIVEN)));
                    
                    //                    objTermLoanSanctionFacilityTO.setRepaymentDt((Date)eachSanctionFacilityRec.get(FACILITY_REPAY_DATE));
                    objTermLoanSanctionFacilityTO.setRepaymentDt(getProperDateFormat(eachSanctionFacilityRec.get(FACILITY_REPAY_DATE)));
                    objTermLoanSanctionFacilityTO.setNoMoratorium(CommonUtil.convertObjToDouble(eachSanctionFacilityRec.get(MORATORIUM_PERIOD)));
                    objTermLoanSanctionFacilityTO.setCommand(CommonUtil.convertObjToStr( eachSanctionFacilityRec.get(COMMAND)));
                    if (eachSanctionFacilityRec.get(COMMAND).equals(INSERT)){
                        objTermLoanSanctionFacilityTO.setStatus(CommonConstants.STATUS_CREATED);
                    }else if (eachSanctionFacilityRec.get(COMMAND).equals(UPDATE)){
                        objTermLoanSanctionFacilityTO.setStatus(CommonConstants.STATUS_MODIFIED);
                    }
                    if (getCommand().equals(CommonConstants.TOSTATUS_DELETE))
                        
                        if (objTermLoanSanctionFacilityTO.getSanctionNo().equals(getTxtSanctionSlNo()) &&
                        objTermLoanSanctionFacilityTO.getSlNo().doubleValue() == CommonUtil.convertObjToDouble(getSanctionSlNo()).doubleValue()) {
                            objTermLoanSanctionFacilityTO.setStatus(CommonConstants.STATUS_DELETED);
                            objTermLoanSanctionFacilityTO.setCommand(getCommand());
                        }
                    objTermLoanSanctionFacilityTO.setStatusBy(TrueTransactMain.USER_ID);
                    objTermLoanSanctionFacilityTO.setStatusDt(curDate);
                    if (objTermLoanSanctionFacilityTO.getSlNo().doubleValue()==slNoForSanction) {
                    sanctionFacilityMap.put(String.valueOf(sanctionFacilityKey), objTermLoanSanctionFacilityTO);
                    }
                    eachSanctionFacilityRec = null;
                    objTermLoanSanctionFacilityTO = null;
                }
                
                if (CommonUtil.convertObjToStr(eachSanctionRec.get(SANCTION_SL_NO)).equals(getTxtSanctionSlNo()) || getTxtSanctionSlNo().length()==0) {
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
            for (int i = tableUtilSanction_Main.getRemovedValues().size() - 1,j = 0;i >= 0;--i,++j){
                eachSanctionRec = (HashMap) tableUtilSanction_Main.getRemovedValues().get(j);
                objTermLoanSanctionTO = new TermLoanSanctionTO();
                objTermLoanSanctionTO.setBorrowNo(borrowerNo);
                objTermLoanSanctionTO.setSanctionNo(CommonUtil.convertObjToStr( eachSanctionRec.get(SANCTION_NO)));
                objTermLoanSanctionTO.setSlNo(CommonUtil.convertObjToStr( eachSanctionRec.get(SANCTION_SL_NO)));
                objTermLoanSanctionTO.setSanctionAuthority(CommonUtil.convertObjToStr( eachSanctionRec.get(SANCTION_AUTHORITY)));
                
                //                objTermLoanSanctionTO.setSanctionDt((Date)eachSanctionRec.get(SANCTION_DATE));
                objTermLoanSanctionTO.setSanctionDt(getProperDateFormat(eachSanctionRec.get(SANCTION_DATE)));
                objTermLoanSanctionTO.setSanctionMode(CommonUtil.convertObjToStr( eachSanctionRec.get(SANCTION_MODE)));
                objTermLoanSanctionTO.setRemarks(CommonUtil.convertObjToStr( eachSanctionRec.get(REMARKS)));
                objTermLoanSanctionTO.setCommand(CommonUtil.convertObjToStr( eachSanctionRec.get(COMMAND)));
                objTermLoanSanctionTO.setStatus(CommonConstants.STATUS_DELETED);
                objTermLoanSanctionTO.setStatusBy(TrueTransactMain.USER_ID);
                objTermLoanSanctionTO.setStatusDt(curDate);
                sanctionFacilityRecs = (LinkedHashMap) eachSanctionRec.get(SANCTION_FACILITY_ALL);
                
                sanctionFacilityKeySet =  sanctionFacilityRecs.keySet();
                objSanctionFacilityKeySet = (Object[]) sanctionFacilityKeySet.toArray();
                // To set the values for Sanction Facility Transfer Object
                // where as the existing records in Database are deleted in client side
                // useful for updating the status
                for (int k = sanctionFacilityRecs.size() - 1, l = 0;k >= 0;--k,++l){
                    eachSanctionFacilityRec = (HashMap) sanctionFacilityRecs.get(objSanctionFacilityKeySet[l]);
                    objTermLoanSanctionFacilityTO = new TermLoanSanctionFacilityTO();
                    sanctionFacilityKey++;
                    objTermLoanSanctionFacilityTO.setBorrowNo(borrowerNo);
                    objTermLoanSanctionFacilityTO.setSanctionNo(CommonUtil.convertObjToStr( eachSanctionFacilityRec.get(SANCTION_NO)));
                    objTermLoanSanctionFacilityTO.setSlNo(CommonUtil.convertObjToDouble( eachSanctionFacilityRec.get(SLNO)));
                    objTermLoanSanctionFacilityTO.setFacilityType(CommonUtil.convertObjToStr( eachSanctionFacilityRec.get(FACILITY_TYPE)));
                    objTermLoanSanctionFacilityTO.setLimit(CommonUtil.convertObjToDouble(eachSanctionFacilityRec.get(LIMIT)));
                    //                    objTermLoanSanctionFacilityTO.setFromDt((Date)eachSanctionFacilityRec.get(FROM));
                    objTermLoanSanctionFacilityTO.setFromDt(getProperDateFormat(eachSanctionFacilityRec.get(FROM)));
                    
                    //                    objTermLoanSanctionFacilityTO.setToDt((Date)eachSanctionFacilityRec.get(TO));
                    objTermLoanSanctionFacilityTO.setToDt(getProperDateFormat(eachSanctionFacilityRec.get(TO)));
                    objTermLoanSanctionFacilityTO.setNoInstall(CommonUtil.convertObjToDouble(eachSanctionFacilityRec.get(NO_INSTALLMENTS)));
                    objTermLoanSanctionFacilityTO.setRepaymentFrequency(CommonUtil.convertObjToDouble(eachSanctionFacilityRec.get(REPAY_FREQ)));
                    objTermLoanSanctionFacilityTO.setProductId(CommonUtil.convertObjToStr( eachSanctionFacilityRec.get(PROD_ID)));
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
            for (int i = tableUtilSanction_Facility.getRemovedValues().size() - 1,j = 0;i >= 0;--i,++j){
                eachSanctionFacilityRec = (HashMap) tableUtilSanction_Facility.getRemovedValues().get(j);
                objTermLoanSanctionFacilityTO = new TermLoanSanctionFacilityTO();
                sanctionFacilityKey++;
                objTermLoanSanctionFacilityTO.setBorrowNo(borrowerNo);
                objTermLoanSanctionFacilityTO.setSanctionNo(CommonUtil.convertObjToStr( eachSanctionFacilityRec.get(SANCTION_NO)));
                objTermLoanSanctionFacilityTO.setSlNo(CommonUtil.convertObjToDouble( eachSanctionFacilityRec.get(SLNO)));
                objTermLoanSanctionFacilityTO.setFacilityType(CommonUtil.convertObjToStr( eachSanctionFacilityRec.get(FACILITY_TYPE)));
                objTermLoanSanctionFacilityTO.setLimit(CommonUtil.convertObjToDouble(eachSanctionFacilityRec.get(LIMIT)));
                
                //                objTermLoanSanctionFacilityTO.setFromDt((Date)eachSanctionFacilityRec.get(FROM));
                objTermLoanSanctionFacilityTO.setFromDt(getProperDateFormat(eachSanctionFacilityRec.get(FROM)));
                //                objTermLoanSanctionFacilityTO.setToDt((Date)eachSanctionFacilityRec.get(TO));
                objTermLoanSanctionFacilityTO.setToDt(getProperDateFormat(eachSanctionFacilityRec.get(TO)));
                objTermLoanSanctionFacilityTO.setNoInstall(CommonUtil.convertObjToDouble(eachSanctionFacilityRec.get(NO_INSTALLMENTS)));
                objTermLoanSanctionFacilityTO.setRepaymentFrequency(CommonUtil.convertObjToDouble(eachSanctionFacilityRec.get(REPAY_FREQ)));
                objTermLoanSanctionFacilityTO.setProductId(CommonUtil.convertObjToStr( eachSanctionFacilityRec.get(PROD_ID)));
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
            if (getCommand().equals(CommonConstants.TOSTATUS_DELETE))
                if (sanctionMainAll.size()==1 && isSanctionDeleted)
                    canBorrowerDelete = true;
            isSanctionDeleted = false;
            
            returnValue.put(SANCTION, sanctionMap);
            returnValue.put(SANCTION_FACILITY, sanctionFacilityMap);
            
        }catch(Exception e){
            log.info("Exception caught In setTermLoanSanction: "+e);
            parseException.logException(e,true);
        }
        return returnValue;
    }
    /* single sanction singleloan
     **/
        private HashMap setTermLoanSanctionSingleRecord(){
        HashMap returnValue = new HashMap();
        try{
            TermLoanSanctionTO objTermLoanSanctionTO=new TermLoanSanctionTO();
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
                
                if (getCommand().equals(INSERT)){
                    objTermLoanSanctionTO.setStatus(CommonConstants.STATUS_CREATED);
                }else if (getCommand().equals(UPDATE)){
                    objTermLoanSanctionTO.setStatus(CommonConstants.STATUS_MODIFIED);
                }
                else if (getCommand().equals(DELETE)){
                    objTermLoanSanctionTO.setStatus(CommonConstants.STATUS_DELETED);
                }
                objTermLoanSanctionTO.setStatusBy(TrueTransactMain.USER_ID);
                objTermLoanSanctionTO.setStatusDt(curDate);
                
                
              sanctionMap.put(objTermLoanSanctionTO.getSlNo(),objTermLoanSanctionTO);
             
                //sanction details
             
                    objTermLoanSanctionFacilityTO = new TermLoanSanctionFacilityTO();
                    objTermLoanSanctionFacilityTO.setBorrowNo(borrowerNo);
                    //                    objTermLoanSanctionFacilityTO.setSanctionNo(CommonUtil.convertObjToStr( eachSanctionFacilityRec.get(SANCTION_NO)));
                    objTermLoanSanctionFacilityTO.setSanctionNo(objTermLoanSanctionTO.getSanctionNo());
                    objTermLoanSanctionFacilityTO.setSlNo(CommonUtil.convertObjToDouble(getTxtSanctionSlNo()));
                    objTermLoanSanctionFacilityTO.setFacilityType(CommonUtil.convertObjToStr( cbmTypeOfFacility.getKeyForSelected()));
                    if(objSMSSubscriptionTO !=null && isMobileBanking){
                        if(objTermLoanSanctionFacilityTO.getFacilityType().equals("OD"))
                         objSMSSubscriptionTO.setProdType("AD");
                        else
                          objSMSSubscriptionTO.setProdType("TL");
                    }
                    objTermLoanSanctionFacilityTO.setLimit(CommonUtil.convertObjToDouble(getTxtLimit_SD()));
                    
                    //                    objTermLoanSanctionFacilityTO.setFromDt((Date)eachSanctionFacilityRec.get(FROM));
                    objTermLoanSanctionFacilityTO.setFromDt(getProperDateFormat(tdtFDate));
                    
                    //                    objTermLoanSanctionFacilityTO.setToDt((Date)eachSanctionFacilityRec.get(TO));
                    objTermLoanSanctionFacilityTO.setToDt(getProperDateFormat(tdtTDate));
                    objTermLoanSanctionFacilityTO.setNoInstall(CommonUtil.convertObjToDouble(txtNoInstallments));
                    objTermLoanSanctionFacilityTO.setRepaymentFrequency(CommonUtil.convertObjToDouble(cbmRepayFreq.getKeyForSelected()));
                    objTermLoanSanctionFacilityTO.setProductId(CommonUtil.convertObjToStr( cbmProductId.getKeyForSelected()));
                     if (getChkMoratorium_Given()){
                objTermLoanSanctionFacilityTO.setMoratoriumGiven("Y");
            }else{
                objTermLoanSanctionFacilityTO.setMoratoriumGiven("N");
            }
                    
            if(isChkEligibleAmt()){
                objTermLoanSanctionFacilityTO.setEligibleAmt(YES);
            }else
                objTermLoanSanctionFacilityTO.setEligibleAmt(NO);
                    
            if(isRdoEnhance_Yes())
                objTermLoanSanctionFacilityTO.setOdRenewal(ENHANCE);
            else if (isRdoRenewal_Yes())
                 objTermLoanSanctionFacilityTO.setOdRenewal(ODRENEWAL);
            else
                objTermLoanSanctionFacilityTO.setOdRenewal("");
            
            //Added by Anju Anand for Mantis Id: 0010492
            if (objTermLoanSanctionFacilityTO.getFacilityType().equals("OD")) {
                objTermLoanSanctionFacilityTO.setRepaymentDt(getProperDateFormat(tdtTDate));
            } else {
                objTermLoanSanctionFacilityTO.setRepaymentDt(getProperDateFormat(getTdtFacility_Repay_Date()));
            }
            //end of code by Anju Anand 

                    objTermLoanSanctionFacilityTO.setNoMoratorium(CommonUtil.convertObjToDouble(getTxtFacility_Moratorium_Period()));
                    objTermLoanSanctionFacilityTO.setCommand(CommonUtil.convertObjToStr(getCommand()));
                    if (getCommand().equals(INSERT)){
                        objTermLoanSanctionFacilityTO.setStatus(CommonConstants.STATUS_CREATED);
                    }else if (getCommand().equals(UPDATE)){
                        objTermLoanSanctionFacilityTO.setStatus(CommonConstants.STATUS_MODIFIED);
                    }
                    if (getCommand().equals(CommonConstants.TOSTATUS_DELETE))
                        objTermLoanSanctionFacilityTO.setStatus(CommonConstants.STATUS_DELETED);
                    objTermLoanSanctionFacilityTO.setCommand(CommonUtil.convertObjToStr(getCommand())); 
                    objTermLoanSanctionFacilityTO.setStatusBy(TrueTransactMain.USER_ID);
                    objTermLoanSanctionFacilityTO.setStatusDt(curDate);
                 sanctionFacilityMap.put(objTermLoanSanctionFacilityTO.getSlNo(),objTermLoanSanctionFacilityTO);
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
            
        }catch(Exception e){
            log.info("Exception caught In setTermLoanSanction: "+e);
            parseException.logException(e,true);
        }
        return returnValue;
    }

    private HashMap setTermLoanFacility(){
        HashMap facilityMap = new HashMap();
        try{
            TermLoanFacilityTO objTermLoanFacilityTO;
            Set keySet =  facilityAll.keySet();
            Object[] objKeySet = (Object[]) keySet.toArray();
            HashMap oneRecord;
            // To set the values for Facility Transfer Object
            for (int i = keySet.size() - 1,j = 0;i >= 0;--i,++j){
                oneRecord = (HashMap) facilityAll.get(objKeySet[j]);
                objTermLoanFacilityTO = new TermLoanFacilityTO();
                objTermLoanFacilityTO.setBorrowNo(borrowerNo);
                objTermLoanFacilityTO.setSanctionNo(CommonUtil.convertObjToStr( oneRecord.get(SANCTION_NO)));
                objTermLoanFacilityTO.setSlNo(CommonUtil.convertObjToDouble( oneRecord.get(SLNO)));
                objTermLoanFacilityTO.setProdId(CommonUtil.convertObjToStr( oneRecord.get(PROD_ID)));
                objTermLoanFacilityTO.setAcctStatus(CommonUtil.convertObjToStr(oneRecord.get(ACCT_STATUS)));
                objTermLoanFacilityTO.setAcctNum(CommonUtil.convertObjToStr(oneRecord.get(ACCT_NUM)));
                objTermLoanFacilityTO.setAcctName(CommonUtil.convertObjToStr(oneRecord.get(ACCT_NAME)));
                objTermLoanFacilityTO.setIntGetFrom(CommonUtil.convertObjToStr(oneRecord.get(INT_GET_FROM)));
                objTermLoanFacilityTO.setSecurityType(CommonUtil.convertObjToStr( oneRecord.get(SECURITY_TYPE)));
                objTermLoanFacilityTO.setSecurityDetails(CommonUtil.convertObjToStr( oneRecord.get(SECURITY_DETAILS)));
                objTermLoanFacilityTO.setAccountType(CommonUtil.convertObjToStr( oneRecord.get(ACC_TYPE)));
                //                objTermLoanFacilityTO.setInterestNature(CommonUtil.convertObjToStr( oneRecord.get(INTEREST_NATURE)));
                objTermLoanFacilityTO.setInterestType(CommonUtil.convertObjToStr( oneRecord.get(INTEREST_TYPE)));
                objTermLoanFacilityTO.setAccountLimit(CommonUtil.convertObjToStr( oneRecord.get(ACC_LIMIT)));
                objTermLoanFacilityTO.setRiskWeight(CommonUtil.convertObjToStr( oneRecord.get(RISK_WEIGHT)));
                
                //                objTermLoanFacilityTO.setDemandPromDt((Date)oneRecord.get(NOTE_DATE));
                objTermLoanFacilityTO.setDemandPromDt(getProperDateFormat(oneRecord.get(NOTE_DATE)));
                
                //                objTermLoanFacilityTO.setDemandPromExpdt((Date)oneRecord.get(NOTE_EXP_DATE));
                objTermLoanFacilityTO.setDemandPromExpdt(getProperDateFormat(oneRecord.get(NOTE_EXP_DATE)));
                objTermLoanFacilityTO.setMultiDisburse(CommonUtil.convertObjToStr( oneRecord.get(MULTI_DISBURSE)));
                
                //                objTermLoanFacilityTO.setAodDt((Date)oneRecord.get(AOD_DATE));
                objTermLoanFacilityTO.setAodDt(getProperDateFormat(oneRecord.get(AOD_DATE)));
                objTermLoanFacilityTO.setPurposeDesc(CommonUtil.convertObjToStr( oneRecord.get(PURPOSE_DESC)));
                objTermLoanFacilityTO.setGroupDesc(CommonUtil.convertObjToStr( oneRecord.get(GROUP_DESC)));
                objTermLoanFacilityTO.setInterest(CommonUtil.convertObjToStr( oneRecord.get(INTEREST)));
                objTermLoanFacilityTO.setDpYesNo(CommonUtil.convertObjToStr( oneRecord.get(DRAWING_POWER)));
                objTermLoanFacilityTO.setContactPerson(CommonUtil.convertObjToStr( oneRecord.get(CONTACT_PERSON)));
                objTermLoanFacilityTO.setDealerID(CommonUtil.convertObjToStr(oneRecord.get(DEALER_ID)));
                objTermLoanFacilityTO.setSalaryRecovery(CommonUtil.convertObjToStr( oneRecord.get(SALARY_RECOVERY)));
                objTermLoanFacilityTO.setLockStatus(CommonUtil.convertObjToStr( oneRecord.get(LOCK_STATUS)));
                objTermLoanFacilityTO.setContactPhone(CommonUtil.convertObjToStr( oneRecord.get(CONTACT_PHONE)));
                objTermLoanFacilityTO.setRemarks(CommonUtil.convertObjToStr( oneRecord.get(REMARKS)));
                objTermLoanFacilityTO.setCommand(CommonUtil.convertObjToStr( oneRecord.get(COMMAND)));
                objTermLoanFacilityTO.setAuthorizedSignatory(CommonUtil.convertObjToStr(oneRecord.get(AUTHSIGNATORY)));
                objTermLoanFacilityTO.setPofAttorney(CommonUtil.convertObjToStr(oneRecord.get(POFATTORNEY)));
                objTermLoanFacilityTO.setDocDetails(CommonUtil.convertObjToStr(oneRecord.get(DOCDETAILS)));
                objTermLoanFacilityTO.setAcctTransfer(CommonUtil.convertObjToStr(oneRecord.get(ACCTTRANSFER)));
                objTermLoanFacilityTO.setAccOpenDt(getProperDateFormat(oneRecord.get(ACCT_OPEN_DT)));
                if(oneRecord.containsKey(ACCT_CLOSE_DT) && oneRecord.get(ACCT_CLOSE_DT)!=null)
                    objTermLoanFacilityTO.setAccCloseDt(getProperDateFormat(oneRecord.get(ACCT_CLOSE_DT)));
                objTermLoanFacilityTO.setRecommendedBy(CommonUtil.convertObjToStr(oneRecord.get(RECOMMANDED_BY)));
                objTermLoanFacilityTO.setRecommendedBy2(CommonUtil.convertObjToStr(oneRecord.get(RECOMMANDED_BY2)));
                objTermLoanFacilityTO.setKoleLandArea(CommonUtil.convertObjToDouble(oneRecord.get(KOLE_LAND_AREA)));
                if (oneRecord.get(COMMAND).equals(INSERT)){
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
                }else if (oneRecord.get(COMMAND).equals(UPDATE)){
                    objTermLoanFacilityTO.setStatus(CommonConstants.STATUS_MODIFIED);
                    //                    if(loanType.equals("OTHERS"))
                    objTermLoanFacilityTO.setAvailableBalance(CommonUtil.convertObjToDouble(oneRecord.get(AVAILABLE_BALANCE))); //FOR UPDATE AVAILABLE AMT
                }
                
                if (getCommand().equals(CommonConstants.TOSTATUS_DELETE))
                    if (objTermLoanFacilityTO.getSanctionNo().equals(getTxtSanctionSlNo()) &&
                    objTermLoanFacilityTO.getSlNo().doubleValue() == CommonUtil.convertObjToDouble(getSanctionSlNo()).doubleValue()) {
                        objTermLoanFacilityTO.setStatus(CommonConstants.STATUS_DELETED);
                    }
                
                objTermLoanFacilityTO.setAuthorizeBy1(CommonUtil.convertObjToStr(oneRecord.get(AUTHORIZE_BY_1)));
                objTermLoanFacilityTO.setAuthorizeBy2(CommonUtil.convertObjToStr(oneRecord.get(AUTHORIZE_BY_2)));
                
                //                objTermLoanFacilityTO.setAuthorizeDt1((Date)oneRecord.get(AUTHORIZE_DT_1));
                objTermLoanFacilityTO.setAuthorizeDt1(getProperDateFormat(oneRecord.get(AUTHORIZE_DT_1)));
                
                //                objTermLoanFacilityTO.setAuthorizeDt2((Date)oneRecord.get(AUTHORIZE_DT_2));
                objTermLoanFacilityTO.setAuthorizeDt2(getProperDateFormat(oneRecord.get(AUTHORIZE_DT_2)));
                objTermLoanFacilityTO.setAuthorizeStatus1(CommonUtil.convertObjToStr(oneRecord.get(AUTHORIZE_STATUS_1)));
                objTermLoanFacilityTO.setAuthorizeStatus2(CommonUtil.convertObjToStr(oneRecord.get(AUTHORIZE_STATUS_2)));
                objTermLoanFacilityTO.setStatusBy(TrueTransactMain.USER_ID);
                objTermLoanFacilityTO.setStatusDt(curDate);
                objTermLoanFacilityTO.setBranchId(getSelectedBranchID());
                if (objTermLoanFacilityTO.getAcctNum().equals(getStrACNumber())) {
                facilityMap.put(String.valueOf(j+1), objTermLoanFacilityTO);
                    slNoForSanction = objTermLoanFacilityTO.getSlNo().doubleValue();
                }
              
                objTermLoanFacilityTO = null;
                  if (oneRecord.get(COMMAND).equals(INSERT)){
                      TermLoanExtenFacilityDetailsTO termLoanExtenFacilityDetailsTO=new TermLoanExtenFacilityDetailsTO();
                      
//                      agriSubSidyOB.setAgriSubSidyTo();//dontdelete
                  }
                  oneRecord = null;
            }
            keySet = null;
            objKeySet= null;
            
            Set removedKeySet =  removedFaccilityTabValues.keySet();
            Object[] objRemovedKeySet = (Object[]) removedKeySet.toArray();
            // To set the values for Facility Transfer Object
            // where as the existing records in Database are deleted in client side
            // useful for updating the status
            for (int i = removedFaccilityTabValues.size() - 1,j = 0;i >= 0;--i,++j){
                oneRecord = (HashMap) removedFaccilityTabValues.get(objRemovedKeySet[j]);
                objTermLoanFacilityTO = new TermLoanFacilityTO();
                objTermLoanFacilityTO.setBorrowNo(borrowerNo);
                objTermLoanFacilityTO.setSanctionNo(CommonUtil.convertObjToStr( oneRecord.get(SANCTION_NO)));
                objTermLoanFacilityTO.setSlNo(CommonUtil.convertObjToDouble( oneRecord.get(SLNO)));
                objTermLoanFacilityTO.setProdId(CommonUtil.convertObjToStr( oneRecord.get(PROD_ID)));
                objTermLoanFacilityTO.setAcctStatus(CommonUtil.convertObjToStr(oneRecord.get(ACCT_STATUS)));
                objTermLoanFacilityTO.setAcctName(CommonUtil.convertObjToStr(oneRecord.get(ACCT_NAME)));
                objTermLoanFacilityTO.setIntGetFrom(CommonUtil.convertObjToStr(oneRecord.get(INT_GET_FROM)));
                objTermLoanFacilityTO.setSecurityType(CommonUtil.convertObjToStr( oneRecord.get(SECURITY_TYPE)));
                objTermLoanFacilityTO.setSecurityDetails(CommonUtil.convertObjToStr( oneRecord.get(SECURITY_DETAILS)));
                objTermLoanFacilityTO.setAccountType(CommonUtil.convertObjToStr( oneRecord.get(ACC_TYPE)));
                //                objTermLoanFacilityTO.setInterestNature(CommonUtil.convertObjToStr( oneRecord.get(INTEREST_NATURE)));
                objTermLoanFacilityTO.setInterestType(CommonUtil.convertObjToStr( oneRecord.get(INTEREST_TYPE)));
                objTermLoanFacilityTO.setAccountLimit(CommonUtil.convertObjToStr( oneRecord.get(ACC_LIMIT)));
                objTermLoanFacilityTO.setRiskWeight(CommonUtil.convertObjToStr( oneRecord.get(RISK_WEIGHT)));
                
                //                objTermLoanFacilityTO.setDemandPromDt((Date)oneRecord.get(NOTE_DATE));
                objTermLoanFacilityTO.setDemandPromDt(getProperDateFormat(oneRecord.get(NOTE_DATE)));
                
                //                objTermLoanFacilityTO.setDemandPromExpdt((Date)oneRecord.get(NOTE_EXP_DATE));
                objTermLoanFacilityTO.setDemandPromExpdt(getProperDateFormat(oneRecord.get(NOTE_EXP_DATE)));
                objTermLoanFacilityTO.setMultiDisburse(CommonUtil.convertObjToStr( oneRecord.get(MULTI_DISBURSE)));
                
                //                objTermLoanFacilityTO.setAodDt((Date)oneRecord.get(AOD_DATE));
                objTermLoanFacilityTO.setAodDt(getProperDateFormat(oneRecord.get(AOD_DATE)));
                objTermLoanFacilityTO.setPurposeDesc(CommonUtil.convertObjToStr( oneRecord.get(PURPOSE_DESC)));
                objTermLoanFacilityTO.setGroupDesc(CommonUtil.convertObjToStr( oneRecord.get(GROUP_DESC)));
                objTermLoanFacilityTO.setInterest(CommonUtil.convertObjToStr( oneRecord.get(INTEREST)));
                objTermLoanFacilityTO.setContactPerson(CommonUtil.convertObjToStr( oneRecord.get(CONTACT_PERSON)));
                objTermLoanFacilityTO.setDealerID(CommonUtil.convertObjToStr(oneRecord.get(DEALER_ID)));
                objTermLoanFacilityTO.setSalaryRecovery(CommonUtil.convertObjToStr( oneRecord.get(SALARY_RECOVERY)));
                objTermLoanFacilityTO.setLockStatus(CommonUtil.convertObjToStr( oneRecord.get(LOCK_STATUS)));
                objTermLoanFacilityTO.setContactPhone(CommonUtil.convertObjToStr( oneRecord.get(CONTACT_PHONE)));
                objTermLoanFacilityTO.setRemarks(CommonUtil.convertObjToStr( oneRecord.get(REMARKS)));
                objTermLoanFacilityTO.setCommand(CommonUtil.convertObjToStr( oneRecord.get(COMMAND)));
                objTermLoanFacilityTO.setStatus(CommonConstants.STATUS_DELETED);
                objTermLoanFacilityTO.setStatusBy(TrueTransactMain.USER_ID);
                objTermLoanFacilityTO.setStatusDt(curDate);
                objTermLoanFacilityTO.setAuthorizeBy1(CommonUtil.convertObjToStr(oneRecord.get(AUTHORIZE_BY_1)));
                objTermLoanFacilityTO.setAuthorizeBy2(CommonUtil.convertObjToStr(oneRecord.get(AUTHORIZE_BY_2)));
                
                //                objTermLoanFacilityTO.setAuthorizeDt1((Date)oneRecord.get(AUTHORIZE_DT_1));
                objTermLoanFacilityTO.setAuthorizeDt1(getProperDateFormat(oneRecord.get(AUTHORIZE_DT_1)));
                
                //                objTermLoanFacilityTO.setAuthorizeDt2((Date)oneRecord.get(AUTHORIZE_DT_2));
                objTermLoanFacilityTO.setAuthorizeDt2(getProperDateFormat(oneRecord.get(AUTHORIZE_DT_2)));
                objTermLoanFacilityTO.setAuthorizeStatus1(CommonUtil.convertObjToStr(oneRecord.get(AUTHORIZE_STATUS_1)));
                objTermLoanFacilityTO.setAuthorizeStatus2(CommonUtil.convertObjToStr(oneRecord.get(AUTHORIZE_STATUS_2)));
                objTermLoanFacilityTO.setBranchId(getSelectedBranchID());
                facilityMap.put(String.valueOf(facilityMap.size()+1), objTermLoanFacilityTO);
                oneRecord = null;
                objTermLoanFacilityTO = null;
            }
            removedKeySet = null;
            objRemovedKeySet = null;
        }catch(Exception e){
            log.info("Exception caught In setTermLoanFacility: "+e);
            parseException.logException(e,true);
        }
        return facilityMap;
    }
    
    // Added by nithya on 07-03-2020 for KD-1379
    private LoansSecurityGoldStockTO getCustomerGoldStockSecurityTO() {
        System.out.println("inside getCustomerGoldStockSecurityTO getStrACNumber() :: " + getStrACNumber());
        LoansSecurityGoldStockTO objCustomerGoldStockSecurityTO = new LoansSecurityGoldStockTO();
        objCustomerGoldStockSecurityTO.setAcctNum(getStrACNumber());
        objCustomerGoldStockSecurityTO.setAsOnDt(curDate);
        objCustomerGoldStockSecurityTO.setPledgeAmount(CommonUtil.convertObjToDouble(getTxtValueOfGold()));
        objCustomerGoldStockSecurityTO.setRemarks(getTxtGoldRemarks());
        objCustomerGoldStockSecurityTO.setStatusDt(curDate);
        objCustomerGoldStockSecurityTO.setStatusBy(TrueTransactMain.USER_ID);
        objCustomerGoldStockSecurityTO.setStatus(CommonConstants.STATUS_CREATED);
        objCustomerGoldStockSecurityTO.setGoldStockId(getTxtGoldSecurityId());
        objCustomerGoldStockSecurityTO.setBranchCode(ProxyParameters.BRANCH_ID);
        objCustomerGoldStockSecurityTO.setProdId(CommonUtil.convertObjToStr( cbmProductId.getKeyForSelected()));
        objCustomerGoldStockSecurityTO.setProdType("TL");
        return objCustomerGoldStockSecurityTO;
    }
    // End
    
    private HashMap setTermLoanFacilitySingleRecord(){
        HashMap facilityMap = new HashMap();
        try{
            TermLoanFacilityTO objTermLoanFacilityTO;
            Set keySet =  facilityAll.keySet();
            Object[] objKeySet = (Object[]) keySet.toArray();
            HashMap oneRecord;
            // To set the values for Facility Transfer Object
            
            objTermLoanFacilityTO = new TermLoanFacilityTO();
            objTermLoanFacilityTO.setBorrowNo(borrowerNo);
            objTermLoanFacilityTO.setSanctionNo(CommonUtil.convertObjToStr( facilityRecord.get(SANCTION_NO)));
            objTermLoanFacilityTO.setSlNo(CommonUtil.convertObjToDouble( facilityRecord.get(SLNO)));
            objTermLoanFacilityTO.setProdId(CommonUtil.convertObjToStr( facilityRecord.get(PROD_ID)));
            objTermLoanFacilityTO.setAcctStatus(CommonUtil.convertObjToStr(facilityRecord.get(ACCT_STATUS)));
            objTermLoanFacilityTO.setAcctNum(CommonUtil.convertObjToStr(facilityRecord.get(ACCT_NUM)));
            objTermLoanFacilityTO.setAcctName(CommonUtil.convertObjToStr(facilityRecord.get(ACCT_NAME)));
            objTermLoanFacilityTO.setIntGetFrom(CommonUtil.convertObjToStr(facilityRecord.get(INT_GET_FROM)));
            objTermLoanFacilityTO.setSecurityType(CommonUtil.convertObjToStr( facilityRecord.get(SECURITY_TYPE)));
            objTermLoanFacilityTO.setSecurityDetails(CommonUtil.convertObjToStr( facilityRecord.get(SECURITY_DETAILS)));
            objTermLoanFacilityTO.setAccountType(CommonUtil.convertObjToStr( facilityRecord.get(ACC_TYPE)));
            objTermLoanFacilityTO.setInterestType(CommonUtil.convertObjToStr( facilityRecord.get(INTEREST_TYPE)));
            objTermLoanFacilityTO.setAccountLimit(CommonUtil.convertObjToStr( facilityRecord.get(ACC_LIMIT)));
            objTermLoanFacilityTO.setRiskWeight(CommonUtil.convertObjToStr( facilityRecord.get(RISK_WEIGHT)));
            
            //                objTermLoanFacilityTO.setDemandPromDt((Date)oneRecord.get(NOTE_DATE));
            objTermLoanFacilityTO.setDemandPromDt(getProperDateFormat(facilityRecord.get(NOTE_DATE)));
            
            //                objTermLoanFacilityTO.setDemandPromExpdt((Date)oneRecord.get(NOTE_EXP_DATE));
            objTermLoanFacilityTO.setDemandPromExpdt(getProperDateFormat(facilityRecord.get(NOTE_EXP_DATE)));
            objTermLoanFacilityTO.setMultiDisburse(CommonUtil.convertObjToStr( facilityRecord.get(MULTI_DISBURSE)));
            
            //                objTermLoanFacilityTO.setAodDt((Date)oneRecord.get(AOD_DATE));
            objTermLoanFacilityTO.setAodDt(getProperDateFormat(facilityRecord.get(AOD_DATE)));
            objTermLoanFacilityTO.setPurposeDesc(CommonUtil.convertObjToStr( facilityRecord.get(PURPOSE_DESC)));
            objTermLoanFacilityTO.setGroupDesc(CommonUtil.convertObjToStr( facilityRecord.get(GROUP_DESC)));
            objTermLoanFacilityTO.setInterest(CommonUtil.convertObjToStr( facilityRecord.get(INTEREST)));
            objTermLoanFacilityTO.setDpYesNo(CommonUtil.convertObjToStr( facilityRecord.get(DRAWING_POWER)));
            objTermLoanFacilityTO.setContactPerson(CommonUtil.convertObjToStr( facilityRecord.get(CONTACT_PERSON)));
            objTermLoanFacilityTO.setDealerID(CommonUtil.convertObjToStr(facilityRecord.get(DEALER_ID)));
            objTermLoanFacilityTO.setSalaryRecovery(CommonUtil.convertObjToStr( facilityRecord.get(SALARY_RECOVERY)));
            objTermLoanFacilityTO.setLockStatus(CommonUtil.convertObjToStr( facilityRecord.get(LOCK_STATUS)));
            objTermLoanFacilityTO.setContactPhone(CommonUtil.convertObjToStr( facilityRecord.get(CONTACT_PHONE)));
            objTermLoanFacilityTO.setRemarks(CommonUtil.convertObjToStr( facilityRecord.get(REMARKS)));
            objTermLoanFacilityTO.setCommand(CommonUtil.convertObjToStr( getCommand()));
            objTermLoanFacilityTO.setAuthorizedSignatory(CommonUtil.convertObjToStr(facilityRecord.get(AUTHSIGNATORY)));
            objTermLoanFacilityTO.setPofAttorney(CommonUtil.convertObjToStr(facilityRecord.get(POFATTORNEY)));
            objTermLoanFacilityTO.setDocDetails(CommonUtil.convertObjToStr(facilityRecord.get(DOCDETAILS)));
            objTermLoanFacilityTO.setAcctTransfer(CommonUtil.convertObjToStr(facilityRecord.get(ACCTTRANSFER)));
            
            objTermLoanFacilityTO.setSubsidyAllowed(CommonUtil.convertObjToStr(facilityRecord.get(SUBSIDY)));
            objTermLoanFacilityTO.setSubsidyAmt(CommonUtil.convertObjToDouble(facilityRecord.get(SUBSIDY_AMT)));
            objTermLoanFacilityTO.setSubsidyAdjustAchd(CommonUtil.convertObjToStr(facilityRecord.get(SUBSIDY_ACHEAD)));
            objTermLoanFacilityTO.setSubsidyAdjustAmt(CommonUtil.convertObjToDouble(facilityRecord.get(SUBSIDY_ADJUSTED_AMT)));
            objTermLoanFacilityTO.setSubsidyDate(getProperDateFormat(facilityRecord.get(SUBSIDY_DT)));
            objTermLoanFacilityTO.setRebateAllowed(CommonUtil.convertObjToStr(facilityRecord.get(REBATE_ALLOWED)));
            objTermLoanFacilityTO.setRebateAmt(CommonUtil.convertObjToDouble(facilityRecord.get(REBATE_AMT)));
            objTermLoanFacilityTO.setRebateDate(getProperDateFormat(facilityRecord.get(REBATE_DT)));
            objTermLoanFacilityTO.setOts(CommonUtil.convertObjToStr(facilityRecord.get("OTS")));
            
            
            
            
            //                objTermLoanFacilityTO.setAccOpenDt((Date)oneRecord.get(ACCT_OPEN_DT));
            objTermLoanFacilityTO.setAccOpenDt(getProperDateFormat(facilityRecord.get(ACCT_OPEN_DT)));
            if(facilityRecord.containsKey(ACCT_CLOSE_DT) && facilityRecord.get(ACCT_CLOSE_DT)!=null)
                objTermLoanFacilityTO.setAccCloseDt(getProperDateFormat(facilityRecord.get(ACCT_CLOSE_DT)));
            objTermLoanFacilityTO.setRecommendedBy(CommonUtil.convertObjToStr(facilityRecord.get(RECOMMANDED_BY)));
            objTermLoanFacilityTO.setRecommendedBy2(CommonUtil.convertObjToStr(facilityRecord.get(RECOMMANDED_BY2)));
            objTermLoanFacilityTO.setKoleLandArea(CommonUtil.convertObjToDouble(facilityRecord.get(KOLE_LAND_AREA)));
            if (getCommand().equals(INSERT)){
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
            }else if (getCommand().equals(UPDATE)){
                objTermLoanFacilityTO.setStatus(CommonConstants.STATUS_MODIFIED);
                //                    if(loanType.equals("OTHERS"))
                objTermLoanFacilityTO.setAvailableBalance(CommonUtil.convertObjToDouble(facilityRecord.get(AVAILABLE_BALANCE))); //FOR UPDATE AVAILABLE AMT
            }
            
            // OD Renewal purpose added
            if(isRdoEnhance_Yes())
                objTermLoanFacilityTO.setAuthorizeStatus2("ENHANCE");
            else if (isRdoRenewal_Yes())
                objTermLoanFacilityTO.setAuthorizeStatus2("ODRENEWAL");
            else
                objTermLoanFacilityTO.setAuthorizeStatus2("");
            
            if (getCommand().equals(CommonConstants.TOSTATUS_DELETE))
                if (objTermLoanFacilityTO.getSanctionNo().equals(getTxtSanctionSlNo()) &&
                objTermLoanFacilityTO.getSlNo().doubleValue() == CommonUtil.convertObjToDouble(getSanctionSlNo()).doubleValue()) {
                    objTermLoanFacilityTO.setStatus(CommonConstants.STATUS_DELETED);
                }
            
            objTermLoanFacilityTO.setAuthorizeBy1(CommonUtil.convertObjToStr(facilityRecord.get(AUTHORIZE_BY_1)));
            objTermLoanFacilityTO.setAuthorizeBy2(CommonUtil.convertObjToStr(facilityRecord.get(AUTHORIZE_BY_2)));
            
            //                objTermLoanFacilityTO.setAuthorizeDt1((Date)oneRecord.get(AUTHORIZE_DT_1));
            objTermLoanFacilityTO.setAuthorizeDt1(getProperDateFormat(facilityRecord.get(AUTHORIZE_DT_1)));
            
            //                objTermLoanFacilityTO.setAuthorizeDt2((Date)oneRecord.get(AUTHORIZE_DT_2));
            objTermLoanFacilityTO.setAuthorizeDt2(getProperDateFormat(facilityRecord.get(AUTHORIZE_DT_2)));
            objTermLoanFacilityTO.setAuthorizeStatus1(CommonUtil.convertObjToStr(facilityRecord.get(AUTHORIZE_STATUS_1)));
            //                objTermLoanFacilityTO.setAuthorizeStatus2(CommonUtil.convertObjToStr(facilityRecord.get(AUTHORIZE_STATUS_2)));
            objTermLoanFacilityTO.setStatusBy(TrueTransactMain.USER_ID);
            objTermLoanFacilityTO.setStatusDt(curDate);
            objTermLoanFacilityTO.setBranchId(getSelectedBranchID());
            objTermLoanFacilityTO.setTxtGoldRemarks(getTxtGoldRemarks());
            objTermLoanFacilityTO.setTxtGrossWeight(getTxtGrossWeight());
            objTermLoanFacilityTO.setTxtJewelleryDetails(getTxtJewelleryDetails());
            objTermLoanFacilityTO.setTxtNetWeight(getTxtNetWeight());
            objTermLoanFacilityTO.setTxtValueOfGold(CommonUtil.convertObjToDouble(getTxtValueOfGold()));
            if (isMobileBanking)
                objTermLoanFacilityTO.setIsMobileBanking("Y");
            else
                objTermLoanFacilityTO.setIsMobileBanking("N");
            if (isMobileBanking) {
                if(objSMSSubscriptionTO ==null)
                    objSMSSubscriptionTO = new SMSSubscriptionTO();
                objSMSSubscriptionTO.setProdId(objTermLoanFacilityTO.getProdId());
                objSMSSubscriptionTO.setActNum(objTermLoanFacilityTO.getAcctNum());
                
                objSMSSubscriptionTO.setMobileNo(CommonUtil.convertObjToStr(getTxtMobileNo()));
                Date smsSubscriptionDt = DateUtil.getDateMMDDYYYY(getTdtMobileSubscribedFrom());
                if(smsSubscriptionDt != null){
                    Date smsDt = (Date) curDate.clone();
                    smsDt.setDate(smsSubscriptionDt.getDate());
                    smsDt.setMonth(smsSubscriptionDt.getMonth());
                    smsDt.setYear(smsSubscriptionDt.getYear());
                    objSMSSubscriptionTO.setSubscriptionDt(smsDt);
                }else{
                    objSMSSubscriptionTO.setSubscriptionDt(DateUtil.getDateMMDDYYYY(getTdtMobileSubscribedFrom()));
                }
                if(!CommonUtil.convertObjToStr(objSMSSubscriptionTO.getStatus()).equals(CommonConstants.STATUS_MODIFIED))
                    objSMSSubscriptionTO.setStatus(CommonConstants.STATUS_CREATED);
                objSMSSubscriptionTO.setStatusBy(ProxyParameters.USER_ID);
                objSMSSubscriptionTO.setStatusDt(curDate);
            } else {
                
                if(objSMSSubscriptionTO !=null ){
                    objSMSSubscriptionTO.setStatus(CommonConstants.STATUS_DELETED);
                }else
                    objSMSSubscriptionTO = null;
            }
            
            
            
            if (objTermLoanFacilityTO.getAcctNum().equals(getStrACNumber())) {
                facilityMap.put(String.valueOf(objTermLoanFacilityTO.getSlNo()), objTermLoanFacilityTO);
                slNoForSanction = objTermLoanFacilityTO.getSlNo().doubleValue();
            }
            
            objTermLoanFacilityTO = null;
            if (getCommand().equals(INSERT)){
                TermLoanExtenFacilityDetailsTO termLoanExtenFacilityDetailsTO=new TermLoanExtenFacilityDetailsTO();
                
                //                      agriSubSidyOB.setAgriSubSidyTo();//dontdelete
            }
            oneRecord = null;
            
        }catch(Exception e){
            e.printStackTrace();
            log.info("Exception caught In setTermLoanFacility: "+e);
            parseException.logException(e,true);
        }
        return facilityMap;
    }
    
    /**
     * This method will reset all the fields of TermLoanUI
     */
    public void resetForm(){
        //  Borrower Details
        termLoanBorrowerOB.resetBorrowerDetails();
        //  Company Details
        termLoanCompanyOB.resetCustomerDetails();
        // Sanction Facility
        resetSanctionFacility();
        // Sanction Main Table
        resetSanctionMain();
        // Facility Details Table
        resetFacilityDetails();
        cbmDirectRepaymentLoanPeriod.setKeyForSelected("");
        cbmDirectPaymentProdType.setKeyForSelected("");
        if(cbmDirectPaymentProdId !=null && CommonUtil.convertObjToStr(cbmDirectPaymentProdId.getKeyForSelected()).length()>0)
            cbmDirectPaymentProdId.setKeyForSelected("");
        setTxtDirectRepaymentAcctNo("");
        setTxtDirectRepaymentAcctNo("");
        setTxtDirectRepaymentAcctHead("");
        setDirectRepayment_Yes(false);
        setDirectRepayment_No(false);
        
        //        agriSubLimitOB.resetFormComponets();
        agriSubLimitOB.resetFormComponetsSubLimit();
        //        agriSubLimitOB.destroyObjects();
        agriSubLimitOB.notifyObservers();
        // Reset the tabs based on the Account Number
//        agriSubSidyOB.resetSubsidyDetails();//dontdelete
//        settlementOB.resetAllFieldsInPoA();//dontdelete
//        settlementOB.resetSetBnkForm();//dontdelete
//        actTransOB.resetAcctTransfer();//dontdelete
        resetSecuritySalaryDetails();
        resetBasedOnAcctNumber();
        termLoanAdditionalSanctionOB.resetPeakSanctionDetails();
        isCaseDetailsTrans=false;
        chkOTS =false;
        editCourtDetailsMap=new HashMap();
        deleteCourtDetailsMap=new HashMap();
        txtCourtOrderNo ="";
        tdtCourtOrderDate ="";
        tdtOTSDate ="";
        txtOTSRate ="";
        txtTotAmountDue  ="";
        txtSettlementAmt  ="";
        txtPrincipalAmount  =0;
        txtInterestAmount =0;
        txtPenalInterestAmount  =0;
        txtChargeAmount  =0;
        txtTotalAmountWrittenOff  =0;
        txtNoInstallment ="";
        cbmFreq.setKeyForSelected("");
        txtInstallmentAmt  ="";
        court_slno=0;
       
       
             rdoRepaySingle_YES=false;
             rdoRepaySingle_NO=false;
       
       
        firstInstallmentDt="";
        lastInstallmentDt="";
        txtPenal="";
        courtRemarks="";
        suspenceAccountNo = "";
        suspenceProductID = "";
        tableCheck = false;
       serviceTax_Map = null;
       lblServiceTaxval="";
        setKccFlag(false);  
        setKccNature("");
        destroyObjects();
        createObject();
        ttNotifyObservers();
    }
    
    /**
     * This method will reset the tabs which are all dependent to term loan account number
     */
    public void resetBasedOnAcctNumber(){
        // Security Details Table
//        termLoanSecurityOB.resetAllSecurityDetails();
        // Repayment Schedule
        termLoanRepaymentOB.resetAllRepayment();
        // Interest Details
        termLoanInterestOB.resetAllInterestDetails();
        //        if (loanType.equals("OTHERS")) {
        // Guarantor Details
        termLoanGuarantorOB.resetAllGuarantorDetails();
        //        }
        // Document Details
        termLoanDocumentDetailsOB.resetAllDocumentDetails();
        // Classification Details
        termLoanClassificationOB.resetClassificationDetails();
        // Other Details
        termLoanOtherDetailsOB.resetOtherDetailsFields();
        
        termLoanAdditionalSanctionOB.resetPeakSanctionDetails();
        // Set Account Number as null
        resetStrAcNumber();
    }
    
    public void resetSecuritySalaryDetails(){
        setTxtSalaryCertificateNo("");
        setTxtEmployerName("");
        setTxtAddress("");
        setCboSecurityCity("");
        setTxtPinCode("");
        setTxtDesignation("");
        setTxtContactNo("");
        setTdtRetirementDt("");
        setTxtMemberNum("");
        setTxtTotalSalary("");
        setTxtNetWorth("");
        setTxtSalaryRemark("");
    }
    
    public void resetSanctionFacility(){
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
        setMaxLoanPeriodChar("");
        setTxtPeriodDifference_Days("");
        setTxtPeriodDifference_Months("");
        setTxtPeriodDifference_Years("");
        setMinDecLoanPeriod("0");
        setMaxDecLoanPeriod("0");
        setLblDepositNo("");
        resetStrAcNumber();
        setPartReject("");
        setRdoSalaryRecovery("");
        setLockStatus("");
        //emi
        setChkDiminishing(false);
        
    }
    
    public void resetStrAcNumber(){
        // Set Account Number as null
        setStrACNumber("");
    }
    
    public void resetAllFacilityDetails(){
        resetFacilityDetails();
        resetFacilityTabSubsidy();
        resetFacilityTabInterestNature();
        resetFacilityTabContactDetails();
    }
    
    public void resetFacilityTabInterestNature(){
        setRdoNatureInterest_NonPLR(false);
        setRdoNatureInterest_PLR(false);
    }
    
    public void resetFacilityTabSubsidy(){
        setRdoSubsidy_No(false);
        setRdoSubsidy_Yes(false);
    }
    public void resetFacilityTabContactDetails(){
        setTxtContactPerson("");
        setTxtContactPhone("");
    }
    public void resetFacilityDetails(){
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
        setChkEligibleAmt(false);
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
        setCboRecommendedByType2("");
        setTdtDemandPromNoteExpDate("");
        setTdtAODDate("");
        setRdoMultiDisburseAllow_No(false);
        setRdoMultiDisburseAllow_Yes(false);
        setTxtPurposeDesc("");
        setTxtGroupDesc("");
        setRdoInterest_Compound(false);
        setRdoInterest_Simple(false);
        setTxtRemarks("");
        setTxtKoleLandArea("");
        setRdoDP_YES(false);
        setRdoDP__NO(false);
        setChkAuthorizedSignatory(false);
        setChkDocDetails(false);
        setChkGurantor(false);
        setChkPOFAttorney(false);
        setClearBalance(0);
        setAvailableBalance(0);
        setRdoEnhance_Yes(false);
        setRdoEnhance_No(false);
        setRdoRenewal_Yes(false);
        setRdoRenewal_No(false);
        isMobileBanking=false;
        rdoRebateInterest_Yes=false;
        rdoRebateInterest_No=false;
        setTxtMobileNo("");
        setTdtMobileSubscribedFrom("");
        setTxtDealerID("");
    }
    
    public void resetSanctionFacilityTable(){
        tblSanctionFacility = new EnhancedTableModel(null, sanctionFacilityTitle);
    }
    
    public void resetSanctionMain(){
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
     * @return the appropriate action taken
     */
    public int getActionType(){
        log.info("In getActionType...");
        return actionType;
    }
    
    /**
     * will set the action type whether it is new or edit or delete
     * @param actionType is new or edit or delete
     */
    public void setActionType(int actionType) {
        log.info("In setActionType: "+actionType);
        this.actionType = actionType;
        setChanged();
    }
    
    /**
     * It will set the result of the query executed
     * @param resultStatus is the integer value which gives whether the data are inserted or updated or
     * deleted or the execution is failed
     */
    public void setResult(int resultStatus) {
        log.info("In setResult...");
        this.resultStatus = resultStatus;
        setChanged();
    }
    
    /**
     * Return the result of the executed query
     * @return the result of the executed query
     */
    public int getResult(){
        log.info("In getResult...");
        return resultStatus;
    }
    
    void setTxtSanctionNo(String txtSanctionNo){
        this.txtSanctionNo = txtSanctionNo;
        setChanged();
    }
    String getTxtSanctionNo(){
        return this.txtSanctionNo;
    }
    
    void setTdtSanctionDate(String tdtSanctionDate){
        this.tdtSanctionDate = tdtSanctionDate;
        setChanged();
    }
    String getTdtSanctionDate(){
        return this.tdtSanctionDate;
    }
    
    
    void setCbmSanctioningAuthority(ComboBoxModel cbmSanctioningAuthority){
        this.cbmSanctioningAuthority = cbmSanctioningAuthority;
        setChanged();
    }
    
    ComboBoxModel getCbmSanctioningAuthority(){
        return this.cbmSanctioningAuthority;
    }
    
    void setCboSanctioningAuthority(String cboSanctioningAuthority){
        this.cboSanctioningAuthority = cboSanctioningAuthority;
        setChanged();
    }
    String getCboSanctioningAuthority(){
        return this.cboSanctioningAuthority;
    }
    
    void setTxtSanctionRemarks(String txtSanctionRemarks){
        this.txtSanctionRemarks = txtSanctionRemarks;
        setChanged();
    }
    String getTxtSanctionRemarks(){
        return this.txtSanctionRemarks;
    }
    
    void setCbmModeSanction(ComboBoxModel cbmModeSanction){
        
        this.cbmModeSanction = cbmModeSanction;
        setChanged();
    }
    
    ComboBoxModel getCbmModeSanction(){
        return this.cbmModeSanction;
    }
    
    void setCboModeSanction(String cboModeSanction){
        this.cboModeSanction = cboModeSanction;
        setChanged();
    }
    String getCboModeSanction(){
        return this.cboModeSanction;
    }
    
    void setTxtNoInstallments(String txtNoInstallments){
        this.txtNoInstallments = txtNoInstallments;
        setChanged();
    }
    String getTxtNoInstallments(){
        return this.txtNoInstallments;
    }
    
    void setCbmRepayFreq(ComboBoxModel cbmRepayFreq){
        this.cbmRepayFreq = cbmRepayFreq;
        setChanged();
    }
    
    ComboBoxModel getCbmRepayFreq(){
        return this.cbmRepayFreq;
    }
    
    void setCboRepayFreq(String cboRepayFreq){
        this.cboRepayFreq = cboRepayFreq;
        setChanged();
    }
    String getCboRepayFreq(){
        return this.cboRepayFreq;
    }
    
    public void setCbmTypeOfFacility(ComboBoxModel cbmTypeOfFacility){
        this.cbmTypeOfFacility = cbmTypeOfFacility;
        setChanged();
    }
    
    public ComboBoxModel getCbmTypeOfFacility(){
        return this.cbmTypeOfFacility;
    }
    
    // Update the cboTypeOfFacility in UI whenever the value changed since it is
    // not updated in update() method of TermLoanUI
    void setCboTypeOfFacility(String cboTypeOfFacility){
        this.cboTypeOfFacility = cboTypeOfFacility;
        setChanged();
    }
    String getCboTypeOfFacility(){
        return this.cboTypeOfFacility;
    }
    
    void setTxtLimit_SD(String txtLimit_SD){
        this.txtLimit_SD = txtLimit_SD;
        termLoanRepaymentOB.setStrLimitAmt(this.txtLimit_SD);
        setChanged();
    }
    String getTxtLimit_SD(){
        return this.txtLimit_SD;
    }
    
    public void setCbmAccStatus(ComboBoxModel cbmAccStatus){
        this.cbmAccStatus = cbmAccStatus;
        setChanged();
    }
    
    ComboBoxModel getCbmAccStatus(){
        return cbmAccStatus;
    }
    
    void setCboAccStatus(String cboAccStatus){
        this.cboAccStatus = cboAccStatus;
        setChanged();
    }
    String getCboAccStatus(){
        return this.cboAccStatus;
    }
    
    void setTdtFDate(String tdtFDate){
        this.tdtFDate = tdtFDate;
        setChanged();
    }
    String getTdtFDate(){
        return this.tdtFDate;
    }
    
    void setTxtFacility_Moratorium_Period(String txtFacility_Moratorium_Period){
        this.txtFacility_Moratorium_Period = txtFacility_Moratorium_Period;
        setChanged();
    }
    String getTxtFacility_Moratorium_Period(){
        return this.txtFacility_Moratorium_Period;
    }
    
    void setTdtFacility_Repay_Date(String tdtFacility_Repay_Date){
        this.tdtFacility_Repay_Date = tdtFacility_Repay_Date;
        setChanged();
    }
    String getTdtFacility_Repay_Date(){
        return this.tdtFacility_Repay_Date;
    }
    
    void setChkMoratorium_Given(boolean chkMoratorium_Given){
        this.chkMoratorium_Given = chkMoratorium_Given;
        setChanged();
    }
    boolean getChkMoratorium_Given(){
        return this.chkMoratorium_Given;
    }
    
    void setTdtTDate(String tdtTDate){
        this.tdtTDate = tdtTDate;
        setChanged();
    }
    String getTdtTDate(){
        return this.tdtTDate;
    }
    
    void setSanctionSlNo(String sanctionSlNo){
        this.sanctionSlNo = sanctionSlNo;
        setChanged();
        
    }
    String getSanctionSlNo(){
        return this.sanctionSlNo;
    }
    
    void setCbmProductId(ComboBoxModel cbmProductId){
        this.cbmProductId = cbmProductId;
        setChanged();
    }
    
    ComboBoxModel getCbmProductId(){
        return this.cbmProductId;
    }
    
    void setCboProductId(String cboProductId){
        this.cboProductId = cboProductId;
        setChanged();
    }
    String getCboProductId(){
        return this.cboProductId;
    }
    
    void setRdoSecurityDetails_Unsec(boolean rdoSecurityDetails_Unsec){
        this.rdoSecurityDetails_Unsec = rdoSecurityDetails_Unsec;
        setChanged();
    }
    boolean getRdoSecurityDetails_Unsec(){
        return this.rdoSecurityDetails_Unsec;
    }
    
    void setRdoSecurityDetails_Partly(boolean rdoSecurityDetails_Partly){
        this.rdoSecurityDetails_Partly = rdoSecurityDetails_Partly;
        setChanged();
    }
    boolean getRdoSecurityDetails_Partly(){
        return this.rdoSecurityDetails_Partly;
    }
    
    /**
     * @param rdoSecurityDetails_Fully
     */
    void setRdoSecurityDetails_Fully(boolean rdoSecurityDetails_Fully){
        this.rdoSecurityDetails_Fully = rdoSecurityDetails_Fully;
        setChanged();
    }
    boolean getRdoSecurityDetails_Fully(){
        return this.rdoSecurityDetails_Fully;
    }
    
    void setChkStockInspect(boolean chkStockInspect){
        this.chkStockInspect = chkStockInspect;
        setChanged();
    }
    boolean getChkStockInspect(){
        return this.chkStockInspect;
    }
    
    void setChkInsurance(boolean chkInsurance){
        this.chkInsurance = chkInsurance;
        setChanged();
    }
    boolean getChkInsurance(){
        return this.chkInsurance;
    }
    
    void setChkGurantor(boolean chkGurantor){
        this.chkGurantor = chkGurantor;
        setChanged();
    }
    boolean getChkGurantor(){
        return this.chkGurantor;
    }
    
    void setRdoAccType_New(boolean rdoAccType_New){
        this.rdoAccType_New = rdoAccType_New;
        setChanged();
    }
    boolean getRdoAccType_New(){
        return this.rdoAccType_New;
    }
    
    void setRdoAccType_Transfered(boolean rdoAccType_Transfered){
        this.rdoAccType_Transfered = rdoAccType_Transfered;
        setChanged();
    }
    boolean getRdoAccType_Transfered(){
        return this.rdoAccType_Transfered;
    }
    
    void setRdoAccLimit_Main(boolean rdoAccLimit_Main){
        this.rdoAccLimit_Main = rdoAccLimit_Main;
        setChanged();
    }
    boolean getRdoAccLimit_Main(){
        return this.rdoAccLimit_Main;
    }
    
    void setRdoAccLimit_Submit(boolean rdoAccLimit_Submit){
        this.rdoAccLimit_Submit = rdoAccLimit_Submit;
        setChanged();
    }
    boolean getRdoAccLimit_Submit(){
        return this.rdoAccLimit_Submit;
    }
    
    void setRdoNatureInterest_PLR(boolean rdoNatureInterest_PLR){
        this.rdoNatureInterest_PLR = rdoNatureInterest_PLR;
        setChanged();
    }
    boolean getRdoNatureInterest_PLR(){
        return this.rdoNatureInterest_PLR;
    }
    
    void setRdoNatureInterest_NonPLR(boolean rdoNatureInterest_NonPLR){
        this.rdoNatureInterest_NonPLR = rdoNatureInterest_NonPLR;
        setChanged();
    }
    boolean getRdoNatureInterest_NonPLR(){
        return this.rdoNatureInterest_NonPLR;
    }
    
    void setCbmInterestType(ComboBoxModel cbmInterestType){
        this.cbmInterestType = cbmInterestType;
        setChanged();
    }
    
    ComboBoxModel getCbmInterestType(){
        return this.cbmInterestType;
    }
    
    void setCboInterestType(String cboInterestType){
        this.cboInterestType = cboInterestType;
        setChanged();
    }
    String getCboInterestType(){
        return this.cboInterestType;
    }
    
    void setRdoRiskWeight_Yes(boolean rdoRiskWeight_Yes){
        this.rdoRiskWeight_Yes = rdoRiskWeight_Yes;
        setChanged();
    }
    boolean getRdoRiskWeight_Yes(){
        return this.rdoRiskWeight_Yes;
    }
    
    void setRdoRiskWeight_No(boolean rdoRiskWeight_No){
        this.rdoRiskWeight_No = rdoRiskWeight_No;
        setChanged();
    }
    boolean getRdoRiskWeight_No(){
        return this.rdoRiskWeight_No;
    }
    
    void setTdtDemandPromNoteDate(String tdtDemandPromNoteDate){
        this.tdtDemandPromNoteDate = tdtDemandPromNoteDate;
        setChanged();
    }
    String getTdtDemandPromNoteDate(){
        return this.tdtDemandPromNoteDate;
    }
    
    void setTdtDemandPromNoteExpDate(String tdtDemandPromNoteExpDate){
        this.tdtDemandPromNoteExpDate = tdtDemandPromNoteExpDate;
        setChanged();
    }
    String getTdtDemandPromNoteExpDate(){
        return this.tdtDemandPromNoteExpDate;
    }
    
    void setTdtAODDate(String tdtAODDate){
        this.tdtAODDate = tdtAODDate;
        setChanged();
    }
    String getTdtAODDate(){
        return this.tdtAODDate;
    }
    
    void setRdoMultiDisburseAllow_Yes(boolean rdoMultiDisburseAllow_Yes){
        this.rdoMultiDisburseAllow_Yes = rdoMultiDisburseAllow_Yes;
        setChanged();
    }
    boolean getRdoMultiDisburseAllow_Yes(){
        return this.rdoMultiDisburseAllow_Yes;
    }
    
    void setRdoMultiDisburseAllow_No(boolean rdoMultiDisburseAllow_No){
        this.rdoMultiDisburseAllow_No = rdoMultiDisburseAllow_No;
        setChanged();
    }
    boolean getRdoMultiDisburseAllow_No(){
        return this.rdoMultiDisburseAllow_No;
    }
    
    void setRdoSubsidy_Yes(boolean rdoSubsidy_Yes){
        this.rdoSubsidy_Yes = rdoSubsidy_Yes;
        setChanged();
    }
    boolean getRdoSubsidy_Yes(){
        return this.rdoSubsidy_Yes;
    }
    
    void setRdoSubsidy_No(boolean rdoSubsidy_No){
        this.rdoSubsidy_No = rdoSubsidy_No;
        setChanged();
    }
    boolean getRdoSubsidy_No(){
        return this.rdoSubsidy_No;
    }
    
    void setTxtPurposeDesc(String txtPurposeDesc){
        this.txtPurposeDesc = txtPurposeDesc;
        setChanged();
    }
    String getTxtPurposeDesc(){
        return this.txtPurposeDesc;
    }
    
    void setTxtGroupDesc(String txtGroupDesc){
        this.txtGroupDesc = txtGroupDesc;
        setChanged();
    }
    String getTxtGroupDesc(){
        return this.txtGroupDesc;
    }
    
    void setRdoInterest_Simple(boolean rdoInterest_Simple){
        this.rdoInterest_Simple = rdoInterest_Simple;
        setChanged();
    }
    boolean getRdoInterest_Simple(){
        return this.rdoInterest_Simple;
    }
    
    void setRdoInterest_Compound(boolean rdoInterest_Compound){
        this.rdoInterest_Compound = rdoInterest_Compound;
        setChanged();
    }
    boolean getRdoInterest_Compound(){
        return this.rdoInterest_Compound;
    }
    
    void setTxtContactPerson(String txtContactPerson){
        this.txtContactPerson = txtContactPerson;
        setChanged();
    }
    String getTxtContactPerson(){
        return this.txtContactPerson;
    }
    
    void setTxtContactPhone(String txtContactPhone){
        this.txtContactPhone = txtContactPhone;
        setChanged();
    }
    String getTxtContactPhone(){
        return this.txtContactPhone;
    }
    
    void setTxtRemarks(String txtRemarks){
        this.txtRemarks = txtRemarks;
        setChanged();
    }
    String getTxtRemarks(){
        return this.txtRemarks;
    }
    
    public String getTxtKoleLandArea() {
        return txtKoleLandArea;
    }

    public void setTxtKoleLandArea(String txtKoleLandArea) {
        this.txtKoleLandArea = txtKoleLandArea;
    }
    
    
    
    void setLblProductID_FD_Disp(String lblProductID_FD_Disp){
        this.lblProductID_FD_Disp = lblProductID_FD_Disp;
        setChanged();
    }
    String getLblProductID_FD_Disp(){
        return this.lblProductID_FD_Disp;
    }
    
    void setLblProdID_IDetail_Disp(String lblProdID_IDetail_Disp){
        this.lblProdID_IDetail_Disp = lblProdID_IDetail_Disp;
        setChanged();
    }
    String getLblProdID_IDetail_Disp(){
        return this.lblProdID_IDetail_Disp;
    }
    
    /**
     * will return the current status of the process
     * @return the current status of the process
     */
    public String getLblStatus(){
        return lblStatus;
    }
    
    /**
     * will set the current status of the process
     * @param lblStatus is the current status of the process
     */
    public void setLblStatus(String lblStatus) {
        this.lblStatus = lblStatus;
        setChanged();
    }
    /**
     * will reset the current status to Cancel mode
     */
    public void resetStatus(){
        this.setLblStatus(ClientConstants.ACTION_STATUS[0]);
    }
    
    /** To set the status based on ActionType, either New, Edit, etc., */
    public void setStatus(){
        this.setLblStatus( ClientConstants.ACTION_STATUS[this.getActionType()]);
        agriSubLimitOB.setLblStatus(ClientConstants.ACTION_STATUS[this.getActionType()]);
        
        ttNotifyObservers();
    }
    
    /** To update the Status based on result performed by doAction() method */
    public void setResultStatus(){
        this.setLblStatus(ClientConstants.RESULT_STATUS[this.getResult()]);
        ttNotifyObservers();
    }
    
    /**
     * It will call the notifyObservers()
     */
    public void ttNotifyObservers(){
        notifyObservers();
    }
    
    /**
     * This method will set the Borrower Number in all the TermLoanxxxxOB
     * @param borrowerNo is the Borrower Number
     */
    public void setBorrowerNo(String borrowerNo){
        termLoanCompanyOB.setBorrowerNo(borrowerNo);
        termLoanAuthorizedSignatoryOB.setBorrowerNo(borrowerNo);
        //        termLoanAuthorizedSignatoryInstructionOB.setBorrowerNo(borrowerNo);
        termLoanPoAOB.setBorrowerNo(borrowerNo);
        termLoanSecurityOB.setBorrowerNo(borrowerNo);
        termLoanInterestOB.setBorrowerNo(borrowerNo);
        termLoanRepaymentOB.setBorrowerNo(borrowerNo);
        termLoanGuarantorOB.setBorrowerNo(borrowerNo);
        termLoanDocumentDetailsOB.setBorrowerNo(borrowerNo);
        termLoanInterestOB.setBorrowerNo(borrowerNo);
        this.borrowerNo = borrowerNo;
        setChanged();
    }
    
    /**
     * returns the borrower number
     * @return the borrower number
     */
    public String getBorrowerNo(){
        return this.borrowerNo;
    }
    
    public void setLblAccHead_2(String lblAccHead_2){
        this.lblAccHead_2 = lblAccHead_2;
        setChanged();
    }
    
    public String getLblAccHead_2(){
        return this.lblAccHead_2;
    }
    
    public void setLblAccountHead_FD_Disp(String lblAccountHead_FD_Disp){
        this.lblAccountHead_FD_Disp = lblAccountHead_FD_Disp;
        setChanged();
    }
    
    public String getLblAccountHead_FD_Disp(){
        return this.lblAccountHead_FD_Disp;
    }
    
    public void setLblAccHead_Idetail_2(String lblAccHead_Idetail_2){
        this.lblAccHead_Idetail_2 = lblAccHead_Idetail_2;
        setChanged();
    }
    
    public String getLblAccHead_Idetail_2(){
        return this.lblAccHead_Idetail_2;
    }
    
    public void setLblAccNo_Idetail_2(String lblAccNo_Idetail_2){
        this.lblAccNo_Idetail_2 = lblAccNo_Idetail_2;
        setChanged();
    }
    
    public String getLblAccNo_Idetail_2(){
        return this.lblAccNo_Idetail_2;
    }
    
    public void setLblDrawingPower_2(String lblDrawingPower_2){
        this.lblDrawingPower_2 = lblDrawingPower_2;
        setChanged();
    }
    
    public String getLblDrawingPower_2(){
        return this.lblDrawingPower_2;
    }
    
    private void setMinLimitValue(Double minLimitValue){
        this.minLimitValue = minLimitValue;
        setChanged();
    }
    
    public Double getMinLimitValue(){
        return this.minLimitValue;
    }
    
    private void setMaxLimitValue(Double maxLimitValue){
        this.maxLimitValue = maxLimitValue;
        setChanged();
    }
    
    public Double getMaxLimitValue(){
        return this.maxLimitValue;
    }
    
    private void setMinLoanPeriod(Double minLoanPeriod){
        this.minLoanPeriod = minLoanPeriod;
        setChanged();
    }
    
    public Double getMinLoanPeriod(){
        return this.minLoanPeriod;
    }
    
    private void setMaxLoanPeriod(Double maxLoanPeriod){
        this.maxLoanPeriod = maxLoanPeriod;
        setChanged();
    }
    
    public Double getMaxLoanPeriod(){
        return this.maxLoanPeriod;
    }
    
    
    private void setMinDecLoanPeriod(String minDecLoanPeriod){
        this.minDecLoanPeriod = minDecLoanPeriod;
        setChanged();
    }
    
    public String getMinDecLoanPeriod(){
        return this.minDecLoanPeriod;
    }
    
    private void setMaxDecLoanPeriod(String maxDecLoanPeriod){
        this.maxDecLoanPeriod = maxDecLoanPeriod;
        setChanged();
    }
    
    public String getMaxDecLoanPeriod(){
        return this.maxDecLoanPeriod;
    }
    
    public void setTxtSanctionSlNo(String txtSanctionSlNo){
        this.txtSanctionSlNo = txtSanctionSlNo;
        setChanged();
    }
    
    public String getTxtSanctionSlNo(){
        return this.txtSanctionSlNo;
    }
    
    public void setStrRealSanctionNo(String strRealSanctionNo){
        this.strRealSanctionNo = strRealSanctionNo;
        setChanged();
    }
    
    public String getStrRealSanctionNo(){
        return this.strRealSanctionNo;
    }
    
    public void setStrACNumber(String strACNumber){
        termLoanSecurityOB.setStrACNumber(strACNumber);
        termLoanRepaymentOB.setStrACNumber(strACNumber);
        termLoanDocumentDetailsOB.setStrACNumber(strACNumber);
        termLoanClassificationOB.setStrACNumber(strACNumber);
        termLoanOtherDetailsOB.setStrACNumber(strACNumber);
        termLoanInterestOB.setStrACNumber(strACNumber);
        termLoanAdditionalSanctionOB.setStrACNumber(strACNumber);
//        agriSubSidyOB.setStrAcctNo(strACNumber);//dontdelete
        agriSubLimitOB.setSubLimitStrNo(strACNumber);
//        settlementOB.setStrAccNumber(strACNumber);
//        actTransOB.setAcctNum(strACNumber);//dontdelete
        this.strACNumber = strACNumber;
        setChanged();
    }
    
    public String getStrACNumber(){
        return this.strACNumber;
    }
    
    void setTblSanctionFacility(EnhancedTableModel tblSanctionFacility){
        this.tblSanctionFacility = tblSanctionFacility;
        setChanged();
    }
    
    EnhancedTableModel getTblSanctionFacility(){
        return this.tblSanctionFacility;
    }
    
    void setTblSanctionMain(EnhancedTableModel tblSanctionMain){
        this.tblSanctionMain = tblSanctionMain;
        setChanged();
    }
    
    EnhancedTableModel getTblSanctionMain(){
        return this.tblSanctionMain;
    }
    
    void setTblFacilityTabSanction(EnhancedTableModel tblSanctionFacility){
        this.tblSanctionFacility = tblSanctionFacility;
        setChanged();
    }
    
    EnhancedTableModel getTblFacilityTabSanction(){
        return this.tblSanctionFacility;
    }
    
    void setTblFacilityTabMainSanction(EnhancedTableModel tblSanctionMain){
        this.tblSanctionMain = tblSanctionMain;
        setChanged();
    }
    
    EnhancedTableModel getTblFacilityTabMainSanction(){
        return this.tblSanctionMain;
    }
    
    /**
     * This method will be called at the time of Facility Tab save button
     * pressed. This will retrieve the corresponding borrower number
     * based on custId, acctStatus, constitution, category, references
     * and populate it in Borrower Tab.
     */
    public void setBorrowerNumber(){
        try{
            if (termLoanBorrowerOB.getLblBorrowerNo_2().length() <= 0){
                HashMap transactionMap = new HashMap();
                HashMap retrieve = new HashMap();
                String mapName = "getBorrowerNumber";
                if(loanType.equals("LTD")) {
                    transactionMap.put("LOAN_NO", getLoanACNo());
                    mapName = "getBorrowerNumberForLTD";
                } else {
                    transactionMap.put("custId", termLoanBorrowerOB.getTxtCustID());
                    transactionMap.put("constitution", termLoanBorrowerOB.getCbmConstitution().getKeyForSelected());
                    transactionMap.put("category", termLoanBorrowerOB.getCbmCategory().getKeyForSelected());
                }
                List resultList = (List) ClientUtil.executeQuery(mapName, transactionMap);
                if (resultList.size() > 0){
                    // If atleast one Record exist
                    retrieve = (HashMap) resultList.get(0);
                    setBorrowerNo(CommonUtil.convertObjToStr(retrieve.get("BORROW_NO")));
                    termLoanBorrowerOB.setLblBorrowerNo_2(CommonUtil.convertObjToStr(retrieve.get("BORROW_NO")));
                }
                retrieve = null;
                transactionMap = null;
                resultList = null;
            }
        }catch(Exception e){
            log.info("Exception in setBorrowerNumber(): "+e);
            parseException.logException(e,true);
        }
    }
    
    /**
     * To update the Product ID for the tables which are all having Account
     * Number as a key
     * @param sanctionNo is the position of the record selected in
     * the sanction details table
     * @param slno is the position of the record selected in
     * the sanction facility details table
     */
    public void updateProd_ID(int sanctionNo, int slno){
        try{
            String strsanctionNo = CommonUtil.convertObjToStr(((ArrayList)tblSanctionMain.getDataArrayList().get(sanctionNo)).get(1));

            slno           = CommonUtil.convertObjToInt(((ArrayList)tblSanctionFacility.getDataArrayList().get(slno)).get(0));
            
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
            ClientUtil.execute("updateProdID_TermLoanGuarantorTO", transactionMap);
            ClientUtil.execute("updateProdID_TermLoanInterestTO", transactionMap);
            ClientUtil.execute("updateProdID_TermLoanClassificationTO", transactionMap);
            
            transactionMap = null;
        }catch(Exception e){
            log.info("Exception in updateProd_ID:  "+e);
            parseException.logException(e,true);
        }
    }
    
    /**
     * To display Account Head in Facility Details and its following Tabs
     */
    public void setFacilityAcctHead(){
        try{
            HashMap transactionMap = new HashMap();
            HashMap retrieve;
            transactionMap.put("prodId", getCbmProductId().getKeyForSelected());
            transactionMap.put("PROD_ID", getCbmProductId().getKeyForSelected());
            
            List resultList1 = ClientUtil.executeQuery("getProdIntDetails", transactionMap);
            List resultList2 = ClientUtil.executeQuery("TermLoan.getProdHead", transactionMap);
            List resultList3 = ClientUtil.executeQuery("TermLoan.getProduct_Details", transactionMap);
            
            if (resultList1.size() > 0){
                // If Product Account Head exist in Database
                retrieve = (HashMap) resultList1.get(0);
                if (retrieve.containsKey(PLR_RATE_APPL)){
                    if (retrieve.get(PLR_RATE_APPL).equals(YES)){
                        setRdoNatureInterest_PLR(true);
                        setRdoNatureInterest_NonPLR(false);
                    }else if (retrieve.get(PLR_RATE_APPL).equals(NO)){
                        setRdoNatureInterest_NonPLR(true);
                        setRdoNatureInterest_PLR(false);
                    }
                }
            }
            retrieve = null;
            
            if (resultList2.size() > 0){
                // If Product Account Head exist in Database
                retrieve = (HashMap) resultList2.get(0);
                setLblAccHead_2(CommonUtil.convertObjToStr(retrieve.get("AC_HEAD")));
                setLblAccountHead_FD_Disp(CommonUtil.convertObjToStr(retrieve.get("AC_HEAD")));
                setEligibleMargin(CommonUtil.convertObjToDouble(retrieve.get("DEP_ELIGIBLE_LOAN_AMT")).doubleValue());
                setSanctionAmtRoundOff(CommonUtil.convertObjToStr(retrieve.get("DEPOSIT_ROUNDOFF")));
                setProductCategory(CommonUtil.convertObjToStr(retrieve.get("AUTHORIZE_REMARK")));
            }else{
                setEligibleMargin(0.0);
                setSanctionAmtRoundOff("");
            }
            retrieve = null;
            if (resultList3.size() > 0){
                retrieve = (HashMap) resultList3.get(0);
                if (retrieve.containsKey(SUBSIDY) && retrieve.get(SUBSIDY) !=null){
                    if (retrieve.get(SUBSIDY).equals(YES)){
                        setRdoSubsidy_Yes(true);
                        setRdoSubsidy_No(false);
                    }else if (retrieve.get(SUBSIDY).equals(NO)){
                        setRdoSubsidy_No(true);
                        setRdoSubsidy_Yes(false);
                    }
                }
                setMinLimitValue(CommonUtil.convertObjToDouble(retrieve.get("MIN_AMT_LOAN")));
                setMaxLimitValue(CommonUtil.convertObjToDouble(retrieve.get("MAX_AMT_LOAN")));
                setMaxLoanPeriod(CommonUtil.convertObjToDouble(retrieve.get("MAX_PERIOD")));
                setMinLoanPeriod(CommonUtil.convertObjToDouble(retrieve.get("MIN_PERIOD")));
                setMaxLoanPeriodChar(CommonUtil.convertObjToStr(retrieve.get("MAX_PERIOD_CHAR")));
                termLoanRepaymentOB.setInterest_Rate(CommonUtil.convertObjToDouble(retrieve.get("APPL_INTEREST")).doubleValue());
            }
            setAllTabProdID(CommonUtil.convertObjToStr(getCbmProductId().getKeyForSelected()));
            setAllTabAccHead();
            retrieve = null;
            transactionMap = null;
            resultList1 = null;
            resultList2 = null;
            resultList3 = null;
        }catch(Exception e){
            log.info("Exception caught in setFacilityAcctHead: "+e);
            parseException.logException(e,true);
        }
    }
    
    public void setFacilityContactDetails(String CustID){
        try{
            HashMap retrieve;
            List resultList = termLoanBorrowerOB.getCustOpenDate(CustID);
            if (resultList.size() > 0){
                // If atleast one Record exist
                retrieve = (HashMap) resultList.get(0);
                if (retrieve.get("CUST_TYPE").equals("CORPORATE")){
                    // If it is the Corporate Customer
                    setTxtContactPerson(CommonUtil.convertObjToStr(retrieve.get("COMP_NAME")));
                }else{
                    setTxtContactPerson(CommonUtil.convertObjToStr(retrieve.get("CUSTOMER NAME")));
                }
            }
            StringBuffer stbPhone = new StringBuffer("");
            int phoneCount = 0;
            List phoneList = termLoanBorrowerOB.getCustPhone(CustID);
            ////System.out.println("phoneList===="+phoneList);
            if (phoneList.size() > 0){
                // To retrieve the Contact Phone Numbers of the Customer
                for (int i = phoneList.size() - 1,j = 0;i >= 0;--i,++j){
                    retrieve = (HashMap) phoneList.get(j);
                    if (retrieve.get("PHONE_TYPE_ID") !=null && (retrieve.get("PHONE_TYPE_ID")).equals("LAND LINE")){
                        // If the Phone Type ID is Land Line
                        if (phoneCount != 0){
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
        }catch(Exception e){
            log.info("Exception caught in setFacilityContactDetails: "+e);
            parseException.logException(e,true);
        }
    }
    
    /**
     * To check whether the Limit entered in UI is falling within
     * the minimum and maximum loan amount in Loan Product
     * @param strEnteredValue is the loan amount value entered in
     * the Sanction Facility Details
     * @return true if the limit amount entered in UI fall within
     * the minimum and maximum value of Loan Product else false
     */
    public boolean checkLimitValue(String strEnteredValue){
        boolean returnValue = false;
        try{
            Double limitValueUI = CommonUtil.convertObjToDouble(strEnteredValue);
            if ((getMaxLimitValue().doubleValue() >= limitValueUI.doubleValue()) && (getMinLimitValue().doubleValue() <= limitValueUI.doubleValue())){
                returnValue = true;
            }
            limitValueUI = null;
        }catch(Exception e){
            log.info("Exception caught in checkLimitValue:  "+e);
            parseException.logException(e,true);
        }
        return returnValue;
    }
    
    
    /**
     * To check whether the Period entered in UI is matched
     * with the minimum and maximum period in Loan Product
     * @return true if the entered period in UI fall within
     * minimum and maximum period of Loan Product else false
     * @param moratorium_Period is the no. of months for moratorium
     * @param noInstall is the String object having number of installment value
     */
    public boolean checkFacilityPeriod(String noInstall, String moratorium_Period){
        boolean returnValue = false;
        try{
            Double frequency = CommonUtil.convertObjToDouble(getCbmRepayFreq().getKeyForSelected());
            Double no_Install = CommonUtil.convertObjToDouble(noInstall);
            int period = no_Install.intValue() * frequency.intValue();
            period = period + (CommonUtil.convertObjToInt(moratorium_Period) * 30);
            if ((getMaxLoanPeriod().intValue() >= period) && (getMinLoanPeriod().intValue() <= period)){
                returnValue = true;
            }
            frequency = null;
            no_Install = null;
        }catch(Exception e){
            log.info("Exception caught in checkFacilityPeriod:  "+e);
            parseException.logException(e,true);
        }
        return returnValue;
    }
    
    /**
     * To check whether the Period entered in UI is matched
     * with the minimum and maximum period in Loan Product
     * @return true if the entered period in UI fall within
     * minimum and maximum period of Loan Product else false
     * @param strFromDate is the facility from date
     * @param strToDate is the facility to date
     * @param noInstall is the String object having number of installment value
     */
    public boolean checkFacilityPeriod(java.util.Date datFromDate, java.util.Date datToDate){
        boolean returnValue = false;
        try{
            double period = DateUtil.dateDiff(datFromDate, datToDate);
            if ((getMaxLoanPeriod().doubleValue() >= period) && (getMinLoanPeriod().doubleValue() <= period)){
                returnValue = true;
            }
        }catch(Exception e){
            log.info("Exception caught in checkFacilityPeriod:  "+e);
            parseException.logException(e,true);
        }
        return returnValue;
    }
    
    
    // to find the value of multiplier on the basis of period...
    public double setCombo(String duration) throws Exception{
        periodData=0;
        resultData=0;
        int period=0;
        if(!duration.equalsIgnoreCase("")){
            if( duration.equals(DAY))
                period = day;
            else if(duration.equals(MONTH))
                period = month;
            else if(duration.equals(YEAR))
                period = year;
        }
        
        duration = "";
        return period;
    }
    
     public void setDailyLoanComponentValue(){
         try{
         if(getActionType()==ClientConstants.ACTIONTYPE_NEW){
             Date toDate =null;
             setTxtNoInstallments(String.valueOf(1));
             setTdtFDate(DateUtil.getStringDate(curDate));
             
              if(getMaxLoanPeriodChar().equals("DAYS")){
                   toDate=DateUtil.addDays(curDate,CommonUtil.convertObjToDouble(getTxtDirectRepaymentLoanPeriod()).intValue(),true);
                     cbmDirectRepaymentLoanPeriod.setKeyForSelected("DAYS");
                     resultValue =CommonUtil.convertObjToDouble(getTxtDirectRepaymentLoanPeriod()).intValue();
                     ////System.out.println("resultValue111@@@>>>"+resultValue);
                     setTxtDirectRepaymentLoanPeriod(String.valueOf(resultValue));
             
              }else{
                   toDate=DateUtil.addDays(curDate,CommonUtil.convertObjToDouble(getTxtDirectRepaymentLoanPeriod()).intValue());
                   resultValue =CommonUtil.convertObjToDouble(getTxtDirectRepaymentLoanPeriod()).intValue();
                   String period = setPeriod(resultValue);
                   cbmDirectRepaymentLoanPeriod.setSelectedItem(period);
                   ////System.out.println("CommonUtil.convertObjToDouble(getTxtDirectRepaymentLoanPeriod())>>"+CommonUtil.convertObjToDouble(getTxtDirectRepaymentLoanPeriod()));
                    ////System.out.println("resultValue222@@@>>>"+resultValue);
                   setTxtDirectRepaymentLoanPeriod(String.valueOf(resultValue));
                   
              }
              resetPeriod();
              ////System.out.println("toDate@@11>>>>"+toDate);
             setTdtTDate(DateUtil.getStringDate(toDate));
             setTdtFacility_Repay_Date(DateUtil.getStringDate(toDate));
            
             cbmRepayFreq.setSelectedItem("User Defined");
             
             ttNotifyObservers();
//             checkFacilityPeriod(curDate,toDate);
             
         }
         }catch(Exception e){
             e.printStackTrace();
         }
    }
    public boolean checkDirectRepaymentTODate(double noOfDays,String strPeriod)throws Exception{
        
//        resultValue = CommonUtil.convertObjToInt(objLoanProductNonPerAssetsTO.getPeriodTransDoubtful2());
//        period = setPeriod(resultValue);
//        setCboPeriodTransDoubtfulAssets2(period);
//        setTxtPeriodTransDoubtfulAssets2(String.valueOf(resultValue));
//        resetPeriod();
        
        
//         if(cboMinPeriodsArrears.length() > 0){
                duration = ((String)cbmDirectRepaymentLoanPeriod.getKeyForSelected());
                periodData = setCombo(duration);
                resultData = periodData * noOfDays;
                String todate =DateUtil.getStringDate(DateUtil.addDays(curDate,(int)resultData));
               
                if(getMaxLoanPeriod().doubleValue()<resultData){
                    ClientUtil.showMessageWindow("Please Selected Value should be less than Max period of Loan");
                    return true;
                }
                ////System.out.println("todate111@@@>>>>"+todate);
                setTdtTDate(todate);
                setTdtFacility_Repay_Date(todate);
//                tdtDirect_Repay_Date.setDateValue(observable.getTdtFacility_Repay_Date());
                
                //objLoanProductNonPerAssetsTO.setMinPeriodArrears( new Double(resultData));
                
//            }
        
//         checkFacilityPeriod(curDate,toDate);
        
        
        
// str = periodLengthValidation(txtPeriodTransDoubtfulAssets2, cboPeriodTransDoubtfulAssets2);
//        if(str.length() > 0){
//            strBAlert.append(str+"\n");
//            str = "";
//        }
                return false;
    }
    public boolean isDepositDaily(HashMap hash){
        if(hash.get("BEHAVES_LIKE").equals("DAILY")) {
            double depPeriod=CommonUtil.convertObjToDouble(hash.get("PREMATURE_WITHDRAWAL")).doubleValue();
            if(DateUtil.dateDiff((Date)hash.get("DEPOSIT_DT"), curDate)>=depPeriod){
                return false;
            } else
                return true;
        }else
            return false;
    }
    
    public void getPaddyFullDetails(HashMap hash){
        List resultList;
        resultList = ClientUtil.executeQuery("getPaddyFullDetails", hash);
        if (resultList.size() > 0){
            hash.putAll((HashMap)resultList.get(0));
        }
    }
    
    public boolean setDetailsForLTD(HashMap hash) {
        try{
            HashMap resultMap = new HashMap();
            HashMap resultCustMap = new HashMap();
            depositCustDetMap = new HashMap();
            depositCustDetMap.putAll(hash);
            ////System.out.println("### setDetailsForLTD - depositCustDetMap 1 : "+depositCustDetMap);
            List resultList;
            List recultCustid=null;
            if (getActionType() == ClientConstants.ACTIONTYPE_AUTHORIZE || getActionType() == ClientConstants.ACTIONTYPE_REJECT)
                resultList = ClientUtil.executeQuery("getDepositLienDetailsForLTD", hash);
            else{
                resultList = ClientUtil.executeQuery("getDepositDetailsForLTD", hash);
                recultCustid = ClientUtil.executeQuery("getDepositCustDetailsForLTD", hash);
                
            }
            if(recultCustid !=null && recultCustid.size()>0){
                resultCustMap=(HashMap)recultCustid.get(0);
                
            }
            if (resultList.size() > 0){
                resultMap = (HashMap) resultList.get(0);
                ////System.out.println("ternLoanBorrower###"+termLoanBorrowerOB.getTxtCustID());
                if(resultCustMap !=null && CommonUtil.convertObjToStr(resultCustMap.get("CUST_ID")).equals(termLoanBorrowerOB.getTxtCustID()))
                    return true;
                else if(resultCustMap !=null && resultCustMap.containsKey("LIEN_AC_NO")){
                    ClientUtil.showMessageWindow("Deposit Holder"+resultMap.get("DEPOSIT_NO")+"\n"+
                    "Already Disbursed This LoanHolder" + resultCustMap.get("LIEN_AC_NO"));
                    return false;
                }
                //                else
                //                    return true;
                depositCustDetMap.putAll(resultMap);
                ////System.out.println("### setDetailsForLTD - depositCustDetMap 2 : "+depositCustDetMap);
                //                setTdtTDate(CommonUtil.convertObjToStr(resultMap.get("MATURITY_DT")));
                setTdtTDate(DateUtil.getStringDate((java.util.Date)resultMap.get("MATURITY_DT")));
                tdtRepaymentDate = DateUtil.getStringDate((java.util.Date)resultMap.get("MATURITY_DT"));
                return true;
            }
            resultMap = null;
            resultList = null;
            
        }catch(Exception e){
            log.info("Exception caught in setDetailsForLTD : "+e);
            parseException.logException(e,true);
        }
        return false;
    }
    
    
    public void populatePeriodDifference(String strNoInstall, String strRepayFreq, String strMoratorium){
        try{
            int period = 0;
            int yrs = 0;
            int months = 0;
            int days = 0;
            if (strRepayFreq.length() > 0){
                // If the Repayment Frequency is Lump Sum
                if (strRepayFreq.equals("1")){
                  //  days++;
                     days += CommonUtil.convertObjToInt(strNoInstall);
                }
                // If the Repayment Frequency is Monthly
                if (strRepayFreq.equals("30")){
                    months += CommonUtil.convertObjToInt(strNoInstall);
                }
                // If the Repayment Frequency is Quaterly
                if (strRepayFreq.equals("90")){
                    months += (CommonUtil.convertObjToInt(strNoInstall) * 3);
                }
                // If the Repayment Frequency is Half yearly
                if (strRepayFreq.equals("180")){
                    months += (CommonUtil.convertObjToInt(strNoInstall) * 6);
                }
                // If the Repayment Frequency is Yearly
                if (strRepayFreq.equals("365")){
                    yrs += CommonUtil.convertObjToInt(strNoInstall);
                }
            }
            if (strMoratorium.length() > 0){
                months += CommonUtil.convertObjToInt(strMoratorium);
            }
              while (days >= 30){
                days -= 30;
                months++;
            }
            while (months >= 12){
                months -= 12;
                yrs++;
            }
           
            setTxtPeriodDifference_Years(String.valueOf(yrs));
            setTxtPeriodDifference_Months(String.valueOf(months));
            setTxtPeriodDifference_Days(String.valueOf(days));
        }catch(Exception e){
            log.info("Exception caught in populatePeriodDifference:  "+e);
            parseException.logException(e,true);
        }
    }
    
    public void populatePeriodDifference(String strFromDate, String strToDate){
        try{
            double difference = DateUtil.dateDiff(DateUtil.getDateMMDDYYYY(strFromDate), DateUtil.getDateMMDDYYYY(strToDate));
            int period = (int)difference;
            int yrs = period / 365;
            int months = (period - (yrs * 365)) / 30;
            int days = (period - ((yrs * 365) + (months * 30)));
            
            setTxtPeriodDifference_Years(String.valueOf(yrs));
            setTxtPeriodDifference_Months(String.valueOf(months));
            setTxtPeriodDifference_Days(String.valueOf(days));
        }catch(Exception e){
            log.info("Exception caught in populatePeriodDifference:  "+e);
            parseException.logException(e,true);
        }
    }
    /**
     * To display the minimum and maximum Loan Period
     * in Year wise, Monthly wise, Day wise
     */
    public void decoratePeriod(){
        try{
            final int minPeriod = getMinLoanPeriod().intValue();
            final int maxPeriod = getMaxLoanPeriod().intValue();
            if (minPeriod%360 == 0 || minPeriod%365 == 0){
                setMinDecLoanPeriod((minPeriod/360)+" Year(s)");
            }else if (minPeriod%30 == 0){
                setMinDecLoanPeriod((minPeriod/30)+" Month(s)");
            }else{
                setMinDecLoanPeriod(minPeriod+" Day(s)");
            }
            if (maxPeriod%360 == 0 || maxPeriod%365 == 0){
                setMaxDecLoanPeriod((maxPeriod/360)+" Year(s)");
            }else if (maxPeriod%30 == 0){
                setMaxDecLoanPeriod((maxPeriod/30)+" Month(s)");
            }else{
                setMaxDecLoanPeriod(maxPeriod+" Day(s)");
            }
        }catch(Exception e){
            log.info("Exception caught in decoratePeriod:  "+e);
            parseException.logException(e,true);
        }
    }
    
    public Date getProperDateFormat(Object obj) {
        Date currDt = null;
        if (obj!=null && obj.toString().length()>0) {
            Date tempDt= DateUtil.getDateMMDDYYYY(CommonUtil.convertObjToStr(obj));
            currDt=(Date)curDate.clone();
            currDt.setDate(tempDt.getDate());
            currDt.setMonth(tempDt.getMonth());
            currDt.setYear(tempDt.getYear());
        }
        return currDt;
    }
    
    
    private void setAllTabProdID(String prodID){
        setLblProductID_FD_Disp(prodID);
        termLoanSecurityOB.setLblProdId_Disp(prodID);
        termLoanRepaymentOB.setLblProdID_RS_Disp(prodID);
        termLoanGuarantorOB.setLblProdID_GD_Disp(prodID);
        termLoanDocumentDetailsOB.setLblProdID_Disp_DocumentDetails(prodID);
        termLoanInterestOB.setLblProdID_IM_Disp(prodID);
        termLoanClassificationOB.setLblProdID_CD_Disp(prodID);
        termLoanOtherDetailsOB.setLblProdID_Disp_ODetails(prodID);
    }
    
    private void setAllTabAccHead(){
        termLoanSecurityOB.setLblAccHeadSec_2(getLblAccHead_2());
        termLoanRepaymentOB.setLblAccHead_RS_2(getLblAccHead_2());
        termLoanGuarantorOB.setLblAccHead_GD_2(getLblAccHead_2());
        termLoanDocumentDetailsOB.setLblAcctHead_Disp_DocumentDetails(getLblAccHead_2());
        termLoanInterestOB.setLblAccHead_IM_2(getLblAccHead_2());
        termLoanClassificationOB.setLblAccHead_CD_2(getLblAccHead_2());
        termLoanOtherDetailsOB.setLblAcctHead_Disp_ODetails(getLblAccHead_2());
    }
    
    private void updateProdID_AccHead(){
        termLoanSecurityOB.setLblAccHeadSec_2(getLblAccountHead_FD_Disp());
        termLoanRepaymentOB.setLblAccHead_RS_2(getLblAccountHead_FD_Disp());
        termLoanGuarantorOB.setLblAccHead_GD_2(getLblAccountHead_FD_Disp());
        termLoanDocumentDetailsOB.setLblAcctHead_Disp_DocumentDetails(getLblAccountHead_FD_Disp());
        setLblAccHead_Idetail_2(getLblAccountHead_FD_Disp());
        termLoanInterestOB.setLblAccHead_IM_2(getLblAccountHead_FD_Disp());
        termLoanClassificationOB.setLblAccHead_CD_2(getLblAccountHead_FD_Disp());
        termLoanOtherDetailsOB.setLblAcctHead_Disp_ODetails(getLblAccountHead_FD_Disp());
        termLoanSecurityOB.setLblProdId_Disp(getLblProductID_FD_Disp());
        termLoanRepaymentOB.setLblProdID_RS_Disp(getLblProductID_FD_Disp());
        termLoanGuarantorOB.setLblProdID_GD_Disp(getLblProductID_FD_Disp());
        termLoanDocumentDetailsOB.setLblProdID_Disp_DocumentDetails(getLblProductID_FD_Disp());
        setLblProdID_IDetail_Disp(getLblProductID_FD_Disp());
        termLoanInterestOB.setLblProdID_IM_Disp(getLblProductID_FD_Disp());
        termLoanClassificationOB.setLblProdID_CD_Disp(getLblProductID_FD_Disp());
        termLoanOtherDetailsOB.setLblProdID_Disp_ODetails(getLblProductID_FD_Disp());
        setChanged();
    }
    
    public void createTableUtilSanctionFacility(){
        tableUtilSanction_Facility = null;
        tableUtilSanction_Facility = new TableUtil();
        tableUtilSanction_Facility.setAttributeKey(SLNO);
    }
    
    private void periodMultipleMessage(){
        StringBuffer strPeriod = new StringBuffer("The Loan Period is in multiples of ");
        try{
            HashMap transactionMap = new HashMap();
            HashMap retrieve;
            transactionMap.put("prodId", getCbmProductId().getKeyForSelected());
            List resultList = ClientUtil.executeQuery("TermLoan.getLoanPeriodMultiples", transactionMap);
            if (resultList.size() > 0){
                // If Product Account Head exist in Database
                retrieve = (HashMap) resultList.get(0);
                
                if (!CommonUtil.convertObjToStr(retrieve.get("LOAN_PERIODS_MULTIPLES")).equals(CommonUtil.convertObjToStr(getCbmRepayFreq().getKeyForSelected()))){
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
        }catch(Exception e){
            log.info("Exception caught in periodMultipleMessage: "+e);
            parseException.logException(e,true);
        }
    }
    
    public int addSanctionFacilityTab(int row, boolean update, boolean updateMain, int mainSlNo, int facilityTabSlNo){
        int option = -1;
        try{
            
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
            Set keySet =  sanctionFacilityAll.keySet();
            Object[] objKeySet = (Object[]) keySet.toArray();
            insertSanctionFacility(dataSize+1);
            // If the Mode is not update then insert the record
            if (!update){
                // If the table is not in Edit Mode
                if (updateMain == true){
                    if (objKeySet.length > 0){
                        strSanctionSlNo = CommonUtil.convertObjToStr(((HashMap) sanctionFacilityAll.get(objKeySet[0])).get(SANCTION_NO));
                        sanctionFacilityRecord.put(SANCTION_NO, strSanctionSlNo);
                    }
                }
                result = tableUtilSanction_Facility.insertTableValues(sanctionFacilityTabRow, sanctionFacilityRecord);
                
                sanctionFacilityAllTabRecords = (ArrayList) result.get(TABLE_VALUES);
                sanctionFacilityAll = (LinkedHashMap)result.get(ALL_VALUES);
                option = CommonUtil.convertObjToInt(result.get(OPTION));
                // To save the changes in Sanction Main at the time of Sanction main edit
                if (updateMain == true){
                    sanctionMainRec = (HashMap) sanctionMainAll.get(((ArrayList) sanctionMainAllTabRecords.get(mainSlNo)).get(0));
                    sanctionMainRec.put(SANCTION_FACILITY_ALL, sanctionFacilityAll);
                    sanctionMainRec.put(SANCTION_FACILITY_TABLE, sanctionFacilityAllTabRecords);
                    sanctionMainAll.put(((ArrayList) sanctionMainAllTabRecords.get(mainSlNo)).get(0), sanctionMainRec);
                }
                tblSanctionFacility.setDataArrayList(sanctionFacilityAllTabRecords, sanctionFacilityTitle);
                option = dataSize;
            }else{
                option = updateSanctionFacility(row, updateMain, mainSlNo);
                option = row;
            }
            setChanged();
            keySet = null;
            objKeySet = null;
            data = null;
            sanctionMainRec = null;
            result = null;
        }catch(Exception e){
            log.info("Exception caught in addSanctionFacilityTab: "+e);
            parseException.logException(e,true);
        }
        return option;
    }
    
    private void insertSanctionFacility(int slno){
        try{
            sanctionFacilityTabRow.add(String.valueOf(slno));
            sanctionFacilityTabRow.add(getCboTypeOfFacility());
            sanctionFacilityTabRow.add(CommonUtil.convertObjToStr(txtLimit_SD));
            sanctionFacilityTabRow.add(tdtFDate);
            sanctionFacilityTabRow.add(tdtTDate);
            
            sanctionFacilityRecord.put(SLNO,  String.valueOf(slno));
//            sanctionFacilityRecord.put(SANCTION_NO, getTxtSanctionNo());
            sanctionFacilityRecord.put(FACILITY_TYPE, CommonUtil.convertObjToStr(cbmTypeOfFacility.getKeyForSelected()));
            sanctionFacilityRecord.put(LIMIT, CommonUtil.convertObjToStr(txtLimit_SD));
            sanctionFacilityRecord.put(INITIAL_MONEY_DEPOSIT, CommonUtil.convertObjToStr(txtLimit_SDMoneyDeposit));
            sanctionFacilityRecord.put(FROM, tdtFDate);
            sanctionFacilityRecord.put(TO, tdtTDate);
            if (getChkMoratorium_Given()){
                sanctionFacilityRecord.put(MORATORIUM_GIVEN, "Y");
            }else{
                sanctionFacilityRecord.put(MORATORIUM_GIVEN, "N");
            }
            sanctionFacilityRecord.put(FACILITY_REPAY_DATE, getTdtFacility_Repay_Date());
            sanctionFacilityRecord.put(MORATORIUM_PERIOD, getTxtFacility_Moratorium_Period());
            sanctionFacilityRecord.put(PROD_ID, CommonUtil.convertObjToStr(cbmProductId.getKeyForSelected()));
            sanctionFacilityRecord.put(REPAY_FREQ, CommonUtil.convertObjToStr(cbmRepayFreq.getKeyForSelected()));
            sanctionFacilityRecord.put(NO_INSTALLMENTS, CommonUtil.convertObjToStr(txtNoInstallments));
            sanctionFacilityRecord.put(SANCTION_NO, getTxtSanctionSlNo());
            sanctionFacilityRecord.put(COMMAND, "");
        }catch(Exception e){
            log.info("Exception caught in insertSanctionFacility: "+e);
            parseException.logException(e,true);
        }
    }
    
    // Update the cboTypeOfFacility after calling this method in UI
    public void populateSanctionFacility(int slno){
        try{
            HashMap eachRecs = new HashMap();
            Set keySet =  sanctionFacilityAll.keySet();
            Object[] objKeySet = (Object[]) keySet.toArray();
            ArrayList sanctionFacilityTableValue = (ArrayList)tblSanctionFacility.getDataArrayList().get(slno);
            ////System.out.println("populate sanction facility #####"+sanctionFacilityTableValue);
            // To populate the corresponding Sanction Facility Details
            for (int i = sanctionFacilityAll.size() - 1,j = 0;i >= 0;--i,++j){
                // If the record matches with the key then populate it in UI
                if ((CommonUtil.convertObjToStr(((HashMap) sanctionFacilityAll.get(objKeySet[j])).get(SLNO))).equals(CommonUtil.convertObjToStr(sanctionFacilityTableValue.get(0)))){
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
                    if (CommonUtil.convertObjToStr(eachRecs.get(MORATORIUM_GIVEN)).equals("Y")){
                        setChkMoratorium_Given(true);
                    }else if (CommonUtil.convertObjToStr(eachRecs.get(MORATORIUM_GIVEN)).equals("N")){
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
        }catch(Exception e){
            log.info("Exception caught in populateSanctionFacility: "+e);
            parseException.logException(e,true);
        }
    }
    
    public void getPLR_Rate(String prod_id){
        try{
            HashMap transactionMap = new HashMap();
            HashMap retrieve = new HashMap();
            transactionMap.put("PROD_ID", prod_id);
            List resultList = ClientUtil.executeQuery("getProdIntDetails", transactionMap);
            if (resultList.size() > 0){
                // If the result contains atleast one record
                retrieve = (HashMap) resultList.get(0);
                if (retrieve.get(PLR_RATE_APPL).equals(YES)){
                    termLoanInterestOB.setLblPLR_Limit_2(CommonUtil.convertObjToStr(retrieve.get(PLR_RATE)));
                }
            }
            retrieve = null;
            transactionMap = null;
            resultList = null;
        }catch(Exception e){
            log.info("Exception caught in getPLR_Rate: "+e);
            parseException.logException(e,true);
        }
    }
    
    public int getIncrementType(){
        int incType = 0;
        try{
            Double incVal = CommonUtil.convertObjToDouble(cbmRepayFreq.getKeyForSelected());
            incType = incVal.intValue();
            incVal = null;
        }catch(Exception e){
            log.info("Exception caught in getIncrementType: "+e);
            parseException.logException(e,true);
        }
        return incType;
    }
    public int getRepayScheduleIncrementType(){
        int incType = 0;
        try{
            Double incVal = CommonUtil.convertObjToDouble(cbmRepayFreq.getKeyForSelected());
            incType = incVal.intValue();
            incVal = null;
        }catch(Exception e){
            log.info("Exception caught in getIncrementType: "+e);
            parseException.logException(e,true);
        }
        return incType;
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
    
    public void populateSanctionTabProdID(int slno){
        try{
            if (slno != -1 && (tblSanctionFacility.getRowCount() != 0)){
                HashMap eachRecs = new HashMap();
                Set keySet =  sanctionFacilityAll.keySet();
                Object[] objKeySet = (Object[]) keySet.toArray();
                ArrayList sanctionFacilityTableValue = (ArrayList)tblSanctionFacility.getDataArrayList().get(slno);
                // To populate the corresponding Sanction Facility Details
                for (int i = sanctionFacilityAll.size() - 1,j = 0;i >= 0;--i,++j){
                    // If the record matches with the key then populate it in UI
                    if ((CommonUtil.convertObjToStr(((HashMap) sanctionFacilityAll.get(objKeySet[j])).get(SLNO))).equals(CommonUtil.convertObjToStr(sanctionFacilityTableValue.get(0)))){
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
        }catch(Exception e){
            log.info("Exception caught in populateSanctionTabProdID: "+e);
            parseException.logException(e,true);
        }
    }
    
    private int updateSanctionFacility(int row, boolean updateMain, int mainSlNo){
        log.info("In updateSanctionFacility...");
        HashMap result = new HashMap();
        int option = -1;
        int count = 0;
        HashMap sanctionMainRec = new HashMap();
        String strSanctionNo;
        try{
            strSanctionNo = CommonUtil.convertObjToStr(((HashMap) sanctionFacilityAll.get(((ArrayList) sanctionFacilityAllTabRecords.get(row)).get(0))).get(SANCTION_NO));
            sanctionFacilityRecord.put(SANCTION_NO, strSanctionNo);
            result = tableUtilSanction_Facility.updateTableValues(sanctionFacilityTabRow, sanctionFacilityRecord, row);
            sanctionFacilityAllTabRecords = (ArrayList) result.get(TABLE_VALUES);
            sanctionFacilityAll = (LinkedHashMap)result.get(ALL_VALUES);
            option = CommonUtil.convertObjToInt(result.get(OPTION));
            // To save the changes in Sanction Main at the time of Sanction main edit
            if (updateMain == true){
                sanctionMainRec = (HashMap) sanctionMainAll.get(((ArrayList) sanctionMainAllTabRecords.get(mainSlNo)).get(0));
                sanctionMainRec.put(SANCTION_FACILITY_ALL, sanctionFacilityAll);
                sanctionMainRec.put(SANCTION_FACILITY_TABLE, sanctionFacilityAllTabRecords);
                sanctionMainAll.put(((ArrayList) sanctionMainAllTabRecords.get(mainSlNo)).get(0), sanctionMainRec);
            }
            tblSanctionFacility.setDataArrayList(sanctionFacilityAllTabRecords, sanctionFacilityTitle);
        }catch(Exception e){
            log.info("Exception caught in updateSanctionFacility: "+e);
            parseException.logException(e,true);
        }
        strSanctionNo = null;
        result = null;
        sanctionMainRec = null;
        return option;
    }
    
    public void setProdIDAsBlank(){
        setBlankKeyValue();
        cbmProductId = new ComboBoxModel(key,value);
    }
    
    private void setFacilityProductID(String strFacilityType){
        try{
            cbmProductId = null;
            HashMap transactionMap = new HashMap();
            HashMap retrieve = new HashMap();
            
            ArrayList keyList = new ArrayList();
            keyList.add("");
            
            ArrayList valList = new ArrayList();
            valList.add("");
            
            if (strFacilityType == null){
                transactionMap.put("behavesLike", CommonUtil.convertObjToStr(cbmTypeOfFacility.getKeyForSelected()));
            }else{
                transactionMap.put("behavesLike", strFacilityType);
            }
            transactionMap.put("AUTHORIZE_REMARK", "GOLD_LOAN");
            List resultList = ClientUtil.executeQuery("TermLoan.getProdID_Behaves", transactionMap);
            
            for (int i = resultList.size() - 1, j = 0;i >= 0;--i,++j){
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
        }catch(Exception e){
            log.info("Exception caught in setFacilityProductID: "+e);
            parseException.logException(e,true);
        }
    }
    
    public void setFacilityProductID(){
        setFacilityProductID(null);
    }
    
    public int deleteSanctionFacility(int sanctionTabFacilityRow, boolean updateMain, int mainSlNo, int facilityTabSanctionNo, int facilityTabSlNo){
        log.info("In deleteSanctionFacility...");
        int option = -1;
        try{
            HashMap result = new HashMap();
            HashMap sanctionMainRec = new HashMap();
            
            ArrayList data = tblSanctionFacility.getDataArrayList();
            
            int keysanctionTabFacilityRow = CommonUtil.convertObjToInt(((ArrayList) data.get(sanctionTabFacilityRow)).get(0));
            
            data = tblSanctionMain.getDataArrayList();
            String strSanctionNo = CommonUtil.convertObjToStr(((ArrayList) data.get(mainSlNo)).get(1));
            mainSlNo = CommonUtil.convertObjToInt(((ArrayList) data.get(mainSlNo)).get(0));
            
            data = tblSanctionMain.getDataArrayList();
            if (facilityTabSanctionNo > -1){
                facilityTabSanctionNo = CommonUtil.convertObjToInt(((ArrayList) data.get(facilityTabSanctionNo)).get(1));
            }
            
            data = tblSanctionFacility.getDataArrayList();
            if (facilityTabSlNo > -1){
                facilityTabSlNo = CommonUtil.convertObjToInt(((ArrayList) data.get(facilityTabSlNo)).get(0));
            }
            
            option = -1;
            
            if (facilityAll.containsKey(getFacilityKey(strSanctionNo, keysanctionTabFacilityRow))){
                //                String[] options = {objTermLoanRB.getString("cDialogOk")};
                //                option = COptionPane.showOptionDialog(null, objTermLoanRB.getString("existanceFacilityWarning"), CommonConstants.WARNINGTITLE,
                //                COptionPane.OK_OPTION, COptionPane.WARNING_MESSAGE, null, options, options[0]); commented by abi a for  delete not transaction rec
                //                List lt=(List)hash;
                //                ////System.out.print("hash###"+lt);
                //                if(lblAcctNo_Sanction_Disp)
                HashMap hash2=new HashMap();
                hash2.put("ACT_NUM", getLoanACNo());
                List lst=ClientUtil.executeQuery("checkTransaction", hash2);
                hash2=(HashMap)lst.get(0);
                int count=0;
                count=CommonUtil.convertObjToInt(hash2.get("CNT"));
                if( count !=0) {
                    ClientUtil.displayAlert("This Account already Disbursed");
                }else{
                    int val=ClientUtil.confirmationAlert("are u sure want to delete LoanAccount\n"+ getLoanACNo());
                    if(val==0){
                        option = 0;
                        result = tableUtilSanction_Facility.deleteTableValues(sanctionTabFacilityRow);
                        sanctionFacilityAllTabRecords = (ArrayList) result.get(TABLE_VALUES);
                        Object  hash=(Object)sanctionFacilityAllTabRecords.get(0);
                        ////System.out.print("hash###"+hash);
                    }
                }
            }else{
                result = tableUtilSanction_Facility.deleteTableValues(sanctionTabFacilityRow);
                sanctionFacilityAllTabRecords = (ArrayList) result.get(TABLE_VALUES);
                sanctionFacilityAll  = (LinkedHashMap)result.get(ALL_VALUES);
                
                // To save the changes in Sanction Main at the time of Sanction main edit
                if (updateMain == true){
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
        }catch(Exception e){
            log.info("Exception caught in deleteSanctionFacility..."+e);
            parseException.logException(e,true);
        }
        return option;
    }
    
    public void setSanctionTableWarningMessage(){
        int option = -1;
        String[] options = {objTermLoanRB.getString("cDialogOk")};
        option = COptionPane.showOptionDialog(null, objTermLoanRB.getString("existanceSanctionDetailsWarning"), CommonConstants.WARNINGTITLE,
        COptionPane.OK_OPTION, COptionPane.WARNING_MESSAGE, null, options, options[0]);
    }
    
    public void setSanctionNumber(){
        try{
            HashMap eachRec;
            Set keySet =  sanctionFacilityAll.keySet();
            Object[] objKeySet = (Object[]) keySet.toArray();
            // To set the Sanction Number for the Inserted Records
            // It will be identified by the value NIL
            for (int i = keySet.size() - 1;i >= 0;--i){
                eachRec = (HashMap) sanctionFacilityAll.get(objKeySet[i]);
                eachRec.put(SANCTION_NO, getTxtSanctionNo());
                sanctionFacilityAll.put(objKeySet[i], eachRec);
                eachRec = null;
            }
            keySet = null;
            objKeySet = null;
        }catch(Exception e){
            log.info("Exception caught in setSanctionNumber()..."+e);
            parseException.logException(e,true);
        }
    }
    
    public int addSanctionMainTab(int row, boolean update){
        int option = -1;
        try{
            HashMap result = new HashMap();
            ArrayList data = tblSanctionMain.getDataArrayList();
            tblSanctionMain.setDataArrayList(data, sanctionMainTitle);
            final int dataSize = data.size();
            insertSanctionMain(dataSize+1, sanctionFacilityAllTabRecords, sanctionFacilityAll);
            if (!update){
                // If the table is not in Edit Mode
                result = tableUtilSanction_Main.insertTableValues(sanctionMainTabRow, sanctionMainRecord, SANCTION_SL_NO);
                
                sanctionMainAllTabRecords = (ArrayList) result.get(TABLE_VALUES);
                sanctionMainAll = (LinkedHashMap)result.get(ALL_VALUES);
                option = CommonUtil.convertObjToInt(result.get(OPTION));
                
                tblSanctionMain.setDataArrayList(sanctionMainAllTabRecords, sanctionMainTitle);
                option = dataSize;
            }else{
                option = updateSanctionMain(row);
                option = row;
            }
//            tblSanctionFacility.setDataArrayList(null, sanctionFacilityTitle);
            setChanged();
            result = null;
            data = null;
        }catch(Exception e){
            log.info("Exception caught in addSanctionMainTab..."+e);
            parseException.logException(e,true);
        }
        return option;
    }
    
    
    private void insertSanctionMain(int slno, ArrayList tableValues, LinkedHashMap allValues){
        try{
            sanctionMainTabRow.add(String.valueOf(slno));
            sanctionMainTabRow.add(getTxtSanctionNo());
            sanctionMainTabRow.add(getCboSanctioningAuthority());
            sanctionMainTabRow.add(getCboModeSanction());
            
            sanctionMainRecord.put(SANCTION_NO,  getTxtSanctionNo());
            sanctionMainRecord.put(SANCTION_SL_NO,  String.valueOf(slno));
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
        }catch(Exception e){
            log.info("Exception caught in insertSanctionMain..."+e);
            parseException.logException(e,true);
        }
    }
    
    public void populateSanctionMain(int slno){
        try{
            HashMap eachRecs = new HashMap();
            Set keySet =  sanctionMainAll.keySet();
            Object[] objKeySet = (Object[]) keySet.toArray();
            ArrayList sanctionMainTableValue = (ArrayList)tblSanctionMain.getDataArrayList().get(slno);
            
            // To populate the corresponding record from the sanction details
            for (int i = sanctionMainAll.size() - 1,j = 0;i >= 0;--i,++j){
                if ((CommonUtil.convertObjToStr(((HashMap) sanctionMainAll.get(objKeySet[j])).get(SANCTION_SL_NO))).equals(CommonUtil.convertObjToStr(sanctionMainTableValue.get(0)))){
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
                    oldSanction_no=CommonUtil.convertObjToStr(eachRecs.get(SANCTION_NO));
                    // To populate the RemovedValues(ArrayList) in Sanction_Facility TableUtil Object
                    // for the first time only after retrieving from the Database
                    if (eachRecs.get(FLAG).equals(TRUE)){
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
        }catch(Exception e){
            log.info("Exception caught in populateSanctionMain..."+e);
            parseException.logException(e,true);
        }
    }
    
    public void createSanctionMainRowObjects(){
        sanctionMainTabRow = new ArrayList();
        sanctionMainRecord = new HashMap();
    }
    
    private int updateSanctionMain(int row){
        HashMap result = new HashMap();
        int option = -1;
        Object tempVal = null;
        try{
            result = tableUtilSanction_Main.updateTableValues(sanctionMainTabRow, sanctionMainRecord, row);
            sanctionMainAllTabRecords = (ArrayList) result.get(TABLE_VALUES);
            sanctionMainAll = (LinkedHashMap)result.get(ALL_VALUES);
            option = CommonUtil.convertObjToInt(result.get(OPTION));
            
            tblSanctionMain.setDataArrayList(sanctionMainAllTabRecords, sanctionMainTitle);
        }catch(Exception e){
            log.info("Exception caught in updateSanctionMain()..."+e);
            parseException.logException(e,true);
        }
        result = null;
        return option;
    }
    
    public void deleteSanctionMain(int row){
        try{
            HashMap result = new HashMap();
            result = tableUtilSanction_Main.deleteTableValues(row);
            sanctionMainAll = (LinkedHashMap)result.get(ALL_VALUES);
            // To rearrange the Sanction Number in Sanction Facility Details Table
            // rearrangeSanctionNo();
            sanctionMainAllTabRecords = (ArrayList) result.get(TABLE_VALUES);
            tblSanctionMain.setDataArrayList(sanctionMainAllTabRecords, sanctionMainTitle);
            tblSanctionFacility.setDataArrayList(null, sanctionFacilityTitle);
            result = null;
        }catch(Exception e){
            log.info("Exception caught in deleteSanctionMain()..."+e);
            parseException.logException(e,true);
        }
    }
    
    public void populateFacilityTabSanction(int sanctionNumberFacility/*, int sanctionTabNumber*/){
        try{
            HashMap eachRecs;
            facilityTabSanction = new ArrayList();
            Set keySet =  sanctionMainAll.keySet();
            Object[] objKeySet = (Object[]) keySet.toArray();
            String strRecordKey = CommonUtil.convertObjToStr(((ArrayList) (tblSanctionMain.getDataArrayList().get(sanctionNumberFacility))).get(0));
            // To populate the corresponding record from Facility Details
            for (int i = sanctionMainAll.size() - 1,j = 0;i >= 0;--i,++j){
                if ((CommonUtil.convertObjToStr(((HashMap) sanctionMainAll.get(objKeySet[j])).get(SANCTION_SL_NO))).equals(String.valueOf(strRecordKey))){
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
        }catch(Exception e){
            log.info("Exception caught in populateFacilityTabSanction: "+e);
            parseException.logException(e,true);
        }
    }
    
    
    public void addFacilityDetails(int sanctionSlNo, int slno){
        try{
            facilityRecord = new HashMap();
            String strSanctionNo = CommonUtil.convertObjToStr(((ArrayList)tblSanctionMain.getDataArrayList().get(sanctionSlNo)).get(0));
//            sanctionSlNo   = CommonUtil.convertObjToInt(((ArrayList)tblSanctionMain.getDataArrayList().get(sanctionSlNo)).get(0));
            slno           = CommonUtil.convertObjToInt(((ArrayList)tblSanctionFacility.getDataArrayList().get(slno)).get(0));
            String strFacilityKey = getFacilityKey(strSanctionNo, slno);
            insertFacilityDetails(strSanctionNo, slno);
            if (facilityAll.containsKey(strFacilityKey) && getStrACNumber().length()>0){
                // If the key already exist in the Linked Hash Map then it status
                // will be changed to UPDATE
                if(getBEHAVES_LIKE().equals("OD"))
                    facilityRecord.put(AVAILABLE_BALANCE, getRenewAvailableBalance());
                else {
                    if (isUpdateAvailableBalance())
                        facilityRecord.put(AVAILABLE_BALANCE, getTxtLimit_SD());
                    else {
                        HashMap facilityRec = (HashMap) facilityAll.get(strFacilityKey);
                        facilityRecord.put(AVAILABLE_BALANCE, facilityRec.get(AVAILABLE_BALANCE));
                    }
                }
                
                facilityRecord.put(COMMAND, UPDATE);
                
            }else{
                // At the time of creating new account set the available balance, total available balance
                // shadow debit, shadow credit, clear balance, unclear balance
                facilityRecord.put(SHADOW_DEBIT, "0");
                facilityRecord.put(SHADOW_CREDIT, "0");
                facilityRecord.put(AVAILABLE_BALANCE,  getTxtLimit_SD());
                facilityRecord.put(TOTAL_AVAILABLE_BALANCE, "0");
                facilityRecord.put(CLEAR_BALANCE, "0");
                facilityRecord.put(UNCLEAR_BALANCE, "0");
            }
            if (getActionType() != 1){
                // If the status is not New
                setActionType(ClientConstants.ACTIONTYPE_EDIT);
            }
            facilityAll.put(strFacilityKey, facilityRecord);
            strSanctionNo = null;
            setBEHAVES_LIKE("");
        }catch(Exception e){
            log.info("Exception caught in addFacilityDetails: "+e);
            parseException.logException(e,true);
        }
    }
    private HashMap insertKccRenewalDetail(){
        HashMap renewalMap = new HashMap();
        try{
            KccRenewalTO KccRenewalTO = new KccRenewalTO();
            KccRenewalTO.setActNum(getStrACNumber());
            KccRenewalTO.setFromDt(getProperDateFormat(tdtFDate));
            KccRenewalTO.setToDt(getProperDateFormat(tdtTDate));
            KccRenewalTO.setStatus("CREATED");
            KccRenewalTO.setBranchID(TrueTransactMain.BRANCH_ID);
            KccRenewalTO.setStatusDt(curDate);
            renewalMap.put("KCC_RENEWAL", KccRenewalTO);
            }catch(Exception e){
                e.printStackTrace();
                log.info("Exception caught In insertKccRenewalDetail: "+e);
                parseException.logException(e,true);
            }
        return renewalMap;
        
    }
    
    private void insertFacilityDetails(String strSanctionNo, int slno){
        facilityRecord=new HashMap();
        facilityRecord.put(PROD_ID, CommonUtil.convertObjToStr(getLblProductID_FD_Disp()));
        facilityRecord.put(ACCT_STATUS, CommonUtil.convertObjToStr(cbmAccStatus.getKeyForSelected()));
        facilityRecord.put(SANCTION_NO,  strSanctionNo);
        facilityRecord.put(SLNO, String.valueOf(slno));
        facilityRecord.put(INTEREST_TYPE, CommonUtil.convertObjToStr(cbmInterestType.getKeyForSelected()));
        facilityRecord.put(NOTE_DATE, getTdtDemandPromNoteDate());
        facilityRecord.put(NOTE_EXP_DATE, getTdtDemandPromNoteExpDate());
        facilityRecord.put(AOD_DATE, getTdtAODDate());
        facilityRecord.put(PURPOSE_DESC, getTxtPurposeDesc());
        facilityRecord.put(GROUP_DESC, getTxtGroupDesc());
        facilityRecord.put(CONTACT_PERSON, getTxtContactPerson());
        facilityRecord.put(DEALER_ID, getTxtDealerID());
        facilityRecord.put(SALARY_RECOVERY, getRdoSalaryRecovery());
        facilityRecord.put(LOCK_STATUS, getLockStatus());
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
        //facilityRecord.put(RECOMMANDED_BY, getCboRecommendedByType());
        facilityRecord.put(RECOMMANDED_BY, CommonUtil.convertObjToStr(cbmRecommendedByType.getKeyForSelected()));
        facilityRecord.put(RECOMMANDED_BY2, CommonUtil.convertObjToStr(cbmRecommendedByType2.getKeyForSelected()));
        facilityRecord.put(KOLE_LAND_AREA, getTxtKoleLandArea());
//        txtSubsidyAmt
        facilityRecord.put(SUBSIDY_AMT, CommonUtil.convertObjToDouble(getTxtSubsidyAmt()));
        facilityRecord.put(SUBSIDY_DT, CommonUtil.convertObjToStr(getTdtSubsidyAppDt()));
        facilityRecord.put(SUBSIDY_ACHEAD, getTxtSubsidyAccHead());
        facilityRecord.put(SUBSIDY_ADJUSTED_AMT, getTxtSubsidyAdjustedAmt());
        
        facilityRecord.put(REBATE_AMT, getTxtRebateInterest_Amt());
        facilityRecord.put(REBATE_DT, getTdtRebateInterest_App_Dt());
        if(isRdoRebateInterest_Yes())
            facilityRecord.put(REBATE_ALLOWED, "Y");
        else if(isRdoRebateInterest_No())
            facilityRecord.put(REBATE_ALLOWED, "N");
        else
            facilityRecord.put(REBATE_ALLOWED, "");
        
        
        
        facilityRecord.put(ACCT_NUM, getStrACNumber());
        
        if (getRdoSecurityDetails_Unsec() == true){
            facilityRecord.put(SECURITY_DETAILS, UNSECURED);
        }else if (getRdoSecurityDetails_Partly() == true){
            facilityRecord.put(SECURITY_DETAILS, PARTLY_SECURED);
        }else if (getRdoSecurityDetails_Fully() == true){
            facilityRecord.put(SECURITY_DETAILS, FULLY_SECURED);
        }else{
            facilityRecord.put(SECURITY_DETAILS, "");
        }
        if(isRdoDP_YES())
            facilityRecord.put(DRAWING_POWER, "Y");
        else if(isRdoDP__NO())
            facilityRecord.put(DRAWING_POWER, "N");
        else
            facilityRecord.put(DRAWING_POWER, " ");
        if ((getChkStockInspect() == true) && (getChkInsurance() == true) && (getChkGurantor() == true)){
            facilityRecord.put(SECURITY_TYPE, INSPECT_INSURANCE_GUARANTAR);
        }else if ((getChkStockInspect() == true) && (getChkInsurance() == true)){
            facilityRecord.put(SECURITY_TYPE, STOCK_INSPECT_INSURANCE);
        }else if ((getChkStockInspect() == true) && (getChkGurantor() == true)){
            facilityRecord.put(SECURITY_TYPE, STOCK_INSPECT_GUARANTAR);
        }else if ((getChkInsurance() == true) && (getChkGurantor() == true)){
            facilityRecord.put(SECURITY_TYPE, INSURANCE_GUARANTAR);
        }else if (getChkStockInspect() == true){
            facilityRecord.put(SECURITY_TYPE, STOCK_INSPECT);
        }else if (getChkInsurance() == true){
            facilityRecord.put(SECURITY_TYPE, INSURANCE);
        }else if (getChkGurantor() == true){
            facilityRecord.put(SECURITY_TYPE, GUARANTAR);
        }else{
            facilityRecord.put(SECURITY_TYPE, "");
        }
        if(getChkAcctTransfer()==true)
             facilityRecord.put(ACCTTRANSFER, ACCTTRANSFER);
        else
            facilityRecord.put(ACCTTRANSFER, "");
            
        if (getRdoAccType_New() == true){
            facilityRecord.put(ACC_TYPE, NEW);
        }else if (getRdoAccType_Transfered() == true){
            facilityRecord.put(ACC_TYPE, TRANSFERED);
        }else{
            facilityRecord.put(ACC_TYPE, "");
        }
        
        if (getRdoAccLimit_Main() == true){
            facilityRecord.put(ACC_LIMIT, MAIN);
        }else if (getRdoAccLimit_Submit() == true){
            facilityRecord.put(ACC_LIMIT, SUBMIT);
        }else{
            facilityRecord.put(ACC_LIMIT, "");
        }
        
        if (getRdoRiskWeight_Yes() == true){
            facilityRecord.put(RISK_WEIGHT, YES);
        }else if (getRdoRiskWeight_No() == true){
            facilityRecord.put(RISK_WEIGHT, NO);
        }else{
            facilityRecord.put(RISK_WEIGHT, " ");
        }
        
        if (getRdoMultiDisburseAllow_Yes() == true){
            facilityRecord.put(MULTI_DISBURSE, YES);
        }else if (getRdoMultiDisburseAllow_No() == true){
            facilityRecord.put(MULTI_DISBURSE, NO);
        }else{
            facilityRecord.put(MULTI_DISBURSE, " ");
        }
        
        if (getRdoInterest_Simple() == true){
            facilityRecord.put(INTEREST, SIMPLE);
        }else if (getRdoInterest_Compound() == true){
            facilityRecord.put(INTEREST, COMPOUND);
        }else{
            facilityRecord.put(INTEREST, " ");
        }
        if(isChkAuthorizedSignatory())
            facilityRecord.put(AUTHSIGNATORY,AUTHSIGNATORY);
        else
            facilityRecord.put(AUTHSIGNATORY," ");
        
         if(isChkPOFAttorney())
            facilityRecord.put(POFATTORNEY,POFATTORNEY);
        else
            facilityRecord.put(POFATTORNEY," ");
        
           if(isChkDocDetails())
            facilityRecord.put(DOCDETAILS,DOCDETAILS);
        else
            facilityRecord.put(DOCDETAILS," ");
        
        if(isChkOTS())
            facilityRecord.put("OTS",YES);
        else
            facilityRecord.put("OTS",NO);
        
        
           if(rdoSubsidy_Yes==true)
            facilityRecord.put(SUBSIDY,"Y");
        else if(rdoSubsidy_No==true)
            facilityRecord.put(SUBSIDY,"N");
        else
            facilityRecord.put(SUBSIDY," ");
        
           if(rdoSubsidy_Yes==true)
            facilityRecord.put(SUBSIDY,"Y");
        else if(rdoSubsidy_No==true)
            facilityRecord.put(SUBSIDY,"N");
        else
            facilityRecord.put(SUBSIDY," ");
        
        
         if (getStrACNumber().length()>0){
                // If the key already exist in the Linked Hash Map then it status
                // will be changed to UPDATE
                if(getBEHAVES_LIKE().equals("OD"))
                    facilityRecord.put(AVAILABLE_BALANCE, getRenewAvailableBalance());
                else {
                    if(getClearBalance()<0)
                        facilityRecord.put(AVAILABLE_BALANCE, String.valueOf(getAvailableBalance()));
                    else if (isUpdateAvailableBalance())
                        facilityRecord.put(AVAILABLE_BALANCE, getTxtLimit_SD());
                   
                }
                
            }else{
                // At the time of creating new account set the available balance, total available balance
                // shadow debit, shadow credit, clear balance, unclear balance
                facilityRecord.put(SHADOW_DEBIT, "0");
                facilityRecord.put(SHADOW_CREDIT, "0");
                facilityRecord.put(AVAILABLE_BALANCE,  getTxtLimit_SD());
                facilityRecord.put(TOTAL_AVAILABLE_BALANCE, "0");
                facilityRecord.put(CLEAR_BALANCE, "0");
                facilityRecord.put(UNCLEAR_BALANCE, "0");
            }
    }
    
    private String getFacilityKey(String sanctionNo, int slno){
        String strFacilityKey = "";
        StringBuffer strbufKey = new StringBuffer();
        try{
            strbufKey.append(sanctionNo);
            strbufKey.append("#");
            strbufKey.append(String.valueOf(slno));
            strFacilityKey = new String(strbufKey);
        }catch(Exception e){
            log.info("Exception caught in getFacilityKey: "+e);
            parseException.logException(e,true);
        }
        strbufKey = null;
        return strFacilityKey;
    }
    
    public void populateFacilityDetails(int sanctionSlNo, int slno){
        try{
            ArrayList data = tblSanctionMain.getDataArrayList();
            String strSanctionNo = CommonUtil.convertObjToStr(((ArrayList) data.get(sanctionSlNo)).get(1));
            sanctionSlNo   = CommonUtil.convertObjToInt(((ArrayList) data.get(sanctionSlNo)).get(0));
            
            //            termLoanSecurityOB.setLimitAmount(CommonUtil.convertObjToDouble(((ArrayList)tblSanctionFacility.getDataArrayList().get(slno)).get(2)).doubleValue());
            termLoanInterestOB.setLblLimitAmt_2(CommonUtil.convertObjToStr(((ArrayList)tblSanctionFacility.getDataArrayList().get(slno)).get(2)));
            termLoanInterestOB.setLblExpiryDate_2(CommonUtil.convertObjToStr(((ArrayList)tblSanctionFacility.getDataArrayList().get(slno)).get(4)));
            slno       = CommonUtil.convertObjToInt(((ArrayList)tblSanctionFacility.getDataArrayList().get(slno)).get(0));
            
            // To set the sanction no. in Classification details
            termLoanClassificationOB.setLblSanctionNo2(strSanctionNo);
            String strFacilityKey = getFacilityKey(String.valueOf(sanctionSlNo), slno);
            termLoanClassificationOB.setLblSanctionDate2(CommonUtil.convertObjToStr(((HashMap) sanctionMainAll.get(String.valueOf(sanctionSlNo))).get(SANCTION_DATE)));
            termLoanInterestOB.setLblSancDate_2(termLoanClassificationOB.getLblSanctionDate2());
            getPLR_Rate(CommonUtil.convertObjToStr(getCbmProductId().getKeyForSelected()));
            resetFacilityDetails();
            if (facilityAll.containsKey(strFacilityKey)){
                // To populate the Corresponding record from CTable
                HashMap oneFacilityRec = (HashMap) facilityAll.get(strFacilityKey);
                
                setTxtContactPerson(CommonUtil.convertObjToStr(oneFacilityRec.get(CONTACT_PERSON)));
                setTxtDealerID(CommonUtil.convertObjToStr(oneFacilityRec.get(DEALER_ID)));
                setRdoSalaryRecovery(CommonUtil.convertObjToStr(oneFacilityRec.get(SALARY_RECOVERY)));
                setLockStatus(CommonUtil.convertObjToStr(oneFacilityRec.get(LOCK_STATUS)));
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
                setCboRecommendedByType2(CommonUtil.convertObjToStr(getCbmRecommendedByType().getDataForKey(CommonUtil.convertObjToStr(oneFacilityRec.get(RECOMMANDED_BY2)))));
                setTxtKoleLandArea(CommonUtil.convertObjToStr(oneFacilityRec.get(KOLE_LAND_AREA)));
                setLast_int_calc_dt(getProperDateFormat(oneFacilityRec.get("LAST_INT_CALC_DT")));
                if (oneFacilityRec.get(SECURITY_DETAILS).equals(UNSECURED)){
                    setRdoSecurityDetails_Unsec(true);
                }else if (oneFacilityRec.get(SECURITY_DETAILS).equals(PARTLY_SECURED)){
                    setRdoSecurityDetails_Partly(true);
                }else if (oneFacilityRec.get(SECURITY_DETAILS).equals(FULLY_SECURED)){
                    setRdoSecurityDetails_Fully(true);
                }
                
                if (oneFacilityRec.get(SECURITY_TYPE).equals(INSPECT_INSURANCE_GUARANTAR)){
                    setChkStockInspect(true);
                    setChkInsurance(true);
                    setChkGurantor(true);
                }else if (oneFacilityRec.get(SECURITY_TYPE).equals(STOCK_INSPECT_INSURANCE)){
                    setChkStockInspect(true);
                    setChkInsurance(true);
                }else if (oneFacilityRec.get(SECURITY_TYPE).equals(STOCK_INSPECT_GUARANTAR)){
                    setChkStockInspect(true);
                    setChkGurantor(true);
                }else if (oneFacilityRec.get(SECURITY_TYPE).equals(INSURANCE_GUARANTAR)){
                    setChkInsurance(true);
                    setChkGurantor(true);
                }else if (oneFacilityRec.get(SECURITY_TYPE).equals(STOCK_INSPECT)){
                    setChkStockInspect(true);
                }else if (oneFacilityRec.get(SECURITY_TYPE).equals(INSURANCE)){
                    setChkInsurance(true);
                }else if (oneFacilityRec.get(SECURITY_TYPE).equals(GUARANTAR)){
                    setChkGurantor(true);
                }
                if (oneFacilityRec.get(ACCTTRANSFER).equals(ACCTTRANSFER)){
                    setChkAcctTransfer(true);
                }
                if (oneFacilityRec.get(ACC_TYPE).equals(NEW)){
                    setRdoAccType_New(true);
                }else if (oneFacilityRec.get(ACC_TYPE).equals(TRANSFERED)){
                    setRdoAccType_Transfered(true);
                }
                
                if (oneFacilityRec.get(ACC_LIMIT).equals(MAIN)){
                    setRdoAccLimit_Main(true);
                }else if (oneFacilityRec.get(ACC_LIMIT).equals(SUBMIT)){
                    setRdoAccLimit_Submit(true);
                }
                
                if (oneFacilityRec.get(RISK_WEIGHT).equals(YES)){
                    setRdoRiskWeight_Yes(true);
                }else if (oneFacilityRec.get(RISK_WEIGHT).equals(NO)){
                    setRdoRiskWeight_No(true);
                }
                
                //                if (oneFacilityRec.get(INTEREST_NATURE).equals(PLR)){
                //                    setRdoNatureInterest_PLR(true);
                //                }else if (oneFacilityRec.get(INTEREST_NATURE).equals(NON_PLR)){
                //                    setRdoNatureInterest_NonPLR(true);
                //                }
                
                if (oneFacilityRec.get(MULTI_DISBURSE).equals(YES)){
                    setRdoMultiDisburseAllow_Yes(true);
                }else if (oneFacilityRec.get(MULTI_DISBURSE).equals(NO)){
                    setRdoMultiDisburseAllow_No(true);
                }
                
                if (oneFacilityRec.get(INTEREST).equals(SIMPLE)){
                    setRdoInterest_Simple(true);
                }else if (oneFacilityRec.get(INTEREST).equals(COMPOUND)){
                    setRdoInterest_Compound(true);
                }
                 if (oneFacilityRec.get(DRAWING_POWER) !=null && oneFacilityRec.get(DRAWING_POWER).equals(YES)){
                     setRdoDP_YES(true);
                 }
                 else if (oneFacilityRec.get(DRAWING_POWER) !=null && oneFacilityRec.get(DRAWING_POWER).equals(NO)){
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
            }else{
                resetBasedOnAcctNumber();
                if(ProductFacilityType.length()>0 && ProductFacilityType.equals("SI_BEARING")) {
                    setRdoInterest_Simple(true);
                    setRdoInterest_Compound(false);
                }else{
                    setRdoInterest_Simple(false);
                    setRdoInterest_Compound(true);
                }
//                setAccountOpenDate(CommonUtil.convertObjToStr(DateUtil.getDateWithoutMinitues(curDate)));
                setAccountOpenDate(CommonUtil.convertObjToStr(curDate));
                if (loanType.equals("LTD"))
                    setRdoMultiDisburseAllow_No(true);
            }
            if (loanType.equals("LTD")) {
                setRdoSecurityDetails_Fully(true);
                setCboInterestType(CommonUtil.convertObjToStr(getCbmInterestType().getDataForKey(FIXED_RATE)));
                //                setRdoMultiDisburseAllow_No(true);
                if (depositCustDetMap!=null) {
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
        }catch(Exception e){
            log.info("Exception caught in populateFacilityDetails: "+e);
            parseException.logException(e,true);
        }
    }
    
    public void setLTDSecurityDetails(){
         if (loanType.equals("LTD")) {
                setRdoSecurityDetails_Fully(true);
                 setRdoSecurityDetails_Unsec(false);
             
                setCboInterestType(CommonUtil.convertObjToStr(getCbmInterestType().getDataForKey(FIXED_RATE)));
                
                //                setRdoMultiDisburseAllow_No(true);
                if (depositCustDetMap!=null) {
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


  if (loanType.equals("LTD"))
                    setRdoMultiDisburseAllow_No(true);
    }
    
    public void setFacilityProdID(int slno){
        try{
            HashMap eachRecs = new HashMap();
            Set keySet =  sanctionFacilityAll.keySet();
            Object[] objKeySet = (Object[]) keySet.toArray();
            ArrayList sanctionFacilityTableValue = (ArrayList)tblSanctionFacility.getDataArrayList().get(slno);
            // To populate the corresponding Sanction Facility Details
            for (int i = sanctionFacilityAll.size() - 1,j = 0;i >= 0;--i,++j){
                // If the record matches with the key then populate it in UI
                if ((CommonUtil.convertObjToStr(((HashMap) sanctionFacilityAll.get(objKeySet[j])).get(SLNO))).equals(CommonUtil.convertObjToStr(sanctionFacilityTableValue.get(0)))){
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
        }catch(Exception e){
            log.info("Exception caught in setFacilityProdID: "+e);
            parseException.logException(e,true);
        }
    }
    
    private void populateRestofProdId_AccHead(){
        try{
            HashMap idTransactionMap = new HashMap();
            HashMap idRetrieve;
            idTransactionMap.put("prodId", getLblProductID_FD_Disp());
            List idResultList = ClientUtil.executeQuery("TermLoan.getProdHead", idTransactionMap);
            if (idResultList.size() > 0){
                // If Product Account Head exist in Database
                idRetrieve = (HashMap) idResultList.get(0);
                setLblAccountHead_FD_Disp(CommonUtil.convertObjToStr(idRetrieve.get("AC_HEAD")));
            }
            idRetrieve = null;
            idTransactionMap = null;
            idResultList = null;
            updateProdID_AccHead();
        }catch(Exception e){
            log.info("Exception caught in populateRestofProdId_AccHead: "+e);
            parseException.logException(e,true);
        }
    }
    
    public HashMap getCompFreqRoundOffValues(){
        HashMap transactionMap = new HashMap();
        HashMap retrieve = new HashMap();
        try{
            transactionMap.put("PROD_ID", getLblProductID_FD_Disp());
            List resultList = ClientUtil.executeQuery("getCompFreqRoundOff_LoanProd", transactionMap);
            if (resultList.size() > 0){
                // If Product Account Head exist in Database
                retrieve = (HashMap) resultList.get(0);
            }
            transactionMap = null;
            resultList = null;
        }catch(Exception e){
            log.info("Exception caught in getCompFreqRoundOffValues: "+e);
            parseException.logException(e,true);
        }
        return retrieve;
    }
    
    private void populateAccountNumber(String strSanctionNo, int slno){
        try{
            HashMap transactionMap = new HashMap();
            HashMap retrieve;
            transactionMap.put("borrowNo", getBorrowerNo());
            transactionMap.put("sanctionNo", strSanctionNo);
            transactionMap.put("slNo", String.valueOf(slno));
            List resultList = (List) ClientUtil.executeQuery("getAccountNumber", transactionMap);
            if (resultList.size() > 0){
                // If the Account Number exist in Database
                retrieve = (HashMap) resultList.get(0);
                cbmIntGetFrom.setKeyForSelected(retrieve.get(INT_GET_FROM));
                setCboIntGetFrom(CommonUtil.convertObjToStr(getCbmIntGetFrom().getDataForKey(CommonUtil.convertObjToStr(retrieve.get(INT_GET_FROM)))));
                termLoanGuarantorOB.setLblAccNo_GD_2(CommonUtil.convertObjToStr(retrieve.get(ACCT_NUM)));
                termLoanOtherDetailsOB.setStrACNumber(CommonUtil.convertObjToStr(retrieve.get(ACCT_NUM)));
                termLoanClassificationOB.setLblAccNo_CD_2(CommonUtil.convertObjToStr(retrieve.get(ACCT_NUM)));
                setLblAccNo_Idetail_2(CommonUtil.convertObjToStr(retrieve.get(ACCT_NUM)));
                termLoanInterestOB.setLblAccNo_IM_2(CommonUtil.convertObjToStr(retrieve.get(ACCT_NUM)));
                termLoanRepaymentOB.setLblAccNo_RS_2(CommonUtil.convertObjToStr(retrieve.get(ACCT_NUM)));
                termLoanSecurityOB.setLblAccNoSec_2(CommonUtil.convertObjToStr(retrieve.get(ACCT_NUM)));
                termLoanOtherDetailsOB.setLblAcctNo_Disp_ODetails(CommonUtil.convertObjToStr(retrieve.get(ACCT_NUM)));
                setStrACNumber(CommonUtil.convertObjToStr(retrieve.get(ACCT_NUM)));
                termLoanDocumentDetailsOB.setLblAcctNo_Disp_DocumentDetails(CommonUtil.convertObjToStr(retrieve.get(ACCT_NUM)));
                termLoanSecurityOB.setLblProdId_Disp(getLblProductID_FD_Disp());
                termLoanOtherDetailsOB.setLblProdID_Disp_ODetails(getLblProductID_FD_Disp());
                termLoanRepaymentOB.setLblProdID_RS_Disp(getLblProductID_FD_Disp());
                termLoanGuarantorOB.setLblProdID_GD_Disp(getLblProductID_FD_Disp());
                termLoanDocumentDetailsOB.setLblProdID_Disp_DocumentDetails(getLblProductID_FD_Disp());
                setLblProdID_IDetail_Disp(getLblProductID_FD_Disp());
                termLoanInterestOB.setLblProdID_IM_Disp(getLblProductID_FD_Disp());
                termLoanClassificationOB.setLblProdID_CD_Disp(getLblProductID_FD_Disp());
                if(loanType.equals("LTD") && getStrACNumber().length()>0){
                    HashMap accountMap=new  HashMap();
                    accountMap.put("ACCT_NUM",getStrACNumber());
                    List lst=ClientUtil.executeQuery("getDepositbeforeAuthDetails",accountMap);//getDepositDetails
                    if(lst !=null && lst.size()>0){
                        accountMap=(HashMap)lst.get(0);
                        setLblDepositNo(CommonUtil.convertObjToStr(accountMap.get("DEPOSIT_NO")));
                    }
                }
                else
                    setLblDepositNo("");
            }
            retrieve = null;
            transactionMap = null;
            resultList = null;
        }catch(Exception e){
            log.info("Exception caught in populateAccountNumber: "+e);
            parseException.logException(e,true);
        }
    }
    
    /*
     * If the customer is a share holder then the query will return a value
     * more than 0
     */
    public boolean isThisCustShareHolder(String strCust){
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
    public boolean isThisCustDepositAcctHolder(String strCust){
        boolean isDepositAcctHolder = false;
        try{
            HashMap transactionMap = new HashMap();
            HashMap retrieve;
            transactionMap.put("CUST_ID", strCust);
            List resultList = (List) ClientUtil.executeQuery("getThisCustDepositAcctHolderOrNot", transactionMap);
            if (resultList.size() > 0){
                // If the Account Number exist in Database
                retrieve = (HashMap) resultList.get(0);
                if (CommonUtil.convertObjToInt(retrieve.get("NO_ACCT")) <= 0){
                    showWarningMsg("isNotADepositAcctHolderWarning");
                }else{
                    isDepositAcctHolder = true;
                }
            }
        }catch(Exception e){
            log.info("Exception caught in isThisCustDepositAcctHolder: "+e);
            parseException.logException(e,true);
        }
        return isDepositAcctHolder;
    }
    
    //regarding reapayment outstanding +uptodateinterest
    public void interestTransaction(HashMap interstMap)throws Exception{
        try{
            interstMap.put("INT_TRANSACTION_REPAYMENT","INT_TRANSACTION_REPAYMENT");
            HashMap mapData =  (HashMap) proxy.executeQuery(interstMap, operationMap);
        }catch(Exception e){
            e.printStackTrace();
        }
    }
    
    public void addPledgeAmountMap(String docNumber,double pledgeAmt){
          try
        {
            if(pledgeValMap==null){
                pledgeValMap= new HashMap();
            }
        HashMap tempMap=new HashMap();
        tempMap.put("DOC_NO",docNumber);
        tempMap.put("PLEDGE_AMT",new Double(pledgeAmt));
        pledgeValMap.put(docNumber,tempMap);
        }
        catch(Exception e)
        {
            ////System.out.println("eeeeeeeeeeeee"+e);
            e.printStackTrace();
        }
    }

    public HashMap validatePledgeAmount(String docNumber,double pledgeAmt){
        HashMap tempMap=new HashMap();
        HashMap returnMap=new HashMap();
//        tempMap.put("DOC_NO",docNumber);
//        tempMap.put("PLEDGE_AMT",new Double(pledgeAmt));
//        pledgeValMap.put(docNumber,tempMap);
        if(pledgeValMap !=null){
//            Set set=pledgeValMap.keySet();
//            Object obj[]=(Object[])set.toArray();
            for(int i=0;i<pledgeValMap.size();i++){
                 tempMap=(HashMap)pledgeValMap.get(docNumber);
                if(tempMap !=null && CommonUtil.convertObjToStr(tempMap.get("DOC_NO")).equals(docNumber)){
                    if(CommonUtil.convertObjToDouble(tempMap.get("PLEDGE_AMT")).doubleValue()<pledgeAmt){
                        returnMap.putAll(tempMap);
                        return returnMap;
                    }
                }
            }
        }
        return returnMap;
    }
    
    public boolean checkSanctionNoDublication(String strSanctionNo){
        boolean save = false;
        try{
            HashMap transactionMap = new HashMap();
            HashMap retrieve;
            transactionMap.put("sanctionNo", strSanctionNo);
            List resultList = (List) ClientUtil.executeQuery("getCountOfSanctionNo", transactionMap);
            if (!(strSanctionNo.equals(""))){
                if (resultList.size() > 0){
                    // If the Query has result
                    retrieve = (HashMap) resultList.get(0);
                    if (CommonUtil.convertObjToStr(retrieve.get("SANCTION_NO")).equals(NUMERIC_ZERO)){
                        save = true;
                    }else{
                        if (getStrRealSanctionNo().equals(strSanctionNo)){
                            // Don't do anything if Existing and entered Sanction Number are same
                            save = true;
                        }else{
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
        }catch(Exception e){
            log.info("Exception caught in checkSanctionNoDublication: "+e);
            parseException.logException(e,true);
        }
        return save;
    }
    
    public void deleteFacilityRecord(int sanctionSlNo, int slno){
        try{
            ////System.out.println("#$#$# (DELETE) facilityAll : "+facilityAll);
            HashMap removedRecord = new HashMap();
            String strSanctionNo = CommonUtil.convertObjToStr(((ArrayList)tblSanctionMain.getDataArrayList().get(sanctionSlNo)).get(1));
            sanctionSlNo = CommonUtil.convertObjToInt(((ArrayList)tblSanctionMain.getDataArrayList().get(sanctionSlNo)).get(0));
            //            slno       = CommonUtil.convertObjToInt(((ArrayList)tblSanctionFacility.getDataArrayList().get(slno)).get(0));
            String strFacilityKey = getFacilityKey(strSanctionNo, slno);
            if (facilityAll.containsKey(strFacilityKey)) {
                removedRecord = (HashMap) facilityAll.remove(strFacilityKey);
                if (removedRecord.get(COMMAND).equals(UPDATE)){
                    // Change the status to DELETE if it is existing in Database
                    removedRecord.put(COMMAND, DELETE);
                    removedFaccilityTabValues.put(strFacilityKey, removedRecord);
                }
            }
            removedRecord = null;
            strFacilityKey = null;
            strSanctionNo = null;
        }catch(Exception e){
            log.info("Exception caught in deleteFacilityRecord: "+e);
            parseException.logException(e,true);
        }
    }
    
    public void populateCustomerProdLeveFields(){
        try{
            if (termLoanBorrowerOB.getTxtCustID().length() > 0){
                HashMap transactionMap = new HashMap();
                HashMap retrieve;
                transactionMap.put("CUST_ID", termLoanBorrowerOB.getTxtCustID());
                List resultList = (List) ClientUtil.executeQuery("getCustGroupDesc", transactionMap);
                if (resultList.size() > 0){
                    retrieve = (HashMap) resultList.get(0);
                    setTxtGroupDesc(CommonUtil.convertObjToStr(retrieve.get(CUSTOMERGROUP)));
                }
                transactionMap = null;
                retrieve = null;
                resultList = null;
            }
        }catch(Exception e){
            log.info("Exception caught in populateCustomerProdLeveFields: "+e);
            parseException.logException(e,true);
        }
    }
    
    public void setDefaultValB4AcctCreation(){
        try{
            setCboAccStatus(CommonUtil.convertObjToStr(this.getCbmAccStatus().getDataForKey(NEW)));
            setTdtDemandPromNoteDate(DateUtil.getStringDate(curDate));
            termLoanBorrowerOB.getTxtCustID();
        }catch(Exception e){
            log.info("Exception caught in setDefaultValB4AcctCreation: "+e);
            parseException.logException(e,true);
        }
    }
    
    public void sanctionFacilityEditWarning(){
        int option = -1;
        String[] options = {objTermLoanRB.getString("cDialogOk")};
        option = COptionPane.showOptionDialog(null, objTermLoanRB.getString("existanceFacilityEditWarning"), CommonConstants.WARNINGTITLE,
        COptionPane.OK_OPTION, COptionPane.WARNING_MESSAGE, null, options, options[0]);
    }
    
    public void repaymentExistingWarning(){
        int option = -1;
        String[] options = {objTermLoanRB.getString("cDialogOk")};
        option = COptionPane.showOptionDialog(null, objTermLoanRB.getString("existanceRepaymentWarning"), CommonConstants.WARNINGTITLE,
        COptionPane.OK_OPTION, COptionPane.WARNING_MESSAGE, null, options, options[0]);
    }
    
    public int interestGetFromValChangeWarn(){
        int option = -1;
        String[] options = {objTermLoanRB.getString("cDialogYes"), objTermLoanRB.getString("cDialogNo")};
        option = COptionPane.showOptionDialog(null, objTermLoanRB.getString("interestGetFromValChangeWarning"), CommonConstants.WARNINGTITLE,
        COptionPane.YES_NO_OPTION, COptionPane.WARNING_MESSAGE, null, options, options[0]);
        return option;
    }
    
    public int showWarningMsg(String strWarnMsg){
        int option = -1;
        String[] options = {objTermLoanRB.getString("cDialogOk")};
        option = COptionPane.showOptionDialog(null, objTermLoanRB.getString(strWarnMsg), CommonConstants.WARNINGTITLE,
        COptionPane.OK_OPTION, COptionPane.WARNING_MESSAGE, null, options, options[0]);
        return option;
    }
    
    public int constitutionCustWarn(String strWarnMsg){
        return showWarningMsg(strWarnMsg);
    }
    
    public void existInstallment(HashMap resultMap,boolean uniformPrincipal)throws Exception{
             TableModel tableModel = new TableModel();
             if(uniformPrincipal)
                 setInstallmentTitleForUniformType();
             else
                 setInstallmentTitle();
                ArrayList heading = installmentTitle;
                ArrayList data = (ArrayList) resultMap.get("TABLEDATA");
                tableModel.setHeading(heading);
                if(data!=null){
                    tableModel.setData(data);
//                    ////System.out.println("### Data Size : " + installmentAllTabRecords.size());
                }
                tableModel.fireTableDataChanged();
                CTable tblInstallment =new CTable();
                tblInstallment.setModel(tableModel);
                tblInstallment.revalidate();
                tblInstallment.show();
               
    }
    
    /**
     * This method will destroy or nullify the ArrayList, LinkedHashMap,
     * EnhancedTableModel, TableUtil instances
     */
    public void destroyObjects(){
        termLoanBorrowerOB.destroyObjects();
        termLoanAuthorizedSignatoryOB.destroyObjects();
        termLoanAuthorizedSignatoryInstructionOB.destroyObjects();
        termLoanPoAOB.destroyObjects();
        termLoanSecurityOB.destroyObjects();
        termLoanRepaymentOB.destroyObjects();
        termLoanInterestOB.destroyObjects();
        termLoanGuarantorOB.destroyObjects();
        termLoanDocumentDetailsOB.destroyObjects();
        agriSubLimitOB.destroyObjects();
//        settlementOB.destroyObjects();//dontdelete
        agriSubLimitOB.destroyObjects();
        tblSanctionFacility = null;
        tblSanctionMain = null;
        tableUtilSanction_Facility = null;
        tableUtilSanction_Main = null;
        facilityTabSanction = null;
        facilityAll = null;
        sanctionMainAllTabRecords = null;
        advanceLiablityMap=null;
        memberTypeMap = null;
        deletedMemberTypeMap = null;
        collateralMap = null;
        deletedCollateralMap = null;
        depositTypeMap=null;
        deletedDepositTypeMap=null;
        losTypeMap=null;
        
        pledgeValMap =null;
        objSMSSubscriptionTO=null;
        salarySecurityAll=null;
        salarySecurityDeleteAll=null;
        tblJointCollateral.setDataArrayList(null,tableCollateralJointTitle);
        tblSalarySecrityTable.setDataArrayList(null,salaryTitle);
        resetSecurityMemberTableValues();
        resetSecurityCollateralTableValues();
        resetDepositTypeTableValues();
        resetLosTypeTableValues();
        resetSecurityVehicleTableValues();
        
//        actTransOB.resetAcctTransfer();//dontdelete
    }
    
    /**
     * This method will create instances like ArrayList, LinkedHashMap,
     * EnhancedTableModel, TableUtil
     */
    public void createObject(){
        termLoanBorrowerOB.createObject();
        termLoanSecurityOB.createObject();
        termLoanRepaymentOB.createObject();
        termLoanInterestOB.createObject();
        //        if (loanType.equals("OTHERS")) {
        termLoanAuthorizedSignatoryOB.createObject();
        termLoanAuthorizedSignatoryInstructionOB.createObject();
        termLoanPoAOB.createObject();
        termLoanGuarantorOB.createObject();
        termLoanDocumentDetailsOB.createObject();
        termLoanAdditionalSanctionOB.createObject();
        //        }
        tblSanctionFacility = new EnhancedTableModel(null, sanctionFacilityTitle);
        tblSanctionMain = new EnhancedTableModel(null, sanctionMainTitle);
        tblShare=new EnhancedTableModel(null,shareTitle);
        facilityTabSanction = new ArrayList();
        tblSanctionFacility.setDataArrayList(null, sanctionFacilityTitle);
        sanctionMainAllTabRecords = new ArrayList();
        tblSanctionMain.setDataArrayList(null, sanctionMainTitle);
        facilityAll = new LinkedHashMap();
        tableUtilSanction_Facility = new TableUtil();
        tableUtilSanction_Facility.setAttributeKey(SLNO);
        tableUtilSanction_Main = new TableUtil();
        tableUtilSanction_Main.setAttributeKey(SANCTION_SL_NO);
        pledgeValMap =new HashMap();
//        settlementOB.createObject();//dontdelete
    }
    public void destroyCreateSanctionFacilityObjects(){
        sanctionFacilityAllTabRecords = new ArrayList();
        sanctionFacilityAll = new LinkedHashMap();
        tableUtilSanction_Facility.setAllValues(sanctionFacilityAll);
        tableUtilSanction_Facility.setTableValues(sanctionFacilityAllTabRecords);
    }
    
    public int getFacilitySize() {
        return facilityAll.size();
    }
    public boolean checkAllfacilityDetails(){
        ArrayList mainSanction=tblSanctionMain.getDataArrayList();
        ArrayList sanctionDetails=tblSanctionFacility.getDataArrayList();
        ////System.out.println("mainSanctionArrayList"+mainSanction);
        ////System.out.println("sanctionDetails##"+sanctionDetails);
        Set keySet =  sanctionMainAll.keySet();
        Object[] objKeySet = (Object[]) keySet.toArray();
        for(int i=0;i<mainSanction.size();i++){
            String strSanctionNumber=CommonUtil.convertObjToStr(((ArrayList)mainSanction.get(i)).get(1));
            int    SlNo=CommonUtil.convertObjToInt(((ArrayList)mainSanction.get(i)).get(0));
            for(int j=0;j<sanctionMainAll.size();j++){
                if((CommonUtil.convertObjToStr(((HashMap)sanctionMainAll.get(objKeySet[j])).get(SANCTION_SL_NO)).equals(String .valueOf(SlNo)))) {
                    HashMap eachRecs= new HashMap();
                    LinkedHashMap sanctionFacilityAlls=new LinkedHashMap();
                    eachRecs = (HashMap)sanctionMainAll.get(objKeySet[j]);
                    sanctionFacilityAlls = (LinkedHashMap) eachRecs.get(SANCTION_FACILITY_ALL);
                    Set sanFacilityKeySet =  sanctionFacilityAlls.keySet();
                    Object[] objSanFacilityKeySet = (Object[]) sanFacilityKeySet.toArray();
                    for(int k=0;k<sanctionFacilityAlls.size();k++){
                        int slno = CommonUtil.convertObjToInt(((HashMap)sanctionFacilityAlls.get(objSanFacilityKeySet[k])).get(SLNO));
                        String strFacilityKeys= getFacilityKey(strSanctionNumber, slno);
                        ////System.out.println("strFacilitykey "+strFacilityKeys+" SlNo "+SlNo+" strSanctionNumber "+strSanctionNumber);
                        if(!facilityAll.containsKey(strFacilityKeys)){
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
    public boolean checkAllfacilitySanctionnoUpdateDetails(String newSanctionNo,String sanctionSlNo){
        ArrayList mainSanction=tblSanctionMain.getDataArrayList();
        ArrayList sanctionDetails=tblSanctionFacility.getDataArrayList();
        ////System.out.println("mainSanctionArrayList"+mainSanction);
        ////System.out.println("sanctionDetails##"+sanctionDetails);
        boolean isTrue=false;
        Set keySet =  sanctionMainAll.keySet();
        Object[] objKeySet = (Object[]) keySet.toArray();
        //        for(int i=0;i<mainSanction.size();i++){
        //            String strSanctionNumber=CommonUtil.convertObjToStr(((ArrayList)mainSanction.get(i)).get(1));
        String strSanctionNumber=oldSanction_no;
        int    SlNo=CommonUtil.convertObjToInt(sanctionSlNo);//((ArrayList)mainSanction.get(i)).get(0));
        for(int j=0;j<sanctionMainAll.size();j++){
            if((CommonUtil.convertObjToStr(((HashMap)sanctionMainAll.get(objKeySet[j])).get(SANCTION_SL_NO)).equals(String .valueOf(SlNo)))) {
                HashMap eachRecs= new HashMap();
                LinkedHashMap sanctionFacilityAlls=new LinkedHashMap();
                eachRecs = (HashMap)sanctionMainAll.get(objKeySet[j]);
                sanctionFacilityAlls = (LinkedHashMap) eachRecs.get(SANCTION_FACILITY_ALL);
                Set sanFacilityKeySet =  sanctionFacilityAlls.keySet();
                Object[] objSanFacilityKeySet = (Object[]) sanFacilityKeySet.toArray();
                for(int k=0;k<sanctionFacilityAlls.size();k++){
                    int slno = CommonUtil.convertObjToInt(((HashMap)sanctionFacilityAlls.get(objSanFacilityKeySet[k])).get(SLNO));
                    String strFacilityKeys= getFacilityKey(strSanctionNumber, slno);
                    String strFacilitySetnewKeys= getFacilityKey(newSanctionNo, slno);
                    ////System.out.println("strFacilitykey "+strFacilityKeys+" SlNo "+SlNo+" strSanctionNumber "+strSanctionNumber);
                    if(facilityAll.containsKey(strFacilityKeys)){
                        facilityAll.put(strFacilitySetnewKeys,facilityAll.get(strFacilityKeys));
                        if(!strFacilitySetnewKeys.equals(strFacilityKeys))
                            facilityAll.remove(strFacilityKeys);
                        isTrue=true;
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
    
    public void showSalaryTableValues(int selectedRow ){
        if(selectedRow !=-1){
            ArrayList selectedList= (ArrayList)tblSalarySecrityTable.getDataArrayList().get(selectedRow);
            String slno=CommonUtil.convertObjToStr(selectedList.get(0));
            TermLoanSecuritySalaryTO objTermLoanSecuritySalaryTO =(TermLoanSecuritySalaryTO)salarySecurityAll.get(String.valueOf(slno));
            
            setTxtSalaryCertificateNo(CommonUtil.convertObjToStr(objTermLoanSecuritySalaryTO.getSalaryCerficateNo()));
            setTxtEmployerName(CommonUtil.convertObjToStr(objTermLoanSecuritySalaryTO.getEmpName()));
            setTxtAddress(CommonUtil.convertObjToStr(objTermLoanSecuritySalaryTO.getEmpAddress()));
            setCboSecurityCity(CommonUtil.convertObjToStr(objTermLoanSecuritySalaryTO.getCity()));
            setTxtPinCode(CommonUtil.convertObjToStr(objTermLoanSecuritySalaryTO.getPin()));
            setTxtDesignation(objTermLoanSecuritySalaryTO.getDesignation());
            setTxtContactNo(CommonUtil.convertObjToStr(objTermLoanSecuritySalaryTO.getContactNo()));
            setTdtRetirementDt(CommonUtil.convertObjToStr(objTermLoanSecuritySalaryTO.getRetirementDt()));
            setTxtMemberNum(CommonUtil.convertObjToStr(objTermLoanSecuritySalaryTO.getEmpMemberNo()));
            setTxtTotalSalary(CommonUtil.convertObjToStr(objTermLoanSecuritySalaryTO.getTotalSalary()));
            setTxtNetWorth(CommonUtil.convertObjToStr(objTermLoanSecuritySalaryTO.getNetworth()));
            setTxtSalaryRemark(CommonUtil.convertObjToStr(objTermLoanSecuritySalaryTO.getSalaryRemarks()));
            
//              objTermLoanSecuritySalaryTO.setSlNo( new Double(slno));
//            objTermLoanSecuritySalaryTO.setAcctNum(getStrACNumber());
//            objTermLoanSecuritySalaryTO.setSalaryCerficateNo(getTxtSalaryCertificateNo());
//            objTermLoanSecuritySalaryTO.setEmpName(getTxtEmployerName());
//            objTermLoanSecuritySalaryTO.setEmpAddress(getTxtAddress());
//            objTermLoanSecuritySalaryTO.setCity(getCboSecurityCity());
//            objTermLoanSecuritySalaryTO.setPin(CommonUtil.convertObjToDouble(getTxtPinCode()));
//            objTermLoanSecuritySalaryTO.setDesignation(getTxtDesignation());
//            objTermLoanSecuritySalaryTO.setContactNo(CommonUtil.convertObjToDouble(getTxtContactNo()));
//            objTermLoanSecuritySalaryTO.setRetirementDt(getProperDateFormat(getTdtRetirementDt()));
//            objTermLoanSecuritySalaryTO.setEmpMemberNo(getTxtMemberNum());
//            objTermLoanSecuritySalaryTO.setTotalSalary(CommonUtil.convertObjToDouble(getTxtTotalSalary()));
//            objTermLoanSecuritySalaryTO.setNetworth(getTxtNetWorth());
//            objTermLoanSecuritySalaryTO.setSalaryRemarks(getTxtSalaryRemark());
        }
        
    }
    public void setSalarySecrityTableValue(int selectedRow,int rowCount){
        
        ArrayList singleList=new ArrayList();
        ArrayList totList=new ArrayList();
        if(salarySecurityAll ==null)
            salarySecurityAll =new LinkedHashMap();
        
        TermLoanSecuritySalaryTO obj =new TermLoanSecuritySalaryTO();
        if(rowCount==0 && selectedRow ==-1){
            securitySlNo++;
          obj= addTermLoanSecuritySalaryTO(1);
            singleList.add(String.valueOf("1"));
                singleList.add(getTxtSalaryCertificateNo());
                singleList.add(getTxtMemberNum());
                singleList.add(getTxtEmployerName());
                singleList.add(getTxtContactNo());
                singleList.add(getTxtNetWorth());
//            tblSalarySecrityTable.insertRow(singleList);
            totList.add(singleList);
            tblSalarySecrityTable.setDataArrayList(totList, salaryTitle);
            salarySecurityAll.put(String.valueOf(1),obj);
            
            
        }else if(selectedRow ==-1){
            singleList  =(ArrayList)tblSalarySecrityTable.getDataArrayList().get(tblSalarySecrityTable.getDataArrayList().size()-1);
            if(singleList!=null && singleList.size()>0 ){
                totList=(ArrayList)tblSalarySecrityTable.getDataArrayList();
                int  slno=CommonUtil.convertObjToInt(singleList.get(0));
                
                securitySlNo++;
//                slno++;
                ArrayList newList=new ArrayList();
                newList.add(String.valueOf(securitySlNo));
                newList.add(getTxtSalaryCertificateNo());
                newList.add(getTxtMemberNum());
                newList.add(getTxtEmployerName());
                newList.add(getTxtContactNo());
                newList.add(getTxtNetWorth());
                //            tblSalarySecrityTable.insertRow(singleList);
                totList.add(newList);
                tblSalarySecrityTable.setDataArrayList(totList, salaryTitle);
                salarySecurityAll.put(String.valueOf(securitySlNo),addTermLoanSecuritySalaryTO(securitySlNo));
            }
            
        }else{
            singleList  =(ArrayList)tblSalarySecrityTable.getDataArrayList().get(selectedRow);
            if(singleList!=null && singleList.size()>0 ){
                int  slno=CommonUtil.convertObjToInt(singleList.get(0));
                //start
                tblSalarySecrityTable.removeRow(selectedRow);
                ArrayList newList = new ArrayList();
                newList.add(String.valueOf(slno));
                newList.add(getTxtSalaryCertificateNo());
                newList.add(getTxtMemberNum());
                newList.add(getTxtEmployerName());
                newList.add(getTxtContactNo());
                newList.add(getTxtNetWorth());
                totList.add(newList);
                tblSalarySecrityTable.insertRow(selectedRow,newList);
                //End
                 salarySecurityAll.put(String.valueOf(slno),addTermLoanSecuritySalaryTO(slno));
            }
        }
        
    }
    
        public void setCropTableValue(int selectedRow,int rowCount){
            
            ArrayList singleList=new ArrayList();
            ArrayList totList=new ArrayList();
            if(salarySecurityAll ==null)
                salarySecurityAll =new LinkedHashMap();
            
            TermLoanSecuritySalaryTO obj =new TermLoanSecuritySalaryTO();
            if(rowCount==0 && selectedRow ==-1){
                securitySlNo++;
                obj= addTermLoanSecuritySalaryTO(1);
                singleList.add(String.valueOf("1"));
                singleList.add(getTxtSalaryCertificateNo());
                singleList.add(getTxtMemberNum());
                singleList.add(getTxtEmployerName());
                singleList.add(getTxtContactNo());
                singleList.add(getTxtNetWorth());
                //            tblSalarySecrityTable.insertRow(singleList);
                totList.add(singleList);
                tblSalarySecrityTable.setDataArrayList(totList, salaryTitle);
                salarySecurityAll.put(String.valueOf(1),obj);
                
                
            }else if(selectedRow ==-1){
                singleList  =(ArrayList)tblSalarySecrityTable.getDataArrayList().get(tblSalarySecrityTable.getDataArrayList().size()-1);
                if(singleList!=null && singleList.size()>0 ){
                    totList=(ArrayList)tblSalarySecrityTable.getDataArrayList();
                    int  slno=CommonUtil.convertObjToInt(singleList.get(0));
                    
                    securitySlNo++;
                    //                slno++;
                    ArrayList newList=new ArrayList();
                    newList.add(String.valueOf(securitySlNo));
                    newList.add(getTxtSalaryCertificateNo());
                    newList.add(getTxtMemberNum());
                    newList.add(getTxtEmployerName());
                    newList.add(getTxtContactNo());
                    newList.add(getTxtNetWorth());
                    //            tblSalarySecrityTable.insertRow(singleList);
                    totList.add(newList);
                    tblSalarySecrityTable.setDataArrayList(totList, salaryTitle);
                    salarySecurityAll.put(String.valueOf(securitySlNo),addTermLoanSecuritySalaryTO(securitySlNo));
                }
                
            }else{
                singleList  =(ArrayList)tblSalarySecrityTable.getDataArrayList().get(selectedRow);
                if(singleList!=null && singleList.size()>0 ){
                    int  slno=CommonUtil.convertObjToInt(singleList.get(0));
                    salarySecurityAll.put(String.valueOf(slno),addTermLoanSecuritySalaryTO(slno));
                }
            }

    }

    // available balance is changed then we need to call this method
    private HashMap  editModeTransDetail() {
        HashMap transMap =new HashMap();
        if(getResult() != ClientConstants.ACTIONTYPE_FAILED) {
            String displayStr = "";
            String oldBatchId = "";
            String newBatchId = "";
            String actNum = CommonUtil.convertObjToStr(getStrACNumber());
            transMap = new HashMap();
            transMap.put("LINK_BATCH_ID",getStrACNumber());
            transMap.put("CURR_DT", curDate);
            List lst = ClientUtil.executeQuery("getAuthBatchTxTransferTOs", transMap);
            if(lst !=null && lst.size()>0){
                displayStr += "Transfer Transaction Details...\n";
                ArrayList transferTransList=new ArrayList();
                for(int i = 0;i<lst.size();i++){
                    
                    TxTransferTO objTxTransferTO=(TxTransferTO)lst.get(i);
                    
                   
                  transMap.put("OLDAMOUNT",objTxTransferTO.getAmount());
                  transferTransList.add(objTxTransferTO);
                  objTxTransferTO.setAmount(CommonUtil.convertObjToDouble(getTxtLimit_SD()));
                    
                }
                 transMap.put("TxTransferTO",transferTransList);
            }
           
            transMap = new HashMap();
           
            transMap.put("LINK_BATCH_ID",getStrACNumber());
            transMap.put("TRANS_DT", curDate);
            lst = ClientUtil.executeQuery("getCashTransactionTOForAuthorzation", transMap);
            
            if(lst !=null && lst.size()>0){
              
                for(int i = 0;i<lst.size();i++){
                 CashTransactionTO   objCashTransactionTO = (CashTransactionTO)lst.get(i);
                 objCashTransactionTO.setCommand("UPDATE");
                 transMap.put("OLDAMOUNT",objCashTransactionTO.getAmount());
                 objCashTransactionTO.setAmount(CommonUtil.convertObjToDouble(getTxtLimit_SD()));
                 transMap.put("CashTransactionTO",objCashTransactionTO);
                
                }
            }
        }
        return transMap;
    }
    
    public void populateDepositTypeDetails(String row){
        try{
            resetDepositTypeDetails();
            final TermLoanDepositTypeTO objDepositTypeTO = (TermLoanDepositTypeTO)depositTypeMap.get(row);
            populateDepositTableData(objDepositTypeTO);
            
        }catch(Exception e){
            parseException.logException(e,true);
        }
    }
    public void populateLosTypeDetails(String row){
        try{
            resetLosTypeDetails();
            final TermLoanLosTypeTO objLosTypeTO = (TermLoanLosTypeTO)losTypeMap.get(row);
            populateLosTableData(objLosTypeTO);
            
        }catch(Exception e){
            parseException.logException(e,true);
        }
    }
    
    private void populateDepositTableData(TermLoanDepositTypeTO objDepositTypeTO)  throws Exception{
        setTxtDepNo(CommonUtil.convertObjToStr(objDepositTypeTO.getTxtDepNo()));
        setCboProductTypeSecurity(CommonUtil.convertObjToStr(getCbmProdTypeSecurity().getDataForKey(CommonUtil.convertObjToStr(objDepositTypeTO.getProdType()))));
        setCboDepProdID(CommonUtil.convertObjToStr(getCbmDepProdID().getDataForKey(CommonUtil.convertObjToStr(objDepositTypeTO.getProdId()))));
        setTxtDepAmount(CommonUtil.convertObjToStr(objDepositTypeTO.getTxtDepAmount()));
        setTdtDepDt(CommonUtil.convertObjToStr(objDepositTypeTO.getTdtDepDt()));
        setTxtMaturityDt(CommonUtil.convertObjToStr(objDepositTypeTO.getTxtMaturityDt()));
        setTxtMaturityValue(CommonUtil.convertObjToStr(objDepositTypeTO.getTxtMaturityValue()));
        setTxtRateOfInterest(CommonUtil.convertObjToStr(objDepositTypeTO.getTxtRateOfInterest()));
        setChanged();
        //        notifyObservers();
    }
    
    private void populateLosTableData(TermLoanLosTypeTO objLosTypeTO)  throws Exception{
        setTxtLosSecurityNo(CommonUtil.convertObjToStr(objLosTypeTO.getLosSecurityNo()));
        setCboLosOtherInstitution(CommonUtil.convertObjToStr(getCbmLosInstitution().getDataForKey(CommonUtil.convertObjToStr(objLosTypeTO.getLosInstitution()))));
        setCboLosSecurityType(CommonUtil.convertObjToStr(getCbmLosSecurityType().getDataForKey(CommonUtil.convertObjToStr(objLosTypeTO.getLosSecurityType()))));
        setTxtLosName(CommonUtil.convertObjToStr(objLosTypeTO.getLosName()));
        setTdtLosIssueDate(CommonUtil.convertObjToStr(objLosTypeTO.getLosIssueDt()));
        setTdtLosMaturityDt(CommonUtil.convertObjToStr(objLosTypeTO.getLosMatDt()));
        setTxtLosMaturityValue(CommonUtil.convertObjToStr(objLosTypeTO.getLosMaturityValue()));
        setTxtLosRemarks(CommonUtil.convertObjToStr(objLosTypeTO.getLosRemarks()));
        setTxtLosAmount(CommonUtil.convertObjToStr(objLosTypeTO.getLosAmount()));
        setChanged();
        //        notifyObservers();
    }
    
    
    public void deleteSalarySecrityTableValue(int salarytblSelectedRow){
        ArrayList totList =new ArrayList();
        
       if(salarySecurityDeleteAll == null){
           salarySecurityDeleteAll =new LinkedHashMap();
       }
       if(salarytblSelectedRow !=-1){
           ArrayList singleList=(ArrayList)tblSalarySecrityTable.getDataArrayList().get(salarytblSelectedRow);
           String slno =CommonUtil.convertObjToStr(singleList.get(0));
           TermLoanSecuritySalaryTO objTermLoanSecuritySalaryTO = new TermLoanSecuritySalaryTO();
           objTermLoanSecuritySalaryTO=(TermLoanSecuritySalaryTO)salarySecurityAll.get(String.valueOf(slno));
           objTermLoanSecuritySalaryTO.setStatus(CommonConstants.STATUS_DELETED);
           salarySecurityDeleteAll.put(String.valueOf(slno),objTermLoanSecuritySalaryTO);
           salarySecurityAll.remove(String.valueOf(slno));
           tblSalarySecrityTable.removeRow(salarytblSelectedRow);
           
       }else{
           ClientUtil.displayAlert("Please Select Record then Delete");
       }
    }
    
    public TermLoanSecuritySalaryTO addTermLoanSecuritySalaryTO(double slno){
        
           final TermLoanSecuritySalaryTO objTermLoanSecuritySalaryTO = new TermLoanSecuritySalaryTO();
        try{
             TermLoanSecuritySalaryTO obj =(TermLoanSecuritySalaryTO)salarySecurityAll.get(String.valueOf(slno));
             
            objTermLoanSecuritySalaryTO.setSlNo( new Double(slno));
            objTermLoanSecuritySalaryTO.setAcctNum(getStrACNumber());
            objTermLoanSecuritySalaryTO.setSalaryCerficateNo(getTxtSalaryCertificateNo());
            objTermLoanSecuritySalaryTO.setEmpName(getTxtEmployerName());
            objTermLoanSecuritySalaryTO.setEmpAddress(getTxtAddress());
            objTermLoanSecuritySalaryTO.setCity(getCboSecurityCity());
            objTermLoanSecuritySalaryTO.setPin(CommonUtil.convertObjToDouble(getTxtPinCode()));
            objTermLoanSecuritySalaryTO.setDesignation(getTxtDesignation());
            objTermLoanSecuritySalaryTO.setContactNo(CommonUtil.convertObjToLong(getTxtContactNo()));
            //System.out.println("date retirement ttt==="+getTdtRetirementDt());
             //System.out.println("date retirement 11==="+getDateFormat(getTdtRetirementDt()));
            objTermLoanSecuritySalaryTO.setRetirementDt(getDateFormat(getTdtRetirementDt()));
            objTermLoanSecuritySalaryTO.setEmpMemberNo(getTxtMemberNum());
            objTermLoanSecuritySalaryTO.setTotalSalary(CommonUtil.convertObjToDouble(getTxtTotalSalary()));
            objTermLoanSecuritySalaryTO.setNetworth(getTxtNetWorth());
            objTermLoanSecuritySalaryTO.setSalaryRemarks(getTxtSalaryRemark());
//            if(!(obj !=null && obj.getStatus().equals(CommonConstants.STATUS_MODIFIED)))
//                objTermLoanSecuritySalaryTO.setStatus(CommonConstants.STATUS_CREATED);
            
            if(isSalaryTypeData()){
                objTermLoanSecuritySalaryTO.setStatus(CommonConstants.STATUS_CREATED);
            }else{
                objTermLoanSecuritySalaryTO.setStatus(CommonConstants.STATUS_MODIFIED);
                objTermLoanSecuritySalaryTO.setStatusBy(TrueTransactMain.USER_ID);
                objTermLoanSecuritySalaryTO.setStatusDt(curDate);
            }
          
            
        }catch(Exception e){
            e.printStackTrace();
        }
        return objTermLoanSecuritySalaryTO;
    }
    
    
    
    public void setCbmProdTypeSecurity(String prodType) {
        try {
            HashMap lookUpHash = new HashMap();
            if (prodType.equals("MDS")) {
                lookUpHash.put(CommonConstants.MAP_NAME,"Lock.getAccProductMDS");
            }else{
                lookUpHash.put(CommonConstants.MAP_NAME,"deposit_getProdId");
            }
            lookUpHash.put(CommonConstants.PARAMFORQUERY, null);
            keyValue = ClientUtil.populateLookupData(lookUpHash);
            getKeyValue((HashMap)keyValue.get(CommonConstants.DATA));
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        cbmDepProdID= new ComboBoxModel(key,value);
        this.cbmDepProdID = cbmDepProdID;
        setChanged();
    }
    
    public void resetDepositTypeDetails() {
        setTxtDepNo("");
        setCboProductTypeSecurity("");
        setCboDepProdID("");
        setTdtDepDt("");
        setTxtDepAmount("");
        setTxtMaturityDt("");
        setTxtMaturityValue("");
        setTxtRateOfInterest("");
        
    }
    
    public void resetLosTypeDetails() {
        setTxtLosAmount("");
        setTxtLosName("");
        setTxtLosRemarks("");
        setTxtLosMaturityValue("");
        setTxtLosSecurityNo("");
        setCboLosOtherInstitution("");
        setCboLosSecurityType("");
        setTdtLosIssueDate("");
        setTdtLosMaturityDt("");
        
    }
    
    public void deleteDepositTableData(String val, int row){
        if(deletedDepositTypeMap == null){
            deletedDepositTypeMap = new LinkedHashMap();
        }
        TermLoanDepositTypeTO objDepositTypeTO = (TermLoanDepositTypeTO) depositTypeMap.get(val);
        objDepositTypeTO.setStatus(CommonConstants.STATUS_DELETED);
        deletedDepositTypeMap.put(CommonUtil.convertObjToStr(tblDepositTypeDetails.getValueAt(row,1)),depositTypeMap.get(val));
        Object obj;
        obj=val;
        depositTypeMap.remove(val);
        tblDepositTypeDetails.setDataArrayList(null,tableTitleDepositList);
        try{
            populateDepositTable();
        }
        catch(Exception e){
            parseException.logException(e,true);
        }
    }
    
    
    public void deleteLosTableData(String val, int row){
        if(deletedLosTypeMap == null){
            deletedLosTypeMap = new LinkedHashMap();
        }
        TermLoanLosTypeTO objLosTypeTO = (TermLoanLosTypeTO) losTypeMap.get(val);
        objLosTypeTO.setStatus(CommonConstants.STATUS_DELETED);
        deletedLosTypeMap.put(CommonUtil.convertObjToStr(tblLosTypeDetails.getValueAt(row,2)),losTypeMap.get(val));
        Object obj;
        obj=val;
        losTypeMap.remove(val);
        tblLosTypeDetails.setDataArrayList(null,tableTitleLosList);
        try{
            populateLosTable();
        }
        catch(Exception e){
            parseException.logException(e,true);
        }
    }
    
    
    private void populateDepositTable()  throws Exception{
        ArrayList incDataList = new ArrayList();
        incDataList = new ArrayList(depositTypeMap.keySet());
        ArrayList addList =new ArrayList(depositTypeMap.keySet());
        int length = incDataList.size();
        for(int i=0; i<length; i++){
            ArrayList incTabRow = new ArrayList();
            TermLoanDepositTypeTO objDepositTypeTO = (TermLoanDepositTypeTO) depositTypeMap.get(addList.get(i));
            IncVal.add(objDepositTypeTO);
            if(!objDepositTypeTO.getStatus().equals("DELETED")){
                incTabRow.add(CommonUtil.convertObjToStr(objDepositTypeTO.getProdType()));
                incTabRow.add(CommonUtil.convertObjToStr(objDepositTypeTO.getTxtDepNo()));
                //                incTabRow.add(CommonUtil.convertObjToStr(objDepositTypeTO.getProdId()));
                //                incTabRow.add(CommonUtil.convertObjToStr(objDepositTypeTO.getDepositDt()));
                incTabRow.add(CommonUtil.convertObjToStr(objDepositTypeTO.getTxtDepAmount()));
                //                incTabRow.add(CommonUtil.convertObjToStr(objDepositTypeTO.getIntRate()));
                incTabRow.add(CommonUtil.convertObjToStr(objDepositTypeTO.getTxtMaturityValue()));
                //                incTabRow.add(CommonUtil.convertObjToStr(objDepositTypeTO.getMaturityDt()));
                tblDepositTypeDetails.addRow(incTabRow);
            }
        }
        //        notifyObservers();
    }
    
    private void populateLosTable()  throws Exception{
        ArrayList incDataList = new ArrayList();
        incDataList = new ArrayList(losTypeMap.keySet());
        ArrayList addList =new ArrayList(losTypeMap.keySet());
        int length = incDataList.size();
        for(int i=0; i<length; i++){
            ArrayList incTabRow = new ArrayList();
            TermLoanLosTypeTO objLosTypeTO = (TermLoanLosTypeTO) losTypeMap.get(addList.get(i));
            IncVal.add(objLosTypeTO);
            if(!objLosTypeTO.getStatus().equals("DELETED")){
                incTabRow.add(CommonUtil.convertObjToStr(objLosTypeTO.getLosInstitution()));
                incTabRow.add(CommonUtil.convertObjToStr(objLosTypeTO.getLosName()));
                incTabRow.add(CommonUtil.convertObjToStr(objLosTypeTO.getLosSecurityNo()));
                //                incTabRow.add(CommonUtil.convertObjToStr(objDepositTypeTO.getDepositDt()));
                incTabRow.add(CommonUtil.convertObjToStr(objLosTypeTO.getLosSecurityType()));
                //                incTabRow.add(CommonUtil.convertObjToStr(objDepositTypeTO.getIntRate()));
                incTabRow.add(CommonUtil.convertObjToStr(objLosTypeTO.getLosAmount()));
                //                incTabRow.add(CommonUtil.convertObjToStr(objDepositTypeTO.getMaturityDt()));
                tblLosTypeDetails.addRow(incTabRow);
            }
        }
        //        notifyObservers();
    }
    
    
    private void setDepositTableTile() throws Exception{
        tableTitleDepositList.add("Prod Type");
        tableTitleDepositList.add("Dep No");
        //        tableTitleDepositList.add("Prod Id");
        //        tableTitleDepositList.add("Dep Dt");
        tableTitleDepositList.add("Dep Amt");
        //        tableTitleDepositList.add("ROI");
        tableTitleDepositList.add("Matur Val");
        //        tableTitleDepositList.add("Matur Dt");
        IncVal = new ArrayList();
    }
    
    private void setLosTableTile() throws Exception{
        tableTitleLosList.add("Other Institution");
        tableTitleLosList.add("Name");
        tableTitleLosList.add("Security No");
        
        tableTitleLosList.add("Security Type");
        
        tableTitleLosList.add("Amount");
        IncVal = new ArrayList();
        
        
    }
    
    
    
 // Method to calculate is its Years, Months or Days
    //depending on the numerical value from DataBase...
    // to be displayed in UI
    private String setPeriod(int rV) throws Exception{
        String periodValue;
        if ((rV >= year) && (rV%year == 0)) {
            periodValue = YEAR1;
            rV = rV/year;
        } else if ((rV >= month) && (rV % month == 0)) {
            periodValue=MONTH1;
            rV = rV/month;
        } else if ((rV >= day) && (rV % day == 0)) {
            periodValue=DAY1;
            rV = rV;
        } else {
            periodValue="";
            rV = 0;
        }
        resultValue = rV;
        return periodValue;
    }

 private void resetPeriod(){
        resultValue = 0;
    }
    
    public void addDepositTypeTable(int rowSelected, boolean updateMode){
        try{
            int rowSel=rowSelected;
            final TermLoanDepositTypeTO objDepositTypeTO = new TermLoanDepositTypeTO();
            if( depositTypeMap == null ){
                depositTypeMap = new LinkedHashMap();
            }
            if(getActionType()==ClientConstants.ACTIONTYPE_EDIT){
                if(isDepositTypeData()){
                    objDepositTypeTO.setStatusDt(ClientUtil.getCurrentDate());
                    objDepositTypeTO.setStatusBy(TrueTransactMain.USER_ID);
                    objDepositTypeTO.setStatus(CommonConstants.STATUS_CREATED);
                }else{
                    objDepositTypeTO.setStatusDt(ClientUtil.getCurrentDate());
                    objDepositTypeTO.setStatusBy(TrueTransactMain.USER_ID);
                    objDepositTypeTO.setStatus(CommonConstants.STATUS_MODIFIED);
                }
            }else{
                objDepositTypeTO.setStatusDt(ClientUtil.getCurrentDate());
                objDepositTypeTO.setStatusBy(TrueTransactMain.USER_ID);
                objDepositTypeTO.setStatus(CommonConstants.STATUS_CREATED);
            }
            
            objDepositTypeTO.setBorrowNo(getBorrowerNo());
            
            objDepositTypeTO.setTxtDepNo(getTxtDepNo());
            //            objDepositTypeTO.setProdId(getTxtProductId());
            objDepositTypeTO.setProdType(CommonUtil.convertObjToStr(cbmProdTypeSecurity.getKeyForSelected()));
            objDepositTypeTO. setProdId(CommonUtil.convertObjToStr(cbmDepProdID.getKeyForSelected()));
            objDepositTypeTO. setTxtDepAmount(CommonUtil.convertObjToDouble(getTxtDepAmount()));
            objDepositTypeTO.setTdtDepDt(DateUtil.getDateMMDDYYYY(getTdtDepDt()));
            objDepositTypeTO.setTxtMaturityDt(DateUtil.getDateMMDDYYYY(getTxtMaturityDt()));
            objDepositTypeTO.setTxtMaturityValue(CommonUtil.convertObjToDouble(getTxtMaturityValue()));
            objDepositTypeTO.setTxtRateOfInterest(getTxtRateOfInterest());
            depositTypeMap.put(objDepositTypeTO.getTxtDepNo(),objDepositTypeTO);
            updateDepositTypeDetails(rowSel,objDepositTypeTO,updateMode);
            //            ttNotifyObservers();
        }catch(Exception e){
            parseException.logException(e,true);
        }
    }
    
    public void addLosTypeTable(int rowSelected, boolean updateMode){
        try{
            int rowSel=rowSelected;
            final TermLoanLosTypeTO objLosTypeTO = new TermLoanLosTypeTO();
            if( losTypeMap == null ){
                losTypeMap = new LinkedHashMap();
            }
            if(getActionType()==ClientConstants.ACTIONTYPE_EDIT){
                if(isLosTypeData()){
                    objLosTypeTO.setStatusDt(ClientUtil.getCurrentDate());
                    objLosTypeTO.setStatusBy(TrueTransactMain.USER_ID);
                    objLosTypeTO.setStatus(CommonConstants.STATUS_CREATED);
                }else{
                    objLosTypeTO.setStatusDt(ClientUtil.getCurrentDate());
                    objLosTypeTO.setStatusBy(TrueTransactMain.USER_ID);
                    objLosTypeTO.setStatus(CommonConstants.STATUS_MODIFIED);
                }
            }else{
                objLosTypeTO.setStatusDt(ClientUtil.getCurrentDate());
                objLosTypeTO.setStatusBy(TrueTransactMain.USER_ID);
                objLosTypeTO.setStatus(CommonConstants.STATUS_CREATED);
            }
            
            objLosTypeTO.setBorrowNo(getBorrowerNo());
            objLosTypeTO.setLosInstitution(CommonUtil.convertObjToStr(cbmLosInstitution.getKeyForSelected()));
            objLosTypeTO.setLosName(getTxtLosName());
            objLosTypeTO.setLosRemarks(getTxtLosRemarks());
            objLosTypeTO.setLosSecurityNo(getTxtLosSecurityNo());
            objLosTypeTO.setLosAmount(CommonUtil.convertObjToDouble(getTxtLosAmount()));
            objLosTypeTO.setLosMaturityValue(CommonUtil.convertObjToDouble(getTxtLosMaturityValue()));
            objLosTypeTO.setLosSecurityType(CommonUtil.convertObjToStr(cbmLosSecurityType.getKeyForSelected()));
            objLosTypeTO.setLosIssueDt(DateUtil.getDateMMDDYYYY(getTdtLosIssueDate()));
            objLosTypeTO.setLosMatDt(DateUtil.getDateMMDDYYYY(getTdtLosMaturityDt()));
            losTypeMap.put(objLosTypeTO.getLosSecurityNo(),objLosTypeTO);
            updateLosTypeDetails(rowSel,objLosTypeTO,updateMode);
            //            ttNotifyObservers();
        }catch(Exception e){
            parseException.logException(e,true);
        }
    }
    
    private void updateLosTypeDetails(int rowSel,  TermLoanLosTypeTO objLosTypeTO, boolean updateMode)  throws Exception{
        Object selectedRow;
        boolean rowExists = false;
        //If row already exists update it, else create a new row & append
        for(int i = tblLosTypeDetails.getRowCount(), j = 0; i > 0; i--,j++){
            selectedRow = ((ArrayList)tblLosTypeDetails.getDataArrayList().get(j)).get(2);
            ////System.out.println("DepNo is"+getTxtDepNo());
            if(getTxtLosSecurityNo().equals(CommonUtil.convertObjToStr(selectedRow))){
                rowExists = true;
                ArrayList IncParRow = new ArrayList();
                ArrayList data = tblLosTypeDetails.getDataArrayList();
                data.remove(rowSel);
                IncParRow.add(CommonUtil.convertObjToStr(cboLosOtherInstitution));
                IncParRow.add(getTxtLosName());
                IncParRow.add(getTxtLosSecurityNo());
                IncParRow.add(cboLosSecurityType);
                
                IncParRow.add(getTxtLosAmount());
                
                tblLosTypeDetails.insertRow(rowSel,IncParRow);
                IncParRow = null;
            }
        }
        if(!rowExists){
            ArrayList IncParRow = new ArrayList();
            IncParRow.add(CommonUtil.convertObjToStr(cboLosOtherInstitution));
            IncParRow.add(getTxtLosName());
            IncParRow.add(getTxtLosSecurityNo());
            IncParRow.add(cboLosSecurityType);
            
            IncParRow.add(getTxtLosAmount());
            
            tblLosTypeDetails.insertRow(tblLosTypeDetails.getRowCount(),IncParRow);
            IncParRow = null;
        }
    }
    
    
    private void updateDepositTypeDetails(int rowSel,  TermLoanDepositTypeTO objDepositTypeTO, boolean updateMode)  throws Exception{
        Object selectedRow;
        boolean rowExists = false;
        //If row already exists update it, else create a new row & append
        for(int i = tblDepositTypeDetails.getRowCount(), j = 0; i > 0; i--,j++){
            selectedRow = ((ArrayList)tblDepositTypeDetails.getDataArrayList().get(j)).get(1);
            ////System.out.println("DepNo is"+getTxtDepNo());
            if(getTxtDepNo().equals(CommonUtil.convertObjToStr(selectedRow))){
                rowExists = true;
                ArrayList IncParRow = new ArrayList();
                ArrayList data = tblDepositTypeDetails.getDataArrayList();
                data.remove(rowSel);
                IncParRow.add(CommonUtil.convertObjToStr(cboProductTypeSecurity));
                IncParRow.add(getTxtDepNo());
                //                IncParRow.add(CommonUtil.convertObjToStr(cbmDepProdID.getKeyForSelected()));
                //                IncParRow.add(getTdtDepDt());
                IncParRow.add(getTxtDepAmount());
                //                IncParRow.add(getTxtRateOfInterest());
                IncParRow.add(getTxtMaturityValue());
                //                IncParRow.add(getTxtMaturityDt());
                tblDepositTypeDetails.insertRow(rowSel,IncParRow);
                IncParRow = null;
            }
        }
        if(!rowExists){
            ArrayList IncParRow = new ArrayList();
            IncParRow.add(CommonUtil.convertObjToStr(cboProductTypeSecurity));
            IncParRow.add(getTxtDepNo());
            //            IncParRow.add(CommonUtil.convertObjToStr(cbmDepProdID.getKeyForSelected()));
            //            IncParRow.add(getTdtDepDt());
            IncParRow.add(getTxtDepAmount());
            //            IncParRow.add(getTxtRateOfInterest());
            IncParRow.add(getTxtMaturityValue());
            //            IncParRow.add(getTxtMaturityDt());
            tblDepositTypeDetails.insertRow(tblDepositTypeDetails.getRowCount(),IncParRow);
            IncParRow = null;
        }
    }
    
    
    public void editDeleteCourtDetails(boolean isDelete){
        
        if(isDelete && editCourtDetailsMap !=null && editCourtDetailsMap.size()>0 ){
            TermLoanCourtDetailsTO obj =(TermLoanCourtDetailsTO)editCourtDetailsMap.get(String.valueOf(1)); 
            obj.setStatus(CommonConstants.STATUS_DELETED);
            obj.setCommand(CommonConstants.TOSTATUS_DELETE);
            deleteCourtDetailsMap.put("1",obj);
            editCourtDetailsMap =new HashMap();
            
        }
        
    }
    /**
     * Getter for property cbmIntGetFrom.
     * @return Value of property cbmIntGetFrom.
     */
    public com.see.truetransact.clientutil.ComboBoxModel getCbmIntGetFrom() {
        return cbmIntGetFrom;
    }
    
    /**
     * Setter for property cbmIntGetFrom.
     * @param cbmIntGetFrom New value of property cbmIntGetFrom.
     */
    public void setCbmIntGetFrom(com.see.truetransact.clientutil.ComboBoxModel cbmIntGetFrom) {
        this.cbmIntGetFrom = cbmIntGetFrom;
    }
    
    /**
     * Getter for property cboIntGetFrom.
     * @return Value of property cboIntGetFrom.
     */
    public java.lang.String getCboIntGetFrom() {
        return cboIntGetFrom;
    }
    
    /**
     * Setter for property cboIntGetFrom.
     * @param cboIntGetFrom New value of property cboIntGetFrom.
     */
    public void setCboIntGetFrom(java.lang.String cboIntGetFrom) {
        this.cboIntGetFrom = cboIntGetFrom;
    }
    
    /**
     * Getter for property txtPeriodDifference_Days.
     * @return Value of property txtPeriodDifference_Days.
     */
    public java.lang.String getTxtPeriodDifference_Days() {
        return txtPeriodDifference_Days;
    }
    
    /**
     * Setter for property txtPeriodDifference_Days.
     * @param txtPeriodDifference_Days New value of property txtPeriodDifference_Days.
     */
    public void setTxtPeriodDifference_Days(java.lang.String txtPeriodDifference_Days) {
        this.txtPeriodDifference_Days = txtPeriodDifference_Days;
    }
    
    /**
     * Getter for property txtPeriodDifference_Months.
     * @return Value of property txtPeriodDifference_Months.
     */
    public java.lang.String getTxtPeriodDifference_Months() {
        return txtPeriodDifference_Months;
    }
    
    /**
     * Setter for property txtPeriodDifference_Months.
     * @param txtPeriodDifference_Months New value of property txtPeriodDifference_Months.
     */
    public void setTxtPeriodDifference_Months(java.lang.String txtPeriodDifference_Months) {
        this.txtPeriodDifference_Months = txtPeriodDifference_Months;
    }
    
    /**
     * Getter for property txtPeriodDifference_Years.
     * @return Value of property txtPeriodDifference_Years.
     */
    public java.lang.String getTxtPeriodDifference_Years() {
        return txtPeriodDifference_Years;
    }
    
    /**
     * Setter for property txtPeriodDifference_Years.
     * @param txtPeriodDifference_Years New value of property txtPeriodDifference_Years.
     */
    public void setTxtPeriodDifference_Years(java.lang.String txtPeriodDifference_Years) {
        this.txtPeriodDifference_Years = txtPeriodDifference_Years;
    }
    
    /**
     * Getter for property cbmRepayFreq_ADVANCE.
     * @return Value of property cbmRepayFreq_ADVANCE.
     */
    public com.see.truetransact.clientutil.ComboBoxModel getCbmRepayFreq_ADVANCE() {
        return cbmRepayFreq_ADVANCE;
    }
    
    /**
     * Setter for property cbmRepayFreq_ADVANCE.
     * @param cbmRepayFreq_PROD New value of property cbmRepayFreq_ADVANCE.
     */
    public void setCbmRepayFreq_ADVANCE(com.see.truetransact.clientutil.ComboBoxModel cbmRepayFreq_ADVANCE) {
        this.cbmRepayFreq_ADVANCE = cbmRepayFreq_ADVANCE;
    }
    
    /**
     * Getter for property cbmRepayFreq_LOAN.
     * @return Value of property cbmRepayFreq_LOAN.
     */
    public com.see.truetransact.clientutil.ComboBoxModel getCbmRepayFreq_LOAN() {
        return cbmRepayFreq_LOAN;
    }
    
    /**
     * Setter for property cbmRepayFreq_LOAN.
     * @param cbmRepayFreq_LOAN New value of property cbmRepayFreq_LOAN.
     */
    public void setCbmRepayFreq_LOAN(com.see.truetransact.clientutil.ComboBoxModel cbmRepayFreq_LOAN) {
        this.cbmRepayFreq_LOAN = cbmRepayFreq_LOAN;
    }
    
    /**
     * Getter for property authorizeMap.
     * @return Value of property authorizeMap.
     */
    public java.util.HashMap getAuthorizeMap() {
        return authorizeMap;
    }
    
    /**
     * Setter for property authorizeMap.
     * @param authorizeMap New value of property authorizeMap.
     */
    public void setAuthorizeMap(java.util.HashMap authorizeMap) {
        this.authorizeMap = authorizeMap;
    }
    
    /**
     * Getter for property txtAcct_Name.
     * @return Value of property txtAcct_Name.
     */
    public java.lang.String getTxtAcct_Name() {
        return txtAcct_Name;
    }
    
    /**
     * Setter for property txtAcct_Name.
     * @param txtAcct_Name New value of property txtAcct_Name.
     */
    public void setTxtAcct_Name(java.lang.String txtAcct_Name) {
        this.txtAcct_Name = txtAcct_Name;
    }
    
    /**
     * Getter for property loanType.
     * @return Value of property loanType.
     */
    public java.lang.String getLoanType() {
        return loanType;
    }
    
    /**
     * Setter for property loanType.
     * @param loanType New value of property loanType.
     */
    public void setLoanType(java.lang.String loanType) {
        this.loanType = loanType;
    }
    
    /**
     * Getter for property lienChanged.
     * @return Value of property lienChanged.
     */
    public boolean isLienChanged() {
        return lienChanged;
    }
    
    /**
     * Setter for property lienChanged.
     * @param lienChanged New value of property lienChanged.
     */
    public void setLienChanged(boolean lienChanged) {
        this.lienChanged = lienChanged;
    }
    
    /**
     * Getter for property loanACNo.
     * @return Value of property loanACNo.
     */
    public java.lang.String getLoanACNo() {
        return loanACNo;
    }
    
    /**
     * Setter for property loanACNo.
     * @param loanACNo New value of property loanACNo.
     */
    public void setLoanACNo(java.lang.String loanACNo) {
        this.loanACNo = loanACNo;
    }
    
    /**
     * Getter for property depositCustDetMap.
     * @return Value of property depositCustDetMap.
     */
    public java.util.HashMap getDepositCustDetMap() {
        return depositCustDetMap;
    }
    
    /**
     * Setter for property depositCustDetMap.
     * @param depositCustDetMap New value of property depositCustDetMap.
     */
    public void setDepositCustDetMap(java.util.HashMap depositCustDetMap) {
        this.depositCustDetMap = depositCustDetMap;
    }
    
    /**
     * Getter for property depositNo.
     * @return Value of property depositNo.
     */
    public java.lang.String getDepositNo() {
        return depositNo;
    }
    
    /**
     * Setter for property depositNo.
     * @param depositNo New value of property depositNo.
     */
    public void setDepositNo(java.lang.String depositNo) {
        this.depositNo = depositNo;
        termLoanInterestOB.setDepositNo("");
        termLoanInterestOB.setDepositNo(depositNo);
        
    }
    
    /**
     * Getter for property deleteAction.
     * @return Value of property deleteAction.
     */
    public java.lang.String getDeleteAction() {
        return deleteAction;
    }
    
    /**
     * Setter for property deleteAction.
     * @param deleteAction New value of property deleteAction.
     */
    public void setDeleteAction(java.lang.String deleteAction) {
        this.deleteAction = deleteAction;
    }
    
    /**
     * Getter for property ProductFacilityType.
     * @return Value of property ProductFacilityType.
     */
    public java.lang.String getProductFacilityType() {
        return ProductFacilityType;
    }
    
    /**
     * Setter for property ProductFacilityType.
     * @param ProductFacilityType New value of property ProductFacilityType.
     */
    public void setProductFacilityType(java.lang.String ProductFacilityType) {
        this.ProductFacilityType = ProductFacilityType;
    }
    
    /**
     * Getter for property cbmRecommendedByType.
     * @return Value of property cbmRecommendedByType.
     */
    public com.see.truetransact.clientutil.ComboBoxModel getCbmRecommendedByType() {
        return cbmRecommendedByType;
    }
    
    /**
     * Setter for property cbmRecommendedByType.
     * @param cbmRecommendedByType New value of property cbmRecommendedByType.
     */
    public void setCbmRecommendedByType(com.see.truetransact.clientutil.ComboBoxModel cbmRecommendedByType) {
        this.cbmRecommendedByType = cbmRecommendedByType;
    }
    
    /**
     * Getter for property cboRecommendedByType.
     * @return Value of property cboRecommendedByType.
     */
    public java.lang.String getCboRecommendedByType() {
        return cboRecommendedByType;
    }
    
    /**
     * Setter for property cboRecommendedByType.
     * @param cboRecommendedByType New value of property cboRecommendedByType.
     */
    public void setCboRecommendedByType(java.lang.String cboRecommendedByType) {
        this.cboRecommendedByType = cboRecommendedByType;
    }
    
    /**
     * Getter for property AccountOpenDate.
     * @return Value of property AccountOpenDate.
     */
    public java.lang.String getAccountOpenDate() {
        return AccountOpenDate;
    }
    
    /**
     * Setter for property AccountOpenDate.
     * @param AccountOpenDate New value of property AccountOpenDate.
     */
    public void setAccountOpenDate(java.lang.String AccountOpenDate) {
        this.AccountOpenDate = AccountOpenDate;
    }
    
    /**
     * Getter for property tblShare.
     * @return Value of property tblShare.
     */
    public com.see.truetransact.clientutil.EnhancedTableModel getTblShare() {
        return tblShare;
    }
    
    /**
     * Setter for property tblShare.
     * @param tblShare New value of property tblShare.
     */
    public void setTblShare(com.see.truetransact.clientutil.EnhancedTableModel tblShare) {
        this.tblShare = tblShare;
        setChanged();
    }
    
    /**
     * Getter for property BEHAVES_LIKE.
     * @return Value of property BEHAVES_LIKE.
     */
    public java.lang.String getBEHAVES_LIKE() {
        return BEHAVES_LIKE;
    }
    
    /**
     * Setter for property BEHAVES_LIKE.
     * @param BEHAVES_LIKE New value of property BEHAVES_LIKE.
     */
    public void setBEHAVES_LIKE(java.lang.String BEHAVES_LIKE) {
        this.BEHAVES_LIKE = BEHAVES_LIKE;
    }
    
    /**
     * Getter for property renewAvailableBalance.
     * @return Value of property renewAvailableBalance.
     */
    public java.lang.String getRenewAvailableBalance() {
        return renewAvailableBalance;
    }
    
    /**
     * Setter for property renewAvailableBalance.
     * @param renewAvailableBalance New value of property renewAvailableBalance.
     */
    public void setRenewAvailableBalance(java.lang.String renewAvailableBalance) {
        this.renewAvailableBalance = renewAvailableBalance;
    }
    
    /**
     * Getter for property updateAvailableBalance.
     * @return Value of property updateAvailableBalance.
     */
    public boolean isUpdateAvailableBalance() {
        return updateAvailableBalance;
    }
    
    /**
     * Setter for property updateAvailableBalance.
     * @param updateAvailableBalance New value of property updateAvailableBalance.
     */
    public void setUpdateAvailableBalance(boolean updateAvailableBalance) {
        this.updateAvailableBalance = updateAvailableBalance;
    }
    
    /**
     * Getter for property advanceLiablityMap.
     * @return Value of property advanceLiablityMap.
     */
    public java.util.HashMap getAdvanceLiablityMap() {
        return advanceLiablityMap;
    }
    
    /**
     * Setter for property advanceLiablityMap.
     * @param advanceLiablityMap New value of property advanceLiablityMap.
     */
    public void setAdvanceLiablityMap(java.util.HashMap advanceLiablityMap) {
        this.advanceLiablityMap = advanceLiablityMap;
    }
    
    /**
     * Getter for property oldSanction_no.
     * @return Value of property oldSanction_no.
     */
    public java.lang.String getOldSanction_no() {
        return oldSanction_no;
    }
    
    /**
     * Setter for property oldSanction_no.
     * @param oldSanction_no New value of property oldSanction_no.
     */
    public void setOldSanction_no(java.lang.String oldSanction_no) {
        this.oldSanction_no = oldSanction_no;
    }
    
    /**
     * Getter for property lblDepositNo.
     * @return Value of property lblDepositNo.
     */
    public java.lang.String getLblDepositNo() {
        return lblDepositNo;
    }
    
    /**
     * Setter for property lblDepositNo.
     * @param lblDepositNo New value of property lblDepositNo.
     */
    public void setLblDepositNo(java.lang.String lblDepositNo) {
        this.lblDepositNo = lblDepositNo;
    }
    
    /**
     * Getter for property eligibleMargin.
     * @return Value of property eligibleMargin.
     */
    public double getEligibleMargin() {
        return eligibleMargin;
    }
    
    /**
     * Setter for property eligibleMargin.
     * @param eligibleMargin New value of property eligibleMargin.
     */
    public void setEligibleMargin(double eligibleMargin) {
        this.eligibleMargin = eligibleMargin;
    }
    
    /**
     * Getter for property sanctionAmtRoundOff.
     * @return Value of property sanctionAmtRoundOff.
     */
    public java.lang.String getSanctionAmtRoundOff() {
        return sanctionAmtRoundOff;
    }
    
    /**
     * Setter for property sanctionAmtRoundOff.
     * @param sanctionAmtRoundOff New value of property sanctionAmtRoundOff.
     */
    public void setSanctionAmtRoundOff(java.lang.String sanctionAmtRoundOff) {
        this.sanctionAmtRoundOff = sanctionAmtRoundOff;
    }
    
    /**
     * Getter for property shadowDebit.
     * @return Value of property shadowDebit.
     */
    public double getShadowDebit() {
        return shadowDebit;
    }
    
    /**
     * Setter for property shadowDebit.
     * @param shadowDebit New value of property shadowDebit.
     */
    public void setShadowDebit(double shadowDebit) {
        this.shadowDebit = shadowDebit;
    }
    
    /**
     * Getter for property shadowCredit.
     * @return Value of property shadowCredit.
     */
    public double getShadowCredit() {
        return shadowCredit;
    }
    
    /**
     * Setter for property shadowCredit.
     * @param shadowCredit New value of property shadowCredit.
     */
    public void setShadowCredit(double shadowCredit) {
        this.shadowCredit = shadowCredit;
    }
    
    /**
     * Getter for property clearBalance.
     * @return Value of property clearBalance.
     */
    public double getClearBalance() {
        return clearBalance;
    }
    
    /**
     * Setter for property clearBalance.
     * @param clearBalance New value of property clearBalance.
     */
    public void setClearBalance(double clearBalance) {
        this.clearBalance = clearBalance;
    }
    
    /**
     * Getter for property partReject.
     * @return Value of property partReject.
     */
    public java.lang.String getPartReject() {
        return partReject;
    }
    
    /**
     * Setter for property partReject.
     * @param partReject New value of property partReject.
     */
    public void setPartReject(java.lang.String partReject) {
        this.partReject = partReject;
    }
    
    /**
     * Getter for property rdoDP__NO.
     * @return Value of property rdoDP__NO.
     */
    public boolean isRdoDP__NO() {
        return rdoDP__NO;
    }
    
    /**
     * Setter for property rdoDP__NO.
     * @param rdoDP__NO New value of property rdoDP__NO.
     */
    public void setRdoDP__NO(boolean rdoDP__NO) {
        this.rdoDP__NO = rdoDP__NO;
    }
    
    /**
     * Getter for property rdoDP_YES.
     * @return Value of property rdoDP_YES.
     */
    public boolean isRdoDP_YES() {
        return rdoDP_YES;
    }
    
    /**
     * Setter for property rdoDP_YES.
     * @param rdoDP_YES New value of property rdoDP_YES.
     */
    public void setRdoDP_YES(boolean rdoDP_YES) {
        this.rdoDP_YES = rdoDP_YES;
    }
    
    /**
     * Getter for property chkDocDetails.
     * @return Value of property chkDocDetails.
     */
    public boolean isChkDocDetails() {
        return chkDocDetails;
    }
    
    /**
     * Setter for property chkDocDetails.
     * @param chkDocDetails New value of property chkDocDetails.
     */
    public void setChkDocDetails(boolean chkDocDetails) {
        this.chkDocDetails = chkDocDetails;
    }
    
    /**
     * Getter for property chkAuthorizedSignatory.
     * @return Value of property chkAuthorizedSignatory.
     */
    public boolean isChkAuthorizedSignatory() {
        return chkAuthorizedSignatory;
    }
    
    /**
     * Setter for property chkAuthorizedSignatory.
     * @param chkAuthorizedSignatory New value of property chkAuthorizedSignatory.
     */
    public void setChkAuthorizedSignatory(boolean chkAuthorizedSignatory) {
        this.chkAuthorizedSignatory = chkAuthorizedSignatory;
    }
    
    /**
     * Getter for property chkPOFAttorney.
     * @return Value of property chkPOFAttorney.
     */
    public boolean isChkPOFAttorney() {
        return chkPOFAttorney;
    }
    
    /**
     * Setter for property chkPOFAttorney.
     * @param chkPOFAttorney New value of property chkPOFAttorney.
     */
    public void setChkPOFAttorney(boolean chkPOFAttorney) {
        this.chkPOFAttorney = chkPOFAttorney;
    }
    
    public int getAuthorizeSigantoryRecord(){
        return termLoanAuthorizedSignatoryOB.getTblAuthorized().getDataArrayList().size();
    }
    public int getPOARecord(){
        return termLoanPoAOB.getTblPoA().getDataArrayList().size();
    }
    
    public int getActTransferMadatoryCheck(){
        return termLoanPoAOB.getTblPoA().getDataArrayList().size();
    }
    /**
     * Getter for property last_int_calc_dt.
     * @return Value of property last_int_calc_dt.
     */
    public java.util.Date getLast_int_calc_dt() {
        return last_int_calc_dt;
    }
    
    /**
     * Setter for property last_int_calc_dt.
     * @param last_int_calc_dt New value of property last_int_calc_dt.
     */
    public void setLast_int_calc_dt(java.util.Date last_int_calc_dt) {
        this.last_int_calc_dt = last_int_calc_dt;
    }
    
    /**
     * Getter for property accountCloseDate.
     * @return Value of property accountCloseDate.
     */
    public java.lang.String getAccountCloseDate() {
        return accountCloseDate;
    }
    
    /**
     * Setter for property accountCloseDate.
     * @param accountCloseDate New value of property accountCloseDate.
     */
    public void setAccountCloseDate(java.lang.String accountCloseDate) {
        this.accountCloseDate = accountCloseDate;
    }
    
     /**
     * Getter for property txtLimit_SDMoneyDeposit.
     * @return Value of property txtLimit_SDMoneyDeposit.
     */
    public java.lang.String getTxtLimit_SDMoneyDeposit() {
        return txtLimit_SDMoneyDeposit;
    }    
    
    /**
     * Setter for property txtLimit_SDMoneyDeposit.
     * @param txtLimit_SDMoneyDeposit New value of property txtLimit_SDMoneyDeposit.
     */
    public void setTxtLimit_SDMoneyDeposit(java.lang.String txtLimit_SDMoneyDeposit) {
        this.txtLimit_SDMoneyDeposit = txtLimit_SDMoneyDeposit;
    }    
    
    /**
     * Getter for property chkAcctTransfer.
     * @return Value of property chkAcctTransfer.
     */
    public boolean getChkAcctTransfer() {
        return chkAcctTransfer;
    }
    
    /**
     * Setter for property chkAcctTransfer.
     * @param chkAcctTransfer New value of property chkAcctTransfer.
     */
    public void setChkAcctTransfer(boolean chkAcctTransfer) {
        this.chkAcctTransfer = chkAcctTransfer;
    }
    
    /**
     * Getter for property availableBalance.
     * @return Value of property availableBalance.
     */
    public double getAvailableBalance() {
        return availableBalance;
    }
    
    /**
     * Setter for property availableBalance.
     * @param availableBalance New value of property availableBalance.
     */
    public void setAvailableBalance(double availableBalance) {
        this.availableBalance = availableBalance;
    }
    
    /**
     * Getter for property cboCaseStatus.
     * @return Value of property cboCaseStatus.
     */
    public java.lang.String getCboCaseStatus() {
        return cboCaseStatus;
    }
    
    /**
     * Setter for property cboCaseStatus.
     * @param cboCaseStatus New value of property cboCaseStatus.
     */
    public void setCboCaseStatus(java.lang.String cboCaseStatus) {
        this.cboCaseStatus = cboCaseStatus;
    }
    

    
//    public void setCbmCaseStatus(ComboBoxModel cbmCaseStatus){
//        this.cbmCaseStatus = cbmCaseStatus;
//        setChanged();
//    }
//    
//    ComboBoxModel getCbmCaseStatus(){
//        return cbmCaseStatus;
//    }
    
     
    
    
    
    
    /**
     * Getter for property txtCaseNumber.
     * @return Value of property txtCaseNumber.
     */
    public java.lang.String getTxtCaseNumber() {
        return txtCaseNumber;
    }
    
    /**
     * Setter for property txtCaseNumber.
     * @param txtCaseNumber New value of property txtCaseNumber.
     */
    public void setTxtCaseNumber(java.lang.String txtCaseNumber) {
        this.txtCaseNumber = txtCaseNumber;
    }
    
    /**
     * Getter for property tdtlFillingDt.
     * @return Value of property tdtlFillingDt.
     */
    public java.lang.String getTdtlFillingDt() {
        return tdtlFillingDt;
    }
    
    /**
     * Setter for property tdtlFillingDt.
     * @param tdtlFillingDt New value of property tdtlFillingDt.
     */
    public void setTdtlFillingDt(java.lang.String tdtlFillingDt) {
        this.tdtlFillingDt = tdtlFillingDt;
    }
    
    /**
     * Getter for property txtFillingFees.
     * @return Value of property txtFillingFees.
     */
    public java.lang.String getTxtFillingFees() {
        return txtFillingFees;
    }
    
    /**
     * Setter for property txtFillingFees.
     * @param txtFillingFees New value of property txtFillingFees.
     */
    public void setTxtFillingFees(java.lang.String txtFillingFees) {
        this.txtFillingFees = txtFillingFees;
    }
    
    /**
     * Getter for property txtMiscCharges.
     * @return Value of property txtMiscCharges.
     */
    public java.lang.String getTxtMiscCharges() {
        return txtMiscCharges;
    }
    
    /**
     * Setter for property txtMiscCharges.
     * @param txtMiscCharges New value of property txtMiscCharges.
     */
    public void setTxtMiscCharges(java.lang.String txtMiscCharges) {
        this.txtMiscCharges = txtMiscCharges;
    }
    
    /**
     * Getter for property tblCaseDetails.
     * @return Value of property tblCaseDetails.
     */
    public com.see.truetransact.clientutil.EnhancedTableModel getTblCaseDetails() {
        return tblCaseDetails;
    }
    
    /**
     * Setter for property tblCaseDetails.
     * @param tblCaseDetails New value of property tblCaseDetails.
     */
    public void setTblCaseDetails(com.see.truetransact.clientutil.EnhancedTableModel tblCaseDetails) {
        this.tblCaseDetails = tblCaseDetails;
    }
    
    /**
     * Getter for property cbmCaseStatus.
     * @return Value of property cbmCaseStatus.
     */
    public com.see.truetransact.clientutil.ComboBoxModel getCbmCaseStatus() {
        return cbmCaseStatus;
    }
    
    /**
     * Setter for property cbmCaseStatus.
     * @param cbmCaseStatus New value of property cbmCaseStatus.
     */
    public void setCbmCaseStatus(com.see.truetransact.clientutil.ComboBoxModel cbmCaseStatus) {
        this.cbmCaseStatus = cbmCaseStatus;
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
     * Getter for property productCategory.
     * @return Value of property productCategory.
     */
    public java.lang.String getProductCategory() {
        return productCategory;
    }
    
    /**
     * Setter for property productCategory.
     * @param productCategory New value of property productCategory.
     */
    public void setProductCategory(java.lang.String productCategory) {
        this.productCategory = productCategory;
    }
    
    /**
     * Getter for property keyValueForPaddyAndMDSLoans.
     * @return Value of property keyValueForPaddyAndMDSLoans.
     */
    public java.lang.String getKeyValueForPaddyAndMDSLoans() {
        return keyValueForPaddyAndMDSLoans;
    }
    
    /**
     * Setter for property keyValueForPaddyAndMDSLoans.
     * @param keyValueForPaddyAndMDSLoans New value of property keyValueForPaddyAndMDSLoans.
     */
    public void setKeyValueForPaddyAndMDSLoans(java.lang.String keyValueForPaddyAndMDSLoans) {
        this.keyValueForPaddyAndMDSLoans = keyValueForPaddyAndMDSLoans;
    }
    
    /**
     * Getter for property paddyMap.
     * @return Value of property paddyMap.
     */
    public Map getPaddyMap() {
        return paddyMap;
    }
    
    /**
     * Setter for property paddyMap.
     * @param paddyMap New value of property paddyMap.
     */
    public void setPaddyMap(Map paddyMap) {
        this.paddyMap = paddyMap;
    }
    
    /**
     * Getter for property mdsMap.
     * @return Value of property mdsMap.
     */
    public java.util.Map getMdsMap() {
        return mdsMap;
    }
    
    /**
     * Setter for property mdsMap.
     * @param mdsMap New value of property mdsMap.
     */
    public void setMdsMap(java.util.Map mdsMap) {
        this.mdsMap = mdsMap;
    }
    
    /**
     * Getter for property newTransactionMap.
     * @return Value of property newTransactionMap.
     */
    public java.util.HashMap getNewTransactionMap() {
        return newTransactionMap;
    }
    
    /**
     * Setter for property newTransactionMap.
     * @param newTransactionMap New value of property newTransactionMap.
     */
    public void setNewTransactionMap(java.util.HashMap newTransactionMap) {
        this.newTransactionMap = newTransactionMap;
    }
    
    /**
     * Getter for property memberTypeData.
     * @return Value of property memberTypeData.
     */
    public boolean isMemberTypeData() {
        return memberTypeData;
    }
    
    /**
     * Setter for property memberTypeData.
     * @param memberTypeData New value of property memberTypeData.
     */
    public void setMemberTypeData(boolean memberTypeData) {
        this.memberTypeData = memberTypeData;
    }
    
    /**
     * Getter for property txtMemNo.
     * @return Value of property txtMemNo.
     */
    public java.lang.String getTxtMemNo() {
        return txtMemNo;
    }
    
    /**
     * Setter for property txtMemNo.
     * @param txtMemNo New value of property txtMemNo.
     */
    public void setTxtMemNo(java.lang.String txtMemNo) {
        this.txtMemNo = txtMemNo;
    }
    
    /**
     * Getter for property txtMemName.
     * @return Value of property txtMemName.
     */
    public java.lang.String getTxtMemName() {
        return txtMemName;
    }
    
    /**
     * Setter for property txtMemName.
     * @param txtMemName New value of property txtMemName.
     */
    public void setTxtMemName(java.lang.String txtMemName) {
        this.txtMemName = txtMemName;
    }
    
    /**
     * Getter for property txtMemNetworth.
     * @return Value of property txtMemNetworth.
     */
    public java.lang.String getTxtMemNetworth() {
        return txtMemNetworth;
    }
    
    /**
     * Setter for property txtMemNetworth.
     * @param txtMemNetworth New value of property txtMemNetworth.
     */
    public void setTxtMemNetworth(java.lang.String txtMemNetworth) {
        this.txtMemNetworth = txtMemNetworth;
    }
    
    /**
     * Getter for property txtContactNum.
     * @return Value of property txtContactNum.
     */
    public java.lang.String getTxtContactNum() {
        return txtContactNum;
    }
    
    /**
     * Setter for property txtContactNum.
     * @param txtContactNum New value of property txtContactNum.
     */
    public void setTxtContactNum(java.lang.String txtContactNum) {
        this.txtContactNum = txtContactNum;
    }
    
    /**
     * Getter for property txtMemType.
     * @return Value of property txtMemType.
     */
    public java.lang.String getTxtMemType() {
        return txtMemType;
    }
    
    /**
     * Setter for property txtMemType.
     * @param txtMemType New value of property txtMemType.
     */
    public void setTxtMemType(java.lang.String txtMemType) {
        this.txtMemType = txtMemType;
    }
    
    /**
     * Getter for property tblMemberTypeDetails.
     * @return Value of property tblMemberTypeDetails.
     */
    public com.see.truetransact.clientutil.EnhancedTableModel getTblMemberTypeDetails() {
        return tblMemberTypeDetails;
    }
    
    /**
     * Setter for property tblMemberTypeDetails.
     * @param tblMemberTypeDetails New value of property tblMemberTypeDetails.
     */
    public void setTblMemberTypeDetails(com.see.truetransact.clientutil.EnhancedTableModel tblMemberTypeDetails) {
        this.tblMemberTypeDetails = tblMemberTypeDetails;
    }
    
    /**
     * Getter for property txtSalaryCertificateNo.
     * @return Value of property txtSalaryCertificateNo.
     */
    public java.lang.String getTxtSalaryCertificateNo() {
        return txtSalaryCertificateNo;
    }
    
    /**
     * Setter for property txtSalaryCertificateNo.
     * @param txtSalaryCertificateNo New value of property txtSalaryCertificateNo.
     */
    public void setTxtSalaryCertificateNo(java.lang.String txtSalaryCertificateNo) {
        this.txtSalaryCertificateNo = txtSalaryCertificateNo;
        setChanged();
    }
    
    /**
     * Getter for property txtEmployerName.
     * @return Value of property txtEmployerName.
     */
    public java.lang.String getTxtEmployerName() {
        return txtEmployerName;
    }
    
    /**
     * Setter for property txtEmployerName.
     * @param txtEmployerName New value of property txtEmployerName.
     */
    public void setTxtEmployerName(java.lang.String txtEmployerName) {
        this.txtEmployerName = txtEmployerName;
        setChanged();
    }
    
    /**
     * Getter for property txtAddress.
     * @return Value of property txtAddress.
     */
    public java.lang.String getTxtAddress() {
        return txtAddress;
    }
    
    /**
     * Setter for property txtAddress.
     * @param txtAddress New value of property txtAddress.
     */
    public void setTxtAddress(java.lang.String txtAddress) {
        this.txtAddress = txtAddress;
        setChanged();
    }
    
    /**
     * Getter for property cboSecurityCity.
     * @return Value of property cboSecurityCity.
     */
    public java.lang.String getCboSecurityCity() {
        return cboSecurityCity;
    }
    
    /**
     * Setter for property cboSecurityCity.
     * @param cboSecurityCity New value of property cboSecurityCity.
     */
    public void setCboSecurityCity(java.lang.String cboSecurityCity) {
        this.cboSecurityCity = cboSecurityCity;
    }
    
    /**
     * Getter for property txtPinCode.
     * @return Value of property txtPinCode.
     */
    public java.lang.String getTxtPinCode() {
        return txtPinCode;
    }
    
    /**
     * Setter for property txtPinCode.
     * @param txtPinCode New value of property txtPinCode.
     */
    public void setTxtPinCode(java.lang.String txtPinCode) {
        this.txtPinCode = txtPinCode;
        setChanged();
    }
    
    /**
     * Getter for property txtDesignation.
     * @return Value of property txtDesignation.
     */
    public java.lang.String getTxtDesignation() {
        return txtDesignation;
    }
    
    /**
     * Setter for property txtDesignation.
     * @param txtDesignation New value of property txtDesignation.
     */
    public void setTxtDesignation(java.lang.String txtDesignation) {
        this.txtDesignation = txtDesignation;
    }
    
    /**
     * Getter for property txtContactNo.
     * @return Value of property txtContactNo.
     */
    public java.lang.String getTxtContactNo() {
        return txtContactNo;
    }
    
    /**
     * Setter for property txtContactNo.
     * @param txtContactNo New value of property txtContactNo.
     */
    public void setTxtContactNo(java.lang.String txtContactNo) {
        this.txtContactNo = txtContactNo;
    }
    
    /**
     * Getter for property tdtRetirementDt.
     * @return Value of property tdtRetirementDt.
     */
    public java.lang.String getTdtRetirementDt() {
        return tdtRetirementDt;
    }
    
    /**
     * Setter for property tdtRetirementDt.
     * @param tdtRetirementDt New value of property tdtRetirementDt.
     */
    public void setTdtRetirementDt(java.lang.String tdtRetirementDt) {
        this.tdtRetirementDt = tdtRetirementDt;
    }
    
    /**
     * Getter for property txtMemberNum.
     * @return Value of property txtMemberNum.
     */
    public java.lang.String getTxtMemberNum() {
        return txtMemberNum;
    }
    
    /**
     * Setter for property txtMemberNum.
     * @param txtMemberNum New value of property txtMemberNum.
     */
    public void setTxtMemberNum(java.lang.String txtMemberNum) {
        this.txtMemberNum = txtMemberNum;
    }
    
    /**
     * Getter for property txtTotalSalary.
     * @return Value of property txtTotalSalary.
     */
    public java.lang.String getTxtTotalSalary() {
        return txtTotalSalary;
    }
    
    /**
     * Setter for property txtTotalSalary.
     * @param txtTotalSalary New value of property txtTotalSalary.
     */
    public void setTxtTotalSalary(java.lang.String txtTotalSalary) {
        this.txtTotalSalary = txtTotalSalary;
    }
    
    /**
     * Getter for property txtNetWorth.
     * @return Value of property txtNetWorth.
     */
    public java.lang.String getTxtNetWorth() {
        return txtNetWorth;
    }
    
    /**
     * Setter for property txtNetWorth.
     * @param txtNetWorth New value of property txtNetWorth.
     */
    public void setTxtNetWorth(java.lang.String txtNetWorth) {
        this.txtNetWorth = txtNetWorth;
    }
    
    /**
     * Getter for property txtSalaryRemark.
     * @return Value of property txtSalaryRemark.
     */
    public java.lang.String getTxtSalaryRemark() {
        return txtSalaryRemark;
    }
    
    /**
     * Setter for property txtSalaryRemark.
     * @param txtSalaryRemark New value of property txtSalaryRemark.
     */
    public void setTxtSalaryRemark(java.lang.String txtSalaryRemark) {
        this.txtSalaryRemark = txtSalaryRemark;
    }
    
    /**
     * Getter for property cbmSecurityCity.
     * @return Value of property cbmSecurityCity.
     */
    public com.see.truetransact.clientutil.ComboBoxModel getCbmSecurityCity() {
        return cbmSecurityCity;
    }
    
    /**
     * Setter for property cbmSecurityCity.
     * @param cbmSecurityCity New value of property cbmSecurityCity.
     */
    public void setCbmSecurityCity(com.see.truetransact.clientutil.ComboBoxModel cbmSecurityCity) {
        this.cbmSecurityCity = cbmSecurityCity;
    }
    
    /**
     * Getter for property txtOwnerMemNo.
     * @return Value of property txtOwnerMemNo.
     */
    public java.lang.String getTxtOwnerMemNo() {
        return txtOwnerMemNo;
    }
    
    /**
     * Setter for property txtOwnerMemNo.
     * @param txtOwnerMemNo New value of property txtOwnerMemNo.
     */
    public void setTxtOwnerMemNo(java.lang.String txtOwnerMemNo) {
        this.txtOwnerMemNo = txtOwnerMemNo;
    }
    
    /**
     * Getter for property txtOwnerMemberNname.
     * @return Value of property txtOwnerMemberNname.
     */
    public java.lang.String getTxtOwnerMemberNname() {
        return txtOwnerMemberNname;
    }
    
    /**
     * Setter for property txtOwnerMemberNname.
     * @param txtOwnerMemberNname New value of property txtOwnerMemberNname.
     */
    public void setTxtOwnerMemberNname(java.lang.String txtOwnerMemberNname) {
        this.txtOwnerMemberNname = txtOwnerMemberNname;
    }
    
    /**
     * Getter for property txtDocumentNo.
     * @return Value of property txtDocumentNo.
     */
    public java.lang.String getTxtDocumentNo() {
        return txtDocumentNo;
    }
    
    /**
     * Setter for property txtDocumentNo.
     * @param txtDocumentNo New value of property txtDocumentNo.
     */
    public void setTxtDocumentNo(java.lang.String txtDocumentNo) {
        this.txtDocumentNo = txtDocumentNo;
    }
    
    /**
     * Getter for property txtDocumentType.
     * @return Value of property txtDocumentType.
     */
    public java.lang.String getTxtDocumentType() {
        return txtDocumentType;
    }
    
    /**
     * Setter for property txtDocumentType.
     * @param txtDocumentType New value of property txtDocumentType.
     */
    public void setTxtDocumentType(java.lang.String txtDocumentType) {
        this.txtDocumentType = txtDocumentType;
    }
    
    /**
     * Getter for property tdtDocumentDate.
     * @return Value of property tdtDocumentDate.
     */
    public java.lang.String getTdtDocumentDate() {
        return tdtDocumentDate;
    }
    
    /**
     * Setter for property tdtDocumentDate.
     * @param tdtDocumentDate New value of property tdtDocumentDate.
     */
    public void setTdtDocumentDate(java.lang.String tdtDocumentDate) {
        this.tdtDocumentDate = tdtDocumentDate;
    }
    
    /**
     * Getter for property txtRegisteredOffice.
     * @return Value of property txtRegisteredOffice.
     */
    public java.lang.String getTxtRegisteredOffice() {
        return txtRegisteredOffice;
    }
    
    /**
     * Setter for property txtRegisteredOffice.
     * @param txtRegisteredOffice New value of property txtRegisteredOffice.
     */
    public void setTxtRegisteredOffice(java.lang.String txtRegisteredOffice) {
        this.txtRegisteredOffice = txtRegisteredOffice;
    }
    
        
    /**
     * Getter for property tdtPledgeDate.
     * @return Value of property tdtPledgeDate.
     */
    public java.lang.String getTdtPledgeDate() {
        return tdtPledgeDate;
    }
    
    /**
     * Setter for property tdtPledgeDate.
     * @param tdtPledgeDate New value of property tdtPledgeDate.
     */
    public void setTdtPledgeDate(java.lang.String tdtPledgeDate) {
        this.tdtPledgeDate = tdtPledgeDate;
    }
    
    /**
     * Getter for property txtPledgeNo.
     * @return Value of property txtPledgeNo.
     */
    public java.lang.String getTxtPledgeNo() {
        return txtPledgeNo;
    }
    
    /**
     * Setter for property txtPledgeNo.
     * @param txtPledgeNo New value of property txtPledgeNo.
     */
    public void setTxtPledgeNo(java.lang.String txtPledgeNo) {
        this.txtPledgeNo = txtPledgeNo;
    }
    
    /**
     * Getter for property txtPledgeAmount.
     * @return Value of property txtPledgeAmount.
     */
    public java.lang.String getTxtPledgeAmount() {
        return txtPledgeAmount;
    }
    
    /**
     * Setter for property txtPledgeAmount.
     * @param txtPledgeAmount New value of property txtPledgeAmount.
     */
    public void setTxtPledgeAmount(java.lang.String txtPledgeAmount) {
        this.txtPledgeAmount = txtPledgeAmount;
    }
    
    /**
     * Getter for property txtVillage.
     * @return Value of property txtVillage.
     */
    public java.lang.String getTxtVillage() {
        return txtVillage;
    }
    
    /**
     * Setter for property txtVillage.
     * @param txtVillage New value of property txtVillage.
     */
    public void setTxtVillage(java.lang.String txtVillage) {
        this.txtVillage = txtVillage;
    }
    
    /**
     * Getter for property txtSurveyNo.
     * @return Value of property txtSurveyNo.
     */
    public java.lang.String getTxtSurveyNo() {
        return txtSurveyNo;
    }
    
    /**
     * Setter for property txtSurveyNo.
     * @param txtSurveyNo New value of property txtSurveyNo.
     */
    public void setTxtSurveyNo(java.lang.String txtSurveyNo) {
        this.txtSurveyNo = txtSurveyNo;
    }
    
    /**
     * Getter for property txtTotalArea.
     * @return Value of property txtTotalArea.
     */
    public java.lang.String getTxtTotalArea() {
        return txtTotalArea;
    }
    
    /**
     * Setter for property txtTotalArea.
     * @param txtTotalArea New value of property txtTotalArea.
     */
    public void setTxtTotalArea(java.lang.String txtTotalArea) {
        this.txtTotalArea = txtTotalArea;
    }
    
    /**
     * Getter for property cboNature.
     * @return Value of property cboNature.
     */
    public java.lang.String getCboNature() {
        return cboNature;
    }
    
    /**
     * Setter for property cboNature.
     * @param cboNature New value of property cboNature.
     */
    public void setCboNature(java.lang.String cboNature) {
        this.cboNature = cboNature;
    }
    
    /**
     * Getter for property txtAreaParticular.
     * @return Value of property txtAreaParticular.
     */
    public java.lang.String getTxtAreaParticular() {
        return txtAreaParticular;
    }
    
    /**
     * Setter for property txtAreaParticular.
     * @param txtAreaParticular New value of property txtAreaParticular.
     */
    public void setTxtAreaParticular(java.lang.String txtAreaParticular) {
        this.txtAreaParticular = txtAreaParticular;
    }
    
    /**
     * Getter for property collateralTypeData.
     * @return Value of property collateralTypeData.
     */
    public boolean isCollateralTypeData() {
        return collateralTypeData;
    }
    
    /**
     * Setter for property collateralTypeData.
     * @param collateralTypeData New value of property collateralTypeData.
     */
    public void setCollateralTypeData(boolean collateralTypeData) {
        this.collateralTypeData = collateralTypeData;
    }
    
    /**
     * Getter for property tblCollateralDetails.
     * @return Value of property tblCollateralDetails.
     */
    public com.see.truetransact.clientutil.EnhancedTableModel getTblCollateralDetails() {
        return tblCollateralDetails;
    }
    
    /**
     * Setter for property tblCollateralDetails.
     * @param tblCollateralDetails New value of property tblCollateralDetails.
     */
    public void setTblCollateralDetails(com.see.truetransact.clientutil.EnhancedTableModel tblCollateralDetails) {
        this.tblCollateralDetails = tblCollateralDetails;
    }
    
    /**
     * Getter for property cbmNature.
     * @return Value of property cbmNature.
     */
    public com.see.truetransact.clientutil.ComboBoxModel getCbmNature() {
        return cbmNature;
    }
    
    /**
     * Setter for property cbmNature.
     * @param cbmNature New value of property cbmNature.
     */
    public void setCbmNature(com.see.truetransact.clientutil.ComboBoxModel cbmNature) {
        this.cbmNature = cbmNature;
    }
    
    /**
     * Getter for property cboPledge.
     * @return Value of property cboPledge.
     */
    public java.lang.String getCboPledge() {
        return cboPledge;
    }
    
    /**
     * Setter for property cboPledge.
     * @param cboPledge New value of property cboPledge.
     */
    public void setCboPledge(java.lang.String cboPledge) {
        this.cboPledge = cboPledge;
    }
    
    /**
     * Getter for property cbmPledge.
     * @return Value of property cbmPledge.
     */
    public com.see.truetransact.clientutil.ComboBoxModel getCbmPledge() {
        return cbmPledge;
    }
    
    /**
     * Setter for property cbmPledge.
     * @param cbmPledge New value of property cbmPledge.
     */
    public void setCbmPledge(com.see.truetransact.clientutil.ComboBoxModel cbmPledge) {
        this.cbmPledge = cbmPledge;
    }
    
    /**
     * Getter for property cboRight.
     * @return Value of property cboRight.
     */
    public java.lang.String getCboRight() {
        return cboRight;
    }
    
    /**
     * Setter for property cboRight.
     * @param cboRight New value of property cboRight.
     */
    public void setCboRight(java.lang.String cboRight) {
        this.cboRight = cboRight;
    }
    
    /**
     * Getter for property cbmRight.
     * @return Value of property cbmRight.
     */
    public com.see.truetransact.clientutil.ComboBoxModel getCbmRight() {
        return cbmRight;
    }
    
    /**
     * Setter for property cbmRight.
     * @param cbmRight New value of property cbmRight.
     */
    public void setCbmRight(com.see.truetransact.clientutil.ComboBoxModel cbmRight) {
        this.cbmRight = cbmRight;
    }
    
    /**
     * Getter for property rdoEnhance_Yes.
     * @return Value of property rdoEnhance_Yes.
     */
    public boolean isRdoEnhance_Yes() {
        return rdoEnhance_Yes;
    }
    
    /**
     * Setter for property rdoEnhance_Yes.
     * @param rdoEnhance_Yes New value of property rdoEnhance_Yes.
     */
    public void setRdoEnhance_Yes(boolean rdoEnhance_Yes) {
        this.rdoEnhance_Yes = rdoEnhance_Yes;
    }
    
    /**
     * Getter for property rdoEnhance_No.
     * @return Value of property rdoEnhance_No.
     */
    public boolean isRdoEnhance_No() {
        return rdoEnhance_No;
    }
    
    /**
     * Setter for property rdoEnhance_No.
     * @param rdoEnhance_No New value of property rdoEnhance_No.
     */
    public void setRdoEnhance_No(boolean rdoEnhance_No) {
        this.rdoEnhance_No = rdoEnhance_No;
    }
    
    /**
     * Getter for property rdoRenewal_Yes.
     * @return Value of property rdoRenewal_Yes.
     */
    public boolean isRdoRenewal_Yes() {
        return rdoRenewal_Yes;
    }
    
    /**
     * Setter for property rdoRenewal_Yes.
     * @param rdoRenewal_Yes New value of property rdoRenewal_Yes.
     */
    public void setRdoRenewal_Yes(boolean rdoRenewal_Yes) {
        this.rdoRenewal_Yes = rdoRenewal_Yes;
    }
    
    /**
     * Getter for property rdoRenewal_No.
     * @return Value of property rdoRenewal_No.
     */
    public boolean isRdoRenewal_No() {
        return rdoRenewal_No;
    }
    
    /**
     * Setter for property rdoRenewal_No.
     * @param rdoRenewal_No New value of property rdoRenewal_No.
     */
    public void setRdoRenewal_No(boolean rdoRenewal_No) {
        this.rdoRenewal_No = rdoRenewal_No;
    }
    
    /**
     * Getter for property chargelst.
     * @return Value of property chargelst.
     */
    public java.util.List getChargelst() {
        return chargelst;
    }
    
    /**
     * Setter for property chargelst.
     * @param chargelst New value of property chargelst.
     */
    public void setChargelst(java.util.List chargelst) {
        this.chargelst = chargelst;
    }

    /**
     * Getter for property rdoGahanYes.
     * @return Value of property rdoGahanYes.
     */
    public boolean isRdoGahanYes() {
        return rdoGahanYes;
    }
    
    /**
     * Setter for property rdoGahanYes.
     * @param rdoGahanYes New value of property rdoGahanYes.
     */
    public void setRdoGahanYes(boolean rdoGahanYes) {
        this.rdoGahanYes = rdoGahanYes;
    }
    
    /**
     * Getter for property rdoGahanNo.
     * @return Value of property rdoGahanNo.
     */
    public boolean isRdoGahanNo() {
        return rdoGahanNo;
    }
    
    /**
     * Setter for property rdoGahanNo.
     * @param rdoGahanNo New value of property rdoGahanNo.
     */
    public void setRdoGahanNo(boolean rdoGahanNo) {
        this.rdoGahanNo = rdoGahanNo;
    }
    
    /**
     * Getter for property collateralMap.
     * @return Value of property collateralMap.
     */
    public java.util.LinkedHashMap getCollateralMap() {
        return collateralMap;
    }
    
    /**
     * Setter for property collateralMap.
     * @param collateralMap New value of property collateralMap.
     */
    public void setCollateralMap(java.util.LinkedHashMap collateralMap) {
        this.collateralMap = collateralMap;
    }
    
    /**
     * Getter for property pledgeValMap.
     * @return Value of property pledgeValMap.
     */
    public java.util.HashMap getPledgeValMap() {
        return pledgeValMap;
    }
    
    /**
     * Setter for property pledgeValMap.
     * @param pledgeValMap New value of property pledgeValMap.
     */
    public void setPledgeValMap(java.util.HashMap pledgeValMap) {
        this.pledgeValMap = pledgeValMap;
    }
    
    /**
     * Getter for property txtPledgeType.
     * @return Value of property txtPledgeType.
     */
    public java.lang.String getTxtPledgeType() {
        return txtPledgeType;
    }
    
    /**
     * Setter for property txtPledgeType.
     * @param txtPledgeType New value of property txtPledgeType.
     */
    public void setTxtPledgeType(java.lang.String txtPledgeType) {
        this.txtPledgeType = txtPledgeType;
    }
    
    /**
     * Getter for property tblJointCollateral.
     * @return Value of property tblJointCollateral.
     */
    public com.see.truetransact.clientutil.EnhancedTableModel getTblJointCollateral() {
        return tblJointCollateral;
    }
    
    /**
     * Setter for property tblJointCollateral.
     * @param tblJointCollateral New value of property tblJointCollateral.
     */
    public void setTblJointCollateral(com.see.truetransact.clientutil.EnhancedTableModel tblJointCollateral) {
        this.tblJointCollateral = tblJointCollateral;
    }
    
    /**
     * Getter for property cbmDocumentType.
     * @return Value of property cbmDocumentType.
     */
    public com.see.truetransact.clientutil.ComboBoxModel getCbmDocumentType() {
        return cbmDocumentType;
    }    
   
    /**
     * Setter for property cbmDocumentType.
     * @param cbmDocumentType New value of property cbmDocumentType.
     */
    public void setCbmDocumentType(com.see.truetransact.clientutil.ComboBoxModel cbmDocumentType) {
        this.cbmDocumentType = cbmDocumentType;
    }    
   
    /**
     * Getter for property docGenId.
     * @return Value of property docGenId.
     */
    public java.lang.String getDocGenId() {
        return docGenId;
    }    
    
    /**
     * Setter for property docGenId.
     * @param docGenId New value of property docGenId.
     */
    public void setDocGenId(java.lang.String docGenId) {
        this.docGenId = docGenId;
    }
    
    /**
     * Getter for property cboDocumentType.
     * @return Value of property cboDocumentType.
     */
    public java.lang.String getCboDocumentType() {
        return cboDocumentType;
    }
    
    /**
     * Setter for property cboDocumentType.
     * @param cboDocumentType New value of property cboDocumentType.
     */
    public void setCboDocumentType(java.lang.String cboDocumentType) {
        this.cboDocumentType = cboDocumentType;
    }
    
    /**
     * Getter for property isMobileBanking.
     * @return Value of property isMobileBanking.
     */
    public boolean getIsMobileBanking() {
        return isMobileBanking;
    }
    
    /**
     * Setter for property isMobileBanking.
     * @param isMobileBanking New value of property isMobileBanking.
     */
    public void setIsMobileBanking(boolean isMobileBanking) {
        this.isMobileBanking = isMobileBanking;
        setChanged();
    }
    
    /**
     * Getter for property txtMobileNo.
     * @return Value of property txtMobileNo.
     */
    public java.lang.String getTxtMobileNo() {
        return txtMobileNo;
    }
    
    /**
     * Setter for property txtMobileNo.
     * @param txtMobileNo New value of property txtMobileNo.
     */
    public void setTxtMobileNo(java.lang.String txtMobileNo) {
        this.txtMobileNo = txtMobileNo;
        setChanged();
    }
    
    /**
     * Getter for property tdtMobileSubscribedFrom.
     * @return Value of property tdtMobileSubscribedFrom.
     */
    public java.lang.String getTdtMobileSubscribedFrom() {
        return tdtMobileSubscribedFrom;
    }
    
    /**
     * Setter for property tdtMobileSubscribedFrom.
     * @param tdtMobileSubscribedFrom New value of property tdtMobileSubscribedFrom.
     */
    public void setTdtMobileSubscribedFrom(java.lang.String tdtMobileSubscribedFrom) {
        this.tdtMobileSubscribedFrom = tdtMobileSubscribedFrom;
    }
    
    /**
     * Getter for property tblSalarySecrityTable.
     * @return Value of property tblSalarySecrityTable.
     */
    public com.see.truetransact.clientutil.EnhancedTableModel getTblSalarySecrityTable() {
        return tblSalarySecrityTable;
    }
    
    /**
     * Setter for property tblSalarySecrityTable.
     * @param tblSalarySecrityTable New value of property tblSalarySecrityTable.
     */
    public void setTblSalarySecrityTable(com.see.truetransact.clientutil.EnhancedTableModel tblSalarySecrityTable) {
        this.tblSalarySecrityTable = tblSalarySecrityTable;
    }
    
    /**
     * Getter for property rdoSalaryRecovery.
     * @return Value of property rdoSalaryRecovery.
     */
    public java.lang.String getRdoSalaryRecovery() {
        return rdoSalaryRecovery;
    }
    
    /**
     * Setter for property rdoSalaryRecovery.
     * @param rdoSalaryRecovery New value of property rdoSalaryRecovery.
     */
    public void setRdoSalaryRecovery(java.lang.String rdoSalaryRecovery) {
        this.rdoSalaryRecovery = rdoSalaryRecovery;
    }
    
    /**
     * Getter for property lockStatus.
     * @return Value of property lockStatus.
     */
    public java.lang.String getLockStatus() {
        return lockStatus;
    }
    
    /**
     * Setter for property lockStatus.
     * @param lockStatus New value of property lockStatus.
     */
    public void setLockStatus(java.lang.String lockStatus) {
        this.lockStatus = lockStatus;
    }
    
    /**
     * Getter for property txtSubsidyAmt.
     * @return Value of property txtSubsidyAmt.
     */
    public java.lang.String getTxtSubsidyAmt() {
        return txtSubsidyAmt;
    }
    
    /**
     * Setter for property txtSubsidyAmt.
     * @param txtSubsidyAmt New value of property txtSubsidyAmt.
     */
    public void setTxtSubsidyAmt(java.lang.String txtSubsidyAmt) {
        this.txtSubsidyAmt = txtSubsidyAmt;
    }
    
    /**
     * Getter for property txtSubsidyAccHead.
     * @return Value of property txtSubsidyAccHead.
     */
    public java.lang.String getTxtSubsidyAccHead() {
        return txtSubsidyAccHead;
    }
    
    /**
     * Setter for property txtSubsidyAccHead.
     * @param txtSubsidyAccHead New value of property txtSubsidyAccHead.
     */
    public void setTxtSubsidyAccHead(java.lang.String txtSubsidyAccHead) {
        this.txtSubsidyAccHead = txtSubsidyAccHead;
    }
    
    /**
     * Getter for property tdtSubsidyAppDt.
     * @return Value of property tdtSubsidyAppDt.
     */
    public java.lang.String getTdtSubsidyAppDt() {
        return tdtSubsidyAppDt;
    }
    
    /**
     * Setter for property tdtSubsidyAppDt.
     * @param tdtSubsidyAppDt New value of property tdtSubsidyAppDt.
     */
    public void setTdtSubsidyAppDt(java.lang.String tdtSubsidyAppDt) {
        this.tdtSubsidyAppDt = tdtSubsidyAppDt;
    }
    
    /**
     * Getter for property rdoRebateInterest_Yes.
     * @return Value of property rdoRebateInterest_Yes.
     */
    public boolean isRdoRebateInterest_Yes() {
        return rdoRebateInterest_Yes;
    }
    
    /**
     * Setter for property rdoRebateInterest_Yes.
     * @param rdoRebateInterest_Yes New value of property rdoRebateInterest_Yes.
     */
    public void setRdoRebateInterest_Yes(boolean rdoRebateInterest_Yes) {
        this.rdoRebateInterest_Yes = rdoRebateInterest_Yes;
    }
    
    /**
     * Getter for property rdoRebateInterest_No.
     * @return Value of property rdoRebateInterest_No.
     */
    public boolean isRdoRebateInterest_No() {
        return rdoRebateInterest_No;
    }
    
    /**
     * Setter for property rdoRebateInterest_No.
     * @param rdoRebateInterest_No New value of property rdoRebateInterest_No.
     */
    public void setRdoRebateInterest_No(boolean rdoRebateInterest_No) {
        this.rdoRebateInterest_No = rdoRebateInterest_No;
    }
    
    /**
     * Getter for property txtRebateInterest_Amt.
     * @return Value of property txtRebateInterest_Amt.
     */
    public java.lang.String getTxtRebateInterest_Amt() {
        return txtRebateInterest_Amt;
    }
    
    /**
     * Setter for property txtRebateInterest_Amt.
     * @param txtRebateInterest_Amt New value of property txtRebateInterest_Amt.
     */
    public void setTxtRebateInterest_Amt(java.lang.String txtRebateInterest_Amt) {
        this.txtRebateInterest_Amt = txtRebateInterest_Amt;
    }
    
    /**
     * Getter for property tdtRebateInterest_App_Dt.
     * @return Value of property tdtRebateInterest_App_Dt.
     */
    public java.lang.String getTdtRebateInterest_App_Dt() {
        return tdtRebateInterest_App_Dt;
    }
    
    /**
     * Setter for property tdtRebateInterest_App_Dt.
     * @param tdtRebateInterest_App_Dt New value of property tdtRebateInterest_App_Dt.
     */
    public void setTdtRebateInterest_App_Dt(java.lang.String tdtRebateInterest_App_Dt) {
        this.tdtRebateInterest_App_Dt = tdtRebateInterest_App_Dt;
    }
    
    /**
     * Getter for property txtSubsidyAdjustedAmt.
     * @return Value of property txtSubsidyAdjustedAmt.
     */
    public java.lang.String getTxtSubsidyAdjustedAmt() {
        return txtSubsidyAdjustedAmt;
    }
    
    /**
     * Setter for property txtSubsidyAdjustedAmt.
     * @param txtSubsidyAdjustedAmt New value of property txtSubsidyAdjustedAmt.
     */
    public void setTxtSubsidyAdjustedAmt(java.lang.String txtSubsidyAdjustedAmt) {
        this.txtSubsidyAdjustedAmt = txtSubsidyAdjustedAmt;
    }
    
    // Setter method for txtJewelleryDetails
    void setTxtJewelleryDetails(String txtJewelleryDetails){
        this.txtJewelleryDetails = txtJewelleryDetails;
        setChanged();
    }
    // Getter method for txtJewelleryDetails
    String getTxtJewelleryDetails(){
        return this.txtJewelleryDetails;
    }
    
    void setTxtGrossWeight(String txtGrossWeight){
        this.txtGrossWeight = txtGrossWeight;
        setChanged();
    }
    // Getter method for txtGrossWeight
    String getTxtGrossWeight(){
        return this.txtGrossWeight;
    }
    
    // Setter method for txtNetWeight
    void setTxtNetWeight(String txtNetWeight){
        this.txtNetWeight = txtNetWeight;
        setChanged();
    }
    // Getter method for txtNetWeight
    String getTxtNetWeight(){
        return this.txtNetWeight;
    }
    
    // Setter method for txtValueOfGold
    void setTxtValueOfGold(String txtValueOfGold){
        this.txtValueOfGold = txtValueOfGold;
        setChanged();
    }
    // Getter method for txtValueOfGold
    String getTxtValueOfGold(){
        return this.txtValueOfGold;
    }
    
    // Setter method for txtGoldRemarks
    void setTxtGoldRemarks(String txtGoldRemarks){
        this.txtGoldRemarks = txtGoldRemarks;
        setChanged();
    }
    // Getter method for txtGoldRemarks
    String getTxtGoldRemarks(){
        return this.txtGoldRemarks;
    }
    
    public java.lang.String getCboProductTypeSecurity() {
        return cboProductTypeSecurity;
    }
    
    /**
     * Setter for property cboProductTypeSecurity.
     * @param cboProductTypeSecurity New value of property cboProductTypeSecurity.
     */
    public void setCboProductTypeSecurity(java.lang.String cboProductTypeSecurity) {
        this.cboProductTypeSecurity = cboProductTypeSecurity;
    }
    
    void setTxtDepNo(String txtDepNo){
        this.txtDepNo = txtDepNo;
        setChanged();
    }
    // Getter method for txtDepNo
    String getTxtDepNo(){
        return this.txtDepNo;
    }
    
    public java.lang.String getCboDepProdID() {
        return cboDepProdID;
    }
    
    /**
     * Setter for property cboDepProdID.
     * @param cboDepProdID New value of property cboDepProdID.
     */
    public void setCboDepProdID(java.lang.String cboDepProdID) {
        this.cboDepProdID = cboDepProdID;
    }
    
    // Setter method for tdtDepDt
    void setTdtDepDt(String tdtDepDt){
        this.tdtDepDt = tdtDepDt;
        setChanged();
    }
    // Getter method for tdtDepDt
    String getTdtDepDt(){
        return this.tdtDepDt;
    }
    
    void setTxtDepAmount(String txtDepAmount){
        this.txtDepAmount = txtDepAmount;
        setChanged();
    }
    // Getter method for txtDepAmount
    String getTxtDepAmount(){
        return this.txtDepAmount;
    }
    
    // Setter method for txtMaturityDt
    void setTxtMaturityDt(String txtMaturityDt){
        this.txtMaturityDt = txtMaturityDt;
        setChanged();
    }
    // Getter method for txtMaturityDt
    String getTxtMaturityDt(){
        return this.txtMaturityDt;
    }
    
    // Setter method for txtMaturityValue
    void setTxtMaturityValue(String txtMaturityValue){
        this.txtMaturityValue = txtMaturityValue;
        setChanged();
    }
    // Getter method for txtMaturityValue
    String getTxtMaturityValue(){
        return this.txtMaturityValue;
    }
    
    // Setter method for txtRateOfInterest
    void setTxtRateOfInterest(String txtRateOfInterest){
        this.txtRateOfInterest = txtRateOfInterest;
        setChanged();
    }
    // Getter method for txtRateOfInterest
    String getTxtRateOfInterest(){
        return this.txtRateOfInterest;
    }
    
    public com.see.truetransact.clientutil.ComboBoxModel getCbmProdTypeSecurity() {
        return cbmProdTypeSecurity;
    }
    
    /**
     * Setter for property cbmProdTypeSecurity.
     * @param cbmProdTypeSecurity New value of property cbmProdTypeSecurity.
     */
    public void setCbmProdTypeSecurity(com.see.truetransact.clientutil.ComboBoxModel cbmProdTypeSecurity) {
        this.cbmProdTypeSecurity = cbmProdTypeSecurity;
    }
    
    
    public com.see.truetransact.clientutil.ComboBoxModel getCbmLosInstitution() {
        return cbmLosInstitution;
    }
    
    /**
     * Setter for property cbmProdTypeSecurity.
     * @param cbmProdTypeSecurity New value of property cbmProdTypeSecurity.
     */
    public void setCbmLosInstitution(com.see.truetransact.clientutil.ComboBoxModel cbmLosInstitution) {
        this.cbmLosInstitution = cbmLosInstitution;
    }
    
    public com.see.truetransact.clientutil.ComboBoxModel getCbmProdType() {
        return cbmProdType;
    }
    
    /**
     * Setter for property cbmProdType.
     * @param cbmProdType New value of property cbmProdType.
     */
    public void setCbmProdType(com.see.truetransact.clientutil.ComboBoxModel cbmProdType) {
        this.cbmProdType = cbmProdType;
    }
    
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
    
    public boolean isDepositTypeData() {
        return depositTypeData;
    }
    
    /**
     * Setter for property depositTypeData.
     * @param depositTypeData New value of property depositTypeData.
     */
    public void setDepositTypeData(boolean depositTypeData) {
        this.depositTypeData = depositTypeData;
    }
    
    public com.see.truetransact.clientutil.ComboBoxModel getCbmDepProdID() {
        return cbmDepProdID;
    }
    
    public boolean isLosTypeData() {
        return losTypeData;
    }
    
    /**
     * Setter for property depositTypeData.
     * @param depositTypeData New value of property depositTypeData.
     */
    public void setLosTypeData(boolean losTypeData) {
        this.losTypeData = losTypeData;
    }
    
    
    
    
    /**
     * Setter for property cbmDepProdID.
     * @param cbmDepProdID New value of property cbmDepProdID.
     */
    public void setCbmDepProdID(com.see.truetransact.clientutil.ComboBoxModel cbmDepProdID) {
        this.cbmDepProdID = cbmDepProdID;
    }
    
    public void setCbmLosSecurityType(com.see.truetransact.clientutil.ComboBoxModel cbmLosSecurityType) {
        this.cbmLosSecurityType = cbmLosSecurityType;
    }
    
    public com.see.truetransact.clientutil.ComboBoxModel getCbmLosSecurityType() {
        return cbmLosSecurityType;
    }
    
    public com.see.truetransact.clientutil.EnhancedTableModel getTblDepositTypeDetails() {
        return tblDepositTypeDetails;
    }
    
    /**
     * Setter for property tblDepositTypeDetails.
     * @param tblDepositTypeDetails New value of property tblDepositTypeDetails.
     */
    public void setTblDepositTypeDetails(com.see.truetransact.clientutil.EnhancedTableModel tblDepositTypeDetails) {
        this.tblDepositTypeDetails = tblDepositTypeDetails;
    }
    
    public com.see.truetransact.clientutil.EnhancedTableModel getTblLosTypeDetails() {
        return tblLosTypeDetails;
    }
    
    /**
     * Setter for property tblDepositTypeDetails.
     * @param tblDepositTypeDetails New value of property tblDepositTypeDetails.
     */
    public void setTblLosTypeDetails(com.see.truetransact.clientutil.EnhancedTableModel tblLosTypeDetals) {
        this.tblLosTypeDetails = tblLosTypeDetails;
    }
    
    /**
     * Getter for property cboLosOtherInstitution.
     * @return Value of property cboLosOtherInstitution.
     */
    public java.lang.String getCboLosOtherInstitution() {
        return cboLosOtherInstitution;
    }
    
    /**
     * Setter for property cboLosOtherInstitution.
     * @param cboLosOtherInstitution New value of property cboLosOtherInstitution.
     */
    public void setCboLosOtherInstitution(java.lang.String cboLosOtherInstitution) {
        this.cboLosOtherInstitution = cboLosOtherInstitution;
    }
    
    /**
     * Getter for property txtLosName.
     * @return Value of property txtLosName.
     */
    public java.lang.String getTxtLosName() {
        return txtLosName;
    }
    
    /**
     * Setter for property txtLosName.
     * @param txtLosName New value of property txtLosName.
     */
    public void setTxtLosName(java.lang.String txtLosName) {
        this.txtLosName = txtLosName;
    }
    
    /**
     * Getter for property cboLosSecurityType.
     * @return Value of property cboLosSecurityType.
     */
    public java.lang.String getCboLosSecurityType() {
        return cboLosSecurityType;
    }
    
    /**
     * Setter for property cboLosSecurityType.
     * @param cboLosSecurityType New value of property cboLosSecurityType.
     */
    public void setCboLosSecurityType(java.lang.String cboLosSecurityType) {
        this.cboLosSecurityType = cboLosSecurityType;
    }
    
    /**
     * Getter for property txtLosSecurityNo.
     * @return Value of property txtLosSecurityNo.
     */
    public java.lang.String getTxtLosSecurityNo() {
        return txtLosSecurityNo;
    }
    
    /**
     * Setter for property txtLosSecurityNo.
     * @param txtLosSecurityNo New value of property txtLosSecurityNo.
     */
    public void setTxtLosSecurityNo(java.lang.String txtLosSecurityNo) {
        this.txtLosSecurityNo = txtLosSecurityNo;
    }
    
    /**
     * Getter for property txtLosAmount.
     * @return Value of property txtLosAmount.
     */
    public java.lang.String getTxtLosAmount() {
        return txtLosAmount;
    }
    
    /**
     * Setter for property txtLosAmount.
     * @param txtLosAmount New value of property txtLosAmount.
     */
    public void setTxtLosAmount(java.lang.String txtLosAmount) {
        this.txtLosAmount = txtLosAmount;
    }
    
    /**
     * Getter for property tdtLosIssueDate.
     * @return Value of property tdtLosIssueDate.
     */
    public java.lang.String getTdtLosIssueDate() {
        return tdtLosIssueDate;
    }
    
    /**
     * Setter for property tdtLosIssueDate.
     * @param tdtLosIssueDate New value of property tdtLosIssueDate.
     */
    public void setTdtLosIssueDate(java.lang.String tdtLosIssueDate) {
        this.tdtLosIssueDate = tdtLosIssueDate;
    }
    
    /**
     * Getter for property tdtLosMaturityDt.
     * @return Value of property tdtLosMaturityDt.
     */
    public java.lang.String getTdtLosMaturityDt() {
        return tdtLosMaturityDt;
    }
    
    /**
     * Setter for property tdtLosMaturityDt.
     * @param tdtLosMaturityDt New value of property tdtLosMaturityDt.
     */
    public void setTdtLosMaturityDt(java.lang.String tdtLosMaturityDt) {
        this.tdtLosMaturityDt = tdtLosMaturityDt;
    }
    
    /**
     * Getter for property txtLosMaturityValue.
     * @return Value of property txtLosMaturityValue.
     */
    public java.lang.String getTxtLosMaturityValue() {
        return txtLosMaturityValue;
    }
    
    /**
     * Setter for property txtLosMaturityValue.
     * @param txtLosMaturityValue New value of property txtLosMaturityValue.
     */
    public void setTxtLosMaturityValue(java.lang.String txtLosMaturityValue) {
        this.txtLosMaturityValue = txtLosMaturityValue;
    }
    
    /**
     * Getter for property txtLosRemarks.
     * @return Value of property txtLosRemarks.
     */
    public java.lang.String getTxtLosRemarks() {
        return txtLosRemarks;
    }
    
    /**
     * Setter for property txtLosRemarks.
     * @param txtLosRemarks New value of property txtLosRemarks.
     */
    public void setTxtLosRemarks(java.lang.String txtLosRemarks) {
        this.txtLosRemarks = txtLosRemarks;
    }				 

    /**
     * Getter for property isCaseDetailsTrans.
     * @return Value of property isCaseDetailsTrans.
     */
    public boolean isIsCaseDetailsTrans() {
        return isCaseDetailsTrans;
    }
    
    /**
     * Setter for property isCaseDetailsTrans.
     * @param isCaseDetailsTrans New value of property isCaseDetailsTrans.
     */
    public void setIsCaseDetailsTrans(boolean isCaseDetailsTrans) {
        this.isCaseDetailsTrans = isCaseDetailsTrans;
    }
    
    /**
     * Getter for property loanCaseAuthStatus.
     * @return Value of property loanCaseAuthStatus.
     */
    public java.lang.String getLoanCaseAuthStatus() {
        return loanCaseAuthStatus;
    }
    
    /**
     * Setter for property loanCaseAuthStatus.
     * @param loanCaseAuthStatus New value of property loanCaseAuthStatus.
     */
    public void setLoanCaseAuthStatus(java.lang.String loanCaseAuthStatus) {
        this.loanCaseAuthStatus = loanCaseAuthStatus;
    }				 

    /**
     * Getter for property cbmDirectPaymentProdType.
     * @return Value of property cbmDirectPaymentProdType.
     */
    public com.see.truetransact.clientutil.ComboBoxModel getCbmDirectPaymentProdType() {
        return cbmDirectPaymentProdType;
    }
    
    /**
     * Setter for property cbmDirectPaymentProdType.
     * @param cbmDirectPaymentProdType New value of property cbmDirectPaymentProdType.
     */
    public void setCbmDirectPaymentProdType(com.see.truetransact.clientutil.ComboBoxModel cbmDirectPaymentProdType) {
        this.cbmDirectPaymentProdType = cbmDirectPaymentProdType;
    }
    
    /**
     * Getter for property cbmDirectPaymentProdId.
     * @return Value of property cbmDirectPaymentProdId.
     */
    public com.see.truetransact.clientutil.ComboBoxModel getCbmDirectPaymentProdId() {
        return cbmDirectPaymentProdId;
    }
    
    /**
     * Setter for property cbmDirectPaymentProdId.
     * @param cbmDirectPaymentProdId New value of property cbmDirectPaymentProdId.
     */
    public void setCbmDirectPaymentProdId(com.see.truetransact.clientutil.ComboBoxModel cbmDirectPaymentProdId) {
        this.cbmDirectPaymentProdId = cbmDirectPaymentProdId;
    }
    
    /**
     * Getter for property cbmAgentId.
     * @return Value of property cbmAgentId.
     */
    public com.see.truetransact.clientutil.ComboBoxModel getCbmAgentId() {
        return cbmAgentId;
    }
    
    /**
     * Setter for property cbmAgentId.
     * @param cbmAgentId New value of property cbmAgentId.
     */
    public void setCbmAgentId(com.see.truetransact.clientutil.ComboBoxModel cbmAgentId) {
        this.cbmAgentId = cbmAgentId;
    }
    
    /**
     * Getter for property dailyLoan.
     * @return Value of property dailyLoan.
     */
    public boolean isDailyLoan() {
        return dailyLoan;
    }
    
    /**
     * Setter for property dailyLoan.
     * @param dailyLoan New value of property dailyLoan.
     */
    public void setDailyLoan(boolean dailyLoan) {
        this.dailyLoan = dailyLoan;
    }
    
    /**
     * Getter for property cbmDirectRepaymentLoanPeriod.
     * @return Value of property cbmDirectRepaymentLoanPeriod.
     */
    public com.see.truetransact.clientutil.ComboBoxModel getCbmDirectRepaymentLoanPeriod() {
        return cbmDirectRepaymentLoanPeriod;
    }
    
    /**
     * Setter for property cbmDirectRepaymentLoanPeriod.
     * @param cbmDirectRepaymentLoanPeriod New value of property cbmDirectRepaymentLoanPeriod.
     */
    public void setCbmDirectRepaymentLoanPeriod(com.see.truetransact.clientutil.ComboBoxModel cbmDirectRepaymentLoanPeriod) {
        this.cbmDirectRepaymentLoanPeriod = cbmDirectRepaymentLoanPeriod;
    }
    
    /**
     * Getter for property txtDirectRepaymentLoanPeriod.
     * @return Value of property txtDirectRepaymentLoanPeriod.
     */
    public java.lang.String getTxtDirectRepaymentLoanPeriod() {
        return txtDirectRepaymentLoanPeriod;
    }
    
    /**
     * Setter for property txtDirectRepaymentLoanPeriod.
     * @param txtDirectRepaymentLoanPeriod New value of property txtDirectRepaymentLoanPeriod.
     */
    public void setTxtDirectRepaymentLoanPeriod(java.lang.String txtDirectRepaymentLoanPeriod) {
        this.txtDirectRepaymentLoanPeriod = txtDirectRepaymentLoanPeriod;
    }
    
    /**
     * Getter for property DirectRepayment_Yes.
     * @return Value of property DirectRepayment_Yes.
     */
    public boolean isDirectRepayment_Yes() {
        return DirectRepayment_Yes;
    }
    
    /**
     * Setter for property DirectRepayment_Yes.
     * @param DirectRepayment_Yes New value of property DirectRepayment_Yes.
     */
    public void setDirectRepayment_Yes(boolean DirectRepayment_Yes) {
        this.DirectRepayment_Yes = DirectRepayment_Yes;
    }
    
    /**
     * Getter for property DirectRepayment_No.
     * @return Value of property DirectRepayment_No.
     */
    public boolean isDirectRepayment_No() {
        return DirectRepayment_No;
    }
    
    /**
     * Setter for property DirectRepayment_No.
     * @param DirectRepayment_No New value of property DirectRepayment_No.
     */
    public void setDirectRepayment_No(boolean DirectRepayment_No) {
        this.DirectRepayment_No = DirectRepayment_No;
    }
    
    /**
     * Getter for property txtDirectRepaymentAcctNo.
     * @return Value of property txtDirectRepaymentAcctNo.
     */
    public java.lang.String getTxtDirectRepaymentAcctNo() {
        return txtDirectRepaymentAcctNo;
    }
    
    /**
     * Setter for property txtDirectRepaymentAcctNo.
     * @param txtDirectRepaymentAcctNo New value of property txtDirectRepaymentAcctNo.
     */
    public void setTxtDirectRepaymentAcctNo(java.lang.String txtDirectRepaymentAcctNo) {
        this.txtDirectRepaymentAcctNo = txtDirectRepaymentAcctNo;
    }
    
    /**
     * Getter for property txtDirectRepaymentAcctHead.
     * @return Value of property txtDirectRepaymentAcctHead.
     */
    public java.lang.String getTxtDirectRepaymentAcctHead() {
        return txtDirectRepaymentAcctHead;
    }
    
    /**
     * Setter for property txtDirectRepaymentAcctHead.
     * @param txtDirectRepaymentAcctHead New value of property txtDirectRepaymentAcctHead.
     */
    public void setTxtDirectRepaymentAcctHead(java.lang.String txtDirectRepaymentAcctHead) {
        this.txtDirectRepaymentAcctHead = txtDirectRepaymentAcctHead;
    }
    
    /**
     * Getter for property txtCourtOrderNo.
     * @return Value of property txtCourtOrderNo.
     */
    public java.lang.String getTxtCourtOrderNo() {
        return txtCourtOrderNo;
    }
    
    /**
     * Setter for property txtCourtOrderNo.
     * @param txtCourtOrderNo New value of property txtCourtOrderNo.
     */
    public void setTxtCourtOrderNo(java.lang.String txtCourtOrderNo) {
        this.txtCourtOrderNo = txtCourtOrderNo;
    }
    
    /**
     * Getter for property tdtCourtOrderDate.
     * @return Value of property tdtCourtOrderDate.
     */
    public java.lang.String getTdtCourtOrderDate() {
        return tdtCourtOrderDate;
    }
    
    /**
     * Setter for property tdtCourtOrderDate.
     * @param tdtCourtOrderDate New value of property tdtCourtOrderDate.
     */
    public void setTdtCourtOrderDate(java.lang.String tdtCourtOrderDate) {
        this.tdtCourtOrderDate = tdtCourtOrderDate;
    }
    
    /**
     * Getter for property tdtOTSDate.
     * @return Value of property tdtOTSDate.
     */
    public java.lang.String getTdtOTSDate() {
        return tdtOTSDate;
    }
    
    /**
     * Setter for property tdtOTSDate.
     * @param tdtOTSDate New value of property tdtOTSDate.
     */
    public void setTdtOTSDate(java.lang.String tdtOTSDate) {
        this.tdtOTSDate = tdtOTSDate;
    }
    
    
    /**
     * Getter for property txtPrincipalAmount.
     * @return Value of property txtPrincipalAmount.
     */
    public double getTxtPrincipalAmount() {
        return txtPrincipalAmount;
    }
    
    /**
     * Setter for property txtPrincipalAmount.
     * @param txtPrincipalAmount New value of property txtPrincipalAmount.
     */
    public void setTxtPrincipalAmount(double txtPrincipalAmount) {
        this.txtPrincipalAmount = txtPrincipalAmount;
    }
    
    /**
     * Getter for property txtInterestAmount.
     * @return Value of property txtInterestAmount.
     */
    public double getTxtInterestAmount() {
        return txtInterestAmount;
    }
    
    /**
     * Setter for property txtInterestAmount.
     * @param txtInterestAmount New value of property txtInterestAmount.
     */
    public void setTxtInterestAmount(double txtInterestAmount) {
        this.txtInterestAmount = txtInterestAmount;
    }
    
    /**
     * Getter for property txtPenalInterestAmount.
     * @return Value of property txtPenalInterestAmount.
     */
    public double getTxtPenalInterestAmount() {
        return txtPenalInterestAmount;
    }
    
    /**
     * Setter for property txtPenalInterestAmount.
     * @param txtPenalInterestAmount New value of property txtPenalInterestAmount.
     */
    public void setTxtPenalInterestAmount(double txtPenalInterestAmount) {
        this.txtPenalInterestAmount = txtPenalInterestAmount;
    }
    
    /**
     * Getter for property txtChargeAmount.
     * @return Value of property txtChargeAmount.
     */
    public double getTxtChargeAmount() {
        return txtChargeAmount;
    }
    
    /**
     * Setter for property txtChargeAmount.
     * @param txtChargeAmount New value of property txtChargeAmount.
     */
    public void setTxtChargeAmount(double txtChargeAmount) {
        this.txtChargeAmount = txtChargeAmount;
    }
    
    /**
     * Getter for property txtTotalAmountWrittenOff.
     * @return Value of property txtTotalAmountWrittenOff.
     */
    public double getTxtTotalAmountWrittenOff() {
        return txtTotalAmountWrittenOff;
    }
    
    /**
     * Setter for property txtTotalAmountWrittenOff.
     * @param txtTotalAmountWrittenOff New value of property txtTotalAmountWrittenOff.
     */
    public void setTxtTotalAmountWrittenOff(double txtTotalAmountWrittenOff) {
        this.txtTotalAmountWrittenOff = txtTotalAmountWrittenOff;
    }
    
    /**
     * Getter for property txtNoInstallment.
     * @return Value of property txtNoInstallment.
     */
    public java.lang.String getTxtNoInstallment() {
        return txtNoInstallment;
    }
    
    /**
     * Setter for property txtNoInstallment.
     * @param txtNoInstallment New value of property txtNoInstallment.
     */
    public void setTxtNoInstallment(java.lang.String txtNoInstallment) {
        this.txtNoInstallment = txtNoInstallment;
    }
    
 
    
    
    
    /**
     * Getter for property cbmFreq.
     * @return Value of property cbmFreq.
     */
    public com.see.truetransact.clientutil.ComboBoxModel getCbmFreq() {
        return cbmFreq;
    }
    
    /**
     * Setter for property cbmFreq.
     * @param cbmFreq New value of property cbmFreq.
     */
    public void setCbmFreq(com.see.truetransact.clientutil.ComboBoxModel cbmFreq) {
        this.cbmFreq = cbmFreq;
    }
    
    /**
     * Getter for property rdoRepaySingle_YES.
     * @return Value of property rdoRepaySingle_YES.
     */
    public boolean isRdoRepaySingle_YES() {
        return rdoRepaySingle_YES;
    }
    
    /**
     * Setter for property rdoRepaySingle_YES.
     * @param rdoRepaySingle_YES New value of property rdoRepaySingle_YES.
     */
    public void setRdoRepaySingle_YES(boolean rdoRepaySingle_YES) {
        this.rdoRepaySingle_YES = rdoRepaySingle_YES;
    }
    
    /**
     * Getter for property rdoRepaySingle_NO.
     * @return Value of property rdoRepaySingle_NO.
     */
    public boolean isRdoRepaySingle_NO() {
        return rdoRepaySingle_NO;
    }
    
    /**
     * Setter for property rdoRepaySingle_NO.
     * @param rdoRepaySingle_NO New value of property rdoRepaySingle_NO.
     */
    public void setRdoRepaySingle_NO(boolean rdoRepaySingle_NO) {
        this.rdoRepaySingle_NO = rdoRepaySingle_NO;
    }
    
    /**
     * Getter for property txtPenal.
     * @return Value of property txtPenal.
     */
    public java.lang.String getTxtPenal() {
        return txtPenal;
    }
    
    /**
     * Setter for property txtPenal.
     * @param txtPenal New value of property txtPenal.
     */
    public void setTxtPenal(java.lang.String txtPenal) {
        this.txtPenal = txtPenal;
    }
    
    /**
     * Getter for property courtRemarks.
     * @return Value of property courtRemarks.
     */
    public java.lang.String getCourtRemarks() {
        return courtRemarks;
    }
    
    /**
     * Setter for property courtRemarks.
     * @param courtRemarks New value of property courtRemarks.
     */
    public void setCourtRemarks(java.lang.String courtRemarks) {
        this.courtRemarks = courtRemarks;
    }
    
    /**
     * Getter for property newCourtDetailsMap.
     * @return Value of property newCourtDetailsMap.
     */
    public java.util.HashMap getNewCourtDetailsMap() {
        return newCourtDetailsMap;
    }
    
    /**
     * Setter for property newCourtDetailsMap.
     * @param newCourtDetailsMap New value of property newCourtDetailsMap.
     */
    public void setNewCourtDetailsMap(java.util.HashMap newCourtDetailsMap) {
        this.newCourtDetailsMap = newCourtDetailsMap;
    }
    
    /**
     * Getter for property editCourtDetailsMap.
     * @return Value of property editCourtDetailsMap.
     */
    public java.util.HashMap getEditCourtDetailsMap() {
        return editCourtDetailsMap;
    }
    
    /**
     * Setter for property editCourtDetailsMap.
     * @param editCourtDetailsMap New value of property editCourtDetailsMap.
     */
    public void setEditCourtDetailsMap(java.util.HashMap editCourtDetailsMap) {
        this.editCourtDetailsMap = editCourtDetailsMap;
    }
    
    /**
     * Getter for property deleteCourtDetailsMap.
     * @return Value of property deleteCourtDetailsMap.
     */
    public java.util.HashMap getDeleteCourtDetailsMap() {
        return deleteCourtDetailsMap;
    }
    
    /**
     * Setter for property deleteCourtDetailsMap.
     * @param deleteCourtDetailsMap New value of property deleteCourtDetailsMap.
     */
    public void setDeleteCourtDetailsMap(java.util.HashMap deleteCourtDetailsMap) {
        this.deleteCourtDetailsMap = deleteCourtDetailsMap;
    }
    
    /**
     * Getter for property chkOTS.
     * @return Value of property chkOTS.
     */
    public boolean isChkOTS() {
        return chkOTS;
    }
    
    /**
     * Setter for property chkOTS.
     * @param chkOTS New value of property chkOTS.
     */
    public void setChkOTS(boolean chkOTS) {
        this.chkOTS = chkOTS;
        setChanged();
    }
    
    /**
     * Getter for property court_slno.
     * @return Value of property court_slno.
     */
    public int getCourt_slno() {
        return court_slno;
    }
    
    /**
     * Setter for property court_slno.
     * @param court_slno New value of property court_slno.
     */
    public void setCourt_slno(int court_slno) {
        this.court_slno = court_slno;
    }
    
    /**
     * Getter for property firstInstallmentDt.
     * @return Value of property firstInstallmentDt.
     */
    public java.lang.String getFirstInstallmentDt() {
        return firstInstallmentDt;
    }
    
    /**
     * Setter for property firstInstallmentDt.
     * @param firstInstallmentDt New value of property firstInstallmentDt.
     */
    public void setFirstInstallmentDt(java.lang.String firstInstallmentDt) {
        this.firstInstallmentDt = firstInstallmentDt;
    }
    
    /**
     * Getter for property lastInstallmentDt.
     * @return Value of property lastInstallmentDt.
     */
    public java.lang.String getLastInstallmentDt() {
        return lastInstallmentDt;
    }
    
    /**
     * Setter for property lastInstallmentDt.
     * @param lastInstallmentDt New value of property lastInstallmentDt.
     */
    public void setLastInstallmentDt(java.lang.String lastInstallmentDt) {
        this.lastInstallmentDt = lastInstallmentDt;
    }
    
    /**
     * Getter for property MaxLoanPeriodChar.
     * @return Value of property MaxLoanPeriodChar.
     */
    public java.lang.String getMaxLoanPeriodChar() {
        return MaxLoanPeriodChar;
    }
    
    /**
     * Setter for property MaxLoanPeriodChar.
     * @param MaxLoanPeriodChar New value of property MaxLoanPeriodChar.
     */
    public void setMaxLoanPeriodChar(java.lang.String MaxLoanPeriodChar) {
        this.MaxLoanPeriodChar = MaxLoanPeriodChar;
    }				 

    /**
     * Getter for property updateCourtDetails.
     * @return Value of property updateCourtDetails.
     */
    public boolean isUpdateCourtDetails() {
        return updateCourtDetails;
    }
    
    /**
     * Setter for property updateCourtDetails.
     * @param updateCourtDetails New value of property updateCourtDetails.
     */
    public void setUpdateCourtDetails(boolean updateCourtDetails) {
        this.updateCourtDetails = updateCourtDetails;
    }
    
    /**
     * Getter for property txtOTSRate.
     * @return Value of property txtOTSRate.
     */
    public java.lang.String getTxtOTSRate() {
        return txtOTSRate;
    }
    
    /**
     * Setter for property txtOTSRate.
     * @param txtOTSRate New value of property txtOTSRate.
     */
    public void setTxtOTSRate(java.lang.String txtOTSRate) {
        this.txtOTSRate = txtOTSRate;
    }
    
    /**
     * Getter for property txtTotAmountDue.
     * @return Value of property txtTotAmountDue.
     */
    public java.lang.String getTxtTotAmountDue() {
        return txtTotAmountDue;
    }
    
    /**
     * Setter for property txtTotAmountDue.
     * @param txtTotAmountDue New value of property txtTotAmountDue.
     */
    public void setTxtTotAmountDue(java.lang.String txtTotAmountDue) {
        this.txtTotAmountDue = txtTotAmountDue;
    }
    
    /**
     * Getter for property txtSettlementAmt.
     * @return Value of property txtSettlementAmt.
     */
    public java.lang.String getTxtSettlementAmt() {
        return txtSettlementAmt;
    }
    
    /**
     * Setter for property txtSettlementAmt.
     * @param txtSettlementAmt New value of property txtSettlementAmt.
     */
    public void setTxtSettlementAmt(java.lang.String txtSettlementAmt) {
        this.txtSettlementAmt = txtSettlementAmt;
    }
    
    /**
     * Getter for property txtInstallmentAmt.
     * @return Value of property txtInstallmentAmt.
     */
    public java.lang.String getTxtInstallmentAmt() {
        return txtInstallmentAmt;
    }
    
    /**
     * Setter for property txtInstallmentAmt.
     * @param txtInstallmentAmt New value of property txtInstallmentAmt.
     */
    public void setTxtInstallmentAmt(java.lang.String txtInstallmentAmt) {
        this.txtInstallmentAmt = txtInstallmentAmt;
    }
    
    /**
     * Getter for property chkEligibleAmt.
     * @return Value of property chkEligibleAmt.
     */
    public boolean isChkEligibleAmt() {
        return chkEligibleAmt;
    }
    
    /**
     * Setter for property chkEligibleAmt.
     * @param chkEligibleAmt New value of property chkEligibleAmt.
     */
    public void setChkEligibleAmt(boolean chkEligibleAmt) {
        this.chkEligibleAmt = chkEligibleAmt;
    }
    
    /**
     * Getter for property cbmCropName.
     * @return Value of property cbmCropName.
     */
    public com.see.truetransact.clientutil.ComboBoxModel getCbmCropName() {
        return cbmCropName;
    }
    
    /**
     * Setter for property cbmCropName.
     * @param cbmCropName New value of property cbmCropName.
     */
    public void setCbmCropName(com.see.truetransact.clientutil.ComboBoxModel cbmCropName) {
        this.cbmCropName = cbmCropName;
    }
    
    /**
     * Getter for property tblCropDetails.
     * @return Value of property tblCropDetails.
     */
    public com.see.truetransact.clientutil.EnhancedTableModel getTblCropDetails() {
        return tblCropDetails;
    }
    
    /**
     * Setter for property tblCropDetails.
     * @param tblCropDetails New value of property tblCropDetails.
     */
    public void setTblCropDetails(com.see.truetransact.clientutil.EnhancedTableModel tblCropDetails) {
        this.tblCropDetails = tblCropDetails;
    }
    
    /**
     * Getter for property txtAreaAcrs.
     * @return Value of property txtAreaAcrs.
     */
    public java.lang.String getTxtAreaAcrs() {
        return txtAreaAcrs;
    }
    
    /**
     * Setter for property txtAreaAcrs.
     * @param txtAreaAcrs New value of property txtAreaAcrs.
     */
    public void setTxtAreaAcrs(java.lang.String txtAreaAcrs) {
        this.txtAreaAcrs = txtAreaAcrs;
    }
    
    /**
     * Getter for property txtEligibleCropAmt.
     * @return Value of property txtEligibleCropAmt.
     */
    public java.lang.String getTxtEligibleCropAmt() {
        return txtEligibleCropAmt;
    }
    
    /**
     * Setter for property txtEligibleCropAmt.
     * @param txtEligibleCropAmt New value of property txtEligibleCropAmt.
     */
    public void setTxtEligibleCropAmt(java.lang.String txtEligibleCropAmt) {
        this.txtEligibleCropAmt = txtEligibleCropAmt;
    }				 

    /**
     * Getter for property loanMap.
     * @return Value of property loanMap.
     */
    public HashMap getLoanMap() {
        return loanMap;
    }
    
    /**
     * Setter for property loanMap.
     * @param loanMap New value of property loanMap.
     */
    public void setLoanMap(HashMap loanMap) {
        this.loanMap = loanMap;
    }
    
    public boolean isChkDiminishing() {
        return chkDiminishing;
    }

    public void setChkDiminishing(boolean chkDiminishing) {
        this.chkDiminishing = chkDiminishing;
    }
     
    public String getBalanceShareAmt() {
        return balanceShareAmt;
    }

    public void setBalanceShareAmt(String balanceShareAmt) {
        this.balanceShareAmt = balanceShareAmt;
    }
    
        public String getChkRecovery() {
        return chkRecovery;
    }

    public void setChkRecovery(String chkRecovery) {
        this.chkRecovery = chkRecovery;
    }
 
    public String getTxtDealerID() {
        return txtDealerID;
    }

    public void setTxtDealerID(String txtDealerID) {
        this.txtDealerID = txtDealerID;
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
    public String getLoanApplictionScheme(HashMap map) {
        String schemeName = "";
        try {
            HashMap schemeMap = new HashMap();
            schemeMap.put("APPLICATION_NO", map.get("APPLICATION_NO"));
            List lst = ClientUtil.executeQuery("getSelectLoanApplicationTO", schemeMap);
            if (lst != null && lst.size() > 0) {
                LoanApplicationTO lp = (LoanApplicationTO) lst.get(0);
                schemeName = CommonUtil.convertObjToStr(lp.getSchemName());
            }
            return schemeName;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
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
    
    // Added by nithya on 07-03-2020 for KD-1379

    public String getRdoGoldSecurityStockExists() {
        return rdoGoldSecurityStockExists;
    }

    public void setRdoGoldSecurityStockExists(String rdoGoldSecurityStockExists) {
        this.rdoGoldSecurityStockExists = rdoGoldSecurityStockExists;
    }

    public String getTxtGoldSecurityId() {
        return txtGoldSecurityId;
    }

    public void setTxtGoldSecurityId(String txtGoldSecurityId) {
        this.txtGoldSecurityId = txtGoldSecurityId;
    }

    public ComboBoxModel getCbmRecommendedByType2() {
        return cbmRecommendedByType2;
    }

    public void setCbmRecommendedByType2(ComboBoxModel cbmRecommendedByType2) {
        this.cbmRecommendedByType2 = cbmRecommendedByType2;
    }

    public String getCboRecommendedByType2() {
        return cboRecommendedByType2;
    }

    public void setCboRecommendedByType2(String cbocboRecommendedByType2) {
        this.cboRecommendedByType2 = cbocboRecommendedByType2;
    }

    public String getTxtMemPriority() {
        return txtMemPriority;
    }

    public void setTxtMemPriority(String txtMemPriority) {
        this.txtMemPriority = txtMemPriority;
    }
    
    
}

