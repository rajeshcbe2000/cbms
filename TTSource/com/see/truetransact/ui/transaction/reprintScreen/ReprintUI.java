/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition.  
 * 
 * ReprintUI.java
 */
package com.see.truetransact.ui.transaction.reprintScreen;

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

public class ReprintUI extends com.see.truetransact.uicomponent.CInternalFrame implements java.util.Observer {
//final int CASH = 0, TRANS = 1;
//int viewType = -1;

    boolean flag = false;
    Date currDt = null;
    private String viewType = "";
   private ReprintOB observable;
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
    public ReprintUI() {
        initComponents();
        setObservable();
        initStartUp();
        currDt = ClientUtil.getCurrentDate();
        System.out.println("currDt###"+currDt);
        tdtTransDt.setDateValue(DateUtil.getStringDate(currDt));
        tblReprint.getModel().addTableModelListener(tableModelListener);
    }

    private void setObservable() {
        try {
            observable = ReprintOB.getInstance();
            observable.addObserver(this);
        } catch (Exception e) {
            System.out.println("Error in setObservable():" + e);
        }
    }

    private void initStartUp() {
        tdtTransDt.setEnabled(true);
        // Date currDt = null;
        setFieldNames();
        internationalize();
        initComponentData();
        fillCombo();
        
    }
    
