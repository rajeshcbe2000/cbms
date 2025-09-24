/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition.
 * 
 *
 * CustomerOB.java
 *
 * Created on August 14, 2003, 10:30 AM
 */
package com.see.truetransact.ui.customer;

import com.see.truetransact.clientexception.ClientParseException;
import com.see.truetransact.clientproxy.ProxyFactory;
import com.see.truetransact.clientproxy.ProxyParameters;
import com.see.truetransact.clientutil.*;
import com.see.truetransact.commonutil.CommonConstants;
import com.see.truetransact.commonutil.CommonUtil;
import com.see.truetransact.commonutil.DateUtil;
import com.see.truetransact.commonutil.Dummy;
import com.see.truetransact.transferobject.customer.*;
import com.see.truetransact.transferobject.deposit.JointAccntTO;
import com.see.truetransact.ui.TrueTransactMain;
import com.see.truetransact.ui.common.introducer.IntroducerOB;
import com.see.truetransact.ui.deposit.CommonRB;
import com.see.truetransact.uicomponent.CObservable;
import com.see.truetransact.uicomponent.COptionPane;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.*;
import oracle.jdbc.*;
import oracle.sql.BLOB;
import org.apache.log4j.Logger;
//import java.util.Set;

/**
 *
 * @author administrator Modified by Karthik Modified by Annamalai
 * @modified by JK
 */
public class CustomerOB extends CObservable {

    JointAcctHolderManipulation objJointAcctHolderManipulation = new JointAcctHolderManipulation();
    CommonRB objCommRB = new CommonRB();
    private String txtFirstName = "";
    private String txtMiddleName = "";
    private String txtLastName = "";
    private String txtTransPwd = "";
    private boolean rdoGender_Male = false;
    private boolean rdoGender_Female = false;
    private boolean rdoMaritalStatus_Single = false;
    private boolean rdoMaritalStatus_Married = false;
    private boolean rdoMaritalStatus_Widow = false;
    private boolean istrue = false;
    private String txtNationality = "";
    private String txtLanguage = "";
    private ComboBoxModel cbmTitle;
    private String cboTitle = "";
    private String tdtDateOfBirth = "";
    private String tdtRetireDt = "";
    private String txtCustomerID = "";
    private String txtRemarks = "";
    private String cboResidentialStatus;
    private ComboBoxModel cbmResidentialStatus;
    private String custTypeId;
    private String lblCreatedDt1;
    private String lblDealingPeriod;
    private String lblValCustomerName = "";
    private String lblValDateOfBirth = "";
    private String lblValStreet = "";
    private String lblValArea = "";
    private String lblValCity = "";
    private String lblValState = "";
    private String lblValCountry = "";
    private String lblValPin = "";
    private String chkClosed = "";
    private String lblValCorpCustDesig = "";
    private String DeletedRemarks = "";
    private String memberShipNo = "";
    private static int rowNum = 0;
    private String tdtjoiningDate = "";
//    private String StatusBy = "";
    public List ALL_ACCOUNT_LIST = null;
    //--- Declarations for Joint Account Table
    public LinkedHashMap jntAcctAll;
    HashMap jntAcctSingleRec;
    private EnhancedTableModel tblJointAccnt;
    private ArrayList tblJointAccntColTitle;
    private ArrayList jntAccntRow;
    LinkedHashMap jntAcctTOMap;
    private final String TBL_JNT_ACCNT_COLUMN_1 = "tblJntAccntColumn1";
    private final String TBL_JNT_ACCNT_COLUMN_2 = "tblJntAccntColumn2";
    private final String TBL_JNT_ACCNT_COLUMN_3 = "tblJntAccntColumn3";
    private final String TBL_JNT_ACCNT_COLUMN_4 = "tblJntAccntColumn4";
    private final String JOINT_ACCNT_FOR_DAO = "JointAccntTO";
    //--- End of Declarations for Joint Account Table
    //--- Declaration for DB Yes Or No
    private final String FLD_FOR_DB_YES_NO = "DBYesOrNo";
    private final String YES_FULL_STR = "Yes";
    private final String NO_FULL_STR = "No";
    //--- Declaration for Status
    private final String FLD_STATUS = "Status";
    private String lblPhoto;
    private String photoFile;
//    private File photoByteArray;
    private byte[] photoByteArray;
    private String lblSign = "";
    private String signFile;
//    private File signByteArray;
    private byte[] signByteArray;
    private String lblCustomerStatus = "";
    private boolean isMinor = false;
    private boolean isMinority=false;
    private boolean isStaff = false;
    private boolean isPassport = false;
    private boolean isIncomePar = false;
    private boolean isLandDetails = false;
    private String cboCustomerType;
    private ComboBoxModel cbmCustomerType;
    private String cboIntroType;
    private ComboBoxModel cbmIntroType;
    private ComboBoxModel cbmRelationManager;
    private String cboRelationManager;
    private String txtCompany = "";
    private String cboAnnualIncomeLevel;
    private ComboBoxModel cbmAnnualIncomeLevel;
    private ComboBoxModel cbmPrefCommunication;
    private String cboPrefCommunication;
    private ComboBoxModel cbmCustomerGroup;
    private String cboCustomerGroup;
    private ComboBoxModel cbmVehicle;
    private String cboVehicle;
    private ComboBoxModel cbmProfession;
    private String cboProfession;
    private ComboBoxModel cbmPrimaryOccupation;
    private String cboPrimaryOccupation;
    private String cboEducationalLevel;
    private ComboBoxModel cbmEducationalLevel;
    private String txtEmailID = "";
    private String txtNetWorth = "0";
    private String txtPincode = "";
    private String txtStreet = "";
    private ComboBoxModel cbmCountry;
    private String cboCountry;
    private String txtArea = "";
    private String cboAddressType;
    private ComboBoxModel cbmAddressType;
    private String cboCity;
    private ComboBoxModel cbmCity;
    private String cboState;
    private ComboBoxModel cbmState;
    public String txtPhoneNumber = "";
    private String txtAreaCode = "";
    private String cboPhoneType;
    private ComboBoxModel cbmPhoneType;
    private EnhancedTableModel tblProofList;
    private EnhancedTableModel tblContactList;
    private EnhancedTableModel tblPhoneList;
    private EnhancedTableModel tbmCustomerHistory;
    private String commAddrType = "";
    private String txtSsn = "";
    private String txtDesignation = "";
    private String txtPanNumber = "";
    private ComboBoxModel cbmSubCaste;
    private ComboBoxModel cbmCaste;
    private ComboBoxModel cbmReligion;
    private String cboCaste;
    private String cboSubCaste;
    private String cboReligion;
    //    private String txtCustUserId = "";
    private String txtCustPwd = "";
    private int actionType;
    private int result;
    private String lblStatus = ClientConstants.ACTION_STATUS[ClientConstants.ACTIONTYPE_CANCEL];
    private String txtAuthCustId = "";
    private String txtCustUserid = "";
    private String txtWebSite = "";
    private String txtAuthCapital = "";
    private String txtIssuedCapital = "";
    private String txtSubscribedCapital = "";
    private String txtTotalResource = "";
    private String txtLastYrPL = "";
    private String txtDividendPercentage = "";
    private String txtAddrRemarks = "";
    private String tdtFinacialYrEnd = "";
    private String txtTotalIncome = "";
    private String txtprofitBefTax = "";
    private String txtTotalNonTaxExp = "";
    private String txtTaxliability = "";
    private boolean chkAddrVerified = false;
    private boolean chkMinor = false;
    private boolean chkMinority=false;
    private boolean chkPhVerified = false;
    private boolean chkFinanceStmtVerified = false;
    private ArrayList IncVal = new ArrayList();
    private ArrayList LandDetailsVal = new ArrayList();
    private boolean chkIncomeDetails = false;
    private boolean chkLandDetails = false;
    private boolean rdoITDec_pan = false;
    private boolean rdoITDec_F60 = false;
    private boolean rdoITDec_F61 = false;
    private static CustomerOB objCustomerOB; // singleton object
//    private final CustomerRB objCustomerRB = new CustomerRB();
    private final static Logger log = Logger.getLogger(CustomerOB.class);
    private final static ClientParseException parseException = ClientParseException.getInstance();
    //for manipulating Address & phone data
    private HashMap addressMap;//Its a HashMap that has the Key, Value Pairs with Key as AddressType and the value as the CustomerAddressTO instance that holds the address Details
    private HashMap phoneMap;//Its a HashMap that has the Key, Value Pairs with Key as AddressType and the value as the phoneList HashMap which contains the address Details
    private HashMap phoneList;//Its a HashMap that has the  Key, Value Pairs with Key as Serial Number and the Value as the CustomerPhoneTO instance which has the phone Details
    private HashMap corpAuthCustMap; //Its a HashMap thas the details of AuthPersonsTO instance and Photo, sign file details. By Rajesh
    private HashMap map;
    private HashMap deletedAddressMap;//This HashMap is filled with addressDetails to be Deleted during the delete operation of particular customerid
    private HashMap deletedPhoneMap;//This HashMap is filled with phoneDetails to be Deleted during the delete operation
    private HashMap deletedProofMap;//This HashMap is filled with ProoofDetails to be Deleted during the delete operation
    private ProxyFactory proxy;
    private LinkedHashMap IncParMap;
    private HashMap deletedIncomeparMap;
    private LinkedHashMap LandDetMap;
    private HashMap deletedLandMap;
    private HashMap proofMap;
    //for filling dropdown
    private HashMap lookupMap;
    private HashMap param;
    private HashMap keyValue;
    private ArrayList key;
    private ArrayList value;
    private Date curDate = null;
    private int screenCustType;
    private final int INDIVIDUAL = 1;
    private final int CORPORATE = 2;
    private final String PRIMARY = "Primary";
    private final String EMPTY_STRING = "";
    private final int PROOFTYPE_COLNO=0;
    private final int ADDRTYPE_COLNO = 0;
    private final int INCPAR_COLNO = 0;
    private final int STATUS_COLNO = 1;
    private String cboCareOf = "";
    private String txtName = "";
    private String cboMembershipClass = "";
    private String cboCustStatus = "";
    private ComboBoxModel cbmCareOf;
    private ComboBoxModel cbmMembershipClass;
    private ComboBoxModel cbmCustStatus;
    //Fields of CustomerInfo
    private String tdtCrAvldSince = "";
    private String txtRiskRate = "";
    private String cboBusNature = "";
    private ComboBoxModel cbmBusNature;
    private String tdtDtEstablished = "";
    private String txtRegNumber = "";
    private String txtCEO = "";
    final String YES = "Y";
    final String NO = "N";
    final String SUSPENDED = "SUSPENDED";
    final String REVOKED = "REVOKED";
    String custId;
    private Connection conn;
    private Statement stmt;
    private ResultSet rset;
    private String cmd;
    private String dataBaseURL;
    private String userName;
    private String passWord;
    //To manipulate Customer data
    private CustomerTO objCustomerTO;
    private CustomerGaurdianTO objCustomerGaurdianTO;
    private CustomerPassPortTO objCustomerPassPortTO;
    private CustomerIncomeParticularsTO objCustomerIncomeParticularsTO;
    private CustomerLandDetailsTO objCustomerLandDetailsTO;
    private CustomerSuspensionTO objCustomerSuspensionTO;
    private final CustFinanceDetailsTO objCustFinanceDetailsTO = new CustFinanceDetailsTO();
    private CustRegionalTo objtCustRegionalTo = new CustRegionalTo();
    private HashMap data;
    public boolean addressTypeExists = false;
    public boolean proofTypeExists=false;
//    private CustomerRB resourceBundle = new CustomerRB();
    private java.util.ResourceBundle objCustomerRB = null;
    /**
     * Gaurdian Details Combobox models *
     */
    private ComboBoxModel cbmGCity;
    private ComboBoxModel cbmGCountry;
    private ComboBoxModel cbmGState;
    private ComboBoxModel cbmRelationNO;
    private String txtBranchId = "";
    private boolean chkSentThanksLetter = false;
    private boolean chkConfirmationfromIntroducer = false;
    private String txtStaffId = "";
    /**
     * Gaurdian Fields *
     */
    private String cboRelationNO = "";
    private String txtGuardianNameNO = "";
    private String txtGuardianACodeNO = "";
    private String txtGuardianPhoneNO = "";
    private String txtGStreet = "";
    private String txtGArea = "";
    private String cboGCountry = "";
    private String cboGState = "";
    private String cboGCity = "";
    private String txtGPinCode = "";
    private String tdtNetWorthAsOn = "";
    private boolean newGuardian = false;
    private boolean newAddress = false;
    private String txtGuardianAge = "";
    private boolean newIncomeDet = false;//To Check whether the NewButton for adding new incomeparticulras is clicked while Editing data
    /* ------------------------------------ **/
    private String cboAddrProof;
    private ComboBoxModel cbmAddrProof;
    private String cboIdenProof;
    private ComboBoxModel cbmIdenProof;
    /* For Passport details*/
    private String txtPassportFirstName = "";
    private String txtPassportMiddleName = "";
    private String txtPassportLastName = "";
    private String tdtPassportIssueDt = "";
    private String tdtPassportValidUpto = "";
    private String txtPassportNo = "";
    private String txtPassportIssueAuth = "";
    private ComboBoxModel cbmPassportTitle;
    private String cboPassportTitle = "";
    private HashMap  loanCustMap;
    private String  cbcomboAmsam="";
    private String cbcomboDesam="";
    private ComboBoxModel cbmcomboDesam;
     private ComboBoxModel cbmPassportIssuePlace;
    private ComboBoxModel  cbmcomboAmsam; 
    private String cboPassportIssuePlace;
    private ComboBoxModel cbmFarClass;
    private String cboFarClass;
    private String txtKartha = "";
    private String txtAge = "";
    private String txtRetireAge = "";
    private String bankruptcy = "";
    /**
     * Income Particulars Details  *
     */
    private String txtIncName = "";
    private String txtIncAmount = "";
    private ComboBoxModel cbmIncPack;
    private String cboIncPack;
    private ComboBoxModel cbmIncRelation;
    private String cboIncRelation;
    private EnhancedTableModel tblIncomeParticulars;
    private String txtSlNo = "";
    private ComboBoxModel cbmIncProfession;
    private String cboIncProfession;
    private String incCompany = "";
    private String mobileAppLoginStatus = "";
    /**
     * LAND Details  *
     */
    private EnhancedTableModel tblCustomerLandDetails;
    private String txtLocation = "";
    private String txtLandDetArea = "";
    private String txtSurNo = "";
    private String txtVillage = "";
    private String txtHobli = "";
    private String txtLandDetPin = "";
    private String txtPost = "";
    private ComboBoxModel cbmIrrigated;
    private String cboIrrigated;
    private ComboBoxModel cbmSourceIrrigated;
    private String cboSourceIrrigated;
    private ComboBoxModel cbmTaluk;
    private String cboTaluk;
    private ComboBoxModel cbmDistrict;
    private String cboDistrict;
    private ComboBoxModel cbmLandDetState;
    private String cboLandDetState;
    private String txtLandDetSlNo = "";
    private boolean newLandDet = false;
    private String tdtGuardianDOB = "";
    private String txtWardNo;
    /**
     * SUSPEND CUSTOMER DETAILS *
     */
    private boolean chksuspendedBy = false;
    private String suspendedDate = "";
    private String revokedDate = "";
    private boolean chkrevokedBy = false;
    private String susRevRemarks = "";
    private boolean isCustSuspended = false;
    private HashMap _authorizeMap;
    private ComboBoxModel cbmCustTaluk;
    private ComboBoxModel cbmCustKara;
    private ComboBoxModel cbmCustVillage;
    private ComboBoxModel CbmPostOffice;
    private String cboCustTaluk;
    private String cboCustKara;
    private String cboCustVillage;
    private String cboPostOffice;
    private String txtUniqueId;
    private boolean newProof = false;
    
    private String cboDivision = "";
    private String cboAgentCustId = "";
    private String lblAgentCustIdValue = "";

    public String getLblAgentCustIdValue() {
        return lblAgentCustIdValue;
    }

    public void setLblAgentCustIdValue(String lblAgentCustIdValue) {
        this.lblAgentCustIdValue = lblAgentCustIdValue;
    }
    public String getCboAgentCustId() {
        return cboAgentCustId;
    }

    public void setCboAgentCustId(String cboAgentCustId) {
        this.cboAgentCustId = cboAgentCustId;
    }
    private ComboBoxModel cbmDivision;
    private ComboBoxModel cbmAgentCustId;

    public com.see.truetransact.clientutil.ComboBoxModel getCbmAgentCustId() {
        return cbmAgentCustId;
    }

    public void setCbmAgentCustId(com.see.truetransact.clientutil.ComboBoxModel cbmAgentCustId) {
        this.cbmAgentCustId = cbmAgentCustId;
    }
     private String txtRegMName = "";
     private String txtRegMaHname = "";
     private String txtRegMaPlace = "";
     private String txtRegMavillage = "";
     private String txtRegMaTaluk = "";
     private String txtRegMaCity = "";
     private String txtRegMaCountry = "";
     private String txtRegMaState = "";
     private String txtRegMaAmsam = "";
     private String txtRegMaDesam = "";
     private String txtRegMaGardName = "";
     HashMap regionalData = new HashMap();
     private boolean chkRegioalLang = false;
     private String proofPhotoFile;
     private byte[] proofPhotoByteArray;
     
     // Added by nithya
     private String cboWardName = "";
     private ComboBoxModel cbmWardName;

    public ComboBoxModel getCbmPostOffice() {
        return CbmPostOffice;
    }

    public void setCbmPostOffice(ComboBoxModel CbmPostOffice) {
        this.CbmPostOffice = CbmPostOffice;
    }

    public String getCboPostOffice() {
        return cboPostOffice;
    }

    public void setCboPostOffice(String cboPostOffice) {
        this.cboPostOffice = cboPostOffice;
    }

    public boolean isChkRegioalLang() {
        return chkRegioalLang;
    }

    public void setChkRegioalLang(boolean chkRegioalLang) {
        this.chkRegioalLang = chkRegioalLang;
    }
     
    public HashMap getRegionalData() {
        return regionalData;
    }

    public void setRegionalData(HashMap regionalData) {
        this.regionalData = regionalData;
    }
      
     
    public String getTxtRegMName() {
        return txtRegMName;
    }

    public void setTxtRegMName(String txtRegMName) {
        this.txtRegMName = txtRegMName;
    }

    public String getTxtRegMaAmsam() {
        return txtRegMaAmsam;
    }

    public void setTxtRegMaAmsam(String txtRegMaAmsam) {
        this.txtRegMaAmsam = txtRegMaAmsam;
    }

    public String getTxtRegMaCity() {
        return txtRegMaCity;
    }

    public void setTxtRegMaCity(String txtRegMaCity) {
        this.txtRegMaCity = txtRegMaCity;
    }

    public String getTxtRegMaCountry() {
        return txtRegMaCountry;
    }

    public void setTxtRegMaCountry(String txtRegMaCountry) {
        this.txtRegMaCountry = txtRegMaCountry;
    }

    public String getTxtRegMaDesam() {
        return txtRegMaDesam;
    }

    public void setTxtRegMaDesam(String txtRegMaDesam) {
        this.txtRegMaDesam = txtRegMaDesam;
    }

    public String getTxtRegMaGardName() {
        return txtRegMaGardName;
    }

    public void setTxtRegMaGardName(String txtRegMaGardName) {
        this.txtRegMaGardName = txtRegMaGardName;
    }

    public String getTxtRegMaHname() {
        return txtRegMaHname;
    }

    public void setTxtRegMaHname(String txtRegMaHname) {
        this.txtRegMaHname = txtRegMaHname;
    }

    public String getTxtRegMaPlace() {
        return txtRegMaPlace;
    }

    public void setTxtRegMaPlace(String txtRegMaPlace) {
        this.txtRegMaPlace = txtRegMaPlace;
    }

    public String getTxtRegMaState() {
        return txtRegMaState;
    }

    public void setTxtRegMaState(String txtRegMaState) {
        this.txtRegMaState = txtRegMaState;
    }

    public String getTxtRegMaTaluk() {
        return txtRegMaTaluk;
    }

    public void setTxtRegMaTaluk(String txtRegMaTaluk) {
        this.txtRegMaTaluk = txtRegMaTaluk;
    }

    public String getTxtRegMavillage() {
        return txtRegMavillage;
    }

    public void setTxtRegMavillage(String txtRegMavillage) {
        this.txtRegMavillage = txtRegMavillage;
    }

    public String getTdtRetireDt() {
        return tdtRetireDt;
    }

    public void setTdtRetireDt(String tdtRetireDt) {
        this.tdtRetireDt = tdtRetireDt;
    }

    public String getTxtRetireAge() {
        return txtRetireAge;
    }

    public void setTxtRetireAge(String txtRetireAge) {
        this.txtRetireAge = txtRetireAge;
    }

    public ComboBoxModel getCbmDivision() {
        return cbmDivision;
    }

    public void setCbmDivision(ComboBoxModel cbmDivision) {
        this.cbmDivision = cbmDivision;
    }

    public String getCboDivision() {
        return cboDivision;
    }

    public void setCboDivision(String cboDivision) {
        this.cboDivision = cboDivision;
    }
       
    
    public EnhancedTableModel getTblProofList() {
        return tblProofList;
    }

    public void setTblProofList(EnhancedTableModel tblProofList) {
        this.tblProofList = tblProofList;
    }
    public boolean isNewProof() {
        return newProof;
    }

    public void setNewProof(boolean newProof) {
        this.newProof = newProof;
    }
   
   
    public String getTxtUniqueId() {
        return txtUniqueId;
    }

    public void setTxtUniqueId(String txtUniqueId) {
        this.txtUniqueId = txtUniqueId;
    }
    
    
    public boolean isChkMinority() {
        return chkMinority;
    }

    public void setChkMinority(boolean chkMinority) {
        this.chkMinority = chkMinority;
    }

    
    
    public boolean isIsMinority() {
        return isMinority;
    }

    public void setIsMinority(boolean isMinority) {
        this.isMinority = isMinority;
    }
    
    
    public ComboBoxModel getCbmSubCaste() {
        return cbmSubCaste;
    }

    public void setCbmSubCaste(ComboBoxModel cbmSubCaste) {
        this.cbmSubCaste = cbmSubCaste;
    }

    public String getCboSubCaste() {
        return cboSubCaste;
    }

    public void setCboSubCaste(String cboSubCaste) {
        this.cboSubCaste = cboSubCaste;
    }
    
    
    
    public HashMap getLoanCustMap() {
        return loanCustMap;
    }

    public void setLoanCustMap(HashMap loanCustMap) {
        this.loanCustMap = loanCustMap;
    }
    public String getCbcomboDesam() {
        return cbcomboDesam;
    }

    public void setCbcomboDesam(String cbcomboDesam) {
        this.cbcomboDesam = cbcomboDesam;
    }

    public com.see.truetransact.clientutil.ComboBoxModel getCbmcomboDesam() {
        return cbmcomboDesam;
    }

    public void setCbmcomboDesam(com.see.truetransact.clientutil.ComboBoxModel cbmcomboDesam) {
        this.cbmcomboDesam = cbmcomboDesam;
    }

    public String getCbcomboAmsam() {
        return cbcomboAmsam;
    }

    public void setCbcomboAmsam(String cbcomboAmsam) {
        this.cbcomboAmsam = cbcomboAmsam;
    }
   

    /**
     * Creates a new instance of CustomerOB If the parameter is '1' then the
     * customer type is INDIVIDUAL If the parameter is '2' then the customer
     * type is CUSTOMER
     */
    public CustomerOB(int param) {
        screenCustType = param;
        try {
            curDate = ClientUtil.getCurrentDate();
            proxy = ProxyFactory.createProxy();
            map = new HashMap();
            map.put(CommonConstants.JNDI, "CustomerJNDI");
            map.put(CommonConstants.HOME, "customer.CustomerHome");
            map.put(CommonConstants.REMOTE, "customer.Customer");
            objCustomerRB = java.util.ResourceBundle.getBundle("com.see.truetransact.ui.customer.CustomerRB");
//            if (param==1) {
//                objCustomerRB  = java.util.ResourceBundle.getBundle("com.see.truetransact.ui.customer.CustomerRB", new java.util.Locale(TrueTransactMain.language, TrueTransactMain.country));
//            } else {
//                objCustomerRB  = java.util.ResourceBundle.getBundle("com.see.truetransact.ui.customer.CorporateCustomerRB", ProxyParameters.LANGUAGE);
//            }
            setJntAcccntTblCol();
            tblJointAccnt = new EnhancedTableModel(null, tblJointAccntColTitle);
            createContactListTable();
            createProoofListTable();
            createPhoneListTable();
            createCustomerHistoryTable();
            createIncomeParticulars();
            createLandDetails();
            //            tblPhoneList.getColumn(
            fillDropdown();
        } catch (Exception e) {
            e.printStackTrace();
            parseException.logException(e, true);
        }
    }

    public CustomerOB() {
        curDate = ClientUtil.getCurrentDate();
    }

