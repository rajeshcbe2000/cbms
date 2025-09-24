/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition.
 **
 *
 * EmployeeOB.java
 *
 * Created on August 14, 2003, 10:30 AM
 */

package com.see.truetransact.ui.employee;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Iterator;
import com.see.truetransact.transferobject.deposit.JointAccntTO;
import com.see.truetransact.uicomponent.CObservable;
import com.see.truetransact.clientproxy.*;
import com.see.truetransact.clientutil.ClientConstants;
import com.see.truetransact.commonutil.CommonConstants;
import com.see.truetransact.commonutil.DateUtil;
import com.see.truetransact.commonutil.CommonUtil;
import com.see.truetransact.clientutil.ClientUtil;
import com.see.truetransact.clientutil.TableUtil;
import com.see.truetransact.clientutil.ComboBoxModel;
import com.see.truetransact.clientutil.TableModel;
import com.see.truetransact.clientutil.EnhancedTableModel;
import com.see.truetransact.clientexception.ClientParseException;
import com.see.truetransact.ui.common.introducer.*;
import com.see.truetransact.ui.TrueTransactMain;
import com.see.truetransact.ui.deposit.CommonRB;
import com.see.truetransact.uicomponent.COptionPane;
import com.see.truetransact.transferobject.employee.EmployeeMasterTO;
import com.see.truetransact.transferobject.employee.EmployeeMasterPassPortTO;
import com.see.truetransact.transferobject.employee.EmployeeMasterAddressTO;
import com.see.truetransact.transferobject.employee.EmployeeMasterEducationTO;
import com.see.truetransact.transferobject.employee.EmployeeMasterTechnicalTO;
import com.see.truetransact.transferobject.employee.EmployeeMasterLanguageTO;
import com.see.truetransact.transferobject.employee.EmployeeMasterDependendTO;
import com.see.truetransact.transferobject.employee.EmployeeMasterPhoneTO;
import com.see.truetransact.transferobject.employee.EmployeeRelativeWorkingTO;
import com.see.truetransact.transferobject.employee.EmployeeRelativeDirectorTO;
import com.see.truetransact.transferobject.employee.EmployeePresentDetailsTO;
import com.see.truetransact.transferobject.employee.EmployeeTermLoanTO;
import com.see.truetransact.transferobject.employee.EmployeeOprativeTO;
import com.see.truetransact.transferobject.common.PromotionTO;
//import java.io.File;
import com.see.truetransact.clientutil.JointAcctHolderManipulation;
import java.util.Date;

import org.apache.log4j.Logger;

import java.io.OutputStream;
import java.io.InputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.sql.*;
import oracle.sql.*;
import oracle.jdbc.driver.*;
import com.see.truetransact.commonutil.Dummy;
import java.util.Set;

/**
 *
 * @author  administrator
 * Modified by Karthik
 * Modified by Annamalai
 * @modified by JK
 */
public class EmployeeMasterOB extends CObservable{
    private EmployeeMasterTO objEmployeeMasterTO=new EmployeeMasterTO();
    private EmployeeMasterPassPortTO objEmployeeMasterPassPortTO;
    private EmployeePresentDetailsTO objEmployeePresentDetailsTO ;
    CommonRB objCommRB = new CommonRB();
    private static EmployeeMasterOB objEmployeeMasterOB; // singleton object
    private final EmployeeMasterRB objEmployeeMasterRB = new EmployeeMasterRB();
    private final static Logger log = Logger.getLogger(EmployeeMasterOB.class);
    private final static ClientParseException parseException = ClientParseException.getInstance();
    HashMap map= new HashMap();
    private Date curDate = null;
    private ProxyFactory proxy;
    private int screenCustType;
    HashMap lookupMap = new HashMap();
    HashMap param = new HashMap();
    HashMap data = new HashMap();
    private ArrayList key;
    private ArrayList value;
    private HashMap keyValue = new HashMap();
    private int actionType=0;
    private int result=0;
    private String lblStatus="";
    private final String PRIMARY = "Primary";
    private final String EMPTY_STRING = "";
    private final int ADDRTYPE_COLNO = 0;
    private final int INCPAR_COLNO = 0;
    private final int STATUS_COLNO = 1;
    public boolean addressTypeExists = false;
    public boolean eductionTypeExists = false;
    public boolean promotionExists = false;
    public boolean technicalTypeExists = false;
    public boolean loanTypeExists=false;
    private boolean newAddress = false;
    private boolean loans = false;
    private boolean newEducation = false;
    private boolean newDependent = false;
    private boolean newTechnical = false;
    private boolean newLoans = false;
    private boolean newRelativesWorking = false;
    private boolean newDirector=false;
    private boolean newLanguage=false;
    private boolean newOperative=false;
    private boolean newPromotion=false;
    
    private boolean educationExists = false;
    private LinkedHashMap educationMap=new LinkedHashMap();
    private LinkedHashMap promotionMap= new LinkedHashMap();
    private HashMap oprativeMap=new HashMap();
    private HashMap loanMap=new HashMap();
    private LinkedHashMap technicalMap=new LinkedHashMap();
    private HashMap laungeMap=new HashMap();
    private LinkedHashMap dependentMap=new LinkedHashMap();
    private HashMap relativeMap=new HashMap();
    private LinkedHashMap directorMap=new LinkedHashMap();
    private String txtRemarks = "";
    
    private ComboBoxModel cbmTitle ,cbmReligion,cbmCaste,cbmAddressType,cbmCity,cbmState,cbmCountry,cbmLevelEducation,cbmSpecilization,cbmGrade,
    cbmTechnicalQualification,cbmLanguage,cbmReleationShip,cbmBranchId,cbmFsatherTitle,cbmMotherTitle,cbmReleationFHTitle,
    cbmPFAcNominee,cbmPassportTitle,cbmTechnicalQualificationSpecilization,cbmTechnicalQualificationGrade,cbmFamilyMemberProf,
    cbmFamilyMemEducation,cbmBloodGroup,cbmDomicileState,cbmDirectorTittle,cbmReleativeTittle,cbmReleativeBranchId,
    cbmReleativeDisg,cbmDirectorReleationShip,cbmReleativeReleationShip,cbmUnionMember ,cbmSocietyMember,cbmProbationPeriod
    ,cbmPresentBranchId,cbmReginoalOffice,cbmZonalOffice , cbmDesignation,cbmPresentGrade
    ,cbmAccountType,cbmSalaryCrBranch,cbmEmployeeLoanType,cbmLoanAvailedBranch;
    private EnhancedTableModel tblContDet,tblAcademicDet,tblTechnicalDet,tblLanguageDet,tblFamilyDet,tblReleativeWorkingInBank,tblDetailsOfRelativeDirector,
    tblOprative,tblEmployeeLoan,tblPromotion;
    private EmployeeMasterRB resourceBundle = new EmployeeMasterRB();
    HashMap addressMap = new HashMap();
    HashMap deletedAddressMap = new HashMap();
    HashMap deletedEducationsMap = new HashMap();
    HashMap deletedOprativeMap = new HashMap();
    HashMap deletedTechnicalMap = new HashMap();
    HashMap deletedLoanMap = new HashMap();
    HashMap deletedLanguageMap = new HashMap();
    HashMap deletedDependent = new HashMap();
    HashMap deletedReleativeMap = new HashMap();
    HashMap deletedPromotionMap = new HashMap();
    
    HashMap deletedDirectorMap = new HashMap();
    private String txtPhoneNumber = "";
    private String txtAreaCode = "";
    private String cboPhoneType;
    private ComboBoxModel cbmPhoneType;
    private EnhancedTableModel tblPhoneList;
    private String txtEmpId="";
    private String sysId="";
    private String cboEmpTitle="";
    private String txtEmpFirstName="";
    private String txtEmpMiddleName="";
    private String txtEmpLastName="";
    private String txtEmpFatherFirstName="";
    private String txtEmpFatherMIddleName="";
    private String txtEmpFatherLasteName="";
    private String txtEmpMotherFirstName="";
    private String txtEmpMotherMIddleName="";
    private String txtEmpMotherLasteName="";
    private String cboEmpFatheTitle="";
    private String cboEmpMotherTitle="";
    private String tdtEmpDateOfBirth="";
    private String txtempAge="";
    private String txtempPlaceOfBirth="";
    private String cboEmpReligon="";
    private String cboEmpCaste="";
    private String txtEmpHomeTown="";
    private String txtEmpIdCardNo="";
    private String txtEmpUIdNo="";
    private String txtEmpPanNo="";
    private String txtEmpPfNo="";
    private String cboEmpPfNominee="";
    private String tdtEmpJoinDate="";
    private String tdtEmpRetirementDate="";
    private String rdoEmpGender="";
    private String rdoMaritalStatus="";
    private String rdoFatherHusband="";
    //emp Address details
    private String cboEmpAddressType="";
    private String txtEmpStreet="";
    private String txtEmpArea="";
    private String cboEmpCity="";
    private String cboEmpState="";
    private String cboEmpCountry="";
    private String txtEmpPinNo="";
    private String txtEmpPhoneNoCountryCode="";
    private String txtEmpPhoneNoAreaCode="";
    private String txtEmpPhoneNo="";
    private String txtEmpMobileNoCountryCode="";
    private String txtEmpMobileNoAreaCode="";
    private String txtEmpMobileNo="";
    //passport Details
    private String txtPassportFirstName = "";
    private String  txtPassportMiddleName = "";
    private String txtPassportLastName = "";
    private String tdtPassportIssueDt = "";
    private String tdtPassportValidUpto = "";
    private String  txtPassportNo = "";
    private String  txtPassportIssueAuth = "";
    private String cboPassportTitle="";
    private ComboBoxModel cbmPassportIssuePlace;
    private String cboPassportIssuePlace="";
    //Academic Education Details
    private String academicID;
    private String operativeId;
    private String cboEmpAcademicLevel="";
    private String txtEmpAcademicSchool="";
    private String tdtAcademicYearOfPassing="";
    private String cboAcademicSpecialization="";
    private String txtAcademicUniverSity="";
    private String txtAcademicMarksScored="";
    private String txtAcademicTotalMarks="";
    private String txtAcademicPer="";
    private String cboAcademicGrade="";
    
    //Technical  Education Details
    private String cboTechnicalLevel="";
    private String txtTechnicalSchool="";
    private String tdtTechnicalYearOfPassing="";
    private String cboTechnicalSpecialization="";
    private String txtTechnicalUniverSity="";
    private String txtTechnicalMarksScored="";
    private String txtTechnicalTotalMarks="";
    private String txtTechnicalPer="";
    private String cboTechnicalGrade="";
    private String technicalID="";
    
    
    //PRIVATE LANGUAGE
    private String cboLanguageType="";
    private String rdoWrite="";
    private String rdoRead="";
    private String rdoSpeak="";
    
    //private Dependent
    private String cboDepReleationShip="";
    private String txtEmpDepFirstName="";
    private String txtEmpDepMIddleName="";
    private String txtEmpDepLasteName="";
    private String cboEmpDepTitle="";
    private String tdtDepDateOfBirth="" ;
    private String cboDepEducation="";
    private String cboDepProfession="";
    private String rdoDepYesNo="";
    private String rdoLiableYesNo="";
    private String dependentId="";
    private HashMap phoneMap;
    private HashMap phoneList;
    private HashMap deletedPhoneMap;
    private String commAddrType = "";
    private String depndentSlno ="";
    private boolean addressedChanged=false;
    private boolean educationChanged=false;
    private boolean oprativeChanged =false;
    private boolean languageChanged=false;
    private boolean dependentChanged=false;
    private boolean relativeChanged=false;
    private boolean directorChanged=false;
    private String txtDepIncomePerannum= "";
    private String txtEmpWith = "";
    private String cboBloodGroup ="";
    private String txtMajorHealthProbeem="";
    
    private String txtPhysicalHandicap="";
    private String txtDrivingLicenceNo="";
    private String  tdtDLRenewalDate ="";
    private String txtEmailId="";
    private String cboDomicileState="";
    
    private String txtRelativeStaffId="";
    private String cboRelativeTittle="";
    private String txtReleativeFirstName="";
    private String txtReleativeMiddleName="";
    private String txtReleativeLastName="";
    private String cboReleativeDisg="";
    private String cboReleativeBranchId="";
    private String cboReleativeReleationShip="";
    private String releativeSysId="";
    private String cboDirectorTittle="";
    private String txtDirectorFirstName="";
    private String txtDirectorMiddleName="";
    private String txtDirectorLastName="";
    private String txtDirectorReleationShip="";
    
    //prsentDetails
    private String rdoGradutionIncrementYesNo="";
    private String tdtGradutionIncrementReleasedDate="";
    private String rdoCAIIBPART1IncrementYesNo="";
    private String tdtCAIIBPART1IncrementReleasedDate="";
    private String rdoCAIIBPART2IncrementYesNo="";
    private String tdtCAIIBPART2IncrementReleasedDate="";
    private String rdoAnyOtherIncrementYesNo="";
    private String txtAnyOtherIncrementInstitutionName="";
    private String tdtAnyOtherIncrementReleasedDate="";
    private String txtPresentBasic="";
    private String tdtLastIncrmentDate="";
    private String txtLossPay_Months="";
    private String txtLossOfpay_Days="";
    private String tdtNextIncrmentDate="";
    private String rdoSigNoYesNo="";
    private String txtSignatureNo="";
    private String cboUnionMember="";
    private String cboSocietyMember="";
    private String societyMemberNo="";
    private String clubMembership="";
    private String clubName="";
    private String txtProbationPeriod="";
    private String cboProbationPeriod="";
    private String tdtConfirmationDate="";
    private String tdtDateofRetirement="";
    private String txtPFNumber="";
    private String cboPFAcNominee="";
    private String cboPresentBranchId="";
    private String cboReginoalOffice="";
    private String cboZonalOffice="";
    private String tdtWorkingSince="";
    private String cboDesignation="";
    private String cboPresentGrade="";
    // oprative details
    
    private String cboOprativePordId="";
    private String txtOPAcNo="";
    private String cboOpACBranch="";
    
    //termLoazn
    
    private String cboEmployeeLoanType="";
    private String cboLoanAvailedBranch="";
    private String txtLoanSanctionRefNo="";
    private String tdtLoanSanctionDate="";
    private String txtLoanNo="";
    private String txtLoanAmount="";
    private String txtLoanRateofInterest="";
    private String txtLoanNoOfInstallments="";
    private String txtLoanInstallmentAmount="";
    private String tdtLoanRepaymentStartDate="";
    private String tdtLoanRepaymentEndDate="";
    private String rdoLoanPreCloserYesNo="";
    private String tdtLoanCloserDate="";
    private String txtLoanRemarks="";
    
    private String cboAccountType="";
    private String cboSalaryCrBranch="";
    private String txtAccountNo="";
    
    private String directorID;
    private String lblPhoto;
    private String photoFile;
    //    private File photoByteArray;
    private byte[] photoByteArray;
    
    private String lblSign = "";
    private String signFile;
    
    //    promotion
    private ComboBoxModel cbmPromotionGradeValue,cbmBranchwiseValue,cbmRegionalValue,cbmPromotedDesignation;
    private TableModel tbmPromotion,tbmLog;
    private ArrayList PromotionTOs,deletePromotionList;
    private String cboPromotionGradeValue;
    private String cboPromotedDesignation;
    private String lblPromotionSLNOValue = "";
    private String txtPromotionEmployeeId = "";
    private String txtPromotionEmployeeName="";
    private String txtPromotionDesignation="";
    private String txtPromotionEmpBranch="";
    private String txtPromotionLastDesg = "";
    private String tdtPromotionEffectiveDateValue = "";
    private String tdtPromotionCreatedDateValue = "";
    private String txtPromotionBasicPayValue = "";
    private String promotionID="";
    private String txtNewBasic="";
    private String txtPromotionLastGrade="";
    
    //    private File signByteArray;
    private byte[] signByteArray;
    ArrayList IncVal= new ArrayList();
    public void resetPassPortetails(){
        setTxtPassportFirstName("");
        setTxtPassportMiddleName("");
        setTxtPassportLastName("");
        setTdtPassportIssueDt("");
        setTdtPassportValidUpto("");
        setTxtPassportNo("");
        setTxtPassportIssueAuth("");
        setCboPassportTitle("");
        setCboPassportIssuePlace("");
    }
    public void resetAcademic(){
        setCboEmpAcademicLevel("");
        setTxtEmpAcademicSchool("");
        setTdtAcademicYearOfPassing("");
        setCboAcademicSpecialization("");
        setTxtAcademicUniverSity("");
        setTxtAcademicMarksScored("");
        setTxtAcademicTotalMarks("");
        setTxtAcademicPer("");
        setCboAcademicGrade("");
        
    }
    
    public void resetFormEmployeeMaster(){
        resetAcademic();
        resetPassPortetails();
        resetOprative();
        resetLoans();
        resetDependent();
        resetDirector();
        resetRelative();
        resetLanguage();
        resetTechnical();
        resetAddressDetails();
        resetAddressExceptAddTypeDetails();
        resetPhoneDetails();
        resetPhotoSign();
        resetDeleteAddress();
        resetAddressListTable();
        resetEducationListTable();
        resetTechnicalListTable();
        resetLanguageListTable();
        resetDependentListTable();
        resetRelativeListTable();
        resetDirectorListTable();
        resetPhoneListTable();
        reSetPresentDetails();
        resetLoanListTable();
        resetOperativeDetails();
        resetOprativeListTable();
        resetPromotion();
        resetPromotionListTable();
        resetForm();
        phoneMap = null;
        phoneList = null;
    }
    public void resetPromotion(){
        setLblPromotionSLNOValue("");
        setTxtRemarks("");
        setTxtNewBasic("");
        setPromotionID("");
        setTxtPromotionEmployeeId("");
        setTxtPromotionEmployeeName("");
        setTxtPromotionDesignation("");
        setTxtPromotionEmpBranch("");
        setPromotionID("");
        setTxtPromotionLastDesg("");
        setTdtPromotionEffectiveDateValue("");
        setTdtPromotionCreatedDateValue("");
        setTxtPromotionBasicPayValue("");
        setCboPromotedDesignation("");
        setcboPromotionGradeValue("");
    }
    public void resetOprative(){
        setCboOprativePordId("");
        setTxtOPAcNo("");
        setCboOpACBranch("");
    }
    public void resetLoans(){
        setCboEmployeeLoanType("");
        setCboLoanAvailedBranch("");
        setTxtLoanSanctionRefNo("");
        setTdtLoanSanctionDate("");
        setTxtLoanNo("");
        setTxtLoanAmount("");
        setTxtLoanRateofInterest("");
        setTxtLoanNoOfInstallments("");
        setTxtLoanInstallmentAmount("");
        setTdtLoanRepaymentStartDate("");
        setTdtLoanRepaymentEndDate("");
        setRdoLoanPreCloserYesNo("");
        setTdtLoanCloserDate("");
        setTxtLoanRemarks("");
    }
    public void resetDependent(){
        setDependentId("");
        setCboDepReleationShip("");
        setTxtEmpDepFirstName("");
        setTxtEmpDepMIddleName("");
        setTxtEmpDepLasteName("");
        setCboEmpDepTitle("");
        setTdtDepDateOfBirth("");
        setCboDepEducation("");
        setCboDepProfession("");
        setRdoDepYesNo("");
        setRdoLiableYesNo("");
        setTxtDepIncomePerannum("");
        setTxtEmpWith("");
    }
    
    public void resetDirector(){
        setDirectorID("");
        setCboDirectorTittle("");
        setTxtDirectorFirstName("");
        setTxtDirectorMiddleName("");
        setTxtDirectorLastName("");
        setTxtDirectorReleationShip("");
        resetDirectorListTable();
        directorMap = null;
    }
    public void resetRelative(){
        setTxtRelativeStaffId("");
        setCboRelativeTittle("");
        setTxtReleativeFirstName("");
        setTxtReleativeMiddleName("");
        setTxtReleativeLastName("");
        setCboReleativeReleationShip("");
        setCboReleativeBranchId("");
        setCboReleativeDisg("");
        setCboDesignation("");
        
    }
    
    
    public void resetLanguage(){
        setCboLanguageType("");
        setRdoWrite("");
        setRdoRead("");
        setRdoSpeak("");
    }
    
    public void resetTechnical(){
        //Technical  Education Details
        setTechnicalID("");
        setCboTechnicalLevel("");
        setTxtTechnicalSchool("");
        setTdtTechnicalYearOfPassing("");
        setCboTechnicalSpecialization("");
        setTxtTechnicalUniverSity("");
        setTxtTechnicalMarksScored("");
        setTxtTechnicalTotalMarks("");
        setTxtTechnicalPer("");
        setCboTechnicalGrade("");
    }
    public void resetAddressDetails(){
        getCbmAddressType().setKeyForSelected("");
        setCboEmpAddressType("");
        setTxtEmpStreet("");
        setTxtEmpArea("");
        setCboEmpCity("");
        setCboEmpState("");
        setCboEmpCountry("");
        setTxtEmpPinNo("");
        setTxtEmpPhoneNoCountryCode("");
        setTxtEmpPhoneNoAreaCode("");
        setTxtEmpPhoneNo("");
        setTxtEmpMobileNoCountryCode("");
        setTxtEmpMobileNoAreaCode("");
        setTxtEmpMobileNo("");
        
    }
    
    public void resetAddressExceptAddTypeDetails(){
        setTxtEmpStreet("");
        setTxtEmpArea("");
        setCboEmpCity("");
        setCboEmpState("");
        setCboEmpCountry("");
        setTxtEmpPinNo("");
        setTxtEmpPhoneNoCountryCode("");
        setTxtEmpPhoneNoAreaCode("");
        setTxtEmpPhoneNo("");
        setTxtEmpMobileNoCountryCode("");
        setTxtEmpMobileNoAreaCode("");
        setTxtEmpMobileNo("");
    }
    public void resetForm() {
        setTxtEmpId("");
        setCboEmpTitle("");
        setTxtEmpFirstName("");
        setTxtEmpMiddleName("");
        setTxtEmpLastName("");
        setTxtEmpFatherFirstName("");
        setTxtEmpFatherMIddleName("");
        setTxtEmpFatherLasteName("");
        setTxtEmpMotherFirstName("");
        setTxtEmpMotherMIddleName("");
        setTxtEmpMotherLasteName("");
        setCboEmpFatheTitle("");
        setCboEmpMotherTitle("");
        setTdtEmpDateOfBirth("");
        setTxtempAge("");
        setTxtempPlaceOfBirth("");
        setCboEmpReligon("");
        setCboEmpCaste("");
        setTxtEmpHomeTown("");
        setTxtEmpIdCardNo("");
        setTxtEmpUIdNo("");
        setTxtEmpPanNo("");
        setTxtEmpPfNo("");
        setCboEmpPfNominee("");
        setTdtEmpJoinDate("");
        setTdtEmpRetirementDate("");
        setRdoEmpGender("");
        setRdoMaritalStatus("");
        setRdoFatherHusband("");
        setTxtPhysicalHandicap("");
        ttNotifyObservers();
    }
    public void resetPhoneDetails(){
        setTxtAreaCode("");
        setTxtPhoneNumber("");
        setCboPhoneType("");
    }
    
    public void resetPhotoSign(){
        setLblPhoto("");
        setPhotoByteArray(null);
        setLblSign("");
        setSignByteArray(null);
    }
    //for resetting the operative account details
    public void resetOperativeDetails() {
        setTxtAccountNo("");
        setCboSalaryCrBranch("");
        setCboAccountType("");
    }
    
    public void resetOprativeListTable() {
        for(int i = tblOprative.getRowCount(); i > 0; i--) {
            tblOprative.removeRow(0);
        }
    }
    
    public EmployeeMasterOB(int param) {
        screenCustType = param;
        try{
            curDate = ClientUtil.getCurrentDate();
            proxy = ProxyFactory.createProxy();
            map = new HashMap();
            map.put(CommonConstants.JNDI, "EmployeeMasterJNDI");
            map.put(CommonConstants.HOME, "serverside.employee.EmployeeMasterHome");
            map.put(CommonConstants.REMOTE, "serverside.employee.EmployeeMaster");
            createContactListTable();
            createTechnicalTable();
            createAcademicTable();
            createDependentTable();
            createLanguageTable();
            createphoneTable();
            createReleativeTable();
            createDirectorTable();
            createPromotionTable();
            loanListTable();
            oprativeListTable();
            
            fillDropdown();
            
        }catch(Exception e){
            parseException.logException(e,true);
        }
    }
    
    public EmployeeMasterOB() {
        curDate = ClientUtil.getCurrentDate();
    }
    
    /** To fill the comboboxes */
    private void fillDropdown() throws Exception{
        lookupMap = new HashMap();
        lookupMap.put(CommonConstants.JNDI, "LookUpJNDI");
        lookupMap.put(CommonConstants.HOME, "common.lookup.LookUpHome");
        lookupMap.put(CommonConstants.REMOTE, "common.lookup.LookUp");
        
        param = new HashMap();
        param.put(CommonConstants.MAP_NAME,null);
        
        final ArrayList lookupKey = new ArrayList();
        lookupKey.add("CUSTOMER.TITLE");
        lookupKey.add("GUARDIAN_RELATIONSHIP");
        lookupKey.add("CUSTOMER.ADDRTYPE");
        lookupKey.add("CUSTOMER.CITY");
        lookupKey.add("CUSTOMER.STATE");
        lookupKey.add("CUSTOMER.COUNTRY");
        lookupKey.add("CUSTOMER.EDUCATION");
        lookupKey.add("EMPLOYEE.EDUCATIONGRADE");
        lookupKey.add("EMPLOYEE.SPECIALIZATION");
        lookupKey.add("EMPLOYEE.TECHNICALQUALIFICATION");
        lookupKey.add("EMPLOYEE.LANGUAGE");
        lookupKey.add("CUSTOMER.PROFESSION");
        lookupKey.add("CUSTOMER.CASTE");
        lookupKey.add("CUSTOMER.RELIGION");
        lookupKey.add("CUSTOMER.PHONETYPE");
        lookupKey.add("CUSTOMER.BLOODGROUP");
        lookupKey.add("CUSTOMER.HANDICAP");
        lookupKey.add("EMPLOYEE.UNION");
        lookupKey.add("EMPLOYEE.SOCIETY");
        lookupKey.add("PERIOD");
        lookupKey.add("SALARY_STRUCTURE");
        param.put(CommonConstants.PARAMFORQUERY, lookupKey);
        final HashMap lookupValues = populateData(param);
        fillData((HashMap)lookupValues.get("CUSTOMER.TITLE"));
        cbmTitle = new ComboBoxModel(key,value);
        cbmFsatherTitle= new ComboBoxModel(key,value);
        cbmMotherTitle= new ComboBoxModel(key,value);
        cbmReleationFHTitle= new ComboBoxModel(key,value);
        cbmPassportTitle= new ComboBoxModel(key,value);
        cbmDirectorTittle=new ComboBoxModel(key,value);
        cbmReleativeTittle=new ComboBoxModel(key,value);
        fillData((HashMap)lookupValues.get("GUARDIAN_RELATIONSHIP"));
        cbmReleationShip = new ComboBoxModel(key,value);
        cbmPFAcNominee = new ComboBoxModel(key,value);
        cbmDirectorReleationShip= new ComboBoxModel(key,value);
        cbmReleativeReleationShip=new ComboBoxModel(key,value);
        fillData((HashMap)lookupValues.get("CUSTOMER.ADDRTYPE"));
        cbmAddressType = new ComboBoxModel(key,value);
        fillData((HashMap)lookupValues.get("CUSTOMER.CITY"));
        cbmCity = new ComboBoxModel(key,value);
        cbmPassportIssuePlace= new ComboBoxModel(key,value);
        fillData((HashMap)lookupValues.get("CUSTOMER.STATE"));
        cbmState = new ComboBoxModel(key,value);
        cbmDomicileState=new ComboBoxModel(key,value);
        fillData((HashMap)lookupValues.get("CUSTOMER.BLOODGROUP"));
        cbmBloodGroup= new ComboBoxModel(key,value);
        fillData((HashMap)lookupValues.get("CUSTOMER.COUNTRY"));
        cbmCountry= new ComboBoxModel(key,value);
        fillData((HashMap)lookupValues.get("CUSTOMER.EDUCATION"));
        cbmLevelEducation= new ComboBoxModel(key,value);
        cbmFamilyMemEducation = new ComboBoxModel(key,value);
        fillData((HashMap)lookupValues.get("EMPLOYEE.EDUCATIONGRADE"));
        cbmGrade= new ComboBoxModel(key,value);
        cbmTechnicalQualificationGrade= new ComboBoxModel(key,value);
        fillData((HashMap)lookupValues.get("EMPLOYEE.SPECIALIZATION"));
        cbmSpecilization= new ComboBoxModel(key,value);
        cbmTechnicalQualificationSpecilization= new ComboBoxModel(key,value);
        fillData((HashMap)lookupValues.get("EMPLOYEE.TECHNICALQUALIFICATION"));
        cbmTechnicalQualification= new ComboBoxModel(key,value);
        fillData((HashMap)lookupValues.get("EMPLOYEE.LANGUAGE"));
        cbmLanguage= new ComboBoxModel(key,value);
        fillData((HashMap)lookupValues.get("CUSTOMER.PROFESSION"));
        cbmFamilyMemberProf= new ComboBoxModel(key,value);
//        fillData((HashMap)lookupValues.get("CUSTOMER.CASTE"));
//        cbmCaste= new ComboBoxModel(key,value);
        fillData((HashMap)lookupValues.get("CUSTOMER.RELIGION"));
        cbmReligion= new ComboBoxModel(key,value);
        fillData((HashMap)lookupValues.get("CUSTOMER.PHONETYPE"));
        cbmPhoneType= new ComboBoxModel(key,value);
        fillData((HashMap)lookupValues.get("EMPLOYEE.UNION"));
        cbmUnionMember= new ComboBoxModel(key,value);
        fillData((HashMap)lookupValues.get("EMPLOYEE.SOCIETY"));
        cbmSocietyMember= new ComboBoxModel(key,value);
        fillData((HashMap)lookupValues.get("PERIOD"));
        cbmProbationPeriod= new ComboBoxModel(key,value);
        fillData((HashMap)lookupValues.get("SALARY_STRUCTURE"));
        cbmPresentGrade= new ComboBoxModel(key,value);
        key =  new ArrayList();
        value = new ArrayList();
        fillData((HashMap)lookupValues.get("SALARY_STRUCTURE"));
        this.cbmPromotionGradeValue = new ComboBoxModel(key,value);
        HashMap param = new HashMap();
        param.put(CommonConstants.MAP_NAME,"getOwnBranches");
        HashMap where = new HashMap();
        where.put("BRANCH_ID", TrueTransactMain.BRANCH_ID);
        param.put(CommonConstants.PARAMFORQUERY, where);
        where = null;
        HashMap keyValue = (HashMap) ClientUtil.populateLookupData(param).get(CommonConstants.DATA);
        fillData(keyValue);
        ArrayList branchKeys = new ArrayList();
        ArrayList branchValues = new ArrayList();
        branchKeys.addAll(key);
        branchValues.addAll(value);
//        cbmBranchId = new ComboBoxModel(branchKeys,branchValues);
        branchKeys = new ArrayList();
        branchValues = new ArrayList();
        branchKeys.addAll(key);
        branchValues.addAll(value);
//        cbmReleativeBranchId = new ComboBoxModel(branchKeys,branchValues);
        branchKeys = new ArrayList();
        branchValues = new ArrayList();
        branchKeys.addAll(key);
        branchValues.addAll(value);
//        cbmPresentBranchId= new ComboBoxModel(branchKeys,branchValues);
        branchKeys = new ArrayList();
        branchValues = new ArrayList();
        branchKeys.addAll(key);
        branchValues.addAll(value);
//        cbmReginoalOffice= new ComboBoxModel(branchKeys,branchValues);
        setZonalEmpMaster(null);
        setBranchEmpMaster(null);
        branchKeys = new ArrayList();
        branchValues = new ArrayList();
        branchKeys.addAll(key);
        branchValues.addAll(value);
//        cbmZonalOffice = new ComboBoxModel(branchKeys,branchValues);
        branchKeys = new ArrayList();
        branchValues = new ArrayList();
        branchKeys.addAll(key);
        branchValues.addAll(value);
        cbmSalaryCrBranch= new ComboBoxModel(branchKeys,branchValues);
        branchKeys = new ArrayList();
        branchValues = new ArrayList();
        branchKeys.addAll(key);
        branchValues.addAll(value);
        cbmLoanAvailedBranch= new ComboBoxModel(branchKeys,branchValues);
        
        param=new HashMap();
        param.put(CommonConstants.MAP_NAME,"getDesignation");
        param.put(CommonConstants.PARAMFORQUERY, null);
        where = null;
        keyValue=new HashMap();
        keyValue = (HashMap) ClientUtil.populateLookupData(param).get(CommonConstants.DATA);
        cbmReleativeDisg=new ComboBoxModel((ArrayList)keyValue.get(CommonConstants.KEY),(ArrayList)keyValue.get(CommonConstants.VALUE));
        cbmDesignation=new ComboBoxModel();
        cbmCaste= new ComboBoxModel();
//        cbmReleativeDisg=new ComboBoxModel();
        //        cbmPresentGrade=new ComboBoxModel();
        //        cbmPresentGrade=new ComboBoxModel((ArrayList)keyValue.get(CommonConstants.KEY),(ArrayList)keyValue.get(CommonConstants.VALUE));
        //        keyValue=new HashMap();
        //        keyValue.put("","");
        //        keyValue.put("Saving Banking Account","SB");
        //        keyValue.put("OverDraft","OD");
        //        cbmAccountType=new ComboBoxModel((ArrayList)keyValue.get(CommonConstants.KEY),(ArrayList)keyValue.get(CommonConstants.VALUE));
        cbmAccountType=new ComboBoxModel();
        cbmAccountType.addKeyAndElement("","");
        cbmAccountType.addKeyAndElement("SB","Saving Banking Account");
        cbmAccountType.addKeyAndElement("OD","Over Draft");
        
        param=new HashMap();
        param.put(CommonConstants.MAP_NAME,"Cash.getAccProductTL");
        param.put(CommonConstants.PARAMFORQUERY, null);
        where = null;
        keyValue=new HashMap();
        keyValue = (HashMap) ClientUtil.populateLookupData(param).get(CommonConstants.DATA);
        cbmEmployeeLoanType=new ComboBoxModel((ArrayList)keyValue.get(CommonConstants.KEY),(ArrayList)keyValue.get(CommonConstants.VALUE));
        
    }
    
    
    /** To set the key & value for comboboxes */
    private void fillData(HashMap keyValue)  throws Exception{
        ArrayList    keys = (ArrayList)keyValue.get(CommonConstants.KEY);
        
        key = (ArrayList)keyValue.get(CommonConstants.KEY);
        value = (ArrayList)keyValue.get(CommonConstants.VALUE);
        
    }
    
    /** To get data for comboboxes */
    private HashMap populateData(HashMap obj)  throws Exception{
        keyValue = proxy.executeQuery(obj,lookupMap);
        log.info("Got HashMap");
        return keyValue;
    }
    
    private void createContactListTable() throws Exception{
        final ArrayList contactAddressColumn = new ArrayList();
        contactAddressColumn.add("AddressType");
        contactAddressColumn.add("Status");
        tblContDet = new EnhancedTableModel(null, contactAddressColumn);
        
    }
    
    private void oprativeListTable() throws Exception{
        final ArrayList oprativeColumn = new ArrayList();
        oprativeColumn.add("SlNo");
        oprativeColumn.add("Prod Type");
        oprativeColumn.add("Acc No");
        oprativeColumn.add("Branch Id");
        
        tblOprative = new EnhancedTableModel(null, oprativeColumn);
        
    }
    
    private void loanListTable() throws Exception{
        final ArrayList loanColumn = new ArrayList();
        loanColumn.add("Prod Type");
        loanColumn.add("Acc No");
        loanColumn.add("Branch Id");
        
        tblEmployeeLoan = new EnhancedTableModel(null, loanColumn);
        
    }
    private void createDirectorTable() throws Exception{
        final ArrayList directorColumn = new ArrayList();
        directorColumn.add("Sl No");
        directorColumn.add("Name");
        
        tblDetailsOfRelativeDirector = new EnhancedTableModel(null, directorColumn);
        
    }
    
    private void createAcademicTable() throws Exception{
        final ArrayList academicColumn = new ArrayList();
        academicColumn.add("Sl No");
        academicColumn.add("Education Type");
        academicColumn.add("Year Of Passing");
        tblAcademicDet = new EnhancedTableModel(null, academicColumn);
        
    }
    private void createPromotionTable() throws Exception{
        final ArrayList promotionColumn = new ArrayList();
        promotionColumn.add("Sl No");
        promotionColumn.add("Grade");
        promotionColumn.add("Designation");
        promotionColumn.add("Promoted Grade");
        promotionColumn.add("Promoted Desig");
        tblPromotion = new EnhancedTableModel(null, promotionColumn);
        
    }
    public int insertPromotionData(int rowNo) {
        //      To insert values on to the PromotionTO one Row at a time
        PromotionTO obj = PromotionTO(rowNo);
        if(rowNo == -1){
            //      To insert To values Into PromotionTOs Arraylist
            PromotionTOs.add(obj);
            ArrayList irRow = this.setRowPromotion(obj);
            tbmPromotion.insertRow(tbmPromotion.getRowCount(), irRow);
            tbmPromotion.fireTableDataChanged();
        }
        return 0;
    }
    private PromotionTO PromotionTO(int rowNo){
        //        PromotionTO obj = null;
        //        obj = new PromotionTO();
        //        if(rowNo == -1){
        //            //            To add into the PromotionTO for a new value
        //            if(tbmPromotion.getRowCount() == 0){
        //                obj.setSlNo(new Double(1));
        //            }else if(tbmPromotion.getRowCount()>0){
        //                obj.setSlNo(new Double(tbmPromotion.getRowCount() + 1));
        //            }
        //            obj.setEmpId(getTxtPromotionEmployeeId());
        //            obj.setTxtPromotionEmployeeName(getTxtPromotionEmployeeName());
        //            obj.setLastDesignation(getTxtPromotionDesignation());
        //            obj.setEffectiveDate(DateUtil.getDateMMDDYYYY(getTdtPromotionEffectiveDateValue()));
        //            obj.setCreatedDate(DateUtil.getDateMMDDYYYY(getTdtPromotionCreatedDateValue()));
        //            obj.setEmployeeStage(getTxtPromotionLastDesg());
        //            obj.setTxtPromotionEmpBranch(getTxtPromotionEmpBranch());
        //            obj.setPresentBasic(getTxtPromotionBasicPayValue());
        //            obj.setPromotionGrade(CommonUtil.convertObjToStr(getCbmPromotionGradeValue().getKeyForSelected()));
        //            obj.setPromotionDesignation(CommonUtil.convertObjToStr(getCbmPromotedDesignation().getKeyForSelected()));
        //            obj.setStatus(CommonConstants.STATUS_CREATED);
        //            obj.setStatusBy(TrueTransactMain.USER_ID);
        //            obj.setStatusDt(curDate);
        //        }else{
        //            //            To add into PromotionTo in Edit Mode
        //            obj = new PromotionTO();
        //            obj = (PromotionTO)PromotionTOs.get(rowNo);
        //            obj.setTxtPromotionEmployeeName(getTxtPromotionEmployeeName());
        //            obj.setLastDesignation(getTxtPromotionDesignation());
        //            obj.setEffectiveDate(DateUtil.getDateMMDDYYYY(getTdtPromotionEffectiveDateValue()));
        //            obj.setCreatedDate(DateUtil.getDateMMDDYYYY(getTdtPromotionCreatedDateValue()));
        //            obj.setEmployeeStage(getTxtPromotionLastDesg());
        //            obj.setTxtPromotionEmpBranch(getTxtPromotionEmpBranch());
        //            obj.setPromotionID(getPromotionID());
        //            obj.setPresentBasic(getTxtPromotionBasicPayValue());
        //            obj.setPromotionGrade(CommonUtil.convertObjToStr(getCbmPromotionGradeValue().getKeyForSelected()));
        //            obj.setPromotionDesignation(CommonUtil.convertObjToStr(getCbmPromotedDesignation().getKeyForSelected()));
        //            obj.setStatusBy(TrueTransactMain.USER_ID);
        //            obj.setStatusDt(curDate);
        //            ArrayList irRow = setRowPromotion(obj);
        //            PromotionTOs.set(rowNo,obj);
        //            tbmPromotion.removeRow(rowNo);
        //            tbmPromotion.insertRow(rowNo,irRow);
        //            tbmPromotion.fireTableDataChanged();
        //        }
        return null;
    }
    
    private ArrayList setRowPromotion(PromotionTO obj){
        ArrayList row = new ArrayList();
        row.add(obj.getPromotionID());
        row.add(obj.getTxtPromotionLastGrade());
        row.add(obj.getLastDesignation());
        row.add(obj.getPromotionGrade());
        row.add(obj.getPromotionDesignation());
        return row;
    }
    private void createphoneTable() throws Exception{
        final ArrayList phoneColumn = new ArrayList();
        phoneColumn.add("Sl No");
        phoneColumn.add("Type");
        phoneColumn.add("Number");
        tblPhoneList = new EnhancedTableModel(null, phoneColumn);
        
    }
    private void createTechnicalTable() throws Exception{
        final ArrayList technicalColumn = new ArrayList();
        technicalColumn.add("Sl No");
        technicalColumn.add("technical Education Type");
        technicalColumn.add("Year Of Passing");
        tblTechnicalDet = new EnhancedTableModel(null, technicalColumn);
        
    }
    
    private void createReleativeTable() throws Exception{
        final ArrayList releativeColumn = new ArrayList();
        releativeColumn.add("Employee No");
        releativeColumn.add("Name");
        releativeColumn.add("RelationShip");
        tblReleativeWorkingInBank = new EnhancedTableModel(null, releativeColumn);
        
    }
    
    private void createLanguageTable() throws Exception{
        final ArrayList languageColumn = new ArrayList();
        languageColumn.add("Langauage");
        
        tblLanguageDet = new EnhancedTableModel(null, languageColumn);
    }
    
    private void createDependentTable() throws Exception{
        final ArrayList dependentColumn = new ArrayList();
        dependentColumn.add("Sl No.");
        dependentColumn.add("RelationShip");
        dependentColumn.add("Name");
        tblFamilyDet = new EnhancedTableModel(null, dependentColumn);
    }
    
    
    
    void setActionType(int actionType){
        this.actionType = actionType;
        setChanged();
    }
    int getActionType(){
        return this.actionType;
    }
    
    public int getResult(){
        return this.result;
    }
    
    public void setResult(int result) {
        this.result = result;
        setChanged();
    }
    
    public String getLblStatus(){
        return this.lblStatus;
    }
    
    public void setLblStatus(String lblStatus) {
        this.lblStatus = lblStatus;
        setChanged();
    }
    public void ttNotifyObservers(){
        notifyObservers();
        //        setChanged();
    }
    
    public void setStatus(){
        this.setLblStatus( ClientConstants.ACTION_STATUS[this.getActionType()]);
        ttNotifyObservers();
        setChanged();
        
    }
    
    /** To update the Status based on result performed by doAction() method */
    public void setResultStatus(){
        this.setLblStatus(ClientConstants.RESULT_STATUS[this.getResult()]);
        ttNotifyObservers();
    }
    /** This will show the alertwindow **/
    public  int showAlertWindow(String alertMsg) throws Exception {
        int optionSelected = 1;
        String[] options = {resourceBundle.getString("cDialogYes"), resourceBundle.getString("CDialogNo")};
        optionSelected = COptionPane.showOptionDialog(null,resourceBundle.getString(alertMsg), CommonConstants.INFORMATIONTITLE,
        COptionPane.YES_NO_OPTION, COptionPane.WARNING_MESSAGE,
        null, options, options[0]);
        return optionSelected;
    }
    
    private String getCommand() throws Exception{
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
    
    /**
     * Getter for property cbmTitle.
     * @return Value of property cbmTitle.
     */
    public com.see.truetransact.clientutil.ComboBoxModel getCbmTitle() {
        return cbmTitle;
    }
    
    /**
     * Setter for property cbmTitle.
     * @param cbmTitle New value of property cbmTitle.
     */
    public void setCbmTitle(com.see.truetransact.clientutil.ComboBoxModel cbmTitle) {
        this.cbmTitle = cbmTitle;
    }
    
    /**
     * Getter for property cbmReligion.
     * @return Value of property cbmReligion.
     */
    public com.see.truetransact.clientutil.ComboBoxModel getCbmReligion() {
        return cbmReligion;
    }
    
    /**
     * Setter for property cbmReligion.
     * @param cbmReligion New value of property cbmReligion.
     */
    public void setCbmReligion(com.see.truetransact.clientutil.ComboBoxModel cbmReligion) {
        this.cbmReligion = cbmReligion;
    }
    
    /**
     * Getter for property cbmCaste.
     * @return Value of property cbmCaste.
     */
    public com.see.truetransact.clientutil.ComboBoxModel getCbmCaste() {
        return cbmCaste;
    }
    
    /**
     * Setter for property cbmCaste.
     * @param cbmCaste New value of property cbmCaste.
     */
    public void setCbmCaste(com.see.truetransact.clientutil.ComboBoxModel cbmCaste) {
        this.cbmCaste = cbmCaste;
    }
    
    /**
     * Getter for property cbmAddressType.
     * @return Value of property cbmAddressType.
     */
    public com.see.truetransact.clientutil.ComboBoxModel getCbmAddressType() {
        return cbmAddressType;
    }
    
    /**
     * Setter for property cbmAddressType.
     * @param cbmAddressType New value of property cbmAddressType.
     */
    public void setCbmAddressType(com.see.truetransact.clientutil.ComboBoxModel cbmAddressType) {
        this.cbmAddressType = cbmAddressType;
    }
    
    /**
     * Getter for property cbmCity.
     * @return Value of property cbmCity.
     */
    public com.see.truetransact.clientutil.ComboBoxModel getCbmCity() {
        return cbmCity;
    }
    
    /**
     * Setter for property cbmCity.
     * @param cbmCity New value of property cbmCity.
     */
    public void setCbmCity(com.see.truetransact.clientutil.ComboBoxModel cbmCity) {
        this.cbmCity = cbmCity;
    }
    
    /**
     * Getter for property cbmState.
     * @return Value of property cbmState.
     */
    public com.see.truetransact.clientutil.ComboBoxModel getCbmState() {
        return cbmState;
    }
    
    /**
     * Setter for property cbmState.
     * @param cbmState New value of property cbmState.
     */
    public void setCbmState(com.see.truetransact.clientutil.ComboBoxModel cbmState) {
        this.cbmState = cbmState;
    }
    
    /**
     * Getter for property cbmCountry.
     * @return Value of property cbmCountry.
     */
    public com.see.truetransact.clientutil.ComboBoxModel getCbmCountry() {
        return cbmCountry;
    }
    
    /**
     * Setter for property cbmCountry.
     * @param cbmCountry New value of property cbmCountry.
     */
    public void setCbmCountry(com.see.truetransact.clientutil.ComboBoxModel cbmCountry) {
        this.cbmCountry = cbmCountry;
    }
    
    /**
     * Getter for property cbmLevelEducation.
     * @return Value of property cbmLevelEducation.
     */
    public com.see.truetransact.clientutil.ComboBoxModel getCbmLevelEducation() {
        return cbmLevelEducation;
    }
    
    /**
     * Setter for property cbmLevelEducation.
     * @param cbmLevelEducation New value of property cbmLevelEducation.
     */
    public void setCbmLevelEducation(com.see.truetransact.clientutil.ComboBoxModel cbmLevelEducation) {
        this.cbmLevelEducation = cbmLevelEducation;
    }
    
    /**
     * Getter for property cbmSpecilization.
     * @return Value of property cbmSpecilization.
     */
    public com.see.truetransact.clientutil.ComboBoxModel getCbmSpecilization() {
        return cbmSpecilization;
    }
    
    /**
     * Setter for property cbmSpecilization.
     * @param cbmSpecilization New value of property cbmSpecilization.
     */
    public void setCbmSpecilization(com.see.truetransact.clientutil.ComboBoxModel cbmSpecilization) {
        this.cbmSpecilization = cbmSpecilization;
    }
    
    /**
     * Getter for property cbmGrade.
     * @return Value of property cbmGrade.
     */
    public com.see.truetransact.clientutil.ComboBoxModel getCbmGrade() {
        return cbmGrade;
    }
    
    /**
     * Setter for property cbmGrade.
     * @param cbmGrade New value of property cbmGrade.
     */
    public void setCbmGrade(com.see.truetransact.clientutil.ComboBoxModel cbmGrade) {
        this.cbmGrade = cbmGrade;
    }
    
    /**
     * Getter for property cbmTechnicalQualification.
     * @return Value of property cbmTechnicalQualification.
     */
    public com.see.truetransact.clientutil.ComboBoxModel getCbmTechnicalQualification() {
        return cbmTechnicalQualification;
    }
    
    /**
     * Setter for property cbmTechnicalQualification.
     * @param cbmTechnicalQualification New value of property cbmTechnicalQualification.
     */
    public void setCbmTechnicalQualification(com.see.truetransact.clientutil.ComboBoxModel cbmTechnicalQualification) {
        this.cbmTechnicalQualification = cbmTechnicalQualification;
    }
    
    /**
     * Getter for property cbmLanguage.
     * @return Value of property cbmLanguage.
     */
    public com.see.truetransact.clientutil.ComboBoxModel getCbmLanguage() {
        return cbmLanguage;
    }
    
    /**
     * Setter for property cbmLanguage.
     * @param cbmLanguage New value of property cbmLanguage.
     */
    public void setCbmLanguage(com.see.truetransact.clientutil.ComboBoxModel cbmLanguage) {
        this.cbmLanguage = cbmLanguage;
    }
    
    /**
     * Getter for property cbmReleationShip.
     * @return Value of property cbmReleationShip.
     */
    public com.see.truetransact.clientutil.ComboBoxModel getCbmReleationShip() {
        return cbmReleationShip;
    }
    
    /**
     * Setter for property cbmReleationShip.
     * @param cbmReleationShip New value of property cbmReleationShip.
     */
    public void setCbmReleationShip(com.see.truetransact.clientutil.ComboBoxModel cbmReleationShip) {
        this.cbmReleationShip = cbmReleationShip;
    }
    
    /**
     * Getter for property cbmBranchId.
     * @return Value of property cbmBranchId.
     */
    public com.see.truetransact.clientutil.ComboBoxModel getCbmBranchId() {
        return cbmBranchId;
    }
    
    /**
     * Setter for property cbmBranchId.
     * @param cbmBranchId New value of property cbmBranchId.
     */
    public void setCbmBranchId(com.see.truetransact.clientutil.ComboBoxModel cbmBranchId) {
        this.cbmBranchId = cbmBranchId;
    }
    
    /**
     * Getter for property cbmFsatherTitle.
     * @return Value of property cbmFsatherTitle.
     */
    public com.see.truetransact.clientutil.ComboBoxModel getCbmFsatherTitle() {
        return cbmFsatherTitle;
    }
    
    /**
     * Setter for property cbmFsatherTitle.
     * @param cbmFsatherTitle New value of property cbmFsatherTitle.
     */
    public void setCbmFsatherTitle(com.see.truetransact.clientutil.ComboBoxModel cbmFsatherTitle) {
        this.cbmFsatherTitle = cbmFsatherTitle;
    }
    
    /**
     * Getter for property cbmMotherTitle.
     * @return Value of property cbmMotherTitle.
     */
    public com.see.truetransact.clientutil.ComboBoxModel getCbmMotherTitle() {
        return cbmMotherTitle;
    }
    
    /**
     * Setter for property cbmMotherTitle.
     * @param cbmMotherTitle New value of property cbmMotherTitle.
     */
    public void setCbmMotherTitle(com.see.truetransact.clientutil.ComboBoxModel cbmMotherTitle) {
        this.cbmMotherTitle = cbmMotherTitle;
    }
    
    /**
     * Getter for property cbmReleationFHTitle.
     * @return Value of property cbmReleationFHTitle.
     */
    public com.see.truetransact.clientutil.ComboBoxModel getCbmReleationFHTitle() {
        return cbmReleationFHTitle;
    }
    
    /**
     * Setter for property cbmReleationFHTitle.
     * @param cbmReleationFHTitle New value of property cbmReleationFHTitle.
     */
    public void setCbmReleationFHTitle(com.see.truetransact.clientutil.ComboBoxModel cbmReleationFHTitle) {
        this.cbmReleationFHTitle = cbmReleationFHTitle;
    }
    
    /**
     * Getter for property cbmPFAcNominee.
     * @return Value of property cbmPFAcNominee.
     */
    public com.see.truetransact.clientutil.ComboBoxModel getCbmPFAcNominee() {
        return cbmPFAcNominee;
    }
    
    /**
     * Setter for property cbmPFAcNominee.
     * @param cbmPFAcNominee New value of property cbmPFAcNominee.
     */
    public void setCbmPFAcNominee(com.see.truetransact.clientutil.ComboBoxModel cbmPFAcNominee) {
        this.cbmPFAcNominee = cbmPFAcNominee;
    }
    
    /**
     * Getter for property cbmPassportTitle.
     * @return Value of property cbmPassportTitle.
     */
    public com.see.truetransact.clientutil.ComboBoxModel getCbmPassportTitle() {
        return cbmPassportTitle;
    }
    
    /**
     * Setter for property cbmPassportTitle.
     * @param cbmPassportTitle New value of property cbmPassportTitle.
     */
    public void setCbmPassportTitle(com.see.truetransact.clientutil.ComboBoxModel cbmPassportTitle) {
        this.cbmPassportTitle = cbmPassportTitle;
    }
    
    /**
     * Getter for property cbmTechnicalQualificationSpecilization.
     * @return Value of property cbmTechnicalQualificationSpecilization.
     */
    public com.see.truetransact.clientutil.ComboBoxModel getCbmTechnicalQualificationSpecilization() {
        return cbmTechnicalQualificationSpecilization;
    }
    
    /**
     * Setter for property cbmTechnicalQualificationSpecilization.
     * @param cbmTechnicalQualificationSpecilization New value of property cbmTechnicalQualificationSpecilization.
     */
    public void setCbmTechnicalQualificationSpecilization(com.see.truetransact.clientutil.ComboBoxModel cbmTechnicalQualificationSpecilization) {
        this.cbmTechnicalQualificationSpecilization = cbmTechnicalQualificationSpecilization;
    }
    
    /**
     * Getter for property cbmTechnicalQualificationGrade.
     * @return Value of property cbmTechnicalQualificationGrade.
     */
    public com.see.truetransact.clientutil.ComboBoxModel getCbmTechnicalQualificationGrade() {
        return cbmTechnicalQualificationGrade;
    }
    
    /**
     * Setter for property cbmTechnicalQualificationGrade.
     * @param cbmTechnicalQualificationGrade New value of property cbmTechnicalQualificationGrade.
     */
    public void setCbmTechnicalQualificationGrade(com.see.truetransact.clientutil.ComboBoxModel cbmTechnicalQualificationGrade) {
        this.cbmTechnicalQualificationGrade = cbmTechnicalQualificationGrade;
    }
    
    /**
     * Getter for property cbmFamilyMemberProf.
     * @return Value of property cbmFamilyMemberProf.
     */
    public com.see.truetransact.clientutil.ComboBoxModel getCbmFamilyMemberProf() {
        return cbmFamilyMemberProf;
    }
    
    /**
     * Setter for property cbmFamilyMemberProf.
     * @param cbmFamilyMemberProf New value of property cbmFamilyMemberProf.
     */
    public void setCbmFamilyMemberProf(com.see.truetransact.clientutil.ComboBoxModel cbmFamilyMemberProf) {
        this.cbmFamilyMemberProf = cbmFamilyMemberProf;
    }
    
    /**
     * Getter for property cbmFamilyMemEducation.
     * @return Value of property cbmFamilyMemEducation.
     */
    public com.see.truetransact.clientutil.ComboBoxModel getCbmFamilyMemEducation() {
        return cbmFamilyMemEducation;
    }
    
    /**
     * Setter for property cbmFamilyMemEducation.
     * @param cbmFamilyMemEducation New value of property cbmFamilyMemEducation.
     */
    public void setCbmFamilyMemEducation(com.see.truetransact.clientutil.ComboBoxModel cbmFamilyMemEducation) {
        this.cbmFamilyMemEducation = cbmFamilyMemEducation;
    }
    
    /**
     * Getter for property tblContDet.
     * @return Value of property tblContDet.
     */
    public com.see.truetransact.clientutil.EnhancedTableModel getTblContDet() {
        return tblContDet;
    }
    
    /**
     * Setter for property tblContDet.
     * @param tblContDet New value of property tblContDet.
     */
    public void setTblContDet(com.see.truetransact.clientutil.EnhancedTableModel tblContDet) {
        this.tblContDet = tblContDet;
        setChanged();
    }
    
    /**
     * Getter for property tblAcademicDet.
     * @return Value of property tblAcademicDet.
     */
    public com.see.truetransact.clientutil.EnhancedTableModel getTblAcademicDet() {
        return tblAcademicDet;
    }
    
    /**
     * Setter for property tblAcademicDet.
     * @param tblAcademicDet New value of property tblAcademicDet.
     */
    public void setTblAcademicDet(com.see.truetransact.clientutil.EnhancedTableModel tblAcademicDet) {
        this.tblAcademicDet = tblAcademicDet;
    }
    
    /**
     * Getter for property tblTechnicalDet.
     * @return Value of property tblTechnicalDet.
     */
    public com.see.truetransact.clientutil.EnhancedTableModel getTblTechnicalDet() {
        return tblTechnicalDet;
    }
    
    /**
     * Setter for property tblTechnicalDet.
     * @param tblTechnicalDet New value of property tblTechnicalDet.
     */
    public void setTblTechnicalDet(com.see.truetransact.clientutil.EnhancedTableModel tblTechnicalDet) {
        this.tblTechnicalDet = tblTechnicalDet;
    }
    
    /**
     * Getter for property tblLanguageDet.
     * @return Value of property tblLanguageDet.
     */
    public com.see.truetransact.clientutil.EnhancedTableModel getTblLanguageDet() {
        return tblLanguageDet;
    }
    
    /**
     * Setter for property tblLanguageDet.
     * @param tblLanguageDet New value of property tblLanguageDet.
     */
    public void setTblLanguageDet(com.see.truetransact.clientutil.EnhancedTableModel tblLanguageDet) {
        this.tblLanguageDet = tblLanguageDet;
    }
    
    /**
     * Getter for property tblFamilyDet.
     * @return Value of property tblFamilyDet.
     */
    public com.see.truetransact.clientutil.EnhancedTableModel getTblFamilyDet() {
        return tblFamilyDet;
    }
    
    /**
     * Setter for property tblFamilyDet.
     * @param tblFamilyDet New value of property tblFamilyDet.
     */
    public void setTblFamilyDet(com.see.truetransact.clientutil.EnhancedTableModel tblFamilyDet) {
        this.tblFamilyDet = tblFamilyDet;
    }
    
    /**
     * Getter for property txtEmpId.
     * @return Value of property txtEmpId.
     */
    public java.lang.String getTxtEmpId() {
        return txtEmpId;
    }
    
    /**
     * Setter for property txtEmpId.
     * @param txtEmpId New value of property txtEmpId.
     */
    public void setTxtEmpId(java.lang.String txtEmpId) {
        this.txtEmpId = txtEmpId;
    }
    
    /**
     * Getter for property tdtEmpDateOfBirth.
     * @return Value of property tdtEmpDateOfBirth.
     */
    public java.lang.String getTdtEmpDateOfBirth() {
        return tdtEmpDateOfBirth;
    }
    
    /**
     * Setter for property tdtEmpDateOfBirth.
     * @param tdtEmpDateOfBirth New value of property tdtEmpDateOfBirth.
     */
    public void setTdtEmpDateOfBirth(java.lang.String tdtEmpDateOfBirth) {
        this.tdtEmpDateOfBirth = tdtEmpDateOfBirth;
    }
    
    /**
     * Getter for property cboEmpTitle.
     * @return Value of property cboEmpTitle.
     */
    public java.lang.String getCboEmpTitle() {
        return cboEmpTitle;
    }
    
    /**
     * Setter for property cboEmpTitle.
     * @param cboEmpTitle New value of property cboEmpTitle.
     */
    public void setCboEmpTitle(java.lang.String cboEmpTitle) {
        this.cboEmpTitle = cboEmpTitle;
    }
    
    /**
     * Getter for property txtEmpFirstName.
     * @return Value of property txtEmpFirstName.
     */
    public java.lang.String getTxtEmpFirstName() {
        return txtEmpFirstName;
    }
    
    /**
     * Setter for property txtEmpFirstName.
     * @param txtEmpFirstName New value of property txtEmpFirstName.
     */
    public void setTxtEmpFirstName(java.lang.String txtEmpFirstName) {
        this.txtEmpFirstName = txtEmpFirstName;
    }
    
    /**
     * Getter for property txtEmpMiddleName.
     * @return Value of property txtEmpMiddleName.
     */
    public java.lang.String getTxtEmpMiddleName() {
        return txtEmpMiddleName;
    }
    
    /**
     * Setter for property txtEmpMiddleName.
     * @param txtEmpMiddleName New value of property txtEmpMiddleName.
     */
    public void setTxtEmpMiddleName(java.lang.String txtEmpMiddleName) {
        this.txtEmpMiddleName = txtEmpMiddleName;
    }
    
    /**
     * Getter for property txtEmpLastName.
     * @return Value of property txtEmpLastName.
     */
    public java.lang.String getTxtEmpLastName() {
        return txtEmpLastName;
    }
    
    /**
     * Setter for property txtEmpLastName.
     * @param txtEmpLastName New value of property txtEmpLastName.
     */
    public void setTxtEmpLastName(java.lang.String txtEmpLastName) {
        this.txtEmpLastName = txtEmpLastName;
    }
    
    /**
     * Getter for property txtEmpFatherFirstName.
     * @return Value of property txtEmpFatherFirstName.
     */
    public java.lang.String getTxtEmpFatherFirstName() {
        return txtEmpFatherFirstName;
    }
    
    /**
     * Setter for property txtEmpFatherFirstName.
     * @param txtEmpFatherFirstName New value of property txtEmpFatherFirstName.
     */
    public void setTxtEmpFatherFirstName(java.lang.String txtEmpFatherFirstName) {
        this.txtEmpFatherFirstName = txtEmpFatherFirstName;
    }
    
    /**
     * Getter for property txtEmpFatherMIddleName.
     * @return Value of property txtEmpFatherMIddleName.
     */
    public java.lang.String getTxtEmpFatherMIddleName() {
        return txtEmpFatherMIddleName;
    }
    
    /**
     * Setter for property txtEmpFatherMIddleName.
     * @param txtEmpFatherMIddleName New value of property txtEmpFatherMIddleName.
     */
    public void setTxtEmpFatherMIddleName(java.lang.String txtEmpFatherMIddleName) {
        this.txtEmpFatherMIddleName = txtEmpFatherMIddleName;
    }
    
    /**
     * Getter for property txtEmpFatherLasteName.
     * @return Value of property txtEmpFatherLasteName.
     */
    public java.lang.String getTxtEmpFatherLasteName() {
        return txtEmpFatherLasteName;
    }
    
    /**
     * Setter for property txtEmpFatherLasteName.
     * @param txtEmpFatherLasteName New value of property txtEmpFatherLasteName.
     */
    public void setTxtEmpFatherLasteName(java.lang.String txtEmpFatherLasteName) {
        this.txtEmpFatherLasteName = txtEmpFatherLasteName;
    }
    
    /**
     * Getter for property txtEmpMotherFirstName.
     * @return Value of property txtEmpMotherFirstName.
     */
    public java.lang.String getTxtEmpMotherFirstName() {
        return txtEmpMotherFirstName;
    }
    
    /**
     * Setter for property txtEmpMotherFirstName.
     * @param txtEmpMotherFirstName New value of property txtEmpMotherFirstName.
     */
    public void setTxtEmpMotherFirstName(java.lang.String txtEmpMotherFirstName) {
        this.txtEmpMotherFirstName = txtEmpMotherFirstName;
    }
    
    /**
     * Getter for property txtEmpMotherMIddleName.
     * @return Value of property txtEmpMotherMIddleName.
     */
    public java.lang.String getTxtEmpMotherMIddleName() {
        return txtEmpMotherMIddleName;
    }
    
    /**
     * Setter for property txtEmpMotherMIddleName.
     * @param txtEmpMotherMIddleName New value of property txtEmpMotherMIddleName.
     */
    public void setTxtEmpMotherMIddleName(java.lang.String txtEmpMotherMIddleName) {
        this.txtEmpMotherMIddleName = txtEmpMotherMIddleName;
    }
    
    /**
     * Getter for property txtEmpMotherLasteName.
     * @return Value of property txtEmpMotherLasteName.
     */
    public java.lang.String getTxtEmpMotherLasteName() {
        return txtEmpMotherLasteName;
    }
    
    /**
     * Setter for property txtEmpMotherLasteName.
     * @param txtEmpMotherLasteName New value of property txtEmpMotherLasteName.
     */
    public void setTxtEmpMotherLasteName(java.lang.String txtEmpMotherLasteName) {
        this.txtEmpMotherLasteName = txtEmpMotherLasteName;
    }
    
    /**
     * Getter for property cboEmpFatheTitle.
     * @return Value of property cboEmpFatheTitle.
     */
    public java.lang.String getCboEmpFatheTitle() {
        return cboEmpFatheTitle;
    }
    
    /**
     * Setter for property cboEmpFatheTitle.
     * @param cboEmpFatheTitle New value of property cboEmpFatheTitle.
     */
    public void setCboEmpFatheTitle(java.lang.String cboEmpFatheTitle) {
        this.cboEmpFatheTitle = cboEmpFatheTitle;
    }
    
    /**
     * Getter for property cboEmpMotherTitle.
     * @return Value of property cboEmpMotherTitle.
     */
    public java.lang.String getCboEmpMotherTitle() {
        return cboEmpMotherTitle;
    }
    
    /**
     * Setter for property cboEmpMotherTitle.
     * @param cboEmpMotherTitle New value of property cboEmpMotherTitle.
     */
    public void setCboEmpMotherTitle(java.lang.String cboEmpMotherTitle) {
        this.cboEmpMotherTitle = cboEmpMotherTitle;
    }
    
    /**
     * Getter for property txtempAge.
     * @return Value of property txtempAge.
     */
    public java.lang.String getTxtempAge() {
        return txtempAge;
    }
    
    /**
     * Setter for property txtempAge.
     * @param txtempAge New value of property txtempAge.
     */
    public void setTxtempAge(java.lang.String txtempAge) {
        this.txtempAge = txtempAge;
    }
    
    /**
     * Getter for property txtempPlaceOfBirth.
     * @return Value of property txtempPlaceOfBirth.
     */
    public java.lang.String getTxtempPlaceOfBirth() {
        return txtempPlaceOfBirth;
    }
    
    /**
     * Setter for property txtempPlaceOfBirth.
     * @param txtempPlaceOfBirth New value of property txtempPlaceOfBirth.
     */
    public void setTxtempPlaceOfBirth(java.lang.String txtempPlaceOfBirth) {
        this.txtempPlaceOfBirth = txtempPlaceOfBirth;
    }
    
    /**
     * Getter for property cboEmpReligon.
     * @return Value of property cboEmpReligon.
     */
    public java.lang.String getCboEmpReligon() {
        return cboEmpReligon;
    }
    
    /**
     * Setter for property cboEmpReligon.
     * @param cboEmpReligon New value of property cboEmpReligon.
     */
    public void setCboEmpReligon(java.lang.String cboEmpReligon) {
        this.cboEmpReligon = cboEmpReligon;
    }
    
    /**
     * Getter for property cboEmpCaste.
     * @return Value of property cboEmpCaste.
     */
    public java.lang.String getCboEmpCaste() {
        return cboEmpCaste;
    }
    
    /**
     * Setter for property cboEmpCaste.
     * @param cboEmpCaste New value of property cboEmpCaste.
     */
    public void setCboEmpCaste(java.lang.String cboEmpCaste) {
        this.cboEmpCaste = cboEmpCaste;
    }
    
    /**
     * Getter for property txtEmpHomeTown.
     * @return Value of property txtEmpHomeTown.
     */
    public java.lang.String getTxtEmpHomeTown() {
        return txtEmpHomeTown;
    }
    
    /**
     * Setter for property txtEmpHomeTown.
     * @param txtEmpHomeTown New value of property txtEmpHomeTown.
     */
    public void setTxtEmpHomeTown(java.lang.String txtEmpHomeTown) {
        this.txtEmpHomeTown = txtEmpHomeTown;
    }
    
    /**
     * Getter for property txtEmpIdCardNo.
     * @return Value of property txtEmpIdCardNo.
     */
    public java.lang.String getTxtEmpIdCardNo() {
        return txtEmpIdCardNo;
    }
    
    /**
     * Setter for property txtEmpIdCardNo.
     * @param txtEmpIdCardNo New value of property txtEmpIdCardNo.
     */
    public void setTxtEmpIdCardNo(java.lang.String txtEmpIdCardNo) {
        this.txtEmpIdCardNo = txtEmpIdCardNo;
    }
    
    /**
     * Getter for property txtEmpUIdNo.
     * @return Value of property txtEmpUIdNo.
     */
    public java.lang.String getTxtEmpUIdNo() {
        return txtEmpUIdNo;
    }
    
    /**
     * Setter for property txtEmpUIdNo.
     * @param txtEmpUIdNo New value of property txtEmpUIdNo.
     */
    public void setTxtEmpUIdNo(java.lang.String txtEmpUIdNo) {
        this.txtEmpUIdNo = txtEmpUIdNo;
    }
    
    /**
     * Getter for property txtEmpPanNo.
     * @return Value of property txtEmpPanNo.
     */
    public java.lang.String getTxtEmpPanNo() {
        return txtEmpPanNo;
    }
    
    /**
     * Setter for property txtEmpPanNo.
     * @param txtEmpPanNo New value of property txtEmpPanNo.
     */
    public void setTxtEmpPanNo(java.lang.String txtEmpPanNo) {
        this.txtEmpPanNo = txtEmpPanNo;
    }
    
    /**
     * Getter for property txtEmpPfNo.
     * @return Value of property txtEmpPfNo.
     */
    public java.lang.String getTxtEmpPfNo() {
        return txtEmpPfNo;
    }
    
    /**
     * Setter for property txtEmpPfNo.
     * @param txtEmpPfNo New value of property txtEmpPfNo.
     */
    public void setTxtEmpPfNo(java.lang.String txtEmpPfNo) {
        this.txtEmpPfNo = txtEmpPfNo;
    }
    
    /**
     * Getter for property cboEmpPfNominee.
     * @return Value of property cboEmpPfNominee.
     */
    public java.lang.String getCboEmpPfNominee() {
        return cboEmpPfNominee;
    }
    
    /**
     * Setter for property cboEmpPfNominee.
     * @param cboEmpPfNominee New value of property cboEmpPfNominee.
     */
    public void setCboEmpPfNominee(java.lang.String cboEmpPfNominee) {
        this.cboEmpPfNominee = cboEmpPfNominee;
    }
    
    /**
     * Getter for property tdtEmpJoinDate.
     * @return Value of property tdtEmpJoinDate.
     */
    public java.lang.String getTdtEmpJoinDate() {
        return tdtEmpJoinDate;
    }
    
    /**
     * Setter for property tdtEmpJoinDate.
     * @param tdtEmpJoinDate New value of property tdtEmpJoinDate.
     */
    public void setTdtEmpJoinDate(java.lang.String tdtEmpJoinDate) {
        this.tdtEmpJoinDate = tdtEmpJoinDate;
    }
    
    /**
     * Getter for property tdtEmpRetirementDate.
     * @return Value of property tdtEmpRetirementDate.
     */
    public java.lang.String getTdtEmpRetirementDate() {
        return tdtEmpRetirementDate;
    }
    
    /**
     * Setter for property tdtEmpRetirementDate.
     * @param tdtEmpRetirementDate New value of property tdtEmpRetirementDate.
     */
    public void setTdtEmpRetirementDate(java.lang.String tdtEmpRetirementDate) {
        this.tdtEmpRetirementDate = tdtEmpRetirementDate;
    }
    
    /**
     * Getter for property rdoEmpGender.
     * @return Value of property rdoEmpGender.
     */
    public java.lang.String getRdoEmpGender() {
        return rdoEmpGender;
    }
    
    /**
     * Setter for property rdoEmpGender.
     * @param rdoEmpGender New value of property rdoEmpGender.
     */
    public void setRdoEmpGender(java.lang.String rdoEmpGender) {
        this.rdoEmpGender = rdoEmpGender;
    }
    
    /**
     * Getter for property rdoMaritalStatus.
     * @return Value of property rdoMaritalStatus.
     */
    public java.lang.String getRdoMaritalStatus() {
        return rdoMaritalStatus;
    }
    
    /**
     * Setter for property rdoMaritalStatus.
     * @param rdoMaritalStatus New value of property rdoMaritalStatus.
     */
    public void setRdoMaritalStatus(java.lang.String rdoMaritalStatus) {
        this.rdoMaritalStatus = rdoMaritalStatus;
    }
    
    /**
     * Getter for property rdoFatherHusband.
     * @return Value of property rdoFatherHusband.
     */
    public java.lang.String getRdoFatherHusband() {
        return rdoFatherHusband;
    }
    
    /**
     * Setter for property rdoFatherHusband.
     * @param rdoFatherHusband New value of property rdoFatherHusband.
     */
    public void setRdoFatherHusband(java.lang.String rdoFatherHusband) {
        this.rdoFatherHusband = rdoFatherHusband;
    }
    
    /**
     * Getter for property cboEmpAddressType.
     * @return Value of property cboEmpAddressType.
     */
    public java.lang.String getCboEmpAddressType() {
        return cboEmpAddressType;
    }
    
    /**
     * Setter for property cboEmpAddressType.
     * @param cboEmpAddressType New value of property cboEmpAddressType.
     */
    public void setCboEmpAddressType(java.lang.String cboEmpAddressType) {
        this.cboEmpAddressType = cboEmpAddressType;
    }
    
    /**
     * Getter for property txtEmpStreet.
     * @return Value of property txtEmpStreet.
     */
    public java.lang.String getTxtEmpStreet() {
        return txtEmpStreet;
    }
    
    /**
     * Setter for property txtEmpStreet.
     * @param txtEmpStreet New value of property txtEmpStreet.
     */
    public void setTxtEmpStreet(java.lang.String txtEmpStreet) {
        this.txtEmpStreet = txtEmpStreet;
    }
    
    /**
     * Getter for property txtEmpArea.
     * @return Value of property txtEmpArea.
     */
    public java.lang.String getTxtEmpArea() {
        return txtEmpArea;
    }
    
    /**
     * Setter for property txtEmpArea.
     * @param txtEmpArea New value of property txtEmpArea.
     */
    public void setTxtEmpArea(java.lang.String txtEmpArea) {
        this.txtEmpArea = txtEmpArea;
    }
    
    /**
     * Getter for property cboEmpCity.
     * @return Value of property cboEmpCity.
     */
    public java.lang.String getCboEmpCity() {
        return cboEmpCity;
    }
    
    /**
     * Setter for property cboEmpCity.
     * @param cboEmpCity New value of property cboEmpCity.
     */
    public void setCboEmpCity(java.lang.String cboEmpCity) {
        this.cboEmpCity = cboEmpCity;
    }
    
    /**
     * Getter for property cboEmpState.
     * @return Value of property cboEmpState.
     */
    public java.lang.String getCboEmpState() {
        return cboEmpState;
    }
    
    /**
     * Setter for property cboEmpState.
     * @param cboEmpState New value of property cboEmpState.
     */
    public void setCboEmpState(java.lang.String cboEmpState) {
        this.cboEmpState = cboEmpState;
    }
    
    /**
     * Getter for property cboEmpCountry.
     * @return Value of property cboEmpCountry.
     */
    public java.lang.String getCboEmpCountry() {
        return cboEmpCountry;
    }
    
    /**
     * Setter for property cboEmpCountry.
     * @param cboEmpCountry New value of property cboEmpCountry.
     */
    public void setCboEmpCountry(java.lang.String cboEmpCountry) {
        this.cboEmpCountry = cboEmpCountry;
    }
    
    /**
     * Getter for property txtEmpPinNo.
     * @return Value of property txtEmpPinNo.
     */
    public java.lang.String getTxtEmpPinNo() {
        return txtEmpPinNo;
    }
    
    /**
     * Setter for property txtEmpPinNo.
     * @param txtEmpPinNo New value of property txtEmpPinNo.
     */
    public void setTxtEmpPinNo(java.lang.String txtEmpPinNo) {
        this.txtEmpPinNo = txtEmpPinNo;
    }
    
    /**
     * Getter for property txtEmpPhoneNoCountryCode.
     * @return Value of property txtEmpPhoneNoCountryCode.
     */
    public java.lang.String getTxtEmpPhoneNoCountryCode() {
        return txtEmpPhoneNoCountryCode;
    }
    
    /**
     * Setter for property txtEmpPhoneNoCountryCode.
     * @param txtEmpPhoneNoCountryCode New value of property txtEmpPhoneNoCountryCode.
     */
    public void setTxtEmpPhoneNoCountryCode(java.lang.String txtEmpPhoneNoCountryCode) {
        this.txtEmpPhoneNoCountryCode = txtEmpPhoneNoCountryCode;
    }
    
    /**
     * Getter for property txtEmpPhoneNoAreaCode.
     * @return Value of property txtEmpPhoneNoAreaCode.
     */
    public java.lang.String getTxtEmpPhoneNoAreaCode() {
        return txtEmpPhoneNoAreaCode;
    }
    
    /**
     * Setter for property txtEmpPhoneNoAreaCode.
     * @param txtEmpPhoneNoAreaCode New value of property txtEmpPhoneNoAreaCode.
     */
    public void setTxtEmpPhoneNoAreaCode(java.lang.String txtEmpPhoneNoAreaCode) {
        this.txtEmpPhoneNoAreaCode = txtEmpPhoneNoAreaCode;
    }
    
    /**
     * Getter for property txtEmpPhoneNo.
     * @return Value of property txtEmpPhoneNo.
     */
    public java.lang.String getTxtEmpPhoneNo() {
        return txtEmpPhoneNo;
    }
    
    /**
     * Setter for property txtEmpPhoneNo.
     * @param txtEmpPhoneNo New value of property txtEmpPhoneNo.
     */
    public void setTxtEmpPhoneNo(java.lang.String txtEmpPhoneNo) {
        this.txtEmpPhoneNo = txtEmpPhoneNo;
    }
    
    /**
     * Getter for property txtEmpMobileNoCountryCode.
     * @return Value of property txtEmpMobileNoCountryCode.
     */
    public java.lang.String getTxtEmpMobileNoCountryCode() {
        return txtEmpMobileNoCountryCode;
    }
    
    /**
     * Setter for property txtEmpMobileNoCountryCode.
     * @param txtEmpMobileNoCountryCode New value of property txtEmpMobileNoCountryCode.
     */
    public void setTxtEmpMobileNoCountryCode(java.lang.String txtEmpMobileNoCountryCode) {
        this.txtEmpMobileNoCountryCode = txtEmpMobileNoCountryCode;
    }
    
    /**
     * Getter for property txtEmpMobileNoAreaCode.
     * @return Value of property txtEmpMobileNoAreaCode.
     */
    public java.lang.String getTxtEmpMobileNoAreaCode() {
        return txtEmpMobileNoAreaCode;
    }
    
    /**
     * Setter for property txtEmpMobileNoAreaCode.
     * @param txtEmpMobileNoAreaCode New value of property txtEmpMobileNoAreaCode.
     */
    public void setTxtEmpMobileNoAreaCode(java.lang.String txtEmpMobileNoAreaCode) {
        this.txtEmpMobileNoAreaCode = txtEmpMobileNoAreaCode;
    }
    
    /**
     * Getter for property txtEmpMobileNo.
     * @return Value of property txtEmpMobileNo.
     */
    public java.lang.String getTxtEmpMobileNo() {
        return txtEmpMobileNo;
    }
    
    /**
     * Setter for property txtEmpMobileNo.
     * @param txtEmpMobileNo New value of property txtEmpMobileNo.
     */
    public void setTxtEmpMobileNo(java.lang.String txtEmpMobileNo) {
        this.txtEmpMobileNo = txtEmpMobileNo;
    }
    
    /**
     * Getter for property txtPassportFirstName.
     * @return Value of property txtPassportFirstName.
     */
    public java.lang.String getTxtPassportFirstName() {
        return txtPassportFirstName;
    }
    
    /**
     * Setter for property txtPassportFirstName.
     * @param txtPassportFirstName New value of property txtPassportFirstName.
     */
    public void setTxtPassportFirstName(java.lang.String txtPassportFirstName) {
        this.txtPassportFirstName = txtPassportFirstName;
    }
    
    /**
     * Getter for property txtPassportMiddleName.
     * @return Value of property txtPassportMiddleName.
     */
    public java.lang.String getTxtPassportMiddleName() {
        return txtPassportMiddleName;
    }
    
    /**
     * Setter for property txtPassportMiddleName.
     * @param txtPassportMiddleName New value of property txtPassportMiddleName.
     */
    public void setTxtPassportMiddleName(java.lang.String txtPassportMiddleName) {
        this.txtPassportMiddleName = txtPassportMiddleName;
    }
    
    /**
     * Getter for property txtPassportLastName.
     * @return Value of property txtPassportLastName.
     */
    public java.lang.String getTxtPassportLastName() {
        return txtPassportLastName;
    }
    
    /**
     * Setter for property txtPassportLastName.
     * @param txtPassportLastName New value of property txtPassportLastName.
     */
    public void setTxtPassportLastName(java.lang.String txtPassportLastName) {
        this.txtPassportLastName = txtPassportLastName;
    }
    
    /**
     * Getter for property tdtPassportIssueDt.
     * @return Value of property tdtPassportIssueDt.
     */
    public java.lang.String getTdtPassportIssueDt() {
        return tdtPassportIssueDt;
    }
    
    /**
     * Setter for property tdtPassportIssueDt.
     * @param tdtPassportIssueDt New value of property tdtPassportIssueDt.
     */
    public void setTdtPassportIssueDt(java.lang.String tdtPassportIssueDt) {
        this.tdtPassportIssueDt = tdtPassportIssueDt;
    }
    
    /**
     * Getter for property tdtPassportValidUpto.
     * @return Value of property tdtPassportValidUpto.
     */
    public java.lang.String getTdtPassportValidUpto() {
        return tdtPassportValidUpto;
    }
    
    /**
     * Setter for property tdtPassportValidUpto.
     * @param tdtPassportValidUpto New value of property tdtPassportValidUpto.
     */
    public void setTdtPassportValidUpto(java.lang.String tdtPassportValidUpto) {
        this.tdtPassportValidUpto = tdtPassportValidUpto;
    }
    
    /**
     * Getter for property txtPassportNo.
     * @return Value of property txtPassportNo.
     */
    public java.lang.String getTxtPassportNo() {
        return txtPassportNo;
    }
    
    /**
     * Setter for property txtPassportNo.
     * @param txtPassportNo New value of property txtPassportNo.
     */
    public void setTxtPassportNo(java.lang.String txtPassportNo) {
        this.txtPassportNo = txtPassportNo;
    }
    
    /**
     * Getter for property txtPassportIssueAuth.
     * @return Value of property txtPassportIssueAuth.
     */
    public java.lang.String getTxtPassportIssueAuth() {
        return txtPassportIssueAuth;
    }
    
    /**
     * Setter for property txtPassportIssueAuth.
     * @param txtPassportIssueAuth New value of property txtPassportIssueAuth.
     */
    public void setTxtPassportIssueAuth(java.lang.String txtPassportIssueAuth) {
        this.txtPassportIssueAuth = txtPassportIssueAuth;
    }
    
    /**
     * Getter for property cboPassportTitle.
     * @return Value of property cboPassportTitle.
     */
    public java.lang.String getCboPassportTitle() {
        return cboPassportTitle;
    }
    
    /**
     * Setter for property cboPassportTitle.
     * @param cboPassportTitle New value of property cboPassportTitle.
     */
    public void setCboPassportTitle(java.lang.String cboPassportTitle) {
        this.cboPassportTitle = cboPassportTitle;
    }
    
    /**
     * Getter for property cbmPassportIssuePlace.
     * @return Value of property cbmPassportIssuePlace.
     */
    public com.see.truetransact.clientutil.ComboBoxModel getCbmPassportIssuePlace() {
        return cbmPassportIssuePlace;
    }
    
    /**
     * Setter for property cbmPassportIssuePlace.
     * @param cbmPassportIssuePlace New value of property cbmPassportIssuePlace.
     */
    public void setCbmPassportIssuePlace(com.see.truetransact.clientutil.ComboBoxModel cbmPassportIssuePlace) {
        this.cbmPassportIssuePlace = cbmPassportIssuePlace;
    }
    
    /**
     * Getter for property cboPassportIssuePlace.
     * @return Value of property cboPassportIssuePlace.
     */
    public java.lang.String getCboPassportIssuePlace() {
        return cboPassportIssuePlace;
    }
    
    /**
     * Setter for property cboPassportIssuePlace.
     * @param cboPassportIssuePlace New value of property cboPassportIssuePlace.
     */
    public void setCboPassportIssuePlace(java.lang.String cboPassportIssuePlace) {
        this.cboPassportIssuePlace = cboPassportIssuePlace;
    }
    
    /**
     * Getter for property cboEmpAcademicLevel.
     * @return Value of property cboEmpAcademicLevel.
     */
    public java.lang.String getCboEmpAcademicLevel() {
        return cboEmpAcademicLevel;
    }
    
    /**
     * Setter for property cboEmpAcademicLevel.
     * @param cboEmpAcademicLevel New value of property cboEmpAcademicLevel.
     */
    public void setCboEmpAcademicLevel(java.lang.String cboEmpAcademicLevel) {
        this.cboEmpAcademicLevel = cboEmpAcademicLevel;
    }
    
    /**
     * Getter for property txtEmpAcademicSchool.
     * @return Value of property txtEmpAcademicSchool.
     */
    public java.lang.String getTxtEmpAcademicSchool() {
        return txtEmpAcademicSchool;
    }
    
    /**
     * Setter for property txtEmpAcademicSchool.
     * @param txtEmpAcademicSchool New value of property txtEmpAcademicSchool.
     */
    public void setTxtEmpAcademicSchool(java.lang.String txtEmpAcademicSchool) {
        this.txtEmpAcademicSchool = txtEmpAcademicSchool;
    }
    
    /**
     * Getter for property tdtAcademicYearOfPassing.
     * @return Value of property tdtAcademicYearOfPassing.
     */
    public java.lang.String getTdtAcademicYearOfPassing() {
        return tdtAcademicYearOfPassing;
    }
    
    /**
     * Setter for property tdtAcademicYearOfPassing.
     * @param tdtAcademicYearOfPassing New value of property tdtAcademicYearOfPassing.
     */
    public void setTdtAcademicYearOfPassing(java.lang.String tdtAcademicYearOfPassing) {
        this.tdtAcademicYearOfPassing = tdtAcademicYearOfPassing;
    }
    
    /**
     * Getter for property cboAcademicSpecialization.
     * @return Value of property cboAcademicSpecialization.
     */
    public java.lang.String getCboAcademicSpecialization() {
        return cboAcademicSpecialization;
    }
    
    /**
     * Setter for property cboAcademicSpecialization.
     * @param cboAcademicSpecialization New value of property cboAcademicSpecialization.
     */
    public void setCboAcademicSpecialization(java.lang.String cboAcademicSpecialization) {
        this.cboAcademicSpecialization = cboAcademicSpecialization;
    }
    
    /**
     * Getter for property txtAcademicUniverSity.
     * @return Value of property txtAcademicUniverSity.
     */
    public java.lang.String getTxtAcademicUniverSity() {
        return txtAcademicUniverSity;
    }
    
    /**
     * Setter for property txtAcademicUniverSity.
     * @param txtAcademicUniverSity New value of property txtAcademicUniverSity.
     */
    public void setTxtAcademicUniverSity(java.lang.String txtAcademicUniverSity) {
        this.txtAcademicUniverSity = txtAcademicUniverSity;
    }
    
    /**
     * Getter for property txtAcademicMarksScored.
     * @return Value of property txtAcademicMarksScored.
     */
    public java.lang.String getTxtAcademicMarksScored() {
        return txtAcademicMarksScored;
    }
    
    /**
     * Setter for property txtAcademicMarksScored.
     * @param txtAcademicMarksScored New value of property txtAcademicMarksScored.
     */
    public void setTxtAcademicMarksScored(java.lang.String txtAcademicMarksScored) {
        this.txtAcademicMarksScored = txtAcademicMarksScored;
    }
    
    /**
     * Getter for property txtAcademicTotalMarks.
     * @return Value of property txtAcademicTotalMarks.
     */
    public java.lang.String getTxtAcademicTotalMarks() {
        return txtAcademicTotalMarks;
    }
    
    /**
     * Setter for property txtAcademicTotalMarks.
     * @param txtAcademicTotalMarks New value of property txtAcademicTotalMarks.
     */
    public void setTxtAcademicTotalMarks(java.lang.String txtAcademicTotalMarks) {
        this.txtAcademicTotalMarks = txtAcademicTotalMarks;
    }
    
    /**
     * Getter for property txtAcademicPer.
     * @return Value of property txtAcademicPer.
     */
    public java.lang.String getTxtAcademicPer() {
        return txtAcademicPer;
    }
    
    /**
     * Setter for property txtAcademicPer.
     * @param txtAcademicPer New value of property txtAcademicPer.
     */
    public void setTxtAcademicPer(java.lang.String txtAcademicPer) {
        this.txtAcademicPer = txtAcademicPer;
    }
    
    /**
     * Getter for property cboAcademicGrade.
     * @return Value of property cboAcademicGrade.
     */
    public java.lang.String getCboAcademicGrade() {
        return cboAcademicGrade;
    }
    
    /**
     * Setter for property cboAcademicGrade.
     * @param cboAcademicGrade New value of property cboAcademicGrade.
     */
    public void setCboAcademicGrade(java.lang.String cboAcademicGrade) {
        this.cboAcademicGrade = cboAcademicGrade;
    }
    
    /**
     * Getter for property cboTechnicalLevel.
     * @return Value of property cboTechnicalLevel.
     */
    public java.lang.String getCboTechnicalLevel() {
        return cboTechnicalLevel;
    }
    
    /**
     * Setter for property cboTechnicalLevel.
     * @param cboTechnicalLevel New value of property cboTechnicalLevel.
     */
    public void setCboTechnicalLevel(java.lang.String cboTechnicalLevel) {
        this.cboTechnicalLevel = cboTechnicalLevel;
    }
    
    /**
     * Getter for property txtTechnicalSchool.
     * @return Value of property txtTechnicalSchool.
     */
    public java.lang.String getTxtTechnicalSchool() {
        return txtTechnicalSchool;
    }
    
    /**
     * Setter for property txtTechnicalSchool.
     * @param txtTechnicalSchool New value of property txtTechnicalSchool.
     */
    public void setTxtTechnicalSchool(java.lang.String txtTechnicalSchool) {
        this.txtTechnicalSchool = txtTechnicalSchool;
    }
    
    /**
     * Getter for property tdtTechnicalYearOfPassing.
     * @return Value of property tdtTechnicalYearOfPassing.
     */
    public java.lang.String getTdtTechnicalYearOfPassing() {
        return tdtTechnicalYearOfPassing;
    }
    
    /**
     * Setter for property tdtTechnicalYearOfPassing.
     * @param tdtTechnicalYearOfPassing New value of property tdtTechnicalYearOfPassing.
     */
    public void setTdtTechnicalYearOfPassing(java.lang.String tdtTechnicalYearOfPassing) {
        this.tdtTechnicalYearOfPassing = tdtTechnicalYearOfPassing;
    }
    
    /**
     * Getter for property cboTechnicalSpecialization.
     * @return Value of property cboTechnicalSpecialization.
     */
    public java.lang.String getCboTechnicalSpecialization() {
        return cboTechnicalSpecialization;
    }
    
    /**
     * Setter for property cboTechnicalSpecialization.
     * @param cboTechnicalSpecialization New value of property cboTechnicalSpecialization.
     */
    public void setCboTechnicalSpecialization(java.lang.String cboTechnicalSpecialization) {
        this.cboTechnicalSpecialization = cboTechnicalSpecialization;
    }
    
    /**
     * Getter for property txtTechnicalUniverSity.
     * @return Value of property txtTechnicalUniverSity.
     */
    public java.lang.String getTxtTechnicalUniverSity() {
        return txtTechnicalUniverSity;
    }
    
    /**
     * Setter for property txtTechnicalUniverSity.
     * @param txtTechnicalUniverSity New value of property txtTechnicalUniverSity.
     */
    public void setTxtTechnicalUniverSity(java.lang.String txtTechnicalUniverSity) {
        this.txtTechnicalUniverSity = txtTechnicalUniverSity;
    }
    
    /**
     * Getter for property txtTechnicalMarksScored.
     * @return Value of property txtTechnicalMarksScored.
     */
    public java.lang.String getTxtTechnicalMarksScored() {
        return txtTechnicalMarksScored;
    }
    
    /**
     * Setter for property txtTechnicalMarksScored.
     * @param txtTechnicalMarksScored New value of property txtTechnicalMarksScored.
     */
    public void setTxtTechnicalMarksScored(java.lang.String txtTechnicalMarksScored) {
        this.txtTechnicalMarksScored = txtTechnicalMarksScored;
    }
    
    /**
     * Getter for property txtTechnicalTotalMarks.
     * @return Value of property txtTechnicalTotalMarks.
     */
    public java.lang.String getTxtTechnicalTotalMarks() {
        return txtTechnicalTotalMarks;
    }
    
    /**
     * Setter for property txtTechnicalTotalMarks.
     * @param txtTechnicalTotalMarks New value of property txtTechnicalTotalMarks.
     */
    public void setTxtTechnicalTotalMarks(java.lang.String txtTechnicalTotalMarks) {
        this.txtTechnicalTotalMarks = txtTechnicalTotalMarks;
    }
    
    /**
     * Getter for property txtTechnicalPer.
     * @return Value of property txtTechnicalPer.
     */
    public java.lang.String getTxtTechnicalPer() {
        return txtTechnicalPer;
    }
    
    /**
     * Setter for property txtTechnicalPer.
     * @param txtTechnicalPer New value of property txtTechnicalPer.
     */
    public void setTxtTechnicalPer(java.lang.String txtTechnicalPer) {
        this.txtTechnicalPer = txtTechnicalPer;
    }
    
    /**
     * Getter for property cboTechnicalGrade.
     * @return Value of property cboTechnicalGrade.
     */
    public java.lang.String getCboTechnicalGrade() {
        return cboTechnicalGrade;
    }
    
    /**
     * Setter for property cboTechnicalGrade.
     * @param cboTechnicalGrade New value of property cboTechnicalGrade.
     */
    public void setCboTechnicalGrade(java.lang.String cboTechnicalGrade) {
        this.cboTechnicalGrade = cboTechnicalGrade;
    }
    
    /**
     * Getter for property cboLanguageType.
     * @return Value of property cboLanguageType.
     */
    public java.lang.String getCboLanguageType() {
        return cboLanguageType;
    }
    
    /**
     * Setter for property cboLanguageType.
     * @param cboLanguageType New value of property cboLanguageType.
     */
    public void setCboLanguageType(java.lang.String cboLanguageType) {
        this.cboLanguageType = cboLanguageType;
    }
    
    /**
     * Getter for property rdoWrite.
     * @return Value of property rdoWrite.
     */
    public java.lang.String getRdoWrite() {
        return rdoWrite;
    }
    
    /**
     * Setter for property rdoWrite.
     * @param rdoWrite New value of property rdoWrite.
     */
    public void setRdoWrite(java.lang.String rdoWrite) {
        this.rdoWrite = rdoWrite;
    }
    
    /**
     * Getter for property rdoRead.
     * @return Value of property rdoRead.
     */
    public java.lang.String getRdoRead() {
        return rdoRead;
    }
    
    /**
     * Setter for property rdoRead.
     * @param rdoRead New value of property rdoRead.
     */
    public void setRdoRead(java.lang.String rdoRead) {
        this.rdoRead = rdoRead;
    }
    
    /**
     * Getter for property rdoSpeak.
     * @return Value of property rdoSpeak.
     */
    public java.lang.String getRdoSpeak() {
        return rdoSpeak;
    }
    
    /**
     * Setter for property rdoSpeak.
     * @param rdoSpeak New value of property rdoSpeak.
     */
    public void setRdoSpeak(java.lang.String rdoSpeak) {
        this.rdoSpeak = rdoSpeak;
    }
    
    /**
     * Getter for property cboDepReleationShip.
     * @return Value of property cboDepReleationShip.
     */
    public java.lang.String getCboDepReleationShip() {
        return cboDepReleationShip;
    }
    
    /**
     * Setter for property cboDepReleationShip.
     * @param cboDepReleationShip New value of property cboDepReleationShip.
     */
    public void setCboDepReleationShip(java.lang.String cboDepReleationShip) {
        this.cboDepReleationShip = cboDepReleationShip;
    }
    
    /**
     * Getter for property txtEmpDepFirstName.
     * @return Value of property txtEmpDepFirstName.
     */
    public java.lang.String getTxtEmpDepFirstName() {
        return txtEmpDepFirstName;
    }
    
    /**
     * Setter for property txtEmpDepFirstName.
     * @param txtEmpDepFirstName New value of property txtEmpDepFirstName.
     */
    public void setTxtEmpDepFirstName(java.lang.String txtEmpDepFirstName) {
        this.txtEmpDepFirstName = txtEmpDepFirstName;
    }
    
    /**
     * Getter for property txtEmpDepMIddleName.
     * @return Value of property txtEmpDepMIddleName.
     */
    public java.lang.String getTxtEmpDepMIddleName() {
        return txtEmpDepMIddleName;
    }
    
    /**
     * Setter for property txtEmpDepMIddleName.
     * @param txtEmpDepMIddleName New value of property txtEmpDepMIddleName.
     */
    public void setTxtEmpDepMIddleName(java.lang.String txtEmpDepMIddleName) {
        this.txtEmpDepMIddleName = txtEmpDepMIddleName;
    }
    
    /**
     * Getter for property txtEmpDepLasteName.
     * @return Value of property txtEmpDepLasteName.
     */
    public java.lang.String getTxtEmpDepLasteName() {
        return txtEmpDepLasteName;
    }
    
    /**
     * Setter for property txtEmpDepLasteName.
     * @param txtEmpDepLasteName New value of property txtEmpDepLasteName.
     */
    public void setTxtEmpDepLasteName(java.lang.String txtEmpDepLasteName) {
        this.txtEmpDepLasteName = txtEmpDepLasteName;
    }
    
    /**
     * Getter for property cboEmpDepTitle.
     * @return Value of property cboEmpDepTitle.
     */
    public java.lang.String getCboEmpDepTitle() {
        return cboEmpDepTitle;
    }
    
    /**
     * Setter for property cboEmpDepTitle.
     * @param cboEmpDepTitle New value of property cboEmpDepTitle.
     */
    public void setCboEmpDepTitle(java.lang.String cboEmpDepTitle) {
        this.cboEmpDepTitle = cboEmpDepTitle;
    }
    
    /**
     * Getter for property tdtDepDateOfBirth.
     * @return Value of property tdtDepDateOfBirth.
     */
    public java.lang.String getTdtDepDateOfBirth() {
        return tdtDepDateOfBirth;
    }
    
    /**
     * Setter for property tdtDepDateOfBirth.
     * @param tdtDepDateOfBirth New value of property tdtDepDateOfBirth.
     */
    public void setTdtDepDateOfBirth(java.lang.String tdtDepDateOfBirth) {
        this.tdtDepDateOfBirth = tdtDepDateOfBirth;
    }
    
    /**
     * Getter for property cboDepEducation.
     * @return Value of property cboDepEducation.
     */
    public java.lang.String getCboDepEducation() {
        return cboDepEducation;
    }
    
    /**
     * Setter for property cboDepEducation.
     * @param cboDepEducation New value of property cboDepEducation.
     */
    public void setCboDepEducation(java.lang.String cboDepEducation) {
        this.cboDepEducation = cboDepEducation;
    }
    
    /**
     * Getter for property cboDepProfession.
     * @return Value of property cboDepProfession.
     */
    public java.lang.String getCboDepProfession() {
        return cboDepProfession;
    }
    
    /**
     * Setter for property cboDepProfession.
     * @param cboDepProfession New value of property cboDepProfession.
     */
    public void setCboDepProfession(java.lang.String cboDepProfession) {
        this.cboDepProfession = cboDepProfession;
    }
    
    /**
     * Getter for property rdoDepYesNo.
     * @return Value of property rdoDepYesNo.
     */
    public java.lang.String getRdoDepYesNo() {
        return rdoDepYesNo;
    }
    
    /**
     * Setter for property rdoDepYesNo.
     * @param rdoDepYesNo New value of property rdoDepYesNo.
     */
    public void setRdoDepYesNo(java.lang.String rdoDepYesNo) {
        this.rdoDepYesNo = rdoDepYesNo;
    }
    
    /**
     * Getter for property addressedChanged.
     * @return Value of property addressedChanged.
     */
    public boolean isAddressedChanged() {
        return addressedChanged;
    }
    
    /**
     * Setter for property addressedChanged.
     * @param addressedChanged New value of property addressedChanged.
     */
    public void setAddressedChanged(boolean addressedChanged) {
        this.addressedChanged = addressedChanged;
    }
    public void addAddressMap(){
        try{
            final EmployeeMasterAddressTO objEmployeeMasterAddressTO = new EmployeeMasterAddressTO();
            if( addressMap == null ){
                addressMap = new HashMap();
            }
            if( phoneMap == null ){
                phoneMap = new HashMap();
            }
            if(getActionType()==ClientConstants.ACTIONTYPE_EDIT){
                if(isNewAddress()){
                    objEmployeeMasterAddressTO.setStatus(CommonConstants.STATUS_CREATED);
                    objEmployeeMasterAddressTO.setStatusBy(TrueTransactMain.USER_ID);
                    objEmployeeMasterAddressTO.setStatusDt(curDate);
                }else{
                    objEmployeeMasterAddressTO.setStatus(CommonConstants.STATUS_MODIFIED);
                    objEmployeeMasterAddressTO.setStatusBy(TrueTransactMain.USER_ID);
                    objEmployeeMasterAddressTO.setStatusDt(curDate);
                }
            }else{
                objEmployeeMasterAddressTO.setStatus(CommonConstants.STATUS_CREATED);
            }
            if( actionType == ClientConstants.ACTIONTYPE_EDIT ){
                objEmployeeMasterAddressTO.setTxtEmpId(sysId);
            }
            objEmployeeMasterAddressTO.setTxtEmpStreet(txtEmpStreet);
            objEmployeeMasterAddressTO.setTxtEmpArea(txtEmpArea);
            objEmployeeMasterAddressTO.setCboEmpCity(CommonUtil.convertObjToStr(getCbmCity().getKeyForSelected()));
            objEmployeeMasterAddressTO.setCboEmpState(CommonUtil.convertObjToStr(getCbmState().getKeyForSelected()));
            objEmployeeMasterAddressTO.setTxtEmpPinNo(txtEmpPinNo);
            objEmployeeMasterAddressTO.setCboEmpCountry(CommonUtil.convertObjToStr(getCbmCountry().getKeyForSelected()));
            objEmployeeMasterAddressTO.setCboEmpAddressType(CommonUtil.convertObjToStr(getCbmAddressType().getKeyForSelected()));
            addressMap.put(CommonUtil.convertObjToStr(cbmAddressType.getKeyForSelected()), objEmployeeMasterAddressTO);
            if(phoneList!=null){
                phoneMap.put(CommonUtil.convertObjToStr(cbmAddressType.getKeyForSelected()),phoneList);
            }
            updateTblContactList();
            ttNotifyObservers();
        }catch(Exception e){
            parseException.logException(e,true);
        }
    }
    
    /** To populate Contact Address table */
    private void updateTblContactList()  throws Exception{
        
        Object selectedRow;
        boolean rowExists = false;
        //If row already exists update it, else create a new row & append
        for(int i = tblContDet.getRowCount(), j = 0; i > 0; i--,j++){
            selectedRow = ((ArrayList)tblContDet.getDataArrayList().get(j)).get(0);
            if( (CommonUtil.convertObjToStr(cbmAddressType.getKeyForSelected())).equals(CommonUtil.convertObjToStr(selectedRow))){
                rowExists = true;
            }
        }
        if(!rowExists){
            ArrayList addressRow = new ArrayList();
            addressRow.add(CommonUtil.convertObjToStr(cbmAddressType.getKeyForSelected()));
            addressRow.add("");
            tblContDet.insertRow(tblContDet.getRowCount(),addressRow);
            addressRow = null;
        }
    }
    
    
    public void deleteAddress(int row){
        if(deletedAddressMap == null){
            deletedAddressMap = new HashMap();
        }
        EmployeeMasterAddressTO objEmployeeMasterAddressTO = (EmployeeMasterAddressTO) addressMap.get(CommonUtil.convertObjToStr(tblContDet.getValueAt(row,ADDRTYPE_COLNO)));
        objEmployeeMasterAddressTO.setStatus(CommonConstants.STATUS_DELETED);
        objEmployeeMasterAddressTO.setStatusDt(curDate);
        objEmployeeMasterAddressTO.setStatusBy(TrueTransactMain.USER_ID);
        deletedAddressMap.put(CommonUtil.convertObjToStr(tblContDet.getValueAt(row,ADDRTYPE_COLNO)),addressMap.get(CommonUtil.convertObjToStr(tblContDet.getValueAt(row,ADDRTYPE_COLNO))));
        addressMap.remove(tblContDet.getValueAt(row,ADDRTYPE_COLNO));
        resetDeleteAddress();
    }
    
    public void setCommunicationAddress(int row){
        try{
            this.setCommAddrType( CommonUtil.convertObjToStr(tblContDet.getValueAt(row,ADDRTYPE_COLNO) ));
            final int tblContactListRowCount = tblContDet.getRowCount();
            for (int i = 0;i<tblContactListRowCount;i++){
                tblContDet.setValueAt(EMPTY_STRING,i, STATUS_COLNO);
            }
            tblContDet.setValueAt(PRIMARY,row, STATUS_COLNO);
        }catch(Exception e){
            parseException.logException(e,true);
        }
    }
    
    
    public void resetDeleteAddress(){
        try{
            resetPhoneListTable();
            resetPhoneDetails();
            resetAddressDetails();
            resetAddressListTable();
            populateContactsTable();
            ttNotifyObservers();
        }catch(Exception e){
            parseException.logException(e,true);
        }
    }
    
    public void resetAddressListTable(){
        for(int i = tblContDet.getRowCount(); i > 0; i--){
            tblContDet.removeRow(0);
        }
    }
    
    private void populateContactsTable()  throws Exception{
        final Iterator addressMapIterator = addressMap.keySet().iterator();
        ArrayList addressRow = null;
        for(int i = addressMap.size(), j = 0; i > 0; i--,j++){
            final String addressType = CommonUtil.convertObjToStr(addressMapIterator.next());
            addressRow = new ArrayList();
            addressRow.add(addressType);
            addressRow.add(EMPTY_STRING);
            tblContDet.insertRow(tblContDet.getRowCount(),addressRow);
            
            addressRow = null;
        }
        
    }
    
    public void populateAddress(int row){
        try{
            addressTypeChanged(CommonUtil.convertObjToStr(tblContDet.getValueAt(row,ADDRTYPE_COLNO)));
        }catch(Exception e){
            parseException.logException(e,true);
        }
    }
    
    
    
    public void addressTypeChanged(String selectedItem){
        try{
            addressTypeExists = true;
            final EmployeeMasterAddressTO objEmployeeMasterAddressTO = (EmployeeMasterAddressTO)addressMap.get(selectedItem);
            populateAddressData(objEmployeeMasterAddressTO);
            if(phoneMap != null){
                if (phoneMap.get(selectedItem) != null){
                    phoneList = (HashMap)phoneMap.get(selectedItem);
                }else{
                    phoneList = null;
                }
            }
            resetPhoneDetails();
            resetPhoneListTable();
            populatePhoneTable();
            ttNotifyObservers();
        }catch(Exception e){
            parseException.logException(e,true);
        }
    }
    
    /** To populate Address Data */
    private void populateAddressData(EmployeeMasterAddressTO objEmployeeMasterAddressTO)  throws Exception{
        try{
            if(objEmployeeMasterAddressTO != null){
                
                if(objEmployeeMasterAddressTO.getCboEmpAddressType() != null ){
                    getCbmAddressType().setKeyForSelected(CommonUtil.convertObjToStr(objEmployeeMasterAddressTO.getCboEmpAddressType()));
                    setCboEmpAddressType(CommonUtil.convertObjToStr(objEmployeeMasterAddressTO.getCboEmpAddressType()));
                }
                setTxtEmpStreet(objEmployeeMasterAddressTO.getTxtEmpStreet());
                setTxtEmpArea(objEmployeeMasterAddressTO.getTxtEmpArea());
                setCboEmpCity(objEmployeeMasterAddressTO.getCboEmpCity());
                setCboEmpState(objEmployeeMasterAddressTO.getCboEmpState());
                setCboEmpCountry(objEmployeeMasterAddressTO.getCboEmpCountry());
                setTxtEmpPinNo(objEmployeeMasterAddressTO.getTxtEmpPinNo());
                
            }
        }catch(Exception e){
            e.printStackTrace();
        }
    }
    public void  promotionDeailsMap(int row,boolean promotiondetailsFlag){
        try{
            final PromotionTO objPromotionTO = new PromotionTO();
            if( promotionMap == null ){
                promotionMap = new LinkedHashMap();
            }
            
            if(getActionType()==ClientConstants.ACTIONTYPE_EDIT){
                if(isNewPromotion()){
                    objPromotionTO.setStatus(CommonConstants.STATUS_CREATED);
                    objPromotionTO.setStatusBy(TrueTransactMain.USER_ID);
                    objPromotionTO.setStatusDt(curDate);
                }else{
                    objPromotionTO.setStatus(CommonConstants.STATUS_MODIFIED);
                    objPromotionTO.setStatusBy(TrueTransactMain.USER_ID);
                    objPromotionTO.setStatusDt(curDate);
                }
            }else{
                objPromotionTO.setStatus(CommonConstants.STATUS_CREATED);
            }
            if( actionType == ClientConstants.ACTIONTYPE_EDIT ){
                objPromotionTO.setEmpId(sysId);
            }
            
            
            int slno;
            slno=0;
            
            if(!promotiondetailsFlag){
                
                ArrayList data = tblPromotion.getDataArrayList();
                slno=serialNo(data,tblPromotion);
            }
            else if(isNewPromotion()){
                int b=CommonUtil.convertObjToInt(tblPromotion.getValueAt(row,0));
                slno= b + tblPromotion.getRowCount();
            }
            else{
                slno = CommonUtil.convertObjToInt(tblPromotion.getValueAt(row,0));
            }
            objPromotionTO.setTxtPromotionEmployeeName(getTxtPromotionEmployeeName());
            objPromotionTO.setLastDesignation(getTxtPromotionLastDesg());
            objPromotionTO.setTxtPromotionLastGrade(getTxtPromotionLastGrade());
            objPromotionTO.setCreatedDate(DateUtil.getDateMMDDYYYY(getTdtPromotionCreatedDateValue()));
            objPromotionTO.setEmpId(getTxtPromotionEmployeeId());
            objPromotionTO.setPresentBasic(getTxtPromotionBasicPayValue());
            objPromotionTO.setEffectiveDate(DateUtil.getDateMMDDYYYY(getTdtPromotionEffectiveDateValue()));
            objPromotionTO.setPromotionGrade(CommonUtil.convertObjToStr(getCbmPromotionGradeValue().getKeyForSelected()));
            objPromotionTO.setPromotionDesignation(CommonUtil.convertObjToStr(getCbmPromotedDesignation().getKeyForSelected()));
            objPromotionTO.setTxtRemarks(CommonUtil.convertObjToStr(getTxtRemarks()));
            objPromotionTO.setTxtNewBasic(CommonUtil.convertObjToStr(getTxtNewBasic()));
            objPromotionTO.setPromotionID(String.valueOf(slno));
            objPromotionTO.setStatusBy(TrueTransactMain.USER_ID);
            promotionMap.put(CommonUtil.convertObjToStr(String.valueOf(slno)), objPromotionTO);
            System.out.println("!@$@#$@#$@$#@promotionMap:"+promotionMap);
            updateTblPromotionList(row,objPromotionTO);
            ttNotifyObservers();
        }catch(Exception e){
            parseException.logException(e,true);
        }
        
    }
    
    
    public void  educationMap(int row,boolean academicEducationFlag){
        try{
            final EmployeeMasterEducationTO objEmployeeMasterEducationTO = new EmployeeMasterEducationTO();
            if( educationMap == null ){
                educationMap = new LinkedHashMap();
            }
            
            if(getActionType()==ClientConstants.ACTIONTYPE_EDIT){
                if(isNewEducation()){
                    objEmployeeMasterEducationTO.setStatus(CommonConstants.STATUS_CREATED);
                    objEmployeeMasterEducationTO.setStatusBy(TrueTransactMain.USER_ID);
                    objEmployeeMasterEducationTO.setStatusDt(curDate);
                }else{
                    objEmployeeMasterEducationTO.setStatus(CommonConstants.STATUS_MODIFIED);
                    objEmployeeMasterEducationTO.setStatusBy(TrueTransactMain.USER_ID);
                    objEmployeeMasterEducationTO.setStatusDt(curDate);
                }
            }else{
                objEmployeeMasterEducationTO.setStatus(CommonConstants.STATUS_CREATED);
            }
            if( actionType == ClientConstants.ACTIONTYPE_EDIT ){
                objEmployeeMasterEducationTO.setTxtEmpId(sysId);
            }
            
            
            int slno;
            slno=0;
            
            if(!academicEducationFlag){
                
                ArrayList data = tblAcademicDet.getDataArrayList();
                slno=serialNo(data,tblAcademicDet);
            }
            else if(isNewEducation()){
                int b=CommonUtil.convertObjToInt(tblAcademicDet.getValueAt(row,0));
                slno= b + tblAcademicDet.getRowCount();
            }
            else{
                slno = CommonUtil.convertObjToInt(tblAcademicDet.getValueAt(row,0));
            }
            objEmployeeMasterEducationTO.setAcademicID(String.valueOf(slno));
            objEmployeeMasterEducationTO.setCboEmpAcademicLevel(CommonUtil.convertObjToStr(getCbmLevelEducation().getDataForKey(getCboEmpAcademicLevel())));
            objEmployeeMasterEducationTO.setTxtEmpAcademicSchool(getTxtEmpAcademicSchool());
            objEmployeeMasterEducationTO.setCboAcademicSpecialization(getCboAcademicSpecialization());
            objEmployeeMasterEducationTO.setTxtAcademicUniverSity(getTxtAcademicUniverSity());
            objEmployeeMasterEducationTO.setTxtAcademicMarksScored(getTxtAcademicMarksScored());
            objEmployeeMasterEducationTO.setTxtAcademicTotalMarks(getTxtAcademicTotalMarks());
            objEmployeeMasterEducationTO.setTxtAcademicPer(getTxtAcademicPer());
            objEmployeeMasterEducationTO.setCboAcademicGrade(getCboAcademicGrade());
            objEmployeeMasterEducationTO.setStatusBy(TrueTransactMain.USER_ID);
            
            Date dateOfJoin = DateUtil.getDateMMDDYYYY(getTdtAcademicYearOfPassing());
            if(dateOfJoin != null){
                Date dJoin = (Date)curDate.clone();
                dJoin.setDate(dateOfJoin.getDate());
                dJoin.setMonth(dateOfJoin.getMonth());
                dJoin.setYear(dateOfJoin.getYear());
                objEmployeeMasterEducationTO.setTdtAcademicYearOfPassing(dJoin);
            }else{
                objEmployeeMasterEducationTO.setTdtAcademicYearOfPassing(null);
            }
            
            educationMap.put(CommonUtil.convertObjToStr(String.valueOf(slno)), objEmployeeMasterEducationTO);
            
            updateTblEducationList(row,objEmployeeMasterEducationTO);
            ttNotifyObservers();
        }catch(Exception e){
            parseException.logException(e,true);
        }
        
    }
    
    private void updateTblEducationList(int row,EmployeeMasterEducationTO objEmployeeMasterEducationTO)  throws Exception{
        Object selectedRow;
        boolean rowExists = false;
        //If row already exists update it, else create a new row & append
        for(int i = tblAcademicDet.getRowCount(), j = 0; i > 0; i--,j++){
            selectedRow = ((ArrayList)tblAcademicDet.getDataArrayList().get(j)).get(0);
            if( (CommonUtil.convertObjToStr(getAcademicID())).equals(CommonUtil.convertObjToStr(selectedRow))){
                rowExists = true;
            }
        }
        ArrayList addressRow = new ArrayList();
        if(row == -1){
            addressRow.add(CommonUtil.convertObjToStr(objEmployeeMasterEducationTO.getAcademicID()));
            addressRow.add(CommonUtil.convertObjToStr(cbmLevelEducation.getKeyForSelected()));
            addressRow.add(getTdtAcademicYearOfPassing());
            tblAcademicDet.insertRow(tblAcademicDet.getRowCount(),addressRow);
            addressRow = null;
        }
        else { 
            tblAcademicDet.removeRow(row);
            addressRow.add(CommonUtil.convertObjToStr(objEmployeeMasterEducationTO.getAcademicID()));
            addressRow.add(CommonUtil.convertObjToStr(cbmLevelEducation.getKeyForSelected()));
            addressRow.add(getTdtAcademicYearOfPassing());
            tblAcademicDet.insertRow(row,addressRow);
            addressRow = null;
        }
    }
    private void updateTblPromotionList(int row,PromotionTO objPromotionTO)  throws Exception{
        Object selectedRow;
        boolean rowExists = false;
        //If row already exists update it, else create a new row & append
        for(int i = tblPromotion.getRowCount(), j = 0; i > 0; i--,j++){
            selectedRow = ((ArrayList)tblPromotion.getDataArrayList().get(j)).get(0);
            if( (CommonUtil.convertObjToStr(getPromotionID())).equals(CommonUtil.convertObjToStr(selectedRow))){
                rowExists = true;
            }
        }
        ArrayList promotionRow = new ArrayList();
        if(row == -1){
            promotionRow.add(CommonUtil.convertObjToStr(objPromotionTO.getPromotionID()));
            promotionRow.add(CommonUtil.convertObjToStr(objPromotionTO.getTxtPromotionLastGrade()));
            promotionRow.add(CommonUtil.convertObjToStr(objPromotionTO.getLastDesignation()));
            promotionRow.add(CommonUtil.convertObjToStr(cbmPromotionGradeValue.getKeyForSelected()));
            promotionRow.add(CommonUtil.convertObjToStr(cbmPromotedDesignation.getKeyForSelected()));
            //            promotionRow.add(CommonUtil.convertObjToStr(objPromotionTO.getPromotionID()));
            tblPromotion.insertRow(tblPromotion.getRowCount(),promotionRow);
            promotionRow = null;
        }
        else {
            tblPromotion.removeRow(row);
            promotionRow.add(CommonUtil.convertObjToStr(objPromotionTO.getPromotionID()));
            promotionRow.add(CommonUtil.convertObjToStr(objPromotionTO.getTxtPromotionLastGrade()));
            promotionRow.add(CommonUtil.convertObjToStr(objPromotionTO.getLastDesignation()));
            promotionRow.add(CommonUtil.convertObjToStr(cbmPromotionGradeValue.getKeyForSelected()));
            promotionRow.add(CommonUtil.convertObjToStr(cbmPromotedDesignation.getKeyForSelected()));
            tblPromotion.insertRow(row,promotionRow);
            promotionRow = null;
        }
    }
    public void populateEducation(int row){
        try{
            educationTypeChanged(CommonUtil.convertObjToStr(tblAcademicDet.getValueAt(row,ADDRTYPE_COLNO)));
        }catch(Exception e){
            parseException.logException(e,true);
        }
    }
    public void populatePromotion(int row){
        try{
            promotionTypeChanged(CommonUtil.convertObjToStr(tblPromotion.getValueAt(row,ADDRTYPE_COLNO)));
        }catch(Exception e){
            parseException.logException(e,true);
        }
    }
    
    
    public void educationTypeChanged(String selectedItem){
        try{
            
            eductionTypeExists = true;
            
            final EmployeeMasterEducationTO objEmployeeMasterEducationTO = (EmployeeMasterEducationTO)educationMap.get(selectedItem);
            populateEducationata(objEmployeeMasterEducationTO);
            ttNotifyObservers();
        }catch(Exception e){
            parseException.logException(e,true);
        }
    }
    public void promotionTypeChanged(String selectedItem){
        try{
            promotionExists = true;
            final PromotionTO objPromotionTO = (PromotionTO)promotionMap.get(selectedItem);
            populatePromotionData(objPromotionTO);
            //            ttNotifyObservers();
        }catch(Exception e){
            parseException.logException(e,true);
        }
    }
    
    public void deleteEducation(int row){
        if(deletedEducationsMap == null){
            deletedEducationsMap = new HashMap();
        }
        EmployeeMasterEducationTO objEmployeeMasterEducationTO = (EmployeeMasterEducationTO)educationMap.get(CommonUtil.convertObjToStr(tblAcademicDet.getValueAt(row,ADDRTYPE_COLNO)));
        objEmployeeMasterEducationTO.setStatus(CommonConstants.STATUS_DELETED);
        objEmployeeMasterEducationTO.setStatusDt(curDate);
        objEmployeeMasterEducationTO.setStatusBy(TrueTransactMain.USER_ID);
        deletedEducationsMap.put(CommonUtil.convertObjToStr(tblAcademicDet.getValueAt(row,ADDRTYPE_COLNO)),educationMap.get(CommonUtil.convertObjToStr(tblAcademicDet.getValueAt(row,ADDRTYPE_COLNO))));
        educationMap.remove(tblAcademicDet.getValueAt(row,ADDRTYPE_COLNO));
        resetDeleteEdecution();
    }
    public void deletePromotionDeatails(int row){
        if(deletedPromotionMap == null){
            deletedPromotionMap = new HashMap();
        }
        PromotionTO objPromotionTO = (PromotionTO)promotionMap.get(CommonUtil.convertObjToStr(tblPromotion.getValueAt(row,ADDRTYPE_COLNO)));
        objPromotionTO.setStatus(CommonConstants.STATUS_DELETED);
        objPromotionTO.setStatusDt(curDate);
        objPromotionTO.setStatusBy(TrueTransactMain.USER_ID);
        deletedPromotionMap.put(CommonUtil.convertObjToStr(tblPromotion.getValueAt(row,ADDRTYPE_COLNO)),promotionMap.get(CommonUtil.convertObjToStr(tblPromotion.getValueAt(row,ADDRTYPE_COLNO))));
        promotionMap.remove(tblPromotion.getValueAt(row,ADDRTYPE_COLNO));
        resetDeletePromotion();
    }
    
    
    public void resetEducationListTable(){
        for(int i = tblAcademicDet.getRowCount(); i > 0; i--){
            tblAcademicDet.removeRow(0);
        }
    }
    public void resetPromotionListTable(){
        for(int i = tblPromotion.getRowCount(); i > 0; i--){
            tblPromotion.removeRow(0);
        }
    }
    
    
    private void populateEducationTable()  throws Exception{
        //added from here
        ArrayList academicDataLst = new ArrayList();
        academicDataLst = new ArrayList(educationMap.keySet());
        ArrayList addList =new ArrayList(educationMap.keySet());
        int length = academicDataLst.size();
        for(int i=0; i<length; i++){
            ArrayList acadTabRow = new ArrayList();
            EmployeeMasterEducationTO objEmployeeMasterEducationTO = (EmployeeMasterEducationTO) educationMap.get(addList.get(i));
            acadTabRow = new ArrayList();
            acadTabRow.add(CommonUtil.convertObjToStr(objEmployeeMasterEducationTO.getAcademicID()));
            acadTabRow.add(CommonUtil.convertObjToStr(objEmployeeMasterEducationTO.getCboEmpAcademicLevel()));
            acadTabRow.add(CommonUtil.convertObjToStr(objEmployeeMasterEducationTO.getTdtAcademicYearOfPassing()));
            tblAcademicDet.insertRow(tblAcademicDet.getRowCount(),acadTabRow);
            
        }
        
    }
    private void populatePromotionTable()  throws Exception{
        //added from here
        ArrayList promotionDetailsLst = new ArrayList();
        promotionDetailsLst = new ArrayList(promotionMap.keySet());
        ArrayList addList =new ArrayList(promotionMap.keySet());
        int length = promotionDetailsLst.size();
        for(int i=0; i<length; i++){
            ArrayList promotionTabRow = new ArrayList();
            PromotionTO objPromotionTO = (PromotionTO) promotionMap.get(addList.get(i));
            promotionTabRow = new ArrayList();
            promotionTabRow.add(CommonUtil.convertObjToStr(objPromotionTO.getPromotionID()));
            promotionTabRow.add(CommonUtil.convertObjToStr(objPromotionTO.getTxtPromotionLastGrade()));
            promotionTabRow.add(CommonUtil.convertObjToStr(objPromotionTO.getLastDesignation()));
            promotionTabRow.add(CommonUtil.convertObjToStr(objPromotionTO.getPromotionGrade()));
            promotionTabRow.add(CommonUtil.convertObjToStr(objPromotionTO.getPromotionDesignation()));
            //            promotionTabRow.add(CommonUtil.convertObjToStr(objPromotionTO.get
            tblPromotion.insertRow(tblPromotion.getRowCount(),promotionTabRow);
            
        }
        
    }
    public void resetDeleteEdecution(){
        try{
            
            resetAcademic();
            resetEducationListTable();
            populateEducationTable();
            ttNotifyObservers();
        }catch(Exception e){
            parseException.logException(e,true);
        }
    }
    public void resetDeletePromotion(){
        try{
            resetPromotion();
            resetPromotionListTable();
            populatePromotionTable();
            ttNotifyObservers();
        }catch(Exception e){
            parseException.logException(e,true);
        }
    }
    
    /** To populate Address Data */
    private void populateEducationata(EmployeeMasterEducationTO objEmployeeMasterEducationTO)  throws Exception{
        try{
            if(objEmployeeMasterEducationTO != null){
                setAcademicID(objEmployeeMasterEducationTO.getAcademicID());
                setCboEmpAcademicLevel(objEmployeeMasterEducationTO.getCboEmpAcademicLevel());
                setTxtEmpAcademicSchool(objEmployeeMasterEducationTO.getTxtEmpAcademicSchool());
                setTdtAcademicYearOfPassing(DateUtil.getStringDate(objEmployeeMasterEducationTO.getTdtAcademicYearOfPassing()));
                setCboAcademicSpecialization(objEmployeeMasterEducationTO.getCboAcademicSpecialization());
                setTxtAcademicUniverSity(objEmployeeMasterEducationTO.getTxtAcademicUniverSity());
                setTxtAcademicMarksScored(objEmployeeMasterEducationTO.getTxtAcademicMarksScored());
                setTxtAcademicTotalMarks(objEmployeeMasterEducationTO.getTxtAcademicTotalMarks());
                setTxtAcademicPer(objEmployeeMasterEducationTO.getTxtAcademicPer());
                setCboAcademicGrade(objEmployeeMasterEducationTO.getCboAcademicGrade());
            }
        }catch(Exception e){
            e.printStackTrace();
        }
    }
    private void populatePromotionData(PromotionTO objPromotionTO)  throws Exception{
        try{
            if(objPromotionTO != null){
                setTxtPromotionEmployeeId(objPromotionTO.getEmpId());
                setTxtPromotionLastGrade(objPromotionTO.getTxtPromotionLastGrade());
                setTxtPromotionLastDesg(objPromotionTO.getLastDesignation());
                setTxtPromotionEmployeeName(objPromotionTO.getTxtPromotionEmployeeName());
                setTxtPromotionEmpBranch(objPromotionTO.getTxtPromotionEmpBranch());
                setPromotionID(objPromotionTO.getPromotionID());
                setTdtPromotionEffectiveDateValue(CommonUtil.convertObjToStr(objPromotionTO.getEffectiveDate()));
                setTdtPromotionCreatedDateValue(CommonUtil.convertObjToStr(objPromotionTO.getCreatedDate()));
                setTxtPromotionBasicPayValue(objPromotionTO.getPresentBasic());
                setCboPromotedDesignation(objPromotionTO.getPromotionDesignation());
                setcboPromotionGradeValue(objPromotionTO.getPromotionGrade());
                setTxtNewBasic(objPromotionTO.getTxtNewBasic());
                setTxtRemarks(objPromotionTO.getTxtRemarks());
            }
        }catch(Exception e){
            e.printStackTrace();
        }
    }
    
    public void  technicalMap(int row,boolean technicalEducationFlag){
        try{
            final EmployeeMasterTechnicalTO objEmployeeMasterTechnicalTO= new EmployeeMasterTechnicalTO();
            if( technicalMap == null ){
                technicalMap = new LinkedHashMap();
            }
            
            if(getActionType()==ClientConstants.ACTIONTYPE_EDIT){
                if(isNewTechnical()){
                    objEmployeeMasterTechnicalTO.setStatus(CommonConstants.STATUS_CREATED);
                    objEmployeeMasterTechnicalTO.setStatusBy(TrueTransactMain.USER_ID);
                    objEmployeeMasterTechnicalTO.setStatusDt(curDate);
                }else{
                    objEmployeeMasterTechnicalTO.setStatus(CommonConstants.STATUS_MODIFIED);
                    objEmployeeMasterTechnicalTO.setStatusBy(TrueTransactMain.USER_ID);
                    objEmployeeMasterTechnicalTO.setStatusDt(curDate);
                }
            }else{
                objEmployeeMasterTechnicalTO.setStatus(CommonConstants.STATUS_CREATED);
            }
            if( actionType == ClientConstants.ACTIONTYPE_EDIT ){
                objEmployeeMasterTechnicalTO.setTxtEmpId(sysId);
            }
            
            int slno;
            slno=0;
            
            if(!technicalEducationFlag){
                
                ArrayList data = tblTechnicalDet.getDataArrayList();
                slno=serialNo(data,tblTechnicalDet);
            }
            else if(isNewTechnical()){
                int b=CommonUtil.convertObjToInt(tblTechnicalDet.getValueAt(row,0));
                slno= b + tblTechnicalDet.getRowCount();
            }
            else{
                slno = CommonUtil.convertObjToInt(tblTechnicalDet.getValueAt(row,0));
            }
            
            
            objEmployeeMasterTechnicalTO.setTechnicalID(String.valueOf(slno));
            objEmployeeMasterTechnicalTO.setCboTechnicalLevel(CommonUtil.convertObjToStr(getCbmTechnicalQualification().getDataForKey(CommonUtil.convertObjToStr(getCboTechnicalLevel()))));
            objEmployeeMasterTechnicalTO.setTxtTechnicalSchool(getTxtTechnicalSchool());
            
            objEmployeeMasterTechnicalTO.setCboTechnicalSpecialization(getCboTechnicalSpecialization());
            objEmployeeMasterTechnicalTO.setTxtTechnicalUniverSity(getTxtTechnicalUniverSity());
            objEmployeeMasterTechnicalTO.setTxtTechnicalMarksScored(getTxtTechnicalMarksScored());
            objEmployeeMasterTechnicalTO.setTxtTechnicalTotalMarks(getTxtTechnicalTotalMarks());
            objEmployeeMasterTechnicalTO.setTxtTechnicalPer(getTxtTechnicalPer());
            objEmployeeMasterTechnicalTO.setCboTechnicalGrade(getCboTechnicalGrade());
            objEmployeeMasterTechnicalTO.setStatusBy(TrueTransactMain.USER_ID);
            
            Date dateOfJoin = DateUtil.getDateMMDDYYYY(getTdtTechnicalYearOfPassing());
            if(dateOfJoin != null){
                Date dJoin = (Date)curDate.clone();
                dJoin.setDate(dateOfJoin.getDate());
                dJoin.setMonth(dateOfJoin.getMonth());
                dJoin.setYear(dateOfJoin.getYear());
                objEmployeeMasterTechnicalTO.setTdtTechnicalYearOfPassing(dJoin);
            }else{
                objEmployeeMasterTechnicalTO.setTdtTechnicalYearOfPassing(DateUtil.getDateMMDDYYYY(getTdtTechnicalYearOfPassing()));
            }
            technicalMap.put(CommonUtil.convertObjToStr(String.valueOf(slno)), objEmployeeMasterTechnicalTO);
            
            updateTblTechnicalList(row,objEmployeeMasterTechnicalTO);
            ttNotifyObservers();
        }catch(Exception e){
            parseException.logException(e,true);
        }
        
    }
    
    private void updateTblTechnicalList(int row,EmployeeMasterTechnicalTO objEmployeeMasterTechnicalTO)  throws Exception{
        Object selectedRow;
        boolean rowExists = false;
        //If row already exists update it, else create a new row & append
        for(int i = tblTechnicalDet.getRowCount(), j = 0; i > 0; i--,j++){
            selectedRow = ((ArrayList)tblTechnicalDet.getDataArrayList().get(j)).get(0);
            if( (CommonUtil.convertObjToStr(getTechnicalID())).equals(CommonUtil.convertObjToStr(selectedRow))){
                rowExists = true;
            }
        }
        ArrayList addressRow = new ArrayList();
        if(row == -1){
            addressRow.add(CommonUtil.convertObjToStr(objEmployeeMasterTechnicalTO.getTechnicalID()));
            addressRow.add(CommonUtil.convertObjToStr(cbmTechnicalQualification.getKeyForSelected()));
            addressRow.add(getTdtTechnicalYearOfPassing());
            tblTechnicalDet.insertRow(tblTechnicalDet.getRowCount(),addressRow);
            addressRow = null;
        }
        else{
            tblTechnicalDet.removeRow(row);
            addressRow.add(CommonUtil.convertObjToStr(objEmployeeMasterTechnicalTO.getTechnicalID()));
            addressRow.add(CommonUtil.convertObjToStr(cbmTechnicalQualification.getKeyForSelected()));
            addressRow.add(getTdtTechnicalYearOfPassing());
            tblTechnicalDet.insertRow(tblTechnicalDet.getRowCount(),addressRow);
            addressRow = null;
        }
    }
    public void populateTechnical(int row){
        try{
            technicalTypeChanged(CommonUtil.convertObjToStr(tblTechnicalDet.getValueAt(row,ADDRTYPE_COLNO)));
        }catch(Exception e){
            parseException.logException(e,true);
        }
    }
    
    public void technicalTypeChanged(String selectedItem){
        try{
            
            technicalTypeExists = true;
            
            final EmployeeMasterTechnicalTO objEmployeeMasterTechnicalTO = (EmployeeMasterTechnicalTO)technicalMap.get(selectedItem);
            populateTechnicalDate(objEmployeeMasterTechnicalTO);
            ttNotifyObservers();
        }catch(Exception e){
            parseException.logException(e,true);
        }
    }
    
    public void deleteTechnical(int row){
        if(deletedTechnicalMap == null){
            deletedTechnicalMap = new HashMap();
        }
        EmployeeMasterTechnicalTO objEmployeeMasterTechnicalTO = (EmployeeMasterTechnicalTO)technicalMap.get(CommonUtil.convertObjToStr(tblTechnicalDet.getValueAt(row,ADDRTYPE_COLNO)));
        objEmployeeMasterTechnicalTO.setStatus(CommonConstants.STATUS_DELETED);
        objEmployeeMasterTechnicalTO.setStatusDt(curDate);
        objEmployeeMasterTechnicalTO.setStatusBy(TrueTransactMain.USER_ID);
        deletedTechnicalMap.put(CommonUtil.convertObjToStr(tblTechnicalDet.getValueAt(row,ADDRTYPE_COLNO)),technicalMap.get(CommonUtil.convertObjToStr(tblTechnicalDet.getValueAt(row,ADDRTYPE_COLNO))));
        technicalMap.remove(tblTechnicalDet.getValueAt(row,ADDRTYPE_COLNO));
        resetDeleteTechnical();
    }
    
    
    
    public void resetTechnicalListTable(){
        for(int i = tblTechnicalDet.getRowCount(); i > 0; i--){
            tblTechnicalDet.removeRow(0);
        }
    }
    
    
    private void populateTechnicalTable()  throws Exception{
        
        //added from here
        ArrayList technicalDataLst = new ArrayList();
        technicalDataLst = new ArrayList(technicalMap.keySet());
        ArrayList addList =new ArrayList(technicalMap.keySet());
        int length = technicalDataLst.size();
        for(int i=0; i<length; i++){
            ArrayList technicalTabRow = new ArrayList();
            EmployeeMasterTechnicalTO objEmployeeMasterTechnicalTO = (EmployeeMasterTechnicalTO) technicalMap.get(addList.get(i));
            technicalTabRow = new ArrayList();
            technicalTabRow.add(CommonUtil.convertObjToStr(objEmployeeMasterTechnicalTO.getTechnicalID()));
            technicalTabRow.add(CommonUtil.convertObjToStr(objEmployeeMasterTechnicalTO.getCboTechnicalLevel()));
            technicalTabRow.add(CommonUtil.convertObjToStr(objEmployeeMasterTechnicalTO.getTdtTechnicalYearOfPassing()));
            tblTechnicalDet.insertRow(tblTechnicalDet.getRowCount(),technicalTabRow);
            
        }
    }
    
    public void resetDeleteTechnical(){
        try{
            
            resetTechnical();
            resetTechnicalListTable();
            populateTechnicalTable();
            ttNotifyObservers();
        }catch(Exception e){
            parseException.logException(e,true);
        }
    }
    
    /** To populate Address Data */
    private void populateTechnicalDate(EmployeeMasterTechnicalTO objEmployeeMasterTechnicalTO)  throws Exception{
        try{
            if(objEmployeeMasterTechnicalTO != null){
                setTechnicalID(objEmployeeMasterTechnicalTO.getTechnicalID());
                setCboTechnicalLevel(objEmployeeMasterTechnicalTO.getCboTechnicalLevel());
                setTxtTechnicalSchool(objEmployeeMasterTechnicalTO.getTxtTechnicalSchool());
                setTdtTechnicalYearOfPassing(DateUtil.getStringDate(objEmployeeMasterTechnicalTO.getTdtTechnicalYearOfPassing()));
                setCboTechnicalSpecialization(objEmployeeMasterTechnicalTO.getCboTechnicalSpecialization());
                setTxtTechnicalUniverSity(objEmployeeMasterTechnicalTO.getTxtTechnicalUniverSity());
                setTxtTechnicalMarksScored(objEmployeeMasterTechnicalTO.getTxtTechnicalMarksScored());
                setTxtTechnicalTotalMarks(objEmployeeMasterTechnicalTO.getTxtTechnicalTotalMarks());
                setTxtTechnicalPer(objEmployeeMasterTechnicalTO.getTxtTechnicalPer());
                setCboTechnicalGrade(objEmployeeMasterTechnicalTO.getCboTechnicalGrade());
            }
        }catch(Exception e){
            e.printStackTrace();
        }
    }
    
    
    
    public void  languageMap(){
        try{
            final EmployeeMasterLanguageTO objEmployeeMasterLanguageTO =new EmployeeMasterLanguageTO();
            if( laungeMap == null ){
                laungeMap = new HashMap();
            }
            
            if(getActionType()==ClientConstants.ACTIONTYPE_EDIT){
                if(isNewLanguage()){
                    objEmployeeMasterLanguageTO.setStatus(CommonConstants.STATUS_CREATED);
                    objEmployeeMasterLanguageTO.setStatusBy(TrueTransactMain.USER_ID);
                    objEmployeeMasterLanguageTO.setStatusDt(curDate);
                }else{
                    objEmployeeMasterLanguageTO.setStatus(CommonConstants.STATUS_MODIFIED);
                    objEmployeeMasterLanguageTO.setStatusBy(TrueTransactMain.USER_ID);
                    objEmployeeMasterLanguageTO.setStatusDt(curDate);
                }
            }else{
                objEmployeeMasterLanguageTO.setStatus(CommonConstants.STATUS_CREATED);
            }
            if( actionType == ClientConstants.ACTIONTYPE_EDIT ){
                objEmployeeMasterLanguageTO.setTxtEmpId(sysId);
            }
            objEmployeeMasterLanguageTO.setCboLanguageType(getCboLanguageType());
            objEmployeeMasterLanguageTO.setRdoRead(getRdoRead());
            objEmployeeMasterLanguageTO.setRdoSpeak(getRdoSpeak());
            objEmployeeMasterLanguageTO.setRdoWrite(getRdoWrite());
            objEmployeeMasterLanguageTO.setStatusBy(TrueTransactMain.USER_ID);
            
            laungeMap.put(CommonUtil.convertObjToStr(getCboLanguageType()), objEmployeeMasterLanguageTO);
            
            updateTblLanguageList();
            ttNotifyObservers();
        }catch(Exception e){
            parseException.logException(e,true);
        }
        
    }
    
    private void updateTblLanguageList()  throws Exception{
        Object selectedRow;
        boolean rowExists = false;
        //If row already exists update it, else create a new row & append
        for(int i = tblLanguageDet.getRowCount(), j = 0; i > 0; i--,j++){
            selectedRow = ((ArrayList)tblLanguageDet.getDataArrayList().get(j)).get(0);
            if( (CommonUtil.convertObjToStr(cbmLanguage.getKeyForSelected())).equals(CommonUtil.convertObjToStr(selectedRow))){
                rowExists = true;
            }
        }
        if(!rowExists){
            ArrayList addressRow = new ArrayList();
            addressRow.add(CommonUtil.convertObjToStr(cbmLanguage.getKeyForSelected()));
            tblLanguageDet.insertRow(tblLanguageDet.getRowCount(),addressRow);
            addressRow = null;
        }
    }
    public void populateLanguage(int row){
        try{
            languageTypeChanged(CommonUtil.convertObjToStr(tblLanguageDet.getValueAt(row,ADDRTYPE_COLNO)));
        }catch(Exception e){
            parseException.logException(e,true);
        }
    }
    
    
    
    public void languageTypeChanged(String selectedItem){
        try{
            
            technicalTypeExists = true;
            
            final EmployeeMasterLanguageTO objEmployeeMasterLanguageTO = (EmployeeMasterLanguageTO)laungeMap.get(selectedItem);
            populateLanguageDate(objEmployeeMasterLanguageTO);
            ttNotifyObservers();
        }catch(Exception e){
            parseException.logException(e,true);
        }
    }
    
    public void deleteLanguage(int row){
        if(deletedTechnicalMap == null){
            deletedTechnicalMap = new HashMap();
        }
        EmployeeMasterLanguageTO objEmployeeMasterLanguageTO = (EmployeeMasterLanguageTO)laungeMap.get(CommonUtil.convertObjToStr(tblLanguageDet.getValueAt(row,ADDRTYPE_COLNO)));
        objEmployeeMasterLanguageTO.setStatus(CommonConstants.STATUS_DELETED);
        objEmployeeMasterLanguageTO.setStatusDt(curDate);
        objEmployeeMasterLanguageTO.setStatusBy(TrueTransactMain.USER_ID);
        deletedLanguageMap.put(CommonUtil.convertObjToStr(tblLanguageDet.getValueAt(row,ADDRTYPE_COLNO)),laungeMap.get(CommonUtil.convertObjToStr(tblLanguageDet.getValueAt(row,ADDRTYPE_COLNO))));
        laungeMap.remove(tblLanguageDet.getValueAt(row,ADDRTYPE_COLNO));
        resetDeleteLanguage();
    }
    public void resetLanguageListTable(){
        for(int i = tblLanguageDet.getRowCount(); i > 0; i--){
            tblLanguageDet.removeRow(0);
        }
    }
    
    
    private void populateLanguageTable()  throws Exception{
        final Iterator addressMapIterator = laungeMap.keySet().iterator();
        ArrayList addressRow = null;
        for(int i = laungeMap.size(), j = 0; i > 0; i--,j++){
            final String addressType = CommonUtil.convertObjToStr(addressMapIterator.next());
            addressRow = new ArrayList();
            addressRow.add(addressType);
            addressRow.add(EMPTY_STRING);
            tblLanguageDet.insertRow(tblLanguageDet.getRowCount(),addressRow);
            
            addressRow = null;
        }
        
    }
    public void resetDeleteLanguage(){
        try{
            
            resetLanguage();
            resetLanguageListTable();
            populateLanguageTable();
            ttNotifyObservers();
        }catch(Exception e){
            parseException.logException(e,true);
        }
    }
    
    /** To populate Address Data */
    private void populateLanguageDate(EmployeeMasterLanguageTO objEmployeeMasterLanguageTO)  throws Exception{
        try{
            if(objEmployeeMasterLanguageTO != null){
                setCboLanguageType(objEmployeeMasterLanguageTO.getCboLanguageType());
                setRdoSpeak(objEmployeeMasterLanguageTO.getRdoSpeak());
                setRdoWrite(objEmployeeMasterLanguageTO.getRdoWrite());
                setRdoRead(objEmployeeMasterLanguageTO.getRdoRead());
            }
        }catch(Exception e){
            e.printStackTrace();
        }
    }
    
    
    public void  dependent(int row,boolean familyDetailsFlag){
        try{
            final EmployeeMasterDependendTO objEmployeeMasterDependendTO=new EmployeeMasterDependendTO();
            if( dependentMap == null ){
                dependentMap = new LinkedHashMap();
            }
            
            if(getActionType()==ClientConstants.ACTIONTYPE_EDIT){
                if(isNewDependent()){
                    objEmployeeMasterDependendTO.setStatus(CommonConstants.STATUS_CREATED);
                    objEmployeeMasterDependendTO.setStatusBy(TrueTransactMain.USER_ID);
                    objEmployeeMasterDependendTO.setStatusDt(curDate);
                }else{
                    objEmployeeMasterDependendTO.setStatus(CommonConstants.STATUS_MODIFIED);
                    objEmployeeMasterDependendTO.setStatusBy(TrueTransactMain.USER_ID);
                    objEmployeeMasterDependendTO.setStatusDt(curDate);
                }
            }else{
                objEmployeeMasterDependendTO.setStatus(CommonConstants.STATUS_CREATED);
            }
            if( actionType == ClientConstants.ACTIONTYPE_EDIT ){
                objEmployeeMasterDependendTO.setTxtEmpId(sysId);
            }
            
            int slno;
            slno=0;
            
            if(!familyDetailsFlag){
                ArrayList data = tblFamilyDet.getDataArrayList();
                slno=serialNo(data,tblFamilyDet);
            }
            else if(isNewDependent()){
                int b=CommonUtil.convertObjToInt(tblFamilyDet.getValueAt(row,0));
                slno= b + tblFamilyDet.getRowCount();
            }
            else{
                slno = CommonUtil.convertObjToInt(tblFamilyDet.getValueAt(row,0));
            }
            
            
            objEmployeeMasterDependendTO.setDependentId(String.valueOf(slno));
            objEmployeeMasterDependendTO.setCboDepReleationShip(getCboDepReleationShip());
            objEmployeeMasterDependendTO.setCboEmpDepTitle(getCboEmpDepTitle());
            objEmployeeMasterDependendTO.setTxtEmpDepFirstName(getTxtEmpDepFirstName());
            objEmployeeMasterDependendTO.setTxtEmpDepMIddleName(getTxtEmpDepMIddleName());
            objEmployeeMasterDependendTO.setTxtEmpDepLasteName(getTxtEmpDepLasteName());
            objEmployeeMasterDependendTO.setCboDepEducation(getCboDepEducation());
            objEmployeeMasterDependendTO.setCboDepProfession(getCboDepProfession());
            objEmployeeMasterDependendTO.setTxtDepIncomePerannum(getTxtDepIncomePerannum());
            objEmployeeMasterDependendTO.setTxtEmpWith(getTxtEmpWith());
            Date dateOfJoin = DateUtil.getDateMMDDYYYY(getTdtDepDateOfBirth());
            if(dateOfJoin != null){
                Date dJoin = (Date)curDate.clone();
                dJoin.setDate(dateOfJoin.getDate());
                dJoin.setMonth(dateOfJoin.getMonth());
                dJoin.setYear(dateOfJoin.getYear());
                objEmployeeMasterDependendTO.setTdtDepDateOfBirth(dJoin);
            }else{
                objEmployeeMasterDependendTO.setTdtDepDateOfBirth(DateUtil.getDateMMDDYYYY(getTdtDepDateOfBirth()));
            }
            
            objEmployeeMasterDependendTO.setRdoDepYesNo(getRdoDepYesNo());
            objEmployeeMasterDependendTO.setRdoLiableYesNo(getRdoLiableYesNo());
            objEmployeeMasterDependendTO.setStatusBy(TrueTransactMain.USER_ID);
            dependentMap.put(CommonUtil.convertObjToStr(String.valueOf(slno)), objEmployeeMasterDependendTO);
            
            
            updateTblDependentList(row,objEmployeeMasterDependendTO);
            ttNotifyObservers();
        }catch(Exception e){
            parseException.logException(e,true);
        }
        
    }
    
    public int serialNo(ArrayList data, EnhancedTableModel table_name){
        final int dataSize = data.size();
        int nums[]= new int[150];
        int max=nums[0];
        int slno=0;
        int a=0;
        slno=dataSize+1;
        for(int i=0;i<data.size();i++){
            a=CommonUtil.convertObjToInt(table_name.getValueAt(i,0));
            nums[i]=a;
            if(nums[i]>max)
                max=nums[i];
            slno=max+1;
        }
        return slno;
    }
    
    public void  relative(int row){
        try{
            final EmployeeRelativeWorkingTO objEmployeeRelativeWorkingTO=new EmployeeRelativeWorkingTO();
            if( relativeMap == null ){
                relativeMap = new HashMap();
            }
            
            if(getActionType()==ClientConstants.ACTIONTYPE_EDIT){
                if(isNewRelativesWorking()){
                    objEmployeeRelativeWorkingTO.setStatus(CommonConstants.STATUS_CREATED);
                    objEmployeeRelativeWorkingTO.setStatusBy(TrueTransactMain.USER_ID);
                    objEmployeeRelativeWorkingTO.setStatusDt(curDate);
                }else{
                    objEmployeeRelativeWorkingTO.setStatus(CommonConstants.STATUS_MODIFIED);
                    objEmployeeRelativeWorkingTO.setStatusBy(TrueTransactMain.USER_ID);
                    objEmployeeRelativeWorkingTO.setStatusDt(curDate);
                }
            }else{
                objEmployeeRelativeWorkingTO.setStatus(CommonConstants.STATUS_CREATED);
            }
            if( actionType == ClientConstants.ACTIONTYPE_EDIT ){
                objEmployeeRelativeWorkingTO.setSysId(sysId);
            }
            objEmployeeRelativeWorkingTO.setRelativeSysId(getReleativeSysId());
            objEmployeeRelativeWorkingTO.setStaffId(getTxtRelativeStaffId());
            objEmployeeRelativeWorkingTO.setRelativeTittle(getCboRelativeTittle());
            objEmployeeRelativeWorkingTO.setRelativeFirstName(getTxtReleativeFirstName());
            objEmployeeRelativeWorkingTO.setRelativeMiddleName(getTxtReleativeMiddleName());
            objEmployeeRelativeWorkingTO.setRelativerelationShip(getCboReleativeReleationShip());
            objEmployeeRelativeWorkingTO.setRelativeLastName(getTxtReleativeLastName());
            objEmployeeRelativeWorkingTO.setRelativeWorkingBranch(getCboReleativeBranchId());
            objEmployeeRelativeWorkingTO.setRelativeDisgnantion(getCboReleativeDisg());
            objEmployeeRelativeWorkingTO.setStatusBy(TrueTransactMain.USER_ID);
            relativeMap.put(CommonUtil.convertObjToStr(getTxtRelativeStaffId()), objEmployeeRelativeWorkingTO);
            updateTblReleativeList(row);
            ttNotifyObservers();
        }catch(Exception e){
            parseException.logException(e,true);
        }
        
    }
    
    private void updateTblReleativeList(int row)  throws Exception{
        Object selectedRow;
        boolean rowExists = false;
        //If row already exists update it, else create a new row & append
        for(int i = tblReleativeWorkingInBank.getRowCount(), j = 0; i > 0; i--,j++){
            selectedRow = ((ArrayList)tblReleativeWorkingInBank.getDataArrayList().get(j)).get(0);
            if( getTxtRelativeStaffId().equals(CommonUtil.convertObjToStr(selectedRow))){
                rowExists = true;
            }
        }
        ArrayList addressRow = new ArrayList();
        if(row == -1){
            addressRow.add(CommonUtil.convertObjToStr(getTxtRelativeStaffId()));
            addressRow.add(CommonUtil.convertObjToStr(getTxtReleativeFirstName()));
            addressRow.add(CommonUtil.convertObjToStr(getCboReleativeReleationShip()));
            tblReleativeWorkingInBank.insertRow(tblReleativeWorkingInBank.getRowCount(),addressRow);
            addressRow = null;
        }
        else{
            tblReleativeWorkingInBank.removeRow(row);
            addressRow.add(CommonUtil.convertObjToStr(getTxtRelativeStaffId()));
            addressRow.add(CommonUtil.convertObjToStr(getTxtReleativeFirstName()));
            addressRow.add(CommonUtil.convertObjToStr(getCboReleativeReleationShip()));
            tblReleativeWorkingInBank.insertRow(row,addressRow);
            addressRow = null;
        }
    }
    
    
    private void updateTblDependentList(int row,EmployeeMasterDependendTO objEmployeeMasterDependendTO)  throws Exception{
        Object selectedRow;
        boolean rowExists = false;
        for(int i = tblFamilyDet.getRowCount(), j = 0; i > 0; i--,j++){
            selectedRow = ((ArrayList)tblFamilyDet.getDataArrayList().get(j)).get(0);
            //   if( (CommonUtil.convertObjToStr(cbmReleationShip.getKeyForSelected())).equals(CommonUtil.convertObjToStr(selectedRow))){
            if(CommonUtil.convertObjToStr(getDependentId()).equals(CommonUtil.convertObjToStr(selectedRow))){
                rowExists = true;
            }
        }
        ArrayList addressRow = new ArrayList();
        if(row == -1){
            addressRow.add(CommonUtil.convertObjToStr(objEmployeeMasterDependendTO.getDependentId()));
            addressRow.add(CommonUtil.convertObjToStr(cbmReleationShip.getKeyForSelected()));
            addressRow.add(CommonUtil.convertObjToStr(getTxtEmpDepFirstName()));
            tblFamilyDet.insertRow(tblFamilyDet.getRowCount(),addressRow);
            addressRow = null;
        }else{
            tblFamilyDet.removeRow(row);
            //addressRow.add(CommonUtil.convertObjToStr(getDependentId()));
            addressRow.add(CommonUtil.convertObjToStr(objEmployeeMasterDependendTO.getDependentId()));
            addressRow.add(CommonUtil.convertObjToStr(cbmReleationShip.getKeyForSelected()));
            addressRow.add(CommonUtil.convertObjToStr(getTxtEmpDepFirstName()));
            tblFamilyDet.insertRow(row,addressRow);
            addressRow = null;
        }
    }
    public void populateDependent(int row){
        try{
            dependentTypeChanged(CommonUtil.convertObjToStr(tblFamilyDet.getValueAt(row,ADDRTYPE_COLNO)));
        }catch(Exception e){
            parseException.logException(e,true);
        }
    }
    
    public void populateReleative(int row){
        try{
            relativeTypeChanged(CommonUtil.convertObjToStr(tblReleativeWorkingInBank.getValueAt(row,ADDRTYPE_COLNO)));
        }catch(Exception e){
            parseException.logException(e,true);
        }
    }
    
    
    
    public void dependentTypeChanged(String selectedItem){
        try{
            technicalTypeExists = true;
            
            final EmployeeMasterDependendTO objEmployeeMasterDependendTO = (EmployeeMasterDependendTO)dependentMap.get(selectedItem);
            populateDependentDate(objEmployeeMasterDependendTO);
            ttNotifyObservers();
        }catch(Exception e){
            parseException.logException(e,true);
        }
    }
    
    public void relativeTypeChanged(String selectedItem){
        try{
            
            technicalTypeExists = true;
            final EmployeeRelativeWorkingTO objEmployeeRelativeWorkingTO = (EmployeeRelativeWorkingTO)relativeMap.get(selectedItem);
            populateReleativeData(objEmployeeRelativeWorkingTO);
            ttNotifyObservers();
        }catch(Exception e){
            parseException.logException(e,true);
        }
    }
    
    public void deleteDependent(int row){
        if(deletedDependent == null){
            deletedDependent = new LinkedHashMap();
        }
        EmployeeMasterDependendTO objEmployeeMasterDependendTO= (EmployeeMasterDependendTO)dependentMap.get(CommonUtil.convertObjToStr(tblFamilyDet.getValueAt(row,ADDRTYPE_COLNO)));
        objEmployeeMasterDependendTO.setStatus(CommonConstants.STATUS_DELETED);
        objEmployeeMasterDependendTO.setStatusDt(curDate);
        objEmployeeMasterDependendTO.setStatusBy(TrueTransactMain.USER_ID);
        deletedDependent.put(CommonUtil.convertObjToStr(tblFamilyDet.getValueAt(row,ADDRTYPE_COLNO)),dependentMap.get(CommonUtil.convertObjToStr(tblFamilyDet.getValueAt(row,ADDRTYPE_COLNO))));
        dependentMap.remove(tblFamilyDet.getValueAt(row,ADDRTYPE_COLNO));
        resetDeleteDependent();
    }
    
    
    public void deleteRelative(int row){
        if(deletedReleativeMap == null){
            deletedReleativeMap = new HashMap();
        }
        EmployeeRelativeWorkingTO objEmployeeRelativeWorkingTO= (EmployeeRelativeWorkingTO)relativeMap.get(CommonUtil.convertObjToStr(tblReleativeWorkingInBank.getValueAt(row,ADDRTYPE_COLNO)));
        objEmployeeRelativeWorkingTO.setStatus(CommonConstants.STATUS_DELETED);
        objEmployeeRelativeWorkingTO.setStatusDt(curDate);
        objEmployeeRelativeWorkingTO.setStatusBy(TrueTransactMain.USER_ID);
        deletedReleativeMap.put(CommonUtil.convertObjToStr(tblReleativeWorkingInBank.getValueAt(row,ADDRTYPE_COLNO)),relativeMap.get(CommonUtil.convertObjToStr(tblReleativeWorkingInBank.getValueAt(row,ADDRTYPE_COLNO))));
        relativeMap.remove(tblReleativeWorkingInBank.getValueAt(row,ADDRTYPE_COLNO));
        resetDeleteRelative();
    }
    
    
    public void resetDependentListTable(){
        for(int i = tblFamilyDet.getRowCount(); i > 0; i--){
            tblFamilyDet.removeRow(0);
        }
    }
    public void resetRelativeListTable(){
        for(int i = tblReleativeWorkingInBank.getRowCount(); i > 0; i--){
            tblReleativeWorkingInBank.removeRow(0);
        }
    }
    
    public void resetDirectorListTable(){
        for(int i = tblDetailsOfRelativeDirector.getRowCount(); i > 0; i--){
            tblDetailsOfRelativeDirector.removeRow(0);
        }
    }
    
    private void populateDependentTable()  throws Exception{
        ArrayList dependentDataLst = new ArrayList();
        dependentDataLst = new ArrayList(dependentMap.keySet());
        ArrayList addList =new ArrayList(dependentMap.keySet());
        int length = dependentDataLst.size();
        for(int i=0; i<length; i++){
            ArrayList dependentTabRow = new ArrayList();
            EmployeeMasterDependendTO objEmployeeMasterDependendTO = (EmployeeMasterDependendTO) dependentMap.get(addList.get(i));
            dependentTabRow = new ArrayList();
            dependentTabRow.add(CommonUtil.convertObjToStr(objEmployeeMasterDependendTO.getDependentId()));
            dependentTabRow.add(CommonUtil.convertObjToStr(objEmployeeMasterDependendTO.getCboDepReleationShip()));
            dependentTabRow.add(CommonUtil.convertObjToStr(objEmployeeMasterDependendTO.getTxtEmpDepFirstName()));
            tblFamilyDet.insertRow(tblFamilyDet.getRowCount(),dependentTabRow);
            dependentTabRow= null;
        }
    }
    
    private void populateDirectorTable()  throws Exception{
        //
        ArrayList directorDataLst = new ArrayList();
        directorDataLst = new ArrayList(directorMap.keySet());
        ArrayList addList =new ArrayList(directorMap.keySet());
        int length = directorDataLst.size();
        for(int i=0; i<length; i++){
            ArrayList directorTabRow = new ArrayList();
            EmployeeRelativeDirectorTO objEmployeeRelativeDirectorTO = (EmployeeRelativeDirectorTO) directorMap.get(addList.get(i));
            directorTabRow = new ArrayList();
            directorTabRow.add(CommonUtil.convertObjToStr(objEmployeeRelativeDirectorTO.getDirectorID()));
            directorTabRow.add(CommonUtil.convertObjToStr(objEmployeeRelativeDirectorTO.getDirectorFirstName()));
            tblDetailsOfRelativeDirector.insertRow(tblDetailsOfRelativeDirector.getRowCount(),directorTabRow);
        }
    }
    private void populateRelativeTable()  throws Exception{
        
        //added from here
        ArrayList relativeDataLst = new ArrayList();
        relativeDataLst = new ArrayList(relativeMap.keySet());
        ArrayList addList =new ArrayList(relativeMap.keySet());
        int length = relativeDataLst.size();
        for(int i=0; i<length; i++){
            ArrayList relativeTabRow = new ArrayList();
            EmployeeRelativeWorkingTO objEmployeeRelativeWorkingTO = (EmployeeRelativeWorkingTO) relativeMap.get(addList.get(i));
            relativeTabRow = new ArrayList();
            relativeTabRow.add(CommonUtil.convertObjToStr(objEmployeeRelativeWorkingTO.getStaffId()));
            
            relativeTabRow.add(CommonUtil.convertObjToStr(objEmployeeRelativeWorkingTO.getRelativeFirstName()));
            relativeTabRow.add(CommonUtil.convertObjToStr(objEmployeeRelativeWorkingTO.getRelativerelationShip()));
            tblReleativeWorkingInBank.insertRow(tblReleativeWorkingInBank.getRowCount(),relativeTabRow);
        }
    }
    
    public void resetDeleteDependent(){
        try{
            
            resetDependent();
            resetDependentListTable();
            populateDependentTable();
            ttNotifyObservers();
        }catch(Exception e){
            parseException.logException(e,true);
        }
    }
    
    public void resetDeleteRelative(){
        try{
            
            resetRelative();
            resetRelativeListTable();
            populateRelativeTable();
            ttNotifyObservers();
        }catch(Exception e){
            parseException.logException(e,true);
        }
    }
    
    public void resetDeleteDirector(){
        try{
            
            resetDirector();
            resetDirectorListTable();
            populateDirectorTable();
            ttNotifyObservers();
            
        }catch(Exception e){
            parseException.logException(e,true);
        }
    }
    /** To populate Address Data */
    private void populateDependentDate(EmployeeMasterDependendTO objEmployeeMasterDependendTO)  throws Exception{
        try{
            if(objEmployeeMasterDependendTO != null){
                setDependentId(objEmployeeMasterDependendTO.getDependentId());
                setCboDepReleationShip(objEmployeeMasterDependendTO.getCboDepReleationShip());
                setTxtEmpDepFirstName(objEmployeeMasterDependendTO.getTxtEmpDepFirstName());
                setTxtEmpDepMIddleName(objEmployeeMasterDependendTO.getTxtEmpDepMIddleName());
                setTxtEmpDepLasteName(objEmployeeMasterDependendTO.getTxtEmpDepLasteName());
                setCboEmpDepTitle(objEmployeeMasterDependendTO.getCboEmpDepTitle());
                setCboDepEducation(objEmployeeMasterDependendTO.getCboDepEducation());
                setCboDepProfession(objEmployeeMasterDependendTO.getCboDepProfession());
                setRdoDepYesNo(objEmployeeMasterDependendTO.getRdoDepYesNo());
                setRdoLiableYesNo(objEmployeeMasterDependendTO.getRdoLiableYesNo());
                setTdtDepDateOfBirth(DateUtil.getStringDate(objEmployeeMasterDependendTO.getTdtDepDateOfBirth()));
                setTxtDepIncomePerannum(objEmployeeMasterDependendTO.getTxtDepIncomePerannum());
                setTxtEmpWith(objEmployeeMasterDependendTO.getTxtEmpWith());
            }
        }catch(Exception e){
            e.printStackTrace();
        }
    }
    
    
    
    private void populateReleativeData(EmployeeRelativeWorkingTO objEmployeeRelativeWorkingTO)  throws Exception{
        try{
            if(objEmployeeRelativeWorkingTO != null){
                
                setTxtRelativeStaffId(CommonUtil.convertObjToStr(objEmployeeRelativeWorkingTO.getStaffId()));
                setCboRelativeTittle(CommonUtil.convertObjToStr(objEmployeeRelativeWorkingTO.getRelativeTittle()));
                setTxtReleativeFirstName(CommonUtil.convertObjToStr(objEmployeeRelativeWorkingTO.getRelativeFirstName()));
                setTxtReleativeMiddleName(CommonUtil.convertObjToStr(objEmployeeRelativeWorkingTO.getRelativeMiddleName()));
                setTxtReleativeLastName(CommonUtil.convertObjToStr(objEmployeeRelativeWorkingTO.getRelativeLastName()));
                setCboReleativeReleationShip(CommonUtil.convertObjToStr(objEmployeeRelativeWorkingTO.getRelativerelationShip()));
                setCboReleativeBranchId(CommonUtil.convertObjToStr(objEmployeeRelativeWorkingTO.getRelativeWorkingBranch()));
                getCbmReleativeBranchId().setKeyForSelected(objEmployeeRelativeWorkingTO.getRelativeWorkingBranch());
                //                getCbmReleativeDisg().setKeyForSelected(objEmployeeRelativeWorkingTO.getRelativeDisgnantion());
                setCboReleativeDisg(CommonUtil.convertObjToStr(objEmployeeRelativeWorkingTO.getRelativeDisgnantion()));
                
            }
        }catch(Exception e){
            e.printStackTrace();
        }
    }
    
    /**
     * Getter for property newAddress.
     * @return Value of property newAddress.
     */
    public boolean isNewAddress() {
        return newAddress;
    }
    
    /**
     * Setter for property newAddress.
     * @param newAddress New value of property newAddress.
     */
    public void setNewAddress(boolean newAddress) {
        phoneList = null;
        this.newAddress = newAddress;
    }
    
    /**
     * Getter for property newEducation.
     * @return Value of property newEducation.
     */
    public boolean isNewEducation() {
        return newEducation;
    }
    
    /**
     * Setter for property newEducation.
     * @param newEducation New value of property newEducation.
     */
    public void setNewEducation(boolean newEducation) {
        this.newEducation = newEducation;
    }
    
    /**
     * Getter for property EducationExists.
     * @return Value of property EducationExists.
     */
    public boolean isEducationExists() {
        return educationExists;
    }
    
    /**
     * Setter for property EducationExists.
     * @param EducationExists New value of property EducationExists.
     */
    public void setEducationExists(boolean educationExists) {
        this.educationExists = educationExists;
    }
    
    /**
     * Getter for property educationChanged.
     * @return Value of property educationChanged.
     */
    public boolean isEducationChanged() {
        return educationChanged;
    }
    
    /**
     * Setter for property educationChanged.
     * @param educationChanged New value of property educationChanged.
     */
    public void setEducationChanged(boolean educationChanged) {
        this.educationChanged = educationChanged;
    }
    
    /**
     * Getter for property newTechnical.
     * @return Value of property newTechnical.
     */
    public boolean isNewTechnical() {
        return newTechnical;
    }
    
    /**
     * Setter for property newTechnical.
     * @param newTechnical New value of property newTechnical.
     */
    public void setNewTechnical(boolean newTechnical) {
        this.newTechnical = newTechnical;
    }
    
    /**
     * Getter for property languageChanged.
     * @return Value of property languageChanged.
     */
    public boolean isLanguageChanged() {
        return languageChanged;
    }
    
    /**
     * Setter for property languageChanged.
     * @param languageChanged New value of property languageChanged.
     */
    public void setLanguageChanged(boolean languageChanged) {
        this.languageChanged = languageChanged;
    }
    
    /**
     * Getter for property Changed.
     * @return Value of property dependentChanged.
     */
    public boolean isDependentChanged() {
        return dependentChanged;
    }
    
    /**
     * Setter for property dependentChanged.
     * @param dependentChanged New value of property dependentChanged.
     */
    public void setDependentChanged(boolean dependentChanged) {
        this.dependentChanged = dependentChanged;
    }
    
    /**
     * Getter for property newDependent.
     * @return Value of property newDependent.
     */
    public boolean isNewDependent() {
        return newDependent;
    }
    
    /**
     * Setter for property newDependent.
     * @param newDependent New value of property newDependent.
     */
    public void setNewDependent(boolean newDependent) {
        this.newDependent = newDependent;
    }
    
    /**
     * Getter for property lblPhoto.
     * @return Value of property lblPhoto.
     */
    public java.lang.String getLblPhoto() {
        return lblPhoto;
    }
    
    /**
     * Setter for property lblPhoto.
     * @param lblPhoto New value of property lblPhoto.
     */
    public void setLblPhoto(java.lang.String lblPhoto) {
        this.lblPhoto = lblPhoto;
    }
    
    /**
     * Getter for property photoFile.
     * @return Value of property photoFile.
     */
    public java.lang.String getPhotoFile() {
        return photoFile;
    }
    
    /**
     * Setter for property photoFile.
     * @param photoFile New value of property photoFile.
     */
    public void setPhotoFile(java.lang.String photoFile) {
        this.photoFile = photoFile;
        setChanged();
    }
    
    /**
     * Getter for property photoByteArray.
     * @return Value of property photoByteArray.
     */
    public byte[] getPhotoByteArray() {
        return this.photoByteArray;
    }
    
    /**
     * Setter for property photoByteArray.
     * @param photoByteArray New value of property photoByteArray.
     */
    public void setPhotoByteArray(byte[] photoByteArray) {
        this.photoByteArray = photoByteArray;
    }
    
    /**
     * Getter for property lblSign.
     * @return Value of property lblSign.
     */
    public java.lang.String getLblSign() {
        return lblSign;
    }
    
    /**
     * Setter for property lblSign.
     * @param lblSign New value of property lblSign.
     */
    public void setLblSign(java.lang.String lblSign) {
        this.lblSign = lblSign;
    }
    
    /**
     * Getter for property signFile.
     * @return Value of property signFile.
     */
    public java.lang.String getSignFile() {
        return signFile;
    }
    
    /**
     * Setter for property signFile.
     * @param signFile New value of property signFile.
     */
    public void setSignFile(java.lang.String signFile) {
        this.signFile = signFile;
    }
    
    /**
     * Getter for property signByteArray.
     * @return Value of property signByteArray.
     */
    public byte[] getSignByteArray() {
        return this.signByteArray;
    }
    
    /**
     * Setter for property signByteArray.
     * @param signByteArray New value of property signByteArray.
     */
    public void setSignByteArray(byte[] signByteArray) {
        this.signByteArray = signByteArray;
    }
    
    public HashMap doAction(){
        HashMap proxyResultMap = new HashMap();
        try{
            if( actionType != ClientConstants.ACTIONTYPE_CANCEL ){
                switch(actionType) {
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
                
                
                data.put(CommonConstants.BRANCH_ID, ProxyParameters.BRANCH_ID);
                data.put(CommonConstants.SELECTED_BRANCH_ID, getSelectedBranchID());
                proxyResultMap = proxy.execute(data, map);
                txtEmpId = CommonUtil.convertObjToStr(proxyResultMap.get("CUST_ID"));
                setProxyReturnMap(proxyResultMap);
                //                storePhotoSign();
                //                resetForm();
                setResult(actionType);
                actionType = ClientConstants.ACTIONTYPE_CANCEL;
            }
        }catch(Exception e){
            setResult(ClientConstants.ACTIONTYPE_FAILED);
            parseException.logException(e,true);
        }
        return proxyResultMap;
    }
    public void authorizeEmployee(HashMap singleAuthorizeMap) {
        try{
            singleAuthorizeMap.put("AUTH_DATA","AUTH_DATA");
            proxy.executeQuery(singleAuthorizeMap,map);
        }
        catch(Exception e){
            setResult(ClientConstants.ACTIONTYPE_FAILED);
            parseException.logException(e,true);
        }
    }
    private void setEmployeeMasterDate(){
        objEmployeeMasterTO.setTxtEmpId(getTxtEmpId());
        objEmployeeMasterTO.setCboEmpTitle(getCboEmpTitle());
        objEmployeeMasterTO.setTxtEmpFirstName(getTxtEmpFirstName());
        objEmployeeMasterTO.setTxtEmpMiddleName(getTxtEmpMiddleName());
        objEmployeeMasterTO.setTxtEmpLastName(getTxtEmpLastName());
        objEmployeeMasterTO.setCboEmpFatheTitle(getCboEmpFatheTitle());
        objEmployeeMasterTO.setTxtEmpFatherFirstName(getTxtEmpFatherFirstName());
        objEmployeeMasterTO.setTxtEmpFatherMIddleName(getTxtEmpFatherMIddleName());
        objEmployeeMasterTO.setTxtEmpFatherLasteName(getTxtEmpFatherLasteName());
        objEmployeeMasterTO.setCboEmpMotherTitle(getCboEmpMotherTitle());
        objEmployeeMasterTO.setTxtEmpMotherFirstName(getTxtEmpMotherFirstName());
        objEmployeeMasterTO.setTxtEmpMotherMIddleName(getTxtEmpMotherMIddleName());
        objEmployeeMasterTO.setTxtEmpMotherLasteName(getTxtEmpMotherLasteName());
        objEmployeeMasterTO.setRdoMaritalStatus(getRdoMaritalStatus());
        objEmployeeMasterTO.setRdoEmpGender(getRdoEmpGender());
        objEmployeeMasterTO.setRdoFatherHusband(getRdoFatherHusband());
        Date issueDt = DateUtil.getDateMMDDYYYY(getTdtEmpDateOfBirth());
        if(issueDt != null){
            Date dobDate = (Date)curDate.clone();
            dobDate.setDate(issueDt.getDate());
            dobDate.setMonth(issueDt.getMonth());
            dobDate.setYear(issueDt.getYear());
            objEmployeeMasterTO.setTdtEmpDateOfBirth(dobDate);
        }else{
            objEmployeeMasterTO.setTdtEmpDateOfBirth(DateUtil.getDateMMDDYYYY(getTdtEmpDateOfBirth()));
        }
        objEmployeeMasterTO.setTxtempAge(getTxtempAge());
        objEmployeeMasterTO.setTxtempPlaceOfBirth(getTxtempPlaceOfBirth());
        objEmployeeMasterTO.setCboEmpReligon(getCboEmpReligon());
        objEmployeeMasterTO.setCboEmpCaste(getCboEmpCaste());
        objEmployeeMasterTO.setTxtEmpHomeTown(getTxtEmpHomeTown());
        objEmployeeMasterTO.setTxtEmpIdCardNo(getTxtEmpIdCardNo());
        objEmployeeMasterTO.setTxtEmpUIdNo(getTxtEmpUIdNo());
        objEmployeeMasterTO.setTxtEmpPanNo(getTxtEmpPanNo());
        objEmployeeMasterTO.setTxtEmpPfNo(getTxtEmpPfNo());
        objEmployeeMasterTO.setCboEmpPfNominee(getCboEmpPfNominee());
        Date dateOfJoin = DateUtil.getDateMMDDYYYY(getTdtEmpJoinDate());
        if(dateOfJoin != null){
            Date dJoin = (Date)curDate.clone();
            dJoin.setDate(dateOfJoin.getDate());
            dJoin.setMonth(dateOfJoin.getMonth());
            dJoin.setYear(dateOfJoin.getYear());
            objEmployeeMasterTO.setTdtEmpJoinDate(dJoin);
        }else{
            objEmployeeMasterTO.setTdtEmpJoinDate(DateUtil.getDateMMDDYYYY(getTdtEmpJoinDate()));
        }
        
        
        Date retDate = DateUtil.getDateMMDDYYYY(getTdtEmpRetirementDate());
        if(retDate != null){
            Date rDate = (Date)curDate.clone();
            rDate.setDate(retDate.getDate());
            rDate.setMonth(retDate.getMonth());
            rDate.setYear(retDate.getYear());
            objEmployeeMasterTO.setTdtEmpRetirementDate(rDate);
        }else{
            objEmployeeMasterTO.setTdtEmpRetirementDate(DateUtil.getDateMMDDYYYY(getTdtEmpRetirementDate()));
        }
        
        objEmployeeMasterTO.setCboBloodGroup(getCboBloodGroup());
        objEmployeeMasterTO.setTxtMajorHealthProbeem(getTxtMajorHealthProbeem());
        objEmployeeMasterTO.setTxtDrivingLicenceNo(getTxtDrivingLicenceNo());
        Date renDate = DateUtil.getDateMMDDYYYY(getTdtDLRenewalDate());
        if(renDate != null){
            Date rDate = (Date)curDate.clone();
            rDate.setDate(renDate.getDate());
            rDate.setMonth(renDate.getMonth());
            rDate.setYear(renDate.getYear());
            objEmployeeMasterTO.setTdtDLRenewalDate(rDate);
        }else{
            objEmployeeMasterTO.setTdtDLRenewalDate(DateUtil.getDateMMDDYYYY(getTdtDLRenewalDate()));
        }
        objEmployeeMasterTO.setTxtEmailId(getTxtEmailId());
        objEmployeeMasterTO.setCboDomicileState(getCboDomicileState());
        objEmployeeMasterTO.setTxtPhysicalHandicap(CommonUtil.convertObjToStr(getTxtPhysicalHandicap()));
        
    }
    private void setData(){
        data = new HashMap();
        
        data.put("EMPLOYEEMASTER",objEmployeeMasterTO);
        data.put("EMPLOYEEPRESENTDETAILS",objEmployeePresentDetailsTO);
        if(addressMap!=null && addressMap.size()>0 )
            data.put("ADDRESS",addressMap);
        if(educationMap!=null && educationMap.size()>0)
            data.put("ACADEMIC",educationMap);
        if(technicalMap!=null && technicalMap.size()>0 )
            data.put("TECHNICAL",technicalMap);
        if(laungeMap!=null && laungeMap.size()>0)
            data.put("LANGUAGE",laungeMap);
        if(dependentMap!=null && dependentMap.size()>0)
            data.put("DEPENDENT",dependentMap);
        
        if(phoneMap!=null && phoneMap.size()>0){
            data.put("PHONE", phoneMap) ;
        }
        
        if(deletedPhoneMap != null){
            data.put("PHONEDELETED", deletedPhoneMap);
            deletedPhoneMap = null;
        }
        
        if(relativeMap!=null && relativeMap.size()>0){
            data.put("RELATIVE", relativeMap) ;
        }
        if(deletedReleativeMap != null){
            data.put("RELATIVEDELETED", deletedReleativeMap);
            deletedReleativeMap = null;
        }
        if(directorMap!=null && directorMap.size()>0){
            data.put("DIRECTOR", directorMap) ;
        }
        if(deletedDirectorMap != null){
            data.put("DIRECTORDELETED", deletedDirectorMap);
            deletedDirectorMap = null;
        }
        if(oprativeMap!=null && oprativeMap.size()>0){
            data.put("OPRATIVE", oprativeMap) ;
        }
        if(deletedOprativeMap != null){
            data.put("OPRATIVEDELETED", deletedOprativeMap);
            deletedOprativeMap = null;
        }
        if(promotionMap != null){
            data.put("PROMOTION",promotionMap);
            promotionMap = null;
        }
        if(deletedPromotionMap != null){
            data.put("PROMOTIONDELETED",deletedPromotionMap);
            deletedPromotionMap = null;
        }
        phoneMap=null;
        phoneList = null;
        addressMap=null;
        educationMap=null;
        technicalMap = null;
        laungeMap = null;
        dependentMap = null;
        relativeMap=null;
        directorMap=null;
        loanMap=null;
        deletedLoanMap = null;
        oprativeMap=null;
        if(objEmployeeMasterPassPortTO != null){
            data.put("PASSPORT", objEmployeeMasterPassPortTO);
        }
        objEmployeeMasterPassPortTO=null;
        
        if(deletedAddressMap!=null && deletedAddressMap.size()>0){
            data.put("ADDRESSDELETED",deletedAddressMap);
            deletedAddressMap=null;
        }
        
        if(deletedDependent!=null && deletedDependent.size()>0){
            data.put("DEPENDENTDELETED",deletedDependent);
            deletedDependent=null;
        }
        if(deletedEducationsMap!=null && deletedEducationsMap.size()>0){
            data.put("ACADEMICDELETED",deletedEducationsMap);
            deletedEducationsMap=null;
        }
        if(deletedLanguageMap!=null && deletedLanguageMap.size()>0){
            data.put("LANGUAGEDELETED",deletedLanguageMap);
            deletedLanguageMap=null;
        }
        if(deletedTechnicalMap!=null && deletedTechnicalMap.size()>0){
            data.put("TECHNICALDELETED",deletedTechnicalMap);
            deletedTechnicalMap=null;
        }
        data.put("PHOTO",this.photoByteArray);
        data.put("SIGN",this.signByteArray);
        
    }
    private void insertData() throws Exception{
        setEmployeeMasterDate();
        setPresentDetails();
        if (txtPassportFirstName.length()>0 && txtPassportNo.length()>0){
            setPassportDetails();
        }
        objEmployeeMasterTO.setCommand(CommonConstants.TOSTATUS_INSERT);
        objEmployeeMasterTO.setStatus(CommonConstants.STATUS_CREATED );
        objEmployeeMasterTO.setCreatedDt(curDate);
        objEmployeeMasterTO.setStatusDt(curDate);
        objEmployeeMasterTO.setCreatedBy(TrueTransactMain.USER_ID);
        setData();
    }
    private void setPromotionDetails()throws Exception{
        HashMap objHashMap=null;
        if (deletePromotionList!=null) {
            PromotionTOs.addAll(deletePromotionList);
        }
        else if(PromotionTOs != null && PromotionTOs.size()>0){
            data.put("PromotionTOs",PromotionTOs);
            data.put("deletePromotionList",deletePromotionList);
        }
    }
    private void setPassportDetails() throws Exception{
        objEmployeeMasterPassPortTO = new EmployeeMasterPassPortTO();
        if( actionType == ClientConstants.ACTIONTYPE_EDIT ){
            objEmployeeMasterPassPortTO.setTxtEmpId(sysId);
        }
        objEmployeeMasterPassPortTO.setPassfname(CommonUtil.convertObjToStr(txtPassportFirstName));
        objEmployeeMasterPassPortTO.setPassmname(CommonUtil.convertObjToStr(txtPassportMiddleName));
        objEmployeeMasterPassPortTO.setPasslname(CommonUtil.convertObjToStr(txtPassportLastName));
        objEmployeeMasterPassPortTO.setPassNumber(CommonUtil.convertObjToStr(txtPassportNo));
        objEmployeeMasterPassPortTO.setPassTitle(CommonUtil.convertObjToStr(getCbmTitle().getKeyForSelected()));
        objEmployeeMasterPassPortTO.setIssuePlace(CommonUtil.convertObjToStr(getCbmPassportIssuePlace().getKeyForSelected()));
        objEmployeeMasterPassPortTO.setIssueAuth(CommonUtil.convertObjToStr(txtPassportIssueAuth));
        Date issueDt = DateUtil.getDateMMDDYYYY(tdtPassportIssueDt);
        if(issueDt != null){
            Date dobDate = (Date)curDate.clone();
            dobDate.setDate(issueDt.getDate());
            dobDate.setMonth(issueDt.getMonth());
            dobDate.setYear(issueDt.getYear());
            objEmployeeMasterPassPortTO.setIssueDt(dobDate);
        }else{
            objEmployeeMasterPassPortTO.setIssueDt(DateUtil.getDateMMDDYYYY(tdtPassportIssueDt));
        }
        Date validDt = DateUtil.getDateMMDDYYYY(tdtPassportValidUpto);
        if(validDt != null){
            Date dobDate = (Date)curDate.clone();
            dobDate.setDate(validDt.getDate());
            dobDate.setMonth(validDt.getMonth());
            dobDate.setYear(validDt.getYear());
            objEmployeeMasterPassPortTO.setValidUpto(dobDate);
        }else{
            objEmployeeMasterPassPortTO.setValidUpto(DateUtil.getDateMMDDYYYY(tdtPassportValidUpto));
        }
    }
    public void  getData(HashMap whereMap){
        try{
            data = (HashMap)proxy.executeQuery(whereMap,map);
            System.out.println("@#$@#$@#$@#data : "+data+ " : "+whereMap+ " : "+map);
            if(data.containsKey("EMPLOYEEPRESENTDETAILS")){
                objEmployeePresentDetailsTO=(EmployeePresentDetailsTO)data.get("EMPLOYEEPRESENTDETAILS");
                getPresentDetails(objEmployeePresentDetailsTO);
            }
            if(data.containsKey("EMPLOYEEMASTER")){
                objEmployeeMasterTO=(EmployeeMasterTO)data.get("EMPLOYEEMASTER");
                pouplateEmployeeMasterData(objEmployeeMasterTO);
                ttNotifyObservers();
                setChanged();
            }
            String addressMapKey = "";
            //final EmployeeMasterAddressTO objCustomerAddressTO = (EmployeeMasterAddressTO)addressMap.get(addressMapKey);
            if(data.containsKey("ADDRESS")){
                addressMap = (HashMap)data.get("ADDRESS");
                ArrayList addList =new ArrayList(addressMap.keySet());
                for(int i=0;i<addList.size();i++){
                    EmployeeMasterAddressTO objAddressTO = (EmployeeMasterAddressTO) addressMap.get(addList.get(i));
                    objAddressTO.setStatus(CommonConstants.STATUS_MODIFIED);
                    objAddressTO.setStatusBy(TrueTransactMain.USER_ID);
                    objAddressTO.setStatusDt(curDate);
                    addressMap.put(objAddressTO.getCboEmpAddressType(), objAddressTO);
                    //addressMapKey = objAddressTO.getCboEmpAddressType();
                }
                
                populateContactsTable();
                if(data.containsKey("PHONE")){
                    phoneMap = (HashMap)data.get("PHONE");
                    ArrayList phoneArray =new ArrayList(phoneMap.keySet());
                }
                log.info("data:"+data);
                log.info("phoneMap:"+phoneMap);
                
                log.info("phoneList:"+phoneList);
                populatePhoneTable();
                
            }
            
            if(data.containsKey("ACADEMIC")){
                educationMap = (LinkedHashMap)data.get("ACADEMIC");
                ArrayList addList =new ArrayList(educationMap.keySet());
                for(int i=0;i<addList.size();i++){
                    EmployeeMasterEducationTO objAddressTO = (EmployeeMasterEducationTO) educationMap.get(addList.get(i));
                    objAddressTO.setStatus(CommonConstants.STATUS_MODIFIED);
                    objAddressTO.setStatusBy(TrueTransactMain.USER_ID);
                    objAddressTO.setStatusDt(curDate);
                    educationMap.put(objAddressTO.getAcademicID(), objAddressTO);
                }
                populateEducationTable();
            }
            if(data.containsKey("TECHNICAL")){
                technicalMap = (LinkedHashMap)data.get("TECHNICAL");
                ArrayList addList =new ArrayList(technicalMap.keySet());
                for(int i=0;i<addList.size();i++){
                    EmployeeMasterTechnicalTO objAddressTO = (EmployeeMasterTechnicalTO) technicalMap.get(addList.get(i));
                    objAddressTO.setStatus(CommonConstants.STATUS_MODIFIED);
                    objAddressTO.setStatusBy(TrueTransactMain.USER_ID);
                    objAddressTO.setStatusDt(curDate);
                    technicalMap.put(objAddressTO.getTechnicalID(), objAddressTO);
                }
                populateTechnicalTable();
            }
            
            
            if(data.containsKey("LANGUAGE")){
                laungeMap = (HashMap)data.get("LANGUAGE");
                ArrayList addList =new ArrayList(laungeMap.keySet());
                for(int i=0;i<addList.size();i++){
                    EmployeeMasterLanguageTO objAddressTO = (EmployeeMasterLanguageTO) laungeMap.get(addList.get(i));
                    objAddressTO.setStatus(CommonConstants.STATUS_MODIFIED);
                    objAddressTO.setStatusBy(TrueTransactMain.USER_ID);
                    objAddressTO.setStatusDt(curDate);
                    laungeMap.put(objAddressTO.getCboLanguageType(), objAddressTO);
                }
                populateLanguageTable();
            }
            
            if(data.containsKey("DEPENDENT")){
                dependentMap = (LinkedHashMap)data.get("DEPENDENT");
                ArrayList addList =new ArrayList(dependentMap.keySet());
                for(int i=0;i<addList.size();i++){
                    EmployeeMasterDependendTO objAddressTO = (EmployeeMasterDependendTO) dependentMap.get(addList.get(i));
                    objAddressTO.setStatus(CommonConstants.STATUS_MODIFIED);
                    objAddressTO.setStatusBy(TrueTransactMain.USER_ID);
                    objAddressTO.setStatusDt(curDate);
                    dependentMap.put(objAddressTO.getDependentId(), objAddressTO);
                }
                populateDependentTable();
            }
            
            if(data.containsKey("RELATIVE")){
                relativeMap = (HashMap)data.get("RELATIVE");
                ArrayList addList =new ArrayList(relativeMap.keySet());
                for(int i=0;i<addList.size();i++){
                    EmployeeRelativeWorkingTO objAddressTO = new EmployeeRelativeWorkingTO();
                    objAddressTO = (EmployeeRelativeWorkingTO) relativeMap.get(addList.get(i));
                    objAddressTO.setStatus(CommonConstants.STATUS_MODIFIED);
                    objAddressTO.setStatusBy(TrueTransactMain.USER_ID);
                    objAddressTO.setStatusDt(curDate);
                    relativeMap.put(objAddressTO.getStaffId(), objAddressTO);
                    ArrayList addressRow = null;
                    addressRow = new ArrayList();
                    addressRow.add(objAddressTO.getStaffId());
                    addressRow.add(objAddressTO.getRelativeFirstName());
                    addressRow.add(objAddressTO.getRelativerelationShip());
                    tblReleativeWorkingInBank.insertRow(tblReleativeWorkingInBank.getRowCount(),addressRow);
                    //       populateRelativeTable();
                }
            }
            
            if(data.containsKey("PROMOTION")){
                if( promotionMap == null ){
                    promotionMap = new LinkedHashMap();
                }
                promotionMap = (LinkedHashMap)data.get("PROMOTION");
                System.out.println("@#$@#$@#$Promotion:"+promotionMap);
                ArrayList addList =new ArrayList(promotionMap.keySet());
                for(int i=0;i<addList.size();i++){
                    PromotionTO objPromotionTO = (PromotionTO) promotionMap.get(addList.get(i));
                    objPromotionTO.setStatus(CommonConstants.STATUS_MODIFIED);
                    objPromotionTO.setStatusBy(TrueTransactMain.USER_ID);
                    objPromotionTO.setStatusDt(curDate);
                    promotionMap.put(objPromotionTO.getPromotionID(), objPromotionTO);
                }
                populatePromotionTable();
            }
            
            if(data.containsKey("DIRECTOR")){
                directorMap = (LinkedHashMap)data.get("DIRECTOR");
                ArrayList addList =new ArrayList(directorMap.keySet());
                for(int i=0;i<addList.size();i++){
                    EmployeeRelativeDirectorTO objAddressTO = (EmployeeRelativeDirectorTO) directorMap.get(addList.get(i));
                    objAddressTO.setStatus(CommonConstants.STATUS_MODIFIED);
                    objAddressTO.setStatusBy(TrueTransactMain.USER_ID);
                    objAddressTO.setStatusDt(curDate);
                    directorMap.put(objAddressTO.getDirectorID(), objAddressTO);
                    ArrayList addressRow = null;
                    addressRow = new ArrayList();
                    addressRow.add(objAddressTO.getDirectorID());
                    addressRow.add(objAddressTO.getDirectorFirstName());
                    tblDetailsOfRelativeDirector.insertRow(tblDetailsOfRelativeDirector.getRowCount(),addressRow);
                    
                }
                
            }
            
            if(data.containsKey("EMPLOYEELOANDETAILS")){
                loanMap=new HashMap();
                loanMap = (HashMap)data.get("EMPLOYEELOANDETAILS");
                ArrayList addList =new ArrayList(loanMap.keySet());
                for(int i=0;i<addList.size();i++){
                    EmployeeTermLoanTO objAddressTO = (EmployeeTermLoanTO) loanMap.get(addList.get(i));
                    objAddressTO.setStatus(CommonConstants.STATUS_MODIFIED);
                    objAddressTO.setStatusBy(TrueTransactMain.USER_ID);
                    objAddressTO.setStatusDt(curDate);
                    loanMap.put(objAddressTO.getCboEmployeeLoanType(), objAddressTO);
                    ArrayList addressRow = null;
                    addressRow = new ArrayList();
                    addressRow.add(objAddressTO.getCboEmployeeLoanType());
                    addressRow.add(objAddressTO.getTxtLoanNo());
                    addressRow.add(objAddressTO.getCboLoanAvailedBranch());
                    tblEmployeeLoan.insertRow(tblEmployeeLoan.getRowCount(),addressRow);
                    //                    tblEmployeeLoant.insertRow(tblEmployeeLoant.getRowCount(),addressRow);
                    
                }
                
            }
            
            if(data.containsKey("OPRATIVE")){
                oprativeMap = (HashMap)data.get("OPRATIVE");
                ArrayList addList =new ArrayList(oprativeMap.keySet());
                for(int i=0;i<addList.size();i++){
                    EmployeeOprativeTO objAddressTO = (EmployeeOprativeTO) oprativeMap.get(addList.get(i));
                    objAddressTO.setStatus(CommonConstants.STATUS_MODIFIED);
                    objAddressTO.setStatusBy(TrueTransactMain.USER_ID);
                    objAddressTO.setStatusDt(curDate);
                    oprativeMap.put(objAddressTO.getOperativeId(), objAddressTO);
                    ArrayList addressRow = null;
                    addressRow = new ArrayList();
                    addressRow.add(objAddressTO.getOperativeId()); 
                    addressRow.add(objAddressTO.getCboOprativePordId());
                    addressRow.add(objAddressTO.getTxtOPAcNo());
                    addressRow.add(objAddressTO.getCboOpACBranch());
                    
                    tblOprative.insertRow(tblOprative.getRowCount(),addressRow);
                }
                
            }
            if(data.containsKey("PASSPORT")){
                objEmployeeMasterPassPortTO = (EmployeeMasterPassPortTO) data.get("PASSPORT");
                if(objEmployeeMasterPassPortTO!=null ){
                    populatePassPortDetails(objEmployeeMasterPassPortTO);
                }
            }
            
        }
        catch(Exception e){
            parseException.logException(e,true);
        }
        
    } 
    public HashMap getPromotionMapData(){
        HashMap promotionDataMap = new HashMap();
        ArrayList promotionDataList = new ArrayList();
        promotionDataList.add(data.get("PROMOTION"));
        promotionDataMap = (HashMap)data.get("PROMOTION");
        System.out.println("@#$@#$@4promotionDataMap"+promotionDataMap);
        return promotionDataMap;
    }
    private void  populatePassPortDetails(EmployeeMasterPassPortTO objEmployeeMasterPassPortTO){
        
        setTxtPassportFirstName(objEmployeeMasterPassPortTO.getPassfname());
        setTxtPassportMiddleName(objEmployeeMasterPassPortTO.getPassmname());
        setTxtPassportLastName(objEmployeeMasterPassPortTO.getPasslname());
        setCboPassportTitle(objEmployeeMasterPassPortTO.getPassTitle());
        setTxtPassportNo(objEmployeeMasterPassPortTO.getPassNumber());
        setTdtPassportIssueDt(DateUtil.getStringDate(objEmployeeMasterPassPortTO.getIssueDt()));
        setTdtPassportValidUpto(DateUtil.getStringDate(objEmployeeMasterPassPortTO.getValidUpto()));
        setTxtPassportIssueAuth(objEmployeeMasterPassPortTO.getIssueAuth());
        setCboPassportIssuePlace(objEmployeeMasterPassPortTO.getIssuePlace());
        
    }
    private void pouplateEmployeeMasterData(EmployeeMasterTO objEmployeeMasterTO){
        setTxtEmpId(objEmployeeMasterTO.getTxtEmpId());
        setCboEmpTitle(objEmployeeMasterTO.getCboEmpTitle());
        setTxtEmpFirstName(objEmployeeMasterTO.getTxtEmpFirstName());
        setTxtEmpMiddleName(objEmployeeMasterTO.getTxtEmpMiddleName());
        setTxtEmpLastName(objEmployeeMasterTO.getTxtEmpLastName());
        setCboEmpFatheTitle(objEmployeeMasterTO.getCboEmpFatheTitle());
        setTxtEmpFatherFirstName(objEmployeeMasterTO.getTxtEmpFatherFirstName());
        setTxtEmpFatherMIddleName(objEmployeeMasterTO.getTxtEmpFatherMIddleName());
        setTxtEmpFatherLasteName(objEmployeeMasterTO.getTxtEmpFatherLasteName());
        setCboEmpMotherTitle(objEmployeeMasterTO.getCboEmpMotherTitle());
        setTxtEmpMotherFirstName(objEmployeeMasterTO.getTxtEmpMotherFirstName());
        setTxtEmpMotherMIddleName(objEmployeeMasterTO.getTxtEmpMotherMIddleName());
        setTxtEmpMotherLasteName(objEmployeeMasterTO.getTxtEmpMotherLasteName());
        setRdoMaritalStatus(objEmployeeMasterTO.getRdoMaritalStatus());
        setRdoEmpGender(objEmployeeMasterTO.getRdoEmpGender());
        setRdoFatherHusband(objEmployeeMasterTO.getRdoFatherHusband());
        setTdtEmpDateOfBirth(DateUtil.getStringDate(objEmployeeMasterTO.getTdtEmpDateOfBirth()));
        setTxtempAge(objEmployeeMasterTO.getTxtempAge());
        setTxtempPlaceOfBirth(objEmployeeMasterTO.getTxtempPlaceOfBirth());
        setCboEmpReligon(objEmployeeMasterTO.getCboEmpReligon());
        setCboEmpCaste(objEmployeeMasterTO.getCboEmpCaste());
        setTxtEmpHomeTown(objEmployeeMasterTO.getTxtEmpHomeTown());
        setTxtEmpIdCardNo(objEmployeeMasterTO.getTxtEmpIdCardNo());
        setTxtEmpUIdNo(objEmployeeMasterTO.getTxtEmpUIdNo());
        setTxtEmpPanNo(objEmployeeMasterTO.getTxtEmpPanNo());
        setTxtEmpPfNo(objEmployeeMasterTO.getTxtEmpPfNo());
        setCboEmpPfNominee(objEmployeeMasterTO.getCboEmpPfNominee());
        setTdtEmpJoinDate(DateUtil.getStringDate(objEmployeeMasterTO.getTdtEmpJoinDate()));
        setTdtEmpRetirementDate(DateUtil.getStringDate(objEmployeeMasterTO.getTdtEmpRetirementDate()));
        setSysId(objEmployeeMasterTO.getSysId());
        setCboDomicileState(objEmployeeMasterTO.getCboDomicileState());
        setCboBloodGroup(objEmployeeMasterTO.getCboBloodGroup());
        setTxtDrivingLicenceNo(objEmployeeMasterTO.getTxtDrivingLicenceNo());
        setTdtDLRenewalDate(DateUtil.getStringDate(objEmployeeMasterTO.getTdtDLRenewalDate()));
        setTxtMajorHealthProbeem(objEmployeeMasterTO.getTxtMajorHealthProbeem());
        setTxtPhysicalHandicap(objEmployeeMasterTO.getTxtPhysicalHandicap());
        setTxtEmailId(objEmployeeMasterTO.getTxtEmailId());
        
        if (data.containsKey("PHOTOSIGN")) {
            HashMap photoSignMap = (HashMap)data.get("PHOTOSIGN");
            System.out.println("##### photoSignMap : "+photoSignMap);
            //                setPhotoFile(CommonUtil.convertObjToStr(photoSignMap.get("PHOTO")));
            //                setSignFile(CommonUtil.convertObjToStr(photoSignMap.get("SIGN")));
            setPhotoByteArray((byte[])photoSignMap.get("PHOTO"));
            setSignByteArray((byte[])photoSignMap.get("SIGN"));
        }
    }
    
    /**
     * Getter for property sysId.
     * @return Value of property sysId.
     */
    public java.lang.String getSysId() {
        return sysId;
    }
    
    /**
     * Setter for property sysId.
     * @param sysId New value of property sysId.
     */
    public void setSysId(java.lang.String sysId) {
        this.sysId = sysId;
    }
    
    private void updateData() throws Exception{
        setEmployeeMasterDate();
        setPresentDetails();
        //        setPromotionDetails();
        if (txtPassportFirstName.length()>0 && txtPassportNo.length()>0){
            setPassportDetails();
        }
        else{
            objEmployeeMasterPassPortTO=null;
        }
        
        setData();
        
        objEmployeeMasterTO.setCommand(CommonConstants.TOSTATUS_UPDATE);
        objEmployeeMasterTO.setSysId(sysId);
        objEmployeeMasterTO.setStatus(CommonConstants.STATUS_MODIFIED);
        objEmployeeMasterTO.setStatusBy(TrueTransactMain.USER_ID);
        objEmployeeMasterTO.setStatusDt(curDate);
    }
    
    private void deleteData() throws Exception{
        setEmployeeMasterDate();
        setPresentDetails();
        
        if (txtPassportFirstName.length()>0 && txtPassportNo.length()>0){
            setPassportDetails();
        }
        else{
            objEmployeeMasterPassPortTO=null;
        }
        
        setData();
        
        objEmployeeMasterTO.setCommand(CommonConstants.TOSTATUS_DELETE);
        objEmployeeMasterTO.setSysId(sysId);
        objEmployeeMasterTO.setStatus(CommonConstants.STATUS_DELETED);
        objEmployeeMasterTO.setStatusBy(TrueTransactMain.USER_ID);
        objEmployeeMasterTO.setStatusDt(curDate);
    }
    
    /**
     * Getter for property txtPhoneNumber.
     * @return Value of property txtPhoneNumber.
     */
    public java.lang.String getTxtPhoneNumber() {
        return txtPhoneNumber;
    }
    
    /**
     * Setter for property txtPhoneNumber.
     * @param txtPhoneNumber New value of property txtPhoneNumber.
     */
    public void setTxtPhoneNumber(java.lang.String txtPhoneNumber) {
        this.txtPhoneNumber = txtPhoneNumber;
    }
    
    /**
     * Getter for property txtAreaCode.
     * @return Value of property txtAreaCode.
     */
    public java.lang.String getTxtAreaCode() {
        return txtAreaCode;
    }
    
    /**
     * Setter for property txtAreaCode.
     * @param txtAreaCode New value of property txtAreaCode.
     */
    public void setTxtAreaCode(java.lang.String txtAreaCode) {
        this.txtAreaCode = txtAreaCode;
    }
    
    /**
     * Getter for property cboPhoneType.
     * @return Value of property cboPhoneType.
     */
    public java.lang.String getCboPhoneType() {
        return cboPhoneType;
    }
    
    /**
     * Setter for property cboPhoneType.
     * @param cboPhoneType New value of property cboPhoneType.
     */
    public void setCboPhoneType(java.lang.String cboPhoneType) {
        this.cboPhoneType = cboPhoneType;
    }
    
    /**
     * Getter for property cbmPhoneType.
     * @return Value of property cbmPhoneType.
     */
    public com.see.truetransact.clientutil.ComboBoxModel getCbmPhoneType() {
        return cbmPhoneType;
    }
    
    /**
     * Setter for property cbmPhoneType.
     * @param cbmPhoneType New value of property cbmPhoneType.
     */
    public void setCbmPhoneType(com.see.truetransact.clientutil.ComboBoxModel cbmPhoneType) {
        this.cbmPhoneType = cbmPhoneType;
    }
    
    /**
     * Getter for property tblPhoneList.
     * @return Value of property tblPhoneList.
     */
    public com.see.truetransact.clientutil.EnhancedTableModel getTblPhoneList() {
        return tblPhoneList;
    }
    
    /**
     * Setter for property tblPhoneList.
     * @param tblPhoneList New value of property tblPhoneList.
     */
    public void setTblPhoneList(com.see.truetransact.clientutil.EnhancedTableModel tblPhoneList) {
        this.tblPhoneList = tblPhoneList;
        setChanged();
    }
    public void addPhoneList(boolean phoneExist,int phoneRow){
        try{
            EmployeeMasterPhoneTO objEmployeeMasterPhoneTO = new EmployeeMasterPhoneTO();
            if(phoneList == null){
                phoneList = new HashMap();
            }
            if( actionType == ClientConstants.ACTIONTYPE_EDIT ){
                objEmployeeMasterPhoneTO.setSysId(sysId);
            }
            if(phoneExist){
                Double phoneId = ((EmployeeMasterPhoneTO)phoneList.get(tblPhoneList.getValueAt(phoneRow,0))).getPhoneId();
                if(phoneId!=null){
                    objEmployeeMasterPhoneTO.setPhoneId(phoneId);
                }else{
                    objEmployeeMasterPhoneTO.setPhoneId((Double)tblPhoneList.getValueAt(phoneRow,0));
                }
                if(getActionType()==ClientConstants.ACTIONTYPE_EDIT){
                    if(!isNewAddress()){
                        objEmployeeMasterPhoneTO.setStatus(CommonConstants.STATUS_MODIFIED);
                        objEmployeeMasterPhoneTO.setStatusBy(TrueTransactMain.USER_ID);
                        objEmployeeMasterPhoneTO.setStatusDt(curDate);
                    }else{
                        objEmployeeMasterPhoneTO.setStatus(CommonConstants.STATUS_CREATED);
                        objEmployeeMasterPhoneTO.setStatusBy(TrueTransactMain.USER_ID);
                        objEmployeeMasterPhoneTO.setStatusDt(curDate);
                    }
                }
            }else if(phoneExist==false){
                double big = 0;
                if(tblPhoneList.getRowCount()>0){//If rowcont is greater than zero, get the first column value, which gives the
                    //gives the phone type id, so finding the greatest phoneID, and its incremented by one and its set as phoneId for the newly entered phone Details
                    for(int i=0;i<tblPhoneList.getRowCount();i++){
                        Double tblPhoneId = (Double) tblPhoneList.getValueAt(i,0);
                        if(tblPhoneId.doubleValue()>big){
                            big = tblPhoneId.doubleValue();
                        }
                    }
                    objEmployeeMasterPhoneTO.setPhoneId(new Double(big+1));
                }else if(tblPhoneList.getRowCount()==0){
                    objEmployeeMasterPhoneTO.setPhoneId(new Double(phoneRow+1));
                }
                objEmployeeMasterPhoneTO.setStatus(CommonConstants.STATUS_CREATED);
                objEmployeeMasterPhoneTO.setStatusBy(TrueTransactMain.USER_ID);
                //                objEmployeeMasterPhoneTO.setStatusDt(curDate);
            }
            objEmployeeMasterPhoneTO.setPhoneTypeId(CommonUtil.convertObjToStr(getCbmPhoneType().getKeyForSelected()));
            objEmployeeMasterPhoneTO.setAreaCode(txtAreaCode );
            objEmployeeMasterPhoneTO.setPhoneNumber(txtPhoneNumber);
            objEmployeeMasterPhoneTO.setAddrType(CommonUtil.convertObjToStr(getCbmAddressType().getKeyForSelected()));
            phoneList.put(objEmployeeMasterPhoneTO.getPhoneId(),objEmployeeMasterPhoneTO);
            updateTblPhoneList(phoneRow,phoneExist);
            objEmployeeMasterPhoneTO = null;
            resetPhoneDetails();
        }catch(Exception e){
            parseException.logException(e,true);
        }
    }
    
    /** To populate phone rows */
    private void updateTblPhoneList(int row,boolean phoneExist) throws Exception{
        Object selectedRow = null;
        boolean rowExists = false;
        if (phoneExist){
            tblPhoneList.setValueAt(getCboPhoneType(), row, 1);
            tblPhoneList.setValueAt(txtPhoneNumber, row, 2);
        }else{
            final ArrayList phoneRow = new ArrayList();
            double big =0;
            if(tblPhoneList.getRowCount()>0){//If rowcont is greater than zero, get the first column value, which gives the
                //gives the phone type id, so finding the greatest phoneID, and its incremented by one and its set as phoneId for the newly entered phone Details
                for(int i=0;i<tblPhoneList.getRowCount();i++){
                    Double tblPhoneId = (Double) tblPhoneList.getValueAt(i,0);
                    if(tblPhoneId.intValue()>big){
                        big = tblPhoneId.doubleValue();
                    }
                }
                phoneRow.add(new Double(big+1));
            }else if(tblPhoneList.getRowCount()==0){
                phoneRow.add(new Double(row+1));
            }
            phoneRow.add(getCboPhoneType());
            phoneRow.add(txtPhoneNumber);
            tblPhoneList.insertRow(tblPhoneList.getRowCount(),phoneRow);
        }
    }
    
    public void deletePhoneDetails(int row){
        try{
            if(deletedPhoneMap == null){
                deletedPhoneMap = new HashMap();
            }
            final Object selectedRow = ((ArrayList)tblPhoneList.getDataArrayList().get(row)).get(0);
            // LinkedHashMap tempHashMap = new LinkedHashMap();
            EmployeeMasterPhoneTO objEmployeeMasterPhoneTO = (EmployeeMasterPhoneTO) phoneList.get(new Double(CommonUtil.convertObjToStr(selectedRow)));
            objEmployeeMasterPhoneTO.setStatus(CommonConstants.STATUS_DELETED);
            objEmployeeMasterPhoneTO.setStatusBy(TrueTransactMain.USER_ID);
            objEmployeeMasterPhoneTO.setStatusDt(curDate);
            deletedPhoneMap.put(new Double(CommonUtil.convertObjToStr(selectedRow)), phoneList.get(new Double(CommonUtil.convertObjToStr(selectedRow))));
            phoneList.remove(new Double(CommonUtil.convertObjToStr(selectedRow)));
            resetDeletePhoneDetails();
        }catch(Exception e){
            parseException.logException(e,true);
        }
    }
    
    public void resetDeletePhoneDetails(){
        try{
            resetPhoneListTable();
            resetPhoneDetails();
            populatePhoneTable();
            ttNotifyObservers();
        }catch(Exception e){
            parseException.logException(e,true);
        }
    }
    
    public void resetPhoneListTable(){
        for(int i = tblPhoneList.getRowCount(); i > 0; i--){
            tblPhoneList.removeRow(0);
        }
    }
    private void populatePhoneTable() throws Exception{
        EmployeeMasterPhoneTO objEmployeeMasterPhoneTO = null;
        ArrayList phoneRow = null;
        log.info("phoneList:"+phoneList);
        System.out.println("#$#$ phoneList before order : "+phoneList);
        if (phoneList != null){
            final ArrayList keys = new ArrayList(phoneList.keySet());
            ArrayList newKeys = new ArrayList();
            for(int i=0; i<(keys.size()-1);i++){
                for(int j=i+1; j<keys.size();j++){
                    double a = CommonUtil.convertObjToDouble(keys.get(i)).doubleValue();
                    double b = CommonUtil.convertObjToDouble(keys.get(j)).doubleValue();
                    if (a>b) {
                        keys.set(i, new Double(b));
                        keys.set(j, new Double(a));
                    }
                }
            }
            System.out.println("#$#$ phoneList after order : "+phoneList);
            for(int i=0; i<keys.size();i++){
                objEmployeeMasterPhoneTO = (EmployeeMasterPhoneTO)phoneList.get((Double)keys.get(i));
                phoneRow = new ArrayList();
                phoneRow.add(objEmployeeMasterPhoneTO.getPhoneId());
                phoneRow.add(objEmployeeMasterPhoneTO.getPhoneTypeId());
                phoneRow.add(objEmployeeMasterPhoneTO.getPhoneNumber());
                tblPhoneList.insertRow(tblPhoneList.getRowCount(),phoneRow);
                phoneRow = null;
            }
        }
    }
    public void resetNewAddress(){
        resetPhoneListTable();
        resetAddressDetails();
        resetPhoneDetails();
        ttNotifyObservers();
    }
    
    /**
     * Getter for property commAddrType.
     * @return Value of property commAddrType.
     */
    public java.lang.String getCommAddrType() {
        return commAddrType;
    }
    
    /**
     * Setter for property commAddrType.
     * @param commAddrType New value of property commAddrType.
     */
    public void setCommAddrType(java.lang.String commAddrType) {
        this.commAddrType = commAddrType;
    }
    
    public void populatePhone(int row){
        try{
            final Object selectedRow = ((ArrayList)tblPhoneList.getDataArrayList().get(row)).get(0);
            final Double selectedRowValue = CommonUtil.convertObjToDouble(selectedRow);
            EmployeeMasterPhoneTO objEmployeeMasterPhoneTO = (EmployeeMasterPhoneTO)phoneList.get(selectedRowValue);
            populatePhoneData(objEmployeeMasterPhoneTO);
        }catch(Exception e){
            parseException.logException(e,true);
        }
    }
    
    
    private void populatePhoneData(EmployeeMasterPhoneTO objEmployeeMasterPhoneTO)  throws Exception{
        setTxtAreaCode(objEmployeeMasterPhoneTO.getAreaCode());
        setTxtPhoneNumber(objEmployeeMasterPhoneTO.getPhoneNumber());
        setCboPhoneType(CommonUtil.convertObjToStr(cbmPhoneType.getDataForKey(CommonUtil.convertObjToStr(objEmployeeMasterPhoneTO.getPhoneTypeId()))));
    }
    
    /**
     * Getter for property cbmBloodGroup.
     * @return Value of property cbmBloodGroup.
     */
    public com.see.truetransact.clientutil.ComboBoxModel getCbmBloodGroup() {
        return cbmBloodGroup;
    }
    
    /**
     * Setter for property cbmBloodGroup.
     * @param cbmBloodGroup New value of property cbmBloodGroup.
     */
    public void setCbmBloodGroup(com.see.truetransact.clientutil.ComboBoxModel cbmBloodGroup) {
        this.cbmBloodGroup = cbmBloodGroup;
    }
    
    /**
     * Getter for property cbmDomicileState.
     * @return Value of property cbmDomicileState.
     */
    public com.see.truetransact.clientutil.ComboBoxModel getCbmDomicileState() {
        return cbmDomicileState;
    }
    
    /**
     * Setter for property cbmDomicileState.
     * @param cbmDomicileState New value of property cbmDomicileState.
     */
    public void setCbmDomicileState(com.see.truetransact.clientutil.ComboBoxModel cbmDomicileState) {
        this.cbmDomicileState = cbmDomicileState;
    }
    
    /**
     * Getter for property txtMajorHealthProbeem.
     * @return Value of property txtMajorHealthProbeem.
     */
    public java.lang.String getTxtMajorHealthProbeem() {
        return txtMajorHealthProbeem;
    }
    
    /**
     * Setter for property txtMajorHealthProbeem.
     * @param txtMajorHealthProbeem New value of property txtMajorHealthProbeem.
     */
    public void setTxtMajorHealthProbeem(java.lang.String txtMajorHealthProbeem) {
        this.txtMajorHealthProbeem = txtMajorHealthProbeem;
    }
    
    
    /**
     * Getter for property cboBloodGroup.
     * @return Value of property cboBloodGroup.
     */
    public java.lang.String getCboBloodGroup() {
        return cboBloodGroup;
    }
    
    /**
     * Setter for property cboBloodGroup.
     * @param cboBloodGroup New value of property cboBloodGroup.
     */
    public void setCboBloodGroup(java.lang.String cboBloodGroup) {
        this.cboBloodGroup = cboBloodGroup;
    }
    
    /**
     * Getter for property txtDrivingLicenceNo.
     * @return Value of property txtDrivingLicenceNo.
     */
    public java.lang.String getTxtDrivingLicenceNo() {
        return txtDrivingLicenceNo;
    }
    
    /**
     * Setter for property txtDrivingLicenceNo.
     * @param txtDrivingLicenceNo New value of property txtDrivingLicenceNo.
     */
    public void setTxtDrivingLicenceNo(java.lang.String txtDrivingLicenceNo) {
        this.txtDrivingLicenceNo = txtDrivingLicenceNo;
    }
    
    /**
     * Getter for property tdtDLRenewalDate.
     * @return Value of property tdtDLRenewalDate.
     */
    public java.lang.String getTdtDLRenewalDate() {
        return tdtDLRenewalDate;
    }
    
    /**
     * Setter for property tdtDLRenewalDate.
     * @param tdtDLRenewalDate New value of property tdtDLRenewalDate.
     */
    public void setTdtDLRenewalDate(java.lang.String tdtDLRenewalDate) {
        this.tdtDLRenewalDate = tdtDLRenewalDate;
    }
    
    /**
     * Getter for property txtEmailId.
     * @return Value of property txtEmailId.
     */
    public java.lang.String getTxtEmailId() {
        return txtEmailId;
    }
    
    /**
     * Setter for property txtEmailId.
     * @param txtEmailId New value of property txtEmailId.
     */
    public void setTxtEmailId(java.lang.String txtEmailId) {
        this.txtEmailId = txtEmailId;
    }
    
    /**
     * Getter for property cboDomicileState.
     * @return Value of property cboDomicileState.
     */
    public java.lang.String getCboDomicileState() {
        return cboDomicileState;
    }
    
    /**
     * Setter for property cboDomicileState.
     * @param cboDomicileState New value of property cboDomicileState.
     */
    public void setCboDomicileState(java.lang.String cboDomicileState) {
        this.cboDomicileState = cboDomicileState;
    }
    
    /**
     * Getter for property cbmPhysicalHandicap.
     * @return Value of property cbmPhysicalHandicap.
     */
    
    public java.lang.String getTxtRelativeStaffId() {
        return txtRelativeStaffId;
    }
    
    /**
     * Setter for property txtRelativeStaffId.
     * @param txtRelativeStaffId New value of property txtRelativeStaffId.
     */
    public void setTxtRelativeStaffId(java.lang.String txtRelativeStaffId) {
        this.txtRelativeStaffId = txtRelativeStaffId;
        setChanged();
    }
    
    /**
     * Getter for property cboRelativeTittle.
     * @return Value of property cboRelativeTittle.
     */
    public java.lang.String getCboRelativeTittle() {
        return cboRelativeTittle;
    }
    
    /**
     * Setter for property cboRelativeTittle.
     * @param cboRelativeTittle New value of property cboRelativeTittle.
     */
    public void setCboRelativeTittle(java.lang.String cboRelativeTittle) {
        this.cboRelativeTittle = cboRelativeTittle;
    }
    
    /**
     * Getter for property txtReleativeFirstName.
     * @return Value of property txtReleativeFirstName.
     */
    public java.lang.String getTxtReleativeFirstName() {
        return txtReleativeFirstName;
    }
    
    /**
     * Setter for property txtReleativeFirstName.
     * @param txtReleativeFirstName New value of property txtReleativeFirstName.
     */
    public void setTxtReleativeFirstName(java.lang.String txtReleativeFirstName) {
        this.txtReleativeFirstName = txtReleativeFirstName;
    }
    
    /**
     * Getter for property txtReleativeMiddleName.
     * @return Value of property txtReleativeMiddleName.
     */
    public java.lang.String getTxtReleativeMiddleName() {
        return txtReleativeMiddleName;
    }
    
    /**
     * Setter for property txtReleativeMiddleName.
     * @param txtReleativeMiddleName New value of property txtReleativeMiddleName.
     */
    public void setTxtReleativeMiddleName(java.lang.String txtReleativeMiddleName) {
        this.txtReleativeMiddleName = txtReleativeMiddleName;
    }
    
    /**
     * Getter for property txtReleativeLastName.
     * @return Value of property txtReleativeLastName.
     */
    public java.lang.String getTxtReleativeLastName() {
        return txtReleativeLastName;
    }
    
    /**
     * Setter for property txtReleativeLastName.
     * @param txtReleativeLastName New value of property txtReleativeLastName.
     */
    public void setTxtReleativeLastName(java.lang.String txtReleativeLastName) {
        this.txtReleativeLastName = txtReleativeLastName;
    }
    
    /**
     * Getter for property txtReleativeDisg.
     * @return Value of property txtReleativeDisg.
     */
    //    public java.lang.String getTxtReleativeDisg() {
    //        return txtReleativeDisg;
    //    }
    //
    /**
     * Setter for property txtReleativeDisg.
     * @param txtReleativeDisg New value of property txtReleativeDisg.
     */
    //    public void setTxtReleativeDisg(java.lang.String txtReleativeDisg) {
    //        this.txtReleativeDisg = txtReleativeDisg;
    //    }
    
    /**
     * Getter for property txtReleativeBranchId.
     * @return Value of property txtReleativeBranchId.
     */
    //    public java.lang.String getTxtReleativeBranchId() {
    //        return txtReleativeBranchId;
    //    }
    //
    /**
     * Setter for property txtReleativeBranchId.
     * @param txtReleativeBranchId New value of property txtReleativeBranchId.
     */
    //    public void setTxtReleativeBranchId(java.lang.String txtReleativeBranchId) {
    //        this.txtReleativeBranchId = txtReleativeBranchId;
    //    }
    
    /**
     * Getter for property txtReleativeReleationShip.
     * @return Value of property txtReleativeReleationShip.
     */
    //    public java.lang.String getTxtReleativeReleationShip() {
    //        return txtReleativeReleationShip;
    //    }
    //
    //    /**
    //     * Setter for property txtReleativeReleationShip.
    //     * @param txtReleativeReleationShip New value of property txtReleativeReleationShip.
    //     */
    //    public void setTxtReleativeReleationShip(java.lang.String txtReleativeReleationShip) {
    //        this.txtReleativeReleationShip = txtReleativeReleationShip;
    //    }
    
    /**
     * Getter for property cboDirectorTittle.
     * @return Value of property cboDirectorTittle.
     */
    public java.lang.String getCboDirectorTittle() {
        return cboDirectorTittle;
    }
    
    /**
     * Setter for property cboDirectorTittle.
     * @param cboDirectorTittle New value of property cboDirectorTittle.
     */
    public void setCboDirectorTittle(java.lang.String cboDirectorTittle) {
        this.cboDirectorTittle = cboDirectorTittle;
    }
    
    /**
     * Getter for property txtDirectorFirstName.
     * @return Value of property txtDirectorFirstName.
     */
    public java.lang.String getTxtDirectorFirstName() {
        return txtDirectorFirstName;
    }
    
    /**
     * Setter for property txtDirectorFirstName.
     * @param txtDirectorFirstName New value of property txtDirectorFirstName.
     */
    public void setTxtDirectorFirstName(java.lang.String txtDirectorFirstName) {
        this.txtDirectorFirstName = txtDirectorFirstName;
    }
    
    /**
     * Getter for property txtDirectorMiddleName.
     * @return Value of property txtDirectorMiddleName.
     */
    public java.lang.String getTxtDirectorMiddleName() {
        return txtDirectorMiddleName;
    }
    
    /**
     * Setter for property txtDirectorMiddleName.
     * @param txtDirectorMiddleName New value of property txtDirectorMiddleName.
     */
    public void setTxtDirectorMiddleName(java.lang.String txtDirectorMiddleName) {
        this.txtDirectorMiddleName = txtDirectorMiddleName;
    }
    
    /**
     * Getter for property txtDirectorLastName.
     * @return Value of property txtDirectorLastName.
     */
    public java.lang.String getTxtDirectorLastName() {
        return txtDirectorLastName;
    }
    
    /**
     * Setter for property txtDirectorLastName.
     * @param txtDirectorLastName New value of property txtDirectorLastName.
     */
    public void setTxtDirectorLastName(java.lang.String txtDirectorLastName) {
        this.txtDirectorLastName = txtDirectorLastName;
    }
    
    /**
     * Getter for property txtDirectorReleationShip.
     * @return Value of property txtDirectorReleationShip.
     */
    public java.lang.String getTxtDirectorReleationShip() {
        return txtDirectorReleationShip;
    }
    
    /**
     * Setter for property txtDirectorReleationShip.
     * @param txtDirectorReleationShip New value of property txtDirectorReleationShip.
     */
    public void setTxtDirectorReleationShip(java.lang.String txtDirectorReleationShip) {
        this.txtDirectorReleationShip = txtDirectorReleationShip;
    }
    
    /**
     * Getter for property cbmDirectorTittle.
     * @return Value of property cbmDirectorTittle.
     */
    public com.see.truetransact.clientutil.ComboBoxModel getCbmDirectorTittle() {
        return cbmDirectorTittle;
    }
    
    /**
     * Setter for property cbmDirectorTittle.
     * @param cbmDirectorTittle New value of property cbmDirectorTittle.
     */
    public void setCbmDirectorTittle(com.see.truetransact.clientutil.ComboBoxModel cbmDirectorTittle) {
        this.cbmDirectorTittle = cbmDirectorTittle;
    }
    
    /**
     * Getter for property cbmReleativeTittle.
     * @return Value of property cbmReleativeTittle.
     */
    public com.see.truetransact.clientutil.ComboBoxModel getCbmReleativeTittle() {
        return cbmReleativeTittle;
    }
    
    /**
     * Setter for property cbmReleativeTittle.
     * @param cbmReleativeTittle New value of property cbmReleativeTittle.
     */
    public void setCbmReleativeTittle(com.see.truetransact.clientutil.ComboBoxModel cbmReleativeTittle) {
        this.cbmReleativeTittle = cbmReleativeTittle;
    }
    
    /**
     * Getter for property cbmReleativeBranchId.
     * @return Value of property cbmReleativeBranchId.
     */
    public com.see.truetransact.clientutil.ComboBoxModel getCbmReleativeBranchId() {
        return cbmReleativeBranchId;
    }
    
    /**
     * Setter for property cbmReleativeBranchId.
     * @param cbmReleativeBranchId New value of property cbmReleativeBranchId.
     */
    public void setCbmReleativeBranchId(com.see.truetransact.clientutil.ComboBoxModel cbmReleativeBranchId) {
        this.cbmReleativeBranchId = cbmReleativeBranchId;
    }
    
    /**
     * Getter for property cbmReleativeDisg.
     * @return Value of property cbmReleativeDisg.
     */
    public com.see.truetransact.clientutil.ComboBoxModel getCbmReleativeDisg() {
        return cbmReleativeDisg;
    }
    
    /**
     * Setter for property cbmReleativeDisg.
     * @param cbmReleativeDisg New value of property cbmReleativeDisg.
     */
    public void setCbmReleativeDisg(com.see.truetransact.clientutil.ComboBoxModel cbmReleativeDisg) {
        this.cbmReleativeDisg = cbmReleativeDisg;
    }
    
    /**
     * Getter for property cbmDirectorReleationShip.
     * @return Value of property cbmDirectorReleationShip.
     */
    public com.see.truetransact.clientutil.ComboBoxModel getCbmDirectorReleationShip() {
        return cbmDirectorReleationShip;
    }
    
    /**
     * Setter for property cbmDirectorReleationShip.
     * @param cbmDirectorReleationShip New value of property cbmDirectorReleationShip.
     */
    public void setCbmDirectorReleationShip(com.see.truetransact.clientutil.ComboBoxModel cbmDirectorReleationShip) {
        this.cbmDirectorReleationShip = cbmDirectorReleationShip;
    }
    
    /**
     * Getter for property cbmReleativeReleationShip.
     * @return Value of property cbmReleativeReleationShip.
     */
    public com.see.truetransact.clientutil.ComboBoxModel getCbmReleativeReleationShip() {
        return cbmReleativeReleationShip;
    }
    
    /**
     * Setter for property cbmReleativeReleationShip.
     * @param cbmReleativeReleationShip New value of property cbmReleativeReleationShip.
     */
    public void setCbmReleativeReleationShip(com.see.truetransact.clientutil.ComboBoxModel cbmReleativeReleationShip) {
        this.cbmReleativeReleationShip = cbmReleativeReleationShip;
    }
    
    /**
     * Getter for property relativeChanged.
     * @return Value of property relativeChanged.
     */
    public boolean isRelativeChanged() {
        return relativeChanged;
    }
    
    /**
     * Setter for property relativeChanged.
     * @param relativeChanged New value of property relativeChanged.
     */
    public void setRelativeChanged(boolean relativeChanged) {
        this.relativeChanged = relativeChanged;
    }
    
    /**
     * Getter for property releativeSysId.
     * @return Value of property releativeSysId.
     */
    public java.lang.String getReleativeSysId() {
        return releativeSysId;
    }
    
    /**
     * Setter for property releativeSysId.
     * @param releativeSysId New value of property releativeSysId.
     */
    public void setReleativeSysId(java.lang.String releativeSysId) {
        this.releativeSysId = releativeSysId;
    }
    
    /**
     * Getter for property tblReleativeWorkingInBank.
     * @return Value of property tblReleativeWorkingInBank.
     */
    public com.see.truetransact.clientutil.EnhancedTableModel getTblReleativeWorkingInBank() {
        return tblReleativeWorkingInBank;
    }
    
    /**
     * Setter for property tblReleativeWorkingInBank.
     * @param tblReleativeWorkingInBank New value of property tblReleativeWorkingInBank.
     */
    public void setTblReleativeWorkingInBank(com.see.truetransact.clientutil.EnhancedTableModel tblReleativeWorkingInBank) {
        this.tblReleativeWorkingInBank = tblReleativeWorkingInBank;
    }
    
    
    public void  director(int row,boolean directorDetailsFlag){
        try{
            final EmployeeRelativeDirectorTO objEmployeeRelativeDirectorTO=new EmployeeRelativeDirectorTO();
            if( directorMap == null ){
                directorMap = new LinkedHashMap();
            }
            
            if(getActionType()==ClientConstants.ACTIONTYPE_EDIT){
                if(isNewDirector()){
                    objEmployeeRelativeDirectorTO.setStatus(CommonConstants.STATUS_CREATED);
                    objEmployeeRelativeDirectorTO.setStatusBy(TrueTransactMain.USER_ID);
                    objEmployeeRelativeDirectorTO.setStatusDt(curDate);
                }else{
                    objEmployeeRelativeDirectorTO.setStatus(CommonConstants.STATUS_MODIFIED);
                    objEmployeeRelativeDirectorTO.setStatusBy(TrueTransactMain.USER_ID);
                    objEmployeeRelativeDirectorTO.setStatusDt(curDate);
                }
            }else{
                objEmployeeRelativeDirectorTO.setStatus(CommonConstants.STATUS_CREATED);
            }
            if( actionType == ClientConstants.ACTIONTYPE_EDIT ){
                objEmployeeRelativeDirectorTO.setSysId(sysId);
            }
            int slno;
            slno=0;
            
            if(!directorDetailsFlag){
                
                ArrayList data = tblDetailsOfRelativeDirector.getDataArrayList();
                slno=serialNo(data,tblDetailsOfRelativeDirector);
            }
            else if(isNewDirector()){
                int b=CommonUtil.convertObjToInt(tblDetailsOfRelativeDirector.getValueAt(row,0));
                slno= b + tblDetailsOfRelativeDirector.getRowCount();
            }
            else{
                slno = CommonUtil.convertObjToInt(tblDetailsOfRelativeDirector.getValueAt(row,0));
            }
            objEmployeeRelativeDirectorTO.setDirectorID(String.valueOf(slno));
            objEmployeeRelativeDirectorTO.setDirectorTittle(getCboDirectorTittle());
            objEmployeeRelativeDirectorTO.setDirectorFirstName(getTxtDirectorFirstName());
            objEmployeeRelativeDirectorTO.setDirectorMiddleName(getTxtDirectorMiddleName());
            objEmployeeRelativeDirectorTO.setDirectorRelationShip(getTxtDirectorReleationShip());
            objEmployeeRelativeDirectorTO.setDirectorLastName(getTxtDirectorLastName());
            
            objEmployeeRelativeDirectorTO.setStatusBy(TrueTransactMain.USER_ID);
            //            directorMap.put(CommonUtil.convertObjToStr(getDirectorID()), objEmployeeRelativeDirectorTO);
            directorMap.put(String.valueOf(slno), objEmployeeRelativeDirectorTO);
            
            updateTblDirectorList(row,objEmployeeRelativeDirectorTO);
            //            ttNotifyObservers();
        }catch(Exception e){
            parseException.logException(e,true);
        }
        
    }
    
    
    
    private void updateTblDirectorList(int row,EmployeeRelativeDirectorTO objEmployeeRelativeDirectorTO)  throws Exception{
        Object selectedRow;
        boolean rowExists = false;
        //If row already exists update it, else create a new row & append
        for(int i = tblDetailsOfRelativeDirector.getRowCount(), j = 0; i > 0; i--,j++){
            selectedRow = ((ArrayList)tblDetailsOfRelativeDirector.getDataArrayList().get(j)).get(0);
            if( CommonUtil.convertObjToStr(getDirectorID()).equals(CommonUtil.convertObjToStr(selectedRow))){
                rowExists = true;
            }
        }
        ArrayList addressRow = new ArrayList();
        if(row == -1){
            addressRow.add(CommonUtil.convertObjToStr(objEmployeeRelativeDirectorTO.getDirectorID()));
            addressRow.add(CommonUtil.convertObjToStr(getTxtDirectorFirstName()));
            
            tblDetailsOfRelativeDirector.insertRow(tblDetailsOfRelativeDirector.getRowCount(),addressRow);
            addressRow = null;
        }else{
            tblDetailsOfRelativeDirector.removeRow(row);
            addressRow.add(CommonUtil.convertObjToStr(objEmployeeRelativeDirectorTO.getDirectorID()));
            addressRow.add(CommonUtil.convertObjToStr(getTxtDirectorFirstName()));
            tblDetailsOfRelativeDirector.insertRow(row,addressRow);
            addressRow = null;
        }
    }
    
    /**
     * Getter for property directorChanged.
     * @return Value of property directorChanged.
     */
    public boolean isDirectorChanged() {
        return directorChanged;
    }
    
    /**
     * Setter for property directorChanged.
     * @param directorChanged New value of property directorChanged.
     */
    public void setDirectorChanged(boolean directorChanged) {
        this.directorChanged = directorChanged;
    }
    
    /**
     * Getter for property tblDetailsOfRelativeDirector.
     * @return Value of property tblDetailsOfRelativeDirector.
     */
    public com.see.truetransact.clientutil.EnhancedTableModel getTblDetailsOfRelativeDirector() {
        return tblDetailsOfRelativeDirector;
    }
    
    /**
     * Setter for property tblDetailsOfRelativeDirector.
     * @param tblDetailsOfRelativeDirector New value of property tblDetailsOfRelativeDirector.
     */
    public void setTblDetailsOfRelativeDirector(com.see.truetransact.clientutil.EnhancedTableModel tblDetailsOfRelativeDirector) {
        this.tblDetailsOfRelativeDirector = tblDetailsOfRelativeDirector;
    }
    
    public void deleteDirector(int row){
        if(deletedDirectorMap == null){
            deletedDirectorMap = new HashMap();
        }
        
        EmployeeRelativeDirectorTO objEmployeeRelativeDirectorTO= (EmployeeRelativeDirectorTO)directorMap.get(CommonUtil.convertObjToStr(tblDetailsOfRelativeDirector.getValueAt(row,ADDRTYPE_COLNO)));
        objEmployeeRelativeDirectorTO.setStatus(CommonConstants.STATUS_DELETED);
        objEmployeeRelativeDirectorTO.setStatusDt(curDate);
        objEmployeeRelativeDirectorTO.setStatusBy(TrueTransactMain.USER_ID);
        deletedDirectorMap.put(CommonUtil.convertObjToStr(tblDetailsOfRelativeDirector.getValueAt(row,ADDRTYPE_COLNO)),directorMap.get(CommonUtil.convertObjToStr(tblDetailsOfRelativeDirector.getValueAt(row,ADDRTYPE_COLNO))));
        directorMap.remove(tblDetailsOfRelativeDirector.getValueAt(row,ADDRTYPE_COLNO));
        resetDeleteDirector();
        //          ttNotifyObservers();
    }
    
    public void populateDirector(int row){
        try{
            directorTypeChanged(CommonUtil.convertObjToStr(tblDetailsOfRelativeDirector.getValueAt(row,ADDRTYPE_COLNO)));
        }catch(Exception e){
            parseException.logException(e,true);
        }
    }
    
    
    
    
    public void directorTypeChanged(String selectedItem){
        try{
            
            technicalTypeExists = true;
            
            final EmployeeRelativeDirectorTO objEmployeeRelativeDirectorTO= (EmployeeRelativeDirectorTO)directorMap.get(selectedItem);
            populateDirectorData(objEmployeeRelativeDirectorTO);
            ttNotifyObservers();
        }catch(Exception e){
            parseException.logException(e,true);
        }
    }
    
    
    private void populateDirectorData(EmployeeRelativeDirectorTO objEmployeeRelativeDirectorTO)  throws Exception{
        try{
            if(objEmployeeRelativeDirectorTO != null){
                
                setDirectorID(objEmployeeRelativeDirectorTO.getDirectorID());
                setCboDirectorTittle(CommonUtil.convertObjToStr(objEmployeeRelativeDirectorTO.getDirectorTittle()));
                setTxtDirectorFirstName(CommonUtil.convertObjToStr(objEmployeeRelativeDirectorTO.getDirectorFirstName()));
                setTxtDirectorMiddleName(CommonUtil.convertObjToStr(objEmployeeRelativeDirectorTO.getDirectorMiddleName()));
                setTxtDirectorLastName(CommonUtil.convertObjToStr(objEmployeeRelativeDirectorTO.getDirectorLastName()));
                setTxtDirectorReleationShip(CommonUtil.convertObjToStr(objEmployeeRelativeDirectorTO.getDirectorRelationShip()));
                
                
                
            }
        }catch(Exception e){
            e.printStackTrace();
        }
    }
    
    /**
     * Getter for property rdoGradutionIncrementYesNo.
     * @return Value of property rdoGradutionIncrementYesNo.
     */
    public java.lang.String getRdoGradutionIncrementYesNo() {
        return rdoGradutionIncrementYesNo;
    }
    
    /**
     * Setter for property rdoGradutionIncrementYesNo.
     * @param rdoGradutionIncrementYesNo New value of property rdoGradutionIncrementYesNo.
     */
    public void setRdoGradutionIncrementYesNo(java.lang.String rdoGradutionIncrementYesNo) {
        this.rdoGradutionIncrementYesNo = rdoGradutionIncrementYesNo;
    }
    
    /**
     * Getter for property tdtGradutionIncrementReleasedDate.
     * @return Value of property tdtGradutionIncrementReleasedDate.
     */
    public java.lang.String getTdtGradutionIncrementReleasedDate() {
        return tdtGradutionIncrementReleasedDate;
    }
    
    /**
     * Setter for property tdtGradutionIncrementReleasedDate.
     * @param tdtGradutionIncrementReleasedDate New value of property tdtGradutionIncrementReleasedDate.
     */
    public void setTdtGradutionIncrementReleasedDate(java.lang.String tdtGradutionIncrementReleasedDate) {
        this.tdtGradutionIncrementReleasedDate = tdtGradutionIncrementReleasedDate;
    }
    
    /**
     * Getter for property rdoCAIIBPART1IncrementYesNo.
     * @return Value of property rdoCAIIBPART1IncrementYesNo.
     */
    public java.lang.String getRdoCAIIBPART1IncrementYesNo() {
        return rdoCAIIBPART1IncrementYesNo;
    }
    
    /**
     * Setter for property rdoCAIIBPART1IncrementYesNo.
     * @param rdoCAIIBPART1IncrementYesNo New value of property rdoCAIIBPART1IncrementYesNo.
     */
    public void setRdoCAIIBPART1IncrementYesNo(java.lang.String rdoCAIIBPART1IncrementYesNo) {
        this.rdoCAIIBPART1IncrementYesNo = rdoCAIIBPART1IncrementYesNo;
    }
    
    /**
     * Getter for property tdtCAIIBPART1IncrementReleasedDate.
     * @return Value of property tdtCAIIBPART1IncrementReleasedDate.
     */
    public java.lang.String getTdtCAIIBPART1IncrementReleasedDate() {
        return tdtCAIIBPART1IncrementReleasedDate;
    }
    
    /**
     * Setter for property tdtCAIIBPART1IncrementReleasedDate.
     * @param tdtCAIIBPART1IncrementReleasedDate New value of property tdtCAIIBPART1IncrementReleasedDate.
     */
    public void setTdtCAIIBPART1IncrementReleasedDate(java.lang.String tdtCAIIBPART1IncrementReleasedDate) {
        this.tdtCAIIBPART1IncrementReleasedDate = tdtCAIIBPART1IncrementReleasedDate;
    }
    
    /**
     * Getter for property rdoCAIIBPART2IncrementYesNo.
     * @return Value of property rdoCAIIBPART2IncrementYesNo.
     */
    public java.lang.String getRdoCAIIBPART2IncrementYesNo() {
        return rdoCAIIBPART2IncrementYesNo;
    }
    
    /**
     * Setter for property rdoCAIIBPART2IncrementYesNo.
     * @param rdoCAIIBPART2IncrementYesNo New value of property rdoCAIIBPART2IncrementYesNo.
     */
    public void setRdoCAIIBPART2IncrementYesNo(java.lang.String rdoCAIIBPART2IncrementYesNo) {
        this.rdoCAIIBPART2IncrementYesNo = rdoCAIIBPART2IncrementYesNo;
    }
    
    /**
     * Getter for property tdtCAIIBPART2IncrementReleasedDate.
     * @return Value of property tdtCAIIBPART2IncrementReleasedDate.
     */
    public java.lang.String getTdtCAIIBPART2IncrementReleasedDate() {
        return tdtCAIIBPART2IncrementReleasedDate;
    }
    
    /**
     * Setter for property tdtCAIIBPART2IncrementReleasedDate.
     * @param tdtCAIIBPART2IncrementReleasedDate New value of property tdtCAIIBPART2IncrementReleasedDate.
     */
    public void setTdtCAIIBPART2IncrementReleasedDate(java.lang.String tdtCAIIBPART2IncrementReleasedDate) {
        this.tdtCAIIBPART2IncrementReleasedDate = tdtCAIIBPART2IncrementReleasedDate;
    }
    
    /**
     * Getter for property rdoAnyOtherIncrementYesNo.
     * @return Value of property rdoAnyOtherIncrementYesNo.
     */
    public java.lang.String getRdoAnyOtherIncrementYesNo() {
        return rdoAnyOtherIncrementYesNo;
    }
    
    /**
     * Setter for property rdoAnyOtherIncrementYesNo.
     * @param rdoAnyOtherIncrementYesNo New value of property rdoAnyOtherIncrementYesNo.
     */
    public void setRdoAnyOtherIncrementYesNo(java.lang.String rdoAnyOtherIncrementYesNo) {
        this.rdoAnyOtherIncrementYesNo = rdoAnyOtherIncrementYesNo;
    }
    
    /**
     * Getter for property txtAnyOtherIncrementInstitutionName.
     * @return Value of property txtAnyOtherIncrementInstitutionName.
     */
    public java.lang.String getTxtAnyOtherIncrementInstitutionName() {
        return txtAnyOtherIncrementInstitutionName;
    }
    
    /**
     * Setter for property txtAnyOtherIncrementInstitutionName.
     * @param txtAnyOtherIncrementInstitutionName New value of property txtAnyOtherIncrementInstitutionName.
     */
    public void setTxtAnyOtherIncrementInstitutionName(java.lang.String txtAnyOtherIncrementInstitutionName) {
        this.txtAnyOtherIncrementInstitutionName = txtAnyOtherIncrementInstitutionName;
    }
    
    /**
     * Getter for property tdtAnyOtherIncrementReleasedDate.
     * @return Value of property tdtAnyOtherIncrementReleasedDate.
     */
    public java.lang.String getTdtAnyOtherIncrementReleasedDate() {
        return tdtAnyOtherIncrementReleasedDate;
    }
    
    /**
     * Setter for property tdtAnyOtherIncrementReleasedDate.
     * @param tdtAnyOtherIncrementReleasedDate New value of property tdtAnyOtherIncrementReleasedDate.
     */
    public void setTdtAnyOtherIncrementReleasedDate(java.lang.String tdtAnyOtherIncrementReleasedDate) {
        this.tdtAnyOtherIncrementReleasedDate = tdtAnyOtherIncrementReleasedDate;
    }
    
    /**
     * Getter for property txtPresentBasic.
     * @return Value of property txtPresentBasic.
     */
    public java.lang.String getTxtPresentBasic() {
        return txtPresentBasic;
    }
    
    /**
     * Setter for property txtPresentBasic.
     * @param txtPresentBasic New value of property txtPresentBasic.
     */
    public void setTxtPresentBasic(java.lang.String txtPresentBasic) {
        this.txtPresentBasic = txtPresentBasic;
    }
    
    /**
     * Getter for property tdtLastIncrmentDate.
     * @return Value of property tdtLastIncrmentDate.
     */
    public java.lang.String getTdtLastIncrmentDate() {
        return tdtLastIncrmentDate;
    }
    
    /**
     * Setter for property tdtLastIncrmentDate.
     * @param tdtLastIncrmentDate New value of property tdtLastIncrmentDate.
     */
    public void setTdtLastIncrmentDate(java.lang.String tdtLastIncrmentDate) {
        this.tdtLastIncrmentDate = tdtLastIncrmentDate;
    }
    
    /**
     * Getter for property txtLossPay_Months.
     * @return Value of property txtLossPay_Months.
     */
    public java.lang.String getTxtLossPay_Months() {
        return txtLossPay_Months;
    }
    
    /**
     * Setter for property txtLossPay_Months.
     * @param txtLossPay_Months New value of property txtLossPay_Months.
     */
    public void setTxtLossPay_Months(java.lang.String txtLossPay_Months) {
        this.txtLossPay_Months = txtLossPay_Months;
    }
    
    /**
     * Getter for property txtLossOfpay_Days.
     * @return Value of property txtLossOfpay_Days.
     */
    public java.lang.String getTxtLossOfpay_Days() {
        return txtLossOfpay_Days;
    }
    
    /**
     * Setter for property txtLossOfpay_Days.
     * @param txtLossOfpay_Days New value of property txtLossOfpay_Days.
     */
    public void setTxtLossOfpay_Days(java.lang.String txtLossOfpay_Days) {
        this.txtLossOfpay_Days = txtLossOfpay_Days;
    }
    
    /**
     * Getter for property tdtNextIncrmentDate.
     * @return Value of property tdtNextIncrmentDate.
     */
    public java.lang.String getTdtNextIncrmentDate() {
        return tdtNextIncrmentDate;
    }
    
    /**
     * Setter for property tdtNextIncrmentDate.
     * @param tdtNextIncrmentDate New value of property tdtNextIncrmentDate.
     */
    public void setTdtNextIncrmentDate(java.lang.String tdtNextIncrmentDate) {
        this.tdtNextIncrmentDate = tdtNextIncrmentDate;
    }
    
    /**
     * Getter for property rdoSigNoYesNo.
     * @return Value of property rdoSigNoYesNo.
     */
    public java.lang.String getRdoSigNoYesNo() {
        return rdoSigNoYesNo;
    }
    
    /**
     * Setter for property rdoSigNoYesNo.
     * @param rdoSigNoYesNo New value of property rdoSigNoYesNo.
     */
    public void setRdoSigNoYesNo(java.lang.String rdoSigNoYesNo) {
        this.rdoSigNoYesNo = rdoSigNoYesNo;
    }
    
    /**
     * Getter for property txtSignatureNo.
     * @return Value of property txtSignatureNo.
     */
    public java.lang.String getTxtSignatureNo() {
        return txtSignatureNo;
    }
    
    /**
     * Setter for property txtSignatureNo.
     * @param txtSignatureNo New value of property txtSignatureNo.
     */
    public void setTxtSignatureNo(java.lang.String txtSignatureNo) {
        this.txtSignatureNo = txtSignatureNo;
    }
    
    /**
     * Getter for property cboUnionMember.
     * @return Value of property cboUnionMember.
     */
    public java.lang.String getCboUnionMember() {
        return cboUnionMember;
    }
    
    /**
     * Setter for property cboUnionMember.
     * @param cboUnionMember New value of property cboUnionMember.
     */
    public void setCboUnionMember(java.lang.String cboUnionMember) {
        this.cboUnionMember = cboUnionMember;
    }
    
    /**
     * Getter for property cboSocietyMember.
     * @return Value of property cboSocietyMember.
     */
    public java.lang.String getCboSocietyMember() {
        return cboSocietyMember;
    }
    
    /**
     * Setter for property cboSocietyMember.
     * @param cboSocietyMember New value of property cboSocietyMember.
     */
    public void setCboSocietyMember(java.lang.String cboSocietyMember) {
        this.cboSocietyMember = cboSocietyMember;
    }
    
    /**
     * Getter for property societyMemberNo.
     * @return Value of property societyMemberNo.
     */
    public java.lang.String getSocietyMemberNo() {
        return societyMemberNo;
    }
    
    /**
     * Setter for property societyMemberNo.
     * @param societyMemberNo New value of property societyMemberNo.
     */
    public void setSocietyMemberNo(java.lang.String societyMemberNo) {
        this.societyMemberNo = societyMemberNo;
    }
    
    /**
     * Getter for property clubMembership.
     * @return Value of property clubMembership.
     */
    public java.lang.String getClubMembership() {
        return clubMembership;
    }
    
    /**
     * Setter for property clubMembership.
     * @param clubMembership New value of property clubMembership.
     */
    public void setClubMembership(java.lang.String clubMembership) {
        this.clubMembership = clubMembership;
    }
    
    /**
     * Getter for property clubName.
     * @return Value of property clubName.
     */
    public java.lang.String getClubName() {
        return clubName;
    }
    
    /**
     * Setter for property clubName.
     * @param clubName New value of property clubName.
     */
    public void setClubName(java.lang.String clubName) {
        this.clubName = clubName;
    }
    
    /**
     * Getter for property txtProbationPeriod.
     * @return Value of property txtProbationPeriod.
     */
    public java.lang.String getTxtProbationPeriod() {
        return txtProbationPeriod;
    }
    
    /**
     * Setter for property txtProbationPeriod.
     * @param txtProbationPeriod New value of property txtProbationPeriod.
     */
    public void setTxtProbationPeriod(java.lang.String txtProbationPeriod) {
        this.txtProbationPeriod = txtProbationPeriod;
    }
    
    /**
     * Getter for property cboProbationPeriod.
     * @return Value of property cboProbationPeriod.
     */
    public java.lang.String getCboProbationPeriod() {
        return cboProbationPeriod;
    }
    
    /**
     * Setter for property cboProbationPeriod.
     * @param cboProbationPeriod New value of property cboProbationPeriod.
     */
    public void setCboProbationPeriod(java.lang.String cboProbationPeriod) {
        this.cboProbationPeriod = cboProbationPeriod;
    }
    
    /**
     * Getter for property tdtConfirmationDate.
     * @return Value of property tdtConfirmationDate.
     */
    public java.lang.String getTdtConfirmationDate() {
        return tdtConfirmationDate;
    }
    
    /**
     * Setter for property tdtConfirmationDate.
     * @param tdtConfirmationDate New value of property tdtConfirmationDate.
     */
    public void setTdtConfirmationDate(java.lang.String tdtConfirmationDate) {
        this.tdtConfirmationDate = tdtConfirmationDate;
    }
    
    /**
     * Getter for property tdtDateofRetirement.
     * @return Value of property tdtDateofRetirement.
     */
    public java.lang.String getTdtDateofRetirement() {
        return tdtDateofRetirement;
    }
    
    /**
     * Setter for property tdtDateofRetirement.
     * @param tdtDateofRetirement New value of property tdtDateofRetirement.
     */
    public void setTdtDateofRetirement(java.lang.String tdtDateofRetirement) {
        this.tdtDateofRetirement = tdtDateofRetirement;
    }
    
    /**
     * Getter for property txtPFNumber.
     * @return Value of property txtPFNumber.
     */
    public java.lang.String getTxtPFNumber() {
        return txtPFNumber;
    }
    
    /**
     * Setter for property txtPFNumber.
     * @param txtPFNumber New value of property txtPFNumber.
     */
    public void setTxtPFNumber(java.lang.String txtPFNumber) {
        this.txtPFNumber = txtPFNumber;
    }
    
    /**
     * Getter for property cboPFAcNominee.
     * @return Value of property cboPFAcNominee.
     */
    public java.lang.String getCboPFAcNominee() {
        return cboPFAcNominee;
    }
    
    /**
     * Setter for property cboPFAcNominee.
     * @param cboPFAcNominee New value of property cboPFAcNominee.
     */
    public void setCboPFAcNominee(java.lang.String cboPFAcNominee) {
        this.cboPFAcNominee = cboPFAcNominee;
    }
    
    /**
     * Getter for property cboPresentBranchId.
     * @return Value of property cboPresentBranchId.
     */
    public java.lang.String getCboPresentBranchId() {
        return cboPresentBranchId;
    }
    
    /**
     * Setter for property cboPresentBranchId.
     * @param cboPresentBranchId New value of property cboPresentBranchId.
     */
    public void setCboPresentBranchId(java.lang.String cboPresentBranchId) {
        this.cboPresentBranchId = cboPresentBranchId;
    }
    
    /**
     * Getter for property cboReginoalOffice.
     * @return Value of property cboReginoalOffice.
     */
    public java.lang.String getCboReginoalOffice() {
        return cboReginoalOffice;
    }
    
    /**
     * Setter for property cboReginoalOffice.
     * @param cboReginoalOffice New value of property cboReginoalOffice.
     */
    public void setCboReginoalOffice(java.lang.String cboReginoalOffice) {
        this.cboReginoalOffice = cboReginoalOffice;
    }
    
    /**
     * Getter for property cboZonalOffice.
     * @return Value of property cboZonalOffice.
     */
    public java.lang.String getCboZonalOffice() {
        return cboZonalOffice;
    }
    
    /**
     * Setter for property cboZonalOffice.
     * @param cboZonalOffice New value of property cboZonalOffice.
     */
    public void setCboZonalOffice(java.lang.String cboZonalOffice) {
        this.cboZonalOffice = cboZonalOffice;
    }
    
    /**
     * Getter for property tdtWorkingSince.
     * @return Value of property tdtWorkingSince.
     */
    public java.lang.String getTdtWorkingSince() {
        return tdtWorkingSince;
    }
    
    /**
     * Setter for property tdtWorkingSince.
     * @param tdtWorkingSince New value of property tdtWorkingSince.
     */
    public void setTdtWorkingSince(java.lang.String tdtWorkingSince) {
        this.tdtWorkingSince = tdtWorkingSince;
    }
    
    /**
     * Getter for property cboDesignation.
     * @return Value of property cboDesignation.
     */
    public java.lang.String getCboDesignation() {
        return cboDesignation;
    }
    
    /**
     * Setter for property cboDesignation.
     * @param cboDesignation New value of property cboDesignation.
     */
    public void setCboDesignation(java.lang.String cboDesignation) {
        this.cboDesignation = cboDesignation;
    }
    
    /**
     * Getter for property cboPresentGrade.
     * @return Value of property cboPresentGrade.
     */
    public java.lang.String getCboPresentGrade() {
        return cboPresentGrade;
    }
    
    /**
     * Setter for property cboPresentGrade.
     * @param cboPresentGrade New value of property cboPresentGrade.
     */
    public void setCboPresentGrade(java.lang.String cboPresentGrade) {
        this.cboPresentGrade = cboPresentGrade;
    }
    
    /**
     * Getter for property cbmUnionMember.
     * @return Value of property cbmUnionMember.
     */
    public com.see.truetransact.clientutil.ComboBoxModel getCbmUnionMember() {
        return cbmUnionMember;
    }
    
    /**
     * Setter for property cbmUnionMember.
     * @param cbmUnionMember New value of property cbmUnionMember.
     */
    public void setCbmUnionMember(com.see.truetransact.clientutil.ComboBoxModel cbmUnionMember) {
        this.cbmUnionMember = cbmUnionMember;
    }
    
    /**
     * Getter for property cbmSocietyMember.
     * @return Value of property cbmSocietyMember.
     */
    public com.see.truetransact.clientutil.ComboBoxModel getCbmSocietyMember() {
        return cbmSocietyMember;
    }
    
    /**
     * Setter for property cbmSocietyMember.
     * @param cbmSocietyMember New value of property cbmSocietyMember.
     */
    public void setCbmSocietyMember(com.see.truetransact.clientutil.ComboBoxModel cbmSocietyMember) {
        this.cbmSocietyMember = cbmSocietyMember;
    }
    
    /**
     * Getter for property cbmProbationPeriod.
     * @return Value of property cbmProbationPeriod.
     */
    public com.see.truetransact.clientutil.ComboBoxModel getCbmProbationPeriod() {
        return cbmProbationPeriod;
    }
    
    /**
     * Setter for property cbmProbationPeriod.
     * @param cbmProbationPeriod New value of property cbmProbationPeriod.
     */
    public void setCbmProbationPeriod(com.see.truetransact.clientutil.ComboBoxModel cbmProbationPeriod) {
        this.cbmProbationPeriod = cbmProbationPeriod;
    }
    
    /**
     * Getter for property cbmPresentBranchId.
     * @return Value of property cbmPresentBranchId.
     */
    public com.see.truetransact.clientutil.ComboBoxModel getCbmPresentBranchId() {
        return cbmPresentBranchId;
    }
    
    /**
     * Setter for property cbmPresentBranchId.
     * @param cbmPresentBranchId New value of property cbmPresentBranchId.
     */
    public void setCbmPresentBranchId(com.see.truetransact.clientutil.ComboBoxModel cbmPresentBranchId) {
        this.cbmPresentBranchId = cbmPresentBranchId;
    }
    
    /**
     * Getter for property cbmReginoalOffice.
     * @return Value of property cbmReginoalOffice.
     */
    public com.see.truetransact.clientutil.ComboBoxModel getCbmReginoalOffice() {
        return cbmReginoalOffice;
    }
    
    /**
     * Setter for property cbmReginoalOffice.
     * @param cbmReginoalOffice New value of property cbmReginoalOffice.
     */
    public void setCbmReginoalOffice(com.see.truetransact.clientutil.ComboBoxModel cbmReginoalOffice) {
        this.cbmReginoalOffice = cbmReginoalOffice;
    }
    
    /**
     * Getter for property cbmZonalOffice.
     * @return Value of property cbmZonalOffice.
     */
    public com.see.truetransact.clientutil.ComboBoxModel getCbmZonalOffice() {
        return cbmZonalOffice;
    }
    
    /**
     * Setter for property cbmZonalOffice.
     * @param cbmZonalOffice New value of property cbmZonalOffice.
     */
    public void setCbmZonalOffice(com.see.truetransact.clientutil.ComboBoxModel cbmZonalOffice) {
        this.cbmZonalOffice = cbmZonalOffice;
    }
    
    /**
     * Getter for property cbmDesignation.
     * @return Value of property cbmDesignation.
     */
    public com.see.truetransact.clientutil.ComboBoxModel getCbmDesignation() {
        return cbmDesignation;
    }
    
    /**
     * Setter for property cbmDesignation.
     * @param cbmDesignation New value of property cbmDesignation.
     */
    public void setCbmDesignation(com.see.truetransact.clientutil.ComboBoxModel cbmDesignation) {
        this.cbmDesignation = cbmDesignation;
    }
    
    /**
     * Getter for property cbmPresentGrade.
     * @return Value of property cbmPresentGrade.
     */
    public com.see.truetransact.clientutil.ComboBoxModel getCbmPresentGrade() {
        return cbmPresentGrade;
    }
    
    /**
     * Setter for property cbmPresentGrade.
     * @param cbmPresentGrade New value of property cbmPresentGrade.
     */
    public void setCbmPresentGrade(com.see.truetransact.clientutil.ComboBoxModel cbmPresentGrade) {
        this.cbmPresentGrade = cbmPresentGrade;
    }
    
    private void  setPresentDetails(){
        
        objEmployeePresentDetailsTO=new EmployeePresentDetailsTO();
        objEmployeePresentDetailsTO.setRdoGradutionIncrementYesNo(getRdoGradutionIncrementYesNo());
        Date issueDt = DateUtil.getDateMMDDYYYY(getTdtGradutionIncrementReleasedDate());
        if(issueDt != null){
            Date dobDate = (Date)curDate.clone();
            dobDate.setDate(issueDt.getDate());
            dobDate.setMonth(issueDt.getMonth());
            dobDate.setYear(issueDt.getYear());
            objEmployeePresentDetailsTO.setTdtGradutionIncrementReleasedDate(dobDate);
        }else{
            objEmployeePresentDetailsTO.setTdtGradutionIncrementReleasedDate(DateUtil.getDateMMDDYYYY(getTdtGradutionIncrementReleasedDate()));
        }
        
        objEmployeePresentDetailsTO.setRdoCAIIBPART1IncrementYesNo(getRdoCAIIBPART1IncrementYesNo());
        Date cAIIB1=DateUtil.getDateMMDDYYYY(getTdtCAIIBPART1IncrementReleasedDate());
        
        if(cAIIB1 != null){
            Date dobDate = (Date)curDate.clone();
            dobDate.setDate(cAIIB1.getDate());
            dobDate.setMonth(cAIIB1.getMonth());
            dobDate.setYear(cAIIB1.getYear());
            objEmployeePresentDetailsTO.setTdtCAIIBPART1IncrementReleasedDate(dobDate);
        }else{
            objEmployeePresentDetailsTO.setTdtCAIIBPART1IncrementReleasedDate(DateUtil.getDateMMDDYYYY(getTdtCAIIBPART1IncrementReleasedDate()));
        }
        
        objEmployeePresentDetailsTO.setRdoCAIIBPART2IncrementYesNo(getRdoCAIIBPART2IncrementYesNo());
        Date cAIIB2=DateUtil.getDateMMDDYYYY(getTdtCAIIBPART2IncrementReleasedDate());
        
        if(cAIIB2 != null){
            Date dobDate = (Date)curDate.clone();
            dobDate.setDate(cAIIB2.getDate());
            dobDate.setMonth(cAIIB2.getMonth());
            dobDate.setYear(cAIIB2.getYear());
            objEmployeePresentDetailsTO.setTdtCAIIBPART2IncrementReleasedDate(dobDate);
        }else{
            objEmployeePresentDetailsTO.setTdtCAIIBPART2IncrementReleasedDate(DateUtil.getDateMMDDYYYY(getTdtCAIIBPART2IncrementReleasedDate()));
        }
        
        objEmployeePresentDetailsTO.setRdoAnyOtherIncrementYesNo(getRdoAnyOtherIncrementYesNo());
        objEmployeePresentDetailsTO.setTxtAnyOtherIncrementInstitutionName(getTxtAnyOtherIncrementInstitutionName());
        Date anyInc=DateUtil.getDateMMDDYYYY(getTdtAnyOtherIncrementReleasedDate());
        
        if(anyInc != null){
            Date dobDate = (Date)curDate.clone();
            dobDate.setDate(anyInc.getDate());
            dobDate.setMonth(anyInc.getMonth());
            dobDate.setYear(anyInc.getYear());
            objEmployeePresentDetailsTO.setTdtAnyOtherIncrementReleasedDate(dobDate);
        }else{
            objEmployeePresentDetailsTO.setTdtAnyOtherIncrementReleasedDate(DateUtil.getDateMMDDYYYY(getTdtAnyOtherIncrementReleasedDate()));
        }
        objEmployeePresentDetailsTO.setTxtPresentBasic(getTxtPresentBasic());
        
        
        Date lstInc=DateUtil.getDateMMDDYYYY(getTdtLastIncrmentDate());
        
        if(lstInc != null){
            Date dobDate = (Date)curDate.clone();
            dobDate.setDate(lstInc.getDate());
            dobDate.setMonth(lstInc.getMonth());
            dobDate.setYear(lstInc.getYear());
            objEmployeePresentDetailsTO.setTdtLastIncrmentDate(dobDate);
        }else{
            objEmployeePresentDetailsTO.setTdtLastIncrmentDate(DateUtil.getDateMMDDYYYY(getTdtLastIncrmentDate()));
        }
        
        Date nxtInc=DateUtil.getDateMMDDYYYY(getTdtNextIncrmentDate());
        
        if(nxtInc != null){
            Date dobDate = (Date)curDate.clone();
            dobDate.setDate(nxtInc.getDate());
            dobDate.setMonth(nxtInc.getMonth());
            dobDate.setYear(nxtInc.getYear());
            objEmployeePresentDetailsTO.setTdtNextIncrmentDate(dobDate);
        }else{
            objEmployeePresentDetailsTO.setTdtNextIncrmentDate(DateUtil.getDateMMDDYYYY(getTdtNextIncrmentDate()));
        }
        objEmployeePresentDetailsTO.setTxtLossOfpay_Days(getTxtLossOfpay_Days());
        objEmployeePresentDetailsTO.setTxtLossPay_Months(getTxtLossPay_Months());
        objEmployeePresentDetailsTO.setRdoSigNoYesNo(getRdoSigNoYesNo());
        objEmployeePresentDetailsTO.setTxtSignatureNo(getTxtSignatureNo());
        objEmployeePresentDetailsTO.setCboUnionMember(getCboUnionMember());
        objEmployeePresentDetailsTO.setCboSocietyMember(getCboSocietyMember());
        objEmployeePresentDetailsTO.setTxtSignatureNo(getSocietyMemberNo());
        objEmployeePresentDetailsTO.setClubMembership(getClubMembership());
        objEmployeePresentDetailsTO.setClubName(getClubName());
        objEmployeePresentDetailsTO.setTxtProbationPeriod(getTxtProbationPeriod());
        objEmployeePresentDetailsTO.setCboProbationPeriod(getCboProbationPeriod());
        
        Date conForDt=DateUtil.getDateMMDDYYYY(getTdtConfirmationDate());
        
        if(conForDt != null){
            Date dobDate = (Date)curDate.clone();
            dobDate.setDate(conForDt.getDate());
            dobDate.setMonth(conForDt.getMonth());
            dobDate.setYear(conForDt.getYear());
            objEmployeePresentDetailsTO.setTdtConfirmationDate(dobDate);
        }else{
            objEmployeePresentDetailsTO.setTdtConfirmationDate(DateUtil.getDateMMDDYYYY(getTdtConfirmationDate()));
        }
        objEmployeePresentDetailsTO.setCboPresentBranchId(getCboPresentBranchId());
        objEmployeePresentDetailsTO.setCboZonalOffice(getCboZonalOffice());
        objEmployeePresentDetailsTO.setCboReginoalOffice(getCboReginoalOffice());
        objEmployeePresentDetailsTO.setCboPresentGrade(getCboPresentGrade());
        objEmployeePresentDetailsTO.setCboDesignation(getCboDesignation());
        
        Date prsWorkBrDt=DateUtil.getDateMMDDYYYY(getTdtWorkingSince());
        if(prsWorkBrDt != null){
            Date dobDate = (Date)curDate.clone();
            dobDate.setDate(prsWorkBrDt.getDate());
            dobDate.setMonth(prsWorkBrDt.getMonth());
            dobDate.setYear(prsWorkBrDt.getYear());
            objEmployeePresentDetailsTO.setTdtWorkingSince(dobDate);
        }else{
            objEmployeePresentDetailsTO.setTdtWorkingSince(DateUtil.getDateMMDDYYYY(getTdtWorkingSince()));
        }
        
    }
    public void setCaste(HashMap religionMap){
        List lst = (List)ClientUtil.executeQuery("getDesignationFromGrade", religionMap);
        if(lst!=null && lst.size()>0){
            getMap(lst);
            cbmCaste = new ComboBoxModel(key,value);
        }
    }
    public void setDesignation(HashMap designationMap){
        List lst = (List)ClientUtil.executeQuery("getDesignationFromGrade", designationMap);
        if(lst!=null && lst.size()>0){
            getMap(lst);
            cbmDesignation = new ComboBoxModel(key,value);
        }
    }
    public void setBranchEmpMaster(HashMap branchMap){
        List lst = (List)ClientUtil.executeQuery("getBranchForEmpMaster",branchMap);
        if(lst!=null && lst.size()>0){
            getMap(lst);
            cbmPresentBranchId = new ComboBoxModel(key,value);
            cbmReleativeBranchId = new ComboBoxModel(key,value);
            cbmBranchId = new ComboBoxModel(key,value);
        }
    }
    public void setZonalEmpMaster(HashMap zonalMap){
        List lst = (List)ClientUtil.executeQuery("getZonalForEmpMaster",zonalMap);
        if(lst!=null && lst.size()>0){
            getMap(lst);
            cbmReginoalOffice = new ComboBoxModel(key,value);
            cbmZonalOffice = new ComboBoxModel(key,value);
        }
    }
    public void setPromotedDesignation(HashMap designationMap){
        List lst = (List)ClientUtil.executeQuery("getDesignationFromGrade", designationMap);
        if(lst!=null && lst.size()>0){
            getMap(lst);
            cbmPromotedDesignation= new ComboBoxModel(key,value);
        }
    }
    private void getMap(List list){
        key = new ArrayList();
        value = new ArrayList();
        //The first values in the ArrayList key and value are empty String to display the first row of all dropdowns to be empty String
        key.add("");
        value.add("");
        for (int i=0, j=list.size(); i < j; i++) {
            key.add(((HashMap)list.get(i)).get("KEY"));
            value.add(((HashMap)list.get(i)).get("VALUE"));
        }
    }
    private void getPresentDetails(EmployeePresentDetailsTO objEmployeePresentDetailsTO){
        setRdoGradutionIncrementYesNo(objEmployeePresentDetailsTO.getRdoGradutionIncrementYesNo());
        setTdtGradutionIncrementReleasedDate(DateUtil.getStringDate(objEmployeePresentDetailsTO.getTdtGradutionIncrementReleasedDate()));
        setRdoCAIIBPART1IncrementYesNo(objEmployeePresentDetailsTO.getRdoCAIIBPART1IncrementYesNo());
        setTdtCAIIBPART1IncrementReleasedDate(DateUtil.getStringDate(objEmployeePresentDetailsTO.getTdtCAIIBPART1IncrementReleasedDate()));
        setRdoCAIIBPART2IncrementYesNo(objEmployeePresentDetailsTO.getRdoCAIIBPART2IncrementYesNo());
        setTdtCAIIBPART2IncrementReleasedDate(DateUtil.getStringDate(objEmployeePresentDetailsTO.getTdtCAIIBPART2IncrementReleasedDate()));
        setRdoAnyOtherIncrementYesNo(objEmployeePresentDetailsTO.getRdoAnyOtherIncrementYesNo());
        setTxtAnyOtherIncrementInstitutionName(objEmployeePresentDetailsTO.getTxtAnyOtherIncrementInstitutionName());
        setTdtAnyOtherIncrementReleasedDate(DateUtil.getStringDate(objEmployeePresentDetailsTO.getTdtAnyOtherIncrementReleasedDate()));
        setTxtPresentBasic(objEmployeePresentDetailsTO.getTxtPresentBasic());
        setTdtLastIncrmentDate(DateUtil.getStringDate(objEmployeePresentDetailsTO.getTdtLastIncrmentDate()));
        setTdtNextIncrmentDate(DateUtil.getStringDate(objEmployeePresentDetailsTO.getTdtNextIncrmentDate()));
        setTxtLossOfpay_Days(objEmployeePresentDetailsTO.getTxtLossOfpay_Days());
        setTxtLossPay_Months(objEmployeePresentDetailsTO.getTxtLossPay_Months());
        setRdoSigNoYesNo(objEmployeePresentDetailsTO.getRdoSigNoYesNo());
        setTxtSignatureNo(objEmployeePresentDetailsTO.getTxtSignatureNo());
        setCboUnionMember(objEmployeePresentDetailsTO.getCboUnionMember());
        setCboSocietyMember(objEmployeePresentDetailsTO.getCboSocietyMember());
        setTxtSignatureNo(objEmployeePresentDetailsTO.getSocietyMemberNo());
        setClubMembership(objEmployeePresentDetailsTO.getClubMembership());
        setClubName(objEmployeePresentDetailsTO.getClubName());
        setTxtProbationPeriod(objEmployeePresentDetailsTO.getTxtProbationPeriod());
        setCboProbationPeriod(objEmployeePresentDetailsTO.getCboProbationPeriod());
        setTdtConfirmationDate(DateUtil.getStringDate(objEmployeePresentDetailsTO.getTdtConfirmationDate()));
        setCboPresentBranchId(objEmployeePresentDetailsTO.getCboPresentBranchId());
        getCbmPresentBranchId().setKeyForSelected(objEmployeePresentDetailsTO.getCboPresentBranchId());
        setCboZonalOffice(objEmployeePresentDetailsTO.getCboZonalOffice());
        getCbmZonalOffice().setKeyForSelected(objEmployeePresentDetailsTO.getCboZonalOffice());
        setCboReginoalOffice(objEmployeePresentDetailsTO.getCboReginoalOffice());
        getCbmReginoalOffice().setKeyForSelected(objEmployeePresentDetailsTO.getCboReginoalOffice());
        setCboPresentGrade(objEmployeePresentDetailsTO.getCboPresentGrade());
        setCboDesignation(objEmployeePresentDetailsTO.getCboDesignation());
        setTdtWorkingSince(DateUtil.getStringDate(objEmployeePresentDetailsTO.getTdtWorkingSince()));
        setCboPresentBranchId(objEmployeePresentDetailsTO.getCboPresentBranchId());
        getCbmPresentBranchId().setKeyForSelected(objEmployeePresentDetailsTO.getCboPresentBranchId());
        setCboZonalOffice(objEmployeePresentDetailsTO.getCboZonalOffice());
        setCboReginoalOffice(objEmployeePresentDetailsTO.getCboReginoalOffice());
    }
    
    public void reSetPresentDetails(){
        setRdoGradutionIncrementYesNo("");
        setTdtGradutionIncrementReleasedDate("");
        setRdoCAIIBPART1IncrementYesNo("");
        setTdtCAIIBPART1IncrementReleasedDate("");
        setRdoCAIIBPART2IncrementYesNo("");
        setTdtCAIIBPART2IncrementReleasedDate("");
        setRdoAnyOtherIncrementYesNo("");
        setTxtAnyOtherIncrementInstitutionName("");
        setTdtAnyOtherIncrementReleasedDate("");
        setTxtPresentBasic("");
        setTdtLastIncrmentDate("");
        setTdtNextIncrmentDate("");
        setTxtLossOfpay_Days("");
        setTxtLossPay_Months("");
        setRdoSigNoYesNo("");
        setTxtSignatureNo("");
        setCboUnionMember("");
        setCboSocietyMember("");
        setTxtSignatureNo("");
        setClubMembership("");
        setClubName("");
        setTxtProbationPeriod("");
        setCboProbationPeriod("");
        setTdtConfirmationDate("");
        setCboPresentBranchId("");
        getCbmPresentBranchId().setKeyForSelected("");
        setCboZonalOffice("");
        getCbmZonalOffice().setKeyForSelected("");
        setCboReginoalOffice("");
        getCbmReginoalOffice().setKeyForSelected("");
        setCboPresentGrade("");
        setCboDesignation("");
        setTdtWorkingSince("");
    }
    
    /**
     * Getter for property cboOprativePordId.
     * @return Value of property cboOprativePordId.
     */
    public java.lang.String getCboOprativePordId() {
        return cboOprativePordId;
    }
    
    /**
     * Setter for property cboOprativePordId.
     * @param cboOprativePordId New value of property cboOprativePordId.
     */
    public void setCboOprativePordId(java.lang.String cboOprativePordId) {
        this.cboOprativePordId = cboOprativePordId;
    }
    
    /**
     * Getter for property txtOPAcNo.
     * @return Value of property txtOPAcNo.
     */
    public java.lang.String getTxtOPAcNo() {
        return txtOPAcNo;
    }
    
    /**
     * Setter for property txtOPAcNo.
     * @param txtOPAcNo New value of property txtOPAcNo.
     */
    public void setTxtOPAcNo(java.lang.String txtOPAcNo) {
        this.txtOPAcNo = txtOPAcNo;
    }
    
    /**
     * Getter for property cboOpACBranch.
     * @return Value of property cboOpACBranch.
     */
    public java.lang.String getCboOpACBranch() {
        return cboOpACBranch;
    }
    
    /**
     * Setter for property cboOpACBranch.
     * @param cboOpACBranch New value of property cboOpACBranch.
     */
    public void setCboOpACBranch(java.lang.String cboOpACBranch) {
        this.cboOpACBranch = cboOpACBranch;
    }
    
    /**
     * Getter for property cboEmployeeLoanType.
     * @return Value of property cboEmployeeLoanType.
     */
    public java.lang.String getCboEmployeeLoanType() {
        return cboEmployeeLoanType;
    }
    
    /**
     * Setter for property cboEmployeeLoanType.
     * @param cboEmployeeLoanType New value of property cboEmployeeLoanType.
     */
    public void setCboEmployeeLoanType(java.lang.String cboEmployeeLoanType) {
        this.cboEmployeeLoanType = cboEmployeeLoanType;
    }
    
    /**
     * Getter for property cboLoanAvailedBranch.
     * @return Value of property cboLoanAvailedBranch.
     */
    public java.lang.String getCboLoanAvailedBranch() {
        return cboLoanAvailedBranch;
    }
    
    /**
     * Setter for property cboLoanAvailedBranch.
     * @param cboLoanAvailedBranch New value of property cboLoanAvailedBranch.
     */
    public void setCboLoanAvailedBranch(java.lang.String cboLoanAvailedBranch) {
        this.cboLoanAvailedBranch = cboLoanAvailedBranch;
    }
    
    /**
     * Getter for property txtLoanSanctionRefNo.
     * @return Value of property txtLoanSanctionRefNo.
     */
    public java.lang.String getTxtLoanSanctionRefNo() {
        return txtLoanSanctionRefNo;
    }
    
    /**
     * Setter for property txtLoanSanctionRefNo.
     * @param txtLoanSanctionRefNo New value of property txtLoanSanctionRefNo.
     */
    public void setTxtLoanSanctionRefNo(java.lang.String txtLoanSanctionRefNo) {
        this.txtLoanSanctionRefNo = txtLoanSanctionRefNo;
    }
    
    /**
     * Getter for property tdtLoanSanctionDate.
     * @return Value of property tdtLoanSanctionDate.
     */
    public java.lang.String getTdtLoanSanctionDate() {
        return tdtLoanSanctionDate;
    }
    
    /**
     * Setter for property tdtLoanSanctionDate.
     * @param tdtLoanSanctionDate New value of property tdtLoanSanctionDate.
     */
    public void setTdtLoanSanctionDate(java.lang.String tdtLoanSanctionDate) {
        this.tdtLoanSanctionDate = tdtLoanSanctionDate;
    }
    
    /**
     * Getter for property txtLoanRemarks.
     * @return Value of property txtLoanRemarks.
     */
    public java.lang.String getTxtLoanRemarks() {
        return txtLoanRemarks;
    }
    
    /**
     * Setter for property txtLoanRemarks.
     * @param txtLoanRemarks New value of property txtLoanRemarks.
     */
    public void setTxtLoanRemarks(java.lang.String txtLoanRemarks) {
        this.txtLoanRemarks = txtLoanRemarks;
    }
    
    /**
     * Getter for property txtLoanNo.
     * @return Value of property txtLoanNo.
     */
    public java.lang.String getTxtLoanNo() {
        return txtLoanNo;
    }
    
    /**
     * Setter for property txtLoanNo.
     * @param txtLoanNo New value of property txtLoanNo.
     */
    public void setTxtLoanNo(java.lang.String txtLoanNo) {
        this.txtLoanNo = txtLoanNo;
    }
    
    /**
     * Getter for property txtLoanAmount.
     * @return Value of property txtLoanAmount.
     */
    public java.lang.String getTxtLoanAmount() {
        return txtLoanAmount;
    }
    
    /**
     * Setter for property txtLoanAmount.
     * @param txtLoanAmount New value of property txtLoanAmount.
     */
    public void setTxtLoanAmount(java.lang.String txtLoanAmount) {
        this.txtLoanAmount = txtLoanAmount;
    }
    
    /**
     * Getter for property txtLoanRateofInterest.
     * @return Value of property txtLoanRateofInterest.
     */
    public java.lang.String getTxtLoanRateofInterest() {
        return txtLoanRateofInterest;
    }
    
    /**
     * Setter for property txtLoanRateofInterest.
     * @param txtLoanRateofInterest New value of property txtLoanRateofInterest.
     */
    public void setTxtLoanRateofInterest(java.lang.String txtLoanRateofInterest) {
        this.txtLoanRateofInterest = txtLoanRateofInterest;
    }
    
    /**
     * Getter for property txtLoanNoOfInstallments.
     * @return Value of property txtLoanNoOfInstallments.
     */
    public java.lang.String getTxtLoanNoOfInstallments() {
        return txtLoanNoOfInstallments;
    }
    
    /**
     * Setter for property txtLoanNoOfInstallments.
     * @param txtLoanNoOfInstallments New value of property txtLoanNoOfInstallments.
     */
    public void setTxtLoanNoOfInstallments(java.lang.String txtLoanNoOfInstallments) {
        this.txtLoanNoOfInstallments = txtLoanNoOfInstallments;
    }
    
    /**
     * Getter for property txtLoanInstallmentAmount.
     * @return Value of property txtLoanInstallmentAmount.
     */
    public java.lang.String getTxtLoanInstallmentAmount() {
        return txtLoanInstallmentAmount;
    }
    
    /**
     * Setter for property txtLoanInstallmentAmount.
     * @param txtLoanInstallmentAmount New value of property txtLoanInstallmentAmount.
     */
    public void setTxtLoanInstallmentAmount(java.lang.String txtLoanInstallmentAmount) {
        this.txtLoanInstallmentAmount = txtLoanInstallmentAmount;
    }
    
    /**
     * Getter for property tdtLoanRepaymentStartDate.
     * @return Value of property tdtLoanRepaymentStartDate.
     */
    public java.lang.String getTdtLoanRepaymentStartDate() {
        return tdtLoanRepaymentStartDate;
    }
    
    /**
     * Setter for property tdtLoanRepaymentStartDate.
     * @param tdtLoanRepaymentStartDate New value of property tdtLoanRepaymentStartDate.
     */
    public void setTdtLoanRepaymentStartDate(java.lang.String tdtLoanRepaymentStartDate) {
        this.tdtLoanRepaymentStartDate = tdtLoanRepaymentStartDate;
    }
    
    /**
     * Getter for property tdLoanRepaymentEndDate.
     * @return Value of property tdLoanRepaymentEndDate.
     */
    
    
    /**
     * Getter for property rdoLoanPreCloserYesNo.
     * @return Value of property rdoLoanPreCloserYesNo.
     */
    public java.lang.String getRdoLoanPreCloserYesNo() {
        return rdoLoanPreCloserYesNo;
    }
    
    /**
     * Setter for property rdoLoanPreCloserYesNo.
     * @param rdoLoanPreCloserYesNo New value of property rdoLoanPreCloserYesNo.
     */
    public void setRdoLoanPreCloserYesNo(java.lang.String rdoLoanPreCloserYesNo) {
        this.rdoLoanPreCloserYesNo = rdoLoanPreCloserYesNo;
    }
    
    /**
     * Getter for property tdtLoanCloserDate.
     * @return Value of property tdtLoanCloserDate.
     */
    public java.lang.String getTdtLoanCloserDate() {
        return tdtLoanCloserDate;
    }
    
    /**
     * Getter for property tdtLoanRepaymentEndDate.
     * @return Value of property tdtLoanRepaymentEndDate.
     */
    public java.lang.String getTdtLoanRepaymentEndDate() {
        return tdtLoanRepaymentEndDate;
    }
    
    /**
     * Setter for property tdtLoanRepaymentEndDate.
     * @param tdtLoanRepaymentEndDate New value of property tdtLoanRepaymentEndDate.
     */
    public void setTdtLoanRepaymentEndDate(java.lang.String tdtLoanRepaymentEndDate) {
        this.tdtLoanRepaymentEndDate = tdtLoanRepaymentEndDate;
    }
    
    /**
     * Getter for property cbmAccountType.
     * @return Value of property cbmAccountType.
     */
    public com.see.truetransact.clientutil.ComboBoxModel getCbmAccountType() {
        return cbmAccountType;
    }
    
    /**
     * Setter for property cbmAccountType.
     * @param cbmAccountType New value of property cbmAccountType.
     */
    public void setCbmAccountType(com.see.truetransact.clientutil.ComboBoxModel cbmAccountType) {
        this.cbmAccountType = cbmAccountType;
    }
    
    /**
     * Getter for property cbmEmployeeLoanType.
     * @return Value of property cbmEmployeeLoanType.
     */
    public com.see.truetransact.clientutil.ComboBoxModel getCbmEmployeeLoanType() {
        return cbmEmployeeLoanType;
    }
    
    /**
     * Setter for property cbmEmployeeLoanType.
     * @param cbmEmployeeLoanType New value of property cbmEmployeeLoanType.
     */
    public void setCbmEmployeeLoanType(com.see.truetransact.clientutil.ComboBoxModel cbmEmployeeLoanType) {
        this.cbmEmployeeLoanType = cbmEmployeeLoanType;
    }
    
    /**
     * Getter for property cbmLoanAvailedBranch.
     * @return Value of property cbmLoanAvailedBranch.
     */
    public com.see.truetransact.clientutil.ComboBoxModel getCbmLoanAvailedBranch() {
        return cbmLoanAvailedBranch;
    }
    
    /**
     * Setter for property cbmLoanAvailedBranch.
     * @param cbmLoanAvailedBranch New value of property cbmLoanAvailedBranch.
     */
    public void setCbmLoanAvailedBranch(com.see.truetransact.clientutil.ComboBoxModel cbmLoanAvailedBranch) {
        this.cbmLoanAvailedBranch = cbmLoanAvailedBranch;
    }
    
    /**
     * Getter for property cbmSalaryCrBranch.
     * @return Value of property cbmSalaryCrBranch.
     */
    public com.see.truetransact.clientutil.ComboBoxModel getCbmSalaryCrBranch() {
        return cbmSalaryCrBranch;
    }
    
    /**
     * Setter for property cbmSalaryCrBranch.
     * @param cbmSalaryCrBranch New value of property cbmSalaryCrBranch.
     */
    public void setCbmSalaryCrBranch(com.see.truetransact.clientutil.ComboBoxModel cbmSalaryCrBranch) {
        this.cbmSalaryCrBranch = cbmSalaryCrBranch;
    }
    
    /**
     * Setter for property tdtLoanCloserDate.
     * @param tdtLoanCloserDate New value of property tdtLoanCloserDate.
     */
    public void setTdtLoanCloserDate(java.lang.String tdtLoanCloserDate) {
        this.tdtLoanCloserDate = tdtLoanCloserDate;
    }
    
    /**
     * Getter for property tblOprative.
     * @return Value of property tblOprative.
     */
    public com.see.truetransact.clientutil.EnhancedTableModel getTblOprative() {
        return tblOprative;
    }
    
    /**
     * Setter for property tblOprative.
     * @param tblOprative New value of property tblOprative.
     */
    public void setTblOprative(com.see.truetransact.clientutil.EnhancedTableModel tblOprative) {
        this.tblOprative = tblOprative;
    }
    
    /**
     * Getter for property tblEmployeeLoan.
     * @return Value of property tblEmployeeLoan.
     */
    public com.see.truetransact.clientutil.EnhancedTableModel getTblEmployeeLoan() {
        return tblEmployeeLoan;
    }
    
    /**
     * Setter for property tblEmployeeLoan.
     * @param tblEmployeeLoan New value of property tblEmployeeLoan.
     */
    public void setTblEmployeeLoan(com.see.truetransact.clientutil.EnhancedTableModel tblEmployeeLoan) {
        this.tblEmployeeLoan = tblEmployeeLoan;
    }
    
    public void  oprateive(int row,boolean operativeFlag){
        try{
            final EmployeeOprativeTO objEmployeeOprativeTO = new EmployeeOprativeTO();
            if( oprativeMap == null ){
                oprativeMap = new HashMap();
            }
            
            if(getActionType()==ClientConstants.ACTIONTYPE_EDIT){
                if(isNewOperative()){
                    objEmployeeOprativeTO.setStatus(CommonConstants.STATUS_CREATED);
                    objEmployeeOprativeTO.setStatusBy(TrueTransactMain.USER_ID);
                    objEmployeeOprativeTO.setStatusDt(curDate);
                }else{
                    objEmployeeOprativeTO.setStatus(CommonConstants.STATUS_MODIFIED);
                    objEmployeeOprativeTO.setStatusBy(TrueTransactMain.USER_ID);
                    objEmployeeOprativeTO.setStatusDt(curDate);
                }
            }else{
                objEmployeeOprativeTO.setStatus(CommonConstants.STATUS_CREATED);
            }
            if( actionType == ClientConstants.ACTIONTYPE_EDIT ){
                objEmployeeOprativeTO.setSysId(sysId);
            }
            int slno;
            slno=0;
            
            if(!operativeFlag){
                
                ArrayList data = tblOprative.getDataArrayList();
                slno=serialNo(data,tblOprative);
            }
            else if(isNewEducation()){
                int b=CommonUtil.convertObjToInt(tblOprative.getValueAt(row,0));
                slno= b + tblOprative.getRowCount();
            }
            else{
                slno = CommonUtil.convertObjToInt(tblOprative.getValueAt(row,0));
            }
            objEmployeeOprativeTO.setOperativeId(String.valueOf(slno));
            objEmployeeOprativeTO.setCboOprativePordId(getCboOprativePordId());
            objEmployeeOprativeTO.setCboOpACBranch(getCboOpACBranch());
            objEmployeeOprativeTO.setTxtOPAcNo(getTxtOPAcNo());
            oprativeMap.put(CommonUtil.convertObjToStr(String.valueOf(slno)),objEmployeeOprativeTO);
            updateTblOprativeList(row,objEmployeeOprativeTO);
            ttNotifyObservers();
        }catch(Exception e){
            parseException.logException(e,true);
        }
        
    }
    
    private void updateTblOprativeList(int row,EmployeeOprativeTO objEmployeeOprativeTO)  throws Exception{
        
//       modified again :'(
        
        Object selectedRow;
        boolean rowExists = false;
        //If row already exists update it, else create a new row & append
        for(int i = tblOprative.getRowCount(), j = 0; i > 0; i--,j++){
            selectedRow = ((ArrayList)tblOprative.getDataArrayList().get(j)).get(0);
            if( (CommonUtil.convertObjToStr(getOperativeId())).equals(CommonUtil.convertObjToStr(selectedRow))){
                rowExists = true;
            }
        }
         ArrayList operativeRow = new ArrayList();
        if(row == -1){
            operativeRow.add(CommonUtil.convertObjToStr(objEmployeeOprativeTO.getOperativeId())); 
            operativeRow.add(CommonUtil.convertObjToStr(cbmAccountType.getKeyForSelected()));
            operativeRow.add(getTxtOPAcNo());
            operativeRow.add(CommonUtil.convertObjToStr(cbmSalaryCrBranch.getKeyForSelected()));
            tblOprative.insertRow(tblOprative.getRowCount(),operativeRow); 
            operativeRow = null;
        }
        else {
            tblOprative.removeRow(row);
            operativeRow.add(CommonUtil.convertObjToStr(objEmployeeOprativeTO.getOperativeId())); 
            operativeRow.add(CommonUtil.convertObjToStr(cbmAccountType.getKeyForSelected()));
            operativeRow.add(getTxtOPAcNo());
            operativeRow.add(CommonUtil.convertObjToStr(cbmSalaryCrBranch.getKeyForSelected()));
            tblOprative.insertRow(row,operativeRow);
            operativeRow = null;
        }
//        if(!rowExists){
//            ArrayList addressRow = new ArrayList();
//            addressRow.add(CommonUtil.convertObjToStr(cbmAccountType.getKeyForSelected()));
//            addressRow.add(getTxtOPAcNo());
//            addressRow.add(CommonUtil.convertObjToStr(cbmSalaryCrBranch.getKeyForSelected()));
//            tblOprative.insertRow(tblOprative.getRowCount(),addressRow);
//            addressRow = null;
//        }
    }
    public void populateOprative(int row){
        try{
            oprativeTypeChanged(CommonUtil.convertObjToStr(tblOprative.getValueAt(row,ADDRTYPE_COLNO)));
        }catch(Exception e){
            parseException.logException(e,true);
        }
    }
    
    
    
    public void oprativeTypeChanged(String selectedItem){
        try{
            
            oprativeChanged = true;
            
            final EmployeeOprativeTO objEmployeeOprativeTO = (EmployeeOprativeTO)oprativeMap.get(selectedItem);
            populateOprative(objEmployeeOprativeTO);
            ttNotifyObservers();
        }catch(Exception e){
            parseException.logException(e,true);
        }
    }
    
    
    private void populateOprative(EmployeeOprativeTO objEmployeeOprativeTO)  throws Exception{
        try{
            if(objEmployeeOprativeTO != null){
                //                setCboOprativePordId(objEmployeeOprativeTO.getCboOprativePordId());
                cbmAccountType.setKeyForSelected(objEmployeeOprativeTO.getCboOprativePordId());
                //                setCboOpACBranch(objEmployeeOprativeTO.getCboOpACBranch());
                getCbmSalaryCrBranch().setKeyForSelected(objEmployeeOprativeTO.getCboOpACBranch());
                setTxtOPAcNo(objEmployeeOprativeTO.getTxtOPAcNo());
            }
        }catch(Exception e){
            e.printStackTrace();
        }
    }
    public void deleteOprative(int row){
        if(deletedOprativeMap == null){
            deletedOprativeMap = new HashMap();
        }
        EmployeeOprativeTO objEmployeeOprativeTO = (EmployeeOprativeTO)oprativeMap.get(CommonUtil.convertObjToStr(tblOprative.getValueAt(row,ADDRTYPE_COLNO)));
        objEmployeeOprativeTO.setStatus(CommonConstants.STATUS_DELETED);
        objEmployeeOprativeTO.setStatusDt(curDate);
        objEmployeeOprativeTO.setStatusBy(TrueTransactMain.USER_ID);
        deletedOprativeMap.put(CommonUtil.convertObjToStr(tblOprative.getValueAt(row,ADDRTYPE_COLNO)),oprativeMap.get(CommonUtil.convertObjToStr(tblOprative.getValueAt(row,ADDRTYPE_COLNO))));
        oprativeMap.remove(tblOprative.getValueAt(row,ADDRTYPE_COLNO));
        resetDeleteOprative();
    }
    
    public void resetDeleteOprative(){
        try{
            
            resetOprative();
            resetOprativeListTable();
            populateOprativeTable();
            ttNotifyObservers();
        }catch(Exception e){
            parseException.logException(e,true);
        }
    }
    
    private void populateOprativeTable()  throws Exception{
        final Iterator addressMapIterator = oprativeMap.keySet().iterator();
        ArrayList addressRow = null;
        for(int i = oprativeMap.size(), j = 0; i > 0; i--,j++){
            final String addressType = CommonUtil.convertObjToStr(addressMapIterator.next());
            addressRow = new ArrayList();
            addressRow.add(addressType);
            addressRow.add(EMPTY_STRING);
            tblOprative.insertRow(tblOprative.getRowCount(),addressRow);
            
            addressRow = null;
        }
        
    }
    
    
    /**
     * Getter for property oprativeChanged.
     * @return Value of property oprativeChanged.
     */
    public boolean isOprativeChanged() {
        return oprativeChanged;
    }
    
    /**
     * Setter for property oprativeChanged.
     * @param oprativeChanged New value of property oprativeChanged.
     */
    public void setOprativeChanged(boolean oprativeChanged) {
        this.oprativeChanged = oprativeChanged;
    }
    
    
    public void  loans(){
        try{
            final EmployeeTermLoanTO objEmployeeTermLoanTO = new EmployeeTermLoanTO();
            if( loanMap == null ){
                loanMap = new HashMap();
            }
            
            if(getActionType()==ClientConstants.ACTIONTYPE_EDIT){
                if(isNewLoans()){
                    objEmployeeTermLoanTO.setStatus(CommonConstants.STATUS_CREATED);
                    objEmployeeTermLoanTO.setStatusBy(TrueTransactMain.USER_ID);
                    objEmployeeTermLoanTO.setStatusDt(curDate);
                }else{
                    objEmployeeTermLoanTO.setStatus(CommonConstants.STATUS_MODIFIED);
                    objEmployeeTermLoanTO.setStatusBy(TrueTransactMain.USER_ID);
                    objEmployeeTermLoanTO.setStatusDt(curDate);
                }
            }else{
                objEmployeeTermLoanTO.setStatus(CommonConstants.STATUS_CREATED);
            }
            if( actionType == ClientConstants.ACTIONTYPE_EDIT ){
                objEmployeeTermLoanTO.setSysId(sysId);
            }
            objEmployeeTermLoanTO.setCboEmployeeLoanType(getCboEmployeeLoanType());
            objEmployeeTermLoanTO.setCboLoanAvailedBranch(getCboLoanAvailedBranch());
            objEmployeeTermLoanTO.setTxtLoanSanctionRefNo(getTxtLoanSanctionRefNo());
            objEmployeeTermLoanTO.setTdtLoanSanctionDate(DateUtil.getDateMMDDYYYY(getTdtLoanSanctionDate()));
            objEmployeeTermLoanTO.setTxtLoanNo(getTxtLoanNo());
            objEmployeeTermLoanTO.setTxtLoanAmount(getTxtLoanAmount());
            objEmployeeTermLoanTO.setTxtLoanRateofInterest(getTxtLoanRateofInterest());
            objEmployeeTermLoanTO.setTxtLoanNoOfInstallments(getTxtLoanNoOfInstallments());
            objEmployeeTermLoanTO.setTxtLoanInstallmentAmount(getTxtLoanInstallmentAmount());
            objEmployeeTermLoanTO.setTdtLoanRepaymentStartDate(DateUtil.getDateMMDDYYYY(getTdtLoanRepaymentStartDate()));
            objEmployeeTermLoanTO.setTdtLoanRepaymentEndDate(DateUtil.getDateMMDDYYYY(getTdtLoanRepaymentEndDate()));
            objEmployeeTermLoanTO.setRdoLoanPreCloserYesNo(getRdoLoanPreCloserYesNo());
            objEmployeeTermLoanTO.setTdtLoanCloserDate(DateUtil.getDateMMDDYYYY(getTdtLoanCloserDate()));
            objEmployeeTermLoanTO.setTxtLoanRemarks(getTxtLoanRemarks());
            loanMap.put(CommonUtil.convertObjToStr(getCboEmployeeLoanType()), objEmployeeTermLoanTO);
            
            updateTblLoanList();
            ttNotifyObservers();
        }catch(Exception e){
            parseException.logException(e,true);
        }
        
    }
    
    private void updateTblLoanList()  throws Exception{
        Object selectedRow;
        boolean rowExists = false;
        //If row already exists update it, else create a new row & append
        for(int i = tblEmployeeLoan.getRowCount(), j = 0; i > 0; i--,j++){
            selectedRow = ((ArrayList)tblEmployeeLoan.getDataArrayList().get(j)).get(0);
            if( (CommonUtil.convertObjToStr(cbmEmployeeLoanType.getKeyForSelected())).equals(CommonUtil.convertObjToStr(selectedRow))){
                rowExists = true;
            }
        }
        if(!rowExists){
            ArrayList addressRow = new ArrayList();
            addressRow.add(CommonUtil.convertObjToStr(cbmEmployeeLoanType.getKeyForSelected()));
            addressRow.add(getTxtLoanNo());
            addressRow.add(CommonUtil.convertObjToStr(cbmLoanAvailedBranch.getKeyForSelected()));
            tblEmployeeLoan.insertRow(tblEmployeeLoan.getRowCount(),addressRow);
            addressRow = null;
        }
    }
    public void populateLoans(int row){
        try{
            LoansTypeChanged(CommonUtil.convertObjToStr(tblEmployeeLoan.getValueAt(row,ADDRTYPE_COLNO)));
        }catch(Exception e){
            parseException.logException(e,true);
        }
    }
    
    
    
    public void LoansTypeChanged(String selectedItem){
        try{
            
            loanTypeExists = true;
            
            final EmployeeTermLoanTO objEmployeeTermLoanTO = (EmployeeTermLoanTO)loanMap.get(selectedItem);
            populateLoans(objEmployeeTermLoanTO);
            ttNotifyObservers();
        }catch(Exception e){
            parseException.logException(e,true);
        }
    }
    
    
    private void populateLoans(EmployeeTermLoanTO objEmployeeTermLoanTO)  throws Exception{
        try{
            if(objEmployeeTermLoanTO != null){
                setCboEmployeeLoanType(objEmployeeTermLoanTO.getCboEmployeeLoanType());
                //                setCboLoanAvailedBranch(objEmployeeTermLoanTO.getCboLoanAvailedBranch());
                getCbmLoanAvailedBranch().setKeyForSelected(objEmployeeTermLoanTO.getCboLoanAvailedBranch());
                setTxtLoanSanctionRefNo(objEmployeeTermLoanTO.getTxtLoanSanctionRefNo());
                setTdtLoanSanctionDate(DateUtil.getStringDate(objEmployeeTermLoanTO.getTdtLoanSanctionDate()));
                setTxtLoanNo(objEmployeeTermLoanTO.getTxtLoanNo());
                setTxtLoanAmount(objEmployeeTermLoanTO.getTxtLoanAmount());
                setTxtLoanRateofInterest(objEmployeeTermLoanTO.getTxtLoanRateofInterest());
                setTxtLoanNoOfInstallments(objEmployeeTermLoanTO.getTxtLoanNoOfInstallments());
                setTxtLoanInstallmentAmount(objEmployeeTermLoanTO.getTxtLoanInstallmentAmount());
                setTdtLoanRepaymentStartDate(DateUtil.getStringDate(objEmployeeTermLoanTO.getTdtLoanRepaymentStartDate()));
                setTdtLoanRepaymentEndDate(DateUtil.getStringDate(objEmployeeTermLoanTO.getTdtLoanRepaymentEndDate()));
                setRdoLoanPreCloserYesNo(objEmployeeTermLoanTO.getRdoLoanPreCloserYesNo());
                setTdtLoanCloserDate(DateUtil.getStringDate(objEmployeeTermLoanTO.getTdtLoanCloserDate()));
                setTxtLoanRemarks(objEmployeeTermLoanTO.getTxtLoanRemarks());
                
            }
        }catch(Exception e){
            e.printStackTrace();
        }
    }
    public void deleteLoans(int row){
        if(deletedOprativeMap == null){
            deletedOprativeMap = new HashMap();
        }
        EmployeeTermLoanTO objEmployeeTermLoanTO = (EmployeeTermLoanTO)loanMap.get(CommonUtil.convertObjToStr(tblEmployeeLoan.getValueAt(row,ADDRTYPE_COLNO)));
        objEmployeeTermLoanTO.setStatus(CommonConstants.STATUS_DELETED);
        objEmployeeTermLoanTO.setStatusDt(curDate);
        objEmployeeTermLoanTO.setStatusBy(TrueTransactMain.USER_ID);
        deletedLoanMap.put(CommonUtil.convertObjToStr(tblEmployeeLoan.getValueAt(row,ADDRTYPE_COLNO)),loanMap.get(CommonUtil.convertObjToStr(tblEmployeeLoan.getValueAt(row,ADDRTYPE_COLNO))));
        oprativeMap.remove(tblEmployeeLoan.getValueAt(row,ADDRTYPE_COLNO));
        resetDeleteLoans();
    }
    
    public void resetDeleteLoans(){
        try{
            
            resetLoans();
            resetLoanListTable();
            populateLoansTable();
            ttNotifyObservers();
        }catch(Exception e){
            parseException.logException(e,true);
        }
    }
    
    public void resetLoanListTable(){
        for(int i = tblEmployeeLoan.getRowCount(); i > 0; i--){
            tblEmployeeLoan.removeRow(0);
        }
    }
    
    
    private void populateLoansTable()  throws Exception{
        final Iterator addressMapIterator = loanMap.keySet().iterator();
        ArrayList addressRow = null;
        for(int i = oprativeMap.size(), j = 0; i > 0; i--,j++){
            final String addressType = CommonUtil.convertObjToStr(addressMapIterator.next());
            addressRow = new ArrayList();
            addressRow.add(addressType);
            addressRow.add(EMPTY_STRING);
            tblEmployeeLoan.insertRow(tblEmployeeLoan.getRowCount(),addressRow);
            
            addressRow = null;
        }
        
    }
    
    /**
     * Getter for property loans.
     * @return Value of property loans.
     */
    public boolean isLoans() {
        return loans;
    }
    
    /**
     * Setter for property loans.
     * @param loans New value of property loans.
     */
    public void setLoans(boolean loans) {
        this.loans = loans;
    }
    
    /**
     * Getter for property txtPhysicalHandicap.
     * @return Value of property txtPhysicalHandicap.
     */
    public java.lang.String getTxtPhysicalHandicap() {
        return txtPhysicalHandicap;
    }
    
    /**
     * Setter for property txtPhysicalHandicap.
     * @param txtPhysicalHandicap New value of property txtPhysicalHandicap.
     */
    public void setTxtPhysicalHandicap(java.lang.String txtPhysicalHandicap) {
        this.txtPhysicalHandicap = txtPhysicalHandicap;
    }
    
    /**
     * Getter for property cboAccountType.
     * @return Value of property cboAccountType.
     */
    public java.lang.String getCboAccountType() {
        return cboAccountType;
    }
    
    /**
     * Setter for property cboAccountType.
     * @param cboAccountType New value of property cboAccountType.
     */
    public void setCboAccountType(java.lang.String cboAccountType) {
        this.cboAccountType = cboAccountType;
    }
    
    /**
     * Getter for property cboSalaryCrBranch.
     * @return Value of property cboSalaryCrBranch.
     */
    public java.lang.String getCboSalaryCrBranch() {
        return cboSalaryCrBranch;
    }
    
    /**
     * Setter for property cboSalaryCrBranch.
     * @param cboSalaryCrBranch New value of property cboSalaryCrBranch.
     */
    public void setCboSalaryCrBranch(java.lang.String cboSalaryCrBranch) {
        this.cboSalaryCrBranch = cboSalaryCrBranch;
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
     * Getter for property newLoans.
     * @return Value of property newLoans.
     */
    public boolean isNewLoans() {
        return newLoans;
    }
    
    /**
     * Setter for property newLoans.
     * @param newLoans New value of property newLoans.
     */
    public void setNewLoans(boolean newLoans) {
        this.newLoans = newLoans;
    }
    
    /**
     * Getter for property newRelativesWorking.
     * @return Value of property newRelativesWorking.
     */
    public boolean isNewRelativesWorking() {
        return newRelativesWorking;
    }
    
    /**
     * Setter for property newRelativesWorking.
     * @param newRelativesWorking New value of property newRelativesWorking.
     */
    public void setNewRelativesWorking(boolean newRelativesWorking) {
        this.newRelativesWorking = newRelativesWorking;
    }
    
    /**
     * Getter for property newDirector.
     * @return Value of property newDirector.
     */
    public boolean isNewDirector() {
        return newDirector;
    }
    
    /**
     * Setter for property newDirector.
     * @param newDirector New value of property newDirector.
     */
    public void setNewDirector(boolean newDirector) {
        this.newDirector = newDirector;
    }
    
    /**
     * Getter for property newLanguage.
     * @return Value of property newLanguage.
     */
    public boolean isNewLanguage() {
        return newLanguage;
    }
    
    /**
     * Setter for property newLanguage.
     * @param newLanguage New value of property newLanguage.
     */
    public void setNewLanguage(boolean newLanguage) {
        this.newLanguage = newLanguage;
    }
    
    /**
     * Getter for property newOperative.
     * @return Value of property newOperative.
     */
    public boolean isNewOperative() {
        return newOperative;
    }
    
    /**
     * Setter for property newOperative.
     * @param newOperative New value of property newOperative.
     */
    public void setNewOperative(boolean newOperative) {
        this.newOperative = newOperative;
    }
    
    /**
     * Getter for property cboReleativeDisg.
     * @return Value of property cboReleativeDisg.
     */
    public java.lang.String getCboReleativeDisg() {
        return cboReleativeDisg;
    }
    
    /**
     * Setter for property cboReleativeDisg.
     * @param cboReleativeDisg New value of property cboReleativeDisg.
     */
    public void setCboReleativeDisg(java.lang.String cboReleativeDisg) {
        this.cboReleativeDisg = cboReleativeDisg;
    }
    
    /**
     * Getter for property cboReleativeBranchId.
     * @return Value of property cboReleativeBranchId.
     */
    public java.lang.String getCboReleativeBranchId() {
        return cboReleativeBranchId;
    }
    
    /**
     * Setter for property cboReleativeBranchId.
     * @param cboReleativeBranchId New value of property cboReleativeBranchId.
     */
    public void setCboReleativeBranchId(java.lang.String cboReleativeBranchId) {
        this.cboReleativeBranchId = cboReleativeBranchId;
    }
    
    /**
     * Getter for property cboReleativeReleationShip.
     * @return Value of property cboReleativeReleationShip.
     */
    public java.lang.String getCboReleativeReleationShip() {
        return cboReleativeReleationShip;
    }
    
    /**
     * Setter for property cboReleativeReleationShip.
     * @param cboReleativeReleationShip New value of property cboReleativeReleationShip.
     */
    public void setCboReleativeReleationShip(java.lang.String cboReleativeReleationShip) {
        this.cboReleativeReleationShip = cboReleativeReleationShip;
    }
    
    /**
     * Getter for property dependentId.
     * @return Value of property dependentId.
     */
    public java.lang.String getDependentId() {
        return dependentId;
    }
    
    /**
     * Setter for property dependentId.
     * @param dependentId New value of property dependentId.
     */
    public void setDependentId(java.lang.String dependentId) {
        this.dependentId = dependentId;
    }
    
    /**
     * Getter for property directorID.
     * @return Value of property directorID.
     */
    public java.lang.String getDirectorID() {
        return directorID;
    }
    
    /**
     * Setter for property directorID.
     * @param directorID New value of property directorID.
     */
    public void setDirectorID(java.lang.String directorID) {
        this.directorID = directorID;
    }
    
    /**
     * Getter for property academicID.
     * @return Value of property academicID.
     */
    public java.lang.String getAcademicID() {
        return academicID;
    }
    
    /**
     * Setter for property academicID.
     * @param academicID New value of property academicID.
     */
    public void setAcademicID(java.lang.String academicID) {
        this.academicID = academicID;
    }
    
    /**
     * Getter for property technicalID.
     * @return Value of property technicalID.
     */
    public java.lang.String getTechnicalID() {
        return technicalID;
    }
    
    /**
     * Setter for property technicalID.
     * @param technicalID New value of property technicalID.
     */
    public void setTechnicalID(java.lang.String technicalID) {
        this.technicalID = technicalID;
    }
    
    /**
     * Getter for property txtDepIncomePerannum.
     * @return Value of property txtDepIncomePerannum.
     */
    public java.lang.String getTxtDepIncomePerannum() {
        return txtDepIncomePerannum;
    }
    
    /**
     * Setter for property txtDepIncomePerannum.
     * @param txtDepIncomePerannum New value of property txtDepIncomePerannum.
     */
    public void setTxtDepIncomePerannum(java.lang.String txtDepIncomePerannum) {
        this.txtDepIncomePerannum = txtDepIncomePerannum;
    }
    
    /**
     * Getter for property txtEmpWith.
     * @return Value of property txtEmpWith.
     */
    public java.lang.String getTxtEmpWith() {
        return txtEmpWith;
    }
    
    /**
     * Setter for property txtEmpWith.
     * @param txtEmpWith New value of property txtEmpWith.
     */
    public void setTxtEmpWith(java.lang.String txtEmpWith) {
        this.txtEmpWith = txtEmpWith;
    }
    
    /**
     * Getter for property rdoLiableYesNo.
     * @return Value of property rdoLiableYesNo.
     */
    public java.lang.String getRdoLiableYesNo() {
        return rdoLiableYesNo;
    }
    
    /**
     * Setter for property rdoLiableYesNo.
     * @param rdoLiableYesNo New value of property rdoLiableYesNo.
     */
    public void setRdoLiableYesNo(java.lang.String rdoLiableYesNo) {
        this.rdoLiableYesNo = rdoLiableYesNo;
    }
    
    /**
     * Getter for property cboPromotionGradeValue.
     * @return Value of property cboPromotionGradeValue.
     */
    public java.lang.String getcboPromotionGradeValue() {
        return cboPromotionGradeValue;
    }
    
    /**
     * Setter for property cboPromotionGradeValue.
     * @param cboPromotionGradeValue New value of property cboPromotionGradeValue.
     */
    public void setcboPromotionGradeValue(java.lang.String cboPromotionGradeValue) {
        this.cboPromotionGradeValue = cboPromotionGradeValue;
    }
    
    /**
     * Getter for property lblPromotionSLNOValue.
     * @return Value of property lblPromotionSLNOValue.
     */
    public java.lang.String getLblPromotionSLNOValue() {
        return lblPromotionSLNOValue;
    }
    
    /**
     * Setter for property lblPromotionSLNOValue.
     * @param lblPromotionSLNOValue New value of property lblPromotionSLNOValue.
     */
    public void setLblPromotionSLNOValue(java.lang.String lblPromotionSLNOValue) {
        this.lblPromotionSLNOValue = lblPromotionSLNOValue;
    }
    
    /**
     * Getter for property txtPromotionEmployeeId.
     * @return Value of property txtPromotionEmployeeId.
     */
    public java.lang.String getTxtPromotionEmployeeId() {
        return txtPromotionEmployeeId;
    }
    
    /**
     * Setter for property txtPromotionEmployeeId.
     * @param txtPromotionEmployeeId New value of property txtPromotionEmployeeId.
     */
    public void setTxtPromotionEmployeeId(java.lang.String txtPromotionEmployeeId) {
        this.txtPromotionEmployeeId = txtPromotionEmployeeId;
    }
    
    /**
     * Getter for property txtPromotionEmployeeName.
     * @return Value of property txtPromotionEmployeeName.
     */
    public java.lang.String getTxtPromotionEmployeeName() {
        return txtPromotionEmployeeName;
    }
    
    /**
     * Setter for property txtPromotionEmployeeName.
     * @param txtPromotionEmployeeName New value of property txtPromotionEmployeeName.
     */
    public void setTxtPromotionEmployeeName(java.lang.String txtPromotionEmployeeName) {
        this.txtPromotionEmployeeName = txtPromotionEmployeeName;
    }
    
    /**
     * Getter for property txtPromotionDesignation.
     * @return Value of property txtPromotionDesignation.
     */
    public java.lang.String getTxtPromotionDesignation() {
        return txtPromotionDesignation;
    }
    
    /**
     * Setter for property txtPromotionDesignation.
     * @param txtPromotionDesignation New value of property txtPromotionDesignation.
     */
    public void setTxtPromotionDesignation(java.lang.String txtPromotionDesignation) {
        this.txtPromotionDesignation = txtPromotionDesignation;
    }
    
    /**
     * Getter for property txtPromotionEmpBranch.
     * @return Value of property txtPromotionEmpBranch.
     */
    public java.lang.String getTxtPromotionEmpBranch() {
        return txtPromotionEmpBranch;
    }
    
    /**
     * Setter for property txtPromotionEmpBranch.
     * @param txtPromotionEmpBranch New value of property txtPromotionEmpBranch.
     */
    public void setTxtPromotionEmpBranch(java.lang.String txtPromotionEmpBranch) {
        this.txtPromotionEmpBranch = txtPromotionEmpBranch;
    }
    
    /**
     * Getter for property txtPromotionLastDesg.
     * @return Value of property txtPromotionLastDesg.
     */
    public java.lang.String getTxtPromotionLastDesg() {
        return txtPromotionLastDesg;
    }
    
    /**
     * Setter for property txtPromotionLastDesg.
     * @param txtPromotionLastDesg New value of property txtPromotionLastDesg.
     */
    public void setTxtPromotionLastDesg(java.lang.String txtPromotionLastDesg) {
        this.txtPromotionLastDesg = txtPromotionLastDesg;
    }
    
    /**
     * Getter for property tdtPromotionEffectiveDateValue.
     * @return Value of property tdtPromotionEffectiveDateValue.
     */
    public java.lang.String getTdtPromotionEffectiveDateValue() {
        return tdtPromotionEffectiveDateValue;
    }
    
    /**
     * Setter for property tdtPromotionEffectiveDateValue.
     * @param tdtPromotionEffectiveDateValue New value of property tdtPromotionEffectiveDateValue.
     */
    public void setTdtPromotionEffectiveDateValue(java.lang.String tdtPromotionEffectiveDateValue) {
        this.tdtPromotionEffectiveDateValue = tdtPromotionEffectiveDateValue;
    }
    
    /**
     * Getter for property tdtPromotionCreatedDateValue.
     * @return Value of property tdtPromotionCreatedDateValue.
     */
    public java.lang.String getTdtPromotionCreatedDateValue() {
        return tdtPromotionCreatedDateValue;
    }
    
    /**
     * Setter for property tdtPromotionCreatedDateValue.
     * @param tdtPromotionCreatedDateValue New value of property tdtPromotionCreatedDateValue.
     */
    public void setTdtPromotionCreatedDateValue(java.lang.String tdtPromotionCreatedDateValue) {
        this.tdtPromotionCreatedDateValue = tdtPromotionCreatedDateValue;
    }
    
    /**
     * Getter for property txtPromotionBasicPayValue.
     * @return Value of property txtPromotionBasicPayValue.
     */
    public java.lang.String getTxtPromotionBasicPayValue() {
        return txtPromotionBasicPayValue;
    }
    
    /**
     * Setter for property txtPromotionBasicPayValue.
     * @param txtPromotionBasicPayValue New value of property txtPromotionBasicPayValue.
     */
    public void setTxtPromotionBasicPayValue(java.lang.String txtPromotionBasicPayValue) {
        this.txtPromotionBasicPayValue = txtPromotionBasicPayValue;
    }
    
    /**
     * Getter for property promotionID.
     * @return Value of property promotionID.
     */
    public java.lang.String getPromotionID() {
        return promotionID;
    }
    
    /**
     * Setter for property promotionID.
     * @param promotionID New value of property promotionID.
     */
    public void setPromotionID(java.lang.String promotionID) {
        this.promotionID = promotionID;
    }
    
    /**
     * Getter for property cbmPromotionGradeValue.
     * @return Value of property cbmPromotionGradeValue.
     */
    public com.see.truetransact.clientutil.ComboBoxModel getCbmPromotionGradeValue() {
        return cbmPromotionGradeValue;
    }
    
    /**
     * Setter for property cbmPromotionGradeValue.
     * @param cbmPromotionGradeValue New value of property cbmPromotionGradeValue.
     */
    public void setCbmPromotionGradeValue(com.see.truetransact.clientutil.ComboBoxModel cbmPromotionGradeValue) {
        this.cbmPromotionGradeValue = cbmPromotionGradeValue;
    }
    
    /**
     * Getter for property tbmPromotion.
     * @return Value of property tbmPromotion.
     */
    public com.see.truetransact.clientutil.TableModel getTbmPromotion() {
        return tbmPromotion;
    }
    
    /**
     * Setter for property tbmPromotion.
     * @param tbmPromotion New value of property tbmPromotion.
     */
    public void setTbmPromotion(com.see.truetransact.clientutil.TableModel tbmPromotion) {
        this.tbmPromotion = tbmPromotion;
    }
    
    /**
     * Getter for property cboPromotedDesignation.
     * @return Value of property cboPromotedDesignation.
     */
    public java.lang.String getCboPromotedDesignation() {
        return cboPromotedDesignation;
    }
    
    /**
     * Setter for property cboPromotedDesignation.
     * @param cboPromotedDesignation New value of property cboPromotedDesignation.
     */
    public void setCboPromotedDesignation(java.lang.String cboPromotedDesignation) {
        this.cboPromotedDesignation = cboPromotedDesignation;
    }
    
    /**
     * Getter for property cbmPromotedDesignation.
     * @return Value of property cbmPromotedDesignation.
     */
    public com.see.truetransact.clientutil.ComboBoxModel getCbmPromotedDesignation() {
        return cbmPromotedDesignation;
    }
    
    /**
     * Setter for property cbmPromotedDesignation.
     * @param cbmPromotedDesignation New value of property cbmPromotedDesignation.
     */
    public void setCbmPromotedDesignation(com.see.truetransact.clientutil.ComboBoxModel cbmPromotedDesignation) {
        this.cbmPromotedDesignation = cbmPromotedDesignation;
    }
    
    /**
     * Getter for property newPromotion.
     * @return Value of property newPromotion.
     */
    public boolean isNewPromotion() {
        return newPromotion;
    }
    
    /**
     * Setter for property newPromotion.
     * @param newPromotion New value of property newPromotion.
     */
    public void setNewPromotion(boolean newPromotion) {
        this.newPromotion = newPromotion;
    }
    
    /**
     * Getter for property tblPromotion.
     * @return Value of property tblPromotion.
     */
    public com.see.truetransact.clientutil.EnhancedTableModel getTblPromotion() {
        return tblPromotion;
    }
    
    /**
     * Setter for property tblPromotion.
     * @param tblPromotion New value of property tblPromotion.
     */
    public void setTblPromotion(com.see.truetransact.clientutil.EnhancedTableModel tblPromotion) {
        this.tblPromotion = tblPromotion;
    }
    
    /**
     * Getter for property txtNewBasic.
     * @return Value of property txtNewBasic.
     */
    public java.lang.String getTxtNewBasic() {
        return txtNewBasic;
    }
    
    /**
     * Setter for property txtNewBasic.
     * @param txtNewBasic New value of property txtNewBasic.
     */
    public void setTxtNewBasic(java.lang.String txtNewBasic) {
        this.txtNewBasic = txtNewBasic;
    }
    
    /**
     * Getter for property txtRemarks.
     * @return Value of property txtRemarks.
     */
    public java.lang.String getTxtRemarks() {
        return txtRemarks;
    }
    
    /**
     * Setter for property txtRemarks.
     * @param txtRemarks New value of property txtRemarks.
     */
    public void setTxtRemarks(java.lang.String txtRemarks) {
        this.txtRemarks = txtRemarks;
    }
    
    /**
     * Getter for property txtPromotionLastGrade.
     * @return Value of property txtPromotionLastGrade.
     */
    public java.lang.String getTxtPromotionLastGrade() {
        return txtPromotionLastGrade;
    }
    
    /**
     * Setter for property txtPromotionLastGrade.
     * @param txtPromotionLastGrade New value of property txtPromotionLastGrade.
     */
    public void setTxtPromotionLastGrade(java.lang.String txtPromotionLastGrade) {
        this.txtPromotionLastGrade = txtPromotionLastGrade;
    }
    
    /**
     * Getter for property operativeId.
     * @return Value of property operativeId.
     */
    public java.lang.String getOperativeId() {
        return operativeId;
    }
    
    /**
     * Setter for property operativeId.
     * @param operativeId New value of property operativeId.
     */
    public void setOperativeId(java.lang.String operativeId) {
        this.operativeId = operativeId;
    }
    
}

