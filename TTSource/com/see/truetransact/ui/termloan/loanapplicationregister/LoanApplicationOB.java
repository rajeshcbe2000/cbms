/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition.
 * 
 *
 * EmpTransferOB.java
 *
 * Created on june , 2010, 4:30 PM Swaroop
 */
package com.see.truetransact.ui.termloan.loanapplicationregister;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.ArrayList;
import java.util.List;
import java.util.Iterator;
import java.util.Date;

import com.see.truetransact.clientutil.ComboBoxModel;
import com.see.truetransact.clientproxy.ProxyFactory;
import com.see.truetransact.commonutil.CommonConstants;
import com.see.truetransact.transferobject.sysadmin.emptransfer.EmpTransferTO;
import com.see.truetransact.clientutil.ClientConstants;
import com.see.truetransact.commonutil.TTException;
import org.apache.log4j.Logger;
import com.see.truetransact.clientutil.ClientUtil;
import com.see.truetransact.clientexception.ClientParseException;
import com.see.truetransact.commonutil.CommonUtil;
import com.see.truetransact.ui.TrueTransactMain;
import com.see.truetransact.uicomponent.COptionPane;
import com.see.truetransact.ui.common.CommonRB;
import com.see.truetransact.commonutil.DateUtil;
import com.see.truetransact.clientproxy.ProxyParameters;
import com.see.truetransact.uicomponent.CObservable;
import com.see.truetransact.transferobject.termloan.loanapplicationregister.LoanApplicationTO;
import com.see.truetransact.ui.common.transaction.TransactionOB; //trans details
//security details
import com.see.truetransact.clientutil.EnhancedTableModel;
import com.see.truetransact.commonutil.interestcalc.Rounding;
import com.see.truetransact.transferobject.servicetax.servicetaxdetails.ServiceTaxDetailsTO;
import com.see.truetransact.transferobject.termloan.TermLoanSecurityMemberTO;
import com.see.truetransact.transferobject.termloan.TermLoanSecuritySalaryTO;
import com.see.truetransact.transferobject.termloan.TermLoanSecurityLandTO;
import com.see.truetransact.transferobject.termloan.TermLoanDepositTypeTO;
import java.util.Map;
import com.see.truetransact.transferobject.termloan.TermLoanLosTypeTO;
import com.see.truetransact.ui.termloan.TermLoanSecurityOB;
import com.see.truetransact.ui.termloan.TermLoanBorrowerOB;
import com.see.truetransact.ui.termloan.TermLoanRepaymentOB;
import com.see.truetransact.ui.termloan.TermLoanGuarantorOB;
import com.see.truetransact.ui.termloan.TermLoanDocumentDetailsOB;
import com.see.truetransact.ui.termloan.TermLoanInterestOB;
import com.see.truetransact.ui.termloan.TermLoanClassificationOB;
import com.see.truetransact.ui.termloan.TermLoanOtherDetailsOB;
import com.see.truetransact.ui.common.authorizedsignatory.AuthorizedSignatoryOB;
import com.see.truetransact.ui.termloan.loandisbursement.LoanDisbursementOB;
import com.see.truetransact.ui.termloan.TermLoanAdditionalSanctionOB;
import com.see.truetransact.ui.termloan.TermLoanCompanyOB;
import com.see.truetransact.ui.common.powerofattorney.PowerOfAttorneyOB;
import com.see.truetransact.ui.common.authorizedsignatory.AuthorizedSignatoryInstructionOB;
import com.see.truetransact.transferobject.termloan.TermLoanFacilityTO;
import com.see.truetransact.transferobject.termloan.TermLoanSecurityVehicleTO;
import com.see.truetransact.ui.common.servicetax.ServiceTaxCalculation;
import java.text.DecimalFormat;

//security end..
/**
 *
 * @author
 *
 */
public class LoanApplicationOB extends CObservable {
    private String txtRepayType = "";
    private String txtisTransaction = "";
    private double txttotSalary=0.0;
    private double txtVehicleMemSal=0.0;
    private String noOfInstallmnt = "";
    private String moratoriumPeriod = "";
    private String txtCustId = "";
    private String txtMemId = "";
    private String txtProdId = "";
    private String txtMemName = "";
    private String txtApplNo = "";
    private String cboPurposeCode = "";
    private Date tdtApplDt = null;
    private Date tdtFromDt = null;
    private Date tdtDueDt = null;
    private Date tdtApplInwrdDt = null;
    private String cboSchemName = "";
    private ComboBoxModel cbmSchemName;
    private Double txtLoanAmt = null;
    private String txtSuretyName = "";
    private String txaRemarks = "";
    private String cboRegstrStatus = "";
    private String txtStatusBy = "";
    private String txtStatus = "";
    private String CreatedDt = "";
    private HashMap newTransactionMap; //charge details
    private List chargelst = null; //charge details
    private HashMap operationMap; //charge details
    private HashMap _authorizeMap;
    private int result;
    private String oldSurvyNo = "";
    private boolean chkLoaneeSecurityonly = false;
    private ProxyFactory proxyAcct;
    private HashMap Acctmap;
    private double txtEligibleAmt = 0.0;
    private int cboRepayFreq_Repayment = 0;
    private double txtInstallAmount = 0.0;

    public double getTxtInstallAmount() {
        return txtInstallAmount;
    }

    public void setTxtInstallAmount(double txtInstallAmount) {
        this.txtInstallAmount = txtInstallAmount;
    }

    public double getTxtVehicleMemSal() {
        return txtVehicleMemSal;
    }

    public void setTxtVehicleMemSal(double txtVehicleMemSal) {
        this.txtVehicleMemSal = txtVehicleMemSal;
    }
   
    public int getCboRepayFreq_Repayment() {
        return cboRepayFreq_Repayment;
    }

    public void setCboRepayFreq_Repayment(int cboRepayFreq_Repayment) {
        this.cboRepayFreq_Repayment = cboRepayFreq_Repayment;
    }
  
    public double getTxtEligibleAmt() {
        return txtEligibleAmt;
    }

    public void setTxtEligibleAmt(double txtEligibleAmt) {
        this.txtEligibleAmt = txtEligibleAmt;
    }
    
    public double getTxttotSalary() {
        return txttotSalary;
    }

    public void setTxttotSalary(double txttotSalary) {
        this.txttotSalary = txttotSalary;
    }
   
    public String getTxtisTransaction() {
        return txtisTransaction;
    }

    public void setTxtisTransaction(String txtisTransaction) {
        this.txtisTransaction = txtisTransaction;
    }
         
    public String getMoratoriumPeriod() {
        return moratoriumPeriod;
    }

    public void setMoratoriumPeriod(String moratoriumPeriod) {
        this.moratoriumPeriod = moratoriumPeriod;
    }

    public String getNoOfInstallmnt() {
        return noOfInstallmnt;
    }

    public void setNoOfInstallmnt(String noOfInstallmnt) {
        this.noOfInstallmnt = noOfInstallmnt;
    }

    public String getTxtRepayType() {
        return txtRepayType;
    }

    public void setTxtRepayType(String txtRepayType) {
        this.txtRepayType = txtRepayType;
    }

    public boolean isChkLoaneeSecurityonly() {
        return chkLoaneeSecurityonly;
    }

