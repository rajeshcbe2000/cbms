/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition.  
 * 
 * DepositReprintUI.java
 */
package com.see.truetransact.ui.deposit.print;

import java.util.Observable;
import com.see.truetransact.clientutil.CMandatoryDialog;
import com.see.truetransact.clientproxy.ProxyParameters;
import com.see.truetransact.clientutil.ClientUtil;
import com.see.truetransact.commonutil.CommonUtil;
import com.see.truetransact.uicomponent.COptionPane;
import com.see.truetransact.commonutil.DateUtil;
import com.see.truetransact.commonutil.CommonConstants;
import java.awt.Dimension;
import com.see.truetransact.clientutil.ComboBoxModel;
import com.see.truetransact.clientutil.EnhancedTableModel;
import com.see.truetransact.ui.common.viewall.ViewAll;
import com.see.truetransact.clientutil.ttrintegration.TTIntegration;
import java.awt.Toolkit;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.ListIterator;
import javax.swing.event.TableModelListener;
import javax.swing.table.DefaultTableModel;
import com.see.truetransact.ui.TrueTransactMain;

public class DepositReprintUI extends com.see.truetransact.uicomponent.CInternalFrame implements java.util.Observer {
//final int CASH = 0, TRANS = 1;
//int viewType = -1;

    boolean flag = false;
    Date currDt = null;
    private String viewType = "";
    private DepositReprintOB observable;
    private boolean selectMode = false;
    public ArrayList partsC = new ArrayList();
    private TableModelListener tableModelListener;
    private ArrayList key;
    private ArrayList value;
    private ComboBoxModel cbmTransType;

    public ComboBoxModel getCbmTransType() {
        return cbmTransType;
    }

    public void setCbmTransType(ComboBoxModel cbmTransType) {
        this.cbmTransType = cbmTransType;
    }

    /** Creates new form CustomerIdChangeUI */
    public DepositReprintUI() {
        initComponents();
        setObservable();
        initStartUp();
        currDt = ClientUtil.getCurrentDate();
        System.out.println("currDt###" + currDt);
        //txtFromNo.setDateValue(DateUtil.getStringDate(currDt));
        //txtFromNo.setDateValue(DateUtil.getStringDate(currDt));
        tblReprint.getModel().addTableModelListener(tableModelListener);
        chkSelectAll.setSelected(false);
    }

    private void setObservable() {
        try {
            observable = DepositReprintOB.getInstance();
            observable.addObserver(this);
        } catch (Exception e) {
            System.out.println("Error in setObservable():" + e);
        }
    }

    private void initStartUp() {
        //tdtFromDt.setEnabled(true);
        //tdtToDate.setEnabled(true);
        // Date currDt = null;
        setFieldNames();
        internationalize();
        initComponentData();
        //fillCombo();

    }

    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {
        java.awt.GridBagConstraints gridBagConstraints;

        panStatus = new com.see.truetransact.uicomponent.CPanel();
        lblMsg = new com.see.truetransact.uicomponent.CLabel();
        panParameters = new com.see.truetransact.uicomponent.CPanel();
        lblCDate = new com.see.truetransact.uicomponent.CLabel();
        lblCDate1 = new com.see.truetransact.uicomponent.CLabel();
        btnProdId = new com.see.truetransact.uicomponent.CButton();
        txtProductID = new com.see.truetransact.uicomponent.CTextField();
        txtToNo = new com.see.truetransact.uicomponent.CTextField();
        btnToNo = new com.see.truetransact.uicomponent.CButton();
        btnFromNo1 = new com.see.truetransact.uicomponent.CButton();
        txtFromNo = new com.see.truetransact.uicomponent.CTextField();
        lblCDate2 = new com.see.truetransact.uicomponent.CLabel();
        panTable = new com.see.truetransact.uicomponent.CPanel();
        srpPrintPan = new com.see.truetransact.uicomponent.CScrollPane();
        tblReprint = new com.see.truetransact.uicomponent.CTable();
        panPrint = new com.see.truetransact.uicomponent.CPanel();
        btnPrint = new com.see.truetransact.uicomponent.CButton();
        btnSearch = new com.see.truetransact.uicomponent.CButton();
        chkSelectAll = new com.see.truetransact.uicomponent.CCheckBox();
        chkViewPrint = new com.see.truetransact.uicomponent.CCheckBox();
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
        setMaximumSize(new java.awt.Dimension(600, 500));
        setMinimumSize(new java.awt.Dimension(600, 500));
        setPreferredSize(new java.awt.Dimension(700, 500));
        getContentPane().setLayout(new java.awt.GridBagLayout());

        panStatus.setLayout(new java.awt.GridBagLayout());
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        panStatus.add(lblMsg, gridBagConstraints);

        getContentPane().add(panStatus, new java.awt.GridBagConstraints());

        panParameters.setLayout(new java.awt.GridBagLayout());

        lblCDate.setText("Prod Id");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 0, 6);
        panParameters.add(lblCDate, gridBagConstraints);

