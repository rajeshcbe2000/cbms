/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition. 
 * 
 * PayrollSalaryStructureUI.java
 *
 * Created on May 25, 2004, 5:18 PM
 */
package com.see.truetransact.ui.payroll.salaryStructure;

import com.see.truetransact.clientproxy.ProxyParameters;
import com.see.truetransact.clientutil.CMandatoryDialog;
import com.see.truetransact.clientutil.ClientConstants;
import com.see.truetransact.clientutil.ClientUtil;
import com.see.truetransact.clientutil.ComboBoxModel;
import com.see.truetransact.commonutil.CommonConstants;
import com.see.truetransact.commonutil.CommonUtil;
import com.see.truetransact.ui.common.viewall.ViewAll;
import com.see.truetransact.uicomponent.CInternalFrame;
import com.see.truetransact.uimandatory.UIMandatoryField;
import com.see.truetransact.uivalidation.CurrencyValidation;
import com.see.truetransact.uivalidation.NumericValidation;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Observable;
import javax.swing.JFrame;

/**
 *
 * @author anjuanand
 */
public class PayrollSalaryStructureUI extends CInternalFrame implements java.util.Observer, UIMandatoryField {

    private HashMap mandatoryMap;
    private SalaryStructureOB observable;
    private SalaryStructureMRB objMandatoryRB;
    private Date curDate = null;
    java.util.ResourceBundle resourceBundle = java.util.ResourceBundle.getBundle("com.see.truetransact.ui.payroll.salaryStructure.SalaryStructureRB", ProxyParameters.LANGUAGE);
    private boolean selectedSingleRow = false;
    int viewType = -1;
    final int EDIT = 0, DELETE = 1;
    private boolean isFilled = false;
    private String view = "";

    /**
     * Creates new form BeanForm
     */
    public PayrollSalaryStructureUI() {
        initComponents();
        initSetUp();
    }

    private void initSetUp() {
        setFieldNames();
        internationalize();
        setMandatoryHashMap();
        setHelpMessage();
        setObservable();
        setMaxLength();
        initComponentData();
        enableDisableAllscreens(false);
        rdoStagnationIncrement_Yes.setEnabled(false);
        rdoStagnationIncrement_No.setEnabled(false);
        setUp(ClientConstants.ACTIONTYPE_CANCEL, false);
        setButtonEnableDisable();
        btnDelete.setEnabled(true);
        curDate = ClientUtil.getCurrentDate();
        this.resetUIData();
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
        panSalaryDetails.setName("panSalaryDetails");
        lblDesignation.setName("lblDesignation");
        cboDesignation.setName("cboDesignation");
        lblScaleId.setName("lblScaleId");
        txtScaleId.setName("txtScaleId");
        lblVersionNo.setName("lblVersionNo");
        txtVersionNo.setName("txtVersionNo");
        lblFromDate.setName("lblFromDate");
        tdtFromDate.setName("tdtFromDate");
        lblToDate.setName("lblToDate");
        tdtToDate.setName("tdtToDate");
        lblStartingAmount.setName("lblStartingAmount");
        txtStartingAmount.setName("txtStartingAmount");
        lblEndingAmount.setName("lblEndingAmount");
        txtEndingAmount.setName("txtEndingAmount");
        lblIncrementAmount.setName("lblIncrementAmount");
        txtIncrementAmount.setName("txtIncrementAmount");
        lblNoOfIncrements.setName("lblNoOfIncrements");
        txtNoOfIncrements.setName("txtNoOfIncrements");
        lblIncrementFrequency.setName("lblIncrementFrequency");
        cboIncrementFrequency.setName("cboIncrementFrequency");
        lblStagnationIncrement.setName("lblStagnationIncrement");
        panStagnationIncrement.setName("panStagnationIncrement");
        rdoStagnationIncrement_Yes.setName("rdoStagnationIncrement_Yes");
        rdoStagnationIncrement_No.setName("rdoStagnationIncrement_No");
        lblSalaryStructureStagnationIncrement.setName("lblSalaryStructureStagnationIncrement");
        lblStagIncrAmount.setName("lblStagIncrAmount");
        txtStagIncrAmount.setName("txtStagIncrAmount");
        lblStagNoOfIncrements.setName("lblStagNoOfIncrements");
        txtStagNoOfIncrements.setName("txtStagNoOfIncrements");
        lblIncrementPeriod.setName("lblIncrementPeriod");
        cboIncrementPeriod.setName("cboIncrementPeriod");
        panIncrementFrequency.setName("panIncrementFrequency");
        panSalaryStructureButtons.setName("panSalaryStructureButtons");
        btnSalaryStructureNew.setName("btnSalaryStructureNew");
        btnSalaryStructureSave.setName("btnSalaryStructureSave");
        btnSalaryStructureDelete.setName("btnSalaryStructureDelete");
        panSalaryStructureInfo.setName("panSalaryStructureInfo");
        panSalaryStructureTable.setName("panSalaryStructureTable");
        tblSalaryStructure.setName("tblSalaryStructure");
    }
    /*
     * Auto Generated Method - internationalize() This method used to assign
     * display texts from the Resource Bundle File.
     */

    private void internationalize() {
        resourceBundle = new SalaryStructureRB();
        btnAuthorize.setText(resourceBundle.getString("btnAuthorize"));
        btnCancel.setText(resourceBundle.getString("btnCancel"));
        btnClose.setText(resourceBundle.getString("btnClose"));
        btnDelete.setText(resourceBundle.getString("btnDelete"));
        btnEdit.setText(resourceBundle.getString("btnEdit"));
        btnException.setText(resourceBundle.getString("btnException"));
        btnNew.setText(resourceBundle.getString("btnNew"));
        btnPrint.setText(resourceBundle.getString("btnPrint"));
        btnReject.setText(resourceBundle.getString("btnReject"));
        btnSave.setText(resourceBundle.getString("btnSave"));
        lblDesignation.setText(resourceBundle.getString("lblDesignation"));
        lblScaleId.setText(resourceBundle.getString("lblScaleId"));
        lblVersionNo.setText(resourceBundle.getString("lblVersionNo"));
        lblFromDate.setText(resourceBundle.getString("lblFromDate"));
        lblToDate.setText(resourceBundle.getString("lblToDate"));
        lblStartingAmount.setText(resourceBundle.getString("lblStartingAmount"));
        lblEndingAmount.setText(resourceBundle.getString("lblEndingAmount"));
        lblIncrementAmount.setText(resourceBundle.getString("lblIncrementAmount"));
        lblNoOfIncrements.setText(resourceBundle.getString("lblNoOfIncrements"));
        lblIncrementFrequency.setText(resourceBundle.getString("lblIncrementFrequency"));
        lblStagnationIncrement.setText(resourceBundle.getString("lblStagnationIncrement"));
        lblSalaryStructureStagnationIncrement.setText(resourceBundle.getString("lblSalaryStructureStagnationIncrement"));
        lblStagIncrAmount.setText(resourceBundle.getString("lblStagIncrAmount"));
        lblStagNoOfIncrements.setText(resourceBundle.getString("lblStagNoOfIncrements"));
        lblIncrementPeriod.setText(resourceBundle.getString("lblIncrementPeriod"));
        rdoStagnationIncrement_Yes.setText(resourceBundle.getString("rdoStagnationIncrement_Yes"));
        rdoStagnationIncrement_No.setText(resourceBundle.getString("rdoStagnationIncrement_No"));
    }
    /*
     * Auto Generated Method - setHelpMessage() This method shows tooltip help
     * for all the input fields available in the UI. It needs the Mandatory
     * Resource Bundle object. Help display Label name should be lblMsg.
     */

