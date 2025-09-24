/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition. 
 * 
 * IncrementUI.java
 *
 * Created on Fri Nov 14 10:00:00 IST 2014
 */
package com.see.truetransact.ui.payroll.increment;

import com.see.truetransact.uicomponent.CInternalFrame;
import com.see.truetransact.uimandatory.UIMandatoryField;
import com.see.truetransact.uimandatory.MandatoryCheck;
import com.see.truetransact.commonutil.CommonConstants;
import com.see.truetransact.commonutil.CommonUtil;
import com.see.truetransact.clientutil.ClientUtil;
import com.see.truetransact.clientutil.ClientConstants;
import com.see.truetransact.clientutil.CMandatoryDialog;
import com.see.truetransact.ui.common.viewall.ViewAll;
import com.see.truetransact.clientproxy.ProxyParameters;
import com.see.truetransact.commonutil.DateUtil;
import com.see.truetransact.transferobject.payroll.employee.SalStructDetailsTO;
import com.see.truetransact.uicomponent.CButtonGroup;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.ArrayList;
import java.util.Date;
import java.util.Observable;
import java.util.List;
import java.util.logging.Level;

/**
 *
 * @author  shihad
 */
public class IncrementUI extends CInternalFrame implements java.util.Observer, UIMandatoryField {

    HashMap mandatoryMap;
    IncrementOB observable;
    java.util.ResourceBundle resourceBundle = java.util.ResourceBundle.getBundle("com.see.truetransact.ui.payroll.increment.IncrementRB", ProxyParameters.LANGUAGE);
    final int EDIT = 0, DELETE = 1, AUTHORIZE = 2, ACCNO = 4, DEPOSITNO = 5, VIEW = 10, ACCTHDID = 6, EMP_DETAILS = 7;
    int viewType = -1;
    private ArrayList tblHeadList = new ArrayList();
    CButtonGroup radioType = new CButtonGroup();
    IncrementMRB objMandatoryRB = new IncrementMRB();
    private String scaleId = "";
    private Date currDt = null;
    boolean isFilled = false;
    SalStructDetailsTO sal = null;
    ArrayList<SalStructDetailsTO> salArray = new ArrayList<SalStructDetailsTO>();
    /** Creates new form IncrementUI */
    public IncrementUI() {
        initComponents();
        initSetup();
        initComponentData();
    }

    private void initSetup() {
        setFieldNames();
        internationalize();
        setMandatoryHashMap();
        setObservable();
        setMaxLenths();
        new MandatoryCheck().putMandatoryMarks(getClass().getName(), panelEmployee);
        ClientUtil.enableDisable(this, false); //__ Disables all when the screen appears for the 1st time
        setButtonEnableDisable();              //__ Enables/Disables the necessary buttons and menu items...
        observable.resetForm();                //__ Resets the Data in the Form...
        observable.setStatus();
        setMandatoryHashMap();
        setHelpMessage();
        btnViewEmp.setEnabled(false);
        radioType.add(rdoIncrement);
        radioType.add(rdoPromotion);
        currDt = ClientUtil.getCurrentDate();
    }

    private void setObservable() {
        try {
            observable = new IncrementOB();//.getInstance();
            observable.addObserver(this);
        } catch (Exception e) {
            e.printStackTrace();
        }
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
        btnNew.setName("btnNew");
        btnPrint.setName("btnPrint");
        btnReject.setName("btnReject");
        btnSave.setName("btnSave");
        lblMsg.setName("lblMsg");
        txtNewBasicSalary.setName("txtNewBasicSalary");
        txtEmpId.setName("txtEmpId");
        txtPresBasicSalary.setName("txtPresBasicSalary");
        cboDesig.setName("cboDesig");
        txtLastIncrDate.setName("txtLastIncrDate");
        txtEmpName.setName("txtEmpName");
        txtDesig.setName("txtDesig");
        txtNextIncrDate.setName("txtNextIncrDate");
        txtNumberIncr.setName("txtNumberIncr");
        tdtNewIncrDate.setName("tdtNewIncrDate");
        lblSpace1.setName("lblSpace1");
        lblSpace2.setName("lblSpace2");
        lblSpace3.setName("lblSpace3");
        lblSpace4.setName("lblSpace4");
        lblStatus.setName("lblStatus1");
        mbrAgent.setName("mbrAgent");
        panelEmployee.setName("panelEmployee");
        panStatus.setName("panStatus");

    }

    /* Auto Generated Method - internationalize()
    This method used to assign display texts from
    the Resource Bundle File. */
    private void internationalize() {
    }
    /* Auto Generated Method - update()
    This method called by Observable. It updates the UI with
    Observable's data. If needed add/Remove RadioButtons
    method need to be added.*/

    public void update(Observable observed, Object arg) {
    }

    /* Auto Generated Method - updateOBFields()
    This method called by Save option of UI.
    It updates the OB with UI data.*/
    public void updateOBFields() {
        observable.setTxtEmpId(txtEmpId.getText());
        observable.setTxtEmpName(txtEmpName.getText());
        observable.setTxtPresBasicSalary(txtPresBasicSalary.getText());
        observable.setTxtLastIncrDate(txtLastIncrDate.getText());
        observable.setCboDesig(CommonUtil.convertObjToStr(cboDesig.getSelectedItem()));
        observable.setTxtNewBasicSalary(txtNewBasicSalary.getText());
        observable.setTxtDesig(txtDesig.getText());
        observable.setTxtNextIncrDate(txtNextIncrDate.getText());
        observable.setTxtNumberIncr(txtNumberIncr.getText());
        observable.setTdtNewIncrDate(tdtNewIncrDate.getDateValue());
        observable.setScaleId(scaleId);
        observable.setIncrId(lblIncrId.getText());
        observable.setVersionNo(lblVersionNoVal.getText());
        if (rdoIncrement.isSelected()) {
            observable.setRdoIncrement(true);
        }
        if (rdoPromotion.isSelected()) {
                observable.setTxtNewBasicSalary(txtNewBasicSalary.getText());
                observable.setScaleId(lblScaleIdVal.getText());
                observable.setRdoPromotion(true);
            }       
    }

