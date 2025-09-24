/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition. 
 * 
 * LockerRentSIApplicationUI.java
 *
 * Created on October 10th, 2011, 11:03 PM
 */

package com.see.truetransact.ui.locker.lockerSI;

/**
 *
 * @author
 */
import java.util.*;
import com.see.truetransact.commonutil.CommonUtil;
import com.see.truetransact.clientutil.ClientUtil;
import com.see.truetransact.commonutil.TTException;
import com.see.truetransact.ui.common.viewall.ViewAll;
import com.see.truetransact.uicomponent.CInternalFrame;
import com.see.truetransact.clientutil.ClientConstants;
import com.see.truetransact.uimandatory.MandatoryCheck;
import com.see.truetransact.commonutil.CommonConstants;
import com.see.truetransact.clientproxy.ProxyParameters;
import com.see.truetransact.clientutil.CMandatoryDialog;
import com.see.truetransact.uimandatory.UIMandatoryField;
import com.see.truetransact.uivalidation.CurrencyValidation;
import com.see.truetransact.clientutil.EnhancedTableModel;
import com.see.truetransact.clientexception.ClientParseException;
import com.see.truetransact.ui.customer.CheckCustomerIdUI;
import com.see.truetransact.clientutil.ttrintegration.TTIntegration;
import com.see.truetransact.uicomponent.COptionPane;
import com.see.truetransact.clientutil.ComboBoxModel;
import com.see.truetransact.ui.TrueTransactMain;
import com.see.truetransact.commonutil.DateUtil;
import com.see.truetransact.uivalidation.NumericValidation;
import com.see.truetransact.clientproxy.ProxyFactory;
import java.util.Observable;
import com.see.truetransact.transferobject.common.transaction.TransactionTO;
import com.see.truetransact.ui.common.servicetax.ServiceTaxCalculation;

import com.see.truetransact.uicomponent.CButtonGroup;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.text.DateFormat;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JDialog;
import javax.swing.SwingWorker;


public class LockerRentSIApplicationUI extends CInternalFrame implements Observer{
    
    /** Vairable Declarations */
    private final static ClientParseException parseException = ClientParseException.getInstance();//Instance of ClientParseException to log the Exceptions
    private String viewType = "";//Variable used to store ActionType(New,Edit,Delete)
    private boolean selectMode = false;
    private ProxyFactory proxy;
    private TransactionTO to;
    private Date currDate = null;
    private boolean isFilled = false;
    private HashMap returnMap = null;
    private String surrenderID = "";
    private LockerRentSIApplicationOB observable;
    private String expDt = "";
    private Date expiryDate = null;
    private int commision;
    private int Penal_rate;
    private int no_of_days;
    CButtonGroup btngroup = new CButtonGroup();
    //ButtonGroup btngroup = new ButtonGroup();
     boolean flag=false;
    public LockerRentSIApplicationUI() {
        returnMap = null;
        currDate = ClientUtil.getCurrentDate();
        initForm();
    }
    
    /** Method which is used to initialize the form TokenConfig */
    private void initForm(){
        initComponents();
        setObservable();
        initComponentData();
        setMaxLengths();
        btngroup.add(rdoCash);
        btngroup.add(rdoTransfer);
    }
    
    private void initComponentData() {
        cboLockerType.setModel(observable.getCboLocType());
        tblLockerRentSIApplication.setModel(observable.getTblLockerRentSIApplication());
    }
    