    public void setHelpMessage() {
        objMandatoryRB = new SalaryStructureMRB();
        cboDesignation.setHelpMessage(lblMsg, objMandatoryRB.getString("cboDesignation"));
        tdtFromDate.setHelpMessage(lblMsg, objMandatoryRB.getString("tdtFromDate"));
        txtStartingAmount.setHelpMessage(lblMsg, objMandatoryRB.getString("txtStartingAmount"));
        txtEndingAmount.setHelpMessage(lblMsg, objMandatoryRB.getString("txtEndingAmount"));
        txtIncrementAmount.setHelpMessage(lblMsg, objMandatoryRB.getString("txtIncrementAmount"));
        txtNoOfIncrements.setHelpMessage(lblMsg, objMandatoryRB.getString("txtNoOfIncrements"));
        cboIncrementFrequency.setHelpMessage(lblMsg, objMandatoryRB.getString("cboIncrementFrequency"));
    }

    private void setObservable() {
        observable = SalaryStructureOB.getInstance();
        observable.addObserver(this);
    }

    public void setUp(int actionType, boolean isEnable) {
        ClientUtil.enableDisable(this, isEnable);
    }

    public void updateOBFields() {
        observable.setCboDesignation(CommonUtil.convertObjToStr(cboDesignation.getSelectedItem()));
        observable.setTxtScaleId(txtScaleId.getText());
        observable.setTxtVersionNo(txtVersionNo.getText());
        observable.setTdtFromDate(tdtFromDate.getDateValue());
        observable.setTdtToDate(tdtToDate.getDateValue());
        observable.setTxtStartingAmount(txtStartingAmount.getText());
        observable.setTxtEndingAmount(CommonUtil.convertObjToDouble(txtEndingAmount.getText()));
        observable.setTxtIncrementAmount(CommonUtil.convertObjToDouble(txtIncrementAmount.getText()));
        observable.setTxtNoOfIncrements(txtNoOfIncrements.getText());
        observable.setCboIncrementFrequency(CommonUtil.convertObjToStr(cboIncrementFrequency.getSelectedItem()));
        if (rdoStagnationIncrement_Yes.isSelected()) {
            observable.setRdoStagnationIncrement_Yes(true);
            observable.setRdoStagnationIncrement_No(false);
        } else {
            observable.setRdoStagnationIncrement_No(true);
            observable.setRdoStagnationIncrement_Yes(false);
        }
        observable.setTxtStagIncrAmount(txtStagIncrAmount.getText());
        observable.setTxtStagNoOfIncrements(txtStagNoOfIncrements.getText());
        observable.setCboIncrementPeriod(CommonUtil.convertObjToStr(cboIncrementPeriod.getSelectedItem()));
    }

    private void resetUIData() {
        this.viewType = -1;
    }

    public void insertTableData(String e) {
        double startAmt = 0.00;
        double endAmt = 0.00;
        double incrementAmt = 0.00;
        int incrementCount = 0;
        int i = tblSalaryStructure.getRowCount();
        int selectedindex = tblSalaryStructure.getSelectedRow();
        if (e.equals("edit")) {
            i = tblSalaryStructure.getSelectedRow();
            if (i == 0 || selectedindex == 0) {
                startAmt = CommonUtil.convertObjToDouble(txtStartingAmount.getText());
                incrementAmt = CommonUtil.convertObjToDouble(txtIncrementAmount.getText());
                incrementCount = CommonUtil.convertObjToInt(txtNoOfIncrements.getText());
                endAmt = startAmt + (incrementAmt * incrementCount);
            } else {
                startAmt = CommonUtil.convertObjToDouble(tblSalaryStructure.getValueAt(i - 1, 3));
                incrementAmt = CommonUtil.convertObjToDouble(txtIncrementAmount.getText());
                incrementCount = CommonUtil.convertObjToInt(txtNoOfIncrements.getText());
                endAmt = startAmt + (incrementAmt * incrementCount);
            }
        } else {
            if (i == 0 || selectedindex == 0) {
                startAmt = CommonUtil.convertObjToDouble(txtStartingAmount.getText());
                incrementAmt = CommonUtil.convertObjToDouble(txtIncrementAmount.getText());
                incrementCount = CommonUtil.convertObjToInt(txtNoOfIncrements.getText());
                endAmt = startAmt + (incrementAmt * incrementCount);
            } else {
                startAmt = CommonUtil.convertObjToDouble(tblSalaryStructure.getValueAt(i - 1, 3));
                incrementAmt = CommonUtil.convertObjToDouble(txtIncrementAmount.getText());
                incrementCount = CommonUtil.convertObjToInt(txtNoOfIncrements.getText());
                endAmt = startAmt + (incrementAmt * incrementCount);
            }
        }
        observable.setTxtStartingAmount(CommonUtil.convertObjToStr(startAmt));
        observable.setTxtEndingAmount(Math.round(endAmt));
        observable.setTxtIncrementAmount(incrementAmt);
        observable.setTxtNoOfIncrements(CommonUtil.convertObjToStr(incrementCount));
        observable.setCboIncrementFrequency(CommonUtil.convertObjToStr(cboIncrementFrequency.getSelectedItem()));
    }

    private void setButtonEnableDisable() {
        btnNew.setEnabled(!btnNew.isEnabled());
        mitNew.setEnabled(btnNew.isEnabled());
        btnSave.setEnabled(!btnNew.isEnabled());
        btnCancel.setEnabled(!btnNew.isEnabled());
        mitSave.setEnabled(btnSave.isEnabled());
        mitCancel.setEnabled(btnCancel.isEnabled());
        btnView.setEnabled(!btnView.isEnabled());
    }

    private void initComponentData() {
        cboDesignation.setModel(observable.getCbmDesignation());
    }

    private void setMaxLength() {
        txtEndingAmount.setValidation(new CurrencyValidation(14, 2));
        txtStartingAmount.setValidation(new CurrencyValidation(14, 2));
        txtIncrementAmount.setValidation(new CurrencyValidation(14, 2));
        txtNoOfIncrements.setValidation(new NumericValidation(3, 3));
        txtStagIncrAmount.setValidation(new CurrencyValidation(14, 2));
        txtStagNoOfIncrements.setValidation(new NumericValidation(3, 3));
    }

    public void enableForm() {
        cboDesignation.setEnabled(true);
        txtScaleId.setEnabled(true);
        txtVersionNo.setEnabled(true);
        tdtFromDate.setEnabled(true);
        tdtToDate.setEnabled(true);
        txtStartingAmount.setEnabled(true);
        txtEndingAmount.setEnabled(true);
        panStagnationIncrement.setEnabled(true);
        rdoStagnationIncrement_Yes.setEnabled(true);
        rdoStagnationIncrement_Yes.setSelected(false);
        rdoStagnationIncrement_No.setEnabled(true);
        rdoStagnationIncrement_No.setSelected(true);
    }

    public void enableIncrementTxt() {
        txtIncrementAmount.setEnabled(true);
        txtNoOfIncrements.setEnabled(true);
        cboIncrementFrequency.setEnabled(true);
    }

    public void disableIncrementTxt() {
        txtIncrementAmount.setEnabled(false);
        txtNoOfIncrements.setEnabled(false);
        cboIncrementFrequency.setEnabled(false);
    }

    public void disableForm() {
        cboDesignation.setEnabled(false);
        txtScaleId.setEnabled(false);
        txtVersionNo.setEnabled(false);
        tdtFromDate.setEnabled(false);
        tdtToDate.setEnabled(false);
        txtStartingAmount.setEnabled(false);
        txtEndingAmount.setEnabled(false);
        panStagnationIncrement.setEnabled(false);
        rdoStagnationIncrement_Yes.setEnabled(false);
        rdoStagnationIncrement_No.setEnabled(false);
    }

    public void enableStagIncr() {
        txtStagIncrAmount.setEnabled(true);
        txtStagNoOfIncrements.setEnabled(true);
        cboIncrementPeriod.setEnabled(true);
    }

