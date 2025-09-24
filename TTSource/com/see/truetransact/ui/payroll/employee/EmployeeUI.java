/*
 * EmployeeUI.java
 *
 *
 */
package com.see.truetransact.ui.payroll.employee;

import com.see.truetransact.clientutil.CMandatoryDialog;
import com.see.truetransact.clientutil.ClientConstants;
import com.see.truetransact.clientutil.ClientUtil;
import com.see.truetransact.clientutil.ComboBoxModel;
import com.see.truetransact.commonutil.CommonConstants;
import com.see.truetransact.commonutil.CommonUtil;
import com.see.truetransact.commonutil.DateUtil;
import com.see.truetransact.transferobject.payroll.employee.SalStructDetailsTO;
import com.see.truetransact.ui.TrueTransactMain;
import com.see.truetransact.ui.common.authorize.AuthorizeUI;
import com.see.truetransact.ui.common.viewall.ViewAll;
import com.see.truetransact.ui.customer.CheckCustomerIdUI;
import com.see.truetransact.uicomponent.CButtonGroup;
import com.see.truetransact.uicomponent.CInternalFrame;
import com.see.truetransact.uimandatory.MandatoryCheck;
import com.see.truetransact.uimandatory.UIMandatoryField;
import java.io.File;
import java.io.FileInputStream;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Observable;
import java.util.logging.Level;
import javax.swing.JFileChooser;
import org.apache.log4j.Logger;
import java.util.Date;

/**
 *
 * @author anjuanand
 */
public class EmployeeUI extends CInternalFrame implements java.util.Observer, UIMandatoryField {

    HashMap mandatoryMap;
    EmployeeOB observable;
    final int EDIT = 0, DELETE = 1, BRANCH = 2, AUTHORIZE = 3, VIEW = 4, CUSTOMER = 5, SALDETAILS = 6, ACCNO = 7, CUSTID = 8;
    boolean isFilled = false;
    int viewType = -1;
    //Data Fields for the Photo/Picture...
    private File selFile;
    private byte[] photoByteArray;
    private StringBuffer fileName;
    private String photoFile;
    SalStructDetailsTO sal = null;
    ArrayList<SalStructDetailsTO> salArray = new ArrayList<SalStructDetailsTO>();
    //Logger
    private final static Logger log = Logger.getLogger(EmployeeUI.class);
    private Date currDt = null;
    private boolean authorize = false;

    /**
     * Creates new form EmployeeUI
     */
    public EmployeeUI() {
        currDt = ClientUtil.getCurrentDate();
        initComponents();
        initSetup();
    }

    private void initSetup() {
        setFieldNames();
        internationalize();
        setMandatoryHashMap();
        setObservable();
        initComponentData();
        setMaxLenths();// To set the Numeric Validation and the Maximum length of the Text fields...
        setPictuteEnableDisable(false);//To Enable the Load Button for Picture...
        this.btnRemove.setEnabled(false);
        lblPicture.setIcon(null);
        new MandatoryCheck().putMandatoryMarks(getClass().getName(), panEmployee);
        observable.resetStatus();// to reset the status
        ClientUtil.enableDisable(this, false);// Disables all when the screen appears for the 1st time
        setButtonEnableDisable();// Enables/Disables the necessary buttons and menu items...
        enableDisableButtons();// To disable the buttons(folder) in the Starting...
        enableTxtFields();
        observable.resetForm();// To reset all the fields in UI...
        setHelpMessage();
        btnCancel.setEnabled(true);
    }

    private void setObservable() {
        observable = EmployeeOB.getInstance();
        observable.addObserver(this);
    }

    private void setFieldNames() {
        btnCancel.setName("btnCancel");
        btnClose.setName("btnClose");
        btnDelete.setName("btnDelete");
        btnEdit.setName("btnEdit");
        btnLoad.setName("btnLoad");
        btnNew.setName("btnNew");
        btnPrint.setName("btnPrint");
        btnRemove.setName("btnRemove");
        btnSave.setName("btnSave");
        btnCustomerId.setName("btnCustomerId");
        txtCity.setName("txtCity");
        txtCountry.setName("txtCountry");
        cboDepartment.setName("cboDepartment");
        cboEmployeeType.setName("cboEmployeeType");
        txtMartialStatus.setName("txtMartialStatus");
        cboJobTitle.setName("cboJobTitle");
        cboManager.setName("cboManager");
        txtState.setName("txtState");
        txtTitle.setName("txtTitle");
        cboAddressType.setName("cboAddressType");
        lblArea.setName("lblArea");
        lblBirthDate.setName("lblBirthDate");
        lblCity.setName("lblCity");
        lblComments.setName("lblComments");
        lblCountry.setName("lblCountry");
        lblDepartment.setName("lblDepartment");
        lblEducation.setName("lblEducation");
        lblEmployeeId.setName("lblEmployeeId");
        lblEmployeeType.setName("lblEmployeeType");
        lblExperience.setName("lblExperience");
        lblFirstName.setName("lblFirstName");
        lblGender.setName("lblGender");
        lblJobTitle.setName("lblJobTitle");
        lblMartialStatus.setName("lblMartialStatus");
        lblLastName.setName("lblLastName");
        lblManager.setName("lblManager");
        lblMsg.setName("lblMsg");
        lblOfficePhone.setName("lblOfficePhone");
        lblOfficialEmail.setName("lblOfficialEmail");
        lblPanNo.setName("lblPanNo");
        lblPassPortNo.setName("lblPassPortNo");
        lblPerformance.setName("lblPerformance");
        lblPicture.setName("lblPicture");
        lblPinCode.setName("lblPinCode");
        lblResponsibility.setName("lblResponsibility");
        lblSkills.setName("lblSkills");
        lblSpace1.setName("lblSpace1");
        lblSpace2.setName("lblSpace2");
        lblSpace3.setName("lblSpace3");
        lblSpaces.setName("lblSpaces");
        lblSsnNo.setName("lblSsnNo");
        lblState.setName("lblState");
        lblStatus1.setName("lblStatus1");
        lblStreet.setName("lblStreet");
        lblTitle.setName("lblTitle");
        lblCustomerId.setName("lblCustomerId");
        lblAddressType.setName("lblAddressType");
        mbrLoanProduct.setName("mbrLoanProduct");
        panAddress.setName("panAddress");
        panButton.setName("panButton");
        panEmployee.setName("panEmployee");
        panEmployeeData.setName("panEmployeeData");
        panEmployeeDetails.setName("panEmployeeDetails");
        panEmployeeInfo.setName("panEmployeeInfo");
        panGender.setName("panGender");
        panGeneraInfo.setName("panGeneraInfol");
        panPerformance.setName("panPerformance");
        panPersonalNumbers.setName("panPersonalNumbers");
        panPicture.setName("panPicture");
        panOtherDetails.setName("panSkills");
        panStatus.setName("panStatus");
        panCustomerId.setName("panCustomerId");
        rdoGender_Female.setName("rdoGender_Female");
        rdoGender_Male.setName("rdoGender_Male");
        sptSkills_Hori.setName("sptSkills_Hori");
        srpComments.setName("srpComments");
        srpEducation.setName("srpEducation");
        srpExperience.setName("srpExperience");
        srpPerformance.setName("srpPerformance");
        srpPicture.setName("srpPicture");
        srpResponsibility.setName("srpResponsibility");
        tabEmployee.setName("tabEmployee");
        tdtBirthDate.setName("tdtBirthDate");
        txaComments.setName("txaComments");
        txaEducation.setName("txaEducation");
        txaExperience.setName("txaExperience");
        txaPerformance.setName("txaPerformance");
        txaResponsibility.setName("txaResponsibility");
        txaSkills.setName("txaSkills");
        txtArea.setName("txtArea");
        txtEmployeeId.setName("txtEmployeeId");
        txtFirstName.setName("txtFirstName");
        txtLastName.setName("txtLastName");
        txtOfficePhone.setName("txtOfficePhone");
        txtOfficialEmail.setName("txtOfficialEmail");
        txtPanNo.setName("txtPanNo");
        txtPassPortNo.setName("txtPassPortNo");
        txtPinCode.setName("txtPinCode");
        txtSsnNo.setName("txtSsnNo");
        txtStreet.setName("txtStreet");
        txtCustomerId.setName("txtCustomerId");
        lblMiddleName.setName("lblMiddleName");
        txtMiddleName.setName("txtMiddleName");
        lblDesignation.setName("lblDesignation");
        lblScaleId.setName("lblScaleId");
        lblEffectiveDate.setName("lblEffectiveDate");
        lblIncrementCount.setName("lblIncrementCount");
        lblPresentBasicSalary.setName("lblPresentBasicSalary");
        lblLastIncrementDate.setName("lblLastIncrementDate");
        lblNextIncrementDate.setName("lblNextIncrementDate");
        lblDateOfJoin.setName("lblDateOfJoin");
        lblProbationEndDate.setName("lblProbationEndDate");
        lblDateOfRetirement.setName("lblDateOfRetirement");
        lblNetSalaryProductType.setName("lblNetSalaryProductType");
        lblNetSalaryProductId.setName("lblNetSalaryProductId");
        lblNetSalaryAccNo.setName("lblNetSalaryAccNo");
        lblCustomerName.setName("lblCustomerName");
        lblPensionCodeNo.setName("lblPensionCodeNo");
        lblPensionOpeningBalance.setName("lblPensionOpeningBalance");
        lblPensionOpeningBalanceOn.setName("lblPensionOpeningBalanceOn");
        lblWfNumber.setName("lblWfNumber");
        lblWfOpeningBalance.setName("lblWfOpeningBalance");
        lblWfOpeningBalanceOn.setName("lblWfOpeningBalanceOn");
        lblStatusOfEmp.setName("lblStatusOfEmp");
        cboDesignation.setName("cboDesignation");
        txtScaleId.setName("txtScaleId");
        txtIncrementCount.setName("txtIncrementCount");
        txtPresentBasicSalary.setName("txtPresentBasicSalary");
        cboNetSalaryProductType.setName("cboNetSalaryProductType");
        cboNetSalaryProductId.setName("cboNetSalaryProductId");
        txtNetSalaryAccNo.setName("txtNetSalaryAccNo");
        txtCustomerName.setName("txtCustomerName");
        txtPensionCodeNo.setName("txtPensionCodeNo");
        txtPensionOpeningBalance.setName("txtPensionOpeningBalance");
        txtWfNumber.setName("txtWfNumber");
        txtWfOpeningBalance.setName("txtWfOpeningBalance");
        cboStatusOfEmp.setName("cboStatusOfEmp");
        chkDAApplicable.setName("chkDAApplicable");
        chkHRAApplicable.setName("chkHRAApplicable");
        chkStopPayt.setName("chkStopPayt");
        tdtDateOfJoin.setName("tdtDateOfJoin");
        tdtProbationEndDate.setName("tdtProbationEndDate");
        tdtDateOfRetirement.setName("tdtDateOfRetirement");
        tdtEffectiveDate.setName("tdtEffectiveDate");
        tdtLastIncrementDate.setName("tdtLastIncrementDate");
        tdtNextIncrementDate.setName("tdtNextIncrementDate");
        tdtPensionOpeningBalanceOn.setName("tdtPensionOpeningBalanceOn");
        tdtWfOpeningBalanceOn.setName("tdtWfOpeningBalanceOn");
        lblSortOrder.setName("lblSortOrder");
        txtSortOrder.setName("txtSortOrder");
        lblFatherName.setName("lblFatherName");
        txtFatherName.setName("txtFatherName");
        lblMotherName.setName("lblMotherName");
        txtMotherName.setName("txtMotherName");
        lblSpouseName.setName("lblSpouseName");
        txtSpouseName.setName("txtSpouseName");
        lblSpouseRelation.setName("lblSpouseRelation");
        txtSpouseRelation.setName("txtSpouseRelation");
        lblPlaceofBirth.setName("lblPlaceofBirth");
        txtPlaceofBirth.setName("txtPlaceofBirth");
        lblReligion.setName("lblReligion");
        txtReligion.setName("txtReligion");
        lblCaste.setName("lblCaste");
        txtCaste.setName("txtCaste");
        lblBloodGroup.setName("lblBloodGroup");
        cboBloodGroup.setName("cboBloodGroup");
        lblIdentificationMark1.setName("lblIdentificationMark1");
        lblIdentificationMark2.setName("lblIdentificationMark2");
        lblPhysicallyHandicapped.setName("lblPhysicallyHandicapped");
        lblMajorHealthProblem.setName("lblMajorHealthProblem");
        txaIdentificationMark1.setName("txaIdentificationMark1");
        txaIdentificationMark2.setName("txaIdentificationMark2");
        txaPhysicallyHandicapped.setName("txaPhysicallyHandicapped");
        txaMajorHealthProblem.setName("txaMajorHealthProblem");
        panPhysicalDetails.setName("panPhysicalDetails");
        panEmploymentData.setName("panEmpDet");
        panJobDetails.setName("panJobDet");
        panGeneraInfo.setName("panGeneraInfo");
        panPersonalNumbers.setName("panPersonalNumbers");
        panDesgDetails.setName("panDesgDetails");
        panApplicable.setName("panApplicable");
        panAccData.setName("panAccData");
        panSalaryData.setName("panSalaryData");
        panPensionData.setName("panPensionData");
        cboFatherTitle.setName("cboFatherTitle");
        cboMotherTitle.setName("cboMotherTitle");
        lblCommunicationAddressType.setName("lblCommunicationAddressType");
        cboCommAddressType.setName("cboCommunicationAddressType");
        lblVersionNo.setName("lblVersionNo");
        txtVersionNo.setName("txtVersionNo");
        lblBranch.setName("lblBranch");
        cboBranch.setName("cboBranch");
    }

    private void internationalize() {
        EmployeeRB resourceBundle = new EmployeeRB();
        btnClose.setText(resourceBundle.getString("btnClose"));
        lblEmployeeId.setText(resourceBundle.getString("lblEmployeeId"));
        lblResponsibility.setText(resourceBundle.getString("lblResponsibility"));
        lblMsg.setText(resourceBundle.getString("lblMsg"));
        lblPassPortNo.setText(resourceBundle.getString("lblPassPortNo"));
        lblPerformance.setText(resourceBundle.getString("lblPerformance"));
        rdoGender_Male.setText(resourceBundle.getString("rdoGender_Male"));
        lblSpace2.setText(resourceBundle.getString("lblSpace2"));
        lblSpace3.setText(resourceBundle.getString("lblSpace3"));
        lblSpace1.setText(resourceBundle.getString("lblSpace1"));
        lblDepartment.setText(resourceBundle.getString("lblDepartment"));
        lblExperience.setText(resourceBundle.getString("lblExperience"));
        lblOfficePhone.setText(resourceBundle.getString("lblOfficePhone"));
        lblCountry.setText(resourceBundle.getString("lblCountry"));
        btnEdit.setText(resourceBundle.getString("btnEdit"));
        lblMartialStatus.setText(resourceBundle.getString("lblMartialStatus"));
        lblSpaces.setText(resourceBundle.getString("lblSpaces"));
        btnRemove.setText(resourceBundle.getString("btnRemove"));
        lblBirthDate.setText(resourceBundle.getString("lblBirthDate"));
        lblEducation.setText(resourceBundle.getString("lblEducation"));
        lblSkills.setText(resourceBundle.getString("lblSkills"));
        lblPicture.setText(resourceBundle.getString("lblPicture"));
        lblGender.setText(resourceBundle.getString("lblGender"));
        lblManager.setText(resourceBundle.getString("lblManager"));
        ((javax.swing.border.TitledBorder) panPicture.getBorder()).setTitle(resourceBundle.getString("panPicture"));
        lblLastName.setText(resourceBundle.getString("lblLastName"));
        btnPrint.setText(resourceBundle.getString("btnPrint"));
        lblStreet.setText(resourceBundle.getString("lblStreet"));
        lblCity.setText(resourceBundle.getString("lblCity"));
        lblStatus1.setText(resourceBundle.getString("lblStatus1"));
        lblPanNo.setText(resourceBundle.getString("lblPanNo"));
        lblAddressType.setText(resourceBundle.getString("lblAddressType"));
        rdoGender_Female.setText(resourceBundle.getString("rdoGender_Female"));
        btnSave.setText(resourceBundle.getString("btnSave"));
        lblTitle.setText(resourceBundle.getString("lblTitle"));
        btnCustomerId.setText(resourceBundle.getString("btnCustomerId"));
        lblOfficialEmail.setText(resourceBundle.getString("lblOfficialEmail"));
        lblArea.setText(resourceBundle.getString("lblArea"));
        lblPinCode.setText(resourceBundle.getString("lblPinCode"));
        btnLoad.setText(resourceBundle.getString("btnLoad"));
        btnDelete.setText(resourceBundle.getString("btnDelete"));
        lblComments.setText(resourceBundle.getString("lblComments"));
        ((javax.swing.border.TitledBorder) panAddress.getBorder()).setTitle(resourceBundle.getString("panAddress"));
        btnNew.setText(resourceBundle.getString("btnNew"));
        lblSsnNo.setText(resourceBundle.getString("lblSsnNo"));
        btnCancel.setText(resourceBundle.getString("btnCancel"));
        lblState.setText(resourceBundle.getString("lblState"));
        lblJobTitle.setText(resourceBundle.getString("lblJobTitle"));
        lblEmployeeType.setText(resourceBundle.getString("lblEmployeeType"));
        lblFirstName.setText(resourceBundle.getString("lblFirstName"));
        lblMiddleName.setText(resourceBundle.getString("lblMiddleName"));
        lblCustomerId.setText(resourceBundle.getString("lblCustomerId"));
        lblDesignation.setText(resourceBundle.getString("lblDesignation"));
        lblScaleId.setText(resourceBundle.getString("lblScaleId"));
        lblEffectiveDate.setText(resourceBundle.getString("lblEffectiveDate"));
        lblIncrementCount.setText(resourceBundle.getString("lblIncrementCount"));
        lblPresentBasicSalary.setText(resourceBundle.getString("lblPresentBasicSalary"));
        lblLastIncrementDate.setText(resourceBundle.getString("lblLastIncrementDate"));
        lblNextIncrementDate.setText(resourceBundle.getString("lblNextIncrementDate"));
        lblDateOfJoin.setText(resourceBundle.getString("lblDateOfJoin"));
        lblProbationEndDate.setText(resourceBundle.getString("lblProbationEndDate"));
        lblDateOfRetirement.setText(resourceBundle.getString("lblDateOfRetirement"));
        lblNetSalaryProductType.setText(resourceBundle.getString("lblNetSalaryProductType"));
        lblNetSalaryProductId.setText(resourceBundle.getString("lblNetSalaryProductId"));
        lblNetSalaryAccNo.setText(resourceBundle.getString("lblNetSalaryAccNo"));
        lblCustomerName.setText(resourceBundle.getString("lblCustomerName"));
        lblPensionCodeNo.setText(resourceBundle.getString("lblPensionCodeNo"));
        lblPensionOpeningBalance.setText(resourceBundle.getString("lblPensionOpeningBalance"));
        lblPensionOpeningBalanceOn.setText(resourceBundle.getString("lblPensionOpeningBalanceOn"));
        lblWfNumber.setText(resourceBundle.getString("lblWfNumber"));
        lblWfOpeningBalance.setText(resourceBundle.getString("lblWfOpeningBalance"));
        lblWfOpeningBalanceOn.setText(resourceBundle.getString("lblWfOpeningBalanceOn"));
        lblStatusOfEmp.setText(resourceBundle.getString("lblStatusOfEmp"));
        chkDAApplicable.setText(resourceBundle.getString("chkDAApplicable"));
        chkHRAApplicable.setText(resourceBundle.getString("chkHRAApplicable"));
        chkStopPayt.setText(resourceBundle.getString("chkStopPayt"));
        lblSortOrder.setText(resourceBundle.getString("lblSortOrder"));
        lblFatherName.setText(resourceBundle.getString("lblFatherName"));
        lblMotherName.setText(resourceBundle.getString("lblMotherName"));
        lblSpouseName.setText(resourceBundle.getString("lblSpouseName"));
        lblSpouseRelation.setText(resourceBundle.getString("lblSpouseRelation"));
        lblPlaceofBirth.setText(resourceBundle.getString("lblPlaceofBirth"));
        lblReligion.setText(resourceBundle.getString("lblReligion"));
        lblCaste.setText(resourceBundle.getString("lblCaste"));
        lblBloodGroup.setText(resourceBundle.getString("lblBloodGroup"));
        lblIdentificationMark1.setText(resourceBundle.getString("lblIdentificationMark1"));
        lblIdentificationMark2.setText(resourceBundle.getString("lblIdentificationMark2"));
        lblPhysicallyHandicapped.setText(resourceBundle.getString("lblPhysicallyHandicapped"));
        lblMajorHealthProblem.setText(resourceBundle.getString("lblMajorHealthProblem"));
        lblCommunicationAddressType.setText(resourceBundle.getString("lblCommunicationAddressType"));
        lblVersionNo.setText(resourceBundle.getString("lblVersionNo"));
    }