        lblCDate1.setText(" ToNo");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 6;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 9, 0, 2);
        panParameters.add(lblCDate1, gridBagConstraints);

        btnProdId.setForeground(new java.awt.Color(0, 0, 153));
        btnProdId.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_SELECT.gif"))); // NOI18N
        btnProdId.setFont(new java.awt.Font("MS Sans Serif", 1, 13)); // NOI18N
        btnProdId.setMaximumSize(new java.awt.Dimension(83, 27));
        btnProdId.setMinimumSize(new java.awt.Dimension(83, 27));
        btnProdId.setPreferredSize(new java.awt.Dimension(21, 21));
        btnProdId.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnProdIdActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 0;
        panParameters.add(btnProdId, gridBagConstraints);

        txtProductID.setAllowAll(true);
        txtProductID.setPreferredSize(new java.awt.Dimension(50, 21));
        txtProductID.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                txtProductIDFocusLost(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        panParameters.add(txtProductID, gridBagConstraints);

        txtToNo.setAllowAll(true);
        txtToNo.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                txtToNoFocusLost(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 7;
        gridBagConstraints.gridy = 0;
        panParameters.add(txtToNo, gridBagConstraints);

        btnToNo.setForeground(new java.awt.Color(0, 0, 153));
        btnToNo.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_SELECT.gif"))); // NOI18N
        btnToNo.setFont(new java.awt.Font("MS Sans Serif", 1, 13)); // NOI18N
        btnToNo.setMaximumSize(new java.awt.Dimension(83, 27));
        btnToNo.setMinimumSize(new java.awt.Dimension(83, 27));
        btnToNo.setPreferredSize(new java.awt.Dimension(21, 21));
        btnToNo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnToNoActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 8;
        gridBagConstraints.gridy = 0;
        panParameters.add(btnToNo, gridBagConstraints);

        btnFromNo1.setForeground(new java.awt.Color(0, 0, 153));
        btnFromNo1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_SELECT.gif"))); // NOI18N
        btnFromNo1.setFont(new java.awt.Font("MS Sans Serif", 1, 13)); // NOI18N
        btnFromNo1.setMaximumSize(new java.awt.Dimension(83, 27));
        btnFromNo1.setMinimumSize(new java.awt.Dimension(83, 27));
        btnFromNo1.setPreferredSize(new java.awt.Dimension(21, 21));
        btnFromNo1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnFromNo1ActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 5;
        gridBagConstraints.gridy = 0;
        panParameters.add(btnFromNo1, gridBagConstraints);

        txtFromNo.setAllowAll(true);
        txtFromNo.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                txtFromNoFocusLost(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 4;
        gridBagConstraints.gridy = 0;
        panParameters.add(txtFromNo, gridBagConstraints);

        lblCDate2.setText("FromNo");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(0, 12, 0, 3);
        panParameters.add(lblCDate2, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.insets = new java.awt.Insets(0, 87, 0, 5);
        getContentPane().add(panParameters, gridBagConstraints);

        panTable.setLayout(new java.awt.GridBagLayout());

        srpPrintPan.setMinimumSize(new java.awt.Dimension(300, 404));
        srpPrintPan.setOpaque(false);
        srpPrintPan.setPreferredSize(new java.awt.Dimension(600, 300));

        tblReprint.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Select", "Deposit No", "Behaviour", "Name", "Amount", "Deposit Date", "Maturity Date"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                true, false, true, false, false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        tblReprint.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tblReprintMouseClicked(evt);
            }
        });
        srpPrintPan.setViewportView(tblReprint);

        panTable.add(srpPrintPan, new java.awt.GridBagConstraints());

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 2;
        getContentPane().add(panTable, gridBagConstraints);

        panPrint.setLayout(new java.awt.GridBagLayout());

        btnPrint.setForeground(new java.awt.Color(0, 0, 153));
        btnPrint.setText("Print");
        btnPrint.setFont(new java.awt.Font("MS Sans Serif", 1, 13)); // NOI18N
        btnPrint.setMaximumSize(new java.awt.Dimension(83, 27));
        btnPrint.setMinimumSize(new java.awt.Dimension(83, 27));
        btnPrint.setPreferredSize(new java.awt.Dimension(83, 27));
        btnPrint.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnPrintActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        panPrint.add(btnPrint, gridBagConstraints);

        btnSearch.setForeground(new java.awt.Color(0, 0, 153));
        btnSearch.setText("Search");
        btnSearch.setToolTipText("Search");
        btnSearch.setFont(new java.awt.Font("MS Sans Serif", 1, 13)); // NOI18N
        btnSearch.setMaximumSize(new java.awt.Dimension(110, 27));
        btnSearch.setMinimumSize(new java.awt.Dimension(110, 27));
        btnSearch.setPreferredSize(new java.awt.Dimension(110, 27));
        btnSearch.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSearchActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        panPrint.add(btnSearch, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 3;
        getContentPane().add(panPrint, gridBagConstraints);

        chkSelectAll.setText("Select All");
        chkSelectAll.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                chkSelectAllActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        getContentPane().add(chkSelectAll, gridBagConstraints);

        chkViewPrint.setText("Preview");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 0, 130);
        getContentPane().add(chkViewPrint, gridBagConstraints);

        mbrCustomer.setName("mbrCustomer"); // NOI18N

        mnuProcess.setText("Reprint");
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

    private void mitCancelActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mitCancelActionPerformed
    }//GEN-LAST:event_mitCancelActionPerformed

    private void mitDeleteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mitDeleteActionPerformed
    }//GEN-LAST:event_mitDeleteActionPerformed

    private void mitEditActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mitEditActionPerformed
    }//GEN-LAST:event_mitEditActionPerformed

    private void mitNewActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mitNewActionPerformed
    }//GEN-LAST:event_mitNewActionPerformed

    private void mitSaveActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mitSaveActionPerformed
    }//GEN-LAST:event_mitSaveActionPerformed