    public void disableStagIncr() {
        txtStagIncrAmount.setEnabled(false);
        txtStagNoOfIncrements.setEnabled(false);
        cboIncrementPeriod.setEnabled(false);
    }

    private void addRadioButton() {
        rdgStagnationIncrement.add(rdoStagnationIncrement_Yes);
        rdgStagnationIncrement.add(rdoStagnationIncrement_No);
    }

    private void removeRadioButton() {
        rdgStagnationIncrement.remove(rdoStagnationIncrement_No);
        rdgStagnationIncrement.remove(rdoStagnationIncrement_No);
    }

    /**
     * This method is called from within the constructor to initialize the form.
     *
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {
        java.awt.GridBagConstraints gridBagConstraints;

        rdgStagnationIncrement = new com.see.truetransact.uicomponent.CButtonGroup();
        tbrSalaryStructure = new javax.swing.JToolBar();
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
        panStatus = new com.see.truetransact.uicomponent.CPanel();
        lblSpace1 = new com.see.truetransact.uicomponent.CLabel();
        lblStatus = new com.see.truetransact.uicomponent.CLabel();
        lblMsg = new com.see.truetransact.uicomponent.CLabel();
        panSalaryStructure = new com.see.truetransact.uicomponent.CPanel();
        panSalaryDetails = new com.see.truetransact.uicomponent.CPanel();
        panSalaryStructureInfo = new com.see.truetransact.uicomponent.CPanel();
        lblDesignation = new com.see.truetransact.uicomponent.CLabel();
        cboDesignation = new com.see.truetransact.uicomponent.CComboBox();
        lblFromDate = new com.see.truetransact.uicomponent.CLabel();
        tdtFromDate = new com.see.truetransact.uicomponent.CDateField();
        lblToDate = new com.see.truetransact.uicomponent.CLabel();
        tdtToDate = new com.see.truetransact.uicomponent.CDateField();
        lblStartingAmount = new com.see.truetransact.uicomponent.CLabel();
        txtStartingAmount = new com.see.truetransact.uicomponent.CTextField();
        lblEndingAmount = new com.see.truetransact.uicomponent.CLabel();
        txtEndingAmount = new com.see.truetransact.uicomponent.CTextField();
        lblIncrementAmount = new com.see.truetransact.uicomponent.CLabel();
        txtIncrementAmount = new com.see.truetransact.uicomponent.CTextField();
        lblNoOfIncrements = new com.see.truetransact.uicomponent.CLabel();
        txtNoOfIncrements = new com.see.truetransact.uicomponent.CTextField();
        lblStagnationIncrement = new com.see.truetransact.uicomponent.CLabel();
        lblStagIncrAmount = new com.see.truetransact.uicomponent.CLabel();
        txtStagIncrAmount = new com.see.truetransact.uicomponent.CTextField();
        lblStagNoOfIncrements = new com.see.truetransact.uicomponent.CLabel();
        txtStagNoOfIncrements = new com.see.truetransact.uicomponent.CTextField();
        lblIncrementPeriod = new com.see.truetransact.uicomponent.CLabel();
        panSalaryStructureButtons = new com.see.truetransact.uicomponent.CPanel();
        btnSalaryStructureNew = new com.see.truetransact.uicomponent.CButton();
        btnSalaryStructureSave = new com.see.truetransact.uicomponent.CButton();
        btnSalaryStructureDelete = new com.see.truetransact.uicomponent.CButton();
        lblSalaryStructureStagnationIncrement = new com.see.truetransact.uicomponent.CLabel();
        panIncrementFrequency = new com.see.truetransact.uicomponent.CPanel();
        cboIncrementPeriod = new com.see.truetransact.uicomponent.CComboBox();
        lblScaleId = new com.see.truetransact.uicomponent.CLabel();
        lblIncrementFrequency = new com.see.truetransact.uicomponent.CLabel();
        lblVersionNo = new com.see.truetransact.uicomponent.CLabel();
        cboIncrementFrequency = new com.see.truetransact.uicomponent.CComboBox();
        panStagnationIncrement = new com.see.truetransact.uicomponent.CPanel();
        rdoStagnationIncrement_Yes = new com.see.truetransact.uicomponent.CRadioButton();
        rdoStagnationIncrement_No = new com.see.truetransact.uicomponent.CRadioButton();
        txtScaleId = new com.see.truetransact.uicomponent.CLabel();
        txtVersionNo = new com.see.truetransact.uicomponent.CLabel();
        panSalaryStructureTable = new com.see.truetransact.uicomponent.CPanel();
        srpSalaryStructure = new com.see.truetransact.uicomponent.CScrollPane();
        tblSalaryStructure = new com.see.truetransact.uicomponent.CTable();
        mbrMain = new com.see.truetransact.uicomponent.CMenuBar();
        mnuProcess = new javax.swing.JMenu();
        mitNew = new javax.swing.JMenuItem();
        mitEdit = new javax.swing.JMenuItem();
        mitDelete = new javax.swing.JMenuItem();
        sptNew = new javax.swing.JSeparator();
        mitSave = new javax.swing.JMenuItem();
        mitCancel = new javax.swing.JMenuItem();
        sptSave = new javax.swing.JSeparator();
        mitPrint = new javax.swing.JMenuItem();
        mitClose = new javax.swing.JMenuItem();

        setClosable(true);
        setIconifiable(true);
        setMaximizable(true);
        setResizable(true);
        setMinimumSize(new java.awt.Dimension(850, 650));
        setPreferredSize(new java.awt.Dimension(850, 650));

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
        tbrSalaryStructure.add(btnView);

        lblSpace5.setText("     ");
        tbrSalaryStructure.add(lblSpace5);

        btnNew.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_NEW.gif"))); // NOI18N
        btnNew.setToolTipText("New");
        btnNew.setEnabled(false);
        btnNew.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnNewActionPerformed(evt);
            }
        });
        tbrSalaryStructure.add(btnNew);

        lblSpace17.setForeground(new java.awt.Color(240, 240, 240));
        lblSpace17.setText("     ");
        lblSpace17.setMaximumSize(new java.awt.Dimension(3, 18));
        lblSpace17.setMinimumSize(new java.awt.Dimension(3, 18));
        lblSpace17.setPreferredSize(new java.awt.Dimension(3, 18));
        tbrSalaryStructure.add(lblSpace17);

        btnEdit.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_EDIT.gif"))); // NOI18N
        btnEdit.setToolTipText("Edit");
        btnEdit.setEnabled(false);
        tbrSalaryStructure.add(btnEdit);

        lblSpace18.setForeground(new java.awt.Color(240, 240, 240));
        lblSpace18.setText("     ");
        lblSpace18.setMaximumSize(new java.awt.Dimension(3, 18));
        lblSpace18.setMinimumSize(new java.awt.Dimension(3, 18));
        lblSpace18.setPreferredSize(new java.awt.Dimension(3, 18));
        tbrSalaryStructure.add(lblSpace18);

        btnDelete.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_DELETE.gif"))); // NOI18N
        btnDelete.setToolTipText("Delete");
        btnDelete.setEnabled(false);
        btnDelete.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnDeleteActionPerformed(evt);
            }
        });
        tbrSalaryStructure.add(btnDelete);

        lblSpace2.setText("     ");
        tbrSalaryStructure.add(lblSpace2);

        btnSave.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_SAVE.gif"))); // NOI18N
        btnSave.setToolTipText("Save");
        btnSave.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSaveActionPerformed(evt);
            }
        });
        tbrSalaryStructure.add(btnSave);

        lblSpace19.setForeground(new java.awt.Color(240, 240, 240));
        lblSpace19.setText("     ");
        lblSpace19.setMaximumSize(new java.awt.Dimension(3, 18));
        lblSpace19.setMinimumSize(new java.awt.Dimension(3, 18));
        lblSpace19.setPreferredSize(new java.awt.Dimension(3, 18));
        tbrSalaryStructure.add(lblSpace19);

        btnCancel.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_CANCEL.gif"))); // NOI18N
        btnCancel.setToolTipText("Cancel");
        btnCancel.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCancelActionPerformed(evt);
            }
        });
        tbrSalaryStructure.add(btnCancel);

        lblSpace3.setText("     ");
        tbrSalaryStructure.add(lblSpace3);

        btnAuthorize.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_AUTHORIZE.gif"))); // NOI18N
        btnAuthorize.setToolTipText("Authorize");
        tbrSalaryStructure.add(btnAuthorize);

        lblSpace20.setForeground(new java.awt.Color(240, 240, 240));
        lblSpace20.setText("     ");
        lblSpace20.setMaximumSize(new java.awt.Dimension(3, 18));
        lblSpace20.setMinimumSize(new java.awt.Dimension(3, 18));
        lblSpace20.setPreferredSize(new java.awt.Dimension(3, 18));
        tbrSalaryStructure.add(lblSpace20);

        btnException.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_EXCEPTION.gif"))); // NOI18N
        btnException.setToolTipText("Exception");
        tbrSalaryStructure.add(btnException);

        lblSpace21.setForeground(new java.awt.Color(240, 240, 240));
        lblSpace21.setText("     ");
        lblSpace21.setMaximumSize(new java.awt.Dimension(3, 18));
        lblSpace21.setMinimumSize(new java.awt.Dimension(3, 18));
        lblSpace21.setPreferredSize(new java.awt.Dimension(3, 18));
        tbrSalaryStructure.add(lblSpace21);

        btnReject.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_REJECT.gif"))); // NOI18N
        btnReject.setToolTipText("Reject\n");
        tbrSalaryStructure.add(btnReject);

        lblSpace4.setText("     ");
        tbrSalaryStructure.add(lblSpace4);

        btnPrint.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_PRINT.gif"))); // NOI18N
        btnPrint.setToolTipText("Print");
        tbrSalaryStructure.add(btnPrint);

        lblSpace22.setForeground(new java.awt.Color(240, 240, 240));
        lblSpace22.setText("     ");
        lblSpace22.setMaximumSize(new java.awt.Dimension(3, 18));
        lblSpace22.setMinimumSize(new java.awt.Dimension(3, 18));
        lblSpace22.setPreferredSize(new java.awt.Dimension(3, 18));
        tbrSalaryStructure.add(lblSpace22);

        btnClose.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_CLOSE.gif"))); // NOI18N
        btnClose.setToolTipText("Close");
        btnClose.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCloseActionPerformed(evt);
            }
        });
        tbrSalaryStructure.add(btnClose);

        getContentPane().add(tbrSalaryStructure, java.awt.BorderLayout.NORTH);

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

        panSalaryStructure.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        panSalaryStructure.setMinimumSize(new java.awt.Dimension(860, 750));
        panSalaryStructure.setPreferredSize(new java.awt.Dimension(860, 400));
        panSalaryStructure.setLayout(new java.awt.GridBagLayout());

        panSalaryDetails.setMinimumSize(new java.awt.Dimension(850, 700));
        panSalaryDetails.setPreferredSize(new java.awt.Dimension(800, 700));
        panSalaryDetails.setLayout(new java.awt.GridBagLayout());

        panSalaryStructureInfo.setMinimumSize(new java.awt.Dimension(500, 680));
        panSalaryStructureInfo.setPreferredSize(new java.awt.Dimension(1000, 1000));
        panSalaryStructureInfo.setLayout(new java.awt.GridBagLayout());

        lblDesignation.setText("Designation");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHEAST;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 2, 0);
        panSalaryStructureInfo.add(lblDesignation, gridBagConstraints);

        cboDesignation.setMinimumSize(new java.awt.Dimension(100, 21));
        cboDesignation.setPopupWidth(250);
        cboDesignation.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cboDesignationActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 6, 2, 0);
        panSalaryStructureInfo.add(cboDesignation, gridBagConstraints);

        lblFromDate.setText("From Date");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHEAST;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 2, 0);
        panSalaryStructureInfo.add(lblFromDate, gridBagConstraints);

        tdtFromDate.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                tdtFromDateFocusLost(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.gridwidth = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 6, 2, 0);
        panSalaryStructureInfo.add(tdtFromDate, gridBagConstraints);

        lblToDate.setText("To Date");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 5;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHEAST;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 2, 0);
        panSalaryStructureInfo.add(lblToDate, gridBagConstraints);

        tdtToDate.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                tdtToDateFocusLost(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 5;
        gridBagConstraints.gridwidth = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 6, 2, 0);
        panSalaryStructureInfo.add(tdtToDate, gridBagConstraints);

        lblStartingAmount.setText("Scale Starting Basic Amount");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 6;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHEAST;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 2, 0);
        panSalaryStructureInfo.add(lblStartingAmount, gridBagConstraints);

        txtStartingAmount.setMinimumSize(new java.awt.Dimension(100, 21));
        txtStartingAmount.setValidation(new CurrencyValidation(14,2));
        txtStartingAmount.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                txtStartingAmountFocusLost(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 6;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 6, 2, 0);
        panSalaryStructureInfo.add(txtStartingAmount, gridBagConstraints);

        lblEndingAmount.setText("Scale Ending Basic Amount");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 7;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHEAST;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 2, 0);
        panSalaryStructureInfo.add(lblEndingAmount, gridBagConstraints);

        txtEndingAmount.setMinimumSize(new java.awt.Dimension(100, 21));
        txtEndingAmount.setValidation(new CurrencyValidation(14,2));
        txtEndingAmount.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                txtEndingAmountFocusLost(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 7;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 6, 2, 0);
        panSalaryStructureInfo.add(txtEndingAmount, gridBagConstraints);

        lblIncrementAmount.setText("Increment Amount(NI)");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 8;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHEAST;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 2, 0);
        panSalaryStructureInfo.add(lblIncrementAmount, gridBagConstraints);

        txtIncrementAmount.setMinimumSize(new java.awt.Dimension(100, 21));
        txtIncrementAmount.setValidation(new CurrencyValidation(14,2));
        txtIncrementAmount.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                txtIncrementAmountFocusLost(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 8;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 6, 2, 0);
        panSalaryStructureInfo.add(txtIncrementAmount, gridBagConstraints);

        lblNoOfIncrements.setText("No.of Increments");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 9;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHEAST;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 2, 0);
        panSalaryStructureInfo.add(lblNoOfIncrements, gridBagConstraints);

        txtNoOfIncrements.setMinimumSize(new java.awt.Dimension(100, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 9;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 6, 2, 0);
        panSalaryStructureInfo.add(txtNoOfIncrements, gridBagConstraints);

        lblStagnationIncrement.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        lblStagnationIncrement.setText("Stagnation Increment");
        lblStagnationIncrement.setMaximumSize(new java.awt.Dimension(80, 18));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 11;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHEAST;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 2, 0);
        panSalaryStructureInfo.add(lblStagnationIncrement, gridBagConstraints);

        lblStagIncrAmount.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        lblStagIncrAmount.setText("Increment Amount(SI)");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 13;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHEAST;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 2, 0);
        panSalaryStructureInfo.add(lblStagIncrAmount, gridBagConstraints);

        txtStagIncrAmount.setMinimumSize(new java.awt.Dimension(100, 21));
        txtStagIncrAmount.setValidation(new CurrencyValidation(14,2));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 13;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 6, 2, 0);
        panSalaryStructureInfo.add(txtStagIncrAmount, gridBagConstraints);

        lblStagNoOfIncrements.setText("No.of Increments");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 14;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHEAST;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 2, 0);
        panSalaryStructureInfo.add(lblStagNoOfIncrements, gridBagConstraints);

        txtStagNoOfIncrements.setMinimumSize(new java.awt.Dimension(100, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 14;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 6, 2, 0);
        panSalaryStructureInfo.add(txtStagNoOfIncrements, gridBagConstraints);

        lblIncrementPeriod.setText("Increment Period");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 15;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHEAST;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 2, 0);
        panSalaryStructureInfo.add(lblIncrementPeriod, gridBagConstraints);

        panSalaryStructureButtons.setMinimumSize(new java.awt.Dimension(60, 50));
        panSalaryStructureButtons.setPreferredSize(new java.awt.Dimension(250, 80));
        panSalaryStructureButtons.setLayout(new java.awt.GridBagLayout());

        btnSalaryStructureNew.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_NEW.gif"))); // NOI18N
        btnSalaryStructureNew.setMinimumSize(new java.awt.Dimension(30, 25));
        btnSalaryStructureNew.setPreferredSize(new java.awt.Dimension(30, 30));
        btnSalaryStructureNew.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSalaryStructureNewActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.ipady = 5;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(5, 0, 15, 0);
        panSalaryStructureButtons.add(btnSalaryStructureNew, gridBagConstraints);

        btnSalaryStructureSave.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_SAVE.gif"))); // NOI18N
        btnSalaryStructureSave.setDefaultCapable(false);
        btnSalaryStructureSave.setMinimumSize(new java.awt.Dimension(30, 25));
        btnSalaryStructureSave.setPreferredSize(new java.awt.Dimension(30, 30));
        btnSalaryStructureSave.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSalaryStructureSaveActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.ipady = 5;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 15, 0);
        panSalaryStructureButtons.add(btnSalaryStructureSave, gridBagConstraints);

        btnSalaryStructureDelete.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_DELETE.gif"))); // NOI18N
        btnSalaryStructureDelete.setMinimumSize(new java.awt.Dimension(30, 25));
        btnSalaryStructureDelete.setPreferredSize(new java.awt.Dimension(30, 30));
        btnSalaryStructureDelete.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSalaryStructureDeleteActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.ipady = 5;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 15, 0);
        panSalaryStructureButtons.add(btnSalaryStructureDelete, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 16;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.ipadx = 250;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(40, 29, 0, 29);
        panSalaryStructureInfo.add(panSalaryStructureButtons, gridBagConstraints);

        lblSalaryStructureStagnationIncrement.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        lblSalaryStructureStagnationIncrement.setText("Stagnation Increment");
        lblSalaryStructureStagnationIncrement.setFont(new java.awt.Font("MS Sans Serif", 1, 14)); // NOI18N
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 12;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(51, 98, 17, 0);
        panSalaryStructureInfo.add(lblSalaryStructureStagnationIncrement, gridBagConstraints);

        panIncrementFrequency.setMinimumSize(new java.awt.Dimension(100, 25));
        panIncrementFrequency.setPreferredSize(new java.awt.Dimension(100, 25));
        panIncrementFrequency.setLayout(new java.awt.GridBagLayout());

        cboIncrementPeriod.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "             ", "YEARLY", "HALF YEARLY", "QUARTERLY" }));
        cboIncrementPeriod.setMinimumSize(new java.awt.Dimension(100, 21));
        cboIncrementPeriod.setPopupWidth(250);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 13;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(1, 2, 1, 1);
        panIncrementFrequency.add(cboIncrementPeriod, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 15;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 6, 2, 0);
        panSalaryStructureInfo.add(panIncrementFrequency, gridBagConstraints);

        lblScaleId.setText("Scale Id");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHEAST;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 2, 0);
        panSalaryStructureInfo.add(lblScaleId, gridBagConstraints);

        lblIncrementFrequency.setText("Increment Frequency");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 10;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHEAST;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 2, 0);
        panSalaryStructureInfo.add(lblIncrementFrequency, gridBagConstraints);

        lblVersionNo.setText("Version No");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHEAST;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 2, 0);
        panSalaryStructureInfo.add(lblVersionNo, gridBagConstraints);

        cboIncrementFrequency.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "           ", "YEARLY", "HALF-YEARLY", "QUARTERLY" }));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 10;
        gridBagConstraints.ipady = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 6, 2, 0);
        panSalaryStructureInfo.add(cboIncrementFrequency, gridBagConstraints);

        panStagnationIncrement.setLayout(new java.awt.GridBagLayout());

        rdgStagnationIncrement.add(rdoStagnationIncrement_Yes);
        rdoStagnationIncrement_Yes.setText("Yes");
        rdoStagnationIncrement_Yes.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                rdoStagnationIncrement_YesActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.weightx = 1.0;
        panStagnationIncrement.add(rdoStagnationIncrement_Yes, gridBagConstraints);

        rdgStagnationIncrement.add(rdoStagnationIncrement_No);
        rdoStagnationIncrement_No.setText("No");
        rdoStagnationIncrement_No.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                rdoStagnationIncrement_NoActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.weightx = 1.0;
        panStagnationIncrement.add(rdoStagnationIncrement_No, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 11;
        gridBagConstraints.ipadx = 10;
        gridBagConstraints.ipady = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 6, 2, 0);
        panSalaryStructureInfo.add(panStagnationIncrement, gridBagConstraints);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(10, 48, 0, 0);
        panSalaryStructureInfo.add(txtScaleId, gridBagConstraints);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(10, 48, 0, 0);
        panSalaryStructureInfo.add(txtVersionNo, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 68, 0);
        panSalaryDetails.add(panSalaryStructureInfo, gridBagConstraints);

        panSalaryStructureTable.setMinimumSize(new java.awt.Dimension(500, 700));
        panSalaryStructureTable.setPreferredSize(new java.awt.Dimension(400, 500));
        panSalaryStructureTable.setLayout(new java.awt.GridBagLayout());

        srpSalaryStructure.setAutoscrolls(true);
        srpSalaryStructure.setMinimumSize(new java.awt.Dimension(500, 400));
        srpSalaryStructure.setPreferredSize(new java.awt.Dimension(500, 400));

        tblSalaryStructure.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Start Amount", "Increment Amount", "Increment Count", "End Amount"
            }
        ));
        tblSalaryStructure.setMinimumSize(new java.awt.Dimension(350, 1000));
        tblSalaryStructure.setOpaque(false);
        tblSalaryStructure.setPreferredSize(new java.awt.Dimension(270, 1000));
        tblSalaryStructure.setReorderingAllowed(false);
        tblSalaryStructure.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tblSalaryStructureMouseClicked(evt);
            }
        });
        srpSalaryStructure.setViewportView(tblSalaryStructure);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTH;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 0.8;
        panSalaryStructureTable.add(srpSalaryStructure, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        panSalaryDetails.add(panSalaryStructureTable, gridBagConstraints);

        panSalaryStructure.add(panSalaryDetails, new java.awt.GridBagConstraints());

        getContentPane().add(panSalaryStructure, java.awt.BorderLayout.CENTER);

        mnuProcess.setText("Process");
        mnuProcess.setName("mnuProcess");

        mitNew.setText("New");
        mitNew.setName("mitNew");
        mitNew.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mitNewActionPerformed(evt);
            }
        });
        mnuProcess.add(mitNew);

        mitEdit.setText("Edit");
        mitEdit.setName("mitEdit");
        mitEdit.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mitEditActionPerformed(evt);
            }
        });
        mnuProcess.add(mitEdit);

        mitDelete.setText("Delete");
        mitDelete.setName("mitDelete");
        mitDelete.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mitDeleteActionPerformed(evt);
            }
        });
        mnuProcess.add(mitDelete);

        sptNew.setName("sptNew");
        mnuProcess.add(sptNew);

        mitSave.setText("Save");
        mitSave.setName("mitSave");
        mitSave.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mitSaveActionPerformed(evt);
            }
        });
        mnuProcess.add(mitSave);

        mitCancel.setText("Cancel");
        mitCancel.setName("mitCancel");
        mitCancel.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mitCancelActionPerformed(evt);
            }
        });
        mnuProcess.add(mitCancel);

        sptSave.setName("sptSave");
        mnuProcess.add(sptSave);

        mitPrint.setText("Print");
        mitPrint.setName("mitPrint");
        mitPrint.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mitPrintActionPerformed(evt);
            }
        });
        mnuProcess.add(mitPrint);

        mitClose.setText("Close");
        mitClose.setName("mitClose");
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

    private void enableDisableAllscreens(boolean allScreen) {
        btnSalaryStructureNew.setEnabled(allScreen);
        btnSalaryStructureSave.setEnabled(allScreen);
        btnSalaryStructureDelete.setEnabled(allScreen);
    }

    private void btnViewActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnViewActionPerformed
        // TODO add your handling code here:
        observable.setActionType(ClientConstants.ACTIONTYPE_VIEW);
        observable.setStatus();
        lblStatus.setText(observable.getLblStatus());
        callView(ClientConstants.ACTIONTYPE_VIEW);
        btnCheck();
        btnSalaryStructureNew.setEnabled(false);
        btnSalaryStructureSave.setEnabled(false);
        btnSalaryStructureDelete.setEnabled(false);
        disableForm();
        disableIncrementTxt();
        disableStagIncr();
    }//GEN-LAST:event_btnViewActionPerformed
    private void btnCheck() {
        btnCancel.setEnabled(true);
        btnSave.setEnabled(false);
        btnNew.setEnabled(false);
        btnDelete.setEnabled(false);
    }

//    private String checkMandatory(JComponent component){
//        return new MandatoryCheck().checkMandatory(getClass().getName(),component);
//    }
    private void displayAlert(String message) {
        CMandatoryDialog cmd = new CMandatoryDialog();
        cmd.setMessage(message);
        cmd.setModal(true);
        cmd.show();
    }

    private void btnCloseActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCloseActionPerformed
        // Add your handling code here:
        observable.resetOBFields();
        cifClosingAlert();
    }//GEN-LAST:event_btnCloseActionPerformed

    private void updateTable() {
        this.tblSalaryStructure.setModel(observable.getTbmSalaryStructure());
        this.tblSalaryStructure.revalidate();
    }

    private void btnSaveActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSaveActionPerformed
        // Add your handling code here:
        updateOBFields();
        setModified(false);
        Double txtEndAmt = 0.00;
        Double endAmt = 0.00;
        int i = tblSalaryStructure.getRowCount();
        endAmt = CommonUtil.convertObjToDouble(tblSalaryStructure.getValueAt(i - 1, 3));
        txtEndAmt = CommonUtil.convertObjToDouble(txtEndingAmount.getText());
        if (i > 0) {
            if (CommonUtil.convertObjToStr(txtEndAmt).equals(CommonUtil.convertObjToStr(endAmt))) {
                observable.doAction();
                btnSave.setEnabled(false);
                btnNew.setEnabled(true);
                clearTextFields();
                disableForm();
                setModified(false);
                tblSalaryStructure.revalidate();
                observable.resetForm();
                observable.setTable();
                tblSalaryStructure.setModel(observable.getTbmSalaryStructure());
                lblStatus.setText("Success");
            } else {
                ClientUtil.showMessageWindow("Scale Ending Amount and Final Increment Amount are not equal!!!");
                lblStatus.setText("Failed");
            }
        } else {

            ClientUtil.showAlertWindow("There are no records to save");
            lblStatus.setText("Failed");
        }

    }//GEN-LAST:event_btnSaveActionPerformed

    private void btnCancelActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCancelActionPerformed
        // Add your handling code here:
        clearTextFields();
        disableForm();
        disableStagIncr();
        observable.resetForm();
        observable.setTable();
        tblSalaryStructure.setModel(observable.getTbmSalaryStructure());
        setButtonEnableDisable();
        btnDelete.setEnabled(true);
        btnSave.setEnabled(false);
        btnView.setEnabled(true);
        btnNew.setEnabled(true);
        setModified(false);
    }//GEN-LAST:event_btnCancelActionPerformed

    private void btnNewActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnNewActionPerformed
        // Add your handling code here:
        this.selectedSingleRow = true;
        observable.setActionType(ClientConstants.ACTIONTYPE_NEW);
        observable.setStatus();
        lblStatus.setText(observable.getLblStatus());
        setUp(ClientConstants.ACTIONTYPE_NEW, true);
        enableForm();
        disableIncrementTxt();
        disableStagIncr();
        clearTextFields();
        panSalaryStructureButtons.setEnabled(true);
        btnDelete.setEnabled(false);
        btnCancel.setEnabled(true);
        btnClose.setEnabled(true);
        observable.setTable();
        tblSalaryStructure.setModel(observable.getTbmSalaryStructure());
        btnSalaryStructureNew.setEnabled(true);
        btnSalaryStructureSave.setEnabled(false);
        btnSalaryStructureDelete.setEnabled(false);

        //__ To Save the data in the Internal Frame...
        setModified(true);
    }//GEN-LAST:event_btnNewActionPerformed

    private void mitCloseActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mitCloseActionPerformed
        // Add your handling code here:
        btnCloseActionPerformed(evt);
    }//GEN-LAST:event_mitCloseActionPerformed

    private void mitPrintActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mitPrintActionPerformed
    }//GEN-LAST:event_mitPrintActionPerformed

    private void mitCancelActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mitCancelActionPerformed
        // Add your handling code here:
        this.btnCancelActionPerformed(evt);
    }//GEN-LAST:event_mitCancelActionPerformed

    private void mitSaveActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mitSaveActionPerformed
        // Add your handling code here:
        this.btnSaveActionPerformed(evt);
    }//GEN-LAST:event_mitSaveActionPerformed

    private void mitDeleteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mitDeleteActionPerformed
        // Add your handling code here:
        this.btnDeleteActionPerformed(evt);
    }//GEN-LAST:event_mitDeleteActionPerformed

    private void mitEditActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mitEditActionPerformed
        // Add your handling code here:
    }//GEN-LAST:event_mitEditActionPerformed

    private void mitNewActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mitNewActionPerformed
        // Add your handling code here:
        this.btnNewActionPerformed(evt);
    }//GEN-LAST:event_mitNewActionPerformed

    private void cboDesignationActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cboDesignationActionPerformed
        // TODO add your handling code here:
        if (observable.getActionType() == ClientConstants.ACTIONTYPE_NEW) {
            if ((cboDesignation.getSelectedIndex() > 0)) {
                String DESG = CommonUtil.convertObjToStr(((ComboBoxModel) (cboDesignation.getModel())).getSelectedItem());
                HashMap dataMap = new HashMap();
                dataMap.put("DESG", DESG);
                List scaleNo = observable.getScaleId(dataMap);
                if (scaleNo != null && scaleNo.size() > 0) {
                    HashMap map = new HashMap();
                    map = (HashMap) scaleNo.get(0);
                    if (map != null && map.size() > 0) {
                        int scId = CommonUtil.convertObjToInt(map.get("SCALE_ID"));
                        if (scId != -1) {
                            txtScaleId.setText(CommonUtil.convertObjToStr(map.get("SCALE_ID")));
                        }
                    }
                } else {
                    txtScaleId.setText("");
                }
                String versionNo = observable.getVersionNo(dataMap);
                if (versionNo != null) {
                    txtVersionNo.setText(CommonUtil.convertObjToStr(versionNo));
                }
            }
        }

    }//GEN-LAST:event_cboDesignationActionPerformed

    private void txtStartingAmountFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtStartingAmountFocusLost
        // TODO add your handling code here:
    }//GEN-LAST:event_txtStartingAmountFocusLost

    private void txtEndingAmountFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtEndingAmountFocusLost
        // TODO add your handling code here:
        if (CommonUtil.convertObjToDouble(txtStartingAmount.getText()) > CommonUtil.convertObjToDouble(txtEndingAmount.getText())) {
            ClientUtil.showMessageWindow("Starting Amount cannot be greater than Ending Amount!!!");
            txtEndingAmount.setText("");
        }
    }//GEN-LAST:event_txtEndingAmountFocusLost

    private void btnSalaryStructureNewActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSalaryStructureNewActionPerformed
        disableForm();
        enableIncrementTxt();
        disableStagIncr();
        rdoStagnationIncrement_No.setSelected(true);
        setButtonEnableDisable();
        this.selectedSingleRow = true;
        btnSalaryStructureSave.setEnabled(true);
        btnSalaryStructureDelete.setEnabled(false);
    }//GEN-LAST:event_btnSalaryStructureNewActionPerformed

    private void btnSalaryStructureSaveActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSalaryStructureSaveActionPerformed
        if (txtStartingAmount.getText().equals("") || txtEndingAmount.getText().equals("") || txtIncrementAmount.getText().equals("") || txtNoOfIncrements.getText().equals("")) {
            ClientUtil.showMessageWindow("Please enter all the necessary details!!!");
        } else {
            disableIncrementTxt();
            if (!this.selectedSingleRow) {
                int rowSelected = this.tblSalaryStructure.getSelectedRow();
                insertTableData("edit");
                observable.insertIntoTableData(rowSelected);
                txtIncrementAmount.setText("");
                txtNoOfIncrements.setText("");
                cboIncrementFrequency.setSelectedIndex(0);
                this.updateTable();
                btnDelete.setEnabled(false);
            } else {
                insertTableData("");
                observable.insertIntoTableData(-1);
                tdtFromDate.setEnabled(false);
                tdtToDate.setEnabled(false);
                txtStartingAmount.setEditable(false);
                txtEndingAmount.setEditable(false);
                txtIncrementAmount.setText("");
                txtNoOfIncrements.setText("");
                cboIncrementFrequency.setSelectedIndex(0);
                btnSave.setEnabled(true);
                btnCancel.setEnabled(true);
                btnNew.setEnabled(false);
                this.updateTable();
                btnDelete.setEnabled(false);
            }
        }
    }//GEN-LAST:event_btnSalaryStructureSaveActionPerformed

    private void btnSalaryStructureDeleteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSalaryStructureDeleteActionPerformed
        // TODO add your handling code here:
        disableIncrementTxt();
        int rowSelected = this.tblSalaryStructure.getSelectedRow();
        if (rowSelected >= 0) {
            observable.deleteDailyData(rowSelected);
            txtIncrementAmount.setText("");
            txtNoOfIncrements.setText("");
            cboIncrementFrequency.setSelectedIndex(0);
            this.updateTable();
        } else {
            ClientUtil.showMessageWindow("Please select a row to delete!!!");
        }
    }//GEN-LAST:event_btnSalaryStructureDeleteActionPerformed

    private void rdoStagnationIncrement_YesActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_rdoStagnationIncrement_YesActionPerformed
        // TODO add your handling code here:
        enableStagIncr();
    }//GEN-LAST:event_rdoStagnationIncrement_YesActionPerformed

    private void rdoStagnationIncrement_NoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_rdoStagnationIncrement_NoActionPerformed
        // TODO add your handling code here:
        disableStagIncr();
    }//GEN-LAST:event_rdoStagnationIncrement_NoActionPerformed

    private void txtIncrementAmountFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtIncrementAmountFocusLost
        // TODO add your handling code here:
        if (CommonUtil.convertObjToDouble(txtIncrementAmount.getText()) > CommonUtil.convertObjToDouble(txtEndingAmount.getText())) {
            ClientUtil.showMessageWindow("Increment Amount cannot be greater than Scale Ending Amount");
            txtIncrementAmount.setText("");
        }
    }//GEN-LAST:event_txtIncrementAmountFocusLost

    private void tblSalaryStructureMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tblSalaryStructureMouseClicked
        // TODO add your handling code here:
        enableIncrementTxt();
        this.selectedSingleRow = true;
        int rowCount = 0;
        int selRow = 0;
        rowCount = tblSalaryStructure.getRowCount();
        selRow = tblSalaryStructure.getSelectedRow();
        if (rowCount >= 0) {
            updateTableData();
            this.selectedSingleRow = false;
            btnSalaryStructureSave.setEnabled(true);
            btnSalaryStructureDelete.setEnabled(true);
            if (observable.getActionType() == ClientConstants.ACTIONTYPE_VIEW || observable.getActionType() == ClientConstants.ACTIONTYPE_DELETE) {
                disableIncrementTxt();
                btnSalaryStructureNew.setEnabled(false);
                btnSalaryStructureSave.setEnabled(false);
                btnSalaryStructureDelete.setEnabled(false);
            }
        } else {
            ClientUtil.showMessageWindow("There are no records to display!!!");
        }

    }//GEN-LAST:event_tblSalaryStructureMouseClicked

    private void btnDeleteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnDeleteActionPerformed
        // TODO add your handling code here:
        if (viewType == ClientConstants.ACTIONTYPE_DELETE && isFilled == false) {
            view = "DELETE";
            isFilled = true;
            observable.doAction();
            lblStatus.setText("Deleted");
            clearTextFields();
            disableForm();
            disableStagIncr();
            tblSalaryStructure.revalidate();
            observable.resetForm();
            observable.setTable();
            tblSalaryStructure.setModel(observable.getTbmSalaryStructure());
        } else {
            observable.setActionType(ClientConstants.ACTIONTYPE_DELETE);
            callView(ClientConstants.ACTIONTYPE_DELETE);
            isFilled = false;
            btnDelete.setEnabled(true);
            btnSalaryStructureNew.setEnabled(false);
            btnSalaryStructureSave.setEnabled(false);
            lblStatus.setText("Delete");
        }
    }//GEN-LAST:event_btnDeleteActionPerformed

    private void tdtFromDateFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_tdtFromDateFocusLost
        // TODO add your handling code here:
    }//GEN-LAST:event_tdtFromDateFocusLost

    private void tdtToDateFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_tdtToDateFocusLost
        // TODO add your handling code here:
        ClientUtil.validateToDate(tdtToDate, tdtFromDate.getDateValue());
    }//GEN-LAST:event_tdtToDateFocusLost
    private void callView(int viewType) {
        HashMap viewMap = new HashMap();
        this.viewType = viewType;
        if (viewType == ClientConstants.ACTIONTYPE_DELETE || viewType == ClientConstants.ACTIONTYPE_VIEW) {
            viewMap.put(CommonConstants.MAP_NAME, "getScaleDetails");
            isFilled = false;
        }
        new ViewAll(this, viewMap).show();
    }

    public void fillData(Object obj) {
        final HashMap hash = (HashMap) obj;
        if (viewType == ClientConstants.ACTIONTYPE_DELETE || viewType == ClientConstants.ACTIONTYPE_VIEW) {
            setModified(true);
            observable.populateData(hash);
            observable.ttNotifyObservers();
        }
    }

    /*
     * Auto Generated Method - setMandatoryHashMap() This method list out all
     * the Input Fields available in the UI. It needs a class level HashMap
     * variable mandatoryMap.
     */
    public void setMandatoryHashMap() {
        mandatoryMap = new HashMap();
        mandatoryMap.put("cboDesignation", new Boolean(false));
        mandatoryMap.put("tdtFromDate", new Boolean(false));
        mandatoryMap.put("tdtToDate", new Boolean(false));
        mandatoryMap.put("txtStartingAmount", new Boolean(false));
        mandatoryMap.put("txtEndingAmount", new Boolean(false));
    }
    /*
     * Auto Generated Method - getMandatoryHashMap() Getter method for
     * setMandatoryHashMap().
     */