    public void setMandatoryHashMap() {
        mandatoryMap = new HashMap();
        mandatoryMap.put("txtCustomerId", new Boolean(true));
        mandatoryMap.put("cboBranch", new Boolean(true));
        mandatoryMap.put("txtFirstName", new Boolean(true));
        mandatoryMap.put("cboEmployeeType", new Boolean(true));
        mandatoryMap.put("cboJobTitle", new Boolean(true));
        mandatoryMap.put("cboDepartment", new Boolean(true));
        mandatoryMap.put("txtOfficialEmail", new Boolean(true));
        mandatoryMap.put("txtOfficePhone", new Boolean(true));
        mandatoryMap.put("tdtBirthDate", new Boolean(true));
        mandatoryMap.put("cboDesignation", new Boolean(true));
        mandatoryMap.put("txtScaleId", new Boolean(true));
        mandatoryMap.put("tdtEffectiveDate", new Boolean(true));
        mandatoryMap.put("txtIncrementCount", new Boolean(true));
        mandatoryMap.put("txtPresentBasicSalary", new Boolean(true));
        mandatoryMap.put("tdtLastIncrementDate", new Boolean(true));
        mandatoryMap.put("tdtNextIncrementDate", new Boolean(true));
        mandatoryMap.put("tdtDateOfJoin", new Boolean(true));
        mandatoryMap.put("tdtProbationEndDate", new Boolean(true));
        mandatoryMap.put("cboStatusOfEmp", new Boolean(true));
        mandatoryMap.put("txtPensionCodeNo", new Boolean(true));
        mandatoryMap.put("txtWfNumber", new Boolean(true));
        mandatoryMap.put("cboManager", new Boolean(true));
    }

    public HashMap getMandatoryHashMap() {
        return mandatoryMap;
    }

    private void removeRadioButtons() {
        rdoGender.remove(rdoGender_Female);
        rdoGender.remove(rdoGender_Male);
    }

    private void addRadioButtons() {
        rdoGender = new CButtonGroup();
        rdoGender.add(rdoGender_Female);
        rdoGender.add(rdoGender_Male);
    }

    public void update(Observable observed, Object arg) {
        removeRadioButtons();
        txtEmployeeId.setText(observable.getTxtEmployeeId());
        txtCustomerId.setText(observable.getTxtCustomerId());
        txtTitle.setText(observable.getTxtTitle());
        txtFirstName.setText(observable.getTxtFirstName());
        txtMiddleName.setText(observable.getTxtMiddleName());
        txtLastName.setText(observable.getTxtLastName());
        rdoGender_Male.setSelected(observable.getRdoGender_Male());
        rdoGender_Female.setSelected(observable.getRdoGender_Female());
        cboEmployeeType.setSelectedItem(observable.getCboEmployeeType());
        txtMartialStatus.setText(observable.getTxtMartialStatus());
        cboJobTitle.setSelectedItem(observable.getCboJobTitle());
        cboDepartment.setSelectedItem(observable.getCboDepartment());
        cboManager.setSelectedItem(observable.getCboManager());
        txtOfficialEmail.setText(observable.getTxtOfficialEmail());
        txtOfficePhone.setText(observable.getTxtOfficePhone());
        txtStreet.setText(observable.getTxtStreet());
        txtArea.setText(observable.getTxtArea());
        txtCity.setText(observable.getTxtCity());
        cboAddressType.setSelectedItem(observable.getCboAddressType());
        txtState.setText(observable.getTxtState());
        txtCountry.setText(observable.getTxtCountry());
        txtPinCode.setText(observable.getTxtPinCode());
        tdtBirthDate.setDateValue(observable.getTdtBirthDate());
        txtPanNo.setText(observable.getTxtPanNo());
        txtSsnNo.setText(observable.getTxtSsnNo());
        txtPassPortNo.setText(observable.getTxtPassPortNo());
        txaSkills.setText(observable.getTxaSkills());
        txaEducation.setText(observable.getTxaEducation());
        txaExperience.setText(observable.getTxaExperience());
        txaResponsibility.setText(observable.getTxaResponsibility());
        txaPerformance.setText(observable.getTxaPerformance());
        txaComments.setText(observable.getTxaComments());
        lblStatus1.setText(observable.getLblStatus());
        txtIncrementCount.setText(observable.getTxtIncrementCount());
        txtPresentBasicSalary.setText(observable.getTxtPresentBasicSalary());
        txtNetSalaryAccNo.setText(observable.getTxtNetSalaryAccountNo());
        txtCustomerName.setText(observable.getTxtCustomerName());
        txtPensionCodeNo.setText(observable.getTxtPensionCodeNo());
        txtPensionOpeningBalance.setText(observable.getTxtPensionOpeningBalance());
        txtWfNumber.setText(observable.getTxtWfNo());
        txtWfOpeningBalance.setText(observable.getTxtWfOpeningBalance());
        txtScaleId.setText(observable.getTxtScaleId());
        txtVersionNo.setText(observable.getTxtVersionNo());
        tdtEffectiveDate.setDateValue(observable.getTdtEffectiveDate());
        tdtLastIncrementDate.setDateValue(observable.getTdtLastIncrementDate());
        tdtNextIncrementDate.setDateValue(observable.getTdtNextIncrementDate());
        tdtDateOfJoin.setDateValue(observable.getTdtDateOfJoin());
        tdtProbationEndDate.setDateValue(observable.getTdtProbationEndDate());
        tdtDateOfRetirement.setDateValue(observable.getTdtDateOfRetirement());
        tdtPensionOpeningBalanceOn.setDateValue(observable.getTdtPensionOpeningBalanceOn());
        tdtWfOpeningBalanceOn.setDateValue(observable.getTdtWfOpeningBalanceOn());
        cboDesignation.setSelectedItem(observable.getCboDesignation());
        cboNetSalaryProductType.setSelectedItem(observable.getCboNetSalaryProductType());
        cboNetSalaryProductId.setSelectedItem(observable.getCbmNetSalaryProductId().getDataForKey(observable.getCboNetSalaryProductId()));
        cboStatusOfEmp.setSelectedItem(observable.getCboStatusOfEmp());
        chkDAApplicable.setSelected(observable.isDaAppl());
        chkHRAApplicable.setSelected(observable.isHraAppl());
        chkStopPayt.setSelected(observable.isStopPayt());
        txtSortOrder.setText(CommonUtil.convertObjToStr(observable.getTxtSortOrder()));
        txtFatherName.setText(observable.getTxtFatherName());
        txtMotherName.setText(observable.getTxtMotherName());
        txtSpouseName.setText(observable.getTxtSpouseName());
        txtSpouseRelation.setText(observable.getTxtSpouseRelation());
        txtPlaceofBirth.setText(observable.getTxtPlaceofBirth());
        txtReligion.setText(observable.getTxtReligion());
        txtCaste.setText(observable.getTxtCaste());
        txaIdentificationMark1.setText(observable.getTxaIdentificationMark1());
        txaIdentificationMark2.setText(observable.getTxaIdentificationMark2());
        txaPhysicallyHandicapped.setText(observable.getTxaPhysicallyHandicapped());
        txaMajorHealthProblem.setText(observable.getTxaMajorHealthProblem());
        cboBloodGroup.setSelectedItem(observable.getCboBloodGroup());
        cboFatherTitle.setSelectedItem(observable.getCboFatherTitle());
        cboMotherTitle.setSelectedItem(observable.getCboMotherTitle());
        txaMajorHealthProblem.setText(observable.getTxaMajorHealthProblem());
        cboMotherTitle.setSelectedItem(observable.getCboMotherTitle());
        cboCommAddressType.setSelectedItem(observable.getCboCommAddressType());
        cboBranch.setSelectedItem(observable.getCboBranchCode());
        try {
            if (observable.getLblPhoto() != null && observable.getLblPhoto().length() > 0) {
                lblPicture.setIcon(new javax.swing.ImageIcon(new java.net.URL(observable.getLblPhoto())));
            }
        } catch (Exception e) {
            log.info("Exception in Picture update");
        }
        addRadioButtons();
    }

    public void updateOBFields() {
        observable.setTxtEmployeeId(txtEmployeeId.getText());
        observable.setTxtCustomerId(txtCustomerId.getText());
        observable.setTxtTitle(txtTitle.getText());
        observable.setTxtFirstName(txtFirstName.getText());
        observable.setTxtMiddleName(txtMiddleName.getText());
        observable.setTxtLastName(txtLastName.getText());
        observable.setRdoGender_Male(rdoGender_Male.isSelected());
        observable.setRdoGender_Female(rdoGender_Female.isSelected());
        observable.setCboEmployeeType(CommonUtil.convertObjToStr(cboEmployeeType.getSelectedItem()));
        observable.setTxtMartialStatus(txtMartialStatus.getText());
        observable.setCboJobTitle(CommonUtil.convertObjToStr(cboJobTitle.getSelectedItem()));
        observable.setCboDepartment(CommonUtil.convertObjToStr(cboDepartment.getSelectedItem()));
        observable.setCboManager(CommonUtil.convertObjToStr(cboManager.getSelectedItem()));
        observable.setTxtOfficialEmail(txtOfficialEmail.getText());
        observable.setTxtOfficePhone(txtOfficePhone.getText());
        observable.setTxtStreet(txtStreet.getText());
        observable.setTxtArea(txtArea.getText());
        observable.setTxtCity(txtCity.getText());
        observable.setCboAddressType(CommonUtil.convertObjToStr(cboAddressType.getSelectedItem()));
        observable.setTxtState(txtState.getText());
        observable.setTxtCountry(txtCountry.getText());
        observable.setTxtPinCode(txtPinCode.getText());
        observable.setTdtBirthDate(tdtBirthDate.getDateValue());
        observable.setTxtPanNo(txtPanNo.getText());
        observable.setTxtSsnNo(txtSsnNo.getText());
        observable.setTxtPassPortNo(txtPassPortNo.getText());
        observable.setTxaSkills(txaSkills.getText());
        observable.setTxaEducation(txaEducation.getText());
        observable.setTxaExperience(txaExperience.getText());
        observable.setTxaResponsibility(txaResponsibility.getText());
        observable.setTxaPerformance(txaPerformance.getText());
        observable.setTxaComments(txaComments.getText());
        observable.setPhotoFile(photoFile);
        observable.setPhotoByteArray(photoByteArray);
        observable.setTxtIncrementCount(txtIncrementCount.getText());
        observable.setTxtPresentBasicSalary(txtPresentBasicSalary.getText());
        observable.setTxtNetSalaryAccountNo(txtNetSalaryAccNo.getText());
        observable.setTxtPensionCodeNo(txtPensionCodeNo.getText());
        observable.setTxtPensionOpeningBalance(txtPensionOpeningBalance.getText());
        observable.setTxtWfNo(txtWfNumber.getText());
        observable.setTxtWfOpeningBalance(txtWfOpeningBalance.getText());
        observable.setTxtScaleId(txtScaleId.getText());
        observable.setTdtEffectiveDate(tdtEffectiveDate.getDateValue());
        observable.setTdtLastIncrementDate(tdtLastIncrementDate.getDateValue());
        observable.setTdtNextIncrementDate(tdtNextIncrementDate.getDateValue());
        observable.setTdtDateOfJoin(tdtDateOfJoin.getDateValue());
        observable.setTdtProbationEndDate(tdtProbationEndDate.getDateValue());
        observable.setTdtDateOfRetirement(tdtDateOfRetirement.getDateValue());
        observable.setTdtPensionOpeningBalanceOn(tdtPensionOpeningBalanceOn.getDateValue());
        observable.setTdtWfOpeningBalanceOn(tdtWfOpeningBalanceOn.getDateValue());
        observable.setCboDesignation(CommonUtil.convertObjToStr(cboDesignation.getSelectedItem()));
        observable.setCboNetSalaryProductType(CommonUtil.convertObjToStr(cboNetSalaryProductType.getSelectedItem()));
        observable.setCboNetSalaryProductId(CommonUtil.convertObjToStr(cboNetSalaryProductId.getSelectedItem()));
        observable.setCboStatusOfEmp(CommonUtil.convertObjToStr(cboStatusOfEmp.getSelectedItem()));
        observable.setDaAppl(chkDAApplicable.isSelected());
        observable.setHraAppl(chkHRAApplicable.isSelected());
        observable.setStopPayt(chkStopPayt.isSelected());
        observable.setTxtSortOrder(CommonUtil.convertObjToInt(txtSortOrder.getText()));
        observable.setTxtFatherName(txtFatherName.getText());
        observable.setTxtMotherName(txtMotherName.getText());
        observable.setTxtSpouseName(txtSpouseName.getText());
        observable.setTxtSpouseRelation(txtSpouseRelation.getText());
        observable.setTxtPlaceofBirth(txtPlaceofBirth.getText());
        observable.setTxtReligion(txtReligion.getText());
        observable.setTxtCaste(txtCaste.getText());
        observable.setTxaIdentificationMark1(txaIdentificationMark1.getText());
        observable.setTxaIdentificationMark2(txaIdentificationMark2.getText());
        observable.setTxaPhysicallyHandicapped(txaPhysicallyHandicapped.getText());
        observable.setTxaMajorHealthProblem(txaMajorHealthProblem.getText());
        observable.setCboBloodGroup(CommonUtil.convertObjToStr(cboBloodGroup.getSelectedItem()));
        observable.setCboFatherTitle(CommonUtil.convertObjToStr(cboFatherTitle.getSelectedItem()));
        observable.setCboMotherTitle(CommonUtil.convertObjToStr(cboMotherTitle.getSelectedItem()));
        observable.setCboCommAddressType(CommonUtil.convertObjToStr(cboCommAddressType.getSelectedItem()));
        observable.setCboBranchCode(CommonUtil.convertObjToStr(cboBranch.getSelectedItem()));
        if (fileName != null && fileName.length() > 0) {
            observable.setLblPhoto(CommonUtil.convertObjToStr(fileName));
        }
    }

    public void setHelpMessage() {
        EmployeeMRB objMandatoryRB = new EmployeeMRB();
        rdoGender_Male.setHelpMessage(lblMsg, objMandatoryRB.getString("rdoGender_Male"));
        cboEmployeeType.setHelpMessage(lblMsg, objMandatoryRB.getString("cboEmployeeType"));
        cboJobTitle.setHelpMessage(lblMsg, objMandatoryRB.getString("cboJobTitle"));
        cboDepartment.setHelpMessage(lblMsg, objMandatoryRB.getString("cboDepartment"));
        cboManager.setHelpMessage(lblMsg, objMandatoryRB.getString("cboManager"));
        tdtBirthDate.setHelpMessage(lblMsg, objMandatoryRB.getString("tdtBirthDate"));
        txtPanNo.setHelpMessage(lblMsg, objMandatoryRB.getString("txtPanNo"));
        txtSsnNo.setHelpMessage(lblMsg, objMandatoryRB.getString("txtSsnNo"));
        txtPassPortNo.setHelpMessage(lblMsg, objMandatoryRB.getString("txtPassPortNo"));
        txaSkills.setHelpMessage(lblMsg, objMandatoryRB.getString("txaSkills"));
        txaEducation.setHelpMessage(lblMsg, objMandatoryRB.getString("txaEducation"));
        txaExperience.setHelpMessage(lblMsg, objMandatoryRB.getString("txaExperience"));
        txaResponsibility.setHelpMessage(lblMsg, objMandatoryRB.getString("txaResponsibility"));
        txaPerformance.setHelpMessage(lblMsg, objMandatoryRB.getString("txaPerformance"));
        txaComments.setHelpMessage(lblMsg, objMandatoryRB.getString("txaComments"));
        txtCustomerId.setHelpMessage(lblMsg, objMandatoryRB.getString("txtCustomerId"));
        tdtLastIncrementDate.setHelpMessage(lblMsg, objMandatoryRB.getString("tdtLastIncrementDate"));
        tdtNextIncrementDate.setHelpMessage(lblMsg, objMandatoryRB.getString("tdtNextIncrementDate"));
        cboDesignation.setHelpMessage(lblMsg, objMandatoryRB.getString("cboDesignation"));
        tdtEffectiveDate.setHelpMessage(lblMsg, objMandatoryRB.getString("tdtEffectiveDate"));
        txtIncrementCount.setHelpMessage(lblMsg, objMandatoryRB.getString("txtIncrementCount"));
        txtPresentBasicSalary.setHelpMessage(lblMsg, objMandatoryRB.getString("txtPresentBasicSalary"));
        tdtDateOfJoin.setHelpMessage(lblMsg, objMandatoryRB.getString("tdtDateOfJoin"));
        tdtProbationEndDate.setHelpMessage(lblMsg, objMandatoryRB.getString("tdtProbationEndDate"));
        cboStatusOfEmp.setHelpMessage(lblMsg, objMandatoryRB.getString("cboStatusOfEmp"));
        txtPensionCodeNo.setHelpMessage(lblMsg, objMandatoryRB.getString("txtPensionCodeNo"));
        txtWfNumber.setHelpMessage(lblMsg, objMandatoryRB.getString("txtWfNumber"));
        cboBranch.setHelpMessage(lblMsg, objMandatoryRB.getString("cboBranch"));
    }

    // To fill the Data into the Combo Boxes...
    // it invokes the Combo Box model defined in OB class...
    private void initComponentData() {
        txtIncrementCount.setAllowNumber(true);
        txtPensionOpeningBalance.setAllowNumber(true);
        txtWfOpeningBalance.setAllowNumber(true);
        txtPresentBasicSalary.setAllowNumber(true);
        txtSortOrder.setAllowNumber(true);
        txtOfficePhone.setAllowNumber(true);
        txtPensionCodeNo.setAllowAll(true);
        txtWfNumber.setAllowAll(true);
        cboEmployeeType.setModel(observable.getCbmEmployeeType());
        cboJobTitle.setModel(observable.getCbmJobTitle());
        cboDepartment.setModel(observable.getCbmDepartment());
        cboManager.setModel(observable.getCbmManager());
        cboDesignation.setModel(observable.getCbmDesignation());
        cboNetSalaryProductType.setModel(observable.getCbmNetSalaryProductType());
        cboFatherTitle.setModel(observable.getCbmFatherTitle());
        cboMotherTitle.setModel(observable.getCbmMotherTitle());
        cboCommAddressType.setModel(observable.getCbmCommAddressType());
        observable.setBranchCode();
        cboBranch.setModel(observable.getCbmBranchCode());
    }

    //To set the length of the Text Fields and Numeric Validation...
    private void setMaxLenths() {
        txtPanNo.setMaxLength(16);
        txtSsnNo.setMaxLength(16);
        txtPassPortNo.setMaxLength(16);
    }