    /* Auto Generated Method - setMandatoryHashMap()
    
    ADD: implements com.see.truetransact.uimandatory.UIMandatoryField
    
    This method list out all the Input Fields available in the UI.
    It needs a class level HashMap variable mandatoryMap. */
    public void setMandatoryHashMap() {
        mandatoryMap = new HashMap();
        mandatoryMap.put("txtEmpId", new Boolean(true));
        mandatoryMap.put("txtPresBasicSalary", new Boolean(true));
        mandatoryMap.put("txtLastIncrDate", new Boolean(true));
        mandatoryMap.put("txtEmpName", new Boolean(true));
        mandatoryMap.put("txtDesig", new Boolean(true));
        mandatoryMap.put("txtNextIncrDate", new Boolean(true));
    }

    /* Auto Generated Method - getMandatoryHashMap()
    Getter method for setMandatoryHashMap().*/
    public HashMap getMandatoryHashMap() {
        return mandatoryMap;
    }
    /* Auto Generated Method - setHelpMessage()
    This method shows tooltip help for all the input fields
    available in the UI. It needs the Mandatory Resource Bundle
    object. Help display Label name should be lblMsg. */

    public void setHelpMessage() {
        IncrementMRB objMandatoryRB = new IncrementMRB();
        txtEmpId.setHelpMessage(lblMsg, objMandatoryRB.getString("txtEmpId"));
        txtPresBasicSalary.setHelpMessage(lblMsg, objMandatoryRB.getString("txtPresBasicSalary"));
        cboDesig.setHelpMessage(lblMsg, objMandatoryRB.getString("cboDesig"));
        txtLastIncrDate.setHelpMessage(lblMsg, objMandatoryRB.getString("txtLastIncrDate"));
        txtNewBasicSalary.setHelpMessage(lblMsg, objMandatoryRB.getString("txtNewBasicSalary"));
        txtEmpName.setHelpMessage(lblMsg, objMandatoryRB.getString("txtEmpName"));
        txtDesig.setHelpMessage(lblMsg, objMandatoryRB.getString("txtDesig"));
        txtNextIncrDate.setHelpMessage(lblMsg, objMandatoryRB.getString("txtNextIncrDate"));
        txtNumberIncr.setHelpMessage(lblMsg, objMandatoryRB.getString("txtNumberIncr"));
    }

    private void setMaxLenths() {
    }

    private String checkMandatory(javax.swing.JComponent component) {
        return "";
    }
    // To set The Value of the Buttons Depending on the Value or Condition...

