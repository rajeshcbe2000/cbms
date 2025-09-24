/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition. 
 * 
 * GDSApplicationUI.java
 * Created on November 26, 2003, 11:27 AM
 *
 */
package com.see.truetransact.ui.gdsapplication;

/**
 *
 * @author Nithya
 *
 *
 */
import com.see.truetransact.ui.mdsapplication.*;
import java.util.*;
import java.util.HashMap;
import java.util.Observer;
import java.util.Observable;
import com.see.truetransact.clientutil.ClientUtil;
import com.see.truetransact.uicomponent.COptionPane;
import com.see.truetransact.clientutil.ComboBoxModel;
import com.see.truetransact.clientutil.ClientConstants;
import com.see.truetransact.clientutil.CMandatoryDialog;
import com.see.truetransact.commonutil.CommonConstants;
import com.see.truetransact.commonutil.DateUtil;
import com.see.truetransact.uivalidation.NumericValidation;
import com.see.truetransact.uivalidation.CurrencyValidation;
import com.see.truetransact.uimandatory.MandatoryCheck;
import com.see.truetransact.ui.common.viewall.ViewAll;
import com.see.truetransact.ui.common.authorizestatus.AuthorizeStatusUI;
import com.see.truetransact.ui.TrueTransactMain;
import com.see.truetransact.clientexception.ClientParseException;
import com.see.truetransact.commonutil.CommonUtil;
import com.see.truetransact.uimandatory.UIMandatoryField;
import com.see.truetransact.ui.common.nominee.NomineeUI;
import com.see.truetransact.ui.common.transaction.TransactionUI;
import com.see.truetransact.clientproxy.ProxyParameters;
import com.see.truetransact.clientutil.ttrintegration.TTIntegration;
import com.see.truetransact.uicomponent.CTextField;
import com.see.truetransact.uicomponent.CComboBox;
import java.util.ArrayList;
import com.see.truetransact.ui.common.viewall.AuthorizeListUI;
import com.see.truetransact.ui.common.viewall.NewAuthorizeListUI;
import com.see.truetransact.ui.salaryrecovery.AuthorizeListDebitUI;
import com.see.truetransact.ui.salaryrecovery.AuthorizeListCreditUI;
import java.awt.Color;
import javax.swing.*;

public class GDSApplicationUI extends com.see.truetransact.uicomponent.CInternalFrame implements Observer, UIMandatoryField {

    private final static ClientParseException parseException = ClientParseException.getInstance();
    //    private RemittanceProductRB resourceBundle = new RemittanceProductRB();
    java.util.ResourceBundle resourceBundle = java.util.ResourceBundle.getBundle("com.see.truetransact.ui.mdsapplication.MDSApplicationRB", ProxyParameters.LANGUAGE);
    final GDSApplicationMRB objMandatoryMRB = new GDSApplicationMRB();
    NomineeUI nomineeUi = new NomineeUI("SCREEN");
    TransactionUI transactionUI = new TransactionUI();
    private HashMap mandatoryMap;
    private GDSApplicationOB observable;
    private HashMap productMap;
    //    private RemittanceProductMRB objMandatoryMRB;
    private String viewType = "";
    private final String AUTHORIZE = "AUTHORIZE";
    private int RemitProdBrchResult;
    private int RemitProdChrgResult;
    private boolean isFilled = false;
    private boolean actionAuthExcepReject = false;
    private boolean tblAliasSelected = false;
    private boolean tblChargesSelected = false;
    private String accountHead = "ACCOUNT HEAD ID";
    private String bankCode = "";
    private int noOfChittals = 0;
    private double instAmt = 0.0;
     private String gDSNo = "";
    Date currDt = null;
    String isTran = "";
    String PROD_ID = "";
    String DEPOSIT_AMT="";
    String popData="";
    String PRODUCT_ID = "";
    String cust_id = "";
    String NEW_EDIT = "";
    boolean fromAuthorizeUI = false;
    boolean fromCashierAuthorizeUI = false;
    boolean fromManagerAuthorizeUI = false;
    HashMap prizedMembers = new HashMap();
    AuthorizeListUI authorizeListUI = null;
    AuthorizeListDebitUI ManagerauthorizeListUI=null;
    AuthorizeListCreditUI CashierauthorizeListUI=null;
    private int rejectFlag = 0;
    NewAuthorizeListUI newauthorizeListUI = null;
    boolean fromNewAuthorizeUI = false;

    /**
     * Creates new form BeanForm
     */
    public GDSApplicationUI() {
        initComponents();
        settingupUI();
        tabRemittanceProduct.resetVisits();
        currDt = ClientUtil.getCurrentDate();
    }

    private void settingupUI() {
        setObservable();
        setFieldNames();
        internationalize();
        setMandatoryHashMap();
        initComponentData();
       new MandatoryCheck().putMandatoryMarks(getClass().getName(), panInsideGeneralRemittance);
        setMaximumLength();
        setHelpMessage();
        setButtonEnableDisable();
        ClientUtil.enableDisable(this, false);
        txtGroupName.setEnabled(false);
        btnDelete.setEnabled(true);
        panInsideGeneralRemittance1.add(transactionUI);
        transactionUI.setSourceScreen("MDS_APPLICATION");
        txtDivisionNo.setEnabled(false);
        btnCancel.setEnabled(true);
        setVisibleStandingIns(false);
        btnChittalNo.setVisible(false);
        if (CommonUtil.convertObjToStr(CommonConstants.SAL_REC_MODULE).length() > 0 && CommonUtil.convertObjToStr(CommonConstants.SAL_REC_MODULE).equals("Y")) {
            panSalaryRecovery.setVisible(true);
        } else {
            panSalaryRecovery.setVisible(false);
        }
    }

    private void setObservable() {
        observable = GDSApplicationOB.getInstance();
        observable.setTransactionOB(transactionUI.getTransactionOB());
        observable.addObserver(this);
    }

    /* Auto Generated Method - setFieldNames()
     This method assigns name for all the components.
     Other functions are working based on this name. */
    private void setFieldNames() {
        btnAuthorize.setName("btnAuthorize");
        btnCancel.setName("btnCancel");
        btnClose.setName("btnClose");
        btnDelete.setName("btnDelete");
        btnEdit.setName("btnEdit");
        btnException.setName("btnException");
        btnMembershipNo.setName("btnMembershipNo");
        btnNew.setName("btnNew");
        btnPrint.setName("btnPrint");
        btnReject.setName("btnReject");
        btnSave.setName("btnSave");
        btnGroupName.setName("btnGroupName");
        btnView.setName("btnView");
        cboCity.setName("cboCity");
        cboState.setName("cboState");
        chkMunnal.setName("chkMunnal");
        chkNominee.setName("chkNominee");
        chkStandingInstn.setName("chkStandingInstn");
        chkThalayal.setName("chkThalayal");
        lblApplnDate.setName("lblApplnDate");
        lblApplnNo.setName("lblApplnNo");
        lblArea.setName("lblArea");
        lblChitEndDt.setName("lblChitEndDt");
        lblChitStartDt.setName("lblChitStartDt");
        lblChittalNo.setName("lblChittalNo");
        lblSubNo.setName("lblSubNo");
        lblCity.setName("lblCity");
        lblDivisionNo.setName("lblDivisionNo");
        lblInstAmt.setName("lblInstAmt");
        lblMembershipName.setName("lblMembershipName");
        lblMembershipNo.setName("lblMembershipNo");
        lblMembershipType.setName("lblMembershipType");
        lblMembershipTypeValue.setName("lblMembershipTypeValue");
        lblMsg.setName("lblMsg");
        lblMunnal.setName("lblMunnal");
        //        lblNominationDetails.setName("lblNominationDetails");
        lblPin.setName("lblPin");
        lblRemarks.setName("lblRemarks");
        lblGroupName.setName("lblGroupName");
        lblSalaryRecovery.setName("lblSalaryRecovery");
        lblSpace.setName("lblSpace");
        lblSpace3.setName("lblSpace3");
        lblSpace4.setName("lblSpace4");
        lblSpace5.setName("lblSpace5");
        lblSpace6.setName("lblSpace6");
        //        lblStandingInstn.setName("lblStandingInstn");
        lblState.setName("lblState");
        lblStatus.setName("lblStatus");
        lblHouseStNo.setName("lblHouseStNo");
        lblThalayal.setName("lblThalayal");
        lblCoChittal.setName("lblCoChittal");
        mbrMain.setName("mbrMain");
        panDiscountPeriodDetails1.setName("panDiscountPeriodDetails1");
        panDiscountPeriodDetails2.setName("panDiscountPeriodDetails2");
        panGeneralRemittance.setName("panGeneralRemittance");
        panInsideGeneralRemittance.setName("panInsideGeneralRemittance");
        panInsideGeneralRemittance1.setName("panInsideGeneralRemittance1");
        panMembershipNo.setName("panMembershipNo");
        panSchemeName.setName("panSchemeName");
        panStatus.setName("panStatus");
        tabRemittanceProduct.setName("tabRemittanceProduct");
        tdtApplnDate.setName("tdtApplnDate");
        tdtChitEndDt.setName("tdtChitEndDt");
        tdtChitStartDt.setName("tdtChitStartDt");
        txtApplnNo.setName("txtApplnNo");
        txtArea.setName("txtArea");
        txtGDSNo.setName("txtChittalNo");
        txtDivisionNo.setName("txtDivisionNo");
        txtHouseStNo.setName("txtHouseStNo");
        txtInstAmt.setName("txtInstAmt");
        txtSubNo.setName("txtSubNo");
       // txtMembershipName.setName("txtMembershipName");
        txtMembershipNo.setName("txtMembershipNo");
        txtRemarks.setName("txtRemarks");
        txtGroupName.setName("txtSchemeName");
        txtpin.setName("txtpin");
    }

    /**
     * Auto Generated Method - internationalize() This method used to assign
     * display texts from the Resource Bundle File.
     */
    private void internationalize() {
//        resourceBundle = new MDSApplicationRB();
//        lblGroupName.setText(resourceBundle.getString("lblGroupName"));
//        lblSalaryRecovery.setText(resourceBundle.getString("lblSalaryRecovery"));
//        lblDivisionNo.setText(resourceBundle.getString("lblDivisionNo"));
//        lblChittalNo.setText(resourceBundle.getString("lblChittalNo"));
//        lblSubNo.setText(resourceBundle.getString("lblSubNo"));
//        lblChitStartDt.setText(resourceBundle.getString("lblChitStartDt"));
//        lblChitEndDt.setText(resourceBundle.getString("lblChitEndDt"));
//        lblInstAmt.setText(resourceBundle.getString("lblInstAmt"));
//        lblApplnNo.setText(resourceBundle.getString("lblApplnNo"));
//        lblApplnDate.setText(resourceBundle.getString("lblApplnDate"));
//        lblThalayal.setText(resourceBundle.getString("lblThalayal"));
//        lblCoChittal.setText(resourceBundle.getString("lblCoChittal"));
//        lblMunnal.setText(resourceBundle.getString("lblMunnal"));
//        lblMembershipNo.setText(resourceBundle.getString("lblMembershipNo"));
//        lblMembershipType.setText(resourceBundle.getString("lblMembershipType"));
//        lblMembershipName.setText(resourceBundle.getString("lblMembershipName"));
//        lblHouseStNo.setText(resourceBundle.getString("lblHouseStNo"));
//        lblCity.setText(resourceBundle.getString("lblCity"));
//        lblPin.setText(resourceBundle.getString("lblPin"));
//        lblArea.setText(resourceBundle.getString("lblArea"));
//        lblState.setText(resourceBundle.getString("lblState"));
//        //        lblStandingInstn.setText(resourceBundle.getString("lblStandingInstn"));
//        //        lblNominationDetails.setText(resourceBundle.getString("lblNominationDetails"));
//        lblRemarks.setText(resourceBundle.getString("lblRemarks"));
    }

    /**
     * Auto Generated Method - setMandatoryHashMap() This method list out all
     * the Input Fields available in the UI. It needs a class level HashMap
     * variable mandatoryMap.
     */
    public void setMandatoryHashMap() {
        mandatoryMap = new HashMap();
        mandatoryMap.put("txtGroupName", new Boolean(true));
        //        mandatoryMap.put("txtMembershipNo", new Boolean(true));
       // mandatoryMap.put("txtMembershipName", new Boolean(true));
        mandatoryMap.put("txtHouseStNo", new Boolean(true));
        mandatoryMap.put("txtArea", new Boolean(true));
        //        mandatoryMap.put("cboCity", new Boolean(true));
        //        mandatoryMap.put("cboState", new Boolean(true));
        mandatoryMap.put("txtpin", new Boolean(true));
        //        mandatoryMap.put("txtRemarks", new Boolean(true));
        mandatoryMap.put("txtApplnNo", new Boolean(true));
        mandatoryMap.put("tdtApplnDate", new Boolean(true));
    }

    /**
     * Auto Generated Method - getMandatoryHashMap() Getter method for setMandatoryHashMap().
     */
    public HashMap getMandatoryHashMap() {
        return mandatoryMap;
    }

    private void initComponentData() {
        try {
            cboCity.setModel(observable.getCbmCity());
            cboState.setModel(observable.getCbmState());
            cboProdType.setModel(observable.getCbmProdType());
            cboProdId.setModel(observable.getCbmProdId());
        } catch (ClassCastException e) {
            parseException.logException(e, true);
        }
    }

    private void setMaximumLength() {
        txtInstAmt.setValidation(new CurrencyValidation());
        txtGDSNo.setValidation(new NumericValidation());
        txtDivisionNo.setValidation(new NumericValidation());
        txtApplnNo.setValidation(new NumericValidation());
        txtpin.setValidation(new NumericValidation(6, 0));
        txtCustomerIdCr.setAllowAll(true);
    }

    /**
     * Auto Generated Method - update() This method called by Observable. It
     * updates the UI with Observable's data. If needed add/Remove RadioButtons
     * method need to be added.
     */
    public void update(Observable observed, Object arg) {
        //        removeRadioButtons();
        txtGroupName.setText(observable.getTxtGroupName());
        txtDivisionNo.setText(observable.getTxtDivisionNo());
      txtGDSNo.setText(observable.getTxtGDSNo());
        txtSubNo.setText(observable.getTxtSubNo());
        tdtChitStartDt.setDateValue(observable.getTdtChitStartDt());
        tdtChitEndDt.setDateValue(observable.getTdtChitEndDt());
        txtInstAmt.setText(observable.getTxtInstAmt());
        txtApplnNo.setText(observable.getTxtApplnNo());
        tdtApplnDate.setDateValue(observable.getTdtApplnDate());
        chkThalayal.setSelected(observable.getChkThalayal());
        chkCoChittal.setSelected(observable.getChkCoChittal());
        chkMunnal.setSelected(observable.getChkMunnal());
        txtMembershipNo.setText(observable.getTxtMembershipNo());
        txtMembershipName.setText(observable.getTxtMembershipName());
        lblMembershipTypeValue.setText(observable.getTxtMembershipType());
        txtHouseStNo.setText(observable.getTxtHouseStNo());
        txtArea.setText(observable.getTxtArea());
        cboCity.setSelectedItem(CommonUtil.convertObjToStr(observable.getCboCity()));
        cboState.setSelectedItem(CommonUtil.convertObjToStr(observable.getCboState()));
        txtpin.setText(observable.getTxtpin());
        chkStandingInstn.setSelected(observable.getChkStandingInstn());
        if (observable.getActionType() != ClientConstants.ACTIONTYPE_NEW) {
            if (chkStandingInstn.isSelected() == true) {


                String prodType = ((ComboBoxModel) cboProdType.getModel()).getKeyForSelected().toString();
                //observable.setCbmProdId(prodType);
                if (!prodType.equals("GL")) {
                    cboProdId.setSelectedItem(observable.getCbmProdId().getKeyForSelected());
                    cboProdId.setSelectedItem(observable.getCboProdId());

                }
                setVisibleStandingIns(true);
                txtCustomerIdCr.setText(observable.getTxtCustomerIdCr());
            } else {
                setVisibleStandingIns(false);
            }
            if (CommonUtil.convertObjToStr(CommonConstants.SAL_REC_MODULE).length() > 0 && CommonUtil.convertObjToStr(CommonConstants.SAL_REC_MODULE).equals("Y")) {
                String salaryRecovery = CommonUtil.convertObjToStr(observable.getRdoSalaryRecovery());
                if (salaryRecovery.length() > 0 && salaryRecovery.equals("Y")) {
                    rdoSalaryRecovery_Yes.setSelected(true);
                } else {
                    rdoSalaryRecovery_No.setSelected(true);
                }
            }
        }
        cust_id = observable.getCust_id();

        chkNominee.setSelected(observable.getChkNominee());
        txtRemarks.setText(observable.getTxtRemarks());
        addRadioButtons();
        chkMobileBankingAD.setSelected(observable.isChkMobileBankingAD());
        if (chkMobileBankingAD.isSelected() == true) {
            txtMobileNo.setText(observable.getTxtMobileNo());  
            tdtMobileSubscribedFrom.setDateValue(observable.getSubscribtionDt());
        }
        
    }

    // b.) To Add the Radio buttons...
    private void addRadioButtons() {
        //        rdgIsLapsedGR = new CButtonGroup();
        //        rdgEFTProductGR = new CButtonGroup();
        //        rdgPrintServicesGR = new CButtonGroup();
        //        rdgSeriesGR = new CButtonGroup();
        //        rdgIsLapsedGR.add(rdoIsLapsedGR_Yes);
        //        rdgIsLapsedGR.add(rdoIsLapsedGR_No);
        //        rdgEFTProductGR.add(rdoEFTProductGR_Yes);
        //        rdgEFTProductGR.add(rdoEFTProductGR_No);
        //        rdgPrintServicesGR.add(rdoPrintServicesGR_Yes);
        //        rdgPrintServicesGR.add(rdoPrintServicesGR_No);
        //        rdgSeriesGR.add(rdoSeriesGR_Yes);
        //        rdgSeriesGR.add(rdoSeriesGR_No);
    }

    /**
     * Auto Generated Method - updateOBFields() This method called by Save
     * option of UI. It updates the OB with UI data.
     */
    public void updateOBFields() {
        observable.setModule(getModule());
        observable.setScreen(getScreen());
        observable.setTxtGroupName(txtGroupName.getText());
        observable.setTxtGDSNo(txtGDSNo.getText());
        observable.setTxtDivisionNo(txtDivisionNo.getText());
        //observable.setTxtChittalNo(txtGDSNo.getText());
        observable.setTxtChittalNo("0000000000001");// testing purpose
        observable.setTxtSubNo(txtSubNo.getText());
        observable.setTdtChitStartDt(tdtChitStartDt.getDateValue());
        observable.setTdtChitEndDt(tdtChitEndDt.getDateValue());
        observable.setTxtInstAmt(txtInstAmt.getText());
        observable.setTxtApplnNo(txtApplnNo.getText());
        if (CommonUtil.convertObjToStr(CommonConstants.SAL_REC_MODULE).length() > 0 && CommonUtil.convertObjToStr(CommonConstants.SAL_REC_MODULE).equals("Y")) {
            if (rdoSalaryRecovery_Yes.isSelected() == true) {
                observable.setRdoSalaryRecovery("Y");
            } else {
                observable.setRdoSalaryRecovery("N");
            }
        } else {
            observable.setRdoSalaryRecovery("");
        }
        observable.setTdtApplnDate(tdtApplnDate.getDateValue());
        observable.setChkThalayal(chkThalayal.isSelected());
        observable.setChkCoChittal(chkCoChittal.isSelected());
        observable.setChkMunnal(chkMunnal.isSelected());
        observable.setTxtMembershipNo(txtMembershipNo.getText());
        observable.setTxtMembershipName(txtMembershipName.getText());
        observable.setTxtMembershipType(lblMembershipTypeValue.getText());
        observable.setCust_id(cust_id);
        observable.setTxtHouseStNo(txtHouseStNo.getText());
        observable.setTxtArea(txtArea.getText());
        observable.setCboCity(CommonUtil.convertObjToStr(cboCity.getSelectedItem()));
        observable.setCboState(CommonUtil.convertObjToStr(cboState.getSelectedItem()));
        observable.setTxtpin(txtpin.getText());
        observable.setChkStandingInstn(chkStandingInstn.isSelected());
        System.out.println("chkStandingInstn.isSelected()..." + chkStandingInstn.isSelected());
        System.out.println("observable.setChkStandingInstn(" + observable.getChkStandingInstn());
        observable.setCboProdType(CommonUtil.convertObjToStr(cboProdType.getSelectedItem()));
        observable.setCboProdId(CommonUtil.convertObjToStr(cboProdId.getSelectedItem()));
        observable.setTxtCustomerIdCr(txtCustomerIdCr.getText());
        observable.setChkNominee(chkNominee.isSelected());
        observable.setTxtRemarks(txtRemarks.getText());
        observable.setIsTran(isTran);
        System.out.println("isTran" + isTran);
        observable.setChkMobileBankingAD(chkMobileBankingAD.isSelected());
        observable.setTxtMobileNo(txtMobileNo.getText());
        observable.setSubscribtionDt(tdtMobileSubscribedFrom.getDateValue());
        System.out.println("###### :getPredefinedInstallBonus on obbbb" + observable.getPredefinedInstallBonus());
        if(observable.getActionType() == ClientConstants.ACTIONTYPE_EDIT){// Added by nithya on 16-08-2019 for KD 585 - gds standing instruction issue-Kuttilanji
            if(((ComboBoxModel) cboProdId.getModel()).getKeyForSelected() != null)
            observable.setCboProdId(((ComboBoxModel) cboProdId.getModel()).getKeyForSelected().toString());
            if(((ComboBoxModel) cboProdType.getModel()).getKeyForSelected() != null)
            observable.setCboProdType(((ComboBoxModel) cboProdType.getModel()).getKeyForSelected().toString());
        }
    }

