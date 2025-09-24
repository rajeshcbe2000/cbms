/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition. 
 * 
 * SalaryProcessUI.java
 *
 * 
 */
package com.see.truetransact.ui.payroll.SalaryProcess;

import com.see.truetransact.clientutil.CMandatoryDialog;
import com.see.truetransact.clientutil.ClientUtil;
import com.see.truetransact.commonutil.CommonConstants;
import com.see.truetransact.commonutil.CommonUtil;
import com.see.truetransact.commonutil.DateUtil;
import com.see.truetransact.ui.payroll.SalaryProcess.SalaryProcessOB;
import com.see.truetransact.uicomponent.CInternalFrame;
import com.see.truetransact.uimandatory.UIMandatoryField;
import java.text.DateFormat;
import java.text.DateFormatSymbols;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import javax.swing.JDialog;
import javax.swing.SwingWorker;

/**
 *
 * @author rishad
 */
public class SalaryProcessUI extends CInternalFrame implements java.util.Observer, UIMandatoryField {

    HashMap mandatoryMap;
    int viewType = -1;
    boolean isFilled = false;
    SalaryProcessOB observable;
    Date currDt;

    /**
     * Creates new form SalaryProcessUI
     */
    public SalaryProcessUI() {
        initComponents();
        initSetup();
        setObservable();
        addRadioButtons();
        setRadioButtons();
        panSalaryData.enableInputMethods(true);
        panSalaryData.enable(true);
        panSalaryData.setVisible(true);
        btnEdit.setEnabled(false);
        btnDelete.setEnabled(false);
        btnException.setEnabled(false);
        btnPrint.setEnabled(false);
        currDt = ClientUtil.getCurrentDate();
    }

    private void setObservable() {
        try {
            observable = new SalaryProcessOB();
            observable.addObserver(this);
        } catch (Exception e) {
            System.out.println("Exception setObservable :" + e);
        }
    }

    private void initSetup() {
        setFieldNames();
        internationalize();
        setMandatoryHashMap();
        setHelpMessage();
        setObservable();
        ClientUtil.enableDisable(this, false); //__ Disables all when the screen appears for the 1st time
        setButtonEnableDisable();              //__ Enables/Disables the necessary buttons and menu items...
        setEnableDisableButtons(false);
        panSalaryDate.setVisible(true);

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
        lblMsg.setName("lblMsg");
        lblSpace.setName("lblSpace");
        lblSpace2.setName("lblSpace2");
        lblSpace3.setName("lblSpace3");
        lblSpace4.setName("lblSpace4");
        lblStatus.setName("lblStatus");
        mbrSalaryProcess.setName("mbrSalaryProcess");
        panStatus.setName("panStatus");
    }

