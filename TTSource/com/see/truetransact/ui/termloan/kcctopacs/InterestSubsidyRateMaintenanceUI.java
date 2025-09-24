/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition. 
 * 
 * InterestSubsidyRateMaintenanceUI.java
 *
 * Created on May 21, 2004, 12:37 PM
 */
package com.see.truetransact.ui.termloan.kcctopacs;

import com.see.truetransact.uicomponent.CInternalFrame;
import com.see.truetransact.uimandatory.UIMandatoryField;
import com.see.truetransact.uimandatory.MandatoryCheck;
import com.see.truetransact.clientutil.ComboBoxModel;
import com.see.truetransact.clientutil.ClientUtil;
import com.see.truetransact.clientutil.ClientConstants;
import com.see.truetransact.commonutil.CommonConstants;
import com.see.truetransact.commonutil.CommonUtil;
import com.see.truetransact.commonutil.DateUtil;
import com.see.truetransact.clientutil.CMandatoryDialog;
import com.see.truetransact.ui.common.viewall.ViewAll;
import com.see.truetransact.uivalidation.NumericValidation;
import com.see.truetransact.uivalidation.CurrencyValidation;
import com.see.truetransact.clientproxy.ProxyParameters;
import com.see.truetransact.uicomponent.CButtonGroup;
import org.apache.log4j.Logger;
import com.see.truetransact.ui.common.authorize.AuthorizeUI;

import com.see.truetransact.ui.TrueTransactMain;
import com.see.truetransact.ui.common.authorizestatus.AuthorizeStatusUI;
import com.see.truetransact.uicomponent.CTable;

import java.util.HashMap;
import java.util.ArrayList;
import java.util.List;
import java.util.Observer;
import java.util.Observable;
import java.util.Date;
import java.text.*;
import com.see.truetransact.clientutil.TableSorter;
import com.see.truetransact.clientutil.TableModel;

/**
 *
 * @author rahul
 */
public class InterestSubsidyRateMaintenanceUI extends CInternalFrame implements java.util.Observer, UIMandatoryField {

    private HashMap mandatoryMap;
    InterestSubsidyRateMaintenanceOB observable;
    final String AUTHORISE = "Authorise";
    private String AuthType = new String();
    final int EDIT = 0, DELETE = 1, ACCNUMBER = 2, AUTHORIZE = 3, VIEW = 4;
    int viewType = -1, updateTab = -1;
    boolean isFilled = false;
    int rowSelected = -1;
    int tabFlag = 0;
    final String OPERAIVE = "OA";
    final String ADVANCES = "AD";
    final String TERMLOAN = "TL";
    final String AGRITERMLOAN = "ATL";
    final String AGRIADVANCES = "AAD";
    final String DEPOSITS = "TD";
    boolean flag = false;
    private CTable _tblData;
    java.util.ResourceBundle resourceBundle = java.util.ResourceBundle.getBundle("com.see.truetransact.ui.deposit.interestmaintenance.InterestMaintenanceRB", ProxyParameters.LANGUAGE);
    private final static Logger log = Logger.getLogger(InterestSubsidyRateMaintenanceUI.class);
    private boolean updateMode = false;

    /**
     * Creates new form InterestSubsidyRateMaintenanceUI
     */
    public InterestSubsidyRateMaintenanceUI() {
        initComponents();
        initSetup();
    }

    private void initSetup() {
        setFieldNames();
        internationalize();
        setMaxLenths();
        setMandatoryHashMap();
        setObservable();
        initComponentData();
        new MandatoryCheck().putMandatoryMarks(getClass().getName(), panGroupnfo);
        new MandatoryCheck().putMandatoryMarks(getClass().getName(), panModeOfOpening);
        new MandatoryCheck().putMandatoryMarks(getClass().getName(), panInsDetails);
        ClientUtil.enableDisable(this, false);
        tabSaveDeleteButtons();
        btnTabNew.setEnabled(false);
        setButtonEnableDisable();
        resetUI();
        setHelpMessage();
        tabInterestMaintenance.resetVisits();
        setSizeTableData();
    }

    /*
     * Creates the instance of OB
     */
    private void setObservable() {
        observable = InterestSubsidyRateMaintenanceOB.getInstance();
        observable.addObserver(this);
    }

    /*
     * Auto Generated Method - setFieldNames() This method assigns name for all
     * the components. Other functions are working based on this name.
     */
    private void setFieldNames() {
        btnAuthorize.setName("btnAuthorize");
        btnCancel.setName("btnCancel");
        btnClose.setName("btnClose");
        btnDelete.setName("btnDelete");
        btnEdit.setName("btnEdit");
        btnException.setName("btnException");
        btnNew.setName("btnNew");
        btnPrint.setName("btnPrint");
        btnReject.setName("btnReject");
        btnSave.setName("btnSave");
        btnTabDelete.setName("btnTabDelete");
        btnTabNew.setName("btnTabNew");
        btnTabSave.setName("btnTabSave");
        cboInstitution.setName("cboInstitution");
        cboProdType.setName("cboProdType");
        lblToDate.setName("lblDate");
        lblInstitution.setName("lblFromAmount");
        lblGroupId.setName("lblGroupId");
        lblGroupIdDesc.setName("lblGroupIdDesc");
        lblGroupName.setName("lblGroupName");
        lblRemarks.setName("lblRemarks");
        lblMsg.setName("lblMsg");
        lblSubsidyInt.setName("lblSubsidyInt");
        lblRefno.setName("lblRefno");
        lblODI_Per.setName("lblODI_Per");
        lblMiscellaneousInt.setName("lblCustRateInt");
        lblMiscellaneousIntPer.setName("lblPenalInterestPer");
        lblProdType.setName("lblProdType");
        lblRateInterest.setName("lblRateInterest");
        lblRateInterestPer.setName("lblRateInterestPer");
        lblSpace1.setName("lblSpace1");
        lblSpace2.setName("lblSpace2");
        lblSpace3.setName("lblSpace3");
        lblSpace4.setName("lblSpace4");
        lblSpaces.setName("lblSpaces");
        lblStatus.setName("lblStatus");
        lblDateReceived.setName("lblToDate");
        mbrMain.setName("mbrMain");
        panButtons.setName("panButtons");
        panCalculations.setName("panCalculations");
        panGroupData.setName("panGroupData");
        panGroupnfo.setName("panGroupnfo");
        panInterestCalc.setName("panInterestCalc");
        panInterestCalculation.setName("panInterestCalculation");
        panInterestGroup.setName("panInterestGroup");
        panInterestMaintenance.setName("panInterestMaintenance");
        panInterestTable.setName("panInterestTable");
        panStatus.setName("panStatus");
        srpCategory.setName("srpCategory");
        srpInterestTable.setName("srpInterestTable");
        srpProduct.setName("srpProduct");
        tabInterestMaintenance.setName("tabInterestMaintenance");
        tblCategory.setName("tblCategory");
        tblInterestTable.setName("tblInterestTable");
        tblProduct.setName("tblProduct");
        tdtToDate.setName("tdtToDate");
        tdtDateReceived.setName("tdtDateReceived");
        txtGroupName.setName("txtGroupName");
        txtRemarks.setName("txtRemarks");
        txtSubsidyInt.setName("txtSubsidyInt");
        txtRateInterest.setName("txtRateInterest");
        rdoInterestSubsidy.setName("rdoInterestSubsidy");
        rdoInterestSubvention.setName("rdoInterestSubvention");
        txtRefno.setName("txtRefno");
        panInsDetails.setName("panInsDetails");
        txtMiscellaneousInt.setName("txtMiscellaneousInt");
        txtCustRateInt.setName("txtCustRateInt");
        tdtFromDate.setName("tdtFromDate");
    }

    /*
     * Auto Generated Method - internationalize() This method used to assign
     * display texts from the Resource Bundle File.
     */
    private void internationalize() {
        final InterestSubsidyRateMaintenanceRB resourceBundle = new InterestSubsidyRateMaintenanceRB();
        btnClose.setText(resourceBundle.getString("btnClose"));
        btnTabSave.setText(resourceBundle.getString("btnTabSave"));
        lblGroupId.setText(resourceBundle.getString("lblGroupId"));
        btnAuthorize.setText(resourceBundle.getString("btnAuthorize"));
        lblMsg.setText(resourceBundle.getString("lblMsg"));
        lblSpace4.setText(resourceBundle.getString("lblSpace4"));
        lblInstitution.setText(resourceBundle.getString("lblInstitution"));
        lblSpace2.setText(resourceBundle.getString("lblSpace2"));
        lblProdType.setText(resourceBundle.getString("lblProdType"));
        lblSpace3.setText(resourceBundle.getString("lblSpace3"));
        lblMiscellaneousInt.setText(resourceBundle.getString("lblCustRateInt"));
        lblSpace1.setText(resourceBundle.getString("lblSpace1"));
        lblRateInterest.setText(resourceBundle.getString("lblRateInterest"));
        btnReject.setText(resourceBundle.getString("btnReject"));
        btnEdit.setText(resourceBundle.getString("btnEdit"));
        lblSpaces.setText(resourceBundle.getString("lblSpaces"));
        btnTabNew.setText(resourceBundle.getString("btnTabNew"));
        btnPrint.setText(resourceBundle.getString("btnPrint"));
        lblRateInterestPer.setText(resourceBundle.getString("lblRateInterestPer"));
        btnException.setText(resourceBundle.getString("btnException"));
        btnSave.setText(resourceBundle.getString("btnSave"));
        lblStatus.setText(resourceBundle.getString("lblStatus"));
        lblODI_Per.setText(resourceBundle.getString("lblODI_Per"));
        btnDelete.setText(resourceBundle.getString("btnDelete"));
        btnTabDelete.setText(resourceBundle.getString("btnTabDelete"));
        ((javax.swing.border.TitledBorder) panGroupData.getBorder()).setTitle(resourceBundle.getString("panGroupData"));
        lblGroupIdDesc.setText(resourceBundle.getString("lblGroupIdDesc"));
        lblRemarks.setText(resourceBundle.getString("lblRemarks"));
        lblSubsidyInt.setText(resourceBundle.getString("lblSubsidyInt"));
        lblFromDate.setText(resourceBundle.getString("lblFromDate"));
        btnNew.setText(resourceBundle.getString("btnNew"));
        lblDateReceived.setText(resourceBundle.getString("lblDateReceived"));
        lblInstitution.setText(resourceBundle.getString("lblInstitution"));
        lblMiscellaneousIntPer.setText(resourceBundle.getString("lblPenalInterestPer"));
        btnCancel.setText(resourceBundle.getString("btnCancel"));
        lblGroupName.setText(resourceBundle.getString("lblGroupName"));
        lblRefno.setText(resourceBundle.getString("lblRefno"));
        lblToDate.setText(resourceBundle.getString("lblToDate"));
        lblMiscellaneousInt.setText(resourceBundle.getString("lblMiscellaneousInt"));
    }

