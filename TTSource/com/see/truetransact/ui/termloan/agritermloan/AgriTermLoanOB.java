/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition.
 * 
 * TermLoanOB.java
 *
 * Created on January 8, 2004, 4:24 PM
 */

package com.see.truetransact.ui.termloan.agritermloan;

import com.see.truetransact.clientutil.ClientConstants;
import com.see.truetransact.clientutil.ClientUtil;
import com.see.truetransact.clientutil.ComboBoxModel;
import com.see.truetransact.clientutil.EnhancedTableModel;
import com.see.truetransact.clientutil.TableUtil;
import com.see.truetransact.clientexception.ClientParseException;
import com.see.truetransact.clientproxy.ProxyFactory;
import com.see.truetransact.clientproxy.ProxyParameters;
import com.see.truetransact.commonutil.CommonConstants;
import com.see.truetransact.commonutil.DateUtil;
import com.see.truetransact.commonutil.CommonUtil;
import com.see.truetransact.commonutil.TTException;
import com.see.truetransact.transferobject.termloan.agritermloan.AgriTermLoanJointAcctTO;
import com.see.truetransact.transferobject.termloan.agritermloan.AgriTermLoanBorrowerTO;
import com.see.truetransact.transferobject.termloan.agritermloan.AgriTermLoanClassificationTO;
import com.see.truetransact.transferobject.termloan.agritermloan.AgriTermLoanCompanyTO;
import com.see.truetransact.transferobject.termloan.agritermloan.AgriTermLoanFacilityTO;
import com.see.truetransact.transferobject.termloan.agritermloan.AgriTermLoanOtherDetailsTO;
import com.see.truetransact.transferobject.termloan.agritermloan.AgriTermLoanSanctionFacilityTO;
import com.see.truetransact.transferobject.termloan.agritermloan.AgriTermLoanDocumentTO;
import com.see.truetransact.transferobject.termloan.agritermloan.AgriTermLoanSanctionTO;
import com.see.truetransact.transferobject.termloan.agritermloan.AgriTermLoanInsuranceTO;
import com.see.truetransact.ui.common.authorizedsignatory.AuthorizedSignatoryInstructionOB;
import com.see.truetransact.ui.termloan.agritermloan.agrinewdetails.AgriSubLimitOB;
import com.see.truetransact.ui.termloan.agritermloan.agrisubsidydetails.AgriSubSidyOB;
import  com.see.truetransact.ui.termloan.agritermloan.agriinspectiondetails.AgriInspectionOB;
import com.see.truetransact.ui.common.authorizedsignatory.AuthorizedSignatoryOB;
import com.see.truetransact.ui.common.powerofattorney.PowerOfAttorneyOB;
import com.see.truetransact.ui.TrueTransactMain;
import com.see.truetransact.uicomponent.CObservable;
import com.see.truetransact.uicomponent.COptionPane;
import com.see.truetransact.ui.deposit.lien.DepositLienUI;

import org.apache.log4j.Logger;

import java.util.ArrayList;
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
public class AgriTermLoanOB extends CObservable {
    
    private int actionType;
    private int resultStatus;
    
    private String lblStatus = ClientConstants.ACTION_STATUS[ClientConstants.ACTIONTYPE_CANCEL];
    
    private       static AgriTermLoanOB termLoanOB;
    private       static AgriTermLoanBorrowerOB termLoanBorrowerOB;
    private       static AgriTermLoanCompanyOB termLoanCompanyOB;
    private       static PowerOfAttorneyOB termLoanPoAOB;
    private       static AgriSubLimitOB agriSubLimitOB;
    private       static AgriSubSidyOB  agriSubSidyOB;
    private       static AgriInspectionOB  agriInspectionOB;
    private       static AgriTermLoanSecurityOB termLoanSecurityOB;
    private       static AgriTermLoanRepaymentOB termLoanRepaymentOB;
    private       static AgriTermLoanGuarantorOB termLoanGuarantorOB;
    private       static AgriTermLoanDocumentDetailsOB termLoanDocumentDetailsOB;
    private       static AgriTermLoanInterestOB termLoanInterestOB;
    private       static AgriTermLoanClassificationOB termLoanClassificationOB;
    private       static AgriTermLoanOtherDetailsOB termLoanOtherDetailsOB;
    private       static AuthorizedSignatoryOB termLoanAuthorizedSignatoryOB;
    private       static AuthorizedSignatoryInstructionOB termLoanAuthorizedSignatoryInstructionOB;
    //    private       static TermLoanPeakSanctionOB  termLoanPeakSanctionOB;//bala
    private final static Logger log = Logger.getLogger(AgriTermLoanOB.class);
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
    
    private final   String    POFATTORNEY="POFATTORNEY";
    private final   String    DOCDETAILS="DOCDETAILS";
    private final   String    AUTHSIGNATORY="AUTHSIGNATORY";   // NEW CODE
    
    private final   String    POF_ATTORNEY="POF_ATTORNEY";
    private final   String    AUTH_SIGNATORY="AUTH_SIGNATORY";
    private final   String    DOC_DETAILS="DOC_DETAILS";
    
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
    //    private final   String    AUTHSIGNATORY="AUTHSIGNATORY";
    //    private final   String    POFATTORNEY="POFATTORNEY";
    private final   String    LIMIT = "LIMIT";
    private final   String    MAIN = "MAIN";
    private final   String    MAX_DEL_SAN_DETAIL_SL_NO = "MAX_DEL_SAN_DETAIL_SL_NO";
    private final   String    MAX_DEL_SAN_SL_NO = "MAX_DEL_SAN_SL_NO";
    private final   String    MORATORIUM_GIVEN = "MORATORIUM_GIVEN";
    private final   String    MORATORIUM_PERIOD = "MORATORIUM_PERIOD";
    private final   String    MULTI_DISBURSE = "MULTI_DISBURSE";
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
    private final   String    RECOMMANDED_BY="RECOMMANDED_BY";
    private final   String    REPAY_FREQ = "REPAY_FREQ";
    private final   String    REPAYMENT_DETAILS = "REPAYMENT_DETAILS";
    private final   String    INSTALLMENT_ALL_DETAILS = "INSTALLMENT_ALL_DETAILS";
    private final   String    RISK_WEIGHT = "RISK_WEIGHT";
    private final   String    CARD_TYPE="CARD_TYPE";
    private final   String    CARD_LIMIT="CARD_LIMIT";
    private final   String    REVIEW_DT="REVIEW_DT";
    private final   String    CARD_PERIOD="CARD_PERIOD";
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
    private final   String    INSEPECTION = "INSEPECTION";
    private final   String    SHADOW_CREDIT = "SHADOW_CREDIT";
    private final   String    SHADOW_DEBIT = "SHADOW_DEBIT";
    private final   String    SIMPLE = "SIMPLE";
    private final   String    SLNO = "SLNO";
    private final   String    STOCK_INSPECT = "STOCK_INSPECT";
    private final   String    STOCK_INSPECT_GUARANTAR = "STOCK_INSPECT,GUARANTAR";
    private final   String    STOCK_INSPECT_INSURANCE = "STOCK_INSPECT,INSURANCE";
    private final   String    SUBMIT = "SUBMIT";
    private final   String    SUBSIDY = "SUBSIDY";
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
    
    private double periodData = 0;// for setting data depending on period comboboxes...
    private double resultData = 0;// for setting data depending on period comboboxes...
    
    
    private final String YEAR ="YEARS";
    private final String MONTH ="MONTHS";
    private final String DAY ="DAYS";
    
    private final String YEAR1 ="Years";
    private final String MONTH1 ="Months";
    private final String DAY1 ="Days";
    private final int year = 365;
    private final int month = 30;
    private final int day = 1;
    
    private final   ArrayList sanctionFacilityTitle = new ArrayList();  //  Table Title of Saction facility
    private final   ArrayList sanctionMainTitle = new ArrayList();      //  Table Title of Sanction Main
    private final   ArrayList shareTitle=new ArrayList();
    private final   ArrayList insuranceTitle=new ArrayList();
    //    private final   TermLoanRB objTermLoanRB = new TermLoanRB();
    private int option=-1;
    private final   java.util.ResourceBundle objTermLoanRB = java.util.ResourceBundle.getBundle("com.see.truetransact.ui.termloan.agritermloan.AgriTermLoanRB", ProxyParameters.LANGUAGE);
    
    private EnhancedTableModel tblSanctionFacility;
    private EnhancedTableModel tblSanctionMain;
    private EnhancedTableModel tblShare;
    private EnhancedTableModel tblInsuranceDetails;
    private TableUtil         insDetailsTab=new TableUtil();
    private ProxyFactory proxy = null;
    private ArrayList LoanNo=new ArrayList();
    private ArrayList key;
    private ArrayList value;
    private ArrayList sanctionFacilityTabRow;
    private ArrayList sanctionFacilityAllTabRecords = new ArrayList(); // ArrayList to display in Sanction Facility
    private ArrayList sanctionMainTabRow;
    private ArrayList sanctionMainAllTabRecords = new ArrayList();     // ArrayList to display in Sanction Main
    private ArrayList facilityTabSanction;
    private LinkedHashMap objRepaymentInstallmentAllMap =new LinkedHashMap();
    private LinkedHashMap sanctionFacilityAll = new LinkedHashMap();   // Both displayed and hidden values in the table
    private LinkedHashMap sanctionMainAll = new LinkedHashMap();       // Both displayed and hidden values in the table
    private LinkedHashMap facilityAll = new LinkedHashMap();           // Both displayed and hidden values in the table
    
    private HashMap removedFaccilityTabValues = new HashMap();
    private HashMap sanctionFacilityRecord;
    private HashMap sanctionMainRecord;
    private HashMap facilityRecord;
    
    private HashMap lookUpHash;
    private HashMap operationMap;
    private HashMap keyValue;
    private HashMap authorizeMap;
    
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
    private ComboBoxModel cbmSelectScreen;
    private ComboBoxModel cbmIntGetFrom;
    
    private ComboBoxModel cbmInsDetails;
    private ComboBoxModel cbmInsUnderScheme;
    private ComboBoxModel cbmDebitProdType;
    private ComboBoxModel cbmDebitProdId;
    private ComboBoxModel cbmNatureOfRiskCoverd;
    private ComboBoxModel cbmCardType;
    private ComboBoxModel cbmCardPeriod;
    
    private String cboCardType="";
    private String cboInsDetails="";
    private String cboInsUnderScheme="";
    private String cboDebitProdType="";
    private String cboDebitProdId="";
    private String cboNatureOfRiskCoverd="";
    
    private String tdtReviewDate="";
    private String txtCardLimit="";
    private String txtCardPeriod="";
    private String cboCardPeriod="";
    private Date   last_int_calc_dt=null;
    private int resultValue=0;// for retrieving data from the period comboboxes...
    
    
    
    
    
    private String insComName;
    private String address;
    private String phoneNo;
    private String policyNo;
    private String premiumAmt;
    private String insIssueDt;
    private String insStDt;
    private String insExpiryDt;
    private String insuranceAmt;
    private String fromActNo;
    private String fromActNo1;
    
    private ArrayList singleInsList;
    private HashMap  singleInsMap;
    private LinkedHashMap allInsMap= new LinkedHashMap();
    private ArrayList     allInsList=new ArrayList() ;
    
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
    private boolean chkInspection=false;
    
    private boolean chkDocDetails  =false;
    private boolean chkAuthorizedSignatory=false;
    private boolean chkPOFAttorney=false;
    
    private boolean chkGurantor = false;
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
    private String txtContactPerson = "";
    private String txtContactPhone = "";
    private String txtRemarks = "";
    private String cboRecommendedByType="";
    private String AccountOpenDate="";
    private String cboProdId = "";
    private String cboProdID_RS = "";
    private String cboProdID_IDetail = "";
    private String borrowerNo = "";
    private String lblAccHead_2 = "";
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
    private double shadowDebit=0;
    private double shadowCredit=0;
    private double clearBalance=0;

    private boolean canBorrowerDelete = false;
    
    static {
        try {
            termLoanOB = new AgriTermLoanOB();
        } catch(Exception e) {
            log.info("try: " + e);
            parseException.logException(e,true);
            e.printStackTrace();
        }
    }
    
    /**
     * Returns an instance of AgriTermLoanOB
     *
     * @return TermLoanOB
     */
    public static AgriTermLoanOB getInstance() {
        return termLoanOB;
    }
    
    /** Creates a new instance of TermLoanOB */
    private AgriTermLoanOB() throws Exception {
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
        termLoanBorrowerOB = AgriTermLoanBorrowerOB.getInstance();
        termLoanCompanyOB = AgriTermLoanCompanyOB.getInstance();
        termLoanSecurityOB = AgriTermLoanSecurityOB.getInstance();
        termLoanRepaymentOB = AgriTermLoanRepaymentOB.getInstance();
        termLoanGuarantorOB = AgriTermLoanGuarantorOB.getInstance();
        termLoanDocumentDetailsOB = AgriTermLoanDocumentDetailsOB.getInstance();
        termLoanInterestOB = AgriTermLoanInterestOB.getInstance();
        termLoanClassificationOB = AgriTermLoanClassificationOB.getInstance();
        termLoanOtherDetailsOB = AgriTermLoanOtherDetailsOB.getInstance();
        //        termLoanPeakSanctionOB =TermLoanPeakSanctionOB.getInstance();
        fillDropdown();
        setSanctionFacilityTitle();
        setSanctionMainTitle();
        setShareTitle();
        setInsuranceTitle();
        tableUtilSanction_Facility.setAttributeKey(SLNO);
        tableUtilSanction_Main.setAttributeKey(SANCTION_SL_NO);
        insDetailsTab.setAttributeKey(SLNO);
        tblSanctionFacility = new EnhancedTableModel(null, sanctionFacilityTitle);
        tblSanctionMain = new EnhancedTableModel(null, sanctionMainTitle);
        tblShare=new EnhancedTableModel(null,shareTitle);
        tblInsuranceDetails=new EnhancedTableModel(null,insuranceTitle);
        notifyObservers();
    }
    
