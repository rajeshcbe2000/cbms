/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition.
 * 
 *
 * ReserveUI.java
 *
 * Created on Jul 08, 2019, 10:53 AM  Suresh R
 */
package com.see.truetransact.ui.indend.reservedepreciationachd;

import com.see.truetransact.clientutil.ClientConstants;
import com.see.truetransact.clientutil.ClientUtil;
import com.see.truetransact.commonutil.CommonUtil;
import com.see.truetransact.uimandatory.UIMandatoryField;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Observable;
import javax.swing.DefaultCellEditor;
import javax.swing.JComboBox;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import org.apache.log4j.Logger;

/**
 *
 * @author Suresh R
 *
 */
public class ReserveDepreciationAcHdUI extends com.see.truetransact.uicomponent.CInternalFrame implements java.util.Observer, UIMandatoryField {

    private HashMap mandatoryMap;
    private ReserveDepreciationAcHdOB observable;
    private final static Logger log = Logger.getLogger(ReserveDepreciationAcHdUI.class);
    String cust_type = null;
    List acHDIncList = null;
    List acHeadList = null;
    List acHDExpList = null;

    /**
     * Creates new form ReserveDepreciationAcHdUI
     */
    public ReserveDepreciationAcHdUI() {
        initComponents();
        HashMap headMap = new HashMap();
        headMap.put("MJR_AC_HD_TYPE", "EXPENDITURE");
        acHDExpList = ClientUtil.executeQuery("getComboboxAcHDHeads", headMap);
        headMap = new HashMap();
        headMap.put("MJR_AC_HD_TYPE", "INCOME");
        acHDIncList = ClientUtil.executeQuery("getComboboxAcHDHeads", headMap);
        initStartUp();
        observable.set1IncList(acHDIncList);
        observable.set2ExpList(acHDExpList);
        initTableData();
        HashMap dataMap = new HashMap();
        String acHd = "";
        String expHd = "";
        String incHd = "";
        acHeadList = ClientUtil.executeQuery("getReserveDepreciationHeads", dataMap);
        if (acHeadList != null && acHeadList.size() > 0) {
            for (int i = 0; i < acHeadList.size(); i++) {
                dataMap = (HashMap) acHeadList.get(i);
                acHd = CommonUtil.convertObjToStr(dataMap.get("AC_HD_ID"));
                // Commented by nithya on 08-01-2022 for KD-3263
//                expHd = CommonUtil.convertObjToStr(dataMap.get("INC_AC_HD"));
//                incHd = CommonUtil.convertObjToStr(dataMap.get("EXP_AC_HD"));
                  incHd = CommonUtil.convertObjToStr(dataMap.get("INC_AC_HD"));
                  expHd = CommonUtil.convertObjToStr(dataMap.get("EXP_AC_HD"));
                ((DefaultTableModel) tblReserveDepreciationDetails.getModel()).addRow(new Object[]{acHd, incHd, expHd});
            }
        }
    }

    private Object[][] setTableData() {
        Object totalList[][] = new Object[0][3];
        return totalList;
    }

    private void initTableData() {
        tblReserveDepreciationDetails.setModel(new javax.swing.table.DefaultTableModel(
                setTableData(),
                new String[]{
                    "AC_HD_ID", "INC_AC_HD", "EXP_AC_HD"}) {

            Class[] types = new Class[]{
                java.lang.String.class,
                JComboBox.class,
                JComboBox.class};
            boolean[] canEdit = new boolean[]{
                false, true, true
            };

            @Override
            public Class getColumnClass(int columnIndex) {
                return types[columnIndex];
            }

            @Override
            public boolean isCellEditable(int rowIndex, int columnIndex) {
                if (observable.getActionType() == ClientConstants.ACTIONTYPE_NEW
                        && (rowIndex == 0)) {
                    if (columnIndex == 1 || columnIndex == 2) {
                        return false;
                    }
                }
                return canEdit[columnIndex];
            }
        });

        tblReserveDepreciationDetails.getColumnModel().getColumn(1).setCellEditor(new DefaultCellEditor(generateInc()));
        DefaultTableCellRenderer renderer = new DefaultTableCellRenderer();
        renderer.setToolTipText("Click for combo box");
        tblReserveDepreciationDetails.getColumnModel().getColumn(1).setCellRenderer(renderer);
        tblReserveDepreciationDetails.getColumnModel().getColumn(2).setCellEditor(new DefaultCellEditor(generateExp()));
        DefaultTableCellRenderer renderer1 = new DefaultTableCellRenderer();
        renderer1.setToolTipText("Click for combo box");
        tblReserveDepreciationDetails.getColumnModel().getColumn(2).setCellRenderer(renderer1);
    }

