/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition. 
 * 
 * EmployeeUI.java
 *
 * Created on February 12, 2004, 3:17 PM
 */

package com.see.truetransact.ui.sysadmin.employee;

import com.see.truetransact.uicomponent.CInternalFrame;
import com.see.truetransact.uimandatory.UIMandatoryField;
import com.see.truetransact.uimandatory.MandatoryCheck;
import com.see.truetransact.commonutil.CommonConstants;
import com.see.truetransact.commonutil.DateUtil;
import com.see.truetransact.clientutil.ComboBoxModel;
import com.see.truetransact.clientutil.ClientUtil;
import com.see.truetransact.commonutil.CommonUtil;
import com.see.truetransact.clientutil.ClientConstants;
import com.see.truetransact.clientutil.ComboBoxModel;
import com.see.truetransact.clientutil.CMandatoryDialog;
import com.see.truetransact.ui.common.viewall.ViewAll;
import com.see.truetransact.uivalidation.EmailValidation;
import com.see.truetransact.uivalidation.PincodeValidation_IN;
import com.see.truetransact.ui.TrueTransactMain;
import com.see.truetransact.uicomponent.CButtonGroup;// To add and Remove the Radio Buttons...
import com.see.truetransact.ui.common.authorizestatus.AuthorizeStatusUI;

import java.util.HashMap;
import java.util.Observer;
import java.util.Observable;
import java.util.List;
import java.io.File;
import javax.swing.JFileChooser;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.ArrayList;
import org.apache.log4j.Logger;
import java.util.Date;
/**
 *
 * @author  rahul
 *  @modified : Sunil
 *      Added Edit Locking - 07-07-2005
 */
public class EmployeeUI extends CInternalFrame implements java.util.Observer , UIMandatoryField{
    HashMap mandatoryMap;
    EmployeeOB observable;
    final int EDIT=0, DELETE=1, BRANCH=2, VIEW =4;
    final int AUTHORIZE=3;
    boolean isFilled = false;
    int viewType=-1;
    //Data Fields for the Photo/Picture...
    private File selFile;
    private byte[] photoByteArray;
    private StringBuffer fileName;
    private String photoFile;
    private String TITLE_MR = "Mr";
    private String TITLE_MRS = "Mrs";
    private String TITLE_MS = "Ms";
    private String MARTIAL_STATUS_SINGLE = "Single";
    private String MARTIAL_STATUS_MARRIED = "Married";
    private Date currDt = null;
    //Logger
    private final static Logger log = Logger.getLogger(EmployeeUI.class);
    
    /** Creates new form EmployeeUI */
    public EmployeeUI() {
        currDt = ClientUtil.getCurrentDate();
        initComponents();
        initSetup();
    }
    
    private void initSetup(){
        setFieldNames();
        internationalize();
        setMandatoryHashMap();
        setObservable();
        initComponentData();
        setMaxLenths();// To set the Numeric Validation and the Maximum length of the Text fields...
        setPictuteEnableDisable(false);//To Enable the Load Button for Picture...
        this.btnRemove.setEnabled(false);
        lblPicture.setIcon(null);
        new MandatoryCheck().putMandatoryMarks(getClass().getName(),panEmployee);
        observable.resetStatus();// to reset the status
        ClientUtil.enableDisable(this, false);// Disables all when the screen appears for the 1st time
        setButtonEnableDisable();// Enables/Disables the necessary buttons and menu items...
        enableDisableButtons();// To disable the buttons(folder) in the Starting...
        
        observable.resetForm();// To reset all the fields in UI...
        cboManager.setVisible(false);
        lblManager.setVisible(false);
        setHelpMessage();
    }
    
    private void setObservable() {
        observable = EmployeeOB.getInstance();
        observable.addObserver(this);
    }
    
    private void setFieldNames() {
        btnBranchCode.setName("btnBranchCode");
        btnCancel.setName("btnCancel");
        btnClose.setName("btnClose");
        btnDelete.setName("btnDelete");
        btnEdit.setName("btnEdit");
        btnLoad.setName("btnLoad");
        btnNew.setName("btnNew");
        btnPrint.setName("btnPrint");
        btnRemove.setName("btnRemove");
        btnSave.setName("btnSave");
        cboCity.setName("cboCity");
        cboCountry.setName("cboCountry");
        cboDepartment.setName("cboDepartment");
        cboEmployeeType.setName("cboEmployeeType");
        cboMartialStatus.setName("cboMartialStatus");
        cboJobTitle.setName("cboJobTitle");
        cboManager.setName("cboManager");
        cboState.setName("cboState");
        cboTitle.setName("cboTitle");
        lblAlternateEmail.setName("lblAlternateEmail");
        lblArea.setName("lblArea");
        lblBirthDate.setName("lblBirthDate");
        lblBranchCode.setName("lblBranchCode");
        lblCellular.setName("lblCellular");
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
        lblHomePhone.setName("lblHomePhone");
        lblJobTitle.setName("lblJobTitle");
        lblJoiningDate.setName("lblJoiningDate");
        lblMartialStatus.setName("lblMartialStatus");
        lblLastName.setName("lblLastName");
        lblLeavingDate.setName("lblLeavingDate");
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
        lblWeddindDate.setName("lblWeddindDate");
        mbrLoanProduct.setName("mbrLoanProduct");
        panAddress.setName("panAddress");
        panButton.setName("panButton");
        panDates.setName("panDates");
        panDescriptions.setName("panDescriptions");
        panEmployee.setName("panEmployee");
        panEmployeeData.setName("panEmployeeData");
        panEmployeeDetails.setName("panEmployeeDetails");
        panEmployeeInfo.setName("panEmployeeInfo");
        panGender.setName("panGender");
        panGeneraInfol.setName("panGeneraInfol");
        panPerformance.setName("panPerformance");
        panPersonalNumbers.setName("panPersonalNumbers");
        panPicture.setName("panPicture");
        panSkills.setName("panSkills");
        panStatus.setName("panStatus");
        rdoGender_Female.setName("rdoGender_Female");
        rdoGender_Male.setName("rdoGender_Male");
        sptSkills_Hori.setName("sptSkills_Hori");
        sptSkills_Vert.setName("sptSkills_Vert");
        srpComments.setName("srpComments");
        srpEducation.setName("srpEducation");
        srpExperience.setName("srpExperience");
        srpPerformance.setName("srpPerformance");
        srpPicture.setName("srpPicture");
        srpResponsibility.setName("srpResponsibility");
        srpSkills.setName("srpSkills");
        tabEmployee.setName("tabEmployee");
        tdtBirthDate.setName("tdtBirthDate");
        tdtJoiningDate.setName("tdtJoiningDate");
        tdtLeavingDate.setName("tdtLeavingDate");
        tdtWeddindDate.setName("tdtWeddindDate");
        txaComments.setName("txaComments");
        txaEducation.setName("txaEducation");
        txaExperience.setName("txaExperience");
        txaPerformance.setName("txaPerformance");
        txaResponsibility.setName("txaResponsibility");
        txaSkills.setName("txaSkills");
        txtAlternateEmail.setName("txtAlternateEmail");
        txtArea.setName("txtArea");
        txtBranchCode.setName("txtBranchCode");
        txtCellular.setName("txtCellular");
        txtEmployeeId.setName("txtEmployeeId");
        txtFirstName.setName("txtFirstName");
        txtHomePhone.setName("txtHomePhone");
        txtLastName.setName("txtLastName");
        txtOfficePhone.setName("txtOfficePhone");
        txtOfficialEmail.setName("txtOfficialEmail");
        txtPanNo.setName("txtPanNo");
        txtPassPortNo.setName("txtPassPortNo");
        txtPinCode.setName("txtPinCode");
        txtSsnNo.setName("txtSsnNo");
        txtStreet.setName("txtStreet");
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
        ((javax.swing.border.TitledBorder)panPicture.getBorder()).setTitle(resourceBundle.getString("panPicture"));
        lblLastName.setText(resourceBundle.getString("lblLastName"));
        btnPrint.setText(resourceBundle.getString("btnPrint"));
        lblAlternateEmail.setText(resourceBundle.getString("lblAlternateEmail"));
        lblStreet.setText(resourceBundle.getString("lblStreet"));
        lblCity.setText(resourceBundle.getString("lblCity"));
        lblHomePhone.setText(resourceBundle.getString("lblHomePhone"));
        lblStatus1.setText(resourceBundle.getString("lblStatus1"));
        lblBranchCode.setText(resourceBundle.getString("lblBranchCode"));
        lblPanNo.setText(resourceBundle.getString("lblPanNo"));
        rdoGender_Female.setText(resourceBundle.getString("rdoGender_Female"));
        btnSave.setText(resourceBundle.getString("btnSave"));
        lblTitle.setText(resourceBundle.getString("lblTitle"));
        lblJoiningDate.setText(resourceBundle.getString("lblJoiningDate"));
        btnBranchCode.setText(resourceBundle.getString("btnBranchCode"));
        lblOfficialEmail.setText(resourceBundle.getString("lblOfficialEmail"));
        lblArea.setText(resourceBundle.getString("lblArea"));
        lblPinCode.setText(resourceBundle.getString("lblPinCode"));
        btnLoad.setText(resourceBundle.getString("btnLoad"));
        btnDelete.setText(resourceBundle.getString("btnDelete"));
        lblComments.setText(resourceBundle.getString("lblComments"));
        lblCellular.setText(resourceBundle.getString("lblCellular"));
        ((javax.swing.border.TitledBorder)panAddress.getBorder()).setTitle(resourceBundle.getString("panAddress"));
        btnNew.setText(resourceBundle.getString("btnNew"));
        lblSsnNo.setText(resourceBundle.getString("lblSsnNo"));
        lblLeavingDate.setText(resourceBundle.getString("lblLeavingDate"));
        lblWeddindDate.setText(resourceBundle.getString("lblWeddindDate"));
        btnCancel.setText(resourceBundle.getString("btnCancel"));
        lblState.setText(resourceBundle.getString("lblState"));
        lblJobTitle.setText(resourceBundle.getString("lblJobTitle"));
        lblEmployeeType.setText(resourceBundle.getString("lblEmployeeType"));
        lblFirstName.setText(resourceBundle.getString("lblFirstName"));
    }
    
