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

package com.see.truetransact.ui.courtexpensesetting;


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
import java.util.Date;
import com.see.truetransact.uimandatory.UIMandatoryField;
import com.see.truetransact.uimandatory.MandatoryCheck;
import com.see.truetransact.uimandatory.MandatoryDBCheck;
import com.see.truetransact.uicomponent.CTextField;
import com.see.truetransact.uivalidation.*;
import com.see.truetransact.uicomponent.CButtonGroup;





/** This form is used to manipulate CustomerIdChangeUI related functionality
 * @author swaroop
 */
public class CourtExpenseSettingUI extends com.see.truetransact.uicomponent.CInternalFrame implements java.util.Observer, UIMandatoryField{

         private String viewType = new String();
         private HashMap mandatoryMap;
         private CourtExpenseSettingOB observable;
   
         final String AUTHORIZE="Authorize";
         private final static Logger log = Logger.getLogger(CourtExpenseSettingUI.class);
         CourtExpenseSettingOB courtexpenseRB = new CourtExpenseSettingOB();
         java.util.ResourceBundle resourceBundle, objMandatoryRB;
         CourtExpenseSettingMRB objMandatoryMRB=new CourtExpenseSettingMRB();
         String txtId=null;
        Object   columnNames[]={"SELECT","PROD ID","PROD DESC"};
         List bufferList;
         ArrayList newList= new ArrayList();
         private boolean selectMode= false;
         private String sts="";
         List editList=new ArrayList();
         private Date currDt = null;
         
    /** Creates new form CustomerIdChangeUI */
    public CourtExpenseSettingUI() {
        currDt = ClientUtil.getCurrentDate();
        initComponents();
        initStartUp();
    }
    
    private void initStartUp(){
        setMandatoryHashMap();
        setFieldNames();
        observable = new CourtExpenseSettingOB();
        observable.addObserver(this);
        enableDisable(false);
        setButtonEnableDisable();
        setMaxLength();
        objMandatoryRB = java.util.ResourceBundle.getBundle("com.see.truetransact.ui.courtexpensesetting.CourtExpenseSettingMRB", ProxyParameters.LANGUAGE);
        new MandatoryCheck().putMandatoryMarks(getClass().getName(), panInterestGroup, getMandatoryHashMap());
    }
       
    private void initTableData() {
    
    }
    
    private void setMaxLength() {
     
        txtFromAmt.setValidation(new NumericValidation());
        txtFromAmt.setAllowAll(true);
        txtToAmt.setValidation(new NumericValidation());
        txtToAmt.setAllowAll(true);
        txtPercentage.setValidation(new NumericValidation());
        txtPercentage.setAllowAll(true);
    }
    
    public void setMandatoryHashMap() {
        mandatoryMap = new HashMap();
        mandatoryMap.put("txtFromAmt", new Boolean(true));
        mandatoryMap.put("txtToAmt", new Boolean(true));
        mandatoryMap.put("txtPercentage", new Boolean(true));
    }
    
