/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition. 
 * 
 * ShareResolutionUI.java
 *
 * Created on April 23, 2005, 4:02 PM
 */

package com.see.truetransact.ui.share.shareresolution;

import java.util.Observable;
import java.util.HashMap;
import java.util.List;
import org.apache.log4j.Logger;
import com.see.truetransact.clientutil.ClientUtil;
import com.see.truetransact.clientproxy.ProxyParameters;
import com.see.truetransact.clientutil.EnhancedComboBoxModel;
import java.util.ArrayList;
import com.see.truetransact.clientutil.ClientConstants;
import com.see.truetransact.commonutil.CommonConstants;
import com.see.truetransact.uimandatory.MandatoryCheck;
import com.see.truetransact.uivalidation.NumericValidation;
import com.see.truetransact.ui.common.transaction.TransactionUI;
import com.see.truetransact.commonutil.CommonUtil;
import com.see.truetransact.ui.TrueTransactMain;
import com.see.truetransact.ui.common.viewall.ViewAll;
import com.see.truetransact.uicomponent.COptionPane;
/**
 *
 * @author  JK
 */
public class ShareResolutionUI extends com.see.truetransact.uicomponent.CInternalFrame implements com.see.truetransact.uimandatory.UIMandatoryField, java.util.Observer {
   
    /** Creates new form ShareResolutionUI */
    public ShareResolutionUI() {
        setupInit();
        tblData.setAutoCreateRowSorter(true);
        
    }
    
    java.util.ResourceBundle resourceBundle = java.util.ResourceBundle.getBundle("com.see.truetransact.ui.share.shareresolution.ShareResolutionRB", ProxyParameters.LANGUAGE);
    java.util.ResourceBundle resourceBundle1 = java.util.ResourceBundle.getBundle("com.see.truetransact.ui.termloan.TermLoanRB", ProxyParameters.LANGUAGE);
    private ShareResolutionOB observable;
      private boolean finalChecking = false;
    private void setupInit() {
        initComponents();
        setFieldNames();
        internationalize();
        setMaxLength();
        setMandatoryHashMap();
        setHelpMessage();
        setObservable();
        populateData();
        btnException.setVisible(false);
        btnSave.setEnabled(false);
        btnCancel.setEnabled(false);
        btnAuthorize.setEnabled(false);
        btnReject.setEnabled(false);
        btnException.setEnabled(false);
    }
    
    private void setMaxLength(){
        txtResolutionNo.setMaxLength(16);
        txtResolutionNo.setAllowAll(true);
    }
    
    private void populateData() {
        try {
            log.info("populateData...");
            ArrayList heading = observable.populateData(tblData);
            if (heading != null) {
                EnhancedComboBoxModel cboModel = new EnhancedComboBoxModel(heading);
            }
        } catch( Exception e ) {
            System.err.println( "Exception " + e.toString() + "Caught" );
            log.info("Exception in populateData..." + e.toString());
        }
    }
    
