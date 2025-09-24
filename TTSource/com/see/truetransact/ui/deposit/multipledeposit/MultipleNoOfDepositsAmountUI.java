/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition. 
 * 
 * MultipleNoOfDepositsAmountUI.java
 *
 * Created on April 13, 2011, 10:10 PM
 */

package com.see.truetransact.ui.deposit.multipledeposit;
import com.see.truetransact.ui.deposit.*;

import java.util.Observer;

import java.util.List;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.HashMap;
import java.util.ArrayList;
import java.awt.Point;
import java.awt.Dimension;
import java.awt.Toolkit;

import com.see.truetransact.uicomponent.CTable;
import com.see.truetransact.ui.TrueTransactMain;
import com.see.truetransact.commonutil.DateUtil;
import com.see.truetransact.clientutil.ClientUtil;
import com.see.truetransact.clientutil.TableModel;
import com.see.truetransact.commonutil.CommonUtil;
import com.see.truetransact.clientutil.TableSorter;
import com.see.truetransact.commonutil.CommonConstants;
import com.see.truetransact.uicomponent.CInternalFrame;
import com.see.truetransact.uimandatory.UIMandatoryField;
import com.see.truetransact.uivalidation.NumericValidation;
import com.see.truetransact.uivalidation.NumericValidation;
import com.see.truetransact.uivalidation.DefaultValidation;
import com.see.truetransact.uivalidation.CurrencyValidation;
import com.see.truetransact.ui.deposit.multipledeposit.MultipleTermDepositUI;

/**
 *
 * @author  Suresh
 */
public class MultipleNoOfDepositsAmountUI extends com.see.truetransact.uicomponent.CDialog implements Observer {
    
    
    //    final CheckCustomerIdRB resourceBundle = new CheckCustomerIdRB();
    private CTable _tblData;
    private HashMap dataHash;
    private ArrayList data;
    private int dataSize;
    private ArrayList _heading;
    private boolean _isAvailable = true;
    private ArrayList termLoanData = new ArrayList();
    Date currDt = null;
    public String branchID;
    private double displayBalance = 0;
    private double totalBalance = 0;
    private double recieptAmount = 0;
    private double paymentAmount = 0;
    private boolean showDueTableFinal = false;
    private boolean hasPenal = false;
    private String noOfDeposits="";
    private String amount="";
    private double penalAmount = 0.0;
    MultipleTermDepositUI termDeposit = null;
    
    //    public LienDetailsUI() {
    public MultipleNoOfDepositsAmountUI() {
        initComponents();
        initForm();
        
    }
    /** Account Number Constructor */
    //    public LienDetailsUI(HashMap termLoanDataMap, String totalAmount, String transType, boolean showDueTable, boolean penalDepositFlag) {
    public MultipleNoOfDepositsAmountUI(MultipleTermDepositUI termDeposit) {
        
        initComponents();
        currDt = ClientUtil.getCurrentDate();
        this.termDeposit=termDeposit;
        System.out.println("%#$^%#$^%termLoanData:"+termLoanData);
        branchID = TrueTransactMain.BRANCH_ID;
        setupScreen();
        txtNoOfDeposits.setAllowAll(true);
        //txtAmount.setValidation(new CurrencyValidation());
        //termDeposit.setDepositAmount(CommonUtil.convertObjToDouble(txtAmount.getText()).doubleValue());
        
    }
    
    /** Method which is used to initialize the form TokenConfig */
    private void initForm(){
        setMaxLengths();
        setFieldNames();
        internationalize();
        currDt = ClientUtil.getCurrentDate();
        //txtAmount.setValidation(new CurrencyValidation());
        txtNoOfDeposits.setAllowAll(true);
        
    }
    
    
    
    
    private void setupScreen() {
        setModal(true);
        setTitle("DepositDetails"+"["+branchID+"]");
        /* Calculate the screen size */
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        System.out.println("@$#@$#@# screenSize : "+screenSize);
        setSize(570, 280);
        /* Center frame on the screen */
        Dimension frameSize = this.getSize();
        if (frameSize.height > screenSize.height) frameSize.height = screenSize.height;
        if (frameSize.width > screenSize.width) frameSize.width = screenSize.width;
        this.setLocation((screenSize.width - frameSize.width) / 2, (screenSize.height - frameSize.height) / 2);
    }
    
    
    