    /**
     * Auto Generated Method - setHelpMessage() This method shows tooltip help
     * for all the input fields available in the UI. It needs the Mandatory
     * Resource Bundle object. Help display Label name should be lblMsg.
     */
    public void setHelpMessage() {
        MDSApplicationMRB objMandatoryRB = new MDSApplicationMRB();
        txtDivisionNo.setHelpMessage(lblMsg, objMandatoryRB.getString("txtDivisionNo"));
        txtGDSNo.setHelpMessage(lblMsg, objMandatoryRB.getString("txtChittalNo"));
        tdtChitStartDt.setHelpMessage(lblMsg, objMandatoryRB.getString("tdtChitStartDt"));
        tdtChitEndDt.setHelpMessage(lblMsg, objMandatoryRB.getString("tdtChitEndDt"));
        txtInstAmt.setHelpMessage(lblMsg, objMandatoryRB.getString("txtInstAmt"));
        txtSubNo.setHelpMessage(lblMsg, objMandatoryRB.getString("txtSubNo"));
        txtApplnNo.setHelpMessage(lblMsg, objMandatoryRB.getString("txtApplnNo"));
        tdtApplnDate.setHelpMessage(lblMsg, objMandatoryRB.getString("tdtApplnDate"));
       // txtMembershipName.setHelpMessage(lblMsg, objMandatoryRB.getString("txtMembershipName"));
        txtHouseStNo.setHelpMessage(lblMsg, objMandatoryRB.getString("txtHouseStNo"));
        cboCity.setHelpMessage(lblMsg, objMandatoryRB.getString("cboCity"));
        txtpin.setHelpMessage(lblMsg, objMandatoryRB.getString("txtpin"));
        txtArea.setHelpMessage(lblMsg, objMandatoryRB.getString("txtArea"));
        cboState.setHelpMessage(lblMsg, objMandatoryRB.getString("cboState"));
        txtRemarks.setHelpMessage(lblMsg, objMandatoryRB.getString("txtRemarks"));
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {
        java.awt.GridBagConstraints gridBagConstraints;

        rdgIsLapsedGR = new com.see.truetransact.uicomponent.CButtonGroup();
        rdgEFTProductGR = new com.see.truetransact.uicomponent.CButtonGroup();
        rdgPayableBranchGR = new com.see.truetransact.uicomponent.CButtonGroup();
        rdgPrintServicesGR = new com.see.truetransact.uicomponent.CButtonGroup();
        rdgSeriesGR = new com.see.truetransact.uicomponent.CButtonGroup();
        rdgSalaryRecovery = new com.see.truetransact.uicomponent.CButtonGroup();
        tbrAdvances = new javax.swing.JToolBar();
        btnView = new com.see.truetransact.uicomponent.CButton();
        lblSpace6 = new com.see.truetransact.uicomponent.CLabel();
        btnNew = new com.see.truetransact.uicomponent.CButton();
        lblSpace73 = new com.see.truetransact.uicomponent.CLabel();
        btnEdit = new com.see.truetransact.uicomponent.CButton();
        lblSpace74 = new com.see.truetransact.uicomponent.CLabel();
        btnDelete = new com.see.truetransact.uicomponent.CButton();
        lblSpace3 = new com.see.truetransact.uicomponent.CLabel();
        btnSave = new com.see.truetransact.uicomponent.CButton();
        lblSpace75 = new com.see.truetransact.uicomponent.CLabel();
        btnCancel = new com.see.truetransact.uicomponent.CButton();
        lblSpace4 = new com.see.truetransact.uicomponent.CLabel();
        btnAuthorize = new com.see.truetransact.uicomponent.CButton();
        lblSpace76 = new com.see.truetransact.uicomponent.CLabel();
        btnException = new com.see.truetransact.uicomponent.CButton();
        lblSpace77 = new com.see.truetransact.uicomponent.CLabel();
        btnReject = new com.see.truetransact.uicomponent.CButton();
        lblSpace5 = new com.see.truetransact.uicomponent.CLabel();
        btnPrint = new com.see.truetransact.uicomponent.CButton();
        lblSpace78 = new com.see.truetransact.uicomponent.CLabel();
        btnClose = new com.see.truetransact.uicomponent.CButton();
        tabRemittanceProduct = new com.see.truetransact.uicomponent.CTabbedPane();
        panGeneralRemittance = new com.see.truetransact.uicomponent.CPanel();
        panInsideGeneralRemittance = new com.see.truetransact.uicomponent.CPanel();
        lblApplnDate = new com.see.truetransact.uicomponent.CLabel();
        lblInstAmt = new com.see.truetransact.uicomponent.CLabel();
        txtApplnNo = new com.see.truetransact.uicomponent.CTextField();
        lblApplnNo = new com.see.truetransact.uicomponent.CLabel();
        lblChitEndDt = new com.see.truetransact.uicomponent.CLabel();
        tdtChitStartDt = new com.see.truetransact.uicomponent.CDateField();
        tdtChitEndDt = new com.see.truetransact.uicomponent.CDateField();
        tdtApplnDate = new com.see.truetransact.uicomponent.CDateField();
        txtInstAmt = new com.see.truetransact.uicomponent.CTextField();
        lblChittalNo = new com.see.truetransact.uicomponent.CLabel();
        lblChitStartDt = new com.see.truetransact.uicomponent.CLabel();
        lblGroupName = new com.see.truetransact.uicomponent.CLabel();
        panDiscountPeriodDetails1 = new com.see.truetransact.uicomponent.CPanel();
        chkThalayal = new com.see.truetransact.uicomponent.CCheckBox();
        lblThalayal = new com.see.truetransact.uicomponent.CLabel();
        chkMunnal = new com.see.truetransact.uicomponent.CCheckBox();
        lblMunnal = new com.see.truetransact.uicomponent.CLabel();
        chkCoChittal = new com.see.truetransact.uicomponent.CCheckBox();
        lblCoChittal = new com.see.truetransact.uicomponent.CLabel();
        txtDivisionNo = new com.see.truetransact.uicomponent.CTextField();
        lblDivisionNo = new com.see.truetransact.uicomponent.CLabel();
        panDiscountPeriodDetails2 = new com.see.truetransact.uicomponent.CPanel();
        lblHouseStNo = new com.see.truetransact.uicomponent.CLabel();
        lblArea = new com.see.truetransact.uicomponent.CLabel();
        lblPin = new com.see.truetransact.uicomponent.CLabel();
        lblState = new com.see.truetransact.uicomponent.CLabel();
        lblCity = new com.see.truetransact.uicomponent.CLabel();
        txtHouseStNo = new com.see.truetransact.uicomponent.CTextField();
        txtArea = new com.see.truetransact.uicomponent.CTextField();
        txtpin = new com.see.truetransact.uicomponent.CTextField();
        cboCity = new com.see.truetransact.uicomponent.CComboBox();
        cboState = new com.see.truetransact.uicomponent.CComboBox();
        panSchemeName = new com.see.truetransact.uicomponent.CPanel();
        txtGroupName = new com.see.truetransact.uicomponent.CTextField();
        btnGroupName = new com.see.truetransact.uicomponent.CButton();
        panMemberDetails = new com.see.truetransact.uicomponent.CPanel();
        lblMembershipNo = new com.see.truetransact.uicomponent.CLabel();
        btnMembershipNo = new com.see.truetransact.uicomponent.CButton();
        txtMembershipNo = new com.see.truetransact.uicomponent.CTextField();
        panMembershipNo = new com.see.truetransact.uicomponent.CPanel();
        lblMembershipType = new com.see.truetransact.uicomponent.CLabel();
        lblMembershipTypeValue = new com.see.truetransact.uicomponent.CLabel();
        panStandingDetails = new com.see.truetransact.uicomponent.CPanel();
        panCustomerNO = new com.see.truetransact.uicomponent.CPanel();
        txtCustomerIdCr = new com.see.truetransact.uicomponent.CTextField();
        btnCustomerIdFileOpenCr = new com.see.truetransact.uicomponent.CButton();
        lblAccountNo = new com.see.truetransact.uicomponent.CLabel();
        lblProdId = new com.see.truetransact.uicomponent.CLabel();
        lblProductType = new com.see.truetransact.uicomponent.CLabel();
        cboProdType = new com.see.truetransact.uicomponent.CComboBox();
        cboProdId = new com.see.truetransact.uicomponent.CComboBox();
        chkStandingInstn = new com.see.truetransact.uicomponent.CCheckBox();
        lblActHolderName = new com.see.truetransact.uicomponent.CLabel();
        panNominee = new com.see.truetransact.uicomponent.CPanel();
        chkNominee = new com.see.truetransact.uicomponent.CCheckBox();
        lblRemarks = new com.see.truetransact.uicomponent.CLabel();
        txtRemarks = new com.see.truetransact.uicomponent.CTextField();
        panSalaryRecovery = new com.see.truetransact.uicomponent.CPanel();
        panSalaryRecoveryValue = new com.see.truetransact.uicomponent.CPanel();
        rdoSalaryRecovery_Yes = new com.see.truetransact.uicomponent.CRadioButton();
        rdoSalaryRecovery_No = new com.see.truetransact.uicomponent.CRadioButton();
        lblSalaryRecovery = new com.see.truetransact.uicomponent.CLabel();
        panMemberName = new com.see.truetransact.uicomponent.CPanel();
        lblMembershipName = new com.see.truetransact.uicomponent.CLabel();
        txtMembershipName = new com.see.truetransact.uicomponent.CTextField();
        panChittalNo = new com.see.truetransact.uicomponent.CPanel();
        txtGDSNo = new com.see.truetransact.uicomponent.CTextField();
        lblSubNo = new com.see.truetransact.uicomponent.CLabel();
        txtSubNo = new com.see.truetransact.uicomponent.CTextField();
        panMobileBanking = new com.see.truetransact.uicomponent.CPanel();
        chkMobileBankingAD = new com.see.truetransact.uicomponent.CCheckBox();
        lblMobileNo = new com.see.truetransact.uicomponent.CLabel();
        txtMobileNo = new com.see.truetransact.uicomponent.CTextField();
        lblMobileSubscribedFrom = new com.see.truetransact.uicomponent.CLabel();
        tdtMobileSubscribedFrom = new com.see.truetransact.uicomponent.CDateField();
        btnChittalNo = new com.see.truetransact.uicomponent.CButton();
        panInsideGeneralRemittance1 = new com.see.truetransact.uicomponent.CPanel();
        panStatus = new com.see.truetransact.uicomponent.CPanel();
        lblSpace = new com.see.truetransact.uicomponent.CLabel();
        lblStatus = new com.see.truetransact.uicomponent.CLabel();
        lblMsg = new com.see.truetransact.uicomponent.CLabel();
        mbrMain = new com.see.truetransact.uicomponent.CMenuBar();
        mnuProcess = new javax.swing.JMenu();
        mitNew = new javax.swing.JMenuItem();
        mitEdit = new javax.swing.JMenuItem();
        mitDelete = new javax.swing.JMenuItem();
        sptDelete = new javax.swing.JSeparator();
        mitSave = new javax.swing.JMenuItem();
        mitCancel = new javax.swing.JMenuItem();
        sptCancel = new javax.swing.JSeparator();
        mitAuthorize = new javax.swing.JMenuItem();
        mitReject = new javax.swing.JMenuItem();
        mitException = new javax.swing.JMenuItem();
        sptException = new javax.swing.JSeparator();
        mitPrint = new javax.swing.JMenuItem();
        mitClose = new javax.swing.JMenuItem();

        setClosable(true);
        setIconifiable(true);
        setMaximizable(true);
        setMinimumSize(new java.awt.Dimension(850, 650));
        setPreferredSize(new java.awt.Dimension(850, 650));
        getContentPane().setLayout(new java.awt.GridBagLayout());

        btnView.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_Summary.gif"))); // NOI18N
        btnView.setToolTipText("Enquiry");
        btnView.setEnabled(false);
        btnView.setFocusable(false);
        btnView.setMinimumSize(new java.awt.Dimension(25, 27));
        btnView.setPreferredSize(new java.awt.Dimension(25, 27));
        btnView.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnViewActionPerformed(evt);
            }
        });
        tbrAdvances.add(btnView);

        lblSpace6.setText("     ");
        tbrAdvances.add(lblSpace6);

        btnNew.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_NEW.gif"))); // NOI18N
        btnNew.setToolTipText("New");
        btnNew.setEnabled(false);
        btnNew.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnNewActionPerformed(evt);
            }
        });
        tbrAdvances.add(btnNew);

        lblSpace73.setForeground(new java.awt.Color(240, 240, 240));
        lblSpace73.setText("     ");
        lblSpace73.setMaximumSize(new java.awt.Dimension(3, 18));
        lblSpace73.setMinimumSize(new java.awt.Dimension(3, 18));
        lblSpace73.setPreferredSize(new java.awt.Dimension(3, 18));
        tbrAdvances.add(lblSpace73);

        btnEdit.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_EDIT.gif"))); // NOI18N
        btnEdit.setToolTipText("Edit");
        btnEdit.setEnabled(false);
        btnEdit.setFocusable(false);
        btnEdit.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnEditActionPerformed(evt);
            }
        });
        tbrAdvances.add(btnEdit);

        lblSpace74.setForeground(new java.awt.Color(240, 240, 240));
        lblSpace74.setText("     ");
        lblSpace74.setMaximumSize(new java.awt.Dimension(3, 18));
        lblSpace74.setMinimumSize(new java.awt.Dimension(3, 18));
        lblSpace74.setPreferredSize(new java.awt.Dimension(3, 18));
        tbrAdvances.add(lblSpace74);

        btnDelete.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_DELETE.gif"))); // NOI18N
        btnDelete.setToolTipText("Delete");
        btnDelete.setEnabled(false);
        btnDelete.setFocusable(false);
        btnDelete.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnDeleteActionPerformed(evt);
            }
        });
        tbrAdvances.add(btnDelete);

        lblSpace3.setText("     ");
        tbrAdvances.add(lblSpace3);

        btnSave.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_SAVE.gif"))); // NOI18N
        btnSave.setToolTipText("Save");
        btnSave.setFocusable(false);
        btnSave.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSaveActionPerformed(evt);
            }
        });
        tbrAdvances.add(btnSave);

        lblSpace75.setForeground(new java.awt.Color(240, 240, 240));
        lblSpace75.setText("     ");
        lblSpace75.setMaximumSize(new java.awt.Dimension(3, 18));
        lblSpace75.setMinimumSize(new java.awt.Dimension(3, 18));
        lblSpace75.setPreferredSize(new java.awt.Dimension(3, 18));
        tbrAdvances.add(lblSpace75);

        btnCancel.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_CANCEL.gif"))); // NOI18N
        btnCancel.setToolTipText("Cancel");
        btnCancel.setEnabled(false);
        btnCancel.setFocusable(false);
        btnCancel.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCancelActionPerformed(evt);
            }
        });
        tbrAdvances.add(btnCancel);

        lblSpace4.setText("     ");
        tbrAdvances.add(lblSpace4);

        btnAuthorize.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_AUTHORIZE.gif"))); // NOI18N
        btnAuthorize.setToolTipText("Authorize");
        btnAuthorize.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnAuthorizeActionPerformed(evt);
            }
        });
        tbrAdvances.add(btnAuthorize);

        lblSpace76.setForeground(new java.awt.Color(240, 240, 240));
        lblSpace76.setText("     ");
        lblSpace76.setMaximumSize(new java.awt.Dimension(3, 18));
        lblSpace76.setMinimumSize(new java.awt.Dimension(3, 18));
        lblSpace76.setPreferredSize(new java.awt.Dimension(3, 18));
        tbrAdvances.add(lblSpace76);

        btnException.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_EXCEPTION.gif"))); // NOI18N
        btnException.setToolTipText("Exception");
        btnException.setFocusable(false);
        btnException.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnExceptionActionPerformed(evt);
            }
        });
        tbrAdvances.add(btnException);

        lblSpace77.setForeground(new java.awt.Color(240, 240, 240));
        lblSpace77.setText("     ");
        lblSpace77.setMaximumSize(new java.awt.Dimension(3, 18));
        lblSpace77.setMinimumSize(new java.awt.Dimension(3, 18));
        lblSpace77.setPreferredSize(new java.awt.Dimension(3, 18));
        tbrAdvances.add(lblSpace77);

        btnReject.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_REJECT.gif"))); // NOI18N
        btnReject.setToolTipText("Reject");
        btnReject.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnRejectActionPerformed(evt);
            }
        });
        tbrAdvances.add(btnReject);

        lblSpace5.setText("     ");
        tbrAdvances.add(lblSpace5);

        btnPrint.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_PRINT.gif"))); // NOI18N
        btnPrint.setToolTipText("Print");
        btnPrint.setFocusable(false);
        tbrAdvances.add(btnPrint);

        lblSpace78.setForeground(new java.awt.Color(240, 240, 240));
        lblSpace78.setText("     ");
        lblSpace78.setMaximumSize(new java.awt.Dimension(3, 18));
        lblSpace78.setMinimumSize(new java.awt.Dimension(3, 18));
        lblSpace78.setPreferredSize(new java.awt.Dimension(3, 18));
        tbrAdvances.add(lblSpace78);

        btnClose.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_CLOSE.gif"))); // NOI18N
        btnClose.setToolTipText("Close");
        btnClose.setFocusable(false);
        btnClose.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCloseActionPerformed(evt);
            }
        });
        tbrAdvances.add(btnClose);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        getContentPane().add(tbrAdvances, gridBagConstraints);

        tabRemittanceProduct.setMinimumSize(new java.awt.Dimension(850, 480));
        tabRemittanceProduct.setPreferredSize(new java.awt.Dimension(850, 480));

        panGeneralRemittance.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        panGeneralRemittance.setMinimumSize(new java.awt.Dimension(850, 450));
        panGeneralRemittance.setPreferredSize(new java.awt.Dimension(850, 450));
        panGeneralRemittance.setLayout(new java.awt.GridBagLayout());

        panInsideGeneralRemittance.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        panInsideGeneralRemittance.setMinimumSize(new java.awt.Dimension(850, 330));
        panInsideGeneralRemittance.setPreferredSize(new java.awt.Dimension(850, 350));

        lblApplnDate.setText("Application Date");

        lblInstAmt.setText("Installment Amount");

        txtApplnNo.setMinimumSize(new java.awt.Dimension(100, 21));

        lblApplnNo.setText("Application No");

        lblChitEndDt.setText("Chit End Date");

        tdtApplnDate.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                tdtApplnDateFocusLost(evt);
            }
        });

        txtInstAmt.setMinimumSize(new java.awt.Dimension(100, 21));
        txtInstAmt.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                txtInstAmtFocusLost(evt);
            }
        });

        lblChittalNo.setText("GDS No");

        lblChitStartDt.setText("Chit start Date");

        lblGroupName.setText("GDS Group Name");

        panDiscountPeriodDetails1.setBorder(javax.swing.BorderFactory.createTitledBorder(""));
        panDiscountPeriodDetails1.setMinimumSize(new java.awt.Dimension(575, 30));
        panDiscountPeriodDetails1.setPreferredSize(new java.awt.Dimension(575, 30));
        panDiscountPeriodDetails1.setLayout(new java.awt.GridBagLayout());

        chkThalayal.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                chkThalayalActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(1, 4, 1, 1);
        panDiscountPeriodDetails1.add(chkThalayal, gridBagConstraints);

        lblThalayal.setText("Thalayal");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(2, 20, 2, 2);
        panDiscountPeriodDetails1.add(lblThalayal, gridBagConstraints);

        chkMunnal.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                chkMunnalActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 5;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(1, 4, 1, 1);
        panDiscountPeriodDetails1.add(chkMunnal, gridBagConstraints);

        lblMunnal.setText("Munnal");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 4;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(2, 20, 2, 2);
        panDiscountPeriodDetails1.add(lblMunnal, gridBagConstraints);

        chkCoChittal.setHorizontalTextPosition(javax.swing.SwingConstants.LEFT);
        chkCoChittal.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                chkCoChittalActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(1, 4, 1, 1);
        panDiscountPeriodDetails1.add(chkCoChittal, gridBagConstraints);

        lblCoChittal.setText("Co-Chittal");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(2, 20, 2, 2);
        panDiscountPeriodDetails1.add(lblCoChittal, gridBagConstraints);

        txtDivisionNo.setAllowAll(true);
        txtDivisionNo.setMinimumSize(new java.awt.Dimension(50, 21));
        txtDivisionNo.setPreferredSize(new java.awt.Dimension(50, 21));

        lblDivisionNo.setText("Division No");

        panDiscountPeriodDetails2.setBorder(javax.swing.BorderFactory.createTitledBorder("Address Details"));
        panDiscountPeriodDetails2.setMinimumSize(new java.awt.Dimension(575, 70));
        panDiscountPeriodDetails2.setPreferredSize(new java.awt.Dimension(575, 70));
        panDiscountPeriodDetails2.setLayout(new java.awt.GridBagLayout());

        lblHouseStNo.setText("House/Street No");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(1, 1, 1, 1);
        panDiscountPeriodDetails2.add(lblHouseStNo, gridBagConstraints);

        lblArea.setText("Area");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(1, 1, 1, 1);
        panDiscountPeriodDetails2.add(lblArea, gridBagConstraints);

        lblPin.setText("Pin");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 4;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(1, 1, 1, 1);
        panDiscountPeriodDetails2.add(lblPin, gridBagConstraints);

        lblState.setText("State");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(1, 1, 1, 1);
        panDiscountPeriodDetails2.add(lblState, gridBagConstraints);

        lblCity.setText("City");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(1, 1, 1, 1);
        panDiscountPeriodDetails2.add(lblCity, gridBagConstraints);

        txtHouseStNo.setMinimumSize(new java.awt.Dimension(175, 21));
        txtHouseStNo.setPreferredSize(new java.awt.Dimension(175, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
        panDiscountPeriodDetails2.add(txtHouseStNo, gridBagConstraints);

        txtArea.setMinimumSize(new java.awt.Dimension(175, 21));
        txtArea.setPreferredSize(new java.awt.Dimension(175, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
        panDiscountPeriodDetails2.add(txtArea, gridBagConstraints);

        txtpin.setMinimumSize(new java.awt.Dimension(100, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 5;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
        panDiscountPeriodDetails2.add(txtpin, gridBagConstraints);

        cboCity.setMinimumSize(new java.awt.Dimension(100, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
        panDiscountPeriodDetails2.add(cboCity, gridBagConstraints);

        cboState.setMinimumSize(new java.awt.Dimension(100, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
        panDiscountPeriodDetails2.add(cboState, gridBagConstraints);

        panSchemeName.setLayout(new java.awt.GridBagLayout());

        txtGroupName.setAllowAll(true);
        txtGroupName.setMinimumSize(new java.awt.Dimension(100, 21));
        txtGroupName.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtGroupNameActionPerformed(evt);
            }
        });
        txtGroupName.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                txtGroupNameFocusLost(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        panSchemeName.add(txtGroupName, gridBagConstraints);

        btnGroupName.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_SELECT.gif"))); // NOI18N
        btnGroupName.setEnabled(false);
        btnGroupName.setMaximumSize(new java.awt.Dimension(21, 21));
        btnGroupName.setMinimumSize(new java.awt.Dimension(21, 21));
        btnGroupName.setPreferredSize(new java.awt.Dimension(21, 21));
        btnGroupName.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnGroupNameActionPerformed(evt);
            }
        });
        panSchemeName.add(btnGroupName, new java.awt.GridBagConstraints());

        panMemberDetails.setMinimumSize(new java.awt.Dimension(575, 24));
        panMemberDetails.setPreferredSize(new java.awt.Dimension(575, 24));
        panMemberDetails.setLayout(new java.awt.GridBagLayout());

        lblMembershipNo.setText("Member No");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
        panMemberDetails.add(lblMembershipNo, gridBagConstraints);

        btnMembershipNo.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_SELECT.gif"))); // NOI18N
        btnMembershipNo.setEnabled(false);
        btnMembershipNo.setPreferredSize(new java.awt.Dimension(21, 21));
        btnMembershipNo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnMembershipNoActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 2, 0, 97);
        panMemberDetails.add(btnMembershipNo, gridBagConstraints);

        txtMembershipNo.setAllowAll(true);
        txtMembershipNo.setMinimumSize(new java.awt.Dimension(100, 21));
        txtMembershipNo.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                txtMembershipNoFocusLost(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        panMemberDetails.add(txtMembershipNo, gridBagConstraints);

        panMembershipNo.setLayout(new java.awt.GridBagLayout());
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
        panMemberDetails.add(panMembershipNo, gridBagConstraints);

        lblMembershipType.setText("Member Type");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 4;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(2, 0, 2, 2);
        panMemberDetails.add(lblMembershipType, gridBagConstraints);

        lblMembershipTypeValue.setText("                         ");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 5;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
        panMemberDetails.add(lblMembershipTypeValue, gridBagConstraints);

        panStandingDetails.setMinimumSize(new java.awt.Dimension(230, 95));
        panStandingDetails.setPreferredSize(new java.awt.Dimension(230, 170));
        panStandingDetails.setLayout(new java.awt.GridBagLayout());

        panCustomerNO.setMinimumSize(new java.awt.Dimension(125, 21));
        panCustomerNO.setPreferredSize(new java.awt.Dimension(125, 21));
        panCustomerNO.setLayout(new java.awt.GridBagLayout());

        txtCustomerIdCr.setEditable(false);
        txtCustomerIdCr.setMinimumSize(new java.awt.Dimension(100, 21));
        txtCustomerIdCr.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                txtCustomerIdCrFocusLost(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        panCustomerNO.add(txtCustomerIdCr, gridBagConstraints);

        btnCustomerIdFileOpenCr.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_SELECT.gif"))); // NOI18N
        btnCustomerIdFileOpenCr.setPreferredSize(new java.awt.Dimension(21, 21));
        btnCustomerIdFileOpenCr.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCustomerIdFileOpenCrActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        panCustomerNO.add(btnCustomerIdFileOpenCr, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(2, 0, 4, 0);
        panStandingDetails.add(panCustomerNO, gridBagConstraints);

        lblAccountNo.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        lblAccountNo.setText("Account No");
        lblAccountNo.setMaximumSize(new java.awt.Dimension(85, 18));
        lblAccountNo.setMinimumSize(new java.awt.Dimension(85, 18));
        lblAccountNo.setPreferredSize(new java.awt.Dimension(85, 18));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 4, 2);
        panStandingDetails.add(lblAccountNo, gridBagConstraints);

        lblProdId.setText("Product Id");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
        panStandingDetails.add(lblProdId, gridBagConstraints);

        lblProductType.setText("Product Type");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
        panStandingDetails.add(lblProductType, gridBagConstraints);

        cboProdType.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "----Select----" }));
        cboProdType.setMinimumSize(new java.awt.Dimension(100, 21));
        cboProdType.setPopupWidth(125);
        cboProdType.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cboProdTypeActionPerformed(evt);
            }
        });
        cboProdType.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                cboProdTypeFocusLost(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
        panStandingDetails.add(cboProdType, gridBagConstraints);

        cboProdId.setMinimumSize(new java.awt.Dimension(100, 21));
        cboProdId.setPopupWidth(200);
        cboProdId.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cboProdIdActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
        panStandingDetails.add(cboProdId, gridBagConstraints);

        chkStandingInstn.setText("Standing Instruction");
        chkStandingInstn.setMaximumSize(new java.awt.Dimension(140, 20));
        chkStandingInstn.setMinimumSize(new java.awt.Dimension(140, 20));
        chkStandingInstn.setPreferredSize(new java.awt.Dimension(140, 20));
        chkStandingInstn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                chkStandingInstnActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(6, 4, 1, 1);
        panStandingDetails.add(chkStandingInstn, gridBagConstraints);

        lblActHolderName.setText("cLabel1");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(1, 0, 12, 0);
        panStandingDetails.add(lblActHolderName, gridBagConstraints);

        panNominee.setMinimumSize(new java.awt.Dimension(200, 90));
        panNominee.setPreferredSize(new java.awt.Dimension(200, 90));
        panNominee.setLayout(new java.awt.GridBagLayout());

        chkNominee.setText("Nomination Details");
        chkNominee.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                chkNomineeActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(1, 2, 1, 1);
        panNominee.add(chkNominee, gridBagConstraints);

        lblRemarks.setText("Remarks");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
        panNominee.add(lblRemarks, gridBagConstraints);

        txtRemarks.setMinimumSize(new java.awt.Dimension(100, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
        panNominee.add(txtRemarks, gridBagConstraints);

        panSalaryRecovery.setMinimumSize(new java.awt.Dimension(200, 30));
        panSalaryRecovery.setPreferredSize(new java.awt.Dimension(200, 30));
        panSalaryRecovery.setLayout(new java.awt.GridBagLayout());

        panSalaryRecoveryValue.setMinimumSize(new java.awt.Dimension(95, 27));
        panSalaryRecoveryValue.setPreferredSize(new java.awt.Dimension(95, 27));
        panSalaryRecoveryValue.setLayout(new java.awt.GridBagLayout());

        rdgSalaryRecovery.add(rdoSalaryRecovery_Yes);
        rdoSalaryRecovery_Yes.setText("Yes");
        rdoSalaryRecovery_Yes.setMinimumSize(new java.awt.Dimension(50, 18));
        rdoSalaryRecovery_Yes.setPreferredSize(new java.awt.Dimension(50, 18));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        panSalaryRecoveryValue.add(rdoSalaryRecovery_Yes, gridBagConstraints);

        rdgSalaryRecovery.add(rdoSalaryRecovery_No);
        rdoSalaryRecovery_No.setText("No");
        rdoSalaryRecovery_No.setMinimumSize(new java.awt.Dimension(46, 18));
        rdoSalaryRecovery_No.setPreferredSize(new java.awt.Dimension(46, 18));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.weightx = 1.0;
        panSalaryRecoveryValue.add(rdoSalaryRecovery_No, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 1, 4);
        panSalaryRecovery.add(panSalaryRecoveryValue, gridBagConstraints);

        lblSalaryRecovery.setText("Salary Recovery");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(0, 4, 1, 4);
        panSalaryRecovery.add(lblSalaryRecovery, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(1, 1, 1, 1);
        panNominee.add(panSalaryRecovery, gridBagConstraints);

        panMemberName.setMinimumSize(new java.awt.Dimension(575, 24));
        panMemberName.setPreferredSize(new java.awt.Dimension(575, 24));

        lblMembershipName.setText("Member Name");

        txtMembershipName.setMinimumSize(new java.awt.Dimension(175, 21));
        txtMembershipName.setPreferredSize(new java.awt.Dimension(175, 21));
        txtMembershipName.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtMembershipNameActionPerformed(evt);
            }
        });
        txtMembershipName.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                txtMembershipNameFocusLost(evt);
            }
        });

        javax.swing.GroupLayout panMemberNameLayout = new javax.swing.GroupLayout(panMemberName);
        panMemberName.setLayout(panMemberNameLayout);
        panMemberNameLayout.setHorizontalGroup(
            panMemberNameLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panMemberNameLayout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(lblMembershipName, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(txtMembershipName, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );
        panMemberNameLayout.setVerticalGroup(
            panMemberNameLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panMemberNameLayout.createSequentialGroup()
                .addGap(1, 1, 1)
                .addGroup(panMemberNameLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lblMembershipName, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtMembershipName, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(2, 2, 2))
        );

        panChittalNo.setMinimumSize(new java.awt.Dimension(180, 21));
        panChittalNo.setOpaque(false);
        panChittalNo.setPreferredSize(new java.awt.Dimension(180, 21));

        txtGDSNo.setAllowAll(true);
        txtGDSNo.setMinimumSize(new java.awt.Dimension(160, 21));
        txtGDSNo.setPreferredSize(new java.awt.Dimension(160, 21));
        txtGDSNo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtGDSNoActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout panChittalNoLayout = new javax.swing.GroupLayout(panChittalNo);
        panChittalNo.setLayout(panChittalNoLayout);
        panChittalNoLayout.setHorizontalGroup(
            panChittalNoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(txtGDSNo, javax.swing.GroupLayout.DEFAULT_SIZE, 180, Short.MAX_VALUE)
        );
        panChittalNoLayout.setVerticalGroup(
            panChittalNoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(txtGDSNo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
        );

        lblSubNo.setText("Sub No");

        txtSubNo.setAllowAll(true);
        txtSubNo.setMinimumSize(new java.awt.Dimension(50, 21));
        txtSubNo.setPreferredSize(new java.awt.Dimension(50, 21));

        chkMobileBankingAD.setText("Required");
        chkMobileBankingAD.setHorizontalTextPosition(javax.swing.SwingConstants.LEFT);
        chkMobileBankingAD.setMinimumSize(new java.awt.Dimension(80, 21));
        chkMobileBankingAD.setPreferredSize(new java.awt.Dimension(80, 21));
        chkMobileBankingAD.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                chkMobileBankingADActionPerformed(evt);
            }
        });
        panMobileBanking.add(chkMobileBankingAD);

        lblMobileNo.setText("Mobile No");
        panMobileBanking.add(lblMobileNo);

        txtMobileNo.setAllowAll(true);
        txtMobileNo.setMaxLength(16);
        txtMobileNo.setMinimumSize(new java.awt.Dimension(100, 21));
        txtMobileNo.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                txtMobileNoFocusLost(evt);
            }
        });
        panMobileBanking.add(txtMobileNo);

        lblMobileSubscribedFrom.setText("Subscribed From");
        panMobileBanking.add(lblMobileSubscribedFrom);
        panMobileBanking.add(tdtMobileSubscribedFrom);

        btnChittalNo.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_SELECT.gif"))); // NOI18N
        btnChittalNo.setEnabled(false);
        btnChittalNo.setMaximumSize(new java.awt.Dimension(21, 21));
        btnChittalNo.setMinimumSize(new java.awt.Dimension(21, 21));
        btnChittalNo.setPreferredSize(new java.awt.Dimension(21, 21));
        btnChittalNo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnChittalNoActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout panInsideGeneralRemittanceLayout = new javax.swing.GroupLayout(panInsideGeneralRemittance);
        panInsideGeneralRemittance.setLayout(panInsideGeneralRemittanceLayout);
        panInsideGeneralRemittanceLayout.setHorizontalGroup(
            panInsideGeneralRemittanceLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panInsideGeneralRemittanceLayout.createSequentialGroup()
                .addGroup(panInsideGeneralRemittanceLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(panInsideGeneralRemittanceLayout.createSequentialGroup()
                        .addGap(12, 12, 12)
                        .addComponent(lblGroupName, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(17, 17, 17)
                        .addComponent(panSchemeName, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(3, 3, 3)
                        .addComponent(panDiscountPeriodDetails1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(panInsideGeneralRemittanceLayout.createSequentialGroup()
                        .addGap(67, 67, 67)
                        .addComponent(lblDivisionNo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(3, 3, 3)
                        .addComponent(txtDivisionNo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(74, 74, 74)
                        .addComponent(panMemberDetails, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(panInsideGeneralRemittanceLayout.createSequentialGroup()
                        .addGap(74, 74, 74)
                        .addComponent(lblChittalNo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(12, 12, 12)
                        .addComponent(panChittalNo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btnChittalNo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(panMemberName, javax.swing.GroupLayout.PREFERRED_SIZE, 319, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(panInsideGeneralRemittanceLayout.createSequentialGroup()
                        .addGap(47, 47, 47)
                        .addGroup(panInsideGeneralRemittanceLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(panInsideGeneralRemittanceLayout.createSequentialGroup()
                                .addGap(41, 41, 41)
                                .addComponent(lblSubNo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addComponent(lblChitStartDt, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(lblChitEndDt, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(3, 3, 3)
                        .addGroup(panInsideGeneralRemittanceLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(txtSubNo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(tdtChitStartDt, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(tdtChitEndDt, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(23, 23, 23)
                        .addComponent(panDiscountPeriodDetails2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(panInsideGeneralRemittanceLayout.createSequentialGroup()
                        .addGap(18, 18, 18)
                        .addGroup(panInsideGeneralRemittanceLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(lblInstAmt, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(panInsideGeneralRemittanceLayout.createSequentialGroup()
                                .addGap(28, 28, 28)
                                .addComponent(lblApplnNo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(panInsideGeneralRemittanceLayout.createSequentialGroup()
                                .addGap(17, 17, 17)
                                .addComponent(lblApplnDate, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addGap(4, 4, 4)
                        .addGroup(panInsideGeneralRemittanceLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(txtInstAmt, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(txtApplnNo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(tdtApplnDate, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(57, 57, 57)
                        .addComponent(panStandingDetails, javax.swing.GroupLayout.PREFERRED_SIZE, 326, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(panNominee, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(panInsideGeneralRemittanceLayout.createSequentialGroup()
                        .addGap(32, 32, 32)
                        .addComponent(panMobileBanking, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(10, 10, 10))
        );
        panInsideGeneralRemittanceLayout.setVerticalGroup(
            panInsideGeneralRemittanceLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panInsideGeneralRemittanceLayout.createSequentialGroup()
                .addGap(12, 12, 12)
                .addGroup(panInsideGeneralRemittanceLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(panInsideGeneralRemittanceLayout.createSequentialGroup()
                        .addGap(6, 6, 6)
                        .addComponent(lblGroupName, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(panInsideGeneralRemittanceLayout.createSequentialGroup()
                        .addGap(4, 4, 4)
                        .addComponent(panSchemeName, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(panDiscountPeriodDetails1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(2, 2, 2)
                .addGroup(panInsideGeneralRemittanceLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(panInsideGeneralRemittanceLayout.createSequentialGroup()
                        .addGap(3, 3, 3)
                        .addComponent(lblDivisionNo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(panInsideGeneralRemittanceLayout.createSequentialGroup()
                        .addGap(1, 1, 1)
                        .addComponent(txtDivisionNo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(panMemberDetails, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGroup(panInsideGeneralRemittanceLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(panInsideGeneralRemittanceLayout.createSequentialGroup()
                        .addGap(5, 5, 5)
                        .addComponent(lblChittalNo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(panInsideGeneralRemittanceLayout.createSequentialGroup()
                        .addGap(3, 3, 3)
                        .addComponent(panChittalNo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(panInsideGeneralRemittanceLayout.createSequentialGroup()
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(panInsideGeneralRemittanceLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(panMemberName, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(btnChittalNo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(panInsideGeneralRemittanceLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(panInsideGeneralRemittanceLayout.createSequentialGroup()
                        .addGap(1, 1, 1)
                        .addComponent(lblSubNo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(7, 7, 7)
                        .addComponent(lblChitStartDt, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(7, 7, 7)
                        .addComponent(lblChitEndDt, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(panInsideGeneralRemittanceLayout.createSequentialGroup()
                        .addComponent(txtSubNo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(4, 4, 4)
                        .addComponent(tdtChitStartDt, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(4, 4, 4)
                        .addComponent(tdtChitEndDt, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(panDiscountPeriodDetails2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(4, 4, 4)
                .addGroup(panInsideGeneralRemittanceLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(panInsideGeneralRemittanceLayout.createSequentialGroup()
                        .addGap(1, 1, 1)
                        .addComponent(lblInstAmt, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(7, 7, 7)
                        .addComponent(lblApplnNo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(17, 17, 17)
                        .addComponent(lblApplnDate, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(panInsideGeneralRemittanceLayout.createSequentialGroup()
                        .addComponent(txtInstAmt, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(4, 4, 4)
                        .addComponent(txtApplnNo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(14, 14, 14)
                        .addComponent(tdtApplnDate, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(panInsideGeneralRemittanceLayout.createSequentialGroup()
                        .addGap(2, 2, 2)
                        .addComponent(panStandingDetails, javax.swing.GroupLayout.PREFERRED_SIZE, 108, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(panInsideGeneralRemittanceLayout.createSequentialGroup()
                        .addGap(2, 2, 2)
                        .addComponent(panNominee, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(21, 21, 21)
                .addComponent(panMobileBanking, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
        );

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.ipady = 23;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(2, 8, 0, 9);
        panGeneralRemittance.add(panInsideGeneralRemittance, gridBagConstraints);

        panInsideGeneralRemittance1.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        panInsideGeneralRemittance1.setMinimumSize(new java.awt.Dimension(850, 300));
        panInsideGeneralRemittance1.setPreferredSize(new java.awt.Dimension(850, 300));
        panInsideGeneralRemittance1.setLayout(new java.awt.GridBagLayout());
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.ipady = -51;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(6, 8, 13, 9);
        panGeneralRemittance.add(panInsideGeneralRemittance1, gridBagConstraints);

        tabRemittanceProduct.addTab("Application Details", panGeneralRemittance);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        getContentPane().add(tabRemittanceProduct, gridBagConstraints);

        panStatus.setLayout(new java.awt.GridBagLayout());

        lblSpace.setText(" Status :");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.fill = java.awt.GridBagConstraints.VERTICAL;
        gridBagConstraints.weighty = 1.0;
        panStatus.add(lblSpace, gridBagConstraints);

        lblStatus.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        lblStatus.setText("                      ");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.fill = java.awt.GridBagConstraints.VERTICAL;
        gridBagConstraints.weighty = 1.0;
        panStatus.add(lblStatus, gridBagConstraints);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        panStatus.add(lblMsg, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        getContentPane().add(panStatus, gridBagConstraints);

        mnuProcess.setText("Process");

        mitNew.setText("New");
        mitNew.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mitNewActionPerformed(evt);
            }
        });
        mnuProcess.add(mitNew);

        mitEdit.setText("Edit");
        mitEdit.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mitEditActionPerformed(evt);
            }
        });
        mnuProcess.add(mitEdit);

        mitDelete.setText("Delete");
        mitDelete.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mitDeleteActionPerformed(evt);
            }
        });
        mnuProcess.add(mitDelete);
        mnuProcess.add(sptDelete);

        mitSave.setText("Save");
        mitSave.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mitSaveActionPerformed(evt);
            }
        });
        mnuProcess.add(mitSave);

        mitCancel.setText("Cancel");
        mitCancel.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mitCancelActionPerformed(evt);
            }
        });
        mnuProcess.add(mitCancel);
        mnuProcess.add(sptCancel);

        mitAuthorize.setText("Authorize");
        mnuProcess.add(mitAuthorize);

        mitReject.setText("Rejection");
        mnuProcess.add(mitReject);

        mitException.setText("Exception");
        mnuProcess.add(mitException);
        mnuProcess.add(sptException);

        mitPrint.setText("Print");
        mnuProcess.add(mitPrint);

        mitClose.setText("Close");
        mitClose.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mitCloseActionPerformed(evt);
            }
        });
        mnuProcess.add(mitClose);

        mbrMain.add(mnuProcess);

        setJMenuBar(mbrMain);
    }// </editor-fold>//GEN-END:initComponents

    private void btnChittalNoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnChittalNoActionPerformed
        // TODO add your handling code here:
        callView("CHITTAL_SUB_NO");
    }//GEN-LAST:event_btnChittalNoActionPerformed

    private void chkCoChittalActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_chkCoChittalActionPerformed
        // TODO add your handling code here:
        if (chkCoChittal.isSelected() == true) {
            btnChittalNo.setVisible(true);
            btnChittalNo.setEnabled(true);
            txtSubNo.setEnabled(false);
//            txtChittalNo.setText("");
            double instAmount = CommonUtil.convertObjToDouble(txtInstAmt.getText()).doubleValue();
            instAmount = instAmount / noOfChittals;
            txtInstAmt.setText(String.valueOf(instAmount));
            observable.setTxtInstAmt(txtInstAmt.getText());
            transactionUI.setCallingAmount(CommonUtil.convertObjToStr(txtInstAmt.getText()));
            chkCoChittal.setEnabled(false);
        } else {
            btnChittalNo.setVisible(false);
            txtSubNo.setEnabled(false);
        }
    }//GEN-LAST:event_chkCoChittalActionPerformed

    private void cboProdTypeFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_cboProdTypeFocusLost
    }//GEN-LAST:event_cboProdTypeFocusLost

    private void txtGroupNameFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtGroupNameFocusLost
        // TODO add your handling code here:
        if (txtGroupName.getText().length() > 0) {
            HashMap whereMap = new HashMap();
            whereMap.put("GROUP_NAME", txtGroupName.getText());
            List lst = ClientUtil.executeQuery("getSelectSchemeName", whereMap);
            if (lst != null && lst.size() > 0) {
                observable.setTxtGroupName(txtGroupName.getText());
                viewType = "GROUP_DETAILS";
                whereMap = (HashMap) lst.get(0);
                List chitLst = ClientUtil.executeQuery("getSelectEachSchemeNotEnteredDetails", whereMap);
                if (chitLst != null && chitLst.size() > 0) {
                    whereMap = (HashMap) chitLst.get(0);
                    fillData(whereMap);
                    chitLst = null;
                    lst = null;
                    whereMap = null;
                } else {
                    ClientUtil.displayAlert("Scheme Name Insert Completed !!! ");
                    txtGroupName.setText("");
                    ClientUtil.clearAll(this);
                }
            } else {
                ClientUtil.displayAlert("Invalid Scheme Name !!! ");
                txtGroupName.setText("");
            }
        }
    }//GEN-LAST:event_txtGroupNameFocusLost

    private void chkStandingInstnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_chkStandingInstnActionPerformed
        // TODO add your handling code here:
        boolean flag;
        if (chkStandingInstn.isSelected() == true) {
            flag = true;
            setVisibleStandingIns(true);
            observable.setChkStandingInstn(chkStandingInstn.isSelected());
        } else {
            flag = false;
            setVisibleStandingIns(false);
        }

    }//GEN-LAST:event_chkStandingInstnActionPerformed
    private void setVisibleStandingIns(boolean flag) {
        lblProductType.setVisible(flag);
        cboProdType.setVisible(flag);
        lblProdId.setVisible(flag);
        cboProdId.setVisible(flag);
        lblAccountNo.setVisible(flag);
        panCustomerNO.setVisible(flag);
        lblActHolderName.setVisible(flag);// Added by nithya on 29-03-2017 for 6095
        lblActHolderName.setText((""));
        btnCustomerIdFileOpenCr.setEnabled(flag);
//        if(flag)
//             cboProdId.setModel(observable.getCbmProdId());
//        if(flag)
//        {
//        cboProdTypeActionPerformed(null);
//            System.out.println("gooooo");
//        }
        //        observable.getCbmProdType().setKeyForSelected("");
        //        observable.getCbmProdId().setKeyForSelected("");
        //        cboProdType.setSelectedItem("");
        //        cboProdId.setSelectedItem("");
        txtCustomerIdCr.setText("");
    }
    private void btnCustomerIdFileOpenCrActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCustomerIdFileOpenCrActionPerformed
        // TODO add your handling code here:
        callView("CREDIT_ACC_NO");
    }//GEN-LAST:event_btnCustomerIdFileOpenCrActionPerformed

    private void cboProdTypeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cboProdTypeActionPerformed
        // TODO add your handling code here:
        System.out.println("asdd" + cboProdType.getSelectedIndex());

        {
            if (cboProdType.getSelectedIndex() > 0) {
                System.out.println("innnnnnsdfsf");// TODO add your handling code here:
                String prodType = ((ComboBoxModel) cboProdType.getModel()).getKeyForSelected().toString();
                observable.setCbmProdId(prodType);
                if (prodType.equals("GL")) {
                    cboProdId.setEnabled(false);
                    txtCustomerIdCr.setText("");
                    lblAccountNo.setText("Account Head Id");
                    btnCustomerIdFileOpenCr.setEnabled(true);
                } else {
                    cboProdId.setEnabled(true);
                    lblAccountNo.setText("Account No");
                    txtCustomerIdCr.setText("");
                    btnCustomerIdFileOpenCr.setEnabled(true);
                    txtCustomerIdCr.setEnabled(true);
                }
                if (!prodType.equals("GL")) {
                    cboProdId.setModel(observable.getCbmProdId());
                }
            }
        }
    }//GEN-LAST:event_cboProdTypeActionPerformed

    private void txtMembershipNoFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtMembershipNoFocusLost
        // TODO add your handling code here:
        if (txtMembershipNo.getText().length() > 0) {
            observable.setTxtApplnNo(CommonUtil.convertObjToStr(txtApplnNo.getText()));
            observable.setTdtApplnDate(tdtApplnDate.getDateValue());
            HashMap supMap = new HashMap();
            supMap.put("GROUP_NAME", CommonUtil.convertObjToStr(txtGroupName.getText()));
            supMap.put("GROUP_NO", observable.getgDSNo());
            List schemeList = ClientUtil.executeQuery("getGDSScheme", supMap);           
            if (schemeList != null && schemeList.size() > 0) {
                HashMap scheme = (HashMap) schemeList.get(0);
                supMap.put("SCHEME_NAME", CommonUtil.convertObjToStr(scheme.get("SCHEME_NAME")));
            }
            List lstSupName = ClientUtil.executeQuery("getOnlyMemberOrNot", supMap);
            HashMap supMapData = new HashMap();
            if (lstSupName != null && lstSupName.size() > 0) {
                supMapData = (HashMap) lstSupName.get(0);
            }
            if( supMapData.containsKey("ONLY_MEMBER")    &&  supMapData.get("ONLY_MEMBER") != null){
             popData = CommonUtil.convertObjToStr(supMapData.get("ONLY_MEMBER"));
            }
            HashMap enteredMap = new HashMap();
            enteredMap.put("MEMBERSHIP_NO", txtMembershipNo.getText());
            java.util.List lst = null;
            if (popData.equalsIgnoreCase("Y")) {
                lst = ClientUtil.executeQuery("getMemeberShipDetailsWhileNotMember", enteredMap);
            } else {
                lst = ClientUtil.executeQuery("getMemeberShipDetails", enteredMap); // changed else part by shihad for mantis 10339 on 16.02.2015
            }
            if (lst != null && lst.size() > 0) {
                enteredMap = (HashMap) lst.get(0);
                viewType = "MEMBER_NO";
                fillData(enteredMap);
            } else {
                ClientUtil.showAlertWindow("Invalid Member No");
                txtMembershipNo.setText("");
                return;
            }
        }
        
    }//GEN-LAST:event_txtMembershipNoFocusLost

    private void tdtApplnDateFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_tdtApplnDateFocusLost
        // TODO add your handling code here:
        java.util.Date applnDate = DateUtil.getDateMMDDYYYY(CommonUtil.convertObjToStr(tdtApplnDate.getDateValue()));
        java.util.Date currDate = (Date) currDt.clone();
        if (currDate != null && applnDate != null && DateUtil.dateDiff(currDate, applnDate) > 0) {
            ClientUtil.showAlertWindow("Application date should be less than current date");
            tdtApplnDate.setDateValue("");
        }
    }//GEN-LAST:event_tdtApplnDateFocusLost

    private void chkMunnalActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_chkMunnalActionPerformed
        // TODO add your handling code here:
        if (chkMunnal.isSelected() == true) {
            txtMembershipNo.setText("");
            txtMembershipNo.setEnabled(false);
            txtMembershipName.setEnabled(true);
            lblMembershipTypeValue.setText("");
            txtMembershipName.setText("");
            btnMembershipNo.setEnabled(false);
            productMap = observable.getProductMap();
            if (productMap != null && !productMap.isEmpty() && productMap.get("MUNNAL").equals("N") && chkMunnal.isSelected() == true) {
                ClientUtil.showAlertWindow("Munnal is not allowed under this schemen");
                chkMunnal.setSelected(false);
            } else if (productMap != null && !productMap.isEmpty() && productMap.get("THALAYAL").equals("Y")
                    && chkMunnal.isSelected() == true && CommonUtil.convertObjToInt(observable.getNextActNo()) == 1) {
                ClientUtil.showAlertWindow("First chittal number should be Thalayal");
                chkMunnal.setSelected(false);
            } else if (productMap != null && !productMap.isEmpty() && productMap.get("MUNNAL").equals("Y")
                    && chkMunnal.isSelected() == true && CommonUtil.convertObjToInt(observable.getNextActNo()) != 1) {
                transactionUI.cancelAction(false);
                transactionUI.setButtonEnableDisable(true);
                transactionUI.resetObjects();
                transactionUI.setChitType("MUNNAL");
            } else {
                transactionUI.setChitType("");
            }
        } else {
            if (productMap != null && !productMap.isEmpty() && productMap.get("MUNNAL").equals("Y")
                    && chkMunnal.isSelected() == false && CommonUtil.convertObjToInt(observable.getNextActNo()) != 1) {
                transactionUI.setChitType("");
            }
            transactionUI.cancelAction(false);
            transactionUI.setButtonEnableDisable(true);
            transactionUI.resetObjects();
            txtMembershipNo.setEnabled(true);
            btnMembershipNo.setEnabled(true);
            txtMembershipName.setEnabled(true);
        }
        if (chkThalayal.isSelected() == false && chkMunnal.isSelected() == false) {
            btnMembershipNo.setEnabled(true);
        } else {
            btnMembershipNo.setEnabled(false);
        }
    }//GEN-LAST:event_chkMunnalActionPerformed

    private void chkThalayalActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_chkThalayalActionPerformed
        // TODO add your handling code here:
        if (chkThalayal.isSelected() == true) {
           // txtMembershipNo.setText("");
            //txtMembershipNo.setEnabled(false);
          //  lblMembershipTypeValue.setText("");
            //txtMembershipName.setText("");
            //txtMembershipName.setEnabled(true);
          //  btnMembershipNo.setEnabled(false);
            productMap = observable.getProductMap();

            if (productMap != null && !productMap.isEmpty() && productMap.get("THALAYAL").equals("N") && chkThalayal.isSelected() == true) {
                ClientUtil.showAlertWindow("Thalayal is not allowed under this scheme");
                chkThalayal.setSelected(false);
            } else if (productMap != null && !productMap.isEmpty() && productMap.get("THALAYAL").equals("Y") && chkThalayal.isSelected() == true
                    && CommonUtil.convertObjToInt(observable.getNextActNo()) != 1) {
                ClientUtil.showAlertWindow("Only First chit holder can be Thalayal");
                chkThalayal.setSelected(false);
            } else if (productMap != null && !productMap.isEmpty() && productMap.get("THALAYAL").equals("Y")
                    && chkThalayal.isSelected() == true && CommonUtil.convertObjToInt(observable.getNextActNo()) == 1) {
                transactionUI.cancelAction(false);
                transactionUI.setButtonEnableDisable(true);
                transactionUI.resetObjects();
                transactionUI.setChitType("THALAYAL");
            } else {
                transactionUI.setChitType("");
            }
        } else {
            if (productMap != null && !productMap.isEmpty() && productMap.get("THALAYAL").equals("Y")
                    && chkThalayal.isSelected() == false && CommonUtil.convertObjToInt(observable.getNextActNo()) == 1) {
                transactionUI.setChitType("");
            }
            transactionUI.cancelAction(false);
            transactionUI.setButtonEnableDisable(true);
            transactionUI.resetObjects();
            txtMembershipNo.setEnabled(true);
            btnMembershipNo.setEnabled(true);
            txtMembershipName.setEnabled(true);
        }
        if (chkThalayal.isSelected() == false && chkMunnal.isSelected() == false) {
            btnMembershipNo.setEnabled(true);
        } else {
          //  btnMembershipNo.setEnabled(false);
        }
    }//GEN-LAST:event_chkThalayalActionPerformed

    private void btnMembershipNoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnMembershipNoActionPerformed
        // TODO add your handling code here:
        callView("MEMBER_NO");
    }//GEN-LAST:event_btnMembershipNoActionPerformed

    private void btnGroupNameActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnGroupNameActionPerformed
        // TODO add your handling code here:
        callView("GROUP_DETAILS");
    }//GEN-LAST:event_btnGroupNameActionPerformed

    private void txtInstAmtFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtInstAmtFocusLost
        // TODO add your handling code here:
//        transactionUI.setCallingAmount(txtInstAmt.getText());
    }//GEN-LAST:event_txtInstAmtFocusLost

    private void chkNomineeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_chkNomineeActionPerformed
        // TODO add your handling code here:
        if (chkNominee.isSelected() == true) {
            //            tabTermDeposit.add(nomineeUi);
            tabRemittanceProduct.add(nomineeUi, "Nominee");
            tabRemittanceProduct.resetVisits();
            ClientUtil.enableDisable(nomineeUi, true);
            nomineeUi.resetNomineeTab();
            nomineeUi.enableDisableNominee_SaveDelete();
            nomineeUi.setMainCustomerId(txtGDSNo.getText() + "_" + txtSubNo.getText());
            //            nomineeUi.setCustomerList(getCustomerList());
        } else if (chkNominee.isSelected() == false) {
            tabRemittanceProduct.remove(nomineeUi);
            tabRemittanceProduct.resetVisits();
        } else if ((chkNominee.isSelected() == false) && (nomineeUi.getTblRowCount() > 0)) {
            //--- if Nominee details is unchecked, then display the warning message
        } else if ((chkNominee.isSelected() == false) && (nomineeUi.getTblRowCount() == 0)) {
            resetNominee();
        }
    }//GEN-LAST:event_chkNomineeActionPerformed
    private void resetNominee() {
        nomineeUi.resetNomineeData();
        nomineeUi.resetTable();
        nomineeUi.resetNomineeTab();
        nomineeUi.setBtnEnableDisable(false);
        //        observable.setChkNomineeDetails(false);
        //        chkNominee.setSelected(observable.getChkNomineeDetails());
        ClientUtil.enableDisable(nomineeUi, false);
    }
    private void btnViewActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnViewActionPerformed
        // TODO add your handling code here:
        observable.setActionType(ClientConstants.ACTIONTYPE_VIEW);
        observable.setStatus();
        lblStatus.setText(observable.getLblStatus());
        callView("Enquirystatus");
        btnCheck();
        btnCustomerIdFileOpenCr.setEnabled(false);
        cboProdId.setEnabled(false);
    }            //    private void enableDisableAliasBranchTable(boolean flag) {//GEN-LAST:event_btnViewActionPerformed
    //        tblAliasBrchRemittNumber.setEnabled(flag);
    //    }        private void updateChargesBankBranchDisplay(){
    //        lblDisplayBankCode.setText(observable.getLblChargesBankCode());
    //        lblDisplayBranchCode.setText(observable.getLblChargesBranchCode());
    //    }

    private void remitBtnDefaultEnableDisable(boolean enableDisable) {
        //        btnBrchRemittNumberNew.setEnabled(enableDisable);
        //        btnBrchRemittNumberSave.setEnabled(enableDisable);
        //        btnBrchRemittNumberDelete.setEnabled(enableDisable);
    }

    /* To enable or disable Remittance buttons when any one of them is selected*/
    private void remitBtnOnSelectionEnableDisable() {
        //        btnBrchRemittNumberNew.setEnabled(true);
        //        btnBrchRemittNumberSave.setEnabled(true);
        //        btnBrchRemittNumberDelete.setEnabled(false);
    }

    /* To enable only the Remittance New button*/
    private void remitBtnEnableDisable() {
        //        btnBrchRemittNumberNew.setEnabled(true);
        //        btnBrchRemittNumberSave.setEnabled(false);
        //        btnBrchRemittNumberDelete.setEnabled(false);
    }

    private boolean forEveryCheck() {
        //        if ((txtForEvery == null || txtForEvery.getText().length() == 0 || CommonUtil.convertObjToDouble(txtForEvery.getText()).doubleValue() == 0) && cboRateType.getSelectedIndex() <= 0 && (txtRateVal == null || txtRateVal.getText().length() == 0 || CommonUtil.convertObjToDouble(txtRateVal.getText()).doubleValue() == 0)){
        //            return true;
        //        }else if ((txtForEvery != null && txtForEvery.getText().length() != 0 || CommonUtil.convertObjToDouble(txtForEvery.getText()).doubleValue() != 0) && cboRateType.getSelectedIndex() > 0 && (txtRateVal != null && txtRateVal.getText().length() != 0 || ! txtRateVal.getText().equals("0") || CommonUtil.convertObjToDouble(txtRateVal.getText()).doubleValue() != 0))  {
        //            return true;
        //        }
        return false;
    }

    private boolean chargeCheck() {
        // Checking for whether Charges and Percentage is containing proper value(s)
        //        if ((txtCharges == null || txtCharges.getText().length() == 0 || CommonUtil.convertObjToDouble(txtCharges.getText()).doubleValue() == 0) && (txtPercentage == null || txtPercentage.getText().length() == 0 || CommonUtil.convertObjToDouble(txtPercentage.getText()).doubleValue() == 0)){
        //            if ((txtForEvery == null ||txtForEvery.getText().length() == 0 || CommonUtil.convertObjToDouble(txtForEvery.getText()).doubleValue() == 0) && cboRateType.getSelectedIndex() <= 0 && (txtRateVal == null || txtRateVal.getText().length() == 0 || CommonUtil.convertObjToDouble(txtRateVal.getText()).doubleValue() == 0)){
        //                return true;
        //            }
        //        }
        return false;
    }
    private void btnRejectActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnRejectActionPerformed
        // Add your handling code here:
        tblAliasSelected = false;
        btnAuthorize.setEnabled(false);
        observable.setActionType(ClientConstants.ACTIONTYPE_REJECT);
        authorizeStatus(CommonConstants.STATUS_REJECTED);
        if (!isModified()) {
            setButtonEnableDisable();
            btnCancelActionPerformed(null);
        }
        cboProdId.setEnabled(false);
        btnCustomerIdFileOpenCr.setEnabled(false);
    }//GEN-LAST:event_btnRejectActionPerformed

    private void btnExceptionActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnExceptionActionPerformed
        // Add your handling code here:
        tblAliasSelected = false;
        observable.setActionType(ClientConstants.ACTIONTYPE_EXCEPTION);
        authorizeStatus(CommonConstants.STATUS_EXCEPTION);
    }//GEN-LAST:event_btnExceptionActionPerformed

    private void btnAuthorizeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAuthorizeActionPerformed
        // Add your handling code here:
        tblAliasSelected = false;
        observable.setActionType(ClientConstants.ACTIONTYPE_AUTHORIZE);
        authorizeStatus(CommonConstants.STATUS_AUTHORIZED);
        //__ If there's no data to be Authorized, call Cancel action...
        if (!isModified()) {
            setButtonEnableDisable();
            btnCancelActionPerformed(null);
        }
        cboProdId.setEnabled(false);
        btnCustomerIdFileOpenCr.setEnabled(false);
    }//GEN-LAST:event_btnAuthorizeActionPerformed

    private void btnNewActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnNewActionPerformed
        // Add your handling code here:
        cust_id = "";
        ClientUtil.enableDisable(panInsideGeneralRemittance, true);
        observable.setActionType(ClientConstants.ACTIONTYPE_NEW);
        observable.setStatus();
        lblStatus.setText(observable.getLblStatus());
        ClientUtil.clearAll(this);
        setButtonEnableDisable();
        txtDivisionNo.setEnabled(false);
        txtGDSNo.setEnabled(false);
        txtGroupName.setEnabled(true);
        btnGroupName.setEnabled(true);
        transactionUI.cancelAction(false);
        transactionUI.setButtonEnableDisable(true);
        transactionUI.resetObjects();
        transactionUI.setCallingUiMode(ClientConstants.ACTIONTYPE_NEW);
        tblAliasSelected = false;
        nomineeUi.resetNomineeTab();
        nomineeUi.setBtnEnableDisable(false);
        lblMembershipTypeValue.setText("");
        btnEdit.setEnabled(false);
        txtSubNo.setText("1");
        observable.setTxtSubNo(txtSubNo.getText());
        txtSubNo.setEnabled(false);
        txtMobileNo.setEnabled(false);
        tdtMobileSubscribedFrom.setEnabled(false);
        //__ To Save the data in the Internal Frame...
        setModified(true);
    }//GEN-LAST:event_btnNewActionPerformed

    private void btnEditActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnEditActionPerformed
        // Add your handling code here:
        cust_id = "";
        actionAuthExcepReject = false;
        tblAliasSelected = false;
        NEW_EDIT="Edit";
        observable.setActionType(ClientConstants.ACTIONTYPE_EDIT);
        transactionUI.setCallingUiMode(ClientConstants.ACTIONTYPE_EDIT);
        callView("Edit");
        transactionUI.cancelAction(true);
        btnDelete.setEnabled(false);
        btnCancel.setEnabled(true);
        chkCoChittal.setEnabled(false);
        txtSubNo.setEnabled(false);
        btnMembershipNo.setEnabled(true);
        System.out.println("memberList"+prizedMembers);
        HashMap newMembersList = new HashMap();
        //commented by shany
        //need to correct edit
//        newMembersList.put("CHITTAL_NO",CommonUtil.convertObjToStr(prizedMembers.get("CHITTAL_NO")));
//        newMembersList.put("GROUP_NAME",CommonUtil.convertObjToStr(prizedMembers.get("GROUP_NAME")));
//        
//        List listPrizedMembers = ClientUtil.executeQuery("getPrizedMembersList", newMembersList);
//        if(listPrizedMembers!=null && listPrizedMembers.size()>0){
//            txtMembershipName.setEditable(false);
//            txtMembershipNo.setEditable(false);
//            txtHouseStNo.setEditable(false);
//            txtArea.setEditable(false);
//            cboCity.setEditable(false);
//            cboCity.setEnabled(false);
//            cboState.setEditable(false);
//            cboState.setEnabled(false);
//            txtpin.setEditable(false);
//            btnMembershipNo.setEnabled(false);
//        }
//        prizedMembers = null;
//        HashMap newMap = new HashMap();
//        newMap.put("CHITTAL_NO", txtGDSNo.getText());
//        List newList = ClientUtil.executeQuery("getProductId", newMap);
//        System.out.println("newList"+newList);
//        HashMap retreivedListMap = new HashMap();
//        if(newList!= null && newList.size()>0)
//        {
//            retreivedListMap = (HashMap)newList.get(0);
//        }
//        PRODUCT_ID = retreivedListMap.get("PROD_ID").toString();
//        System.out.println("PRODUCT_ID"+PRODUCT_ID);
    }//GEN-LAST:event_btnEditActionPerformed

    private void btnDeleteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnDeleteActionPerformed
        // Add your handling code here:
        observable.setActionType(ClientConstants.ACTIONTYPE_DELETE);
        transactionUI.setCallingUiMode(ClientConstants.ACTIONTYPE_DELETE);
        observable.setStatus();
        //        lblStatus.setText(observable.getResult());
        callView("Delete");
        btnCancel.setEnabled(true);
    }//GEN-LAST:event_btnDeleteActionPerformed

    private void btnSaveActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSaveActionPerformed
        // Add your handling code here:
        btnSave.setEnabled(false);
        HashMap groupNoMap = new HashMap();
        if (!txtGroupName.getText().equals("")) {
            groupNoMap.put("GROUP_NO", observable.getgDSNo());
            System.out.println("GROUP_NO "+ observable.getgDSNo());
        } else {
            ClientUtil.showAlertWindow("GDS Group Name should not be empty !!!");
            return;
        }
        HashMap isTranMap = new HashMap();
        List isTranList = ClientUtil.executeQuery("getGDSSelIsTran", groupNoMap);
        System.out.println("isTranListisTranListisTranList" + isTranList.size());
        if (isTranList != null && isTranList.size() > 0) {
            isTranMap = (HashMap) isTranList.get(0);
            isTran = isTranMap.get("TRANS_FIRST_INSTALLMENT").toString();
            System.out.println("isTranisTranisTranisTran" + isTran);
        }
        if (observable.getActionType() == ClientConstants.ACTIONTYPE_NEW) {
            String mandatoryMessage = new MandatoryCheck().checkMandatory(getClass().getName(), panInsideGeneralRemittance);
            if (observable.getActionType() != ClientConstants.ACTIONTYPE_DELETE && mandatoryMessage.length() > 0) {
                displayAlert(mandatoryMessage);
                return;
            } else {
                if (isTran.equals("Y")) {
                    String status = transactionUI.getCallingStatus();
                    String transProdType = transactionUI.getCallingTransProdType();
                    int transactionSize = 0;
                    if (transactionUI.getOutputTO().size() == 0 && CommonUtil.convertObjToDouble(txtInstAmt.getText()).doubleValue() > 0) {
                        ClientUtil.showAlertWindow(transactionUI.getMessage(TransactionUI.NO_RECORDS));
                        return;
                    } else {
                        if (CommonUtil.convertObjToDouble(txtInstAmt.getText()).doubleValue() > 0) {
                            transactionSize = (transactionUI.getOutputTO()).size();
                            if (transactionSize != 1 && CommonUtil.convertObjToDouble(txtInstAmt.getText()).doubleValue() > 0) {
                                ClientUtil.showAlertWindow("Multiple Transactions are Not allowed, Make it one Transaction");
                                return;
                            } else {
                                observable.setAllowedTransactionDetailsTO(transactionUI.getOutputTO());
                            }
                        } else if (transactionUI.getOutputTO().size() > 0) {
                            observable.setAllowedTransactionDetailsTO(transactionUI.getOutputTO());
                        }
                    }
                    if (transactionSize == 0 && CommonUtil.convertObjToDouble(txtInstAmt.getText()).doubleValue() > 0) {
                        ClientUtil.showAlertWindow(transactionUI.getMessage(TransactionUI.NO_RECORDS));
                        return;
                    } else if (transactionSize != 0) {
                        if (!transactionUI.isBtnSaveTransactionDetailsFlag()) {
                            ClientUtil.showAlertWindow(transactionUI.getMessage(TransactionUI.SAVE_TX_DETAILS));
                            return;
                        } else {
                        }
                    }
                }
            }
        } else {
//            if (isTran.equals("Y")) {
//                if (transactionUI.getOutputTO().size() > 0) {
//                    observable.setAllowedTransactionDetailsTO(transactionUI.getOutputTO());
//                }
//
//            } 
//            else {
                observable.setTransId("");
//            }   //changed by jithin
        }
        productMap = observable.getProductMap();

        StringBuffer strBMandatory = new StringBuffer();
        if (observable.getActionType() == ClientConstants.ACTIONTYPE_NEW || observable.getActionType() == ClientConstants.ACTIONTYPE_EDIT) {
            if (chkThalayal.isSelected() == true && chkMunnal.isSelected() == true) {
                ClientUtil.showAlertWindow("Should not select both Munnal and Thalayal");
                return;
            } else {
                if (productMap != null && !productMap.isEmpty() && productMap.get("THALAYAL").equals("Y")
                        && chkThalayal.isSelected() == true && chkMunnal.isSelected() == false && CommonUtil.convertObjToInt(observable.getNextActNo()) != 1) {
                    ClientUtil.showAlertWindow("First chittal number should be Thalayal");
                    return;
                } else if (productMap != null && !productMap.isEmpty() && productMap.get("THALAYAL").equals("N") && chkThalayal.isSelected() == true) {
                    ClientUtil.showAlertWindow("Thalayal is not allowed under this schemen");
                } else if (productMap != null && !productMap.isEmpty() && productMap.get("MUNNAL").equals("N") && chkMunnal.isSelected() == true) {
                    ClientUtil.showAlertWindow("Munnal is not allowed under this schemen");
                } else if (chkThalayal.isSelected() == true && CommonUtil.convertObjToInt(observable.getNextActNo()) != 1) {
                    ClientUtil.showAlertWindow("Only First chit holder can be Thalayal");
                    return;
                } else if (chkNominee.isSelected() == true && nomineeUi.getTblRowCount() == 0) {
                    ClientUtil.showAlertWindow("Enter the Nominee details.");
                    return;
                } else if (txtGDSNo.getText().length() <= 0) {
                    ClientUtil.showAlertWindow("Member GDS Should Not Be Empty !!! ");
                    return;
                } else {
                    savePerformed();
                }
            }
        } else {
            savePerformed();
        }

        //__ Make the Screen Closable..
        setModified(false);
    }//GEN-LAST:event_btnSaveActionPerformed
    private String periodLengthValidation(CTextField txtField, CComboBox comboField) {
        String message = "";
        String key = CommonUtil.convertObjToStr(((ComboBoxModel) comboField.getModel()).getKeyForSelected());
        if (!ClientUtil.validPeriodMaxLength(txtField, key)) {
            message = objMandatoryMRB.getString(txtField.getName());
        }
        return message;
    }
    private void btnCancelActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCancelActionPerformed
        // Add your handling code here:
        tblAliasSelected = false;
        HashMap editLockMap = new HashMap();
        editLockMap.put("SCREEN_ID", getScreenID());
        editLockMap.put("RECORD_KEY", txtGDSNo.getText());
        editLockMap.put("LOCKED_BY", TrueTransactMain.USER_ID);
        editLockMap.put("BRANCH_ID", TrueTransactMain.BRANCH_ID);
        editLockMap.put("CUR_DATE", currDt.clone());
        editLockMap.put("RECORD_KEY", "null");
        setEditLockMap(editLockMap);
        removeEditLock("null");
        //        ClientUtil.executeQuery("deleteEditLock", editLockMap);
        actionAuthExcepReject = false;
        observable.resetStatus();
        transactionUI.setButtonEnableDisable(true);
        transactionUI.cancelAction(false);
        transactionUI.resetObjects();
        transactionUI.setCallingApplicantName("");
        transactionUI.setCallingAmount("");
        transactionUI.setChitType("");
        transactionUI.setSchemeName("");

        nomineeUi.resetTable();
        nomineeUi.resetNomineeData();
        nomineeUi.resetNomineeTab();
        nomineeUi.disableNewButton(false);
        observable.resetOBFields();
        resetUIValues();
        btnMembershipNo.setEnabled(false);
        btnGroupName.setEnabled(false);
        remitBtnDefaultEnableDisable(false);
        ClientUtil.enableDisable(this, false);
        ClientUtil.enableDisable(panMobileBanking, false);
        viewType = "";
        setButtonEnableDisable();
        tabRemittanceProduct.remove(nomineeUi);
        ClientUtil.clearAll(this);
        lblMembershipTypeValue.setText("");
        lblStatus.setText("             ");
        btnCustomerIdFileOpenCr.setEnabled(false);
        //        btnCopy.setEnabled(true);

        //__ Make the Screen Closable..
        setModified(false);
        btnDelete.setEnabled(true);
        btnEdit.setEnabled(true);
        btnNew.setEnabled(true);
        btnSave.setEnabled(false);
        btnAuthorize.setEnabled(true);
        btnException.setEnabled(true);
        btnReject.setEnabled(true);
        setVisibleStandingIns(false);
        observable.getCbmProdType().setKeyForSelected("");
        observable.getCbmProdId().setKeyForSelected("");
        btnChittalNo.setVisible(false);
        if (fromNewAuthorizeUI) {
            this.dispose();
            fromNewAuthorizeUI = false;
            newauthorizeListUI.setFocusToTable();
        }
        if (fromAuthorizeUI) {
            this.dispose();
            fromAuthorizeUI = false;
            authorizeListUI.setFocusToTable();
        }
        if (fromCashierAuthorizeUI) {
            CashierauthorizeListUI.removeSelectedRow();
            this.dispose();
            fromCashierAuthorizeUI = false;
            CashierauthorizeListUI.setFocusToTable();
        } 
        if (fromManagerAuthorizeUI) {
            ManagerauthorizeListUI.removeSelectedRow();
            this.dispose();
            fromManagerAuthorizeUI = false;
            ManagerauthorizeListUI.setFocusToTable();
        }
        
    }//GEN-LAST:event_btnCancelActionPerformed
    private void resetUIValues() {
        txtGroupName.setText("");
        txtDivisionNo.setText("");
        txtGDSNo.setText("");
        tdtChitStartDt.setDateValue("");
        tdtChitEndDt.setDateValue("");
        txtInstAmt.setText("");
        txtApplnNo.setText("");
        tdtApplnDate.setDateValue("");
        chkThalayal.setSelected(false);
        chkMunnal.setSelected(false);
        txtMembershipNo.setText("");
        txtMembershipName.setText("");
        txtHouseStNo.setText("");
        txtArea.setText("");
        cboCity.setSelectedItem("");
        cboState.setSelectedItem("");
        txtpin.setText("");
        chkStandingInstn.setSelected(false);
        chkNominee.setSelected(false);
        txtRemarks.setText("");
        lblMembershipTypeValue.setText("");
        txtSubNo.setText("");
        chkMobileBankingAD.setSelected(false);
        txtMobileNo.setText("");
        tdtMobileSubscribedFrom.setDateValue("");
        lblActHolderName.setText("");// Added by nithya on 29-03-2017 for 6095
    }
    private void btnCloseActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCloseActionPerformed
        // Add your handling code here:
        cifClosingAlert();
        //        this.dispose();
    }//GEN-LAST:event_btnCloseActionPerformed
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

    public void authorize(HashMap map) {
        map.put(CommonConstants.USER_ID, TrueTransactMain.USER_ID);
        //        observable.setActionType(ClientConstants.ACTIONTYPE_AUTHORIZE);
        observable.setAuthorizeMap(map);

        HashMap schNamMap = new HashMap();
        HashMap isTranMap = new HashMap();
        if (!txtGroupName.getText().equals("")) {
            schNamMap.put("GROUP_NO", observable.getgDSNo());
            System.out.println("GROUP_NO" + observable.getgDSNo());
        }
        List isTranList1 = ClientUtil.executeQuery("getGDSSelIsTran", schNamMap);
        System.out.println("isTranListisTranListisTranList" + isTranList1.size());
        if (isTranList1 != null && isTranList1.size() > 0) {
            isTranMap = (HashMap) isTranList1.get(0);
            isTran = isTranMap.get("TRANS_FIRST_INSTALLMENT").toString();
            System.out.println("isTranisTranisTranisTran" + isTran);
        }

        System.out.println("%5"+transactionUI.getOutputTO());
        try {
            if (isTran.equals("Y")) {
                if (transactionUI.getOutputTO().size() > 0) {
                    observable.setAllowedTransactionDetailsTO(transactionUI.getOutputTO());
                }
            }
            HashMap masterMap = new HashMap();
            masterMap.put("GROUP_NAME", txtGroupName.getText());
            masterMap.put("GDS_NO", txtGDSNo.getText());
            masterMap.put("SUB_NO", CommonUtil.convertObjToInt(txtSubNo.getText()));
            observable.setMasterMap(masterMap);
            observable.doSave(nomineeUi.getNomineeOB());
        } catch (Exception e) {
            e.printStackTrace();
        }
        //        btnCancelActionPerformed(null);
        //        cancelOperation();
        //        doWhenCancelled();
        //observable.makeToNull();
        setModified(false);
    }

    public void authorizeStatus(String authorizeStatus) {
        actionAuthExcepReject = true;
        ArrayList arrList = new ArrayList();
        HashMap authDataMap = new HashMap();
        HashMap singleAuthorizeMap = new HashMap();
        if (viewType.equals(AUTHORIZE) && isFilled) {
            authDataMap.put("TRANS_ID", observable.getTransId());
            arrList.add(authDataMap);
            singleAuthorizeMap.put(CommonConstants.AUTHORIZESTATUS, authorizeStatus);
            singleAuthorizeMap.put(CommonConstants.BRANCH_ID, ProxyParameters.BRANCH_ID);
            singleAuthorizeMap.put(CommonConstants.AUTHORIZEDT, ClientUtil.getCurrentDate());
            singleAuthorizeMap.put(CommonConstants.AUTHORIZEDATA, arrList);
            authorize(singleAuthorizeMap);
            super.setOpenForEditBy(observable.getStatusBy());
            //            super.removeEditLock(txtProductIdGR.getText());
            //btnCancelActionPerformed(null);
            observable.setResult(observable.getActionType());
            if(observable.getActionType()!= ClientConstants.ACTIONTYPE_FAILED){
                if (fromNewAuthorizeUI) {
                    newauthorizeListUI.removeSelectedRow();
                    this.dispose();
                    newauthorizeListUI.setFocusToTable();
                    newauthorizeListUI.displayDetails("GDSApplication");
                }
                if (fromAuthorizeUI) {
                authorizeListUI.removeSelectedRow();
                this.dispose();
                authorizeListUI.setFocusToTable();
                authorizeListUI.displayDetails("GDS Application");
            }
            if (fromCashierAuthorizeUI) {
                this.dispose();
                fromCashierAuthorizeUI = false;
                CashierauthorizeListUI.setFocusToTable();
            }
            if (fromManagerAuthorizeUI) {
                this.dispose();
                fromManagerAuthorizeUI = false;
                ManagerauthorizeListUI.setFocusToTable();
            }
            }
            observable.setStatus();
            observable.setResultStatus();
            lblStatus.setText(observable.getLblStatus());
            viewType = "";
        } else {
            final HashMap mapParam = new HashMap();
            HashMap whereMap = new HashMap();
            whereMap.put(CommonConstants.USER_ID, TrueTransactMain.USER_ID);
            whereMap.put(CommonConstants.AUTHORIZESTATUS, authorizeStatus);
            whereMap.put(CommonConstants.SELECTED_BRANCH_ID, getSelectedBranchID());
            whereMap.put("TRANS_DT",currDt);
            mapParam.put(CommonConstants.MAP_WHERE, whereMap);
            whereMap = null;
            if (TrueTransactMain.CASHIER_AUTH_ALLOWED != null && TrueTransactMain.CASHIER_AUTH_ALLOWED.equals("Y")) {
                mapParam.put(CommonConstants.MAP_NAME, "getSelectNonAuthRecordForEntered");
            } else {
                mapParam.put(CommonConstants.MAP_NAME, "getGDSSelectNonAuthRecordForEnteredWithOutCashier");
            }
            //mapParam.put(CommonConstants.MAP_NAME, "getGDSSelectNonAuthRecordForEntered");
            mapParam.put(CommonConstants.UPDATE_MAP_NAME, "authorizeRemittanceProduct");
            viewType = AUTHORIZE;
            lblStatus.setText(ClientConstants.ACTION_STATUS[0]);
            isFilled = false;
            final AuthorizeStatusUI authorizeUI = new AuthorizeStatusUI(this, mapParam);
            authorizeUI.show();
            btnSaveDisable();
            lblStatus.setText(observable.getLblStatus());
            //            setAuthBtnEnableDisable();
        }
    }

    private void btnSaveDisable() {
        btnSave.setEnabled(false);
        mitSave.setEnabled(false);
        btnCancel.setEnabled(true);
        mitCancel.setEnabled(true);
    }

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

    private void clearingMemberField() {
        observable.setTxtMembershipNo("");
        observable.setTxtMembershipType("");
        observable.setTxtMembershipName("");
        txtMembershipNo.setText("");
        txtMembershipName.setText("");
        lblMembershipTypeValue.setText("");
        txtHouseStNo.setText("");
        txtArea.setText("");
        cboCity.setSelectedItem("");
        cboState.setSelectedItem("");
        txtpin.setText("");
        observable.setTxtHouseStNo("");
        observable.setTxtArea("");
        observable.setCboCity("");
        observable.setCboState("");
        observable.setTxtpin("");

    }
    private void mitCloseActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mitCloseActionPerformed
        // Add your handling code here:
        btnCloseActionPerformed(evt);
        //        this.dispose();
    }//GEN-LAST:event_mitCloseActionPerformed

    private void txtMembershipNameActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtMembershipNameActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtMembershipNameActionPerformed

    private void txtMembershipNameFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtMembershipNameFocusLost
        // TODO add your handling code here:

        if (txtMembershipName.getText().length() > 0) {
            transactionUI.setCallingApplicantName(txtMembershipName.getText());
        }
    }//GEN-LAST:event_txtMembershipNameFocusLost

    private void cboProdIdActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cboProdIdActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_cboProdIdActionPerformed

    private void txtCustomerIdCrFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtCustomerIdCrFocusLost
        // TODO add your handling code here:
		      String ACCOUNTNO = (String) txtCustomerIdCr.getText();
        if (ACCOUNTNO.length() > 0) {
            if (observable.checkAcNoWithoutProdType(ACCOUNTNO)) {
                cboProdId.setModel(observable.getCbmProdId());
                cboProdIdActionPerformed(null);
                txtCustomerIdCr.setText(observable.getTxtCustomerIdCr());
                HashMap viewMap = new HashMap();
                String prodType = ((ComboBoxModel) cboProdType.getModel()).getKeyForSelected().toString();
                if (!prodType.equals("GL")) {
                    viewMap.put(CommonConstants.MAP_NAME, "Cash.getAccountList"
                            + ((ComboBoxModel) cboProdType.getModel()).getKeyForSelected().toString());
                } else {
                    viewMap.put(CommonConstants.MAP_NAME, "Cash.getSelectAcctHead");
                }
                HashMap whereMap = new HashMap();
                if (cboProdId.getModel() != null && cboProdId.getModel().getSize() > 0) {
                    whereMap.put("PROD_ID", ((ComboBoxModel) cboProdId.getModel()).getKeyForSelected());
                }
                whereMap.put("SELECTED_BRANCH", ACCOUNTNO.substring(0, 4));
                viewMap.put(CommonConstants.MAP_WHERE, whereMap);
                whereMap.put("ACT_NUM", ACCOUNTNO);
                whereMap.put(CommonConstants.DB_DRIVER_NAME, ProxyParameters.dbDriverName);
                whereMap.put("FILTERED_LIST", "FILTERED_LIST" + "_" + ProxyParameters.dbDriverName);
                List list = ClientUtil.executeQuery(CommonUtil.convertObjToStr(viewMap.get(CommonConstants.MAP_NAME)), whereMap);
                if (list != null && list.size() > 0) {
                    HashMap custNAmeMap = (HashMap) list.get(0);
                    if (custNAmeMap.containsKey("CustomerName") && custNAmeMap.get("CustomerName") != null && !custNAmeMap.get("CustomerName").equals("") && CommonUtil.convertObjToStr(custNAmeMap.get("CustomerName")).length() > 0) {
                        lblActHolderName.setText(CommonUtil.convertObjToStr(custNAmeMap.get("CustomerName")));
                        lblActHolderName.setForeground(Color.blue);
                    }
                }
            }
        }
    }//GEN-LAST:event_txtCustomerIdCrFocusLost

private void EnableDisbleMobileBanking(boolean flag) {
        txtMobileNo.setEnabled(flag);
        tdtMobileSubscribedFrom.setEnabled(flag);
}

private void chkMobileBankingADActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_chkMobileBankingADActionPerformed
        // TODO add your handling code here:
        if (chkMobileBankingAD.isSelected()) {
            EnableDisbleMobileBanking(true);
            if (observable.getActionType() == ClientConstants.ACTIONTYPE_NEW) {
                if (observable.getCust_id().length() > 0) {
                    long mobileNo = getMobileNo(observable.getCust_id());
                    if (mobileNo != 0) {
                        txtMobileNo.setText(CommonUtil.convertObjToStr(mobileNo));
                    }
                }
            }       
        } else {
            EnableDisbleMobileBanking(false);
            txtMobileNo.setText("");
            tdtMobileSubscribedFrom.setDateValue("");
        }
}//GEN-LAST:event_chkMobileBankingADActionPerformed

    private long getMobileNo(String custId) {
        long mobileNo = 0;
        HashMap mobileMap = new HashMap();
        mobileMap.put("CUST_ID", custId);
        mobileMap.put("BRANCH_CODE", ProxyParameters.BRANCH_ID);
        List list = ClientUtil.executeQuery("getSMSContactForDepositMaturedCustomer", mobileMap);
        if (list != null && list.size() > 0) {
            mobileMap = (HashMap) list.get(0);
            mobileNo = CommonUtil.convertObjToLong(mobileMap.get("CONTACT_NO"));
        }
        return mobileNo;
    }

private void txtMobileNoFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtMobileNoFocusLost
// TODO add your handling code here:
    if(txtMobileNo.getText().length()!=10){
        ClientUtil.showMessageWindow("Mobile No. not valid!!!");
        txtMobileNo.setText("");
        return;
    }
}//GEN-LAST:event_txtMobileNoFocusLost

    private void txtGroupNameActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtGroupNameActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtGroupNameActionPerformed

    private void txtGDSNoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtGDSNoActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtGDSNoActionPerformed

    /**
     * This method helps in popoualting the data from the data base
     *
     * @param currField Action the argument is passed according to the command
     * issued
     */
    private void callView(String currField) {
        updateOBFields();
        viewType = currField;
        
        HashMap viewMap = new HashMap();
        if (currField.equals("Edit") || currField.equals("Delete")) {
            lblStatus.setText(ClientConstants.ACTION_STATUS[0]);
            ArrayList lst = new ArrayList();
            lst.add("PRODUCT ID");
            viewMap.put(ClientConstants.RECORD_KEY_COL, lst);
            lst = null;
            HashMap whereMap = new HashMap();
            whereMap.put("SCHEME_NAMES", txtGroupName.getText());           
            viewMap.put(CommonConstants.MAP_WHERE, whereMap);
            viewMap.put(CommonConstants.MAP_NAME, "getGDSSelectRecordForEntered");
        } else if (currField.equalsIgnoreCase("GROUP_DETAILS")) {
            viewMap.put(CommonConstants.MAP_NAME, "getGDSSelectEachSchemeNotEnteredDetails");
            
            System.out.println("viewMap"+viewMap);
        } else if (currField.equalsIgnoreCase("CHITTAL_SUB_NO")) {
            HashMap whereMap = new HashMap();
            whereMap.put("SCHEME_NAMES", txtGroupName.getText());
            viewMap.put(CommonConstants.MAP_NAME, "getChittalSubNo");
            viewMap.put(CommonConstants.MAP_WHERE, whereMap);
        } else if (currField.equalsIgnoreCase("MEMBER_NO")) {
            HashMap userMap = new HashMap();
            
            if(NEW_EDIT!="" && NEW_EDIT=="Edit")
            {
                System.out.println("PRODUCT_ID"+PROD_ID);
                userMap.put("PROD_ID", observable.getProdId());
            }
            else
            {
                System.out.println("PROD_ID"+PROD_ID);
                userMap.put("PROD_ID", PROD_ID);
            }
            List userList = ClientUtil.executeQuery("getUserAllowedDetails", userMap);
            //  PROD_ID="";
            String A = "";
            String B = "";
            String C = "";
            boolean AB = false;
            boolean AC = false;
            boolean BC = false;
            boolean done = false;

            if (!userList.isEmpty()) {
                userMap = (HashMap) userList.get(0);
                System.out.println("userMap" + userMap);
                if (userMap.get("ONLY_MEMBER").toString().equals("Y")) {
                    HashMap where = new HashMap();

                    if (userMap.get("ACCEPT_TYPE_CLASSA") != null && userMap.get("ACCEPT_TYPE_CLASSA").toString().equals("A")) {
                        A = "A";
                    }
                    if (userMap.get("ACCEPT_TYPE_CLASSB") != null && userMap.get("ACCEPT_TYPE_CLASSB").toString().equals("B")) {
                        B = "B";
                    }
                    if (userMap.get("ACCEPT_TYPE_CLASSC") != null && userMap.get("ACCEPT_TYPE_CLASSC").toString().equals("C")) {
                        C = "C";
                    }
                    if (A.equals("A") & B.equals("B")) {
                        AB = true;
                    }

                    if (A.equals("A") & B.equals("C")) {
                        AC = true;
                    }

                    if (A.equals("B") & B.equals("C")) {
                        BC = true;
                    }

                    if (AB) {
                        viewMap.put(CommonConstants.MAP_NAME, "getShareMemeberDetails");
                        where.put("SHARE_TYPE", "AA");
                        where.put("SHARE_TYPE1", "BB");
                        viewMap.put(CommonConstants.MAP_WHERE, where);
                        done = true;
                        System.out.println("1");
                    } else if (AC) {
                        viewMap.put(CommonConstants.MAP_NAME, "getShareMemeberDetails");
                        where.put("SHARE_TYPE", "AA");
                        where.put("SHARE_TYPE1", "CC");
                        viewMap.put(CommonConstants.MAP_WHERE, where);
                        done = true;
                        System.out.println("2");
                    } else if (BC) {
                        viewMap.put(CommonConstants.MAP_NAME, "getShareMemeberDetails");
                        where.put("SHARE_TYPE", "BB");
                        where.put("SHARE_TYPE1", "CC");
                        viewMap.put(CommonConstants.MAP_WHERE, where);
                        done = true;
                        System.out.println("3");
                    } else if (userMap.get("ACCEPT_TYPE_CLASSA") != null && userMap.get("ACCEPT_TYPE_CLASSA").toString().equals("A")) {
                        viewMap.put(CommonConstants.MAP_NAME, "getShareMemeberDetails");
                        where.put("SHARE_TYPE", "AA");
                        viewMap.put(CommonConstants.MAP_WHERE, where);
                        System.out.println("4");
                    } else if (userMap.get("ACCEPT_TYPE_CLASSB") != null && userMap.get("ACCEPT_TYPE_CLASSB").toString().equals("B")) {
                        viewMap.put(CommonConstants.MAP_NAME, "getShareMemeberDetails");
                        where.put("SHARE_TYPE", "BB");
                        viewMap.put(CommonConstants.MAP_WHERE, where);
                        System.out.println("5");
                    } else if (userMap.get("ACCEPT_TYPE_CLASSC") != null && userMap.get("ACCEPT_TYPE_CLASSC").toString().equals("C")) {
                        viewMap.put(CommonConstants.MAP_NAME, "getShareMemeberDetails");
                        where.put("SHARE_TYPE", "CC");
                        viewMap.put(CommonConstants.MAP_WHERE, where);
                        System.out.println("6");
                    } else if (userMap.get("ACCEPT_TYPE_CLASSALL") != null && userMap.get("ACCEPT_TYPE_CLASSALL").toString().equals("D")) {
                        HashMap supMap = new HashMap();
                        supMap.put("GROUP_NAME", CommonUtil.convertObjToStr(txtGroupName.getText()));
                        supMap.put("GROUP_NO", observable.getgDSNo());
                        List schemeList = ClientUtil.executeQuery("getGDSScheme", supMap);                       
                        if (schemeList != null && schemeList.size() > 0) {
                            HashMap scheme = (HashMap) schemeList.get(0);
                            supMap.put("SCHEME_NAME", CommonUtil.convertObjToStr(scheme.get("SCHEME_NAME")));
                        }
                        List lstSupName = ClientUtil.executeQuery("getOnlyMemberOrNot", supMap);
                        HashMap supMapData = new HashMap();
                        if (lstSupName != null && lstSupName.size() > 0) {
                            supMapData = (HashMap) lstSupName.get(0);
                        }
                        if( supMapData.containsKey("ONLY_MEMBER")    &&  supMapData.get("ONLY_MEMBER") != null){
                        popData = CommonUtil.convertObjToStr(supMapData.get("ONLY_MEMBER"));
                        }
                        
                        if (popData.equalsIgnoreCase("Y")) {
                           viewMap.put(CommonConstants.MAP_NAME, "getMemeberShipDetailsWhileNotMember");
                        } else {
                         viewMap.put(CommonConstants.MAP_NAME, "getMemeberShipDetails");
                        }
                        System.out.println("7");
                        } 
                    } else if (userMap.get("ONLY_MEMBER").toString().equals("N")) {
                    viewMap.put(CommonConstants.MAP_NAME, "getAllMemeberDetails");
                } else {
                }

            }
        } else if (currField.equals("Enquirystatus")) {
            viewMap.put(CommonConstants.MAP_NAME, "getSelectRecordForEnquiryDetails");
        } else if (currField == "CREDIT_ACC_NO") {
            viewMap = new HashMap();
            String prodType = ((ComboBoxModel) cboProdType.getModel()).getKeyForSelected().toString();
            if (!prodType.equals("GL")) {
                viewMap.put(CommonConstants.MAP_NAME, "Cash.getAccountList"
                        + ((ComboBoxModel) cboProdType.getModel()).getKeyForSelected().toString());
            } else {
                viewMap.put(CommonConstants.MAP_NAME, "Cash.getSelectAcctHead");
            }
            HashMap whereMap = new HashMap();
            if (cboProdId.getModel() != null && cboProdId.getModel().getSize() > 0) {
                whereMap.put("PROD_ID", ((ComboBoxModel) cboProdId.getModel()).getKeyForSelected());
            }
            System.out.println("SELECTED_BRANCH######"+whereMap.get("SELECTED_BRANCH"));
            System.out.println("getSelectedBranchID######"+getSelectedBranchID());
            //if (whereMap.get("SELECTED_BRANCH") == null) {
            //    whereMap.put("SELECTED_BRANCH", ProxyParameters.BRANCH_ID);
            //} else {
                whereMap.put("SELECTED_BRANCH", getSelectedBranchID());
           // }
            viewMap.put(CommonConstants.MAP_WHERE, whereMap);
        }
       // viewMap.put("RECORD_KEY", observable.getTxtChittalNo());
        
       //System.out.println("RECORD_KEY"+viewMap.get("RECORD_KEY"));
        new ViewAll(this, viewMap).show();
    }

    /**
     * This method helps in filling the data frm the data base to respective txt
     * fields
     *
     * @param obj param The selected data from the viewAll() is passed as a
     * param
     */
    public void fillData(Object map) {
        HashMap hash = (HashMap) map;
        HashMap viewMap1 = new HashMap();
        System.out.println("map...." + map);
        prizedMembers = (HashMap) map;
         if (hash.containsKey("NEW_FROM_AUTHORIZE_LIST_UI")) {
            fromNewAuthorizeUI = true;
            newauthorizeListUI = (NewAuthorizeListUI) hash.get("PARENT");
            hash.remove("PARENT");
            viewType = AUTHORIZE;
            observable.setActionType(ClientConstants.ACTIONTYPE_AUTHORIZE);
            observable.setStatus();
            transactionUI.setCallingUiMode(ClientConstants.ACTIONTYPE_AUTHORIZE);
            btnSave.setEnabled(false);
            btnCancel.setEnabled(true);
            btnAuthorize.setEnabled(true);
            btnReject.setEnabled(false);
            rejectFlag = 1;
        }
        if (hash.containsKey("FROM_AUTHORIZE_LIST_UI")) {
            fromAuthorizeUI = true;
            authorizeListUI = (AuthorizeListUI) hash.get("PARENT");
            hash.remove("PARENT");
            viewType = AUTHORIZE;
            observable.setActionType(ClientConstants.ACTIONTYPE_AUTHORIZE);
            observable.setStatus();
            transactionUI.setCallingUiMode(ClientConstants.ACTIONTYPE_AUTHORIZE);
            btnSave.setEnabled(false);
            btnCancel.setEnabled(true);
            btnAuthorize.setEnabled(true);
            btnReject.setEnabled(false);
            rejectFlag = 1;
        }
        if (hash.containsKey("FROM_CASHIER_AUTHORIZE_LIST_UI")) {
            fromCashierAuthorizeUI = true;
            CashierauthorizeListUI = (AuthorizeListCreditUI) hash.get("PARENT");
            hash.remove("PARENT");
            viewType = AUTHORIZE;
            observable.setActionType(ClientConstants.ACTIONTYPE_AUTHORIZE);
            observable.setStatus();
            transactionUI.setCallingUiMode(ClientConstants.ACTIONTYPE_AUTHORIZE);
            btnSave.setEnabled(false);
            btnCancel.setEnabled(true);
            btnAuthorize.setEnabled(true);
            btnReject.setEnabled(false);
            rejectFlag = 1;
        }
         if (hash.containsKey("FROM_MANAGER_AUTHORIZE_LIST_UI")) {
            fromManagerAuthorizeUI = true;
            ManagerauthorizeListUI = (AuthorizeListDebitUI) hash.get("PARENT");
            hash.remove("PARENT");
            viewType = AUTHORIZE;
            observable.setActionType(ClientConstants.ACTIONTYPE_AUTHORIZE);
            observable.setStatus();
            transactionUI.setCallingUiMode(ClientConstants.ACTIONTYPE_AUTHORIZE);
            btnSave.setEnabled(false);
            btnCancel.setEnabled(true);
            btnAuthorize.setEnabled(true);
            btnReject.setEnabled(false);
            rejectFlag = 1;
        }
        System.out.println("hash...." + hash);
        
      //  observable.setSchemeName(CommonUtil.convertObjToStr(hash.get("SCHEME_NAME")));
        //System.out.println("Observable SchemeName"+observable.getSchemeName());
        int remitProduBranchRow = 0;
        if (viewType != null) {
            if (viewType.equals("Edit") || viewType.equals(AUTHORIZE) || viewType.equals("Enquirystatus") || viewType.equals("Delete")) {
                isFilled = true;
                hash.put(CommonConstants.MAP_WHERE, hash.get("PRODUCT ID"));
                System.out.println("mmmmnnnnnbbb");
                if (observable.populateData(hash, nomineeUi.getNomineeOB())) {
                }
                transactionUI.okAction(true);
                //                observable.setTxtProductIdGR((String) hash.get("PRODUCT ID"));
                if (viewType.equals("Delete") || viewType.equals(AUTHORIZE) || viewType.equals("Enquirystatus")) {
                    setButtonEnableDisable();
                    ClientUtil.enableDisable(this, false);
                }
                if (viewType == AUTHORIZE) {
                    btnAuthorize.setEnabled(observable.getActionType() == ClientConstants.ACTIONTYPE_AUTHORIZE ? true : false);
                    btnReject.setEnabled(observable.getActionType() == ClientConstants.ACTIONTYPE_REJECT ? true : false);
                    btnException.setEnabled(observable.getActionType() == ClientConstants.ACTIONTYPE_EXCEPTION ? true : false);
                    btnAuthorize.setEnabled(true);
                    btnAuthorize.requestFocusInWindow();
                }
                if (viewType.equalsIgnoreCase("Edit")) {
                    //Added By Suresh
                    displayTransDetail(hash);
                    ClientUtil.enableDisable(this, true);
                    setButtonEnableDisable();
                    setDelBtnEnableDisable(true);
                    setEnableDisableLapsedBtn();
                } else {
                    ClientUtil.enableDisable(this, false);
                }
                txtGroupName.setEnabled(false);
                btnGroupName.setEnabled(false);
                txtDivisionNo.setEnabled(false);
                txtGDSNo.setEnabled(false);
                tdtChitStartDt.setEnabled(false);
                tdtChitEndDt.setEnabled(false);
                txtInstAmt.setEnabled(false);
            } else if (viewType.equalsIgnoreCase("CHITTAL_SUB_NO")) {
                HashMap whereMap = new HashMap();
                whereMap.put("GROUP_NAME", txtGroupName.getText());
                whereMap.put("CHITTAL_NO", hash.get("CHITTAL_NO"));
                List countList = ClientUtil.executeQuery("getCountDetailsForCoChittal", whereMap);
                if (countList != null && countList.size() > 0) {
                    whereMap = (HashMap) countList.get(0);
                    int count = CommonUtil.convertObjToInt(whereMap.get("COUNT"));
                    if (count < noOfChittals) {
                        int subNo = 0;
                        txtGDSNo.setText(CommonUtil.convertObjToStr(hash.get("CHITTAL_NO")));
                        subNo = CommonUtil.convertObjToInt(hash.get("MAX_SUB"));
                        subNo += 1;
                        txtSubNo.setText(String.valueOf(subNo));
                        observable.setTxtChittalNo(CommonUtil.convertObjToStr(hash.get("CHITTAL_NO")));
                        observable.setTxtSubNo(String.valueOf(subNo));
                        observable.setUpdateChitNo("N");
                    } else {
                        observable.setUpdateChitNo("Y");
                        ClientUtil.showMessageWindow("Co-Chittal " + noOfChittals + " Only Allowed !!!");
                        return;
                    }
                }
            } else if (viewType.equalsIgnoreCase("GROUP_DETAILS")) {  
                int totGDSAppEntryAllow=(CommonUtil.convertObjToInt(hash.get("SCHEME_COUNT")))*(CommonUtil.convertObjToInt(hash.get("TOTAL_NO_OF_MEMBERS")));
                
                String subStrToCompare=(CommonUtil.convertObjToStr(hash.get("BRANCH_ID")))+(CommonUtil.convertObjToStr(hash.get("GROUP_NO")));
                 System.out.println("subStrToCompare :"+subStrToCompare);
                hash.put("subStrToCompare",subStrToCompare);
                System.out.println("totGDSAppEntryAllow :"+totGDSAppEntryAllow);
                List countList = (List) ClientUtil.executeQuery("getGDSMaxLimitList", hash);
                HashMap countMap=(HashMap)countList.get(0);
                System.out.println("countList :"+countList);
                int currentGDSAppEntry=CommonUtil.convertObjToInt(countMap.get("COUNT(SCHEME_NAME)"));
                
                 System.out.println("currentGDSAppEntry :"+currentGDSAppEntry);
                if(totGDSAppEntryAllow>currentGDSAppEntry)
                {
                txtGroupName.setText(CommonUtil.convertObjToStr(hash.get("GROUP_NAME")));
                PROD_ID = CommonUtil.convertObjToStr(hash.get("PROD_ID"));
                System.out.println("prodi PROD_ID " + PROD_ID + " PROD_ID: " + hash.get("PROD_ID") + " BRANCH_ID: "+hash.get("BRANCH_ID") + "  "+hash.get("GROUP_NO") + " NEXT_GDS_NO: "+hash.get("NEXT_GDS_NO") + " NO_OF_DIVISIONS: "+hash.get("NO_OF_DIVISIONS"));
                DEPOSIT_AMT=CommonUtil.convertObjToStr(hash.get("DEPOSIT_AMT"));
                observable.setInstallAmount(DEPOSIT_AMT);
                observable.setgDSNo(CommonUtil.convertObjToStr(hash.get("GROUP_NO")));
                observable.setProdId(CommonUtil.convertObjToStr(hash.get("PROD_ID")));
                observable.setSchemeStartDt(CommonUtil.convertObjToStr(hash.get("SCHEME_START_DT")));
                observable.setSchemeEndDt(CommonUtil.convertObjToStr(hash.get("SCHEME_END_DT")));
                observable.setNextGDSNo(CommonUtil.convertObjToStr(hash.get("NEXT_GDS_NO")));
                observable.setTxtDivisionNo(CommonUtil.convertObjToStr(hash.get("NO_OF_DIVISIONS")));
                observable.setTotalNoOfMembers(CommonUtil.convertObjToStr(hash.get("TOTAL_NO_OF_MEMBERS")));
                observable.setBranchId(CommonUtil.convertObjToStr(hash.get("BRANCH_ID")));
                observable.setSchemeCount(CommonUtil.convertObjToStr(hash.get("SCHEME_COUNT")));
                
                observable.setTxtGroupName(txtGroupName.getText());
                txtDivisionNo.setEnabled(false);
                tdtChitStartDt.setEnabled(false);
                tdtChitEndDt.setEnabled(false);
                txtInstAmt.setEnabled(false);
                txtGDSNo.setEnabled(false);
               // viewMap1.put(CommonConstants.MAP_NAME, "getGDSSelectEachSchemeNotEnteredDetails");
                getMemberGDSNo(hash);
//                String nextGenNo=CommonUtil.convertObjToStr(hash.get("NEXT_GDS_NO"));
//                String gDSNoInString=CommonUtil.lpad(nextGenNo, 5, '0');
//                gDSNo=CommonUtil.convertObjToStr(hash.get("BRANCH_ID"))+CommonUtil.convertObjToStr(hash.get("GROUP_NO"))+CommonUtil.convertObjToStr(gDSNoInString);                //gDSNo=observable.getGDSNo(viewMap1);
                System.out.println("MEMBERGROUPNO "+observable.getTxtGDSNo());
                txtDivisionNo.setText(CommonUtil.convertObjToStr(hash.get("NO_OF_DIVISIONS")));
                txtGDSNo.setText(CommonUtil.convertObjToStr(observable.getTxtGDSNo()));
                txtInstAmt.setText(CommonUtil.convertObjToStr(hash.get("DEPOSIT_AMT")));
                tdtChitStartDt.setDateValue(CommonUtil.convertObjToStr(hash.get("SCHEME_START_DT")));
                tdtChitEndDt.setDateValue(CommonUtil.convertObjToStr(hash.get("SCHEME_END_DT")));
                //                txtDivisionNo.setText(CommonUtil.convertObjToStr(hash.get("NO_OF_DIVISIONS")));
                
                if (hash.get("MULTIPLE_MEMBER").equals("Y") && !hash.get("MULTIPLE_MEMBER").equals("")) {
                    double instAmount = 0.0;
                    noOfChittals = CommonUtil.convertObjToInt(hash.get("NO_OF_CHITTALS"));
                    chkCoChittal.setEnabled(true);
                    observable.setMultipleMember("Y");
                    txtDivisionNo.setText("1");
                    observable.setTxtDivisionNo("1");
//                    instAmount = CommonUtil.convertObjToDouble(hash.get("INSTALLMENT_AMOUNT")).doubleValue()/CommonUtil.convertObjToDouble(hash.get("NO_OF_CHITTALS")).doubleValue();
//                    hash.put("INSTALLMENT_AMOUNT",String.valueOf(instAmount));
                } else {
                    chkCoChittal.setEnabled(false);
                    observable.setMultipleMember("N");
                }
                instAmt = CommonUtil.convertObjToDouble(hash.get("DEPOSIT_AMT")).doubleValue();
                txtInstAmt.setText(CommonUtil.convertObjToStr(hash.get("DEPOSIT_AMT")));
                HashMap chitalMap =observable.getChittalNo();
                //  observable.getProductMapForGroup();
                System.out.println("###########FLAGchitalMap"+chitalMap);
                //    String cN = observable.getChittalNo();
                txtDivisionNo.setText(CommonUtil.convertObjToStr(observable.getTxtDivisionNo()));
                observable.setTxtDivisionNo(txtDivisionNo.getText());
                txtGroupName.setEnabled(false);
                //call these in save //Shany
                productMap = new HashMap();
                productMap = observable.getProductMap();
                if (productMap.get("BONUS_FIRST_INSTALLMENT").equals("Y") && !productMap.get("BONUS_FIRST_INSTALLMENT").equals("")) {
                    System.out.println("executing here for BONUS_FIRST_INSTALLMENT");
                    double commisionRateAmt = 0.0;
                    double installment_amt = 0.0;
                    double trans_amt = 0.0;
                    double auction_maxamt = 0.0;
                    commisionRateAmt = CommonUtil.convertObjToDouble(productMap.get("COMMISION_RATE_AMT")).doubleValue();
                            String commisionType = CommonUtil.convertObjToStr(productMap.get("COMMISION_RATE_TYPE"));
                            auction_maxamt = CommonUtil.convertObjToDouble(productMap.get("AUCTION_MAXAMT")).doubleValue();
                            installment_amt = CommonUtil.convertObjToDouble(hash.get("DEPOSIT_AMT")).doubleValue();
                            if (commisionType != null && !commisionType.equals("") && commisionType.equals("Percent")) {
                                trans_amt = installment_amt - (installment_amt * (auction_maxamt - commisionRateAmt) / 100);
                            } else {
                                trans_amt = installment_amt - (installment_amt * auction_maxamt / 100);
                                trans_amt -= commisionRateAmt;
                            }
                            System.out.println("trans_amt ... "+ trans_amt);
                            transactionUI.setCallingAmount(String.valueOf(trans_amt));
                }
                observable.setTdtChitStartDt(tdtChitStartDt.getDateValue());
                observable.setTdtChitEndDt(tdtChitEndDt.getDateValue());
                observable.setTxtInstAmt(txtInstAmt.getText());
                //HashMap isTranMap= new HashMap();
                HashMap schNamMap = new HashMap();
                if (hash.containsKey("GROUP_NO")&&hash.get("GROUP_NO")!=null) {
                    schNamMap.put("GROUP_NO", hash.get("GROUP_NO"));
                }
                HashMap isTranMap = new HashMap();
                List isTranList = ClientUtil.executeQuery("getGDSSelIsTran", schNamMap);
                System.out.println("isTranListisTranListisTranList" + isTranList.size());
                if (isTranList != null && isTranList.size() > 0) {
                    isTranMap = (HashMap) isTranList.get(0);
                    isTran = isTranMap.get("TRANS_FIRST_INSTALLMENT").toString();
                    System.out.println("isTranisTranisTranisTran" + isTran);
                }
                System.out.println("rishasssss"+isTran);
                if (isTran.equals("Y")) {
                    System.out.println("inside trans");
                    if(chitalMap.containsKey("TRANS_AMT")&&chitalMap.get("TRANS_AMT")!=null)
                    {
                    transactionUI.setCallingAmount(CommonUtil.convertObjToStr(chitalMap.get("TRANS_AMT")));
                    }
                    else
                    {
                      transactionUI.setCallingAmount(txtInstAmt.getText());
                    }
                    // added By Suresh
                 //boolean flag =observable.getChittalNo();
                    //start of comment
//                    if (productMap.get("BONUS_FIRST_INSTALLMENT").equals("Y") && !productMap.get("BONUS_FIRST_INSTALLMENT").equals("")) {
//                        double auction_maxamt = 0.0;
//                        double installment_amt = 0.0;
//                        double trans_amt = 0.0;
//                        double commisionRateAmt = 0.0;
//                        HashMap predefinedMap = new HashMap();
//                        if(productMap.get("PREDEFINITION_INSTALLMENT")!=null && !productMap.get("PREDEFINITION_INSTALLMENT").equals("")
//                            && productMap.get("PREDEFINITION_INSTALLMENT").equals("Y")){
//                            
//                            List predefinedList = ClientUtil.executeQuery("getFirstPredefinedInstallment", productMap);
//                            if (predefinedList != null && predefinedList.size() > 0) {
//                                predefinedMap = (HashMap) predefinedList.get(0);
//                                //observable.setPredefinedInstallBonus(CommonUtil.convertObjToDouble(predefinedMap.get("NEXT_BONUS_AMOUNT")));
//                                trans_amt = CommonUtil.convertObjToDouble(txtInstAmt.getText())-CommonUtil.convertObjToDouble(predefinedMap.get("NEXT_BONUS_AMOUNT"));
//                                transactionUI.setCallingAmount(String.valueOf(trans_amt));
//                                System.out.println("trans_amt$&&$&$&"+trans_amt);
//                            }else{
//                                commisionRateAmt = CommonUtil.convertObjToDouble(productMap.get("COMMISION_RATE_AMT")).doubleValue();
//                                String commisionType = CommonUtil.convertObjToStr(productMap.get("COMMISION_RATE_TYPE"));
//                                auction_maxamt = CommonUtil.convertObjToDouble(productMap.get("AUCTION_MAXAMT")).doubleValue();
//                                installment_amt = CommonUtil.convertObjToDouble(hash.get("INSTALLMENT_AMOUNT")).doubleValue();
//                                if (commisionType != null && !commisionType.equals("") && commisionType.equals("Percent")) {
//                                    trans_amt = installment_amt - (installment_amt * (auction_maxamt - commisionRateAmt) / 100);
//                                } else {
//                                    trans_amt = installment_amt - (installment_amt * auction_maxamt / 100);
//                                    trans_amt -= commisionRateAmt;
//                                }
//                                transactionUI.setCallingAmount(String.valueOf(trans_amt));
//                            }
//                        }else{                            
//                            commisionRateAmt = CommonUtil.convertObjToDouble(productMap.get("COMMISION_RATE_AMT")).doubleValue();
//                            String commisionType = CommonUtil.convertObjToStr(productMap.get("COMMISION_RATE_TYPE"));
//                            auction_maxamt = CommonUtil.convertObjToDouble(productMap.get("AUCTION_MAXAMT")).doubleValue();
//                            installment_amt = CommonUtil.convertObjToDouble(hash.get("INSTALLMENT_AMOUNT")).doubleValue();
//                            if (commisionType != null && !commisionType.equals("") && commisionType.equals("Percent")) {
//                                trans_amt = installment_amt - (installment_amt * (auction_maxamt - commisionRateAmt) / 100);
//                            } else {
//                                trans_amt = installment_amt - (installment_amt * auction_maxamt / 100);
//                                trans_amt -= commisionRateAmt;
//                            }
//                            transactionUI.setCallingAmount(String.valueOf(trans_amt));
//                        }
//                    } else {
//                        transactionUI.setCallingAmount(CommonUtil.convertObjToStr(txtInstAmt.getText()));
//                    }
//                    
                    //end of comment
                     List schemeList = ClientUtil.executeQuery("getGDSScheme", hash);
                     int schemeCount=schemeList.size();
                     if (schemeList != null && schemeList.size() > 0) {
                        HashMap scheme = (HashMap) schemeList.get(0);
                        hash.put("SCHEME_NAME",CommonUtil.convertObjToStr(scheme.get("SCHEME_NAME")));
                     }   
                     System.out.println("hash;;;;;;;; :: " + hash);
                    //GROUP_NO=H00289
                    
                                       
                    
                   transactionUI.setSchemeName(CommonUtil.convertObjToStr(hash.get("SCHEME_NAME")));
                } else {
                    transactionUI.setButtonEnableDisable(false);
                    // transactionUI.s
                    //panInsideGeneralRemittance1.setEnabled(false);
                }
                btnMembershipNo.setEnabled(true);
                setAppDateAndNo();
            }
                else{
                 ClientUtil.showAlertWindow("Member Limit Exceeded"); 
                }
                
            } else if (viewType.equalsIgnoreCase("MEMBER_NO")) {
                productMap = new HashMap();
                productMap = observable.getProductMap();
                HashMap hmap = new HashMap();
                hmap.put("MEMBER_NO", hash.get("MEMBERSHIP_NO"));
                hmap.put("CUST_ID", hash.get("CUST_ID"));
                List lst1 = ClientUtil.executeQuery("getDeathDetailsForAcsOpening", hmap);
                if (lst1 != null && lst1.size() > 0) {
                    ClientUtil.displayAlert("Customer is death marked please select another customerId");
                    txtMembershipNo.setText("");
                    return;
                }
                String onlyMember = CommonUtil.convertObjToStr(productMap.get("ONLY_MEMBER"));
                String classA = CommonUtil.convertObjToStr(productMap.get("ACCEPT_TYPE_CLASSA"));
                String classB = CommonUtil.convertObjToStr(productMap.get("ACCEPT_TYPE_CLASSB"));
                String classC = CommonUtil.convertObjToStr(productMap.get("ACCEPT_TYPE_CLASSC"));
                String classALL = CommonUtil.convertObjToStr(productMap.get("ACCEPT_TYPE_CLASSALL"));
                boolean flag = false;
                System.out.println("ABC " + classA + " " + classB + " " + classC + " " + classALL + "  " + hash.get("MEMBERSHIP_CLASS"));
//                if((classA!=null  && !classA.equals("") && classB!=null && !classB.equals("")) || (classB!=null  && !classB.equals("") && classC!=null && !classC.equals("")) ||
//                (classC!=null  && !classC.equals("") && classA!=null && !classA.equals(""))){
////                    if(classA!=null  && !classA.equals("") && classB!=null && !classB.equals("") &&
////                    !classA.equals(hash.get("MEMBERSHIP_CLASS")) && !classB.equals(hash.get("MEMBERSHIP_CLASS"))){
////                        ClientUtil.showAlertWindow("This Member Type not allowed under this scheme");
////                        clearingMemberField();
////                        return;
////                    }else if(classB!=null  && !classB.equals("") && classC!=null && !classC.equals("") &&
////                    !classB.equals(hash.get("MEMBERSHIP_CLASS")) && !classC.equals(hash.get("MEMBERSHIP_CLASS"))){
////                        ClientUtil.showAlertWindow("This Member Type not allowed under this scheme");
////                        clearingMemberField();
////                        return;
////                    }else if(classC!=null  && !classC.equals("") && classA!=null && !classA.equals("") &&
////                    !classC.equals(hash.get("MEMBERSHIP_CLASS")) && !classA.equals(hash.get("MEMBERSHIP_CLASS"))){
////                        ClientUtil.showAlertWindow("This Member Type not allowed under this scheme");
////                        clearingMemberField();
////                        return;
////                    }else 
                {
                    observable.setTxtMembershipNo(CommonUtil.convertObjToStr(hash.get("MEMBERSHIP_NO")));
                    observable.setTxtMembershipType(CommonUtil.convertObjToStr(hash.get("MEMBERSHIP_CLASS")));
                    observable.setTxtMembershipName(CommonUtil.convertObjToStr(hash.get("CUSTOMER")));
                    observable.setCust_id(CommonUtil.convertObjToStr(hash.get("CUST_ID")));
                    cust_id = observable.getCust_id();
                    txtMembershipNo.setText(observable.getTxtMembershipNo());
                    txtMembershipName.setText(observable.getTxtMembershipName());
                    lblMembershipTypeValue.setText(observable.getTxtMembershipType());
                    observable.getCustomerAddressDetails(CommonUtil.convertObjToStr(hash.get("CUST_ID")));
                }
//              
                {
                    observable.setTxtMembershipNo(CommonUtil.convertObjToStr(hash.get("MEMBERSHIP_NO")));
                    observable.setTxtMembershipType(CommonUtil.convertObjToStr(hash.get("MEMBERSHIP_CLASS")));
                    observable.setTxtMembershipName(CommonUtil.convertObjToStr(hash.get("CUSTOMER")));
                    observable.setCust_id(CommonUtil.convertObjToStr(hash.get("CUST_ID")));
                    cust_id = observable.getCust_id();
                    txtMembershipNo.setText(observable.getTxtMembershipNo());
                    txtMembershipName.setText(observable.getTxtMembershipName());
                    lblMembershipTypeValue.setText(observable.getTxtMembershipType());
                    observable.getCustomerAddressDetails(CommonUtil.convertObjToStr(hash.get("CUST_ID")));
                }
//                }
                if (txtMembershipName.getText().length() > 0) {
                    transactionUI.setCallingApplicantName(txtMembershipName.getText());
                }
            } else if (viewType.equals("CREDIT_ACC_NO")) {
                String prodType = ((ComboBoxModel) cboProdType.getModel()).getKeyForSelected().toString();
                if (prodType != null && !prodType.equals("GL")) {
                    if (prodType.equals("TD")) {
                        hash.put("ACCOUNTNO", hash.get("ACCOUNTNO") + "_1");
                    }
                    txtCustomerIdCr.setText(CommonUtil.convertObjToStr(hash.get("ACCOUNTNO")));
                    observable.setTxtCustomerIdCr(CommonUtil.convertObjToStr(hash.get("ACCOUNTNO")));
                     // Added by nithya on 29-03-2017 for 6095
                    if(hash.containsKey("CUSTOMERNAME") && hash.get("CUSTOMERNAME") != null && !hash.get("CUSTOMERNAME").equals("") && CommonUtil.convertObjToStr(hash.get("CUSTOMERNAME")).length() > 0){
                        lblActHolderName.setText(CommonUtil.convertObjToStr(hash.get("CUSTOMERNAME")));
                        lblActHolderName.setForeground(Color.blue);
                    }
                } else {
                    txtCustomerIdCr.setText(CommonUtil.convertObjToStr(hash.get("A/C HEAD")));
                    observable.setTxtCustomerIdCr(CommonUtil.convertObjToStr(hash.get("A/C HEAD")));
                }
            }
            if (observable.getChkNominee() == true) {
                //            tabTermDeposit.add(nomineeUi);
                tabRemittanceProduct.add(nomineeUi, "Nominee");
                tabRemittanceProduct.resetVisits();
                //            ClientUtil.enableDisable(nomineeUi, true);
                nomineeUi.enableDisableNominee_SaveDelete();
                /**
                 * * TO get the Max of the deleted Nominee(s) for the particular
                 * Account-Holder...
                 */
                nomineeUi.callMaxDel(txtGDSNo.getText());
                nomineeUi.resetNomineeTab();
                nomineeUi.setActionType(observable.getActionType());
                if (observable.getActionType() == ClientConstants.ACTIONTYPE_VIEW
                        || observable.getActionType() == ClientConstants.ACTIONTYPE_VIEW_MODE
                        || observable.getActionType() == ClientConstants.ACTIONTYPE_AUTHORIZE
                        || observable.getActionType() == ClientConstants.ACTIONTYPE_REJECT) {
                    ClientUtil.enableDisable(nomineeUi, false);
                } else {
                    ClientUtil.enableDisable(nomineeUi, true);
                }
            }
            observable.setStatus();
            lblStatus.setText(observable.getLblStatus());
        }
        //__ To Save the data in the Internal Frame...
        if(observable.getActionType() == ClientConstants.ACTIONTYPE_AUTHORIZE){
	        btnAuthorize.setEnabled(true);
	        btnAuthorize.requestFocusInWindow();
       }
        setModified(true);
    }

    private void displayAlert(String message) {
        final CMandatoryDialog cmd = new CMandatoryDialog();
        cmd.setMessage(message);
        cmd.setModal(true);
        cmd.show();
    }

    private void savePerformed() {
        //System.out.println("jibyjibyjibyjibyjiby");
        updateOBFields();
        boolean productID = false;
        // If the Action Type is  NEW or EDIT, Check for the Uniqueness of Product ID and Product Description
        //System.out.println("observable.getActionType() : " + observable.getActionType());
        //System.out.println("productID : " + productID);
        //        if(observable.getActionType() == ClientConstants.ACTIONTYPE_NEW || observable.getActionType() == ClientConstants.ACTIONTYPE_EDIT || observable.getActionType() == ClientConstants.ACTIONTYPE_COPY){
        //            productID = observable.uniqueProduct();
        //        }
        String mandatoryMessage = new MandatoryCheck().checkMandatory(getClass().getName(), panInsideGeneralRemittance);
        if (observable.getActionType() != ClientConstants.ACTIONTYPE_DELETE && mandatoryMessage.length() > 0) {
            displayAlert(mandatoryMessage);
        } else {

            if (productID == false) {
                //added by rishad for avoiding doubling issue at 05/08/2015
                CommonUtil comm = new CommonUtil();
                final JDialog loading = comm.addProgressBar();
                SwingWorker<Void, Void> worker = new SwingWorker<Void, Void>() {

                    @Override
                    protected Void doInBackground() throws InterruptedException /** Execute some operation */
                    {
                        try {
                            observable.doSave(nomineeUi.getNomineeOB());
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        return null;
                    }

                    @Override
                    protected void done() {
                        loading.dispose();
                    }
                };
                worker.execute();
                loading.show();
                try {
                    worker.get();
                } catch (Exception e1) {
                    e1.printStackTrace();
                }
                //__ if the Action is not Falied, Reset the fields...
                if (observable.getResult() != ClientConstants.ACTIONTYPE_FAILED) {
                    if (observable.getProxyReturnMap().containsKey("TRANS_ID")) {
                        displayTransDetail(observable.getProxyReturnMap());
                    }
                    HashMap lockMap = new HashMap();
                    ArrayList lst = new ArrayList();
                    lst.add("PRODUCT ID");
                    lockMap.put("RECORD_KEY", txtGDSNo.getText());
                    setEditLockMap(lockMap);
                    setEditLock();
                    observable.resetOBFields();
                    btnCancelActionPerformed(null);
                    remitBtnDefaultEnableDisable(false);
                    ClientUtil.enableDisable(this, false);
                    if (observable.getActionType() != ClientConstants.ACTIONTYPE_DELETE) {
                        observable.setResult(observable.getActionType());
                    }
                    observable.setResultStatus();
                    lblStatus.setText(observable.getLblStatus());
                    setButtonEnableDisable();
                    btnSave.setEnabled(false);
                    btnNew.setEnabled(true);
                }
            }
        }
    }
    //Added By Suresh

    private void displayTransDetail(HashMap proxyResultMap) {
        System.out.println("@#$@@$@@@$ proxyResultMap : " + proxyResultMap);
        HashMap getTransMap = new HashMap(); 
        getTransMap.put("BATCH_ID", proxyResultMap.get("TRANS_ID"));
        getTransMap.put("TRANS_DT", currDt);
        getTransMap.put(CommonConstants.BRANCH_ID, ProxyParameters.BRANCH_ID);
        HashMap returnMap = new HashMap();
        List transList = (List) ClientUtil.executeQuery("getTransferDetailsInvestment", getTransMap);
        if (transList != null && transList.size() > 0) {
            returnMap.put("TRANSFER_TRANS_LIST", transList);
        }
        List cashList = (List) ClientUtil.executeQuery("getCashDetails", getTransMap);
        if (cashList != null && cashList.size() > 0) {
            returnMap.put("CASH_TRANS_LIST", cashList);
            getTransMap.put("BATCH_ID", proxyResultMap.get("BONUS_TRANS_ID"));
            List transListBonus = (List) ClientUtil.executeQuery("getTransferDetails", getTransMap);
            if (transListBonus != null && transListBonus.size() > 0) {
                returnMap.put("TRANSFER_TRANS_LIST", transListBonus);
            }
        }
        displayTransDetailNew(returnMap);
        getTransMap.clear();
        getTransMap = null;
        transList = null;
        cashList = null;
        ///////
      /*  int yesNo = 0;
        String[] options = {"Yes", "No"};
        yesNo = COptionPane.showOptionDialog(null, "Do you want to print?", CommonConstants.WARNINGTITLE,
                COptionPane.OK_OPTION, COptionPane.WARNING_MESSAGE,
                null, options, options[0]);
        System.out.println("#$#$$ yesNo : " + yesNo);
        if (yesNo == 0) {
            TTIntegration ttIntgration = null;
            HashMap paramMap = new HashMap();
            paramMap.put("TransId", proxyResultMap.get("TRANS_ID"));
            paramMap.put("TransDt", currDt);
            paramMap.put("BranchId", ProxyParameters.BRANCH_ID);
            ttIntgration.setParam(paramMap);
            ttIntgration.integrationForPrint("MDSApplication", false);
        }*/
    }
  public void  displayTransDetailNew(HashMap returnMap){
         String cashDisplayStr = "Cash Transaction Details...\n";
        String transferDisplayStr = "Transfer Transaction Details...\n";
        String displayStr = "";
        String transId = "";
        String transType = "";
      //  System.out.println("jhj>>>>>>>");
        Object keys[] = returnMap.keySet().toArray();
        System.out.println("jhj>>>>>>>adad");
        //System.out.println("keeeeeeeeeeeyyy>>>>>>>>>>>"+keys[]);
        int cashCount = 0;
        int transferCount = 0;
        List tempList = null;
        HashMap transMap = null;
        String actNum = "";
        HashMap transIdMap = new HashMap();
        HashMap transTypeMap = new HashMap();
       // System.out.println("keeeeeeeeeeeyyy>>>>>>>>>>>" + keys.length);
        for (int i = 0; i < keys.length; i++) {
            System.out.println("jhj>>>>>>>adad1211222@@@@" + (returnMap.get(keys[i]) instanceof String));
            if (returnMap.get(keys[i]) instanceof String) {
          //      System.out.println("hdfdasd");
                continue;
            }

          //  System.out.println("hdfdasd@@@@@");
            tempList = (List) returnMap.get(keys[i]);
          //  System.out.println("hdfdasd@@@@@>>>>>" + tempList);
            if (CommonUtil.convertObjToStr(keys[i]).indexOf("CASH") != -1) {
              //  System.out.println("haaaiii11....>>>");
                for (int j = 0; j < tempList.size(); j++) {
                  //  System.out.println("haaaiii11....>>>aa");
                    transMap = (HashMap) tempList.get(j);
                    if (j == 0) {
                       // System.out.println("haaaiii11....>>>bb");
                        transId = (String) transMap.get("SINGLE_TRANS_ID");
                    }
                    cashDisplayStr += "Trans Id : " + transMap.get("TRANS_ID")
                            + "   Trans Type : " + transMap.get("TRANS_TYPE");
                    actNum = CommonUtil.convertObjToStr(transMap.get("ACT_NUM"));
                    if (actNum != null && !actNum.equals("")) {
                      //  System.out.println("haaaiii11....>>>cc");
                        cashDisplayStr += "   Account No : " + transMap.get("ACT_NUM")
                                + "   Amount : " + transMap.get("AMOUNT") + "\n";
                    } else {
                      //  System.out.println("haaaiii11....>>>dd");
                        cashDisplayStr += "   Ac Hd Desc : " + transMap.get("AC_HD_ID")
                                + "   Amount : " + transMap.get("AMOUNT") + "\n";
                    }
                    transTypeMap.put(transMap.get("SINGLE_TRANS_ID"), transMap.get("TRANS_TYPE"));
                    transIdMap.put(transMap.get("SINGLE_TRANS_ID"), "CASH");
                }
                cashCount++;
            } else if (CommonUtil.convertObjToStr(keys[i]).indexOf("TRANSFER") != -1) {
                //System.out.println("haaaiii22....>>>");
                for (int j = 0; j < tempList.size(); j++) {
                    transMap = (HashMap) tempList.get(j);
                    if (j == 0) {
                      //  System.out.println("haaaiii22....>>>aa");
                        transId = (String) transMap.get("SINGLE_TRANS_ID");
                    }
                    transferDisplayStr += "Trans Id : " + transMap.get("TRANS_ID")
                            + "   Batch Id : " + transMap.get("BATCH_ID")
                            + "   Trans Type : " + transMap.get("TRANS_TYPE");
                    actNum = CommonUtil.convertObjToStr(transMap.get("ACT_NUM"));
                    if (actNum != null && !actNum.equals("")) {
                       // System.out.println("haaaiii22....>>>bb");
                        transferDisplayStr += "   Account No : " + transMap.get("ACT_NUM")
                                + "   Amount : " + transMap.get("AMOUNT") + "\n";
                    } else {
                      //  System.out.println("haaaiii22....>>>cc");
                        transferDisplayStr += "   Ac Hd Desc : " + transMap.get("AC_HD_ID")
                                + "   Amount : " + transMap.get("AMOUNT") + "\n";
                    }
                    transIdMap.put(transMap.get("BATCH_ID"), "TRANSFER");
                }
                transferCount++;
            }
        }
        if (cashCount > 0) {
            displayStr += cashDisplayStr;
        }
        if (transferCount > 0) {
            displayStr += transferDisplayStr;
        }
        if (!displayStr.equals("")) {
            ClientUtil.showMessageWindow("" + displayStr);
        }
        int yesNo = 0;
        String[] options = {"Yes", "No"};
        yesNo = COptionPane.showOptionDialog(null,"Do you want to print?", CommonConstants.WARNINGTITLE,
        COptionPane.OK_OPTION, COptionPane.WARNING_MESSAGE,
        null, options, options[0]);
        System.out.println("#$#$$ yesNo : "+yesNo);
        if (yesNo==0) {
            TTIntegration ttIntgration = null;
            HashMap paramMap = new HashMap();
            //paramMap.put("TransId", transId);
            paramMap.put("TransDt", currDt);
            paramMap.put("BranchId", ProxyParameters.BRANCH_ID);
            Object keys1[] = transIdMap.keySet().toArray();
             for (int i = 0; i < keys.length; i++) {
                            paramMap.put("TransId", keys1[i]);
                            ttIntgration.setParam(paramMap);
                            //                        if (((String)TrueTransactMain.BANKINFO.get("BANK_NAME")).toUpperCase().lastIndexOf("POLPULLY")!=-1) {
                            if (CommonUtil.convertObjToStr(transIdMap.get(keys1[i])).equals("TRANSFER")) {
                                paramMap.put("TransId", transId);
                                ttIntgration.setParam(paramMap);
                                ttIntgration.integrationForPrint("MDSReceiptsTransfer");
                            } 
                            else if (CommonUtil.convertObjToStr(transTypeMap.get(keys1[i])).equals("DEBIT")) {
                                    ttIntgration.integrationForPrint("CashPayment", false);
                            }else {
                                ttIntgration.integrationForPrint("MDSReceipts", false);
                            }
                        }
//            if (((String)TrueTransactMain.BANKINFO.get("BANK_NAME")).toUpperCase().lastIndexOf("POLPULLY")!=-1) {
//                ttIntgration.integrationForPrint("ReceiptPayment");
//            
        }
        
    }
    private void setAppDateAndNo(){
        //System.out.println("curDt"+currDt);
        tdtApplnDate.setDateValue(CommonUtil.convertObjToStr(currDt));
        String chitNo=observable.getTxtChittalNo();
//        System.out.println("chitNo"+chitNo);
        if(chitNo.length()>0){
            int n=chitNo.length();
            String appno=chitNo.substring(n-3, n);
            System.out.println("appno"+appno.charAt(0));
            while(appno.charAt(0)=='0'){ 
                appno=appno.substring(1,appno.length());
                //System.out.println("appno12121"+appno);
            }
          // System.out.println("appno"+appno);
            txtApplnNo.setText(appno);
        }
        System.out.println("appnotxt"+txtApplnNo.getText());
        observable.setTxtApplnNo(txtApplnNo.getText());
        observable.setTdtApplnDate(tdtApplnDate.getDateValue());
    }

    private void setEnableDisableLapsedBtn() {
    }

    private void setEnableDisableRodLapsedBtn() {
    }

    private void enableDisableall() {
        observable.resetOBFields();
        setButtonEnableDisable();
        ClientUtil.enableDisable(this, false);
    }

    private void doaddRemittanceProductBranchTable() {
        updateOBFields();
    }

    /**
     * Adding datas from Fields of Remittance Product Branch to the CTable of
     * Remittance Product Branch
     */
    public void addRemittanceProductBranchTable() {
    }

    /**
     * Adding datas from Fields of Remittance Product Charges to the CTable of
     * Remittance Product Charges
     */
    public void addRemittanceProductChargesTable() {
        updateOBFields();
        tblChargesSelected = false;
    }

    private void resetRemitProdBranchTab() {
        updateOBFields();
    }

    /**
     * To Enable or Disable New, Edit, Save and Cancel Button
     */
    private void setButtonEnableDisable() {
        btnNew.setEnabled(!btnNew.isEnabled());
        btnEdit.setEnabled(!btnEdit.isEnabled());
        mitNew.setEnabled(btnNew.isEnabled());
        mitEdit.setEnabled(btnEdit.isEnabled());
        btnSave.setEnabled(!btnNew.isEnabled());
        btnCancel.setEnabled(!btnNew.isEnabled());
        mitSave.setEnabled(btnSave.isEnabled());
        mitCancel.setEnabled(btnCancel.isEnabled());
        btnView.setEnabled(!btnView.isEnabled());
        setDelBtnEnableDisable(false);
        setAuthBtnEnableDisable();
    }

    /**
     * To Enable or Disable Delete Button
     */
    private void setDelBtnEnableDisable(boolean enableDisable) {
        btnDelete.setEnabled(enableDisable);
        mitDelete.setEnabled(enableDisable);
    }

    /**
     * To Enable or Disable Authorize, Rejection and Exception Button
     */
    private void setAuthBtnEnableDisable() {
        final boolean enableDisable = !btnSave.isEnabled();
        btnAuthorize.setEnabled(enableDisable);
        btnException.setEnabled(enableDisable);
        btnReject.setEnabled(enableDisable);
        mitAuthorize.setEnabled(enableDisable);
        mitException.setEnabled(enableDisable);
        mitReject.setEnabled(enableDisable);
    }
    
    private void getMemberGDSNo(HashMap hash) {
        String gDSNoGen;
        String nextGenNo = CommonUtil.convertObjToStr(hash.get("NEXT_GDS_NO"));
        observable.setNextActNo(nextGenNo);
        String gDSNoInString = CommonUtil.lpad(nextGenNo, 5, '0');
        gDSNoGen = CommonUtil.convertObjToStr(hash.get("BRANCH_ID")) + CommonUtil.convertObjToStr(hash.get("GROUP_NO")) + CommonUtil.convertObjToStr(gDSNoInString);
        observable.setTxtGDSNo(CommonUtil.convertObjToStr(gDSNoGen));
        observable.setNextGDSNo(CommonUtil.convertObjToStr(CommonUtil.convertObjToInt(nextGenNo)+1));
       

        // return gDSNoGen;
    }
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private com.see.truetransact.uicomponent.CButton btnAuthorize;
    private com.see.truetransact.uicomponent.CButton btnCancel;
    private com.see.truetransact.uicomponent.CButton btnChittalNo;
    private com.see.truetransact.uicomponent.CButton btnClose;
    private com.see.truetransact.uicomponent.CButton btnCustomerIdFileOpenCr;
    private com.see.truetransact.uicomponent.CButton btnDelete;
    private com.see.truetransact.uicomponent.CButton btnEdit;
    private com.see.truetransact.uicomponent.CButton btnException;
    private com.see.truetransact.uicomponent.CButton btnGroupName;
    private com.see.truetransact.uicomponent.CButton btnMembershipNo;
    private com.see.truetransact.uicomponent.CButton btnNew;
    private com.see.truetransact.uicomponent.CButton btnPrint;
    private com.see.truetransact.uicomponent.CButton btnReject;
    private com.see.truetransact.uicomponent.CButton btnSave;
    private com.see.truetransact.uicomponent.CButton btnView;
    private com.see.truetransact.uicomponent.CComboBox cboCity;
    private com.see.truetransact.uicomponent.CComboBox cboProdId;
    private com.see.truetransact.uicomponent.CComboBox cboProdType;
    private com.see.truetransact.uicomponent.CComboBox cboState;
    private com.see.truetransact.uicomponent.CCheckBox chkCoChittal;
    private com.see.truetransact.uicomponent.CCheckBox chkMobileBankingAD;
    private com.see.truetransact.uicomponent.CCheckBox chkMunnal;
    private com.see.truetransact.uicomponent.CCheckBox chkNominee;
    private com.see.truetransact.uicomponent.CCheckBox chkStandingInstn;
    private com.see.truetransact.uicomponent.CCheckBox chkThalayal;
    private com.see.truetransact.uicomponent.CLabel lblAccountNo;
    private com.see.truetransact.uicomponent.CLabel lblActHolderName;
    private com.see.truetransact.uicomponent.CLabel lblApplnDate;
    private com.see.truetransact.uicomponent.CLabel lblApplnNo;
    private com.see.truetransact.uicomponent.CLabel lblArea;
    private com.see.truetransact.uicomponent.CLabel lblChitEndDt;
    private com.see.truetransact.uicomponent.CLabel lblChitStartDt;
    private com.see.truetransact.uicomponent.CLabel lblChittalNo;
    private com.see.truetransact.uicomponent.CLabel lblCity;
    private com.see.truetransact.uicomponent.CLabel lblCoChittal;
    private com.see.truetransact.uicomponent.CLabel lblDivisionNo;
    private com.see.truetransact.uicomponent.CLabel lblGroupName;
    private com.see.truetransact.uicomponent.CLabel lblHouseStNo;
    private com.see.truetransact.uicomponent.CLabel lblInstAmt;
    private com.see.truetransact.uicomponent.CLabel lblMembershipName;
    private com.see.truetransact.uicomponent.CLabel lblMembershipNo;
    private com.see.truetransact.uicomponent.CLabel lblMembershipType;
    private com.see.truetransact.uicomponent.CLabel lblMembershipTypeValue;
    private com.see.truetransact.uicomponent.CLabel lblMobileNo;
    private com.see.truetransact.uicomponent.CLabel lblMobileSubscribedFrom;
    private com.see.truetransact.uicomponent.CLabel lblMsg;
    private com.see.truetransact.uicomponent.CLabel lblMunnal;
    private com.see.truetransact.uicomponent.CLabel lblPin;
    private com.see.truetransact.uicomponent.CLabel lblProdId;
    private com.see.truetransact.uicomponent.CLabel lblProductType;
    private com.see.truetransact.uicomponent.CLabel lblRemarks;
    private com.see.truetransact.uicomponent.CLabel lblSalaryRecovery;
    private com.see.truetransact.uicomponent.CLabel lblSpace;
    private com.see.truetransact.uicomponent.CLabel lblSpace3;
    private com.see.truetransact.uicomponent.CLabel lblSpace4;
    private com.see.truetransact.uicomponent.CLabel lblSpace5;
    private com.see.truetransact.uicomponent.CLabel lblSpace6;
    private com.see.truetransact.uicomponent.CLabel lblSpace73;
    private com.see.truetransact.uicomponent.CLabel lblSpace74;
    private com.see.truetransact.uicomponent.CLabel lblSpace75;
    private com.see.truetransact.uicomponent.CLabel lblSpace76;
    private com.see.truetransact.uicomponent.CLabel lblSpace77;
    private com.see.truetransact.uicomponent.CLabel lblSpace78;
    private com.see.truetransact.uicomponent.CLabel lblState;
    private com.see.truetransact.uicomponent.CLabel lblStatus;
    private com.see.truetransact.uicomponent.CLabel lblSubNo;
    private com.see.truetransact.uicomponent.CLabel lblThalayal;
    private com.see.truetransact.uicomponent.CMenuBar mbrMain;
    private javax.swing.JMenuItem mitAuthorize;
    private javax.swing.JMenuItem mitCancel;
    private javax.swing.JMenuItem mitClose;
    private javax.swing.JMenuItem mitDelete;
    private javax.swing.JMenuItem mitEdit;
    private javax.swing.JMenuItem mitException;
    private javax.swing.JMenuItem mitNew;
    private javax.swing.JMenuItem mitPrint;
    private javax.swing.JMenuItem mitReject;
    private javax.swing.JMenuItem mitSave;
    private javax.swing.JMenu mnuProcess;
    private com.see.truetransact.uicomponent.CPanel panChittalNo;
    private com.see.truetransact.uicomponent.CPanel panCustomerNO;
    private com.see.truetransact.uicomponent.CPanel panDiscountPeriodDetails1;
    private com.see.truetransact.uicomponent.CPanel panDiscountPeriodDetails2;
    private com.see.truetransact.uicomponent.CPanel panGeneralRemittance;
    private com.see.truetransact.uicomponent.CPanel panInsideGeneralRemittance;
    private com.see.truetransact.uicomponent.CPanel panInsideGeneralRemittance1;
    private com.see.truetransact.uicomponent.CPanel panMemberDetails;
    private com.see.truetransact.uicomponent.CPanel panMemberName;
    private com.see.truetransact.uicomponent.CPanel panMembershipNo;
    private com.see.truetransact.uicomponent.CPanel panMobileBanking;
    private com.see.truetransact.uicomponent.CPanel panNominee;
    private com.see.truetransact.uicomponent.CPanel panSalaryRecovery;
    private com.see.truetransact.uicomponent.CPanel panSalaryRecoveryValue;
    private com.see.truetransact.uicomponent.CPanel panSchemeName;
    private com.see.truetransact.uicomponent.CPanel panStandingDetails;
    private com.see.truetransact.uicomponent.CPanel panStatus;
    private com.see.truetransact.uicomponent.CButtonGroup rdgEFTProductGR;
    private com.see.truetransact.uicomponent.CButtonGroup rdgIsLapsedGR;
    private com.see.truetransact.uicomponent.CButtonGroup rdgPayableBranchGR;
    private com.see.truetransact.uicomponent.CButtonGroup rdgPrintServicesGR;
    private com.see.truetransact.uicomponent.CButtonGroup rdgSalaryRecovery;
    private com.see.truetransact.uicomponent.CButtonGroup rdgSeriesGR;
    private com.see.truetransact.uicomponent.CRadioButton rdoSalaryRecovery_No;
    private com.see.truetransact.uicomponent.CRadioButton rdoSalaryRecovery_Yes;
    private javax.swing.JSeparator sptCancel;
    private javax.swing.JSeparator sptDelete;
    private javax.swing.JSeparator sptException;
    private com.see.truetransact.uicomponent.CTabbedPane tabRemittanceProduct;
    private javax.swing.JToolBar tbrAdvances;
    private com.see.truetransact.uicomponent.CDateField tdtApplnDate;
    private com.see.truetransact.uicomponent.CDateField tdtChitEndDt;
    private com.see.truetransact.uicomponent.CDateField tdtChitStartDt;
    private com.see.truetransact.uicomponent.CDateField tdtMobileSubscribedFrom;
    private com.see.truetransact.uicomponent.CTextField txtApplnNo;
    private com.see.truetransact.uicomponent.CTextField txtArea;
    private com.see.truetransact.uicomponent.CTextField txtCustomerIdCr;
    private com.see.truetransact.uicomponent.CTextField txtDivisionNo;
    private com.see.truetransact.uicomponent.CTextField txtGDSNo;
    private com.see.truetransact.uicomponent.CTextField txtGroupName;
    private com.see.truetransact.uicomponent.CTextField txtHouseStNo;
    private com.see.truetransact.uicomponent.CTextField txtInstAmt;
    private com.see.truetransact.uicomponent.CTextField txtMembershipName;
    private com.see.truetransact.uicomponent.CTextField txtMembershipNo;
    private com.see.truetransact.uicomponent.CTextField txtMobileNo;
    private com.see.truetransact.uicomponent.CTextField txtRemarks;
    private com.see.truetransact.uicomponent.CTextField txtSubNo;
    private com.see.truetransact.uicomponent.CTextField txtpin;
    // End of variables declaration//GEN-END:variables

    public static void main(String[] arg) {
        try {
            javax.swing.UIManager.setLookAndFeel("com.sun.java.swing.plaf.windows.WindowsLookAndFeel");
        } catch (Throwable th) {
            th.printStackTrace();
        }
        javax.swing.JFrame jf = new javax.swing.JFrame();
        GDSApplicationUI gui = new GDSApplicationUI();
        jf.getContentPane().add(gui);
        jf.setSize(536, 566);
        jf.show();
        gui.show();
    }
}