private void tblReprintMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tblReprintMouseClicked
// TODO add your handling code here:
    System.out.println("reached check action performed");
    boolean chk = ((Boolean) tblReprint.getValueAt(tblReprint.getSelectedRow(), 0)).booleanValue();
    String strNodeUnselec = "";
    System.out.println("checkBoxNode.selected : " + chk);
    if (!chk) {
        String singleTranId = CommonUtil.convertObjToStr(tblReprint.getValueAt(tblReprint.getSelectedRow(), 1));
        String behaviour = CommonUtil.convertObjToStr(tblReprint.getValueAt(tblReprint.getSelectedRow(), 2));
        if (partsC.contains(singleTranId)) {
            partsC.remove(singleTranId);
        }
        partsC.add(singleTranId);
        tblReprint.setValueAt(true, tblReprint.getSelectedRow(), 0);
    } else {
        String singleTranId = CommonUtil.convertObjToStr(tblReprint.getValueAt(tblReprint.getSelectedRow(), 1));
        String behaviour = CommonUtil.convertObjToStr(tblReprint.getValueAt(tblReprint.getSelectedRow(), 2));
        System.out.println("nodeVal=====" + singleTranId);
        strNodeUnselec = singleTranId;
        System.out.println("nodeVal unselec : " + singleTranId);
        partsC.remove(singleTranId);
        tblReprint.setValueAt(false, tblReprint.getSelectedRow(), 0);
    }
}//GEN-LAST:event_tblReprintMouseClicked