    private void setButtonEnableDisable() {
        btnNew.setEnabled(!btnNew.isEnabled());
        btnEdit.setEnabled(false);
        btnAuthorize.setEnabled(false);
        btnDelete.setEnabled(!btnDelete.isEnabled());
        mitNew.setEnabled(btnNew.isEnabled());
        mitEdit.setEnabled(btnEdit.isEnabled());
        mitDelete.setEnabled(btnDelete.isEnabled());
        btnSave.setEnabled(!btnSave.isEnabled());
        btnCancel.setEnabled(!btnNew.isEnabled());
        mitSave.setEnabled(btnSave.isEnabled());
        mitCancel.setEnabled(btnCancel.isEnabled());
        btnView.setEnabled(!btnView.isEnabled());
        btnReject.setEnabled(false);
    }

    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {
        java.awt.GridBagConstraints gridBagConstraints;

        panAgent = new com.see.truetransact.uicomponent.CPanel();
        cPanel1 = new com.see.truetransact.uicomponent.CPanel();
        cLabel1 = new com.see.truetransact.uicomponent.CLabel();
        rdoIncrement = new com.see.truetransact.uicomponent.CRadioButton();
        rdoPromotion = new com.see.truetransact.uicomponent.CRadioButton();
        panelEmployee = new com.see.truetransact.uicomponent.CPanel();
        cLabel2 = new com.see.truetransact.uicomponent.CLabel();
        cLabel3 = new com.see.truetransact.uicomponent.CLabel();
        cLabel4 = new com.see.truetransact.uicomponent.CLabel();
        cLabel5 = new com.see.truetransact.uicomponent.CLabel();
        cLabel6 = new com.see.truetransact.uicomponent.CLabel();
        cLabel7 = new com.see.truetransact.uicomponent.CLabel();
        cLabel8 = new com.see.truetransact.uicomponent.CLabel();
        cLabel9 = new com.see.truetransact.uicomponent.CLabel();
        cLabel10 = new com.see.truetransact.uicomponent.CLabel();
        cLabel11 = new com.see.truetransact.uicomponent.CLabel();
        txtEmpId = new com.see.truetransact.uicomponent.CTextField();
        txtPresBasicSalary = new com.see.truetransact.uicomponent.CTextField();
        txtLastIncrDate = new com.see.truetransact.uicomponent.CTextField();
        txtNewBasicSalary = new com.see.truetransact.uicomponent.CTextField();
        txtEmpName = new com.see.truetransact.uicomponent.CTextField();
        txtNumberIncr = new com.see.truetransact.uicomponent.CTextField();
        txtDesig = new com.see.truetransact.uicomponent.CTextField();
        txtNextIncrDate = new com.see.truetransact.uicomponent.CTextField();
        cboDesig = new com.see.truetransact.uicomponent.CComboBox();
        tdtNewIncrDate = new com.see.truetransact.uicomponent.CDateField();
        btnViewEmp = new com.see.truetransact.uicomponent.CButton();
        cLabel12 = new com.see.truetransact.uicomponent.CLabel();
        txtIncrAmt = new com.see.truetransact.uicomponent.CTextField();
        txtIncrId = new com.see.truetransact.uicomponent.CLabel();
        lblIncrId = new com.see.truetransact.uicomponent.CLabel();
        lblScaleId = new com.see.truetransact.uicomponent.CLabel();
        lblVersionNo = new com.see.truetransact.uicomponent.CLabel();
        lblVersionNoVal = new com.see.truetransact.uicomponent.CLabel();
        lblScaleIdVal = new com.see.truetransact.uicomponent.CLabel();
        panStatus = new com.see.truetransact.uicomponent.CPanel();
        lblSpace1 = new com.see.truetransact.uicomponent.CLabel();
        lblStatus = new com.see.truetransact.uicomponent.CLabel();
        lblMsg = new com.see.truetransact.uicomponent.CLabel();
        tbrLoantProduct = new javax.swing.JToolBar();
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
        mbrAgent = new com.see.truetransact.uicomponent.CMenuBar();
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
        setMaximumSize(new java.awt.Dimension(800, 600));
        setMinimumSize(new java.awt.Dimension(800, 600));
        setPreferredSize(new java.awt.Dimension(800, 600));

        panAgent.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        panAgent.setFocusCycleRoot(true);
        panAgent.setMinimumSize(new java.awt.Dimension(600, 500));
        panAgent.setPreferredSize(new java.awt.Dimension(600, 500));
        panAgent.setLayout(new java.awt.GridBagLayout());

        cPanel1.setBorder(javax.swing.BorderFactory.createTitledBorder(""));
        cPanel1.setLayout(new java.awt.GridBagLayout());

        cLabel1.setText("Increment Type");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(27, 40, 0, 0);
        cPanel1.add(cLabel1, gridBagConstraints);

        rdoIncrement.setText("Increment");
        rdoIncrement.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                rdoIncrementActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.gridheight = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(23, 18, 7, 0);
        cPanel1.add(rdoIncrement, gridBagConstraints);

        rdoPromotion.setText("Promotion");
        rdoPromotion.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                rdoPromotionActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.gridheight = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(23, 18, 7, 27);
        cPanel1.add(rdoPromotion, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.ipadx = 15;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(11, 124, 0, 0);
        panAgent.add(cPanel1, gridBagConstraints);

        panelEmployee.setBorder(javax.swing.BorderFactory.createTitledBorder(""));
        panelEmployee.setLayout(new java.awt.GridBagLayout());

        cLabel2.setText("Emp ID");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(10, 34, 0, 0);
        panelEmployee.add(cLabel2, gridBagConstraints);

        cLabel3.setText("New Designation");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(10, 34, 0, 0);
        panelEmployee.add(cLabel3, gridBagConstraints);

        cLabel4.setText("Last Increment Date");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(10, 34, 0, 0);
        panelEmployee.add(cLabel4, gridBagConstraints);

        cLabel5.setText("Present Basic Salary");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(9, 34, 0, 0);
        panelEmployee.add(cLabel5, gridBagConstraints);

        cLabel6.setText("Employee Name");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 4;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(10, 7, 0, 0);
        panelEmployee.add(cLabel6, gridBagConstraints);

        cLabel7.setText("New Basic Salary");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(10, 34, 0, 0);
        panelEmployee.add(cLabel7, gridBagConstraints);

        cLabel8.setText("Next Increment Date");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 4;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.gridwidth = 4;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(10, 7, 0, 0);
        panelEmployee.add(cLabel8, gridBagConstraints);

        cLabel9.setText("Designation");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 4;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(9, 7, 0, 0);
        panelEmployee.add(cLabel9, gridBagConstraints);

        cLabel10.setText("Number of Increments");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 4;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.gridwidth = 5;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(10, 7, 0, 0);
        panelEmployee.add(cLabel10, gridBagConstraints);

        cLabel11.setText("New Increment Date(Effective Date)");
        cLabel11.setAutoscrolls(true);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 4;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.gridwidth = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(10, 7, 0, 0);
        panelEmployee.add(cLabel11, gridBagConstraints);

        txtEmpId.setAllowAll(true);
        txtEmpId.setNextFocusableComponent(txtPresBasicSalary);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.ipadx = 140;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(10, 12, 0, 0);
        panelEmployee.add(txtEmpId, gridBagConstraints);

        txtPresBasicSalary.setAllowAll(true);
        txtPresBasicSalary.setNextFocusableComponent(txtLastIncrDate);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.ipadx = 140;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(9, 12, 0, 0);
        panelEmployee.add(txtPresBasicSalary, gridBagConstraints);

        txtLastIncrDate.setAllowAll(true);
        txtLastIncrDate.setNextFocusableComponent(cboDesig);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.ipadx = 140;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(10, 12, 0, 0);
        panelEmployee.add(txtLastIncrDate, gridBagConstraints);

        txtNewBasicSalary.setAllowAll(true);
        txtNewBasicSalary.setNextFocusableComponent(txtNumberIncr);
        txtNewBasicSalary.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                txtNewBasicSalaryFocusLost(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.ipadx = 140;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(10, 12, 0, 0);
        panelEmployee.add(txtNewBasicSalary, gridBagConstraints);

        txtEmpName.setAllowAll(true);
        txtEmpName.setNextFocusableComponent(txtDesig);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 9;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.ipadx = 140;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(10, 10, 0, 35);
        panelEmployee.add(txtEmpName, gridBagConstraints);

        txtNumberIncr.setAllowAll(true);
        txtNumberIncr.setNextFocusableComponent(tdtNewIncrDate);
        txtNumberIncr.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                txtNumberIncrFocusLost(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 9;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.ipadx = 140;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(10, 10, 0, 35);
        panelEmployee.add(txtNumberIncr, gridBagConstraints);

        txtDesig.setAllowAll(true);
        txtDesig.setNextFocusableComponent(txtNextIncrDate);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 9;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.ipadx = 140;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(9, 10, 0, 35);
        panelEmployee.add(txtDesig, gridBagConstraints);

        txtNextIncrDate.setAllowAll(true);
        txtNextIncrDate.setNextFocusableComponent(txtNumberIncr);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 9;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.ipadx = 140;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(10, 10, 0, 35);
        panelEmployee.add(txtNextIncrDate, gridBagConstraints);

        cboDesig.setMaximumSize(new java.awt.Dimension(50, 21));
        cboDesig.setMinimumSize(new java.awt.Dimension(50, 21));
        cboDesig.setNextFocusableComponent(txtNewBasicSalary);
        cboDesig.setPreferredSize(new java.awt.Dimension(50, 21));
        cboDesig.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cboDesigActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.ipadx = 100;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(11, 10, 0, 0);
        panelEmployee.add(cboDesig, gridBagConstraints);

        tdtNewIncrDate.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                tdtNewIncrDateFocusLost(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 9;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(10, 10, 0, 0);
        panelEmployee.add(tdtNewIncrDate, gridBagConstraints);

        btnViewEmp.setText("cButton1");
        btnViewEmp.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnViewEmpActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.ipadx = -40;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(10, 7, 0, 0);
        panelEmployee.add(btnViewEmp, gridBagConstraints);

        cLabel12.setText("Increment Amount");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 5;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(10, 34, 0, 0);
        panelEmployee.add(cLabel12, gridBagConstraints);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 5;
        gridBagConstraints.ipadx = 140;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(10, 12, 0, 0);
        panelEmployee.add(txtIncrAmt, gridBagConstraints);

        txtIncrId.setText("Increment ID");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 4;
        gridBagConstraints.gridy = 5;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(10, 7, 0, 0);
        panelEmployee.add(txtIncrId, gridBagConstraints);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 9;
        gridBagConstraints.gridy = 5;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(10, 10, 0, 0);
        panelEmployee.add(lblIncrId, gridBagConstraints);

        lblScaleId.setText("Scale ID");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 6;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(10, 34, 0, 0);
        panelEmployee.add(lblScaleId, gridBagConstraints);

        lblVersionNo.setText("Version No");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 7;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(10, 34, 0, 0);
        panelEmployee.add(lblVersionNo, gridBagConstraints);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 7;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(10, 12, 0, 0);
        panelEmployee.add(lblVersionNoVal, gridBagConstraints);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 6;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(10, 12, 0, 0);
        panelEmployee.add(lblScaleIdVal, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.ipadx = 27;
        gridBagConstraints.ipady = 38;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(18, 68, 87, 27);
        panAgent.add(panelEmployee, gridBagConstraints);

        getContentPane().add(panAgent, java.awt.BorderLayout.CENTER);

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

        lblSpace70.setForeground(new java.awt.Color(240, 240, 240));
        lblSpace70.setText("     ");
        lblSpace70.setMaximumSize(new java.awt.Dimension(3, 18));
        lblSpace70.setMinimumSize(new java.awt.Dimension(3, 18));
        lblSpace70.setPreferredSize(new java.awt.Dimension(3, 18));
        tbrLoantProduct.add(lblSpace70);

        btnEdit.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_EDIT.gif"))); // NOI18N
        btnEdit.setToolTipText("Edit");
        btnEdit.setEnabled(false);
        btnEdit.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnEditActionPerformed(evt);
            }
        });
        tbrLoantProduct.add(btnEdit);

        lblSpace71.setForeground(new java.awt.Color(240, 240, 240));
        lblSpace71.setText("     ");
        lblSpace71.setMaximumSize(new java.awt.Dimension(3, 18));
        lblSpace71.setMinimumSize(new java.awt.Dimension(3, 18));
        lblSpace71.setPreferredSize(new java.awt.Dimension(3, 18));
        tbrLoantProduct.add(lblSpace71);

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

        lblSpace72.setForeground(new java.awt.Color(240, 240, 240));
        lblSpace72.setText("     ");
        lblSpace72.setMaximumSize(new java.awt.Dimension(3, 18));
        lblSpace72.setMinimumSize(new java.awt.Dimension(3, 18));
        lblSpace72.setPreferredSize(new java.awt.Dimension(3, 18));
        tbrLoantProduct.add(lblSpace72);

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

        lblSpace73.setForeground(new java.awt.Color(240, 240, 240));
        lblSpace73.setText("     ");
        lblSpace73.setMaximumSize(new java.awt.Dimension(3, 18));
        lblSpace73.setMinimumSize(new java.awt.Dimension(3, 18));
        lblSpace73.setPreferredSize(new java.awt.Dimension(3, 18));
        tbrLoantProduct.add(lblSpace73);

        btnException.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_EXCEPTION.gif"))); // NOI18N
        btnException.setToolTipText("Exception");
        btnException.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnExceptionActionPerformed(evt);
            }
        });
        tbrLoantProduct.add(btnException);