    public HashMap getMandatoryHashMap() {
        return mandatoryMap;
    }

    public void update(Observable o, Object arg) {
        txtScaleId.setText(observable.getTxtScaleId());
        txtVersionNo.setText(observable.getTxtVersionNo());
        txtStartingAmount.setText(observable.getTxtStartingAmount());
        txtEndingAmount.setText(CommonUtil.convertObjToStr(observable.getTxtEndingAmount()));
        txtIncrementAmount.setText(CommonUtil.convertObjToStr(observable.getTxtIncrementAmount()));
        txtNoOfIncrements.setText(observable.getTxtNoOfIncrements());
        txtStagIncrAmount.setText(observable.getTxtStagIncrAmount());
        txtStagNoOfIncrements.setText(observable.getTxtStagNoOfIncrements());
        cboDesignation.setSelectedItem(observable.getCboDesignation());
        cboIncrementFrequency.setSelectedItem(observable.getCboIncrementFrequency());
        cboIncrementPeriod.setSelectedItem(observable.getCboIncrementPeriod());
        tdtFromDate.setDateValue(observable.getTdtFromDate());
        tdtToDate.setDateValue(observable.getTdtToDate());
        if (observable.isRdoStagnationIncrement_No() == true) {
            rdoStagnationIncrement_No.setSelected(true);
            rdoStagnationIncrement_Yes.setSelected(false);
        } else if (observable.isRdoStagnationIncrement_Yes() == true) {
            rdoStagnationIncrement_No.setSelected(false);
            rdoStagnationIncrement_Yes.setSelected(true);
        }
        this.updateTable();
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
    private com.see.truetransact.uicomponent.CButton btnSalaryStructureDelete;
    private com.see.truetransact.uicomponent.CButton btnSalaryStructureNew;
    private com.see.truetransact.uicomponent.CButton btnSalaryStructureSave;
    private com.see.truetransact.uicomponent.CButton btnSave;
    private com.see.truetransact.uicomponent.CButton btnView;
    private com.see.truetransact.uicomponent.CComboBox cboDesignation;
    private com.see.truetransact.uicomponent.CComboBox cboIncrementFrequency;
    private com.see.truetransact.uicomponent.CComboBox cboIncrementPeriod;
    private com.see.truetransact.uicomponent.CLabel lblDesignation;
    private com.see.truetransact.uicomponent.CLabel lblEndingAmount;
    private com.see.truetransact.uicomponent.CLabel lblFromDate;
    private com.see.truetransact.uicomponent.CLabel lblIncrementAmount;
    private com.see.truetransact.uicomponent.CLabel lblIncrementFrequency;
    private com.see.truetransact.uicomponent.CLabel lblIncrementPeriod;
    private com.see.truetransact.uicomponent.CLabel lblMsg;
    private com.see.truetransact.uicomponent.CLabel lblNoOfIncrements;
    private com.see.truetransact.uicomponent.CLabel lblSalaryStructureStagnationIncrement;
    private com.see.truetransact.uicomponent.CLabel lblScaleId;
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
    private com.see.truetransact.uicomponent.CLabel lblStagIncrAmount;
    private com.see.truetransact.uicomponent.CLabel lblStagNoOfIncrements;
    private com.see.truetransact.uicomponent.CLabel lblStagnationIncrement;
    private com.see.truetransact.uicomponent.CLabel lblStartingAmount;
    private com.see.truetransact.uicomponent.CLabel lblStatus;
    private com.see.truetransact.uicomponent.CLabel lblToDate;
    private com.see.truetransact.uicomponent.CLabel lblVersionNo;
    private com.see.truetransact.uicomponent.CMenuBar mbrMain;
    private javax.swing.JMenuItem mitCancel;
    private javax.swing.JMenuItem mitClose;
    private javax.swing.JMenuItem mitDelete;
    private javax.swing.JMenuItem mitEdit;
    private javax.swing.JMenuItem mitNew;
    private javax.swing.JMenuItem mitPrint;
    private javax.swing.JMenuItem mitSave;
    private javax.swing.JMenu mnuProcess;
    private com.see.truetransact.uicomponent.CPanel panIncrementFrequency;
    private com.see.truetransact.uicomponent.CPanel panSalaryDetails;
    private com.see.truetransact.uicomponent.CPanel panSalaryStructure;
    private com.see.truetransact.uicomponent.CPanel panSalaryStructureButtons;
    private com.see.truetransact.uicomponent.CPanel panSalaryStructureInfo;
    private com.see.truetransact.uicomponent.CPanel panSalaryStructureTable;
    private com.see.truetransact.uicomponent.CPanel panStagnationIncrement;
    private com.see.truetransact.uicomponent.CPanel panStatus;
    private com.see.truetransact.uicomponent.CButtonGroup rdgStagnationIncrement;
    private com.see.truetransact.uicomponent.CRadioButton rdoStagnationIncrement_No;
    private com.see.truetransact.uicomponent.CRadioButton rdoStagnationIncrement_Yes;
    private javax.swing.JSeparator sptNew;
    private javax.swing.JSeparator sptSave;
    private com.see.truetransact.uicomponent.CScrollPane srpSalaryStructure;
    private com.see.truetransact.uicomponent.CTable tblSalaryStructure;
    private javax.swing.JToolBar tbrSalaryStructure;
    private com.see.truetransact.uicomponent.CDateField tdtFromDate;
    private com.see.truetransact.uicomponent.CDateField tdtToDate;
    private com.see.truetransact.uicomponent.CTextField txtEndingAmount;
    private com.see.truetransact.uicomponent.CTextField txtIncrementAmount;
    private com.see.truetransact.uicomponent.CTextField txtNoOfIncrements;
    private com.see.truetransact.uicomponent.CLabel txtScaleId;
    private com.see.truetransact.uicomponent.CTextField txtStagIncrAmount;
    private com.see.truetransact.uicomponent.CTextField txtStagNoOfIncrements;
    private com.see.truetransact.uicomponent.CTextField txtStartingAmount;
    private com.see.truetransact.uicomponent.CLabel txtVersionNo;
    // End of variables declaration//GEN-END:variables

    public static void main(String[] args) {
        //        PayrollSalaryStructureUI lui = new PayrollSalaryStructureUI();
        JFrame j = new JFrame();
        //        j.getContentPane().add(lui);
        j.setSize(800, 1200);
        j.show();
        //        lui.show();
    }

    private void clearTextFields() {
        cboDesignation.setSelectedIndex(0);
        txtScaleId.setText("");
        txtVersionNo.setText("");
        tdtFromDate.setDateValue(null);
        tdtToDate.setDateValue(null);
        txtStartingAmount.setText("");
        txtEndingAmount.setText("");
        txtIncrementAmount.setText("");
        txtNoOfIncrements.setText("");
        cboIncrementFrequency.setSelectedIndex(0);
        rdoStagnationIncrement_Yes.setSelected(false);
        rdoStagnationIncrement_No.setSelected(false);
        txtStagIncrAmount.setText("");
        txtStagNoOfIncrements.setText("");
        cboIncrementPeriod.setSelectedIndex(0);
    }

    private void updateTableData() {
        this.selectedSingleRow = true;
        observable.populateTableData(tblSalaryStructure.getSelectedRow());
        txtIncrementAmount.setText(CommonUtil.convertObjToStr(observable.getTxtIncrementAmount()));
        txtNoOfIncrements.setText(CommonUtil.convertObjToStr(observable.getTxtNoOfIncrements()));
        cboIncrementFrequency.setSelectedItem(CommonUtil.convertObjToStr(observable.getCboIncrementFrequency()));
    }
}