    // To set The Value of the Buttons Depending on the Value or Condition...
    private void setButtonEnableDisable() {
        btnNew.setEnabled(!btnNew.isEnabled());
        btnEdit.setEnabled(!btnEdit.isEnabled());
        btnDelete.setEnabled(!btnDelete.isEnabled());
        mitNew.setEnabled(btnNew.isEnabled());
        mitEdit.setEnabled(btnEdit.isEnabled());
        mitDelete.setEnabled(btnDelete.isEnabled());
        btnSave.setEnabled(!btnNew.isEnabled());
        btnCancel.setEnabled(!btnNew.isEnabled());
        mitSave.setEnabled(btnSave.isEnabled());
        mitCancel.setEnabled(btnCancel.isEnabled());
        btnView.setEnabled(!btnView.isEnabled());
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {
        java.awt.GridBagConstraints gridBagConstraints;

        rdoGender = new com.see.truetransact.uicomponent.CButtonGroup();
        panEmployee = new com.see.truetransact.uicomponent.CPanel();
        lblSpaces = new com.see.truetransact.uicomponent.CLabel();
        tabEmployee = new com.see.truetransact.uicomponent.CTabbedPane();
        panEmployeeDetails = new com.see.truetransact.uicomponent.CPanel();
        panEmployeeData = new com.see.truetransact.uicomponent.CPanel();
        lblEmployeeId = new com.see.truetransact.uicomponent.CLabel();
        txtEmployeeId = new com.see.truetransact.uicomponent.CTextField();
        lblTitle = new com.see.truetransact.uicomponent.CLabel();
        lblFirstName = new com.see.truetransact.uicomponent.CLabel();
        lblLastName = new com.see.truetransact.uicomponent.CLabel();
        lblGender = new com.see.truetransact.uicomponent.CLabel();
        panGender = new com.see.truetransact.uicomponent.CPanel();
        rdoGender_Male = new com.see.truetransact.uicomponent.CRadioButton();
        rdoGender_Female = new com.see.truetransact.uicomponent.CRadioButton();
        lblMartialStatus = new com.see.truetransact.uicomponent.CLabel();
        lblOfficialEmail = new com.see.truetransact.uicomponent.CLabel();
        lblCustomerId = new com.see.truetransact.uicomponent.CLabel();
        panCustomerId = new com.see.truetransact.uicomponent.CPanel();
        txtCustomerId = new com.see.truetransact.uicomponent.CTextField();
        btnCustomerId = new com.see.truetransact.uicomponent.CButton();
        lblMiddleName = new com.see.truetransact.uicomponent.CLabel();
        txtMiddleName = new com.see.truetransact.uicomponent.CLabel();
        txtFirstName = new com.see.truetransact.uicomponent.CLabel();
        txtTitle = new com.see.truetransact.uicomponent.CLabel();
        txtLastName = new com.see.truetransact.uicomponent.CLabel();
        txtMartialStatus = new com.see.truetransact.uicomponent.CLabel();
        txtOfficialEmail = new com.see.truetransact.uicomponent.CLabel();
        lblSortOrder = new javax.swing.JLabel();
        lblFatherName = new javax.swing.JLabel();
        lblMotherName = new javax.swing.JLabel();
        lblSpouseName = new javax.swing.JLabel();
        lblSpouseRelation = new javax.swing.JLabel();
        lblPlaceofBirth = new javax.swing.JLabel();
        txtSortOrder = new com.see.truetransact.uicomponent.CTextField();
        txtFatherName = new com.see.truetransact.uicomponent.CTextField();
        txtMotherName = new com.see.truetransact.uicomponent.CTextField();
        txtSpouseName = new com.see.truetransact.uicomponent.CTextField();
        txtSpouseRelation = new com.see.truetransact.uicomponent.CTextField();
        txtPlaceofBirth = new com.see.truetransact.uicomponent.CTextField();
        lblBirthDate = new com.see.truetransact.uicomponent.CLabel();
        tdtBirthDate = new com.see.truetransact.uicomponent.CDateField();
        cboFatherTitle = new com.see.truetransact.uicomponent.CComboBox();
        cboMotherTitle = new com.see.truetransact.uicomponent.CComboBox();
        lblCommunicationAddressType = new com.see.truetransact.uicomponent.CLabel();
        cboCommAddressType = new com.see.truetransact.uicomponent.CComboBox();
        lblOfficePhone = new com.see.truetransact.uicomponent.CLabel();
        txtOfficePhone = new com.see.truetransact.uicomponent.CTextField();
        lblReligion = new com.see.truetransact.uicomponent.CLabel();
        lblCaste = new com.see.truetransact.uicomponent.CLabel();
        lblBloodGroup = new com.see.truetransact.uicomponent.CLabel();
        cboBloodGroup = new com.see.truetransact.uicomponent.CComboBox();
        txtReligion = new com.see.truetransact.uicomponent.CLabel();
        txtCaste = new com.see.truetransact.uicomponent.CLabel();
        lblBranch = new com.see.truetransact.uicomponent.CLabel();
        cboBranch = new com.see.truetransact.uicomponent.CComboBox();
        panEmployeeInfo = new com.see.truetransact.uicomponent.CPanel();
        panPicture = new com.see.truetransact.uicomponent.CPanel();
        srpPicture = new com.see.truetransact.uicomponent.CScrollPane();
        lblPicture = new com.see.truetransact.uicomponent.CLabel();
        panButton = new com.see.truetransact.uicomponent.CPanel();
        btnLoad = new com.see.truetransact.uicomponent.CButton();
        btnRemove = new com.see.truetransact.uicomponent.CButton();
        panAddress = new com.see.truetransact.uicomponent.CPanel();
        lblStreet = new com.see.truetransact.uicomponent.CLabel();
        lblArea = new com.see.truetransact.uicomponent.CLabel();
        lblCity = new com.see.truetransact.uicomponent.CLabel();
        lblState = new com.see.truetransact.uicomponent.CLabel();
        lblCountry = new com.see.truetransact.uicomponent.CLabel();
        lblPinCode = new com.see.truetransact.uicomponent.CLabel();
        lblAddressType = new com.see.truetransact.uicomponent.CLabel();
        cboAddressType = new com.see.truetransact.uicomponent.CComboBox();
        txtStreet = new com.see.truetransact.uicomponent.CLabel();
        txtArea = new com.see.truetransact.uicomponent.CLabel();
        txtCity = new com.see.truetransact.uicomponent.CLabel();
        txtState = new com.see.truetransact.uicomponent.CLabel();
        txtCountry = new com.see.truetransact.uicomponent.CLabel();
        txtPinCode = new com.see.truetransact.uicomponent.CLabel();
        panSalaryDetails = new com.see.truetransact.uicomponent.CPanel();
        panSalaryData = new com.see.truetransact.uicomponent.CPanel();
        panDesgDetails = new javax.swing.JPanel();
        lblScaleId = new javax.swing.JLabel();
        lblEffectiveDate = new javax.swing.JLabel();
        lblDesignation = new javax.swing.JLabel();
        lblPresentBasicSalary = new javax.swing.JLabel();
        lblLastIncrementDate = new javax.swing.JLabel();
        lblNextIncrementDate = new javax.swing.JLabel();
        lblIncrementCount = new javax.swing.JLabel();
        cboDesignation = new com.see.truetransact.uicomponent.CComboBox();
        tdtEffectiveDate = new com.see.truetransact.uicomponent.CDateField();
        txtIncrementCount = new com.see.truetransact.uicomponent.CTextField();
        txtPresentBasicSalary = new com.see.truetransact.uicomponent.CTextField();
        tdtLastIncrementDate = new com.see.truetransact.uicomponent.CDateField();
        tdtNextIncrementDate = new com.see.truetransact.uicomponent.CDateField();
        txtScaleId = new com.see.truetransact.uicomponent.CTextField();
        lblVersionNo = new com.see.truetransact.uicomponent.CLabel();
        txtVersionNo = new com.see.truetransact.uicomponent.CTextField();
        panBranch1 = new com.see.truetransact.uicomponent.CPanel();
        panApplicable = new javax.swing.JPanel();
        chkDAApplicable = new com.see.truetransact.uicomponent.CCheckBox();
        chkHRAApplicable = new com.see.truetransact.uicomponent.CCheckBox();
        chkStopPayt = new com.see.truetransact.uicomponent.CCheckBox();
        panAccData = new javax.swing.JPanel();
        lblCustomerName = new javax.swing.JLabel();
        lblNetSalaryAccNo = new javax.swing.JLabel();
        lblNetSalaryProductId = new javax.swing.JLabel();
        lblNetSalaryProductType = new javax.swing.JLabel();
        lblDateOfJoin = new javax.swing.JLabel();
        lblProbationEndDate = new javax.swing.JLabel();
        lblDateOfRetirement = new javax.swing.JLabel();
        tdtDateOfJoin = new com.see.truetransact.uicomponent.CDateField();
        tdtProbationEndDate = new com.see.truetransact.uicomponent.CDateField();
        tdtDateOfRetirement = new com.see.truetransact.uicomponent.CDateField();
        cboNetSalaryProductType = new com.see.truetransact.uicomponent.CComboBox();
        cboNetSalaryProductId = new com.see.truetransact.uicomponent.CComboBox();
        btnNetSalaryAccNo = new com.see.truetransact.uicomponent.CButton();
        txtCustomerName = new com.see.truetransact.uicomponent.CLabel();
        txtNetSalaryAccNo = new com.see.truetransact.uicomponent.CLabel();
        panPensionData = new javax.swing.JPanel();
        lblPensionOpeningBalance = new javax.swing.JLabel();
        lblPensionOpeningBalanceOn = new javax.swing.JLabel();
        lblWfOpeningBalance = new javax.swing.JLabel();
        lblWfNumber = new javax.swing.JLabel();
        lblWfOpeningBalanceOn = new javax.swing.JLabel();
        lblStatusOfEmp = new javax.swing.JLabel();
        lblPensionCodeNo = new javax.swing.JLabel();
        txtPensionCodeNo = new com.see.truetransact.uicomponent.CTextField();
        txtPensionOpeningBalance = new com.see.truetransact.uicomponent.CTextField();
        txtWfNumber = new com.see.truetransact.uicomponent.CTextField();
        txtWfOpeningBalance = new com.see.truetransact.uicomponent.CTextField();
        tdtPensionOpeningBalanceOn = new com.see.truetransact.uicomponent.CDateField();
        tdtWfOpeningBalanceOn = new com.see.truetransact.uicomponent.CDateField();
        cboStatusOfEmp = new com.see.truetransact.uicomponent.CComboBox();
        panOtherDetails = new com.see.truetransact.uicomponent.CPanel();
        sptSkills_Hori = new com.see.truetransact.uicomponent.CSeparator();
        panEmploymentData = new com.see.truetransact.uicomponent.CPanel();
        panGeneraInfo = new com.see.truetransact.uicomponent.CPanel();
        panPersonalNumbers = new com.see.truetransact.uicomponent.CPanel();
        lblPanNo = new com.see.truetransact.uicomponent.CLabel();
        txtPanNo = new com.see.truetransact.uicomponent.CTextField();
        lblSsnNo = new com.see.truetransact.uicomponent.CLabel();
        txtSsnNo = new com.see.truetransact.uicomponent.CTextField();
        lblPassPortNo = new com.see.truetransact.uicomponent.CLabel();
        txtPassPortNo = new com.see.truetransact.uicomponent.CTextField();
        panJobDetails = new com.see.truetransact.uicomponent.CPanel();
        lblEmployeeType = new com.see.truetransact.uicomponent.CLabel();
        cboEmployeeType = new com.see.truetransact.uicomponent.CComboBox();
        lblJobTitle = new com.see.truetransact.uicomponent.CLabel();
        cboJobTitle = new com.see.truetransact.uicomponent.CComboBox();
        lblDepartment = new com.see.truetransact.uicomponent.CLabel();
        cboDepartment = new com.see.truetransact.uicomponent.CComboBox();
        lblManager = new com.see.truetransact.uicomponent.CLabel();
        cboManager = new com.see.truetransact.uicomponent.CComboBox();
        panPhysicalDetails = new com.see.truetransact.uicomponent.CPanel();
        lblMajorHealthProblem = new javax.swing.JLabel();
        jScrollPane2 = new javax.swing.JScrollPane();
        txaIdentificationMark1 = new com.see.truetransact.uicomponent.CTextArea();
        jScrollPane3 = new javax.swing.JScrollPane();
        txaPhysicallyHandicapped = new com.see.truetransact.uicomponent.CTextArea();
        jScrollPane7 = new javax.swing.JScrollPane();
        txaMajorHealthProblem = new com.see.truetransact.uicomponent.CTextArea();
        lblIdentificationMark1 = new javax.swing.JLabel();
        lblIdentificationMark2 = new javax.swing.JLabel();
        lblPhysicallyHandicapped = new javax.swing.JLabel();
        jScrollPane8 = new javax.swing.JScrollPane();
        txaIdentificationMark2 = new com.see.truetransact.uicomponent.CTextArea();
        panPerformance = new com.see.truetransact.uicomponent.CPanel();
        srpResponsibility = new com.see.truetransact.uicomponent.CScrollPane();
        txaResponsibility = new com.see.truetransact.uicomponent.CTextArea();
        srpPerformance = new com.see.truetransact.uicomponent.CScrollPane();
        txaPerformance = new com.see.truetransact.uicomponent.CTextArea();
        srpComments = new com.see.truetransact.uicomponent.CScrollPane();
        txaComments = new com.see.truetransact.uicomponent.CTextArea();
        srpEducation = new com.see.truetransact.uicomponent.CScrollPane();
        txaEducation = new com.see.truetransact.uicomponent.CTextArea();
        srpResponsibility3 = new com.see.truetransact.uicomponent.CScrollPane();
        txaSkills = new com.see.truetransact.uicomponent.CTextArea();
        srpExperience = new com.see.truetransact.uicomponent.CScrollPane();
        txaExperience = new com.see.truetransact.uicomponent.CTextArea();
        lblSkills = new com.see.truetransact.uicomponent.CLabel();
        lblEducation = new com.see.truetransact.uicomponent.CLabel();
        lblExperience = new com.see.truetransact.uicomponent.CLabel();
        lblResponsibility = new com.see.truetransact.uicomponent.CLabel();
        lblPerformance = new com.see.truetransact.uicomponent.CLabel();
        lblComments = new com.see.truetransact.uicomponent.CLabel();
        sptSkills_Vert1 = new com.see.truetransact.uicomponent.CSeparator();
        sptSkills_Vert2 = new com.see.truetransact.uicomponent.CSeparator();
        sptSkills_Vert3 = new com.see.truetransact.uicomponent.CSeparator();
        panStatus = new com.see.truetransact.uicomponent.CPanel();
        lblSpace1 = new com.see.truetransact.uicomponent.CLabel();
        lblStatus1 = new com.see.truetransact.uicomponent.CLabel();
        lblMsg = new com.see.truetransact.uicomponent.CLabel();
        tbrLoantProduct = new javax.swing.JToolBar();
        btnView = new com.see.truetransact.uicomponent.CButton();
        lblSpace5 = new com.see.truetransact.uicomponent.CLabel();
        btnNew = new com.see.truetransact.uicomponent.CButton();
        lblSpace17 = new com.see.truetransact.uicomponent.CLabel();
        btnEdit = new com.see.truetransact.uicomponent.CButton();
        lblSpace18 = new com.see.truetransact.uicomponent.CLabel();
        btnDelete = new com.see.truetransact.uicomponent.CButton();
        lblSpace2 = new com.see.truetransact.uicomponent.CLabel();
        btnSave = new com.see.truetransact.uicomponent.CButton();
        lblSpace19 = new com.see.truetransact.uicomponent.CLabel();
        btnCancel = new com.see.truetransact.uicomponent.CButton();
        lblSpace3 = new com.see.truetransact.uicomponent.CLabel();
        btnAuthorize = new com.see.truetransact.uicomponent.CButton();
        lblSpace20 = new com.see.truetransact.uicomponent.CLabel();
        btnException = new com.see.truetransact.uicomponent.CButton();
        lblSpace21 = new com.see.truetransact.uicomponent.CLabel();
        btnReject = new com.see.truetransact.uicomponent.CButton();
        lblSpace4 = new com.see.truetransact.uicomponent.CLabel();
        btnPrint = new com.see.truetransact.uicomponent.CButton();
        lblSpace22 = new com.see.truetransact.uicomponent.CLabel();
        btnClose = new com.see.truetransact.uicomponent.CButton();
        mbrLoanProduct = new com.see.truetransact.uicomponent.CMenuBar();
        mnuProcess = new javax.swing.JMenu();
        mitNew = new javax.swing.JMenuItem();
        mitEdit = new javax.swing.JMenuItem();
        mitDelete = new javax.swing.JMenuItem();
        sptProcess = new javax.swing.JSeparator();
        mitSave = new javax.swing.JMenuItem();
        mitCancel = new javax.swing.JMenuItem();
        sptCancel = new javax.swing.JSeparator();
        mitPrint = new javax.swing.JMenuItem();
        sptPrint = new javax.swing.JSeparator();
        mitClose = new javax.swing.JMenuItem();

        setClosable(true);
        setIconifiable(true);
        setMaximizable(true);
        setResizable(true);
        setMinimumSize(new java.awt.Dimension(1000, 670));
        setPreferredSize(new java.awt.Dimension(850, 670));

        panEmployee.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        panEmployee.setMinimumSize(new java.awt.Dimension(500, 650));
        panEmployee.setPreferredSize(new java.awt.Dimension(500, 650));
        panEmployee.setLayout(new java.awt.GridBagLayout());

        lblSpaces.setMinimumSize(new java.awt.Dimension(3, 11));
        lblSpaces.setPreferredSize(new java.awt.Dimension(3, 11));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        panEmployee.add(lblSpaces, gridBagConstraints);

        tabEmployee.setMaximumSize(new java.awt.Dimension(2147483647, 2147483647));
        tabEmployee.setMinimumSize(new java.awt.Dimension(780, 620));
        tabEmployee.setPreferredSize(new java.awt.Dimension(780, 620));

        panEmployeeDetails.setMinimumSize(new java.awt.Dimension(600, 450));
        panEmployeeDetails.setPreferredSize(new java.awt.Dimension(600, 450));
        panEmployeeDetails.setLayout(new java.awt.GridBagLayout());

        panEmployeeData.setBorder(javax.swing.BorderFactory.createTitledBorder(""));
        panEmployeeData.setMinimumSize(new java.awt.Dimension(460, 220));
        panEmployeeData.setPreferredSize(new java.awt.Dimension(1000, 1000));
        panEmployeeData.setLayout(new java.awt.GridBagLayout());

        lblEmployeeId.setText("Employee ID");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHEAST;
        gridBagConstraints.insets = new java.awt.Insets(2, 0, 2, 0);
        panEmployeeData.add(lblEmployeeId, gridBagConstraints);

        txtEmployeeId.setMinimumSize(new java.awt.Dimension(100, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 6, 2, 0);
        panEmployeeData.add(txtEmployeeId, gridBagConstraints);

        lblTitle.setText("Title");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHEAST;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 2, 0);
        panEmployeeData.add(lblTitle, gridBagConstraints);

        lblFirstName.setText("First Name");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 5;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHEAST;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 2, 0);
        panEmployeeData.add(lblFirstName, gridBagConstraints);

        lblLastName.setText("Last Name");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 7;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHEAST;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 2, 0);
        panEmployeeData.add(lblLastName, gridBagConstraints);