    public void setExtendedOB(AuthorizedSignatoryOB authOB, AuthorizedSignatoryInstructionOB authInstOB, PowerOfAttorneyOB poaOB,
    AgriSubLimitOB agriSUBLIMITOB,AgriSubSidyOB subsidyOB,AgriInspectionOB insOB){
        termLoanAuthorizedSignatoryOB = authOB;
        termLoanAuthorizedSignatoryInstructionOB = authInstOB;
        termLoanPoAOB = poaOB;
        agriSubLimitOB=agriSUBLIMITOB;
        agriSubSidyOB=subsidyOB;
        agriInspectionOB=insOB;
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
    private void setInsuranceTitle() throws Exception{
        try{
            insuranceTitle.add(objTermLoanRB.getString("tblcolumnInsuranceSlno"));
            insuranceTitle.add(objTermLoanRB.getString("tblcolumnInsuranceType"));
            insuranceTitle.add(objTermLoanRB.getString("tblcolumnInsuranceScheme"));
            insuranceTitle.add(objTermLoanRB.getString("tblcolumnInsuranceCompName"));
            
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
        lookup_keys.add("TERMLOAN.SCREEN_TYPE");
        lookup_keys.add("TERMLOAN.AGRI_INSURANCE_TYPE");
        lookup_keys.add("TERMLOAN.AGRI_INSURANCE_UNDER_SCHEME");
        lookup_keys.add("PRODUCTTYPE");
        lookup_keys.add("TERMLOAN.SECURITY");
        lookup_keys.add("PERIOD");
        lookUpHash.put(CommonConstants.PARAMFORQUERY, lookup_keys);
        
        keyValue = ClientUtil.populateLookupData(lookUpHash);
        
        getKeyValue((HashMap)keyValue.get("CATEGORY"));
        termLoanBorrowerOB.setCbmCategory(new ComboBoxModel(key,value));
        
        getKeyValue((HashMap)keyValue.get("CONSTITUTION"));
        termLoanBorrowerOB.setCbmConstitution(new ComboBoxModel(key,value));
        
        getKeyValue((HashMap)keyValue.get("CUSTOMER.ADDRTYPE"));
        termLoanCompanyOB.setCbmAddressType(new ComboBoxModel(key,value));
        
        getKeyValue((HashMap)keyValue.get("CUSTOMER.CITY"));
        termLoanCompanyOB.setCbmCity_CompDetail(new ComboBoxModel(key,value));
        
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
        
        getKeyValue((HashMap)keyValue.get("PERIOD"));
        cbmCardPeriod = new ComboBoxModel(key,value);
        
        getKeyValue((HashMap)keyValue.get("LOAN.FREQUENCY"));
        cbmRepayFreq = new ComboBoxModel(key,value);
        
        getKeyValue((HashMap)keyValue.get("LOAN.FREQUENCY"));
        setCbmRepayFreq_LOAN(new ComboBoxModel(key,value));
        //insurancefilldropdown
        setBlankKeyValue();
        getKeyValue((HashMap)keyValue.get("TERMLOAN.AGRI_INSURANCE_TYPE"));
        setCbmInsDetails(new ComboBoxModel(key,value));
        setBlankKeyValue();
        getKeyValue((HashMap)keyValue.get("TERMLOAN.AGRI_INSURANCE_UNDER_SCHEME"));
        setCbmInsUnderScheme(new ComboBoxModel(key,value));
        setBlankKeyValue();
        getKeyValue((HashMap)keyValue.get("PRODUCTTYPE"));
        setCbmDebitProdType(new ComboBoxModel(key,value));
        setBlankKeyValue();
        setCbmDebitProdId(new ComboBoxModel(key,value));
        setBlankKeyValue();
        getKeyValue((HashMap)keyValue.get("TERM_LOAN.RISK_NATURE"));
        setCbmNatureOfRiskCoverd(new ComboBoxModel(key,value));
        
        getKeyValue((HashMap)keyValue.get("FREQUENCY"));
        for (int i = key.size() - 1;i >= 0;--i){
            if (key.get(i).equals("180") || key.get(i).equals("90") || key.get(i).equals("7") || key.get(i).equals("1")){
                key.remove(i);
            }
        }
        for (int i = value.size() - 1;i >= 0;--i){
            if (value.get(i).equals("Half Yearly") || value.get(i).equals("Quaterly") || value.get(i).equals("Weekly") || value.get(i).equals("Daily")){
                value.remove(i);
            }
        }
        setCbmRepayFreq_ADVANCE(new ComboBoxModel(key,value));
        
        getKeyValue((HashMap)keyValue.get("LOANPRODUCT.OPERATESLIKE"));
        cbmTypeOfFacility = new ComboBoxModel(key,value);
        //        if(loanType.equals("OTHERS"))
        //                 cbmTypeOfFacility.removeKeyAndElement("Over Draft");
        //                 cbmTypeOfFacility.removeKeyAndElement("Cash Credit");
        System.out.println("loanType###"+loanType);
        getKeyValue((HashMap)keyValue.get("CONSTITUTION"));
        termLoanGuarantorOB.setCbmConstitution_GD(new ComboBoxModel(key,value));
        //not commited
        getKeyValue((HashMap)keyValue.get("TERMLOAN.SECURITY"));
        termLoanGuarantorOB.setCbmSecurityType(new ComboBoxModel(key,value));
        
        getKeyValue((HashMap)keyValue.get("CUSTOMER.CITY"));
        termLoanGuarantorOB.setCbmCity_GD(new ComboBoxModel(key,value));
        
        getKeyValue((HashMap)keyValue.get("CUSTOMER.STATE"));
        termLoanGuarantorOB.setCbmState_GD(new ComboBoxModel(key,value));
        
        getKeyValue((HashMap)keyValue.get("CUSTOMER.COUNTRY"));
        termLoanGuarantorOB.setCbmCountry_GD(new ComboBoxModel(key,value));
        
        getKeyValue((HashMap)keyValue.get("PRODUCTTYPE"));
        termLoanGuarantorOB.setCbmProdType(new ComboBoxModel(key,value));
        
        getKeyValue((HashMap)keyValue.get("TERMLOAN.INTERESTTYPE"));
        cbmInterestType = new ComboBoxModel(key,value);
        getKeyValue((HashMap)keyValue.get("BOARDDIRECTORS"));
        cbmRecommendedByType = new ComboBoxModel(key,value);
        
        getKeyValue((HashMap)keyValue.get("LOAN.FREQUENCY"));
        termLoanRepaymentOB.setCbmRepayFreq_Repayment(new ComboBoxModel(key,value));
        
        getKeyValue((HashMap)keyValue.get("TERM_LOAN.REPAYMENT_TYPE"));
        termLoanRepaymentOB.setCbmRepayType(new ComboBoxModel(key,value));
        
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
        
        setBlankKeyValue();
        setCbmCardType(new ComboBoxModel(key,value));
        getKeyValue((HashMap)keyValue.get("TERMLOAN.SCREEN_TYPE"));
        setCbmSelectScreen(new ComboBoxModel(key,value));
        
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
    
    private void setOperationMap() throws Exception{
        operationMap = new HashMap();
        operationMap.put(CommonConstants.JNDI, "AgriTermLoanJNDI");
        operationMap.put(CommonConstants.HOME, "termloan.agritermloan.AgriTermLoanHome");
        operationMap.put(CommonConstants.REMOTE, "termloan.agritermloan.AgriTermLoan");
    }
    
    private void setBlankKeyValue(){
        key = new ArrayList();
        key.add("");
        value = new ArrayList();
        value.add("");
    }
    
    private void getKeyValue(HashMap keyValue)  throws Exception{
        key = (ArrayList)keyValue.get(CommonConstants.KEY);
        value = (ArrayList)keyValue.get(CommonConstants.VALUE);
    }
    
    /**
     * To retrieve the data from database to populate in UI based on
     * Borrower Number, Account Number
     * @param whereMap is the HashMap having where conditions
     */
    public void populateData(HashMap whereMap, AuthorizedSignatoryOB authOB, PowerOfAttorneyOB poaOB) {
        log.info("In populateData..."+whereMap);
        HashMap mapData = null;
        try {
            whereMap.put("UI_PRODUCT_TYPE", "TL");
            mapData =  (HashMap) proxy.executeQuery(whereMap, operationMap);
            log.info("proxy.executeQuery is working fine");
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
        System.out.println("populateOb###3"+mapData);
        // Populate the Tabs on the Basis of Borrower Number or Account Number
        if ((mapData.get("KEY_VALUE").equals("BORROWER_NUMBER")) || (mapData.get("KEY_VALUE").equals("AUTHORIZE"))){
            // Populate the Tabs on the Basis of Borrower Number
            if (((List) mapData.get("AgriTermLoanBorrowerTO")).size() > 0){
                // Populate the Borrower Tab if the corresponding record is existing in Database
                setBorrowerNo(((AgriTermLoanBorrowerTO) ((List) mapData.get("AgriTermLoanBorrowerTO")).get(0)).getBorrowNo());
                termLoanBorrowerOB.setTermLoanBorrowerTO((AgriTermLoanBorrowerTO) ((List) mapData.get("AgriTermLoanBorrowerTO")).get(0));
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
                termLoanBorrowerOB.setTermLoanJointAcctTO((ArrayList) (mapData.get("AgriTermLoanJointAcctTO")));
            }
            if (((List) mapData.get("AgriTermLoanCompanyTO")).size() > 0){
                // Populate the Company Tab if the corresponding record is existing in Database
                termLoanCompanyOB.setTermLoanCompanyTO((AgriTermLoanCompanyTO) ((List) mapData.get("AgriTermLoanCompanyTO")).get(0));
            }
            //switch  over into fetching basing on acctnum
            //            termLoanAuthorizedSignatoryOB.setAuthorizedSignatoryTO((ArrayList) (mapData.get("AuthorizedSignatoryTO")), getBorrowerNo());
            //            termLoanAuthorizedSignatoryInstructionOB.setAuthorizedSignatoryInstructionTO((ArrayList) (mapData.get("AuthorizedSignatoryInstructionTO")), getBorrowerNo());
            //            termLoanPoAOB.setTermLoanPowerAttorneyTO((ArrayList) (mapData.get("PowerAttorneyTO")), getBorrowerNo());
            //end switch over
            sanctionList = (ArrayList) (mapData.get("AgriTermLoanSanctionTO"));
            sanctionFacilityList = (ArrayList) (mapData.get("AgriTermLoanSanctionFacilityTO"));
            setTermLoanSanctionTO(sanctionList, sanctionFacilityList);
            tableUtilSanction_Main.setAllValues(sanctionMainAll);
            tableUtilSanction_Main.setTableValues(sanctionMainAllTabRecords);
            setMax_Del_San_Details_No(getBorrowerNo());
            
            facilityList = (ArrayList) (mapData.get("AgriTermLoanFacilityTO"));
            setTermLoanFacilityTO(facilityList);
            if(loanType.equals("OTHERS"))
                setShareTab(mapData);
            queryMap = null;
        }
        if ((mapData.get("KEY_VALUE").equals("ACCOUNT_NUMBER")) || (mapData.get("KEY_VALUE").equals("AUTHORIZE"))){
            // Populate the Tabs on the Basis of Account Number
            // To populate Facility details at the time of authorization
            if (mapData.get("KEY_VALUE").equals("AUTHORIZE")){
                ArrayList sanctionAuthorizeList = (ArrayList) (mapData.get("AgriTermLoanSanctionTO.AUTHORIZE"));
                setTermLoanSanctionTOForAuthorize(sanctionAuthorizeList);
                ArrayList sanctionFacilityAuthorizeList = (ArrayList) (mapData.get("AgriTermLoanSanctionFacilityTO.AUTHORIZE"));
                setTermLoanSanctionFacilityTOForAuthorize(sanctionFacilityAuthorizeList);
                facilityList = (ArrayList) (mapData.get("AgriTermLoanFacilityTO.AUTHORIZE"));
                // The return HashMap contains "sanctionNo" as key and
                // corresponding sanction number as value
                HashMap transactionMap = setTermLoanFacilityByAcctNo(facilityList);
                // To get the sanction details and sanction limit
                List resultList = (List) ClientUtil.executeQuery("getAgriSanctionDetails", transactionMap);
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
                getPLR_Rate(getLblProductID_FD_Disp());
                transactionMap = null;
                resultList = null;
                sanctionAuthorizeList = null;
                sanctionFacilityAuthorizeList = null;
            }
            termLoanSecurityOB.resetSecurityDetails();
            if(loanType.equals("OTHERS")) {
                termLoanAuthorizedSignatoryOB.setAuthorizedSignatoryTO((ArrayList) (mapData.get("AuthorizedSignatoryTO")), getStrACNumber());
                termLoanAuthorizedSignatoryInstructionOB.setAuthorizedSignatoryInstructionTO((ArrayList) (mapData.get("AuthorizedSignatoryInstructionTO")), getStrACNumber());
                termLoanPoAOB.setTermLoanPowerAttorneyTO((ArrayList) (mapData.get("PowerAttorneyTO")),getStrACNumber());
                
                //            termLoanSecurityOB.setTblSecurityTab(new EnhancedTableModel(null, termLoanSecurityOB.getSecurityTabTitle()));
                termLoanSecurityOB.getTblSecurityTab().setDataArrayList(null, termLoanSecurityOB.getSecurityTabTitle());
                //            else
                //                termLoanSecurityOB.getTblSecurityTab().setDataArrayList(null, termLoanSecurityOB.getDepositSecurityTabTitle());
                termLoanSecurityOB.setTermLoanSecurityTO((ArrayList) (mapData.get("AgriTermLoanSecurityTO")), getStrACNumber(), CommonUtil.convertObjToStr(getCbmTypeOfFacility().getKeyForSelected()));
            }
            //            if(loanType.equals("OTHERS"))
            //                termLoanSecurityOB.getTblSecurityTab().setDataArrayList(null, termLoanSecurityOB.getSecurityTabTitle());
            //            termLoanSecurityOB.setTermLoanSecurityTO((ArrayList) (mapData.get("TermLoanSecurityTO")), getStrACNumber(), CommonUtil.convertObjToStr(getCbmTypeOfFacility().getKeyForSelected()));
            // Populate the Tabs on the Basis of Account Number
            termLoanRepaymentOB.resetRepaymentSchedule();
            termLoanRepaymentOB.getTblRepaymentTab().setDataArrayList(null, termLoanRepaymentOB.getRepaymentTabTitle());
            termLoanRepaymentOB.setTermLoanRepaymentTO((ArrayList) (mapData.get("AgriTermLoanRepaymentTO")), getStrACNumber());
            // Populate the Tabs on the Basis of Account Number
            termLoanGuarantorOB.resetGuarantorDetails();
            termLoanGuarantorOB.getTblGuarantorTab().setDataArrayList(null, termLoanGuarantorOB.getGuarantorTabTitle());
            termLoanGuarantorOB.setTermLoanGuarantorTO((ArrayList) (mapData.get("AgriTermLoanGuarantorTO")), getStrACNumber());
            // Populate the Tabs on the Basis of Account Number
            termLoanInterestOB.resetInterestDetails();
            termLoanInterestOB.getTblInterestTab().setDataArrayList(null, termLoanInterestOB.getInterestTabTitle());
            System.out.println("### setDetailsForLTD - depositCustDetMap 2 : "+depositCustDetMap);
            if(loanType.equals("LTD")) {
                termLoanInterestOB.setLoanType(loanType);
                //                termLoanInterestOB.setDepositNo(CommonUtil.convertObjToStr(depositCustDetMap.get("DEPOSIT_NO")));
            }
            termLoanInterestOB.setTermLoanInterestTO((ArrayList) (mapData.get("AgriTermLoanInterestTO")), getStrACNumber());
            if (((List) mapData.get("AgriTermLoanClassificationTO")).size() > 0){
                // Populate the Classification Tab if the corresponding record is existing in Database
                termLoanClassificationOB.setTermLoanClassificationTO((AgriTermLoanClassificationTO) ((List) mapData.get("AgriTermLoanClassificationTO")).get(0));
                termLoanClassificationOB.setClassifiDetails(CommonConstants.TOSTATUS_UPDATE);
            }else{
                termLoanClassificationOB.setClassifiDetails(CommonConstants.TOSTATUS_INSERT);
            }
            
            if (((List) mapData.get("AgriTermLoanOtherDetailsTO")).size() > 0){
                // Populate the Other Details Tab if the corresponding record is existing in Database
                termLoanOtherDetailsOB.setTermLoanOtherDetailsTO((AgriTermLoanOtherDetailsTO) ((List) mapData.get("AgriTermLoanOtherDetailsTO")).get(0));
                termLoanOtherDetailsOB.setOtherDetailsMode(CommonConstants.TOSTATUS_UPDATE);
            }else{
                termLoanOtherDetailsOB.setOtherDetailsMode(CommonConstants.TOSTATUS_INSERT);
            }
            
            // Populate the Tabs on the Basis of Account Number
            termLoanDocumentDetailsOB.resetDocumentDetails();
            termLoanDocumentDetailsOB.getTblDocumentTab().setDataArrayList(null, termLoanDocumentDetailsOB.getDocumentTabTitle());
            termLoanDocumentDetailsOB.setTermLoanDocumentTO((ArrayList) (mapData.get("AgriTermLoanDocumentTO")));
            
            //             Populate the Tabs on the Basis of Account Number
            agriSubLimitOB.resetFormComponets();
            agriSubLimitOB.notifyObservers();
            agriSubLimitOB.getTblInspectionDetails().setDataArrayList(null, agriSubLimitOB.setInspectionTableTitle());
            agriSubLimitOB.setSubLimitDetailsTO((ArrayList) (mapData.get("AgriSubLimitDetailsTO")),getStrACNumber());
            //
            agriSubLimitOB.getTblSubLimit().setDataArrayList(null, agriSubLimitOB.setSubLimitTableTitle());
            agriSubLimitOB.setSubLimitTO((ArrayList) (mapData.get("AgriSubLimitTO")),getStrACNumber(),(ArrayList) (mapData.get("AgriSubLimitDetailsTO")));
            
            //             Populate the Tabs on the Basis of Account Number
            agriSubLimitOB.resetFormComponets();
            
            //insepection details
            agriInspectionOB.getTblInspectionDetails().setDataArrayList(null, agriSubLimitOB.setInspectionTableTitle());
            agriInspectionOB.setInspectionTO((ArrayList) (mapData.get("AgriInspectionTO")),getStrACNumber()); //  insepection purpose
            resetInsuranceDetails();
            tblInsuranceDetails.setDataArrayList(null, insuranceTitle);
            setInsuranceDetails((ArrayList)(mapData.get("AgriTermLoanInsuranceTO")),getStrACNumber());
            
            //populate the subsidy && valution
            agriSubSidyOB.resetFormComponets();
            agriSubSidyOB.resetSubsidyDetails();
            agriSubSidyOB.notifyObservers();
            agriSubSidyOB.getTblSubsidyDetails().setDataArrayList(null, agriSubSidyOB.setSubSidyTableTitle());
            agriSubSidyOB.setSubSidyTO((ArrayList) (mapData.get("AgriSubSidyTO")),getStrACNumber());
            agriSubSidyOB.getTblValution().setDataArrayList(null, agriSubSidyOB.setSubLimitTableTitle());
            agriSubSidyOB.setValutionTO((ArrayList) (mapData.get("AgriValutionTO")),getStrACNumber());
            
            
        }
        sanctionList = null;
        sanctionFacilityList = null;
        facilityList = null;
        ttNotifyObservers();
    }
    public int getSubLimitTableRecord(){
        return agriSubLimitOB.getTblSubLimit().getDataArrayList().size();
    }
    
    public int getInspectionTableRecord(){
        return agriSubLimitOB.getTblInspectionDetails().getDataArrayList().size();
    }
    
    public int getValutionTableRecord(){
        return agriSubSidyOB.getTblValution().getDataArrayList().size();
    }
    public int getSubSidyTableRecord(){
        return agriSubSidyOB.getTblSubsidyDetails().getDataArrayList().size();
        
    }
     public int getInspectionTableRecords(){
        return agriInspectionOB.getTblInspectionDetails().getDataArrayList().size();
        
    }
    public int getAuthorizeSigantoryRecord(){
        return termLoanAuthorizedSignatoryOB.getTblAuthorized().getDataArrayList().size();
    }
    public int getPOARecord(){
        return termLoanPoAOB.getTblPoA().getDataArrayList().size();
    }
    public void resetSubLimitInsureance(){
        agriSubLimitOB.getTblInspectionDetails().setDataArrayList(null, agriSubLimitOB.setInspectionTableTitle());
        agriSubLimitOB.getTblSubLimit().setDataArrayList(null, agriSubLimitOB.setSubLimitTableTitle());
        tblInsuranceDetails.setDataArrayList(null, insuranceTitle);
    }
    private void setTermLoanSanctionTO(ArrayList objSanctionTO, ArrayList objSanctionFacilityTO){
        try{
            AgriTermLoanSanctionTO objSanction;
            AgriTermLoanSanctionFacilityTO objSanctionFacility;
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
                objSanction = (AgriTermLoanSanctionTO) objSanctionTO.get(j);
                sanctionTabRec = new ArrayList();
                removedValuesSanctionFacility = new ArrayList();
                sanctionRecord = new HashMap();
                sanctionFacilityTableValues = new ArrayList();
                sanctionFacilityAllValues = new LinkedHashMap();
                
                sanctionTabRec.add(CommonUtil.convertObjToStr(objSanction.getSlNo()));
                sanctionTabRec.add(CommonUtil.convertObjToStr(objSanction.getSanctionNo()));
                sanctionTabRec.add(CommonUtil.convertObjToStr(getCbmSanctioningAuthority().getDataForKey(objSanction.getSanctionAuthority())));
                sanctionTabRec.add(CommonUtil.convertObjToStr(getCbmModeSanction().getDataForKey(objSanction.getSanctionMode())));
                sanctionMainAllTabRecords.add(sanctionTabRec);
                
                sanctionRecord.put(SANCTION_NO,objSanction.getSanctionNo());
                sanctionRecord.put(SANCTION_SL_NO,CommonUtil.convertObjToStr(objSanction.getSlNo()));
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
                    objSanctionFacility = (AgriTermLoanSanctionFacilityTO) objSanctionFacilityTO.get(m);
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
                        
                        sanctionFacilityLocalRecord.put(SANCTION_NO, objSanctionFacility.getSanctionNo());
                        sanctionFacilityLocalRecord.put(SLNO, CommonUtil.convertObjToStr(objSanctionFacility.getSlNo()));
                        sanctionFacilityLocalRecord.put(FACILITY_TYPE, objSanctionFacility.getFacilityType());
                        sanctionFacilityLocalRecord.put(LIMIT, CommonUtil.convertObjToStr(objSanctionFacility.getLimit()));
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
                List resultList = ClientUtil.executeQuery("getSelectAgriTermLoanSanctionFacilityMaxSLNO", transactionMap);
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
                
                sanctionMainAll.put(CommonUtil.convertObjToStr(objSanction.getSlNo()), sanctionRecord);
                
                sanctionFacilityTableValues = null;
                sanctionFacilityAllValues = null;
                sanctionTabRec = null;
                sanctionMaxDelSlNo = null;
            }
            System.out.println("sanctionMainAll  ####"+sanctionMainAll);
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
    public int addInsuranceData(int row,boolean update){
        ArrayList data=(ArrayList)tblInsuranceDetails.getDataArrayList();
        //        int option=-1;
        tblInsuranceDetails.setDataArrayList(data,insuranceTitle);
        int dataSize=data.size();
        singleInsList=new ArrayList();
        singleInsMap=new HashMap();
        HashMap resultMap=new HashMap();
        
        if(!update){
            insertinsuranceDetails(dataSize+1);
            resultMap=insDetailsTab.insertTableValues(singleInsList,singleInsMap);
            allInsMap=(LinkedHashMap)resultMap.get("ALL_VALUES");
            allInsList=(ArrayList)resultMap.get("TABLE_VALUES");
            tblInsuranceDetails.setDataArrayList(allInsList,insuranceTitle);
            option=CommonUtil.convertObjToInt(resultMap.get("OPTION"));
            
        }else{
            insertinsuranceDetails(row+1);
            option=updateInsuranceDetails(row);
        }
        return option;
    }
    public void setInsuranceDetails(ArrayList insuranceList,String strAct_num){
        ArrayList singleList=new ArrayList();
        ArrayList totalList=new ArrayList();
        insDetailsTab=new TableUtil();
        LinkedHashMap totalMap=new LinkedHashMap();
        HashMap singleMap=new HashMap();
        if(insuranceList !=null && (!insuranceList.isEmpty())){
            for(int i=0;i<insuranceList.size();i++){
                AgriTermLoanInsuranceTO agriTermLoanInsuranceTO=new AgriTermLoanInsuranceTO();
                agriTermLoanInsuranceTO=(AgriTermLoanInsuranceTO)insuranceList.get(i);
                singleList=new ArrayList();
                singleMap=new HashMap();
                singleList.add(CommonUtil.convertObjToStr(agriTermLoanInsuranceTO.getSlno()));
                singleList.add(CommonUtil.convertObjToStr(agriTermLoanInsuranceTO.getInsuranceDetails()));
                singleList.add(CommonUtil.convertObjToStr(agriTermLoanInsuranceTO.getInsurnaceUnderScheme()));
                singleList.add(CommonUtil.convertObjToStr(agriTermLoanInsuranceTO.getInsuranceCompany()));
                totalList.add(singleList);
                singleMap.put("SLNO",CommonUtil.convertObjToStr(agriTermLoanInsuranceTO.getSlno()));
                singleMap.put("INSDETAILS",CommonUtil.convertObjToStr(agriTermLoanInsuranceTO.getInsuranceDetails()));//)CboInsDetails());
                singleMap.put("INSUNDERSCHEME",CommonUtil.convertObjToStr(agriTermLoanInsuranceTO.getInsurnaceUnderScheme()));//getCboInsUnderScheme());
                singleMap.put("INSCOMNAME", CommonUtil.convertObjToStr(agriTermLoanInsuranceTO.getInsuranceCompany()));
                singleMap.put("ADDRESS",CommonUtil.convertObjToStr(agriTermLoanInsuranceTO.getAddress()));
                singleMap.put("PHONENO",CommonUtil.convertObjToStr(agriTermLoanInsuranceTO.getPhoneNo()));
                singleMap.put("POLICYNO",CommonUtil.convertObjToStr(agriTermLoanInsuranceTO.getPolicyNo()));
                singleMap.put("ACCT_NUM",getStrACNumber());
                singleMap.put("INSISSUEDT",CommonUtil.convertObjToStr(agriTermLoanInsuranceTO.getFromAppliedDt()));
                singleMap.put("INSFROMDT",CommonUtil.convertObjToStr(agriTermLoanInsuranceTO.getPolicyDt()));
                singleMap.put("INSEXPIRYDT",CommonUtil.convertObjToStr(agriTermLoanInsuranceTO.getExpiryDt()));
                singleMap.put("INSURANCEAMT",CommonUtil.convertObjToStr(agriTermLoanInsuranceTO.getPolicyAmt()));
                singleMap.put("PREMIUMAMT",CommonUtil.convertObjToStr(agriTermLoanInsuranceTO.getPremiumAmt()));
                singleMap.put("NATUREOFRISKCOVERD",CommonUtil.convertObjToStr(agriTermLoanInsuranceTO.getRiskNature()));//getCboNatureOfRiskCoverd());
                singleMap.put("DEBITPRODTYPE",CommonUtil.convertObjToStr(agriTermLoanInsuranceTO.getDebitProdType()));//getCboDebitProdType());
                singleMap.put("DEBITPRODID",CommonUtil.convertObjToStr(agriTermLoanInsuranceTO.getDebitProdId()));//getCboDebitProdId());
                singleMap.put("FROMACTNO",CommonUtil.convertObjToStr(agriTermLoanInsuranceTO.getDebitAcctNum()));
                singleMap.put("FROMACTNO1", CommonUtil.convertObjToStr(agriTermLoanInsuranceTO.getRemarks()));
                singleMap.put("COMMAND","UPDATE");
                totalMap.put(agriTermLoanInsuranceTO.getSlno(),singleMap);
            }
            allInsList=totalList;
            allInsMap=totalMap;
            tblInsuranceDetails.setDataArrayList(allInsList, insuranceTitle);
            insDetailsTab.setAllValues(allInsMap);
            insDetailsTab.setTableValues(allInsList);
            totalList=null;
            totalMap=null;
        }
        ttNotifyObservers();
    }
    public void setCbmProdId(String prodType) {
        if (CommonUtil.convertObjToStr(prodType).length()>1) {
            if (prodType.equals("GL")) {
                key = new ArrayList();
                value = new ArrayList();
                key.add("");
                value.add("");
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
        cbmDebitProdId = new ComboBoxModel(key,value);
        this.cbmDebitProdId = cbmDebitProdId;
        setChanged();
    }
    private void insertinsuranceDetails(int size){
        singleInsList.add(String.valueOf(size));
        singleInsList.add(getCboInsDetails());
        singleInsList.add(getCboInsUnderScheme());
        singleInsList.add(getInsComName());
        
        singleInsMap.put("SLNO",String.valueOf(size));
        singleInsMap.put("INSDETAILS",CommonUtil.convertObjToStr(getCbmInsDetails().getKeyForSelected()));//)CboInsDetails());
        singleInsMap.put("INSUNDERSCHEME",CommonUtil.convertObjToStr(getCbmInsUnderScheme().getKeyForSelected()));//getCboInsUnderScheme());
        singleInsMap.put("INSCOMNAME",getInsComName());
        singleInsMap.put("ADDRESS",getAddress());
        singleInsMap.put("PHONENO",getPhoneNo());
        singleInsMap.put("POLICYNO",getPolicyNo());
        singleInsMap.put("ACCT_NUM",getStrACNumber());
        singleInsMap.put("INSISSUEDT",CommonUtil.convertObjToStr(getInsIssueDt()));
        singleInsMap.put("INSFROMDT",CommonUtil.convertObjToStr(getInsStDt()));
        singleInsMap.put("INSEXPIRYDT",CommonUtil.convertObjToStr(getInsExpiryDt()));
        singleInsMap.put("INSURANCEAMT",getInsuranceAmt());
        singleInsMap.put("PREMIUMAMT",getPremiumAmt());
        singleInsMap.put("NATUREOFRISKCOVERD",CommonUtil.convertObjToStr(getCbmNatureOfRiskCoverd().getKeyForSelected()));//getCboNatureOfRiskCoverd());
        singleInsMap.put("DEBITPRODTYPE",CommonUtil.convertObjToStr(getCbmDebitProdType().getKeyForSelected()));//getCboDebitProdType());
        singleInsMap.put("DEBITPRODID",CommonUtil.convertObjToStr(getCbmDebitProdId().getKeyForSelected()));//getCboDebitProdId());
        singleInsMap.put("FROMACTNO",getFromActNo());
        singleInsMap.put("FROMACTNO1",getFromActNo1());
        singleInsMap.put("COMMAND","");
    }
    private int updateInsuranceDetails(int row){
        //        int option =-1;
        HashMap resultMap =new HashMap();
        resultMap=insDetailsTab.updateTableValues(singleInsList, singleInsMap, row);
        allInsMap=(LinkedHashMap)resultMap.get("ALL_VALUES");
        allInsList=(ArrayList)resultMap.get("TABLE_VALUES");
        tblInsuranceDetails.setDataArrayList(allInsList,insuranceTitle);
        option=CommonUtil.convertObjToInt(resultMap.get("OPTION"));
        
        return option;
    }
    public int deleteInsuranceDetails(int row){
        HashMap resultMap=new HashMap();
        int option=-1;
        resultMap=insDetailsTab.deleteTableValues(row);
        allInsMap=(LinkedHashMap)resultMap.get("ALL_VALUES");
        allInsList=(ArrayList)resultMap.get("TABLE_VALUES");
        tblInsuranceDetails.setDataArrayList(allInsList,insuranceTitle);
        option=CommonUtil.convertObjToInt(resultMap.get("OPTION"));
        
        return option;
        
    }
    public void setSelectInsuranceDetails(int row){
        ArrayList data=(ArrayList)tblInsuranceDetails.getDataArrayList().get(row);
        java.util.Set keySet=allInsMap.keySet();
        Object objkeySet[]=(Object[])keySet.toArray();
        for(int i=0;i<allInsMap.size();i++){
            if(((HashMap)allInsMap.get(objkeySet[i])).get("SLNO").equals(data.get(0))){
                HashMap singleMap=(HashMap)allInsMap.get(objkeySet[i]);
                setCboInsDetails(CommonUtil.convertObjToStr(singleMap.get("INSDETAILS")));
                setCboInsUnderScheme(CommonUtil.convertObjToStr(singleMap.get("INSUNDERSCHEME")));
                setCboDebitProdType(CommonUtil.convertObjToStr(singleMap.get("DEBITPRODTYPE")));
                setCboDebitProdId(CommonUtil.convertObjToStr(singleMap.get("DEBITPRODID")));
                setCboNatureOfRiskCoverd(CommonUtil.convertObjToStr(singleMap.get("NATUREOFRISKCOVERD")));
                setInsComName(CommonUtil.convertObjToStr(singleMap.get("INSCOMNAME")));
                setAddress(CommonUtil.convertObjToStr(singleMap.get("ADDRESS")));
                setPhoneNo(CommonUtil.convertObjToStr(singleMap.get("PHONENO")));
                
                setPolicyNo(CommonUtil.convertObjToStr(singleMap.get("POLICYNO")));
                setPremiumAmt(CommonUtil.convertObjToStr(singleMap.get("PREMIUMAMT")));
                setInsIssueDt(CommonUtil.convertObjToStr(singleMap.get("INSISSUEDT")));
                setInsStDt(CommonUtil.convertObjToStr(singleMap.get("INSFROMDT")));
                setInsExpiryDt(CommonUtil.convertObjToStr(singleMap.get("INSEXPIRYDT")));
                setInsuranceAmt(CommonUtil.convertObjToStr(singleMap.get("INSURANCEAMT")));
                setFromActNo(CommonUtil.convertObjToStr(singleMap.get("FROMACTNO")));
                setFromActNo1(CommonUtil.convertObjToStr(singleMap.get("FROMACTNO1")));
                break;
            }
        }
        setChanged();
        ttNotifyObservers();
    }
    private void setShareTab(HashMap mapData){
        
        ArrayList shareTableList=new ArrayList();
        ArrayList jointAccount=(ArrayList)mapData.get("TermLoanJointAcctTO");
        if(jointAccount !=null && jointAccount.size()>0){
            for(int i=0;i<jointAccount.size();i++){
                AgriTermLoanJointAcctTO termloanJoint=(AgriTermLoanJointAcctTO)jointAccount.get(i);
                String cust_id=termloanJoint.getCustId();
                shareTableList.add(getShareDetails(cust_id));
            }
        }else{
            String cust_id= termLoanBorrowerOB.getTxtCustID();
            shareTableList.add(getShareDetails(cust_id));
            
        }
        if(! ((ArrayList)shareTableList.get(0)).isEmpty()){
            ArrayList facilityList = (ArrayList) (mapData.get("AgriTermLoanFacilityTO"));
            if(facilityList !=null && facilityList.size()>0){
                for(int t=0;t<facilityList.size();t++){
                    ArrayList finalshareTab=new ArrayList();
                    AgriTermLoanFacilityTO  objTermLoanFacilityTO = (AgriTermLoanFacilityTO) facilityList.get(t);
                    ArrayList actnum=new ArrayList();CommonUtil.convertObjToStr(objTermLoanFacilityTO.getAcctNum());
                    actnum.add(CommonUtil.convertObjToStr(objTermLoanFacilityTO.getAcctNum()));
                    for(int k=0;k<shareTableList.size();k++){
                        ArrayList list=(ArrayList)shareTableList.get(k);
                        finalshareTab.add(list.get(0));
                        finalshareTab.add(list.get(1));
                        finalshareTab.add(actnum.get(0));
                        finalshareTab.add(list.get(2));
                        finalshareTab.add(list.get(3));
                        finalshareTab.add(list.get(4));
                        finalshareTab.add(list.get(5));
                        System.out.println("finalshareTab####"+finalshareTab+"shareTitle#3"+shareTitle);
                        shareTableList.set(k, finalshareTab);
                        finalshareTab=new ArrayList();
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
            List resultList = ClientUtil.executeQuery("getSelectAgriTermLoanSanctionMaxSLNO", transactionMap);
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
            AgriTermLoanSanctionTO objTermLoanSanctionTO;
            objTermLoanSanctionTO = (AgriTermLoanSanctionTO) objSanctionTOList.get(0);
            
            setTxtSanctionNo(objTermLoanSanctionTO.getSanctionNo());
            setTxtSanctionSlNo(CommonUtil.convertObjToStr(objTermLoanSanctionTO.getSlNo()));
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
            AgriTermLoanSanctionFacilityTO objTermLoanSanctionFacilityTO;
            objTermLoanSanctionFacilityTO = (AgriTermLoanSanctionFacilityTO) objSanctionFacilityTOList.get(0);
            setSanctionSlNo(CommonUtil.convertObjToStr(objTermLoanSanctionFacilityTO.getSlNo()));
            cbmRepayFreq.setKeyForSelected(CommonUtil.convertObjToStr(objTermLoanSanctionFacilityTO.getRepaymentFrequency()));
            setLblProductID_FD_Disp(objTermLoanSanctionFacilityTO.getProductId());
            populateRestofProdId_AccHead();
            getCbmTypeOfFacility().setKeyForSelected(CommonUtil.convertObjToStr(objTermLoanSanctionFacilityTO.getFacilityType()));
            setCboTypeOfFacility(CommonUtil.convertObjToStr(getCbmTypeOfFacility().getDataForKey(CommonUtil.convertObjToStr(objTermLoanSanctionFacilityTO.getFacilityType()))));
            // To set the product ids based on Type of facility
            setFacilityProductID(CommonUtil.convertObjToStr(objTermLoanSanctionFacilityTO.getFacilityType()));
            setTxtLimit_SD(CommonUtil.convertObjToStr(objTermLoanSanctionFacilityTO.getLimit()));
            termLoanInterestOB.setLblLimitAmt_2(getTxtLimit_SD());
            setTdtFDate(DateUtil.getStringDate(objTermLoanSanctionFacilityTO.getFromDt()));
            setTdtTDate(DateUtil.getStringDate(objTermLoanSanctionFacilityTO.getToDt()));
            if (CommonUtil.convertObjToStr(objTermLoanSanctionFacilityTO.getMoratoriumGiven()).equals("Y")){
                setChkMoratorium_Given(true);
            }else if (CommonUtil.convertObjToStr(objTermLoanSanctionFacilityTO.getMoratoriumGiven()).equals("N")){
                setChkMoratorium_Given(false);
            }
            setTdtFacility_Repay_Date(DateUtil.getStringDate(objTermLoanSanctionFacilityTO.getRepaymentDt()));
            setTxtFacility_Moratorium_Period(CommonUtil.convertObjToStr(objTermLoanSanctionFacilityTO.getNoMoratorium()));
            termLoanInterestOB.setLblExpiryDate_2(getTdtTDate());
            setTxtNoInstallments(CommonUtil.convertObjToStr(objTermLoanSanctionFacilityTO.getNoInstall()));
            setCboRepayFreq(CommonUtil.convertObjToStr(getCbmRepayFreq().getDataForKey(CommonUtil.convertObjToStr(objTermLoanSanctionFacilityTO.getRepaymentFrequency()))));
            getCbmProductId().setKeyForSelected(CommonUtil.convertObjToStr(objTermLoanSanctionFacilityTO.getProductId()));
            //            setCboProductId(CommonUtil.convertObjToStr(getCbmProductId().getDataForKey(CommonUtil.convertObjToStr(objTermLoanSanctionFacilityTO.getProductId()))));
            getPLR_Rate(CommonUtil.convertObjToStr(getCbmProductId().getKeyForSelected()));
        }catch(Exception e){
            log.info("Exception caught in setTermLoanSanctionFacilityTOForAuthorize: "+e);
            parseException.logException(e,true);
        }
    }
    
    private void setTermLoanFacilityTO(ArrayList objFacilityTOList){
        try{
            AgriTermLoanFacilityTO objTermLoanFacilityTO;
            HashMap facilityRecord;
            LinkedHashMap allLocalRecords = new LinkedHashMap();
            int serialNo;
            // To retrieve the Facility Details from the Database
            for (int i = objFacilityTOList.size() - 1,j = 0;i >= 0;--i,++j){
                objTermLoanFacilityTO = (AgriTermLoanFacilityTO) objFacilityTOList.get(j);
                facilityRecord = new HashMap();
                facilityRecord.put(SANCTION_NO, objTermLoanFacilityTO.getSanctionNo());
                facilityRecord.put(SLNO, CommonUtil.convertObjToStr(objTermLoanFacilityTO.getSlNo()));
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
                facilityRecord.put(ACCT_STATUS, objTermLoanFacilityTO.getAcctStatus());
                facilityRecord.put(ACCT_NAME, objTermLoanFacilityTO.getAcctName());
                facilityRecord.put(ACCT_NUM, objTermLoanFacilityTO.getAcctNum());
                facilityRecord.put(PURPOSE_DESC, objTermLoanFacilityTO.getPurposeDesc());
                facilityRecord.put(GROUP_DESC, objTermLoanFacilityTO.getGroupDesc());
                facilityRecord.put(INTEREST, CommonUtil.convertObjToStr(objTermLoanFacilityTO.getInterest()));
                facilityRecord.put(CONTACT_PERSON, objTermLoanFacilityTO.getContactPerson());
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
                facilityRecord.put(ACCT_OPEN_DT, objTermLoanFacilityTO.getAccOpenDt());
                facilityRecord.put(AVAILABLE_BALANCE, objTermLoanFacilityTO.getAvailableBalance());// ADD BY BALA
                facilityRecord.put(CARD_TYPE, objTermLoanFacilityTO.getCardType());
                facilityRecord.put(INSEPECTION, objTermLoanFacilityTO.getInspection());
                
                facilityRecord.put(CARD_PERIOD ,objTermLoanFacilityTO.getCardPeriod());
                facilityRecord.put(CARD_LIMIT ,objTermLoanFacilityTO.getCardLimit());
                facilityRecord.put(REVIEW_DT ,objTermLoanFacilityTO.getReviewDate());
                
                facilityRecord.put(AUTHSIGNATORY, CommonUtil.convertObjToStr(objTermLoanFacilityTO.getAuthorizedSignatory()));
                facilityRecord.put(DOCDETAILS, CommonUtil.convertObjToStr(objTermLoanFacilityTO.getDocDetails()));
                facilityRecord.put(POFATTORNEY, CommonUtil.convertObjToStr(objTermLoanFacilityTO.getPofAttorney()));
                facilityRecord.put(SHADOW_DEBIT, objTermLoanFacilityTO.getShadowDebit());
                facilityRecord.put(SHADOW_CREDIT, objTermLoanFacilityTO.getShadowCredit());
                facilityRecord.put(CLEAR_BALANCE, objTermLoanFacilityTO.getClearBalance());
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
            AgriTermLoanFacilityTO objTermLoanFacilityTO;
            HashMap facilityRecord;
            //            LinkedHashMap allLocalRecords = new LinkedHashMap();
            String sanctionNo = null;
            int serialNo = -1;
            // To retrieve the Facility Details from the Database
            if (objFacilityTOList.size() > 0){
                objTermLoanFacilityTO = (AgriTermLoanFacilityTO) objFacilityTOList.get(0);
                facilityRecord = new HashMap();
                facilityRecord.put(SANCTION_NO, objTermLoanFacilityTO.getSanctionNo());
                returnMap.put("sanctionNo", objTermLoanFacilityTO.getSanctionNo());
                facilityRecord.put(SLNO, objTermLoanFacilityTO.getSlNo());
                returnMap.put("slNo", objTermLoanFacilityTO.getSlNo());
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
                facilityRecord.put(RECOMMANDED_BY,objTermLoanFacilityTO.getRecommendedBy());
                facilityRecord.put(ACCT_OPEN_DT,objTermLoanFacilityTO.getAccOpenDt());
                facilityRecord.put(DOCDETAILS,objTermLoanFacilityTO.getDocDetails());
                facilityRecord.put(POFATTORNEY,objTermLoanFacilityTO.getPofAttorney());
                facilityRecord.put(AUTHSIGNATORY,objTermLoanFacilityTO.getAuthorizedSignatory());
                facilityRecord.put(CARD_TYPE ,objTermLoanFacilityTO.getCardType());
                facilityRecord.put(CARD_PERIOD ,objTermLoanFacilityTO.getCardPeriod());
                facilityRecord.put(CARD_LIMIT ,objTermLoanFacilityTO.getCardLimit());
                facilityRecord.put(REVIEW_DT ,objTermLoanFacilityTO.getReviewDate());
                
                
                facilityRecord.put(COMMAND, UPDATE);
                sanctionNo = objTermLoanFacilityTO.getSanctionNo();
                serialNo = CommonUtil.convertObjToInt(objTermLoanFacilityTO.getSlNo());
                setStrACNumber(objTermLoanFacilityTO.getAcctNum());
                setCboAccStatus(CommonUtil.convertObjToStr(getCbmAccStatus().getDataForKey(facilityRecord.get(ACCT_STATUS))));
                setCboRecommendedByType(CommonUtil.convertObjToStr(getCbmRecommendedByType().getDataForKey(facilityRecord.get(RECOMMANDED_BY))));
                setAccountOpenDate(CommonUtil.convertObjToStr(facilityRecord.get(ACCT_OPEN_DT)));
                setCboIntGetFrom(CommonUtil.convertObjToStr(getCbmIntGetFrom().getDataForKey(facilityRecord.get(INT_GET_FROM))));
                setCardType(objTermLoanFacilityTO.getProdId());
                
                setCboCardType(CommonUtil.convertObjToStr(getCbmCardType().getDataForKey(facilityRecord.get(CARD_TYPE))));
                setTxtCardLimit(CommonUtil.convertObjToStr(facilityRecord.get(CARD_LIMIT)));
                setTdtReviewDate(CommonUtil.convertObjToStr(facilityRecord.get(REVIEW_DT)));
                
                setTxtContactPerson(CommonUtil.convertObjToStr(facilityRecord.get(CONTACT_PERSON)));
                setTxtContactPhone(CommonUtil.convertObjToStr(facilityRecord.get(CONTACT_PHONE)));
                setTxtRemarks(CommonUtil.convertObjToStr(facilityRecord.get(REMARKS)));
                setTxtPurposeDesc(CommonUtil.convertObjToStr(facilityRecord.get(PURPOSE_DESC)));
                setTxtGroupDesc(CommonUtil.convertObjToStr(facilityRecord.get(GROUP_DESC)));
                setTdtDemandPromNoteDate(CommonUtil.convertObjToStr(facilityRecord.get(NOTE_DATE)));
                setTdtDemandPromNoteExpDate(CommonUtil.convertObjToStr(facilityRecord.get(NOTE_EXP_DATE)));
                setTdtAODDate(CommonUtil.convertObjToStr(facilityRecord.get(AOD_DATE)));
                if (facilityRecord.get(INSEPECTION) !=null && facilityRecord.get(INSEPECTION).equals(YES))
                    setChkInspection(true);
                else
                    setChkInspection(false);
                if (facilityRecord.get(SECURITY_DETAILS).equals(UNSECURED)){
                    setRdoSecurityDetails_Unsec(true);
                }else if (facilityRecord.get(SECURITY_DETAILS).equals(PARTLY_SECURED)){
                    setRdoSecurityDetails_Partly(true);
                }else if (facilityRecord.get(SECURITY_DETAILS).equals(FULLY_SECURED)){
                    setRdoSecurityDetails_Fully(true);
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
                
                if (facilityRecord.get(INTEREST).equals(SIMPLE)){
                    setRdoInterest_Simple(true);
                }else if (facilityRecord.get(INTEREST).equals(COMPOUND)){
                    setRdoInterest_Compound(true);
                }
                
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
                termLoanSecurityOB.setLblAccNoSec_2(objTermLoanFacilityTO.getAcctNum());
                termLoanOtherDetailsOB.setLblAcctNo_Disp_ODetails(objTermLoanFacilityTO.getAcctNum());
                termLoanClassificationOB.setLblSanctionNo2(sanctionNo);
                setStrACNumber(objTermLoanFacilityTO.getAcctNum());
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
    public  boolean checkSubLimitDetails(String mainLimit){
        if(getStrACNumber().length() > 0){
        StringBuffer buf=agriSubLimitOB.checkSubLimitDetails(mainLimit);
        if(buf !=null && buf.length()>0) {
            ClientUtil.showMessageWindow(buf.toString());
            return false;
        }}
        return true;
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
        HashMap objSantionDetailsTOMap = setTermLoanSanction();
        HashMap objTermLoanSanctionTOMap = (HashMap) objSantionDetailsTOMap.get(SANCTION);
        HashMap objTermLoanSanctionFacilityTOMap = (HashMap) objSantionDetailsTOMap.get(SANCTION_FACILITY);
        HashMap objTermLoanJointAcctTOMap = termLoanBorrowerOB.setTermLoanJointAcct();
        AgriTermLoanBorrowerTO objTermLoanBorrowerTO = termLoanBorrowerOB.setTermLoanBorrower();
        AgriTermLoanCompanyTO objTermLoanCompanyTO = termLoanCompanyOB.setTermLoanCompany();
        HashMap objTermLoanAuthorizedSignatoryTOList = termLoanAuthorizedSignatoryOB.setAuthorizedSignatory();
        HashMap objTermLoanSubLimitTOList =  agriSubLimitOB.setAgriSubLimitTo();
         
        HashMap objTermLoanSubLimitDetailTOList =  agriSubLimitOB.getResultSubLimitDetailsMap();//setAgriSubLimitDetailsTO();
        HashMap objTermLoanValutionTOList =  agriSubSidyOB.setAgriValutionTo();
        HashMap objTermLoanSubSidyTOList =  agriSubSidyOB.setAgriSubSidyTo();
        HashMap objTermLoanInspectionTOList =  agriInspectionOB.setAgriInspectionTo();
        HashMap objTermLoanInuranceDetailList=setTermLoanInsuranceDetails();
        HashMap objTermLoanAuthorizedSignatoryInstructionTOList = termLoanAuthorizedSignatoryInstructionOB.setAuthorizedSignatoryInstruction();
        HashMap objTermLoanPowerAttorneyTOMap = termLoanPoAOB.setTermLoanPowerAttorney();
        HashMap objTermLoanFacilityTOMap = setTermLoanFacility();
        HashMap objTermLoanSecurityTOMap = termLoanSecurityOB.setTermLoanSecurity();
        HashMap objRepaymentInstallmentMap = termLoanRepaymentOB.setTermLoanRepayment();
        System.out.println("printsavemode"+saveMode);
        //        if(saveMode==1){
        //            System.out.println("checkthis"+termLoanRepaymentOB.getRepaymentEachRecord().get(INSTALLMENT_ALL_DETAILS));
        //         objRepaymentInstallmentAllMap=(LinkedHashMap)((HashMap)termLoanRepaymentOB.getRepaymentEachRecord()).get(INSTALLMENT_ALL_DETAILS);
        //        System.out.println("thisisforcheckob"+objRepaymentInstallmentAllMap);
        //        }
        HashMap objTermLoanRepaymentTOMap = (HashMap) objRepaymentInstallmentMap.get(REPAYMENT_DETAILS);
        HashMap objTermLoanInstallmentTOMap = (HashMap) objRepaymentInstallmentMap.get(INSTALLMENT_DETAILS);
        HashMap objTermLoanInstallMultIntMap = (HashMap) objRepaymentInstallmentMap.get(INSTALLMULTINT_DETAILS);
        HashMap objTermLoanGuarantorTOMap = termLoanGuarantorOB.setTermLoanGuarantor();
        HashMap objTermLoanDocumentTOMap = termLoanDocumentDetailsOB.setTermLoanDocument();
        HashMap objTermLoanInterestTOMap = termLoanInterestOB.setTermLoanInterest();
        AgriTermLoanClassificationTO objTermLoanClassificationTO = termLoanClassificationOB.setTermLoanClassification();
        
        AgriTermLoanOtherDetailsTO objTermLoanOtherDetailsTO = termLoanOtherDetailsOB.setTermLoanOtherDetails();
        if (getCommand().equals(DELETE) && !canBorrowerDelete) {
            objTermLoanBorrowerTO.setCommand(UPDATE);
            objTermLoanCompanyTO.setCommand(UPDATE);
        } else {
            objTermLoanBorrowerTO.setCommand(getCommand());
            objTermLoanCompanyTO.setCommand(getCommand());
        }
        System.out.println("getCommand(): "+getCommand());
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
        data.put("AgriSubLimitTO",objTermLoanSubLimitTOList);
        data.put("AgriSubLimitDetailTO",objTermLoanSubLimitDetailTOList);
        data.put("AgriValutionTO",objTermLoanValutionTOList);
        data.put("AgriSubsidyTO",objTermLoanSubSidyTOList);
        data.put("AgriInspectionTO",objTermLoanInspectionTOList);
        data.put("AgriTermLoanInsuranceTO",objTermLoanInuranceDetailList);
        data.put("AuthorizedSignatoryInstructionTO",objTermLoanAuthorizedSignatoryInstructionTOList);
        data.put("PowerAttorneyTO", objTermLoanPowerAttorneyTOMap);
        data.put("TermLoanSanctionTO", objTermLoanSanctionTOMap);
        data.put("TermLoanSanctionFacilityTO", objTermLoanSanctionFacilityTOMap);
        data.put("TermLoanFacilityTO", objTermLoanFacilityTOMap);
        data.put("TermLoanSecurityTO", objTermLoanSecurityTOMap);
        data.put("TermLoanRepaymentTO", objTermLoanRepaymentTOMap);
        data.put("TermLoanInstallmentTO", objTermLoanInstallmentTOMap);
        data.put("TermLoanInstallMultIntTO", objTermLoanInstallMultIntMap);
        data.put("TermLoanGuarantorTO", objTermLoanGuarantorTOMap);
        data.put("TermLoanInterestTO", objTermLoanInterestTOMap);
        data.put("TermLoanDocumentTO", objTermLoanDocumentTOMap);
        data.put("TermLoanClassificationTO", objTermLoanClassificationTO);
        if(advanceLiablityMap !=null && (!advanceLiablityMap.isEmpty()))
            data.put("AdvancesLiablityExceedLimit",advanceLiablityMap);
        if(getCommand().equals("UPDATE")){
            if(termLoanClassificationOB.getListAssetStatus() !=null)
                
                data.put("NPAHISTORY",termLoanClassificationOB.getListAssetStatus());
        }
        //        if(saveMode==1){
        //        data.put("TermRepaymentInstallmentAllTO",objRepaymentInstallmentAllMap);
        //        System.out.println("goingDAO"+data.get("TermRepaymentInstallmentAllTO"));
        //        }
        
        String facilityType = CommonUtil.convertObjToStr(getCbmTypeOfFacility().getKeyForSelected());
        if (facilityType.equals("ACC") || facilityType.equals("AOD")){
            data.put("TermLoanOtherDetailsTO", objTermLoanOtherDetailsTO);
        }else{
            data.put("TermLoanOtherDetailsTO", null);
        }
        
        if (getCommand().equals(CommonConstants.TOSTATUS_INSERT)) {
            if (loanType.equals("LTD")) {
                data.put("LTD",depositCustDetMap);
            }
        }
        
        if (getAuthorizeMap() != null){
            data.put(CommonConstants.AUTHORIZEMAP, getAuthorizeMap());
        }
        
        data.put(CommonConstants.MODULE, getModule());
        data.put(CommonConstants.SCREEN, getScreen());
        data.put("UI_PRODUCT_TYPE", "TL");
        System.out.print("dataonly#######"+data);
        HashMap proxyResultMap = new HashMap();
        proxyResultMap = proxy.execute(data,operationMap);
        System.out.println("proxyresultmap#$$$$$"+proxyResultMap);
        if (proxyResultMap!=null) {
            if(proxyResultMap.containsKey("ACCTNO") && loanType.equals("OTHERS"))
                ClientUtil.showMessageWindow("Loan Account No. : "+proxyResultMap.get("ACCTNO"));
            //            loanACNo = CommonUtil.convertObjToStr(proxyResultMap.get("ACCTNO"));
            if (proxyResultMap.size() > 0) {
                if (proxyResultMap.containsKey("LIENNO")) {
                    String lienNo = CommonUtil.convertObjToStr(proxyResultMap.get("LIENNO"));
                    loanACNo = CommonUtil.convertObjToStr(proxyResultMap.get("ACCTNO"));
                    //                setStrACNumber(loanACNo);
                    ClientUtil.showMessageWindow("Lien Marked for this Loan\nLien No : " + lienNo
                    + "\nThe Lien will be opened for Edit...");
                    HashMap lienMap = new HashMap();
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
            termLoanSecurityOB.changeStatusSecurity(getResult());
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
            objTermLoanSecurityTOMap = null;
            objRepaymentInstallmentMap = null;
            objTermLoanRepaymentTOMap = null;
            objTermLoanInstallmentTOMap = null;
            objTermLoanGuarantorTOMap = null;
            objTermLoanDocumentTOMap = null;
            objTermLoanInstallMultIntMap = null;
            objTermLoanInterestTOMap = null;
            objTermLoanClassificationTO = null;
            objTermLoanOtherDetailsTO = null;
            objRepaymentInstallmentAllMap=null;
            data = null;
        }
    }
    private HashMap setTermLoanInsuranceDetails(){
        HashMap resultMap=new HashMap();
        ArrayList deleteList=insDetailsTab.getRemovedValues();
        AgriTermLoanInsuranceTO agriTermLoanInsuranceTO=null;
        HashMap singleMap=new HashMap();
        HashMap deleteMap=new HashMap();
        if(allInsMap !=null){
            java.util.Set keySet=allInsMap.keySet();
            Object objKeySet[]=(Object[])keySet.toArray();
            for(int i=0;i<allInsMap.size();i++){
                singleMap=(HashMap) allInsMap.get(objKeySet[i]);
                agriTermLoanInsuranceTO=new AgriTermLoanInsuranceTO();
                agriTermLoanInsuranceTO.setInsuranceDetails(CommonUtil.convertObjToStr(singleMap.get("INSDETAILS")));
                agriTermLoanInsuranceTO.setInsuranceCompany(CommonUtil.convertObjToStr(singleMap.get("INSCOMNAME")));
                agriTermLoanInsuranceTO.setInsurnaceUnderScheme(CommonUtil.convertObjToStr(singleMap.get("INSUNDERSCHEME")));
                agriTermLoanInsuranceTO.setPolicyNo(CommonUtil.convertObjToStr(singleMap.get("POLICYNO")));
                agriTermLoanInsuranceTO.setPolicyAmt(CommonUtil.convertObjToDouble(singleMap.get("INSURANCEAMT")));
                agriTermLoanInsuranceTO.setAddress(CommonUtil.convertObjToStr(singleMap.get("ADDRESS")));
                agriTermLoanInsuranceTO.setPhoneNo(CommonUtil.convertObjToStr(singleMap.get("PHONENO")));
                agriTermLoanInsuranceTO.setPolicyNo(CommonUtil.convertObjToStr(singleMap.get("POLICYNO")));
                agriTermLoanInsuranceTO.setPolicyDt(DateUtil.getDateMMDDYYYY(CommonUtil.convertObjToStr(singleMap.get("INSISSUEDT"))));
                agriTermLoanInsuranceTO.setFromAppliedDt(DateUtil.getDateMMDDYYYY(CommonUtil.convertObjToStr(singleMap.get("INSFROMDT"))));
                agriTermLoanInsuranceTO.setPremiumAmt(CommonUtil.convertObjToDouble(singleMap.get("PREMIUMAMT")));
                agriTermLoanInsuranceTO.setExpiryDt(DateUtil.getDateMMDDYYYY(CommonUtil.convertObjToStr(singleMap.get("INSEXPIRYDT"))));
                agriTermLoanInsuranceTO.setRiskNature(CommonUtil.convertObjToStr(singleMap.get("NATUREOFRISKCOVERD")));
                //            agriTermLoanInsuranceTO.setInsuranceCompany(CommonUtil.convertObjToStr(singleMap.get("INSCOMNAME")));
                agriTermLoanInsuranceTO.setAcctNum(CommonUtil.convertObjToStr(singleMap.get("ACCT_NUM")));
                agriTermLoanInsuranceTO.setCommand(CommonUtil.convertObjToStr(singleMap.get("COMMAND")));
                
                if(singleMap.get("COMMAND").equals("INSERT"))
                    agriTermLoanInsuranceTO.setStatus(CommonConstants.STATUS_CREATED);
                if(singleMap.get("COMMAND").equals("UPDATE"))
                    agriTermLoanInsuranceTO.setStatus(CommonConstants.STATUS_MODIFIED);
                agriTermLoanInsuranceTO.setSlno(CommonUtil.convertObjToStr(singleMap.get("SLNO")));
                agriTermLoanInsuranceTO.setDebitAcctNum(CommonUtil.convertObjToStr(singleMap.get("FROMACTNO")));
                agriTermLoanInsuranceTO.setDebitProdType(CommonUtil.convertObjToStr(singleMap.get("DEBITPRODTYPE")));
                agriTermLoanInsuranceTO.setDebitProdId(CommonUtil.convertObjToStr(singleMap.get("DEBITPRODID")));
                agriTermLoanInsuranceTO.setRemarks(CommonUtil.convertObjToStr(singleMap.get("FROMACTNO1")));
                resultMap.put(agriTermLoanInsuranceTO.getSlno(),agriTermLoanInsuranceTO);
            }
            singleMap=null;
        }
        //delete list
        if(deleteList!=null)
            for(int i=0;i<deleteList.size();i++){
                deleteMap=(HashMap) deleteList.get(i);
                agriTermLoanInsuranceTO=new AgriTermLoanInsuranceTO();
                agriTermLoanInsuranceTO.setInsuranceDetails(CommonUtil.convertObjToStr(deleteMap.get("INSDETAILS")));
                agriTermLoanInsuranceTO.setInsuranceCompany(CommonUtil.convertObjToStr(deleteMap.get("INSCOMNAME")));
                agriTermLoanInsuranceTO.setInsurnaceUnderScheme(CommonUtil.convertObjToStr(deleteMap.get("INSUNDERSCHEME")));
                agriTermLoanInsuranceTO.setPolicyNo(CommonUtil.convertObjToStr(deleteMap.get("POLICYNO")));
                agriTermLoanInsuranceTO.setPolicyAmt(CommonUtil.convertObjToDouble(deleteMap.get("INSURANCEAMT")));
                agriTermLoanInsuranceTO.setAddress(CommonUtil.convertObjToStr(deleteMap.get("ADDRESS")));
                agriTermLoanInsuranceTO.setPhoneNo(CommonUtil.convertObjToStr(deleteMap.get("PHONENO")));
                agriTermLoanInsuranceTO.setPolicyNo(CommonUtil.convertObjToStr(deleteMap.get("POLICYNO")));
                agriTermLoanInsuranceTO.setPolicyDt(DateUtil.getDateMMDDYYYY(CommonUtil.convertObjToStr(deleteMap.get("INSISSUEDT"))));
                agriTermLoanInsuranceTO.setFromAppliedDt(DateUtil.getDateMMDDYYYY(CommonUtil.convertObjToStr(deleteMap.get("INSFROMDT"))));
                agriTermLoanInsuranceTO.setPremiumAmt(CommonUtil.convertObjToDouble(deleteMap.get("PREMIUMAMT")));
                agriTermLoanInsuranceTO.setExpiryDt(DateUtil.getDateMMDDYYYY(CommonUtil.convertObjToStr(deleteMap.get("INSEXPIRYDT"))));
                agriTermLoanInsuranceTO.setRiskNature(CommonUtil.convertObjToStr(deleteMap.get("NATUREOFRISKCOVERD")));
                //            agriTermLoanInsuranceTO.setInsuranceCompany(CommonUtil.convertObjToStr(singleMap.get("INSCOMNAME")));
                agriTermLoanInsuranceTO.setAcctNum(CommonUtil.convertObjToStr(deleteMap.get("ACCT_NUM")));
                agriTermLoanInsuranceTO.setCommand(CommonUtil.convertObjToStr(deleteMap.get("COMMAND")));
                agriTermLoanInsuranceTO.setStatus(CommonConstants.STATUS_DELETED);
                agriTermLoanInsuranceTO.setSlno(CommonUtil.convertObjToStr(deleteMap.get("SLNO")));
                agriTermLoanInsuranceTO.setDebitAcctNum(CommonUtil.convertObjToStr(deleteMap.get("FROMACTNO")));
                agriTermLoanInsuranceTO.setDebitProdType(CommonUtil.convertObjToStr(deleteMap.get("DEBITPRODTYPE")));
                agriTermLoanInsuranceTO.setDebitProdId(CommonUtil.convertObjToStr(deleteMap.get("DEBITPRODID")));
                agriTermLoanInsuranceTO.setRemarks(CommonUtil.convertObjToStr(deleteMap.get("FROMACTNO1")));
                resultMap.put(agriTermLoanInsuranceTO.getSlno(),agriTermLoanInsuranceTO);
            }
        
        return resultMap;
    }
    private HashMap setTermLoanSanction(){
        HashMap returnValue = new HashMap();
        try{
            AgriTermLoanSanctionTO objTermLoanSanctionTO;
            AgriTermLoanSanctionFacilityTO objTermLoanSanctionFacilityTO;
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
                objTermLoanSanctionTO = new AgriTermLoanSanctionTO();
                objTermLoanSanctionTO.setBorrowNo(borrowerNo);
                objTermLoanSanctionTO.setSanctionNo(CommonUtil.convertObjToStr( eachSanctionRec.get(SANCTION_NO)));
                objTermLoanSanctionTO.setSlNo(CommonUtil.convertObjToDouble( eachSanctionRec.get(SANCTION_SL_NO)));
                objTermLoanSanctionTO.setSanctionAuthority(CommonUtil.convertObjToStr( eachSanctionRec.get(SANCTION_AUTHORITY)));
                
                //                objTermLoanSanctionTO.setSanctionDt((Date)eachSanctionRec.get(SANCTION_DATE));
                objTermLoanSanctionTO.setSanctionDt(DateUtil.getDateMMDDYYYY(CommonUtil.convertObjToStr( eachSanctionRec.get(SANCTION_DATE))));
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
                        if (objTermLoanSanctionTO.getSanctionNo().equals(getTxtSanctionNo()) &&
                        objTermLoanSanctionTO.getSlNo().doubleValue() == CommonUtil.convertObjToDouble(getTxtSanctionSlNo()).doubleValue()) {
                            objTermLoanSanctionTO.setStatus(CommonConstants.STATUS_DELETED);
                            objTermLoanSanctionTO.setCommand(getCommand());
                            isSanctionDeleted = true;
                        }
                
                sanctionFacilityKeySet =  sanctionFacilityRecs.keySet();
                objSanctionFacilityKeySet = (Object[]) sanctionFacilityKeySet.toArray();
                // To set the values for Sanction Facility Transfer Object
                for (int k = sanctionFacilityRecs.size() - 1, m = 0;k >= 0;--k,++m){
                    eachSanctionFacilityRec = (HashMap) sanctionFacilityRecs.get(objSanctionFacilityKeySet[m]);
                    objTermLoanSanctionFacilityTO = new AgriTermLoanSanctionFacilityTO();
                    sanctionFacilityKey++;
                    objTermLoanSanctionFacilityTO.setBorrowNo(borrowerNo);
                    objTermLoanSanctionFacilityTO.setSanctionNo(CommonUtil.convertObjToStr( eachSanctionFacilityRec.get(SANCTION_NO)));
                    objTermLoanSanctionFacilityTO.setSlNo(CommonUtil.convertObjToDouble( eachSanctionFacilityRec.get(SLNO)));
                    objTermLoanSanctionFacilityTO.setFacilityType(CommonUtil.convertObjToStr( eachSanctionFacilityRec.get(FACILITY_TYPE)));
                    objTermLoanSanctionFacilityTO.setLimit(CommonUtil.convertObjToDouble(eachSanctionFacilityRec.get(LIMIT)));
                    
                    //                    objTermLoanSanctionFacilityTO.setFromDt((Date)eachSanctionFacilityRec.get(FROM));
                    objTermLoanSanctionFacilityTO.setFromDt(DateUtil.getDateMMDDYYYY(CommonUtil.convertObjToStr( eachSanctionFacilityRec.get(FROM))));
                    
                    //                    objTermLoanSanctionFacilityTO.setToDt((Date)eachSanctionFacilityRec.get(TO));
                    objTermLoanSanctionFacilityTO.setToDt(DateUtil.getDateMMDDYYYY(CommonUtil.convertObjToStr( eachSanctionFacilityRec.get(TO))));
                    objTermLoanSanctionFacilityTO.setNoInstall(CommonUtil.convertObjToDouble(eachSanctionFacilityRec.get(NO_INSTALLMENTS)));
                    objTermLoanSanctionFacilityTO.setRepaymentFrequency(CommonUtil.convertObjToDouble(eachSanctionFacilityRec.get(REPAY_FREQ)));
                    objTermLoanSanctionFacilityTO.setProductId(CommonUtil.convertObjToStr( eachSanctionFacilityRec.get(PROD_ID)));
                    objTermLoanSanctionFacilityTO.setMoratoriumGiven(CommonUtil.convertObjToStr(eachSanctionFacilityRec.get(MORATORIUM_GIVEN)));
                    
                    //                    objTermLoanSanctionFacilityTO.setRepaymentDt((Date)eachSanctionFacilityRec.get(FACILITY_REPAY_DATE));
                    objTermLoanSanctionFacilityTO.setRepaymentDt(DateUtil.getDateMMDDYYYY(CommonUtil.convertObjToStr(eachSanctionFacilityRec.get(FACILITY_REPAY_DATE))));
                    objTermLoanSanctionFacilityTO.setNoMoratorium(CommonUtil.convertObjToDouble(eachSanctionFacilityRec.get(MORATORIUM_PERIOD)));
                    objTermLoanSanctionFacilityTO.setCommand(CommonUtil.convertObjToStr( eachSanctionFacilityRec.get(COMMAND)));
                    if (eachSanctionFacilityRec.get(COMMAND).equals(INSERT)){
                        objTermLoanSanctionFacilityTO.setStatus(CommonConstants.STATUS_CREATED);
                    }else if (eachSanctionFacilityRec.get(COMMAND).equals(UPDATE)){
                        objTermLoanSanctionFacilityTO.setStatus(CommonConstants.STATUS_MODIFIED);
                    }
                    if (getCommand().equals(CommonConstants.TOSTATUS_DELETE))
                        if (objTermLoanSanctionFacilityTO.getSanctionNo().equals(getTxtSanctionNo()) &&
                        objTermLoanSanctionFacilityTO.getSlNo().doubleValue() == CommonUtil.convertObjToDouble(getSanctionSlNo()).doubleValue()) {
                            objTermLoanSanctionFacilityTO.setStatus(CommonConstants.STATUS_DELETED);
                            objTermLoanSanctionFacilityTO.setCommand(getCommand());
                        }
                    objTermLoanSanctionFacilityTO.setStatusBy(TrueTransactMain.USER_ID);
                    objTermLoanSanctionFacilityTO.setStatusDt(curDate);
                    sanctionFacilityMap.put(String.valueOf(sanctionFacilityKey), objTermLoanSanctionFacilityTO);
                    eachSanctionFacilityRec = null;
                    objTermLoanSanctionFacilityTO = null;
                }
                
                sanctionMap.put(eachSanctionRec.get(SANCTION_SL_NO), objTermLoanSanctionTO);
                
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
                objTermLoanSanctionTO = new AgriTermLoanSanctionTO();
                objTermLoanSanctionTO.setBorrowNo(borrowerNo);
                objTermLoanSanctionTO.setSanctionNo(CommonUtil.convertObjToStr( eachSanctionRec.get(SANCTION_NO)));
                objTermLoanSanctionTO.setSlNo(CommonUtil.convertObjToDouble( eachSanctionRec.get(SANCTION_SL_NO)));
                objTermLoanSanctionTO.setSanctionAuthority(CommonUtil.convertObjToStr( eachSanctionRec.get(SANCTION_AUTHORITY)));
                
                //                objTermLoanSanctionTO.setSanctionDt((Date)eachSanctionRec.get(SANCTION_DATE));
                objTermLoanSanctionTO.setSanctionDt(DateUtil.getDateMMDDYYYY(CommonUtil.convertObjToStr( eachSanctionRec.get(SANCTION_DATE))));
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
                    objTermLoanSanctionFacilityTO = new AgriTermLoanSanctionFacilityTO();
                    sanctionFacilityKey++;
                    objTermLoanSanctionFacilityTO.setBorrowNo(borrowerNo);
                    objTermLoanSanctionFacilityTO.setSanctionNo(CommonUtil.convertObjToStr( eachSanctionFacilityRec.get(SANCTION_NO)));
                    objTermLoanSanctionFacilityTO.setSlNo(CommonUtil.convertObjToDouble( eachSanctionFacilityRec.get(SLNO)));
                    objTermLoanSanctionFacilityTO.setFacilityType(CommonUtil.convertObjToStr( eachSanctionFacilityRec.get(FACILITY_TYPE)));
                    objTermLoanSanctionFacilityTO.setLimit(CommonUtil.convertObjToDouble(eachSanctionFacilityRec.get(LIMIT)));
                    //                    objTermLoanSanctionFacilityTO.setFromDt((Date)eachSanctionFacilityRec.get(FROM));
                    objTermLoanSanctionFacilityTO.setFromDt(DateUtil.getDateMMDDYYYY(CommonUtil.convertObjToStr( eachSanctionFacilityRec.get(FROM))));
                    
                    //                    objTermLoanSanctionFacilityTO.setToDt((Date)eachSanctionFacilityRec.get(TO));
                    objTermLoanSanctionFacilityTO.setToDt(DateUtil.getDateMMDDYYYY(CommonUtil.convertObjToStr( eachSanctionFacilityRec.get(TO))));
                    objTermLoanSanctionFacilityTO.setNoInstall(CommonUtil.convertObjToDouble(eachSanctionFacilityRec.get(NO_INSTALLMENTS)));
                    objTermLoanSanctionFacilityTO.setRepaymentFrequency(CommonUtil.convertObjToDouble(eachSanctionFacilityRec.get(REPAY_FREQ)));
                    objTermLoanSanctionFacilityTO.setProductId(CommonUtil.convertObjToStr( eachSanctionFacilityRec.get(PROD_ID)));
                    objTermLoanSanctionFacilityTO.setMoratoriumGiven(CommonUtil.convertObjToStr(eachSanctionFacilityRec.get(MORATORIUM_GIVEN)));
                    
                    //                    objTermLoanSanctionFacilityTO.setRepaymentDt((Date)eachSanctionFacilityRec.get(FACILITY_REPAY_DATE));
                    objTermLoanSanctionFacilityTO.setRepaymentDt(DateUtil.getDateMMDDYYYY(CommonUtil.convertObjToStr(eachSanctionFacilityRec.get(FACILITY_REPAY_DATE))));
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
                objTermLoanSanctionFacilityTO = new AgriTermLoanSanctionFacilityTO();
                sanctionFacilityKey++;
                objTermLoanSanctionFacilityTO.setBorrowNo(borrowerNo);
                objTermLoanSanctionFacilityTO.setSanctionNo(CommonUtil.convertObjToStr( eachSanctionFacilityRec.get(SANCTION_NO)));
                objTermLoanSanctionFacilityTO.setSlNo(CommonUtil.convertObjToDouble( eachSanctionFacilityRec.get(SLNO)));
                objTermLoanSanctionFacilityTO.setFacilityType(CommonUtil.convertObjToStr( eachSanctionFacilityRec.get(FACILITY_TYPE)));
                objTermLoanSanctionFacilityTO.setLimit(CommonUtil.convertObjToDouble(eachSanctionFacilityRec.get(LIMIT)));
                
                //                objTermLoanSanctionFacilityTO.setFromDt((Date)eachSanctionFacilityRec.get(FROM));
                objTermLoanSanctionFacilityTO.setFromDt(DateUtil.getDateMMDDYYYY(CommonUtil.convertObjToStr( eachSanctionFacilityRec.get(FROM))));
                //                objTermLoanSanctionFacilityTO.setToDt((Date)eachSanctionFacilityRec.get(TO));
                objTermLoanSanctionFacilityTO.setToDt(DateUtil.getDateMMDDYYYY(CommonUtil.convertObjToStr( eachSanctionFacilityRec.get(TO))));
                objTermLoanSanctionFacilityTO.setNoInstall(CommonUtil.convertObjToDouble(eachSanctionFacilityRec.get(NO_INSTALLMENTS)));
                objTermLoanSanctionFacilityTO.setRepaymentFrequency(CommonUtil.convertObjToDouble(eachSanctionFacilityRec.get(REPAY_FREQ)));
                objTermLoanSanctionFacilityTO.setProductId(CommonUtil.convertObjToStr( eachSanctionFacilityRec.get(PROD_ID)));
                objTermLoanSanctionFacilityTO.setMoratoriumGiven(CommonUtil.convertObjToStr(eachSanctionFacilityRec.get(MORATORIUM_GIVEN)));
                
                //                objTermLoanSanctionFacilityTO.setRepaymentDt((Date)eachSanctionFacilityRec.get(FACILITY_REPAY_DATE));
                objTermLoanSanctionFacilityTO.setRepaymentDt(DateUtil.getDateMMDDYYYY(CommonUtil.convertObjToStr(eachSanctionFacilityRec.get(FACILITY_REPAY_DATE))));
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
    
    private HashMap setTermLoanFacility(){
        HashMap facilityMap = new HashMap();
        try{
            AgriTermLoanFacilityTO objTermLoanFacilityTO;
            Set keySet =  facilityAll.keySet();
            Object[] objKeySet = (Object[]) keySet.toArray();
            HashMap oneRecord;
            // To set the values for Facility Transfer Object
            for (int i = keySet.size() - 1,j = 0;i >= 0;--i,++j){
                oneRecord = (HashMap) facilityAll.get(objKeySet[j]);
                objTermLoanFacilityTO = new AgriTermLoanFacilityTO();
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
                objTermLoanFacilityTO.setCardType(CommonUtil.convertObjToStr( oneRecord.get("CARD_TYPE")));
                objTermLoanFacilityTO.setInspection(CommonUtil.convertObjToStr(oneRecord.get(INSEPECTION)));
                
                objTermLoanFacilityTO.setCardLimit(CommonUtil.convertObjToDouble(oneRecord.get("CARD_LIMIT")));
                double period=CommonUtil.convertObjToDouble(oneRecord.get("CARD_PERIOD")).doubleValue();
                if(period > 0){
                    //                duration = ((String)cbmPeriodTranSStanAssets.getKeyForSelected());
                }
                periodData = setCombo(String.valueOf(period));
                //                (Double.parseDouble(CommonUtil.convertObjToStr(txtPeriodTranSStanAssets)));
                //                resultData = periodData * (CommonUtil.convertObjToDouble(txtPeriodTranSStanAssets).doubleValue());
                
                objTermLoanFacilityTO.setCardPeriod(CommonUtil.convertObjToDouble(oneRecord.get("CARD_PERIOD")));
                objTermLoanFacilityTO.setReviewDate((Date)oneRecord.get("REVIEW_DT"));
                
                //                objTermLoanFacilityTO.setDemandPromDt((Date)oneRecord.get(NOTE_DATE));
                objTermLoanFacilityTO.setDemandPromDt(DateUtil.getDateMMDDYYYY(CommonUtil.convertObjToStr( oneRecord.get(NOTE_DATE))));
                
                //                objTermLoanFacilityTO.setDemandPromExpdt((Date)oneRecord.get(NOTE_EXP_DATE));
                objTermLoanFacilityTO.setDemandPromExpdt(DateUtil.getDateMMDDYYYY(CommonUtil.convertObjToStr( oneRecord.get(NOTE_EXP_DATE))));
                objTermLoanFacilityTO.setMultiDisburse(CommonUtil.convertObjToStr( oneRecord.get(MULTI_DISBURSE)));
                
                //                objTermLoanFacilityTO.setAodDt((Date)oneRecord.get(AOD_DATE));
                objTermLoanFacilityTO.setAodDt(DateUtil.getDateMMDDYYYY(CommonUtil.convertObjToStr( oneRecord.get(AOD_DATE))));
                objTermLoanFacilityTO.setPurposeDesc(CommonUtil.convertObjToStr( oneRecord.get(PURPOSE_DESC)));
                objTermLoanFacilityTO.setGroupDesc(CommonUtil.convertObjToStr( oneRecord.get(GROUP_DESC)));
                objTermLoanFacilityTO.setInterest(CommonUtil.convertObjToStr( oneRecord.get(INTEREST)));
                objTermLoanFacilityTO.setContactPerson(CommonUtil.convertObjToStr( oneRecord.get(CONTACT_PERSON)));
                objTermLoanFacilityTO.setContactPhone(CommonUtil.convertObjToStr( oneRecord.get(CONTACT_PHONE)));
                objTermLoanFacilityTO.setRemarks(CommonUtil.convertObjToStr( oneRecord.get(REMARKS)));
                objTermLoanFacilityTO.setCommand(CommonUtil.convertObjToStr( oneRecord.get(COMMAND)));
                
                //                objTermLoanFacilityTO.setAccOpenDt((Date)oneRecord.get(ACCT_OPEN_DT));
                objTermLoanFacilityTO.setAccOpenDt(DateUtil.getDateMMDDYYYY(CommonUtil.convertObjToStr(oneRecord.get(ACCT_OPEN_DT))));
                objTermLoanFacilityTO.setRecommendedBy(CommonUtil.convertObjToStr(oneRecord.get(RECOMMANDED_BY)));
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
                    if (objTermLoanFacilityTO.getSanctionNo().equals(getTxtSanctionNo()) &&
                    objTermLoanFacilityTO.getSlNo().doubleValue() == CommonUtil.convertObjToDouble(getSanctionSlNo()).doubleValue()) {
                        objTermLoanFacilityTO.setStatus(CommonConstants.STATUS_DELETED);
                    }
                
                objTermLoanFacilityTO.setAuthorizeBy1(CommonUtil.convertObjToStr(oneRecord.get(AUTHORIZE_BY_1)));
                objTermLoanFacilityTO.setAuthorizeBy2(CommonUtil.convertObjToStr(oneRecord.get(AUTHORIZE_BY_2)));
                
                //                objTermLoanFacilityTO.setAuthorizeDt1((Date)oneRecord.get(AUTHORIZE_DT_1));
                objTermLoanFacilityTO.setAuthorizeDt1(DateUtil.getDateMMDDYYYY(CommonUtil.convertObjToStr(oneRecord.get(AUTHORIZE_DT_1))));
                
                //                objTermLoanFacilityTO.setAuthorizeDt2((Date)oneRecord.get(AUTHORIZE_DT_2));
                objTermLoanFacilityTO.setAuthorizeDt2(DateUtil.getDateMMDDYYYY(CommonUtil.convertObjToStr(oneRecord.get(AUTHORIZE_DT_2))));
                objTermLoanFacilityTO.setAuthorizeStatus1(CommonUtil.convertObjToStr(oneRecord.get(AUTHORIZE_STATUS_1)));
                objTermLoanFacilityTO.setAuthorizeStatus2(CommonUtil.convertObjToStr(oneRecord.get(AUTHORIZE_STATUS_2)));
                
                objTermLoanFacilityTO.setAuthorizedSignatory(CommonUtil.convertObjToStr(oneRecord.get(AUTHSIGNATORY)));
                objTermLoanFacilityTO.setPofAttorney(CommonUtil.convertObjToStr(oneRecord.get(POFATTORNEY)));
                objTermLoanFacilityTO.setDocDetails(CommonUtil.convertObjToStr(oneRecord.get(DOCDETAILS)));
                
                objTermLoanFacilityTO.setStatusBy(TrueTransactMain.USER_ID);
                objTermLoanFacilityTO.setStatusDt(curDate);
                objTermLoanFacilityTO.setBranchId(getSelectedBranchID());
                facilityMap.put(String.valueOf(j+1), objTermLoanFacilityTO);
                oneRecord = null;
                objTermLoanFacilityTO = null;
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
                objTermLoanFacilityTO = new AgriTermLoanFacilityTO();
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
                objTermLoanFacilityTO.setCardType(CommonUtil.convertObjToStr( oneRecord.get("CARD_TYPE")));
                //                objTermLoanFacilityTO.setDemandPromDt((Date)oneRecord.get(NOTE_DATE));
                objTermLoanFacilityTO.setDemandPromDt(DateUtil.getDateMMDDYYYY(CommonUtil.convertObjToStr( oneRecord.get(NOTE_DATE))));
                
                //                objTermLoanFacilityTO.setDemandPromExpdt((Date)oneRecord.get(NOTE_EXP_DATE));
                objTermLoanFacilityTO.setDemandPromExpdt(DateUtil.getDateMMDDYYYY(CommonUtil.convertObjToStr( oneRecord.get(NOTE_EXP_DATE))));
                objTermLoanFacilityTO.setMultiDisburse(CommonUtil.convertObjToStr( oneRecord.get(MULTI_DISBURSE)));
                
                //                objTermLoanFacilityTO.setAodDt((Date)oneRecord.get(AOD_DATE));
                objTermLoanFacilityTO.setAodDt(DateUtil.getDateMMDDYYYY(CommonUtil.convertObjToStr( oneRecord.get(AOD_DATE))));
                objTermLoanFacilityTO.setPurposeDesc(CommonUtil.convertObjToStr( oneRecord.get(PURPOSE_DESC)));
                objTermLoanFacilityTO.setGroupDesc(CommonUtil.convertObjToStr( oneRecord.get(GROUP_DESC)));
                objTermLoanFacilityTO.setInterest(CommonUtil.convertObjToStr( oneRecord.get(INTEREST)));
                objTermLoanFacilityTO.setContactPerson(CommonUtil.convertObjToStr( oneRecord.get(CONTACT_PERSON)));
                objTermLoanFacilityTO.setContactPhone(CommonUtil.convertObjToStr( oneRecord.get(CONTACT_PHONE)));
                objTermLoanFacilityTO.setRemarks(CommonUtil.convertObjToStr( oneRecord.get(REMARKS)));
                objTermLoanFacilityTO.setCommand(CommonUtil.convertObjToStr( oneRecord.get(COMMAND)));
                objTermLoanFacilityTO.setStatus(CommonConstants.STATUS_DELETED);
                objTermLoanFacilityTO.setStatusBy(TrueTransactMain.USER_ID);
                objTermLoanFacilityTO.setStatusDt(curDate);
                objTermLoanFacilityTO.setAuthorizeBy1(CommonUtil.convertObjToStr(oneRecord.get(AUTHORIZE_BY_1)));
                objTermLoanFacilityTO.setAuthorizeBy2(CommonUtil.convertObjToStr(oneRecord.get(AUTHORIZE_BY_2)));
                
                //                objTermLoanFacilityTO.setAuthorizeDt1((Date)oneRecord.get(AUTHORIZE_DT_1));
                objTermLoanFacilityTO.setAuthorizeDt1(DateUtil.getDateMMDDYYYY(CommonUtil.convertObjToStr(oneRecord.get(AUTHORIZE_DT_1))));
                
                //                objTermLoanFacilityTO.setAuthorizeDt2((Date)oneRecord.get(AUTHORIZE_DT_2));
                objTermLoanFacilityTO.setAuthorizeDt2(DateUtil.getDateMMDDYYYY(CommonUtil.convertObjToStr(oneRecord.get(AUTHORIZE_DT_2))));
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
        //insuranceDetails
        resetInsuranceDetails();
        // Reset the tabs based on the Account Number
        resetBasedOnAcctNumber();
        agriSubSidyOB.resetFormComponets();
        agriSubSidyOB.notifyObservers();
        agriSubLimitOB.resetFormComponets();
        agriSubLimitOB.resetFormComponetsSubLimit();
        //        agriSubLimitOB.destroyObjects();
        agriSubLimitOB.notifyObservers();
        destroyObjects();
        createObject();
        ttNotifyObservers();
    }
    private void resetInsuranceDetails(){
        setCboInsDetails("");
        setCboInsUnderScheme("");
        setInsComName("");
        setAddress("");
        setPhoneNo("");
        setPolicyNo("");
        setInsIssueDt("");
        setInsStDt("");
        setInsExpiryDt("");
        setInsuranceAmt("");
        setPremiumAmt("");
        setCboNatureOfRiskCoverd("");
        setCboDebitProdType("");
        setCboDebitProdId("");
        setFromActNo("");
        setFromActNo1("");
    }
    /**
     * This method will reset the tabs which are all dependent to term loan account number
     */
    public void resetBasedOnAcctNumber(){
        // Security Details Table
        termLoanSecurityOB.resetAllSecurityDetails();
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
        // Set Account Number as null
        resetStrAcNumber();
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
        resetStrAcNumber();
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
        setChkAuthorizedSignatory(false);
        setChkDocDetails(false);
        setChkGurantor(false);
        setChkInsurance(false);
        setChkInspection(false);
        setChkPOFAttorney(false);
        setCboCardType("");
        //        setChkGurantor(false);
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
        agriSubSidyOB.setLblStatus(ClientConstants.ACTION_STATUS[this.getActionType()]);
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
        //        termLoanAuthorizedSignatoryOB.setBorrowerNo(borrowerNo);
        //        termLoanAuthorizedSignatoryInstructionOB.setBorrowerNo(borrowerNo);
        //        termLoanPoAOB.setBorrowerNo(borrowerNo);
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
        agriSubLimitOB.setSubLimitStrNo(strACNumber);
        agriInspectionOB.setSubLimitStrNo(strACNumber);
        agriSubSidyOB.setStrAcctNo(strACNumber);
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
                String mapName = "getAgriBorrowerNumber";
                if(loanType.equals("LTD")) {
                    transactionMap.put("LOAN_NO", getLoanACNo());
                    mapName = "getAgriBorrowerNumberForLTD";
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
            
            List resultList1 = ClientUtil.executeQuery("getAgriProdIntDetails", transactionMap);
            List resultList2 = ClientUtil.executeQuery("AgriTermLoan.getProdHead", transactionMap);
            List resultList3 = ClientUtil.executeQuery("AgriTermLoan.getProduct_Details", transactionMap);
            
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
            }
            retrieve = null;
            if (resultList3.size() > 0){
                retrieve = (HashMap) resultList3.get(0);
                if (retrieve.containsKey(SUBSIDY)){
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
    
    public boolean setDetailsForLTD(HashMap hash) {
        try{
            HashMap resultMap = new HashMap();
            HashMap resultCustMap = new HashMap();
            depositCustDetMap = new HashMap();
            depositCustDetMap.putAll(hash);
            System.out.println("### setDetailsForLTD - depositCustDetMap 1 : "+depositCustDetMap);
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
                System.out.println("ternLoanBorrower###"+termLoanBorrowerOB.getTxtCustID());
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
                System.out.println("### setDetailsForLTD - depositCustDetMap 2 : "+depositCustDetMap);
                setTdtTDate(DateUtil.getStringDate((java.util.Date)resultMap.get("MATURITY_DT")));
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
                    days++;
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
            List resultList=null;
            //            List resultList = ClientUtil.executeQuery("TermLoan.getLoanPeriodMultiples", transactionMap);
            if (resultList !=null && resultList.size() > 0){
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
            
            if (!loanType.equals("LTD"))
                periodMultipleMessage();
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
            }else{
                option = updateSanctionFacility(row, updateMain, mainSlNo);
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
            sanctionFacilityRecord.put(SANCTION_NO, getTxtSanctionNo());
            sanctionFacilityRecord.put(FACILITY_TYPE, CommonUtil.convertObjToStr(cbmTypeOfFacility.getKeyForSelected()));
            sanctionFacilityRecord.put(LIMIT, CommonUtil.convertObjToStr(txtLimit_SD));
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
            sanctionFacilityRecord.put(SANCTION_NO, getTxtSanctionNo());
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
            System.out.println("populate sanction facility #####"+sanctionFacilityTableValue);
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
                    agriSubLimitOB.setMainLimit(CommonUtil.convertObjToStr(eachRecs.get(LIMIT)));
                    termLoanInterestOB.setLblLimitAmt_2(getTxtLimit_SD());
                    setTdtFDate(CommonUtil.convertObjToStr(eachRecs.get(FROM)));
                    setTdtTDate(CommonUtil.convertObjToStr(eachRecs.get(TO)));
                    agriSubLimitOB.setLoanExpiryDate(CommonUtil.convertObjToStr(eachRecs.get(TO)));
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
    public void setCardType(String prod_id){
        if(getCboProductId().length()>0 || prod_id.length()>0){
            HashMap map=new HashMap();
            if(getCboProductId().length()>0)
                map.put("PROD_ID",CommonUtil.convertObjToStr(getCbmProductId().getKeyForSelected()));
            else
                map.put("PROD_ID",prod_id);
            List lst =ClientUtil.executeQuery("getAgriCardDetails",map);
            if(lst !=null && lst.size()>0){
                map=(HashMap)lst.get(0);
                setBlankKeyValue();
                key.add(map.get("LOOKUP_REF_ID"));
                value.add(map.get("LOOKUP_DESC"));
                setCbmCardType(new ComboBoxModel(key,value));
            }
        }
    }
    public void getPLR_Rate(String prod_id){
        try{
            HashMap transactionMap = new HashMap();
            HashMap retrieve = new HashMap();
            transactionMap.put("PROD_ID", prod_id);
            List resultList = ClientUtil.executeQuery("getAgriProdIntDetails", transactionMap);
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
            
            List resultList = ClientUtil.executeQuery("AgriTermLoan.getProdID_Behaves", transactionMap);
            
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
                //                System.out.print("hash###"+lt);
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
                        System.out.print("hash###"+hash);
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
                result = tableUtilSanction_Main.insertTableValues(sanctionMainTabRow, sanctionMainRecord, SANCTION_NO);
                
                sanctionMainAllTabRecords = (ArrayList) result.get(TABLE_VALUES);
                sanctionMainAll = (LinkedHashMap)result.get(ALL_VALUES);
                option = CommonUtil.convertObjToInt(result.get(OPTION));
                
                tblSanctionMain.setDataArrayList(sanctionMainAllTabRecords, sanctionMainTitle);
            }else{
                option = updateSanctionMain(row);
            }
            tblSanctionFacility.setDataArrayList(null, sanctionFacilityTitle);
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
                    agriSubLimitOB.setMainSanctionDt(CommonUtil.convertObjToStr(eachRecs.get(SANCTION_DATE)));
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
        int count = 0;
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
    
    public void populateFacilityTabSanction(int sanctionNumberFacility, int sanctionTabNumber){
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
                    if (sanctionNumberFacility == sanctionTabNumber){
                        // if facility table sanction number and
                        // sanction table sanction number are equal
                        facilityTabSanction = sanctionFacilityAllTabRecords;
                    }
                    
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
            String strSanctionNo = CommonUtil.convertObjToStr(((ArrayList)tblSanctionMain.getDataArrayList().get(sanctionSlNo)).get(1));
            sanctionSlNo   = CommonUtil.convertObjToInt(((ArrayList)tblSanctionMain.getDataArrayList().get(sanctionSlNo)).get(0));
            slno           = CommonUtil.convertObjToInt(((ArrayList)tblSanctionFacility.getDataArrayList().get(slno)).get(0));
            String strFacilityKey = getFacilityKey(strSanctionNo, slno);
            insertFacilityDetails(strSanctionNo, slno);
            if (facilityAll.containsKey(strFacilityKey)){
                // If the key already exist in the Linked Hash Map then it status
                // will be changed to UPDATE
                if(getBEHAVES_LIKE().equals("AOD"))
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
    
    private void insertFacilityDetails(String strSanctionNo, int slno)throws Exception{
        
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
        facilityRecord.put(CONTACT_PHONE, getTxtContactPhone());
        facilityRecord.put(REMARKS, getTxtRemarks());
        facilityRecord.put(ACCT_NAME, getTxtAcct_Name());
        facilityRecord.put(INT_GET_FROM, CommonUtil.convertObjToStr(cbmIntGetFrom.getKeyForSelected()));
        facilityRecord.put("CARD_TYPE", CommonUtil.convertObjToStr(cbmCardType.getKeyForSelected()));
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
        
        facilityRecord.put(CARD_LIMIT, getTxtCardLimit());
        facilityRecord.put(REVIEW_DT, DateUtil.getDateMMDDYYYY(getTdtReviewDate()));
        String  duration=CommonUtil.convertObjToStr(getCbmCardPeriod().getKeyForSelected());
        periodData=setCombo(duration);
        if(periodData>0){
            facilityRecord.put(CARD_PERIOD, new Double(CommonUtil.convertObjToDouble(getTxtCardPeriod()).doubleValue()* periodData));
        }
        if (getRdoSecurityDetails_Unsec() == true){
            facilityRecord.put(SECURITY_DETAILS, UNSECURED);
        }else if (getRdoSecurityDetails_Partly() == true){
            facilityRecord.put(SECURITY_DETAILS, PARTLY_SECURED);
        }else if (getRdoSecurityDetails_Fully() == true){
            facilityRecord.put(SECURITY_DETAILS, FULLY_SECURED);
        }else{
            facilityRecord.put(SECURITY_DETAILS, "");
        }
        
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
        
        if(isChkInspection())
            facilityRecord.put(INSEPECTION, YES);
        else
            facilityRecord.put(INSEPECTION, " ");
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
            facilityRecord.put("AUTHSIGNATORY","AUTHSIGNATORY");
        if(isChkDocDetails())
            facilityRecord.put("DOCDETAILS","DOCDETAILS");
        if(isChkPOFAttorney())
            facilityRecord.put("POFATTORNEY","POFATTORNEY");
        
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
            String strFacilityKey = getFacilityKey(strSanctionNo, slno);
            termLoanClassificationOB.setLblSanctionDate2(CommonUtil.convertObjToStr(((HashMap) sanctionMainAll.get(String.valueOf(sanctionSlNo))).get(SANCTION_DATE)));
            termLoanInterestOB.setLblSancDate_2(termLoanClassificationOB.getLblSanctionDate2());
            getPLR_Rate(CommonUtil.convertObjToStr(getCbmProductId().getKeyForSelected()));
            resetFacilityDetails();
            if (facilityAll.containsKey(strFacilityKey)){
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
                agriSubLimitOB.setLoanOpenDt(CommonUtil.convertObjToStr(oneFacilityRec.get(ACCT_OPEN_DT)));
                
                setTdtReviewDate(CommonUtil.convertObjToStr(oneFacilityRec.get(REVIEW_DT)));
                setLast_int_calc_dt(DateUtil.getDateMMDDYYYY(CommonUtil.convertObjToStr(oneFacilityRec.get("LAST_INT_CALC_DT"))));
                setCboRecommendedByType(CommonUtil.convertObjToStr(getCbmRecommendedByType().getDataForKey(CommonUtil.convertObjToStr(oneFacilityRec.get(RECOMMANDED_BY)))));
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
                if (oneFacilityRec.containsKey(POFATTORNEY) && oneFacilityRec.get(POFATTORNEY).equals(POFATTORNEY)){
                    setChkPOFAttorney(true);
                }
                if (oneFacilityRec.containsKey(AUTHSIGNATORY) && oneFacilityRec.get(AUTHSIGNATORY).equals(AUTHSIGNATORY)){
                    setChkAuthorizedSignatory(true);
                }
                if (oneFacilityRec.containsKey(DOCDETAILS) && oneFacilityRec.get(DOCDETAILS).equals(DOCDETAILS)){
                    setChkDocDetails(true);
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
                
                
                  if (oneFacilityRec.get(INSEPECTION) !=null && oneFacilityRec.get(INSEPECTION).equals(YES))
                    setChkInspection(true);
                  else  
                    setChkInspection(false);
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
                
                setCboAccStatus(CommonUtil.convertObjToStr(getCbmAccStatus().getDataForKey(CommonUtil.convertObjToStr(oneFacilityRec.get(ACCT_STATUS)))));
                setCboCardType(CommonUtil.convertObjToStr(getCbmCardType().getDataForKey(CommonUtil.convertObjToStr(oneFacilityRec.get(CARD_TYPE)))));
                
                resultValue =  CommonUtil.convertObjToInt(oneFacilityRec.get(CARD_PERIOD));
                String period = setPeriod(resultValue);
                setCboCardPeriod(period);
                setTxtCardPeriod(String.valueOf(resultValue));
                resetPeriod();
                
                //                setCboCardPeriod(CommonUtil.convertObjToStr(getCbmCardType().getDataForKey(CommonUtil.convertObjToStr(oneFacilityRec.get(CARD_LIMIT)))));
                //                setTxtCardPeriod(CommonUtil.convertObjToStr(getCbmCardType().getDataForKey(CommonUtil.convertObjToStr(oneFacilityRec.get("CARD_TYPE")))));
                
                setTxtCardLimit(CommonUtil.convertObjToStr(oneFacilityRec.get(CARD_LIMIT)));
                
                cbmIntGetFrom.setKeyForSelected(oneFacilityRec.get(INT_GET_FROM));
                setCboIntGetFrom(CommonUtil.convertObjToStr(getCbmIntGetFrom().getDataForKey(CommonUtil.convertObjToStr(oneFacilityRec.get(INT_GET_FROM)))));
                setCboInterestType(CommonUtil.convertObjToStr(getCbmInterestType().getDataForKey(CommonUtil.convertObjToStr(oneFacilityRec.get(INTEREST_TYPE)))));
                setShadowCredit(CommonUtil.convertObjToDouble(oneFacilityRec.get(SHADOW_CREDIT)).doubleValue());
                setShadowDebit(CommonUtil.convertObjToDouble(oneFacilityRec.get(SHADOW_DEBIT)).doubleValue());
                setClearBalance(CommonUtil.convertObjToDouble(oneFacilityRec.get(CLEAR_BALANCE)).doubleValue());
                


                populateAccountNumber(strSanctionNo, slno);
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
                setAccountOpenDate(CommonUtil.convertObjToStr(DateUtil.getDateWithoutMinitues(curDate)));
            }
            if (loanType.equals("LTD")) {
                setRdoSecurityDetails_Fully(true);
                setCboInterestType(CommonUtil.convertObjToStr(getCbmInterestType().getDataForKey(FIXED_RATE)));
                setRdoMultiDisburseAllow_No(true);
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
    
    private void resetPeriod(){
        resultValue = 0;
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
        public void setAccOpenDtForSubLimit(String actopenDt){
        agriSubLimitOB.setLoanOpenDt(actopenDt);
        }
    private void populateRestofProdId_AccHead(){
        try{
            HashMap idTransactionMap = new HashMap();
            HashMap idRetrieve;
            idTransactionMap.put("prodId", getLblProductID_FD_Disp());
            List idResultList = ClientUtil.executeQuery("AgriTermLoan.getProdHead", idTransactionMap);
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
            List resultList = ClientUtil.executeQuery("getCompFreqRoundOff_AgriLoanProd", transactionMap);
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
            List resultList = (List) ClientUtil.executeQuery("getAgriAccountNumber", transactionMap);
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
            System.out.println("#$#$# (DELETE) facilityAll : "+facilityAll);
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
        agriInspectionOB.destroyObjects();
        agriSubSidyOB.destoryObjects();
        tblSanctionFacility = null;
        tblSanctionMain = null;
        tableUtilSanction_Facility = null;
        tableUtilSanction_Main = null;
        facilityTabSanction = null;
        facilityAll = null;
        sanctionMainAllTabRecords = null;
        insDetailsTab=new TableUtil();
        advanceLiablityMap=null;
        allInsList=null;
        allInsMap=null;
        System.gc();
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
        tblInsuranceDetails=new EnhancedTableModel(null,insuranceTitle);
        insDetailsTab=new TableUtil();
        insDetailsTab.setAttributeKey(SLNO);
        tableUtilSanction_Main.setAttributeKey(SANCTION_SL_NO);
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
        System.out.println("mainSanctionArrayList"+mainSanction);
        System.out.println("sanctionDetails##"+sanctionDetails);
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
                        System.out.println("strFacilitykey "+strFacilityKeys+" SlNo "+SlNo+" strSanctionNumber "+strSanctionNumber);
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
        System.out.println("mainSanctionArrayList"+mainSanction);
        System.out.println("sanctionDetails##"+sanctionDetails);
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
                    System.out.println("strFacilitykey "+strFacilityKeys+" SlNo "+SlNo+" strSanctionNumber "+strSanctionNumber);
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
     * Getter for property cbmSelectScreen.
     * @return Value of property cbmSelectScreen.
     */
    public com.see.truetransact.clientutil.ComboBoxModel getCbmSelectScreen() {
        return cbmSelectScreen;
    }
    
    /**
     * Setter for property cbmSelectScreen.
     * @param cbmSelectScreen New value of property cbmSelectScreen.
     */
    public void setCbmSelectScreen(com.see.truetransact.clientutil.ComboBoxModel cbmSelectScreen) {
        this.cbmSelectScreen = cbmSelectScreen;
    }
    
    /**
     * Getter for property cbmInsDetails.
     * @return Value of property cbmInsDetails.
     */
    public com.see.truetransact.clientutil.ComboBoxModel getCbmInsDetails() {
        return cbmInsDetails;
    }
    
    /**
     * Setter for property cbmInsDetails.
     * @param cbmInsDetails New value of property cbmInsDetails.
     */
    public void setCbmInsDetails(com.see.truetransact.clientutil.ComboBoxModel cbmInsDetails) {
        this.cbmInsDetails = cbmInsDetails;
    }
    
    /**
     * Getter for property cbmDebitProdType.
     * @return Value of property cbmDebitProdType.
     */
    public com.see.truetransact.clientutil.ComboBoxModel getCbmDebitProdType() {
        return cbmDebitProdType;
    }
    
    /**
     * Setter for property cbmDebitProdType.
     * @param cbmDebitProdType New value of property cbmDebitProdType.
     */
    public void setCbmDebitProdType(com.see.truetransact.clientutil.ComboBoxModel cbmDebitProdType) {
        this.cbmDebitProdType = cbmDebitProdType;
    }
    
    /**
     * Getter for property cbmDebitProdId.
     * @return Value of property cbmDebitProdId.
     */
    public com.see.truetransact.clientutil.ComboBoxModel getCbmDebitProdId() {
        return cbmDebitProdId;
    }
    
    /**
     * Setter for property cbmDebitProdId.
     * @param cbmDebitProdId New value of property cbmDebitProdId.
     */
    public void setCbmDebitProdId(com.see.truetransact.clientutil.ComboBoxModel cbmDebitProdId) {
        this.cbmDebitProdId = cbmDebitProdId;
    }
    
    /**
     * Getter for property cbmNatureOfRiskCoverd.
     * @return Value of property cbmNatureOfRiskCoverd.
     */
    public com.see.truetransact.clientutil.ComboBoxModel getCbmNatureOfRiskCoverd() {
        return cbmNatureOfRiskCoverd;
    }
    
    /**
     * Setter for property cbmNatureOfRiskCoverd.
     * @param cbmNatureOfRiskCoverd New value of property cbmNatureOfRiskCoverd.
     */
    public void setCbmNatureOfRiskCoverd(com.see.truetransact.clientutil.ComboBoxModel cbmNatureOfRiskCoverd) {
        this.cbmNatureOfRiskCoverd = cbmNatureOfRiskCoverd;
    }
    
    /**
     * Getter for property cbmInsUnderScheme.
     * @return Value of property cbmInsUnderScheme.
     */
    public com.see.truetransact.clientutil.ComboBoxModel getCbmInsUnderScheme() {
        return cbmInsUnderScheme;
    }
    
    /**
     * Setter for property cbmInsUnderScheme.
     * @param cbmInsUnderScheme New value of property cbmInsUnderScheme.
     */
    public void setCbmInsUnderScheme(com.see.truetransact.clientutil.ComboBoxModel cbmInsUnderScheme) {
        this.cbmInsUnderScheme = cbmInsUnderScheme;
    }
    
    /**
     * Getter for property insComName.
     * @return Value of property insComName.
     */
    public java.lang.String getInsComName() {
        return insComName;
    }
    
    /**
     * Setter for property insComName.
     * @param insComName New value of property insComName.
     */
    public void setInsComName(java.lang.String insComName) {
        this.insComName = insComName;
    }
    
    /**
     * Getter for property address.
     * @return Value of property address.
     */
    public java.lang.String getAddress() {
        return address;
    }
    
    /**
     * Setter for property address.
     * @param address New value of property address.
     */
    public void setAddress(java.lang.String address) {
        this.address = address;
    }
    
    /**
     * Getter for property phoneNo.
     * @return Value of property phoneNo.
     */
    public java.lang.String getPhoneNo() {
        return phoneNo;
    }
    
    /**
     * Setter for property phoneNo.
     * @param phoneNo New value of property phoneNo.
     */
    public void setPhoneNo(java.lang.String phoneNo) {
        this.phoneNo = phoneNo;
    }
    
    /**
     * Getter for property policyNo.
     * @return Value of property policyNo.
     */
    public java.lang.String getPolicyNo() {
        return policyNo;
    }
    
    /**
     * Setter for property policyNo.
     * @param policyNo New value of property policyNo.
     */
    public void setPolicyNo(java.lang.String policyNo) {
        this.policyNo = policyNo;
    }
    
    /**
     * Getter for property premiumAmt.
     * @return Value of property premiumAmt.
     */
    public java.lang.String getPremiumAmt() {
        return premiumAmt;
    }
    
    /**
     * Setter for property premiumAmt.
     * @param premiumAmt New value of property premiumAmt.
     */
    public void setPremiumAmt(java.lang.String premiumAmt) {
        this.premiumAmt = premiumAmt;
    }
    
    /**
     * Getter for property insIssueDt.
     * @return Value of property insIssueDt.
     */
    public java.lang.String getInsIssueDt() {
        return insIssueDt;
    }
    
    /**
     * Setter for property insIssueDt.
     * @param insIssueDt New value of property insIssueDt.
     */
    public void setInsIssueDt(java.lang.String insIssueDt) {
        this.insIssueDt = insIssueDt;
    }
    
    /**
     * Getter for property insExpiryDt.
     * @return Value of property insExpiryDt.
     */
    public java.lang.String getInsExpiryDt() {
        return insExpiryDt;
    }
    
    /**
     * Setter for property insExpiryDt.
     * @param insExpiryDt New value of property insExpiryDt.
     */
    public void setInsExpiryDt(java.lang.String insExpiryDt) {
        this.insExpiryDt = insExpiryDt;
    }
    
    /**
     * Getter for property insuranceAmt.
     * @return Value of property insuranceAmt.
     */
    public java.lang.String getInsuranceAmt() {
        return insuranceAmt;
    }
    
    /**
     * Setter for property insuranceAmt.
     * @param insuranceAmt New value of property insuranceAmt.
     */
    public void setInsuranceAmt(java.lang.String insuranceAmt) {
        this.insuranceAmt = insuranceAmt;
    }
    
    /**
     * Getter for property fromActNo.
     * @return Value of property fromActNo.
     */
    public java.lang.String getFromActNo() {
        return fromActNo;
    }
    
    /**
     * Setter for property fromActNo.
     * @param fromActNo New value of property fromActNo.
     */
    public void setFromActNo(java.lang.String fromActNo) {
        this.fromActNo = fromActNo;
    }
    
    /**
     * Getter for property fromActNo1.
     * @return Value of property fromActNo1.
     */
    public java.lang.String getFromActNo1() {
        return fromActNo1;
    }
    
    /**
     * Setter for property fromActNo1.
     * @param fromActNo1 New value of property fromActNo1.
     */
    public void setFromActNo1(java.lang.String fromActNo1) {
        this.fromActNo1 = fromActNo1;
    }
    
    /**
     * Getter for property cboInsDetails.
     * @return Value of property cboInsDetails.
     */
    public java.lang.String getCboInsDetails() {
        return cboInsDetails;
    }
    
    /**
     * Setter for property cboInsDetails.
     * @param cboInsDetails New value of property cboInsDetails.
     */
    public void setCboInsDetails(java.lang.String cboInsDetails) {
        this.cboInsDetails = cboInsDetails;
    }
    
    /**
     * Getter for property cboInsUnderScheme.
     * @return Value of property cboInsUnderScheme.
     */
    public java.lang.String getCboInsUnderScheme() {
        return cboInsUnderScheme;
    }
    
    /**
     * Setter for property cboInsUnderScheme.
     * @param cboInsUnderScheme New value of property cboInsUnderScheme.
     */
    public void setCboInsUnderScheme(java.lang.String cboInsUnderScheme) {
        this.cboInsUnderScheme = cboInsUnderScheme;
    }
    
    /**
     * Getter for property cboDebitProdType.
     * @return Value of property cboDebitProdType.
     */
    public java.lang.String getCboDebitProdType() {
        return cboDebitProdType;
    }
    
    /**
     * Setter for property cboDebitProdType.
     * @param cboDebitProdType New value of property cboDebitProdType.
     */
    public void setCboDebitProdType(java.lang.String cboDebitProdType) {
        this.cboDebitProdType = cboDebitProdType;
    }
    
    /**
     * Getter for property cboDebitProdId.
     * @return Value of property cboDebitProdId.
     */
    public java.lang.String getCboDebitProdId() {
        return cboDebitProdId;
    }
    
    /**
     * Setter for property cboDebitProdId.
     * @param cboDebitProdId New value of property cboDebitProdId.
     */
    public void setCboDebitProdId(java.lang.String cboDebitProdId) {
        this.cboDebitProdId = cboDebitProdId;
    }
    
    /**
     * Getter for property cboNatureOfRiskCoverd.
     * @return Value of property cboNatureOfRiskCoverd.
     */
    public java.lang.String getCboNatureOfRiskCoverd() {
        return cboNatureOfRiskCoverd;
    }
    
    /**
     * Setter for property cboNatureOfRiskCoverd.
     * @param cboNatureOfRiskCoverd New value of property cboNatureOfRiskCoverd.
     */
    public void setCboNatureOfRiskCoverd(java.lang.String cboNatureOfRiskCoverd) {
        this.cboNatureOfRiskCoverd = cboNatureOfRiskCoverd;
    }
    
    /**
     * Getter for property tblInsuranceDetails.
     * @return Value of property tblInsuranceDetails.
     */
    public com.see.truetransact.clientutil.EnhancedTableModel getTblInsuranceDetails() {
        return tblInsuranceDetails;
    }
    
    /**
     * Setter for property tblInsuranceDetails.
     * @param tblInsuranceDetails New value of property tblInsuranceDetails.
     */
    public void setTblInsuranceDetails(com.see.truetransact.clientutil.EnhancedTableModel tblInsuranceDetails) {
        this.tblInsuranceDetails = tblInsuranceDetails;
    }
    
    /**
     * Getter for property insStDt.
     * @return Value of property insStDt.
     */
    public java.lang.String getInsStDt() {
        return insStDt;
    }
    
    /**
     * Setter for property insStDt.
     * @param insStDt New value of property insStDt.
     */
    public void setInsStDt(java.lang.String insStDt) {
        this.insStDt = insStDt;
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
    
    /**
     * Getter for property cbmCardType.
     * @return Value of property cbmCardType.
     */
    public com.see.truetransact.clientutil.ComboBoxModel getCbmCardType() {
        return cbmCardType;
    }
    
    /**
     * Setter for property cbmCardType.
     * @param cbmCardType New value of property cbmCardType.
     */
    public void setCbmCardType(com.see.truetransact.clientutil.ComboBoxModel cbmCardType) {
        this.cbmCardType = cbmCardType;
    }
    
    /**
     * Getter for property cboCardType.
     * @return Value of property cboCardType.
     */
    public java.lang.String getCboCardType() {
        return cboCardType;
    }
    
    /**
     * Setter for property cboCardType.
     * @param cboCardType New value of property cboCardType.
     */
    public void setCboCardType(java.lang.String cboCardType) {
        this.cboCardType = cboCardType;
    }
    
    
    
    /**
     * Getter for property tdtReviewDate.
     * @return Value of property tdtReviewDate.
     */
    public java.lang.String getTdtReviewDate() {
        return tdtReviewDate;
    }
    
    /**
     * Setter for property tdtReviewDate.
     * @param tdtReviewDate New value of property tdtReviewDate.
     */
    public void setTdtReviewDate(java.lang.String tdtReviewDate) {
        this.tdtReviewDate = tdtReviewDate;
    }
    
    /**
     * Getter for property txtCardLimit.
     * @return Value of property txtCardLimit.
     */
    public java.lang.String getTxtCardLimit() {
        return txtCardLimit;
    }
    
    /**
     * Setter for property txtCardLimit.
     * @param txtCardLimit New value of property txtCardLimit.
     */
    public void setTxtCardLimit(java.lang.String txtCardLimit) {
        this.txtCardLimit = txtCardLimit;
    }
    
    /**
     * Getter for property txtCardPeriod.
     * @return Value of property txtCardPeriod.
     */
    public java.lang.String getTxtCardPeriod() {
        return txtCardPeriod;
    }
    
    /**
     * Setter for property txtCardPeriod.
     * @param txtCardPeriod New value of property txtCardPeriod.
     */
    public void setTxtCardPeriod(java.lang.String txtCardPeriod) {
        this.txtCardPeriod = txtCardPeriod;
    }
    
    /**
     * Getter for property cboCardPeriod.
     * @return Value of property cboCardPeriod.
     */
    public java.lang.String getCboCardPeriod() {
        return cboCardPeriod;
    }
    
    /**
     * Setter for property cboCardPeriod.
     * @param cboCardPeriod New value of property cboCardPeriod.
     */
    public void setCboCardPeriod(java.lang.String cboCardPeriod) {
        this.cboCardPeriod = cboCardPeriod;
    }
    
    /**
     * Getter for property cbmCardPeriod.
     * @return Value of property cbmCardPeriod.
     */
    public com.see.truetransact.clientutil.ComboBoxModel getCbmCardPeriod() {
        return cbmCardPeriod;
    }
    
    /**
     * Setter for property cbmCardPeriod.
     * @param cbmCardPeriod New value of property cbmCardPeriod.
     */
    public void setCbmCardPeriod(com.see.truetransact.clientutil.ComboBoxModel cbmCardPeriod) {
        this.cbmCardPeriod = cbmCardPeriod;
    }
    
    /**
     * Getter for property chkInspection.
     * @return Value of property chkInspection.
     */
    public boolean isChkInspection() {
        return chkInspection;
    }
    
    /**
     * Setter for property chkInspection.
     * @param chkInspection New value of property chkInspection.
     */
    public void setChkInspection(boolean chkInspection) {
        this.chkInspection = chkInspection;
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
    
}