        lblSpace74.setForeground(new java.awt.Color(240, 240, 240));
        lblSpace74.setText("     ");
        lblSpace74.setMaximumSize(new java.awt.Dimension(3, 18));
        lblSpace74.setMinimumSize(new java.awt.Dimension(3, 18));
        lblSpace74.setPreferredSize(new java.awt.Dimension(3, 18));
        tbrLoantProduct.add(lblSpace74);

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

        lblSpace75.setForeground(new java.awt.Color(240, 240, 240));
        lblSpace75.setText("     ");
        lblSpace75.setMaximumSize(new java.awt.Dimension(3, 18));
        lblSpace75.setMinimumSize(new java.awt.Dimension(3, 18));
        lblSpace75.setPreferredSize(new java.awt.Dimension(3, 18));
        tbrLoantProduct.add(lblSpace75);

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

        mbrAgent.add(mnuProcess);

        setJMenuBar(mbrAgent);

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btnViewActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnViewActionPerformed
        // TODO add your handling code here:
        observable.setActionType(ClientConstants.ACTIONTYPE_VIEW);
        observable.setStatus();
        lblStatus.setText(observable.getLblStatus());
        popUp(VIEW);
        btnCheck();
    }//GEN-LAST:event_btnViewActionPerformed

    private void btnExceptionActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnExceptionActionPerformed
        // TODO add your handling code here:
//        authorizeStatus(CommonConstants.STATUS_EXCEPTION);
    }//GEN-LAST:event_btnExceptionActionPerformed

    private void btnRejectActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnRejectActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_btnRejectActionPerformed

    private void btnAuthorizeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAuthorizeActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_btnAuthorizeActionPerformed
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

    public void authorizeStatus(int authorizeStatus) {
    }
    private void btnCloseActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCloseActionPerformed
        // TODO add your handling code here:
        cifClosingAlert();