        lblGender.setText("Gender");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 8;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHEAST;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 2, 0);
        panEmployeeData.add(lblGender, gridBagConstraints);

        panGender.setMinimumSize(new java.awt.Dimension(110, 21));
        panGender.setPreferredSize(new java.awt.Dimension(110, 21));
        panGender.setLayout(new java.awt.GridBagLayout());

        rdoGender.add(rdoGender_Male);
        rdoGender_Male.setText("Male");
        rdoGender_Male.setMaximumSize(new java.awt.Dimension(55, 21));
        rdoGender_Male.setMinimumSize(new java.awt.Dimension(55, 21));
        rdoGender_Male.setPreferredSize(new java.awt.Dimension(47, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 23, 0, 0);
        panGender.add(rdoGender_Male, gridBagConstraints);

        rdoGender.add(rdoGender_Female);
        rdoGender_Female.setText("Female");
        rdoGender_Female.setMaximumSize(new java.awt.Dimension(59, 21));
        rdoGender_Female.setMinimumSize(new java.awt.Dimension(85, 21));
        rdoGender_Female.setPreferredSize(new java.awt.Dimension(85, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(1, 0, 0, 2);
        panGender.add(rdoGender_Female, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 8;
        gridBagConstraints.ipadx = 15;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 6, 2, 0);
        panEmployeeData.add(panGender, gridBagConstraints);

        lblMartialStatus.setText("Marital Status");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 12;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHEAST;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 2, 0);
        panEmployeeData.add(lblMartialStatus, gridBagConstraints);

        lblOfficialEmail.setText("Official Email");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 15;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHEAST;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 2, 0);
        panEmployeeData.add(lblOfficialEmail, gridBagConstraints);

        lblCustomerId.setText("Customer ID");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHEAST;
        gridBagConstraints.insets = new java.awt.Insets(6, 0, 2, 0);
        panEmployeeData.add(lblCustomerId, gridBagConstraints);

        panCustomerId.setMinimumSize(new java.awt.Dimension(133, 29));
        panCustomerId.setPreferredSize(new java.awt.Dimension(133, 29));
        panCustomerId.setLayout(new java.awt.GridBagLayout());

        txtCustomerId.setMinimumSize(new java.awt.Dimension(100, 21));
        txtCustomerId.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                txtCustomerIdFocusLost(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panCustomerId.add(txtCustomerId, gridBagConstraints);

        btnCustomerId.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_SELECT.gif"))); // NOI18N
        btnCustomerId.setToolTipText("Save");
        btnCustomerId.setMaximumSize(new java.awt.Dimension(25, 25));
        btnCustomerId.setPreferredSize(new java.awt.Dimension(21, 21));
        btnCustomerId.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCustomerIdActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        panCustomerId.add(btnCustomerId, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 2, 0);
        panEmployeeData.add(panCustomerId, gridBagConstraints);

        lblMiddleName.setText("Middle Name");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 6;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHEAST;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 2, 0);
        panEmployeeData.add(lblMiddleName, gridBagConstraints);

        txtMiddleName.setText("cLabel2");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 6;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 6, 2, 0);
        panEmployeeData.add(txtMiddleName, gridBagConstraints);

        txtFirstName.setText("cLabel1");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 5;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 6, 2, 0);
        panEmployeeData.add(txtFirstName, gridBagConstraints);

        txtTitle.setText("cLabel1");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 6, 2, 0);
        panEmployeeData.add(txtTitle, gridBagConstraints);

        txtLastName.setText("cLabel1");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 7;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 6, 2, 0);
        panEmployeeData.add(txtLastName, gridBagConstraints);

        txtMartialStatus.setText("cLabel1");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 12;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 6, 2, 0);
        panEmployeeData.add(txtMartialStatus, gridBagConstraints);

        txtOfficialEmail.setText("cLabel1");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 15;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 6, 2, 0);
        panEmployeeData.add(txtOfficialEmail, gridBagConstraints);

        lblSortOrder.setFont(new java.awt.Font("Microsoft Sans Serif", 0, 13)); // NOI18N
        lblSortOrder.setText("Sort Order");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHEAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 0, 2, 0);
        panEmployeeData.add(lblSortOrder, gridBagConstraints);

        lblFatherName.setFont(new java.awt.Font("Microsoft Sans Serif", 0, 13)); // NOI18N
        lblFatherName.setText("Father's Name");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 10;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHEAST;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 2, 0);
        panEmployeeData.add(lblFatherName, gridBagConstraints);

        lblMotherName.setFont(new java.awt.Font("Microsoft Sans Serif", 0, 13)); // NOI18N
        lblMotherName.setText("Mother's Name");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 11;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHEAST;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 2, 0);
        panEmployeeData.add(lblMotherName, gridBagConstraints);

        lblSpouseName.setFont(new java.awt.Font("Microsoft Sans Serif", 0, 13)); // NOI18N
        lblSpouseName.setText("Spouse Name");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 13;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHEAST;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 2, 0);
        panEmployeeData.add(lblSpouseName, gridBagConstraints);

        lblSpouseRelation.setFont(new java.awt.Font("Microsoft Sans Serif", 0, 13)); // NOI18N
        lblSpouseRelation.setText("Spouse Relation");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 14;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHEAST;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 2, 0);
        panEmployeeData.add(lblSpouseRelation, gridBagConstraints);

        lblPlaceofBirth.setFont(new java.awt.Font("Microsoft Sans Serif", 0, 13)); // NOI18N
        lblPlaceofBirth.setText("Place of birth");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 17;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHEAST;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 2, 0);
        panEmployeeData.add(lblPlaceofBirth, gridBagConstraints);

        txtSortOrder.setMaximumSize(new java.awt.Dimension(100, 21));
        txtSortOrder.setMinimumSize(new java.awt.Dimension(100, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 6, 2, 0);
        panEmployeeData.add(txtSortOrder, gridBagConstraints);

        txtFatherName.setMaximumSize(new java.awt.Dimension(100, 21));
        txtFatherName.setMinimumSize(new java.awt.Dimension(100, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 10;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 6, 2, 0);
        panEmployeeData.add(txtFatherName, gridBagConstraints);

        txtMotherName.setMaximumSize(new java.awt.Dimension(100, 21));
        txtMotherName.setMinimumSize(new java.awt.Dimension(100, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 11;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 6, 2, 0);
        panEmployeeData.add(txtMotherName, gridBagConstraints);

        txtSpouseName.setMaximumSize(new java.awt.Dimension(100, 21));
        txtSpouseName.setMinimumSize(new java.awt.Dimension(100, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 13;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 6, 2, 0);
        panEmployeeData.add(txtSpouseName, gridBagConstraints);

        txtSpouseRelation.setMaximumSize(new java.awt.Dimension(100, 21));
        txtSpouseRelation.setMinimumSize(new java.awt.Dimension(100, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 14;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 6, 2, 0);
        panEmployeeData.add(txtSpouseRelation, gridBagConstraints);

        txtPlaceofBirth.setMaximumSize(new java.awt.Dimension(100, 21));
        txtPlaceofBirth.setMinimumSize(new java.awt.Dimension(100, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 17;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 6, 2, 0);
        panEmployeeData.add(txtPlaceofBirth, gridBagConstraints);

        lblBirthDate.setText("Birth Date");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 9;
        gridBagConstraints.ipady = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHEAST;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 2, 0);
        panEmployeeData.add(lblBirthDate, gridBagConstraints);

        tdtBirthDate.setMinimumSize(new java.awt.Dimension(100, 21));
        tdtBirthDate.setPreferredSize(new java.awt.Dimension(100, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 9;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 6, 2, 0);
        panEmployeeData.add(tdtBirthDate, gridBagConstraints);

        cboFatherTitle.setMinimumSize(new java.awt.Dimension(50, 20));
        cboFatherTitle.setPopupWidth(80);
        cboFatherTitle.setPreferredSize(new java.awt.Dimension(50, 20));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 10;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 2, 0);
        panEmployeeData.add(cboFatherTitle, gridBagConstraints);

        cboMotherTitle.setMinimumSize(new java.awt.Dimension(50, 20));
        cboMotherTitle.setPopupWidth(80);
        cboMotherTitle.setPreferredSize(new java.awt.Dimension(50, 20));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 11;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 2, 0);
        panEmployeeData.add(cboMotherTitle, gridBagConstraints);

        lblCommunicationAddressType.setText("Communication Address Type");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 21;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHEAST;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 2, 0);
        panEmployeeData.add(lblCommunicationAddressType, gridBagConstraints);

        cboCommAddressType.setMaximumSize(new java.awt.Dimension(100, 21));
        cboCommAddressType.setMinimumSize(new java.awt.Dimension(100, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 21;
        gridBagConstraints.ipadx = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 6, 2, 0);
        panEmployeeData.add(cboCommAddressType, gridBagConstraints);

        lblOfficePhone.setText("Contact No");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 16;
        gridBagConstraints.ipadx = 5;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHEAST;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 2, 0);
        panEmployeeData.add(lblOfficePhone, gridBagConstraints);

        txtOfficePhone.setMaximumSize(new java.awt.Dimension(100, 21));
        txtOfficePhone.setMinimumSize(new java.awt.Dimension(100, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 16;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 6, 2, 0);
        panEmployeeData.add(txtOfficePhone, gridBagConstraints);

        lblReligion.setText("Religion");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 18;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHEAST;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 2, 0);
        panEmployeeData.add(lblReligion, gridBagConstraints);

        lblCaste.setText("Caste");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 19;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHEAST;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 2, 0);
        panEmployeeData.add(lblCaste, gridBagConstraints);

        lblBloodGroup.setText("BloodGroup");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 20;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHEAST;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 2, 0);
        panEmployeeData.add(lblBloodGroup, gridBagConstraints);

        cboBloodGroup.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "    ", "AB+", "O+", "A+", "B+", "AB-", "O-", "A-", "B-" }));
        cboBloodGroup.setMinimumSize(new java.awt.Dimension(100, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 20;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 6, 2, 0);
        panEmployeeData.add(cboBloodGroup, gridBagConstraints);

        txtReligion.setText("cLabel1");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 18;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 6, 2, 0);
        panEmployeeData.add(txtReligion, gridBagConstraints);

        txtCaste.setText("cLabel2");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 19;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 6, 2, 0);
        panEmployeeData.add(txtCaste, gridBagConstraints);

        lblBranch.setText("Branch");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHEAST;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 2, 0);
        panEmployeeData.add(lblBranch, gridBagConstraints);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 6, 20, 0);
        panEmployeeData.add(cboBranch, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTH;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(10, 4, 4, 4);
        panEmployeeDetails.add(panEmployeeData, gridBagConstraints);

        panEmployeeInfo.setMinimumSize(new java.awt.Dimension(350, 550));
        panEmployeeInfo.setPreferredSize(new java.awt.Dimension(350, 550));
        panEmployeeInfo.setLayout(new java.awt.GridBagLayout());

        panPicture.setBorder(javax.swing.BorderFactory.createTitledBorder("Picture"));
        panPicture.setMinimumSize(new java.awt.Dimension(220, 260));
        panPicture.setPreferredSize(new java.awt.Dimension(220, 260));
        panPicture.setLayout(new java.awt.GridBagLayout());

        srpPicture.setMinimumSize(new java.awt.Dimension(175, 190));
        srpPicture.setPreferredSize(new java.awt.Dimension(175, 190));
        srpPicture.setViewportView(lblPicture);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panPicture.add(srpPicture, gridBagConstraints);

        panButton.setLayout(new java.awt.GridBagLayout());

        btnLoad.setText("Load");
        btnLoad.setMinimumSize(new java.awt.Dimension(73, 25));
        btnLoad.setPreferredSize(new java.awt.Dimension(73, 25));
        btnLoad.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnLoadActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panButton.add(btnLoad, gridBagConstraints);

        btnRemove.setText("Remove");
        btnRemove.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnRemoveActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panButton.add(btnRemove, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        panPicture.add(panButton, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.insets = new java.awt.Insets(6, 126, 0, 0);
        panEmployeeInfo.add(panPicture, gridBagConstraints);

        panAddress.setBorder(javax.swing.BorderFactory.createTitledBorder("Address"));
        panAddress.setMinimumSize(new java.awt.Dimension(420, 212));
        panAddress.setPreferredSize(new java.awt.Dimension(420, 212));
        panAddress.setLayout(new java.awt.GridBagLayout());

        lblStreet.setText("Street");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHEAST;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 2, 0);
        panAddress.add(lblStreet, gridBagConstraints);

        lblArea.setText("Area");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHEAST;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 2, 0);
        panAddress.add(lblArea, gridBagConstraints);

        lblCity.setText("City");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHEAST;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 2, 0);
        panAddress.add(lblCity, gridBagConstraints);

        lblState.setText("State");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHEAST;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 2, 0);
        panAddress.add(lblState, gridBagConstraints);

        lblCountry.setText("Country");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 5;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHEAST;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 2, 0);
        panAddress.add(lblCountry, gridBagConstraints);

        lblPinCode.setText("Pin Code");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 6;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHEAST;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 2, 0);
        panAddress.add(lblPinCode, gridBagConstraints);

        lblAddressType.setText("Address Type");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHEAST;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 2, 0);
        panAddress.add(lblAddressType, gridBagConstraints);

        cboAddressType.setMinimumSize(new java.awt.Dimension(100, 21));
        cboAddressType.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cboAddressTypeActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 6, 2, 0);
        panAddress.add(cboAddressType, gridBagConstraints);

        txtStreet.setText("cLabel1");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 6, 2, 0);
        panAddress.add(txtStreet, gridBagConstraints);

        txtArea.setText("cLabel1");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 6, 2, 0);
        panAddress.add(txtArea, gridBagConstraints);

        txtCity.setText("cLabel1");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 6, 2, 0);
        panAddress.add(txtCity, gridBagConstraints);

        txtState.setText("cLabel1");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 6, 2, 0);
        panAddress.add(txtState, gridBagConstraints);

        txtCountry.setText("cLabel1");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 5;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 6, 2, 0);
        panAddress.add(txtCountry, gridBagConstraints);

        txtPinCode.setText("cLabel1");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 6;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 6, 2, 0);
        panAddress.add(txtPinCode, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.gridwidth = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(10, 70, 10, 110);
        panEmployeeInfo.add(panAddress, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTH;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panEmployeeDetails.add(panEmployeeInfo, gridBagConstraints);

        tabEmployee.addTab("Employee Details", panEmployeeDetails);

        panSalaryDetails.setMinimumSize(new java.awt.Dimension(600, 600));
        panSalaryDetails.setPreferredSize(new java.awt.Dimension(600, 600));
        panSalaryDetails.setLayout(new java.awt.GridBagLayout());

        panSalaryData.setMaximumSize(new java.awt.Dimension(2147483647, 2147483647));
        panSalaryData.setMinimumSize(new java.awt.Dimension(750, 600));
        panSalaryData.setPreferredSize(new java.awt.Dimension(750, 600));
        panSalaryData.setLayout(new java.awt.GridBagLayout());

        panDesgDetails.setBorder(javax.swing.BorderFactory.createTitledBorder(""));
        panDesgDetails.setMaximumSize(new java.awt.Dimension(720, 250));
        panDesgDetails.setMinimumSize(new java.awt.Dimension(500, 250));
        panDesgDetails.setPreferredSize(new java.awt.Dimension(500, 250));
        panDesgDetails.setLayout(new java.awt.GridBagLayout());

        lblScaleId.setFont(new java.awt.Font("Microsoft Sans Serif", 0, 13)); // NOI18N
        lblScaleId.setText("Scale ID");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHEAST;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 2, 0);
        panDesgDetails.add(lblScaleId, gridBagConstraints);

        lblEffectiveDate.setFont(new java.awt.Font("Microsoft Sans Serif", 0, 13)); // NOI18N
        lblEffectiveDate.setText("Effective Date");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHEAST;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 2, 0);
        panDesgDetails.add(lblEffectiveDate, gridBagConstraints);

        lblDesignation.setFont(new java.awt.Font("Microsoft Sans Serif", 0, 13)); // NOI18N
        lblDesignation.setText("Designation");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHEAST;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 2, 0);
        panDesgDetails.add(lblDesignation, gridBagConstraints);

        lblPresentBasicSalary.setFont(new java.awt.Font("Microsoft Sans Serif", 0, 13)); // NOI18N
        lblPresentBasicSalary.setText("Present Basic Salary");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 5;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHEAST;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 2, 0);
        panDesgDetails.add(lblPresentBasicSalary, gridBagConstraints);

        lblLastIncrementDate.setFont(new java.awt.Font("Microsoft Sans Serif", 0, 13)); // NOI18N
        lblLastIncrementDate.setText("Last Increment Date");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 6;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHEAST;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 2, 0);
        panDesgDetails.add(lblLastIncrementDate, gridBagConstraints);

        lblNextIncrementDate.setFont(new java.awt.Font("Microsoft Sans Serif", 0, 13)); // NOI18N
        lblNextIncrementDate.setText("Next Increment Date");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 7;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHEAST;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 2, 0);
        panDesgDetails.add(lblNextIncrementDate, gridBagConstraints);

        lblIncrementCount.setFont(new java.awt.Font("Microsoft Sans Serif", 0, 13)); // NOI18N
        lblIncrementCount.setText("Increment Count");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHEAST;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 2, 0);
        panDesgDetails.add(lblIncrementCount, gridBagConstraints);

        cboDesignation.setFocusable(false);
        cboDesignation.setMaximumSize(new java.awt.Dimension(100, 21));
        cboDesignation.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                cboDesignationMousePressed(evt);
            }
        });
        cboDesignation.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cboDesignationActionPerformed(evt);
            }
        });
        cboDesignation.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                cboDesignationFocusLost(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.ipadx = 25;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 6, 2, 0);
        panDesgDetails.add(cboDesignation, gridBagConstraints);

        tdtEffectiveDate.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                tdtEffectiveDateFocusLost(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 6, 2, 0);
        panDesgDetails.add(tdtEffectiveDate, gridBagConstraints);

        txtIncrementCount.setMinimumSize(new java.awt.Dimension(100, 21));
        txtIncrementCount.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                txtIncrementCountFocusLost(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.ipadx = 25;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 6, 2, 0);
        panDesgDetails.add(txtIncrementCount, gridBagConstraints);

        txtPresentBasicSalary.setMinimumSize(new java.awt.Dimension(100, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 5;
        gridBagConstraints.ipadx = 25;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 6, 2, 0);
        panDesgDetails.add(txtPresentBasicSalary, gridBagConstraints);

        tdtLastIncrementDate.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                tdtLastIncrementDateFocusLost(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 6;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 6, 2, 0);
        panDesgDetails.add(tdtLastIncrementDate, gridBagConstraints);

        tdtNextIncrementDate.setMinimumSize(new java.awt.Dimension(100, 21));
        tdtNextIncrementDate.setPreferredSize(new java.awt.Dimension(100, 21));
        tdtNextIncrementDate.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                tdtNextIncrementDateFocusLost(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 7;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 6, 2, 0);
        panDesgDetails.add(tdtNextIncrementDate, gridBagConstraints);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.ipadx = 25;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 6, 2, 0);
        panDesgDetails.add(txtScaleId, gridBagConstraints);

        lblVersionNo.setText("Version No");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHEAST;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 2, 0);
        panDesgDetails.add(lblVersionNo, gridBagConstraints);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.ipadx = 25;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 6, 2, 0);
        panDesgDetails.add(txtVersionNo, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(8, 7, 0, 0);
        panSalaryData.add(panDesgDetails, gridBagConstraints);

        panBranch1.setLayout(new java.awt.GridBagLayout());
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        panSalaryData.add(panBranch1, gridBagConstraints);

        panApplicable.setBorder(javax.swing.BorderFactory.createTitledBorder(""));
        panApplicable.setMaximumSize(new java.awt.Dimension(500, 800));
        panApplicable.setMinimumSize(new java.awt.Dimension(257, 800));
        panApplicable.setPreferredSize(new java.awt.Dimension(600, 800));
        panApplicable.setLayout(new java.awt.GridBagLayout());

        chkDAApplicable.setText("DA Applicable");
        chkDAApplicable.setFont(new java.awt.Font("Microsoft Sans Serif", 0, 13)); // NOI18N
        chkDAApplicable.setMaximumSize(new java.awt.Dimension(150, 23));
        chkDAApplicable.setMinimumSize(new java.awt.Dimension(120, 23));
        chkDAApplicable.setPreferredSize(new java.awt.Dimension(150, 23));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.insets = new java.awt.Insets(0, 30, 0, 0);
        panApplicable.add(chkDAApplicable, gridBagConstraints);

        chkHRAApplicable.setText("HRA Applicable");
        chkHRAApplicable.setFont(new java.awt.Font("Microsoft Sans Serif", 0, 13)); // NOI18N
        chkHRAApplicable.setMaximumSize(new java.awt.Dimension(130, 23));
        chkHRAApplicable.setMinimumSize(new java.awt.Dimension(130, 23));
        chkHRAApplicable.setPreferredSize(new java.awt.Dimension(130, 23));
        panApplicable.add(chkHRAApplicable, new java.awt.GridBagConstraints());

        chkStopPayt.setText("Stop Payt");
        chkStopPayt.setFont(new java.awt.Font("Microsoft Sans Serif", 0, 13)); // NOI18N
        chkStopPayt.setMaximumSize(new java.awt.Dimension(150, 23));
        chkStopPayt.setMinimumSize(new java.awt.Dimension(100, 23));
        chkStopPayt.setPreferredSize(new java.awt.Dimension(150, 23));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 0;
        panApplicable.add(chkStopPayt, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.gridwidth = 4;
        gridBagConstraints.ipadx = 253;
        gridBagConstraints.ipady = -770;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(10, 0, 10, 0);
        panSalaryData.add(panApplicable, gridBagConstraints);

        panAccData.setBorder(javax.swing.BorderFactory.createTitledBorder(""));
        panAccData.setMaximumSize(new java.awt.Dimension(600, 250));
        panAccData.setMinimumSize(new java.awt.Dimension(500, 250));
        panAccData.setPreferredSize(new java.awt.Dimension(500, 250));
        panAccData.setLayout(new java.awt.GridBagLayout());

        lblCustomerName.setFont(new java.awt.Font("Microsoft Sans Serif", 0, 13)); // NOI18N
        lblCustomerName.setText("Customer Name");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 6;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHEAST;
        gridBagConstraints.insets = new java.awt.Insets(0, 2, 2, 0);
        panAccData.add(lblCustomerName, gridBagConstraints);

        lblNetSalaryAccNo.setFont(new java.awt.Font("Microsoft Sans Serif", 0, 13)); // NOI18N
        lblNetSalaryAccNo.setText("Net Salary Acc No");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 5;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHEAST;
        gridBagConstraints.insets = new java.awt.Insets(0, 2, 2, 0);
        panAccData.add(lblNetSalaryAccNo, gridBagConstraints);

        lblNetSalaryProductId.setFont(new java.awt.Font("Microsoft Sans Serif", 0, 13)); // NOI18N
        lblNetSalaryProductId.setText("Net Salary Product ID");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHEAST;
        gridBagConstraints.insets = new java.awt.Insets(0, 2, 2, 0);
        panAccData.add(lblNetSalaryProductId, gridBagConstraints);

        lblNetSalaryProductType.setFont(new java.awt.Font("Microsoft Sans Serif", 0, 13)); // NOI18N
        lblNetSalaryProductType.setText("Net Salary Product type");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHEAST;
        gridBagConstraints.insets = new java.awt.Insets(0, 2, 2, 0);
        panAccData.add(lblNetSalaryProductType, gridBagConstraints);

        lblDateOfJoin.setFont(new java.awt.Font("Microsoft Sans Serif", 0, 13)); // NOI18N
        lblDateOfJoin.setText("Date of Join");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHEAST;
        gridBagConstraints.insets = new java.awt.Insets(0, 2, 2, 0);
        panAccData.add(lblDateOfJoin, gridBagConstraints);

        lblProbationEndDate.setFont(new java.awt.Font("Microsoft Sans Serif", 0, 13)); // NOI18N
        lblProbationEndDate.setText("Probation End Date");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHEAST;
        gridBagConstraints.insets = new java.awt.Insets(0, 2, 2, 0);
        panAccData.add(lblProbationEndDate, gridBagConstraints);

        lblDateOfRetirement.setFont(new java.awt.Font("Microsoft Sans Serif", 0, 13)); // NOI18N
        lblDateOfRetirement.setText("Date of Retirement");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHEAST;
        gridBagConstraints.insets = new java.awt.Insets(0, 2, 2, 0);
        panAccData.add(lblDateOfRetirement, gridBagConstraints);

        tdtDateOfJoin.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                tdtDateOfJoinFocusLost(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 6, 2, 0);
        panAccData.add(tdtDateOfJoin, gridBagConstraints);

        tdtProbationEndDate.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                tdtProbationEndDateFocusLost(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 6, 2, 0);
        panAccData.add(tdtProbationEndDate, gridBagConstraints);

        tdtDateOfRetirement.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                tdtDateOfRetirementFocusLost(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 6, 2, 0);
        panAccData.add(tdtDateOfRetirement, gridBagConstraints);

        cboNetSalaryProductType.setMaximumSize(new java.awt.Dimension(100, 20));
        cboNetSalaryProductType.setPopupWidth(180);
        cboNetSalaryProductType.setPreferredSize(new java.awt.Dimension(100, 20));
        cboNetSalaryProductType.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cboNetSalaryProductTypeActionPerformed(evt);
            }
        });
        cboNetSalaryProductType.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                cboNetSalaryProductTypeFocusLost(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.ipadx = 25;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 6, 2, 0);
        panAccData.add(cboNetSalaryProductType, gridBagConstraints);

        cboNetSalaryProductId.setPopupWidth(180);
        cboNetSalaryProductId.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                cboNetSalaryProductIdFocusLost(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.ipadx = 25;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 6, 2, 0);
        panAccData.add(cboNetSalaryProductId, gridBagConstraints);

        btnNetSalaryAccNo.setText("cButton1");
        btnNetSalaryAccNo.setMinimumSize(new java.awt.Dimension(21, 21));
        btnNetSalaryAccNo.setPreferredSize(new java.awt.Dimension(21, 21));
        btnNetSalaryAccNo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnNetSalaryAccNoActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 5;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 2, 0);
        panAccData.add(btnNetSalaryAccNo, gridBagConstraints);

        txtCustomerName.setText("cLabel1");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 6;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 6, 2, 0);
        panAccData.add(txtCustomerName, gridBagConstraints);

        txtNetSalaryAccNo.setText("cLabel1");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 5;
        gridBagConstraints.ipadx = 25;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 6, 2, 0);
        panAccData.add(txtNetSalaryAccNo, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.gridheight = 4;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 10, 8, 0);
        panSalaryData.add(panAccData, gridBagConstraints);

        panPensionData.setBorder(javax.swing.BorderFactory.createTitledBorder(""));
        panPensionData.setMinimumSize(new java.awt.Dimension(400, 375));
        panPensionData.setPreferredSize(new java.awt.Dimension(400, 375));
        panPensionData.setLayout(new java.awt.GridBagLayout());

        lblPensionOpeningBalance.setFont(new java.awt.Font("Microsoft Sans Serif", 0, 13)); // NOI18N
        lblPensionOpeningBalance.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        lblPensionOpeningBalance.setText("Pension Opening balance");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHEAST;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 2, 0);
        panPensionData.add(lblPensionOpeningBalance, gridBagConstraints);

        lblPensionOpeningBalanceOn.setFont(new java.awt.Font("Microsoft Sans Serif", 0, 13)); // NOI18N
        lblPensionOpeningBalanceOn.setText("Pension Opening balance on");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHEAST;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 2, 0);
        panPensionData.add(lblPensionOpeningBalanceOn, gridBagConstraints);

        lblWfOpeningBalance.setFont(new java.awt.Font("Microsoft Sans Serif", 0, 13)); // NOI18N
        lblWfOpeningBalance.setText("WF Opening Balance");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHEAST;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 2, 0);
        panPensionData.add(lblWfOpeningBalance, gridBagConstraints);

        lblWfNumber.setFont(new java.awt.Font("Microsoft Sans Serif", 0, 13)); // NOI18N
        lblWfNumber.setText("WF Number");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHEAST;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 2, 0);
        panPensionData.add(lblWfNumber, gridBagConstraints);

        lblWfOpeningBalanceOn.setFont(new java.awt.Font("Microsoft Sans Serif", 0, 13)); // NOI18N
        lblWfOpeningBalanceOn.setText("WF Opening balance on");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 5;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHEAST;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 2, 0);
        panPensionData.add(lblWfOpeningBalanceOn, gridBagConstraints);

        lblStatusOfEmp.setFont(new java.awt.Font("Microsoft Sans Serif", 0, 13)); // NOI18N
        lblStatusOfEmp.setText("Status");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 6;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHEAST;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 2, 0);
        panPensionData.add(lblStatusOfEmp, gridBagConstraints);

        lblPensionCodeNo.setFont(new java.awt.Font("Microsoft Sans Serif", 0, 13)); // NOI18N
        lblPensionCodeNo.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        lblPensionCodeNo.setText("Pension code No");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHEAST;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 2, 0);
        panPensionData.add(lblPensionCodeNo, gridBagConstraints);

        txtPensionCodeNo.setMinimumSize(new java.awt.Dimension(100, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.ipadx = 25;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 6, 2, 30);
        panPensionData.add(txtPensionCodeNo, gridBagConstraints);

        txtPensionOpeningBalance.setMinimumSize(new java.awt.Dimension(100, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.ipadx = 25;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 6, 2, 30);
        panPensionData.add(txtPensionOpeningBalance, gridBagConstraints);

        txtWfNumber.setMinimumSize(new java.awt.Dimension(100, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.ipadx = 25;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 6, 2, 30);
        panPensionData.add(txtWfNumber, gridBagConstraints);

        txtWfOpeningBalance.setMinimumSize(new java.awt.Dimension(100, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.ipadx = 25;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 6, 2, 30);
        panPensionData.add(txtWfOpeningBalance, gridBagConstraints);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 6, 2, 30);
        panPensionData.add(tdtPensionOpeningBalanceOn, gridBagConstraints);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 5;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 6, 2, 30);
        panPensionData.add(tdtWfOpeningBalanceOn, gridBagConstraints);

        cboStatusOfEmp.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "", "Service", "Retired", "Suspended", "Dismissed" }));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 6;
        gridBagConstraints.ipadx = 25;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 6, 2, 30);
        panPensionData.add(cboStatusOfEmp, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.gridwidth = 5;
        gridBagConstraints.gridheight = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(110, 13, 0, 67);
        panSalaryData.add(panPensionData, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTH;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(10, 4, 4, 4);
        panSalaryDetails.add(panSalaryData, gridBagConstraints);

        tabEmployee.addTab("Present Details", panSalaryDetails);

        panOtherDetails.setMinimumSize(new java.awt.Dimension(500, 700));
        panOtherDetails.setPreferredSize(new java.awt.Dimension(1000, 1000));
        panOtherDetails.setLayout(new java.awt.GridBagLayout());
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.ipadx = 491;
        gridBagConstraints.ipady = -2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 110, 0, 0);
        panOtherDetails.add(sptSkills_Hori, gridBagConstraints);

        panEmploymentData.setMinimumSize(new java.awt.Dimension(450, 430));
        panEmploymentData.setPreferredSize(new java.awt.Dimension(450, 400));
        panEmploymentData.setLayout(new java.awt.GridBagLayout());

        panGeneraInfo.setBorder(javax.swing.BorderFactory.createTitledBorder(""));
        panGeneraInfo.setMinimumSize(new java.awt.Dimension(200, 98));
        panGeneraInfo.setLayout(new java.awt.GridBagLayout());

        panPersonalNumbers.setPreferredSize(new java.awt.Dimension(300, 300));
        panPersonalNumbers.setLayout(new java.awt.GridBagLayout());

        lblPanNo.setText("PAN No.");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHEAST;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 2, 0);
        panPersonalNumbers.add(lblPanNo, gridBagConstraints);

        txtPanNo.setMinimumSize(new java.awt.Dimension(100, 21));
        txtPanNo.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                txtPanNoFocusLost(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 2, 2, 0);
        panPersonalNumbers.add(txtPanNo, gridBagConstraints);

        lblSsnNo.setText("Social Security No.");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHEAST;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 2, 0);
        panPersonalNumbers.add(lblSsnNo, gridBagConstraints);

        txtSsnNo.setMinimumSize(new java.awt.Dimension(100, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 2, 2, 0);
        panPersonalNumbers.add(txtSsnNo, gridBagConstraints);

        lblPassPortNo.setText("Passport No.");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHEAST;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 2, 0);
        panPersonalNumbers.add(lblPassPortNo, gridBagConstraints);

        txtPassPortNo.setMinimumSize(new java.awt.Dimension(100, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 2, 2, 0);
        panPersonalNumbers.add(txtPassPortNo, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTH;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panGeneraInfo.add(panPersonalNumbers, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.gridwidth = 6;
        gridBagConstraints.ipadx = 211;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 22, 0, 22);
        panEmploymentData.add(panGeneraInfo, gridBagConstraints);

        panJobDetails.setBorder(javax.swing.BorderFactory.createTitledBorder(""));
        panJobDetails.setMinimumSize(new java.awt.Dimension(250, 200));
        panJobDetails.setPreferredSize(new java.awt.Dimension(500, 500));
        panJobDetails.setLayout(new java.awt.GridBagLayout());

        lblEmployeeType.setText("Employee Type");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHEAST;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 2, 0);
        panJobDetails.add(lblEmployeeType, gridBagConstraints);

        cboEmployeeType.setMinimumSize(new java.awt.Dimension(100, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.gridwidth = 5;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 2, 2, 0);
        panJobDetails.add(cboEmployeeType, gridBagConstraints);

        lblJobTitle.setText("Job Title");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHEAST;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 2, 0);
        panJobDetails.add(lblJobTitle, gridBagConstraints);

        cboJobTitle.setMinimumSize(new java.awt.Dimension(100, 21));
        cboJobTitle.setPopupWidth(170);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.gridwidth = 5;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 2, 2, 0);
        panJobDetails.add(cboJobTitle, gridBagConstraints);

        lblDepartment.setText("Department");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHEAST;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 2, 0);
        panJobDetails.add(lblDepartment, gridBagConstraints);

        cboDepartment.setMinimumSize(new java.awt.Dimension(100, 21));
        cboDepartment.setPopupWidth(175);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.gridwidth = 5;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 2, 2, 0);
        panJobDetails.add(cboDepartment, gridBagConstraints);

        lblManager.setText("Manager");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHEAST;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 2, 0);
        panJobDetails.add(lblManager, gridBagConstraints);

        cboManager.setMinimumSize(new java.awt.Dimension(100, 21));
        cboManager.setPopupWidth(150);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.gridwidth = 5;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 2, 2, 0);
        panJobDetails.add(cboManager, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.gridwidth = 5;
        gridBagConstraints.ipadx = 100;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 50, 0, 0);
        panEmploymentData.add(panJobDetails, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.ipady = -70;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(20, 100, 0, 0);
        panOtherDetails.add(panEmploymentData, gridBagConstraints);

        panPhysicalDetails.setBorder(javax.swing.BorderFactory.createTitledBorder(""));
        panPhysicalDetails.setMinimumSize(new java.awt.Dimension(180, 200));
        panPhysicalDetails.setPreferredSize(new java.awt.Dimension(500, 500));
        panPhysicalDetails.setLayout(new java.awt.GridBagLayout());

        lblMajorHealthProblem.setFont(new java.awt.Font("Microsoft Sans Serif", 0, 13)); // NOI18N
        lblMajorHealthProblem.setText("Major Health Problem");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.ipadx = 10;
        gridBagConstraints.ipady = 5;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHEAST;
        gridBagConstraints.insets = new java.awt.Insets(35, 36, 0, 0);
        panPhysicalDetails.add(lblMajorHealthProblem, gridBagConstraints);

        txaIdentificationMark1.setColumns(20);
        txaIdentificationMark1.setRows(5);
        txaIdentificationMark1.setMinimumSize(new java.awt.Dimension(4, 10));
        txaIdentificationMark1.setPreferredSize(new java.awt.Dimension(4, 10));
        jScrollPane2.setViewportView(txaIdentificationMark1);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.gridheight = 3;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.ipadx = 143;
        gridBagConstraints.ipady = 28;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(20, 13, 0, 0);
        panPhysicalDetails.add(jScrollPane2, gridBagConstraints);

        txaPhysicallyHandicapped.setColumns(20);
        txaPhysicallyHandicapped.setRows(5);
        txaPhysicallyHandicapped.setMinimumSize(new java.awt.Dimension(4, 10));
        txaPhysicallyHandicapped.setPreferredSize(new java.awt.Dimension(4, 10));
        jScrollPane3.setViewportView(txaPhysicallyHandicapped);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 4;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.gridheight = 3;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.ipadx = 143;
        gridBagConstraints.ipady = 28;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(20, 10, 0, 24);
        panPhysicalDetails.add(jScrollPane3, gridBagConstraints);

        txaMajorHealthProblem.setColumns(20);
        txaMajorHealthProblem.setRows(5);
        txaMajorHealthProblem.setMinimumSize(new java.awt.Dimension(4, 10));
        txaMajorHealthProblem.setPreferredSize(new java.awt.Dimension(4, 10));
        jScrollPane7.setViewportView(txaMajorHealthProblem);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 4;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.gridheight = 3;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.ipadx = 143;
        gridBagConstraints.ipady = 28;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(29, 10, 29, 24);
        panPhysicalDetails.add(jScrollPane7, gridBagConstraints);

        lblIdentificationMark1.setFont(new java.awt.Font("Microsoft Sans Serif", 0, 13)); // NOI18N
        lblIdentificationMark1.setText("Identification Mark 1");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHEAST;
        gridBagConstraints.insets = new java.awt.Insets(30, 10, 0, 0);
        panPhysicalDetails.add(lblIdentificationMark1, gridBagConstraints);

        lblIdentificationMark2.setFont(new java.awt.Font("Microsoft Sans Serif", 0, 13)); // NOI18N
        lblIdentificationMark2.setText("Identification Mark 2");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.gridheight = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHEAST;
        gridBagConstraints.insets = new java.awt.Insets(39, 10, 0, 0);
        panPhysicalDetails.add(lblIdentificationMark2, gridBagConstraints);

        lblPhysicallyHandicapped.setFont(new java.awt.Font("Microsoft Sans Serif", 0, 13)); // NOI18N
        lblPhysicallyHandicapped.setText("Physically Handicapped");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.gridheight = 2;
        gridBagConstraints.ipady = 5;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHEAST;
        gridBagConstraints.insets = new java.awt.Insets(26, 34, 2, 0);
        panPhysicalDetails.add(lblPhysicallyHandicapped, gridBagConstraints);

        txaIdentificationMark2.setColumns(20);
        txaIdentificationMark2.setRows(5);
        txaIdentificationMark2.setMinimumSize(new java.awt.Dimension(4, 10));
        txaIdentificationMark2.setPreferredSize(new java.awt.Dimension(4, 10));
        jScrollPane8.setViewportView(txaIdentificationMark2);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.gridheight = 3;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.ipadx = 143;
        gridBagConstraints.ipady = 28;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(29, 13, 29, 0);
        panPhysicalDetails.add(jScrollPane8, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.gridwidth = 3;
        gridBagConstraints.ipadx = 480;
        gridBagConstraints.ipady = -20;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(20, 40, 78, 19);
        panOtherDetails.add(panPhysicalDetails, gridBagConstraints);

        tabEmployee.addTab("Other Details", panOtherDetails);

        panPerformance.setMinimumSize(new java.awt.Dimension(470, 750));
        panPerformance.setPreferredSize(new java.awt.Dimension(470, 750));
        panPerformance.setLayout(new java.awt.GridBagLayout());

        srpResponsibility.setMinimumSize(new java.awt.Dimension(346, 100));
        srpResponsibility.setPreferredSize(new java.awt.Dimension(346, 100));

        txaResponsibility.setMinimumSize(new java.awt.Dimension(20, 20));
        txaResponsibility.setPreferredSize(new java.awt.Dimension(20, 20));
        srpResponsibility.setViewportView(txaResponsibility);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 8;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.gridheight = 2;
        gridBagConstraints.ipadx = -120;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(100, 10, 0, 45);
        panPerformance.add(srpResponsibility, gridBagConstraints);

        srpPerformance.setMinimumSize(new java.awt.Dimension(346, 100));
        srpPerformance.setPreferredSize(new java.awt.Dimension(346, 100));

        txaPerformance.setMinimumSize(new java.awt.Dimension(20, 20));
        txaPerformance.setPreferredSize(new java.awt.Dimension(20, 20));
        srpPerformance.setViewportView(txaPerformance);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 8;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.gridheight = 2;
        gridBagConstraints.ipadx = -120;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(26, 10, 0, 45);
        panPerformance.add(srpPerformance, gridBagConstraints);

        srpComments.setMinimumSize(new java.awt.Dimension(346, 100));
        srpComments.setPreferredSize(new java.awt.Dimension(346, 100));

        txaComments.setMinimumSize(new java.awt.Dimension(50, 50));
        txaComments.setPreferredSize(new java.awt.Dimension(50, 50));
        srpComments.setViewportView(txaComments);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 8;
        gridBagConstraints.gridy = 6;
        gridBagConstraints.gridheight = 2;
        gridBagConstraints.ipadx = -120;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(26, 10, 0, 45);
        panPerformance.add(srpComments, gridBagConstraints);

        srpEducation.setMinimumSize(new java.awt.Dimension(346, 100));
        srpEducation.setPreferredSize(new java.awt.Dimension(346, 100));

        txaEducation.setMaximumSize(new java.awt.Dimension(20, 20));
        txaEducation.setMinimumSize(new java.awt.Dimension(20, 20));
        txaEducation.setPreferredSize(new java.awt.Dimension(20, 20));
        srpEducation.setViewportView(txaEducation);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.gridheight = 2;
        gridBagConstraints.ipadx = -120;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(26, 10, 0, 0);
        panPerformance.add(srpEducation, gridBagConstraints);

        srpResponsibility3.setMinimumSize(new java.awt.Dimension(346, 100));
        srpResponsibility3.setPreferredSize(new java.awt.Dimension(346, 100));

        txaSkills.setMinimumSize(new java.awt.Dimension(20, 20));
        txaSkills.setPreferredSize(new java.awt.Dimension(20, 20));
        srpResponsibility3.setViewportView(txaSkills);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.ipadx = -120;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(100, 10, 0, 0);
        panPerformance.add(srpResponsibility3, gridBagConstraints);

        srpExperience.setMinimumSize(new java.awt.Dimension(346, 100));
        srpExperience.setPreferredSize(new java.awt.Dimension(500, 100));

        txaExperience.setMinimumSize(new java.awt.Dimension(20, 20));
        txaExperience.setPreferredSize(new java.awt.Dimension(20, 20));
        srpExperience.setViewportView(txaExperience);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 6;
        gridBagConstraints.gridheight = 2;
        gridBagConstraints.ipadx = -120;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(26, 10, 0, 0);
        panPerformance.add(srpExperience, gridBagConstraints);

        lblSkills.setText("Skills");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(108, 55, 0, 0);
        panPerformance.add(lblSkills, gridBagConstraints);

        lblEducation.setText("Education");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(34, 38, 0, 0);
        panPerformance.add(lblEducation, gridBagConstraints);

        lblExperience.setText("Experiences");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 6;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(34, 35, 0, 0);
        panPerformance.add(lblExperience, gridBagConstraints);

        lblResponsibility.setText("Responsibility");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 5;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(108, 24, 0, 0);
        panPerformance.add(lblResponsibility, gridBagConstraints);

        lblPerformance.setText("Performance");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 5;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(34, 28, 0, 0);
        panPerformance.add(lblPerformance, gridBagConstraints);

        lblComments.setText("Comments");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 5;
        gridBagConstraints.gridy = 6;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(34, 33, 0, 0);
        panPerformance.add(lblComments, gridBagConstraints);

        sptSkills_Vert1.setOrientation(javax.swing.SwingConstants.VERTICAL);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 4;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.gridheight = 3;
        gridBagConstraints.ipadx = 1;
        gridBagConstraints.ipady = 129;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(84, 14, 0, 0);
        panPerformance.add(sptSkills_Vert1, gridBagConstraints);

        sptSkills_Vert2.setOrientation(javax.swing.SwingConstants.VERTICAL);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 4;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.gridheight = 3;
        gridBagConstraints.ipadx = 1;
        gridBagConstraints.ipady = 131;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(10, 14, 0, 0);
        panPerformance.add(sptSkills_Vert2, gridBagConstraints);

        sptSkills_Vert3.setOrientation(javax.swing.SwingConstants.VERTICAL);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 4;
        gridBagConstraints.gridy = 6;
        gridBagConstraints.gridheight = 3;
        gridBagConstraints.ipadx = 1;
        gridBagConstraints.ipady = 131;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(8, 14, 84, 0);
        panPerformance.add(sptSkills_Vert3, gridBagConstraints);

        tabEmployee.addTab("Skills and Performance", panPerformance);

        panEmployee.add(tabEmployee, new java.awt.GridBagConstraints());

        getContentPane().add(panEmployee, java.awt.BorderLayout.CENTER);

        panStatus.setLayout(new java.awt.GridBagLayout());

        lblSpace1.setText(" Status :");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.fill = java.awt.GridBagConstraints.VERTICAL;
        gridBagConstraints.weighty = 1.0;
        panStatus.add(lblSpace1, gridBagConstraints);

        lblStatus1.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        lblStatus1.setText("                      ");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.fill = java.awt.GridBagConstraints.VERTICAL;
        gridBagConstraints.weighty = 1.0;
        panStatus.add(lblStatus1, gridBagConstraints);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        panStatus.add(lblMsg, gridBagConstraints);

        getContentPane().add(panStatus, java.awt.BorderLayout.SOUTH);

        btnView.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_Summary.gif"))); // NOI18N
        btnView.setToolTipText("Enquiry");
        btnView.setEnabled(false);
        btnView.setMinimumSize(new java.awt.Dimension(21, 21));
        btnView.setPreferredSize(new java.awt.Dimension(21, 21));
        btnView.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnViewActionPerformed(evt);
            }
        });
        tbrLoantProduct.add(btnView);

        lblSpace5.setText("     ");
        tbrLoantProduct.add(lblSpace5);

        btnNew.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_NEW.gif"))); // NOI18N
        btnNew.setToolTipText("New");
        btnNew.setEnabled(false);
        btnNew.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnNewActionPerformed(evt);
            }
        });
        tbrLoantProduct.add(btnNew);

        lblSpace17.setForeground(new java.awt.Color(240, 240, 240));
        lblSpace17.setText("     ");
        lblSpace17.setMaximumSize(new java.awt.Dimension(3, 18));
        lblSpace17.setMinimumSize(new java.awt.Dimension(3, 18));
        lblSpace17.setPreferredSize(new java.awt.Dimension(3, 18));
        tbrLoantProduct.add(lblSpace17);

        btnEdit.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_EDIT.gif"))); // NOI18N
        btnEdit.setToolTipText("Edit");
        btnEdit.setEnabled(false);
        btnEdit.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnEditActionPerformed(evt);
            }
        });
        tbrLoantProduct.add(btnEdit);

        lblSpace18.setForeground(new java.awt.Color(240, 240, 240));
        lblSpace18.setText("     ");
        lblSpace18.setMaximumSize(new java.awt.Dimension(3, 18));
        lblSpace18.setMinimumSize(new java.awt.Dimension(3, 18));
        lblSpace18.setPreferredSize(new java.awt.Dimension(3, 18));
        tbrLoantProduct.add(lblSpace18);

        btnDelete.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_DELETE.gif"))); // NOI18N
        btnDelete.setToolTipText("Delete");
        btnDelete.setEnabled(false);
        btnDelete.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnDeleteActionPerformed(evt);
            }
        });
        tbrLoantProduct.add(btnDelete);

        lblSpace2.setText("     ");
        tbrLoantProduct.add(lblSpace2);

        btnSave.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_SAVE.gif"))); // NOI18N
        btnSave.setToolTipText("Save");
        btnSave.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSaveActionPerformed(evt);
            }
        });
        tbrLoantProduct.add(btnSave);

        lblSpace19.setForeground(new java.awt.Color(240, 240, 240));
        lblSpace19.setText("     ");
        lblSpace19.setMaximumSize(new java.awt.Dimension(3, 18));
        lblSpace19.setMinimumSize(new java.awt.Dimension(3, 18));
        lblSpace19.setPreferredSize(new java.awt.Dimension(3, 18));
        tbrLoantProduct.add(lblSpace19);

        btnCancel.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_CANCEL.gif"))); // NOI18N
        btnCancel.setToolTipText("Cancel");
        btnCancel.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCancelActionPerformed(evt);
            }
        });
        tbrLoantProduct.add(btnCancel);

        lblSpace3.setText("     ");
        tbrLoantProduct.add(lblSpace3);

        btnAuthorize.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_AUTHORIZE.gif"))); // NOI18N
        btnAuthorize.setToolTipText("Authorize");
        btnAuthorize.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnAuthorizeActionPerformed(evt);
            }
        });
        tbrLoantProduct.add(btnAuthorize);

        lblSpace20.setForeground(new java.awt.Color(240, 240, 240));
        lblSpace20.setText("     ");
        lblSpace20.setMaximumSize(new java.awt.Dimension(3, 18));
        lblSpace20.setMinimumSize(new java.awt.Dimension(3, 18));
        lblSpace20.setPreferredSize(new java.awt.Dimension(3, 18));
        tbrLoantProduct.add(lblSpace20);

        btnException.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_EXCEPTION.gif"))); // NOI18N
        btnException.setToolTipText("Exception");
        btnException.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnExceptionActionPerformed(evt);
            }
        });
        tbrLoantProduct.add(btnException);

        lblSpace21.setForeground(new java.awt.Color(240, 240, 240));
        lblSpace21.setText("     ");
        lblSpace21.setMaximumSize(new java.awt.Dimension(3, 18));
        lblSpace21.setMinimumSize(new java.awt.Dimension(3, 18));
        lblSpace21.setPreferredSize(new java.awt.Dimension(3, 18));
        tbrLoantProduct.add(lblSpace21);

        btnReject.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_REJECT.gif"))); // NOI18N
        btnReject.setToolTipText("Reject");
        btnReject.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnRejectActionPerformed(evt);
            }
        });
        tbrLoantProduct.add(btnReject);

        lblSpace4.setText("     ");
        tbrLoantProduct.add(lblSpace4);

        btnPrint.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_PRINT.gif"))); // NOI18N
        btnPrint.setToolTipText("Print");
        btnPrint.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnPrintActionPerformed(evt);
            }
        });
        tbrLoantProduct.add(btnPrint);

        lblSpace22.setForeground(new java.awt.Color(240, 240, 240));
        lblSpace22.setText("     ");
        lblSpace22.setMaximumSize(new java.awt.Dimension(3, 18));
        lblSpace22.setMinimumSize(new java.awt.Dimension(3, 18));
        lblSpace22.setPreferredSize(new java.awt.Dimension(3, 18));
        tbrLoantProduct.add(lblSpace22);

        btnClose.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_CLOSE.gif"))); // NOI18N
        btnClose.setToolTipText("Close");
        btnClose.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCloseActionPerformed(evt);
            }
        });
        tbrLoantProduct.add(btnClose);

        getContentPane().add(tbrLoantProduct, java.awt.BorderLayout.NORTH);

        mnuProcess.setText("Process");

        mitNew.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_N, java.awt.event.InputEvent.CTRL_MASK));
        mitNew.setMnemonic('N');
        mitNew.setText("New");
        mitNew.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mitNewActionPerformed(evt);
            }
        });
        mnuProcess.add(mitNew);

        mitEdit.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_E, java.awt.event.InputEvent.CTRL_MASK));
        mitEdit.setMnemonic('E');
        mitEdit.setText("Edit");
        mitEdit.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mitEditActionPerformed(evt);
            }
        });
        mnuProcess.add(mitEdit);

        mitDelete.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_D, java.awt.event.InputEvent.CTRL_MASK));
        mitDelete.setMnemonic('D');
        mitDelete.setText("Delete");
        mitDelete.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mitDeleteActionPerformed(evt);
            }
        });
        mnuProcess.add(mitDelete);
        mnuProcess.add(sptProcess);

        mitSave.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_S, java.awt.event.InputEvent.CTRL_MASK));
        mitSave.setMnemonic('S');
        mitSave.setText("Save");
        mitSave.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mitSaveActionPerformed(evt);
            }
        });
        mnuProcess.add(mitSave);

        mitCancel.setMnemonic('C');
        mitCancel.setText("Cancel");
        mitCancel.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mitCancelActionPerformed(evt);
            }
        });
        mnuProcess.add(mitCancel);
        mnuProcess.add(sptCancel);

        mitPrint.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_P, java.awt.event.InputEvent.CTRL_MASK));
        mitPrint.setMnemonic('P');
        mitPrint.setText("Print");
        mitPrint.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mitPrintActionPerformed(evt);
            }
        });
        mnuProcess.add(mitPrint);
        mnuProcess.add(sptPrint);

        mitClose.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_L, java.awt.event.InputEvent.CTRL_MASK));
        mitClose.setMnemonic('l');
        mitClose.setText("Close");
        mitClose.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mitCloseActionPerformed(evt);
            }
        });
        mnuProcess.add(mitClose);

        mbrLoanProduct.add(mnuProcess);

        setJMenuBar(mbrLoanProduct);

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btnViewActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnViewActionPerformed
        // TODO add your handling code here:
        observable.setActionType(ClientConstants.ACTIONTYPE_VIEW);
        observable.setStatus();
        lblStatus1.setText(observable.getLblStatus());
        popUp(VIEW);
        btnCheck();
        cboAddressType.setEnabled(true);
        final String CUSTID = CommonUtil.convertObjToStr(observable.getTxtCustomerId());
        observable.getCustAddrData(CUSTID);
        cboAddressType.setModel(observable.getCbmAddressType());
    }//GEN-LAST:event_btnViewActionPerformed

    private void btnExceptionActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnExceptionActionPerformed
        // TODO add your handling code here:
        observable.setActionType(ClientConstants.ACTIONTYPE_EXCEPTION);
        authorizeStatus(CommonConstants.STATUS_EXCEPTION);
        authorize = false;
    }//GEN-LAST:event_btnExceptionActionPerformed

    private void btnRejectActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnRejectActionPerformed
        // TODO add your handling code here:
        observable.setActionType(ClientConstants.ACTIONTYPE_REJECT);
        authorizeStatus(CommonConstants.STATUS_REJECTED);
        authorize = false;
    }//GEN-LAST:event_btnRejectActionPerformed

    private void btnAuthorizeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAuthorizeActionPerformed
        // TODO add your handling code here:
        observable.setActionType(ClientConstants.ACTIONTYPE_AUTHORIZE);
        setModified(true);
        authorizeStatus(CommonConstants.STATUS_AUTHORIZED);
        ClientUtil.enableDisable(this, false);
        authorize = true;
    }//GEN-LAST:event_btnAuthorizeActionPerformed
    public void authorizeStatus(String authorizeStatus) {
        if (viewType == AUTHORIZE && isFilled) {
            HashMap singleAuthorizeMap = new HashMap();
            singleAuthorizeMap.put("STATUS", authorizeStatus);
            singleAuthorizeMap.put("USER_ID", TrueTransactMain.USER_ID);
            singleAuthorizeMap.put("EMPLOYEE CODE", this.txtEmployeeId.getText());
            singleAuthorizeMap.put("CURR_DATE", setProperDateFormat(currDt));
            ClientUtil.execute("authorizeEmployee", singleAuthorizeMap);
            ClientUtil.execute("authorizeEmployeeOtherDet", singleAuthorizeMap);
            ClientUtil.execute("authorizeEmployeePresentDet", singleAuthorizeMap);
            if (authorize == true) {
                observable.setAuthorized(true);
                updateOBFields();
                observable.doAction();
            } else {
                observable.setAuthorized(false);
            }
            btnCancelActionPerformed(null);
            btnCancel.setEnabled(true);
            viewType = 0;
        } else {
            viewType = AUTHORIZE;
            final HashMap mapParam = new HashMap();
            mapParam.put(CommonConstants.MAP_NAME, "getSelectEmployeeAuthorizationList");
            mapParam.put(CommonConstants.AUTHORIZESTATUS, authorizeStatus);
            isFilled = false;
            AuthorizeUI authorizeUI = new AuthorizeUI(this, mapParam);
            authorizeUI.show();
            btnCancel.setEnabled(true);
            btnSave.setEnabled(false);
            ClientUtil.enableDisable(this, false);
            btnNetSalaryAccNo.setEnabled(false);
            lblStatus1.setText(observable.getLblStatus());
            panAddress.setVisible(false);
        }
    }

    private void readPhoto() {
        try {
            photoFile = fileName.substring(fileName.lastIndexOf("."));
            FileInputStream reader = new FileInputStream(selFile);
            int size = reader.available();
            photoByteArray = new byte[size];
            reader.read(photoByteArray);
        } catch (Exception e) {
            log.info("catch in readPhoto:" + e);
        }
    }
    private void btnCloseActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCloseActionPerformed
        // Add your handling code here:
        cifClosingAlert();
    }//GEN-LAST:event_btnCloseActionPerformed

    private void btnCancelActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCancelActionPerformed
        // Add your handling code here:
        txtCustomerId.setText("");
        super.removeEditLock(txtEmployeeId.getText());
        observable.resetForm();// Reset the fields in the UI to null...
        ClientUtil.enableDisable(this, false);// Disables the panel...
        disableButtons();// To Disable the folder buttons in the UI...
        setButtonEnableDisable();// Enables or Disables the buttons and menu Items depending on their previous state...
        observable.setActionType(ClientConstants.ACTIONTYPE_CANCEL);//Sets the Action Type to be performed...
        observable.setStatus();// To set the Value of lblStatus...
        setPictuteEnableDisable(false);//To Enable the Load Button for Picture...
        this.btnRemove.setEnabled(false);
        lblPicture.setIcon(null);
        fileName = null;
        btnSave.setEnabled(false);
        btnAuthorize.setEnabled(true);
        btnReject.setEnabled(true);
        btnException.setEnabled(true);
        viewType = 0;
        setModified(false);
        txtPresentBasicSalary.setEditable(false);
        cboBloodGroup.setSelectedIndex(0);
        cboStatusOfEmp.setSelectedIndex(0);
        cboManager.setSelectedIndex(0);
        btnNetSalaryAccNo.setEnabled(false);
    }//GEN-LAST:event_btnCancelActionPerformed

    private void btnSaveActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSaveActionPerformed
        // Add your handling code here:
        updateOBFields();
        setModified(false);
        //To display an alert if the mandatory fields are not properly inputted, else proceed with normal operation
        final String mandatoryMessage = new MandatoryCheck().checkMandatory(getClass().getName(), panEmployee);
        if (observable.getActionType() != ClientConstants.ACTIONTYPE_DELETE && mandatoryMessage.length() > 0) {
            displayAlert(mandatoryMessage);
        } else {
            observable.doAction();// To perform the necessary operation depending on the Action type...
            super.removeEditLock(txtEmployeeId.getText());
            observable.resetForm();// Reset the fields in the UI to null...
            if (observable.getActionType() != ClientConstants.ACTIONTYPE_DELETE) {
                enableDisableButtons();// To disable the buttons(folder) in the UI...
            }
            ClientUtil.enableDisable(this, false);// Disables the panel...
            setButtonEnableDisable();// Enables or Disables the buttons and menu Items depending on their previous state...
            observable.setResultStatus();// To Reset the Value of lblStatus...
            setPictuteEnableDisable(false);//To Enable the Load Button for Picture...
            this.btnRemove.setEnabled(false);
            lblPicture.setIcon(null);
            fileName = null;
            txtCustomerId.setText("");
            cboBloodGroup.setSelectedIndex(0);
            cboStatusOfEmp.setSelectedIndex(0);
            cboManager.setSelectedIndex(0);
            btnNetSalaryAccNo.setEnabled(false);
        }
        btnAuthorize.setEnabled(true);
        btnReject.setEnabled(true);
        btnException.setEnabled(true);
    }//GEN-LAST:event_btnSaveActionPerformed

    private void displayAlert(String message) {
        CMandatoryDialog cmd = new CMandatoryDialog();
        cmd.setMessage(message);
        cmd.show();
    }
    //To enable and/or Disable buttons(folder)...

    private void enableDisableButtons() {
        btnCustomerId.setEnabled(!btnCustomerId.isEnabled());
    }

    // called from cancel button, to make all the buttons as disabled...
    private void disableButtons() {
        btnCustomerId.setEnabled(false);
    }
    private void btnDeleteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnDeleteActionPerformed
        // Add your handling code here:
        observable.resetForm();// Reset the fields in the UI to null...
        observable.setActionType(ClientConstants.ACTIONTYPE_DELETE);//Sets the Action Type to be performed...
        popUp(DELETE);
        btnAuthorize.setEnabled(false);
        btnReject.setEnabled(false);
        btnException.setEnabled(false);
    }//GEN-LAST:event_btnDeleteActionPerformed

    private void btnEditActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnEditActionPerformed
        // Add your handling code here:
        observable.resetForm();// Reset the fields in the UI to null...
        observable.setActionType(ClientConstants.ACTIONTYPE_EDIT);//Sets the Action Type to be performed...
        popUp(EDIT);
        btnAuthorize.setEnabled(false);
        btnReject.setEnabled(false);
        btnException.setEnabled(false);
        btnNetSalaryAccNo.setEnabled(true);
        panAddress.setVisible(true);
        txtPresentBasicSalary.setEditable(false);
        btnNetSalaryAccNo.setEnabled(false);

    }//GEN-LAST:event_btnEditActionPerformed
    private void btnCheck() {
        btnCancel.setEnabled(true);
        btnSave.setEnabled(false);
        btnNew.setEnabled(false);
        btnDelete.setEnabled(false);
        btnAuthorize.setEnabled(false);
        btnReject.setEnabled(false);
        btnException.setEnabled(false);
        btnEdit.setEnabled(false);
    }

    private void popUp(int field) {
        final HashMap viewMap = new HashMap();
        viewType = field;
        if (field == EDIT || field == DELETE || field == VIEW) {//Edit=0 and Delete=1
            ArrayList lst = new ArrayList();
            lst.add("EMPLOYEE_ID");
            viewMap.put(ClientConstants.RECORD_KEY_COL, lst);
            lst = null;
            viewMap.put("MAPNAME", "viewEmployee");//mapped statement: viewEmployee---> result map should be a Hashmap...
        }
        new ViewAll(this, viewMap).show();
    }

    // this method is called automatically from ViewAll...
    public void fillData(Object param) {
        final HashMap hash = (HashMap) param;
        if (viewType == EDIT || viewType == DELETE || viewType == AUTHORIZE || viewType == VIEW) {
            isFilled = true;
            hash.put("WHERE", hash.get("EMPLOYEE_ID"));
            if (viewType == EDIT) {
                try {
                    super.setMode(ClientConstants.ACTIONTYPE_EDIT);
                    super.setOpenForEditBy(TrueTransactMain.USER_ID);
                    super.removeEditLock(CommonUtil.convertObjToStr(hash.get("EMPLOYEE_ID")));
                    observable.populateData(hash);// Called to display the Data in the UI fields...
                    observable.getCustId(hash);
                    txtCustomerId.setText(observable.getTxtCustomerId());
                    final String CUSTID = CommonUtil.convertObjToStr(observable.getTxtCustomerId());
                    observable.getCustAddrData(CUSTID);
                    cboAddressType.setModel(observable.getCbmAddressType());
                    if (observable.isDaAppl() == true) {
                        chkDAApplicable.setSelected(true);
                    }
                    if (observable.isHraAppl() == true) {
                        chkHRAApplicable.setSelected(true);
                    }
                    if (observable.isStopPayt() == true) {
                        chkStopPayt.setSelected(true);
                    }

                } catch (SQLException ex) {
                    java.util.logging.Logger.getLogger(EmployeeUI.class.getName()).log(Level.SEVERE, null, ex);
                }

            }
            if (viewType == AUTHORIZE) {
                try {
                    hash.put("WHERE", hash.get("EMPLOYEEID"));
                    observable.populateData(hash);// Called to display the Data in the UI fields...
                    observable.getCustIdAuth(hash);
                    txtCustomerId.setText(observable.getTxtCustomerId());
                } catch (SQLException ex) {
                    java.util.logging.Logger.getLogger(EmployeeUI.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
            if (viewType == VIEW) {
                try {
                    hash.put("WHERE", hash.get("EMPLOYEE_ID"));
                    observable.populateData(hash);// Called to display the Data in the UI fields...
                    observable.getCustId(hash);
                    txtCustomerId.setText(observable.getTxtCustomerId());
                } catch (SQLException ex) {
                    java.util.logging.Logger.getLogger(EmployeeUI.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
            // To Insert the Photo from Database to UI...
            fileName = new StringBuffer(ClientConstants.SERVER_ROOT).append("employee/");
            photoFile = observable.getPhotoFile();
            fileName.append(photoFile);
            try {
                lblPicture.setIcon(new javax.swing.ImageIcon(new java.net.URL(CommonUtil.convertObjToStr(fileName))));
                lblPicture.setIcon(new javax.swing.ImageIcon(new java.net.URL(CommonUtil.convertObjToStr(fileName))));

            } catch (Exception e) {
                log.info("Picture error in fillData");
            }
            if (observable.getActionType() == ClientConstants.ACTIONTYPE_DELETE || observable.getActionType() == ClientConstants.ACTIONTYPE_VIEW) {
                ClientUtil.enableDisable(this, false);// Disables the panel...
                observable.setPhotoFile(null);
            } else {
                ClientUtil.enableDisable(this, true);// Enables the panel...
                enableDisableButtons();// To Enable the Buttons(folder) buttons in UI...
                setPictuteEnableDisable(true);//To Enable the Load Button for Picture...
                // If there is no Image, Remove button should be disabled...
                if (observable.getPhotoFile() != null) {
                    this.btnRemove.setEnabled(true);
                }
                disableTextBox();
            }
            setButtonEnableDisable();// Enables or Disables the buttons and menu Items depending on their previous state...
            observable.setStatus();// To set the Value of lblStatus...
        }
        if (viewType == AUTHORIZE) {
            btnAuthorize.setEnabled(observable.getActionType() == ClientConstants.ACTIONTYPE_AUTHORIZE ? true : false);
            btnReject.setEnabled(observable.getActionType() == ClientConstants.ACTIONTYPE_REJECT ? true : false);
            btnException.setEnabled(observable.getActionType() == ClientConstants.ACTIONTYPE_EXCEPTION ? true : false);
        }
        if (viewType == CUSTOMER) {
            try {
                txtEmployeeId.setEnabled(true);
                txtEmployeeId.setEditable(false);
                cboAddressType.setEnabled(true);

                //__ To reset the data for the Previous selected Customer..
                final String CUSTID = CommonUtil.convertObjToStr(hash.get("CUST_ID"));
                txtCustomerId.setText(CUSTID);

                //__ To get the ComboBox value for the Communication Addr type...
                observable.getCustAddrData(CUSTID);
                cboAddressType.setModel(observable.getCbmAddressType());
                observable.getCustData(hash);
                txtEmployeeId.setText(observable.getTxtEmployeeId());
                txtTitle.setText(observable.getTxtTitle());
                txtFirstName.setText(observable.getTxtFirstName());
                txtMiddleName.setText(observable.getTxtMiddleName());
                txtLastName.setText(observable.getTxtLastName());
                txtOfficialEmail.setText(observable.getTxtOfficialEmail());
                txtMartialStatus.setText(observable.getTxtMartialStatus());
                txtCaste.setText(observable.getTxtCaste());
                txtReligion.setText(observable.getTxtReligion());
                if (observable.getTxtMartialStatus().equals("Married")) {
                    txtSpouseName.setEnabled(true);
                    txtSpouseRelation.setEnabled(true);
                } else {
                    txtSpouseName.setEnabled(false);
                    txtSpouseRelation.setEnabled(false);
                }
                tdtBirthDate.setDateValue(observable.getTdtBirthDate());
                txtSsnNo.setText(observable.getTxtSsnNo());
                txtPanNo.setText(observable.getTxtPanNo());
                if (observable.getRdoGender_Male() == true) {
                    rdoGender_Male.setSelected(true);
                    rdoGender_Female.setSelected(false);
                } else if (observable.getRdoGender_Female() == true) {
                    rdoGender_Female.setSelected(true);
                    rdoGender_Male.setSelected(false);
                } else {
                    rdoGender_Male.setSelected(false);
                    rdoGender_Female.setSelected(false);
                }
                //__ To set the Communication addr Type...
                cboAddressType.setSelectedItem(((ComboBoxModel) cboAddressType.getModel()).getDataForKey(observable.getAddrType()));
                observable.getCustPhone(hash);
                txtOfficePhone.setText(observable.getTxtOfficePhone());
                HashMap whereMap = new HashMap();
                whereMap.put("CUST_ID", txtCustomerId.getText());
                List list = ClientUtil.executeQuery("getCustWithEmplId", whereMap);
                if (list.size() > 0 && list != null) {
                    HashMap map = (HashMap) list.get(0);
                    if (map.containsKey("STAFF_ID") && map.get("STAFF_ID") != null) {
                        ClientUtil.showMessageWindow("This customer is already an Employee!!!");
                    }
                    txtCustomerId.setText("");
                    observable.resetForm();
                    new CheckCustomerIdUI(this);
                }

            } catch (SQLException ex) {
                java.util.logging.Logger.getLogger(EmployeeUI.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        if (viewType == ACCNO) {
            try {
                txtNetSalaryAccNo.setText(CommonUtil.convertObjToStr(hash.get("ACCOUNTNO")));
                txtCustomerName.setText(CommonUtil.convertObjToStr(hash.get("CUSTOMERNAME")));
            } catch (Exception ex) {
                java.util.logging.Logger.getLogger(EmployeeUI.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    private void enableTxtFields() {
        txtFirstName.setEnabled(true);
        txtFirstName.setVisible(true);
        txtTitle.setEnabled(true);
        txtTitle.setVisible(true);
        txtLastName.setEnabled(true);
        txtLastName.setVisible(true);
        txtMartialStatus.setEnabled(true);
        txtMartialStatus.setVisible(true);
        txtOfficialEmail.setEnabled(true);
        txtOfficialEmail.setVisible(true);
        txtStreet.setEnabled(true);
        txtStreet.setVisible(true);
        txtArea.setEnabled(true);
        txtArea.setVisible(true);
        txtCity.setEnabled(true);
        txtCity.setVisible(true);
        txtState.setEnabled(true);
        txtState.setVisible(true);
        txtCountry.setEnabled(true);
        txtCountry.setVisible(true);
        txtPinCode.setEnabled(true);
        txtPinCode.setVisible(true);
        txaIdentificationMark1.setVisible(true);
        txaComments.setVisible(true);
        txaEducation.setVisible(true);
        txaExperience.setVisible(true);
        txaIdentificationMark2.setVisible(true);
        txaMajorHealthProblem.setVisible(true);
        txaPerformance.setVisible(true);
        txaPhysicallyHandicapped.setVisible(true);
        txaResponsibility.setVisible(true);
        txaSkills.setVisible(true);
        txaIdentificationMark1.setEnabled(true);
        txaComments.setEnabled(true);
        txaEducation.setEnabled(true);
        txaExperience.setEnabled(true);
        txaIdentificationMark2.setEnabled(true);
        txaMajorHealthProblem.setEnabled(true);
        txaPerformance.setEnabled(true);
        txaPhysicallyHandicapped.setEnabled(true);
        txaResponsibility.setEnabled(true);
        txaSkills.setEnabled(true);
    }

    private void btnNewActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnNewActionPerformed
        // Add your handling code here:
        setModified(true);
        observable.resetForm();// Reset the fields in the UI to null...
        ClientUtil.enableDisable(this, true);// Enables the panel...
        observable.setActionType(ClientConstants.ACTIONTYPE_NEW);//Sets the Action Type to be performed...
        setButtonEnableDisable();// Enables or Disables the buttons and menu Items depending on their previous state...
        observable.setStatus();// To set the Value of lblStatus...
        enableDisableButtons();// To enable the buttons(folder) in the UI...
        disableTextBox();
        enableTxtFields();
        setPictuteEnableDisable(true);//To Enable the Load Button for Picture...
        this.btnRemove.setEnabled(false);
        btnAuthorize.setEnabled(false);
        btnReject.setEnabled(false);
        btnException.setEnabled(false);
        txtScaleId.setEditable(false);
        txtVersionNo.setEditable(false);
        txtPresentBasicSalary.setEditable(false);
        btnNetSalaryAccNo.setEnabled(true);
        panAddress.setVisible(true);
        lblPicture.setIcon(null);
        fileName = null;
        cboBloodGroup.setSelectedIndex(0);
        cboStatusOfEmp.setSelectedIndex(0);
        cboManager.setSelectedIndex(0);
        btnNetSalaryAccNo.setEnabled(false);
        cboAddressType.setEnabled(false);
    }//GEN-LAST:event_btnNewActionPerformed
    //To Enable the Load Button for Picture...

    private void setPictuteEnableDisable(boolean load) {
        this.btnLoad.setEnabled(load);
    }

    private void disableTextBox() {
        txtEmployeeId.setEditable(false);
        txtCustomerId.setEditable(true);
    }

    private void mitCloseActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mitCloseActionPerformed
        // Add your handling code here:
        btnCloseActionPerformed(evt);
    }//GEN-LAST:event_mitCloseActionPerformed

    private void mitPrintActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mitPrintActionPerformed
        // Add your handling code here:
        btnPrintActionPerformed(evt);
    }//GEN-LAST:event_mitPrintActionPerformed

    private void mitCancelActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mitCancelActionPerformed
        // Add your handling code here:
        btnCancelActionPerformed(evt);
    }//GEN-LAST:event_mitCancelActionPerformed

    private void mitSaveActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mitSaveActionPerformed
        // Add your handling code here:
        btnSaveActionPerformed(evt);
    }//GEN-LAST:event_mitSaveActionPerformed

    private void mitDeleteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mitDeleteActionPerformed
        // Add your handling code here:
        btnDeleteActionPerformed(evt);
    }//GEN-LAST:event_mitDeleteActionPerformed

    private void mitEditActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mitEditActionPerformed
        // Add your handling code here:
        btnEditActionPerformed(evt);
    }//GEN-LAST:event_mitEditActionPerformed

    private void mitNewActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mitNewActionPerformed
        // Add your handling code here:
        btnNewActionPerformed(evt);
    }//GEN-LAST:event_mitNewActionPerformed

    /**
     * Exit the Application
     */
    private void exitForm(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_exitForm
        System.exit(0);
    }//GEN-LAST:event_exitForm

private void txtPanNoFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtPanNoFocusLost
    ClientUtil.validatePAN(txtPanNo);
}//GEN-LAST:event_txtPanNoFocusLost

private void cboAddressTypeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cboAddressTypeActionPerformed
// TODO add your handling code here:
    final String ADDRTYPE = CommonUtil.convertObjToStr(((ComboBoxModel) (cboAddressType.getModel())).getKeyForSelected());
    if (!ADDRTYPE.equalsIgnoreCase("")) {
        HashMap dataMap = new HashMap();
        dataMap.put("CUST_ID", txtCustomerId.getText());
        dataMap.put("ADDR_TYPE", ADDRTYPE);
        final HashMap resultMap = observable.getCustAddrType(dataMap);
        HashMap lookmap = new HashMap();
        lookmap.put("LOOKUP_REF_ID", CommonUtil.convertObjToStr(resultMap.get("CITY")));
        lookmap.put("LOOKUP_ID", "CUSTOMER.CITY");
        List list = ClientUtil.executeQuery("getSelectLookUpDesc", lookmap);
        HashMap result = new HashMap();
        if (list != null && list.size() > 0) {
            result = (HashMap) list.get(0);
        }
        if (result != null && result.containsKey("LOOKUP_DESC")) {
            txtCity.setText(CommonUtil.convertObjToStr(result.get("LOOKUP_DESC")));
        }
        txtStreet.setText(CommonUtil.convertObjToStr(resultMap.get("STREET")));
        txtArea.setText(CommonUtil.convertObjToStr(resultMap.get("AREA")));
        txtPinCode.setText(CommonUtil.convertObjToStr(resultMap.get("PIN_CODE")));

        lookmap.put("LOOKUP_REF_ID", CommonUtil.convertObjToStr(resultMap.get("STATE")));
        lookmap.put("LOOKUP_ID", "CUSTOMER.STATE");
        list = ClientUtil.executeQuery("getSelectLookUpDesc", lookmap);
        if (list != null && list.size() > 0) {
            result = (HashMap) list.get(0);
        }
        if (result != null && result.containsKey("LOOKUP_DESC")) {
            txtState.setText(CommonUtil.convertObjToStr(result.get("LOOKUP_DESC")));
        }
        lookmap.put("LOOKUP_REF_ID", CommonUtil.convertObjToStr(resultMap.get("COUNTRY_CODE")));
        lookmap.put("LOOKUP_ID", "CUSTOMER.COUNTRY");
        list = ClientUtil.executeQuery("getSelectLookUpDesc", lookmap);
        if (list != null && list.size() > 0) {
            result = (HashMap) list.get(0);
        }
        if (result != null && result.containsKey("LOOKUP_DESC")) {
            txtCountry.setText(CommonUtil.convertObjToStr(result.get("LOOKUP_DESC")));
        }
        dataMap = null;
    }
}//GEN-LAST:event_cboAddressTypeActionPerformed

private void btnRemoveActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnRemoveActionPerformed
    // Add your handling code here:
    lblPicture.setIcon(null);
    photoFile = null;
    this.btnRemove.setEnabled(false);
}//GEN-LAST:event_btnRemoveActionPerformed

private void btnLoadActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnLoadActionPerformed
    // Add your handling code here:
    //press = true;
    JFileChooser objJFileChooser = new JFileChooser();
    if (objJFileChooser.showOpenDialog(this) == 0) {
        selFile = objJFileChooser.getSelectedFile();
        if (selFile != null) {
            fileName = new StringBuffer("file:\\");
            fileName.append(selFile.getAbsolutePath());
        }
        try {
            if (btnRemove.isEnabled()) {
                btnRemoveActionPerformed(null);
                photoByteArray = null;
            }
            lblPicture.setIcon(new javax.swing.ImageIcon(new java.net.URL(CommonUtil.convertObjToStr(fileName))));
            //photoFile = fileName.substring(fileName.lastIndexOf("."));
            readPhoto();
        } catch (Exception e) {
            log.info("Error Declaration in btnLoadActionPerformed:" + e);
        }
    }
    if (lblPicture.getIcon() != null) {
        this.btnRemove.setEnabled(true);
    }
}//GEN-LAST:event_btnLoadActionPerformed

private void btnCustomerIdActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCustomerIdActionPerformed
// TODO add your handling code here:
    viewType = CUSTOMER;
    new CheckCustomerIdUI(this);
}//GEN-LAST:event_btnCustomerIdActionPerformed

