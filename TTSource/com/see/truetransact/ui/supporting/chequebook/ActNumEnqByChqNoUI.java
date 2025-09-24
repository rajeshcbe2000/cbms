/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition. 
 * 
 * ActNumEnqByChqNoUI.java
 *
 * Created on September 09, 2009, 4:06 PM
 */

package com.see.truetransact.ui.supporting.chequebook;

/**
 *
 * @author Swaroop.V
 */
import com.see.truetransact.uicomponent.CInternalFrame;
import com.see.truetransact.uimandatory.UIMandatoryField;
import com.see.truetransact.clientexception.ClientParseException;
import com.see.truetransact.clientutil.ClientUtil;
import com.see.truetransact.clientutil.ClientConstants;
import com.see.truetransact.uimandatory.MandatoryCheck;
import com.see.truetransact.clientutil.CMandatoryDialog;
import com.see.truetransact.commonutil.CommonConstants;
import com.see.truetransact.commonutil.CommonUtil;
import com.see.truetransact.ui.TrueTransactMain;
import com.see.truetransact.ui.common.viewall.ViewAll;
import com.see.truetransact.ui.common.authorizestatus.AuthorizeStatusUI;
import com.see.truetransact.uicomponent.COptionPane;
import java.util.ResourceBundle;
import com.see.truetransact.clientproxy.ProxyParameters;
import com.see.truetransact.uivalidation.ToDateValidation;
import com.see.truetransact.commonutil.DateUtil;
import com.see.truetransact.uivalidation.ToDateValidation;
import java.util.Observer;
import java.util.Observable;
import java.util.HashMap;
import java.util.ArrayList;
import java.util.Date;
import com.see.truetransact.uivalidation.NumericValidation;
import java.util.List;
import com.see.truetransact.clientutil.EnhancedTableModel;
import java.util.Observable;

public class ActNumEnqByChqNoUI extends CInternalFrame implements Observer, UIMandatoryField{
  
    private HashMap mandatoryMap;
    private final static ClientParseException parseException = ClientParseException.getInstance();
    private EnhancedTableModel tblCh;
    ArrayList ChqTitle = new ArrayList();
    
    /** Creates new form ActNumEnqByChqNoUI */
    public ActNumEnqByChqNoUI(String selectedBranchId) {
        initForm();
    }
    
    /** Initialise the Form ActNumEnqByChqNoUI **/
    private void initForm(){
        initComponents();
        setFieldNames();
        internationalize();
        setMandatoryHashMap();
        setObservable();
        initComponentData();
        setMaxLenths();
        txtChqNo1.setEnabled(true);
        txtChqNo1.setEnabled(true);
        setChequeDetailsTitle();
        tblCh = new EnhancedTableModel(null,ChqTitle);
        tblChqList.setModel(getTblCh());
    }
    
    /* Auto Generated Method - setFieldNames()
   This method assigns name for all the components.
   Other functions are working based on this name. */
    private void setFieldNames() {
       
        btnCancel.setName("btnCancel");
        btnClose.setName("btnClose");
        btnPrint.setName("btnPrint");
        lblMsg.setName("lblMsg"); 
        lblSpace1.setName("lblSpace1");
        lblSpace3.setName("lblSpace3");
        lblStatus.setName("lblStatus");
        lblChqNo.setName("lblChqNo");
        panStatus.setName("panStatus");
        panCheque.setName("panCheque");
        txtChqNo1.setName("txtChqNo1");
        txtIChqNo2.setName("txtIChqNo2");
        panChqDetails.setName("panChqDetails");
        panChqResults.setName("panChqResults");
        panInstrumentNo.setName("panInstrumentNo");
        tblChqList.setName("tblChqList");
    }
    private void setMaxLenths() {
    txtIChqNo2.setValidation(new NumericValidation(ClientConstants.INSTRUMENT_NO2, 0));
    }
/* Auto Generated Method - internationalize()
   This method used to assign display texts from
   the Resource Bundle File. */
    private void internationalize() {
       
    }
    