    public void fillCombo(){
        key = new ArrayList();
        value = new ArrayList();
        key.add("");
        key.add("CREDIT");
        key.add("DEBIT");
        value.add("");
        value.add("Receipts");
        value.add("Payments");
        cbmTransType = new ComboBoxModel(key,value);
        cboTransType.setModel(getCbmTransType());
        
    }

    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {
        java.awt.GridBagConstraints gridBagConstraints;

        panOutwardRegister = new com.see.truetransact.uicomponent.CPanel();
        cPanel1 = new com.see.truetransact.uicomponent.CPanel();
        lblCDate = new com.see.truetransact.uicomponent.CLabel();
        rdoCash = new com.see.truetransact.uicomponent.CRadioButton();
        rdoTransfer = new com.see.truetransact.uicomponent.CRadioButton();
        tdtTransDt = new com.see.truetransact.uicomponent.CDateField();
        cProcess = new com.see.truetransact.uicomponent.CButton();
        chkWideSize = new com.see.truetransact.uicomponent.CCheckBox();
        btnReprint = new com.see.truetransact.uicomponent.CButton();
        txtAcNo = new com.see.truetransact.uicomponent.CTextField();
        lblAcNo = new com.see.truetransact.uicomponent.CLabel();
        chkUnAuth = new com.see.truetransact.uicomponent.CCheckBox();
        lblTransType = new com.see.truetransact.uicomponent.CLabel();
        cboTransType = new com.see.truetransact.uicomponent.CComboBox();
        chkViewPrint = new com.see.truetransact.uicomponent.CCheckBox();
        chkAuth = new com.see.truetransact.uicomponent.CCheckBox();
        txtAcName = new com.see.truetransact.uicomponent.CTextField();
        lblAcName = new com.see.truetransact.uicomponent.CLabel();
        lblTransId = new com.see.truetransact.uicomponent.CLabel();
        txtTransID = new com.see.truetransact.uicomponent.CTextField();
        chkBonusPrint = new com.see.truetransact.uicomponent.CCheckBox();
        cPanel2 = new com.see.truetransact.uicomponent.CPanel();
        srpPrintPan = new com.see.truetransact.uicomponent.CScrollPane();
        tblReprint = new com.see.truetransact.uicomponent.CTable();
        panStatus = new com.see.truetransact.uicomponent.CPanel();
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
        setMaximumSize(new java.awt.Dimension(920, 665));
        setMinimumSize(new java.awt.Dimension(920, 665));
        setPreferredSize(new java.awt.Dimension(920, 665));

        panOutwardRegister.setMaximumSize(new java.awt.Dimension(350, 350));
        panOutwardRegister.setMinimumSize(new java.awt.Dimension(350, 350));
        panOutwardRegister.setPreferredSize(new java.awt.Dimension(350, 350));
        panOutwardRegister.setLayout(new java.awt.GridBagLayout());

        cPanel1.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        cPanel1.setMinimumSize(new java.awt.Dimension(450, 150));
        cPanel1.setPreferredSize(new java.awt.Dimension(450, 180));
        cPanel1.setLayout(new java.awt.GridBagLayout());

        lblCDate.setText(" Date");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        cPanel1.add(lblCDate, gridBagConstraints);

        rdoCash.setText("Cash");
        rdoCash.setMinimumSize(new java.awt.Dimension(85, 27));
        rdoCash.setPreferredSize(new java.awt.Dimension(85, 27));
        rdoCash.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                rdoCashActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.ipady = -2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        cPanel1.add(rdoCash, gridBagConstraints);

        rdoTransfer.setText("Transfer");
        rdoTransfer.setMargin(new java.awt.Insets(2, 5, 2, 2));
        rdoTransfer.setMaximumSize(new java.awt.Dimension(69, 27));
        rdoTransfer.setMinimumSize(new java.awt.Dimension(85, 27));
        rdoTransfer.setPreferredSize(new java.awt.Dimension(85, 27));
        rdoTransfer.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                rdoTransferActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.ipadx = 21;
        gridBagConstraints.ipady = -2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        cPanel1.add(rdoTransfer, gridBagConstraints);

        tdtTransDt.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tdtTransDtMouseClicked(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                tdtTransDtMouseExited(evt);
            }
            public void mouseReleased(java.awt.event.MouseEvent evt) {
                tdtTransDtMouseReleased(evt);
            }
        });
        tdtTransDt.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                tdtTransDtFocusLost(evt);
            }
        });
        tdtTransDt.addInputMethodListener(new java.awt.event.InputMethodListener() {
            public void caretPositionChanged(java.awt.event.InputMethodEvent evt) {
            }
            public void inputMethodTextChanged(java.awt.event.InputMethodEvent evt) {
                tdtTransDtInputMethodTextChanged(evt);
            }
        });
        tdtTransDt.addPropertyChangeListener(new java.beans.PropertyChangeListener() {
            public void propertyChange(java.beans.PropertyChangeEvent evt) {
                tdtTransDtPropertyChange(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.ipady = 9;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        cPanel1.add(tdtTransDt, gridBagConstraints);

        cProcess.setForeground(new java.awt.Color(0, 0, 153));
        cProcess.setText("Search");
        cProcess.setFont(new java.awt.Font("MS Sans Serif", 1, 13)); // NOI18N
        cProcess.setMaximumSize(new java.awt.Dimension(83, 27));
        cProcess.setMinimumSize(new java.awt.Dimension(83, 27));
        cProcess.setPreferredSize(new java.awt.Dimension(83, 27));
        cProcess.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cProcessActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 5;
        cPanel1.add(cProcess, gridBagConstraints);

        chkWideSize.setText("Wide Size");
        chkWideSize.setMaximumSize(new java.awt.Dimension(100, 21));
        chkWideSize.setMinimumSize(new java.awt.Dimension(100, 21));
        chkWideSize.setPreferredSize(new java.awt.Dimension(100, 21));
        chkWideSize.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                chkWideSizeActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        cPanel1.add(chkWideSize, gridBagConstraints);

        btnReprint.setForeground(new java.awt.Color(0, 0, 153));
        btnReprint.setText("Print");
        btnReprint.setFont(new java.awt.Font("MS Sans Serif", 1, 13)); // NOI18N
        btnReprint.setMaximumSize(new java.awt.Dimension(83, 27));
        btnReprint.setMinimumSize(new java.awt.Dimension(83, 27));
        btnReprint.setPreferredSize(new java.awt.Dimension(83, 27));
        btnReprint.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnReprintActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 5;
        cPanel1.add(btnReprint, gridBagConstraints);

        txtAcNo.setAllowAll(true);
        txtAcNo.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                txtAcNoFocusLost(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        cPanel1.add(txtAcNo, gridBagConstraints);

        lblAcNo.setText("Account No.");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 0, 8);
        cPanel1.add(lblAcNo, gridBagConstraints);

        chkUnAuth.setText("Unauthorized");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        cPanel1.add(chkUnAuth, gridBagConstraints);

        lblTransType.setText("Trans Type");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        cPanel1.add(lblTransType, gridBagConstraints);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.insets = new java.awt.Insets(0, 6, 0, 0);
        cPanel1.add(cboTransType, gridBagConstraints);

        chkViewPrint.setText("View Print");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        cPanel1.add(chkViewPrint, gridBagConstraints);

        chkAuth.setText("Authorized");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        cPanel1.add(chkAuth, gridBagConstraints);

        txtAcName.setAllowAll(true);
        txtAcName.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                txtAcNameFocusLost(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        cPanel1.add(txtAcName, gridBagConstraints);

        lblAcName.setText("Account Name");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 0, 7);
        cPanel1.add(lblAcName, gridBagConstraints);

        lblTransId.setText("Trans Id");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        cPanel1.add(lblTransId, gridBagConstraints);

        txtTransID.setAllowAll(true);
        txtTransID.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                txtTransIDFocusLost(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(2, 0, 3, 0);
        cPanel1.add(txtTransID, gridBagConstraints);

        chkBonusPrint.setText("Bonus Print");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 5;
        cPanel1.add(chkBonusPrint, gridBagConstraints);

        panOutwardRegister.add(cPanel1, new java.awt.GridBagConstraints());

        cPanel2.setLayout(new java.awt.GridBagLayout());

        srpPrintPan.setMinimumSize(new java.awt.Dimension(800, 350));
        srpPrintPan.setPreferredSize(new java.awt.Dimension(800, 350));

        tblReprint.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Select", "TransId", "Product", "Name", "Amount", "Account No", "Trans Type", "Module", "Single TransID"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false, false, false, false, false
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
        tblReprint.getColumnModel().getColumn(8).setResizable(false);

        cPanel2.add(srpPrintPan, new java.awt.GridBagConstraints());

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        panOutwardRegister.add(cPanel2, gridBagConstraints);

        getContentPane().add(panOutwardRegister, java.awt.BorderLayout.CENTER);

        panStatus.setLayout(new java.awt.GridBagLayout());
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

private void rdoCashActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_rdoCashActionPerformed
    if (rdoCash.isSelected() == true) {
        //resetFields();
        chkWideSize.setEnabled(false);
        flag = true;
        rdoCash.setSelected(true);
        rdoTransfer.setSelected(false);
        observable.setRdoTransfer(false);
        observable.setTableTitle();
        tblReprint.setModel(observable.getTblReciept());
    }


}//GEN-LAST:event_rdoCashActionPerformed

private void rdoTransferActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_rdoTransferActionPerformed
    if (rdoTransfer.isSelected() == true) {
        //resetFields();
        flag = false;
        observable.setRdoTransfer(true);
        rdoCash.setSelected(false);
        rdoTransfer.setSelected(true);
        chkWideSize.setEnabled(true);
        observable.setTableTitle();
        tblReprint.setModel(observable.getTblReciept());
    }

}//GEN-LAST:event_rdoTransferActionPerformed
    private void resetFields() {
        tdtTransDt.setDateValue("");


    }
private void tdtTransDtFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_tdtTransDtFocusLost
}//GEN-LAST:event_tdtTransDtFocusLost

private void tdtTransDtPropertyChange(java.beans.PropertyChangeEvent evt) {//GEN-FIRST:event_tdtTransDtPropertyChange
}//GEN-LAST:event_tdtTransDtPropertyChange

private void tdtTransDtMouseReleased(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tdtTransDtMouseReleased
}//GEN-LAST:event_tdtTransDtMouseReleased

private void tdtTransDtMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tdtTransDtMouseExited
// TODO add your handling code here:
    System.out.println("hgjgdfuyrdys6");
}//GEN-LAST:event_tdtTransDtMouseExited

private void tdtTransDtInputMethodTextChanged(java.awt.event.InputMethodEvent evt) {//GEN-FIRST:event_tdtTransDtInputMethodTextChanged
// TODO add your handling code here:
    System.out.println("HAiiiiiiiiiiiiiiiiiii");
}//GEN-LAST:event_tdtTransDtInputMethodTextChanged

private void tdtTransDtMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tdtTransDtMouseClicked
// TODO add your handling code here:
    tdtTransDt.setDateValue("");
}//GEN-LAST:event_tdtTransDtMouseClicked

