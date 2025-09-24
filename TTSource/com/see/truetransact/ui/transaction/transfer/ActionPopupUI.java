/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition.
 * 
 *
 * ActionPopupUI.java
 *
 * Created on August 7, 2003, 1:55 PM
 */
package com.see.truetransact.ui.transaction.transfer;

import com.see.truetransact.clientutil.ClientConstants;
import com.see.truetransact.clientutil.TableModel;
import com.see.truetransact.clientutil.TableSorter;
import com.see.truetransact.clientutil.ClientUtil;
import com.see.truetransact.uicomponent.CDialog;
import com.see.truetransact.uicomponent.CInternalFrame;
import java.awt.Color;
import java.util.Observable;
import java.util.Observer;
import javax.swing.border.TitledBorder;
import javax.swing.ListSelectionModel;
import javax.swing.event.ListSelectionListener;
import javax.swing.event.ListSelectionEvent;
import java.util.Date;
import java.util.HashMap;

/**
 *
 * @author Pranav
 * @modified Pinky
 *
 * @modified Bala
 */
public class ActionPopupUI extends CDialog implements Observer {

    private ActionPopupRB resourceBundle = new ActionPopupRB();
    private ActionPopupOB actionPopupOB;
    private int operation;
    private Object baseUI;

    /*
     * this constructor will be called if the operation is DELETE, EXCEPTIONS
     * or AUTHORIZED
     * baseUI is the object which has called this popup
     */
    public ActionPopupUI(CInternalFrame parent, boolean modal, int operation) {
        super(parent, modal);

        this.operation = operation;

        // first generate the controls
        initComponents();
        initStartup();
        update(actionPopupOB, null);

        actionPopupOB.setModule(parent.getScreen());
        actionPopupOB.setScreen(parent.getModule());

        chkDetailSelect.setVisible(false);
        chkMasterSelect.setVisible(false);
    }

    /*
     * this constructor will be called if the operation is EDIT
     * baseUI is the object which has called this popup
     */
    public ActionPopupUI(CInternalFrame parent, boolean modal, int operation, Object baseUI) {
        super(parent, modal);

        this.baseUI = baseUI;
        this.operation = operation;

        // first generate the controls
        initComponents();
        initStartup();
        update(actionPopupOB, null);

        actionPopupOB.setModule(parent.getScreen());
        actionPopupOB.setScreen(parent.getModule());
    }

    private void initStartup() {
        // then set the names for the controls using setName()
        setFieldNames();

        /* call the intenationalize() method to load the RB values
         * and initialize the Observable for this class
         */
        internationalize();
        setObservable();

        // set up the starting components
        initComponentData();
    }