    /* Auto Generated Method - setMandatoryHashMap()
     
ADD: implements com.see.truetransact.uimandatory.UIMandatoryField
     
   This method list out all the Input Fields available in the UI.
   It needs a class level HashMap variable mandatoryMap. */
    public void setMandatoryHashMap() {
       
    }
    
/* Auto Generated Method - getMandatoryHashMap()
   Getter method for setMandatoryHashMap().*/
    public HashMap getMandatoryHashMap() {
        return mandatoryMap;
    }
    
/* Auto Generated Method - setHelpMessage()
   This method shows tooltip help for all the input fields
   available in the UI. It needs the Mandatory Resource Bundle
   object. Help display Label name should be lblMsg. */
    public void setHelpMessage() {
    }
    
    /** Sets the maximum possible length to the TextFields in the UI **/
    private void setMaxLengths(){
    }
    
    /** Sets the SelectedItem for cboTokenType as "Metal Type" **/
    
    /*Makes the button Enable or Disable accordingly when usier clicks new,edit or delete buttons */
    private void setButtonEnableDisable() {
        mitCancel.setEnabled(btnCancel.isEnabled());
    }
    /* Auto Generated Method - update()
   This method called by Observable. It updates the UI with
   Observable's data. If needed add/Remove RadioButtons
   method need to be added.*/
    public void update(Observable observed, Object arg) {
    }
    
    
    /** Adds up the Observable to this form */
    private void setObservable() {
        try{
          
        }catch(Exception e){
            parseException.logException(e,true);
        }
    }
    
    /** Auto Generated Method - updateOBFields()
     * This method called by Save option of UI.
     * It updates the OB with UI data.*/
    public void updateOBFields() {
    }
    
    /*Setting model to the combobox cboTokenType  */
    private void initComponentData() {
        try{
//            cboSeriesNo.setModel(observable.getCbmSeriesNo());
        }catch(ClassCastException e){
            parseException.logException(e,true);
        }
    }
    /** Method used to check whether the Mandatory Fields in the Form are Filled or not */
    private String checkMandatory(javax.swing.JComponent component){
        return new MandatoryCheck().checkMandatory(getClass().getName(), component);
    }
    
    /** Method used to Give a Alert when any Mandatory Field is not filled by the user */
    private void displayAlert(String message){
        CMandatoryDialog cmd = new CMandatoryDialog();
        cmd.setMessage(message);
        cmd.setModal(true);
        cmd.show();
    }
    
    /* Calls the execute method of TerminalOB to do insertion or updation or deletion */
    private void saveAction(String status){
        
    }
    