    private void setObservable() {
        observable = ShareResolutionOB.getInstance();
        observable.addObserver(this);
    }
    /* Auto Generated Method - internationalize()
   This method used to assign display texts from
   the Resource Bundle File. */
    private void internationalize() {
        lblSpace5.setText(resourceBundle.getString("lblSpace5"));
        btnDelete.setText(resourceBundle.getString("btnDelete"));
        lblResolutionNo.setText(resourceBundle.getString("lblResolutionNo"));
        btnClose.setText(resourceBundle.getString("btnClose"));
        btnAccept.setText(resourceBundle.getString("btnAccept"));
        btnReject.setText(resourceBundle.getString("btnReject"));
        btnEdit.setText(resourceBundle.getString("btnEdit"));
        btnAuthorize.setText(resourceBundle.getString("btnAuthorize"));
        lblMsg.setText(resourceBundle.getString("lblMsg"));
        btnException.setText(resourceBundle.getString("btnException"));
        btnNew.setText(resourceBundle.getString("btnNew"));
        btnClose1.setText(resourceBundle.getString("btnClose1"));
        lblSpace2.setText(resourceBundle.getString("lblSpace2"));
        btnSave.setText(resourceBundle.getString("btnSave"));
        btnCancel.setText(resourceBundle.getString("btnCancel"));
        lblSpace3.setText(resourceBundle.getString("lblSpace3"));
       // btnDeffered.setText(resourceBundle.getString("btnDeffered"));
        chkSelectAll.setText(resourceBundle.getString("chkSelectAll"));
        lblStatus.setText(resourceBundle.getString("lblStatus"));
        lblSpace1.setText(resourceBundle.getString("lblSpace1"));
        btnReject1.setText(resourceBundle.getString("btnReject1"));
        btnPrint.setText(resourceBundle.getString("btnPrint"));
        lblResolutionDt.setText(resourceBundle.getString("lblResolutionDt"));
    }
    
    
/* Auto Generated Method - update()
   This method called by Observable. It updates the UI with
   Observable's data. If needed add/Remove RadioButtons
   method need to be added.*/
    public void update(Observable observed, Object arg) {
        chkSelectAll.setSelected(observable.getChkSelectAll());
        txtResolutionNo.setText(observable.getTxtResolutionNo());
        tdtResolutionDt.setDateValue(observable.getTdtResolutionDt());
    }
    
    
/* Auto Generated Method - updateOBFields()
   This method called by Save option of UI.
   It updates the OB with UI data.*/
    public void updateOBFields() {
        observable.setChkSelectAll(chkSelectAll.isSelected());
        observable.setTxtResolutionNo(txtResolutionNo.getText());
        observable.setTdtResolutionDt(tdtResolutionDt.getDateValue());
        observable.setChkApplicationFee(chkApplicationFee.isSelected());
        observable.setChkMemberShipFee(chkMemberShipFee.isSelected());
        observable.setChkShareFee(chkShareFee.isSelected());
    }
    
    
/* Auto Generated Method - setMandatoryHashMap()
ADD: implements com.see.truetransact.uimandatory.UIMandatoryField
This method list out all the Input Fields available in the UI.
It needs a class level HashMap variable mandatoryMap. */
    public void setMandatoryHashMap() {
        HashMap mandatoryMap = new HashMap();
        mandatoryMap.put("txtResolutionNo", new Boolean(true));
        mandatoryMap.put("tdtResolutionDt", new Boolean(true));
    }
    
    
    
    
/* Auto Generated Method - setHelpMessage()
   This method shows tooltip help for all the input fields
   available in the UI. It needs the Mandatory Resource Bundle
   object. Help display Label name should be lblMsg. */
    public void setHelpMessage() {
        chkSelectAll.setHelpMessage(lblMsg, resourceBundle.getString("chkSelectAll"));
        txtResolutionNo.setHelpMessage(lblMsg, resourceBundle.getString("txtResolutionNo"));
        tdtResolutionDt.setHelpMessage(lblMsg, resourceBundle.getString("tdtResolutionDt"));
    }
    