    private void setObservable() {
        try {
            actionPopupOB = new ActionPopupOB(operation);
            actionPopupOB.addObserver(this);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void initComponentData() {
        btnOK.setEnabled(false);
        this.setTitle(ClientConstants.ACTION_STATUS[operation] + " Transfer");

        TableSorter tableSorter = new TableSorter();
        tableSorter.addMouseListenerToHeaderInTable(tblMaster);
        tableSorter.setModel(actionPopupOB.getMasterModel());
        tableSorter.fireTableDataChanged();

        tblMaster.setModel(tableSorter);
        tblMaster.revalidate();
    }

    public void show() {
        System.out.println("operation  " + operation);

        if (operation == 10) {
            // if(paramMap.get().toString().equals(CommonConstants.STATUS_AUTHORIZED))
            //this.setForeground(Color.red);

            panMaster.setForeground(Color.red);
            sptView.setForeground(Color.red);
            panDetail.setForeground(Color.red);
            cPanel1.setForeground(Color.red);
            panAction.setForeground(Color.red);
            // tblData.setForeground(Color.red);
//            srcTable.setForeground(Color.red);
//             panSearchCondition.setForeground(Color.red);
//            panMultiSearch.setForeground(Color.red);
//            panTable.setForeground(Color.red);
            panMaster.setBackground(Color.red);
            sptView.setBackground(Color.red);
            panDetail.setBackground(Color.red);
            panAction.setBackground(Color.red);
            cPanel1.setBackground(Color.red);
            this.setTitle("Reject");
        } else if (operation == 8) {
            int red = 98;
            int green = 167;
            int blue = 107;
            Color myBlue = new Color(98, 167, 107);
            panMaster.setForeground(myBlue);
            sptView.setForeground(myBlue);
            panDetail.setForeground(myBlue);
            cPanel1.setForeground(myBlue);
            panAction.setForeground(myBlue);
//             panSearchCondition.setForeground(myBlue);
//            panMultiSearch.setForeground(myBlue);
//            panTable.setForeground(myBlue);
            panMaster.setBackground(myBlue);
            sptView.setBackground(myBlue);
            panDetail.setBackground(myBlue);
            panAction.setBackground(myBlue);
            cPanel1.setBackground(myBlue);
            this.setTitle("Authorize");
        }






        if (actionPopupOB.isAvailable()) {
            super.show();
        } else {
            ClientUtil.noDataAlert();
        }
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {
        java.awt.GridBagConstraints gridBagConstraints;

        cPanel1 = new com.see.truetransact.uicomponent.CPanel();
        panAction = new com.see.truetransact.uicomponent.CPanel();
        btnOK = new com.see.truetransact.uicomponent.CButton();
        btnClose = new com.see.truetransact.uicomponent.CButton();
        panMaster = new com.see.truetransact.uicomponent.CPanel();
        srpMaster = new com.see.truetransact.uicomponent.CScrollPane();
        tblMaster = new com.see.truetransact.uicomponent.CTable();
        chkMasterSelect = new com.see.truetransact.uicomponent.CCheckBox();
        sptView = new com.see.truetransact.uicomponent.CSeparator();
        panDetail = new com.see.truetransact.uicomponent.CPanel();
        srpDetail = new com.see.truetransact.uicomponent.CScrollPane();
        tblDetail = new com.see.truetransact.uicomponent.CTable();
        chkDetailSelect = new com.see.truetransact.uicomponent.CCheckBox();

        setDefaultCloseOperation(javax.swing.WindowConstants.DO_NOTHING_ON_CLOSE);
        setModal(true);
        setName("dlgActionPopup"); // NOI18N
        addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowClosing(java.awt.event.WindowEvent evt) {
                closeDialog(evt);
            }
        });
        getContentPane().setLayout(new java.awt.GridBagLayout());

        cPanel1.setMinimumSize(new java.awt.Dimension(654, 559));
        cPanel1.setPreferredSize(new java.awt.Dimension(654, 559));
        cPanel1.setLayout(new java.awt.GridBagLayout());

        panAction.setLayout(new java.awt.GridBagLayout());

        btnOK.setText("OK");
        btnOK.setMaximumSize(new java.awt.Dimension(67, 25));
        btnOK.setMinimumSize(new java.awt.Dimension(67, 25));
        btnOK.setName("btnOK"); // NOI18N
        btnOK.setPreferredSize(new java.awt.Dimension(67, 25));
        btnOK.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnOKActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panAction.add(btnOK, gridBagConstraints);

        btnClose.setText("Close");
        btnClose.setName("btnClose"); // NOI18N
        btnClose.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCloseActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panAction.add(btnClose, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
        cPanel1.add(panAction, gridBagConstraints);

        panMaster.setBorder(javax.swing.BorderFactory.createTitledBorder("Master View"));
        panMaster.setName("panMaster"); // NOI18N
        panMaster.setPreferredSize(new java.awt.Dimension(650, 350));
        panMaster.setLayout(new java.awt.GridBagLayout());

        srpMaster.setPreferredSize(new java.awt.Dimension(600, 100));

        tblMaster.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {

            }
        ));
        tblMaster.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        ListSelectionModel rowSM = tblMaster.getSelectionModel();
        rowSM.addListSelectionListener(new ListSelectionListener() {
            public void valueChanged(ListSelectionEvent e) {
                //Ignore extra messages.
                if (e.getValueIsAdjusting()) return;

                ListSelectionModel lsm = (ListSelectionModel)e.getSource();
                if (lsm.isSelectionEmpty()) {
                    System.out.println("No rows are selected.");
                } else {
                    int selectedRow = lsm.getMinSelectionIndex();
                    System.out.println("Row " + selectedRow
                        + " is now selected.");
                    /*if (operation == ClientConstants.ACTIONTYPE_AUTHORIZE) {
                        String crVal = ((com.see.truetransact.clientutil.TableModel)tblMaster.getModel()).getValueAt(selectedRow, 2).toString();
                        String drVal = ((com.see.truetransact.clientutil.TableModel)tblMaster.getModel()).getValueAt(selectedRow, 4).toString();

                        if (!crVal.equals(drVal)) {

                            JOptionPane.showMessageDialog(null, "This batch is not tallied. Can not proceed with Authorization.");
                            return;
                        }
                    }*/
                    btnOK.setEnabled(true);
                    actionPopupOB.setSelectAllDetails(true);
                    actionPopupOB.setSelectedBatch((String)(((com.see.truetransact.clientutil.TableModel)tblMaster.getModel()).getValueAt(selectedRow, 0)));
                    actionPopupOB.setTransaction_Dt((Date)(((com.see.truetransact.clientutil.TableModel)tblMaster.getModel()).getValueAt(selectedRow, 6)));
                    actionPopupOB.setInitiatedBranch((String)(((com.see.truetransact.clientutil.TableModel)tblMaster.getModel()).getValueAt(selectedRow, 7)));
                    actionPopupOB.getDetailData();
                }
            }
        });
        tblMaster.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tblMasterMouseClicked(evt);
            }
        });
        srpMaster.setViewportView(tblMaster);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        panMaster.add(srpMaster, gridBagConstraints);

        chkMasterSelect.setText("Select All");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        panMaster.add(chkMasterSelect, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
        cPanel1.add(panMaster, gridBagConstraints);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
        cPanel1.add(sptView, gridBagConstraints);

        panDetail.setBorder(javax.swing.BorderFactory.createTitledBorder("Detailed View"));
        panDetail.setName("panDetail"); // NOI18N
        panDetail.setLayout(new java.awt.GridBagLayout());

        srpDetail.setPreferredSize(new java.awt.Dimension(600, 100));

        tblDetail.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {

            }
        ));
        tblDetail.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        srpDetail.setViewportView(tblDetail);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.weightx = 0.1;
        panDetail.add(srpDetail, gridBagConstraints);

        chkDetailSelect.setText("Select All");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        panDetail.add(chkDetailSelect, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
        cPanel1.add(panDetail, gridBagConstraints);

        getContentPane().add(cPanel1, new java.awt.GridBagConstraints());

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void tblMasterMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tblMasterMouseClicked
        // Add your handling code here:
        int mouseClickCount = evt.getClickCount();
        if (mouseClickCount == 2) {
            ((TransferUI) baseUI).setBatchIdForEdit(actionPopupOB.getSelectedBatch());
            ((TransferUI) baseUI).setTransactionDateForEdit(actionPopupOB.getTransaction_Dt());
            ((TransferUI) baseUI).setTransactionInitBranForEdit(actionPopupOB.getInitiatedBranch());
            ((TransferUI) baseUI).setTransactionIdForEdit(null);
            closeActionPopupUI();
        }
    }//GEN-LAST:event_tblMasterMouseClicked

    private void btnCloseActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCloseActionPerformed
        // Add your handling code here:
        // baseUI will be null for "Delete"
        if (baseUI != null) {
            ((TransferUI) baseUI).setBatchIdForEdit(null);
        }
        closeActionPopupUI();
    }//GEN-LAST:event_btnCloseActionPerformed

    private void btnOKActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnOKActionPerformed
        // Add your handling code here:
        if (operation == ClientConstants.ACTIONTYPE_EDIT
                || operation == ClientConstants.ACTIONTYPE_DELETE
                || operation == ClientConstants.ACTIONTYPE_AUTHORIZE
                || operation == ClientConstants.ACTIONTYPE_REJECT
                || operation == ClientConstants.ACTIONTYPE_EXCEPTION) {
            /* get the selected row and get the actual selected transaction
             * id for that record, set this tarnsaction id on the baseUI, that
             * is the calling window and close this popup
             */
            int selectedRow = tblDetail.getSelectedRow();
            if (selectedRow != -1) {
                ((TransferUI) baseUI).setTransactionIdForEdit((String) ((TableModel) tblDetail.getModel()).getValueAt(selectedRow, 0));

                ((TransferUI) baseUI).setBatchIdForEdit(actionPopupOB.getSelectedBatch());
                ((TransferUI) baseUI).setTransactionDateForEdit(actionPopupOB.getTransaction_Dt());
                ((TransferUI) baseUI).setTransactionInitBranForEdit(actionPopupOB.getInitiatedBranch());
                closeActionPopupUI();
            }
        }
    }//GEN-LAST:event_btnOKActionPerformed

    /**
     * Closes the dialog
     */
    private void closeDialog(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_closeDialog
        closeActionPopupUI();
    }//GEN-LAST:event_closeDialog

    private void closeActionPopupUI() {
        setVisible(false);
        dispose();
    }

    private void setFieldNames() {
        panMaster.setName("panMaster");
        chkMasterSelect.setName("chkMasterSelect");
        panDetail.setName("panDetail");
        chkDetailSelect.setName("chkDetailSelect");
        btnOK.setName("btnOK");
        btnClose.setName("btnClose");
    }

    private void internationalize() {
        panMaster.setBorder(
                new TitledBorder(resourceBundle.getString("panMaster")));
        chkMasterSelect.setText(resourceBundle.getString("chkMasterSelect"));

        panDetail.setBorder(
                new TitledBorder(resourceBundle.getString("panDetail")));
        chkDetailSelect.setText(resourceBundle.getString("chkDetailSelect"));

        btnOK.setText(resourceBundle.getString("btnOK"));
        btnClose.setText(resourceBundle.getString("btnClose"));
    }

    public void update(Observable o, Object arg) {
        try {
            chkMasterSelect.setEnabled(false);
            if (operation == ClientConstants.ACTIONTYPE_EDIT
                    || operation == ClientConstants.ACTIONTYPE_DELETE
                    || operation == ClientConstants.ACTIONTYPE_AUTHORIZE
                    || operation == ClientConstants.ACTIONTYPE_EXCEPTION
                    || operation == ClientConstants.ACTIONTYPE_REJECT) {
                chkDetailSelect.setEnabled(false);
            }
            chkDetailSelect.setSelected(actionPopupOB.getSelectAllDetails());

            TableSorter tableSorter = new TableSorter();
            tableSorter.addMouseListenerToHeaderInTable(tblDetail);
            tableSorter.setModel(actionPopupOB.getDetailModel());
            tableSorter.fireTableDataChanged();

            tblDetail.setModel(tableSorter);
            tblDetail.revalidate();
        } catch (Exception e) {
            System.out.println("ActionPopupUI: " + e);
        }
    }
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private com.see.truetransact.uicomponent.CButton btnClose;
    private com.see.truetransact.uicomponent.CButton btnOK;
    private com.see.truetransact.uicomponent.CPanel cPanel1;
    private com.see.truetransact.uicomponent.CCheckBox chkDetailSelect;
    private com.see.truetransact.uicomponent.CCheckBox chkMasterSelect;
    private com.see.truetransact.uicomponent.CPanel panAction;
    private com.see.truetransact.uicomponent.CPanel panDetail;
    private com.see.truetransact.uicomponent.CPanel panMaster;
    private com.see.truetransact.uicomponent.CSeparator sptView;
    private com.see.truetransact.uicomponent.CScrollPane srpDetail;
    private com.see.truetransact.uicomponent.CScrollPane srpMaster;
    private com.see.truetransact.uicomponent.CTable tblDetail;
    private com.see.truetransact.uicomponent.CTable tblMaster;
    // End of variables declaration//GEN-END:variables
}