    public void populateData() {
        HashMap whereMap = new HashMap();
        HashMap viewMap = new HashMap();
        viewMap.put(CommonConstants.MAP_NAME, "getEmployeeData");
        viewMap.put(CommonConstants.MAP_WHERE, whereMap);
        try {
            ArrayList heading = observable.populateData(viewMap, tblEmployeeDetails, true);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void populateSettingData() {
        HashMap whereMap = new HashMap();
        HashMap viewMap = new HashMap();
        viewMap.put(CommonConstants.MAP_NAME, "getSettingData");
        viewMap.put(CommonConstants.MAP_WHERE, whereMap);
        try {
            ArrayList heading = observable.populateData(viewMap, tblPaySettingDetails, true);
        } catch (Exception e) {;
            e.printStackTrace();
        }
    }

    public void populateSalaryData(HashMap whereMap) {
        HashMap viewMap = new HashMap();
        viewMap.put(CommonConstants.MAP_NAME, "getSalaryProcessData");
        viewMap.put(CommonConstants.MAP_WHERE, whereMap);
        try {
            ArrayList heading = observable.populateData(viewMap, tblpayrollDetails, false);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void populatepayrollData(HashMap whereMap) {
        HashMap viewMap = new HashMap();
        viewMap.put(CommonConstants.MAP_NAME, "getSelectPayrollData");
        viewMap.put(CommonConstants.MAP_WHERE, whereMap);
        try {
            ArrayList heading = observable.populateData(viewMap, tblpayrollDetails, false);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /*
     * Auto Generated Method - internationalize() This method used to assign
     * display texts from the Resource Bundle File.
     */
    private void internationalize() {
    }

    /*
     * Auto Generated Method - update() This method called by Observable. It
     * updates the UI with Observable's data. If needed add/Remove RadioButtons
     * method need to be added.
     */
    public void update(Observable observed, Object arg) {
        this.addRadioButtons();
        panSalaryDate.setVisible(true);
        panSalaryDate.setEnabled(true);
    }

    private void removeRadioButtons() {
    }

    private void addRadioButtons() {
    }

    private void setRadioButtons() {
        this.removeRadioButtons();
        this.addRadioButtons();
    }

    /*
     * Auto Generated Method - updateOBFields() This method called by Save
     * option of UI. It updates the OB with UI data.
     */
    public void updateOBFields() {
    }

    /*
     * Auto Generated Method - setMandatoryHashMap()
     *
     * ADD: implements com.see.truetransact.uimandatory.UIMandatoryField
     *
     * This method list out all the Input Fields available in the UI. It needs a
     * class level HashMap variable mandatoryMap.
     */
    public void setMandatoryHashMap() {
        mandatoryMap = new HashMap();
    }

    /*
     * Auto Generated Method - getMandatoryHashMap() Getter method for
     * setMandatoryHashMap().
     */
    public HashMap getMandatoryHashMap() {
        return mandatoryMap;
    }

    /*
     * Auto Generated Method - setHelpMessage() This method shows tooltip help
     * for all the input fields available in the UI. It needs the Mandatory
     * Resource Bundle object. Help display Label name should be lblMsg.
     */
    public void setHelpMessage() {
    }

    // To set The Value of the Buttons Depending on the Value or Condition...
    private void setButtonEnableDisable() {
        btnNew.setEnabled(!btnNew.isEnabled());
        btnEdit.setEnabled(!btnEdit.isEnabled());
        btnDelete.setEnabled(!btnDelete.isEnabled());
        mitNew.setEnabled(btnNew.isEnabled());
        mitEdit.setEnabled(btnEdit.isEnabled());
        mitDelete.setEnabled(btnDelete.isEnabled());
        btnSave.setEnabled(!btnSave.isEnabled());
        btnCancel.setEnabled(!btnNew.isEnabled());
        mitSave.setEnabled(btnSave.isEnabled());
        mitCancel.setEnabled(btnCancel.isEnabled());

    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {
        java.awt.GridBagConstraints gridBagConstraints;

        panStatus = new com.see.truetransact.uicomponent.CPanel();
        lblSpace = new com.see.truetransact.uicomponent.CLabel();
        lblStatus = new com.see.truetransact.uicomponent.CLabel();
        lblMsg = new com.see.truetransact.uicomponent.CLabel();
        tbrPfMaster = new javax.swing.JToolBar();
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
        panSalaryProcessUI = new com.see.truetransact.uicomponent.CPanel();
        panTblDetails = new com.see.truetransact.uicomponent.CPanel();
        panSalaryDate = new com.see.truetransact.uicomponent.CPanel();
        panSalaryDetails = new com.see.truetransact.uicomponent.CPanel();
        panSalData2 = new com.see.truetransact.uicomponent.CPanel();
        srpPaySettingDetails = new com.see.truetransact.uicomponent.CScrollPane();
        tblPaySettingDetails = new com.see.truetransact.uicomponent.CTable();
        panChk2 = new com.see.truetransact.uicomponent.CPanel();
        chkSet = new com.see.truetransact.uicomponent.CCheckBox();
        panSalData1 = new com.see.truetransact.uicomponent.CPanel();
        srpemployeeDetails = new com.see.truetransact.uicomponent.CScrollPane();
        tblEmployeeDetails = new com.see.truetransact.uicomponent.CTable();
        panChk1 = new com.see.truetransact.uicomponent.CPanel();
        chkemp = new com.see.truetransact.uicomponent.CCheckBox();
        panSalDat3 = new com.see.truetransact.uicomponent.CPanel();
        panSalaryData = new com.see.truetransact.uicomponent.CPanel();
        cLabel2 = new com.see.truetransact.uicomponent.CLabel();
        tdtSalaryMonth = new com.see.truetransact.uicomponent.CDateField();
        cLabel3 = new com.see.truetransact.uicomponent.CLabel();
        tdtCurrentDate = new com.see.truetransact.uicomponent.CDateField();
        cLabel4 = new com.see.truetransact.uicomponent.CLabel();
        tdtInterestDate = new com.see.truetransact.uicomponent.CDateField();
        panSalData = new com.see.truetransact.uicomponent.CPanel();
        cButton3 = new com.see.truetransact.uicomponent.CButton();
        btnAdd = new com.see.truetransact.uicomponent.CButton();
        panSalary = new com.see.truetransact.uicomponent.CPanel();
        panSubmit = new com.see.truetransact.uicomponent.CPanel();
        cButton1 = new com.see.truetransact.uicomponent.CButton();
        cButton2 = new com.see.truetransact.uicomponent.CButton();
        btnLeaveProcess = new com.see.truetransact.uicomponent.CButton();
        srpPayrollDetails = new javax.swing.JScrollPane();
        tblpayrollDetails = new com.see.truetransact.uicomponent.CTable();
        mbrSalaryProcess = new com.see.truetransact.uicomponent.CMenuBar();
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
        setMaximumSize(new java.awt.Dimension(980, 650));
        setMinimumSize(new java.awt.Dimension(980, 650));
        setPreferredSize(new java.awt.Dimension(980, 650));

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

        getContentPane().add(panStatus, java.awt.BorderLayout.SOUTH);

        btnNew.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_NEW.gif"))); // NOI18N
        btnNew.setToolTipText("New");
        btnNew.setEnabled(false);
        btnNew.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnNewActionPerformed(evt);
            }
        });
        tbrPfMaster.add(btnNew);

        lblSpace17.setForeground(new java.awt.Color(240, 240, 240));
        lblSpace17.setText("     ");
        lblSpace17.setMaximumSize(new java.awt.Dimension(3, 18));
        lblSpace17.setMinimumSize(new java.awt.Dimension(3, 18));
        lblSpace17.setPreferredSize(new java.awt.Dimension(3, 18));
        tbrPfMaster.add(lblSpace17);

        btnEdit.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_EDIT.gif"))); // NOI18N
        btnEdit.setToolTipText("Edit");
        btnEdit.setEnabled(false);
        btnEdit.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnEditActionPerformed(evt);
            }
        });
        tbrPfMaster.add(btnEdit);

        lblSpace18.setForeground(new java.awt.Color(240, 240, 240));
        lblSpace18.setText("     ");
        lblSpace18.setMaximumSize(new java.awt.Dimension(3, 18));
        lblSpace18.setMinimumSize(new java.awt.Dimension(3, 18));
        lblSpace18.setPreferredSize(new java.awt.Dimension(3, 18));
        tbrPfMaster.add(lblSpace18);

        btnDelete.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_DELETE.gif"))); // NOI18N
        btnDelete.setToolTipText("Delete");
        btnDelete.setEnabled(false);
        btnDelete.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnDeleteActionPerformed(evt);
            }
        });
        tbrPfMaster.add(btnDelete);

        lblSpace2.setText("     ");
        tbrPfMaster.add(lblSpace2);

        btnSave.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_SAVE.gif"))); // NOI18N
        btnSave.setToolTipText("Save");
        btnSave.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSaveActionPerformed(evt);
            }
        });
        tbrPfMaster.add(btnSave);

        lblSpace19.setForeground(new java.awt.Color(240, 240, 240));
        lblSpace19.setText("     ");
        lblSpace19.setMaximumSize(new java.awt.Dimension(3, 18));
        lblSpace19.setMinimumSize(new java.awt.Dimension(3, 18));
        lblSpace19.setPreferredSize(new java.awt.Dimension(3, 18));
        tbrPfMaster.add(lblSpace19);

        btnCancel.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_CANCEL.gif"))); // NOI18N
        btnCancel.setToolTipText("Cancel");
        btnCancel.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCancelActionPerformed(evt);
            }
        });
        tbrPfMaster.add(btnCancel);

        lblSpace3.setText("     ");
        tbrPfMaster.add(lblSpace3);

        btnAuthorize.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_AUTHORIZE.gif"))); // NOI18N
        btnAuthorize.setToolTipText("Authorize");
        tbrPfMaster.add(btnAuthorize);

        lblSpace20.setForeground(new java.awt.Color(240, 240, 240));
        lblSpace20.setText("     ");
        lblSpace20.setMaximumSize(new java.awt.Dimension(3, 18));
        lblSpace20.setMinimumSize(new java.awt.Dimension(3, 18));
        lblSpace20.setPreferredSize(new java.awt.Dimension(3, 18));
        tbrPfMaster.add(lblSpace20);

        btnException.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_EXCEPTION.gif"))); // NOI18N
        btnException.setToolTipText("Exception");
        btnException.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnExceptionActionPerformed(evt);
            }
        });
        tbrPfMaster.add(btnException);

        lblSpace21.setForeground(new java.awt.Color(240, 240, 240));
        lblSpace21.setText("     ");
        lblSpace21.setMaximumSize(new java.awt.Dimension(3, 18));
        lblSpace21.setMinimumSize(new java.awt.Dimension(3, 18));
        lblSpace21.setPreferredSize(new java.awt.Dimension(3, 18));
        tbrPfMaster.add(lblSpace21);

        btnReject.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_REJECT.gif"))); // NOI18N
        btnReject.setToolTipText("Reject");
        btnReject.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnRejectActionPerformed(evt);
            }
        });
        tbrPfMaster.add(btnReject);

        lblSpace4.setText("     ");
        tbrPfMaster.add(lblSpace4);

        btnPrint.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_PRINT.gif"))); // NOI18N
        btnPrint.setToolTipText("Print");
        btnPrint.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnPrintActionPerformed(evt);
            }
        });
        tbrPfMaster.add(btnPrint);

        lblSpace22.setForeground(new java.awt.Color(240, 240, 240));
        lblSpace22.setText("     ");
        lblSpace22.setMaximumSize(new java.awt.Dimension(3, 18));
        lblSpace22.setMinimumSize(new java.awt.Dimension(3, 18));
        lblSpace22.setPreferredSize(new java.awt.Dimension(3, 18));
        tbrPfMaster.add(lblSpace22);

        btnClose.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_CLOSE.gif"))); // NOI18N
        btnClose.setToolTipText("Close");
        btnClose.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCloseActionPerformed(evt);
            }
        });
        tbrPfMaster.add(btnClose);

        getContentPane().add(tbrPfMaster, java.awt.BorderLayout.NORTH);

        panSalaryProcessUI.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        panSalaryProcessUI.setMinimumSize(new java.awt.Dimension(700, 660));
        panSalaryProcessUI.setPreferredSize(new java.awt.Dimension(700, 660));
        panSalaryProcessUI.setLayout(new java.awt.GridBagLayout());

        panTblDetails.setBorder(javax.swing.BorderFactory.createBevelBorder(0));
        panTblDetails.setMinimumSize(new java.awt.Dimension(950, 280));
        panTblDetails.setPreferredSize(new java.awt.Dimension(1200, 600));
        panTblDetails.setLayout(new java.awt.GridLayout(1, 0));

        panSalaryDate.setBorder(javax.swing.BorderFactory.createTitledBorder(""));
        panSalaryDate.setMinimumSize(new java.awt.Dimension(430, 300));
        panSalaryDate.setPreferredSize(new java.awt.Dimension(430, 300));
        panSalaryDate.setLayout(new java.awt.GridBagLayout());

        panSalaryDetails.setMinimumSize(new java.awt.Dimension(930, 270));
        panSalaryDetails.setPreferredSize(new java.awt.Dimension(930, 270));
        panSalaryDetails.setLayout(new java.awt.GridBagLayout());

        panSalData2.setBorder(javax.swing.BorderFactory.createTitledBorder(""));
        panSalData2.setMinimumSize(new java.awt.Dimension(350, 250));
        panSalData2.setPreferredSize(new java.awt.Dimension(350, 600));
        panSalData2.setLayout(new java.awt.GridBagLayout());

        srpPaySettingDetails.setMinimumSize(new java.awt.Dimension(325, 200));
        srpPaySettingDetails.setName("");
        srpPaySettingDetails.setRequestFocusEnabled(false);

        tblPaySettingDetails.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {},
                {},
                {},
                {}
            },
            new String [] {

            }
        ));
        srpPaySettingDetails.setViewportView(tblPaySettingDetails);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTH;
        gridBagConstraints.insets = new java.awt.Insets(5, 0, 0, 0);
        panSalData2.add(srpPaySettingDetails, gridBagConstraints);

        panChk2.setBorder(javax.swing.BorderFactory.createTitledBorder(""));
        panChk2.setMinimumSize(new java.awt.Dimension(106, 36));
        panChk2.setPreferredSize(new java.awt.Dimension(106, 36));
        panChk2.setLayout(new java.awt.GridBagLayout());

        chkSet.setText("Select All");
        chkSet.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                chkSetActionPerformed(evt);
            }
        });
        panChk2.add(chkSet, new java.awt.GridBagConstraints());

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTH;
        panSalData2.add(panChk2, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 0;
        panSalaryDetails.add(panSalData2, gridBagConstraints);

        panSalData1.setBorder(javax.swing.BorderFactory.createTitledBorder(""));
        panSalData1.setMinimumSize(new java.awt.Dimension(360, 250));
        panSalData1.setPreferredSize(new java.awt.Dimension(350, 600));
        panSalData1.setLayout(new java.awt.GridBagLayout());

        srpemployeeDetails.setMinimumSize(new java.awt.Dimension(350, 200));

        tblEmployeeDetails.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {},
                {},
                {},
                {}
            },
            new String [] {

            }
        ));
        srpemployeeDetails.setViewportView(tblEmployeeDetails);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.insets = new java.awt.Insets(5, 0, 0, 0);
        panSalData1.add(srpemployeeDetails, gridBagConstraints);

        panChk1.setBorder(javax.swing.BorderFactory.createTitledBorder(""));
        panChk1.setMinimumSize(new java.awt.Dimension(106, 36));
        panChk1.setPreferredSize(new java.awt.Dimension(106, 36));
        panChk1.setLayout(new java.awt.GridBagLayout());

        chkemp.setText("Select All");
        chkemp.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                chkempActionPerformed(evt);
            }
        });
        panChk1.add(chkemp, new java.awt.GridBagConstraints());

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        panSalData1.add(panChk1, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        panSalaryDetails.add(panSalData1, gridBagConstraints);

        panSalDat3.setBorder(javax.swing.BorderFactory.createTitledBorder(""));
        panSalDat3.setMinimumSize(new java.awt.Dimension(200, 200));
        panSalDat3.setPreferredSize(new java.awt.Dimension(300, 300));
        panSalDat3.setLayout(new java.awt.GridBagLayout());

        panSalaryData.setMinimumSize(new java.awt.Dimension(190, 125));
        panSalaryData.setName("");
        panSalaryData.setPreferredSize(new java.awt.Dimension(200, 125));
        panSalaryData.setLayout(new java.awt.GridBagLayout());

        cLabel2.setText("Salary Month");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        panSalaryData.add(cLabel2, gridBagConstraints);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHEAST;
        panSalaryData.add(tdtSalaryMonth, gridBagConstraints);

        cLabel3.setText("CurrentDate");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHEAST;
        panSalaryData.add(cLabel3, gridBagConstraints);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHEAST;
        panSalaryData.add(tdtCurrentDate, gridBagConstraints);

        cLabel4.setText("InterestDate");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        panSalaryData.add(cLabel4, gridBagConstraints);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        panSalaryData.add(tdtInterestDate, gridBagConstraints);

        panSalDat3.add(panSalaryData, new java.awt.GridBagConstraints());

        panSalData.setBorder(javax.swing.BorderFactory.createTitledBorder(""));
        panSalData.setMinimumSize(new java.awt.Dimension(200, 40));
        panSalData.setPreferredSize(new java.awt.Dimension(200, 40));
        panSalData.setLayout(new java.awt.GridBagLayout());

        cButton3.setText("Reject");
        cButton3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cButton3ActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        panSalData.add(cButton3, gridBagConstraints);

        btnAdd.setText("Process");
        btnAdd.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnAddActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        panSalData.add(btnAdd, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        panSalDat3.add(panSalData, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.insets = new java.awt.Insets(0, 6, 0, 0);
        panSalaryDetails.add(panSalDat3, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTH;
        panSalaryDate.add(panSalaryDetails, gridBagConstraints);

        panTblDetails.add(panSalaryDate);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        panSalaryProcessUI.add(panTblDetails, gridBagConstraints);

        panSalary.setBorder(javax.swing.BorderFactory.createBevelBorder(0));
        panSalary.setMinimumSize(new java.awt.Dimension(850, 250));
        panSalary.setPreferredSize(new java.awt.Dimension(850, 250));
        panSalary.setLayout(new java.awt.GridBagLayout());

        panSubmit.setBorder(javax.swing.BorderFactory.createTitledBorder(""));
        panSubmit.setMinimumSize(new java.awt.Dimension(700, 30));
        panSubmit.setPreferredSize(new java.awt.Dimension(700, 30));
        panSubmit.setLayout(new java.awt.GridBagLayout());

        cButton1.setText("Post");
        cButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cButton1ActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 0;
        panSubmit.add(cButton1, gridBagConstraints);

        cButton2.setText("Report");
        cButton2.setMaximumSize(new java.awt.Dimension(83, 27));
        cButton2.setMinimumSize(new java.awt.Dimension(83, 27));
        cButton2.setPreferredSize(new java.awt.Dimension(83, 27));
        cButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cButton2ActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        panSubmit.add(cButton2, gridBagConstraints);

        btnLeaveProcess.setText("LeaveProcess");
        btnLeaveProcess.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnLeaveProcessActionPerformed(evt);
            }
        });
        panSubmit.add(btnLeaveProcess, new java.awt.GridBagConstraints());

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.insets = new java.awt.Insets(7, 0, 0, 0);
        panSalary.add(panSubmit, gridBagConstraints);

        srpPayrollDetails.setMaximumSize(new java.awt.Dimension(50, 200));
        srpPayrollDetails.setMinimumSize(new java.awt.Dimension(50, 50));
        srpPayrollDetails.setPreferredSize(new java.awt.Dimension(800, 200));
        srpPayrollDetails.setRequestFocusEnabled(false);

        tblpayrollDetails.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {},
                {},
                {},
                {}
            },
            new String [] {

            }
        ));
        tblpayrollDetails.setMaximumSize(new java.awt.Dimension(700, 8000));
        tblpayrollDetails.setMinimumSize(new java.awt.Dimension(700, 2000));
        tblpayrollDetails.setName("");
        tblpayrollDetails.setPreferredScrollableViewportSize(new java.awt.Dimension(700, 8000));
        tblpayrollDetails.setPreferredSize(new java.awt.Dimension(700, 8000));
        srpPayrollDetails.setViewportView(tblpayrollDetails);

        panSalary.add(srpPayrollDetails, new java.awt.GridBagConstraints());

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(18, 0, 0, 0);
        panSalaryProcessUI.add(panSalary, gridBagConstraints);

        getContentPane().add(panSalaryProcessUI, java.awt.BorderLayout.CENTER);

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

        mbrSalaryProcess.add(mnuProcess);

        setJMenuBar(mbrSalaryProcess);

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btnCloseActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCloseActionPerformed
        // TODO add your handling code here:
        cifClosingAlert();
    }//GEN-LAST:event_btnCloseActionPerformed

    private void btnCancelActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCancelActionPerformed
        // TODO add your handling code here:
        btnSave.setEnabled(false);
        btnEdit.setEnabled(false);
        btnDelete.setEnabled(false);
        btnException.setEnabled(false);
        btnPrint.setEnabled(false);
        resetLables();
        ClientUtil.enableDisable(this, false);  //__ Disables the panel...
        setButtonEnableDisable();               //__ Enables or Disables the buttons and menu Items depending on their previous state...
        setEnableDisableButtons(false);
        btnAuthorize.setEnabled(true);
        btnReject.setEnabled(true);
        btnException.setEnabled(true);
        viewType = -1;
        resetTransactionUI();
        //__ Make the Screen Closable..
        setModified(false);
        setRadioButtons();
    }//GEN-LAST:event_btnCancelActionPerformed

    private void displayAlert(String message) {
        CMandatoryDialog cmd = new CMandatoryDialog();
        cmd.setMessage(message);
        cmd.show();
    }

    private void resetTransactionUI() {
    }

    // this method is called automatically from ViewAll...
    public void fillData(Object param) {
    }

    private void mitCloseActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mitCloseActionPerformed
        // TODO add your handling code here:
        btnCloseActionPerformed(null);
    }//GEN-LAST:event_mitCloseActionPerformed

    private void mitPrintActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mitPrintActionPerformed
        // TODO add your handling code here:
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

    /**
     * Exit the Application
     */
    private void exitForm(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_exitForm
        System.exit(0);
    }//GEN-LAST:event_exitForm

    private void btnRejectActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnRejectActionPerformed
        // TODO add your handling code here
        authorizeStatus(CommonConstants.STATUS_REJECTED);
        btnEdit.setEnabled(false);
        btnNew.setEnabled(false);
        btnDelete.setEnabled(false);
        btnCancel.setEnabled(true);
        btnReject.setEnabled(true);
        setRadioButtons();
    }//GEN-LAST:event_btnRejectActionPerformed

    public void authorizeStatus(String authorizeStatus) {
    }

    public void authorize(HashMap map) {
    }
    //__ To avaoid the looping of the testCase...
    boolean isTest = false;

    private void cButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cButton1ActionPerformed

        if (tblpayrollDetails.getRowCount() > 0) {
            CommonUtil comm = new CommonUtil();
            final JDialog loading = comm.addProgressBar();
            SwingWorker<Void, Void> worker = new SwingWorker<Void, Void>() {

                @Override
                protected Void doInBackground() throws InterruptedException {
                    observable.setPayrollId(CommonUtil.convertObjToStr(tblpayrollDetails.getValueAt(0, 10)));
                    Date salDate = DateUtil.getDateMMDDYYYY(tdtSalaryMonth.getDateValue());
                    String salMonth = getMonthName(salDate.getMonth()+1)+" "+(salDate.getYear()+1900);                    
                    observable.setSalaryMonth(salMonth);                  
                    observable.doAction("PROCESS");
                    HashMap wherePayroll = new HashMap();
                    populatepayrollData(wherePayroll);
                    salMonth = "";
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
        } else {
            ClientUtil.showMessageWindow("First post on before process");
        }
    }//GEN-LAST:event_cButton1ActionPerformed

    String getMonthName(int monthNumber) {
        String[] months = new DateFormatSymbols().getMonths();
        int n = monthNumber - 1;
        return (n >= 0 && n <= 11) ? months[n] : "wrong number";
    }
    

    private void savePerformed() {
    }

    private void setbtndisabled() {
    }

    private void clear() {
    }

    private void btnExceptionActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnExceptionActionPerformed
        // TODO add your handling code here:
        authorizeStatus(CommonConstants.STATUS_AUTHORIZED);
        btnEdit.setEnabled(false);
        btnNew.setEnabled(false);
        btnDelete.setEnabled(false);
        btnCancel.setEnabled(true);
    }//GEN-LAST:event_btnExceptionActionPerformed

    private void btnPrintActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnPrintActionPerformed
        // TODO add your handling code here:
        ReportUI reportUi=new ReportUI(this);
        reportUi.show();
