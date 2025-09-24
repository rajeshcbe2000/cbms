/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition.
 * 
 *
 * IndendClosingUI.java
 *
 * Created on Jun 24, 2019, 10:53 AM  Suresh R
 */
package com.see.truetransact.ui.indend.closing;

import java.util.List;
import java.util.Date;
import java.util.HashMap;
import java.util.Observable;
import org.apache.log4j.Logger;
import com.see.truetransact.ui.TrueTransactMain;
import com.see.truetransact.commonutil.CommonUtil;
import com.see.truetransact.clientutil.ClientUtil;
import com.see.truetransact.ui.common.viewall.TextUI;
import com.see.truetransact.clientutil.ComboBoxModel;
import com.see.truetransact.ui.common.viewall.ViewAll;
import com.see.truetransact.clientutil.ClientConstants;
import com.see.truetransact.commonutil.CommonConstants;
import com.see.truetransact.uimandatory.MandatoryCheck;
import com.see.truetransact.clientutil.CMandatoryDialog;
import com.see.truetransact.clientproxy.ProxyParameters;
import com.see.truetransact.clientutil.TableModel;
import com.see.truetransact.commonutil.DateUtil;
import com.see.truetransact.uimandatory.UIMandatoryField;
import com.see.truetransact.ui.common.viewall.TableDialogUI;
import java.util.ArrayList;

/**
 *
 * @author Suresh R
 *
 */
public class IndendClosingUI extends com.see.truetransact.uicomponent.CInternalFrame implements java.util.Observer, UIMandatoryField {

    final int AUTHORIZE = 3, CANCEL = 0;
    int viewType = -1;
    private HashMap mandatoryMap;
    private IndendClosingOB observable;
    private final static Logger log = Logger.getLogger(IndendClosingUI.class);
    IndendClosingRB custIdChangeRB = new IndendClosingRB();
    private String prodType = "";
    java.util.ResourceBundle resourceBundle, objMandatoryRB;
    String cust_type = null;
    private Date currDt = null;
    private List finalList = null;

    /**
     * Creates new form IndendClosingUI
     */
    public IndendClosingUI() {
        initComponents();
        initStartUp();
    }

    private void initStartUp() {
        currDt = ClientUtil.getCurrentDate();
        setMandatoryHashMap();
        setFieldNames();
        internationalize();
        observable = new IndendClosingOB();
        observable.addObserver(this);
        initComponentData();
        enableDisable(false);
        setMaxLength();
        objMandatoryRB = java.util.ResourceBundle.getBundle("com.see.truetransact.ui.indend.closing.IndendClosingMRB", ProxyParameters.LANGUAGE);
        new MandatoryCheck().putMandatoryMarks(getClass().getName(), panIndendClosingSubDetails);
        setHelpMessage();
        btnAuthorize.setVisible(false);
        btnReject.setVisible(false);
        btnException.setVisible(false);
        btnDisplay.setEnabled(false);
        setSizeTableData();
        tbrOperativeAcctProduct.setVisible(false);
        tdtClosingDt.setDateValue(CommonUtil.convertObjToStr(TrueTransactMain.BANKINFO.get("YEAREND_PROCESS_DT")));
        ClientUtil.enableDisable(panClosingSubDetails, true);
        btnDisplay.setEnabled(true);
    }

    private void setSizeTableData() {
        tblIndendCloseDetails.getColumnModel().getColumn(0).setPreferredWidth(10);
        tblIndendCloseDetails.getColumnModel().getColumn(1).setPreferredWidth(70);
        tblIndendCloseDetails.getColumnModel().getColumn(2).setPreferredWidth(200);
        tblIndendCloseDetails.getColumnModel().getColumn(3).setPreferredWidth(70);
        tblIndendCloseDetails.getColumnModel().getColumn(4).setPreferredWidth(70);
        //tblIndendCloseDetails.getColumnModel().getColumn(5).setPreferredWidth(80);
    }

    private void setSizeTableDataForDamage() {
        tblIndendCloseDetails.getColumnModel().getColumn(0).setPreferredWidth(10);
        tblIndendCloseDetails.getColumnModel().getColumn(1).setPreferredWidth(70);
        tblIndendCloseDetails.getColumnModel().getColumn(2).setPreferredWidth(250);
        tblIndendCloseDetails.getColumnModel().getColumn(3).setPreferredWidth(100);
    }

    private void setMaxLength() {
    }

    public void setMandatoryHashMap() {
        mandatoryMap = new HashMap();
        mandatoryMap.put("txtDepoID", new Boolean(true));
        mandatoryMap.put("tdtClosingDt", new Boolean(true));
        mandatoryMap.put("txtClosingAmount", new Boolean(true));
        mandatoryMap.put("txtClosingPerLessAmt", new Boolean(true));
        mandatoryMap.put("cboStockType", new Boolean(true));
        mandatoryMap.put("cboClosingStockType", new Boolean(true));
    }