    /*
     * Auto Generated Method - setMandatoryHashMap() This method list out all
     * the Input Fields available in the UI. It needs a class level HashMap
     * variable mandatoryMap.
     */
    public void setMandatoryHashMap() {
        mandatoryMap = new HashMap();
        mandatoryMap.put("txtGroupName", new Boolean(true));
        mandatoryMap.put("cboProdType", new Boolean(true));
        mandatoryMap.put("tdtDate", new Boolean(true));
        mandatoryMap.put("txtFromPeriod", new Boolean(true));
        mandatoryMap.put("txtToPeriod", new Boolean(true));
        mandatoryMap.put("txtRateInterest", new Boolean(true));
        mandatoryMap.put("txtPenalInterest", new Boolean(true));
        mandatoryMap.put("tdtToDate", new Boolean(true));
        mandatoryMap.put("txtAgainstInterest", new Boolean(true));
        mandatoryMap.put("txtLimitAmt", new Boolean(true));
        mandatoryMap.put("txtInterExpiry", new Boolean(true));
        mandatoryMap.put("txtPLR", new Boolean(true));
        mandatoryMap.put("txtFloatingRate", new Boolean(true));
    }

    /*
     * Auto Generated Method - getMandatoryHashMap() Getter method for
     * setMandatoryHashMap().
     */
    public HashMap getMandatoryHashMap() {
        return mandatoryMap;
    }

    /*
     * Auto Generated Method - update() This method called by Observable. It
     * updates the UI with Observable's data. If needed add/Remove RadioButtons
     * method need to be added.
     */
    public void update(Observable observed, Object arg) {
        cboProdType.setSelectedItem(observable.getCboProdType());
        txtGroupName.setText(observable.getTxtGroupName());
        tdtFromDate.setDateValue(observable.getTdtFromDate());
        tdtDateReceived.setDateValue(observable.getTdtDateReceived());
        tdtToDate.setDateValue(observable.getTdtToDate());
        cboInstitution.setSelectedItem(observable.getCboInstitution());
        txtRemarks.setText(observable.getTxtRemarks());
        tblProduct.setModel(observable.getTblProduct());
        tblCategory.setModel(observable.getTblCategory());
        tblInterestTable.setModel(observable.getTblInterest());
        lblGroupIdDesc.setText(observable.getLblRoiGroupId());
        lblStatus.setText(observable.getLblStatus());
        removeRadioButtons();
        rdoInterestType_Debit.setSelected(observable.isRdoInterestType_Debit());
        rdoInterestType_Credit.setSelected(observable.isRdoInterestType_Credit());
        addRadioButtons();
    }

    private void removeRadioButtons() {
        rdgInterestType.remove(rdoInterestType_Debit);
        rdgInterestType.remove(rdoInterestType_Credit);
    }

    private void addRadioButtons() {
        rdgInterestType = new CButtonGroup();
        rdgInterestType.add(rdoInterestType_Debit);
        rdgInterestType.add(rdoInterestType_Credit);
    }

    public void updateOBFields() {
        observable.setTxtGroupName(txtGroupName.getText());
        observable.setCboProdType((String) cboProdType.getSelectedItem());
        observable.setCboInstitution((String) cboInstitution.getSelectedItem());
        observable.setTxtRefno(txtRefno.getText());
        observable.setTxtCustRateInt(CommonUtil.convertObjToDouble(txtCustRateInt.getText()));
        observable.setTxtRemarks(txtRemarks.getText());
        observable.setTxtSubsidyInt(CommonUtil.convertObjToDouble(txtSubsidyInt.getText()));
        observable.setTxtRateInterest(CommonUtil.convertObjToDouble(txtRateInterest.getText()));
        observable.setTdtFromDate(tdtFromDate.getDateValue());
        observable.setTdtDateReceived(tdtDateReceived.getDateValue());
        observable.setTblProduct((com.see.truetransact.clientutil.EnhancedTableModel) tblProduct.getModel());
        observable.setTblCategory((com.see.truetransact.clientutil.EnhancedTableModel) tblCategory.getModel());
        observable.setRdoInterestType_Debit(rdoInterestType_Debit.isSelected());
        observable.setRdoInterestType_Credit(rdoInterestType_Credit.isSelected());
        observable.setTxtMiscellaneousInt(CommonUtil.convertObjToDouble(txtMiscellaneousInt.getText()));
    }

    /*
     * Auto Generated Method - setHelpMessage() This method shows tooltip help
     * for all the input fields available in the UI. It needs the Mandatory
     * Resource Bundle object. Help display Label name should be lblMsg.
     */
    public void setHelpMessage() {
        InterestSubsidyRateMaintenanceMRB objMandatoryRB = new InterestSubsidyRateMaintenanceMRB();
        txtGroupName.setHelpMessage(lblMsg, objMandatoryRB.getString("txtGroupName"));
        tdtToDate.setHelpMessage(lblMsg, objMandatoryRB.getString("tdtDate"));
        cboInstitution.setHelpMessage(lblMsg, objMandatoryRB.getString("cboInstitution"));
        txtRateInterest.setHelpMessage(lblMsg, objMandatoryRB.getString("txtRateInterest"));

        cboProdType.setHelpMessage(lblMsg, objMandatoryRB.getString("cboProdType"));
    }

    // To fill the Data into the Combo Boxes...
    // it invokes the Combo Box model defined in OB class...
    private void initComponentData() {
        cboProdType.setModel(observable.getCbmProdType());
        cboInstitution.setModel(observable.getCbmInstitution());
    }

    private void setMaxLenths() {
        txtGroupName.setMaxLength(64);
        txtRateInterest.setValidation(new NumericValidation(2, 2));
        txtCustRateInt.setValidation(new NumericValidation(2, 2));
        txtSubsidyInt.setValidation(new NumericValidation(2, 2));
        txtRefno.setAllowNumber(true);
        txtRemarks.setAllowAll(true);
        txtMiscellaneousInt.setValidation(new NumericValidation(2, 2));
    }

    // To set The Status of the Buttons Depending on the Condition...
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

        rdgInterestType = new com.see.truetransact.uicomponent.CButtonGroup();
        panInterestMaintenance = new com.see.truetransact.uicomponent.CPanel();
        lblSpaces = new com.see.truetransact.uicomponent.CLabel();
        tabInterestMaintenance = new com.see.truetransact.uicomponent.CTabbedPane();
        panInterestGroup = new com.see.truetransact.uicomponent.CPanel();
        panGroupnfo = new com.see.truetransact.uicomponent.CPanel();
        lblGroupId = new com.see.truetransact.uicomponent.CLabel();
        lblGroupIdDesc = new com.see.truetransact.uicomponent.CLabel();
        lblGroupName = new com.see.truetransact.uicomponent.CLabel();
        txtGroupName = new com.see.truetransact.uicomponent.CTextField();
        lblProdType = new com.see.truetransact.uicomponent.CLabel();
        cboProdType = new com.see.truetransact.uicomponent.CComboBox();
        panGroupData = new com.see.truetransact.uicomponent.CPanel();
        srpProduct = new com.see.truetransact.uicomponent.CScrollPane();
        tblProduct = new com.see.truetransact.uicomponent.CTable();
        srpCategory = new com.see.truetransact.uicomponent.CScrollPane();
        tblCategory = new com.see.truetransact.uicomponent.CTable();
        panModeOfOpening = new com.see.truetransact.uicomponent.CPanel();
        rdoInterestType_Debit = new com.see.truetransact.uicomponent.CRadioButton();
        rdoInterestType_Credit = new com.see.truetransact.uicomponent.CRadioButton();
        lblInterestType = new com.see.truetransact.uicomponent.CLabel();
        panInterestCalc = new com.see.truetransact.uicomponent.CPanel();
        panInterestCalculation = new com.see.truetransact.uicomponent.CPanel();
        panCalculations = new com.see.truetransact.uicomponent.CPanel();
        lblRateInterest = new com.see.truetransact.uicomponent.CLabel();
        txtRateInterest = new com.see.truetransact.uicomponent.CTextField();
        lblRateInterestPer = new com.see.truetransact.uicomponent.CLabel();
        lblMiscellaneousInt = new com.see.truetransact.uicomponent.CLabel();
        txtMiscellaneousInt = new com.see.truetransact.uicomponent.CTextField();
        lblMiscellaneousIntPer = new com.see.truetransact.uicomponent.CLabel();
        panInsDetails = new com.see.truetransact.uicomponent.CPanel();
        lblInstitution = new com.see.truetransact.uicomponent.CLabel();
        lblDateReceived = new com.see.truetransact.uicomponent.CLabel();
        panButtons = new com.see.truetransact.uicomponent.CPanel();
        btnTabNew = new com.see.truetransact.uicomponent.CButton();
        btnTabSave = new com.see.truetransact.uicomponent.CButton();
        btnTabDelete = new com.see.truetransact.uicomponent.CButton();
        txtRefno = new com.see.truetransact.uicomponent.CTextField();
        txtRemarks = new com.see.truetransact.uicomponent.CTextField();
        lblSubsidyInt = new com.see.truetransact.uicomponent.CLabel();
        lblRefno = new com.see.truetransact.uicomponent.CLabel();
        cboInstitution = new com.see.truetransact.uicomponent.CComboBox();
        tdtDateReceived = new com.see.truetransact.uicomponent.CDateField();
        lblRemarks = new com.see.truetransact.uicomponent.CLabel();
        panModeOfOpening1 = new com.see.truetransact.uicomponent.CPanel();
        rdoInterestSubvention = new com.see.truetransact.uicomponent.CRadioButton();
        rdoInterestSubsidy = new com.see.truetransact.uicomponent.CRadioButton();
        txtSubsidyInt = new com.see.truetransact.uicomponent.CTextField();
        lblODI_Per = new com.see.truetransact.uicomponent.CLabel();
        lblToDate = new com.see.truetransact.uicomponent.CLabel();
        tdtToDate = new com.see.truetransact.uicomponent.CDateField();
        lblFromDate = new com.see.truetransact.uicomponent.CLabel();
        tdtFromDate = new com.see.truetransact.uicomponent.CDateField();
        lblCustRateInt = new com.see.truetransact.uicomponent.CLabel();
        txtCustRateInt = new com.see.truetransact.uicomponent.CTextField();
        lblPenalInterestPer = new com.see.truetransact.uicomponent.CLabel();
        panInterestTable = new com.see.truetransact.uicomponent.CPanel();
        srpInterestTable = new com.see.truetransact.uicomponent.CScrollPane();
        tblInterestTable = new com.see.truetransact.uicomponent.CTable();
        tbrHead = new javax.swing.JToolBar();
        btnView = new com.see.truetransact.uicomponent.CButton();
        lblSpace5 = new com.see.truetransact.uicomponent.CLabel();
        btnNew = new com.see.truetransact.uicomponent.CButton();
        lblSpace70 = new com.see.truetransact.uicomponent.CLabel();
        btnEdit = new com.see.truetransact.uicomponent.CButton();
        lblSpace71 = new com.see.truetransact.uicomponent.CLabel();
        btnDelete = new com.see.truetransact.uicomponent.CButton();
        lblSpace2 = new com.see.truetransact.uicomponent.CLabel();
        btnSave = new com.see.truetransact.uicomponent.CButton();
        lblSpace72 = new com.see.truetransact.uicomponent.CLabel();
        btnCancel = new com.see.truetransact.uicomponent.CButton();
        lblSpace3 = new com.see.truetransact.uicomponent.CLabel();
        btnAuthorize = new com.see.truetransact.uicomponent.CButton();
        lblSpace73 = new com.see.truetransact.uicomponent.CLabel();
        btnException = new com.see.truetransact.uicomponent.CButton();
        lblSpace74 = new com.see.truetransact.uicomponent.CLabel();
        btnReject = new com.see.truetransact.uicomponent.CButton();
        lblSpace4 = new com.see.truetransact.uicomponent.CLabel();
        btnPrint = new com.see.truetransact.uicomponent.CButton();
        lblSpace75 = new com.see.truetransact.uicomponent.CLabel();
        btnClose = new com.see.truetransact.uicomponent.CButton();
        panStatus = new com.see.truetransact.uicomponent.CPanel();
        lblSpace1 = new com.see.truetransact.uicomponent.CLabel();
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
        mitClose = new javax.swing.JMenuItem();