//        this.dispose();
    }//GEN-LAST:event_btnCloseActionPerformed

    private void btnPrintActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnPrintActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_btnPrintActionPerformed

    private void btnCancelActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCancelActionPerformed
        // TODO add your handling code here:
        setModified(false);
        observable.resetForm();                 //__ Reset the fields in the UI to null...
        ClientUtil.clearAll(this);
        ClientUtil.enableDisable(this, false);  //__ Disables the panel...    
        if (!btnSave.isEnabled()) {
            btnSave.setEnabled(true);
        }
        setButtonEnableDisable();               //__ Enables or Disables the buttons and menu Items depending on their previous state...
        observable.setActionType(ClientConstants.ACTIONTYPE_CANCEL);//Sets the Action Type to be performed...
        observable.setStatus();                 //__ To set the Value of lblStatus..
        //__ Make the Screen Closable..
        setModified(false);
//        btnReject.setEnabled(true);
//        btnAuthorize.setEnabled(true);
        btnException.setEnabled(true);
        btnViewEmp.setEnabled(false);
        lblScaleIdVal.setText("");
        lblVersionNoVal.setText("");
        lblIncrId.setText("");

    }//GEN-LAST:event_btnCancelActionPerformed

    private void btnSaveActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSaveActionPerformed

        String mandatoryMessage = new MandatoryCheck().checkMandatory(getClass().getName(), panelEmployee);
        StringBuffer mandatoryAlert = new StringBuffer();
        if (mandatoryMessage.length() > 0) {
            mandatoryAlert.append(mandatoryMessage + "\n");
        }
        if (rdoPromotion.isSelected()) {
            if (cboDesig.getSelectedItem().toString().equalsIgnoreCase("")) {
                mandatoryAlert.append(resourceBundle.getString("cboDesig") + "\n");
            }
        }
        if (rdoIncrement.isSelected()) {
            if (txtNewBasicSalary.getText().equalsIgnoreCase("")) {
                mandatoryAlert.append(resourceBundle.getString("txtNewBasicSalary") + "\n");
            }
        }
        if(tdtNewIncrDate.getDateValue().isEmpty()){
            mandatoryAlert.append("new increment date should not be null"+"\n");
        }
        if (mandatoryAlert.length() > 0) {
            displayAlert(mandatoryAlert.toString());
            return;
        }
        if (rdoIncrement.isSelected()) {
            double oldBasic = CommonUtil.convertObjToDouble(txtPresBasicSalary.getText());
            double newbasic = CommonUtil.convertObjToDouble(txtNewBasicSalary.getText());
            if (newbasic < oldBasic) {
                ClientUtil.showAlertWindow("Please enter New Basic Salary greater than Present Basic Salary");
                txtNewBasicSalary.setText(null);
                txtNewBasicSalary.requestFocus();
                return;
            }
            HashMap basicMap = new HashMap();
            basicMap.put("DESIG", CommonUtil.convertObjToStr(txtDesig.getText()));
            basicMap.put("SCALEID", CommonUtil.convertObjToInt(lblScaleIdVal.getText()));
            basicMap.put("VERSION", CommonUtil.convertObjToStr(lblVersionNoVal.getText()));
            List basic = ClientUtil.executeQuery("getSalaryBasic", basicMap);
            if(basic!=null && basic.size()>0){
            HashMap resultMap = (HashMap) basic.get(0);
            double basicLimit = CommonUtil.convertObjToDouble(resultMap.get("SCALE_END_AMOUNT"));
            if (newbasic > basicLimit) {
                txtNewBasicSalary.setText("");
                ClientUtil.showAlertWindow("Basic salary crossed scale limit of this designation");
                txtNewBasicSalary.requestFocus();
                return;
                }
            }
        }
        String action = "";
        if (observable.getActionType() == ClientConstants.ACTIONTYPE_NEW) {
            action = CommonConstants.TOSTATUS_INSERT;
            saveAction(action);
        } else if (observable.getActionType() == ClientConstants.ACTIONTYPE_EDIT) {
            action = CommonConstants.TOSTATUS_UPDATE;
            saveAction(action);
        } else if (observable.getActionType() == ClientConstants.ACTIONTYPE_DELETE) {
            action = CommonConstants.TOSTATUS_DELETE;
            saveAction(action);
        }
    }//GEN-LAST:event_btnSaveActionPerformed
    private void saveAction(String status) {
        updateOBFields();
        observable.doAction(status);
        if (observable.getProxyReturnMap() != null) {
            observable.setResultStatus();
            lblStatus.setText(observable.getLblStatus());
        }
        observable.resetForm();                    //__ Reset the fields in the UI to null...
        ClientUtil.clearAll(this);
        ClientUtil.enableDisable(this, false);     //__ Disables the panel...
        btnCancelActionPerformed(null);
        observable.setResultStatus();
        setModified(false);
//        btnReject.setEnabled(true);
//        btnAuthorize.setEnabled(true);
        btnException.setEnabled(true);
        lblIncrId.setText("");
        lblScaleIdVal.setText("");
        lblVersionNoVal.setText("");
    }

    private void displayAlert(String message) {
        CMandatoryDialog cmd = new CMandatoryDialog();
        cmd.setMessage(message);
        cmd.show();
    }
    private void btnDeleteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnDeleteActionPerformed
        // TODO add your handling code here:
        observable.setActionType(ClientConstants.ACTIONTYPE_DELETE); //__ Sets the Action Type to be performed...
        callView(DELETE);
        setButtonEnableDisable();
        observable.setStatus();
        lblStatus.setText(observable.getLblStatus());
        btnReject.setEnabled(false);
        btnAuthorize.setEnabled(false);
        btnException.setEnabled(false);

    }//GEN-LAST:event_btnDeleteActionPerformed

    private void btnEditActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnEditActionPerformed
        // TODO add your handling code here:
        observable.resetForm();
        setButtonEnableDisable();             // Enables/Disables the necessary buttons and menu items...
        ClientUtil.enableDisable(this, true);
        observable.setActionType(ClientConstants.ACTIONTYPE_EDIT); //__ Sets the Action Type to be performed...
        callView(EDIT);
        observable.setStatus();
        lblStatus.setText(observable.getLblStatus());
        btnReject.setEnabled(false);
        btnAuthorize.setEnabled(false);
        btnException.setEnabled(false);
        setModified(true);
    }//GEN-LAST:event_btnEditActionPerformed
    private void popUp(int field) {
    }
    // this method is called automatically from ViewAll...

    public void fillData(Object param) {
        HashMap hash = (HashMap) param;
        HashMap OAprodDescMap = new HashMap();
        HashMap depProdDescMap = new HashMap();
        System.out.println("hash:  " + hash);
        if (viewType == EMP_DETAILS || viewType == DELETE) {
            txtEmpId.setText(CommonUtil.convertObjToStr(hash.get("EMPLOYEEID")));
            txtPresBasicSalary.setText(CommonUtil.convertObjToStr(hash.get("PRESENTBASIC")));
            txtLastIncrDate.setText(CommonUtil.convertObjToStr(hash.get("LASTINCREMENT")));
            txtNextIncrDate.setText(CommonUtil.convertObjToStr(hash.get("NEXTINCREMENT")));
            txtEmpName.setText(CommonUtil.convertObjToStr(hash.get("NAME")));
            txtDesig.setText(CommonUtil.convertObjToStr(hash.get("DESIGNATION")));
            txtNumberIncr.setText(CommonUtil.convertObjToStr(CommonUtil.convertObjToInt(hash.get("INCREMENTCOUNT"))+1));
            scaleId = CommonUtil.convertObjToStr(hash.get("SCALEID"));
            lblScaleIdVal.setText(CommonUtil.convertObjToStr(hash.get("SCALEID")));
            lblVersionNoVal.setText(CommonUtil.convertObjToStr(hash.get("VERSIONNO")));
            if(hash.containsKey("INCR_ID")&& CommonUtil.convertObjToStr(hash.get("INCR_ID"))!=null){
                lblIncrId.setText(CommonUtil.convertObjToStr(hash.get("INCR_ID")));
            }
            hash.put("INCREMENTCOUNT", CommonUtil.convertObjToStr(CommonUtil.convertObjToInt(hash.get("INCREMENTCOUNT"))+1));
//              String scaleDet = "";
//              scaleDet = CommonUtil.convertObjToStr(hash.get("SCALEID"))+"-"+
//                    CommonUtil.convertObjToStr(hash.get("VERSIONNO"))+"-"+
//                    CommonUtil.convertObjToStr(hash.get("PRESENTBASIC"))+"-"+
//                    CommonUtil.convertObjToStr(incrMap.get("INCR_AMOUNT"));
            if (rdoIncrement.isSelected()) {
                txtNewBasicSalary.requestFocus();
//                lblScaleDetails.setText(scaleDet);
                HashMap incrMap = getSalaryIncrement(hash);
                int newBasic = CommonUtil.convertObjToInt(hash.get("PRESENTBASIC"))+CommonUtil.convertObjToInt(incrMap.get("INCR_AMOUNT"));
                txtNewBasicSalary.setText(CommonUtil.convertObjToStr(newBasic));
                txtIncrAmt.setText(CommonUtil.convertObjToStr(incrMap.get("INCR_AMOUNT")));
            }
            if (rdoPromotion.isSelected()) {
                cboDesig.requestFocus();
                
            }
            disablefields();
            txtNumberIncr.setEnabled(true);
        }
        if (viewType == EDIT) {
            txtEmpId.setText(CommonUtil.convertObjToStr(hash.get("EMPLOYEEID")));
            txtPresBasicSalary.setText(CommonUtil.convertObjToStr(hash.get("PRESENT_BASIC")));
            txtLastIncrDate.setText(CommonUtil.convertObjToStr(hash.get("LAST_INCREAMENT_DATE")));
            txtNextIncrDate.setText(CommonUtil.convertObjToStr(hash.get("NEXT_INCREAMENT_DATE")));
            txtEmpName.setText(CommonUtil.convertObjToStr(hash.get("EMPLOYEE_NAME")));
            txtDesig.setText(CommonUtil.convertObjToStr(hash.get("DESIGNATION")));
            disablefields();
        }
    }
    private HashMap getSalaryIncrement(HashMap hash){
        HashMap scaleParamMap = new HashMap();
        HashMap incrMap = new HashMap();
        scaleParamMap.put("SCALE_ID", CommonUtil.convertObjToStr(hash.get("SCALEID")));
        scaleParamMap.put("VERSION_NO", CommonUtil.convertObjToStr(hash.get("VERSIONNO")));
        scaleParamMap.put("INCREMENT_COUNT", CommonUtil.convertObjToStr(CommonUtil.convertObjToInt(hash.get("INCREMENTCOUNT"))));
        List incrAmtList = ClientUtil.executeQuery("getIncrementAmount", scaleParamMap);
        if(incrAmtList!=null && incrAmtList.size()>0){
             incrMap = (HashMap) incrAmtList.get(0);
        }
        return incrMap;
    }
    private void btnNewActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnNewActionPerformed
        // TODO add your handling cde here:
        observable.resetForm();
        observable.setActionType(ClientConstants.ACTIONTYPE_NEW);// to Reset all the Fields and Status in UI...
        setButtonEnableDisable();             // Enables/Disables the necessary buttons and menu items...
        ClientUtil.enableDisable(this, true); // Enables the panel...
        observable.setStatus();
        lblStatus.setText(observable.getLblStatus());// To set the Value of lblStatus...
        btnReject.setEnabled(false);
        btnAuthorize.setEnabled(false);
        btnException.setEnabled(false);
        btnViewEmp.setEnabled(true);
        rdoIncrement.setSelected(true);
        cboDesig.setEnabled(false);
        btnViewEmp.requestFocus();
        setModified(true);
    }//GEN-LAST:event_btnNewActionPerformed
    private void disablefields() {
        txtEmpId.setEnabled(false);
        txtPresBasicSalary.setEnabled(false);
        txtLastIncrDate.setEnabled(false);
        txtEmpName.setEnabled(false);
        txtDesig.setEnabled(false);
        txtNextIncrDate.setEnabled(false);
        txtNumberIncr.setEnabled(false);
        txtNewBasicSalary.setEnabled(false);
        txtIncrAmt.setEnabled(false);
    }
    private void mitCloseActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mitCloseActionPerformed
        // TODO add your handling code here:
        btnCloseActionPerformed(null);
    }//GEN-LAST:event_mitCloseActionPerformed

    private void mitPrintActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mitPrintActionPerformed
        // TODO add your handling code here:
        btnPrintActionPerformed(null);
    }//GEN-LAST:event_mitPrintActionPerformed

    private void mitCancelActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mitCancelActionPerformed
        // TODO add your handling code here:
        btnCancelActionPerformed(null);
    }//GEN-LAST:event_mitCancelActionPerformed

    private void mitSaveActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mitSaveActionPerformed
        // TODO add your handling code here:
        btnSaveActionPerformed(null);
    }//GEN-LAST:event_mitSaveActionPerformed

    private void mitDeleteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mitDeleteActionPerformed
        // TODO add your handling code here:
        btnDeleteActionPerformed(null);
    }//GEN-LAST:event_mitDeleteActionPerformed

    private void mitEditActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mitEditActionPerformed
        // TODO add your handling code here:
        btnEditActionPerformed(null);
    }//GEN-LAST:event_mitEditActionPerformed

    private void mitNewActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mitNewActionPerformed
        // TODO add your handling code here:
        btnNewActionPerformed(null);
    }//GEN-LAST:event_mitNewActionPerformed

    /** Exit the Application */
    private void exitForm(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_exitForm
        System.exit(0);
    }//GEN-LAST:event_exitForm