    public HashMap getMandatoryHashMap() {
        return mandatoryMap;
    }

    public void setHelpMessage() {
        tdtClosingDt.setHelpMessage(lblMsg, objMandatoryRB.getString("tdtClosingDt"));
        cboStockType.setHelpMessage(lblMsg, objMandatoryRB.getString("cboStockType"));
    }
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {
        java.awt.GridBagConstraints gridBagConstraints;

        rdoTransactionType = new com.see.truetransact.uicomponent.CButtonGroup();
        panIndendInfo = new com.see.truetransact.uicomponent.CPanel();
        panIndendClosingSubDetails = new com.see.truetransact.uicomponent.CPanel();
        panClosingSubDetails = new com.see.truetransact.uicomponent.CPanel();
        cboStockType = new com.see.truetransact.uicomponent.CComboBox();
        lblStockType = new com.see.truetransact.uicomponent.CLabel();
        tdtClosingDt = new com.see.truetransact.uicomponent.CDateField();
        lblClosingDt = new com.see.truetransact.uicomponent.CLabel();
        btnDisplay = new com.see.truetransact.uicomponent.CButton();
        panIndendCloseTbl = new com.see.truetransact.uicomponent.CPanel();
        srpTable_IndendCloseDetails = new com.see.truetransact.uicomponent.CScrollPane();
        tblIndendCloseDetails = new com.see.truetransact.uicomponent.CTable();
        panProcess = new com.see.truetransact.uicomponent.CPanel();
        btnProcess = new com.see.truetransact.uicomponent.CButton();
        btnClose1 = new com.see.truetransact.uicomponent.CButton();
        btnClear = new com.see.truetransact.uicomponent.CButton();
        tbrOperativeAcctProduct = new javax.swing.JToolBar();
        btnView = new com.see.truetransact.uicomponent.CButton();
        lblSpace5 = new com.see.truetransact.uicomponent.CLabel();
        btnNew = new com.see.truetransact.uicomponent.CButton();
        lblSpace51 = new com.see.truetransact.uicomponent.CLabel();
        btnEdit = new com.see.truetransact.uicomponent.CButton();
        lblSpace52 = new com.see.truetransact.uicomponent.CLabel();
        btnDelete = new com.see.truetransact.uicomponent.CButton();
        lblSpace2 = new com.see.truetransact.uicomponent.CLabel();
        btnSave = new com.see.truetransact.uicomponent.CButton();
        lblSpace53 = new com.see.truetransact.uicomponent.CLabel();
        btnCancel = new com.see.truetransact.uicomponent.CButton();
        lblSpace3 = new com.see.truetransact.uicomponent.CLabel();
        btnAuthorize = new com.see.truetransact.uicomponent.CButton();
        lblSpace54 = new com.see.truetransact.uicomponent.CLabel();
        btnException = new com.see.truetransact.uicomponent.CButton();
        lblSpace55 = new com.see.truetransact.uicomponent.CLabel();
        btnReject = new com.see.truetransact.uicomponent.CButton();
        lblSpace4 = new com.see.truetransact.uicomponent.CLabel();
        btnPrint = new com.see.truetransact.uicomponent.CButton();
        lblSpace56 = new com.see.truetransact.uicomponent.CLabel();
        btnClose = new com.see.truetransact.uicomponent.CButton();
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
        setMaximumSize(new java.awt.Dimension(850, 630));
        setMinimumSize(new java.awt.Dimension(850, 630));
        setPreferredSize(new java.awt.Dimension(850, 630));

        panIndendInfo.setBorder(javax.swing.BorderFactory.createTitledBorder(""));
        panIndendInfo.setMinimumSize(new java.awt.Dimension(360, 260));
        panIndendInfo.setPreferredSize(new java.awt.Dimension(360, 260));
        panIndendInfo.setLayout(new java.awt.GridBagLayout());

        panIndendClosingSubDetails.setBorder(javax.swing.BorderFactory.createTitledBorder("Indend Closing Details"));
        panIndendClosingSubDetails.setMaximumSize(new java.awt.Dimension(800, 100));
        panIndendClosingSubDetails.setMinimumSize(new java.awt.Dimension(800, 100));
        panIndendClosingSubDetails.setPreferredSize(new java.awt.Dimension(800, 100));
        panIndendClosingSubDetails.setLayout(new java.awt.GridBagLayout());

        panClosingSubDetails.setMaximumSize(new java.awt.Dimension(790, 92));
        panClosingSubDetails.setMinimumSize(new java.awt.Dimension(790, 92));
        panClosingSubDetails.setPreferredSize(new java.awt.Dimension(790, 92));
        panClosingSubDetails.setLayout(new java.awt.GridBagLayout());

        cboStockType.setMinimumSize(new java.awt.Dimension(100, 21));
        cboStockType.setPopupWidth(120);
        cboStockType.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cboStockTypeActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panClosingSubDetails.add(cboStockType, gridBagConstraints);

        lblStockType.setText("Stock Type");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panClosingSubDetails.add(lblStockType, gridBagConstraints);

        tdtClosingDt.setMinimumSize(new java.awt.Dimension(100, 21));
        tdtClosingDt.setPreferredSize(new java.awt.Dimension(100, 21));
        tdtClosingDt.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                tdtClosingDtFocusLost(evt);
            }
        });
        tdtClosingDt.addAncestorListener(new javax.swing.event.AncestorListener() {
            public void ancestorMoved(javax.swing.event.AncestorEvent evt) {
            }
            public void ancestorAdded(javax.swing.event.AncestorEvent evt) {
                tdtClosingDtAncestorAdded(evt);
            }
            public void ancestorRemoved(javax.swing.event.AncestorEvent evt) {
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panClosingSubDetails.add(tdtClosingDt, gridBagConstraints);

        lblClosingDt.setText("Closing Date");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panClosingSubDetails.add(lblClosingDt, gridBagConstraints);

        btnDisplay.setForeground(new java.awt.Color(51, 0, 51));
        btnDisplay.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/Down_Arrow.gif"))); // NOI18N
        btnDisplay.setText("Display");
        btnDisplay.setFont(new java.awt.Font("MS Sans Serif", 1, 13)); // NOI18N
        btnDisplay.setHorizontalTextPosition(javax.swing.SwingConstants.LEFT);
        btnDisplay.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnDisplayActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.LAST_LINE_END;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panClosingSubDetails.add(btnDisplay, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panIndendClosingSubDetails.add(panClosingSubDetails, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panIndendInfo.add(panIndendClosingSubDetails, gridBagConstraints);

        panIndendCloseTbl.setBorder(javax.swing.BorderFactory.createTitledBorder(""));
        panIndendCloseTbl.setMinimumSize(new java.awt.Dimension(800, 400));
        panIndendCloseTbl.setPreferredSize(new java.awt.Dimension(800, 400));
        panIndendCloseTbl.setLayout(new java.awt.GridBagLayout());

        tblIndendCloseDetails.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "BRANCH_ID", "DEPO_ID", "DESCRIPTION", "CLOSING_AMT", "% DEDUCTED_AMT"
            }
        ));
        tblIndendCloseDetails.setPreferredScrollableViewportSize(new java.awt.Dimension(450, 350));
        tblIndendCloseDetails.setSelectionBackground(new java.awt.Color(255, 255, 0));
        tblIndendCloseDetails.setSelectionForeground(new java.awt.Color(0, 0, 0));
        tblIndendCloseDetails.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tblIndendCloseDetailsMouseClicked(evt);
            }
        });
        srpTable_IndendCloseDetails.setViewportView(tblIndendCloseDetails);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.gridwidth = 7;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        panIndendCloseTbl.add(srpTable_IndendCloseDetails, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panIndendInfo.add(panIndendCloseTbl, gridBagConstraints);

        panProcess.setMaximumSize(new java.awt.Dimension(800, 50));
        panProcess.setMinimumSize(new java.awt.Dimension(800, 50));
        panProcess.setPreferredSize(new java.awt.Dimension(800, 50));
        panProcess.setLayout(new java.awt.GridBagLayout());

        btnProcess.setForeground(new java.awt.Color(0, 102, 0));
        btnProcess.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_SAVE.gif"))); // NOI18N
        btnProcess.setText("SAVE");
        btnProcess.setFont(new java.awt.Font("MS Sans Serif", 1, 13)); // NOI18N
        btnProcess.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnProcessActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panProcess.add(btnProcess, gridBagConstraints);

        btnClose1.setForeground(new java.awt.Color(255, 0, 0));
        btnClose1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_CLOSE.gif"))); // NOI18N
        btnClose1.setText("CLOSE");
        btnClose1.setFont(new java.awt.Font("MS Sans Serif", 1, 13)); // NOI18N
        btnClose1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnClose1ActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panProcess.add(btnClose1, gridBagConstraints);

        btnClear.setForeground(new java.awt.Color(0, 0, 204));
        btnClear.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_CANCEL.gif"))); // NOI18N
        btnClear.setText("CLEAR");
        btnClear.setFont(new java.awt.Font("MS Sans Serif", 1, 13)); // NOI18N
        btnClear.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnClearActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panProcess.add(btnClear, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridy = 2;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panIndendInfo.add(panProcess, gridBagConstraints);

        getContentPane().add(panIndendInfo, java.awt.BorderLayout.CENTER);

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
        tbrOperativeAcctProduct.add(btnView);

        lblSpace5.setText("     ");
        tbrOperativeAcctProduct.add(lblSpace5);

        btnNew.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_NEW.gif"))); // NOI18N
        btnNew.setToolTipText("New");
        btnNew.setEnabled(false);
        btnNew.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnNewActionPerformed(evt);
            }
        });
        tbrOperativeAcctProduct.add(btnNew);

        lblSpace51.setForeground(new java.awt.Color(240, 240, 240));
        lblSpace51.setText("     ");
        lblSpace51.setMaximumSize(new java.awt.Dimension(3, 18));
        lblSpace51.setMinimumSize(new java.awt.Dimension(3, 18));
        lblSpace51.setPreferredSize(new java.awt.Dimension(3, 18));
        tbrOperativeAcctProduct.add(lblSpace51);

        btnEdit.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_EDIT.gif"))); // NOI18N
        btnEdit.setToolTipText("Edit");
        btnEdit.setEnabled(false);
        btnEdit.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnEditActionPerformed(evt);
            }
        });
        tbrOperativeAcctProduct.add(btnEdit);

        lblSpace52.setForeground(new java.awt.Color(240, 240, 240));
        lblSpace52.setText("     ");
        lblSpace52.setMaximumSize(new java.awt.Dimension(3, 18));
        lblSpace52.setMinimumSize(new java.awt.Dimension(3, 18));
        lblSpace52.setPreferredSize(new java.awt.Dimension(3, 18));
        tbrOperativeAcctProduct.add(lblSpace52);

        btnDelete.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_DELETE.gif"))); // NOI18N
        btnDelete.setToolTipText("Delete");
        btnDelete.setEnabled(false);
        btnDelete.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnDeleteActionPerformed(evt);
            }
        });
        tbrOperativeAcctProduct.add(btnDelete);

        lblSpace2.setText("     ");
        tbrOperativeAcctProduct.add(lblSpace2);

        btnSave.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_SAVE.gif"))); // NOI18N
        btnSave.setToolTipText("Save");
        btnSave.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSaveActionPerformed(evt);
            }
        });
        tbrOperativeAcctProduct.add(btnSave);

        lblSpace53.setForeground(new java.awt.Color(240, 240, 240));
        lblSpace53.setText("     ");
        lblSpace53.setMaximumSize(new java.awt.Dimension(3, 18));
        lblSpace53.setMinimumSize(new java.awt.Dimension(3, 18));
        lblSpace53.setPreferredSize(new java.awt.Dimension(3, 18));
        tbrOperativeAcctProduct.add(lblSpace53);

        btnCancel.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_CANCEL.gif"))); // NOI18N
        btnCancel.setToolTipText("Cancel");
        btnCancel.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCancelActionPerformed(evt);
            }
        });
        tbrOperativeAcctProduct.add(btnCancel);

        lblSpace3.setText("     ");
        tbrOperativeAcctProduct.add(lblSpace3);

        btnAuthorize.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_AUTHORIZE.gif"))); // NOI18N
        btnAuthorize.setToolTipText("Authorize");
        btnAuthorize.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnAuthorizeActionPerformed(evt);
            }
        });
        tbrOperativeAcctProduct.add(btnAuthorize);

        lblSpace54.setForeground(new java.awt.Color(240, 240, 240));
        lblSpace54.setText("     ");
        lblSpace54.setMaximumSize(new java.awt.Dimension(3, 18));
        lblSpace54.setMinimumSize(new java.awt.Dimension(3, 18));
        lblSpace54.setPreferredSize(new java.awt.Dimension(3, 18));
        tbrOperativeAcctProduct.add(lblSpace54);

        btnException.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_EXCEPTION.gif"))); // NOI18N
        btnException.setToolTipText("Exception");
        btnException.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnExceptionActionPerformed(evt);
            }
        });
        tbrOperativeAcctProduct.add(btnException);

        lblSpace55.setForeground(new java.awt.Color(240, 240, 240));
        lblSpace55.setText("     ");
        lblSpace55.setMaximumSize(new java.awt.Dimension(3, 18));
        lblSpace55.setMinimumSize(new java.awt.Dimension(3, 18));
        lblSpace55.setPreferredSize(new java.awt.Dimension(3, 18));
        tbrOperativeAcctProduct.add(lblSpace55);

        btnReject.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_REJECT.gif"))); // NOI18N
        btnReject.setToolTipText("Reject");
        btnReject.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnRejectActionPerformed(evt);
            }
        });
        tbrOperativeAcctProduct.add(btnReject);

        lblSpace4.setText("     ");
        tbrOperativeAcctProduct.add(lblSpace4);

        btnPrint.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_PRINT.gif"))); // NOI18N
        btnPrint.setToolTipText("Print");
        tbrOperativeAcctProduct.add(btnPrint);

        lblSpace56.setForeground(new java.awt.Color(240, 240, 240));
        lblSpace56.setText("     ");
        lblSpace56.setMaximumSize(new java.awt.Dimension(3, 18));
        lblSpace56.setMinimumSize(new java.awt.Dimension(3, 18));
        lblSpace56.setPreferredSize(new java.awt.Dimension(3, 18));
        tbrOperativeAcctProduct.add(lblSpace56);

        btnClose.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_CLOSE.gif"))); // NOI18N
        btnClose.setToolTipText("Close");
        btnClose.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCloseActionPerformed(evt);
            }
        });
        tbrOperativeAcctProduct.add(btnClose);

        getContentPane().add(tbrOperativeAcctProduct, java.awt.BorderLayout.NORTH);

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

    private void btnViewActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnViewActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_btnViewActionPerformed

    private void cboStockTypeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cboStockTypeActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_cboStockTypeActionPerformed

    private void btnExceptionActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnExceptionActionPerformed
    }//GEN-LAST:event_btnExceptionActionPerformed

    private void btnRejectActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnRejectActionPerformed
    }//GEN-LAST:event_btnRejectActionPerformed

    private void btnAuthorizeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAuthorizeActionPerformed
    }//GEN-LAST:event_btnAuthorizeActionPerformed

    private void btnCloseActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCloseActionPerformed
        mitCloseActionPerformed(evt);
    }//GEN-LAST:event_btnCloseActionPerformed

    private void mitCloseActionPerformed(java.awt.event.ActionEvent evt) {
        cifClosingAlert();
    }

    private void btnDeleteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnDeleteActionPerformed
    }//GEN-LAST:event_btnDeleteActionPerformed

    private void btnEditActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnEditActionPerformed
    }//GEN-LAST:event_btnEditActionPerformed

    private void mitCancelActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mitCancelActionPerformed
        btnCancelActionPerformed(evt);
    }//GEN-LAST:event_mitCancelActionPerformed

    private void btnCancelActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCancelActionPerformed
        viewType = CANCEL;
        observable.resetForm();
        tblIndendCloseDetails.setModel(observable.getTblIndendCloseDetails());
        setSizeTableData();
        btnView.setEnabled(true);
        ClientUtil.enableDisable(this, false);
        lblStatus.setText("                      ");
        cust_type = null;
        setModified(false);
        btnDisplay.setEnabled(false);
    }//GEN-LAST:event_btnCancelActionPerformed

    private void btnSaveActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSaveActionPerformed
    }//GEN-LAST:event_btnSaveActionPerformed

    private void btnNewActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnNewActionPerformed
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

    private void tdtClosingDtFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_tdtClosingDtFocusLost
        // TODO add your handling code here:
    }//GEN-LAST:event_tdtClosingDtFocusLost

    private void tdtClosingDtAncestorAdded(javax.swing.event.AncestorEvent evt) {//GEN-FIRST:event_tdtClosingDtAncestorAdded
        // TODO add your handling code here:
    }//GEN-LAST:event_tdtClosingDtAncestorAdded

    private void tblIndendCloseDetailsMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tblIndendCloseDetailsMouseClicked
        // TODO add your handling code here:
        if (tblIndendCloseDetails.getRowCount() > 0) {
            if (tblIndendCloseDetails.getSelectedColumn() >= 3) {
                TableModel tblModel = (TableModel) tblIndendCloseDetails.getModel();
                String branchNo = "";
                String depoID = "";
                branchNo = CommonUtil.convertObjToStr(tblIndendCloseDetails.getValueAt(tblIndendCloseDetails.getSelectedRow(), 0));
                depoID = CommonUtil.convertObjToStr(tblIndendCloseDetails.getValueAt(tblIndendCloseDetails.getSelectedRow(), 1));
                if (!observable.getEditableStopMap().containsKey(branchNo + depoID)) {
                    tblModel.setEditColoumnNo(tblIndendCloseDetails.getSelectedColumn());
                    //System.out.println("@@@@ Edit YES : ");
                }else{
                    tblModel.setEditColoumnNo(-1);
                    //System.out.println("@@@@ Edit NO : ");
                }
            }
        }
    }//GEN-LAST:event_tblIndendCloseDetailsMouseClicked
    
    public Date getProperDateFormat(Object obj) {
        Date properDt = null;
        if (obj != null && obj.toString().length() > 0) {
            Date tempDt = DateUtil.getDateMMDDYYYY(CommonUtil.convertObjToStr(obj));
            properDt = (Date) currDt.clone();
            properDt.setDate(tempDt.getDate());
            properDt.setMonth(tempDt.getMonth());
            properDt.setYear(tempDt.getYear());
        }
        return properDt;
    }

    private void btnDisplayActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnDisplayActionPerformed
        // TODO add your handling code here:
        try {
            if (tdtClosingDt.getDateValue().length() > 0) {
                if (cboStockType.getSelectedIndex() > 0) {
                    HashMap whereMap = new HashMap();
                    whereMap.put("CLOSE_DT", getProperDateFormat(tdtClosingDt.getDateValue()));
                    whereMap.put("STOCK_TYPE", cboStockType.getSelectedItem());
                    observable.insertTableData(whereMap);
                    tblIndendCloseDetails.setModel(observable.getTblIndendCloseDetails());
                    /*tblIndendCloseDetails.setDefaultRenderer(ClosingStockType.class, new ClosingStockTypeRenderer());
                     List<ClosingStockType> finalList = new ArrayList();
                     finalList.add(new ClosingStockType("PERMANENT"));
                     finalList.add(new ClosingStockType("TEMPARORY"));
                     tblIndendCloseDetails.setDefaultEditor(ClosingStockType.class, new ClosingStockTypeCellEditor(finalList));
                     tblIndendCloseDetails.setRowHeight(25);*/
                    if (cboStockType.getSelectedItem().equals("CLOSING")) {
                        setSizeTableData();
                    } else {
                        setSizeTableDataForDamage();
                    }
                    //System.out.println("#$#$ column 4 type:"+tblIndendCloseDetails.getColumnClass(4));
                    //System.out.println("#$#$ column 5 type:"+tblIndendCloseDetails.getColumnClass(5));
                    if (tblIndendCloseDetails.getRowCount() > 0) {
                        btnDisplay.setEnabled(false);
                        ClientUtil.enableDisable(panClosingSubDetails, false);
                    } else {
                        ClientUtil.showMessageWindow("List Is Empty !!! ");
                        return;
                    }
                } else {
                    ClientUtil.showMessageWindow("Stock Type Should not be Empty !!! ");
                    return;
                }
            } else {
                ClientUtil.showMessageWindow("Closing Date Should not be Empty !!! ");
                return;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }//GEN-LAST:event_btnDisplayActionPerformed

    private void btnProcessActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnProcessActionPerformed
        // TODO add your handling code here:
        setModified(false);
        String mandatoryMessage = new MandatoryCheck().checkMandatory(getClass().getName(), panIndendClosingSubDetails);
        if (mandatoryMessage.length() > 0) {
            displayAlert(mandatoryMessage);
        } else {
            if (tblIndendCloseDetails.getRowCount() > 0) {
                updateOBFields();
                savePerformed();
            }
        }
    }//GEN-LAST:event_btnProcessActionPerformed

    private void btnClose1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnClose1ActionPerformed
        // TODO add your handling code here:
        this.dispose();
    }//GEN-LAST:event_btnClose1ActionPerformed

    private void btnClearActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnClearActionPerformed
        // TODO add your handling code here:
        viewType = CANCEL;
        observable.resetForm();
        tblIndendCloseDetails.setModel(observable.getTblIndendCloseDetails());
        setSizeTableData();
        btnView.setEnabled(true);
        ClientUtil.enableDisable(this, false);
        lblStatus.setText("                      ");
        cust_type = null;
        setModified(false);
        btnDisplay.setEnabled(false);
        ClientUtil.clearAll(this);
        btnPrint.setEnabled(false);
        ClientUtil.enableDisable(panClosingSubDetails, true);
        btnDisplay.setEnabled(true);
        tdtClosingDt.setDateValue(CommonUtil.convertObjToStr(TrueTransactMain.BANKINFO.get("YEAREND_PROCESS_DT")));
    }//GEN-LAST:event_btnClearActionPerformed

    /**
     * To populate Comboboxes
     */
    private void initComponentData() {
        cboStockType.setModel(observable.getCbmStockType());
        tblIndendCloseDetails.setModel(observable.getTblIndendCloseDetails());
    }

    /**
     * To display a popUp window for viewing existing data
     */
    private void popUp() {
        HashMap testMap = null;
        //To display customer info based on the selected ProductID
        if (viewType == 1) {
            testMap = new HashMap();
            final HashMap whereMap = new HashMap();
            testMap.put("MAPNAME", "getSelectIndendCloseDepoID");
            whereMap.put("BRANCH_CODE", TrueTransactMain.BRANCH_ID);
            testMap.put(CommonConstants.MAP_WHERE, whereMap);
            new ViewAll(this, testMap, true).show();
        } else if (observable.getActionType() == ClientConstants.ACTIONTYPE_EDIT
                || observable.getActionType() == ClientConstants.ACTIONTYPE_DELETE
                || observable.getActionType() == ClientConstants.ACTIONTYPE_VIEW) {
            testMap = new HashMap();
            testMap.put("MAPNAME", "getSelectIndendCloseEdit");
            final HashMap whereMap = new HashMap();
            whereMap.put("BRANCH_CODE", TrueTransactMain.BRANCH_ID);
            testMap.put(CommonConstants.MAP_WHERE, whereMap);
            new ViewAll(this, testMap, true).show();
        }
    }

    /**
     * Called by the Popup window created thru popUp method
     */
    public void fillData(Object obj) {
        try {
            final HashMap hash = (HashMap) obj;
            //System.out.println("filldata####" + hash);
            if (observable.getActionType() == ClientConstants.ACTIONTYPE_NEW || viewType == 1) {
            } else if (observable.getActionType() == ClientConstants.ACTIONTYPE_EDIT
                    || observable.getActionType() == ClientConstants.ACTIONTYPE_DELETE
                    || observable.getActionType() == ClientConstants.ACTIONTYPE_VIEW) {
                observable.getData(hash);
                observable.setStatus();
            }
        } catch (Exception e) {
            log.error(e);
        }
    }

    private void enableDisable(boolean yesno) {
        ClientUtil.enableDisable(this, yesno);
    }

    public void update(Observable observed, Object arg) {
        tdtClosingDt.setDateValue(observable.getTdtClosingDt());
        cboStockType.setSelectedItem(observable.getCboStockType());
        lblStatus.setText(observable.getLblStatus());
    }

    public void updateOBFields() {
        observable.setTdtClosingDt(tdtClosingDt.getDateValue());
        observable.setCboStockType(CommonUtil.convertObjToStr(cboStockType.getSelectedItem()));
    }

    private void savePerformed() {
        finalList = observable.getFinalCloseList();
        HashMap singleMap = new HashMap();
        if (finalList != null && finalList.size() > 0) {
            String branchNo = "";
            String depoID = "";
            //System.out.println("#$@$#@$@$@ FinalList : " + finalList);
            for (int i = 0; i < finalList.size(); i++) {
                singleMap = (HashMap) finalList.get(i);
                branchNo = CommonUtil.convertObjToStr(singleMap.get("BRANCH_CODE"));
                depoID = CommonUtil.convertObjToStr(singleMap.get("DEPID"));
                for (int j = 0; j < tblIndendCloseDetails.getRowCount(); j++) {
                    if (CommonUtil.convertObjToStr(tblIndendCloseDetails.getValueAt(j, 0)).equals(branchNo)
                            && (CommonUtil.convertObjToStr(tblIndendCloseDetails.getValueAt(j, 1)).equals(depoID))) {
                        singleMap.put("CLOSING_AMT", String.valueOf(CommonUtil.convertObjToStr(tblIndendCloseDetails.getValueAt(j, 3))));
                        if (cboStockType.getSelectedItem().equals("CLOSING")) {
                            singleMap.put("CLOS_PERC_LESS_AMT", String.valueOf(CommonUtil.convertObjToStr(tblIndendCloseDetails.getValueAt(j, 4))));
                        } else {
                            singleMap.put("CLOS_PERC_LESS_AMT", String.valueOf(0));
                        }
                        //singleMap.put("CLOSE_STOCK_TYPE", String.valueOf(CommonUtil.convertObjToStr(tblIndendCloseDetails.getValueAt(j, 5))));
                        break;
                    }
                }
            }
            //System.out.println("############### FINAL List Data  : " + finalList);
            observable.setFinalCloseList(finalList);
        }
        observable.doAction();
        if (observable.getProxyReturnMap() != null && observable.getProxyReturnMap().size() > 0) {
            if (observable.getProxyReturnMap().containsKey("SUCCESS")) {
                String message = "<html> <b> <font color=Red> Indend Closing Details Successfully Inserted/Updated !!! ";
                ClientUtil.showMessageWindow(message + "</font></b></html>");
            }
        }
        btnCancelActionPerformed(null);
        lblStatus.setText(ClientConstants.RESULT_STATUS[observable.getResult()]);
        setModified(false);
        ClientUtil.clearAll(this);
    }

    private void setFieldNames() {
        lblClosingDt.setName("lblClosingDt");
        lblStockType.setName("lblStockType");
        tdtClosingDt.setName("tdtClosingDt");
        cboStockType.setName("cboStockType");
    }

    private void internationalize() {
        java.util.ResourceBundle resourceBundle = java.util.ResourceBundle.getBundle("com.see.truetransact.ui.indend.closing.IndendClosingRB", ProxyParameters.LANGUAGE);
        lblClosingDt.setText(resourceBundle.getString("lblClosingDt"));
        lblStockType.setText(resourceBundle.getString("lblStockType"));
    }

    private void displayAlert(String message) {
        CMandatoryDialog cmd = new CMandatoryDialog();
        cmd.setMessage(message);
        cmd.show();
    }
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private com.see.truetransact.uicomponent.CButton btnAuthorize;
    private com.see.truetransact.uicomponent.CButton btnCancel;
    private com.see.truetransact.uicomponent.CButton btnClear;
    private com.see.truetransact.uicomponent.CButton btnClose;
    private com.see.truetransact.uicomponent.CButton btnClose1;
    private com.see.truetransact.uicomponent.CButton btnDelete;
    private com.see.truetransact.uicomponent.CButton btnDisplay;
    private com.see.truetransact.uicomponent.CButton btnEdit;
    private com.see.truetransact.uicomponent.CButton btnException;
    private com.see.truetransact.uicomponent.CButton btnNew;
    private com.see.truetransact.uicomponent.CButton btnPrint;
    private com.see.truetransact.uicomponent.CButton btnProcess;
    private com.see.truetransact.uicomponent.CButton btnReject;
    private com.see.truetransact.uicomponent.CButton btnSave;
    private com.see.truetransact.uicomponent.CButton btnView;
    private com.see.truetransact.uicomponent.CComboBox cboStockType;
    private com.see.truetransact.uicomponent.CLabel lblClosingDt;
    private com.see.truetransact.uicomponent.CLabel lblMsg;
    private com.see.truetransact.uicomponent.CLabel lblSpace1;
    private com.see.truetransact.uicomponent.CLabel lblSpace2;
    private com.see.truetransact.uicomponent.CLabel lblSpace3;
    private com.see.truetransact.uicomponent.CLabel lblSpace4;
    private com.see.truetransact.uicomponent.CLabel lblSpace5;
    private com.see.truetransact.uicomponent.CLabel lblSpace51;
    private com.see.truetransact.uicomponent.CLabel lblSpace52;
    private com.see.truetransact.uicomponent.CLabel lblSpace53;
    private com.see.truetransact.uicomponent.CLabel lblSpace54;
    private com.see.truetransact.uicomponent.CLabel lblSpace55;
    private com.see.truetransact.uicomponent.CLabel lblSpace56;
    private com.see.truetransact.uicomponent.CLabel lblStatus;
    private com.see.truetransact.uicomponent.CLabel lblStockType;
    private com.see.truetransact.uicomponent.CMenuBar mbrCustomer;
    private javax.swing.JMenuItem mitCancel;
    private javax.swing.JMenuItem mitClose;
    private javax.swing.JMenuItem mitDelete;
    private javax.swing.JMenuItem mitEdit;
    private javax.swing.JMenuItem mitNew;
    private javax.swing.JMenuItem mitPrint;
    private javax.swing.JMenuItem mitSave;
    private javax.swing.JMenu mnuProcess;
    private com.see.truetransact.uicomponent.CPanel panClosingSubDetails;
    private com.see.truetransact.uicomponent.CPanel panIndendCloseTbl;
    private com.see.truetransact.uicomponent.CPanel panIndendClosingSubDetails;
    private com.see.truetransact.uicomponent.CPanel panIndendInfo;
    private com.see.truetransact.uicomponent.CPanel panProcess;
    private com.see.truetransact.uicomponent.CPanel panStatus;
    private com.see.truetransact.uicomponent.CButtonGroup rdoTransactionType;
    private javax.swing.JSeparator sptNew;
    private javax.swing.JSeparator sptSave;
    private com.see.truetransact.uicomponent.CScrollPane srpTable_IndendCloseDetails;
    private com.see.truetransact.uicomponent.CTable tblIndendCloseDetails;
    private javax.swing.JToolBar tbrOperativeAcctProduct;
    private com.see.truetransact.uicomponent.CDateField tdtClosingDt;
    // End of variables declaration//GEN-END:variables

    public static void main(String[] args) {
        IndendClosingUI tod = new IndendClosingUI();
        javax.swing.JFrame j = new javax.swing.JFrame();
        j.getContentPane().add(tod);
        j.show();
        tod.show();
    }
}