private void btnPrintActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnPrintActionPerformed
// TODO add your handling code here:
    ArrayList branAryLst = new ArrayList();
    HashMap DepoMap;
    boolean count = false;
    int printCount = 0;
    int dataCount = 0;
    boolean selectedcount = false;
    try {
        if (tblReprint.getRowCount() > 0) {            
            for (int i = 0; i < tblReprint.getRowCount(); i++) {
                  if ((Boolean) tblReprint.getValueAt(i, 0)) {
                        selectedcount = true;
                   }
            }
            if(!selectedcount){
                ClientUtil.showMessageWindow("No row selected !!!"); 
                selectedcount = false;
                return;
            }
            int yesNo = 0;
            String[] options = {"Yes", "No"};
            yesNo = COptionPane.showOptionDialog(null, "Do you want to print " + partsC.size() + " Items ?", CommonConstants.WARNINGTITLE,
                    COptionPane.OK_OPTION, COptionPane.WARNING_MESSAGE,
                    null, options, options[0]);
            dataCount = tblReprint.getRowCount();
            String depositNo = "";
            if (yesNo == 0) {
                for (int i = 0; i < dataCount; i++) {
                    if ((Boolean) tblReprint.getValueAt(i, 0)) {
                        DepoMap = new HashMap();
                        DepoMap.put("DEPOSIT_NO", CommonUtil.convertObjToStr(tblReprint.getValueAt(i, 1)));
                        DepoMap.put("BEHAVES_LIKE", CommonUtil.convertObjToStr(tblReprint.getValueAt(i, 2)));
                        print(DepoMap);
                    }
                }
            }
            uncheckTable();
        } else {
            ClientUtil.showMessageWindow("No Data !!!");
            return;
        }
    } catch (Exception e) {
        ClientUtil.showMessageWindow(e.getMessage());
    }
}//GEN-LAST:event_btnPrintActionPerformed

private void btnProdIdActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnProdIdActionPerformed
// TODO add your handling code here:
    callView("PROD_ID");

}//GEN-LAST:event_btnProdIdActionPerformed

private void chkSelectAllActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_chkSelectAllActionPerformed
// TODO add your handling code here:
    if (chkSelectAll.isSelected()) {
        for (int i = 0; i < tblReprint.getRowCount(); i++) {
            tblReprint.setValueAt(new Boolean(true), i, 0);
        }
    } else if (!chkSelectAll.isSelected()) {
        for (int i = 0; i < tblReprint.getRowCount(); i++) {
            tblReprint.setValueAt(new Boolean(false), i, 0);
        }
    }
}//GEN-LAST:event_chkSelectAllActionPerformed

private void btnToNoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnToNoActionPerformed
// TODO add your handling code here:
    if (txtProductID.getText() != null && txtProductID.getText().length() > 0) {
        callView("TO_NO");
    } else {
        ClientUtil.showMessageWindow("Please select prodId!!!");
    }

}//GEN-LAST:event_btnToNoActionPerformed

private void btnFromNo1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnFromNo1ActionPerformed
// TODO add your handling code here:    
    if (txtProductID.getText() != null && txtProductID.getText().length() > 0) {
        callView("FROM_NO");
    } else {
        ClientUtil.showMessageWindow("Please select prodId!!!");
    }
}//GEN-LAST:event_btnFromNo1ActionPerformed

