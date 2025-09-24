/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition. 
 * 
 * CheckGahanCustomerUI.java
 *
 * Created on April 13, 2011, 10:10 PM
 */
package com.see.truetransact.ui.customer;

import java.util.Date;
import java.awt.Toolkit;
import java.util.HashMap;
import java.awt.Dimension;
import java.util.Observer;
import java.util.ArrayList;
import com.see.truetransact.uicomponent.CTable;
import com.see.truetransact.ui.TrueTransactMain;
import com.see.truetransact.uicomponent.CDialog;
import com.see.truetransact.clientutil.ClientUtil;
import com.see.truetransact.clientutil.EnhancedTableModel;
import com.see.truetransact.commonutil.CommonUtil;
import com.see.truetransact.ui.customer.gahan.GahanCustomerOB;
import com.see.truetransact.uicomponent.CInternalFrame;
import com.see.truetransact.ui.termloan.GoldLoanUI;
import com.see.truetransact.ui.mdsapplication.mdschangeofmember.MDSChangeofMemberUI;
import com.see.truetransact.ui.transaction.common.TransDetailsUI;
import java.util.List;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author Suresh
 */
public class CheckGahanCustomerUI extends CDialog implements Observer {

    final GahanCustomerOB gahanOB = new GahanCustomerOB();
    private List itemsSubmittingLists = new ArrayList();
    private EnhancedTableModel tmbGahanCustDetails;
    GoldLoanUI parentGoldLoanUI = null;
    MDSChangeofMemberUI parentChangeMemberUI = null;
    Date currDt = null;
    public String branchID;
    public HashMap sourceMap;
    CInternalFrame parent = null;
    final int BRANCH = 2;
    int vieType = -1;
    DefaultTableModel tblCustomerLists;

    public CheckGahanCustomerUI(List items) {
        setItemsSubmittingLists(items);
        initForm();
        show();
    }

    public CheckGahanCustomerUI(CInternalFrame parent, HashMap map) {
        if (sourceMap == null) {
            sourceMap = new HashMap();
        }
        sourceMap.putAll(map);
        this.parent = parent;
        initForm();
        show();
    }

    /**
     * Method which is used to initialize the form TokenConfig
     */
    private void initForm() {
        initComponents();
        setMaxLengths();
        setFieldNames();
        internationalize();
        branchID = TrueTransactMain.BRANCH_ID;
        setupScreen();
        currDt = ClientUtil.getCurrentDate();
        tblCustomerLists = (DefaultTableModel) tblCustomerList.getModel();

        for (int j = 0; j < getItemsSubmittingLists().size(); j++) {
            tblCustomerList.getModel().setValueAt(CommonUtil.convertObjToStr(getItemsSubmittingLists().get(j)), j, 0);
        }

    }

    private void setMaxLengths() {
    }

    public EnhancedTableModel getTmbGahanCustDetails() {
        return tmbGahanCustDetails;
    }

    public void setTmbGahanCustDetails(EnhancedTableModel tmbGahanCustDetails) {
        this.tmbGahanCustDetails = tmbGahanCustDetails;
    }

    private void setupScreen() {
        setModal(true);
        setTitle("Check Customer Id " + "[" + branchID + "]");
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        Dimension frameSize = this.getSize();
        if (frameSize.height > screenSize.height) {
            frameSize.height = screenSize.height;
        }
        if (frameSize.width > screenSize.width) {
            frameSize.width = screenSize.width;
        }
        this.setLocation((screenSize.width - frameSize.width) / 2, (screenSize.height - frameSize.height) / 2);

    }

    private void setFieldNames() {

        btnClose.setName("btnClose");

        panCustDetails.setName("panCustDetails");
    }

    private void internationalize() {
    }

    public void update(java.util.Observable o, Object arg) {
    }

    public List getItemsSubmittingLists() {
        return itemsSubmittingLists;
    }

    public void setItemsSubmittingLists(List itemsSubmittingLists) {
        this.itemsSubmittingLists = itemsSubmittingLists;
    }

    public void updateOBFields() {
    }

    public void setMandatoryHashMap() {
    }