private void btnNetSalaryAccNoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnNetSalaryAccNoActionPerformed
// TODO add your handling code here:
    callView(ACCNO);
}//GEN-LAST:event_btnNetSalaryAccNoActionPerformed

private void cboDesignationActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cboDesignationActionPerformed
// TODO add your handling code here:
    final String DESG = CommonUtil.convertObjToStr(((ComboBoxModel) (cboDesignation.getModel())).getKeyForSelected());
    if (!DESG.equalsIgnoreCase("")) {
        try {
            HashMap dataMap = new HashMap();
            dataMap.put("DESIGNATION", DESG);
            final HashMap resultMap = observable.getScaleId(dataMap);
            txtScaleId.setText(observable.getTxtScaleId());
            txtVersionNo.setText(observable.getTxtVersionNo());
            txtScaleId.setEditable(false);
            txtVersionNo.setEditable(false);
        } catch (SQLException ex) {
            java.util.logging.Logger.getLogger(EmployeeUI.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}//GEN-LAST:event_cboDesignationActionPerformed

private void cboNetSalaryProductTypeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cboNetSalaryProductTypeActionPerformed
// TODO add your handling code here:
    final String TYPE = CommonUtil.convertObjToStr(((ComboBoxModel) (cboNetSalaryProductType.getModel())).getKeyForSelected());
    observable.setCbmProdId(TYPE);
    cboNetSalaryProductId.setModel(observable.getCbmNetSalaryProductId());
    btnNetSalaryAccNo.setEnabled(true);
}//GEN-LAST:event_cboNetSalaryProductTypeActionPerformed

private void tdtProbationEndDateFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_tdtProbationEndDateFocusLost
// TODO add your handling code here:
    ClientUtil.validateToDate(tdtProbationEndDate, tdtDateOfJoin.getDateValue());
}//GEN-LAST:event_tdtProbationEndDateFocusLost