    public void setMandatoryHashMap() {
        mandatoryMap = new HashMap();
        mandatoryMap.put("txtEmployeeId", new Boolean(true));
        mandatoryMap.put("txtBranchCode", new Boolean(true));
        mandatoryMap.put("cboTitle", new Boolean(true));
        mandatoryMap.put("txtFirstName", new Boolean(true));
        mandatoryMap.put("txtLastName", new Boolean(true));
        mandatoryMap.put("rdoGender_Male", new Boolean(true));
        mandatoryMap.put("cboEmployeeType", new Boolean(true));
        mandatoryMap.put("cboMartialStatus", new Boolean(true));
        mandatoryMap.put("cboJobTitle", new Boolean(true));
        mandatoryMap.put("cboDepartment", new Boolean(true));
        mandatoryMap.put("cboManager", new Boolean(true));
        mandatoryMap.put("txtOfficialEmail", new Boolean(true));
        mandatoryMap.put("txtAlternateEmail", new Boolean(true));
        mandatoryMap.put("txtOfficePhone", new Boolean(true));
        mandatoryMap.put("txtHomePhone", new Boolean(true));
        mandatoryMap.put("txtCellular", new Boolean(true));
        mandatoryMap.put("txtStreet", new Boolean(true));
        mandatoryMap.put("txtArea", new Boolean(true));
        mandatoryMap.put("cboCity", new Boolean(true));
        mandatoryMap.put("cboState", new Boolean(true));
        mandatoryMap.put("cboCountry", new Boolean(true));
        mandatoryMap.put("txtPinCode", new Boolean(true));
        mandatoryMap.put("tdtBirthDate", new Boolean(true));
        mandatoryMap.put("tdtWeddindDate", new Boolean(true));
        mandatoryMap.put("tdtJoiningDate", new Boolean(true));
        mandatoryMap.put("tdtLeavingDate", new Boolean(true));
        mandatoryMap.put("txtPanNo", new Boolean(true));
        mandatoryMap.put("txtSsnNo", new Boolean(true));
        mandatoryMap.put("txtPassPortNo", new Boolean(true));
        mandatoryMap.put("txaSkills", new Boolean(true));
        mandatoryMap.put("txaEducation", new Boolean(true));
        mandatoryMap.put("txaExperience", new Boolean(true));
        mandatoryMap.put("txaResponsibility", new Boolean(true));
        mandatoryMap.put("txaPerformance", new Boolean(true));
        mandatoryMap.put("txaComments", new Boolean(true));
    }
    
    public HashMap getMandatoryHashMap() {
        return mandatoryMap;
    }
    
     // To Reset the Radio Buttons in the UI after any operation, We've to 
    //1. Remove the Radio Buttons fron the Radio Groups...
    //2. Add the Radio Buttons Back in The Radio Groups...
    //a.) To Remove the Radio buttons...
    private void removeRadioButtons() {
         //---Account---
         rdoGender.remove(rdoGender_Female);
         rdoGender.remove(rdoGender_Male);
    }
    
    // b.) To Add the Radio buttons...
     private void addRadioButtons() {
         //---Account---
         rdoGender = new CButtonGroup();
         rdoGender.add(rdoGender_Female);
         rdoGender.add(rdoGender_Male);
     }
         
    
    public void update(Observable observed, Object arg) {
        removeRadioButtons();
        
        txtEmployeeId.setText(observable.getTxtEmployeeId());
        txtBranchCode.setText(observable.getTxtBranchCode());
        cboTitle.setSelectedItem(observable.getCboTitle());
        txtFirstName.setText(observable.getTxtFirstName());
        txtLastName.setText(observable.getTxtLastName());
        rdoGender_Male.setSelected(observable.getRdoGender_Male());
        rdoGender_Female.setSelected(observable.getRdoGender_Female());
        cboEmployeeType.setSelectedItem(observable.getCboEmployeeType());
        cboMartialStatus.setSelectedItem(observable.getCboMartialStatus());
        cboJobTitle.setSelectedItem(observable.getCboJobTitle());
        cboDepartment.setSelectedItem(observable.getCboDepartment());
        cboManager.setSelectedItem(observable.getCboManager());
        txtOfficialEmail.setText(observable.getTxtOfficialEmail());
        txtAlternateEmail.setText(observable.getTxtAlternateEmail());
        txtOfficePhone.setText(observable.getTxtOfficePhone());
        txtHomePhone.setText(observable.getTxtHomePhone());
        txtCellular.setText(observable.getTxtCellular());
        txtStreet.setText(observable.getTxtStreet());
        txtArea.setText(observable.getTxtArea());
        cboCity.setSelectedItem(observable.getCboCity());
        cboState.setSelectedItem(observable.getCboState());
        cboCountry.setSelectedItem(observable.getCboCountry());
        txtPinCode.setText(observable.getTxtPinCode());
        tdtBirthDate.setDateValue(observable.getTdtBirthDate());
        tdtWeddindDate.setDateValue(observable.getTdtWeddindDate());
        tdtJoiningDate.setDateValue(observable.getTdtJoiningDate());
        tdtLeavingDate.setDateValue(observable.getTdtLeavingDate());
        txtPanNo.setText(observable.getTxtPanNo());
        txtSsnNo.setText(observable.getTxtSsnNo());
        txtPassPortNo.setText(observable.getTxtPassPortNo());
        txaSkills.setText(observable.getTxaSkills());
        txaEducation.setText(observable.getTxaEducation());
        txaExperience.setText(observable.getTxaExperience());
        txaResponsibility.setText(observable.getTxaResponsibility());
        txaPerformance.setText(observable.getTxaPerformance());
        txaComments.setText(observable.getTxaComments());
        lblValBranchName.setText(observable.getLblValBranchName());
        lblStatus1.setText(observable.getLblStatus());
        
        try{
            if (observable.getLblPhoto() != null&& observable.getLblPhoto().length() > 0){
                lblPicture.setIcon(new javax.swing.ImageIcon(new java.net.URL(observable.getLblPhoto())));
            }
        }catch(Exception e){
            log.info("Exception in Picture update");
        }
        
        addRadioButtons();
    }
    
    public void updateOBFields() {
        observable.setTxtEmployeeId(txtEmployeeId.getText());
        observable.setTxtBranchCode(txtBranchCode.getText());
        observable.setCboTitle((String) cboTitle.getSelectedItem());
        observable.setTxtFirstName(txtFirstName.getText());
        observable.setTxtLastName(txtLastName.getText());
        observable.setRdoGender_Male(rdoGender_Male.isSelected());
        observable.setRdoGender_Female(rdoGender_Female.isSelected());
        observable.setCboEmployeeType((String) cboEmployeeType.getSelectedItem());
        observable.setCboMartialStatus((String) cboMartialStatus.getSelectedItem());
        observable.setCboJobTitle((String) cboJobTitle.getSelectedItem());
        observable.setCboDepartment((String) cboDepartment.getSelectedItem());
        observable.setCboManager((String) cboManager.getSelectedItem());
        observable.setTxtOfficialEmail(txtOfficialEmail.getText());
        observable.setTxtAlternateEmail(txtAlternateEmail.getText());
        observable.setTxtOfficePhone(txtOfficePhone.getText());
        observable.setTxtHomePhone(txtHomePhone.getText());
        observable.setTxtCellular(txtCellular.getText());
        observable.setTxtStreet(txtStreet.getText());
        observable.setTxtArea(txtArea.getText());
        observable.setCboCity((String) cboCity.getSelectedItem());
        observable.setCboState((String)cboState.getSelectedItem());
        observable.setCboCountry((String) cboCountry.getSelectedItem());
        observable.setTxtPinCode(txtPinCode.getText());
        observable.setTdtBirthDate(tdtBirthDate.getDateValue());
        observable.setTdtWeddindDate(tdtWeddindDate.getDateValue());
        observable.setTdtJoiningDate(tdtJoiningDate.getDateValue());
        observable.setTdtLeavingDate(tdtLeavingDate.getDateValue());
        observable.setTxtPanNo(txtPanNo.getText());
        observable.setTxtSsnNo(txtSsnNo.getText());
        observable.setTxtPassPortNo(txtPassPortNo.getText());
        observable.setTxaSkills(txaSkills.getText());
        observable.setTxaEducation(txaEducation.getText());
        observable.setTxaExperience(txaExperience.getText());
        observable.setTxaResponsibility(txaResponsibility.getText());
        observable.setTxaPerformance(txaPerformance.getText());
        observable.setTxaComments(txaComments.getText());
        observable.setLblValBranchName(lblValBranchName.getText());
        observable.setPhotoFile(photoFile);
        observable.setPhotoByteArray(photoByteArray);
        if (fileName != null && fileName.length() > 0 ){
            observable.setLblPhoto(fileName.toString());
        }
    }
    