    public HashMap getMandatoryHashMap() {
        return null;
    }

    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {
        java.awt.GridBagConstraints gridBagConstraints;

        panCustDetails = new com.see.truetransact.uicomponent.CPanel();
        srpRepaymentCTable = new com.see.truetransact.uicomponent.CScrollPane();
        tblCustomerList = new com.see.truetransact.uicomponent.CTable();
        panSearch = new com.see.truetransact.uicomponent.CPanel();
        btnClose = new com.see.truetransact.uicomponent.CButton();
        btnRemove = new com.see.truetransact.uicomponent.CButton();

        getContentPane().setLayout(new java.awt.GridBagLayout());

        panCustDetails.setMaximumSize(new java.awt.Dimension(830, 300));
        panCustDetails.setMinimumSize(new java.awt.Dimension(830, 300));
        panCustDetails.setPreferredSize(new java.awt.Dimension(830, 300));
        panCustDetails.setLayout(new java.awt.GridBagLayout());

        srpRepaymentCTable.setMaximumSize(new java.awt.Dimension(795, 130));
        srpRepaymentCTable.setMinimumSize(new java.awt.Dimension(795, 130));
        srpRepaymentCTable.setPreferredSize(new java.awt.Dimension(795, 130));

        tblCustomerList.setBorder(javax.swing.BorderFactory.createEmptyBorder(1, 1, 1, 1));
        tblCustomerList.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null},
                {null},
                {null},
                {null},
                {null},
                {null},
                {null},
                {null},
                {null}
            },
            new String [] {
                "Items Added"
            }
        ));
        tblCustomerList.setAutoResizeMode(javax.swing.JTable.AUTO_RESIZE_ALL_COLUMNS);
        tblCustomerList.setDragEnabled(true);
        tblCustomerList.setMaximumSize(new java.awt.Dimension(2147483647, 64));
        tblCustomerList.setMinimumSize(new java.awt.Dimension(750, 0));
        tblCustomerList.setPreferredScrollableViewportSize(new java.awt.Dimension(794, 246));
        tblCustomerList.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                tblCustomerListMousePressed(evt);
            }
        });
        srpRepaymentCTable.setViewportView(tblCustomerList);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.ipadx = -526;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(37, 17, 0, 0);
        panCustDetails.add(srpRepaymentCTable, gridBagConstraints);

        panSearch.setLayout(new java.awt.GridBagLayout());

        btnClose.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_CLOSE.gif"))); // NOI18N
        btnClose.setText("Close");
        btnClose.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCloseActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panSearch.add(btnClose, gridBagConstraints);

        btnRemove.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_CANCEL.gif"))); // NOI18N
        btnRemove.setText("Remove");
        btnRemove.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnRemoveActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panSearch.add(btnRemove, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 17, 41, 52);
        panCustDetails.add(panSearch, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.ipadx = -484;
        gridBagConstraints.ipady = -53;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(11, 5, 11, 10);
        getContentPane().add(panCustDetails, gridBagConstraints);

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btnCloseActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCloseActionPerformed
        // TODO add your handling code here:
        this.dispose();
    }//GEN-LAST:event_btnCloseActionPerformed

    private void btnRemoveActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnRemoveActionPerformed
        int row = tblCustomerList.getSelectedRow();
        setCustomerListTable(row);
        return;

    }//GEN-LAST:event_btnRemoveActionPerformed
    public void setCustomerListTable(int row) {
        tblCustomerLists.removeRow(row);
        getItemsSubmittingLists().clear();
        for (int i = 0; i < tblCustomerList.getRowCount(); i++) {
            if (tblCustomerList.getValueAt(i, 0) != null) {
                getItemsSubmittingLists().add(tblCustomerList.getValueAt(i, 0));
            }
            gahanOB.setItemsSubmittingList(getItemsSubmittingLists());
        }
    }
    private void tblCustomerListMousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tblCustomerListMousePressed
    }//GEN-LAST:event_tblCustomerListMousePressed

                                private void formWindowClosed(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_formWindowClosed
                                                                                                                                                                            }//GEN-LAST:event_formWindowClosed

                                                            private void formWindowClosing(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_formWindowClosing
                                                                                                                                                                                                                                                                                                                                                            }//GEN-LAST:event_formWindowClosing

    private void exitForm(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_exitForm
                                                            }//GEN-LAST:event_exitForm

    public HashMap loanInterestCalculationAsAndWhen(HashMap whereMap) {
        HashMap mapData = new HashMap();
        try {
            List mapDataList = ClientUtil.executeQuery("", whereMap);
            mapData = (HashMap) mapDataList.get(0);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return mapData;
    }

    public static void main(String args[]) {
        //        new CheckCustomerIdUI().show();
    }
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private com.see.truetransact.uicomponent.CButton btnClose;
    private com.see.truetransact.uicomponent.CButton btnRemove;
    private com.see.truetransact.uicomponent.CPanel panCustDetails;
    private com.see.truetransact.uicomponent.CPanel panSearch;
    private com.see.truetransact.uicomponent.CScrollPane srpRepaymentCTable;
    private com.see.truetransact.uicomponent.CTable tblCustomerList;
    // End of variables declaration//GEN-END:variables
}