    public HashMap getMandatoryHashMap() {
        return mandatoryMap;
    }
    
    
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {
        java.awt.GridBagConstraints gridBagConstraints;

        rdoApplType = new com.see.truetransact.uicomponent.CButtonGroup();
        rdoApplType1 = new com.see.truetransact.uicomponent.CButtonGroup();
        panEmpTransfer = new com.see.truetransact.uicomponent.CPanel();
        tabInterestMaintenance = new com.see.truetransact.uicomponent.CTabbedPane();
        panInterestGroup = new com.see.truetransact.uicomponent.CPanel();
        panGroupnfo = new com.see.truetransact.uicomponent.CPanel();
        lblFromAmt = new com.see.truetransact.uicomponent.CLabel();
        lblGroupIdDesc = new com.see.truetransact.uicomponent.CLabel();
        lblToAmt = new com.see.truetransact.uicomponent.CLabel();
        txtToAmt = new com.see.truetransact.uicomponent.CTextField();
        lblDate = new com.see.truetransact.uicomponent.CLabel();
        lblPercentage = new com.see.truetransact.uicomponent.CLabel();
        txtFromAmt = new com.see.truetransact.uicomponent.CTextField();
        txtPercentage = new com.see.truetransact.uicomponent.CTextField();
        tdtDate = new com.see.truetransact.uicomponent.CDateField();
        panGroupData = new com.see.truetransact.uicomponent.CPanel();
        srpProduct = new com.see.truetransact.uicomponent.CScrollPane();
        tblProduct = new com.see.truetransact.uicomponent.CTable();
        tbrOperativeAcctProduct = new javax.swing.JToolBar();
        btnView = new com.see.truetransact.uicomponent.CButton();
        lblSpace5 = new com.see.truetransact.uicomponent.CLabel();
        btnNew = new com.see.truetransact.uicomponent.CButton();
        lblSpace62 = new com.see.truetransact.uicomponent.CLabel();
        btnEdit = new com.see.truetransact.uicomponent.CButton();
        lblSpace2 = new com.see.truetransact.uicomponent.CLabel();
        btnSave = new com.see.truetransact.uicomponent.CButton();
        lblSpace63 = new com.see.truetransact.uicomponent.CLabel();
        btnCancel = new com.see.truetransact.uicomponent.CButton();
        lblSpace3 = new com.see.truetransact.uicomponent.CLabel();
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
        setMaximumSize(new java.awt.Dimension(800, 625));
        setMinimumSize(new java.awt.Dimension(800, 625));
        setPreferredSize(new java.awt.Dimension(800, 625));

        panEmpTransfer.setMaximumSize(new java.awt.Dimension(650, 520));
        panEmpTransfer.setMinimumSize(new java.awt.Dimension(650, 520));
        panEmpTransfer.setPreferredSize(new java.awt.Dimension(650, 520));
        panEmpTransfer.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                panEmpTransferFocusLost(evt);
            }
        });
        panEmpTransfer.setLayout(new java.awt.GridBagLayout());

        tabInterestMaintenance.setMinimumSize(new java.awt.Dimension(860, 534));
        tabInterestMaintenance.setPreferredSize(new java.awt.Dimension(860, 534));

        panInterestGroup.setLayout(new java.awt.GridBagLayout());

        panGroupnfo.setLayout(null);

        lblFromAmt.setText("From Amount");
        lblFromAmt.setMaximumSize(new java.awt.Dimension(100, 16));
        lblFromAmt.setMinimumSize(new java.awt.Dimension(100, 16));
        lblFromAmt.setPreferredSize(new java.awt.Dimension(100, 16));
        panGroupnfo.add(lblFromAmt);
        lblFromAmt.setBounds(30, 40, 100, 16);
        panGroupnfo.add(lblGroupIdDesc);
        lblGroupIdDesc.setBounds(0, 0, 0, 0);

        lblToAmt.setText("To Amount");
        lblToAmt.setMaximumSize(new java.awt.Dimension(100, 16));
        lblToAmt.setMinimumSize(new java.awt.Dimension(100, 16));
        lblToAmt.setPreferredSize(new java.awt.Dimension(100, 16));
        panGroupnfo.add(lblToAmt);
        lblToAmt.setBounds(40, 70, 100, 16);

        txtToAmt.setMinimumSize(new java.awt.Dimension(100, 21));
        panGroupnfo.add(txtToAmt);
        txtToAmt.setBounds(170, 70, 100, 21);

        lblDate.setText("Effect From");
        panGroupnfo.add(lblDate);
        lblDate.setBounds(30, 10, 67, 18);

        lblPercentage.setText("Percentage");
        panGroupnfo.add(lblPercentage);
        lblPercentage.setBounds(40, 100, 90, 18);
        panGroupnfo.add(txtFromAmt);
        txtFromAmt.setBounds(170, 40, 100, 21);
        panGroupnfo.add(txtPercentage);
        txtPercentage.setBounds(170, 100, 100, 21);
        panGroupnfo.add(tdtDate);
        tdtDate.setBounds(170, 10, 101, 21);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.ipadx = 299;
        gridBagConstraints.ipady = 129;
        gridBagConstraints.insets = new java.awt.Insets(3, 3, 3, 3);
        panInterestGroup.add(panGroupnfo, gridBagConstraints);

        panGroupData.setBorder(javax.swing.BorderFactory.createTitledBorder("Group Data"));
        panGroupData.setLayout(new java.awt.GridBagLayout());

        srpProduct.setMinimumSize(new java.awt.Dimension(300, 150));
        srpProduct.setPreferredSize(new java.awt.Dimension(300, 150));

        tblProduct.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Select", "Product ID", "Product Desc"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.Boolean.class, java.lang.Object.class, java.lang.Object.class
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }
        });
        tblProduct.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tblProductMouseClicked(evt);
            }
        });
        srpProduct.setViewportView(tblProduct);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panGroupData.add(srpProduct, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.gridwidth = 3;
        gridBagConstraints.ipadx = 112;
        gridBagConstraints.insets = new java.awt.Insets(3, 3, 3, 3);
        panInterestGroup.add(panGroupData, gridBagConstraints);

        tabInterestMaintenance.addTab("Court Expense Setting", panInterestGroup);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panEmpTransfer.add(tabInterestMaintenance, gridBagConstraints);

        getContentPane().add(panEmpTransfer, java.awt.BorderLayout.CENTER);

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

        lblSpace62.setForeground(new java.awt.Color(240, 240, 240));
        lblSpace62.setText("     ");
        lblSpace62.setMaximumSize(new java.awt.Dimension(3, 18));
        lblSpace62.setMinimumSize(new java.awt.Dimension(3, 18));
        lblSpace62.setPreferredSize(new java.awt.Dimension(3, 18));
        tbrOperativeAcctProduct.add(lblSpace62);

        btnEdit.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_EDIT.gif"))); // NOI18N
        btnEdit.setToolTipText("Edit");
        btnEdit.setEnabled(false);
        btnEdit.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnEditActionPerformed(evt);
            }
        });
        tbrOperativeAcctProduct.add(btnEdit);

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

        lblSpace63.setForeground(new java.awt.Color(240, 240, 240));
        lblSpace63.setText("     ");
        lblSpace63.setMaximumSize(new java.awt.Dimension(3, 18));
        lblSpace63.setMinimumSize(new java.awt.Dimension(3, 18));
        lblSpace63.setPreferredSize(new java.awt.Dimension(3, 18));
        tbrOperativeAcctProduct.add(lblSpace63);

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

    private void tblProductMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tblProductMouseClicked
         if(selectMode == true){
            String st=CommonUtil.convertObjToStr(tblProduct.getValueAt(tblProduct.getSelectedRow(),0));
            if(st.equals("true")){
                tblProduct.setValueAt(new Boolean(false),tblProduct.getSelectedRow(),0);
            }else{
                tblProduct.setValueAt(new Boolean(true),tblProduct.getSelectedRow(),0);
            }
}
    }//GEN-LAST:event_tblProductMouseClicked

    private void panEmpTransferFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_panEmpTransferFocusLost
        // TODO add your handling code here:
    }//GEN-LAST:event_panEmpTransferFocusLost

    private void btnViewActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnViewActionPerformed
        // TODO add your handling code here:
        btnView.setEnabled(false);
        observable.setActionType(ClientConstants.ACTIONTYPE_VIEW);
 
    }//GEN-LAST:event_btnViewActionPerformed
                            
    private void btnCloseActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCloseActionPerformed
        mitCloseActionPerformed(evt);
    }//GEN-LAST:event_btnCloseActionPerformed
    
    private void mitCloseActionPerformed(java.awt.event.ActionEvent evt) {
        cifClosingAlert();
    }
        
    private void btnEditActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnEditActionPerformed
        sts="Edit";
        observable.setActionType(ClientConstants.ACTIONTYPE_EDIT);
        observable.setStatus();
        System.out.println("in edit btn");
        popUp("Edit");
        lblStatus.setText(observable.getLblStatus());
    }//GEN-LAST:event_btnEditActionPerformed
    
    private void mitCancelActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mitCancelActionPerformed
        btnCancelActionPerformed(evt);
    }//GEN-LAST:event_mitCancelActionPerformed
    
    private void btnCancelActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCancelActionPerformed
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
//         System.out.println("in save");
//         StringBuffer str  =  new StringBuffer();
//        String mandatoryMessage = new MandatoryCheck().checkMandatory(getClass().getName(), panInterestGroup);
//        System.out.println("in mandatoryMessage>>>"+mandatoryMessage);
//        if( observable.getActionType() != ClientConstants.ACTIONTYPE_DELETE && mandatoryMessage.length() > 0 ){
//           displayAlert(mandatoryMessage);
//            System.out.println("in save1");
//         }
                      String mandatoryMessage ="";
        StringBuffer message = new StringBuffer(mandatoryMessage);
       
       
        resourceBundle = new CourtExpenseSettingRB();
        //final String shareAcctMandatoryMessage = new MandatoryCheck().checkMandatory(getClass().getName(), panDrfTransDetails);
       
        StringBuffer strBMandatory = new StringBuffer();
