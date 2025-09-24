/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition.
 * 
 *
 * EmptransferUI.java
 *
 * Created on feb 9, 2009, 10:53 AM
 */

package com.see.truetransact.ui.boardresolutiondetails;

import java.util.HashMap;
import java.util.Observable;
import org.apache.log4j.Logger;
import com.see.truetransact.commonutil.CommonConstants;
import com.see.truetransact.uimandatory.UIMandatoryField;
import com.see.truetransact.clientutil.ComboBoxModel;
import com.see.truetransact.commonutil.TTException;
import com.see.truetransact.clientutil.ClientUtil;
import com.see.truetransact.clientutil.CMandatoryDialog;
import com.see.truetransact.uimandatory.MandatoryCheck;
import com.see.truetransact.clientutil.ClientConstants;
import com.see.truetransact.uivalidation.NumericValidation;
import com.see.truetransact.uivalidation.CurrencyValidation;
import java.util.ArrayList;
import java.util.List;
import com.see.truetransact.commonutil.CommonUtil;
import com.see.truetransact.clientproxy.ProxyParameters;
import com.see.truetransact.ui.common.transaction.TransactionUI;
import com.see.truetransact.ui.common.authorize.AuthorizeUI ;
import com.see.truetransact.ui.TrueTransactMain;
import com.see.truetransact.ui.common.viewall.ViewAll;
import com.see.truetransact.ui.customer.customeridchange.CustomerIdChangeOB;
import com.see.truetransact.commonutil.DateUtil;
import com.see.truetransact.ui.common.authorizestatus.AuthorizeStatusUI;
import java.util.Date;
import com.see.truetransact.uimandatory.UIMandatoryField;
import com.see.truetransact.uimandatory.MandatoryCheck;
import com.see.truetransact.uimandatory.MandatoryDBCheck;
import com.see.truetransact.uicomponent.CTextField;
import com.see.truetransact.uivalidation.*;
import com.see.truetransact.uicomponent.CButtonGroup;
import javax.swing.JScrollPane;
import javax.swing.ScrollPaneConstants;




/** This form is used to manipulate CustomerIdChangeUI related functionality
 * @author swaroop
 */
public class BoardResolutionDetailsUI extends com.see.truetransact.uicomponent.CInternalFrame implements java.util.Observer, UIMandatoryField{

    private String viewType = new String();
    private HashMap mandatoryMap;
    private BoardResolutionDetailsOB observable;
    final String AUTHORIZE="Authorize";
    private final static Logger log = Logger.getLogger(BoardResolutionDetailsUI.class);
    BoardResolutionDetailsRB BoardResolutionRB = new  BoardResolutionDetailsRB();
    private String prodType="";
    java.util.ResourceBundle resourceBundle, objMandatoryRB;
    String cust_type=null;
    String resolutionid="";
    private Date currDt = null;
    /** Creates new form CustomerIdChangeUI */
    public BoardResolutionDetailsUI() {
        currDt = ClientUtil.getCurrentDate();
        initComponents();
        initStartUp();
    }
    
    private void initStartUp(){
//    srpDetails.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
//       srpDetails.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_ALWAYS);

        setMandatoryHashMap();
        setFieldNames();
        internationalize();
        observable = new BoardResolutionDetailsOB();
        observable.addObserver(this);
        initComponentData();
        enableDisable(false);
        setButtonEnableDisable();
        setMaxLength();
         objMandatoryRB = java.util.ResourceBundle.getBundle("com.see.truetransact.ui.boardresolutiondetails.BoardResolutionDetailsMRB", ProxyParameters.LANGUAGE);
        new MandatoryCheck().putMandatoryMarks(getClass().getName(), panOutward);
         setHelpMessage();
    }
    
    private void setMaxLength() {

    }
    
    public void setMandatoryHashMap() {
        mandatoryMap = new HashMap();
        mandatoryMap.put("txaDetails", new Boolean(true));
        mandatoryMap.put("tdtDate", new Boolean(true));
        mandatoryMap.put("txaRemarks", new Boolean(false));
        mandatoryMap.put("txtReferenceNo", new Boolean(true));
           }
    