private void cProcessActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cProcessActionPerformed
    if(tdtTransDt.getDateValue().equals("") || tdtTransDt.getDateValue().isEmpty()){
            ClientUtil.displayAlert("Date Should not be null!!! "); 
            return;
    }    
    if ((rdoCash.isSelected() == true)) {             
            reprint("Cash");        
    } else if ((rdoTransfer.isSelected() == true)){        
        reprint("transfer");
    } else{
        ClientUtil.showMessageWindow(" Please select Cash or Transfer !!! ");
    }
}//GEN-LAST:event_cProcessActionPerformed

    private void reprint(String type) {
        // btnReject.setVisible(true);
        HashMap shareTypeMap = new HashMap();
        HashMap whereMap = new HashMap();
        whereMap.put("From_Date", setProperDtFormat(DateUtil.getDateMMDDYYYY(tdtTransDt.getDateValue())));
        whereMap.put(CommonConstants.BRANCH_ID, ProxyParameters.BRANCH_ID);
        if(txtTransID.getText() != null && txtTransID.getText().length()>0){
            whereMap.put("TRANS_ID","%" +txtTransID.getText()+ "%");            
        }
        if(txtAcNo.getText() != null && txtAcNo.getText().length()>0){
            whereMap.put("ACT_NUM","%" +txtAcNo.getText());            
        }
        if(txtAcName.getText() != null && txtAcName.getText().length()>0){
            whereMap.put("ACT_NAME","%" +txtAcName.getText()+ "%");            
        }
        if(chkUnAuth.isSelected() && !chkAuth.isSelected()){
            whereMap.put("UNAUTHORIZED","UNAUTHORIZED");            
        }else if(!chkUnAuth.isSelected() && chkAuth.isSelected()){
            whereMap.put("AUTHORIZED","AUTHORIZED");            
        }else if(chkAuth.isSelected() && chkUnAuth.isSelected()){
            whereMap.put("BOTH","BOTH");
        }else {
            whereMap.put("BOTH","BOTH");
        }
        if (type.equals("Cash")) {
            if (!cboTransType.getSelectedItem().equals("") && cboTransType.getSelectedItem() != null) {
                whereMap.put("TRANS_TYPE",CommonUtil.convertObjToStr(getCbmTransType().getKeyForSelected()));          
            }
            shareTypeMap.put(CommonConstants.MAP_NAME, "getReprintData");
        } else if (type.equals("transfer")) {
            shareTypeMap.put(CommonConstants.MAP_NAME, "getReprintTransData");
        }
        shareTypeMap.put(CommonConstants.MAP_WHERE, whereMap);
        try {
            // log.info("populateData...");
            System.out.println("shareTypeMap=======" + shareTypeMap);
           observable.populateData(shareTypeMap, tblReprint);
            tblReprint.setModel(observable.getTblReciept());
            selectMode = true;
        } catch (Exception e) {
            System.err.println("Exception " + e.toString() + "Caught");
            e.printStackTrace();
        }
        tblReprint.setEditingColumn(0);
//        javax.swing.table.TableColumn col = tblReprint.getColumn(tblReprint.getColumnName(0));
//        col.setMaxWidth(50);
//        col.setPreferredWidth(50);
//        col = tblReprint.getColumn(tblReprint.getColumnName(1));
//        col.setMaxWidth(70);
//        col.setPreferredWidth(70);
//        col = tblReprint.getColumn(tblReprint.getColumnName(2));
//        col.setMaxWidth(300);
//        col.setPreferredWidth(300);
//        System.out.println(" shareTypeMap=======" + shareTypeMap);
//        System.out.println("contrrrrrrrrrrrrrrrrr" + tblReprint.getRowCount());
//        System.out.println("contrrrrrrrrrrrrrrrrr" + tblReprint.getRowCount());
        //lblTotReceipts.setText(CommonUtil.convertObjToStr(data.size()));
        //lblTotPayments.setText(CommonUtil.convertObjToStr(tblPayment.getRowCount()));
        //  CheckBoxNodeRenderer render = new CheckBoxNodeRenderer();
        //        tblReciept.setCe(render);


        // TODO add your handling code here:
    }