    private void setObservable() {
        try {
            observable = new LockerRentSIApplicationOB();
            observable.addObserver(this);
        } catch (Exception e) {
            e.printStackTrace();
        }
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
        
        
    }
    
    
/* Auto Generated Method - setMandatoryHashMap()
 
ADD: implements com.see.truetransact.uimandatory.UIMandatoryField
 
   This method list out all the Input Fields available in the UI.
   It needs a class level HashMap variable mandatoryMap. */
    public void setMandatoryHashMap() {
        
    }
    
    
    /** Used to set Maximum possible lenghts for TextFields */
    private void setMaxLengths(){
        txtRentDueAsOnMM.setMaxLength(2);
        txtRentDueAsOnMM.setValidation(new NumericValidation());
        txtRentDueAsOnyyyy.setMaxLength(4);
        txtRentDueAsOnyyyy.setValidation(new NumericValidation());
        
    }
    
    
    /** Method used to Give a Alert when any Mandatory Field is not filled by the user */
    private void displayAlert(String message){
        CMandatoryDialog cmd = new CMandatoryDialog();
        cmd.setMessage(message);
        cmd.setModal(true);
        cmd.show();
    }
    
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {
        java.awt.GridBagConstraints gridBagConstraints;

        panDepositInterestApplication = new com.see.truetransact.uicomponent.CPanel();
        panProductDetails = new com.see.truetransact.uicomponent.CPanel();
        lblLockerType = new com.see.truetransact.uicomponent.CLabel();
        panProductID = new com.see.truetransact.uicomponent.CPanel();
        lblProductName = new com.see.truetransact.uicomponent.CLabel();
        panTransType = new com.see.truetransact.uicomponent.CPanel();
        lblRentDueAsOn = new com.see.truetransact.uicomponent.CLabel();
        lblCustName = new com.see.truetransact.uicomponent.CLabel();
        panCustID = new com.see.truetransact.uicomponent.CPanel();
        panSINumber = new com.see.truetransact.uicomponent.CPanel();
        cboLockerType = new com.see.truetransact.uicomponent.CComboBox();
        panOperations1 = new com.see.truetransact.uicomponent.CPanel();
        txtRentDueAsOnMM = new com.see.truetransact.uicomponent.CTextField();
        txtRentDueAsOnyyyy = new com.see.truetransact.uicomponent.CTextField();
        btnProcess1 = new com.see.truetransact.uicomponent.CButton();
        btnClear1 = new com.see.truetransact.uicomponent.CButton();
        lblRentDueAsOn1 = new com.see.truetransact.uicomponent.CLabel();
        rdoTransfer = new com.see.truetransact.uicomponent.CRadioButton();
        rdoCash = new com.see.truetransact.uicomponent.CRadioButton();
        chkFdToLocker = new javax.swing.JCheckBox();
        panProductTableData = new com.see.truetransact.uicomponent.CPanel();
        srpLockerRentSIApplication = new com.see.truetransact.uicomponent.CScrollPane();
        tblLockerRentSIApplication = new com.see.truetransact.uicomponent.CTable();
        panSelectAll = new com.see.truetransact.uicomponent.CPanel();
        lblSelectAll = new com.see.truetransact.uicomponent.CLabel();
        chkSelectAll = new com.see.truetransact.uicomponent.CCheckBox();
        panProcess = new com.see.truetransact.uicomponent.CPanel();
        btnClose = new com.see.truetransact.uicomponent.CButton();
        btnProcess = new com.see.truetransact.uicomponent.CButton();
        lblTotalTransactionAmtVal = new com.see.truetransact.uicomponent.CLabel();
        panStatus = new com.see.truetransact.uicomponent.CPanel();
        lblSpace1 = new com.see.truetransact.uicomponent.CLabel();
        lblStatus = new com.see.truetransact.uicomponent.CLabel();
        lblMsg = new com.see.truetransact.uicomponent.CLabel();

        setClosable(true);
        setIconifiable(true);
        setMaximizable(true);
        setResizable(true);
        setMaximumSize(new java.awt.Dimension(960, 620));
        setMinimumSize(new java.awt.Dimension(960, 620));
        setPreferredSize(new java.awt.Dimension(960, 620));

        panDepositInterestApplication.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        panDepositInterestApplication.setMaximumSize(new java.awt.Dimension(950, 450));
        panDepositInterestApplication.setMinimumSize(new java.awt.Dimension(860, 620));
        panDepositInterestApplication.setName(""); // NOI18N
        panDepositInterestApplication.setPreferredSize(new java.awt.Dimension(860, 620));
        panDepositInterestApplication.setLayout(new java.awt.GridBagLayout());

        panProductDetails.setBorder(javax.swing.BorderFactory.createTitledBorder("Locker Details"));
        panProductDetails.setMinimumSize(new java.awt.Dimension(830, 110));
        panProductDetails.setPreferredSize(new java.awt.Dimension(830, 110));

        lblLockerType.setText("Locker_Type");

        panProductID.setLayout(new java.awt.GridBagLayout());

        panTransType.setMinimumSize(new java.awt.Dimension(130, 25));
        panTransType.setPreferredSize(new java.awt.Dimension(130, 25));
        panTransType.setLayout(new java.awt.GridBagLayout());

        lblRentDueAsOn.setText("Rent Due As On");

        lblCustName.setForeground(new java.awt.Color(0, 51, 204));
        lblCustName.setFont(new java.awt.Font("MS Sans Serif", 1, 13)); // NOI18N

        panCustID.setLayout(new java.awt.GridBagLayout());

        panSINumber.setLayout(new java.awt.GridBagLayout());

        cboLockerType.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "" }));

        panOperations1.setLayout(new java.awt.GridBagLayout());

        txtRentDueAsOnMM.setPreferredSize(new java.awt.Dimension(30, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 12;
        panOperations1.add(txtRentDueAsOnMM, gridBagConstraints);

        txtRentDueAsOnyyyy.setPreferredSize(new java.awt.Dimension(60, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 12;
        panOperations1.add(txtRentDueAsOnyyyy, gridBagConstraints);

        btnProcess1.setText("Display");
        btnProcess1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnProcess1ActionPerformed(evt);
            }
        });

        btnClear1.setText("Clear");
        btnClear1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnClear1ActionPerformed(evt);
            }
        });

        lblRentDueAsOn1.setText("MM YYYY");

        rdoTransfer.setText("Transfer");

        rdoCash.setText("Cash");
        rdoCash.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                rdoCashActionPerformed(evt);
            }
        });

        chkFdToLocker.setText("FD To Locker");
        chkFdToLocker.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                chkFdToLockerActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout panProductDetailsLayout = new javax.swing.GroupLayout(panProductDetails);
        panProductDetails.setLayout(panProductDetailsLayout);
        panProductDetailsLayout.setHorizontalGroup(
            panProductDetailsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panProductDetailsLayout.createSequentialGroup()
                .addGroup(panProductDetailsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(panCustID, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(panSINumber, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lblProductName, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(panProductID, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lblCustName, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(panProductDetailsLayout.createSequentialGroup()
                        .addGap(124, 124, 124)
                        .addComponent(panTransType, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(panProductDetailsLayout.createSequentialGroup()
                        .addGroup(panProductDetailsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(panProductDetailsLayout.createSequentialGroup()
                                .addGap(32, 32, 32)
                                .addComponent(chkFdToLocker, javax.swing.GroupLayout.PREFERRED_SIZE, 103, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(48, 48, 48))
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, panProductDetailsLayout.createSequentialGroup()
                                .addContainerGap()
                                .addComponent(rdoCash, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)))
                        .addGroup(panProductDetailsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addGroup(panProductDetailsLayout.createSequentialGroup()
                                .addComponent(lblLockerType, javax.swing.GroupLayout.PREFERRED_SIZE, 94, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(cboLockerType, javax.swing.GroupLayout.PREFERRED_SIZE, 97, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(18, 18, 18)
                                .addComponent(lblRentDueAsOn, javax.swing.GroupLayout.PREFERRED_SIZE, 101, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(panProductDetailsLayout.createSequentialGroup()
                                .addComponent(rdoTransfer, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(btnProcess1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(45, 45, 45)
                                .addComponent(btnClear1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(panOperations1, javax.swing.GroupLayout.PREFERRED_SIZE, 124, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(lblRentDueAsOn1, javax.swing.GroupLayout.PREFERRED_SIZE, 68, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(113, Short.MAX_VALUE))
        );
        panProductDetailsLayout.setVerticalGroup(
            panProductDetailsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panProductDetailsLayout.createSequentialGroup()
                .addGroup(panProductDetailsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(panCustID, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(panSINumber, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lblProductName, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(panProductID, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lblCustName, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGroup(panProductDetailsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(panProductDetailsLayout.createSequentialGroup()
                        .addGap(20, 20, 20)
                        .addComponent(chkFdToLocker))
                    .addGroup(panProductDetailsLayout.createSequentialGroup()
                        .addGap(11, 11, 11)
                        .addGroup(panProductDetailsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(cboLockerType, javax.swing.GroupLayout.PREFERRED_SIZE, 19, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(lblRentDueAsOn, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(lblLockerType, javax.swing.GroupLayout.PREFERRED_SIZE, 36, javax.swing.GroupLayout.PREFERRED_SIZE))))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(panProductDetailsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(panProductDetailsLayout.createSequentialGroup()
                        .addGroup(panProductDetailsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(rdoTransfer, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(rdoCash, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(panTransType, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(btnProcess1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnClear1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
            .addGroup(panProductDetailsLayout.createSequentialGroup()
                .addGap(22, 22, 22)
                .addComponent(lblRentDueAsOn1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
            .addGroup(panProductDetailsLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(panOperations1, javax.swing.GroupLayout.PREFERRED_SIZE, 38, javax.swing.GroupLayout.PREFERRED_SIZE))
        );

        panDepositInterestApplication.add(panProductDetails, new java.awt.GridBagConstraints());

        panProductTableData.setBorder(javax.swing.BorderFactory.createTitledBorder("Locker Details"));
        panProductTableData.setMinimumSize(new java.awt.Dimension(830, 360));
        panProductTableData.setPreferredSize(new java.awt.Dimension(830, 360));
        panProductTableData.setLayout(new java.awt.GridBagLayout());

        srpLockerRentSIApplication.setMinimumSize(new java.awt.Dimension(810, 300));
        srpLockerRentSIApplication.setPreferredSize(new java.awt.Dimension(919, 300));

        tblLockerRentSIApplication.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Select", "LocNo", "Name", "Exp Date", "Rent", "Service Tax", "ProdType", "Title 8", "AvailbleBalance"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.Boolean.class, java.lang.Object.class, java.lang.Object.class, java.lang.Object.class, java.lang.Object.class, java.lang.Object.class, java.lang.Object.class, java.lang.Object.class, java.lang.Object.class
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }
        });
        tblLockerRentSIApplication.setAutoResizeMode(javax.swing.JTable.AUTO_RESIZE_OFF);
        tblLockerRentSIApplication.setMaximumSize(new java.awt.Dimension(2147483647, 2147483647));
        tblLockerRentSIApplication.setMinimumSize(new java.awt.Dimension(180, 400));
        tblLockerRentSIApplication.setPreferredScrollableViewportSize(new java.awt.Dimension(900, 331));
        tblLockerRentSIApplication.setPreferredSize(new java.awt.Dimension(900, 3000));
        tblLockerRentSIApplication.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tblLockerRentSIApplicationMouseClicked(evt);
            }
        });
        srpLockerRentSIApplication.setViewportView(tblLockerRentSIApplication);

        panProductTableData.add(srpLockerRentSIApplication, new java.awt.GridBagConstraints());

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.VERTICAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(1, 4, 1, 1);
        panDepositInterestApplication.add(panProductTableData, gridBagConstraints);
        panProductTableData.getAccessibleContext().setAccessibleDescription("");

        panSelectAll.setMinimumSize(new java.awt.Dimension(101, 27));
        panSelectAll.setPreferredSize(new java.awt.Dimension(101, 27));
        panSelectAll.setLayout(new java.awt.GridBagLayout());

        lblSelectAll.setText("Select All");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(1, 19, 5, 0);
        panSelectAll.add(lblSelectAll, gridBagConstraints);

        chkSelectAll.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                chkSelectAllActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(1, 4, 5, 1);
        panSelectAll.add(chkSelectAll, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 62, 0, 62);
        panDepositInterestApplication.add(panSelectAll, gridBagConstraints);

        panProcess.setMinimumSize(new java.awt.Dimension(780, 30));
        panProcess.setPreferredSize(new java.awt.Dimension(780, 30));
        panProcess.setLayout(new java.awt.GridBagLayout());

        btnClose.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_CLOSE.gif"))); // NOI18N
        btnClose.setText("Close");
        btnClose.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCloseActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panProcess.add(btnClose, gridBagConstraints);

        btnProcess.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_AUTHORIZE.gif"))); // NOI18N
        btnProcess.setText("PROCESS");
        btnProcess.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnProcessActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.insets = new java.awt.Insets(4, 120, 4, 4);
        panProcess.add(btnProcess, gridBagConstraints);

        lblTotalTransactionAmtVal.setMinimumSize(new java.awt.Dimension(100, 21));
        lblTotalTransactionAmtVal.setPreferredSize(new java.awt.Dimension(100, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 6;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        panProcess.add(lblTotalTransactionAmtVal, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 4, 0, 0);
        panDepositInterestApplication.add(panProcess, gridBagConstraints);

        getContentPane().add(panDepositInterestApplication, java.awt.BorderLayout.CENTER);

        panStatus.setLayout(new java.awt.GridBagLayout());

        lblSpace1.setText(" Status :");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.fill = java.awt.GridBagConstraints.VERTICAL;
        gridBagConstraints.weighty = 1.0;
        panStatus.add(lblSpace1, gridBagConstraints);

        lblStatus.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        lblStatus.setText("                      ");
        panStatus.add(lblStatus, new java.awt.GridBagConstraints());
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        panStatus.add(lblMsg, gridBagConstraints);

        getContentPane().add(panStatus, java.awt.BorderLayout.SOUTH);

        pack();
    }// </editor-fold>//GEN-END:initComponents
    
    private void tblLockerRentSIApplicationMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tblLockerRentSIApplicationMouseClicked
        // TODO add your handling code here:
        boolean flag=false;
        double gst = 0.0;
        if(!chkFdToLocker.isSelected()){
            gst = CommonUtil.convertObjToDouble(tblLockerRentSIApplication.getValueAt(tblLockerRentSIApplication.getSelectedRow(),11));
        }
        
        if(selectMode == true){
            String st=CommonUtil.convertObjToStr(tblLockerRentSIApplication.getValueAt(tblLockerRentSIApplication.getSelectedRow(),0));
            if(st.equals("true")){
                tblLockerRentSIApplication.setValueAt(new Boolean(false),tblLockerRentSIApplication.getSelectedRow(),0);
            }else{
                tblLockerRentSIApplication.setValueAt(new Boolean(true),tblLockerRentSIApplication.getSelectedRow(),0);
            }
        }
        //Added by sreekrishnan for balance validation
        double total = CommonUtil.convertObjToDouble(tblLockerRentSIApplication.getValueAt(tblLockerRentSIApplication.getSelectedRow(),4))+
                CommonUtil.convertObjToDouble(tblLockerRentSIApplication.getValueAt(tblLockerRentSIApplication.getSelectedRow(),5))+
                        CommonUtil.convertObjToDouble(tblLockerRentSIApplication.getValueAt(tblLockerRentSIApplication.getSelectedRow(),6)) + gst;
        if(total > CommonUtil.convertObjToDouble(tblLockerRentSIApplication.getValueAt(tblLockerRentSIApplication.getSelectedRow(),9))){
            tblLockerRentSIApplication.setValueAt(new Boolean(false),tblLockerRentSIApplication.getSelectedRow(),0);
        }
        if (evt.getClickCount()==2) {
            if (returnMap!=null) {
                if (returnMap.containsKey(tblLockerRentSIApplication.getValueAt(
                tblLockerRentSIApplication.getSelectedRow(), 0))) {
                    TTException exception = (TTException)returnMap.get(tblLockerRentSIApplication.getValueAt(
                    tblLockerRentSIApplication.getSelectedRow(), 0));
                    parseException.logException(exception, true);
                }
            }
        }
        
    }//GEN-LAST:event_tblLockerRentSIApplicationMouseClicked
    
    private void btnClear1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnClear1ActionPerformed
        // TODO add your handling code here:
        chkFdToLocker.setEnabled(true);
        cboLockerType.setSelectedItem("");
        txtRentDueAsOnMM.setText("");
        txtRentDueAsOnyyyy.setText("");
        observable.resetForm();
        ClientUtil.clearAll(this);
        txtRentDueAsOnyyyy.setEnabled(true);
        txtRentDueAsOnMM.setEnabled(true);
        cboLockerType.setEnabled(true);
        cboLockerType.setEnabled(true);
        btngroup.clearSelection();
    }//GEN-LAST:event_btnClear1ActionPerformed
    public ArrayList getrentCalculation(int mm,int yyyy){
        java.util.Date upexpdt=DateUtil.getDate(31, mm, yyyy);
        Date upexp=(Date)currDate.clone();
        upexp.setDate(31 );
        upexp.setMonth(upexpdt.getMonth());
        upexp.setYear(upexpdt.getYear());
        ArrayList rowList=new ArrayList();
        HashMap where = new HashMap();
        HashMap stMap=new HashMap();
        String lockerType = "";
        stMap.put("EXP_DT", upexp);
        List stList =ClientUtil.executeQuery("getLockDetails",stMap);
        if(stList!=null && stList.size()>0){
            stMap = null;
            int s= stList.size();
            for(int j=0;j<s;j++){
                double ser=0;
                double dd1=0;
                double val1=0;
                double ser1=0;
                double dd2=0;
                double val2=0;
                double ser2=0;
                double dd3=0;
                double val3=0;
                double ser3=0;
                double dd4=0;
                double val4=0;
                double ser4=0;
                double val=0;
                double charg=0;
                double service=0;
                double totval=0;
                double comm;
                double serv;
                double dd=0;
                int  realmont=0;
                Date edt=null;
                stMap = (HashMap) stList.get(j);
                String locNo=CommonUtil.convertObjToStr(stMap.get("LOCKER_NUM"));
                String issueDt=CommonUtil.convertObjToStr(stMap.get("ISSUE_DT"));
                expDt= CommonUtil.convertObjToStr(stMap.get("EXP_DT"));
                expiryDate=DateUtil.getDateMMDDYYYY(expDt);
                java.util.Date isdt= DateUtil.getDateMMDDYYYY(issueDt);
                int k=1;
                int date=isdt.getDate();
                int month=isdt.getMonth()+1;
                int year=isdt.getYear()+1900;
                java.util.Date exp= DateUtil.getDateMMDDYYYY(expDt);
                
                int day=exp.getDate();
                int mont=exp.getMonth()+1;;
                int yyy=exp.getYear()+1900;
                
                int cal=yyyy-yyy;
                int calyy=cal*12;
                int calmon=mm-mont;
                int totcal=calmon+calyy;
                
                HashMap hash=new HashMap();
                Date tempDt = (Date)currDate.clone();
                tempDt.setDate(isdt.getDate());
                tempDt.setMonth(isdt.getMonth());
                tempDt.setYear(isdt.getYear());
                
                Date tempexDt = (Date)currDate.clone();
                tempexDt.setDate(exp.getDate());
                tempexDt.setMonth(exp.getMonth());
                tempexDt.setYear(exp.getYear());
                //System.out.println("Today date here "+tempDt);
                // System.out.println("exp date here "+tempexDt);
                hash.put("PROD_ID", lockerType);
                hash.put("EXP_DT", tempexDt);
                hash.put("TODAY_DT", tempDt);
                hash.put("CHARGE_TYPE", "RENT_CHARGES");
                hash.put("LOCKER_NUM", locNo);
                hash.put("BRANCH_ID", TrueTransactMain.BRANCH_ID);
                if(totcal>=0){
                    List lis=ClientUtil.executeQuery("getExpDetailsForDayEnd",hash);
                    if(lis !=null && lis.size()>0){
                        hash = null;
                        hash= (HashMap)lis.get(0);
                        List LIST=ClientUtil.executeQuery("getLockerTypeForDayEnd", hash);
                        if(LIST!=null && LIST.size()>0){
                            HashMap HASHMAP=(HashMap)LIST.get(0);
                            lockerType=CommonUtil.convertObjToStr(HASHMAP.get("PROD_ID"));
                        }
                        String prodId=CommonUtil.convertObjToStr(hash.get("PRODUCT_ID"));
                        String prodType=CommonUtil.convertObjToStr(hash.get("PROD_TYPE"));
                        String actNo=CommonUtil.convertObjToStr(hash.get("CUSTOMER_ID_CR"));
                        String name=CommonUtil.convertObjToStr(hash.get("FNAME"));
                        String bal=CommonUtil.convertObjToStr(hash.get("AVAILABLE_BALANCE"));
                        
                       double penal_Amt= setLockerCha(lockerType); 
                       System.out.println("penal_Amt"+penal_Amt);
                        
                        for(int i=yyy;i<=yyyy;i++) {
                            
                            
                            
                            if(mm==mont && yyy==yyyy){
                                if(k==1){
                                    HashMap h2=new HashMap();
                                    Date htempmid=(Date)currDate.clone();
                                    htempmid.setDate(exp.getDate());
                                    htempmid.setMonth(exp.getMonth());
                                    Date expdate=(Date)currDate.clone();
                                    expdate.setDate(31);
                                    expdate.setMonth(11);
                                    expdate.setYear(yyyy);
                                    htempmid.setYear(i-1900);
                                    h2.put("PROD_ID",lockerType);
                                    h2.put("TODAY_DT",  htempmid);
                                    h2.put("CHARGE_TYPE","RENT_CHARGES");
                                    h2.put("EXP_DT", expdate);
                                    List hlit=ClientUtil.executeQuery("getServiceDetails",h2);
                                    int diffcurmont=exp.getMonth()+1;
                                    int q=0;
                                    if(hlit!=null && hlit.size()>0){
                                        h2= null;
                                        int calmonth=12-diffcurmont;
                                        for(int m=0;m<hlit.size();m++){
                                            
                                            h2= (HashMap)hlit.get(m);
                                            comm = CommonUtil.convertObjToDouble(h2.get("COMMISION")).doubleValue();
                                            serv = CommonUtil.convertObjToDouble(h2.get("SERVICE_TAX")).doubleValue();
                                            String sdate=CommonUtil.convertObjToStr(h2.get("START_DT"));
                                            String eedate=CommonUtil.convertObjToStr(h2.get("END_DT"));
                                            
                                            
                                            
                                            if(eedate!=""){
                                                edt= DateUtil.getDateMMDDYYYY(eedate);
                                                Date comparedt=(Date)currDate.clone();
                                                comparedt.setDate(edt.getDate());
                                                comparedt.setMonth(edt.getMonth());
                                                comparedt.setYear(edt.getYear());
                                                
                                                int eeyear=edt.getYear()+1900;
                                                int eemonth=edt.getMonth()+1;
                                                
                                                Date ssdate= DateUtil.getDateMMDDYYYY(sdate);
                                                int syear=ssdate.getYear()+1900;
                                                int smonth=ssdate.getMonth()+1;
                                                
                                                
                                                int diffmonth=(eeyear-syear)+(eemonth-smonth);
                                                
                                                realmont=calmonth-diffmonth+1;
                                                
                                                if(eeyear==i && DateUtil.dateDiff(htempmid,comparedt)>=0)
                                                    
                                                {
                                                    if(calmonth!=0){
                                                        if(q==0)
                                                            realmont=eemonth-(exp.getMonth()+1);
                                                        else
                                                            realmont=(eemonth-smonth)+1;
                                                        double d=comm/12.0;
                                                        
                                                        dd1=dd1+d*realmont;
                                                        dd = (double)getNearest((long)(dd1 *100),100)/100;
                                                        ser1=ser1+((d*realmont)/100)* serv;
                                                        ser = (double)getNearest((long)(ser1 *100),100)/100;
                                                        val1=val1+dd+ser;
                                                        val = (double)getNearest((long)(val1 *100),100)/100;
                                                        calmonth=calmonth-realmont;
                                                        q++;
                                                    }
                                                }
                                                else  if(eeyear>i){
                                                    if(calmonth!=0){
                                                        realmont=calmonth;
                                                        double d=comm/12.0;
                                                        dd1=dd1+d*realmont;
                                                        dd = (double)getNearest((long)(dd1 *100),100)/100;
                                                        ser1=ser1+((d*realmont)/100)* serv;
                                                        ser = (double)getNearest((long)(ser1 *100),100)/100;
                                                        val1=val1+dd+ser;
                                                        val =(double)getNearest((long)(val1 *100),100)/100;
                                                        calmonth=calmonth-realmont;
                                                    }
                                                }
                                            }
                                            else {
                                                if(calmonth!=0){
                                                    double d=comm/12.0;
                                                    
                                                    dd1=dd1+d*calmonth;
                                                    
                                                    dd = (double)getNearest((long)(dd1 *100),100)/100;
                                                    ser1=ser3+((d*calmonth)/100)* serv;
                                                    ser = (double)getNearest((long)(ser1 *100),100)/100;
                                                    val1=val3+dd+ser;
                                                    val = (double)getNearest((long)(val1 *100),100)/100;
                                                    calmonth=calmonth-realmont;
                                                }
                                                
                                            }
                                        }
                                    }
                                    
                                    
                                    
                                    
                                    HashMap h3=new HashMap();
                                    Date hltempmid=(Date)currDate.clone();
                                    hltempmid.setDate(exp.getDate());
                                    hltempmid.setMonth(exp.getMonth());
                                    hltempmid.setYear((i+1)-1900);
                                    Date hlexpdate=(Date)currDate.clone();
                                    hlexpdate.setDate(31);
                                    hlexpdate.setMonth(11);
                                    hlexpdate.setYear((i+1)-1900);
                                    h3.put("PROD_ID",lockerType);
                                    h3.put("TODAY_DT",  hltempmid);
                                    h3.put("EXP_DT",hlexpdate);
                                    h3.put("CHARGE_TYPE","RENT_CHARGES");
                                    List hllit=ClientUtil.executeQuery("getServiceDetails",h3);
                                    if(hllit!=null && hlit.size()>0){
                                        h3= null;
                                        for(int m=0;m< hlit.size();m++){
                                            h3= (HashMap)hllit.get(m);
                                            comm = CommonUtil.convertObjToDouble(h3.get("COMMISION")).doubleValue();
                                            serv = CommonUtil.convertObjToDouble(h3.get("SERVICE_TAX")).doubleValue();
                                            String sdate=CommonUtil.convertObjToStr(h3.get("START_DT"));
                                            String  eedate=CommonUtil.convertObjToStr(h3.get("END_DT"));
                                            int calmonth=mm;
                                            if(eedate!= ""){
                                                edt= DateUtil.getDateMMDDYYYY(eedate);
                                                Date comparedt=(Date)currDate.clone();
                                                comparedt.setDate(edt.getDate());
                                                comparedt.setMonth(edt.getMonth());
                                                comparedt.setYear(edt.getYear());
                                                int eeyear=edt.getYear()+1900;
                                                int eemonth=edt.getMonth()+1;
                                                
                                                Date ssdate= DateUtil.getDateMMDDYYYY(sdate);
                                                int syear=ssdate.getYear()+1900;
                                                int smonth=ssdate.getMonth()+1;
                                                
                                                
                                                int diffmonth=(eeyear-syear)+(eemonth-smonth);
                                                
                                                realmont=calmonth-diffmonth+1;
                                                
                                                if(eeyear==i )
                                                    
                                                {
                                                    if(calmonth!=0){
                                                        if(calmonth<=eemonth)
                                                            realmont=calmonth;
                                                        else if(calmonth>eemonth)
                                                            realmont=eemonth;
                                                        double d=comm/12.0;
                                                        
                                                        dd=dd1+(d*realmont);
                                                        dd = (double)getNearest((long)(dd *100),100)/100;
                                                        ser=ser1+(((d*realmont)/100)* serv);
                                                        ser = (double)getNearest((long)(ser *100),100)/100;
                                                        val=val1+dd+ser;
                                                        val = (double)getNearest((long)(val *100),100)/100;
                                                        calmonth=calmonth-realmont;
                                                    }
                                                }
                                                else  if(eeyear>i){
                                                    if(calmonth!=0){
                                                        realmont=calmonth;
                                                        double d=comm/12.0;
                                                        dd=dd1+(d*realmont);
                                                        dd = (double)getNearest((long)(dd *100),100)/100;
                                                        ser=ser1+((d*realmont)/100)* serv;
                                                        ser = (double)getNearest((long)(ser *100),100)/100;
                                                        val=val1+dd+ser;
                                                        val =(double)getNearest((long)(val *100),100)/100;
                                                        calmonth=calmonth-realmont;
                                                    }
                                                }
                                            }
                                            else {
                                                if(calmonth!=0){
                                                    double d=comm/12.0;
                                                    
                                                    dd=dd1+(d*calmonth);
                                                    
                                                    dd = (double)getNearest((long)(dd *100),100)/100;
                                                    ser=ser1+(((d*calmonth)/100)* serv);
                                                    ser = (double)getNearest((long)(ser *100),100)/100;
                                                    val=val1+dd+ser;
                                                    val = (double)getNearest((long)(val *100),100)/100;
                                                    calmonth=calmonth-realmont;
                                                }
                                            }
                                        }
                                    }
                                    
                                    
                                    
                                }
                                else{
                                    HashMap h2=new HashMap();
                                    Date htempmid=(Date)currDate.clone();
                                    htempmid.setDate(exp.getDate());
                                    htempmid.setMonth(exp.getMonth());
                                    Date expdate=(Date)currDate.clone();
                                    expdate.setDate(31);
                                    expdate.setMonth(11);
                                    expdate.setYear(yyyy);
                                    htempmid.setYear(i-1900);
                                    h2.put("PROD_ID",lockerType);
                                    h2.put("TODAY_DT",  htempmid);
                                    h2.put("CHARGE_TYPE","RENT_CHARGES");
                                    h2.put("EXP_DT", expdate);
                                    List hlit=ClientUtil.executeQuery("getServiceDetails",h2);
                                    int diffcurmont=exp.getMonth()+1;
                                    
                                    if(hlit!=null && hlit.size()>0){
                                        h2= null;
                                        int calmonth=mont;
                                        for(int m=0;m<hlit.size();m++){
                                            h2= (HashMap)hlit.get(k);
                                            comm = CommonUtil.convertObjToDouble(h2.get("COMMISION")).doubleValue();
                                            serv = CommonUtil.convertObjToDouble(h2.get("SERVICE_TAX")).doubleValue();
                                            String sdate=CommonUtil.convertObjToStr(h2.get("START_DT"));
                                            String eedate=CommonUtil.convertObjToStr(h2.get("END_DT"));
                                            
                                            
                                            
                                            if(eedate!=""){
                                                edt= DateUtil.getDateMMDDYYYY(eedate);
                                                Date comparedt=(Date)currDate.clone();
                                                comparedt.setDate(edt.getDate());
                                                comparedt.setMonth(edt.getMonth());
                                                comparedt.setYear(edt.getYear());
                                                
                                                int eeyear=edt.getYear()+1900;
                                                int eemonth=edt.getMonth()+1;
                                                
                                                Date ssdate= DateUtil.getDateMMDDYYYY(sdate);
                                                int syear=ssdate.getYear()+1900;
                                                int smonth=ssdate.getMonth()+1;
                                                
                                                
                                                int diffmonth=(eeyear-syear)+(eemonth-smonth);
                                                
                                                realmont=calmonth-diffmonth+1;
                                                
                                                if(eeyear==i )
                                                    
                                                {
                                                    if(calmonth!=0) {
                                                        if(calmonth<=eemonth)
                                                            realmont=calmonth;
                                                        else if(calmonth>eemonth)
                                                            realmont=eemonth;
                                                        double d=comm/12.0;
                                                        
                                                        dd1=dd1+d*realmont;
                                                        dd = (double)getNearest((long)(dd1 *100),100)/100;
                                                        ser1=ser1+((d*realmont)/100)* serv;
                                                        ser = (double)getNearest((long)(ser1 *100),100)/100;
                                                        val1=val1+dd+ser;
                                                        val = (double)getNearest((long)(val1 *100),100)/100;
                                                        calmonth=calmonth-realmont;
                                                    }
                                                }
                                                else  if(eeyear>i){
                                                    if(calmonth!=0) {
                                                        realmont=calmonth;
                                                        double d=comm/12.0;
                                                        dd2=dd1+d*realmont;
                                                        dd = (double)getNearest((long)(dd2 *100),100)/100;
                                                        ser2=ser1+((d*realmont)/100)* serv;
                                                        ser = (double)getNearest((long)(ser2 *100),100)/100;
                                                        val2=val1+dd+ser;
                                                        val =(double)getNearest((long)(val2 *100),100)/100;
                                                        calmonth=calmonth-realmont;
                                                    }
                                                }
                                            }
                                            else {
                                                if( calmonth!=0){
                                                    
                                                    double d=comm/12.0;
                                                    
                                                    dd3=dd1+d*calmonth;
                                                    
                                                    dd = (double)getNearest((long)(dd3 *100),100)/100;
                                                    ser3=ser3+((d*calmonth)/100)* serv;
                                                    ser = (double)getNearest((long)(ser3 *100),100)/100;
                                                    val3=val3+dd+ser;
                                                    val = (double)getNearest((long)(val3 *100),100)/100;
                                                    calmonth=calmonth-realmont;
                                                }
                                            }
                                        }
                                    }
                                }
                                
                            }
                            else
                                if(i==yyyy) {
                                    if(mont<mm){
                                        int q=0;
                                        
                                        HashMap h1=new HashMap();
                                        Date tempmid=(Date)currDate.clone();
                                        tempmid.setDate(exp.getDate());
                                        tempmid.setMonth(exp.getMonth());
                                        tempmid.setYear(i-1900);
                                        Date hltempmid=(Date)currDate.clone();
                                        hltempmid.setDate(31);
                                        hltempmid.setMonth(11);
                                        hltempmid.setYear(i-1900);
                                        h1.put("PROD_ID",lockerType);
                                        h1.put("TODAY_DT",  tempmid);
                                        h1.put("EXP_DT",hltempmid);
                                        h1.put("CHARGE_TYPE","RENT_CHARGES");
                                        List lit=ClientUtil.executeQuery("getServiceDetails",h1);
                                        int calmonth;
                                        
                                        if(lit!=null && lit.size()>0){
                                            if(k==1){
                                                calmonth=12-mont;
                                                h1= null;
                                                for(int m=0;m<lit.size();m++){
                                                    h1= (HashMap)lit.get(m);
                                                    comm = CommonUtil.convertObjToDouble(h1.get("COMMISION")).doubleValue();
                                                    serv = CommonUtil.convertObjToDouble(h1.get("SERVICE_TAX")).doubleValue();
                                                    String sdate=CommonUtil.convertObjToStr(h1.get("START_DT"));
                                                    String eedate=CommonUtil.convertObjToStr(h1.get("END_DT"));
                                                    Date eedt= DateUtil.getDateMMDDYYYY(eedate);
                                                    Date ssdate= DateUtil.getDateMMDDYYYY(sdate);
                                                    
                                                    
                                                    if(eedate!= ""){
                                                        edt= DateUtil.getDateMMDDYYYY(eedate);
                                                        Date comparedt=(Date)currDate.clone();
                                                        comparedt.setDate(edt.getDate());
                                                        comparedt.setMonth(edt.getMonth());
                                                        comparedt.setYear(edt.getYear());
                                                        int eeyear=edt.getYear()+1900;
                                                        int eemonth=edt.getMonth()+1;
                                                        
                                                        
                                                        int syear=ssdate.getYear()+1900;
                                                        int smonth=ssdate.getMonth()+1;
                                                        
                                                        
                                                        int diffmonth=(eeyear-syear)+(eemonth-smonth);
                                                        
                                                        realmont=calmonth-diffmonth+1;
                                                        
                                                        if(eeyear==i && DateUtil.dateDiff(tempmid,comparedt)>=0)
                                                            
                                                        {
                                                            if(calmonth!=0) {
                                                                if(q==0)
                                                                    realmont=eemonth-(exp.getMonth()+1);
                                                                else
                                                                    realmont=(eemonth-smonth)+1;
                                                                double d=comm/12.0;
                                                                
                                                                dd2=dd2+(d*realmont);
                                                                dd = (double)getNearest((long)(dd2 *100),100)/100;
                                                                ser2=ser2+(((d*realmont)/100)* serv);
                                                                ser = (double)getNearest((long)(ser2 *100),100)/100;
                                                                val2=val2+dd+ser;
                                                                val = (double)getNearest((long)(val2 *100),100)/100;
                                                                calmonth=calmonth-realmont;
                                                                q++;
                                                            }
                                                        }
                                                        else  if(eeyear>i){
                                                            if(calmonth!=0) {
                                                                realmont=calmonth;
                                                                double d=comm/12.0;
                                                                dd2=dd2+(d*realmont);
                                                                dd = (double)getNearest((long)(dd *100),100)/100;
                                                                ser2=ser2+((d*realmont)/100)* serv;
                                                                ser = (double)getNearest((long)(ser *100),100)/100;
                                                                val2=val2+dd+ser;
                                                                val =(double)getNearest((long)(val *100),100)/100;
                                                                calmonth=calmonth-realmont;
                                                            }
                                                            
                                                        }
                                                    }
                                                    else {
                                                        if(calmonth!=0){
                                                            double d=comm/12.0;
                                                            
                                                            dd2=dd2+(d*calmonth);
                                                            
                                                            dd = (double)getNearest((long)(dd *100),100)/100;
                                                            ser2=ser2+(((d*calmonth)/100)* serv);
                                                            ser = (double)getNearest((long)(ser *100),100)/100;
                                                            val2=val2+dd+ser;
                                                            val = (double)getNearest((long)(val *100),100)/100;
                                                            calmonth=calmonth-realmont;
                                                        }
                                                    }
                                                }
                                            }
                                            
                                            else{
                                                calmonth=12;
                                                
                                                
                                                h1= null;
                                                for(int m=0;m<lit.size();m++){
                                                    h1= (HashMap)lit.get(m);
                                                    comm = CommonUtil.convertObjToDouble(h1.get("COMMISION")).doubleValue();
                                                    serv = CommonUtil.convertObjToDouble(h1.get("SERVICE_TAX")).doubleValue();
                                                    String sdate=CommonUtil.convertObjToStr(h1.get("START_DT"));
                                                    String eedate=CommonUtil.convertObjToStr(h1.get("END_DT"));
                                                    Date eedt= DateUtil.getDateMMDDYYYY(eedate);
                                                    Date ssdate= DateUtil.getDateMMDDYYYY(sdate);
                                                    
                                                    
                                                    if(eedate!= ""){
                                                        edt= DateUtil.getDateMMDDYYYY(eedate);
                                                        Date comparedt=(Date)currDate.clone();
                                                        comparedt.setDate(edt.getDate());
                                                        comparedt.setMonth(edt.getMonth());
                                                        comparedt.setYear(edt.getYear());
                                                        int eeyear=edt.getYear()+1900;
                                                        int eemonth=edt.getMonth()+1;
                                                        
                                                        
                                                        int syear=ssdate.getYear()+1900;
                                                        int smonth=ssdate.getMonth()+1;
                                                        
                                                        
                                                        int diffmonth=(eeyear-syear)+(eemonth-smonth);
                                                        
                                                        realmont=calmonth-diffmonth+1;
                                                        
                                                        if(eeyear==i )
                                                            
                                                        {
                                                            if(calmonth!=0) {
                                                                if(calmonth<=eemonth)
                                                                    realmont=calmonth;
                                                                else if(calmonth>eemonth)
                                                                    realmont=eemonth;
                                                                double d=comm/12.0;
                                                                
                                                                dd2=dd2+(d*realmont);
                                                                dd = (double)getNearest((long)(dd2 *100),100)/100;
                                                                ser2=ser2+(((d*realmont)/100)* serv);
                                                                ser = (double)getNearest((long)(ser2 *100),100)/100;
                                                                val2=val2+dd+ser;
                                                                val = (double)getNearest((long)(val2 *100),100)/100;
                                                                calmonth=calmonth-realmont;
                                                                
                                                            }
                                                        }
                                                        else  if(eeyear>i){
                                                            if(calmonth!=0) {
                                                                realmont=calmonth;
                                                                double d=comm/12.0;
                                                                dd2=dd2+(d*realmont);
                                                                dd = (double)getNearest((long)(dd *100),100)/100;
                                                                ser2=ser2+((d*realmont)/100)* serv;
                                                                ser = (double)getNearest((long)(ser *100),100)/100;
                                                                val2=val2+dd+ser;
                                                                val =(double)getNearest((long)(val *100),100)/100;
                                                                calmonth=calmonth-realmont;
                                                            }
                                                            
                                                        }
                                                    }
                                                    else {
                                                        if(calmonth!=0){
                                                            double d=comm/12.0;
                                                            
                                                            dd2=dd2+(d*calmonth);
                                                            
                                                            dd = (double)getNearest((long)(dd *100),100)/100;
                                                            ser2=ser2+(((d*calmonth)/100)* serv);
                                                            ser = (double)getNearest((long)(ser *100),100)/100;
                                                            val2=val2+dd+ser;
                                                            val = (double)getNearest((long)(val *100),100)/100;
                                                            calmonth=calmonth-realmont;
                                                        }
                                                    }
                                                }
                                            }
                                        }
                                        HashMap h3=new HashMap();
                                        Date temp=(Date)currDate.clone();
                                        temp.setDate(exp.getDate());
                                        temp.setMonth(exp.getMonth());
                                        temp.setYear((i+1)-1900);
                                        Date hlexpdate=(Date)currDate.clone();
                                        hlexpdate.setDate(31);
                                        hlexpdate.setMonth(11);
                                        hlexpdate.setYear((i+1)-1900);
                                        h3.put("PROD_ID",lockerType);
                                        h3.put("TODAY_DT",  temp);
                                        h3.put("EXP_DT",hlexpdate);
                                        h3.put("CHARGE_TYPE","RENT_CHARGES");
                                        List hllit=ClientUtil.executeQuery("getServiceDetails",h3);
                                        if(hllit!=null && hllit.size()>0){
                                            h3= null;
                                            calmonth=mont;
                                            for(int m=0;m<hllit.size();m++){
                                                h3= (HashMap)hllit.get(m);
                                                comm = CommonUtil.convertObjToDouble(h3.get("COMMISION")).doubleValue();
                                                serv = CommonUtil.convertObjToDouble(h3.get("SERVICE_TAX")).doubleValue();
                                                String   sdate=CommonUtil.convertObjToStr(h3.get("START_DT"));
                                                String eedate=CommonUtil.convertObjToStr(h3.get("END_DT"));
                                                
                                                if(eedate!= ""){
                                                    edt= DateUtil.getDateMMDDYYYY(eedate);
                                                    Date comparedt=(Date)currDate.clone();
                                                    comparedt.setDate(edt.getDate());
                                                    comparedt.setMonth(edt.getMonth());
                                                    comparedt.setYear(edt.getYear());
                                                    int eeyear=edt.getYear()+1900;
                                                    int eemonth=edt.getMonth()+1;
                                                    
                                                    Date  ssdate= DateUtil.getDateMMDDYYYY(sdate);
                                                    int syear=ssdate.getYear()+1900;
                                                    int smonth=ssdate.getMonth()+1;
                                                    
                                                    
                                                    int diffmonth=(eeyear-syear)+(eemonth-smonth);
                                                    
                                                    realmont=calmonth-diffmonth+1;
                                                    long  r= DateUtil.dateDiff(comparedt,hlexpdate);
                                                    System.out.println("#######"+r);
                                                    long  l= DateUtil.dateDiff(temp,comparedt);
                                                    
                                                    if(eeyear==i+1 )
                                                        
                                                    {
                                                        if(calmonth!=0) {
                                                            if(calmonth<=eemonth)
                                                                realmont=calmonth;
                                                            else if(calmonth>eemonth)
                                                                realmont=eemonth;
                                                            double d=comm/12.0;
                                                            
                                                            dd=dd2+(d*realmont);
                                                            dd = (double)getNearest((long)(dd *100),100)/100;
                                                            ser=ser2+(((d*realmont)/100)* serv);
                                                            ser = (double)getNearest((long)(ser *100),100)/100;
                                                            val=val2+dd+ser;
                                                            val = (double)getNearest((long)(val *100),100)/100;
                                                            calmonth=calmonth-realmont;
                                                        }
                                                    }
                                                    else  if(eeyear>(i+1)){
                                                        if(calmonth!=0) {
                                                            realmont=calmonth;
                                                            double d=comm/12.0;
                                                            dd=dd2+(d*realmont);
                                                            dd = (double)getNearest((long)(dd *100),100)/100;
                                                            ser=ser2+((d*realmont)/100)* serv;
                                                            ser = (double)getNearest((long)(ser *100),100)/100;
                                                            val=val2+dd+ser;
                                                            val =(double)getNearest((long)(val *100),100)/100;
                                                            calmonth=calmonth-realmont;
                                                        }
                                                    }
                                                }
                                                else {
                                                    if(calmonth!=0){
                                                        
                                                        double d=comm/12.0;
                                                        
                                                        dd=dd2+(d*calmonth);
                                                        
                                                        dd = (double)getNearest((long)(dd *100),100)/100;
                                                        ser=ser2+(((d*calmonth)/100)* serv);
                                                        ser = (double)getNearest((long)(ser *100),100)/100;
                                                        val=val2+dd+ser;
                                                        val = (double)getNearest((long)(val *100),100)/100;
                                                        calmonth=calmonth-realmont;
                                                    }
                                                }
                                            }
                                        }
                                    }
                                    else{
                                        HashMap h1=new HashMap();
                                        Date tempmid=(Date)currDate.clone();
                                        tempmid.setDate(exp.getDate());
                                        tempmid.setMonth(exp.getMonth());
                                        tempmid.setYear(i-1900);
                                        Date hltempmid=(Date)currDate.clone();
                                        hltempmid.setDate(31);
                                        hltempmid.setMonth(11);
                                        hltempmid.setYear(i-1900);
                                        h1.put("PROD_ID",lockerType);
                                        h1.put("TODAY_DT",  tempmid);
                                        h1.put("EXP_DT",hltempmid);
                                        h1.put("CHARGE_TYPE","RENT_CHARGES");
                                        List lit=ClientUtil.executeQuery("getServiceDetails",h1);
                                        if(lit!=null && lit.size()>0){
                                            h1= null;
                                            int calmonth=mont;
                                            for(int m=0;m<lit.size();m++){
                                                h1= (HashMap)lit.get(m);
                                                comm = CommonUtil.convertObjToDouble(h1.get("COMMISION")).doubleValue();
                                                serv = CommonUtil.convertObjToDouble(h1.get("SERVICE_TAX")).doubleValue();
                                                String sdate=CommonUtil.convertObjToStr(h1.get("START_DT"));
                                                String eedate=CommonUtil.convertObjToStr(h1.get("END_DT"));
                                                Date eedt= DateUtil.getDateMMDDYYYY(eedate);
                                                Date ssdate= DateUtil.getDateMMDDYYYY(sdate);
                                                
                                                
                                                
                                                
                                                
                                                
                                                if(eedate!= ""){
                                                    edt= DateUtil.getDateMMDDYYYY(eedate);
                                                    Date comparedt=(Date)currDate.clone();
                                                    comparedt.setDate(edt.getDate());
                                                    comparedt.setMonth(edt.getMonth());
                                                    comparedt.setYear(edt.getYear());
                                                    int eeyear=edt.getYear()+1900;
                                                    int eemonth=edt.getMonth()+1;
                                                    
                                                    
                                                    int syear=ssdate.getYear()+1900;
                                                    int smonth=ssdate.getMonth()+1;
                                                    
                                                    
                                                    int diffmonth=(eeyear-syear)+(eemonth-smonth);
                                                    
                                                    realmont=calmonth-diffmonth+1;
                                                    
                                                    if(eeyear==i )
                                                        
                                                    {
                                                        if(calmonth!=0) {
                                                            if(calmonth<=eemonth)
                                                                realmont=calmonth;
                                                            else if(calmonth>eemonth)
                                                                realmont=eemonth;
                                                            double d=comm/12.0;
                                                            
                                                            dd1=dd1+(d*realmont);
                                                            dd = (double)getNearest((long)(dd1 *100),100)/100;
                                                            ser1=ser1+((d*realmont)/100)* serv;
                                                            ser = (double)getNearest((long)(ser1 *100),100)/100;
                                                            val1=val1+dd+ser;
                                                            val = (double)getNearest((long)(val1 *100),100)/100;
                                                            calmonth=calmonth-realmont;
                                                        }
                                                    }
                                                    else  if(eeyear>i){
                                                        if(calmonth!=0) {
                                                            realmont=calmonth;
                                                            double d=comm/12.0;
                                                            dd=dd1+(d*realmont);
                                                            dd = (double)getNearest((long)(dd *100),100)/100;
                                                            ser=ser1+((d*realmont)/100)* serv;
                                                            ser = (double)getNearest((long)(ser *100),100)/100;
                                                            val=val1+ser;
                                                            val =(double)getNearest((long)(val *100),100)/100;
                                                            calmonth=calmonth-realmont;
                                                        }
                                                    }
                                                }
                                                else {
                                                    if(calmonth!=0){
                                                        double d=comm/12.0;
                                                        
                                                        dd=dd1+(d*calmonth);
                                                        
                                                        dd = (double)getNearest((long)(dd *100),100)/100;
                                                        ser=ser1+((d*calmonth)/100)* serv;
                                                        ser = (double)getNearest((long)(ser *100),100)/100;
                                                        val=val1+ser;
                                                        val = (double)getNearest((long)(val *100),100)/100;
                                                        calmonth=calmonth-realmont;
                                                    }
                                                    
                                                }
                                            }
                                        }
                                    }
                                    
                                    
                                }
                            
                            
                            
                                else
                                    if(yyy>=i){
                                        HashMap h=new HashMap();
                                        Date hhdate=(Date)currDate.clone();
                                        hhdate.setDate(31);
                                        hhdate.setMonth(11);
                                        hhdate.setYear(i-1900);
                                        h.put("PROD_ID",lockerType);
                                        h.put("TODAY_DT",tempexDt);
                                        h.put("CHARGE_TYPE","RENT_CHARGES");
                                        h.put("EXP_DT",hhdate);
                                        int q=0;
                                        List list=ClientUtil.executeQuery("getServiceDetails",h);
                                        if(list!=null && list.size()>0){
                                            h= null;
                                            int calmonth=12-mont;
                                            for(int m=0;m<list.size();m++){
                                                h= (HashMap)list.get(m);
                                                comm = CommonUtil.convertObjToDouble(h.get("COMMISION")).doubleValue();
                                                serv = CommonUtil.convertObjToDouble(h.get("SERVICE_TAX")).doubleValue();
                                                String sdate=CommonUtil.convertObjToStr(h.get("START_DT"));
                                                String eedate=CommonUtil.convertObjToStr(h.get("END_DT"));
                                                
                                                
                                                
                                                if(eedate!=""){
                                                    edt= DateUtil.getDateMMDDYYYY(eedate);
                                                    Date comparedt=(Date)currDate.clone();
                                                    comparedt.setDate(edt.getDate());
                                                    comparedt.setMonth(edt.getMonth());
                                                    comparedt.setYear(edt.getYear());
                                                    
                                                    int eeyear=edt.getYear()+1900;
                                                    int eemonth=edt.getMonth()+1;
                                                    
                                                    Date ssdate= DateUtil.getDateMMDDYYYY(sdate);
                                                    int syear=ssdate.getYear()+1900;
                                                    int smonth=ssdate.getMonth()+1;
                                                    
                                                    
                                                    int diffmonth=(eeyear-syear)+(eemonth-smonth);
                                                    
                                                    realmont=exp.getMonth()+1;
                                                    
                                                    if(eeyear==i && DateUtil.dateDiff(tempexDt,comparedt)>=0)
                                                        
                                                    {
                                                        if(calmonth!=0)
                                                            
                                                        {
                                                            if(q==0)
                                                                realmont=eemonth-(exp.getMonth()+1);
                                                            else
                                                                realmont=(eemonth-smonth)+1;
                                                            double d=comm/12.0;
                                                            
                                                            dd1=dd1+d*realmont;
                                                            dd = (double)getNearest((long)(dd1 *100),100)/100;
                                                            ser1=ser1+((d*realmont)/100)* serv;
                                                            ser = (double)getNearest((long)(ser1 *100),100)/100;
                                                            val1=val1+dd+ser;
                                                            val = (double)getNearest((long)(val1 *100),100)/100;
                                                            calmonth=calmonth-realmont;
                                                            q++;
                                                        }
                                                    }
                                                    else  if(eeyear>i){
                                                        if(calmonth!=0) {
                                                            realmont=calmonth;
                                                            double d=comm/12.0;
                                                            dd=dd1+d*realmont;
                                                            dd = (double)getNearest((long)(dd *100),100)/100;
                                                            ser=ser1+((d*realmont)/100)* serv;
                                                            ser = (double)getNearest((long)(ser *100),100)/100;
                                                            val=val1+dd+ser;
                                                            val =(double)getNearest((long)(val *100),100)/100;
                                                            calmonth=calmonth-realmont;
                                                        }
                                                    }
                                                }
                                                else {
                                                    if(calmonth!=0){
                                                        double d=comm/12.0;
                                                        
                                                        dd=dd1+d*calmonth;
                                                        
                                                        dd = (double)getNearest((long)(dd *100),100)/100;
                                                        ser=ser1+((d*calmonth)/100)* serv;
                                                        ser = (double)getNearest((long)(ser *100),100)/100;
                                                        val=val1+dd+ser;
                                                        val = (double)getNearest((long)(val *100),100)/100;
                                                        calmonth=calmonth-realmont;
                                                    }
                                                }
                                            }
                                        }
                                    }
                                    else   {
                                        HashMap h1=new HashMap();
                                        Date hhdate=(Date)currDate.clone();
                                        hhdate.setDate(31);
                                        hhdate.setMonth(11);
                                        hhdate.setYear(i-1900);
                                        Date tempmid=(Date)currDate.clone();
                                        tempmid.setDate(exp.getDate());
                                        tempmid.setMonth(exp.getMonth());
                                        tempmid.setYear(i-1900);
                                        h1.put("PROD_ID",lockerType);
                                        h1.put("TODAY_DT",  tempmid);
                                        h1.put("CHARGE_TYPE","RENT_CHARGES");
                                        h1.put("EXP_DT",hhdate);
                                        List lit=ClientUtil.executeQuery("getServiceDetails",h1);
                                        int calmonth=12;
                                        if(lit!=null && lit.size()>0){
                                            h1= null;
                                            for(int m=0;m<lit.size();m++){
                                                h1= (HashMap)lit.get(m);
                                                comm = CommonUtil.convertObjToDouble(h1.get("COMMISION")).doubleValue();
                                                serv = CommonUtil.convertObjToDouble(h1.get("SERVICE_TAX")).doubleValue();
                                                String sdate=CommonUtil.convertObjToStr(h1.get("START_DT"));
                                                String eedate=CommonUtil.convertObjToStr(h1.get("END_DT"));
                                                
                                                if(eedate!=""){
                                                    edt= DateUtil.getDateMMDDYYYY(eedate);
                                                    Date comparedt=(Date)currDate.clone();
                                                    comparedt.setDate(edt.getDate());
                                                    comparedt.setMonth(edt.getMonth());
                                                    comparedt.setYear(edt.getYear());
                                                    
                                                    int eeyear=edt.getYear()+1900;
                                                    int eemonth=edt.getMonth()+1;
                                                    
                                                    Date ssdate= DateUtil.getDateMMDDYYYY(sdate);
                                                    int syear=ssdate.getYear()+1900;
                                                    int smonth=ssdate.getMonth()+1;
                                                    
                                                    
                                                    int diffmonth=(eeyear-syear)+(eemonth-smonth);
                                                    
                                                    
                                                    
                                                    if(eeyear==i )
                                                        
                                                    {
                                                        if(calmonth!=0) {
                                                            if(calmonth<=eemonth)
                                                                realmont=calmonth;
                                                            else if(calmonth>eemonth)
                                                                realmont=eemonth;
                                                            double d=comm/12.0;
                                                            
                                                            dd1=dd1+d*realmont;
                                                            dd = (double)getNearest((long)(dd1 *100),100)/100;
                                                            ser1=ser1+((d*realmont)/100)* serv;
                                                            ser = (double)getNearest((long)(ser1 *100),100)/100;
                                                            val1=val1+dd+ser;
                                                            val = (double)getNearest((long)(val1 *100),100)/100;
                                                            calmonth=calmonth-realmont;
                                                        }
                                                    }
                                                    else  if(eeyear>i){
                                                        if(calmonth!=0) {
                                                            realmont=calmonth;
                                                            double d=comm/12.0;
                                                            dd=dd1+d*realmont;
                                                            dd = (double)getNearest((long)(dd *100),100)/100;
                                                            ser=ser1+((d*realmont)/100)* serv;
                                                            ser = (double)getNearest((long)(ser *100),100)/100;
                                                            val=val1+dd+ser;
                                                            val =(double)getNearest((long)(val *100),100)/100;
                                                            calmonth=calmonth-realmont;
                                                        }
                                                    }
                                                }
                                                else {
                                                    if(calmonth!=0){
                                                        double d=comm/12.0;
                                                        
                                                        dd=dd1+d*calmonth;
                                                        
                                                        dd = (double)getNearest((long)(dd *100),100)/100;
                                                        ser=ser1+((d*calmonth)/100)* serv;
                                                        ser = (double)getNearest((long)(ser *100),100)/100;
                                                        val=val1+dd+ser;
                                                        val = (double)getNearest((long)(val *100),100)/100;
                                                        calmonth=calmonth-realmont;
                                                    }
                                                }
                                            }
                                        }
                                    }
                            
                            charg=charg+dd;
                            service=service+ser;
                            totval=totval+val;
                            k++;
                            dd1=0;
                            dd2=0;
                            dd3=0;
                            dd4=0;
                            ser1=0;
                            ser2=0;
                            ser3=0;
                            ser4=0;
                            val1=0;
                            val2=0;
                            val3=0;
                            val4=0;
                        }
                        
                        ArrayList alist=new ArrayList();
                        alist.add(new Boolean(true));
                        alist.add(locNo);
                        alist.add(name);
                        alist.add(expDt);
                        alist.add(String.valueOf(charg));
                        alist.add(String.valueOf(service));
                        alist.add(penal_Amt);
                        alist.add( prodId);
                        alist.add( prodType);
                        alist.add(bal);
                        alist.add(actNo);
                        rowList.add(alist);
                        
                    }
                }
            }
            
        }
        else {
            ClientUtil.noDataAlert();
            
        }
        return rowList;
    }
    private void btnProcess1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnProcess1ActionPerformed
                
        int mm=CommonUtil.convertObjToInt(txtRentDueAsOnMM.getText());
        int yyyy=CommonUtil.convertObjToInt(txtRentDueAsOnyyyy.getText());
        chkFdToLocker.setEnabled(false);
        cboLockerType.setEnabled(false);
        if (!chkFdToLocker.isSelected()) {
            selectMode = true;
            java.util.Date upexpdt = DateUtil.getDate(31, mm, yyyy);
            Date upexp = (Date) currDate.clone();
            upexp.setDate(31);
            upexp.setMonth(upexpdt.getMonth());
            upexp.setYear(upexpdt.getYear());
            ArrayList rowList = new ArrayList();
            HashMap where = new HashMap();
            HashMap stMap = new HashMap();
            String lockerType = CommonUtil.convertObjToStr(((ComboBoxModel) (cboLockerType.getModel())).getKeyForSelected());
            stMap.put("PROD_ID", lockerType);
            stMap.put("EXP_DT", upexp);
            List stList = ClientUtil.executeQuery("getLockDetails", stMap);
            if (stList != null && stList.size() > 0) {
                stMap = null;
                int s = stList.size();
                for (int j = 0; j < s; j++) {
                    double ser = 0;
                    double dd1 = 0;
                    double val1 = 0;
                    double ser1 = 0;
                    double dd2 = 0;
                    double val2 = 0;
                    double ser2 = 0;
                    double dd3 = 0;
                    double val3 = 0;
                    double ser3 = 0;
                    double dd4 = 0;
                    double val4 = 0;
                    double ser4 = 0;
                    double val = 0;
                    double charg = 0;
                    double service = 0;
                    double totval = 0;
                    double comm;
                    double serv;
                    double dd = 0;
                    int realmont = 0;
                    Date edt = null;
                    stMap = (HashMap) stList.get(j);
                    String locNo = CommonUtil.convertObjToStr(stMap.get("LOCKER_NUM"));
                    String issueDt = CommonUtil.convertObjToStr(stMap.get("ISSUE_DT"));
                    expDt = CommonUtil.convertObjToStr(stMap.get("EXP_DT"));
                    expiryDate = DateUtil.getDateMMDDYYYY(expDt);
                    java.util.Date isdt = DateUtil.getDateMMDDYYYY(issueDt);
                    int k = 1;
                    int date = isdt.getDate();
                    int month = isdt.getMonth() + 1;
                    int year = isdt.getYear() + 1900;
                    java.util.Date exp = DateUtil.getDateMMDDYYYY(expDt);

                    int day = exp.getDate();
                    int mont = exp.getMonth() + 1;;
                    int yyy = exp.getYear() + 1900;

                    int cal = yyyy - yyy;
                    int calyy = cal * 12;
                    int calmon = mm - mont;
                    int totcal = calmon + calyy;

                    HashMap hash = new HashMap();
                    Date tempDt = (Date) currDate.clone();
                    tempDt.setDate(isdt.getDate());
                    tempDt.setMonth(isdt.getMonth());
                    tempDt.setYear(isdt.getYear());

                    Date tempexDt = (Date) currDate.clone();
                    tempexDt.setDate(exp.getDate());
                    tempexDt.setMonth(exp.getMonth());
                    tempexDt.setYear(exp.getYear());
                    hash.put("PROD_ID", lockerType);
                    hash.put("TODAY_DT", tempDt);
                    hash.put("EXP_DT", tempexDt);
                    hash.put("CHARGE_TYPE", "RENT_CHARGES");
                    hash.put("LOCKER_NUM", locNo);
                    hash.put("BRANCH_ID", TrueTransactMain.BRANCH_ID);
                    if (totcal >= 0) {
                        List lis = ClientUtil.executeQuery("getExpDetails", hash);
                        if (lis != null && lis.size() > 0) {
                            hash = null;
                            hash = (HashMap) lis.get(0);

                            String prodId = CommonUtil.convertObjToStr(hash.get("PRODUCT_ID"));
                            String prodType = CommonUtil.convertObjToStr(hash.get("PROD_TYPE"));
                            String actNo = CommonUtil.convertObjToStr(hash.get("CUSTOMER_ID_CR"));
                            String name = CommonUtil.convertObjToStr(hash.get("FNAME"));
                            String bal = CommonUtil.convertObjToStr(hash.get("AVAILABLE_BALANCE"));

                            double penal_Amt = setLockerCha(lockerType);
                            System.out.println("penal_Amt" + penal_Amt);

                            for (int i = yyy; i <= yyyy; i++) {



                                if (mm == mont && yyy == yyyy) {
                                    if (k == 1) {
                                        HashMap h2 = new HashMap();
                                        Date htempmid = (Date) currDate.clone();
                                        htempmid.setDate(exp.getDate());
                                        htempmid.setMonth(exp.getMonth());
                                        Date expdate = (Date) currDate.clone();
                                        expdate.setDate(31);
                                        expdate.setMonth(11);
                                        expdate.setYear(yyyy);
                                        htempmid.setYear(i - 1900);
                                        h2.put("PROD_ID", lockerType);
                                        h2.put("TODAY_DT", htempmid);
                                        h2.put("CHARGE_TYPE", "RENT_CHARGES");
                                        h2.put("EXP_DT", expdate);
                                        List hlit = ClientUtil.executeQuery("getServiceDetails", h2);
                                        int diffcurmont = exp.getMonth() + 1;
                                        int q = 0;
                                        if (hlit != null && hlit.size() > 0) {
                                            h2 = null;
                                            int calmonth = 12 - diffcurmont;
                                            for (int m = 0; m < hlit.size(); m++) {

                                                h2 = (HashMap) hlit.get(m);
                                                comm = CommonUtil.convertObjToDouble(h2.get("COMMISION")).doubleValue();
                                                serv = CommonUtil.convertObjToDouble(h2.get("SERVICE_TAX")).doubleValue();
                                                String sdate = CommonUtil.convertObjToStr(h2.get("START_DT"));
                                                String eedate = CommonUtil.convertObjToStr(h2.get("END_DT"));



                                                if (eedate != "") {
                                                    edt = DateUtil.getDateMMDDYYYY(eedate);
                                                    Date comparedt = (Date) currDate.clone();
                                                    comparedt.setDate(edt.getDate());
                                                    comparedt.setMonth(edt.getMonth());
                                                    comparedt.setYear(edt.getYear());

                                                    int eeyear = edt.getYear() + 1900;
                                                    int eemonth = edt.getMonth() + 1;

                                                    Date ssdate = DateUtil.getDateMMDDYYYY(sdate);
                                                    int syear = ssdate.getYear() + 1900;
                                                    int smonth = ssdate.getMonth() + 1;


                                                    int diffmonth = (eeyear - syear) + (eemonth - smonth);

                                                    realmont = calmonth - diffmonth + 1;

                                                    if (eeyear == i && DateUtil.dateDiff(htempmid, comparedt) >= 0) {
                                                        if (calmonth != 0) {
                                                            if (q == 0) {
                                                                realmont = eemonth - (exp.getMonth() + 1);
                                                            } else {
                                                                realmont = (eemonth - smonth) + 1;
                                                            }
                                                            double d = comm / 12.0;

                                                            dd1 = dd1 + d * realmont;
                                                            dd = (double) getNearest((long) (dd1 * 100), 100) / 100;
                                                            ser1 = ser1 + ((d * realmont) / 100) * serv;
                                                            ser = (double) getNearest((long) (ser1 * 100), 100) / 100;
                                                            val1 = val1 + dd + ser;
                                                            val = (double) getNearest((long) (val1 * 100), 100) / 100;
                                                            calmonth = calmonth - realmont;
                                                            q++;
                                                        }
                                                    } else if (eeyear > i) {
                                                        if (calmonth != 0) {
                                                            realmont = calmonth;
                                                            double d = comm / 12.0;
                                                            dd1 = dd1 + d * realmont;
                                                            dd = (double) getNearest((long) (dd1 * 100), 100) / 100;
                                                            ser1 = ser1 + ((d * realmont) / 100) * serv;
                                                            ser = (double) getNearest((long) (ser1 * 100), 100) / 100;
                                                            val1 = val1 + dd + ser;
                                                            val = (double) getNearest((long) (val1 * 100), 100) / 100;
                                                            calmonth = calmonth - realmont;
                                                        }
                                                    }
                                                } else {
                                                    if (calmonth != 0) {
                                                        double d = comm / 12.0;

                                                        dd1 = dd1 + d * calmonth;

                                                        dd = (double) getNearest((long) (dd1 * 100), 100) / 100;
                                                        ser1 = ser3 + ((d * calmonth) / 100) * serv;
                                                        ser = (double) getNearest((long) (ser1 * 100), 100) / 100;
                                                        val1 = val3 + dd + ser;
                                                        val = (double) getNearest((long) (val1 * 100), 100) / 100;
                                                        calmonth = calmonth - realmont;
                                                    }

                                                }
                                            }
                                        }




                                        HashMap h3 = new HashMap();
                                        Date hltempmid = (Date) currDate.clone();
                                        hltempmid.setDate(exp.getDate());
                                        hltempmid.setMonth(exp.getMonth());
                                        hltempmid.setYear((i + 1) - 1900);
                                        Date hlexpdate = (Date) currDate.clone();
                                        hlexpdate.setDate(31);
                                        hlexpdate.setMonth(11);
                                        hlexpdate.setYear((i + 1) - 1900);
                                        h3.put("PROD_ID", lockerType);
                                        h3.put("TODAY_DT", hltempmid);
                                        h3.put("EXP_DT", hlexpdate);
                                        h3.put("CHARGE_TYPE", "RENT_CHARGES");
                                        List hllit = ClientUtil.executeQuery("getServiceDetails", h3);
                                        if (hllit != null && hlit.size() > 0) {
                                            h3 = null;
                                            for (int m = 0; m < hlit.size(); m++) {
                                                h3 = (HashMap) hllit.get(m);
                                                comm = CommonUtil.convertObjToDouble(h3.get("COMMISION")).doubleValue();
                                                serv = CommonUtil.convertObjToDouble(h3.get("SERVICE_TAX")).doubleValue();
                                                String sdate = CommonUtil.convertObjToStr(h3.get("START_DT"));
                                                String eedate = CommonUtil.convertObjToStr(h3.get("END_DT"));
                                                int calmonth = mm;
                                                if (eedate != "") {
                                                    edt = DateUtil.getDateMMDDYYYY(eedate);
                                                    Date comparedt = (Date) currDate.clone();
                                                    comparedt.setDate(edt.getDate());
                                                    comparedt.setMonth(edt.getMonth());
                                                    comparedt.setYear(edt.getYear());
                                                    int eeyear = edt.getYear() + 1900;
                                                    int eemonth = edt.getMonth() + 1;

                                                    Date ssdate = DateUtil.getDateMMDDYYYY(sdate);
                                                    int syear = ssdate.getYear() + 1900;
                                                    int smonth = ssdate.getMonth() + 1;


                                                    int diffmonth = (eeyear - syear) + (eemonth - smonth);

                                                    realmont = calmonth - diffmonth + 1;

                                                    if (eeyear == i) {
                                                        if (calmonth != 0) {
                                                            if (calmonth <= eemonth) {
                                                                realmont = calmonth;
                                                            } else if (calmonth > eemonth) {
                                                                realmont = eemonth;
                                                            }
                                                            double d = comm / 12.0;

                                                            dd = dd1 + (d * realmont);
                                                            dd = (double) getNearest((long) (dd * 100), 100) / 100;
                                                            ser = ser1 + (((d * realmont) / 100) * serv);
                                                            ser = (double) getNearest((long) (ser * 100), 100) / 100;
                                                            val = val1 + dd + ser;
                                                            val = (double) getNearest((long) (val * 100), 100) / 100;
                                                            calmonth = calmonth - realmont;
                                                        }
                                                    } else if (eeyear > i) {
                                                        if (calmonth != 0) {
                                                            realmont = calmonth;
                                                            double d = comm / 12.0;
                                                            dd = dd1 + (d * realmont);
                                                            dd = (double) getNearest((long) (dd * 100), 100) / 100;
                                                            ser = ser1 + ((d * realmont) / 100) * serv;
                                                            ser = (double) getNearest((long) (ser * 100), 100) / 100;
                                                            val = val1 + dd + ser;
                                                            val = (double) getNearest((long) (val * 100), 100) / 100;
                                                            calmonth = calmonth - realmont;
                                                        }
                                                    }
                                                } else {
                                                    if (calmonth != 0) {
                                                        double d = comm / 12.0;

                                                        dd = dd1 + (d * calmonth);

                                                        dd = (double) getNearest((long) (dd * 100), 100) / 100;
                                                        ser = ser1 + (((d * calmonth) / 100) * serv);
                                                        ser = (double) getNearest((long) (ser * 100), 100) / 100;
                                                        val = val1 + dd + ser;
                                                        val = (double) getNearest((long) (val * 100), 100) / 100;
                                                        calmonth = calmonth - realmont;
                                                    }
                                                }
                                            }
                                        }
                                    } else {
                                        HashMap h2 = new HashMap();
                                        Date htempmid = (Date) currDate.clone();
                                        htempmid.setDate(exp.getDate());
                                        htempmid.setMonth(exp.getMonth());
                                        Date expdate = (Date) currDate.clone();
                                        expdate.setDate(31);
                                        expdate.setMonth(11);
                                        expdate.setYear(yyyy);
                                        htempmid.setYear(i - 1900);
                                        h2.put("PROD_ID", lockerType);
                                        h2.put("TODAY_DT", htempmid);
                                        h2.put("CHARGE_TYPE", "RENT_CHARGES");
                                        h2.put("EXP_DT", expdate);
                                        List hlit = ClientUtil.executeQuery("getServiceDetails", h2);
                                        int diffcurmont = exp.getMonth() + 1;

                                        if (hlit != null && hlit.size() > 0) {
                                            h2 = null;
                                            int calmonth = mont;
                                            for (int m = 0; m < hlit.size(); m++) {
                                                h2 = (HashMap) hlit.get(k);
                                                comm = CommonUtil.convertObjToDouble(h2.get("COMMISION")).doubleValue();
                                                serv = CommonUtil.convertObjToDouble(h2.get("SERVICE_TAX")).doubleValue();
                                                String sdate = CommonUtil.convertObjToStr(h2.get("START_DT"));
                                                String eedate = CommonUtil.convertObjToStr(h2.get("END_DT"));



                                                if (eedate != "") {
                                                    edt = DateUtil.getDateMMDDYYYY(eedate);
                                                    Date comparedt = (Date) currDate.clone();
                                                    comparedt.setDate(edt.getDate());
                                                    comparedt.setMonth(edt.getMonth());
                                                    comparedt.setYear(edt.getYear());

                                                    int eeyear = edt.getYear() + 1900;
                                                    int eemonth = edt.getMonth() + 1;

                                                    Date ssdate = DateUtil.getDateMMDDYYYY(sdate);
                                                    int syear = ssdate.getYear() + 1900;
                                                    int smonth = ssdate.getMonth() + 1;


                                                    int diffmonth = (eeyear - syear) + (eemonth - smonth);

                                                    realmont = calmonth - diffmonth + 1;

                                                    if (eeyear == i) {
                                                        if (calmonth != 0) {
                                                            if (calmonth <= eemonth) {
                                                                realmont = calmonth;
                                                            } else if (calmonth > eemonth) {
                                                                realmont = eemonth;
                                                            }
                                                            double d = comm / 12.0;

                                                            dd1 = dd1 + d * realmont;
                                                            dd = (double) getNearest((long) (dd1 * 100), 100) / 100;
                                                            ser1 = ser1 + ((d * realmont) / 100) * serv;
                                                            ser = (double) getNearest((long) (ser1 * 100), 100) / 100;
                                                            val1 = val1 + dd + ser;
                                                            val = (double) getNearest((long) (val1 * 100), 100) / 100;
                                                            calmonth = calmonth - realmont;
                                                        }
                                                    } else if (eeyear > i) {
                                                        if (calmonth != 0) {
                                                            realmont = calmonth;
                                                            double d = comm / 12.0;
                                                            dd2 = dd1 + d * realmont;
                                                            dd = (double) getNearest((long) (dd2 * 100), 100) / 100;
                                                            ser2 = ser1 + ((d * realmont) / 100) * serv;
                                                            ser = (double) getNearest((long) (ser2 * 100), 100) / 100;
                                                            val2 = val1 + dd + ser;
                                                            val = (double) getNearest((long) (val2 * 100), 100) / 100;
                                                            calmonth = calmonth - realmont;
                                                        }
                                                    }
                                                } else {
                                                    if (calmonth != 0) {

                                                        double d = comm / 12.0;

                                                        dd3 = dd1 + d * calmonth;

                                                        dd = (double) getNearest((long) (dd3 * 100), 100) / 100;
                                                        ser3 = ser3 + ((d * calmonth) / 100) * serv;
                                                        ser = (double) getNearest((long) (ser3 * 100), 100) / 100;
                                                        val3 = val3 + dd + ser;
                                                        val = (double) getNearest((long) (val3 * 100), 100) / 100;
                                                        calmonth = calmonth - realmont;
                                                    }
                                                }
                                            }
                                        }
                                    }

                                } else if (i == yyyy) {
                                    if (mont < mm) {
                                        int q = 0;

                                        HashMap h1 = new HashMap();
                                        Date tempmid = (Date) currDate.clone();
                                        tempmid.setDate(exp.getDate());
                                        tempmid.setMonth(exp.getMonth());
                                        tempmid.setYear(i - 1900);
                                        Date hltempmid = (Date) currDate.clone();
                                        hltempmid.setDate(31);
                                        hltempmid.setMonth(11);
                                        hltempmid.setYear(i - 1900);
                                        h1.put("PROD_ID", lockerType);
                                        h1.put("TODAY_DT", tempmid);
                                        h1.put("EXP_DT", hltempmid);
                                        h1.put("CHARGE_TYPE", "RENT_CHARGES");
                                        List lit = ClientUtil.executeQuery("getServiceDetails", h1);
                                        int calmonth;

                                        if (lit != null && lit.size() > 0) {
                                            if (k == 1) {
                                                calmonth = 12 - mont;
                                                h1 = null;
                                                for (int m = 0; m < lit.size(); m++) {
                                                    h1 = (HashMap) lit.get(m);
                                                    comm = CommonUtil.convertObjToDouble(h1.get("COMMISION")).doubleValue();
                                                    serv = CommonUtil.convertObjToDouble(h1.get("SERVICE_TAX")).doubleValue();
                                                    String sdate = CommonUtil.convertObjToStr(h1.get("START_DT"));
                                                    String eedate = CommonUtil.convertObjToStr(h1.get("END_DT"));
                                                    Date eedt = DateUtil.getDateMMDDYYYY(eedate);
                                                    Date ssdate = DateUtil.getDateMMDDYYYY(sdate);


                                                    if (eedate != "") {
                                                        edt = DateUtil.getDateMMDDYYYY(eedate);
                                                        Date comparedt = (Date) currDate.clone();
                                                        comparedt.setDate(edt.getDate());
                                                        comparedt.setMonth(edt.getMonth());
                                                        comparedt.setYear(edt.getYear());
                                                        int eeyear = edt.getYear() + 1900;
                                                        int eemonth = edt.getMonth() + 1;


                                                        int syear = ssdate.getYear() + 1900;
                                                        int smonth = ssdate.getMonth() + 1;


                                                        int diffmonth = (eeyear - syear) + (eemonth - smonth);

                                                        realmont = calmonth - diffmonth + 1;

                                                        if (eeyear == i && DateUtil.dateDiff(tempmid, comparedt) >= 0) {
                                                            if (calmonth != 0) {
                                                                if (q == 0) {
                                                                    realmont = eemonth - (exp.getMonth() + 1);
                                                                } else {
                                                                    realmont = (eemonth - smonth) + 1;
                                                                }
                                                                double d = comm / 12.0;

                                                                dd2 = dd2 + (d * realmont);
                                                                dd = (double) getNearest((long) (dd2 * 100), 100) / 100;
                                                                ser2 = ser2 + (((d * realmont) / 100) * serv);
                                                                ser = (double) getNearest((long) (ser2 * 100), 100) / 100;
                                                                val2 = val2 + dd + ser;
                                                                val = (double) getNearest((long) (val2 * 100), 100) / 100;
                                                                calmonth = calmonth - realmont;
                                                                q++;
                                                            }
                                                        } else if (eeyear > i) {
                                                            if (calmonth != 0) {
                                                                realmont = calmonth;
                                                                double d = comm / 12.0;
                                                                dd2 = dd2 + (d * realmont);
                                                                dd = (double) getNearest((long) (dd * 100), 100) / 100;
                                                                ser2 = ser2 + ((d * realmont) / 100) * serv;
                                                                ser = (double) getNearest((long) (ser * 100), 100) / 100;
                                                                val2 = val2 + dd + ser;
                                                                val = (double) getNearest((long) (val * 100), 100) / 100;
                                                                calmonth = calmonth - realmont;
                                                            }

                                                        }
                                                    } else {
                                                        if (calmonth != 0) {
                                                            double d = comm / 12.0;

                                                            dd2 = dd2 + (d * calmonth);

                                                            dd = (double) getNearest((long) (dd * 100), 100) / 100;
                                                            ser2 = ser2 + (((d * calmonth) / 100) * serv);
                                                            ser = (double) getNearest((long) (ser * 100), 100) / 100;
                                                            val2 = val2 + dd + ser;
                                                            val = (double) getNearest((long) (val * 100), 100) / 100;
                                                            calmonth = calmonth - realmont;
                                                        }
                                                    }
                                                }
                                            } else {
                                                calmonth = 12;


                                                h1 = null;
                                                for (int m = 0; m < lit.size(); m++) {
                                                    h1 = (HashMap) lit.get(m);
                                                    comm = CommonUtil.convertObjToDouble(h1.get("COMMISION")).doubleValue();
                                                    serv = CommonUtil.convertObjToDouble(h1.get("SERVICE_TAX")).doubleValue();
                                                    String sdate = CommonUtil.convertObjToStr(h1.get("START_DT"));
                                                    String eedate = CommonUtil.convertObjToStr(h1.get("END_DT"));
                                                    Date eedt = DateUtil.getDateMMDDYYYY(eedate);
                                                    Date ssdate = DateUtil.getDateMMDDYYYY(sdate);
                                                    if (eedate != "") {
                                                        edt = DateUtil.getDateMMDDYYYY(eedate);
                                                        Date comparedt = (Date) currDate.clone();
                                                        comparedt.setDate(edt.getDate());
                                                        comparedt.setMonth(edt.getMonth());
                                                        comparedt.setYear(edt.getYear());
                                                        int eeyear = edt.getYear() + 1900;
                                                        int eemonth = edt.getMonth() + 1;
                                                        int syear = ssdate.getYear() + 1900;
                                                        int smonth = ssdate.getMonth() + 1;
                                                        int diffmonth = (eeyear - syear) + (eemonth - smonth);
                                                        realmont = calmonth - diffmonth + 1;
                                                        if (eeyear == i) {
                                                            if (calmonth != 0) {
                                                                if (calmonth <= eemonth) {
                                                                    realmont = calmonth;
                                                                } else if (calmonth > eemonth) {
                                                                    realmont = eemonth;
                                                                }
                                                                double d = comm / 12.0;
                                                                dd2 = dd2 + (d * realmont);
                                                                dd = (double) getNearest((long) (dd2 * 100), 100) / 100;
                                                                ser2 = ser2 + (((d * realmont) / 100) * serv);
                                                                ser = (double) getNearest((long) (ser2 * 100), 100) / 100;
                                                                val2 = val2 + dd + ser;
                                                                val = (double) getNearest((long) (val2 * 100), 100) / 100;
                                                                calmonth = calmonth - realmont;
                                                            }
                                                        } else if (eeyear > i) {
                                                            if (calmonth != 0) {
                                                                realmont = calmonth;
                                                                double d = comm / 12.0;
                                                                dd2 = dd2 + (d * realmont);
                                                                dd = (double) getNearest((long) (dd * 100), 100) / 100;
                                                                ser2 = ser2 + ((d * realmont) / 100) * serv;
                                                                ser = (double) getNearest((long) (ser * 100), 100) / 100;
                                                                val2 = val2 + dd + ser;
                                                                val = (double) getNearest((long) (val * 100), 100) / 100;
                                                                calmonth = calmonth - realmont;
                                                            }

                                                        }
                                                    } else {
                                                        if (calmonth != 0) {
                                                            double d = comm / 12.0;
                                                            dd2 = dd2 + (d * calmonth);
                                                            dd = (double) getNearest((long) (dd * 100), 100) / 100;
                                                            ser2 = ser2 + (((d * calmonth) / 100) * serv);
                                                            ser = (double) getNearest((long) (ser * 100), 100) / 100;
                                                            val2 = val2 + dd + ser;
                                                            val = (double) getNearest((long) (val * 100), 100) / 100;
                                                            calmonth = calmonth - realmont;
                                                        }
                                                    }
                                                }
                                            }
                                        }
                                        HashMap h3 = new HashMap();
                                        Date temp = (Date) currDate.clone();
                                        temp.setDate(exp.getDate());
                                        temp.setMonth(exp.getMonth());
                                        temp.setYear((i + 1) - 1900);
                                        Date hlexpdate = (Date) currDate.clone();
                                        hlexpdate.setDate(31);
                                        hlexpdate.setMonth(11);
                                        hlexpdate.setYear((i + 1) - 1900);
                                        h3.put("PROD_ID", lockerType);
                                        h3.put("TODAY_DT", temp);
                                        h3.put("EXP_DT", hlexpdate);
                                        h3.put("CHARGE_TYPE", "RENT_CHARGES");
                                        List hllit = ClientUtil.executeQuery("getServiceDetails", h3);
                                        if (hllit != null && hllit.size() > 0) {
                                            h3 = null;
                                            calmonth = mont;
                                            for (int m = 0; m < hllit.size(); m++) {
                                                h3 = (HashMap) hllit.get(m);
                                                comm = CommonUtil.convertObjToDouble(h3.get("COMMISION")).doubleValue();
                                                serv = CommonUtil.convertObjToDouble(h3.get("SERVICE_TAX")).doubleValue();
                                                String sdate = CommonUtil.convertObjToStr(h3.get("START_DT"));
                                                String eedate = CommonUtil.convertObjToStr(h3.get("END_DT"));
                                                if (eedate != "") {
                                                    edt = DateUtil.getDateMMDDYYYY(eedate);
                                                    Date comparedt = (Date) currDate.clone();
                                                    comparedt.setDate(edt.getDate());
                                                    comparedt.setMonth(edt.getMonth());
                                                    comparedt.setYear(edt.getYear());
                                                    int eeyear = edt.getYear() + 1900;
                                                    int eemonth = edt.getMonth() + 1;
                                                    Date ssdate = DateUtil.getDateMMDDYYYY(sdate);
                                                    int syear = ssdate.getYear() + 1900;
                                                    int smonth = ssdate.getMonth() + 1;
                                                    int diffmonth = (eeyear - syear) + (eemonth - smonth);
                                                    realmont = calmonth - diffmonth + 1;
                                                    long r = DateUtil.dateDiff(comparedt, hlexpdate);
                                                    System.out.println("#######" + r);
                                                    long l = DateUtil.dateDiff(temp, comparedt);
                                                    if (eeyear == i + 1) {
                                                        if (calmonth != 0) {
                                                            if (calmonth <= eemonth) {
                                                                realmont = calmonth;
                                                            } else if (calmonth > eemonth) {
                                                                realmont = eemonth;
                                                            }
                                                            double d = comm / 12.0;
                                                            dd = dd2 + (d * realmont);
                                                            dd = (double) getNearest((long) (dd * 100), 100) / 100;
                                                            ser = ser2 + (((d * realmont) / 100) * serv);
                                                            ser = (double) getNearest((long) (ser * 100), 100) / 100;
                                                            val = val2 + dd + ser;
                                                            val = (double) getNearest((long) (val * 100), 100) / 100;
                                                            calmonth = calmonth - realmont;
                                                        }
                                                    } else if (eeyear > (i + 1)) {
                                                        if (calmonth != 0) {
                                                            realmont = calmonth;
                                                            double d = comm / 12.0;
                                                            dd = dd2 + (d * realmont);
                                                            dd = (double) getNearest((long) (dd * 100), 100) / 100;
                                                            ser = ser2 + ((d * realmont) / 100) * serv;
                                                            ser = (double) getNearest((long) (ser * 100), 100) / 100;
                                                            val = val2 + dd + ser;
                                                            val = (double) getNearest((long) (val * 100), 100) / 100;
                                                            calmonth = calmonth - realmont;
                                                        }
                                                    }
                                                } else {
                                                    if (calmonth != 0) {
                                                        double d = comm / 12.0;
                                                        dd = dd2 + (d * calmonth);
                                                        dd = (double) getNearest((long) (dd * 100), 100) / 100;
                                                        ser = ser2 + (((d * calmonth) / 100) * serv);
                                                        ser = (double) getNearest((long) (ser * 100), 100) / 100;
                                                        val = val2 + dd + ser;
                                                        val = (double) getNearest((long) (val * 100), 100) / 100;
                                                        calmonth = calmonth - realmont;
                                                    }
                                                }
                                            }
                                        }
                                    } else {
                                        HashMap h1 = new HashMap();
                                        Date tempmid = (Date) currDate.clone();
                                        tempmid.setDate(exp.getDate());
                                        tempmid.setMonth(exp.getMonth());
                                        tempmid.setYear(i - 1900);
                                        Date hltempmid = (Date) currDate.clone();
                                        hltempmid.setDate(31);
                                        hltempmid.setMonth(11);
                                        hltempmid.setYear(i - 1900);
                                        h1.put("PROD_ID", lockerType);
                                        h1.put("TODAY_DT", tempmid);
                                        h1.put("EXP_DT", hltempmid);
                                        h1.put("CHARGE_TYPE", "RENT_CHARGES");
                                        List lit = ClientUtil.executeQuery("getServiceDetails", h1);
                                        if (lit != null && lit.size() > 0) {
                                            h1 = null;
                                            int calmonth = mont;
                                            for (int m = 0; m < lit.size(); m++) {
                                                h1 = (HashMap) lit.get(m);
                                                comm = CommonUtil.convertObjToDouble(h1.get("COMMISION")).doubleValue();
                                                serv = CommonUtil.convertObjToDouble(h1.get("SERVICE_TAX")).doubleValue();
                                                String sdate = CommonUtil.convertObjToStr(h1.get("START_DT"));
                                                String eedate = CommonUtil.convertObjToStr(h1.get("END_DT"));
                                                Date eedt = DateUtil.getDateMMDDYYYY(eedate);
                                                Date ssdate = DateUtil.getDateMMDDYYYY(sdate);
                                                if (eedate != "") {
                                                    edt = DateUtil.getDateMMDDYYYY(eedate);
                                                    Date comparedt = (Date) currDate.clone();
                                                    comparedt.setDate(edt.getDate());
                                                    comparedt.setMonth(edt.getMonth());
                                                    comparedt.setYear(edt.getYear());
                                                    int eeyear = edt.getYear() + 1900;
                                                    int eemonth = edt.getMonth() + 1;
                                                    int syear = ssdate.getYear() + 1900;
                                                    int smonth = ssdate.getMonth() + 1;
                                                    int diffmonth = (eeyear - syear) + (eemonth - smonth);
                                                    realmont = calmonth - diffmonth + 1;
                                                    if (eeyear == i) {
                                                        if (calmonth != 0) {
                                                            if (calmonth <= eemonth) {
                                                                realmont = calmonth;
                                                            } else if (calmonth > eemonth) {
                                                                realmont = eemonth;
                                                            }
                                                            double d = comm / 12.0;
                                                            dd1 = dd1 + (d * realmont);
                                                            dd = (double) getNearest((long) (dd1 * 100), 100) / 100;
                                                            ser1 = ser1 + ((d * realmont) / 100) * serv;
                                                            ser = (double) getNearest((long) (ser1 * 100), 100) / 100;
                                                            val1 = val1 + dd + ser;
                                                            val = (double) getNearest((long) (val1 * 100), 100) / 100;
                                                            calmonth = calmonth - realmont;
                                                        }
                                                    } else if (eeyear > i) {
                                                        if (calmonth != 0) {
                                                            realmont = calmonth;
                                                            double d = comm / 12.0;
                                                            dd = dd1 + (d * realmont);
                                                            dd = (double) getNearest((long) (dd * 100), 100) / 100;
                                                            ser = ser1 + ((d * realmont) / 100) * serv;
                                                            ser = (double) getNearest((long) (ser * 100), 100) / 100;
                                                            val = val1 + ser;
                                                            val = (double) getNearest((long) (val * 100), 100) / 100;
                                                            calmonth = calmonth - realmont;
                                                        }
                                                    }
                                                } else {
                                                    if (calmonth != 0) {
                                                        double d = comm / 12.0;
                                                        dd = dd1 + (d * calmonth);
                                                        dd = (double) getNearest((long) (dd * 100), 100) / 100;
                                                        ser = ser1 + ((d * calmonth) / 100) * serv;
                                                        ser = (double) getNearest((long) (ser * 100), 100) / 100;
                                                        val = val1 + ser;
                                                        val = (double) getNearest((long) (val * 100), 100) / 100;
                                                        calmonth = calmonth - realmont;
                                                    }

                                                }
                                            }
                                        }
                                    }
                                } else if (yyy >= i) {
                                    HashMap h = new HashMap();
                                    Date hhdate = (Date) currDate.clone();
                                    hhdate.setDate(31);
                                    hhdate.setMonth(11);
                                    hhdate.setYear(i - 1900);
                                    h.put("PROD_ID", lockerType);
                                    h.put("TODAY_DT", tempexDt);
                                    h.put("CHARGE_TYPE", "RENT_CHARGES");
                                    h.put("EXP_DT", hhdate);
                                    int q = 0;
                                    List list = ClientUtil.executeQuery("getServiceDetails", h);
                                    if (list != null && list.size() > 0) {
                                        h = null;
                                        int calmonth = 12 - mont;
                                        for (int m = 0; m < list.size(); m++) {
                                            h = (HashMap) list.get(m);
                                            comm = CommonUtil.convertObjToDouble(h.get("COMMISION")).doubleValue();
                                            serv = CommonUtil.convertObjToDouble(h.get("SERVICE_TAX")).doubleValue();
                                            String sdate = CommonUtil.convertObjToStr(h.get("START_DT"));
                                            String eedate = CommonUtil.convertObjToStr(h.get("END_DT"));
                                            if (eedate != "") {
                                                edt = DateUtil.getDateMMDDYYYY(eedate);
                                                Date comparedt = (Date) currDate.clone();
                                                comparedt.setDate(edt.getDate());
                                                comparedt.setMonth(edt.getMonth());
                                                comparedt.setYear(edt.getYear());
                                                int eeyear = edt.getYear() + 1900;
                                                int eemonth = edt.getMonth() + 1;
                                                Date ssdate = DateUtil.getDateMMDDYYYY(sdate);
                                                int syear = ssdate.getYear() + 1900;
                                                int smonth = ssdate.getMonth() + 1;
                                                int diffmonth = (eeyear - syear) + (eemonth - smonth);
                                                realmont = exp.getMonth() + 1;
                                                if (eeyear == i && DateUtil.dateDiff(tempexDt, comparedt) >= 0) {
                                                    if (calmonth != 0) {
                                                        if (q == 0) {
                                                            realmont = eemonth - (exp.getMonth() + 1);
                                                        } else {
                                                            realmont = (eemonth - smonth) + 1;
                                                        }
                                                        double d = comm / 12.0;
                                                        dd1 = dd1 + d * realmont;
                                                        dd = (double) getNearest((long) (dd1 * 100), 100) / 100;
                                                        ser1 = ser1 + ((d * realmont) / 100) * serv;
                                                        ser = (double) getNearest((long) (ser1 * 100), 100) / 100;
                                                        val1 = val1 + dd + ser;
                                                        val = (double) getNearest((long) (val1 * 100), 100) / 100;
                                                        calmonth = calmonth - realmont;
                                                        q++;
                                                    }
                                                } else if (eeyear > i) {
                                                    if (calmonth != 0) {
                                                        realmont = calmonth;
                                                        double d = comm / 12.0;
                                                        dd = dd1 + d * realmont;
                                                        dd = (double) getNearest((long) (dd * 100), 100) / 100;
                                                        ser = ser1 + ((d * realmont) / 100) * serv;
                                                        ser = (double) getNearest((long) (ser * 100), 100) / 100;
                                                        val = val1 + dd + ser;
                                                        val = (double) getNearest((long) (val * 100), 100) / 100;
                                                        calmonth = calmonth - realmont;
                                                    }
                                                }
                                            } else {
                                                if (calmonth != 0) {
                                                    double d = comm / 12.0;
                                                    dd = dd1 + d * calmonth;
                                                    dd = (double) getNearest((long) (dd * 100), 100) / 100;
                                                    ser = ser1 + ((d * calmonth) / 100) * serv;
                                                    ser = (double) getNearest((long) (ser * 100), 100) / 100;
                                                    val = val1 + dd + ser;
                                                    val = (double) getNearest((long) (val * 100), 100) / 100;
                                                    calmonth = calmonth - realmont;
                                                }
                                            }
                                        }
                                    }
                                } else {
                                    HashMap h1 = new HashMap();
                                    Date hhdate = (Date) currDate.clone();
                                    hhdate.setDate(31);
                                    hhdate.setMonth(11);
                                    hhdate.setYear(i - 1900);
                                    Date tempmid = (Date) currDate.clone();
                                    tempmid.setDate(exp.getDate());
                                    tempmid.setMonth(exp.getMonth());
                                    tempmid.setYear(i - 1900);
                                    h1.put("PROD_ID", lockerType);
                                    h1.put("TODAY_DT", tempmid);
                                    h1.put("CHARGE_TYPE", "RENT_CHARGES");
                                    h1.put("EXP_DT", hhdate);
                                    List lit = ClientUtil.executeQuery("getServiceDetails", h1);
                                    int calmonth = 12;
                                    if (lit != null && lit.size() > 0) {
                                        h1 = null;
                                        for (int m = 0; m < lit.size(); m++) {
                                            h1 = (HashMap) lit.get(m);
                                            comm = CommonUtil.convertObjToDouble(h1.get("COMMISION")).doubleValue();
                                            serv = CommonUtil.convertObjToDouble(h1.get("SERVICE_TAX")).doubleValue();
                                            String sdate = CommonUtil.convertObjToStr(h1.get("START_DT"));
                                            String eedate = CommonUtil.convertObjToStr(h1.get("END_DT"));
                                            if (eedate != "") {
                                                edt = DateUtil.getDateMMDDYYYY(eedate);
                                                Date comparedt = (Date) currDate.clone();
                                                comparedt.setDate(edt.getDate());
                                                comparedt.setMonth(edt.getMonth());
                                                comparedt.setYear(edt.getYear());
                                                int eeyear = edt.getYear() + 1900;
                                                int eemonth = edt.getMonth() + 1;
                                                Date ssdate = DateUtil.getDateMMDDYYYY(sdate);
                                                int syear = ssdate.getYear() + 1900;
                                                int smonth = ssdate.getMonth() + 1;
                                                int diffmonth = (eeyear - syear) + (eemonth - smonth);
                                                if (eeyear == i) {
                                                    if (calmonth != 0) {
                                                        if (calmonth <= eemonth) {
                                                            realmont = calmonth;
                                                        } else if (calmonth > eemonth) {
                                                            realmont = eemonth;
                                                        }
                                                        double d = comm / 12.0;

                                                        dd1 = dd1 + d * realmont;
                                                        dd = (double) getNearest((long) (dd1 * 100), 100) / 100;
                                                        ser1 = ser1 + ((d * realmont) / 100) * serv;
                                                        ser = (double) getNearest((long) (ser1 * 100), 100) / 100;
                                                        val1 = val1 + dd + ser;
                                                        val = (double) getNearest((long) (val1 * 100), 100) / 100;
                                                        calmonth = calmonth - realmont;
                                                    }
                                                } else if (eeyear > i) {
                                                    if (calmonth != 0) {
                                                        realmont = calmonth;
                                                        double d = comm / 12.0;
                                                        dd = dd1 + d * realmont;
                                                        dd = (double) getNearest((long) (dd * 100), 100) / 100;
                                                        ser = ser1 + ((d * realmont) / 100) * serv;
                                                        ser = (double) getNearest((long) (ser * 100), 100) / 100;
                                                        val = val1 + dd + ser;
                                                        val = (double) getNearest((long) (val * 100), 100) / 100;
                                                        calmonth = calmonth - realmont;
                                                    }
                                                }
                                            } else {
                                                if (calmonth != 0) {
                                                    double d = comm / 12.0;
                                                    dd = dd1 + d * calmonth;
                                                    dd = (double) getNearest((long) (dd * 100), 100) / 100;
                                                    ser = ser1 + ((d * calmonth) / 100) * serv;
                                                    ser = (double) getNearest((long) (ser * 100), 100) / 100;
                                                    val = val1 + dd + ser;
                                                    val = (double) getNearest((long) (val * 100), 100) / 100;
                                                    calmonth = calmonth - realmont;
                                                }
                                            }
                                        }
                                    }
                                }
                                charg = charg + dd;
                                service = service + ser;
                                totval = totval + val;
                                k++;
                                dd1 = 0;
                                dd2 = 0;
                                dd3 = 0;
                                dd4 = 0;
                                ser1 = 0;
                                ser2 = 0;
                                ser3 = 0;
                                ser4 = 0;
                                val1 = 0;
                                val2 = 0;
                                val3 = 0;
                                val4 = 0;
                            }
                            double sgst = 0.0;
                            double cgst = 0.0;
                            if (TrueTransactMain.SERVICE_TAX_REQ.equals("Y")) {
                                HashMap serviceTax_Map = calculateServiceTax(charg, service, penal_Amt);
                                if (serviceTax_Map.containsKey("KRISHI_KALYAN_CESS") && serviceTax_Map.get("KRISHI_KALYAN_CESS") != null) {
                                    sgst = CommonUtil.convertObjToDouble(serviceTax_Map.get("KRISHI_KALYAN_CESS"));
                                }
                                if (serviceTax_Map.containsKey("SWACHH_CESS") && serviceTax_Map.get("SWACHH_CESS") != null) {
                                    cgst = CommonUtil.convertObjToDouble(serviceTax_Map.get("SWACHH_CESS"));
                                }
                            }
                            ArrayList alist = new ArrayList();
                            alist.add(new Boolean(false));
                            alist.add(locNo);
                            alist.add(name);
                            alist.add(expDt);
                            alist.add(String.valueOf(charg));
                            alist.add(String.valueOf(service));
                            alist.add(penal_Amt);
                            alist.add(prodId);
                            alist.add(prodType);
                            alist.add(bal);
                            alist.add(actNo);
                            alist.add(sgst+cgst);
                            rowList.add(alist);
                            observable.insertTableData(rowList);
                            tblLockerRentSIApplication.setModel(observable.getTblLockerRentSIApplication());
                        }
                    }
                }
            } else {
                ClientUtil.noDataAlert();
            }
            chkSelectAll.setSelected(false);
            cboLockerType.setEnabled(false);
            txtRentDueAsOnMM.setEnabled(false);
            txtRentDueAsOnyyyy.setEnabled(false);
            btngroup.clearSelection();
        } else {
            HashMap viewMap = new HashMap();
            HashMap whereMap = new HashMap();
            boolean isOK = false;
            if (CommonUtil.convertObjToStr(cboLockerType.getSelectedItem()).equals("")) {
                ClientUtil.displayAlert("Select Locker Type ...");
                return;
            }
            if (txtRentDueAsOnMM.getText().equals("")) {
                ClientUtil.displayAlert("Please Enter Month...");
                return;
            }
            if (txtRentDueAsOnyyyy.getText().equals("")) {
                ClientUtil.displayAlert("Please Enter Year...");
                return;
            }
            whereMap.put("PROD_ID", CommonUtil.convertObjToStr(((ComboBoxModel) (cboLockerType.getModel())).getKeyForSelected()));
            whereMap.put("BRANCH_ID", TrueTransactMain.selBranch);
            whereMap.put("MONTH", CommonUtil.convertObjToStr(txtRentDueAsOnMM.getText()));
            whereMap.put("YEAR", CommonUtil.convertObjToStr(txtRentDueAsOnyyyy.getText()));
            viewMap.put(CommonConstants.MAP_NAME, "getLockerWithDepostDetails");
            viewMap.put(CommonConstants.MAP_WHERE, whereMap);
            try {
                ArrayList heading = observable.populateData(viewMap, tblLockerRentSIApplication);
                heading = null;
            } catch (Exception e) {
                System.err.println("Exception " + e.toString() + "Caught");
                e.printStackTrace();
            }
            viewMap = null;
            whereMap = null;
        }
    }//GEN-LAST:event_btnProcess1ActionPerformed
    

    private HashMap calculateServiceTax(double rent, double serviceTax, double penal) {
        // Added by nithya for GST
        HashMap taxMap;
        HashMap serviceTax_Map = null;
        List taxSettingsList = new ArrayList();
        HashMap headMap = new HashMap();
        headMap.put("value", CommonUtil.convertObjToStr(((ComboBoxModel) (cboLockerType.getModel())).getKeyForSelected()));
        List lockerTransHeadList = ClientUtil.executeQuery("getLockerAccountHeads", headMap);// Added by nithya for GST changes 
        if (lockerTransHeadList != null && lockerTransHeadList.size() > 0) {
            HashMap lockerTransHeadMap = (HashMap) lockerTransHeadList.get(0);
            //-- GST for rent --
            if (rent > 0) {
                String achd = CommonUtil.convertObjToStr(lockerTransHeadMap.get("LOC_RENT_AC_HD"));
                HashMap checkForTaxMap = observable.checkServiceTaxApplicable(achd);
                taxMap = getGSTAmountMap(checkForTaxMap);
                taxMap.put(ServiceTaxCalculation.TOT_AMOUNT, rent);
                if (taxMap != null && taxMap.size() > 0) {
                    taxSettingsList.add(taxMap);
                }
            }
            //-- GST for servicetax --
            if (serviceTax > 0) {
                String achd = CommonUtil.convertObjToStr(lockerTransHeadMap.get("SERV_TAX_AC_HD"));
                HashMap checkForTaxMap = observable.checkServiceTaxApplicable(achd);
                taxMap = getGSTAmountMap(checkForTaxMap);
                taxMap.put(ServiceTaxCalculation.TOT_AMOUNT, serviceTax);
                if (taxMap != null && taxMap.size() > 0) {
                    taxSettingsList.add(taxMap);
                }
            }
            //-- GST for Penal --
            if (penal > 0) {
                String achd = CommonUtil.convertObjToStr(lockerTransHeadMap.get("PENAL_INTEREST_AC_HEAD"));
                HashMap checkForTaxMap = observable.checkServiceTaxApplicable(achd);
                taxMap = getGSTAmountMap(checkForTaxMap);
                taxMap.put(ServiceTaxCalculation.TOT_AMOUNT, penal);
                if (taxMap != null && taxMap.size() > 0) {
                    taxSettingsList.add(taxMap);
                }
            }
            System.out.println("taxSettingsList :: " + taxSettingsList);
            serviceTax_Map = getTaxAmount(taxSettingsList);
            System.out.println("serviceTax_Map :: " + serviceTax_Map);
        }
        return serviceTax_Map;
        // End
    }
    
     private HashMap getTaxAmount(List taxSettingsList) {   
         HashMap serviceTax_Map = new HashMap();
        if (taxSettingsList != null && taxSettingsList.size() > 0) {    
            HashMap ser_Tax_Val = new HashMap();
            ser_Tax_Val.put(ServiceTaxCalculation.CURR_DT, currDate);
            ser_Tax_Val.put("SERVICE_TAX_DATA", taxSettingsList);
            try {
                ServiceTaxCalculation objServiceTax = new ServiceTaxCalculation();
                serviceTax_Map = objServiceTax.calculateServiceTax(ser_Tax_Val);
                if (serviceTax_Map != null && serviceTax_Map.containsKey(ServiceTaxCalculation.TOT_TAX_AMT)) {
                    String amt = CommonUtil.convertObjToStr(serviceTax_Map.get(ServiceTaxCalculation.TOT_TAX_AMT));
                    serviceTax_Map.put(ServiceTaxCalculation.TOT_TAX_AMT, amt);
                } 
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
        return serviceTax_Map;
    }
    
    
    
    
     private double setLockerCha(String prodID)
        {
            try
            {
        Date tempDt = (Date)currDate.clone();
        HashMap stMap = new HashMap();
        stMap.put("PROD_ID",prodID);
        stMap.put("TODAY_DT",tempDt);
        stMap.put("CHARGE_TYPE","RENT_CHARGES");
        System.out.println("stMap===="+stMap);
        List stList =ClientUtil.executeQuery("getSelectPenalty",stMap);
        if(stList!=null && stList.size()>0){
            stMap = null;
            stMap = (HashMap) stList.get(0);
            int count_commi=Integer.parseInt(stMap.get("COUNT_COMM").toString());
            System.out.println("count_commi======="+count_commi);
            java.util.Date ldate=tempDt;
            if(count_commi==1)
            {
                    int lyear=expiryDate.getYear()+1900;
                    //java.util.Date c=currDt;
                    int cyear=tempDt.getYear()+1900;
                    int year_diff=cyear-lyear;
//                    System.out.println("year_diff===="+year_diff);
                    
                    HashMap stMap1 = new HashMap();
                        stMap1.put("PROD_ID",prodID);
                        stMap1.put("TODAY_DT",tempDt);
                        stMap1.put("CHARGE_TYPE","RENT_CHARGES");
                        List stList1 =ClientUtil.executeQuery("getSelectCommission",stMap1);
                        if(stList1!=null && stList1.size()>0)
                        {
                            stMap1 = null;
                            stMap1 = (HashMap) stList1.get(0);
                            commision=Integer.parseInt(stMap1.get("COMMISION").toString());
                            System.out.println("commision====="+commision);
                        }
                        HashMap stMap2 = new HashMap();
                        System.out.println("prodID==="+prodID);
                        stMap2.put("PROD_ID",prodID);
                        List stList2 =ClientUtil.executeQuery("getSelectPenalRate",stMap2);
                        System.out.println("stList2====="+stList2);
                        if(stList2!=null && stList2.size()>0)
                        {
                            stMap2 = null;
                            stMap2 = (HashMap) stList2.get(0);
                            Penal_rate=Integer.parseInt(stMap2.get("PENAL_RATE_OF_INTEREST").toString());
                            System.out.println("Penal_rate====="+Penal_rate);
                        }
//                        System.out.println("commision===="+commision);
//                        System.out.println("no_of_days===="+no_of_days);
//                        System.out.println("Penal_rate===="+Penal_rate);
            
                    
            if(year_diff<=1)
                    {
                        System.out.println("ldate==="+ldate);
                        System.out.println("expiryDate===="+expDt);
                        //int no_of_days1 = (int) ((ldate.getTime() - expDt.getTime()));
                        no_of_days = (int) ((ldate.getTime() - expiryDate.getTime())/(1000*60*60*24));
                        double penal_Amt=(commision*no_of_days*Penal_rate)/36500.0;
                        penal_Amt=(double)getNearest((long)(penal_Amt *100),100)/100;
                        System.out.println("penal_Amt===="+penal_Amt);
                        //txtPenalAmt.setText(CommonUtil.convertObjToStr(penal_Amt));
                        return penal_Amt;
            }
            else if(year_diff>1)
            {
                Date due_date=expiryDate;
                Date todayDt=ldate;
                String dDate=getDateddMMyyyy(due_date);
                System.out.println("dDate================"+dDate);
                int no_of_days=0;
                double penal_Amt=0.0;
                java.util.Date dt1 =null;
//                java.util.Date expirydate=DateUtil.getDateMMDDYYYY(due_date);
                do
                {
                    no_of_days = (int) (( todayDt.getTime()-due_date.getTime())/(1000*60*60*24));
                    penal_Amt=penal_Amt+((commision*no_of_days*Penal_rate)/36500.0);   
                    String dt = "";  // Start date
                    SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
                    Calendar cal = Calendar.getInstance();
                    cal.setTime(sdf.parse(dDate));
                    cal.add(Calendar.YEAR, 1);  // number of days to add
                    dDate = sdf.format(cal.getTime());
                    dt1 = sdf.parse(dDate);
                    due_date=dt1;
                 }
              while(DateUtil.dateDiff(todayDt, dt1)<0);
                penal_Amt=(double)getNearest((long)(penal_Amt *100),100)/100;
                //txtPenalAmt.setText(CommonUtil.convertObjToStr(penal_Amt));
                return penal_Amt;
            }
        } 
            else if(count_commi>1)
            {
                
                String incrdDate=null;
                Date incrUtildt1=null;
                Double penal_Amt=0.0;
                Date due_date=expiryDate;
                Date todayDt=ldate;
                do
                {
                String dDate=getDateddMMyyyy(due_date);
                SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
                Calendar cal = Calendar.getInstance();
                cal.setTime(sdf.parse(dDate));
                cal.add(Calendar.YEAR, 1);
                incrdDate = sdf.format(cal.getTime());
                incrUtildt1 = sdf.parse(incrdDate);
                HashMap stMapn = new HashMap();
                        stMapn.put("PROD_ID",prodID);
                        System.out.println("due_date==="+due_date);
                        stMapn.put("BEG_DAT",due_date);
                        System.out.println("incrUtildt1==="+incrUtildt1);
                        stMapn.put("LAST_DAT",incrUtildt1);
                        stMapn.put("CHARGE_TYPE","RENT_CHARGES");
                         List stListn =ClientUtil.executeQuery("getSelectCommissionForIncr",stMapn);
                         //System.out.println("incrUtildt1==="+incrUtildt1);
                        if(stListn!=null && stListn.size()>0)
                        {
                            System.out.println("stListn==="+stListn);
                            stMapn = null;
                            stMapn = (HashMap) stListn.get(0);
                            System.out.println("stListn==="+stListn);
                            commision=Integer.parseInt(stMapn.get("COMMISION").toString());
                            System.out.println("commision====="+commision);
                        }
                        else 
                        {
                            HashMap stMapx = new HashMap(); 
                            stMapx.put("PROD_ID",prodID);
                            stMapx.put("CHARGE_TYPE","RENT_CHARGES");
                            List stListx =ClientUtil.executeQuery("getSelectCommForIncr",stMapx);
                             if(stListx!=null && stListx.size()>0)
                        {
                            stMapx = null;
                            stMapx = (HashMap) stListx.get(count_commi-1);
                            System.out.println("stMapx====="+stMapx);
                            commision=Integer.parseInt(stMapx.get("COMMISION").toString());
                            System.out.println("commision====="+commision);
                        }
                        }
                         System.out.println("todaydate===="+todayDt);
                         System.out.println("due_date===="+due_date);
                         no_of_days = (int) (( todayDt.getTime()-due_date.getTime())/(1000*60*60*24));
                          System.out.println("no_of_days====="+no_of_days);
                          
                          HashMap stMap2 = new HashMap();
                        System.out.println("prodID==="+prodID);
                        stMap2.put("PROD_ID",prodID);
                        List stList2 =ClientUtil.executeQuery("getSelectPenalRate",stMap2);
                        System.out.println("stList2====="+stList2);
                        if(stList2!=null && stList2.size()>0)
                        {
                            stMap2 = null;
                            stMap2 = (HashMap) stList2.get(0);
                            Penal_rate = Integer.parseInt(CommonUtil.convertObjToStr(stMap2.get("PENAL_RATE_OF_INTEREST")));

                            System.out.println("Penal_rate=====" + Penal_rate);
                        }
                        System.out.println("penal_-amt"+penal_Amt);
                    penal_Amt=penal_Amt+((commision*no_of_days*Penal_rate)/36500.0); 
                    System.out.println("penal_Amt====="+penal_Amt);
                due_date=incrUtildt1;
                System.out.println("due_date====="+due_date);
                
                
                }
                while(DateUtil.dateDiff(todayDt, incrUtildt1)<0);
                 penal_Amt=(double)getNearest((long)(penal_Amt *100),100)/100;
                //txtPenalAmt.setText(CommonUtil.convertObjToStr(penal_Amt));
                 return penal_Amt;
            }
        }
       
            }
            catch(Exception e)
            {
                e.printStackTrace();
            }
//        else
//        {
////            ClientUtil.showMessageWindow("Error In Populating Comission and Service Tax");
//        }
            return 0.0;
    }
     public String getDateddMMyyyy(java.util.Date strDate1)
{
    ////////////////////////////
     DateFormat df = new SimpleDateFormat("MM/dd/yyyy");
         
        // Get the date today using Calendar object.
       // Date today = Calendar.getInstance().getTime();       
        // Using DateFormat format method we can create a string
        // representation of a date with the defined format.
        String strDate = df.format(strDate1);
    //////////////////////////////
    
    
    
    
    //String strDate=(String)strDate1;
    SimpleDateFormat dateFormat =null;
    java.util.Date varDate=null;
    try
    {
        //String strDate="23-Mar-2011";
    dateFormat = new SimpleDateFormat("MM/dd/yyyy");//

    varDate=(java.util.Date) dateFormat.parse(strDate);
    dateFormat=new SimpleDateFormat("dd-MM-yyyy");
    //System.out.println("Date :"+dateFormat.format(varDate));

    }
    catch(Exception e)
    {
        e.printStackTrace();;
    }
    return dateFormat.format(varDate);
}

    private void btnCloseActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCloseActionPerformed
        // TODO add your handling code here:
        this.dispose();
    }//GEN-LAST:event_btnCloseActionPerformed
   public void getRentCalculationProcess(int mm, int yyyy){
       int n=tblLockerRentSIApplication.getSelectedRow();
        HashMap data = new HashMap();
        HashMap retMap=new HashMap();
        String ryear=CommonUtil.convertObjToStr(yyyy);
        String rmonth=CommonUtil.convertObjToStr(mm);
        java.util.Date upexpdt=DateUtil.getDate(31, mm, yyyy);
        Date upexp=(Date)currDate.clone();
        upexp.setDate(31 );
        upexp.setMonth(upexpdt.getMonth());
        upexp.setYear(upexpdt.getYear());
        String action="INSERT";
        String lockerType = CommonUtil.convertObjToStr(((ComboBoxModel)(cboLockerType.getModel())).getKeyForSelected());
        HashMap hmap=new HashMap();
        hmap.put("PROD_ID",lockerType);
        hmap.put("EXP_DT",upexp);
        ArrayList finalList = ((EnhancedTableModel)tblLockerRentSIApplication.getModel()).getDataArrayList();
        List llist=ClientUtil.executeQuery("getLockDetailsForDayEnd", hmap);
        hmap=null;
        hmap=(HashMap)llist.get(0);
        try{
            data.put("MODE",action);
            data.put("Finallist",finalList);
            data.put("lockertype",lockerType);
            data.put("name",hmap.get("CUSTOMER_NAME"));
            data.put("ryear",ryear);
            data.put("rmonth",rmonth);
            data.put(CommonConstants.MODULE, getModule());
            data.put(CommonConstants.SCREEN, getScreen());
            observable.doActionPerform(data);
            retMap = observable.getProxyReturnMap();
            
        }
        catch(Exception e) {
            e.printStackTrace();
        }
       
   }
    private void btnProcessActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnProcessActionPerformed
        // TODO add your handling code here:
        if (!chkFdToLocker.isSelected()) {
            if ((rdoCash.isSelected()) || (rdoTransfer.isSelected())) {
                int n = tblLockerRentSIApplication.getSelectedRow();
                // if()
                if (!flag && n < 0) {
                    ClientUtil.showMessageWindow("Please select atleast one row");
                } else {
                    System.out.println("no of rows " + n);
                    HashMap data = new HashMap();
                    int mm = CommonUtil.convertObjToInt(txtRentDueAsOnMM.getText());
                    int yyyy = CommonUtil.convertObjToInt(txtRentDueAsOnyyyy.getText());
                    String ryear = txtRentDueAsOnyyyy.getText();
                    String rmonth = txtRentDueAsOnMM.getText();
                    java.util.Date upexpdt = DateUtil.getDate(31, mm, yyyy);
                    Date upexp = (Date) currDate.clone();
                    upexp.setDate(31);
                    upexp.setMonth(upexpdt.getMonth());
                    upexp.setYear(upexpdt.getYear());
                    String action = "INSERT";
                    String lockerType = CommonUtil.convertObjToStr(((ComboBoxModel) (cboLockerType.getModel())).getKeyForSelected());
                    HashMap hmap = new HashMap();
                    hmap.put("PROD_ID", lockerType);
                    hmap.put("EXP_DT", upexp);
                    ArrayList finalList = ((EnhancedTableModel) tblLockerRentSIApplication.getModel()).getDataArrayList();
                    //System.out.println("Final list here "+finalList);
                    List llist = ClientUtil.executeQuery("getLockDetails", hmap);
                    System.out.println("Final list here " + llist);

                    hmap = null;
                    hmap = (HashMap) llist.get(0);
                    try {
                        data.put("MODE", action);
                        data.put("Finallist", finalList);
                        data.put("lockertype", lockerType);
                        data.put("name", CommonUtil.convertObjToStr(hmap.get("CUSTOMER_NAME")));
                        data.put("ryear", ryear);
                        data.put("rmonth", rmonth);
                        data.put(CommonConstants.MODULE, getModule());
                        data.put(CommonConstants.SCREEN, getScreen());
                        System.out.println("date hashmap " + data);
                        if (rdoCash.isSelected()) {
                            data.put("TRANS_MODE", "CASH");
                        }
                        if (rdoTransfer.isSelected()) {
                            data.put("TRANS_MODE", "TRANSFER");
                        }
                        data.put("SERVIC_TAX_REQ", TrueTransactMain.SERVICE_TAX_REQ);
                        observable.doActionPerform(data);
                        if (observable.getResult() != ClientConstants.ACTIONTYPE_FAILED) {
                            if (observable.getProxyReturnMap() == null || observable.getProxyReturnMap().size() == 0) {
                                ClientUtil.showMessageWindow(" Transaction Completed !!! ");

                            } else {

                                returnMap = observable.getProxyReturnMap();
                                System.out.println("returnMap" + returnMap);
                                List errorList = (ArrayList) returnMap.get("error");

                                List transList = new ArrayList();
                                if (errorList != null && errorList.size() > 0) {
                                    for (int j = 0; j < errorList.size(); j++) {
                                        if (errorList.get(j) instanceof HashMap) {
                                            returnMap = (HashMap) errorList.get(j);
                                        } else {
                                            transList.add(errorList.get(j));
                                        }
                                    }
                                    EnhancedTableModel tbm = observable.getTblLockerRentSIApplication();
                                    ArrayList head = observable.getTableTitle();
                                    ArrayList title = new ArrayList();
                                    selectMode = false;
                                    title.addAll(head);
                                    title.add("Status");
                                    title.remove(0);
                                    ArrayList dat = tbm.getDataArrayList();
                                    ArrayList rowList = null;
                                    for (int i = 0; i < tblLockerRentSIApplication.getRowCount(); i++) {
                                        rowList = (ArrayList) dat.get(i);
                                        if (returnMap.containsKey(tblLockerRentSIApplication.getValueAt(i, 1))) {
                                            rowList.add("Error");
                                        } else {
                                            if (((Boolean) tblLockerRentSIApplication.getValueAt(i, 0)).booleanValue()) {
                                                rowList.add("Completed");

                                            } else {
                                                rowList.add("Not Processed");
                                            }
                                        }
                                        rowList.remove(0);
                                    }
                                    tbm.setDataArrayList(dat, title);
                                    System.out.println("translist size " + transList.size());
                                    if (transList.size() > 0) {
                                        System.out.println("in trans loop");
                                        String toTransId = "";
                                        // System.out.println("trans value "+ CommonUtil.convertObjToStr(transList.get(1)));
                                        //String fromTransId = CommonUtil.convertObjToStr(transList.get(0));
                                        List lst = (List) transList.get(0);
                                        String fromTransId = CommonUtil.convertObjToStr(lst.get(0));
                                        System.out.println("Trans Id " + fromTransId);
                                        String message = "Please note the Transactions ID :   " + fromTransId;
                                        if (lst.size() > 3) {
                                            toTransId = CommonUtil.convertObjToStr(lst.get(lst.size() - 3));
                                            message = "Please note the Transactions IDs" + "\n" + "From Trans ID :   " + fromTransId + "\n" + "To Trans ID    :   " + toTransId;
                                            //message = "PLEASE NOTE THE TRANSACTION IDs" + toTransId;
                                        }
                                        ClientUtil.showMessageWindow(message);
                                    }
                                }
                            }


                        }

                    } catch (Exception e) {

                        e.printStackTrace();
                    }
                    System.out.println("return map here " + observable.getProxyReturnMap());
                    displayTransDetail(observable.getProxyReturnMap());
                }


            } else {
                ClientUtil.showMessageWindow("Please specify Cash or Transfer");
            }
        } else {
            //added by Rishad For Deposit To Locker
            try {
                ArrayList arbList = new ArrayList();
             for (int i = 0; i < tblLockerRentSIApplication.getRowCount(); i++) {
                    if (((Boolean) tblLockerRentSIApplication.getValueAt(i, 0)).booleanValue()) {
                        ArrayList list = new ArrayList();
                        list.add(tblLockerRentSIApplication.getValueAt(i, 1));
                        list.add(tblLockerRentSIApplication.getValueAt(i, 5));
                        list.add(tblLockerRentSIApplication.getValueAt(i, 6));
                        list.add(tblLockerRentSIApplication.getValueAt(i, 7));
                        list.add(tblLockerRentSIApplication.getValueAt(i, 8));
                        list.add(tblLockerRentSIApplication.getValueAt(i, 10));
                        list.add(tblLockerRentSIApplication.getValueAt(i, 11));
                        list.add(tblLockerRentSIApplication.getValueAt(i, 12));
                        list.add(tblLockerRentSIApplication.getValueAt(i, 3));
                        arbList.add(list);
                    }
                }
                HashMap processMap = new HashMap();
                processMap.put("LOCKER_RENT_DEPOSIT_LIST", arbList);
                processMap.put("PROD_ID", CommonUtil.convertObjToStr(((ComboBoxModel) (cboLockerType.getModel())).getKeyForSelected()));
                processMap.put(CommonConstants.BRANCH_ID, TrueTransactMain.BRANCH_ID);
                processMap.put("BRANCH_ID", TrueTransactMain.selBranch);
                processMap.put("MODE", "INSERT");
                processMap.put(CommonConstants.MODULE, getModule());
                processMap.put(CommonConstants.SCREEN, getScreen());
                processMap.put(CommonConstants.USER_ID, TrueTransactMain.USER_ID);
                processMap.put("MONTH", txtRentDueAsOnMM.getText());
                processMap.put("YEAR", txtRentDueAsOnyyyy.getText());
                processMap.put("SERVIC_TAX_REQ", TrueTransactMain.SERVICE_TAX_REQ);
                observable.setProcessMap(processMap);
                CommonUtil comm = new CommonUtil();
                final JDialog loading = comm.addProgressBar();
                SwingWorker<Void, Void> worker = new SwingWorker<Void, Void>() {

                    @Override
                    protected Void doInBackground() throws InterruptedException, Exception /**
                     * Execute some operation
                     */
                    {
                        observable.doActionPerformed();
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
        } //Progress bar code ends here 
                  if (observable.getResult() != ClientConstants.ACTIONTYPE_FAILED) {
                    if (observable.getProxyReturnMap() == null || observable.getProxyReturnMap().size() == 0) {
                        ClientUtil.showMessageWindow("  Completed With Exception!!! ");

                    } else {
                        ArrayList tableTitle = new ArrayList();
                        tableTitle.add("LOCKER NUMBER");
                        tableTitle.add("DEPOSIT_NUMBER");
                        tableTitle.add("BATCH_ID");
                        tableTitle.add("TRANSMODE");
                        tableTitle.add("SINGLE_TRANS_ID");
                        returnMap = observable.getProxyReturnMap();
                        System.out.println("returnMap" + returnMap);
                        ArrayList errorList = (ArrayList) returnMap.get("error");
                        observable.insertTableData(errorList, tableTitle);
                        tblLockerRentSIApplication.setModel(observable.getTblLockerRentSIApplication());
                        System.out.println("ErrorList" + errorList);
                        List transList = new ArrayList();
                        if (errorList != null && errorList.size() > 0) {
                            for (int j = 0; j < errorList.size(); j++) {
                                if (errorList.get(j) instanceof HashMap) {
                                    returnMap = (HashMap) errorList.get(j);
                                } else {
                                    transList.add(errorList.get(j));
                                }
                            }
                            if (transList.size() > 0) {
                                String toTransId = "";
                                List lst = (List) transList.get(0);
                                String fromTransId = CommonUtil.convertObjToStr(lst.get(2));
                                String message = "Please note the Transactions ID :   " + fromTransId;
                                if (transList.size() > 1) {
                                    toTransId = CommonUtil.convertObjToStr(((List) transList.get(transList.size()-1)).get(2));
                                    message = "Please note the Transactions IDs" + "\n" + "From Trans ID :   " + fromTransId + "\n" + "To Trans ID    :   " + toTransId;
                                    //message = "PLEASE NOTE THE TRANSACTION IDs" + toTransId;
                                }
                                ClientUtil.showMessageWindow(message);
                            }
                        }
                    }

                }
            } catch (Exception ex) {
                ex.printStackTrace();
            }
            displayTransDetail(observable.getProxyReturnMap());
        }
    }//GEN-LAST:event_btnProcessActionPerformed
    /*
    private void displayTransDetail(HashMap proxyResultMap) {
    System.out.println("@#$@@$@@@$ proxyResultMap : " + proxyResultMap);
    String cashDisplayStr = "Cash Transaction Details...\n";
    String transferDisplayStr = "Transfer Transaction Details...\n";
    String displayStr = "";
    String transId = "";
    String transType = "";
    Object keys[] = proxyResultMap.keySet().toArray();
    int cashCount = 0;
    int transferCount = 0;
    List tempList = null;
    List tempList1 = null;
    HashMap transMap = null;
    HashMap transIdMap = new HashMap();
    HashMap transTypeMap = new HashMap();
    String actNum = "";
    
    tempList = (List) proxyResultMap.get("error");
    tempList1= (List) tempList.get(0);
    System.out.println("temp list here" + tempList1);
    
    int yesNo = 0;
    String[] options = {"Yes", "No"};
    yesNo = COptionPane.showOptionDialog(null, "Do you want to print?", CommonConstants.WARNINGTITLE,
    COptionPane.OK_OPTION, COptionPane.WARNING_MESSAGE,
    null, options, options[0]);
    System.out.println("#$#$$ yesNo : " + yesNo);
    if (yesNo == 0) {
    TTIntegration ttIntgration = null;
    HashMap paramMap = new HashMap();
    paramMap.put("TransDt", observable.getCurrDt());
    paramMap.put("BranchId", ProxyParameters.BRANCH_ID);
    paramMap.put("TransId", CommonUtil.convertObjToStr(tempList1.get(1)));
    ttIntgration.setParam(paramMap);
    if (CommonUtil.convertObjToStr(tempList1.get(2)).equals("TRANSFER")) {
    System.out.println("in transfer");
    ttIntgration.integrationForPrint("ReceiptPayment");
    } else if (CommonUtil.convertObjToStr(tempList1.get(2)).equals("DEBIT")) {
    ttIntgration.integrationForPrint("CashPayment", false);
    } else {
    ttIntgration.integrationForPrint("CashReceipt", false);
    }
    
    }
    }
     * */

    private void displayTransDetail(HashMap proxyResultMap) {
        System.out.println("@#$@@$@@@$ proxyResultMap new: " + proxyResultMap);
        List list = (List) proxyResultMap.get("error");
        System.out.println("list here" + list);
        List list1 = (List) list.get(0);
        String transid = CommonUtil.convertObjToStr(list1.get(list1.size() - 1));
        String transType = CommonUtil.convertObjToStr(list1.get(list1.size() - 2));
        System.out.println("lastValuelastValuelastValue" + transid);
        System.out.println("trans type" + transType);
        int yesNo = 0;
        String[] options = {"Yes", "No"};
        yesNo = COptionPane.showOptionDialog(null, "Do you want to print?", CommonConstants.WARNINGTITLE,
                COptionPane.OK_OPTION, COptionPane.WARNING_MESSAGE,
                null, options, options[0]);
        //system.out.println("#$#$$ yesNo : " + yesNo);
        if (yesNo == 0) {
            TTIntegration ttIntgration = null;
            HashMap reportTransIdMap = new HashMap();
            reportTransIdMap.put("TransId", transid);
            reportTransIdMap.put("TransDt", currDate.clone());
            reportTransIdMap.put("BranchId", ProxyParameters.BRANCH_ID);
            ttIntgration.setParam(reportTransIdMap);
            //System.out.println("inside print" +transType);
            if (transType.equals("TRANSFER")) {
                ttIntgration.integrationForPrint("ReceiptPayment");
            } else if (transType.equals("CASH")) {
                ttIntgration.integrationForPrint("CashPayment");
            } else {
                ttIntgration.integrationForPrint("CashReceipt");
            }

        }

    }

    private void chkSelectAllActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_chkSelectAllActionPerformed
       
        if (chkSelectAll.isSelected() == true) {
            flag = true;
        } else {
            flag = false;
        }
      //  System.out.println("Flag value============"+flag);
        for (int i = 0; i < tblLockerRentSIApplication.getRowCount(); i++) {
            tblLockerRentSIApplication.setValueAt(new Boolean(flag), i, 0);
            
        }
        if (!chkFdToLocker.isSelected()) {
            double gst = 0.0;
            for (int i = 0; i < tblLockerRentSIApplication.getRowCount(); i++) {
                gst = CommonUtil.convertObjToDouble(tblLockerRentSIApplication.getValueAt(i, 11));
                double total = CommonUtil.convertObjToDouble(tblLockerRentSIApplication.getValueAt(i, 4))
                        + CommonUtil.convertObjToDouble(tblLockerRentSIApplication.getValueAt(i, 5))
                        + CommonUtil.convertObjToDouble(tblLockerRentSIApplication.getValueAt(i, 6)) + gst;
                if (total > CommonUtil.convertObjToDouble(tblLockerRentSIApplication.getValueAt(i, 9))) {
                    tblLockerRentSIApplication.setValueAt(new Boolean(false), i, 0);
                }
            }
        }        
    }//GEN-LAST:event_chkSelectAllActionPerformed

    private void chkFdToLockerActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_chkFdToLockerActionPerformed
        // TODO add your handling code here:
        if (chkFdToLocker.isSelected()) {
            rdoCash.setVisible(false);
        } else {
            rdoCash.setVisible(true);
        }
    }//GEN-LAST:event_chkFdToLockerActionPerformed

    private void rdoCashActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_rdoCashActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_rdoCashActionPerformed
    
    
    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private com.see.truetransact.uicomponent.CButton btnClear1;
    private com.see.truetransact.uicomponent.CButton btnClose;
    private com.see.truetransact.uicomponent.CButton btnProcess;
    private com.see.truetransact.uicomponent.CButton btnProcess1;
    private com.see.truetransact.uicomponent.CComboBox cboLockerType;
    private javax.swing.JCheckBox chkFdToLocker;
    private com.see.truetransact.uicomponent.CCheckBox chkSelectAll;
    private com.see.truetransact.uicomponent.CLabel lblCustName;
    private com.see.truetransact.uicomponent.CLabel lblLockerType;
    private com.see.truetransact.uicomponent.CLabel lblMsg;
    private com.see.truetransact.uicomponent.CLabel lblProductName;
    private com.see.truetransact.uicomponent.CLabel lblRentDueAsOn;
    private com.see.truetransact.uicomponent.CLabel lblRentDueAsOn1;
    private com.see.truetransact.uicomponent.CLabel lblSelectAll;
    private com.see.truetransact.uicomponent.CLabel lblSpace1;
    private com.see.truetransact.uicomponent.CLabel lblStatus;
    private com.see.truetransact.uicomponent.CLabel lblTotalTransactionAmtVal;
    private com.see.truetransact.uicomponent.CPanel panCustID;
    private com.see.truetransact.uicomponent.CPanel panDepositInterestApplication;
    private com.see.truetransact.uicomponent.CPanel panOperations1;
    private com.see.truetransact.uicomponent.CPanel panProcess;
    private com.see.truetransact.uicomponent.CPanel panProductDetails;
    private com.see.truetransact.uicomponent.CPanel panProductID;
    private com.see.truetransact.uicomponent.CPanel panProductTableData;
    private com.see.truetransact.uicomponent.CPanel panSINumber;
    private com.see.truetransact.uicomponent.CPanel panSelectAll;
    private com.see.truetransact.uicomponent.CPanel panStatus;
    private com.see.truetransact.uicomponent.CPanel panTransType;
    private com.see.truetransact.uicomponent.CRadioButton rdoCash;
    private com.see.truetransact.uicomponent.CRadioButton rdoTransfer;
    private com.see.truetransact.uicomponent.CScrollPane srpLockerRentSIApplication;
    private com.see.truetransact.uicomponent.CTable tblLockerRentSIApplication;
    private com.see.truetransact.uicomponent.CTextField txtRentDueAsOnMM;
    private com.see.truetransact.uicomponent.CTextField txtRentDueAsOnyyyy;
    // End of variables declaration//GEN-END:variables
    
    public static void main(String[] args) {
        LockerRentSIApplicationUI fad = new LockerRentSIApplicationUI();
        javax.swing.JFrame j = new javax.swing.JFrame();
        j.getContentPane().add(fad);
        j.show();
        fad.show();
    }
    public long getNearest(long number,long roundingFactor)  {
        long roundingFactorOdd = roundingFactor;
        if ((roundingFactor%2) != 0) {
            roundingFactorOdd +=1;
        }
        long mod = number%roundingFactor;
        if ((mod < (roundingFactor/2)) || (mod < (roundingFactorOdd/2))) {
            return lower(number,roundingFactor);
        } else {
            return higher(number,roundingFactor);
        }
    }
    public long lower(long number,long roundingFactor) {
        long mod = number%roundingFactor;
        return number-mod;
    }
    
    public long higher(long number,long roundingFactor) {
        long mod = number%roundingFactor;
        if ( mod == 0) {
            return number;
        }
        return (number-mod) + roundingFactor ;
    }
    
    public java.lang.String getSurrenderID() {
        return surrenderID;
    }
    
    /**
     * Setter for property surrenderID.
     * @param surrenderID New value of property surrenderID.
     */
    public void setSurrenderID(java.lang.String surrenderID) {
        this.surrenderID = surrenderID;
    }
    
    
    private HashMap getGSTAmountMap(HashMap checkForTaxMap){
        HashMap taxMap = new HashMap();
        if (checkForTaxMap.containsKey("SERVICE_TAX_APPLICABLE") && checkForTaxMap.get("SERVICE_TAX_APPLICABLE") != null && CommonUtil.convertObjToStr(checkForTaxMap.get("SERVICE_TAX_APPLICABLE")).equalsIgnoreCase("Y")) {
            if (checkForTaxMap.containsKey("SERVICE_TAX_ID") && checkForTaxMap.get("SERVICE_TAX_ID") != null && CommonUtil.convertObjToStr(checkForTaxMap.get("SERVICE_TAX_ID")).length() > 0) {
               taxMap.put("SETTINGS_ID", checkForTaxMap.get("SERVICE_TAX_ID"));    
            }
        }
        return taxMap;
    }
    
    
    
}