    public HashMap getMandatoryHashMap() {
        return mandatoryMap;
    }
    
    
    
   
    public void setHelpMessage() {
    }
    
   
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {
        java.awt.GridBagConstraints gridBagConstraints;

        panOutwardRegister = new com.see.truetransact.uicomponent.CPanel();
        panOutward = new com.see.truetransact.uicomponent.CPanel();
        lblDate = new com.see.truetransact.uicomponent.CLabel();
        lblRemarks = new com.see.truetransact.uicomponent.CLabel();
        tdtDate = new com.see.truetransact.uicomponent.CDateField();
        lblReferenceNo = new javax.swing.JLabel();
        txtReferenceNo = new javax.swing.JTextField();
        scrRemarks = new javax.swing.JScrollPane();
        txaRemarks = new com.see.truetransact.uicomponent.CTextArea();
        tbrInwardRegister = new javax.swing.JToolBar();
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
        lblSpace76 = new com.see.truetransact.uicomponent.CLabel();
        btnDateChange = new com.see.truetransact.uicomponent.CButton();
        panStatus = new com.see.truetransact.uicomponent.CPanel();
        lblSpace1 = new com.see.truetransact.uicomponent.CLabel();
        lblStatus = new com.see.truetransact.uicomponent.CLabel();
        lblMsg = new com.see.truetransact.uicomponent.CLabel();
        mbrCustomer = new com.see.truetransact.uicomponent.CMenuBar();
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
        setMaximumSize(new java.awt.Dimension(800, 625));
        setMinimumSize(new java.awt.Dimension(800, 625));
        setPreferredSize(new java.awt.Dimension(800, 625));

        panOutwardRegister.setMaximumSize(new java.awt.Dimension(650, 520));
        panOutwardRegister.setMinimumSize(new java.awt.Dimension(650, 520));
        panOutwardRegister.setPreferredSize(new java.awt.Dimension(650, 520));
        panOutwardRegister.setLayout(new java.awt.GridBagLayout());

        panOutward.setBorder(javax.swing.BorderFactory.createTitledBorder(""));
        panOutward.setEnabled(false);
        panOutward.setMinimumSize(new java.awt.Dimension(600, 400));
        panOutward.setName("panMaritalStatus"); // NOI18N
        panOutward.setPreferredSize(new java.awt.Dimension(600, 400));
        panOutward.setLayout(new java.awt.GridBagLayout());

        lblDate.setText("Resolution Date ");
        lblDate.setFont(new java.awt.Font("DejaVu Sans", 0, 13)); // NOI18N
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(3, 3, 3, 3);
        panOutward.add(lblDate, gridBagConstraints);
        lblDate.getAccessibleContext().setAccessibleName("Date   ");

        lblRemarks.setText("Remarks");
        lblRemarks.setFont(new java.awt.Font("DejaVu Sans", 0, 13)); // NOI18N
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 7;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.gridheight = 2;
        gridBagConstraints.ipadx = 22;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(3, 3, 3, 3);
        panOutward.add(lblRemarks, gridBagConstraints);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(3, 3, 3, 3);
        panOutward.add(tdtDate, gridBagConstraints);

        lblReferenceNo.setFont(new java.awt.Font("DejaVu Sans", 0, 12)); // NOI18N
        lblReferenceNo.setText("Resolution Number ");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(3, 3, 3, 3);
        panOutward.add(lblReferenceNo, gridBagConstraints);

        txtReferenceNo.setMinimumSize(new java.awt.Dimension(101, 21));
        txtReferenceNo.setPreferredSize(new java.awt.Dimension(101, 21));
        txtReferenceNo.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                txtReferenceNoFocusLost(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.SOUTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(3, 3, 3, 3);
        panOutward.add(txtReferenceNo, gridBagConstraints);

        scrRemarks.setMinimumSize(new java.awt.Dimension(200, 50));
        scrRemarks.setPreferredSize(new java.awt.Dimension(240, 160));

        txaRemarks.setColumns(20);
        txaRemarks.setRows(5);
        txaRemarks.setMinimumSize(new java.awt.Dimension(17, 72));
        txaRemarks.setPreferredSize(new java.awt.Dimension(500, 288));
        scrRemarks.setViewportView(txaRemarks);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 7;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(3, 3, 3, 3);
        panOutward.add(scrRemarks, gridBagConstraints);

        panOutwardRegister.add(panOutward, new java.awt.GridBagConstraints());

        getContentPane().add(panOutwardRegister, java.awt.BorderLayout.CENTER);

        btnView.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_Summary.gif"))); // NOI18N
        btnView.setToolTipText("Enquiry");
        btnView.setMinimumSize(new java.awt.Dimension(21, 21));
        btnView.setPreferredSize(new java.awt.Dimension(21, 21));
        btnView.setEnabled(false);
        btnView.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnViewActionPerformed(evt);
            }
        });
        tbrInwardRegister.add(btnView);

        lblSpace5.setText("     ");
        tbrInwardRegister.add(lblSpace5);

        btnNew.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_NEW.gif"))); // NOI18N
        btnNew.setToolTipText("New");
        btnNew.setEnabled(false);
        btnNew.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnNewActionPerformed(evt);
            }
        });
        tbrInwardRegister.add(btnNew);

        lblSpace70.setForeground(new java.awt.Color(240, 240, 240));
        lblSpace70.setText("     ");
        lblSpace70.setMaximumSize(new java.awt.Dimension(3, 18));
        lblSpace70.setMinimumSize(new java.awt.Dimension(3, 18));
        lblSpace70.setPreferredSize(new java.awt.Dimension(3, 18));
        tbrInwardRegister.add(lblSpace70);

        btnEdit.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_EDIT.gif"))); // NOI18N
        btnEdit.setToolTipText("Edit");
        btnEdit.setEnabled(false);
        btnEdit.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnEditActionPerformed(evt);
            }
        });
        tbrInwardRegister.add(btnEdit);

        lblSpace71.setForeground(new java.awt.Color(240, 240, 240));
        lblSpace71.setText("     ");
        lblSpace71.setMaximumSize(new java.awt.Dimension(3, 18));
        lblSpace71.setMinimumSize(new java.awt.Dimension(3, 18));
        lblSpace71.setPreferredSize(new java.awt.Dimension(3, 18));
        tbrInwardRegister.add(lblSpace71);

        btnDelete.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_DELETE.gif"))); // NOI18N
        btnDelete.setToolTipText("Delete");
        btnDelete.setEnabled(false);
        btnDelete.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnDeleteActionPerformed(evt);
            }
        });
        tbrInwardRegister.add(btnDelete);

        lblSpace2.setText("     ");
        tbrInwardRegister.add(lblSpace2);

        btnSave.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_SAVE.gif"))); // NOI18N
        btnSave.setToolTipText("Save");
        btnSave.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSaveActionPerformed(evt);
            }
        });
        tbrInwardRegister.add(btnSave);

        lblSpace72.setForeground(new java.awt.Color(240, 240, 240));
        lblSpace72.setText("     ");
        lblSpace72.setMaximumSize(new java.awt.Dimension(3, 18));
        lblSpace72.setMinimumSize(new java.awt.Dimension(3, 18));
        lblSpace72.setPreferredSize(new java.awt.Dimension(3, 18));
        tbrInwardRegister.add(lblSpace72);

        btnCancel.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_CANCEL.gif"))); // NOI18N
        btnCancel.setToolTipText("Cancel");
        btnCancel.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCancelActionPerformed(evt);
            }
        });
        tbrInwardRegister.add(btnCancel);

        lblSpace3.setText("     ");
        tbrInwardRegister.add(lblSpace3);

        btnAuthorize.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_AUTHORIZE.gif"))); // NOI18N
        btnAuthorize.setToolTipText("Authorize");
        btnAuthorize.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnAuthorizeActionPerformed(evt);
            }
        });
        tbrInwardRegister.add(btnAuthorize);

        lblSpace73.setForeground(new java.awt.Color(240, 240, 240));
        lblSpace73.setText("     ");
        lblSpace73.setMaximumSize(new java.awt.Dimension(3, 18));
        lblSpace73.setMinimumSize(new java.awt.Dimension(3, 18));
        lblSpace73.setPreferredSize(new java.awt.Dimension(3, 18));
        tbrInwardRegister.add(lblSpace73);

        btnException.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_EXCEPTION.gif"))); // NOI18N
        btnException.setToolTipText("Exception");
        btnException.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnExceptionActionPerformed(evt);
            }
        });
        tbrInwardRegister.add(btnException);

        lblSpace74.setForeground(new java.awt.Color(240, 240, 240));
        lblSpace74.setText("     ");
        lblSpace74.setMaximumSize(new java.awt.Dimension(3, 18));
        lblSpace74.setMinimumSize(new java.awt.Dimension(3, 18));
        lblSpace74.setPreferredSize(new java.awt.Dimension(3, 18));
        tbrInwardRegister.add(lblSpace74);

        btnReject.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_REJECT.gif"))); // NOI18N
        btnReject.setToolTipText("Reject");
        btnReject.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnRejectActionPerformed(evt);
            }
        });
        tbrInwardRegister.add(btnReject);

        lblSpace4.setText("     ");
        tbrInwardRegister.add(lblSpace4);

        btnPrint.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_PRINT.gif"))); // NOI18N
        btnPrint.setToolTipText("Print");
        tbrInwardRegister.add(btnPrint);

        lblSpace75.setForeground(new java.awt.Color(240, 240, 240));
        lblSpace75.setText("     ");
        lblSpace75.setMaximumSize(new java.awt.Dimension(3, 18));
        lblSpace75.setMinimumSize(new java.awt.Dimension(3, 18));
        lblSpace75.setPreferredSize(new java.awt.Dimension(3, 18));
        tbrInwardRegister.add(lblSpace75);

        btnClose.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_CLOSE.gif"))); // NOI18N
        btnClose.setToolTipText("Close");
        btnClose.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCloseActionPerformed(evt);
            }
        });
        tbrInwardRegister.add(btnClose);

        lblSpace76.setForeground(new java.awt.Color(240, 240, 240));
        lblSpace76.setText("     ");
        lblSpace76.setMaximumSize(new java.awt.Dimension(3, 18));
        lblSpace76.setMinimumSize(new java.awt.Dimension(3, 18));
        lblSpace76.setPreferredSize(new java.awt.Dimension(3, 18));
        tbrInwardRegister.add(lblSpace76);

        btnDateChange.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/denomination.gif"))); // NOI18N
        btnDateChange.setToolTipText("Exception");
        btnDateChange.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnDateChangeActionPerformed(evt);
            }
        });
        tbrInwardRegister.add(btnDateChange);

        getContentPane().add(tbrInwardRegister, java.awt.BorderLayout.NORTH);

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

        mbrCustomer.setName("mbrCustomer"); // NOI18N

        mnuProcess.setText("Process");
        mnuProcess.setName("mnuProcess"); // NOI18N

        mitNew.setText("New");
        mitNew.setName("mitNew"); // NOI18N
        mitNew.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mitNewActionPerformed(evt);
            }
        });
        mnuProcess.add(mitNew);

        mitEdit.setText("Edit");
        mitEdit.setName("mitEdit"); // NOI18N
        mitEdit.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mitEditActionPerformed(evt);
            }
        });
        mnuProcess.add(mitEdit);

        mitDelete.setText("Delete");
        mitDelete.setName("mitDelete"); // NOI18N
        mitDelete.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mitDeleteActionPerformed(evt);
            }
        });
        mnuProcess.add(mitDelete);

        sptNew.setName("sptNew"); // NOI18N
        mnuProcess.add(sptNew);

        mitSave.setText("Save");
        mitSave.setName("mitSave"); // NOI18N
        mitSave.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mitSaveActionPerformed(evt);
            }
        });
        mnuProcess.add(mitSave);

        mitCancel.setText("Cancel");
        mitCancel.setName("mitCancel"); // NOI18N
        mitCancel.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mitCancelActionPerformed(evt);
            }
        });
        mnuProcess.add(mitCancel);

        sptSave.setName("sptSave"); // NOI18N
        mnuProcess.add(sptSave);

        mitPrint.setText("Print");
        mitPrint.setName("mitPrint"); // NOI18N
        mnuProcess.add(mitPrint);

        mitClose.setText("Close");
        mitClose.setName("mitClose"); // NOI18N
        mnuProcess.add(mitClose);

        mbrCustomer.add(mnuProcess);

        setJMenuBar(mbrCustomer);

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btnDateChangeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnDateChangeActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_btnDateChangeActionPerformed

    private void btnViewActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnViewActionPerformed
       
    }//GEN-LAST:event_btnViewActionPerformed
                
    private void btnExceptionActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnExceptionActionPerformed

    }//GEN-LAST:event_btnExceptionActionPerformed
    
    private void btnRejectActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnRejectActionPerformed

    }//GEN-LAST:event_btnRejectActionPerformed
    
    private void btnAuthorizeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAuthorizeActionPerformed
observable.setActionType(ClientConstants.ACTIONTYPE_AUTHORIZE);
        authorizeStatus(CommonConstants.STATUS_AUTHORIZED);
   //     updateAuthorizeStatus(CommonConstants.STATUS_AUTHORIZED);
        btnCancel.setEnabled(true);
        btnAuthorize.setEnabled(true);
        btnReject.setEnabled(false);
        btnException.setEnabled(false);
    }//GEN-LAST:event_btnAuthorizeActionPerformed
    
      public void authorizeStatus(String authorizeStatus) {
        if (!viewType.equals(AUTHORIZE)){
            viewType = AUTHORIZE;
            //            setModified(true);
            HashMap mapParam = new HashMap();
            HashMap whereMap = new HashMap();
            whereMap.put(CommonConstants.USER_ID,TrueTransactMain.USER_ID);
            whereMap.put(CommonConstants.BRANCH_ID,TrueTransactMain.BRANCH_ID);
            whereMap.put(CommonConstants.AUTHORIZESTATUS, authorizeStatus);
            mapParam.put(CommonConstants.MAP_WHERE, whereMap);
            System.out.println("mapParam1111>>>>>>>"+mapParam);
            whereMap = null;
            mapParam.put(CommonConstants.MAP_NAME, "getBoardResolutionAuthorizeList");
            mapParam.put(CommonConstants.UPDATE_MAP_NAME, "authorizeBoardResolution");
            System.out.println("mapParam22222>>>>>>>"+mapParam);
            AuthorizeStatusUI authorizeUI = new AuthorizeStatusUI(this, mapParam);
            authorizeUI.show();
            observable.setStatus();
            btnCancel.setEnabled(true);
            btnSave.setEnabled(false);
        } else if (viewType.equals(AUTHORIZE)){
            HashMap singleAuthorizeMap = new HashMap();
            ArrayList arrList = new ArrayList();
            HashMap authorizeMap = new HashMap();
            //    HashMap singleAuthorizeMap = new HashMap();
            singleAuthorizeMap.put("STATUS", authorizeStatus);
            singleAuthorizeMap.put("AUTH_BY", TrueTransactMain.USER_ID);
            singleAuthorizeMap.put("AUTH_DT",ClientUtil.getCurrentDateWithTime());
            //   singleAuthorizeMap.put("DEPR_BATCH_ID", observable.getDeprBatchId());
            singleAuthorizeMap.put("PAN","DEPR");
            singleAuthorizeMap.put(CommonConstants.STATUS, authorizeStatus);
            singleAuthorizeMap.put(CommonConstants.USER_ID, ProxyParameters.USER_ID);//commented on 13-Feb-2012
            singleAuthorizeMap.put(CommonConstants.AUTHORIZEDT, currDt.clone());
            singleAuthorizeMap.put("RESOLUTION_ID",resolutionid);
            singleAuthorizeMap.put(CommonConstants.BRANCH_ID,TrueTransactMain.BRANCH_ID);
            System.out.println("in singleAuthorizeMap>>>>>"+singleAuthorizeMap);
            ClientUtil.execute("authorizeBoardResolution", singleAuthorizeMap);
            //                singleAuthorizeMap.put("DEPRECIATION_DATE",ClientUtil.getCurrentDateWithTime());
            //                singleAuthorizeMap.put("BRANCH_CODE", observable.getDeprBranchCode());
            arrList.add(singleAuthorizeMap);
            authorizeMap.put(CommonConstants.AUTHORIZESTATUS, authorizeStatus);
            authorizeMap.put(CommonConstants.AUTHORIZEDATA, arrList);
            
            observable.set_authorizeMap(authorizeMap);
            //  observable.execute(authorizeStatus);
            // ClientUtil.ex
            viewType = "";
            //            super.setOpenForEditBy(observable.getStatusBy());
            //            super.removeEditLock(txtTokenConfigId.getText());
            btnCancelActionPerformed(null);
            //  lblStatus.setText(authorizeStatus);
        }
    }
    
    
    private void btnCloseActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCloseActionPerformed
        mitCloseActionPerformed(evt);
    }//GEN-LAST:event_btnCloseActionPerformed
    
    private void mitCloseActionPerformed(java.awt.event.ActionEvent evt) {
        cifClosingAlert();
    }
    
    private void btnDeleteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnDeleteActionPerformed
//       observable.setActionType(ClientConstants.ACTIONTYPE_DELETE);
//        observable.setStatus();
//        popUp("Delete");
////        txtOutwardNo.setVisible(true);
////    txtOutwardNo.setEditable(false);
//        lblStatus.setText(observable.getLblStatus());
    }//GEN-LAST:event_btnDeleteActionPerformed
    
    private void btnEditActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnEditActionPerformed
   observable.setActionType(ClientConstants.ACTIONTYPE_EDIT);
    observable.setStatus();
    popUp("Edit");

    lblStatus.setText(observable.getLblStatus());
    }//GEN-LAST:event_btnEditActionPerformed
    
    private void mitCancelActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mitCancelActionPerformed
        btnCancelActionPerformed(evt);
    }//GEN-LAST:event_mitCancelActionPerformed
    
    private void btnCancelActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCancelActionPerformed
       // deletescreenLock();
        viewType = "CANCEL" ;
        observable.resetForm();
        ClientUtil.clearAll(this);
        setButtonEnableDisable();
        ClientUtil.enableDisable(this, false);
        lblStatus.setText("");
        setModified(false);
        
    }//GEN-LAST:event_btnCancelActionPerformed
    
    
    private void btnSaveActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSaveActionPerformed
        updateOBFields();
        setModified(false);
       
        String mandatoryMessage = new MandatoryCheck().checkMandatory(getClass().getName(), panOutward);
        if( observable.getActionType() != ClientConstants.ACTIONTYPE_DELETE && mandatoryMessage.length() > 0 ){
            displayAlert(mandatoryMessage);
        }
        else{
            savePerformed();
        }
    }//GEN-LAST:event_btnSaveActionPerformed
    
    private void btnNewActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnNewActionPerformed
        enableDisable(true);
        setButtonEnableDisable();
        setModified(true);
       // btnEmp.setEnabled(true);
        observable.setActionType(ClientConstants.ACTIONTYPE_NEW);
        observable.resetForm();
        observable.setStatus();
        lblStatus.setText(observable.getLblStatus());
        
        
    }//GEN-LAST:event_btnNewActionPerformed
    
    private void mitDeleteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mitDeleteActionPerformed
        btnDeleteActionPerformed(evt);
    }//GEN-LAST:event_mitDeleteActionPerformed
    
    private void mitEditActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mitEditActionPerformed
        btnEditActionPerformed(evt);
    }//GEN-LAST:event_mitEditActionPerformed
    
    private void mitNewActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mitNewActionPerformed
        btnNewActionPerformed(evt);
    }//GEN-LAST:event_mitNewActionPerformed
    
    private void mitSaveActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mitSaveActionPerformed
        btnSaveActionPerformed(evt);
    }//GEN-LAST:event_mitSaveActionPerformed