    public void setHelpMessage() {
        EmployeeMRB objMandatoryRB = new EmployeeMRB();
        txtEmployeeId.setHelpMessage(lblMsg, objMandatoryRB.getString("txtEmployeeId"));
        txtBranchCode.setHelpMessage(lblMsg, objMandatoryRB.getString("txtBranchCode"));
        cboTitle.setHelpMessage(lblMsg, objMandatoryRB.getString("cboTitle"));
        txtFirstName.setHelpMessage(lblMsg, objMandatoryRB.getString("txtFirstName"));
        txtLastName.setHelpMessage(lblMsg, objMandatoryRB.getString("txtLastName"));
        rdoGender_Male.setHelpMessage(lblMsg, objMandatoryRB.getString("rdoGender_Male"));
        cboEmployeeType.setHelpMessage(lblMsg, objMandatoryRB.getString("cboEmployeeType"));
        cboMartialStatus.setHelpMessage(lblMsg, objMandatoryRB.getString("cboMartialStatus"));
        cboJobTitle.setHelpMessage(lblMsg, objMandatoryRB.getString("cboJobTitle"));
        cboDepartment.setHelpMessage(lblMsg, objMandatoryRB.getString("cboDepartment"));
        cboManager.setHelpMessage(lblMsg, objMandatoryRB.getString("cboManager"));
        txtOfficialEmail.setHelpMessage(lblMsg, objMandatoryRB.getString("txtOfficialEmail"));
        txtAlternateEmail.setHelpMessage(lblMsg, objMandatoryRB.getString("txtAlternateEmail"));
        txtOfficePhone.setHelpMessage(lblMsg, objMandatoryRB.getString("txtOfficePhone"));
        txtHomePhone.setHelpMessage(lblMsg, objMandatoryRB.getString("txtHomePhone"));
        txtCellular.setHelpMessage(lblMsg, objMandatoryRB.getString("txtCellular"));
        txtStreet.setHelpMessage(lblMsg, objMandatoryRB.getString("txtStreet"));
        txtArea.setHelpMessage(lblMsg, objMandatoryRB.getString("txtArea"));
        cboCity.setHelpMessage(lblMsg, objMandatoryRB.getString("cboCity"));
        cboState.setHelpMessage(lblMsg, objMandatoryRB.getString("cboState"));
        cboCountry.setHelpMessage(lblMsg, objMandatoryRB.getString("cboCountry"));
        txtPinCode.setHelpMessage(lblMsg, objMandatoryRB.getString("txtPinCode"));
        tdtBirthDate.setHelpMessage(lblMsg, objMandatoryRB.getString("tdtBirthDate"));
        tdtWeddindDate.setHelpMessage(lblMsg, objMandatoryRB.getString("tdtWeddindDate"));
        tdtJoiningDate.setHelpMessage(lblMsg, objMandatoryRB.getString("tdtJoiningDate"));
        tdtLeavingDate.setHelpMessage(lblMsg, objMandatoryRB.getString("tdtLeavingDate"));
        txtPanNo.setHelpMessage(lblMsg, objMandatoryRB.getString("txtPanNo"));
        txtSsnNo.setHelpMessage(lblMsg, objMandatoryRB.getString("txtSsnNo"));
        txtPassPortNo.setHelpMessage(lblMsg, objMandatoryRB.getString("txtPassPortNo"));
        txaSkills.setHelpMessage(lblMsg, objMandatoryRB.getString("txaSkills"));
        txaEducation.setHelpMessage(lblMsg, objMandatoryRB.getString("txaEducation"));
        txaExperience.setHelpMessage(lblMsg, objMandatoryRB.getString("txaExperience"));
        txaResponsibility.setHelpMessage(lblMsg, objMandatoryRB.getString("txaResponsibility"));
        txaPerformance.setHelpMessage(lblMsg, objMandatoryRB.getString("txaPerformance"));
        txaComments.setHelpMessage(lblMsg, objMandatoryRB.getString("txaComments"));
    }
    
    // To fill the Data into the Combo Boxes...
    // it invokes the Combo Box model defined in OB class...
    private void initComponentData() {
        cboTitle.setModel(observable.getCbmTitle());
        cboEmployeeType.setModel(observable.getCbmEmployeeType());
        cboMartialStatus.setModel(observable.getCbmMartialStatus());
        cboCity.setModel(observable.getCbmCity());
        cboState.setModel(observable.getCbmState());
        cboCountry.setModel(observable.getCbmCountry());
        cboJobTitle.setModel(observable.getCbmJobTitle());
        cboDepartment.setModel(observable.getCbmDepartment());
        cboManager.setModel(observable.getCbmManager());
    }
    