    private JavaCAutoComboBox generateInc() {
        List listItem = new ArrayList();
        if (acHDIncList != null && acHDIncList.size() > 0) {
            List lis = observable.getIncList();
            for (int i = 0; i < lis.size(); i++) {
                listItem.add(lis.get(i));
            }
            JavaCAutoComboBox comboBox = new JavaCAutoComboBox(listItem);
            comboBox.setDataList(listItem);
            listItem = null;
            lis = null;
            return comboBox;
        }
        return null;
    }

    private JavaCAutoComboBox generateExp() {
        List listItem = new ArrayList();
        if (acHDExpList != null && acHDExpList.size() > 0) {
            List lis = observable.getExpList();
            for (int i = 0; i < lis.size(); i++) {
                listItem.add(lis.get(i));
            }
            JavaCAutoComboBox comboBox = new JavaCAutoComboBox(listItem);
            comboBox.setDataList(listItem);
            listItem = null;
            lis = null;
            return comboBox;
        }
        return null;
    }

    private void initStartUp() {

        observable = new ReserveDepreciationAcHdOB();
        observable.addObserver(this);
        enableDisable(false);
        btnAuthorize.setVisible(false);
        btnReject.setVisible(false);
        btnException.setVisible(false);
        setSizeTableData();
        tbrOperativeAcctProduct.setVisible(false);
    }

    private void setSizeTableData() {
        tblReserveDepreciationDetails.getColumnModel().getColumn(0).setPreferredWidth(250);
        tblReserveDepreciationDetails.getColumnModel().getColumn(1).setPreferredWidth(250);
        tblReserveDepreciationDetails.getColumnModel().getColumn(2).setPreferredWidth(250);
    }

    @Override
    public void setMandatoryHashMap() {
        mandatoryMap = new HashMap();
    }

    public HashMap getMandatoryHashMap() {
        return mandatoryMap;
    }