    public void setChkLoaneeSecurityonly(boolean chkLoaneeSecurityonly) {
        this.chkLoaneeSecurityonly = chkLoaneeSecurityonly;
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
    private String lblStatus = ClientConstants.ACTION_STATUS[ClientConstants.ACTIONTYPE_CANCEL];
    //security details
    private String txtJewelleryDetails = "";
    private String txtGrossWeight = "";
    private String txtNetWeight = "";
    private String txtValueOfGold = "";
    private String txtGoldRemarks = "";
    private boolean memberTypeData = false;
    private boolean collateralTypeData = false;
    private EnhancedTableModel tblCollateralDetails;
    private String txtOwnerMemNo = "";
    private ComboBoxModel cbmSecurityCity;
    private ComboBoxModel cbmNature;
    private ComboBoxModel cbmRight;
    private ComboBoxModel cbmPledge;
    private ComboBoxModel cbmDocumentType;
    private ComboBoxModel CbmRepayFreq_Repayment;
    private ComboBoxModel CbmRepayType;
    private String txtMemNo = "";
    private String txtMembName = "";
    //    private String txtMemName = "";
    private String txtMemNetworth = "";
    private String txtMemPriority = "";
    private String txtContactNum = "";
    private String txtMemType = "";
    private String txtMemberTotalSalary="";
    //   private boolean memberTypeData = false;
    //   private EnhancedTableModel tblMemberTypeDetails;
    private TermLoanSecurityMemberTO objMemberTypeTO;
    private LinkedHashMap memberTypeMap;
    private boolean vehicleTypeData = false;
    private EnhancedTableModel tblMemberTypeDetails;
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
    private double txtCostOfVehicle=0;
    private double txtVehicleNetworth=0;
    final ArrayList tableVehicleTitle = new ArrayList();
    private LinkedHashMap deletedVehicleTypeMap;
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
    private int securitySlNo = 0;
    private TermLoanSecuritySalaryTO objTermLoanSecuritySalaryTO;
    // private String txtOwnerMemNo = "";
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
    private String cboDocumentType = "";
    private String txtAreaParticular = "";
    private String txtPledgeType = "";
    private boolean rdoGahanYes = false;
    private boolean rdoGahanNo = false;
    //  private boolean collateralTypeData = false;
    //   private EnhancedTableModel tblCollateralDetails;
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
    //   private HashMap newTransactionMap;
    private HashMap pledgeValMap = new HashMap();
    private boolean depositTypeData = false;
    private EnhancedTableModel tblDepositTypeDetails;
    private ComboBoxModel cbmProdType;
    private ComboBoxModel cbmPurposeCode;
    private ComboBoxModel cbmProdTypeSecurity;
    private ComboBoxModel cbmProdId;
    private ComboBoxModel cbmDepProdID;
    private boolean losTypeData = false;
    private EnhancedTableModel tblLosTypeDetails;
    private String txtDepNo = "";
    private String cboProductTypeSecurity = "";
    private String cboDepProdID = "";
    private String tdtDepDt = "";
    private String txtDepAmount = "";
    private String txtMaturityDt = "";
    private String txtMaturityValue = "";
    private String txtRateOfInterest = "";
    private String cboLosOtherInstitution;
    private String txtLosName = "";
    private String cboLosSecurityType = "";
    private String txtLosSecurityNo = "";
    private String txtLosAmount = "";
    private String tdtLosIssueDate = "";
    private String tdtLosMaturityDt = "";
    private String txtLosMaturityValue = "";
    private String txtLosRemarks = "";
    private LinkedHashMap salarySecurityAll = new LinkedHashMap();
    private LinkedHashMap salarySecurityDeleteAll = new LinkedHashMap();
    private final ArrayList salaryTitle = new ArrayList();
    private EnhancedTableModel tblSalarySecrityTable;
    Date curDate = null;
    private String strACNumber = "";
    private String docGenId = "";
    private LinkedHashMap depositTypeMap;
    private String borrowerNo = "";
    private LinkedHashMap deletedDepositTypeMap;
    final ArrayList tableTitleDepositList = new ArrayList();
    private LinkedHashMap losTypeMap;
    private ComboBoxModel cbmLosInstitution;
    private ComboBoxModel cbmLosSecurityType;
    private LinkedHashMap deletedLosTypeMap;
    final ArrayList tableTitleLosList = new ArrayList();
    private static TermLoanBorrowerOB termLoanBorrowerOB;
    private static TermLoanSecurityOB termLoanSecurityOB;
    private static TermLoanRepaymentOB termLoanRepaymentOB;
    private static TermLoanGuarantorOB termLoanGuarantorOB;
    private static TermLoanDocumentDetailsOB termLoanDocumentDetailsOB;
    private static TermLoanInterestOB termLoanInterestOB;
    private static TermLoanClassificationOB termLoanClassificationOB;
    private static TermLoanOtherDetailsOB termLoanOtherDetailsOB;
    private static AuthorizedSignatoryOB termLoanAuthorizedSignatoryOB;
    private static AuthorizedSignatoryInstructionOB termLoanAuthorizedSignatoryInstructionOB;
    private static TermLoanAdditionalSanctionOB termLoanAdditionalSanctionOB;//bala
    private static LoanDisbursementOB agriSubLimitOB;
    private static TermLoanCompanyOB termLoanCompanyOB;
    private static PowerOfAttorneyOB termLoanPoAOB;
    private HashMap facilityRecord;
    //security end..
    private final java.util.ResourceBundle objLoanApplicationRB = java.util.ResourceBundle.getBundle("com.see.truetransact.ui.termloan.loanapplicationregister.LoanApplicationRB", ProxyParameters.LANGUAGE);
    private LinkedHashMap transactionDetailsTO = null; //trans details
    private LinkedHashMap deletedTransactionDetailsTO = null; //trans details
    private LinkedHashMap allowedTransactionDetailsTO = null; //trans details
    private TransactionOB transactionOB; //trans details
    private HashMap authMap = new HashMap(); //trans details
    private Date currDt = null; //trans details
    private final String DELETED_TRANS_TOs = "DELETED_TRANS_TOs"; //trans details
    private final String NOT_DELETED_TRANS_TOs = "NOT_DELETED_TRANS_TOs"; //trans details
    private double txtAmount; //trans details
    private final static Logger log = Logger.getLogger(LoanApplicationOB.class);
    private final static ClientParseException parseException = ClientParseException.getInstance();
    private HashMap map;
    private ProxyFactory proxy;
    //for filling dropdown
    private HashMap lookupMap;
    private HashMap param;
    private HashMap keyValue;
    private ArrayList key;
    private ArrayList value;
    private int actionType;
    private LoanApplicationOB LoanApplicationOB;
    LoanApplicationRB LoanApplicationRB = new LoanApplicationRB();
    private static final CommonRB objCommonRB = new CommonRB();
    private LoanApplicationTO objLoanApplicationTO;
    //for loans
    private HashMap loanMap;
    private HashMap serviceTax_Map=null;
    private String lblServiceTaxval="";
    private ServiceTaxDetailsTO objservicetaxDetTo;

    /**
     * Creates a new instance of TDS MiantenanceOB
     */
    public LoanApplicationOB() {
        try {
            currDt = ClientUtil.getCurrentDate(); //trans details
            curDate = ClientUtil.getCurrentDate();//security details
            termLoanBorrowerOB = TermLoanBorrowerOB.getInstance();//security details
            proxy = ProxyFactory.createProxy();
            proxyAcct = ProxyFactory.createProxy();
            setOperationMap();
            map = new HashMap();
            map.put(CommonConstants.JNDI, "LoanApplicationJNDI");
            map.put(CommonConstants.HOME, "termloan.loanapplicationregister.LoanApplicationHome");
            map.put(CommonConstants.REMOTE, "termloan.loanapplicationregister.LoanApplication");
            fillDropdown();
            //security details
            setSalaryTitle();
            setMemberTableTile();
            setVehicleTableTile();
            setCollateralTableTile();
            setCollateralJointTableTile();
            setDepositTableTile();
            setLosTableTile();
            tblVehicleTypeDetails= new EnhancedTableModel(null, tableVehicleTitle);
            tblMemberTypeDetails = new EnhancedTableModel(null, tableMemberTitle);
            tblCollateralDetails = new EnhancedTableModel(null, tableCollateralTitle);
            tblJointCollateral = new EnhancedTableModel(null, tableCollateralJointTitle);
            tblDepositTypeDetails = new EnhancedTableModel(null, tableTitleDepositList);
            tblLosTypeDetails = new EnhancedTableModel(null, tableTitleLosList);
            tblSalarySecrityTable = new EnhancedTableModel(null, salaryTitle);
            //security end..
            notifyObservers();

        } catch (Exception e) {
            parseException.logException(e, true);
        }
    }

    //security details
    private void setMemberTableTile() throws Exception {

        tableMemberTitle.add("Member No");
        tableMemberTitle.add("Name");
        tableMemberTitle.add("Total Salary");
        tableMemberTitle.add("Contact No");
        tableMemberTitle.add("Networth");
        tableMemberTitle.add("Priority");
        IncVal = new ArrayList();
    }
     private void setVehicleTableTile() throws Exception {
         tableVehicleTitle.add("Member No");
         tableVehicleTitle.add("Name");
         tableVehicleTitle.add("Vehicle No");
      //   tableVehicleTitle.add("Vehicle Type");
         tableVehicleTitle.add("Vehicle Rc BookNo");
         tableVehicleTitle.add("Salary");
         tableVehicleTitle.add("NetWorth");
         IncVal = new ArrayList();
    }
    private void setCollateralTableTile() throws Exception {
        // tableCollateralTitle.add("Sl No");
        tableCollateralTitle.add("Member No");
        tableCollateralTitle.add("Name");
        tableCollateralTitle.add("Doc No");
        tableCollateralTitle.add("Pledge Amt");
        tableCollateralTitle.add("SurveyNo");
        tableCollateralTitle.add("TotalArea");
        IncVal = new ArrayList();
    }

    private void setCollateralJointTableTile() throws Exception {
        tableCollateralJointTitle.add("Cust Id");
        tableCollateralJointTitle.add("Name");
        tableCollateralJointTitle.add("Constitution");
        IncVal = new ArrayList();
    }

    private void setDepositTableTile() throws Exception {
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

    private void setLosTableTile() throws Exception {
        tableTitleLosList.add("Other Institution");
        tableTitleLosList.add("Name");
        tableTitleLosList.add("Security No");

        tableTitleLosList.add("Security Type");

        tableTitleLosList.add("Amount");
        IncVal = new ArrayList();


    }

    private void setOperationMap() throws Exception {
        Acctmap = new HashMap();
        Acctmap.put(CommonConstants.JNDI, "AccountClosingJNDI");
        Acctmap.put(CommonConstants.HOME, "operativeaccount.AccountClosingHome");
        Acctmap.put(CommonConstants.REMOTE, "operativeaccount.AccountClosing");
    }

    private void setSalaryTitle() throws Exception {
        try {
            salaryTitle.add(objLoanApplicationRB.getString("tblcolumnSalaryslno"));
            salaryTitle.add(objLoanApplicationRB.getString("tblcolumnSalaryCertificate"));
            salaryTitle.add(objLoanApplicationRB.getString("tblcolumnSalaryMemberNo"));
            salaryTitle.add(objLoanApplicationRB.getString("tblcolumnSalaryMemberName"));
            salaryTitle.add(objLoanApplicationRB.getString("tblcolumnSalaryContactNo"));
            salaryTitle.add(objLoanApplicationRB.getString("tblcolumnSalaryNetworth"));
        } catch (Exception e) {
            log.info("Exception in setShare Title:" + e);
            parseException.logException(e, true);
        }
    }

    public void resetGoldTypeDetails() {
        setTxtJewelleryDetails("");
        setTxtGoldRemarks("");
        setTxtGrossWeight("");
        setTxtValueOfGold("");
        setTxtNetWeight("");
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
            /*
             * HashMap param = new HashMap();
             * param.put(CommonConstants.MAP_NAME,"getAllBoardMem"); HashMap
             * where = new HashMap(); where.put("BRANCH_ID",
             * ProxyParameters.BRANCH_ID);
             * param.put(CommonConstants.PARAMFORQUERY, where); where = null;
             * keyValue = ClientUtil.populateLookupData(param);
             * fillData((HashMap)keyValue.get(CommonConstants.DATA));
             * cbmSchemName = new ComboBoxModel(key,value);
             */
            //product id,product description

            //To fill dropdown scheme name
            LinkedHashMap transactionMap = new LinkedHashMap();
            HashMap retrieve = new HashMap();
            ArrayList keyList = new ArrayList();
            keyList.add("");
            ArrayList valList = new ArrayList();
            valList.add("");
            List resultList = ClientUtil.executeQuery("InterMaintenance.getProductDataTLAD", transactionMap);
            for (int i = resultList.size() - 1, j = 0; i >= 0; --i, ++j) {
                // If the result contains atleast one record
                retrieve = (HashMap) resultList.get(j);
                keyList.add(retrieve.get("PROD_ID"));
                valList.add(retrieve.get("PROD_DESC"));
            }
            cbmSchemName = new ComboBoxModel(keyList, valList);
            transactionMap = null;
            resultList = null;
            keyList = null;
            valList = null;

            //security details
            ArrayList lookup_keys = new ArrayList();
           // lookup_keys.add("ACT_OP_MODE");
            lookup_keys.add("TERM_LOAN.PURPOSE_CODE");
        //    lookup_keys.add("SETTLEMENT_MODE");
        //    lookup_keys.add("LOAN.ACCOUNT_STATUS");
            lookup_keys.add("CATEGORY");
        //    lookup_keys.add("CONSTITUTION");
        //    lookup_keys.add("CORPORATE.ADDRESS_TYPE");
        //    lookup_keys.add("CUSTOMER.ADDRTYPE");
            lookup_keys.add("CUSTOMER.CITY");
       //     lookup_keys.add("CUSTOMER.STATE");
       //     lookup_keys.add("CUSTOMER.COUNTRY");
       //     lookup_keys.add("TERM_LOAN.BUSINESS_NATURE");
       //     lookup_keys.add("CUSTOMER.PRIMARYOCCUPATION");
       //     lookup_keys.add("TERM_LOAN.SANCTIONING_AUTHORITY");
      //      lookup_keys.add("TERM_LOAN.SANCTION_MODE");
      //      lookup_keys.add("FREQUENCY");
            lookup_keys.add("PRODUCTTYPE");
            lookup_keys.add("LOAN.FREQUENCY");
        //    lookup_keys.add("LOANPRODUCT.OPERATESLIKE");
        //    lookup_keys.add("TERMLOAN.INTERESTTYPE");
        //    lookup_keys.add("TERM_LOAN.SECURITY_CATEGORY");
       //     lookup_keys.add("TERM_LOAN.CHARGE_NATURE");
       //     lookup_keys.add("TERM_LOAN.MILLS_INDUSTRIAL_USERS");
            lookup_keys.add("TERM_LOAN.REPAYMENT_TYPE");
      //      lookup_keys.add("TERM_LOAN.REPAYMENT_FREQUENCY");
          //  lookup_keys.add("TERM_LOAN.COMMODITY_CODE");
         //   lookup_keys.add("TERM_LOAN.SECTOR_CODE");
            lookup_keys.add("TERM_LOAN.PURPOSE_CODE");
          //  lookup_keys.add("TERM_LOAN.INDUSTRY_CODE");
         //   lookup_keys.add("TERM_LOAN.RISK_NATURE");
         //   lookup_keys.add("TERM_LOAN.20_CODE");
        //    lookup_keys.add("TERM_LOAN.GOVT_SCHEME_CODE");
        //    lookup_keys.add("TERM_LOAN.GUARANTEE_COVER_CODE");
        //    lookup_keys.add("TERM_LOAN.HEALTH_CODE");
       //     lookup_keys.add("TERM_LOAN.DISTRICT_CODE");
       //     lookup_keys.add("TERM_LOAN.WEAKERSECTION_CODE");
       //     lookup_keys.add("TERM_LOAN.REFINANCING_INSTITUTION");
       //     lookup_keys.add("TERM_LOAN.ASSET_STATUS");
       //     lookup_keys.add("TERM_LOAN.FACILITY");
      //      lookup_keys.add("LOAN.INT_GET_FROM");
      //      lookup_keys.add("BOARDDIRECTORS");
      //      lookup_keys.add("TERM_LOAN.GUARANTEE_STATUS");
      //      lookup_keys.add("TERM_LOAN.CASE_TYPE");
            lookup_keys.add("SECURITY.NATURE");
            lookup_keys.add("SECURITY.PLEDGE");
            lookup_keys.add("SECURITY.RIGHT");
    //        lookup_keys.add("SECURITY_TYPE");
            lookup_keys.add("LOSSECURITYTYPE");
            lookup_keys.add("LOSINSTITUTION");
            lookup_keys.add("TERMLOAN.DOCUMENT_TYPE");
        //    lookup_keys.add("TERM_LOAN.REPAYMENT_TYPE");
         //   lookup_keys.add("TERM_LOAN.REPAYMENT_FREQUENCY");
            lookupMap.put(CommonConstants.PARAMFORQUERY, lookup_keys);

            keyValue = ClientUtil.populateLookupData(lookupMap);
            getKeyValue((HashMap) keyValue.get("CATEGORY"));
            termLoanBorrowerOB.setCbmCategory(new ComboBoxModel(key, value));
            getKeyValue((HashMap) keyValue.get("LOSINSTITUTION"));
            cbmLosInstitution = new ComboBoxModel(key, value);
            getKeyValue((HashMap) keyValue.get("LOSSECURITYTYPE"));
            cbmLosSecurityType = new ComboBoxModel(key, value);
            /*
             * getKeyValue((HashMap)keyValue.get("TERM_LOAN.CASE_TYPE"));
             * setCbmCaseStatus(new ComboBoxModel(key,value));
             *
             * getKeyValue((HashMap)keyValue.get("CONSTITUTION"));
             * termLoanBorrowerOB.setCbmConstitution(new
             * ComboBoxModel(key,value));
             *
             * getKeyValue((HashMap)keyValue.get("CUSTOMER.ADDRTYPE"));
             * termLoanCompanyOB.setCbmAddressType(new
             * ComboBoxModel(key,value));
             *
             * getKeyValue((HashMap)keyValue.get("CUSTOMER.CITY"));
             * termLoanCompanyOB.setCbmCity_CompDetail(new
             * ComboBoxModel(key,value));
             */
            getKeyValue((HashMap) keyValue.get("CUSTOMER.CITY"));
            setCbmSecurityCity(new ComboBoxModel(key, value));

            getKeyValue((HashMap) keyValue.get("SECURITY.NATURE"));
            setCbmNature(new ComboBoxModel(key, value));

            getKeyValue((HashMap) keyValue.get("SECURITY.RIGHT"));
            setCbmRight(new ComboBoxModel(key, value));

            getKeyValue((HashMap) keyValue.get("SECURITY.PLEDGE"));
            setCbmPledge(new ComboBoxModel(key, value));

            getKeyValue((HashMap) keyValue.get("TERMLOAN.DOCUMENT_TYPE"));//SECURITY_TYPE
            setCbmDocumentType(new ComboBoxModel(key, value));
            /*
             * getKeyValue((HashMap)keyValue.get("CUSTOMER.STATE"));
             * termLoanCompanyOB.setCbmState_CompDetail(new
             * ComboBoxModel(key,value));
             *
             * getKeyValue((HashMap)keyValue.get("CUSTOMER.COUNTRY"));
             * termLoanCompanyOB.setCbmCountry_CompDetail(new
             * ComboBoxModel(key,value));
             *
             * getKeyValue((HashMap)keyValue.get("TERM_LOAN.BUSINESS_NATURE"));
             * ArrayList tempKey = key; ArrayList tempVal = value;
             * getKeyValue((HashMap)keyValue.get("CUSTOMER.PRIMARYOCCUPATION"));
             * for (int i = key.size() - 1,j = 0;i >= 0;--i,++j){ if
             * (!key.get(j).equals("")){ tempKey.add(key.get(j)); } } for (int i
             * = value.size() - 1,j = 0;i >= 0;--i,++j){ if
             * (!value.get(j).equals("")){ tempVal.add(value.get(j)); } }
             * termLoanCompanyOB.setCbmNatureBusiness(new ComboBoxModel(tempKey,
             * tempVal));
             *
             * getKeyValue((HashMap)keyValue.get("LOAN.ACCOUNT_STATUS"));
             * setCbmAccStatus(new ComboBoxModel(key,value));
             *
             * getKeyValue((HashMap)keyValue.get("LOAN.INT_GET_FROM"));
             * cbmIntGetFrom = new ComboBoxModel(key,value);
             *
             * getKeyValue((HashMap)keyValue.get("CUSTOMER.CITY"));
             * cbmSanctioningAuthority = new ComboBoxModel(key,value);
             *
             * getKeyValue((HashMap)keyValue.get("TERM_LOAN.SANCTIONING_AUTHORITY"));
             * cbmSanctioningAuthority = new ComboBoxModel(key,value);
             *
             * getKeyValue((HashMap)keyValue.get("TERM_LOAN.SANCTION_MODE"));
             * cbmModeSanction = new ComboBoxModel(key,value);
             *
             * getKeyValue((HashMap)keyValue.get("LOAN.FREQUENCY"));
             * cbmRepayFreq = new ComboBoxModel(key,value);
             *
             * getKeyValue((HashMap)keyValue.get("LOAN.FREQUENCY"));
             * setCbmRepayFreq_LOAN(new ComboBoxModel(key,value));
             *
             * getKeyValue((HashMap)keyValue.get("TERM_LOAN.SANCTION_MODE"));
             * cbmModeSanction = new ComboBoxModel(key,value);
             *
             * getKeyValue((HashMap)keyValue.get("FREQUENCY")); for (int i =
             * key.size() - 1;i >= 0;--i){ if (key.get(i).equals("180") ||
             * key.get(i).equals("90") || key.get(i).equals("7") ||
             * key.get(i).equals("1")){ key.remove(i); } } for (int i =
             * value.size() - 1;i >= 0;--i){ if (value.get(i).equals("Half
             * Yearly") || value.get(i).equals("Quaterly") ||
             * value.get(i).equals("Weekly") || value.get(i).equals("Daily")){
             * value.remove(i); } } setCbmRepayFreq_ADVANCE(new
             * ComboBoxModel(key,value));
             *
             * getKeyValue((HashMap)keyValue.get("LOANPRODUCT.OPERATESLIKE"));
             * cbmTypeOfFacility = new ComboBoxModel(key,value); //
             * if(loanType.equals("OTHERS")) //
             * cbmTypeOfFacility.removeElement("Loans Against Deposits");
             * System.out.println("loanType###"+loanType);
             * getKeyValue((HashMap)keyValue.get("CONSTITUTION"));
             * termLoanGuarantorOB.setCbmConstitution_GD(new
             * ComboBoxModel(key,value));
             *
             * getKeyValue((HashMap)keyValue.get("CUSTOMER.CITY"));
             * termLoanGuarantorOB.setCbmCity_GD(new ComboBoxModel(key,value));
             *
             * getKeyValue((HashMap)keyValue.get("CUSTOMER.STATE"));
             * termLoanGuarantorOB.setCbmState_GD(new ComboBoxModel(key,value));
             *
             * getKeyValue((HashMap)keyValue.get("CUSTOMER.COUNTRY"));
             * termLoanGuarantorOB.setCbmCountry_GD(new
             * ComboBoxModel(key,value));
             */
            getKeyValue((HashMap) keyValue.get("PRODUCTTYPE"));
            //            termLoanGuarantorOB.setCbmProdType(new ComboBoxModel(key,value));
            cbmProdType = new ComboBoxModel(key, value);

            getKeyValue((HashMap) keyValue.get("TERM_LOAN.PURPOSE_CODE"));
            cbmPurposeCode = new ComboBoxModel(key, value);

            /*
             * getKeyValue((HashMap)keyValue.get("TERM_LOAN.GUARANTEE_STATUS"));
             * termLoanGuarantorOB.setCbmGuaranStatus(new
             * ComboBoxModel(key,value));
             *
             * getKeyValue((HashMap)keyValue.get("TERMLOAN.INTERESTTYPE"));
             * cbmInterestType = new ComboBoxModel(key,value);
             * getKeyValue((HashMap)keyValue.get("BOARDDIRECTORS"));
             * cbmRecommendedByType = new ComboBoxModel(key,value);
             */

            getKeyValue((HashMap) keyValue.get("LOAN.FREQUENCY"));
            setCbmRepayFreq_Repayment(new ComboBoxModel(key, value));

            getKeyValue((HashMap) keyValue.get("TERM_LOAN.REPAYMENT_TYPE"));
            setCbmRepayType(new ComboBoxModel(key, value));

            /*
             * getKeyValue((HashMap)keyValue.get("TERM_LOAN.COMMODITY_CODE"));
             * termLoanClassificationOB.setCbmCommodityCode(new
             * ComboBoxModel(key,value));
             *
             * getKeyValue((HashMap)keyValue.get("TERM_LOAN.SECTOR_CODE"));
             * termLoanClassificationOB.setCbmSectorCode1(new
             * ComboBoxModel(key,value));
             *
             * getKeyValue((HashMap)keyValue.get("TERM_LOAN.PURPOSE_CODE"));
             * termLoanClassificationOB.setCbmPurposeCode(new
             * ComboBoxModel(key,value));
             *
             * getKeyValue((HashMap)keyValue.get("TERM_LOAN.INDUSTRY_CODE"));
             * termLoanClassificationOB.setCbmIndusCode(new
             * ComboBoxModel(key,value));
             *
             * getKeyValue((HashMap)keyValue.get("TERM_LOAN.20_CODE"));
             * termLoanClassificationOB.setCbm20Code(new
             * ComboBoxModel(key,value));
             *
             * getKeyValue((HashMap)keyValue.get("TERM_LOAN.GOVT_SCHEME_CODE"));
             * termLoanClassificationOB.setCbmGovtSchemeCode(new
             * ComboBoxModel(key,value));
             *
             * getKeyValue((HashMap)keyValue.get("TERM_LOAN.GUARANTEE_COVER_CODE"));
             * termLoanClassificationOB.setCbmGuaranteeCoverCode(new
             * ComboBoxModel(key,value));
             *
             * getKeyValue((HashMap)keyValue.get("TERM_LOAN.HEALTH_CODE"));
             * termLoanClassificationOB.setCbmHealthCode(new
             * ComboBoxModel(key,value));
             *
             * getKeyValue((HashMap)keyValue.get("TERM_LOAN.DISTRICT_CODE"));
             * termLoanClassificationOB.setCbmDistrictCode(new
             * ComboBoxModel(key,value));
             *
             * getKeyValue((HashMap)keyValue.get("TERM_LOAN.WEAKERSECTION_CODE"));
             * termLoanClassificationOB.setCbmWeakerSectionCode(new
             * ComboBoxModel(key,value));
             *
             * getKeyValue((HashMap)keyValue.get("TERM_LOAN.REFINANCING_INSTITUTION"));
             * termLoanClassificationOB.setCbmRefinancingInsti(new
             * ComboBoxModel(key,value));
             *
             * getKeyValue((HashMap)keyValue.get("TERM_LOAN.ASSET_STATUS"));
             * termLoanClassificationOB.setCbmAssetCode(new
             * ComboBoxModel(key,value));
             *
             * getKeyValue((HashMap)keyValue.get("TERM_LOAN.FACILITY"));
             * termLoanClassificationOB.setCbmTypeFacility(new
             * ComboBoxModel(key,value));
             *
             * getKeyValue((HashMap)keyValue.get("ACT_OP_MODE"));
             * termLoanOtherDetailsOB.setCbmOpModeAI(new
             * ComboBoxModel(key,value));
             *
             * getKeyValue((HashMap)keyValue.get("SETTLEMENT_MODE"));
             * termLoanOtherDetailsOB.setCbmSettlementModeAI(new
             * ComboBoxModel(key,value));
             *
             * getKeyValue((HashMap)keyValue.get("FREQUENCY"));
             * termLoanOtherDetailsOB.setCbmStmtFreqAD(new
             * ComboBoxModel(key,value));
             */
            //            lookup_keys.add("PRODUCTTYPE");
            //            lookupMap.put(CommonConstants.PARAMFORQUERY, lookup_keys);
            //            keyValue = com.see.truetransact.clientutil.ClientUtil.populateLookupData(lookupMap);
            //            getKeyValue((HashMap)keyValue.get("PRODUCTTYPE"));
            //            cbmProdType= new ComboBoxModel(key,value);

            key = new ArrayList();
            value = new ArrayList();
            key.add("");
            value.add("");
            key.add("TD");
            value.add("Deposits");
            key.add("MDS");
            value.add("MDS");
            cbmProdTypeSecurity = new ComboBoxModel(key, value);

            cbmProdId = new ComboBoxModel();
            cbmDepProdID = new ComboBoxModel();
            //security end..

        } catch (NullPointerException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private void fillData(HashMap keyValue) throws Exception {
        key = (ArrayList) keyValue.get("KEY");
        value = (ArrayList) keyValue.get("VALUE");
    }

    /**
     * To get data for comboboxes
     */
    private HashMap populateData(HashMap obj) throws Exception {
        keyValue = proxy.executeQuery(obj, lookupMap);

        log.info("Got HashMap");
        return keyValue;

    }

    public void destroyObjects() {

        memberTypeMap = null;
        deletedMemberTypeMap = null;
        collateralMap = null;
        deletedCollateralMap = null;
        depositTypeMap = null;
        deletedDepositTypeMap = null;
        losTypeMap = null;

        pledgeValMap = null;
        //  objSMSSubscriptionTO=null;
        salarySecurityAll = null;
        salarySecurityDeleteAll = null;
        tblJointCollateral.setDataArrayList(null, tableCollateralJointTitle);
        tblSalarySecrityTable.setDataArrayList(null, salaryTitle);
        resetSecurityMemberTableValues();
        resetSecurityCollateralTableValues();
        resetDepositTypeTableValues();
        resetLosTypeTableValues();
        resetSecurityVehicleTableValues();
        serviceTax_Map=null;
//        actTransOB.resetAcctTransfer();//dontdelete
        objservicetaxDetTo=null;
    }

    public void ttNotifyObservers() {
        notifyObservers();
    }

    /**
     * To perform the necessary operation
     */
     public void doAction(String command) {
        try {

            if (actionType != ClientConstants.ACTIONTYPE_CANCEL || get_authorizeMap() != null) {
                doActionPerform(command);
            }
        } catch (Exception e) {
            setResult(ClientConstants.ACTIONTYPE_FAILED);
            parseException.logException(e, true);
        }
    }

    //charge details
             /*
     * Executes Query using the TO object
     */
    /*
     * public void execute(String command) { try { // System.out.println("GET
     * BOPRRR NO IN EDIT :="+setLoanData(command));
     *
     * HashMap term = new HashMap();//trans details
     * term.put(CommonConstants.MODULE, getModule());
     * term.put(CommonConstants.SCREEN, getScreen());
     * term.put(CommonConstants.USER_ID, ProxyParameters.USER_ID);
     * term.put(CommonConstants.BRANCH_ID, ProxyParameters.BRANCH_ID);
     * term.put("LoanApplicationTO", setLoanData(command)); /* //cheque details
     * if(chequeMap!=null && chequeMap.size()>0 ){
     * term.put("chequeBookDetails",chequeMap); } if(deletedChequeMap!=null &&
     * deletedChequeMap.size()>0 ){
     * term.put("deletedChequeBookDetails",deletedChequeMap); } //end..
     */
    /*
     * System.out.println("GET term IN EDIT :="+term); System.out.println("GET
     * map IN EDIT :="+map); //trans details if (transactionDetailsTO == null)
     * transactionDetailsTO = new LinkedHashMap(); if
     * (deletedTransactionDetailsTO != null) {
     * transactionDetailsTO.put(DELETED_TRANS_TOs,deletedTransactionDetailsTO);
     * deletedTransactionDetailsTO = null; }
     * transactionDetailsTO.put(NOT_DELETED_TRANS_TOs,allowedTransactionDetailsTO);
     * allowedTransactionDetailsTO = null;
     * term.put("TransactionTO",transactionDetailsTO);
     *
     * if(getAuthMap() != null && getAuthMap().size() > 0 ){ if( getAuthMap() !=
     * null){ term.put(CommonConstants.AUTHORIZEMAP, getAuthMap()); }
     * if(allowedTransactionDetailsTO!=null &&
     * allowedTransactionDetailsTO.size()>0){ if (transactionDetailsTO == null){
     * transactionDetailsTO = new LinkedHashMap(); }
     * transactionDetailsTO.put(NOT_DELETED_TRANS_TOs,allowedTransactionDetailsTO);
     * term.put("TransactionTO",transactionDetailsTO);
     * allowedTransactionDetailsTO = null; } authMap = null; } if(getChargelst()
     * != null && getChargelst().size()>0){ //charge details term.put("Charge
     * List Data", getChargelst()); } //charge end.. HashMap proxyReturnMap =
     * proxy.execute(term, map);
     * System.out.println("proxyy111>>>>>>>==="+proxyReturnMap);
     * setProxyReturnMap(proxyReturnMap); //
     * System.out.println(setProxyReturnMap(proxyReturnMap)); //end..
     *
     * // HashMap proxyReturnMap = proxy.execute(term, map); //
     * setProxyReturnMap(proxyReturnMap); setResult(getActionType());
     * System.out.println("ACTIONN TYPEEE==="+getActionType());
     *
     * } catch (Exception e) { setResult(ClientConstants.ACTIONTYPE_FAILED);
     * //parseException.logException(e,true); System.out.println("Error in
     * execute():"+e); }
}
     */
    // charge end.. 
    /**
     * To perform the necessary action
     */
    private void doActionPerform(String command) throws Exception {
        final LoanApplicationTO objLoanApplicationTO = new LoanApplicationTO();
        HashMap data = new HashMap();
        HashMap term = new HashMap();//trans details //security details
        //security details
        insertFacilityDetails();
        HashMap objTermLoanFacilityTOMap = setTermLoanFacilitySingleRecord();
        data.put("TermLoanFacilityTO", objTermLoanFacilityTOMap);
        data.put("DepositSecurityTableDetails", depositTypeMap);
        data.put("LosSecurityTableDetails", losTypeMap);
        if (deletedDepositTypeMap != null && deletedDepositTypeMap.size() > 0) {
            data.put("deletedDepositTypeData", deletedDepositTypeMap);
        }
        if (deletedLosTypeMap != null && deletedLosTypeMap.size() > 0) {
            data.put("deletedLosTypeData", deletedLosTypeMap);
        }

        if (salarySecurityAll != null && salarySecurityAll.size() > 0) {
            data.put("TermLoanSecuritySalaryTOData", salarySecurityAll);
        }
        if (salarySecurityDeleteAll != null && salarySecurityDeleteAll.size() > 0) {
            data.put("TermLoanSecuritySalaryTODeletedData", salarySecurityDeleteAll);
        }
        //        }
        if (memberTypeMap != null && memberTypeMap.size() > 0) {
            data.put("MemberTableDetails", memberTypeMap);
        }
        if (deletedMemberTypeMap != null && deletedMemberTypeMap.size() > 0) {
            data.put("deletedMemberTypeData", deletedMemberTypeMap);
        }
        if (collateralMap != null && collateralMap.size() > 0) {
            data.put("CollateralTableDetails", collateralMap);
        }
        if (deletedCollateralMap != null && deletedCollateralMap.size() > 0) {
            data.put("deletedCollateralTypeData", deletedCollateralMap);
        }
        //added by rishad 24/03/2016 for adding vehicle security RBI
        if (vehicleTypeMap != null && vehicleTypeMap.size() > 0) {
            data.put("VehicleTableDetails", vehicleTypeMap);
        }
        if (deletedVehicleTypeMap != null && deletedVehicleTypeMap.size() > 0) {
            data.put("deletedVehicleTypeData", deletedVehicleTypeMap);
        }
        //security end..
        data.put("COMMAND", getCommand());
        
        if (get_authorizeMap() == null) {
            data.put("LoanApplication", setLoanData());
        } else {
            data.put(CommonConstants.AUTHORIZEMAP, get_authorizeMap());
        }

        //trans details
        if (!command.equals(CommonConstants.AUTHORIZESTATUS)) {
            if (transactionDetailsTO == null) {
                transactionDetailsTO = new LinkedHashMap();
            }
            if (deletedTransactionDetailsTO != null) {
                transactionDetailsTO.put(DELETED_TRANS_TOs, deletedTransactionDetailsTO);
                deletedTransactionDetailsTO = null;
            }
            transactionDetailsTO.put(NOT_DELETED_TRANS_TOs, allowedTransactionDetailsTO);
            allowedTransactionDetailsTO = null;
            data.put("TransactionTO", transactionDetailsTO);
        }
        if (getAuthMap() != null && getAuthMap().size() > 0) {
            if (getAuthMap() != null) {
                data.put(CommonConstants.AUTHORIZEMAP, getAuthMap());
            }
            if (allowedTransactionDetailsTO != null && allowedTransactionDetailsTO.size() > 0) {
                if (transactionDetailsTO == null) {
                    transactionDetailsTO = new LinkedHashMap();
                }
                transactionDetailsTO.put(NOT_DELETED_TRANS_TOs, allowedTransactionDetailsTO);
                data.put("TransactionTO", transactionDetailsTO);
                allowedTransactionDetailsTO = null;
            }
            authMap = null;
        }
        if (getNewTransactionMap() != null) {
            data.put("Transaction Details Data", getNewTransactionMap());
        }
        if (getChargelst() != null && getChargelst().size() > 0) { //charge details
            data.put("Charge List Data", getChargelst());
        }
        if(getLblServiceTaxval()!=null && CommonUtil.convertObjToDouble(getLblServiceTaxval())>0 && 
            getServiceTax_Map()!=null && getServiceTax_Map().size()>0){
            data.put("serviceTaxDetails",getServiceTax_Map());
            data.put("serviceTaxDetailsTO",setServiceTaxDetails());
        }
//              HashMap proxyReturnMap = proxy.execute(data, map);
//              System.out.println("proxyy111>>>>>>>==="+proxyReturnMap);
//            setProxyReturnMap(proxyReturnMap);
//             System.out.println(setProxyReturnMap(proxyReturnMap));
        //end..

        //added by vivek
        //   if(getChargelst() != null && getChargelst().size()>0){
        //     data.put("Charge List Data", getChargelst());
        // }
        data.put(CommonConstants.SCREEN,getScreen());
        HashMap proxyResultMap = proxy.execute(data, map);
        setProxyReturnMap(proxyResultMap);
        if (proxyResultMap != null && getCommand() != null && getCommand().equalsIgnoreCase("INSERT")) {
            ClientUtil.showMessageWindow("Application No. : " + CommonUtil.convertObjToStr(proxyResultMap.get("APPLICATION_NO")));
        }
        if (proxyResultMap != null && getCommand() != null && getCommand().equalsIgnoreCase("UPDATE")) {
            ClientUtil.showMessageWindow("Updated Application No. : " + CommonUtil.convertObjToStr(proxyResultMap.get("APPLICATION_NO")));
        }
        if (proxyResultMap != null && getCommand() != null && getCommand().equalsIgnoreCase("DELETE")) {
            ClientUtil.showMessageWindow("Deleted Application No. : " + CommonUtil.convertObjToStr(proxyResultMap.get("APPLICATION_NO")));
        }
        setResult(getActionType());
        setResult(actionType);
        if (getResult() != 4) {
            chargelst = null;
        }
        //   }
    }
    //security details

    private void insertFacilityDetails() {
        facilityRecord = new HashMap();
        /*
         * 1 facilityRecord.put(PROD_ID, null); facilityRecord.put(ACCT_STATUS,
         * null); facilityRecord.put(SANCTION_NO, null);
         * facilityRecord.put(SLNO, null); facilityRecord.put(INTEREST_TYPE,
         * null); facilityRecord.put(NOTE_DATE, null);
         * facilityRecord.put(NOTE_EXP_DATE, null); facilityRecord.put(AOD_DATE,
         * null); facilityRecord.put(PURPOSE_DESC, null);
         * facilityRecord.put(GROUP_DESC, null);
         * facilityRecord.put(CONTACT_PERSON, null);
         * facilityRecord.put(SALARY_RECOVERY, null);
         * facilityRecord.put(LOCK_STATUS, null);
         * facilityRecord.put(CONTACT_PHONE, null); facilityRecord.put(REMARKS,
         * null); facilityRecord.put(ACCT_NAME, null);
         * facilityRecord.put(INT_GET_FROM, null); facilityRecord.put(COMMAND,
         * INSERT); facilityRecord.put(AUTHORIZED, NO);
         * facilityRecord.put(AUTHORIZE_BY_1, null);
         * facilityRecord.put(AUTHORIZE_BY_2, null);
         * facilityRecord.put(AUTHORIZE_DT_1, null);
         * facilityRecord.put(AUTHORIZE_DT_2, null);
         * facilityRecord.put(AUTHORIZE_STATUS_1, null);
         * facilityRecord.put(AUTHORIZE_STATUS_2, null);
         * facilityRecord.put(ACCT_OPEN_DT, null);
         * facilityRecord.put(RECOMMANDED_BY, null); // txtSubsidyAmt
         * facilityRecord.put(SUBSIDY_AMT, null); facilityRecord.put(SUBSIDY_DT,
         * null); facilityRecord.put(SUBSIDY_ACHEAD, null);
         * facilityRecord.put(SUBSIDY_ADJUSTED_AMT, null);
         *
         * facilityRecord.put(REBATE_AMT, null); facilityRecord.put(REBATE_DT,
         * null); // if(isRdoRebateInterest_Yes())
         * facilityRecord.put(REBATE_ALLOWED, null); // else
         * if(isRdoRebateInterest_No()) // facilityRecord.put(REBATE_ALLOWED,
         * null); // else // facilityRecord.put(REBATE_ALLOWED, null);
         *
         */

        facilityRecord.put("ACCT_NUM", getStrACNumber());

        //  if (getRdoSecurityDetails_Unsec() == true){
        facilityRecord.put("SECURITY_DETAILS", null);
        //  }else if (getRdoSecurityDetails_Partly() == true){
        //    facilityRecord.put(SECURITY_DETAILS, null);
        //  }else if (getRdoSecurityDetails_Fully() == true){
        //    facilityRecord.put(SECURITY_DETAILS, null);
        //  }else{
        //    facilityRecord.put(SECURITY_DETAILS, null);
        //    }
        //    if(isRdoDP_YES())
        //       facilityRecord.put(DRAWING_POWER, "Y");
        //  else if(isRdoDP__NO())
        facilityRecord.put("DRAWING_POWER", null);
        //   else
        //   facilityRecord.put(DRAWING_POWER, " ");
        //   if ((getChkStockInspect() == true) && (getChkInsurance() == true) && (getChkGurantor() == true)){
        facilityRecord.put("SECURITY_TYPE", null);
        //  }else if ((getChkStockInspect() == true) && (getChkInsurance() == true)){
        //      facilityRecord.put(SECURITY_TYPE, STOCK_INSPECT_INSURANCE);
        //    }else if ((getChkStockInspect() == true) && (getChkGurantor() == true)){
        //       facilityRecord.put(SECURITY_TYPE, STOCK_INSPECT_GUARANTAR);
        //   }else if ((getChkInsurance() == true) && (getChkGurantor() == true)){
        //      facilityRecord.put(SECURITY_TYPE, INSURANCE_GUARANTAR);
        //   }else if (getChkStockInspect() == true){
        //       facilityRecord.put(SECURITY_TYPE, STOCK_INSPECT);
        //   }else if (getChkInsurance() == true){
        //       facilityRecord.put(SECURITY_TYPE, INSURANCE);
        //    }else if (getChkGurantor() == true){
        //       facilityRecord.put(SECURITY_TYPE, GUARANTAR);
        //   }else{
        //        facilityRecord.put(SECURITY_TYPE, "");
        //  }
        //    if(getChkAcctTransfer()==true)
        facilityRecord.put("ACCTTRANSFER", null);
        //   else
        //       facilityRecord.put(ACCTTRANSFER, "");

        //     if (getRdoAccType_New() == true){
        //         facilityRecord.put(ACC_TYPE, NEW);
        //  }else if (getRdoAccType_Transfered() == true){
        facilityRecord.put("ACC_TYPE", null);
        //   }else{
        //      facilityRecord.put(ACC_TYPE, "");
        //   }

        //   if (getRdoAccLimit_Main() == true){
        facilityRecord.put("ACC_LIMIT", null);
        //    }else if (getRdoAccLimit_Submit() == true){
        //       facilityRecord.put(ACC_LIMIT, SUBMIT);
        //    }else{
        //      facilityRecord.put(ACC_LIMIT, "");
        //  }

        //     if (getRdoRiskWeight_Yes() == true){
        //        facilityRecord.put(RISK_WEIGHT, YES);
        //     }else if (getRdoRiskWeight_No() == true){
        //       facilityRecord.put(RISK_WEIGHT, NO);
        //    }else{
        facilityRecord.put("RISK_WEIGHT", null);
        //   }

        //   if (getRdoMultiDisburseAllow_Yes() == true){
        facilityRecord.put("MULTI_DISBURSE", null);
        //    }else if (getRdoMultiDisburseAllow_No() == true){
        //        facilityRecord.put(MULTI_DISBURSE, NO);
        //   }else{
        //        facilityRecord.put(MULTI_DISBURSE, " ");
        //  }

        //   if (getRdoInterest_Simple() == true){
        facilityRecord.put("INTEREST", null);
        //     }else if (getRdoInterest_Compound() == true){
        //      facilityRecord.put(INTEREST, COMPOUND);
        //   }else{
        //     facilityRecord.put(INTEREST, " ");
        //    }
        //    if(isChkAuthorizedSignatory())
        //        facilityRecord.put(AUTHSIGNATORY,AUTHSIGNATORY);
        //    else
        facilityRecord.put("AUTHSIGNATORY", null);

        //   if(isChkPOFAttorney())
        facilityRecord.put("POFATTORNEY", null);
        //   else
        //   facilityRecord.put(POFATTORNEY," ");

        //     if(isChkDocDetails())
        facilityRecord.put("DOCDETAILS", null);
        //  else
        //     facilityRecord.put(DOCDETAILS," ");


        //    if(rdoSubsidy_Yes==true)
        facilityRecord.put("SUBSIDY", null);
        //  else if(rdoSubsidy_No==true)
        //      facilityRecord.put(SUBSIDY,"N");
        //   else
        //    facilityRecord.put(SUBSIDY," ");

        //    if(rdoSubsidy_Yes==true)
        //   facilityRecord.put(SUBSIDY,"Y");
        //    else if(rdoSubsidy_No==true)
        //     facilityRecord.put(SUBSIDY,"N");
        //   else
        //   facilityRecord.put(SUBSIDY," ");


        //      if (getStrACNumber().length()>0){
        // If the key already exist in the Linked Hash Map then it status
        // will be changed to UPDATE
        //       if(getBEHAVES_LIKE().equals("OD"))
        facilityRecord.put("AVAILABLE_BALANCE", null);
        //   else {
        //    if(getClearBalance()<0)
        //     facilityRecord.put(AVAILABLE_BALANCE, String.valueOf(getAvailableBalance()));
        //  else if (isUpdateAvailableBalance())
        //     facilityRecord.put(AVAILABLE_BALANCE, getTxtLimit_SD());

        //   }

        //  }else{
        // At the time of creating new account set the available balance, total available balance
        // shadow debit, shadow credit, clear balance, unclear balance
    /*
         * 2 facilityRecord.put(SHADOW_DEBIT, null);
         * facilityRecord.put(SHADOW_CREDIT, null);
         * facilityRecord.put(AVAILABLE_BALANCE, null);
         * facilityRecord.put(TOTAL_AVAILABLE_BALANCE, null);
         * facilityRecord.put(CLEAR_BALANCE, null);
         * facilityRecord.put(UNCLEAR_BALANCE, null);
         */
        facilityRecord.put("JEWELLERY_DETAILS", getTxtJewelleryDetails());
        facilityRecord.put("GROSS_WEIGHT", getTxtGrossWeight());
        facilityRecord.put("NET_WEIGHT", getTxtNetWeight());
        facilityRecord.put("VALUE_OF_GOLD", getTxtValueOfGold());
        facilityRecord.put("GOLD_REMARKS", getTxtGoldRemarks());
        //  }
    }

    private HashMap setTermLoanFacilitySingleRecord() {
        HashMap facilityMap = new HashMap();
        try {
            TermLoanFacilityTO objTermLoanFacilityTO;
            objTermLoanFacilityTO = new TermLoanFacilityTO();
            objTermLoanFacilityTO.setAcctNum(CommonUtil.convertObjToStr(facilityRecord.get("ACCT_NUM")));
            objTermLoanFacilityTO.setTxtJewelleryDetails(CommonUtil.convertObjToStr(facilityRecord.get("JEWELLERY_DETAILS")));
            objTermLoanFacilityTO.setTxtGrossWeight(CommonUtil.convertObjToStr(facilityRecord.get("GROSS_WEIGHT")));
            objTermLoanFacilityTO.setTxtNetWeight(CommonUtil.convertObjToStr(facilityRecord.get("NET_WEIGHT")));
            objTermLoanFacilityTO.setTxtValueOfGold(CommonUtil.convertObjToDouble(facilityRecord.get("VALUE_OF_GOLD")));
            objTermLoanFacilityTO.setTxtGoldRemarks(CommonUtil.convertObjToStr(facilityRecord.get("GOLD_REMARKS")));
            if (getCommand().equals(CommonConstants.TOSTATUS_INSERT)) {
                objTermLoanFacilityTO.setCreateDt(currDt);
                objTermLoanFacilityTO.setCreatedBy(TrueTransactMain.USER_ID);
                objTermLoanFacilityTO.setStatus(CommonConstants.STATUS_CREATED);
            } else if (getCommand().equals(CommonConstants.TOSTATUS_UPDATE)) {
                objTermLoanFacilityTO.setStatus(CommonConstants.STATUS_MODIFIED);
            } else if (getCommand().equals(CommonConstants.TOSTATUS_DELETE)) {
                objTermLoanFacilityTO.setStatus(CommonConstants.STATUS_DELETED);
            }

            objTermLoanFacilityTO.setStatusBy(TrueTransactMain.USER_ID);
            objTermLoanFacilityTO.setStatusDt(curDate);
            if (objTermLoanFacilityTO.getAcctNum().equals(getStrACNumber())) {
                facilityMap.put(String.valueOf(objTermLoanFacilityTO.getSlNo()), objTermLoanFacilityTO);
                //    slNoForSanction = objTermLoanFacilityTO.getSlNo().doubleValue();
            }

        } catch (Exception e) {
            e.printStackTrace();
            log.info("Exception caught In setTermLoanFacility: " + e);
            parseException.logException(e, true);
        }
        return facilityMap;
    }
    //security end..

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
        // System.out.println("command : " + command);
        return command;
    }

    private String getAction() {
        String action = null;
        // System.out.println("actionType : " + actionType);
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
        // System.out.println("command : " + command);
        return action;
    }

    public HashMap getAccountClosingCharges(String prodid, String AcctNum) {
        HashMap returnMap = new HashMap();
        param = new HashMap();
        try {
            param.put("PROD_ID", prodid);
            param.put("Acct_NUM", AcctNum);
            param.put("PREMATURE_ONEMONTH_INT", "PREMATURE_ONEMONTH_INT");
            HashMap dataMap = populateData1(param);
            returnMap.put("dataMap", dataMap);
            HashMap chargeMap = (HashMap) dataMap.get("AccountDetailsTO");
            HashMap intMap = new HashMap();
            returnMap.put("chargeMap", chargeMap);
        } catch (Exception e) {
            e.printStackTrace();
            parseException.logException(e, true);
        }
        return returnMap;
    }

    private HashMap populateData1(HashMap obj) throws Exception {
        obj.put(CommonConstants.MAP_WHERE, obj.get("Acct_NUM"));
        if (obj.get("PROD_ID") != null) {
            obj.put("PROD_ID", obj.get("PROD_ID"));
        }
        obj.put("PROD_TYPE", "TermLoan");
        obj.put("CURR_DATE", ClientUtil.getCurrentDateProperFormat());
        HashMap where = proxyAcct.executeQuery(obj, Acctmap);
        return where;
    }

    /**
     * To retrieve a particular customer's accountclosing record
     */
    public void getData(HashMap whereMap) {
        HashMap mapData = null;
        try {
            mapData = (HashMap) proxy.executeQuery(whereMap, map);
            setLoanMap(mapData);
            objLoanApplicationTO = (LoanApplicationTO) ((List) mapData.get("LoanApplicationTO")).get(0);
            populateData(objLoanApplicationTO);
            //trans details
            if (mapData.containsKey("TRANSACTION_LIST")) {
                List list = (List) mapData.get("TRANSACTION_LIST");
                if (!list.isEmpty()) {
                    transactionOB.setDetails(list);
                }
            }
            //end..
            //security details
            if (mapData.containsKey("SECURITY_DETAILS")) {
                HashMap data1 = (HashMap) mapData.get("SECURITY_DETAILS");
                if (data1.containsKey("memberListTO")) {
                    memberTypeMap = (LinkedHashMap) data1.get("memberListTO");
                    ArrayList addList = new ArrayList(memberTypeMap.keySet());
                    for (int i = 0; i < addList.size(); i++) {
                        TermLoanSecurityMemberTO objMemberTypeTO = (TermLoanSecurityMemberTO) memberTypeMap.get(addList.get(i));
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
                if (data1.containsKey("CollateralListTO")) {
                    collateralMap = (LinkedHashMap) data1.get("CollateralListTO");
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
                        //comm by jiby
                        //collateralMap.put(addList.get(i),objTermLoanSecurityLandTO);
                        //added by jiby
                        collateralMap.put(CommonUtil.convertObjToStr(objTermLoanSecurityLandTO.getMemberNo()) + "_" + (i + 1), objTermLoanSecurityLandTO);
                        addPledgeAmountMap(objTermLoanSecurityLandTO.getDocumentNo(), CommonUtil.convertObjToDouble(objTermLoanSecurityLandTO.getPledgeAmount()).doubleValue());
                        //System.out.println("gdfgdfg");
                    }
                }
                if (data1.containsKey("TermLoanSecuritySalaryTO")) {
                    //                objTermLoanSecuritySalaryTO = (TermLoanSecuritySalaryTO) ((List) mapData.get("TermLoanSecuritySalaryTO")).get(0);
                    populateTermLoanSecuritySalaryTOData(data1);
                }
                 if (data1.containsKey("TermLoanSecurityvehicleListTO")) {
                    //                objTermLoanSecuritySalaryTO = (TermLoanSecuritySalaryTO) ((List) mapData.get("TermLoanSecuritySalaryTO")).get(0);
                    vehicleTypeMap = (LinkedHashMap) data1.get("TermLoanSecurityvehicleListTO");
                    ArrayList addList = new ArrayList(vehicleTypeMap.keySet());
                     for (int i = 0; i < addList.size(); i++) {
                         TermLoanSecurityVehicleTO objVehicleTypeTO = (TermLoanSecurityVehicleTO) vehicleTypeMap.get(addList.get(i));
                         objVehicleTypeTO.setStatus(CommonConstants.STATUS_MODIFIED);
                         ArrayList incTabRow = new ArrayList();
                         incTabRow.add(CommonUtil.convertObjToStr(objVehicleTypeTO.getMemberNo()));
                         incTabRow.add(CommonUtil.convertObjToStr(objVehicleTypeTO.getMemberName()));
                         incTabRow.add(CommonUtil.convertObjToStr(objVehicleTypeTO.getVehicleNo()));
                         //   incTabRow.add(CommonUtil.convertObjToStr(objVehicleTypeTO.getVehicleType()));
                         incTabRow.add(CommonUtil.convertObjToStr(objVehicleTypeTO.getVehicleRcBookNo()));
                         incTabRow.add(CommonUtil.convertObjToStr(objVehicleTypeTO.getMemSalary()));
                         incTabRow.add(CommonUtil.convertObjToStr(objVehicleTypeTO.getNetWorth()));
                         tblVehicleTypeDetails.addRow(incTabRow);
                    }
                }


                if (data1.containsKey("DeositTypeDetails")) {
                    depositTypeMap = (LinkedHashMap) data1.get("DeositTypeDetails");
                    ArrayList addList = new ArrayList(depositTypeMap.keySet());
                    for (int i = 0; i < addList.size(); i++) {
                        TermLoanDepositTypeTO objTermLoanDepositTypeTO = (TermLoanDepositTypeTO) depositTypeMap.get(addList.get(i));
                        objTermLoanDepositTypeTO.setStatus(CommonConstants.STATUS_MODIFIED);
                        ArrayList incTabRow = new ArrayList();
                        incTabRow.add(CommonUtil.convertObjToStr(objTermLoanDepositTypeTO.getProdType()));
                        incTabRow.add(CommonUtil.convertObjToStr(objTermLoanDepositTypeTO.getTxtDepNo()));
                        incTabRow.add(CommonUtil.convertObjToStr(objTermLoanDepositTypeTO.getTxtDepAmount()));
                        incTabRow.add(CommonUtil.convertObjToStr(objTermLoanDepositTypeTO.getTxtMaturityValue()));
                        tblDepositTypeDetails.addRow(incTabRow);
                        depositTypeMap.put(addList.get(i), objTermLoanDepositTypeTO);
                        //                    addPledgeAmountMap(objTermLoanSecurityLandTO.getDocumentNo(),CommonUtil.convertObjToDouble(objTermLoanSecurityLandTO.getPledgeAmount()).doubleValue());

                    }
                }
                if (data1.containsKey("LosTypeDetails")) {
                    losTypeMap = (LinkedHashMap) data1.get("LosTypeDetails");
                    ArrayList addList = new ArrayList(losTypeMap.keySet());
                    for (int i = 0; i < addList.size(); i++) {
                        TermLoanLosTypeTO objTermLoanLosTypeTO = (TermLoanLosTypeTO) losTypeMap.get(addList.get(i));
                        objTermLoanLosTypeTO.setStatus(CommonConstants.STATUS_MODIFIED);
                        ArrayList incTabRow = new ArrayList();
                        incTabRow.add(CommonUtil.convertObjToStr(objTermLoanLosTypeTO.getLosInstitution()));
                        incTabRow.add(CommonUtil.convertObjToStr(objTermLoanLosTypeTO.getLosName()));
                        incTabRow.add(CommonUtil.convertObjToStr(objTermLoanLosTypeTO.getLosSecurityNo()));
                        incTabRow.add(CommonUtil.convertObjToStr(objTermLoanLosTypeTO.getLosSecurityType()));
                        incTabRow.add(CommonUtil.convertObjToStr(objTermLoanLosTypeTO.getLosAmount()));
                        tblLosTypeDetails.addRow(incTabRow);
                        losTypeMap.put(addList.get(i), objTermLoanLosTypeTO);

                    }
                }
                if (data1.containsKey("TermLoanFacilityTO")) {
                    List aList = (List) data1.get("TermLoanFacilityTO");
                    TermLoanFacilityTO objto = (TermLoanFacilityTO) aList.get(0);
                    setTxtJewelleryDetails(objto.getTxtJewelleryDetails());
                    setTxtGrossWeight(objto.getTxtGrossWeight());
                    setTxtNetWeight(objto.getTxtNetWeight());
                    setTxtValueOfGold(CommonUtil.convertObjToStr(objto.getTxtValueOfGold()));
                    setTxtGoldRemarks(objto.getTxtGoldRemarks());
                }

            }
            setChanged();
            //security end..
            ttNotifyObservers();
        } catch (Exception e) {
          e.printStackTrace();
            parseException.logException(e, true);
        }
    }

    //security details
    public void addPledgeAmountMap(String docNumber, double pledgeAmt) {
        try {
            if (pledgeValMap == null) {
                pledgeValMap = new HashMap();
            }
            HashMap tempMap = new HashMap();
            tempMap.put("DOC_NO", docNumber);
            tempMap.put("PLEDGE_AMT", new Double(pledgeAmt));
            pledgeValMap.put(docNumber, tempMap);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    //security end..

    /**
     * To populate data into the screen
     */
    public LoanApplicationTO setLoanData() {

        final LoanApplicationTO objLoanApplicationTO = new LoanApplicationTO();
        try {

            //objLoanApplicationTO.setBranCode(TrueTransactMain.BRANCH_ID);
            objLoanApplicationTO.setStatusBy(ProxyParameters.USER_ID);
            objLoanApplicationTO.setCommand(getCommand());
            objLoanApplicationTO.setStatus(getAction());
            objLoanApplicationTO.setStatusBy(TrueTransactMain.USER_ID);
            objLoanApplicationTO.setCustId(getTxtCustId());
            objLoanApplicationTO.setProdId(getTxtProdId());
            objLoanApplicationTO.setRePayment(getTxtRepayType());
            objLoanApplicationTO.setNoOfInstalmnt(CommonUtil.convertObjToInt(getNoOfInstallmnt()));
            objLoanApplicationTO.setMoratoriumPeriod(CommonUtil.convertObjToInt(getMoratoriumPeriod()));
            objLoanApplicationTO.setMemId(getTxtMemId());
            objLoanApplicationTO.setMemName(getTxtMemName());
            objLoanApplicationTO.setApplNo(getTxtApplNo());
            objLoanApplicationTO.setApplDt(getTdtApplDt());
            objLoanApplicationTO.setApplInwrdDt(getTdtApplInwrdDt());
            objLoanApplicationTO.setSchemName(getCboSchemName());
            objLoanApplicationTO.setPurposeCode(getCboPurposeCode());
            objLoanApplicationTO.setLoanAmt(getTxtLoanAmt());
            objLoanApplicationTO.setSuretyName(getTxtSuretyName());
            objLoanApplicationTO.setRemarks(getTxaRemarks());
            objLoanApplicationTO.setRegstrStatus(getCboRegstrStatus());
            objLoanApplicationTO.setFromDt(getTdtFromDt());
            objLoanApplicationTO.setDueDt(getTdtDueDt());
            objLoanApplicationTO.setIsTransaction(getTxtisTransaction());
            objLoanApplicationTO.setBranCode(ProxyParameters.BRANCH_ID);
            objLoanApplicationTO.setTotalSalary(getTxttotSalary());
            objLoanApplicationTO.setEligibleAmt(getTxtEligibleAmt());
            objLoanApplicationTO.setRepaymentType(getCboRepayFreq_Repayment());
            objLoanApplicationTO.setCostOfVehicle(getTxtCostOfVehicle());
            objLoanApplicationTO.setTxtInstallmentAmt(getTxtInstallAmount());
            // objLoanApplicationTO.setBranCode(null);
            if (getCommand().equalsIgnoreCase("INSERT")) {
                objLoanApplicationTO.setCreatedBy(TrueTransactMain.USER_ID);
                objLoanApplicationTO.setCreatedDt(ClientUtil.getCurrentDate());
            }
            if (getCommand().equalsIgnoreCase("UPDATE") || getCommand().equalsIgnoreCase("DELETE")) {
                //objLoanApplicationTO.setEmpTransferID(getTxtEmpTransferID());  
            }
        } catch (Exception e) {
            log.info("Error In setLoanApplicationData()");
            e.printStackTrace();
        }
        return objLoanApplicationTO;
    }
//    
    public ServiceTaxDetailsTO setServiceTaxDetails() {
        final ServiceTaxDetailsTO objservicetaxDetTo = new ServiceTaxDetailsTO();
        try {

            //objLoanApplicationTO.setBranCode(TrueTransactMain.BRANCH_ID);
            objservicetaxDetTo.setCommand(getCommand());
            objservicetaxDetTo.setStatus(getAction());
            objservicetaxDetTo.setStatusBy(TrueTransactMain.USER_ID);
            objservicetaxDetTo.setAcct_Num(getTxtApplNo());
            objservicetaxDetTo.setParticulars("Loan Application");
            objservicetaxDetTo.setBranchID(ProxyParameters.BRANCH_ID);
            System.out.println("SERVICE_TAX Map :: " + serviceTax_Map);
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

            
            objservicetaxDetTo.setRoundVal(roundOffAmt(CommonUtil.convertObjToStr(roudVal),"NEAREST_VALUE"));
            objservicetaxDetTo.setStatusDt(ClientUtil.getCurrentDate());
            objservicetaxDetTo.setTrans_type("C");
            // objLoanApplicationTO.setBranCode(null);
            if (getCommand().equalsIgnoreCase("INSERT")) {
                objservicetaxDetTo.setCreatedBy(TrueTransactMain.USER_ID);
                objservicetaxDetTo.setCreatedDt(ClientUtil.getCurrentDate());
            }
            if (getCommand().equalsIgnoreCase("UPDATE") || getCommand().equalsIgnoreCase("DELETE")) {
                //objLoanApplicationTO.setEmpTransferID(getTxtEmpTransferID());  
            }
        } catch (Exception e) {
            log.info("Error In setLoanApplicationData()");
            e.printStackTrace();
        }
        return objservicetaxDetTo;

    }
    private void populateData(LoanApplicationTO objLoanApplicationTO) throws Exception {
        this.setTxtCustId((objLoanApplicationTO.getCustId()));
        //   System.out.println("populate inward no...."+getTxtInwardNo()+"dddd"+objLoanApplicationTO.getInwardNo());
        this.setTxtMemId(CommonUtil.convertObjToStr(objLoanApplicationTO.getMemId()));
        this.setCboPurposeCode(CommonUtil.convertObjToStr(objLoanApplicationTO.getPurposeCode()));
        this.setTxtRepayType(CommonUtil.convertObjToStr(objLoanApplicationTO.getRePayment()));
        this.setNoOfInstallmnt(CommonUtil.convertObjToStr(objLoanApplicationTO.getNoOfInstalmnt()));
        this.setMoratoriumPeriod(CommonUtil.convertObjToStr(objLoanApplicationTO.getMoratoriumPeriod()));
        this.setTxtMemName(CommonUtil.convertObjToStr(objLoanApplicationTO.getMemName()));
        this.setTxtApplNo(CommonUtil.convertObjToStr(objLoanApplicationTO.getApplNo()));
        this.setTdtApplDt(objLoanApplicationTO.getApplDt());
        this.setTdtApplInwrdDt(objLoanApplicationTO.getApplInwrdDt());
        this.setCboSchemName(objLoanApplicationTO.getSchemName());
        this.setTxtLoanAmt(objLoanApplicationTO.getLoanAmt());
        this.setTxtSuretyName(CommonUtil.convertObjToStr(objLoanApplicationTO.getSuretyName()));
        this.setTxaRemarks(CommonUtil.convertObjToStr(objLoanApplicationTO.getRemarks()));
        this.setCboRegstrStatus(objLoanApplicationTO.getRegstrStatus());
        //this.setTxt(CommonUtil.convertObjToStr(objLoanApplicationTO.getCurrBran()));
        //this.setCurrBranName(CommonUtil.convertObjToStr(objLoanApplicationTO.getCurrBranName()));
        this.setTxtStatusBy(CommonUtil.convertObjToStr(objLoanApplicationTO.getStatusBy()));
        this.setCreatedDt(CommonUtil.convertObjToStr(objLoanApplicationTO.getCreatedDt()));
        this.setStatusBy(CommonUtil.convertObjToStr(objLoanApplicationTO.getStatusBy()));
        this.setTxttotSalary(objLoanApplicationTO.getTotalSalary());
        this.setTxtEligibleAmt(objLoanApplicationTO.getEligibleAmt());
        this.setCboRepayFreq_Repayment(objLoanApplicationTO.getRepaymentType());
        this.setTdtFromDt(objLoanApplicationTO.getFromDt());
        this.setTdtDueDt(objLoanApplicationTO.getDueDt());
        this.setTxtCostOfVehicle(objLoanApplicationTO.getCostOfVehicle());
        this.setTxtInstallAmount(CommonUtil.convertObjToDouble(objLoanApplicationTO.getTxtInstallmentAmt()));
        setChanged();
        notifyObservers();
    }

    //security details
    public void setSalarySecrityTableValue(int selectedRow, int rowCount) {

        ArrayList singleList = new ArrayList();
        ArrayList totList = new ArrayList();
        if (salarySecurityAll == null) {
            salarySecurityAll = new LinkedHashMap();
        }

        TermLoanSecuritySalaryTO obj = new TermLoanSecuritySalaryTO();
        if (rowCount == 0 && selectedRow == -1) {
            securitySlNo++;
            obj = addTermLoanSecuritySalaryTO(1);
            singleList.add(String.valueOf("1"));
            singleList.add(getTxtSalaryCertificateNo());
            singleList.add(getTxtMemberNum());
            singleList.add(getTxtEmployerName());
            singleList.add(getTxtContactNo());
            singleList.add(getTxtNetWorth());
            //            tblSalarySecrityTable.insertRow(singleList);
            totList.add(singleList);
            tblSalarySecrityTable.setDataArrayList(totList, salaryTitle);
            salarySecurityAll.put(String.valueOf(1), obj);


        } else if (selectedRow == -1) {
            singleList = (ArrayList) tblSalarySecrityTable.getDataArrayList().get(tblSalarySecrityTable.getDataArrayList().size() - 1);
            if (singleList != null && singleList.size() > 0) {
                totList = (ArrayList) tblSalarySecrityTable.getDataArrayList();
                int slno = CommonUtil.convertObjToInt(singleList.get(0));

                securitySlNo++;
                //                slno++;
                ArrayList newList = new ArrayList();
                newList.add(String.valueOf(securitySlNo));
                newList.add(getTxtSalaryCertificateNo());
                newList.add(getTxtMemberNum());
                newList.add(getTxtEmployerName());
                newList.add(getTxtContactNo());
                newList.add(getTxtNetWorth());
                //            tblSalarySecrityTable.insertRow(singleList);
                totList.add(newList);
                tblSalarySecrityTable.setDataArrayList(totList, salaryTitle);
                //tblSalarySecrityTable.
                salarySecurityAll.put(String.valueOf(securitySlNo), addTermLoanSecuritySalaryTO(securitySlNo));
            }

        } else {
            singleList = (ArrayList) tblSalarySecrityTable.getDataArrayList().get(selectedRow);
            if (singleList != null && singleList.size() > 0) {
                int slno = CommonUtil.convertObjToInt(singleList.get(0));
                salarySecurityAll.put(String.valueOf(slno), addTermLoanSecuritySalaryTO(slno));
            }
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

    public void showSalaryTableValues(int selectedRow) {
        if (selectedRow != -1) {
            ArrayList selectedList = (ArrayList) tblSalarySecrityTable.getDataArrayList().get(selectedRow);
            String slno = CommonUtil.convertObjToStr(selectedList.get(0));
            TermLoanSecuritySalaryTO objTermLoanSecuritySalaryTO = (TermLoanSecuritySalaryTO) salarySecurityAll.get(String.valueOf(slno));

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

    public void populateMemberTypeDetails(String row) {
        try {
            resetMemberTypeDetails();
            final TermLoanSecurityMemberTO objMemberTypeTO = (TermLoanSecurityMemberTO) memberTypeMap.get(row);
            populateMemberTableData(objMemberTypeTO);

        } catch (Exception e) {
            parseException.logException(e, true);
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
    public void addMemberTypeTable(int rowSelected, boolean updateMode) {
        try {
            int rowSel = rowSelected;
            final TermLoanSecurityMemberTO objMemberTypeTO = new TermLoanSecurityMemberTO();
            if (memberTypeMap == null) {
                memberTypeMap = new LinkedHashMap();
            }


            if (getActionType() == ClientConstants.ACTIONTYPE_EDIT) {
                if (isMemberTypeData()) {
                    objMemberTypeTO.setStatusDt(curDate);
                    objMemberTypeTO.setStatusBy(TrueTransactMain.USER_ID);
                    objMemberTypeTO.setStatus(CommonConstants.STATUS_CREATED);
                } else {
                    objMemberTypeTO.setStatusDt(curDate);
                    objMemberTypeTO.setStatusBy(TrueTransactMain.USER_ID);
                    objMemberTypeTO.setStatus(CommonConstants.STATUS_MODIFIED);
                }
            } else {
                objMemberTypeTO.setStatusDt(curDate);
                objMemberTypeTO.setStatusBy(TrueTransactMain.USER_ID);
                objMemberTypeTO.setStatus(CommonConstants.STATUS_CREATED);
            }
            objMemberTypeTO.setAcctNum(getStrACNumber());
            objMemberTypeTO.setMemberNo(getTxtMemNo());
            objMemberTypeTO.setMemberName(getTxtMembName());
            objMemberTypeTO.setMemberType(getTxtMemType());
            objMemberTypeTO.setContactNo(CommonUtil.convertObjToLong(getTxtContactNum()));
            objMemberTypeTO.setNetworth(getTxtMemNetworth());
            objMemberTypeTO.setPriority(CommonUtil.convertObjToInt(getTxtMemPriority()));
            objMemberTypeTO.setTxtMemberTotalSalary(CommonUtil.convertObjToDouble(getTxtMemberTotalSalary()));
            memberTypeMap.put(objMemberTypeTO.getMemberNo(), objMemberTypeTO);
            updateMemberTypeDetails(rowSel, objMemberTypeTO, updateMode);
            //            ttNotifyObservers();
        } catch (Exception e) {
            parseException.logException(e, true);
        }
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

    public void resetMemberTypeDetails() {
        setTxtMemNo("");
        setTxtMembName("");
        setTxtMemType("");
        setTxtContactNum("");
        setTxtMemNetworth("");
        setTxtMemPriority("");
        setTxtMemberTotalSalary("");
        
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

    public void resetSecurityMemberTableValues() {
        tblMemberTypeDetails.setDataArrayList(null, tableMemberTitle);
    }
    
      public void resetSecurityVehicleTableValues() {
        tblVehicleTypeDetails.setDataArrayList(null, tableVehicleTitle);
    }

    public void resetSecurityCollateralTableValues() {
        tblCollateralDetails.setDataArrayList(null, tableCollateralTitle);
    }

    public void resetDepositTypeTableValues() {
        tblDepositTypeDetails.setDataArrayList(null, tableTitleDepositList);
    }

    public void resetLosTypeTableValues() {
        tblLosTypeDetails.setDataArrayList(null, tableTitleLosList);
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

    public void deleteMemberTableData(String val, int row) {
        if (deletedMemberTypeMap == null) {
            deletedMemberTypeMap = new LinkedHashMap();
        }
        TermLoanSecurityMemberTO objMemberTypeTO = (TermLoanSecurityMemberTO) memberTypeMap.get(val);
        objMemberTypeTO.setStatus(CommonConstants.STATUS_DELETED);
        deletedMemberTypeMap.put(CommonUtil.convertObjToStr(tblMemberTypeDetails.getValueAt(row, 0)), memberTypeMap.get(val));
        Object obj;
        obj = val;
        memberTypeMap.remove(val);
        tblMemberTypeDetails.setDataArrayList(null, tableMemberTitle);
        try {
            populateMemberTable();
        } catch (Exception e) {
            parseException.logException(e, true);
        }
    }

    public void populateCollateralDetails(String row) {
        try {
            resetCollateralDetails();
            final TermLoanSecurityLandTO objTermLoanSecurityLandTO = (TermLoanSecurityLandTO) collateralMap.get(row);
            populateCollateralTableData(objTermLoanSecurityLandTO);

        } catch (Exception e) {
            parseException.logException(e, true);
        }
    }

    public HashMap validatePledgeAmount(String docNumber, double pledgeAmt) {
        HashMap tempMap = new HashMap();
        HashMap returnMap = new HashMap();
        //        tempMap.put("DOC_NO",docNumber);
        //        tempMap.put("PLEDGE_AMT",new Double(pledgeAmt));
        //        pledgeValMap.put(docNumber,tempMap);
        if (pledgeValMap != null) {
            //            Set set=pledgeValMap.keySet();
            //            Object obj[]=(Object[])set.toArray();
            for (int i = 0; i < pledgeValMap.size(); i++) {
                tempMap = (HashMap) pledgeValMap.get(docNumber);
                if (tempMap != null && CommonUtil.convertObjToStr(tempMap.get("DOC_NO")).equals(docNumber)) {
                    if (CommonUtil.convertObjToDouble(tempMap.get("PLEDGE_AMT")).doubleValue() < pledgeAmt) {
                        returnMap.putAll(tempMap);
                        return returnMap;
                    }
                }
            }
        }
        return returnMap;
    }

    public void addCollateralTable(int rowSelected, boolean updateMode) {
        try {
            int rowSel = rowSelected;
            final TermLoanSecurityLandTO objTermLoanSecurityLandTO = new TermLoanSecurityLandTO();
            TermLoanSecurityLandTO obj = new TermLoanSecurityLandTO();
            if (collateralMap == null) {
                collateralMap = new LinkedHashMap();
            }
            if (rowSelected != -1 && (!tblCollateralDetails.getDataArrayList().isEmpty())) {
                ArrayList list = (ArrayList) tblCollateralDetails.getDataArrayList().get(rowSelected);
                obj = (TermLoanSecurityLandTO) collateralMap.get(list.get(0) + "_" + (rowSelected + 1));
            }
            if (getActionType() == ClientConstants.ACTIONTYPE_EDIT) {
                if (isCollateralTypeData() || (!obj.getStatus().equals(CommonConstants.STATUS_MODIFIED))) {
                    objTermLoanSecurityLandTO.setStatusDt(curDate);
                    objTermLoanSecurityLandTO.setStatusBy(TrueTransactMain.USER_ID);
                    objTermLoanSecurityLandTO.setStatus(CommonConstants.STATUS_CREATED);
                } else {
                    objTermLoanSecurityLandTO.setStatusDt(curDate);
                    objTermLoanSecurityLandTO.setStatusBy(TrueTransactMain.USER_ID);
                    objTermLoanSecurityLandTO.setStatus(CommonConstants.STATUS_MODIFIED);
                }
            } else {
                objTermLoanSecurityLandTO.setStatusDt(curDate);
                objTermLoanSecurityLandTO.setStatusBy(TrueTransactMain.USER_ID);
                objTermLoanSecurityLandTO.setStatus(CommonConstants.STATUS_CREATED);
            }
            if (isRdoGahanYes()) {
                objTermLoanSecurityLandTO.setGahanYesNo("Y");
            } else if (isRdoGahanNo()) {
                objTermLoanSecurityLandTO.setGahanYesNo("N");
            } else {
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
            objTermLoanSecurityLandTO.setOldSurvyNo(getOldSurvyNo());
            collateralMap.put(objTermLoanSecurityLandTO.getMemberNo() + "_" + (getRowCoun()), objTermLoanSecurityLandTO);
            updateCollateralDetails(rowSel, objTermLoanSecurityLandTO, updateMode);
        } catch (Exception e) {
            parseException.logException(e, true);
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

    public void deleteCollateralTableData(String val, int row) {
        if (deletedCollateralMap == null) {
            deletedCollateralMap = new LinkedHashMap();
        }
        TermLoanSecurityLandTO objTermLoanSecurityLandTO = (TermLoanSecurityLandTO) collateralMap.get(val);
        objTermLoanSecurityLandTO.setStatus(CommonConstants.STATUS_DELETED);
        deletedCollateralMap.put(CommonUtil.convertObjToStr(tblCollateralDetails.getValueAt(row, 0)), collateralMap.get(val));
        Object obj;
        obj = val;
        collateralMap.remove(val);
        tblCollateralDetails.setDataArrayList(null, tableCollateralTitle);
        try {
            populateCollateralTable();
            objTermLoanSecurityLandTO.setOldSurvyNo(getOldSurvyNo());
        } catch (Exception e) {
            parseException.logException(e, true);
        }
    }

    public void addDepositTypeTable(int rowSelected, boolean updateMode) {
        try {
            int rowSel = rowSelected;
            final TermLoanDepositTypeTO objDepositTypeTO = new TermLoanDepositTypeTO();
            if (depositTypeMap == null) {
                depositTypeMap = new LinkedHashMap();
            }
            if (getActionType() == ClientConstants.ACTIONTYPE_EDIT) {
                if (isDepositTypeData()) {
                    objDepositTypeTO.setStatusDt(ClientUtil.getCurrentDate());
                    objDepositTypeTO.setStatusBy(TrueTransactMain.USER_ID);
                    objDepositTypeTO.setStatus(CommonConstants.STATUS_CREATED);
                } else {
                    objDepositTypeTO.setStatusDt(ClientUtil.getCurrentDate());
                    objDepositTypeTO.setStatusBy(TrueTransactMain.USER_ID);
                    objDepositTypeTO.setStatus(CommonConstants.STATUS_MODIFIED);
                }
            } else {
                objDepositTypeTO.setStatusDt(ClientUtil.getCurrentDate());
                objDepositTypeTO.setStatusBy(TrueTransactMain.USER_ID);
                objDepositTypeTO.setStatus(CommonConstants.STATUS_CREATED);
            }
            objDepositTypeTO.setBorrowNo(getBorrowerNo());
            objDepositTypeTO.setTxtDepNo(getTxtDepNo());
            //            objDepositTypeTO.setProdId(getTxtProductId());
            objDepositTypeTO.setProdType(CommonUtil.convertObjToStr(cbmProdTypeSecurity.getKeyForSelected()));
            objDepositTypeTO.setProdId(CommonUtil.convertObjToStr(cbmDepProdID.getKeyForSelected()));
            objDepositTypeTO.setTxtDepAmount(CommonUtil.convertObjToDouble(getTxtDepAmount()));
            objDepositTypeTO.setTdtDepDt(DateUtil.getDateMMDDYYYY(getTdtDepDt()));
            objDepositTypeTO.setTxtMaturityDt(DateUtil.getDateMMDDYYYY(getTxtMaturityDt()));
            objDepositTypeTO.setTxtMaturityValue(CommonUtil.convertObjToDouble(getTxtMaturityValue()));
            objDepositTypeTO.setTxtRateOfInterest(getTxtRateOfInterest());
            depositTypeMap.put(objDepositTypeTO.getTxtDepNo(), objDepositTypeTO);
            updateDepositTypeDetails(rowSel, objDepositTypeTO, updateMode);
            //            ttNotifyObservers();
        } catch (Exception e) {
            parseException.logException(e, true);
        }
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

    public void deleteDepositTableData(String val, int row) {
        if (deletedDepositTypeMap == null) {
            deletedDepositTypeMap = new LinkedHashMap();
        }
        TermLoanDepositTypeTO objDepositTypeTO = (TermLoanDepositTypeTO) depositTypeMap.get(val);
        objDepositTypeTO.setStatus(CommonConstants.STATUS_DELETED);
        deletedDepositTypeMap.put(CommonUtil.convertObjToStr(tblDepositTypeDetails.getValueAt(row, 1)), depositTypeMap.get(val));
        Object obj;
        obj = val;
        depositTypeMap.remove(val);
        tblDepositTypeDetails.setDataArrayList(null, tableTitleDepositList);
        try {
            populateDepositTable();
        } catch (Exception e) {
            parseException.logException(e, true);
        }
    }

    public void setCbmProdTypeSecurity(String prodType) {
        try {

            HashMap lookUpHash = new HashMap();
            HashMap whereMap = new HashMap();
            if (prodType.equals("MDS")) {
                if (isChkLoaneeSecurityonly() == true) {
                    lookUpHash.put(CommonConstants.MAP_NAME, "Lock.getAccProductMDSLIEN");
                    whereMap.put("cust_id", getTxtCustId());
                } else {
                    lookUpHash.put(CommonConstants.MAP_NAME, "Lock.getAccProductMDS");
                }
            } else {
                lookUpHash.put(CommonConstants.MAP_NAME, "deposit_getProdId");
            }
            if (whereMap != null && whereMap.containsKey("cust_id")) {
                lookUpHash.put(CommonConstants.PARAMFORQUERY, whereMap);
            } else {
                lookUpHash.put(CommonConstants.PARAMFORQUERY, null);
            }

            keyValue = ClientUtil.populateLookupData(lookUpHash);
            getKeyValue((HashMap) keyValue.get(CommonConstants.DATA));
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        cbmDepProdID = new ComboBoxModel(key, value);
        this.cbmDepProdID = cbmDepProdID;
        setChanged();
    }

    public void populateDepositTypeDetails(String row) {
        try {
            resetDepositTypeDetails();
            final TermLoanDepositTypeTO objDepositTypeTO = (TermLoanDepositTypeTO) depositTypeMap.get(row);
            populateDepositTableData(objDepositTypeTO);
        } catch (Exception e) {
            parseException.logException(e, true);
        }
    }

    public void addLosTypeTable(int rowSelected, boolean updateMode) {
        try {
            int rowSel = rowSelected;
            final TermLoanLosTypeTO objLosTypeTO = new TermLoanLosTypeTO();
            if (losTypeMap == null) {
                losTypeMap = new LinkedHashMap();
            }
            if (getActionType() == ClientConstants.ACTIONTYPE_EDIT) {
                if (isLosTypeData()) {
                    objLosTypeTO.setStatusDt(ClientUtil.getCurrentDate());
                    objLosTypeTO.setStatusBy(TrueTransactMain.USER_ID);
                    objLosTypeTO.setStatus(CommonConstants.STATUS_CREATED);
                } else {
                    objLosTypeTO.setStatusDt(ClientUtil.getCurrentDate());
                    objLosTypeTO.setStatusBy(TrueTransactMain.USER_ID);
                    objLosTypeTO.setStatus(CommonConstants.STATUS_MODIFIED);
                }
            } else {
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
            losTypeMap.put(objLosTypeTO.getLosSecurityNo(), objLosTypeTO);
            updateLosTypeDetails(rowSel, objLosTypeTO, updateMode);
            //            ttNotifyObservers();
        } catch (Exception e) {
            parseException.logException(e, true);
        }
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

    public void deleteLosTableData(String val, int row) {
        if (deletedLosTypeMap == null) {
            deletedLosTypeMap = new LinkedHashMap();
        }
        TermLoanLosTypeTO objLosTypeTO = (TermLoanLosTypeTO) losTypeMap.get(val);
        objLosTypeTO.setStatus(CommonConstants.STATUS_DELETED);
        deletedLosTypeMap.put(CommonUtil.convertObjToStr(tblLosTypeDetails.getValueAt(row, 2)), losTypeMap.get(val));
        Object obj;
        obj = val;
        losTypeMap.remove(val);
        tblLosTypeDetails.setDataArrayList(null, tableTitleLosList);
        try {
            populateLosTable();
        } catch (Exception e) {
            parseException.logException(e, true);
        }
    }

    public void populateLosTypeDetails(String row) {
        try {
            resetLosTypeDetails();
            final TermLoanLosTypeTO objLosTypeTO = (TermLoanLosTypeTO) losTypeMap.get(row);
            populateLosTableData(objLosTypeTO);

        } catch (Exception e) {
            parseException.logException(e, true);
        }
    }

    public void updateCollateralJointDetails(String memberNo) {
        HashMap dataMap = new HashMap();
        ArrayList totList = new ArrayList();
        dataMap.put("MEMBER_NO", memberNo);
        dataMap.put("DOCUMENT_GEN_ID", getDocGenId());
        ArrayList IncParRow = null;
        List lst = null;
        //         tblJointCollateral = new EnhancedTableModel(null, tableCollateralJointTitle);
        if (isRdoGahanYes() == true) {
            lst = ClientUtil.executeQuery("getGahanJointDetailsforLoanFromGahan", dataMap);
        } else {
            lst = ClientUtil.executeQuery("getGahanJointDetailsforLoan", dataMap);
        }
        if (lst != null && lst.size() > 0) {
            for (int i = 0; i < lst.size(); i++) {
                HashMap resultMap = (HashMap) lst.get(i);
                IncParRow = new ArrayList();
                IncParRow.add(resultMap.get("CUST_ID"));
                IncParRow.add(resultMap.get("CUSTOMER"));
                IncParRow.add(resultMap.get("CONSTITUTION"));
                totList.add(IncParRow);
                //            tblJointCollateral.insertRow(tblJointCollateral.getRowCount(),IncParRow);
            }
        }
        tblJointCollateral.setDataArrayList(totList, tableCollateralJointTitle);
    }

    public TermLoanSecuritySalaryTO addTermLoanSecuritySalaryTO(double slno) {

        final TermLoanSecuritySalaryTO objTermLoanSecuritySalaryTO = new TermLoanSecuritySalaryTO();
        try {
            TermLoanSecuritySalaryTO obj = (TermLoanSecuritySalaryTO) salarySecurityAll.get(String.valueOf(slno));
            objTermLoanSecuritySalaryTO.setSlNo(new Double(slno));
            objTermLoanSecuritySalaryTO.setAcctNum(getStrACNumber());
            objTermLoanSecuritySalaryTO.setSalaryCerficateNo(getTxtSalaryCertificateNo());
            objTermLoanSecuritySalaryTO.setEmpName(getTxtEmployerName());
            objTermLoanSecuritySalaryTO.setEmpAddress(getTxtAddress());
            objTermLoanSecuritySalaryTO.setCity(getCboSecurityCity());
            objTermLoanSecuritySalaryTO.setPin(CommonUtil.convertObjToDouble(getTxtPinCode()));
            objTermLoanSecuritySalaryTO.setDesignation(getTxtDesignation());
            objTermLoanSecuritySalaryTO.setContactNo(CommonUtil.convertObjToLong(getTxtContactNo()));
            objTermLoanSecuritySalaryTO.setRetirementDt(getProperDateFormat(getTdtRetirementDt()));
            objTermLoanSecuritySalaryTO.setEmpMemberNo(getTxtMemberNum());
            objTermLoanSecuritySalaryTO.setTotalSalary(CommonUtil.convertObjToDouble(getTxtTotalSalary()));
            objTermLoanSecuritySalaryTO.setNetworth(getTxtNetWorth());
            objTermLoanSecuritySalaryTO.setSalaryRemarks(getTxtSalaryRemark());
            if (!(obj != null && obj.getStatus().equals(CommonConstants.STATUS_MODIFIED))) {
                objTermLoanSecuritySalaryTO.setStatus(CommonConstants.STATUS_CREATED);
            }


        } catch (Exception e) {
            e.printStackTrace();
        }
        return objTermLoanSecuritySalaryTO;
    }

    private void populateMemberTableData(TermLoanSecurityMemberTO objMemberTypeTO) throws Exception {
        setTxtMemNo(CommonUtil.convertObjToStr(objMemberTypeTO.getMemberNo()));
        setTxtMemberTotalSalary(CommonUtil.convertObjToStr(objMemberTypeTO.getTxtMemberTotalSalary()));
        setTxtMembName(CommonUtil.convertObjToStr(objMemberTypeTO.getMemberName()));
        setTxtContactNum(CommonUtil.convertObjToStr(objMemberTypeTO.getContactNo()));
        setTxtMemNetworth(CommonUtil.convertObjToStr(objMemberTypeTO.getNetworth()));
        setTxtMemPriority(CommonUtil.convertObjToStr(objMemberTypeTO.getPriority()));
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

    private void updateMemberTypeDetails(int rowSel, TermLoanSecurityMemberTO objMemberTypeTO, boolean updateMode) throws Exception {
        Object selectedRow;
        boolean rowExists = false;
        //If row already exists update it, else create a new row & append
        for (int i = tblMemberTypeDetails.getRowCount(), j = 0; i > 0; i--, j++) {
            selectedRow = ((ArrayList) tblMemberTypeDetails.getDataArrayList().get(j)).get(0);
            if (getTxtMemNo().equals(CommonUtil.convertObjToStr(selectedRow))) {
                rowExists = true;
                ArrayList IncParRow = new ArrayList();
                ArrayList data = tblMemberTypeDetails.getDataArrayList();
                data.remove(rowSel);
                IncParRow.add(getTxtMemNo());
                IncParRow.add(getTxtMembName());
                IncParRow.add(getTxtMemberTotalSalary());
                IncParRow.add(getTxtContactNum());
                IncParRow.add(getTxtMemNetworth());
                IncParRow.add(getTxtMemPriority());
                tblMemberTypeDetails.insertRow(rowSel, IncParRow);
                IncParRow = null;
            }
        }
        if (!rowExists) {
            ArrayList IncParRow = new ArrayList();
            IncParRow.add(getTxtMemNo());
            IncParRow.add(getTxtMembName());
            IncParRow.add(getTxtMemberTotalSalary());
            IncParRow.add(getTxtContactNum());
            IncParRow.add(getTxtMemNetworth());
            IncParRow.add(getTxtMemPriority());
            tblMemberTypeDetails.insertRow(tblMemberTypeDetails.getRowCount(), IncParRow);
            IncParRow = null;
        }
    }
  private void updateVehicleTypeDetails(int rowSel, TermLoanSecurityVehicleTO objVehicleTypeTO, boolean updateMode) throws Exception {
        Object selectedRow;
        boolean rowExists = false;
        //If row already exists update it, else create a new row & append
      for (int i = tblVehicleTypeDetails.getRowCount(), j = 0; i > 0; i--, j++) {
          selectedRow = ((ArrayList) tblVehicleTypeDetails.getDataArrayList().get(j)).get(0);
          if (getTxtMemNo().equals(CommonUtil.convertObjToStr(selectedRow))) {
              rowExists = true;
              ArrayList IncParRow = new ArrayList();
              ArrayList data = tblVehicleTypeDetails.getDataArrayList();
              data.remove(rowSel);
              IncParRow.add(getTxtVehicleMemNo());
              IncParRow.add(getTxtVehicleMembName());
              IncParRow.add(getTxtVehicleNo());
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
          IncParRow.add(getTxtVehicleRcBookNo());
          IncParRow.add(getTxtVehicleMemSal());
          IncParRow.add(getTxtVehicleNetworth());
          tblVehicleTypeDetails.insertRow(tblVehicleTypeDetails.getRowCount(), IncParRow);
          IncParRow = null;
      }
    }

    private void populateMemberTable() throws Exception {
        ArrayList incDataList = new ArrayList();
        incDataList = new ArrayList(memberTypeMap.keySet());
        ArrayList addList = new ArrayList(memberTypeMap.keySet());
        int length = incDataList.size();
        for (int i = 0; i < length; i++) {
            ArrayList incTabRow = new ArrayList();
            TermLoanSecurityMemberTO objMemberTypeTO = (TermLoanSecurityMemberTO) memberTypeMap.get(addList.get(i));
            IncVal.add(objMemberTypeTO);
            if (!objMemberTypeTO.getStatus().equals("DELETED")) {
                incTabRow.add(CommonUtil.convertObjToStr(objMemberTypeTO.getMemberNo()));
                incTabRow.add(CommonUtil.convertObjToStr(objMemberTypeTO.getMemberType()));
                incTabRow.add(CommonUtil.convertObjToStr(objMemberTypeTO.getMemberName()));
                incTabRow.add(CommonUtil.convertObjToStr(objMemberTypeTO.getContactNo()));
                incTabRow.add(CommonUtil.convertObjToStr(objMemberTypeTO.getNetworth()));
                tblMemberTypeDetails.addRow(incTabRow);
            }
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
                incTabRow.add(CommonUtil.convertObjToStr(objVehicleTypeTo.getVehicleRcBookNo()));
                // incTabRow.add(CommonUtil.convertObjToStr(objVehicleTypeTo.getVehicleType()));
                incTabRow.add(CommonUtil.convertObjToStr(objVehicleTypeTo.getMemSalary()));
                incTabRow.add(CommonUtil.convertObjToStr(objVehicleTypeTo.getNetWorth()));
                tblVehicleTypeDetails.addRow(incTabRow);
            }
        }
    }

    private void populateCollateralTableData(TermLoanSecurityLandTO objTermLoanSecurityLandTO) throws Exception {
        if (CommonUtil.convertObjToStr(objTermLoanSecurityLandTO.getGahanYesNo()).equals("Y")) {
            setRdoGahanYes(true);
        } else if (CommonUtil.convertObjToStr(objTermLoanSecurityLandTO.getGahanYesNo()).equals("N")) {
            setRdoGahanNo(true);
        }
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

    private void updateCollateralDetails(int rowSel, TermLoanSecurityLandTO objTermLoanSecurityLandTO, boolean updateMode) throws Exception {
        Object selectedRow;
        boolean rowExists = false;
        //If row already exists update it, else create a new row & append
        if (!(rowSel == -1)) {
            for (int i = tblCollateralDetails.getRowCount(), j = 0; i > 0; i--, j++) {
                selectedRow = ((ArrayList) tblCollateralDetails.getDataArrayList().get(j)).get(0);
                if (getTxtOwnerMemNo().equals(CommonUtil.convertObjToStr(selectedRow))) {
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
                    tblCollateralDetails.insertRow(rowSel, IncParRow);
                    IncParRow = null;
                }
            }
        }
        if (!rowExists) {
            ArrayList IncParRow = new ArrayList();
            IncParRow.add(getTxtOwnerMemNo());
            IncParRow.add(getTxtOwnerMemberNname());
            IncParRow.add(getTxtDocumentNo());
            IncParRow.add(getTxtPledgeAmount());
            IncParRow.add(getTxtSurveyNo());
            IncParRow.add(getTxtTotalArea());
            tblCollateralDetails.insertRow(tblCollateralDetails.getRowCount(), IncParRow);
            IncParRow = null;
        }
    }

    private void populateCollateralTable() throws Exception {
        ArrayList incDataList = new ArrayList();
        incDataList = new ArrayList(collateralMap.keySet());
        ArrayList addList = new ArrayList(collateralMap.keySet());
        int length = incDataList.size();
        LinkedHashMap tempCollateralmp = new LinkedHashMap();
        for (int i = 0; i < length; i++) {
            ArrayList incTabRow = new ArrayList();
            TermLoanSecurityLandTO objTermLoanSecurityLandTO = (TermLoanSecurityLandTO) collateralMap.get(addList.get(i));
            IncVal.add(objTermLoanSecurityLandTO);
            if (!objTermLoanSecurityLandTO.getStatus().equals("DELETED")) {
                incTabRow.add(CommonUtil.convertObjToStr(objTermLoanSecurityLandTO.getMemberNo()));
                incTabRow.add(CommonUtil.convertObjToStr(objTermLoanSecurityLandTO.getMemberName()));
                incTabRow.add(CommonUtil.convertObjToStr(objTermLoanSecurityLandTO.getDocumentNo()));
                incTabRow.add(CommonUtil.convertObjToStr(objTermLoanSecurityLandTO.getPledgeAmount()));
                incTabRow.add(CommonUtil.convertObjToStr(objTermLoanSecurityLandTO.getSurveyNo()));
                incTabRow.add(CommonUtil.convertObjToStr(objTermLoanSecurityLandTO.getTotalArea()));
                tblCollateralDetails.addRow(incTabRow);
                tempCollateralmp.put(objTermLoanSecurityLandTO.getMemberNo() + "_" + (i + 1), objTermLoanSecurityLandTO);
            }

        }
        collateralMap = new LinkedHashMap();
        collateralMap.putAll(tempCollateralmp);
    }

    private void updateDepositTypeDetails(int rowSel, TermLoanDepositTypeTO objDepositTypeTO, boolean updateMode) throws Exception {
        Object selectedRow;
        boolean rowExists = false;
        //If row already exists update it, else create a new row & append
        for (int i = tblDepositTypeDetails.getRowCount(), j = 0; i > 0; i--, j++) {
            selectedRow = ((ArrayList) tblDepositTypeDetails.getDataArrayList().get(j)).get(1);
            if (getTxtDepNo().equals(CommonUtil.convertObjToStr(selectedRow))) {
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
                tblDepositTypeDetails.insertRow(rowSel, IncParRow);
                IncParRow = null;
            }
        }
        if (!rowExists) {
            ArrayList IncParRow = new ArrayList();
            IncParRow.add(CommonUtil.convertObjToStr(cboProductTypeSecurity));
            IncParRow.add(getTxtDepNo());
            //            IncParRow.add(CommonUtil.convertObjToStr(cbmDepProdID.getKeyForSelected()));
            //            IncParRow.add(getTdtDepDt());
            IncParRow.add(getTxtDepAmount());
            //            IncParRow.add(getTxtRateOfInterest());
            IncParRow.add(getTxtMaturityValue());
            //            IncParRow.add(getTxtMaturityDt());
            tblDepositTypeDetails.insertRow(tblDepositTypeDetails.getRowCount(), IncParRow);
            IncParRow = null;
        }
    }

    private void populateDepositTable() throws Exception {
        ArrayList incDataList = new ArrayList();
        incDataList = new ArrayList(depositTypeMap.keySet());
        ArrayList addList = new ArrayList(depositTypeMap.keySet());
        int length = incDataList.size();
        for (int i = 0; i < length; i++) {
            ArrayList incTabRow = new ArrayList();
            TermLoanDepositTypeTO objDepositTypeTO = (TermLoanDepositTypeTO) depositTypeMap.get(addList.get(i));
            IncVal.add(objDepositTypeTO);
            if (!objDepositTypeTO.getStatus().equals("DELETED")) {
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

    private void getKeyValue(HashMap keyValue) throws Exception {
        key = (ArrayList) keyValue.get(CommonConstants.KEY);
        value = (ArrayList) keyValue.get(CommonConstants.VALUE);
    }

    private void populateDepositTableData(TermLoanDepositTypeTO objDepositTypeTO) throws Exception {
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

    private void updateLosTypeDetails(int rowSel, TermLoanLosTypeTO objLosTypeTO, boolean updateMode) throws Exception {
        Object selectedRow;
        boolean rowExists = false;
        //If row already exists update it, else create a new row & append
        for (int i = tblLosTypeDetails.getRowCount(), j = 0; i > 0; i--, j++) {
            selectedRow = ((ArrayList) tblLosTypeDetails.getDataArrayList().get(j)).get(2);
            if (getTxtLosSecurityNo().equals(CommonUtil.convertObjToStr(selectedRow))) {
                rowExists = true;
                ArrayList IncParRow = new ArrayList();
                ArrayList data = tblLosTypeDetails.getDataArrayList();
                data.remove(rowSel);
                IncParRow.add(CommonUtil.convertObjToStr(cboLosOtherInstitution));
                IncParRow.add(getTxtLosName());
                IncParRow.add(getTxtLosSecurityNo());
                IncParRow.add(cboLosSecurityType);
                IncParRow.add(getTxtLosAmount());
                tblLosTypeDetails.insertRow(rowSel, IncParRow);
                IncParRow = null;
            }
        }
        if (!rowExists) {
            ArrayList IncParRow = new ArrayList();
            IncParRow.add(CommonUtil.convertObjToStr(cboLosOtherInstitution));
            IncParRow.add(getTxtLosName());
            IncParRow.add(getTxtLosSecurityNo());
            IncParRow.add(cboLosSecurityType);
            IncParRow.add(getTxtLosAmount());
            tblLosTypeDetails.insertRow(tblLosTypeDetails.getRowCount(), IncParRow);
            IncParRow = null;
        }
    }

    private void populateLosTable() throws Exception {
        ArrayList incDataList = new ArrayList();
        incDataList = new ArrayList(losTypeMap.keySet());
        ArrayList addList = new ArrayList(losTypeMap.keySet());
        int length = incDataList.size();
        for (int i = 0; i < length; i++) {
            ArrayList incTabRow = new ArrayList();
            TermLoanLosTypeTO objLosTypeTO = (TermLoanLosTypeTO) losTypeMap.get(addList.get(i));
            IncVal.add(objLosTypeTO);
            if (!objLosTypeTO.getStatus().equals("DELETED")) {
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

    private void populateLosTableData(TermLoanLosTypeTO objLosTypeTO) throws Exception {
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

    private void populateTermLoanSecurityVehicleTOData(HashMap mapData) {
        if (vehicleTypeMap == null) {
            vehicleTypeMap = new LinkedHashMap();
        }
        tblVehicleTypeDetails.setDataArrayList(null, tableVehicleTitle);
        vehicleTypeMap = (LinkedHashMap) mapData.get("TermLoanSecurityvehicleListTO");
        ArrayList addList = new ArrayList(vehicleTypeMap.keySet());
        for (int i = 0; i < addList.size(); i++) {
            TermLoanSecurityVehicleTO objVehicleTypeTO = (TermLoanSecurityVehicleTO) vehicleTypeMap.get(addList.get(i));
            objVehicleTypeTO.setStatus(CommonConstants.STATUS_MODIFIED);
            ArrayList incTabRow = new ArrayList();
            incTabRow.add(CommonUtil.convertObjToStr(objVehicleTypeTO.getMemberNo()));
            incTabRow.add(CommonUtil.convertObjToStr(objVehicleTypeTO.getMemberName()));
            incTabRow.add(CommonUtil.convertObjToStr(objVehicleTypeTO.getVehicleNo()));
            // incTabRow.add(CommonUtil.convertObjToStr(objVehicleTypeTO.getVehicleType()));
            incTabRow.add(CommonUtil.convertObjToStr(objVehicleTypeTO.getVehicleRcBookNo()));
            incTabRow.add(CommonUtil.convertObjToStr(objVehicleTypeTO.getMemSalary()));
            incTabRow.add(CommonUtil.convertObjToStr(objVehicleTypeTO.getNetWorth()));
            tblVehicleTypeDetails.addRow(incTabRow);
            vehicleTypeMap.put(CommonUtil.convertObjToStr(objVehicleTypeTO.getMemberNo()), objVehicleTypeTO);
            //    System.out.println("tblVehicleTypeDetails>>>>" + tblVehicleTypeDetails.getRowCount());

        }
    }
    private void populateTermLoanSecuritySalaryTOData(HashMap mapData) {

        if (salarySecurityAll == null) {
            salarySecurityAll = new LinkedHashMap();
        }
        tblSalarySecrityTable.setDataArrayList(null, salaryTitle);
        LinkedHashMap totsalarySecurity = (LinkedHashMap) mapData.get("TermLoanSecuritySalaryTO");
        ArrayList addList = new ArrayList(totsalarySecurity.keySet());
        for (int i = 0; i < addList.size(); i++) {
            TermLoanSecuritySalaryTO objTermLoanSecuritySalaryTO = (TermLoanSecuritySalaryTO) totsalarySecurity.get(addList.get(i));
            objTermLoanSecuritySalaryTO.setStatus(CommonConstants.STATUS_MODIFIED);
            ArrayList incTabRow = new ArrayList();
            securitySlNo = CommonUtil.convertObjToInt(objTermLoanSecuritySalaryTO.getSlNo());
            incTabRow.add(CommonUtil.convertObjToStr(objTermLoanSecuritySalaryTO.getSlNo()));
            incTabRow.add(CommonUtil.convertObjToStr(objTermLoanSecuritySalaryTO.getSalaryCerficateNo()));
            incTabRow.add(CommonUtil.convertObjToStr(objTermLoanSecuritySalaryTO.getEmpMemberNo()));
            incTabRow.add(CommonUtil.convertObjToStr(objTermLoanSecuritySalaryTO.getEmpName()));
            incTabRow.add(CommonUtil.convertObjToStr(objTermLoanSecuritySalaryTO.getContactNo()));
            incTabRow.add(CommonUtil.convertObjToStr(objTermLoanSecuritySalaryTO.getNetworth()));
            tblSalarySecrityTable.addRow(incTabRow);
            salarySecurityAll.put(CommonUtil.convertObjToStr(objTermLoanSecuritySalaryTO.getSlNo()), objTermLoanSecuritySalaryTO);
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

    //security end..
    public void resetForm() {
        setTxtCustId("");
        setTxtMemId("");
        setTxtRepayType("");
        setNoOfInstallmnt("");
        setMoratoriumPeriod("");
        setTxtMemName("");
        setTxtApplNo("");
        setTdtApplDt(null);
        setTdtApplInwrdDt(null);
        setCboSchemName("");
        setTxtLoanAmt(null);
        setTxtSuretyName("");
        setTxaRemarks("");
        setCboRegstrStatus("");
        setChanged();
        setTxtCostOfVehicle(0.0);
        setTxttotSalary(0.0);
        ttNotifyObservers();
        setLblServiceTaxval("");
    }
    //charge details

    public void interestTransaction(HashMap interstMap) throws Exception {
        try {
            interstMap.put("INT_TRANSACTION_REPAYMENT", "INT_TRANSACTION_REPAYMENT");
            HashMap mapData = (HashMap) proxy.executeQuery(interstMap, map);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    //end..

    /**
     * To set the status based on ActionType, either New, Edit, etc.,
     */
    public void setStatus() {
        this.setLblStatus(ClientConstants.ACTION_STATUS[this.getActionType()]);
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
     * Getter for property txtCustId.
     *
     * @return Value of property txtCustId.
     */
    public String getTxtCustId() {
        return txtCustId;
    }

    /**
     * Setter for property txtCustId.
     *
     * @param txtCustId New value of property txtCustId.
     */
    public void setTxtCustId(String txtCustId) {
        this.txtCustId = txtCustId;
    }

    /**
     * Getter for property txtMemId.
     *
     * @return Value of property txtMemId.
     */
    public String getTxtMemId() {
        return txtMemId;
    }

    /**
     * Setter for property txtMemId.
     *
     * @param txtMemId New value of property txtMemId.
     */
    public void setTxtMemId(String txtMemId) {
        this.txtMemId = txtMemId;
    }

    /**
     * Getter for property txtMemName.
     *
     * @return Value of property txtMemName.
     */
    public String getTxtMemName() {
        return txtMemName;
    }

    /**
     * Setter for property txtMemName.
     *
     * @param txtMemName New value of property txtMemName.
     */
    public void setTxtMemName(String txtMemName) {
        this.txtMemName = txtMemName;
    }

    /**
     * Getter for property txtApplNo.
     *
     * @return Value of property txtApplNo.
     */
    public String getTxtApplNo() {
        return txtApplNo;
    }

    /**
     * Setter for property txtApplNo.
     *
     * @param txtApplNo New value of property txtApplNo.
     */
    public void setTxtApplNo(String txtApplNo) {
        this.txtApplNo = txtApplNo;
    }

    /**
     * Getter for property tdtApplDt.
     *
     * @return Value of property tdtApplDt.
     */
    public Date getTdtApplDt() {
        return tdtApplDt;
    }

    /**
     * Setter for property tdtApplDt.
     *
     * @param tdtApplDt New value of property tdtApplDt.
     */
    public void setTdtApplDt(Date tdtApplDt) {
        this.tdtApplDt = tdtApplDt;
    }

    /**
     * Getter for property tdtApplInwrdDt.
     *
     * @return Value of property tdtApplInwrdDt.
     */
    public Date getTdtApplInwrdDt() {
        return tdtApplInwrdDt;
    }

    /**
     * Setter for property tdtApplInwrdDt.
     *
     * @param tdtApplInwrdDt New value of property tdtApplInwrdDt.
     */
    public void setTdtApplInwrdDt(Date tdtApplInwrdDt) {
        this.tdtApplInwrdDt = tdtApplInwrdDt;
    }

    /**
     * Getter for property cboSchemName.
     *
     * @return Value of property cboSchemName.
     */
    public String getCboSchemName() {
        return cboSchemName;
    }

    /**
     * Setter for property cboSchemName.
     *
     * @param cboSchemName New value of property cboSchemName.
     */
    public void setCboSchemName(String cboSchemName) {
        this.cboSchemName = cboSchemName;
    }

    /**
     * Getter for property txtSuretyName.
     *
     * @return Value of property txtSuretyName.
     */
    public String getTxtSuretyName() {
        return txtSuretyName;
    }

    /**
     * Setter for property txtSuretyName.
     *
     * @param txtSuretyName New value of property txtSuretyName.
     */
    public void setTxtSuretyName(String txtSuretyName) {
        this.txtSuretyName = txtSuretyName;
    }

    /**
     * Getter for property txaRemarks.
     *
     * @return Value of property txaRemarks.
     */
    public String getTxaRemarks() {
        return txaRemarks;
    }

    /**
     * Setter for property txaRemarks.
     *
     * @param txaRemarks New value of property txaRemarks.
     */
    public void setTxaRemarks(String txaRemarks) {
        this.txaRemarks = txaRemarks;
    }

    /**
     * Getter for property cboRegstrStatus.
     *
     * @return Value of property cboRegstrStatus.
     */
    public String getCboRegstrStatus() {
        return cboRegstrStatus;
    }

    /**
     * Setter for property cboRegstrStatus.
     *
     * @param cboRegstrStatus New value of property cboRegstrStatus.
     */
    public void setCboRegstrStatus(String cboRegstrStatus) {
        this.cboRegstrStatus = cboRegstrStatus;
    }

    /**
     * Getter for property txtStatusBy.
     *
     * @return Value of property txtStatusBy.
     */
    public String getTxtStatusBy() {
        return txtStatusBy;
    }

    /**
     * Setter for property txtStatusBy.
     *
     * @param txtStatusBy New value of property txtStatusBy.
     */
    public void setTxtStatusBy(String txtStatusBy) {
        this.txtStatusBy = txtStatusBy;
    }

    /**
     * Getter for property txtStatus.
     *
     * @return Value of property txtStatus.
     */
    public String getTxtStatus() {
        return txtStatus;
    }

    /**
     * Setter for property txtStatus.
     *
     * @param txtStatus New value of property txtStatus.
     */
    public void setTxtStatus(String txtStatus) {
        this.txtStatus = txtStatus;
    }

    /**
     * Getter for property CreatedDt.
     *
     * @return Value of property CreatedDt.
     */
    public String getCreatedDt() {
        return CreatedDt;
    }

    /**
     * Setter for property CreatedDt.
     *
     * @param CreatedDt New value of property CreatedDt.
     */
    public void setCreatedDt(String CreatedDt) {
        this.CreatedDt = CreatedDt;
    }

    /**
     * Getter for property lblStatus.
     *
     * @return Value of property lblStatus.
     */
    public String getLblStatus() {
        return lblStatus;
    }

    /**
     * Setter for property lblStatus.
     *
     * @param lblStatus New value of property lblStatus.
     */
    public void setLblStatus(String lblStatus) {
        this.lblStatus = lblStatus;
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
     * Getter for property _authorizeMap.
     *
     * @return Value of property _authorizeMap.
     */
    public HashMap get_authorizeMap() {
        return _authorizeMap;
    }

    /**
     * Setter for property _authorizeMap.
     *
     * @param _authorizeMap New value of property _authorizeMap.
     */
    public void set_authorizeMap(HashMap _authorizeMap) {
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
     * Getter for property txtLoanAmt.
     *
     * @return Value of property txtLoanAmt.
     */
    public Double getTxtLoanAmt() {
        return txtLoanAmt;
    }

    /**
     * Setter for property txtLoanAmt.
     *
     * @param txtLoanAmt New value of property txtLoanAmt.
     */
    public void setTxtLoanAmt(Double txtLoanAmt) {
        this.txtLoanAmt = txtLoanAmt;
    }

    /**
     * Getter for property cbmSchemName.
     *
     * @return Value of property cbmSchemName.
     */
    public ComboBoxModel getCbmSchemName() {
        return cbmSchemName;
    }

    /**
     * Setter for property cbmSchemName.
     *
     * @param cbmSchemName New value of property cbmSchemName.
     */
    public void setCbmSchemName(ComboBoxModel cbmSchemName) {
        this.cbmSchemName = cbmSchemName;
    }

    /**
     * Getter for property chargelst.
     *
     * @return Value of property chargelst.
     */
    public List getChargelst() {
        return chargelst;
    }

    /**
     * Setter for property chargelst.
     *
     * @param chargelst New value of property chargelst.
     */
    public void setChargelst(List chargelst) {
        this.chargelst = chargelst;
    }

    /**
     * Getter for property transactionOB.
     *
     * @return Value of property transactionOB.
     */
    public TransactionOB getTransactionOB() {
        return transactionOB;
    }

    /**
     * Setter for property transactionOB.
     *
     * @param transactionOB New value of property transactionOB.
     */
    public void setTransactionOB(TransactionOB transactionOB) {
        this.transactionOB = transactionOB;
    }

    /**
     * Getter for property allowedTransactionDetailsTO.
     *
     * @return Value of property allowedTransactionDetailsTO.
     */
    public LinkedHashMap getAllowedTransactionDetailsTO() {
        return allowedTransactionDetailsTO;
    }

    /**
     * Setter for property allowedTransactionDetailsTO.
     *
     * @param allowedTransactionDetailsTO New value of property
     * allowedTransactionDetailsTO.
     */
    public void setAllowedTransactionDetailsTO(LinkedHashMap allowedTransactionDetailsTO) {
        this.allowedTransactionDetailsTO = allowedTransactionDetailsTO;
    }

    /**
     * Getter for property currDt.
     *
     * @return Value of property currDt.
     */
    public Date getCurrDt() {
        return currDt;
    }

    /**
     * Setter for property currDt.
     *
     * @param currDt New value of property currDt.
     */
    public void setCurrDt(Date currDt) {
        this.currDt = currDt;
    }

    /**
     * Getter for property authMap.
     *
     * @return Value of property authMap.
     */
    public HashMap getAuthMap() {
        return authMap;
    }

    /**
     * Setter for property authMap.
     *
     * @param authMap New value of property authMap.
     */
    public void setAuthMap(HashMap authMap) {
        this.authMap = authMap;
    }

    /**
     * Getter for property deletedTransactionDetailsTO.
     *
     * @return Value of property deletedTransactionDetailsTO.
     */
    public LinkedHashMap getDeletedTransactionDetailsTO() {
        return deletedTransactionDetailsTO;
    }

    /**
     * Setter for property deletedTransactionDetailsTO.
     *
     * @param deletedTransactionDetailsTO New value of property
     * deletedTransactionDetailsTO.
     */
    public void setDeletedTransactionDetailsTO(LinkedHashMap deletedTransactionDetailsTO) {
        this.deletedTransactionDetailsTO = deletedTransactionDetailsTO;
    }

    /**
     * Getter for property newTransactionMap.
     *
     * @return Value of property newTransactionMap.
     */
    public HashMap getNewTransactionMap() {
        return newTransactionMap;
    }

    /**
     * Setter for property newTransactionMap.
     *
     * @param newTransactionMap New value of property newTransactionMap.
     */
    public void setNewTransactionMap(HashMap newTransactionMap) {
        this.newTransactionMap = newTransactionMap;
    }

    /**
     * Getter for property txtProdId.
     *
     * @return Value of property txtProdId.
     */
    public String getTxtProdId() {
        return txtProdId;
    }

    /**
     * Setter for property txtProdId.
     *
     * @param txtProdId New value of property txtProdId.
     */
    public void setTxtProdId(String txtProdId) {
        this.txtProdId = txtProdId;
    }

    public boolean isVehicleTypeData() {
        return vehicleTypeData;
    }

    public void setVehicleTypeData(boolean vehicleTypeData) {
        this.vehicleTypeData = vehicleTypeData;
    }
    

    /**
     * Getter for property memberTypeData.
     *
     * @return Value of property memberTypeData.
     */
    public boolean isMemberTypeData() {
        return memberTypeData;
    }

    /**
     * Setter for property memberTypeData.
     *
     * @param memberTypeData New value of property memberTypeData.
     */
    public void setMemberTypeData(boolean memberTypeData) {
        this.memberTypeData = memberTypeData;
    }

    public EnhancedTableModel getTblVehicleTypeDetails() {
        return tblVehicleTypeDetails;
    }

    public void setTblVehicleTypeDetails(EnhancedTableModel tblVehicleTypeDetails) {
        this.tblVehicleTypeDetails = tblVehicleTypeDetails;
    }
    
    /**
     * Getter for property tblMemberTypeDetails.
     *
     * @return Value of property tblMemberTypeDetails.
     */
    public EnhancedTableModel getTblMemberTypeDetails() {
        return tblMemberTypeDetails;
    }

    /**
     * Setter for property tblMemberTypeDetails.
     *
     * @param tblMemberTypeDetails New value of property tblMemberTypeDetails.
     */
    public void setTblMemberTypeDetails(EnhancedTableModel tblMemberTypeDetails) {
        this.tblMemberTypeDetails = tblMemberTypeDetails;
    }

    /**
     * Getter for property collateralTypeData.
     *
     * @return Value of property collateralTypeData.
     */
    public boolean isCollateralTypeData() {
        return collateralTypeData;
    }

    /**
     * Setter for property collateralTypeData.
     *
     * @param collateralTypeData New value of property collateralTypeData.
     */
    public void setCollateralTypeData(boolean collateralTypeData) {
        this.collateralTypeData = collateralTypeData;
    }

    /**
     * Getter for property tblCollateralDetails.
     *
     * @return Value of property tblCollateralDetails.
     */
    public EnhancedTableModel getTblCollateralDetails() {
        return tblCollateralDetails;
    }

    /**
     * Setter for property tblCollateralDetails.
     *
     * @param tblCollateralDetails New value of property tblCollateralDetails.
     */
    public void setTblCollateralDetails(EnhancedTableModel tblCollateralDetails) {
        this.tblCollateralDetails = tblCollateralDetails;
    }

    public Double getTxtCostOfVehicle() {
        return txtCostOfVehicle;
    }

    public void setTxtCostOfVehicle(Double txtCostOfVehicle) {
        this.txtCostOfVehicle = txtCostOfVehicle;
    }

    public Double getTxtVehicleNetworth() {
        return txtVehicleNetworth;
    }

    public void setTxtVehicleNetworth(Double txtVehicleNetworth) {
        this.txtVehicleNetworth = txtVehicleNetworth;
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
  
    
    public String getTxtVehicleMemNo() {
        return txtVehicleMemNo;
    }

    public void setTxtVehicleMemNo(String txtVehicleMemNo) {
        this.txtVehicleMemNo = txtVehicleMemNo;
    }

    /**
     * Getter for property txtOwnerMemNo.
     *
     * @return Value of property txtOwnerMemNo.
     */
    public String getTxtOwnerMemNo() {
        return txtOwnerMemNo;
    }

    /**
     * Setter for property txtOwnerMemNo.
     *
     * @param txtOwnerMemNo New value of property txtOwnerMemNo.
     */
    public void setTxtOwnerMemNo(String txtOwnerMemNo) {
        this.txtOwnerMemNo = txtOwnerMemNo;
    }

    /**
     * Getter for property txtMemNo.
     *
     * @return Value of property txtMemNo.
     */
    public String getTxtMemNo() {
        return txtMemNo;
    }

    /**
     * Setter for property txtMemNo.
     *
     * @param txtMemNo New value of property txtMemNo.
     */
    public void setTxtMemNo(String txtMemNo) {
        this.txtMemNo = txtMemNo;
    }

    public String getTxtMemberTotalSalary() {
        return txtMemberTotalSalary;
    }

    public void setTxtMemberTotalSalary(String txtMemberTotalSalary) {
        this.txtMemberTotalSalary = txtMemberTotalSalary;
    }
    /**
     * Getter for property txtMemNetworth.
     *
     * @return Value of property txtMemNetworth.
     */
    public String getTxtMemNetworth() {
        return txtMemNetworth;
    }
    /**
     * Setter for property txtMemNetworth.
     *
     * @param txtMemNetworth New value of property txtMemNetworth.
     */
    public void setTxtMemNetworth(String txtMemNetworth) {
        this.txtMemNetworth = txtMemNetworth;
    }

    public String getTxtMemPriority() {
        return txtMemPriority;
    }

    public void setTxtMemPriority(String txtMemPriority) {
        this.txtMemPriority = txtMemPriority;
    }

    /**
     * Getter for property txtContactNum.
     *
     * @return Value of property txtContactNum.
     */
    public String getTxtContactNum() {
        return txtContactNum;
    }

    /**
     * Setter for property txtContactNum.
     *
     * @param txtContactNum New value of property txtContactNum.
     */
    public void setTxtContactNum(String txtContactNum) {
        this.txtContactNum = txtContactNum;
    }

    /**
     * Getter for property txtMemType.
     *
     * @return Value of property txtMemType.
     */
    public String getTxtMemType() {
        return txtMemType;
    }

    /**
     * Setter for property txtMemType.
     *
     * @param txtMemType New value of property txtMemType.
     */
    public void setTxtMemType(String txtMemType) {
        this.txtMemType = txtMemType;
    }

    /**
     * Getter for property txtSalaryCertificateNo.
     *
     * @return Value of property txtSalaryCertificateNo.
     */
    public String getTxtSalaryCertificateNo() {
        return txtSalaryCertificateNo;
    }

    /**
     * Setter for property txtSalaryCertificateNo.
     *
     * @param txtSalaryCertificateNo New value of property
     * txtSalaryCertificateNo.
     */
    public void setTxtSalaryCertificateNo(String txtSalaryCertificateNo) {
        this.txtSalaryCertificateNo = txtSalaryCertificateNo;
    }

    /**
     * Getter for property txtEmployerName.
     *
     * @return Value of property txtEmployerName.
     */
    public String getTxtEmployerName() {
        return txtEmployerName;
    }

    /**
     * Setter for property txtEmployerName.
     *
     * @param txtEmployerName New value of property txtEmployerName.
     */
    public void setTxtEmployerName(String txtEmployerName) {
        this.txtEmployerName = txtEmployerName;
    }

    /**
     * Getter for property txtAddress.
     *
     * @return Value of property txtAddress.
     */
    public String getTxtAddress() {
        return txtAddress;
    }

    /**
     * Setter for property txtAddress.
     *
     * @param txtAddress New value of property txtAddress.
     */
    public void setTxtAddress(String txtAddress) {
        this.txtAddress = txtAddress;
    }

    /**
     * Getter for property cboSecurityCity.
     *
     * @return Value of property cboSecurityCity.
     */
    public String getCboSecurityCity() {
        return cboSecurityCity;
    }

    /**
     * Setter for property cboSecurityCity.
     *
     * @param cboSecurityCity New value of property cboSecurityCity.
     */
    public void setCboSecurityCity(String cboSecurityCity) {
        this.cboSecurityCity = cboSecurityCity;
    }

    /**
     * Getter for property txtPinCode.
     *
     * @return Value of property txtPinCode.
     */
    public String getTxtPinCode() {
        return txtPinCode;
    }

    /**
     * Setter for property txtPinCode.
     *
     * @param txtPinCode New value of property txtPinCode.
     */
    public void setTxtPinCode(String txtPinCode) {
        this.txtPinCode = txtPinCode;
    }

    /**
     * Getter for property txtDesignation.
     *
     * @return Value of property txtDesignation.
     */
    public String getTxtDesignation() {
        return txtDesignation;
    }

    /**
     * Setter for property txtDesignation.
     *
     * @param txtDesignation New value of property txtDesignation.
     */
    public void setTxtDesignation(String txtDesignation) {
        this.txtDesignation = txtDesignation;
    }

    /**
     * Getter for property txtContactNo.
     *
     * @return Value of property txtContactNo.
     */
    public String getTxtContactNo() {
        return txtContactNo;
    }

    /**
     * Setter for property txtContactNo.
     *
     * @param txtContactNo New value of property txtContactNo.
     */
    public void setTxtContactNo(String txtContactNo) {
        this.txtContactNo = txtContactNo;
    }

    /**
     * Getter for property tdtRetirementDt.
     *
     * @return Value of property tdtRetirementDt.
     */
    public String getTdtRetirementDt() {
        return tdtRetirementDt;
    }

    /**
     * Setter for property tdtRetirementDt.
     *
     * @param tdtRetirementDt New value of property tdtRetirementDt.
     */
    public void setTdtRetirementDt(String tdtRetirementDt) {
        this.tdtRetirementDt = tdtRetirementDt;
    }

    /**
     * Getter for property txtMemberNum.
     *
     * @return Value of property txtMemberNum.
     */
    public String getTxtMemberNum() {
        return txtMemberNum;
    }

    /**
     * Setter for property txtMemberNum.
     *
     * @param txtMemberNum New value of property txtMemberNum.
     */
    public void setTxtMemberNum(String txtMemberNum) {
        this.txtMemberNum = txtMemberNum;
    }

    /**
     * Getter for property txtTotalSalary.
     *
     * @return Value of property txtTotalSalary.
     */
    public String getTxtTotalSalary() {
        return txtTotalSalary;
    }

    /**
     * Setter for property txtTotalSalary.
     *
     * @param txtTotalSalary New value of property txtTotalSalary.
     */
    public void setTxtTotalSalary(String txtTotalSalary) {
        this.txtTotalSalary = txtTotalSalary;
    }

    /**
     * Getter for property txtNetWorth.
     *
     * @return Value of property txtNetWorth.
     */
    public String getTxtNetWorth() {
        return txtNetWorth;
    }

    /**
     * Setter for property txtNetWorth.
     *
     * @param txtNetWorth New value of property txtNetWorth.
     */
    public void setTxtNetWorth(String txtNetWorth) {
        this.txtNetWorth = txtNetWorth;
    }

    /**
     * Getter for property txtSalaryRemark.
     *
     * @return Value of property txtSalaryRemark.
     */
    public String getTxtSalaryRemark() {
        return txtSalaryRemark;
    }

    /**
     * Setter for property txtSalaryRemark.
     *
     * @param txtSalaryRemark New value of property txtSalaryRemark.
     */
    public void setTxtSalaryRemark(String txtSalaryRemark) {
        this.txtSalaryRemark = txtSalaryRemark;
    }

    /**
     * Getter for property txtOwnerMemberNname.
     *
     * @return Value of property txtOwnerMemberNname.
     */
    public String getTxtOwnerMemberNname() {
        return txtOwnerMemberNname;
    }

    /**
     * Setter for property txtOwnerMemberNname.
     *
     * @param txtOwnerMemberNname New value of property txtOwnerMemberNname.
     */
    public void setTxtOwnerMemberNname(String txtOwnerMemberNname) {
        this.txtOwnerMemberNname = txtOwnerMemberNname;
    }

    /**
     * Getter for property txtDocumentNo.
     *
     * @return Value of property txtDocumentNo.
     */
    public String getTxtDocumentNo() {
        return txtDocumentNo;
    }

    /**
     * Setter for property txtDocumentNo.
     *
     * @param txtDocumentNo New value of property txtDocumentNo.
     */
    public void setTxtDocumentNo(String txtDocumentNo) {
        this.txtDocumentNo = txtDocumentNo;
    }

    /**
     * Getter for property txtDocumentType.
     *
     * @return Value of property txtDocumentType.
     */
    public String getTxtDocumentType() {
        return txtDocumentType;
    }

    /**
     * Setter for property txtDocumentType.
     *
     * @param txtDocumentType New value of property txtDocumentType.
     */
    public void setTxtDocumentType(String txtDocumentType) {
        this.txtDocumentType = txtDocumentType;
    }

    /**
     * Getter for property tdtDocumentDate.
     *
     * @return Value of property tdtDocumentDate.
     */
    public String getTdtDocumentDate() {
        return tdtDocumentDate;
    }

    /**
     * Setter for property tdtDocumentDate.
     *
     * @param tdtDocumentDate New value of property tdtDocumentDate.
     */
    public void setTdtDocumentDate(String tdtDocumentDate) {
        this.tdtDocumentDate = tdtDocumentDate;
    }

    /**
     * Getter for property txtRegisteredOffice.
     *
     * @return Value of property txtRegisteredOffice.
     */
    public String getTxtRegisteredOffice() {
        return txtRegisteredOffice;
    }

    /**
     * Setter for property txtRegisteredOffice.
     *
     * @param txtRegisteredOffice New value of property txtRegisteredOffice.
     */
    public void setTxtRegisteredOffice(String txtRegisteredOffice) {
        this.txtRegisteredOffice = txtRegisteredOffice;
    }

    /**
     * Getter for property cboPledge.
     *
     * @return Value of property cboPledge.
     */
    public String getCboPledge() {
        return cboPledge;
    }

    /**
     * Setter for property cboPledge.
     *
     * @param cboPledge New value of property cboPledge.
     */
    public void setCboPledge(String cboPledge) {
        this.cboPledge = cboPledge;
    }

    /**
     * Getter for property tdtPledgeDate.
     *
     * @return Value of property tdtPledgeDate.
     */
    public String getTdtPledgeDate() {
        return tdtPledgeDate;
    }

    /**
     * Setter for property tdtPledgeDate.
     *
     * @param tdtPledgeDate New value of property tdtPledgeDate.
     */
    public void setTdtPledgeDate(String tdtPledgeDate) {
        this.tdtPledgeDate = tdtPledgeDate;
    }

    /**
     * Getter for property txtPledgeNo.
     *
     * @return Value of property txtPledgeNo.
     */
    public String getTxtPledgeNo() {
        return txtPledgeNo;
    }

    /**
     * Setter for property txtPledgeNo.
     *
     * @param txtPledgeNo New value of property txtPledgeNo.
     */
    public void setTxtPledgeNo(String txtPledgeNo) {
        this.txtPledgeNo = txtPledgeNo;
    }

    /**
     * Getter for property txtPledgeAmount.
     *
     * @return Value of property txtPledgeAmount.
     */
    public String getTxtPledgeAmount() {
        return txtPledgeAmount;
    }

    /**
     * Setter for property txtPledgeAmount.
     *
     * @param txtPledgeAmount New value of property txtPledgeAmount.
     */
    public void setTxtPledgeAmount(String txtPledgeAmount) {
        this.txtPledgeAmount = txtPledgeAmount;
    }

    /**
     * Getter for property txtVillage.
     *
     * @return Value of property txtVillage.
     */
    public String getTxtVillage() {
        return txtVillage;
    }

    /**
     * Setter for property txtVillage.
     *
     * @param txtVillage New value of property txtVillage.
     */
    public void setTxtVillage(String txtVillage) {
        this.txtVillage = txtVillage;
    }

    /**
     * Getter for property txtSurveyNo.
     *
     * @return Value of property txtSurveyNo.
     */
    public String getTxtSurveyNo() {
        return txtSurveyNo;
    }

    /**
     * Setter for property txtSurveyNo.
     *
     * @param txtSurveyNo New value of property txtSurveyNo.
     */
    public void setTxtSurveyNo(String txtSurveyNo) {
        this.txtSurveyNo = txtSurveyNo;
    }

    /**
     * Getter for property txtTotalArea.
     *
     * @return Value of property txtTotalArea.
     */
    public String getTxtTotalArea() {
        return txtTotalArea;
    }

    /**
     * Setter for property txtTotalArea.
     *
     * @param txtTotalArea New value of property txtTotalArea.
     */
    public void setTxtTotalArea(String txtTotalArea) {
        this.txtTotalArea = txtTotalArea;
    }

    /**
     * Getter for property cboNature.
     *
     * @return Value of property cboNature.
     */
    public String getCboNature() {
        return cboNature;
    }

    /**
     * Setter for property cboNature.
     *
     * @param cboNature New value of property cboNature.
     */
    public void setCboNature(String cboNature) {
        this.cboNature = cboNature;
    }

    /**
     * Getter for property cboRight.
     *
     * @return Value of property cboRight.
     */
    public String getCboRight() {
        return cboRight;
    }

    /**
     * Setter for property cboRight.
     *
     * @param cboRight New value of property cboRight.
     */
    public void setCboRight(String cboRight) {
        this.cboRight = cboRight;
    }

    /**
     * Getter for property cboDocumentType.
     *
     * @return Value of property cboDocumentType.
     */
    public String getCboDocumentType() {
        return cboDocumentType;
    }

    /**
     * Setter for property cboDocumentType.
     *
     * @param cboDocumentType New value of property cboDocumentType.
     */
    public void setCboDocumentType(String cboDocumentType) {
        this.cboDocumentType = cboDocumentType;
    }

    /**
     * Getter for property txtAreaParticular.
     *
     * @return Value of property txtAreaParticular.
     */
    public String getTxtAreaParticular() {
        return txtAreaParticular;
    }

    /**
     * Setter for property txtAreaParticular.
     *
     * @param txtAreaParticular New value of property txtAreaParticular.
     */
    public void setTxtAreaParticular(String txtAreaParticular) {
        this.txtAreaParticular = txtAreaParticular;
    }

    /**
     * Getter for property txtPledgeType.
     *
     * @return Value of property txtPledgeType.
     */
    public String getTxtPledgeType() {
        return txtPledgeType;
    }

    /**
     * Setter for property txtPledgeType.
     *
     * @param txtPledgeType New value of property txtPledgeType.
     */
    public void setTxtPledgeType(String txtPledgeType) {
        this.txtPledgeType = txtPledgeType;
    }

    /**
     * Getter for property rdoGahanYes.
     *
     * @return Value of property rdoGahanYes.
     */
    public boolean isRdoGahanYes() {
        return rdoGahanYes;
    }

    /**
     * Setter for property rdoGahanYes.
     *
     * @param rdoGahanYes New value of property rdoGahanYes.
     */
    public void setRdoGahanYes(boolean rdoGahanYes) {
        this.rdoGahanYes = rdoGahanYes;
    }

    /**
     * Getter for property rdoGahanNo.
     *
     * @return Value of property rdoGahanNo.
     */
    public boolean isRdoGahanNo() {
        return rdoGahanNo;
    }

    /**
     * Setter for property rdoGahanNo.
     *
     * @param rdoGahanNo New value of property rdoGahanNo.
     */
    public void setRdoGahanNo(boolean rdoGahanNo) {
        this.rdoGahanNo = rdoGahanNo;
    }

    /**
     * Getter for property tblJointCollateral.
     *
     * @return Value of property tblJointCollateral.
     */
    public EnhancedTableModel getTblJointCollateral() {
        return tblJointCollateral;
    }

    /**
     * Setter for property tblJointCollateral.
     *
     * @param tblJointCollateral New value of property tblJointCollateral.
     */
    public void setTblJointCollateral(EnhancedTableModel tblJointCollateral) {
        this.tblJointCollateral = tblJointCollateral;
    }

    /**
     * Getter for property collateralMap.
     *
     * @return Value of property collateralMap.
     */
    public LinkedHashMap getCollateralMap() {
        return collateralMap;
    }

    /**
     * Setter for property collateralMap.
     *
     * @param collateralMap New value of property collateralMap.
     */
    public void setCollateralMap(LinkedHashMap collateralMap) {
        this.collateralMap = collateralMap;
    }

    /**
     * Getter for property tblCaseDetails.
     *
     * @return Value of property tblCaseDetails.
     */
    public EnhancedTableModel getTblCaseDetails() {
        return tblCaseDetails;
    }

    /**
     * Setter for property tblCaseDetails.
     *
     * @param tblCaseDetails New value of property tblCaseDetails.
     */
    public void setTblCaseDetails(EnhancedTableModel tblCaseDetails) {
        this.tblCaseDetails = tblCaseDetails;
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

    /**
     * Getter for property productCategory.
     *
     * @return Value of property productCategory.
     */
    public String getProductCategory() {
        return productCategory;
    }

    /**
     * Setter for property productCategory.
     *
     * @param productCategory New value of property productCategory.
     */
    public void setProductCategory(String productCategory) {
        this.productCategory = productCategory;
    }

    /**
     * Getter for property keyValueForPaddyAndMDSLoans.
     *
     * @return Value of property keyValueForPaddyAndMDSLoans.
     */
    public String getKeyValueForPaddyAndMDSLoans() {
        return keyValueForPaddyAndMDSLoans;
    }

    /**
     * Setter for property keyValueForPaddyAndMDSLoans.
     *
     * @param keyValueForPaddyAndMDSLoans New value of property
     * keyValueForPaddyAndMDSLoans.
     */
    public void setKeyValueForPaddyAndMDSLoans(String keyValueForPaddyAndMDSLoans) {
        this.keyValueForPaddyAndMDSLoans = keyValueForPaddyAndMDSLoans;
    }

    /**
     * Getter for property paddyMap.
     *
     * @return Value of property paddyMap.
     */
    public Map getPaddyMap() {
        return paddyMap;
    }

    /**
     * Setter for property paddyMap.
     *
     * @param paddyMap New value of property paddyMap.
     */
    public void setPaddyMap(Map paddyMap) {
        this.paddyMap = paddyMap;
    }

    /**
     * Getter for property mdsMap.
     *
     * @return Value of property mdsMap.
     */
    public Map getMdsMap() {
        return mdsMap;
    }

    /**
     * Setter for property mdsMap.
     *
     * @param mdsMap New value of property mdsMap.
     */
    public void setMdsMap(Map mdsMap) {
        this.mdsMap = mdsMap;
    }

    /**
     * Getter for property pledgeValMap.
     *
     * @return Value of property pledgeValMap.
     */
    public HashMap getPledgeValMap() {
        return pledgeValMap;
    }

    /**
     * Setter for property pledgeValMap.
     *
     * @param pledgeValMap New value of property pledgeValMap.
     */
    public void setPledgeValMap(HashMap pledgeValMap) {
        this.pledgeValMap = pledgeValMap;
    }

    /**
     * Getter for property depositTypeData.
     *
     * @return Value of property depositTypeData.
     */
    public boolean isDepositTypeData() {
        return depositTypeData;
    }

    /**
     * Setter for property depositTypeData.
     *
     * @param depositTypeData New value of property depositTypeData.
     */
    public void setDepositTypeData(boolean depositTypeData) {
        this.depositTypeData = depositTypeData;
    }

    /**
     * Getter for property tblDepositTypeDetails.
     *
     * @return Value of property tblDepositTypeDetails.
     */
    public EnhancedTableModel getTblDepositTypeDetails() {
        return tblDepositTypeDetails;
    }

    /**
     * Setter for property tblDepositTypeDetails.
     *
     * @param tblDepositTypeDetails New value of property tblDepositTypeDetails.
     */
    public void setTblDepositTypeDetails(EnhancedTableModel tblDepositTypeDetails) {
        this.tblDepositTypeDetails = tblDepositTypeDetails;
    }

    /**
     * Getter for property cbmProdTypeSecurity.
     *
     * @return Value of property cbmProdTypeSecurity.
     */
    public ComboBoxModel getCbmProdTypeSecurity() {
        return cbmProdTypeSecurity;
    }

    /**
     * Setter for property cbmProdTypeSecurity.
     *
     * @param cbmProdTypeSecurity New value of property cbmProdTypeSecurity.
     */
    public void setCbmProdTypeSecurity(ComboBoxModel cbmProdTypeSecurity) {
        this.cbmProdTypeSecurity = cbmProdTypeSecurity;
    }

    /**
     * Getter for property cbmDepProdID.
     *
     * @return Value of property cbmDepProdID.
     */
    public ComboBoxModel getCbmDepProdID() {
        return cbmDepProdID;
    }

    /**
     * Setter for property cbmDepProdID.
     *
     * @param cbmDepProdID New value of property cbmDepProdID.
     */
    public void setCbmDepProdID(ComboBoxModel cbmDepProdID) {
        this.cbmDepProdID = cbmDepProdID;
    }

    /**
     * Getter for property losTypeData.
     *
     * @return Value of property losTypeData.
     */
    public boolean isLosTypeData() {
        return losTypeData;
    }

    /**
     * Setter for property losTypeData.
     *
     * @param losTypeData New value of property losTypeData.
     */
    public void setLosTypeData(boolean losTypeData) {
        this.losTypeData = losTypeData;
    }

    /**
     * Getter for property tblLosTypeDetails.
     *
     * @return Value of property tblLosTypeDetails.
     */
    public EnhancedTableModel getTblLosTypeDetails() {
        return tblLosTypeDetails;
    }

    /**
     * Setter for property tblLosTypeDetails.
     *
     * @param tblLosTypeDetails New value of property tblLosTypeDetails.
     */
    public void setTblLosTypeDetails(EnhancedTableModel tblLosTypeDetails) {
        this.tblLosTypeDetails = tblLosTypeDetails;
    }

    /**
     * Getter for property txtDepNo.
     *
     * @return Value of property txtDepNo.
     */
    public String getTxtDepNo() {
        return txtDepNo;
    }

    /**
     * Setter for property txtDepNo.
     *
     * @param txtDepNo New value of property txtDepNo.
     */
    public void setTxtDepNo(String txtDepNo) {
        this.txtDepNo = txtDepNo;
    }

    /**
     * Getter for property cboProductTypeSecurity.
     *
     * @return Value of property cboProductTypeSecurity.
     */
    public String getCboProductTypeSecurity() {
        return cboProductTypeSecurity;
    }

    /**
     * Setter for property cboProductTypeSecurity.
     *
     * @param cboProductTypeSecurity New value of property
     * cboProductTypeSecurity.
     */
    public void setCboProductTypeSecurity(String cboProductTypeSecurity) {
        this.cboProductTypeSecurity = cboProductTypeSecurity;
    }

    /**
     * Getter for property cboDepProdID.
     *
     * @return Value of property cboDepProdID.
     */
    public String getCboDepProdID() {
        return cboDepProdID;
    }

    /**
     * Setter for property cboDepProdID.
     *
     * @param cboDepProdID New value of property cboDepProdID.
     */
    public void setCboDepProdID(String cboDepProdID) {
        this.cboDepProdID = cboDepProdID;
    }

    /**
     * Getter for property tdtDepDt.
     *
     * @return Value of property tdtDepDt.
     */
    public String getTdtDepDt() {
        return tdtDepDt;
    }

    /**
     * Setter for property tdtDepDt.
     *
     * @param tdtDepDt New value of property tdtDepDt.
     */
    public void setTdtDepDt(String tdtDepDt) {
        this.tdtDepDt = tdtDepDt;
    }

    /**
     * Getter for property txtDepAmount.
     *
     * @return Value of property txtDepAmount.
     */
    public String getTxtDepAmount() {
        return txtDepAmount;
    }

    /**
     * Setter for property txtDepAmount.
     *
     * @param txtDepAmount New value of property txtDepAmount.
     */
    public void setTxtDepAmount(String txtDepAmount) {
        this.txtDepAmount = txtDepAmount;
    }

    /**
     * Getter for property txtMaturityDt.
     *
     * @return Value of property txtMaturityDt.
     */
    public String getTxtMaturityDt() {
        return txtMaturityDt;
    }

    /**
     * Setter for property txtMaturityDt.
     *
     * @param txtMaturityDt New value of property txtMaturityDt.
     */
    public void setTxtMaturityDt(String txtMaturityDt) {
        this.txtMaturityDt = txtMaturityDt;
    }

    /**
     * Getter for property txtMaturityValue.
     *
     * @return Value of property txtMaturityValue.
     */
    public String getTxtMaturityValue() {
        return txtMaturityValue;
    }

    /**
     * Setter for property txtMaturityValue.
     *
     * @param txtMaturityValue New value of property txtMaturityValue.
     */
    public void setTxtMaturityValue(String txtMaturityValue) {
        this.txtMaturityValue = txtMaturityValue;
    }

    /**
     * Getter for property txtRateOfInterest.
     *
     * @return Value of property txtRateOfInterest.
     */
    public String getTxtRateOfInterest() {
        return txtRateOfInterest;
    }

    /**
     * Setter for property txtRateOfInterest.
     *
     * @param txtRateOfInterest New value of property txtRateOfInterest.
     */
    public void setTxtRateOfInterest(String txtRateOfInterest) {
        this.txtRateOfInterest = txtRateOfInterest;
    }

    /**
     * Getter for property cboLosOtherInstitution.
     *
     * @return Value of property cboLosOtherInstitution.
     */
    public String getCboLosOtherInstitution() {
        return cboLosOtherInstitution;
    }

    /**
     * Setter for property cboLosOtherInstitution.
     *
     * @param cboLosOtherInstitution New value of property
     * cboLosOtherInstitution.
     */
    public void setCboLosOtherInstitution(String cboLosOtherInstitution) {
        this.cboLosOtherInstitution = cboLosOtherInstitution;
    }

    /**
     * Getter for property txtLosName.
     *
     * @return Value of property txtLosName.
     */
    public String getTxtLosName() {
        return txtLosName;
    }

    /**
     * Setter for property txtLosName.
     *
     * @param txtLosName New value of property txtLosName.
     */
    public void setTxtLosName(String txtLosName) {
        this.txtLosName = txtLosName;
    }

    /**
     * Getter for property cboLosSecurityType.
     *
     * @return Value of property cboLosSecurityType.
     */
    public String getCboLosSecurityType() {
        return cboLosSecurityType;
    }

    /**
     * Setter for property cboLosSecurityType.
     *
     * @param cboLosSecurityType New value of property cboLosSecurityType.
     */
    public void setCboLosSecurityType(String cboLosSecurityType) {
        this.cboLosSecurityType = cboLosSecurityType;
    }

    /**
     * Getter for property txtLosSecurityNo.
     *
     * @return Value of property txtLosSecurityNo.
     */
    public String getTxtLosSecurityNo() {
        return txtLosSecurityNo;
    }

    /**
     * Setter for property txtLosSecurityNo.
     *
     * @param txtLosSecurityNo New value of property txtLosSecurityNo.
     */
    public void setTxtLosSecurityNo(String txtLosSecurityNo) {
        this.txtLosSecurityNo = txtLosSecurityNo;
    }

    /**
     * Getter for property txtLosAmount.
     *
     * @return Value of property txtLosAmount.
     */
    public String getTxtLosAmount() {
        return txtLosAmount;
    }

    /**
     * Setter for property txtLosAmount.
     *
     * @param txtLosAmount New value of property txtLosAmount.
     */
    public void setTxtLosAmount(String txtLosAmount) {
        this.txtLosAmount = txtLosAmount;
    }

    /**
     * Getter for property tdtLosIssueDate.
     *
     * @return Value of property tdtLosIssueDate.
     */
    public String getTdtLosIssueDate() {
        return tdtLosIssueDate;
    }

    /**
     * Setter for property tdtLosIssueDate.
     *
     * @param tdtLosIssueDate New value of property tdtLosIssueDate.
     */
    public void setTdtLosIssueDate(String tdtLosIssueDate) {
        this.tdtLosIssueDate = tdtLosIssueDate;
    }

    /**
     * Getter for property tdtLosMaturityDt.
     *
     * @return Value of property tdtLosMaturityDt.
     */
    public String getTdtLosMaturityDt() {
        return tdtLosMaturityDt;
    }

    /**
     * Setter for property tdtLosMaturityDt.
     *
     * @param tdtLosMaturityDt New value of property tdtLosMaturityDt.
     */
    public void setTdtLosMaturityDt(String tdtLosMaturityDt) {
        this.tdtLosMaturityDt = tdtLosMaturityDt;
    }

    /**
     * Getter for property txtLosMaturityValue.
     *
     * @return Value of property txtLosMaturityValue.
     */
    public String getTxtLosMaturityValue() {
        return txtLosMaturityValue;
    }

    /**
     * Setter for property txtLosMaturityValue.
     *
     * @param txtLosMaturityValue New value of property txtLosMaturityValue.
     */
    public void setTxtLosMaturityValue(String txtLosMaturityValue) {
        this.txtLosMaturityValue = txtLosMaturityValue;
    }

    /**
     * Getter for property txtLosRemarks.
     *
     * @return Value of property txtLosRemarks.
     */
    public String getTxtLosRemarks() {
        return txtLosRemarks;
    }

    /**
     * Setter for property txtLosRemarks.
     *
     * @param txtLosRemarks New value of property txtLosRemarks.
     */
    public void setTxtLosRemarks(String txtLosRemarks) {
        this.txtLosRemarks = txtLosRemarks;
    }

    /**
     * Getter for property strACNumber.
     *
     * @return Value of property strACNumber.
     */
    public String getStrACNumber() {
        return strACNumber;
    }

    /**
     * Setter for property strACNumber.
     *
     * @param strACNumber New value of property strACNumber.
     */
    public void setStrACNumber(String strACNumber) {
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
        //this.strACNumber = strACNumber;
        this.strACNumber = strACNumber;
        setChanged();
    }

    /**
     * Getter for property docGenId.
     *
     * @return Value of property docGenId.
     */
    public String getDocGenId() {
        return docGenId;
    }

    /**
     * Setter for property docGenId.
     *
     * @param docGenId New value of property docGenId.
     */
    public void setDocGenId(String docGenId) {
        this.docGenId = docGenId;
    }

    /**
     * Getter for property borrowerNo.
     *
     * @return Value of property borrowerNo.
     */
    public String getBorrowerNo() {
        return borrowerNo;
    }

    /**
     * Setter for property borrowerNo.
     *
     * @param borrowerNo New value of property borrowerNo.
     */
    public void setBorrowerNo(String borrowerNo) {
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
     * Getter for property cbmLosInstitution.
     *
     * @return Value of property cbmLosInstitution.
     */
    public ComboBoxModel getCbmLosInstitution() {
        return cbmLosInstitution;
    }

    /**
     * Setter for property cbmLosInstitution.
     *
     * @param cbmLosInstitution New value of property cbmLosInstitution.
     */
    public void setCbmLosInstitution(ComboBoxModel cbmLosInstitution) {
        this.cbmLosInstitution = cbmLosInstitution;
    }

    /**
     * Getter for property cbmLosSecurityType.
     *
     * @return Value of property cbmLosSecurityType.
     */
    public ComboBoxModel getCbmLosSecurityType() {
        return cbmLosSecurityType;
    }

    /**
     * Setter for property cbmLosSecurityType.
     *
     * @param cbmLosSecurityType New value of property cbmLosSecurityType.
     */
    public void setCbmLosSecurityType(ComboBoxModel cbmLosSecurityType) {
        this.cbmLosSecurityType = cbmLosSecurityType;
    }

    /**
     * Getter for property tblSalarySecrityTable.
     *
     * @return Value of property tblSalarySecrityTable.
     */
    public EnhancedTableModel getTblSalarySecrityTable() {
        return tblSalarySecrityTable;
    }

    /**
     * Setter for property tblSalarySecrityTable.
     *
     * @param tblSalarySecrityTable New value of property tblSalarySecrityTable.
     */
    public void setTblSalarySecrityTable(EnhancedTableModel tblSalarySecrityTable) {
        this.tblSalarySecrityTable = tblSalarySecrityTable;
    }

    /**
     * Getter for property .
     *
     * @return Value of property cbmProdType.
     */
    public ComboBoxModel getCbmProdType() {
        return cbmProdType;
    }

    /**
     * Setter for property cbmProdType.
     *
     * @param cbmProdType New value of property cbmProdType.
     */
    public void setCbmProdType(ComboBoxModel cbmProdType) {
        this.cbmProdType = cbmProdType;
    }

    /**
     * Getter for property cbmProdId.
     *
     * @return Value of property cbmProdId.
     */
    public ComboBoxModel getCbmProdId() {
        return cbmProdId;
    }

    /**
     * Setter for property cbmProdId.
     *
     * @param cbmProdId New value of property cbmProdId.
     */
    public void setCbmProdId(ComboBoxModel cbmProdId) {
        this.cbmProdId = cbmProdId;
    }

    /**
     * Getter for property cbmSecurityCity.
     *
     * @return Value of property cbmSecurityCity.
     */
    public ComboBoxModel getCbmSecurityCity() {
        return cbmSecurityCity;
    }

    /**
     * Setter for property cbmSecurityCity.
     *
     * @param cbmSecurityCity New value of property cbmSecurityCity.
     */
    public void setCbmSecurityCity(ComboBoxModel cbmSecurityCity) {
        this.cbmSecurityCity = cbmSecurityCity;
    }

    /**
     * Getter for property cbmNature.
     *
     * @return Value of property cbmNature.
     */
    public ComboBoxModel getCbmNature() {
        return cbmNature;
    }

    /**
     * Setter for property cbmNature.
     *
     * @param cbmNature New value of property cbmNature.
     */
    public void setCbmNature(ComboBoxModel cbmNature) {
        this.cbmNature = cbmNature;
    }

    /**
     * Getter for property cbmRight.
     *
     * @return Value of property cbmRight.
     */
    public ComboBoxModel getCbmRight() {
        return cbmRight;
    }

    /**
     * Setter for property cbmRight.
     *
     * @param cbmRight New value of property cbmRight.
     */
    public void setCbmRight(ComboBoxModel cbmRight) {
        this.cbmRight = cbmRight;
    }

    /**
     * Getter for property cbmPledge.
     *
     * @return Value of property cbmPledge.
     */
    public ComboBoxModel getCbmPledge() {
        return cbmPledge;
    }

    /**
     * Setter for property cbmPledge.
     *
     * @param cbmPledge New value of property cbmPledge.
     */
    public void setCbmPledge(ComboBoxModel cbmPledge) {
        this.cbmPledge = cbmPledge;
    }

    /**
     * Getter for property cbmDocumentType.
     *
     * @return Value of property cbmDocumentType.
     */
    public ComboBoxModel getCbmDocumentType() {
        return cbmDocumentType;
    }

    /**
     * Setter for property cbmDocumentType.
     *
     * @param cbmDocumentType New value of property cbmDocumentType.
     */
    public void setCbmDocumentType(ComboBoxModel cbmDocumentType) {
        this.cbmDocumentType = cbmDocumentType;
    }

    /**
     * Getter for property txtJewelleryDetails.
     *
     * @return Value of property txtJewelleryDetails.
     */
    public String getTxtJewelleryDetails() {
        return txtJewelleryDetails;
    }

    /**
     * Setter for property txtJewelleryDetails.
     *
     * @param txtJewelleryDetails New value of property txtJewelleryDetails.
     */
    public void setTxtJewelleryDetails(String txtJewelleryDetails) {
        this.txtJewelleryDetails = txtJewelleryDetails;
    }

    /**
     * Getter for property txtGrossWeight.
     *
     * @return Value of property txtGrossWeight.
     */
    public String getTxtGrossWeight() {
        return txtGrossWeight;
    }

    /**
     * Setter for property txtGrossWeight.
     *
     * @param txtGrossWeight New value of property txtGrossWeight.
     */
    public void setTxtGrossWeight(String txtGrossWeight) {
        this.txtGrossWeight = txtGrossWeight;
    }

    /**
     * Getter for property txtNetWeight.
     *
     * @return Value of property txtNetWeight.
     */
    public String getTxtNetWeight() {
        return txtNetWeight;
    }

    /**
     * Setter for property txtNetWeight.
     *
     * @param txtNetWeight New value of property txtNetWeight.
     */
    public void setTxtNetWeight(String txtNetWeight) {
        this.txtNetWeight = txtNetWeight;
    }

    /**
     * Getter for property txtValueOfGold.
     *
     * @return Value of property txtValueOfGold.
     */
    public String getTxtValueOfGold() {
        return txtValueOfGold;
    }

    /**
     * Setter for property txtValueOfGold.
     *
     * @param txtValueOfGold New value of property txtValueOfGold.
     */
    public void setTxtValueOfGold(String txtValueOfGold) {
        this.txtValueOfGold = txtValueOfGold;
    }

    /**
     * Getter for property txtGoldRemarks.
     *
     * @return Value of property txtGoldRemarks.
     */
    public String getTxtGoldRemarks() {
        return txtGoldRemarks;
    }

    /**
     * Setter for property txtGoldRemarks.
     *
     * @param txtGoldRemarks New value of property txtGoldRemarks.
     */
    public void setTxtGoldRemarks(String txtGoldRemarks) {
        this.txtGoldRemarks = txtGoldRemarks;
    }

    /**
     * Getter for property txtMembName.
     *
     * @return Value of property txtMembName.
     */
    public String getTxtMembName() {
        return txtMembName;
    }

    /**
     * Setter for property txtMembName.
     *
     * @param txtMembName New value of property txtMembName.
     */
    public void setTxtMembName(String txtMembName) {
        this.txtMembName = txtMembName;
    }

    /**
     * Getter for property CbmRepayFreq_Repayment.
     *
     * @return Value of property CbmRepayFreq_Repayment.
     */
    public ComboBoxModel getCbmRepayFreq_Repayment() {
        return CbmRepayFreq_Repayment;
    }

    /**
     * Setter for property CbmRepayFreq_Repayment.
     *
     * @param CbmRepayFreq_Repayment New value of property
     * CbmRepayFreq_Repayment.
     */
    public void setCbmRepayFreq_Repayment(ComboBoxModel CbmRepayFreq_Repayment) {
        this.CbmRepayFreq_Repayment = CbmRepayFreq_Repayment;
    }

    /**
     * Getter for property CbmRepayType.
     *
     * @return Value of property CbmRepayType.
     */
    public ComboBoxModel getCbmRepayType() {
        return CbmRepayType;
    }

    /**
     * Setter for property CbmRepayType.
     *
     * @param CbmRepayType New value of property CbmRepayType.
     */
    public void setCbmRepayType(ComboBoxModel CbmRepayType) {
        this.CbmRepayType = CbmRepayType;
    }

    /**
     * Getter for property tdtFromDt.
     *
     * @return Value of property tdtFromDt.
     */
    public Date getTdtFromDt() {
        return tdtFromDt;
    }

    /**
     * Setter for property tdtFromDt.
     *
     * @param tdtFromDt New value of property tdtFromDt.
     */
    public void setTdtFromDt(Date tdtFromDt) {
        this.tdtFromDt = tdtFromDt;
    }

    /**
     * Getter for property tdtDueDt.
     *
     * @return Value of property tdtDueDt.
     */
    public java.util.Date getTdtDueDt() {
        return tdtDueDt;
    }

    /**
     * Setter for property tdtDueDt.
     *
     * @param tdtDueDt New value of property tdtDueDt.
     */
    public void setTdtDueDt(java.util.Date tdtDueDt) {
        this.tdtDueDt = tdtDueDt;
    }

    /**
     * Getter for property loanMap.
     *
     * @return Value of property loanMap.
     */
    public HashMap getLoanMap() {
        return loanMap;
    }

    /**
     * Setter for property loanMap.
     *
     * @param loanMap New value of property loanMap.
     */
    public void setLoanMap(HashMap loanMap) {
        this.loanMap = loanMap;
    }

    /**
     * Getter for property forLoanMap.
     *
     * @return Value of property forLoanMap.
     */
    public ComboBoxModel getCbmPurposeCode() {
        return cbmPurposeCode;
    }

    public void setCbmPurposeCode(ComboBoxModel cbmPurposeCode) {
        this.cbmPurposeCode = cbmPurposeCode;
    }

    public String getCboPurposeCode() {
        return cboPurposeCode;
    }

    public void setCboPurposeCode(String cboPurposeCode) {
        this.cboPurposeCode = cboPurposeCode;
    }

    public HashMap getServiceTax_Map() {
        return serviceTax_Map;
    }

    public void setServiceTax_Map(HashMap serviceTax_Map) {
        this.serviceTax_Map = serviceTax_Map;
    }

    public String getLblServiceTaxval() {
        return lblServiceTaxval;
    }

    public void setLblServiceTaxval(String lblServiceTaxval) {
        this.lblServiceTaxval = lblServiceTaxval;
    }
    private String roundOffAmt(String amtStr, String method) throws Exception {
        String amt = amtStr;
        DecimalFormat d = new DecimalFormat();
        d.setMaximumFractionDigits(2);
        d.setDecimalSeparatorAlwaysShown(true);
        if (amtStr != null && !amtStr.equals("")) {
            amtStr = d.parse(d.format(new Double(amtStr).doubleValue())).toString();
        }
        Rounding rd = new Rounding();
        int pos = amtStr.indexOf(".");
        long intPart = 0;
        long decPart = 0;
        if (pos >= 0) {
            intPart = new Long(amtStr.substring(0, pos)).longValue();
            decPart = new Long(amtStr.substring(pos + 1)).longValue();
        } else {
            if (amtStr != null && !amtStr.equals("")) {
                intPart = new Long(amtStr).longValue();
            }
        }
        if (method.equals("NEAREST_VALUE")) {
            decPart = rd.getNearest(decPart, 10);
            amtStr = intPart + "." + decPart;
        }
        return amtStr;
    }
}