    //To set the length of the Text Fields and Numeric Validation...
    private void setMaxLenths() {
        
        txtBranchCode.setMaxLength(8);
        txtEmployeeId.setMaxLength(8);
        txtLastName.setMaxLength(32);
        txtFirstName.setMaxLength(32);
        txtOfficialEmail.setMaxLength(32);
        txtOfficialEmail.setValidation(new EmailValidation());         
        txtAlternateEmail.setMaxLength(32);
        txtAlternateEmail.setValidation(new EmailValidation());
        txtOfficePhone.setMaxLength(32);
        txtOfficePhone.setAllowNumber(true);
        txtHomePhone.setMaxLength(32);
        txtHomePhone.setAllowNumber(true);
        txtCellular.setMaxLength(32);
        txtCellular.setAllowNumber(true);
        txtPanNo.setMaxLength(16);
        txtSsnNo.setMaxLength(16);
        txtPassPortNo.setMaxLength(16);
        //txaSkills.setMaxLength(1024);
        //txaEducation.setMaxLength(1024);
        //txaExperience.setMaxLength(1024);
        //txtphotoFile.setMaxLength(128);
        //txaResponsibility.setMaxLength(1024);
        //txaPerformance.setMaxLength(1024);
        //txaComments.setMaxLength(1024);
                
        txtStreet.setMaxLength(256);
        txtArea.setMaxLength(128);
       
        txtPinCode.setValidation(new PincodeValidation_IN());
        txtPinCode.setMaxLength(16);
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
        /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    private void initComponents() {//GEN-BEGIN:initComponents
        java.awt.GridBagConstraints gridBagConstraints;

        rdoGender = new com.see.truetransact.uicomponent.CButtonGroup();
        panEmployee = new com.see.truetransact.uicomponent.CPanel();
        lblSpaces = new com.see.truetransact.uicomponent.CLabel();
        tabEmployee = new com.see.truetransact.uicomponent.CTabbedPane();
        panEmployeeDetails = new com.see.truetransact.uicomponent.CPanel();
        panEmployeeData = new com.see.truetransact.uicomponent.CPanel();
        lblEmployeeId = new com.see.truetransact.uicomponent.CLabel();
        txtEmployeeId = new com.see.truetransact.uicomponent.CTextField();
        lblBranchCode = new com.see.truetransact.uicomponent.CLabel();
        lblTitle = new com.see.truetransact.uicomponent.CLabel();
        cboTitle = new com.see.truetransact.uicomponent.CComboBox();
        lblFirstName = new com.see.truetransact.uicomponent.CLabel();
        txtFirstName = new com.see.truetransact.uicomponent.CTextField();
        lblLastName = new com.see.truetransact.uicomponent.CLabel();
        txtLastName = new com.see.truetransact.uicomponent.CTextField();
        lblGender = new com.see.truetransact.uicomponent.CLabel();
        panGender = new com.see.truetransact.uicomponent.CPanel();
        rdoGender_Male = new com.see.truetransact.uicomponent.CRadioButton();
        rdoGender_Female = new com.see.truetransact.uicomponent.CRadioButton();
        lblMartialStatus = new com.see.truetransact.uicomponent.CLabel();
        lblEmployeeType = new com.see.truetransact.uicomponent.CLabel();
        cboEmployeeType = new com.see.truetransact.uicomponent.CComboBox();
        lblJobTitle = new com.see.truetransact.uicomponent.CLabel();
        cboJobTitle = new com.see.truetransact.uicomponent.CComboBox();
        lblDepartment = new com.see.truetransact.uicomponent.CLabel();
        cboDepartment = new com.see.truetransact.uicomponent.CComboBox();
        lblManager = new com.see.truetransact.uicomponent.CLabel();
        cboManager = new com.see.truetransact.uicomponent.CComboBox();
        lblOfficialEmail = new com.see.truetransact.uicomponent.CLabel();
        txtOfficialEmail = new com.see.truetransact.uicomponent.CTextField();
        lblAlternateEmail = new com.see.truetransact.uicomponent.CLabel();
        txtAlternateEmail = new com.see.truetransact.uicomponent.CTextField();
        lblOfficePhone = new com.see.truetransact.uicomponent.CLabel();
        txtOfficePhone = new com.see.truetransact.uicomponent.CTextField();
        lblHomePhone = new com.see.truetransact.uicomponent.CLabel();
        txtHomePhone = new com.see.truetransact.uicomponent.CTextField();
        lblCellular = new com.see.truetransact.uicomponent.CLabel();
        txtCellular = new com.see.truetransact.uicomponent.CTextField();
        lblBranchName = new com.see.truetransact.uicomponent.CLabel();
        lblValBranchName = new com.see.truetransact.uicomponent.CLabel();
        panBranch = new com.see.truetransact.uicomponent.CPanel();
        txtBranchCode = new com.see.truetransact.uicomponent.CTextField();
        btnBranchCode = new com.see.truetransact.uicomponent.CButton();
        cboMartialStatus = new com.see.truetransact.uicomponent.CComboBox();
        panEmployeeInfo = new com.see.truetransact.uicomponent.CPanel();
        panPicture = new com.see.truetransact.uicomponent.CPanel();
        srpPicture = new com.see.truetransact.uicomponent.CScrollPane();
        lblPicture = new com.see.truetransact.uicomponent.CLabel();
        panButton = new com.see.truetransact.uicomponent.CPanel();
        btnLoad = new com.see.truetransact.uicomponent.CButton();
        btnRemove = new com.see.truetransact.uicomponent.CButton();
        panAddress = new com.see.truetransact.uicomponent.CPanel();
        lblStreet = new com.see.truetransact.uicomponent.CLabel();
        txtStreet = new com.see.truetransact.uicomponent.CTextField();
        lblArea = new com.see.truetransact.uicomponent.CLabel();
        txtArea = new com.see.truetransact.uicomponent.CTextField();
        lblCity = new com.see.truetransact.uicomponent.CLabel();
        cboCity = new com.see.truetransact.uicomponent.CComboBox();
        lblState = new com.see.truetransact.uicomponent.CLabel();
        cboState = new com.see.truetransact.uicomponent.CComboBox();
        lblCountry = new com.see.truetransact.uicomponent.CLabel();
        cboCountry = new com.see.truetransact.uicomponent.CComboBox();
        lblPinCode = new com.see.truetransact.uicomponent.CLabel();
        txtPinCode = new com.see.truetransact.uicomponent.CTextField();
        panSkills = new com.see.truetransact.uicomponent.CPanel();
        panGeneraInfol = new com.see.truetransact.uicomponent.CPanel();
        panDates = new com.see.truetransact.uicomponent.CPanel();
        lblBirthDate = new com.see.truetransact.uicomponent.CLabel();
        tdtBirthDate = new com.see.truetransact.uicomponent.CDateField();
        lblWeddindDate = new com.see.truetransact.uicomponent.CLabel();
        tdtWeddindDate = new com.see.truetransact.uicomponent.CDateField();
        lblJoiningDate = new com.see.truetransact.uicomponent.CLabel();
        tdtJoiningDate = new com.see.truetransact.uicomponent.CDateField();
        lblLeavingDate = new com.see.truetransact.uicomponent.CLabel();
        tdtLeavingDate = new com.see.truetransact.uicomponent.CDateField();
        sptSkills_Vert = new com.see.truetransact.uicomponent.CSeparator();
        panPersonalNumbers = new com.see.truetransact.uicomponent.CPanel();
        lblPanNo = new com.see.truetransact.uicomponent.CLabel();
        txtPanNo = new com.see.truetransact.uicomponent.CTextField();
        lblSsnNo = new com.see.truetransact.uicomponent.CLabel();
        txtSsnNo = new com.see.truetransact.uicomponent.CTextField();
        lblPassPortNo = new com.see.truetransact.uicomponent.CLabel();
        txtPassPortNo = new com.see.truetransact.uicomponent.CTextField();
        sptSkills_Hori = new com.see.truetransact.uicomponent.CSeparator();
        panDescriptions = new com.see.truetransact.uicomponent.CPanel();
        lblSkills = new com.see.truetransact.uicomponent.CLabel();
        srpSkills = new com.see.truetransact.uicomponent.CScrollPane();
        txaSkills = new com.see.truetransact.uicomponent.CTextArea();
        lblEducation = new com.see.truetransact.uicomponent.CLabel();
        srpEducation = new com.see.truetransact.uicomponent.CScrollPane();
        txaEducation = new com.see.truetransact.uicomponent.CTextArea();
        lblExperience = new com.see.truetransact.uicomponent.CLabel();
        srpExperience = new com.see.truetransact.uicomponent.CScrollPane();
        txaExperience = new com.see.truetransact.uicomponent.CTextArea();
        panPerformance = new com.see.truetransact.uicomponent.CPanel();
        lblResponsibility = new com.see.truetransact.uicomponent.CLabel();
        srpResponsibility = new com.see.truetransact.uicomponent.CScrollPane();
        txaResponsibility = new com.see.truetransact.uicomponent.CTextArea();
        lblPerformance = new com.see.truetransact.uicomponent.CLabel();
        srpPerformance = new com.see.truetransact.uicomponent.CScrollPane();
        txaPerformance = new com.see.truetransact.uicomponent.CTextArea();
        lblComments = new com.see.truetransact.uicomponent.CLabel();
        srpComments = new com.see.truetransact.uicomponent.CScrollPane();
        txaComments = new com.see.truetransact.uicomponent.CTextArea();
        panStatus = new com.see.truetransact.uicomponent.CPanel();
        lblSpace1 = new com.see.truetransact.uicomponent.CLabel();
        lblStatus1 = new com.see.truetransact.uicomponent.CLabel();
        lblMsg = new com.see.truetransact.uicomponent.CLabel();
        tbrLoantProduct = new javax.swing.JToolBar();
        btnView = new com.see.truetransact.uicomponent.CButton();
        lblSpace5 = new com.see.truetransact.uicomponent.CLabel();
        btnNew = new com.see.truetransact.uicomponent.CButton();
        btnEdit = new com.see.truetransact.uicomponent.CButton();
        btnDelete = new com.see.truetransact.uicomponent.CButton();
        lblSpace2 = new com.see.truetransact.uicomponent.CLabel();
        btnSave = new com.see.truetransact.uicomponent.CButton();
        btnCancel = new com.see.truetransact.uicomponent.CButton();
        lblSpace3 = new com.see.truetransact.uicomponent.CLabel();
        btnAuthorize = new com.see.truetransact.uicomponent.CButton();
        btnReject = new com.see.truetransact.uicomponent.CButton();
        btnException = new com.see.truetransact.uicomponent.CButton();
        lblSpace4 = new com.see.truetransact.uicomponent.CLabel();
        btnPrint = new com.see.truetransact.uicomponent.CButton();
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
        panEmployee.setLayout(new java.awt.GridBagLayout());

        panEmployee.setBorder(new javax.swing.border.EtchedBorder());
        lblSpaces.setMinimumSize(new java.awt.Dimension(3, 11));
        lblSpaces.setPreferredSize(new java.awt.Dimension(3, 11));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        panEmployee.add(lblSpaces, gridBagConstraints);

        panEmployeeDetails.setLayout(new java.awt.GridBagLayout());

        panEmployeeData.setLayout(new java.awt.GridBagLayout());

        lblEmployeeId.setText("Employee ID");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panEmployeeData.add(lblEmployeeId, gridBagConstraints);

        txtEmployeeId.setMinimumSize(new java.awt.Dimension(100, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panEmployeeData.add(txtEmployeeId, gridBagConstraints);

        lblBranchCode.setText("Branch Code");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panEmployeeData.add(lblBranchCode, gridBagConstraints);

        lblTitle.setText("Title");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panEmployeeData.add(lblTitle, gridBagConstraints);

        cboTitle.setMinimumSize(new java.awt.Dimension(50, 21));
        cboTitle.setPreferredSize(new java.awt.Dimension(50, 21));
        cboTitle.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cboTitleActionPerformed(evt);
            }
        });

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panEmployeeData.add(cboTitle, gridBagConstraints);

        lblFirstName.setText("First Name");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panEmployeeData.add(lblFirstName, gridBagConstraints);

        txtFirstName.setMinimumSize(new java.awt.Dimension(100, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panEmployeeData.add(txtFirstName, gridBagConstraints);

        lblLastName.setText("Last Name");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 5;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panEmployeeData.add(lblLastName, gridBagConstraints);

        txtLastName.setMinimumSize(new java.awt.Dimension(100, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 5;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panEmployeeData.add(txtLastName, gridBagConstraints);

        lblGender.setText("Gender");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 6;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panEmployeeData.add(lblGender, gridBagConstraints);

        panGender.setLayout(new java.awt.GridBagLayout());

        panGender.setMinimumSize(new java.awt.Dimension(110, 21));
        panGender.setPreferredSize(new java.awt.Dimension(110, 21));
        rdoGender_Male.setText("Male");
        rdoGender.add(rdoGender_Male);
        rdoGender_Male.setMaximumSize(new java.awt.Dimension(55, 21));
        rdoGender_Male.setMinimumSize(new java.awt.Dimension(55, 21));
        rdoGender_Male.setPreferredSize(new java.awt.Dimension(47, 21));
        rdoGender_Male.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                rdoGender_MaleFocusLost(evt);
            }
        });

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 23, 0, 0);
        panGender.add(rdoGender_Male, gridBagConstraints);

        rdoGender_Female.setText("Female");
        rdoGender.add(rdoGender_Female);
        rdoGender_Female.setMaximumSize(new java.awt.Dimension(59, 21));
        rdoGender_Female.setMinimumSize(new java.awt.Dimension(85, 21));
        rdoGender_Female.setPreferredSize(new java.awt.Dimension(85, 21));
        rdoGender_Female.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                rdoGender_FemaleFocusLost(evt);
            }
        });

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(1, 0, 0, 2);
        panGender.add(rdoGender_Female, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 6;
        gridBagConstraints.ipadx = 15;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 0, 4, 4);
        panEmployeeData.add(panGender, gridBagConstraints);

        lblMartialStatus.setText("Martial Status");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 7;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panEmployeeData.add(lblMartialStatus, gridBagConstraints);

        lblEmployeeType.setText("Employee Type");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 8;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panEmployeeData.add(lblEmployeeType, gridBagConstraints);

        cboEmployeeType.setMinimumSize(new java.awt.Dimension(100, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 8;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panEmployeeData.add(cboEmployeeType, gridBagConstraints);

        lblJobTitle.setText("Job Title");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 9;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panEmployeeData.add(lblJobTitle, gridBagConstraints);

        cboJobTitle.setMinimumSize(new java.awt.Dimension(100, 21));
        cboJobTitle.setPopupWidth(170);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 9;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panEmployeeData.add(cboJobTitle, gridBagConstraints);

        lblDepartment.setText("Department");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 10;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panEmployeeData.add(lblDepartment, gridBagConstraints);

        cboDepartment.setMinimumSize(new java.awt.Dimension(100, 21));
        cboDepartment.setPopupWidth(175);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 10;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panEmployeeData.add(cboDepartment, gridBagConstraints);

        lblManager.setText("Manager");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 11;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panEmployeeData.add(lblManager, gridBagConstraints);

        cboManager.setMinimumSize(new java.awt.Dimension(100, 21));
        cboManager.setPopupWidth(150);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 11;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panEmployeeData.add(cboManager, gridBagConstraints);

        lblOfficialEmail.setText("Official Email");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 12;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panEmployeeData.add(lblOfficialEmail, gridBagConstraints);

        txtOfficialEmail.setMinimumSize(new java.awt.Dimension(100, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 12;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panEmployeeData.add(txtOfficialEmail, gridBagConstraints);

        lblAlternateEmail.setText("Alternate Email");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 13;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panEmployeeData.add(lblAlternateEmail, gridBagConstraints);

        txtAlternateEmail.setMinimumSize(new java.awt.Dimension(100, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 13;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panEmployeeData.add(txtAlternateEmail, gridBagConstraints);

        lblOfficePhone.setText("Office Phone");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 14;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panEmployeeData.add(lblOfficePhone, gridBagConstraints);

        txtOfficePhone.setMinimumSize(new java.awt.Dimension(100, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 14;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panEmployeeData.add(txtOfficePhone, gridBagConstraints);

        lblHomePhone.setText("Home Phone");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 15;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panEmployeeData.add(lblHomePhone, gridBagConstraints);

        txtHomePhone.setMinimumSize(new java.awt.Dimension(100, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 15;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panEmployeeData.add(txtHomePhone, gridBagConstraints);

        lblCellular.setText("Cellular");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 16;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panEmployeeData.add(lblCellular, gridBagConstraints);

        txtCellular.setMinimumSize(new java.awt.Dimension(100, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 16;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panEmployeeData.add(txtCellular, gridBagConstraints);

        lblBranchName.setText("Branch Name");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panEmployeeData.add(lblBranchName, gridBagConstraints);

        lblValBranchName.setPreferredSize(new java.awt.Dimension(150, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panEmployeeData.add(lblValBranchName, gridBagConstraints);

        panBranch.setLayout(new java.awt.GridBagLayout());

        txtBranchCode.setMinimumSize(new java.awt.Dimension(100, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panBranch.add(txtBranchCode, gridBagConstraints);

        btnBranchCode.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_SELECT.gif")));
        btnBranchCode.setToolTipText("Save");
        btnBranchCode.setMaximumSize(new java.awt.Dimension(25, 25));
        btnBranchCode.setPreferredSize(new java.awt.Dimension(21, 21));
        btnBranchCode.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnBranchCodeActionPerformed(evt);
            }
        });

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        panBranch.add(btnBranchCode, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        panEmployeeData.add(panBranch, gridBagConstraints);

        cboMartialStatus.setMinimumSize(new java.awt.Dimension(100, 21));
        cboMartialStatus.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                cboMartialStatusFocusLost(evt);
            }
        });

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 7;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panEmployeeData.add(cboMartialStatus, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTH;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(10, 4, 4, 4);
        panEmployeeDetails.add(panEmployeeData, gridBagConstraints);

        panEmployeeInfo.setLayout(new java.awt.GridBagLayout());

        panPicture.setLayout(new java.awt.GridBagLayout());

        panPicture.setBorder(new javax.swing.border.TitledBorder("Picture"));
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
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTH;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panEmployeeInfo.add(panPicture, gridBagConstraints);

        panAddress.setLayout(new java.awt.GridBagLayout());

        panAddress.setBorder(new javax.swing.border.TitledBorder("Address"));
        lblStreet.setText("Street");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panAddress.add(lblStreet, gridBagConstraints);

        txtStreet.setMinimumSize(new java.awt.Dimension(100, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panAddress.add(txtStreet, gridBagConstraints);

        lblArea.setText("Area");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panAddress.add(lblArea, gridBagConstraints);

        txtArea.setMinimumSize(new java.awt.Dimension(100, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panAddress.add(txtArea, gridBagConstraints);

        lblCity.setText("City");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panAddress.add(lblCity, gridBagConstraints);

        cboCity.setMinimumSize(new java.awt.Dimension(100, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panAddress.add(cboCity, gridBagConstraints);

        lblState.setText("State");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panAddress.add(lblState, gridBagConstraints);

        cboState.setMinimumSize(new java.awt.Dimension(100, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panAddress.add(cboState, gridBagConstraints);

        lblCountry.setText("Country");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panAddress.add(lblCountry, gridBagConstraints);

        cboCountry.setMinimumSize(new java.awt.Dimension(100, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panAddress.add(cboCountry, gridBagConstraints);

        lblPinCode.setText("Pin Code");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 5;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panAddress.add(lblPinCode, gridBagConstraints);

        txtPinCode.setMinimumSize(new java.awt.Dimension(100, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 5;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panAddress.add(txtPinCode, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTH;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panEmployeeInfo.add(panAddress, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTH;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panEmployeeDetails.add(panEmployeeInfo, gridBagConstraints);

        tabEmployee.addTab("Employee Details", panEmployeeDetails);

        panSkills.setLayout(new java.awt.GridBagLayout());

        panGeneraInfol.setLayout(new java.awt.GridBagLayout());

        panDates.setLayout(new java.awt.GridBagLayout());

        lblBirthDate.setText("Birth Date");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panDates.add(lblBirthDate, gridBagConstraints);

        tdtBirthDate.setMinimumSize(new java.awt.Dimension(100, 21));
        tdtBirthDate.setPreferredSize(new java.awt.Dimension(100, 21));
        tdtBirthDate.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                tdtBirthDateFocusLost(evt);
            }
        });

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panDates.add(tdtBirthDate, gridBagConstraints);

        lblWeddindDate.setText(" Wedding Date");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panDates.add(lblWeddindDate, gridBagConstraints);

        tdtWeddindDate.setMinimumSize(new java.awt.Dimension(100, 21));
        tdtWeddindDate.setPreferredSize(new java.awt.Dimension(100, 21));
        tdtWeddindDate.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                tdtWeddindDateFocusLost(evt);
            }
        });

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panDates.add(tdtWeddindDate, gridBagConstraints);

        lblJoiningDate.setText("Joining Date");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panDates.add(lblJoiningDate, gridBagConstraints);

        tdtJoiningDate.setMinimumSize(new java.awt.Dimension(100, 21));
        tdtJoiningDate.setPreferredSize(new java.awt.Dimension(100, 21));
        tdtJoiningDate.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                tdtJoiningDateFocusLost(evt);
            }
        });

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panDates.add(tdtJoiningDate, gridBagConstraints);

        lblLeavingDate.setText("Leaving Date");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panDates.add(lblLeavingDate, gridBagConstraints);

        tdtLeavingDate.setMinimumSize(new java.awt.Dimension(100, 21));
        tdtLeavingDate.setPreferredSize(new java.awt.Dimension(100, 21));
        tdtLeavingDate.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                tdtLeavingDateFocusLost(evt);
            }
        });

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panDates.add(tdtLeavingDate, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTH;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panGeneraInfol.add(panDates, gridBagConstraints);

        sptSkills_Vert.setOrientation(javax.swing.SwingConstants.VERTICAL);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panGeneraInfol.add(sptSkills_Vert, gridBagConstraints);

        panPersonalNumbers.setLayout(new java.awt.GridBagLayout());

        lblPanNo.setText("PAN No.");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panPersonalNumbers.add(lblPanNo, gridBagConstraints);

        txtPanNo.setMinimumSize(new java.awt.Dimension(100, 21));
        txtPanNo.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                txtPanNoFocusLost(evt);
            }
        });

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panPersonalNumbers.add(txtPanNo, gridBagConstraints);

        lblSsnNo.setText("Social Security No.");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panPersonalNumbers.add(lblSsnNo, gridBagConstraints);

        txtSsnNo.setMinimumSize(new java.awt.Dimension(100, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panPersonalNumbers.add(txtSsnNo, gridBagConstraints);

        lblPassPortNo.setText("Passport No.");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panPersonalNumbers.add(lblPassPortNo, gridBagConstraints);

        txtPassPortNo.setMinimumSize(new java.awt.Dimension(100, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panPersonalNumbers.add(txtPassPortNo, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTH;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panGeneraInfol.add(panPersonalNumbers, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTH;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panSkills.add(panGeneraInfol, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTH;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panSkills.add(sptSkills_Hori, gridBagConstraints);

        panDescriptions.setLayout(new java.awt.GridBagLayout());

        lblSkills.setText("Skills");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHEAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panDescriptions.add(lblSkills, gridBagConstraints);

        srpSkills.setMinimumSize(new java.awt.Dimension(346, 100));
        srpSkills.setPreferredSize(new java.awt.Dimension(346, 100));
        srpSkills.setViewportView(txaSkills);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panDescriptions.add(srpSkills, gridBagConstraints);

        lblEducation.setText("Education");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHEAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panDescriptions.add(lblEducation, gridBagConstraints);

        srpEducation.setMinimumSize(new java.awt.Dimension(346, 100));
        srpEducation.setPreferredSize(new java.awt.Dimension(346, 100));
        srpEducation.setViewportView(txaEducation);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panDescriptions.add(srpEducation, gridBagConstraints);

        lblExperience.setText("Experiences");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHEAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panDescriptions.add(lblExperience, gridBagConstraints);

        srpExperience.setMinimumSize(new java.awt.Dimension(346, 100));
        srpExperience.setPreferredSize(new java.awt.Dimension(346, 100));
        srpExperience.setViewportView(txaExperience);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panDescriptions.add(srpExperience, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTH;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panSkills.add(panDescriptions, gridBagConstraints);

        tabEmployee.addTab("Skills", panSkills);

        panPerformance.setLayout(new java.awt.GridBagLayout());

        lblResponsibility.setText("Responsibility");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHEAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panPerformance.add(lblResponsibility, gridBagConstraints);

        srpResponsibility.setMinimumSize(new java.awt.Dimension(346, 100));
        srpResponsibility.setPreferredSize(new java.awt.Dimension(346, 100));
        srpResponsibility.setViewportView(txaResponsibility);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panPerformance.add(srpResponsibility, gridBagConstraints);

        lblPerformance.setText("Performance");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHEAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panPerformance.add(lblPerformance, gridBagConstraints);

        srpPerformance.setMinimumSize(new java.awt.Dimension(346, 100));
        srpPerformance.setPreferredSize(new java.awt.Dimension(346, 100));
        srpPerformance.setViewportView(txaPerformance);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panPerformance.add(srpPerformance, gridBagConstraints);

        lblComments.setText("Comments");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHEAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panPerformance.add(lblComments, gridBagConstraints);

        srpComments.setMinimumSize(new java.awt.Dimension(346, 100));
        srpComments.setPreferredSize(new java.awt.Dimension(346, 100));
        srpComments.setViewportView(txaComments);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panPerformance.add(srpComments, gridBagConstraints);

        tabEmployee.addTab("Performamce", panPerformance);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        panEmployee.add(tabEmployee, gridBagConstraints);

        getContentPane().add(panEmployee, java.awt.BorderLayout.CENTER);

        panStatus.setLayout(new java.awt.GridBagLayout());

        lblSpace1.setText(" Status :");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.fill = java.awt.GridBagConstraints.VERTICAL;
        gridBagConstraints.weighty = 1.0;
        panStatus.add(lblSpace1, gridBagConstraints);

        lblStatus1.setBorder(new javax.swing.border.EtchedBorder());
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

        btnView.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_Summary.gif")));
        btnView.setToolTipText("Enquiry");
        btnView.setMinimumSize(new java.awt.Dimension(21, 21));
        btnView.setPreferredSize(new java.awt.Dimension(21, 21));
        btnView.setEnabled(false);
        btnView.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnViewActionPerformed(evt);
            }
        });

        tbrLoantProduct.add(btnView);

        lblSpace5.setText("     ");
        tbrLoantProduct.add(lblSpace5);

        btnNew.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_NEW.gif")));
        btnNew.setToolTipText("New");
        btnNew.setEnabled(false);
        btnNew.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnNewActionPerformed(evt);
            }
        });

        tbrLoantProduct.add(btnNew);

        btnEdit.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_EDIT.gif")));
        btnEdit.setToolTipText("Edit");
        btnEdit.setEnabled(false);
        btnEdit.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnEditActionPerformed(evt);
            }
        });

        tbrLoantProduct.add(btnEdit);

        btnDelete.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_DELETE.gif")));
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

        btnSave.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_SAVE.gif")));
        btnSave.setToolTipText("Save");
        btnSave.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSaveActionPerformed(evt);
            }
        });

        tbrLoantProduct.add(btnSave);

        btnCancel.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_CANCEL.gif")));
        btnCancel.setToolTipText("Cancel");
        btnCancel.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCancelActionPerformed(evt);
            }
        });

        tbrLoantProduct.add(btnCancel);

        lblSpace3.setText("     ");
        tbrLoantProduct.add(lblSpace3);

        btnAuthorize.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_AUTHORIZE.gif")));
        btnAuthorize.setToolTipText("Authorize");
        btnAuthorize.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnAuthorizeActionPerformed(evt);
            }
        });

        tbrLoantProduct.add(btnAuthorize);

        btnReject.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_REJECT.gif")));
        btnReject.setToolTipText("Reject");
        btnReject.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnRejectActionPerformed(evt);
            }
        });

        tbrLoantProduct.add(btnReject);

        btnException.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_EXCEPTION.gif")));
        btnException.setToolTipText("Exception");
        btnException.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnExceptionActionPerformed(evt);
            }
        });

        tbrLoantProduct.add(btnException);

        lblSpace4.setText("     ");
        tbrLoantProduct.add(lblSpace4);

        btnPrint.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_PRINT.gif")));
        btnPrint.setToolTipText("Print");
        btnPrint.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnPrintActionPerformed(evt);
            }
        });

        tbrLoantProduct.add(btnPrint);

        btnClose.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_CLOSE.gif")));
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
    }//GEN-END:initComponents

    private void btnViewActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnViewActionPerformed
        // TODO add your handling code here:
        observable.setActionType(ClientConstants.ACTIONTYPE_VIEW);
        observable.setStatus();
        lblStatus1.setText(observable.getLblStatus());
        popUp(VIEW);
        btnCheck();

    }//GEN-LAST:event_btnViewActionPerformed
    private void txtPanNoFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtPanNoFocusLost
        ClientUtil.validatePAN(txtPanNo);
    }//GEN-LAST:event_txtPanNoFocusLost
    private void tdtLeavingDateFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_tdtLeavingDateFocusLost
        ClientUtil.validateToDate(tdtLeavingDate, tdtBirthDate.getDateValue());
        ClientUtil.validateToDate(tdtLeavingDate, tdtJoiningDate.getDateValue());
    }//GEN-LAST:event_tdtLeavingDateFocusLost

    private void tdtJoiningDateFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_tdtJoiningDateFocusLost
        ClientUtil.validateToDate(tdtJoiningDate, tdtBirthDate.getDateValue());
        ClientUtil.validateFromDate(tdtJoiningDate, tdtLeavingDate.getDateValue());
    }//GEN-LAST:event_tdtJoiningDateFocusLost

    private void tdtWeddindDateFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_tdtWeddindDateFocusLost
        ClientUtil.validateToDate(tdtWeddindDate, tdtBirthDate.getDateValue());
    }//GEN-LAST:event_tdtWeddindDateFocusLost

    private void tdtBirthDateFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_tdtBirthDateFocusLost
        //--- validate for DOB 
        ClientUtil.validateDob(tdtBirthDate);
    }//GEN-LAST:event_tdtBirthDateFocusLost

    private void rdoGender_FemaleFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_rdoGender_FemaleFocusLost
        // TODO add your handling code here:
        if(rdoGender_Female.isSelected()==true){
            //--- If some Title is selected, then continue
            if(cboTitle.getSelectedIndex()>0){
                //--- If Mr. is selected, then reset the Title.
                if(cboTitle.getSelectedItem().equals(TITLE_MR)){
                    cboTitle.setSelectedIndex(0);
                }
            }
        }
    }//GEN-LAST:event_rdoGender_FemaleFocusLost

    private void rdoGender_MaleFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_rdoGender_MaleFocusLost
        // TODO add your handling code here:
        if(rdoGender_Male.isSelected()==true){
            //--- If some Title is selected, then continue
            if(cboTitle.getSelectedIndex()>0){
                //--- If Mrs. or Ms. is selected, then reset the Title.
                if((cboTitle.getSelectedItem().equals(TITLE_MRS)) || (cboTitle.getSelectedItem().equals(TITLE_MRS))){
                    cboTitle.setSelectedIndex(0);
                }
            }
        }
    }//GEN-LAST:event_rdoGender_MaleFocusLost

    private void cboMartialStatusFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_cboMartialStatusFocusLost
        // TODO add your handling code here:
        //--- If some value is selected,  then continue
        if(cboMartialStatus.getSelectedIndex()>0){
            //--- If some title is selected, then continue
            if(cboTitle.getSelectedIndex()>0){
                //--- If Mrs and Single are seleted or if Ms and Married are selected then reset the martial status
                if((cboTitle.getSelectedItem().equals(TITLE_MRS) && cboMartialStatus.getSelectedItem().equals(MARTIAL_STATUS_SINGLE))|| (cboTitle.getSelectedItem().equals(TITLE_MS) && cboMartialStatus.getSelectedItem().equals(MARTIAL_STATUS_MARRIED))){
                    cboMartialStatus.setSelectedIndex(0);
                }
            }
        }
    }//GEN-LAST:event_cboMartialStatusFocusLost

    private void cboTitleActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cboTitleActionPerformed
        // TODO add your handling code here:
        //--- If some value is selected, continue.
        if(cboTitle.getSelectedIndex()>0){
            String title = CommonUtil.convertObjToStr(cboTitle.getSelectedItem());
            //--- If Mr. then select male else select Female
            if(title.equals(TITLE_MR)){
                rdoGender_Male.setSelected(true);
                rdoGender_Female.setSelected(false);
            } else if(title.equals(TITLE_MRS)||title.equals(TITLE_MS)){
                rdoGender_Male.setSelected(false);
                rdoGender_Female.setSelected(true);
            } 
        } 
    }//GEN-LAST:event_cboTitleActionPerformed
   
    private void btnExceptionActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnExceptionActionPerformed
        // TODO add your handling code here:
        observable.setActionType(ClientConstants.ACTIONTYPE_EXCEPTION);
        authorizeStatus(CommonConstants.STATUS_EXCEPTION);
    }//GEN-LAST:event_btnExceptionActionPerformed

    private void btnRejectActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnRejectActionPerformed
        // TODO add your handling code here:
        observable.setActionType(ClientConstants.ACTIONTYPE_REJECT);
        authorizeStatus(CommonConstants.STATUS_REJECTED);
    }//GEN-LAST:event_btnRejectActionPerformed

    private void btnAuthorizeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAuthorizeActionPerformed
        // TODO add your handling code here:
        observable.setActionType(ClientConstants.ACTIONTYPE_AUTHORIZE);
        setModified(true);
        authorizeStatus(CommonConstants.STATUS_AUTHORIZED);
        ClientUtil.enableDisable(this, false);
    }//GEN-LAST:event_btnAuthorizeActionPerformed
 public void authorizeStatus(String authorizeStatus) {
         if (viewType == AUTHORIZE && isFilled){
            HashMap singleAuthorizeMap = new HashMap();
            singleAuthorizeMap.put("STATUS", authorizeStatus);
            singleAuthorizeMap.put("USER_ID", TrueTransactMain.USER_ID);
            singleAuthorizeMap.put("EMPLOYEE CODE", this.txtEmployeeId.getText());
            singleAuthorizeMap.put("CURR_DATE", currDt.clone());
            ClientUtil.execute("authorizeEmployee", singleAuthorizeMap);
            btnCancelActionPerformed(null);
            viewType=0;
        } else{
            viewType = AUTHORIZE;
            final HashMap mapParam = new HashMap();
            mapParam.put(CommonConstants.MAP_NAME, "getSelectEmployeeAuthorizeTOList");
            mapParam.put(CommonConstants.UPDATE_MAP_NAME, "authorizeEmployee");
            mapParam.put(CommonConstants.AUTHORIZESTATUS, authorizeStatus);
            isFilled = false;
            final AuthorizeStatusUI authorizeUI = new AuthorizeStatusUI(this, mapParam);
            authorizeUI.show();
            btnCancel.setEnabled(true);
            btnSave.setEnabled(false);
            ClientUtil.enableDisable(this, false);
            lblStatus1.setText(observable.getLblStatus());
        }
 }
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
        if (objJFileChooser.showOpenDialog(this) == 0){
            selFile = objJFileChooser.getSelectedFile();
            if (selFile != null){
                fileName = new StringBuffer("file:\\");
                fileName.append(selFile.getAbsolutePath());
            }
            try{
                if(btnRemove.isEnabled()){
                    btnRemoveActionPerformed(null);
                    photoByteArray = null;
                }
                lblPicture.setIcon(new javax.swing.ImageIcon(new java.net.URL(fileName.toString()))); 
                //photoFile = fileName.substring(fileName.lastIndexOf("."));
                readPhoto();
            } catch (Exception e) {
                log.info("Error Declaration in btnLoadActionPerformed:"+e);
            }
        }
        if(lblPicture.getIcon()!=null){
                    this.btnRemove.setEnabled(true);
        }
    }//GEN-LAST:event_btnLoadActionPerformed
     private void readPhoto(){  
        try{
            photoFile = fileName.substring(fileName.lastIndexOf("."));
            FileInputStream reader = new FileInputStream(selFile);
            int size = reader.available();
            photoByteArray = new byte[size];
            reader.read(photoByteArray);
        }catch(Exception e){
            log.info("catch in readPhoto:"+e);
        }
    }
    private void btnBranchCodeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnBranchCodeActionPerformed
        // Add your handling code here:
        popUp(BRANCH);
    }//GEN-LAST:event_btnBranchCodeActionPerformed
    
    private void btnCloseActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCloseActionPerformed
        // Add your handling code here:
        cifClosingAlert();