private void btnSearchActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSearchActionPerformed
// TODO add your handling code here:
    HashMap shareTypeMap = new HashMap();
    HashMap whereMap = new HashMap();
    try {
        // Added by nithya on 01-06-2018 for 0011354: soubagyavathy deposit scheme bond print 
        HashMap chkMap = new HashMap();
        int dep_freq = 0;
        chkMap.put("PID", txtProductID.getText());
        List chklist = ClientUtil.executeQuery("getDailyDepositFrequency", chkMap);
        if (chklist != null && chklist.size() > 0) {
            chkMap = (HashMap) chklist.get(0);
            if (chkMap != null && chkMap.containsKey("DEPOSIT_FREQ") && chkMap.get("DEPOSIT_FREQ") != null) {
                dep_freq = CommonUtil.convertObjToInt(chkMap.get("DEPOSIT_FREQ"));
                if (dep_freq == 365) {
                    whereMap.put("INCLUDE_RD","INCLUDE_RD");
                }else{
                    whereMap.put("EXCLUDE_RD","EXCLUDE_RD");
                }
            }else{
                 whereMap.put("EXCLUDE_RD","EXCLUDE_RD");
            }
        }else{
            whereMap.put("EXCLUDE_RD","EXCLUDE_RD");
        }
        if (txtFromNo.getText() != null && txtToNo.getText() != null) {
            whereMap.put("FROM_NO", CommonUtil.convertObjToStr(txtFromNo.getText()));
            whereMap.put("TO_NO", CommonUtil.convertObjToStr(txtToNo.getText()));
            whereMap.put(CommonConstants.BRANCH_ID, ProxyParameters.BRANCH_ID);
            //System.out.println("whereMap :: "+whereMap);
            shareTypeMap.put(CommonConstants.MAP_NAME, "getDepositReprintData");
            shareTypeMap.put(CommonConstants.MAP_WHERE, whereMap);
            observable.populateData(shareTypeMap, tblReprint);
            tblReprint.setModel(observable.getTblReciept());
            selectMode = true;
        } else {
            ClientUtil.showAlertWindow("Please fill the Options!!!");
            return;
        }
        partsC.clear();
    } catch (Exception e) {
        System.err.println("Exception " + e.toString() + "Caught");
        e.printStackTrace();
        ClientUtil.showMessageWindow(e.getMessage());
    }
    tblReprint.setEditingColumn(0);
}//GEN-LAST:event_btnSearchActionPerformed

private void txtProductIDFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtProductIDFocusLost
// TODO add your handling code here:
    if (txtProductID.getText().length() > 0) {
        HashMap whereMap = new HashMap();
        whereMap.put("PROD_ID", txtProductID.getText());
        List lst = ClientUtil.executeQuery("getAcctHead", whereMap);
        if (lst != null && lst.size() > 0) {
            whereMap = (HashMap) lst.get(0);
        } else {
            ClientUtil.displayAlert("Invalid Product ID !!! ");
            txtProductID.setText("");
            return;
        }
    }
}//GEN-LAST:event_txtProductIDFocusLost

private void txtFromNoFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtFromNoFocusLost
// TODO add your handling code here:
    if (txtFromNo.getText().length() > 0) {
        HashMap hash = new HashMap();
        hash.put("PROD_ID", txtProductID.getText());
        hash.put("CURR_DATE", setProperDtFormat(currDt));
        hash.put("DEPOSIT_NO", txtFromNo.getText());
        hash.put(CommonConstants.BRANCH_ID, TrueTransactMain.BRANCH_ID);
        List lst = ClientUtil.executeQuery("TDCharges.getAcctList", hash);
        if (lst != null && lst.size() > 0) {
        } else {
            ClientUtil.displayAlert("InValid Number");
            txtFromNo.setText("");
            return;
        }
    }
}//GEN-LAST:event_txtFromNoFocusLost