    private final static Logger log = Logger.getLogger(ShareResolutionUI.class);
    
    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {
        java.awt.GridBagConstraints gridBagConstraints;

        tbrShareAcct = new javax.swing.JToolBar();
        btnNew = new com.see.truetransact.uicomponent.CButton();
        lblSpace29 = new com.see.truetransact.uicomponent.CLabel();
        btnEdit = new com.see.truetransact.uicomponent.CButton();
        lblSpace30 = new com.see.truetransact.uicomponent.CLabel();
        btnDelete = new com.see.truetransact.uicomponent.CButton();
        lblSpace2 = new com.see.truetransact.uicomponent.CLabel();
        btnSave = new com.see.truetransact.uicomponent.CButton();
        lblSpace31 = new com.see.truetransact.uicomponent.CLabel();
        btnCancel = new com.see.truetransact.uicomponent.CButton();
        lblSpace3 = new com.see.truetransact.uicomponent.CLabel();
        btnAuthorize = new com.see.truetransact.uicomponent.CButton();
        lblSpace32 = new com.see.truetransact.uicomponent.CLabel();
        btnException = new com.see.truetransact.uicomponent.CButton();
        lblSpace33 = new com.see.truetransact.uicomponent.CLabel();
        btnReject = new com.see.truetransact.uicomponent.CButton();
        lblSpace5 = new com.see.truetransact.uicomponent.CLabel();
        btnPrint = new com.see.truetransact.uicomponent.CButton();
        lblSpace34 = new com.see.truetransact.uicomponent.CLabel();
        btnClose = new com.see.truetransact.uicomponent.CButton();
        panStatus = new com.see.truetransact.uicomponent.CPanel();
        lblSpace1 = new com.see.truetransact.uicomponent.CLabel();
        lblStatus = new com.see.truetransact.uicomponent.CLabel();
        lblMsg = new com.see.truetransact.uicomponent.CLabel();
        panShareResolution = new com.see.truetransact.uicomponent.CPanel();
        tabShareResolution = new com.see.truetransact.uicomponent.CTabbedPane();
        panResolution = new com.see.truetransact.uicomponent.CPanel();
        chkSelectAll = new com.see.truetransact.uicomponent.CCheckBox();
        panTable = new com.see.truetransact.uicomponent.CPanel();
        srcTable = new com.see.truetransact.uicomponent.CScrollPane();
        tblData = new com.see.truetransact.uicomponent.CTable();
        panResolFields = new com.see.truetransact.uicomponent.CPanel();
        lblResolutionNo = new com.see.truetransact.uicomponent.CLabel();
        txtResolutionNo = new com.see.truetransact.uicomponent.CTextField();
        lblResolutionDt = new com.see.truetransact.uicomponent.CLabel();
        tdtResolutionDt = new com.see.truetransact.uicomponent.CDateField();
        SearchResolution = new com.see.truetransact.uicomponent.CButton();
        panButton = new com.see.truetransact.uicomponent.CPanel();
        btnAccept = new com.see.truetransact.uicomponent.CButton();
        btnReject1 = new com.see.truetransact.uicomponent.CButton();
        btnClose1 = new com.see.truetransact.uicomponent.CButton();
        chkApplicationFee = new com.see.truetransact.uicomponent.CCheckBox();
        chkMemberShipFee = new com.see.truetransact.uicomponent.CCheckBox();
        chkShareFee = new com.see.truetransact.uicomponent.CCheckBox();
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
        getContentPane().setLayout(new java.awt.GridBagLayout());

        btnNew.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_NEW.gif"))); // NOI18N
        btnNew.setToolTipText("New");
        btnNew.setEnabled(false);
        btnNew.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnNewActionPerformed(evt);
            }
        });
        tbrShareAcct.add(btnNew);

        lblSpace29.setForeground(new java.awt.Color(240, 240, 240));
        lblSpace29.setText("     ");
        lblSpace29.setMaximumSize(new java.awt.Dimension(3, 18));
        lblSpace29.setMinimumSize(new java.awt.Dimension(3, 18));
        lblSpace29.setPreferredSize(new java.awt.Dimension(3, 18));
        tbrShareAcct.add(lblSpace29);

        btnEdit.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_EDIT.gif"))); // NOI18N
        btnEdit.setToolTipText("Edit");
        btnEdit.setEnabled(false);
        btnEdit.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnEditActionPerformed(evt);
            }
        });
        tbrShareAcct.add(btnEdit);

        lblSpace30.setForeground(new java.awt.Color(240, 240, 240));
        lblSpace30.setText("     ");
        lblSpace30.setMaximumSize(new java.awt.Dimension(3, 18));
        lblSpace30.setMinimumSize(new java.awt.Dimension(3, 18));
        lblSpace30.setPreferredSize(new java.awt.Dimension(3, 18));
        tbrShareAcct.add(lblSpace30);

        btnDelete.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_DELETE.gif"))); // NOI18N
        btnDelete.setToolTipText("Delete");
        btnDelete.setEnabled(false);
        btnDelete.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnDeleteActionPerformed(evt);
            }
        });
        tbrShareAcct.add(btnDelete);

        lblSpace2.setText("     ");
        tbrShareAcct.add(lblSpace2);

        btnSave.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_SAVE.gif"))); // NOI18N
        btnSave.setToolTipText("Save");
        btnSave.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSaveActionPerformed(evt);
            }
        });
        tbrShareAcct.add(btnSave);

        lblSpace31.setForeground(new java.awt.Color(240, 240, 240));
        lblSpace31.setText("     ");
        lblSpace31.setMaximumSize(new java.awt.Dimension(3, 18));
        lblSpace31.setMinimumSize(new java.awt.Dimension(3, 18));
        lblSpace31.setPreferredSize(new java.awt.Dimension(3, 18));
        tbrShareAcct.add(lblSpace31);

        btnCancel.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_CANCEL.gif"))); // NOI18N
        btnCancel.setToolTipText("Cancel");
        tbrShareAcct.add(btnCancel);

        lblSpace3.setText("     ");
        tbrShareAcct.add(lblSpace3);

        btnAuthorize.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_AUTHORIZE.gif"))); // NOI18N
        btnAuthorize.setToolTipText("Authorize");
        tbrShareAcct.add(btnAuthorize);

        lblSpace32.setForeground(new java.awt.Color(240, 240, 240));
        lblSpace32.setText("     ");
        lblSpace32.setMaximumSize(new java.awt.Dimension(3, 18));
        lblSpace32.setMinimumSize(new java.awt.Dimension(3, 18));
        lblSpace32.setPreferredSize(new java.awt.Dimension(3, 18));
        tbrShareAcct.add(lblSpace32);

        btnException.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_EXCEPTION.gif"))); // NOI18N
        btnException.setToolTipText("Exception");
        tbrShareAcct.add(btnException);

        lblSpace33.setForeground(new java.awt.Color(240, 240, 240));
        lblSpace33.setText("     ");
        lblSpace33.setMaximumSize(new java.awt.Dimension(3, 18));
        lblSpace33.setMinimumSize(new java.awt.Dimension(3, 18));
        lblSpace33.setPreferredSize(new java.awt.Dimension(3, 18));
        tbrShareAcct.add(lblSpace33);

        btnReject.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_REJECT.gif"))); // NOI18N
        btnReject.setToolTipText("Reject");
        btnReject.setMaximumSize(new java.awt.Dimension(29, 27));
        btnReject.setMinimumSize(new java.awt.Dimension(29, 27));
        btnReject.setPreferredSize(new java.awt.Dimension(29, 27));
        tbrShareAcct.add(btnReject);

        lblSpace5.setText("     ");
        tbrShareAcct.add(lblSpace5);

        btnPrint.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_PRINT.gif"))); // NOI18N
        btnPrint.setToolTipText("Print");
        btnPrint.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnPrintActionPerformed(evt);
            }
        });
        tbrShareAcct.add(btnPrint);

        lblSpace34.setForeground(new java.awt.Color(240, 240, 240));
        lblSpace34.setText("     ");
        lblSpace34.setMaximumSize(new java.awt.Dimension(3, 18));
        lblSpace34.setMinimumSize(new java.awt.Dimension(3, 18));
        lblSpace34.setPreferredSize(new java.awt.Dimension(3, 18));
        tbrShareAcct.add(lblSpace34);

        btnClose.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_CLOSE.gif"))); // NOI18N
        btnClose.setToolTipText("Close");
        btnClose.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCloseActionPerformed(evt);
            }
        });
        tbrShareAcct.add(btnClose);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.weightx = 1.0;
        getContentPane().add(tbrShareAcct, gridBagConstraints);

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

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        getContentPane().add(panStatus, gridBagConstraints);

        panShareResolution.setMinimumSize(new java.awt.Dimension(613, 550));
        panShareResolution.setPreferredSize(new java.awt.Dimension(613, 550));
        panShareResolution.setLayout(new java.awt.GridBagLayout());

        panResolution.setLayout(new java.awt.GridBagLayout());

        chkSelectAll.setText("Select All");
        chkSelectAll.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                chkSelectAllActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 0, 13);
        panResolution.add(chkSelectAll, gridBagConstraints);

        panTable.setMinimumSize(new java.awt.Dimension(600, 500));
        panTable.setPreferredSize(new java.awt.Dimension(600, 500));
        panTable.setLayout(new java.awt.GridBagLayout());

        tblData.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "Title 1", "Title 2", "Title 3", "Title 4"
            }
        ));
        tblData.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tblDataMouseClicked(evt);
            }
        });
        srcTable.setViewportView(tblData);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        panTable.add(srcTable, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panResolution.add(panTable, gridBagConstraints);

        panResolFields.setLayout(new java.awt.GridBagLayout());

        lblResolutionNo.setText("Resolution No.");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panResolFields.add(lblResolutionNo, gridBagConstraints);

        txtResolutionNo.setMinimumSize(new java.awt.Dimension(100, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panResolFields.add(txtResolutionNo, gridBagConstraints);

        lblResolutionDt.setText("Resolution Date");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panResolFields.add(lblResolutionDt, gridBagConstraints);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panResolFields.add(tdtResolutionDt, gridBagConstraints);

        SearchResolution.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_SELECT.gif"))); // NOI18N
        SearchResolution.setMaximumSize(new java.awt.Dimension(21, 21));
        SearchResolution.setMinimumSize(new java.awt.Dimension(21, 21));
        SearchResolution.setPreferredSize(new java.awt.Dimension(21, 21));
        SearchResolution.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                SearchResolutionActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        panResolFields.add(SearchResolution, gridBagConstraints);

        panResolution.add(panResolFields, new java.awt.GridBagConstraints());

        panButton.setLayout(new java.awt.GridBagLayout());

        btnAccept.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_AUTHORIZE.gif"))); // NOI18N
        btnAccept.setText("Accept");
        btnAccept.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnAcceptActionPerformed(evt);
            }
        });
        panButton.add(btnAccept, new java.awt.GridBagConstraints());

        btnReject1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_REJECT.gif"))); // NOI18N
        btnReject1.setText("Reject");
        btnReject1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnReject1ActionPerformed(evt);
            }
        });
        panButton.add(btnReject1, new java.awt.GridBagConstraints());

        btnClose1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_CLOSE.gif"))); // NOI18N
        btnClose1.setText("Close");
        btnClose1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnClose1ActionPerformed(evt);
            }
        });
        panButton.add(btnClose1, new java.awt.GridBagConstraints());

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panResolution.add(panButton, gridBagConstraints);

        chkApplicationFee.setText("ApplicationFee");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.fill = java.awt.GridBagConstraints.VERTICAL;
        gridBagConstraints.insets = new java.awt.Insets(0, 15, 0, 440);
        panResolution.add(chkApplicationFee, gridBagConstraints);

        chkMemberShipFee.setText("MemberShipFee");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 0, 199);
        panResolution.add(chkMemberShipFee, gridBagConstraints);

        chkShareFee.setText("ShareFee");
        chkShareFee.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                chkShareFeeActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 3;
        panResolution.add(chkShareFee, gridBagConstraints);

        tabShareResolution.addTab("Resolution", panResolution);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        panShareResolution.add(tabShareResolution, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        getContentPane().add(panShareResolution, gridBagConstraints);

        mnuProcess.setText("Process");

        mitNew.setText("New");
        mnuProcess.add(mitNew);

        mitEdit.setText("Edit");
        mnuProcess.add(mitEdit);

        mitDelete.setText("Delete");
        mnuProcess.add(mitDelete);
        mnuProcess.add(sptDelete);

        mitSave.setText("Save");
        mnuProcess.add(mitSave);

        mitCancel.setText("Cancel");
        mnuProcess.add(mitCancel);
        mnuProcess.add(sptCancel);

        mitClose.setText("Close");
        mnuProcess.add(mitClose);

        mbrMain.add(mnuProcess);

        setJMenuBar(mbrMain);

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void chkShareFeeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_chkShareFeeActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_chkShareFeeActionPerformed

    private void btnSaveActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSaveActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_btnSaveActionPerformed

    private void btnDeleteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnDeleteActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_btnDeleteActionPerformed

    private void btnNewActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnNewActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_btnNewActionPerformed

    private void btnEditActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnEditActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_btnEditActionPerformed

    private void btnPrintActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnPrintActionPerformed
        // TODO add your handling code here:
         HashMap reportParamMap = new HashMap();
 com.see.truetransact.clientutil.ttrintegration.LinkReport.getReports(getScreenID(), reportParamMap);
    }//GEN-LAST:event_btnPrintActionPerformed
    //--- For Mandatory check
    private String mandatoryChk(){
        String shareResolutionMandatoryMessage = new MandatoryCheck().checkMandatory(getClass().getName(), panResolFields);
        if(shareResolutionMandatoryMessage.length()>0){
            ClientUtil.showAlertWindow(shareResolutionMandatoryMessage);
        }
        return shareResolutionMandatoryMessage;
    }
    
    //--- calls the Observable.doAction
    private void doAction(int actionType){
        try{
            String mandatoryChk = mandatoryChk();
            int no=15;
            if(actionType==no){
                 updateOBFields();
                observable.setActionType(actionType);
                observable.setStatus();
              
           
                observable.doAction();
                populateData();
                observable.ttNotifyObservers();
            }
            else
            if(mandatoryChk.length()<=0){
                updateOBFields();
                observable.setActionType(actionType);
                observable.setStatus();
              if(chkApplicationFee.isSelected()==true || chkMemberShipFee.isSelected()==true || chkShareFee.isSelected()==true){
                  ClientUtil.showMessageWindow("ApplicationFee,ShareFee,MembershipFee can be ticked only for rejection");
                  
              }
              else{
                observable.doAction();
                populateData();
                observable.ttNotifyObservers();
              }
            
            
         
        }
        }catch (Exception e ){
            e.printStackTrace();
            log.info("Exception in doAction UI..." + e.toString());
        }
    }    
    private void btnReject1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnReject1ActionPerformed
        
        insertTransactionPart() ;
        
        doAction(ClientConstants.ACTIONTYPE_RESOLUTION_REJECT);
        clear();
    }//GEN-LAST:event_btnReject1ActionPerformed
    
    private void btnAcceptActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAcceptActionPerformed
        int flag1 = 1;
      for(int i=0;i<tblData.getRowCount();i++){
              if(((Boolean)tblData.getValueAt(i, 0)).booleanValue()){
                flag1 = 0;  
              }  
      }
      if(flag1 == 1){
          ClientUtil.showMessageWindow("Please select atleast one row to accept");
          return;
      }else{
        doAction(ClientConstants.ACTIONTYPE_RESOLUTION_ACCEPT);
      }
        clear();
    }//GEN-LAST:event_btnAcceptActionPerformed
    
    public void clear(){
     txtResolutionNo.setText(null);
     tdtResolutionDt.setDateValue(null);
     chkApplicationFee.setSelected(false);
     chkMemberShipFee.setSelected(false);
     chkSelectAll.setSelected(false);
     chkShareFee.setSelected(false);
    }
    
    private void btnCloseActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCloseActionPerformed
        closeOperation();
    }//GEN-LAST:event_btnCloseActionPerformed
    
    private void chkSelectAllActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_chkSelectAllActionPerformed
        // Add your handling code here:
        observable.setSelectAll(new Boolean(chkSelectAll.isSelected()));
    }//GEN-LAST:event_chkSelectAllActionPerformed
   
     private void insertTransactionPart() {
        HashMap singleAuthorizeMap = new HashMap();
        java.util.ArrayList arrList = new java.util.ArrayList();
        HashMap authDataMap = new HashMap();
        arrList.add(authDataMap);
      int flag1 = 1;
      for(int i=0;i<tblData.getRowCount();i++){
              if(((Boolean)tblData.getValueAt(i, 0)).booleanValue()){
                flag1 = 0;  
              }  
      }
      if(flag1 == 1){
          ClientUtil.showMessageWindow("Please select atleast one row to reject");
          return;
      }
      else if(flag1 == 0){
            String [] debitType = {"Cash","Transfer"};
            int option3 = 0 ;
            if(option3 == 0){
                String transType = "";
                //                System.out.println("!!! transType :"+transType);
                while (CommonUtil.convertObjToStr(transType).length()==0) {
                    transType = (String)COptionPane.showInputDialog(null,"Select Transaction Type", "Transaction type", COptionPane.QUESTION_MESSAGE, null, debitType, "");
                    if (CommonUtil.convertObjToStr(transType).length()>0) {
                        authDataMap.put("TRANSACTION_PART","TRANSACTION_PART");
                        authDataMap.put("TRANS_TYPE",transType.toUpperCase());
                        //authDataMap.put("LIMIT",txtLimit_SD.getText());
                        if(CommonUtil.convertObjToStr(transType.toUpperCase()).equals("CASH")){
                            boolean flag = true;
                            do {
                                String tokenNo = COptionPane.showInputDialog(this,resourceBundle1.getString("REMARK_CASH_TRANS"));
                                if (tokenNo != null && tokenNo.length()>0) {
                                    flag = tokenValidation(tokenNo);
                                }else{
                                    flag = true;
                                }
                                if(flag == false){
                                    ClientUtil.showAlertWindow("Token is invalid or not issued for you. Please verify.");
                                }else{
                                    authDataMap.put("TOKEN_NO",tokenNo);
                                }
                            } while (!flag);
                        }else if(CommonUtil.convertObjToStr(transType.toUpperCase()).equals("TRANSFER")){
                            boolean flag = true;
                            do {
                                String sbAcNo = firstEnteredActNo();
                                if(sbAcNo!=null && sbAcNo.length()>0){
                                    flag = checkingActNo(sbAcNo);
                                    if(flag == false && finalChecking == false){
                                        ClientUtil.showAlertWindow("Account No is invalid, Please enter correct no");
                                    }else{
                                        authDataMap.put("CR_ACT_NUM",sbAcNo);
                                    }
                                    finalChecking = false;
                                } else {
                                    ClientUtil.showMessageWindow("Transaction Not Created");
                                    flag = true;
                                    authDataMap.remove("TRANSACTION_PART");
                                }
                            } while (!flag);
                        }
                        observable.setNewTransactionMap(authDataMap);
                    }else{
                        transType = "Cancel";
                    }
                }
            }
            
      }
      
    }
    
    private boolean tokenValidation(String tokenNo){
        boolean tokenflag = false;
        HashMap tokenWhereMap = new HashMap();// Separating Serias No and Token No
        char[] chrs = tokenNo.toCharArray();
        StringBuffer seriesNo = new StringBuffer();
        int i=0;
        for (int j= chrs.length; i < j; i++ ) {
            if (Character.isDigit(chrs[i]))
                break;
            else
                seriesNo.append(chrs[i]);
        }
        tokenWhereMap.put("SERIES_NO", seriesNo.toString());
        tokenWhereMap.put("TOKEN_NO", CommonUtil.convertObjToInt(tokenNo.substring(i)));
        tokenWhereMap.put("USER_ID", ProxyParameters.USER_ID);
        tokenWhereMap.put("BRANCH_ID", ProxyParameters.BRANCH_ID);
        tokenWhereMap.put("CURRENT_DT", ClientUtil.getCurrentDate());
        List lst = ClientUtil.executeQuery("validateTokenNo", tokenWhereMap);
        if (((Integer) lst.get(0)).intValue() == 0) {
            tokenflag = false;
        }else{
            tokenflag = true;
        }
        return tokenflag;
    }
     private String firstEnteredActNo(){
        String sbAcNo = COptionPane.showInputDialog(this,resourceBundle1.getString("REMARK_TRANSFER_TRANS"));
        return sbAcNo;
    }
     
      private boolean checkingActNo(String sbAcNo){
        boolean flag = false;
        HashMap existingMap = new HashMap();
        existingMap.put("ACT_NUM",sbAcNo.toUpperCase());
        List mapDataList = ClientUtil.executeQuery("getAccNoDet", existingMap);
        System.out.println("#### mapDataList :"+mapDataList);
        if (mapDataList!=null && mapDataList.size()>0) {
            existingMap = (HashMap)mapDataList.get(0);
            String[] obj5 = {"Proceed","ReEnter"};
            int option4 = COptionPane.showOptionDialog(null,("Please check whether Account No, Name coreect or not " + "\nOperative AcctNo is : " + existingMap.get("Account Number")+"\nCustomer Name :" + existingMap.get("Customer Name")), ("Transaction Part"),
            COptionPane.YES_NO_CANCEL_OPTION,COptionPane.QUESTION_MESSAGE,null,obj5,obj5[0]);
            if(option4 == 0){
                flag = true;
            }else{
                flag = false;
            }
        }
        return flag;
    }
    
    /* Auto Generated Method - setFieldNames()
       This method assigns name for all the components.
       Other functions are working based on this name. */
    private void setFieldNames() {
        btnAuthorize.setName("btnAuthorize");
        btnCancel.setName("btnCancel");
        btnClose.setName("btnClose");
        btnClose1.setName("btnClose1");
        btnDelete.setName("btnDelete");
        btnEdit.setName("btnEdit");
        btnException.setName("btnException");
       // btnDeffered.setName("btnDeffered");
        btnNew.setName("btnNew");
        btnPrint.setName("btnPrint");
        btnReject.setName("btnReject");
        btnReject1.setName("btnReject1");
        btnAccept.setName("btnAccept");
        btnSave.setName("btnSave");
        chkSelectAll.setName("chkSelectAll");
        lblMsg.setName("lblMsg");
        lblResolutionDt.setName("lblResolutionDt");
        lblResolutionNo.setName("lblResolutionNo");
        lblSpace1.setName("lblSpace1");
        lblSpace2.setName("lblSpace2");
        lblSpace3.setName("lblSpace3");
        lblSpace5.setName("lblSpace5");
        lblStatus.setName("lblStatus");
        mbrMain.setName("mbrMain");
        panButton.setName("panButton");
        panResolFields.setName("panResolFields");
        panResolution.setName("panResolution");
        panShareResolution.setName("panShareResolution");
        panStatus.setName("panStatus");
        panTable.setName("panTable");
        srcTable.setName("srcTable");
        tabShareResolution.setName("tabShareResolution");
        tblData.setName("tblData");
        tdtResolutionDt.setName("tdtResolutionDt");
        txtResolutionNo.setName("txtResolutionNo");
    }
    private void btnClose1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnClose1ActionPerformed
        closeOperation();
    }//GEN-LAST:event_btnClose1ActionPerformed

    private void tblDataMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tblDataMouseClicked
        // TODO add your handling code here:
        tblData.setEnabled(true);
        
    }//GEN-LAST:event_tblDataMouseClicked