private void btnViewEmpActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnViewEmpActionPerformed
// TODO add your handling code here:
    callView(EMP_DETAILS);
}//GEN-LAST:event_btnViewEmpActionPerformed

private void txtNewBasicSalaryFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtNewBasicSalaryFocusLost
}//GEN-LAST:event_txtNewBasicSalaryFocusLost

private void rdoIncrementActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_rdoIncrementActionPerformed
// TODO add your handling code here:
    cboDesig.setEnabled(false);
    txtNewBasicSalary.setEnabled(true);
}//GEN-LAST:event_rdoIncrementActionPerformed

private void rdoPromotionActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_rdoPromotionActionPerformed
// TODO add your handling code here:
    cboDesig.setEnabled(true);
    txtNewBasicSalary.setEnabled(false);
}//GEN-LAST:event_rdoPromotionActionPerformed

private void txtNumberIncrFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtNumberIncrFocusLost
// TODO add your handling code here:
//    HashMap salParMap = new HashMap();
//    salParMap.put("SCALEID", CommonUtil.convertObjToInt(lblScaleIdVal.getText()));
//    salParMap.put("VERSIONNO", CommonUtil.convertObjToInt(lblVersionNoVal.getText()));
//    salParMap.put("INCREMENTCOUNT", CommonUtil.convertObjToInt(txtNumberIncr.getText()));
//        HashMap incrMap = getSalaryIncrement(salParMap);
//                int newBasic = CommonUtil.convertObjToInt(txtPresBasicSalary.getText())+CommonUtil.convertObjToInt(incrMap.get("INCR_AMOUNT"));
//                txtNewBasicSalary.setText(CommonUtil.convertObjToStr(newBasic));
//                txtIncrAmt.setText(CommonUtil.convertObjToStr(incrMap.get("INCR_AMOUNT")));
     try {
            int scale = CommonUtil.convertObjToInt(lblScaleIdVal.getText());
            int incrmtcnt = CommonUtil.convertObjToInt(txtNumberIncr.getText());
            HashMap dataMap1 = new HashMap();
            dataMap1.put("SCALE", scale);
            dataMap1.put("INCR", incrmtcnt);
            final HashMap resultMap1 = observable.getIncrementStagCount(dataMap1);
            int increment = CommonUtil.convertObjToInt(resultMap1.get("INCREAMENT_COUNT"));
            if (incrmtcnt > increment) {
                ClientUtil.showAlertWindow("Increment Count entered is greater than actual Increment Count for selected Scale ID");
                txtNumberIncr.setText("");
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
            double incrementamount=0;
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
                    incrementamount=incrementamount+s.getIncAmt();
                    salary = salary + s.getIncAmt();
                    c--;
                    i--;
                }
            }
            txtNewBasicSalary.setText(CommonUtil.convertObjToStr(salary));
             txtIncrAmt.setText(CommonUtil.convertObjToStr(CommonUtil.convertObjToStr(incrementamount)));
        } 
     catch (SQLException ex) {
            java.util.logging.Logger.getLogger(IncrementUI.class.getName()).log(Level.SEVERE, null, ex);
        }
}//GEN-LAST:event_txtNumberIncrFocusLost