private void txtToNoFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtToNoFocusLost
// TODO add your handling code here:
    if (txtToNo.getText().length() > 0) {
        HashMap hash = new HashMap();
        hash.put("PROD_ID", txtProductID.getText());
        hash.put("DEPOSIT_NO", txtToNo.getText());
        hash.put("CURR_DATE", setProperDtFormat(currDt));
        hash.put(CommonConstants.BRANCH_ID, TrueTransactMain.BRANCH_ID);
        List lst = ClientUtil.executeQuery("TDCharges.getAcctList", hash);
        if (lst != null && lst.size() > 0) {
        } else {
            ClientUtil.displayAlert("InValid Number");
            txtToNo.setText("");
            return;
        }
    }
}//GEN-LAST:event_txtToNoFocusLost
    private void resetFields() {
        txtProductID.setText("");
        txtToNo.setText("");

    }

    private void uncheckTable() {
        for (int i = 0; i < tblReprint.getRowCount(); i++) {
            tblReprint.setValueAt(new Boolean(false), i, 0);
        }
    }

    private void callView(String currField) {
        viewType = currField;
        HashMap viewMap = new HashMap();
        HashMap hash = new HashMap();
        if (currField.equalsIgnoreCase("TO_NO") || currField.equalsIgnoreCase("FROM_NO")) {
            hash.put("PROD_ID", txtProductID.getText());
            hash.put("CURR_DATE", setProperDtFormat(currDt));
            hash.put(CommonConstants.BRANCH_ID, com.see.truetransact.ui.TrueTransactMain.BRANCH_ID);
            viewMap.put(CommonConstants.MAP_WHERE, hash);
            viewMap.put(CommonConstants.MAP_NAME, "TDCharges.getAcctList");
        } else if (currField.equalsIgnoreCase("PROD_ID")) {
            viewMap.put(CommonConstants.MAP_NAME, "getDepositProductsReprint");
        }
        new ViewAll(this, viewMap).show();
    }

    public void fillData(Object map) {
        try {
            HashMap hash = (HashMap) map;
            System.out.println("#@@# Hash :" + hash);
            if (viewType != null) {
                if (viewType.equalsIgnoreCase("PROD_ID")) {
                    txtProductID.setText(CommonUtil.convertObjToStr(hash.get("PROD_ID")));
                } else if (viewType.equals("FROM_NO")) {
                    txtFromNo.setText(CommonUtil.convertObjToStr(hash.get("DEPOSIT NUMBER")));
                } else if (viewType.equals("TO_NO")) {
                    txtToNo.setText(CommonUtil.convertObjToStr(hash.get("DEPOSIT NUMBER")));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void reprint() {
        // btnReject.setVisible(true);        
        // TODO add your handling code here:
    }

    private void print(HashMap DepoMap) {
        System.out.println("DepoMap##########" + DepoMap);
        TTIntegration ttIntgration = null;
        HashMap paramMap = new HashMap();
        boolean flag = false;
        ClientUtil.execute("updateCertificatePrintCount", DepoMap);
        if(chkViewPrint.isSelected() == true){
            flag = true;    
        }
        paramMap.put("BranchId", ProxyParameters.BRANCH_ID);
        paramMap.put("Act_Num", DepoMap.get("DEPOSIT_NO"));
        paramMap.put("DepNo", DepoMap.get("DEPOSIT_NO"));
        paramMap.put("TransDt", this.currDt);
        ttIntgration.setParam(paramMap);
        if (CommonUtil.convertObjToStr(DepoMap.get("BEHAVES_LIKE")).equals("CUMMULATIVE")) {
            ttIntgration.integrationForPrint("CummulativeDepositCertificate",flag);
        } else if (CommonUtil.convertObjToStr(DepoMap.get("BEHAVES_LIKE")).equals("FIXED")) {
            ttIntgration.integrationForPrint("DepositReceipt", flag);
        } else if (CommonUtil.convertObjToStr(DepoMap.get("BEHAVES_LIKE")).equals("RECURRING")) {
            ttIntgration.integrationForPrint("DepositReceipt", flag);
        }
        HashMap updateMap = new HashMap();
        updateMap.put("DEPOSIT_NO", DepoMap.get("DEPOSIT_NO"));//Account No
        ClientUtil.execute("updateDepositReprint", updateMap);
    }

    private void clearTable() {
        //observable.resetForm();
        if (tblReprint.getRowCount() > 0) {
            EnhancedTableModel tableModel = (EnhancedTableModel) tblReprint.getModel();
            tableModel.setRowCount(0);
        }
    }

    private Date setProperDtFormat(Date dt) {
        Date tempDt = (Date) currDt.clone();
        if (dt != null) {
            tempDt.setDate(dt.getDate());
            tempDt.setMonth(dt.getMonth());
            tempDt.setYear(dt.getYear());
            return tempDt;
        }
        return null;
    }

    private void initComponentData() {
        //chkWideSize.setEnabled(false);
    }

    public void update(Observable observed, Object arg) {
        // removeRadioButtons();
    }

    private void setFieldNames() {
        //panOutwardRegister.setName("panOutwardRegister");
    }

    private void internationalize() {
        //lblProdType.setText(resourceBundle.getString("lblProdType"));
    }

    private void displayAlert(String message) {
        CMandatoryDialog cmd = new CMandatoryDialog();
        cmd.setMessage(message);
        cmd.show();
    }
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private com.see.truetransact.uicomponent.CButton btnFromNo1;
    private com.see.truetransact.uicomponent.CButton btnPrint;
    private com.see.truetransact.uicomponent.CButton btnProdId;
    private com.see.truetransact.uicomponent.CButton btnSearch;
    private com.see.truetransact.uicomponent.CButton btnToNo;
    private com.see.truetransact.uicomponent.CCheckBox chkSelectAll;
    private com.see.truetransact.uicomponent.CCheckBox chkViewPrint;
    private com.see.truetransact.uicomponent.CLabel lblCDate;
    private com.see.truetransact.uicomponent.CLabel lblCDate1;
    private com.see.truetransact.uicomponent.CLabel lblCDate2;
    private com.see.truetransact.uicomponent.CLabel lblMsg;
    private com.see.truetransact.uicomponent.CMenuBar mbrCustomer;
    private javax.swing.JMenuItem mitCancel;
    private javax.swing.JMenuItem mitClose;
    private javax.swing.JMenuItem mitDelete;
    private javax.swing.JMenuItem mitEdit;
    private javax.swing.JMenuItem mitNew;
    private javax.swing.JMenuItem mitPrint;
    private javax.swing.JMenuItem mitSave;
    private javax.swing.JMenu mnuProcess;
    private com.see.truetransact.uicomponent.CPanel panParameters;
    private com.see.truetransact.uicomponent.CPanel panPrint;
    private com.see.truetransact.uicomponent.CPanel panStatus;
    private com.see.truetransact.uicomponent.CPanel panTable;
    private javax.swing.JSeparator sptNew;
    private javax.swing.JSeparator sptSave;
    private com.see.truetransact.uicomponent.CScrollPane srpPrintPan;
    private com.see.truetransact.uicomponent.CTable tblReprint;
    private com.see.truetransact.uicomponent.CTextField txtFromNo;
    private com.see.truetransact.uicomponent.CTextField txtProductID;
    private com.see.truetransact.uicomponent.CTextField txtToNo;
    // End of variables declaration//GEN-END:variables

    public static void main(String[] args) {
        DepositReprintUI Outward = new DepositReprintUI();
        javax.swing.JFrame j = new javax.swing.JFrame();
        j.getContentPane().add(Outward);
        j.show();
        Outward.show();
    }
}