private void SearchResolutionActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_SearchResolutionActionPerformed
HashMap viewMap = new HashMap();
    HashMap map = new HashMap();
            map.put("BRANCH_CODE",TrueTransactMain.BRANCH_ID);
           // map.put("BRANCH_CODE","0001");
            viewMap.put(CommonConstants.MAP_WHERE, map);
            viewMap.put(CommonConstants.MAP_NAME, "getBoardResolutionAuth"); 
             new ViewAll(this,viewMap).show();

    
    // TODO add your handling code here:
}//GEN-LAST:event_SearchResolutionActionPerformed
      public void fillData(Object map) {
        try{
            String resolutionno="";
             HashMap hash = (HashMap) map;
            resolutionno=CommonUtil.convertObjToStr(hash.get("RESOLUTION_ID"));
            txtResolutionNo.setText( resolutionno);
            tdtResolutionDt.setDateValue(CommonUtil.convertObjToStr(hash.get("RESOLUTION_DATE")));
        }
        catch(Exception e){
            log.error(e);
        }
    }

    
    private void closeOperation(){
        cancelOperation();
        this.dispose();
    }
    
    private void cancelOperation(){
        observable.resetForm();
        ClientUtil.enableDisable(this, false);
        observable.ttNotifyObservers();
    }
    
    public java.util.HashMap getMandatoryHashMap() {
        return new HashMap();
    }
    
    
    
    
    
    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        javax.swing.JFrame frm = new javax.swing.JFrame();
        ShareResolutionUI shareResolutionUI = new ShareResolutionUI();
        frm.getContentPane().add(shareResolutionUI);
        shareResolutionUI.show();
        frm.setSize(800, 650);
        frm.show();
    }
    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private com.see.truetransact.uicomponent.CButton SearchResolution;
    private com.see.truetransact.uicomponent.CButton btnAccept;
    private com.see.truetransact.uicomponent.CButton btnAuthorize;
    private com.see.truetransact.uicomponent.CButton btnCancel;
    private com.see.truetransact.uicomponent.CButton btnClose;
    private com.see.truetransact.uicomponent.CButton btnClose1;
    private com.see.truetransact.uicomponent.CButton btnDelete;
    private com.see.truetransact.uicomponent.CButton btnEdit;
    private com.see.truetransact.uicomponent.CButton btnException;
    private com.see.truetransact.uicomponent.CButton btnNew;
    private com.see.truetransact.uicomponent.CButton btnPrint;
    private com.see.truetransact.uicomponent.CButton btnReject;
    private com.see.truetransact.uicomponent.CButton btnReject1;
    private com.see.truetransact.uicomponent.CButton btnSave;
    private com.see.truetransact.uicomponent.CCheckBox chkApplicationFee;
    private com.see.truetransact.uicomponent.CCheckBox chkMemberShipFee;
    private com.see.truetransact.uicomponent.CCheckBox chkSelectAll;
    private com.see.truetransact.uicomponent.CCheckBox chkShareFee;
    private com.see.truetransact.uicomponent.CLabel lblMsg;
    private com.see.truetransact.uicomponent.CLabel lblResolutionDt;
    private com.see.truetransact.uicomponent.CLabel lblResolutionNo;
    private com.see.truetransact.uicomponent.CLabel lblSpace1;
    private com.see.truetransact.uicomponent.CLabel lblSpace2;
    private com.see.truetransact.uicomponent.CLabel lblSpace29;
    private com.see.truetransact.uicomponent.CLabel lblSpace3;
    private com.see.truetransact.uicomponent.CLabel lblSpace30;
    private com.see.truetransact.uicomponent.CLabel lblSpace31;
    private com.see.truetransact.uicomponent.CLabel lblSpace32;
    private com.see.truetransact.uicomponent.CLabel lblSpace33;
    private com.see.truetransact.uicomponent.CLabel lblSpace34;
    private com.see.truetransact.uicomponent.CLabel lblSpace5;
    private com.see.truetransact.uicomponent.CLabel lblStatus;
    private com.see.truetransact.uicomponent.CMenuBar mbrMain;
    private javax.swing.JMenuItem mitCancel;
    private javax.swing.JMenuItem mitClose;
    private javax.swing.JMenuItem mitDelete;
    private javax.swing.JMenuItem mitEdit;
    private javax.swing.JMenuItem mitNew;
    private javax.swing.JMenuItem mitSave;
    private javax.swing.JMenu mnuProcess;
    private com.see.truetransact.uicomponent.CPanel panButton;
    private com.see.truetransact.uicomponent.CPanel panResolFields;
    private com.see.truetransact.uicomponent.CPanel panResolution;
    private com.see.truetransact.uicomponent.CPanel panShareResolution;
    private com.see.truetransact.uicomponent.CPanel panStatus;
    private com.see.truetransact.uicomponent.CPanel panTable;
    private javax.swing.JSeparator sptCancel;
    private javax.swing.JSeparator sptDelete;
    private com.see.truetransact.uicomponent.CScrollPane srcTable;
    private com.see.truetransact.uicomponent.CTabbedPane tabShareResolution;
    private com.see.truetransact.uicomponent.CTable tblData;
    private javax.swing.JToolBar tbrShareAcct;
    private com.see.truetransact.uicomponent.CDateField tdtResolutionDt;
    private com.see.truetransact.uicomponent.CTextField txtResolutionNo;
    // End of variables declaration//GEN-END:variables
    
}
