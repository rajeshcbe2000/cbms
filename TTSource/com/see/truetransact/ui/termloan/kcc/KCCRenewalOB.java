/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition.
 * 
 *
 * TermLoanInstallmentOB.java
 *
 * Created on Tue Jan 25 16:03:11 IST 2005
 */

package com.see.truetransact.ui.termloan.kcc;

import com.see.truetransact.ui.termloan.repayment.*;
import com.see.truetransact.ui.termloan.emicalculator.*;
import com.see.truetransact.clientutil.TableSorter;
import com.see.truetransact.clientutil.EnhancedTableModel;
import com.see.truetransact.clientutil.TableUtil;
import com.see.truetransact.clientutil.ComboBoxModel;
import com.see.truetransact.clientutil.ClientUtil;
import com.see.truetransact.clientexception.ClientParseException;
import com.see.truetransact.clientproxy.ProxyFactory;
import com.see.truetransact.clientproxy.ProxyParameters;
import com.see.truetransact.clientutil.*;
import com.see.truetransact.commonutil.CommonConstants;
import com.see.truetransact.commonutil.DateUtil;
import com.see.truetransact.commonutil.CommonUtil;
import com.see.truetransact.commonutil.TTException;
import com.see.truetransact.ui.common.authorizedsignatory.AuthorizedSignatoryOB;
import com.see.truetransact.commonutil.interestcalc.InterestCalculationBean;
import com.see.truetransact.commonutil.interestcalc.LoanCalculateInterest;
import com.see.truetransact.commonutil.interestcalc.InterestCalculationConstants;
import com.see.truetransact.transferobject.servicetax.servicetaxdetails.ServiceTaxDetailsTO;
import com.see.truetransact.transferobject.termloan.*;
import com.see.truetransact.ui.common.powerofattorney.PowerOfAttorneyOB;
import com.see.truetransact.ui.TrueTransactMain;
import com.see.truetransact.ui.common.servicetax.ServiceTaxCalculation;
import com.see.truetransact.uicomponent.CObservable;
import com.see.truetransact.uicomponent.COptionPane;
import com.see.truetransact.uicomponent.CTable;
import java.util.*;

import org.apache.log4j.Logger;
import com.see.truetransact.ui.common.transaction.TransactionOB;

/**
 *
 * @author  152713
 */

public class KCCRenewalOB extends CObservable{
    private static KCCRenewalOB repaymentScheduleCreationOB;   
    private final        TermLoanInstallmentRB objTermLoanInstallmentRB = new TermLoanInstallmentRB();
    private final static Logger log = Logger.getLogger(KCCRenewalOB.class);
    private final static ClientParseException parseException = ClientParseException.getInstance();
    private LoanCalculateInterest  interestCalculator;
    private TransactionOB transactionOB;
    private ArrayList installmentTitle = new ArrayList();  //  Table Title of Installment
    private CTable tblInstallment;
    private HashMap operationMap= new HashMap();
    private ArrayList installmentTabRow;
    private ArrayList installmentAllTabRecords = new ArrayList();
    private HashMap installmentRecord;
    private LinkedHashMap installmentAll = new LinkedHashMap();
    private TableUtil tableUtilInstallment = new TableUtil();
    private ProxyFactory proxy=null;
    private double repayAmt = 0.0;
    private double totalRepayAmount = 0.0;
    private double existTotals=0;
    private boolean isLimitAmtTallyRepayAmt = false;
    private boolean hideSubmit=false;
    private String txtLoanAmt = "";
    private String tdtInstallmentDate = "";
    private String txtPrincipalAmt = "";
    private String txtInterest = "";
    private String txtTotal = "";
    private String txtBalance = "";
    private String lblAcctNo_Disp = "";
    private String txtInterestRate = "";
    private String strInstallDate = "";
    private String cboFrequency = "";
    private String txtNoOfInstall = "";
    private String cboroundOfType="";
    private String cboRepaymentType="";
    private String txtAcctNo = "";
    
    private LinkedHashMap allowedTransactionDetailsTO = null;
    private LinkedHashMap transactionDetailsTO = null;
    private final String NOT_DELETED_TRANS_TOs = "NOT_DELETED_TRANS_TOs";
    
    
    private ComboBoxModel cbmFrequency;
    private ComboBoxModel cbmroundOfType;
    private ComboBoxModel cbmRepaymentType;
    private HashMap lookUpHash;
    private HashMap keyValue;
    private ArrayList key;
    private ArrayList value;
    
    private final String ALL_VALUES = "ALL_VALUES";
    private final String BALANCE = "BALANCE";
    private final String COMMAND = "COMMAND";
    private final String COMPOUNDING_TYPE = "COMPOUNDING_TYPE";
    private final String COMPOUNDING_PERIOD = "COMPOUNDING_PERIOD";
    private final String DURATION_DD = "DURATION_DD";
    private final String DURATION_MM = "DURATION_MM";
    private final String DURATION_YY = "DURATION_YY";
    private final String FLOAT_PRECISION = "FLOAT_PRECISION";
    private final String FROM_DT = "FROM_DATE";
    private final String INTEREST = "INTEREST";
    private final String ISDURATION_DDMMYY = "ISDURATION_DDMMYY";
    private final String INTEREST_RATE = "INTEREST_RATE";
    private final String INSTALLMENT_DATE = "INSTALLMENT_DATE";
    private final String INTEREST_TYPE = "INTEREST_TYPE";
    private final String MONTH_OPTION = "MONTH_OPTION";
    private final String NO_INSTALL = "NO_INSTALL";
    private final String NO = "NO";
    private final String OPTION = "OPTION";
    private final String PRINCIPAL = "PRINCIPAL";
    private final String PRINCIPAL_AMOUNT = "PRINCIPAL_AMOUNT";
    private final String REPAYMENT_FREQ = "REPAYMENT_FREQUENCY";
    private final String REPAYMENT_TYPE = "REPAYMENT_TYPE";
    private final String ROUNDING_FACTOR = "ROUNDING_FACTOR";
    private final String ROUNDING_TYPE = "ROUNDING_TYPE";
    private final String SLNO = "SLNO";
    private final String TABLE_VALUES = "TABLE_VALUES";
    private final String TO_DT = "TO_DATE";
    private final String TOTAL = "TOTAL";
    private final String VARIOUS_INTEREST_RATE = "VARIOUS_INTEREST_RATE";
    private final String YEAR_OPTION = "YEAR_OPTION";
    private final String YES = "YES";
    private final String CATEGORY_ID = "CATEGORY_ID";
    private final String FROM_DATE = "FROM_DATE";
    private final String AMOUNT = "AMOUNT";
    private final String PROD_ID = "PROD_ID";
    private final String TO_DATE = "TO_DATE";
    private final String ACT = "ACT";
    private final String PROD = "PROD";
    private Date curr_dt = null;
    private ArrayList repaymentEachTabRecord;
    private HashMap repaymentEachRecord; 
    private TableUtil tableUtilRepayment ;
    private ArrayList repaymentTabValues = new ArrayList();
    private LinkedHashMap repaymentAll = new LinkedHashMap();
    private EnhancedTableModel tblRepaymentTab;
    private final   ArrayList repaymentTabTitle = new ArrayList();      //  Table Title of Repayment
    private final   java.util.ResourceBundle objTermLoanRB = java.util.ResourceBundle.getBundle("com.see.truetransact.ui.termloan.TermLoanRB", ProxyParameters.LANGUAGE);
    private final   String  REF_SCHEDULE_NUMBER = "REF_SCHEDULE_NUMBER";
    private final   String  ACTIVE_STATUS = "ACTIVE_STATUS";
    private double activeLoanAmt = 0.0;
    private String txtAmtLastInstall = "";
    private String txtAmtPenulInstall = "";
    private String tdtLastInstall = "";
    private String txtTotalInstallAmt = "";
    private boolean deleteInstallment = false;
    private String txtMoratorium = "";
    private String repayScheduleMode = null;
    private String strRefScheduleNumber = null;
    private String fromDate = "";
    private String toDate = "";
    private String firstInstallDate = "";
    private String totalBaseAmount = "";
    private String disbusmentDate = "";
    private String newScheduleNo = "";
    private String disbusmentId = "";
    private String activeStatus = "";
    private String sanctionDate ="" ;
    private String repayFromDate = "";
    private String result;
    private String outstandingAmnt = "";
    private String sanctionedAmount = "";
    private LinkedHashMap memberTypeMap;
    private EnhancedTableModel tblMemberTypeDetails;
    final ArrayList tableMemberTitle = new ArrayList();
    private ArrayList IncVal = new ArrayList();

    private String txtMemNo = "";
    private String txtMemName = "";
    private String txtMemType = "";
    private String txtContactNum = "";
    private String txtMemNetworth ="";
    private String txtMemPriority = "";
    private boolean memberTypeData = false;
    private String txtNoOfYears = "";
    private String tdtFromDt = "";
    private String tdtToDt = "";
    private String txtLimitAmount = "";
    private String txtBorrowerNo = "";
    
    private String txtJewelleryDetails = "";
    private String txtGrossWeight = "";
    private String txtGoldRemarks = "";
    private String txtValueOfGold = "";
    private String txtNetWeight = "";
    
    private String strACNumber   = "";   
    private EnhancedTableModel tblCollateralDetails;
    private EnhancedTableModel tblJointCollateral;
    private boolean rdoGahanYes=false;
    private boolean rdoGahanNo=false;
    private String txtOwnerMemNo = "";
    private String txtOwnerMemberNname = "";
    private String txtDocumentNo = "";
    private String cboDocumentType = "";
    private String tdtDocumentDate = "";
    private String txtRegisteredOffice = "";
    private String cboPledge = "";
    private String txtPledgeType = "";
    private String tdtPledgeDate = "";
    private String txtPledgeNo = "";
    private String txtPledgeAmount = "";
    private String txtVillage = "";
    private String txtSurveyNo = "";
    private String txtTotalArea = "";
    private String cboNature = "";
    private String cboRight = "";
    private String txtAreaParticular = "";
    private String docGenId="";
    private LinkedHashMap collateralMap;
    private LinkedHashMap deletedCollateralMap;
    private boolean collateralTypeData = false;
    final ArrayList tableCollateralJointTitle = new ArrayList();
    final ArrayList tableCollateralTitle = new ArrayList();
    private String txtDepNo = "";
    private String cboProductTypeSecurity = "";
    private String cboDepProdType = "";
    private String tdtDepDt = "";
    private String txtDepAmount = "";       
    private String txtMaturityDt = "";
    private String txtMaturityValue = ""; 
    private String txtRateOfInterest = "";   
    private boolean depositTypeData=false;
    private ComboBoxModel cbmProdTypeSecurity;
    private ComboBoxModel cbmDepProdID;
            
    
    private String txtDocumentType = "";
    private HashMap pledgeValMap=new HashMap();
    private ComboBoxModel cbmSecurityCity;
    private ComboBoxModel cbmNature;
    private ComboBoxModel cbmRight;
    private ComboBoxModel cbmPledge;
    private ComboBoxModel cbmDocumentType;
    private String cboDepProdID="";
    private LinkedHashMap depositTypeMap;
    private EnhancedTableModel tblDepositTypeDetails;
    final ArrayList tableTitleDepositList = new ArrayList();
    private final   ArrayList salaryTitle=new ArrayList();
    private LinkedHashMap deletedDepositTypeMap;
    private String chkRenew = "";
    
    private String txtSalaryCertificateNo = "";
    private String txtEmployerName ="";
    private String txtAddress = "";
    private String cboCity = "";
    private String txtPinCode = "";
    private String txtDesignation = "";
    private String txtContactNo = "";
    private String tdtRetirementDt = "";
    private String txtMemberNum = "";
    private String txtTotalSalary = "";
    private String txtNetWorth1 = "";
    private String txtSalaryRemark = "";

    private String cboSecurityCity = "";
    private String txtNetWorth = "";
    private EnhancedTableModel tblSalarySecrityTable;
    private LinkedHashMap salarySecurityAll = new LinkedHashMap(); 
    private LinkedHashMap salarySecurityDeleteAll = new LinkedHashMap(); 
    private int securitySlNo=0;
    private List chargelst = null;
    private HashMap serviceTax_Map=null;//30-12-2019
    private String rdoGoldSecurityStockExists = "N"; // Added by nithya on 07-03-2020 for KD-1379
    private String txtGoldSecurityId = "";
    private String prodId = "";

    public ComboBoxModel getCbmSecurityCity() {
        return cbmSecurityCity;
    }

    public void setCbmSecurityCity(ComboBoxModel cbmSecurityCity) {
        this.cbmSecurityCity = cbmSecurityCity;
    }

    public EnhancedTableModel getTblSalarySecrityTable() {
        return tblSalarySecrityTable;
    }

    public void setTblSalarySecrityTable(EnhancedTableModel tblSalarySecrityTable) {
        this.tblSalarySecrityTable = tblSalarySecrityTable;
    }
    
    public String getCboSecurityCity() {
        return cboSecurityCity;
    }

    public void setCboSecurityCity(String cboSecurityCity) {
        this.cboSecurityCity = cboSecurityCity;
    }

    public String getTxtNetWorth() {
        return txtNetWorth;
    }

    public void setTxtNetWorth(String txtNetWorth) {
        this.txtNetWorth = txtNetWorth;
    }
    
    
    public String getCboCity() {
        return cboCity;
    }

    public void setCboCity(String cboCity) {
        this.cboCity = cboCity;
    }

    public String getTxtEmployerName() {
        return txtEmployerName;
    }

    public void setTxtEmployerName(String txtEmployerName) {
        this.txtEmployerName = txtEmployerName;
    }

    public String getTdtRetirementDt() {
        return tdtRetirementDt;
    }

    public void setTdtRetirementDt(String tdtRetirementDt) {
        this.tdtRetirementDt = tdtRetirementDt;
    }

    public String getTxtAddress() {
        return txtAddress;
    }

    public void setTxtAddress(String txtAddress) {
        this.txtAddress = txtAddress;
    }

    public String getTxtContactNo() {
        return txtContactNo;
    }

    public void setTxtContactNo(String txtContactNo) {
        this.txtContactNo = txtContactNo;
    }

    public String getTxtDesignation() {
        return txtDesignation;
    }

    public void setTxtDesignation(String txtDesignation) {
        this.txtDesignation = txtDesignation;
    }

    public String getTxtMemberNum() {
        return txtMemberNum;
    }

    public void setTxtMemberNum(String txtMemberNum) {
        this.txtMemberNum = txtMemberNum;
    }

    public String getTxtNetWorth1() {
        return txtNetWorth1;
    }

    public void setTxtNetWorth1(String txtNetWorth1) {
        this.txtNetWorth1 = txtNetWorth1;
    }

    public String getTxtPinCode() {
        return txtPinCode;
    }

    public void setTxtPinCode(String txtPinCode) {
        this.txtPinCode = txtPinCode;
    }

    public String getTxtSalaryCertificateNo() {
        return txtSalaryCertificateNo;
    }

    public void setTxtSalaryCertificateNo(String txtSalaryCertificateNo) {
        this.txtSalaryCertificateNo = txtSalaryCertificateNo;
    }

    public String getTxtSalaryRemark() {
        return txtSalaryRemark;
    }

    public void setTxtSalaryRemark(String txtSalaryRemark) {
        this.txtSalaryRemark = txtSalaryRemark;
    }

    public String getTxtTotalSalary() {
        return txtTotalSalary;
    }

    public void setTxtTotalSalary(String txtTotalSalary) {
        this.txtTotalSalary = txtTotalSalary;
    }
    
    

    public String getChkRenew() {
        return chkRenew;
    }

    public void setChkRenew(String chkRenew) {
        this.chkRenew = chkRenew;
    }

    public EnhancedTableModel getTblDepositTypeDetails() {
        return tblDepositTypeDetails;
    }

    public void setTblDepositTypeDetails(EnhancedTableModel tblDepositTypeDetails) {
        this.tblDepositTypeDetails = tblDepositTypeDetails;
    }

    public String getCboDepProdID() {
        return cboDepProdID;
    }

    public void setCboDepProdID(String cboDepProdID) {
        this.cboDepProdID = cboDepProdID;
    }
    