private void txtReferenceNoFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtReferenceNoFocusLost
String tempid=txtReferenceNo.getText();
 HashMap mapParam = new HashMap(); 
 HashMap whereMap = new HashMap();
  whereMap.put("TEMPID",tempid);
   //  mapParam.put(CommonConstants.MAP_WHERE, whereMap);
   //  whereMap=null;
    //  mapParam.put(CommonConstants.MAP_NAME, "getBoardResolutionID");
List result =ClientUtil.executeQuery("getBoardResolutionID",whereMap);
System.out.println("List"+result);
  if(!result.isEmpty())
     {
          ClientUtil.showMessageWindow("Resolution ID already exist. : ");
txtReferenceNo.setText("");
grabFocus();
     }
        // TODO add your handling code here:
}//GEN-LAST:event_txtReferenceNoFocusLost
                
    /** To populate Comboboxes */
    private void initComponentData() {

    }
    
    /** To display a popUp window for viewing existing data */
    private void popUp(String currAction){
       viewType = currAction;
        HashMap viewMap = new HashMap();
        if (currAction.equalsIgnoreCase("Edit")){
            HashMap map = new HashMap();
            map.put("BRANCH_CODE",TrueTransactMain.BRANCH_ID);
           // map.put("BRANCH_CODE","0001");
            viewMap.put(CommonConstants.MAP_WHERE, map);
            viewMap.put(CommonConstants.MAP_NAME, "getBoardResolutionEdit"); 
        }else if(currAction.equalsIgnoreCase("Delete")){
            HashMap map = new HashMap();
            map.put("BRANCH_CODE",TrueTransactMain.BRANCH_ID);
            viewMap.put(CommonConstants.MAP_WHERE, map);
            viewMap.put(CommonConstants.MAP_NAME, "getBoardResolutionEdit");
        } else if(currAction.equalsIgnoreCase("EMP")){
            HashMap map = new HashMap();
            map.put("BRANCH_CODE",TrueTransactMain.BRANCH_ID);
            viewMap.put(CommonConstants.MAP_WHERE, map);
            viewMap.put(CommonConstants.MAP_NAME, "setEmpDetailsForTransfer");
        }
        else if(currAction.equalsIgnoreCase("Enquiry")){
            HashMap map = new HashMap();
            map.put("BRANCH_CODE",TrueTransactMain.BRANCH_ID);
            viewMap.put(CommonConstants.MAP_WHERE, map);
            viewMap.put(CommonConstants.MAP_NAME, "getEmpTransferDetailsView");
        }
        new ViewAll(this,viewMap).show();
       
    }

    
    /** Called by the Popup window created thru popUp method */
    public void fillData(Object map) {
        try{
           setModified(true);
        HashMap hash = (HashMap) map;
        if (viewType != null) {
            if (viewType.equals(ClientConstants.ACTION_STATUS[2]) ||
            viewType.equals(ClientConstants.ACTION_STATUS[3])|| viewType.equals(AUTHORIZE) ||
            viewType.equals(ClientConstants.ACTION_STATUS[17])) {
        
                hash.put(CommonConstants.BRANCH_ID, TrueTransactMain.BRANCH_ID);
                resolutionid=CommonUtil.convertObjToStr(hash.get("RESOLUTION_ID"));
                observable.getData(hash);
                if (viewType.equals(ClientConstants.ACTION_STATUS[3]) || viewType.equals(AUTHORIZE) ||
                viewType.equals(ClientConstants.ACTION_STATUS[17])) {
                    ClientUtil.enableDisable(panOutward, false);
                   
                } else {
                    ClientUtil.enableDisable(panOutward, true);
                 
                }
                setButtonEnableDisable();
                if(viewType ==  AUTHORIZE) {
                    btnAuthorize.setEnabled(observable.getActionType() == ClientConstants.ACTIONTYPE_AUTHORIZE ? true : false);
                    btnReject.setEnabled(observable.getActionType() == ClientConstants.ACTIONTYPE_REJECT ? true : false);
                    btnException.setEnabled(observable.getActionType() == ClientConstants.ACTIONTYPE_EXCEPTION ? true : false);
                }
            }
            
        }

        }catch(Exception e){
            log.error(e);
        }
    }
    
    
    private void enableDisable(boolean yesno){
        ClientUtil.enableDisable(this, yesno);
    }
    
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
        
        btnAuthorize.setEnabled(btnNew.isEnabled());
        btnReject.setEnabled(btnNew.isEnabled());
        btnException.setEnabled(btnNew.isEnabled());
        btnView.setEnabled(!btnView.isEnabled());
        