private void cboDesigActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cboDesigActionPerformed
// TODO add your handling code here:
    lblScaleIdVal.setText("");
    lblVersionNoVal.setText("");
    HashMap desigMap = new HashMap();
    desigMap.put("DESIG", cboDesig.getSelectedItem());
    List basicSalList = ClientUtil.executeQuery("getDesigBasicSalary", desigMap);
    if(basicSalList!=null && basicSalList.size()>0){
    HashMap basicMap = (HashMap) basicSalList.get(0);
    double basicStart = CommonUtil.convertObjToDouble(basicMap.get("SCALE_START_AMOUNT"));
    double currBasic = CommonUtil.convertObjToDouble(txtPresBasicSalary.getText());
    double incrAmt = 0.0;
    if(basicStart<currBasic){
        double addAmt = 0.0;
        addAmt = currBasic - basicStart;
        txtNewBasicSalary.setText(CommonUtil.convertObjToStr(currBasic));
        HashMap param = new HashMap();
        param.put("SCALE_ID", basicMap.get("SCALE_ID"));
        param.put("INCR_AMT", addAmt);
        param.put("VERSION_NO", basicMap.get("VERSION_NO"));
        List newScaleList = ClientUtil.executeQuery("getNumberOfIncrements", param); 
        if(newScaleList!=null && newScaleList.size()>0){
          HashMap incrCount = (HashMap) newScaleList.get(0);
          txtNumberIncr.setText(CommonUtil.convertObjToStr(incrCount.get("INCREMENT_COUNT")));
        }
    }
    else{
        incrAmt = basicStart - currBasic;
        txtNewBasicSalary.setText(CommonUtil.convertObjToStr(basicStart));
        txtNumberIncr.setText("1");
    }
    txtIncrAmt.setText(CommonUtil.convertObjToStr(incrAmt));
    lblScaleIdVal.setText(CommonUtil.convertObjToStr(basicMap.get("SCALE_ID")));
    lblVersionNoVal.setText(CommonUtil.convertObjToStr(basicMap.get("VERSION_NO")));
    }
}//GEN-LAST:event_cboDesigActionPerformed