    public ComboBoxModel getCbmDepProdID() {
        return cbmDepProdID;
    }

    public void setCbmDepProdID(ComboBoxModel cbmDepProdID) {
        this.cbmDepProdID = cbmDepProdID;
    }
    
    public ComboBoxModel getCbmProdTypeSecurity() {
        return cbmProdTypeSecurity;
    }

    public void setCbmProdTypeSecurity(ComboBoxModel cbmProdTypeSecurity) {
        this.cbmProdTypeSecurity = cbmProdTypeSecurity;
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
    

    public ComboBoxModel getCbmDocumentType() {
        return cbmDocumentType;
    }

    public void setCbmDocumentType(ComboBoxModel cbmDocumentType) {
        this.cbmDocumentType = cbmDocumentType;
    }

    public ComboBoxModel getCbmNature() {
        return cbmNature;
    }

    public void setCbmNature(ComboBoxModel cbmNature) {
        this.cbmNature = cbmNature;
    }

    public ComboBoxModel getCbmPledge() {
        return cbmPledge;
    }

    public void setCbmPledge(ComboBoxModel cbmPledge) {
        this.cbmPledge = cbmPledge;
    }

    public ComboBoxModel getCbmRight() {
        return cbmRight;
    }

    public void setCbmRight(ComboBoxModel cbmRight) {
        this.cbmRight = cbmRight;
    }

    public String getTdtDepDt() {
        return tdtDepDt;
    }

    public void setTdtDepDt(String tdtDepDt) {
        this.tdtDepDt = tdtDepDt;
    }

    public String getTxtDepAmount() {
        return txtDepAmount;
    }

    public void setTxtDepAmount(String txtDepAmount) {
        this.txtDepAmount = txtDepAmount;
    }

    public String getTxtDepNo() {
        return txtDepNo;
    }

    public void setTxtDepNo(String txtDepNo) {
        this.txtDepNo = txtDepNo;
    }

    public String getTxtMaturityDt() {
        return txtMaturityDt;
    }

    public void setTxtMaturityDt(String txtMaturityDt) {
        this.txtMaturityDt = txtMaturityDt;
    }

    public String getTxtMaturityValue() {
        return txtMaturityValue;
    }

    public void setTxtMaturityValue(String txtMaturityValue) {
        this.txtMaturityValue = txtMaturityValue;
    }

    public String getTxtRateOfInterest() {
        return txtRateOfInterest;
    }

    public void setTxtRateOfInterest(String txtRateOfInterest) {
        this.txtRateOfInterest = txtRateOfInterest;
    }

    public String getCboDepProdType() {
        return cboDepProdType;
    }

    public void setCboDepProdType(String cboDepProdType) {
        this.cboDepProdType = cboDepProdType;
    }

    public String getCboProductTypeSecurity() {
        return cboProductTypeSecurity;
    }

    public void setCboProductTypeSecurity(String cboProductTypeSecurity) {
        this.cboProductTypeSecurity = cboProductTypeSecurity;
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
    

    public String getDocGenId() {
        return docGenId;
    }

    public void setDocGenId(String docGenId) {
        this.docGenId = docGenId;
    }

    public String getCboDocumentType() {
        return cboDocumentType;
    }

    public void setCboDocumentType(String cboDocumentType) {
        this.cboDocumentType = cboDocumentType;
    }

    public String getCboNature() {
        return cboNature;
    }

    public void setCboNature(String cboNature) {
        this.cboNature = cboNature;
    }

    public String getCboPledge() {
        return cboPledge;
    }

    public boolean isRdoGahanNo() {
        return rdoGahanNo;
    }

    public void setRdoGahanNo(boolean rdoGahanNo) {
        this.rdoGahanNo = rdoGahanNo;
    }

    public boolean isRdoGahanYes() {
        return rdoGahanYes;
    }

    public void setRdoGahanYes(boolean rdoGahanYes) {
        this.rdoGahanYes = rdoGahanYes;
    }
    
     private int rowCoun;

    public int getRowCoun() {
        return rowCoun;
    }

    public void setRowCoun(int rowCoun) {
        this.rowCoun = rowCoun;
    }

    public void setCboPledge(String cboPledge) {
        this.cboPledge = cboPledge;
    }

    public String getCboRight() {
        return cboRight;
    }

    public void setCboRight(String cboRight) {
        this.cboRight = cboRight;
    }

    public String getTdtDocumentDate() {
        return tdtDocumentDate;
    }

    public void setTdtDocumentDate(String tdtDocumentDate) {
        this.tdtDocumentDate = tdtDocumentDate;
    }

    public String getTdtPledgeDate() {
        return tdtPledgeDate;
    }

    public void setTdtPledgeDate(String tdtPledgeDate) {
        this.tdtPledgeDate = tdtPledgeDate;
    }

    public String getTxtAreaParticular() {
        return txtAreaParticular;
    }

    public void setTxtAreaParticular(String txtAreaParticular) {
        this.txtAreaParticular = txtAreaParticular;
    }

    public String getTxtDocumentNo() {
        return txtDocumentNo;
    }

    public void setTxtDocumentNo(String txtDocumentNo) {
        this.txtDocumentNo = txtDocumentNo;
    }

    public String getTxtOwnerMemNo() {
        return txtOwnerMemNo;
    }

    public void setTxtOwnerMemNo(String txtOwnerMemNo) {
        this.txtOwnerMemNo = txtOwnerMemNo;
    }

    public String getTxtOwnerMemberNname() {
        return txtOwnerMemberNname;
    }

    public void setTxtOwnerMemberNname(String txtOwnerMemberNname) {
        this.txtOwnerMemberNname = txtOwnerMemberNname;
    }

    public String getTxtPledgeAmount() {
        return txtPledgeAmount;
    }

    public void setTxtPledgeAmount(String txtPledgeAmount) {
        this.txtPledgeAmount = txtPledgeAmount;
    }

    public String getTxtPledgeNo() {
        return txtPledgeNo;
    }

    public void setTxtPledgeNo(String txtPledgeNo) {
        this.txtPledgeNo = txtPledgeNo;
    }

    public String getTxtPledgeType() {
        return txtPledgeType;
    }

    public void setTxtPledgeType(String txtPledgeType) {
        this.txtPledgeType = txtPledgeType;
    }

    public String getTxtRegisteredOffice() {
        return txtRegisteredOffice;
    }

    public void setTxtRegisteredOffice(String txtRegisteredOffice) {
        this.txtRegisteredOffice = txtRegisteredOffice;
    }

    public String getTxtSurveyNo() {
        return txtSurveyNo;
    }

    public void setTxtSurveyNo(String txtSurveyNo) {
        this.txtSurveyNo = txtSurveyNo;
    }

    public String getTxtTotalArea() {
        return txtTotalArea;
    }

    public void setTxtTotalArea(String txtTotalArea) {
        this.txtTotalArea = txtTotalArea;
    }

    public String getTxtVillage() {
        return txtVillage;
    }

    public void setTxtVillage(String txtVillage) {
        this.txtVillage = txtVillage;
    }
    
    public EnhancedTableModel getTblCollateralDetails() {
        return tblCollateralDetails;
    }

    public void setTblCollateralDetails(EnhancedTableModel tblCollateralDetails) {
        this.tblCollateralDetails = tblCollateralDetails;
    }

    public EnhancedTableModel getTblJointCollateral() {
        return tblJointCollateral;
    }

    public void setTblJointCollateral(EnhancedTableModel tblJointCollateral) {
        this.tblJointCollateral = tblJointCollateral;
    }
    
    private String oldSurvyNo="";

    public String getOldSurvyNo() {
        return oldSurvyNo;
    }

    public void setOldSurvyNo(String oldSurvyNo) {
        this.oldSurvyNo = oldSurvyNo;
    }
    
    private LinkedHashMap deletedMemberTypeMap;

    public String getTxtContactNum() {
        return txtContactNum;
    }

    public void setTxtContactNum(String txtContactNum) {
        this.txtContactNum = txtContactNum;
    }

    public String getTxtMemName() {
        return txtMemName;
    }

    public void setTxtMemName(String txtMemName) {
        this.txtMemName = txtMemName;
    }

    public String getTxtMemNetworth() {
        return txtMemNetworth;
    }

    public String getTxtMemPriority() {
        return txtMemPriority;
    }

    public void setTxtMemPriority(String txtMemPriority) {
        this.txtMemPriority = txtMemPriority;
    }

    public String getTxtBorrowerNo() {
        return txtBorrowerNo;
    }

    public void setTxtBorrowerNo(String txtBorrowerNo) {
        this.txtBorrowerNo = txtBorrowerNo;
    }
    
    public void setTxtMemNetworth(String txtMemNetworth) {
        this.txtMemNetworth = txtMemNetworth;
    }

    public String getTxtMemNo() {
        return txtMemNo;
    }

    public void setTxtMemNo(String txtMemNo) {
        this.txtMemNo = txtMemNo;
    }

    public String getTxtMemType() {
        return txtMemType;
    }

    public void setTxtMemType(String txtMemType) {
        this.txtMemType = txtMemType;
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

    public String getTdtFromDt() {
        return tdtFromDt;
    }

    public void setTdtFromDt(String tdtFromDt) {
        this.tdtFromDt = tdtFromDt;
    }

    public String getTdtToDt() {
        return tdtToDt;
    }

    public void setTdtToDt(String tdtToDt) {
        this.tdtToDt = tdtToDt;
    }

    public String getTxtLimitAmount() {
        return txtLimitAmount;
    }

    public void setTxtLimitAmount(String txtLimitAmount) {
        this.txtLimitAmount = txtLimitAmount;
    }

    public String getTxtNoOfYears() {
        return txtNoOfYears;
    }

    public void setTxtNoOfYears(String txtNoOfYears) {
        this.txtNoOfYears = txtNoOfYears;
    }
    

    public String getSanctionedAmount() {
        return sanctionedAmount;
    }

    public void setSanctionedAmount(String sanctionedAmount) {
        this.sanctionedAmount = sanctionedAmount;
    }
    
    public String getOutstandingAmnt() {
        return outstandingAmnt;
    }

    public void setOutstandingAmnt(String outstandingAmnt) {
        this.outstandingAmnt = outstandingAmnt;
    }

    public String getTxtGoldRemarks() {
        return txtGoldRemarks;
    }

    public void setTxtGoldRemarks(String txtGoldRemarks) {
        this.txtGoldRemarks = txtGoldRemarks;
    }

    public String getTxtGrossWeight() {
        return txtGrossWeight;
    }

    public void setTxtGrossWeight(String txtGrossWeight) {
        this.txtGrossWeight = txtGrossWeight;
    }

    public String getTxtJewelleryDetails() {
        return txtJewelleryDetails;
    }

    public void setTxtJewelleryDetails(String txtJewelleryDetails) {
        this.txtJewelleryDetails = txtJewelleryDetails;
    }

    public String getTxtNetWeight() {
        return txtNetWeight;
    }

    public void setTxtNetWeight(String txtNetWeight) {
        this.txtNetWeight = txtNetWeight;
    }

    public String getTxtValueOfGold() {
        return txtValueOfGold;
    }

    public void setTxtValueOfGold(String txtValueOfGold) {
        this.txtValueOfGold = txtValueOfGold;
    }
    
    public String getRepayFromDate() {
        return repayFromDate;
    }

    public void setRepayFromDate(String repayFromDate) {
        this.repayFromDate = repayFromDate;
    }
   
    public String getSanctionDate() {
        return sanctionDate;
    }

    public void setSanctionDate(String sanctionDate) {
        this.sanctionDate = sanctionDate;
    }

    public String getActiveStatus() {
        return activeStatus;
    }

    public void setActiveStatus(String activeStatus) {
        this.activeStatus = activeStatus;
    }   
        
    public String getDisbusmentId() {
        return disbusmentId;
    }

    public void setDisbusmentId(String disbusmentId) {
        this.disbusmentId = disbusmentId;
    }
    
    
    public String getNewScheduleNo() {
        return newScheduleNo;
    }

    public void setNewScheduleNo(String newScheduleNo) {
        this.newScheduleNo = newScheduleNo;
    }    
    
    public String getDisbusmentDate() {
        return disbusmentDate;
    }

    public void setDisbusmentDate(String disbusmentDate) {
        this.disbusmentDate = disbusmentDate;
    }

    public String getTotalBaseAmount() {
        return totalBaseAmount;
    }

    public void setTotalBaseAmount(String totalBaseAmount) {
        this.totalBaseAmount = totalBaseAmount;
    }
  
    public String getFirstInstallDate() {
        return firstInstallDate;
    }

    public String getFromDate() {
        return fromDate;
    }

    public String getToDate() {
        return toDate;
    }

    public void setFirstInstallDate(String firstInstallDate) {
        this.firstInstallDate = firstInstallDate;
    }

    public void setFromDate(String fromDate) {
        this.fromDate = fromDate;
    }

    public void setToDate(String toDate) {
        this.toDate = toDate;
    }

    public String getRepayScheduleMode() {
        return repayScheduleMode;
    }

    public String getStrRefScheduleNumber() {
        return strRefScheduleNumber;
    }

    public void setRepayScheduleMode(String repayScheduleMode) {
        this.repayScheduleMode = repayScheduleMode;
    }

    public void setStrRefScheduleNumber(String strRefScheduleNumber) {
        this.strRefScheduleNumber = strRefScheduleNumber;
    }
    

    public String getTxtMoratorium() {
        return txtMoratorium;
    }

    public void setTxtMoratorium(String txtMoratorium) {
        this.txtMoratorium = txtMoratorium;
    }

    public String getTdtLastInstall() {
        return tdtLastInstall;
    }

    public String getTxtAmtLastInstall() {
        return txtAmtLastInstall;
    }

    public String getTxtAmtPenulInstall() {
        return txtAmtPenulInstall;
    }

    public String getTxtTotalInstallAmt() {
        return txtTotalInstallAmt;
    }

    public void setTdtLastInstall(String tdtLastInstall) {
        this.tdtLastInstall = tdtLastInstall;
    }

    public void setTxtAmtLastInstall(String txtAmtLastInstall) {
        this.txtAmtLastInstall = txtAmtLastInstall;
    }

    public void setTxtAmtPenulInstall(String txtAmtPenulInstall) {
        this.txtAmtPenulInstall = txtAmtPenulInstall;
    }

    public void setTxtTotalInstallAmt(String txtTotalInstallAmt) {
        this.txtTotalInstallAmt = txtTotalInstallAmt;
    }
    
    
    public double getActiveLoanAmt() {
        return activeLoanAmt;
    }

    public void setActiveLoanAmt(double activeLoanAmt) {
        this.activeLoanAmt = activeLoanAmt;
    }
    
     public void setTransactionOB(TransactionOB transactionOB) {
        this.transactionOB = transactionOB;
    }
    
    Date curDate = null;
    
    static {
        try {
            repaymentScheduleCreationOB = new KCCRenewalOB();
        } catch(Exception e) {
            log.info("try: " + e);
            parseException.logException(e,true);
            e.printStackTrace();
        }
    }

     public static KCCRenewalOB getInstance() {
        return repaymentScheduleCreationOB;
    }
     
      private void setOperationMap() throws Exception{
        operationMap = new HashMap();
        operationMap.put(CommonConstants.JNDI, "KCCRenewalJNDI");
        operationMap.put(CommonConstants.HOME, "termloan.kcc.KCCRenewalHome");
        operationMap.put(CommonConstants.REMOTE, "termloan.kcc.KCCRenewal");
        fillDropdown();
    }
      
    private void setCollateralJointTableTile() throws Exception{
        tableCollateralJointTitle.add("Cust Id");
        tableCollateralJointTitle.add("Name");
        tableCollateralJointTitle.add("Constitution");
        IncVal = new ArrayList();
    }
    
    private void setDepositTableTile() throws Exception{
        tableTitleDepositList.add("Prod Type");
        tableTitleDepositList.add("Dep No");    
        tableTitleDepositList.add("Dep Amt");       
        tableTitleDepositList.add("Matur Val");     
        IncVal = new ArrayList();
    }
    
    private KCCRenewalOB() throws Exception{

        setMemberTableTile();
        setCollateralJointTableTile();
        setCollateralTableTile();
        setOperationMap();    
        setDepositTableTile();
        setSalaryTitle();
        tblMemberTypeDetails = new EnhancedTableModel(null, tableMemberTitle);
        tblJointCollateral = new EnhancedTableModel(null, tableCollateralJointTitle);
        tblCollateralDetails = new EnhancedTableModel(null, tableCollateralTitle);
        tblDepositTypeDetails = new EnhancedTableModel(null, tableTitleDepositList);
        tblSalarySecrityTable=new EnhancedTableModel(null,salaryTitle);
        try {
            curDate = ClientUtil.getCurrentDate();
            proxy = ProxyFactory.createProxy();            
            ttNotifyObservers();
        } catch (Exception e) {
            parseException.logException(e,true);
            e.printStackTrace();
            log.info("RepaymentScheduleCreationOB..."+e);
        }
        RepaymentScheduleCreationOB();
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
    
    private void RepaymentScheduleCreationOB() throws Exception{
        setRepaymentTabTitle();        
        tblRepaymentTab = new EnhancedTableModel(null, repaymentTabTitle);
        notifyObservers();
    }
    
    public void fillDropdown() throws Exception{
        lookUpHash = new HashMap();
        lookUpHash.put(CommonConstants.MAP_NAME,null);
        
        ArrayList lookup_keys = new ArrayList();
        lookup_keys.add("LOAN.FREQUENCY");
        lookup_keys.add("TERM_LOAN.REPAYMENT_TYPE");
        lookup_keys.add("OPERATIVEACCTPRODUCT.ROUNDOFF");
        lookup_keys.add("SECURITY.NATURE");
        lookup_keys.add("SECURITY.RIGHT");
        lookup_keys.add("SECURITY.PLEDGE");
        lookup_keys.add("TERMLOAN.DOCUMENT_TYPE");
        lookup_keys.add("CUSTOMER.CITY");
        lookUpHash.put(CommonConstants.PARAMFORQUERY, lookup_keys);
        
        keyValue = ClientUtil.populateLookupData(lookUpHash);
        //System.out.println("keyValue : " +keyValue);
        getKeyValue((HashMap)keyValue.get("LOAN.FREQUENCY"));
        cbmFrequency = new ComboBoxModel(key,value);
        getKeyValue((HashMap)keyValue.get("TERM_LOAN.REPAYMENT_TYPE"));
        cbmRepaymentType = new ComboBoxModel(key,value);
        getKeyValue((HashMap)keyValue.get("OPERATIVEACCTPRODUCT.ROUNDOFF"));
        cbmroundOfType = new ComboBoxModel(key,value);
        
        getKeyValue((HashMap)keyValue.get("SECURITY.NATURE"));
        setCbmNature(new ComboBoxModel(key,value));
        
        getKeyValue((HashMap)keyValue.get("CUSTOMER.CITY"));
        setCbmSecurityCity(new ComboBoxModel(key,value));
        
        getKeyValue((HashMap)keyValue.get("SECURITY.RIGHT"));
        setCbmRight(new ComboBoxModel(key,value));
        
        getKeyValue((HashMap)keyValue.get("SECURITY.PLEDGE"));
        setCbmPledge(new ComboBoxModel(key,value));

        getKeyValue((HashMap)keyValue.get("TERMLOAN.DOCUMENT_TYPE"));//SECURITY_TYPE
        setCbmDocumentType(new ComboBoxModel(key,value));
        // cbmRepayFreq = new ComboBoxModel(key,value);
        
        key = new ArrayList();
        value = new ArrayList();
        key.add("");
        value.add("");
        key.add("TD");
        value.add("Deposits");
        key.add("MDS");
        value.add("MDS");
        cbmProdTypeSecurity = new ComboBoxModel(key,value);
        cbmDepProdID = new ComboBoxModel();
        lookup_keys = null;
    }
    
    private void setBlankKeyValue(){
        key = new ArrayList();
        key.add("");
        value = new ArrayList();
        value.add("");
    }
    
    private void getKeyValue(HashMap keyValue)  throws Exception{
        //System.out.println("keyValue" + keyValue);
        key = (ArrayList)keyValue.get(CommonConstants.KEY);
        value = (ArrayList)keyValue.get(CommonConstants.VALUE);
    }
    private void setInstallmentTitle() throws Exception{
        try {
            installmentTitle = new ArrayList();
            installmentTitle.add(objTermLoanInstallmentRB.getString("tblColumnInstallNo"));
            installmentTitle.add(objTermLoanInstallmentRB.getString("tblColumnInstallDate"));
            installmentTitle.add(objTermLoanInstallmentRB.getString("tblColumnInstallPrincipal"));
            installmentTitle.add(objTermLoanInstallmentRB.getString("tblColumnInstallInterest"));
            installmentTitle.add(objTermLoanInstallmentRB.getString("tblColumnInstallTotal"));
            installmentTitle.add(objTermLoanInstallmentRB.getString("tblColumnInstallBalance"));
            installmentTitle.add(objTermLoanInstallmentRB.getString("tblColumnInstallActiveStatus"));
            //            installmentTitle.add("EMI Amount");
        }catch(Exception e) {
            log.info("Exception in setInstallmentTitle: "+e);
            parseException.logException(e,true);
        }
    }
    
    private void setInstallmentTitleForUniformType() throws Exception{
        try {
            installmentTitle = new ArrayList();
            installmentTitle.add(objTermLoanInstallmentRB.getString("tblColumnInstallNo"));
            installmentTitle.add(objTermLoanInstallmentRB.getString("tblColumnInstallDate"));
            installmentTitle.add(objTermLoanInstallmentRB.getString("tblColumnInstallPrincipal"));
            installmentTitle.add(objTermLoanInstallmentRB.getString("tblColumnInstallInterest"));
            installmentTitle.add(objTermLoanInstallmentRB.getString("tblColumnInstallTotal"));
            installmentTitle.add("EMI Amount");
            installmentTitle.add(objTermLoanInstallmentRB.getString("tblColumnInstallBalance"));
            installmentTitle.add(objTermLoanInstallmentRB.getString("tblColumnInstallActiveStatus"));
            //            tblInstallment = new EnhancedTableModel(null, installmentTitle);
        }catch(Exception e) {
            log.info("Exception in setInstallmentTitle: "+e);
            parseException.logException(e,true);
        }
    }
    
    public void resetInstallmentCTable(){
        tblInstallment = new CTable();
        tableUtilInstallment = new TableUtil();
        tableUtilInstallment.setAttributeKey(SLNO);
    }
    
    public void resetInstallmentDetails(){
        setTxtTotal("");
        setTxtBalance("");
        setTxtInterest("");
        setTxtPrincipalAmt("");
        setTdtInstallmentDate("");
    }
    
    public void resetOB(){
        setTxtAcctNo("");
        setCboRepaymentType("");
        setTxtInterest("");
        setCboFrequency((""));    
        setOutstandingAmnt("");
        setSanctionedAmount("");
        setProdId("");
    }
    // Setter method for txtLoanAmt
    void setTxtLoanAmt(String txtLoanAmt){
        this.txtLoanAmt = txtLoanAmt;
        setChanged();
    }
    // Getter method for txtLoanAmt
    String getTxtLoanAmt(){
        return this.txtLoanAmt;
    }
    
    // Setter method for tdtInstallmentDate
    void setTdtInstallmentDate(String tdtInstallmentDate){
        this.tdtInstallmentDate = tdtInstallmentDate;
        setChanged();
    }
    // Getter method for tdtInstallmentDate
    String getTdtInstallmentDate(){
        return this.tdtInstallmentDate;
    }
    
    // Setter method for txtPrincipalAmt
    void setTxtPrincipalAmt(String txtPrincipalAmt){
        this.txtPrincipalAmt = txtPrincipalAmt;
        setChanged();
    }
    // Getter method for txtPrincipalAmt
    String getTxtPrincipalAmt(){
        return this.txtPrincipalAmt;
    }
    
    // Setter method for txtInterest
    void setTxtInterest(String txtInterest){
        this.txtInterest = txtInterest;
        setChanged();
    }
    // Getter method for txtInterest
    String getTxtInterest(){
        return this.txtInterest;
    }
    
    // Setter method for txtTotal
    void setTxtTotal(String txtTotal){
        this.txtTotal = txtTotal;
        setChanged();
    }
    // Getter method for txtTotal
    String getTxtTotal(){
        return this.txtTotal;
    }
    
    // Setter method for txtBalance
    void setTxtBalance(String txtBalance){
        this.txtBalance = txtBalance;
        setChanged();
    }
    // Getter method for txtBalance
    String getTxtBalance(){
        return this.txtBalance;
    }
    
    /**
     * Getter for property txtInterestRate.
     * @return Value of property txtInterestRate.
     */
    public java.lang.String getTxtInterestRate() {
        return txtInterestRate;
    }
    
    /**
     * Setter for property txtInterestRate.
     * @param txtInterestRate New value of property txtInterestRate.
     */
    public void setTxtInterestRate(java.lang.String txtInterestRate) {
        this.setChanged();
        this.txtInterestRate = txtInterestRate;
    }
    
    /**
     * Getter for property lblAcctNo_Disp.
     * @return Value of property lblAcctNo_Disp.
     */
    public java.lang.String getLblAcctNo_Disp() {
        return lblAcctNo_Disp;
    }
    
    /**
     * Setter for property lblAcctNo_Disp.
     * @param lblAcctNo_Disp New value of property lblAcctNo_Disp.
     */
    public void setLblAcctNo_Disp(java.lang.String lblAcctNo_Disp) {
        this.setChanged();
        this.lblAcctNo_Disp = lblAcctNo_Disp;
    }
    
    public void ttNotifyObservers(){
        this.notifyObservers();
    }
    
    /**
     * Getter for property tblInstallment.
     * @return Value of property tblInstallment.
     */
    public com.see.truetransact.uicomponent.CTable getTblInstallment() {
        return tblInstallment;
    }
    
    /**
     * Setter for property tblInstallment.
     * @param tblInstallment New value of property tblInstallment.
     */
    public void setTblInstallment(com.see.truetransact.uicomponent.CTable tblInstallment) {
        this.setChanged();
        this.tblInstallment = tblInstallment;
    }
    
    /**
     * Getter for property cboFrequency.
     * @return Value of property cboFrequency.
     */
    public java.lang.String getCboFrequency() {
        return cboFrequency;
    }
    
    /**
     * Setter for property cboFrequency.
     * @param cboFrequency New value of property cboFrequency.
     */
    public void setCboFrequency(java.lang.String cboFrequency) {
        this.cboFrequency = cboFrequency;
    }
    
    /**
     * Getter for property cbmFrequency.
     * @return Value of property cbmFrequency.
     */
    public com.see.truetransact.clientutil.ComboBoxModel getCbmFrequency() {
        return cbmFrequency;
    }
    
    /**
     * Setter for property cbmFrequency.
     * @param cbmFrequency New value of property cbmFrequency.
     */
    public void setCbmFrequency(com.see.truetransact.clientutil.ComboBoxModel cbmFrequency) {
        this.cbmFrequency = cbmFrequency;
    }
    
    /**
     * Getter for property txtNoOfInstall.
     * @return Value of property txtNoOfInstall.
     */
    public java.lang.String getTxtNoOfInstall() {
        return txtNoOfInstall;
    }
    
    /**
     * Setter for property txtNoOfInstall.
     * @param txtNoOfInstall New value of property txtNoOfInstall.
     */
    public void setTxtNoOfInstall(java.lang.String txtNoOfInstall) {
        this.txtNoOfInstall = txtNoOfInstall;
    }
    
    /**
     * Getter for property installmentAll.
     * @return Value of property installmentAll.
     */
    public java.util.LinkedHashMap getInstallmentAll() {
        return installmentAll;
    }
    
    /**
     * Setter for property installmentAll.
     * @param installmentAll New value of property installmentAll.
     */
    public void setInstallmentAll(java.util.LinkedHashMap installmentAll) {
        this.installmentAll = installmentAll;
    }

    public List getChargelst() {
        return chargelst;
    }

    public void setChargelst(List chargelst) {
        this.chargelst = chargelst;
    }
            
    public void populateData(HashMap data, CTable tbl){
        try{
            this.tblInstallment = tbl;
            calculateInterest(data);
            ttNotifyObservers();
        }catch(Exception e){
            log.info("Exception in populateData: "+e);
            parseException.logException(e,true);
        }
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
    
    public void populateDepositTypeDetails(String row){
        try{
            resetDepositTypeDetails();
            final TermLoanDepositTypeTO objDepositTypeTO = (TermLoanDepositTypeTO)depositTypeMap.get(row);
            populateDepositTableData(objDepositTypeTO);
            
        }catch(Exception e){
            parseException.logException(e,true);
        }
    }
    
    public HashMap calculateInterest(HashMap data){
        HashMap resultMap = new HashMap();
        try{
            interestCalculator = new LoanCalculateInterest();
            InterestCalculationBean interestBean;
            interestBean = new InterestCalculationBean();
            interestBean.setDuration_yy(CommonUtil.convertObjToStr(data.get(DURATION_YY)));
            interestBean.setCompoundingPeriod(CommonUtil.convertObjToStr(data.get(COMPOUNDING_PERIOD)));
            interestBean.setPrincipalAmt(CommonUtil.convertObjToStr(data.get(PRINCIPAL_AMOUNT)));
            interestBean.setRateOfInterest(CommonUtil.convertObjToStr(data.get(INTEREST)));
            interestBean.setCompoundingType(CommonUtil.convertObjToStr(data.get(COMPOUNDING_TYPE)));
            interestBean.setInterestType(CommonUtil.convertObjToStr(data.get(INTEREST_TYPE)));
            if (data.containsKey(ISDURATION_DDMMYY)){
                if (data.get(ISDURATION_DDMMYY).equals(YES)){
                    interestBean.setIsDuration_ddmmyy(true);
                }else if (data.get(ISDURATION_DDMMYY).equals(NO)){
                    interestBean.setIsDuration_ddmmyy(false);
                }
            }else{
                if (data.containsKey(FROM_DT) && data.containsKey(TO_DT)){
                    interestBean.setIsDuration_ddmmyy(false);
                }
            }
            
            if (data.containsKey(FROM_DT)){
                interestBean.setDuration_FromDate(CommonUtil.convertObjToStr(data.get(FROM_DT)));
            }else{
                interestBean.setDuration_FromDate(DateUtil.getStringDate(ClientUtil.getCurrentDate()));
            }
            if (data.containsKey(TO_DT)){
                interestBean.setDuration_ToDate(CommonUtil.convertObjToStr(data.get(TO_DT)));
            }
           
            if (data.containsKey(FLOAT_PRECISION)){
                interestBean.setFloatPrecision(CommonUtil.convertObjToStr(data.get(FLOAT_PRECISION)));
            }else{
                interestBean.setFloatPrecision("2");
            }
            if (data.containsKey(YEAR_OPTION)){
                interestBean.setYearOption(CommonUtil.convertObjToStr(data.get(YEAR_OPTION)));
            }else{
                interestBean.setYearOption(InterestCalculationConstants.YEAR_OPTION_ACTUAL);
            }
            if (data.containsKey(MONTH_OPTION)){
                interestBean.setMonthOption(CommonUtil.convertObjToStr(data.get(MONTH_OPTION)));
            }else{
                interestBean.setMonthOption(InterestCalculationConstants.MONTH_OPTION_ACTUAL);
            }
            if (data.containsKey(ROUNDING_TYPE)){
                interestBean.setRoundingType(CommonUtil.convertObjToStr(data.get(ROUNDING_TYPE)));
            }else{
                interestBean.setRoundingType(InterestCalculationConstants.ROUNDING_HIGHER);
            }
            if (data.containsKey(ROUNDING_FACTOR)){
                interestBean.setRoundingFactor(CommonUtil.convertObjToStr(data.get(ROUNDING_FACTOR)));
            }else{
                interestBean.setRoundingFactor(InterestCalculationConstants.ROUNDING_VALUE_5_PAISE);
            }
            HashMap map = new HashMap();
            HashMap repayMap = new HashMap();
            if (data.containsKey(FROM_DT)){
                repayMap.put(FROM_DT, data.get(FROM_DT));
            }else{
                repayMap.put(FROM_DT, ClientUtil.getCurrentDate());
            }
            if (data.containsKey(TO_DT)){
                repayMap.put(TO_DT, data.get(TO_DT));
            }
            if (data.containsKey(REPAYMENT_FREQ)){
                // To find the next installment date
                repayMap.put(REPAYMENT_FREQ, data.get(REPAYMENT_FREQ));
            }
            if (data.containsKey(NO_INSTALL)){
                // No. of Installments
                repayMap.put(NO_INSTALL, data.get(NO_INSTALL));
            }
            if (data.containsKey("INSTALLMENT_AMOUNT")) {
                repayMap.put("INSTALLMENT_AMOUNT", data.get("INSTALLMENT_AMOUNT"));
            }
            if (data.containsKey(VARIOUS_INTEREST_RATE)){
                repayMap.put(VARIOUS_INTEREST_RATE, data.get(VARIOUS_INTEREST_RATE));
            }
            if (data.containsKey(REPAYMENT_TYPE)){
                repayMap.put(REPAYMENT_TYPE, data.get(REPAYMENT_TYPE));
            }
            if(data.containsKey("PROD_ID")){
                repayMap.put("PROD_ID", data.get("PROD_ID"));
            }
            //            if(data.containsKey("EMI_TYPE"))
            //            {
            //                repayMap.put("EMI_TYPE", data.get("EMI_TYPE"));
            //            } else {
            //                repayMap.put("EMI_TYPE", data.get(REPAYMENT_TYPE));
            //            }
            map.put(CommonConstants.DATA, interestBean);
            map.put("REPAYMENT_DETAILS", repayMap);
            //            System.out.println("#### repayMap : "+repayMap);
            showExistingInstallment(data);
            resultMap = interestCalculator.getInterest(map);
            if (resultMap.containsKey("TABLE_RECORDS")){
                if(!hideSubmit)
                installmentAllTabRecords = (ArrayList) resultMap.get("TABLE_RECORDS");
               // System.out.println("####installmentalltabrecord"+installmentAllTabRecords);
                //                if(data.containsKey("EMI_TYPE"))
                if(data.get(REPAYMENT_TYPE).equals("UNIFORM_PRINCIPLE_EMI")) {
                    setInstallmentTitleForUniformType();
                }
                else
                    setInstallmentTitle();
                //                tableUtilInstallment.setAttributeKey(SLNO);
                tableUtilInstallment.setTableValues(installmentAllTabRecords);
                //System.out.println("### Heading Size : "+installmentTitle.size());
                
                TableModel tableModel = new TableModel();
                tableModel.setHeading(installmentTitle);
                if(installmentAllTabRecords!=null){
                    tableModel.setData(installmentAllTabRecords);
                    //System.out.println("### Data Size : " + installmentAllTabRecords.size());
                }
                tableModel.fireTableDataChanged();
                
                tblInstallment.setModel(tableModel);
                tblInstallment.revalidate();
                if(data.get(REPAYMENT_TYPE).equals("UNIFORM_PRINCIPLE_EMI"))
                    addTableColumn();
                
                
                //        tblInstallment.setDataArrayList(installmentAllTabRecords, installmentTitle);
                //               // setChanged();
            }
            if (resultMap.containsKey("ALL_RECORDS")){
                installmentAll = (LinkedHashMap) resultMap.get("ALL_RECORDS");
                //System.out.println("checkInstallmentall"+installmentAll);
                tableUtilInstallment.setAllValues(installmentAll);
            }
            if (resultMap.containsKey("TOTAL_REPAY_AMOUNT")){
                totalRepayAmount = CommonUtil.convertObjToDouble(resultMap.get("TOTAL_REPAY_AMOUNT")).doubleValue();
            }
            double interestAmount = CommonUtil.convertObjToDouble(resultMap.get(InterestCalculationConstants.INTEREST)).doubleValue();
            double prinAmount = CommonUtil.convertObjToDouble(resultMap.get(InterestCalculationConstants.AMOUNT)).doubleValue();
            if (data.containsKey(REPAYMENT_TYPE)){
                isLimitAmtTallyRepayAmt = true;
            }
            interestBean = null;
            map = null;
            repayMap = null;
        }catch(Exception e){
            log.info("Exception in calculateInterest: "+e);
            parseException.logException(e,true);
        }
        return resultMap;
    }
    
    
    
    private void showExistingInstallment(HashMap collectedData){
             HashMap map=new HashMap();
             
             try{
                 
                 HashMap dataMap =new HashMap();
                 map.put("ACCT_NUM",collectedData.get("ACT_NO"));
                 existTotals=0;
                 if(collectedData.containsKey("SCHEDULE_ID"))
                     map.put("SCHEDULE_ID",collectedData.get("SCHEDULE_ID"));
                 else{
                     return;
                 }
                    if(collectedData.get(REPAYMENT_TYPE).equals("UNIFORM_PRINCIPLE_EMI"))
                        dataMap.put(CommonConstants.MAP_NAME,"selectLoansInstallmentTL");
                    else
                        dataMap.put(CommonConstants.MAP_NAME,"selectLoansInstallmentEMITL");
                 
                 dataMap.put(CommonConstants.MAP_WHERE,map);
                 
                 HashMap resultMap=ClientUtil.executeTableQuery(dataMap);
                 if(resultMap !=null && resultMap.size()>0 && resultMap.get("TABLEHEAD") !=null){
                     hideSubmit=true;
                     installmentAllTabRecords=new ArrayList();
                     installmentAllTabRecords=    (ArrayList) resultMap.get("TABLEDATA");
                     for(int i=0;i<installmentAllTabRecords.size();i++){
                         ArrayList singleList=(ArrayList)installmentAllTabRecords.get(i);
                     existTotals+=CommonUtil.convertObjToDouble(singleList.get(4)).doubleValue();
                     }
                 }
                 else
                     hideSubmit=false;
                 //                observable.existInstallment(resultMap,uniformPrincipal);
                 
             }catch (Exception e){
                 e.printStackTrace();
             }
             
    }
    private void addTableColumn() {
        //        double t=0;
        //        int rowcount = installmentAllTabRecords.size()-1;
        //        String totals = ((ArrayList)installmentAllTabRecords.get(rowcount)).get(4).toString();
        //        System.out.println("#### totals : "+totals);
        //        double tot = Double.parseDouble(totals);
        //        t=LoanCalculateInterest.amountRouding(tot/(rowcount-1));
        //        double tableTot = t*(rowcount-1);
        //        int k;
        //        ArrayList tempList = new ArrayList();
        //        for(k=0;k<rowcount-2;k++) {
        //            tempList = (ArrayList)installmentAllTabRecords.get(k);
        //            System.out.println("#### tempList : "+tempList);
        //            tempList.set(5, new Double(t));
        //            installmentAllTabRecords.set(k, tempList);
        //            System.out.println("#### installmentAllTabRecords : "+installmentAllTabRecords.get(k));
        //        }
        //        System.out.println("##### tot = "+tot);
        //        System.out.println("##### tableTot = "+tableTot);
        //        double finalTot =0;
        //        if (tot>=tableTot) {
        //            finalTot = t+(tot-tableTot);
        //        }
        //        else
        //            finalTot = t-(tableTot-tot);
        //            tempList = (ArrayList)installmentAllTabRecords.get(k);
        //            tempList.set(5, new Double(finalTot));
        //            installmentAllTabRecords.set(k, tempList);
        double t=0;
        int rowcount=tblInstallment.getRowCount();
        //System.out.println("#####nrowcounttotal"+rowcount);
        
        String totals=CommonUtil.convertObjToStr(tblInstallment.getValueAt(rowcount-1, 4));
        double tot =0;
        if(existTotals>0){
            tot = existTotals;
            rowcount+=1;
        }
        else
             tot = Double.parseDouble(totals);
        t=LoanCalculateInterest.amountRouding(tot/(rowcount-1));
        double tableTot = t*(rowcount-1);
        int k;
        
        for(k=0;k<rowcount-2;k++) {
            //System.out.println("##### k = "+k);
            tblInstallment.setValueAt(String.valueOf(t), k, 5);
        }
        //System.out.println("##### tot = "+tot);
        //System.out.println("##### tableTot = "+tableTot);
        double finalTot =0;
        try {
            if (tot>=tableTot) {
                finalTot = t+(tot-tableTot);
            }
            else
                finalTot = t-(tableTot-tot);
            tblInstallment.setValueAt(String.valueOf(finalTot), k, 5);
        } catch (java.lang.IllegalArgumentException e){
            e.printStackTrace();
        }
    }
    public boolean checkLoanLimit(){
        return isLimitAmtTallyRepayAmt;
    }
    
    
    
    public void storeEmiAmount()
    {    HashMap mapData=new HashMap();
         HashMap hash=new HashMap();
         try {
             proxy = ProxyFactory.createProxy();
             operationMap.put(CommonConstants.JNDI, "TermLoanJNDI");
             operationMap.put(CommonConstants.HOME, "termloan.TermLoanHome");
             operationMap.put(CommonConstants.REMOTE, "termloan.TermLoan");
             
             if(installmentAllTabRecords.size()>0){
                 hash.put("ALL_EMI_RECORDS",installmentAllTabRecords);
                 //System.out.println("this for list"+installmentAllTabRecords);
                 mapData =  (HashMap) proxy.executeQuery(hash, operationMap);
                 //System.out.println("mapdata"+mapData);
             }
         } catch (Exception e) {
             parseException.logException(e,true);
             e.printStackTrace();
             log.info("TermLoanstoreemiamount..."+e);
         }
         
         
    }
    
    
    
    
    
    public void validateLoanLimitAmount(){
        if (repayAmt >= CommonUtil.convertObjToDouble(getTxtLoanAmt()).doubleValue()){
            isLimitAmtTallyRepayAmt = true;
            setTotalRepayAmount(repayAmt);
        }else{
            isLimitAmtTallyRepayAmt = false;
        }
    }
    
    public int addInstallmentDetails(int recordPosition, boolean update){
        int option = -1;
        try{
            installmentTabRow = new ArrayList();
            installmentRecord = new HashMap();
            HashMap result = new HashMap();
            ArrayList data = ((TableModel)tblInstallment.getModel()).getDataArrayList();
            ((TableModel)tblInstallment.getModel()).setData(data);
            ((TableModel)tblInstallment.getModel()).setHeading(installmentTitle);
            final int dataSize = data.size();
            insertInstallment(dataSize+1);
            if (!update) {
                // If the table is not in Edit Mode
                result = tableUtilInstallment.insertTableValues(installmentTabRow, installmentRecord);
                
                installmentAllTabRecords = (ArrayList) result.get(TABLE_VALUES);
                installmentAll = (LinkedHashMap) result.get(ALL_VALUES);
                option = CommonUtil.convertObjToInt(result.get(OPTION));
                
                //                tblInstallment.setDataArrayList(installmentAllTabRecords, installmentTitle);
                ((TableModel)tblInstallment.getModel()).setData(data);
                ((TableModel)tblInstallment.getModel()).setHeading(installmentTitle);
                repayAmt += CommonUtil.convertObjToDouble(txtTotal).doubleValue();
            }else{
                option = updateInstallmentTab(recordPosition);
            }
            
            setChanged();
            
            installmentTabRow = null;
            installmentRecord = null;
            result = null;
            data = null;
        }catch(Exception e){
            log.info("Error in addInstallmentDetails: "+e);
            parseException.logException(e,true);
        }
        return option;
    }
    
    private void insertInstallment(int recordPosition){
        installmentTabRow.add(String.valueOf(recordPosition));
        installmentTabRow.add(getTdtInstallmentDate());
        installmentTabRow.add(getTxtPrincipalAmt());
        installmentTabRow.add(getTxtInterest());
        installmentTabRow.add(getTxtTotal());
        installmentTabRow.add(getTxtBalance());
        
        installmentRecord.put(SLNO, String.valueOf(recordPosition));
        installmentRecord.put(INSTALLMENT_DATE, getTdtInstallmentDate());
        installmentRecord.put(PRINCIPAL, getTxtPrincipalAmt());
        installmentRecord.put(INTEREST_RATE, getTxtInterest());
        installmentRecord.put(TOTAL, getTxtTotal());
        installmentRecord.put(BALANCE, getTxtBalance());
        installmentRecord.put(COMMAND, "");
    }
    
    private int updateInstallmentTab(int recordPosition){
        int option = -1;
        HashMap result = new HashMap();
        try{
            //            double prevAmt = CommonUtil.convertObjToDouble(((ArrayList) (tblInstallment.getDataArrayList().get(recordPosition))).get(4)).doubleValue();
            //            result = tableUtilInstallment.updateTableValues(installmentTabRow, installmentRecord, recordPosition);
            //
            //            installmentAllTabRecords = (ArrayList) result.get(TABLE_VALUES);
            //            installmentAll = (LinkedHashMap) result.get(ALL_VALUES);
            //            option = CommonUtil.convertObjToInt(result.get(OPTION));
            //
            //            tblInstallment.setDataArrayList(installmentAllTabRecords, installmentTitle);
            //            repayAmt -= prevAmt;
            //            repayAmt += CommonUtil.convertObjToDouble(txtTotal).doubleValue();
        }catch(Exception e){
            log.info("Error in updateInstallmentTab: "+e);
            parseException.logException(e,true);
        }
        result = null;
        return option;
    }
    
    public void populateInstallmentDetails(int recordPosition){
        try{
            //            HashMap eachRecs;
            //            java.util.Set keySet =  installmentAll.keySet();
            //            Object[] objKeySet = (Object[]) keySet.toArray();
            //            String strRecordKey = (String) ((ArrayList) (tblInstallment.getDataArrayList().get(recordPosition))).get(0);
            //
            //            // To populate the corresponding record from the Installment Details
            //            for (int i = keySet.size() - 1, j = 0;i >= 0;--i, ++j){
            //                if (((String) ((HashMap) installmentAll.get(objKeySet[j])).get(SLNO)).equals(strRecordKey)){
            //                    // To populate the Corresponding record from CTable
            //                    eachRecs = (HashMap) installmentAll.get(objKeySet[j]);
            //
            //                    setTdtInstallmentDate(CommonUtil.convertObjToStr(eachRecs.get(INSTALLMENT_DATE)));
            //                    setTxtPrincipalAmt(CommonUtil.convertObjToStr(eachRecs.get(PRINCIPAL)));
            //                    setTxtInterest(CommonUtil.convertObjToStr(eachRecs.get(INTEREST_RATE)));
            //                    setTxtTotal(CommonUtil.convertObjToStr(eachRecs.get(TOTAL)));
            //                    setTxtBalance(CommonUtil.convertObjToStr(eachRecs.get(BALANCE)));
            //                    break;
            //                }
            //                eachRecs = null;
            //            }
            //            keySet = null;
            //
            //            objKeySet = null;
            //            setChanged();
            //            ttNotifyObservers();
        }catch(Exception e){
            log.info("Error in populateInstallmentDetails: "+e);
            parseException.logException(e,true);
        }
    }
    
    public void deleteInstallmentTabRecord(int recordPosition){
        HashMap result = new HashMap();
        try{
            //            result = tableUtilInstallment.deleteTableValues(recordPosition);
            //
            //            installmentAllTabRecords = (ArrayList) result.get(TABLE_VALUES);
            //            installmentAll = (LinkedHashMap) result.get(ALL_VALUES);
            //
            //            tblInstallment.setDataArrayList(installmentAllTabRecords, installmentTitle);
            //            repayAmt -= CommonUtil.convertObjToDouble(txtTotal).doubleValue();
        }catch(Exception e){
            log.info("Error in deleteInstallmentTabRecord: "+e);
            parseException.logException(e,true);
        }
        result = null;
    }
    
    // To create objects
    public void createObject(){
        installmentAllTabRecords = new ArrayList();
        //        tblInstallment.setDataArrayList(null, installmentTitle);
        TableModel tableModel = new TableModel();
        tableModel.setHeading(installmentTitle);
        tblInstallment.setModel(tableModel);
        installmentAll = new LinkedHashMap();
        tableUtilInstallment = new TableUtil();
        tableUtilInstallment.setAttributeKey(SLNO);
    }
    
    // To destroy Objects
    public void destroyObjects(){
        installmentAllTabRecords = null;
        installmentAll = null;
        tableUtilInstallment = null;
        memberTypeMap = null;
        deletedMemberTypeMap = null;
        depositTypeMap=null;
        deletedDepositTypeMap=null;
        salarySecurityAll = null;
        salarySecurityDeleteAll=null;
        tblSalarySecrityTable.setDataArrayList(null,salaryTitle);
        setChkRenew("N");
        resetSecurityMemberTableValues();
        resetSecurityCollateralTableValues();
        resetDepositTypeTableValues();
        resetCalarySecurity();
    }
    
    /**
     * Getter for property totalRepayAmount.
     * @return Value of property totalRepayAmount.
     */
    public double getTotalRepayAmount() {
        return totalRepayAmount;
    }
    
    /**
     * Setter for property totalRepayAmount.
     * @param totalRepayAmount New value of property totalRepayAmount.
     */
    public void setTotalRepayAmount(double totalRepayAmount) {
        this.totalRepayAmount = totalRepayAmount;
    }
    
    /**
     * Getter for property cboroundOfType.
     * @return Value of property cboroundOfType.
     */
    public java.lang.String getCboroundOfType() {
        return cboroundOfType;
    }
    
    /**
     * Setter for property cboroundOfType.
     * @param cboroundOfType New value of property cboroundOfType.
     */
    public void setCboroundOfType(java.lang.String cboroundOfType) {
        this.cboroundOfType = cboroundOfType;
    }
    
    /**
     * Getter for property cboRepaymentType.
     * @return Value of property cboRepaymentType.
     */
    public java.lang.String getCboRepaymentType() {
        return cboRepaymentType;
    }
    
    /**
     * Setter for property cboRepaymentType.
     * @param cboRepaymentType New value of property cboRepaymentType.
     */
    public void setCboRepaymentType(java.lang.String cboRepaymentType) {
        this.cboRepaymentType = cboRepaymentType;
    }
    
    /**
     * Getter for property cbmroundOfType.
     * @return Value of property cbmroundOfType.
     */
    public com.see.truetransact.clientutil.ComboBoxModel getCbmroundOfType() {
        return cbmroundOfType;
    }
    
    /**
     * Setter for property cbmroundOfType.
     * @param cbmroundOfType New value of property cbmroundOfType.
     */
    public void setCbmroundOfType(com.see.truetransact.clientutil.ComboBoxModel cbmroundOfType) {
        this.cbmroundOfType = cbmroundOfType;
    }
    
    /**
     * Getter for property cbmRepaymentType.
     * @return Value of property cbmRepaymentType.
     */
    public com.see.truetransact.clientutil.ComboBoxModel getCbmRepaymentType() {
        return cbmRepaymentType;
    }
    
    /**
     * Setter for property cbmRepaymentType.
     * @param cbmRepaymentType New value of property cbmRepaymentType.
     */
    public void setCbmRepaymentType(com.see.truetransact.clientutil.ComboBoxModel cbmRepaymentType) {
        this.cbmRepaymentType = cbmRepaymentType;
    }
    
    /**
     * Getter for property hideSubmit.
     * @return Value of property hideSubmit.
     */
    public boolean isHideSubmit() {
        return hideSubmit;
    }
    
    /**
     * Setter for property hideSubmit.
     * @param hideSubmit New value of property hideSubmit.
     */
    public void setHideSubmit(boolean hideSubmit) {
        this.hideSubmit = hideSubmit;
    }

    public String getTxtAcctNo() {
        return txtAcctNo;
    }

    public void setTxtAcctNo(String txtAcctNo) {
        this.txtAcctNo = txtAcctNo;
    }
   
    public HashMap getRepaymentDetailsForAcctno(HashMap acctMap){
        HashMap detailMap = new HashMap();
         List lst = ClientUtil.executeQuery("getRepaymentDetailsForAcctno", acctMap);
         if(lst != null && lst.size() > 0){
            detailMap = (HashMap)lst.get(0);
         }
         return detailMap;
    }
    
    public HashMap getLoansDetailsForScheduleCreation(HashMap acctMap){
        
        HashMap detailMap = new HashMap();
         List lst = ClientUtil.executeQuery("getLoansDetailsForScheduleCreation", acctMap);
         if(lst != null && lst.size() > 0){
            detailMap = (HashMap)lst.get(0);
         }
         return detailMap;
    }
    
    // getInterestDetailsWhereConditions
    public String getInterestDetailsConditions(HashMap acctMap){
        HashMap detailMap = new HashMap(); 
        String interestAmount = null;
        List list = ClientUtil.executeQuery("getInterestDetailsWhereConditions", acctMap); 
        HashMap whereConditionMap = new HashMap();
        HashMap finalMap = new HashMap();
        String strIntGetFrom = "";
        String interstType = "";
//        whereConditionMap.put(CATEGORY_ID, "");
//        whereConditionMap.put(AMOUNT, "");
//        whereConditionMap.put(PROD_ID, "");
//        whereConditionMap.put(FROM_DATE, null);
//        whereConditionMap.put(TO_DATE, null);
        if (list != null && list.size() > 0) {
            finalMap = (HashMap) list.get(0);
            whereConditionMap.put(CATEGORY_ID, CommonUtil.convertObjToStr(finalMap.get("CATEGORY")));
            whereConditionMap.put(AMOUNT, new java.math.BigDecimal(CommonUtil.convertObjToDouble(finalMap.get("LIMIT")).doubleValue()));
            whereConditionMap.put(PROD_ID, CommonUtil.convertObjToStr(finalMap.get("PROD_ID")));
            whereConditionMap.put(FROM_DATE, setProperDtFormat(DateUtil.getDateMMDDYYYY(CommonUtil.convertObjToStr(finalMap.get("FROM_DATE")))));
            whereConditionMap.put(TO_DATE, setProperDtFormat(DateUtil.getDateMMDDYYYY(CommonUtil.convertObjToStr(finalMap.get("TO_DATE")))));
            strIntGetFrom = CommonUtil.convertObjToStr(finalMap.get("INT_GET_FROM"));
            interstType = CommonUtil.convertObjToStr(finalMap.get("INTEREST_TYPE"));
        }
        list = null;
        List interstList = new ArrayList();
        if (strIntGetFrom.equals(PROD)) {
            if (interstType.equals("FLOATING_RATE")) {
                interstList = ClientUtil.executeQuery("getSelectProductTermLoanInterestFloatTO", whereConditionMap);
            } else {
                interstList = ClientUtil.executeQuery("getSelectProductTermLoanInterestTO", whereConditionMap); 
            }
        } else if (strIntGetFrom.equals(ACT)) {
            interstList = ClientUtil.executeQuery("getSelectTermLoanInterestTO", acctMap); 
            //interestAmount = list.get(0).;          
        }
        
        //System.out.println("interest list ::" + interstList);
        TermLoanInterestTO  objTermLoanInterestTO  = new TermLoanInterestTO ();
        objTermLoanInterestTO = (TermLoanInterestTO)interstList.get(0);
        interestAmount = objTermLoanInterestTO.getInterest().toString();
        whereConditionMap = null;
        finalMap = null;
        strIntGetFrom = null;
//        if(list != null && list.size() > 0){
//            detailMap = (HashMap)list.get(0);
//         }
      
      return interestAmount; 
    }
    
    private Date setProperDtFormat(Date dt) {
       curr_dt = ClientUtil.getCurrentDate();
        Date tempDt = (Date) curr_dt.clone();
        if (dt != null) {
            tempDt.setDate(dt.getDate());
            tempDt.setMonth(dt.getMonth());
            tempDt.setYear(dt.getYear());
            return tempDt;
        }
        return null;
    }
    
    
    private int addRepaymentDetails(int recordPosition, boolean update){
        int option = -1;
        try{
            tableUtilRepayment = new TableUtil();
            tableUtilRepayment.setAttributeKey(SLNO);
            log.info("Add Repayment Details...");
            repaymentEachTabRecord = new ArrayList();
            repaymentEachRecord = new HashMap();
            HashMap result = new HashMap();            
            insertRepayment(recordPosition);              
            repaymentEachTabRecord.add(3, repaymentEachTabRecord.get(0));
            repaymentEachRecord.put(REF_SCHEDULE_NUMBER, repaymentEachRecord.get(SLNO));                
            result = tableUtilRepayment.insertTableValues(repaymentEachTabRecord, repaymentEachRecord);                
            repaymentTabValues = (ArrayList) result.get(TABLE_VALUES);
            //System.out.println("repaymentTabValues :: " + repaymentTabValues);
            repaymentAll = (LinkedHashMap) result.get(ALL_VALUES);
            //System.out.println("repaymentAll :: " + repaymentAll);
            option = CommonUtil.convertObjToInt(result.get(OPTION));                
            tblRepaymentTab.setDataArrayList(repaymentTabValues, repaymentTabTitle);
            if (repaymentEachRecord.get(ACTIVE_STATUS).equals(YES)){
             //setActiveLoanAmt(activeLoanAmt+CommonUtil.convertObjToDouble(getTxtLaonAmt()).doubleValue());
            }           
            setChanged();
            repaymentEachTabRecord = null;
            repaymentEachRecord = null;
            result = null;
            
        }catch(Exception e){
            log.info("Error in addRepaymentDetails..."+e);
            parseException.logException(e,true);
        }
        return option;
    }
    
    private int updateRepaymentTab(int recordPosition){
        int option = -1;
        HashMap result = new HashMap();
        try{
            String strRecordKey = CommonUtil.convertObjToStr(((ArrayList) (tblRepaymentTab.getDataArrayList().get(recordPosition))).get(0));
            double loanBeforeUpdate = CommonUtil.convertObjToDouble(((HashMap)repaymentAll.get(strRecordKey)).get("LOAN_AMT")).doubleValue();
            String activeStatusBeforeUpdate = CommonUtil.convertObjToStr(((HashMap)repaymentAll.get(strRecordKey)).get(ACTIVE_STATUS));
            double currLoanAmt = CommonUtil.convertObjToDouble(getTxtLoanAmt()).doubleValue();
            String currActiveStatus = CommonUtil.convertObjToStr(repaymentEachRecord.get(ACTIVE_STATUS));
            double dummyLoanAmt = getActiveLoanAmt();
            
            if (activeStatusBeforeUpdate == YES){
                dummyLoanAmt -= loanBeforeUpdate;
            }
            if (currActiveStatus == YES){
                dummyLoanAmt += currLoanAmt;
            }
//            if (CommonUtil.convertObjToDouble(getStrLimitAmt()).doubleValue() < dummyLoanAmt){
//                repayTabWarning("loanExceedLimitWarning");commented regarding outstanding repayment
//                return 1;
//            }
            
            result = tableUtilRepayment.updateTableValues(repaymentEachTabRecord, repaymentEachRecord, recordPosition);
            
            repaymentTabValues = (ArrayList) result.get(TABLE_VALUES);
            repaymentAll = (LinkedHashMap) result.get(ALL_VALUES);
            option = CommonUtil.convertObjToInt(result.get(OPTION));
            
            tblRepaymentTab.setDataArrayList(repaymentTabValues, repaymentTabTitle);
            setActiveLoanAmt(dummyLoanAmt);
            strRecordKey = null;
            activeStatusBeforeUpdate = null;
        }catch(Exception e){
            log.info("Error in updateRepaymentTab..."+e);
            parseException.logException(e,true);
        }
        result = null;
        return option;
    }
    
    private void setRepaymentTabTitle() throws Exception{
        try{
            repaymentTabTitle.add(objTermLoanRB.getString("tblColumnRepaymentScheduleNo"));
            repaymentTabTitle.add(objTermLoanRB.getString("tblColumnRepaymentLoanAmount"));
            repaymentTabTitle.add(objTermLoanRB.getString("tblColumnRepaymentSceduleMode"));
            repaymentTabTitle.add(objTermLoanRB.getString("tblColumnRepaymentRefScheduleNo"));
        }catch(Exception e){
            log.info("Exception in setRepaymentTabTitle(): "+e);
            parseException.logException(e,true);
        }
    }
    
    // Test data : 0001241105869
    
    public void repaymentFillData(java.util.LinkedHashMap data, double totRepayVal) {
        populateEMICalculatedFields(data, totRepayVal);
        deleteInstallment = false;
    }
    
    public void populateEMICalculatedFields(LinkedHashMap map, double totRepayVal){
        try{
            java.util.Set keySet =  map.keySet();
            Object[] objKeySet = (Object[]) keySet.toArray();
            if (keySet.size() > 0){
                setTxtAmtLastInstall(CommonUtil.convertObjToStr(((HashMap)map.get(objKeySet[keySet.size() - 1])).get("TOTAL")));
                if (keySet.size() == 1){
                    setTxtAmtPenulInstall(CommonUtil.convertObjToStr(((HashMap)map.get(objKeySet[keySet.size() - 1])).get("TOTAL")));
                }else{
                    setTxtAmtPenulInstall(CommonUtil.convertObjToStr(((HashMap)map.get(objKeySet[keySet.size() - 2])).get("TOTAL")));
                }
                setTdtLastInstall(CommonUtil.convertObjToStr(((HashMap)map.get(objKeySet[keySet.size() - 1])).get("INSTALLMENT_DATE")));
            }
            setTxtTotalInstallAmt(String.valueOf(totRepayVal));
            //System.out.println("maponly"+map);
            setInstallmentAll(map);
            ttNotifyObservers();
        }catch(Exception e){
            e.printStackTrace();
            log.info("Error In populateEMICalculatedFields() "+e);
            parseException.logException(e,true);
        }
    }
    
    public HashMap getMaxScheduleNumberForScheduleCreation(HashMap acctMap){
        HashMap detailMap = new HashMap();
         List lst = ClientUtil.executeQuery("getMaxScheduleNumberForScheduleCreation", acctMap);
         if(lst != null && lst.size() > 0){
            detailMap = (HashMap)lst.get(0);
         }
         return detailMap; 
    }
    
    public List getMaxScheduleNumberForScheduleCreationList(HashMap acctMap){        
         List lst = ClientUtil.executeQuery("getMaxScheduleNumberForScheduleCreation", acctMap);
         return lst; 
    }
    
    private HashMap getMaxAccountNumberForAcct(HashMap acctMap){
        HashMap detailMap = new HashMap();
        List lst = ClientUtil.executeQuery("getMaxAccountNumberForAcct", acctMap);
        if(lst != null && lst.size() > 0){
            detailMap = (HashMap)lst.get(0);
         }
        return detailMap;
    }
    
    public List getMaxInstallmentScheduleForCreationList(HashMap acctMap){       
         List lst = ClientUtil.executeQuery("getMaxInstallmentScheduleForCreation", acctMap);         
         return lst; 
    }
    public HashMap getMaxInstallmentScheduleForCreation(HashMap acctMap){
        HashMap detailMap = new HashMap();
         List lst = ClientUtil.executeQuery("getMaxInstallmentScheduleForCreation", acctMap);
         if(lst != null && lst.size() > 0){
            detailMap = (HashMap)lst.get(0);
         }
         return detailMap; 
    }
    
   
    private void insertRepayment(int recordPosition){
        repaymentEachTabRecord.add(String.valueOf(recordPosition));
        repaymentEachTabRecord.add(getTxtLoanAmt());
        repaymentEachTabRecord.add(getRepayScheduleMode());
        repaymentEachTabRecord.add(getStrRefScheduleNumber());        
        repaymentEachRecord.put(SLNO, String.valueOf(recordPosition)); 
        repaymentEachRecord.put(ACTIVE_STATUS, "Y");
        repaymentEachRecord.put("SCHEDULE_MODE",getRepayScheduleMode() );
        repaymentEachRecord.put("ADD_SIS", "N");
        repaymentEachRecord.put("POST_DT_CHQ_ALLOW", "N");
        repaymentEachRecord.put("LOAN_AMT", CommonUtil.convertObjToStr(getTxtLoanAmt()));       
        repaymentEachRecord.put("REPAY_TYPE", CommonUtil.convertObjToStr(getCbmRepaymentType().getKeyForSelected()));
        repaymentEachRecord.put("REPAY_FREQ", CommonUtil.convertObjToStr(getCbmFrequency().getKeyForSelected()));
        repaymentEachRecord.put("NO_INSTALLMENTS", CommonUtil.convertObjToStr(getTxtNoOfInstall()));
        repaymentEachRecord.put(FROM_DATE, getFromDate()); // from date - no change
        repaymentEachRecord.put("INSTAL_STDT", getFirstInstallDate()); // first install date FirstInstallDate - correct
        repaymentEachRecord.put(TO_DATE, getTdtLastInstall()); // last installdate  - setTdtLastInstall correct
        repaymentEachRecord.put("REPAY_MOROTORIUM", getTxtMoratorium());        
        repaymentEachRecord.put("TOT_BASE_AMT", CommonUtil.convertObjToStr(getTotalBaseAmount()));
        repaymentEachRecord.put("TOT_INSTALL_AMT", CommonUtil.convertObjToStr(getTxtTotalInstallAmt()));
        repaymentEachRecord.put("LAST_INSTALL_AMT", CommonUtil.convertObjToStr(getTxtAmtLastInstall()));
        repaymentEachRecord.put("PENULTI_AMT_INSTALL", CommonUtil.convertObjToStr(getTxtAmtPenulInstall()));
        repaymentEachRecord.put("INSTALLMENT_DETAILS", getInstallmentAll());       
        //repaymentEachRecord.put("MORATORIUM_INTEREST", getMoratoriumInterestAll());
        repaymentEachRecord.put("DISBURSEMENT_DT", getDisbusmentDate()); // To be set
        repaymentEachRecord.put(REF_SCHEDULE_NUMBER, getStrRefScheduleNumber());        
        repaymentEachRecord.put(COMMAND, "INSERT");
    }

    void doAction() {
        HashMap updateMap = new HashMap();
        HashMap proxyResultMap = new HashMap();
        HashMap existingSchedulesMap = new HashMap();
        HashMap existingInstallmentMap = new HashMap();
        try {
            HashMap acctNumMap = new HashMap();
            acctNumMap.put("ACCT_NUM", getTxtAcctNo());
            if (memberTypeMap != null && memberTypeMap.size() > 0) {
                updateMap.put("MemberTableDetails", memberTypeMap);
            }
            if (deletedMemberTypeMap != null && deletedMemberTypeMap.size() > 0) {
                updateMap.put("deletedMemberTypeData", deletedMemberTypeMap);
            }
            if (collateralMap != null && collateralMap.size() > 0) {
                updateMap.put("CollateralTableDetails", collateralMap);
            }
            if (deletedCollateralMap != null && deletedCollateralMap.size() > 0) {
                updateMap.put("deletedCollateralTypeData", deletedCollateralMap);
            }
            
                updateMap.put("DepositSecurityTableDetails",depositTypeMap);
            
            if (deletedDepositTypeMap != null && deletedDepositTypeMap.size() > 0) {
                updateMap.put("deletedDepositTypeData", deletedDepositTypeMap);
            }
            if(salarySecurityAll !=null &&salarySecurityAll .size()>0 )
                updateMap.put("TermLoanSecuritySalaryTOData",salarySecurityAll);
            if(salarySecurityDeleteAll !=null && salarySecurityDeleteAll .size()>0 )
                updateMap.put("TermLoanSecuritySalaryTODeletedData",salarySecurityDeleteAll);

            HashMap KccRenewalTOMap = insertKccRenewalDetail();
            if (KccRenewalTOMap != null) {
                updateMap.put("KccRenewalTO", KccRenewalTOMap.get("KCC_RENEWAL"));
            }
            updateMap.put("ACCT_NUM",getTxtAcctNo());
            updateMap.put("BORROWER_NO",getTxtBorrowerNo());
            updateMap.put("BORROW_NO",getTxtBorrowerNo());
        
            if (rdoGoldSecurityStockExists.equalsIgnoreCase("Y")) {
                LoansSecurityGoldStockTO objCustomerGoldStockSecurityTO = getCustomerGoldStockSecurityTO();
                updateMap.put("CUST_GOLD_SECURITY_STOCK", "CUST_GOLD_SECURITY_STOCK");
                updateMap.put("CustomerGoldStockSecurityTO", objCustomerGoldStockSecurityTO);
                HashMap goldUpdatemap = new HashMap();
                goldUpdatemap.put("GOLD_REMARKS",getTxtGoldRemarks());
                goldUpdatemap.put("GROSS_WEIGHT", getTxtGrossWeight());
                goldUpdatemap.put("NET_WEIGHT", getTxtNetWeight());
                goldUpdatemap.put("VALUE_OF_GOLD", getTxtValueOfGold());
                goldUpdatemap.put("JEWELLERY_DETAILS", getTxtJewelleryDetails());
                updateMap.put("GOLD_UPDATE_MAP", goldUpdatemap);
            }else if(getTxtValueOfGold().length() > 0 && CommonUtil.convertObjToDouble(getTxtValueOfGold()) > 0){// Added by nithya on 29-04-2020 for KD 1834
                GoldLoanSecurityTO obgGoldLoanSecurityTO = setGoldLoanSecurityTo();
                updateMap.put("GOLD_LOAN_SECURITY",obgGoldLoanSecurityTO);
                updateMap.put("GOLD_REMARKS",getTxtGoldRemarks());
            }
            updateMap.put("SECURITY_AMT",getTxtLimitAmount());
            if(getChkRenew().equalsIgnoreCase("Y")){
                updateMap.put("RENEWAL_OD_KCC","RENEWAL_OD_KCC");
            }
            updateMap.put("USER",TrueTransactMain.USER_ID);
            if (getChargelst() != null && getChargelst().size() > 0) { 
                updateMap.put("CHARGE_LIST_DATA", getChargelst());
                if (getServiceTax_Map() != null && getServiceTax_Map().size() > 0
                        && CommonUtil.convertObjToDouble(getServiceTax_Map().get("TOT_TAX_AMT")) > 0) { // Added by nithya on 30-12-2019 for KD-1131
                    updateMap.put("serviceTax_Details", getServiceTax_Map());
                    serviceTax_Map.put("act_num", getTxtAcctNo());
                    updateMap.put("serviceTax_DetailsTo", setServiceTaxDetails(serviceTax_Map));
                }
                if (allowedTransactionDetailsTO != null && allowedTransactionDetailsTO.size() > 0) {
                    if (transactionDetailsTO == null) {
                        transactionDetailsTO = new LinkedHashMap();
                    }
                    transactionDetailsTO.put(NOT_DELETED_TRANS_TOs, allowedTransactionDetailsTO);
                    updateMap.put("TransactionTO", transactionDetailsTO);
                    allowedTransactionDetailsTO = null;
                }
            }
            //System.out.println("updateMap :: "+ updateMap);    
            updateMap.put("PROD_ID",getProdId());
            updateMap.put(CommonConstants.SCREEN,getScreen());
            proxyResultMap = proxy.execute(updateMap, operationMap);
            setResult(ClientConstants.RESULT_STATUS[1]);
            repaymentAll = null;   
            destroyObjects();

        } catch (Exception e) {
            setResult(ClientConstants.RESULT_STATUS[4]);
            parseException.logException(e, true);
            e.printStackTrace();
        }
    }
    
    
    public ServiceTaxDetailsTO setServiceTaxDetails(HashMap serviceTax_Map) { // Added by nithya on 30-12-2019 for KD-1131
        final ServiceTaxDetailsTO objservicetaxDetTo = new ServiceTaxDetailsTO();
        try {            
            objservicetaxDetTo.setStatus(CommonConstants.STATUS_CREATED);
            objservicetaxDetTo.setStatusBy(TrueTransactMain.USER_ID);
            objservicetaxDetTo.setAcct_Num(CommonUtil.convertObjToStr(serviceTax_Map.get("act_num")));
            objservicetaxDetTo.setParticulars("Deposit Closing");
            objservicetaxDetTo.setBranchID(ProxyParameters.BRANCH_ID);
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
            if (serviceTax_Map != null && serviceTax_Map.containsKey("SWACHH_CESS")) {
                objservicetaxDetTo.setSwachhCess(CommonUtil.convertObjToDouble(serviceTax_Map.get("SWACHH_CESS")));
            }
            if (serviceTax_Map != null && serviceTax_Map.containsKey("KRISHI_KALYAN_CESS")) {
                objservicetaxDetTo.setKrishiKalyan(CommonUtil.convertObjToDouble(serviceTax_Map.get("KRISHI_KALYAN_CESS")));
            }
            if (serviceTax_Map != null && serviceTax_Map.containsKey("TOT_TAX_AMT")) {
                objservicetaxDetTo.setTotalTaxAmt(CommonUtil.convertObjToDouble(serviceTax_Map.get("TOT_TAX_AMT")));
            }
            double roudVal = objservicetaxDetTo.getTotalTaxAmt() - (objservicetaxDetTo.getServiceTaxAmt() + objservicetaxDetTo.getEducationCess() + objservicetaxDetTo.getHigherCess() + objservicetaxDetTo.getSwachhCess() + objservicetaxDetTo.getKrishiKalyan());
            ServiceTaxCalculation serviceTaxObj = new ServiceTaxCalculation();
            objservicetaxDetTo.setRoundVal(CommonUtil.convertObjToStr(serviceTaxObj.roundOffAmtForRoundVal(roudVal)));
            objservicetaxDetTo.setStatusDt((Date) curDate.clone());
                objservicetaxDetTo.setCreatedBy(TrueTransactMain.USER_ID);
                objservicetaxDetTo.setCreatedDt((Date) curDate.clone());         
        } catch (Exception e) {
            e.printStackTrace();
        }
        return objservicetaxDetTo;

    }
    
    private GoldLoanSecurityTO setGoldLoanSecurityTo(){
        GoldLoanSecurityTO objTermLoanSecurityTO = new GoldLoanSecurityTO();
        objTermLoanSecurityTO.setSlNo(0);
        objTermLoanSecurityTO.setAcctNum(getTxtAcctNo());
        objTermLoanSecurityTO.setStatus(CommonConstants.STATUS_CREATED);
        objTermLoanSecurityTO.setAsOn((Date) curDate.clone());
        objTermLoanSecurityTO.setGrossWeight(CommonUtil.convertObjToDouble(getTxtGrossWeight()));
        objTermLoanSecurityTO.setNetWeight(CommonUtil.convertObjToDouble(getTxtNetWeight()));
        objTermLoanSecurityTO.setPurity("");
        objTermLoanSecurityTO.setMarketRate("");
        objTermLoanSecurityTO.setSecurityValue(CommonUtil.convertObjToStr(getTxtValueOfGold()));   
        objTermLoanSecurityTO.setMargin("");
        objTermLoanSecurityTO.setMarginAmt("");
        objTermLoanSecurityTO.setEligibleLoanAmt(CommonUtil.convertObjToStr(getTxtLimitAmount()));
        objTermLoanSecurityTO.setAppraiserId(""); 
        objTermLoanSecurityTO.setParticulars(getTxtJewelleryDetails());         
        objTermLoanSecurityTO.setNoofPacket(1);
        return objTermLoanSecurityTO;
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

        }
        
    }
    
    private HashMap insertKccRenewalDetail(){
        HashMap renewalMap = new HashMap();
        try{
            KccRenewalTO KccRenewalTO = new KccRenewalTO();
            KccRenewalTO.setActNum(getTxtAcctNo());
            KccRenewalTO.setFromDt(getProperDateFormat(getTdtFromDt()));
            KccRenewalTO.setToDt(getProperDateFormat(getTdtToDt()));
            KccRenewalTO.setStatus("CREATED");
            KccRenewalTO.setBranchID(TrueTransactMain.BRANCH_ID);
            KccRenewalTO.setStatusDt(curDate);
            KccRenewalTO.setLimit(CommonUtil.convertObjToDouble(getTxtLimitAmount()));
            renewalMap.put("KCC_RENEWAL", KccRenewalTO);
            }catch(Exception e){
                e.printStackTrace();
                log.info("Exception caught In insertKccRenewalDetail: "+e);
                parseException.logException(e,true);
            }
        return renewalMap;
        
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
   
   
   public HashMap setTermLoanRepayment(boolean isnew){
        HashMap repayInstallmentMap = new HashMap();
        HashMap repaymentMap = new HashMap();
        HashMap installmentMap = new HashMap();
        HashMap installMultIntMap = new HashMap();
        boolean reschudleavailable=false;
        try{            
            LinkedHashMap interestRecords;
            HashMap interestOneRecord;
            TermLoanRepaymentTO termLoanRepaymentTO;
            TermLoanInstallmentTO termLoanInstallmentTO;
            TermLoanInstallMultIntTO termLoanInstallMultIntTO;
            ArrayList installMultiIntList;
            java.util.Set interestKeySet;
            Object[] objInterestKeySet;
            java.util.Set keySet =  repaymentAll.keySet();
            Object[] objKeySet = (Object[]) keySet.toArray();
            HashMap oneRecord;
            for (int i = keySet.size() - 1,j = 0;i >= 0;--i,++j){
                oneRecord = (HashMap) repaymentAll.get(objKeySet[j]);
                termLoanRepaymentTO = new TermLoanRepaymentTO();
                termLoanRepaymentTO.setAcctNum(getTxtAcctNo());
                //                termLoanRepaymentTO.setBorrowNo(getBorrowerNo());
                termLoanRepaymentTO.setScheduleNo(CommonUtil.convertObjToDouble(oneRecord.get(REF_SCHEDULE_NUMBER)));
                termLoanRepaymentTO.setCommand(CommonUtil.convertObjToStr(oneRecord.get(COMMAND)));
//                if (oneRecord.get(ADD_SIS).equals(YES)){
//                    termLoanRepaymentTO.setAddSi(YES);
//                }else if (oneRecord.get(ADD_SIS).equals(NO)){
                   termLoanRepaymentTO.setAddSi("N");
//                }else{
//                    termLoanRepaymentTO.setAddSi(" ");
//                }
                termLoanRepaymentTO.setAmtLastInstall(CommonUtil.convertObjToDouble(oneRecord.get("LAST_INSTALL_AMT")));
                termLoanRepaymentTO.setAmtPenultimateInstall(CommonUtil.convertObjToDouble(oneRecord.get("PENULTI_AMT_INSTALL")));
                
//              termLoanRepaymentTO.setFirstInstallDt((Date)oneRecord.get(FROM_DATE));
                termLoanRepaymentTO.setFromDate(getProperDateFormat(oneRecord.get(FROM_DATE)));
                termLoanRepaymentTO.setFirstInstallDt(getProperDateFormat(oneRecord.get("INSTAL_STDT")));
//                if(isnew)
//                    termLoanRepaymentTO.setRepaymentType(CommonUtil.convertObjToDouble(getCbmRepaymentType().getKeyForSelected()));
//                 else
                    termLoanRepaymentTO.setRepaymentType(CommonUtil.convertObjToDouble(oneRecord.get("REPAY_FREQ")));
                
//              termLoanRepaymentTO.setLastInstallDt((Date)oneRecord.get(TO_DATE));
                termLoanRepaymentTO.setLastInstallDt(getProperDateFormat(oneRecord.get(TO_DATE)));
                termLoanRepaymentTO.setLoanAmount(CommonUtil.convertObjToDouble(oneRecord.get("LOAN_AMT")));
                termLoanRepaymentTO.setNoInstallments(CommonUtil.convertObjToDouble(oneRecord.get("NO_INSTALLMENTS")));
//                if (oneRecord.get("POST_DT_CHQ_ALLOW").equals(YES)){
//                    termLoanRepaymentTO.setPostDateChqallowed(YES);
//                }else if (oneRecord.get("POST_DT_CHQ_ALLOW").equals(NO)){
                   termLoanRepaymentTO.setPostDateChqallowed("N");
//                }else{
//                    termLoanRepaymentTO.setPostDateChqallowed(" ");
//                }
                if (oneRecord.get(ACTIVE_STATUS).equals("Y")){
                    termLoanRepaymentTO.setRepayActive("Y");
                }else if (oneRecord.get(ACTIVE_STATUS).equals("N")){
                    termLoanRepaymentTO.setRepayActive("N");
                }else{
                    termLoanRepaymentTO.setRepayActive(" ");
                }
                //                termLoanRepaymentTO.setProdId(CommonUtil.convertObjToStr(getLblProdID_RS_Disp()));
                termLoanRepaymentTO.setInstallType(CommonUtil.convertObjToStr(oneRecord.get("REPAY_TYPE")));
                termLoanRepaymentTO.setTotalBaseAmt(CommonUtil.convertObjToDouble(oneRecord.get("TOT_BASE_AMT")));
                termLoanRepaymentTO.setTotalInstallAmt(CommonUtil.convertObjToDouble(oneRecord.get("TOT_INSTALL_AMT")));
                
//                termLoanRepaymentTO.setDisbursementDt((Date)oneRecord.get(DISBURSEMENT_DT));
                termLoanRepaymentTO.setDisbursementDt(getProperDateFormat(oneRecord.get("DISBURSEMENT_DT")));
                // These fields are in server side and not in UI
                //            termLoanRepaymentTO.setRepayInterest(new);
                //            termLoanRepaymentTO.setIntType();
                //            termLoanRepaymentTO.setEmi();
                //            termLoanRepaymentTO.setRepaymentPr();
                //            termLoanRepaymentTO.setBalanceLoanAmt();
                termLoanRepaymentTO.setDisbursementId(CommonUtil.convertObjToDouble(oneRecord.get("DISBURSEMENT_ID")));
                termLoanRepaymentTO.setRepayMorotorium(CommonUtil.convertObjToDouble(oneRecord.get("REPAY_MOROTORIUM")));
                termLoanRepaymentTO.setScheduleMode(CommonUtil.convertObjToStr(oneRecord.get("SCHEDULE_MODE")));
                termLoanRepaymentTO.setRefScheduleNo(CommonUtil.convertObjToDouble(oneRecord.get(REF_SCHEDULE_NUMBER)));
                if (oneRecord.get(COMMAND).equals("INSERT")){
                    termLoanRepaymentTO.setStatus(CommonConstants.STATUS_CREATED);
                }else if (oneRecord.get(COMMAND).equals("UPDATE")){
                    termLoanRepaymentTO.setStatus(CommonConstants.STATUS_MODIFIED);
                }
                termLoanRepaymentTO.setStatusBy(TrueTransactMain.USER_ID);
                termLoanRepaymentTO.setStatusDt(curDate);
                
                repaymentMap.put(String.valueOf(j+1), termLoanRepaymentTO);
                if (oneRecord.get("INSTALLMENT_DETAILS") != null){  // && (oneRecord.get(COMMAND).equals(INSERT))  //Condition removed to insert in update mode also
                    interestRecords = ((LinkedHashMap)oneRecord.get("INSTALLMENT_DETAILS"));
                    interestKeySet =  interestRecords.keySet();
                    objInterestKeySet = (Object[]) interestKeySet.toArray();
                    int noOfInstallment = interestKeySet.size();
                    for (int m = noOfInstallment - 1;m >= 0;--m){
                        interestOneRecord = (HashMap) interestRecords.get(objInterestKeySet[m]);
                        termLoanInstallmentTO = new TermLoanInstallmentTO();
                        termLoanInstallmentTO.setAcctNum(getTxtAcctNo());
                        termLoanInstallmentTO.setBalanceAmt(CommonUtil.convertObjToDouble(interestOneRecord.get(BALANCE)));
                        termLoanInstallmentTO.setCommand(CommonUtil.convertObjToStr(interestOneRecord.get(COMMAND)));
                        
//                        termLoanInstallmentTO.setInstallmentDt((Date)interestOneRecord.get(INSTALLMENT_DATE));
                        termLoanInstallmentTO.setInstallmentDt(getProperDateFormat(interestOneRecord.get(INSTALLMENT_DATE)));
                        termLoanInstallmentTO.setInstallmentSlno(CommonUtil.convertObjToDouble(interestOneRecord.get(SLNO)));
                        termLoanInstallmentTO.setInterestAmt(CommonUtil.convertObjToDouble(interestOneRecord.get("INTEREST_AMOUNT")));
                        termLoanInstallmentTO.setInterestRate(CommonUtil.convertObjToDouble(interestOneRecord.get(INTEREST_RATE)));
                        termLoanInstallmentTO.setPrincipalAmt(CommonUtil.convertObjToDouble(interestOneRecord.get(PRINCIPAL)));
                        termLoanInstallmentTO.setScheduleId(CommonUtil.convertObjToDouble(oneRecord.get(REF_SCHEDULE_NUMBER)));
                        termLoanInstallmentTO.setTotalAmt(CommonUtil.convertObjToDouble(interestOneRecord.get(TOTAL)));
                         termLoanInstallmentTO.setActiveStatus(CommonUtil.convertObjToStr(interestOneRecord.get("ACTIVE_STATUS")));
                        termLoanInstallmentTO.setInstallmentPaid("N");
                        if (interestOneRecord.containsKey("MULTIPLE_INTEREST")){
                            installMultiIntList = (ArrayList) interestOneRecord.get("MULTIPLE_INTEREST");
                            HashMap multiIntRec;
                            for (int n = installMultiIntList.size()-1;n>=0;--n){
                                multiIntRec = (HashMap) installMultiIntList.get(n);
                                termLoanInstallMultIntTO = new TermLoanInstallMultIntTO();
                                termLoanInstallMultIntTO.setAcctNum(getTxtAcctNo());
                                if (interestOneRecord.get(COMMAND).equals("INSERT")){
                                    termLoanInstallMultIntTO.setCommand(CommonConstants.TOSTATUS_INSERT);
                                }else if (interestOneRecord.get(COMMAND).equals("UPDATE")){
                                    termLoanInstallMultIntTO.setCommand(CommonConstants.TOSTATUS_UPDATE);
                                }
                                termLoanInstallMultIntTO.setInstallmentSlno(termLoanInstallmentTO.getInstallmentSlno());
                                termLoanInstallMultIntTO.setInterestRate(CommonUtil.convertObjToDouble(multiIntRec.get(INTEREST_RATE)));
                                termLoanInstallMultIntTO.setFromDt(getProperDateFormat(multiIntRec.get(FROM_DATE)));
                                termLoanInstallMultIntTO.setToDt(getProperDateFormat(multiIntRec.get(TO_DATE)));
                                
//                                termLoanInstallMultIntTO.setFromDt((Date)multiIntRec.get(FROM_DATE));
//                                
//                                termLoanInstallMultIntTO.setToDt((Date)multiIntRec.get(TO_DATE));
                                termLoanInstallMultIntTO.setScheduleId(termLoanInstallmentTO.getScheduleId());
                                installMultIntMap.put(String.valueOf(installMultIntMap.size()), termLoanInstallMultIntTO);
                                termLoanInstallMultIntTO = null;
                                multiIntRec = null;
                            }
                            multiIntRec = null;
                            termLoanInstallMultIntTO = null;
                            installMultiIntList = null;
                        }
                        if (interestOneRecord.get(COMMAND).equals("INSERT")){
                            termLoanInstallmentTO.setStatus(CommonConstants.STATUS_CREATED);
                            reschudleavailable=true;
                        }else if (interestOneRecord.get(COMMAND).equals("UPDATE")){
                            termLoanInstallmentTO.setStatus(CommonConstants.STATUS_MODIFIED);
                        }
                        installmentMap.put(String.valueOf(installmentMap.size()+1), termLoanInstallmentTO);
                        interestOneRecord = null;
                    }
                }
                if (oneRecord.get("MORATORIUM_INTEREST") != null && (oneRecord.get(COMMAND).equals("INSERT"))){
                    interestRecords = ((LinkedHashMap)oneRecord.get("MORATORIUM_INTEREST"));
                    interestKeySet =  interestRecords.keySet();
                    objInterestKeySet = (Object[]) interestKeySet.toArray();
                    HashMap moratoriumIntRec = (HashMap) interestRecords.get(objInterestKeySet[0]);
                    if (moratoriumIntRec.containsKey("MULTIPLE_INTEREST")){
                        installMultiIntList = (ArrayList) moratoriumIntRec.get("MULTIPLE_INTEREST");
                        HashMap multiIntRec;
                        for (int n = installMultiIntList.size()-1;n>=0;--n){
                            multiIntRec = (HashMap) installMultiIntList.get(n);
                            termLoanInstallMultIntTO = new TermLoanInstallMultIntTO();
                            termLoanInstallMultIntTO.setAcctNum(getTxtAcctNo());
                            if (oneRecord.get(COMMAND).equals("INSERT")){
                                termLoanInstallMultIntTO.setCommand(CommonConstants.TOSTATUS_INSERT);
                            }else if (oneRecord.get(COMMAND).equals("UPDATE")){
                                termLoanInstallMultIntTO.setCommand(CommonConstants.TOSTATUS_UPDATE);
                            }
                            termLoanInstallMultIntTO.setInterestRate(CommonUtil.convertObjToDouble(multiIntRec.get(INTEREST_RATE)));
                            termLoanInstallMultIntTO.setFromDt(getProperDateFormat(multiIntRec.get(FROM_DATE)));
                            termLoanInstallMultIntTO.setToDt(getProperDateFormat(multiIntRec.get(TO_DATE)));
                            
//                             termLoanInstallMultIntTO.setFromDt((Date)multiIntRec.get(FROM_DATE));
//                             
//                            termLoanInstallMultIntTO.setToDt((Date)multiIntRec.get(TO_DATE));
                            termLoanInstallMultIntTO.setScheduleId(CommonUtil.convertObjToDouble(oneRecord.get(SLNO)));
                            installMultIntMap.put(String.valueOf(installMultIntMap.size()), termLoanInstallMultIntTO);
                            termLoanInstallMultIntTO = null;
                            multiIntRec = null;
                        }
                        multiIntRec = null;
                        termLoanInstallMultIntTO = null;
                        installMultiIntList = null;
                    }
                    moratoriumIntRec = null;
                }
                oneRecord = null;
                termLoanRepaymentTO = null;
            }
            keySet = null;
            objKeySet = null;
            interestKeySet = null;
            objInterestKeySet = null;
            termLoanInstallMultIntTO = null;
            installMultiIntList = null;
            repayInstallmentMap.put("REPAYMENT_DETAILS", repaymentMap);
            repayInstallmentMap.put("INSTALLMENT_DETAILS", installmentMap);
            repayInstallmentMap.put("INSTALLMULTINT_DETAILS", installMultIntMap); 
            installmentMap = null;
            repaymentMap = null;
            installMultIntMap = null;
        }catch(Exception e){
            log.info("Error In setTermLoanRepayment() "+e);
            parseException.logException(e,true);
        }
        return repayInstallmentMap; 
   } 
   
   public int getIncrementType(){
        int incType = 0;
        try{
            Double incVal = CommonUtil.convertObjToDouble(getCbmFrequency().getKeyForSelected());
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
   
   public void resetOBFields(){
      setCboFrequency("");
      setCboRepaymentType("");
      setCboroundOfType("");
      setTxtAcctNo("");
      setTxtInterest("");
      setTxtInterestRate("");
      setTxtMoratorium("");
      setTxtNoOfInstall("");
      setFirstInstallDate("");
      setFromDate("");
      setDisbusmentDate("");
      setDisbusmentId("");
      setRepayFromDate("");
      setRepayScheduleMode("");
      setToDate("");
      setFirstInstallDate("");
      setTxtLoanAmt("");
      setActiveStatus("");
      setTdtInstallmentDate("");
      setTdtLastInstall("");  
      setTxtBorrowerNo("");
      setTxtLimitAmount("");
      setTdtFromDt("");
      setTdtToDt("");
      setTxtNoOfYears("");
      resetSecurityMemberTableValues();  
      resetSecurityCollateralTableValues();
      resetGoldSecurity();
      this.setServiceTax_Map(null); // Added by nithya on 30-12-2019 for KD-1131
   }
   
    public void resetSecurityCollateralTableValues(){
        tblCollateralDetails.setDataArrayList(null,tableCollateralTitle);
        tblJointCollateral.setDataArrayList(null,tableCollateralJointTitle);
    }
   
    public void resetCalarySecurity(){
        tblSalarySecrityTable.setDataArrayList(null,salaryTitle);
    }
    
    
   /**
     * Setter for property result.
     *
     * @param result New value of property result.
     */
    public void setResult(java.lang.String result) {
        this.result = result;
    }
    
     /**
     * Getter for property result.
     *
     * @return Value of property result.
     */
    public java.lang.String getResult() {
        return result;
    }
    
    public void populateData(HashMap whereMap) {
        log.info("In populateData..."+whereMap);
        //System.out.println("loanType###"+loanType);
        HashMap mapData = null;
        try {
            whereMap.put("UI_PRODUCT_TYPE", "TL");
            whereMap.put("LOAN_TYPE", "OTHERS");           
            //whereMap.put("PRODUCT_CATEGORY", getProductCategory());
            mapData =  (HashMap) proxy.executeQuery(whereMap, operationMap);
            //System.out.println("mapData... inside populatedata :: "+ mapData);   
            populateOB(mapData);            
        } catch( Exception e ) {
            log.info("Exception caught in populateData"+e);
            parseException.logException(e,true);
            e.printStackTrace();
        }
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
     
     public void addDepositTypeTable(int rowSelected, boolean updateMode){
        try{
            int rowSel=rowSelected;
            final TermLoanDepositTypeTO objDepositTypeTO = new TermLoanDepositTypeTO();
            if( depositTypeMap == null ){
                depositTypeMap = new LinkedHashMap();
            }
           
                if(isDepositTypeData()){
                    objDepositTypeTO.setStatusDt(ClientUtil.getCurrentDate());
                    objDepositTypeTO.setStatusBy(TrueTransactMain.USER_ID);
                    objDepositTypeTO.setStatus(CommonConstants.STATUS_CREATED);
                }else{
                    objDepositTypeTO.setStatusDt(ClientUtil.getCurrentDate());
                    objDepositTypeTO.setStatusBy(TrueTransactMain.USER_ID);
                    objDepositTypeTO.setStatus(CommonConstants.STATUS_MODIFIED);
                }
            objDepositTypeTO.setBorrowNo(getTxtBorrowerNo());            
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
                incTabRow.add(CommonUtil.convertObjToStr(objDepositTypeTO.getTxtDepAmount()));           
                incTabRow.add(CommonUtil.convertObjToStr(objDepositTypeTO.getTxtMaturityValue()));             
                tblDepositTypeDetails.addRow(incTabRow);
            }
        }       
    }
    
     
      private void updateDepositTypeDetails(int rowSel,  TermLoanDepositTypeTO objDepositTypeTO, boolean updateMode)  throws Exception{
        Object selectedRow;
        boolean rowExists = false;       
        for(int i = tblDepositTypeDetails.getRowCount(), j = 0; i > 0; i--,j++){
            selectedRow = ((ArrayList)tblDepositTypeDetails.getDataArrayList().get(j)).get(1);           
            if(getTxtDepNo().equals(CommonUtil.convertObjToStr(selectedRow))){
                rowExists = true;
                ArrayList IncParRow = new ArrayList();
                ArrayList data = tblDepositTypeDetails.getDataArrayList();
                data.remove(rowSel);
                IncParRow.add(CommonUtil.convertObjToStr(cboProductTypeSecurity));
                IncParRow.add(getTxtDepNo());             
                IncParRow.add(getTxtDepAmount());              
                IncParRow.add(getTxtMaturityValue());              
                tblDepositTypeDetails.insertRow(rowSel,IncParRow);
                IncParRow = null;
            }
        }
        if(!rowExists){
            ArrayList IncParRow = new ArrayList();
            IncParRow.add(CommonUtil.convertObjToStr(cboProductTypeSecurity));
            IncParRow.add(getTxtDepNo());           
            IncParRow.add(getTxtDepAmount());          
            IncParRow.add(getTxtMaturityValue());          
            tblDepositTypeDetails.insertRow(tblDepositTypeDetails.getRowCount(),IncParRow);
            IncParRow = null;
        }
    }
     
        public void deleteSalarySecrityTableValue(int salarytblSelectedRow) {
        ArrayList totList = new ArrayList();
        if (salarySecurityDeleteAll == null) {
            salarySecurityDeleteAll = new LinkedHashMap();
        }
        if (salarytblSelectedRow != -1) {
            ArrayList singleList = (ArrayList) tblSalarySecrityTable.getDataArrayList().get(salarytblSelectedRow);
            String slno = CommonUtil.convertObjToStr(singleList.get(0));
            TermLoanSecuritySalaryTO objTermLoanSecuritySalaryTO = new TermLoanSecuritySalaryTO();
            objTermLoanSecuritySalaryTO = (TermLoanSecuritySalaryTO) salarySecurityAll.get(String.valueOf(slno));
            objTermLoanSecuritySalaryTO.setStatus(CommonConstants.STATUS_DELETED);
            salarySecurityDeleteAll.put(String.valueOf(slno), objTermLoanSecuritySalaryTO);
            salarySecurityAll.remove(String.valueOf(slno));
            tblSalarySecrityTable.removeRow(salarytblSelectedRow);

        } else {
            ClientUtil.displayAlert("Please Select Record then Delete");
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
                 salarySecurityAll.put(String.valueOf(slno),addTermLoanSecuritySalaryTO(slno));
            }
        }
        
    }    
    
    
    public TermLoanSecuritySalaryTO addTermLoanSecuritySalaryTO(double slno){        
           final TermLoanSecuritySalaryTO objTermLoanSecuritySalaryTO = new TermLoanSecuritySalaryTO();
        try{
            TermLoanSecuritySalaryTO obj =(TermLoanSecuritySalaryTO)salarySecurityAll.get(String.valueOf(slno));             
            objTermLoanSecuritySalaryTO.setSlNo( new Double(slno));
            objTermLoanSecuritySalaryTO.setAcctNum(getTxtAcctNo());
            objTermLoanSecuritySalaryTO.setSalaryCerficateNo(getTxtSalaryCertificateNo());
            objTermLoanSecuritySalaryTO.setEmpName(getTxtEmployerName());
            objTermLoanSecuritySalaryTO.setEmpAddress(getTxtAddress());
            objTermLoanSecuritySalaryTO.setCity(getCboSecurityCity());
            objTermLoanSecuritySalaryTO.setPin(CommonUtil.convertObjToDouble(getTxtPinCode()));
            objTermLoanSecuritySalaryTO.setDesignation(getTxtDesignation());
            objTermLoanSecuritySalaryTO.setContactNo(CommonUtil.convertObjToLong(getTxtContactNo()));           
            objTermLoanSecuritySalaryTO.setRetirementDt(getDateFormat(getTdtRetirementDt()));
            objTermLoanSecuritySalaryTO.setEmpMemberNo(getTxtMemberNum());
            objTermLoanSecuritySalaryTO.setTotalSalary(CommonUtil.convertObjToDouble(getTxtTotalSalary()));
            objTermLoanSecuritySalaryTO.setNetworth(getTxtNetWorth());
            objTermLoanSecuritySalaryTO.setSalaryRemarks(getTxtSalaryRemark());
            if(!(obj !=null && obj.getStatus().equals(CommonConstants.STATUS_MODIFIED)))
                objTermLoanSecuritySalaryTO.setStatus(CommonConstants.STATUS_CREATED);         
            
        }catch(Exception e){
            e.printStackTrace();
        }
        return objTermLoanSecuritySalaryTO;
    }
    
    public Date getDateFormat(String d) {
        Date s = null;
        try {
            java.text.SimpleDateFormat dt = new java.text.SimpleDateFormat("MM/dd/yyyy");
            s = dt.parse(d);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return s;
    }
    
      
    private void populateOB(HashMap mapData) {
        if (mapData.containsKey("memberListTO")) {
            memberTypeMap = (LinkedHashMap) mapData.get("memberListTO");
            ArrayList addList = new ArrayList(memberTypeMap.keySet());
            for (int i = 0; i < addList.size(); i++) {
                TermLoanSecurityMemberTO objMemberTypeTO = (TermLoanSecurityMemberTO) memberTypeMap.get(addList.get(i));
                //System.out.println("TermLoanSecurityMemberTO :: " + objMemberTypeTO);
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
        if (mapData.containsKey("CollateralListTO")) {
            collateralMap = (LinkedHashMap) mapData.get("CollateralListTO");
            ArrayList addList = new ArrayList(collateralMap.keySet());
            for (int i = 0; i < addList.size(); i++) {
                TermLoanSecurityLandTO objTermLoanSecurityLandTO = (TermLoanSecurityLandTO) collateralMap.get(addList.get(i));
                objTermLoanSecurityLandTO.setStatus(CommonConstants.STATUS_MODIFIED);
                ArrayList incTabRow = new ArrayList();
                incTabRow.add(CommonUtil.convertObjToStr(objTermLoanSecurityLandTO.getMemberNo()));
                incTabRow.add(CommonUtil.convertObjToStr(objTermLoanSecurityLandTO.getMemberName()));
                incTabRow.add(CommonUtil.convertObjToStr(objTermLoanSecurityLandTO.getDocumentNo()));
                incTabRow.add(CommonUtil.convertObjToStr(objTermLoanSecurityLandTO.getPledgeAmount()));
                incTabRow.add(CommonUtil.convertObjToStr(objTermLoanSecurityLandTO.getSurveyNo()));
                incTabRow.add(CommonUtil.convertObjToStr(objTermLoanSecurityLandTO.getTotalArea()));
                tblCollateralDetails.addRow(incTabRow);
                collateralMap.remove(objTermLoanSecurityLandTO.getMemberNo());
                collateralMap.put(CommonUtil.convertObjToStr(objTermLoanSecurityLandTO.getMemberNo()) + "_" + (i + 1), objTermLoanSecurityLandTO);
                addPledgeAmountMap(objTermLoanSecurityLandTO.getDocumentNo(), CommonUtil.convertObjToDouble(objTermLoanSecurityLandTO.getPledgeAmount()).doubleValue());

            }
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
            }
        }
        if (mapData.containsKey("TermLoanSecuritySalaryTO")) {
            populateTermLoanSecuritySalaryTOData(mapData);
        }
      
        // Added by nithya on 07-03-2020 for KD-1379
        if (mapData.containsKey("CustomerGoldStockSecurityTO")) {
            LoansSecurityGoldStockTO objLoansSecurityGoldStockTO = (LoansSecurityGoldStockTO) mapData.get("CustomerGoldStockSecurityTO");
            setRdoGoldSecurityStockExists("Y");
            setTxtGoldSecurityId(objLoansSecurityGoldStockTO.getGoldStockId());
        }
        // End
            
        ArrayList sanctionFacilityAuthorizeList = (ArrayList) (mapData.get("TermLoanSanctionFacilityTO.AUTHORIZE"));
        setTermLoanSanctionFacilityTOForAuthorize(sanctionFacilityAuthorizeList);
        ArrayList facilityList = (ArrayList) (mapData.get("TermLoanFacilityTO.AUTHORIZE"));
        HashMap transactionMap = setTermLoanFacilityByAcctNo(facilityList);
        setChanged();
        ttNotifyObservers();
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
    }
    
  
     private HashMap setTermLoanFacilityByAcctNo(ArrayList objFacilityTOList){
        HashMap returnMap = new HashMap();
        try{
            TermLoanFacilityTO objTermLoanFacilityTO;          
            if (objFacilityTOList.size() > 0){
                objTermLoanFacilityTO = (TermLoanFacilityTO) objFacilityTOList.get(0);
                setTxtGoldRemarks(objTermLoanFacilityTO.getTxtGoldRemarks());
                setTxtGrossWeight(objTermLoanFacilityTO.getTxtGrossWeight());
                setTxtJewelleryDetails(objTermLoanFacilityTO.getTxtJewelleryDetails());
                setTxtNetWeight(objTermLoanFacilityTO.getTxtNetWeight());
                setTxtValueOfGold(CommonUtil.convertObjToStr(objTermLoanFacilityTO.getTxtValueOfGold()));
                objTermLoanFacilityTO = null;
            }
        }catch(Exception e){
            log.info("Exception caught in setTermLoanFacilityByAcctNo: "+e);
            parseException.logException(e,true);
        }
        return returnMap;
    }
    
    
    private void setTermLoanSanctionFacilityTOForAuthorize(ArrayList objSanctionFacilityTOList){
        try{
            TermLoanSanctionFacilityTO objTermLoanSanctionFacilityTO;
            objTermLoanSanctionFacilityTO = (TermLoanSanctionFacilityTO) objSanctionFacilityTOList.get(0);
            setTdtFromDt(DateUtil.getStringDate(objTermLoanSanctionFacilityTO.getToDt()));
            setTxtLimitAmount(CommonUtil.convertObjToStr(objTermLoanSanctionFacilityTO.getLimit()));
            setTxtBorrowerNo(objTermLoanSanctionFacilityTO.getBorrowNo());
            //System.out.println("objTermLoanSanctionFacilityTO :: "+ objTermLoanSanctionFacilityTO);
        }catch(Exception e){
            log.info("Exception caught in setTermLoanSanctionFacilityTOForAuthorize: "+e);
            parseException.logException(e,true);
        }
    }
    
   public void resetSecurityMemberTableValues(){
        tblMemberTypeDetails.setDataArrayList(null,tableMemberTitle);        
    }
   
    public void resetDepositTypeTableValues(){
        tblDepositTypeDetails.setDataArrayList(null,tableTitleDepositList);
    }
    
    public void resetGoldSecurity() {
        setTxtJewelleryDetails("");
        setTxtGrossWeight("");
        setTxtGoldRemarks("");
        setTxtValueOfGold("");
        setTxtNetWeight("");
        setRdoGoldSecurityStockExists("N"); // Added by nithya on 07-03-2020 for KD-1379
        setTxtGoldSecurityId("");
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
     private void setMemberTableTile() throws Exception{
        tableMemberTitle.add("Member No");
        tableMemberTitle.add("Name");
        tableMemberTitle.add("Member Type");
        tableMemberTitle.add("Contact No");
        tableMemberTitle.add("Networth");
        tableMemberTitle.add("Priority");
        IncVal = new ArrayList();
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
   
      public void resetMemberTypeDetails() {
        setTxtMemNo("");
        setTxtMemName("");
        setTxtMemType("");
        setTxtContactNum("");
        setTxtMemNetworth("");
        setTxtMemPriority("");
      }
      private void populateMemberTableData(TermLoanSecurityMemberTO objMemberTypeTO) throws Exception {
        setTxtMemNo(CommonUtil.convertObjToStr(objMemberTypeTO.getMemberNo()));
        setTxtMemType(CommonUtil.convertObjToStr(objMemberTypeTO.getMemberType()));
        setTxtMemName(CommonUtil.convertObjToStr(objMemberTypeTO.getMemberName()));
        setTxtContactNum(CommonUtil.convertObjToStr(objMemberTypeTO.getContactNo()));
        setTxtMemNetworth(CommonUtil.convertObjToStr(objMemberTypeTO.getNetworth()));
        setTxtMemPriority(CommonUtil.convertObjToStr(objMemberTypeTO.getPriority()));
      }
      
      public String getStrACNumber(){
        return this.strACNumber;
      }
      
      public void addMemberTypeTable(int rowSelected, boolean updateMode){
        try{
            int rowSel=rowSelected;
            final TermLoanSecurityMemberTO objMemberTypeTO = new TermLoanSecurityMemberTO();
            if( memberTypeMap == null ){
                memberTypeMap = new LinkedHashMap();
            }            
            
            //if(getActionType()==ClientConstants.ACTIONTYPE_EDIT){
                if(isMemberTypeData()){
                    objMemberTypeTO.setStatusDt(curDate);
                    objMemberTypeTO.setStatusBy(TrueTransactMain.USER_ID);
                    objMemberTypeTO.setStatus(CommonConstants.STATUS_CREATED);
                }else{
                    objMemberTypeTO.setStatusDt(curDate);
                    objMemberTypeTO.setStatusBy(TrueTransactMain.USER_ID);
                    objMemberTypeTO.setStatus(CommonConstants.STATUS_MODIFIED);
                }
           // }
            
//            else{
//                objMemberTypeTO.setStatusDt(curDate);
//                objMemberTypeTO.setStatusBy(TrueTransactMain.USER_ID);
//                objMemberTypeTO.setStatus(CommonConstants.STATUS_CREATED);
//            }
            objMemberTypeTO.setAcctNum(getTxtAcctNo());
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
                incTabRow.add(CommonUtil.convertObjToStr(objMemberTypeTO.getPriority()));//KD-3515
                tblMemberTypeDetails.addRow(incTabRow);
            }
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
        setDocGenId(objTermLoanSecurityLandTO.getDocGenId());// Added by nithya on 08-06-2018 for gahan joint customer display issue
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
      
     public void populateMemberTypeDetails(String row){
        try{
            resetMemberTypeDetails();
            final TermLoanSecurityMemberTO objMemberTypeTO = (TermLoanSecurityMemberTO)memberTypeMap.get(row);
            populateMemberTableData(objMemberTypeTO);
            
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
           
                if(isCollateralTypeData() || (!obj.getStatus().equals(CommonConstants.STATUS_MODIFIED))){
                    objTermLoanSecurityLandTO.setStatusDt(curDate);
                    objTermLoanSecurityLandTO.setStatusBy(TrueTransactMain.USER_ID);
                    objTermLoanSecurityLandTO.setStatus(CommonConstants.STATUS_CREATED);
                }else{
                    objTermLoanSecurityLandTO.setStatusDt(curDate);
                    objTermLoanSecurityLandTO.setStatusBy(TrueTransactMain.USER_ID);
                    objTermLoanSecurityLandTO.setStatus(CommonConstants.STATUS_MODIFIED);
                }
            
            if(isRdoGahanYes())
                objTermLoanSecurityLandTO.setGahanYesNo("Y");
            else  if(isRdoGahanNo())
                objTermLoanSecurityLandTO.setGahanYesNo("N");
            else {
                objTermLoanSecurityLandTO.setGahanYesNo("");
            }
            objTermLoanSecurityLandTO.setAcctNum(getTxtAcctNo());
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

    public LinkedHashMap getAllowedTransactionDetailsTO() {
        return allowedTransactionDetailsTO;
    }

    public void setAllowedTransactionDetailsTO(LinkedHashMap allowedTransactionDetailsTO) {
        this.allowedTransactionDetailsTO = allowedTransactionDetailsTO;
    }

    public HashMap getServiceTax_Map() {  // Added by nithya on 30-12-2019 for KD-1131
        return serviceTax_Map;
    }

    public void setServiceTax_Map(HashMap serviceTax_Map) {
        this.serviceTax_Map = serviceTax_Map;
    }

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
    
     // Added by nithya on 07-03-2020 for KD-1379
    private LoansSecurityGoldStockTO getCustomerGoldStockSecurityTO() {
        System.out.println("inside getCustomerGoldStockSecurityTO getStrACNumber() :: " + getStrACNumber());
        LoansSecurityGoldStockTO objCustomerGoldStockSecurityTO = new LoansSecurityGoldStockTO();
        objCustomerGoldStockSecurityTO.setAcctNum(getTxtAcctNo());
        objCustomerGoldStockSecurityTO.setAsOnDt(curDate);
        objCustomerGoldStockSecurityTO.setPledgeAmount(CommonUtil.convertObjToDouble(getTxtValueOfGold()));
        objCustomerGoldStockSecurityTO.setRemarks(getTxtGoldRemarks());
        objCustomerGoldStockSecurityTO.setStatusDt(curDate);
        objCustomerGoldStockSecurityTO.setStatusBy(TrueTransactMain.USER_ID);
        objCustomerGoldStockSecurityTO.setStatus(CommonConstants.STATUS_CREATED);
        objCustomerGoldStockSecurityTO.setGoldStockId(getTxtGoldSecurityId());
        objCustomerGoldStockSecurityTO.setBranchCode(ProxyParameters.BRANCH_ID);
        objCustomerGoldStockSecurityTO.setProdId(CommonUtil.convertObjToStr(CommonUtil.convertObjToStr(getTxtAcctNo().substring(4,7))));
        objCustomerGoldStockSecurityTO.setProdType("AD");
        return objCustomerGoldStockSecurityTO;
    }
    // End

    public String getProdId() {
        return prodId;
    }

    public void setProdId(String prodId) {
        this.prodId = prodId;
    }
    
}