    public ArrayList populateData(ArrayList recievedList, CTable tblData) {
        _tblData = tblData;
        data = new ArrayList();
        _heading = new ArrayList();
        _heading.add("Name");
        _heading.add("Due");
        _heading.add("Received");
        _heading.add("Receivable");
        
        data = recievedList;
        System.out.println("### Data : "+data);
        if(showDueTableFinal){
            populateTable();
            
        }else{
            
        }
        displayBalance();
        //        whereMap = null;
        recievedList = null;
        return _heading;
    }
    
    public void populateTable() {
        boolean dataExist;
        if (_heading != null){
            _isAvailable = true;
            dataExist = true;
            TableSorter tableSorter = new TableSorter();
            tableSorter.addMouseListenerToHeaderInTable(_tblData);
            TableModel tableModel = new TableModel();
            tableModel.setHeading(_heading);
            tableModel.setData(data);
            tableModel.fireTableDataChanged();
            tableSorter.setModel(tableModel);
            tableSorter.fireTableDataChanged();
            _tblData.setAutoResizeMode(0);
            _tblData.doLayout();
            _tblData.setModel(tableSorter);
            _tblData.revalidate();
            calculateTot();
            
            //            _tblData.getColumnModel().getColumn(0).setPreferredWidth(100);
            //            _tblData.getColumnModel().getColumn(1).setPreferredWidth(110);
            
        }
    }
    public void displayBalance(){
        if(!hasPenal){
            
        }else{
            
            
            
            System.out.println("#$%#$%#$%penalAmount"+penalAmount);
        }
    }
    public void calculateTot() {
        double totDue = 0.0;
        double totRecieved = 0.0;
        double totRecievable = 0.0;
        for (int i=0; i<_tblData.getRowCount(); i++) {
            totDue = totDue + CommonUtil.convertObjToDouble(_tblData.getValueAt(i, 1).toString()).doubleValue();
            totRecieved = totRecieved + CommonUtil.convertObjToDouble(_tblData.getValueAt(i, 2).toString()).doubleValue();
            totRecievable = totRecievable + CommonUtil.convertObjToDouble(_tblData.getValueAt(i, 3).toString()).doubleValue();
            
            
        }
    }
    
    /** Used to set Maximum possible lenghts for TextFields */
    private void setMaxLengths(){
        
    }
    
    
    /* Auto Generated Method - setFieldNames()
   This method assigns name for all the components.
   Other functions are working based on this name. */
    private void setFieldNames() {
        panMemberShipFacility.setName("panMemberShipFacility");
    }
/* Auto Generated Method - internationalize()
   This method used to assign display texts from
   the Resource Bundle File. */
    private void internationalize() {
        
    }
    