private void chkWideSizeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_chkWideSizeActionPerformed
    boolean flag;
    if (chkWideSize.isSelected() == true) {
        flag = true;
    } else {
        flag = false;
    }

}//GEN-LAST:event_chkWideSizeActionPerformed

private void btnReprintActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnReprintActionPerformed
    ArrayList branAryLst = new ArrayList();
    HashMap newBranMap;
    boolean count = false;
    int printCount = 0;
    int dataCount = 0;
    if (tblReprint.getRowCount() > 0) {
        for (int i = 0; i < tblReprint.getRowCount(); i++) {
            if ((Boolean) tblReprint.getValueAt(i, 0)) {
                count = true;
                printCount++;
            }
        }
        if (!count) {
            ClientUtil.showMessageWindow(" NO Rows Selected !!! ");
            count = false;
            return;
        } else {
            System.out.println("#$#$$ printCount : " + printCount);
            int yesNo = 0;
            String[] options = {"Yes", "No"};
            yesNo = COptionPane.showOptionDialog(null, "Do you want to print "+printCount+" Items ?", CommonConstants.WARNINGTITLE,
                    COptionPane.OK_OPTION, COptionPane.WARNING_MESSAGE,
                    null, options, options[0]);
            System.out.println("#$#$$ yesNo : " + yesNo);
            dataCount = tblReprint.getRowCount();   
            String singleTransId = "";
            if (yesNo == 0) {
                for (int i = 0; i < dataCount; i++) {
                    if ((Boolean) tblReprint.getValueAt(i, 0)) {  
                        System.out.println("singleTransId%%%%%%%%%%"+singleTransId);
                        System.out.println("CommonUtil.convertObjToStr(tblReprint.getValueAt(i, 8))%%%%%%%%%%"+CommonUtil.convertObjToStr(tblReprint.getValueAt(i, 8)));
                        if(!singleTransId.equals(CommonUtil.convertObjToStr(tblReprint.getValueAt(i, 8)))){
                            singleTransId = CommonUtil.convertObjToStr(tblReprint.getValueAt(i, 8));
                            newBranMap = new HashMap();
                            newBranMap.put("MODULE", CommonUtil.convertObjToStr(tblReprint.getValueAt(i, 7)));
                            newBranMap.put("SINGLE_TRANS_ID", CommonUtil.convertObjToStr(tblReprint.getValueAt(i, 8)));
                            newBranMap.put("TRANS_TYPE", CommonUtil.convertObjToStr(tblReprint.getValueAt(i, 6)));                            
                            print(newBranMap);  
                        }
                    }
                }
            }
        }
    } else {
        ClientUtil.showMessageWindow("Transaction Empty!!!");
        return;
    }
}//GEN-LAST:event_btnReprintActionPerformed

    private void print(HashMap printMap) {
        System.out.println("printMap##########" + printMap);
        boolean flag = false;
        if(chkViewPrint.isSelected() == true){
            flag = true;    
        }        
        TTIntegration ttIntgration = null;
        HashMap paramMap = new HashMap();
        paramMap.put("BranchId", ProxyParameters.BRANCH_ID);
        paramMap.put("TransId", printMap.get("SINGLE_TRANS_ID"));
        paramMap.put("TransDt", setProperDtFormat(DateUtil.getDateMMDDYYYY(tdtTransDt.getDateValue())));
        ttIntgration.setParam(paramMap);
        if (rdoCash.isSelected() == true) {
            if (printMap.get("TRANS_TYPE").equals("CREDIT")) {
                if (printMap.get("MODULE").equals("OTHERS")) {
                    System.out.println("credit-------OTHERS");
                    ttIntgration.integrationForPrint("CashReceipt", flag);
                } else if (printMap.get("MODULE").equals("MDS")) {
                    System.out.println("credit-------MDS");
                    ttIntgration.integrationForPrint("MDSReceipts", flag);    
                    if (chkBonusPrint.isSelected()) { // Added by nithya on 13-11-2019 for KD 427 - 0020127: veethapalisa not shown in transfer slip (reprint screen)
                        ttIntgration.integrationForPrint("MDSReceiptsTransfer", flag);
                    }
                }
            } else {
                ttIntgration.integrationForPrint("CashPayment", flag);
            }
        }
        if (rdoTransfer.isSelected() == true) {
            if (printMap.get("MODULE").equals("OTHERS")) {
                if(chkWideSize.isSelected() == false){
                    ttIntgration.integrationForPrint("ReceiptPayment", flag);    
                }else if(chkWideSize.isSelected() == true){
                    ttIntgration.integrationForPrint("ReceiptPaymentWide", flag);    
                }                
            } else if (printMap.get("MODULE").equals("MDS")) {
                ttIntgration.integrationForPrint("MDSReceiptsTransfer", flag);
            }
        }
    }