//        HashMap reportParamMap = new HashMap();
//        com.see.truetransact.clientutil.ttrintegration.LinkReport.getReports(getScreenID(), reportParamMap);
    }//GEN-LAST:event_btnPrintActionPerformed

    private void btnNewActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnNewActionPerformed
        // TODO add your handling code here:
        ClientUtil.enableDisable(this, true);
        populateData();
        populateSettingData();
        tdtCurrentDate.setDateValue(CommonUtil.convertObjToStr(currDt));
        tdtInterestDate.setDateValue(CommonUtil.convertObjToStr(currDt));
        Date dt = (Date) currDt.clone();
        dt.setDate(1);
        tdtSalaryMonth.setDateValue(CommonUtil.convertObjToStr(dt));
        HashMap whereMap = new HashMap();
        populatepayrollData(whereMap);
        btnCancel.setEnabled(true);
        btnPrint.setEnabled(true);
    }//GEN-LAST:event_btnNewActionPerformed

    private void btnEditActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnEditActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_btnEditActionPerformed

    private void btnDeleteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnDeleteActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_btnDeleteActionPerformed

    private void btnSaveActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSaveActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_btnSaveActionPerformed

    private void btnAddActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAddActionPerformed
      
        List netPaySalaryAccount = ClientUtil.executeQuery("getClosedNetPayAccount", null);
        if (netPaySalaryAccount != null && netPaySalaryAccount.size() > 0) {
            HashMap resultMap = new HashMap();
            StringBuffer alertMessage = new StringBuffer();
            String message = "Closed Net Pay Salary Account Are :";
            alertMessage.append(message + "\n");
            for (int i = 0; i < netPaySalaryAccount.size(); i++) {
                resultMap = (HashMap) netPaySalaryAccount.get(i);
                String resultData = CommonUtil.convertObjToStr(resultMap.get("ACT_NUM")) + " EmployeeId:" + CommonUtil.convertObjToStr(resultMap.get("EMPLOYEE_ID"));
                alertMessage.append(resultData + "\n");
            }
            displayAlert(alertMessage.toString());
            return;
        }
        int empcount = 0;
        int paycount = 0;
        HashMap newEmpMap;
        HashMap newPayMap;
        ArrayList EmpList = new ArrayList();
        ArrayList payList = new ArrayList();
        ArrayList PayCodeList = new ArrayList();
        int emprowscnt = tblEmployeeDetails.getRowCount();
        ArrayList EmpArrayLst = new ArrayList();
        ArrayList payArrayList = new ArrayList();
        String empid = "";
        String paycode = "";
        empcount = tblEmployeeDetails.getSelectedRowCount();
        paycount = tblPaySettingDetails.getSelectedRowCount();
        int empselectcount = 0;
        int payselectcount = 0;
        if (emprowscnt > 0) {
            for (int i = 0; i < emprowscnt; i++) {
                String isSelected = CommonUtil.convertObjToStr(tblEmployeeDetails.getValueAt(i, 0));
                if (isSelected.equalsIgnoreCase("TRUE")) {
                    empselectcount++;
                    newEmpMap = new HashMap();
                    newEmpMap.put("EMP", CommonUtil.convertObjToStr(tblEmployeeDetails.getValueAt(i, 1)));
                    if (empselectcount == 1) {
                        empid = "'" + CommonUtil.convertObjToStr(tblEmployeeDetails.getValueAt(i, 1)) + "'";
                    } else {
                        empid += "," + "'" + CommonUtil.convertObjToStr(tblEmployeeDetails.getValueAt(i, 1)) + "'";
                    }
                    EmpArrayLst.add(newEmpMap);
                }
            }
        }
        if (empselectcount == 0) {
            ClientUtil.showMessageWindow("please select at least one Employee");
            return;
        }
        int payRowCnt = tblPaySettingDetails.getRowCount();
        if (payRowCnt > 0) {
            for (int i = 0; i < payRowCnt; i++) {
                String isSelected = CommonUtil.convertObjToStr(tblPaySettingDetails.getValueAt(i, 0));
                if (isSelected.equalsIgnoreCase("TRUE")) {
                    payselectcount++;
                    newPayMap = new HashMap();
                    newPayMap.put("paycode", CommonUtil.convertObjToStr(tblPaySettingDetails.getValueAt(i, 1)));
                    if (payselectcount == 1) {
                        paycode = "'" + CommonUtil.convertObjToStr(tblPaySettingDetails.getValueAt(i, 1)) + "'";
                    } else {
                        paycode += "," + "'" + CommonUtil.convertObjToStr(tblPaySettingDetails.getValueAt(i, 1)) + "'";
                    }
                    payList.add(newPayMap);
                }
            }
        }
        if (payselectcount == 0) {
            ClientUtil.showMessageWindow("please select at least one Paycode");
            return;
        }
        HashMap payrollmap = new HashMap();
        payrollmap.put("EMPID", empid);
        payrollmap.put("paycode", paycode);
        payrollmap.put("salaryMonth", tdtSalaryMonth.getDateValue());
        List nonGlClosedList = getNonGlClosedAccounts(payrollmap);
       if (nonGlClosedList != null && nonGlClosedList.size() > 0) {
            HashMap resultMap = new HashMap();
            StringBuffer alertMessage = new StringBuffer();
            String message = "Closed Non GL Account Are :";
            alertMessage.append(message + "\n");
            for (int i = 0; i < nonGlClosedList.size(); i++) {
                resultMap = (HashMap) nonGlClosedList.get(i);
                String resultData = CommonUtil.convertObjToStr(resultMap.get("ACC_NO")) + " EmployeeId:" + CommonUtil.convertObjToStr(resultMap.get("EMPLOYEEID"))+" Pay Code:" + CommonUtil.convertObjToStr(resultMap.get("PAY_CODE"));
                alertMessage.append(resultData + "\n");
            }
            displayAlert(alertMessage.toString());
            return;
        }
        observable.setPayrollMap(payrollmap);
        CommonUtil comm = new CommonUtil();
        final JDialog loading = comm.addProgressBar();
        SwingWorker<Void, Void> worker = new SwingWorker<Void, Void>() {
            @Override
            protected Void doInBackground() throws InterruptedException {
                observable.doAction("POST");
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
        if (observable.getProxyReturnMap() != null && observable.getProxyReturnMap().containsKey("PAYROLL_ID")) {
            String payrollid = CommonUtil.convertObjToStr(observable.getProxyReturnMap().get("PAYROLL_ID"));
            HashMap wherePayroll = new HashMap();
            wherePayroll.put("PAYROLL_ID", payrollid);
            populatepayrollData(wherePayroll);
            populateData();
            populateSettingData();
            chkemp.setSelected(false);
            chkSet.setSelected(false);
        }
    }//GEN-LAST:event_btnAddActionPerformed
    public List getNonGlClosedAccounts(HashMap whereDudMap) {
        List lis;
        lis = ClientUtil.executeQuery("GetNonGLClosedAccounts", whereDudMap);
        return lis;
    }

    private void chkempActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_chkempActionPerformed
        if (chkemp.isSelected() == true) {
            for (int i = 0; i < tblEmployeeDetails.getRowCount(); i++) {
                tblEmployeeDetails.setValueAt(new Boolean(true), i, 0);
            }
        } else if (chkemp.isSelected() == false) {
            for (int i = 0; i < tblEmployeeDetails.getRowCount(); i++) {
                tblEmployeeDetails.setValueAt(new Boolean(false), i, 0);
            }
        }
        // TODO add your handling code here:
    }//GEN-LAST:event_chkempActionPerformed

    private void chkSetActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_chkSetActionPerformed

        if (chkSet.isSelected() == true) {
            for (int i = 0; i < tblPaySettingDetails.getRowCount(); i++) {
                tblPaySettingDetails.setValueAt(new Boolean(true), i, 0);
            }
        } else if (chkSet.isSelected() == false) {
            for (int i = 0; i < tblPaySettingDetails.getRowCount(); i++) {
                tblPaySettingDetails.setValueAt(new Boolean(false), i, 0);
            }
        }
    }//GEN-LAST:event_chkSetActionPerformed

    private void cButton3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cButton3ActionPerformed
        if (tblpayrollDetails.getRowCount() > 0) {
            String payrollid = CommonUtil.convertObjToStr(tblpayrollDetails.getValueAt(0, 10));
            HashMap whereMap = new HashMap();
            whereMap.put("PAYROLL_ID", payrollid);
            ClientUtil.execute("rejectpayrollPost", whereMap);
            populatepayrollData(whereMap);
        } else {
            ClientUtil.showMessageWindow("NO Data found TO Reject");
        }
        // TODO add your handling code here:
    }//GEN-LAST:event_cButton3ActionPerformed

    private void cButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cButton2ActionPerformed
        MyReportViewer myReport = new MyReportViewer();
        String payrollId = "";
        if (tblpayrollDetails.getRowCount() > 0) {
            Date tblDate = (Date) tblpayrollDetails.getValueAt(0, 9);
            DateFormat df = new SimpleDateFormat("MMM yyyy");
            String reportDate = df.format(tblDate);
            myReport.setSalaryDate(reportDate);
            payrollId = CommonUtil.convertObjToStr(tblpayrollDetails.getValueAt(0, 10));
        }
        if (payrollId != null) {
            String reportCheck = myReport.acquitenceReport(payrollId, "salaryProcess");
            if (reportCheck == null || reportCheck.equals("")) {
                ClientUtil.showMessageWindow("The document has no pages!!!");
            } else {
                myReport.openURL("file:///" + CommonConstants.SERVER_PATH + "/report/processreport.html");
            }
        } else {
            ClientUtil.showMessageWindow("The document has no pages!!!");
        }
    }//GEN-LAST:event_cButton2ActionPerformed

    private void btnLeaveProcessActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnLeaveProcessActionPerformed
       if (tblpayrollDetails.getRowCount() > 0) {
        HashMap whereMap=new HashMap();
        btnLeaveProcess.setEnabled(false);
        whereMap.put("SalaryMonth", DateUtil.getDateMMDDYYYY(CommonUtil.convertObjToStr(tblpayrollDetails.getValueAt(0, 9))));
        ClientUtil.execute("callParoll_Leave_Process",whereMap);
          HashMap whereMap1 = new HashMap();
        populatepayrollData(whereMap1);
       }
       // TODO add your handling code here:
    }//GEN-LAST:event_btnLeaveProcessActionPerformed

    private void setEnableDisableButtons(boolean value) {
    }

    private void resetLables() {
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {

        new SalaryProcessUI().show();
    }
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private com.see.truetransact.uicomponent.CButton btnAdd;
    private com.see.truetransact.uicomponent.CButton btnAuthorize;
    private com.see.truetransact.uicomponent.CButton btnCancel;
    private com.see.truetransact.uicomponent.CButton btnClose;
    private com.see.truetransact.uicomponent.CButton btnDelete;
    private com.see.truetransact.uicomponent.CButton btnEdit;
    private com.see.truetransact.uicomponent.CButton btnException;
    private com.see.truetransact.uicomponent.CButton btnLeaveProcess;
    private com.see.truetransact.uicomponent.CButton btnNew;
    private com.see.truetransact.uicomponent.CButton btnPrint;
    private com.see.truetransact.uicomponent.CButton btnReject;
    private com.see.truetransact.uicomponent.CButton btnSave;
    private com.see.truetransact.uicomponent.CButton cButton1;
    private com.see.truetransact.uicomponent.CButton cButton2;
    private com.see.truetransact.uicomponent.CButton cButton3;
    private com.see.truetransact.uicomponent.CLabel cLabel2;
    private com.see.truetransact.uicomponent.CLabel cLabel3;
    private com.see.truetransact.uicomponent.CLabel cLabel4;
    private com.see.truetransact.uicomponent.CCheckBox chkSet;
    private com.see.truetransact.uicomponent.CCheckBox chkemp;
    private com.see.truetransact.uicomponent.CLabel lblMsg;
    private com.see.truetransact.uicomponent.CLabel lblSpace;
    private com.see.truetransact.uicomponent.CLabel lblSpace17;
    private com.see.truetransact.uicomponent.CLabel lblSpace18;
    private com.see.truetransact.uicomponent.CLabel lblSpace19;
    private com.see.truetransact.uicomponent.CLabel lblSpace2;
    private com.see.truetransact.uicomponent.CLabel lblSpace20;
    private com.see.truetransact.uicomponent.CLabel lblSpace21;
    private com.see.truetransact.uicomponent.CLabel lblSpace22;
    private com.see.truetransact.uicomponent.CLabel lblSpace3;
    private com.see.truetransact.uicomponent.CLabel lblSpace4;
    private com.see.truetransact.uicomponent.CLabel lblStatus;
    private com.see.truetransact.uicomponent.CMenuBar mbrSalaryProcess;
    private javax.swing.JMenuItem mitCancel;
    private javax.swing.JMenuItem mitClose;
    private javax.swing.JMenuItem mitDelete;
    private javax.swing.JMenuItem mitEdit;
    private javax.swing.JMenuItem mitNew;
    private javax.swing.JMenuItem mitPrint;
    private javax.swing.JMenuItem mitSave;
    private javax.swing.JMenu mnuProcess;
    private com.see.truetransact.uicomponent.CPanel panChk1;
    private com.see.truetransact.uicomponent.CPanel panChk2;
    private com.see.truetransact.uicomponent.CPanel panSalDat3;
    private com.see.truetransact.uicomponent.CPanel panSalData;
    private com.see.truetransact.uicomponent.CPanel panSalData1;
    private com.see.truetransact.uicomponent.CPanel panSalData2;
    private com.see.truetransact.uicomponent.CPanel panSalary;
    private com.see.truetransact.uicomponent.CPanel panSalaryData;
    private com.see.truetransact.uicomponent.CPanel panSalaryDate;
    private com.see.truetransact.uicomponent.CPanel panSalaryDetails;
    private com.see.truetransact.uicomponent.CPanel panSalaryProcessUI;
    private com.see.truetransact.uicomponent.CPanel panStatus;
    private com.see.truetransact.uicomponent.CPanel panSubmit;
    private com.see.truetransact.uicomponent.CPanel panTblDetails;
    private javax.swing.JSeparator sptCancel;
    private javax.swing.JSeparator sptPrint;
    private javax.swing.JSeparator sptProcess;
    private com.see.truetransact.uicomponent.CScrollPane srpPaySettingDetails;
    private javax.swing.JScrollPane srpPayrollDetails;
    private com.see.truetransact.uicomponent.CScrollPane srpemployeeDetails;
    private com.see.truetransact.uicomponent.CTable tblEmployeeDetails;
    private com.see.truetransact.uicomponent.CTable tblPaySettingDetails;
    private com.see.truetransact.uicomponent.CTable tblpayrollDetails;
    private javax.swing.JToolBar tbrPfMaster;
    private com.see.truetransact.uicomponent.CDateField tdtCurrentDate;
    private com.see.truetransact.uicomponent.CDateField tdtInterestDate;
    private com.see.truetransact.uicomponent.CDateField tdtSalaryMonth;
    // End of variables declaration//GEN-END:variables
}