//        txtOutwardNo.setVisible(false);
    }
    
    
    public void update(Observable observed, Object arg) {
        // removeRadioButtons();

        tdtDate.setDateValue(observable.getTdtDate());
  
        txaRemarks.setText(observable.getTxaRemarks());

         txtReferenceNo.setText(observable.getTxtReferenceNo());

        lblStatus.setText(observable.getLblStatus());
    }
    
    public void updateOBFields(){
        
        observable.setTdtDate(tdtDate.getDateValue());
      //  observable.setTxaDetails(txaDetails.getText());
        observable.setTxaRemarks(txaRemarks.getText());
        
    
        observable.setTxtReferenceNo(txtReferenceNo.getText());
    }
    
    
    
    private void savePerformed(){
        
        updateOBFields();
        observable.doAction() ;
        if(observable.getResult() != ClientConstants.ACTIONTYPE_FAILED){
            HashMap lockMap = new HashMap();
            ArrayList lst = new ArrayList();
            // lst.add("EMP_TRANSFER_ID");
            //lockMap.put("EMP_TRANSFER_ID",CommonUtil.convertObjToStr(lblEmpTransferID.getText()));
            // lockMap.put(ClientConstants.RECORD_KEY_COL, lst);
            if(observable.getActionType()==ClientConstants.ACTIONTYPE_NEW) {
                //   lockMap.put(ClientConstants.RECORD_KEY_COL, lst);
                if (observable.getProxyReturnMap()!=null) {
//                    if (observable.getProxyReturnMap().containsKey("BOARD_MT_ID")) {
//                        //lockMap.put("EMP_TRANSFER_ID",observable.getProxyReturnMap().get("VISIT_ID"));
//                    }
                }
            }
            if(observable.getResult()==ClientConstants.ACTIONTYPE_EDIT){
                // lockMap.put("EMP_TRANSFER_ID", observable.getTxtEmpTransferID());
            }
            // setEditLockMap(lockMap);
            ////  setEditLock();
            //  deletescreenLock();
        }
        
        observable.resetForm();
        enableDisable(false);
        setButtonEnableDisable();
        lblStatus.setText(observable.getLblStatus());
        ClientUtil.enableDisable(this, false);
        observable.setResultStatus();
        lblStatus.setText(observable.getLblStatus());
        //__ Make the Screen Closable..
        setModified(false);
        ClientUtil.clearAll(this);
        observable.ttNotifyObservers();
        
    }
    private void setFieldNames() {
        tdtDate.setName("tdtDate");

       txaRemarks.setName("txaRemarks");

        txtReferenceNo.setName("txtReferenceNo");
        lblDate.setName("lblDate");


        lblReferenceNo.setName("lblReferenceNo");
        lblStatus.setName("lblStatus");
        panOutwardRegister.setName("panOutwardRegister");
        panOutward.setName("panOutward");
        
    }
    
    private void internationalize() {
             //lblProdType.setText(resourceBundle.getString("lblProdType"));
    }
    
    private void displayAlert(String message){
        CMandatoryDialog cmd = new CMandatoryDialog();
        cmd.setMessage(message);
        cmd.show();
    }
    
    private void deletescreenLock(){
        HashMap map=new HashMap();
        map.put("USER_ID",ProxyParameters.USER_ID);
        map.put("TRANS_DT", currDt.clone());
        map.put("INITIATED_BRANCH",ProxyParameters.BRANCH_ID);
        ClientUtil.execute("DELETE_SCREEN_LOCK", map);
    }
    private String getLockDetails(String lockedBy, String screenId){
        HashMap map = new HashMap();
        StringBuffer data = new StringBuffer() ;
        map.put("LOCKED_BY", lockedBy) ;
        map.put("SCREEN_ID", screenId) ;
        java.util.List lstLock = ClientUtil.executeQuery("getLockedDetails", map);
        map.clear();
        if(lstLock.size() > 0){
            map = (HashMap)(lstLock.get(0));
            data.append("\nLog in Time : ").append(map.get("LOCKED_TIME")) ;
            data.append("\nIP Address : ").append(map.get("IP_ADDR")) ;
            data.append("\nBranch : ").append(map.get("BRANCH_ID"));
        }
        lstLock = null ;
        map = null ;
        return data.toString();
    }
    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private com.see.truetransact.uicomponent.CButton btnAuthorize;
    private com.see.truetransact.uicomponent.CButton btnCancel;
    private com.see.truetransact.uicomponent.CButton btnClose;
    private com.see.truetransact.uicomponent.CButton btnDateChange;
    private com.see.truetransact.uicomponent.CButton btnDelete;
    private com.see.truetransact.uicomponent.CButton btnEdit;
    private com.see.truetransact.uicomponent.CButton btnException;
    private com.see.truetransact.uicomponent.CButton btnNew;
    private com.see.truetransact.uicomponent.CButton btnPrint;
    private com.see.truetransact.uicomponent.CButton btnReject;
    private com.see.truetransact.uicomponent.CButton btnSave;
    private com.see.truetransact.uicomponent.CButton btnView;
    private com.see.truetransact.uicomponent.CLabel lblDate;
    private com.see.truetransact.uicomponent.CLabel lblMsg;
    private javax.swing.JLabel lblReferenceNo;
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
    private com.see.truetransact.uicomponent.CLabel lblSpace76;
    private com.see.truetransact.uicomponent.CLabel lblStatus;
    private com.see.truetransact.uicomponent.CMenuBar mbrCustomer;
    private javax.swing.JMenuItem mitCancel;
    private javax.swing.JMenuItem mitClose;
    private javax.swing.JMenuItem mitDelete;
    private javax.swing.JMenuItem mitEdit;
    private javax.swing.JMenuItem mitNew;
    private javax.swing.JMenuItem mitPrint;
    private javax.swing.JMenuItem mitSave;
    private javax.swing.JMenu mnuProcess;
    private com.see.truetransact.uicomponent.CPanel panOutward;
    private com.see.truetransact.uicomponent.CPanel panOutwardRegister;
    private com.see.truetransact.uicomponent.CPanel panStatus;
    private javax.swing.JScrollPane scrRemarks;
    private javax.swing.JSeparator sptNew;
    private javax.swing.JSeparator sptSave;
    private javax.swing.JToolBar tbrInwardRegister;
    private com.see.truetransact.uicomponent.CDateField tdtDate;
    private com.see.truetransact.uicomponent.CTextArea txaRemarks;
    private javax.swing.JTextField txtReferenceNo;
    // End of variables declaration//GEN-END:variables
    
    public static void main(String[] args) {
      BoardResolutionDetailsUI BoardResolution = new BoardResolutionDetailsUI();
        javax.swing.JFrame j = new javax.swing.JFrame();
        j.getContentPane().add(BoardResolution);
        j.show();
        BoardResolution.show();
    }
}