private void tblReprintMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tblReprintMouseClicked
// TODO add your handling code here:
    System.out.println("reached check action performed");
    boolean chk = ((Boolean) tblReprint.getValueAt(tblReprint.getSelectedRow(), 0)).booleanValue();
    String strNodeUnselec = "";
    System.out.println("checkBoxNode.selected : " + chk);
    if (!chk) {
        String singleTranId = CommonUtil.convertObjToStr(tblReprint.getValueAt(tblReprint.getSelectedRow(), 8));
        String module = CommonUtil.convertObjToStr(tblReprint.getValueAt(tblReprint.getSelectedRow(), 8));
        System.out.println("nodeVal selected : " + module);
        if (partsC.contains(singleTranId)) {
            partsC.remove(singleTranId);
        }
        if (partsC.contains(module)) {
            partsC.remove(module);
        }
        partsC.add(singleTranId);
        partsC.add(module);
        tblReprint.setValueAt(true, tblReprint.getSelectedRow(), 0);
        if (rdoCash.isSelected()) { // Added by nithya on 13-11-2019 for KD 427 - 0020127: veethapalisa not shown in transfer slip (reprint screen)
            if (CommonUtil.convertObjToStr(tblReprint.getValueAt(tblReprint.getSelectedRow(), 7)).equalsIgnoreCase("MDS")) {
                chkBonusPrint.setVisible(true);
            } else {
                chkBonusPrint.setSelected(false);
                chkBonusPrint.setVisible(false);
            }
        } else {
            chkBonusPrint.setSelected(false);
            chkBonusPrint.setVisible(false);
        }
    } else {
        String singleTranId = CommonUtil.convertObjToStr(tblReprint.getValueAt(tblReprint.getSelectedRow(), 8));
        String module = CommonUtil.convertObjToStr(tblReprint.getValueAt(tblReprint.getSelectedRow(), 8));
        System.out.println("nodeVal=====" + singleTranId);
        strNodeUnselec = singleTranId;
        System.out.println("nodeVal unselec : " + singleTranId);
        partsC.remove(singleTranId);
        partsC.remove(module);
        tblReprint.setValueAt(false, tblReprint.getSelectedRow(), 0);
    }
}//GEN-LAST:event_tblReprintMouseClicked