//        if(cboBoardMember.getSelectedIndex()==0){
//            message.append(objMandatoryMRB.getString("cboBoardMember"));
//        }
//        if(tdtMeetingDate.getDateValue()==null || tdtMeetingDate.getDateValue().equals("") )
//        {
//            message.append(objMandatoryMRB.getString("tdtMeetingDate"));
//        }
//        if(txtSittingFeeAmount.getText().equals("")) {
//            message.append(objMandatoryMRB.getString("txtSittingFeeAmount"));
//        }
//          if(tdtPaidDate.getDateValue()==null || tdtPaidDate.getDateValue().equals("") )
//        {
//            message.append(objMandatoryMRB.getString("tdtPaidDate"));
//        }

        if(message.length() > 0 ) {
            displayAlert(message.toString());
            return;
        } 
         
        else{
            System.out.println("in save2");
            savePerformed();
            }
    }//GEN-LAST:event_btnSaveActionPerformed
    
    
    private void btnNewActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnNewActionPerformed
        enableDisable(true);
        //selectMode=true;
        setButtonEnableDisable();
        observable.setActionType(ClientConstants.ACTIONTYPE_NEW);
        observable.resetForm();
        observable.setStatus();
        lblStatus.setText(observable.getLblStatus());
        tableData();
        
        
    }//GEN-LAST:event_btnNewActionPerformed
    
    private void mitDeleteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mitDeleteActionPerformed

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

    
    /** To display a popUp window for viewing existing data */
    private void popUp(String currAction){
       viewType = currAction;
        HashMap viewMap = new HashMap();
        if (currAction.equalsIgnoreCase("Edit")){
            System.out.println("in edit popup");
            HashMap map = new HashMap();
            map.put("BRANCH_CODE",TrueTransactMain.BRANCH_ID);
            viewMap.put(CommonConstants.MAP_WHERE, map);
            viewMap.put(CommonConstants.MAP_NAME, "getcourtexpenseEdit"); 
        }
        
        else if(currAction.equalsIgnoreCase("Delete")){
            HashMap map = new HashMap();
            map.put("BRANCH_CODE",TrueTransactMain.BRANCH_ID);
            viewMap.put(CommonConstants.MAP_WHERE, map);
            viewMap.put(CommonConstants.MAP_NAME, "getcourtexpenseEdit");
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
               hash.put(CommonConstants.MAP_WHERE, hash.get("CE_ID"));
                hash.put(CommonConstants.BRANCH_ID, TrueTransactMain.BRANCH_ID);
                observable.getData(hash);
          if (viewType.equals(ClientConstants.ACTION_STATUS[3]) || viewType.equals(AUTHORIZE) ||
               viewType.equals(ClientConstants.ACTION_STATUS[17])) {
                    ClientUtil.enableDisable(panInterestGroup, false);
                 
                } else {
                   ClientUtil.enableDisable(panInterestGroup, true);
                 
               }
              setButtonEnableDisable();
               
           }
       }
       }catch(Exception e){
          log.error(e);
        }
       
   }
  private void tableData() {
        
        HashMap me=new HashMap();
        bufferList=(List)ClientUtil.executeQuery("getLoanproduct", me);
        Object rowData[][] = new Object[+bufferList.size()][5];
        System.out.println("Load BufferelIst>>>"+bufferList.size());
        boolean b=false;
        if(!bufferList.isEmpty())
        {
            for(int i=0;i<bufferList.size();i++)
         {
            
             HashMap m1=new HashMap();
             m1=(HashMap)bufferList.get(i);
             rowData[i][0]=b;
             rowData[i][1]=m1.get("PROD_ID");
             rowData[i][2]=m1.get("PROD_DESC");
              System.out.println("iii" +i+"  prod id firstttt.."+m1.get("PROD_ID"));
         }
          if(sts.equals("Edit"))
          {  boolean t=true;
             System.out.println("Ediitttttttteaaaaaain tbl..."+sts);
             editList=observable.getSelectedList();
               System.out.println("Ediitttttttteaaaaaa..."+editList.size());
             for(int k=0;k<editList.size();k++)
             {
                 HashMap m1=new HashMap();
                m1=(HashMap)editList.get(k);
                for(int i=0;i<bufferList.size();i++)
                {
                    HashMap m2=new HashMap();
                m2=(HashMap)bufferList.get(i);
                
             System.out.println("iii" +i+"prod id sect.."+m1.get("PROD_ID")+"   "+m2.get("PROD_ID"));
                    if(m1.get("PROD_ID").toString().equals(m2.get("PROD_ID").toString()))
                    {
                         rowData[i][0]=t;
                        
                         System.out.println("rowData[i][0]=="+rowData[i][0].toString()+" rowData[i][0]"+rowData[i][1].toString());
                          break;
                    }
                }
             }
               sts="";
          }
          
        
        
        //+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
        
        
        tblProduct.setModel(new javax.swing.table.DefaultTableModel(rowData,columnNames){
            
         
      // @Override
    public Class getColumnClass(int column) {
        if(column==0)
        {
           return Boolean.class;
        }
        else
        {
             return String.class;
        }
    }
            
            
         public boolean isCellEditable(int row, int column) {
       //Only the third column
             if(column==0)
             {
                 return true;
             }
             else
             {
       return false;
             }
   }
        
        }) ;
        tblProduct.setVisible(true);
      
    }
        
      }
    
   
  
      private void enableDisable(boolean yesno){
      ClientUtil.enableDisable(this, yesno);
  }
    
   private void setButtonEnableDisable() {
        btnNew.setEnabled(!btnNew.isEnabled());
        btnEdit.setEnabled(!btnEdit.isEnabled());
        mitNew.setEnabled(btnNew.isEnabled());
        mitEdit.setEnabled(btnEdit.isEnabled());
        btnSave.setEnabled(!btnNew.isEnabled());
        btnCancel.setEnabled(!btnNew.isEnabled());
        mitSave.setEnabled(btnSave.isEnabled());
        mitCancel.setEnabled(btnCancel.isEnabled());
        btnView.setEnabled(!btnView.isEnabled());
   }
    
   public void update(Observable observed, Object arg) {
            txtId=(observable.getTxtId());
            tdtDate.setDateValue(observable.getTdtDate());
            txtFromAmt.setText(observable.getTxtFromAmt());
            txtToAmt.setText(observable.getTxtToAmt());
            txtPercentage.setText(observable.getTxtPercentage());
            editList=observable.getTblList();
            tableData();
            lblStatus.setText(observable.getLblStatus());
    }
    
    public void updateOBFields() {
             
            observable.setTdtDate(tdtDate.getDateValue());
            observable.setTxtFromAmt(txtFromAmt.getText());
            observable.setTxtToAmt(txtToAmt.getText());
            observable.setTxtPercentage(txtPercentage.getText());
            observable.setTblList(getSelectedData());
            observable.setTxtId(txtId);
             
    }
    
    public  List getSelectedData()
   {
       List select=new ArrayList();
       for(int i=0;i<tblProduct.getRowCount();i++){
        
           if(tblProduct.getValueAt(i,0).toString().equals("true"))
           {
               HashMap m=new HashMap();
               m.put("PROD_ID",tblProduct.getValueAt(i,1).toString());
               select.add(m);
           }
       }
       return select;
   }

    private void savePerformed(){
        updateOBFields();
        observable.doAction();
        if(observable.getResult() != ClientConstants.ACTIONTYPE_FAILED){
            HashMap lockMap = new HashMap();
            ArrayList lst = new ArrayList();
            
            if(observable.getActionType()==ClientConstants.ACTIONTYPE_NEW) {
               
                if (observable.getProxyReturnMap()!=null) {
                    if (observable.getProxyReturnMap().containsKey("CE_ID")) {
                      
                    }
                    
                }
            }
            
                  
            if(observable.getResult()==ClientConstants.ACTIONTYPE_EDIT) {
              
            }
    
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
        txtFromAmt.setName("txtFromAmt");
        txtToAmt.setName("txtToAmt");
        txtPercentage.setName("txtPercentage");
        lblFromAmt.setName("lblFromAmt");
        lblToAmt.setName("lblToAmt");
        lblPercentage.setName("lblPercentage");
        srpProduct.setName("srpProduct");
        tblProduct.setName("tblProduct");
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
    private com.see.truetransact.uicomponent.CButton btnCancel;
    private com.see.truetransact.uicomponent.CButton btnClose;
    private com.see.truetransact.uicomponent.CButton btnEdit;
    private com.see.truetransact.uicomponent.CButton btnNew;
    private com.see.truetransact.uicomponent.CButton btnSave;
    private com.see.truetransact.uicomponent.CButton btnView;
    private com.see.truetransact.uicomponent.CLabel lblDate;
    private com.see.truetransact.uicomponent.CLabel lblFromAmt;
    private com.see.truetransact.uicomponent.CLabel lblGroupIdDesc;
    private com.see.truetransact.uicomponent.CLabel lblMsg;
    private com.see.truetransact.uicomponent.CLabel lblPercentage;
    private com.see.truetransact.uicomponent.CLabel lblSpace1;
    private com.see.truetransact.uicomponent.CLabel lblSpace2;
    private com.see.truetransact.uicomponent.CLabel lblSpace3;
    private com.see.truetransact.uicomponent.CLabel lblSpace5;
    private com.see.truetransact.uicomponent.CLabel lblSpace62;
    private com.see.truetransact.uicomponent.CLabel lblSpace63;
    private com.see.truetransact.uicomponent.CLabel lblStatus;
    private com.see.truetransact.uicomponent.CLabel lblToAmt;
    private com.see.truetransact.uicomponent.CMenuBar mbrCustomer;
    private javax.swing.JMenuItem mitCancel;
    private javax.swing.JMenuItem mitClose;
    private javax.swing.JMenuItem mitDelete;
    private javax.swing.JMenuItem mitEdit;
    private javax.swing.JMenuItem mitNew;
    private javax.swing.JMenuItem mitPrint;
    private javax.swing.JMenuItem mitSave;
    private javax.swing.JMenu mnuProcess;
    private com.see.truetransact.uicomponent.CPanel panEmpTransfer;
    private com.see.truetransact.uicomponent.CPanel panGroupData;
    private com.see.truetransact.uicomponent.CPanel panGroupnfo;
    private com.see.truetransact.uicomponent.CPanel panInterestGroup;
    private com.see.truetransact.uicomponent.CPanel panStatus;
    private com.see.truetransact.uicomponent.CButtonGroup rdoApplType;
    private com.see.truetransact.uicomponent.CButtonGroup rdoApplType1;
    private javax.swing.JSeparator sptNew;
    private javax.swing.JSeparator sptSave;
    private com.see.truetransact.uicomponent.CScrollPane srpProduct;
    private com.see.truetransact.uicomponent.CTabbedPane tabInterestMaintenance;
    private com.see.truetransact.uicomponent.CTable tblProduct;
    private javax.swing.JToolBar tbrOperativeAcctProduct;
    private com.see.truetransact.uicomponent.CDateField tdtDate;
    private com.see.truetransact.uicomponent.CTextField txtFromAmt;
    private com.see.truetransact.uicomponent.CTextField txtPercentage;
    private com.see.truetransact.uicomponent.CTextField txtToAmt;
    // End of variables declaration//GEN-END:variables
    
    public static void main(String[] args) {
        CourtExpenseSettingUI dirBrd = new CourtExpenseSettingUI();
        javax.swing.JFrame j = new javax.swing.JFrame();
        j.getContentPane().add(dirBrd);
        j.show();
        dirBrd.show();
}
}