    /* set the screen after the updation,insertion, deletion */
    private void settings(){
        ClientUtil.clearAll(this);
        ClientUtil.enableDisable(panCheque, false);
        setButtonEnableDisable();
    }
    
    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {
        java.awt.GridBagConstraints gridBagConstraints;

        panCheque = new com.see.truetransact.uicomponent.CPanel();
        panChqResults = new com.see.truetransact.uicomponent.CPanel();
        srpChqDetails = new com.see.truetransact.uicomponent.CScrollPane();
        tblChqList = new com.see.truetransact.uicomponent.CTable();
        panChqDetails = new com.see.truetransact.uicomponent.CPanel();
        btnSearch = new com.see.truetransact.uicomponent.CButton();
        lblChqNo = new com.see.truetransact.uicomponent.CLabel();
        panInstrumentNo = new com.see.truetransact.uicomponent.CPanel();
        txtChqNo1 = new com.see.truetransact.uicomponent.CTextField();
        txtIChqNo2 = new com.see.truetransact.uicomponent.CTextField();
        tbrChequeEnq = new com.see.truetransact.uicomponent.CToolBar();
        btnCancel = new com.see.truetransact.uicomponent.CButton();
        lblSpace3 = new com.see.truetransact.uicomponent.CLabel();
        btnPrint = new com.see.truetransact.uicomponent.CButton();
        lblSpace17 = new com.see.truetransact.uicomponent.CLabel();
        btnClose = new com.see.truetransact.uicomponent.CButton();
        panStatus = new com.see.truetransact.uicomponent.CPanel();
        lblSpace1 = new com.see.truetransact.uicomponent.CLabel();
        lblStatus = new com.see.truetransact.uicomponent.CLabel();
        lblMsg = new com.see.truetransact.uicomponent.CLabel();
        mbrTokenLoss = new com.see.truetransact.uicomponent.CMenuBar();
        mnuProcess = new javax.swing.JMenu();
        mitNew = new javax.swing.JMenuItem();
        mitEdit = new javax.swing.JMenuItem();
        mitDelete = new javax.swing.JMenuItem();
        sptView = new javax.swing.JSeparator();
        mitSave = new javax.swing.JMenuItem();
        mitCancel = new javax.swing.JMenuItem();
        sptCancel = new javax.swing.JSeparator();
        mitClose = new javax.swing.JMenuItem();

        setClosable(true);
        setIconifiable(true);
        setMaximizable(true);
        setResizable(true);
        setMinimumSize(new java.awt.Dimension(215, 126));
        setPreferredSize(new java.awt.Dimension(520, 490));

        panCheque.setBorder(javax.swing.BorderFactory.createTitledBorder(""));
        panCheque.setLayout(new java.awt.GridBagLayout());

        panChqResults.setBorder(javax.swing.BorderFactory.createTitledBorder("Account Details"));
        panChqResults.setMinimumSize(new java.awt.Dimension(250, 100));
        panChqResults.setName("panTransInfo");
        panChqResults.setPreferredSize(new java.awt.Dimension(250, 100));
        panChqResults.setLayout(new java.awt.GridBagLayout());

        srpChqDetails.setMaximumSize(new java.awt.Dimension(400, 400));
        srpChqDetails.setPreferredSize(new java.awt.Dimension(454, 400));

        tblChqList.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Cust Id", "A/c No", "Name", "Av Bal", "Prod Id", "Series"
            }
        ));
        tblChqList.setMaximumSize(new java.awt.Dimension(300, 300));
        tblChqList.setMinimumSize(new java.awt.Dimension(60, 64));
        tblChqList.setPreferredScrollableViewportSize(new java.awt.Dimension(450, 300));
        tblChqList.setPreferredSize(new java.awt.Dimension(300, 300));
        tblChqList.setOpaque(false);
        srpChqDetails.setViewportView(tblChqList);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        panChqResults.add(srpChqDetails, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.weightx = 1.0;
        panCheque.add(panChqResults, gridBagConstraints);
        panChqResults.getAccessibleContext().setAccessibleName("");

        panChqDetails.setBorder(javax.swing.BorderFactory.createTitledBorder("Chq Details"));
        panChqDetails.setMaximumSize(new java.awt.Dimension(250, 140));
        panChqDetails.setMinimumSize(new java.awt.Dimension(250, 140));
        panChqDetails.setName("panChqDetails");
        panChqDetails.setPreferredSize(new java.awt.Dimension(250, 140));
        panChqDetails.setLayout(new java.awt.GridBagLayout());

        btnSearch.setText("Search");
        btnSearch.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSearchActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        panChqDetails.add(btnSearch, gridBagConstraints);

        lblChqNo.setText("Cheque No.");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panChqDetails.add(lblChqNo, gridBagConstraints);

        panInstrumentNo.setPreferredSize(new java.awt.Dimension(21, 200));
        panInstrumentNo.setLayout(new java.awt.GridBagLayout());

        txtChqNo1.setMinimumSize(new java.awt.Dimension(50, 21));
        txtChqNo1.setPreferredSize(new java.awt.Dimension(50, 21));
        panInstrumentNo.add(txtChqNo1, new java.awt.GridBagConstraints());

        txtIChqNo2.setMinimumSize(new java.awt.Dimension(100, 21));
        panInstrumentNo.add(txtIChqNo2, new java.awt.GridBagConstraints());

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 5, 4, 5);
        panChqDetails.add(panInstrumentNo, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.weightx = 1.0;
        panCheque.add(panChqDetails, gridBagConstraints);
        panChqDetails.getAccessibleContext().setAccessibleName("");

        getContentPane().add(panCheque, java.awt.BorderLayout.CENTER);

        btnCancel.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_CANCEL.gif"))); // NOI18N
        btnCancel.setToolTipText("Cancel");
        btnCancel.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCancelActionPerformed(evt);
            }
        });
        tbrChequeEnq.add(btnCancel);

        lblSpace3.setText("     ");
        tbrChequeEnq.add(lblSpace3);

        btnPrint.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_PRINT.gif"))); // NOI18N
        btnPrint.setToolTipText("Print");
        tbrChequeEnq.add(btnPrint);

        lblSpace17.setForeground(new java.awt.Color(240, 240, 240));
        lblSpace17.setText("     ");
        lblSpace17.setMaximumSize(new java.awt.Dimension(3, 18));
        lblSpace17.setMinimumSize(new java.awt.Dimension(3, 18));
        lblSpace17.setPreferredSize(new java.awt.Dimension(3, 18));
        tbrChequeEnq.add(lblSpace17);

        btnClose.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_CLOSE.gif"))); // NOI18N
        btnClose.setToolTipText("Close");
        btnClose.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCloseActionPerformed(evt);
            }
        });
        tbrChequeEnq.add(btnClose);

        getContentPane().add(tbrChequeEnq, java.awt.BorderLayout.NORTH);

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

        mnuProcess.setText("Process");

        mitNew.setText("New");
        mitNew.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mitNewActionPerformed(evt);
            }
        });
        mnuProcess.add(mitNew);

        mitEdit.setText("Edit");
        mitEdit.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mitEditActionPerformed(evt);
            }
        });
        mnuProcess.add(mitEdit);

        mitDelete.setText("Delete");
        mitDelete.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mitDeleteActionPerformed(evt);
            }
        });
        mnuProcess.add(mitDelete);
        mnuProcess.add(sptView);

        mitSave.setText("Save");
        mitSave.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mitSaveActionPerformed(evt);
            }
        });
        mnuProcess.add(mitSave);

        mitCancel.setText("Cancel");
        mitCancel.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mitCancelActionPerformed(evt);
            }
        });
        mnuProcess.add(mitCancel);
        mnuProcess.add(sptCancel);

        mitClose.setText("Close");
        mitClose.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mitCloseActionPerformed(evt);
            }
        });
        mnuProcess.add(mitClose);

        mbrTokenLoss.add(mnuProcess);

        setJMenuBar(mbrTokenLoss);

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btnSearchActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSearchActionPerformed
        // TODO add your handling code here:
        if(tblCh.getRowCount()>0)
         for(int i = tblCh.getRowCount(); i > 0; i--){
            tblCh.removeRow(i-1);
        }
        String chNo1=txtChqNo1.getText();
        String chqNo2= txtIChqNo2.getText();
        if(chqNo2.length()>0){
            HashMap chqMap = new HashMap();
            if(chNo1.length()>0){
                chqMap.put("CHQ_NO1",chNo1);
            }
            chqMap.put("CHQ_NO2",chqNo2);
            List actList= ClientUtil.executeQuery("getActDetailsForChqNo",chqMap);
            if(actList!=null && actList.size()>0){
                for(int i=0; i<actList.size();i++){
                    HashMap resultMap = new HashMap();
                    resultMap=(HashMap)actList.get(i);
                    ArrayList actListForTable = new ArrayList();
                    actListForTable.add(resultMap.get("CUST_ID"));
                    actListForTable.add(resultMap.get("ACCT_NO"));
                    actListForTable.add(resultMap.get("ACCT_NAMES"));
                    actListForTable.add(resultMap.get("AV_BAL"));
                    actListForTable.add(resultMap.get("PROD_DESC"));
                    actListForTable.add(resultMap.get("SERIES"));
                    tblCh.addRow(actListForTable);
//                    tblCh.fireTableDataChanged();
//                    tblChqList.setModel(getTblCh());
                }
            }
            else{
                ClientUtil.displayAlert("No Relevant Data Found");
            }
        }
        else{
            ClientUtil.displayAlert("Cheque Number Should Not Be Empty");
        }
    }//GEN-LAST:event_btnSearchActionPerformed
            
    private void mitCloseActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mitCloseActionPerformed
        // TODO add your handling code here:
        cifClosingAlert();
    }//GEN-LAST:event_mitCloseActionPerformed
    
    private void mitSaveActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mitSaveActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_mitSaveActionPerformed
    
    private void mitCancelActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mitCancelActionPerformed
        // TODO add your handling code here:
        btnCancelActionPerformed(evt);
    }//GEN-LAST:event_mitCancelActionPerformed
    
    private void mitDeleteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mitDeleteActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_mitDeleteActionPerformed
    
    private void mitEditActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mitEditActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_mitEditActionPerformed
        
    private void mitNewActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mitNewActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_mitNewActionPerformed
    
    private void btnCloseActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCloseActionPerformed
        // TODO add your handling code here:
        cifClosingAlert();
    }//GEN-LAST:event_btnCloseActionPerformed
    
    private void btnCancelActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCancelActionPerformed
        // TODO add your handling code here:
        txtChqNo1.setText("");
        txtIChqNo2.setText("");
        for(int i = tblCh.getRowCount(); i > 0; i--){
            tblCh.removeRow(i-1);
        }
        
    }//GEN-LAST:event_btnCancelActionPerformed

    /**
     * Getter for property tblCh.
     * @return Value of property tblCh.
     */
    public com.see.truetransact.clientutil.EnhancedTableModel getTblCh() {
        return tblCh;
    }    
                            
    /**
     * Setter for property tblCh.
     * @param tblCh New value of property tblCh.
     */
    public void setTblCh(com.see.truetransact.clientutil.EnhancedTableModel tblCh) {
        this.tblCh = tblCh;
    }    
    private void setChequeDetailsTitle(){
        ChqTitle.add("Cust Id");
        ChqTitle.add("A/c No");
        ChqTitle.add("Name");
        ChqTitle.add("Av Bal");
        ChqTitle.add("Prod Id");
        ChqTitle.add("Series");
    }
    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private com.see.truetransact.uicomponent.CButton btnCancel;
    private com.see.truetransact.uicomponent.CButton btnClose;
    private com.see.truetransact.uicomponent.CButton btnPrint;
    private com.see.truetransact.uicomponent.CButton btnSearch;
    private com.see.truetransact.uicomponent.CLabel lblChqNo;
    private com.see.truetransact.uicomponent.CLabel lblMsg;
    private com.see.truetransact.uicomponent.CLabel lblSpace1;
    private com.see.truetransact.uicomponent.CLabel lblSpace17;
    private com.see.truetransact.uicomponent.CLabel lblSpace3;
    private com.see.truetransact.uicomponent.CLabel lblStatus;
    private com.see.truetransact.uicomponent.CMenuBar mbrTokenLoss;
    private javax.swing.JMenuItem mitCancel;
    private javax.swing.JMenuItem mitClose;
    private javax.swing.JMenuItem mitDelete;
    private javax.swing.JMenuItem mitEdit;
    private javax.swing.JMenuItem mitNew;
    private javax.swing.JMenuItem mitSave;
    private javax.swing.JMenu mnuProcess;
    private com.see.truetransact.uicomponent.CPanel panCheque;
    private com.see.truetransact.uicomponent.CPanel panChqDetails;
    private com.see.truetransact.uicomponent.CPanel panChqResults;
    private com.see.truetransact.uicomponent.CPanel panInstrumentNo;
    private com.see.truetransact.uicomponent.CPanel panStatus;
    private javax.swing.JSeparator sptCancel;
    private javax.swing.JSeparator sptView;
    private com.see.truetransact.uicomponent.CScrollPane srpChqDetails;
    private com.see.truetransact.uicomponent.CTable tblChqList;
    private com.see.truetransact.uicomponent.CToolBar tbrChequeEnq;
    private com.see.truetransact.uicomponent.CTextField txtChqNo1;
    private com.see.truetransact.uicomponent.CTextField txtIChqNo2;
    // End of variables declaration//GEN-END:variables
    
}