        setClosable(true);
        setIconifiable(true);
        setMaximizable(true);
        setResizable(true);
        setMinimumSize(new java.awt.Dimension(860, 663));
        setPreferredSize(new java.awt.Dimension(860, 663));

        panInterestMaintenance.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        panInterestMaintenance.setMinimumSize(new java.awt.Dimension(880, 561));
        panInterestMaintenance.setPreferredSize(new java.awt.Dimension(880, 561));
        panInterestMaintenance.setLayout(new java.awt.GridBagLayout());

        lblSpaces.setMinimumSize(new java.awt.Dimension(3, 15));
        lblSpaces.setPreferredSize(new java.awt.Dimension(3, 15));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        panInterestMaintenance.add(lblSpaces, gridBagConstraints);

        tabInterestMaintenance.setMinimumSize(new java.awt.Dimension(860, 534));
        tabInterestMaintenance.setPreferredSize(new java.awt.Dimension(860, 534));

        panInterestGroup.setLayout(new java.awt.GridBagLayout());

        panGroupnfo.setLayout(new java.awt.GridBagLayout());

        lblGroupId.setText("Interest Rate Group ID");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panGroupnfo.add(lblGroupId, gridBagConstraints);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panGroupnfo.add(lblGroupIdDesc, gridBagConstraints);

        lblGroupName.setText("Interest Rate Group Name");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panGroupnfo.add(lblGroupName, gridBagConstraints);