    public void update(java.util.Observable o, Object arg) {
    }
/* Auto Generated Method - updateOBFields()
   This method called by Save option of UI.
   It updates the OB with UI data.*/
    public void updateOBFields() {
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
        return null;
    }
    
    
    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {
        java.awt.GridBagConstraints gridBagConstraints;

        panMemberShipFacility = new com.see.truetransact.uicomponent.CPanel();
        panTotal1 = new com.see.truetransact.uicomponent.CPanel();
        lblTotalBalance = new com.see.truetransact.uicomponent.CLabel();
        txtNoOfDeposits = new com.see.truetransact.uicomponent.CTextField();
        btnLoanOtherSocietyUnLien = new com.see.truetransact.uicomponent.CButton();

        addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowClosing(java.awt.event.WindowEvent evt) {
                formWindowClosing(evt);
            }
        });
        getContentPane().setLayout(new java.awt.GridBagLayout());

        panMemberShipFacility.setMaximumSize(new java.awt.Dimension(300, 100));
        panMemberShipFacility.setMinimumSize(new java.awt.Dimension(300, 100));
        panMemberShipFacility.setPreferredSize(new java.awt.Dimension(300, 100));
        panMemberShipFacility.setLayout(new java.awt.GridBagLayout());

        panTotal1.setMaximumSize(new java.awt.Dimension(350, 75));
        panTotal1.setMinimumSize(new java.awt.Dimension(350, 70));
        panTotal1.setPreferredSize(new java.awt.Dimension(350, 75));
        panTotal1.setLayout(new java.awt.GridBagLayout());

        lblTotalBalance.setText("No Of Deposits");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panTotal1.add(lblTotalBalance, gridBagConstraints);

        txtNoOfDeposits.setMinimumSize(new java.awt.Dimension(100, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(2, 4, 2, 1);
        panTotal1.add(txtNoOfDeposits, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(0, 38, 3, 41);
        panMemberShipFacility.add(panTotal1, gridBagConstraints);

        btnLoanOtherSocietyUnLien.setText("OK");
        btnLoanOtherSocietyUnLien.setMaximumSize(new java.awt.Dimension(43, 31));
        btnLoanOtherSocietyUnLien.setMinimumSize(new java.awt.Dimension(43, 31));
        btnLoanOtherSocietyUnLien.setPreferredSize(new java.awt.Dimension(65, 31));
        btnLoanOtherSocietyUnLien.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnLoanOtherSocietyUnLienActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        panMemberShipFacility.add(btnLoanOtherSocietyUnLien, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        getContentPane().add(panMemberShipFacility, gridBagConstraints);
        panMemberShipFacility.getAccessibleContext().setAccessibleName("MembershipFacifility");

        pack();
    }// </editor-fold>//GEN-END:initComponents
    
    private void btnLoanOtherSocietyUnLienActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnLoanOtherSocietyUnLienActionPerformed
        // TODO add your handling code here:
        if((!(txtNoOfDeposits.getText().length()>0) || (txtNoOfDeposits == null))){
            ClientUtil.displayAlert("Please Fill No of Deposits");
            return;
        }
        
        if(CommonUtil.convertObjToDouble(txtNoOfDeposits.getText())<=0){
            ClientUtil.displayAlert("No of Deposits should be greater than Zero");
            return;
        }
        
        //termDeposit.setDepositAmount(CommonUtil.convertObjToDouble(txtAmount.getText()).doubleValue());
        termDeposit.setNoOfDeposits(CommonUtil.convertObjToDouble(txtNoOfDeposits.getText()).doubleValue());
        this.dispose();
        
        
    }//GEN-LAST:event_btnLoanOtherSocietyUnLienActionPerformed
    
    
                                    private void formWindowClosed(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_formWindowClosed
                                                                                                                                                                                                                        }//GEN-LAST:event_formWindowClosed
//     private void formWindowClosing(java.awt.event.WindowEvent evt){
//         System.out.println("close");
//         this.dispose();
//     }                               
//                                                                                                
                                                            /** Exit the Application */
    private void exitForm(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_exitForm
                                                                    }//GEN-LAST:event_exitForm

    private void formWindowClosing(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_formWindowClosing
        termDeposit.setIsClosed(true);
        this.dispose();
        
    }//GEN-LAST:event_formWindowClosing
    
    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        //        new CheckCustomerIdUI().show();
    }
    
    /**
     * Getter for property noOfDeposits.
     * @return Value of property noOfDeposits.
     */
    public java.lang.String getNoOfDeposits() {
        return noOfDeposits;
    }
    
    /**
     * Setter for property noOfDeposits.
     * @param noOfDeposits New value of property noOfDeposits.
     */
    public void setNoOfDeposits(java.lang.String noOfDeposits) {
        this.noOfDeposits = noOfDeposits;
    }
    
    /**
     * Getter for property amount.
     * @return Value of property amount.
     */
    public java.lang.String getAmount() {
        return amount;
    }
    
    /**
     * Setter for property amount.
     * @param amount New value of property amount.
     */
    public void setAmount(java.lang.String amount) {
        this.amount = amount;
    }
    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private com.see.truetransact.uicomponent.CButton btnLoanOtherSocietyUnLien;
    private com.see.truetransact.uicomponent.CLabel lblTotalBalance;
    private com.see.truetransact.uicomponent.CPanel panMemberShipFacility;
    private com.see.truetransact.uicomponent.CPanel panTotal1;
    private com.see.truetransact.uicomponent.CTextField txtNoOfDeposits;
    // End of variables declaration//GEN-END:variables
    
}