private void tdtNewIncrDateFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_tdtNewIncrDateFocusLost
}//GEN-LAST:event_tdtNewIncrDateFocusLost
    /* This method is used to popup the window which will have some display
    
     * information
     */

    private void callView(int currField) {
        viewType = currField;
        HashMap whereMap = new HashMap();
        HashMap viewMap = new HashMap();
        ArrayList lst = new ArrayList();
        if (currField == EMP_DETAILS) {  
            viewMap.put(ClientConstants.RECORD_KEY_COL, lst);
            viewMap.put(CommonConstants.MAP_NAME, "getEmpDetailsIncrement");
            viewMap.put(CommonConstants.MAP_WHERE, whereMap);
            new ViewAll(this, viewMap).show();
        }
        else if (currField == EDIT) {
//            HashMap whereMap = new HashMap();
//            HashMap viewMap = new HashMap();
//            ArrayList lst = new ArrayList();
//            viewMap.put(ClientConstants.RECORD_KEY_COL, lst);
//            viewMap.put(CommonConstants.MAP_NAME, "getEmpDetailsIncrementEdit");
//            viewMap.put(CommonConstants.MAP_WHERE, whereMap);
//            new ViewAll(this, viewMap).show();
        }
        else if(currField == DELETE){
            viewMap.put(ClientConstants.RECORD_KEY_COL, lst);
            whereMap.put("CURDATE", setProperDateFormat(currDt));
            viewMap.put(CommonConstants.MAP_NAME, "getPayrollIncrementDetailsReject");
            viewMap.put(CommonConstants.MAP_WHERE, whereMap);
            new ViewAll(this, viewMap).show();
        }
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
    private void initComponentData() {
        try {
            cboDesig.setModel(observable.getCbmDesig());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        new IncrementUI().show();
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
    private com.see.truetransact.uicomponent.CButton btnView;
    private com.see.truetransact.uicomponent.CButton btnViewEmp;
    private com.see.truetransact.uicomponent.CLabel cLabel1;
    private com.see.truetransact.uicomponent.CLabel cLabel10;
    private com.see.truetransact.uicomponent.CLabel cLabel11;
    private com.see.truetransact.uicomponent.CLabel cLabel12;
    private com.see.truetransact.uicomponent.CLabel cLabel2;
    private com.see.truetransact.uicomponent.CLabel cLabel3;
    private com.see.truetransact.uicomponent.CLabel cLabel4;
    private com.see.truetransact.uicomponent.CLabel cLabel5;
    private com.see.truetransact.uicomponent.CLabel cLabel6;
    private com.see.truetransact.uicomponent.CLabel cLabel7;
    private com.see.truetransact.uicomponent.CLabel cLabel8;
    private com.see.truetransact.uicomponent.CLabel cLabel9;
    private com.see.truetransact.uicomponent.CPanel cPanel1;
    private com.see.truetransact.uicomponent.CComboBox cboDesig;
    private com.see.truetransact.uicomponent.CLabel lblIncrId;
    private com.see.truetransact.uicomponent.CLabel lblMsg;
    private com.see.truetransact.uicomponent.CLabel lblScaleId;
    private com.see.truetransact.uicomponent.CLabel lblScaleIdVal;
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
    private com.see.truetransact.uicomponent.CLabel lblStatus;
    private com.see.truetransact.uicomponent.CLabel lblVersionNo;
    private com.see.truetransact.uicomponent.CLabel lblVersionNoVal;
    private com.see.truetransact.uicomponent.CMenuBar mbrAgent;
    private javax.swing.JMenuItem mitCancel;
    private javax.swing.JMenuItem mitClose;
    private javax.swing.JMenuItem mitDelete;
    private javax.swing.JMenuItem mitEdit;
    private javax.swing.JMenuItem mitNew;
    private javax.swing.JMenuItem mitPrint;
    private javax.swing.JMenuItem mitSave;
    private javax.swing.JMenu mnuProcess;
    private com.see.truetransact.uicomponent.CPanel panAgent;
    private com.see.truetransact.uicomponent.CPanel panStatus;
    private com.see.truetransact.uicomponent.CPanel panelEmployee;
    private com.see.truetransact.uicomponent.CRadioButton rdoIncrement;
    private com.see.truetransact.uicomponent.CRadioButton rdoPromotion;
    private javax.swing.JSeparator sptCancel;
    private javax.swing.JSeparator sptPrint;
    private javax.swing.JSeparator sptProcess;
    private javax.swing.JToolBar tbrLoantProduct;
    private com.see.truetransact.uicomponent.CDateField tdtNewIncrDate;
    private com.see.truetransact.uicomponent.CTextField txtDesig;
    private com.see.truetransact.uicomponent.CTextField txtEmpId;
    private com.see.truetransact.uicomponent.CTextField txtEmpName;
    private com.see.truetransact.uicomponent.CTextField txtIncrAmt;
    private com.see.truetransact.uicomponent.CLabel txtIncrId;
    private com.see.truetransact.uicomponent.CTextField txtLastIncrDate;
    private com.see.truetransact.uicomponent.CTextField txtNewBasicSalary;
    private com.see.truetransact.uicomponent.CTextField txtNextIncrDate;
    private com.see.truetransact.uicomponent.CTextField txtNumberIncr;
    private com.see.truetransact.uicomponent.CTextField txtPresBasicSalary;
    // End of variables declaration//GEN-END:variables
}