//        this.dispose();
    }//GEN-LAST:event_btnCloseActionPerformed
    
    private void btnPrintActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnPrintActionPerformed
        // Add your handling code here:
    }//GEN-LAST:event_btnPrintActionPerformed
    
    private void btnCancelActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCancelActionPerformed
        // Add your handling code here:
        setModified(false);
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
        viewType=0;
        
    }//GEN-LAST:event_btnCancelActionPerformed
    
    private void btnSaveActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSaveActionPerformed
        // Add your handling code here:
        updateOBFields();
        setModified(false);
        //To display an alert if the mandatory fields are not properly inputted, else proceed with normal operation
        final String mandatoryMessage = new MandatoryCheck().checkMandatory(getClass().getName(), panEmployee);
        if( observable.getActionType() != ClientConstants.ACTIONTYPE_DELETE && mandatoryMessage.length() > 0 ){
            displayAlert(mandatoryMessage);
        }
        else{
            observable.doAction();// To perform the necessary operation depending on the Action type...
            super.removeEditLock(txtEmployeeId.getText());
            observable.resetForm();// Reset the fields in the UI to null...
            if (observable.getActionType() != ClientConstants.ACTIONTYPE_DELETE)
                enableDisableButtons();// To disable the buttons(folder) in the UI...
            ClientUtil.enableDisable(this, false);// Disables the panel...
            setButtonEnableDisable();// Enables or Disables the buttons and menu Items depending on their previous state...
            observable.setResultStatus();// To Reset the Value of lblStatus...

            setPictuteEnableDisable(false);//To Enable the Load Button for Picture...
            this.btnRemove.setEnabled(false);
            lblPicture.setIcon(null);
            fileName = null;
        }
        btnAuthorize.setEnabled(true);
        btnReject.setEnabled(true);
        btnException.setEnabled(true);
    }//GEN-LAST:event_btnSaveActionPerformed
    private void displayAlert(String message){
        CMandatoryDialog cmd = new CMandatoryDialog();
        cmd.setMessage(message);
        cmd.show();
    }
    //To enable and/or Disable buttons(folder)...
    private void enableDisableButtons() {
        btnBranchCode.setEnabled(!btnBranchCode.isEnabled());
    }
    
    // called from cancel button, to make all the buttons as disabled...
    private void disableButtons() {
        btnBranchCode.setEnabled(false);
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
    }//GEN-LAST:event_btnEditActionPerformed
    private void btnCheck(){
         btnCancel.setEnabled(true);
         btnSave.setEnabled(false);
         btnNew.setEnabled(false);
         btnDelete.setEnabled(false);
         btnAuthorize.setEnabled(false);
         btnReject.setEnabled(false);
         btnException.setEnabled(false);
         btnEdit.setEnabled(false);
     }
    
    
    // To display the All the Product Id's which r having status as
    // created or updated, in a table...
    private void popUp(int field) {
        final HashMap viewMap = new HashMap();
        viewType = field;
        if(field==EDIT || field==DELETE || field==VIEW){//Edit=0 and Delete=1
            ArrayList lst = new ArrayList();
            lst.add("EMPLOYEE_CODE");
            viewMap.put(ClientConstants.RECORD_KEY_COL, lst);
            lst = null;
            viewMap.put("MAPNAME", "viewEmployee");//mapped statement: viewEmployee---> result map should be a Hashmap...
       }else{
           updateOBFields();
            viewMap.put("MAPNAME", "getBranchData");
        }
        new ViewAll(this, viewMap).show();
    }
    
    // this method is called automatically from ViewAll...
    public void fillData(Object param) {
        final HashMap hash = (HashMap) param;
        if (viewType==EDIT || viewType==DELETE || viewType == AUTHORIZE || viewType==VIEW) {
            isFilled = true;
            hash.put("WHERE", hash.get("EMPLOYEE_CODE"));
            if(viewType == AUTHORIZE){
                hash.put("WHERE", hash.get("EMPLOYEE CODE"));
            }
            observable.populateData(hash);// Called to display the Data in the UI fields...
            
            // To Insert the Photo from Database to UI...
            fileName = new StringBuffer(ClientConstants.SERVER_ROOT).append("employee/");
            photoFile = observable.getPhotoFile();
            fileName.append(photoFile);
            try{
                lblPicture.setIcon(new javax.swing.ImageIcon(new java.net.URL(fileName.toString())));
               
                lblPicture.setIcon(new javax.swing.ImageIcon(new java.net.URL(fileName.toString())));
                
            }catch(Exception e){
                log.info("Picture error in fillData");
            } 
            
            if (observable.getActionType() == ClientConstants.ACTIONTYPE_DELETE || observable.getActionType() == ClientConstants.ACTIONTYPE_VIEW) {
                ClientUtil.enableDisable(this, false);// Disables the panel...
                observable.setPhotoFile(null);
            }else{
                ClientUtil.enableDisable(this, true);// Enables the panel...
                enableDisableButtons();// To Enable the Buttons(folder) buttons in UI...
                setPictuteEnableDisable(true);//To Enable the Load Button for Picture...
                // If there is no Image, Remove button should be disabled...
                if(observable.getPhotoFile()!=null)
                    this.btnRemove.setEnabled(true);
                disableTextBox();
            }
            setButtonEnableDisable();// Enables or Disables the buttons and menu Items depending on their previous state...
            observable.setStatus();// To set the Value of lblStatus...
        }else if (viewType==BRANCH) {
            txtBranchCode.setText((String)hash.get("BRANCH_CODE"));
            lblValBranchName.setText((String)hash.get("BRANCH_NAME"));
        }
        if(viewType == AUTHORIZE){
            btnAuthorize.setEnabled(observable.getActionType() == ClientConstants.ACTIONTYPE_AUTHORIZE ? true : false);
            btnReject.setEnabled(observable.getActionType() == ClientConstants.ACTIONTYPE_REJECT ? true : false);
            btnException.setEnabled(observable.getActionType() == ClientConstants.ACTIONTYPE_EXCEPTION ? true : false);
            btnBranchCode.setEnabled(false);
        }
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
        
        setPictuteEnableDisable(true);//To Enable the Load Button for Picture...
        this.btnRemove.setEnabled(false);
        btnAuthorize.setEnabled(false);
        btnReject.setEnabled(false);
        btnException.setEnabled(false);
        lblPicture.setIcon(null);
        fileName = null;
    }//GEN-LAST:event_btnNewActionPerformed
    //To Enable the Load Button for Picture...
    private void setPictuteEnableDisable(boolean load){
        this.btnLoad.setEnabled(load);
    }
    
    private void disableTextBox(){
        txtEmployeeId.setEditable(false);//To make this textBox non editable...
        txtBranchCode.setEnabled(false);//To make this textBox non editable...
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
    
    /** Exit the Application */
    private void exitForm(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_exitForm
        System.exit(0);
    }//GEN-LAST:event_exitForm
    
        
    public static void main(java.lang.String[] args) {
        javax.swing.JFrame frm = new javax.swing.JFrame();
        EmployeeUI emp = new EmployeeUI();
        frm.getContentPane().add(emp);
        emp.show();
        emp.setSize(600, 500);
        emp.show();
    }
        
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private com.see.truetransact.uicomponent.CButton btnAuthorize;
    private com.see.truetransact.uicomponent.CButton btnBranchCode;
    private com.see.truetransact.uicomponent.CButton btnCancel;
    private com.see.truetransact.uicomponent.CButton btnClose;
    private com.see.truetransact.uicomponent.CButton btnDelete;
    private com.see.truetransact.uicomponent.CButton btnEdit;
    private com.see.truetransact.uicomponent.CButton btnException;
    private com.see.truetransact.uicomponent.CButton btnLoad;
    private com.see.truetransact.uicomponent.CButton btnNew;
    private com.see.truetransact.uicomponent.CButton btnPrint;
    private com.see.truetransact.uicomponent.CButton btnReject;
    private com.see.truetransact.uicomponent.CButton btnRemove;
    private com.see.truetransact.uicomponent.CButton btnSave;
    private com.see.truetransact.uicomponent.CButton btnView;
    private com.see.truetransact.uicomponent.CComboBox cboCity;
    private com.see.truetransact.uicomponent.CComboBox cboCountry;
    private com.see.truetransact.uicomponent.CComboBox cboDepartment;
    private com.see.truetransact.uicomponent.CComboBox cboEmployeeType;
    private com.see.truetransact.uicomponent.CComboBox cboJobTitle;
    private com.see.truetransact.uicomponent.CComboBox cboManager;
    private com.see.truetransact.uicomponent.CComboBox cboMartialStatus;
    private com.see.truetransact.uicomponent.CComboBox cboState;
    private com.see.truetransact.uicomponent.CComboBox cboTitle;
    private com.see.truetransact.uicomponent.CLabel lblAlternateEmail;
    private com.see.truetransact.uicomponent.CLabel lblArea;
    private com.see.truetransact.uicomponent.CLabel lblBirthDate;
    private com.see.truetransact.uicomponent.CLabel lblBranchCode;
    private com.see.truetransact.uicomponent.CLabel lblBranchName;
    private com.see.truetransact.uicomponent.CLabel lblCellular;
    private com.see.truetransact.uicomponent.CLabel lblCity;
    private com.see.truetransact.uicomponent.CLabel lblComments;
    private com.see.truetransact.uicomponent.CLabel lblCountry;
    private com.see.truetransact.uicomponent.CLabel lblDepartment;
    private com.see.truetransact.uicomponent.CLabel lblEducation;
    private com.see.truetransact.uicomponent.CLabel lblEmployeeId;
    private com.see.truetransact.uicomponent.CLabel lblEmployeeType;
    private com.see.truetransact.uicomponent.CLabel lblExperience;
    private com.see.truetransact.uicomponent.CLabel lblFirstName;
    private com.see.truetransact.uicomponent.CLabel lblGender;
    private com.see.truetransact.uicomponent.CLabel lblHomePhone;
    private com.see.truetransact.uicomponent.CLabel lblJobTitle;
    private com.see.truetransact.uicomponent.CLabel lblJoiningDate;
    private com.see.truetransact.uicomponent.CLabel lblLastName;
    private com.see.truetransact.uicomponent.CLabel lblLeavingDate;
    private com.see.truetransact.uicomponent.CLabel lblManager;
    private com.see.truetransact.uicomponent.CLabel lblMartialStatus;
    private com.see.truetransact.uicomponent.CLabel lblMsg;
    private com.see.truetransact.uicomponent.CLabel lblOfficePhone;
    private com.see.truetransact.uicomponent.CLabel lblOfficialEmail;
    private com.see.truetransact.uicomponent.CLabel lblPanNo;
    private com.see.truetransact.uicomponent.CLabel lblPassPortNo;
    private com.see.truetransact.uicomponent.CLabel lblPerformance;
    private com.see.truetransact.uicomponent.CLabel lblPicture;
    private com.see.truetransact.uicomponent.CLabel lblPinCode;
    private com.see.truetransact.uicomponent.CLabel lblResponsibility;
    private com.see.truetransact.uicomponent.CLabel lblSkills;
    private com.see.truetransact.uicomponent.CLabel lblSpace1;
    private com.see.truetransact.uicomponent.CLabel lblSpace2;
    private com.see.truetransact.uicomponent.CLabel lblSpace3;
    private com.see.truetransact.uicomponent.CLabel lblSpace4;
    private com.see.truetransact.uicomponent.CLabel lblSpace5;
    private com.see.truetransact.uicomponent.CLabel lblSpaces;
    private com.see.truetransact.uicomponent.CLabel lblSsnNo;
    private com.see.truetransact.uicomponent.CLabel lblState;
    private com.see.truetransact.uicomponent.CLabel lblStatus1;
    private com.see.truetransact.uicomponent.CLabel lblStreet;
    private com.see.truetransact.uicomponent.CLabel lblTitle;
    private com.see.truetransact.uicomponent.CLabel lblValBranchName;
    private com.see.truetransact.uicomponent.CLabel lblWeddindDate;
    private com.see.truetransact.uicomponent.CMenuBar mbrLoanProduct;
    private javax.swing.JMenuItem mitCancel;
    private javax.swing.JMenuItem mitClose;
    private javax.swing.JMenuItem mitDelete;
    private javax.swing.JMenuItem mitEdit;
    private javax.swing.JMenuItem mitNew;
    private javax.swing.JMenuItem mitPrint;
    private javax.swing.JMenuItem mitSave;
    private javax.swing.JMenu mnuProcess;
    private com.see.truetransact.uicomponent.CPanel panAddress;
    private com.see.truetransact.uicomponent.CPanel panBranch;
    private com.see.truetransact.uicomponent.CPanel panButton;
    private com.see.truetransact.uicomponent.CPanel panDates;
    private com.see.truetransact.uicomponent.CPanel panDescriptions;
    private com.see.truetransact.uicomponent.CPanel panEmployee;
    private com.see.truetransact.uicomponent.CPanel panEmployeeData;
    private com.see.truetransact.uicomponent.CPanel panEmployeeDetails;
    private com.see.truetransact.uicomponent.CPanel panEmployeeInfo;
    private com.see.truetransact.uicomponent.CPanel panGender;
    private com.see.truetransact.uicomponent.CPanel panGeneraInfol;
    private com.see.truetransact.uicomponent.CPanel panPerformance;
    private com.see.truetransact.uicomponent.CPanel panPersonalNumbers;
    private com.see.truetransact.uicomponent.CPanel panPicture;
    private com.see.truetransact.uicomponent.CPanel panSkills;
    private com.see.truetransact.uicomponent.CPanel panStatus;
    private com.see.truetransact.uicomponent.CButtonGroup rdoGender;
    private com.see.truetransact.uicomponent.CRadioButton rdoGender_Female;
    private com.see.truetransact.uicomponent.CRadioButton rdoGender_Male;
    private javax.swing.JSeparator sptCancel;
    private javax.swing.JSeparator sptPrint;
    private javax.swing.JSeparator sptProcess;
    private com.see.truetransact.uicomponent.CSeparator sptSkills_Hori;
    private com.see.truetransact.uicomponent.CSeparator sptSkills_Vert;
    private com.see.truetransact.uicomponent.CScrollPane srpComments;
    private com.see.truetransact.uicomponent.CScrollPane srpEducation;
    private com.see.truetransact.uicomponent.CScrollPane srpExperience;
    private com.see.truetransact.uicomponent.CScrollPane srpPerformance;
    private com.see.truetransact.uicomponent.CScrollPane srpPicture;
    private com.see.truetransact.uicomponent.CScrollPane srpResponsibility;
    private com.see.truetransact.uicomponent.CScrollPane srpSkills;
    private com.see.truetransact.uicomponent.CTabbedPane tabEmployee;
    private javax.swing.JToolBar tbrLoantProduct;
    private com.see.truetransact.uicomponent.CDateField tdtBirthDate;
    private com.see.truetransact.uicomponent.CDateField tdtJoiningDate;
    private com.see.truetransact.uicomponent.CDateField tdtLeavingDate;
    private com.see.truetransact.uicomponent.CDateField tdtWeddindDate;
    private com.see.truetransact.uicomponent.CTextArea txaComments;
    private com.see.truetransact.uicomponent.CTextArea txaEducation;
    private com.see.truetransact.uicomponent.CTextArea txaExperience;
    private com.see.truetransact.uicomponent.CTextArea txaPerformance;
    private com.see.truetransact.uicomponent.CTextArea txaResponsibility;
    private com.see.truetransact.uicomponent.CTextArea txaSkills;
    private com.see.truetransact.uicomponent.CTextField txtAlternateEmail;
    private com.see.truetransact.uicomponent.CTextField txtArea;
    private com.see.truetransact.uicomponent.CTextField txtBranchCode;
    private com.see.truetransact.uicomponent.CTextField txtCellular;
    private com.see.truetransact.uicomponent.CTextField txtEmployeeId;
    private com.see.truetransact.uicomponent.CTextField txtFirstName;
    private com.see.truetransact.uicomponent.CTextField txtHomePhone;
    private com.see.truetransact.uicomponent.CTextField txtLastName;
    private com.see.truetransact.uicomponent.CTextField txtOfficePhone;
    private com.see.truetransact.uicomponent.CTextField txtOfficialEmail;
    private com.see.truetransact.uicomponent.CTextField txtPanNo;
    private com.see.truetransact.uicomponent.CTextField txtPassPortNo;
    private com.see.truetransact.uicomponent.CTextField txtPinCode;
    private com.see.truetransact.uicomponent.CTextField txtSsnNo;
    private com.see.truetransact.uicomponent.CTextField txtStreet;
    // End of variables declaration//GEN-END:variables
    
}