    /**
     * ********** END OF NEW METHODS **************
     */
    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {
        java.awt.GridBagConstraints gridBagConstraints;

        panReserveDepreciationDetails = new com.see.truetransact.uicomponent.CPanel();
        panReserveDepreciationTbl = new com.see.truetransact.uicomponent.CPanel();
        srpTable_ReserveDepreciationDetails = new com.see.truetransact.uicomponent.CScrollPane();
        tblReserveDepreciationDetails = new com.see.truetransact.uicomponent.CTable();
        panProcess = new com.see.truetransact.uicomponent.CPanel();
        btnDisplay1 = new com.see.truetransact.uicomponent.CButton();
        btnClose1 = new com.see.truetransact.uicomponent.CButton();
        btnClear = new com.see.truetransact.uicomponent.CButton();
        tbrOperativeAcctProduct = new javax.swing.JToolBar();
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

        panReserveDepreciationDetails.setBorder(javax.swing.BorderFactory.createTitledBorder(""));
        panReserveDepreciationDetails.setMinimumSize(new java.awt.Dimension(360, 260));
        panReserveDepreciationDetails.setPreferredSize(new java.awt.Dimension(360, 260));
        panReserveDepreciationDetails.setLayout(new java.awt.GridBagLayout());

        panReserveDepreciationTbl.setBorder(javax.swing.BorderFactory.createTitledBorder(""));
        panReserveDepreciationTbl.setMinimumSize(new java.awt.Dimension(845, 500));
        panReserveDepreciationTbl.setPreferredSize(new java.awt.Dimension(845, 500));
        panReserveDepreciationTbl.setLayout(new java.awt.GridBagLayout());

        srpTable_ReserveDepreciationDetails.setMaximumSize(new java.awt.Dimension(552, 552));
        srpTable_ReserveDepreciationDetails.setMinimumSize(new java.awt.Dimension(552, 552));
        srpTable_ReserveDepreciationDetails.setPreferredSize(new java.awt.Dimension(552, 552));

        tblReserveDepreciationDetails.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "AC_HD_ID", "INC_AC_HD", "EXP_AC_HD"
            }
        ));
        tblReserveDepreciationDetails.setPreferredScrollableViewportSize(new java.awt.Dimension(850, 850));
        tblReserveDepreciationDetails.setSelectionBackground(new java.awt.Color(204, 204, 255));
        tblReserveDepreciationDetails.setSelectionForeground(new java.awt.Color(0, 0, 0));
        tblReserveDepreciationDetails.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tblReserveDepreciationDetailsMouseClicked(evt);
            }
        });
        srpTable_ReserveDepreciationDetails.setViewportView(tblReserveDepreciationDetails);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.gridwidth = 7;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        panReserveDepreciationTbl.add(srpTable_ReserveDepreciationDetails, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panReserveDepreciationDetails.add(panReserveDepreciationTbl, gridBagConstraints);

        panProcess.setMaximumSize(new java.awt.Dimension(800, 50));
        panProcess.setMinimumSize(new java.awt.Dimension(800, 50));
        panProcess.setPreferredSize(new java.awt.Dimension(800, 30));
        panProcess.setLayout(new java.awt.GridBagLayout());

        btnDisplay1.setForeground(new java.awt.Color(0, 102, 0));
        btnDisplay1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_AUTHORIZE.gif"))); // NOI18N
        btnDisplay1.setText("SAVE");
        btnDisplay1.setFont(new java.awt.Font("MS Sans Serif", 1, 13)); // NOI18N
        btnDisplay1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnDisplay1ActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panProcess.add(btnDisplay1, gridBagConstraints);

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
        gridBagConstraints.gridy = 3;
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
        gridBagConstraints.gridy = 3;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panProcess.add(btnClear, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridy = 2;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panReserveDepreciationDetails.add(panProcess, gridBagConstraints);

        getContentPane().add(panReserveDepreciationDetails, java.awt.BorderLayout.CENTER);

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

        lblSpace17.setForeground(new java.awt.Color(240, 240, 240));
        lblSpace17.setText("     ");
        lblSpace17.setMaximumSize(new java.awt.Dimension(3, 18));
        lblSpace17.setMinimumSize(new java.awt.Dimension(3, 18));
        lblSpace17.setPreferredSize(new java.awt.Dimension(3, 18));
        tbrOperativeAcctProduct.add(lblSpace17);

        btnEdit.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_EDIT.gif"))); // NOI18N
        btnEdit.setToolTipText("Edit");
        btnEdit.setEnabled(false);
        btnEdit.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnEditActionPerformed(evt);
            }
        });
        tbrOperativeAcctProduct.add(btnEdit);

        lblSpace18.setForeground(new java.awt.Color(240, 240, 240));
        lblSpace18.setText("     ");
        lblSpace18.setMaximumSize(new java.awt.Dimension(3, 18));
        lblSpace18.setMinimumSize(new java.awt.Dimension(3, 18));
        lblSpace18.setPreferredSize(new java.awt.Dimension(3, 18));
        tbrOperativeAcctProduct.add(lblSpace18);

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

        lblSpace19.setForeground(new java.awt.Color(240, 240, 240));
        lblSpace19.setText("     ");
        lblSpace19.setMaximumSize(new java.awt.Dimension(3, 18));
        lblSpace19.setMinimumSize(new java.awt.Dimension(3, 18));
        lblSpace19.setPreferredSize(new java.awt.Dimension(3, 18));
        tbrOperativeAcctProduct.add(lblSpace19);

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

        lblSpace20.setForeground(new java.awt.Color(240, 240, 240));
        lblSpace20.setText("     ");
        lblSpace20.setMaximumSize(new java.awt.Dimension(3, 18));
        lblSpace20.setMinimumSize(new java.awt.Dimension(3, 18));
        lblSpace20.setPreferredSize(new java.awt.Dimension(3, 18));
        tbrOperativeAcctProduct.add(lblSpace20);

        btnException.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_EXCEPTION.gif"))); // NOI18N
        btnException.setToolTipText("Exception");
        btnException.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnExceptionActionPerformed(evt);
            }
        });
        tbrOperativeAcctProduct.add(btnException);

        lblSpace21.setForeground(new java.awt.Color(240, 240, 240));
        lblSpace21.setText("     ");
        lblSpace21.setMaximumSize(new java.awt.Dimension(3, 18));
        lblSpace21.setMinimumSize(new java.awt.Dimension(3, 18));
        lblSpace21.setPreferredSize(new java.awt.Dimension(3, 18));
        tbrOperativeAcctProduct.add(lblSpace21);

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

        lblSpace22.setForeground(new java.awt.Color(240, 240, 240));
        lblSpace22.setText("     ");
        lblSpace22.setMaximumSize(new java.awt.Dimension(3, 18));
        lblSpace22.setMinimumSize(new java.awt.Dimension(3, 18));
        lblSpace22.setPreferredSize(new java.awt.Dimension(3, 18));
        tbrOperativeAcctProduct.add(lblSpace22);

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

        tblReserveDepreciationDetails.setModel(observable.getTblIndendCloseDetails());
        setSizeTableData();
        btnView.setEnabled(true);
        ClientUtil.enableDisable(this, false);
        lblStatus.setText("                      ");
        cust_type = null;
        setModified(false);
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

    private void tblReserveDepreciationDetailsMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tblReserveDepreciationDetailsMouseClicked
        // TODO add your handling code here:
    }//GEN-LAST:event_tblReserveDepreciationDetailsMouseClicked

    private void btnDisplay1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnDisplay1ActionPerformed
        // TODO add your handling code here:
        if (tblReserveDepreciationDetails.getRowCount() > 0) {
            setModified(false);
            for (int i = 0; i < tblReserveDepreciationDetails.getRowCount(); i++) {
                if (CommonUtil.convertObjToStr(tblReserveDepreciationDetails.getValueAt(i, 1)).length() <= 0
                        || CommonUtil.convertObjToStr(tblReserveDepreciationDetails.getValueAt(i, 2)).length() <= 0) {
                    ClientUtil.showAlertWindow("Please Enter The INC/EXP Account Heads !!!");
                    return;
                }
            }
            savePerformed();
        }
    }//GEN-LAST:event_btnDisplay1ActionPerformed

    private void btnClose1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnClose1ActionPerformed
        // TODO add your handling code here:
        this.dispose();
    }//GEN-LAST:event_btnClose1ActionPerformed

    private void btnClearActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnClearActionPerformed

        tblReserveDepreciationDetails.setModel(observable.getTblIndendCloseDetails());
        setSizeTableData();
        ClientUtil.enableDisable(this, false);
        lblStatus.setText("                      ");
        cust_type = null;
        setModified(false);
        ClientUtil.clearAll(this);

    }//GEN-LAST:event_btnClearActionPerformed

    public void fillData(Object obj) {
        try {
            final HashMap hash = (HashMap) obj;
            System.out.println("filldata####" + hash);
            if (observable.getActionType() == ClientConstants.ACTIONTYPE_NEW) {
            }
        } catch (Exception e) {
            log.error(e);
        }
    }

    private void enableDisable(boolean yesno) {
        ClientUtil.enableDisable(this, yesno);
    }

    public void update(Observable observed, Object arg) {

        lblStatus.setText(observable.getLblStatus());
    }

    private void savePerformed() {

        HashMap singleMap = new HashMap();
        if (acHeadList != null && acHeadList.size() > 0) {
            String acHDID = "";
            System.out.println("#$@$#@$@$@ acHeadList : " + acHeadList);
            for (int i = 0; i < acHeadList.size(); i++) {

                singleMap = (HashMap) acHeadList.get(i);
                acHDID = CommonUtil.convertObjToStr(singleMap.get("AC_HD_ID"));
                for (int j = 0; j < tblReserveDepreciationDetails.getRowCount(); j++) {
                    if (CommonUtil.convertObjToStr(tblReserveDepreciationDetails.getValueAt(j, 0)).equals(acHDID)) {
                        singleMap.put("INC_AC_HD", String.valueOf(CommonUtil.convertObjToStr(tblReserveDepreciationDetails.getValueAt(j, 1))));
                        singleMap.put("EXP_AC_HD", String.valueOf(CommonUtil.convertObjToStr(tblReserveDepreciationDetails.getValueAt(j, 2))));
                        break;
                    }
                }
            }
            System.out.println("############### FINAL List Data  : " + acHeadList);
            observable.setFinalCloseList(acHeadList);
        }
        observable.doAction();
        if (observable.getResult() != ClientConstants.ACTIONTYPE_FAILED) {
            btnCancelActionPerformed(null);
            lblStatus.setText(ClientConstants.RESULT_STATUS[observable.getResult()]);
        }
        setModified(false);
        ClientUtil.clearAll(this);
    }
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private com.see.truetransact.uicomponent.CButton btnAuthorize;
    private com.see.truetransact.uicomponent.CButton btnCancel;
    private com.see.truetransact.uicomponent.CButton btnClear;
    private com.see.truetransact.uicomponent.CButton btnClose;
    private com.see.truetransact.uicomponent.CButton btnClose1;
    private com.see.truetransact.uicomponent.CButton btnDelete;
    private com.see.truetransact.uicomponent.CButton btnDisplay1;
    private com.see.truetransact.uicomponent.CButton btnEdit;
    private com.see.truetransact.uicomponent.CButton btnException;
    private com.see.truetransact.uicomponent.CButton btnNew;
    private com.see.truetransact.uicomponent.CButton btnPrint;
    private com.see.truetransact.uicomponent.CButton btnReject;
    private com.see.truetransact.uicomponent.CButton btnSave;
    private com.see.truetransact.uicomponent.CButton btnView;
    private com.see.truetransact.uicomponent.CLabel lblMsg;
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
    private com.see.truetransact.uicomponent.CPanel panProcess;
    private com.see.truetransact.uicomponent.CPanel panReserveDepreciationDetails;
    private com.see.truetransact.uicomponent.CPanel panReserveDepreciationTbl;
    private com.see.truetransact.uicomponent.CPanel panStatus;
    private javax.swing.JSeparator sptNew;
    private javax.swing.JSeparator sptSave;
    private com.see.truetransact.uicomponent.CScrollPane srpTable_ReserveDepreciationDetails;
    private com.see.truetransact.uicomponent.CTable tblReserveDepreciationDetails;
    private javax.swing.JToolBar tbrOperativeAcctProduct;
    // End of variables declaration//GEN-END:variables

    public static void main(String[] args) {
        ReserveDepreciationAcHdUI tod = new ReserveDepreciationAcHdUI();
        javax.swing.JFrame j = new javax.swing.JFrame();
        j.getContentPane().add(tod);
        j.show();
        tod.show();
    }
}