        txtGroupName.setMinimumSize(new java.awt.Dimension(100, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panGroupnfo.add(txtGroupName, gridBagConstraints);

        lblProdType.setText("Product Type");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panGroupnfo.add(lblProdType, gridBagConstraints);

        cboProdType.setMinimumSize(new java.awt.Dimension(100, 21));
        cboProdType.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                cboProdTypeItemStateChanged(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panGroupnfo.add(cboProdType, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panInterestGroup.add(panGroupnfo, gridBagConstraints);

        panGroupData.setBorder(javax.swing.BorderFactory.createTitledBorder("Group Data"));
        panGroupData.setLayout(new java.awt.GridBagLayout());

        srpProduct.setMinimumSize(new java.awt.Dimension(300, 150));
        srpProduct.setPreferredSize(new java.awt.Dimension(300, 150));

        tblProduct.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Check", "Product ID", "Product Description"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.Boolean.class, java.lang.Object.class, java.lang.Object.class
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }
        });
        srpProduct.setViewportView(tblProduct);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panGroupData.add(srpProduct, gridBagConstraints);

        srpCategory.setMinimumSize(new java.awt.Dimension(300, 150));
        srpCategory.setPreferredSize(new java.awt.Dimension(300, 150));

        tblCategory.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Check", "Category", "Category Description"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.Boolean.class, java.lang.Object.class, java.lang.Object.class
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }
        });
        srpCategory.setViewportView(tblCategory);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panGroupData.add(srpCategory, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panInterestGroup.add(panGroupData, gridBagConstraints);

        panModeOfOpening.setMinimumSize(new java.awt.Dimension(330, 19));
        panModeOfOpening.setPreferredSize(new java.awt.Dimension(330, 20));
        panModeOfOpening.setLayout(new java.awt.GridBagLayout());

        rdgInterestType.add(rdoInterestType_Debit);
        rdoInterestType_Debit.setText("Debit");
        rdoInterestType_Debit.setMaximumSize(new java.awt.Dimension(68, 15));
        rdoInterestType_Debit.setMinimumSize(new java.awt.Dimension(66, 15));
        rdoInterestType_Debit.setPreferredSize(new java.awt.Dimension(66, 18));
        rdoInterestType_Debit.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                rdoInterestType_DebitActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(1, 10, 1, 0);
        panModeOfOpening.add(rdoInterestType_Debit, gridBagConstraints);

        rdgInterestType.add(rdoInterestType_Credit);
        rdoInterestType_Credit.setText("Credit");
        rdoInterestType_Credit.setMaximumSize(new java.awt.Dimension(85, 21));
        rdoInterestType_Credit.setMinimumSize(new java.awt.Dimension(85, 15));
        rdoInterestType_Credit.setPreferredSize(new java.awt.Dimension(85, 18));
        rdoInterestType_Credit.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                rdoInterestType_CreditActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(1, 0, 1, 0);
        panModeOfOpening.add(rdoInterestType_Credit, gridBagConstraints);

        lblInterestType.setText("Interest Type");
        lblInterestType.setMinimumSize(new java.awt.Dimension(98, 15));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 0, 2);
        panModeOfOpening.add(lblInterestType, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.insets = new java.awt.Insets(1, 1, 1, 1);
        panInterestGroup.add(panModeOfOpening, gridBagConstraints);

        tabInterestMaintenance.addTab("Interest Rate Group", panInterestGroup);

        panInterestCalc.setMinimumSize(new java.awt.Dimension(855, 508));
        panInterestCalc.setPreferredSize(new java.awt.Dimension(855, 508));
        panInterestCalc.setLayout(new java.awt.GridBagLayout());

        panInterestCalculation.setMinimumSize(new java.awt.Dimension(855, 508));
        panInterestCalculation.setPreferredSize(new java.awt.Dimension(855, 508));
        panInterestCalculation.setLayout(new java.awt.GridBagLayout());

        panCalculations.setMinimumSize(new java.awt.Dimension(330, 430));
        panCalculations.setPreferredSize(new java.awt.Dimension(330, 430));
        panCalculations.setLayout(new java.awt.GridBagLayout());

        lblRateInterest.setText("Total Rate of Interest");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panCalculations.add(lblRateInterest, gridBagConstraints);

        txtRateInterest.setMinimumSize(new java.awt.Dimension(50, 21));
        txtRateInterest.setPreferredSize(new java.awt.Dimension(50, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panCalculations.add(txtRateInterest, gridBagConstraints);

        lblRateInterestPer.setText("%");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        panCalculations.add(lblRateInterestPer, gridBagConstraints);

        lblMiscellaneousInt.setText("Miscellaneous Interest");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panCalculations.add(lblMiscellaneousInt, gridBagConstraints);

        txtMiscellaneousInt.setMinimumSize(new java.awt.Dimension(50, 21));
        txtMiscellaneousInt.setPreferredSize(new java.awt.Dimension(50, 21));
        txtMiscellaneousInt.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                txtMiscellaneousIntFocusLost(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panCalculations.add(txtMiscellaneousInt, gridBagConstraints);

        lblMiscellaneousIntPer.setText("%");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        panCalculations.add(lblMiscellaneousIntPer, gridBagConstraints);

        panInsDetails.setBorder(javax.swing.BorderFactory.createTitledBorder("Institution Details"));
        panInsDetails.setMinimumSize(new java.awt.Dimension(320, 260));
        panInsDetails.setPreferredSize(new java.awt.Dimension(320, 260));
        panInsDetails.setLayout(new java.awt.GridBagLayout());

        lblInstitution.setText("Name of the Institution");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panInsDetails.add(lblInstitution, gridBagConstraints);

        lblDateReceived.setText("Date Received");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panInsDetails.add(lblDateReceived, gridBagConstraints);

        panButtons.setLayout(new java.awt.GridBagLayout());

        btnTabNew.setText("New");
        btnTabNew.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnTabNewActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panButtons.add(btnTabNew, gridBagConstraints);

        btnTabSave.setText("Save");
        btnTabSave.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnTabSaveActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panButtons.add(btnTabSave, gridBagConstraints);

        btnTabDelete.setText("Delete");
        btnTabDelete.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnTabDeleteActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panButtons.add(btnTabDelete, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 6;
        gridBagConstraints.gridwidth = 4;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panInsDetails.add(panButtons, gridBagConstraints);

        txtRefno.setMinimumSize(new java.awt.Dimension(100, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panInsDetails.add(txtRefno, gridBagConstraints);

        txtRemarks.setMinimumSize(new java.awt.Dimension(100, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 5;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panInsDetails.add(txtRemarks, gridBagConstraints);

        lblSubsidyInt.setText("Subsidy Rate of Interest ");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panInsDetails.add(lblSubsidyInt, gridBagConstraints);

        lblRefno.setText("Ref No.");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panInsDetails.add(lblRefno, gridBagConstraints);

        cboInstitution.setMinimumSize(new java.awt.Dimension(100, 21));
        cboInstitution.setPopupWidth(150);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panInsDetails.add(cboInstitution, gridBagConstraints);

        tdtDateReceived.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                tdtDateReceivedFocusLost(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panInsDetails.add(tdtDateReceived, gridBagConstraints);

        lblRemarks.setText("Remarks");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 5;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panInsDetails.add(lblRemarks, gridBagConstraints);

        panModeOfOpening1.setMinimumSize(new java.awt.Dimension(330, 19));
        panModeOfOpening1.setPreferredSize(new java.awt.Dimension(270, 20));
        panModeOfOpening1.setLayout(new java.awt.GridBagLayout());

        rdgInterestType.add(rdoInterestSubvention);
        rdoInterestSubvention.setText("Interest Subvention");
        rdoInterestSubvention.setMaximumSize(new java.awt.Dimension(85, 21));
        rdoInterestSubvention.setMinimumSize(new java.awt.Dimension(85, 15));
        rdoInterestSubvention.setPreferredSize(new java.awt.Dimension(140, 15));
        rdoInterestSubvention.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                rdoInterestSubventionActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 5;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(1, 0, 1, 0);
        panModeOfOpening1.add(rdoInterestSubvention, gridBagConstraints);

        rdgInterestType.add(rdoInterestSubsidy);
        rdoInterestSubsidy.setText("Interest Subsidy");
        rdoInterestSubsidy.setMaximumSize(new java.awt.Dimension(120, 15));
        rdoInterestSubsidy.setMinimumSize(new java.awt.Dimension(120, 15));
        rdoInterestSubsidy.setPreferredSize(new java.awt.Dimension(120, 15));
        rdoInterestSubsidy.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                rdoInterestSubsidyActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 5;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(1, 10, 1, 0);
        panModeOfOpening1.add(rdoInterestSubsidy, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.gridwidth = 4;
        gridBagConstraints.insets = new java.awt.Insets(1, 1, 1, 1);
        panInsDetails.add(panModeOfOpening1, gridBagConstraints);

        txtSubsidyInt.setMinimumSize(new java.awt.Dimension(50, 21));
        txtSubsidyInt.setPreferredSize(new java.awt.Dimension(50, 21));
        txtSubsidyInt.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                txtSubsidyIntFocusLost(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panInsDetails.add(txtSubsidyInt, gridBagConstraints);

        lblODI_Per.setText("%");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        panInsDetails.add(lblODI_Per, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 5;
        gridBagConstraints.gridwidth = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panCalculations.add(panInsDetails, gridBagConstraints);

        lblToDate.setText("To Date");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 5, 4, 3);
        panCalculations.add(lblToDate, gridBagConstraints);

        tdtToDate.setMinimumSize(new java.awt.Dimension(100, 21));
        tdtToDate.setPreferredSize(new java.awt.Dimension(100, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panCalculations.add(tdtToDate, gridBagConstraints);

        lblFromDate.setText("From Date");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 5, 4, 3);
        panCalculations.add(lblFromDate, gridBagConstraints);

        tdtFromDate.setMinimumSize(new java.awt.Dimension(100, 21));
        tdtFromDate.setPreferredSize(new java.awt.Dimension(100, 21));
        tdtFromDate.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                tdtFromDateFocusLost(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panCalculations.add(tdtFromDate, gridBagConstraints);

        lblCustRateInt.setText("Interest to be charged to the Customer");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panCalculations.add(lblCustRateInt, gridBagConstraints);

        txtCustRateInt.setMinimumSize(new java.awt.Dimension(50, 21));
        txtCustRateInt.setPreferredSize(new java.awt.Dimension(50, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panCalculations.add(txtCustRateInt, gridBagConstraints);

        lblPenalInterestPer.setText("%");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        panCalculations.add(lblPenalInterestPer, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panInterestCalculation.add(panCalculations, gridBagConstraints);

        panInterestTable.setMinimumSize(new java.awt.Dimension(500, 420));
        panInterestTable.setPreferredSize(new java.awt.Dimension(500, 420));
        panInterestTable.setLayout(new java.awt.GridBagLayout());

        srpInterestTable.setMinimumSize(new java.awt.Dimension(500, 420));
        srpInterestTable.setPreferredSize(new java.awt.Dimension(500, 420));

        tblInterestTable.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "From Date", "To Date", "Total Roi", "Cust Roi", "Institution", "Subsidy/Subvention", "Subsidy Rate", "Ref No", "Auth Status"
            }
        ));
        tblInterestTable.setMinimumSize(new java.awt.Dimension(750, 0));
        tblInterestTable.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                tblInterestTableMousePressed(evt);
            }
        });
        srpInterestTable.setViewportView(tblInterestTable);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTH;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        panInterestTable.add(srpInterestTable, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panInterestCalculation.add(panInterestTable, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        panInterestCalc.add(panInterestCalculation, gridBagConstraints);

        tabInterestMaintenance.addTab("Int/Subsidy/Subvention ", panInterestCalc);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panInterestMaintenance.add(tabInterestMaintenance, gridBagConstraints);

        getContentPane().add(panInterestMaintenance, java.awt.BorderLayout.CENTER);

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
        tbrHead.add(btnView);

        lblSpace5.setText("     ");
        tbrHead.add(lblSpace5);

        btnNew.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_NEW.gif"))); // NOI18N
        btnNew.setToolTipText("New");
        btnNew.setEnabled(false);
        btnNew.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnNewActionPerformed(evt);
            }
        });
        tbrHead.add(btnNew);

        lblSpace70.setForeground(new java.awt.Color(240, 240, 240));
        lblSpace70.setText("     ");
        lblSpace70.setMaximumSize(new java.awt.Dimension(3, 18));
        lblSpace70.setMinimumSize(new java.awt.Dimension(3, 18));
        lblSpace70.setPreferredSize(new java.awt.Dimension(3, 18));
        tbrHead.add(lblSpace70);

        btnEdit.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_EDIT.gif"))); // NOI18N
        btnEdit.setToolTipText("Edit");
        btnEdit.setEnabled(false);
        btnEdit.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnEditActionPerformed(evt);
            }
        });
        tbrHead.add(btnEdit);

        lblSpace71.setForeground(new java.awt.Color(240, 240, 240));
        lblSpace71.setText("     ");
        lblSpace71.setMaximumSize(new java.awt.Dimension(3, 18));
        lblSpace71.setMinimumSize(new java.awt.Dimension(3, 18));
        lblSpace71.setPreferredSize(new java.awt.Dimension(3, 18));
        tbrHead.add(lblSpace71);

        btnDelete.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_DELETE.gif"))); // NOI18N
        btnDelete.setToolTipText("Delete");
        btnDelete.setEnabled(false);
        btnDelete.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnDeleteActionPerformed(evt);
            }
        });
        tbrHead.add(btnDelete);

        lblSpace2.setText("     ");
        tbrHead.add(lblSpace2);

        btnSave.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_SAVE.gif"))); // NOI18N
        btnSave.setToolTipText("Save");
        btnSave.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSaveActionPerformed(evt);
            }
        });
        tbrHead.add(btnSave);

        lblSpace72.setForeground(new java.awt.Color(240, 240, 240));
        lblSpace72.setText("     ");
        lblSpace72.setMaximumSize(new java.awt.Dimension(3, 18));
        lblSpace72.setMinimumSize(new java.awt.Dimension(3, 18));
        lblSpace72.setPreferredSize(new java.awt.Dimension(3, 18));
        tbrHead.add(lblSpace72);

        btnCancel.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_CANCEL.gif"))); // NOI18N
        btnCancel.setToolTipText("Cancel");
        btnCancel.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCancelActionPerformed(evt);
            }
        });
        tbrHead.add(btnCancel);

        lblSpace3.setText("     ");
        tbrHead.add(lblSpace3);

        btnAuthorize.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_AUTHORIZE.gif"))); // NOI18N
        btnAuthorize.setToolTipText("Authorize");
        btnAuthorize.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnAuthorizeActionPerformed(evt);
            }
        });
        tbrHead.add(btnAuthorize);

        lblSpace73.setForeground(new java.awt.Color(240, 240, 240));
        lblSpace73.setText("     ");
        lblSpace73.setMaximumSize(new java.awt.Dimension(3, 18));
        lblSpace73.setMinimumSize(new java.awt.Dimension(3, 18));
        lblSpace73.setPreferredSize(new java.awt.Dimension(3, 18));
        tbrHead.add(lblSpace73);

        btnException.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_EXCEPTION.gif"))); // NOI18N
        btnException.setToolTipText("Exception");
        btnException.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnExceptionActionPerformed(evt);
            }
        });
        tbrHead.add(btnException);

        lblSpace74.setForeground(new java.awt.Color(240, 240, 240));
        lblSpace74.setText("     ");
        lblSpace74.setMaximumSize(new java.awt.Dimension(3, 18));
        lblSpace74.setMinimumSize(new java.awt.Dimension(3, 18));
        lblSpace74.setPreferredSize(new java.awt.Dimension(3, 18));
        tbrHead.add(lblSpace74);

        btnReject.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_REJECT.gif"))); // NOI18N
        btnReject.setToolTipText("Reject");
        btnReject.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnRejectActionPerformed(evt);
            }
        });
        tbrHead.add(btnReject);

        lblSpace4.setText("     ");
        tbrHead.add(lblSpace4);

        btnPrint.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_PRINT.gif"))); // NOI18N
        btnPrint.setToolTipText("Print");
        btnPrint.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnPrintActionPerformed(evt);
            }
        });
        tbrHead.add(btnPrint);

        lblSpace75.setForeground(new java.awt.Color(240, 240, 240));
        lblSpace75.setText("     ");
        lblSpace75.setMaximumSize(new java.awt.Dimension(3, 18));
        lblSpace75.setMinimumSize(new java.awt.Dimension(3, 18));
        lblSpace75.setPreferredSize(new java.awt.Dimension(3, 18));
        tbrHead.add(lblSpace75);

        btnClose.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_CLOSE.gif"))); // NOI18N
        btnClose.setToolTipText("Close");
        btnClose.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCloseActionPerformed(evt);
            }
        });
        tbrHead.add(btnClose);

        getContentPane().add(tbrHead, java.awt.BorderLayout.NORTH);

        panStatus.setLayout(new java.awt.GridBagLayout());

        lblSpace1.setText(" Status :");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.fill = java.awt.GridBagConstraints.VERTICAL;
        gridBagConstraints.weighty = 1.0;
        panStatus.add(lblSpace1, gridBagConstraints);

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

        getContentPane().add(panStatus, java.awt.BorderLayout.SOUTH);

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

        mitClose.setText("Close");
        mitClose.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mitCloseActionPerformed(evt);
            }
        });
        mnuProcess.add(mitClose);

        mbrMain.add(mnuProcess);

        setJMenuBar(mbrMain);

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void rdoInterestType_CreditActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_rdoInterestType_CreditActionPerformed
        // TODO add your handling code here:
        if (CommonUtil.convertObjToStr(cboProdType.getSelectedItem()).length() > 0) {
            observable.setRdoInterestType_Credit(true);
            observable.setRdoInterestType_Debit(false);
        }
    }//GEN-LAST:event_rdoInterestType_CreditActionPerformed

    private void rdoInterestType_DebitActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_rdoInterestType_DebitActionPerformed
        // TODO add your handling code here:
        if (CommonUtil.convertObjToStr(cboProdType.getSelectedItem()).length() > 0) {
            observable.setRdoInterestType_Debit(true);
            observable.setRdoInterestType_Credit(false);
        }
    }//GEN-LAST:event_rdoInterestType_DebitActionPerformed

    private void btnViewActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnViewActionPerformed
        // TODO add your handling code here:
        observable.setActionType(ClientConstants.ACTIONTYPE_VIEW);
        observable.setStatus();
        lblStatus.setText(observable.getLblStatus());
        popUp(VIEW);
        btnCheck();
    }//GEN-LAST:event_btnViewActionPerformed

    private void tdtDateReceivedFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_tdtDateReceivedFocusLost
        // TODO add your handling code here:      
        if (tdtDateReceived.getDateValue().length() > 0) {
            Date currdt = ClientUtil.getCurrentDate();
            Date receivDate = DateUtil.getDateMMDDYYYY(tdtDateReceived.getDateValue());
            if (DateUtil.dateDiff(receivDate, currdt) < 0) {
                ClientUtil.showAlertWindow("Received Date Should Be Equal Or less than current Date !!!");
                tdtDateReceived.setDateValue("");
            }
        }
    }//GEN-LAST:event_tdtDateReceivedFocusLost

    private void cboProdTypeItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_cboProdTypeItemStateChanged
        // TODO add your handling code here:
        observable.setCboProdType(CommonUtil.convertObjToStr(cboProdType.getSelectedItem()));
        if (observable.getCboProdType().length() > 1) {
            observable.resetProductTab();
            String id = "";
            id = CommonUtil.convertObjToStr((((ComboBoxModel) (cboProdType).getModel())).getKeyForSelected());
            if (id.length() > 0) {
                //System.out.println("id: " + id);
                observable.getProductData(id);
            }
        }
    }//GEN-LAST:event_cboProdTypeItemStateChanged

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

    private void btnExceptionActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnExceptionActionPerformed
        // Add your handling code here:
        observable.setActionType(ClientConstants.ACTIONTYPE_EXCEPTION);
        authorizeStatus(CommonConstants.STATUS_EXCEPTION);
    }//GEN-LAST:event_btnExceptionActionPerformed

    private void btnRejectActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnRejectActionPerformed
        // Add your handling code here:
        observable.setActionType(ClientConstants.ACTIONTYPE_REJECT);
        authorizeStatus(CommonConstants.STATUS_REJECTED);
    }//GEN-LAST:event_btnRejectActionPerformed

    private void btnAuthorizeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAuthorizeActionPerformed
        // Add your handling code here:
        tblProduct.setEnabled(false);
        tblCategory.setEnabled(false);
        observable.setActionType(ClientConstants.ACTIONTYPE_AUTHORIZE);
        authorizeStatus(CommonConstants.STATUS_AUTHORIZED);
    }//GEN-LAST:event_btnAuthorizeActionPerformed
    public void authorizeStatus(String authorizeStatus) {
        if (AuthType == AUTHORISE && isFilled) {
            ArrayList arrList = new ArrayList();
            HashMap authorizeMap = new HashMap();
            HashMap singleAuthorizeMap = new HashMap();
            singleAuthorizeMap.put("STATUS", authorizeStatus);
            singleAuthorizeMap.put("AUTHORIZE_BY", TrueTransactMain.USER_ID);
            singleAuthorizeMap.put("AUTHORIZE_DT", ClientUtil.getCurrentDateWithTime());
            singleAuthorizeMap.put("ROI_GROUP_ID", observable.getLblRoiGroupId());
            arrList.add(singleAuthorizeMap);
            authorizeMap.put(CommonConstants.AUTHORIZESTATUS, authorizeStatus);
            authorizeMap.put(CommonConstants.AUTHORIZEDATA, arrList);
            authorizeMap.put(CommonConstants.AUTHORIZEDATA, arrList);
            authorize(authorizeMap, CommonUtil.convertObjToStr(observable.getLblRoiGroupId()));
            AuthType = "";
            super.setOpenForEditBy(observable.getStatusBy());
            singleAuthorizeMap = null;
            arrList = null;
            authorizeMap = null;
        } else {
            AuthType = AUTHORISE;
            HashMap mapParam = new HashMap();
            HashMap whereMap = new HashMap();
            whereMap.put("USER_ID", TrueTransactMain.USER_ID);
            whereMap.put("BRANCH_CODE", TrueTransactMain.BRANCH_ID);
            whereMap.put(CommonConstants.AUTHORIZESTATUS, authorizeStatus);
            mapParam.put(CommonConstants.MAP_WHERE, whereMap);
            whereMap = null;
            mapParam.put(CommonConstants.MAP_NAME, "getSubsidyGroupDetailsAuthorize");
            isFilled = false;
            AuthorizeUI authorizeUI = new AuthorizeUI(this, mapParam);
            authorizeUI.show();
            btnSave.setEnabled(false);
            observable.setStatus();
            lblStatus.setText(observable.getLblStatus());
            btnAuthorize.setEnabled(true);
            btnTabNew.setEnabled(false);
            btnTabSave.setEnabled(false);
            btnTabDelete.setEnabled(false);
        }
    }

    public void authorize(HashMap map, String id) {
        //System.out.println("Authorize Map : " + map);
        if (map.get(CommonConstants.AUTHORIZEDATA) != null) {
            observable.set_authorizeMap(map);
            observable.doAction();
            if (observable.getResult() != ClientConstants.ACTIONTYPE_FAILED) {
                super.setOpenForEditBy(observable.getStatusBy());
                super.removeEditLock(id);
            }
            btnCancelActionPerformed(null);
            observable.setStatus();
            observable.setResultStatus();
            lblStatus.setText(observable.getLblStatus());
        }
    }

    private void tblInterestTableMousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tblInterestTableMousePressed
        // Add your handling code here:
        if (tblInterestTable.getRowCount() > 0) {
            updateOBFields();
            updateMode = true;
            updateTab = tblInterestTable.getSelectedRow();
            observable.setTypenewData(false);
            String st = CommonUtil.convertObjToStr(tblInterestTable.getValueAt(tblInterestTable.getSelectedRow(), 4));
            String ref = CommonUtil.convertObjToStr(tblInterestTable.getValueAt(tblInterestTable.getSelectedRow(), 7));
            ClientUtil.enableDisable(panInterestTable, false);
            observable.populateSubsidyTableDetails(st + ref, updateTab);
            subsidyTableUpdate();
            if (observable.getActionType() == ClientConstants.ACTIONTYPE_AUTHORIZE || observable.getActionType() == ClientConstants.ACTIONTYPE_REJECT
                    || observable.getActionType() == ClientConstants.ACTIONTYPE_VIEW || observable.getActionType() == ClientConstants.ACTIONTYPE_DELETE
                    || observable.getActionType() == ClientConstants.ACTIONTYPE_EXCEPTION) {
                ClientUtil.enableDisable(panCalculations, false);
                ClientUtil.enableDisable(panInterestGroup, false);
            } else {
                buttonEnableDisableCalc(true);
                btnTabNew.setEnabled(false);
                ClientUtil.enableDisable(panCalculations, true);
                ClientUtil.enableDisable(panInterestGroup, true);
                txtCustRateInt.setEnabled(false);
                tdtToDate.setEnabled(false);
                tdtFromDate.setEnabled(false);
            }
        }
        if ((observable.getActionType() != ClientConstants.ACTIONTYPE_DELETE)
                && (observable.getActionType() != ClientConstants.ACTIONTYPE_AUTHORIZE)) {
            String authorizeStatus = CommonUtil.convertObjToStr(tblInterestTable.getValueAt(updateTab, 8));
            if (authorizeStatus != null && authorizeStatus.length() > 0) {
                if (authorizeStatus.equalsIgnoreCase(CommonConstants.STATUS_AUTHORIZED)) {
                    ClientUtil.enableDisable(panCalculations, false);
                    ClientUtil.enableDisable(panGroupData, false);
                    ClientUtil.enableDisable(panGroupnfo, false);
                    ClientUtil.enableDisable(panModeOfOpening, false);
                    btnTabDelete.setEnabled(false);
                    btnTabSave.setEnabled(false);
                    btnTabNew.setEnabled(true);
                }
            } else {
                ClientUtil.enableDisable(panCalculations, true);
                txtCustRateInt.setEnabled(false);
                setInterTabEnableDisable(true);
                tabNewButtons();
                btnTabNew.setEnabled(false);
                tdtToDate.setEnabled(false);
            }
        } else {
            ClientUtil.enableDisable(panCalculations, false);
        }
    }//GEN-LAST:event_tblInterestTableMousePressed

    private void buttonEnableDisableCalc(boolean flag) {
        btnTabNew.setEnabled(flag);
        btnTabSave.setEnabled(flag);
        btnTabDelete.setEnabled(flag);
    }

    private void setInterTabEnableDisable(boolean value) {
        tdtToDate.setEnabled(value);
        tdtDateReceived.setEnabled(value);
        cboInstitution.setEnabled(value);
    }

    private void btnTabDeleteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnTabDeleteActionPerformed
        // Add your handling code here:       
        updateOBFields();
        ClientUtil.enableDisable(panCalculations, false);
        if (tblInterestTable.getSelectedRow() == tblInterestTable.getRowCount() - 1) {
            String st = CommonUtil.convertObjToStr(tblInterestTable.getValueAt(tblInterestTable.getSelectedRow(), 4));
            String ref = CommonUtil.convertObjToStr(tblInterestTable.getValueAt(tblInterestTable.getSelectedRow(), 7));
            Date delfrmDt = DateUtil.getDateMMDDYYYY(CommonUtil.convertObjToStr(tblInterestTable.getValueAt(tblInterestTable.getSelectedRow(), 0)));
            observable.deleteSudsidyTableData(st + ref, tblInterestTable.getSelectedRow(), delfrmDt);
            observable.resetintTable();
            tdtFromDate.setDateValue("");
            txtRateInterest.setText("");
            txtCustRateInt.setText("");
            txtMiscellaneousInt.setText("");
            resetintTable();
            btnTabNew.setEnabled(true);
            setSizeTableData();
        }
    }//GEN-LAST:event_btnTabDeleteActionPerformed

    private void tabSaveDeleteButtons() {
        btnTabNew.setEnabled(true);
        btnTabSave.setEnabled(false);
        btnTabDelete.setEnabled(false);
    }

    private void tabNewButtons() {
        btnTabNew.setEnabled(true);
        btnTabSave.setEnabled(true);
        btnTabDelete.setEnabled(true);
    }

    private void btnTabSaveActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnTabSaveActionPerformed
        // Add your handling code here:          
        updateOBFields();
        if (rdoInterestSubsidy.isSelected()) {
            observable.setRdoInterestSubsidy(true);
        } else {
            observable.setRdoInterestSubsidy(false);
        }
        final String InstitueName = CommonUtil.convertObjToStr(cboInstitution.getSelectedItem());
        if (updateMode == false && tblInterestTable.getRowCount() > 0) {
            int selectrow = tblInterestTable.getRowCount() - 1;
            Date frmdt = DateUtil.getDateMMDDYYYY(tdtFromDate.getDateValue());
            String temdt = CommonUtil.convertObjToStr(tblInterestTable.getValueAt(selectrow, 0));
            Date lastFrmDt = DateUtil.getDateMMDDYYYY(temdt);
            if (DateUtil.dateDiff(frmdt, lastFrmDt) > 0) {
                ClientUtil.showAlertWindow("Already Record Exists In This Date !!! Enter New From Date !!!!");
                return;
            }
        }
        if (updateMode == false && tblInterestTable.getRowCount() > 0) {
            int selectrow = tblInterestTable.getRowCount() - 1;
            Date frmdt = DateUtil.getDateMMDDYYYY(tdtFromDate.getDateValue());
            Date lastFrmDt = DateUtil.getDateMMDDYYYY(CommonUtil.convertObjToStr(tblInterestTable.getValueAt(selectrow, 0)));
            Date todt = DateUtil.getDateMMDDYYYY(CommonUtil.convertObjToStr(tblInterestTable.getValueAt(selectrow, 1)));
            for (int i = 0; i < tblInterestTable.getRowCount(); i++) {
                if (CommonUtil.convertObjToStr(tblInterestTable.getValueAt(i, 8)).equals("")) {
                    String insName = CommonUtil.convertObjToStr(cboInstitution.getSelectedItem());
                    String tblIns = CommonUtil.convertObjToStr(tblInterestTable.getValueAt(i, 4));
                    if (DateUtil.dateDiff(frmdt, lastFrmDt) == 0 && insName.equals(tblIns)) {
                        ClientUtil.showAlertWindow("Same Institutions Not Allowed on Same Period!!!!");
                        return;
                    }
                }
            }
        }

        if (tdtFromDate.getDateValue().length() <= 0) {
            ClientUtil.showAlertWindow("From Date Should Not Be Empty!!!");
            return;
        } else if (txtRateInterest.getText().length() <= 0) {
            ClientUtil.showAlertWindow("Total Rate Of Interest Should Not Be Empty!!!");
            return;
        } else if (txtSubsidyInt.getText().length() <= 0) {
            ClientUtil.showAlertWindow("Subsidy Rate Should Not Be Empty!!!");
            return;
        }

        if (InstitueName.length() > 0) {
            observable.addDataToinstSubsidytable(updateTab, updateMode);
            tblInterestTable.setModel(observable.getTblInterestTable());
            observable.resetintTable();
            resetintTable();
            ClientUtil.enableDisable(tblInterestTable, false);
            resetIntTableTextFieldsSave();
            buttonEnableDisableCalc(false);
            btnTabNew.setEnabled(true);
            setSizeTableData();
        } else {
            ClientUtil.showAlertWindow("Institution Name should Not be Empty!!!");
            return;
        }
    }//GEN-LAST:event_btnTabSaveActionPerformed
    private void resetintTable() {
        cboInstitution.setSelectedItem("");
        tdtDateReceived.setDateValue("");
        tdtToDate.setDateValue("");
        txtSubsidyInt.setText("");
        txtRemarks.setText("");
        txtRefno.setText("");
        rdoInterestSubsidy.setSelected(false);
        rdoInterestSubvention.setSelected(false);
    }

    private void resetIntTableTextFieldsSave() {
        cboInstitution.setEnabled(false);
        txtRefno.setEnabled(false);
        tdtDateReceived.setEnabled(false);
        tdtToDate.setEnabled(false);
        txtSubsidyInt.setEnabled(false);
        txtRemarks.setEnabled(false);
    }

    private void custRoiCalc() {
        Double subsidyTotal = 0.0;
        Double custRoi = 0.0;
        if ((CommonUtil.convertObjToDouble(txtSubsidyInt.getText())) > 0) {
            if (tblInterestTable.getRowCount() > 0) {
                for (int j = 0; j < tblInterestTable.getRowCount(); j++) {
                    if (CommonUtil.convertObjToStr(tblInterestTable.getValueAt(j, 8)).equals("") && CommonUtil.convertObjToStr(tblInterestTable.getValueAt(j, 5)).equals("SUBSIDY")) {
                        subsidyTotal = subsidyTotal + CommonUtil.convertObjToDouble(tblInterestTable.getValueAt(j, 6));
                    }
                }
            }
            if (updateMode) {
                int selectedRow = tblInterestTable.getSelectedRow();
                if (tblInterestTable.getRowCount() > 0) {
                    subsidyTotal = 0.0;
                    for (int j = 0; j < tblInterestTable.getRowCount(); j++) {
                        if (CommonUtil.convertObjToStr(tblInterestTable.getValueAt(j, 8)).equals("") && CommonUtil.convertObjToStr(tblInterestTable.getValueAt(j, 5)).equals("SUBSIDY")) {
                            subsidyTotal = subsidyTotal + CommonUtil.convertObjToDouble(tblInterestTable.getValueAt(j, 6));
                        }
                    }
                    subsidyTotal = subsidyTotal - CommonUtil.convertObjToDouble(tblInterestTable.getValueAt(selectedRow, 6));
                }
            }

            if (rdoInterestSubsidy.isSelected()) {
                subsidyTotal = subsidyTotal + CommonUtil.convertObjToDouble(txtSubsidyInt.getText());
            }
            
            
             if (CommonUtil.convertObjToDouble(txtRateInterest.getText()) < CommonUtil.convertObjToDouble(subsidyTotal)+ CommonUtil.convertObjToDouble(txtMiscellaneousInt.getText())) {
                ClientUtil.showMessageWindow("Exceeding The Total Rate Of Interest Not Allowed!!!");
                txtSubsidyInt.setText("");
                return;
            }

            custRoi = CommonUtil.convertObjToDouble(txtRateInterest.getText()) - CommonUtil.convertObjToDouble(txtMiscellaneousInt.getText()) - subsidyTotal;
            //System.out.println("custRoi&&&&&&&" + custRoi);
            int ix = (int) (custRoi * 100.0);
            custRoi = ((double) ix) / 100.0;
            txtCustRateInt.setText(CommonUtil.convertObjToStr(custRoi));
        }
    }

    private void btnTabNewActionPerformed() {
        try {
            final String mandatoryMessage = new MandatoryCheck().checkMandatory(getClass().getName(), panGroupnfo);
            if (mandatoryMessage.length() > 0) {
                displayAlert(mandatoryMessage);
                return;
            }
            if (!observable.isRdoInterestType_Debit() && !observable.isRdoInterestType_Credit()) {
                displayAlert(resourceBundle.getString("INT_TYPE_WARNING"));
                return;
            }

            if (tblInterestTable.getRowCount() > 0) {
                updateTab = tblInterestTable.getRowCount() - 1;
                String AuthStat = CommonUtil.convertObjToStr(tblInterestTable.getValueAt(updateTab, 8));
                if (AuthStat.equals("")) {
                    resetIntTableTextFieldsNew();
                    tdtFromDate.setDateValue(CommonUtil.convertObjToStr(tblInterestTable.getValueAt(updateTab, 0)));
                    tdtToDate.setDateValue(CommonUtil.convertObjToStr(tblInterestTable.getValueAt(updateTab, 1)));
                    txtRateInterest.setText(CommonUtil.convertObjToStr(tblInterestTable.getValueAt(updateTab, 2)));
                    txtCustRateInt.setText(CommonUtil.convertObjToStr(tblInterestTable.getValueAt(updateTab, 3)));
                } else {
                    tdtFromDate.setDateValue("");
                    resetIntTableTextFieldsNext();
                }
            }

            updateOBFields();
            observable.resetintTable();
            ClientUtil.enableDisable(panCalculations, true);
            tabNewButtons();
            updateMode = false;
            tdtToDate.setEnabled(false);
            txtCustRateInt.setEnabled(false);
            btnTabNew.setEnabled(false);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void resetIntTableTextFieldsNew() {
        cboInstitution.setSelectedItem("");
        txtRefno.setText("");
        tdtDateReceived.setDateValue("");
        tdtToDate.setDateValue("");
        txtSubsidyInt.setText("");
        txtRemarks.setText("");
        cboInstitution.setEnabled(true);
        txtRefno.setEnabled(true);
        tdtDateReceived.setEnabled(true);
        txtSubsidyInt.setEnabled(true);
        txtRemarks.setEnabled(true);
    }

    private void resetIntTableTextFieldsNext() {
        cboInstitution.setSelectedItem("");
        txtRefno.setText("");
        tdtDateReceived.setDateValue("");
        tdtToDate.setDateValue("");
        txtSubsidyInt.setText("");
        txtRemarks.setText("");
        txtRateInterest.setText("");
        txtMiscellaneousInt.setText("");
        txtCustRateInt.setText("");
        cboInstitution.setEnabled(true);
        txtRefno.setEnabled(true);
        tdtDateReceived.setEnabled(true);
        txtSubsidyInt.setEnabled(true);
        txtRemarks.setEnabled(true);
    }

    private void setSizeTableData() {
        tblInterestTable.getColumnModel().getColumn(0).setPreferredWidth(50);
        tblInterestTable.getColumnModel().getColumn(1).setPreferredWidth(50);
        tblInterestTable.getColumnModel().getColumn(2).setPreferredWidth(30);
        tblInterestTable.getColumnModel().getColumn(3).setPreferredWidth(30);
        tblInterestTable.getColumnModel().getColumn(4).setPreferredWidth(80);
        tblInterestTable.getColumnModel().getColumn(5).setPreferredWidth(45);
        tblInterestTable.getColumnModel().getColumn(6).setPreferredWidth(30);
        tblInterestTable.getColumnModel().getColumn(7).setPreferredWidth(30);
        tblInterestTable.getColumnModel().getColumn(8).setPreferredWidth(40);
    }

    private void btnTabNewActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnTabNewActionPerformed
        // Add your handling code here:       
        btnTabNewActionPerformed();
    }//GEN-LAST:event_btnTabNewActionPerformed

    private void btnCloseActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCloseActionPerformed
        // Add your handling code here:
        cifClosingAlert();
    }//GEN-LAST:event_btnCloseActionPerformed

    private void btnPrintActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnPrintActionPerformed
        // Add your handling code here:
    }//GEN-LAST:event_btnPrintActionPerformed

    private void btnCancelActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCancelActionPerformed
        // Add your handling code here:
        if (observable.getAuthorizeStatus() != null) {
            super.removeEditLock(lblGroupIdDesc.getText());
        }
        tabInterestMaintenance.resetVisits();
        resetUI();
        ClientUtil.clearAll(this);
        ClientUtil.enableDisable(this, false);
        setButtonEnableDisable();
        tabSaveDeleteButtons();
        btnTabNew.setEnabled(false);
        observable.setActionType(ClientConstants.ACTIONTYPE_CANCEL);
        observable.setStatus();
        isFilled = false;
        viewType = -1;
        setModified(false);
        btnReject.setEnabled(true);
        btnAuthorize.setEnabled(true);
        btnException.setEnabled(true);
        btnNew.setEnabled(true);
        btnEdit.setEnabled(true);
        btnDelete.setEnabled(true);
        btnView.setEnabled(true);
        observable.resetSubsidytableValues();
    }//GEN-LAST:event_btnCancelActionPerformed
    private void resetUI() {
        observable.resetForm();
        observable.resetintTable();
        observable.resetLable();
        observable.resetProductTab();
        observable.resetCategoryTab();
        setSizeTableData();
    }
    private void btnSaveActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSaveActionPerformed
        // Add your handling code here: 
        try {
            final String mandatoryMessage = new MandatoryCheck().checkMandatory(getClass().getName(), panGroupnfo);
            if (mandatoryMessage.length() > 0) {
                displayAlert(mandatoryMessage);
                return;
            }
            if (observable.getActionType() != ClientConstants.ACTIONTYPE_DELETE && mandatoryMessage.length() > 0) {
                displayAlert(mandatoryMessage);
            } else {
                if (!(observable.getRowCount() > 0)) {
                    displayAlert(resourceBundle.getString("NOROWWARNING"));
                    return;
                }
            }
            if (observable.getActionType() != ClientConstants.ACTIONTYPE_DELETE && cboInstitution.getSelectedIndex() > 0) {
                ClientUtil.showMessageWindow("First Save the Newly Added Record!!!");
                return;
            }
            updateOBFields();
            if (tblInterestTable.getRowCount() > 0 && tblInterestTable != null) {
                updateTableValuesNew();
                updatEndDate();
            }
            savePerformed();
            observable.resetSubsidytableValues();
            btnCancel.setEnabled(true);
            btnSave.setEnabled(false);            
        } catch (Exception e) {
            e.printStackTrace();
        }
    }//GEN-LAST:event_btnSaveActionPerformed

    private void savePerformed() {
        observable.doAction();
        if (observable.getResult() != ClientConstants.ACTIONTYPE_FAILED) {
            btnCancelActionPerformed(null);
            btnCancel.setEnabled(true);
            lblStatus.setText(ClientConstants.RESULT_STATUS[observable.getResult()]);
        }
        btnCancel.setEnabled(true);
        btnAuthorize.setEnabled(true);
        btnReject.setEnabled(true);
        btnException.setEnabled(true);
        setModified(false);
        ClientUtil.clearAll(this);
    }

    private void updateTableValuesNew() {
//        Double custroi = CommonUtil.convertObjToDouble(tblInterestTable.getValueAt(tblInterestTable.getRowCount() - 1, 3));
        Double custroi = CommonUtil.convertObjToDouble(txtCustRateInt.getText());
        Double miscInt= CommonUtil.convertObjToDouble(txtMiscellaneousInt.getText());
        for (int i = 0; i < tblInterestTable.getRowCount(); i++) {
            if (CommonUtil.convertObjToStr(tblInterestTable.getValueAt(i, 8)).equals("")) {
                String insName = CommonUtil.convertObjToStr(tblInterestTable.getValueAt(i, 4));
                String refno = CommonUtil.convertObjToStr(tblInterestTable.getValueAt(i, 7));
                String AuthStatus = CommonUtil.convertObjToStr(tblInterestTable.getValueAt(i, 8));
                observable.updatecustroi(insName + refno, custroi,miscInt);
            }
        }
        for (int i = 0; i < tblInterestTable.getRowCount(); i++) {
            String insName = CommonUtil.convertObjToStr(tblInterestTable.getValueAt(i, 4));
            String refno = CommonUtil.convertObjToStr(tblInterestTable.getValueAt(i, 7));
            String AuthStatus = CommonUtil.convertObjToStr(tblInterestTable.getValueAt(i, 8));
            observable.setAuthStatus(insName + refno, AuthStatus);
        }
    }

    public void updatEndDate() {
        for (int i = 0; i < tblInterestTable.getRowCount(); i++) {
            Date Todate = DateUtil.getDateMMDDYYYY(CommonUtil.convertObjToStr(tblInterestTable.getValueAt(i, 1)));
            String insnm = CommonUtil.convertObjToStr(tblInterestTable.getValueAt(i, 4));
            String refno = CommonUtil.convertObjToStr(tblInterestTable.getValueAt(i, 7));
            if (Todate != null) {
                observable.setTodate(insnm + refno, Todate);
            }
        }
    }

    private void displayAlert(String message) {
        CMandatoryDialog cmd = new CMandatoryDialog();
        cmd.setMessage(message);
        cmd.show();
    }

    private void btnDeleteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnDeleteActionPerformed
        // Add your handling code here:
        resetUI();
        observable.setActionType(ClientConstants.ACTIONTYPE_DELETE);
        popUp(DELETE);
        lblStatus.setText("Delete");
        btnReject.setEnabled(false);
        btnAuthorize.setEnabled(false);
        btnException.setEnabled(false);
    }//GEN-LAST:event_btnDeleteActionPerformed

    private void btnEditActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnEditActionPerformed
        // Add your handling code here:                                                                 
        resetUI();
        observable.setActionType(ClientConstants.ACTIONTYPE_EDIT);
        popUp(EDIT);
        lblStatus.setText("Edit");
        btnReject.setEnabled(false);
        btnAuthorize.setEnabled(false);
        btnException.setEnabled(false);
        tblProduct.setEnabled(true);
        tblCategory.setEnabled(true);
    }//GEN-LAST:event_btnEditActionPerformed
    private void popUp(int field) {
        final HashMap viewMap = new HashMap();
        if (field == EDIT || field == VIEW) {
            viewType = field;
            HashMap map = new HashMap();
            map.put("BRANCH_CODE", TrueTransactMain.BRANCH_ID);
            viewMap.put(CommonConstants.MAP_WHERE, map);
            viewMap.put(CommonConstants.MAP_NAME, "getSubsidyRateDetailsEdit");
        }
        if (field == DELETE) {
            viewType = field;
            HashMap map = new HashMap();
            map.put("BRANCH_CODE", TrueTransactMain.BRANCH_ID);
            viewMap.put(CommonConstants.MAP_WHERE, map);
            viewMap.put(CommonConstants.MAP_NAME, "getSubsidyRateDetailsDelete");
        }
        
        new ViewAll(this, viewMap, false).show();
    }

    public void fillData(Object param) {
        isFilled = true;
        final HashMap hash = (HashMap) param;
        if (observable.getActionType() == ClientConstants.ACTIONTYPE_EDIT
                || observable.getActionType() == ClientConstants.ACTIONTYPE_DELETE
                || observable.getActionType() == ClientConstants.ACTIONTYPE_VIEW) {
            observable.setLblroiGroupId(CommonUtil.convertObjToStr(hash.get("ROI_GROUP_ID")));
            observable.getData(hash);
            HashMap dispMap = new HashMap();
            hash.put("ROI_GROUP_ID", lblGroupIdDesc.getText());
            updateRoiDetails();
            enableDisableEdit();
            setSizeTableData();
            updateMode = false;
        } else if (observable.getActionType() == ClientConstants.ACTIONTYPE_AUTHORIZE
                || observable.getActionType() == ClientConstants.ACTIONTYPE_EXCEPTION
                || observable.getActionType() == ClientConstants.ACTIONTYPE_REJECT) {
            ClientUtil.enableDisable(panCalculations, false);
            ClientUtil.enableDisable(panGroupData, false);
            ClientUtil.enableDisable(panModeOfOpening, false);
            ClientUtil.enableDisable(panGroupnfo, false);            
            this.setButtonEnableDisable();
            btnReject.setEnabled(true);
            btnException.setEnabled(true);
            observable.setLblroiGroupId(CommonUtil.convertObjToStr(hash.get("ROI_GROUP_ID")));
            observable.getData(hash);
            updateRoiDetails();
            setSizeTableData();
        }
        if(tblInterestTable.getRowCount()>0){
        String authorizeStatus = CommonUtil.convertObjToStr(tblInterestTable.getValueAt(0, 8));
        if (authorizeStatus != null && authorizeStatus.length() > 0) {
            if (authorizeStatus.equalsIgnoreCase(CommonConstants.STATUS_AUTHORIZED)) {
                ClientUtil.enableDisable(panGroupData, false);
                ClientUtil.enableDisable(panGroupnfo, false);
                ClientUtil.enableDisable(panModeOfOpening, false);
            }
        }
        }
    }

    private void updateRoiDetails() {
        lblGroupIdDesc.setText(observable.getLblRoiGroupId());
        txtGroupName.setText(observable.getTxtGroupName());
        cboProdType.setSelectedItem(observable.getCboProdType());
        rdoInterestType_Debit.setSelected(observable.isRdoInterestType_Debit());
        rdoInterestType_Credit.setSelected(observable.isRdoInterestType_Credit());
    }

    private void enableDisableEdit() {
        btnCancel.setEnabled(true);
        btnTabNew.setEnabled(true);
        btnTabSave.setEnabled(false);
        btnTabDelete.setEnabled(false);
        txtGroupName.setEnabled(true);
        btnSave.setEnabled(true);
        ClientUtil.enableDisable(panGroupData, true);
        ClientUtil.enableDisable(panModeOfOpening, true);
    }

    private void btnNewActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnNewActionPerformed
        // Add your handling code here:
        tdtToDate.setEnabled(false);
        observable.resetForm();
        observable.resetLable();
        ClientUtil.enableDisable(this, true);
        ClientUtil.enableDisable(panGroupData, true);
        tblProduct.setEnabled(true);
        tblCategory.setEnabled(true);
        ClientUtil.enableDisable(panCalculations, false);
        tabSaveDeleteButtons();
        btnTabNew.setEnabled(true);
        observable.setActionType(ClientConstants.ACTIONTYPE_NEW);
        setButtonEnableDisable();
        observable.setStatus();
        observable.getCategoryData();
        observable.ttNotifyObservers();
        setModified(true);
        btnReject.setEnabled(false);
        btnAuthorize.setEnabled(false);
        btnException.setEnabled(false);
    }//GEN-LAST:event_btnNewActionPerformed

    private void mitCloseActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mitCloseActionPerformed
        // Add your handling code here:
        btnCloseActionPerformed(evt);
    }//GEN-LAST:event_mitCloseActionPerformed

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

    private void rdoInterestSubsidyActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_rdoInterestSubsidyActionPerformed
        // TODO add your handling code here:
        if (rdoInterestSubsidy.isSelected()) {
            lblSubsidyInt.setText("Subsidy Rate of Interest");
        }
    }//GEN-LAST:event_rdoInterestSubsidyActionPerformed

    private void rdoInterestSubventionActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_rdoInterestSubventionActionPerformed
        // TODO add your handling code here:
        if (rdoInterestSubvention.isSelected()) {
            lblSubsidyInt.setText("Interest Subvention Rate");
        }
    }//GEN-LAST:event_rdoInterestSubventionActionPerformed

    private void tdtFromDateFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_tdtFromDateFocusLost
        // TODO add your handling code here:
        if (tdtFromDate.getDateValue() != null && tdtFromDate.getDateValue().length() > 0) {
            if (tblInterestTable.getRowCount() > 0) {
                Date currentFromDt = DateUtil.getDateMMDDYYYY(tdtFromDate.getDateValue());
                String TemppreFromDt = CommonUtil.convertObjToStr(tblInterestTable.getValueAt(tblInterestTable.getRowCount() - 1, 0));
                Date preFromDt = DateUtil.getDateMMDDYYYY(TemppreFromDt);
                if (currentFromDt.compareTo(preFromDt) < 0) {
                    ClientUtil.showMessageWindow("From Date Should Be Greater Than Previous Record From Date !!!!");
                    tdtFromDate.setDateValue("");
                    return;
                }
                if ((DateUtil.dateDiff(preFromDt, currentFromDt) > 0) && CommonUtil.convertObjToStr(tblInterestTable.getValueAt(tblInterestTable.getRowCount() - 1, 8)).equals("")) {
                    ClientUtil.showMessageWindow("Record Pending For Authorization , Cannot Add New Group !!!!");
                    tdtFromDate.setDateValue("");
                    txtRateInterest.setText("");
                    txtCustRateInt.setText("");
                    txtMiscellaneousInt.setText("");
                    ClientUtil.enableDisable(panInsDetails,false);                    
                    return;
                }
                if ((DateUtil.dateDiff(preFromDt, currentFromDt) == 0) && !(CommonUtil.convertObjToStr(tblInterestTable.getValueAt(tblInterestTable.getRowCount() - 1, 8)).equals(""))) {
                    ClientUtil.showMessageWindow("This Date Record Already Authorized , Cannot Add New Group !!!!");
                    tdtFromDate.setDateValue("");
                    return;
                }
                if (preFromDt.equals(currentFromDt)) {
                    resetIntTableTextFieldsNew();
                } else {
                    resetIntTableTextFieldsNext();
                }
            }
        }
    }//GEN-LAST:event_tdtFromDateFocusLost

    private void txtSubsidyIntFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtSubsidyIntFocusLost
        // TODO add your handling code here:        
        if (!(rdoInterestSubsidy.isSelected() || rdoInterestSubvention.isSelected())) {
            ClientUtil.showMessageWindow("Select Subsidy or Subvention Radio Button!!!");
            txtSubsidyInt.setText("");
            return;
        }
        custRoiCalc();
    }//GEN-LAST:event_txtSubsidyIntFocusLost

    private void txtMiscellaneousIntFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtMiscellaneousIntFocusLost
        // TODO add your handling code here:
        if (txtMiscellaneousInt.getText().length() == 0) {
            txtMiscellaneousInt.setText("0");
        }
        custRoiCalc();
    }//GEN-LAST:event_txtMiscellaneousIntFocusLost

    private void subsidyTableUpdate() {
        txtRateInterest.setText(CommonUtil.convertObjToStr(observable.getTxtRateInterest()));
        txtCustRateInt.setText(CommonUtil.convertObjToStr(observable.getTxtCustRateInt()));
        txtMiscellaneousInt.setText(CommonUtil.convertObjToStr(observable.getTxtMiscellaneousInt()));
        cboInstitution.setSelectedItem(observable.getCboInstitution());
        txtRefno.setText(observable.getTxtRefno());
        tdtDateReceived.setDateValue(observable.getTdtDateReceived());
        tdtFromDate.setDateValue(observable.getTdtFromDate());
        tdtToDate.setDateValue(observable.getTdtToDate());
        txtSubsidyInt.setText(CommonUtil.convertObjToStr(observable.getTxtSubsidyInt()));
        txtRemarks.setText(observable.getTxtRemarks());
        rdoInterestSubsidy.setSelected(observable.isRdoInterestSubsidy());
        rdoInterestSubvention.setSelected(observable.isRdoInterestSubvention());
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        new InterestSubsidyRateMaintenanceUI().show();
    }
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private com.see.truetransact.uicomponent.CButton btnAuthorize;
    private com.see.truetransact.uicomponent.CButton btnCancel;
    private com.see.truetransact.uicomponent.CButton btnClose;
    private com.see.truetransact.uicomponent.CButton btnDelete;
    private com.see.truetransact.uicomponent.CButton btnEdit;
    private com.see.truetransact.uicomponent.CButton btnException;
    private com.see.truetransact.uicomponent.CButton btnNew;
    private com.see.truetransact.uicomponent.CButton btnPrint;
    private com.see.truetransact.uicomponent.CButton btnReject;
    private com.see.truetransact.uicomponent.CButton btnSave;
    private com.see.truetransact.uicomponent.CButton btnTabDelete;
    private com.see.truetransact.uicomponent.CButton btnTabNew;
    private com.see.truetransact.uicomponent.CButton btnTabSave;
    private com.see.truetransact.uicomponent.CButton btnView;
    private com.see.truetransact.uicomponent.CComboBox cboInstitution;
    private com.see.truetransact.uicomponent.CComboBox cboProdType;
    private com.see.truetransact.uicomponent.CLabel lblCustRateInt;
    private com.see.truetransact.uicomponent.CLabel lblDateReceived;
    private com.see.truetransact.uicomponent.CLabel lblFromDate;
    private com.see.truetransact.uicomponent.CLabel lblGroupId;
    private com.see.truetransact.uicomponent.CLabel lblGroupIdDesc;
    private com.see.truetransact.uicomponent.CLabel lblGroupName;
    private com.see.truetransact.uicomponent.CLabel lblInstitution;
    private com.see.truetransact.uicomponent.CLabel lblInterestType;
    private com.see.truetransact.uicomponent.CLabel lblMiscellaneousInt;
    private com.see.truetransact.uicomponent.CLabel lblMiscellaneousIntPer;
    private com.see.truetransact.uicomponent.CLabel lblMsg;
    private com.see.truetransact.uicomponent.CLabel lblODI_Per;
    private com.see.truetransact.uicomponent.CLabel lblPenalInterestPer;
    private com.see.truetransact.uicomponent.CLabel lblProdType;
    private com.see.truetransact.uicomponent.CLabel lblRateInterest;
    private com.see.truetransact.uicomponent.CLabel lblRateInterestPer;
    private com.see.truetransact.uicomponent.CLabel lblRefno;
    private com.see.truetransact.uicomponent.CLabel lblRemarks;
    private com.see.truetransact.uicomponent.CLabel lblSpace1;
    private com.see.truetransact.uicomponent.CLabel lblSpace2;
    private com.see.truetransact.uicomponent.CLabel lblSpace3;
    private com.see.truetransact.uicomponent.CLabel lblSpace4;
    private com.see.truetransact.uicomponent.CLabel lblSpace5;
    private com.see.truetransact.uicomponent.CLabel lblSpace70;
    private com.see.truetransact.uicomponent.CLabel lblSpace71;
    private com.see.truetransact.uicomponent.CLabel lblSpace72;
    private com.see.truetransact.uicomponent.CLabel lblSpace73;
    private com.see.truetransact.uicomponent.CLabel lblSpace74;
    private com.see.truetransact.uicomponent.CLabel lblSpace75;
    private com.see.truetransact.uicomponent.CLabel lblSpaces;
    private com.see.truetransact.uicomponent.CLabel lblStatus;
    private com.see.truetransact.uicomponent.CLabel lblSubsidyInt;
    private com.see.truetransact.uicomponent.CLabel lblToDate;
    private com.see.truetransact.uicomponent.CMenuBar mbrMain;
    private javax.swing.JMenuItem mitCancel;
    private javax.swing.JMenuItem mitClose;
    private javax.swing.JMenuItem mitDelete;
    private javax.swing.JMenuItem mitEdit;
    private javax.swing.JMenuItem mitNew;
    private javax.swing.JMenuItem mitSave;
    private javax.swing.JMenu mnuProcess;
    private com.see.truetransact.uicomponent.CPanel panButtons;
    private com.see.truetransact.uicomponent.CPanel panCalculations;
    private com.see.truetransact.uicomponent.CPanel panGroupData;
    private com.see.truetransact.uicomponent.CPanel panGroupnfo;
    private com.see.truetransact.uicomponent.CPanel panInsDetails;
    private com.see.truetransact.uicomponent.CPanel panInterestCalc;
    private com.see.truetransact.uicomponent.CPanel panInterestCalculation;
    private com.see.truetransact.uicomponent.CPanel panInterestGroup;
    private com.see.truetransact.uicomponent.CPanel panInterestMaintenance;
    private com.see.truetransact.uicomponent.CPanel panInterestTable;
    private com.see.truetransact.uicomponent.CPanel panModeOfOpening;
    private com.see.truetransact.uicomponent.CPanel panModeOfOpening1;
    private com.see.truetransact.uicomponent.CPanel panStatus;
    private com.see.truetransact.uicomponent.CButtonGroup rdgInterestType;
    private com.see.truetransact.uicomponent.CRadioButton rdoInterestSubsidy;
    private com.see.truetransact.uicomponent.CRadioButton rdoInterestSubvention;
    private com.see.truetransact.uicomponent.CRadioButton rdoInterestType_Credit;
    private com.see.truetransact.uicomponent.CRadioButton rdoInterestType_Debit;
    private javax.swing.JSeparator sptCancel;
    private javax.swing.JSeparator sptDelete;
    private com.see.truetransact.uicomponent.CScrollPane srpCategory;
    private com.see.truetransact.uicomponent.CScrollPane srpInterestTable;
    private com.see.truetransact.uicomponent.CScrollPane srpProduct;
    private com.see.truetransact.uicomponent.CTabbedPane tabInterestMaintenance;
    private com.see.truetransact.uicomponent.CTable tblCategory;
    private com.see.truetransact.uicomponent.CTable tblInterestTable;
    private com.see.truetransact.uicomponent.CTable tblProduct;
    private javax.swing.JToolBar tbrHead;
    private com.see.truetransact.uicomponent.CDateField tdtDateReceived;
    private com.see.truetransact.uicomponent.CDateField tdtFromDate;
    private com.see.truetransact.uicomponent.CDateField tdtToDate;
    private com.see.truetransact.uicomponent.CTextField txtCustRateInt;
    private com.see.truetransact.uicomponent.CTextField txtGroupName;
    private com.see.truetransact.uicomponent.CTextField txtMiscellaneousInt;
    private com.see.truetransact.uicomponent.CTextField txtRateInterest;
    private com.see.truetransact.uicomponent.CTextField txtRefno;
    private com.see.truetransact.uicomponent.CTextField txtRemarks;
    private com.see.truetransact.uicomponent.CTextField txtSubsidyInt;
    // End of variables declaration//GEN-END:variables
}