private void txtAcNoFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtAcNoFocusLost
// TODO add your handling code here:
    txtAcNo.setText(txtAcNo.getText().toUpperCase());
}//GEN-LAST:event_txtAcNoFocusLost

private void txtAcNameFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtAcNameFocusLost
// TODO add your handling code here:
}//GEN-LAST:event_txtAcNameFocusLost

private void txtTransIDFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtTransIDFocusLost
// TODO add your handling code here:
}//GEN-LAST:event_txtTransIDFocusLost

private void clearTable() {
        //observable.resetForm();
    if(tblReprint.getRowCount()>0){
        EnhancedTableModel tableModel = (EnhancedTableModel) tblReprint.getModel();
        tableModel.setRowCount(0);
    }
}

    private void callView(String currField) {
        HashMap viewMap = new HashMap();
        HashMap transMap = new HashMap();
        if (currField.equals("Cash")) {
            HashMap whereMap = new HashMap();
            whereMap.put("From_Date", setProperDtFormat(DateUtil.getDateMMDDYYYY(tdtTransDt.getDateValue())));
            whereMap.put(CommonConstants.BRANCH_ID, ProxyParameters.BRANCH_ID);
            System.out.println("whereMapgjhydj" + whereMap);
            viewMap.put(CommonConstants.MAP_NAME, "getReprintData");
            viewMap.put(CommonConstants.MAP_WHERE, whereMap);
            transMap = ClientUtil.executeTableQuery(viewMap);
            System.out.println("viewMap-=-=-=" + transMap);

        } else if (currField.equals("transfer")) {
            HashMap whereMap = new HashMap();
            whereMap.put("From_Date", setProperDtFormat(DateUtil.getDateMMDDYYYY(tdtTransDt.getDateValue())));
            whereMap.put(CommonConstants.BRANCH_ID, ProxyParameters.BRANCH_ID);
            viewMap.put(CommonConstants.MAP_NAME, "getReprintTransData");
            viewMap.put(CommonConstants.MAP_WHERE, whereMap);
            transMap = ClientUtil.executeTableQuery(viewMap);
            System.out.println("viewMap-=-=-=" + transMap);
        }

        new ViewAll(this, viewMap).show();
    }

    public void fillData(Object map) {
        HashMap hash = (HashMap) map;

        System.out.println("hash=====" + hash);
        if (viewType.equals("Cash")) {
            String cash = CommonUtil.convertObjToStr(hash.get("SINGLE_TRANS_ID"));
            System.out.println("cash 5y6ttr5uhy" + cash);
            hash.put("TRANS_ID", hash.get("TRANS_ID"));
            if (rdoCash.isSelected() == true) {
                int yesNo = 0;
                String[] options = {"Yes", "No"};
                yesNo = COptionPane.showOptionDialog(null, "Do you want to print?", CommonConstants.WARNINGTITLE,
                        COptionPane.OK_OPTION, COptionPane.WARNING_MESSAGE,
                        null, options, options[0]);
                System.out.println("#$#$$ yesNo : " + yesNo);
                if (yesNo == 0) {
                    TTIntegration ttIntgration = null;
                    HashMap paramMap = new HashMap();
                    paramMap.put("BranchId", ProxyParameters.BRANCH_ID);
                    paramMap.put("TransId", cash);
                    paramMap.put("TransDt", setProperDtFormat(DateUtil.getDateMMDDYYYY(tdtTransDt.getDateValue())));
                    System.out.println("DateUtil.getDateMMDDYYYY(From_Date.getDateValue())" + DateUtil.getDateMMDDYYYY(tdtTransDt.getDateValue()));
                    ttIntgration.setParam(paramMap);
                    if (hash.get("TRANS_TYPE").equals("CREDIT")) {
                        if (hash.get("MODULE").equals("OTHERS")) {
                            System.out.println("credit-------");
                            ttIntgration.integrationForPrint("CashReceipt", true);
                        } else if (hash.get("MODULE").equals("MDS")) {
                            System.out.println("credit-------");
                            ttIntgration.integrationForPrint("MDSReceipts", true);
                        }
                    } else {
                        ttIntgration.integrationForPrint("CashPayment", true);
                    }
                }
            }
        }
        if (viewType.equals("transfer") && (chkWideSize.isSelected() == false)) {
            String transfer = CommonUtil.convertObjToStr(hash.get("SINGLE_TRANS_ID"));
            System.out.println("cash 5y6ttr5uhy" + transfer);
            hash.put("TRANS_ID", hash.get("TRANS_ID"));
            int yesNo = 0;
            String[] options = {"Yes", "No"};
            yesNo = COptionPane.showOptionDialog(null, "Do you want to print?", CommonConstants.WARNINGTITLE,
                    COptionPane.OK_OPTION, COptionPane.WARNING_MESSAGE,
                    null, options, options[0]);
            System.out.println("#$#$$ yesNo : " + yesNo);
            if (yesNo == 0) {
                TTIntegration ttIntgration = null;
                HashMap paramMap = new HashMap();
                paramMap.put("BranchId", ProxyParameters.BRANCH_ID);
                paramMap.put("TransId", transfer);
                paramMap.put("TransDt", setProperDtFormat(DateUtil.getDateMMDDYYYY(tdtTransDt.getDateValue())));
                ttIntgration.setParam(paramMap);
                if (hash.get("MODULE").equals("OTHERS")) {
                    ttIntgration.integrationForPrint("ReceiptPayment", true);
                } else if (hash.get("MODULE").equals("MDS")) {
                    ttIntgration.integrationForPrint("MDSReceiptsTransfer", true);
                }
            }
        }
        if (viewType.equals("transfer") && (chkWideSize.isSelected() == true)) {
            String transfer = CommonUtil.convertObjToStr(hash.get("SINGLE_TRANS_ID"));
            System.out.println("cash 5y6ttr5uhy" + transfer);
            hash.put("TRANS_ID", hash.get("TRANS_ID"));
            int yesNo = 0;
            String[] options = {"Yes", "No"};
            yesNo = COptionPane.showOptionDialog(null, "Do you want to print?", CommonConstants.WARNINGTITLE,
                    COptionPane.OK_OPTION, COptionPane.WARNING_MESSAGE,
                    null, options, options[0]);
            System.out.println("#$#$$ yesNo : " + yesNo);
            if (yesNo == 0) {
                TTIntegration ttIntgration = null;
                HashMap paramMap = new HashMap();
                paramMap.put("BranchId", ProxyParameters.BRANCH_ID);
                paramMap.put("TransId", transfer);
                paramMap.put("TransDt", setProperDtFormat(DateUtil.getDateMMDDYYYY(tdtTransDt.getDateValue())));
                ttIntgration.setParam(paramMap);
                ttIntgration.integrationForPrint("ReceiptPaymentWide", true);
            }
        }
    }

    private void generateReport() {
        int yesNo = 0;
        String[] options = {"Yes", "No"};
        yesNo = COptionPane.showOptionDialog(null, "Do you want to print?", CommonConstants.WARNINGTITLE,
                COptionPane.OK_OPTION, COptionPane.WARNING_MESSAGE,
                null, options, options[0]);
        System.out.println("#$#$$ yesNo : " + yesNo);
        if (yesNo == 0) {
            TTIntegration ttIntgration = null;
            HashMap paramMap = new HashMap();
            paramMap.put("BranchId", ProxyParameters.BRANCH_ID);
            paramMap.put("From_Date", DateUtil.getDateMMDDYYYY(tdtTransDt.getDateValue()));
            ttIntgration.setParam(paramMap);
            ttIntgration.integrationForPrint("MDSAuthorizationLetterForTrans", false);
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
        chkWideSize.setEnabled(false);
        chkBonusPrint.setVisible(false); // Added by nithya on 13-11-2019 for KD 427 - 0020127: veethapalisa not shown in transfer slip (reprint screen)
    }

    public void update(Observable observed, Object arg) {
        // removeRadioButtons();
    }

    private void setFieldNames() {

        panOutwardRegister.setName("panOutwardRegister");
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
    private com.see.truetransact.uicomponent.CButton btnReprint;
    private com.see.truetransact.uicomponent.CPanel cPanel1;
    private com.see.truetransact.uicomponent.CPanel cPanel2;
    private com.see.truetransact.uicomponent.CButton cProcess;
    private com.see.truetransact.uicomponent.CComboBox cboTransType;
    private com.see.truetransact.uicomponent.CCheckBox chkAuth;
    private com.see.truetransact.uicomponent.CCheckBox chkBonusPrint;
    private com.see.truetransact.uicomponent.CCheckBox chkUnAuth;
    private com.see.truetransact.uicomponent.CCheckBox chkViewPrint;
    private com.see.truetransact.uicomponent.CCheckBox chkWideSize;
    private com.see.truetransact.uicomponent.CLabel lblAcName;
    private com.see.truetransact.uicomponent.CLabel lblAcNo;
    private com.see.truetransact.uicomponent.CLabel lblCDate;
    private com.see.truetransact.uicomponent.CLabel lblMsg;
    private com.see.truetransact.uicomponent.CLabel lblTransId;
    private com.see.truetransact.uicomponent.CLabel lblTransType;
    private com.see.truetransact.uicomponent.CMenuBar mbrCustomer;
    private javax.swing.JMenuItem mitCancel;
    private javax.swing.JMenuItem mitClose;
    private javax.swing.JMenuItem mitDelete;
    private javax.swing.JMenuItem mitEdit;
    private javax.swing.JMenuItem mitNew;
    private javax.swing.JMenuItem mitPrint;
    private javax.swing.JMenuItem mitSave;
    private javax.swing.JMenu mnuProcess;
    private com.see.truetransact.uicomponent.CPanel panOutwardRegister;
    private com.see.truetransact.uicomponent.CPanel panStatus;
    private com.see.truetransact.uicomponent.CRadioButton rdoCash;
    private com.see.truetransact.uicomponent.CRadioButton rdoTransfer;
    private javax.swing.JSeparator sptNew;
    private javax.swing.JSeparator sptSave;
    private com.see.truetransact.uicomponent.CScrollPane srpPrintPan;
    private com.see.truetransact.uicomponent.CTable tblReprint;
    private com.see.truetransact.uicomponent.CDateField tdtTransDt;
    private com.see.truetransact.uicomponent.CTextField txtAcName;
    private com.see.truetransact.uicomponent.CTextField txtAcNo;
    private com.see.truetransact.uicomponent.CTextField txtTransID;
    // End of variables declaration//GEN-END:variables

    public static void main(String[] args) {
        ReprintUI Outward = new ReprintUI();
        javax.swing.JFrame j = new javax.swing.JFrame();
        j.getContentPane().add(Outward);
        j.show();
        Outward.show();
    }
}