private void tdtDateOfJoinFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_tdtDateOfJoinFocusLost
// TODO add your handling code here:
    ClientUtil.validateLessDate(tdtDateOfJoin, tdtEffectiveDate.getDateValue());
}//GEN-LAST:event_tdtDateOfJoinFocusLost

private void tdtDateOfRetirementFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_tdtDateOfRetirementFocusLost
// TODO add your handling code here:
    ClientUtil.validateToDate(tdtDateOfRetirement, tdtProbationEndDate.getDateValue());
}//GEN-LAST:event_tdtDateOfRetirementFocusLost

private void tdtLastIncrementDateFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_tdtLastIncrementDateFocusLost
// TODO add your handling code here:
    ClientUtil.validateToDate(tdtLastIncrementDate, tdtEffectiveDate.getDateValue());
}//GEN-LAST:event_tdtLastIncrementDateFocusLost

private void tdtEffectiveDateFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_tdtEffectiveDateFocusLost
// TODO add your handling code here:
}//GEN-LAST:event_tdtEffectiveDateFocusLost

private void tdtNextIncrementDateFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_tdtNextIncrementDateFocusLost
    // TODO add your handling code here: 
    ClientUtil.validateToDate(tdtNextIncrementDate, tdtLastIncrementDate.getDateValue());
}//GEN-LAST:event_tdtNextIncrementDateFocusLost

    private void txtIncrementCountFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtIncrementCountFocusLost
        // TODO add your handling code here:
        try {
            int scale = CommonUtil.convertObjToInt(txtScaleId.getText());
            int incrmtcnt = CommonUtil.convertObjToInt(txtIncrementCount.getText());
            HashMap dataMap1 = new HashMap();
            dataMap1.put("SCALE", scale);
            dataMap1.put("INCR", incrmtcnt);
            final HashMap resultMap1 = observable.getIncrementStagCount(dataMap1);
            int increment = CommonUtil.convertObjToInt(resultMap1.get("INCREAMENT_COUNT"));
            if (incrmtcnt > increment) {
                ClientUtil.showAlertWindow("Increment Count entered is greater than actual Increment Count for selected Scale ID");
                txtIncrementCount.setText("");
                return;
            }
            HashMap dataMap2 = new HashMap();
            dataMap2.put("SCALE", scale);
            final HashMap resultMap2 = observable.getPreBasicSal(dataMap2);
            HashMap dataMap3 = new HashMap();
            dataMap3.put("SCALE", scale);
            dataMap3.put("VERSION", resultMap2.get("VERSION_NO"));
            double startSal = CommonUtil.convertObjToDouble(resultMap2.get("SCALE_START_AMOUNT"));
            double endSal = CommonUtil.convertObjToDouble(resultMap2.get("SCALE_END_AMOUNT"));
            int c = 0;
            int i = incrmtcnt;
            double salary = 0;
            salary = startSal;
            final List scaleList = ClientUtil.executeQuery("getScaleDet", dataMap3);
            if (scaleList != null && scaleList.size() > 0) {
                for (int k = 0; k < scaleList.size(); k++) {
                    sal = new SalStructDetailsTO();
                    HashMap resultMap3 = (HashMap) scaleList.get(k);
                    sal.setIncCount(CommonUtil.convertObjToInt(resultMap3.get("INCREAMENT_COUNT")));
                    sal.setIncAmt(CommonUtil.convertObjToDouble(resultMap3.get("INCREAMENT_AMOUNT")));
                    salArray.add(sal);
                }

            }
            for (SalStructDetailsTO s : salArray) {
                c = s.getIncCount();
                while (c > 0 && (salary <= endSal) && i > 0) {
                    salary = salary + s.getIncAmt();
                    c--;
                    i--;
                }
            }
            txtPresentBasicSalary.setText(CommonUtil.convertObjToStr(salary));
        } catch (SQLException ex) {
            java.util.logging.Logger.getLogger(EmployeeUI.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_txtIncrementCountFocusLost

    private void cboDesignationFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_cboDesignationFocusLost
        // TODO add your handling code here:
        txtIncrementCount.setText("");
        txtPresentBasicSalary.setText("");
    }//GEN-LAST:event_cboDesignationFocusLost

    private void txtCustomerIdFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtCustomerIdFocusLost
        // TODO add your handling code here:
        HashMap executeMap = new HashMap();
        String cust_id = CommonUtil.convertObjToStr(txtCustomerId.getText());
        viewType = CUSTOMER;
        executeMap.put("BRANCH_CODE", getSelectedBranchID());
        executeMap.put("CUST_ID", new String(cust_id));
        fillData(executeMap);
    }//GEN-LAST:event_txtCustomerIdFocusLost

    private void cboNetSalaryProductIdFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_cboNetSalaryProductIdFocusLost
        // TODO add your handling code here:
        btnNetSalaryAccNo.setEnabled(true);
    }//GEN-LAST:event_cboNetSalaryProductIdFocusLost

    private void cboNetSalaryProductTypeFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_cboNetSalaryProductTypeFocusLost
        // TODO add your handling code here:
        txtNetSalaryAccNo.setText("");
        txtCustomerName.setText("");
    }//GEN-LAST:event_cboNetSalaryProductTypeFocusLost

    private void btnPrintActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnPrintActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_btnPrintActionPerformed

    private void cboDesignationMousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_cboDesignationMousePressed
        // TODO add your handling code here:
        txtIncrementCount.setText("");
        txtPresentBasicSalary.setText("");
    }//GEN-LAST:event_cboDesignationMousePressed

    private void callView(int currField) {
        HashMap viewMap = new HashMap();
        viewType = currField;
        if (currField == ACCNO) {
            this.txtNetSalaryAccNo.setText("");
            HashMap whereListMap = new HashMap();
            viewMap.put(CommonConstants.MAP_NAME, "Transfer.getAccountList" + ((ComboBoxModel) this.cboNetSalaryProductType.getModel()).getKeyForSelected());
            whereListMap.put("PROD_ID", ((ComboBoxModel) cboNetSalaryProductId.getModel()).getKeyForSelected());
            // for allowing interbranvh account commented by rishad ( bank need direct mapping without selecting select branch option)
//            whereListMap.put("SELECTED_BRANCH", TrueTransactMain.selBranch);
            viewMap.put(CommonConstants.MAP_WHERE, whereListMap);
        }
        new ViewAll(this, viewMap).show();
    }

    public Date setProperDateFormat(Object obj) {
        Date currDate = null;
        if (obj != null && obj.toString().length() > 0) {
            Date tempDt = DateUtil.getDateMMDDYYYY(CommonUtil.convertObjToStr(obj));
            currDate = (Date) currDt.clone();
            currDate.setDate(tempDt.getDate());
            currDate.setMonth(tempDt.getMonth());
            currDate.setYear(tempDt.getYear());
        }
        return currDate;
    }

    public static void main(java.lang.String[] args) {
        javax.swing.JFrame frm = new javax.swing.JFrame();
        EmployeeUI emp = new EmployeeUI();
        frm.getContentPane().add(emp);
        emp.show();
        emp.setSize(900, 900);
        emp.show();
    }
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private com.see.truetransact.uicomponent.CButton btnAuthorize;
    private com.see.truetransact.uicomponent.CButton btnCancel;
    private com.see.truetransact.uicomponent.CButton btnClose;
    private com.see.truetransact.uicomponent.CButton btnCustomerId;
    private com.see.truetransact.uicomponent.CButton btnDelete;
    private com.see.truetransact.uicomponent.CButton btnEdit;
    private com.see.truetransact.uicomponent.CButton btnException;
    private com.see.truetransact.uicomponent.CButton btnLoad;
    private com.see.truetransact.uicomponent.CButton btnNetSalaryAccNo;
    private com.see.truetransact.uicomponent.CButton btnNew;
    private com.see.truetransact.uicomponent.CButton btnPrint;
    private com.see.truetransact.uicomponent.CButton btnReject;
    private com.see.truetransact.uicomponent.CButton btnRemove;
    private com.see.truetransact.uicomponent.CButton btnSave;
    private com.see.truetransact.uicomponent.CButton btnView;
    private com.see.truetransact.uicomponent.CComboBox cboAddressType;
    private com.see.truetransact.uicomponent.CComboBox cboBloodGroup;
    private com.see.truetransact.uicomponent.CComboBox cboBranch;
    private com.see.truetransact.uicomponent.CComboBox cboCommAddressType;
    private com.see.truetransact.uicomponent.CComboBox cboDepartment;
    private com.see.truetransact.uicomponent.CComboBox cboDesignation;
    private com.see.truetransact.uicomponent.CComboBox cboEmployeeType;
    private com.see.truetransact.uicomponent.CComboBox cboFatherTitle;
    private com.see.truetransact.uicomponent.CComboBox cboJobTitle;
    private com.see.truetransact.uicomponent.CComboBox cboManager;
    private com.see.truetransact.uicomponent.CComboBox cboMotherTitle;
    private com.see.truetransact.uicomponent.CComboBox cboNetSalaryProductId;
    private com.see.truetransact.uicomponent.CComboBox cboNetSalaryProductType;
    private com.see.truetransact.uicomponent.CComboBox cboStatusOfEmp;
    private com.see.truetransact.uicomponent.CCheckBox chkDAApplicable;
    private com.see.truetransact.uicomponent.CCheckBox chkHRAApplicable;
    private com.see.truetransact.uicomponent.CCheckBox chkStopPayt;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JScrollPane jScrollPane7;
    private javax.swing.JScrollPane jScrollPane8;
    private com.see.truetransact.uicomponent.CLabel lblAddressType;
    private com.see.truetransact.uicomponent.CLabel lblArea;
    private com.see.truetransact.uicomponent.CLabel lblBirthDate;
    private com.see.truetransact.uicomponent.CLabel lblBloodGroup;
    private com.see.truetransact.uicomponent.CLabel lblBranch;
    private com.see.truetransact.uicomponent.CLabel lblCaste;
    private com.see.truetransact.uicomponent.CLabel lblCity;
    private com.see.truetransact.uicomponent.CLabel lblComments;
    private com.see.truetransact.uicomponent.CLabel lblCommunicationAddressType;
    private com.see.truetransact.uicomponent.CLabel lblCountry;
    private com.see.truetransact.uicomponent.CLabel lblCustomerId;
    private javax.swing.JLabel lblCustomerName;
    private javax.swing.JLabel lblDateOfJoin;
    private javax.swing.JLabel lblDateOfRetirement;
    private com.see.truetransact.uicomponent.CLabel lblDepartment;
    private javax.swing.JLabel lblDesignation;
    private com.see.truetransact.uicomponent.CLabel lblEducation;
    private javax.swing.JLabel lblEffectiveDate;
    private com.see.truetransact.uicomponent.CLabel lblEmployeeId;
    private com.see.truetransact.uicomponent.CLabel lblEmployeeType;
    private com.see.truetransact.uicomponent.CLabel lblExperience;
    private javax.swing.JLabel lblFatherName;
    private com.see.truetransact.uicomponent.CLabel lblFirstName;
    private com.see.truetransact.uicomponent.CLabel lblGender;
    private javax.swing.JLabel lblIdentificationMark1;
    private javax.swing.JLabel lblIdentificationMark2;
    private javax.swing.JLabel lblIncrementCount;
    private com.see.truetransact.uicomponent.CLabel lblJobTitle;
    private javax.swing.JLabel lblLastIncrementDate;
    private com.see.truetransact.uicomponent.CLabel lblLastName;
    private javax.swing.JLabel lblMajorHealthProblem;
    private com.see.truetransact.uicomponent.CLabel lblManager;
    private com.see.truetransact.uicomponent.CLabel lblMartialStatus;
    private com.see.truetransact.uicomponent.CLabel lblMiddleName;
    private javax.swing.JLabel lblMotherName;
    private com.see.truetransact.uicomponent.CLabel lblMsg;
    private javax.swing.JLabel lblNetSalaryAccNo;
    private javax.swing.JLabel lblNetSalaryProductId;
    private javax.swing.JLabel lblNetSalaryProductType;
    private javax.swing.JLabel lblNextIncrementDate;
    private com.see.truetransact.uicomponent.CLabel lblOfficePhone;
    private com.see.truetransact.uicomponent.CLabel lblOfficialEmail;
    private com.see.truetransact.uicomponent.CLabel lblPanNo;
    private com.see.truetransact.uicomponent.CLabel lblPassPortNo;
    private javax.swing.JLabel lblPensionCodeNo;
    private javax.swing.JLabel lblPensionOpeningBalance;
    private javax.swing.JLabel lblPensionOpeningBalanceOn;
    private com.see.truetransact.uicomponent.CLabel lblPerformance;
    private javax.swing.JLabel lblPhysicallyHandicapped;
    private com.see.truetransact.uicomponent.CLabel lblPicture;
    private com.see.truetransact.uicomponent.CLabel lblPinCode;
    private javax.swing.JLabel lblPlaceofBirth;
    private javax.swing.JLabel lblPresentBasicSalary;
    private javax.swing.JLabel lblProbationEndDate;
    private com.see.truetransact.uicomponent.CLabel lblReligion;
    private com.see.truetransact.uicomponent.CLabel lblResponsibility;
    private javax.swing.JLabel lblScaleId;
    private com.see.truetransact.uicomponent.CLabel lblSkills;
    private javax.swing.JLabel lblSortOrder;
    private com.see.truetransact.uicomponent.CLabel lblSpace1;
    private com.see.truetransact.uicomponent.CLabel lblSpace17;
    private com.see.truetransact.uicomponent.CLabel lblSpace18;
    private com.see.truetransact.uicomponent.CLabel lblSpace19;
    private com.see.truetransact.uicomponent.CLabel lblSpace2;
    private com.see.truetransact.uicomponent.CLabel lblSpace20;
    private com.see.truetransact.uicomponent.CLabel lblSpace21;
    private com.see.truetransact.uicomponent.CLabel lblSpace22;
    private com.see.truetransact.uicomponent.CLabel lblSpace3;
    private com.see.truetransact.uicomponent.CLabel lblSpace4;
    private com.see.truetransact.uicomponent.CLabel lblSpace5;
    private com.see.truetransact.uicomponent.CLabel lblSpaces;
    private javax.swing.JLabel lblSpouseName;
    private javax.swing.JLabel lblSpouseRelation;
    private com.see.truetransact.uicomponent.CLabel lblSsnNo;
    private com.see.truetransact.uicomponent.CLabel lblState;
    private com.see.truetransact.uicomponent.CLabel lblStatus1;
    private javax.swing.JLabel lblStatusOfEmp;
    private com.see.truetransact.uicomponent.CLabel lblStreet;
    private com.see.truetransact.uicomponent.CLabel lblTitle;
    private com.see.truetransact.uicomponent.CLabel lblVersionNo;
    private javax.swing.JLabel lblWfNumber;
    private javax.swing.JLabel lblWfOpeningBalance;
    private javax.swing.JLabel lblWfOpeningBalanceOn;
    private com.see.truetransact.uicomponent.CMenuBar mbrLoanProduct;
    private javax.swing.JMenuItem mitCancel;
    private javax.swing.JMenuItem mitClose;
    private javax.swing.JMenuItem mitDelete;
    private javax.swing.JMenuItem mitEdit;
    private javax.swing.JMenuItem mitNew;
    private javax.swing.JMenuItem mitPrint;
    private javax.swing.JMenuItem mitSave;
    private javax.swing.JMenu mnuProcess;
    private javax.swing.JPanel panAccData;
    private com.see.truetransact.uicomponent.CPanel panAddress;
    private javax.swing.JPanel panApplicable;
    private com.see.truetransact.uicomponent.CPanel panBranch1;
    private com.see.truetransact.uicomponent.CPanel panButton;
    private com.see.truetransact.uicomponent.CPanel panCustomerId;
    private javax.swing.JPanel panDesgDetails;
    private com.see.truetransact.uicomponent.CPanel panEmployee;
    private com.see.truetransact.uicomponent.CPanel panEmployeeData;
    private com.see.truetransact.uicomponent.CPanel panEmployeeDetails;
    private com.see.truetransact.uicomponent.CPanel panEmployeeInfo;
    private com.see.truetransact.uicomponent.CPanel panEmploymentData;
    private com.see.truetransact.uicomponent.CPanel panGender;
    private com.see.truetransact.uicomponent.CPanel panGeneraInfo;
    private com.see.truetransact.uicomponent.CPanel panJobDetails;
    private com.see.truetransact.uicomponent.CPanel panOtherDetails;
    private javax.swing.JPanel panPensionData;
    private com.see.truetransact.uicomponent.CPanel panPerformance;
    private com.see.truetransact.uicomponent.CPanel panPersonalNumbers;
    private com.see.truetransact.uicomponent.CPanel panPhysicalDetails;
    private com.see.truetransact.uicomponent.CPanel panPicture;
    private com.see.truetransact.uicomponent.CPanel panSalaryData;
    private com.see.truetransact.uicomponent.CPanel panSalaryDetails;
    private com.see.truetransact.uicomponent.CPanel panStatus;
    private com.see.truetransact.uicomponent.CButtonGroup rdoGender;
    private com.see.truetransact.uicomponent.CRadioButton rdoGender_Female;
    private com.see.truetransact.uicomponent.CRadioButton rdoGender_Male;
    private javax.swing.JSeparator sptCancel;
    private javax.swing.JSeparator sptPrint;
    private javax.swing.JSeparator sptProcess;
    private com.see.truetransact.uicomponent.CSeparator sptSkills_Hori;
    private com.see.truetransact.uicomponent.CSeparator sptSkills_Vert1;
    private com.see.truetransact.uicomponent.CSeparator sptSkills_Vert2;
    private com.see.truetransact.uicomponent.CSeparator sptSkills_Vert3;
    private com.see.truetransact.uicomponent.CScrollPane srpComments;
    private com.see.truetransact.uicomponent.CScrollPane srpEducation;
    private com.see.truetransact.uicomponent.CScrollPane srpExperience;
    private com.see.truetransact.uicomponent.CScrollPane srpPerformance;
    private com.see.truetransact.uicomponent.CScrollPane srpPicture;
    private com.see.truetransact.uicomponent.CScrollPane srpResponsibility;
    private com.see.truetransact.uicomponent.CScrollPane srpResponsibility3;
    private com.see.truetransact.uicomponent.CTabbedPane tabEmployee;
    private javax.swing.JToolBar tbrLoantProduct;
    private com.see.truetransact.uicomponent.CDateField tdtBirthDate;
    private com.see.truetransact.uicomponent.CDateField tdtDateOfJoin;
    private com.see.truetransact.uicomponent.CDateField tdtDateOfRetirement;
    private com.see.truetransact.uicomponent.CDateField tdtEffectiveDate;
    private com.see.truetransact.uicomponent.CDateField tdtLastIncrementDate;
    private com.see.truetransact.uicomponent.CDateField tdtNextIncrementDate;
    private com.see.truetransact.uicomponent.CDateField tdtPensionOpeningBalanceOn;
    private com.see.truetransact.uicomponent.CDateField tdtProbationEndDate;
    private com.see.truetransact.uicomponent.CDateField tdtWfOpeningBalanceOn;
    private com.see.truetransact.uicomponent.CTextArea txaComments;
    private com.see.truetransact.uicomponent.CTextArea txaEducation;
    private com.see.truetransact.uicomponent.CTextArea txaExperience;
    private com.see.truetransact.uicomponent.CTextArea txaIdentificationMark1;
    private com.see.truetransact.uicomponent.CTextArea txaIdentificationMark2;
    private com.see.truetransact.uicomponent.CTextArea txaMajorHealthProblem;
    private com.see.truetransact.uicomponent.CTextArea txaPerformance;
    private com.see.truetransact.uicomponent.CTextArea txaPhysicallyHandicapped;
    private com.see.truetransact.uicomponent.CTextArea txaResponsibility;
    private com.see.truetransact.uicomponent.CTextArea txaSkills;
    private com.see.truetransact.uicomponent.CLabel txtArea;
    private com.see.truetransact.uicomponent.CLabel txtCaste;
    private com.see.truetransact.uicomponent.CLabel txtCity;
    private com.see.truetransact.uicomponent.CLabel txtCountry;
    private com.see.truetransact.uicomponent.CTextField txtCustomerId;
    private com.see.truetransact.uicomponent.CLabel txtCustomerName;
    private com.see.truetransact.uicomponent.CTextField txtEmployeeId;
    private com.see.truetransact.uicomponent.CTextField txtFatherName;
    private com.see.truetransact.uicomponent.CLabel txtFirstName;
    private com.see.truetransact.uicomponent.CTextField txtIncrementCount;
    private com.see.truetransact.uicomponent.CLabel txtLastName;
    private com.see.truetransact.uicomponent.CLabel txtMartialStatus;
    private com.see.truetransact.uicomponent.CLabel txtMiddleName;
    private com.see.truetransact.uicomponent.CTextField txtMotherName;
    private com.see.truetransact.uicomponent.CLabel txtNetSalaryAccNo;
    private com.see.truetransact.uicomponent.CTextField txtOfficePhone;
    private com.see.truetransact.uicomponent.CLabel txtOfficialEmail;
    private com.see.truetransact.uicomponent.CTextField txtPanNo;
    private com.see.truetransact.uicomponent.CTextField txtPassPortNo;
    private com.see.truetransact.uicomponent.CTextField txtPensionCodeNo;
    private com.see.truetransact.uicomponent.CTextField txtPensionOpeningBalance;
    private com.see.truetransact.uicomponent.CLabel txtPinCode;
    private com.see.truetransact.uicomponent.CTextField txtPlaceofBirth;
    private com.see.truetransact.uicomponent.CTextField txtPresentBasicSalary;
    private com.see.truetransact.uicomponent.CLabel txtReligion;
    private com.see.truetransact.uicomponent.CTextField txtScaleId;
    private com.see.truetransact.uicomponent.CTextField txtSortOrder;
    private com.see.truetransact.uicomponent.CTextField txtSpouseName;
    private com.see.truetransact.uicomponent.CTextField txtSpouseRelation;
    private com.see.truetransact.uicomponent.CTextField txtSsnNo;
    private com.see.truetransact.uicomponent.CLabel txtState;
    private com.see.truetransact.uicomponent.CLabel txtStreet;
    private com.see.truetransact.uicomponent.CLabel txtTitle;
    private com.see.truetransact.uicomponent.CTextField txtVersionNo;
    private com.see.truetransact.uicomponent.CTextField txtWfNumber;
    private com.see.truetransact.uicomponent.CTextField txtWfOpeningBalance;
    // End of variables declaration//GEN-END:variables
}