    public void refreshTableColumns(java.util.ResourceBundle rb) {
        try {
            objCustomerRB = rb;
            createContactListTable();
            createIncomeParticulars();
            createLandDetails();
            createPhoneListTable();
            createCustomerHistoryTable();
           createProoofListTable();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * To build Contacts table
     *
     * @throws Exception
     */
    private void createContactListTable() throws Exception {
        final ArrayList contactAddressColumn = new ArrayList();
        contactAddressColumn.add(objCustomerRB.getString("tblContactColumn1"));
        contactAddressColumn.add(objCustomerRB.getString("tblContactColumn2"));
        if (tblContactList != null && tblContactList.getDataArrayList() != null) {
            tblContactList.setColumnIdentifiers(contactAddressColumn);
        } else {
            tblContactList = new EnhancedTableModel(null, contactAddressColumn);
        }
    }
    
    
     private void createProoofListTable() throws Exception {
        final ArrayList proofColumn = new ArrayList();
        proofColumn.add(objCustomerRB.getString("tblProofColumn1"));
        proofColumn.add(objCustomerRB.getString("tblProofColumn2"));
        if (tblProofList != null && tblProofList.getDataArrayList() != null) {
            tblProofList.setColumnIdentifiers(proofColumn);
        } else {
            tblProofList = new EnhancedTableModel(null, proofColumn);
        }
    }

    private void createIncomeParticulars() throws Exception {
        final ArrayList IncomeParColumn = new ArrayList();
        IncomeParColumn.add(objCustomerRB.getString("tblIncPar1"));
        IncomeParColumn.add(objCustomerRB.getString("tblIncPar2"));
        IncomeParColumn.add(objCustomerRB.getString("tblIncPar3"));
        IncomeParColumn.add(objCustomerRB.getString("tblIncPar4"));
        IncomeParColumn.add(objCustomerRB.getString("tblIncPar5"));
        IncomeParColumn.add(objCustomerRB.getString("tblIncPar6"));
        IncomeParColumn.add(objCustomerRB.getString("tblIncPar7"));
        if (tblIncomeParticulars != null && tblIncomeParticulars.getDataArrayList() != null) {
            tblIncomeParticulars.setColumnIdentifiers(IncomeParColumn);
        } else {
            tblIncomeParticulars = new EnhancedTableModel(null, IncomeParColumn);
        }
        IncVal = new ArrayList();
    }

    private void createLandDetails() throws Exception {
        final ArrayList LandDetColumn = new ArrayList();
        LandDetColumn.add(objCustomerRB.getString("tblLand1"));
        LandDetColumn.add(objCustomerRB.getString("tblLand2"));
        LandDetColumn.add(objCustomerRB.getString("tblLand3"));
        LandDetColumn.add(objCustomerRB.getString("tblLand4"));
        LandDetColumn.add(objCustomerRB.getString("tblLand5"));
        LandDetColumn.add(objCustomerRB.getString("tblLand6"));
        LandDetColumn.add(objCustomerRB.getString("tblLand7"));
        LandDetColumn.add(objCustomerRB.getString("tblLand8"));
        LandDetColumn.add(objCustomerRB.getString("tblLand9"));
        if (tblCustomerLandDetails != null && tblCustomerLandDetails.getDataArrayList() != null) {
            tblCustomerLandDetails.setColumnIdentifiers(LandDetColumn);
        } else {
            tblCustomerLandDetails = new EnhancedTableModel(null, LandDetColumn);
        }
        LandDetailsVal = new ArrayList();
    }

    /**
     * To build Phone table
     *
     * @throws Exception
     */
    private void createPhoneListTable() throws Exception {
        final ArrayList phoneListColumn = new ArrayList();
        phoneListColumn.add(objCustomerRB.getString("tblPhoneColumn1"));
        phoneListColumn.add(objCustomerRB.getString("tblPhoneColumn2"));
        phoneListColumn.add(objCustomerRB.getString("tblPhoneColumn3"));
        if (tblPhoneList != null && tblPhoneList.getDataArrayList() != null) {
            tblPhoneList.setColumnIdentifiers(phoneListColumn);
        } else {
            tblPhoneList = new EnhancedTableModel(null, phoneListColumn);
        }
    }

    /**
     * To build Customer History table
     *
     * @throws Exception
     */
    private void createCustomerHistoryTable() throws Exception {
        final ArrayList tblhistorycolumn = new ArrayList();
        tblhistorycolumn.add(objCustomerRB.getString("tblHistoryTitle1"));
        tblhistorycolumn.add(objCustomerRB.getString("tblHistoryTitle2"));
        tblhistorycolumn.add(objCustomerRB.getString("tblHistoryTitle3"));
        tblhistorycolumn.add(objCustomerRB.getString("tblHistoryTitle4"));
        tblhistorycolumn.add(objCustomerRB.getString("tblHistoryTitle5"));
        tblhistorycolumn.add(objCustomerRB.getString("tblHistoryTitle6"));
        tblhistorycolumn.add(objCustomerRB.getString("tblHistoryTitle7"));
        tblhistorycolumn.add(objCustomerRB.getString("tblHistoryTitle8"));
        tblhistorycolumn.add(objCustomerRB.getString("tblHistoryTitle9"));
        tblhistorycolumn.add(objCustomerRB.getString("tblHistoryTitle10"));
        tblhistorycolumn.add(objCustomerRB.getString("tblHistoryTitle11"));
        if (tbmCustomerHistory != null && tbmCustomerHistory.getDataArrayList() != null) {
            tbmCustomerHistory.setColumnIdentifiers(tblhistorycolumn);
        } else {
            tbmCustomerHistory = new EnhancedTableModel(null, tblhistorycolumn);
        }
    }

    /**
     * To fill the comboboxes
     */
    private void fillDropdown() throws Exception {
        lookupMap = new HashMap();
        lookupMap.put(CommonConstants.JNDI, "LookUpJNDI");
        lookupMap.put(CommonConstants.HOME, "common.lookup.LookUpHome");
        lookupMap.put(CommonConstants.REMOTE, "common.lookup.LookUp");

        param = new HashMap();
        param.put(CommonConstants.MAP_NAME, null);

        final ArrayList lookupKey = new ArrayList();
        lookupKey.add("CUSTOMER.TITLE");
        lookupKey.add("CUSTOMER.RESIDENTIALSTATUS");
        lookupKey.add("FARMER_CLASSIFICATION");
        lookupKey.add("CUSTOMER.AMSAM");
        lookupKey.add("CUSTOMER.DESAM");
        if (screenCustType == INDIVIDUAL) {
            //            lookupKey.add("FOREX.CUSTTYPE");
            lookupKey.add("CUSTOMER.CUSTOMERTYPE");
        } else if (screenCustType == CORPORATE) {
            lookupKey.add("CORPORATE.CUSTOMER_TYPE");
        }
        //        lookupKey.add("CUSTOMER.RELATIONMANAGER");
        lookupKey.add("INTRODUCTION");
        lookupKey.add("CUSTOMER.ANNINCOME");
        lookupKey.add("CUSTOMER.PREFCOMM");
        lookupKey.add("CUSTOMER.CUSTOMERGROUP");
        lookupKey.add("CUSTOMER.VEHICLE");
        lookupKey.add("CUSTOMER.PROFESSION");
        lookupKey.add("CUSTOMER.PRIMARYOCCUPATION");
        lookupKey.add("CUSTOMER.EDUCATION");
        lookupKey.add("CUSTOMER.COUNTRY");


        if (screenCustType == INDIVIDUAL) {
            lookupKey.add("CUSTOMER.ADDRTYPE");
            lookupKey.add("GUARDIAN_RELATIONSHIP");
            lookupKey.add("CUSTOMER.CASTE");
            lookupKey.add("CUSTOMER.SUBCASTE");
            lookupKey.add("CUSTOMER.RELIGION");
            lookupKey.add("LAND_SOURCE");
            lookupKey.add("LAND_IRRIGATED");
            lookupKey.add("GUARDIAN_RELATIONSHIP_INCOME");
            lookupKey.add("CUSTOMER.DIVISION");
            
        } else if (screenCustType == CORPORATE) {
            lookupKey.add("CORPORATE.ADDRESS_TYPE");
            lookupKey.add("TERM_LOAN.BUSINESS_NATURE");
        }
        lookupKey.add("CUSTOMER.CITY");
        lookupKey.add("CUSTOMER.STATE");
        lookupKey.add("CUSTOMER.PHONETYPE");

        lookupKey.add("CUSTOMER.MEMBERSHIP");
        lookupKey.add("CUSTOMER.CUST_STATUS");
        lookupKey.add("CUSTOMER.CAREOF");
        lookupKey.add("INTRO_DOCUMENT");
        lookupKey.add("INDENTITY_TYPE");
        lookupKey.add("INCOME_PACKAGE");
        lookupKey.add("CUSTOMER.TALUK");
        lookupKey.add("CUSTOMER.KARA");
        lookupKey.add("CUSTOMER.VILLAGE");
        lookupKey.add("POSTOFFICE");
        lookupKey.add("CUSTOMER.WARD"); // Added by nithya

        param.put(CommonConstants.PARAMFORQUERY, lookupKey);

//        final HashMap lookupValues = populateData(param);
        final HashMap lookupValues = ClientUtil.populateLookupData(param);

        fillData((HashMap) lookupValues.get("CUSTOMER.TITLE"));
        cbmTitle = new ComboBoxModel(key, value);
        fillData((HashMap) lookupValues.get("CUSTOMER.AMSAM"));
        cbmcomboAmsam=new ComboBoxModel(key, value);
         fillData((HashMap) lookupValues.get("CUSTOMER.DESAM"));
        cbmcomboDesam=new ComboBoxModel(key, value);
        
        // Added by nithya
        fillData((HashMap)lookupValues.get("CUSTOMER.WARD"));
        cbmWardName = new ComboBoxModel(key,value);
        // End
        
        fillData((HashMap) lookupValues.get("FARMER_CLASSIFICATION"));
        cbmFarClass = new ComboBoxModel(key, value);

        fillData((HashMap) lookupValues.get("CUSTOMER.RESIDENTIALSTATUS"));
        cbmResidentialStatus = new ComboBoxModel(key, value);

        if (screenCustType == INDIVIDUAL) {
            fillData((HashMap) lookupValues.get("CUSTOMER.CUSTOMERTYPE"));
        } else if (screenCustType == CORPORATE) {
            fillData((HashMap) lookupValues.get("CORPORATE.CUSTOMER_TYPE"));
        }
        cbmCustomerType = new ComboBoxModel(key, value);

        //        fillData((HashMap)lookupValues.get("CUSTOMER.RELATIONMANAGER"));
        //        cbmRelationManager = new ComboBoxModel(key,value);

        fillData((HashMap) lookupValues.get("INTRODUCTION"));
        cbmIntroType = new ComboBoxModel(key, value);

        fillData((HashMap) lookupValues.get("CUSTOMER.ANNINCOME"));
        cbmAnnualIncomeLevel = new ComboBoxModel(key, value);

        fillData((HashMap) lookupValues.get("CUSTOMER.PREFCOMM"));
        cbmPrefCommunication = new ComboBoxModel(key, value);

        fillData((HashMap) lookupValues.get("CUSTOMER.CUSTOMERGROUP"));
        cbmCustomerGroup = new ComboBoxModel(key, value);

        fillData((HashMap) lookupValues.get("CUSTOMER.VEHICLE"));
        cbmVehicle = new ComboBoxModel(key, value);

        fillData((HashMap) lookupValues.get("CUSTOMER.PROFESSION"));
        cbmProfession = new ComboBoxModel(key, value);
        cbmIncProfession = new ComboBoxModel(key, value);

        fillData((HashMap) lookupValues.get("CUSTOMER.PRIMARYOCCUPATION"));
        cbmPrimaryOccupation = new ComboBoxModel(key, value);

        fillData((HashMap) lookupValues.get("CUSTOMER.EDUCATION"));
        cbmEducationalLevel = new ComboBoxModel(key, value);

        fillData((HashMap) lookupValues.get("CUSTOMER.COUNTRY"));
        cbmCountry = new ComboBoxModel(key, value);

        if (screenCustType == INDIVIDUAL) {
            cbmGCountry = new ComboBoxModel(key, value);
            fillData((HashMap) lookupValues.get("CUSTOMER.CASTE"));
            cbmCaste = new ComboBoxModel(key, value);
            fillData((HashMap) lookupValues.get("CUSTOMER.SUBCASTE"));
            cbmSubCaste = new ComboBoxModel(key, value);
//            changed by nikhil
            fillData((HashMap) lookupValues.get("CUSTOMER.RELIGION"));
            cbmReligion = new ComboBoxModel(key, value);
        }
        
        

        if (screenCustType == INDIVIDUAL) {
            fillData((HashMap) lookupValues.get("GUARDIAN_RELATIONSHIP"));
            cbmRelationNO = new ComboBoxModel(key, value);
            fillData((HashMap) lookupValues.get("GUARDIAN_RELATIONSHIP_INCOME"));
            cbmIncRelation = new ComboBoxModel(key, value);
            fillData((HashMap) lookupValues.get("LAND_SOURCE"));
            cbmSourceIrrigated = new ComboBoxModel(key, value);
            cbmLandDetState = new ComboBoxModel(key, value);
            fillData((HashMap) lookupValues.get("LAND_IRRIGATED"));
            cbmIrrigated = new ComboBoxModel(key, value);

            fillData((HashMap) lookupValues.get("CUSTOMER.DIVISION"));
            cbmDivision = new ComboBoxModel(key, value);
            
            // Moved code  by nithya on 13-05-2016 for 4454
            HashMap agentCustMap = new HashMap();
            agentCustMap.put("BRANCH_ID", ProxyParameters.BRANCH_ID);
            //List list = ClientUtil.executeQuery("getAgentIdName", where);
            List list = ClientUtil.executeQuery("getAgentNameID", agentCustMap);
            System.out.println("$$$$$$AgentId : " + list);
            getMap(list);
            cbmAgentCustId = new ComboBoxModel(key, value);
            
            fillData((HashMap) lookupValues.get("CUSTOMER.ADDRTYPE"));            
            
        } else if (screenCustType == CORPORATE) {
            fillData((HashMap) lookupValues.get("TERM_LOAN.BUSINESS_NATURE"));
            cbmBusNature = new ComboBoxModel(key, value);
            fillData((HashMap) lookupValues.get("CORPORATE.ADDRESS_TYPE"));

        }
        cbmAddressType = new ComboBoxModel(key, value);

        fillData((HashMap) lookupValues.get("CUSTOMER.CITY"));
        cbmCity = new ComboBoxModel(key, value);
        cbmPassportIssuePlace = new ComboBoxModel(key, value);
        if (screenCustType == INDIVIDUAL) {
            cbmGCity = new ComboBoxModel(key, value);
        }
        fillData((HashMap) lookupValues.get("CUSTOMER.STATE"));
        cbmState = new ComboBoxModel(key, value);
        if (screenCustType == INDIVIDUAL) {
            cbmGState = new ComboBoxModel(key, value);
        }
        fillData((HashMap) lookupValues.get("CUSTOMER.PHONETYPE"));
        cbmPhoneType = new ComboBoxModel(key, value);


        fillData((HashMap) lookupValues.get("CUSTOMER.MEMBERSHIP"));
        cbmMembershipClass = new ComboBoxModel(key, value);

        fillData((HashMap) lookupValues.get("POSTOFFICE"));
      //   Collections.sort(lookupValues.get("POSTOFFICE"));
        CbmPostOffice = new ComboBoxModel(key, value);
//        HashMap postMap = (HashMap) lookupValues.get("POSTOFFICE");
//        System.out.println("lookupValues :::" +postMap.get("VALUE"));
        
        
        fillData((HashMap) lookupValues.get("CUSTOMER.CUST_STATUS"));
        cbmCustStatus = new ComboBoxModel(key, value);

        fillData((HashMap) lookupValues.get("CUSTOMER.CAREOF"));
        cbmCareOf = new ComboBoxModel(key, value);

        fillData((HashMap) lookupValues.get("INCOME_PACKAGE"));
        cbmIncPack = new ComboBoxModel(key, value);


        fillData((HashMap) lookupValues.get("INTRO_DOCUMENT"));
        cbmAddrProof = new ComboBoxModel(key, value);

        fillData((HashMap) lookupValues.get("INDENTITY_TYPE"));
        cbmIdenProof = new ComboBoxModel(key, value);
        fillData((HashMap) lookupValues.get("CUSTOMER.TALUK"));
        cbmCustTaluk = new ComboBoxModel(key, value);
        fillData((HashMap) lookupValues.get("CUSTOMER.VILLAGE"));
        cbmCustVillage = new ComboBoxModel(key, value);
        fillData((HashMap) lookupValues.get("CUSTOMER.KARA"));
        cbmCustKara = new ComboBoxModel(key, value);

        param.put(CommonConstants.MAP_NAME, "getRelationshipManager");
        HashMap where = new HashMap();
        where.put("BRANCH_ID", ProxyParameters.BRANCH_ID);
        param.put(CommonConstants.PARAMFORQUERY, where);
        where = null;
        keyValue = ClientUtil.populateLookupData(param);
        fillData((HashMap) keyValue.get(CommonConstants.DATA));
        cbmRelationManager = new ComboBoxModel(key, value);

        param.put(CommonConstants.MAP_NAME, "getState");
        HashMap wheres = new HashMap();
        param.put(CommonConstants.PARAMFORQUERY, wheres);
        wheres = null;
        keyValue = ClientUtil.populateLookupData(param);
        fillData((HashMap) keyValue.get(CommonConstants.DATA));
        cbmLandDetState = new ComboBoxModel(key, value); 
    }

    public void setCbmDistrict(String state) {
        try {
            HashMap where = new HashMap();
            where.put("STATE_CODE", CommonUtil.convertObjToInt(state));
            param.put(CommonConstants.MAP_NAME, "getDistrictName");
            param.put(CommonConstants.PARAMFORQUERY, where);
            where = null;
            keyValue = ClientUtil.populateLookupData(param);
            fillData((HashMap) keyValue.get(CommonConstants.DATA));
            cbmDistrict = new ComboBoxModel(key, value);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        this.cbmDistrict = cbmDistrict;
        setChanged();
    }

    public void setCbmTaluk(String dis, String state) {
        try {
            HashMap where = new HashMap();
            where.put("DISTRICT_CODE", CommonUtil.convertObjToInt(dis));
            where.put("STATE_CODE", CommonUtil.convertObjToInt(state));
            param.put(CommonConstants.MAP_NAME, "getTalukName");
            param.put(CommonConstants.PARAMFORQUERY, where);
            where = null;
            keyValue = ClientUtil.populateLookupData(param);
            fillData((HashMap) keyValue.get(CommonConstants.DATA));
            cbmTaluk = new ComboBoxModel(key, value);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        this.cbmTaluk = cbmTaluk;
        setChanged();
    }

    /**
     * To set the key & value for comboboxes
     */
    private void fillData(HashMap keyValue) throws Exception {
//        ArrayList    keys = (ArrayList)keyValue.get(CommonConstants.KEY);
//        for(int i=0;i<keys.size();i++)
//        {
//        String checkKey=CommonUtil.convertObjToStr(keys.get(i));
//        System.out.println("check Key   "+checkKey);
//        if(checkKey.equals("NONE")){
//        
    
        key = (ArrayList) keyValue.get(CommonConstants.KEY);
        value = (ArrayList) keyValue.get(CommonConstants.VALUE);


    }

    /**
     * To get data for comboboxes
     */
    private HashMap populateData(HashMap obj) throws Exception {
        keyValue = proxy.executeQuery(obj, lookupMap);
        log.info("Got HashMap");
        return keyValue;
    }

    void setTxtFirstName(String txtFirstName) {
        this.txtFirstName = txtFirstName;
        setChanged();
    }

    String getTxtFirstName() {
        return this.txtFirstName;
    }

    void setTxtMiddleName(String txtMiddleName) {
        this.txtMiddleName = txtMiddleName;
        setChanged();
    }

    String getTxtMiddleName() {
        return this.txtMiddleName;
    }

    void setTxtLastName(String txtLastName) {
        this.txtLastName = txtLastName;
        setChanged();
    }

    String getTxtLastName() {
        return this.txtLastName;
    }

    void setRdoGender_Male(boolean rdoGender_Male) {
        this.rdoGender_Male = rdoGender_Male;
        setChanged();
    }

    boolean getRdoGender_Male() {
        return this.rdoGender_Male;
    }

    void setRdoGender_Female(boolean rdoGender_Female) {
        this.rdoGender_Female = rdoGender_Female;
        setChanged();
    }

    boolean getRdoGender_Female() {
        return this.rdoGender_Female;
    }

    void setRdoMaritalStatus_Single(boolean rdoMaritalStatus_Single) {
        this.rdoMaritalStatus_Single = rdoMaritalStatus_Single;
        setChanged();
    }

    boolean getRdoMaritalStatus_Single() {
        return this.rdoMaritalStatus_Single;
    }

    void setRdoMaritalStatus_Married(boolean rdoMaritalStatus_Married) {
        this.rdoMaritalStatus_Married = rdoMaritalStatus_Married;
        setChanged();
    }

    boolean getRdoMaritalStatus_Married() {
        return this.rdoMaritalStatus_Married;
    }

    public boolean getRdoMaritalStatus_Widow() {
        return rdoMaritalStatus_Widow;
    }

    public void setRdoMaritalStatus_Widow(boolean rdoMaritalStatus_Widow) {
        this.rdoMaritalStatus_Widow = rdoMaritalStatus_Widow;
         setChanged();
    }

    void setTxtNationality(String txtNationality) {
        this.txtNationality = txtNationality;
        setChanged();
    }

    String getTxtNationality() {
        return this.txtNationality;
    }

    void setTxtLanguage(String txtLanguage) {
        this.txtLanguage = txtLanguage;
        setChanged();
    }

    String getTxtLanguage() {
        return this.txtLanguage;
    }

    void setCboTitle(String cboTitle) {
        this.cboTitle = cboTitle;
        setChanged();
    }

    String getCboTitle() {
        return this.cboTitle;
    }

    void setCbmTitle(ComboBoxModel cbmTitle) {
        this.cbmTitle = cbmTitle;
        setChanged();
    }

    ComboBoxModel getCbmTitle() {
        return this.cbmTitle;
    }

    void setTdtDateOfBirth(String tdtDateOfBirth) {
        this.tdtDateOfBirth = tdtDateOfBirth;
        setChanged();
    }

    String getTdtDateOfBirth() {
        return this.tdtDateOfBirth;
    }

    void setTxtCustomerID(String txtCustomerID) {
        this.txtCustomerID = txtCustomerID;
        setChanged();
    }

    String getTxtCustomerID() {
        return this.txtCustomerID;
    }

    void setTxtRemarks(String txtRemarks) {
        this.txtRemarks = txtRemarks;
        setChanged();
    }

    String getTxtRemarks() {
        return this.txtRemarks;
    }

    void setCboResidentialStatus(String cboResidentialStatus) {
        this.cboResidentialStatus = cboResidentialStatus;
        setChanged();
    }

    String getCboResidentialStatus() {
        return this.cboResidentialStatus;
    }

    void setCbmResidentialStatus(ComboBoxModel cbmResidentialStatus) {
        this.cbmResidentialStatus = cbmResidentialStatus;
        setChanged();
    }

    ComboBoxModel getCbmResidentialStatus() {
        return this.cbmResidentialStatus;
    }

    void setLblCustomerStatus(String lblCustomerStatus) {
        this.lblCustomerStatus = lblCustomerStatus;
        setChanged();
    }

    String getLblCustomerStatus() {
        return this.lblCustomerStatus;
    }

    void setIsMinor(boolean isMinor) {
        this.isMinor = isMinor;
        setChanged();
    }

    boolean getIsMinor() {
        return this.isMinor;
    }

    void setCbmCustomerType(ComboBoxModel cbmCustomerType) {
        this.cbmCustomerType = cbmCustomerType;
        setChanged();
    }

    ComboBoxModel getCbmCustomerType() {
        return this.cbmCustomerType;
    }

    void setCboCustomerType(String cboCustomerType) {
        this.cboCustomerType = cboCustomerType;
        setChanged();
    }

    String getCboCustomerType() {
        return this.cboCustomerType;
    }

    void setCboRelationManager(String cboRelationManager) {
        this.cboRelationManager = cboRelationManager;
        setChanged();
    }

    String getCboRelationManager() {
        return this.cboRelationManager;
    }

    void setCbmRelationManager(ComboBoxModel cbmRelationManager) {
        this.cbmRelationManager = cbmRelationManager;
        setChanged();
    }

    ComboBoxModel getCbmRelationManager() {
        return this.cbmRelationManager;
    }

    void setTxtCompany(String txtCompany) {
        this.txtCompany = txtCompany;
        setChanged();
    }

    String getTxtCompany() {
        return this.txtCompany;
    }

    void setCboAnnualIncomeLevel(String cboAnnualIncomeLevel) {
        this.cboAnnualIncomeLevel = cboAnnualIncomeLevel;
        setChanged();
    }

    String getCboAnnualIncomeLevel() {
        return this.cboAnnualIncomeLevel;
    }

    void setCbmAnnualIncomeLevel(ComboBoxModel cbmAnnualIncomeLevel) {
        this.cbmAnnualIncomeLevel = cbmAnnualIncomeLevel;
        setChanged();
    }

    ComboBoxModel getCbmAnnualIncomeLevel() {
        return this.cbmAnnualIncomeLevel;
    }

    void setCboPrefCommunication(String cboPrefCommunication) {
        this.cboPrefCommunication = cboPrefCommunication;
        setChanged();
    }

    String getCboPrefCommunication() {
        return this.cboPrefCommunication;
    }

    void setCbmPrefCommunication(ComboBoxModel cbmPrefCommunication) {
        this.cbmPrefCommunication = cbmPrefCommunication;
        setChanged();
    }

    ComboBoxModel getCbmPrefCommunication() {
        return this.cbmPrefCommunication;
    }

    void setCboCustomerGroup(String cboCustomerGroup) {
        this.cboCustomerGroup = cboCustomerGroup;
        setChanged();
    }

    String getCboCustomerGroup() {
        return this.cboCustomerGroup;
    }

    void setCbmCustomerGroup(ComboBoxModel cbmCustomerGroup) {
        this.cbmCustomerGroup = cbmCustomerGroup;
        setChanged();
    }

    ComboBoxModel getCbmCustomerGroup() {
        return this.cbmCustomerGroup;
    }

    void setCboVehicle(String cboVehicle) {
        this.cboVehicle = cboVehicle;
        setChanged();
    }

    String getCboVehicle() {
        return this.cboVehicle;
    }

    void setCbmVehicle(ComboBoxModel cbmVehicle) {
        this.cbmVehicle = cbmVehicle;
        setChanged();
    }

    ComboBoxModel getCbmVehicle() {
        return this.cbmVehicle;
    }

    void setCboProfession(String cboProfession) {
        this.cboProfession = cboProfession;
        setChanged();
    }

    String getCboProfession() {
        return this.cboProfession;
    }

    void setCbmProfession(ComboBoxModel cbmProfession) {
        this.cbmProfession = cbmProfession;
        setChanged();
    }

    ComboBoxModel getCbmProfession() {
        return this.cbmProfession;
    }

    void setCboPrimaryOccupation(String cboPrimaryOccupation) {
        this.cboPrimaryOccupation = cboPrimaryOccupation;
        setChanged();
    }

    String getCboPrimaryOccupation() {
        return this.cboPrimaryOccupation;
    }

    void setCbmPrimaryOccupation(ComboBoxModel cbmPrimaryOccupation) {
        this.cbmPrimaryOccupation = cbmPrimaryOccupation;
        setChanged();
    }

    ComboBoxModel getCbmPrimaryOccupation() {
        return this.cbmPrimaryOccupation;
    }

    void setCbmEducationalLevel(ComboBoxModel cbmEducationalLevel) {
        this.cbmEducationalLevel = cbmEducationalLevel;
        setChanged();
    }

    ComboBoxModel getCbmEducationalLevel() {
        return this.cbmEducationalLevel;
    }

    void setCboEducationalLevel(String cboEducationalLevel) {
        this.cboEducationalLevel = cboEducationalLevel;
        setChanged();
    }

    String getCboEducationalLevel() {
        return this.cboEducationalLevel;
    }

    void setTxtEmailID(String txtEmailID) {
        this.txtEmailID = txtEmailID;
        setChanged();
    }

    String getTxtEmailID() {
        return this.txtEmailID;
    }

    void setTxtNetWorth(String txtNetWorth) {
        this.txtNetWorth = txtNetWorth;
        setChanged();
    }

    String getTxtNetWorth() {
        return this.txtNetWorth;
    }

    void setTxtPincode(String txtPincode) {
        this.txtPincode = txtPincode;
        setChanged();
    }

    String getTxtPincode() {
        return this.txtPincode;
    }

    void setTxtStreet(String txtStreet) {
        this.txtStreet = txtStreet;
        setChanged();
    }

    String getTxtStreet() {
        return this.txtStreet;
    }

    void setCboCountry(String cboCountry) {
        this.cboCountry = cboCountry;
        setChanged();
    }

    String getCboCountry() {
        return this.cboCountry;
    }

    void setCbmCountry(ComboBoxModel cbmCountry) {
        this.cbmCountry = cbmCountry;
        setChanged();
    }

    ComboBoxModel getCbmCountry() {
        return this.cbmCountry;
    }

    void setTxtArea(String txtArea) {
        this.txtArea = txtArea;
        setChanged();
    }

    String getTxtArea() {
        return this.txtArea;
    }

    void setCboAddressType(String cboAddressType) {
        this.cboAddressType = cboAddressType;
        setChanged();
    }

    String getCboAddressType() {
        return this.cboAddressType;
    }

    void setCbmAddressType(ComboBoxModel cbmAddressType) {
        this.cbmAddressType = cbmAddressType;
        setChanged();
    }

    ComboBoxModel getCbmAddressType() {
        return this.cbmAddressType;
    }

    void setCboCity(String cboCity) {
        this.cboCity = cboCity;
        setChanged();
    }

    String getCboCity() {
        return this.cboCity;
    }

    void setCbmCity(ComboBoxModel cbmCity) {
        this.cbmCity = cbmCity;
        setChanged();
    }

    ComboBoxModel getCbmCity() {
        return this.cbmCity;
    }

    void setCboState(String cboState) {
        this.cboState = cboState;
        setChanged();
    }

    String getCboState() {
        return this.cboState;
    }

    void setCbmState(ComboBoxModel cbmState) {
        this.cbmState = cbmState;
        setChanged();
    }

    ComboBoxModel getCbmState() {
        return this.cbmState;
    }

    void setTxtPhoneNumber(String txtPhoneNumber) {
        this.txtPhoneNumber = txtPhoneNumber;
        setChanged();
    }

    String getTxtPhoneNumber() {
        return this.txtPhoneNumber;
    }

    void setTxtAreaCode(String txtAreaCode) {
        this.txtAreaCode = txtAreaCode;
        setChanged();
    }

    String getTxtAreaCode() {
        return this.txtAreaCode;
    }

    void setCboPhoneType(String cboPhoneType) {
        this.cboPhoneType = cboPhoneType;
        setChanged();
    }

    String getCboPhoneType() {
        return this.cboPhoneType;
    }

    void setCbmPhoneType(ComboBoxModel cbmPhoneType) {
        this.cbmPhoneType = cbmPhoneType;
        setChanged();
    }

    ComboBoxModel getCbmPhoneType() {
        return this.cbmPhoneType;
    }

    void setTblContactList(EnhancedTableModel tblContactList) {
        this.tblContactList = tblContactList;
        setChanged();
    }

    EnhancedTableModel getTblContactList() {
        return this.tblContactList;
    }

    void setTblPhoneList(EnhancedTableModel tblPhoneList) {
        this.tblPhoneList = tblPhoneList;
        setChanged();
    }

    EnhancedTableModel getTblPhoneList() {
        return this.tblPhoneList;
    }

    /**
     * Getter for property tbmCustomerHistory.
     *
     * @return Value of property tbmCustomerHistory.
     */
    public com.see.truetransact.clientutil.EnhancedTableModel getTbmCustomerHistory() {
        return tbmCustomerHistory;
    }

    /**
     * Setter for property tbmCustomerHistory.
     *
     * @param tbmCustomerHistory New value of property tbmCustomerHistory.
     */
    public void setTbmCustomerHistory(com.see.truetransact.clientutil.EnhancedTableModel tbmCustomerHistory) {
        this.tbmCustomerHistory = tbmCustomerHistory;
    }

    void setActionType(int actionType) {
        this.actionType = actionType;
        setChanged();
    }

    int getActionType() {
        return this.actionType;
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

    public String getCommAddrType() {
        return this.commAddrType;
    }

    public void setCommAddrType(String commAddrType) {
        this.commAddrType = commAddrType;
        setChanged();
    }

    public String getTxtSsn() {
        return this.txtSsn;
    }

    public void setTxtSsn(String txtSsn) {
        this.txtSsn = txtSsn;
        setChanged();
    }

    public String getTxtTransPwd() {
        return txtTransPwd;
    }

    public void setTxtTransPwd(String txtTransPwd) {
        this.txtTransPwd = txtTransPwd;
        setChanged();
    }

    //    public String getTxtCustUserId(){
    //        return this.txtCustUserId;
    //    }
    //
    //    public void setTxtCustUserId(String txtCustUserId) {
    //        this.txtCustUserId = txtCustUserId;
    //        setChanged();
    //    }
    public String getTxtCustPwd() {
        return this.txtCustPwd;
    }

    public void setTxtCustPwd(String txtCustPwd) {
        this.txtCustPwd = txtCustPwd;
        setChanged();
    }

    public String getPhotoFile() {
        return this.photoFile;
    }

    public void setPhotoFile(String photoFile) {
        this.photoFile = photoFile;
        setChanged();
    }

    public String getSignFile() {
        return this.signFile;
    }

    public void setSignFile(String signFile) {
        this.signFile = signFile;
        setChanged();
    }

    public String getLblPhoto() {
        return this.lblPhoto;
    }

    public void setLblPhoto(String lblPhoto) {
        this.lblPhoto = lblPhoto;
        setChanged();
    }

    public String getLblSign() {
        return this.lblSign;
    }

    public void setLblSign(String lblSign) {
        this.lblSign = lblSign;
        setChanged();
    }

    public String getTxtAuthCustId() {
        return this.txtAuthCustId;
    }

    public void setTxtAuthCustId(String txtAuthCustId) {
        this.txtAuthCustId = txtAuthCustId;
        setChanged();
    }

    public String getTxtCustUserid() {
        return this.txtCustUserid;
    }

    public void setTxtCustUserid(String txtCustUserid) {
        this.txtCustUserid = txtCustUserid;
        setChanged();
    }

    public String getTxtWebSite() {
        return this.txtWebSite;
    }

    public void setTxtWebSite(String txtWebSite) {
        this.txtWebSite = txtWebSite;
        setChanged();
    }

    /**
     * * START -- FINANCE DETAILS ******
     */
    // Setter method for txtAuthCapital
    void setTxtAuthCapital(String txtAuthCapital) {
        this.txtAuthCapital = txtAuthCapital;
        setChanged();
    }
    // Getter method for txtAuthCapital

    String getTxtAuthCapital() {
        return this.txtAuthCapital;
    }

    // Setter method for txtIssuedCapital
    void setTxtIssuedCapital(String txtIssuedCapital) {
        this.txtIssuedCapital = txtIssuedCapital;
        setChanged();
    }
    // Getter method for txtIssuedCapital

    String getTxtIssuedCapital() {
        return this.txtIssuedCapital;
    }

    // Setter method for txtSubscribedCapital
    void setTxtSubscribedCapital(String txtSubscribedCapital) {
        this.txtSubscribedCapital = txtSubscribedCapital;
        setChanged();
    }
    // Getter method for txtSubscribedCapital

    String getTxtSubscribedCapital() {
        return this.txtSubscribedCapital;
    }

    // Setter method for txtTotalResource
    void setTxtTotalResource(String txtTotalResource) {
        this.txtTotalResource = txtTotalResource;
        setChanged();
    }
    // Getter method for txtTotalResource

    String getTxtTotalResource() {
        return this.txtTotalResource;
    }

    // Setter method for txtLastYrPL
    void setTxtLastYrPL(String txtLastYrPL) {
        this.txtLastYrPL = txtLastYrPL;
        setChanged();
    }
    // Getter method for txtLastYrPL

    String getTxtLastYrPL() {
        return this.txtLastYrPL;
    }

    // Setter method for txtDividendPercentage
    void setTxtDividendPercentage(String txtDividendPercentage) {
        this.txtDividendPercentage = txtDividendPercentage;
        setChanged();
    }
    // Getter method for txtDividendPercentage

    String getTxtDividendPercentage() {
        return this.txtDividendPercentage;
    }

    /**
     * * END -- FINANCE DETAILS ******
     */
    // Setter method for cboCareOf
    void setCboCareOf(String cboCareOf) {
        this.cboCareOf = cboCareOf;
        setChanged();
    }
    // Getter method for cboCareOf

    String getCboCareOf() {
        return this.cboCareOf;
    }

    // Setter method for cbmCareOf
    void setCbmCareOf(ComboBoxModel cbmCareOf) {
        this.cbmCareOf = cbmCareOf;
        setChanged();
    }
    // Getter method for cbmCareOf

    ComboBoxModel getCbmCareOf() {
        return this.cbmCareOf;
    }

    // Setter method for txtName
    void setTxtName(String txtName) {
        this.txtName = txtName;
        setChanged();
    }
    // Getter method for txtName

    String getTxtName() {
        return this.txtName;
    }

    // Setter method for cboMembershipClass
    void setCboMembershipClass(String cboMembershipClass) {
        this.cboMembershipClass = cboMembershipClass;
        setChanged();
    }
    // Getter method for cboMembershipClass

    String getCboMembershipClass() {
        return this.cboMembershipClass;
    }

    // Setter method for cbmMembershipClass
    void setCbmMembershipClass(ComboBoxModel cbmMembershipClass) {
        this.cbmMembershipClass = cbmMembershipClass;
        setChanged();
    }
    // Getter method for cbmMembershipClass

    ComboBoxModel getCbmMembershipClass() {
        return this.cbmMembershipClass;
    }

    // Setter method for cboCustStatus
    void setCboCustStatus(String cboCustStatus) {
        this.cboCustStatus = cboCustStatus;
        setChanged();
    }
    // Getter method for cboCustStatus

    String getCboCustStatus() {
        return this.cboCustStatus;
    }

    // Setter method for cbmCustStatus
    void setCbmCustStatus(ComboBoxModel cbmCustStatus) {

        this.cbmCustStatus = cbmCustStatus;
        setChanged();
    }
    // Getter method for cbmCustStatus

    ComboBoxModel getCbmCustStatus() {
        return this.cbmCustStatus;
    }

    public void setPhotoByteArray(byte[] photoByteArray) {
        this.photoByteArray = photoByteArray;
        setChanged();
    }

    public byte[] getPhotoByteArray() {
        return this.photoByteArray;
    }

    public void setSignByteArray(byte[] signByteArray) {
        this.signByteArray = signByteArray;
        setChanged();
    }

    public byte[] getSignByteArray() {
        return this.signByteArray;
    }

//    public void setPhotoByteArray(File photoByteArray) {
//        this.photoByteArray = photoByteArray;
//        setChanged();
//    }
//    
//    public File getPhotoByteArray() {
//        return this.photoByteArray;
//    }
//
//    public void setSignByteArray(File signByteArray) {
//        this.signByteArray = signByteArray;
//        setChanged();
//    }
//    
//    public File getSignByteArray() {
//        return this.signByteArray;
//    }
    /**
     * Getter for property lblCrAvldSinceValue.
     *
     * @return Value of property lblCrAvldSinceValue.
     */
    public java.lang.String getTdtCrAvldSince() {
        return tdtCrAvldSince;
    }

    /**
     * Setter for property lblCrAvldSinceValue.
     *
     * @param lblCrAvldSinceValue New value of property lblCrAvldSinceValue.
     */
    public void setTdtCrAvldSince(java.lang.String tdtCrAvldSince) {
        this.tdtCrAvldSince = tdtCrAvldSince;
        setChanged();
    }

    /**
     * Getter for property lblRiskRateValue.
     *
     * @return Value of property lblRiskRateValue.
     */
    public java.lang.String getTxtRiskRate() {
        return txtRiskRate;
    }

    /**
     * Setter for property lblRiskRateValue.
     *
     * @param lblRiskRateValue New value of property lblRiskRateValue.
     */
    public void setTxtRiskRate(java.lang.String txtRiskRate) {
        this.txtRiskRate = txtRiskRate;
        setChanged();
    }

    /**
     * Getter for property cboBusNature.
     *
     * @return Value of property cboBusNature.
     */
    public String getCboBusNature() {
        return cboBusNature;
    }

    // Getter method for cbmRelationNO
    ComboBoxModel getCbmRelationNO() {
        return cbmRelationNO;
    }

    // Getter method for cbmGCountry
    ComboBoxModel getCbmGCountry() {
        return cbmGCountry;
    }

    // Getter method for cbmGState
    ComboBoxModel getCbmGState() {
        return cbmGState;
    }

    // Getter method for cbmGCity
    ComboBoxModel getCbmGCity() {
        return cbmGCity;
    }

    /**
     * Setter for property cboBusNature.
     *
     * @param cboBusNature New value of property cboBusNature.
     */
    public void setCboBusNature(java.lang.String cboBusNature) {
        this.cboBusNature = cboBusNature;
        setChanged();
    }

    void setCbmBusNature(ComboBoxModel cbmBusNature) {
        this.cbmBusNature = cbmBusNature;
        setChanged();
    }

    ComboBoxModel getCbmBusNature() {
        return this.cbmBusNature;
    }

    /**
     * Getter for property tdtDtEstablished.
     *
     * @return Value of property tdtDtEstablished.
     */
    public String getTdtDtEstablished() {
        return tdtDtEstablished;
    }

    /**
     * Setter for property tdtDtEstablished.
     *
     * @param tdtDtEstablished New value of property tdtDtEstablished.
     */
    public void setTdtDtEstablished(java.lang.String tdtDtEstablished) {
        this.tdtDtEstablished = tdtDtEstablished;
        setChanged();
    }

    /**
     * Getter for property txtRegNumber.
     *
     * @return Value of property txtRegNumber.
     */
    public String getTxtRegNumber() {
        return txtRegNumber;
    }

    /**
     * Setter for property txtRegNumber.
     *
     * @param txtRegNumber New value of property txtRegNumber.
     */
    public void setTxtRegNumber(java.lang.String txtRegNumber) {
        this.txtRegNumber = txtRegNumber;
        setChanged();
    }

    /**
     * Getter for property txtCEO.
     *
     * @return Value of property txtCEO.
     */
    public String getTxtCEO() {
        return txtCEO;
    }

    /**
     * Setter for property txtCEO.
     *
     * @param txtCEO New value of property txtCEO.
     */
    public void setTxtCEO(java.lang.String txtCEO) {
        this.txtCEO = txtCEO;
        setChanged();
    }

    /**
     * Getter for property txtAddrRemarks.
     *
     * @return Value of property txtAddrRemarks.
     */
    public java.lang.String getTxtAddrRemarks() {
        return txtAddrRemarks;
    }

    /**
     * Setter for property txtAddrRemarks.
     *
     * @param txtAddrRemarks New value of property txtAddrRemarks.
     */
    public void setTxtAddrRemarks(java.lang.String txtAddrRemarks) {
        this.txtAddrRemarks = txtAddrRemarks;
        setChanged();
    }

    /**
     * Getter for property chkAddrVerified.
     *
     * @return Value of property chkAddrVerified.
     */
    public boolean getChkAddrVerified() {
        return chkAddrVerified;
    }

    /**
     * Setter for property chkAddrVerified.
     *
     * @param chkAddrVerified New value of property chkAddrVerified.
     */
    public void setChkAddrVerified(boolean chkAddrVerified) {
        this.chkAddrVerified = chkAddrVerified;
        setChanged();
    }

    /**
     * Getter for property chkPhVerified.
     *
     * @return Value of property chkPhVerified.
     */
    public boolean getChkPhVerified() {
        return chkPhVerified;
    }

    /**
     * Setter for property chkPhVerified.
     *
     * @param chkPhVerified New value of property chkPhVerified.
     */
    public void setChkPhVerified(boolean chkPhVerified) {
        this.chkPhVerified = chkPhVerified;
        setChanged();
    }

    /**
     * Getter for property chkFinanceStmtVerified.
     *
     * @return Value of property chkFinanceStmtVerified.
     */
    public boolean getChkFinanceStmtVerified() {
        return chkFinanceStmtVerified;
    }

    /**
     * Setter for property chkFinanceStmtVerified.
     *
     * @param chkFinanceStmtVerified New value of property
     * chkFinanceStmtVerified.
     */
    public void setChkFinanceStmtVerified(boolean chkFinanceStmtVerified) {
        this.chkFinanceStmtVerified = chkFinanceStmtVerified;
        setChanged();
    }

    ComboBoxModel getCbmDistrict() {
        return cbmDistrict;
    }

    ComboBoxModel getCbmTaluk() {
        return cbmTaluk;
    }

    private void resetVariables() {
        addressMap = null;
        phoneMap = null;
        phoneList = null;
        data = null;
        objCustomerTO = null;
        commAddrType = "";
        IncParMap = null;
        LandDetMap = null;
        proofMap = null; // Added by nithya on 29-11-2016 for 5472
        deletedProofMap = null; // Added by nithya on 29-11-2016 for 5472
    }

    // Setter method for cboRelationNO
    void setCboRelationNO(String cboRelationNO) {
        this.cboRelationNO = cboRelationNO;
        setChanged();
    }
    // Getter method for cboRelationNO

    String getCboRelationNO() {
        return this.cboRelationNO;
    }

    // Setter method for txtGuardianNameNO
    void setTxtGuardianNameNO(String txtGuardianNameNO) {
        this.txtGuardianNameNO = txtGuardianNameNO;
        setChanged();
    }
    // Getter method for txtGuardianNameNO

    String getTxtGuardianNameNO() {
        return this.txtGuardianNameNO;
    }

    // Setter method for txtGuardianACodeNO
    void setTxtGuardianACodeNO(String txtGuardianACodeNO) {
        this.txtGuardianACodeNO = txtGuardianACodeNO;
        setChanged();
    }
    // Getter method for txtGuardianACodeNO

    String getTxtGuardianACodeNO() {
        return this.txtGuardianACodeNO;
    }

    // Setter method for txtGuardianPhoneNO
    void setTxtGuardianPhoneNO(String txtGuardianPhoneNO) {
        this.txtGuardianPhoneNO = txtGuardianPhoneNO;
        setChanged();
    }
    // Getter method for txtGuardianPhoneNO

    String getTxtGuardianPhoneNO() {
        return this.txtGuardianPhoneNO;
    }

    // Setter method for txtGStreet
    void setTxtGStreet(String txtGStreet) {
        this.txtGStreet = txtGStreet;
        setChanged();
    }
    // Getter method for txtGStreet

    String getTxtGStreet() {
        return this.txtGStreet;
    }

    // Setter method for txtGArea
    void setTxtGArea(String txtGArea) {
        this.txtGArea = txtGArea;
        setChanged();
    }
    // Getter method for txtGArea

    String getTxtGArea() {
        return this.txtGArea;
    }

    // Setter method for cboGCountry
    void setCboGCountry(String cboGCountry) {
        this.cboGCountry = cboGCountry;
        setChanged();
    }
    // Getter method for cboGCountry

    String getCboGCountry() {
        return this.cboGCountry;
    }

    // Setter method for cboGState
    void setCboGState(String cboGState) {
        this.cboGState = cboGState;
        setChanged();
    }
    // Getter method for cboGState

    String getCboGState() {
        return this.cboGState;
    }

    // Setter method for cboGCity
    void setCboGCity(String cboGCity) {
        this.cboGCity = cboGCity;
        setChanged();
    }
    // Getter method for cboGCity

    String getCboGCity() {
        return this.cboGCity;
    }

    // Setter method for txtGPinCode
    void setTxtGPinCode(String txtGPinCode) {
        this.txtGPinCode = txtGPinCode;
        setChanged();
    }
    // Getter method for txtGPinCode

    String getTxtGPinCode() {
        return this.txtGPinCode;
    }

    /**
     * To set data for insertion
     */
    private void insertData() throws Exception {
        setCustomerData();
        if (screenCustType == INDIVIDUAL && isMinor) {
            setGaurdianData();
        }
        if (screenCustType == CORPORATE) {
            setFinanceDetails();
        }
        if (txtPassportFirstName.length() > 0 && txtPassportNo.length() > 0) {
            setPassportDetails();
        }
        if (isIncomePar) {
            setIncomeParticulars();
        }
        if (isLandDetails) {
            setLandDetails();
        }
        if (isCustSuspended) {
            setSuspendCustomer();
        }
        objCustomerTO.setCommand(CommonConstants.TOSTATUS_INSERT);
        objCustomerTO.setStatus(CommonConstants.STATUS_CREATED);
        objCustomerTO.setCreateddt(curDate);
        objCustomerTO.setCreatedBy(TrueTransactMain.USER_ID);
        setData();
    }

    private void setFinanceDetails() throws Exception {
        objCustFinanceDetailsTO.setCustId(getTxtCustomerID());
        objCustFinanceDetailsTO.setAuthorizeCapital(CommonUtil.convertObjToDouble(getTxtAuthCapital()));
        objCustFinanceDetailsTO.setIssuedCapital(CommonUtil.convertObjToDouble(getTxtIssuedCapital()));
        objCustFinanceDetailsTO.setSubscribedCapital(CommonUtil.convertObjToDouble(getTxtSubscribedCapital()));
        objCustFinanceDetailsTO.setTotalResource(CommonUtil.convertObjToDouble(getTxtTotalResource()));
        objCustFinanceDetailsTO.setLastYearPl(CommonUtil.convertObjToDouble(getTxtLastYrPL()));
        objCustFinanceDetailsTO.setDividendPercent(CommonUtil.convertObjToDouble(getTxtDividendPercentage()));
        objCustFinanceDetailsTO.setCeoName(getTxtCEO());

        Date EstDt = DateUtil.getDateMMDDYYYY(getTdtDtEstablished());
        if (EstDt != null) {
            Date estDate = (Date) curDate.clone();
            estDate.setDate(EstDt.getDate());
            estDate.setMonth(EstDt.getMonth());
            estDate.setYear(EstDt.getYear());
//        objCustFinanceDetailsTO.setEstablishDt(DateUtil.getDateMMDDYYYY(getTdtDtEstablished()));
            objCustFinanceDetailsTO.setEstablishDt(estDate);
        } else {
            objCustFinanceDetailsTO.setEstablishDt(DateUtil.getDateMMDDYYYY(getTdtDtEstablished()));
        }

        objCustFinanceDetailsTO.setNatureOfBusiness(getCboBusNature());
        objCustFinanceDetailsTO.setCompRegNo(getTxtRegNumber());
        objCustFinanceDetailsTO.setTotalIncome(CommonUtil.convertObjToDouble(getTxtTotalIncome()));
        objCustFinanceDetailsTO.setTotalNonTaxExp(CommonUtil.convertObjToDouble(getTxtTotalNonTaxExp()));
        objCustFinanceDetailsTO.setLiablityTax(CommonUtil.convertObjToDouble(getTxtTaxliability()));
        objCustFinanceDetailsTO.setProfitBeforeTax(CommonUtil.convertObjToDouble(getTxtprofitBefTax()));

        Date FianDt = DateUtil.getDateMMDDYYYY(getTdtFinacialYrEnd());
        if (FianDt != null) {
            Date finDate = (Date) curDate.clone();
            finDate.setDate(FianDt.getDate());
            finDate.setMonth(FianDt.getMonth());
            finDate.setYear(FianDt.getYear());
            objCustFinanceDetailsTO.setFinancialYrEnd(finDate);
        } else {
            objCustFinanceDetailsTO.setFinancialYrEnd(DateUtil.getDateMMDDYYYY(getTdtFinacialYrEnd()));
        }
    }

    /**
     * To set the Customer Data
     */
    private void setCustomerData() throws Exception {
        objCustomerTO = new CustomerTO();
        if (screenCustType == INDIVIDUAL) {
            objCustomerTO.setCustType("INDIVIDUAL");
       
//            objCustomerTO.setPhotoFile(getPhotoFile());
//            objCustomerTO.setSignatureFile(getSignFile());
        } else if (screenCustType == CORPORATE) {
     
            objCustomerTO.setCustType(CommonUtil.convertObjToStr(getCbmCustomerType().getKeyForSelected()));
        }
   
        objCustomerTO.setAmsam(cbcomboAmsam);
        objCustomerTO.setDesam(cbcomboDesam);        
        objCustomerTO.setStaffId(getTxtStaffId());
        objCustomerTO.setBranchCode(getSelectedBranchID());
        objCustomerTO.setInitiatedBranch(ProxyParameters.BRANCH_ID);
        objCustomerTO.setLname(txtLastName);
        objCustomerTO.setFname(txtFirstName);
        objCustomerTO.setMname(txtMiddleName);
        if(getTdtjoiningDate()!=null && CommonUtil.convertObjToStr(getTdtjoiningDate()).length()>0&& !CommonUtil.convertObjToStr(getTdtjoiningDate()).equals("null")){
        objCustomerTO.setJoiningDate(DateUtil.getDateMMDDYYYY(getTdtjoiningDate()));
        }
        Date DobDt = DateUtil.getDateMMDDYYYY(tdtDateOfBirth);
        if (DobDt != null) {
            Date dobDate = (Date) curDate.clone();
            dobDate.setDate(DobDt.getDate());
            dobDate.setMonth(DobDt.getMonth());
            dobDate.setYear(DobDt.getYear());
//        objCustomerTO.setDob(DateUtil.getDateMMDDYYYY(tdtDateOfBirth));
            objCustomerTO.setDob(dobDate);
        } else {
            objCustomerTO.setDob(DateUtil.getDateMMDDYYYY(tdtDateOfBirth));
        }
        objCustomerTO.setRetDt(CommonUtil.getProperDate(curDate, DateUtil.getDateMMDDYYYY(getTdtRetireDt())));
        objCustomerTO.setRetAge(CommonUtil.convertObjToInt(getTxtRetireAge()));
//        objCustomerTO.setCustTypeId(getCustTypeId());
        if (screenCustType == INDIVIDUAL) {
            objCustomerTO.setCustTypeId(CommonUtil.convertObjToStr(getCbmCustomerType().getKeyForSelected()));
        } else if (screenCustType == CORPORATE) {
            if (actionType == ClientConstants.ACTIONTYPE_NEW) {
                objCustomerTO.setCustTypeId("OTHERS");
            } else {
                HashMap whereMap = new HashMap();
                whereMap.put("CUST_ID", getTxtCustomerID());
                List lst = (List) ClientUtil.executeQuery("getCustTypeID", whereMap);
                if (lst != null && lst.size() > 0) {
                    objCustomerTO.setCustTypeId(CommonUtil.convertObjToStr(lst.get(0)));
                }
            }
        }
        //        objCustomerTO.setNetworth(CommonUtil.convertObjToDouble("0"));
        objCustomerTO.setNetworth(CommonUtil.convertObjToDouble(this.txtNetWorth));
      
        Date NetDt = DateUtil.getDateMMDDYYYY(tdtNetWorthAsOn);
        if (NetDt != null) {
            Date netDate = (Date) curDate.clone();
            netDate.setDate(NetDt.getDate());
            netDate.setMonth(NetDt.getMonth());
            netDate.setYear(NetDt.getYear());
//        objCustomerTO.setNetworthAsOn(DateUtil.getDateMMDDYYYY(tdtNetWorthAsOn));
            objCustomerTO.setNetworthAsOn(netDate);
        } else {
            objCustomerTO.setNetworthAsOn(DateUtil.getDateMMDDYYYY(tdtNetWorthAsOn));
        }

        if (screenCustType == INDIVIDUAL) {
            if (rdoGender_Male) {
                objCustomerTO.setGender(objCustomerRB.getString("genderMale"));
            } else {
                objCustomerTO.setGender(objCustomerRB.getString("genderFeMale"));
            }

            if (rdoMaritalStatus_Single) {
                objCustomerTO.setMaritalstatus(objCustomerRB.getString("maritalStatusSingle"));
            }
            if (rdoMaritalStatus_Married) {
                objCustomerTO.setMaritalstatus(objCustomerRB.getString("maritalStatusMarried"));
            }
            if (rdoMaritalStatus_Widow) {
                objCustomerTO.setMaritalstatus(objCustomerRB.getString("maritalStatusWidow"));
            }
            objCustomerTO.setProfession(CommonUtil.convertObjToStr(getCbmProfession().getKeyForSelected()));
            if (isMinor) {
                objCustomerTO.setMinor(objCustomerRB.getString("minorYes"));
            } else {
                objCustomerTO.setMinor(objCustomerRB.getString("minorNo"));
            }
            if (isMinority) {
                objCustomerTO.setMinority(objCustomerRB.getString("minorityYes"));
            } else {
                objCustomerTO.setMinority(objCustomerRB.getString("minorityNo"));
            }
        }
        objCustomerTO.setAnnincome(CommonUtil.convertObjToStr(getCbmAnnualIncomeLevel().getKeyForSelected()));
        objCustomerTO.setEducation(CommonUtil.convertObjToStr(getCbmEducationalLevel().getKeyForSelected()));
        objCustomerTO.setVehicle(CommonUtil.convertObjToStr(getCbmVehicle().getKeyForSelected()));
        objCustomerTO.setRemarks(getTxtRemarks());
        objCustomerTO.setCompName(txtCompany);
        objCustomerTO.setCommAddrType(commAddrType);
        objCustomerTO.setEmailId(txtEmailID);
        objCustomerTO.setTitle(CommonUtil.convertObjToStr(getCbmTitle().getKeyForSelected()));
        objCustomerTO.setResidentialstatus(CommonUtil.convertObjToStr(getCbmResidentialStatus().getKeyForSelected()));
        objCustomerTO.setNationality(txtNationality);
        objCustomerTO.setLanguage(txtLanguage);
        objCustomerTO.setCustomergroup(CommonUtil.convertObjToStr(getCbmCustomerGroup().getKeyForSelected()));
        objCustomerTO.setRelationmanager(CommonUtil.convertObjToStr(getCbmRelationManager().getKeyForSelected()));
        objCustomerTO.setPrimaryOccupation(CommonUtil.convertObjToStr(getCbmPrimaryOccupation().getKeyForSelected()));
        objCustomerTO.setPreferredComm(CommonUtil.convertObjToStr(getCbmPrefCommunication().getKeyForSelected()));
        if (screenCustType == INDIVIDUAL) {
            objCustomerTO.setCaste(CommonUtil.convertObjToStr(getCbmCaste().getKeyForSelected()));
//            changed by nikhil
           objCustomerTO.setCboReligion(CommonUtil.convertObjToStr(getCbmReligion().getKeyForSelected()));
           objCustomerTO.setSubCaste(CommonUtil.convertObjToStr(getCbmSubCaste().getKeyForSelected()));
        }

        objCustomerTO.setIntroType(CommonUtil.convertObjToStr(getCbmIntroType().getKeyForSelected()));
        objCustomerTO.setSsn(txtSsn);
        objCustomerTO.setTransPwd(txtTransPwd);
        objCustomerTO.setCustUserid(txtCustUserid);
        objCustomerTO.setCustPwd(txtCustPwd);

        objCustomerTO.setAuthorizeCustId(txtAuthCustId);
        objCustomerTO.setWebsite(txtWebSite);



        objCustomerTO.setMembershipClass(CommonUtil.convertObjToStr(getCbmMembershipClass().getKeyForSelected()));
        objCustomerTO.setCareOf(CommonUtil.convertObjToStr(getCbmCareOf().getKeyForSelected()));
        objCustomerTO.setCareOfName(getTxtName());
        objCustomerTO.setCustomerStatus(CommonUtil.convertObjToStr(getCbmCustStatus().getKeyForSelected()));
        objCustomerTO.setRiskRating(getTxtRiskRate());
        objCustomerTO.setDesignation(getTxtDesignation());
        objCustomerTO.setPanNumber(getTxtPanNumber());

        Date CrAvDt = DateUtil.getDateMMDDYYYY(getTdtCrAvldSince());
        if (CrAvDt != null) {
            Date cravDate = (Date) curDate.clone();
            cravDate.setDate(CrAvDt.getDate());
            cravDate.setMonth(CrAvDt.getMonth());
            cravDate.setYear(CrAvDt.getYear());
//        objCustomerTO.setCrAvailedSince(DateUtil.getDateMMDDYYYY(getTdtCrAvldSince()));
            objCustomerTO.setCrAvailedSince(cravDate);
        } else {
            objCustomerTO.setCrAvailedSince(DateUtil.getDateMMDDYYYY(getTdtCrAvldSince()));
        }
        if (getChkAddrVerified()) {
            objCustomerTO.setAddrVerified(YES);
        } else {
            objCustomerTO.setAddrVerified(NO);
        }
        if (getChkPhVerified()) {
            objCustomerTO.setPhoneVerified(YES);
        } else {
            objCustomerTO.setPhoneVerified(NO);
        }
        if (getChkFinanceStmtVerified()) {
            objCustomerTO.setObtainFinstat(YES);
        } else {
            objCustomerTO.setObtainFinstat(NO);
        }
        if (getChkSentThanksLetter()) {
            objCustomerTO.setSendThanksLetter(YES);
        } else {
            objCustomerTO.setSendThanksLetter(NO);
        }
        if (getChkConfirmationfromIntroducer()) {
            objCustomerTO.setConfirmThanks(YES);
        } else {
            objCustomerTO.setConfirmThanks(NO);
        }
        objCustomerTO.setStatusBy(TrueTransactMain.USER_ID);
        objCustomerTO.setStatusDt(curDate);
        objCustomerTO.setAddrProof(CommonUtil.convertObjToStr(getCbmAddrProof().getKeyForSelected()));
        objCustomerTO.setIdenProof(CommonUtil.convertObjToStr(getCbmIdenProof().getKeyForSelected()));
        objCustomerTO.setKartha(txtKartha);
        objCustomerTO.setFarClass(CommonUtil.convertObjToStr(getCbmFarClass().getKeyForSelected()));
        objCustomerTO.setAge(CommonUtil.convertObjToInt(txtAge));
        objCustomerTO.setBankruptcy(bankruptcy);
        objCustomerTO.setMembershipNum(memberShipNo);
        objCustomerTO.setWardNo(txtWardNo);
        if (rdoITDec_pan) {
            objCustomerTO.setItDec(objCustomerRB.getString("pan"));
        } else if (rdoITDec_F60) {
            objCustomerTO.setItDec(objCustomerRB.getString("F60"));
        } else if (rdoITDec_F61) {
            objCustomerTO.setItDec(objCustomerRB.getString("F61"));
        }
        objCustomerTO.setDivision(getCboDivision());
        objCustomerTO.setAgentCustId(getLblAgentCustIdValue());
    }

    /**
     * Sets the to variables for Gaurdian details *
     */
    private void setGaurdianData() {
        objCustomerGaurdianTO = new CustomerGaurdianTO();
        if (actionType == ClientConstants.ACTIONTYPE_EDIT) {
            objCustomerGaurdianTO.setCustId(txtCustomerID);
        }
        if (isNewGuardian()) {
            objCustomerGaurdianTO.setCommand(CommonConstants.TOSTATUS_INSERT);
        }
        objCustomerGaurdianTO.setGuardianName(CommonUtil.convertObjToStr(txtGuardianNameNO));
        objCustomerGaurdianTO.setStreet(CommonUtil.convertObjToStr(txtGStreet));
        objCustomerGaurdianTO.setArea(CommonUtil.convertObjToStr(txtGArea));
        objCustomerGaurdianTO.setCity(CommonUtil.convertObjToStr(cbmGCity.getKeyForSelected()));
        objCustomerGaurdianTO.setState(CommonUtil.convertObjToStr(cbmGState.getKeyForSelected()));
        objCustomerGaurdianTO.setPinCode(CommonUtil.convertObjToStr(txtGPinCode));
        objCustomerGaurdianTO.setAreaCode(CommonUtil.convertObjToStr(txtGuardianACodeNO));
        objCustomerGaurdianTO.setPhNo(CommonUtil.convertObjToStr(txtGuardianPhoneNO));
        objCustomerGaurdianTO.setRelationship(CommonUtil.convertObjToStr(cbmRelationNO.getKeyForSelected()));
        objCustomerGaurdianTO.setCountryCode(CommonUtil.convertObjToStr(cbmGCountry.getKeyForSelected()));
        Date GuardinDobDt = DateUtil.getDateMMDDYYYY(tdtGuardianDOB);
        if (GuardinDobDt != null) {
            Date dobDate = (Date) curDate.clone();
            dobDate.setDate(GuardinDobDt.getDate());
            dobDate.setMonth(GuardinDobDt.getMonth());
            dobDate.setYear(GuardinDobDt.getYear());

            objCustomerGaurdianTO.setGuardianDob(dobDate);
        } else {
            objCustomerGaurdianTO.setGuardianDob(DateUtil.getDateMMDDYYYY(tdtGuardianDOB));
        }
        objCustomerGaurdianTO.setGuardianAge(CommonUtil.convertObjToInt(txtGuardianAge));
    }

    private CustRegionalTo setCustRegionalTo() {
        if (isChkRegioalLang() == true) {
            CustRegionalTo objTo = new CustRegionalTo();
            objTo.setFname(getTxtRegMName());
            objTo.setHouseName(getTxtRegMaHname());
            objTo.setPlace(getTxtRegMaPlace());
            objTo.setVillage(getTxtRegMavillage());
            objTo.setCareOfName(getTxtRegMaGardName());
            objTo.setCity(getTxtRegMaCity());
            objTo.setTaluk(getTxtRegMaTaluk());
            objTo.setCountry(getTxtRegMaCountry());
            objTo.setState(getTxtRegMaState());
            objTo.setAmsam(getTxtRegMaAmsam());
            objTo.setDesam(getTxtRegMaDesam());
            return objTo;
        } else {
            return null;
        }
    }
    /**
     * Method used to reset the GaurdianDetails Fields *
     */
    private void resetGaurdianDetails() {
        setCboRelationNO("");
        setTxtGuardianNameNO("");
        setTxtGuardianACodeNO("");
        setTxtGuardianPhoneNO("");
        setTxtGStreet("");
        setTxtGArea("");
        setCboGCountry("");
        setCboGState("");
        setCboGCity("");
        setTxtGPinCode("");
        setTdtGuardianDOB("");
        setTxtGuardianAge("");
    }

    public void resetForm() {
        resetVariables();
        resetCustomerDetails();
        resetCustDetails();
        resetJntAccntHoldTbl();
        resetAddressDetails();
        resetAddressListTable();
        resetPhoneDetails();
        resetPhoneListTable();
        resetCustomerHistoryTable();
        resetGaurdianDetails();
        resetNewIncomeParticulars();
        resetIncomeListTable();
        resetLandDetailsListTable();
        resetNewLandDetails();
        resetProofListTable();
        resetRegionallanguages();
        notifyObservers();
    }

    public void resetNewAddress() {
        resetPhoneListTable();
        resetPhoneDetails();
        resetAddressDetails();
        ttNotifyObservers();
    }
     public void resetNewProofDetails() {
         setCboIdenProof("");
         setTxtUniqueId("");
        ttNotifyObservers();
    }
    public void resetNewIncomeParticulars() {
        setTxtIncAmount("");
        setTxtIncName("");
        setCboIncPack("");
        setCboIncRelation("");
        setTxtSlNo("");
        setCboIncProfession("");
    }

    public void resetNewLandDetails() {
        setTxtLocation("");
        setTxtLandDetArea("");
        setTxtSurNo("");
        setCboIrrigated("");
        setCboSourceIrrigated("");
        setTxtVillage("");
        setTxtPost("");
        setTxtHobli("");
        setCboTaluk("");
        setCboDistrict("");
        setCboLandDetState("");
        setTxtLandDetSlNo("");
        setTxtLandDetPin("");
    }
    public void resetRegionallanguages() {
        setTxtRegMName("");
        setTxtRegMaHname("");
        setTxtRegMaPlace("");
        setTxtRegMavillage("");
        setTxtRegMaTaluk("");
        setTxtRegMaCity("");
        setTxtRegMaCountry("");
        setTxtRegMaState("");
        setTxtRegMaAmsam("");
        setTxtRegMaDesam("");
        setTxtRegMaGardName("");
        setRegionalData(null);
        setChkRegioalLang(false);
    }
    public void resetDeleteAddress() {
        try {
            resetPhoneListTable();
            resetPhoneDetails();
            resetAddressDetails();
            resetAddressListTable();
            populateContactsTable();
            ttNotifyObservers();
        } catch (Exception e) {
            parseException.logException(e, true);
        }
    }

    public void resetDeletePhoneDetails() {
        try {
            resetPhoneListTable();
            resetPhoneDetails();
            populatePhoneTable();
            ttNotifyObservers();
        } catch (Exception e) {
            parseException.logException(e, true);
        }
    }

    
    public void resetDeleteProofDetails() {
        try {
            resetProofListTable();
            populateProofTable();
            ttNotifyObservers();
        } catch (Exception e) {
            parseException.logException(e, true);
        }
    }
    
    public void resetPhoneDetailsNotify() {
        resetPhoneDetails();
        ttNotifyObservers();
    }

    /**
     * To add data to PhoneList
     */
    public void addPhoneList(boolean phoneExist, int phoneRow) {
        try {
            CustomerPhoneTO objCustomerPhoneTO = new CustomerPhoneTO();
            if (phoneList == null) {
                phoneList = new HashMap();
            }
            if (actionType == ClientConstants.ACTIONTYPE_EDIT) {
                objCustomerPhoneTO.setCustId(txtCustomerID);
            }
            if (phoneExist) {
                Double phoneId = ((CustomerPhoneTO) phoneList.get(tblPhoneList.getValueAt(phoneRow, 0))).getPhoneId();
                if (phoneId != null) {
                    objCustomerPhoneTO.setPhoneId(phoneId);
                } else {
                    objCustomerPhoneTO.setPhoneId((Double) tblPhoneList.getValueAt(phoneRow, 0));
                }
                if (getActionType() == ClientConstants.ACTIONTYPE_EDIT) {
                    if (!isNewAddress()) {
                        objCustomerPhoneTO.setStatus(CommonConstants.STATUS_MODIFIED);
                        objCustomerPhoneTO.setStatusBy(TrueTransactMain.USER_ID);
                        objCustomerPhoneTO.setStatusDt(curDate);
                    } else {
                        objCustomerPhoneTO.setStatus(CommonConstants.STATUS_CREATED);
                        objCustomerPhoneTO.setStatusBy(TrueTransactMain.USER_ID);
                        objCustomerPhoneTO.setStatusDt(curDate);
                    }
                }
            } else if (phoneExist == false) {
                double big = 0;
                if (tblPhoneList.getRowCount() > 0) {//If rowcont is greater than zero, get the first column value, which gives the
                    //gives the phone type id, so finding the greatest phoneID, and its incremented by one and its set as phoneId for the newly entered phone Details
                    for (int i = 0; i < tblPhoneList.getRowCount(); i++) {
                        Double tblPhoneId = (Double) tblPhoneList.getValueAt(i, 0);
                        if (tblPhoneId.doubleValue() > big) {
                            big = tblPhoneId.doubleValue();
                        }
                    }
                    objCustomerPhoneTO.setPhoneId(new Double(big + 1));
                } else if (tblPhoneList.getRowCount() == 0) {
                    objCustomerPhoneTO.setPhoneId(new Double(phoneRow + 1));
                }
                objCustomerPhoneTO.setStatus(CommonConstants.STATUS_CREATED);
                objCustomerPhoneTO.setStatusBy(TrueTransactMain.USER_ID);
                objCustomerPhoneTO.setStatusDt(curDate);
            }
            objCustomerPhoneTO.setPhoneTypeId(CommonUtil.convertObjToStr(getCbmPhoneType().getKeyForSelected()));
            objCustomerPhoneTO.setAreaCode(txtAreaCode);
            objCustomerPhoneTO.setPhoneNumber(txtPhoneNumber);
            objCustomerPhoneTO.setAddrType(CommonUtil.convertObjToStr(getCbmAddressType().getKeyForSelected()));
            phoneList.put(objCustomerPhoneTO.getPhoneId(), objCustomerPhoneTO);
            updateTblPhoneList(phoneRow, phoneExist);
            objCustomerPhoneTO = null;
            resetPhoneDetails();
        } catch (Exception e) {
            parseException.logException(e, true);
        }
    }

    /**
     * To populate phone rows
     */
    private void updateTblPhoneList(int row, boolean phoneExist) throws Exception {
        Object selectedRow = null;
        boolean rowExists = false;
        if (phoneExist) {
            tblPhoneList.setValueAt(getCboPhoneType(), row, 1);
            tblPhoneList.setValueAt(txtPhoneNumber, row, 2);
        } else {
            final ArrayList phoneRow = new ArrayList();
            double big = 0;
            if (tblPhoneList.getRowCount() > 0) {//If rowcont is greater than zero, get the first column value, which gives the
                //gives the phone type id, so finding the greatest phoneID, and its incremented by one and its set as phoneId for the newly entered phone Details
                for (int i = 0; i < tblPhoneList.getRowCount(); i++) {
                    Double tblPhoneId = (Double) tblPhoneList.getValueAt(i, 0);
                    if (tblPhoneId.intValue() > big) {
                        big = tblPhoneId.doubleValue();
                    }
                }
                phoneRow.add(new Double(big + 1));
            } else if (tblPhoneList.getRowCount() == 0) {
                phoneRow.add(new Double(row + 1));
            }
            phoneRow.add(getCboPhoneType());
            phoneRow.add(txtPhoneNumber);
            tblPhoneList.insertRow(tblPhoneList.getRowCount(), phoneRow);
        }
    }

    /**
     * To add address data
     */
    public void addAddressMap() {
        try {
            final CustomerAddressTO objCustomerAddressTO = new CustomerAddressTO();
            if (addressMap == null) {
                addressMap = new HashMap();
            }
            if (phoneMap == null) {
                phoneMap = new HashMap();
            }
            objCustomerAddressTO.setBranchCode(getSelectedBranchID());
            //  objCustomerAddressTO.setInitiatedBranch(ProxyParameters.BRANCH_ID);
            if (getActionType() == ClientConstants.ACTIONTYPE_EDIT) {
                if (isNewAddress()) {
                    objCustomerAddressTO.setStatus(CommonConstants.STATUS_CREATED);
                    objCustomerAddressTO.setStatusBy(TrueTransactMain.USER_ID);
                    objCustomerAddressTO.setStatusDt(curDate);
                } else {
                    objCustomerAddressTO.setStatus(CommonConstants.STATUS_MODIFIED);
                    objCustomerAddressTO.setStatusBy(TrueTransactMain.USER_ID);
                    objCustomerAddressTO.setStatusDt(curDate);
                }
            } else {
                objCustomerAddressTO.setStatus(CommonConstants.STATUS_CREATED);
            }
            if (actionType == ClientConstants.ACTIONTYPE_EDIT) {
                objCustomerAddressTO.setCustId(txtCustomerID);
            }
            objCustomerAddressTO.setStreet(txtStreet);
            objCustomerAddressTO.setArea(txtArea);
            objCustomerAddressTO.setCity(CommonUtil.convertObjToStr(getCbmCity().getKeyForSelected()));
            objCustomerAddressTO.setState(CommonUtil.convertObjToStr(getCbmState().getKeyForSelected()));
            objCustomerAddressTO.setPostOffice(CommonUtil.convertObjToStr(getCbmPostOffice().getSelectedItem()));
            objCustomerAddressTO.setPinCode(txtPincode);
            objCustomerAddressTO.setCountryCode(CommonUtil.convertObjToStr(getCbmCountry().getKeyForSelected()));
            objCustomerAddressTO.setAddrType(CommonUtil.convertObjToStr(getCbmAddressType().getKeyForSelected()));
            objCustomerAddressTO.setRemarks(getTxtAddrRemarks());
            objCustomerAddressTO.setVillage(CommonUtil.convertObjToStr(getCbmCustVillage().getKeyForSelected()));
            objCustomerAddressTO.setTaluk(CommonUtil.convertObjToStr(getCbmCustTaluk().getKeyForSelected()));
            objCustomerAddressTO.setWardName(CommonUtil.convertObjToStr(getCbmWardName().getKeyForSelected())); // Added by nithya
            addressMap.put(CommonUtil.convertObjToStr(cbmAddressType.getKeyForSelected()), objCustomerAddressTO);
            regionalData = new HashMap();
            if (CommonUtil.convertObjToStr(cbmAddressType.getKeyForSelected()) != null
                    && CommonUtil.convertObjToStr(cbmAddressType.getKeyForSelected()).equalsIgnoreCase("Home")) {
                regionalData.put("HNAME", txtStreet);
                regionalData.put("PLACE", txtArea);
                regionalData.put("CITY", CommonUtil.convertObjToStr(getCbmCity().getSelectedItem()));
                regionalData.put("STATE", CommonUtil.convertObjToStr(getCbmState().getSelectedItem()));
                regionalData.put("COUNTRY", CommonUtil.convertObjToStr(getCbmCountry().getSelectedItem()));
                regionalData.put("VILLAGE", CommonUtil.convertObjToStr(getCbmCustVillage().getSelectedItem()));
                regionalData.put("TALUK", CommonUtil.convertObjToStr(getCbmCustTaluk().getSelectedItem()));
                // Added by nithya
                regionalData.put("WARDNAME",CommonUtil.convertObjToStr(getCbmWardName().getSelectedItem()));
            }
            System.out.println("regionalData new ::"+regionalData);
            if (phoneList != null) {
                phoneMap.put(CommonUtil.convertObjToStr(cbmAddressType.getKeyForSelected()), phoneList);
            }
            updateTblContactList();
            ttNotifyObservers();
        } catch (Exception e) {
            e.printStackTrace();
            parseException.logException(e, true);
        }
    }    
      /**
     * To add proof data  added by rishad 05/03/2015 for keeping data of proof details of customer
     */
    public void addProofMap(boolean proofExist, int proofRow) {
        try {
            final CustomerProofTo objCustomerProofTo = new CustomerProofTo();
            if (proofMap == null) {
                proofMap = new HashMap();
            }
            if (proofExist) {
                objCustomerProofTo.setStatus(CommonConstants.STATUS_MODIFIED);
            } else {
                if (getActionType() == ClientConstants.ACTIONTYPE_EDIT) {
                    if (isNewProof()) {
                        objCustomerProofTo.setStatus(CommonConstants.STATUS_CREATED);
                    } else {
                        objCustomerProofTo.setStatus(CommonConstants.STATUS_MODIFIED);
                    }

                } else {
                    objCustomerProofTo.setStatus(CommonConstants.STATUS_CREATED);
                }
                if (actionType == ClientConstants.ACTIONTYPE_EDIT) {
                    objCustomerProofTo.setCustId(txtCustomerID);
                }
            }
            objCustomerProofTo.setUniqueId(getTxtUniqueId());
            objCustomerProofTo.setProofType(CommonUtil.convertObjToStr(getCbmIdenProof().getKeyForSelected()));
            proofMap.put(CommonUtil.convertObjToStr(cbmIdenProof.getKeyForSelected()), objCustomerProofTo);
            updateTblProofList(proofRow, proofExist);
            ttNotifyObservers();
        } catch (Exception e) {
            parseException.logException(e, true);
        }
    }

    public void addIncomeParMap(int rowSelected, boolean updateMode) {
        try {
            int rowSel = rowSelected;
            String inc = "INCOME";
            final CustomerIncomeParticularsTO objCustomerIncomeParticularsTO = new CustomerIncomeParticularsTO();
            if (IncParMap == null) {
                IncParMap = new LinkedHashMap();
            }
            if (getActionType() == ClientConstants.ACTIONTYPE_EDIT) {
                if (isNewIncomeDet()) {
                    objCustomerIncomeParticularsTO.setStatus(CommonConstants.STATUS_CREATED);
                } else {
                    objCustomerIncomeParticularsTO.setStatus(CommonConstants.STATUS_MODIFIED);
                }
            } else {
                objCustomerIncomeParticularsTO.setStatus(CommonConstants.STATUS_CREATED);
            }
            if (actionType == ClientConstants.ACTIONTYPE_EDIT) {
                objCustomerIncomeParticularsTO.setCustId(txtCustomerID);
            }
            int slno = 0;
            int nums[] = new int[50];
            int max = nums[0];
            if (!updateMode) {
                ArrayList data = tblIncomeParticulars.getDataArrayList();
                slno = serialNo(data, inc);
//            ArrayList data = tblIncomeParticulars.getDataArrayList();
//            final int dataSize = data.size();
//              slno=dataSize+1;
//            for(int i=0;i<data.size();i++){
//                int a=CommonUtil.convertObjToInt(tblIncomeParticulars.getValueAt(i,0));
//               nums[i]=a;
//               if(nums[i]>max)
//                   max=nums[i];
//               slno=max+1;
//            }
            } else {
                if (isNewIncomeDet()) {
                    ArrayList data = tblCustomerLandDetails.getDataArrayList();
                    slno = serialNo(data, inc);
                } else {
                    int b = CommonUtil.convertObjToInt(tblIncomeParticulars.getValueAt(rowSelected, 0));
                    slno = b;
                }
            }

            objCustomerIncomeParticularsTO.setSlno(String.valueOf(slno));
            objCustomerIncomeParticularsTO.setIncAmount(txtIncAmount);
            objCustomerIncomeParticularsTO.setIncName(txtIncName);
            objCustomerIncomeParticularsTO.setIncPackage(CommonUtil.convertObjToStr(getCbmIncPack().getKeyForSelected()));
            objCustomerIncomeParticularsTO.setIncRelation(CommonUtil.convertObjToStr(getCbmIncRelation().getKeyForSelected()));
            objCustomerIncomeParticularsTO.setIncDetProfession(CommonUtil.convertObjToStr(getCbmIncProfession().getKeyForSelected()));
            objCustomerIncomeParticularsTO.setIncDetCompany(incCompany);
            IncParMap.put(objCustomerIncomeParticularsTO.getSlno(), objCustomerIncomeParticularsTO);
            String sno = String.valueOf(slno);
            updateTblIncomeParList(rowSel, sno);
            ttNotifyObservers();
        } catch (Exception e) {
            parseException.logException(e, true);
        }
    }

    public int serialNo(ArrayList data, String str) {
        final int dataSize = data.size();
        int nums[] = new int[50];
        int max = nums[0];
        int slno = 0;
        int a = 0;
        slno = dataSize + 1;
        for (int i = 0; i < data.size(); i++) {
            if (str.equals("LAND")) {
                a = CommonUtil.convertObjToInt(tblCustomerLandDetails.getValueAt(i, 0));
            } else if (str.equals("INCOME")) {
                a = CommonUtil.convertObjToInt(tblIncomeParticulars.getValueAt(i, 0));
            }
            nums[i] = a;
            if (nums[i] > max) {
                max = nums[i];
            }
            slno = max + 1;
        }
        return slno;
    }

    public void addLandDetMap(int rowSelected, boolean updateMode) {
        try {
            int rowSel = rowSelected;
            String inc = "LAND";
            final CustomerLandDetailsTO objCustomerLandDetailsTO = new CustomerLandDetailsTO();
            if (LandDetMap == null) {
                LandDetMap = new LinkedHashMap();
            }
            if (getActionType() == ClientConstants.ACTIONTYPE_EDIT) {
                if (isNewLandDet()) {
                    objCustomerLandDetailsTO.setStatus(CommonConstants.STATUS_CREATED);
                } else {
                    objCustomerLandDetailsTO.setStatus(CommonConstants.STATUS_MODIFIED);
                }
            } else {
                objCustomerLandDetailsTO.setStatus(CommonConstants.STATUS_CREATED);
            }
            if (actionType == ClientConstants.ACTIONTYPE_EDIT) {
                objCustomerLandDetailsTO.setCustId(txtCustomerID);
            }
            int slno = 0;
//            int nums[]= new int[50];
//            int max=nums[0];
            if (!updateMode) {
                ArrayList data = tblCustomerLandDetails.getDataArrayList();
                slno = serialNo(data, inc);
//            final int dataSize = data.size();
//              slno=dataSize+1;
//            for(int i=0;i<data.size();i++){
//                int a=CommonUtil.convertObjToInt(tblCustomerLandDetails.getValueAt(i,0));
//               nums[i]=a;
//               if(nums[i]>max)
//                   max=nums[i];
//               slno=max+1;
//            }
            } else {
                if (isNewLandDet()) {
                    ArrayList data = tblCustomerLandDetails.getDataArrayList();
                    slno = serialNo(data, inc);
                } else {
                    int b = CommonUtil.convertObjToInt(tblCustomerLandDetails.getValueAt(rowSelected, 0));
                    slno = b;
                }
            }

            objCustomerLandDetailsTO.setSlno(String.valueOf(slno));
            objCustomerLandDetailsTO.setLocation(txtLocation);
            objCustomerLandDetailsTO.setSurvey_no(txtSurNo);
            objCustomerLandDetailsTO.setArea(txtLandDetArea);
            objCustomerLandDetailsTO.setIrrigated(CommonUtil.convertObjToStr(getCbmIrrigated().getKeyForSelected()));
            objCustomerLandDetailsTO.setIrrSrc(CommonUtil.convertObjToStr(getCbmSourceIrrigated().getKeyForSelected()));
            objCustomerLandDetailsTO.setVillage(txtVillage);
            objCustomerLandDetailsTO.setPost(txtPost);
            objCustomerLandDetailsTO.setHobli(txtHobli);
            objCustomerLandDetailsTO.setState((String) cbmLandDetState.getKeyForSelected());
            objCustomerLandDetailsTO.setDistrict(CommonUtil.convertObjToStr(getCbmDistrict().getKeyForSelected()));
            objCustomerLandDetailsTO.setTaluk(CommonUtil.convertObjToStr(getCbmTaluk().getKeyForSelected()));
            objCustomerLandDetailsTO.setPin(txtLandDetPin);
            LandDetMap.put(objCustomerLandDetailsTO.getSlno(), objCustomerLandDetailsTO);
            String sno = String.valueOf(slno);
            updateTblLandDetList(rowSel, sno);
            ttNotifyObservers();
        } catch (Exception e) {
            parseException.logException(e, true);
        }
    }

    /**
     * To populate Contact Address table
     */
    private void updateTblContactList() throws Exception {
        Object selectedRow;
        boolean rowExists = false;
        //If row already exists update it, else create a new row & append
        for (int i = tblContactList.getRowCount(), j = 0; i > 0; i--, j++) {
            selectedRow = ((ArrayList) tblContactList.getDataArrayList().get(j)).get(0);
            if ((CommonUtil.convertObjToStr(cbmAddressType.getKeyForSelected())).equals(CommonUtil.convertObjToStr(selectedRow))) {
                rowExists = true;
            }
        }
        if (!rowExists) {
            ArrayList addressRow = new ArrayList();
            addressRow.add(CommonUtil.convertObjToStr(cbmAddressType.getKeyForSelected()));
            addressRow.add("");
            tblContactList.insertRow(tblContactList.getRowCount(), addressRow);
            addressRow = null;
        }
    }
    
    //added by rishad 04/03/2015 for related customer proof details storing
    private void updateTblProofList(int row, boolean proofExist) throws Exception {
        Object selectedRow;
        boolean rowExists = false;
        //If row already exists update it, else create a new row & append
        if (proofExist) {
            tblProofList.setValueAt(CommonUtil.convertObjToStr(cbmIdenProof.getKeyForSelected()), row, 0);
            tblProofList.setValueAt(txtUniqueId, row, 1);
        } else {
            for (int i = tblProofList.getRowCount(), j = 0; i > 0; i--, j++) {
                selectedRow = ((ArrayList) tblProofList.getDataArrayList().get(j)).get(0);
                if ((CommonUtil.convertObjToStr(cbmIdenProof.getKeyForSelected())).equals(CommonUtil.convertObjToStr(selectedRow))) {
                    rowExists = true;
                }
            }
            if (!rowExists) {
                ArrayList proofRow = new ArrayList();
                proofRow.add(CommonUtil.convertObjToStr(cbmIdenProof.getKeyForSelected()));
                proofRow.add(getTxtUniqueId());
                tblProofList.insertRow(tblProofList.getRowCount(), proofRow);
                proofRow = null;
            }
        }
    }

    private void updateTblIncomeParList(int rowSel, String sno) throws Exception {
        Object selectedRow;
        boolean rowExists = false;
        //If row already exists update it, else create a new row & append
        for (int i = tblIncomeParticulars.getRowCount(), j = 0; i > 0; i--, j++) {
            selectedRow = ((ArrayList) tblIncomeParticulars.getDataArrayList().get(j)).get(0);
            if (sno.equals(CommonUtil.convertObjToStr(selectedRow))) {
                rowExists = true;
                ArrayList IncParRow = new ArrayList();
                ArrayList data = tblIncomeParticulars.getDataArrayList();
                data.remove(rowSel);
                IncParRow.add(sno);
                IncParRow.add(txtIncName);
                IncParRow.add(CommonUtil.convertObjToStr(cbmIncRelation.getKeyForSelected()));
                IncParRow.add(CommonUtil.convertObjToStr(cbmIncProfession.getKeyForSelected()));
                IncParRow.add(incCompany);
                IncParRow.add(txtIncAmount);
                IncParRow.add(CommonUtil.convertObjToStr(cbmIncPack.getKeyForSelected()));
                tblIncomeParticulars.insertRow(rowSel, IncParRow);
                IncParRow = null;
            }
        }
        if (!rowExists) {
            ArrayList IncParRow = new ArrayList();
            IncParRow.add(String.valueOf(sno));
            IncParRow.add(txtIncName);
            IncParRow.add(CommonUtil.convertObjToStr(cbmIncRelation.getKeyForSelected()));
            IncParRow.add(CommonUtil.convertObjToStr(cbmIncProfession.getKeyForSelected()));
            IncParRow.add(incCompany);
            IncParRow.add(txtIncAmount);
            IncParRow.add(CommonUtil.convertObjToStr(cbmIncPack.getKeyForSelected()));
            tblIncomeParticulars.insertRow(tblIncomeParticulars.getRowCount(), IncParRow);
            IncParRow = null;
        }
    }

    private void updateTblLandDetList(int rowSel, String sno) throws Exception {
        Object selectedRow;
        boolean rowExists = false;
        //If row already exists update it, else create a new row & append
        for (int i = tblCustomerLandDetails.getRowCount(), j = 0; i > 0; i--, j++) {
            selectedRow = ((ArrayList) tblCustomerLandDetails.getDataArrayList().get(j)).get(0);
            if (sno.equals(CommonUtil.convertObjToStr(selectedRow))) {
                rowExists = true;
                ArrayList LandRow = new ArrayList();
                ArrayList data = tblCustomerLandDetails.getDataArrayList();
                data.remove(rowSel);
                LandRow.add(sno);
                LandRow.add(txtLocation);
                LandRow.add(txtSurNo);
                LandRow.add(txtLandDetArea);
                LandRow.add(CommonUtil.convertObjToStr(cbmIrrigated.getKeyForSelected()));
                LandRow.add(CommonUtil.convertObjToStr(cbmSourceIrrigated.getKeyForSelected()));
                LandRow.add(txtVillage);
                LandRow.add(txtPost);
                LandRow.add(txtHobli);
                tblCustomerLandDetails.insertRow(rowSel, LandRow);
                LandRow = null;
            }
        }
        if (!rowExists) {
            ArrayList LandRow = new ArrayList();
//            ArrayList data = tblIncomeParticulars.getDataArrayList();
//            final int dataSize = data.size();
//            int slno=dataSize+1;
            LandRow.add(String.valueOf(sno));
            LandRow.add(txtLocation);
            LandRow.add(txtSurNo);
            LandRow.add(txtLandDetArea);
            LandRow.add(CommonUtil.convertObjToStr(cbmIrrigated.getKeyForSelected()));
            LandRow.add(CommonUtil.convertObjToStr(cbmSourceIrrigated.getKeyForSelected()));
            LandRow.add(txtVillage);
            LandRow.add(txtPost);
            LandRow.add(txtHobli);
            tblCustomerLandDetails.insertRow(tblCustomerLandDetails.getRowCount(), LandRow);
            LandRow = null;
        }
    }

    public void resetAddressDetails() {
        getCbmAddressType().setKeyForSelected("");
        resetAddressExceptAddTypeDetails();
    }

    public void resetAddressExceptAddTypeDetails() {
        setTxtStreet("");
        setTxtArea("");
        setCboCity("");
        setCboState("");
        setTxtPincode("");
        setCboCountry("");
        setCboCustTaluk("");
        setCboCustVillage("");
        setTxtAddrRemarks("");
        setCboPostOffice("");
        setCboWardName(""); // Added by nithya
    }

    public void resetAddressListTable() {
        for (int i = tblContactList.getRowCount(); i > 0; i--) {
            tblContactList.removeRow(0);
        }
    }
    
    public void resetProofListTable() {
        for (int i = tblProofList.getRowCount(); i > 0; i--) {
            tblProofList.removeRow(0);
        }
    }
    public void resetIncomeListTable() {
        for (int i = tblIncomeParticulars.getRowCount(); i > 0; i--) {
            tblIncomeParticulars.removeRow(0);
        }
    }

    public void resetLandDetailsListTable() {
        for (int i = tblCustomerLandDetails.getRowCount(); i > 0; i--) {
            tblCustomerLandDetails.removeRow(0);
        }
    }

    public void resetPhoneDetails() {
        setTxtAreaCode("");
        setTxtPhoneNumber("");
        setCboPhoneType("");
    }

    public void resetPhoneListTable() {
        for (int i = tblPhoneList.getRowCount(); i > 0; i--) {
            tblPhoneList.removeRow(0);
        }
    }

    public void resetCustomerHistoryTable() {
        for (int i = tbmCustomerHistory.getRowCount(); i > 0; i--) {
            tbmCustomerHistory.removeRow(0);
        }
    }

    public void resetAddressRelatedDetails() {
        resetAddressExceptAddTypeDetails();
        resetPhoneDetails();
        resetPhoneListTable();
    }

    public void resetCustomerDetails() {
        setCbcomboAmsam("");
        setCboWardName(""); // Added by nithya
        setCbcomboDesam("");
        setTxtNationality("");
        setTxtLanguage("");
        setTdtDateOfBirth("");
        setTxtCustomerID("");
        setTxtRemarks("");
        setCboResidentialStatus("");
        getCbmResidentialStatus().setKeyForSelected("RESIDENT");
        isMinor = false;
        isMinority=false;
        isPassport = false;
        setCboCustomerType("");
        setCustTypeId("");
        setCboRelationManager("");
        setTxtCompany("");
        setCboAnnualIncomeLevel("");
        setCboPrefCommunication("");
        setCboCustomerGroup("");
        setCboVehicle("");
        setCboProfession("");
        setCboPrimaryOccupation("");
        setChkAddrVerified(false);
        setChkIncomeDetails(false);
        setChkLandDetails(false);
        setChkPhVerified(false);
        setChkFinanceStmtVerified(false);
        setCboCaste("");
        setCboReligion("");
        setTxtPanNumber("");
        setChkMinor(false);
        setChkMinority(false);
        setIsStaff(false);
        setCboIntroType("");
        getCbmIntroType().setKeyForSelected("");
        setCboEducationalLevel("");
        setTxtEmailID("");
        setTxtNetWorth("");
        setTdtNetWorthAsOn("");
        setLblCustomerStatus("");
        setLblPhoto("");
        setPhotoByteArray(null);
        setProofPhotoByteArray(null);
        setProofPhotoFile("");
        setLblSign("");
        setSignByteArray(null);
        setTxtSsn("");
        setTxtTransPwd("");
        setTxtCustUserid("");
        setTxtCustPwd("");
        setTxtWebSite("");
        setCboCareOf("");
        setTxtName("");
        setCboMembershipClass("");
        setCboCustStatus("");
        setTdtCrAvldSince("");
        setTxtRiskRate("");
        setChkAddrVerified(false);
        setChkMinor(false);
        setIsMinority(false);
        setChkPhVerified(false);
        setChkFinanceStmtVerified(false);
        setTxtBranchId("");
        setChkSentThanksLetter(false);
        setChkConfirmationfromIntroducer(false);
        setRdoMaritalStatus_Married(false);
        setRdoMaritalStatus_Single(false);
        setRdoMaritalStatus_Widow(false);
        setRdoGender_Male(false);
        setRdoGender_Female(false);
        setTxtStaffId("");
        setTxtDesignation("");
        resetAuthCustDetails();
        /* FINANCIAL DETAILS */

        setTxtAuthCapital("");
        setTxtIssuedCapital("");
        setTxtSubscribedCapital("");
        setTxtTotalResource("");
        setTxtLastYrPL("");
        setTxtDividendPercentage("");
        setTxtTaxliability("");
        setTxtTotalIncome("");
        setTxtTotalNonTaxExp("");
        setTxtprofitBefTax("");
        setTdtFinacialYrEnd("");
        setCboBusNature("");
        setTdtDtEstablished("");
        setTxtRegNumber("");
        setTdtDtEstablished("");
        setTxtCEO("");
        setLblCreatedDt1("");
        setLblDealingPeriod("");
        setCboAddrProof("");
        setCboIdenProof("");
        setTxtPassportFirstName("");
        setTxtPassportMiddleName("");
        setTxtPassportLastName("");
        setTxtPassportIssueAuth("");
        setTxtPassportNo("");
        setTdtPassportIssueDt("");
        setTdtPassportValidUpto("");
        setCboPassportIssuePlace("");
        setCboPassportTitle("");
        setTxtKartha("");
        setCboFarClass("");
        setTxtAge("");
        setSusRevRemarks("");
        setChkrevokedBy(false);
        setChksuspendedBy(false);
        setSuspendedDate("");
        setRevokedDate("");
        setBankruptcy("");
        setMemberShipNo("");
        setRdoITDec_F60(false);
        setRdoITDec_F61(false);
        setRdoITDec_pan(false);
        setTxtWardNo("");
        setCboDivision("");
        setCboAgentCustId("");
        setTxtRetireAge("");
        setTdtRetireDt("");
    }

    //--- resets the Customer Details
    public void resetAuthCustDetails() {
        setTxtAuthCustId("");
        setCboTitle("");
        setTxtFirstName("");
        setTxtMiddleName("");
        setTxtLastName("");
        rdoGender_Male = false;
        rdoGender_Female = false;


    }

    /**
     * To set communication address type
     */
    public void setCommunicationAddress(int row) {
        try {
            this.setCommAddrType(CommonUtil.convertObjToStr(tblContactList.getValueAt(row, ADDRTYPE_COLNO)));
            final int tblContactListRowCount = tblContactList.getRowCount();
            for (int i = 0; i < tblContactListRowCount; i++) {
                tblContactList.setValueAt(EMPTY_STRING, i, STATUS_COLNO);
            }
            tblContactList.setValueAt(PRIMARY, row, STATUS_COLNO);
        } catch (Exception e) {
            parseException.logException(e, true);
        }
    }

    /**
     * To get the data from server for a particular customer & populate
     */
    public void getData(HashMap whereMap, IntroducerOB introducerOB) {
        try {
            whereMap.put(CommonConstants.BRANCH_ID, getSelectedBranchID());
            data = (HashMap) proxy.executeQuery(whereMap, map);
            String introType = null;
            if (data.containsKey("CUSTOMER")) {
                objCustomerTO = (CustomerTO) data.get("CUSTOMER");
                introType = CommonUtil.convertObjToStr(objCustomerTO.getIntroType());
                if (data.containsKey("ADDRESS")) {
                    addressMap = (HashMap) data.get("ADDRESS");
                    ArrayList addList = new ArrayList(addressMap.keySet());
                    for (int i = 0; i < addList.size(); i++) {
                        CustomerAddressTO objAddressTO = (CustomerAddressTO) addressMap.get(addList.get(i));
                        objAddressTO.setStatus(CommonConstants.STATUS_MODIFIED);
                        objAddressTO.setStatusBy(TrueTransactMain.USER_ID);
                        objAddressTO.setStatusDt(curDate);
                        addressMap.put(objAddressTO.getAddrType(), objAddressTO);
                        regionalData = new HashMap();
                        if (CommonUtil.convertObjToStr(objAddressTO.getAddrType()) != null
                                && CommonUtil.convertObjToStr(objAddressTO.getAddrType()).equalsIgnoreCase("Home")) {
                            regionalData.put("HNAME", objAddressTO.getStreet());
                            regionalData.put("PLACE", objAddressTO.getArea());
                            regionalData.put("CITY", CommonUtil.convertObjToStr(getCbmCity().getDataForKey(objAddressTO.getCity())));
                            regionalData.put("STATE", CommonUtil.convertObjToStr(getCbmState().getDataForKey(objAddressTO.getState())));
                            regionalData.put("COUNTRY", CommonUtil.convertObjToStr(getCbmCountry().getDataForKey(objAddressTO.getCountryCode())));
                            regionalData.put("VILLAGE", CommonUtil.convertObjToStr(getCbmCustVillage().getDataForKey(objAddressTO.getVillage())));
                            regionalData.put("TALUK", CommonUtil.convertObjToStr(getCbmCustTaluk().getDataForKey(objAddressTO.getTaluk())));
                        }
                    }
                    System.out.println("regionalData :"+regionalData);
                    final String addressMapKey = objCustomerTO.getCommAddrType();
                    final CustomerAddressTO objCustomerAddressTO = (CustomerAddressTO) addressMap.get(addressMapKey);
//                    populateAddressData(objCustomerAddressTO);
                    populateContactsTable();
                    if (data.containsKey("PHONE")) {
                        phoneMap = (HashMap) data.get("PHONE");
                        if (phoneMap.get(addressMapKey) != null) {
                            phoneList = (HashMap) phoneMap.get(addressMapKey);
                            ArrayList phoneArray = new ArrayList(phoneList.keySet());
                            for (int i = 0; i < phoneArray.size(); i++) {
                                CustomerPhoneTO objPhoneTO = (CustomerPhoneTO) phoneList.get(phoneArray.get(i));
                                objPhoneTO.setStatus(CommonConstants.STATUS_MODIFIED);
                                objPhoneTO.setStatusBy(TrueTransactMain.USER_ID);
                                objPhoneTO.setStatusDt(curDate);
                                phoneList.put(objPhoneTO.getPhoneId(), objPhoneTO);
                            }
                        }
                    }
                    log.info("data:" + data);
                    log.info("phoneMap:" + phoneMap);
                    log.info("addressMapKey:" + addressMapKey);
                    log.info("phoneList:" + phoneList);
                    //populatePhoneTable();
                }
                

                
                populatePersonalData();
            }
               //added by rishad  04/03/2015 
            if (data.containsKey("Proof")) {
                proofMap = (HashMap) data.get("Proof");
                ArrayList addList = new ArrayList(proofMap.keySet());
                for (int i = 0; i < addList.size(); i++) {
                    CustomerProofTo objProofTO = (CustomerProofTo) proofMap.get(addList.get(i));
                    objProofTO.setStatus(CommonConstants.STATUS_MODIFIED);
                    proofMap.put(objProofTO.getProofType(), objProofTO);
                }
                populateProofTable();
            }
            if (data.containsKey("ALL_ACCOUNT_LIST")) {
                ALL_ACCOUNT_LIST = (List) ((List) data.get("ALL_ACCOUNT_LIST"));
            }
            if (screenCustType == INDIVIDUAL) {
                if (data.containsKey("GAURDIAN")) {
                    objCustomerGaurdianTO = (CustomerGaurdianTO) data.get("GAURDIAN");
                    if (objCustomerGaurdianTO != null) {
                        populateGaurdianData(objCustomerGaurdianTO);
                    }
                }
            }
            if (data.containsKey("PASSPORT")) {
                objCustomerPassPortTO = (CustomerPassPortTO) data.get("PASSPORT");
                if (objCustomerPassPortTO != null) {
                    populatePassPortDetails(objCustomerPassPortTO);
                }
            }
            if (data.containsKey("SUSPREVOKE")) {
                objCustomerSuspensionTO = (CustomerSuspensionTO) data.get("SUSPREVOKE");
                if (objCustomerSuspensionTO != null) {
                    populateSuspRevokeDetails(objCustomerSuspensionTO);
                }
            }
            if (data.containsKey("REGIONAL_LANG")) {
                objtCustRegionalTo = (CustRegionalTo) data.get("REGIONAL_LANG");
                if (objtCustRegionalTo != null) {
                    populateCustRegionalDetails(objtCustRegionalTo);
                }
            }
            if (screenCustType == CORPORATE) {
                //--- To Populate the Main Customer
                HashMap queryMap = new HashMap();
                queryMap.put("CUST_ID", getTxtAuthCustId());
                populateScreen(queryMap, false);
                //--- populates if the Constitution is Joint Account
                if (data.containsKey("JointAcctDetails")) {
                    List jntAccntDetailsList = (List) ((List) data.get("JointAcctDetails"));
                    if (jntAccntDetailsList.size() > 0) {
                        setJointAcctDetails(jntAccntDetailsList);
                    }
                    jntAccntDetailsList = null;
                }
                if (data.containsKey("AuthPersonsTO")) {
//                    List jntAccntDetailsList =  (List) data.get("AuthPersonsTO");
//                    if (jntAccntDetailsList.size() > 0)
//                        setCorpAuthPerDetails(jntAccntDetailsList);
//                    jntAccntDetailsList = null;
                    corpAuthCustMap = (HashMap) data.get("AuthPersonsTO");
                }
            }

            if (data.containsKey("INCOMEPAR")) {
                IncParMap = (LinkedHashMap) data.get("INCOMEPAR");
                ArrayList addList = new ArrayList(IncParMap.keySet());
                for (int i = 0; i < addList.size(); i++) {
                    CustomerIncomeParticularsTO objCustomerIncomeParticularsTO = (CustomerIncomeParticularsTO) IncParMap.get(addList.get(i));
                    objCustomerIncomeParticularsTO.setStatus(CommonConstants.STATUS_MODIFIED);
                    ArrayList incTabRow = new ArrayList();
                    incTabRow.add(CommonUtil.convertObjToStr(objCustomerIncomeParticularsTO.getSlno()));
                    incTabRow.add(CommonUtil.convertObjToStr(objCustomerIncomeParticularsTO.getIncName()));
                    incTabRow.add(CommonUtil.convertObjToStr(objCustomerIncomeParticularsTO.getIncRelation()));
                    incTabRow.add(CommonUtil.convertObjToStr(objCustomerIncomeParticularsTO.getIncDetProfession()));
                    incTabRow.add(CommonUtil.convertObjToStr(objCustomerIncomeParticularsTO.getIncDetCompany()));
                    incTabRow.add(CommonUtil.convertObjToStr(objCustomerIncomeParticularsTO.getIncAmount()));
                    incTabRow.add(CommonUtil.convertObjToStr(objCustomerIncomeParticularsTO.getIncPackage()));
                    tblIncomeParticulars.addRow(incTabRow);
                }
                setChkIncomeDetails(true);
            }
            if (data.containsKey("LANDDETAILS")) {
                LandDetMap = (LinkedHashMap) data.get("LANDDETAILS");
                ArrayList addList = new ArrayList(LandDetMap.keySet());
                for (int i = 0; i < addList.size(); i++) {
                    CustomerLandDetailsTO objCustomerLandDetailsTO = (CustomerLandDetailsTO) LandDetMap.get(addList.get(i));
                    objCustomerLandDetailsTO.setStatus(CommonConstants.STATUS_MODIFIED);
                    ArrayList incTabRow = new ArrayList();
                    incTabRow.add(CommonUtil.convertObjToStr(objCustomerLandDetailsTO.getSlno()));
                    incTabRow.add(CommonUtil.convertObjToStr(objCustomerLandDetailsTO.getLocation()));
                    incTabRow.add(CommonUtil.convertObjToStr(objCustomerLandDetailsTO.getSurvey_no()));
                    incTabRow.add(CommonUtil.convertObjToStr(objCustomerLandDetailsTO.getArea()));
                    incTabRow.add(CommonUtil.convertObjToStr(objCustomerLandDetailsTO.getIrrigated()));
                    incTabRow.add(CommonUtil.convertObjToStr(objCustomerLandDetailsTO.getIrrSrc()));
                    incTabRow.add(CommonUtil.convertObjToStr(objCustomerLandDetailsTO.getVillage()));
                    incTabRow.add(CommonUtil.convertObjToStr(objCustomerLandDetailsTO.getPost()));
                    incTabRow.add(CommonUtil.convertObjToStr(objCustomerLandDetailsTO.getHobli()));
                    incTabRow.add(CommonUtil.convertObjToStr(objCustomerLandDetailsTO.getTaluk()));
                    incTabRow.add(CommonUtil.convertObjToStr(objCustomerLandDetailsTO.getDistrict()));
                    incTabRow.add(CommonUtil.convertObjToStr(objCustomerLandDetailsTO.getState()));
                    incTabRow.add(CommonUtil.convertObjToStr(objCustomerLandDetailsTO.getPin()));
                    tblCustomerLandDetails.addRow(incTabRow);
                }
                setChkLandDetails(true);
            }
            // load the data based on the introduction type
            if (introType != null) {
                final HashMap resultMap = new HashMap();
                resultMap.put(introType, data.get(introType));
                introducerOB.setData(introType, resultMap);
            }

            if (screenCustType == CORPORATE) {
                data.put("FINANCE", objCustFinanceDetailsTO);
                populateFinanceData(whereMap);
            }//else{
            data = null;
            //}
            populateAddlInfo();
            ttNotifyObservers();
        } catch (Exception e) {
            parseException.logException(e, true);
            e.printStackTrace();
        }
    }

    private void populateFinanceData(HashMap whereMap) {
        HashMap where = new HashMap();
        whereMap.put("CUST_ID", whereMap.get(CommonConstants.MAP_WHERE));
        where.put(CommonConstants.MAP_WHERE, whereMap);
        final List objList = ClientUtil.executeQuery("getSelectCustFinanceDetailsTO", whereMap);
        if (objList.size() > 0) {
            final CustFinanceDetailsTO objCustFinanceDetailsTO = (CustFinanceDetailsTO) (ClientUtil.executeQuery("getSelectCustFinanceDetailsTO", whereMap).get(0));
            setTxtAuthCapital(CommonUtil.convertObjToStr(objCustFinanceDetailsTO.getAuthorizeCapital()));
            setTxtIssuedCapital(CommonUtil.convertObjToStr(objCustFinanceDetailsTO.getIssuedCapital()));
            setTxtSubscribedCapital(CommonUtil.convertObjToStr(objCustFinanceDetailsTO.getSubscribedCapital()));
            setTxtTotalResource(CommonUtil.convertObjToStr(objCustFinanceDetailsTO.getTotalResource()));
            setTxtLastYrPL(CommonUtil.convertObjToStr(objCustFinanceDetailsTO.getLastYearPl()));
            setTxtDividendPercentage(CommonUtil.convertObjToStr(objCustFinanceDetailsTO.getDividendPercent()));
            setCboBusNature(CommonUtil.convertObjToStr(getCbmBusNature().getDataForKey(objCustFinanceDetailsTO.getNatureOfBusiness())));
            setTdtDtEstablished(DateUtil.getStringDate(objCustFinanceDetailsTO.getEstablishDt()));
            setTxtRegNumber(objCustFinanceDetailsTO.getCompRegNo());
            setTxtCEO(objCustFinanceDetailsTO.getCeoName());
            setTdtFinacialYrEnd(DateUtil.getStringDate(objCustFinanceDetailsTO.getFinancialYrEnd()));
            setTxtTotalIncome(CommonUtil.convertObjToStr(objCustFinanceDetailsTO.getTotalIncome()));
            setTxtprofitBefTax(CommonUtil.convertObjToStr(objCustFinanceDetailsTO.getProfitBeforeTax()));
            setTxtTotalNonTaxExp(CommonUtil.convertObjToStr(objCustFinanceDetailsTO.getTotalNonTaxExp()));
            setTxtTaxliability(CommonUtil.convertObjToStr(objCustFinanceDetailsTO.getLiablityTax()));
        }
        data = null;
    }

    public void populateScreen(HashMap queryWhereMap, boolean jntAcctNewClicked) {
        try {
            queryWhereMap.put("BRANCH_ID", ProxyParameters.BRANCH_ID);
            List custListData = ClientUtil.executeQuery("getSelectAccInfoDisplay", queryWhereMap);
            if (custListData.size() > 0) {
                HashMap custMapData;
                custMapData = (HashMap) custListData.get(0);
                String strPrevMainCust_ID = getTxtAuthCustId();
                if (jntAcctNewClicked == false) {//--- If it is Main acctnt,set CustomerId in Main
                    setTxtAuthCustId(CommonUtil.convertObjToStr(custMapData.get("CUST_ID")));
                    objJointAcctHolderManipulation.setJntAcctTableData(custMapData, false, tblJointAccnt);
                    setTblJointAccnt(objJointAcctHolderManipulation.getTblJointAccnt());
                }
                setLblValCustomerName(CommonUtil.convertObjToStr(custMapData.get("Name")));
                setLblValDateOfBirth(DateUtil.getStringDate((Date) custMapData.get("DOB")));
                setLblValStreet(CommonUtil.convertObjToStr(custMapData.get("STREET")));
                setLblValArea(CommonUtil.convertObjToStr(custMapData.get("AREA")));
                setLblValCity(CommonUtil.convertObjToStr(custMapData.get("CITY1")));
                setLblValState(CommonUtil.convertObjToStr(custMapData.get("STATE1")));
                setLblValCountry(CommonUtil.convertObjToStr(custMapData.get("COUNTRY")));
                setLblValCorpCustDesig(CommonUtil.convertObjToStr(custMapData.get("DESIGNATION")));
                setLblValPin(CommonUtil.convertObjToStr(custMapData.get("PIN_CODE")));
                custListData = null;
                custMapData = null;
            } else {
                setTxtAuthCustId("");
                resetCustDetails();
                resetJntAccntHoldTbl();
            }

        } catch (Exception e) {
            parseException.logException(e, true);
        }
    }

    //    /**
    //     *To get few details for the selected customer when help icon is pressed
    //     */
    //    public void getAuthCustData(HashMap whereMap) {
    //        final List authCustData = (List) ClientUtil.executeQuery("getSingleIndividualCustInfo", whereMap);
    //        populateAuthCustInfo((HashMap)authCustData.get(0));
    //    }
    //
    //    private void populateAuthCustInfo(HashMap authCustInfo){
    //        setTxtFirstName((String)authCustInfo.get("FNAME"));
    //        setTxtMiddleName((String)authCustInfo.get("MNAME"));
    //        setTxtLastName((String)authCustInfo.get("LNAME"));
    //        if (((String)authCustInfo.get("GENDER")).equalsIgnoreCase("Male")){
    //            setRdoGender_Male(true);
    //            setRdoGender_Female(false);
    //        }else{
    //            setRdoGender_Male(false);
    //            setRdoGender_Female(true);
    //        }
    //        setTxtAuthCustId((String)authCustInfo.get("CUST_ID"));
    //        setCboTitle(CommonUtil.convertObjToStr(getCbmTitle().getDataForKey(authCustInfo.get("TITLE"))));
    //        ttNotifyObservers();
    //    }
//    private void populatePhotoSign(){
//        populatePhoto();
//        populateSign();
//    }
//    
//    private void populatePhoto(){
//        setPhotoFile(objCustomerTO.getPhotoFile());
//    }
//    
//    private void populateSign(){
//        setSignFile(objCustomerTO.getSignatureFile());
//    }
    private void populateAddlInfo() {
        setTxtSsn(objCustomerTO.getSsn());
        if (objCustomerTO.getTransPwd() != null) {
            setTxtTransPwd(objCustomerTO.getTransPwd());
        }
        setTxtCustUserid(objCustomerTO.getCustUserid());
        if (objCustomerTO.getCustPwd() != null) {
            setTxtCustPwd(objCustomerTO.getCustPwd());
        }
    }

    public void ttNotifyObservers() {
        notifyObservers();
    }
    
     /**
     * To populate Proof Data
     */
    private void populateProofData(CustomerProofTo objCustomerProofTo) throws Exception {
        try {
            if (objCustomerProofTo != null) {
                if (objCustomerProofTo.getProofType() != null) {
                    getCbmIdenProof().setKeyForSelected(CommonUtil.convertObjToStr(objCustomerProofTo.getProofType()));
                }
                setTxtUniqueId(objCustomerProofTo.getUniqueId());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
     
    /**
     * To populate Address Data
     */
    private void populateAddressData(CustomerAddressTO objCustomerAddressTO) throws Exception {
        try {
            if (objCustomerAddressTO != null) {
                if (objCustomerAddressTO.getAddrType() != null) {
//        setCboAddressType((String) getCbmAddressType().getDataForKey(CommonUtil.convertObjToStr(objCustomerAddressTO.getAddrType())));
                    getCbmAddressType().setKeyForSelected(CommonUtil.convertObjToStr(objCustomerAddressTO.getAddrType()));
                }
                setTxtStreet(objCustomerAddressTO.getStreet());
                setTxtArea(objCustomerAddressTO.getArea());
                setCboCity(CommonUtil.convertObjToStr(getCbmCity().getDataForKey(CommonUtil.convertObjToStr(objCustomerAddressTO.getCity()))));
                setCboState(CommonUtil.convertObjToStr(getCbmState().getDataForKey(CommonUtil.convertObjToStr(objCustomerAddressTO.getState()))));
                setCboPostOffice(objCustomerAddressTO.getPostOffice());
                setTxtPincode(objCustomerAddressTO.getPinCode());
                setCboCountry(CommonUtil.convertObjToStr(getCbmCountry().getDataForKey(CommonUtil.convertObjToStr(objCustomerAddressTO.getCountryCode()))));
                setCboCustVillage(CommonUtil.convertObjToStr(getCbmCustVillage().getDataForKey(CommonUtil.convertObjToStr(objCustomerAddressTO.getVillage()))));
                setCboCustTaluk(CommonUtil.convertObjToStr(getCbmCustTaluk().getDataForKey(CommonUtil.convertObjToStr(objCustomerAddressTO.getTaluk()))));
                setCboWardName(CommonUtil.convertObjToStr(getCbmWardName().getDataForKey(CommonUtil.convertObjToStr(objCustomerAddressTO.getWardName())))); // Added by nithya
                setTxtAddrRemarks(objCustomerAddressTO.getRemarks());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void populateIncParticulars(CustomerIncomeParticularsTO objCustomerIncomeParticularsTO) throws Exception {
        setTxtIncAmount(objCustomerIncomeParticularsTO.getIncAmount());
        setTxtIncName(objCustomerIncomeParticularsTO.getIncName());
        setCboIncPack(CommonUtil.convertObjToStr(getCbmIncPack().getDataForKey(CommonUtil.convertObjToStr(objCustomerIncomeParticularsTO.getIncPackage()))));
        setCboIncRelation(CommonUtil.convertObjToStr(getCbmIncRelation().getDataForKey(CommonUtil.convertObjToStr(objCustomerIncomeParticularsTO.getIncRelation()))));
        setCboIncProfession(CommonUtil.convertObjToStr(getCbmIncProfession().getDataForKey(CommonUtil.convertObjToStr(objCustomerIncomeParticularsTO.getIncDetProfession()))));
        setIncCompany(objCustomerIncomeParticularsTO.getIncDetCompany());
        setTxtSlNo(objCustomerIncomeParticularsTO.getSlno());
        setChanged();
    }

    private void populateLandDetails(CustomerLandDetailsTO objCustomerLandDetailsTO) throws Exception {
        setTxtLocation(objCustomerLandDetailsTO.getLocation());
        setTxtSurNo(objCustomerLandDetailsTO.getSurvey_no());
        setTxtLandDetArea(objCustomerLandDetailsTO.getArea());
        setCboIrrigated(CommonUtil.convertObjToStr(getCbmIrrigated().getDataForKey(CommonUtil.convertObjToStr(objCustomerLandDetailsTO.getIrrigated()))));
        setCboSourceIrrigated(CommonUtil.convertObjToStr(getCbmSourceIrrigated().getDataForKey(CommonUtil.convertObjToStr(objCustomerLandDetailsTO.getIrrSrc()))));
        setTxtVillage(objCustomerLandDetailsTO.getVillage());
        setTxtPost(objCustomerLandDetailsTO.getPost());
        setTxtHobli(objCustomerLandDetailsTO.getHobli());
        String st = ((String) getCbmLandDetState().getDataForKey(CommonUtil.convertObjToStr(objCustomerLandDetailsTO.getState())));
        setCboLandDetState(st);

        if (!CommonUtil.convertObjToStr(objCustomerLandDetailsTO.getDistrict()).equals("")) {
            setCbmDistrict(objCustomerLandDetailsTO.getState());
            getCbmDistrict().setKeyForSelected(CommonUtil.convertObjToStr(objCustomerLandDetailsTO.getDistrict())); // This line added by Rajesh
            setCboDistrict((String) getCbmDistrict().getDataForKey(CommonUtil.convertObjToStr(objCustomerLandDetailsTO.getDistrict())));
        }
        if (!CommonUtil.convertObjToStr(objCustomerLandDetailsTO.getTaluk()).equals("")) {
            setCbmTaluk(objCustomerLandDetailsTO.getDistrict(), objCustomerLandDetailsTO.getState());
            getCbmTaluk().setKeyForSelected(CommonUtil.convertObjToStr(objCustomerLandDetailsTO.getTaluk())); // This line added by Rajesh
            setCboTaluk((String) getCbmTaluk().getDataForKey(CommonUtil.convertObjToStr(objCustomerLandDetailsTO.getTaluk())));
        }


//            setCboTaluk((String) getCbmTaluk().getDataForKey(CommonUtil.convertObjToStr(objCustomerLandDetailsTO.getTaluk())));
        setTxtLandDetPin(objCustomerLandDetailsTO.getPin());
        setTxtLandDetSlNo(objCustomerLandDetailsTO.getSlno());
        setChanged();
    }

    /**
     * To populate Customer Data
     */
    private void populatePersonalData() throws Exception {
        setTxtFirstName(objCustomerTO.getFname());
        setTxtMiddleName(objCustomerTO.getMname());
        setTxtLastName(objCustomerTO.getLname());
        setTdtjoiningDate(DateUtil.getStringDate(objCustomerTO.getJoiningDate()));
        Date createdDate = objCustomerTO.getCreateddt();
//        Date cDate = DateUtil.getDate(createdDate.getDay(),createdDate.getMonth(),createdDate.getYear());
        String crDate = null;
        if (createdDate != null) {
            crDate = createdDate.getDate() + "/" + (createdDate.getMonth() + 1) + "/"
                    + (createdDate.getYear() + 1900);
        }
        setLblCreatedDt1(crDate);
        Date currDt = (Date) curDate.clone();
        long period = DateUtil.dateDiff((Date) DateUtil.getDateMMDDYYYY(crDate), (Date) currDt);
        long years;
        long months;
        long days;
        if (period > 365) {
            years = period / 365;
            period = period - (365 * years);
        } else {
            years = 0;
        }
        if (period > 30) {
            months = period / 30;
            period = period - (30 * months);
        } else {
            months = 0;
        }
        if (period > 0) {
            days = period;
        } else {
            days = 0;
        }
        StringBuffer periodLetters = new StringBuffer();
        periodLetters.append(String.valueOf(years) + " Years ");
        periodLetters.append(String.valueOf(months) + " Months ");
        periodLetters.append(String.valueOf(days) + " Days ");
        setLblDealingPeriod(String.valueOf(periodLetters));
        if (screenCustType == INDIVIDUAL) {
//            if( objCustomerTO.getGender().equalsIgnoreCase(objCustomerRB.getString("genderMale")) ){
//            rdoGender_Male = true;
//        }else{
//            rdoGender_Female = true;
//        }
            if (objCustomerTO.getGender() != null) {
                if (objCustomerTO.getGender().equalsIgnoreCase(objCustomerRB.getString("genderMale"))) {
                    rdoGender_Male = true;
                } else {
                    rdoGender_Female = true;
                }
            }

            if (objCustomerTO.getMaritalstatus() != null) {
                if (objCustomerTO.getMaritalstatus().equalsIgnoreCase(objCustomerRB.getString("maritalStatusSingle"))) {
                    rdoMaritalStatus_Single = true;
                } else if (objCustomerTO.getMaritalstatus().equalsIgnoreCase(objCustomerRB.getString("maritalStatusMarried"))){
                    rdoMaritalStatus_Married = true;
                }else if (objCustomerTO.getMaritalstatus().equalsIgnoreCase(objCustomerRB.getString("maritalStatusWidow"))){
                    rdoMaritalStatus_Widow = true;
                } 
            }
            if (objCustomerTO.getMinor().equalsIgnoreCase(objCustomerRB.getString("minorYes"))) {
                isMinor = true;
                this.setLblCustomerStatus(objCustomerRB.getString("minor"));
                chkMinor = true;
            } else {
                isMinor = false;
                this.setLblCustomerStatus("");
                chkMinor = false;
            }

        }
        setTxtNationality(objCustomerTO.getNationality());
        setTxtLanguage(objCustomerTO.getLanguage());
        setCboTitle(CommonUtil.convertObjToStr(getCbmTitle().getDataForKey(objCustomerTO.getTitle())));
        setTdtDateOfBirth(DateUtil.getStringDate(objCustomerTO.getDob()));
        setTdtRetireDt(DateUtil.getStringDate(objCustomerTO.getRetDt()));
        setTxtRetireAge(CommonUtil.convertObjToStr(objCustomerTO.getRetAge()));
        setTxtCustomerID(objCustomerTO.getCustId());
        setTxtRemarks(objCustomerTO.getRemarks());
        getCbmResidentialStatus().setKeyForSelected(CommonUtil.convertObjToStr(objCustomerTO.getResidentialstatus()));
        if (objCustomerTO.getCustType().equals("INDIVIDUAL")) {
            setCboCustomerType(CommonUtil.convertObjToStr(getCbmCustomerType().getDataForKey(CommonUtil.convertObjToStr(objCustomerTO.getCustTypeId()))));
        } else {
            setCboCustomerType(CommonUtil.convertObjToStr(getCbmCustomerType().getDataForKey(CommonUtil.convertObjToStr(objCustomerTO.getCustType()))));
        }
        setCboRelationManager(CommonUtil.convertObjToStr(getCbmRelationManager().getDataForKey(CommonUtil.convertObjToStr(objCustomerTO.getRelationmanager()))));
        setTxtCompany(objCustomerTO.getCompName());
        setCboAnnualIncomeLevel(CommonUtil.convertObjToStr(getCbmAnnualIncomeLevel().getDataForKey(CommonUtil.convertObjToStr(objCustomerTO.getAnnincome()))));
        setCboPrefCommunication(CommonUtil.convertObjToStr(getCbmPrefCommunication().getDataForKey(CommonUtil.convertObjToStr(objCustomerTO.getPreferredComm()))));
        setCboCustomerGroup(CommonUtil.convertObjToStr(getCbmCustomerGroup().getDataForKey(CommonUtil.convertObjToStr(objCustomerTO.getCustomergroup()))));
        setCboVehicle(CommonUtil.convertObjToStr(getCbmVehicle().getDataForKey(CommonUtil.convertObjToStr(objCustomerTO.getVehicle()))));
        setCboProfession(CommonUtil.convertObjToStr(getCbmProfession().getDataForKey(CommonUtil.convertObjToStr(objCustomerTO.getProfession()))));
        setTxtPanNumber(CommonUtil.convertObjToStr(objCustomerTO.getPanNumber()));
        if (screenCustType == INDIVIDUAL) {
            setCboCaste(CommonUtil.convertObjToStr(getCbmCaste().getDataForKey(CommonUtil.convertObjToStr(objCustomerTO.getCaste()))));
            setCboSubCaste(CommonUtil.convertObjToStr(getCbmSubCaste().getDataForKey(CommonUtil.convertObjToStr(objCustomerTO.getSubCaste()))));
            //            changed by nikhil
            setCboReligion(CommonUtil.convertObjToStr(getCbmReligion().getDataForKey(CommonUtil.convertObjToStr(objCustomerTO.getCboReligion()))));
         
            if(objCustomerTO.getDivision() != null && (!objCustomerTO.getDivision().equals(""))){
                // Added by nithya on 30-06-2016 for 4774
                setCboDivision(CommonUtil.convertObjToStr(getCbmDivision().getDataForKey(CommonUtil.convertObjToStr(objCustomerTO.getDivision()))));

                //setCboDivision(objCustomerTO.getDivision()); // Commented by nithya
            }
            if(objCustomerTO.getAgentCustId() != null && (!objCustomerTO.getAgentCustId().equals(""))){
                setCboAgentCustId(objCustomerTO.getAgentCustId());
                HashMap agentCustMap = new HashMap();
                agentCustMap.put("AGENT_CUST_ID",objCustomerTO.getAgentCustId());
                List lst = ClientUtil.executeQuery("getSelectAgentCustomerName", agentCustMap);
                if(lst != null && lst.size()>0){
                    agentCustMap = (HashMap)lst.get(0);
                    setCboAgentCustId(CommonUtil.convertObjToStr(agentCustMap.get("VALUE")));
                    setLblAgentCustIdValue(objCustomerTO.getAgentCustId());
                }
            }
        }
        setCboPrimaryOccupation(CommonUtil.convertObjToStr(getCbmPrimaryOccupation().getDataForKey(CommonUtil.convertObjToStr(objCustomerTO.getPrimaryOccupation()))));
        if (objCustomerTO.getAddrVerified() != null && objCustomerTO.getAddrVerified().trim().equalsIgnoreCase(YES)) {
            setChkAddrVerified(true);
        } else {
            setChkAddrVerified(false);
        }
        if (objCustomerTO.getPhoneVerified() != null && objCustomerTO.getPhoneVerified().trim().equalsIgnoreCase(YES)) {
            setChkPhVerified(true);
        } else {
            setChkPhVerified(false);
        }
        if (objCustomerTO.getObtainFinstat() != null && objCustomerTO.getObtainFinstat().equals(YES)) {
            setChkFinanceStmtVerified(true);
        } else {
            setChkFinanceStmtVerified(false);
        }
        setCboEducationalLevel(CommonUtil.convertObjToStr(getCbmEducationalLevel().getDataForKey(CommonUtil.convertObjToStr(objCustomerTO.getEducation()))));
        setTxtEmailID(objCustomerTO.getEmailId());
        setTxtNetWorth(CommonUtil.convertObjToStr(objCustomerTO.getNetworth()));
        setTdtNetWorthAsOn(DateUtil.getStringDate(objCustomerTO.getNetworthAsOn()));

        setCommAddrType(objCustomerTO.getCommAddrType());
        setTxtAuthCustId(objCustomerTO.getAuthorizeCustId());
        setTxtWebSite(objCustomerTO.getWebsite());
        setCboMembershipClass(CommonUtil.convertObjToStr(getCbmMembershipClass().getDataForKey(objCustomerTO.getMembershipClass())));
        setCboCareOf(CommonUtil.convertObjToStr(getCbmCareOf().getDataForKey(objCustomerTO.getCareOf())));
        setTxtName(objCustomerTO.getCareOfName());
        setCboCustStatus(CommonUtil.convertObjToStr(getCbmCustStatus().getDataForKey(objCustomerTO.getCustomerStatus())));
        setTdtCrAvldSince(DateUtil.getStringDate(objCustomerTO.getCrAvailedSince()));
        setTxtRiskRate(objCustomerTO.getRiskRating());
        getCbmIntroType().setKeyForSelected(CommonUtil.convertObjToStr(objCustomerTO.getIntroType()));
        setTxtBranchId(objCustomerTO.getBranchCode());
        setTxtStaffId(objCustomerTO.getStaffId());
        setTxtDesignation(objCustomerTO.getDesignation());
        setStatusBy(objCustomerTO.getStatusBy());
        setMobileAppLoginStatus(objCustomerTO.getMobileAppLoginStatus());
        setAuthorizeStatus(objCustomerTO.getAuthorizeStatus());
        setCboAddrProof(CommonUtil.convertObjToStr(getCbmAddrProof().getDataForKey(CommonUtil.convertObjToStr(objCustomerTO.getAddrProof()))));
        setCboIdenProof(CommonUtil.convertObjToStr(getCbmIdenProof().getDataForKey(CommonUtil.convertObjToStr(objCustomerTO.getIdenProof()))));
        if (objCustomerTO.getStaffId() != null) {
            setIsStaff(true);
        } else {
            setIsStaff(false);
        }
        if (objCustomerTO.getMinority() != null && objCustomerTO.getMinority().equalsIgnoreCase("Y")) {
            setIsMinority(true);
        } else {
            setIsMinority(false);
        }

        if (objCustomerTO.getSendThanksLetter() != null && objCustomerTO.getSendThanksLetter().equals(YES)) {
            setChkSentThanksLetter(true);
        } else {
            setChkSentThanksLetter(false);
        }
        if (objCustomerTO.getConfirmThanks() != null && objCustomerTO.getConfirmThanks().equals(YES)) {
            setChkConfirmationfromIntroducer(true);
        } else {
            setChkConfirmationfromIntroducer(false);
        }
        if (objCustomerTO.getCustType().equals("INDIVIDUAL")) {
//            populatePhotoSign();
            if (data.containsKey("PHOTOSIGN")) {
                HashMap photoSignMap = (HashMap) data.get("PHOTOSIGN");
//                setPhotoFile(CommonUtil.convertObjToStr(photoSignMap.get("PHOTO")));
//                setSignFile(CommonUtil.convertObjToStr(photoSignMap.get("SIGN")));
                setPhotoByteArray((byte[]) photoSignMap.get("PHOTO"));
                setSignByteArray((byte[]) photoSignMap.get("SIGN"));
            }
            if(data.containsKey("PROOF_PHOTO")){ //28-11-2020
                setProofPhotoByteArray((byte[]) data.get("PROOF_PHOTO"));
            }
//            getPhotoSign();
        }
        setTxtKartha(objCustomerTO.getKartha());
        setCboFarClass(CommonUtil.convertObjToStr(getCbmFarClass().getDataForKey(CommonUtil.convertObjToStr(objCustomerTO.getFarClass()))));
        setCbcomboAmsam(CommonUtil.convertObjToStr(getCbmcomboAmsam().getDataForKey(CommonUtil.convertObjToStr(objCustomerTO.getAmsam()))));
        setCbcomboDesam(CommonUtil.convertObjToStr(getCbmcomboDesam().getDataForKey(CommonUtil.convertObjToStr(objCustomerTO.getDesam()))));
        setTxtAge(CommonUtil.convertObjToStr(objCustomerTO.getAge()));
        setBankruptcy(objCustomerTO.getBankruptcy());
        setMemberShipNo(objCustomerTO.getMembershipNum());
        setTxtWardNo(objCustomerTO.getWardNo());
        if (objCustomerTO.getItDec() != null) {
            if (objCustomerTO.getItDec().equalsIgnoreCase(objCustomerRB.getString("pan"))) {
                rdoITDec_pan = true;
            } else if (objCustomerTO.getItDec().equalsIgnoreCase(objCustomerRB.getString("F60"))) {
                rdoITDec_F60 = true;
            } else if (objCustomerTO.getItDec().equalsIgnoreCase(objCustomerRB.getString("F61"))) {
                rdoITDec_F61 = true;
            }
        }
    }

    /**
     * To populate Contacts table
     */
    private void populateContactsTable() throws Exception {
        final Iterator addressMapIterator = addressMap.keySet().iterator();
        ArrayList addressRow = null;
        for (int i = addressMap.size(), j = 0; i > 0; i--, j++) {
            final String addressType = CommonUtil.convertObjToStr(addressMapIterator.next());
            addressRow = new ArrayList();
            addressRow.add(addressType);
            if (objCustomerTO.getCommAddrType().equals(addressType)) {
                addressRow.add(PRIMARY);
                tblContactList.insertRow(0, addressRow);
            } else {
                addressRow.add(EMPTY_STRING);
                tblContactList.insertRow(tblContactList.getRowCount(), addressRow);
            }
            addressRow = null;
        }

    }
    //added by rishad
    private void populateProofTable() throws Exception {
        CustomerProofTo objCustomerproofTo = null;
        ArrayList proofRow = null;
        if (proofMap != null) {
            final ArrayList keys = new ArrayList(proofMap.keySet());
            ArrayList newKeys = new ArrayList();
            for (int i = 0; i < keys.size(); i++) {
                objCustomerproofTo = (CustomerProofTo) proofMap.get(keys.get(i));
                proofRow = new ArrayList();
                proofRow.add(objCustomerproofTo.getProofType());
                proofRow.add(objCustomerproofTo.getUniqueId());
                tblProofList.insertRow(tblProofList.getRowCount(), proofRow);
                proofRow = null;
            }
        }
    }
    
    private void populateincParTable() throws Exception {
        ArrayList incDataList = new ArrayList();
        incDataList = new ArrayList(IncParMap.keySet());
        ArrayList addList = new ArrayList(IncParMap.keySet());
        int length = incDataList.size();
        for (int i = 0; i < length; i++) {
            ArrayList incTabRow = new ArrayList();
            CustomerIncomeParticularsTO objCustomerIncomeParticularsTO = (CustomerIncomeParticularsTO) IncParMap.get(addList.get(i));

            IncVal.add(objCustomerIncomeParticularsTO);

            incTabRow.add(CommonUtil.convertObjToStr(objCustomerIncomeParticularsTO.getSlno()));
            incTabRow.add(CommonUtil.convertObjToStr(objCustomerIncomeParticularsTO.getIncName()));
            incTabRow.add(CommonUtil.convertObjToStr(objCustomerIncomeParticularsTO.getIncRelation()));
            incTabRow.add(CommonUtil.convertObjToStr(objCustomerIncomeParticularsTO.getIncDetProfession()));
            incTabRow.add(CommonUtil.convertObjToStr(objCustomerIncomeParticularsTO.getIncDetCompany()));
            incTabRow.add(CommonUtil.convertObjToStr(objCustomerIncomeParticularsTO.getIncAmount()));
            incTabRow.add(CommonUtil.convertObjToStr(objCustomerIncomeParticularsTO.getIncPackage()));
            tblIncomeParticulars.addRow(incTabRow);
        }
        ttNotifyObservers();

    }

    /**
     * Populating Land Details *
     */
    private void populateLand() throws Exception {
        ArrayList LandDataList = new ArrayList();
        LandDataList = new ArrayList(LandDetMap.keySet());
        ArrayList addList = new ArrayList(LandDetMap.keySet());
        int length = LandDataList.size();
        for (int i = 0; i < length; i++) {
            ArrayList incTabRow = new ArrayList();
            CustomerLandDetailsTO objCustomerLandDetailsTO = (CustomerLandDetailsTO) LandDetMap.get(addList.get(i));

            LandDetailsVal.add(objCustomerLandDetailsTO);

            incTabRow.add(CommonUtil.convertObjToStr(objCustomerLandDetailsTO.getSlno()));
            incTabRow.add(CommonUtil.convertObjToStr(objCustomerLandDetailsTO.getLocation()));
            incTabRow.add(CommonUtil.convertObjToStr(objCustomerLandDetailsTO.getSurvey_no()));
            incTabRow.add(CommonUtil.convertObjToStr(objCustomerLandDetailsTO.getArea()));
            incTabRow.add(CommonUtil.convertObjToStr(objCustomerLandDetailsTO.getIrrigated()));
            incTabRow.add(CommonUtil.convertObjToStr(objCustomerLandDetailsTO.getIrrSrc()));
            incTabRow.add(CommonUtil.convertObjToStr(objCustomerLandDetailsTO.getVillage()));
            incTabRow.add(CommonUtil.convertObjToStr(objCustomerLandDetailsTO.getPost()));
            incTabRow.add(CommonUtil.convertObjToStr(objCustomerLandDetailsTO.getHobli()));
            incTabRow.add(CommonUtil.convertObjToStr(objCustomerLandDetailsTO.getTaluk()));
            incTabRow.add(CommonUtil.convertObjToStr(objCustomerLandDetailsTO.getDistrict()));
            incTabRow.add(CommonUtil.convertObjToStr(objCustomerLandDetailsTO.getState()));
            incTabRow.add(CommonUtil.convertObjToStr(objCustomerLandDetailsTO.getPin()));
            tblCustomerLandDetails.addRow(incTabRow);
        }
        ttNotifyObservers();

    }

    /**
     * Populating Gaurdian Details *
     */
    private void populateGaurdianData(CustomerGaurdianTO objCustomerGaurdianTO) {
        setTxtGuardianNameNO(CommonUtil.convertObjToStr(objCustomerGaurdianTO.getGuardianName()));
        setTxtGStreet(CommonUtil.convertObjToStr(objCustomerGaurdianTO.getStreet()));
        setTxtGArea(CommonUtil.convertObjToStr(objCustomerGaurdianTO.getArea()));
        setCboGCity(CommonUtil.convertObjToStr(getCbmGCity().getDataForKey(CommonUtil.convertObjToStr(objCustomerGaurdianTO.getCity()))));
        setCboGState(CommonUtil.convertObjToStr(getCbmGState().getDataForKey(CommonUtil.convertObjToStr(objCustomerGaurdianTO.getState()))));
        setTxtGPinCode(CommonUtil.convertObjToStr(objCustomerGaurdianTO.getPinCode()));
        setTxtGuardianACodeNO(CommonUtil.convertObjToStr(objCustomerGaurdianTO.getAreaCode()));
        setTxtGuardianPhoneNO(CommonUtil.convertObjToStr(objCustomerGaurdianTO.getPhNo()));
        setCboRelationNO(CommonUtil.convertObjToStr(getCbmRelationNO().getDataForKey(CommonUtil.convertObjToStr(objCustomerGaurdianTO.getRelationship()))));
        setCboGCountry(CommonUtil.convertObjToStr(getCbmGCountry().getDataForKey(CommonUtil.convertObjToStr(objCustomerGaurdianTO.getCountryCode()))));
        setTdtGuardianDOB(CommonUtil.convertObjToStr(objCustomerGaurdianTO.getGuardianDob()));
        setTxtGuardianAge(CommonUtil.convertObjToStr(objCustomerGaurdianTO.getGuardianAge()));
    }

    private void populatePassPortDetails(CustomerPassPortTO objCustomerPassPortTO) {
        setTxtPassportFirstName(objCustomerPassPortTO.getPassfname());
        setTxtPassportLastName(objCustomerPassPortTO.getPasslname());
        setTxtPassportMiddleName(objCustomerPassPortTO.getPassmname());
        setTxtPassportIssueAuth(objCustomerPassPortTO.getIssueAuth());
        setTxtPassportNo(objCustomerPassPortTO.getPassNumber());
        setCboPassportTitle(CommonUtil.convertObjToStr(getCbmTitle().getDataForKey(CommonUtil.convertObjToStr(objCustomerPassPortTO.getPassTitle()))));
        setCboPassportIssuePlace(CommonUtil.convertObjToStr(getCbmPassportIssuePlace().getDataForKey(CommonUtil.convertObjToStr(objCustomerPassPortTO.getIssuePlace()))));
        setTdtPassportIssueDt(DateUtil.getStringDate(objCustomerPassPortTO.getIssueDt()));
        setTdtPassportValidUpto(DateUtil.getStringDate(objCustomerPassPortTO.getValidUpto()));
    }

    private void populateSuspRevokeDetails(CustomerSuspensionTO objCustomerSuspensionTO) {
        setSuspendedDate(DateUtil.getStringDate(objCustomerSuspensionTO.getSuspendedFromDate()));
        setRevokedDate(DateUtil.getStringDate(objCustomerSuspensionTO.getRevokedDate()));
        if (objCustomerSuspensionTO.getStatus() != null && objCustomerSuspensionTO.getStatus().trim().equalsIgnoreCase(SUSPENDED)) {
            setChksuspendedBy(true);
        } else {
            setChksuspendedBy(false);
        }
        if (objCustomerSuspensionTO.getStatus() != null && objCustomerSuspensionTO.getStatus().trim().equalsIgnoreCase(REVOKED)) {
            setChkrevokedBy(true);
            setChksuspendedBy(false);
        } else {
            setChkrevokedBy(false);
        }
        setSusRevRemarks(objCustomerSuspensionTO.getRemarks());
    }
    private void populateCustRegionalDetails(CustRegionalTo objtCustRegionalTo) {
        setTxtRegMName(objtCustRegionalTo.getFname());
        setTxtRegMaHname(objtCustRegionalTo.getHouseName());
        setTxtRegMaPlace(objtCustRegionalTo.getPlace());
        setTxtRegMavillage(objtCustRegionalTo.getVillage());
        setTxtRegMaTaluk(objtCustRegionalTo.getTaluk());
        setTxtRegMaCity(objtCustRegionalTo.getCity());
        setTxtRegMaCountry(objtCustRegionalTo.getCountry());
        setTxtRegMaState(objtCustRegionalTo.getState());
        setTxtRegMaAmsam(objtCustRegionalTo.getAmsam());
        setTxtRegMaDesam(objtCustRegionalTo.getDesam());
        setTxtRegMaGardName(objtCustRegionalTo.getCareOfName());
        setChkRegioalLang(true);
    }
    /**
     * to populate Phone Details
     */
    private void populatePhoneData(CustomerPhoneTO objCustomerPhoneTO) throws Exception {
        setTxtAreaCode(objCustomerPhoneTO.getAreaCode());
        setTxtPhoneNumber(objCustomerPhoneTO.getPhoneNumber());
        setCboPhoneType(CommonUtil.convertObjToStr(cbmPhoneType.getDataForKey(CommonUtil.convertObjToStr(objCustomerPhoneTO.getPhoneTypeId()))));
    }

    /**
     * To populate phone list table
     */
    private void populatePhoneTable() throws Exception {
        CustomerPhoneTO objCustomerPhoneTO = null;
        ArrayList phoneRow = null;
        log.info("phoneList:" + phoneList);
        if (phoneList != null) {
            final ArrayList keys = new ArrayList(phoneList.keySet());
            ArrayList newKeys = new ArrayList();
            for (int i = 0; i < (keys.size() - 1); i++) {
                for (int j = i + 1; j < keys.size(); j++) {
                    double a = CommonUtil.convertObjToDouble(keys.get(i)).doubleValue();
                    double b = CommonUtil.convertObjToDouble(keys.get(j)).doubleValue();
                    if (a > b) {
                        keys.set(i, new Double(b));
                        keys.set(j, new Double(a));
                    }
                }
            }
            for (int i = 0; i < keys.size(); i++) {
                objCustomerPhoneTO = (CustomerPhoneTO) phoneList.get((Double) keys.get(i));
                phoneRow = new ArrayList();
                phoneRow.add(objCustomerPhoneTO.getPhoneId());
                phoneRow.add(objCustomerPhoneTO.getPhoneTypeId());
                phoneRow.add(objCustomerPhoneTO.getPhoneNumber());
                tblPhoneList.insertRow(tblPhoneList.getRowCount(), phoneRow);
                phoneRow = null;
            }
        }
    }

    /**
     * To populate Income Particulars
     */
    public void populateIncParDetails(int row) {
        try {
            IncomeChanged(CommonUtil.convertObjToStr(tblIncomeParticulars.getValueAt(row, 0)));
        } catch (Exception e) {
            parseException.logException(e, true);
        }
    }

    /**
     * To populate Land Details
     */
    public void populateLandDetailsOfCustomer(int row) {
        try {
            LandDetChanged(CommonUtil.convertObjToStr(tblCustomerLandDetails.getValueAt(row, 0)));
        } catch (Exception e) {
            parseException.logException(e, true);
        }
    }

    /**
     * To populate address, if address type changes
     */
    public void populateAddress(int row) {
        try {
            addressTypeChanged(CommonUtil.convertObjToStr(tblContactList.getValueAt(row, ADDRTYPE_COLNO)));
        } catch (Exception e) {
            parseException.logException(e, true);
        }
    }

    public void deletePhoneDetails(int row) {
        try {
            if (deletedPhoneMap == null) {
                deletedPhoneMap = new HashMap();
            }
            final Object selectedRow = ((ArrayList) tblPhoneList.getDataArrayList().get(row)).get(0);
            // LinkedHashMap tempHashMap = new LinkedHashMap();
            CustomerPhoneTO objCustomerPhoneTO = (CustomerPhoneTO) phoneList.get(new Double(CommonUtil.convertObjToStr(selectedRow)));
            objCustomerPhoneTO.setStatus(CommonConstants.STATUS_DELETED);
            objCustomerPhoneTO.setStatusBy(TrueTransactMain.USER_ID);
            objCustomerPhoneTO.setStatusDt(curDate);
            deletedPhoneMap.put(new Double(CommonUtil.convertObjToStr(selectedRow)), phoneList.get(new Double(CommonUtil.convertObjToStr(selectedRow))));
            phoneList.remove(new Double(CommonUtil.convertObjToStr(selectedRow)));
            resetDeletePhoneDetails();
        } catch (Exception e) {
            parseException.logException(e, true);
        }
    }
    
     public void populateProof(int row) {
        try {
            proofTypeChanged(CommonUtil.convertObjToStr(tblProofList.getValueAt(row, PROOFTYPE_COLNO)));
        } catch (Exception e) {
            parseException.logException(e, true);
        }
    }
    
      /**
     * To delete the selected proof details
     */
     public void deleteProofDetails(int row) {
        try {
            if (deletedProofMap == null) {
                deletedProofMap = new HashMap();
            }
            final Object selectedRow = ((ArrayList) tblProofList.getDataArrayList().get(row)).get(0);
            CustomerProofTo objCustomerProofTo = (CustomerProofTo) proofMap.get(CommonUtil.convertObjToStr(selectedRow));
            objCustomerProofTo.setStatus(CommonConstants.STATUS_DELETED);
            deletedProofMap.put(CommonUtil.convertObjToStr(selectedRow), proofMap.get(CommonUtil.convertObjToStr(selectedRow)));
            proofMap.remove(CommonUtil.convertObjToStr(selectedRow));
            resetDeleteProofDetails();
        } catch (Exception e) {
            parseException.logException(e, true);
        }
    }      
    /**
     * To delete the selected address
     */
    public void deleteAddress(int row) {
        if (deletedAddressMap == null) {
            deletedAddressMap = new HashMap();
        }
        // Modified the method by nithya on 23-03-2017 for 6030
       // CustomerProofTo objCustomerProofTO = (CustomerProofTo) addressMap.get(CommonUtil.convertObjToStr(tblContactList.getValueAt(row, ADDRTYPE_COLNO)));
        CustomerAddressTO  objCustomerAddressTO  = (CustomerAddressTO ) addressMap.get(CommonUtil.convertObjToStr(tblContactList.getValueAt(row, ADDRTYPE_COLNO))); // Added by nithya on 23-03-2017
        objCustomerAddressTO.setStatus(CommonConstants.STATUS_DELETED);
        objCustomerAddressTO.setStatusDt(curDate);
        objCustomerAddressTO.setStatusBy(TrueTransactMain.USER_ID);
        deletedAddressMap.put(CommonUtil.convertObjToStr(tblContactList.getValueAt(row, ADDRTYPE_COLNO)), addressMap.get(CommonUtil.convertObjToStr(tblContactList.getValueAt(row, ADDRTYPE_COLNO))));
        addressMap.remove(tblContactList.getValueAt(row, ADDRTYPE_COLNO));
        resetDeleteAddress();
    }

    public void deleteIncome(int row) {
        if (deletedIncomeparMap == null) {
            deletedIncomeparMap = new HashMap();
        }
        CustomerIncomeParticularsTO objCustomerIncomeParticularsTO = (CustomerIncomeParticularsTO) IncParMap.get(CommonUtil.convertObjToStr(tblIncomeParticulars.getValueAt(row, 0)));
        objCustomerIncomeParticularsTO.setStatus(CommonConstants.STATUS_DELETED);
        deletedIncomeparMap.put(CommonUtil.convertObjToStr(tblIncomeParticulars.getValueAt(row, 0)), IncParMap.get(CommonUtil.convertObjToStr(tblIncomeParticulars.getValueAt(row, 0))));
        IncParMap.remove(tblIncomeParticulars.getValueAt(row, 0));
        resetIncomeListTable();
        try {
            populateincParTable();
        } catch (Exception e) {
            parseException.logException(e, true);
        }
//        resetNewIncomeParticulars();
    }

    public void deleteLandDetails(int row) {
        if (deletedLandMap == null) {
            deletedLandMap = new HashMap();
        }
        CustomerLandDetailsTO objCustomerLandDetailsTO = (CustomerLandDetailsTO) LandDetMap.get(CommonUtil.convertObjToStr(tblCustomerLandDetails.getValueAt(row, 0)));
        objCustomerLandDetailsTO.setStatus(CommonConstants.STATUS_DELETED);
        deletedLandMap.put(CommonUtil.convertObjToStr(tblCustomerLandDetails.getValueAt(row, 0)), LandDetMap.get(CommonUtil.convertObjToStr(tblCustomerLandDetails.getValueAt(row, 0))));
        LandDetMap.remove(tblCustomerLandDetails.getValueAt(row, 0));
        resetLandDetailsListTable();
        try {
            populateLand();
        } catch (Exception e) {
            parseException.logException(e, true);
        }
//        resetNewIncomeParticulars();
    }

    /**
     * If addresstype changed, then populate the corresponding phone details
     */
    public void addressTypeChanged(String selectedItem) {
        try {
            addressTypeExists = true;
            final CustomerAddressTO objCustomerAddressTO = (CustomerAddressTO) addressMap.get(selectedItem);
            populateAddressData(objCustomerAddressTO);
            if (phoneMap != null) {
                if (phoneMap.get(selectedItem) != null) {
                    phoneList = (HashMap) phoneMap.get(selectedItem);
                } else {
                    phoneList = null;
                }
            }
            resetPhoneDetails();
            resetPhoneListTable();
            populatePhoneTable();
            ttNotifyObservers();
        } catch (Exception e) {
            parseException.logException(e, true);
        }
    }

  public void proofTypeChanged(String selectedItem) {
        try {
            proofTypeExists = true;
            final CustomerProofTo objCustomerProofTo = (CustomerProofTo) proofMap.get(selectedItem);
            populateProofData(objCustomerProofTo);
            ttNotifyObservers();
        } catch (Exception e) {
            parseException.logException(e, true);
        }
    }
    
    public void IncomeChanged(String selectedItem) {
        try {
            final CustomerIncomeParticularsTO objCustomerIncomeParticularsTO = (CustomerIncomeParticularsTO) IncParMap.get(selectedItem);
            resetNewIncomeParticulars();
            populateIncParticulars(objCustomerIncomeParticularsTO);
            ttNotifyObservers();
        } catch (Exception e) {
            parseException.logException(e, true);
        }
    }

    public void LandDetChanged(String selectedItem) {
        try {
            final CustomerLandDetailsTO objCustomerLandDetailsTO = (CustomerLandDetailsTO) LandDetMap.get(selectedItem);
            resetNewLandDetails();
            populateLandDetails(objCustomerLandDetailsTO);
            ttNotifyObservers();
        } catch (Exception e) {
            parseException.logException(e, true);
        }
    }

    /**
     * To populate the given phone row
     */
    public void populatePhone(int row) {
        try {
            final Object selectedRow = ((ArrayList) tblPhoneList.getDataArrayList().get(row)).get(0);
            final Double selectedRowValue = CommonUtil.convertObjToDouble(selectedRow);
            CustomerPhoneTO objCustomerPhoneTO = (CustomerPhoneTO) phoneList.get(selectedRowValue);
            populatePhoneData(objCustomerPhoneTO);
        } catch (Exception e) {
            parseException.logException(e, true);
        }
    }

    /**
     * To do the necessary action
     */
    public HashMap doAction(IntroducerOB objIntroducerOB) {
        HashMap proxyResultMap = new HashMap();
        if (data == null) {
            data = new HashMap();
        }
        try {
            if (actionType != ClientConstants.ACTIONTYPE_CANCEL) {
                switch (actionType) {
                    case ClientConstants.ACTIONTYPE_NEW:
                        insertData();
                        break;
                    case ClientConstants.ACTIONTYPE_EDIT:
                        updateData();
                        break;
                    case ClientConstants.ACTIONTYPE_DELETE:
                        deleteData();
                        break;
                    default:
                    // throw new ActionNotFoundException();
                }
                data.put(CommonConstants.MODULE, getModule());
                data.put(CommonConstants.SCREEN, getScreen());
                data.put(CommonConstants.BRANCH_ID, getSelectedBranchID());

                /* load the introduction details based on the type of introduction
                 * selected in the UI
                 */
                if (get_authorizeMap() == null) {
                    final String INTROTYPE = CommonUtil.convertObjToStr(cbmIntroType.getKeyForSelected());
                    HashMap introDataMap = new HashMap();
                    data.put("INTRO_TYPE", INTROTYPE);
                    introDataMap = objIntroducerOB.getData(INTROTYPE);
                    data.put(INTROTYPE, introDataMap.get(INTROTYPE));
                    //__ If the IntroType is Modified, Delete the Data for the previous type...
                    String prevIntroType = "";
                    if (CommonUtil.convertObjToStr(objIntroducerOB.getPrevIntroType()).equalsIgnoreCase(INTROTYPE)) {
                        prevIntroType = "";
                    } else {
                        prevIntroType = CommonUtil.convertObjToStr(objIntroducerOB.getPrevIntroType());
                    }
                    data.put("PREVINTROTYPE", prevIntroType);
                } else {
                    data.put(CommonConstants.AUTHORIZEMAP, get_authorizeMap());
                }
               if(getLoanCustMap()!=null&&getLoanCustMap().containsKey("LOAN_CUSTOMER_ID"))
               {
             data.put("LOAN_CUSTOMER_ID","LOAN_CUSTOMER_ID");
               }
                data.put(CommonConstants.BRANCH_ID, ProxyParameters.BRANCH_ID);
                data.put(CommonConstants.SELECTED_BRANCH_ID, getSelectedBranchID());
                proxyResultMap = proxy.execute(data, map);
                if (proxyResultMap != null && proxyResultMap.containsKey("CUST_ID")) {
                    custId = CommonUtil.convertObjToStr(proxyResultMap.get("CUST_ID"));
                }
                setProxyReturnMap(proxyResultMap);
//                storePhotoSign();
//                resetForm();
                _authorizeMap = null;
                setResult(actionType);
                actionType = ClientConstants.ACTIONTYPE_CANCEL;
            }
        } catch (Exception e) {
            setResult(ClientConstants.ACTIONTYPE_FAILED);
            parseException.logException(e, true);
            e.printStackTrace();
        }
        return proxyResultMap;
    }

//    private void storePhotoSign() throws Exception {
//        try{
//        DriverManager.registerDriver (new oracle.jdbc.driver.OracleDriver());
//        setDriver();
//        conn = DriverManager.getConnection (dataBaseURL, userName, passWord);
//                             // @machineName:port:SID,   userid,  password
//        conn.setAutoCommit(false);
//        stmt = conn.createStatement();
//        boolean b=false;
////        if( cmd.equals(CommonConstants.TOSTATUS_INSERT) )
////            b = stmt.execute("UPDATE CUSTOMER SET PHOTO_FILE = empty_blob(), SIGNATURE_FILE = empty_blob() WHERE CUST_ID='"+ custId +"'");
////        conn.commit();
//        System.out.println ("Statement executed : "+b);   // Print col 1
//        rset = stmt.executeQuery("SELECT PHOTO_FILE, SIGNATURE_FILE, CUST_ID FROM CUSTOMER WHERE CUST_ID='"+ custId +"' FOR UPDATE");
//        System.out.println ("Statement execute query : "+rset);
//        rset.next();
//        System.out.println ("rset.next()... Photo.... ");
//        BLOB oracleBlob = ((OracleResultSet)rset).getBLOB(1);
//        System.out.println("selected file length = " + photoByteArray.length());
//        System.out.println("cust_id = " + rset.getString(3));
//        FileInputStream reader = new FileInputStream(photoByteArray);
//        System.out.println("reader initialized ");
//        OutputStream outstream = oracleBlob.getBinaryOutputStream();
//        System.out.println("outstream initialized ");
//        int size = oracleBlob.getBufferSize();
//        byte[] buffer = new byte[size];
//        int length = -1;
//        while ((length = reader.read(buffer)) != -1) {
//            System.out.println("length : "+length);
//            outstream.write(buffer);
//        }
//        System.out.println("outstream written ");
//        reader.close();
//        outstream.close();
//
//        System.out.println ("rset.next()... Signature.... ");
//        oracleBlob = ((OracleResultSet)rset).getBLOB(2);
//        System.out.println("selected file length = " + signByteArray.length());
//        System.out.println("cust_id = " + rset.getString(3));
//        reader = new FileInputStream(signByteArray);
//        System.out.println("reader initialized ");
//        outstream = oracleBlob.getBinaryOutputStream();
//        System.out.println("outstream initialized ");
//        size = oracleBlob.getBufferSize();
//        buffer = new byte[size];
//        length = -1;
//        int oldLength = 0;
//        while ((length = reader.read(buffer)) != -1) {
//            System.out.println("length : "+length);
////            length = oldLength + length;
//            outstream.write(buffer,oldLength,length);
//            oldLength = length;
//        }
//        System.out.println("outstream written ");
//        reader.close();
//        System.out.println("reader closed ");
//        outstream.close();
//        System.out.println("outstream closed ");
//        stmt.close();
//        System.out.println("stmt closed ");
//        conn.commit();
//        conn.close();
//        rset.close();
////        if( cmd.equals(CommonConstants.TOSTATUS_INSERT) ){
////            customerTO = null;
////        }
//        } catch(Exception se) {
//            System.out.println("SQL Exception : "+se);
//            conn.close();
//            stmt.close();
//            rset.close();
////            if( cmd.equals(CommonConstants.TOSTATUS_INSERT) )
////                customerTO = null;
//        }                
//    }    
    private void setDriver() throws Exception {
        java.util.Properties serverProperties = new java.util.Properties();
        try {
            Dummy cons = new Dummy();
            serverProperties.load(cons.getClass().getResourceAsStream("/com/see/truetransact/serverutil/SqlMapConfig.properties"));
            dataBaseURL = serverProperties.getProperty("url");
            userName = serverProperties.getProperty("username");
            passWord = serverProperties.getProperty("password");
            serverProperties = null;
            cons = null;
        } catch (Exception ex) {
        }
    }

    private void getPhotoSign() throws Exception {
        try {
//           mapPhotoSign = new HashMap();
            DriverManager.registerDriver(new oracle.jdbc.driver.OracleDriver());
            setDriver();
            conn = DriverManager.getConnection(dataBaseURL, userName, passWord);
            // @machineName:port:SID,   userid,  password
            conn.setAutoCommit(false);
            stmt = conn.createStatement();
            rset = stmt.executeQuery("select CUST_ID, PHOTO_FILE, SIGNATURE_FILE from CUSTOMER where CUST_ID = '" + getTxtCustomerID() + "'");
            if (rset != null) {
                while (rset.next()) {
                    BLOB oracleBlob = ((OracleResultSet) rset).getBLOB(2);
                    // get the length of the blob
                    long length = oracleBlob.length();
                    // print the length of the blob
                    try {
                        File f1;
                        FileOutputStream out;
                        InputStream in;
                        if (length != 0) {
                            f1 = new File("C:\\" + getTxtCustomerID() + "_photo.jpg");
                            f1.createNewFile();
                            out = new FileOutputStream(f1);
                            in = oracleBlob.getBinaryStream();
                            int fileLength = oracleBlob.getBufferSize();
                            byte[] blobBytes = new byte[fileLength];
                            int len = -1;
                            in.read(blobBytes);
                            out.write(blobBytes);
                            out.close();
                            in.close();
                            setPhotoFile(f1.getAbsolutePath());

                            oracleBlob = ((OracleResultSet) rset).getBLOB(3);
                            // get the length of the blob
                            length = oracleBlob.length();

                            // print the length of the blob
                            f1 = new File("C:\\" + getTxtCustomerID() + "_sign.jpg");
                            f1.createNewFile();
                            out = new FileOutputStream(f1);
                            in = oracleBlob.getBinaryStream();
                            fileLength = oracleBlob.getBufferSize();
                            blobBytes = new byte[fileLength];
                            len = -1;
                            in.read(blobBytes);
                            out.write(blobBytes);
                            out.close();
                            in.close();
                            setSignFile(f1.getAbsolutePath());
//                System.out.println("##### PHOTO SIGN MAP : " + mapPhotoSign);
//                data.put("PHOTOSIGN",mapPhotoSign);
                        }
//                mapPhotoSign = null;
                        f1 = null;
                        out = null;
                        in = null;
                        oracleBlob = null;
                    } catch (Exception ioe) {
                        System.out.println("##### Exception : " + ioe);
                    }
                }
                conn.close();
                stmt.close();
                rset.close();
            }
        } catch (java.sql.SQLException se) {
            System.out.println("SQL Exception : " + se);
        }
    }

    /**
     * To delete customer data
     */
    private void deleteData() throws Exception {
        objCustomerTO = new CustomerTO();
        objCustomerTO.setCommand(CommonConstants.TOSTATUS_DELETE);
        objCustomerTO.setCustId(txtCustomerID);
        objCustomerTO.setStatusBy(TrueTransactMain.USER_ID);
        objCustomerTO.setStatusDt(curDate);
        objCustomerTO.setStatus(CommonConstants.STATUS_DELETED);
        objCustomerTO.setCustId(txtCustomerID);
        objCustomerTO.setDeleteRemarks(getDeletedRemarks());
        data = new HashMap();
        data.put("CUSTOMER", objCustomerTO);
        for (int i = 0; i < tblContactList.getRowCount(); i++) {
            CustomerAddressTO objCustomerAddressTO = new CustomerAddressTO();
            objCustomerAddressTO.setStatus(CommonConstants.STATUS_MODIFIED);
            objCustomerAddressTO.setStatusBy(TrueTransactMain.USER_ID);
            objCustomerAddressTO.setStatusDt(curDate);
            objCustomerAddressTO.setCustId(txtCustomerID);
            objCustomerAddressTO.setAddrType(CommonUtil.convertObjToStr(tblContactList.getValueAt(i, 0)));
            if (addressMap == null) {
                addressMap = new HashMap();
            } else {
                addressMap.put(CommonUtil.convertObjToStr(tblContactList.getValueAt(i, 0)), objCustomerAddressTO);
            }
            if (phoneMap != null && phoneMap.containsKey(objCustomerAddressTO.getAddrType())) {
                HashMap tempMap = (HashMap) phoneMap.get(objCustomerAddressTO.getAddrType());
                if (tempMap != null) {
                    ArrayList keys = new ArrayList(tempMap.keySet());
                    CustomerPhoneTO customerPhoneTO;
                    for (int x = 0; x < keys.size(); x++) {
                        customerPhoneTO = (CustomerPhoneTO) tempMap.get((Double) keys.get(x));
                        customerPhoneTO.setStatus(CommonConstants.STATUS_MODIFIED);
                        customerPhoneTO.setStatusBy(TrueTransactMain.USER_ID);
                        customerPhoneTO.setStatusDt(curDate);
                        tempMap.put(new Double(x), customerPhoneTO);
                    }
                }
                phoneMap.put(objCustomerAddressTO.getAddrType(), tempMap);
            }
        }

        for (int i = 0; i < tblIncomeParticulars.getRowCount(); i++) {
            CustomerIncomeParticularsTO objCustomerIncomeParticularsTO = new CustomerIncomeParticularsTO();
            objCustomerIncomeParticularsTO.setStatus(CommonConstants.STATUS_DELETED);
            objCustomerIncomeParticularsTO.setSlno(CommonUtil.convertObjToStr(tblIncomeParticulars.getValueAt(i, 0)));
            objCustomerIncomeParticularsTO.setCustId(txtCustomerID);
            if (IncParMap == null) {
                IncParMap = new LinkedHashMap();
            } else {
                IncParMap.put(CommonUtil.convertObjToStr(tblIncomeParticulars.getValueAt(i, 0)), objCustomerIncomeParticularsTO);
            }
        }
        for (int i = 0; i < tblCustomerLandDetails.getRowCount(); i++) {
            CustomerLandDetailsTO objCustomerLandDetailsTO = new CustomerLandDetailsTO();
            objCustomerLandDetailsTO.setStatus(CommonConstants.STATUS_DELETED);
            objCustomerLandDetailsTO.setSlno(CommonUtil.convertObjToStr(tblCustomerLandDetails.getValueAt(i, 0)));
            objCustomerLandDetailsTO.setCustId(txtCustomerID);
            if (LandDetMap == null) {
                LandDetMap = new LinkedHashMap();
            } else {
                LandDetMap.put(CommonUtil.convertObjToStr(tblCustomerLandDetails.getValueAt(i, 0)), objCustomerLandDetailsTO);
            }
        }
        data.put("ADDRESS", addressMap);
        
        data.put("PHONE", phoneMap);
        data.put("INCOMEPAR", IncParMap);
        data.put("LANDDETAILS", LandDetMap);
    }

    /**
     * To update customer data
     */
    private void updateData() throws Exception {
        setCustomerData();
        if (screenCustType == INDIVIDUAL && getIsMinor()) {
            setGaurdianData();
        }
        if (screenCustType == CORPORATE) {
            setFinanceDetails();
        }
        if (txtPassportFirstName.length() > 0 && txtPassportNo.length() > 0) {
            setPassportDetails();
        } else {
            objCustomerPassPortTO = null;
        }
        if (isIncomePar) {
            setIncomeParticulars();
        }
        if (isLandDetails) {
            setLandDetails();
        }
        if (isCustSuspended) {
            setSuspendCustomer();
        }
        setData();

        objCustomerTO.setCommand(CommonConstants.TOSTATUS_UPDATE);
        objCustomerTO.setCustId(txtCustomerID);
        objCustomerTO.setStatus(CommonConstants.STATUS_MODIFIED);
        objCustomerTO.setStatusBy(TrueTransactMain.USER_ID);
        objCustomerTO.setStatusDt(curDate);
    }

    /**
     * To initialize HashMap and put Personal details, address details & phone
     * details and this HashMap data will be used by insert & update methods to
     * data to server
     */
    private void setData() {
        System.out.println("Addressmap size : " + addressMap.size());
        data = new HashMap();
        data.put("PHONE", phoneMap);
        phoneMap = null;
        phoneList = null;
        data.put("CUSTOMER", objCustomerTO);
        data.put("ADDRESS", addressMap);
        data.put("Proof", proofMap);
        addressMap = null;
        data.put("INCOMEPAR", IncParMap);
        IncParMap = null;
        data.put("LANDDETAILS", LandDetMap);
        LandDetMap = null;
        if (objCustomerGaurdianTO != null && getIsMinor()) {
            data.put("GAURDIAN", objCustomerGaurdianTO);
        }
        objCustomerGaurdianTO = null;
        if (objCustomerPassPortTO != null) {
            data.put("PASSPORT", objCustomerPassPortTO);
        }
        if (objCustomerSuspensionTO != null && isIsCustSuspended()) {
            data.put("SUSPREVOKED", objCustomerSuspensionTO);
        }
        if (isChkRegioalLang()==true) {
            data.put("REGIONAL_LANG", setCustRegionalTo());
        }else{
          data.remove("REGIONAL_LANG");
        }
        objCustomerGaurdianTO = null;
        objCustomerSuspensionTO = null;
        //--- Puts the data if the Constitution is JointAccount
        //        if(objAccInfoTO.getConstitution().equals("JOINT_ACCOUNT")){
        if (tblJointAccnt.getRowCount() >= 1 && isIstrue()) {
            setIstrue(false);
            data.put(JOINT_ACCNT_FOR_DAO, setJointAccntData());
            jntAcctTOMap = null;
        }
        if (tblJointAccnt.getRowCount() >= 1) {
            data.put("AuthPersonsTO", getCorpAuthCustMap());
        }

        data.put("FINANCE", objCustFinanceDetailsTO);

        if (deletedAddressMap != null) {
            data.put("ADDRESSDELETED", deletedAddressMap);
            deletedAddressMap = null;
        }
        if (deletedPhoneMap != null) {
            data.put("PHONEDELETED", deletedPhoneMap);
            deletedPhoneMap = null;
        }
        if (deletedProofMap != null) {
            data.put("PROOFDELETED", deletedProofMap);
            deletedProofMap = null;
        }
        if (deletedIncomeparMap != null) {
            data.put("INCOMEDELETED", deletedIncomeparMap);
            deletedIncomeparMap = null;
        }
        if (deletedLandMap != null) {
            data.put("LANDDETAILSDELETED", deletedLandMap);
            deletedLandMap = null;
        }
        data.put("PHOTO", this.photoByteArray);
        data.put("SIGN", this.signByteArray);
        data.put("PROOF_PHOTO",this.proofPhotoByteArray);//28-11-2020
    }

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
     * Getter for property cboIntroType.
     *
     * @return Value of property cboIntroType.
     */
    public java.lang.String getCboIntroType() {
        return cboIntroType;
    }

    /**
     * Setter for property cboIntroType.
     *
     * @param cboIntroType New value of property cboIntroType.
     */
    public void setCboIntroType(java.lang.String cboIntroType) {
        this.cboIntroType = cboIntroType;
    }

    /**
     * Getter for property cbmIntroType.
     *
     * @return Value of property cbmIntroType.
     */
    public com.see.truetransact.clientutil.ComboBoxModel getCbmIntroType() {
        return cbmIntroType;
    }

    /**
     * Setter for property cbmIntroType.
     *
     * @param cbmIntroType New value of property cbmIntroType.
     */
    public void setCbmIntroType(com.see.truetransact.clientutil.ComboBoxModel cbmIntroType) {
        this.cbmIntroType = cbmIntroType;
    }

    /**
     * This will show the alertwindow *
     */
    public int showAlertWindow(String alertMsg) throws Exception {
        int optionSelected = 1;
        String[] options = {objCustomerRB.getString("cDialogYes"), objCustomerRB.getString("CDialogNo")};
        optionSelected = COptionPane.showOptionDialog(null, objCustomerRB.getString(alertMsg), CommonConstants.INFORMATIONTITLE,
                COptionPane.YES_NO_OPTION, COptionPane.WARNING_MESSAGE,
                null, options, options[0]);
        return optionSelected;
    }

    private void setJntAcccntTblCol() throws Exception {
        try {
            tblJointAccntColTitle = new ArrayList();
            tblJointAccntColTitle.add(objCommRB.getString(TBL_JNT_ACCNT_COLUMN_1));
            tblJointAccntColTitle.add(objCommRB.getString(TBL_JNT_ACCNT_COLUMN_2));
            tblJointAccntColTitle.add(objCommRB.getString(TBL_JNT_ACCNT_COLUMN_3));
            tblJointAccntColTitle.add(objCommRB.getString(TBL_JNT_ACCNT_COLUMN_4));
        } catch (Exception e) {
            parseException.logException(e, true);
        }
    }

    //--- resets the Joint Accnt Holder Table
    public void resetJntAccntHoldTbl() {
        tblJointAccnt.setDataArrayList(null, tblJointAccntColTitle);
    }

    public void populateJointAccntTable(HashMap queryWhereMap) {
        try {
            jntAcctAll = objJointAcctHolderManipulation.populateJointAccntTable(queryWhereMap, jntAcctAll, tblJointAccnt);
            setTblJointAccnt(objJointAcctHolderManipulation.getTblJointAccnt());
        } catch (Exception e) {
            parseException.logException(e, true);
        }
    }

    void setTblJointAccnt(EnhancedTableModel tblJointAccnt) {
        this.tblJointAccnt = tblJointAccnt;
        setChanged();
    }

    EnhancedTableModel getTblJointAccnt() {
        return this.tblJointAccnt;
    }

    public void moveToMain(String mainAccntRow, String strRowSelected, int intRowSelected) {
        jntAcctAll = objJointAcctHolderManipulation.moveToMain(mainAccntRow, strRowSelected, intRowSelected, tblJointAccnt, jntAcctAll);
        setTxtAuthCustId(strRowSelected);
        setTblJointAccnt(objJointAcctHolderManipulation.getTblJointAccnt());
    }

    public void delJointAccntHolder(String strDelRowCount, int intDelRowCount) {

        jntAcctAll = objJointAcctHolderManipulation.delJointAccntHolder(strDelRowCount, intDelRowCount, tblJointAccnt, jntAcctAll);
        setTblJointAccnt(objJointAcctHolderManipulation.getTblJointAccnt());
    }

    private void setJointAcctDetails(List jntAccntDetailsList) {
        jntAcctAll = new LinkedHashMap();
        HashMap custMapData;
        HashMap calcJointAcct;
        List custListData;
        if (jntAccntDetailsList.size() > 0) {
            int jntAccntDetailsListSize = jntAccntDetailsList.size();
            for (int i = 0; i < jntAccntDetailsListSize; i++) {
                calcJointAcct = (HashMap) jntAccntDetailsList.get(i);
                jntAcctSingleRec = new HashMap();
                jntAcctSingleRec.put("AUTHORIZE_CUST_ID", CommonUtil.convertObjToStr(calcJointAcct.get("AUTHORIZE_CUST_ID")));
                jntAcctSingleRec.put(FLD_FOR_DB_YES_NO, YES_FULL_STR);
                jntAcctSingleRec.put("STATUS", CommonUtil.convertObjToStr(calcJointAcct.get("STATUS")));

                jntAcctAll.put(CommonUtil.convertObjToStr(calcJointAcct.get("AUTHORIZE_CUST_ID")), jntAcctSingleRec);
                if (!CommonUtil.convertObjToStr(calcJointAcct.get("STATUS")).equals("DELETED")) {
                    custListData = ClientUtil.executeQuery("getCustSelectAccInfoTblDisplay", jntAcctSingleRec);
                    if (custListData.size() > 0) {
                        custMapData = (HashMap) custListData.get(0);
                        objJointAcctHolderManipulation.setJntAcctTableData(custMapData, true, tblJointAccnt);
                        setTblJointAccnt(objJointAcctHolderManipulation.getTblJointAccnt());
                    }
                }
                custMapData = null;
                jntAcctSingleRec = null;
                calcJointAcct = null;
                custListData = null;
            }
        }
        notifyObservers();
    }

    private void setCorpAuthPerDetails(List corpAuthPerDetailsList) {
        corpAuthCustMap = new HashMap();
        HashMap singleRec;
        HashMap calcAuthPer;
        String keyValue = "";
        if (corpAuthPerDetailsList.size() > 0) {
            int corpAuthPerDetailsListSize = corpAuthPerDetailsList.size();
            for (int i = 0; i < corpAuthPerDetailsListSize; i++) {
                calcAuthPer = (HashMap) corpAuthPerDetailsList.get(i);
                singleRec = new HashMap();
                singleRec.put("AUTHORIZE_CUST_ID", CommonUtil.convertObjToStr(calcAuthPer.get("AUTHORIZE_CUST_ID")));
                singleRec.put("PHOTO_FILE", CommonUtil.convertObjToStr(calcAuthPer.get("PHOTO_FILE")));
                singleRec.put("SIGNATURE_FILE", CommonUtil.convertObjToStr(calcAuthPer.get("SIGNATURE_FILE")));
                corpAuthCustMap.put(CommonUtil.convertObjToStr(calcAuthPer.get("AUTHORIZE_CUST_ID")), singleRec);
            }
            singleRec = null;
            calcAuthPer = null;
        }
    }

    /**
     * Getter for property lblValPin.
     *
     * @return Value of property lblValPin.
     */
    public java.lang.String getLblValPin() {
        return lblValPin;
    }

    /**
     * Setter for property lblValPin.
     *
     * @param lblValPin New value of property lblValPin.
     */
    public void setLblValPin(java.lang.String lblValPin) {
        this.lblValPin = lblValPin;
    }

    /**
     * Getter for property lblValPin.
     *
     * @return Value of property lblValPin.
     */
    public java.lang.String getChkClosed() {
        return chkClosed;
    }

    /**
     * Setter for property lblValPin.
     *
     * @param lblValPin New value of property lblValPin.
     */
    public void setChkClosed(java.lang.String chkClosed) {
        this.chkClosed = chkClosed;
    }

    /**
     * Getter for property lblValCountry.
     *
     * @return Value of property lblValCountry.
     */
    public java.lang.String getLblValCountry() {
        return lblValCountry;
    }

    /**
     * Setter for property lblValCountry.
     *
     * @param lblValCountry New value of property lblValCountry.
     */
    public void setLblValCountry(java.lang.String lblValCountry) {
        this.lblValCountry = lblValCountry;
    }

    /**
     * Getter for property lblValState.
     *
     * @return Value of property lblValState.
     */
    public java.lang.String getLblValState() {
        return lblValState;
    }

    /**
     * Setter for property lblValState.
     *
     * @param lblValState New value of property lblValState.
     */
    public void setLblValState(java.lang.String lblValState) {
        this.lblValState = lblValState;
    }

    /**
     * Getter for property lblValCity.
     *
     * @return Value of property lblValCity.
     */
    public java.lang.String getLblValCity() {
        return lblValCity;
    }

    /**
     * Setter for property lblValCity.
     *
     * @param lblValCity New value of property lblValCity.
     */
    public void setLblValCity(java.lang.String lblValCity) {
        this.lblValCity = lblValCity;
    }

    /**
     * Getter for property lblValArea.
     *
     * @return Value of property lblValArea.
     */
    public java.lang.String getLblValArea() {
        return lblValArea;
    }

    /**
     * Setter for property lblValArea.
     *
     * @param lblValArea New value of property lblValArea.
     */
    public void setLblValArea(java.lang.String lblValArea) {
        this.lblValArea = lblValArea;
    }

    /**
     * Getter for property lblValStreet.
     *
     * @return Value of property lblValStreet.
     */
    public java.lang.String getLblValStreet() {
        return lblValStreet;
    }

    /**
     * Setter for property lblValStreet.
     *
     * @param lblValStreet New value of property lblValStreet.
     */
    public void setLblValStreet(java.lang.String lblValStreet) {
        this.lblValStreet = lblValStreet;
    }

    /**
     * Getter for property lblValDateOfBirth.
     *
     * @return Value of property lblValDateOfBirth.
     */
    public java.lang.String getLblValDateOfBirth() {
        return lblValDateOfBirth;
    }

    /**
     * Setter for property lblValDateOfBirth.
     *
     * @param lblValDateOfBirth New value of property lblValDateOfBirth.
     */
    public void setLblValDateOfBirth(java.lang.String lblValDateOfBirth) {
        this.lblValDateOfBirth = lblValDateOfBirth;
    }

    /**
     * Getter for property lblValCustomerName.
     *
     * @return Value of property lblValCustomerName.
     */
    public java.lang.String getLblValCustomerName() {
        return lblValCustomerName;
    }

    /**
     * Setter for property lblValCustomerName.
     *
     * @param lblValCustomerName New value of property lblValCustomerName.
     */
    public void setLblValCustomerName(java.lang.String lblValCustomerName) {
        this.lblValCustomerName = lblValCustomerName;
    }

    /**
     * Getter for property txtBranchId.
     *
     * @return Value of property txtBranchId.
     */
    public java.lang.String getTxtBranchId() {
        return txtBranchId;
    }

    /**
     * Setter for property txtBranchId.
     *
     * @param txtBranchId New value of property txtBranchId.
     */
    public void setTxtBranchId(java.lang.String txtBranchId) {
        this.txtBranchId = txtBranchId;
    }

    /**
     * Getter for property chkSentThanksLetter.
     *
     * @return Value of property chkSentThanksLetter.
     */
    public boolean getChkSentThanksLetter() {
        return chkSentThanksLetter;
    }

    /**
     * Setter for property chkSentThanksLetter.
     *
     * @param chkSentThanksLetter New value of property chkSentThanksLetter.
     */
    public void setChkSentThanksLetter(boolean chkSentThanksLetter) {
        this.chkSentThanksLetter = chkSentThanksLetter;
        setChanged();
    }

    /**
     * Getter for property chkConfirmationfromIntroducer.
     *
     * @return Value of property chkConfirmationfromIntroducer.
     */
    public boolean getChkConfirmationfromIntroducer() {
        return chkConfirmationfromIntroducer;
    }

    /**
     * Setter for property chkConfirmationfromIntroducer.
     *
     * @param chkConfirmationfromIntroducer New value of property
     * chkConfirmationfromIntroducer.
     */
    public void setChkConfirmationfromIntroducer(boolean chkConfirmationfromIntroducer) {
        this.chkConfirmationfromIntroducer = chkConfirmationfromIntroducer;
        setChanged();
    }

    /**
     * Getter for property txtStaffId.
     *
     * @return Value of property txtStaffId.
     */
    public java.lang.String getTxtStaffId() {
        return txtStaffId;
    }

    /**
     * Setter for property txtStaffId.
     *
     * @param txtStaffId New value of property txtStaffId.
     */
    public void setTxtStaffId(java.lang.String txtStaffId) {
        this.txtStaffId = txtStaffId;
        setChanged();
    }

    //--- resets the Customer Details
    public void resetCustDetails() {
        setLblValArea("");
        setLblValCity("");
        setLblValCountry("");
        setLblValCorpCustDesig("");
        setLblValCustomerName("");
        setLblValDateOfBirth("");
        setLblValPin("");
        setLblValState("");
        setLblValStreet("");
    }

    /* To set Joint Account data in the Transfer Object*/
    public HashMap setJointAccntData() {
        HashMap singleRecordJntAcct;
        jntAcctTOMap = new LinkedHashMap();
        try {
            JointAccntTO objJointAccntTO;
            int jntAcctSize = jntAcctAll.size();
            for (int i = 0; i < jntAcctSize; i++) {
                singleRecordJntAcct = (HashMap) jntAcctAll.get(CommonUtil.convertObjToStr(jntAcctAll.keySet().toArray()[i]));
                objJointAccntTO = new JointAccntTO();
                if (getCommand().equals(CommonConstants.TOSTATUS_INSERT)) {
                    objJointAccntTO.setCustId(CommonUtil.convertObjToStr(singleRecordJntAcct.get("CUST_ID")));
                } else {
                    if (singleRecordJntAcct.get(FLD_FOR_DB_YES_NO).equals(YES_FULL_STR)) {
                        objJointAccntTO.setCustId(CommonUtil.convertObjToStr(singleRecordJntAcct.get("AUTHORIZE_CUST_ID")));
                    } else {
                        objJointAccntTO.setCustId(CommonUtil.convertObjToStr(singleRecordJntAcct.get("CUST_ID")));
                    }
                }
                objJointAccntTO.setDepositNo(getTxtCustomerID());
                objJointAccntTO.setStatus(CommonUtil.convertObjToStr(singleRecordJntAcct.get("STATUS")));
                objJointAccntTO.setCommand(getCommand());
                jntAcctTOMap.put(String.valueOf(i), objJointAccntTO);
                objJointAccntTO = null;
                singleRecordJntAcct = null;
            }
        } catch (Exception e) {
            parseException.logException(e, true);
        }
        return jntAcctTOMap;
    }

    /* To get the command type according to the Action */
    private String getCommand() throws Exception {
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
            case ClientConstants.ACTIONTYPE_RENEW:
                command = CommonConstants.TOSTATUS_RENEW;
                break;
            default:
        }

        return command;
    }

    public void resetCustomerTable() {
        final ArrayList tblhistorycolumn = new ArrayList();
        tblhistorycolumn.add(objCustomerRB.getString("tblHistoryTitle1"));
        tblhistorycolumn.add(objCustomerRB.getString("tblHistoryTitle2"));
        tblhistorycolumn.add(objCustomerRB.getString("tblHistoryTitle3"));
        tblhistorycolumn.add(objCustomerRB.getString("tblHistoryTitle4"));
        tblhistorycolumn.add(objCustomerRB.getString("tblHistoryTitle5"));
        tblhistorycolumn.add(objCustomerRB.getString("tblHistoryTitle6"));
        tblhistorycolumn.add(objCustomerRB.getString("tblHistoryTitle7"));
        tblhistorycolumn.add(objCustomerRB.getString("tblHistoryTitle8"));
        tblhistorycolumn.add(objCustomerRB.getString("tblHistoryTitle9"));
        tblhistorycolumn.add(objCustomerRB.getString("tblHistoryTitle10"));
        tblhistorycolumn.add(objCustomerRB.getString("tblHistoryTitle11"));
        tbmCustomerHistory = new EnhancedTableModel(null, tblhistorycolumn);
    }

    /**
     * Method used to insert rows in to tbmCusotmerHistory according to the
     * CustomerID entered in the UI *
     */
    public void fillTbmCustomerHistory(String customerId) {
        HashMap where = new HashMap();
        rowNum = 0;
        where.put("CUST_ID", customerId);
        ArrayList list = (ArrayList) ClientUtil.executeQuery("getSelectCustomerHistory", where);
        if (list.size() > 0) {
            for (int i = 0; i < list.size(); i++) {
                HashMap resultMap = (HashMap) list.get(i);
//                if(resultMap.get("PRODUCT_TYPE")!=null && resultMap.get("PROD_ID")!=null)
                if (!CommonUtil.convertObjToStr(resultMap.get("PRODUCT_TYPE")).equals("TD")) {
                    ArrayList data = new ArrayList();
                    data.add(resultMap.get("CUST_ID"));
                    data.add(resultMap.get("RELATIONSHIP"));
                    data.add(resultMap.get("PROD_ID"));
                    data.add(resultMap.get("PRODUCT_TYPE"));
                    data.add(resultMap.get("ACCT_NO"));
                    data.add(resultMap.get("FROM_DT"));
                    data.add(resultMap.get("TO_DT"));
                    data.add(resultMap.get("BALANCE"));
                    data.add("");
                    data.add("");
                    data.add(resultMap.get("STATUS"));
                    tbmCustomerHistory.insertRow(rowNum, data);
                    rowNum++;
                }
            }
        }
    }

    /**
     * Method used to insert rows in to tbmCusotmerHistory according to the
     * CustomerID entered in the UI *
     */
    public void fillTbmAllCustomerHistory(String status) {
        HashMap where = new HashMap();
        rowNum = 0;
        if (ALL_ACCOUNT_LIST != null && ALL_ACCOUNT_LIST.size() > 0) {
            for (int i = 0; i < ALL_ACCOUNT_LIST.size(); i++) {
                HashMap resultMap = (HashMap) ALL_ACCOUNT_LIST.get(i);
                ArrayList data = new ArrayList();
                if (!status.equals(CommonUtil.convertObjToStr(resultMap.get("STATUS")))) {
                    data.add(resultMap.get("CUST_ID"));
                    data.add(resultMap.get("RELATIONSHIP"));
                    data.add(resultMap.get("PROD_ID"));
                    data.add(resultMap.get("PRODUCT_TYPE"));
                    data.add(resultMap.get("ACCT_NO"));
                    data.add(resultMap.get("FROM_DT"));
                    data.add(resultMap.get("TO_DT"));
                    data.add(resultMap.get("BALANCE"));
                    data.add("");
                    data.add("");
                    data.add(resultMap.get("STATUS"));
                    tbmCustomerHistory.insertRow(rowNum, data);
                    rowNum++;
                }
            }
        }
    }

    public void fillTbmCustomerDepositHistory(String customerId) {
        HashMap custMap = new HashMap();
        custMap.put("CUST_ID", customerId);
        ArrayList list = (ArrayList) ClientUtil.executeQuery("getSelectDepositCustDet", custMap);
        if (list.size() > 0) {
            for (int i = 0; i < list.size(); i++) {
                HashMap resultMap = (HashMap) list.get(i);
//                if(resultMap.get("PRODUCT_TYPE")!=null && resultMap.get("PROD_ID")!=null)
                if (resultMap.get("PRODUCT_TYPE").equals("TD")) {//|| (resultMap.get("PRODUCT_TYPE")==null && resultMap.get("PROD_ID")==null)){
                    ArrayList data = new ArrayList();
                    data.add(resultMap.get("CUST_ID"));
                    data.add(resultMap.get("RELATIONSHIP"));
                    data.add(resultMap.get("PROD_ID"));
                    data.add(resultMap.get("PRODUCT_TYPE"));
                    data.add(resultMap.get("ACCT_NO"));
                    data.add(resultMap.get("DEPOSIT_DT"));
                    data.add(resultMap.get("MATURITY_DT"));
                    data.add(resultMap.get("DEPOSIT_AMT"));
                    data.add(resultMap.get("MATURITY_AMT"));
                    data.add(resultMap.get("TOTAL_BALANCE"));
                    data.add(resultMap.get("STATUS"));
                    tbmCustomerHistory.insertRow(rowNum, data);
                    rowNum++;
                }
            }
        }
    }

    private void setPassportDetails() throws Exception {
        objCustomerPassPortTO = new CustomerPassPortTO();
        if (actionType == ClientConstants.ACTIONTYPE_EDIT) {
            objCustomerPassPortTO.setCustId(txtCustomerID);
        }
        objCustomerPassPortTO.setPassfname(CommonUtil.convertObjToStr(txtPassportFirstName));
        objCustomerPassPortTO.setPassmname(CommonUtil.convertObjToStr(txtPassportMiddleName));
        objCustomerPassPortTO.setPasslname(CommonUtil.convertObjToStr(txtPassportLastName));
        objCustomerPassPortTO.setPassNumber(CommonUtil.convertObjToStr(txtPassportNo));
        objCustomerPassPortTO.setPassTitle(CommonUtil.convertObjToStr(getCbmTitle().getKeyForSelected()));
        objCustomerPassPortTO.setIssuePlace(CommonUtil.convertObjToStr(getCbmPassportIssuePlace().getKeyForSelected()));
        objCustomerPassPortTO.setIssueAuth(CommonUtil.convertObjToStr(txtPassportIssueAuth));
        Date issueDt = DateUtil.getDateMMDDYYYY(tdtPassportIssueDt);
        if (issueDt != null) {
            Date dobDate = (Date) curDate.clone();
            dobDate.setDate(issueDt.getDate());
            dobDate.setMonth(issueDt.getMonth());
            dobDate.setYear(issueDt.getYear());
            objCustomerPassPortTO.setIssueDt(dobDate);
        } else {
            objCustomerPassPortTO.setIssueDt(DateUtil.getDateMMDDYYYY(tdtPassportIssueDt));
        }
        Date validDt = DateUtil.getDateMMDDYYYY(tdtPassportValidUpto);
        if (validDt != null) {
            Date dobDate = (Date) curDate.clone();
            dobDate.setDate(validDt.getDate());
            dobDate.setMonth(validDt.getMonth());
            dobDate.setYear(validDt.getYear());
            objCustomerPassPortTO.setValidUpto(dobDate);
        } else {
            objCustomerPassPortTO.setValidUpto(DateUtil.getDateMMDDYYYY(tdtPassportValidUpto));
        }
    }

    private void setIncomeParticulars() throws Exception {
        objCustomerIncomeParticularsTO = new CustomerIncomeParticularsTO();
        if (actionType == ClientConstants.ACTIONTYPE_EDIT) {
            objCustomerIncomeParticularsTO.setCustId(txtCustomerID);
        }
        objCustomerIncomeParticularsTO.setIncName(CommonUtil.convertObjToStr(txtIncName));
        objCustomerIncomeParticularsTO.setIncAmount(CommonUtil.convertObjToStr(txtIncAmount));
        objCustomerIncomeParticularsTO.setIncPackage(CommonUtil.convertObjToStr(getCbmIncPack().getKeyForSelected()));
        objCustomerIncomeParticularsTO.setIncRelation(CommonUtil.convertObjToStr(getCbmIncRelation().getKeyForSelected()));
        objCustomerIncomeParticularsTO.setIncDetProfession(CommonUtil.convertObjToStr(getCbmIncProfession().getKeyForSelected()));
        objCustomerIncomeParticularsTO.setIncDetCompany(CommonUtil.convertObjToStr(incCompany));
        if (isNewIncomeDet()) {
            objCustomerIncomeParticularsTO.setStatus(CommonConstants.STATUS_CREATED);
        } else {
            objCustomerIncomeParticularsTO.setStatus(CommonConstants.STATUS_MODIFIED);
        }
    }

    private void setLandDetails() throws Exception {
        objCustomerLandDetailsTO = new CustomerLandDetailsTO();
        if (actionType == ClientConstants.ACTIONTYPE_EDIT) {
            objCustomerLandDetailsTO.setCustId(txtCustomerID);
        }
        objCustomerLandDetailsTO.setLocation(CommonUtil.convertObjToStr(txtLocation));
        objCustomerLandDetailsTO.setSurvey_no(CommonUtil.convertObjToStr(txtSurNo));
        objCustomerLandDetailsTO.setArea(CommonUtil.convertObjToStr(txtLandDetArea));
        objCustomerLandDetailsTO.setIrrigated(CommonUtil.convertObjToStr(getCbmIrrigated().getKeyForSelected()));
        objCustomerLandDetailsTO.setIrrSrc(CommonUtil.convertObjToStr(getCbmSourceIrrigated().getKeyForSelected()));
        objCustomerLandDetailsTO.setVillage(CommonUtil.convertObjToStr(txtVillage));
        objCustomerLandDetailsTO.setPost(CommonUtil.convertObjToStr(txtPost));
        objCustomerLandDetailsTO.setHobli(CommonUtil.convertObjToStr(txtHobli));
//        objCustomerLandDetailsTO.setState(CommonUtil.convertObjToStr(getCbmLandDetState().getDataForKey(cbmLandDetState)));
        objCustomerLandDetailsTO.setState((String) cbmLandDetState.getKeyForSelected());
        objCustomerLandDetailsTO.setTaluk(CommonUtil.convertObjToStr(getCbmTaluk().getKeyForSelected()));
        objCustomerLandDetailsTO.setDistrict(CommonUtil.convertObjToStr(getCbmDistrict().getKeyForSelected()));
        objCustomerLandDetailsTO.setPin(CommonUtil.convertObjToStr(txtLandDetPin));
        if (isNewLandDet()) {
            objCustomerLandDetailsTO.setStatus(CommonConstants.STATUS_CREATED);
        } else {
            objCustomerLandDetailsTO.setStatus(CommonConstants.STATUS_MODIFIED);
        }
    }

    private void setSuspendCustomer() throws Exception {
        objCustomerSuspensionTO = new CustomerSuspensionTO();
        if (actionType == ClientConstants.ACTIONTYPE_EDIT) {
            objCustomerSuspensionTO.setCustId(txtCustomerID);
        }
        objCustomerSuspensionTO.setSuspendedFromDate(DateUtil.getDateMMDDYYYY(suspendedDate));
        objCustomerSuspensionTO.setRevokedDate(DateUtil.getDateMMDDYYYY(revokedDate));
        objCustomerSuspensionTO.setRemarks(CommonUtil.convertObjToStr(susRevRemarks));
        objCustomerSuspensionTO.setStatusDate(curDate);
        if (isChksuspendedBy()) {
            objCustomerSuspensionTO.setStatus(SUSPENDED);
            objCustomerSuspensionTO.setSuspendedBy(TrueTransactMain.USER_ID);
        } else {
            objCustomerSuspensionTO.setStatus("");
        }
        if (isChkrevokedBy()) {
            objCustomerSuspensionTO.setStatus(REVOKED);
            objCustomerSuspensionTO.setRevokedBy(TrueTransactMain.USER_ID);
        }
    }

    /**
     * Getter for property lblValCorpCustDesig.
     *
     * @return Value of property lblValCorpCustDesig.
     */
    public java.lang.String getLblValCorpCustDesig() {
        return lblValCorpCustDesig;
    }

    /**
     * Setter for property lblValCorpCustDesig.
     *
     * @param lblValCorpCustDesig New value of property lblValCorpCustDesig.
     */
    public void setLblValCorpCustDesig(java.lang.String lblValCorpCustDesig) {
        this.lblValCorpCustDesig = lblValCorpCustDesig;
    }

    /**
     * Getter for property txtDesignation.
     *
     * @return Value of property txtDesignation.
     */
    public java.lang.String getTxtDesignation() {
        return txtDesignation;
    }

    /**
     * Setter for property txtDesignation.
     *
     * @param txtDesignation New value of property txtDesignation.
     */
    public void setTxtDesignation(java.lang.String txtDesignation) {
        this.txtDesignation = txtDesignation;
        setChanged();
    }

    void setTdtNetWorthAsOn(String tdtNetWorthAsOn) {
        this.tdtNetWorthAsOn = tdtNetWorthAsOn;
        //setChanged();
    }

    String getTdtNetWorthAsOn() {
        return this.tdtNetWorthAsOn;
    }

    /**
     * Getter for property newAddress.
     *
     * @return Value of property newAddress.
     */
    public boolean isNewAddress() {
        return newAddress;
    }

    /**
     * Setter for property newAddress.
     *
     * @param newAddress New value of property newAddress.
     */
    public void setNewAddress(boolean newAddress) {
        phoneList = null;//Plz dont Comment this the reason for this usage is given below
        /**
         * phoneList is the HashMap which holds the phone details with serial
         * number as phone details, once its filled up..it will be phone details
         * for particular address hence after this while filling up the
         * addressMap which will be holding address details, the phoneMap is
         * filled up with key, value pairs with key as addressType and the value
         * as this phoneList. So after the user enters the phone details for one
         * address, he may enter new phone details for other address, it may
         * have same Key what the previous phoneList has, when its not made null
         * the previous value will be replaced and the same phone details will
         * be added up to different addressTypes *
         */
        this.newAddress = newAddress;
    }

    public HashMap getPhoneList() {
        return this.phoneList;
    }

    public void setPhoneList(HashMap phoneList) {
        this.phoneList = phoneList;
    }

    /**
     * Getter for property corpAuthCustMap.
     *
     * @return Value of property corpAuthCustMap.
     */
    public java.util.HashMap getCorpAuthCustMap() {
        return corpAuthCustMap;
    }

    /**
     * Setter for property corpAuthCustMap.
     *
     * @param corpAuthCustMap New value of property corpAuthCustMap.
     */
    public void setCorpAuthCustMap(java.util.HashMap corpAuthCustMap) {
        this.corpAuthCustMap = corpAuthCustMap;
    }

    /**
     * Getter for property newGuardian.
     *
     * @return Value of property newGuardian.
     */
    public boolean isNewGuardian() {
        return newGuardian;
    }

    /**
     * Setter for property newGuardian.
     *
     * @param newGuardian New value of property newGuardian.
     */
    public void setNewGuardian(boolean newGuardian) {
        this.newGuardian = newGuardian;
    }

    /**
     * Getter for property txtPanNumber.
     *
     * @return Value of property txtPanNumber.
     */
    public java.lang.String getTxtPanNumber() {
        return txtPanNumber;
    }

    /**
     * Setter for property txtPanNumber.
     *
     * @param txtPanNumber New value of property txtPanNumber.
     */
    public void setTxtPanNumber(java.lang.String txtPanNumber) {
        this.txtPanNumber = txtPanNumber;
    }

    /**
     * Getter for property cbmCaste.
     *
     * @return Value of property cbmCaste.
     */
    public com.see.truetransact.clientutil.ComboBoxModel getCbmCaste() {
        return cbmCaste;
    }

    /**
     * Setter for property cbmCaste.
     *
     * @param cbmCaste New value of property cbmCaste.
     */
    public void setCbmCaste(com.see.truetransact.clientutil.ComboBoxModel cbmCaste) {
        this.cbmCaste = cbmCaste;
    }

    /**
     * Getter for property cboCaste.
     *
     * @return Value of property cboCaste.
     */
    public java.lang.String getCboCaste() {
        return cboCaste;
    }

    /**
     * Setter for property cboCaste.
     *
     * @param cboCaste New value of property cboCaste.
     */
    public void setCboCaste(java.lang.String cboCaste) {
        this.cboCaste = cboCaste;
    }

    /**
     * Getter for property custTypeId.
     *
     * @return Value of property custTypeId.
     */
    public java.lang.String getCustTypeId() {
        return custTypeId;
    }

    /**
     * Setter for property custTypeId.
     *
     * @param custTypeId New value of property custTypeId.
     */
    public void setCustTypeId(java.lang.String custTypeId) {
        this.custTypeId = custTypeId;
    }

    /**
     * Getter for property istrue.
     *
     * @return Value of property istrue.
     */
    public boolean isIstrue() {
        return istrue;
    }

    /**
     * Setter for property istrue.
     *
     * @param istrue New value of property istrue.
     */
    public void setIstrue(boolean istrue) {
        this.istrue = istrue;
    }

    /**
     * Getter for property chkMinor.
     *
     * @return Value of property chkMinor.
     */
    public boolean isChkMinor() {
        return chkMinor;
    }

    /**
     * Setter for property chkMinor.
     *
     * @param chkMinor New value of property chkMinor.
     */
    public void setChkMinor(boolean chkMinor) {
        this.chkMinor = chkMinor;
    }

    /**
     * Getter for property DeletedRemarks.
     *
     * @return Value of property DeletedRemarks.
     */
    public java.lang.String getDeletedRemarks() {
        return DeletedRemarks;
    }

    /**
     * Setter for property DeletedRemarks.
     *
     * @param DeletedRemarks New value of property DeletedRemarks.
     */
    public void setDeletedRemarks(java.lang.String DeletedRemarks) {
        this.DeletedRemarks = DeletedRemarks;
    }

    /**
     * Getter for property lblCreatedDt1.
     *
     * @return Value of property lblCreatedDt1.
     */
    public java.lang.String getLblCreatedDt1() {
        return lblCreatedDt1;
    }

    /**
     * Setter for property lblCreatedDt1.
     *
     * @param lblCreatedDt1 New value of property lblCreatedDt1.
     */
    public void setLblCreatedDt1(java.lang.String lblCreatedDt1) {
        this.lblCreatedDt1 = lblCreatedDt1;
    }

    /**
     * Getter for property isStaff.
     *
     * @return Value of property isStaff.
     */
    public boolean getIsStaff() {
        return isStaff;
    }

    /**
     * Setter for property isStaff.
     *
     * @param isStaff New value of property isStaff.
     */
    public void setIsStaff(boolean isStaff) {
        this.isStaff = isStaff;
    }

    /**
     * Getter for property cboAddrProof.
     *
     * @return Value of property cboAddrProof.
     */
    public java.lang.String getCboAddrProof() {
        return cboAddrProof;
    }

    /**
     * Setter for property cboAddrProof.
     *
     * @param cboAddrProof New value of property cboAddrProof.
     */
    public void setCboAddrProof(java.lang.String cboAddrProof) {
        this.cboAddrProof = cboAddrProof;
    }

    /**
     * Getter for property cbmAddrProof.
     *
     * @return Value of property cbmAddrProof.
     */
    public com.see.truetransact.clientutil.ComboBoxModel getCbmAddrProof() {
        return cbmAddrProof;
    }

    /**
     * Setter for property cbmAddrProof.
     *
     * @param cbmAddrProof New value of property cbmAddrProof.
     */
    public void setCbmAddrProof(com.see.truetransact.clientutil.ComboBoxModel cbmAddrProof) {
        this.cbmAddrProof = cbmAddrProof;
    }

    /**
     * Getter for property cboIdenProof.
     *
     * @return Value of property cboIdenProof.
     */
    public java.lang.String getCboIdenProof() {
        return cboIdenProof;
    }

    /**
     * Setter for property cboIdenProof.
     *
     * @param cboIdenProof New value of property cboIdenProof.
     */
    public void setCboIdenProof(java.lang.String cboIdenProof) {
        this.cboIdenProof = cboIdenProof;
    }

    /**
     * Getter for property cbmIdenProof.
     *
     * @return Value of property cbmIdenProof.
     */
    public com.see.truetransact.clientutil.ComboBoxModel getCbmIdenProof() {
        return cbmIdenProof;
    }

    /**
     * Setter for property cbmIdenProof.
     *
     * @param cbmIdenProof New value of property cbmIdenProof.
     */
    public void setCbmIdenProof(com.see.truetransact.clientutil.ComboBoxModel cbmIdenProof) {
        this.cbmIdenProof = cbmIdenProof;
    }

    /**
     * Getter for property txtPassportFirstName.
     *
     * @return Value of property txtPassportFirstName.
     */
    public java.lang.String getTxtPassportFirstName() {
        return txtPassportFirstName;
    }

    /**
     * Setter for property txtPassportFirstName.
     *
     * @param txtPassportFirstName New value of property txtPassportFirstName.
     */
    public void setTxtPassportFirstName(java.lang.String txtPassportFirstName) {
        this.txtPassportFirstName = txtPassportFirstName;
    }

    /**
     * Getter for property txtPassportMiddleName.
     *
     * @return Value of property txtPassportMiddleName.
     */
    public java.lang.String getTxtPassportMiddleName() {
        return txtPassportMiddleName;
    }

    /**
     * Setter for property txtPassportMiddleName.
     *
     * @param txtPassportMiddleName New value of property txtPassportMiddleName.
     */
    public void setTxtPassportMiddleName(java.lang.String txtPassportMiddleName) {
        this.txtPassportMiddleName = txtPassportMiddleName;
    }

    /**
     * Getter for property txtPassportLastName.
     *
     * @return Value of property txtPassportLastName.
     */
    public java.lang.String getTxtPassportLastName() {
        return txtPassportLastName;
    }

    /**
     * Setter for property txtPassportLastName.
     *
     * @param txtPassportLastName New value of property txtPassportLastName.
     */
    public void setTxtPassportLastName(java.lang.String txtPassportLastName) {
        this.txtPassportLastName = txtPassportLastName;
    }

    /**
     * Getter for property tdtPassportIssueDt.
     *
     * @return Value of property tdtPassportIssueDt.
     */
    public java.lang.String getTdtPassportIssueDt() {
        return tdtPassportIssueDt;
    }

    /**
     * Setter for property tdtPassportIssueDt.
     *
     * @param tdtPassportIssueDt New value of property tdtPassportIssueDt.
     */
    public void setTdtPassportIssueDt(java.lang.String tdtPassportIssueDt) {
        this.tdtPassportIssueDt = tdtPassportIssueDt;
    }

    /**
     * Getter for property tdtPassportValidUpto.
     *
     * @return Value of property tdtPassportValidUpto.
     */
    public java.lang.String getTdtPassportValidUpto() {
        return tdtPassportValidUpto;
    }

    /**
     * Setter for property tdtPassportValidUpto.
     *
     * @param tdtPassportValidUpto New value of property tdtPassportValidUpto.
     */
    public void setTdtPassportValidUpto(java.lang.String tdtPassportValidUpto) {
        this.tdtPassportValidUpto = tdtPassportValidUpto;
    }

    /**
     * Getter for property txtPassportNo.
     *
     * @return Value of property txtPassportNo.
     */
    public java.lang.String getTxtPassportNo() {
        return txtPassportNo;
    }

    /**
     * Setter for property txtPassportNo.
     *
     * @param txtPassportNo New value of property txtPassportNo.
     */
    public void setTxtPassportNo(java.lang.String txtPassportNo) {
        this.txtPassportNo = txtPassportNo;
    }

    /**
     * Getter for property txtPassportIssueAuth.
     *
     * @return Value of property txtPassportIssueAuth.
     */
    public java.lang.String getTxtPassportIssueAuth() {
        return txtPassportIssueAuth;
    }

    /**
     * Setter for property txtPassportIssueAuth.
     *
     * @param txtPassportIssueAuth New value of property txtPassportIssueAuth.
     */
    public void setTxtPassportIssueAuth(java.lang.String txtPassportIssueAuth) {
        this.txtPassportIssueAuth = txtPassportIssueAuth;
    }

    /**
     * Getter for property cbmPassportTitle.
     *
     * @return Value of property cbmPassportTitle.
     */
    public com.see.truetransact.clientutil.ComboBoxModel getCbmPassportTitle() {
        return cbmPassportTitle;
    }

    /**
     * Setter for property cbmPassportTitle.
     *
     * @param cbmPassportTitle New value of property cbmPassportTitle.
     */
    public void setCbmPassportTitle(com.see.truetransact.clientutil.ComboBoxModel cbmPassportTitle) {
        this.cbmPassportTitle = cbmPassportTitle;
    }

    /**
     * Getter for property cboPassportTitle.
     *
     * @return Value of property cboPassportTitle.
     */
    public java.lang.String getCboPassportTitle() {
        return cboPassportTitle;
    }

    /**
     * Setter for property cboPassportTitle.
     *
     * @param cboPassportTitle New value of property cboPassportTitle.
     */
    public void setCboPassportTitle(java.lang.String cboPassportTitle) {
        this.cboPassportTitle = cboPassportTitle;
    }

    public com.see.truetransact.clientutil.ComboBoxModel getCbmcomboAmsam() {
        return cbmcomboAmsam;
    }

    public void setCbcomboAmsam(com.see.truetransact.clientutil.ComboBoxModel cbmcomboAmsam) {
        this.cbmcomboAmsam = cbmcomboAmsam;
    }

    /**
     * Getter for property cbmPassportIssuePlace.
     *
     * @return Value of property cbmPassportIssuePlace.
     */
    public com.see.truetransact.clientutil.ComboBoxModel getCbmPassportIssuePlace() {
        return cbmPassportIssuePlace;
    }

    /**
     * Setter for property cbmPassportIssuePlace.
     *
     * @param cbmPassportIssuePlace New value of property cbmPassportIssuePlace.
     */
    public void setCbmPassportIssuePlace(com.see.truetransact.clientutil.ComboBoxModel cbmPassportIssuePlace) {
        this.cbmPassportIssuePlace = cbmPassportIssuePlace;
    }

    /**
     * Getter for property cboPassportIssuePlace.
     *
     * @return Value of property cboPassportIssuePlace.
     */
    public java.lang.String getCboPassportIssuePlace() {
        return cboPassportIssuePlace;
    }

    /**
     * Setter for property cboPassportIssuePlace.
     *
     * @param cboPassportIssuePlace New value of property cboPassportIssuePlace.
     */
    public void setCboPassportIssuePlace(java.lang.String cboPassportIssuePlace) {
        this.cboPassportIssuePlace = cboPassportIssuePlace;
    }

    /**
     * Getter for property isPassport.
     *
     * @return Value of property isPassport.
     */
    public boolean isIsPassport() {
        return isPassport;
    }

    /**
     * Setter for property isPassport.
     *
     * @param isPassport New value of property isPassport.
     */
    public void setIsPassport(boolean isPassport) {
        this.isPassport = isPassport;
    }

    /**
     * Getter for property cbmFarClass.
     *
     * @return Value of property cbmFarClass.
     */
    public com.see.truetransact.clientutil.ComboBoxModel getCbmFarClass() {
        return cbmFarClass;
    }

    /**
     * Setter for property cbmFarClass.
     *
     * @param cbmFarClass New value of property cbmFarClass.
     */
    public void setCbmFarClass(com.see.truetransact.clientutil.ComboBoxModel cbmFarClass) {
        this.cbmFarClass = cbmFarClass;
    }

    /**
     * Getter for property cboFarClass.
     *
     * @return Value of property cboFarClass.
     */
    public java.lang.String getCboFarClass() {
        return cboFarClass;
    }

    /**
     * Setter for property cboFarClass.
     *
     * @param cboFarClass New value of property cboFarClass.
     */
    public void setCboFarClass(java.lang.String cboFarClass) {
        this.cboFarClass = cboFarClass;
    }

    /**
     * Getter for property txtKartha.
     *
     * @return Value of property txtKartha.
     */
    public java.lang.String getTxtKartha() {
        return txtKartha;
    }

    /**
     * Setter for property txtKartha.
     *
     * @param txtKartha New value of property txtKartha.
     */
    public void setTxtKartha(java.lang.String txtKartha) {
        this.txtKartha = txtKartha;
    }

    /**
     * Getter for property txtAge.
     *
     * @return Value of property txtAge.
     */
    public java.lang.String getTxtAge() {
        return txtAge;
    }

    /**
     * Setter for property txtAge.
     *
     * @param txtAge New value of property txtAge.
     */
    public void setTxtAge(java.lang.String txtAge) {
        this.txtAge = txtAge;
    }

    /**
     * Getter for property txtIncName.
     *
     * @return Value of property txtIncName.
     */
    public java.lang.String getTxtIncName() {
        return txtIncName;
    }

    /**
     * Setter for property txtIncName.
     *
     * @param txtIncName New value of property txtIncName.
     */
    public void setTxtIncName(java.lang.String txtIncName) {
        this.txtIncName = txtIncName;
    }

    /**
     * Getter for property txtIncAmount.
     *
     * @return Value of property txtIncAmount.
     */
    public java.lang.String getTxtIncAmount() {
        return txtIncAmount;
    }

    /**
     * Setter for property txtIncAmount.
     *
     * @param txtIncAmount New value of property txtIncAmount.
     */
    public void setTxtIncAmount(java.lang.String txtIncAmount) {
        this.txtIncAmount = txtIncAmount;
    }

    /**
     * Getter for property cbmIncPack.
     *
     * @return Value of property cbmIncPack.
     */
    public com.see.truetransact.clientutil.ComboBoxModel getCbmIncPack() {
        return cbmIncPack;
    }

    /**
     * Setter for property cbmIncPack.
     *
     * @param cbmIncPack New value of property cbmIncPack.
     */
    public void setCbmIncPack(com.see.truetransact.clientutil.ComboBoxModel cbmIncPack) {
        this.cbmIncPack = cbmIncPack;
    }

    /**
     * Getter for property cboIncPack.
     *
     * @return Value of property cboIncPack.
     */
    public java.lang.String getCboIncPack() {
        return cboIncPack;
    }

    /**
     * Setter for property cboIncPack.
     *
     * @param cboIncPack New value of property cboIncPack.
     */
    public void setCboIncPack(java.lang.String cboIncPack) {
        this.cboIncPack = cboIncPack;
    }

    /**
     * Getter for property cbmIncRelation.
     *
     * @return Value of property cbmIncRelation.
     */
    public com.see.truetransact.clientutil.ComboBoxModel getCbmIncRelation() {
        return cbmIncRelation;
    }

    /**
     * Setter for property cbmIncRelation.
     *
     * @param cbmIncRelation New value of property cbmIncRelation.
     */
    public void setCbmIncRelation(com.see.truetransact.clientutil.ComboBoxModel cbmIncRelation) {
        this.cbmIncRelation = cbmIncRelation;
    }

    /**
     * Getter for property cboIncRelation.
     *
     * @return Value of property cboIncRelation.
     */
    public java.lang.String getCboIncRelation() {
        return cboIncRelation;
    }

    /**
     * Setter for property cboIncRelation.
     *
     * @param cboIncRelation New value of property cboIncRelation.
     */
    public void setCboIncRelation(java.lang.String cboIncRelation) {
        this.cboIncRelation = cboIncRelation;
    }

    /**
     * Getter for property tblIncomeParticulars.
     *
     * @return Value of property tblIncomeParticulars.
     */
    public com.see.truetransact.clientutil.EnhancedTableModel getTblIncomeParticulars() {
        return tblIncomeParticulars;
    }

    /**
     * Setter for property tblIncomeParticulars.
     *
     * @param tblIncomeParticulars New value of property tblIncomeParticulars.
     */
    public void setTblIncomeParticulars(com.see.truetransact.clientutil.EnhancedTableModel tblIncomeParticulars) {
        this.tblIncomeParticulars = tblIncomeParticulars;
    }

    /**
     * Getter for property newIncomeDet.
     *
     * @return Value of property newIncomeDet.
     */
    public boolean isNewIncomeDet() {
        return newIncomeDet;
    }

    /**
     * Setter for property newIncomeDet.
     *
     * @param newIncomeDet New value of property newIncomeDet.
     */
    public void setNewIncomeDet(boolean newIncomeDet) {
        this.newIncomeDet = newIncomeDet;
    }

    /**
     * Getter for property txtSlNo.
     *
     * @return Value of property txtSlNo.
     */
    public java.lang.String getTxtSlNo() {
        return txtSlNo;
    }

    /**
     * Setter for property txtSlNo.
     *
     * @param txtSlNo New value of property txtSlNo.
     */
    public void setTxtSlNo(java.lang.String txtSlNo) {
        this.txtSlNo = txtSlNo;
    }

    /**
     * Getter for property isIncomePar.
     *
     * @return Value of property isIncomePar.
     */
    public boolean isIsIncomePar() {
        return isIncomePar;
    }

    /**
     * Setter for property isIncomePar.
     *
     * @param isIncomePar New value of property isIncomePar.
     */
    public void setIsIncomePar(boolean isIncomePar) {
        this.isIncomePar = isIncomePar;
    }

    /**
     * Getter for property chkAddrVerified.
     *
     * @return Value of property chkAddrVerified.
     */
    public boolean isChkAddrVerified() {
        return chkAddrVerified;
    }

    /**
     * Getter for property chkIncomeDetails.
     *
     * @return Value of property chkIncomeDetails.
     */
    public boolean isChkIncomeDetails() {
        return chkIncomeDetails;
    }

    /**
     * Setter for property chkIncomeDetails.
     *
     * @param chkIncomeDetails New value of property chkIncomeDetails.
     */
    public void setChkIncomeDetails(boolean chkIncomeDetails) {
        this.chkIncomeDetails = chkIncomeDetails;
    }

    /**
     * Getter for property tblCustomerLandDetails.
     *
     * @return Value of property tblCustomerLandDetails.
     */
    public com.see.truetransact.clientutil.EnhancedTableModel getTblCustomerLandDetails() {
        return tblCustomerLandDetails;
    }

    /**
     * Setter for property tblCustomerLandDetails.
     *
     * @param tblCustomerLandDetails New value of property
     * tblCustomerLandDetails.
     */
    public void setTblCustomerLandDetails(com.see.truetransact.clientutil.EnhancedTableModel tblCustomerLandDetails) {
        this.tblCustomerLandDetails = tblCustomerLandDetails;
    }

    /**
     * Getter for property txtLocation.
     *
     * @return Value of property txtLocation.
     */
    public java.lang.String getTxtLocation() {
        return txtLocation;
    }

    /**
     * Setter for property txtLocation.
     *
     * @param txtLocation New value of property txtLocation.
     */
    public void setTxtLocation(java.lang.String txtLocation) {
        this.txtLocation = txtLocation;
    }

    /**
     * Getter for property txtLandDetArea.
     *
     * @return Value of property txtLandDetArea.
     */
    public java.lang.String getTxtLandDetArea() {
        return txtLandDetArea;
    }

    /**
     * Setter for property txtLandDetArea.
     *
     * @param txtLandDetArea New value of property txtLandDetArea.
     */
    public void setTxtLandDetArea(java.lang.String txtLandDetArea) {
        this.txtLandDetArea = txtLandDetArea;
    }

    /**
     * Getter for property txtSurNo.
     *
     * @return Value of property txtSurNo.
     */
    public java.lang.String getTxtSurNo() {
        return txtSurNo;
    }

    /**
     * Setter for property txtSurNo.
     *
     * @param txtSurNo New value of property txtSurNo.
     */
    public void setTxtSurNo(java.lang.String txtSurNo) {
        this.txtSurNo = txtSurNo;
    }

    /**
     * Getter for property txtVillage.
     *
     * @return Value of property txtVillage.
     */
    public java.lang.String getTxtVillage() {
        return txtVillage;
    }

    /**
     * Setter for property txtVillage.
     *
     * @param txtVillage New value of property txtVillage.
     */
    public void setTxtVillage(java.lang.String txtVillage) {
        this.txtVillage = txtVillage;
    }

    /**
     * Getter for property txtHobli.
     *
     * @return Value of property txtHobli.
     */
    public java.lang.String getTxtHobli() {
        return txtHobli;
    }

    /**
     * Setter for property txtHobli.
     *
     * @param txtHobli New value of property txtHobli.
     */
    public void setTxtHobli(java.lang.String txtHobli) {
        this.txtHobli = txtHobli;
    }

    /**
     * Getter for property txtLandDetPin.
     *
     * @return Value of property txtLandDetPin.
     */
    public java.lang.String getTxtLandDetPin() {
        return txtLandDetPin;
    }

    /**
     * Setter for property txtLandDetPin.
     *
     * @param txtLandDetPin New value of property txtLandDetPin.
     */
    public void setTxtLandDetPin(java.lang.String txtLandDetPin) {
        this.txtLandDetPin = txtLandDetPin;
    }

    /**
     * Getter for property txtPost.
     *
     * @return Value of property txtPost.
     */
    public java.lang.String getTxtPost() {
        return txtPost;
    }

    /**
     * Setter for property txtPost.
     *
     * @param txtPost New value of property txtPost.
     */
    public void setTxtPost(java.lang.String txtPost) {
        this.txtPost = txtPost;
    }

    /**
     * Getter for property cbmIrrigated.
     *
     * @return Value of property cbmIrrigated.
     */
    public com.see.truetransact.clientutil.ComboBoxModel getCbmIrrigated() {
        return cbmIrrigated;
    }

    /**
     * Setter for property cbmIrrigated.
     *
     * @param cbmIrrigated New value of property cbmIrrigated.
     */
    public void setCbmIrrigated(com.see.truetransact.clientutil.ComboBoxModel cbmIrrigated) {
        this.cbmIrrigated = cbmIrrigated;
    }

    /**
     * Getter for property cboIrrigated.
     *
     * @return Value of property cboIrrigated.
     */
    public java.lang.String getCboIrrigated() {
        return cboIrrigated;
    }

    /**
     * Setter for property cboIrrigated.
     *
     * @param cboIrrigated New value of property cboIrrigated.
     */
    public void setCboIrrigated(java.lang.String cboIrrigated) {
        this.cboIrrigated = cboIrrigated;
    }

    /**
     * Getter for property cbmSourceIrrigated.
     *
     * @return Value of property cbmSourceIrrigated.
     */
    public com.see.truetransact.clientutil.ComboBoxModel getCbmSourceIrrigated() {
        return cbmSourceIrrigated;
    }

    /**
     * Setter for property cbmSourceIrrigated.
     *
     * @param cbmSourceIrrigated New value of property cbmSourceIrrigated.
     */
    public void setCbmSourceIrrigated(com.see.truetransact.clientutil.ComboBoxModel cbmSourceIrrigated) {
        this.cbmSourceIrrigated = cbmSourceIrrigated;
    }

    /**
     * Getter for property cboSourceIrrigated.
     *
     * @return Value of property cboSourceIrrigated.
     */
    public java.lang.String getCboSourceIrrigated() {
        return cboSourceIrrigated;
    }

    /**
     * Setter for property cboSourceIrrigated.
     *
     * @param cboSourceIrrigated New value of property cboSourceIrrigated.
     */
    public void setCboSourceIrrigated(java.lang.String cboSourceIrrigated) {
        this.cboSourceIrrigated = cboSourceIrrigated;
    }

    /**
     * Getter for property cbmTaluk.
     *
     * @return Value of property cbmTaluk.
     */
//    public com.see.truetransact.clientutil.ComboBoxModel getCbmTaluk() {
//        return cbmTaluk;
//    }
    /**
     * Setter for property cbmTaluk.
     *
     * @param cbmTaluk New value of property cbmTaluk.
     */
//    public void setCbmTaluk(com.see.truetransact.clientutil.ComboBoxModel cbmTaluk) {
//        this.cbmTaluk = cbmTaluk;
//    }
//    
//    /**
//    * Getter for property cboTaluk.
//     * @return Value of property cboTaluk.
//     */
    public java.lang.String getCboTaluk() {
        return cboTaluk;
    }

    /**
     * Setter for property cboTaluk.
     *
     * @param cboTaluk New value of property cboTaluk.
     */
    public void setCboTaluk(java.lang.String cboTaluk) {
        this.cboTaluk = cboTaluk;
    }

    /**
     * Getter for property cbmDistrict.
     *
     * @return Value of property cbmDistrict.
     */
//    public com.see.truetransact.clientutil.ComboBoxModel getCbmDistrict() {
//        return cbmDistrict;
//    }
//    
//    /**
//     * Setter for property cbmDistrict.
//     * @param cbmDistrict New value of property cbmDistrict.
//     */
//    public void setCbmDistrict(com.see.truetransact.clientutil.ComboBoxModel cbmDistrict) {
//        this.cbmDistrict = cbmDistrict;
//    }
    /**
     * Getter for property cboDistrict.
     *
     * @return Value of property cboDistrict.
     */
    public java.lang.String getCboDistrict() {
        return cboDistrict;
    }

    /**
     * Setter for property cboDistrict.
     *
     * @param cboDistrict New value of property cboDistrict.
     */
    public void setCboDistrict(java.lang.String cboDistrict) {
        this.cboDistrict = cboDistrict;
    }

    /**
     * Getter for property cbmLandDetState.
     *
     * @return Value of property cbmLandDetState.
     */
    public com.see.truetransact.clientutil.ComboBoxModel getCbmLandDetState() {
        return cbmLandDetState;
    }

    /**
     * Setter for property cbmLandDetState.
     *
     * @param cbmLandDetState New value of property cbmLandDetState.
     */
    public void setCbmLandDetState(com.see.truetransact.clientutil.ComboBoxModel cbmLandDetState) {
        this.cbmLandDetState = cbmLandDetState;
    }

    /**
     * Getter for property cboLandDetState.
     *
     * @return Value of property cboLandDetState.
     */
    public java.lang.String getCboLandDetState() {
        return cboLandDetState;
    }

    /**
     * Setter for property cboLandDetState.
     *
     * @param cboLandDetState New value of property cboLandDetState.
     */
    public void setCboLandDetState(java.lang.String cboLandDetState) {
        this.cboLandDetState = cboLandDetState;
    }

    /**
     * Getter for property txtLandDetSlNo.
     *
     * @return Value of property txtLandDetSlNo.
     */
    public java.lang.String getTxtLandDetSlNo() {
        return txtLandDetSlNo;
    }

    /**
     * Setter for property txtLandDetSlNo.
     *
     * @param txtLandDetSlNo New value of property txtLandDetSlNo.
     */
    public void setTxtLandDetSlNo(java.lang.String txtLandDetSlNo) {
        this.txtLandDetSlNo = txtLandDetSlNo;
    }

    /**
     * Getter for property newLandDet.
     *
     * @return Value of property newLandDet.
     */
    public boolean isNewLandDet() {
        return newLandDet;
    }

    /**
     * Setter for property newLandDet.
     *
     * @param newLandDet New value of property newLandDet.
     */
    public void setNewLandDet(boolean newLandDet) {
        this.newLandDet = newLandDet;
    }

    /**
     * Getter for property isLandDetails.
     *
     * @return Value of property isLandDetails.
     */
    public boolean isIsLandDetails() {
        return isLandDetails;
    }

    /**
     * Setter for property isLandDetails.
     *
     * @param isLandDetails New value of property isLandDetails.
     */
    public void setIsLandDetails(boolean isLandDetails) {
        this.isLandDetails = isLandDetails;
    }

    /**
     * Getter for property chkLandDetails.
     *
     * @return Value of property chkLandDetails.
     */
    public boolean isChkLandDetails() {
        return chkLandDetails;
    }

    /**
     * Setter for property chkLandDetails.
     *
     * @param chkLandDetails New value of property chkLandDetails.
     */
    public void setChkLandDetails(boolean chkLandDetails) {
        this.chkLandDetails = chkLandDetails;
    }

    /**
     * Getter for property lblDealingPeriod.
     *
     * @return Value of property lblDealingPeriod.
     */
    public java.lang.String getLblDealingPeriod() {
        return lblDealingPeriod;
    }

    /**
     * Setter for property lblDealingPeriod.
     *
     * @param lblDealingPeriod New value of property lblDealingPeriod.
     */
    public void setLblDealingPeriod(java.lang.String lblDealingPeriod) {
        this.lblDealingPeriod = lblDealingPeriod;
    }

    /**
     * Getter for property tdtGuardianDOB.
     *
     * @return Value of property tdtGuardianDOB.
     */
    public java.lang.String getTdtGuardianDOB() {
        return tdtGuardianDOB;
    }

    /**
     * Setter for property tdtGuardianDOB.
     *
     * @param tdtGuardianDOB New value of property tdtGuardianDOB.
     */
    public void setTdtGuardianDOB(java.lang.String tdtGuardianDOB) {
        this.tdtGuardianDOB = tdtGuardianDOB;
    }

    /**
     * Getter for property txtGuardianAge.
     *
     * @return Value of property txtGuardianAge.
     */
    public java.lang.String getTxtGuardianAge() {
        return txtGuardianAge;
    }

    /**
     * Setter for property txtGuardianAge.
     *
     * @param txtGuardianAge New value of property txtGuardianAge.
     */
    public void setTxtGuardianAge(java.lang.String txtGuardianAge) {
        this.txtGuardianAge = txtGuardianAge;
    }

    /**
     * Getter for property tdtFinacialYrEnd.
     *
     * @return Value of property tdtFinacialYrEnd.
     */
    public java.lang.String getTdtFinacialYrEnd() {
        return tdtFinacialYrEnd;
    }

    /**
     * Setter for property tdtFinacialYrEnd.
     *
     * @param tdtFinacialYrEnd New value of property tdtFinacialYrEnd.
     */
    public void setTdtFinacialYrEnd(java.lang.String tdtFinacialYrEnd) {
        this.tdtFinacialYrEnd = tdtFinacialYrEnd;
    }

    /**
     * Getter for property txtTotalIncome.
     *
     * @return Value of property txtTotalIncome.
     */
    public java.lang.String getTxtTotalIncome() {
        return txtTotalIncome;
    }

    /**
     * Setter for property txtTotalIncome.
     *
     * @param txtTotalIncome New value of property txtTotalIncome.
     */
    public void setTxtTotalIncome(java.lang.String txtTotalIncome) {
        this.txtTotalIncome = txtTotalIncome;
    }

    /**
     * Getter for property txtprofitBefTax.
     *
     * @return Value of property txtprofitBefTax.
     */
    public java.lang.String getTxtprofitBefTax() {
        return txtprofitBefTax;
    }

    /**
     * Setter for property txtprofitBefTax.
     *
     * @param txtprofitBefTax New value of property txtprofitBefTax.
     */
    public void setTxtprofitBefTax(java.lang.String txtprofitBefTax) {
        this.txtprofitBefTax = txtprofitBefTax;
    }

    /**
     * Getter for property txtTotalNonTaxExp.
     *
     * @return Value of property txtTotalNonTaxExp.
     */
    public java.lang.String getTxtTotalNonTaxExp() {
        return txtTotalNonTaxExp;
    }

    /**
     * Setter for property txtTotalNonTaxExp.
     *
     * @param txtTotalNonTaxExp New value of property txtTotalNonTaxExp.
     */
    public void setTxtTotalNonTaxExp(java.lang.String txtTotalNonTaxExp) {
        this.txtTotalNonTaxExp = txtTotalNonTaxExp;
    }

    /**
     * Getter for property txtTaxliability.
     *
     * @return Value of property txtTaxliability.
     */
    public java.lang.String getTxtTaxliability() {
        return txtTaxliability;
    }

    /**
     * Setter for property txtTaxliability.
     *
     * @param txtTaxliability New value of property txtTaxliability.
     */
    public void setTxtTaxliability(java.lang.String txtTaxliability) {
        this.txtTaxliability = txtTaxliability;
    }

    /**
     * Getter for property suspendedDate.
     *
     * @return Value of property suspendedDate.
     */
    public java.lang.String getSuspendedDate() {
        return suspendedDate;
    }

    /**
     * Setter for property suspendedDate.
     *
     * @param suspendedDate New value of property suspendedDate.
     */
    public void setSuspendedDate(java.lang.String suspendedDate) {
        this.suspendedDate = suspendedDate;
    }

    /**
     * Getter for property revokedDate.
     *
     * @return Value of property revokedDate.
     */
    public java.lang.String getRevokedDate() {
        return revokedDate;
    }

    /**
     * Setter for property revokedDate.
     *
     * @param revokedDate New value of property revokedDate.
     */
    public void setRevokedDate(java.lang.String revokedDate) {
        this.revokedDate = revokedDate;
    }

    /**
     * Getter for property susRevRemarks.
     *
     * @return Value of property susRevRemarks.
     */
    public java.lang.String getSusRevRemarks() {
        return susRevRemarks;
    }

    /**
     * Setter for property susRevRemarks.
     *
     * @param susRevRemarks New value of property susRevRemarks.
     */
    public void setSusRevRemarks(java.lang.String susRevRemarks) {
        this.susRevRemarks = susRevRemarks;
    }

    /**
     * Getter for property chksuspendedBy.
     *
     * @return Value of property chksuspendedBy.
     */
    public boolean isChksuspendedBy() {
        return chksuspendedBy;
    }

    /**
     * Setter for property chksuspendedBy.
     *
     * @param chksuspendedBy New value of property chksuspendedBy.
     */
    public void setChksuspendedBy(boolean chksuspendedBy) {
        this.chksuspendedBy = chksuspendedBy;
    }

    /**
     * Getter for property chkrevokedBy.
     *
     * @return Value of property chkrevokedBy.
     */
    public boolean isChkrevokedBy() {
        return chkrevokedBy;
    }

    /**
     * Setter for property chkrevokedBy.
     *
     * @param chkrevokedBy New value of property chkrevokedBy.
     */
    public void setChkrevokedBy(boolean chkrevokedBy) {
        this.chkrevokedBy = chkrevokedBy;
    }

    /**
     * Getter for property isCustSuspended.
     *
     * @return Value of property isCustSuspended.
     */
    public boolean isIsCustSuspended() {
        return isCustSuspended;
    }

    /**
     * Setter for property isCustSuspended.
     *
     * @param isCustSuspended New value of property isCustSuspended.
     */
    public void setIsCustSuspended(boolean isCustSuspended) {
        this.isCustSuspended = isCustSuspended;
    }

    /**
     * Getter for property bankruptcy.
     *
     * @return Value of property bankruptcy.
     */
    public java.lang.String getBankruptcy() {
        return bankruptcy;
    }

    /**
     * Setter for property bankruptcy.
     *
     * @param bankruptcy New value of property bankruptcy.
     */
    public void setBankruptcy(java.lang.String bankruptcy) {
        this.bankruptcy = bankruptcy;
    }

    /**
     * Getter for property memberShipNo.
     *
     * @return Value of property memberShipNo.
     */
    public java.lang.String getMemberShipNo() {
        return memberShipNo;
    }

    /**
     * Setter for property memberShipNo.
     *
     * @param memberShipNo New value of property memberShipNo.
     */
    public void setMemberShipNo(java.lang.String memberShipNo) {
        this.memberShipNo = memberShipNo;
    }

    /**
     * Getter for property rdoITDec_pan.
     *
     * @return Value of property rdoITDec_pan.
     */
    public boolean isRdoITDec_pan() {
        return rdoITDec_pan;
    }

    /**
     * Setter for property rdoITDec_pan.
     *
     * @param rdoITDec_pan New value of property rdoITDec_pan.
     */
    public void setRdoITDec_pan(boolean rdoITDec_pan) {
        this.rdoITDec_pan = rdoITDec_pan;
    }

    /**
     * Getter for property rdoITDec_F60.
     *
     * @return Value of property rdoITDec_F60.
     */
    public boolean isRdoITDec_F60() {
        return rdoITDec_F60;
    }

    /**
     * Setter for property rdoITDec_F60.
     *
     * @param rdoITDec_F60 New value of property rdoITDec_F60.
     */
    public void setRdoITDec_F60(boolean rdoITDec_F60) {
        this.rdoITDec_F60 = rdoITDec_F60;
    }

    /**
     * Getter for property rdoITDec_F61.
     *
     * @return Value of property rdoITDec_F61.
     */
    public boolean isRdoITDec_F61() {
        return rdoITDec_F61;
    }

    /**
     * Setter for property rdoITDec_F61.
     *
     * @param rdoITDec_F61 New value of property rdoITDec_F61.
     */
    public void setRdoITDec_F61(boolean rdoITDec_F61) {
        this.rdoITDec_F61 = rdoITDec_F61;
    }

    /**
     * Getter for property cbmIncProfession.
     *
     * @return Value of property cbmIncProfession.
     */
    public com.see.truetransact.clientutil.ComboBoxModel getCbmIncProfession() {
        return cbmIncProfession;
    }

    /**
     * Setter for property cbmIncProfession.
     *
     * @param cbmIncProfession New value of property cbmIncProfession.
     */
    public void setCbmIncProfession(com.see.truetransact.clientutil.ComboBoxModel cbmIncProfession) {
        this.cbmIncProfession = cbmIncProfession;
    }

    /**
     * Getter for property cboIncProfession.
     *
     * @return Value of property cboIncProfession.
     */
    public java.lang.String getCboIncProfession() {
        return cboIncProfession;
    }

    /**
     * Setter for property cboIncProfession.
     *
     * @param cboIncProfession New value of property cboIncProfession.
     */
    public void setCboIncProfession(java.lang.String cboIncProfession) {
        this.cboIncProfession = cboIncProfession;
    }

    /**
     * Getter for property incCompany.
     *
     * @return Value of property incCompany.
     */
    public java.lang.String getIncCompany() {
        return incCompany;
    }

    /**
     * Setter for property incCompany.
     *
     * @param incCompany New value of property incCompany.
     */
    public void setIncCompany(java.lang.String incCompany) {
        this.incCompany = incCompany;
    }

    /**
     * Getter for property objCustomerRB.
     *
     * @return Value of property objCustomerRB.
     */
    public java.util.ResourceBundle getObjCustomerRB() {
        return objCustomerRB;
    }

    /**
     * Setter for property objCustomerRB.
     *
     * @param objCustomerRB New value of property objCustomerRB.
     */
    public void setObjCustomerRB(java.util.ResourceBundle objCustomerRB) {
        this.objCustomerRB = objCustomerRB;
    }

    /**
     * Getter for property cbmReligion.
     *
     * @return Value of property cbmReligion.
     */
    public com.see.truetransact.clientutil.ComboBoxModel getCbmReligion() {
        return cbmReligion;
    }

    /**
     * Setter for property cbmReligion.
     *
     * @param cbmReligion New value of property cbmReligion.
     */
    public void setCbmReligion(com.see.truetransact.clientutil.ComboBoxModel cbmReligion) {
        this.cbmReligion = cbmReligion;
    }

    /**
     * Getter for property cboReligion.
     *
     * @return Value of property cboReligion.
     */
    public java.lang.String getCboReligion() {
        return cboReligion;
    }

    /**
     * Setter for property cboReligion.
     *
     * @param cboReligion New value of property cboReligion.
     */
    public void setCboReligion(java.lang.String cboReligion) {
        this.cboReligion = cboReligion;
    }

    /**
     * Getter for property txtWardNo.
     *
     * @return Value of property txtWardNo.
     */
    public java.lang.String getTxtWardNo() {
        return txtWardNo;
    }

    /**
     * Setter for property txtWardNo.
     *
     * @param txtWardNo New value of property txtWardNo.
     */
    public void setTxtWardNo(java.lang.String txtWardNo) {
        this.txtWardNo = txtWardNo;
    }

    public String getMobileAppLoginStatus() {
        return mobileAppLoginStatus;
    }

    public void setMobileAppLoginStatus(String mobileAppLoginStatus) {
        this.mobileAppLoginStatus = mobileAppLoginStatus;
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
     * Getter for property tdtjoiningDate.
     *
     * @return Value of property tdtjoiningDate.
     */
    public String getTdtjoiningDate() {
        return tdtjoiningDate;
    }

    /**
     * Setter for property tdtjoiningDate.
     *
     * @param tdtjoiningDate New value of property tdtjoiningDate.
     */
    public void setTdtjoiningDate(String tdtjoiningDate) {
        this.tdtjoiningDate = tdtjoiningDate;
    }

    public ComboBoxModel getCbmCustKara() {
        return cbmCustKara;
    }

    public void setCbmCustKara(ComboBoxModel cbmCustKara) {
        this.cbmCustKara = cbmCustKara;
    }

    public ComboBoxModel getCbmCustTaluk() {
        return cbmCustTaluk;
    }

    public void setCbmCustTaluk(ComboBoxModel cbmCustTaluk) {
        this.cbmCustTaluk = cbmCustTaluk;
    }

    public ComboBoxModel getCbmCustVillage() {
        return cbmCustVillage;
    }

    public void setCbmCustVillage(ComboBoxModel cbmCustVillage) {
        this.cbmCustVillage = cbmCustVillage;
    }

    public String getCboCustKara() {
        return cboCustKara;
    }

    public void setCboCustKara(String cboCustKara) {
        this.cboCustKara = cboCustKara;
    }

    public String getCboCustTaluk() {
        return cboCustTaluk;
    }

    public void setCboCustTaluk(String cboCustTaluk) {
        this.cboCustTaluk = cboCustTaluk;
    }

    public String getCboCustVillage() {
        return cboCustVillage;
    }

    public void setCboCustVillage(String cboCustVillage) {
        this.cboCustVillage = cboCustVillage;
    }

    // Added by nithya
    public ComboBoxModel getCbmWardName() {
        return cbmWardName;
    }

    public String getCboWardName() {
        return cboWardName;
    }

    public void setCbmWardName(ComboBoxModel cbmWardName) {
        this.cbmWardName = cbmWardName;
    }

    public void setCboWardName(String cboWardName) {
        this.cboWardName = cboWardName;
    }
    // End    
    
    
    private void getMap(List list) throws Exception {
        key = new ArrayList();
        value = new ArrayList();
        key.add("");
        value.add("");
        for (int i = 0, j = list.size(); i < j; i++) {
            key.add(((HashMap) list.get(i)).get("KEY"));
            value.add(((HashMap) list.get(i)).get("VALUE"));
        }
        System.out.println("####Key : " + key);
        System.out.println("####value : " + value);

    }

    public byte[] getProofPhotoByteArray() {
        return proofPhotoByteArray;
    }

    public void setProofPhotoByteArray(byte[] proofPhotoByteArray) {
        this.proofPhotoByteArray = proofPhotoByteArray;
    }

    public String getProofPhotoFile() {
        return proofPhotoFile;
    }

    public void setProofPhotoFile(String proofPhotoFile) {
        this.proofPhotoFile = proofPhotoFile;
    }    
    
}
//
